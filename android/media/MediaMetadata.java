package android.media;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public final class MediaMetadata
  implements Parcelable
{
  public static final Parcelable.Creator<MediaMetadata> CREATOR = new Parcelable.Creator()
  {
    public MediaMetadata createFromParcel(Parcel paramAnonymousParcel)
    {
      return new MediaMetadata(paramAnonymousParcel, null);
    }
    
    public MediaMetadata[] newArray(int paramAnonymousInt)
    {
      return new MediaMetadata[paramAnonymousInt];
    }
  };
  private static final SparseArray<String> EDITOR_KEY_MAPPING;
  private static final ArrayMap<String, Integer> METADATA_KEYS_TYPE;
  public static final String METADATA_KEY_ALBUM = "android.media.metadata.ALBUM";
  public static final String METADATA_KEY_ALBUM_ART = "android.media.metadata.ALBUM_ART";
  public static final String METADATA_KEY_ALBUM_ARTIST = "android.media.metadata.ALBUM_ARTIST";
  public static final String METADATA_KEY_ALBUM_ART_URI = "android.media.metadata.ALBUM_ART_URI";
  public static final String METADATA_KEY_ART = "android.media.metadata.ART";
  public static final String METADATA_KEY_ARTIST = "android.media.metadata.ARTIST";
  public static final String METADATA_KEY_ART_URI = "android.media.metadata.ART_URI";
  public static final String METADATA_KEY_AUTHOR = "android.media.metadata.AUTHOR";
  public static final String METADATA_KEY_BT_FOLDER_TYPE = "android.media.metadata.BT_FOLDER_TYPE";
  public static final String METADATA_KEY_COMPILATION = "android.media.metadata.COMPILATION";
  public static final String METADATA_KEY_COMPOSER = "android.media.metadata.COMPOSER";
  public static final String METADATA_KEY_DATE = "android.media.metadata.DATE";
  public static final String METADATA_KEY_DISC_NUMBER = "android.media.metadata.DISC_NUMBER";
  public static final String METADATA_KEY_DISPLAY_DESCRIPTION = "android.media.metadata.DISPLAY_DESCRIPTION";
  public static final String METADATA_KEY_DISPLAY_ICON = "android.media.metadata.DISPLAY_ICON";
  public static final String METADATA_KEY_DISPLAY_ICON_URI = "android.media.metadata.DISPLAY_ICON_URI";
  public static final String METADATA_KEY_DISPLAY_SUBTITLE = "android.media.metadata.DISPLAY_SUBTITLE";
  public static final String METADATA_KEY_DISPLAY_TITLE = "android.media.metadata.DISPLAY_TITLE";
  public static final String METADATA_KEY_DURATION = "android.media.metadata.DURATION";
  public static final String METADATA_KEY_GENRE = "android.media.metadata.GENRE";
  public static final String METADATA_KEY_MEDIA_ID = "android.media.metadata.MEDIA_ID";
  public static final String METADATA_KEY_MEDIA_URI = "android.media.metadata.MEDIA_URI";
  public static final String METADATA_KEY_NUM_TRACKS = "android.media.metadata.NUM_TRACKS";
  public static final String METADATA_KEY_RATING = "android.media.metadata.RATING";
  public static final String METADATA_KEY_TITLE = "android.media.metadata.TITLE";
  public static final String METADATA_KEY_TRACK_NUMBER = "android.media.metadata.TRACK_NUMBER";
  public static final String METADATA_KEY_USER_RATING = "android.media.metadata.USER_RATING";
  public static final String METADATA_KEY_WRITER = "android.media.metadata.WRITER";
  public static final String METADATA_KEY_YEAR = "android.media.metadata.YEAR";
  private static final int METADATA_TYPE_BITMAP = 2;
  private static final int METADATA_TYPE_INVALID = -1;
  private static final int METADATA_TYPE_LONG = 0;
  private static final int METADATA_TYPE_RATING = 3;
  private static final int METADATA_TYPE_TEXT = 1;
  private static final String[] PREFERRED_BITMAP_ORDER;
  private static final String[] PREFERRED_DESCRIPTION_ORDER = { "android.media.metadata.TITLE", "android.media.metadata.ARTIST", "android.media.metadata.ALBUM", "android.media.metadata.ALBUM_ARTIST", "android.media.metadata.WRITER", "android.media.metadata.AUTHOR", "android.media.metadata.COMPOSER" };
  private static final String[] PREFERRED_URI_ORDER;
  private static final String TAG = "MediaMetadata";
  private final Bundle mBundle;
  private MediaDescription mDescription;
  
  static
  {
    PREFERRED_BITMAP_ORDER = new String[] { "android.media.metadata.DISPLAY_ICON", "android.media.metadata.ART", "android.media.metadata.ALBUM_ART" };
    PREFERRED_URI_ORDER = new String[] { "android.media.metadata.DISPLAY_ICON_URI", "android.media.metadata.ART_URI", "android.media.metadata.ALBUM_ART_URI" };
    METADATA_KEYS_TYPE = new ArrayMap();
    METADATA_KEYS_TYPE.put("android.media.metadata.TITLE", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.ARTIST", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.DURATION", Integer.valueOf(0));
    METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.AUTHOR", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.WRITER", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.COMPOSER", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.COMPILATION", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.DATE", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.YEAR", Integer.valueOf(0));
    METADATA_KEYS_TYPE.put("android.media.metadata.GENRE", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.TRACK_NUMBER", Integer.valueOf(0));
    METADATA_KEYS_TYPE.put("android.media.metadata.NUM_TRACKS", Integer.valueOf(0));
    METADATA_KEYS_TYPE.put("android.media.metadata.DISC_NUMBER", Integer.valueOf(0));
    METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ARTIST", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.ART", Integer.valueOf(2));
    METADATA_KEYS_TYPE.put("android.media.metadata.ART_URI", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ART", Integer.valueOf(2));
    METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ART_URI", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.USER_RATING", Integer.valueOf(3));
    METADATA_KEYS_TYPE.put("android.media.metadata.RATING", Integer.valueOf(3));
    METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_TITLE", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_SUBTITLE", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_DESCRIPTION", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_ICON", Integer.valueOf(2));
    METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_ICON_URI", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.BT_FOLDER_TYPE", Integer.valueOf(0));
    METADATA_KEYS_TYPE.put("android.media.metadata.MEDIA_ID", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.MEDIA_URI", Integer.valueOf(1));
    EDITOR_KEY_MAPPING = new SparseArray();
    EDITOR_KEY_MAPPING.put(100, "android.media.metadata.ART");
    EDITOR_KEY_MAPPING.put(101, "android.media.metadata.RATING");
    EDITOR_KEY_MAPPING.put(268435457, "android.media.metadata.USER_RATING");
    EDITOR_KEY_MAPPING.put(1, "android.media.metadata.ALBUM");
    EDITOR_KEY_MAPPING.put(13, "android.media.metadata.ALBUM_ARTIST");
    EDITOR_KEY_MAPPING.put(2, "android.media.metadata.ARTIST");
    EDITOR_KEY_MAPPING.put(3, "android.media.metadata.AUTHOR");
    EDITOR_KEY_MAPPING.put(0, "android.media.metadata.TRACK_NUMBER");
    EDITOR_KEY_MAPPING.put(4, "android.media.metadata.COMPOSER");
    EDITOR_KEY_MAPPING.put(15, "android.media.metadata.COMPILATION");
    EDITOR_KEY_MAPPING.put(5, "android.media.metadata.DATE");
    EDITOR_KEY_MAPPING.put(14, "android.media.metadata.DISC_NUMBER");
    EDITOR_KEY_MAPPING.put(9, "android.media.metadata.DURATION");
    EDITOR_KEY_MAPPING.put(6, "android.media.metadata.GENRE");
    EDITOR_KEY_MAPPING.put(10, "android.media.metadata.NUM_TRACKS");
    EDITOR_KEY_MAPPING.put(7, "android.media.metadata.TITLE");
    EDITOR_KEY_MAPPING.put(11, "android.media.metadata.WRITER");
    EDITOR_KEY_MAPPING.put(8, "android.media.metadata.YEAR");
  }
  
  private MediaMetadata(Bundle paramBundle)
  {
    mBundle = new Bundle(paramBundle);
  }
  
  private MediaMetadata(Parcel paramParcel)
  {
    mBundle = Bundle.setDefusable(paramParcel.readBundle(), true);
  }
  
  public static String getKeyFromMetadataEditorKey(int paramInt)
  {
    return (String)EDITOR_KEY_MAPPING.get(paramInt, null);
  }
  
  public boolean containsKey(String paramString)
  {
    return mBundle.containsKey(paramString);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == this) {
      return true;
    }
    if (!(paramObject instanceof MediaMetadata)) {
      return false;
    }
    MediaMetadata localMediaMetadata = (MediaMetadata)paramObject;
    for (int i = 0; i < METADATA_KEYS_TYPE.size(); i++)
    {
      paramObject = (String)METADATA_KEYS_TYPE.keyAt(i);
      switch (((Integer)METADATA_KEYS_TYPE.valueAt(i)).intValue())
      {
      default: 
        break;
      case 1: 
        if (!Objects.equals(getString(paramObject), localMediaMetadata.getString(paramObject))) {
          return false;
        }
        break;
      case 0: 
        if (getLong(paramObject) != localMediaMetadata.getLong(paramObject)) {
          return false;
        }
        break;
      }
    }
    return true;
  }
  
  public Bitmap getBitmap(String paramString)
  {
    Object localObject = null;
    try
    {
      paramString = (Bitmap)mBundle.getParcelable(paramString);
    }
    catch (Exception paramString)
    {
      Log.w("MediaMetadata", "Failed to retrieve a key as Bitmap.", paramString);
      paramString = localObject;
    }
    return paramString;
  }
  
  public MediaDescription getDescription()
  {
    if (mDescription != null) {
      return mDescription;
    }
    String str = getString("android.media.metadata.MEDIA_ID");
    CharSequence[] arrayOfCharSequence = new CharSequence[3];
    Object localObject1 = null;
    Uri localUri = null;
    Object localObject2 = getText("android.media.metadata.DISPLAY_TITLE");
    if (!TextUtils.isEmpty((CharSequence)localObject2))
    {
      arrayOfCharSequence[0] = localObject2;
      arrayOfCharSequence[1] = getText("android.media.metadata.DISPLAY_SUBTITLE");
      arrayOfCharSequence[2] = getText("android.media.metadata.DISPLAY_DESCRIPTION");
    }
    else
    {
      i = 0;
      int j = 0;
      while ((i < arrayOfCharSequence.length) && (j < PREFERRED_DESCRIPTION_ORDER.length))
      {
        localObject2 = getText(PREFERRED_DESCRIPTION_ORDER[j]);
        int k = i;
        if (!TextUtils.isEmpty((CharSequence)localObject2))
        {
          arrayOfCharSequence[i] = localObject2;
          k = i + 1;
        }
        j++;
        i = k;
      }
    }
    for (int i = 0;; i++)
    {
      localObject2 = localObject1;
      if (i >= PREFERRED_BITMAP_ORDER.length) {
        break;
      }
      localObject2 = getBitmap(PREFERRED_BITMAP_ORDER[i]);
      if (localObject2 != null) {
        break;
      }
    }
    for (i = 0;; i++)
    {
      localObject1 = localUri;
      if (i >= PREFERRED_URI_ORDER.length) {
        break;
      }
      localObject1 = getString(PREFERRED_URI_ORDER[i]);
      if (!TextUtils.isEmpty((CharSequence)localObject1))
      {
        localObject1 = Uri.parse((String)localObject1);
        break;
      }
    }
    localUri = null;
    Object localObject3 = getString("android.media.metadata.MEDIA_URI");
    if (!TextUtils.isEmpty((CharSequence)localObject3)) {
      localUri = Uri.parse((String)localObject3);
    }
    localObject3 = new MediaDescription.Builder();
    ((MediaDescription.Builder)localObject3).setMediaId(str);
    ((MediaDescription.Builder)localObject3).setTitle(arrayOfCharSequence[0]);
    ((MediaDescription.Builder)localObject3).setSubtitle(arrayOfCharSequence[1]);
    ((MediaDescription.Builder)localObject3).setDescription(arrayOfCharSequence[2]);
    ((MediaDescription.Builder)localObject3).setIconBitmap((Bitmap)localObject2);
    ((MediaDescription.Builder)localObject3).setIconUri((Uri)localObject1);
    ((MediaDescription.Builder)localObject3).setMediaUri(localUri);
    if (mBundle.containsKey("android.media.metadata.BT_FOLDER_TYPE"))
    {
      localObject2 = new Bundle();
      ((Bundle)localObject2).putLong("android.media.extra.BT_FOLDER_TYPE", getLong("android.media.metadata.BT_FOLDER_TYPE"));
      ((MediaDescription.Builder)localObject3).setExtras((Bundle)localObject2);
    }
    mDescription = ((MediaDescription.Builder)localObject3).build();
    return mDescription;
  }
  
  public long getLong(String paramString)
  {
    return mBundle.getLong(paramString, 0L);
  }
  
  public Rating getRating(String paramString)
  {
    Object localObject = null;
    try
    {
      paramString = (Rating)mBundle.getParcelable(paramString);
    }
    catch (Exception paramString)
    {
      Log.w("MediaMetadata", "Failed to retrieve a key as Rating.", paramString);
      paramString = localObject;
    }
    return paramString;
  }
  
  public String getString(String paramString)
  {
    paramString = getText(paramString);
    if (paramString != null) {
      return paramString.toString();
    }
    return null;
  }
  
  public CharSequence getText(String paramString)
  {
    return mBundle.getCharSequence(paramString);
  }
  
  public int hashCode()
  {
    int i = 17;
    for (int j = 0; j < METADATA_KEYS_TYPE.size(); j++)
    {
      String str = (String)METADATA_KEYS_TYPE.keyAt(j);
      switch (((Integer)METADATA_KEYS_TYPE.valueAt(j)).intValue())
      {
      default: 
        break;
      case 1: 
        i = 31 * i + Objects.hash(new Object[] { getString(str) });
        break;
      case 0: 
        i = 31 * i + Long.hashCode(getLong(str));
      }
    }
    return i;
  }
  
  public Set<String> keySet()
  {
    return mBundle.keySet();
  }
  
  public int size()
  {
    return mBundle.size();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeBundle(mBundle);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface BitmapKey {}
  
  public static final class Builder
  {
    private final Bundle mBundle;
    
    public Builder()
    {
      mBundle = new Bundle();
    }
    
    public Builder(MediaMetadata paramMediaMetadata)
    {
      mBundle = new Bundle(mBundle);
    }
    
    public Builder(MediaMetadata paramMediaMetadata, int paramInt)
    {
      this(paramMediaMetadata);
      paramMediaMetadata = mBundle.keySet().iterator();
      while (paramMediaMetadata.hasNext())
      {
        String str = (String)paramMediaMetadata.next();
        Object localObject = mBundle.get(str);
        if ((localObject != null) && ((localObject instanceof Bitmap)))
        {
          localObject = (Bitmap)localObject;
          if ((((Bitmap)localObject).getHeight() > paramInt) || (((Bitmap)localObject).getWidth() > paramInt)) {
            putBitmap(str, scaleBitmap((Bitmap)localObject, paramInt));
          }
        }
      }
    }
    
    private Bitmap scaleBitmap(Bitmap paramBitmap, int paramInt)
    {
      float f = paramInt;
      f = Math.min(f / paramBitmap.getWidth(), f / paramBitmap.getHeight());
      paramInt = (int)(paramBitmap.getHeight() * f);
      return Bitmap.createScaledBitmap(paramBitmap, (int)(paramBitmap.getWidth() * f), paramInt, true);
    }
    
    public MediaMetadata build()
    {
      return new MediaMetadata(mBundle, null);
    }
    
    public Builder putBitmap(String paramString, Bitmap paramBitmap)
    {
      if ((MediaMetadata.METADATA_KEYS_TYPE.containsKey(paramString)) && (((Integer)MediaMetadata.METADATA_KEYS_TYPE.get(paramString)).intValue() != 2))
      {
        paramBitmap = new StringBuilder();
        paramBitmap.append("The ");
        paramBitmap.append(paramString);
        paramBitmap.append(" key cannot be used to put a Bitmap");
        throw new IllegalArgumentException(paramBitmap.toString());
      }
      mBundle.putParcelable(paramString, paramBitmap);
      return this;
    }
    
    public Builder putLong(String paramString, long paramLong)
    {
      if ((MediaMetadata.METADATA_KEYS_TYPE.containsKey(paramString)) && (((Integer)MediaMetadata.METADATA_KEYS_TYPE.get(paramString)).intValue() != 0))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("The ");
        localStringBuilder.append(paramString);
        localStringBuilder.append(" key cannot be used to put a long");
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      mBundle.putLong(paramString, paramLong);
      return this;
    }
    
    public Builder putRating(String paramString, Rating paramRating)
    {
      if ((MediaMetadata.METADATA_KEYS_TYPE.containsKey(paramString)) && (((Integer)MediaMetadata.METADATA_KEYS_TYPE.get(paramString)).intValue() != 3))
      {
        paramRating = new StringBuilder();
        paramRating.append("The ");
        paramRating.append(paramString);
        paramRating.append(" key cannot be used to put a Rating");
        throw new IllegalArgumentException(paramRating.toString());
      }
      mBundle.putParcelable(paramString, paramRating);
      return this;
    }
    
    public Builder putString(String paramString1, String paramString2)
    {
      if ((MediaMetadata.METADATA_KEYS_TYPE.containsKey(paramString1)) && (((Integer)MediaMetadata.METADATA_KEYS_TYPE.get(paramString1)).intValue() != 1))
      {
        paramString2 = new StringBuilder();
        paramString2.append("The ");
        paramString2.append(paramString1);
        paramString2.append(" key cannot be used to put a String");
        throw new IllegalArgumentException(paramString2.toString());
      }
      mBundle.putCharSequence(paramString1, paramString2);
      return this;
    }
    
    public Builder putText(String paramString, CharSequence paramCharSequence)
    {
      if ((MediaMetadata.METADATA_KEYS_TYPE.containsKey(paramString)) && (((Integer)MediaMetadata.METADATA_KEYS_TYPE.get(paramString)).intValue() != 1))
      {
        paramCharSequence = new StringBuilder();
        paramCharSequence.append("The ");
        paramCharSequence.append(paramString);
        paramCharSequence.append(" key cannot be used to put a CharSequence");
        throw new IllegalArgumentException(paramCharSequence.toString());
      }
      mBundle.putCharSequence(paramString, paramCharSequence);
      return this;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface LongKey {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface RatingKey {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface TextKey {}
}
