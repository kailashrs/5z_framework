package android.hardware.hdmi;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public final class HdmiHotplugEvent
  implements Parcelable
{
  public static final Parcelable.Creator<HdmiHotplugEvent> CREATOR = new Parcelable.Creator()
  {
    public HdmiHotplugEvent createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      int j = paramAnonymousParcel.readByte();
      boolean bool = true;
      if (j != 1) {
        bool = false;
      }
      return new HdmiHotplugEvent(i, bool);
    }
    
    public HdmiHotplugEvent[] newArray(int paramAnonymousInt)
    {
      return new HdmiHotplugEvent[paramAnonymousInt];
    }
  };
  private final boolean mConnected;
  private final int mPort;
  
  public HdmiHotplugEvent(int paramInt, boolean paramBoolean)
  {
    mPort = paramInt;
    mConnected = paramBoolean;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getPort()
  {
    return mPort;
  }
  
  public boolean isConnected()
  {
    return mConnected;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mPort);
    paramParcel.writeByte((byte)mConnected);
  }
}
