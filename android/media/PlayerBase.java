package android.media;

import android.app.ActivityThread;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.server.notification.ZenModeUtils;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.app.IAppOpsCallback;
import com.android.internal.app.IAppOpsCallback.Stub;
import com.android.internal.app.IAppOpsService;
import com.android.internal.app.IAppOpsService.Stub;
import java.lang.ref.WeakReference;
import java.util.Objects;

public abstract class PlayerBase
{
  public static final String ACTION_SURROUND_AUDIO_DETECTED = "action.SURROUND_AUDIO_DETECTED";
  private static final boolean DEBUG = DEBUG_DND;
  private static final boolean DEBUG_APP_OPS = false;
  private static final boolean DEBUG_DND = Log.isLoggable("PlayerBase", 3);
  private static final String TAG = "PlayerBase";
  public static final String TELECOM_ASUS_AIVOLUME_FIRST_TIME_VOLUME_ONE_FALSE = "false";
  public static final String TELECOM_ASUS_AIVOLUME_FIRST_TIME_VOLUME_ONE_SYSPROP = "persist.vendor.asus.aivolsetone";
  public static final String TELECOM_ASUS_AIVOLUME_FIRST_TIME_VOLUME_ONE_TRUE = "true";
  public static final int TELECOM_ASUS_AIVOLUME_STATUS_OFF = 0;
  public static final int TELECOM_ASUS_AIVOLUME_STATUS_ON = 1;
  public static final String TELECOM_ASUS_AIVOLUME_STATUS_SYSPROP = "persist.vendor.asus.aivolstatus";
  private static IAudioService sService;
  private IAppOpsService mAppOps;
  private IAppOpsCallback mAppOpsCallback;
  protected AudioAttributes mAttributes;
  protected float mAuxEffectSendLevel = 0.0F;
  @GuardedBy("mLock")
  private boolean mHasAppOpsPlayAudio = true;
  private final int mImplType;
  protected float mLeftVolume = 1.0F;
  private final Object mLock = new Object();
  @GuardedBy("mLock")
  private float mPanMultiplierL = 1.0F;
  @GuardedBy("mLock")
  private float mPanMultiplierR = 1.0F;
  private int mPlayerIId = 0;
  protected float mRightVolume = 1.0F;
  @GuardedBy("mLock")
  private int mStartDelayMs = 0;
  @GuardedBy("mLock")
  private int mState;
  
  PlayerBase(AudioAttributes paramAudioAttributes, int paramInt)
  {
    if (paramAudioAttributes != null)
    {
      mAttributes = paramAudioAttributes;
      mImplType = paramInt;
      mState = 1;
      return;
    }
    throw new IllegalArgumentException("Illegal null AudioAttributes");
  }
  
  public static void deprecateStreamTypeForPlayback(int paramInt, String paramString1, String paramString2)
    throws IllegalArgumentException
  {
    if (paramInt != 10)
    {
      Log.w(paramString1, "Use of stream types is deprecated for operations other than volume control");
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("See the documentation of ");
      localStringBuilder.append(paramString2);
      localStringBuilder.append(" for what to use instead with android.media.AudioAttributes to qualify your playback use case");
      Log.w(paramString1, localStringBuilder.toString());
      return;
    }
    throw new IllegalArgumentException("Use of STREAM_ACCESSIBILITY is reserved for volume control");
  }
  
  private static IAudioService getService()
  {
    if (sService != null) {
      return sService;
    }
    sService = IAudioService.Stub.asInterface(ServiceManager.getService("audio"));
    return sService;
  }
  
  private void notifyAroundAudioDetected(Application paramApplication, int paramInt)
  {
    if (paramApplication != null)
    {
      Intent localIntent = new Intent();
      localIntent.setAction("action.SURROUND_AUDIO_DETECTED");
      localIntent.putExtra("defMusicVol", Integer.toString(paramInt));
      localIntent.putExtra("applicationName", paramApplication.getPackageName());
      localIntent.setFlags(268435456);
      paramApplication.sendBroadcast(localIntent);
    }
  }
  
  private void updateAppOpsPlayAudio()
  {
    synchronized (mLock)
    {
      updateAppOpsPlayAudio_sync(false);
      return;
    }
  }
  
