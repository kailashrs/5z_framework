package android.util;

import android.content.res.Configuration;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.PrintWriter;

public class MergedConfiguration
  implements Parcelable
{
  public static final Parcelable.Creator<MergedConfiguration> CREATOR = new Parcelable.Creator()
  {
    public MergedConfiguration createFromParcel(Parcel paramAnonymousParcel)
    {
      return new MergedConfiguration(paramAnonymousParcel, null);
    }
    
    public MergedConfiguration[] newArray(int paramAnonymousInt)
    {
      return new MergedConfiguration[paramAnonymousInt];
    }
  };
  private Configuration mGlobalConfig = new Configuration();
  private Configuration mMergedConfig = new Configuration();
  private Configuration mOverrideConfig = new Configuration();
  
  public MergedConfiguration() {}
  
  public MergedConfiguration(Configuration paramConfiguration)
  {
    setGlobalConfiguration(paramConfiguration);
  }
  
  public MergedConfiguration(Configuration paramConfiguration1, Configuration paramConfiguration2)
  {
    setConfiguration(paramConfiguration1, paramConfiguration2);
  }
  
  private MergedConfiguration(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public MergedConfiguration(MergedConfiguration paramMergedConfiguration)
  {
    setConfiguration(paramMergedConfiguration.getGlobalConfiguration(), paramMergedConfiguration.getOverrideConfiguration());
  }
  
  private void updateMergedConfig()
  {
    mMergedConfig.setTo(mGlobalConfig);
    mMergedConfig.updateFrom(mOverrideConfig);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void dump(PrintWriter paramPrintWriter, String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("mGlobalConfig=");
    localStringBuilder.append(mGlobalConfig);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("mOverrideConfig=");
    localStringBuilder.append(mOverrideConfig);
    paramPrintWriter.println(localStringBuilder.toString());
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof MergedConfiguration)) {
      return false;
    }
    if (paramObject == this) {
      return true;
    }
    return mMergedConfig.equals(mMergedConfig);
  }
  
  public Configuration getGlobalConfiguration()
  {
    return mGlobalConfig;
  }
  
  public Configuration getMergedConfiguration()
  {
    return mMergedConfig;
  }
  
  public Configuration getOverrideConfiguration()
  {
    return mOverrideConfig;
  }
  
  public int hashCode()
  {
    return mMergedConfig.hashCode();
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    mGlobalConfig = ((Configuration)paramParcel.readParcelable(Configuration.class.getClassLoader()));
    mOverrideConfig = ((Configuration)paramParcel.readParcelable(Configuration.class.getClassLoader()));
    mMergedConfig = ((Configuration)paramParcel.readParcelable(Configuration.class.getClassLoader()));
  }
  
  public void setConfiguration(Configuration paramConfiguration1, Configuration paramConfiguration2)
  {
    mGlobalConfig.setTo(paramConfiguration1);
    mOverrideConfig.setTo(paramConfiguration2);
    updateMergedConfig();
  }
  
  public void setGlobalConfiguration(Configuration paramConfiguration)
  {
    mGlobalConfig.setTo(paramConfiguration);
    updateMergedConfig();
  }
  
  public void setOverrideConfiguration(Configuration paramConfiguration)
  {
    mOverrideConfig.setTo(paramConfiguration);
    updateMergedConfig();
  }
  
  public void setTo(MergedConfiguration paramMergedConfiguration)
  {
    setConfiguration(mGlobalConfig, mOverrideConfig);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{mGlobalConfig=");
    localStringBuilder.append(mGlobalConfig);
    localStringBuilder.append(" mOverrideConfig=");
    localStringBuilder.append(mOverrideConfig);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void unset()
  {
    mGlobalConfig.unset();
    mOverrideConfig.unset();
    updateMergedConfig();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mGlobalConfig, paramInt);
    paramParcel.writeParcelable(mOverrideConfig, paramInt);
    paramParcel.writeParcelable(mMergedConfig, paramInt);
  }
}
