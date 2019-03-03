package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class IccOpenLogicalChannelResponse
  implements Parcelable
{
  public static final Parcelable.Creator<IccOpenLogicalChannelResponse> CREATOR = new Parcelable.Creator()
  {
    public IccOpenLogicalChannelResponse createFromParcel(Parcel paramAnonymousParcel)
    {
      return new IccOpenLogicalChannelResponse(paramAnonymousParcel, null);
    }
    
    public IccOpenLogicalChannelResponse[] newArray(int paramAnonymousInt)
    {
      return new IccOpenLogicalChannelResponse[paramAnonymousInt];
    }
  };
  public static final int INVALID_CHANNEL = -1;
  public static final int STATUS_MISSING_RESOURCE = 2;
  public static final int STATUS_NO_ERROR = 1;
  public static final int STATUS_NO_SUCH_ELEMENT = 3;
  public static final int STATUS_UNKNOWN_ERROR = 4;
  private final int mChannel;
  private final byte[] mSelectResponse;
  private final int mStatus;
  
  public IccOpenLogicalChannelResponse(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    mChannel = paramInt1;
    mStatus = paramInt2;
    mSelectResponse = paramArrayOfByte;
  }
  
  private IccOpenLogicalChannelResponse(Parcel paramParcel)
  {
    mChannel = paramParcel.readInt();
    mStatus = paramParcel.readInt();
    int i = paramParcel.readInt();
    if (i > 0)
    {
      mSelectResponse = new byte[i];
      paramParcel.readByteArray(mSelectResponse);
    }
    else
    {
      mSelectResponse = null;
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getChannel()
  {
    return mChannel;
  }
  
  public byte[] getSelectResponse()
  {
    return mSelectResponse;
  }
  
  public int getStatus()
  {
    return mStatus;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Channel: ");
    localStringBuilder.append(mChannel);
    localStringBuilder.append(" Status: ");
    localStringBuilder.append(mStatus);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mChannel);
    paramParcel.writeInt(mStatus);
    if ((mSelectResponse != null) && (mSelectResponse.length > 0))
    {
      paramParcel.writeInt(mSelectResponse.length);
      paramParcel.writeByteArray(mSelectResponse);
    }
    else
    {
      paramParcel.writeInt(0);
    }
  }
}
