package android.bluetooth;

import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class BluetoothGattCharacteristic
  implements Parcelable
{
  public static final Parcelable.Creator<BluetoothGattCharacteristic> CREATOR = new Parcelable.Creator()
  {
    public BluetoothGattCharacteristic createFromParcel(Parcel paramAnonymousParcel)
    {
      return new BluetoothGattCharacteristic(paramAnonymousParcel, null);
    }
    
    public BluetoothGattCharacteristic[] newArray(int paramAnonymousInt)
    {
      return new BluetoothGattCharacteristic[paramAnonymousInt];
    }
  };
  public static final int FORMAT_FLOAT = 52;
  public static final int FORMAT_SFLOAT = 50;
  public static final int FORMAT_SINT16 = 34;
  public static final int FORMAT_SINT32 = 36;
  public static final int FORMAT_SINT8 = 33;
  public static final int FORMAT_UINT16 = 18;
  public static final int FORMAT_UINT32 = 20;
  public static final int FORMAT_UINT8 = 17;
  public static final int PERMISSION_READ = 1;
  public static final int PERMISSION_READ_ENCRYPTED = 2;
  public static final int PERMISSION_READ_ENCRYPTED_MITM = 4;
  public static final int PERMISSION_WRITE = 16;
  public static final int PERMISSION_WRITE_ENCRYPTED = 32;
  public static final int PERMISSION_WRITE_ENCRYPTED_MITM = 64;
  public static final int PERMISSION_WRITE_SIGNED = 128;
  public static final int PERMISSION_WRITE_SIGNED_MITM = 256;
  public static final int PROPERTY_BROADCAST = 1;
  public static final int PROPERTY_EXTENDED_PROPS = 128;
  public static final int PROPERTY_INDICATE = 32;
  public static final int PROPERTY_NOTIFY = 16;
  public static final int PROPERTY_READ = 2;
  public static final int PROPERTY_SIGNED_WRITE = 64;
  public static final int PROPERTY_WRITE = 8;
  public static final int PROPERTY_WRITE_NO_RESPONSE = 4;
  public static final int WRITE_TYPE_DEFAULT = 2;
  public static final int WRITE_TYPE_NO_RESPONSE = 1;
  public static final int WRITE_TYPE_SIGNED = 4;
  protected List<BluetoothGattDescriptor> mDescriptors;
  protected int mInstance;
  protected int mKeySize = 16;
  protected int mPermissions;
  protected int mProperties;
  protected BluetoothGattService mService;
  protected UUID mUuid;
  protected byte[] mValue;
  protected int mWriteType;
  
  BluetoothGattCharacteristic(BluetoothGattService paramBluetoothGattService, UUID paramUUID, int paramInt1, int paramInt2, int paramInt3)
  {
    initCharacteristic(paramBluetoothGattService, paramUUID, paramInt1, paramInt2, paramInt3);
  }
  
  private BluetoothGattCharacteristic(Parcel paramParcel)
  {
    mUuid = ((ParcelUuid)paramParcel.readParcelable(null)).getUuid();
    mInstance = paramParcel.readInt();
    mProperties = paramParcel.readInt();
    mPermissions = paramParcel.readInt();
    mKeySize = paramParcel.readInt();
    mWriteType = paramParcel.readInt();
    mDescriptors = new ArrayList();
    paramParcel = paramParcel.createTypedArrayList(BluetoothGattDescriptor.CREATOR);
    if (paramParcel != null)
    {
      Iterator localIterator = paramParcel.iterator();
      while (localIterator.hasNext())
      {
        paramParcel = (BluetoothGattDescriptor)localIterator.next();
        paramParcel.setCharacteristic(this);
        mDescriptors.add(paramParcel);
      }
    }
  }
  
  public BluetoothGattCharacteristic(UUID paramUUID, int paramInt1, int paramInt2)
  {
    initCharacteristic(null, paramUUID, 0, paramInt1, paramInt2);
  }
  
  public BluetoothGattCharacteristic(UUID paramUUID, int paramInt1, int paramInt2, int paramInt3)
  {
    initCharacteristic(null, paramUUID, paramInt1, paramInt2, paramInt3);
  }
  
  private float bytesToFloat(byte paramByte1, byte paramByte2)
  {
    int i = unsignedToSigned(unsignedByteToInt(paramByte1) + ((unsignedByteToInt(paramByte2) & 0xF) << 8), 12);
    int j = unsignedToSigned(unsignedByteToInt(paramByte2) >> 4, 4);
    return (float)(i * Math.pow(10.0D, j));
  }
  
  private float bytesToFloat(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4)
  {
    return (float)(unsignedToSigned(unsignedByteToInt(paramByte1) + (unsignedByteToInt(paramByte2) << 8) + (unsignedByteToInt(paramByte3) << 16), 24) * Math.pow(10.0D, paramByte4));
  }
  
  private int getTypeLen(int paramInt)
  {
    return paramInt & 0xF;
  }
  
  private void initCharacteristic(BluetoothGattService paramBluetoothGattService, UUID paramUUID, int paramInt1, int paramInt2, int paramInt3)
  {
    mUuid = paramUUID;
    mInstance = paramInt1;
    mProperties = paramInt2;
    mPermissions = paramInt3;
    mService = paramBluetoothGattService;
    mValue = null;
    mDescriptors = new ArrayList();
    if ((mProperties & 0x4) != 0) {
      mWriteType = 1;
    } else {
      mWriteType = 2;
    }
  }
  
  private int intToSignedBits(int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    if (paramInt1 < 0) {
      i = (1 << paramInt2 - 1) + (paramInt1 & (1 << paramInt2 - 1) - 1);
    }
    return i;
  }
  
  private int unsignedByteToInt(byte paramByte)
  {
    return paramByte & 0xFF;
  }
  
  private int unsignedBytesToInt(byte paramByte1, byte paramByte2)
  {
    return unsignedByteToInt(paramByte1) + (unsignedByteToInt(paramByte2) << 8);
  }
  
  private int unsignedBytesToInt(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4)
  {
    return unsignedByteToInt(paramByte1) + (unsignedByteToInt(paramByte2) << 8) + (unsignedByteToInt(paramByte3) << 16) + (unsignedByteToInt(paramByte4) << 24);
  }
  
  private int unsignedToSigned(int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    if ((1 << paramInt2 - 1 & paramInt1) != 0) {
      i = -1 * ((1 << paramInt2 - 1) - (paramInt1 & (1 << paramInt2 - 1) - 1));
    }
    return i;
  }
  
  public boolean addDescriptor(BluetoothGattDescriptor paramBluetoothGattDescriptor)
  {
    mDescriptors.add(paramBluetoothGattDescriptor);
    paramBluetoothGattDescriptor.setCharacteristic(this);
    return true;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public BluetoothGattDescriptor getDescriptor(UUID paramUUID)
  {
    Iterator localIterator = mDescriptors.iterator();
    while (localIterator.hasNext())
    {
      BluetoothGattDescriptor localBluetoothGattDescriptor = (BluetoothGattDescriptor)localIterator.next();
      if (localBluetoothGattDescriptor.getUuid().equals(paramUUID)) {
        return localBluetoothGattDescriptor;
      }
    }
    return null;
  }
  
  BluetoothGattDescriptor getDescriptor(UUID paramUUID, int paramInt)
  {
    Iterator localIterator = mDescriptors.iterator();
    while (localIterator.hasNext())
    {
      BluetoothGattDescriptor localBluetoothGattDescriptor = (BluetoothGattDescriptor)localIterator.next();
      if ((localBluetoothGattDescriptor.getUuid().equals(paramUUID)) && (localBluetoothGattDescriptor.getInstanceId() == paramInt)) {
        return localBluetoothGattDescriptor;
      }
    }
    return null;
  }
  
  public List<BluetoothGattDescriptor> getDescriptors()
  {
    return mDescriptors;
  }
  
  public Float getFloatValue(int paramInt1, int paramInt2)
  {
    if (getTypeLen(paramInt1) + paramInt2 > mValue.length) {
      return null;
    }
    if (paramInt1 != 50)
    {
      if (paramInt1 != 52) {
        return null;
      }
      return Float.valueOf(bytesToFloat(mValue[paramInt2], mValue[(paramInt2 + 1)], mValue[(paramInt2 + 2)], mValue[(paramInt2 + 3)]));
    }
    return Float.valueOf(bytesToFloat(mValue[paramInt2], mValue[(paramInt2 + 1)]));
  }
  
  public int getInstanceId()
  {
    return mInstance;
  }
  
  public Integer getIntValue(int paramInt1, int paramInt2)
  {
    if (getTypeLen(paramInt1) + paramInt2 > mValue.length) {
      return null;
    }
    switch (paramInt1)
    {
    default: 
      return null;
    case 36: 
      return Integer.valueOf(unsignedToSigned(unsignedBytesToInt(mValue[paramInt2], mValue[(paramInt2 + 1)], mValue[(paramInt2 + 2)], mValue[(paramInt2 + 3)]), 32));
    case 34: 
      return Integer.valueOf(unsignedToSigned(unsignedBytesToInt(mValue[paramInt2], mValue[(paramInt2 + 1)]), 16));
    case 33: 
      return Integer.valueOf(unsignedToSigned(unsignedByteToInt(mValue[paramInt2]), 8));
    case 20: 
      return Integer.valueOf(unsignedBytesToInt(mValue[paramInt2], mValue[(paramInt2 + 1)], mValue[(paramInt2 + 2)], mValue[(paramInt2 + 3)]));
    case 18: 
      return Integer.valueOf(unsignedBytesToInt(mValue[paramInt2], mValue[(paramInt2 + 1)]));
    }
    return Integer.valueOf(unsignedByteToInt(mValue[paramInt2]));
  }
  
  public int getKeySize()
  {
    return mKeySize;
  }
  
  public int getPermissions()
  {
    return mPermissions;
  }
  
  public int getProperties()
  {
    return mProperties;
  }
  
  public BluetoothGattService getService()
  {
    return mService;
  }
  
  public String getStringValue(int paramInt)
  {
    if ((mValue != null) && (paramInt <= mValue.length))
    {
      byte[] arrayOfByte = new byte[mValue.length - paramInt];
      for (int i = 0; i != mValue.length - paramInt; i++) {
        arrayOfByte[i] = ((byte)mValue[(paramInt + i)]);
      }
      return new String(arrayOfByte);
    }
    return null;
  }
  
  public UUID getUuid()
  {
    return mUuid;
  }
  
  public byte[] getValue()
  {
    return mValue;
  }
  
  public int getWriteType()
  {
    return mWriteType;
  }
  
  public void setInstanceId(int paramInt)
  {
    mInstance = paramInt;
  }
  
  public void setKeySize(int paramInt)
  {
    mKeySize = paramInt;
  }
  
  void setService(BluetoothGattService paramBluetoothGattService)
  {
    mService = paramBluetoothGattService;
  }
  
  public boolean setValue(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = getTypeLen(paramInt2) + paramInt3;
    if (mValue == null) {
      mValue = new byte[i];
    }
    if (i > mValue.length) {
      return false;
    }
    i = paramInt1;
    int j = paramInt1;
    int k = paramInt1;
    switch (paramInt2)
    {
    default: 
      return false;
    case 36: 
      i = intToSignedBits(paramInt1, 32);
      break;
    case 34: 
      j = intToSignedBits(paramInt1, 16);
      break;
    case 33: 
      k = intToSignedBits(paramInt1, 8);
      break;
    case 20: 
      byte[] arrayOfByte = mValue;
      paramInt1 = paramInt3 + 1;
      arrayOfByte[paramInt3] = ((byte)(byte)(i & 0xFF));
      arrayOfByte = mValue;
      paramInt2 = paramInt1 + 1;
      arrayOfByte[paramInt1] = ((byte)(byte)(i >> 8 & 0xFF));
      mValue[paramInt2] = ((byte)(byte)(i >> 16 & 0xFF));
      mValue[(paramInt2 + 1)] = ((byte)(byte)(i >> 24 & 0xFF));
      break;
    case 18: 
      mValue[paramInt3] = ((byte)(byte)(j & 0xFF));
      mValue[(paramInt3 + 1)] = ((byte)(byte)(j >> 8 & 0xFF));
      break;
    }
    mValue[paramInt3] = ((byte)(byte)(k & 0xFF));
    return true;
  }
  
  public boolean setValue(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = getTypeLen(paramInt3) + paramInt4;
    if (mValue == null) {
      mValue = new byte[i];
    }
    if (i > mValue.length) {
      return false;
    }
    byte[] arrayOfByte;
    if (paramInt3 != 50)
    {
      if (paramInt3 != 52) {
        return false;
      }
      paramInt1 = intToSignedBits(paramInt1, 24);
      paramInt2 = intToSignedBits(paramInt2, 8);
      arrayOfByte = mValue;
      i = paramInt4 + 1;
      arrayOfByte[paramInt4] = ((byte)(byte)(paramInt1 & 0xFF));
      arrayOfByte = mValue;
      paramInt3 = i + 1;
      arrayOfByte[i] = ((byte)(byte)(paramInt1 >> 8 & 0xFF));
      arrayOfByte = mValue;
      paramInt4 = paramInt3 + 1;
      arrayOfByte[paramInt3] = ((byte)(byte)(paramInt1 >> 16 & 0xFF));
      arrayOfByte = mValue;
      arrayOfByte[paramInt4] = ((byte)(byte)(arrayOfByte[paramInt4] + (byte)(paramInt2 & 0xFF)));
    }
    else
    {
      paramInt1 = intToSignedBits(paramInt1, 12);
      paramInt2 = intToSignedBits(paramInt2, 4);
      arrayOfByte = mValue;
      paramInt3 = paramInt4 + 1;
      arrayOfByte[paramInt4] = ((byte)(byte)(paramInt1 & 0xFF));
      mValue[paramInt3] = ((byte)(byte)(paramInt1 >> 8 & 0xF));
      arrayOfByte = mValue;
      arrayOfByte[paramInt3] = ((byte)(byte)(arrayOfByte[paramInt3] + (byte)((paramInt2 & 0xF) << 4)));
    }
    return true;
  }
  
  public boolean setValue(String paramString)
  {
    mValue = paramString.getBytes();
    return true;
  }
  
  public boolean setValue(byte[] paramArrayOfByte)
  {
    mValue = paramArrayOfByte;
    return true;
  }
  
  public void setWriteType(int paramInt)
  {
    mWriteType = paramInt;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(new ParcelUuid(mUuid), 0);
    paramParcel.writeInt(mInstance);
    paramParcel.writeInt(mProperties);
    paramParcel.writeInt(mPermissions);
    paramParcel.writeInt(mKeySize);
    paramParcel.writeInt(mWriteType);
    paramParcel.writeTypedList(mDescriptors);
  }
}
