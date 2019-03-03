package android.nfc;

import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcBarcode;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public final class Tag
  implements Parcelable
{
  public static final Parcelable.Creator<Tag> CREATOR = new Parcelable.Creator()
  {
    public Tag createFromParcel(Parcel paramAnonymousParcel)
    {
      byte[] arrayOfByte = Tag.readBytesWithNull(paramAnonymousParcel);
      int[] arrayOfInt = new int[paramAnonymousParcel.readInt()];
      paramAnonymousParcel.readIntArray(arrayOfInt);
      Bundle[] arrayOfBundle = (Bundle[])paramAnonymousParcel.createTypedArray(Bundle.CREATOR);
      int i = paramAnonymousParcel.readInt();
      if (paramAnonymousParcel.readInt() == 0) {}
      for (paramAnonymousParcel = INfcTag.Stub.asInterface(paramAnonymousParcel.readStrongBinder());; paramAnonymousParcel = null) {
        break;
      }
      return new Tag(arrayOfByte, arrayOfInt, arrayOfBundle, i, paramAnonymousParcel);
    }
    
    public Tag[] newArray(int paramAnonymousInt)
    {
      return new Tag[paramAnonymousInt];
    }
  };
  int mConnectedTechnology;
  final byte[] mId;
  final int mServiceHandle;
  final INfcTag mTagService;
  final Bundle[] mTechExtras;
  final int[] mTechList;
  final String[] mTechStringList;
  
  public Tag(byte[] paramArrayOfByte, int[] paramArrayOfInt, Bundle[] paramArrayOfBundle, int paramInt, INfcTag paramINfcTag)
  {
    if (paramArrayOfInt != null)
    {
      mId = paramArrayOfByte;
      mTechList = Arrays.copyOf(paramArrayOfInt, paramArrayOfInt.length);
      mTechStringList = generateTechStringList(paramArrayOfInt);
      mTechExtras = ((Bundle[])Arrays.copyOf(paramArrayOfBundle, paramArrayOfInt.length));
      mServiceHandle = paramInt;
      mTagService = paramINfcTag;
      mConnectedTechnology = -1;
      return;
    }
    throw new IllegalArgumentException("rawTargets cannot be null");
  }
  
  public static Tag createMockTag(byte[] paramArrayOfByte, int[] paramArrayOfInt, Bundle[] paramArrayOfBundle)
  {
    return new Tag(paramArrayOfByte, paramArrayOfInt, paramArrayOfBundle, 0, null);
  }
  
  private String[] generateTechStringList(int[] paramArrayOfInt)
  {
    int i = paramArrayOfInt.length;
    Object localObject = new String[i];
    for (int j = 0; j < i; j++) {
      switch (paramArrayOfInt[j])
      {
      default: 
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Unknown tech type ");
        ((StringBuilder)localObject).append(paramArrayOfInt[j]);
        throw new IllegalArgumentException(((StringBuilder)localObject).toString());
      case 10: 
        localObject[j] = NfcBarcode.class.getName();
        break;
      case 9: 
        localObject[j] = MifareUltralight.class.getName();
        break;
      case 8: 
        localObject[j] = MifareClassic.class.getName();
        break;
      case 7: 
        localObject[j] = NdefFormatable.class.getName();
        break;
      case 6: 
        localObject[j] = Ndef.class.getName();
        break;
      case 5: 
        localObject[j] = NfcV.class.getName();
        break;
      case 4: 
        localObject[j] = NfcF.class.getName();
        break;
      case 3: 
        localObject[j] = IsoDep.class.getName();
        break;
      case 2: 
        localObject[j] = NfcB.class.getName();
        break;
      case 1: 
        localObject[j] = NfcA.class.getName();
      }
    }
    return localObject;
  }
  
  static int[] getTechCodesFromStrings(String[] paramArrayOfString)
    throws IllegalArgumentException
  {
    if (paramArrayOfString != null)
    {
      Object localObject = new int[paramArrayOfString.length];
      HashMap localHashMap = getTechStringToCodeMap();
      int i = 0;
      while (i < paramArrayOfString.length)
      {
        Integer localInteger = (Integer)localHashMap.get(paramArrayOfString[i]);
        if (localInteger != null)
        {
          localObject[i] = localInteger.intValue();
          i++;
        }
        else
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Unknown tech type ");
          ((StringBuilder)localObject).append(paramArrayOfString[i]);
          throw new IllegalArgumentException(((StringBuilder)localObject).toString());
        }
      }
      return localObject;
    }
    throw new IllegalArgumentException("List cannot be null");
  }
  
  private static HashMap<String, Integer> getTechStringToCodeMap()
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put(IsoDep.class.getName(), Integer.valueOf(3));
    localHashMap.put(MifareClassic.class.getName(), Integer.valueOf(8));
    localHashMap.put(MifareUltralight.class.getName(), Integer.valueOf(9));
    localHashMap.put(Ndef.class.getName(), Integer.valueOf(6));
    localHashMap.put(NdefFormatable.class.getName(), Integer.valueOf(7));
    localHashMap.put(NfcA.class.getName(), Integer.valueOf(1));
    localHashMap.put(NfcB.class.getName(), Integer.valueOf(2));
    localHashMap.put(NfcF.class.getName(), Integer.valueOf(4));
    localHashMap.put(NfcV.class.getName(), Integer.valueOf(5));
    localHashMap.put(NfcBarcode.class.getName(), Integer.valueOf(10));
    return localHashMap;
  }
  
  static byte[] readBytesWithNull(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    byte[] arrayOfByte = null;
    if (i >= 0)
    {
      arrayOfByte = new byte[i];
      paramParcel.readByteArray(arrayOfByte);
    }
    return arrayOfByte;
  }
  
  static void writeBytesWithNull(Parcel paramParcel, byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null)
    {
      paramParcel.writeInt(-1);
      return;
    }
    paramParcel.writeInt(paramArrayOfByte.length);
    paramParcel.writeByteArray(paramArrayOfByte);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getConnectedTechnology()
  {
    return mConnectedTechnology;
  }
  
  public byte[] getId()
  {
    return mId;
  }
  
  public int getServiceHandle()
  {
    return mServiceHandle;
  }
  
  public INfcTag getTagService()
  {
    return mTagService;
  }
  
  public int[] getTechCodeList()
  {
    return mTechList;
  }
  
  public Bundle getTechExtras(int paramInt)
  {
    int i = -1;
    int k;
    for (int j = 0;; j++)
    {
      k = i;
      if (j >= mTechList.length) {
        break;
      }
      if (mTechList[j] == paramInt)
      {
        k = j;
        break;
      }
    }
    if (k < 0) {
      return null;
    }
    return mTechExtras[k];
  }
  
  public String[] getTechList()
  {
    return mTechStringList;
  }
  
  public boolean hasTech(int paramInt)
  {
    int[] arrayOfInt = mTechList;
    int i = arrayOfInt.length;
    for (int j = 0; j < i; j++) {
      if (arrayOfInt[j] == paramInt) {
        return true;
      }
    }
    return false;
  }
  
  public Tag rediscover()
    throws IOException
  {
    if (getConnectedTechnology() == -1)
    {
      if (mTagService != null) {
        try
        {
          Object localObject = mTagService.rediscover(getServiceHandle());
          if (localObject != null) {
            return localObject;
          }
          localObject = new java/io/IOException;
          ((IOException)localObject).<init>("Failed to rediscover tag");
          throw ((Throwable)localObject);
        }
        catch (RemoteException localRemoteException)
        {
          throw new IOException("NFC service dead");
        }
      }
      throw new IOException("Mock tags don't support this operation.");
    }
    throw new IllegalStateException("Close connection to the technology first!");
  }
  
  public void setConnectedTechnology(int paramInt)
  {
    try
    {
      if (mConnectedTechnology == -1)
      {
        mConnectedTechnology = paramInt;
        return;
      }
      IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
      localIllegalStateException.<init>("Close other technology first!");
      throw localIllegalStateException;
    }
    finally {}
  }
  
  public void setTechnologyDisconnected()
  {
    mConnectedTechnology = -1;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("TAG: Tech [");
    String[] arrayOfString = getTechList();
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++)
    {
      localStringBuilder.append(arrayOfString[j]);
      if (j < i - 1) {
        localStringBuilder.append(", ");
      }
    }
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (mTagService == null) {
      paramInt = 1;
    } else {
      paramInt = 0;
    }
    writeBytesWithNull(paramParcel, mId);
    paramParcel.writeInt(mTechList.length);
    paramParcel.writeIntArray(mTechList);
    paramParcel.writeTypedArray(mTechExtras, 0);
    paramParcel.writeInt(mServiceHandle);
    paramParcel.writeInt(paramInt);
    if (paramInt == 0) {
      paramParcel.writeStrongBinder(mTagService.asBinder());
    }
  }
}
