package android.companion;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable.Creator;
import android.provider.OneTimeUseBuilder;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public final class BluetoothDeviceFilter
  implements DeviceFilter<BluetoothDevice>
{
  public static final Parcelable.Creator<BluetoothDeviceFilter> CREATOR = new Parcelable.Creator()
  {
    public BluetoothDeviceFilter createFromParcel(Parcel paramAnonymousParcel)
    {
      return new BluetoothDeviceFilter(paramAnonymousParcel, null);
    }
    
    public BluetoothDeviceFilter[] newArray(int paramAnonymousInt)
    {
      return new BluetoothDeviceFilter[paramAnonymousInt];
    }
  };
  private final String mAddress;
  private final Pattern mNamePattern;
  private final List<ParcelUuid> mServiceUuidMasks;
  private final List<ParcelUuid> mServiceUuids;
  
  private BluetoothDeviceFilter(Parcel paramParcel)
  {
    this(BluetoothDeviceFilterUtils.patternFromString(paramParcel.readString()), paramParcel.readString(), readUuids(paramParcel), readUuids(paramParcel));
  }
  
  private BluetoothDeviceFilter(Pattern paramPattern, String paramString, List<ParcelUuid> paramList1, List<ParcelUuid> paramList2)
  {
    mNamePattern = paramPattern;
    mAddress = paramString;
    mServiceUuids = CollectionUtils.emptyIfNull(paramList1);
    mServiceUuidMasks = CollectionUtils.emptyIfNull(paramList2);
  }
  
  private static List<ParcelUuid> readUuids(Parcel paramParcel)
  {
    return paramParcel.readParcelableList(new ArrayList(), ParcelUuid.class.getClassLoader());
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
      paramObject = (BluetoothDeviceFilter)paramObject;
      if ((!Objects.equals(mNamePattern, mNamePattern)) || (!Objects.equals(mAddress, mAddress)) || (!Objects.equals(mServiceUuids, mServiceUuids)) || (!Objects.equals(mServiceUuidMasks, mServiceUuidMasks))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public String getAddress()
  {
    return mAddress;
  }
  
  public String getDeviceDisplayName(BluetoothDevice paramBluetoothDevice)
  {
    return BluetoothDeviceFilterUtils.getDeviceDisplayNameInternal(paramBluetoothDevice);
  }
  
  public int getMediumType()
  {
    return 0;
  }
  
  public Pattern getNamePattern()
  {
    return mNamePattern;
  }
  
  public List<ParcelUuid> getServiceUuidMasks()
  {
    return mServiceUuidMasks;
  }
  
  public List<ParcelUuid> getServiceUuids()
  {
    return mServiceUuids;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { mNamePattern, mAddress, mServiceUuids, mServiceUuidMasks });
  }
  
  public boolean matches(BluetoothDevice paramBluetoothDevice)
  {
    boolean bool;
    if ((BluetoothDeviceFilterUtils.matchesAddress(mAddress, paramBluetoothDevice)) && (BluetoothDeviceFilterUtils.matchesServiceUuids(mServiceUuids, mServiceUuidMasks, paramBluetoothDevice)) && (BluetoothDeviceFilterUtils.matchesName(getNamePattern(), paramBluetoothDevice))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(BluetoothDeviceFilterUtils.patternToString(getNamePattern()));
    paramParcel.writeString(mAddress);
    paramParcel.writeParcelableList(mServiceUuids, paramInt);
    paramParcel.writeParcelableList(mServiceUuidMasks, paramInt);
  }
  
  public static final class Builder
    extends OneTimeUseBuilder<BluetoothDeviceFilter>
  {
    private String mAddress;
    private Pattern mNamePattern;
    private ArrayList<ParcelUuid> mServiceUuid;
    private ArrayList<ParcelUuid> mServiceUuidMask;
    
    public Builder() {}
    
    public Builder addServiceUuid(ParcelUuid paramParcelUuid1, ParcelUuid paramParcelUuid2)
    {
      checkNotUsed();
      mServiceUuid = ArrayUtils.add(mServiceUuid, paramParcelUuid1);
      mServiceUuidMask = ArrayUtils.add(mServiceUuidMask, paramParcelUuid2);
      return this;
    }
    
    public BluetoothDeviceFilter build()
    {
      markUsed();
      return new BluetoothDeviceFilter(mNamePattern, mAddress, mServiceUuid, mServiceUuidMask, null);
    }
    
    public Builder setAddress(String paramString)
    {
      checkNotUsed();
      mAddress = paramString;
      return this;
    }
    
    public Builder setNamePattern(Pattern paramPattern)
    {
      checkNotUsed();
      mNamePattern = paramPattern;
      return this;
    }
  }
}
