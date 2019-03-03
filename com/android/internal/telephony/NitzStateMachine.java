package com.android.internal.telephony;

import android.content.ContentResolver;
import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemProperties;
import android.provider.Settings.Global;
import android.telephony.Rlog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.LocalLog;
import android.util.TimeUtils;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.metrics.TelephonyMetrics;
import com.android.internal.telephony.util.TimeStampedValue;
import com.android.internal.util.IndentingPrintWriter;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.TimeZone;

public class NitzStateMachine
{
  private static final boolean DBG = true;
  private static final String LOG_TAG = "SST";
  private static final String WAKELOCK_TAG = "NitzStateMachine";
  private final DeviceState mDeviceState;
  private boolean mGotCountryCode = false;
  private TimeStampedValue<NitzData> mLatestNitzSignal;
  private boolean mNeedCountryCodeForNitz = false;
  private boolean mNitzTimeZoneDetectionSuccessful = false;
  private final GsmCdmaPhone mPhone;
  private TimeStampedValue<Long> mSavedNitzTime;
  private String mSavedTimeZoneId;
  private final LocalLog mTimeLog = new LocalLog(15);
  private final TimeServiceHelper mTimeServiceHelper;
  private final LocalLog mTimeZoneLog = new LocalLog(15);
  private final TimeZoneLookupHelper mTimeZoneLookupHelper;
  private TzLocAsus mTzLoc = null;
  private final PowerManager.WakeLock mWakeLock;
  private boolean mWrongNitz = false;
  
  public NitzStateMachine(GsmCdmaPhone paramGsmCdmaPhone)
  {
    this(paramGsmCdmaPhone, new TimeServiceHelper(paramGsmCdmaPhone.getContext()), new DeviceState(paramGsmCdmaPhone), new TimeZoneLookupHelper());
  }
  
  @VisibleForTesting
  public NitzStateMachine(GsmCdmaPhone paramGsmCdmaPhone, TimeServiceHelper paramTimeServiceHelper, DeviceState paramDeviceState, TimeZoneLookupHelper paramTimeZoneLookupHelper)
  {
    mPhone = paramGsmCdmaPhone;
    mWakeLock = ((PowerManager)paramGsmCdmaPhone.getContext().getSystemService("power")).newWakeLock(1, "NitzStateMachine");
    mDeviceState = paramDeviceState;
    mTimeZoneLookupHelper = paramTimeZoneLookupHelper;
    mTimeServiceHelper = paramTimeServiceHelper;
    mTimeServiceHelper.setListener(new TimeServiceHelper.Listener()
    {
      public void onTimeDetectionChange(boolean paramAnonymousBoolean)
      {
        if (paramAnonymousBoolean) {
          NitzStateMachine.this.handleAutoTimeEnabled();
        }
      }
      
      public void onTimeZoneDetectionChange(boolean paramAnonymousBoolean)
      {
        if (paramAnonymousBoolean) {
          NitzStateMachine.this.handleAutoTimeZoneEnabled();
        }
      }
    });
    mTzLoc = new TzLocAsus(paramGsmCdmaPhone, this);
  }
  
  private boolean countryUsesUtc(String paramString, TimeStampedValue<NitzData> paramTimeStampedValue)
  {
    return mTimeZoneLookupHelper.countryUsesUtc(paramString, ((NitzData)mValue).getCurrentTimeInMillis());
  }
  
