package android.media;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Objects;

public final class AudioRecordingConfiguration
  implements Parcelable
{
  public static final Parcelable.Creator<AudioRecordingConfiguration> CREATOR = new Parcelable.Creator()
  {
    public AudioRecordingConfiguration createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AudioRecordingConfiguration(paramAnonymousParcel, null);
    }
    
    public AudioRecordingConfiguration[] newArray(int paramAnonymousInt)
    {
      return new AudioRecordingConfiguration[paramAnonymousInt];
    }
  };
  private static final String TAG = new String("AudioRecordingConfiguration");
  private final AudioFormat mClientFormat;
  private final String mClientPackageName;
  private final int mClientSource;
  private final int mClientUid;
  private final AudioFormat mDeviceFormat;
  private final int mPatchHandle;
  private final int mSessionId;
  
  public AudioRecordingConfiguration(int paramInt1, int paramInt2, int paramInt3, AudioFormat paramAudioFormat1, AudioFormat paramAudioFormat2, int paramInt4, String paramString)
  {
    mClientUid = paramInt1;
    mSessionId = paramInt2;
    mClientSource = paramInt3;
    mClientFormat = paramAudioFormat1;
    mDeviceFormat = paramAudioFormat2;
    mPatchHandle = paramInt4;
    mClientPackageName = paramString;
  }
  
  private AudioRecordingConfiguration(Parcel paramParcel)
  {
    mSessionId = paramParcel.readInt();
    mClientSource = paramParcel.readInt();
    mClientFormat = ((AudioFormat)AudioFormat.CREATOR.createFromParcel(paramParcel));
    mDeviceFormat = ((AudioFormat)AudioFormat.CREATOR.createFromParcel(paramParcel));
    mPatchHandle = paramParcel.readInt();
    mClientPackageName = paramParcel.readString();
    mClientUid = paramParcel.readInt();
  }
  
  public static AudioRecordingConfiguration anonymizedCopy(AudioRecordingConfiguration paramAudioRecordingConfiguration)
  {
    return new AudioRecordingConfiguration(-1, mSessionId, mClientSource, mClientFormat, mDeviceFormat, mPatchHandle, "");
  }
  
  public static String toLogFriendlyString(AudioRecordingConfiguration paramAudioRecordingConfiguration)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("session:");
    localStringBuilder.append(mSessionId);
    localStringBuilder.append(" -- source:");
    localStringBuilder.append(MediaRecorder.toLogFriendlyAudioSource(mClientSource));
    localStringBuilder.append(" -- uid:");
    localStringBuilder.append(mClientUid);
    localStringBuilder.append(" -- patch:");
    localStringBuilder.append(mPatchHandle);
    localStringBuilder.append(" -- pack:");
    localStringBuilder.append(mClientPackageName);
    localStringBuilder.append(" -- format client=");
    localStringBuilder.append(mClientFormat.toLogFriendlyString());
    localStringBuilder.append(", dev=");
    localStringBuilder.append(mDeviceFormat.toLogFriendlyString());
    return new String(localStringBuilder.toString());
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void dump(PrintWriter paramPrintWriter)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("  ");
    localStringBuilder.append(toLogFriendlyString(this));
    paramPrintWriter.println(localStringBuilder.toString());
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && ((paramObject instanceof AudioRecordingConfiguration)))
    {
      paramObject = (AudioRecordingConfiguration)paramObject;
      if ((mClientUid != mClientUid) || (mSessionId != mSessionId) || (mClientSource != mClientSource) || (mPatchHandle != mPatchHandle) || (!mClientFormat.equals(mClientFormat)) || (!mDeviceFormat.equals(mDeviceFormat)) || (!mClientPackageName.equals(mClientPackageName))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public AudioDeviceInfo getAudioDevice()
  {
    Object localObject = new ArrayList();
    if (AudioManager.listAudioPatches((ArrayList)localObject) != 0)
    {
      Log.e(TAG, "Error retrieving list of audio patches");
      return null;
    }
    int i = 0;
    for (int j = 0; j < ((ArrayList)localObject).size(); j++)
    {
      AudioPatch localAudioPatch = (AudioPatch)((ArrayList)localObject).get(j);
      if (localAudioPatch.id() == mPatchHandle)
      {
        localObject = localAudioPatch.sources();
        if ((localObject == null) || (localObject.length <= 0)) {
          break;
        }
        int k = localObject[0].port().id();
        localObject = AudioManager.getDevicesStatic(1);
        for (j = i; j < localObject.length; j++) {
          if (localObject[j].getId() == k) {
            return localObject[j];
          }
        }
        break;
      }
    }
    Log.e(TAG, "Couldn't find device for recording, did recording end already?");
    return null;
  }
  
  public int getClientAudioSessionId()
  {
    return mSessionId;
  }
  
  public int getClientAudioSource()
  {
    return mClientSource;
  }
  
  public AudioFormat getClientFormat()
  {
    return mClientFormat;
  }
  
  public String getClientPackageName()
  {
    return mClientPackageName;
  }
  
  public int getClientUid()
  {
    return mClientUid;
  }
  
  public AudioFormat getFormat()
  {
    return mDeviceFormat;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(mSessionId), Integer.valueOf(mClientSource) });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mSessionId);
    paramParcel.writeInt(mClientSource);
    mClientFormat.writeToParcel(paramParcel, 0);
    mDeviceFormat.writeToParcel(paramParcel, 0);
    paramParcel.writeInt(mPatchHandle);
    paramParcel.writeString(mClientPackageName);
    paramParcel.writeInt(mClientUid);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface AudioSource {}
}
