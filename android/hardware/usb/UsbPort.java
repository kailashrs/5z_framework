package android.hardware.usb;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;

public final class UsbPort
  implements Parcelable
{
  public static final Parcelable.Creator<UsbPort> CREATOR = new Parcelable.Creator()
  {
    public UsbPort createFromParcel(Parcel paramAnonymousParcel)
    {
      return new UsbPort(paramAnonymousParcel.readString(), paramAnonymousParcel.readInt());
    }
    
    public UsbPort[] newArray(int paramAnonymousInt)
    {
      return new UsbPort[paramAnonymousInt];
    }
  };
  public static final int DATA_ROLE_DEVICE = 2;
  public static final int DATA_ROLE_HOST = 1;
  public static final int DATA_ROLE_NONE = 0;
  public static final int MODE_AUDIO_ACCESSORY = 4;
  public static final int MODE_DEBUG_ACCESSORY = 8;
  public static final int MODE_DFP = 2;
  public static final int MODE_DUAL = 3;
  public static final int MODE_NONE = 0;
  public static final int MODE_UFP = 1;
  private static final int NUM_DATA_ROLES = 3;
  public static final int POWER_ROLE_NONE = 0;
  private static final int POWER_ROLE_OFFSET = 0;
  public static final int POWER_ROLE_SINK = 2;
  public static final int POWER_ROLE_SOURCE = 1;
  private final String mId;
  private final int mSupportedModes;
  
  public UsbPort(String paramString, int paramInt)
  {
    mId = paramString;
    mSupportedModes = paramInt;
  }
  
  public static void checkDataRole(int paramInt)
  {
    Preconditions.checkArgumentInRange(paramInt, 0, 2, "powerRole");
  }
  
  public static void checkMode(int paramInt)
  {
    Preconditions.checkArgumentInRange(paramInt, 0, 3, "portMode");
  }
  
  public static void checkPowerRole(int paramInt)
  {
    Preconditions.checkArgumentInRange(paramInt, 0, 2, "powerRole");
  }
  
  public static void checkRoles(int paramInt1, int paramInt2)
  {
    Preconditions.checkArgumentInRange(paramInt1, 0, 2, "powerRole");
    Preconditions.checkArgumentInRange(paramInt2, 0, 2, "dataRole");
  }
  
  public static int combineRolesAsBit(int paramInt1, int paramInt2)
  {
    checkRoles(paramInt1, paramInt2);
    return 1 << (paramInt1 + 0) * 3 + paramInt2;
  }
  
  public static String dataRoleToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return Integer.toString(paramInt);
    case 2: 
      return "device";
    case 1: 
      return "host";
    }
    return "no-data";
  }
  
  public static String modeToString(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (paramInt == 0) {
      return "none";
    }
    if ((paramInt & 0x3) == 3) {
      localStringBuilder.append("dual, ");
    } else if ((paramInt & 0x2) == 2) {
      localStringBuilder.append("dfp, ");
    } else if ((paramInt & 0x1) == 1) {
      localStringBuilder.append("ufp, ");
    }
    if ((paramInt & 0x4) == 4) {
      localStringBuilder.append("audio_acc, ");
    }
    if ((paramInt & 0x8) == 8) {
      localStringBuilder.append("debug_acc, ");
    }
    if (localStringBuilder.length() == 0) {
      return Integer.toString(paramInt);
    }
    return localStringBuilder.substring(0, localStringBuilder.length() - 2);
  }
  
  public static String powerRoleToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return Integer.toString(paramInt);
    case 2: 
      return "sink";
    case 1: 
      return "source";
    }
    return "no-power";
  }
  
  public static String roleCombinationsToString(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    int i = paramInt;
    paramInt = 1;
    while (i != 0)
    {
      int j = Integer.numberOfTrailingZeros(i);
      i &= 1 << j;
      int k = j / 3;
      if (paramInt != 0) {
        paramInt = 0;
      } else {
        localStringBuilder.append(", ");
      }
      localStringBuilder.append(powerRoleToString(k + 0));
      localStringBuilder.append(':');
      localStringBuilder.append(dataRoleToString(j % 3));
    }
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getId()
  {
    return mId;
  }
  
  public int getSupportedModes()
  {
    return mSupportedModes;
  }
  
  public boolean isModeSupported(int paramInt)
  {
    return (mSupportedModes & paramInt) == paramInt;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("UsbPort{id=");
    localStringBuilder.append(mId);
    localStringBuilder.append(", supportedModes=");
    localStringBuilder.append(modeToString(mSupportedModes));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mId);
    paramParcel.writeInt(mSupportedModes);
  }
}
