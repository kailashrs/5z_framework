package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class BluetoothHidDeviceAppQosSettings
  implements Parcelable
{
  public static final Parcelable.Creator<BluetoothHidDeviceAppQosSettings> CREATOR = new Parcelable.Creator()
  {
    public BluetoothHidDeviceAppQosSettings createFromParcel(Parcel paramAnonymousParcel)
    {
      return new BluetoothHidDeviceAppQosSettings(paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt());
    }
    
    public BluetoothHidDeviceAppQosSettings[] newArray(int paramAnonymousInt)
    {
      return new BluetoothHidDeviceAppQosSettings[paramAnonymousInt];
    }
  };
  public static final int MAX = -1;
  public static final int SERVICE_BEST_EFFORT = 1;
  public static final int SERVICE_GUARANTEED = 2;
  public static final int SERVICE_NO_TRAFFIC = 0;
  private final int mDelayVariation;
  private final int mLatency;
  private final int mPeakBandwidth;
  private final int mServiceType;
  private final int mTokenBucketSize;
  private final int mTokenRate;
  
  public BluetoothHidDeviceAppQosSettings(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    mServiceType = paramInt1;
    mTokenRate = paramInt2;
    mTokenBucketSize = paramInt3;
    mPeakBandwidth = paramInt4;
    mLatency = paramInt5;
    mDelayVariation = paramInt6;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getDelayVariation()
  {
    return mDelayVariation;
  }
  
  public int getLatency()
  {
    return mLatency;
  }
  
  public int getPeakBandwidth()
  {
    return mPeakBandwidth;
  }
  
  public int getServiceType()
  {
    return mServiceType;
  }
  
  public int getTokenBucketSize()
  {
    return mTokenBucketSize;
  }
  
  public int getTokenRate()
  {
    return mTokenRate;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mServiceType);
    paramParcel.writeInt(mTokenRate);
    paramParcel.writeInt(mTokenBucketSize);
    paramParcel.writeInt(mPeakBandwidth);
    paramParcel.writeInt(mLatency);
    paramParcel.writeInt(mDelayVariation);
  }
}
