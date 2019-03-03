package android.bluetooth.le;

import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.ArrayMap;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class AdvertiseData
  implements Parcelable
{
  public static final Parcelable.Creator<AdvertiseData> CREATOR = new Parcelable.Creator()
  {
    public AdvertiseData createFromParcel(Parcel paramAnonymousParcel)
    {
      AdvertiseData.Builder localBuilder = new AdvertiseData.Builder();
      Iterator localIterator = paramAnonymousParcel.createTypedArrayList(ParcelUuid.CREATOR).iterator();
      while (localIterator.hasNext()) {
        localBuilder.addServiceUuid((ParcelUuid)localIterator.next());
      }
      int i = paramAnonymousParcel.readInt();
      boolean bool1 = false;
      for (int j = 0; j < i; j++) {
        localBuilder.addManufacturerData(paramAnonymousParcel.readInt(), paramAnonymousParcel.createByteArray());
      }
      i = paramAnonymousParcel.readInt();
      for (j = 0; j < i; j++) {
        localBuilder.addServiceData((ParcelUuid)paramAnonymousParcel.readTypedObject(ParcelUuid.CREATOR), paramAnonymousParcel.createByteArray());
      }
      if (paramAnonymousParcel.readByte() == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      localBuilder.setIncludeTxPowerLevel(bool2);
      boolean bool2 = bool1;
      if (paramAnonymousParcel.readByte() == 1) {
        bool2 = true;
      }
      localBuilder.setIncludeDeviceName(bool2);
      if (paramAnonymousParcel.readInt() > 0) {
        localBuilder.addTransportDiscoveryData(paramAnonymousParcel.createByteArray());
      }
      return localBuilder.build();
    }
    
    public AdvertiseData[] newArray(int paramAnonymousInt)
    {
      return new AdvertiseData[paramAnonymousInt];
    }
  };
  private final boolean mIncludeDeviceName;
  private final boolean mIncludeTxPowerLevel;
  private final SparseArray<byte[]> mManufacturerSpecificData;
  private final Map<ParcelUuid, byte[]> mServiceData;
  private final List<ParcelUuid> mServiceUuids;
  private final byte[] mTransportDiscoveryData;
  
  private AdvertiseData(List<ParcelUuid> paramList, SparseArray<byte[]> paramSparseArray, Map<ParcelUuid, byte[]> paramMap, boolean paramBoolean1, boolean paramBoolean2, byte[] paramArrayOfByte)
  {
    mServiceUuids = paramList;
    mManufacturerSpecificData = paramSparseArray;
    mServiceData = paramMap;
    mIncludeTxPowerLevel = paramBoolean1;
    mIncludeDeviceName = paramBoolean2;
    mTransportDiscoveryData = paramArrayOfByte;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (AdvertiseData)paramObject;
      if ((!Objects.equals(mServiceUuids, mServiceUuids)) || (!BluetoothLeUtils.equals(mManufacturerSpecificData, mManufacturerSpecificData)) || (!BluetoothLeUtils.equals(mServiceData, mServiceData)) || (mIncludeDeviceName != mIncludeDeviceName) || (mIncludeTxPowerLevel != mIncludeTxPowerLevel) || (!BluetoothLeUtils.equals(mTransportDiscoveryData, mTransportDiscoveryData))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public boolean getIncludeDeviceName()
  {
    return mIncludeDeviceName;
  }
  
  public boolean getIncludeTxPowerLevel()
  {
    return mIncludeTxPowerLevel;
  }
  
  public SparseArray<byte[]> getManufacturerSpecificData()
  {
    return mManufacturerSpecificData;
  }
  
  public Map<ParcelUuid, byte[]> getServiceData()
  {
    return mServiceData;
  }
  
  public List<ParcelUuid> getServiceUuids()
  {
    return mServiceUuids;
  }
  
  public byte[] getTransportDiscoveryData()
  {
    return mTransportDiscoveryData;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { mServiceUuids, mManufacturerSpecificData, mServiceData, Boolean.valueOf(mIncludeDeviceName), Boolean.valueOf(mIncludeTxPowerLevel), mTransportDiscoveryData });
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("AdvertiseData [mServiceUuids=");
    localStringBuilder.append(mServiceUuids);
    localStringBuilder.append(", mManufacturerSpecificData=");
    localStringBuilder.append(BluetoothLeUtils.toString(mManufacturerSpecificData));
    localStringBuilder.append(", mServiceData=");
    localStringBuilder.append(BluetoothLeUtils.toString(mServiceData));
    localStringBuilder.append(", mIncludeTxPowerLevel=");
    localStringBuilder.append(mIncludeTxPowerLevel);
    localStringBuilder.append(", mIncludeDeviceName=");
    localStringBuilder.append(mIncludeDeviceName);
    localStringBuilder.append(", mTransportDiscoveryData=");
    localStringBuilder.append(BluetoothLeUtils.toString(mTransportDiscoveryData));
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeTypedArray((ParcelUuid[])mServiceUuids.toArray(new ParcelUuid[mServiceUuids.size()]), paramInt);
    paramParcel.writeInt(mManufacturerSpecificData.size());
    int i = 0;
    for (int j = 0; j < mManufacturerSpecificData.size(); j++)
    {
      paramParcel.writeInt(mManufacturerSpecificData.keyAt(j));
      paramParcel.writeByteArray((byte[])mManufacturerSpecificData.valueAt(j));
    }
    paramParcel.writeInt(mServiceData.size());
    Iterator localIterator = mServiceData.keySet().iterator();
    while (localIterator.hasNext())
    {
      ParcelUuid localParcelUuid = (ParcelUuid)localIterator.next();
      paramParcel.writeTypedObject(localParcelUuid, paramInt);
      paramParcel.writeByteArray((byte[])mServiceData.get(localParcelUuid));
    }
    paramParcel.writeByte((byte)getIncludeTxPowerLevel());
    paramParcel.writeByte((byte)getIncludeDeviceName());
    paramInt = i;
    if (mTransportDiscoveryData != null) {
      paramInt = mTransportDiscoveryData.length;
    }
    paramParcel.writeInt(paramInt);
    if (mTransportDiscoveryData != null) {
      paramParcel.writeByteArray(mTransportDiscoveryData);
    }
  }
  
  public static final class Builder
  {
    private boolean mIncludeDeviceName;
    private boolean mIncludeTxPowerLevel;
    private SparseArray<byte[]> mManufacturerSpecificData = new SparseArray();
    private Map<ParcelUuid, byte[]> mServiceData = new ArrayMap();
    private List<ParcelUuid> mServiceUuids = new ArrayList();
    private byte[] mTransportDiscoveryData;
    
    public Builder() {}
    
    public Builder addManufacturerData(int paramInt, byte[] paramArrayOfByte)
    {
      if (paramInt >= 0)
      {
        if (paramArrayOfByte != null)
        {
          mManufacturerSpecificData.put(paramInt, paramArrayOfByte);
          return this;
        }
        throw new IllegalArgumentException("manufacturerSpecificData is null");
      }
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("invalid manufacturerId - ");
      paramArrayOfByte.append(paramInt);
      throw new IllegalArgumentException(paramArrayOfByte.toString());
    }
    
    public Builder addServiceData(ParcelUuid paramParcelUuid, byte[] paramArrayOfByte)
    {
      if ((paramParcelUuid != null) && (paramArrayOfByte != null))
      {
        mServiceData.put(paramParcelUuid, paramArrayOfByte);
        return this;
      }
      throw new IllegalArgumentException("serviceDataUuid or serviceDataUuid is null");
    }
    
    public Builder addServiceUuid(ParcelUuid paramParcelUuid)
    {
      if (paramParcelUuid != null)
      {
        mServiceUuids.add(paramParcelUuid);
        return this;
      }
      throw new IllegalArgumentException("serivceUuids are null");
    }
    
    public Builder addTransportDiscoveryData(byte[] paramArrayOfByte)
    {
      if ((paramArrayOfByte != null) && (paramArrayOfByte.length != 0))
      {
        mTransportDiscoveryData = paramArrayOfByte;
        return this;
      }
      throw new IllegalArgumentException("transportDiscoveryData is null");
    }
    
    public AdvertiseData build()
    {
      return new AdvertiseData(mServiceUuids, mManufacturerSpecificData, mServiceData, mIncludeTxPowerLevel, mIncludeDeviceName, mTransportDiscoveryData, null);
    }
    
    public Builder setIncludeDeviceName(boolean paramBoolean)
    {
      mIncludeDeviceName = paramBoolean;
      return this;
    }
    
    public Builder setIncludeTxPowerLevel(boolean paramBoolean)
    {
      mIncludeTxPowerLevel = paramBoolean;
      return this;
    }
  }
}
