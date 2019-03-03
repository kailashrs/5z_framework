package android.media.audiopolicy;

import android.annotation.SystemApi;
import android.content.Context;
import android.media.AudioAttributes.Builder;
import android.media.AudioFocusInfo;
import android.media.AudioFormat;
import android.media.AudioFormat.Builder;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.IAudioService;
import android.media.IAudioService.Stub;
import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.util.Slog;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SystemApi
public class AudioPolicy
{
  private static final boolean DEBUG = false;
  public static final int FOCUS_POLICY_DUCKING_DEFAULT = 0;
  @SystemApi
  public static final int FOCUS_POLICY_DUCKING_IN_APP = 0;
  @SystemApi
  public static final int FOCUS_POLICY_DUCKING_IN_POLICY = 1;
  private static final int MSG_FOCUS_ABANDON = 5;
  private static final int MSG_FOCUS_GRANT = 1;
  private static final int MSG_FOCUS_LOSS = 2;
  private static final int MSG_FOCUS_REQUEST = 4;
  private static final int MSG_MIX_STATE_UPDATE = 3;
  private static final int MSG_POLICY_STATUS_CHANGE = 0;
  private static final int MSG_VOL_ADJUST = 6;
  @SystemApi
  public static final int POLICY_STATUS_REGISTERED = 2;
  @SystemApi
  public static final int POLICY_STATUS_UNREGISTERED = 1;
  private static final String TAG = "AudioPolicy";
  private static IAudioService sService;
  private AudioPolicyConfig mConfig;
  private Context mContext;
  private final EventHandler mEventHandler;
  private AudioPolicyFocusListener mFocusListener;
  private boolean mIsFocusPolicy;
  private final Object mLock = new Object();
  private final IAudioPolicyCallback mPolicyCb = new IAudioPolicyCallback.Stub()
  {
    public void notifyAudioFocusAbandon(AudioFocusInfo paramAnonymousAudioFocusInfo)
    {
      AudioPolicy.this.sendMsg(5, paramAnonymousAudioFocusInfo, 0);
    }
    
    public void notifyAudioFocusGrant(AudioFocusInfo paramAnonymousAudioFocusInfo, int paramAnonymousInt)
    {
      AudioPolicy.this.sendMsg(1, paramAnonymousAudioFocusInfo, paramAnonymousInt);
    }
    
    public void notifyAudioFocusLoss(AudioFocusInfo paramAnonymousAudioFocusInfo, boolean paramAnonymousBoolean)
    {
      AudioPolicy.this.sendMsg(2, paramAnonymousAudioFocusInfo, paramAnonymousBoolean);
    }
    
    public void notifyAudioFocusRequest(AudioFocusInfo paramAnonymousAudioFocusInfo, int paramAnonymousInt)
    {
      AudioPolicy.this.sendMsg(4, paramAnonymousAudioFocusInfo, paramAnonymousInt);
    }
    
    public void notifyMixStateUpdate(String paramAnonymousString, int paramAnonymousInt)
    {
      Iterator localIterator = mConfig.getMixes().iterator();
      while (localIterator.hasNext())
      {
        AudioMix localAudioMix = (AudioMix)localIterator.next();
        if (localAudioMix.getRegistration().equals(paramAnonymousString))
        {
          mMixState = paramAnonymousInt;
          AudioPolicy.this.sendMsg(3, localAudioMix, 0);
        }
      }
    }
    
    public void notifyVolumeAdjust(int paramAnonymousInt)
    {
      AudioPolicy.this.sendMsg(6, null, paramAnonymousInt);
    }
  };
  private String mRegistrationId;
  private int mStatus;
  private AudioPolicyStatusListener mStatusListener;
  private final AudioPolicyVolumeCallback mVolCb;
  
