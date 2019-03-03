package android.telecom;

import android.annotation.SystemApi;
import android.os.Bundle;
import android.util.ArrayMap;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

@SystemApi
@Deprecated
public final class Phone
{
  private CallAudioState mCallAudioState;
  private final Map<String, Call> mCallByTelecomCallId = new ArrayMap();
  private final String mCallingPackage;
  private final List<Call> mCalls = new CopyOnWriteArrayList();
  private boolean mCanAddCall = true;
  private final InCallAdapter mInCallAdapter;
  private final List<Listener> mListeners = new CopyOnWriteArrayList();
  private final int mTargetSdkVersion;
  private final List<Call> mUnmodifiableCalls = Collections.unmodifiableList(mCalls);
  
  Phone(InCallAdapter paramInCallAdapter, String paramString, int paramInt)
  {
    mInCallAdapter = paramInCallAdapter;
    mCallingPackage = paramString;
    mTargetSdkVersion = paramInt;
  }
  
  private void checkCallTree(ParcelableCall paramParcelableCall)
  {
    if (paramParcelableCall.getChildCallIds() != null) {
      for (int i = 0; i < paramParcelableCall.getChildCallIds().size(); i++) {
        if (!mCallByTelecomCallId.containsKey(paramParcelableCall.getChildCallIds().get(i))) {
          Log.wtf(this, "ParcelableCall %s has nonexistent child %s", new Object[] { paramParcelableCall.getId(), paramParcelableCall.getChildCallIds().get(i) });
        }
      }
    }
  }
  
