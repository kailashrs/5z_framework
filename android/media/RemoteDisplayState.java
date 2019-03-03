package android.media;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.util.ArrayList;

public final class RemoteDisplayState
  implements Parcelable
{
  public static final Parcelable.Creator<RemoteDisplayState> CREATOR = new Parcelable.Creator()
  {
    public RemoteDisplayState createFromParcel(Parcel paramAnonymousParcel)
    {
      return new RemoteDisplayState(paramAnonymousParcel);
    }
    
    public RemoteDisplayState[] newArray(int paramAnonymousInt)
    {
      return new RemoteDisplayState[paramAnonymousInt];
    }
  };
  public static final int DISCOVERY_MODE_ACTIVE = 2;
  public static final int DISCOVERY_MODE_NONE = 0;
  public static final int DISCOVERY_MODE_PASSIVE = 1;
  public static final String SERVICE_INTERFACE = "com.android.media.remotedisplay.RemoteDisplayProvider";
  public final ArrayList<RemoteDisplayInfo> displays;
  
  public RemoteDisplayState()
  {
    displays = new ArrayList();
  }
  
  RemoteDisplayState(Parcel paramParcel)
  {
    displays = paramParcel.createTypedArrayList(RemoteDisplayInfo.CREATOR);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean isValid()
  {
    if (displays == null) {
      return false;
    }
    int i = displays.size();
    for (int j = 0; j < i; j++) {
      if (!((RemoteDisplayInfo)displays.get(j)).isValid()) {
        return false;
      }
    }
    return true;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeTypedList(displays);
  }
  
  public static final class RemoteDisplayInfo
    implements Parcelable
  {
    public static final Parcelable.Creator<RemoteDisplayInfo> CREATOR = new Parcelable.Creator()
    {
      public RemoteDisplayState.RemoteDisplayInfo createFromParcel(Parcel paramAnonymousParcel)
      {
        return new RemoteDisplayState.RemoteDisplayInfo(paramAnonymousParcel);
      }
      
      public RemoteDisplayState.RemoteDisplayInfo[] newArray(int paramAnonymousInt)
      {
        return new RemoteDisplayState.RemoteDisplayInfo[paramAnonymousInt];
      }
    };
    public static final int PLAYBACK_VOLUME_FIXED = 0;
    public static final int PLAYBACK_VOLUME_VARIABLE = 1;
    public static final int STATUS_AVAILABLE = 2;
    public static final int STATUS_CONNECTED = 4;
    public static final int STATUS_CONNECTING = 3;
    public static final int STATUS_IN_USE = 1;
    public static final int STATUS_NOT_AVAILABLE = 0;
    public String description;
    public String id;
    public String name;
    public int presentationDisplayId;
    public int status;
    public int volume;
    public int volumeHandling;
    public int volumeMax;
    
    public RemoteDisplayInfo(RemoteDisplayInfo paramRemoteDisplayInfo)
    {
      id = id;
      name = name;
      description = description;
      status = status;
      volume = volume;
      volumeMax = volumeMax;
      volumeHandling = volumeHandling;
      presentationDisplayId = presentationDisplayId;
    }
    
    RemoteDisplayInfo(Parcel paramParcel)
    {
      id = paramParcel.readString();
      name = paramParcel.readString();
      description = paramParcel.readString();
      status = paramParcel.readInt();
      volume = paramParcel.readInt();
      volumeMax = paramParcel.readInt();
      volumeHandling = paramParcel.readInt();
      presentationDisplayId = paramParcel.readInt();
    }
    
    public RemoteDisplayInfo(String paramString)
    {
      id = paramString;
      status = 0;
      volumeHandling = 0;
      presentationDisplayId = -1;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean isValid()
    {
      boolean bool;
      if ((!TextUtils.isEmpty(id)) && (!TextUtils.isEmpty(name))) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("RemoteDisplayInfo{ id=");
      localStringBuilder.append(id);
      localStringBuilder.append(", name=");
      localStringBuilder.append(name);
      localStringBuilder.append(", description=");
      localStringBuilder.append(description);
      localStringBuilder.append(", status=");
      localStringBuilder.append(status);
      localStringBuilder.append(", volume=");
      localStringBuilder.append(volume);
      localStringBuilder.append(", volumeMax=");
      localStringBuilder.append(volumeMax);
      localStringBuilder.append(", volumeHandling=");
      localStringBuilder.append(volumeHandling);
      localStringBuilder.append(", presentationDisplayId=");
      localStringBuilder.append(presentationDisplayId);
      localStringBuilder.append(" }");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeString(id);
      paramParcel.writeString(name);
      paramParcel.writeString(description);
      paramParcel.writeInt(status);
      paramParcel.writeInt(volume);
      paramParcel.writeInt(volumeMax);
      paramParcel.writeInt(volumeHandling);
      paramParcel.writeInt(presentationDisplayId);
    }
  }
}
