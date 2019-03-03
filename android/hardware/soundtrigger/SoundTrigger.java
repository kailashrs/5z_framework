package android.hardware.soundtrigger;

import android.annotation.SystemApi;
import android.media.AudioFormat;
import android.media.AudioFormat.Builder;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.system.OsConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@SystemApi
public class SoundTrigger
{
  public static final int RECOGNITION_MODE_USER_AUTHENTICATION = 4;
  public static final int RECOGNITION_MODE_USER_IDENTIFICATION = 2;
  public static final int RECOGNITION_MODE_VOICE_TRIGGER = 1;
  public static final int RECOGNITION_STATUS_ABORT = 1;
  public static final int RECOGNITION_STATUS_FAILURE = 2;
  public static final int RECOGNITION_STATUS_SUCCESS = 0;
  public static final int SERVICE_STATE_DISABLED = 1;
  public static final int SERVICE_STATE_ENABLED = 0;
  public static final int SOUNDMODEL_STATUS_UPDATED = 0;
  public static final int STATUS_BAD_VALUE = -OsConstants.EINVAL;
  public static final int STATUS_DEAD_OBJECT = -OsConstants.EPIPE;
  public static final int STATUS_ERROR = Integer.MIN_VALUE;
  public static final int STATUS_INVALID_OPERATION = -OsConstants.ENOSYS;
  public static final int STATUS_NO_INIT;
  public static final int STATUS_OK = 0;
  public static final int STATUS_PERMISSION_DENIED = -OsConstants.EPERM;
  
  static
  {
    STATUS_NO_INIT = -OsConstants.ENODEV;
  }
  
  private SoundTrigger() {}
  
  public static SoundTriggerModule attachModule(int paramInt, StatusListener paramStatusListener, Handler paramHandler)
  {
    if (paramStatusListener == null) {
      return null;
    }
    return new SoundTriggerModule(paramInt, paramStatusListener, paramHandler);
  }
  
  public static native int listModules(ArrayList<ModuleProperties> paramArrayList);
  
  public static class ConfidenceLevel
    implements Parcelable
  {
    public static final Parcelable.Creator<ConfidenceLevel> CREATOR = new Parcelable.Creator()
    {
      public SoundTrigger.ConfidenceLevel createFromParcel(Parcel paramAnonymousParcel)
      {
        return SoundTrigger.ConfidenceLevel.fromParcel(paramAnonymousParcel);
      }
      
      public SoundTrigger.ConfidenceLevel[] newArray(int paramAnonymousInt)
      {
        return new SoundTrigger.ConfidenceLevel[paramAnonymousInt];
      }
    };
    public final int confidenceLevel;
    public final int userId;
    
    public ConfidenceLevel(int paramInt1, int paramInt2)
    {
      userId = paramInt1;
      confidenceLevel = paramInt2;
    }
    
    private static ConfidenceLevel fromParcel(Parcel paramParcel)
    {
      return new ConfidenceLevel(paramParcel.readInt(), paramParcel.readInt());
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (paramObject == null) {
        return false;
      }
      if (getClass() != paramObject.getClass()) {
        return false;
      }
      paramObject = (ConfidenceLevel)paramObject;
      if (confidenceLevel != confidenceLevel) {
        return false;
      }
      return userId == userId;
    }
    
    public int hashCode()
    {
      return 31 * (31 * 1 + confidenceLevel) + userId;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("ConfidenceLevel [userId=");
      localStringBuilder.append(userId);
      localStringBuilder.append(", confidenceLevel=");
      localStringBuilder.append(confidenceLevel);
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(userId);
      paramParcel.writeInt(confidenceLevel);
    }
  }
  
  public static class GenericRecognitionEvent
    extends SoundTrigger.RecognitionEvent
    implements Parcelable
  {
    public static final Parcelable.Creator<GenericRecognitionEvent> CREATOR = new Parcelable.Creator()
    {
      public SoundTrigger.GenericRecognitionEvent createFromParcel(Parcel paramAnonymousParcel)
      {
        return SoundTrigger.GenericRecognitionEvent.fromParcelForGeneric(paramAnonymousParcel);
      }
      
      public SoundTrigger.GenericRecognitionEvent[] newArray(int paramAnonymousInt)
      {
        return new SoundTrigger.GenericRecognitionEvent[paramAnonymousInt];
      }
    };
    
    public GenericRecognitionEvent(int paramInt1, int paramInt2, boolean paramBoolean1, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean2, AudioFormat paramAudioFormat, byte[] paramArrayOfByte)
    {
      super(paramInt2, paramBoolean1, paramInt3, paramInt4, paramInt5, paramBoolean2, paramAudioFormat, paramArrayOfByte);
    }
    
    private static GenericRecognitionEvent fromParcelForGeneric(Parcel paramParcel)
    {
      paramParcel = SoundTrigger.RecognitionEvent.fromParcel(paramParcel);
      return new GenericRecognitionEvent(status, soundModelHandle, captureAvailable, captureSession, captureDelayMs, capturePreambleMs, triggerInData, captureFormat, data);
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (paramObject == null) {
        return false;
      }
      if (getClass() != paramObject.getClass()) {
        return false;
      }
      return super.equals(paramObject);
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("GenericRecognitionEvent ::");
      localStringBuilder.append(super.toString());
      return localStringBuilder.toString();
    }
  }
  
