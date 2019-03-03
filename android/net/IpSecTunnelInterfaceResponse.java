package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class IpSecTunnelInterfaceResponse
  implements Parcelable
{
  public static final Parcelable.Creator<IpSecTunnelInterfaceResponse> CREATOR = new Parcelable.Creator()
  {
    public IpSecTunnelInterfaceResponse createFromParcel(Parcel paramAnonymousParcel)
    {
      return new IpSecTunnelInterfaceResponse(paramAnonymousParcel, null);
    }
    
    public IpSecTunnelInterfaceResponse[] newArray(int paramAnonymousInt)
    {
      return new IpSecTunnelInterfaceResponse[paramAnonymousInt];
    }
  };
  private static final String TAG = "IpSecTunnelInterfaceResponse";
  public final String interfaceName;
  public final int resourceId;
  public final int status;
  
  public IpSecTunnelInterfaceResponse(int paramInt)
  {
    if (paramInt != 0)
    {
      status = paramInt;
      resourceId = -1;
      interfaceName = "";
      return;
    }
    throw new IllegalArgumentException("Valid status implies other args must be provided");
  }
  
  public IpSecTunnelInterfaceResponse(int paramInt1, int paramInt2, String paramString)
  {
    status = paramInt1;
    resourceId = paramInt2;
    interfaceName = paramString;
  }
  
  private IpSecTunnelInterfaceResponse(Parcel paramParcel)
  {
    status = paramParcel.readInt();
    resourceId = paramParcel.readInt();
    interfaceName = paramParcel.readString();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(status);
    paramParcel.writeInt(resourceId);
    paramParcel.writeString(interfaceName);
  }
}
