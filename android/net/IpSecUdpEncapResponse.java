package android.net;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.FileDescriptor;
import java.io.IOException;

public final class IpSecUdpEncapResponse
  implements Parcelable
{
  public static final Parcelable.Creator<IpSecUdpEncapResponse> CREATOR = new Parcelable.Creator()
  {
    public IpSecUdpEncapResponse createFromParcel(Parcel paramAnonymousParcel)
    {
      return new IpSecUdpEncapResponse(paramAnonymousParcel, null);
    }
    
    public IpSecUdpEncapResponse[] newArray(int paramAnonymousInt)
    {
      return new IpSecUdpEncapResponse[paramAnonymousInt];
    }
  };
  private static final String TAG = "IpSecUdpEncapResponse";
  public final ParcelFileDescriptor fileDescriptor;
  public final int port;
  public final int resourceId;
  public final int status;
  
  public IpSecUdpEncapResponse(int paramInt)
  {
    if (paramInt != 0)
    {
      status = paramInt;
      resourceId = -1;
      port = -1;
      fileDescriptor = null;
      return;
    }
    throw new IllegalArgumentException("Valid status implies other args must be provided");
  }
  
  public IpSecUdpEncapResponse(int paramInt1, int paramInt2, int paramInt3, FileDescriptor paramFileDescriptor)
    throws IOException
  {
    if ((paramInt1 == 0) && (paramFileDescriptor == null)) {
      throw new IllegalArgumentException("Valid status implies FD must be non-null");
    }
    status = paramInt1;
    resourceId = paramInt2;
    port = paramInt3;
    if (status == 0) {
      paramFileDescriptor = ParcelFileDescriptor.dup(paramFileDescriptor);
    } else {
      paramFileDescriptor = null;
    }
    fileDescriptor = paramFileDescriptor;
  }
  
  private IpSecUdpEncapResponse(Parcel paramParcel)
  {
    status = paramParcel.readInt();
    resourceId = paramParcel.readInt();
    port = paramParcel.readInt();
    fileDescriptor = ((ParcelFileDescriptor)paramParcel.readParcelable(ParcelFileDescriptor.class.getClassLoader()));
  }
  
  public int describeContents()
  {
    int i;
    if (fileDescriptor != null) {
      i = 1;
    } else {
      i = 0;
    }
    return i;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(status);
    paramParcel.writeInt(resourceId);
    paramParcel.writeInt(port);
    paramParcel.writeParcelable(fileDescriptor, 1);
  }
}
