package android.companion;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.provider.OneTimeUseBuilder;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class AssociationRequest
  implements Parcelable
{
  public static final Parcelable.Creator<AssociationRequest> CREATOR = new Parcelable.Creator()
  {
    public AssociationRequest createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AssociationRequest(paramAnonymousParcel, null);
    }
    
    public AssociationRequest[] newArray(int paramAnonymousInt)
    {
      return new AssociationRequest[paramAnonymousInt];
    }
  };
  private final List<DeviceFilter<?>> mDeviceFilters;
  private final boolean mSingleDevice;
  
  private AssociationRequest(Parcel paramParcel)
  {
    this(bool, paramParcel.readParcelableList(new ArrayList(), AssociationRequest.class.getClassLoader()));
  }
  
  private AssociationRequest(boolean paramBoolean, List<DeviceFilter<?>> paramList)
  {
    mSingleDevice = paramBoolean;
    mDeviceFilters = CollectionUtils.emptyIfNull(paramList);
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
      paramObject = (AssociationRequest)paramObject;
      if ((mSingleDevice != mSingleDevice) || (!Objects.equals(mDeviceFilters, mDeviceFilters))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public List<DeviceFilter<?>> getDeviceFilters()
  {
    return mDeviceFilters;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Boolean.valueOf(mSingleDevice), mDeviceFilters });
  }
  
  public boolean isSingleDevice()
  {
    return mSingleDevice;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("AssociationRequest{mSingleDevice=");
    localStringBuilder.append(mSingleDevice);
    localStringBuilder.append(", mDeviceFilters=");
    localStringBuilder.append(mDeviceFilters);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeByte((byte)mSingleDevice);
    paramParcel.writeParcelableList(mDeviceFilters, paramInt);
  }
  
  public static final class Builder
    extends OneTimeUseBuilder<AssociationRequest>
  {
    private ArrayList<DeviceFilter<?>> mDeviceFilters = null;
    private boolean mSingleDevice = false;
    
    public Builder() {}
    
    public Builder addDeviceFilter(DeviceFilter<?> paramDeviceFilter)
    {
      checkNotUsed();
      if (paramDeviceFilter != null) {
        mDeviceFilters = ArrayUtils.add(mDeviceFilters, paramDeviceFilter);
      }
      return this;
    }
    
    public AssociationRequest build()
    {
      markUsed();
      return new AssociationRequest(mSingleDevice, mDeviceFilters, null);
    }
    
    public Builder setSingleDevice(boolean paramBoolean)
    {
      checkNotUsed();
      mSingleDevice = paramBoolean;
      return this;
    }
  }
}
