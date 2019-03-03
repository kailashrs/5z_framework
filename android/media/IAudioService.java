package android.media;

import android.bluetooth.BluetoothDevice;
import android.media.audiopolicy.AudioPolicyConfig;
import android.media.audiopolicy.IAudioPolicyCallback;
import android.media.audiopolicy.IAudioPolicyCallback.Stub;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

public abstract interface IAudioService
  extends IInterface
{
  public abstract int abandonAudioFocus(IAudioFocusDispatcher paramIAudioFocusDispatcher, String paramString1, AudioAttributes paramAudioAttributes, String paramString2)
    throws RemoteException;
  
  public abstract int addMixForPolicy(AudioPolicyConfig paramAudioPolicyConfig, IAudioPolicyCallback paramIAudioPolicyCallback)
    throws RemoteException;
  
  public abstract void adjustStreamVolume(int paramInt1, int paramInt2, int paramInt3, String paramString)
    throws RemoteException;
  
  public abstract void adjustSuggestedStreamVolume(int paramInt1, int paramInt2, int paramInt3, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void avrcpSupportsAbsoluteVolume(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void disableRingtoneSync(int paramInt)
    throws RemoteException;
  
  public abstract void disableSafeMediaVolume(String paramString)
    throws RemoteException;
  
  public abstract int dispatchFocusChange(AudioFocusInfo paramAudioFocusInfo, int paramInt, IAudioPolicyCallback paramIAudioPolicyCallback)
    throws RemoteException;
  
  public abstract void forceRemoteSubmixFullVolume(boolean paramBoolean, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void forceVolumeControlStream(int paramInt, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract List<AudioPlaybackConfiguration> getActivePlaybackConfigurations()
    throws RemoteException;
  
  public abstract List<AudioRecordingConfiguration> getActiveRecordingConfigurations()
    throws RemoteException;
  
  public abstract int getCurrentAudioFocus()
    throws RemoteException;
  
  public abstract int getFocusRampTimeMs(int paramInt, AudioAttributes paramAudioAttributes)
    throws RemoteException;
  
  public abstract int getLastAudibleStreamVolume(int paramInt)
    throws RemoteException;
  
  public abstract int getMode()
    throws RemoteException;
  
  public abstract int getRingerModeExternal()
    throws RemoteException;
  
  public abstract int getRingerModeInternal()
    throws RemoteException;
  
  public abstract IRingtonePlayer getRingtonePlayer()
    throws RemoteException;
  
  public abstract int getStreamMaxVolume(int paramInt)
    throws RemoteException;
  
  public abstract int getStreamMinVolume(int paramInt)
    throws RemoteException;
  
  public abstract int getStreamVolume(int paramInt)
    throws RemoteException;
  
  public abstract int getUiSoundsStreamType()
    throws RemoteException;
  
  public abstract int getVibrateSetting(int paramInt)
    throws RemoteException;
  
  public abstract void handleBluetoothA2dpDeviceConfigChange(BluetoothDevice paramBluetoothDevice)
    throws RemoteException;
  
  public abstract boolean isAudioServerRunning()
    throws RemoteException;
  
  public abstract boolean isBluetoothA2dpOn()
    throws RemoteException;
  
  public abstract boolean isBluetoothScoOn()
    throws RemoteException;
  
  public abstract boolean isCameraSoundForced()
    throws RemoteException;
  
  public abstract boolean isHdmiSystemAudioSupported()
    throws RemoteException;
  
  public abstract boolean isMasterMute()
    throws RemoteException;
  
  public abstract boolean isSpeakerphoneOn()
    throws RemoteException;
  
  public abstract boolean isStreamAffectedByMute(int paramInt)
    throws RemoteException;
  
  public abstract boolean isStreamAffectedByRingerMode(int paramInt)
    throws RemoteException;
  
  public abstract boolean isStreamMute(int paramInt)
    throws RemoteException;
  
  public abstract boolean isValidRingerMode(int paramInt)
    throws RemoteException;
  
  public abstract boolean loadSoundEffects()
    throws RemoteException;
  
  public abstract void notifyVolumeControllerVisible(IVolumeController paramIVolumeController, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void playSoundEffect(int paramInt)
    throws RemoteException;
  
  public abstract void playSoundEffectVolume(int paramInt, float paramFloat)
    throws RemoteException;
  
  public abstract void playerAttributes(int paramInt, AudioAttributes paramAudioAttributes)
    throws RemoteException;
  
  public abstract void playerEvent(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void playerHasOpPlayAudio(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract String registerAudioPolicy(AudioPolicyConfig paramAudioPolicyConfig, IAudioPolicyCallback paramIAudioPolicyCallback, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
    throws RemoteException;
  
  public abstract void registerAudioServerStateDispatcher(IAudioServerStateDispatcher paramIAudioServerStateDispatcher)
    throws RemoteException;
  
  public abstract void registerPlaybackCallback(IPlaybackConfigDispatcher paramIPlaybackConfigDispatcher)
    throws RemoteException;
  
  public abstract void registerRecordingCallback(IRecordingConfigDispatcher paramIRecordingConfigDispatcher)
    throws RemoteException;
  
  public abstract void releasePlayer(int paramInt)
    throws RemoteException;
  
  public abstract void reloadAudioSettings()
    throws RemoteException;
  
  public abstract int removeMixForPolicy(AudioPolicyConfig paramAudioPolicyConfig, IAudioPolicyCallback paramIAudioPolicyCallback)
    throws RemoteException;
  
  public abstract int requestAudioFocus(AudioAttributes paramAudioAttributes, int paramInt1, IBinder paramIBinder, IAudioFocusDispatcher paramIAudioFocusDispatcher, String paramString1, String paramString2, int paramInt2, IAudioPolicyCallback paramIAudioPolicyCallback, int paramInt3)
    throws RemoteException;
  
  public abstract void setAudioWizardForcePresetState(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract int setBluetoothA2dpDeviceConnectionState(BluetoothDevice paramBluetoothDevice, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract int setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent(BluetoothDevice paramBluetoothDevice, int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3)
    throws RemoteException;
  
  public abstract void setBluetoothA2dpOn(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setBluetoothScoOn(boolean paramBoolean)
    throws RemoteException;
  
  public abstract int setFocusPropertiesForPolicy(int paramInt, IAudioPolicyCallback paramIAudioPolicyCallback)
    throws RemoteException;
  
  public abstract void setFocusRequestResultFromExtPolicy(AudioFocusInfo paramAudioFocusInfo, int paramInt, IAudioPolicyCallback paramIAudioPolicyCallback)
    throws RemoteException;
  
  public abstract int setHdmiSystemAudioSupported(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setHearingAidDeviceConnectionState(BluetoothDevice paramBluetoothDevice, int paramInt)
    throws RemoteException;
  
  public abstract void setMasterMute(boolean paramBoolean, int paramInt1, String paramString, int paramInt2)
    throws RemoteException;
  
  public abstract void setMicrophoneMute(boolean paramBoolean, String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void setMode(int paramInt, IBinder paramIBinder, String paramString)
    throws RemoteException;
  
  public abstract void setRingerModeExternal(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void setRingerModeInternal(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void setRingtonePlayer(IRingtonePlayer paramIRingtonePlayer)
    throws RemoteException;
  
  public abstract void setSpeakerphoneOn(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setStreamVolume(int paramInt1, int paramInt2, int paramInt3, String paramString)
    throws RemoteException;
  
  public abstract void setVibrateSetting(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setVolumeController(IVolumeController paramIVolumeController)
    throws RemoteException;
  
  public abstract void setVolumePolicy(VolumePolicy paramVolumePolicy)
    throws RemoteException;
  
  public abstract void setWiredDeviceConnectionState(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3)
    throws RemoteException;
  
  public abstract boolean shouldVibrate(int paramInt)
    throws RemoteException;
  
  public abstract void startBluetoothSco(IBinder paramIBinder, int paramInt)
    throws RemoteException;
  
  public abstract void startBluetoothScoVirtualCall(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract AudioRoutesInfo startWatchingRoutes(IAudioRoutesObserver paramIAudioRoutesObserver)
    throws RemoteException;
  
  public abstract void stopBluetoothSco(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract int trackPlayer(PlayerBase.PlayerIdCard paramPlayerIdCard)
    throws RemoteException;
  
  public abstract void unloadSoundEffects()
    throws RemoteException;
  
  public abstract void unregisterAudioFocusClient(String paramString)
    throws RemoteException;
  
  public abstract void unregisterAudioPolicyAsync(IAudioPolicyCallback paramIAudioPolicyCallback)
    throws RemoteException;
  
  public abstract void unregisterAudioServerStateDispatcher(IAudioServerStateDispatcher paramIAudioServerStateDispatcher)
    throws RemoteException;
  
  public abstract void unregisterPlaybackCallback(IPlaybackConfigDispatcher paramIPlaybackConfigDispatcher)
    throws RemoteException;
  
  public abstract void unregisterRecordingCallback(IRecordingConfigDispatcher paramIRecordingConfigDispatcher)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAudioService
  {
    private static final String DESCRIPTOR = "android.media.IAudioService";
    static final int TRANSACTION_abandonAudioFocus = 40;
    static final int TRANSACTION_addMixForPolicy = 65;
    static final int TRANSACTION_adjustStreamVolume = 6;
    static final int TRANSACTION_adjustSuggestedStreamVolume = 5;
    static final int TRANSACTION_avrcpSupportsAbsoluteVolume = 32;
    static final int TRANSACTION_disableRingtoneSync = 75;
    static final int TRANSACTION_disableSafeMediaVolume = 60;
    static final int TRANSACTION_dispatchFocusChange = 77;
    static final int TRANSACTION_forceRemoteSubmixFullVolume = 9;
    static final int TRANSACTION_forceVolumeControlStream = 46;
    static final int TRANSACTION_getActivePlaybackConfigurations = 74;
    static final int TRANSACTION_getActiveRecordingConfigurations = 71;
    static final int TRANSACTION_getCurrentAudioFocus = 42;
    static final int TRANSACTION_getFocusRampTimeMs = 76;
    static final int TRANSACTION_getLastAudibleStreamVolume = 15;
    static final int TRANSACTION_getMode = 26;
    static final int TRANSACTION_getRingerModeExternal = 19;
    static final int TRANSACTION_getRingerModeInternal = 20;
    static final int TRANSACTION_getRingtonePlayer = 48;
    static final int TRANSACTION_getStreamMaxVolume = 14;
    static final int TRANSACTION_getStreamMinVolume = 13;
    static final int TRANSACTION_getStreamVolume = 12;
    static final int TRANSACTION_getUiSoundsStreamType = 49;
    static final int TRANSACTION_getVibrateSetting = 23;
    static final int TRANSACTION_handleBluetoothA2dpDeviceConfigChange = 53;
    static final int TRANSACTION_isAudioServerRunning = 83;
    static final int TRANSACTION_isBluetoothA2dpOn = 38;
    static final int TRANSACTION_isBluetoothScoOn = 36;
    static final int TRANSACTION_isCameraSoundForced = 55;
    static final int TRANSACTION_isHdmiSystemAudioSupported = 62;
    static final int TRANSACTION_isMasterMute = 10;
    static final int TRANSACTION_isSpeakerphoneOn = 34;
    static final int TRANSACTION_isStreamAffectedByMute = 59;
    static final int TRANSACTION_isStreamAffectedByRingerMode = 58;
    static final int TRANSACTION_isStreamMute = 8;
    static final int TRANSACTION_isValidRingerMode = 21;
    static final int TRANSACTION_loadSoundEffects = 29;
    static final int TRANSACTION_notifyVolumeControllerVisible = 57;
    static final int TRANSACTION_playSoundEffect = 27;
    static final int TRANSACTION_playSoundEffectVolume = 28;
    static final int TRANSACTION_playerAttributes = 2;
    static final int TRANSACTION_playerEvent = 3;
    static final int TRANSACTION_playerHasOpPlayAudio = 78;
    static final int TRANSACTION_registerAudioPolicy = 63;
    static final int TRANSACTION_registerAudioServerStateDispatcher = 81;
    static final int TRANSACTION_registerPlaybackCallback = 72;
    static final int TRANSACTION_registerRecordingCallback = 69;
    static final int TRANSACTION_releasePlayer = 4;
    static final int TRANSACTION_reloadAudioSettings = 31;
    static final int TRANSACTION_removeMixForPolicy = 66;
    static final int TRANSACTION_requestAudioFocus = 39;
    static final int TRANSACTION_setAudioWizardForcePresetState = 84;
    static final int TRANSACTION_setBluetoothA2dpDeviceConnectionState = 52;
    static final int TRANSACTION_setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent = 79;
    static final int TRANSACTION_setBluetoothA2dpOn = 37;
    static final int TRANSACTION_setBluetoothScoOn = 35;
    static final int TRANSACTION_setFocusPropertiesForPolicy = 67;
    static final int TRANSACTION_setFocusRequestResultFromExtPolicy = 80;
    static final int TRANSACTION_setHdmiSystemAudioSupported = 61;
    static final int TRANSACTION_setHearingAidDeviceConnectionState = 51;
    static final int TRANSACTION_setMasterMute = 11;
    static final int TRANSACTION_setMicrophoneMute = 16;
    static final int TRANSACTION_setMode = 25;
    static final int TRANSACTION_setRingerModeExternal = 17;
    static final int TRANSACTION_setRingerModeInternal = 18;
    static final int TRANSACTION_setRingtonePlayer = 47;
    static final int TRANSACTION_setSpeakerphoneOn = 33;
    static final int TRANSACTION_setStreamVolume = 7;
    static final int TRANSACTION_setVibrateSetting = 22;
    static final int TRANSACTION_setVolumeController = 56;
    static final int TRANSACTION_setVolumePolicy = 68;
    static final int TRANSACTION_setWiredDeviceConnectionState = 50;
    static final int TRANSACTION_shouldVibrate = 24;
    static final int TRANSACTION_startBluetoothSco = 43;
    static final int TRANSACTION_startBluetoothScoVirtualCall = 44;
    static final int TRANSACTION_startWatchingRoutes = 54;
    static final int TRANSACTION_stopBluetoothSco = 45;
    static final int TRANSACTION_trackPlayer = 1;
    static final int TRANSACTION_unloadSoundEffects = 30;
    static final int TRANSACTION_unregisterAudioFocusClient = 41;
    static final int TRANSACTION_unregisterAudioPolicyAsync = 64;
    static final int TRANSACTION_unregisterAudioServerStateDispatcher = 82;
    static final int TRANSACTION_unregisterPlaybackCallback = 73;
    static final int TRANSACTION_unregisterRecordingCallback = 70;
    
    public Stub()
    {
      attachInterface(this, "android.media.IAudioService");
    }
    
    public static IAudioService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.media.IAudioService");
      if ((localIInterface != null) && ((localIInterface instanceof IAudioService))) {
        return (IAudioService)localIInterface;
      }
      return new Proxy(paramIBinder);
    }
    
    public IBinder asBinder()
    {
      return this;
    }
    
    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      if (paramInt1 != 1598968902)
      {
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        boolean bool4 = false;
        boolean bool5 = false;
        boolean bool6 = false;
        boolean bool7 = false;
        boolean bool8 = false;
        boolean bool9 = false;
        boolean bool10 = false;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        Object localObject7 = null;
        Object localObject8 = null;
        Object localObject9 = null;
        Object localObject10 = null;
        Object localObject11 = null;
        Object localObject12 = null;
        Object localObject13 = null;
        Object localObject14 = null;
        Object localObject15 = null;
        IAudioFocusDispatcher localIAudioFocusDispatcher = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 84: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          setAudioWizardForcePresetState(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 83: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramInt1 = isAudioServerRunning();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 82: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          unregisterAudioServerStateDispatcher(IAudioServerStateDispatcher.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 81: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          registerAudioServerStateDispatcher(IAudioServerStateDispatcher.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 80: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (AudioFocusInfo)AudioFocusInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localIAudioFocusDispatcher;
          }
          setFocusRequestResultFromExtPolicy(paramParcel2, paramParcel1.readInt(), IAudioPolicyCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 79: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          if (paramParcel1.readInt() != 0) {
            localObject12 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject12 = localObject1;
          }
          paramInt2 = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool10 = true;
          } else {
            bool10 = false;
          }
          paramInt1 = setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent((BluetoothDevice)localObject12, paramInt2, paramInt1, bool10, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 78: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool10 = true;
          }
          playerHasOpPlayAudio(paramInt1, bool10);
          return true;
        case 77: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          if (paramParcel1.readInt() != 0) {
            localObject12 = (AudioFocusInfo)AudioFocusInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject12 = localObject2;
          }
          paramInt1 = dispatchFocusChange((AudioFocusInfo)localObject12, paramParcel1.readInt(), IAudioPolicyCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 76: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (AudioAttributes)AudioAttributes.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          paramInt1 = getFocusRampTimeMs(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 75: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          disableRingtoneSync(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 74: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramParcel1 = getActivePlaybackConfigurations();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 73: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          unregisterPlaybackCallback(IPlaybackConfigDispatcher.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 72: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          registerPlaybackCallback(IPlaybackConfigDispatcher.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 71: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramParcel1 = getActiveRecordingConfigurations();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 70: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          unregisterRecordingCallback(IRecordingConfigDispatcher.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 69: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          registerRecordingCallback(IRecordingConfigDispatcher.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 68: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (VolumePolicy)VolumePolicy.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          setVolumePolicy(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 67: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramInt1 = setFocusPropertiesForPolicy(paramParcel1.readInt(), IAudioPolicyCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 66: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          if (paramParcel1.readInt() != 0) {
            localObject12 = (AudioPolicyConfig)AudioPolicyConfig.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject12 = localObject5;
          }
          paramInt1 = removeMixForPolicy((AudioPolicyConfig)localObject12, IAudioPolicyCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 65: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          if (paramParcel1.readInt() != 0) {
            localObject12 = (AudioPolicyConfig)AudioPolicyConfig.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject12 = localObject6;
          }
          paramInt1 = addMixForPolicy((AudioPolicyConfig)localObject12, IAudioPolicyCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 64: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          unregisterAudioPolicyAsync(IAudioPolicyCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 63: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          if (paramParcel1.readInt() != 0) {
            localObject12 = (AudioPolicyConfig)AudioPolicyConfig.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject12 = localObject7;
          }
          localObject11 = IAudioPolicyCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            bool10 = true;
          } else {
            bool10 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          paramParcel1 = registerAudioPolicy((AudioPolicyConfig)localObject12, (IAudioPolicyCallback)localObject11, bool10, bool1, bool2);
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 62: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramInt1 = isHdmiSystemAudioSupported();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 61: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          bool10 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool10 = true;
          }
          paramInt1 = setHdmiSystemAudioSupported(bool10);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 60: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          disableSafeMediaVolume(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 59: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramInt1 = isStreamAffectedByMute(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 58: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramInt1 = isStreamAffectedByRingerMode(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 57: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          localObject12 = IVolumeController.Stub.asInterface(paramParcel1.readStrongBinder());
          bool10 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool10 = true;
          }
          notifyVolumeControllerVisible((IVolumeController)localObject12, bool10);
          paramParcel2.writeNoException();
          return true;
        case 56: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          setVolumeController(IVolumeController.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 55: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramInt1 = isCameraSoundForced();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 54: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramParcel1 = startWatchingRoutes(IAudioRoutesObserver.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 53: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject8;
          }
          handleBluetoothA2dpDeviceConfigChange(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 52: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          if (paramParcel1.readInt() != 0) {
            localObject12 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject12 = localObject9;
          }
          paramInt1 = setBluetoothA2dpDeviceConnectionState((BluetoothDevice)localObject12, paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 51: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          if (paramParcel1.readInt() != 0) {
            localObject12 = (BluetoothDevice)BluetoothDevice.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject12 = localObject10;
          }
          setHearingAidDeviceConnectionState((BluetoothDevice)localObject12, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 50: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          setWiredDeviceConnectionState(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 49: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramInt1 = getUiSoundsStreamType();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 48: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          localObject12 = getRingtonePlayer();
          paramParcel2.writeNoException();
          paramParcel1 = (Parcel)localObject11;
          if (localObject12 != null) {
            paramParcel1 = ((IRingtonePlayer)localObject12).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 47: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          setRingtonePlayer(IRingtonePlayer.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 46: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          forceVolumeControlStream(paramParcel1.readInt(), paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 45: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          stopBluetoothSco(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 44: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          startBluetoothScoVirtualCall(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 43: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          startBluetoothSco(paramParcel1.readStrongBinder(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 42: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramInt1 = getCurrentAudioFocus();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 41: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          unregisterAudioFocusClient(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 40: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          localIAudioFocusDispatcher = IAudioFocusDispatcher.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject11 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject12 = (AudioAttributes)AudioAttributes.CREATOR.createFromParcel(paramParcel1);
          }
          paramInt1 = abandonAudioFocus(localIAudioFocusDispatcher, (String)localObject11, (AudioAttributes)localObject12, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 39: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          localObject12 = localObject13;
          if (paramParcel1.readInt() != 0) {
            localObject12 = (AudioAttributes)AudioAttributes.CREATOR.createFromParcel(paramParcel1);
          }
          paramInt1 = requestAudioFocus((AudioAttributes)localObject12, paramParcel1.readInt(), paramParcel1.readStrongBinder(), IAudioFocusDispatcher.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), IAudioPolicyCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 38: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramInt1 = isBluetoothA2dpOn();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 37: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          bool10 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool10 = true;
          }
          setBluetoothA2dpOn(bool10);
          paramParcel2.writeNoException();
          return true;
        case 36: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramInt1 = isBluetoothScoOn();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 35: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          bool10 = bool4;
          if (paramParcel1.readInt() != 0) {
            bool10 = true;
          }
          setBluetoothScoOn(bool10);
          paramParcel2.writeNoException();
          return true;
        case 34: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramInt1 = isSpeakerphoneOn();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 33: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          bool10 = bool5;
          if (paramParcel1.readInt() != 0) {
            bool10 = true;
          }
          setSpeakerphoneOn(bool10);
          paramParcel2.writeNoException();
          return true;
        case 32: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramParcel2 = paramParcel1.readString();
          bool10 = bool6;
          if (paramParcel1.readInt() != 0) {
            bool10 = true;
          }
          avrcpSupportsAbsoluteVolume(paramParcel2, bool10);
          return true;
        case 31: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          reloadAudioSettings();
          return true;
        case 30: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          unloadSoundEffects();
          return true;
        case 29: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramInt1 = loadSoundEffects();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 28: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          playSoundEffectVolume(paramParcel1.readInt(), paramParcel1.readFloat());
          return true;
        case 27: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          playSoundEffect(paramParcel1.readInt());
          return true;
        case 26: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramInt1 = getMode();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 25: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          setMode(paramParcel1.readInt(), paramParcel1.readStrongBinder(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramInt1 = shouldVibrate(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramInt1 = getVibrateSetting(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          setVibrateSetting(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramInt1 = isValidRingerMode(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramInt1 = getRingerModeInternal();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramInt1 = getRingerModeExternal();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          setRingerModeInternal(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          setRingerModeExternal(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          bool10 = bool7;
          if (paramParcel1.readInt() != 0) {
            bool10 = true;
          }
          setMicrophoneMute(bool10, paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramInt1 = getLastAudibleStreamVolume(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramInt1 = getStreamMaxVolume(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramInt1 = getStreamMinVolume(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramInt1 = getStreamVolume(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          bool10 = bool8;
          if (paramParcel1.readInt() != 0) {
            bool10 = true;
          }
          setMasterMute(bool10, paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramInt1 = isMasterMute();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          bool10 = bool9;
          if (paramParcel1.readInt() != 0) {
            bool10 = true;
          }
          forceRemoteSubmixFullVolume(bool10, paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramInt1 = isStreamMute(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          setStreamVolume(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          adjustStreamVolume(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          adjustSuggestedStreamVolume(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          releasePlayer(paramParcel1.readInt());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          playerEvent(paramParcel1.readInt(), paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.media.IAudioService");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (AudioAttributes)AudioAttributes.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject14;
          }
          playerAttributes(paramInt1, paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.media.IAudioService");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (PlayerBase.PlayerIdCard)PlayerBase.PlayerIdCard.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject15;
        }
        paramInt1 = trackPlayer(paramParcel1);
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.media.IAudioService");
      return true;
    }
    
    private static class Proxy
      implements IAudioService
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public int abandonAudioFocus(IAudioFocusDispatcher paramIAudioFocusDispatcher, String paramString1, AudioAttributes paramAudioAttributes, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          if (paramIAudioFocusDispatcher != null) {
            paramIAudioFocusDispatcher = paramIAudioFocusDispatcher.asBinder();
          } else {
            paramIAudioFocusDispatcher = null;
          }
          localParcel1.writeStrongBinder(paramIAudioFocusDispatcher);
          localParcel1.writeString(paramString1);
          if (paramAudioAttributes != null)
          {
            localParcel1.writeInt(1);
            paramAudioAttributes.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString2);
          mRemote.transact(40, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int addMixForPolicy(AudioPolicyConfig paramAudioPolicyConfig, IAudioPolicyCallback paramIAudioPolicyCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          if (paramAudioPolicyConfig != null)
          {
            localParcel1.writeInt(1);
            paramAudioPolicyConfig.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIAudioPolicyCallback != null) {
            paramAudioPolicyConfig = paramIAudioPolicyCallback.asBinder();
          } else {
            paramAudioPolicyConfig = null;
          }
          localParcel1.writeStrongBinder(paramAudioPolicyConfig);
          mRemote.transact(65, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void adjustStreamVolume(int paramInt1, int paramInt2, int paramInt3, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeString(paramString);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void adjustSuggestedStreamVolume(int paramInt1, int paramInt2, int paramInt3, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IAudioService");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeInt(paramInt3);
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void avrcpSupportsAbsoluteVolume(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IAudioService");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(32, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void disableRingtoneSync(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(75, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void disableSafeMediaVolume(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeString(paramString);
          mRemote.transact(60, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int dispatchFocusChange(AudioFocusInfo paramAudioFocusInfo, int paramInt, IAudioPolicyCallback paramIAudioPolicyCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          if (paramAudioFocusInfo != null)
          {
            localParcel1.writeInt(1);
            paramAudioFocusInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          if (paramIAudioPolicyCallback != null) {
            paramAudioFocusInfo = paramIAudioPolicyCallback.asBinder();
          } else {
            paramAudioFocusInfo = null;
          }
          localParcel1.writeStrongBinder(paramAudioFocusInfo);
          mRemote.transact(77, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void forceRemoteSubmixFullVolume(boolean paramBoolean, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void forceVolumeControlStream(int paramInt, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(46, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<AudioPlaybackConfiguration> getActivePlaybackConfigurations()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          mRemote.transact(74, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(AudioPlaybackConfiguration.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<AudioRecordingConfiguration> getActiveRecordingConfigurations()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          mRemote.transact(71, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(AudioRecordingConfiguration.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getCurrentAudioFocus()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          mRemote.transact(42, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getFocusRampTimeMs(int paramInt, AudioAttributes paramAudioAttributes)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramInt);
          if (paramAudioAttributes != null)
          {
            localParcel1.writeInt(1);
            paramAudioAttributes.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(76, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.media.IAudioService";
      }
      
      public int getLastAudibleStreamVolume(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getMode()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          mRemote.transact(26, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getRingerModeExternal()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getRingerModeInternal()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IRingtonePlayer getRingtonePlayer()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          mRemote.transact(48, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IRingtonePlayer localIRingtonePlayer = IRingtonePlayer.Stub.asInterface(localParcel2.readStrongBinder());
          return localIRingtonePlayer;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getStreamMaxVolume(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getStreamMinVolume(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getStreamVolume(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getUiSoundsStreamType()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          mRemote.transact(49, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getVibrateSetting(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void handleBluetoothA2dpDeviceConfigChange(BluetoothDevice paramBluetoothDevice)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(53, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isAudioServerRunning()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(83, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isBluetoothA2dpOn()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(38, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isBluetoothScoOn()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(36, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isCameraSoundForced()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(55, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isHdmiSystemAudioSupported()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(62, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isMasterMute()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isSpeakerphoneOn()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(34, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isStreamAffectedByMute(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(59, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isStreamAffectedByRingerMode(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(58, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isStreamMute(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isValidRingerMode(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean loadSoundEffects()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(29, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyVolumeControllerVisible(IVolumeController paramIVolumeController, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          if (paramIVolumeController != null) {
            paramIVolumeController = paramIVolumeController.asBinder();
          } else {
            paramIVolumeController = null;
          }
          localParcel1.writeStrongBinder(paramIVolumeController);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(57, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void playSoundEffect(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IAudioService");
          localParcel.writeInt(paramInt);
          mRemote.transact(27, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void playSoundEffectVolume(int paramInt, float paramFloat)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IAudioService");
          localParcel.writeInt(paramInt);
          localParcel.writeFloat(paramFloat);
          mRemote.transact(28, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void playerAttributes(int paramInt, AudioAttributes paramAudioAttributes)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IAudioService");
          localParcel.writeInt(paramInt);
          if (paramAudioAttributes != null)
          {
            localParcel.writeInt(1);
            paramAudioAttributes.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void playerEvent(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IAudioService");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void playerHasOpPlayAudio(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IAudioService");
          localParcel.writeInt(paramInt);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(78, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String registerAudioPolicy(AudioPolicyConfig paramAudioPolicyConfig, IAudioPolicyCallback paramIAudioPolicyCallback, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          if (paramAudioPolicyConfig != null)
          {
            localParcel1.writeInt(1);
            paramAudioPolicyConfig.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIAudioPolicyCallback != null) {
            paramAudioPolicyConfig = paramIAudioPolicyCallback.asBinder();
          } else {
            paramAudioPolicyConfig = null;
          }
          localParcel1.writeStrongBinder(paramAudioPolicyConfig);
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeInt(paramBoolean2);
          localParcel1.writeInt(paramBoolean3);
          mRemote.transact(63, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramAudioPolicyConfig = localParcel2.readString();
          return paramAudioPolicyConfig;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerAudioServerStateDispatcher(IAudioServerStateDispatcher paramIAudioServerStateDispatcher)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          if (paramIAudioServerStateDispatcher != null) {
            paramIAudioServerStateDispatcher = paramIAudioServerStateDispatcher.asBinder();
          } else {
            paramIAudioServerStateDispatcher = null;
          }
          localParcel1.writeStrongBinder(paramIAudioServerStateDispatcher);
          mRemote.transact(81, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerPlaybackCallback(IPlaybackConfigDispatcher paramIPlaybackConfigDispatcher)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          if (paramIPlaybackConfigDispatcher != null) {
            paramIPlaybackConfigDispatcher = paramIPlaybackConfigDispatcher.asBinder();
          } else {
            paramIPlaybackConfigDispatcher = null;
          }
          localParcel1.writeStrongBinder(paramIPlaybackConfigDispatcher);
          mRemote.transact(72, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerRecordingCallback(IRecordingConfigDispatcher paramIRecordingConfigDispatcher)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          if (paramIRecordingConfigDispatcher != null) {
            paramIRecordingConfigDispatcher = paramIRecordingConfigDispatcher.asBinder();
          } else {
            paramIRecordingConfigDispatcher = null;
          }
          localParcel1.writeStrongBinder(paramIRecordingConfigDispatcher);
          mRemote.transact(69, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void releasePlayer(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IAudioService");
          localParcel.writeInt(paramInt);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void reloadAudioSettings()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IAudioService");
          mRemote.transact(31, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public int removeMixForPolicy(AudioPolicyConfig paramAudioPolicyConfig, IAudioPolicyCallback paramIAudioPolicyCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          if (paramAudioPolicyConfig != null)
          {
            localParcel1.writeInt(1);
            paramAudioPolicyConfig.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIAudioPolicyCallback != null) {
            paramAudioPolicyConfig = paramIAudioPolicyCallback.asBinder();
          } else {
            paramAudioPolicyConfig = null;
          }
          localParcel1.writeStrongBinder(paramAudioPolicyConfig);
          mRemote.transact(66, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int requestAudioFocus(AudioAttributes paramAudioAttributes, int paramInt1, IBinder paramIBinder, IAudioFocusDispatcher paramIAudioFocusDispatcher, String paramString1, String paramString2, int paramInt2, IAudioPolicyCallback paramIAudioPolicyCallback, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          if (paramAudioAttributes != null)
          {
            localParcel1.writeInt(1);
            paramAudioAttributes.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeStrongBinder(paramIBinder);
          paramIBinder = null;
          if (paramIAudioFocusDispatcher != null) {
            paramAudioAttributes = paramIAudioFocusDispatcher.asBinder();
          } else {
            paramAudioAttributes = null;
          }
          localParcel1.writeStrongBinder(paramAudioAttributes);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt2);
          paramAudioAttributes = paramIBinder;
          if (paramIAudioPolicyCallback != null) {
            paramAudioAttributes = paramIAudioPolicyCallback.asBinder();
          }
          localParcel1.writeStrongBinder(paramAudioAttributes);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(39, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setAudioWizardForcePresetState(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(84, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int setBluetoothA2dpDeviceConnectionState(BluetoothDevice paramBluetoothDevice, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(52, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int setBluetoothA2dpDeviceConnectionStateSuppressNoisyIntent(BluetoothDevice paramBluetoothDevice, int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(79, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setBluetoothA2dpOn(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(37, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setBluetoothScoOn(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(35, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int setFocusPropertiesForPolicy(int paramInt, IAudioPolicyCallback paramIAudioPolicyCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramInt);
          if (paramIAudioPolicyCallback != null) {
            paramIAudioPolicyCallback = paramIAudioPolicyCallback.asBinder();
          } else {
            paramIAudioPolicyCallback = null;
          }
          localParcel1.writeStrongBinder(paramIAudioPolicyCallback);
          mRemote.transact(67, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setFocusRequestResultFromExtPolicy(AudioFocusInfo paramAudioFocusInfo, int paramInt, IAudioPolicyCallback paramIAudioPolicyCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IAudioService");
          if (paramAudioFocusInfo != null)
          {
            localParcel.writeInt(1);
            paramAudioFocusInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          if (paramIAudioPolicyCallback != null) {
            paramAudioFocusInfo = paramIAudioPolicyCallback.asBinder();
          } else {
            paramAudioFocusInfo = null;
          }
          localParcel.writeStrongBinder(paramAudioFocusInfo);
          mRemote.transact(80, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public int setHdmiSystemAudioSupported(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(61, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          return paramBoolean;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setHearingAidDeviceConnectionState(BluetoothDevice paramBluetoothDevice, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          if (paramBluetoothDevice != null)
          {
            localParcel1.writeInt(1);
            paramBluetoothDevice.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(51, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setMasterMute(boolean paramBoolean, int paramInt1, String paramString, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setMicrophoneMute(boolean paramBoolean, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setMode(int paramInt, IBinder paramIBinder, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString);
          mRemote.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setRingerModeExternal(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setRingerModeInternal(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setRingtonePlayer(IRingtonePlayer paramIRingtonePlayer)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          if (paramIRingtonePlayer != null) {
            paramIRingtonePlayer = paramIRingtonePlayer.asBinder();
          } else {
            paramIRingtonePlayer = null;
          }
          localParcel1.writeStrongBinder(paramIRingtonePlayer);
          mRemote.transact(47, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setSpeakerphoneOn(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(33, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setStreamVolume(int paramInt1, int paramInt2, int paramInt3, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeString(paramString);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setVibrateSetting(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setVolumeController(IVolumeController paramIVolumeController)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          if (paramIVolumeController != null) {
            paramIVolumeController = paramIVolumeController.asBinder();
          } else {
            paramIVolumeController = null;
          }
          localParcel1.writeStrongBinder(paramIVolumeController);
          mRemote.transact(56, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setVolumePolicy(VolumePolicy paramVolumePolicy)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          if (paramVolumePolicy != null)
          {
            localParcel1.writeInt(1);
            paramVolumePolicy.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(68, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setWiredDeviceConnectionState(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          mRemote.transact(50, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean shouldVibrate(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startBluetoothSco(IBinder paramIBinder, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt);
          mRemote.transact(43, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startBluetoothScoVirtualCall(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(44, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public AudioRoutesInfo startWatchingRoutes(IAudioRoutesObserver paramIAudioRoutesObserver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          Object localObject = null;
          if (paramIAudioRoutesObserver != null) {
            paramIAudioRoutesObserver = paramIAudioRoutesObserver.asBinder();
          } else {
            paramIAudioRoutesObserver = null;
          }
          localParcel1.writeStrongBinder(paramIAudioRoutesObserver);
          mRemote.transact(54, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIAudioRoutesObserver = (AudioRoutesInfo)AudioRoutesInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIAudioRoutesObserver = localObject;
          }
          return paramIAudioRoutesObserver;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void stopBluetoothSco(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(45, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int trackPlayer(PlayerBase.PlayerIdCard paramPlayerIdCard)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          if (paramPlayerIdCard != null)
          {
            localParcel1.writeInt(1);
            paramPlayerIdCard.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unloadSoundEffects()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IAudioService");
          mRemote.transact(30, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void unregisterAudioFocusClient(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.media.IAudioService");
          localParcel1.writeString(paramString);
          mRemote.transact(41, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unregisterAudioPolicyAsync(IAudioPolicyCallback paramIAudioPolicyCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IAudioService");
          if (paramIAudioPolicyCallback != null) {
            paramIAudioPolicyCallback = paramIAudioPolicyCallback.asBinder();
          } else {
            paramIAudioPolicyCallback = null;
          }
          localParcel.writeStrongBinder(paramIAudioPolicyCallback);
          mRemote.transact(64, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void unregisterAudioServerStateDispatcher(IAudioServerStateDispatcher paramIAudioServerStateDispatcher)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IAudioService");
          if (paramIAudioServerStateDispatcher != null) {
            paramIAudioServerStateDispatcher = paramIAudioServerStateDispatcher.asBinder();
          } else {
            paramIAudioServerStateDispatcher = null;
          }
          localParcel.writeStrongBinder(paramIAudioServerStateDispatcher);
          mRemote.transact(82, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void unregisterPlaybackCallback(IPlaybackConfigDispatcher paramIPlaybackConfigDispatcher)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IAudioService");
          if (paramIPlaybackConfigDispatcher != null) {
            paramIPlaybackConfigDispatcher = paramIPlaybackConfigDispatcher.asBinder();
          } else {
            paramIPlaybackConfigDispatcher = null;
          }
          localParcel.writeStrongBinder(paramIPlaybackConfigDispatcher);
          mRemote.transact(73, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void unregisterRecordingCallback(IRecordingConfigDispatcher paramIRecordingConfigDispatcher)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.media.IAudioService");
          if (paramIRecordingConfigDispatcher != null) {
            paramIRecordingConfigDispatcher = paramIRecordingConfigDispatcher.asBinder();
          } else {
            paramIRecordingConfigDispatcher = null;
          }
          localParcel.writeStrongBinder(paramIRecordingConfigDispatcher);
          mRemote.transact(70, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
    }
  }
}
