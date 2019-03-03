package com.android.internal.telephony.test;

import android.hardware.radio.V1_0.DataRegStateResult;
import android.hardware.radio.V1_0.SetupDataCallResult;
import android.hardware.radio.V1_0.VoiceRegStateResult;
import android.net.KeepalivePacketData;
import android.net.LinkProperties;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.Registrant;
import android.os.RegistrantList;
import android.os.SystemClock;
import android.os.WorkSource;
import android.service.carrier.CarrierIdentifier;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.ImsiEncryptionInfo;
import android.telephony.NetworkScanRequest;
import android.telephony.Rlog;
import android.telephony.SignalStrength;
import android.telephony.data.DataProfile;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.BaseCommands;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandException.Error;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.CommandsInterface.RadioState;
import com.android.internal.telephony.LastCallFailCause;
import com.android.internal.telephony.RadioCapability;
import com.android.internal.telephony.SmsResponse;
import com.android.internal.telephony.UUSInfo;
import com.android.internal.telephony.cdma.CdmaSmsBroadcastConfigInfo;
import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import com.android.internal.telephony.gsm.SuppServiceNotification;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccIoResult;
import com.android.internal.telephony.uicc.IccSlotStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulatedCommands
  extends BaseCommands
  implements CommandsInterface, SimulatedRadioControl
{
  public static final int DEFAULT_PIN1_ATTEMPT = 5;
  public static final int DEFAULT_PIN2_ATTEMPT = 5;
  public static final String DEFAULT_SIM_PIN2_CODE = "5678";
  public static final String DEFAULT_SIM_PIN_CODE = "1234";
  public static final String FAKE_ESN = "1234";
  public static final String FAKE_IMEI = "012345678901234";
  public static final String FAKE_IMEISV = "99";
  public static final String FAKE_LONG_NAME = "Fake long name";
  public static final String FAKE_MCC_MNC = "310260";
  public static final String FAKE_MEID = "1234";
  public static final String FAKE_SHORT_NAME = "Fake short name";
  private static final SimFdnState INITIAL_FDN_STATE = SimFdnState.NONE;
  private static final SimLockState INITIAL_LOCK_STATE = SimLockState.NONE;
  private static final String LOG_TAG = "SimulatedCommands";
  private static final String SIM_PUK2_CODE = "87654321";
  private static final String SIM_PUK_CODE = "12345678";
  private final AtomicInteger getNetworkSelectionModeCallCount;
  private AtomicBoolean mAllowed;
  private List<CellInfo> mCellInfoList;
  private int mChannelId;
  public boolean mCssSupported;
  private int mDataRadioTech;
  private int mDataRegState;
  private boolean mDcSuccess;
  public int mDefaultRoamingIndicator;
  private final AtomicInteger mGetDataRegistrationStateCallCount;
  private final AtomicInteger mGetOperatorCallCount;
  private final AtomicInteger mGetVoiceRegistrationStateCallCount;
  HandlerThread mHandlerThread;
  private IccCardStatus mIccCardStatus;
  private IccIoResult mIccIoResultForApduLogicalChannel;
  private IccSlotStatus mIccSlotStatus;
  private String mImei;
  private String mImeiSv;
  private int[] mImsRegState;
  private boolean mIsRadioPowerFailResponse;
  public int mMaxDataCalls;
  int mNetworkType;
  int mNextCallFailCause;
  int mPausedResponseCount;
  ArrayList<Message> mPausedResponses;
  int mPin1attemptsRemaining = 5;
  String mPin2Code;
  int mPin2UnlockAttempts;
  String mPinCode;
  int mPinUnlockAttempts;
  int mPuk2UnlockAttempts;
  int mPukUnlockAttempts;
  public int mReasonForDenial;
  public int mRoamingIndicator;
  private SetupDataCallResult mSetupDataCallResult;
  private SignalStrength mSignalStrength;
  boolean mSimFdnEnabled;
  SimFdnState mSimFdnEnabledState;
  boolean mSimLockEnabled;
  SimLockState mSimLockedState;
  boolean mSsnNotifyOn;
  public int mSystemIsInPrl;
  private int mVoiceRadioTech;
  private int mVoiceRegState;
  SimulatedGsmCallState simulatedCallState;
  private String smscAddress;
  
  public SimulatedCommands()
  {
    super(null);
    boolean bool1 = false;
    mSsnNotifyOn = false;
    mVoiceRegState = 1;
    mVoiceRadioTech = 3;
    mDataRegState = 1;
    mDataRadioTech = 3;
    mChannelId = -1;
    mPausedResponses = new ArrayList();
    mNextCallFailCause = 16;
    mDcSuccess = true;
    mIsRadioPowerFailResponse = false;
    mGetVoiceRegistrationStateCallCount = new AtomicInteger(0);
    mGetDataRegistrationStateCallCount = new AtomicInteger(0);
    mGetOperatorCallCount = new AtomicInteger(0);
    getNetworkSelectionModeCallCount = new AtomicInteger(0);
    mAllowed = new AtomicBoolean(false);
    mHandlerThread = new HandlerThread("SimulatedCommands");
    mHandlerThread.start();
    simulatedCallState = new SimulatedGsmCallState(mHandlerThread.getLooper());
    setRadioState(CommandsInterface.RadioState.RADIO_ON);
    mSimLockedState = INITIAL_LOCK_STATE;
    if (mSimLockedState != SimLockState.NONE) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mSimLockEnabled = bool2;
    mPinCode = "1234";
    mSimFdnEnabledState = INITIAL_FDN_STATE;
    boolean bool2 = bool1;
    if (mSimFdnEnabledState != SimFdnState.NONE) {
      bool2 = true;
    }
    mSimFdnEnabled = bool2;
    mPin2Code = "5678";
  }
  
  private boolean isSimLocked()
  {
    return mSimLockedState != SimLockState.NONE;
  }
  
  private void log(String paramString)
  {
    Rlog.d("SimulatedCommands", paramString);
  }
  
  private void resultFail(Message paramMessage, Object paramObject, Throwable paramThrowable)
  {
    if (paramMessage != null)
    {
      AsyncResult.forMessage(paramMessage, paramObject, paramThrowable);
      if (mPausedResponseCount > 0) {
        mPausedResponses.add(paramMessage);
      } else {
        paramMessage.sendToTarget();
      }
    }
  }
  
  private void resultSuccess(Message paramMessage, Object paramObject)
  {
    if (paramMessage != null)
    {
      forMessageresult = paramObject;
      if (mPausedResponseCount > 0) {
        mPausedResponses.add(paramMessage);
      } else {
        paramMessage.sendToTarget();
      }
    }
  }
  
  private void unimplemented(Message paramMessage)
  {
    if (paramMessage != null)
    {
      forMessageexception = new RuntimeException("Unimplemented");
      if (mPausedResponseCount > 0) {
        mPausedResponses.add(paramMessage);
      } else {
        paramMessage.sendToTarget();
      }
    }
  }
  
  public void acceptCall(Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().acceptCall(paramMessage);
    if (!simulatedCallState.onAnswer()) {
      resultFail(paramMessage, null, new RuntimeException("Hangup Error"));
    } else {
      resultSuccess(paramMessage, null);
    }
  }
  
  public void acknowledgeIncomingGsmSmsWithPdu(boolean paramBoolean, String paramString, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void acknowledgeLastIncomingCdmaSms(boolean paramBoolean, int paramInt, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void acknowledgeLastIncomingGsmSms(boolean paramBoolean, int paramInt, Message paramMessage)
  {
    unimplemented(paramMessage);
    SimulatedCommandsVerifier.getInstance().acknowledgeLastIncomingGsmSms(paramBoolean, paramInt, paramMessage);
  }
  
  public void cancelPendingUssd(Message paramMessage)
  {
    resultSuccess(paramMessage, null);
  }
  
  public void changeBarringPassword(String paramString1, String paramString2, String paramString3, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void changeIccPin(String paramString1, String paramString2, Message paramMessage)
  {
    if ((paramString1 != null) && (paramString1.equals(mPinCode)))
    {
      mPinCode = paramString2;
      resultSuccess(paramMessage, null);
      return;
    }
    Rlog.i("SimulatedCommands", "[SimCmd] changeIccPin: pin failed!");
    resultFail(paramMessage, null, new CommandException(CommandException.Error.PASSWORD_INCORRECT));
  }
  
  public void changeIccPin2(String paramString1, String paramString2, Message paramMessage)
  {
    if ((paramString1 != null) && (paramString1.equals(mPin2Code)))
    {
      mPin2Code = paramString2;
      resultSuccess(paramMessage, null);
      return;
    }
    Rlog.i("SimulatedCommands", "[SimCmd] changeIccPin2: pin2 failed!");
    resultFail(paramMessage, null, new CommandException(CommandException.Error.PASSWORD_INCORRECT));
  }
  
  public void changeIccPin2ForApp(String paramString1, String paramString2, String paramString3, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void changeIccPinForApp(String paramString1, String paramString2, String paramString3, Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().changeIccPinForApp(paramString1, paramString2, paramString3, paramMessage);
    changeIccPin(paramString1, paramString2, paramMessage);
  }
  
  public void conference(Message paramMessage)
  {
    if (!simulatedCallState.onChld('3', '\000')) {
      resultFail(paramMessage, null, new RuntimeException("Hangup Error"));
    } else {
      resultSuccess(paramMessage, null);
    }
  }
  
  public void deactivateDataCall(int paramInt1, int paramInt2, Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().deactivateDataCall(paramInt1, paramInt2, paramMessage);
    resultSuccess(paramMessage, null);
  }
  
  public void deleteSmsOnRuim(int paramInt, Message paramMessage)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Delete RUIM message at index ");
    localStringBuilder.append(paramInt);
    Rlog.d("SimulatedCommands", localStringBuilder.toString());
    unimplemented(paramMessage);
  }
  
  public void deleteSmsOnSim(int paramInt, Message paramMessage)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Delete message at index ");
    localStringBuilder.append(paramInt);
    Rlog.d("SimulatedCommands", localStringBuilder.toString());
    unimplemented(paramMessage);
  }
  
  public void dial(String paramString, int paramInt, Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().dial(paramString, paramInt, paramMessage);
    simulatedCallState.onDial(paramString);
    resultSuccess(paramMessage, null);
  }
  
  public void dial(String paramString, int paramInt, UUSInfo paramUUSInfo, Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().dial(paramString, paramInt, paramUUSInfo, paramMessage);
    simulatedCallState.onDial(paramString);
    resultSuccess(paramMessage, null);
  }
  
  public void dispose()
  {
    if (mHandlerThread != null) {
      mHandlerThread.quit();
    }
  }
  
  public void exitEmergencyCallbackMode(Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void explicitCallTransfer(Message paramMessage)
  {
    if (!simulatedCallState.onChld('4', '\000')) {
      resultFail(paramMessage, null, new RuntimeException("Hangup Error"));
    } else {
      resultSuccess(paramMessage, null);
    }
  }
  
  public void forceDataDormancy(Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void getAllowedCarriers(Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void getAvailableNetworks(Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void getBasebandVersion(Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().getBasebandVersion(paramMessage);
    resultSuccess(paramMessage, "SimulatedCommands");
  }
  
  public void getCDMASubscription(Message paramMessage)
  {
    resultSuccess(paramMessage, new String[] { "123", "456", "789", "234", "345" });
  }
  
  public void getCLIR(Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void getCdmaBroadcastConfig(Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void getCdmaSubscriptionSource(Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void getCellInfoList(Message paramMessage, WorkSource paramWorkSource)
  {
    if (mCellInfoList == null)
    {
      paramWorkSource = Parcel.obtain();
      paramWorkSource.writeInt(1);
      paramWorkSource.writeInt(1);
      paramWorkSource.writeInt(2);
      paramWorkSource.writeLong(1453510289108L);
      paramWorkSource.writeInt(310);
      paramWorkSource.writeInt(260);
      paramWorkSource.writeInt(123);
      paramWorkSource.writeInt(456);
      paramWorkSource.writeInt(99);
      paramWorkSource.writeInt(3);
      paramWorkSource.setDataPosition(0);
      paramWorkSource = (CellInfoGsm)CellInfoGsm.CREATOR.createFromParcel(paramWorkSource);
      new ArrayList().add(paramWorkSource);
    }
    resultSuccess(paramMessage, mCellInfoList);
  }
  
  public void getCurrentCalls(Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().getCurrentCalls(paramMessage);
    if ((mState == CommandsInterface.RadioState.RADIO_ON) && (!isSimLocked())) {
      resultSuccess(paramMessage, simulatedCallState.getDriverCalls());
    } else {
      resultFail(paramMessage, null, new CommandException(CommandException.Error.RADIO_NOT_AVAILABLE));
    }
  }
  
  public void getDataCallList(Message paramMessage)
  {
    resultSuccess(paramMessage, new ArrayList(0));
  }
  
  public void getDataRegistrationState(Message paramMessage)
  {
    mGetDataRegistrationStateCallCount.incrementAndGet();
    DataRegStateResult localDataRegStateResult = new DataRegStateResult();
    regState = mDataRegState;
    rat = mDataRadioTech;
    maxDataCalls = mMaxDataCalls;
    reasonDataDenied = mReasonForDenial;
    resultSuccess(paramMessage, localDataRegStateResult);
  }
  
  public void getDeviceIdentity(Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().getDeviceIdentity(paramMessage);
    resultSuccess(paramMessage, new String[] { "012345678901234", "99", "1234", "1234" });
  }
  
  @VisibleForTesting
  public int getGetDataRegistrationStateCallCount()
  {
    return mGetDataRegistrationStateCallCount.get();
  }
  
  @VisibleForTesting
  public int getGetNetworkSelectionModeCallCount()
  {
    return getNetworkSelectionModeCallCount.get();
  }
  
  @VisibleForTesting
  public int getGetOperatorCallCount()
  {
    mGetOperatorCallCount.get();
    return mGetOperatorCallCount.get();
  }
  
  @VisibleForTesting
  public int getGetVoiceRegistrationStateCallCount()
  {
    return mGetVoiceRegistrationStateCallCount.get();
  }
  
  public void getGsmBroadcastConfig(Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void getHardwareConfig(Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void getIMEI(Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().getIMEI(paramMessage);
    String str;
    if (mImei != null) {
      str = mImei;
    } else {
      str = "012345678901234";
    }
    resultSuccess(paramMessage, str);
  }
  
  public void getIMEISV(Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().getIMEISV(paramMessage);
    String str;
    if (mImeiSv != null) {
      str = mImeiSv;
    } else {
      str = "99";
    }
    resultSuccess(paramMessage, str);
  }
  
  public void getIMSI(Message paramMessage)
  {
    getIMSIForApp(null, paramMessage);
  }
  
  public void getIMSIForApp(String paramString, Message paramMessage)
  {
    resultSuccess(paramMessage, "012345678901234");
  }
  
  public void getIccCardStatus(Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().getIccCardStatus(paramMessage);
    if (mIccCardStatus != null) {
      resultSuccess(paramMessage, mIccCardStatus);
    } else {
      resultFail(paramMessage, null, new RuntimeException("IccCardStatus not set"));
    }
  }
  
  public void getIccSlotsStatus(Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().getIccSlotsStatus(paramMessage);
    if (mIccSlotStatus != null) {
      resultSuccess(paramMessage, mIccSlotStatus);
    } else {
      resultFail(paramMessage, null, new CommandException(CommandException.Error.REQUEST_NOT_SUPPORTED));
    }
  }
  
  public void getImsRegistrationState(Message paramMessage)
  {
    if (mImsRegState == null) {
      mImsRegState = new int[] { 1, 0 };
    }
    resultSuccess(paramMessage, mImsRegState);
  }
  
  public void getLastCallFailCause(Message paramMessage)
  {
    LastCallFailCause localLastCallFailCause = new LastCallFailCause();
    causeCode = mNextCallFailCause;
    resultSuccess(paramMessage, localLastCallFailCause);
  }
  
  public void getLastDataCallFailCause(Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  @Deprecated
  public void getLastPdpFailCause(Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void getModemActivityInfo(Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void getMute(Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void getNeighboringCids(Message paramMessage, WorkSource paramWorkSource)
  {
    paramWorkSource = new int[7];
    paramWorkSource[0] = 6;
    for (int i = 1; i < 7; i++) {
      paramWorkSource[i] = i;
    }
    resultSuccess(paramMessage, paramWorkSource);
  }
  
  public void getNetworkSelectionMode(Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().getNetworkSelectionMode(paramMessage);
    getNetworkSelectionModeCallCount.incrementAndGet();
    resultSuccess(paramMessage, new int[] { 0 });
  }
  
  public void getOperator(Message paramMessage)
  {
    mGetOperatorCallCount.incrementAndGet();
    resultSuccess(paramMessage, new String[] { "Fake long name", "Fake short name", "310260" });
  }
  
  @Deprecated
  public void getPDPContextList(Message paramMessage)
  {
    getDataCallList(paramMessage);
  }
  
  public void getPreferredNetworkType(Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().getPreferredNetworkType(paramMessage);
    resultSuccess(paramMessage, new int[] { mNetworkType });
  }
  
  public void getPreferredVoicePrivacy(Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void getRadioCapability(Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().getRadioCapability(paramMessage);
    resultSuccess(paramMessage, new RadioCapability(0, 0, 0, 65535, null, 0));
  }
  
  public int getRilVersion()
  {
    return 11;
  }
  
  public void getSignalStrength(Message paramMessage)
  {
    if (mSignalStrength == null) {
      mSignalStrength = new SignalStrength(20, 0, -1, -1, -1, -1, -1, 99, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
    resultSuccess(paramMessage, mSignalStrength);
  }
  
  public void getSmscAddress(Message paramMessage)
  {
    resultSuccess(paramMessage, smscAddress);
    SimulatedCommandsVerifier.getInstance().getSmscAddress(paramMessage);
  }
  
  public void getVoiceRadioTechnology(Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().getVoiceRadioTechnology(paramMessage);
    resultSuccess(paramMessage, new int[] { mVoiceRadioTech });
  }
  
  public void getVoiceRegistrationState(Message paramMessage)
  {
    mGetVoiceRegistrationStateCallCount.incrementAndGet();
    VoiceRegStateResult localVoiceRegStateResult = new VoiceRegStateResult();
    regState = mVoiceRegState;
    rat = mVoiceRadioTech;
    cssSupported = mCssSupported;
    roamingIndicator = mRoamingIndicator;
    systemIsInPrl = mSystemIsInPrl;
    defaultRoamingIndicator = mDefaultRoamingIndicator;
    reasonForDenial = mReasonForDenial;
    resultSuccess(paramMessage, localVoiceRegStateResult);
  }
  
  public void handleCallSetupRequestFromSim(boolean paramBoolean, Message paramMessage)
  {
    resultSuccess(paramMessage, null);
  }
  
  public void hangupConnection(int paramInt, Message paramMessage)
  {
    if (!simulatedCallState.onChld('1', (char)(48 + paramInt)))
    {
      Rlog.i("GSM", "[SimCmd] hangupConnection: resultFail");
      resultFail(paramMessage, null, new RuntimeException("Hangup Error"));
    }
    else
    {
      Rlog.i("GSM", "[SimCmd] hangupConnection: resultSuccess");
      resultSuccess(paramMessage, null);
    }
  }
  
  public void hangupForegroundResumeBackground(Message paramMessage)
  {
    if (!simulatedCallState.onChld('1', '\000')) {
      resultFail(paramMessage, null, new RuntimeException("Hangup Error"));
    } else {
      resultSuccess(paramMessage, null);
    }
  }
  
  public void hangupWaitingOrBackground(Message paramMessage)
  {
    if (!simulatedCallState.onChld('0', '\000')) {
      resultFail(paramMessage, null, new RuntimeException("Hangup Error"));
    } else {
      resultSuccess(paramMessage, null);
    }
  }
  
  public void iccCloseLogicalChannel(int paramInt, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void iccIO(int paramInt1, int paramInt2, String paramString1, int paramInt3, int paramInt4, int paramInt5, String paramString2, String paramString3, Message paramMessage)
  {
    iccIOForApp(paramInt1, paramInt2, paramString1, paramInt3, paramInt4, paramInt5, paramString2, paramString3, null, paramMessage);
  }
  
  public void iccIOForApp(int paramInt1, int paramInt2, String paramString1, int paramInt3, int paramInt4, int paramInt5, String paramString2, String paramString3, String paramString4, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void iccOpenLogicalChannel(String paramString, int paramInt, Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().iccOpenLogicalChannel(paramString, paramInt, paramMessage);
    resultSuccess(paramMessage, new int[] { mChannelId });
  }
  
  public void iccTransmitApduBasicChannel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, String paramString, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void iccTransmitApduLogicalChannel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, String paramString, Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().iccTransmitApduLogicalChannel(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramString, paramMessage);
    if (mIccIoResultForApduLogicalChannel != null) {
      resultSuccess(paramMessage, mIccIoResultForApduLogicalChannel);
    } else {
      resultFail(paramMessage, null, new RuntimeException("IccIoResult not set"));
    }
  }
  
  public void invokeOemRilRequestRaw(byte[] paramArrayOfByte, Message paramMessage)
  {
    if (paramMessage != null)
    {
      forMessageresult = paramArrayOfByte;
      paramMessage.sendToTarget();
    }
  }
  
  public void invokeOemRilRequestStrings(String[] paramArrayOfString, Message paramMessage)
  {
    if (paramMessage != null)
    {
      forMessageresult = paramArrayOfString;
      paramMessage.sendToTarget();
    }
  }
  
  @VisibleForTesting
  public boolean isDataAllowed()
  {
    return mAllowed.get();
  }
  
  public void notifyEmergencyCallbackMode()
  {
    if (mEmergencyCallbackModeRegistrant != null) {
      mEmergencyCallbackModeRegistrant.notifyRegistrant();
    }
  }
  
  public void notifyExitEmergencyCallbackMode()
  {
    if (mExitEmergencyCallbackModeRegistrants != null) {
      mExitEmergencyCallbackModeRegistrants.notifyRegistrants(new AsyncResult(null, null, null));
    }
  }
  
  public void notifyGsmBroadcastSms(Object paramObject)
  {
    if (mGsmBroadcastSmsRegistrant != null) {
      mGsmBroadcastSmsRegistrant.notifyRegistrant(new AsyncResult(null, paramObject, null));
    }
  }
  
  public void notifyIccSmsFull()
  {
    if (mIccSmsFullRegistrant != null) {
      mIccSmsFullRegistrant.notifyRegistrant();
    }
  }
  
  public void notifyImsNetworkStateChanged()
  {
    if (mImsNetworkStateChangedRegistrants != null) {
      mImsNetworkStateChangedRegistrants.notifyRegistrants();
    }
  }
  
  public void notifyModemReset()
  {
    if (mModemResetRegistrants != null) {
      mModemResetRegistrants.notifyRegistrants(new AsyncResult(null, "Test", null));
    }
  }
  
  @VisibleForTesting
  public void notifyNetworkStateChanged()
  {
    mNetworkStateRegistrants.notifyRegistrants();
  }
  
  @VisibleForTesting
  public void notifyOtaProvisionStatusChanged()
  {
    if (mOtaProvisionRegistrants != null) {
      mOtaProvisionRegistrants.notifyRegistrants(new AsyncResult(null, new int[] { 8 }, null));
    }
  }
  
  public void notifyRadioOn()
  {
    mOnRegistrants.notifyRegistrants();
  }
  
  public void notifySignalStrength()
  {
    if (mSignalStrength == null) {
      mSignalStrength = new SignalStrength(20, 0, -1, -1, -1, -1, -1, 99, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }
    if (mSignalStrengthRegistrant != null) {
      mSignalStrengthRegistrant.notifyRegistrant(new AsyncResult(null, mSignalStrength, null));
    }
  }
  
  public void notifySmsStatus(Object paramObject)
  {
    if (mSmsStatusRegistrant != null) {
      mSmsStatusRegistrant.notifyRegistrant(new AsyncResult(null, paramObject, null));
    }
  }
  
  public void nvReadItem(int paramInt, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void nvResetConfig(int paramInt, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void nvWriteCdmaPrl(byte[] paramArrayOfByte, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void nvWriteItem(int paramInt, String paramString, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void pauseResponses()
  {
    mPausedResponseCount += 1;
  }
  
  public void progressConnectingCallState()
  {
    simulatedCallState.progressConnectingCallState();
    mCallStateRegistrants.notifyRegistrants();
  }
  
  public void progressConnectingToActive()
  {
    simulatedCallState.progressConnectingToActive();
    mCallStateRegistrants.notifyRegistrants();
  }
  
  public void pullLceData(Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void queryAvailableBandMode(Message paramMessage)
  {
    resultSuccess(paramMessage, new int[] { 4, 2, 3, 4 });
  }
  
  public void queryCLIP(Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void queryCallForwardStatus(int paramInt1, int paramInt2, String paramString, Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().queryCallForwardStatus(paramInt1, paramInt2, paramString, paramMessage);
    resultSuccess(paramMessage, null);
  }
  
  public void queryCallWaiting(int paramInt, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void queryCdmaRoamingPreference(Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void queryFacilityLock(String paramString1, String paramString2, int paramInt, Message paramMessage)
  {
    queryFacilityLockForApp(paramString1, paramString2, paramInt, null, paramMessage);
  }
  
  public void queryFacilityLockForApp(String paramString1, String paramString2, int paramInt, String paramString3, Message paramMessage)
  {
    if ((paramString1 != null) && (paramString1.equals("SC")))
    {
      if (paramMessage != null)
      {
        paramString2 = new int[1];
        paramString2[0] = mSimLockEnabled;
        paramString3 = new StringBuilder();
        paramString3.append("[SimCmd] queryFacilityLock: SIM is ");
        if (paramString2[0] == 0) {
          paramString1 = "unlocked";
        } else {
          paramString1 = "locked";
        }
        paramString3.append(paramString1);
        Rlog.i("SimulatedCommands", paramString3.toString());
        resultSuccess(paramMessage, paramString2);
      }
      return;
    }
    if ((paramString1 != null) && (paramString1.equals("FD")))
    {
      if (paramMessage != null)
      {
        paramString2 = new int[1];
        paramString2[0] = mSimFdnEnabled;
        paramString3 = new StringBuilder();
        paramString3.append("[SimCmd] queryFacilityLock: FDN is ");
        if (paramString2[0] == 0) {
          paramString1 = "disabled";
        } else {
          paramString1 = "enabled";
        }
        paramString3.append(paramString1);
        Rlog.i("SimulatedCommands", paramString3.toString());
        resultSuccess(paramMessage, paramString2);
      }
      return;
    }
    unimplemented(paramMessage);
  }
  
  public void queryTTYMode(Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void registerForExitEmergencyCallbackMode(Handler paramHandler, int paramInt, Object paramObject)
  {
    SimulatedCommandsVerifier.getInstance().registerForExitEmergencyCallbackMode(paramHandler, paramInt, paramObject);
    super.registerForExitEmergencyCallbackMode(paramHandler, paramInt, paramObject);
  }
  
  public void registerForIccRefresh(Handler paramHandler, int paramInt, Object paramObject)
  {
    super.registerForIccRefresh(paramHandler, paramInt, paramObject);
    SimulatedCommandsVerifier.getInstance().registerForIccRefresh(paramHandler, paramInt, paramObject);
  }
  
  public void registerForLceInfo(Handler paramHandler, int paramInt, Object paramObject)
  {
    SimulatedCommandsVerifier.getInstance().registerForLceInfo(paramHandler, paramInt, paramObject);
  }
  
  public void registerForModemReset(Handler paramHandler, int paramInt, Object paramObject)
  {
    SimulatedCommandsVerifier.getInstance().registerForModemReset(paramHandler, paramInt, paramObject);
    super.registerForModemReset(paramHandler, paramInt, paramObject);
  }
  
  public void registerForNattKeepaliveStatus(Handler paramHandler, int paramInt, Object paramObject)
  {
    SimulatedCommandsVerifier.getInstance().registerForNattKeepaliveStatus(paramHandler, paramInt, paramObject);
  }
  
  public void registerForPcoData(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void rejectCall(Message paramMessage)
  {
    if (!simulatedCallState.onChld('0', '\000')) {
      resultFail(paramMessage, null, new RuntimeException("Hangup Error"));
    } else {
      resultSuccess(paramMessage, null);
    }
  }
  
  public void reportSmsMemoryStatus(boolean paramBoolean, Message paramMessage)
  {
    resultSuccess(paramMessage, null);
    SimulatedCommandsVerifier.getInstance().reportSmsMemoryStatus(paramBoolean, paramMessage);
  }
  
  public void reportStkServiceIsRunning(Message paramMessage)
  {
    resultSuccess(paramMessage, null);
  }
  
  public void requestIccSimAuthentication(int paramInt, String paramString1, String paramString2, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void requestShutdown(Message paramMessage)
  {
    setRadioState(CommandsInterface.RadioState.RADIO_UNAVAILABLE);
  }
  
  public void resetRadio(Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void resumeResponses()
  {
    mPausedResponseCount -= 1;
    if (mPausedResponseCount == 0)
    {
      int i = 0;
      int j = mPausedResponses.size();
      while (i < j)
      {
        ((Message)mPausedResponses.get(i)).sendToTarget();
        i++;
      }
      mPausedResponses.clear();
    }
    else
    {
      Rlog.e("GSM", "SimulatedCommands.resumeResponses < 0");
    }
  }
  
  public void sendBurstDtmf(String paramString, int paramInt1, int paramInt2, Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().sendBurstDtmf(paramString, paramInt1, paramInt2, paramMessage);
    resultSuccess(paramMessage, null);
  }
  
  public void sendCDMAFeatureCode(String paramString, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void sendCdmaSms(byte[] paramArrayOfByte, Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().sendCdmaSms(paramArrayOfByte, paramMessage);
    resultSuccess(paramMessage, null);
  }
  
  public void sendCdmaSms(byte[] paramArrayOfByte, Message paramMessage, boolean paramBoolean) {}
  
  public void sendDeviceState(int paramInt, boolean paramBoolean, Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().sendDeviceState(paramInt, paramBoolean, paramMessage);
    resultSuccess(paramMessage, null);
  }
  
  public void sendDtmf(char paramChar, Message paramMessage)
  {
    resultSuccess(paramMessage, null);
  }
  
  public void sendEnvelope(String paramString, Message paramMessage)
  {
    resultSuccess(paramMessage, null);
  }
  
  public void sendEnvelopeWithStatus(String paramString, Message paramMessage)
  {
    resultSuccess(paramMessage, null);
  }
  
  public void sendImsCdmaSms(byte[] paramArrayOfByte, int paramInt1, int paramInt2, Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().sendImsCdmaSms(paramArrayOfByte, paramInt1, paramInt2, paramMessage);
    resultSuccess(paramMessage, new SmsResponse(0, null, 0));
  }
  
  public void sendImsGsmSms(String paramString1, String paramString2, int paramInt1, int paramInt2, Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().sendImsGsmSms(paramString1, paramString2, paramInt1, paramInt2, paramMessage);
    resultSuccess(paramMessage, new SmsResponse(0, null, 0));
  }
  
  public void sendSMS(String paramString1, String paramString2, Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().sendSMS(paramString1, paramString2, paramMessage);
    resultSuccess(paramMessage, new SmsResponse(0, null, 0));
  }
  
  public void sendSMSExpectMore(String paramString1, String paramString2, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void sendStkCcAplha(String paramString)
  {
    triggerIncomingStkCcAlpha(paramString);
  }
  
  public void sendTerminalResponse(String paramString, Message paramMessage)
  {
    resultSuccess(paramMessage, null);
  }
  
  public void sendUSSD(String paramString, Message paramMessage)
  {
    if (paramString.equals("#646#"))
    {
      resultSuccess(paramMessage, null);
      triggerIncomingUssd("0", "You have NNN minutes remaining.");
    }
    else
    {
      resultSuccess(paramMessage, null);
      triggerIncomingUssd("0", "All Done");
    }
  }
  
  public void separateConnection(int paramInt, Message paramMessage)
  {
    char c = (char)(paramInt + 48);
    if (!simulatedCallState.onChld('2', c)) {
      resultFail(paramMessage, null, new RuntimeException("Hangup Error"));
    } else {
      resultSuccess(paramMessage, null);
    }
  }
  
  public void setAllowedCarriers(List<CarrierIdentifier> paramList, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void setAutoProgressConnectingCall(boolean paramBoolean)
  {
    simulatedCallState.setAutoProgressConnectingCall(paramBoolean);
  }
  
  public void setBandMode(int paramInt, Message paramMessage)
  {
    resultSuccess(paramMessage, null);
  }
  
  public void setCLIR(int paramInt, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void setCallForward(int paramInt1, int paramInt2, int paramInt3, String paramString, int paramInt4, Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().setCallForward(paramInt1, paramInt2, paramInt3, paramString, paramInt4, paramMessage);
    resultSuccess(paramMessage, null);
  }
  
  public void setCallWaiting(boolean paramBoolean, int paramInt, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void setCarrierInfoForImsiEncryption(ImsiEncryptionInfo paramImsiEncryptionInfo, Message paramMessage)
  {
    if (paramMessage != null)
    {
      forMessageresult = paramImsiEncryptionInfo;
      paramMessage.sendToTarget();
    }
  }
  
  public void setCdmaBroadcastActivation(boolean paramBoolean, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void setCdmaBroadcastConfig(CdmaSmsBroadcastConfigInfo[] paramArrayOfCdmaSmsBroadcastConfigInfo, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void setCdmaRoamingPreference(int paramInt, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void setCdmaSubscriptionSource(int paramInt, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void setCellInfoList(List<CellInfo> paramList)
  {
    mCellInfoList = paramList;
  }
  
  public void setCellInfoListRate(int paramInt, Message paramMessage, WorkSource paramWorkSource)
  {
    unimplemented(paramMessage);
  }
  
  public void setDataAllowed(boolean paramBoolean, Message paramMessage)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("setDataAllowed = ");
    localStringBuilder.append(paramBoolean);
    log(localStringBuilder.toString());
    mAllowed.set(paramBoolean);
    resultSuccess(paramMessage, null);
  }
  
  public void setDataCallResult(boolean paramBoolean, SetupDataCallResult paramSetupDataCallResult)
  {
    mSetupDataCallResult = paramSetupDataCallResult;
    mDcSuccess = paramBoolean;
  }
  
  public void setDataProfile(DataProfile[] paramArrayOfDataProfile, boolean paramBoolean, Message paramMessage) {}
  
  public void setDataRadioTech(int paramInt)
  {
    mDataRadioTech = paramInt;
  }
  
  public void setDataRegState(int paramInt)
  {
    mDataRegState = paramInt;
  }
  
  public void setEmergencyCallbackMode(Handler paramHandler, int paramInt, Object paramObject)
  {
    SimulatedCommandsVerifier.getInstance().setEmergencyCallbackMode(paramHandler, paramInt, paramObject);
    super.setEmergencyCallbackMode(paramHandler, paramInt, paramObject);
  }
  
  public void setFacilityLock(String paramString1, boolean paramBoolean, String paramString2, int paramInt, Message paramMessage)
  {
    setFacilityLockForApp(paramString1, paramBoolean, paramString2, paramInt, null, paramMessage);
  }
  
  public void setFacilityLockForApp(String paramString1, boolean paramBoolean, String paramString2, int paramInt, String paramString3, Message paramMessage)
  {
    if ((paramString1 != null) && (paramString1.equals("SC")))
    {
      if ((paramString2 != null) && (paramString2.equals(mPinCode)))
      {
        Rlog.i("SimulatedCommands", "[SimCmd] setFacilityLock: pin is valid");
        mSimLockEnabled = paramBoolean;
        resultSuccess(paramMessage, null);
        return;
      }
      Rlog.i("SimulatedCommands", "[SimCmd] setFacilityLock: pin failed!");
      resultFail(paramMessage, null, new CommandException(CommandException.Error.GENERIC_FAILURE));
      return;
    }
    if ((paramString1 != null) && (paramString1.equals("FD")))
    {
      if ((paramString2 != null) && (paramString2.equals(mPin2Code)))
      {
        Rlog.i("SimulatedCommands", "[SimCmd] setFacilityLock: pin2 is valid");
        mSimFdnEnabled = paramBoolean;
        resultSuccess(paramMessage, null);
        return;
      }
      Rlog.i("SimulatedCommands", "[SimCmd] setFacilityLock: pin2 failed!");
      resultFail(paramMessage, null, new CommandException(CommandException.Error.GENERIC_FAILURE));
      return;
    }
    unimplemented(paramMessage);
  }
  
  public void setGsmBroadcastActivation(boolean paramBoolean, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void setGsmBroadcastConfig(SmsBroadcastConfigInfo[] paramArrayOfSmsBroadcastConfigInfo, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void setIMEI(String paramString)
  {
    mImei = paramString;
  }
  
  public void setIMEISV(String paramString)
  {
    mImeiSv = paramString;
  }
  
  public void setIccCardStatus(IccCardStatus paramIccCardStatus)
  {
    mIccCardStatus = paramIccCardStatus;
  }
  
  public void setIccIoResultForApduLogicalChannel(IccIoResult paramIccIoResult)
  {
    mIccIoResultForApduLogicalChannel = paramIccIoResult;
  }
  
  public void setIccSlotStatus(IccSlotStatus paramIccSlotStatus)
  {
    mIccSlotStatus = paramIccSlotStatus;
  }
  
  public void setImsRegistrationState(int[] paramArrayOfInt)
  {
    mImsRegState = paramArrayOfInt;
  }
  
  public void setInitialAttachApn(DataProfile paramDataProfile, boolean paramBoolean, Message paramMessage) {}
  
  public void setLinkCapacityReportingCriteria(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt4, Message paramMessage) {}
  
  public void setLocationUpdates(boolean paramBoolean, Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().setLocationUpdates(paramBoolean, paramMessage);
    resultSuccess(paramMessage, null);
  }
  
  public void setLogicalToPhysicalSlotMapping(int[] paramArrayOfInt, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void setMute(boolean paramBoolean, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void setNetworkSelectionModeAutomatic(Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void setNetworkSelectionModeManual(String paramString, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void setNextCallFailCause(int paramInt)
  {
    mNextCallFailCause = paramInt;
  }
  
  public void setNextDialFailImmediately(boolean paramBoolean)
  {
    simulatedCallState.setNextDialFailImmediately(paramBoolean);
  }
  
  public void setOnRestrictedStateChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    super.setOnRestrictedStateChanged(paramHandler, paramInt, paramObject);
    SimulatedCommandsVerifier.getInstance().setOnRestrictedStateChanged(paramHandler, paramInt, paramObject);
  }
  
  public void setOpenChannelId(int paramInt)
  {
    mChannelId = paramInt;
  }
  
  public void setPhoneType(int paramInt) {}
  
  public void setPin1RemainingAttempt(int paramInt)
  {
    mPin1attemptsRemaining = paramInt;
  }
  
  public void setPreferredNetworkType(int paramInt, Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().setPreferredNetworkType(paramInt, paramMessage);
    mNetworkType = paramInt;
    resultSuccess(paramMessage, null);
  }
  
  public void setPreferredVoicePrivacy(boolean paramBoolean, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void setRadioPower(boolean paramBoolean, Message paramMessage)
  {
    if (mIsRadioPowerFailResponse)
    {
      resultFail(paramMessage, null, new RuntimeException("setRadioPower failed!"));
      return;
    }
    if (paramBoolean) {
      setRadioState(CommandsInterface.RadioState.RADIO_ON);
    } else {
      setRadioState(CommandsInterface.RadioState.RADIO_OFF);
    }
    resultSuccess(paramMessage, null);
  }
  
  public void setRadioPowerFailResponse(boolean paramBoolean)
  {
    mIsRadioPowerFailResponse = paramBoolean;
  }
  
  public void setSignalStrength(SignalStrength paramSignalStrength)
  {
    mSignalStrength = paramSignalStrength;
  }
  
  public void setSignalStrengthReportingCriteria(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, Message paramMessage) {}
  
  public void setSimCardPower(int paramInt, Message paramMessage) {}
  
  public void setSmscAddress(String paramString, Message paramMessage)
  {
    smscAddress = paramString;
    resultSuccess(paramMessage, null);
    SimulatedCommandsVerifier.getInstance().setSmscAddress(paramString, paramMessage);
  }
  
  public void setSuppServiceNotifications(boolean paramBoolean, Message paramMessage)
  {
    resultSuccess(paramMessage, null);
    if ((paramBoolean) && (mSsnNotifyOn)) {
      Rlog.w("SimulatedCommands", "Supp Service Notifications already enabled!");
    }
    mSsnNotifyOn = paramBoolean;
  }
  
  public void setTTYMode(int paramInt, Message paramMessage)
  {
    Rlog.w("SimulatedCommands", "Not implemented in SimulatedCommands");
    unimplemented(paramMessage);
  }
  
  public void setUnsolResponseFilter(int paramInt, Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().setUnsolResponseFilter(paramInt, paramMessage);
    resultSuccess(paramMessage, null);
  }
  
  public void setVoiceRadioTech(int paramInt)
  {
    mVoiceRadioTech = paramInt;
  }
  
  public void setVoiceRegState(int paramInt)
  {
    mVoiceRegState = paramInt;
  }
  
  public void setupDataCall(int paramInt1, DataProfile paramDataProfile, boolean paramBoolean1, boolean paramBoolean2, int paramInt2, LinkProperties paramLinkProperties, Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().setupDataCall(paramInt1, paramDataProfile, paramBoolean1, paramBoolean2, paramInt2, paramLinkProperties, paramMessage);
    if (mSetupDataCallResult == null) {
      try
      {
        paramDataProfile = new android/hardware/radio/V1_0/SetupDataCallResult;
        paramDataProfile.<init>();
        mSetupDataCallResult = paramDataProfile;
        mSetupDataCallResult.status = 0;
        mSetupDataCallResult.suggestedRetryTime = -1;
        mSetupDataCallResult.cid = 1;
        mSetupDataCallResult.active = 2;
        mSetupDataCallResult.type = "IP";
        mSetupDataCallResult.ifname = "rmnet_data7";
        mSetupDataCallResult.addresses = "12.34.56.78";
        mSetupDataCallResult.dnses = "98.76.54.32";
        mSetupDataCallResult.gateways = "11.22.33.44";
        mSetupDataCallResult.pcscf = "";
        mSetupDataCallResult.mtu = 1440;
      }
      catch (Exception paramDataProfile) {}
    }
    if (mDcSuccess) {
      resultSuccess(paramMessage, mSetupDataCallResult);
    } else {
      resultFail(paramMessage, mSetupDataCallResult, new RuntimeException("Setup data call failed!"));
    }
  }
  
  public void shutdown()
  {
    setRadioState(CommandsInterface.RadioState.RADIO_UNAVAILABLE);
    Looper localLooper = mHandlerThread.getLooper();
    if (localLooper != null) {
      localLooper.quit();
    }
  }
  
  public void startDtmf(char paramChar, Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().startDtmf(paramChar, paramMessage);
    resultSuccess(paramMessage, null);
  }
  
  public void startLceService(int paramInt, boolean paramBoolean, Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().startLceService(paramInt, paramBoolean, paramMessage);
  }
  
  public void startNattKeepalive(int paramInt1, KeepalivePacketData paramKeepalivePacketData, int paramInt2, Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().startNattKeepalive(paramInt1, paramKeepalivePacketData, paramInt2, paramMessage);
  }
  
  public void startNetworkScan(NetworkScanRequest paramNetworkScanRequest, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void stopDtmf(Message paramMessage)
  {
    resultSuccess(paramMessage, null);
  }
  
  public void stopLceService(Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void stopNattKeepalive(int paramInt, Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().stopNattKeepalive(paramInt, paramMessage);
  }
  
  public void stopNetworkScan(Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void supplyIccPin(String paramString, Message paramMessage)
  {
    if (mSimLockedState != SimLockState.REQUIRE_PIN)
    {
      paramString = new StringBuilder();
      paramString.append("[SimCmd] supplyIccPin: wrong state, state=");
      paramString.append(mSimLockedState);
      Rlog.i("SimulatedCommands", paramString.toString());
      resultFail(paramMessage, null, new CommandException(CommandException.Error.PASSWORD_INCORRECT));
      return;
    }
    if ((paramString != null) && (paramString.equals(mPinCode)))
    {
      Rlog.i("SimulatedCommands", "[SimCmd] supplyIccPin: success!");
      mPinUnlockAttempts = 0;
      mSimLockedState = SimLockState.NONE;
      mIccStatusChangedRegistrants.notifyRegistrants();
      resultSuccess(paramMessage, null);
      return;
    }
    if (paramMessage != null)
    {
      mPinUnlockAttempts += 1;
      paramString = new StringBuilder();
      paramString.append("[SimCmd] supplyIccPin: failed! attempt=");
      paramString.append(mPinUnlockAttempts);
      Rlog.i("SimulatedCommands", paramString.toString());
      if (mPinUnlockAttempts >= 5)
      {
        Rlog.i("SimulatedCommands", "[SimCmd] supplyIccPin: set state to REQUIRE_PUK");
        mSimLockedState = SimLockState.REQUIRE_PUK;
      }
      resultFail(paramMessage, null, new CommandException(CommandException.Error.PASSWORD_INCORRECT));
    }
  }
  
  public void supplyIccPin2(String paramString, Message paramMessage)
  {
    if (mSimFdnEnabledState != SimFdnState.REQUIRE_PIN2)
    {
      paramString = new StringBuilder();
      paramString.append("[SimCmd] supplyIccPin2: wrong state, state=");
      paramString.append(mSimFdnEnabledState);
      Rlog.i("SimulatedCommands", paramString.toString());
      resultFail(paramMessage, null, new CommandException(CommandException.Error.PASSWORD_INCORRECT));
      return;
    }
    if ((paramString != null) && (paramString.equals(mPin2Code)))
    {
      Rlog.i("SimulatedCommands", "[SimCmd] supplyIccPin2: success!");
      mPin2UnlockAttempts = 0;
      mSimFdnEnabledState = SimFdnState.NONE;
      resultSuccess(paramMessage, null);
      return;
    }
    if (paramMessage != null)
    {
      mPin2UnlockAttempts += 1;
      paramString = new StringBuilder();
      paramString.append("[SimCmd] supplyIccPin2: failed! attempt=");
      paramString.append(mPin2UnlockAttempts);
      Rlog.i("SimulatedCommands", paramString.toString());
      if (mPin2UnlockAttempts >= 5)
      {
        Rlog.i("SimulatedCommands", "[SimCmd] supplyIccPin2: set state to REQUIRE_PUK2");
        mSimFdnEnabledState = SimFdnState.REQUIRE_PUK2;
      }
      resultFail(paramMessage, null, new CommandException(CommandException.Error.PASSWORD_INCORRECT));
    }
  }
  
  public void supplyIccPin2ForApp(String paramString1, String paramString2, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void supplyIccPinForApp(String paramString1, String paramString2, Message paramMessage)
  {
    SimulatedCommandsVerifier.getInstance().supplyIccPinForApp(paramString1, paramString2, paramMessage);
    if ((mPinCode != null) && (mPinCode.equals(paramString1)))
    {
      resultSuccess(paramMessage, null);
      return;
    }
    Rlog.i("SimulatedCommands", "[SimCmd] supplyIccPinForApp: pin failed!");
    paramString1 = new CommandException(CommandException.Error.PASSWORD_INCORRECT);
    int i = mPin1attemptsRemaining - 1;
    mPin1attemptsRemaining = i;
    if (i < 0) {
      i = 0;
    } else {
      i = mPin1attemptsRemaining;
    }
    resultFail(paramMessage, new int[] { i }, paramString1);
  }
  
  public void supplyIccPuk(String paramString1, String paramString2, Message paramMessage)
  {
    if (mSimLockedState != SimLockState.REQUIRE_PUK)
    {
      paramString1 = new StringBuilder();
      paramString1.append("[SimCmd] supplyIccPuk: wrong state, state=");
      paramString1.append(mSimLockedState);
      Rlog.i("SimulatedCommands", paramString1.toString());
      resultFail(paramMessage, null, new CommandException(CommandException.Error.PASSWORD_INCORRECT));
      return;
    }
    if ((paramString1 != null) && (paramString1.equals("12345678")))
    {
      Rlog.i("SimulatedCommands", "[SimCmd] supplyIccPuk: success!");
      mSimLockedState = SimLockState.NONE;
      mPukUnlockAttempts = 0;
      mIccStatusChangedRegistrants.notifyRegistrants();
      resultSuccess(paramMessage, null);
      return;
    }
    if (paramMessage != null)
    {
      mPukUnlockAttempts += 1;
      paramString1 = new StringBuilder();
      paramString1.append("[SimCmd] supplyIccPuk: failed! attempt=");
      paramString1.append(mPukUnlockAttempts);
      Rlog.i("SimulatedCommands", paramString1.toString());
      if (mPukUnlockAttempts >= 10)
      {
        Rlog.i("SimulatedCommands", "[SimCmd] supplyIccPuk: set state to SIM_PERM_LOCKED");
        mSimLockedState = SimLockState.SIM_PERM_LOCKED;
      }
      resultFail(paramMessage, null, new CommandException(CommandException.Error.PASSWORD_INCORRECT));
    }
  }
  
  public void supplyIccPuk2(String paramString1, String paramString2, Message paramMessage)
  {
    if (mSimFdnEnabledState != SimFdnState.REQUIRE_PUK2)
    {
      paramString1 = new StringBuilder();
      paramString1.append("[SimCmd] supplyIccPuk2: wrong state, state=");
      paramString1.append(mSimLockedState);
      Rlog.i("SimulatedCommands", paramString1.toString());
      resultFail(paramMessage, null, new CommandException(CommandException.Error.PASSWORD_INCORRECT));
      return;
    }
    if ((paramString1 != null) && (paramString1.equals("87654321")))
    {
      Rlog.i("SimulatedCommands", "[SimCmd] supplyIccPuk2: success!");
      mSimFdnEnabledState = SimFdnState.NONE;
      mPuk2UnlockAttempts = 0;
      resultSuccess(paramMessage, null);
      return;
    }
    if (paramMessage != null)
    {
      mPuk2UnlockAttempts += 1;
      paramString1 = new StringBuilder();
      paramString1.append("[SimCmd] supplyIccPuk2: failed! attempt=");
      paramString1.append(mPuk2UnlockAttempts);
      Rlog.i("SimulatedCommands", paramString1.toString());
      if (mPuk2UnlockAttempts >= 10)
      {
        Rlog.i("SimulatedCommands", "[SimCmd] supplyIccPuk2: set state to SIM_PERM_LOCKED");
        mSimFdnEnabledState = SimFdnState.SIM_PERM_LOCKED;
      }
      resultFail(paramMessage, null, new CommandException(CommandException.Error.PASSWORD_INCORRECT));
    }
  }
  
  public void supplyIccPuk2ForApp(String paramString1, String paramString2, String paramString3, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void supplyIccPukForApp(String paramString1, String paramString2, String paramString3, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void supplyNetworkDepersonalization(String paramString, Message paramMessage)
  {
    unimplemented(paramMessage);
  }
  
  public void switchWaitingOrHoldingAndActive(Message paramMessage)
  {
    if (!simulatedCallState.onChld('2', '\000')) {
      resultFail(paramMessage, null, new RuntimeException("Hangup Error"));
    } else {
      resultSuccess(paramMessage, null);
    }
  }
  
  public void triggerHangupAll()
  {
    simulatedCallState.triggerHangupAll();
    mCallStateRegistrants.notifyRegistrants();
  }
  
  public void triggerHangupBackground()
  {
    simulatedCallState.triggerHangupBackground();
    mCallStateRegistrants.notifyRegistrants();
  }
  
  public void triggerHangupForeground()
  {
    simulatedCallState.triggerHangupForeground();
    mCallStateRegistrants.notifyRegistrants();
  }
  
  public void triggerIncomingSMS(String paramString) {}
  
  public void triggerIncomingStkCcAlpha(String paramString)
  {
    if (mCatCcAlphaRegistrant != null) {
      mCatCcAlphaRegistrant.notifyResult(paramString);
    }
  }
  
  public void triggerIncomingUssd(String paramString1, String paramString2)
  {
    if (mUSSDRegistrant != null) {
      mUSSDRegistrant.notifyResult(new String[] { paramString1, paramString2 });
    }
  }
  
  public void triggerNITZupdate(String paramString)
  {
    if (paramString != null) {
      mNITZTimeRegistrant.notifyRegistrant(new AsyncResult(null, new Object[] { paramString, Long.valueOf(SystemClock.elapsedRealtime()) }, null));
    }
  }
  
  @VisibleForTesting
  public void triggerRestrictedStateChanged(int paramInt)
  {
    if (mRestrictedStateRegistrant != null) {
      mRestrictedStateRegistrant.notifyRegistrant(new AsyncResult(null, Integer.valueOf(paramInt), null));
    }
  }
  
  public void triggerRing(String paramString)
  {
    simulatedCallState.triggerRing(paramString);
    mCallStateRegistrants.notifyRegistrants();
  }
  
  public void triggerSsn(int paramInt1, int paramInt2)
  {
    SuppServiceNotification localSuppServiceNotification = new SuppServiceNotification();
    notificationType = paramInt1;
    code = paramInt2;
    mSsnRegistrant.notifyRegistrant(new AsyncResult(null, localSuppServiceNotification, null));
  }
  
  public void unregisterForIccRefresh(Handler paramHandler)
  {
    super.unregisterForIccRefresh(paramHandler);
    SimulatedCommandsVerifier.getInstance().unregisterForIccRefresh(paramHandler);
  }
  
  public void unregisterForLceInfo(Handler paramHandler)
  {
    SimulatedCommandsVerifier.getInstance().unregisterForLceInfo(paramHandler);
  }
  
  public void unregisterForNattKeepaliveStatus(Handler paramHandler)
  {
    SimulatedCommandsVerifier.getInstance().unregisterForNattKeepaliveStatus(paramHandler);
  }
  
  public void unregisterForPcoData(Handler paramHandler) {}
  
  public void writeSmsToRuim(int paramInt, String paramString, Message paramMessage)
  {
    paramString = new StringBuilder();
    paramString.append("Write SMS to RUIM with status ");
    paramString.append(paramInt);
    Rlog.d("SimulatedCommands", paramString.toString());
    unimplemented(paramMessage);
  }
  
  public void writeSmsToSim(int paramInt, String paramString1, String paramString2, Message paramMessage)
  {
    paramString1 = new StringBuilder();
    paramString1.append("Write SMS to SIM with status ");
    paramString1.append(paramInt);
    Rlog.d("SimulatedCommands", paramString1.toString());
    unimplemented(paramMessage);
  }
  
  private static enum SimFdnState
  {
    NONE,  REQUIRE_PIN2,  REQUIRE_PUK2,  SIM_PERM_LOCKED;
    
    private SimFdnState() {}
  }
  
  private static enum SimLockState
  {
    NONE,  REQUIRE_PIN,  REQUIRE_PUK,  SIM_PERM_LOCKED;
    
    private SimLockState() {}
  }
}
