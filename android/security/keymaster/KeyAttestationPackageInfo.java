package android.security.keymaster;

import android.content.pm.Signature;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class KeyAttestationPackageInfo
  implements Parcelable
{
  public static final Parcelable.Creator<KeyAttestationPackageInfo> CREATOR = new Parcelable.Creator()
  {
    public KeyAttestationPackageInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new KeyAttestationPackageInfo(paramAnonymousParcel, null);
    }
    
    public KeyAttestationPackageInfo[] newArray(int paramAnonymousInt)
    {
      return new KeyAttestationPackageInfo[paramAnonymousInt];
    }
  };
  private final String mPackageName;
  private final Signature[] mPackageSignatures;
  private final long mPackageVersionCode;
  
  private KeyAttestationPackageInfo(Parcel paramParcel)
  {
    mPackageName = paramParcel.readString();
    mPackageVersionCode = paramParcel.readLong();
    mPackageSignatures = ((Signature[])paramParcel.createTypedArray(Signature.CREATOR));
  }
  
  public KeyAttestationPackageInfo(String paramString, long paramLong, Signature[] paramArrayOfSignature)
  {
    mPackageName = paramString;
    mPackageVersionCode = paramLong;
    mPackageSignatures = paramArrayOfSignature;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getPackageName()
  {
    return mPackageName;
  }
  
  public Signature[] getPackageSignatures()
  {
    return mPackageSignatures;
  }
  
  public long getPackageVersionCode()
  {
    return mPackageVersionCode;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mPackageName);
    paramParcel.writeLong(mPackageVersionCode);
    paramParcel.writeTypedArray(mPackageSignatures, paramInt);
  }
}
