package android.nfc;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.nio.ByteBuffer;
import java.util.Arrays;

public final class NdefMessage
  implements Parcelable
{
  public static final Parcelable.Creator<NdefMessage> CREATOR = new Parcelable.Creator()
  {
    public NdefMessage createFromParcel(Parcel paramAnonymousParcel)
    {
      NdefRecord[] arrayOfNdefRecord = new NdefRecord[paramAnonymousParcel.readInt()];
      paramAnonymousParcel.readTypedArray(arrayOfNdefRecord, NdefRecord.CREATOR);
      return new NdefMessage(arrayOfNdefRecord);
    }
    
    public NdefMessage[] newArray(int paramAnonymousInt)
    {
      return new NdefMessage[paramAnonymousInt];
    }
  };
  private final NdefRecord[] mRecords;
  
  public NdefMessage(NdefRecord paramNdefRecord, NdefRecord... paramVarArgs)
  {
    if (paramNdefRecord != null)
    {
      int i = paramVarArgs.length;
      int j = 0;
      while (j < i) {
        if (paramVarArgs[j] != null) {
          j++;
        } else {
          throw new NullPointerException("record cannot be null");
        }
      }
      mRecords = new NdefRecord[paramVarArgs.length + 1];
      mRecords[0] = paramNdefRecord;
      System.arraycopy(paramVarArgs, 0, mRecords, 1, paramVarArgs.length);
      return;
    }
    throw new NullPointerException("record cannot be null");
  }
  
  public NdefMessage(byte[] paramArrayOfByte)
    throws FormatException
  {
    if (paramArrayOfByte != null)
    {
      paramArrayOfByte = ByteBuffer.wrap(paramArrayOfByte);
      mRecords = NdefRecord.parse(paramArrayOfByte, false);
      if (paramArrayOfByte.remaining() <= 0) {
        return;
      }
      throw new FormatException("trailing data");
    }
    throw new NullPointerException("data is null");
  }
  
  public NdefMessage(NdefRecord[] paramArrayOfNdefRecord)
  {
    if (paramArrayOfNdefRecord.length >= 1)
    {
      int i = paramArrayOfNdefRecord.length;
      int j = 0;
      while (j < i) {
        if (paramArrayOfNdefRecord[j] != null) {
          j++;
        } else {
          throw new NullPointerException("records cannot contain null");
        }
      }
      mRecords = paramArrayOfNdefRecord;
      return;
    }
    throw new IllegalArgumentException("must have at least one record");
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (paramObject == null) {
      return false;
    }
    if (getClass() != paramObject.getClass()) {
      return false;
    }
    paramObject = (NdefMessage)paramObject;
    return Arrays.equals(mRecords, mRecords);
  }
  
  public int getByteArrayLength()
  {
    int i = 0;
    NdefRecord[] arrayOfNdefRecord = mRecords;
    int j = arrayOfNdefRecord.length;
    for (int k = 0; k < j; k++) {
      i += arrayOfNdefRecord[k].getByteLength();
    }
    return i;
  }
  
  public NdefRecord[] getRecords()
  {
    return mRecords;
  }
  
  public int hashCode()
  {
    return Arrays.hashCode(mRecords);
  }
  
  public byte[] toByteArray()
  {
    ByteBuffer localByteBuffer = ByteBuffer.allocate(getByteArrayLength());
    for (int i = 0; i < mRecords.length; i++)
    {
      boolean bool1 = true;
      boolean bool2;
      if (i == 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      if (i != mRecords.length - 1) {
        bool1 = false;
      }
      mRecords[i].writeToByteBuffer(localByteBuffer, bool2, bool1);
    }
    return localByteBuffer.array();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("NdefMessage ");
    localStringBuilder.append(Arrays.toString(mRecords));
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mRecords.length);
    paramParcel.writeTypedArray(mRecords, paramInt);
  }
}