  public static class GenericSoundModel
    extends SoundTrigger.SoundModel
    implements Parcelable
  {
    public static final Parcelable.Creator<GenericSoundModel> CREATOR = new Parcelable.Creator()
    {
      public SoundTrigger.GenericSoundModel createFromParcel(Parcel paramAnonymousParcel)
      {
        return SoundTrigger.GenericSoundModel.fromParcel(paramAnonymousParcel);
      }
      
      public SoundTrigger.GenericSoundModel[] newArray(int paramAnonymousInt)
      {
        return new SoundTrigger.GenericSoundModel[paramAnonymousInt];
      }
    };
    
    public GenericSoundModel(UUID paramUUID1, UUID paramUUID2, byte[] paramArrayOfByte)
    {
      super(paramUUID2, 1, paramArrayOfByte);
    }
    
    private static GenericSoundModel fromParcel(Parcel paramParcel)
    {
      UUID localUUID1 = UUID.fromString(paramParcel.readString());
      UUID localUUID2 = null;
      if (paramParcel.readInt() >= 0) {
        localUUID2 = UUID.fromString(paramParcel.readString());
      }
      return new GenericSoundModel(localUUID1, localUUID2, paramParcel.readBlob());
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("GenericSoundModel [uuid=");
      localStringBuilder.append(uuid);
      localStringBuilder.append(", vendorUuid=");
      localStringBuilder.append(vendorUuid);
      localStringBuilder.append(", type=");
      localStringBuilder.append(type);
      localStringBuilder.append(", data=");
      int i;
      if (data == null) {
        i = 0;
      } else {
        i = data.length;
      }
      localStringBuilder.append(i);
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeString(uuid.toString());
      if (vendorUuid == null)
      {
        paramParcel.writeInt(-1);
      }
      else
      {
        paramParcel.writeInt(vendorUuid.toString().length());
        paramParcel.writeString(vendorUuid.toString());
      }
      paramParcel.writeBlob(data);
    }
  }
  
