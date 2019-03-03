package android.nfc;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.IOException;

public final class TransceiveResult
  implements Parcelable
{
  public static final Parcelable.Creator<TransceiveResult> CREATOR = new Parcelable.Creator()
  {
    public TransceiveResult createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      if (i == 0)
      {
        byte[] arrayOfByte = new byte[paramAnonymousParcel.readInt()];
        paramAnonymousParcel.readByteArray(arrayOfByte);
        paramAnonymousParcel = arrayOfByte;
      }
      else
      {
        paramAnonymousParcel = null;
      }
      return new TransceiveResult(i, paramAnonymousParcel);
    }
    
    public TransceiveResult[] newArray(int paramAnonymousInt)
    {
      return new TransceiveResult[paramAnonymousInt];
    }
  };
  public static final int RESULT_EXCEEDED_LENGTH = 3;
  public static final int RESULT_FAILURE = 1;
  public static final int RESULT_SUCCESS = 0;
  public static final int RESULT_TAGLOST = 2;
  final byte[] mResponseData;
  final int mResult;
  
  public TransceiveResult(int paramInt, byte[] paramArrayOfByte)
  {
    mResult = paramInt;
    mResponseData = paramArrayOfByte;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public byte[] getResponseOrThrow()
    throws IOException
  {
    int i = mResult;
    if (i != 0)
    {
      switch (i)
      {
      default: 
        throw new IOException("Transceive failed");
      case 3: 
        throw new IOException("Transceive length exceeds supported maximum");
      }
      throw new TagLostException("Tag was lost.");
    }
    return mResponseData;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mResult);
    if (mResult == 0)
    {
      paramParcel.writeInt(mResponseData.length);
      paramParcel.writeByteArray(mResponseData);
    }
  }
}
