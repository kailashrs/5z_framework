package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class OobData
  implements Parcelable
{
  public static final Parcelable.Creator<OobData> CREATOR = new Parcelable.Creator()
  {
    public OobData createFromParcel(Parcel paramAnonymousParcel)
    {
      return new OobData(paramAnonymousParcel, null);
    }
    
    public OobData[] newArray(int paramAnonymousInt)
    {
      return new OobData[paramAnonymousInt];
    }
  };
  private byte[] mLeBluetoothDeviceAddress;
  private byte[] mLeSecureConnectionsConfirmation;
  private byte[] mLeSecureConnectionsRandom;
  private byte[] mSecurityManagerTk;
  
  public OobData() {}
  
  private OobData(Parcel paramParcel)
  {
    mLeBluetoothDeviceAddress = paramParcel.createByteArray();
    mSecurityManagerTk = paramParcel.createByteArray();
    mLeSecureConnectionsConfirmation = paramParcel.createByteArray();
    mLeSecureConnectionsRandom = paramParcel.createByteArray();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public byte[] getLeBluetoothDeviceAddress()
  {
    return mLeBluetoothDeviceAddress;
  }
  
  public byte[] getLeSecureConnectionsConfirmation()
  {
    return mLeSecureConnectionsConfirmation;
  }
  
  public byte[] getLeSecureConnectionsRandom()
  {
    return mLeSecureConnectionsRandom;
  }
  
  public byte[] getSecurityManagerTk()
  {
    return mSecurityManagerTk;
  }
  
  public void setLeBluetoothDeviceAddress(byte[] paramArrayOfByte)
  {
    mLeBluetoothDeviceAddress = paramArrayOfByte;
  }
  
  public void setLeSecureConnectionsConfirmation(byte[] paramArrayOfByte)
  {
    mLeSecureConnectionsConfirmation = paramArrayOfByte;
  }
  
  public void setLeSecureConnectionsRandom(byte[] paramArrayOfByte)
  {
    mLeSecureConnectionsRandom = paramArrayOfByte;
  }
  
  public void setSecurityManagerTk(byte[] paramArrayOfByte)
  {
    mSecurityManagerTk = paramArrayOfByte;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeByteArray(mLeBluetoothDeviceAddress);
    paramParcel.writeByteArray(mSecurityManagerTk);
    paramParcel.writeByteArray(mLeSecureConnectionsConfirmation);
    paramParcel.writeByteArray(mLeSecureConnectionsRandom);
  }
}
