package android.hardware.display;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public final class WifiDisplay
  implements Parcelable
{
  public static final Parcelable.Creator<WifiDisplay> CREATOR = new Parcelable.Creator()
  {
    public WifiDisplay createFromParcel(Parcel paramAnonymousParcel)
    {
      String str1 = paramAnonymousParcel.readString();
      String str2 = paramAnonymousParcel.readString();
      String str3 = paramAnonymousParcel.readString();
      boolean bool1;
      if (paramAnonymousParcel.readInt() != 0) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      boolean bool2;
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      boolean bool3;
      if (paramAnonymousParcel.readInt() != 0) {
        bool3 = true;
      } else {
        bool3 = false;
      }
      return new WifiDisplay(str1, str2, str3, bool1, bool2, bool3);
    }
    
    public WifiDisplay[] newArray(int paramAnonymousInt)
    {
      WifiDisplay[] arrayOfWifiDisplay;
      if (paramAnonymousInt == 0) {
        arrayOfWifiDisplay = WifiDisplay.EMPTY_ARRAY;
      } else {
        arrayOfWifiDisplay = new WifiDisplay[paramAnonymousInt];
      }
      return arrayOfWifiDisplay;
    }
  };
  public static final WifiDisplay[] EMPTY_ARRAY = new WifiDisplay[0];
  private final boolean mCanConnect;
  private final String mDeviceAddress;
  private final String mDeviceAlias;
  private final String mDeviceName;
  private final boolean mIsAvailable;
  private final boolean mIsRemembered;
  
  public WifiDisplay(String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    if (paramString1 != null)
    {
      if (paramString2 != null)
      {
        mDeviceAddress = paramString1;
        mDeviceName = paramString2;
        mDeviceAlias = paramString3;
        mIsAvailable = paramBoolean1;
        mCanConnect = paramBoolean2;
        mIsRemembered = paramBoolean3;
        return;
      }
      throw new IllegalArgumentException("deviceName must not be null");
    }
    throw new IllegalArgumentException("deviceAddress must not be null");
  }
  
  public boolean canConnect()
  {
    return mCanConnect;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(WifiDisplay paramWifiDisplay)
  {
    boolean bool;
    if ((paramWifiDisplay != null) && (mDeviceAddress.equals(mDeviceAddress)) && (mDeviceName.equals(mDeviceName)) && (Objects.equals(mDeviceAlias, mDeviceAlias))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool;
    if (((paramObject instanceof WifiDisplay)) && (equals((WifiDisplay)paramObject))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String getDeviceAddress()
  {
    return mDeviceAddress;
  }
  
  public String getDeviceAlias()
  {
    return mDeviceAlias;
  }
  
  public String getDeviceName()
  {
    return mDeviceName;
  }
  
  public String getFriendlyDisplayName()
  {
    String str;
    if (mDeviceAlias != null) {
      str = mDeviceAlias;
    } else {
      str = mDeviceName;
    }
    return str;
  }
  
  public boolean hasSameAddress(WifiDisplay paramWifiDisplay)
  {
    boolean bool;
    if ((paramWifiDisplay != null) && (mDeviceAddress.equals(mDeviceAddress))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public int hashCode()
  {
    return mDeviceAddress.hashCode();
  }
  
  public boolean isAvailable()
  {
    return mIsAvailable;
  }
  
  public boolean isRemembered()
  {
    return mIsRemembered;
  }
  
  public String toString()
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(mDeviceName);
    ((StringBuilder)localObject1).append(" (");
    ((StringBuilder)localObject1).append(mDeviceAddress);
    ((StringBuilder)localObject1).append(")");
    Object localObject2 = ((StringBuilder)localObject1).toString();
    localObject1 = localObject2;
    if (mDeviceAlias != null)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localObject2);
      ((StringBuilder)localObject1).append(", alias ");
      ((StringBuilder)localObject1).append(mDeviceAlias);
      localObject1 = ((StringBuilder)localObject1).toString();
    }
    localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append((String)localObject1);
    ((StringBuilder)localObject2).append(", isAvailable ");
    ((StringBuilder)localObject2).append(mIsAvailable);
    ((StringBuilder)localObject2).append(", canConnect ");
    ((StringBuilder)localObject2).append(mCanConnect);
    ((StringBuilder)localObject2).append(", isRemembered ");
    ((StringBuilder)localObject2).append(mIsRemembered);
    return ((StringBuilder)localObject2).toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mDeviceAddress);
    paramParcel.writeString(mDeviceName);
    paramParcel.writeString(mDeviceAlias);
    paramParcel.writeInt(mIsAvailable);
    paramParcel.writeInt(mCanConnect);
    paramParcel.writeInt(mIsRemembered);
  }
}