  public static class Keyphrase
    implements Parcelable
  {
    public static final Parcelable.Creator<Keyphrase> CREATOR = new Parcelable.Creator()
    {
      public SoundTrigger.Keyphrase createFromParcel(Parcel paramAnonymousParcel)
      {
        return SoundTrigger.Keyphrase.fromParcel(paramAnonymousParcel);
      }
      
      public SoundTrigger.Keyphrase[] newArray(int paramAnonymousInt)
      {
        return new SoundTrigger.Keyphrase[paramAnonymousInt];
      }
    };
    public final int id;
    public final String locale;
    public final int recognitionModes;
    public final String text;
    public final int[] users;
    
    public Keyphrase(int paramInt1, int paramInt2, String paramString1, String paramString2, int[] paramArrayOfInt)
    {
      id = paramInt1;
      recognitionModes = paramInt2;
      locale = paramString1;
      text = paramString2;
      users = paramArrayOfInt;
    }
    
    private static Keyphrase fromParcel(Parcel paramParcel)
    {
      int i = paramParcel.readInt();
      int j = paramParcel.readInt();
      String str1 = paramParcel.readString();
      String str2 = paramParcel.readString();
      int[] arrayOfInt = null;
      int k = paramParcel.readInt();
      if (k >= 0)
      {
        arrayOfInt = new int[k];
        paramParcel.readIntArray(arrayOfInt);
      }
      return new Keyphrase(i, j, str1, str2, arrayOfInt);
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (paramObject == null) {
        return false;
      }
      if (getClass() != paramObject.getClass()) {
        return false;
      }
      paramObject = (Keyphrase)paramObject;
      if (text == null)
      {
        if (text != null) {
          return false;
        }
      }
      else if (!text.equals(text)) {
        return false;
      }
      if (id != id) {
        return false;
      }
      if (locale == null)
      {
        if (locale != null) {
          return false;
        }
      }
      else if (!locale.equals(locale)) {
        return false;
      }
      if (recognitionModes != recognitionModes) {
        return false;
      }
      return Arrays.equals(users, users);
    }
    
    public int hashCode()
    {
      String str = text;
      int i = 0;
      int j;
      if (str == null) {
        j = 0;
      } else {
        j = text.hashCode();
      }
      int k = id;
      if (locale != null) {
        i = locale.hashCode();
      }
      return 31 * (31 * (31 * (31 * (31 * 1 + j) + k) + i) + recognitionModes) + Arrays.hashCode(users);
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Keyphrase [id=");
      localStringBuilder.append(id);
      localStringBuilder.append(", recognitionModes=");
      localStringBuilder.append(recognitionModes);
      localStringBuilder.append(", locale=");
      localStringBuilder.append(locale);
      localStringBuilder.append(", text=");
      localStringBuilder.append(text);
      localStringBuilder.append(", users=");
      localStringBuilder.append(Arrays.toString(users));
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(id);
      paramParcel.writeInt(recognitionModes);
      paramParcel.writeString(locale);
      paramParcel.writeString(text);
      if (users != null)
      {
        paramParcel.writeInt(users.length);
        paramParcel.writeIntArray(users);
      }
      else
      {
        paramParcel.writeInt(-1);
      }
    }
  }
  
  public static class KeyphraseRecognitionEvent
    extends SoundTrigger.RecognitionEvent
    implements Parcelable
  {
    public static final Parcelable.Creator<KeyphraseRecognitionEvent> CREATOR = new Parcelable.Creator()
    {
      public SoundTrigger.KeyphraseRecognitionEvent createFromParcel(Parcel paramAnonymousParcel)
      {
        return SoundTrigger.KeyphraseRecognitionEvent.fromParcelForKeyphrase(paramAnonymousParcel);
      }
      
      public SoundTrigger.KeyphraseRecognitionEvent[] newArray(int paramAnonymousInt)
      {
        return new SoundTrigger.KeyphraseRecognitionEvent[paramAnonymousInt];
      }
    };
    public final SoundTrigger.KeyphraseRecognitionExtra[] keyphraseExtras;
    
    public KeyphraseRecognitionEvent(int paramInt1, int paramInt2, boolean paramBoolean1, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean2, AudioFormat paramAudioFormat, byte[] paramArrayOfByte, SoundTrigger.KeyphraseRecognitionExtra[] paramArrayOfKeyphraseRecognitionExtra)
    {
      super(paramInt2, paramBoolean1, paramInt3, paramInt4, paramInt5, paramBoolean2, paramAudioFormat, paramArrayOfByte);
      keyphraseExtras = paramArrayOfKeyphraseRecognitionExtra;
    }
    
    private static KeyphraseRecognitionEvent fromParcelForKeyphrase(Parcel paramParcel)
    {
      int i = paramParcel.readInt();
      int j = paramParcel.readInt();
      boolean bool1;
      if (paramParcel.readByte() == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      int k = paramParcel.readInt();
      int m = paramParcel.readInt();
      int n = paramParcel.readInt();
      boolean bool2;
      if (paramParcel.readByte() == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      AudioFormat localAudioFormat = null;
      if (paramParcel.readByte() == 1)
      {
        int i1 = paramParcel.readInt();
        int i2 = paramParcel.readInt();
        int i3 = paramParcel.readInt();
        localAudioFormat = new AudioFormat.Builder().setChannelMask(i3).setEncoding(i2).setSampleRate(i1).build();
      }
      return new KeyphraseRecognitionEvent(i, j, bool1, k, m, n, bool2, localAudioFormat, paramParcel.readBlob(), (SoundTrigger.KeyphraseRecognitionExtra[])paramParcel.createTypedArray(SoundTrigger.KeyphraseRecognitionExtra.CREATOR));
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (!super.equals(paramObject)) {
        return false;
      }
      if (getClass() != paramObject.getClass()) {
        return false;
      }
      paramObject = (KeyphraseRecognitionEvent)paramObject;
      return Arrays.equals(keyphraseExtras, keyphraseExtras);
    }
    
    public int hashCode()
    {
      return 31 * super.hashCode() + Arrays.hashCode(keyphraseExtras);
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("KeyphraseRecognitionEvent [keyphraseExtras=");
      localStringBuilder.append(Arrays.toString(keyphraseExtras));
      localStringBuilder.append(", status=");
      localStringBuilder.append(status);
      localStringBuilder.append(", soundModelHandle=");
      localStringBuilder.append(soundModelHandle);
      localStringBuilder.append(", captureAvailable=");
      localStringBuilder.append(captureAvailable);
      localStringBuilder.append(", captureSession=");
      localStringBuilder.append(captureSession);
      localStringBuilder.append(", captureDelayMs=");
      localStringBuilder.append(captureDelayMs);
      localStringBuilder.append(", capturePreambleMs=");
      localStringBuilder.append(capturePreambleMs);
      localStringBuilder.append(", triggerInData=");
      localStringBuilder.append(triggerInData);
      Object localObject;
      if (captureFormat == null)
      {
        localObject = "";
      }
      else
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(", sampleRate=");
        ((StringBuilder)localObject).append(captureFormat.getSampleRate());
        localObject = ((StringBuilder)localObject).toString();
      }
      localStringBuilder.append((String)localObject);
      if (captureFormat == null)
      {
        localObject = "";
      }
      else
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(", encoding=");
        ((StringBuilder)localObject).append(captureFormat.getEncoding());
        localObject = ((StringBuilder)localObject).toString();
      }
      localStringBuilder.append((String)localObject);
      if (captureFormat == null)
      {
        localObject = "";
      }
      else
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(", channelMask=");
        ((StringBuilder)localObject).append(captureFormat.getChannelMask());
        localObject = ((StringBuilder)localObject).toString();
      }
      localStringBuilder.append((String)localObject);
      localStringBuilder.append(", data=");
      int i;
      if (data == null) {
        i = 0;
      } else {
        i = data.length;
      }
      localStringBuilder.append(i);
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(status);
      paramParcel.writeInt(soundModelHandle);
      paramParcel.writeByte((byte)captureAvailable);
      paramParcel.writeInt(captureSession);
      paramParcel.writeInt(captureDelayMs);
      paramParcel.writeInt(capturePreambleMs);
      paramParcel.writeByte((byte)triggerInData);
      if (captureFormat != null)
      {
        paramParcel.writeByte((byte)1);
        paramParcel.writeInt(captureFormat.getSampleRate());
        paramParcel.writeInt(captureFormat.getEncoding());
        paramParcel.writeInt(captureFormat.getChannelMask());
      }
      else
      {
        paramParcel.writeByte((byte)0);
      }
      paramParcel.writeBlob(data);
      paramParcel.writeTypedArray(keyphraseExtras, paramInt);
    }
  }
  
