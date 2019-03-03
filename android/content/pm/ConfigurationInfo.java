package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ConfigurationInfo
  implements Parcelable
{
  public static final Parcelable.Creator<ConfigurationInfo> CREATOR = new Parcelable.Creator()
  {
    public ConfigurationInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ConfigurationInfo(paramAnonymousParcel, null);
    }
    
    public ConfigurationInfo[] newArray(int paramAnonymousInt)
    {
      return new ConfigurationInfo[paramAnonymousInt];
    }
  };
  public static final int GL_ES_VERSION_UNDEFINED = 0;
  public static final int INPUT_FEATURE_FIVE_WAY_NAV = 2;
  public static final int INPUT_FEATURE_HARD_KEYBOARD = 1;
  public int reqGlEsVersion;
  public int reqInputFeatures = 0;
  public int reqKeyboardType;
  public int reqNavigation;
  public int reqTouchScreen;
  
  public ConfigurationInfo() {}
  
  public ConfigurationInfo(ConfigurationInfo paramConfigurationInfo)
  {
    reqTouchScreen = reqTouchScreen;
    reqKeyboardType = reqKeyboardType;
    reqNavigation = reqNavigation;
    reqInputFeatures = reqInputFeatures;
    reqGlEsVersion = reqGlEsVersion;
  }
  
  private ConfigurationInfo(Parcel paramParcel)
  {
    reqTouchScreen = paramParcel.readInt();
    reqKeyboardType = paramParcel.readInt();
    reqNavigation = paramParcel.readInt();
    reqInputFeatures = paramParcel.readInt();
    reqGlEsVersion = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getGlEsVersion()
  {
    int i = reqGlEsVersion;
    int j = reqGlEsVersion;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(String.valueOf((i & 0xFFFF0000) >> 16));
    localStringBuilder.append(".");
    localStringBuilder.append(String.valueOf(j & 0xFFFF));
    return localStringBuilder.toString();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ConfigurationInfo{");
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    localStringBuilder.append(" touchscreen = ");
    localStringBuilder.append(reqTouchScreen);
    localStringBuilder.append(" inputMethod = ");
    localStringBuilder.append(reqKeyboardType);
    localStringBuilder.append(" navigation = ");
    localStringBuilder.append(reqNavigation);
    localStringBuilder.append(" reqInputFeatures = ");
    localStringBuilder.append(reqInputFeatures);
    localStringBuilder.append(" reqGlEsVersion = ");
    localStringBuilder.append(reqGlEsVersion);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(reqTouchScreen);
    paramParcel.writeInt(reqKeyboardType);
    paramParcel.writeInt(reqNavigation);
    paramParcel.writeInt(reqInputFeatures);
    paramParcel.writeInt(reqGlEsVersion);
  }
}
