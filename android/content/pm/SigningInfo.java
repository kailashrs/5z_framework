package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class SigningInfo
  implements Parcelable
{
  public static final Parcelable.Creator<SigningInfo> CREATOR = new Parcelable.Creator()
  {
    public SigningInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SigningInfo(paramAnonymousParcel, null);
    }
    
    public SigningInfo[] newArray(int paramAnonymousInt)
    {
      return new SigningInfo[paramAnonymousInt];
    }
  };
  private final PackageParser.SigningDetails mSigningDetails;
  
  public SigningInfo()
  {
    mSigningDetails = PackageParser.SigningDetails.UNKNOWN;
  }
  
  public SigningInfo(PackageParser.SigningDetails paramSigningDetails)
  {
    mSigningDetails = new PackageParser.SigningDetails(paramSigningDetails);
  }
  
  public SigningInfo(SigningInfo paramSigningInfo)
  {
    mSigningDetails = new PackageParser.SigningDetails(mSigningDetails);
  }
  
  private SigningInfo(Parcel paramParcel)
  {
    mSigningDetails = ((PackageParser.SigningDetails)PackageParser.SigningDetails.CREATOR.createFromParcel(paramParcel));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Signature[] getApkContentsSigners()
  {
    return mSigningDetails.signatures;
  }
  
  public Signature[] getSigningCertificateHistory()
  {
    if (hasMultipleSigners()) {
      return null;
    }
    if (!hasPastSigningCertificates()) {
      return mSigningDetails.signatures;
    }
    return mSigningDetails.pastSigningCertificates;
  }
  
  public boolean hasMultipleSigners()
  {
    Signature[] arrayOfSignature = mSigningDetails.signatures;
    boolean bool = true;
    if ((arrayOfSignature == null) || (mSigningDetails.signatures.length <= 1)) {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasPastSigningCertificates()
  {
    boolean bool;
    if ((mSigningDetails.signatures != null) && (mSigningDetails.pastSigningCertificates != null)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    mSigningDetails.writeToParcel(paramParcel, paramInt);
  }
}
