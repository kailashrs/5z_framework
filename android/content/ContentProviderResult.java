package android.content;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ContentProviderResult
  implements Parcelable
{
  public static final Parcelable.Creator<ContentProviderResult> CREATOR = new Parcelable.Creator()
  {
    public ContentProviderResult createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ContentProviderResult(paramAnonymousParcel);
    }
    
    public ContentProviderResult[] newArray(int paramAnonymousInt)
    {
      return new ContentProviderResult[paramAnonymousInt];
    }
  };
  public final Integer count;
  public final Uri uri;
  
  public ContentProviderResult(int paramInt)
  {
    count = Integer.valueOf(paramInt);
    uri = null;
  }
  
  public ContentProviderResult(ContentProviderResult paramContentProviderResult, int paramInt)
  {
    uri = ContentProvider.maybeAddUserId(uri, paramInt);
    count = count;
  }
  
  public ContentProviderResult(Uri paramUri)
  {
    if (paramUri != null)
    {
      uri = paramUri;
      count = null;
      return;
    }
    throw new IllegalArgumentException("uri must not be null");
  }
  
  public ContentProviderResult(Parcel paramParcel)
  {
    if (paramParcel.readInt() == 1)
    {
      count = Integer.valueOf(paramParcel.readInt());
      uri = null;
    }
    else
    {
      count = null;
      uri = ((Uri)Uri.CREATOR.createFromParcel(paramParcel));
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    if (uri != null)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("ContentProviderResult(uri=");
      localStringBuilder.append(uri.toString());
      localStringBuilder.append(")");
      return localStringBuilder.toString();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ContentProviderResult(count=");
    localStringBuilder.append(count);
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (uri == null)
    {
      paramParcel.writeInt(1);
      paramParcel.writeInt(count.intValue());
    }
    else
    {
      paramParcel.writeInt(2);
      uri.writeToParcel(paramParcel, 0);
    }
  }
}
