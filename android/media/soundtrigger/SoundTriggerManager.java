package android.media.soundtrigger;

import android.annotation.SystemApi;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.hardware.soundtrigger.SoundTrigger.GenericSoundModel;
import android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel;
import android.hardware.soundtrigger.SoundTrigger.RecognitionConfig;
import android.hardware.soundtrigger.SoundTrigger.SoundModel;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.provider.Settings.Global;
import android.provider.Settings.SettingNotFoundException;
import android.util.Slog;
import com.android.internal.app.ISoundTriggerService;
import com.android.internal.util.Preconditions;
import java.util.HashMap;
import java.util.UUID;

@SystemApi
public final class SoundTriggerManager
{
  private static final boolean DBG = false;
  public static final String EXTRA_MESSAGE_TYPE = "android.media.soundtrigger.MESSAGE_TYPE";
  public static final String EXTRA_RECOGNITION_EVENT = "android.media.soundtrigger.RECOGNITION_EVENT";
  public static final String EXTRA_STATUS = "android.media.soundtrigger.STATUS";
  public static final int FLAG_MESSAGE_TYPE_RECOGNITION_ERROR = 1;
  public static final int FLAG_MESSAGE_TYPE_RECOGNITION_EVENT = 0;
  public static final int FLAG_MESSAGE_TYPE_RECOGNITION_PAUSED = 2;
  public static final int FLAG_MESSAGE_TYPE_RECOGNITION_RESUMED = 3;
  public static final int FLAG_MESSAGE_TYPE_UNKNOWN = -1;
  private static final String TAG = "SoundTriggerManager";
  private final Context mContext;
  private final HashMap<UUID, SoundTriggerDetector> mReceiverInstanceMap;
  private final ISoundTriggerService mSoundTriggerService;
  
  public SoundTriggerManager(Context paramContext, ISoundTriggerService paramISoundTriggerService)
  {
    mSoundTriggerService = paramISoundTriggerService;
    mContext = paramContext;
    mReceiverInstanceMap = new HashMap();
  }
  
  public SoundTriggerDetector createSoundTriggerDetector(UUID paramUUID, SoundTriggerDetector.Callback paramCallback, Handler paramHandler)
  {
    if (paramUUID == null) {
      return null;
    }
    mReceiverInstanceMap.get(paramUUID);
    paramCallback = new SoundTriggerDetector(mSoundTriggerService, paramUUID, paramCallback, paramHandler);
    mReceiverInstanceMap.put(paramUUID, paramCallback);
    return paramCallback;
  }
  
  public void deleteModel(UUID paramUUID)
  {
    try
    {
      ISoundTriggerService localISoundTriggerService = mSoundTriggerService;
      ParcelUuid localParcelUuid = new android/os/ParcelUuid;
      localParcelUuid.<init>(paramUUID);
      localISoundTriggerService.deleteSoundModel(localParcelUuid);
      return;
    }
    catch (RemoteException paramUUID)
    {
      throw paramUUID.rethrowFromSystemServer();
    }
  }
  
  public int getDetectionServiceOperationsTimeout()
  {
    try
    {
      int i = Settings.Global.getInt(mContext.getContentResolver(), "sound_trigger_detection_service_op_timeout");
      return i;
    }
    catch (Settings.SettingNotFoundException localSettingNotFoundException) {}
    return Integer.MAX_VALUE;
  }
  
  public Model getModel(UUID paramUUID)
  {
    try
    {
      ISoundTriggerService localISoundTriggerService = mSoundTriggerService;
      ParcelUuid localParcelUuid = new android/os/ParcelUuid;
      localParcelUuid.<init>(paramUUID);
      paramUUID = new Model(localISoundTriggerService.getSoundModel(localParcelUuid));
      return paramUUID;
    }
    catch (RemoteException paramUUID)
    {
      throw paramUUID.rethrowFromSystemServer();
    }
  }
  
  public boolean isRecognitionActive(UUID paramUUID)
  {
    if (paramUUID == null) {
      return false;
    }
    try
    {
      ISoundTriggerService localISoundTriggerService = mSoundTriggerService;
      ParcelUuid localParcelUuid = new android/os/ParcelUuid;
      localParcelUuid.<init>(paramUUID);
      boolean bool = localISoundTriggerService.isRecognitionActive(localParcelUuid);
      return bool;
    }
    catch (RemoteException paramUUID)
    {
      throw paramUUID.rethrowFromSystemServer();
    }
  }
  
