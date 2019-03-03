package android.media;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseIntArray;

@Deprecated
public abstract class MediaMetadataEditor
{
  public static final int BITMAP_KEY_ARTWORK = 100;
  public static final int KEY_EDITABLE_MASK = 536870911;
  protected static final SparseIntArray METADATA_KEYS_TYPE = new SparseIntArray(17);
  protected static final int METADATA_TYPE_BITMAP = 2;
  protected static final int METADATA_TYPE_INVALID = -1;
  protected static final int METADATA_TYPE_LONG = 0;
  protected static final int METADATA_TYPE_RATING = 3;
  protected static final int METADATA_TYPE_STRING = 1;
  public static final int RATING_KEY_BY_OTHERS = 101;
  public static final int RATING_KEY_BY_USER = 268435457;
  private static final String TAG = "MediaMetadataEditor";
  protected boolean mApplied = false;
  protected boolean mArtworkChanged = false;
  protected long mEditableKeys;
  protected Bitmap mEditorArtwork;
  protected Bundle mEditorMetadata;
  protected MediaMetadata.Builder mMetadataBuilder;
  protected boolean mMetadataChanged = false;
  
  static
  {
    METADATA_KEYS_TYPE.put(0, 0);
    METADATA_KEYS_TYPE.put(14, 0);
    METADATA_KEYS_TYPE.put(9, 0);
    METADATA_KEYS_TYPE.put(8, 0);
    METADATA_KEYS_TYPE.put(1, 1);
    METADATA_KEYS_TYPE.put(13, 1);
    METADATA_KEYS_TYPE.put(7, 1);
    METADATA_KEYS_TYPE.put(2, 1);
    METADATA_KEYS_TYPE.put(3, 1);
    METADATA_KEYS_TYPE.put(15, 1);
    METADATA_KEYS_TYPE.put(4, 1);
    METADATA_KEYS_TYPE.put(5, 1);
    METADATA_KEYS_TYPE.put(6, 1);
    METADATA_KEYS_TYPE.put(11, 1);
    METADATA_KEYS_TYPE.put(100, 2);
    METADATA_KEYS_TYPE.put(101, 3);
    METADATA_KEYS_TYPE.put(268435457, 3);
  }
  
  protected MediaMetadataEditor() {}
  
