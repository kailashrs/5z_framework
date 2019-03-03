package com.android.internal.telephony.imsphone;

import android.content.Context;
import android.net.LinkProperties;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.RegistrantList;
import android.os.SystemProperties;
import android.os.WorkSource;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.NetworkScanRequest;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.util.Pair;
import com.android.internal.annotations.VisibleForTesting;
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
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.dataconnection.DataConnection;
import com.android.internal.telephony.uicc.IccFileHandler;
import java.util.ArrayList;
import java.util.List;

abstract class ImsPhoneBase
  extends Phone
{
  private static final String LOG_TAG = "ImsPhoneBase";
  private RegistrantList mOnHoldRegistrants = new RegistrantList();
  private RegistrantList mRingbackRegistrants = new RegistrantList();
  private PhoneConstants.State mState = PhoneConstants.State.IDLE;
  private RegistrantList mTtyModeReceivedRegistrants = new RegistrantList();
  
  public ImsPhoneBase(String paramString, Context paramContext, PhoneNotifier paramPhoneNotifier, boolean paramBoolean)
  {
    super(paramString, paramPhoneNotifier, paramContext, new ImsPhoneCommandInterface(paramContext), paramBoolean);
  }
  
  public void activateCellBroadcastSms(int paramInt, Message paramMessage)
  {
    Rlog.e("ImsPhoneBase", "Error! This functionality is not implemented for Volte.");
  }
  
  public boolean canDial()
  {
    int i = getServiceState().getState();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("canDial(): serviceState = ");
    localStringBuilder.append(i);
    Rlog.v("ImsPhoneBase", localStringBuilder.toString());
    boolean bool1 = false;
    if (i == 3) {
      return false;
    }
    String str = SystemProperties.get("ro.telephony.disable-call", "false");
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("canDial(): disableCall = ");
    localStringBuilder.append(str);
    Rlog.v("ImsPhoneBase", localStringBuilder.toString());
    if (str.equals("true")) {
      return false;
    }
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("canDial(): ringingCall: ");
    localStringBuilder.append(getRingingCall().getState());
    Rlog.v("ImsPhoneBase", localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("canDial(): foregndCall: ");
    localStringBuilder.append(getForegroundCall().getState());
    Rlog.v("ImsPhoneBase", localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("canDial(): backgndCall: ");
    localStringBuilder.append(getBackgroundCall().getState());
    Rlog.v("ImsPhoneBase", localStringBuilder.toString());
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
  
  public List<CellInfo> getAllCellInfo(WorkSource paramWorkSource)
  {
    return getServiceStateTracker().getAllCellInfo(paramWorkSource);
  }
  
  public void getAvailableNetworks(Message paramMessage) {}
  
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
    Rlog.e("ImsPhoneBase", "Error! This functionality is not implemented for Volte.");
  }
  
  public CellLocation getCellLocation(WorkSource paramWorkSource)
  {
    return null;
  }
  
  public List<DataConnection> getCurrentDataConnectionList()
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
    Rlog.e("ImsPhoneBase", "[VoltePhone] getEsn() is a CDMA method");
    return "0";
  }
  
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
    Rlog.e("ImsPhoneBase", "[VoltePhone] getMeid() is a CDMA method");
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
    return 5;
  }
  
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
  
  public void migrateFrom(Phone paramPhone)
  {
    super.migrateFrom(paramPhone);
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
  
  public void notifyDisconnect(Connection paramConnection)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("notifyDisconnect, connection = ");
    localStringBuilder.append(paramConnection);
    Rlog.d("ImsPhoneBase", localStringBuilder.toString());
    mDisconnectRegistrants.notifyResult(paramConnection);
    mNotifier.notifyDisconnectCause(paramConnection.getDisconnectCause(), paramConnection.getPreciseDisconnectCause());
  }
  
  public void notifyPhoneStateChanged()
  {
    mNotifier.notifyPhoneState(this);
  }
  
  public void notifyPreciseCallStateChanged()
  {
    super.notifyPreciseCallStateChangedP();
  }
  
  void notifyServiceStateChanged(ServiceState paramServiceState)
  {
    super.notifyServiceStateChangedP(paramServiceState);
  }
  
  public void notifySuppServiceFailed(PhoneInternalInterface.SuppService paramSuppService)
  {
    mSuppServiceFailedRegistrants.notifyResult(paramSuppService);
  }
  
  void notifyUnknownConnection()
  {
    mUnknownConnectionRegistrants.notifyResult(this);
  }
  
  public void onTtyModeReceived(int paramInt)
  {
    AsyncResult localAsyncResult = new AsyncResult(null, Integer.valueOf(paramInt), null);
    mTtyModeReceivedRegistrants.notifyRegistrants(localAsyncResult);
  }
  
  protected void onUpdateIccAvailability() {}
  
  public void registerForOnHoldTone(Handler paramHandler, int paramInt, Object paramObject)
  {
    mOnHoldRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForRingbackTone(Handler paramHandler, int paramInt, Object paramObject)
  {
    mRingbackRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForSuppServiceNotification(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForTtyModeReceived(Handler paramHandler, int paramInt, Object paramObject)
  {
    mTtyModeReceivedRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void selectNetworkManually(OperatorInfo paramOperatorInfo, boolean paramBoolean, Message paramMessage) {}
  
  public void sendUssdResponse(String paramString) {}
  
  public void setCallBarring(String paramString1, boolean paramBoolean, String paramString2, Message paramMessage, int paramInt) {}
  
  public void setCallForwardingOption(int paramInt1, int paramInt2, String paramString, int paramInt3, Message paramMessage) {}
  
  public void setCallWaiting(boolean paramBoolean, Message paramMessage)
  {
    Rlog.e("ImsPhoneBase", "call waiting not supported");
  }
  
  public void setCellBroadcastSmsConfig(int[] paramArrayOfInt, Message paramMessage)
  {
    Rlog.e("ImsPhoneBase", "Error! This functionality is not implemented for Volte.");
  }
  
  public void setDataRoamingEnabled(boolean paramBoolean) {}
  
  public boolean setLine1Number(String paramString1, String paramString2, Message paramMessage)
  {
    return false;
  }
  
  public void setNetworkSelectionModeAutomatic(Message paramMessage) {}
  
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
  
  @VisibleForTesting
  public void startOnHoldTone(Connection paramConnection)
  {
    paramConnection = new Pair(paramConnection, Boolean.TRUE);
    mOnHoldRegistrants.notifyRegistrants(new AsyncResult(null, paramConnection, null));
  }
  
  public void startRingbackTone()
  {
    AsyncResult localAsyncResult = new AsyncResult(null, Boolean.TRUE, null);
    mRingbackRegistrants.notifyRegistrants(localAsyncResult);
  }
  
  public void stopNetworkScan(Message paramMessage) {}
  
  protected void stopOnHoldTone(Connection paramConnection)
  {
    paramConnection = new Pair(paramConnection, Boolean.FALSE);
    mOnHoldRegistrants.notifyRegistrants(new AsyncResult(null, paramConnection, null));
  }
  
  public void stopRingbackTone()
  {
    AsyncResult localAsyncResult = new AsyncResult(null, Boolean.FALSE, null);
    mRingbackRegistrants.notifyRegistrants(localAsyncResult);
  }
  
  public void unregisterForOnHoldTone(Handler paramHandler)
  {
    mOnHoldRegistrants.remove(paramHandler);
  }
  
  public void unregisterForRingbackTone(Handler paramHandler)
  {
    mRingbackRegistrants.remove(paramHandler);
  }
  
  public void unregisterForSuppServiceNotification(Handler paramHandler) {}
  
  public void unregisterForTtyModeReceived(Handler paramHandler)
  {
    mTtyModeReceivedRegistrants.remove(paramHandler);
  }
  
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
      Rlog.d("ImsPhoneBase", ((StringBuilder)localObject).toString());
      notifyPhoneStateChanged();
    }
  }
  
  public void updateServiceLocation() {}
}
