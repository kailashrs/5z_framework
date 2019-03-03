package android.media.session;

import android.media.AudioAttributes;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ParcelableVolumeInfo
  implements Parcelable
{
  public static final Parcelable.Creator<ParcelableVolumeInfo> CREATOR = new Parcelable.Creator()
  {
    public ParcelableVolumeInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ParcelableVolumeInfo(paramAnonymousParcel);
    }
    
    public ParcelableVolumeInfo[] newArray(int paramAnonymousInt)
    {
      return new ParcelableVolumeInfo[paramAnonymousInt];
    }
  };
  public AudioAttributes audioAttrs;
  public int controlType;
  public int currentVolume;
  public int maxVolume;
  public int volumeType;
  
  public ParcelableVolumeInfo(int paramInt1, AudioAttributes paramAudioAttributes, int paramInt2, int paramInt3, int paramInt4)
  {
    volumeType = paramInt1;
    audioAttrs = paramAudioAttributes;
    controlType = paramInt2;
    maxVolume = paramInt3;
    currentVolume = paramInt4;
  }
  
  public ParcelableVolumeInfo(Parcel paramParcel)
  {
    volumeType = paramParcel.readInt();
    controlType = paramParcel.readInt();
    maxVolume = paramParcel.readInt();
    currentVolume = paramParcel.readInt();
    audioAttrs = ((AudioAttributes)AudioAttributes.CREATOR.createFromParcel(paramParcel));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(volumeType);
    paramParcel.writeInt(controlType);
    paramParcel.writeInt(maxVolume);
    paramParcel.writeInt(currentVolume);
    audioAttrs.writeToParcel(paramParcel, paramInt);
  }
}
