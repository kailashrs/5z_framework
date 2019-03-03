package android.bluetooth;

import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.UUID;

public class BluetoothGattIncludedService
  implements Parcelable
{
  public static final Parcelable.Creator<BluetoothGattIncludedService> CREATOR = new Parcelable.Creator()
  {
    public BluetoothGattIncludedService createFromParcel(Parcel paramAnonymousParcel)
    {
      return new BluetoothGattIncludedService(paramAnonymousParcel, null);
    }
    
    public BluetoothGattIncludedService[] newArray(int paramAnonymousInt)
    {
      return new BluetoothGattIncludedService[paramAnonymousInt];
    }
  };
  protected int mInstanceId;
  protected int mServiceType;
  protected UUID mUuid;
  
  private BluetoothGattIncludedService(Parcel paramParcel)
  {
    mUuid = ((ParcelUuid)paramParcel.readParcelable(null)).getUuid();
    mInstanceId = paramParcel.readInt();
    mServiceType = paramParcel.readInt();
  }
  
  public BluetoothGattIncludedService(UUID paramUUID, int paramInt1, int paramInt2)
  {
    mUuid = paramUUID;
    mInstanceId = paramInt1;
    mServiceType = paramInt2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getInstanceId()
  {
    return mInstanceId;
  }
  
  public int getType()
  {
    return mServiceType;
  }
  
  public UUID getUuid()
  {
    return mUuid;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(new ParcelUuid(mUuid), 0);
    paramParcel.writeInt(mInstanceId);
    paramParcel.writeInt(mServiceType);
  }
}
