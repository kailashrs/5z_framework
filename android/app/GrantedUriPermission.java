package android.app;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class GrantedUriPermission
  implements Parcelable
{
  public static final Parcelable.Creator<GrantedUriPermission> CREATOR = new Parcelable.Creator()
  {
    public GrantedUriPermission createFromParcel(Parcel paramAnonymousParcel)
    {
      return new GrantedUriPermission(paramAnonymousParcel, null);
    }
    
    public GrantedUriPermission[] newArray(int paramAnonymousInt)
    {
      return new GrantedUriPermission[paramAnonymousInt];
    }
  };
  public final String packageName;
  public final Uri uri;
  
  public GrantedUriPermission(Uri paramUri, String paramString)
  {
    uri = paramUri;
    packageName = paramString;
  }
  
  private GrantedUriPermission(Parcel paramParcel)
  {
    uri = ((Uri)paramParcel.readParcelable(null));
    packageName = paramParcel.readString();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(packageName);
    localStringBuilder.append(":");
    localStringBuilder.append(uri);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(uri, paramInt);
    paramParcel.writeString(packageName);
  }
}