  private AudioPolicy(AudioPolicyConfig paramAudioPolicyConfig, Context paramContext, Looper paramLooper, AudioPolicyFocusListener paramAudioPolicyFocusListener, AudioPolicyStatusListener paramAudioPolicyStatusListener, boolean paramBoolean, AudioPolicyVolumeCallback paramAudioPolicyVolumeCallback)
  {
    mConfig = paramAudioPolicyConfig;
    mStatus = 1;
    mContext = paramContext;
    paramAudioPolicyConfig = paramLooper;
    if (paramLooper == null) {
      paramAudioPolicyConfig = Looper.getMainLooper();
    }
    if (paramAudioPolicyConfig != null)
    {
      mEventHandler = new EventHandler(this, paramAudioPolicyConfig);
    }
    else
    {
      mEventHandler = null;
      Log.e("AudioPolicy", "No event handler due to looper without a thread");
    }
    mFocusListener = paramAudioPolicyFocusListener;
    mStatusListener = paramAudioPolicyStatusListener;
    mIsFocusPolicy = paramBoolean;
    mVolCb = paramAudioPolicyVolumeCallback;
  }
  
  private static String addressForTag(AudioMix paramAudioMix)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("addr=");
    localStringBuilder.append(paramAudioMix.getRegistration());
    return localStringBuilder.toString();
  }
  
  private void checkMixReadyToUse(AudioMix paramAudioMix, boolean paramBoolean)
    throws IllegalArgumentException
  {
    if (paramAudioMix == null)
    {
      if (paramBoolean) {
        paramAudioMix = "Invalid null AudioMix for AudioTrack creation";
      } else {
        paramAudioMix = "Invalid null AudioMix for AudioRecord creation";
      }
      throw new IllegalArgumentException(paramAudioMix);
    }
    if (mConfig.mMixes.contains(paramAudioMix))
    {
      if ((paramAudioMix.getRouteFlags() & 0x2) == 2)
      {
        if ((paramBoolean) && (paramAudioMix.getMixType() != 1)) {
          throw new IllegalArgumentException("Invalid AudioMix: not defined for being a recording source");
        }
        if ((!paramBoolean) && (paramAudioMix.getMixType() != 0)) {
          throw new IllegalArgumentException("Invalid AudioMix: not defined for capturing playback");
        }
        return;
      }
      throw new IllegalArgumentException("Invalid AudioMix: not defined for loop back");
    }
    throw new IllegalArgumentException("Invalid mix: not part of this policy");
  }
  
  private static IAudioService getService()
  {
    if (sService != null) {
      return sService;
    }
    sService = IAudioService.Stub.asInterface(ServiceManager.getService("audio"));
    return sService;
  }
  
  private void onPolicyStatusChange()
  {
    synchronized (mLock)
    {
      if (mStatusListener == null) {
        return;
      }
      AudioPolicyStatusListener localAudioPolicyStatusListener = mStatusListener;
      localAudioPolicyStatusListener.onStatusChange();
      return;
    }
  }
  
  private boolean policyReadyToUse()
  {
    synchronized (mLock)
    {
      if (mStatus != 2)
      {
        Log.e("AudioPolicy", "Cannot use unregistered AudioPolicy");
        return false;
      }
      if (mContext == null)
      {
        Log.e("AudioPolicy", "Cannot use AudioPolicy without context");
        return false;
      }
      if (mRegistrationId == null)
      {
        Log.e("AudioPolicy", "Cannot use unregistered AudioPolicy");
        return false;
      }
      if (mContext.checkCallingOrSelfPermission("android.permission.MODIFY_AUDIO_ROUTING") != 0)
      {
        ??? = new StringBuilder();
        ((StringBuilder)???).append("Cannot use AudioPolicy for pid ");
        ((StringBuilder)???).append(Binder.getCallingPid());
        ((StringBuilder)???).append(" / uid ");
        ((StringBuilder)???).append(Binder.getCallingUid());
        ((StringBuilder)???).append(", needs MODIFY_AUDIO_ROUTING");
        Slog.w("AudioPolicy", ((StringBuilder)???).toString());
        return false;
      }
      return true;
    }
  }
  
  private void sendMsg(int paramInt)
  {
    if (mEventHandler != null) {
      mEventHandler.sendEmptyMessage(paramInt);
    }
  }
  
  private void sendMsg(int paramInt1, Object paramObject, int paramInt2)
  {
    if (mEventHandler != null) {
      mEventHandler.sendMessage(mEventHandler.obtainMessage(paramInt1, paramInt2, 0, paramObject));
    }
  }
  
  @SystemApi
  public int attachMixes(List<AudioMix> paramList)
  {
    if (paramList != null) {
      synchronized (mLock)
      {
        if (mStatus == 2)
        {
          ArrayList localArrayList = new java/util/ArrayList;
          localArrayList.<init>(paramList.size());
          paramList = paramList.iterator();
          while (paramList.hasNext())
          {
            localObject2 = (AudioMix)paramList.next();
            if (localObject2 != null)
            {
              localArrayList.add(localObject2);
            }
            else
            {
              paramList = new java/lang/IllegalArgumentException;
              paramList.<init>("Illegal null AudioMix in attachMixes");
              throw paramList;
            }
          }
          Object localObject2 = new android/media/audiopolicy/AudioPolicyConfig;
          ((AudioPolicyConfig)localObject2).<init>(localArrayList);
          paramList = getService();
          try
          {
            int i = paramList.addMixForPolicy((AudioPolicyConfig)localObject2, cb());
            if (i == 0) {
              mConfig.add(localArrayList);
            }
            return i;
          }
          catch (RemoteException paramList)
          {
            Log.e("AudioPolicy", "Dead object in attachMixes", paramList);
            return -1;
          }
        }
        paramList = new java/lang/IllegalStateException;
        paramList.<init>("Cannot alter unregistered AudioPolicy");
        throw paramList;
      }
    }
    throw new IllegalArgumentException("Illegal null list of AudioMix");
  }
  
  public IAudioPolicyCallback cb()
  {
    return mPolicyCb;
  }
  
  @SystemApi
  public AudioRecord createAudioRecordSink(AudioMix paramAudioMix)
    throws IllegalArgumentException
  {
    if (!policyReadyToUse())
    {
      Log.e("AudioPolicy", "Cannot create AudioRecord sink for AudioMix");
      return null;
    }
    checkMixReadyToUse(paramAudioMix, false);
    AudioFormat localAudioFormat = new AudioFormat.Builder(paramAudioMix.getFormat()).setChannelMask(AudioFormat.inChannelMaskFromOutChannelMask(paramAudioMix.getFormat().getChannelMask())).build();
    return new AudioRecord(new AudioAttributes.Builder().setInternalCapturePreset(8).addTag(addressForTag(paramAudioMix)).addTag("fixedVolume").build(), localAudioFormat, AudioRecord.getMinBufferSize(paramAudioMix.getFormat().getSampleRate(), 12, paramAudioMix.getFormat().getEncoding()), 0);
  }
  
  @SystemApi
  public AudioTrack createAudioTrackSource(AudioMix paramAudioMix)
    throws IllegalArgumentException
  {
    if (!policyReadyToUse())
    {
      Log.e("AudioPolicy", "Cannot create AudioTrack source for AudioMix");
      return null;
    }
    checkMixReadyToUse(paramAudioMix, true);
    return new AudioTrack(new AudioAttributes.Builder().setUsage(15).addTag(addressForTag(paramAudioMix)).build(), paramAudioMix.getFormat(), AudioTrack.getMinBufferSize(paramAudioMix.getFormat().getSampleRate(), paramAudioMix.getFormat().getChannelMask(), paramAudioMix.getFormat().getEncoding()), 1, 0);
  }
  
  @SystemApi
  public int detachMixes(List<AudioMix> paramList)
  {
    if (paramList != null) {
      synchronized (mLock)
      {
        if (mStatus == 2)
        {
          ArrayList localArrayList = new java/util/ArrayList;
          localArrayList.<init>(paramList.size());
          paramList = paramList.iterator();
          while (paramList.hasNext())
          {
            localObject2 = (AudioMix)paramList.next();
            if (localObject2 != null)
            {
              localArrayList.add(localObject2);
            }
            else
            {
              paramList = new java/lang/IllegalArgumentException;
              paramList.<init>("Illegal null AudioMix in detachMixes");
              throw paramList;
            }
          }
          Object localObject2 = new android/media/audiopolicy/AudioPolicyConfig;
          ((AudioPolicyConfig)localObject2).<init>(localArrayList);
          paramList = getService();
          try
          {
            int i = paramList.removeMixForPolicy((AudioPolicyConfig)localObject2, cb());
            if (i == 0) {
              mConfig.remove(localArrayList);
            }
            return i;
          }
          catch (RemoteException paramList)
          {
            Log.e("AudioPolicy", "Dead object in detachMixes", paramList);
            return -1;
          }
        }
        paramList = new java/lang/IllegalStateException;
        paramList.<init>("Cannot alter unregistered AudioPolicy");
        throw paramList;
      }
    }
    throw new IllegalArgumentException("Illegal null list of AudioMix");
  }
  
  public AudioPolicyConfig getConfig()
  {
    return mConfig;
  }
  
  @SystemApi
  public int getFocusDuckingBehavior()
  {
    return mConfig.mDuckingPolicy;
  }
  
  @SystemApi
  public int getStatus()
  {
    return mStatus;
  }
  
  public boolean hasFocusListener()
  {
    boolean bool;
    if (mFocusListener != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isFocusPolicy()
  {
    return mIsFocusPolicy;
  }
  
  public boolean isVolumeController()
  {
    boolean bool;
    if (mVolCb != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @SystemApi
  public int setFocusDuckingBehavior(int paramInt)
    throws IllegalArgumentException, IllegalStateException
  {
    if ((paramInt != 0) && (paramInt != 1))
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("Invalid ducking behavior ");
      ((StringBuilder)???).append(paramInt);
      throw new IllegalArgumentException(((StringBuilder)???).toString());
    }
    synchronized (mLock)
    {
      if (mStatus == 2)
      {
        if ((paramInt == 1) && (mFocusListener == null))
        {
          localObject2 = new java/lang/IllegalStateException;
          ((IllegalStateException)localObject2).<init>("Cannot handle ducking without an audio focus listener");
          throw ((Throwable)localObject2);
        }
        Object localObject2 = getService();
        try
        {
          int i = ((IAudioService)localObject2).setFocusPropertiesForPolicy(paramInt, cb());
          if (i == 0) {
            mConfig.mDuckingPolicy = paramInt;
          }
          return i;
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("AudioPolicy", "Dead object in setFocusPropertiesForPolicy for behavior", localRemoteException);
          return -1;
        }
      }
      IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
      localIllegalStateException.<init>("Cannot change ducking behavior for unregistered policy");
      throw localIllegalStateException;
    }
  }
  
  public void setRegistration(String paramString)
  {
    synchronized (mLock)
    {
      mRegistrationId = paramString;
      mConfig.setRegistration(paramString);
      if (paramString != null) {
        mStatus = 2;
      } else {
        mStatus = 1;
      }
      sendMsg(0);
      return;
    }
  }
  
  public String toLogFriendlyString()
  {
    String str = new String("android.media.audiopolicy.AudioPolicy:\n");
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(str);
    localStringBuilder.append("config=");
    localStringBuilder.append(mConfig.toLogFriendlyString());
    return localStringBuilder.toString();
  }
  
  @SystemApi
  public static abstract class AudioPolicyFocusListener
  {
    public AudioPolicyFocusListener() {}
    
    public void onAudioFocusAbandon(AudioFocusInfo paramAudioFocusInfo) {}
    
    public void onAudioFocusGrant(AudioFocusInfo paramAudioFocusInfo, int paramInt) {}
    
    public void onAudioFocusLoss(AudioFocusInfo paramAudioFocusInfo, boolean paramBoolean) {}
    
    public void onAudioFocusRequest(AudioFocusInfo paramAudioFocusInfo, int paramInt) {}
  }
  
  @SystemApi
  public static abstract class AudioPolicyStatusListener
  {
    public AudioPolicyStatusListener() {}
    
    public void onMixStateUpdate(AudioMix paramAudioMix) {}
    
    public void onStatusChange() {}
  }
  
  @SystemApi
  public static abstract class AudioPolicyVolumeCallback
  {
    public AudioPolicyVolumeCallback() {}
    
    public void onVolumeAdjustment(int paramInt) {}
  }
  
  @SystemApi
  public static class Builder
  {
    private Context mContext;
    private AudioPolicy.AudioPolicyFocusListener mFocusListener;
    private boolean mIsFocusPolicy = false;
    private Looper mLooper;
    private ArrayList<AudioMix> mMixes = new ArrayList();
    private AudioPolicy.AudioPolicyStatusListener mStatusListener;
    private AudioPolicy.AudioPolicyVolumeCallback mVolCb;
    
    @SystemApi
    public Builder(Context paramContext)
    {
      mContext = paramContext;
    }
    
    @SystemApi
    public Builder addMix(AudioMix paramAudioMix)
      throws IllegalArgumentException
    {
      if (paramAudioMix != null)
      {
        mMixes.add(paramAudioMix);
        return this;
      }
      throw new IllegalArgumentException("Illegal null AudioMix argument");
    }
    
    @SystemApi
    public AudioPolicy build()
    {
      if (mStatusListener != null)
      {
        Iterator localIterator = mMixes.iterator();
        while (localIterator.hasNext())
        {
          AudioMix localAudioMix = (AudioMix)localIterator.next();
          mCallbackFlags |= 0x1;
        }
      }
      if ((mIsFocusPolicy) && (mFocusListener == null)) {
        throw new IllegalStateException("Cannot be a focus policy without an AudioPolicyFocusListener");
      }
      return new AudioPolicy(new AudioPolicyConfig(mMixes), mContext, mLooper, mFocusListener, mStatusListener, mIsFocusPolicy, mVolCb, null);
    }
    
    @SystemApi
    public void setAudioPolicyFocusListener(AudioPolicy.AudioPolicyFocusListener paramAudioPolicyFocusListener)
    {
      mFocusListener = paramAudioPolicyFocusListener;
    }
    
    @SystemApi
    public void setAudioPolicyStatusListener(AudioPolicy.AudioPolicyStatusListener paramAudioPolicyStatusListener)
    {
      mStatusListener = paramAudioPolicyStatusListener;
    }
    
    @SystemApi
    public Builder setAudioPolicyVolumeCallback(AudioPolicy.AudioPolicyVolumeCallback paramAudioPolicyVolumeCallback)
    {
      if (paramAudioPolicyVolumeCallback != null)
      {
        mVolCb = paramAudioPolicyVolumeCallback;
        return this;
      }
      throw new IllegalArgumentException("Invalid null volume callback");
    }
    
    @SystemApi
    public Builder setIsAudioFocusPolicy(boolean paramBoolean)
    {
      mIsFocusPolicy = paramBoolean;
      return this;
    }
    
    @SystemApi
    public Builder setLooper(Looper paramLooper)
      throws IllegalArgumentException
    {
      if (paramLooper != null)
      {
        mLooper = paramLooper;
        return this;
      }
      throw new IllegalArgumentException("Illegal null Looper argument");
    }
  }
  
  private class EventHandler
    extends Handler
  {
    public EventHandler(AudioPolicy paramAudioPolicy, Looper paramLooper)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      Object localObject;
      switch (what)
      {
      default: 
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Unknown event ");
        ((StringBuilder)localObject).append(what);
        Log.e("AudioPolicy", ((StringBuilder)localObject).toString());
        break;
      case 6: 
        if (mVolCb != null) {
          mVolCb.onVolumeAdjustment(arg1);
        } else {
          Log.e("AudioPolicy", "Invalid null volume event");
        }
        break;
      case 5: 
        if (mFocusListener != null) {
          mFocusListener.onAudioFocusAbandon((AudioFocusInfo)obj);
        } else {
          Log.e("AudioPolicy", "Invalid null focus listener for focus abandon event");
        }
        break;
      case 4: 
        if (mFocusListener != null) {
          mFocusListener.onAudioFocusRequest((AudioFocusInfo)obj, arg1);
        } else {
          Log.e("AudioPolicy", "Invalid null focus listener for focus request event");
        }
        break;
      case 3: 
        if (mStatusListener != null) {
          mStatusListener.onMixStateUpdate((AudioMix)obj);
        }
        break;
      case 2: 
        if (mFocusListener != null)
        {
          AudioPolicy.AudioPolicyFocusListener localAudioPolicyFocusListener = mFocusListener;
          localObject = (AudioFocusInfo)obj;
          boolean bool;
          if (arg1 != 0) {
            bool = true;
          } else {
            bool = false;
          }
          localAudioPolicyFocusListener.onAudioFocusLoss((AudioFocusInfo)localObject, bool);
        }
        break;
      case 1: 
        if (mFocusListener != null) {
          mFocusListener.onAudioFocusGrant((AudioFocusInfo)obj, arg1);
        }
        break;
      case 0: 
        AudioPolicy.this.onPolicyStatusChange();
      }
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface PolicyStatus {}
}
