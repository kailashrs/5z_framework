package android.content.pm;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@Deprecated
public class VerificationParams
  implements Parcelable
{
  public static final Parcelable.Creator<VerificationParams> CREATOR = new Parcelable.Creator()
  {
    public VerificationParams createFromParcel(Parcel paramAnonymousParcel)
    {
      return new VerificationParams(paramAnonymousParcel, null);
    }
    
    public VerificationParams[] newArray(int paramAnonymousInt)
    {
      return new VerificationParams[paramAnonymousInt];
    }
  };
  public static final int NO_UID = -1;
  private static final String TO_STRING_PREFIX = "VerificationParams{";
  private int mInstallerUid;
  private final Uri mOriginatingURI;
  private final int mOriginatingUid;
  private final Uri mReferrer;
  private final Uri mVerificationURI;
  
  public VerificationParams(Uri paramUri1, Uri paramUri2, Uri paramUri3, int paramInt)
  {
    mVerificationURI = paramUri1;
    mOriginatingURI = paramUri2;
    mReferrer = paramUri3;
    mOriginatingUid = paramInt;
    mInstallerUid = -1;
  }
  
  private VerificationParams(Parcel paramParcel)
  {
    mVerificationURI = ((Uri)paramParcel.readParcelable(Uri.class.getClassLoader()));
    mOriginatingURI = ((Uri)paramParcel.readParcelable(Uri.class.getClassLoader()));
    mReferrer = ((Uri)paramParcel.readParcelable(Uri.class.getClassLoader()));
    mOriginatingUid = paramParcel.readInt();
    mInstallerUid = paramParcel.readInt();
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
    if (!(paramObject instanceof VerificationParams)) {
      return false;
    }
    paramObject = (VerificationParams)paramObject;
    if (mVerificationURI == null)
    {
      if (mVerificationURI != null) {
        return false;
      }
    }
    else if (!mVerificationURI.equals(mVerificationURI)) {
      return false;
    }
    if (mOriginatingURI == null)
    {
      if (mOriginatingURI != null) {
        return false;
      }
    }
    else if (!mOriginatingURI.equals(mOriginatingURI)) {
      return false;
    }
    if (mReferrer == null)
    {
      if (mReferrer != null) {
        return false;
      }
    }
    else if (!mReferrer.equals(mReferrer)) {
      return false;
    }
    if (mOriginatingUid != mOriginatingUid) {
      return false;
    }
    return mInstallerUid == mInstallerUid;
  }
  
  public int getInstallerUid()
  {
    return mInstallerUid;
  }
  
  public Uri getOriginatingURI()
  {
    return mOriginatingURI;
  }
  
  public int getOriginatingUid()
  {
    return mOriginatingUid;
  }
  
  public Uri getReferrer()
  {
    return mReferrer;
  }
  
  public Uri getVerificationURI()
  {
    return mVerificationURI;
  }
  
  public int hashCode()
  {
    Uri localUri = mVerificationURI;
    int i = 1;
    int j;
    if (localUri == null) {
      j = 1;
    } else {
      j = mVerificationURI.hashCode();
    }
    int k;
    if (mOriginatingURI == null) {
      k = 1;
    } else {
      k = mOriginatingURI.hashCode();
    }
    if (mReferrer != null) {
      i = mReferrer.hashCode();
    }
    return 3 + 5 * j + 7 * k + 11 * i + 13 * mOriginatingUid + 17 * mInstallerUid;
  }
  
  public void setInstallerUid(int paramInt)
  {
    mInstallerUid = paramInt;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("VerificationParams{");
    localStringBuilder.append("mVerificationURI=");
    localStringBuilder.append(mVerificationURI.toString());
    localStringBuilder.append(",mOriginatingURI=");
    localStringBuilder.append(mOriginatingURI.toString());
    localStringBuilder.append(",mReferrer=");
    localStringBuilder.append(mReferrer.toString());
    localStringBuilder.append(",mOriginatingUid=");
    localStringBuilder.append(mOriginatingUid);
    localStringBuilder.append(",mInstallerUid=");
    localStringBuilder.append(mInstallerUid);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mVerificationURI, 0);
    paramParcel.writeParcelable(mOriginatingURI, 0);
    paramParcel.writeParcelable(mReferrer, 0);
    paramParcel.writeInt(mOriginatingUid);
    paramParcel.writeInt(mInstallerUid);
  }
}
