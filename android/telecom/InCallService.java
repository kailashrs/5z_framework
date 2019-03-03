package android.telecom;

import android.annotation.SystemApi;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.view.Surface;
import com.android.internal.os.SomeArgs;
import com.android.internal.telecom.IInCallAdapter;
import com.android.internal.telecom.IInCallService.Stub;
import java.util.Collections;
import java.util.List;

public abstract class InCallService
  extends Service
{
  private static final int MSG_ADD_CALL = 2;
  private static final int MSG_BRING_TO_FOREGROUND = 6;
  private static final int MSG_ON_CALL_AUDIO_STATE_CHANGED = 5;
  private static final int MSG_ON_CAN_ADD_CALL_CHANGED = 7;
  private static final int MSG_ON_CONNECTION_EVENT = 9;
  private static final int MSG_ON_HANDOVER_COMPLETE = 13;
  private static final int MSG_ON_HANDOVER_FAILED = 12;
  private static final int MSG_ON_RTT_INITIATION_FAILURE = 11;
  private static final int MSG_ON_RTT_UPGRADE_REQUEST = 10;
  private static final int MSG_SET_IN_CALL_ADAPTER = 1;
  private static final int MSG_SET_POST_DIAL_WAIT = 4;
  private static final int MSG_SILENCE_RINGER = 8;
  private static final int MSG_UPDATE_CALL = 3;
  public static final String SERVICE_INTERFACE = "android.telecom.InCallService";
  private final Handler mHandler = new Handler(Looper.getMainLooper())
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      Object localObject1 = mPhone;
      boolean bool1 = true;
      boolean bool2 = true;
      if ((localObject1 == null) && (what != 1)) {
        return;
      }
      int i;
      String str3;
      Object localObject3;
      switch (what)
      {
      default: 
        break;
      case 13: 
        paramAnonymousMessage = (String)obj;
        mPhone.internalOnHandoverComplete(paramAnonymousMessage);
        break;
      case 12: 
        localObject1 = (String)obj;
        i = arg1;
        mPhone.internalOnHandoverFailed((String)localObject1, i);
        break;
      case 11: 
        localObject1 = (String)obj;
        i = arg1;
        mPhone.internalOnRttInitiationFailure((String)localObject1, i);
        break;
      case 10: 
        localObject1 = (String)obj;
        i = arg1;
        mPhone.internalOnRttUpgradeRequest((String)localObject1, i);
        break;
      case 9: 
        paramAnonymousMessage = (SomeArgs)obj;
      case 8: 
        try
        {
          String str2 = (String)arg1;
          str3 = (String)arg2;
          localObject1 = (Bundle)arg3;
          mPhone.internalOnConnectionEvent(str2, str3, (Bundle)localObject1);
          paramAnonymousMessage.recycle();
        }
        finally
        {
          paramAnonymousMessage.recycle();
        }
        break;
      case 7: 
        localObject3 = mPhone;
        if (arg1 != 1) {
          bool2 = false;
        }
        ((Phone)localObject3).internalSetCanAddCall(bool2);
        break;
      case 6: 
        localObject3 = mPhone;
        if (arg1 == 1) {
          bool2 = bool1;
        } else {
          bool2 = false;
        }
        ((Phone)localObject3).internalBringToForeground(bool2);
        break;
      case 5: 
        mPhone.internalCallAudioStateChanged((CallAudioState)obj);
        break;
      case 4: 
        paramAnonymousMessage = (SomeArgs)obj;
      case 3: 
        try
        {
          localObject3 = (String)arg1;
          str3 = (String)arg2;
          mPhone.internalSetPostDialWait((String)localObject3, str3);
          paramAnonymousMessage.recycle();
        }
        finally
        {
          paramAnonymousMessage.recycle();
        }
        break;
      case 2: 
        mPhone.internalAddCall((ParcelableCall)obj);
        break;
      case 1: 
        String str1 = getApplicationContext().getOpPackageName();
        InCallService.access$002(InCallService.this, new Phone(new InCallAdapter((IInCallAdapter)obj), str1, getApplicationContext().getApplicationInfo().targetSdkVersion));
        mPhone.addListener(mPhoneListener);
        onPhoneCreated(mPhone);
      }
    }
  };
  private Phone mPhone;
  private Phone.Listener mPhoneListener = new Phone.Listener()
  {
    public void onAudioStateChanged(Phone paramAnonymousPhone, AudioState paramAnonymousAudioState)
    {
      onAudioStateChanged(paramAnonymousAudioState);
    }
    
    public void onBringToForeground(Phone paramAnonymousPhone, boolean paramAnonymousBoolean)
    {
      onBringToForeground(paramAnonymousBoolean);
    }
    
    public void onCallAdded(Phone paramAnonymousPhone, Call paramAnonymousCall)
    {
      onCallAdded(paramAnonymousCall);
    }
    
    public void onCallAudioStateChanged(Phone paramAnonymousPhone, CallAudioState paramAnonymousCallAudioState)
    {
      onCallAudioStateChanged(paramAnonymousCallAudioState);
    }
    
    public void onCallRemoved(Phone paramAnonymousPhone, Call paramAnonymousCall)
    {
      onCallRemoved(paramAnonymousCall);
    }
    
    public void onCanAddCallChanged(Phone paramAnonymousPhone, boolean paramAnonymousBoolean)
    {
      onCanAddCallChanged(paramAnonymousBoolean);
    }
    
    public void onSilenceRinger(Phone paramAnonymousPhone)
    {
      onSilenceRinger();
    }
  };
  
  public InCallService() {}
  
  public final boolean canAddCall()
  {
    boolean bool;
    if (mPhone == null) {
      bool = false;
    } else {
      bool = mPhone.canAddCall();
    }
    return bool;
  }
  
  @Deprecated
  public final AudioState getAudioState()
  {
    AudioState localAudioState;
    if (mPhone == null) {
      localAudioState = null;
    } else {
      localAudioState = mPhone.getAudioState();
    }
    return localAudioState;
  }
  
  public final CallAudioState getCallAudioState()
  {
    CallAudioState localCallAudioState;
    if (mPhone == null) {
      localCallAudioState = null;
    } else {
      localCallAudioState = mPhone.getCallAudioState();
    }
    return localCallAudioState;
  }
  
  public final List<Call> getCalls()
  {
    List localList;
    if (mPhone == null) {
      localList = Collections.emptyList();
    } else {
      localList = mPhone.getCalls();
    }
    return localList;
  }
  
  @SystemApi
  @Deprecated
  public Phone getPhone()
  {
    return mPhone;
  }
  
  @Deprecated
  public void onAudioStateChanged(AudioState paramAudioState) {}
  
  public IBinder onBind(Intent paramIntent)
  {
    return new InCallServiceBinder(null);
  }
  
  public void onBringToForeground(boolean paramBoolean) {}
  
  public void onCallAdded(Call paramCall) {}
  
  public void onCallAudioStateChanged(CallAudioState paramCallAudioState) {}
  
  public void onCallRemoved(Call paramCall) {}
  
  public void onCanAddCallChanged(boolean paramBoolean) {}
  
  public void onConnectionEvent(Call paramCall, String paramString, Bundle paramBundle) {}
  
  @SystemApi
  @Deprecated
  public void onPhoneCreated(Phone paramPhone) {}
  
  @SystemApi
  @Deprecated
  public void onPhoneDestroyed(Phone paramPhone) {}
  
  public void onSilenceRinger() {}
  
  public boolean onUnbind(Intent paramIntent)
  {
    if (mPhone != null)
    {
      paramIntent = mPhone;
      mPhone = null;
      paramIntent.destroy();
      paramIntent.removeListener(mPhoneListener);
      onPhoneDestroyed(paramIntent);
    }
    return false;
  }
  
  public final void requestBluetoothAudio(BluetoothDevice paramBluetoothDevice)
  {
    if (mPhone != null) {
      mPhone.requestBluetoothAudio(paramBluetoothDevice.getAddress());
    }
  }
  
  public final void setAudioRoute(int paramInt)
  {
    if (mPhone != null) {
      mPhone.setAudioRoute(paramInt);
    }
  }
  
  public final void setMuted(boolean paramBoolean)
  {
    if (mPhone != null) {
      mPhone.setMuted(paramBoolean);
    }
  }
  
  private final class InCallServiceBinder
    extends IInCallService.Stub
  {
    private InCallServiceBinder() {}
    
    public void addCall(ParcelableCall paramParcelableCall)
    {
      mHandler.obtainMessage(2, paramParcelableCall).sendToTarget();
    }
    
    public void bringToForeground(boolean paramBoolean)
    {
      mHandler.obtainMessage(6, paramBoolean, 0).sendToTarget();
    }
    
    public void onCallAudioStateChanged(CallAudioState paramCallAudioState)
    {
      mHandler.obtainMessage(5, paramCallAudioState).sendToTarget();
    }
    
    public void onCanAddCallChanged(boolean paramBoolean)
    {
      mHandler.obtainMessage(7, paramBoolean, 0).sendToTarget();
    }
    
    public void onConnectionEvent(String paramString1, String paramString2, Bundle paramBundle)
    {
      SomeArgs localSomeArgs = SomeArgs.obtain();
      arg1 = paramString1;
      arg2 = paramString2;
      arg3 = paramBundle;
      mHandler.obtainMessage(9, localSomeArgs).sendToTarget();
    }
    
    public void onHandoverComplete(String paramString)
    {
      mHandler.obtainMessage(13, paramString).sendToTarget();
    }
    
    public void onHandoverFailed(String paramString, int paramInt)
    {
      mHandler.obtainMessage(12, paramInt, 0, paramString).sendToTarget();
    }
    
    public void onRttInitiationFailure(String paramString, int paramInt)
    {
      mHandler.obtainMessage(11, paramInt, 0, paramString).sendToTarget();
    }
    
    public void onRttUpgradeRequest(String paramString, int paramInt)
    {
      mHandler.obtainMessage(10, paramInt, 0, paramString).sendToTarget();
    }
    
    public void setInCallAdapter(IInCallAdapter paramIInCallAdapter)
    {
      mHandler.obtainMessage(1, paramIInCallAdapter).sendToTarget();
    }
    
    public void setPostDial(String paramString1, String paramString2) {}
    
    public void setPostDialWait(String paramString1, String paramString2)
    {
      SomeArgs localSomeArgs = SomeArgs.obtain();
      arg1 = paramString1;
      arg2 = paramString2;
      mHandler.obtainMessage(4, localSomeArgs).sendToTarget();
    }
    
    public void silenceRinger()
    {
      mHandler.obtainMessage(8).sendToTarget();
    }
    
    public void updateCall(ParcelableCall paramParcelableCall)
    {
      mHandler.obtainMessage(3, paramParcelableCall).sendToTarget();
    }
  }
  
  public static abstract class VideoCall
  {
    public VideoCall() {}
    
    public abstract void destroy();
    
    public abstract void registerCallback(Callback paramCallback);
    
    public abstract void registerCallback(Callback paramCallback, Handler paramHandler);
    
    public abstract void requestCallDataUsage();
    
    public abstract void requestCameraCapabilities();
    
    public abstract void sendSessionModifyRequest(VideoProfile paramVideoProfile);
    
    public abstract void sendSessionModifyResponse(VideoProfile paramVideoProfile);
    
    public abstract void setCamera(String paramString);
    
    public abstract void setDeviceOrientation(int paramInt);
    
    public abstract void setDisplaySurface(Surface paramSurface);
    
    public abstract void setPauseImage(Uri paramUri);
    
    public abstract void setPreviewSurface(Surface paramSurface);
    
    public abstract void setZoom(float paramFloat);
    
    public abstract void unregisterCallback(Callback paramCallback);
    
    public static abstract class Callback
    {
      public Callback() {}
      
      public abstract void onCallDataUsageChanged(long paramLong);
      
      public abstract void onCallSessionEvent(int paramInt);
      
      public abstract void onCameraCapabilitiesChanged(VideoProfile.CameraCapabilities paramCameraCapabilities);
      
      public abstract void onPeerDimensionsChanged(int paramInt1, int paramInt2);
      
      public abstract void onSessionModifyRequestReceived(VideoProfile paramVideoProfile);
      
      public abstract void onSessionModifyResponseReceived(int paramInt, VideoProfile paramVideoProfile1, VideoProfile paramVideoProfile2);
      
      public abstract void onVideoQualityChanged(int paramInt);
    }
  }
}