  private void fireBringToForeground(boolean paramBoolean)
  {
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onBringToForeground(this, paramBoolean);
    }
  }
  
  private void fireCallAdded(Call paramCall)
  {
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onCallAdded(this, paramCall);
    }
  }
  
  private void fireCallAudioStateChanged(CallAudioState paramCallAudioState)
  {
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext())
    {
      Listener localListener = (Listener)localIterator.next();
      localListener.onCallAudioStateChanged(this, paramCallAudioState);
      localListener.onAudioStateChanged(this, new AudioState(paramCallAudioState));
    }
  }
  
  private void fireCallRemoved(Call paramCall)
  {
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onCallRemoved(this, paramCall);
    }
  }
  
  private void fireCanAddCallChanged(boolean paramBoolean)
  {
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onCanAddCallChanged(this, paramBoolean);
    }
  }
  
  private void fireSilenceRinger()
  {
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onSilenceRinger(this);
    }
  }
  
  public final void addListener(Listener paramListener)
  {
    mListeners.add(paramListener);
  }
  
  public final boolean canAddCall()
  {
    return mCanAddCall;
  }
  
  final void destroy()
  {
    Iterator localIterator = mCalls.iterator();
    while (localIterator.hasNext())
    {
      Call localCall = (Call)localIterator.next();
      InCallService.VideoCall localVideoCall = localCall.getVideoCall();
      if (localVideoCall != null) {
        localVideoCall.destroy();
      }
      if (localCall.getState() != 7) {
        localCall.internalSetDisconnected();
      }
    }
  }
  
  @Deprecated
  public final AudioState getAudioState()
  {
    return new AudioState(mCallAudioState);
  }
  
  public final CallAudioState getCallAudioState()
  {
    return mCallAudioState;
  }
  
  public final List<Call> getCalls()
  {
    return mUnmodifiableCalls;
  }
  
  final void internalAddCall(ParcelableCall paramParcelableCall)
  {
    Call localCall = new Call(this, paramParcelableCall.getId(), mInCallAdapter, paramParcelableCall.getState(), mCallingPackage, mTargetSdkVersion);
    mCallByTelecomCallId.put(paramParcelableCall.getId(), localCall);
    mCalls.add(localCall);
    checkCallTree(paramParcelableCall);
    localCall.internalUpdate(paramParcelableCall, mCallByTelecomCallId);
    fireCallAdded(localCall);
  }
  
  final void internalBringToForeground(boolean paramBoolean)
  {
    fireBringToForeground(paramBoolean);
  }
  
  final void internalCallAudioStateChanged(CallAudioState paramCallAudioState)
  {
    if (!Objects.equals(mCallAudioState, paramCallAudioState))
    {
      mCallAudioState = paramCallAudioState;
      fireCallAudioStateChanged(paramCallAudioState);
    }
  }
  
  final Call internalGetCallByTelecomId(String paramString)
  {
    return (Call)mCallByTelecomCallId.get(paramString);
  }
  
  final void internalOnConnectionEvent(String paramString1, String paramString2, Bundle paramBundle)
  {
    paramString1 = (Call)mCallByTelecomCallId.get(paramString1);
    if (paramString1 != null) {
      paramString1.internalOnConnectionEvent(paramString2, paramBundle);
    }
  }
  
  final void internalOnHandoverComplete(String paramString)
  {
    paramString = (Call)mCallByTelecomCallId.get(paramString);
    if (paramString != null) {
      paramString.internalOnHandoverComplete();
    }
  }
  
  final void internalOnHandoverFailed(String paramString, int paramInt)
  {
    paramString = (Call)mCallByTelecomCallId.get(paramString);
    if (paramString != null) {
      paramString.internalOnHandoverFailed(paramInt);
    }
  }
  
  final void internalOnRttInitiationFailure(String paramString, int paramInt)
  {
    paramString = (Call)mCallByTelecomCallId.get(paramString);
    if (paramString != null) {
      paramString.internalOnRttInitiationFailure(paramInt);
    }
  }
  
  final void internalOnRttUpgradeRequest(String paramString, int paramInt)
  {
    paramString = (Call)mCallByTelecomCallId.get(paramString);
    if (paramString != null) {
      paramString.internalOnRttUpgradeRequest(paramInt);
    }
  }
  
  final void internalRemoveCall(Call paramCall)
  {
    mCallByTelecomCallId.remove(paramCall.internalGetCallId());
    mCalls.remove(paramCall);
    InCallService.VideoCall localVideoCall = paramCall.getVideoCall();
    if (localVideoCall != null) {
      localVideoCall.destroy();
    }
    fireCallRemoved(paramCall);
  }
  
  final void internalSetCanAddCall(boolean paramBoolean)
  {
    if (mCanAddCall != paramBoolean)
    {
      mCanAddCall = paramBoolean;
      fireCanAddCallChanged(paramBoolean);
    }
  }
  
  final void internalSetPostDialWait(String paramString1, String paramString2)
  {
    paramString1 = (Call)mCallByTelecomCallId.get(paramString1);
    if (paramString1 != null) {
      paramString1.internalSetPostDialWait(paramString2);
    }
  }
  
  final void internalSilenceRinger()
  {
    fireSilenceRinger();
  }
  
  final void internalUpdateCall(ParcelableCall paramParcelableCall)
  {
    Call localCall = (Call)mCallByTelecomCallId.get(paramParcelableCall.getId());
    if (localCall != null)
    {
      checkCallTree(paramParcelableCall);
      localCall.internalUpdate(paramParcelableCall, mCallByTelecomCallId);
    }
  }
  
  public final void removeListener(Listener paramListener)
  {
    if (paramListener != null) {
      mListeners.remove(paramListener);
    }
  }
  
  public void requestBluetoothAudio(String paramString)
  {
    mInCallAdapter.requestBluetoothAudio(paramString);
  }
  
  public final void setAudioRoute(int paramInt)
  {
    mInCallAdapter.setAudioRoute(paramInt);
  }
  
  public final void setMuted(boolean paramBoolean)
  {
    mInCallAdapter.mute(paramBoolean);
  }
  
  public final void setProximitySensorOff(boolean paramBoolean)
  {
    mInCallAdapter.turnProximitySensorOff(paramBoolean);
  }
  
  public final void setProximitySensorOn()
  {
    mInCallAdapter.turnProximitySensorOn();
  }
  
  public static abstract class Listener
  {
    public Listener() {}
    
    @Deprecated
    public void onAudioStateChanged(Phone paramPhone, AudioState paramAudioState) {}
    
    public void onBringToForeground(Phone paramPhone, boolean paramBoolean) {}
    
    public void onCallAdded(Phone paramPhone, Call paramCall) {}
    
    public void onCallAudioStateChanged(Phone paramPhone, CallAudioState paramCallAudioState) {}
    
    public void onCallRemoved(Phone paramPhone, Call paramCall) {}
    
    public void onCanAddCallChanged(Phone paramPhone, boolean paramBoolean) {}
    
    public void onSilenceRinger(Phone paramPhone) {}
  }
}
