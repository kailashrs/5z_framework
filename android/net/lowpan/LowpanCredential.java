package android.net.lowpan;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.HexDump;
import java.util.Arrays;
import java.util.Objects;

public class LowpanCredential
  implements Parcelable
{
  public static final Parcelable.Creator<LowpanCredential> CREATOR = new Parcelable.Creator()
  {
    public LowpanCredential createFromParcel(Parcel paramAnonymousParcel)
    {
      LowpanCredential localLowpanCredential = new LowpanCredential();
      LowpanCredential.access$002(localLowpanCredential, paramAnonymousParcel.createByteArray());
      LowpanCredential.access$102(localLowpanCredential, paramAnonymousParcel.readInt());
      return localLowpanCredential;
    }
    
    public LowpanCredential[] newArray(int paramAnonymousInt)
    {
      return new LowpanCredential[paramAnonymousInt];
    }
  };
  public static final int UNSPECIFIED_KEY_INDEX = 0;
  private byte[] mMasterKey = null;
  private int mMasterKeyIndex = 0;
  
  LowpanCredential() {}
  
  private LowpanCredential(byte[] paramArrayOfByte)
  {
    setMasterKey(paramArrayOfByte);
  }
  
  private LowpanCredential(byte[] paramArrayOfByte, int paramInt)
  {
    setMasterKey(paramArrayOfByte, paramInt);
  }
  
  public static LowpanCredential createMasterKey(byte[] paramArrayOfByte)
  {
    return new LowpanCredential(paramArrayOfByte);
  }
  
  public static LowpanCredential createMasterKey(byte[] paramArrayOfByte, int paramInt)
  {
    return new LowpanCredential(paramArrayOfByte, paramInt);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof LowpanCredential;
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    paramObject = (LowpanCredential)paramObject;
    bool1 = bool2;
    if (Arrays.equals(mMasterKey, mMasterKey))
    {
      bool1 = bool2;
      if (mMasterKeyIndex == mMasterKeyIndex) {
        bool1 = true;
      }
    }
    return bool1;
  }
  
  public byte[] getMasterKey()
  {
    if (mMasterKey != null) {
      return (byte[])mMasterKey.clone();
    }
    return null;
  }
  
  public int getMasterKeyIndex()
  {
    return mMasterKeyIndex;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(Arrays.hashCode(mMasterKey)), Integer.valueOf(mMasterKeyIndex) });
  }
  
  public boolean isMasterKey()
  {
    boolean bool;
    if (mMasterKey != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  void setMasterKey(byte[] paramArrayOfByte)
  {
    byte[] arrayOfByte = paramArrayOfByte;
    if (paramArrayOfByte != null) {
      arrayOfByte = (byte[])paramArrayOfByte.clone();
    }
    mMasterKey = arrayOfByte;
  }
  
  void setMasterKey(byte[] paramArrayOfByte, int paramInt)
  {
    setMasterKey(paramArrayOfByte);
    setMasterKeyIndex(paramInt);
  }
  
  void setMasterKeyIndex(int paramInt)
  {
    mMasterKeyIndex = paramInt;
  }
  
  public String toSensitiveString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("<LowpanCredential");
    if (isMasterKey())
    {
      localStringBuffer.append(" MasterKey:");
      localStringBuffer.append(HexDump.toHexString(mMasterKey));
      if (mMasterKeyIndex != 0)
      {
        localStringBuffer.append(", Index:");
        localStringBuffer.append(mMasterKeyIndex);
      }
    }
    else
    {
      localStringBuffer.append(" empty");
    }
    localStringBuffer.append(">");
    return localStringBuffer.toString();
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("<LowpanCredential");
    if (isMasterKey())
    {
      localStringBuffer.append(" MasterKey");
      if (mMasterKeyIndex != 0)
      {
        localStringBuffer.append(", Index:");
        localStringBuffer.append(mMasterKeyIndex);
      }
    }
    else
    {
      localStringBuffer.append(" empty");
    }
    localStringBuffer.append(">");
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeByteArray(mMasterKey);
    paramParcel.writeInt(mMasterKeyIndex);
  }
}
