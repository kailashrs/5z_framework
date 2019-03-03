package com.android.internal.telephony;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.Registrant;
import android.os.RegistrantList;
import android.telephony.Rlog;
import android.telephony.TelephonyManager;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class BaseCommands
  implements CommandsInterface
{
  protected static final String EVT_CALL = "[RIL] CALL ";
  protected static final String EVT_DATA_REG = "[RIL] CGREG ";
  protected static final String EVT_PLMN = "[RIL] COPS ";
  protected static final String EVT_RADIO_STATE = "[RIL] radio_power ";
  protected static final String EVT_SIGNAL = "[RIL] SIG ";
  protected static final String EVT_SMS = "[RIL] SMS ";
  protected static final String EVT_VOICE_REG = "[RIL] CREG ";
  protected static final String LOG_PROC = "/proc/asusevtlog";
  static final String LOG_TAG = "BaseCommands";
  private static final String PROPERTY_BASEBAND_VERSION = "gsm.version.baseband";
  private static final String TAG = "BaseCommands";
  private Handler baseHandler = new Handler()
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      Rlog.i("BaseCommands", "into BaseCommands.baseHandler handleMessage().");
      paramAnonymousMessage = (AsyncResult)obj;
      if (exception != null)
      {
        Rlog.i("BaseCommands", "into BaseCommands.baseHandler handleMessage(), get exception");
      }
      else
      {
        paramAnonymousMessage = (String)result;
        Object localObject = new StringBuilder();
        ((StringBuilder)localObject).append("into BaseCommands.baseHandler handleMessage(), baseband result: ");
        ((StringBuilder)localObject).append(paramAnonymousMessage);
        Rlog.i("BaseCommands", ((StringBuilder)localObject).toString());
        if (paramAnonymousMessage != null)
        {
          localObject = PhoneFactory.getPhones();
          for (int i = 0; i < localObject.length; i++) {
            localObject[i].setSystemProperty("gsm.version.baseband", paramAnonymousMessage);
          }
        }
      }
    }
  };
  protected RegistrantList mAvailRegistrants = new RegistrantList();
  protected RegistrantList mCallStateRegistrants = new RegistrantList();
  protected RegistrantList mCallWaitingInfoRegistrants = new RegistrantList();
  protected RegistrantList mCarrierInfoForImsiEncryptionRegistrants = new RegistrantList();
  protected Registrant mCatCallSetUpRegistrant;
  protected Registrant mCatCcAlphaRegistrant;
  protected Registrant mCatEventRegistrant;
  protected Registrant mCatProCmdRegistrant;
  protected Registrant mCatSessionEndRegistrant;
  protected RegistrantList mCdmaPrlChangedRegistrants = new RegistrantList();
  protected Registrant mCdmaSmsRegistrant;
  protected int mCdmaSubscription;
  protected RegistrantList mCdmaSubscriptionChangedRegistrants = new RegistrantList();
  protected Context mContext;
  protected RegistrantList mDataCallListChangedRegistrants = new RegistrantList();
  protected RegistrantList mDisplayInfoRegistrants = new RegistrantList();
  protected Registrant mEmergencyCallbackModeRegistrant;
  protected RegistrantList mExitEmergencyCallbackModeRegistrants = new RegistrantList();
  protected Registrant mGsmBroadcastSmsRegistrant;
  protected Registrant mGsmSmsRegistrant;
  protected RegistrantList mHardwareConfigChangeRegistrants = new RegistrantList();
  protected RegistrantList mIccRefreshRegistrants = new RegistrantList();
  protected RegistrantList mIccSlotStatusChangedRegistrants = new RegistrantList();
  protected Registrant mIccSmsFullRegistrant;
  protected RegistrantList mIccStatusChangedRegistrants = new RegistrantList();
  protected RegistrantList mImsNetworkStateChangedRegistrants = new RegistrantList();
  protected Integer mInstanceId;
  protected RegistrantList mLceInfoRegistrants = new RegistrantList();
  protected RegistrantList mLineControlInfoRegistrants = new RegistrantList();
  protected RegistrantList mModemResetRegistrants = new RegistrantList();
  protected Registrant mNITZTimeRegistrant;
  protected RegistrantList mNattKeepaliveStatusRegistrants = new RegistrantList();
  protected RegistrantList mNetworkStateRegistrants = new RegistrantList();
  protected RegistrantList mNotAvailRegistrants = new RegistrantList();
  protected RegistrantList mNumberInfoRegistrants = new RegistrantList();
  protected RegistrantList mOffOrNotAvailRegistrants = new RegistrantList();
  protected RegistrantList mOnRegistrants = new RegistrantList();
  protected RegistrantList mOtaProvisionRegistrants = new RegistrantList();
  protected RegistrantList mPcoDataRegistrants = new RegistrantList();
  protected RegistrantList mPhoneRadioCapabilityChangedRegistrants = new RegistrantList();
  protected int mPhoneType;
  protected RegistrantList mPhysicalChannelConfigurationRegistrants = new RegistrantList();
  protected int mPreferredNetworkType;
  protected RegistrantList mRadioStateChangedRegistrants = new RegistrantList();
  protected RegistrantList mRedirNumInfoRegistrants = new RegistrantList();
  protected RegistrantList mResendIncallMuteRegistrants = new RegistrantList();
  protected Registrant mRestrictedStateRegistrant;
  protected RegistrantList mRilCellInfoListRegistrants = new RegistrantList();
  protected RegistrantList mRilConnectedRegistrants = new RegistrantList();
  protected RegistrantList mRilNetworkScanResultRegistrants = new RegistrantList();
  protected int mRilVersion = -1;
  protected Registrant mRingRegistrant;
  protected RegistrantList mRingbackToneRegistrants = new RegistrantList();
  protected RegistrantList mSignalInfoRegistrants = new RegistrantList();
  protected Registrant mSignalStrengthRegistrant;
  protected Registrant mSmsOnSimRegistrant;
  protected Registrant mSmsStatusRegistrant;
  protected RegistrantList mSrvccStateRegistrants = new RegistrantList();
  protected Registrant mSsRegistrant;
  protected Registrant mSsnRegistrant;
  protected CommandsInterface.RadioState mState = CommandsInterface.RadioState.RADIO_UNAVAILABLE;
  protected Object mStateMonitor = new Object();
  protected RegistrantList mSubscriptionStatusRegistrants = new RegistrantList();
  protected RegistrantList mT53AudCntrlInfoRegistrants = new RegistrantList();
  protected RegistrantList mT53ClirInfoRegistrants = new RegistrantList();
  protected Registrant mUSSDRegistrant;
  protected Registrant mUnsolOemHookRawRegistrant;
  protected RegistrantList mVoicePrivacyOffRegistrants = new RegistrantList();
  protected RegistrantList mVoicePrivacyOnRegistrants = new RegistrantList();
  protected RegistrantList mVoiceRadioTechChangedRegistrants = new RegistrantList();
  
  public BaseCommands(Context paramContext)
  {
    mContext = paramContext;
  }
  
  protected void asusEvtLog(String paramString)
  {
    try
    {
      PrintWriter localPrintWriter = new java/io/PrintWriter;
      localPrintWriter.<init>("/proc/asusevtlog");
      localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append(paramString);
      if (mInstanceId != null)
      {
        paramString = new java/lang/StringBuilder;
        paramString.<init>();
        paramString.append(" [");
        paramString.append(mInstanceId);
        paramString.append("]");
        paramString = paramString.toString();
      }
      else
      {
        paramString = " [??]";
      }
      localStringBuilder.append(paramString);
      localPrintWriter.println(localStringBuilder.toString());
      localPrintWriter.close();
    }
    catch (IOException paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("asusEvtLog fail: ");
      localStringBuilder.append(paramString);
      Rlog.e("BaseCommands", localStringBuilder.toString());
    }
  }
  
  public int getLteOnCdmaMode()
  {
    return TelephonyManager.getLteOnCdmaModeStatic();
  }
  
  public void getRadioCapability(Message paramMessage) {}
  
  public CommandsInterface.RadioState getRadioState()
  {
    return mState;
  }
  
  public int getRilVersion()
  {
    return mRilVersion;
  }
  
  public void logSignal(Object paramObject) {}
  
  public void pullLceData(Message paramMessage) {}
  
  public void registerFoT53ClirlInfo(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mT53ClirInfoRegistrants.add(paramHandler);
  }
  
  public void registerForAvailable(Handler arg1, int paramInt, Object paramObject)
  {
    paramObject = new Registrant(???, paramInt, paramObject);
    synchronized (mStateMonitor)
    {
      mAvailRegistrants.add(paramObject);
      if (mState.isAvailable())
      {
        AsyncResult localAsyncResult = new android/os/AsyncResult;
        localAsyncResult.<init>(null, null, null);
        paramObject.notifyRegistrant(localAsyncResult);
      }
      return;
    }
  }
  
  public void registerForCallStateChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mCallStateRegistrants.add(paramHandler);
  }
  
  public void registerForCallWaitingInfo(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mCallWaitingInfoRegistrants.add(paramHandler);
  }
  
  public void registerForCarrierInfoForImsiEncryption(Handler paramHandler, int paramInt, Object paramObject)
  {
    mCarrierInfoForImsiEncryptionRegistrants.add(new Registrant(paramHandler, paramInt, paramObject));
  }
  
  public void registerForCdmaOtaProvision(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mOtaProvisionRegistrants.add(paramHandler);
  }
  
  public void registerForCdmaPrlChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mCdmaPrlChangedRegistrants.add(paramHandler);
  }
  
  public void registerForCdmaSubscriptionChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mCdmaSubscriptionChangedRegistrants.add(paramHandler);
  }
  
  public void registerForCellInfoList(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mRilCellInfoListRegistrants.add(paramHandler);
  }
  
  public void registerForDataCallListChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mDataCallListChangedRegistrants.add(paramHandler);
  }
  
  public void registerForDisplayInfo(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mDisplayInfoRegistrants.add(paramHandler);
  }
  
  public void registerForExitEmergencyCallbackMode(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mExitEmergencyCallbackModeRegistrants.add(paramHandler);
  }
  
  public void registerForHardwareConfigChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mHardwareConfigChangeRegistrants.add(paramHandler);
  }
  
  public void registerForIccRefresh(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mIccRefreshRegistrants.add(paramHandler);
  }
  
  public void registerForIccSlotStatusChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mIccSlotStatusChangedRegistrants.add(paramHandler);
  }
  
  public void registerForIccStatusChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mIccStatusChangedRegistrants.add(paramHandler);
  }
  
  public void registerForImsNetworkStateChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mImsNetworkStateChangedRegistrants.add(paramHandler);
  }
  
  public void registerForInCallVoicePrivacyOff(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mVoicePrivacyOffRegistrants.add(paramHandler);
  }
  
  public void registerForInCallVoicePrivacyOn(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mVoicePrivacyOnRegistrants.add(paramHandler);
  }
  
  public void registerForLceInfo(Handler arg1, int paramInt, Object paramObject)
  {
    paramObject = new Registrant(???, paramInt, paramObject);
    synchronized (mStateMonitor)
    {
      mLceInfoRegistrants.add(paramObject);
      return;
    }
  }
  
  public void registerForLineControlInfo(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mLineControlInfoRegistrants.add(paramHandler);
  }
  
  public void registerForModemReset(Handler paramHandler, int paramInt, Object paramObject)
  {
    mModemResetRegistrants.add(new Registrant(paramHandler, paramInt, paramObject));
  }
  
  public void registerForNattKeepaliveStatus(Handler arg1, int paramInt, Object paramObject)
  {
    paramObject = new Registrant(???, paramInt, paramObject);
    synchronized (mStateMonitor)
    {
      mNattKeepaliveStatusRegistrants.add(paramObject);
      return;
    }
  }
  
  public void registerForNetworkScanResult(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mRilNetworkScanResultRegistrants.add(paramHandler);
  }
  
  public void registerForNetworkStateChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mNetworkStateRegistrants.add(paramHandler);
  }
  
  public void registerForNotAvailable(Handler arg1, int paramInt, Object paramObject)
  {
    Registrant localRegistrant = new Registrant(???, paramInt, paramObject);
    synchronized (mStateMonitor)
    {
      mNotAvailRegistrants.add(localRegistrant);
      if (!mState.isAvailable())
      {
        paramObject = new android/os/AsyncResult;
        paramObject.<init>(null, null, null);
        localRegistrant.notifyRegistrant(paramObject);
      }
      return;
    }
  }
  
  public void registerForNumberInfo(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mNumberInfoRegistrants.add(paramHandler);
  }
  
  public void registerForOffOrNotAvailable(Handler arg1, int paramInt, Object paramObject)
  {
    Registrant localRegistrant = new Registrant(???, paramInt, paramObject);
    synchronized (mStateMonitor)
    {
      mOffOrNotAvailRegistrants.add(localRegistrant);
      if ((mState == CommandsInterface.RadioState.RADIO_OFF) || (!mState.isAvailable()))
      {
        paramObject = new android/os/AsyncResult;
        paramObject.<init>(null, null, null);
        localRegistrant.notifyRegistrant(paramObject);
      }
      return;
    }
  }
  
  public void registerForOn(Handler arg1, int paramInt, Object paramObject)
  {
    paramObject = new Registrant(???, paramInt, paramObject);
    synchronized (mStateMonitor)
    {
      mOnRegistrants.add(paramObject);
      if (mState.isOn())
      {
        AsyncResult localAsyncResult = new android/os/AsyncResult;
        localAsyncResult.<init>(null, null, null);
        paramObject.notifyRegistrant(localAsyncResult);
      }
      return;
    }
  }
  
  public void registerForPcoData(Handler paramHandler, int paramInt, Object paramObject)
  {
    mPcoDataRegistrants.add(new Registrant(paramHandler, paramInt, paramObject));
  }
  
  public void registerForPhysicalChannelConfiguration(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mPhysicalChannelConfigurationRegistrants.add(paramHandler);
  }
  
  public void registerForRadioCapabilityChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mPhoneRadioCapabilityChangedRegistrants.add(paramHandler);
  }
  
  public void registerForRadioStateChanged(Handler arg1, int paramInt, Object paramObject)
  {
    paramObject = new Registrant(???, paramInt, paramObject);
    synchronized (mStateMonitor)
    {
      mRadioStateChangedRegistrants.add(paramObject);
      paramObject.notifyRegistrant();
      return;
    }
  }
  
  public void registerForRedirectedNumberInfo(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mRedirNumInfoRegistrants.add(paramHandler);
  }
  
  public void registerForResendIncallMute(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mResendIncallMuteRegistrants.add(paramHandler);
  }
  
  public void registerForRilConnected(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mRilConnectedRegistrants.add(paramHandler);
    if (mRilVersion != -1) {
      paramHandler.notifyRegistrant(new AsyncResult(null, new Integer(mRilVersion), null));
    }
  }
  
  public void registerForRingbackTone(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mRingbackToneRegistrants.add(paramHandler);
  }
  
  public void registerForSignalInfo(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mSignalInfoRegistrants.add(paramHandler);
  }
  
  public void registerForSrvccStateChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mSrvccStateRegistrants.add(paramHandler);
  }
  
  public void registerForSubscriptionStatusChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mSubscriptionStatusRegistrants.add(paramHandler);
  }
  
  public void registerForT53AudioControlInfo(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mT53AudCntrlInfoRegistrants.add(paramHandler);
  }
  
  public void registerForVoiceRadioTechChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mVoiceRadioTechChangedRegistrants.add(paramHandler);
  }
  
  public void requestShutdown(Message paramMessage) {}
  
  public void setDataAllowed(boolean paramBoolean, Message paramMessage) {}
  
  public void setEmergencyCallbackMode(Handler paramHandler, int paramInt, Object paramObject)
  {
    mEmergencyCallbackModeRegistrant = new Registrant(paramHandler, paramInt, paramObject);
  }
  
  public void setOnCallRing(Handler paramHandler, int paramInt, Object paramObject)
  {
    mRingRegistrant = new Registrant(paramHandler, paramInt, paramObject);
  }
  
  public void setOnCatCallSetUp(Handler paramHandler, int paramInt, Object paramObject)
  {
    mCatCallSetUpRegistrant = new Registrant(paramHandler, paramInt, paramObject);
  }
  
  public void setOnCatCcAlphaNotify(Handler paramHandler, int paramInt, Object paramObject)
  {
    mCatCcAlphaRegistrant = new Registrant(paramHandler, paramInt, paramObject);
  }
  
  public void setOnCatEvent(Handler paramHandler, int paramInt, Object paramObject)
  {
    mCatEventRegistrant = new Registrant(paramHandler, paramInt, paramObject);
  }
  
  public void setOnCatProactiveCmd(Handler paramHandler, int paramInt, Object paramObject)
  {
    mCatProCmdRegistrant = new Registrant(paramHandler, paramInt, paramObject);
  }
  
  public void setOnCatSessionEnd(Handler paramHandler, int paramInt, Object paramObject)
  {
    mCatSessionEndRegistrant = new Registrant(paramHandler, paramInt, paramObject);
  }
  
  public void setOnIccRefresh(Handler paramHandler, int paramInt, Object paramObject)
  {
    registerForIccRefresh(paramHandler, paramInt, paramObject);
  }
  
  public void setOnIccSmsFull(Handler paramHandler, int paramInt, Object paramObject)
  {
    mIccSmsFullRegistrant = new Registrant(paramHandler, paramInt, paramObject);
  }
  
  public void setOnNITZTime(Handler paramHandler, int paramInt, Object paramObject)
  {
    mNITZTimeRegistrant = new Registrant(paramHandler, paramInt, paramObject);
  }
  
  public void setOnNewCdmaSms(Handler paramHandler, int paramInt, Object paramObject)
  {
    mCdmaSmsRegistrant = new Registrant(paramHandler, paramInt, paramObject);
  }
  
  public void setOnNewGsmBroadcastSms(Handler paramHandler, int paramInt, Object paramObject)
  {
    mGsmBroadcastSmsRegistrant = new Registrant(paramHandler, paramInt, paramObject);
  }
  
  public void setOnNewGsmSms(Handler paramHandler, int paramInt, Object paramObject)
  {
    mGsmSmsRegistrant = new Registrant(paramHandler, paramInt, paramObject);
  }
  
  public void setOnRestrictedStateChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    mRestrictedStateRegistrant = new Registrant(paramHandler, paramInt, paramObject);
  }
  
  public void setOnSignalStrengthUpdate(Handler paramHandler, int paramInt, Object paramObject)
  {
    mSignalStrengthRegistrant = new Registrant(paramHandler, paramInt, paramObject);
  }
  
  public void setOnSmsOnSim(Handler paramHandler, int paramInt, Object paramObject)
  {
    mSmsOnSimRegistrant = new Registrant(paramHandler, paramInt, paramObject);
  }
  
  public void setOnSmsStatus(Handler paramHandler, int paramInt, Object paramObject)
  {
    mSmsStatusRegistrant = new Registrant(paramHandler, paramInt, paramObject);
  }
  
  public void setOnSs(Handler paramHandler, int paramInt, Object paramObject)
  {
    mSsRegistrant = new Registrant(paramHandler, paramInt, paramObject);
  }
  
  public void setOnSuppServiceNotification(Handler paramHandler, int paramInt, Object paramObject)
  {
    mSsnRegistrant = new Registrant(paramHandler, paramInt, paramObject);
  }
  
  public void setOnUSSD(Handler paramHandler, int paramInt, Object paramObject)
  {
    mUSSDRegistrant = new Registrant(paramHandler, paramInt, paramObject);
  }
  
  public void setOnUnsolOemHookRaw(Handler paramHandler, int paramInt, Object paramObject)
  {
    mUnsolOemHookRawRegistrant = new Registrant(paramHandler, paramInt, paramObject);
  }
  
  public void setRadioCapability(RadioCapability paramRadioCapability, Message paramMessage) {}
  
  protected void setRadioState(CommandsInterface.RadioState paramRadioState)
  {
    synchronized (mStateMonitor)
    {
      CommandsInterface.RadioState localRadioState = mState;
      mState = paramRadioState;
      if (localRadioState == mState)
      {
        if (localRadioState == CommandsInterface.RadioState.RADIO_UNAVAILABLE) {
          getBasebandVersion(baseHandler.obtainMessage());
        }
        return;
      }
      paramRadioState = new java/lang/StringBuilder;
      paramRadioState.<init>();
      paramRadioState.append("[RIL] radio_power ");
      paramRadioState.append(mState.toString());
      asusEvtLog(paramRadioState.toString());
      mRadioStateChangedRegistrants.notifyRegistrants();
      if ((mState.isAvailable()) && (!localRadioState.isAvailable())) {
        mAvailRegistrants.notifyRegistrants();
      }
      if ((!mState.isAvailable()) && (localRadioState.isAvailable())) {
        mNotAvailRegistrants.notifyRegistrants();
      }
      if ((mState.isOn()) && (!localRadioState.isOn())) {
        mOnRegistrants.notifyRegistrants();
      }
      if (((!mState.isOn()) || (!mState.isAvailable())) && (localRadioState.isOn()) && (localRadioState.isAvailable())) {
        mOffOrNotAvailRegistrants.notifyRegistrants();
      }
      return;
    }
  }
  
  public void setScreenState(boolean paramBoolean) {}
  
  public void setTransmitPower(int paramInt1, int paramInt2, Message paramMessage) {}
  
  public void setUiccSubscription(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Message paramMessage) {}
  
  public void startLceService(int paramInt, boolean paramBoolean, Message paramMessage) {}
  
  public void stopLceService(Message paramMessage) {}
  
  public void testingEmergencyCall() {}
  
  public void unSetOnCallRing(Handler paramHandler)
  {
    if ((mRingRegistrant != null) && (mRingRegistrant.getHandler() == paramHandler))
    {
      mRingRegistrant.clear();
      mRingRegistrant = null;
    }
  }
  
  public void unSetOnCatCallSetUp(Handler paramHandler)
  {
    if ((mCatCallSetUpRegistrant != null) && (mCatCallSetUpRegistrant.getHandler() == paramHandler))
    {
      mCatCallSetUpRegistrant.clear();
      mCatCallSetUpRegistrant = null;
    }
  }
  
  public void unSetOnCatCcAlphaNotify(Handler paramHandler)
  {
    mCatCcAlphaRegistrant.clear();
  }
  
  public void unSetOnCatEvent(Handler paramHandler)
  {
    if ((mCatEventRegistrant != null) && (mCatEventRegistrant.getHandler() == paramHandler))
    {
      mCatEventRegistrant.clear();
      mCatEventRegistrant = null;
    }
  }
  
  public void unSetOnCatProactiveCmd(Handler paramHandler)
  {
    if ((mCatProCmdRegistrant != null) && (mCatProCmdRegistrant.getHandler() == paramHandler))
    {
      mCatProCmdRegistrant.clear();
      mCatProCmdRegistrant = null;
    }
  }
  
  public void unSetOnCatSessionEnd(Handler paramHandler)
  {
    if ((mCatSessionEndRegistrant != null) && (mCatSessionEndRegistrant.getHandler() == paramHandler))
    {
      mCatSessionEndRegistrant.clear();
      mCatSessionEndRegistrant = null;
    }
  }
  
  public void unSetOnIccSmsFull(Handler paramHandler)
  {
    if ((mIccSmsFullRegistrant != null) && (mIccSmsFullRegistrant.getHandler() == paramHandler))
    {
      mIccSmsFullRegistrant.clear();
      mIccSmsFullRegistrant = null;
    }
  }
  
  public void unSetOnNITZTime(Handler paramHandler)
  {
    if ((mNITZTimeRegistrant != null) && (mNITZTimeRegistrant.getHandler() == paramHandler))
    {
      mNITZTimeRegistrant.clear();
      mNITZTimeRegistrant = null;
    }
  }
  
  public void unSetOnNewCdmaSms(Handler paramHandler)
  {
    if ((mCdmaSmsRegistrant != null) && (mCdmaSmsRegistrant.getHandler() == paramHandler))
    {
      mCdmaSmsRegistrant.clear();
      mCdmaSmsRegistrant = null;
    }
  }
  
  public void unSetOnNewGsmBroadcastSms(Handler paramHandler)
  {
    if ((mGsmBroadcastSmsRegistrant != null) && (mGsmBroadcastSmsRegistrant.getHandler() == paramHandler))
    {
      mGsmBroadcastSmsRegistrant.clear();
      mGsmBroadcastSmsRegistrant = null;
    }
  }
  
  public void unSetOnNewGsmSms(Handler paramHandler)
  {
    if ((mGsmSmsRegistrant != null) && (mGsmSmsRegistrant.getHandler() == paramHandler))
    {
      mGsmSmsRegistrant.clear();
      mGsmSmsRegistrant = null;
    }
  }
  
  public void unSetOnRestrictedStateChanged(Handler paramHandler)
  {
    if ((mRestrictedStateRegistrant != null) && (mRestrictedStateRegistrant.getHandler() == paramHandler))
    {
      mRestrictedStateRegistrant.clear();
      mRestrictedStateRegistrant = null;
    }
  }
  
  public void unSetOnSignalStrengthUpdate(Handler paramHandler)
  {
    if ((mSignalStrengthRegistrant != null) && (mSignalStrengthRegistrant.getHandler() == paramHandler))
    {
      mSignalStrengthRegistrant.clear();
      mSignalStrengthRegistrant = null;
    }
  }
  
  public void unSetOnSmsOnSim(Handler paramHandler)
  {
    if ((mSmsOnSimRegistrant != null) && (mSmsOnSimRegistrant.getHandler() == paramHandler))
    {
      mSmsOnSimRegistrant.clear();
      mSmsOnSimRegistrant = null;
    }
  }
  
  public void unSetOnSmsStatus(Handler paramHandler)
  {
    if ((mSmsStatusRegistrant != null) && (mSmsStatusRegistrant.getHandler() == paramHandler))
    {
      mSmsStatusRegistrant.clear();
      mSmsStatusRegistrant = null;
    }
  }
  
  public void unSetOnSs(Handler paramHandler)
  {
    mSsRegistrant.clear();
  }
  
  public void unSetOnSuppServiceNotification(Handler paramHandler)
  {
    if ((mSsnRegistrant != null) && (mSsnRegistrant.getHandler() == paramHandler))
    {
      mSsnRegistrant.clear();
      mSsnRegistrant = null;
    }
  }
  
  public void unSetOnUSSD(Handler paramHandler)
  {
    if ((mUSSDRegistrant != null) && (mUSSDRegistrant.getHandler() == paramHandler))
    {
      mUSSDRegistrant.clear();
      mUSSDRegistrant = null;
    }
  }
  
  public void unSetOnUnsolOemHookRaw(Handler paramHandler)
  {
    if ((mUnsolOemHookRawRegistrant != null) && (mUnsolOemHookRawRegistrant.getHandler() == paramHandler))
    {
      mUnsolOemHookRawRegistrant.clear();
      mUnsolOemHookRawRegistrant = null;
    }
  }
  
  public void unregisterForAvailable(Handler paramHandler)
  {
    synchronized (mStateMonitor)
    {
      mAvailRegistrants.remove(paramHandler);
      return;
    }
  }
  
  public void unregisterForCallStateChanged(Handler paramHandler)
  {
    mCallStateRegistrants.remove(paramHandler);
  }
  
  public void unregisterForCallWaitingInfo(Handler paramHandler)
  {
    mCallWaitingInfoRegistrants.remove(paramHandler);
  }
  
  public void unregisterForCarrierInfoForImsiEncryption(Handler paramHandler)
  {
    mCarrierInfoForImsiEncryptionRegistrants.remove(paramHandler);
  }
  
  public void unregisterForCdmaOtaProvision(Handler paramHandler)
  {
    mOtaProvisionRegistrants.remove(paramHandler);
  }
  
  public void unregisterForCdmaPrlChanged(Handler paramHandler)
  {
    mCdmaPrlChangedRegistrants.remove(paramHandler);
  }
  
  public void unregisterForCdmaSubscriptionChanged(Handler paramHandler)
  {
    mCdmaSubscriptionChangedRegistrants.remove(paramHandler);
  }
  
  public void unregisterForCellInfoList(Handler paramHandler)
  {
    mRilCellInfoListRegistrants.remove(paramHandler);
  }
  
  public void unregisterForDataCallListChanged(Handler paramHandler)
  {
    mDataCallListChangedRegistrants.remove(paramHandler);
  }
  
  public void unregisterForDisplayInfo(Handler paramHandler)
  {
    mDisplayInfoRegistrants.remove(paramHandler);
  }
  
  public void unregisterForExitEmergencyCallbackMode(Handler paramHandler)
  {
    mExitEmergencyCallbackModeRegistrants.remove(paramHandler);
  }
  
  public void unregisterForHardwareConfigChanged(Handler paramHandler)
  {
    mHardwareConfigChangeRegistrants.remove(paramHandler);
  }
  
  public void unregisterForIccRefresh(Handler paramHandler)
  {
    mIccRefreshRegistrants.remove(paramHandler);
  }
  
  public void unregisterForIccSlotStatusChanged(Handler paramHandler)
  {
    mIccSlotStatusChangedRegistrants.remove(paramHandler);
  }
  
  public void unregisterForIccStatusChanged(Handler paramHandler)
  {
    mIccStatusChangedRegistrants.remove(paramHandler);
  }
  
  public void unregisterForImsNetworkStateChanged(Handler paramHandler)
  {
    mImsNetworkStateChangedRegistrants.remove(paramHandler);
  }
  
  public void unregisterForInCallVoicePrivacyOff(Handler paramHandler)
  {
    mVoicePrivacyOffRegistrants.remove(paramHandler);
  }
  
  public void unregisterForInCallVoicePrivacyOn(Handler paramHandler)
  {
    mVoicePrivacyOnRegistrants.remove(paramHandler);
  }
  
  public void unregisterForLceInfo(Handler paramHandler)
  {
    synchronized (mStateMonitor)
    {
      mLceInfoRegistrants.remove(paramHandler);
      return;
    }
  }
  
  public void unregisterForLineControlInfo(Handler paramHandler)
  {
    mLineControlInfoRegistrants.remove(paramHandler);
  }
  
  public void unregisterForModemReset(Handler paramHandler)
  {
    mModemResetRegistrants.remove(paramHandler);
  }
  
  public void unregisterForNattKeepaliveStatus(Handler paramHandler)
  {
    synchronized (mStateMonitor)
    {
      mNattKeepaliveStatusRegistrants.remove(paramHandler);
      return;
    }
  }
  
  public void unregisterForNetworkScanResult(Handler paramHandler)
  {
    mRilNetworkScanResultRegistrants.remove(paramHandler);
  }
  
  public void unregisterForNetworkStateChanged(Handler paramHandler)
  {
    mNetworkStateRegistrants.remove(paramHandler);
  }
  
  public void unregisterForNotAvailable(Handler paramHandler)
  {
    synchronized (mStateMonitor)
    {
      mNotAvailRegistrants.remove(paramHandler);
      return;
    }
  }
  
  public void unregisterForNumberInfo(Handler paramHandler)
  {
    mNumberInfoRegistrants.remove(paramHandler);
  }
  
  public void unregisterForOffOrNotAvailable(Handler paramHandler)
  {
    synchronized (mStateMonitor)
    {
      mOffOrNotAvailRegistrants.remove(paramHandler);
      return;
    }
  }
  
  public void unregisterForOn(Handler paramHandler)
  {
    synchronized (mStateMonitor)
    {
      mOnRegistrants.remove(paramHandler);
      return;
    }
  }
  
  public void unregisterForPcoData(Handler paramHandler)
  {
    mPcoDataRegistrants.remove(paramHandler);
  }
  
  public void unregisterForPhysicalChannelConfiguration(Handler paramHandler)
  {
    mPhysicalChannelConfigurationRegistrants.remove(paramHandler);
  }
  
  public void unregisterForRadioCapabilityChanged(Handler paramHandler)
  {
    mPhoneRadioCapabilityChangedRegistrants.remove(paramHandler);
  }
  
  public void unregisterForRadioStateChanged(Handler paramHandler)
  {
    synchronized (mStateMonitor)
    {
      mRadioStateChangedRegistrants.remove(paramHandler);
      return;
    }
  }
  
  public void unregisterForRedirectedNumberInfo(Handler paramHandler)
  {
    mRedirNumInfoRegistrants.remove(paramHandler);
  }
  
  public void unregisterForResendIncallMute(Handler paramHandler)
  {
    mResendIncallMuteRegistrants.remove(paramHandler);
  }
  
  public void unregisterForRilConnected(Handler paramHandler)
  {
    mRilConnectedRegistrants.remove(paramHandler);
  }
  
  public void unregisterForRingbackTone(Handler paramHandler)
  {
    mRingbackToneRegistrants.remove(paramHandler);
  }
  
  public void unregisterForSignalInfo(Handler paramHandler)
  {
    mSignalInfoRegistrants.remove(paramHandler);
  }
  
  public void unregisterForSrvccStateChanged(Handler paramHandler)
  {
    mSrvccStateRegistrants.remove(paramHandler);
  }
  
  public void unregisterForSubscriptionStatusChanged(Handler paramHandler)
  {
    mSubscriptionStatusRegistrants.remove(paramHandler);
  }
  
  public void unregisterForT53AudioControlInfo(Handler paramHandler)
  {
    mT53AudCntrlInfoRegistrants.remove(paramHandler);
  }
  
  public void unregisterForT53ClirInfo(Handler paramHandler)
  {
    mT53ClirInfoRegistrants.remove(paramHandler);
  }
  
  public void unregisterForVoiceRadioTechChanged(Handler paramHandler)
  {
    mVoiceRadioTechChangedRegistrants.remove(paramHandler);
  }
  
  public void unsetOnIccRefresh(Handler paramHandler)
  {
    unregisterForIccRefresh(paramHandler);
  }
}
