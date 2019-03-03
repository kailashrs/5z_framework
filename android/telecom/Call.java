package android.telecom;

import android.annotation.SystemApi;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor.AutoCloseInputStream;
import android.os.ParcelFileDescriptor.AutoCloseOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Call
{
  public static final String AVAILABLE_PHONE_ACCOUNTS = "selectPhoneAccountAccounts";
  public static final String EVENT_HANDOVER_COMPLETE = "android.telecom.event.HANDOVER_COMPLETE";
  public static final String EVENT_HANDOVER_FAILED = "android.telecom.event.HANDOVER_FAILED";
  public static final String EVENT_HANDOVER_SOURCE_DISCONNECTED = "android.telecom.event.HANDOVER_SOURCE_DISCONNECTED";
  public static final String EVENT_REQUEST_HANDOVER = "android.telecom.event.REQUEST_HANDOVER";
  public static final String EXTRA_HANDOVER_EXTRAS = "android.telecom.extra.HANDOVER_EXTRAS";
  public static final String EXTRA_HANDOVER_PHONE_ACCOUNT_HANDLE = "android.telecom.extra.HANDOVER_PHONE_ACCOUNT_HANDLE";
  public static final String EXTRA_HANDOVER_VIDEO_STATE = "android.telecom.extra.HANDOVER_VIDEO_STATE";
  public static final String EXTRA_LAST_EMERGENCY_CALLBACK_TIME_MILLIS = "android.telecom.extra.LAST_EMERGENCY_CALLBACK_TIME_MILLIS";
  public static final int STATE_ACTIVE = 4;
  public static final int STATE_CONNECTING = 9;
  public static final int STATE_DIALING = 1;
  public static final int STATE_DISCONNECTED = 7;
  public static final int STATE_DISCONNECTING = 10;
  public static final int STATE_HOLDING = 3;
  public static final int STATE_NEW = 0;
  @SystemApi
  @Deprecated
  public static final int STATE_PRE_DIAL_WAIT = 8;
  public static final int STATE_PULLING_CALL = 11;
  public static final int STATE_RINGING = 2;
  public static final int STATE_SELECT_PHONE_ACCOUNT = 8;
  private final List<CallbackRecord<Callback>> mCallbackRecords = new CopyOnWriteArrayList();
  private String mCallingPackage;
  private List<String> mCannedTextResponses = null;
  private final List<Call> mChildren = new ArrayList();
  private boolean mChildrenCached;
  private final List<String> mChildrenIds = new ArrayList();
  private final List<Call> mConferenceableCalls = new ArrayList();
  private Details mDetails;
  private Bundle mExtras;
  private final InCallAdapter mInCallAdapter;
  private String mParentId = null;
  private final Phone mPhone;
  private String mRemainingPostDialSequence;
  private RttCall mRttCall;
  private int mState;
  private int mTargetSdkVersion;
  private final String mTelecomCallId;
  private final List<Call> mUnmodifiableChildren = Collections.unmodifiableList(mChildren);
  private final List<Call> mUnmodifiableConferenceableCalls = Collections.unmodifiableList(mConferenceableCalls);
  private VideoCallImpl mVideoCallImpl;
  
  Call(Phone paramPhone, String paramString1, InCallAdapter paramInCallAdapter, int paramInt1, String paramString2, int paramInt2)
  {
    mPhone = paramPhone;
    mTelecomCallId = paramString1;
    mInCallAdapter = paramInCallAdapter;
    mState = paramInt1;
    mCallingPackage = paramString2;
    mTargetSdkVersion = paramInt2;
  }
  
  Call(Phone paramPhone, String paramString1, InCallAdapter paramInCallAdapter, String paramString2, int paramInt)
  {
    mPhone = paramPhone;
    mTelecomCallId = paramString1;
    mInCallAdapter = paramInCallAdapter;
    mState = 0;
    mCallingPackage = paramString2;
    mTargetSdkVersion = paramInt;
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
  
  private void fireCallDestroyed()
  {
    if (mCallbackRecords.isEmpty()) {
      mPhone.internalRemoveCall(this);
    }
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      final CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      final Callback localCallback = (Callback)localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new Runnable()
      {
        public void run()
        {
          int i = 0;
          Object localObject1 = null;
          try
          {
            localCallback.onCallDestroyed(jdField_this);
          }
          catch (RuntimeException localRuntimeException) {}
          synchronized (Call.this)
          {
            mCallbackRecords.remove(localCallbackRecord);
            if (mCallbackRecords.isEmpty()) {
              i = 1;
            }
            if (i != 0) {
              mPhone.internalRemoveCall(jdField_this);
            }
            if (localRuntimeException == null) {
              return;
            }
            throw localRuntimeException;
          }
        }
      });
    }
  }
  
  private void fireCannedTextResponsesLoaded(final List<String> paramList)
  {
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      final Callback localCallback = (Callback)localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new Runnable()
      {
        public void run()
        {
          localCallback.onCannedTextResponsesLoaded(jdField_this, paramList);
        }
      });
    }
  }
  
  private void fireChildrenChanged(final List<Call> paramList)
  {
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      final Callback localCallback = (Callback)localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new Runnable()
      {
        public void run()
        {
          localCallback.onChildrenChanged(jdField_this, paramList);
        }
      });
    }
  }
  
  private void fireConferenceableCallsChanged()
  {
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      final Callback localCallback = (Callback)localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new Runnable()
      {
        public void run()
        {
          localCallback.onConferenceableCallsChanged(jdField_this, mUnmodifiableConferenceableCalls);
        }
      });
    }
  }
  
  private void fireDetailsChanged(final Details paramDetails)
  {
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      final Callback localCallback = (Callback)localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new Runnable()
      {
        public void run()
        {
          localCallback.onDetailsChanged(jdField_this, paramDetails);
        }
      });
    }
  }
  
  private void fireOnConnectionEvent(final String paramString, final Bundle paramBundle)
  {
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      final Callback localCallback = (Callback)localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new Runnable()
      {
        public void run()
        {
          localCallback.onConnectionEvent(jdField_this, paramString, paramBundle);
        }
      });
    }
  }
  
  private void fireOnIsRttChanged(boolean paramBoolean, RttCall paramRttCall)
  {
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      Callback localCallback = (Callback)localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new _..Lambda.Call.5JdbCgV1DP_WhiljnHJKKAJdCu0(localCallback, this, paramBoolean, paramRttCall));
    }
  }
  
  private void fireOnRttModeChanged(int paramInt)
  {
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      Callback localCallback = (Callback)localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new _..Lambda.Call.qjo4awib5yVZC_4Qe_hhqUSk7ho(localCallback, this, paramInt));
    }
  }
  
  private void fireParentChanged(final Call paramCall)
  {
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      final Callback localCallback = (Callback)localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new Runnable()
      {
        public void run()
        {
          localCallback.onParentChanged(jdField_this, paramCall);
        }
      });
    }
  }
  
  private void firePostDialWait(final String paramString)
  {
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      final Callback localCallback = (Callback)localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new Runnable()
      {
        public void run()
        {
          localCallback.onPostDialWait(jdField_this, paramString);
        }
      });
    }
  }
  
  private void fireStateChanged(final int paramInt)
  {
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      final Callback localCallback = (Callback)localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new Runnable()
      {
        public void run()
        {
          localCallback.onStateChanged(jdField_this, paramInt);
        }
      });
    }
  }
  
  private void fireVideoCallChanged(final InCallService.VideoCall paramVideoCall)
  {
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      final Callback localCallback = (Callback)localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new Runnable()
      {
        public void run()
        {
          localCallback.onVideoCallChanged(jdField_this, paramVideoCall);
        }
      });
    }
  }
  
  private static String stateToString(int paramInt)
  {
    switch (paramInt)
    {
    case 5: 
    case 6: 
    default: 
      Log.w(Call.class, "Unknown state %d", new Object[] { Integer.valueOf(paramInt) });
      return "UNKNOWN";
    case 10: 
      return "DISCONNECTING";
    case 9: 
      return "CONNECTING";
    case 8: 
      return "SELECT_PHONE_ACCOUNT";
    case 7: 
      return "DISCONNECTED";
    case 4: 
      return "ACTIVE";
    case 3: 
      return "HOLDING";
    case 2: 
      return "RINGING";
    case 1: 
      return "DIALING";
    }
    return "NEW";
  }
  
  @SystemApi
  @Deprecated
  public void addListener(Listener paramListener)
  {
    registerCallback(paramListener);
  }
  
  public void answer(int paramInt)
  {
    mInCallAdapter.answerCall(mTelecomCallId, paramInt);
  }
  
  public void conference(Call paramCall)
  {
    if (paramCall != null) {
      mInCallAdapter.conference(mTelecomCallId, mTelecomCallId);
    }
  }
  
  public void deflect(Uri paramUri)
  {
    mInCallAdapter.deflectCall(mTelecomCallId, paramUri);
  }
  
  public void disconnect()
  {
    mInCallAdapter.disconnectCall(mTelecomCallId);
  }
  
  public List<String> getCannedTextResponses()
  {
    return mCannedTextResponses;
  }
  
  public List<Call> getChildren()
  {
    if (!mChildrenCached)
    {
      mChildrenCached = true;
      mChildren.clear();
      Iterator localIterator = mChildrenIds.iterator();
      while (localIterator.hasNext())
      {
        Object localObject = (String)localIterator.next();
        localObject = mPhone.internalGetCallByTelecomId((String)localObject);
        if (localObject == null) {
          mChildrenCached = false;
        } else {
          mChildren.add(localObject);
        }
      }
    }
    return mUnmodifiableChildren;
  }
  
  public List<Call> getConferenceableCalls()
  {
    return mUnmodifiableConferenceableCalls;
  }
  
  public Details getDetails()
  {
    return mDetails;
  }
  
  public Call getParent()
  {
    if (mParentId != null) {
      return mPhone.internalGetCallByTelecomId(mParentId);
    }
    return null;
  }
  
  public String getRemainingPostDialSequence()
  {
    return mRemainingPostDialSequence;
  }
  
  public RttCall getRttCall()
  {
    return mRttCall;
  }
  
  public int getState()
  {
    return mState;
  }
  
  public InCallService.VideoCall getVideoCall()
  {
    return mVideoCallImpl;
  }
  
  public void handoverTo(PhoneAccountHandle paramPhoneAccountHandle, int paramInt, Bundle paramBundle)
  {
    mInCallAdapter.handoverTo(mTelecomCallId, paramPhoneAccountHandle, paramInt, paramBundle);
  }
  
  public void hold()
  {
    mInCallAdapter.holdCall(mTelecomCallId);
  }
  
  final String internalGetCallId()
  {
    return mTelecomCallId;
  }
  
  final void internalOnConnectionEvent(String paramString, Bundle paramBundle)
  {
    fireOnConnectionEvent(paramString, paramBundle);
  }
  
  final void internalOnHandoverComplete()
  {
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      Callback localCallback = (Callback)localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new _..Lambda.Call.bt1B6cq3ylYqEtzOXnJWMeJ_ojc(localCallback, this));
    }
  }
  
  final void internalOnHandoverFailed(int paramInt)
  {
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      Callback localCallback = (Callback)localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new _..Lambda.Call.aPdcAxyKfpxcuraTjET8ce3xApc(localCallback, this, paramInt));
    }
  }
  
  final void internalOnRttInitiationFailure(int paramInt)
  {
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      Callback localCallback = (Callback)localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new _..Lambda.Call.JyYlHynNNc3DTrfrP5aXatJNft4(localCallback, this, paramInt));
    }
  }
  
  final void internalOnRttUpgradeRequest(int paramInt)
  {
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      Callback localCallback = (Callback)localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new _..Lambda.Call.hgXdGxKbb9IRpCeFrYieOwUuElE(localCallback, this, paramInt));
    }
  }
  
  final void internalSetDisconnected()
  {
    if (mState != 7)
    {
      mState = 7;
      fireStateChanged(mState);
      fireCallDestroyed();
    }
  }
  
  final void internalSetPostDialWait(String paramString)
  {
    mRemainingPostDialSequence = paramString;
    firePostDialWait(mRemainingPostDialSequence);
  }
  
  final void internalUpdate(ParcelableCall paramParcelableCall, Map<String, Call> paramMap)
  {
    Object localObject = Details.createFromParcelableCall(paramParcelableCall);
    boolean bool1 = Objects.equals(mDetails, localObject) ^ true;
    if (bool1) {
      mDetails = ((Details)localObject);
    }
    int i = 0;
    int j = i;
    if (mCannedTextResponses == null)
    {
      j = i;
      if (paramParcelableCall.getCannedSmsResponses() != null)
      {
        j = i;
        if (!paramParcelableCall.getCannedSmsResponses().isEmpty())
        {
          mCannedTextResponses = Collections.unmodifiableList(paramParcelableCall.getCannedSmsResponses());
          j = 1;
        }
      }
    }
    localObject = paramParcelableCall.getVideoCallImpl(mCallingPackage, mTargetSdkVersion);
    int k;
    if ((paramParcelableCall.isVideoCallProviderChanged()) && (!Objects.equals(mVideoCallImpl, localObject))) {
      k = 1;
    } else {
      k = 0;
    }
    if (k != 0) {
      mVideoCallImpl = ((VideoCallImpl)localObject);
    }
    if (mVideoCallImpl != null) {
      mVideoCallImpl.setVideoState(getDetails().getVideoState());
    }
    i = paramParcelableCall.getState();
    int m;
    if (mState != i) {
      m = 1;
    } else {
      m = 0;
    }
    if (m != 0) {
      mState = i;
    }
    localObject = paramParcelableCall.getParentCallId();
    boolean bool2 = Objects.equals(mParentId, localObject) ^ true;
    if (bool2) {
      mParentId = ((String)localObject);
    }
    boolean bool3 = Objects.equals(paramParcelableCall.getChildCallIds(), mChildrenIds) ^ true;
    if (bool3)
    {
      mChildrenIds.clear();
      mChildrenIds.addAll(paramParcelableCall.getChildCallIds());
      mChildrenCached = false;
    }
    localObject = paramParcelableCall.getConferenceableCallIds();
    ArrayList localArrayList = new ArrayList(((List)localObject).size());
    localObject = ((List)localObject).iterator();
    for (;;)
    {
      Map<String, Call> localMap = paramMap;
      if (!((Iterator)localObject).hasNext()) {
        break;
      }
      String str = (String)((Iterator)localObject).next();
      if (localMap.containsKey(str)) {
        localArrayList.add((Call)localMap.get(str));
      }
    }
    if (!Objects.equals(mConferenceableCalls, localArrayList))
    {
      mConferenceableCalls.clear();
      mConferenceableCalls.addAll(localArrayList);
      fireConferenceableCallsChanged();
    }
    if (paramParcelableCall.getIsRttCallChanged())
    {
      n = 0;
      i = 0;
      if (mDetails.hasProperty(1024))
      {
        paramParcelableCall = paramParcelableCall.getParcelableRttCall();
        localObject = new InputStreamReader(new ParcelFileDescriptor.AutoCloseInputStream(paramParcelableCall.getReceiveStream()), StandardCharsets.UTF_8);
        paramMap = new OutputStreamWriter(new ParcelFileDescriptor.AutoCloseOutputStream(paramParcelableCall.getTransmitStream()), StandardCharsets.UTF_8);
        paramParcelableCall = new RttCall(mTelecomCallId, (InputStreamReader)localObject, paramMap, paramParcelableCall.getRttMode(), mInCallAdapter);
        if (mRttCall == null) {
          i = 1;
        }
        while (mRttCall.getRttAudioMode() == paramParcelableCall.getRttAudioMode())
        {
          n = 0;
          break;
        }
        i1 = 1;
        i = n;
        n = i1;
        mRttCall = paramParcelableCall;
        break label600;
      }
    }
    int i1 = 0;
    int i2 = 0;
    i = i2;
    int n = i1;
    if (mRttCall != null)
    {
      i = i2;
      n = i1;
      if (paramParcelableCall.getParcelableRttCall() == null)
      {
        i = i2;
        n = i1;
        if (paramParcelableCall.getIsRttCallChanged())
        {
          mRttCall = null;
          i = 1;
          n = i1;
        }
      }
    }
    label600:
    if (m != 0) {
      fireStateChanged(mState);
    }
    if (bool1) {
      fireDetailsChanged(mDetails);
    }
    if (j != 0) {
      fireCannedTextResponsesLoaded(mCannedTextResponses);
    }
    if (k != 0) {
      fireVideoCallChanged(mVideoCallImpl);
    }
    if (bool2) {
      fireParentChanged(getParent());
    }
    if (bool3) {
      fireChildrenChanged(getChildren());
    }
    if (i != 0)
    {
      boolean bool4;
      if (mRttCall != null) {
        bool4 = true;
      } else {
        bool4 = false;
      }
      fireOnIsRttChanged(bool4, mRttCall);
    }
    if (n != 0) {
      fireOnRttModeChanged(mRttCall.getRttAudioMode());
    }
    if (mState == 7) {
      fireCallDestroyed();
    }
  }
  
  public boolean isRttActive()
  {
    boolean bool;
    if ((mRttCall != null) && (mDetails.hasProperty(1024))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void mergeConference()
  {
    mInCallAdapter.mergeConference(mTelecomCallId);
  }
  
  public void phoneAccountSelected(PhoneAccountHandle paramPhoneAccountHandle, boolean paramBoolean)
  {
    mInCallAdapter.phoneAccountSelected(mTelecomCallId, paramPhoneAccountHandle, paramBoolean);
  }
  
  public void playDtmfTone(char paramChar)
  {
    mInCallAdapter.playDtmfTone(mTelecomCallId, paramChar);
  }
  
  public void postDialContinue(boolean paramBoolean)
  {
    mInCallAdapter.postDialContinue(mTelecomCallId, paramBoolean);
  }
  
  public void pullExternalCall()
  {
    if (!mDetails.hasProperty(64)) {
      return;
    }
    mInCallAdapter.pullExternalCall(mTelecomCallId);
  }
  
  public final void putExtra(String paramString, int paramInt)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putInt(paramString, paramInt);
    mInCallAdapter.putExtra(mTelecomCallId, paramString, paramInt);
  }
  
  public final void putExtra(String paramString1, String paramString2)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putString(paramString1, paramString2);
    mInCallAdapter.putExtra(mTelecomCallId, paramString1, paramString2);
  }
  
  public final void putExtra(String paramString, boolean paramBoolean)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putBoolean(paramString, paramBoolean);
    mInCallAdapter.putExtra(mTelecomCallId, paramString, paramBoolean);
  }
  
  public final void putExtras(Bundle paramBundle)
  {
    if (paramBundle == null) {
      return;
    }
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putAll(paramBundle);
    mInCallAdapter.putExtras(mTelecomCallId, paramBundle);
  }
  
  public void registerCallback(Callback paramCallback)
  {
    registerCallback(paramCallback, new Handler());
  }
  
  public void registerCallback(Callback paramCallback, Handler paramHandler)
  {
    unregisterCallback(paramCallback);
    if ((paramCallback != null) && (paramHandler != null) && (mState != 7)) {
      mCallbackRecords.add(new CallbackRecord(paramCallback, paramHandler));
    }
  }
  
  public void reject(boolean paramBoolean, String paramString)
  {
    mInCallAdapter.rejectCall(mTelecomCallId, paramBoolean, paramString);
  }
  
  public final void removeExtras(List<String> paramList)
  {
    if (mExtras != null)
    {
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        mExtras.remove(str);
      }
      if (mExtras.size() == 0) {
        mExtras = null;
      }
    }
    mInCallAdapter.removeExtras(mTelecomCallId, paramList);
  }
  
  public final void removeExtras(String... paramVarArgs)
  {
    removeExtras(Arrays.asList(paramVarArgs));
  }
  
  @SystemApi
  @Deprecated
  public void removeListener(Listener paramListener)
  {
    unregisterCallback(paramListener);
  }
  
  public void respondToRttRequest(int paramInt, boolean paramBoolean)
  {
    mInCallAdapter.respondToRttRequest(mTelecomCallId, paramInt, paramBoolean);
  }
  
  public void sendCallEvent(String paramString, Bundle paramBundle)
  {
    mInCallAdapter.sendCallEvent(mTelecomCallId, paramString, mTargetSdkVersion, paramBundle);
  }
  
  public void sendRttRequest()
  {
    mInCallAdapter.sendRttRequest(mTelecomCallId);
  }
  
  public void splitFromConference()
  {
    mInCallAdapter.splitFromConference(mTelecomCallId);
  }
  
  public void stopDtmfTone()
  {
    mInCallAdapter.stopDtmfTone(mTelecomCallId);
  }
  
  public void stopRtt()
  {
    mInCallAdapter.stopRtt(mTelecomCallId);
  }
  
  public void swapConference()
  {
    mInCallAdapter.swapConference(mTelecomCallId);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Call [id: ");
    localStringBuilder.append(mTelecomCallId);
    localStringBuilder.append(", state: ");
    localStringBuilder.append(stateToString(mState));
    localStringBuilder.append(", details: ");
    localStringBuilder.append(mDetails);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void unhold()
  {
    mInCallAdapter.unholdCall(mTelecomCallId);
  }
  
  public void unregisterCallback(Callback paramCallback)
  {
    if ((paramCallback != null) && (mState != 7))
    {
      Iterator localIterator = mCallbackRecords.iterator();
      while (localIterator.hasNext())
      {
        CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
        if (localCallbackRecord.getCallback() == paramCallback)
        {
          mCallbackRecords.remove(localCallbackRecord);
          break;
        }
      }
    }
  }
  
  public static abstract class Callback
  {
    public static final int HANDOVER_FAILURE_DEST_APP_REJECTED = 1;
    public static final int HANDOVER_FAILURE_NOT_SUPPORTED = 2;
    public static final int HANDOVER_FAILURE_ONGOING_EMERGENCY_CALL = 4;
    public static final int HANDOVER_FAILURE_UNKNOWN = 5;
    public static final int HANDOVER_FAILURE_USER_REJECTED = 3;
    
    public Callback() {}
    
    public void onCallDestroyed(Call paramCall) {}
    
    public void onCannedTextResponsesLoaded(Call paramCall, List<String> paramList) {}
    
    public void onChildrenChanged(Call paramCall, List<Call> paramList) {}
    
    public void onConferenceableCallsChanged(Call paramCall, List<Call> paramList) {}
    
    public void onConnectionEvent(Call paramCall, String paramString, Bundle paramBundle) {}
    
    public void onDetailsChanged(Call paramCall, Call.Details paramDetails) {}
    
    public void onHandoverComplete(Call paramCall) {}
    
    public void onHandoverFailed(Call paramCall, int paramInt) {}
    
    public void onParentChanged(Call paramCall1, Call paramCall2) {}
    
    public void onPostDialWait(Call paramCall, String paramString) {}
    
    public void onRttInitiationFailure(Call paramCall, int paramInt) {}
    
    public void onRttModeChanged(Call paramCall, int paramInt) {}
    
    public void onRttRequest(Call paramCall, int paramInt) {}
    
    public void onRttStatusChanged(Call paramCall, boolean paramBoolean, Call.RttCall paramRttCall) {}
    
    public void onStateChanged(Call paramCall, int paramInt) {}
    
    public void onVideoCallChanged(Call paramCall, InCallService.VideoCall paramVideoCall) {}
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface HandoverFailureErrors {}
  }
  
  public static class Details
  {
    public static final int CAPABILITY_ADD_PARTICIPANT = 33554432;
    public static final int CAPABILITY_CANNOT_DOWNGRADE_VIDEO_TO_AUDIO = 4194304;
    public static final int CAPABILITY_CAN_PAUSE_VIDEO = 1048576;
    public static final int CAPABILITY_CAN_PULL_CALL = 8388608;
    public static final int CAPABILITY_CAN_SEND_RESPONSE_VIA_CONNECTION = 2097152;
    public static final int CAPABILITY_CAN_UPGRADE_TO_VIDEO = 524288;
    public static final int CAPABILITY_DISCONNECT_FROM_CONFERENCE = 8192;
    public static final int CAPABILITY_HOLD = 1;
    public static final int CAPABILITY_MANAGE_CONFERENCE = 128;
    public static final int CAPABILITY_MERGE_CONFERENCE = 4;
    public static final int CAPABILITY_MUTE = 64;
    public static final int CAPABILITY_RESPOND_VIA_TEXT = 32;
    public static final int CAPABILITY_SEPARATE_FROM_CONFERENCE = 4096;
    public static final int CAPABILITY_SPEED_UP_MT_AUDIO = 262144;
    public static final int CAPABILITY_SUPPORTS_RTT_REMOTE = 67108864;
    public static final int CAPABILITY_SUPPORTS_VT_LOCAL_BIDIRECTIONAL = 768;
    public static final int CAPABILITY_SUPPORTS_VT_LOCAL_RX = 256;
    public static final int CAPABILITY_SUPPORTS_VT_LOCAL_TX = 512;
    public static final int CAPABILITY_SUPPORTS_VT_REMOTE_BIDIRECTIONAL = 3072;
    public static final int CAPABILITY_SUPPORTS_VT_REMOTE_RX = 1024;
    public static final int CAPABILITY_SUPPORTS_VT_REMOTE_TX = 2048;
    public static final int CAPABILITY_SUPPORT_DEFLECT = 16777216;
    public static final int CAPABILITY_SUPPORT_HOLD = 2;
    public static final int CAPABILITY_SWAP_CONFERENCE = 8;
    public static final int CAPABILITY_UNUSED_1 = 16;
    public static final int PROPERTY_ASSISTED_DIALING_USED = 512;
    public static final int PROPERTY_CONFERENCE = 1;
    public static final int PROPERTY_EMERGENCY_CALLBACK_MODE = 4;
    public static final int PROPERTY_ENTERPRISE_CALL = 32;
    public static final int PROPERTY_GENERIC_CONFERENCE = 2;
    public static final int PROPERTY_HAS_CDMA_VOICE_PRIVACY = 128;
    public static final int PROPERTY_HIGH_DEF_AUDIO = 16;
    public static final int PROPERTY_IS_EXTERNAL_CALL = 64;
    public static final int PROPERTY_RTT = 1024;
    public static final int PROPERTY_SELF_MANAGED = 256;
    public static final int PROPERTY_WIFI = 8;
    private final PhoneAccountHandle mAccountHandle;
    private final int mCallCapabilities;
    private final int mCallProperties;
    private final String mCallerDisplayName;
    private final int mCallerDisplayNamePresentation;
    private final long mConnectTimeMillis;
    private final long mCreationTimeMillis;
    private final DisconnectCause mDisconnectCause;
    private final Bundle mExtras;
    private final GatewayInfo mGatewayInfo;
    private final Uri mHandle;
    private final int mHandlePresentation;
    private final Bundle mIntentExtras;
    private final StatusHints mStatusHints;
    private final int mSupportedAudioRoutes = 15;
    private final String mTelecomCallId;
    private final int mVideoState;
    
    public Details(String paramString1, Uri paramUri, int paramInt1, String paramString2, int paramInt2, PhoneAccountHandle paramPhoneAccountHandle, int paramInt3, int paramInt4, DisconnectCause paramDisconnectCause, long paramLong1, GatewayInfo paramGatewayInfo, int paramInt5, StatusHints paramStatusHints, Bundle paramBundle1, Bundle paramBundle2, long paramLong2)
    {
      mTelecomCallId = paramString1;
      mHandle = paramUri;
      mHandlePresentation = paramInt1;
      mCallerDisplayName = paramString2;
      mCallerDisplayNamePresentation = paramInt2;
      mAccountHandle = paramPhoneAccountHandle;
      mCallCapabilities = paramInt3;
      mCallProperties = paramInt4;
      mDisconnectCause = paramDisconnectCause;
      mConnectTimeMillis = paramLong1;
      mGatewayInfo = paramGatewayInfo;
      mVideoState = paramInt5;
      mStatusHints = paramStatusHints;
      mExtras = paramBundle1;
      mIntentExtras = paramBundle2;
      mCreationTimeMillis = paramLong2;
    }
    
    public static boolean can(int paramInt1, int paramInt2)
    {
      boolean bool;
      if ((paramInt1 & paramInt2) == paramInt2) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public static String capabilitiesToString(int paramInt)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("[Capabilities:");
      if (can(paramInt, 1)) {
        localStringBuilder.append(" CAPABILITY_HOLD");
      }
      if (can(paramInt, 2)) {
        localStringBuilder.append(" CAPABILITY_SUPPORT_HOLD");
      }
      if (can(paramInt, 4)) {
        localStringBuilder.append(" CAPABILITY_MERGE_CONFERENCE");
      }
      if (can(paramInt, 8)) {
        localStringBuilder.append(" CAPABILITY_SWAP_CONFERENCE");
      }
      if (can(paramInt, 32)) {
        localStringBuilder.append(" CAPABILITY_RESPOND_VIA_TEXT");
      }
      if (can(paramInt, 64)) {
        localStringBuilder.append(" CAPABILITY_MUTE");
      }
      if (can(paramInt, 128)) {
        localStringBuilder.append(" CAPABILITY_MANAGE_CONFERENCE");
      }
      if (can(paramInt, 256)) {
        localStringBuilder.append(" CAPABILITY_SUPPORTS_VT_LOCAL_RX");
      }
      if (can(paramInt, 512)) {
        localStringBuilder.append(" CAPABILITY_SUPPORTS_VT_LOCAL_TX");
      }
      if (can(paramInt, 768)) {
        localStringBuilder.append(" CAPABILITY_SUPPORTS_VT_LOCAL_BIDIRECTIONAL");
      }
      if (can(paramInt, 1024)) {
        localStringBuilder.append(" CAPABILITY_SUPPORTS_VT_REMOTE_RX");
      }
      if (can(paramInt, 2048)) {
        localStringBuilder.append(" CAPABILITY_SUPPORTS_VT_REMOTE_TX");
      }
      if (can(paramInt, 4194304)) {
        localStringBuilder.append(" CAPABILITY_CANNOT_DOWNGRADE_VIDEO_TO_AUDIO");
      }
      if (can(paramInt, 3072)) {
        localStringBuilder.append(" CAPABILITY_SUPPORTS_VT_REMOTE_BIDIRECTIONAL");
      }
      if (can(paramInt, 262144)) {
        localStringBuilder.append(" CAPABILITY_SPEED_UP_MT_AUDIO");
      }
      if (can(paramInt, 524288)) {
        localStringBuilder.append(" CAPABILITY_CAN_UPGRADE_TO_VIDEO");
      }
      if (can(paramInt, 1048576)) {
        localStringBuilder.append(" CAPABILITY_CAN_PAUSE_VIDEO");
      }
      if (can(paramInt, 8388608)) {
        localStringBuilder.append(" CAPABILITY_CAN_PULL_CALL");
      }
      if (can(paramInt, 33554432)) {
        localStringBuilder.append(" CAPABILITY_ADD_PARTICIPANT");
      }
      if (can(paramInt, 16777216)) {
        localStringBuilder.append(" CAPABILITY_SUPPORT_DEFLECT");
      }
      if (can(paramInt, 67108864)) {
        localStringBuilder.append(" CAPABILITY_SUPPORTS_RTT_REMOTE");
      }
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
    
    public static Details createFromParcelableCall(ParcelableCall paramParcelableCall)
    {
      return new Details(paramParcelableCall.getId(), paramParcelableCall.getHandle(), paramParcelableCall.getHandlePresentation(), paramParcelableCall.getCallerDisplayName(), paramParcelableCall.getCallerDisplayNamePresentation(), paramParcelableCall.getAccountHandle(), paramParcelableCall.getCapabilities(), paramParcelableCall.getProperties(), paramParcelableCall.getDisconnectCause(), paramParcelableCall.getConnectTimeMillis(), paramParcelableCall.getGatewayInfo(), paramParcelableCall.getVideoState(), paramParcelableCall.getStatusHints(), paramParcelableCall.getExtras(), paramParcelableCall.getIntentExtras(), paramParcelableCall.getCreationTimeMillis());
    }
    
    public static boolean hasProperty(int paramInt1, int paramInt2)
    {
      boolean bool;
      if ((paramInt1 & paramInt2) == paramInt2) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public static String propertiesToString(int paramInt)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("[Properties:");
      if (hasProperty(paramInt, 1)) {
        localStringBuilder.append(" PROPERTY_CONFERENCE");
      }
      if (hasProperty(paramInt, 2)) {
        localStringBuilder.append(" PROPERTY_GENERIC_CONFERENCE");
      }
      if (hasProperty(paramInt, 8)) {
        localStringBuilder.append(" PROPERTY_WIFI");
      }
      if (hasProperty(paramInt, 16)) {
        localStringBuilder.append(" PROPERTY_HIGH_DEF_AUDIO");
      }
      if (hasProperty(paramInt, 4)) {
        localStringBuilder.append(" PROPERTY_EMERGENCY_CALLBACK_MODE");
      }
      if (hasProperty(paramInt, 64)) {
        localStringBuilder.append(" PROPERTY_IS_EXTERNAL_CALL");
      }
      if (hasProperty(paramInt, 128)) {
        localStringBuilder.append(" PROPERTY_HAS_CDMA_VOICE_PRIVACY");
      }
      if (hasProperty(paramInt, 512)) {
        localStringBuilder.append(" PROPERTY_ASSISTED_DIALING_USED");
      }
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
    
    public boolean can(int paramInt)
    {
      return can(mCallCapabilities, paramInt);
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool1 = paramObject instanceof Details;
      boolean bool2 = false;
      if (bool1)
      {
        paramObject = (Details)paramObject;
        bool1 = bool2;
        if (Objects.equals(mHandle, mHandle))
        {
          bool1 = bool2;
          if (Objects.equals(Integer.valueOf(mHandlePresentation), Integer.valueOf(mHandlePresentation)))
          {
            bool1 = bool2;
            if (Objects.equals(mCallerDisplayName, mCallerDisplayName))
            {
              bool1 = bool2;
              if (Objects.equals(Integer.valueOf(mCallerDisplayNamePresentation), Integer.valueOf(mCallerDisplayNamePresentation)))
              {
                bool1 = bool2;
                if (Objects.equals(mAccountHandle, mAccountHandle))
                {
                  bool1 = bool2;
                  if (Objects.equals(Integer.valueOf(mCallCapabilities), Integer.valueOf(mCallCapabilities)))
                  {
                    bool1 = bool2;
                    if (Objects.equals(Integer.valueOf(mCallProperties), Integer.valueOf(mCallProperties)))
                    {
                      bool1 = bool2;
                      if (Objects.equals(mDisconnectCause, mDisconnectCause))
                      {
                        bool1 = bool2;
                        if (Objects.equals(Long.valueOf(mConnectTimeMillis), Long.valueOf(mConnectTimeMillis)))
                        {
                          bool1 = bool2;
                          if (Objects.equals(mGatewayInfo, mGatewayInfo))
                          {
                            bool1 = bool2;
                            if (Objects.equals(Integer.valueOf(mVideoState), Integer.valueOf(mVideoState)))
                            {
                              bool1 = bool2;
                              if (Objects.equals(mStatusHints, mStatusHints))
                              {
                                bool1 = bool2;
                                if (Call.areBundlesEqual(mExtras, mExtras))
                                {
                                  bool1 = bool2;
                                  if (Call.areBundlesEqual(mIntentExtras, mIntentExtras))
                                  {
                                    bool1 = bool2;
                                    if (Objects.equals(Long.valueOf(mCreationTimeMillis), Long.valueOf(mCreationTimeMillis))) {
                                      bool1 = true;
                                    }
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        return bool1;
      }
      return false;
    }
    
    public PhoneAccountHandle getAccountHandle()
    {
      return mAccountHandle;
    }
    
    public int getCallCapabilities()
    {
      return mCallCapabilities;
    }
    
    public int getCallProperties()
    {
      return mCallProperties;
    }
    
    public String getCallerDisplayName()
    {
      return mCallerDisplayName;
    }
    
    public int getCallerDisplayNamePresentation()
    {
      return mCallerDisplayNamePresentation;
    }
    
    public final long getConnectTimeMillis()
    {
      return mConnectTimeMillis;
    }
    
    public long getCreationTimeMillis()
    {
      return mCreationTimeMillis;
    }
    
    public DisconnectCause getDisconnectCause()
    {
      return mDisconnectCause;
    }
    
    public Bundle getExtras()
    {
      return mExtras;
    }
    
    public GatewayInfo getGatewayInfo()
    {
      return mGatewayInfo;
    }
    
    public Uri getHandle()
    {
      return mHandle;
    }
    
    public int getHandlePresentation()
    {
      return mHandlePresentation;
    }
    
    public Bundle getIntentExtras()
    {
      return mIntentExtras;
    }
    
    public StatusHints getStatusHints()
    {
      return mStatusHints;
    }
    
    public int getSupportedAudioRoutes()
    {
      return 15;
    }
    
    public String getTelecomCallId()
    {
      return mTelecomCallId;
    }
    
    public int getVideoState()
    {
      return mVideoState;
    }
    
    public boolean hasProperty(int paramInt)
    {
      return hasProperty(mCallProperties, paramInt);
    }
    
    public int hashCode()
    {
      return Objects.hash(new Object[] { mHandle, Integer.valueOf(mHandlePresentation), mCallerDisplayName, Integer.valueOf(mCallerDisplayNamePresentation), mAccountHandle, Integer.valueOf(mCallCapabilities), Integer.valueOf(mCallProperties), mDisconnectCause, Long.valueOf(mConnectTimeMillis), mGatewayInfo, Integer.valueOf(mVideoState), mStatusHints, mExtras, mIntentExtras, Long.valueOf(mCreationTimeMillis) });
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("[pa: ");
      localStringBuilder.append(mAccountHandle);
      localStringBuilder.append(", hdl: ");
      localStringBuilder.append(Log.pii(mHandle));
      localStringBuilder.append(", caps: ");
      localStringBuilder.append(capabilitiesToString(mCallCapabilities));
      localStringBuilder.append(", props: ");
      localStringBuilder.append(propertiesToString(mCallProperties));
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
  }
  
  @SystemApi
  @Deprecated
  public static abstract class Listener
    extends Call.Callback
  {
    public Listener() {}
  }
  
  public static final class RttCall
  {
    private static final int READ_BUFFER_SIZE = 1000;
    public static final int RTT_MODE_FULL = 1;
    public static final int RTT_MODE_HCO = 2;
    public static final int RTT_MODE_INVALID = 0;
    public static final int RTT_MODE_VCO = 3;
    private final InCallAdapter mInCallAdapter;
    private char[] mReadBuffer = new char['Ϩ'];
    private InputStreamReader mReceiveStream;
    private int mRttMode;
    private final String mTelecomCallId;
    private OutputStreamWriter mTransmitStream;
    
    public RttCall(String paramString, InputStreamReader paramInputStreamReader, OutputStreamWriter paramOutputStreamWriter, int paramInt, InCallAdapter paramInCallAdapter)
    {
      mTelecomCallId = paramString;
      mReceiveStream = paramInputStreamReader;
      mTransmitStream = paramOutputStreamWriter;
      mRttMode = paramInt;
      mInCallAdapter = paramInCallAdapter;
    }
    
    public void close()
    {
      try
      {
        mReceiveStream.close();
      }
      catch (IOException localIOException1) {}
      try
      {
        mTransmitStream.close();
      }
      catch (IOException localIOException2) {}
    }
    
    public int getRttAudioMode()
    {
      return mRttMode;
    }
    
    public String read()
    {
      try
      {
        int i = mReceiveStream.read(mReadBuffer, 0, 1000);
        if (i < 0) {
          return null;
        }
        String str = new String(mReadBuffer, 0, i);
        return str;
      }
      catch (IOException localIOException)
      {
        Log.w(this, "Exception encountered when reading from InputStreamReader: %s", new Object[] { localIOException });
      }
      return null;
    }
    
    public String readImmediately()
      throws IOException
    {
      if (mReceiveStream.ready())
      {
        int i = mReceiveStream.read(mReadBuffer, 0, 1000);
        if (i < 0) {
          return null;
        }
        return new String(mReadBuffer, 0, i);
      }
      return null;
    }
    
    public void setRttMode(int paramInt)
    {
      mInCallAdapter.setRttMode(mTelecomCallId, paramInt);
    }
    
    public void write(String paramString)
      throws IOException
    {
      mTransmitStream.write(paramString);
      mTransmitStream.flush();
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface RttAudioMode {}
  }
}
