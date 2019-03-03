package android.net.nsd;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class DnsSdTxtRecord
  implements Parcelable
{
  public static final Parcelable.Creator<DnsSdTxtRecord> CREATOR = new Parcelable.Creator()
  {
    public DnsSdTxtRecord createFromParcel(Parcel paramAnonymousParcel)
    {
      DnsSdTxtRecord localDnsSdTxtRecord = new DnsSdTxtRecord();
      paramAnonymousParcel.readByteArray(mData);
      return localDnsSdTxtRecord;
    }
    
    public DnsSdTxtRecord[] newArray(int paramAnonymousInt)
    {
      return new DnsSdTxtRecord[paramAnonymousInt];
    }
  };
  private static final byte mSeperator = 61;
  private byte[] mData;
  
  public DnsSdTxtRecord()
  {
    mData = new byte[0];
  }
  
  public DnsSdTxtRecord(DnsSdTxtRecord paramDnsSdTxtRecord)
  {
    if ((paramDnsSdTxtRecord != null) && (mData != null)) {
      mData = ((byte[])mData.clone());
    }
  }
  
  public DnsSdTxtRecord(byte[] paramArrayOfByte)
  {
    mData = ((byte[])paramArrayOfByte.clone());
  }
  
  private String getKey(int paramInt)
  {
    int i = 0;
    for (int j = 0; (j < paramInt) && (i < mData.length); j++) {
      i += mData[i] + 1;
    }
    if (i < mData.length)
    {
      j = mData[i];
      for (paramInt = 0; (paramInt < j) && (mData[(i + paramInt + 1)] != 61); paramInt++) {}
      return new String(mData, i + 1, paramInt);
    }
    return null;
  }
  
  private byte[] getValue(int paramInt)
  {
    Object localObject1 = null;
    int i = 0;
    for (int j = 0; (j < paramInt) && (i < mData.length); j++) {
      i += mData[i] + 1;
    }
    Object localObject2 = localObject1;
    if (i < mData.length)
    {
      j = mData[i];
      for (paramInt = 0;; paramInt++)
      {
        localObject2 = localObject1;
        if (paramInt >= j) {
          break;
        }
        if (mData[(i + paramInt + 1)] == 61)
        {
          localObject2 = new byte[j - paramInt - 1];
          System.arraycopy(mData, i + paramInt + 2, (byte[])localObject2, 0, j - paramInt - 1);
          break;
        }
      }
    }
    return localObject2;
  }
  
  private byte[] getValue(String paramString)
  {
    for (int i = 0;; i++)
    {
      String str = getKey(i);
      if (str == null) {
        break;
      }
      if (paramString.compareToIgnoreCase(str) == 0) {
        return getValue(i);
      }
    }
    return null;
  }
  
  private String getValueAsString(int paramInt)
  {
    Object localObject = getValue(paramInt);
    if (localObject != null) {
      localObject = new String((byte[])localObject);
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  private void insert(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
  {
    byte[] arrayOfByte = mData;
    int i;
    if (paramArrayOfByte2 != null) {
      i = paramArrayOfByte2.length;
    } else {
      i = 0;
    }
    int j = 0;
    for (int k = 0; (k < paramInt) && (j < mData.length); k++) {
      j += (0xFF & mData[j] + 1);
    }
    k = paramArrayOfByte1.length;
    if (paramArrayOfByte2 != null) {
      paramInt = 1;
    } else {
      paramInt = 0;
    }
    int m = k + i + paramInt;
    paramInt = arrayOfByte.length + m + 1;
    mData = new byte[paramInt];
    System.arraycopy(arrayOfByte, 0, mData, 0, j);
    k = arrayOfByte.length - j;
    System.arraycopy(arrayOfByte, j, mData, paramInt - k, k);
    mData[j] = ((byte)(byte)m);
    System.arraycopy(paramArrayOfByte1, 0, mData, j + 1, paramArrayOfByte1.length);
    if (paramArrayOfByte2 != null)
    {
      mData[(j + 1 + paramArrayOfByte1.length)] = ((byte)61);
      System.arraycopy(paramArrayOfByte2, 0, mData, paramArrayOfByte1.length + j + 2, i);
    }
  }
  
  public boolean contains(String paramString)
  {
    for (int i = 0;; i++)
    {
      String str = getKey(i);
      if (str == null) {
        break;
      }
      if (paramString.compareToIgnoreCase(str) == 0) {
        return true;
      }
    }
    return false;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == this) {
      return true;
    }
    if (!(paramObject instanceof DnsSdTxtRecord)) {
      return false;
    }
    return Arrays.equals(mData, mData);
  }
  
  public String get(String paramString)
  {
    paramString = getValue(paramString);
    if (paramString != null) {
      paramString = new String(paramString);
    } else {
      paramString = null;
    }
    return paramString;
  }
  
  public byte[] getRawData()
  {
    return (byte[])mData.clone();
  }
  
  public int hashCode()
  {
    return Arrays.hashCode(mData);
  }
  
  public int keyCount()
  {
    int i = 0;
    int j = 0;
    while (j < mData.length)
    {
      j += (0xFF & mData[j] + 1);
      i++;
    }
    return i;
  }
  
  public int remove(String paramString)
  {
    int i = 0;
    for (int j = 0; i < mData.length; j++)
    {
      int k = mData[i];
      if ((paramString.length() <= k) && ((paramString.length() == k) || (mData[(paramString.length() + i + 1)] == 61)) && (paramString.compareToIgnoreCase(new String(mData, i + 1, paramString.length())) == 0))
      {
        paramString = mData;
        mData = new byte[paramString.length - k - 1];
        System.arraycopy(paramString, 0, mData, 0, i);
        System.arraycopy(paramString, i + k + 1, mData, i, paramString.length - i - k - 1);
        return j;
      }
      i += (0xFF & k + 1);
    }
    return -1;
  }
  
  public void set(String paramString1, String paramString2)
  {
    int i = 0;
    int j;
    if (paramString2 != null)
    {
      paramString2 = paramString2.getBytes();
      j = paramString2.length;
    }
    else
    {
      paramString2 = null;
      j = 0;
    }
    try
    {
      byte[] arrayOfByte = paramString1.getBytes("US-ASCII");
      while (i < arrayOfByte.length) {
        if (arrayOfByte[i] != 61) {
          i++;
        } else {
          throw new IllegalArgumentException("= is not a valid character in key");
        }
      }
      if (arrayOfByte.length + j < 255)
      {
        i = remove(paramString1);
        j = i;
        if (i == -1) {
          j = keyCount();
        }
        insert(arrayOfByte, paramString2, j);
        return;
      }
      throw new IllegalArgumentException("Key and Value length cannot exceed 255 bytes");
    }
    catch (UnsupportedEncodingException paramString1)
    {
      throw new IllegalArgumentException("key should be US-ASCII");
    }
  }
  
  public int size()
  {
    return mData.length;
  }
  
  public String toString()
  {
    Object localObject = null;
    for (int i = 0;; i++)
    {
      String str1 = getKey(i);
      if (str1 == null) {
        break;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("{");
      localStringBuilder.append(str1);
      str1 = localStringBuilder.toString();
      String str2 = getValueAsString(i);
      if (str2 != null)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append(str1);
        localStringBuilder.append("=");
        localStringBuilder.append(str2);
        localStringBuilder.append("}");
        str1 = localStringBuilder.toString();
      }
      else
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append(str1);
        localStringBuilder.append("}");
        str1 = localStringBuilder.toString();
      }
      if (localObject == null)
      {
        localObject = str1;
      }
      else
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append((String)localObject);
        localStringBuilder.append(", ");
        localStringBuilder.append(str1);
        localObject = localStringBuilder.toString();
      }
    }
    if (localObject == null) {
      localObject = "";
    }
    return localObject;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeByteArray(mData);
  }
}
