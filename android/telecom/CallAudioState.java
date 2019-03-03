package android.telecom;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CallAudioState
  implements Parcelable
{
  public static final Parcelable.Creator<CallAudioState> CREATOR = new Parcelable.Creator()
  {
    public CallAudioState createFromParcel(Parcel paramAnonymousParcel)
    {
      if (paramAnonymousParcel.readByte() == 0) {}
      for (boolean bool = false;; bool = true) {
        break;
      }
      int i = paramAnonymousParcel.readInt();
      int j = paramAnonymousParcel.readInt();
      BluetoothDevice localBluetoothDevice = (BluetoothDevice)paramAnonymousParcel.readParcelable(ClassLoader.getSystemClassLoader());
      ArrayList localArrayList = new ArrayList();
      paramAnonymousParcel.readParcelableList(localArrayList, ClassLoader.getSystemClassLoader());
      return new CallAudioState(bool, i, j, localBluetoothDevice, localArrayList);
    }
    
    public CallAudioState[] newArray(int paramAnonymousInt)
    {
      return new CallAudioState[paramAnonymousInt];
    }
  };
  public static final int ROUTE_ALL = 15;
  public static final int ROUTE_BLUETOOTH = 2;
  public static final int ROUTE_EARPIECE = 1;
  public static final int ROUTE_SPEAKER = 8;
  public static final int ROUTE_WIRED_HEADSET = 4;
  public static final int ROUTE_WIRED_OR_EARPIECE = 5;
  private final BluetoothDevice activeBluetoothDevice;
  private final boolean isMuted;
  private final int route;
  private final Collection<BluetoothDevice> supportedBluetoothDevices;
  private final int supportedRouteMask;
  
  public CallAudioState(AudioState paramAudioState)
  {
    isMuted = paramAudioState.isMuted();
    route = paramAudioState.getRoute();
    supportedRouteMask = paramAudioState.getSupportedRouteMask();
    activeBluetoothDevice = null;
    supportedBluetoothDevices = Collections.emptyList();
  }
  
  public CallAudioState(CallAudioState paramCallAudioState)
  {
    isMuted = paramCallAudioState.isMuted();
    route = paramCallAudioState.getRoute();
    supportedRouteMask = paramCallAudioState.getSupportedRouteMask();
    activeBluetoothDevice = activeBluetoothDevice;
    supportedBluetoothDevices = paramCallAudioState.getSupportedBluetoothDevices();
  }
  
  public CallAudioState(boolean paramBoolean, int paramInt1, int paramInt2)
  {
    this(paramBoolean, paramInt1, paramInt2, null, Collections.emptyList());
  }
  
  public CallAudioState(boolean paramBoolean, int paramInt1, int paramInt2, BluetoothDevice paramBluetoothDevice, Collection<BluetoothDevice> paramCollection)
  {
    isMuted = paramBoolean;
    route = paramInt1;
    supportedRouteMask = paramInt2;
    activeBluetoothDevice = paramBluetoothDevice;
    supportedBluetoothDevices = paramCollection;
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
    if (!(paramObject instanceof CallAudioState)) {
      return false;
    }
    CallAudioState localCallAudioState = (CallAudioState)paramObject;
    if (supportedBluetoothDevices.size() != supportedBluetoothDevices.size()) {
      return false;
    }
    Iterator localIterator = supportedBluetoothDevices.iterator();
    while (localIterator.hasNext())
    {
      paramObject = (BluetoothDevice)localIterator.next();
      if (!supportedBluetoothDevices.contains(paramObject)) {
        return false;
      }
    }
    boolean bool2 = bool1;
    if (Objects.equals(activeBluetoothDevice, activeBluetoothDevice))
    {
      bool2 = bool1;
      if (isMuted() == localCallAudioState.isMuted())
      {
        bool2 = bool1;
        if (getRoute() == localCallAudioState.getRoute())
        {
          bool2 = bool1;
          if (getSupportedRouteMask() == localCallAudioState.getSupportedRouteMask()) {
            bool2 = true;
          }
        }
      }
    }
    return bool2;
  }
  
  public BluetoothDevice getActiveBluetoothDevice()
  {
    return activeBluetoothDevice;
  }
  
  public int getRoute()
  {
    return route;
  }
  
  public Collection<BluetoothDevice> getSupportedBluetoothDevices()
  {
    return supportedBluetoothDevices;
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
    String str = (String)supportedBluetoothDevices.stream().map(_..Lambda.cyYWqCYT05eM23eLVm4oQ5DrYjw.INSTANCE).collect(Collectors.joining(", "));
    return String.format(Locale.US, "[AudioState isMuted: %b, route: %s, supportedRouteMask: %s, activeBluetoothDevice: [%s], supportedBluetoothDevices: [%s]]", new Object[] { Boolean.valueOf(isMuted), audioRouteToString(route), audioRouteToString(supportedRouteMask), activeBluetoothDevice, str });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeByte((byte)isMuted);
    paramParcel.writeInt(route);
    paramParcel.writeInt(supportedRouteMask);
    paramParcel.writeParcelable(activeBluetoothDevice, 0);
    paramParcel.writeParcelableList(new ArrayList(supportedBluetoothDevices), 0);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface CallAudioRoute {}
}
