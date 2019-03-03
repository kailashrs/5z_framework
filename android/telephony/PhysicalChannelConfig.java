package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class PhysicalChannelConfig
  implements Parcelable
{
  public static final int CONNECTION_PRIMARY_SERVING = 1;
  public static final int CONNECTION_SECONDARY_SERVING = 2;
  public static final int CONNECTION_UNKNOWN = Integer.MAX_VALUE;
  public static final Parcelable.Creator<PhysicalChannelConfig> CREATOR = new Parcelable.Creator()
  {
    public PhysicalChannelConfig createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PhysicalChannelConfig(paramAnonymousParcel);
    }
    
    public PhysicalChannelConfig[] newArray(int paramAnonymousInt)
    {
      return new PhysicalChannelConfig[paramAnonymousInt];
    }
  };
  private int mCellBandwidthDownlinkKhz;
  private int mCellConnectionStatus;
  
  public PhysicalChannelConfig(int paramInt1, int paramInt2)
  {
    mCellConnectionStatus = paramInt1;
    mCellBandwidthDownlinkKhz = paramInt2;
  }
  
  public PhysicalChannelConfig(Parcel paramParcel)
  {
    mCellConnectionStatus = paramParcel.readInt();
    mCellBandwidthDownlinkKhz = paramParcel.readInt();
  }
  
  private String getConnectionStatusString()
  {
    int i = mCellConnectionStatus;
    if (i != Integer.MAX_VALUE)
    {
      switch (i)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Invalid(");
        localStringBuilder.append(mCellConnectionStatus);
        localStringBuilder.append(")");
        return localStringBuilder.toString();
      case 2: 
        return "SecondaryServing";
      }
      return "PrimaryServing";
    }
    return "Unknown";
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
    if (!(paramObject instanceof PhysicalChannelConfig)) {
      return false;
    }
    paramObject = (PhysicalChannelConfig)paramObject;
    if ((mCellConnectionStatus != mCellConnectionStatus) || (mCellBandwidthDownlinkKhz != mCellBandwidthDownlinkKhz)) {
      bool = false;
    }
    return bool;
  }
  
  public int getCellBandwidthDownlink()
  {
    return mCellBandwidthDownlinkKhz;
  }
  
  public int getConnectionStatus()
  {
    return mCellConnectionStatus;
  }
  
  public int hashCode()
  {
    return mCellBandwidthDownlinkKhz * 29 + mCellConnectionStatus * 31;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{mConnectionStatus=");
    localStringBuilder.append(getConnectionStatusString());
    localStringBuilder.append(",mCellBandwidthDownlinkKhz=");
    localStringBuilder.append(mCellBandwidthDownlinkKhz);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mCellConnectionStatus);
    paramParcel.writeInt(mCellBandwidthDownlinkKhz);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ConnectionStatus {}
}
