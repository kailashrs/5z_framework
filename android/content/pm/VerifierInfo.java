package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.security.PublicKey;

public class VerifierInfo
  implements Parcelable
{
  public static final Parcelable.Creator<VerifierInfo> CREATOR = new Parcelable.Creator()
  {
    public VerifierInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new VerifierInfo(paramAnonymousParcel, null);
    }
    
    public VerifierInfo[] newArray(int paramAnonymousInt)
    {
      return new VerifierInfo[paramAnonymousInt];
    }
  };
  public final String packageName;
  public final PublicKey publicKey;
  
  private VerifierInfo(Parcel paramParcel)
  {
    packageName = paramParcel.readString();
    publicKey = ((PublicKey)paramParcel.readSerializable());
  }
  
  public VerifierInfo(String paramString, PublicKey paramPublicKey)
  {
    if ((paramString != null) && (paramString.length() != 0))
    {
      if (paramPublicKey != null)
      {
        packageName = paramString;
        publicKey = paramPublicKey;
        return;
      }
      throw new IllegalArgumentException("publicKey must not be null");
    }
    throw new IllegalArgumentException("packageName must not be null or empty");
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(packageName);
    paramParcel.writeSerializable(publicKey);
  }
}
