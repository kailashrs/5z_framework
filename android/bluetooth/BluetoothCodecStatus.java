package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class BluetoothCodecStatus
  implements Parcelable
{
  public static final Parcelable.Creator<BluetoothCodecStatus> CREATOR = new Parcelable.Creator()
  {
    public BluetoothCodecStatus createFromParcel(Parcel paramAnonymousParcel)
    {
      return new BluetoothCodecStatus((BluetoothCodecConfig)paramAnonymousParcel.readTypedObject(BluetoothCodecConfig.CREATOR), (BluetoothCodecConfig[])paramAnonymousParcel.createTypedArray(BluetoothCodecConfig.CREATOR), (BluetoothCodecConfig[])paramAnonymousParcel.createTypedArray(BluetoothCodecConfig.CREATOR));
    }
    
    public BluetoothCodecStatus[] newArray(int paramAnonymousInt)
    {
      return new BluetoothCodecStatus[paramAnonymousInt];
    }
  };
  public static final String EXTRA_CODEC_STATUS = "android.bluetooth.codec.extra.CODEC_STATUS";
  private final BluetoothCodecConfig mCodecConfig;
  private final BluetoothCodecConfig[] mCodecsLocalCapabilities;
  private final BluetoothCodecConfig[] mCodecsSelectableCapabilities;
  
  public BluetoothCodecStatus(BluetoothCodecConfig paramBluetoothCodecConfig, BluetoothCodecConfig[] paramArrayOfBluetoothCodecConfig1, BluetoothCodecConfig[] paramArrayOfBluetoothCodecConfig2)
  {
    mCodecConfig = paramBluetoothCodecConfig;
    mCodecsLocalCapabilities = paramArrayOfBluetoothCodecConfig1;
    mCodecsSelectableCapabilities = paramArrayOfBluetoothCodecConfig2;
  }
  
  private static boolean sameCapabilities(BluetoothCodecConfig[] paramArrayOfBluetoothCodecConfig1, BluetoothCodecConfig[] paramArrayOfBluetoothCodecConfig2)
  {
    boolean bool = false;
    if (paramArrayOfBluetoothCodecConfig1 == null)
    {
      if (paramArrayOfBluetoothCodecConfig2 == null) {
        bool = true;
      }
      return bool;
    }
    if (paramArrayOfBluetoothCodecConfig2 == null) {
      return false;
    }
    if (paramArrayOfBluetoothCodecConfig1.length != paramArrayOfBluetoothCodecConfig2.length) {
      return false;
    }
    return Arrays.asList(paramArrayOfBluetoothCodecConfig1).containsAll(Arrays.asList(paramArrayOfBluetoothCodecConfig2));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof BluetoothCodecStatus;
    boolean bool2 = false;
    if (bool1)
    {
      paramObject = (BluetoothCodecStatus)paramObject;
      bool1 = bool2;
      if (Objects.equals(mCodecConfig, mCodecConfig))
      {
        bool1 = bool2;
        if (sameCapabilities(mCodecsLocalCapabilities, mCodecsLocalCapabilities))
        {
          bool1 = bool2;
          if (sameCapabilities(mCodecsSelectableCapabilities, mCodecsSelectableCapabilities)) {
            bool1 = true;
          }
        }
      }
      return bool1;
    }
    return false;
  }
  
  public BluetoothCodecConfig getCodecConfig()
  {
    return mCodecConfig;
  }
  
  public BluetoothCodecConfig[] getCodecsLocalCapabilities()
  {
    return mCodecsLocalCapabilities;
  }
  
  public BluetoothCodecConfig[] getCodecsSelectableCapabilities()
  {
    return mCodecsSelectableCapabilities;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { mCodecConfig, mCodecsLocalCapabilities, mCodecsLocalCapabilities });
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{mCodecConfig:");
    localStringBuilder.append(mCodecConfig);
    localStringBuilder.append(",mCodecsLocalCapabilities:");
    localStringBuilder.append(Arrays.toString(mCodecsLocalCapabilities));
    localStringBuilder.append(",mCodecsSelectableCapabilities:");
    localStringBuilder.append(Arrays.toString(mCodecsSelectableCapabilities));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeTypedObject(mCodecConfig, 0);
    paramParcel.writeTypedArray(mCodecsLocalCapabilities, 0);
    paramParcel.writeTypedArray(mCodecsSelectableCapabilities, 0);
  }
}
