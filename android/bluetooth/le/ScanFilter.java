package android.bluetooth.le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.BitUtils;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class ScanFilter
  implements Parcelable
{
  public static final Parcelable.Creator<ScanFilter> CREATOR = new Parcelable.Creator()
  {
    public ScanFilter createFromParcel(Parcel paramAnonymousParcel)
    {
      ScanFilter.Builder localBuilder = new ScanFilter.Builder();
      if (paramAnonymousParcel.readInt() == 1) {
        localBuilder.setDeviceName(paramAnonymousParcel.readString());
      }
      if (paramAnonymousParcel.readInt() == 1) {
        localBuilder.setDeviceAddress(paramAnonymousParcel.readString());
      }
      Object localObject1;
      if (paramAnonymousParcel.readInt() == 1)
      {
        localObject1 = (ParcelUuid)paramAnonymousParcel.readParcelable(ParcelUuid.class.getClassLoader());
        localBuilder.setServiceUuid((ParcelUuid)localObject1);
        if (paramAnonymousParcel.readInt() == 1) {
          localBuilder.setServiceUuid((ParcelUuid)localObject1, (ParcelUuid)paramAnonymousParcel.readParcelable(ParcelUuid.class.getClassLoader()));
        }
      }
      if (paramAnonymousParcel.readInt() == 1)
      {
        localObject1 = (ParcelUuid)paramAnonymousParcel.readParcelable(ParcelUuid.class.getClassLoader());
        localBuilder.setServiceSolicitationUuid((ParcelUuid)localObject1);
        if (paramAnonymousParcel.readInt() == 1) {
          localBuilder.setServiceSolicitationUuid((ParcelUuid)localObject1, (ParcelUuid)paramAnonymousParcel.readParcelable(ParcelUuid.class.getClassLoader()));
        }
      }
      Object localObject2;
      if (paramAnonymousParcel.readInt() == 1)
      {
        localObject2 = (ParcelUuid)paramAnonymousParcel.readParcelable(ParcelUuid.class.getClassLoader());
        if (paramAnonymousParcel.readInt() == 1)
        {
          localObject1 = new byte[paramAnonymousParcel.readInt()];
          paramAnonymousParcel.readByteArray((byte[])localObject1);
          if (paramAnonymousParcel.readInt() == 0)
          {
            localBuilder.setServiceData((ParcelUuid)localObject2, (byte[])localObject1);
          }
          else
          {
            byte[] arrayOfByte = new byte[paramAnonymousParcel.readInt()];
            paramAnonymousParcel.readByteArray(arrayOfByte);
            localBuilder.setServiceData((ParcelUuid)localObject2, (byte[])localObject1, arrayOfByte);
          }
        }
      }
      int i = paramAnonymousParcel.readInt();
      if (paramAnonymousParcel.readInt() == 1)
      {
        localObject2 = new byte[paramAnonymousParcel.readInt()];
        paramAnonymousParcel.readByteArray((byte[])localObject2);
        if (paramAnonymousParcel.readInt() == 0)
        {
          localBuilder.setManufacturerData(i, (byte[])localObject2);
        }
        else
        {
          localObject1 = new byte[paramAnonymousParcel.readInt()];
          paramAnonymousParcel.readByteArray((byte[])localObject1);
          localBuilder.setManufacturerData(i, (byte[])localObject2, (byte[])localObject1);
        }
      }
      return localBuilder.build();
    }
    
    public ScanFilter[] newArray(int paramAnonymousInt)
    {
      return new ScanFilter[paramAnonymousInt];
    }
  };
  public static final ScanFilter EMPTY = new Builder().build();
  private final String mDeviceAddress;
  private final String mDeviceName;
  private final byte[] mManufacturerData;
  private final byte[] mManufacturerDataMask;
  private final int mManufacturerId;
  private final byte[] mServiceData;
  private final byte[] mServiceDataMask;
  private final ParcelUuid mServiceDataUuid;
  private final ParcelUuid mServiceSolicitationUuid;
  private final ParcelUuid mServiceSolicitationUuidMask;
  private final ParcelUuid mServiceUuid;
  private final ParcelUuid mServiceUuidMask;
  
  private ScanFilter(String paramString1, String paramString2, ParcelUuid paramParcelUuid1, ParcelUuid paramParcelUuid2, ParcelUuid paramParcelUuid3, ParcelUuid paramParcelUuid4, ParcelUuid paramParcelUuid5, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4)
  {
    mDeviceName = paramString1;
    mServiceUuid = paramParcelUuid1;
    mServiceUuidMask = paramParcelUuid2;
    mServiceSolicitationUuid = paramParcelUuid3;
    mServiceSolicitationUuidMask = paramParcelUuid4;
    mDeviceAddress = paramString2;
    mServiceDataUuid = paramParcelUuid5;
    mServiceData = paramArrayOfByte1;
    mServiceDataMask = paramArrayOfByte2;
    mManufacturerId = paramInt;
    mManufacturerData = paramArrayOfByte3;
    mManufacturerDataMask = paramArrayOfByte4;
  }
  
  private boolean matchesPartialData(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
  {
    if ((paramArrayOfByte3 != null) && (paramArrayOfByte3.length >= paramArrayOfByte1.length))
    {
      if (paramArrayOfByte2 == null)
      {
        for (i = 0; i < paramArrayOfByte1.length; i++) {
          if (paramArrayOfByte3[i] != paramArrayOfByte1[i]) {
            return false;
          }
        }
        return true;
      }
      for (int i = 0; i < paramArrayOfByte1.length; i++) {
        if ((paramArrayOfByte2[i] & paramArrayOfByte3[i]) != (paramArrayOfByte2[i] & paramArrayOfByte1[i])) {
          return false;
        }
      }
      return true;
    }
    return false;
  }
  
  private static boolean matchesServiceSolicitationUuid(UUID paramUUID1, UUID paramUUID2, UUID paramUUID3)
  {
    return BitUtils.maskedEquals(paramUUID3, paramUUID1, paramUUID2);
  }
  
  public static boolean matchesServiceSolicitationUuids(ParcelUuid paramParcelUuid1, ParcelUuid paramParcelUuid2, List<ParcelUuid> paramList)
  {
    if (paramParcelUuid1 == null) {
      return true;
    }
    if (paramList == null) {
      return false;
    }
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      ParcelUuid localParcelUuid = (ParcelUuid)localIterator.next();
      if (paramParcelUuid2 == null) {
        paramList = null;
      } else {
        paramList = paramParcelUuid2.getUuid();
      }
      if (matchesServiceUuid(paramParcelUuid1.getUuid(), paramList, localParcelUuid.getUuid())) {
        return true;
      }
    }
    return false;
  }
  
  private static boolean matchesServiceUuid(UUID paramUUID1, UUID paramUUID2, UUID paramUUID3)
  {
    return BitUtils.maskedEquals(paramUUID3, paramUUID1, paramUUID2);
  }
  
  public static boolean matchesServiceUuids(ParcelUuid paramParcelUuid1, ParcelUuid paramParcelUuid2, List<ParcelUuid> paramList)
  {
    if (paramParcelUuid1 == null) {
      return true;
    }
    if (paramList == null) {
      return false;
    }
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      ParcelUuid localParcelUuid = (ParcelUuid)localIterator.next();
      if (paramParcelUuid2 == null) {
        paramList = null;
      } else {
        paramList = paramParcelUuid2.getUuid();
      }
      if (matchesServiceUuid(paramParcelUuid1.getUuid(), paramList, localParcelUuid.getUuid())) {
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
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (ScanFilter)paramObject;
      if ((!Objects.equals(mDeviceName, mDeviceName)) || (!Objects.equals(mDeviceAddress, mDeviceAddress)) || (mManufacturerId != mManufacturerId) || (!Objects.deepEquals(mManufacturerData, mManufacturerData)) || (!Objects.deepEquals(mManufacturerDataMask, mManufacturerDataMask)) || (!Objects.equals(mServiceDataUuid, mServiceDataUuid)) || (!Objects.deepEquals(mServiceData, mServiceData)) || (!Objects.deepEquals(mServiceDataMask, mServiceDataMask)) || (!Objects.equals(mServiceUuid, mServiceUuid)) || (!Objects.equals(mServiceUuidMask, mServiceUuidMask)) || (!Objects.equals(mServiceSolicitationUuid, mServiceSolicitationUuid)) || (!Objects.equals(mServiceSolicitationUuidMask, mServiceSolicitationUuidMask))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public String getDeviceAddress()
  {
    return mDeviceAddress;
  }
  
  public String getDeviceName()
  {
    return mDeviceName;
  }
  
  public byte[] getManufacturerData()
  {
    return mManufacturerData;
  }
  
  public byte[] getManufacturerDataMask()
  {
    return mManufacturerDataMask;
  }
  
  public int getManufacturerId()
  {
    return mManufacturerId;
  }
  
  public byte[] getServiceData()
  {
    return mServiceData;
  }
  
  public byte[] getServiceDataMask()
  {
    return mServiceDataMask;
  }
  
  public ParcelUuid getServiceDataUuid()
  {
    return mServiceDataUuid;
  }
  
  public ParcelUuid getServiceSolicitationUuid()
  {
    return mServiceSolicitationUuid;
  }
  
  public ParcelUuid getServiceSolicitationUuidMask()
  {
    return mServiceSolicitationUuidMask;
  }
  
  public ParcelUuid getServiceUuid()
  {
    return mServiceUuid;
  }
  
  public ParcelUuid getServiceUuidMask()
  {
    return mServiceUuidMask;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { mDeviceName, mDeviceAddress, Integer.valueOf(mManufacturerId), Integer.valueOf(Arrays.hashCode(mManufacturerData)), Integer.valueOf(Arrays.hashCode(mManufacturerDataMask)), mServiceDataUuid, Integer.valueOf(Arrays.hashCode(mServiceData)), Integer.valueOf(Arrays.hashCode(mServiceDataMask)), mServiceUuid, mServiceUuidMask, mServiceSolicitationUuid, mServiceSolicitationUuidMask });
  }
  
  public boolean isAllFieldsEmpty()
  {
    return EMPTY.equals(this);
  }
  
  public boolean matches(ScanResult paramScanResult)
  {
    if (paramScanResult == null) {
      return false;
    }
    BluetoothDevice localBluetoothDevice = paramScanResult.getDevice();
    if ((mDeviceAddress != null) && ((localBluetoothDevice == null) || (!mDeviceAddress.equals(localBluetoothDevice.getAddress())))) {
      return false;
    }
    paramScanResult = paramScanResult.getScanRecord();
    if ((paramScanResult == null) && ((mDeviceName != null) || (mServiceUuid != null) || (mManufacturerData != null) || (mServiceData != null) || (mServiceSolicitationUuid != null))) {
      return false;
    }
    if ((mDeviceName != null) && (!mDeviceName.equals(paramScanResult.getDeviceName()))) {
      return false;
    }
    if ((mServiceUuid != null) && (!matchesServiceUuids(mServiceUuid, mServiceUuidMask, paramScanResult.getServiceUuids()))) {
      return false;
    }
    if ((mServiceSolicitationUuid != null) && (!matchesServiceSolicitationUuids(mServiceSolicitationUuid, mServiceSolicitationUuidMask, paramScanResult.getServiceSolicitationUuids()))) {
      return false;
    }
    if ((mServiceDataUuid != null) && (!matchesPartialData(mServiceData, mServiceDataMask, paramScanResult.getServiceData(mServiceDataUuid)))) {
      return false;
    }
    return (mManufacturerId < 0) || (matchesPartialData(mManufacturerData, mManufacturerDataMask, paramScanResult.getManufacturerSpecificData(mManufacturerId)));
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("BluetoothLeScanFilter [mDeviceName=");
    localStringBuilder.append(mDeviceName);
    localStringBuilder.append(", mDeviceAddress=");
    localStringBuilder.append(mDeviceAddress);
    localStringBuilder.append(", mUuid=");
    localStringBuilder.append(mServiceUuid);
    localStringBuilder.append(", mUuidMask=");
    localStringBuilder.append(mServiceUuidMask);
    localStringBuilder.append(", mServiceSolicitationUuid=");
    localStringBuilder.append(mServiceSolicitationUuid);
    localStringBuilder.append(", mServiceSolicitationUuidMask=");
    localStringBuilder.append(mServiceSolicitationUuidMask);
    localStringBuilder.append(", mServiceDataUuid=");
    localStringBuilder.append(Objects.toString(mServiceDataUuid));
    localStringBuilder.append(", mServiceData=");
    localStringBuilder.append(Arrays.toString(mServiceData));
    localStringBuilder.append(", mServiceDataMask=");
    localStringBuilder.append(Arrays.toString(mServiceDataMask));
    localStringBuilder.append(", mManufacturerId=");
    localStringBuilder.append(mManufacturerId);
    localStringBuilder.append(", mManufacturerData=");
    localStringBuilder.append(Arrays.toString(mManufacturerData));
    localStringBuilder.append(", mManufacturerDataMask=");
    localStringBuilder.append(Arrays.toString(mManufacturerDataMask));
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    String str = mDeviceName;
    int i = 1;
    int j;
    if (str == null) {
      j = 0;
    } else {
      j = 1;
    }
    paramParcel.writeInt(j);
    if (mDeviceName != null) {
      paramParcel.writeString(mDeviceName);
    }
    if (mDeviceAddress == null) {
      j = 0;
    } else {
      j = 1;
    }
    paramParcel.writeInt(j);
    if (mDeviceAddress != null) {
      paramParcel.writeString(mDeviceAddress);
    }
    if (mServiceUuid == null) {
      j = 0;
    } else {
      j = 1;
    }
    paramParcel.writeInt(j);
    if (mServiceUuid != null)
    {
      paramParcel.writeParcelable(mServiceUuid, paramInt);
      if (mServiceUuidMask == null) {
        j = 0;
      } else {
        j = 1;
      }
      paramParcel.writeInt(j);
      if (mServiceUuidMask != null) {
        paramParcel.writeParcelable(mServiceUuidMask, paramInt);
      }
    }
    if (mServiceSolicitationUuid == null) {
      j = 0;
    } else {
      j = 1;
    }
    paramParcel.writeInt(j);
    if (mServiceSolicitationUuid != null)
    {
      paramParcel.writeParcelable(mServiceSolicitationUuid, paramInt);
      if (mServiceSolicitationUuidMask == null) {
        j = 0;
      } else {
        j = 1;
      }
      paramParcel.writeInt(j);
      if (mServiceSolicitationUuidMask != null) {
        paramParcel.writeParcelable(mServiceSolicitationUuidMask, paramInt);
      }
    }
    if (mServiceDataUuid == null) {
      j = 0;
    } else {
      j = 1;
    }
    paramParcel.writeInt(j);
    if (mServiceDataUuid != null)
    {
      paramParcel.writeParcelable(mServiceDataUuid, paramInt);
      if (mServiceData == null) {
        paramInt = 0;
      } else {
        paramInt = 1;
      }
      paramParcel.writeInt(paramInt);
      if (mServiceData != null)
      {
        paramParcel.writeInt(mServiceData.length);
        paramParcel.writeByteArray(mServiceData);
        if (mServiceDataMask == null) {
          paramInt = 0;
        } else {
          paramInt = 1;
        }
        paramParcel.writeInt(paramInt);
        if (mServiceDataMask != null)
        {
          paramParcel.writeInt(mServiceDataMask.length);
          paramParcel.writeByteArray(mServiceDataMask);
        }
      }
    }
    paramParcel.writeInt(mManufacturerId);
    if (mManufacturerData == null) {
      paramInt = 0;
    } else {
      paramInt = 1;
    }
    paramParcel.writeInt(paramInt);
    if (mManufacturerData != null)
    {
      paramParcel.writeInt(mManufacturerData.length);
      paramParcel.writeByteArray(mManufacturerData);
      paramInt = i;
      if (mManufacturerDataMask == null) {
        paramInt = 0;
      }
      paramParcel.writeInt(paramInt);
      if (mManufacturerDataMask != null)
      {
        paramParcel.writeInt(mManufacturerDataMask.length);
        paramParcel.writeByteArray(mManufacturerDataMask);
      }
    }
  }
  
  public static final class Builder
  {
    private String mDeviceAddress;
    private String mDeviceName;
    private byte[] mManufacturerData;
    private byte[] mManufacturerDataMask;
    private int mManufacturerId = -1;
    private byte[] mServiceData;
    private byte[] mServiceDataMask;
    private ParcelUuid mServiceDataUuid;
    private ParcelUuid mServiceSolicitationUuid;
    private ParcelUuid mServiceSolicitationUuidMask;
    private ParcelUuid mServiceUuid;
    private ParcelUuid mUuidMask;
    
    public Builder() {}
    
    public ScanFilter build()
    {
      return new ScanFilter(mDeviceName, mDeviceAddress, mServiceUuid, mUuidMask, mServiceSolicitationUuid, mServiceSolicitationUuidMask, mServiceDataUuid, mServiceData, mServiceDataMask, mManufacturerId, mManufacturerData, mManufacturerDataMask, null);
    }
    
    public Builder setDeviceAddress(String paramString)
    {
      if ((paramString != null) && (!BluetoothAdapter.checkBluetoothAddress(paramString)))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("invalid device address ");
        localStringBuilder.append(paramString);
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      mDeviceAddress = paramString;
      return this;
    }
    
    public Builder setDeviceName(String paramString)
    {
      mDeviceName = paramString;
      return this;
    }
    
    public Builder setManufacturerData(int paramInt, byte[] paramArrayOfByte)
    {
      if ((paramArrayOfByte != null) && (paramInt < 0)) {
        throw new IllegalArgumentException("invalid manufacture id");
      }
      mManufacturerId = paramInt;
      mManufacturerData = paramArrayOfByte;
      mManufacturerDataMask = null;
      return this;
    }
    
    public Builder setManufacturerData(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    {
      if ((paramArrayOfByte1 != null) && (paramInt < 0)) {
        throw new IllegalArgumentException("invalid manufacture id");
      }
      if (mManufacturerDataMask != null) {
        if (mManufacturerData != null)
        {
          if (mManufacturerData.length != mManufacturerDataMask.length) {
            throw new IllegalArgumentException("size mismatch for manufacturerData and manufacturerDataMask");
          }
        }
        else {
          throw new IllegalArgumentException("manufacturerData is null while manufacturerDataMask is not null");
        }
      }
      mManufacturerId = paramInt;
      mManufacturerData = paramArrayOfByte1;
      mManufacturerDataMask = paramArrayOfByte2;
      return this;
    }
    
    public Builder setServiceData(ParcelUuid paramParcelUuid, byte[] paramArrayOfByte)
    {
      if (paramParcelUuid != null)
      {
        mServiceDataUuid = paramParcelUuid;
        mServiceData = paramArrayOfByte;
        mServiceDataMask = null;
        return this;
      }
      throw new IllegalArgumentException("serviceDataUuid is null");
    }
    
    public Builder setServiceData(ParcelUuid paramParcelUuid, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    {
      if (paramParcelUuid != null)
      {
        if (mServiceDataMask != null) {
          if (mServiceData != null)
          {
            if (mServiceData.length != mServiceDataMask.length) {
              throw new IllegalArgumentException("size mismatch for service data and service data mask");
            }
          }
          else {
            throw new IllegalArgumentException("serviceData is null while serviceDataMask is not null");
          }
        }
        mServiceDataUuid = paramParcelUuid;
        mServiceData = paramArrayOfByte1;
        mServiceDataMask = paramArrayOfByte2;
        return this;
      }
      throw new IllegalArgumentException("serviceDataUuid is null");
    }
    
    public Builder setServiceSolicitationUuid(ParcelUuid paramParcelUuid)
    {
      mServiceSolicitationUuid = paramParcelUuid;
      return this;
    }
    
    public Builder setServiceSolicitationUuid(ParcelUuid paramParcelUuid1, ParcelUuid paramParcelUuid2)
    {
      if ((mServiceSolicitationUuidMask != null) && (mServiceSolicitationUuid == null)) {
        throw new IllegalArgumentException("SolicitationUuid is null while SolicitationUuidMask is not null!");
      }
      mServiceSolicitationUuid = paramParcelUuid1;
      mServiceSolicitationUuidMask = paramParcelUuid2;
      return this;
    }
    
    public Builder setServiceUuid(ParcelUuid paramParcelUuid)
    {
      mServiceUuid = paramParcelUuid;
      mUuidMask = null;
      return this;
    }
    
    public Builder setServiceUuid(ParcelUuid paramParcelUuid1, ParcelUuid paramParcelUuid2)
    {
      if ((mUuidMask != null) && (mServiceUuid == null)) {
        throw new IllegalArgumentException("uuid is null while uuidMask is not null!");
      }
      mServiceUuid = paramParcelUuid1;
      mUuidMask = paramParcelUuid2;
      return this;
    }
  }
}
