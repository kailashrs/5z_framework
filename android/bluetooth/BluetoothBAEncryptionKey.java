package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class BluetoothBAEncryptionKey
  implements Parcelable
{
  public static final Parcelable.Creator<BluetoothBAEncryptionKey> CREATOR = new Parcelable.Creator()
  {
    public BluetoothBAEncryptionKey createFromParcel(Parcel paramAnonymousParcel)
    {
      return new BluetoothBAEncryptionKey(paramAnonymousParcel, null);
    }
    
    public BluetoothBAEncryptionKey[] newArray(int paramAnonymousInt)
    {
      return new BluetoothBAEncryptionKey[paramAnonymousInt];
    }
  };
  public static int ENCRYPTION_KEY_LENGTH = 16;
  public static int SECURITY_KEY_FORWARD_ENABLED = 0;
  public static int SECURITY_KEY_TYPE_PRIVATE = 1;
  public static int SECURITY_KEY_TYPE_TEMP = 2;
  public static final String TAG = "BluetoothBAEncryptionKey";
  private byte[] mEncryptionKey = new byte[ENCRYPTION_KEY_LENGTH];
  private int mFlagType;
  
  static
  {
    SECURITY_KEY_FORWARD_ENABLED = 128;
  }
  
  private BluetoothBAEncryptionKey(Parcel paramParcel)
  {
    for (int i = 0; i < ENCRYPTION_KEY_LENGTH; i++) {
      mEncryptionKey[i] = paramParcel.readByte();
    }
    mFlagType = paramParcel.readInt();
  }
  
  public BluetoothBAEncryptionKey(byte[] paramArrayOfByte, int paramInt)
  {
    for (int i = 0; i < ENCRYPTION_KEY_LENGTH; i++) {
      mEncryptionKey[i] = ((byte)paramArrayOfByte[i]);
    }
    mFlagType = paramInt;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public byte[] getEncryptionKey()
  {
    return mEncryptionKey;
  }
  
  public int getFlagType()
  {
    return mFlagType;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    for (paramInt = 0; paramInt < ENCRYPTION_KEY_LENGTH; paramInt++) {
      paramParcel.writeByte(mEncryptionKey[paramInt]);
    }
    paramParcel.writeInt(mFlagType);
  }
}