  private void updateState(int paramInt)
  {
    synchronized (mLock)
    {
      mState = paramInt;
      int i = mPlayerIId;
      try
      {
        getService().playerEvent(i, paramInt);
      }
      catch (RemoteException localRemoteException)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Error talking to audio service, ");
        localStringBuilder.append(AudioPlaybackConfiguration.toLogFriendlyPlayerState(paramInt));
        localStringBuilder.append(" state will not be tracked for piid=");
        localStringBuilder.append(i);
        Log.e("PlayerBase", localStringBuilder.toString(), localRemoteException);
      }
      return;
    }
  }
  
  void basePause()
  {
    if (DEBUG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("basePause() piid=");
      localStringBuilder.append(mPlayerIId);
      Log.v("PlayerBase", localStringBuilder.toString());
    }
    updateState(3);
  }
  
  protected void baseRegisterPlayer()
  {
    int i = -1;
    mAppOps = IAppOpsService.Stub.asInterface(ServiceManager.getService("appops"));
    updateAppOpsPlayAudio();
    mAppOpsCallback = new IAppOpsCallbackWrapper(this);
    try
    {
      mAppOps.startWatchingMode(28, ActivityThread.currentPackageName(), mAppOpsCallback);
    }
    catch (RemoteException localRemoteException1)
    {
      Log.e("PlayerBase", "Error registering appOps callback", localRemoteException1);
      mHasAppOpsPlayAudio = false;
    }
    try
    {
      IAudioService localIAudioService = getService();
      PlayerIdCard localPlayerIdCard = new android/media/PlayerBase$PlayerIdCard;
      int j = mImplType;
      AudioAttributes localAudioAttributes = mAttributes;
      IPlayerWrapper localIPlayerWrapper = new android/media/PlayerBase$IPlayerWrapper;
      localIPlayerWrapper.<init>(this);
      localPlayerIdCard.<init>(j, localAudioAttributes, localIPlayerWrapper);
      j = localIAudioService.trackPlayer(localPlayerIdCard);
      i = j;
    }
    catch (RemoteException localRemoteException2)
    {
      Log.e("PlayerBase", "Error talking to audio service, player will not be tracked", localRemoteException2);
    }
    mPlayerIId = i;
  }
  
  void baseRelease()
  {
    if (DEBUG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("baseRelease() piid=");
      localStringBuilder.append(mPlayerIId);
      localStringBuilder.append(" state=");
      localStringBuilder.append(mState);
      Log.v("PlayerBase", localStringBuilder.toString());
    }
    int i = 0;
    synchronized (mLock)
    {
      if (mState != 0)
      {
        i = 1;
        mState = 0;
      }
      if (i != 0) {
        try
        {
          getService().releasePlayer(mPlayerIId);
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("PlayerBase", "Error talking to audio service, the player will still be tracked", localRemoteException);
        }
      }
      try
      {
        if (mAppOps != null) {
          mAppOps.stopWatchingMode(mAppOpsCallback);
        }
      }
      catch (Exception localException) {}
      return;
    }
  }
  
  int baseSetAuxEffectSendLevel(float paramFloat)
  {
    synchronized (mLock)
    {
      mAuxEffectSendLevel = paramFloat;
      if (isRestricted_sync()) {
        return 0;
      }
      return playerSetAuxEffectSendLevel(false, paramFloat);
    }
  }
  
  void baseSetPan(float paramFloat)
  {
    paramFloat = Math.min(Math.max(-1.0F, paramFloat), 1.0F);
    Object localObject1 = mLock;
    if (paramFloat >= 0.0F)
    {
      try
      {
        mPanMultiplierL = (1.0F - paramFloat);
        mPanMultiplierR = 1.0F;
      }
      finally
      {
        break label71;
      }
    }
    else
    {
      mPanMultiplierL = 1.0F;
      mPanMultiplierR = (1.0F + paramFloat);
    }
    baseSetVolume(mLeftVolume, mRightVolume);
    return;
    label71:
    throw localObject2;
  }
  
  void baseSetStartDelayMs(int paramInt)
  {
    synchronized (mLock)
    {
      mStartDelayMs = Math.max(paramInt, 0);
      return;
    }
  }
  
  void baseSetVolume(float paramFloat1, float paramFloat2)
  {
    synchronized (mLock)
    {
      mLeftVolume = paramFloat1;
      mRightVolume = paramFloat2;
      boolean bool = isRestricted_sync();
      playerSetVolume(bool, mPanMultiplierL * paramFloat1, mPanMultiplierR * paramFloat2);
      return;
    }
  }
  
  void baseStart()
  {
    if (DEBUG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("baseStart() piid=");
      localStringBuilder.append(mPlayerIId);
      Log.v("PlayerBase", localStringBuilder.toString());
    }
    updateState(2);
    synchronized (mLock)
    {
      if (isRestricted_sync()) {
        playerSetVolume(true, 0.0F, 0.0F);
      }
      return;
    }
  }
  
  void baseStop()
  {
    if (DEBUG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("baseStop() piid=");
      localStringBuilder.append(mPlayerIId);
      Log.v("PlayerBase", localStringBuilder.toString());
    }
    updateState(4);
  }
  
  void baseUpdateAudioAttributes(AudioAttributes paramAudioAttributes)
  {
    if (paramAudioAttributes != null)
    {
      try
      {
        getService().playerAttributes(mPlayerIId, paramAudioAttributes);
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("PlayerBase", "Error talking to audio service, STARTED state will not be tracked", localRemoteException);
      }
      synchronized (mLock)
      {
        boolean bool;
        if (mAttributes != paramAudioAttributes) {
          bool = true;
        } else {
          bool = false;
        }
        mAttributes = paramAudioAttributes;
        updateAppOpsPlayAudio_sync(bool);
        return;
      }
    }
    throw new IllegalArgumentException("Illegal null AudioAttributes");
  }
  
  protected void executeAIVolume(String paramString)
  {
    int i = SystemProperties.getInt("persist.vendor.asus.aivolstatus", 0);
    String str = SystemProperties.get("persist.vendor.asus.aivolsetone", "false");
    Application localApplication = ActivityThread.currentApplication();
    Object localObject = (AudioManager)localApplication.getSystemService("audio");
    int j = ((AudioManager)localObject).getStreamVolume(3);
    if ((i == 1) && (j > 0))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("executeAIVolume(): fileTag = ");
      localStringBuilder.append(paramString);
      Log.d("PlayerBase", localStringBuilder.toString());
      if (localApplication != null) {
        try
        {
          paramString = new java/lang/StringBuilder;
          paramString.<init>();
          paramString.append("executeAIVolume(): piid=");
          paramString.append(mPlayerIId);
          paramString.append(", ");
          paramString.append(localApplication.getPackageName());
          Log.v("PlayerBase", paramString.toString());
          if (str.equals("true")) {
            ((AudioManager)localObject).setStreamVolume(3, 1, 0);
          }
          notifyAroundAudioDetected(localApplication, j);
          Log.v("PlayerBase", "executeAIVolume(): sendBroadcast");
        }
        catch (Exception paramString)
        {
          break label197;
        }
      } else {
        Log.d("PlayerBase", "executeAIVolume(): application = null");
      }
      return;
      label197:
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("executeAIVolume: ");
      ((StringBuilder)localObject).append(paramString);
      Log.e("PlayerBase", ((StringBuilder)localObject).toString());
    }
  }
  
  protected int getStartDelayMs()
  {
    synchronized (mLock)
    {
      int i = mStartDelayMs;
      return i;
    }
  }
  
  boolean isRestricted_sync()
  {
    if (mHasAppOpsPlayAudio) {
      return false;
    }
    if ((mAttributes.getAllFlags() & 0x40) != 0) {
      return false;
    }
    if (((mAttributes.getAllFlags() & 0x1) != 0) && (mAttributes.getUsage() == 13))
    {
      boolean bool1 = false;
      boolean bool2 = false;
      boolean bool3;
      try
      {
        bool3 = getService().isCameraSoundForced();
      }
      catch (NullPointerException localNullPointerException)
      {
        Log.e("PlayerBase", "Null AudioService in isRestricted_sync()");
        bool3 = bool1;
      }
      catch (RemoteException localRemoteException)
      {
        for (;;)
        {
          Log.e("PlayerBase", "Cannot access AudioService in isRestricted_sync()");
          bool3 = bool2;
        }
      }
      if (bool3) {
        return false;
      }
    }
    return true;
  }
  
  abstract boolean isStreamSystemEnforcedMute();
  
  abstract int playerApplyVolumeShaper(VolumeShaper.Configuration paramConfiguration, VolumeShaper.Operation paramOperation);
  
  abstract VolumeShaper.State playerGetVolumeShaperState(int paramInt);
  
  abstract void playerPause();
  
  abstract int playerSetAuxEffectSendLevel(boolean paramBoolean, float paramFloat);
  
  abstract void playerSetVolume(boolean paramBoolean, float paramFloat1, float paramFloat2);
  
  abstract void playerStart();
  
  abstract void playerStop();
  
  public void setStartDelayMs(int paramInt)
  {
    baseSetStartDelayMs(paramInt);
  }
  
  void updateAppOpsPlayAudio_sync(boolean paramBoolean)
  {
    boolean bool1 = mHasAppOpsPlayAudio;
    int i = 1;
    try
    {
      if (mAppOps != null) {
        i = mAppOps.checkAudioOperation(28, mAttributes.getUsage(), Process.myUid(), ActivityThread.currentPackageName());
      }
      boolean bool2;
      if (i == 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      mHasAppOpsPlayAudio = bool2;
      Application localApplication = ActivityThread.currentApplication();
      if ((localApplication != null) && (localApplication.getPackageManager() != null) && (localApplication.getPackageManager().hasSystemFeature("asus.software.dnd.unrestricted"))) {
        if ((mAttributes.getUsage() == 9527) && (ZenModeUtils.checkSystemApp(Process.myUid())))
        {
          if (DEBUG) {
            Log.i("PlayerBase", "set mHasAppOpsPlayAudio = true, because is system urgent sound");
          }
          mHasAppOpsPlayAudio = true;
        }
        else if (((mAttributes.getFlags() & 0x1) != 0) && (mAttributes.getContentType() == 4) && (mHasAppOpsPlayAudio) && (isStreamSystemEnforcedMute()))
        {
          if (DEBUG) {
            Log.i("PlayerBase", "set mHasAppOpsPlayAudio = false, because need mute");
          }
          mHasAppOpsPlayAudio = false;
        }
      }
    }
    catch (RemoteException localRemoteException)
    {
      mHasAppOpsPlayAudio = false;
    }
    try
    {
      if ((bool1 != mHasAppOpsPlayAudio) || (paramBoolean))
      {
        getService().playerHasOpPlayAudio(mPlayerIId, mHasAppOpsPlayAudio);
        if (!isRestricted_sync())
        {
          playerSetVolume(false, mLeftVolume * mPanMultiplierL, mRightVolume * mPanMultiplierR);
          playerSetAuxEffectSendLevel(false, mAuxEffectSendLevel);
        }
        else
        {
          playerSetVolume(true, 0.0F, 0.0F);
          playerSetAuxEffectSendLevel(true, 0.0F);
        }
      }
    }
    catch (Exception localException) {}
  }
  
  private static class IAppOpsCallbackWrapper
    extends IAppOpsCallback.Stub
  {
    private final WeakReference<PlayerBase> mWeakPB;
    
    public IAppOpsCallbackWrapper(PlayerBase paramPlayerBase)
    {
      mWeakPB = new WeakReference(paramPlayerBase);
    }
    
    public void opChanged(int paramInt1, int paramInt2, String paramString)
    {
      if (paramInt1 == 28)
      {
        paramString = (PlayerBase)mWeakPB.get();
        if (paramString != null) {
          paramString.updateAppOpsPlayAudio();
        }
      }
    }
  }
  
  private static class IPlayerWrapper
    extends IPlayer.Stub
  {
    private final WeakReference<PlayerBase> mWeakPB;
    
    public IPlayerWrapper(PlayerBase paramPlayerBase)
    {
      mWeakPB = new WeakReference(paramPlayerBase);
    }
    
    public void applyVolumeShaper(VolumeShaper.Configuration paramConfiguration, VolumeShaper.Operation paramOperation)
    {
      PlayerBase localPlayerBase = (PlayerBase)mWeakPB.get();
      if (localPlayerBase != null) {
        localPlayerBase.playerApplyVolumeShaper(paramConfiguration, paramOperation);
      }
    }
    
    public void pause()
    {
      PlayerBase localPlayerBase = (PlayerBase)mWeakPB.get();
      if (localPlayerBase != null) {
        localPlayerBase.playerPause();
      }
    }
    
    public void setPan(float paramFloat)
    {
      PlayerBase localPlayerBase = (PlayerBase)mWeakPB.get();
      if (localPlayerBase != null) {
        localPlayerBase.baseSetPan(paramFloat);
      }
    }
    
    public void setStartDelayMs(int paramInt)
    {
      PlayerBase localPlayerBase = (PlayerBase)mWeakPB.get();
      if (localPlayerBase != null) {
        localPlayerBase.baseSetStartDelayMs(paramInt);
      }
    }
    
    public void setVolume(float paramFloat)
    {
      PlayerBase localPlayerBase = (PlayerBase)mWeakPB.get();
      if (localPlayerBase != null) {
        localPlayerBase.baseSetVolume(paramFloat, paramFloat);
      }
    }
    
    public void start()
    {
      PlayerBase localPlayerBase = (PlayerBase)mWeakPB.get();
      if (localPlayerBase != null) {
        localPlayerBase.playerStart();
      }
    }
    
    public void stop()
    {
      PlayerBase localPlayerBase = (PlayerBase)mWeakPB.get();
      if (localPlayerBase != null) {
        localPlayerBase.playerStop();
      }
    }
  }
  
  public static class PlayerIdCard
    implements Parcelable
  {
    public static final int AUDIO_ATTRIBUTES_DEFINED = 1;
    public static final int AUDIO_ATTRIBUTES_NONE = 0;
    public static final Parcelable.Creator<PlayerIdCard> CREATOR = new Parcelable.Creator()
    {
      public PlayerBase.PlayerIdCard createFromParcel(Parcel paramAnonymousParcel)
      {
        return new PlayerBase.PlayerIdCard(paramAnonymousParcel, null);
      }
      
      public PlayerBase.PlayerIdCard[] newArray(int paramAnonymousInt)
      {
        return new PlayerBase.PlayerIdCard[paramAnonymousInt];
      }
    };
    public final AudioAttributes mAttributes;
    public final IPlayer mIPlayer;
    public final int mPlayerType;
    
    PlayerIdCard(int paramInt, AudioAttributes paramAudioAttributes, IPlayer paramIPlayer)
    {
      mPlayerType = paramInt;
      mAttributes = paramAudioAttributes;
      mIPlayer = paramIPlayer;
    }
    
    private PlayerIdCard(Parcel paramParcel)
    {
      mPlayerType = paramParcel.readInt();
      mAttributes = ((AudioAttributes)AudioAttributes.CREATOR.createFromParcel(paramParcel));
      paramParcel = paramParcel.readStrongBinder();
      if (paramParcel == null) {
        paramParcel = null;
      } else {
        paramParcel = IPlayer.Stub.asInterface(paramParcel);
      }
      mIPlayer = paramParcel;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool = true;
      if (this == paramObject) {
        return true;
      }
      if ((paramObject != null) && ((paramObject instanceof PlayerIdCard)))
      {
        paramObject = (PlayerIdCard)paramObject;
        if ((mPlayerType != mPlayerType) || (!mAttributes.equals(mAttributes))) {
          bool = false;
        }
        return bool;
      }
      return false;
    }
    
    public int hashCode()
    {
      return Objects.hash(new Object[] { Integer.valueOf(mPlayerType) });
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(mPlayerType);
      mAttributes.writeToParcel(paramParcel, 0);
      IBinder localIBinder;
      if (mIPlayer == null) {
        localIBinder = null;
      } else {
        localIBinder = mIPlayer.asBinder();
      }
      paramParcel.writeStrongBinder(localIBinder);
    }
  }
}