  public static class KeyphraseRecognitionExtra
    implements Parcelable
  {
    public static final Parcelable.Creator<KeyphraseRecognitionExtra> CREATOR = new Parcelable.Creator()
    {
      public SoundTrigger.KeyphraseRecognitionExtra createFromParcel(Parcel paramAnonymousParcel)
      {
        return SoundTrigger.KeyphraseRecognitionExtra.fromParcel(paramAnonymousParcel);
      }
      
      public SoundTrigger.KeyphraseRecognitionExtra[] newArray(int paramAnonymousInt)
      {
        return new SoundTrigger.KeyphraseRecognitionExtra[paramAnonymousInt];
      }
    };
    public final int coarseConfidenceLevel;
    public final SoundTrigger.ConfidenceLevel[] confidenceLevels;
    public final int id;
    public final int recognitionModes;
    
    public KeyphraseRecognitionExtra(int paramInt1, int paramInt2, int paramInt3, SoundTrigger.ConfidenceLevel[] paramArrayOfConfidenceLevel)
    {
      id = paramInt1;
      recognitionModes = paramInt2;
      coarseConfidenceLevel = paramInt3;
      confidenceLevels = paramArrayOfConfidenceLevel;
    }
    
    private static KeyphraseRecognitionExtra fromParcel(Parcel paramParcel)
    {
      return new KeyphraseRecognitionExtra(paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt(), (SoundTrigger.ConfidenceLevel[])paramParcel.createTypedArray(SoundTrigger.ConfidenceLevel.CREATOR));
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (paramObject == null) {
        return false;
      }
      if (getClass() != paramObject.getClass()) {
        return false;
      }
      paramObject = (KeyphraseRecognitionExtra)paramObject;
      if (!Arrays.equals(confidenceLevels, confidenceLevels)) {
        return false;
      }
      if (id != id) {
        return false;
      }
      if (recognitionModes != recognitionModes) {
        return false;
      }
      return coarseConfidenceLevel == coarseConfidenceLevel;
    }
    
    public int hashCode()
    {
      return 31 * (31 * (31 * (31 * 1 + Arrays.hashCode(confidenceLevels)) + id) + recognitionModes) + coarseConfidenceLevel;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("KeyphraseRecognitionExtra [id=");
      localStringBuilder.append(id);
      localStringBuilder.append(", recognitionModes=");
      localStringBuilder.append(recognitionModes);
      localStringBuilder.append(", coarseConfidenceLevel=");
      localStringBuilder.append(coarseConfidenceLevel);
      localStringBuilder.append(", confidenceLevels=");
      localStringBuilder.append(Arrays.toString(confidenceLevels));
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(id);
      paramParcel.writeInt(recognitionModes);
      paramParcel.writeInt(coarseConfidenceLevel);
      paramParcel.writeTypedArray(confidenceLevels, paramInt);
    }
  }
  