  public int loadSoundModel(SoundTrigger.SoundModel paramSoundModel)
  {
    if (paramSoundModel == null) {
      return Integer.MIN_VALUE;
    }
    try
    {
      switch (type)
      {
      default: 
        break;
      case 1: 
        return mSoundTriggerService.loadGenericSoundModel((SoundTrigger.GenericSoundModel)paramSoundModel);
      case 0: 
        return mSoundTriggerService.loadKeyphraseSoundModel((SoundTrigger.KeyphraseSoundModel)paramSoundModel);
      }
      Slog.e("SoundTriggerManager", "Unkown model type");
      return Integer.MIN_VALUE;
    }
    catch (RemoteException paramSoundModel)
    {
      throw paramSoundModel.rethrowFromSystemServer();
    }
  }
  
  public int startRecognition(UUID paramUUID, PendingIntent paramPendingIntent, SoundTrigger.RecognitionConfig paramRecognitionConfig)
  {
    if ((paramUUID != null) && (paramPendingIntent != null) && (paramRecognitionConfig != null)) {
      try
      {
        ISoundTriggerService localISoundTriggerService = mSoundTriggerService;
        ParcelUuid localParcelUuid = new android/os/ParcelUuid;
        localParcelUuid.<init>(paramUUID);
        int i = localISoundTriggerService.startRecognitionForIntent(localParcelUuid, paramPendingIntent, paramRecognitionConfig);
        return i;
      }
      catch (RemoteException paramUUID)
      {
        throw paramUUID.rethrowFromSystemServer();
      }
    }
    return Integer.MIN_VALUE;
  }
  
  public int startRecognition(UUID paramUUID, Bundle paramBundle, ComponentName paramComponentName, SoundTrigger.RecognitionConfig paramRecognitionConfig)
  {
    Preconditions.checkNotNull(paramUUID);
    Preconditions.checkNotNull(paramComponentName);
    Preconditions.checkNotNull(paramRecognitionConfig);
    try
    {
      ISoundTriggerService localISoundTriggerService = mSoundTriggerService;
      ParcelUuid localParcelUuid = new android/os/ParcelUuid;
      localParcelUuid.<init>(paramUUID);
      int i = localISoundTriggerService.startRecognitionForService(localParcelUuid, paramBundle, paramComponentName, paramRecognitionConfig);
      return i;
    }
    catch (RemoteException paramUUID)
    {
      throw paramUUID.rethrowFromSystemServer();
    }
  }
  
  public int stopRecognition(UUID paramUUID)
  {
    if (paramUUID == null) {
      return Integer.MIN_VALUE;
    }
    try
    {
      ISoundTriggerService localISoundTriggerService = mSoundTriggerService;
      ParcelUuid localParcelUuid = new android/os/ParcelUuid;
      localParcelUuid.<init>(paramUUID);
      int i = localISoundTriggerService.stopRecognitionForIntent(localParcelUuid);
      return i;
    }
    catch (RemoteException paramUUID)
    {
      throw paramUUID.rethrowFromSystemServer();
    }
  }
  
  public int unloadSoundModel(UUID paramUUID)
  {
    if (paramUUID == null) {
      return Integer.MIN_VALUE;
    }
    try
    {
      ISoundTriggerService localISoundTriggerService = mSoundTriggerService;
      ParcelUuid localParcelUuid = new android/os/ParcelUuid;
      localParcelUuid.<init>(paramUUID);
      int i = localISoundTriggerService.unloadSoundModel(localParcelUuid);
      return i;
    }
    catch (RemoteException paramUUID)
    {
      throw paramUUID.rethrowFromSystemServer();
    }
  }
  
  public void updateModel(Model paramModel)
  {
    try
    {
      mSoundTriggerService.updateSoundModel(paramModel.getGenericSoundModel());
      return;
    }
    catch (RemoteException paramModel)
    {
      throw paramModel.rethrowFromSystemServer();
    }
  }
  
  public static class Model
  {
    private SoundTrigger.GenericSoundModel mGenericSoundModel;
    
    Model(SoundTrigger.GenericSoundModel paramGenericSoundModel)
    {
      mGenericSoundModel = paramGenericSoundModel;
    }
    
    public static Model create(UUID paramUUID1, UUID paramUUID2, byte[] paramArrayOfByte)
    {
      return new Model(new SoundTrigger.GenericSoundModel(paramUUID1, paramUUID2, paramArrayOfByte));
    }
    
    SoundTrigger.GenericSoundModel getGenericSoundModel()
    {
      return mGenericSoundModel;
    }
    
    public byte[] getModelData()
    {
      return mGenericSoundModel.data;
    }
    
    public UUID getModelUuid()
    {
      return mGenericSoundModel.uuid;
    }
    
    public UUID getVendorUuid()
    {
      return mGenericSoundModel.vendorUuid;
    }
  }
}
