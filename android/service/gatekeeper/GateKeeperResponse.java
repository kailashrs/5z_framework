package android.service.gatekeeper;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.annotations.VisibleForTesting;

public final class GateKeeperResponse
  implements Parcelable
{
  public static final Parcelable.Creator<GateKeeperResponse> CREATOR = new Parcelable.Creator()
  {
    public GateKeeperResponse createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      boolean bool = true;
      if (i == 1)
      {
        paramAnonymousParcel = GateKeeperResponse.createRetryResponse(paramAnonymousParcel.readInt());
      }
      else if (i == 0)
      {
        if (paramAnonymousParcel.readInt() != 1) {
          bool = false;
        }
        byte[] arrayOfByte = null;
        i = paramAnonymousParcel.readInt();
        if (i > 0)
        {
          arrayOfByte = new byte[i];
          paramAnonymousParcel.readByteArray(arrayOfByte);
        }
        paramAnonymousParcel = GateKeeperResponse.createOkResponse(arrayOfByte, bool);
      }
      else
      {
        paramAnonymousParcel = GateKeeperResponse.createGenericResponse(i);
      }
      return paramAnonymousParcel;
    }
    
    public GateKeeperResponse[] newArray(int paramAnonymousInt)
    {
      return new GateKeeperResponse[paramAnonymousInt];
    }
  };
  public static final int RESPONSE_ERROR = -1;
  public static final int RESPONSE_OK = 0;
  public static final int RESPONSE_RETRY = 1;
  private byte[] mPayload;
  private final int mResponseCode;
  private boolean mShouldReEnroll;
  private int mTimeout;
  
  private GateKeeperResponse(int paramInt)
  {
    mResponseCode = paramInt;
  }
  
  @VisibleForTesting
  public static GateKeeperResponse createGenericResponse(int paramInt)
  {
    return new GateKeeperResponse(paramInt);
  }
  
  @VisibleForTesting
  public static GateKeeperResponse createOkResponse(byte[] paramArrayOfByte, boolean paramBoolean)
  {
    GateKeeperResponse localGateKeeperResponse = new GateKeeperResponse(0);
    mPayload = paramArrayOfByte;
    mShouldReEnroll = paramBoolean;
    return localGateKeeperResponse;
  }
  
  private static GateKeeperResponse createRetryResponse(int paramInt)
  {
    GateKeeperResponse localGateKeeperResponse = new GateKeeperResponse(1);
    mTimeout = paramInt;
    return localGateKeeperResponse;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public byte[] getPayload()
  {
    return mPayload;
  }
  
  public int getResponseCode()
  {
    return mResponseCode;
  }
  
  public boolean getShouldReEnroll()
  {
    return mShouldReEnroll;
  }
  
  public int getTimeout()
  {
    return mTimeout;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mResponseCode);
    if (mResponseCode == 1)
    {
      paramParcel.writeInt(mTimeout);
    }
    else if (mResponseCode == 0)
    {
      paramParcel.writeInt(mShouldReEnroll);
      if (mPayload != null)
      {
        paramParcel.writeInt(mPayload.length);
        paramParcel.writeByteArray(mPayload);
      }
      else
      {
        paramParcel.writeInt(0);
      }
    }
  }
}