  public static class KeyphraseSoundModel
    extends SoundTrigger.SoundModel
    implements Parcelable
  {
    public static final Parcelable.Creator<KeyphraseSoundModel> CREATOR = new Parcelable.Creator()
    {
      public SoundTrigger.KeyphraseSoundModel createFromParcel(Parcel paramAnonymousParcel)
      {
        return SoundTrigger.KeyphraseSoundModel.fromParcel(paramAnonymousParcel);
      }
      
      public SoundTrigger.KeyphraseSoundModel[] newArray(int paramAnonymousInt)
      {
        return new SoundTrigger.KeyphraseSoundModel[paramAnonymousInt];
      }
    };
    public final SoundTrigger.Keyphrase[] keyphrases;
    
    public KeyphraseSoundModel(UUID paramUUID1, UUID paramUUID2, byte[] paramArrayOfByte, SoundTrigger.Keyphrase[] paramArrayOfKeyphrase)
    {
      super(paramUUID2, 0, paramArrayOfByte);
      keyphrases = paramArrayOfKeyphrase;
    }
    
    private static KeyphraseSoundModel fromParcel(Parcel paramParcel)
    {
      UUID localUUID1 = UUID.fromString(paramParcel.readString());
      UUID localUUID2 = null;
      if (paramParcel.readInt() >= 0) {
        localUUID2 = UUID.fromString(paramParcel.readString());
      }
      return new KeyphraseSoundModel(localUUID1, localUUID2, paramParcel.readBlob(), (SoundTrigger.Keyphrase[])paramParcel.createTypedArray(SoundTrigger.Keyphrase.CREATOR));
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (!super.equals(paramObject)) {
        return false;
      }
      if (!(paramObject instanceof KeyphraseSoundModel)) {
        return false;
      }
      paramObject = (KeyphraseSoundModel)paramObject;
      return Arrays.equals(keyphrases, keyphrases);
    }
    
    public int hashCode()
    {
      return 31 * super.hashCode() + Arrays.hashCode(keyphrases);
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("KeyphraseSoundModel [keyphrases=");
      localStringBuilder.append(Arrays.toString(keyphrases));
      localStringBuilder.append(", uuid=");
      localStringBuilder.append(uuid);
      localStringBuilder.append(", vendorUuid=");
      localStringBuilder.append(vendorUuid);
      localStringBuilder.append(", type=");
      localStringBuilder.append(type);
      localStringBuilder.append(", data=");
      int i;
      if (data == null) {
        i = 0;
      } else {
        i = data.length;
      }
      localStringBuilder.append(i);
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeString(uuid.toString());
      if (vendorUuid == null)
      {
        paramParcel.writeInt(-1);
      }
      else
      {
        paramParcel.writeInt(vendorUuid.toString().length());
        paramParcel.writeString(vendorUuid.toString());
      }
      paramParcel.writeBlob(data);
      paramParcel.writeTypedArray(keyphrases, paramInt);
    }
  }
  
