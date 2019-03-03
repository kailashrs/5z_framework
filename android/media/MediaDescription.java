package android.media;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class MediaDescription
  implements Parcelable
{
  public static final long BT_FOLDER_TYPE_ALBUMS = 2L;
  public static final long BT_FOLDER_TYPE_ARTISTS = 3L;
  public static final long BT_FOLDER_TYPE_GENRES = 4L;
  public static final long BT_FOLDER_TYPE_MIXED = 0L;
  public static final long BT_FOLDER_TYPE_PLAYLISTS = 5L;
  public static final long BT_FOLDER_TYPE_TITLES = 1L;
  public static final long BT_FOLDER_TYPE_YEARS = 6L;
  public static final Parcelable.Creator<MediaDescription> CREATOR = new Parcelable.Creator()
  {
    public MediaDescription createFromParcel(Parcel paramAnonymousParcel)
    {
      return new MediaDescription(paramAnonymousParcel, null);
    }
    
    public MediaDescription[] newArray(int paramAnonymousInt)
    {
      return new MediaDescription[paramAnonymousInt];
    }
  };
  public static final String EXTRA_BT_FOLDER_TYPE = "android.media.extra.BT_FOLDER_TYPE";
  private final CharSequence mDescription;
  private final Bundle mExtras;
  private final Bitmap mIcon;
  private final Uri mIconUri;
  private final String mMediaId;
  private final Uri mMediaUri;
  private final CharSequence mSubtitle;
  private final CharSequence mTitle;
  
  private MediaDescription(Parcel paramParcel)
  {
    mMediaId = paramParcel.readString();
    mTitle = paramParcel.readCharSequence();
    mSubtitle = paramParcel.readCharSequence();
    mDescription = paramParcel.readCharSequence();
    mIcon = ((Bitmap)paramParcel.readParcelable(null));
    mIconUri = ((Uri)paramParcel.readParcelable(null));
    mExtras = paramParcel.readBundle();
    mMediaUri = ((Uri)paramParcel.readParcelable(null));
  }
  
  private MediaDescription(String paramString, CharSequence paramCharSequence1, CharSequence paramCharSequence2, CharSequence paramCharSequence3, Bitmap paramBitmap, Uri paramUri1, Bundle paramBundle, Uri paramUri2)
  {
    mMediaId = paramString;
    mTitle = paramCharSequence1;
    mSubtitle = paramCharSequence2;
    mDescription = paramCharSequence3;
    mIcon = paramBitmap;
    mIconUri = paramUri1;
    mExtras = paramBundle;
    mMediaUri = paramUri2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == null) {
      return false;
    }
    if (!(paramObject instanceof MediaDescription)) {
      return false;
    }
    paramObject = (MediaDescription)paramObject;
    if (!String.valueOf(mTitle).equals(String.valueOf(mTitle))) {
      return false;
    }
    if (!String.valueOf(mSubtitle).equals(String.valueOf(mSubtitle))) {
      return false;
    }
    return String.valueOf(mDescription).equals(String.valueOf(mDescription));
  }
  
  public CharSequence getDescription()
  {
    return mDescription;
  }
  
  public Bundle getExtras()
  {
    return mExtras;
  }
  
  public Bitmap getIconBitmap()
  {
    return mIcon;
  }
  
  public Uri getIconUri()
  {
    return mIconUri;
  }
  
  public String getMediaId()
  {
    return mMediaId;
  }
  
  public Uri getMediaUri()
  {
    return mMediaUri;
  }
  
  public CharSequence getSubtitle()
  {
    return mSubtitle;
  }
  
  public CharSequence getTitle()
  {
    return mTitle;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(mTitle);
    localStringBuilder.append(", ");
    localStringBuilder.append(mSubtitle);
    localStringBuilder.append(", ");
    localStringBuilder.append(mDescription);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mMediaId);
    paramParcel.writeCharSequence(mTitle);
    paramParcel.writeCharSequence(mSubtitle);
    paramParcel.writeCharSequence(mDescription);
    paramParcel.writeParcelable(mIcon, paramInt);
    paramParcel.writeParcelable(mIconUri, paramInt);
    paramParcel.writeBundle(mExtras);
    paramParcel.writeParcelable(mMediaUri, paramInt);
  }
  
  public static class Builder
  {
    private CharSequence mDescription;
    private Bundle mExtras;
    private Bitmap mIcon;
    private Uri mIconUri;
    private String mMediaId;
    private Uri mMediaUri;
    private CharSequence mSubtitle;
    private CharSequence mTitle;
    
    public Builder() {}
    
    public MediaDescription build()
    {
      return new MediaDescription(mMediaId, mTitle, mSubtitle, mDescription, mIcon, mIconUri, mExtras, mMediaUri, null);
    }
    
    public Builder setDescription(CharSequence paramCharSequence)
    {
      mDescription = paramCharSequence;
      return this;
    }
    
    public Builder setExtras(Bundle paramBundle)
    {
      mExtras = paramBundle;
      return this;
    }
    
    public Builder setIconBitmap(Bitmap paramBitmap)
    {
      mIcon = paramBitmap;
      return this;
    }
    
    public Builder setIconUri(Uri paramUri)
    {
      mIconUri = paramUri;
      return this;
    }
    
    public Builder setMediaId(String paramString)
    {
      mMediaId = paramString;
      return this;
    }
    
    public Builder setMediaUri(Uri paramUri)
    {
      mMediaUri = paramUri;
      return this;
    }
    
    public Builder setSubtitle(CharSequence paramCharSequence)
    {
      mSubtitle = paramCharSequence;
      return this;
    }
    
    public Builder setTitle(CharSequence paramCharSequence)
    {
      mTitle = paramCharSequence;
      return this;
    }
  }
}
