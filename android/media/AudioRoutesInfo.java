package android.media;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;

public class AudioRoutesInfo
  implements Parcelable
{
  public static final Parcelable.Creator<AudioRoutesInfo> CREATOR = new Parcelable.Creator()
  {
    public AudioRoutesInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AudioRoutesInfo(paramAnonymousParcel);
    }
    
    public AudioRoutesInfo[] newArray(int paramAnonymousInt)
    {
      return new AudioRoutesInfo[paramAnonymousInt];
    }
  };
  public static final int MAIN_DOCK_SPEAKERS = 4;
  public static final int MAIN_HDMI = 8;
  public static final int MAIN_HEADPHONES = 2;
  public static final int MAIN_HEADSET = 1;
  public static final int MAIN_SPEAKER = 0;
  public static final int MAIN_USB = 16;
  public CharSequence bluetoothName;
  public int mainType = 0;
  
  public AudioRoutesInfo() {}
  
  public AudioRoutesInfo(AudioRoutesInfo paramAudioRoutesInfo)
  {
    bluetoothName = bluetoothName;
    mainType = mainType;
  }
  
  AudioRoutesInfo(Parcel paramParcel)
  {
    bluetoothName = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    mainType = paramParcel.readInt();
  }
  
  private static String typeToString(int paramInt)
  {
    if (paramInt == 0) {
      return "SPEAKER";
    }
    if ((paramInt & 0x1) != 0) {
      return "HEADSET";
    }
    if ((paramInt & 0x2) != 0) {
      return "HEADPHONES";
    }
    if ((paramInt & 0x4) != 0) {
      return "DOCK_SPEAKERS";
    }
    if ((paramInt & 0x8) != 0) {
      return "HDMI";
    }
    if ((paramInt & 0x10) != 0) {
      return "USB";
    }
    return Integer.toHexString(paramInt);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(getClass().getSimpleName());
    localStringBuilder.append("{ type=");
    localStringBuilder.append(typeToString(mainType));
    Object localObject;
    if (TextUtils.isEmpty(bluetoothName))
    {
      localObject = "";
    }
    else
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(", bluetoothName=");
      ((StringBuilder)localObject).append(bluetoothName);
      localObject = ((StringBuilder)localObject).toString();
    }
    localStringBuilder.append((String)localObject);
    localStringBuilder.append(" }");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    TextUtils.writeToParcel(bluetoothName, paramParcel, paramInt);
    paramParcel.writeInt(mainType);
  }
}