  private void handleAutoTimeEnabled()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("handleAutoTimeEnabled: Reverting to NITZ Time: mSavedNitzTime=");
    localStringBuilder.append(mSavedNitzTime);
    logd(localStringBuilder.toString());
    if (mSavedNitzTime != null) {
      try
      {
        mWakeLock.acquire();
        long l = mTimeServiceHelper.elapsedRealtime();
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("mSavedNitzTime: Reverting to NITZ time elapsedRealtime=");
        localStringBuilder.append(l);
        localStringBuilder.append(" mSavedNitzTime=");
        localStringBuilder.append(mSavedNitzTime);
        setAndBroadcastNetworkSetTime(localStringBuilder.toString(), ((Long)mSavedNitzTime.mValue).longValue() + (l - mSavedNitzTime.mElapsedRealtime));
        mWakeLock.release();
      }
      finally
      {
        mWakeLock.release();
      }
    }
  }
  
  private void handleAutoTimeZoneEnabled()
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("handleAutoTimeZoneEnabled: Reverting to NITZ TimeZone: mSavedTimeZoneId=");
    ((StringBuilder)localObject).append(mSavedTimeZoneId);
    localObject = ((StringBuilder)localObject).toString();
    logd((String)localObject);
    mTimeZoneLog.log((String)localObject);
    if (mSavedTimeZoneId != null)
    {
      setAndBroadcastNetworkSetTimeZone(mSavedTimeZoneId);
    }
    else
    {
      localObject = mDeviceState.getNetworkCountryIsoForPhone();
      if (!TextUtils.isEmpty((CharSequence)localObject)) {
        updateTimeZoneByNetworkCountryCode((String)localObject);
      }
    }
  }
  
  private void handleTimeFromNitz(TimeStampedValue<NitzData> paramTimeStampedValue)
  {
    try
    {
      if (mDeviceState.getIgnoreNitz())
      {
        logd("handleTimeFromNitz: Not setting clock because gsm.ignore-nitz is set");
        return;
      }
      StringBuilder localStringBuilder;
      try
      {
        mWakeLock.acquire();
        long l1 = mTimeServiceHelper.elapsedRealtime();
        long l2 = l1 - mElapsedRealtime;
        Object localObject1;
        if ((l2 >= 0L) && (l2 <= 2147483647L))
        {
          long l3 = ((NitzData)mValue).getCurrentTimeInMillis() + l2;
          l1 = l3 - mTimeServiceHelper.currentTimeMillis();
          if (mTimeServiceHelper.isTimeDetectionEnabled())
          {
            localObject1 = new java/lang/StringBuilder;
            ((StringBuilder)localObject1).<init>();
            ((StringBuilder)localObject1).append("handleTimeFromNitz: nitzSignal=");
            ((StringBuilder)localObject1).append(paramTimeStampedValue);
            ((StringBuilder)localObject1).append(" adjustedCurrentTimeMillis=");
            ((StringBuilder)localObject1).append(l3);
            ((StringBuilder)localObject1).append(" millisSinceNitzReceived= ");
            ((StringBuilder)localObject1).append(l2);
            ((StringBuilder)localObject1).append(" gained=");
            ((StringBuilder)localObject1).append(l1);
            localObject1 = ((StringBuilder)localObject1).toString();
            if (mSavedNitzTime == null)
            {
              localStringBuilder = new java/lang/StringBuilder;
              localStringBuilder.<init>();
              localStringBuilder.append((String)localObject1);
              localStringBuilder.append(": First update received.");
              setAndBroadcastNetworkSetTime(localStringBuilder.toString(), l3);
            }
            else
            {
              l2 = mTimeServiceHelper.elapsedRealtime();
              long l4 = mSavedNitzTime.mElapsedRealtime;
              int i = mDeviceState.getNitzUpdateSpacingMillis();
              int j = mDeviceState.getNitzUpdateDiffMillis();
              if ((l2 - l4 <= i) && (Math.abs(l1) <= j))
              {
                localStringBuilder = new java/lang/StringBuilder;
                localStringBuilder.<init>();
                localStringBuilder.append((String)localObject1);
                localStringBuilder.append(": Update throttled.");
                logd(localStringBuilder.toString());
                return;
              }
              localStringBuilder = new java/lang/StringBuilder;
              localStringBuilder.<init>();
              localStringBuilder.append((String)localObject1);
              localStringBuilder.append(": New update received.");
              setAndBroadcastNetworkSetTime(localStringBuilder.toString(), l3);
            }
          }
          localObject1 = new com/android/internal/telephony/util/TimeStampedValue;
          ((TimeStampedValue)localObject1).<init>(Long.valueOf(l3), mElapsedRealtime);
          mSavedNitzTime = ((TimeStampedValue)localObject1);
          mWakeLock.release();
        }
        else
        {
          localObject1 = new java/lang/StringBuilder;
          ((StringBuilder)localObject1).<init>();
          ((StringBuilder)localObject1).append("handleTimeFromNitz: not setting time, unexpected elapsedRealtime=");
          ((StringBuilder)localObject1).append(l1);
          ((StringBuilder)localObject1).append(" nitzSignal=");
          ((StringBuilder)localObject1).append(paramTimeStampedValue);
          logd(((StringBuilder)localObject1).toString());
          return;
        }
      }
      finally
      {
        mWakeLock.release();
      }
      return;
    }
    catch (RuntimeException localRuntimeException)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("handleTimeFromNitz: Processing NITZ data nitzSignal=");
      localStringBuilder.append(paramTimeStampedValue);
      localStringBuilder.append(" ex=");
      localStringBuilder.append(localRuntimeException);
      loge(localStringBuilder.toString());
    }
  }
  
  private void handleTimeZoneFromNitz(TimeStampedValue<NitzData> paramTimeStampedValue)
  {
    try
    {
      NitzData localNitzData = (NitzData)mValue;
      String str = mDeviceState.getNetworkCountryIsoForPhone();
      if (localNitzData.getEmulatorHostTimeZone() != null) {
        localObject1 = localNitzData.getEmulatorHostTimeZone().getID();
      }
      for (;;)
      {
        break;
        if (!mGotCountryCode)
        {
          localObject1 = null;
        }
        else
        {
          boolean bool = TextUtils.isEmpty(str);
          localObject1 = null;
          localObject2 = null;
          Object localObject3;
          if (!bool)
          {
            localObject3 = mTimeZoneLookupHelper.lookupByNitzCountry(localNitzData, str);
            localObject1 = localObject2;
            if (localObject3 != null) {
              localObject1 = zoneId;
            }
            if (localObject1 == null)
            {
              logd("Not match tz from lookupByNitzCountry(nitz,iso)");
              mWrongNitz = true;
              return;
            }
          }
          for (;;)
          {
            break;
            localObject2 = mTimeZoneLookupHelper.lookupByNitz(localNitzData);
            localObject3 = new java/lang/StringBuilder;
            ((StringBuilder)localObject3).<init>();
            ((StringBuilder)localObject3).append("handleTimeZoneFromNitz: guessZoneIdByNitz returned lookupResult=");
            ((StringBuilder)localObject3).append(localObject2);
            logd(((StringBuilder)localObject3).toString());
            if (localObject2 != null) {
              localObject1 = zoneId;
            }
          }
        }
      }
      if ((localObject1 == null) || (mLatestNitzSignal == null) || (offsetInfoDiffers(localNitzData, (NitzData)mLatestNitzSignal.mValue)))
      {
        mNeedCountryCodeForNitz = true;
        mLatestNitzSignal = paramTimeStampedValue;
      }
      Object localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>();
      ((StringBuilder)localObject2).append("handleTimeZoneFromNitz: nitzSignal=");
      ((StringBuilder)localObject2).append(paramTimeStampedValue);
      ((StringBuilder)localObject2).append(" zoneId=");
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append(" iso=");
      ((StringBuilder)localObject2).append(str);
      ((StringBuilder)localObject2).append(" mGotCountryCode=");
      ((StringBuilder)localObject2).append(mGotCountryCode);
      ((StringBuilder)localObject2).append(" mNeedCountryCodeForNitz=");
      ((StringBuilder)localObject2).append(mNeedCountryCodeForNitz);
      ((StringBuilder)localObject2).append(" isTimeZoneDetectionEnabled()=");
      ((StringBuilder)localObject2).append(mTimeServiceHelper.isTimeZoneDetectionEnabled());
      localObject2 = ((StringBuilder)localObject2).toString();
      logd((String)localObject2);
      mTimeZoneLog.log((String)localObject2);
      if (localObject1 != null)
      {
        if (mTimeServiceHelper.isTimeZoneDetectionEnabled()) {
          setAndBroadcastNetworkSetTimeZone((String)localObject1);
        }
        mNitzTimeZoneDetectionSuccessful = true;
        mSavedTimeZoneId = ((String)localObject1);
        if (mTzLoc != null) {
          mTzLoc.setTzIsoNow(str);
        }
      }
    }
    catch (RuntimeException localRuntimeException)
    {
      Object localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("handleTimeZoneFromNitz: Processing NITZ data nitzSignal=");
      ((StringBuilder)localObject1).append(paramTimeStampedValue);
      ((StringBuilder)localObject1).append(" ex=");
      ((StringBuilder)localObject1).append(localRuntimeException);
      loge(((StringBuilder)localObject1).toString());
    }
  }
  
  private void log(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append(" [");
    localStringBuilder.append(mPhone.getPhoneId());
    localStringBuilder.append("]");
    Rlog.i("SST", localStringBuilder.toString());
  }
  
  private void logd(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append(" [");
    localStringBuilder.append(mPhone.getPhoneId());
    localStringBuilder.append("]");
    Rlog.d("SST", localStringBuilder.toString());
  }
  
  private void loge(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append(" [");
    localStringBuilder.append(mPhone.getPhoneId());
    localStringBuilder.append("]");
    Rlog.e("SST", localStringBuilder.toString());
  }
  
  private void logw(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append(" [");
    localStringBuilder.append(mPhone.getPhoneId());
    localStringBuilder.append("]");
    Rlog.w("SST", localStringBuilder.toString());
  }
  
  private static boolean nitzOffsetMightBeBogus(NitzData paramNitzData)
  {
    boolean bool;
    if ((paramNitzData.getLocalOffsetMillis() == 0) && (!paramNitzData.isDst())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static boolean offsetInfoDiffers(NitzData paramNitzData1, NitzData paramNitzData2)
  {
    boolean bool;
    if ((paramNitzData1.getLocalOffsetMillis() == paramNitzData2.getLocalOffsetMillis()) && (paramNitzData1.isDst() == paramNitzData2.isDst())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private void setAndBroadcastNetworkSetTime(String paramString, long paramLong)
  {
    if (!mWakeLock.isHeld())
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("setAndBroadcastNetworkSetTime: Wake lock not held while setting device time (msg=");
      localStringBuilder.append(paramString);
      localStringBuilder.append(")");
      logw(localStringBuilder.toString());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("setAndBroadcastNetworkSetTime: [Setting time to time=");
    localStringBuilder.append(paramLong);
    localStringBuilder.append("]:");
    localStringBuilder.append(paramString);
    paramString = localStringBuilder.toString();
    logd(paramString);
    mTimeLog.log(paramString);
    mTimeServiceHelper.setDeviceTime(paramLong);
    TelephonyMetrics.getInstance().writeNITZEvent(mPhone.getPhoneId(), paramLong);
  }
  
  private void setAndBroadcastNetworkSetTimeZone(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("setAndBroadcastNetworkSetTimeZone: zoneId=");
    localStringBuilder.append(paramString);
    logd(localStringBuilder.toString());
    mTimeServiceHelper.setDeviceTimeZone(paramString);
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("setAndBroadcastNetworkSetTimeZone: called setDeviceTimeZone() zoneId=");
    localStringBuilder.append(paramString);
    logd(localStringBuilder.toString());
  }
  
  private void updateTimeZoneByNetworkCountryCode(String paramString)
  {
    TimeZoneLookupHelper.CountryResult localCountryResult = mTimeZoneLookupHelper.lookupByCountry(paramString, mTimeServiceHelper.currentTimeMillis());
    Object localObject;
    if ((localCountryResult != null) && (allZonesHaveSameOffset))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("updateTimeZoneByNetworkCountryCode: set time lookupResult=");
      ((StringBuilder)localObject).append(localCountryResult);
      ((StringBuilder)localObject).append(" iso=");
      ((StringBuilder)localObject).append(paramString);
      localObject = ((StringBuilder)localObject).toString();
      logd((String)localObject);
      mTimeZoneLog.log((String)localObject);
      setAndBroadcastNetworkSetTimeZone(zoneId);
      if (mTzLoc != null) {
        mTzLoc.setTzIsoNow(paramString);
      }
    }
    else
    {
      if (mTzLoc != null)
      {
        localObject = mTzLoc.getTimeZoneId(paramString, zoneId);
        if (!TextUtils.isEmpty((CharSequence)localObject))
        {
          setAndBroadcastNetworkSetTimeZone((String)localObject);
          return;
        }
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("updateTimeZoneByNetworkCountryCode: no good zone for iso=");
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append(" lookupResult=");
      ((StringBuilder)localObject).append(localCountryResult);
      logd(((StringBuilder)localObject).toString());
    }
  }
  
  public void dumpLogs(FileDescriptor paramFileDescriptor, IndentingPrintWriter paramIndentingPrintWriter, String[] paramArrayOfString)
  {
    paramIndentingPrintWriter.println(" Time Logs:");
    paramIndentingPrintWriter.increaseIndent();
    mTimeLog.dump(paramFileDescriptor, paramIndentingPrintWriter, paramArrayOfString);
    paramIndentingPrintWriter.decreaseIndent();
    paramIndentingPrintWriter.println(" Time zone Logs:");
    paramIndentingPrintWriter.increaseIndent();
    mTimeZoneLog.dump(paramFileDescriptor, paramIndentingPrintWriter, paramArrayOfString);
    paramIndentingPrintWriter.decreaseIndent();
  }
  
  public void dumpState(PrintWriter paramPrintWriter)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mSavedTime=");
    localStringBuilder.append(mSavedNitzTime);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mNeedCountryCodeForNitz=");
    localStringBuilder.append(mNeedCountryCodeForNitz);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mLatestNitzSignal=");
    localStringBuilder.append(mLatestNitzSignal);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mGotCountryCode=");
    localStringBuilder.append(mGotCountryCode);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mSavedTimeZoneId=");
    localStringBuilder.append(mSavedTimeZoneId);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mNitzTimeZoneDetectionSuccessful=");
    localStringBuilder.append(mNitzTimeZoneDetectionSuccessful);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mWakeLock=");
    localStringBuilder.append(mWakeLock);
    paramPrintWriter.println(localStringBuilder.toString());
    paramPrintWriter.flush();
  }
  
  public NitzData getCachedNitzData()
  {
    NitzData localNitzData;
    if (mLatestNitzSignal != null) {
      localNitzData = (NitzData)mLatestNitzSignal.mValue;
    } else {
      localNitzData = null;
    }
    return localNitzData;
  }
  
  public boolean getNitzTimeZoneDetectionSuccessful()
  {
    return mNitzTimeZoneDetectionSuccessful;
  }
  
  public String getSavedTimeZoneId()
  {
    return mSavedTimeZoneId;
  }
  
  public void handleNetworkAvailable()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("handleNetworkAvailable: mNitzTimeZoneDetectionSuccessful=");
    localStringBuilder.append(mNitzTimeZoneDetectionSuccessful);
    localStringBuilder.append(", Setting mNitzTimeZoneDetectionSuccessful=false");
    logd(localStringBuilder.toString());
    mNitzTimeZoneDetectionSuccessful = false;
  }
  
  public void handleNetworkCountryCodeSet(boolean paramBoolean)
  {
    mGotCountryCode = true;
    String str1 = mDeviceState.getNetworkCountryIsoForPhone();
    if ((!TextUtils.isEmpty(str1)) && (!mNitzTimeZoneDetectionSuccessful) && (mTimeServiceHelper.isTimeZoneDetectionEnabled())) {
      updateTimeZoneByNetworkCountryCode(str1);
    }
    if ((paramBoolean) || (mNeedCountryCodeForNitz))
    {
      paramBoolean = mTimeServiceHelper.isTimeZoneSettingInitialized();
      Object localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("handleNetworkCountryCodeSet: isTimeZoneSettingInitialized=");
      ((StringBuilder)localObject1).append(paramBoolean);
      ((StringBuilder)localObject1).append(" mLatestNitzSignal=");
      ((StringBuilder)localObject1).append(mLatestNitzSignal);
      ((StringBuilder)localObject1).append(" isoCountryCode=");
      ((StringBuilder)localObject1).append(str1);
      logd(((StringBuilder)localObject1).toString());
      boolean bool = TextUtils.isEmpty(str1);
      localObject1 = null;
      Object localObject3 = null;
      Object localObject4;
      String str2;
      if ((bool) && (mNeedCountryCodeForNitz))
      {
        localObject4 = mTimeZoneLookupHelper.lookupByNitz((NitzData)mLatestNitzSignal.mValue);
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("handleNtworkCountryCodeSet: guessZoneIdByNitz() returned lookupResult=");
        ((StringBuilder)localObject1).append(localObject4);
        logd(((StringBuilder)localObject1).toString());
        localObject1 = localObject3;
        if (localObject4 != null) {
          localObject1 = zoneId;
        }
      }
      else if (mLatestNitzSignal == null)
      {
        localObject1 = null;
        logd("handleNetworkCountryCodeSet: No cached NITZ data available, not setting zone");
      }
      else if ((nitzOffsetMightBeBogus((NitzData)mLatestNitzSignal.mValue)) && (paramBoolean) && (!countryUsesUtc(str1, mLatestNitzSignal)))
      {
        localObject3 = TimeZone.getDefault();
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("handleNetworkCountryCodeSet: NITZ looks bogus, maybe using current default zone to adjust the system clock, mNeedCountryCodeForNitz=");
        ((StringBuilder)localObject1).append(mNeedCountryCodeForNitz);
        ((StringBuilder)localObject1).append(" mLatestNitzSignal=");
        ((StringBuilder)localObject1).append(mLatestNitzSignal);
        ((StringBuilder)localObject1).append(" zone=");
        ((StringBuilder)localObject1).append(localObject3);
        logd(((StringBuilder)localObject1).toString());
        localObject1 = ((TimeZone)localObject3).getID();
        if (mNeedCountryCodeForNitz)
        {
          localObject4 = (NitzData)mLatestNitzSignal.mValue;
          try
          {
            mWakeLock.acquire();
            long l1 = ((NitzData)localObject4).getCurrentTimeInMillis();
            long l2 = mTimeServiceHelper.elapsedRealtime() - mLatestNitzSignal.mElapsedRealtime + l1;
            l1 = ((TimeZone)localObject3).getOffset(l2);
            localObject3 = new java/lang/StringBuilder;
            ((StringBuilder)localObject3).<init>();
            ((StringBuilder)localObject3).append("handleNetworkCountryCodeSet: tzOffset=");
            ((StringBuilder)localObject3).append(l1);
            ((StringBuilder)localObject3).append(" delayAdjustedCtm=");
            ((StringBuilder)localObject3).append(TimeUtils.logTimeOfDay(l2));
            logd(((StringBuilder)localObject3).toString());
            if (mTimeServiceHelper.isTimeDetectionEnabled())
            {
              l1 = l2 - l1;
              localObject3 = new java/lang/StringBuilder;
              ((StringBuilder)localObject3).<init>();
              ((StringBuilder)localObject3).append("handleNetworkCountryCodeSet: setting time timeZoneAdjustedCtm=");
              ((StringBuilder)localObject3).append(TimeUtils.logTimeOfDay(l1));
              setAndBroadcastNetworkSetTime(((StringBuilder)localObject3).toString(), l1);
            }
            else
            {
              localObject3 = new com/android/internal/telephony/util/TimeStampedValue;
              ((TimeStampedValue)localObject3).<init>(Long.valueOf(((Long)mSavedNitzTime.mValue).longValue() - l1), mSavedNitzTime.mElapsedRealtime);
              mSavedNitzTime = ((TimeStampedValue)localObject3);
              localObject3 = new java/lang/StringBuilder;
              ((StringBuilder)localObject3).<init>();
              ((StringBuilder)localObject3).append("handleNetworkCountryCodeSet:adjusting time mSavedNitzTime=");
              ((StringBuilder)localObject3).append(mSavedNitzTime);
              logd(((StringBuilder)localObject3).toString());
            }
          }
          finally
          {
            mWakeLock.release();
          }
        }
      }
      else
      {
        NitzData localNitzData = (NitzData)mLatestNitzSignal.mValue;
        localObject3 = mTimeZoneLookupHelper.lookupByNitzCountry(localNitzData, str1);
        localObject4 = new StringBuilder();
        ((StringBuilder)localObject4).append("handleNetworkCountryCodeSet: using guessZoneIdByNitzCountry(nitzData, isoCountryCode), nitzData=");
        ((StringBuilder)localObject4).append(localNitzData);
        ((StringBuilder)localObject4).append(" isoCountryCode=");
        ((StringBuilder)localObject4).append(str1);
        ((StringBuilder)localObject4).append(" lookupResult=");
        ((StringBuilder)localObject4).append(localObject3);
        logd(((StringBuilder)localObject4).toString());
        if (localObject3 != null) {
          str2 = zoneId;
        }
      }
      localObject3 = new StringBuilder();
      ((StringBuilder)localObject3).append("handleNetworkCountryCodeSet: isTimeZoneSettingInitialized=");
      ((StringBuilder)localObject3).append(paramBoolean);
      ((StringBuilder)localObject3).append(" mLatestNitzSignal=");
      ((StringBuilder)localObject3).append(mLatestNitzSignal);
      ((StringBuilder)localObject3).append(" isoCountryCode=");
      ((StringBuilder)localObject3).append(str1);
      ((StringBuilder)localObject3).append(" mNeedCountryCodeForNitz=");
      ((StringBuilder)localObject3).append(mNeedCountryCodeForNitz);
      ((StringBuilder)localObject3).append(" zoneId=");
      ((StringBuilder)localObject3).append(str2);
      localObject3 = ((StringBuilder)localObject3).toString();
      mTimeZoneLog.log((String)localObject3);
      if (str2 != null)
      {
        localObject3 = new StringBuilder();
        ((StringBuilder)localObject3).append("handleNetworkCountryCodeSet: zoneId != null, zoneId=");
        ((StringBuilder)localObject3).append(str2);
        logd(((StringBuilder)localObject3).toString());
        if (mTimeServiceHelper.isTimeZoneDetectionEnabled()) {
          setAndBroadcastNetworkSetTimeZone(str2);
        } else {
          logd("handleNetworkCountryCodeSet: skip changing zone as isTimeZoneDetectionEnabled() is false");
        }
        if (mNeedCountryCodeForNitz) {
          mSavedTimeZoneId = str2;
        }
      }
      else
      {
        logd("handleNetworkCountryCodeSet: lookupResult == null, do nothing");
      }
      mNeedCountryCodeForNitz = false;
    }
  }
  
  public void handleNetworkUnavailable()
  {
    logd("handleNetworkUnavailable");
    mGotCountryCode = false;
    mNitzTimeZoneDetectionSuccessful = false;
  }
  
  public void handleNitzReceived(TimeStampedValue<NitzData> paramTimeStampedValue)
  {
    mWrongNitz = false;
    handleTimeZoneFromNitz(paramTimeStampedValue);
    if (mWrongNitz)
    {
      logd("wrong nitz receive skip handleTimeFromNitz");
      return;
    }
    handleTimeFromNitz(paramTimeStampedValue);
  }
  
  public static class DeviceState
  {
    private static final int NITZ_UPDATE_DIFF_DEFAULT = 2000;
    private static final int NITZ_UPDATE_SPACING_DEFAULT = 600000;
    private final ContentResolver mCr;
    private final int mNitzUpdateDiff;
    private final int mNitzUpdateSpacing;
    private final GsmCdmaPhone mPhone;
    private final TelephonyManager mTelephonyManager;
    
    public DeviceState(GsmCdmaPhone paramGsmCdmaPhone)
    {
      mPhone = paramGsmCdmaPhone;
      paramGsmCdmaPhone = paramGsmCdmaPhone.getContext();
      mTelephonyManager = ((TelephonyManager)paramGsmCdmaPhone.getSystemService("phone"));
      mCr = paramGsmCdmaPhone.getContentResolver();
      mNitzUpdateSpacing = SystemProperties.getInt("ro.nitz_update_spacing", 600000);
      mNitzUpdateDiff = SystemProperties.getInt("ro.nitz_update_diff", 2000);
    }
    
    public boolean getIgnoreNitz()
    {
      String str = SystemProperties.get("gsm.ignore-nitz");
      boolean bool;
      if ((str != null) && (str.equals("yes"))) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public String getNetworkCountryIsoForPhone()
    {
      return mTelephonyManager.getNetworkCountryIsoForPhone(mPhone.getPhoneId());
    }
    
    public int getNitzUpdateDiffMillis()
    {
      return Settings.Global.getInt(mCr, "nitz_update_diff", mNitzUpdateDiff);
    }
    
    public int getNitzUpdateSpacingMillis()
    {
      return Settings.Global.getInt(mCr, "nitz_update_spacing", mNitzUpdateSpacing);
    }
  }
}
