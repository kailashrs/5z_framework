package com.android.internal.telephony;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.UserManager;
import android.provider.Settings.Global;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.SubscriptionManager.OnSubscriptionsChangedListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import java.util.Iterator;
import java.util.List;

public class TzLocAsus
  extends Handler
{
  private static final String ACTION_POLL = "asus.tzloc.gps.POLL";
  private static final String ACTION_TZLOC_FAIL = "asus.tzloc.gps.fail";
  private static final boolean DBG = Build.IS_DEBUGGABLE;
  private static final int EVENT_LOCATION_CHANGE = 1;
  private static final int EVENT_LOCATION_RETRY = 3;
  private static final int EVENT_POLL_GPS_TIME = 2;
  private static final int EVENT_RADIO_OFF_OR_NOT_AVAILABLE = 4;
  private static final int EVENT_TZLOC_FAIL = 5;
  private static final long FRESH_THRESHOLD = 600000L;
  private static int GPS_RETRY_DELAY_MS = 0;
  private static int GPS_RETRY_MAX = 0;
  private static final String LOG_TAG = "TzLoc";
  private static int POLL_REQUEST = 0;
  private static int TZLOC_FAIL_WAIT_MS;
  private static int[] sSubId = { -1, -1 };
  private static String[] sTzIsoFail;
  private static String sTzIsoSuccess = "";
  private float mAccuracy;
  private AlarmManager mAlarmManager;
  private Context mContext;
  private int mGpsRetryTimes = GPS_RETRY_MAX;
  private boolean mIsGpsRetry = false;
  private boolean mIsLocReq = false;
  private boolean mIsTzFailMsg = false;
  private boolean mIsTzLocFail = false;
  private String mLastTzIsoFromLoc;
  private final LocationListener mLocationListener = new LocationListener()
  {
    public void onLocationChanged(Location paramAnonymousLocation)
    {
      TzLocAsus.access$702(TzLocAsus.this, false);
      sendMessage(obtainMessage(1, paramAnonymousLocation));
    }
    
    public void onProviderDisabled(String paramAnonymousString) {}
    
    public void onProviderEnabled(String paramAnonymousString) {}
    
    public void onStatusChanged(String paramAnonymousString, int paramAnonymousInt, Bundle paramAnonymousBundle) {}
  };
  private LocationManager mLocationManager = null;
  private PendingIntent mPendingPollIntent;
  protected GsmCdmaPhone mPhone;
  private int mPhoneId;
  private int mPreviousSubId = -1;
  private NitzStateMachine mStateMachine;
  private SubscriptionManager mSubManager;
  private TelephonyManager mTelephonyManager;
  private UserManager mUserManager;
  
  static
  {
    GPS_RETRY_MAX = 4;
    GPS_RETRY_DELAY_MS = 30000;
    TZLOC_FAIL_WAIT_MS = 30000;
    sTzIsoFail = new String[] { "", "" };
  }
  
  public TzLocAsus(GsmCdmaPhone paramGsmCdmaPhone, NitzStateMachine paramNitzStateMachine)
  {
    mPhone = paramGsmCdmaPhone;
    mPhoneId = mPhone.getPhoneId();
    mContext = mPhone.getContext();
    mStateMachine = paramNitzStateMachine;
    mAccuracy = 2000.0F;
    mPhone.registerForRadioOffOrNotAvailable(this, 4, null);
    mLocationManager = ((LocationManager)mContext.getSystemService("location"));
    mTelephonyManager = TelephonyManager.from(mContext);
    mUserManager = ((UserManager)mContext.getSystemService("user"));
    mAlarmManager = ((AlarmManager)mContext.getSystemService("alarm"));
    mSubManager = SubscriptionManager.from(mPhone.getContext());
    paramGsmCdmaPhone = new StringBuilder();
    paramGsmCdmaPhone.append("asus.tzloc.gps.POLL");
    paramGsmCdmaPhone.append(mPhoneId);
    paramGsmCdmaPhone = paramGsmCdmaPhone.toString();
    paramNitzStateMachine = new Intent(paramGsmCdmaPhone, null);
    mPendingPollIntent = PendingIntent.getBroadcast(mContext, POLL_REQUEST, paramNitzStateMachine, 0);
    registerForAlarms(paramGsmCdmaPhone);
    mSubManager.addOnSubscriptionsChangedListener(new SubscriptionManager.OnSubscriptionsChangedListener()
    {
      public void onSubscriptionsChanged()
      {
        int i = mPhone.getSubId();
        int j = SubscriptionManager.getSimStateForSlotIndex(mPhoneId);
        if (mPreviousSubId != i) {
          if (mSubManager.isActiveSubId(i))
          {
            TzLocAsus localTzLocAsus = TzLocAsus.this;
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("sim is change: ");
            localStringBuilder.append(i);
            localTzLocAsus.logd(localStringBuilder.toString());
            TzLocAsus.this.resetAll(false);
            TzLocAsus.this.cancelIntent();
            TzLocAsus.access$202(TzLocAsus.this, i);
          }
          else if ((mIsTzFailMsg) && (1 == j))
          {
            TzLocAsus.this.logd("sim absent, clean pendding intent");
            TzLocAsus.access$602(TzLocAsus.this, false);
            removeMessages(5);
          }
        }
      }
    });
  }
  
  private void cancelIntent()
  {
    if (mIsTzFailMsg) {
      removeMessages(5);
    }
  }
  
  private void checkFailNotify()
  {
    if (!mIsTzLocFail) {
      return;
    }
    if (!isAutoTimeZoneEnabled()) {
      return;
    }
    if ((mPhone.getServiceState().getVoiceRegState() != 0) && (mPhone.getServiceState().getDataRegState() != 0))
    {
      logd("no service ignore the notification");
      return;
    }
    Object localObject = mTelephonyManager.getNetworkCountryIsoForPhone(mPhoneId);
    int i = getSubId(mPhoneId);
    if (TextUtils.isEmpty((CharSequence)localObject))
    {
      logd("getNetworkCountryIsoForPhone is Empty");
      return;
    }
    if (((String)localObject).equals(sTzIsoSuccess))
    {
      logd("already success, then skip");
      return;
    }
    if ((((String)localObject).equals(sTzIsoFail[mPhoneId])) && (i == sSubId[mPhoneId]))
    {
      logd("already notify fail, then skip");
      return;
    }
    if (mIsTzFailMsg)
    {
      logd("already pendding intent, then skip");
      return;
    }
    mIsTzFailMsg = sendMessageDelayed(obtainMessage(5, localObject), TZLOC_FAIL_WAIT_MS);
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Queue EVENT_TZLOC_FAIL delay ");
    ((StringBuilder)localObject).append(TZLOC_FAIL_WAIT_MS / 1000);
    logd(((StringBuilder)localObject).toString());
  }
  
  private Location getLastKnownLocation(float paramFloat)
  {
    Object localObject1 = mLocationManager.getAllProviders();
    Object localObject2 = null;
    long l1 = System.currentTimeMillis();
    Iterator localIterator = ((List)localObject1).iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      Location localLocation = mLocationManager.getLastKnownLocation(str);
      localObject1 = localObject2;
      float f1 = paramFloat;
      if (localLocation != null)
      {
        float f2 = localLocation.getAccuracy();
        long l2 = localLocation.getTime();
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("loc ");
        ((StringBuilder)localObject1).append(str);
        ((StringBuilder)localObject1).append(" acc ");
        ((StringBuilder)localObject1).append(f2);
        ((StringBuilder)localObject1).append(" time ");
        ((StringBuilder)localObject1).append(l2);
        ((StringBuilder)localObject1).append(" Lat ");
        ((StringBuilder)localObject1).append(localLocation.getLatitude());
        ((StringBuilder)localObject1).append(" Lng ");
        ((StringBuilder)localObject1).append(localLocation.getLongitude());
        logd(((StringBuilder)localObject1).toString());
        localObject1 = localObject2;
        f1 = paramFloat;
        if (l1 - l2 < 600000L)
        {
          localObject1 = localObject2;
          f1 = paramFloat;
          if (f2 < paramFloat)
          {
            localObject1 = localLocation;
            f1 = f2;
          }
        }
      }
      localObject2 = localObject1;
      paramFloat = f1;
    }
    if (localObject2 != null)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("found location Lat ");
      ((StringBuilder)localObject1).append(localObject2.getLatitude());
      ((StringBuilder)localObject1).append(" Lng ");
      ((StringBuilder)localObject1).append(localObject2.getLongitude());
      logd(((StringBuilder)localObject1).toString());
    }
    return localObject2;
  }
  
  private int getSubId(int paramInt)
  {
    int[] arrayOfInt = SubscriptionManager.getSubId(paramInt);
    int i = -1;
    paramInt = i;
    if (arrayOfInt != null)
    {
      paramInt = i;
      if (arrayOfInt.length >= 1) {
        paramInt = arrayOfInt[0];
      }
    }
    return paramInt;
  }
  
  private String getTimeZone(String paramString)
  {
    if (!mUserManager.isUserUnlocked())
    {
      log("Device is locked");
      return null;
    }
    try
    {
      localObject1 = getLastKnownLocation(mAccuracy);
      if (localObject1 == null)
      {
        if (paramString.equals(mLastTzIsoFromLoc)) {
          return null;
        }
        requestLocationAsus();
        return null;
      }
      resetAll(false);
      localObject1 = TimezoneMapper.latLngToTimezoneString(((Location)localObject1).getLatitude(), ((Location)localObject1).getLongitude());
      if (TextUtils.isEmpty((CharSequence)localObject1))
      {
        loge("Empty timezone");
        return null;
      }
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("tz = ");
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append(" iso = ");
      ((StringBuilder)localObject2).append(paramString);
      logd(((StringBuilder)localObject2).toString());
      localObject2 = DefaultTimeZoneAsus.getTimeZonesAsus(paramString).iterator();
      while (((Iterator)localObject2).hasNext()) {
        if (((String)localObject1).equalsIgnoreCase(((android.icu.util.TimeZone)((Iterator)localObject2).next()).getID()))
        {
          mLastTzIsoFromLoc = paramString;
          return localObject1;
        }
      }
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append(" isn't @ ");
      ((StringBuilder)localObject2).append(paramString);
      log(((StringBuilder)localObject2).toString());
      return null;
    }
    catch (Exception paramString)
    {
      Object localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("getLastKnownLocation ");
      ((StringBuilder)localObject1).append(paramString);
      loge(((StringBuilder)localObject1).toString());
    }
    return null;
  }
  
  private boolean isAutoTimeZoneEnabled()
  {
    boolean bool = true;
    try
    {
      int i = Settings.Global.getInt(mContext.getContentResolver(), "auto_time_zone");
      if (i <= 0) {
        bool = false;
      }
      return bool;
    }
    catch (Exception localException) {}
    return true;
  }
  
  private void log(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append(" [");
    localStringBuilder.append(mPhoneId);
    localStringBuilder.append("]");
    Rlog.i("TzLoc", localStringBuilder.toString());
  }
  
  private void logd(String paramString)
  {
    if (DBG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append(" [");
      localStringBuilder.append(mPhoneId);
      localStringBuilder.append("]");
      Rlog.d("TzLoc", localStringBuilder.toString());
    }
  }
  
  private void loge(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append(" [");
    localStringBuilder.append(mPhoneId);
    localStringBuilder.append("]");
    Rlog.e("TzLoc", localStringBuilder.toString());
  }
  
  private void registerForAlarms(String paramString)
  {
    mContext.registerReceiver(new BroadcastReceiver()new IntentFilter
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        sendMessage(obtainMessage(2));
      }
    }, new IntentFilter(paramString));
  }
  
  private void requestLocationAsus()
  {
    if (mIsLocReq) {
      return;
    }
    if (mLocationManager.getProvider("fused") == null)
    {
      if (mIsGpsRetry) {
        return;
      }
      if (mGpsRetryTimes > 0)
      {
        mGpsRetryTimes -= 1;
        mIsGpsRetry = sendMessageDelayed(obtainMessage(3), GPS_RETRY_DELAY_MS);
        logd("sendMessageDelayed EVENT_LOCATION_RETRY");
      }
      else
      {
        sendFailNotify();
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Can't get Location Provider: ");
        localStringBuilder.append("fused");
        logd(localStringBuilder.toString());
      }
      return;
    }
    resetAlarm(90000L);
    if (mIsGpsRetry)
    {
      mIsGpsRetry = false;
      removeMessages(3);
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Best Available Location Provider: ");
    localStringBuilder.append("fused");
    log(localStringBuilder.toString());
    mLocationManager.requestSingleUpdate("fused", mLocationListener, getLooper());
    mIsLocReq = true;
  }
  
  private void resetAlarm(long paramLong)
  {
    mAlarmManager.cancel(mPendingPollIntent);
    long l = SystemClock.elapsedRealtime();
    mAlarmManager.setExactAndAllowWhileIdle(3, l + paramLong, mPendingPollIntent);
  }
  
  private void resetAll(boolean paramBoolean)
  {
    try
    {
      if (mIsLocReq)
      {
        mLocationManager.removeUpdates(mLocationListener);
        mAlarmManager.cancel(mPendingPollIntent);
      }
      if ((mIsTzFailMsg) && ((!TextUtils.isEmpty(sTzIsoSuccess)) || (paramBoolean))) {
        removeMessages(5);
      }
      mIsTzFailMsg = false;
      mIsLocReq = false;
      mGpsRetryTimes = GPS_RETRY_MAX;
      mIsGpsRetry = false;
      mIsTzLocFail = false;
      removeMessages(3);
      if (paramBoolean)
      {
        mAccuracy = 2000.0F;
        mLastTzIsoFromLoc = null;
      }
    }
    catch (Exception localException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("resetAll err ");
      localStringBuilder.append(localException);
      loge(localStringBuilder.toString());
    }
  }
  
  private void sendFailNotify()
  {
    mIsTzLocFail = true;
    checkFailNotify();
  }
  
  public String getTimeZoneId(String paramString1, String paramString2)
  {
    if (TextUtils.isEmpty(paramString1)) {
      return null;
    }
    String str = getTimeZone(paramString1);
    Object localObject = str;
    if (TextUtils.isEmpty(str))
    {
      paramString2 = DefaultTimeZoneAsus.getDefaultTimeZone(paramString1, java.util.TimeZone.getDefault().getID(), paramString2);
      localObject = paramString2;
      if (!TextUtils.isEmpty(paramString2))
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("get timezone ");
        ((StringBuilder)localObject).append(paramString2);
        ((StringBuilder)localObject).append(" @ ");
        ((StringBuilder)localObject).append(paramString1);
        logd(((StringBuilder)localObject).toString());
        localObject = paramString2;
      }
    }
    if (!TextUtils.isEmpty((CharSequence)localObject))
    {
      sTzIsoSuccess = paramString1;
      cancelIntent();
    }
    else
    {
      checkFailNotify();
    }
    return localObject;
  }
  
  public void handleMessage(Message paramMessage)
  {
    int i = what;
    int j = 0;
    switch (i)
    {
    default: 
      ??? = new StringBuilder();
      ((StringBuilder)???).append("Unhandled message with number: ");
      ((StringBuilder)???).append(what);
      loge(((StringBuilder)???).toString());
      break;
    case 5: 
      paramMessage = (String)obj;
      mIsTzFailMsg = false;
      i = getSubId(mPhoneId);
      if (!SubscriptionController.getInstance().isActiveSubId(i))
      {
        paramMessage = new StringBuilder();
        paramMessage.append("invalid subId: ");
        paramMessage.append(i);
        paramMessage.append(", then skip");
        logd(paramMessage.toString());
      }
      else
      {
        mIsTzLocFail = false;
        if (paramMessage.equals(sTzIsoSuccess))
        {
          logd("already success, then skip");
        }
        else
        {
          if ((paramMessage.equals(sTzIsoFail[mPhoneId])) && (i == sSubId[mPhoneId]))
          {
            logd("already notify fail, then skip.");
            return;
          }
          synchronized (sTzIsoFail)
          {
            while (j < sTzIsoFail.length)
            {
              i = getSubId(j);
              sTzIsoFail[j] = paramMessage;
              sSubId[j] = i;
              StringBuilder localStringBuilder = new java/lang/StringBuilder;
              localStringBuilder.<init>();
              localStringBuilder.append("setup iso:");
              localStringBuilder.append(paramMessage);
              localStringBuilder.append(", subId:");
              localStringBuilder.append(i);
              logd(localStringBuilder.toString());
              j++;
            }
            ??? = new Intent("asus.tzloc.gps.fail");
            ((Intent)???).addFlags(536870912);
            SubscriptionManager.putPhoneIdAndSubIdExtra((Intent)???, mPhoneId);
            ((Intent)???).putExtra("iso", paramMessage);
            mContext.sendBroadcast((Intent)???);
            ??? = new StringBuilder();
            ((StringBuilder)???).append("broadcast ACTION_TZLOC_FAIL with iso ");
            ((StringBuilder)???).append(paramMessage);
            log(((StringBuilder)???).toString());
          }
        }
      }
      break;
    case 4: 
      resetAll(true);
      break;
    case 3: 
      mIsGpsRetry = false;
      requestLocationAsus();
      break;
    case 2: 
      mLocationManager.removeUpdates(mLocationListener);
      log("gps polling timeout");
      mAccuracy = 20000.0F;
      if (getLastKnownLocation(mAccuracy) != null) {
        mStateMachine.handleNetworkCountryCodeSet(false);
      } else {
        sendFailNotify();
      }
      break;
    case 1: 
      mLocationManager.removeUpdates(mLocationListener);
      mAlarmManager.cancel(mPendingPollIntent);
      if (!(obj instanceof Location)) {
        return;
      }
      ??? = (Location)obj;
      if (System.currentTimeMillis() - ((Location)???).getTime() < 600000L)
      {
        paramMessage = new StringBuilder();
        paramMessage.append("fix location Lat ");
        paramMessage.append(((Location)???).getLatitude());
        paramMessage.append(" Lng ");
        paramMessage.append(((Location)???).getLongitude());
        log(paramMessage.toString());
      }
      mStateMachine.handleNetworkCountryCodeSet(false);
    }
  }
  
  public void setTzIsoNow(String paramString)
  {
    if (TextUtils.isEmpty(paramString)) {
      return;
    }
    sTzIsoSuccess = paramString;
    resetAll(false);
  }
}
