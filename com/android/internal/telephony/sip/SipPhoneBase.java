package com.android.internal.telephony.sip;

import android.content.Context;
import android.net.LinkProperties;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.RegistrantList;
import android.os.ResultReceiver;
import android.os.SystemProperties;
import android.os.WorkSource;
import android.telephony.CellLocation;
import android.telephony.NetworkScanRequest;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.Call.State;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccPhoneBookInterfaceManager;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.OperatorInfo;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants.DataState;
import com.android.internal.telephony.PhoneConstants.State;
import com.android.internal.telephony.PhoneInternalInterface.DataActivityState;
import com.android.internal.telephony.PhoneInternalInterface.SuppService;
import com.android.internal.telephony.PhoneNotifier;
import com.android.internal.telephony.uicc.IccFileHandler;
import java.util.ArrayList;
import java.util.List;

abstract class SipPhoneBase
  extends Phone
{
  private static final String LOG_TAG = "SipPhoneBase";
  private RegistrantList mRingbackRegistrants = new RegistrantList();
  private PhoneConstants.State mState = PhoneConstants.State.IDLE;
  
  public SipPhoneBase(String paramString, Context paramContext, PhoneNotifier paramPhoneNotifier)
  {
    super(paramString, paramPhoneNotifier, paramContext, new SipCommandInterface(paramContext), false);
  }
  
  public void activateCellBroadcastSms(int paramInt, Message paramMessage)
  {
    Rlog.e("SipPhoneBase", "Error! This functionality is not implemented for SIP.");
  }
  
  public boolean canDial()
  {
    int i = getServiceState().getState();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("canDial(): serviceState = ");
    localStringBuilder.append(i);
    Rlog.v("SipPhoneBase", localStringBuilder.toString());
    boolean bool1 = false;
    if (i == 3) {
      return false;
    }
    String str = SystemProperties.get("ro.telephony.disable-call", "false");
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("canDial(): disableCall = ");
    localStringBuilder.append(str);
    Rlog.v("SipPhoneBase", localStringBuilder.toString());
    if (str.equals("true")) {
      return false;
    }
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("canDial(): ringingCall: ");
    localStringBuilder.append(getRingingCall().getState());
    Rlog.v("SipPhoneBase", localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("canDial(): foregndCall: ");
    localStringBuilder.append(getForegroundCall().getState());
    Rlog.v("SipPhoneBase", localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("canDial(): backgndCall: ");
    localStringBuilder.append(getBackgroundCall().getState());
    Rlog.v("SipPhoneBase", localStringBuilder.toString());
    boolean bool2 = bool1;
    if (!getRingingCall().isRinging()) {
      if (getForegroundCall().getState().isAlive())
      {
        bool2 = bool1;
        if (getBackgroundCall().getState().isAlive()) {}
      }
      else
      {
        bool2 = true;
      }
    }
    return bool2;
  }
  
  public boolean disableDataConnectivity()
  {
    return false;
  }
  
  public void disableLocationUpdates() {}
  
  public boolean enableDataConnectivity()
  {
    return false;
  }
  
  public void enableLocationUpdates() {}
  
  public void getAvailableNetworks(Message paramMessage) {}
  
  public abstract Call getBackgroundCall();
  
  public void getCallBarring(String paramString1, String paramString2, Message paramMessage, int paramInt) {}
  
  public boolean getCallForwardingIndicator()
  {
    return false;
  }
  
  public void getCallForwardingOption(int paramInt, Message paramMessage) {}
  
  public void getCallWaiting(Message paramMessage)
  {
    AsyncResult.forMessage(paramMessage, null, null);
    paramMessage.sendToTarget();
  }
  
  public void getCellBroadcastSmsConfig(Message paramMessage)
  {
    Rlog.e("SipPhoneBase", "Error! This functionality is not implemented for SIP.");
  }
  
  public CellLocation getCellLocation(WorkSource paramWorkSource)
  {
    return null;
  }
  
  public PhoneInternalInterface.DataActivityState getDataActivityState()
  {
    return PhoneInternalInterface.DataActivityState.NONE;
  }
  
  public PhoneConstants.DataState getDataConnectionState()
  {
    return PhoneConstants.DataState.DISCONNECTED;
  }
  
  public PhoneConstants.DataState getDataConnectionState(String paramString)
  {
    return PhoneConstants.DataState.DISCONNECTED;
  }
  
  public boolean getDataRoamingEnabled()
  {
    return false;
  }
  
  public String getDeviceId()
  {
    return null;
  }
  
  public String getDeviceSvn()
  {
    return null;
  }
  
  public String getEsn()
  {
    Rlog.e("SipPhoneBase", "[SipPhone] getEsn() is a CDMA method");
    return "0";
  }
  
  public abstract Call getForegroundCall();
  
  public String getGroupIdLevel1()
  {
    return null;
  }
  
  public String getGroupIdLevel2()
  {
    return null;
  }
  
  public IccCard getIccCard()
  {
    return null;
  }
  
  public IccFileHandler getIccFileHandler()
  {
    return null;
  }
  
  public IccPhoneBookInterfaceManager getIccPhoneBookInterfaceManager()
  {
    return null;
  }
  
  public boolean getIccRecordsLoaded()
  {
    return false;
  }
  
  public String getIccSerialNumber()
  {
    return null;
  }
  
  public String getImei()
  {
    return null;
  }
  
  public String getLine1AlphaTag()
  {
    return null;
  }
  
  public String getLine1Number()
  {
    return null;
  }
  
  public LinkProperties getLinkProperties(String paramString)
  {
    return null;
  }
  
  public String getMeid()
  {
    Rlog.e("SipPhoneBase", "[SipPhone] getMeid() is a CDMA method");
    return "0";
  }
  
  public boolean getMessageWaitingIndicator()
  {
    return false;
  }
  
  public void getOutgoingCallerIdDisplay(Message paramMessage)
  {
    AsyncResult.forMessage(paramMessage, null, null);
    paramMessage.sendToTarget();
  }
  
  public List<? extends MmiCode> getPendingMmiCodes()
  {
    return new ArrayList(0);
  }
  
  public int getPhoneType()
  {
    return 3;
  }
  
  public abstract Call getRingingCall();
  
  public ServiceState getServiceState()
  {
    ServiceState localServiceState = new ServiceState();
    localServiceState.setVoiceRegState(0);
    return localServiceState;
  }
  
  public SignalStrength getSignalStrength()
  {
    return new SignalStrength();
  }
  
  public PhoneConstants.State getState()
  {
    return mState;
  }
  
  public String getSubscriberId()
  {
    return null;
  }
  
  public String getVoiceMailAlphaTag()
  {
    return null;
  }
  
  public String getVoiceMailNumber()
  {
    return null;
  }
  
  public boolean handleInCallMmiCommands(String paramString)
  {
    return false;
  }
  
  public boolean handlePinMmi(String paramString)
  {
    return false;
  }
  
  public boolean handleUssdRequest(String paramString, ResultReceiver paramResultReceiver)
  {
    return false;
  }
  
  public boolean isDataAllowed()
  {
    return false;
  }
  
  public boolean isDataEnabled()
  {
    return false;
  }
  
  boolean isInCall()
  {
    Call.State localState1 = getForegroundCall().getState();
    Call.State localState2 = getBackgroundCall().getState();
    Call.State localState3 = getRingingCall().getState();
    boolean bool;
    if ((!localState1.isAlive()) && (!localState2.isAlive()) && (!localState3.isAlive())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isUserDataEnabled()
  {
    return false;
  }
  
  public boolean isVideoEnabled()
  {
    return false;
  }
  
  void migrateFrom(SipPhoneBase paramSipPhoneBase)
  {
    super.migrateFrom(paramSipPhoneBase);
    migrate(mRingbackRegistrants, mRingbackRegistrants);
  }
  
  public boolean needsOtaServiceProvisioning()
  {
    return false;
  }
  
  public void notifyCallForwardingIndicator()
  {
    mNotifier.notifyCallForwardingChanged(this);
  }
  
  void notifyDisconnect(Connection paramConnection)
  {
    mDisconnectRegistrants.notifyResult(paramConnection);
  }
  
  void notifyNewRingingConnection(Connection paramConnection)
  {
    super.notifyNewRingingConnectionP(paramConnection);
  }
  
  void notifyPhoneStateChanged() {}
  
  void notifyPreciseCallStateChanged()
  {
    super.notifyPreciseCallStateChangedP();
  }
  
  void notifyServiceStateChanged(ServiceState paramServiceState)
  {
    super.notifyServiceStateChangedP(paramServiceState);
  }
  
  void notifySuppServiceFailed(PhoneInternalInterface.SuppService paramSuppService)
  {
    mSuppServiceFailedRegistrants.notifyResult(paramSuppService);
  }
  
  void notifyUnknownConnection()
  {
    mUnknownConnectionRegistrants.notifyResult(this);
  }
  
  protected void onUpdateIccAvailability() {}
  
  public void registerForRingbackTone(Handler paramHandler, int paramInt, Object paramObject)
  {
    mRingbackRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForSuppServiceNotification(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void saveClirSetting(int paramInt) {}
  
  public void selectNetworkManually(OperatorInfo paramOperatorInfo, boolean paramBoolean, Message paramMessage) {}
  
  public void sendEmergencyCallStateChange(boolean paramBoolean) {}
  
  public void sendUssdResponse(String paramString) {}
  
  public void setBroadcastEmergencyCallStateChanges(boolean paramBoolean) {}
  
  public void setCallBarring(String paramString1, boolean paramBoolean, String paramString2, Message paramMessage, int paramInt) {}
  
  public void setCallForwardingOption(int paramInt1, int paramInt2, String paramString, int paramInt3, Message paramMessage) {}
  
  public void setCallWaiting(boolean paramBoolean, Message paramMessage)
  {
    Rlog.e("SipPhoneBase", "call waiting not supported");
  }
  
  public void setCellBroadcastSmsConfig(int[] paramArrayOfInt, Message paramMessage)
  {
    Rlog.e("SipPhoneBase", "Error! This functionality is not implemented for SIP.");
  }
  
  public void setDataRoamingEnabled(boolean paramBoolean) {}
  
  public boolean setLine1Number(String paramString1, String paramString2, Message paramMessage)
  {
    return false;
  }
  
  public void setNetworkSelectionModeAutomatic(Message paramMessage) {}
  
  public void setOnPostDialCharacter(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void setOutgoingCallerIdDisplay(int paramInt, Message paramMessage)
  {
    AsyncResult.forMessage(paramMessage, null, null);
    paramMessage.sendToTarget();
  }
  
  public void setRadioPower(boolean paramBoolean) {}
  
  public void setUserDataEnabled(boolean paramBoolean) {}
  
  public void setVoiceMailNumber(String paramString1, String paramString2, Message paramMessage)
  {
    AsyncResult.forMessage(paramMessage, null, null);
    paramMessage.sendToTarget();
  }
  
  public void startNetworkScan(NetworkScanRequest paramNetworkScanRequest, Message paramMessage) {}
  
  public void startRingbackTone()
  {
    AsyncResult localAsyncResult = new AsyncResult(null, Boolean.TRUE, null);
    mRingbackRegistrants.notifyRegistrants(localAsyncResult);
  }
  
  public void stopNetworkScan(Message paramMessage) {}
  
  public void stopRingbackTone()
  {
    AsyncResult localAsyncResult = new AsyncResult(null, Boolean.FALSE, null);
    mRingbackRegistrants.notifyRegistrants(localAsyncResult);
  }
  
  public void unregisterForRingbackTone(Handler paramHandler)
  {
    mRingbackRegistrants.remove(paramHandler);
  }
  
  public void unregisterForSuppServiceNotification(Handler paramHandler) {}
  
  void updatePhoneState()
  {
    Object localObject = mState;
    if (getRingingCall().isRinging()) {
      mState = PhoneConstants.State.RINGING;
    } else if ((getForegroundCall().isIdle()) && (getBackgroundCall().isIdle())) {
      mState = PhoneConstants.State.IDLE;
    } else {
      mState = PhoneConstants.State.OFFHOOK;
    }
    if (mState != localObject)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(" ^^^ new phone state: ");
      ((StringBuilder)localObject).append(mState);
      Rlog.d("SipPhoneBase", ((StringBuilder)localObject).toString());
      notifyPhoneStateChanged();
    }
  }
  
  public void updateServiceLocation() {}
}
