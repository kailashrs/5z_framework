package com.android.internal.telephony;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.Registrant;
import android.os.RegistrantList;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.sip.SipPhone;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CallManager
{
  private static final boolean DBG = true;
  private static final int EVENT_CALL_WAITING = 108;
  private static final int EVENT_CDMA_OTA_STATUS_CHANGE = 111;
  private static final int EVENT_DISCONNECT = 100;
  private static final int EVENT_DISPLAY_INFO = 109;
  private static final int EVENT_ECM_TIMER_RESET = 115;
  private static final int EVENT_INCOMING_RING = 104;
  private static final int EVENT_IN_CALL_VOICE_PRIVACY_OFF = 107;
  private static final int EVENT_IN_CALL_VOICE_PRIVACY_ON = 106;
  private static final int EVENT_MMI_COMPLETE = 114;
  private static final int EVENT_MMI_INITIATE = 113;
  private static final int EVENT_NEW_RINGING_CONNECTION = 102;
  private static final int EVENT_ONHOLD_TONE = 120;
  private static final int EVENT_POST_DIAL_CHARACTER = 119;
  private static final int EVENT_PRECISE_CALL_STATE_CHANGED = 101;
  private static final int EVENT_RESEND_INCALL_MUTE = 112;
  private static final int EVENT_RINGBACK_TONE = 105;
  private static final int EVENT_SERVICE_STATE_CHANGED = 118;
  private static final int EVENT_SIGNAL_INFO = 110;
  private static final int EVENT_SUBSCRIPTION_INFO_READY = 116;
  private static final int EVENT_SUPP_SERVICE_FAILED = 117;
  private static final int EVENT_TTY_MODE_RECEIVED = 122;
  private static final int EVENT_UNKNOWN_CONNECTION = 103;
  private static final CallManager INSTANCE = new CallManager();
  private static final String LOG_TAG = "CallManager";
  private static final boolean VDBG = false;
  private final ArrayList<Call> mBackgroundCalls = new ArrayList();
  protected final RegistrantList mCallWaitingRegistrants = new RegistrantList();
  protected final RegistrantList mCdmaOtaStatusChangeRegistrants = new RegistrantList();
  private Phone mDefaultPhone = null;
  protected final RegistrantList mDisconnectRegistrants = new RegistrantList();
  protected final RegistrantList mDisplayInfoRegistrants = new RegistrantList();
  protected final RegistrantList mEcmTimerResetRegistrants = new RegistrantList();
  private final ArrayList<Connection> mEmptyConnections = new ArrayList();
  private final ArrayList<Call> mForegroundCalls = new ArrayList();
  private final HashMap<Phone, CallManagerHandler> mHandlerMap = new HashMap();
  protected final RegistrantList mInCallVoicePrivacyOffRegistrants = new RegistrantList();
  protected final RegistrantList mInCallVoicePrivacyOnRegistrants = new RegistrantList();
  protected final RegistrantList mIncomingRingRegistrants = new RegistrantList();
  protected final RegistrantList mMmiCompleteRegistrants = new RegistrantList();
  protected final RegistrantList mMmiInitiateRegistrants = new RegistrantList();
  protected final RegistrantList mMmiRegistrants = new RegistrantList();
  protected final RegistrantList mNewRingingConnectionRegistrants = new RegistrantList();
  protected final RegistrantList mOnHoldToneRegistrants = new RegistrantList();
  private final ArrayList<Phone> mPhones = new ArrayList();
  protected final RegistrantList mPostDialCharacterRegistrants = new RegistrantList();
  protected final RegistrantList mPreciseCallStateRegistrants = new RegistrantList();
  private Object mRegistrantidentifier = new Object();
  protected final RegistrantList mResendIncallMuteRegistrants = new RegistrantList();
  protected final RegistrantList mRingbackToneRegistrants = new RegistrantList();
  private final ArrayList<Call> mRingingCalls = new ArrayList();
  protected final RegistrantList mServiceStateChangedRegistrants = new RegistrantList();
  protected final RegistrantList mSignalInfoRegistrants = new RegistrantList();
  private boolean mSpeedUpAudioForMtCall = false;
  protected final RegistrantList mSubscriptionInfoReadyRegistrants = new RegistrantList();
  protected final RegistrantList mSuppServiceFailedRegistrants = new RegistrantList();
  protected final RegistrantList mTtyModeReceivedRegistrants = new RegistrantList();
  protected final RegistrantList mUnknownConnectionRegistrants = new RegistrantList();
  
  private CallManager() {}
  
  private boolean canDial(Phone paramPhone)
  {
    int i = paramPhone.getServiceState().getState();
    int j = paramPhone.getSubId();
    boolean bool1 = hasActiveRingingCall();
    Call.State localState = getActiveFgCallState(j);
    boolean bool2;
    if ((i != 3) && (!bool1) && ((localState == Call.State.ACTIVE) || (localState == Call.State.IDLE) || (localState == Call.State.DISCONNECTED) || (localState == Call.State.ALERTING))) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    if (!bool2)
    {
      paramPhone = new StringBuilder();
      paramPhone.append("canDial serviceState=");
      paramPhone.append(i);
      paramPhone.append(" hasRingingCall=");
      paramPhone.append(bool1);
      paramPhone.append(" fgCallState=");
      paramPhone.append(localState);
      Rlog.d("CallManager", paramPhone.toString());
    }
    return bool2;
  }
  
  private Context getContext()
  {
    Object localObject = getDefaultPhone();
    if (localObject == null) {
      localObject = null;
    } else {
      localObject = ((Phone)localObject).getContext();
    }
    return localObject;
  }
  
  private Call getFirstActiveCall(ArrayList<Call> paramArrayList)
  {
    Iterator localIterator = paramArrayList.iterator();
    while (localIterator.hasNext())
    {
      paramArrayList = (Call)localIterator.next();
      if (!paramArrayList.isIdle()) {
        return paramArrayList;
      }
    }
    return null;
  }
  
  private Call getFirstActiveCall(ArrayList<Call> paramArrayList, int paramInt)
  {
    paramArrayList = paramArrayList.iterator();
    while (paramArrayList.hasNext())
    {
      Call localCall = (Call)paramArrayList.next();
      if ((!localCall.isIdle()) && ((localCall.getPhone().getSubId() == paramInt) || ((localCall.getPhone() instanceof SipPhone)))) {
        return localCall;
      }
    }
    return null;
  }
  
  private Call getFirstCallOfState(ArrayList<Call> paramArrayList, Call.State paramState)
  {
    paramArrayList = paramArrayList.iterator();
    while (paramArrayList.hasNext())
    {
      Call localCall = (Call)paramArrayList.next();
      if (localCall.getState() == paramState) {
        return localCall;
      }
    }
    return null;
  }
  
  private Call getFirstCallOfState(ArrayList<Call> paramArrayList, Call.State paramState, int paramInt)
  {
    Iterator localIterator = paramArrayList.iterator();
    while (localIterator.hasNext())
    {
      paramArrayList = (Call)localIterator.next();
      if ((paramArrayList.getState() != paramState) && (paramArrayList.getPhone().getSubId() != paramInt) && (!(paramArrayList.getPhone() instanceof SipPhone))) {}
      return paramArrayList;
    }
    return null;
  }
  
  private Call getFirstNonIdleCall(List<Call> paramList)
  {
    List<Call> localList = null;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Call localCall = (Call)localIterator.next();
      if (!localCall.isIdle()) {
        return localCall;
      }
      paramList = localList;
      if (localCall.getState() != Call.State.IDLE)
      {
        paramList = localList;
        if (localList == null) {
          paramList = localCall;
        }
      }
      localList = paramList;
    }
    return localList;
  }
  
  private Call getFirstNonIdleCall(List<Call> paramList, int paramInt)
  {
    Object localObject = null;
    Iterator localIterator = paramList.iterator();
    for (paramList = (List<Call>)localObject; localIterator.hasNext(); paramList = (List<Call>)localObject)
    {
      Call localCall = (Call)localIterator.next();
      if (localCall.getPhone().getSubId() != paramInt)
      {
        localObject = paramList;
        if (!(localCall.getPhone() instanceof SipPhone)) {}
      }
      else
      {
        if (!localCall.isIdle()) {
          return localCall;
        }
        localObject = paramList;
        if (localCall.getState() != Call.State.IDLE)
        {
          localObject = paramList;
          if (paramList == null) {
            localObject = localCall;
          }
        }
      }
    }
    return paramList;
  }
  
  public static CallManager getInstance()
  {
    return INSTANCE;
  }
  
  private Phone getPhone(int paramInt)
  {
    Object localObject1 = null;
    Iterator localIterator = mPhones.iterator();
    Object localObject2;
    for (;;)
    {
      localObject2 = localObject1;
      if (!localIterator.hasNext()) {
        break;
      }
      localObject2 = (Phone)localIterator.next();
      if ((((Phone)localObject2).getSubId() == paramInt) && (((Phone)localObject2).getPhoneType() != 5)) {
        break;
      }
    }
    return localObject2;
  }
  
  private boolean hasMoreThanOneHoldingCall(int paramInt)
  {
    int i = 0;
    Iterator localIterator = mBackgroundCalls.iterator();
    while (localIterator.hasNext())
    {
      Call localCall = (Call)localIterator.next();
      int j = i;
      if (localCall.getState() == Call.State.HOLDING) {
        if (localCall.getPhone().getSubId() != paramInt)
        {
          j = i;
          if (!(localCall.getPhone() instanceof SipPhone)) {}
        }
        else
        {
          i++;
          j = i;
          if (i > 1) {
            return true;
          }
        }
      }
      i = j;
    }
    return false;
  }
  
  private boolean hasMoreThanOneRingingCall()
  {
    int i = 0;
    Iterator localIterator = mRingingCalls.iterator();
    while (localIterator.hasNext())
    {
      int j = i;
      if (((Call)localIterator.next()).getState().isRinging())
      {
        i++;
        j = i;
        if (i > 1) {
          return true;
        }
      }
      i = j;
    }
    return false;
  }
  
  private boolean hasMoreThanOneRingingCall(int paramInt)
  {
    int i = 0;
    Iterator localIterator = mRingingCalls.iterator();
    while (localIterator.hasNext())
    {
      Call localCall = (Call)localIterator.next();
      int j = i;
      if (localCall.getState().isRinging()) {
        if (localCall.getPhone().getSubId() != paramInt)
        {
          j = i;
          if (!(localCall.getPhone() instanceof SipPhone)) {}
        }
        else
        {
          i++;
          j = i;
          if (i > 1) {
            return true;
          }
        }
      }
      i = j;
    }
    return false;
  }
  
  private void registerForPhoneStates(Phone paramPhone)
  {
    if ((CallManagerHandler)mHandlerMap.get(paramPhone) != null)
    {
      Rlog.d("CallManager", "This phone has already been registered.");
      return;
    }
    CallManagerHandler localCallManagerHandler = new CallManagerHandler(null);
    mHandlerMap.put(paramPhone, localCallManagerHandler);
    paramPhone.registerForPreciseCallStateChanged(localCallManagerHandler, 101, mRegistrantidentifier);
    paramPhone.registerForDisconnect(localCallManagerHandler, 100, mRegistrantidentifier);
    paramPhone.registerForNewRingingConnection(localCallManagerHandler, 102, mRegistrantidentifier);
    paramPhone.registerForUnknownConnection(localCallManagerHandler, 103, mRegistrantidentifier);
    paramPhone.registerForIncomingRing(localCallManagerHandler, 104, mRegistrantidentifier);
    paramPhone.registerForRingbackTone(localCallManagerHandler, 105, mRegistrantidentifier);
    paramPhone.registerForInCallVoicePrivacyOn(localCallManagerHandler, 106, mRegistrantidentifier);
    paramPhone.registerForInCallVoicePrivacyOff(localCallManagerHandler, 107, mRegistrantidentifier);
    paramPhone.registerForDisplayInfo(localCallManagerHandler, 109, mRegistrantidentifier);
    paramPhone.registerForSignalInfo(localCallManagerHandler, 110, mRegistrantidentifier);
    paramPhone.registerForResendIncallMute(localCallManagerHandler, 112, mRegistrantidentifier);
    paramPhone.registerForMmiInitiate(localCallManagerHandler, 113, mRegistrantidentifier);
    paramPhone.registerForMmiComplete(localCallManagerHandler, 114, mRegistrantidentifier);
    paramPhone.registerForSuppServiceFailed(localCallManagerHandler, 117, mRegistrantidentifier);
    paramPhone.registerForServiceStateChanged(localCallManagerHandler, 118, mRegistrantidentifier);
    paramPhone.setOnPostDialCharacter(localCallManagerHandler, 119, null);
    paramPhone.registerForCdmaOtaStatusChange(localCallManagerHandler, 111, null);
    paramPhone.registerForSubscriptionInfoReady(localCallManagerHandler, 116, null);
    paramPhone.registerForCallWaiting(localCallManagerHandler, 108, null);
    paramPhone.registerForEcmTimerReset(localCallManagerHandler, 115, null);
    paramPhone.registerForOnHoldTone(localCallManagerHandler, 120, null);
    paramPhone.registerForSuppServiceFailed(localCallManagerHandler, 117, null);
    paramPhone.registerForTtyModeReceived(localCallManagerHandler, 122, null);
  }
  
  private void unregisterForPhoneStates(Phone paramPhone)
  {
    CallManagerHandler localCallManagerHandler = (CallManagerHandler)mHandlerMap.get(paramPhone);
    if (localCallManagerHandler == null)
    {
      Rlog.e("CallManager", "Could not find Phone handler for unregistration");
      return;
    }
    mHandlerMap.remove(paramPhone);
    paramPhone.unregisterForPreciseCallStateChanged(localCallManagerHandler);
    paramPhone.unregisterForDisconnect(localCallManagerHandler);
    paramPhone.unregisterForNewRingingConnection(localCallManagerHandler);
    paramPhone.unregisterForUnknownConnection(localCallManagerHandler);
    paramPhone.unregisterForIncomingRing(localCallManagerHandler);
    paramPhone.unregisterForRingbackTone(localCallManagerHandler);
    paramPhone.unregisterForInCallVoicePrivacyOn(localCallManagerHandler);
    paramPhone.unregisterForInCallVoicePrivacyOff(localCallManagerHandler);
    paramPhone.unregisterForDisplayInfo(localCallManagerHandler);
    paramPhone.unregisterForSignalInfo(localCallManagerHandler);
    paramPhone.unregisterForResendIncallMute(localCallManagerHandler);
    paramPhone.unregisterForMmiInitiate(localCallManagerHandler);
    paramPhone.unregisterForMmiComplete(localCallManagerHandler);
    paramPhone.unregisterForSuppServiceFailed(localCallManagerHandler);
    paramPhone.unregisterForServiceStateChanged(localCallManagerHandler);
    paramPhone.unregisterForTtyModeReceived(localCallManagerHandler);
    paramPhone.setOnPostDialCharacter(null, 119, null);
    paramPhone.unregisterForCdmaOtaStatusChange(localCallManagerHandler);
    paramPhone.unregisterForSubscriptionInfoReady(localCallManagerHandler);
    paramPhone.unregisterForCallWaiting(localCallManagerHandler);
    paramPhone.unregisterForEcmTimerReset(localCallManagerHandler);
    paramPhone.unregisterForOnHoldTone(localCallManagerHandler);
    paramPhone.unregisterForSuppServiceFailed(localCallManagerHandler);
  }
  
  public void acceptCall(Call paramCall)
    throws CallStateException
  {
    paramCall = paramCall.getPhone();
    if (hasActiveFgCall())
    {
      Phone localPhone = getActiveFgCall().getPhone();
      boolean bool = localPhone.getBackgroundCall().isIdle();
      int i = 1;
      int j = bool ^ true;
      if (localPhone != paramCall) {
        i = 0;
      }
      if ((i != 0) && (j != 0)) {
        getActiveFgCall().hangup();
      } else if ((i == 0) && (j == 0)) {
        localPhone.switchHoldingAndActive();
      } else if ((i == 0) && (j != 0)) {
        getActiveFgCall().hangup();
      }
    }
    paramCall.acceptCall(0);
  }
  
  public boolean canConference(Call paramCall)
  {
    Phone localPhone1 = null;
    Phone localPhone2 = null;
    if (hasActiveFgCall()) {
      localPhone1 = getActiveFgCall().getPhone();
    }
    if (paramCall != null) {
      localPhone2 = paramCall.getPhone();
    }
    return localPhone2.getClass().equals(localPhone1.getClass());
  }
  
  public boolean canConference(Call paramCall, int paramInt)
  {
    Phone localPhone1 = null;
    Phone localPhone2 = null;
    if (hasActiveFgCall(paramInt)) {
      localPhone1 = getActiveFgCall(paramInt).getPhone();
    }
    if (paramCall != null) {
      localPhone2 = paramCall.getPhone();
    }
    return localPhone2.getClass().equals(localPhone1.getClass());
  }
  
  public boolean canTransfer(Call paramCall)
  {
    Phone localPhone1 = null;
    Phone localPhone2 = null;
    if (hasActiveFgCall()) {
      localPhone1 = getActiveFgCall().getPhone();
    }
    if (paramCall != null) {
      localPhone2 = paramCall.getPhone();
    }
    boolean bool;
    if ((localPhone2 == localPhone1) && (localPhone1.canTransfer())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean canTransfer(Call paramCall, int paramInt)
  {
    Phone localPhone1 = null;
    Phone localPhone2 = null;
    if (hasActiveFgCall(paramInt)) {
      localPhone1 = getActiveFgCall(paramInt).getPhone();
    }
    if (paramCall != null) {
      localPhone2 = paramCall.getPhone();
    }
    boolean bool;
    if ((localPhone2 == localPhone1) && (localPhone1.canTransfer())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void clearDisconnected()
  {
    Iterator localIterator = mPhones.iterator();
    while (localIterator.hasNext()) {
      ((Phone)localIterator.next()).clearDisconnected();
    }
  }
  
  public void clearDisconnected(int paramInt)
  {
    Iterator localIterator = mPhones.iterator();
    while (localIterator.hasNext())
    {
      Phone localPhone = (Phone)localIterator.next();
      if (localPhone.getSubId() == paramInt) {
        localPhone.clearDisconnected();
      }
    }
  }
  
  public void conference(Call paramCall)
    throws CallStateException
  {
    Phone localPhone = getFgPhone(paramCall.getPhone().getSubId());
    if (localPhone != null)
    {
      if ((localPhone instanceof SipPhone)) {
        ((SipPhone)localPhone).conference(paramCall);
      } else if (canConference(paramCall)) {
        localPhone.conference();
      } else {
        throw new CallStateException("Can't conference foreground and selected background call");
      }
    }
    else {
      Rlog.d("CallManager", "conference: fgPhone=null");
    }
  }
  
  public Connection dial(Phone paramPhone, String paramString, int paramInt)
    throws CallStateException
  {
    int i = paramPhone.getSubId();
    if (!canDial(paramPhone))
    {
      if (paramPhone.handleInCallMmiCommands(PhoneNumberUtils.stripSeparators(paramString))) {
        return null;
      }
      throw new CallStateException("cannot dial in current state");
    }
    if (hasActiveFgCall(i))
    {
      Phone localPhone = getActiveFgCall(i).getPhone();
      boolean bool1 = localPhone.getBackgroundCall().isIdle();
      boolean bool2 = true;
      bool1 ^= true;
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("hasBgCall: ");
      ((StringBuilder)localObject).append(bool1);
      ((StringBuilder)localObject).append(" sameChannel:");
      if (localPhone != paramPhone) {
        bool2 = false;
      }
      ((StringBuilder)localObject).append(bool2);
      Rlog.d("CallManager", ((StringBuilder)localObject).toString());
      localObject = paramPhone.getImsPhone();
      if ((localPhone != paramPhone) && ((localObject == null) || (localObject != localPhone))) {
        if (bool1)
        {
          Rlog.d("CallManager", "Hangup");
          getActiveFgCall(i).hangup();
        }
        else
        {
          Rlog.d("CallManager", "Switch");
          localPhone.switchHoldingAndActive();
        }
      }
    }
    return paramPhone.dial(paramString, new PhoneInternalInterface.DialArgs.Builder().setVideoState(paramInt).build());
  }
  
  public Connection dial(Phone paramPhone, String paramString, UUSInfo paramUUSInfo, int paramInt)
    throws CallStateException
  {
    return paramPhone.dial(paramString, new PhoneInternalInterface.DialArgs.Builder().setUusInfo(paramUUSInfo).setVideoState(paramInt).build());
  }
  
  public void explicitCallTransfer(Call paramCall)
    throws CallStateException
  {
    if (canTransfer(paramCall)) {
      paramCall.getPhone().explicitCallTransfer();
    }
  }
  
  public Call getActiveFgCall()
  {
    Call localCall1 = getFirstNonIdleCall(mForegroundCalls);
    Call localCall2 = localCall1;
    if (localCall1 == null) {
      if (mDefaultPhone == null) {
        localCall2 = null;
      } else {
        localCall2 = mDefaultPhone.getForegroundCall();
      }
    }
    return localCall2;
  }
  
  public Call getActiveFgCall(int paramInt)
  {
    Call localCall = getFirstNonIdleCall(mForegroundCalls, paramInt);
    Object localObject = localCall;
    if (localCall == null)
    {
      localObject = getPhone(paramInt);
      if (localObject == null) {
        localObject = null;
      } else {
        localObject = ((Phone)localObject).getForegroundCall();
      }
    }
    return localObject;
  }
  
  public Call.State getActiveFgCallState()
  {
    Call localCall = getActiveFgCall();
    if (localCall != null) {
      return localCall.getState();
    }
    return Call.State.IDLE;
  }
  
  public Call.State getActiveFgCallState(int paramInt)
  {
    Call localCall = getActiveFgCall(paramInt);
    if (localCall != null) {
      return localCall.getState();
    }
    return Call.State.IDLE;
  }
  
  public List<Phone> getAllPhones()
  {
    return Collections.unmodifiableList(mPhones);
  }
  
  public List<Call> getBackgroundCalls()
  {
    return Collections.unmodifiableList(mBackgroundCalls);
  }
  
  public List<Connection> getBgCallConnections()
  {
    Call localCall = getFirstActiveBgCall();
    if (localCall != null) {
      return localCall.getConnections();
    }
    return mEmptyConnections;
  }
  
  public List<Connection> getBgCallConnections(int paramInt)
  {
    Call localCall = getFirstActiveBgCall(paramInt);
    if (localCall != null) {
      return localCall.getConnections();
    }
    return mEmptyConnections;
  }
  
  public Phone getBgPhone()
  {
    return getFirstActiveBgCall().getPhone();
  }
  
  public Phone getBgPhone(int paramInt)
  {
    return getFirstActiveBgCall(paramInt).getPhone();
  }
  
  public Phone getDefaultPhone()
  {
    return mDefaultPhone;
  }
  
  public List<Connection> getFgCallConnections()
  {
    Call localCall = getActiveFgCall();
    if (localCall != null) {
      return localCall.getConnections();
    }
    return mEmptyConnections;
  }
  
  public List<Connection> getFgCallConnections(int paramInt)
  {
    Call localCall = getActiveFgCall(paramInt);
    if (localCall != null) {
      return localCall.getConnections();
    }
    return mEmptyConnections;
  }
  
  public Connection getFgCallLatestConnection()
  {
    Call localCall = getActiveFgCall();
    if (localCall != null) {
      return localCall.getLatestConnection();
    }
    return null;
  }
  
  public Connection getFgCallLatestConnection(int paramInt)
  {
    Call localCall = getActiveFgCall(paramInt);
    if (localCall != null) {
      return localCall.getLatestConnection();
    }
    return null;
  }
  
  public Phone getFgPhone()
  {
    return getActiveFgCall().getPhone();
  }
  
  public Phone getFgPhone(int paramInt)
  {
    return getActiveFgCall(paramInt).getPhone();
  }
  
  public Call getFirstActiveBgCall()
  {
    Call localCall1 = getFirstNonIdleCall(mBackgroundCalls);
    Call localCall2 = localCall1;
    if (localCall1 == null) {
      if (mDefaultPhone == null) {
        localCall2 = null;
      } else {
        localCall2 = mDefaultPhone.getBackgroundCall();
      }
    }
    return localCall2;
  }
  
  public Call getFirstActiveBgCall(int paramInt)
  {
    Phone localPhone = getPhone(paramInt);
    if (hasMoreThanOneHoldingCall(paramInt)) {
      return localPhone.getBackgroundCall();
    }
    Call localCall1 = getFirstNonIdleCall(mBackgroundCalls, paramInt);
    Call localCall2 = localCall1;
    if (localCall1 == null) {
      if (localPhone == null) {
        localCall2 = null;
      } else {
        localCall2 = localPhone.getBackgroundCall();
      }
    }
    return localCall2;
  }
  
  public Call getFirstActiveRingingCall()
  {
    Call localCall1 = getFirstNonIdleCall(mRingingCalls);
    Call localCall2 = localCall1;
    if (localCall1 == null) {
      if (mDefaultPhone == null) {
        localCall2 = null;
      } else {
        localCall2 = mDefaultPhone.getRingingCall();
      }
    }
    return localCall2;
  }
  
  public Call getFirstActiveRingingCall(int paramInt)
  {
    Phone localPhone = getPhone(paramInt);
    Call localCall1 = getFirstNonIdleCall(mRingingCalls, paramInt);
    Call localCall2 = localCall1;
    if (localCall1 == null) {
      if (localPhone == null) {
        localCall2 = null;
      } else {
        localCall2 = localPhone.getRingingCall();
      }
    }
    return localCall2;
  }
  
  public List<Call> getForegroundCalls()
  {
    return Collections.unmodifiableList(mForegroundCalls);
  }
  
  public boolean getMute()
  {
    if (hasActiveFgCall()) {
      return getActiveFgCall().getPhone().getMute();
    }
    if (hasActiveBgCall()) {
      return getFirstActiveBgCall().getPhone().getMute();
    }
    return false;
  }
  
  public List<? extends MmiCode> getPendingMmiCodes(Phone paramPhone)
  {
    Rlog.e("CallManager", "getPendingMmiCodes not implemented");
    return null;
  }
  
  public Phone getPhoneInCall()
  {
    Phone localPhone;
    if (!getFirstActiveRingingCall().isIdle()) {
      localPhone = getFirstActiveRingingCall().getPhone();
    } else if (!getActiveFgCall().isIdle()) {
      localPhone = getActiveFgCall().getPhone();
    } else {
      localPhone = getFirstActiveBgCall().getPhone();
    }
    return localPhone;
  }
  
  public Phone getPhoneInCall(int paramInt)
  {
    Phone localPhone;
    if (!getFirstActiveRingingCall(paramInt).isIdle()) {
      localPhone = getFirstActiveRingingCall(paramInt).getPhone();
    } else if (!getActiveFgCall(paramInt).isIdle()) {
      localPhone = getActiveFgCall(paramInt).getPhone();
    } else {
      localPhone = getFirstActiveBgCall(paramInt).getPhone();
    }
    return localPhone;
  }
  
  public Object getRegistrantIdentifier()
  {
    return mRegistrantidentifier;
  }
  
  public List<Call> getRingingCalls()
  {
    return Collections.unmodifiableList(mRingingCalls);
  }
  
  public Phone getRingingPhone()
  {
    return getFirstActiveRingingCall().getPhone();
  }
  
  public Phone getRingingPhone(int paramInt)
  {
    return getFirstActiveRingingCall(paramInt).getPhone();
  }
  
  public int getServiceState()
  {
    int i = 1;
    Iterator localIterator = mPhones.iterator();
    int j;
    for (;;)
    {
      j = i;
      if (!localIterator.hasNext()) {
        break;
      }
      int k = ((Phone)localIterator.next()).getServiceState().getState();
      if (k == 0)
      {
        j = k;
        break;
      }
      if (k == 1)
      {
        if (i != 2)
        {
          j = i;
          if (i != 3) {}
        }
        else
        {
          j = k;
        }
      }
      else
      {
        j = i;
        if (k == 2)
        {
          j = i;
          if (i == 3) {
            j = k;
          }
        }
      }
      i = j;
    }
    return j;
  }
  
  public int getServiceState(int paramInt)
  {
    int i = 1;
    Iterator localIterator = mPhones.iterator();
    int j;
    for (;;)
    {
      j = i;
      if (!localIterator.hasNext()) {
        break;
      }
      Phone localPhone = (Phone)localIterator.next();
      j = i;
      if (localPhone.getSubId() == paramInt)
      {
        int k = localPhone.getServiceState().getState();
        if (k == 0)
        {
          j = k;
          break;
        }
        if (k == 1)
        {
          if (i != 2)
          {
            j = i;
            if (i != 3) {}
          }
          else
          {
            j = k;
          }
        }
        else
        {
          j = i;
          if (k == 2)
          {
            j = i;
            if (i == 3) {
              j = k;
            }
          }
        }
      }
      i = j;
    }
    return j;
  }
  
  public PhoneConstants.State getState()
  {
    Object localObject1 = PhoneConstants.State.IDLE;
    Iterator localIterator = mPhones.iterator();
    while (localIterator.hasNext())
    {
      Phone localPhone = (Phone)localIterator.next();
      Object localObject2;
      if (localPhone.getState() == PhoneConstants.State.RINGING)
      {
        localObject2 = PhoneConstants.State.RINGING;
      }
      else
      {
        localObject2 = localObject1;
        if (localPhone.getState() == PhoneConstants.State.OFFHOOK)
        {
          localObject2 = localObject1;
          if (localObject1 == PhoneConstants.State.IDLE) {
            localObject2 = PhoneConstants.State.OFFHOOK;
          }
        }
      }
      localObject1 = localObject2;
    }
    return localObject1;
  }
  
  public PhoneConstants.State getState(int paramInt)
  {
    Object localObject1 = PhoneConstants.State.IDLE;
    Iterator localIterator = mPhones.iterator();
    while (localIterator.hasNext())
    {
      Phone localPhone = (Phone)localIterator.next();
      Object localObject2 = localObject1;
      if (localPhone.getSubId() == paramInt) {
        if (localPhone.getState() == PhoneConstants.State.RINGING)
        {
          localObject2 = PhoneConstants.State.RINGING;
        }
        else
        {
          localObject2 = localObject1;
          if (localPhone.getState() == PhoneConstants.State.OFFHOOK)
          {
            localObject2 = localObject1;
            if (localObject1 == PhoneConstants.State.IDLE) {
              localObject2 = PhoneConstants.State.OFFHOOK;
            }
          }
        }
      }
      localObject1 = localObject2;
    }
    return localObject1;
  }
  
  public void hangupForegroundResumeBackground(Call paramCall)
    throws CallStateException
  {
    if (hasActiveFgCall())
    {
      Phone localPhone = getFgPhone();
      if (paramCall != null) {
        if (localPhone == paramCall.getPhone())
        {
          getActiveFgCall().hangup();
        }
        else
        {
          getActiveFgCall().hangup();
          switchHoldingAndActive(paramCall);
        }
      }
    }
  }
  
  public boolean hasActiveBgCall()
  {
    boolean bool;
    if (getFirstActiveCall(mBackgroundCalls) != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasActiveBgCall(int paramInt)
  {
    boolean bool;
    if (getFirstActiveCall(mBackgroundCalls, paramInt) != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasActiveFgCall()
  {
    boolean bool;
    if (getFirstActiveCall(mForegroundCalls) != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasActiveFgCall(int paramInt)
  {
    boolean bool;
    if (getFirstActiveCall(mForegroundCalls, paramInt) != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasActiveRingingCall()
  {
    boolean bool;
    if (getFirstActiveCall(mRingingCalls) != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasActiveRingingCall(int paramInt)
  {
    boolean bool;
    if (getFirstActiveCall(mRingingCalls, paramInt) != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasDisconnectedBgCall()
  {
    boolean bool;
    if (getFirstCallOfState(mBackgroundCalls, Call.State.DISCONNECTED) != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasDisconnectedBgCall(int paramInt)
  {
    boolean bool;
    if (getFirstCallOfState(mBackgroundCalls, Call.State.DISCONNECTED, paramInt) != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasDisconnectedFgCall()
  {
    boolean bool;
    if (getFirstCallOfState(mForegroundCalls, Call.State.DISCONNECTED) != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasDisconnectedFgCall(int paramInt)
  {
    boolean bool;
    if (getFirstCallOfState(mForegroundCalls, Call.State.DISCONNECTED, paramInt) != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void registerForCallWaiting(Handler paramHandler, int paramInt, Object paramObject)
  {
    mCallWaitingRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForCdmaOtaStatusChange(Handler paramHandler, int paramInt, Object paramObject)
  {
    mCdmaOtaStatusChangeRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForDisconnect(Handler paramHandler, int paramInt, Object paramObject)
  {
    mDisconnectRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForDisplayInfo(Handler paramHandler, int paramInt, Object paramObject)
  {
    mDisplayInfoRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForEcmTimerReset(Handler paramHandler, int paramInt, Object paramObject)
  {
    mEcmTimerResetRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForInCallVoicePrivacyOff(Handler paramHandler, int paramInt, Object paramObject)
  {
    mInCallVoicePrivacyOffRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForInCallVoicePrivacyOn(Handler paramHandler, int paramInt, Object paramObject)
  {
    mInCallVoicePrivacyOnRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForIncomingRing(Handler paramHandler, int paramInt, Object paramObject)
  {
    mIncomingRingRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForMmiComplete(Handler paramHandler, int paramInt, Object paramObject)
  {
    Rlog.d("CallManager", "registerForMmiComplete");
    mMmiCompleteRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForMmiInitiate(Handler paramHandler, int paramInt, Object paramObject)
  {
    mMmiInitiateRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForNewRingingConnection(Handler paramHandler, int paramInt, Object paramObject)
  {
    mNewRingingConnectionRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForOnHoldTone(Handler paramHandler, int paramInt, Object paramObject)
  {
    mOnHoldToneRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForPostDialCharacter(Handler paramHandler, int paramInt, Object paramObject)
  {
    mPostDialCharacterRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForPreciseCallStateChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    mPreciseCallStateRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForResendIncallMute(Handler paramHandler, int paramInt, Object paramObject)
  {
    mResendIncallMuteRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForRingbackTone(Handler paramHandler, int paramInt, Object paramObject)
  {
    mRingbackToneRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForServiceStateChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    mServiceStateChangedRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForSignalInfo(Handler paramHandler, int paramInt, Object paramObject)
  {
    mSignalInfoRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForSubscriptionInfoReady(Handler paramHandler, int paramInt, Object paramObject)
  {
    mSubscriptionInfoReadyRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForSuppServiceFailed(Handler paramHandler, int paramInt, Object paramObject)
  {
    mSuppServiceFailedRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForTtyModeReceived(Handler paramHandler, int paramInt, Object paramObject)
  {
    mTtyModeReceivedRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForUnknownConnection(Handler paramHandler, int paramInt, Object paramObject)
  {
    mUnknownConnectionRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public boolean registerPhone(Phone paramPhone)
  {
    if ((paramPhone != null) && (!mPhones.contains(paramPhone)))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("registerPhone(");
      localStringBuilder.append(paramPhone.getPhoneName());
      localStringBuilder.append(" ");
      localStringBuilder.append(paramPhone);
      localStringBuilder.append(")");
      Rlog.d("CallManager", localStringBuilder.toString());
      if (mPhones.isEmpty()) {
        mDefaultPhone = paramPhone;
      }
      mPhones.add(paramPhone);
      mRingingCalls.add(paramPhone.getRingingCall());
      mBackgroundCalls.add(paramPhone.getBackgroundCall());
      mForegroundCalls.add(paramPhone.getForegroundCall());
      registerForPhoneStates(paramPhone);
      return true;
    }
    return false;
  }
  
  public void rejectCall(Call paramCall)
    throws CallStateException
  {
    paramCall.getPhone().rejectCall();
  }
  
  public boolean sendBurstDtmf(String paramString, int paramInt1, int paramInt2, Message paramMessage)
  {
    if (hasActiveFgCall())
    {
      getActiveFgCall().getPhone().sendBurstDtmf(paramString, paramInt1, paramInt2, paramMessage);
      return true;
    }
    return false;
  }
  
  public boolean sendDtmf(char paramChar)
  {
    boolean bool = false;
    if (hasActiveFgCall())
    {
      getActiveFgCall().getPhone().sendDtmf(paramChar);
      bool = true;
    }
    return bool;
  }
  
  public boolean sendUssdResponse(Phone paramPhone, String paramString)
  {
    Rlog.e("CallManager", "sendUssdResponse not implemented");
    return false;
  }
  
  public void setEchoSuppressionEnabled()
  {
    if (hasActiveFgCall()) {
      getActiveFgCall().getPhone().setEchoSuppressionEnabled();
    }
  }
  
  public void setMute(boolean paramBoolean)
  {
    if (hasActiveFgCall()) {
      getActiveFgCall().getPhone().setMute(paramBoolean);
    }
  }
  
  public boolean startDtmf(char paramChar)
  {
    boolean bool = false;
    if (hasActiveFgCall())
    {
      getActiveFgCall().getPhone().startDtmf(paramChar);
      bool = true;
    }
    return bool;
  }
  
  public void stopDtmf()
  {
    if (hasActiveFgCall()) {
      getFgPhone().stopDtmf();
    }
  }
  
  public void switchHoldingAndActive(Call paramCall)
    throws CallStateException
  {
    Phone localPhone1 = null;
    Phone localPhone2 = null;
    if (hasActiveFgCall()) {
      localPhone1 = getActiveFgCall().getPhone();
    }
    if (paramCall != null) {
      localPhone2 = paramCall.getPhone();
    }
    if (localPhone1 != null) {
      localPhone1.switchHoldingAndActive();
    }
    if ((localPhone2 != null) && (localPhone2 != localPhone1)) {
      localPhone2.switchHoldingAndActive();
    }
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Object localObject2;
    for (int i = 0; i < TelephonyManager.getDefault().getPhoneCount(); i++)
    {
      localStringBuilder.append("CallManager {");
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("\nstate = ");
      ((StringBuilder)localObject1).append(getState(i));
      localStringBuilder.append(((StringBuilder)localObject1).toString());
      localObject1 = getActiveFgCall(i);
      if (localObject1 != null)
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("\n- Foreground: ");
        ((StringBuilder)localObject2).append(getActiveFgCallState(i));
        localStringBuilder.append(((StringBuilder)localObject2).toString());
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append(" from ");
        ((StringBuilder)localObject2).append(((Call)localObject1).getPhone());
        localStringBuilder.append(((StringBuilder)localObject2).toString());
        localStringBuilder.append("\n  Conn: ");
        localStringBuilder.append(getFgCallConnections(i));
      }
      localObject1 = getFirstActiveBgCall(i);
      if (localObject1 != null)
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("\n- Background: ");
        ((StringBuilder)localObject2).append(((Call)localObject1).getState());
        localStringBuilder.append(((StringBuilder)localObject2).toString());
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append(" from ");
        ((StringBuilder)localObject2).append(((Call)localObject1).getPhone());
        localStringBuilder.append(((StringBuilder)localObject2).toString());
        localStringBuilder.append("\n  Conn: ");
        localStringBuilder.append(getBgCallConnections(i));
      }
      localObject1 = getFirstActiveRingingCall(i);
      if (localObject1 != null)
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("\n- Ringing: ");
        ((StringBuilder)localObject2).append(((Call)localObject1).getState());
        localStringBuilder.append(((StringBuilder)localObject2).toString());
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append(" from ");
        ((StringBuilder)localObject2).append(((Call)localObject1).getPhone());
        localStringBuilder.append(((StringBuilder)localObject2).toString());
      }
    }
    Object localObject1 = getAllPhones().iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (Phone)((Iterator)localObject1).next();
      if (localObject2 != null)
      {
        Object localObject3 = new StringBuilder();
        ((StringBuilder)localObject3).append("\nPhone: ");
        ((StringBuilder)localObject3).append(localObject2);
        ((StringBuilder)localObject3).append(", name = ");
        ((StringBuilder)localObject3).append(((Phone)localObject2).getPhoneName());
        ((StringBuilder)localObject3).append(", state = ");
        ((StringBuilder)localObject3).append(((Phone)localObject2).getState());
        localStringBuilder.append(((StringBuilder)localObject3).toString());
        localObject3 = ((Phone)localObject2).getForegroundCall();
        if (localObject3 != null)
        {
          localStringBuilder.append("\n- Foreground: ");
          localStringBuilder.append(localObject3);
        }
        localObject3 = ((Phone)localObject2).getBackgroundCall();
        if (localObject3 != null)
        {
          localStringBuilder.append(" Background: ");
          localStringBuilder.append(localObject3);
        }
        localObject2 = ((Phone)localObject2).getRingingCall();
        if (localObject2 != null)
        {
          localStringBuilder.append(" Ringing: ");
          localStringBuilder.append(localObject2);
        }
      }
    }
    localStringBuilder.append("\n}");
    return localStringBuilder.toString();
  }
  
  public void unregisterForCallWaiting(Handler paramHandler)
  {
    mCallWaitingRegistrants.remove(paramHandler);
  }
  
  public void unregisterForCdmaOtaStatusChange(Handler paramHandler)
  {
    mCdmaOtaStatusChangeRegistrants.remove(paramHandler);
  }
  
  public void unregisterForDisconnect(Handler paramHandler)
  {
    mDisconnectRegistrants.remove(paramHandler);
  }
  
  public void unregisterForDisplayInfo(Handler paramHandler)
  {
    mDisplayInfoRegistrants.remove(paramHandler);
  }
  
  public void unregisterForEcmTimerReset(Handler paramHandler)
  {
    mEcmTimerResetRegistrants.remove(paramHandler);
  }
  
  public void unregisterForInCallVoicePrivacyOff(Handler paramHandler)
  {
    mInCallVoicePrivacyOffRegistrants.remove(paramHandler);
  }
  
  public void unregisterForInCallVoicePrivacyOn(Handler paramHandler)
  {
    mInCallVoicePrivacyOnRegistrants.remove(paramHandler);
  }
  
  public void unregisterForIncomingRing(Handler paramHandler)
  {
    mIncomingRingRegistrants.remove(paramHandler);
  }
  
  public void unregisterForMmiComplete(Handler paramHandler)
  {
    mMmiCompleteRegistrants.remove(paramHandler);
  }
  
  public void unregisterForMmiInitiate(Handler paramHandler)
  {
    mMmiInitiateRegistrants.remove(paramHandler);
  }
  
  public void unregisterForNewRingingConnection(Handler paramHandler)
  {
    mNewRingingConnectionRegistrants.remove(paramHandler);
  }
  
  public void unregisterForOnHoldTone(Handler paramHandler)
  {
    mOnHoldToneRegistrants.remove(paramHandler);
  }
  
  public void unregisterForPostDialCharacter(Handler paramHandler)
  {
    mPostDialCharacterRegistrants.remove(paramHandler);
  }
  
  public void unregisterForPreciseCallStateChanged(Handler paramHandler)
  {
    mPreciseCallStateRegistrants.remove(paramHandler);
  }
  
  public void unregisterForResendIncallMute(Handler paramHandler)
  {
    mResendIncallMuteRegistrants.remove(paramHandler);
  }
  
  public void unregisterForRingbackTone(Handler paramHandler)
  {
    mRingbackToneRegistrants.remove(paramHandler);
  }
  
  public void unregisterForServiceStateChanged(Handler paramHandler)
  {
    mServiceStateChangedRegistrants.remove(paramHandler);
  }
  
  public void unregisterForSignalInfo(Handler paramHandler)
  {
    mSignalInfoRegistrants.remove(paramHandler);
  }
  
  public void unregisterForSubscriptionInfoReady(Handler paramHandler)
  {
    mSubscriptionInfoReadyRegistrants.remove(paramHandler);
  }
  
  public void unregisterForSuppServiceFailed(Handler paramHandler)
  {
    mSuppServiceFailedRegistrants.remove(paramHandler);
  }
  
  public void unregisterForTtyModeReceived(Handler paramHandler)
  {
    mTtyModeReceivedRegistrants.remove(paramHandler);
  }
  
  public void unregisterForUnknownConnection(Handler paramHandler)
  {
    mUnknownConnectionRegistrants.remove(paramHandler);
  }
  
  public void unregisterPhone(Phone paramPhone)
  {
    if ((paramPhone != null) && (mPhones.contains(paramPhone)))
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("unregisterPhone(");
      ((StringBuilder)localObject).append(paramPhone.getPhoneName());
      ((StringBuilder)localObject).append(" ");
      ((StringBuilder)localObject).append(paramPhone);
      ((StringBuilder)localObject).append(")");
      Rlog.d("CallManager", ((StringBuilder)localObject).toString());
      localObject = paramPhone.getImsPhone();
      if (localObject != null) {
        unregisterPhone((Phone)localObject);
      }
      mPhones.remove(paramPhone);
      mRingingCalls.remove(paramPhone.getRingingCall());
      mBackgroundCalls.remove(paramPhone.getBackgroundCall());
      mForegroundCalls.remove(paramPhone.getForegroundCall());
      unregisterForPhoneStates(paramPhone);
      if (paramPhone == mDefaultPhone) {
        if (mPhones.isEmpty()) {
          mDefaultPhone = null;
        } else {
          mDefaultPhone = ((Phone)mPhones.get(0));
        }
      }
    }
  }
  
  private class CallManagerHandler
    extends Handler
  {
    private CallManagerHandler() {}
    
    public void handleMessage(Message paramMessage)
    {
      int i;
      Object localObject;
      switch (what)
      {
      case 121: 
      default: 
        break;
      case 122: 
        mTtyModeReceivedRegistrants.notifyRegistrants((AsyncResult)obj);
        break;
      case 120: 
        mOnHoldToneRegistrants.notifyRegistrants((AsyncResult)obj);
        break;
      case 119: 
        for (i = 0; i < mPostDialCharacterRegistrants.size(); i++)
        {
          localObject = ((Registrant)mPostDialCharacterRegistrants.get(i)).messageForRegistrant();
          obj = obj;
          arg1 = arg1;
          ((Message)localObject).sendToTarget();
        }
        break;
      case 118: 
        mServiceStateChangedRegistrants.notifyRegistrants((AsyncResult)obj);
        break;
      case 117: 
        mSuppServiceFailedRegistrants.notifyRegistrants((AsyncResult)obj);
        break;
      case 116: 
        mSubscriptionInfoReadyRegistrants.notifyRegistrants((AsyncResult)obj);
        break;
      case 115: 
        mEcmTimerResetRegistrants.notifyRegistrants((AsyncResult)obj);
        break;
      case 114: 
        Rlog.d("CallManager", "CallManager: handleMessage (EVENT_MMI_COMPLETE)");
        mMmiCompleteRegistrants.notifyRegistrants((AsyncResult)obj);
        break;
      case 113: 
        mMmiInitiateRegistrants.notifyRegistrants((AsyncResult)obj);
        break;
      case 112: 
        mResendIncallMuteRegistrants.notifyRegistrants((AsyncResult)obj);
        break;
      case 111: 
        mCdmaOtaStatusChangeRegistrants.notifyRegistrants((AsyncResult)obj);
        break;
      case 110: 
        mSignalInfoRegistrants.notifyRegistrants((AsyncResult)obj);
        break;
      case 109: 
        mDisplayInfoRegistrants.notifyRegistrants((AsyncResult)obj);
        break;
      case 108: 
        mCallWaitingRegistrants.notifyRegistrants((AsyncResult)obj);
        break;
      case 107: 
        mInCallVoicePrivacyOffRegistrants.notifyRegistrants((AsyncResult)obj);
        break;
      case 106: 
        mInCallVoicePrivacyOnRegistrants.notifyRegistrants((AsyncResult)obj);
        break;
      case 105: 
        mRingbackToneRegistrants.notifyRegistrants((AsyncResult)obj);
        break;
      case 104: 
        if (!hasActiveFgCall()) {
          mIncomingRingRegistrants.notifyRegistrants((AsyncResult)obj);
        }
        break;
      case 103: 
        mUnknownConnectionRegistrants.notifyRegistrants((AsyncResult)obj);
        break;
      case 102: 
        localObject = (Connection)obj).result;
        i = ((Connection)localObject).getCall().getPhone().getSubId();
        if ((!getActiveFgCallState(i).isDialing()) && (!CallManager.this.hasMoreThanOneRingingCall())) {
          mNewRingingConnectionRegistrants.notifyRegistrants((AsyncResult)obj);
        } else {
          try
          {
            paramMessage = new java/lang/StringBuilder;
            paramMessage.<init>();
            paramMessage.append("silently drop incoming call: ");
            paramMessage.append(((Connection)localObject).getCall());
            Rlog.d("CallManager", paramMessage.toString());
            ((Connection)localObject).getCall().hangup();
          }
          catch (CallStateException paramMessage)
          {
            Rlog.w("CallManager", "new ringing connection", paramMessage);
          }
        }
        break;
      case 101: 
        mPreciseCallStateRegistrants.notifyRegistrants((AsyncResult)obj);
        break;
      case 100: 
        mDisconnectRegistrants.notifyRegistrants((AsyncResult)obj);
      }
    }
  }
}
