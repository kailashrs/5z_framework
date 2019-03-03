package com.android.internal.telephony.imsphone;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Registrant;
import android.os.SystemClock;
import android.telecom.Connection.RttTextStream;
import android.telecom.Connection.VideoProvider;
import android.telecom.VideoProfile;
import android.telephony.CarrierConfigManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsStreamMediaProfile;
import android.text.TextUtils;
import com.android.ims.ImsCall;
import com.android.ims.ImsException;
import com.android.ims.internal.ImsVideoCallProviderWrapper;
import com.android.ims.internal.ImsVideoCallProviderWrapper.ImsVideoProviderWrapperCallback;
import com.android.internal.telephony.Call.State;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.Connection.PostDialState;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.UUSInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class ImsPhoneConnection
  extends Connection
  implements ImsVideoCallProviderWrapper.ImsVideoProviderWrapperCallback
{
  private static final boolean DBG = true;
  private static final int EVENT_DTMF_DELAY_DONE = 5;
  private static final int EVENT_DTMF_DONE = 1;
  private static final int EVENT_NEXT_POST_DIAL = 3;
  private static final int EVENT_PAUSE_DONE = 2;
  private static final int EVENT_WAKE_LOCK_TIMEOUT = 4;
  private static final String LOG_TAG = "ImsPhoneConnection";
  public static ArrayList<String> NumberRecords = new ArrayList();
  private static final int PAUSE_DELAY_MILLIS = 3000;
  private static final int WAKE_LOCK_TIMEOUT_MILLIS = 60000;
  private long mConferenceConnectTime = 0L;
  private long mDisconnectTime;
  private boolean mDisconnected;
  private int mDtmfToneDelay = 0;
  private Bundle mExtras = new Bundle();
  private Handler mHandler;
  private Messenger mHandlerMessenger;
  private ImsCall mImsCall;
  private ImsVideoCallProviderWrapper mImsVideoCallProviderWrapper;
  private boolean mIsEmergency = false;
  private boolean mIsMergeInProcess = false;
  private boolean mIsRttEnabledForCall = false;
  private boolean mIsVideoEnabled = true;
  private ImsPhoneCallTracker mOwner;
  private ImsPhoneCall mParent;
  private PowerManager.WakeLock mPartialWakeLock;
  private int mPreciseDisconnectCause = 0;
  private ImsRttTextHandler mRttTextHandler;
  private Connection.RttTextStream mRttTextStream;
  private boolean mShouldIgnoreVideoStateChanges = false;
  private UUSInfo mUusInfo;
  
  public ImsPhoneConnection(Phone paramPhone, ImsCall paramImsCall, ImsPhoneCallTracker paramImsPhoneCallTracker, ImsPhoneCall paramImsPhoneCall, boolean paramBoolean)
  {
    super(5);
    createWakeLock(paramPhone.getContext());
    acquireWakeLock();
    mOwner = paramImsPhoneCallTracker;
    mHandler = new MyHandler(mOwner.getLooper());
    mHandlerMessenger = new Messenger(mHandler);
    mImsCall = paramImsCall;
    if ((paramImsCall != null) && (paramImsCall.getCallProfile() != null))
    {
      mAddress = paramImsCall.getCallProfile().getCallExtra("oi");
      mCnapName = paramImsCall.getCallProfile().getCallExtra("cna");
      mNumberPresentation = ImsCallProfile.OIRToPresentation(paramImsCall.getCallProfile().getCallExtraInt("oir"));
      mCnapNamePresentation = ImsCallProfile.OIRToPresentation(paramImsCall.getCallProfile().getCallExtraInt("cnap"));
      updateMediaCapabilities(paramImsCall);
    }
    else
    {
      mNumberPresentation = 3;
      mCnapNamePresentation = 3;
    }
    mIsIncoming = (paramBoolean ^ true);
    mCreateTime = System.currentTimeMillis();
    mUusInfo = null;
    updateExtras(paramImsCall);
    mParent = paramImsPhoneCall;
    paramImsPhoneCallTracker = mParent;
    if (mIsIncoming) {
      paramImsCall = Call.State.INCOMING;
    } else {
      paramImsCall = Call.State.DIALING;
    }
    paramImsPhoneCallTracker.attach(this, paramImsCall);
    fetchDtmfToneDelay(paramPhone);
    if (paramPhone.getContext().getResources().getBoolean(17957074)) {
      setAudioModeIsVoip(true);
    }
  }
  
  public ImsPhoneConnection(Phone paramPhone, String paramString, ImsPhoneCallTracker paramImsPhoneCallTracker, ImsPhoneCall paramImsPhoneCall, boolean paramBoolean)
  {
    this(paramPhone, paramString, paramImsPhoneCallTracker, paramImsPhoneCall, paramBoolean, null);
  }
  
  public ImsPhoneConnection(Phone paramPhone, String paramString, ImsPhoneCallTracker paramImsPhoneCallTracker, ImsPhoneCall paramImsPhoneCall, boolean paramBoolean, Bundle paramBundle)
  {
    super(5);
    createWakeLock(paramPhone.getContext());
    acquireWakeLock();
    boolean bool1 = false;
    boolean bool2 = false;
    if (paramBundle != null)
    {
      bool1 = paramBundle.getBoolean("org.codeaurora.extra.DIAL_CONFERENCE_URI", false);
      bool2 = paramBundle.getBoolean("org.codeaurora.extra.SKIP_SCHEMA_PARSING", false);
    }
    mOwner = paramImsPhoneCallTracker;
    mHandler = new MyHandler(mOwner.getLooper());
    mDialString = paramString;
    if ((!bool1) && (!bool2))
    {
      mAddress = PhoneNumberUtils.extractNetworkPortionAlt(paramString);
      mPostDialString = PhoneNumberUtils.extractPostDialPortion(paramString);
    }
    else
    {
      mAddress = paramString;
      mPostDialString = "";
    }
    mIsIncoming = false;
    mCnapName = null;
    mCnapNamePresentation = 1;
    mNumberPresentation = 1;
    mCreateTime = System.currentTimeMillis();
    mParent = paramImsPhoneCall;
    paramImsPhoneCall.attachFake(this, Call.State.DIALING);
    mIsEmergency = paramBoolean;
    fetchDtmfToneDelay(paramPhone);
    if (paramPhone.getContext().getResources().getBoolean(17957074)) {
      setAudioModeIsVoip(true);
    }
  }
  
  private void acquireWakeLock()
  {
    Rlog.d("ImsPhoneConnection", "acquireWakeLock");
    mPartialWakeLock.acquire();
  }
  
  private int applyLocalCallCapabilities(ImsCallProfile paramImsCallProfile, int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("applyLocalCallCapabilities - localProfile = ");
    localStringBuilder.append(paramImsCallProfile);
    Rlog.i("ImsPhoneConnection", localStringBuilder.toString());
    paramInt = removeCapability(paramInt, 4);
    if (!mIsVideoEnabled)
    {
      Rlog.i("ImsPhoneConnection", "applyLocalCallCapabilities - disabling video (overidden)");
      return paramInt;
    }
    int i = mCallType;
    if (i != -1) {
      switch (i)
      {
      default: 
        break;
      case 3: 
      case 4: 
        paramInt = addCapability(paramInt, 4);
        break;
      }
    } else {
      paramInt = removeCapability(paramInt, 1);
    }
    return paramInt;
  }
  
  private static int applyRemoteCallCapabilities(ImsCallProfile paramImsCallProfile, int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("applyRemoteCallCapabilities - remoteProfile = ");
    localStringBuilder.append(paramImsCallProfile);
    Rlog.w("ImsPhoneConnection", localStringBuilder.toString());
    paramInt = removeCapability(paramInt, 8);
    int i = mCallType;
    if (i != -1) {
      switch (i)
      {
      default: 
        break;
      case 3: 
      case 4: 
        paramInt = addCapability(paramInt, 8);
        break;
      }
    } else {
      paramInt = removeCapability(paramInt, 2);
    }
    i = paramInt;
    if (paramImsCallProfile.getMediaProfile().getRttMode() == 1) {
      i = addCapability(paramInt, 64);
    }
    return i;
  }
  
  private static boolean areBundlesEqual(Bundle paramBundle1, Bundle paramBundle2)
  {
    boolean bool = true;
    if ((paramBundle1 != null) && (paramBundle2 != null))
    {
      if (paramBundle1.size() != paramBundle2.size()) {
        return false;
      }
      Iterator localIterator = paramBundle1.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        if ((str != null) && (!Objects.equals(paramBundle1.get(str), paramBundle2.get(str)))) {
          return false;
        }
      }
      return true;
    }
    if (paramBundle1 != paramBundle2) {
      bool = false;
    }
    return bool;
  }
  
  public static void clearNumberRecords()
  {
    for (int i = NumberRecords.size() - 1; i >= 0; i--)
    {
      Object localObject1 = (String)NumberRecords.remove(i);
      Object localObject2 = localObject1;
      if (((String)localObject1).length() > 4)
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("******");
        ((StringBuilder)localObject2).append(((String)localObject1).substring(((String)localObject1).length() - 4, ((String)localObject1).length()));
        localObject2 = ((StringBuilder)localObject2).toString();
      }
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Remove NumberRecords [");
      ((StringBuilder)localObject1).append(i);
      ((StringBuilder)localObject1).append("] = ");
      ((StringBuilder)localObject1).append((String)localObject2);
      Rlog.d("ImsPhoneConnection", ((StringBuilder)localObject1).toString());
    }
  }
  
  private void createRttTextHandler()
  {
    mRttTextHandler = new ImsRttTextHandler(Looper.getMainLooper(), new _..Lambda.ImsPhoneConnection.gXYXXIQcibrbO2gQqP7d18avaBI(this));
    mRttTextHandler.initialize(mRttTextStream);
  }
  
  private void createWakeLock(Context paramContext)
  {
    mPartialWakeLock = ((PowerManager)paramContext.getSystemService("power")).newWakeLock(1, "ImsPhoneConnection");
  }
  
  static boolean equalsBaseDialString(String paramString1, String paramString2)
  {
    boolean bool = false;
    if (paramString1 == null) {
      if (paramString2 != null) {}
    }
    for (;;)
    {
      bool = true;
      break;
      do
      {
        break;
      } while ((paramString2 == null) || (!paramString1.startsWith(paramString2)));
    }
    return bool;
  }
  
  static boolean equalsHandlesNulls(Object paramObject1, Object paramObject2)
  {
    boolean bool;
    if (paramObject1 == null)
    {
      if (paramObject2 == null) {
        bool = true;
      } else {
        bool = false;
      }
    }
    else {
      bool = paramObject1.equals(paramObject2);
    }
    return bool;
  }
  
  private void fetchDtmfToneDelay(Phone paramPhone)
  {
    paramPhone = ((CarrierConfigManager)paramPhone.getContext().getSystemService("carrier_config")).getConfigForSubId(paramPhone.getSubId());
    if (paramPhone != null) {
      mDtmfToneDelay = paramPhone.getInt("ims_dtmf_tone_delay_int");
    }
  }
  
  private int getAudioQualityFromCallProfile(ImsCallProfile paramImsCallProfile1, ImsCallProfile paramImsCallProfile2)
  {
    int i = 1;
    if ((paramImsCallProfile1 != null) && (paramImsCallProfile2 != null) && (mMediaProfile != null))
    {
      int j = mMediaProfile.mAudioQuality;
      int k = 0;
      if ((j != 18) && (mMediaProfile.mAudioQuality != 19) && (mMediaProfile.mAudioQuality != 20)) {
        m = 0;
      } else {
        m = 1;
      }
      if ((mMediaProfile.mAudioQuality != 2) && (mMediaProfile.mAudioQuality != 6))
      {
        j = k;
        if (m == 0) {}
      }
      else
      {
        j = k;
        if (mRestrictCause == 0) {
          j = 1;
        }
      }
      int m = i;
      if (j != 0) {
        m = 2;
      }
      return m;
    }
    return 1;
  }
  
  private void processNextPostDialChar()
  {
    if (mPostDialState == Connection.PostDialState.CANCELLED) {
      return;
    }
    int i;
    char c2;
    if ((mPostDialString != null) && (mPostDialString.length() > mNextPostDialChar))
    {
      setPostDialState(Connection.PostDialState.STARTED);
      localObject = mPostDialString;
      i = mNextPostDialChar;
      mNextPostDialChar = (i + 1);
      char c1 = ((String)localObject).charAt(i);
      c2 = c1;
      if (!processPostDialChar(c1))
      {
        mHandler.obtainMessage(3).sendToTarget();
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("processNextPostDialChar: c=");
        ((StringBuilder)localObject).append(c1);
        ((StringBuilder)localObject).append(" isn't valid!");
        Rlog.e("ImsPhoneConnection", ((StringBuilder)localObject).toString());
      }
    }
    else
    {
      setPostDialState(Connection.PostDialState.COMPLETE);
      i = 0;
      c2 = i;
    }
    notifyPostDialListenersNextChar(c2);
    Object localObject = mOwner.mPhone.getPostDialHandler();
    if (localObject != null)
    {
      localObject = ((Registrant)localObject).messageForRegistrant();
      if (localObject != null)
      {
        Connection.PostDialState localPostDialState = mPostDialState;
        AsyncResult localAsyncResult = AsyncResult.forMessage((Message)localObject);
        result = this;
        userObj = localPostDialState;
        arg1 = c2;
        ((Message)localObject).sendToTarget();
      }
    }
  }
  
  private boolean processPostDialChar(char paramChar)
  {
    if (PhoneNumberUtils.is12Key(paramChar))
    {
      Message localMessage = mHandler.obtainMessage(1);
      replyTo = mHandlerMessenger;
      mOwner.sendDtmf(paramChar, localMessage);
    }
    else if (paramChar == ',')
    {
      mHandler.sendMessageDelayed(mHandler.obtainMessage(2), 3000L);
    }
    else if (paramChar == ';')
    {
      setPostDialState(Connection.PostDialState.WAIT);
    }
    else
    {
      if (paramChar != 'N') {
        break label95;
      }
      setPostDialState(Connection.PostDialState.WILD);
    }
    return true;
    label95:
    return false;
  }
  
  private void setPostDialState(Connection.PostDialState paramPostDialState)
  {
    if ((mPostDialState != Connection.PostDialState.STARTED) && (paramPostDialState == Connection.PostDialState.STARTED))
    {
      acquireWakeLock();
      Message localMessage = mHandler.obtainMessage(4);
      mHandler.sendMessageDelayed(localMessage, 60000L);
    }
    else if ((mPostDialState == Connection.PostDialState.STARTED) && (paramPostDialState != Connection.PostDialState.STARTED))
    {
      mHandler.removeMessages(4);
      releaseWakeLock();
    }
    mPostDialState = paramPostDialState;
    notifyPostDialListeners();
  }
  
  private void updateVideoState(int paramInt)
  {
    if (mImsVideoCallProviderWrapper != null) {
      mImsVideoCallProviderWrapper.onVideoStateChanged(paramInt);
    }
    setVideoState(paramInt);
  }
  
  private void updateWifiStateFromExtras(Bundle paramBundle)
  {
    if ((paramBundle.containsKey("CallRadioTech")) || (paramBundle.containsKey("callRadioTech")))
    {
      paramBundle = getImsCall();
      boolean bool = false;
      if (paramBundle != null) {
        bool = paramBundle.isWifiCall();
      }
      if (isWifi() != bool) {
        setWifi(bool);
      }
    }
  }
  
  public void cancelPostDial()
  {
    setPostDialState(Connection.PostDialState.CANCELLED);
  }
  
  public void changeParent(ImsPhoneCall paramImsPhoneCall)
  {
    mParent = paramImsPhoneCall;
  }
  
  public void changeToPausedState()
  {
    int i = getVideoState() | 0x4;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ImsPhoneConnection: changeToPausedState - setting paused bit; newVideoState=");
    localStringBuilder.append(VideoProfile.videoStateToString(i));
    Rlog.i("ImsPhoneConnection", localStringBuilder.toString());
    updateVideoState(i);
    mShouldIgnoreVideoStateChanges = true;
  }
  
  public void changeToUnPausedState()
  {
    int i = getVideoState() & 0xFFFFFFFB;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ImsPhoneConnection: changeToUnPausedState - unsetting paused bit; newVideoState=");
    localStringBuilder.append(VideoProfile.videoStateToString(i));
    Rlog.i("ImsPhoneConnection", localStringBuilder.toString());
    updateVideoState(i);
    mShouldIgnoreVideoStateChanges = false;
  }
  
  public void deflect(String paramString)
    throws CallStateException
  {
    if (mParent.getState().isRinging()) {
      try
      {
        if (mImsCall != null)
        {
          mImsCall.deflect(paramString);
          return;
        }
        paramString = new com/android/internal/telephony/CallStateException;
        paramString.<init>("no valid ims call to deflect");
        throw paramString;
      }
      catch (ImsException paramString)
      {
        throw new CallStateException("cannot deflect call");
      }
    }
    throw new CallStateException("phone not ringing");
  }
  
  public void dispose() {}
  
  protected void finalize()
  {
    releaseWakeLock();
  }
  
  public ImsPhoneCall getCall()
  {
    return mParent;
  }
  
  public long getConferenceConnectTime()
  {
    return mConferenceConnectTime;
  }
  
  public long getDisconnectTime()
  {
    return mDisconnectTime;
  }
  
  public long getHoldDurationMillis()
  {
    if (getState() != Call.State.HOLDING) {
      return 0L;
    }
    return SystemClock.elapsedRealtime() - mHoldingStartTime;
  }
  
  public long getHoldingStartTime()
  {
    return mHoldingStartTime;
  }
  
  public ImsCall getImsCall()
  {
    try
    {
      ImsCall localImsCall = mImsCall;
      return localImsCall;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public int getNumberPresentation()
  {
    return mNumberPresentation;
  }
  
  public Connection getOrigConnection()
  {
    return null;
  }
  
  public String getOrigDialString()
  {
    return mDialString;
  }
  
  public ImsPhoneCallTracker getOwner()
  {
    return mOwner;
  }
  
  public int getPreciseDisconnectCause()
  {
    return mPreciseDisconnectCause;
  }
  
  public Call.State getState()
  {
    if (mDisconnected) {
      return Call.State.DISCONNECTED;
    }
    return super.getState();
  }
  
  public UUSInfo getUUSInfo()
  {
    return mUusInfo;
  }
  
  public String getVendorDisconnectCause()
  {
    return null;
  }
  
  public void handleMergeComplete()
  {
    mIsMergeInProcess = false;
    onConnectionEvent("android.telecom.event.MERGE_COMPLETE", null);
  }
  
  public void handleMergeStart()
  {
    mIsMergeInProcess = true;
    onConnectionEvent("android.telecom.event.MERGE_START", null);
  }
  
  public void hangup()
    throws CallStateException
  {
    if (!mDisconnected)
    {
      mOwner.hangup(this);
      return;
    }
    throw new CallStateException("disconnected");
  }
  
  public boolean hasRttTextStream()
  {
    boolean bool;
    if (mRttTextStream != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isConferenceHost()
  {
    try
    {
      if (mImsCall != null)
      {
        bool = mImsCall.isConferenceHost();
        if (bool)
        {
          bool = true;
          break label28;
        }
      }
      boolean bool = false;
      label28:
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  protected boolean isEmergency()
  {
    return mIsEmergency;
  }
  
  public boolean isMemberOfPeerConference()
  {
    return isConferenceHost() ^ true;
  }
  
  public boolean isMultiparty()
  {
    try
    {
      if (mImsCall != null)
      {
        bool = mImsCall.isMultiparty();
        if (bool)
        {
          bool = true;
          break label28;
        }
      }
      boolean bool = false;
      label28:
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public boolean isRttEnabledForCall()
  {
    return mIsRttEnabledForCall;
  }
  
  public void onCallSessionPropertyChanged(int paramInt)
  {
    Bundle localBundle = new Bundle();
    localBundle.putInt("android.telecom.extra.EXTRA_CALL_PROPERTY", paramInt);
    onConnectionEvent("android.telecom.event.EVENT_CALL_PROPERTY_CHANGED", localBundle);
  }
  
  void onConnectedInOrOut()
  {
    mConnectTime = System.currentTimeMillis();
    mConnectTimeReal = SystemClock.elapsedRealtime();
    mDuration = 0L;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("onConnectedInOrOut: connectTime=");
    localStringBuilder.append(mConnectTime);
    Rlog.d("ImsPhoneConnection", localStringBuilder.toString());
    if (!mIsIncoming) {
      processNextPostDialChar();
    }
    releaseWakeLock();
  }
  
  public boolean onDisconnect()
  {
    boolean bool1 = false;
    boolean bool2 = false;
    if (!mDisconnected)
    {
      mDisconnectTime = System.currentTimeMillis();
      mDuration = (SystemClock.elapsedRealtime() - mConnectTimeReal);
      mDisconnected = true;
      Rlog.d("ImsPhoneConnection", "onDisconnect: notifyDisconnect");
      mOwner.mPhone.notifyDisconnect(this);
      notifyDisconnect(mCause);
      if (mParent != null) {}
      for (bool1 = mParent.connectionDisconnected(this);; bool1 = bool2)
      {
        break;
        Rlog.d("ImsPhoneConnection", "onDisconnect: no parent");
      }
      try
      {
        if (mImsCall != null) {
          mImsCall.close();
        }
        mImsCall = null;
      }
      finally {}
    }
    releaseWakeLock();
    return bool1;
  }
  
  public boolean onDisconnect(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("onDisconnect: cause=");
    localStringBuilder.append(paramInt);
    Rlog.d("ImsPhoneConnection", localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("onDisconnect(): conn = ");
    localStringBuilder.append(this);
    Rlog.d("ImsPhoneConnection", localStringBuilder.toString());
    if ((mCause != 3) || (paramInt == 16)) {
      mCause = paramInt;
    }
    return onDisconnect();
  }
  
  public void onDisconnectConferenceParticipant(Uri paramUri)
  {
    ImsCall localImsCall = getImsCall();
    if (localImsCall == null) {
      return;
    }
    try
    {
      localImsCall.removeParticipants(new String[] { paramUri.toString() });
    }
    catch (ImsException localImsException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onDisconnectConferenceParticipant: no session in place. Failed to disconnect endpoint = ");
      localStringBuilder.append(paramUri);
      Rlog.e("ImsPhoneConnection", localStringBuilder.toString());
    }
  }
  
  void onHangupLocal()
  {
    mCause = 3;
  }
  
  public void onReceiveSessionModifyResponse(int paramInt, VideoProfile paramVideoProfile1, VideoProfile paramVideoProfile2)
  {
    if ((paramInt == 1) && (mShouldIgnoreVideoStateChanges))
    {
      int i = getVideoState();
      paramInt = paramVideoProfile2.getVideoState();
      int j = (i ^ paramInt) & 0x3;
      if (j == 0) {
        return;
      }
      paramInt = i & j & i | j & paramInt;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onReceiveSessionModifyResponse : received ");
      localStringBuilder.append(VideoProfile.videoStateToString(paramVideoProfile1.getVideoState()));
      localStringBuilder.append(" / ");
      localStringBuilder.append(VideoProfile.videoStateToString(paramVideoProfile2.getVideoState()));
      localStringBuilder.append(" while paused ; sending new videoState = ");
      localStringBuilder.append(VideoProfile.videoStateToString(paramInt));
      Rlog.d("ImsPhoneConnection", localStringBuilder.toString());
      setVideoState(paramInt);
    }
  }
  
  public void onRttMessageReceived(String paramString)
  {
    try
    {
      if (mRttTextHandler == null)
      {
        Rlog.w("ImsPhoneConnection", "onRttMessageReceived: RTT text handler not available. Attempting to create one.");
        if (mRttTextStream == null)
        {
          Rlog.e("ImsPhoneConnection", "onRttMessageReceived: Unable to process incoming message. No textstream available");
          return;
        }
        createRttTextHandler();
      }
      mRttTextHandler.sendToInCall(paramString);
      return;
    }
    finally {}
  }
  
  void onStartedHolding()
  {
    mHoldingStartTime = SystemClock.elapsedRealtime();
  }
  
  public void pauseVideo(int paramInt)
  {
    if (mImsVideoCallProviderWrapper == null) {
      return;
    }
    mImsVideoCallProviderWrapper.pauseVideo(getVideoState(), paramInt);
  }
  
  public void proceedAfterWaitChar()
  {
    if (mPostDialState != Connection.PostDialState.WAIT)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("ImsPhoneConnection.proceedAfterWaitChar(): Expected getPostDialState() to be WAIT but was ");
      localStringBuilder.append(mPostDialState);
      Rlog.w("ImsPhoneConnection", localStringBuilder.toString());
      return;
    }
    setPostDialState(Connection.PostDialState.STARTED);
    processNextPostDialChar();
  }
  
  public void proceedAfterWildChar(String paramString)
  {
    if (mPostDialState != Connection.PostDialState.WILD)
    {
      paramString = new StringBuilder();
      paramString.append("ImsPhoneConnection.proceedAfterWaitChar(): Expected getPostDialState() to be WILD but was ");
      paramString.append(mPostDialState);
      Rlog.w("ImsPhoneConnection", paramString.toString());
      return;
    }
    setPostDialState(Connection.PostDialState.STARTED);
    paramString = new StringBuilder(paramString);
    paramString.append(mPostDialString.substring(mNextPostDialChar));
    mPostDialString = paramString.toString();
    mNextPostDialChar = 0;
    paramString = new StringBuilder();
    paramString.append("proceedAfterWildChar: new postDialString is ");
    paramString.append(mPostDialString);
    Rlog.d("ImsPhoneConnection", paramString.toString());
    processNextPostDialChar();
  }
  
  void releaseWakeLock()
  {
    if (mPartialWakeLock != null) {
      synchronized (mPartialWakeLock)
      {
        if (mPartialWakeLock.isHeld())
        {
          Rlog.d("ImsPhoneConnection", "releaseWakeLock");
          mPartialWakeLock.release();
        }
      }
    }
  }
  
  public void resumeVideo(int paramInt)
  {
    if (mImsVideoCallProviderWrapper == null) {
      return;
    }
    mImsVideoCallProviderWrapper.resumeVideo(getVideoState(), paramInt);
  }
  
  public void sendRttModifyRequest(Connection.RttTextStream paramRttTextStream)
  {
    getImsCall().sendRttModifyRequest();
    setCurrentRttTextStream(paramRttTextStream);
  }
  
  public void sendRttModifyResponse(Connection.RttTextStream paramRttTextStream)
  {
    boolean bool;
    if (paramRttTextStream != null) {
      bool = true;
    } else {
      bool = false;
    }
    getImsCall().sendRttModifyResponse(bool);
    if (bool) {
      setCurrentRttTextStream(paramRttTextStream);
    } else {
      Rlog.e("ImsPhoneConnection", "sendRttModifyResponse: foreground call has no connections");
    }
  }
  
  public void separate()
    throws CallStateException
  {
    throw new CallStateException("not supported");
  }
  
  public void setConferenceConnectTime(long paramLong)
  {
    mConferenceConnectTime = paramLong;
  }
  
  public void setCurrentRttTextStream(Connection.RttTextStream paramRttTextStream)
  {
    try
    {
      mRttTextStream = paramRttTextStream;
      if ((mRttTextHandler == null) && (mIsRttEnabledForCall))
      {
        Rlog.i("ImsPhoneConnection", "setCurrentRttTextStream: Creating a text handler");
        createRttTextHandler();
      }
      return;
    }
    finally {}
  }
  
  public void setDisconnectCause(int paramInt)
  {
    mCause = paramInt;
  }
  
  public void setImsCall(ImsCall paramImsCall)
  {
    try
    {
      mImsCall = paramImsCall;
      return;
    }
    finally
    {
      paramImsCall = finally;
      throw paramImsCall;
    }
  }
  
  public void setPreciseDisconnectCause(int paramInt)
  {
    mPreciseDisconnectCause = paramInt;
  }
  
  public void setVideoEnabled(boolean paramBoolean)
  {
    mIsVideoEnabled = paramBoolean;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("setVideoEnabled: mIsVideoEnabled = ");
    localStringBuilder.append(mIsVideoEnabled);
    localStringBuilder.append("; updating local video availability.");
    Rlog.i("ImsPhoneConnection", localStringBuilder.toString());
    updateMediaCapabilities(getImsCall());
  }
  
  public void setVideoProvider(Connection.VideoProvider paramVideoProvider)
  {
    super.setVideoProvider(paramVideoProvider);
    if ((paramVideoProvider instanceof ImsVideoCallProviderWrapper)) {
      mImsVideoCallProviderWrapper = ((ImsVideoCallProviderWrapper)paramVideoProvider);
    }
  }
  
  public void startRttTextProcessing()
  {
    try
    {
      if (mRttTextStream == null)
      {
        Rlog.w("ImsPhoneConnection", "startRttTextProcessing: no RTT text stream. Ignoring.");
        return;
      }
      if (mRttTextHandler != null)
      {
        Rlog.w("ImsPhoneConnection", "startRttTextProcessing: RTT text handler already exists");
        return;
      }
      createRttTextHandler();
      return;
    }
    finally {}
  }
  
  /* Error */
  public String toString()
  {
    // Byte code:
    //   0: new 292	java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial 293	java/lang/StringBuilder:<init>	()V
    //   7: astore_1
    //   8: aload_1
    //   9: ldc_w 892
    //   12: invokevirtual 299	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   15: pop
    //   16: aload_1
    //   17: aload_0
    //   18: invokestatic 896	java/lang/System:identityHashCode	(Ljava/lang/Object;)I
    //   21: invokevirtual 394	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   24: pop
    //   25: aload_1
    //   26: ldc_w 898
    //   29: invokevirtual 299	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   32: pop
    //   33: aload_1
    //   34: aload_0
    //   35: invokevirtual 901	com/android/internal/telephony/imsphone/ImsPhoneConnection:getTelecomCallId	()Ljava/lang/String;
    //   38: invokevirtual 299	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   41: pop
    //   42: aload_1
    //   43: ldc_w 903
    //   46: invokevirtual 299	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   49: pop
    //   50: aload_1
    //   51: ldc 26
    //   53: aload_0
    //   54: invokevirtual 906	com/android/internal/telephony/imsphone/ImsPhoneConnection:getAddress	()Ljava/lang/String;
    //   57: invokestatic 910	android/telephony/Rlog:pii	(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
    //   60: invokevirtual 299	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   63: pop
    //   64: aload_1
    //   65: ldc_w 912
    //   68: invokevirtual 299	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   71: pop
    //   72: aload_0
    //   73: monitorenter
    //   74: aload_0
    //   75: getfield 136	com/android/internal/telephony/imsphone/ImsPhoneConnection:mImsCall	Lcom/android/ims/ImsCall;
    //   78: ifnonnull +22 -> 100
    //   81: aload_1
    //   82: ldc_w 914
    //   85: invokevirtual 299	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   88: pop
    //   89: aload_1
    //   90: ldc_w 916
    //   93: invokevirtual 299	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   96: pop
    //   97: goto +52 -> 149
    //   100: aload_1
    //   101: aload_0
    //   102: getfield 136	com/android/internal/telephony/imsphone/ImsPhoneConnection:mImsCall	Lcom/android/ims/ImsCall;
    //   105: invokevirtual 302	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   108: pop
    //   109: aload_1
    //   110: ldc_w 918
    //   113: invokevirtual 299	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   116: pop
    //   117: aload_1
    //   118: aload_0
    //   119: getfield 136	com/android/internal/telephony/imsphone/ImsPhoneConnection:mImsCall	Lcom/android/ims/ImsCall;
    //   122: invokevirtual 921	com/android/ims/ImsCall:getLocalCallProfile	()Landroid/telephony/ims/ImsCallProfile;
    //   125: invokevirtual 302	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   128: pop
    //   129: aload_1
    //   130: ldc_w 923
    //   133: invokevirtual 299	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   136: pop
    //   137: aload_1
    //   138: aload_0
    //   139: getfield 136	com/android/internal/telephony/imsphone/ImsPhoneConnection:mImsCall	Lcom/android/ims/ImsCall;
    //   142: invokevirtual 926	com/android/ims/ImsCall:getRemoteCallProfile	()Landroid/telephony/ims/ImsCallProfile;
    //   145: invokevirtual 302	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   148: pop
    //   149: goto +43 -> 192
    //   152: astore_1
    //   153: goto +106 -> 259
    //   156: astore_2
    //   157: new 292	java/lang/StringBuilder
    //   160: astore_3
    //   161: aload_3
    //   162: invokespecial 293	java/lang/StringBuilder:<init>	()V
    //   165: aload_3
    //   166: ldc_w 928
    //   169: invokevirtual 299	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   172: pop
    //   173: aload_3
    //   174: aload_2
    //   175: invokevirtual 929	java/lang/Exception:toString	()Ljava/lang/String;
    //   178: invokevirtual 299	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   181: pop
    //   182: ldc 26
    //   184: aload_3
    //   185: invokevirtual 306	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   188: invokestatic 281	android/telephony/Rlog:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   191: pop
    //   192: aload_0
    //   193: monitorexit
    //   194: aload_1
    //   195: ldc_w 931
    //   198: invokevirtual 299	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   201: pop
    //   202: aload_1
    //   203: aload_0
    //   204: getfield 183	com/android/internal/telephony/imsphone/ImsPhoneConnection:mIsIncoming	Z
    //   207: invokevirtual 877	java/lang/StringBuilder:append	(Z)Ljava/lang/StringBuilder;
    //   210: pop
    //   211: ldc_w 933
    //   214: ldc_w 935
    //   217: ldc_w 262
    //   220: invokestatic 940	android/os/SystemProperties:get	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   223: invokevirtual 943	java/lang/String:equalsIgnoreCase	(Ljava/lang/String;)Z
    //   226: ifeq +20 -> 246
    //   229: aload_1
    //   230: ldc_w 945
    //   233: invokevirtual 299	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   236: pop
    //   237: aload_1
    //   238: aload_0
    //   239: getfield 153	com/android/internal/telephony/imsphone/ImsPhoneConnection:mAddress	Ljava/lang/String;
    //   242: invokevirtual 299	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   245: pop
    //   246: aload_1
    //   247: ldc_w 947
    //   250: invokevirtual 299	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   253: pop
    //   254: aload_1
    //   255: invokevirtual 306	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   258: areturn
    //   259: aload_0
    //   260: monitorexit
    //   261: aload_1
    //   262: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	263	0	this	ImsPhoneConnection
    //   7	131	1	localStringBuilder1	StringBuilder
    //   152	110	1	localObject	Object
    //   156	19	2	localException	Exception
    //   160	25	3	localStringBuilder2	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   74	97	152	finally
    //   100	149	152	finally
    //   157	192	152	finally
    //   192	194	152	finally
    //   259	261	152	finally
    //   74	97	156	java/lang/Exception
    //   100	149	156	java/lang/Exception
  }
  
  public boolean update(ImsCall paramImsCall, Call.State paramState)
  {
    Call.State localState = Call.State.ACTIVE;
    boolean bool1 = false;
    if (paramState == localState)
    {
      if (paramImsCall.isPendingHold())
      {
        Rlog.w("ImsPhoneConnection", "update : state is ACTIVE, but call is pending hold, skipping");
        return false;
      }
      if ((mParent.getState().isRinging()) || (mParent.getState().isDialing())) {
        onConnectedInOrOut();
      }
      if ((mParent.getState().isRinging()) || (mParent == mOwner.mBackgroundCall))
      {
        mParent.detach(this);
        mParent = mOwner.mForegroundCall;
        mParent.attach(this);
      }
    }
    else if (paramState == Call.State.HOLDING)
    {
      onStartedHolding();
    }
    boolean bool2 = mParent.update(this, paramImsCall, paramState);
    boolean bool3 = updateAddressDisplay(paramImsCall);
    boolean bool4 = updateMediaCapabilities(paramImsCall);
    boolean bool5 = updateExtras(paramImsCall);
    if ((!bool2) && (!bool3) && (!bool4) && (!bool5)) {
      break label190;
    }
    bool1 = true;
    label190:
    return bool1;
  }
  
  public boolean updateAddressDisplay(ImsCall paramImsCall)
  {
    int i = 0;
    if (paramImsCall == null) {
      return false;
    }
    int j = 1;
    int k = 1;
    boolean bool1 = false;
    boolean bool2 = false;
    paramImsCall = paramImsCall.getCallProfile();
    boolean bool3 = bool1;
    if (paramImsCall != null)
    {
      String str1 = paramImsCall.getCallExtra("oi");
      String str2 = paramImsCall.getCallExtra("cna");
      int m = ImsCallProfile.OIRToPresentation(paramImsCall.getCallExtraInt("oir"));
      int n = ImsCallProfile.OIRToPresentation(paramImsCall.getCallExtraInt("cnap"));
      paramImsCall = NumberRecords.iterator();
      int i1;
      for (;;)
      {
        i1 = k;
        if (!paramImsCall.hasNext()) {
          break;
        }
        if (str1.equals((String)paramImsCall.next()))
        {
          i1 = 0;
          break;
        }
      }
      if (i1 != 0)
      {
        paramImsCall = str2.toLowerCase();
        if ((str1.equals("")) && (paramImsCall.equals("anonymous")))
        {
          i1 = 1;
        }
        else
        {
          i1 = j;
          if (str1.equals("")) {
            i1 = 0;
          }
        }
        if (i1 != 0) {
          NumberRecords.add(str1);
        }
        for (i1 = i; i1 < NumberRecords.size(); i1++)
        {
          Object localObject = (String)NumberRecords.get(i1);
          paramImsCall = (ImsCall)localObject;
          if (((String)localObject).length() > 4)
          {
            paramImsCall = new StringBuilder();
            paramImsCall.append("******");
            paramImsCall.append(((String)localObject).substring(((String)localObject).length() - 4, ((String)localObject).length()));
            paramImsCall = paramImsCall.toString();
          }
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("NumberRecords [");
          ((StringBuilder)localObject).append(i1);
          ((StringBuilder)localObject).append("] = ");
          ((StringBuilder)localObject).append(paramImsCall);
          Rlog.d("ImsPhoneConnection", ((StringBuilder)localObject).toString());
        }
      }
      bool3 = bool1;
      if (isIncoming())
      {
        paramImsCall = new StringBuilder();
        paramImsCall.append("updateAddressDisplay: callId = ");
        paramImsCall.append(getTelecomCallId());
        paramImsCall.append(" address = ");
        paramImsCall.append(Rlog.pii("ImsPhoneConnection", str1));
        paramImsCall.append(" name = ");
        paramImsCall.append(Rlog.pii("ImsPhoneConnection", str2));
        paramImsCall.append(" nump = ");
        paramImsCall.append(m);
        paramImsCall.append(" namep = ");
        paramImsCall.append(n);
        Rlog.d("ImsPhoneConnection", paramImsCall.toString());
        bool3 = bool1;
        if (!mIsMergeInProcess)
        {
          bool3 = bool2;
          if (!equalsBaseDialString(mAddress, str1))
          {
            mAddress = str1;
            bool3 = true;
          }
          if (TextUtils.isEmpty(str2))
          {
            if (!TextUtils.isEmpty(mCnapName))
            {
              mCnapName = "";
              bool3 = true;
            }
          }
          else if (!str2.equals(mCnapName))
          {
            mCnapName = str2;
            bool3 = true;
          }
          if (mNumberPresentation != m)
          {
            mNumberPresentation = m;
            bool3 = true;
          }
          if (mCnapNamePresentation != n)
          {
            mCnapNamePresentation = n;
            bool3 = true;
          }
        }
      }
    }
    return bool3;
  }
  
  boolean updateExtras(ImsCall paramImsCall)
  {
    if (paramImsCall == null) {
      return false;
    }
    paramImsCall = paramImsCall.getCallProfile();
    if (paramImsCall != null) {
      paramImsCall = mCallExtras;
    } else {
      paramImsCall = null;
    }
    if (paramImsCall == null) {
      Rlog.d("ImsPhoneConnection", "Call profile extras are null.");
    }
    boolean bool = areBundlesEqual(paramImsCall, mExtras) ^ true;
    if (bool)
    {
      updateWifiStateFromExtras(paramImsCall);
      mExtras.clear();
      mExtras.putAll(paramImsCall);
      setConnectionExtras(mExtras);
    }
    return bool;
  }
  
  public boolean updateMediaCapabilities(ImsCall paramImsCall)
  {
    if (paramImsCall == null) {
      return false;
    }
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    boolean bool4 = false;
    boolean bool5 = bool3;
    try
    {
      ImsCallProfile localImsCallProfile = paramImsCall.getCallProfile();
      bool5 = bool3;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      bool5 = bool3;
      localStringBuilder.<init>();
      bool5 = bool3;
      localStringBuilder.append("updateMediaCapabilities(): imsCall = ");
      bool5 = bool3;
      localStringBuilder.append(paramImsCall);
      bool5 = bool3;
      localStringBuilder.append(", negotiatedCallProfile = ");
      bool5 = bool3;
      localStringBuilder.append(localImsCallProfile);
      bool5 = bool3;
      Rlog.d("ImsPhoneConnection", localStringBuilder.toString());
      if (localImsCallProfile != null)
      {
        bool5 = bool3;
        i = getVideoState();
        bool5 = bool3;
        j = ImsCallProfile.getVideoStateFromImsCallProfile(localImsCallProfile);
        bool5 = bool3;
        localStringBuilder = new java/lang/StringBuilder;
        bool5 = bool3;
        localStringBuilder.<init>();
        bool5 = bool3;
        localStringBuilder.append("updateMediaCapabilities(): oldVideoState = ");
        bool5 = bool3;
        localStringBuilder.append(i);
        bool5 = bool3;
        localStringBuilder.append(", newVideoState = ");
        bool5 = bool3;
        localStringBuilder.append(j);
        bool5 = bool3;
        Rlog.d("ImsPhoneConnection", localStringBuilder.toString());
        if (i != j)
        {
          bool5 = bool3;
          if (VideoProfile.isPaused(i))
          {
            bool5 = bool3;
            if (!VideoProfile.isPaused(j))
            {
              bool5 = bool3;
              mShouldIgnoreVideoStateChanges = false;
            }
          }
          bool5 = bool3;
          if (!mShouldIgnoreVideoStateChanges)
          {
            bool5 = bool3;
            updateVideoState(j);
            bool2 = true;
          }
          else
          {
            bool5 = bool3;
            Rlog.d("ImsPhoneConnection", "updateMediaCapabilities - ignoring video state change due to paused state.");
            bool2 = bool4;
          }
          bool1 = bool2;
          bool5 = bool2;
          if (!VideoProfile.isPaused(i))
          {
            bool1 = bool2;
            bool5 = bool2;
            if (VideoProfile.isPaused(j))
            {
              bool5 = bool2;
              mShouldIgnoreVideoStateChanges = true;
              bool1 = bool2;
            }
          }
        }
        bool2 = bool1;
        bool5 = bool1;
        if (mMediaProfile != null)
        {
          bool5 = bool1;
          mIsRttEnabledForCall = mMediaProfile.isRttCall();
          bool5 = bool1;
          if (mIsRttEnabledForCall)
          {
            bool5 = bool1;
            if (mRttTextHandler == null)
            {
              bool5 = bool1;
              localStringBuilder = new java/lang/StringBuilder;
              bool5 = bool1;
              localStringBuilder.<init>();
              bool5 = bool1;
              localStringBuilder.append("updateMediaCapabilities -- turning RTT on, profile=");
              bool5 = bool1;
              localStringBuilder.append(localImsCallProfile);
              bool5 = bool1;
              Rlog.d("ImsPhoneConnection", localStringBuilder.toString());
              bool5 = bool1;
              startRttTextProcessing();
              bool5 = bool1;
              onRttInitiated();
              bool2 = true;
              break label559;
            }
          }
          bool2 = bool1;
          bool5 = bool1;
          if (!mIsRttEnabledForCall)
          {
            bool2 = bool1;
            bool5 = bool1;
            if (mRttTextHandler != null)
            {
              bool5 = bool1;
              localStringBuilder = new java/lang/StringBuilder;
              bool5 = bool1;
              localStringBuilder.<init>();
              bool5 = bool1;
              localStringBuilder.append("updateMediaCapabilities -- turning RTT off, profile=");
              bool5 = bool1;
              localStringBuilder.append(localImsCallProfile);
              bool5 = bool1;
              Rlog.d("ImsPhoneConnection", localStringBuilder.toString());
              bool5 = bool1;
              mRttTextHandler.tearDown();
              bool5 = bool1;
              mRttTextHandler = null;
              bool5 = bool1;
              onRttTerminated();
              bool2 = true;
            }
          }
        }
      }
      label559:
      bool5 = bool2;
      int j = getConnectionCapabilities();
      bool5 = bool2;
      if (mOwner.isCarrierDowngradeOfVtCallSupported())
      {
        bool5 = bool2;
        i = addCapability(j, 3);
      }
      else
      {
        bool5 = bool2;
        i = removeCapability(j, 3);
      }
      bool5 = bool2;
      localImsCallProfile = paramImsCall.getLocalCallProfile();
      bool5 = bool2;
      localStringBuilder = new java/lang/StringBuilder;
      bool5 = bool2;
      localStringBuilder.<init>();
      bool5 = bool2;
      localStringBuilder.append("update localCallProfile=");
      bool5 = bool2;
      localStringBuilder.append(localImsCallProfile);
      bool5 = bool2;
      Rlog.v("ImsPhoneConnection", localStringBuilder.toString());
      j = i;
      if (localImsCallProfile != null)
      {
        bool5 = bool2;
        j = applyLocalCallCapabilities(localImsCallProfile, i);
      }
      bool5 = bool2;
      paramImsCall = paramImsCall.getRemoteCallProfile();
      bool5 = bool2;
      localStringBuilder = new java/lang/StringBuilder;
      bool5 = bool2;
      localStringBuilder.<init>();
      bool5 = bool2;
      localStringBuilder.append("update remoteCallProfile=");
      bool5 = bool2;
      localStringBuilder.append(paramImsCall);
      bool5 = bool2;
      Rlog.v("ImsPhoneConnection", localStringBuilder.toString());
      int i = j;
      if (paramImsCall != null)
      {
        bool5 = bool2;
        i = applyRemoteCallCapabilities(paramImsCall, j);
      }
      bool1 = bool2;
      bool5 = bool2;
      if (getConnectionCapabilities() != i)
      {
        bool5 = bool2;
        setConnectionCapabilities(i);
        bool1 = true;
      }
      bool5 = bool1;
      if (!mOwner.isViLteDataMetered())
      {
        bool5 = bool1;
        Rlog.v("ImsPhoneConnection", "data is not metered");
      }
      else
      {
        bool5 = bool1;
        if (mImsVideoCallProviderWrapper != null)
        {
          bool5 = bool1;
          mImsVideoCallProviderWrapper.setIsVideoEnabled(hasCapabilities(4));
        }
      }
      bool5 = bool1;
      j = getAudioQualityFromCallProfile(localImsCallProfile, paramImsCall);
      bool2 = bool1;
      bool5 = bool1;
      if (getAudioQuality() != j)
      {
        bool5 = bool1;
        setAudioQuality(j);
        bool2 = true;
      }
      bool5 = bool2;
    }
    catch (ImsException paramImsCall) {}
    return bool5;
  }
  
  public boolean wasVideoPausedFromSource(int paramInt)
  {
    if (mImsVideoCallProviderWrapper == null) {
      return false;
    }
    return mImsVideoCallProviderWrapper.wasVideoPausedFromSource(paramInt);
  }
  
  class MyHandler
    extends Handler
  {
    MyHandler(Looper paramLooper)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      switch (what)
      {
      default: 
        break;
      case 4: 
        releaseWakeLock();
        break;
      case 2: 
      case 3: 
      case 5: 
        ImsPhoneConnection.this.processNextPostDialChar();
        break;
      case 1: 
        mHandler.sendMessageDelayed(mHandler.obtainMessage(5), mDtmfToneDelay);
      }
    }
  }
}
