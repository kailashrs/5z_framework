package android.companion;

import android.annotation.SuppressLint;
import android.net.wifi.ScanResult;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.provider.OneTimeUseBuilder;
import java.util.Objects;
import java.util.regex.Pattern;

public final class WifiDeviceFilter
  implements DeviceFilter<ScanResult>
{
  public static final Parcelable.Creator<WifiDeviceFilter> CREATOR = new Parcelable.Creator()
  {
    public WifiDeviceFilter createFromParcel(Parcel paramAnonymousParcel)
    {
      return new WifiDeviceFilter(paramAnonymousParcel, null);
    }
    
    public WifiDeviceFilter[] newArray(int paramAnonymousInt)
    {
      return new WifiDeviceFilter[paramAnonymousInt];
    }
  };
  private final Pattern mNamePattern;
  
  @SuppressLint({"ParcelClassLoader"})
  private WifiDeviceFilter(Parcel paramParcel)
  {
    this(BluetoothDeviceFilterUtils.patternFromString(paramParcel.readString()));
  }
  
  private WifiDeviceFilter(Pattern paramPattern)
  {
    mNamePattern = paramPattern;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (WifiDeviceFilter)paramObject;
      return Objects.equals(mNamePattern, mNamePattern);
    }
    return false;
  }
  
  public String getDeviceDisplayName(ScanResult paramScanResult)
  {
    return BluetoothDeviceFilterUtils.getDeviceDisplayNameInternal(paramScanResult);
  }
  
  public int getMediumType()
  {
    return 2;
  }
  
  public Pattern getNamePattern()
  {
    return mNamePattern;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { mNamePattern });
  }
  
  public boolean matches(ScanResult paramScanResult)
  {
    return BluetoothDeviceFilterUtils.matchesName(getNamePattern(), paramScanResult);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(BluetoothDeviceFilterUtils.patternToString(getNamePattern()));
  }
  
  public static final class Builder
    extends OneTimeUseBuilder<WifiDeviceFilter>
  {
    private Pattern mNamePattern;
    
    public Builder() {}
    
    public WifiDeviceFilter build()
    {
      markUsed();
      return new WifiDeviceFilter(mNamePattern, null);
    }
    
    public Builder setNamePattern(Pattern paramPattern)
    {
      checkNotUsed();
      mNamePattern = paramPattern;
      return this;
    }
  }
}
