package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class FeatureGroupInfo
  implements Parcelable
{
  public static final Parcelable.Creator<FeatureGroupInfo> CREATOR = new Parcelable.Creator()
  {
    public FeatureGroupInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      FeatureGroupInfo localFeatureGroupInfo = new FeatureGroupInfo();
      features = ((FeatureInfo[])paramAnonymousParcel.createTypedArray(FeatureInfo.CREATOR));
      return localFeatureGroupInfo;
    }
    
    public FeatureGroupInfo[] newArray(int paramAnonymousInt)
    {
      return new FeatureGroupInfo[paramAnonymousInt];
    }
  };
  public FeatureInfo[] features;
  
  public FeatureGroupInfo() {}
  
  public FeatureGroupInfo(FeatureGroupInfo paramFeatureGroupInfo)
  {
    features = features;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeTypedArray(features, paramInt);
  }
}
