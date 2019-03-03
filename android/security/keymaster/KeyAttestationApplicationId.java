package android.security.keymaster;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class KeyAttestationApplicationId
  implements Parcelable
{
  public static final Parcelable.Creator<KeyAttestationApplicationId> CREATOR = new Parcelable.Creator()
  {
    public KeyAttestationApplicationId createFromParcel(Parcel paramAnonymousParcel)
    {
      return new KeyAttestationApplicationId(paramAnonymousParcel);
    }
    
    public KeyAttestationApplicationId[] newArray(int paramAnonymousInt)
    {
      return new KeyAttestationApplicationId[paramAnonymousInt];
    }
  };
  private final KeyAttestationPackageInfo[] mAttestationPackageInfos;
  
  KeyAttestationApplicationId(Parcel paramParcel)
  {
    mAttestationPackageInfos = ((KeyAttestationPackageInfo[])paramParcel.createTypedArray(KeyAttestationPackageInfo.CREATOR));
  }
  
  public KeyAttestationApplicationId(KeyAttestationPackageInfo[] paramArrayOfKeyAttestationPackageInfo)
  {
    mAttestationPackageInfos = paramArrayOfKeyAttestationPackageInfo;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public KeyAttestationPackageInfo[] getAttestationPackageInfos()
  {
    return mAttestationPackageInfos;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeTypedArray(mAttestationPackageInfos, paramInt);
  }
}