  public static class ModuleProperties
    implements Parcelable
  {
    public static final Parcelable.Creator<ModuleProperties> CREATOR = new Parcelable.Creator()
    {
      public SoundTrigger.ModuleProperties createFromParcel(Parcel paramAnonymousParcel)
      {
        return SoundTrigger.ModuleProperties.fromParcel(paramAnonymousParcel);
      }
      
      public SoundTrigger.ModuleProperties[] newArray(int paramAnonymousInt)
      {
        return new SoundTrigger.ModuleProperties[paramAnonymousInt];
      }
    };
    public final String description;
    public final int id;
    public final String implementor;
    public final int maxBufferMs;
    public final int maxKeyphrases;
    public final int maxSoundModels;
    public final int maxUsers;
    public final int powerConsumptionMw;
    public final int recognitionModes;
    public final boolean returnsTriggerInEvent;
    public final boolean supportsCaptureTransition;
    public final boolean supportsConcurrentCapture;
    public final UUID uuid;
    public final int version;
    
    ModuleProperties(int paramInt1, String paramString1, String paramString2, String paramString3, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean1, int paramInt7, boolean paramBoolean2, int paramInt8, boolean paramBoolean3)
    {
      id = paramInt1;
      implementor = paramString1;
      description = paramString2;
      uuid = UUID.fromString(paramString3);
      version = paramInt2;
      maxSoundModels = paramInt3;
      maxKeyphrases = paramInt4;
      maxUsers = paramInt5;
      recognitionModes = paramInt6;
      supportsCaptureTransition = paramBoolean1;
      maxBufferMs = paramInt7;
      supportsConcurrentCapture = paramBoolean2;
      powerConsumptionMw = paramInt8;
      returnsTriggerInEvent = paramBoolean3;
    }
    
    private static ModuleProperties fromParcel(Parcel paramParcel)
    {
      int i = paramParcel.readInt();
      String str1 = paramParcel.readString();
      String str2 = paramParcel.readString();
      String str3 = paramParcel.readString();
      int j = paramParcel.readInt();
      int k = paramParcel.readInt();
      int m = paramParcel.readInt();
      int n = paramParcel.readInt();
      int i1 = paramParcel.readInt();
      boolean bool1;
      if (paramParcel.readByte() == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      int i2 = paramParcel.readInt();
      boolean bool2;
      if (paramParcel.readByte() == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      int i3 = paramParcel.readInt();
      boolean bool3;
      if (paramParcel.readByte() == 1) {
        bool3 = true;
      } else {
        bool3 = false;
      }
      return new ModuleProperties(i, str1, str2, str3, j, k, m, n, i1, bool1, i2, bool2, i3, bool3);
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("ModuleProperties [id=");
      localStringBuilder.append(id);
      localStringBuilder.append(", implementor=");
      localStringBuilder.append(implementor);
      localStringBuilder.append(", description=");
      localStringBuilder.append(description);
      localStringBuilder.append(", uuid=");
      localStringBuilder.append(uuid);
      localStringBuilder.append(", version=");
      localStringBuilder.append(version);
      localStringBuilder.append(", maxSoundModels=");
      localStringBuilder.append(maxSoundModels);
      localStringBuilder.append(", maxKeyphrases=");
      localStringBuilder.append(maxKeyphrases);
      localStringBuilder.append(", maxUsers=");
      localStringBuilder.append(maxUsers);
      localStringBuilder.append(", recognitionModes=");
      localStringBuilder.append(recognitionModes);
      localStringBuilder.append(", supportsCaptureTransition=");
      localStringBuilder.append(supportsCaptureTransition);
      localStringBuilder.append(", maxBufferMs=");
      localStringBuilder.append(maxBufferMs);
      localStringBuilder.append(", supportsConcurrentCapture=");
      localStringBuilder.append(supportsConcurrentCapture);
      localStringBuilder.append(", powerConsumptionMw=");
      localStringBuilder.append(powerConsumptionMw);
      localStringBuilder.append(", returnsTriggerInEvent=");
      localStringBuilder.append(returnsTriggerInEvent);
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(id);
      paramParcel.writeString(implementor);
      paramParcel.writeString(description);
      paramParcel.writeString(uuid.toString());
      paramParcel.writeInt(version);
      paramParcel.writeInt(maxSoundModels);
      paramParcel.writeInt(maxKeyphrases);
      paramParcel.writeInt(maxUsers);
      paramParcel.writeInt(recognitionModes);
      paramParcel.writeByte((byte)supportsCaptureTransition);
      paramParcel.writeInt(maxBufferMs);
      paramParcel.writeByte((byte)supportsConcurrentCapture);
      paramParcel.writeInt(powerConsumptionMw);
      paramParcel.writeByte((byte)returnsTriggerInEvent);
    }
  }
  
  public static class RecognitionConfig
    implements Parcelable
  {
    public static final Parcelable.Creator<RecognitionConfig> CREATOR = new Parcelable.Creator()
    {
      public SoundTrigger.RecognitionConfig createFromParcel(Parcel paramAnonymousParcel)
      {
        return SoundTrigger.RecognitionConfig.fromParcel(paramAnonymousParcel);
      }
      
      public SoundTrigger.RecognitionConfig[] newArray(int paramAnonymousInt)
      {
        return new SoundTrigger.RecognitionConfig[paramAnonymousInt];
      }
    };
    public final boolean allowMultipleTriggers;
    public final boolean captureRequested;
    public final byte[] data;
    public final SoundTrigger.KeyphraseRecognitionExtra[] keyphrases;
    
    public RecognitionConfig(boolean paramBoolean1, boolean paramBoolean2, SoundTrigger.KeyphraseRecognitionExtra[] paramArrayOfKeyphraseRecognitionExtra, byte[] paramArrayOfByte)
    {
      captureRequested = paramBoolean1;
      allowMultipleTriggers = paramBoolean2;
      keyphrases = paramArrayOfKeyphraseRecognitionExtra;
      data = paramArrayOfByte;
    }
    
    private static RecognitionConfig fromParcel(Parcel paramParcel)
    {
      int i = paramParcel.readByte();
      boolean bool1 = false;
      boolean bool2;
      if (i == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      if (paramParcel.readByte() == 1) {
        bool1 = true;
      }
      return new RecognitionConfig(bool2, bool1, (SoundTrigger.KeyphraseRecognitionExtra[])paramParcel.createTypedArray(SoundTrigger.KeyphraseRecognitionExtra.CREATOR), paramParcel.readBlob());
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("RecognitionConfig [captureRequested=");
      localStringBuilder.append(captureRequested);
      localStringBuilder.append(", allowMultipleTriggers=");
      localStringBuilder.append(allowMultipleTriggers);
      localStringBuilder.append(", keyphrases=");
      localStringBuilder.append(Arrays.toString(keyphrases));
      localStringBuilder.append(", data=");
      localStringBuilder.append(Arrays.toString(data));
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeByte((byte)captureRequested);
      paramParcel.writeByte((byte)allowMultipleTriggers);
      paramParcel.writeTypedArray(keyphrases, paramInt);
      paramParcel.writeBlob(data);
    }
  }
  
  public static class RecognitionEvent
  {
    public static final Parcelable.Creator<RecognitionEvent> CREATOR = new Parcelable.Creator()
    {
      public SoundTrigger.RecognitionEvent createFromParcel(Parcel paramAnonymousParcel)
      {
        return SoundTrigger.RecognitionEvent.fromParcel(paramAnonymousParcel);
      }
      
      public SoundTrigger.RecognitionEvent[] newArray(int paramAnonymousInt)
      {
        return new SoundTrigger.RecognitionEvent[paramAnonymousInt];
      }
    };
    public final boolean captureAvailable;
    public final int captureDelayMs;
    public final AudioFormat captureFormat;
    public final int capturePreambleMs;
    public final int captureSession;
    public final byte[] data;
    public final int soundModelHandle;
    public final int status;
    public final boolean triggerInData;
    
    public RecognitionEvent(int paramInt1, int paramInt2, boolean paramBoolean1, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean2, AudioFormat paramAudioFormat, byte[] paramArrayOfByte)
    {
      status = paramInt1;
      soundModelHandle = paramInt2;
      captureAvailable = paramBoolean1;
      captureSession = paramInt3;
      captureDelayMs = paramInt4;
      capturePreambleMs = paramInt5;
      triggerInData = paramBoolean2;
      captureFormat = paramAudioFormat;
      data = paramArrayOfByte;
    }
    
    protected static RecognitionEvent fromParcel(Parcel paramParcel)
    {
      int i = paramParcel.readInt();
      int j = paramParcel.readInt();
      boolean bool1;
      if (paramParcel.readByte() == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      int k = paramParcel.readInt();
      int m = paramParcel.readInt();
      int n = paramParcel.readInt();
      boolean bool2;
      if (paramParcel.readByte() == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      AudioFormat localAudioFormat = null;
      if (paramParcel.readByte() == 1)
      {
        int i1 = paramParcel.readInt();
        int i2 = paramParcel.readInt();
        int i3 = paramParcel.readInt();
        localAudioFormat = new AudioFormat.Builder().setChannelMask(i3).setEncoding(i2).setSampleRate(i1).build();
      }
      return new RecognitionEvent(i, j, bool1, k, m, n, bool2, localAudioFormat, paramParcel.readBlob());
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (paramObject == null) {
        return false;
      }
      if (getClass() != paramObject.getClass()) {
        return false;
      }
      paramObject = (RecognitionEvent)paramObject;
      if (captureAvailable != captureAvailable) {
        return false;
      }
      if (captureDelayMs != captureDelayMs) {
        return false;
      }
      if (capturePreambleMs != capturePreambleMs) {
        return false;
      }
      if (captureSession != captureSession) {
        return false;
      }
      if (!Arrays.equals(data, data)) {
        return false;
      }
      if (soundModelHandle != soundModelHandle) {
        return false;
      }
      if (status != status) {
        return false;
      }
      if (triggerInData != triggerInData) {
        return false;
      }
      if (captureFormat == null)
      {
        if (captureFormat != null) {
          return false;
        }
      }
      else
      {
        if (captureFormat == null) {
          return false;
        }
        if (captureFormat.getSampleRate() != captureFormat.getSampleRate()) {
          return false;
        }
        if (captureFormat.getEncoding() != captureFormat.getEncoding()) {
          return false;
        }
        if (captureFormat.getChannelMask() != captureFormat.getChannelMask()) {
          return false;
        }
      }
      return true;
    }
    
    public AudioFormat getCaptureFormat()
    {
      return captureFormat;
    }
    
    public int getCaptureSession()
    {
      return captureSession;
    }
    
    public byte[] getData()
    {
      return data;
    }
    
    public int hashCode()
    {
      boolean bool = captureAvailable;
      int i = 1237;
      if (bool) {
        j = 1231;
      } else {
        j = 1237;
      }
      int k = captureDelayMs;
      int m = capturePreambleMs;
      int n = captureSession;
      if (triggerInData) {
        i = 1231;
      }
      i = 31 * (31 * (31 * (31 * (31 * 1 + j) + k) + m) + n) + i;
      int j = i;
      if (captureFormat != null) {
        j = 31 * (31 * (31 * i + captureFormat.getSampleRate()) + captureFormat.getEncoding()) + captureFormat.getChannelMask();
      }
      return 31 * (31 * (31 * j + Arrays.hashCode(data)) + soundModelHandle) + status;
    }
    
    public boolean isCaptureAvailable()
    {
      return captureAvailable;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("RecognitionEvent [status=");
      localStringBuilder.append(status);
      localStringBuilder.append(", soundModelHandle=");
      localStringBuilder.append(soundModelHandle);
      localStringBuilder.append(", captureAvailable=");
      localStringBuilder.append(captureAvailable);
      localStringBuilder.append(", captureSession=");
      localStringBuilder.append(captureSession);
      localStringBuilder.append(", captureDelayMs=");
      localStringBuilder.append(captureDelayMs);
      localStringBuilder.append(", capturePreambleMs=");
      localStringBuilder.append(capturePreambleMs);
      localStringBuilder.append(", triggerInData=");
      localStringBuilder.append(triggerInData);
      Object localObject;
      if (captureFormat == null)
      {
        localObject = "";
      }
      else
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(", sampleRate=");
        ((StringBuilder)localObject).append(captureFormat.getSampleRate());
        localObject = ((StringBuilder)localObject).toString();
      }
      localStringBuilder.append((String)localObject);
      if (captureFormat == null)
      {
        localObject = "";
      }
      else
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(", encoding=");
        ((StringBuilder)localObject).append(captureFormat.getEncoding());
        localObject = ((StringBuilder)localObject).toString();
      }
      localStringBuilder.append((String)localObject);
      if (captureFormat == null)
      {
        localObject = "";
      }
      else
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(", channelMask=");
        ((StringBuilder)localObject).append(captureFormat.getChannelMask());
        localObject = ((StringBuilder)localObject).toString();
      }
      localStringBuilder.append((String)localObject);
      localStringBuilder.append(", data=");
      int i;
      if (data == null) {
        i = 0;
      } else {
        i = data.length;
      }
      localStringBuilder.append(i);
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(status);
      paramParcel.writeInt(soundModelHandle);
      paramParcel.writeByte((byte)captureAvailable);
      paramParcel.writeInt(captureSession);
      paramParcel.writeInt(captureDelayMs);
      paramParcel.writeInt(capturePreambleMs);
      paramParcel.writeByte((byte)triggerInData);
      if (captureFormat != null)
      {
        paramParcel.writeByte((byte)1);
        paramParcel.writeInt(captureFormat.getSampleRate());
        paramParcel.writeInt(captureFormat.getEncoding());
        paramParcel.writeInt(captureFormat.getChannelMask());
      }
      else
      {
        paramParcel.writeByte((byte)0);
      }
      paramParcel.writeBlob(data);
    }
  }
  
  public static class SoundModel
  {
    public static final int TYPE_GENERIC_SOUND = 1;
    public static final int TYPE_KEYPHRASE = 0;
    public static final int TYPE_UNKNOWN = -1;
    public final byte[] data;
    public final int type;
    public final UUID uuid;
    public final UUID vendorUuid;
    
    public SoundModel(UUID paramUUID1, UUID paramUUID2, int paramInt, byte[] paramArrayOfByte)
    {
      uuid = paramUUID1;
      vendorUuid = paramUUID2;
      type = paramInt;
      data = paramArrayOfByte;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (paramObject == null) {
        return false;
      }
      if (!(paramObject instanceof SoundModel)) {
        return false;
      }
      paramObject = (SoundModel)paramObject;
      if (!Arrays.equals(data, data)) {
        return false;
      }
      if (type != type) {
        return false;
      }
      if (uuid == null)
      {
        if (uuid != null) {
          return false;
        }
      }
      else if (!uuid.equals(uuid)) {
        return false;
      }
      if (vendorUuid == null)
      {
        if (vendorUuid != null) {
          return false;
        }
      }
      else if (!vendorUuid.equals(vendorUuid)) {
        return false;
      }
      return true;
    }
    
    public int hashCode()
    {
      int i = Arrays.hashCode(data);
      int j = type;
      UUID localUUID = uuid;
      int k = 0;
      int m;
      if (localUUID == null) {
        m = 0;
      } else {
        m = uuid.hashCode();
      }
      if (vendorUuid != null) {
        k = vendorUuid.hashCode();
      }
      return 31 * (31 * (31 * (31 * 1 + i) + j) + m) + k;
    }
  }
  
  public static class SoundModelEvent
    implements Parcelable
  {
    public static final Parcelable.Creator<SoundModelEvent> CREATOR = new Parcelable.Creator()
    {
      public SoundTrigger.SoundModelEvent createFromParcel(Parcel paramAnonymousParcel)
      {
        return SoundTrigger.SoundModelEvent.fromParcel(paramAnonymousParcel);
      }
      
      public SoundTrigger.SoundModelEvent[] newArray(int paramAnonymousInt)
      {
        return new SoundTrigger.SoundModelEvent[paramAnonymousInt];
      }
    };
    public final byte[] data;
    public final int soundModelHandle;
    public final int status;
    
    SoundModelEvent(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
    {
      status = paramInt1;
      soundModelHandle = paramInt2;
      data = paramArrayOfByte;
    }
    
    private static SoundModelEvent fromParcel(Parcel paramParcel)
    {
      return new SoundModelEvent(paramParcel.readInt(), paramParcel.readInt(), paramParcel.readBlob());
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if (paramObject == null) {
        return false;
      }
      if (getClass() != paramObject.getClass()) {
        return false;
      }
      paramObject = (SoundModelEvent)paramObject;
      if (!Arrays.equals(data, data)) {
        return false;
      }
      if (soundModelHandle != soundModelHandle) {
        return false;
      }
      return status == status;
    }
    
    public int hashCode()
    {
      return 31 * (31 * (31 * 1 + Arrays.hashCode(data)) + soundModelHandle) + status;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("SoundModelEvent [status=");
      localStringBuilder.append(status);
      localStringBuilder.append(", soundModelHandle=");
      localStringBuilder.append(soundModelHandle);
      localStringBuilder.append(", data=");
      int i;
      if (data == null) {
        i = 0;
      } else {
        i = data.length;
      }
      localStringBuilder.append(i);
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(status);
      paramParcel.writeInt(soundModelHandle);
      paramParcel.writeBlob(data);
    }
  }
  
  public static abstract interface StatusListener
  {
    public abstract void onRecognition(SoundTrigger.RecognitionEvent paramRecognitionEvent);
    
    public abstract void onServiceDied();
    
    public abstract void onServiceStateChange(int paramInt);
    
    public abstract void onSoundModelUpdate(SoundTrigger.SoundModelEvent paramSoundModelEvent);
  }
}
