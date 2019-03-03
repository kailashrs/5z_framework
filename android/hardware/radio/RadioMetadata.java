package android.hardware.radio;

import android.annotation.SystemApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import java.util.Iterator;
import java.util.Set;

@SystemApi
public final class RadioMetadata
  implements Parcelable
{
  public static final Parcelable.Creator<RadioMetadata> CREATOR = new Parcelable.Creator()
  {
    public RadioMetadata createFromParcel(Parcel paramAnonymousParcel)
    {
      return new RadioMetadata(paramAnonymousParcel, null);
    }
    
    public RadioMetadata[] newArray(int paramAnonymousInt)
    {
      return new RadioMetadata[paramAnonymousInt];
    }
  };
  private static final ArrayMap<String, Integer> METADATA_KEYS_TYPE = new ArrayMap();
  public static final String METADATA_KEY_ALBUM = "android.hardware.radio.metadata.ALBUM";
  public static final String METADATA_KEY_ART = "android.hardware.radio.metadata.ART";
  public static final String METADATA_KEY_ARTIST = "android.hardware.radio.metadata.ARTIST";
  public static final String METADATA_KEY_CLOCK = "android.hardware.radio.metadata.CLOCK";
  public static final String METADATA_KEY_DAB_COMPONENT_NAME = "android.hardware.radio.metadata.DAB_COMPONENT_NAME";
  public static final String METADATA_KEY_DAB_COMPONENT_NAME_SHORT = "android.hardware.radio.metadata.DAB_COMPONENT_NAME_SHORT";
  public static final String METADATA_KEY_DAB_ENSEMBLE_NAME = "android.hardware.radio.metadata.DAB_ENSEMBLE_NAME";
  public static final String METADATA_KEY_DAB_ENSEMBLE_NAME_SHORT = "android.hardware.radio.metadata.DAB_ENSEMBLE_NAME_SHORT";
  public static final String METADATA_KEY_DAB_SERVICE_NAME = "android.hardware.radio.metadata.DAB_SERVICE_NAME";
  public static final String METADATA_KEY_DAB_SERVICE_NAME_SHORT = "android.hardware.radio.metadata.DAB_SERVICE_NAME_SHORT";
  public static final String METADATA_KEY_GENRE = "android.hardware.radio.metadata.GENRE";
  public static final String METADATA_KEY_ICON = "android.hardware.radio.metadata.ICON";
  public static final String METADATA_KEY_PROGRAM_NAME = "android.hardware.radio.metadata.PROGRAM_NAME";
  public static final String METADATA_KEY_RBDS_PTY = "android.hardware.radio.metadata.RBDS_PTY";
  public static final String METADATA_KEY_RDS_PI = "android.hardware.radio.metadata.RDS_PI";
  public static final String METADATA_KEY_RDS_PS = "android.hardware.radio.metadata.RDS_PS";
  public static final String METADATA_KEY_RDS_PTY = "android.hardware.radio.metadata.RDS_PTY";
  public static final String METADATA_KEY_RDS_RT = "android.hardware.radio.metadata.RDS_RT";
  public static final String METADATA_KEY_TITLE = "android.hardware.radio.metadata.TITLE";
  private static final int METADATA_TYPE_BITMAP = 2;
  private static final int METADATA_TYPE_CLOCK = 3;
  private static final int METADATA_TYPE_INT = 0;
  private static final int METADATA_TYPE_INVALID = -1;
  private static final int METADATA_TYPE_TEXT = 1;
  private static final int NATIVE_KEY_ALBUM = 7;
  private static final int NATIVE_KEY_ART = 10;
  private static final int NATIVE_KEY_ARTIST = 6;
  private static final int NATIVE_KEY_CLOCK = 11;
  private static final int NATIVE_KEY_GENRE = 8;
  private static final int NATIVE_KEY_ICON = 9;
  private static final int NATIVE_KEY_INVALID = -1;
  private static final SparseArray<String> NATIVE_KEY_MAPPING;
  private static final int NATIVE_KEY_RBDS_PTY = 3;
  private static final int NATIVE_KEY_RDS_PI = 0;
  private static final int NATIVE_KEY_RDS_PS = 1;
  private static final int NATIVE_KEY_RDS_PTY = 2;
  private static final int NATIVE_KEY_RDS_RT = 4;
  private static final int NATIVE_KEY_TITLE = 5;
  private static final String TAG = "BroadcastRadio.metadata";
  private final Bundle mBundle;
  
  static
  {
    METADATA_KEYS_TYPE.put("android.hardware.radio.metadata.RDS_PI", Integer.valueOf(0));
    METADATA_KEYS_TYPE.put("android.hardware.radio.metadata.RDS_PS", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.hardware.radio.metadata.RDS_PTY", Integer.valueOf(0));
    METADATA_KEYS_TYPE.put("android.hardware.radio.metadata.RBDS_PTY", Integer.valueOf(0));
    METADATA_KEYS_TYPE.put("android.hardware.radio.metadata.RDS_RT", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.hardware.radio.metadata.TITLE", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.hardware.radio.metadata.ARTIST", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.hardware.radio.metadata.ALBUM", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.hardware.radio.metadata.GENRE", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.hardware.radio.metadata.ICON", Integer.valueOf(2));
    METADATA_KEYS_TYPE.put("android.hardware.radio.metadata.ART", Integer.valueOf(2));
    METADATA_KEYS_TYPE.put("android.hardware.radio.metadata.CLOCK", Integer.valueOf(3));
    METADATA_KEYS_TYPE.put("android.hardware.radio.metadata.PROGRAM_NAME", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.hardware.radio.metadata.DAB_ENSEMBLE_NAME", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.hardware.radio.metadata.DAB_ENSEMBLE_NAME_SHORT", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.hardware.radio.metadata.DAB_SERVICE_NAME", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.hardware.radio.metadata.DAB_SERVICE_NAME_SHORT", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.hardware.radio.metadata.DAB_COMPONENT_NAME", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.hardware.radio.metadata.DAB_COMPONENT_NAME_SHORT", Integer.valueOf(1));
    NATIVE_KEY_MAPPING = new SparseArray();
    NATIVE_KEY_MAPPING.put(0, "android.hardware.radio.metadata.RDS_PI");
    NATIVE_KEY_MAPPING.put(1, "android.hardware.radio.metadata.RDS_PS");
    NATIVE_KEY_MAPPING.put(2, "android.hardware.radio.metadata.RDS_PTY");
    NATIVE_KEY_MAPPING.put(3, "android.hardware.radio.metadata.RBDS_PTY");
    NATIVE_KEY_MAPPING.put(4, "android.hardware.radio.metadata.RDS_RT");
    NATIVE_KEY_MAPPING.put(5, "android.hardware.radio.metadata.TITLE");
    NATIVE_KEY_MAPPING.put(6, "android.hardware.radio.metadata.ARTIST");
    NATIVE_KEY_MAPPING.put(7, "android.hardware.radio.metadata.ALBUM");
    NATIVE_KEY_MAPPING.put(8, "android.hardware.radio.metadata.GENRE");
    NATIVE_KEY_MAPPING.put(9, "android.hardware.radio.metadata.ICON");
    NATIVE_KEY_MAPPING.put(10, "android.hardware.radio.metadata.ART");
    NATIVE_KEY_MAPPING.put(11, "android.hardware.radio.metadata.CLOCK");
  }
  
  RadioMetadata()
  {
    mBundle = new Bundle();
  }
  
  private RadioMetadata(Bundle paramBundle)
  {
    mBundle = new Bundle(paramBundle);
  }
  
  private RadioMetadata(Parcel paramParcel)
  {
    mBundle = paramParcel.readBundle();
  }
  
  public static String getKeyFromNativeKey(int paramInt)
  {
    return (String)NATIVE_KEY_MAPPING.get(paramInt, null);
  }
  
  private static void putInt(Bundle paramBundle, String paramString, int paramInt)
  {
    int i = ((Integer)METADATA_KEYS_TYPE.getOrDefault(paramString, Integer.valueOf(-1))).intValue();
    if ((i != 0) && (i != 2))
    {
      paramBundle = new StringBuilder();
      paramBundle.append("The ");
      paramBundle.append(paramString);
      paramBundle.append(" key cannot be used to put an int");
      throw new IllegalArgumentException(paramBundle.toString());
    }
    paramBundle.putInt(paramString, paramInt);
  }
  
  public boolean containsKey(String paramString)
  {
    return mBundle.containsKey(paramString);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  @Deprecated
  public Bitmap getBitmap(String paramString)
  {
    Object localObject = null;
    try
    {
      paramString = (Bitmap)mBundle.getParcelable(paramString);
    }
    catch (Exception paramString)
    {
      Log.w("BroadcastRadio.metadata", "Failed to retrieve a key as Bitmap.", paramString);
      paramString = localObject;
    }
    return paramString;
  }
  
  public int getBitmapId(String paramString)
  {
    if ((!"android.hardware.radio.metadata.ICON".equals(paramString)) && (!"android.hardware.radio.metadata.ART".equals(paramString))) {
      return 0;
    }
    return getInt(paramString);
  }
  
  public Clock getClock(String paramString)
  {
    Object localObject = null;
    try
    {
      paramString = (Clock)mBundle.getParcelable(paramString);
    }
    catch (Exception paramString)
    {
      Log.w("BroadcastRadio.metadata", "Failed to retrieve a key as Clock.", paramString);
      paramString = localObject;
    }
    return paramString;
  }
  
  public int getInt(String paramString)
  {
    return mBundle.getInt(paramString, 0);
  }
  
  public String getString(String paramString)
  {
    return mBundle.getString(paramString);
  }
  
  public Set<String> keySet()
  {
    return mBundle.keySet();
  }
  
  int putBitmapFromNative(int paramInt, byte[] paramArrayOfByte)
  {
    String str = getKeyFromNativeKey(paramInt);
    if ((METADATA_KEYS_TYPE.containsKey(str)) && (((Integer)METADATA_KEYS_TYPE.get(str)).intValue() == 2))
    {
      try
      {
        paramArrayOfByte = BitmapFactory.decodeByteArray(paramArrayOfByte, 0, paramArrayOfByte.length);
        if (paramArrayOfByte != null)
        {
          mBundle.putParcelable(str, paramArrayOfByte);
          return 0;
        }
      }
      catch (Exception paramArrayOfByte) {}
      return -1;
    }
    return -1;
  }
  
  int putClockFromNative(int paramInt1, long paramLong, int paramInt2)
  {
    String str = getKeyFromNativeKey(paramInt1);
    if ((METADATA_KEYS_TYPE.containsKey(str)) && (((Integer)METADATA_KEYS_TYPE.get(str)).intValue() == 3))
    {
      mBundle.putParcelable(str, new Clock(paramLong, paramInt2));
      return 0;
    }
    return -1;
  }
  
  int putIntFromNative(int paramInt1, int paramInt2)
  {
    String str = getKeyFromNativeKey(paramInt1);
    try
    {
      putInt(mBundle, str, paramInt2);
      return 0;
    }
    catch (IllegalArgumentException localIllegalArgumentException) {}
    return -1;
  }
  
  int putStringFromNative(int paramInt, String paramString)
  {
    String str = getKeyFromNativeKey(paramInt);
    if ((METADATA_KEYS_TYPE.containsKey(str)) && (((Integer)METADATA_KEYS_TYPE.get(str)).intValue() == 1))
    {
      mBundle.putString(str, paramString);
      return 0;
    }
    return -1;
  }
  
  public int size()
  {
    return mBundle.size();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("RadioMetadata[");
    int i = 1;
    Iterator localIterator = mBundle.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str1 = (String)localIterator.next();
      if (i != 0) {
        i = 0;
      } else {
        localStringBuilder.append(", ");
      }
      String str2 = str1;
      if (str1.startsWith("android.hardware.radio.metadata")) {
        str2 = str1.substring("android.hardware.radio.metadata".length());
      }
      localStringBuilder.append(str2);
      localStringBuilder.append('=');
      localStringBuilder.append(mBundle.get(str1));
    }
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeBundle(mBundle);
  }
  
  public static final class Builder
  {
    private final Bundle mBundle;
    
    public Builder()
    {
      mBundle = new Bundle();
    }
    
    public Builder(RadioMetadata paramRadioMetadata)
    {
      mBundle = new Bundle(mBundle);
    }
    
    public Builder(RadioMetadata paramRadioMetadata, int paramInt)
    {
      this(paramRadioMetadata);
      Iterator localIterator = mBundle.keySet().iterator();
      while (localIterator.hasNext())
      {
        paramRadioMetadata = (String)localIterator.next();
        Object localObject = mBundle.get(paramRadioMetadata);
        if ((localObject != null) && ((localObject instanceof Bitmap)))
        {
          localObject = (Bitmap)localObject;
          if ((((Bitmap)localObject).getHeight() > paramInt) || (((Bitmap)localObject).getWidth() > paramInt)) {
            putBitmap(paramRadioMetadata, scaleBitmap((Bitmap)localObject, paramInt));
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
    
    public RadioMetadata build()
    {
      return new RadioMetadata(mBundle, null);
    }
    
    public Builder putBitmap(String paramString, Bitmap paramBitmap)
    {
      if ((RadioMetadata.METADATA_KEYS_TYPE.containsKey(paramString)) && (((Integer)RadioMetadata.METADATA_KEYS_TYPE.get(paramString)).intValue() == 2))
      {
        mBundle.putParcelable(paramString, paramBitmap);
        return this;
      }
      paramBitmap = new StringBuilder();
      paramBitmap.append("The ");
      paramBitmap.append(paramString);
      paramBitmap.append(" key cannot be used to put a Bitmap");
      throw new IllegalArgumentException(paramBitmap.toString());
    }
    
    public Builder putClock(String paramString, long paramLong, int paramInt)
    {
      if ((RadioMetadata.METADATA_KEYS_TYPE.containsKey(paramString)) && (((Integer)RadioMetadata.METADATA_KEYS_TYPE.get(paramString)).intValue() == 3))
      {
        mBundle.putParcelable(paramString, new RadioMetadata.Clock(paramLong, paramInt));
        return this;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("The ");
      localStringBuilder.append(paramString);
      localStringBuilder.append(" key cannot be used to put a RadioMetadata.Clock.");
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public Builder putInt(String paramString, int paramInt)
    {
      RadioMetadata.putInt(mBundle, paramString, paramInt);
      return this;
    }
    
    public Builder putString(String paramString1, String paramString2)
    {
      if ((RadioMetadata.METADATA_KEYS_TYPE.containsKey(paramString1)) && (((Integer)RadioMetadata.METADATA_KEYS_TYPE.get(paramString1)).intValue() == 1))
      {
        mBundle.putString(paramString1, paramString2);
        return this;
      }
      paramString2 = new StringBuilder();
      paramString2.append("The ");
      paramString2.append(paramString1);
      paramString2.append(" key cannot be used to put a String");
      throw new IllegalArgumentException(paramString2.toString());
    }
  }
  
  @SystemApi
  public static final class Clock
    implements Parcelable
  {
    public static final Parcelable.Creator<Clock> CREATOR = new Parcelable.Creator()
    {
      public RadioMetadata.Clock createFromParcel(Parcel paramAnonymousParcel)
      {
        return new RadioMetadata.Clock(paramAnonymousParcel, null);
      }
      
      public RadioMetadata.Clock[] newArray(int paramAnonymousInt)
      {
        return new RadioMetadata.Clock[paramAnonymousInt];
      }
    };
    private final int mTimezoneOffsetMinutes;
    private final long mUtcEpochSeconds;
    
    public Clock(long paramLong, int paramInt)
    {
      mUtcEpochSeconds = paramLong;
      mTimezoneOffsetMinutes = paramInt;
    }
    
    private Clock(Parcel paramParcel)
    {
      mUtcEpochSeconds = paramParcel.readLong();
      mTimezoneOffsetMinutes = paramParcel.readInt();
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public int getTimezoneOffsetMinutes()
    {
      return mTimezoneOffsetMinutes;
    }
    
    public long getUtcEpochSeconds()
    {
      return mUtcEpochSeconds;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeLong(mUtcEpochSeconds);
      paramParcel.writeInt(mTimezoneOffsetMinutes);
    }
  }
}
