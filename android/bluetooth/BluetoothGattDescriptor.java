package android.bluetooth;

import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.UUID;

public class BluetoothGattDescriptor
  implements Parcelable
{
  public static final Parcelable.Creator<BluetoothGattDescriptor> CREATOR = new Parcelable.Creator()
  {
    public BluetoothGattDescriptor createFromParcel(Parcel paramAnonymousParcel)
    {
      return new BluetoothGattDescriptor(paramAnonymousParcel, null);
    }
    
    public BluetoothGattDescriptor[] newArray(int paramAnonymousInt)
    {
      return new BluetoothGattDescriptor[paramAnonymousInt];
    }
  };
  public static final byte[] DISABLE_NOTIFICATION_VALUE;
  public static final byte[] ENABLE_INDICATION_VALUE;
  public static final byte[] ENABLE_NOTIFICATION_VALUE = { 1, 0 };
  public static final int PERMISSION_READ = 1;
  public static final int PERMISSION_READ_ENCRYPTED = 2;
  public static final int PERMISSION_READ_ENCRYPTED_MITM = 4;
  public static final int PERMISSION_WRITE = 16;
  public static final int PERMISSION_WRITE_ENCRYPTED = 32;
  public static final int PERMISSION_WRITE_ENCRYPTED_MITM = 64;
  public static final int PERMISSION_WRITE_SIGNED = 128;
  public static final int PERMISSION_WRITE_SIGNED_MITM = 256;
  protected BluetoothGattCharacteristic mCharacteristic;
  protected int mInstance;
  protected int mPermissions;
  protected UUID mUuid;
  protected byte[] mValue;
  
  static
  {
    ENABLE_INDICATION_VALUE = new byte[] { 2, 0 };
    DISABLE_NOTIFICATION_VALUE = new byte[] { 0, 0 };
  }
  
  BluetoothGattDescriptor(BluetoothGattCharacteristic paramBluetoothGattCharacteristic, UUID paramUUID, int paramInt1, int paramInt2)
  {
    initDescriptor(paramBluetoothGattCharacteristic, paramUUID, paramInt1, paramInt2);
  }
  
  private BluetoothGattDescriptor(Parcel paramParcel)
  {
    mUuid = ((ParcelUuid)paramParcel.readParcelable(null)).getUuid();
    mInstance = paramParcel.readInt();
    mPermissions = paramParcel.readInt();
  }
  
  public BluetoothGattDescriptor(UUID paramUUID, int paramInt)
  {
    initDescriptor(null, paramUUID, 0, paramInt);
  }
  
  public BluetoothGattDescriptor(UUID paramUUID, int paramInt1, int paramInt2)
  {
    initDescriptor(null, paramUUID, paramInt1, paramInt2);
  }
  
  private void initDescriptor(BluetoothGattCharacteristic paramBluetoothGattCharacteristic, UUID paramUUID, int paramInt1, int paramInt2)
  {
    mCharacteristic = paramBluetoothGattCharacteristic;
    mUuid = paramUUID;
    mInstance = paramInt1;
    mPermissions = paramInt2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public BluetoothGattCharacteristic getCharacteristic()
  {
    return mCharacteristic;
  }
  
  public int getInstanceId()
  {
    return mInstance;
  }
  
  public int getPermissions()
  {
    return mPermissions;
  }
  
  public UUID getUuid()
  {
    return mUuid;
  }
  
  public byte[] getValue()
  {
    return mValue;
  }
  
  void setCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic)
  {
    mCharacteristic = paramBluetoothGattCharacteristic;
  }
  
  public void setInstanceId(int paramInt)
  {
    mInstance = paramInt;
  }
  
  public boolean setValue(byte[] paramArrayOfByte)
  {
    mValue = paramArrayOfByte;
    return true;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(new ParcelUuid(mUuid), 0);
    paramParcel.writeInt(mInstance);
    paramParcel.writeInt(mPermissions);
  }
}
