package android.telephony.mbms;

import android.annotation.SystemApi;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public final class UriPathPair
  implements Parcelable
{
  public static final Parcelable.Creator<UriPathPair> CREATOR = new Parcelable.Creator()
  {
    public UriPathPair createFromParcel(Parcel paramAnonymousParcel)
    {
      return new UriPathPair(paramAnonymousParcel, null);
    }
    
    public UriPathPair[] newArray(int paramAnonymousInt)
    {
      return new UriPathPair[paramAnonymousInt];
    }
  };
  private final Uri mContentUri;
  private final Uri mFilePathUri;
  
  public UriPathPair(Uri paramUri1, Uri paramUri2)
  {
    if ((paramUri1 != null) && ("file".equals(paramUri1.getScheme())))
    {
      if ((paramUri2 != null) && ("content".equals(paramUri2.getScheme())))
      {
        mFilePathUri = paramUri1;
        mContentUri = paramUri2;
        return;
      }
      throw new IllegalArgumentException("Content URI must have content scheme");
    }
    throw new IllegalArgumentException("File URI must have file scheme");
  }
  
  private UriPathPair(Parcel paramParcel)
  {
    mFilePathUri = ((Uri)paramParcel.readParcelable(Uri.class.getClassLoader()));
    mContentUri = ((Uri)paramParcel.readParcelable(Uri.class.getClassLoader()));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Uri getContentUri()
  {
    return mContentUri;
  }
  
  public Uri getFilePathUri()
  {
    return mFilePathUri;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mFilePathUri, paramInt);
    paramParcel.writeParcelable(mContentUri, paramInt);
  }
}
