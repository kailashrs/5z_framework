package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class IpSecTransformResponse
  implements Parcelable
{
  public static final Parcelable.Creator<IpSecTransformResponse> CREATOR = new Parcelable.Creator()
  {
    public IpSecTransformResponse createFromParcel(Parcel paramAnonymousParcel)
    {
      return new IpSecTransformResponse(paramAnonymousParcel, null);
    }
    
    public IpSecTransformResponse[] newArray(int paramAnonymousInt)
    {
      return new IpSecTransformResponse[paramAnonymousInt];
    }
  };
  private static final String TAG = "IpSecTransformResponse";
  public final int resourceId;
  public final int status;
  
  public IpSecTransformResponse(int paramInt)
  {
    if (paramInt != 0)
    {
      status = paramInt;
      resourceId = -1;
      return;
    }
    throw new IllegalArgumentException("Valid status implies other args must be provided");
  }
  
  public IpSecTransformResponse(int paramInt1, int paramInt2)
  {
    status = paramInt1;
    resourceId = paramInt2;
  }
  
  private IpSecTransformResponse(Parcel paramParcel)
  {
    status = paramParcel.readInt();
    resourceId = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(status);
    paramParcel.writeInt(resourceId);
  }
}
