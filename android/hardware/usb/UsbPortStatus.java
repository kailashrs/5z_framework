package android.hardware.usb;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class UsbPortStatus
  implements Parcelable
{
  public static final Parcelable.Creator<UsbPortStatus> CREATOR = new Parcelable.Creator()
  {
    public UsbPortStatus createFromParcel(Parcel paramAnonymousParcel)
    {
      return new UsbPortStatus(paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt());
    }
    
    public UsbPortStatus[] newArray(int paramAnonymousInt)
    {
      return new UsbPortStatus[paramAnonymousInt];
    }
  };
  private final int mCurrentDataRole;
  private final int mCurrentMode;
  private final int mCurrentPowerRole;
  private final int mSupportedRoleCombinations;
  
  public UsbPortStatus(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mCurrentMode = paramInt1;
    mCurrentPowerRole = paramInt2;
    mCurrentDataRole = paramInt3;
    mSupportedRoleCombinations = paramInt4;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getCurrentDataRole()
  {
    return mCurrentDataRole;
  }
  
  public int getCurrentMode()
  {
    return mCurrentMode;
  }
  
  public int getCurrentPowerRole()
  {
    return mCurrentPowerRole;
  }
  
  public int getSupportedRoleCombinations()
  {
    return mSupportedRoleCombinations;
  }
  
  public boolean isConnected()
  {
    boolean bool;
    if (mCurrentMode != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isRoleCombinationSupported(int paramInt1, int paramInt2)
  {
    boolean bool;
    if ((mSupportedRoleCombinations & UsbPort.combineRolesAsBit(paramInt1, paramInt2)) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("UsbPortStatus{connected=");
    localStringBuilder.append(isConnected());
    localStringBuilder.append(", currentMode=");
    localStringBuilder.append(UsbPort.modeToString(mCurrentMode));
    localStringBuilder.append(", currentPowerRole=");
    localStringBuilder.append(UsbPort.powerRoleToString(mCurrentPowerRole));
    localStringBuilder.append(", currentDataRole=");
    localStringBuilder.append(UsbPort.dataRoleToString(mCurrentDataRole));
    localStringBuilder.append(", supportedRoleCombinations=");
    localStringBuilder.append(UsbPort.roleCombinationsToString(mSupportedRoleCombinations));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mCurrentMode);
    paramParcel.writeInt(mCurrentPowerRole);
    paramParcel.writeInt(mCurrentDataRole);
    paramParcel.writeInt(mSupportedRoleCombinations);
  }
}
