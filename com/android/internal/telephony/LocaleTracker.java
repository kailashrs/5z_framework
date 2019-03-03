package com.android.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.LocalLog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.CollectionUtils;
import com.android.internal.util.IndentingPrintWriter;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LocaleTracker
  extends Handler
{
  private static final long CELL_INFO_MAX_DELAY_MS = 600000L;
  private static final long CELL_INFO_MIN_DELAY_MS = 2000L;
  private static final long CELL_INFO_PERIODIC_POLLING_DELAY_MS = 600000L;
  private static final boolean DBG = true;
  private static final int EVENT_GET_CELL_INFO = 1;
  private static final int EVENT_SERVICE_STATE_CHANGED = 3;
  private static final int EVENT_UPDATE_OPERATOR_NUMERIC = 2;
  private static final int MAX_FAIL_COUNT = 30;
  private static final String TAG = LocaleTracker.class.getSimpleName();
  private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      if (("android.telephony.action.SIM_CARD_STATE_CHANGED".equals(paramAnonymousIntent.getAction())) && (paramAnonymousIntent.getIntExtra("phone", 0) == mPhone.getPhoneId())) {
        LocaleTracker.this.onSimCardStateChanged(paramAnonymousIntent.getIntExtra("android.telephony.extra.SIM_STATE", 0));
      }
    }
  };
  private List<CellInfo> mCellInfo;
  private String mCurrentCountryIso;
  private int mFailCellInfoCount;
  private int mLastServiceState = -1;
  private final LocalLog mLocalLog = new LocalLog(50);
  private String mOperatorNumeric;
  private final Phone mPhone;
  private int mSimState;
  
  public LocaleTracker(Phone paramPhone, Looper paramLooper)
  {
    super(paramLooper);
    mPhone = paramPhone;
    mSimState = 0;
    paramPhone = new IntentFilter();
    paramPhone.addAction("android.telephony.action.SIM_CARD_STATE_CHANGED");
    mPhone.getContext().registerReceiver(mBroadcastReceiver, paramPhone);
    mPhone.registerForServiceStateChanged(this, 3, null);
  }
  
  private void getCellInfo()
  {
    if (!mPhone.getServiceStateTracker().getDesiredPowerState())
    {
      if (mCellInfo != null) {
        mCellInfo.clear();
      }
      log("Radio is off. Stopped cell info retry. Cleared the previous cached cell info.");
      mLocalLog.log("Radio is off. Stopped cell info retry. Cleared the previous cached cell info.");
      stopCellInfoRetry();
      return;
    }
    mCellInfo = mPhone.getAllCellInfo(null);
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("getCellInfo: cell info=");
    ((StringBuilder)localObject).append(mCellInfo);
    localObject = ((StringBuilder)localObject).toString();
    log((String)localObject);
    mLocalLog.log((String)localObject);
    if (CollectionUtils.isEmpty(mCellInfo))
    {
      int i = mFailCellInfoCount + 1;
      mFailCellInfoCount = i;
      long l = getCellInfoDelayTime(i);
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Can't get cell info. Try again in ");
      ((StringBuilder)localObject).append(l / 1000L);
      ((StringBuilder)localObject).append(" secs.");
      log(((StringBuilder)localObject).toString());
      removeMessages(1);
      sendMessageDelayed(obtainMessage(1), l);
    }
    else
    {
      stopCellInfoRetry();
      sendMessageDelayed(obtainMessage(1), 600000L);
    }
  }
  
  @VisibleForTesting
  public static long getCellInfoDelayTime(int paramInt)
  {
    return Math.min(Math.max(Math.pow(2.0D, Math.min(paramInt, 30) - 1) * 2000L, 2000L), 600000L);
  }
  
  private String getMccFromCellInfo()
  {
    Object localObject1 = null;
    Object localObject2 = null;
    if (mCellInfo != null)
    {
      HashMap localHashMap = new HashMap();
      int i = 0;
      Iterator localIterator = mCellInfo.iterator();
      for (;;)
      {
        localObject1 = localObject2;
        if (!localIterator.hasNext()) {
          break;
        }
        Object localObject3 = (CellInfo)localIterator.next();
        localObject1 = null;
        if ((localObject3 instanceof CellInfoGsm)) {
          localObject1 = ((CellInfoGsm)localObject3).getCellIdentity().getMccString();
        } else if ((localObject3 instanceof CellInfoLte)) {
          localObject1 = ((CellInfoLte)localObject3).getCellIdentity().getMccString();
        } else if ((localObject3 instanceof CellInfoWcdma)) {
          localObject1 = ((CellInfoWcdma)localObject3).getCellIdentity().getMccString();
        }
        localObject3 = localObject2;
        int j = i;
        if (localObject1 != null)
        {
          int k = 1;
          if (localHashMap.containsKey(localObject1)) {
            k = ((Integer)localHashMap.get(localObject1)).intValue() + 1;
          }
          localHashMap.put(localObject1, Integer.valueOf(k));
          localObject3 = localObject2;
          j = i;
          if (k > i)
          {
            j = k;
            localObject3 = localObject1;
          }
        }
        localObject2 = localObject3;
        i = j;
      }
    }
    return localObject1;
  }
  
  private void log(String paramString)
  {
    Rlog.d(TAG, paramString);
  }
  
  private void loge(String paramString)
  {
    Rlog.e(TAG, paramString);
  }
  
  private void onServiceStateChanged(ServiceState paramServiceState)
  {
    int i = paramServiceState.getState();
    if (i != mLastServiceState)
    {
      if ((i != 3) && (TextUtils.isEmpty(mOperatorNumeric)))
      {
        paramServiceState = new StringBuilder();
        paramServiceState.append("Service state ");
        paramServiceState.append(ServiceState.rilServiceStateToString(i));
        paramServiceState.append(". Get cell info now.");
        paramServiceState = paramServiceState.toString();
        log(paramServiceState);
        mLocalLog.log(paramServiceState);
        getCellInfo();
      }
      else if (i == 3)
      {
        if (mCellInfo != null) {
          mCellInfo.clear();
        }
        stopCellInfoRetry();
      }
      updateLocale();
      mLastServiceState = i;
    }
  }
  
  private void onSimCardStateChanged(int paramInt)
  {
    try
    {
      if ((mSimState != paramInt) && (paramInt == 1))
      {
        log("Sim absent. Get latest cell info from the modem.");
        getCellInfo();
        updateLocale();
      }
      mSimState = paramInt;
      return;
    }
    finally {}
  }
  
  private void stopCellInfoRetry()
  {
    mFailCellInfoCount = 0;
    removeMessages(1);
  }
  
  private void updateLocale()
  {
    Object localObject1 = null;
    String str1 = null;
    Object localObject2 = "";
    Object localObject3 = localObject2;
    if (!TextUtils.isEmpty(mOperatorNumeric))
    {
      localObject1 = str1;
      try
      {
        localObject3 = mOperatorNumeric.substring(0, 3);
        localObject1 = localObject3;
        str1 = MccTable.countryCodeForMcc(Integer.parseInt((String)localObject3));
        localObject2 = str1;
        localObject1 = localObject3;
        localObject3 = localObject2;
      }
      catch (StringIndexOutOfBoundsException|NumberFormatException localStringIndexOutOfBoundsException)
      {
        localObject3 = new StringBuilder();
        ((StringBuilder)localObject3).append("updateLocale: Can't get country from operator numeric. mcc = ");
        ((StringBuilder)localObject3).append((String)localObject1);
        ((StringBuilder)localObject3).append(". ex=");
        ((StringBuilder)localObject3).append(localStringIndexOutOfBoundsException);
        loge(((StringBuilder)localObject3).toString());
        localObject3 = localObject2;
      }
    }
    localObject2 = localObject1;
    localObject1 = localObject3;
    String str3;
    if (TextUtils.isEmpty((CharSequence)localObject3))
    {
      String str2 = getMccFromCellInfo();
      localObject2 = str2;
      localObject1 = localObject3;
      if (!TextUtils.isEmpty(str2)) {
        try
        {
          localObject1 = MccTable.countryCodeForMcc(Integer.parseInt(str2));
          localObject2 = str2;
        }
        catch (NumberFormatException localNumberFormatException)
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("updateLocale: Can't get country from cell info. mcc = ");
          ((StringBuilder)localObject1).append(str2);
          ((StringBuilder)localObject1).append(". ex=");
          ((StringBuilder)localObject1).append(localNumberFormatException);
          loge(((StringBuilder)localObject1).toString());
          localObject1 = localObject3;
          str3 = str2;
        }
      }
    }
    localObject3 = new StringBuilder();
    ((StringBuilder)localObject3).append("updateLocale: mcc = ");
    ((StringBuilder)localObject3).append(str3);
    ((StringBuilder)localObject3).append(", country = ");
    ((StringBuilder)localObject3).append((String)localObject1);
    localObject3 = ((StringBuilder)localObject3).toString();
    log((String)localObject3);
    mLocalLog.log((String)localObject3);
    if (!Objects.equals(localObject1, mCurrentCountryIso))
    {
      localObject3 = new StringBuilder();
      ((StringBuilder)localObject3).append("updateLocale: Change the current country to ");
      ((StringBuilder)localObject3).append((String)localObject1);
      localObject3 = ((StringBuilder)localObject3).toString();
      log((String)localObject3);
      mLocalLog.log((String)localObject3);
      mCurrentCountryIso = ((String)localObject1);
      TelephonyManager.setTelephonyProperty(mPhone.getPhoneId(), "gsm.operator.iso-country", mCurrentCountryIso);
      ((WifiManager)mPhone.getContext().getSystemService("wifi")).setCountryCode((String)localObject1);
    }
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    IndentingPrintWriter localIndentingPrintWriter = new IndentingPrintWriter(paramPrintWriter, "  ");
    paramPrintWriter.println("LocaleTracker:");
    localIndentingPrintWriter.increaseIndent();
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("mOperatorNumeric = ");
    paramPrintWriter.append(mOperatorNumeric);
    localIndentingPrintWriter.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("mSimState = ");
    paramPrintWriter.append(mSimState);
    localIndentingPrintWriter.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("mCellInfo = ");
    paramPrintWriter.append(mCellInfo);
    localIndentingPrintWriter.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("mCurrentCountryIso = ");
    paramPrintWriter.append(mCurrentCountryIso);
    localIndentingPrintWriter.println(paramPrintWriter.toString());
    paramPrintWriter = new StringBuilder();
    paramPrintWriter.append("mFailCellInfoCount = ");
    paramPrintWriter.append(mFailCellInfoCount);
    localIndentingPrintWriter.println(paramPrintWriter.toString());
    localIndentingPrintWriter.println("Local logs:");
    localIndentingPrintWriter.increaseIndent();
    mLocalLog.dump(paramFileDescriptor, localIndentingPrintWriter, paramArrayOfString);
    localIndentingPrintWriter.decreaseIndent();
    localIndentingPrintWriter.decreaseIndent();
    localIndentingPrintWriter.flush();
  }
  
  public String getCurrentCountry()
  {
    try
    {
      String str;
      if (mCurrentCountryIso != null) {
        str = mCurrentCountryIso;
      } else {
        str = "";
      }
      return str;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void handleMessage(Message paramMessage)
  {
    switch (what)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unexpected message arrives. msg = ");
      localStringBuilder.append(what);
      throw new IllegalStateException(localStringBuilder.toString());
    case 3: 
      onServiceStateChanged((ServiceState)obj).result);
      break;
    case 2: 
      updateOperatorNumericSync((String)obj);
      break;
    case 1: 
      
    }
    try
    {
      getCellInfo();
      updateLocale();
      return;
    }
    finally {}
  }
  
  public void updateOperatorNumericAsync(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("updateOperatorNumericAsync. mcc/mnc=");
    localStringBuilder.append(paramString);
    log(localStringBuilder.toString());
    sendMessage(obtainMessage(2, paramString));
  }
  
  public void updateOperatorNumericSync(String paramString)
  {
    try
    {
      Object localObject = new java/lang/StringBuilder;
      ((StringBuilder)localObject).<init>();
      ((StringBuilder)localObject).append("updateOperatorNumericSync. mcc/mnc=");
      ((StringBuilder)localObject).append(paramString);
      log(((StringBuilder)localObject).toString());
      if (!Objects.equals(mOperatorNumeric, paramString))
      {
        localObject = new java/lang/StringBuilder;
        ((StringBuilder)localObject).<init>();
        ((StringBuilder)localObject).append("Operator numeric changes to ");
        ((StringBuilder)localObject).append(paramString);
        localObject = ((StringBuilder)localObject).toString();
        log((String)localObject);
        mLocalLog.log((String)localObject);
        mOperatorNumeric = paramString;
        if (TextUtils.isEmpty(mOperatorNumeric))
        {
          log("Operator numeric unavailable. Get latest cell info from the modem.");
          getCellInfo();
        }
        else
        {
          if (mCellInfo != null) {
            mCellInfo.clear();
          }
          stopCellInfoRetry();
        }
        updateLocale();
      }
      return;
    }
    finally {}
  }
}
