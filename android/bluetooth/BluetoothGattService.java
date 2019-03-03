package android.bluetooth;

import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class BluetoothGattService
  implements Parcelable
{
  public static final Parcelable.Creator<BluetoothGattService> CREATOR = new Parcelable.Creator()
  {
    public BluetoothGattService createFromParcel(Parcel paramAnonymousParcel)
    {
      return new BluetoothGattService(paramAnonymousParcel, null);
    }
    
    public BluetoothGattService[] newArray(int paramAnonymousInt)
    {
      return new BluetoothGattService[paramAnonymousInt];
    }
  };
  public static final int SERVICE_TYPE_PRIMARY = 0;
  public static final int SERVICE_TYPE_SECONDARY = 1;
  private boolean mAdvertisePreferred;
  protected List<BluetoothGattCharacteristic> mCharacteristics;
  protected BluetoothDevice mDevice;
  protected int mHandles = 0;
  protected List<BluetoothGattService> mIncludedServices;
  protected int mInstanceId;
  protected int mServiceType;
  protected UUID mUuid;
  
  BluetoothGattService(BluetoothDevice paramBluetoothDevice, UUID paramUUID, int paramInt1, int paramInt2)
  {
    mDevice = paramBluetoothDevice;
    mUuid = paramUUID;
    mInstanceId = paramInt1;
    mServiceType = paramInt2;
    mCharacteristics = new ArrayList();
    mIncludedServices = new ArrayList();
  }
  
  private BluetoothGattService(Parcel paramParcel)
  {
    mUuid = ((ParcelUuid)paramParcel.readParcelable(null)).getUuid();
    mInstanceId = paramParcel.readInt();
    mServiceType = paramParcel.readInt();
    mCharacteristics = new ArrayList();
    Object localObject = paramParcel.createTypedArrayList(BluetoothGattCharacteristic.CREATOR);
    if (localObject != null)
    {
      Iterator localIterator = ((ArrayList)localObject).iterator();
      while (localIterator.hasNext())
      {
        BluetoothGattCharacteristic localBluetoothGattCharacteristic = (BluetoothGattCharacteristic)localIterator.next();
        localBluetoothGattCharacteristic.setService(this);
        mCharacteristics.add(localBluetoothGattCharacteristic);
      }
    }
    mIncludedServices = new ArrayList();
    paramParcel = paramParcel.createTypedArrayList(BluetoothGattIncludedService.CREATOR);
    if (localObject != null)
    {
      localObject = paramParcel.iterator();
      while (((Iterator)localObject).hasNext())
      {
        paramParcel = (BluetoothGattIncludedService)((Iterator)localObject).next();
        mIncludedServices.add(new BluetoothGattService(null, paramParcel.getUuid(), paramParcel.getInstanceId(), paramParcel.getType()));
      }
    }
  }
  
  public BluetoothGattService(UUID paramUUID, int paramInt)
  {
    mDevice = null;
    mUuid = paramUUID;
    mInstanceId = 0;
    mServiceType = paramInt;
    mCharacteristics = new ArrayList();
    mIncludedServices = new ArrayList();
  }
  
  public BluetoothGattService(UUID paramUUID, int paramInt1, int paramInt2)
  {
    mDevice = null;
    mUuid = paramUUID;
    mInstanceId = paramInt1;
    mServiceType = paramInt2;
    mCharacteristics = new ArrayList();
    mIncludedServices = new ArrayList();
  }
  
  public boolean addCharacteristic(BluetoothGattCharacteristic paramBluetoothGattCharacteristic)
  {
    mCharacteristics.add(paramBluetoothGattCharacteristic);
    paramBluetoothGattCharacteristic.setService(this);
    return true;
  }
  
  public void addIncludedService(BluetoothGattService paramBluetoothGattService)
  {
    mIncludedServices.add(paramBluetoothGattService);
  }
  
  public boolean addService(BluetoothGattService paramBluetoothGattService)
  {
    mIncludedServices.add(paramBluetoothGattService);
    return true;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public BluetoothGattCharacteristic getCharacteristic(UUID paramUUID)
  {
    Iterator localIterator = mCharacteristics.iterator();
    while (localIterator.hasNext())
    {
      BluetoothGattCharacteristic localBluetoothGattCharacteristic = (BluetoothGattCharacteristic)localIterator.next();
      if (paramUUID.equals(localBluetoothGattCharacteristic.getUuid())) {
        return localBluetoothGattCharacteristic;
      }
    }
    return null;
  }
  
  BluetoothGattCharacteristic getCharacteristic(UUID paramUUID, int paramInt)
  {
    Iterator localIterator = mCharacteristics.iterator();
    while (localIterator.hasNext())
    {
      BluetoothGattCharacteristic localBluetoothGattCharacteristic = (BluetoothGattCharacteristic)localIterator.next();
      if ((paramUUID.equals(localBluetoothGattCharacteristic.getUuid())) && (localBluetoothGattCharacteristic.getInstanceId() == paramInt)) {
        return localBluetoothGattCharacteristic;
      }
    }
    return null;
  }
  
  public List<BluetoothGattCharacteristic> getCharacteristics()
  {
    return mCharacteristics;
  }
  
  BluetoothDevice getDevice()
  {
    return mDevice;
  }
  
  int getHandles()
  {
    return mHandles;
  }
  
  public List<BluetoothGattService> getIncludedServices()
  {
    return mIncludedServices;
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
  
  public boolean isAdvertisePreferred()
  {
    return mAdvertisePreferred;
  }
  
  public void setAdvertisePreferred(boolean paramBoolean)
  {
    mAdvertisePreferred = paramBoolean;
  }
  
  void setDevice(BluetoothDevice paramBluetoothDevice)
  {
    mDevice = paramBluetoothDevice;
  }
  
  public void setHandles(int paramInt)
  {
    mHandles = paramInt;
  }
  
  public void setInstanceId(int paramInt)
  {
    mInstanceId = paramInt;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(new ParcelUuid(mUuid), 0);
    paramParcel.writeInt(mInstanceId);
    paramParcel.writeInt(mServiceType);
    paramParcel.writeTypedList(mCharacteristics);
    ArrayList localArrayList = new ArrayList(mIncludedServices.size());
    Iterator localIterator = mIncludedServices.iterator();
    while (localIterator.hasNext())
    {
      BluetoothGattService localBluetoothGattService = (BluetoothGattService)localIterator.next();
      localArrayList.add(new BluetoothGattIncludedService(localBluetoothGattService.getUuid(), localBluetoothGattService.getInstanceId(), localBluetoothGattService.getType()));
    }
    paramParcel.writeTypedList(localArrayList);
  }
}
