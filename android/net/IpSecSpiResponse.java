package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class IpSecSpiResponse
  implements Parcelable
{
  public static final Parcelable.Creator<IpSecSpiResponse> CREATOR = new Parcelable.Creator()
  {
    public IpSecSpiResponse createFromParcel(Parcel paramAnonymousParcel)
    {
      return new IpSecSpiResponse(paramAnonymousParcel, null);
    }
    
    public IpSecSpiResponse[] newArray(int paramAnonymousInt)
    {
      return new IpSecSpiResponse[paramAnonymousInt];
    }
  };
  private static final String TAG = "IpSecSpiResponse";
  public final int resourceId;
  public final int spi;
  public final int status;
  
  public IpSecSpiResponse(int paramInt)
  {
    if (paramInt != 0)
    {
      status = paramInt;
      resourceId = -1;
      spi = 0;
      return;
    }
    throw new IllegalArgumentException("Valid status implies other args must be provided");
  }
  
  public IpSecSpiResponse(int paramInt1, int paramInt2, int paramInt3)
  {
    status = paramInt1;
    resourceId = paramInt2;
    spi = paramInt3;
  }
  
  private IpSecSpiResponse(Parcel paramParcel)
  {
    status = paramParcel.readInt();
    resourceId = paramParcel.readInt();
    spi = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(status);
    paramParcel.writeInt(resourceId);
    paramParcel.writeInt(spi);
  }
}