  public void addEditableKey(int paramInt)
  {
    try
    {
      if (mApplied)
      {
        Log.e("MediaMetadataEditor", "Can't change editable keys of a previously applied MetadataEditor");
        return;
      }
      if (paramInt == 268435457)
      {
        mEditableKeys |= 0x1FFFFFFF & paramInt;
        mMetadataChanged = true;
      }
      else
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Metadata key ");
        localStringBuilder.append(paramInt);
        localStringBuilder.append(" cannot be edited");
        Log.e("MediaMetadataEditor", localStringBuilder.toString());
      }
      return;
    }
    finally {}
  }
  
  public abstract void apply();
  
  public void clear()
  {
    try
    {
      if (mApplied)
      {
        Log.e("MediaMetadataEditor", "Can't clear a previously applied MediaMetadataEditor");
        return;
      }
      mEditorMetadata.clear();
      mEditorArtwork = null;
      MediaMetadata.Builder localBuilder = new android/media/MediaMetadata$Builder;
      localBuilder.<init>();
      mMetadataBuilder = localBuilder;
      return;
    }
    finally {}
  }
  
  public Bitmap getBitmap(int paramInt, Bitmap paramBitmap)
    throws IllegalArgumentException
  {
    if (paramInt == 100) {}
    try
    {
      if (mEditorArtwork != null) {
        paramBitmap = mEditorArtwork;
      }
      return paramBitmap;
    }
    finally {}
    IllegalArgumentException localIllegalArgumentException = new java/lang/IllegalArgumentException;
    paramBitmap = new java/lang/StringBuilder;
    paramBitmap.<init>();
    paramBitmap.append("Invalid type 'Bitmap' for key ");
    paramBitmap.append(paramInt);
    localIllegalArgumentException.<init>(paramBitmap.toString());
    throw localIllegalArgumentException;
  }
  
  public int[] getEditableKeys()
  {
    try
    {
      if (mEditableKeys == 268435457L) {
        return new int[] { 268435457 };
      }
      return null;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public long getLong(int paramInt, long paramLong)
    throws IllegalArgumentException
  {
    try
    {
      if (METADATA_KEYS_TYPE.get(paramInt, -1) == 0)
      {
        paramLong = mEditorMetadata.getLong(String.valueOf(paramInt), paramLong);
        return paramLong;
      }
      IllegalArgumentException localIllegalArgumentException = new java/lang/IllegalArgumentException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Invalid type 'long' for key ");
      localStringBuilder.append(paramInt);
      localIllegalArgumentException.<init>(localStringBuilder.toString());
      throw localIllegalArgumentException;
    }
    finally {}
  }
  
  public Object getObject(int paramInt, Object paramObject)
    throws IllegalArgumentException
  {
    try
    {
      switch (METADATA_KEYS_TYPE.get(paramInt, -1))
      {
      default: 
        break;
      case 3: 
        if (mEditorMetadata.containsKey(String.valueOf(paramInt)))
        {
          paramObject = mEditorMetadata.getParcelable(String.valueOf(paramInt));
          return paramObject;
        }
        return paramObject;
      case 2: 
        if (paramInt == 100)
        {
          if (mEditorArtwork != null) {
            paramObject = mEditorArtwork;
          }
          return paramObject;
        }
        break;
      case 1: 
        if (mEditorMetadata.containsKey(String.valueOf(paramInt)))
        {
          paramObject = mEditorMetadata.getString(String.valueOf(paramInt));
          return paramObject;
        }
        return paramObject;
      case 0: 
        if (mEditorMetadata.containsKey(String.valueOf(paramInt)))
        {
          long l = mEditorMetadata.getLong(String.valueOf(paramInt));
          return Long.valueOf(l);
        }
        return paramObject;
      }
      IllegalArgumentException localIllegalArgumentException = new java/lang/IllegalArgumentException;
      paramObject = new java/lang/StringBuilder;
      paramObject.<init>();
      paramObject.append("Invalid key ");
      paramObject.append(paramInt);
      localIllegalArgumentException.<init>(paramObject.toString());
      throw localIllegalArgumentException;
    }
    finally {}
  }
  
  public String getString(int paramInt, String paramString)
    throws IllegalArgumentException
  {
    try
    {
      if (METADATA_KEYS_TYPE.get(paramInt, -1) == 1)
      {
        paramString = mEditorMetadata.getString(String.valueOf(paramInt), paramString);
        return paramString;
      }
      IllegalArgumentException localIllegalArgumentException = new java/lang/IllegalArgumentException;
      paramString = new java/lang/StringBuilder;
      paramString.<init>();
      paramString.append("Invalid type 'String' for key ");
      paramString.append(paramInt);
      localIllegalArgumentException.<init>(paramString.toString());
      throw localIllegalArgumentException;
    }
    finally {}
  }
  
  public MediaMetadataEditor putBitmap(int paramInt, Bitmap paramBitmap)
    throws IllegalArgumentException
  {
    try
    {
      if (mApplied)
      {
        Log.e("MediaMetadataEditor", "Can't edit a previously applied MediaMetadataEditor");
        return this;
      }
      if (paramInt == 100)
      {
        mEditorArtwork = paramBitmap;
        mArtworkChanged = true;
        return this;
      }
      paramBitmap = new java/lang/IllegalArgumentException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Invalid type 'Bitmap' for key ");
      localStringBuilder.append(paramInt);
      paramBitmap.<init>(localStringBuilder.toString());
      throw paramBitmap;
    }
    finally {}
  }
  
  public MediaMetadataEditor putLong(int paramInt, long paramLong)
    throws IllegalArgumentException
  {
    try
    {
      if (mApplied)
      {
        Log.e("MediaMetadataEditor", "Can't edit a previously applied MediaMetadataEditor");
        return this;
      }
      if (METADATA_KEYS_TYPE.get(paramInt, -1) == 0)
      {
        mEditorMetadata.putLong(String.valueOf(paramInt), paramLong);
        mMetadataChanged = true;
        return this;
      }
      IllegalArgumentException localIllegalArgumentException = new java/lang/IllegalArgumentException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Invalid type 'long' for key ");
      localStringBuilder.append(paramInt);
      localIllegalArgumentException.<init>(localStringBuilder.toString());
      throw localIllegalArgumentException;
    }
    finally {}
  }
  
  public MediaMetadataEditor putObject(int paramInt, Object paramObject)
    throws IllegalArgumentException
  {
    try
    {
      if (mApplied)
      {
        Log.e("MediaMetadataEditor", "Can't edit a previously applied MediaMetadataEditor");
        return this;
      }
      switch (METADATA_KEYS_TYPE.get(paramInt, -1))
      {
      default: 
        paramObject = new java/lang/IllegalArgumentException;
        break;
      case 3: 
        mEditorMetadata.putParcelable(String.valueOf(paramInt), (Parcelable)paramObject);
        mMetadataChanged = true;
        return this;
      case 2: 
        if ((paramObject != null) && (!(paramObject instanceof Bitmap)))
        {
          localObject = new java/lang/IllegalArgumentException;
          paramObject = new java/lang/StringBuilder;
          paramObject.<init>();
          paramObject.append("Not a Bitmap for key ");
          paramObject.append(paramInt);
          ((IllegalArgumentException)localObject).<init>(paramObject.toString());
          throw ((Throwable)localObject);
        }
        paramObject = putBitmap(paramInt, (Bitmap)paramObject);
        return paramObject;
      case 1: 
        if ((paramObject != null) && (!(paramObject instanceof String)))
        {
          paramObject = new java/lang/IllegalArgumentException;
          localObject = new java/lang/StringBuilder;
          ((StringBuilder)localObject).<init>();
          ((StringBuilder)localObject).append("Not a String for key ");
          ((StringBuilder)localObject).append(paramInt);
          paramObject.<init>(((StringBuilder)localObject).toString());
          throw paramObject;
        }
        paramObject = putString(paramInt, (String)paramObject);
        return paramObject;
      case 0: 
        if ((paramObject instanceof Long))
        {
          paramObject = putLong(paramInt, ((Long)paramObject).longValue());
          return paramObject;
        }
        paramObject = new java/lang/IllegalArgumentException;
        localObject = new java/lang/StringBuilder;
        ((StringBuilder)localObject).<init>();
        ((StringBuilder)localObject).append("Not a non-null Long for key ");
        ((StringBuilder)localObject).append(paramInt);
        paramObject.<init>(((StringBuilder)localObject).toString());
        throw paramObject;
      }
      Object localObject = new java/lang/StringBuilder;
      ((StringBuilder)localObject).<init>();
      ((StringBuilder)localObject).append("Invalid key ");
      ((StringBuilder)localObject).append(paramInt);
      paramObject.<init>(((StringBuilder)localObject).toString());
      throw paramObject;
    }
    finally {}
  }
  
  public MediaMetadataEditor putString(int paramInt, String paramString)
    throws IllegalArgumentException
  {
    try
    {
      if (mApplied)
      {
        Log.e("MediaMetadataEditor", "Can't edit a previously applied MediaMetadataEditor");
        return this;
      }
      if (METADATA_KEYS_TYPE.get(paramInt, -1) == 1)
      {
        mEditorMetadata.putString(String.valueOf(paramInt), paramString);
        mMetadataChanged = true;
        return this;
      }
      IllegalArgumentException localIllegalArgumentException = new java/lang/IllegalArgumentException;
      paramString = new java/lang/StringBuilder;
      paramString.<init>();
      paramString.append("Invalid type 'String' for key ");
      paramString.append(paramInt);
      localIllegalArgumentException.<init>(paramString.toString());
      throw localIllegalArgumentException;
    }
    finally {}
  }
  
  public void removeEditableKeys()
  {
    try
    {
      if (mApplied)
      {
        Log.e("MediaMetadataEditor", "Can't remove all editable keys of a previously applied MetadataEditor");
        return;
      }
      if (mEditableKeys != 0L)
      {
        mEditableKeys = 0L;
        mMetadataChanged = true;
      }
      return;
    }
    finally {}
  }
}
