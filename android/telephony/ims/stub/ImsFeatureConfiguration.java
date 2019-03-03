package android.telephony.ims.stub;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.ArraySet;
import java.util.Set;

@SystemApi
public final class ImsFeatureConfiguration
  implements Parcelable
{
  public static final Parcelable.Creator<ImsFeatureConfiguration> CREATOR = new Parcelable.Creator()
  {
    public ImsFeatureConfiguration createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ImsFeatureConfiguration(paramAnonymousParcel);
    }
    
    public ImsFeatureConfiguration[] newArray(int paramAnonymousInt)
    {
      return new ImsFeatureConfiguration[paramAnonymousInt];
    }
  };
  private final Set<FeatureSlotPair> mFeatures;
  
  public ImsFeatureConfiguration()
  {
    mFeatures = new ArraySet();
  }
  
  protected ImsFeatureConfiguration(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    mFeatures = new ArraySet(i);
    for (int j = 0; j < i; j++) {
      mFeatures.add(new FeatureSlotPair(paramParcel.readInt(), paramParcel.readInt()));
    }
  }
  
  public ImsFeatureConfiguration(Set<FeatureSlotPair> paramSet)
  {
    mFeatures = new ArraySet();
    if (paramSet != null) {
      mFeatures.addAll(paramSet);
    }
  }
  
  void addFeature(int paramInt1, int paramInt2)
  {
    mFeatures.add(new FeatureSlotPair(paramInt1, paramInt2));
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
    if (!(paramObject instanceof ImsFeatureConfiguration)) {
      return false;
    }
    paramObject = (ImsFeatureConfiguration)paramObject;
    return mFeatures.equals(mFeatures);
  }
  
  public Set<FeatureSlotPair> getServiceFeatures()
  {
    return new ArraySet(mFeatures);
  }
  
  public int hashCode()
  {
    return mFeatures.hashCode();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    FeatureSlotPair[] arrayOfFeatureSlotPair = new FeatureSlotPair[mFeatures.size()];
    mFeatures.toArray(arrayOfFeatureSlotPair);
    paramParcel.writeInt(arrayOfFeatureSlotPair.length);
    int i = arrayOfFeatureSlotPair.length;
    for (paramInt = 0; paramInt < i; paramInt++)
    {
      FeatureSlotPair localFeatureSlotPair = arrayOfFeatureSlotPair[paramInt];
      paramParcel.writeInt(slotId);
      paramParcel.writeInt(featureType);
    }
  }
  
  public static class Builder
  {
    ImsFeatureConfiguration mConfig = new ImsFeatureConfiguration();
    
    public Builder() {}
    
    public Builder addFeature(int paramInt1, int paramInt2)
    {
      mConfig.addFeature(paramInt1, paramInt2);
      return this;
    }
    
    public ImsFeatureConfiguration build()
    {
      return mConfig;
    }
  }
  
  public static final class FeatureSlotPair
  {
    public final int featureType;
    public final int slotId;
    
    public FeatureSlotPair(int paramInt1, int paramInt2)
    {
      slotId = paramInt1;
      featureType = paramInt2;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool = true;
      if (this == paramObject) {
        return true;
      }
      if ((paramObject != null) && (getClass() == paramObject.getClass()))
      {
        paramObject = (FeatureSlotPair)paramObject;
        if (slotId != slotId) {
          return false;
        }
        if (featureType != featureType) {
          bool = false;
        }
        return bool;
      }
      return false;
    }
    
    public int hashCode()
    {
      return 31 * slotId + featureType;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("{s=");
      localStringBuilder.append(slotId);
      localStringBuilder.append(", f=");
      localStringBuilder.append(featureType);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
  }
}
