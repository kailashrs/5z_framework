package com.android.internal.widget;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.service.gatekeeper.GateKeeperResponse;
import android.util.Slog;

public final class VerifyCredentialResponse
  implements Parcelable
{
  public static final Parcelable.Creator<VerifyCredentialResponse> CREATOR = new Parcelable.Creator()
  {
    public VerifyCredentialResponse createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      VerifyCredentialResponse localVerifyCredentialResponse = new VerifyCredentialResponse(i, 0, null, null);
      if (i == 1)
      {
        localVerifyCredentialResponse.setTimeout(paramAnonymousParcel.readInt());
      }
      else if (i == 0)
      {
        i = paramAnonymousParcel.readInt();
        if (i > 0)
        {
          byte[] arrayOfByte = new byte[i];
          paramAnonymousParcel.readByteArray(arrayOfByte);
          localVerifyCredentialResponse.setPayload(arrayOfByte);
        }
      }
      return localVerifyCredentialResponse;
    }
    
    public VerifyCredentialResponse[] newArray(int paramAnonymousInt)
    {
      return new VerifyCredentialResponse[paramAnonymousInt];
    }
  };
  public static final VerifyCredentialResponse ERROR;
  public static final VerifyCredentialResponse OK = new VerifyCredentialResponse();
  public static final int RESPONSE_ERROR = -1;
  public static final int RESPONSE_OK = 0;
  public static final int RESPONSE_RETRY = 1;
  private static final String TAG = "VerifyCredentialResponse";
  private byte[] mPayload;
  private int mResponseCode;
  private int mTimeout;
  
  static
  {
    ERROR = new VerifyCredentialResponse(-1, 0, null);
  }
  
  public VerifyCredentialResponse()
  {
    mResponseCode = 0;
    mPayload = null;
  }
  
  public VerifyCredentialResponse(int paramInt)
  {
    mTimeout = paramInt;
    mResponseCode = 1;
    mPayload = null;
  }
  
  private VerifyCredentialResponse(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    mResponseCode = paramInt1;
    mTimeout = paramInt2;
    mPayload = paramArrayOfByte;
  }
  
  public VerifyCredentialResponse(byte[] paramArrayOfByte)
  {
    mPayload = paramArrayOfByte;
    mResponseCode = 0;
  }
  
  public static VerifyCredentialResponse fromGateKeeperResponse(GateKeeperResponse paramGateKeeperResponse)
  {
    int i = paramGateKeeperResponse.getResponseCode();
    if (i == 1)
    {
      paramGateKeeperResponse = new VerifyCredentialResponse(paramGateKeeperResponse.getTimeout());
    }
    else if (i == 0)
    {
      paramGateKeeperResponse = paramGateKeeperResponse.getPayload();
      if (paramGateKeeperResponse == null)
      {
        Slog.e("VerifyCredentialResponse", "verifyChallenge response had no associated payload");
        paramGateKeeperResponse = ERROR;
      }
      else
      {
        paramGateKeeperResponse = new VerifyCredentialResponse(paramGateKeeperResponse);
      }
    }
    else
    {
      paramGateKeeperResponse = ERROR;
    }
    return paramGateKeeperResponse;
  }
  
  private void setPayload(byte[] paramArrayOfByte)
  {
    mPayload = paramArrayOfByte;
  }
  
  private void setTimeout(int paramInt)
  {
    mTimeout = paramInt;
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
  
  public int getTimeout()
  {
    return mTimeout;
  }
  
  public VerifyCredentialResponse stripPayload()
  {
    return new VerifyCredentialResponse(mResponseCode, mTimeout, new byte[0]);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mResponseCode);
    if (mResponseCode == 1) {
      paramParcel.writeInt(mTimeout);
    } else if (mResponseCode == 0) {
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
