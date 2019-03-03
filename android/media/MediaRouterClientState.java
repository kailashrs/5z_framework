package android.media;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;

public final class MediaRouterClientState
  implements Parcelable
{
  public static final Parcelable.Creator<MediaRouterClientState> CREATOR = new Parcelable.Creator()
  {
    public MediaRouterClientState createFromParcel(Parcel paramAnonymousParcel)
    {
      return new MediaRouterClientState(paramAnonymousParcel);
    }
    
    public MediaRouterClientState[] newArray(int paramAnonymousInt)
    {
      return new MediaRouterClientState[paramAnonymousInt];
    }
  };
  public final ArrayList<RouteInfo> routes;
  
  public MediaRouterClientState()
  {
    routes = new ArrayList();
  }
  
  MediaRouterClientState(Parcel paramParcel)
  {
    routes = paramParcel.createTypedArrayList(RouteInfo.CREATOR);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public RouteInfo getRoute(String paramString)
  {
    int i = routes.size();
    for (int j = 0; j < i; j++)
    {
      RouteInfo localRouteInfo = (RouteInfo)routes.get(j);
      if (id.equals(paramString)) {
        return localRouteInfo;
      }
    }
    return null;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("MediaRouterClientState{ routes=");
    localStringBuilder.append(routes.toString());
    localStringBuilder.append(" }");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeTypedList(routes);
  }
  
  public static final class RouteInfo
    implements Parcelable
  {
    public static final Parcelable.Creator<RouteInfo> CREATOR = new Parcelable.Creator()
    {
      public MediaRouterClientState.RouteInfo createFromParcel(Parcel paramAnonymousParcel)
      {
        return new MediaRouterClientState.RouteInfo(paramAnonymousParcel);
      }
      
      public MediaRouterClientState.RouteInfo[] newArray(int paramAnonymousInt)
      {
        return new MediaRouterClientState.RouteInfo[paramAnonymousInt];
      }
    };
    public String description;
    public int deviceType;
    public boolean enabled;
    public String id;
    public String name;
    public int playbackStream;
    public int playbackType;
    public int presentationDisplayId;
    public int statusCode;
    public int supportedTypes;
    public int volume;
    public int volumeHandling;
    public int volumeMax;
    
    public RouteInfo(RouteInfo paramRouteInfo)
    {
      id = id;
      name = name;
      description = description;
      supportedTypes = supportedTypes;
      enabled = enabled;
      statusCode = statusCode;
      playbackType = playbackType;
      playbackStream = playbackStream;
      volume = volume;
      volumeMax = volumeMax;
      volumeHandling = volumeHandling;
      presentationDisplayId = presentationDisplayId;
      deviceType = deviceType;
    }
    
    RouteInfo(Parcel paramParcel)
    {
      id = paramParcel.readString();
      name = paramParcel.readString();
      description = paramParcel.readString();
      supportedTypes = paramParcel.readInt();
      boolean bool;
      if (paramParcel.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      enabled = bool;
      statusCode = paramParcel.readInt();
      playbackType = paramParcel.readInt();
      playbackStream = paramParcel.readInt();
      volume = paramParcel.readInt();
      volumeMax = paramParcel.readInt();
      volumeHandling = paramParcel.readInt();
      presentationDisplayId = paramParcel.readInt();
      deviceType = paramParcel.readInt();
    }
    
    public RouteInfo(String paramString)
    {
      id = paramString;
      enabled = true;
      statusCode = 0;
      playbackType = 1;
      playbackStream = -1;
      volumeHandling = 0;
      presentationDisplayId = -1;
      deviceType = 0;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("RouteInfo{ id=");
      localStringBuilder.append(id);
      localStringBuilder.append(", name=");
      localStringBuilder.append(name);
      localStringBuilder.append(", description=");
      localStringBuilder.append(description);
      localStringBuilder.append(", supportedTypes=0x");
      localStringBuilder.append(Integer.toHexString(supportedTypes));
      localStringBuilder.append(", enabled=");
      localStringBuilder.append(enabled);
      localStringBuilder.append(", statusCode=");
      localStringBuilder.append(statusCode);
      localStringBuilder.append(", playbackType=");
      localStringBuilder.append(playbackType);
      localStringBuilder.append(", playbackStream=");
      localStringBuilder.append(playbackStream);
      localStringBuilder.append(", volume=");
      localStringBuilder.append(volume);
      localStringBuilder.append(", volumeMax=");
      localStringBuilder.append(volumeMax);
      localStringBuilder.append(", volumeHandling=");
      localStringBuilder.append(volumeHandling);
      localStringBuilder.append(", presentationDisplayId=");
      localStringBuilder.append(presentationDisplayId);
      localStringBuilder.append(", deviceType=");
      localStringBuilder.append(deviceType);
      localStringBuilder.append(" }");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeString(id);
      paramParcel.writeString(name);
      paramParcel.writeString(description);
      paramParcel.writeInt(supportedTypes);
      paramParcel.writeInt(enabled);
      paramParcel.writeInt(statusCode);
      paramParcel.writeInt(playbackType);
      paramParcel.writeInt(playbackStream);
      paramParcel.writeInt(volume);
      paramParcel.writeInt(volumeMax);
      paramParcel.writeInt(volumeHandling);
      paramParcel.writeInt(presentationDisplayId);
      paramParcel.writeInt(deviceType);
    }
  }
}
