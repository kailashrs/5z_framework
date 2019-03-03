package android.telephony.mbms;

import android.annotation.SystemApi;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public final class FileInfo
  implements Parcelable
{
  public static final Parcelable.Creator<FileInfo> CREATOR = new Parcelable.Creator()
  {
    public FileInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new FileInfo(paramAnonymousParcel, null);
    }
    
    public FileInfo[] newArray(int paramAnonymousInt)
    {
      return new FileInfo[paramAnonymousInt];
    }
  };
  private final String mimeType;
  private final Uri uri;
  
  @SystemApi
  public FileInfo(Uri paramUri, String paramString)
  {
    uri = paramUri;
    mimeType = paramString;
  }
  
  private FileInfo(Parcel paramParcel)
  {
    uri = ((Uri)paramParcel.readParcelable(null));
    mimeType = paramParcel.readString();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (FileInfo)paramObject;
      if ((!Objects.equals(uri, uri)) || (!Objects.equals(mimeType, mimeType))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public String getMimeType()
  {
    return mimeType;
  }
  
  public Uri getUri()
  {
    return uri;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { uri, mimeType });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(uri, paramInt);
    paramParcel.writeString(mimeType);
  }
}
