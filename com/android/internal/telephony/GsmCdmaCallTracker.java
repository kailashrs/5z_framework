package com.android.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.Registrant;
import android.os.RegistrantList;
import android.os.SystemProperties;
import android.telephony.CarrierConfigManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.EventLog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.cdma.CdmaCallWaitingNotification;
import com.android.internal.telephony.dataconnection.DcTracker;
import com.android.internal.telephony.metrics.TelephonyMetrics;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GsmCdmaCallTracker
  extends CallTracker
{
  private static final boolean DBG_POLL = false;
  private static final String LOG_TAG = "GsmCdmaCallTracker";
  private static final int MAX_CONNECTIONS_CDMA = 8;
  public static final int MAX_CONNECTIONS_GSM = 19;
  private static final int MAX_CONNECTIONS_PER_CALL_CDMA = 1;
  private static final int MAX_CONNECTIONS_PER_CALL_GSM = 5;
  private static final boolean REPEAT_POLLING = false;
  private static final boolean VDBG = false;
  private int m3WayCallFlashDelay;
  public GsmCdmaCall mBackgroundCall = new GsmCdmaCall(this);
  private RegistrantList mCallWaitingRegistrants = new RegistrantList();
  @VisibleForTesting
  public GsmCdmaConnection[] mConnections;
  private boolean mDesiredMute = false;
  private ArrayList<GsmCdmaConnection> mDroppedDuringPoll = new ArrayList(19);
  private BroadcastReceiver mEcmExitReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      if (paramAnonymousIntent.getAction().equals("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED"))
      {
        boolean bool = paramAnonymousIntent.getBooleanExtra("phoneinECMState", false);
        paramAnonymousIntent = GsmCdmaCallTracker.this;
        paramAnonymousContext = new StringBuilder();
        paramAnonymousContext.append("Received ACTION_EMERGENCY_CALLBACK_MODE_CHANGED isInEcm = ");
        paramAnonymousContext.append(bool);
        paramAnonymousIntent.log(paramAnonymousContext.toString());
        if (!bool)
        {
          paramAnonymousContext = new ArrayList();
          paramAnonymousContext.addAll(mRingingCall.getConnections());
          paramAnonymousContext.addAll(mForegroundCall.getConnections());
          paramAnonymousContext.addAll(mBackgroundCall.getConnections());
          if (mPendingMO != null) {
            paramAnonymousContext.add(mPendingMO);
          }
          paramAnonymousContext = paramAnonymousContext.iterator();
          while (paramAnonymousContext.hasNext())
          {
            paramAnonymousIntent = (Connection)paramAnonymousContext.next();
            if (paramAnonymousIntent != null) {
              paramAnonymousIntent.onExitedEcmMode();
            }
          }
        }
      }
    }
  };
  public GsmCdmaCall mForegroundCall = new GsmCdmaCall(this);
  private boolean mHangupPendingMO;
  private boolean mIsEcmTimerCanceled;
  private boolean mIsInEmergencyCall;
  private TelephonyMetrics mMetrics = TelephonyMetrics.getInstance();
  private int mPendingCallClirMode;
  private boolean mPendingCallInEcm;
  private GsmCdmaConnection mPendingMO;
  private GsmCdmaPhone mPhone;
  public GsmCdmaCall mRingingCall = new GsmCdmaCall(this);
  public PhoneConstants.State mState = PhoneConstants.State.IDLE;
  private RegistrantList mVoiceCallEndedRegistrants = new RegistrantList();
  private RegistrantList mVoiceCallStartedRegistrants = new RegistrantList();
  
  public GsmCdmaCallTracker(GsmCdmaPhone paramGsmCdmaPhone)
  {
    mPhone = paramGsmCdmaPhone;
    mCi = mCi;
    mCi.registerForCallStateChanged(this, 2, null);
    mCi.registerForOn(this, 9, null);
    mCi.registerForNotAvailable(this, 10, null);
    paramGsmCdmaPhone = new IntentFilter();
    paramGsmCdmaPhone.addAction("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED");
    mPhone.getContext().registerReceiver(mEcmExitReceiver, paramGsmCdmaPhone);
    updatePhoneType(true);
  }
  
  private boolean canDial()
  {
    String str = SystemProperties.get("ro.telephony.disable-call", "false");
    boolean bool1 = mCi.getRadioState().isOn();
    boolean bool2 = false;
    if ((bool1) && (mPendingMO == null) && (!str.equals("true")) && ((!mForegroundCall.getState().isAlive()) || (!mBackgroundCall.getState().isAlive()) || ((!isPhoneTypeGsm()) && (mForegroundCall.getState() == Call.State.ACTIVE)))) {
      bool1 = true;
    } else {
      bool1 = false;
    }
    boolean bool3 = bool1;
    if (SystemProperties.getInt("persist.asus.gcf.mode", 0) != 1)
    {
      if ((bool1) && (!mRingingCall.isRinging())) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      bool3 = bool1;
    }
    if (!bool3)
    {
      boolean bool4 = mCi.getRadioState().isOn();
      if (mPendingMO == null) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      boolean bool5 = mRingingCall.isRinging();
      boolean bool6 = str.equals("true");
      boolean bool7 = mForegroundCall.getState().isAlive();
      if (mForegroundCall.getState() == Call.State.ACTIVE) {
        bool2 = true;
      }
      log(String.format("canDial is false\n(radio isOn ::=%s\n&& pendingMO == null::=%s\n&& !ringingCall.isRinging()::=%s\n&& !disableCall.equals(\"true\")::=%s\n&& (!foregroundCall.getState().isAlive()::=%s\n   || foregroundCall.getState() == GsmCdmaCall.State.ACTIVE::=%s\n   ||!backgroundCall.getState().isAlive())::=%s)", new Object[] { Boolean.valueOf(bool4), Boolean.valueOf(bool1), Boolean.valueOf(bool5 ^ true), Boolean.valueOf(bool6 ^ true), Boolean.valueOf(bool7 ^ true), Boolean.valueOf(bool2), Boolean.valueOf(true ^ mBackgroundCall.getState().isAlive()) }));
    }
    return bool3;
  }
  
  private void checkAndEnableDataCallAfterEmergencyCallDropped()
  {
    if (mIsInEmergencyCall)
    {
      mIsInEmergencyCall = false;
      boolean bool = mPhone.isInEcm();
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("checkAndEnableDataCallAfterEmergencyCallDropped,inEcm=");
      localStringBuilder.append(bool);
      log(localStringBuilder.toString());
      if (!bool)
      {
        mPhone.mDcTracker.setInternalDataEnabled(true);
        mPhone.notifyEmergencyCallRegistrants(false);
      }
      mPhone.sendEmergencyCallStateChange(false);
    }
  }
  
  private Connection checkMtFindNewRinging(DriverCall paramDriverCall, int paramInt)
  {
    StringBuilder localStringBuilder = null;
    Object localObject;
    if (mConnections[paramInt].getCall() == mRingingCall)
    {
      localObject = mConnections[paramInt];
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Notify new ring ");
      localStringBuilder.append(paramDriverCall);
      log(localStringBuilder.toString());
    }
    else
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Phantom call appeared ");
      ((StringBuilder)localObject).append(paramDriverCall);
      Rlog.e("GsmCdmaCallTracker", ((StringBuilder)localObject).toString());
      localObject = localStringBuilder;
      if (state != DriverCall.State.ALERTING)
      {
        localObject = localStringBuilder;
        if (state != DriverCall.State.DIALING)
        {
          mConnections[paramInt].onConnectedInOrOut();
          localObject = localStringBuilder;
          if (state == DriverCall.State.HOLDING)
          {
            mConnections[paramInt].onStartedHolding();
            localObject = localStringBuilder;
          }
        }
      }
    }
    return localObject;
  }
  
  private Connection dial(String paramString, int paramInt)
    throws CallStateException
  {
    clearDisconnected();
    if (canDial())
    {
      Object localObject = (TelephonyManager)mPhone.getContext().getSystemService("phone");
      String str = ((TelephonyManager)localObject).getNetworkCountryIsoForPhone(mPhone.getPhoneId());
      localObject = ((TelephonyManager)localObject).getSimCountryIsoForPhone(mPhone.getPhoneId());
      int i;
      if ((!TextUtils.isEmpty(str)) && (!TextUtils.isEmpty((CharSequence)localObject)) && (!((String)localObject).equals(str))) {
        i = 1;
      } else {
        i = 0;
      }
      int j = i;
      if (i != 0) {
        if ("us".equals(localObject))
        {
          if ((i != 0) && (!"vi".equals(str))) {
            j = 1;
          } else {
            j = 0;
          }
        }
        else
        {
          j = i;
          if ("vi".equals(localObject)) {
            if ((i != 0) && (!"us".equals(str))) {
              j = 1;
            } else {
              j = 0;
            }
          }
        }
      }
      if (j != 0) {
        str = convertNumberIfNecessary(mPhone, paramString);
      } else {
        str = paramString;
      }
      boolean bool1 = mPhone.isInEcm();
      boolean bool2 = PhoneNumberUtils.isLocalEmergencyNumber(mPhone.getContext(), str);
      if ((bool1) && (bool2)) {
        handleEcmTimer(1);
      }
      if (mForegroundCall.getState() == Call.State.ACTIVE) {
        return dialThreeWay(str);
      }
      mPendingMO = new GsmCdmaConnection(mPhone, checkForTestEmergencyNumber(str), this, mForegroundCall, bool2);
      mHangupPendingMO = false;
      if ((mPendingMO.getAddress() != null) && (mPendingMO.getAddress().length() != 0) && (mPendingMO.getAddress().indexOf('N') < 0))
      {
        setMute(false);
        disableDataCallInEmergencyCall(str);
        if ((bool1) && ((!bool1) || (!bool2)))
        {
          mPhone.exitEmergencyCallbackMode();
          mPhone.setOnEcbModeExitResponse(this, 14, null);
          mPendingCallClirMode = paramInt;
          mPendingCallInEcm = true;
        }
        else
        {
          mCi.dial(mPendingMO.getAddress(), paramInt, obtainCompleteMessage());
        }
      }
      else
      {
        mPendingMO.mCause = 7;
        pollCallsWhenSafe();
      }
      if (mNumberConverted)
      {
        mPendingMO.setConverted(paramString);
        mNumberConverted = false;
      }
      updatePhoneState();
      mPhone.notifyPreciseCallStateChanged();
      return mPendingMO;
    }
    throw new CallStateException("cannot dial in current state");
  }
  
  private Connection dial(String paramString, int paramInt, Bundle paramBundle)
    throws CallStateException
  {
    return dial(paramString, paramInt, null, paramBundle);
  }
  
  private Connection dialThreeWay(String paramString)
  {
    if (!mForegroundCall.isIdle())
    {
      disableDataCallInEmergencyCall(paramString);
      mPendingMO = new GsmCdmaConnection(mPhone, checkForTestEmergencyNumber(paramString), this, mForegroundCall, mIsInEmergencyCall);
      paramString = ((CarrierConfigManager)mPhone.getContext().getSystemService("carrier_config")).getConfig();
      if (paramString != null) {
        m3WayCallFlashDelay = paramString.getInt("cdma_3waycall_flash_delay_int");
      } else {
        m3WayCallFlashDelay = 0;
      }
      if (m3WayCallFlashDelay > 0) {
        mCi.sendCDMAFeatureCode("", obtainMessage(20));
      } else {
        mCi.sendCDMAFeatureCode(mPendingMO.getAddress(), obtainMessage(16));
      }
      return mPendingMO;
    }
    return null;
  }
  
  private void disableDataCallInEmergencyCall(String paramString)
  {
    if (PhoneNumberUtils.isLocalEmergencyNumber(mPhone.getContext(), paramString))
    {
      log("disableDataCallInEmergencyCall");
      setIsInEmergencyCall();
    }
  }
  
  private void dumpState()
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Phone State:");
    ((StringBuilder)localObject).append(mState);
    Rlog.i("GsmCdmaCallTracker", ((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Ringing call: ");
    ((StringBuilder)localObject).append(mRingingCall.toString());
    Rlog.i("GsmCdmaCallTracker", ((StringBuilder)localObject).toString());
    localObject = mRingingCall.getConnections();
    int i = 0;
    int j = ((List)localObject).size();
    while (i < j)
    {
      Rlog.i("GsmCdmaCallTracker", ((List)localObject).get(i).toString());
      i++;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Foreground call: ");
    ((StringBuilder)localObject).append(mForegroundCall.toString());
    Rlog.i("GsmCdmaCallTracker", ((StringBuilder)localObject).toString());
    localObject = mForegroundCall.getConnections();
    i = 0;
    j = ((List)localObject).size();
    while (i < j)
    {
      Rlog.i("GsmCdmaCallTracker", ((List)localObject).get(i).toString());
      i++;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Background call: ");
    ((StringBuilder)localObject).append(mBackgroundCall.toString());
    Rlog.i("GsmCdmaCallTracker", ((StringBuilder)localObject).toString());
    localObject = mBackgroundCall.getConnections();
    i = 0;
    j = ((List)localObject).size();
    while (i < j)
    {
      Rlog.i("GsmCdmaCallTracker", ((List)localObject).get(i).toString());
      i++;
    }
  }
  
  private void fakeHoldForegroundBeforeDial()
  {
    List localList = (List)mForegroundCall.mConnections.clone();
    int i = 0;
    int j = localList.size();
    while (i < j)
    {
      ((GsmCdmaConnection)localList.get(i)).fakeHoldBeforeDial();
      i++;
    }
  }
  
  private void flashAndSetGenericTrue()
  {
    mCi.sendCDMAFeatureCode("", obtainMessage(8));
    mPhone.notifyPreciseCallStateChanged();
  }
  
  private PhoneInternalInterface.SuppService getFailedService(int paramInt)
  {
    if (paramInt != 8)
    {
      switch (paramInt)
      {
      default: 
        return PhoneInternalInterface.SuppService.UNKNOWN;
      case 13: 
        return PhoneInternalInterface.SuppService.TRANSFER;
      case 12: 
        return PhoneInternalInterface.SuppService.SEPARATE;
      }
      return PhoneInternalInterface.SuppService.CONFERENCE;
    }
    return PhoneInternalInterface.SuppService.SWITCH;
  }
  
  private void handleCallWaitingInfo(CdmaCallWaitingNotification paramCdmaCallWaitingNotification)
  {
    new GsmCdmaConnection(mPhone.getContext(), paramCdmaCallWaitingNotification, this, mRingingCall);
    updatePhoneState();
    notifyCallWaitingInfo(paramCdmaCallWaitingNotification);
  }
  
  private void handleEcmTimer(int paramInt)
  {
    mPhone.handleTimerInEmergencyCallbackMode(paramInt);
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("handleEcmTimer, unsupported action ");
      localStringBuilder.append(paramInt);
      Rlog.e("GsmCdmaCallTracker", localStringBuilder.toString());
      break;
    case 1: 
      mIsEcmTimerCanceled = true;
      break;
    case 0: 
      mIsEcmTimerCanceled = false;
    }
  }
  
  private void handleRadioNotAvailable()
  {
    pollCallsWhenSafe();
  }
  
  private void internalClearDisconnected()
  {
    mRingingCall.clearDisconnected();
    mForegroundCall.clearDisconnected();
    mBackgroundCall.clearDisconnected();
  }
  
  private boolean isPhoneTypeGsm()
  {
    int i = mPhone.getPhoneType();
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    return bool;
  }
  
  private void logHangupEvent(GsmCdmaCall paramGsmCdmaCall)
  {
    int i = mConnections.size();
    for (int j = 0; j < i; j++)
    {
      GsmCdmaConnection localGsmCdmaConnection = (GsmCdmaConnection)mConnections.get(j);
      int k;
      try
      {
        k = localGsmCdmaConnection.getGsmCdmaIndex();
      }
      catch (CallStateException localCallStateException)
      {
        k = -1;
      }
      mMetrics.writeRilHangup(mPhone.getPhoneId(), localGsmCdmaConnection, k);
    }
  }
  
  private void notifyCallWaitingInfo(CdmaCallWaitingNotification paramCdmaCallWaitingNotification)
  {
    if (mCallWaitingRegistrants != null) {
      mCallWaitingRegistrants.notifyRegistrants(new AsyncResult(null, paramCdmaCallWaitingNotification, null));
    }
  }
  
  private Message obtainCompleteMessage()
  {
    return obtainCompleteMessage(4);
  }
  
  private Message obtainCompleteMessage(int paramInt)
  {
    mPendingOperations += 1;
    mLastRelevantPoll = null;
    mNeedsPoll = true;
    return obtainMessage(paramInt);
  }
  
  private void operationComplete()
  {
    mPendingOperations -= 1;
    if ((mPendingOperations == 0) && (mNeedsPoll))
    {
      mLastRelevantPoll = obtainMessage(1);
      mCi.getCurrentCalls(mLastRelevantPoll);
    }
    else if (mPendingOperations < 0)
    {
      Rlog.e("GsmCdmaCallTracker", "GsmCdmaCallTracker.pendingOperations < 0");
      mPendingOperations = 0;
    }
  }
  
  private void reset()
  {
    Rlog.d("GsmCdmaCallTracker", "reset");
    for (GsmCdmaConnection localGsmCdmaConnection : mConnections) {
      if (localGsmCdmaConnection != null)
      {
        localGsmCdmaConnection.onDisconnect(36);
        localGsmCdmaConnection.dispose();
      }
    }
    if (mPendingMO != null)
    {
      mPendingMO.onDisconnect(36);
      mPendingMO.dispose();
    }
    mConnections = null;
    mPendingMO = null;
    clearDisconnected();
  }
  
  private void updateMetrics(GsmCdmaConnection[] paramArrayOfGsmCdmaConnection)
  {
    ArrayList localArrayList = new ArrayList();
    int i = paramArrayOfGsmCdmaConnection.length;
    for (int j = 0; j < i; j++)
    {
      GsmCdmaConnection localGsmCdmaConnection = paramArrayOfGsmCdmaConnection[j];
      if (localGsmCdmaConnection != null) {
        localArrayList.add(localGsmCdmaConnection);
      }
    }
    mMetrics.writeRilCallList(mPhone.getPhoneId(), localArrayList);
  }
  
  private void updatePhoneState()
  {
    PhoneConstants.State localState = mState;
    if (mRingingCall.isRinging())
    {
      mState = PhoneConstants.State.RINGING;
    }
    else if ((mPendingMO == null) && (mForegroundCall.isIdle()) && (mBackgroundCall.isIdle()))
    {
      localObject = mPhone.getImsPhone();
      if ((mState == PhoneConstants.State.OFFHOOK) && (localObject != null)) {
        ((Phone)localObject).callEndCleanupHandOverCallIfAny();
      }
      mState = PhoneConstants.State.IDLE;
    }
    else
    {
      mState = PhoneConstants.State.OFFHOOK;
    }
    if ((mState == PhoneConstants.State.IDLE) && (localState != mState)) {
      mVoiceCallEndedRegistrants.notifyRegistrants(new AsyncResult(null, null, null));
    } else if ((localState == PhoneConstants.State.IDLE) && (localState != mState)) {
      mVoiceCallStartedRegistrants.notifyRegistrants(new AsyncResult(null, null, null));
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("update phone state, old=");
    ((StringBuilder)localObject).append(localState);
    ((StringBuilder)localObject).append(" new=");
    ((StringBuilder)localObject).append(mState);
    log(((StringBuilder)localObject).toString());
    if (mState != localState)
    {
      mPhone.notifyPhoneStateChanged();
      mMetrics.writePhoneState(mPhone.getPhoneId(), mState);
      try
      {
        mPhone.notifySignalStrength();
        log("notifySignalStrength for System UI for VoWiFi icon");
      }
      catch (Exception localException)
      {
        log("notifySignalStrength error");
      }
    }
  }
  
  private void updatePhoneType(boolean paramBoolean)
  {
    if (!paramBoolean)
    {
      reset();
      pollCallsWhenSafe();
    }
    if (mPhone.isPhoneTypeGsm())
    {
      mConnections = new GsmCdmaConnection[19];
      mCi.unregisterForCallWaitingInfo(this);
      if (mIsInEmergencyCall) {
        mPhone.mDcTracker.setInternalDataEnabled(true);
      }
    }
    else
    {
      mConnections = new GsmCdmaConnection[8];
      mPendingCallInEcm = false;
      mIsInEmergencyCall = false;
      mPendingCallClirMode = 0;
      mIsEcmTimerCanceled = false;
      m3WayCallFlashDelay = 0;
      mCi.registerForCallWaitingInfo(this, 15, null);
    }
  }
  
  public void acceptCall()
    throws CallStateException
  {
    if (mRingingCall.getState() == Call.State.INCOMING)
    {
      Rlog.i("phone", "acceptCall: incoming...");
      setMute(false);
      mCi.acceptCall(obtainCompleteMessage());
    }
    else
    {
      if (mRingingCall.getState() != Call.State.WAITING) {
        break label108;
      }
      if (isPhoneTypeGsm())
      {
        setMute(false);
      }
      else
      {
        GsmCdmaConnection localGsmCdmaConnection = (GsmCdmaConnection)mRingingCall.getLatestConnection();
        localGsmCdmaConnection.updateParent(mRingingCall, mForegroundCall);
        localGsmCdmaConnection.onConnectedInOrOut();
        updatePhoneState();
      }
      switchWaitingOrHoldingAndActive();
    }
    return;
    label108:
    throw new CallStateException("phone not ringing");
  }
  
  public boolean canConference()
  {
    boolean bool;
    if ((mForegroundCall.getState() == Call.State.ACTIVE) && (mBackgroundCall.getState() == Call.State.HOLDING) && (!mBackgroundCall.isFull()) && (!mForegroundCall.isFull())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean canTransfer()
  {
    boolean bool1 = isPhoneTypeGsm();
    boolean bool2 = false;
    if (bool1)
    {
      if ((mForegroundCall.getState() != Call.State.ACTIVE) && (mForegroundCall.getState() != Call.State.ALERTING))
      {
        bool1 = bool2;
        if (mForegroundCall.getState() != Call.State.DIALING) {}
      }
      else
      {
        bool1 = bool2;
        if (mBackgroundCall.getState() == Call.State.HOLDING) {
          bool1 = true;
        }
      }
      return bool1;
    }
    Rlog.e("GsmCdmaCallTracker", "canTransfer: not possible in CDMA");
    return false;
  }
  
  public void cleanupCalls()
  {
    pollCallsWhenSafe();
  }
  
  public void clearDisconnected()
  {
    internalClearDisconnected();
    updatePhoneState();
    mPhone.notifyPreciseCallStateChanged();
  }
  
  public void conference()
  {
    if (isPhoneTypeGsm()) {
      mCi.conference(obtainCompleteMessage(11));
    } else {
      flashAndSetGenericTrue();
    }
  }
  
  public Connection dial(String paramString)
    throws CallStateException
  {
    if (isPhoneTypeGsm()) {
      return dial(paramString, 0, null);
    }
    return dial(paramString, 0);
  }
  
  public Connection dial(String paramString, int paramInt, UUSInfo paramUUSInfo, Bundle paramBundle)
    throws CallStateException
  {
    try
    {
      clearDisconnected();
      if (canDial())
      {
        paramBundle = convertNumberIfNecessary(mPhone, paramString);
        if (mForegroundCall.getState() == Call.State.ACTIVE)
        {
          switchWaitingOrHoldingAndActive();
          try
          {
            Thread.sleep(500L);
          }
          catch (InterruptedException localInterruptedException) {}
          fakeHoldForegroundBeforeDial();
        }
        if (mForegroundCall.getState() == Call.State.IDLE)
        {
          boolean bool = PhoneNumberUtils.isLocalEmergencyNumber(mPhone.getContext(), paramBundle);
          GsmCdmaConnection localGsmCdmaConnection = new com/android/internal/telephony/GsmCdmaConnection;
          localGsmCdmaConnection.<init>(mPhone, checkForTestEmergencyNumber(paramBundle), this, mForegroundCall, bool);
          mPendingMO = localGsmCdmaConnection;
          mHangupPendingMO = false;
          mMetrics.writeRilDial(mPhone.getPhoneId(), mPendingMO, paramInt, paramUUSInfo);
          if ((mPendingMO.getAddress() != null) && (mPendingMO.getAddress().length() != 0) && (mPendingMO.getAddress().indexOf('N') < 0))
          {
            setMute(false);
            mCi.dial(mPendingMO.getAddress(), paramInt, paramUUSInfo, obtainCompleteMessage());
          }
          else
          {
            mPendingMO.mCause = 7;
            pollCallsWhenSafe();
          }
          if (mNumberConverted)
          {
            mPendingMO.setConverted(paramString);
            mNumberConverted = false;
          }
          updatePhoneState();
          mPhone.notifyPreciseCallStateChanged();
          paramString = mPendingMO;
          return paramString;
        }
        paramString = new com/android/internal/telephony/CallStateException;
        paramString.<init>("cannot dial in current state");
        throw paramString;
      }
      paramString = new com/android/internal/telephony/CallStateException;
      paramString.<init>("cannot dial in current state");
      throw paramString;
    }
    finally {}
  }
  
  public Connection dial(String paramString, UUSInfo paramUUSInfo, Bundle paramBundle)
    throws CallStateException
  {
    return dial(paramString, 0, paramUUSInfo, paramBundle);
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter.println("GsmCdmaCallTracker extends:");
    super.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("mConnections: length=");
    paramFileDescriptor.append(mConnections.length);
    paramPrintWriter.println(paramFileDescriptor.toString());
    for (int i = 0; i < mConnections.length; i++) {
      paramPrintWriter.printf("  mConnections[%d]=%s\n", new Object[] { Integer.valueOf(i), mConnections[i] });
    }
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mVoiceCallEndedRegistrants=");
    paramFileDescriptor.append(mVoiceCallEndedRegistrants);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mVoiceCallStartedRegistrants=");
    paramFileDescriptor.append(mVoiceCallStartedRegistrants);
    paramPrintWriter.println(paramFileDescriptor.toString());
    if (!isPhoneTypeGsm())
    {
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append(" mCallWaitingRegistrants=");
      paramFileDescriptor.append(mCallWaitingRegistrants);
      paramPrintWriter.println(paramFileDescriptor.toString());
    }
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mDroppedDuringPoll: size=");
    paramFileDescriptor.append(mDroppedDuringPoll.size());
    paramPrintWriter.println(paramFileDescriptor.toString());
    for (i = 0; i < mDroppedDuringPoll.size(); i++) {
      paramPrintWriter.printf("  mDroppedDuringPoll[%d]=%s\n", new Object[] { Integer.valueOf(i), mDroppedDuringPoll.get(i) });
    }
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mRingingCall=");
    paramFileDescriptor.append(mRingingCall);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mForegroundCall=");
    paramFileDescriptor.append(mForegroundCall);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mBackgroundCall=");
    paramFileDescriptor.append(mBackgroundCall);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mPendingMO=");
    paramFileDescriptor.append(mPendingMO);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mHangupPendingMO=");
    paramFileDescriptor.append(mHangupPendingMO);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mPhone=");
    paramFileDescriptor.append(mPhone);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mDesiredMute=");
    paramFileDescriptor.append(mDesiredMute);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mState=");
    paramFileDescriptor.append(mState);
    paramPrintWriter.println(paramFileDescriptor.toString());
    if (!isPhoneTypeGsm())
    {
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append(" mPendingCallInEcm=");
      paramFileDescriptor.append(mPendingCallInEcm);
      paramPrintWriter.println(paramFileDescriptor.toString());
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append(" mIsInEmergencyCall=");
      paramFileDescriptor.append(mIsInEmergencyCall);
      paramPrintWriter.println(paramFileDescriptor.toString());
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append(" mPendingCallClirMode=");
      paramFileDescriptor.append(mPendingCallClirMode);
      paramPrintWriter.println(paramFileDescriptor.toString());
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append(" mIsEcmTimerCanceled=");
      paramFileDescriptor.append(mIsEcmTimerCanceled);
      paramPrintWriter.println(paramFileDescriptor.toString());
    }
  }
  
  public void explicitCallTransfer()
  {
    mCi.explicitCallTransfer(obtainCompleteMessage(13));
  }
  
  protected void finalize()
  {
    Rlog.d("GsmCdmaCallTracker", "GsmCdmaCallTracker finalized");
  }
  
  public GsmCdmaConnection getConnectionByIndex(GsmCdmaCall paramGsmCdmaCall, int paramInt)
    throws CallStateException
  {
    int i = mConnections.size();
    for (int j = 0; j < i; j++)
    {
      GsmCdmaConnection localGsmCdmaConnection = (GsmCdmaConnection)mConnections.get(j);
      if ((!mDisconnected) && (localGsmCdmaConnection.getGsmCdmaIndex() == paramInt)) {
        return localGsmCdmaConnection;
      }
    }
    return null;
  }
  
  public int getMaxConnectionsPerCall()
  {
    int i;
    if (mPhone.isPhoneTypeGsm()) {
      i = 5;
    } else {
      i = 1;
    }
    return i;
  }
  
  public boolean getMute()
  {
    return mDesiredMute;
  }
  
  public GsmCdmaPhone getPhone()
  {
    return mPhone;
  }
  
  public PhoneConstants.State getState()
  {
    return mState;
  }
  
  public void handleMessage(Message paramMessage)
  {
    int i = what;
    if (i != 20)
    {
      switch (i)
      {
      default: 
        switch (i)
        {
        default: 
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("unexpected event ");
          ((StringBuilder)localObject).append(what);
          ((StringBuilder)localObject).append(" not handled by phone type ");
          ((StringBuilder)localObject).append(mPhone.getPhoneType());
          throw new RuntimeException(((StringBuilder)localObject).toString());
        case 16: 
          if (!isPhoneTypeGsm())
          {
            if (obj).exception != null) {
              break label1023;
            }
            mPendingMO.onConnectedInOrOut();
            mPendingMO = null;
            break label1023;
          }
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("unexpected event ");
          ((StringBuilder)localObject).append(what);
          ((StringBuilder)localObject).append(" not handled by phone type ");
          ((StringBuilder)localObject).append(mPhone.getPhoneType());
          throw new RuntimeException(((StringBuilder)localObject).toString());
        case 15: 
          if (!isPhoneTypeGsm())
          {
            paramMessage = (AsyncResult)obj;
            if (exception != null) {
              break label1023;
            }
            handleCallWaitingInfo((CdmaCallWaitingNotification)result);
            Rlog.d("GsmCdmaCallTracker", "Event EVENT_CALL_WAITING_INFO_CDMA Received");
            break label1023;
          }
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("unexpected event ");
          ((StringBuilder)localObject).append(what);
          ((StringBuilder)localObject).append(" not handled by phone type ");
          ((StringBuilder)localObject).append(mPhone.getPhoneType());
          throw new RuntimeException(((StringBuilder)localObject).toString());
        case 14: 
          if (!isPhoneTypeGsm())
          {
            if (mPendingCallInEcm)
            {
              mCi.dial(mPendingMO.getAddress(), mPendingCallClirMode, obtainCompleteMessage());
              mPendingCallInEcm = false;
            }
            mPhone.unsetOnEcbModeExitResponse(this);
            break label1023;
          }
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("unexpected event ");
          ((StringBuilder)localObject).append(what);
          ((StringBuilder)localObject).append(" not handled by phone type ");
          ((StringBuilder)localObject).append(mPhone.getPhoneType());
          throw new RuntimeException(((StringBuilder)localObject).toString());
        case 11: 
          if ((isPhoneTypeGsm()) && (obj).exception != null))
          {
            localObject = mForegroundCall.getLatestConnection();
            if (localObject != null) {
              ((Connection)localObject).onConferenceMergeFailed();
            }
          }
          break;
        case 10: 
          handleRadioNotAvailable();
          break;
        case 9: 
          handleRadioAvailable();
          break;
        }
        if (isPhoneTypeGsm())
        {
          if (obj).exception != null) {
            mPhone.notifySuppServiceFailed(getFailedService(what));
          }
          operationComplete();
          break;
        }
        if (what == 8) {
          break;
        }
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("unexpected event ");
        ((StringBuilder)localObject).append(what);
        ((StringBuilder)localObject).append(" not handled by phone type ");
        ((StringBuilder)localObject).append(mPhone.getPhoneType());
        throw new RuntimeException(((StringBuilder)localObject).toString());
      case 5: 
        localObject = null;
        paramMessage = (AsyncResult)obj;
        operationComplete();
        int j;
        if (exception != null)
        {
          j = 16;
          Rlog.i("GsmCdmaCallTracker", "Exception during getLastCallFailCause, assuming normal disconnect");
          paramMessage = (Message)localObject;
        }
        else
        {
          paramMessage = (LastCallFailCause)result;
          j = causeCode;
          paramMessage = vendorCause;
        }
        if ((j == 34) || (j == 41) || (j == 42) || (j == 44) || (j == 49) || (j == 58) || (j == 65535))
        {
          localObject = mPhone.getCellLocation();
          i = -1;
          if (localObject != null) {
            if (isPhoneTypeGsm()) {
              i = ((GsmCellLocation)localObject).getCid();
            } else {
              i = ((CdmaCellLocation)localObject).getBaseStationId();
            }
          }
          EventLog.writeEvent(50106, new Object[] { Integer.valueOf(j), Integer.valueOf(i), Integer.valueOf(TelephonyManager.getDefault().getNetworkType()) });
        }
        i = 0;
        int k = mDroppedDuringPoll.size();
        while (i < k)
        {
          ((GsmCdmaConnection)mDroppedDuringPoll.get(i)).onRemoteDisconnect(j, paramMessage);
          i++;
        }
        updatePhoneState();
        mPhone.notifyPreciseCallStateChanged();
        mMetrics.writeRilCallList(mPhone.getPhoneId(), mDroppedDuringPoll);
        mDroppedDuringPoll.clear();
        break;
      case 4: 
        operationComplete();
        break;
      case 2: 
      case 3: 
        if (what == 2) {
          log("EVENT_CALL_STATE_CHANGE");
        }
        pollCallsWhenSafe();
        break;
      case 1: 
        Rlog.d("GsmCdmaCallTracker", "Event EVENT_POLL_CALLS_RESULT Received");
        if (paramMessage != mLastRelevantPoll) {
          break;
        }
        mNeedsPoll = false;
        mLastRelevantPoll = null;
        log("Ehandle EVENT_POLL_CALL_RESULT: set needsPoll=F");
        handlePollCalls((AsyncResult)obj);
        break;
      }
    }
    else
    {
      if (isPhoneTypeGsm()) {
        break label1024;
      }
      if (obj).exception == null)
      {
        postDelayed(new Runnable()
        {
          public void run()
          {
            if (mPendingMO != null) {
              mCi.sendCDMAFeatureCode(mPendingMO.getAddress(), obtainMessage(16));
            }
          }
        }, m3WayCallFlashDelay);
      }
      else
      {
        mPendingMO = null;
        Rlog.w("GsmCdmaCallTracker", "exception happened on Blank Flash for 3-way call");
      }
    }
    label1023:
    return;
    label1024:
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("unexpected event ");
    ((StringBuilder)localObject).append(what);
    ((StringBuilder)localObject).append(" not handled by phone type ");
    ((StringBuilder)localObject).append(mPhone.getPhoneType());
    throw new RuntimeException(((StringBuilder)localObject).toString());
  }
  
  protected void handlePollCalls(AsyncResult paramAsyncResult)
  {
    try
    {
      if (exception == null) {}
      for (paramAsyncResult = (List)result;; paramAsyncResult = new ArrayList())
      {
        break;
        if (!isCommandExceptionRadioNotAvailable(exception)) {
          break label2319;
        }
      }
      AsyncResult localAsyncResult = paramAsyncResult;
      ArrayList localArrayList = new java/util/ArrayList;
      localArrayList.<init>();
      int i = 0;
      int j = mHandoverConnections.size();
      int k = 0;
      int m = 0;
      int n = localAsyncResult.size();
      paramAsyncResult = null;
      int i1 = 1;
      int i2 = 0;
      int i3 = 0;
      Object localObject1 = null;
      Object localObject5;
      int i4;
      int i8;
      while (k < mConnections.length)
      {
        Object localObject2 = mConnections[k];
        if (m < n)
        {
          localObject3 = (DriverCall)localAsyncResult.get(m);
          if (index == k + 1) {
            m++;
          } else {
            localObject3 = null;
          }
        }
        else
        {
          localObject3 = null;
        }
        if ((localObject2 == null) && (localObject3 == null)) {}
        for (;;)
        {
          break;
          i1 = 0;
        }
        if ((localObject2 == null) && (localObject3 != null)) {
          if ((mPendingMO != null) && (mPendingMO.compareTo((DriverCall)localObject3)))
          {
            mConnections[k] = mPendingMO;
            mPendingMO.mIndex = k;
            mPendingMO.update((DriverCall)localObject3);
            mPendingMO = null;
            if (mHangupPendingMO)
            {
              mHangupPendingMO = false;
              if ((!isPhoneTypeGsm()) && (mIsEcmTimerCanceled)) {
                handleEcmTimer(0);
              }
              try
              {
                paramAsyncResult = new java/lang/StringBuilder;
                paramAsyncResult.<init>();
                paramAsyncResult.append("poll: hangupPendingMO, hangup conn ");
                paramAsyncResult.append(k);
                log(paramAsyncResult.toString());
                hangup(mConnections[k]);
              }
              catch (CallStateException paramAsyncResult)
              {
                Rlog.e("GsmCdmaCallTracker", "unexpected error on hangup");
              }
              return;
            }
            localObject3 = paramAsyncResult;
            paramAsyncResult = (AsyncResult)localObject1;
          }
          else
          {
            localObject4 = new java/lang/StringBuilder;
            ((StringBuilder)localObject4).<init>();
            ((StringBuilder)localObject4).append("pendingMo=");
            ((StringBuilder)localObject4).append(mPendingMO);
            ((StringBuilder)localObject4).append(", dc=");
            ((StringBuilder)localObject4).append(localObject3);
            log(((StringBuilder)localObject4).toString());
            localObject5 = mConnections;
            localObject4 = new com/android/internal/telephony/GsmCdmaConnection;
            ((GsmCdmaConnection)localObject4).<init>(mPhone, (DriverCall)localObject3, this, k);
            localObject5[k] = localObject4;
            localObject4 = getHoConnection((DriverCall)localObject3);
            if (localObject4 != null)
            {
              mConnections[k].migrateFrom((Connection)localObject4);
              if ((mPreHandoverState != Call.State.ACTIVE) && (mPreHandoverState != Call.State.HOLDING) && (state == DriverCall.State.ACTIVE)) {
                mConnections[k].onConnectedInOrOut();
              }
              mHandoverConnections.remove(localObject4);
              i4 = i;
              i5 = i2;
              i3 = m;
              if (isPhoneTypeGsm())
              {
                localObject3 = mHandoverConnections.iterator();
                for (;;)
                {
                  i4 = i;
                  i5 = i2;
                  i3 = m;
                  if (!((Iterator)localObject3).hasNext()) {
                    break;
                  }
                  localObject5 = (Connection)((Iterator)localObject3).next();
                  localObject2 = new java/lang/StringBuilder;
                  ((StringBuilder)localObject2).<init>();
                  ((StringBuilder)localObject2).append("HO Conn state is ");
                  ((StringBuilder)localObject2).append(mPreHandoverState);
                  Rlog.i("GsmCdmaCallTracker", ((StringBuilder)localObject2).toString());
                  if (mPreHandoverState == mConnections[k].getState())
                  {
                    localObject2 = new java/lang/StringBuilder;
                    ((StringBuilder)localObject2).<init>();
                    ((StringBuilder)localObject2).append("Removing HO conn ");
                    ((StringBuilder)localObject2).append(localObject4);
                    ((StringBuilder)localObject2).append(mPreHandoverState);
                    Rlog.i("GsmCdmaCallTracker", ((StringBuilder)localObject2).toString());
                    ((Iterator)localObject3).remove();
                  }
                }
              }
              i = i4;
              i2 = i5;
              m = i3;
              mPhone.notifyHandoverStateChanged(mConnections[k]);
              localObject3 = paramAsyncResult;
              paramAsyncResult = (AsyncResult)localObject1;
            }
            else
            {
              i3 = i;
              i5 = m;
              localObject4 = checkMtFindNewRinging((DriverCall)localObject3, k);
              localObject3 = localObject4;
              paramAsyncResult = (AsyncResult)localObject1;
              i = i3;
              m = i5;
              if (localObject4 == null)
              {
                if (isPhoneTypeGsm())
                {
                  localArrayList.add(mConnections[k]);
                  paramAsyncResult = (AsyncResult)localObject1;
                }
                else
                {
                  paramAsyncResult = mConnections[k];
                }
                i2 = 1;
                m = i5;
                i = i3;
                localObject3 = localObject4;
              }
            }
          }
        }
        for (;;)
        {
          i3 = 1;
          localObject4 = localObject3;
          localObject1 = paramAsyncResult;
          break label1431;
          if ((localObject2 != null) && (localObject3 == null))
          {
            if (isPhoneTypeGsm())
            {
              mDroppedDuringPoll.add(localObject2);
            }
            else
            {
              i4 = mForegroundCall.mConnections.size();
              for (i5 = 0; i5 < i4; i5++)
              {
                localObject3 = new java/lang/StringBuilder;
                ((StringBuilder)localObject3).<init>();
                ((StringBuilder)localObject3).append("adding fgCall cn ");
                ((StringBuilder)localObject3).append(i5);
                ((StringBuilder)localObject3).append(" to droppedDuringPoll");
                log(((StringBuilder)localObject3).toString());
                localObject3 = (GsmCdmaConnection)mForegroundCall.mConnections.get(i5);
                mDroppedDuringPoll.add(localObject3);
              }
              i4 = mRingingCall.mConnections.size();
              for (i5 = 0; i5 < i4; i5++)
              {
                localObject3 = new java/lang/StringBuilder;
                ((StringBuilder)localObject3).<init>();
                ((StringBuilder)localObject3).append("adding rgCall cn ");
                ((StringBuilder)localObject3).append(i5);
                ((StringBuilder)localObject3).append(" to droppedDuringPoll");
                log(((StringBuilder)localObject3).toString());
                localObject3 = (GsmCdmaConnection)mRingingCall.mConnections.get(i5);
                mDroppedDuringPoll.add(localObject3);
              }
              if (mIsEcmTimerCanceled) {
                handleEcmTimer(0);
              }
              checkAndEnableDataCallAfterEmergencyCallDropped();
            }
            mConnections[k] = null;
            localObject4 = paramAsyncResult;
            i4 = i3;
            localObject5 = localObject1;
            i6 = i;
            i7 = i2;
            i5 = m;
            break label1451;
          }
          if ((localObject2 == null) || (localObject3 == null) || (((GsmCdmaConnection)localObject2).compareTo((DriverCall)localObject3)) || (!isPhoneTypeGsm())) {
            break;
          }
          mDroppedDuringPoll.add(localObject2);
          localObject4 = mConnections;
          localObject5 = new com/android/internal/telephony/GsmCdmaConnection;
          ((GsmCdmaConnection)localObject5).<init>(mPhone, (DriverCall)localObject3, this, k);
          localObject4[k] = localObject5;
          if (mConnections[k].getCall() == mRingingCall) {
            paramAsyncResult = mConnections[k];
          }
          localObject3 = paramAsyncResult;
          paramAsyncResult = (AsyncResult)localObject1;
        }
        localObject4 = paramAsyncResult;
        i4 = i3;
        localObject5 = localObject1;
        int i6 = i;
        int i7 = i2;
        i5 = m;
        if (localObject2 != null)
        {
          localObject4 = paramAsyncResult;
          i4 = i3;
          localObject5 = localObject1;
          i6 = i;
          i7 = i2;
          i5 = m;
          if (localObject3 != null) {
            if ((!isPhoneTypeGsm()) && (((GsmCdmaConnection)localObject2).isIncoming() != isMT))
            {
              if (isMT == true)
              {
                mDroppedDuringPoll.add(localObject2);
                paramAsyncResult = checkMtFindNewRinging((DriverCall)localObject3, k);
                if (paramAsyncResult == null)
                {
                  localObject1 = localObject2;
                  i2 = 1;
                }
                checkAndEnableDataCallAfterEmergencyCallDropped();
                i7 = i2;
                break label1470;
              }
              localObject4 = new java/lang/StringBuilder;
              ((StringBuilder)localObject4).<init>();
              ((StringBuilder)localObject4).append("Error in RIL, Phantom call appeared ");
              ((StringBuilder)localObject4).append(localObject3);
              Rlog.e("GsmCdmaCallTracker", ((StringBuilder)localObject4).toString());
              localObject4 = paramAsyncResult;
              i4 = i3;
              localObject5 = localObject1;
              i6 = i;
              i7 = i2;
              i5 = m;
            }
            else
            {
              i8 = ((GsmCdmaConnection)localObject2).update((DriverCall)localObject3);
              if ((i3 == 0) && (i8 == 0)) {
                i3 = 0;
              } else {
                i3 = 1;
              }
              localObject4 = paramAsyncResult;
              label1431:
              i5 = m;
              i7 = i2;
              i6 = i;
              localObject5 = localObject1;
              i4 = i3;
            }
          }
        }
        label1451:
        localObject1 = localObject5;
        m = i5;
        i = i6;
        i3 = i4;
        paramAsyncResult = (AsyncResult)localObject4;
        label1470:
        i2 = i7;
        k++;
      }
      if ((!isPhoneTypeGsm()) && (i1 != 0)) {
        checkAndEnableDataCallAfterEmergencyCallDropped();
      }
      if (mPendingMO != null)
      {
        localObject3 = new java/lang/StringBuilder;
        ((StringBuilder)localObject3).<init>();
        ((StringBuilder)localObject3).append("Pending MO dropped before poll fg state:");
        ((StringBuilder)localObject3).append(mForegroundCall.getState());
        Rlog.d("GsmCdmaCallTracker", ((StringBuilder)localObject3).toString());
        mDroppedDuringPoll.add(mPendingMO);
        mPendingMO = null;
        mHangupPendingMO = false;
        if (!isPhoneTypeGsm())
        {
          if (mPendingCallInEcm) {
            mPendingCallInEcm = false;
          }
          checkAndEnableDataCallAfterEmergencyCallDropped();
        }
      }
      if (paramAsyncResult != null)
      {
        log("handlePollCalls(): newRinging not null => notifyNewRingingConnection");
        mPhone.notifyNewRingingConnection(paramAsyncResult);
      }
      Object localObject4 = new java/util/ArrayList;
      ((ArrayList)localObject4).<init>();
      i1 = mDroppedDuringPoll.size() - 1;
      m = i2;
      int i5 = i;
      while (i1 >= 0)
      {
        localObject5 = (GsmCdmaConnection)mDroppedDuringPoll.get(i1);
        i = 0;
        if ((((GsmCdmaConnection)localObject5).isIncoming()) && (((GsmCdmaConnection)localObject5).getConnectTime() == 0L))
        {
          if (mCause == 3) {
            i2 = 16;
          } else {
            i2 = 1;
          }
          localObject3 = new java/lang/StringBuilder;
          ((StringBuilder)localObject3).<init>();
          ((StringBuilder)localObject3).append("missed/rejected call, conn.cause=");
          ((StringBuilder)localObject3).append(mCause);
          log(((StringBuilder)localObject3).toString());
          localObject3 = new java/lang/StringBuilder;
          ((StringBuilder)localObject3).<init>();
          ((StringBuilder)localObject3).append("setting cause to ");
          ((StringBuilder)localObject3).append(i2);
          log(((StringBuilder)localObject3).toString());
          mDroppedDuringPoll.remove(i1);
          i8 = ((GsmCdmaConnection)localObject5).onDisconnect(i2);
          i = 1;
          ((ArrayList)localObject4).add(localObject5);
          i2 = i5 | i8;
        }
        else if (mCause != 3)
        {
          i2 = i5;
          if (mCause != 7) {}
        }
        else
        {
          mDroppedDuringPoll.remove(i1);
          int i9 = ((GsmCdmaConnection)localObject5).onDisconnect(mCause);
          i = 1;
          ((ArrayList)localObject4).add(localObject5);
          i2 = i5 | i9;
        }
        localObject3 = localObject1;
        i4 = m;
        if (!isPhoneTypeGsm())
        {
          localObject3 = localObject1;
          i4 = m;
          if (i != 0)
          {
            localObject3 = localObject1;
            i4 = m;
            if (m != 0)
            {
              localObject3 = localObject1;
              i4 = m;
              if (localObject5 == localObject1)
              {
                localObject3 = null;
                i4 = 0;
              }
            }
          }
        }
        i1--;
        localObject1 = localObject3;
        i5 = i2;
        m = i4;
      }
      if (((ArrayList)localObject4).size() > 0) {
        mMetrics.writeRilCallList(mPhone.getPhoneId(), (ArrayList)localObject4);
      }
      Object localObject3 = mHandoverConnections.iterator();
      while (((Iterator)localObject3).hasNext())
      {
        localObject4 = (Connection)((Iterator)localObject3).next();
        localObject5 = new java/lang/StringBuilder;
        ((StringBuilder)localObject5).<init>();
        ((StringBuilder)localObject5).append("handlePollCalls - disconnect hoConn= ");
        ((StringBuilder)localObject5).append(localObject4);
        ((StringBuilder)localObject5).append(" hoConn.State= ");
        ((StringBuilder)localObject5).append(((Connection)localObject4).getState());
        log(((StringBuilder)localObject5).toString());
        if (((Connection)localObject4).getState().isRinging()) {
          ((Connection)localObject4).onDisconnect(1);
        } else {
          ((Connection)localObject4).onDisconnect(-1);
        }
        ((Iterator)localObject3).remove();
      }
      if (mDroppedDuringPoll.size() > 0) {
        mCi.getLastCallFailCause(obtainNoPollCompleteMessage(5));
      }
      if (0 != 0) {
        pollCallsAfterDelay();
      }
      if ((paramAsyncResult != null) || (i3 != 0) || (i5 != 0)) {
        internalClearDisconnected();
      }
      updatePhoneState();
      if (m != 0)
      {
        if (isPhoneTypeGsm())
        {
          localObject1 = localArrayList.iterator();
          while (((Iterator)localObject1).hasNext())
          {
            localObject4 = (Connection)((Iterator)localObject1).next();
            localObject3 = new java/lang/StringBuilder;
            ((StringBuilder)localObject3).<init>();
            ((StringBuilder)localObject3).append("Notify unknown for ");
            ((StringBuilder)localObject3).append(localObject4);
            log(((StringBuilder)localObject3).toString());
            mPhone.notifyUnknownConnection((Connection)localObject4);
          }
        }
        mPhone.notifyUnknownConnection((Connection)localObject1);
      }
      if ((i3 != 0) || (paramAsyncResult != null) || (i5 != 0))
      {
        mPhone.notifyPreciseCallStateChanged();
        updateMetrics(mConnections);
      }
      if ((j > 0) && (mHandoverConnections.size() == 0))
      {
        paramAsyncResult = mPhone.getImsPhone();
        if (paramAsyncResult != null) {
          paramAsyncResult.callEndCleanupHandOverCallIfAny();
        }
      }
      return;
      label2319:
      pollCallsAfterDelay();
      return;
    }
    finally {}
  }
  
  public void hangup(GsmCdmaCall paramGsmCdmaCall)
    throws CallStateException
  {
    if (paramGsmCdmaCall.getConnections().size() != 0)
    {
      if (paramGsmCdmaCall == mRingingCall)
      {
        log("(ringing) hangup waiting or background");
        logHangupEvent(paramGsmCdmaCall);
        mCi.hangupWaitingOrBackground(obtainCompleteMessage());
      }
      else if (paramGsmCdmaCall == mForegroundCall)
      {
        if (paramGsmCdmaCall.isDialingOrAlerting())
        {
          log("(foregnd) hangup dialing or alerting...");
          hangup((GsmCdmaConnection)paramGsmCdmaCall.getConnections().get(0));
        }
        else if ((isPhoneTypeGsm()) && (mRingingCall.isRinging()))
        {
          log("hangup all conns in active/background call, without affecting ringing call");
          hangupAllConnections(paramGsmCdmaCall);
        }
        else
        {
          logHangupEvent(paramGsmCdmaCall);
          hangupForegroundResumeBackground();
        }
      }
      else
      {
        if (paramGsmCdmaCall != mBackgroundCall) {
          break label183;
        }
        if (mRingingCall.isRinging())
        {
          log("hangup all conns in background call");
          hangupAllConnections(paramGsmCdmaCall);
        }
        else
        {
          hangupWaitingOrBackground();
        }
      }
      paramGsmCdmaCall.onHangupLocal();
      mPhone.notifyPreciseCallStateChanged();
      return;
      label183:
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("GsmCdmaCall ");
      localStringBuilder.append(paramGsmCdmaCall);
      localStringBuilder.append("does not belong to GsmCdmaCallTracker ");
      localStringBuilder.append(this);
      throw new RuntimeException(localStringBuilder.toString());
    }
    throw new CallStateException("no connections in call");
  }
  
  public void hangup(GsmCdmaConnection paramGsmCdmaConnection)
    throws CallStateException
  {
    if (mOwner == this)
    {
      if (paramGsmCdmaConnection == mPendingMO)
      {
        if (mIsEcmTimerCanceled) {
          handleEcmTimer(0);
        }
        log("hangup conn with callId '-1' as there is no DIAL response yet ");
        mCi.hangupConnection(-1, obtainCompleteMessage());
      }
      else
      {
        if ((!isPhoneTypeGsm()) && (paramGsmCdmaConnection.getCall() == mRingingCall) && (mRingingCall.getState() == Call.State.WAITING))
        {
          paramGsmCdmaConnection.onLocalDisconnect();
          updatePhoneState();
          mPhone.notifyPreciseCallStateChanged();
          return;
        }
        try
        {
          mMetrics.writeRilHangup(mPhone.getPhoneId(), paramGsmCdmaConnection, paramGsmCdmaConnection.getGsmCdmaIndex());
          mCi.hangupConnection(paramGsmCdmaConnection.getGsmCdmaIndex(), obtainCompleteMessage());
        }
        catch (CallStateException localCallStateException)
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("GsmCdmaCallTracker WARN: hangup() on absent connection ");
          localStringBuilder.append(paramGsmCdmaConnection);
          Rlog.w("GsmCdmaCallTracker", localStringBuilder.toString());
        }
      }
      paramGsmCdmaConnection.onHangupLocal();
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("GsmCdmaConnection ");
    localStringBuilder.append(paramGsmCdmaConnection);
    localStringBuilder.append("does not belong to GsmCdmaCallTracker ");
    localStringBuilder.append(this);
    throw new CallStateException(localStringBuilder.toString());
  }
  
  public void hangupAllConnections(GsmCdmaCall paramGsmCdmaCall)
  {
    try
    {
      int i = mConnections.size();
      for (int j = 0; j < i; j++)
      {
        localObject = (GsmCdmaConnection)mConnections.get(j);
        if (!mDisconnected)
        {
          mMetrics.writeRilHangup(mPhone.getPhoneId(), (GsmCdmaConnection)localObject, ((GsmCdmaConnection)localObject).getGsmCdmaIndex());
          mCi.hangupConnection(((GsmCdmaConnection)localObject).getGsmCdmaIndex(), obtainCompleteMessage());
        }
      }
    }
    catch (CallStateException paramGsmCdmaCall)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("hangupConnectionByIndex caught ");
      ((StringBuilder)localObject).append(paramGsmCdmaCall);
      Rlog.e("GsmCdmaCallTracker", ((StringBuilder)localObject).toString());
    }
  }
  
  public void hangupConnectionByIndex(GsmCdmaCall paramGsmCdmaCall, int paramInt)
    throws CallStateException
  {
    int i = mConnections.size();
    for (int j = 0; j < i; j++)
    {
      GsmCdmaConnection localGsmCdmaConnection = (GsmCdmaConnection)mConnections.get(j);
      if ((!mDisconnected) && (localGsmCdmaConnection.getGsmCdmaIndex() == paramInt))
      {
        mMetrics.writeRilHangup(mPhone.getPhoneId(), localGsmCdmaConnection, localGsmCdmaConnection.getGsmCdmaIndex());
        mCi.hangupConnection(paramInt, obtainCompleteMessage());
        return;
      }
    }
    throw new CallStateException("no GsmCdma index found");
  }
  
  public void hangupForegroundResumeBackground()
  {
    log("hangupForegroundResumeBackground");
    mCi.hangupForegroundResumeBackground(obtainCompleteMessage());
  }
  
  public void hangupWaitingOrBackground()
  {
    log("hangupWaitingOrBackground");
    logHangupEvent(mBackgroundCall);
    mCi.hangupWaitingOrBackground(obtainCompleteMessage());
  }
  
  public boolean isInEmergencyCall()
  {
    return mIsInEmergencyCall;
  }
  
  protected void log(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(mPhone.getPhoneId());
    localStringBuilder.append("] ");
    localStringBuilder.append(paramString);
    Rlog.d("GsmCdmaCallTracker", localStringBuilder.toString());
  }
  
  public void registerForCallWaiting(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mCallWaitingRegistrants.add(paramHandler);
  }
  
  public void registerForVoiceCallEnded(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mVoiceCallEndedRegistrants.add(paramHandler);
  }
  
  public void registerForVoiceCallStarted(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mVoiceCallStartedRegistrants.add(paramHandler);
    if (mState != PhoneConstants.State.IDLE) {
      paramHandler.notifyRegistrant(new AsyncResult(null, null, null));
    }
  }
  
  public void rejectCall()
    throws CallStateException
  {
    if (mRingingCall.getState().isRinging())
    {
      mCi.rejectCall(obtainCompleteMessage());
      return;
    }
    throw new CallStateException("phone not ringing");
  }
  
  public void separate(GsmCdmaConnection paramGsmCdmaConnection)
    throws CallStateException
  {
    if (mOwner == this)
    {
      try
      {
        mCi.separateConnection(paramGsmCdmaConnection.getGsmCdmaIndex(), obtainCompleteMessage(12));
      }
      catch (CallStateException localCallStateException)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("GsmCdmaCallTracker WARN: separate() on absent connection ");
        localStringBuilder.append(paramGsmCdmaConnection);
        Rlog.w("GsmCdmaCallTracker", localStringBuilder.toString());
      }
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("GsmCdmaConnection ");
    localStringBuilder.append(paramGsmCdmaConnection);
    localStringBuilder.append("does not belong to GsmCdmaCallTracker ");
    localStringBuilder.append(this);
    throw new CallStateException(localStringBuilder.toString());
  }
  
  public void setIsInEmergencyCall()
  {
    mIsInEmergencyCall = true;
    mPhone.mDcTracker.setInternalDataEnabled(false);
    mPhone.notifyEmergencyCallRegistrants(true);
    mPhone.sendEmergencyCallStateChange(true);
  }
  
  public void setMute(boolean paramBoolean)
  {
    mDesiredMute = paramBoolean;
    mCi.setMute(mDesiredMute, null);
  }
  
  public void switchWaitingOrHoldingAndActive()
    throws CallStateException
  {
    if (mRingingCall.getState() != Call.State.INCOMING)
    {
      if (isPhoneTypeGsm()) {
        mCi.switchWaitingOrHoldingAndActive(obtainCompleteMessage(8));
      } else if (mForegroundCall.getConnections().size() > 1) {
        flashAndSetGenericTrue();
      } else {
        mCi.sendCDMAFeatureCode("", obtainMessage(8));
      }
      return;
    }
    throw new CallStateException("cannot be in the incoming state");
  }
  
  public void unregisterForCallWaiting(Handler paramHandler)
  {
    mCallWaitingRegistrants.remove(paramHandler);
  }
  
  public void unregisterForVoiceCallEnded(Handler paramHandler)
  {
    mVoiceCallEndedRegistrants.remove(paramHandler);
  }
  
  public void unregisterForVoiceCallStarted(Handler paramHandler)
  {
    mVoiceCallStartedRegistrants.remove(paramHandler);
  }
  
  public void updatePhoneType()
  {
    updatePhoneType(false);
  }
}
