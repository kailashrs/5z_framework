package android.telecom;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Locale;

@SystemApi
@Deprecated
public class AudioState
  implements Parcelable
{
  public static final Parcelable.Creator<AudioState> CREATOR = new Parcelable.Creator()
  {
    public AudioState createFromParcel(Parcel paramAnonymousParcel)
    {
      boolean bool;
      if (paramAnonymousParcel.readByte() == 0) {
        bool = false;
      } else {
        bool = true;
      }
      return new AudioState(bool, paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt());
    }
    
    public AudioState[] newArray(int paramAnonymousInt)
    {
      return new AudioState[paramAnonymousInt];
    }
  };
  private static final int ROUTE_ALL = 15;
  public static final int ROUTE_BLUETOOTH = 2;
  public static final int ROUTE_EARPIECE = 1;
  public static final int ROUTE_SPEAKER = 8;
  public static final int ROUTE_WIRED_HEADSET = 4;
  public static final int ROUTE_WIRED_OR_EARPIECE = 5;
  private final boolean isMuted;
  private final int route;
  private final int supportedRouteMask;
  
  public AudioState(AudioState paramAudioState)
  {
    isMuted = paramAudioState.isMuted();
    route = paramAudioState.getRoute();
    supportedRouteMask = paramAudioState.getSupportedRouteMask();
  }
  
  public AudioState(CallAudioState paramCallAudioState)
  {
    isMuted = paramCallAudioState.isMuted();
    route = paramCallAudioState.getRoute();
    supportedRouteMask = paramCallAudioState.getSupportedRouteMask();
  }
  
  public AudioState(boolean paramBoolean, int paramInt1, int paramInt2)
  {
    isMuted = paramBoolean;
    route = paramInt1;
    supportedRouteMask = paramInt2;
  }
  
  public static String audioRouteToString(int paramInt)
  {
    if ((paramInt != 0) && ((paramInt & 0xFFFFFFF0) == 0))
    {
      StringBuffer localStringBuffer = new StringBuffer();
      if ((paramInt & 0x1) == 1) {
        listAppend(localStringBuffer, "EARPIECE");
      }
      if ((paramInt & 0x2) == 2) {
        listAppend(localStringBuffer, "BLUETOOTH");
      }
      if ((paramInt & 0x4) == 4) {
        listAppend(localStringBuffer, "WIRED_HEADSET");
      }
      if ((paramInt & 0x8) == 8) {
        listAppend(localStringBuffer, "SPEAKER");
      }
      return localStringBuffer.toString();
    }
    return "UNKNOWN";
  }
  
  private static void listAppend(StringBuffer paramStringBuffer, String paramString)
  {
    if (paramStringBuffer.length() > 0) {
      paramStringBuffer.append(", ");
    }
    paramStringBuffer.append(paramString);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = false;
    if (paramObject == null) {
      return false;
    }
    if (!(paramObject instanceof AudioState)) {
      return false;
    }
    paramObject = (AudioState)paramObject;
    boolean bool2 = bool1;
    if (isMuted() == paramObject.isMuted())
    {
      bool2 = bool1;
      if (getRoute() == paramObject.getRoute())
      {
        bool2 = bool1;
        if (getSupportedRouteMask() == paramObject.getSupportedRouteMask()) {
          bool2 = true;
        }
      }
    }
    return bool2;
  }
  
  public int getRoute()
  {
    return route;
  }
  
  public int getSupportedRouteMask()
  {
    return supportedRouteMask;
  }
  
  public boolean isMuted()
  {
    return isMuted;
  }
  
  public String toString()
  {
    return String.format(Locale.US, "[AudioState isMuted: %b, route: %s, supportedRouteMask: %s]", new Object[] { Boolean.valueOf(isMuted), audioRouteToString(route), audioRouteToString(supportedRouteMask) });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeByte((byte)isMuted);
    paramParcel.writeInt(route);
    paramParcel.writeInt(supportedRouteMask);
  }
}
