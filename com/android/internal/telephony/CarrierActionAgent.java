package com.android.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.Registrant;
import android.os.RegistrantList;
import android.provider.Settings.Global;
import android.provider.Telephony.Carriers;
import android.telephony.Rlog;
import android.telephony.TelephonyManager;
import android.util.LocalLog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.IndentingPrintWriter;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class CarrierActionAgent
  extends Handler
{
  public static final int CARRIER_ACTION_REPORT_DEFAULT_NETWORK_STATUS = 3;
  public static final int CARRIER_ACTION_RESET = 2;
  public static final int CARRIER_ACTION_SET_METERED_APNS_ENABLED = 0;
  public static final int CARRIER_ACTION_SET_RADIO_ENABLED = 1;
  private static final boolean DBG = true;
  public static final int EVENT_APM_SETTINGS_CHANGED = 4;
  public static final int EVENT_APN_SETTINGS_CHANGED = 8;
  public static final int EVENT_DATA_ROAMING_OFF = 6;
  public static final int EVENT_MOBILE_DATA_SETTINGS_CHANGED = 5;
  public static final int EVENT_SIM_STATE_CHANGED = 7;
  private static final String LOG_TAG = "CarrierActionAgent";
  private static final boolean VDBG = Rlog.isLoggable("CarrierActionAgent", 2);
  private Boolean mCarrierActionOnMeteredApnEnabled = Boolean.valueOf(true);
  private Boolean mCarrierActionOnRadioEnabled = Boolean.valueOf(true);
  private Boolean mCarrierActionReportDefaultNetworkStatus = Boolean.valueOf(false);
  private RegistrantList mDefaultNetworkReportRegistrants = new RegistrantList();
  private RegistrantList mMeteredApnEnableRegistrants = new RegistrantList();
  private LocalLog mMeteredApnEnabledLog = new LocalLog(10);
  private final Phone mPhone;
  private RegistrantList mRadioEnableRegistrants = new RegistrantList();
  private LocalLog mRadioEnabledLog = new LocalLog(10);
  private final BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      String str = paramAnonymousIntent.getAction();
      paramAnonymousContext = paramAnonymousIntent.getStringExtra("ss");
      if ("android.intent.action.SIM_STATE_CHANGED".equals(str))
      {
        if (paramAnonymousIntent.getBooleanExtra("rebroadcastOnUnlock", false)) {
          return;
        }
        sendMessage(obtainMessage(7, paramAnonymousContext));
      }
    }
  };
  private LocalLog mReportDefaultNetworkStatusLog = new LocalLog(10);
  private final SettingsObserver mSettingsObserver;
  
  public CarrierActionAgent(Phone paramPhone)
  {
    mPhone = paramPhone;
    mPhone.getContext().registerReceiver(mReceiver, new IntentFilter("android.intent.action.SIM_STATE_CHANGED"));
    mSettingsObserver = new SettingsObserver(mPhone.getContext(), this);
    log("Creating CarrierActionAgent");
  }
  
  private void carrierActionReset()
  {
    carrierActionReportDefaultNetworkStatus(false);
    carrierActionSetMeteredApnsEnabled(true);
    carrierActionSetRadioEnabled(true);
    mPhone.getCarrierSignalAgent().notifyCarrierSignalReceivers(new Intent("com.android.internal.telephony.CARRIER_SIGNAL_RESET"));
  }
  
  private Boolean getCarrierActionEnabled(int paramInt)
  {
    if (paramInt != 3)
    {
      switch (paramInt)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unsupported action: ");
        localStringBuilder.append(paramInt);
        loge(localStringBuilder.toString());
        return null;
      case 1: 
        return mCarrierActionOnRadioEnabled;
      }
      return mCarrierActionOnMeteredApnEnabled;
    }
    return mCarrierActionReportDefaultNetworkStatus;
  }
  
  private RegistrantList getRegistrantsFromAction(int paramInt)
  {
    if (paramInt != 3)
    {
      switch (paramInt)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unsupported action: ");
        localStringBuilder.append(paramInt);
        loge(localStringBuilder.toString());
        return null;
      case 1: 
        return mRadioEnableRegistrants;
      }
      return mMeteredApnEnableRegistrants;
    }
    return mDefaultNetworkReportRegistrants;
  }
  
  private void log(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(mPhone.getPhoneId());
    localStringBuilder.append("]");
    localStringBuilder.append(paramString);
    Rlog.d("CarrierActionAgent", localStringBuilder.toString());
  }
  
  private void loge(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(mPhone.getPhoneId());
    localStringBuilder.append("]");
    localStringBuilder.append(paramString);
    Rlog.e("CarrierActionAgent", localStringBuilder.toString());
  }
  
  private void logv(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(mPhone.getPhoneId());
    localStringBuilder.append("]");
    localStringBuilder.append(paramString);
    Rlog.v("CarrierActionAgent", localStringBuilder.toString());
  }
  
  public void carrierActionReportDefaultNetworkStatus(boolean paramBoolean)
  {
    sendMessage(obtainMessage(3, Boolean.valueOf(paramBoolean)));
  }
  
  public void carrierActionSetMeteredApnsEnabled(boolean paramBoolean)
  {
    sendMessage(obtainMessage(0, Boolean.valueOf(paramBoolean)));
  }
  
  public void carrierActionSetRadioEnabled(boolean paramBoolean)
  {
    sendMessage(obtainMessage(1, Boolean.valueOf(paramBoolean)));
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    IndentingPrintWriter localIndentingPrintWriter = new IndentingPrintWriter(paramPrintWriter, "  ");
    paramPrintWriter.println(" mCarrierActionOnMeteredApnsEnabled Log:");
    localIndentingPrintWriter.increaseIndent();
    mMeteredApnEnabledLog.dump(paramFileDescriptor, localIndentingPrintWriter, paramArrayOfString);
    localIndentingPrintWriter.decreaseIndent();
    paramPrintWriter.println(" mCarrierActionOnRadioEnabled Log:");
    localIndentingPrintWriter.increaseIndent();
    mRadioEnabledLog.dump(paramFileDescriptor, localIndentingPrintWriter, paramArrayOfString);
    localIndentingPrintWriter.decreaseIndent();
    paramPrintWriter.println(" mCarrierActionReportDefaultNetworkStatus Log:");
    localIndentingPrintWriter.increaseIndent();
    mReportDefaultNetworkStatusLog.dump(paramFileDescriptor, localIndentingPrintWriter, paramArrayOfString);
    localIndentingPrintWriter.decreaseIndent();
  }
  
  @VisibleForTesting
  public ContentObserver getContentObserver()
  {
    return mSettingsObserver;
  }
  
  public void handleMessage(Message paramMessage)
  {
    Object localObject = getCarrierActionEnabled(what);
    if ((localObject != null) && (((Boolean)localObject).booleanValue() == ((Boolean)obj).booleanValue())) {
      return;
    }
    switch (what)
    {
    default: 
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Unknown carrier action: ");
      ((StringBuilder)localObject).append(what);
      loge(((StringBuilder)localObject).toString());
      break;
    case 8: 
      log("EVENT_APN_SETTINGS_CHANGED");
      carrierActionReset();
      break;
    case 7: 
      paramMessage = (String)obj;
      if ("LOADED".equals(paramMessage))
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("EVENT_SIM_STATE_CHANGED status: ");
        ((StringBuilder)localObject).append(paramMessage);
        log(((StringBuilder)localObject).toString());
        carrierActionReset();
        paramMessage = "mobile_data";
        if (TelephonyManager.getDefault().getSimCount() != 1)
        {
          paramMessage = new StringBuilder();
          paramMessage.append("mobile_data");
          paramMessage.append(mPhone.getSubId());
          paramMessage = paramMessage.toString();
        }
        mSettingsObserver.observe(Settings.Global.getUriFor(paramMessage), 5);
        mSettingsObserver.observe(Settings.Global.getUriFor("airplane_mode_on"), 4);
        mSettingsObserver.observe(Telephony.Carriers.CONTENT_URI, 8);
        if (mPhone.getServiceStateTracker() != null) {
          mPhone.getServiceStateTracker().registerForDataRoamingOff(this, 6, null, false);
        }
      }
      else if ("ABSENT".equals(paramMessage))
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("EVENT_SIM_STATE_CHANGED status: ");
        ((StringBuilder)localObject).append(paramMessage);
        log(((StringBuilder)localObject).toString());
        carrierActionReset();
        mSettingsObserver.unobserve();
        if (mPhone.getServiceStateTracker() != null) {
          mPhone.getServiceStateTracker().unregisterForDataRoamingOff(this);
        }
      }
      break;
    case 6: 
      log("EVENT_DATA_ROAMING_OFF");
      carrierActionReset();
      break;
    case 5: 
      log("EVENT_MOBILE_DATA_SETTINGS_CHANGED");
      if (!mPhone.isUserDataEnabled()) {
        carrierActionReset();
      }
      break;
    case 4: 
      log("EVENT_APM_SETTINGS_CHANGED");
      if (Settings.Global.getInt(mPhone.getContext().getContentResolver(), "airplane_mode_on", 0) != 0) {
        carrierActionReset();
      }
      break;
    case 3: 
      mCarrierActionReportDefaultNetworkStatus = Boolean.valueOf(((Boolean)obj).booleanValue());
      paramMessage = new StringBuilder();
      paramMessage.append("CARRIER_ACTION_REPORT_AT_DEFAULT_NETWORK_STATUS: ");
      paramMessage.append(mCarrierActionReportDefaultNetworkStatus);
      log(paramMessage.toString());
      localObject = mReportDefaultNetworkStatusLog;
      paramMessage = new StringBuilder();
      paramMessage.append("REGISTER_DEFAULT_NETWORK_STATUS: ");
      paramMessage.append(mCarrierActionReportDefaultNetworkStatus);
      ((LocalLog)localObject).log(paramMessage.toString());
      mDefaultNetworkReportRegistrants.notifyRegistrants(new AsyncResult(null, mCarrierActionReportDefaultNetworkStatus, null));
      break;
    case 2: 
      log("CARRIER_ACTION_RESET");
      carrierActionReset();
      break;
    case 1: 
      mCarrierActionOnRadioEnabled = Boolean.valueOf(((Boolean)obj).booleanValue());
      paramMessage = new StringBuilder();
      paramMessage.append("SET_RADIO_ENABLED: ");
      paramMessage.append(mCarrierActionOnRadioEnabled);
      log(paramMessage.toString());
      paramMessage = mRadioEnabledLog;
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("SET_RADIO_ENABLED: ");
      ((StringBuilder)localObject).append(mCarrierActionOnRadioEnabled);
      paramMessage.log(((StringBuilder)localObject).toString());
      mRadioEnableRegistrants.notifyRegistrants(new AsyncResult(null, mCarrierActionOnRadioEnabled, null));
      break;
    case 0: 
      mCarrierActionOnMeteredApnEnabled = Boolean.valueOf(((Boolean)obj).booleanValue());
      paramMessage = new StringBuilder();
      paramMessage.append("SET_METERED_APNS_ENABLED: ");
      paramMessage.append(mCarrierActionOnMeteredApnEnabled);
      log(paramMessage.toString());
      localObject = mMeteredApnEnabledLog;
      paramMessage = new StringBuilder();
      paramMessage.append("SET_METERED_APNS_ENABLED: ");
      paramMessage.append(mCarrierActionOnMeteredApnEnabled);
      ((LocalLog)localObject).log(paramMessage.toString());
      mMeteredApnEnableRegistrants.notifyRegistrants(new AsyncResult(null, mCarrierActionOnMeteredApnEnabled, null));
    }
  }
  
  public void registerForCarrierAction(int paramInt1, Handler paramHandler, int paramInt2, Object paramObject, boolean paramBoolean)
  {
    Boolean localBoolean = getCarrierActionEnabled(paramInt1);
    if (localBoolean != null)
    {
      RegistrantList localRegistrantList = getRegistrantsFromAction(paramInt1);
      paramHandler = new Registrant(paramHandler, paramInt2, paramObject);
      localRegistrantList.add(paramHandler);
      if (paramBoolean) {
        paramHandler.notifyRegistrant(new AsyncResult(null, localBoolean, null));
      }
      return;
    }
    paramHandler = new StringBuilder();
    paramHandler.append("invalid carrier action: ");
    paramHandler.append(paramInt1);
    throw new IllegalArgumentException(paramHandler.toString());
  }
  
  public void unregisterForCarrierAction(Handler paramHandler, int paramInt)
  {
    RegistrantList localRegistrantList = getRegistrantsFromAction(paramInt);
    if (localRegistrantList != null)
    {
      localRegistrantList.remove(paramHandler);
      return;
    }
    paramHandler = new StringBuilder();
    paramHandler.append("invalid carrier action: ");
    paramHandler.append(paramInt);
    throw new IllegalArgumentException(paramHandler.toString());
  }
}
