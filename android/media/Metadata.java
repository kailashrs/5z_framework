package android.media;

import android.os.Parcel;
import android.util.Log;
import android.util.MathUtils;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.TimeZone;

@Deprecated
public class Metadata
{
  public static final int ALBUM = 8;
  public static final int ALBUM_ART = 18;
  public static final int ANY = 0;
  public static final int ARTIST = 9;
  public static final int AUDIO_BIT_RATE = 21;
  public static final int AUDIO_CODEC = 26;
  public static final int AUDIO_SAMPLE_RATE = 23;
  public static final int AUTHOR = 10;
  public static final int BIT_RATE = 20;
  public static final int BOOLEAN_VAL = 3;
  public static final int BYTE_ARRAY_VAL = 7;
  public static final int CD_TRACK_MAX = 16;
  public static final int CD_TRACK_NUM = 15;
  public static final int COMMENT = 6;
  public static final int COMPOSER = 11;
  public static final int COPYRIGHT = 7;
  public static final int DATE = 13;
  public static final int DATE_VAL = 6;
  public static final int DOUBLE_VAL = 5;
  public static final int DRM_CRIPPLED = 31;
  public static final int DURATION = 14;
  private static final int FIRST_CUSTOM = 8192;
  public static final int GENRE = 12;
  public static final int INTEGER_VAL = 2;
  private static final int LAST_SYSTEM = 31;
  private static final int LAST_TYPE = 7;
  public static final int LONG_VAL = 4;
  public static final Set<Integer> MATCH_ALL = Collections.singleton(Integer.valueOf(0));
  public static final Set<Integer> MATCH_NONE = Collections.EMPTY_SET;
  public static final int MIME_TYPE = 25;
  public static final int NUM_TRACKS = 30;
  public static final int PAUSE_AVAILABLE = 1;
  public static final int RATING = 17;
  public static final int SEEK_AVAILABLE = 4;
  public static final int SEEK_BACKWARD_AVAILABLE = 2;
  public static final int SEEK_FORWARD_AVAILABLE = 3;
  public static final int STRING_VAL = 1;
  private static final String TAG = "media.Metadata";
  public static final int TITLE = 5;
  public static final int VIDEO_BIT_RATE = 22;
  public static final int VIDEO_CODEC = 27;
  public static final int VIDEO_FRAME = 19;
  public static final int VIDEO_FRAME_RATE = 24;
  public static final int VIDEO_HEIGHT = 28;
  public static final int VIDEO_WIDTH = 29;
  private static final int kInt32Size = 4;
  private static final int kMetaHeaderSize = 8;
  private static final int kMetaMarker = 1296389185;
  private static final int kRecordHeaderSize = 12;
  private final HashMap<Integer, Integer> mKeyToPosMap = new HashMap();
  private Parcel mParcel;
  
  public Metadata() {}
  
  private boolean checkMetadataId(int paramInt)
  {
    if ((paramInt > 0) && ((31 >= paramInt) || (paramInt >= 8192))) {
      return true;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid metadata ID ");
    localStringBuilder.append(paramInt);
    Log.e("media.Metadata", localStringBuilder.toString());
    return false;
  }
  
  private void checkType(int paramInt1, int paramInt2)
  {
    paramInt1 = ((Integer)mKeyToPosMap.get(Integer.valueOf(paramInt1))).intValue();
    mParcel.setDataPosition(paramInt1);
    paramInt1 = mParcel.readInt();
    if (paramInt1 == paramInt2) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Wrong type ");
    localStringBuilder.append(paramInt2);
    localStringBuilder.append(" but got ");
    localStringBuilder.append(paramInt1);
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  public static int firstCustomId()
  {
    return 8192;
  }
  
  public static int lastSytemId()
  {
    return 31;
  }
  
  public static int lastType()
  {
    return 7;
  }
  
  private boolean scanAllRecords(Parcel paramParcel, int paramInt)
  {
    int i = 0;
    int j = 0;
    mKeyToPosMap.clear();
    int k = paramInt;
    int n;
    for (;;)
    {
      paramInt = j;
      if (k <= 12) {
        break label246;
      }
      paramInt = paramParcel.dataPosition();
      int m = paramParcel.readInt();
      if (m <= 12)
      {
        Log.e("media.Metadata", "Record is too short");
        paramInt = 1;
        break label246;
      }
      n = paramParcel.readInt();
      if (!checkMetadataId(n))
      {
        paramInt = 1;
        break label246;
      }
      if (mKeyToPosMap.containsKey(Integer.valueOf(n)))
      {
        Log.e("media.Metadata", "Duplicate metadata ID found");
        paramInt = 1;
        break label246;
      }
      mKeyToPosMap.put(Integer.valueOf(n), Integer.valueOf(paramParcel.dataPosition()));
      n = paramParcel.readInt();
      if ((n > 0) && (n <= 7)) {
        try
        {
          paramParcel.setDataPosition(MathUtils.addOrThrow(paramInt, m));
          k -= m;
          i++;
        }
        catch (IllegalArgumentException paramParcel)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Invalid size: ");
          localStringBuilder.append(paramParcel.getMessage());
          Log.e("media.Metadata", localStringBuilder.toString());
          paramInt = 1;
          break label246;
        }
      }
    }
    paramParcel = new StringBuilder();
    paramParcel.append("Invalid metadata type ");
    paramParcel.append(n);
    Log.e("media.Metadata", paramParcel.toString());
    paramInt = 1;
    label246:
    if ((k == 0) && (paramInt == 0)) {
      return true;
    }
    paramParcel = new StringBuilder();
    paramParcel.append("Ran out of data or error on record ");
    paramParcel.append(i);
    Log.e("media.Metadata", paramParcel.toString());
    mKeyToPosMap.clear();
    return false;
  }
  
  public boolean getBoolean(int paramInt)
  {
    checkType(paramInt, 3);
    paramInt = mParcel.readInt();
    boolean bool = true;
    if (paramInt != 1) {
      bool = false;
    }
    return bool;
  }
  
  public byte[] getByteArray(int paramInt)
  {
    checkType(paramInt, 7);
    return mParcel.createByteArray();
  }
  
  public Date getDate(int paramInt)
  {
    checkType(paramInt, 6);
    long l = mParcel.readLong();
    Object localObject = mParcel.readString();
    if (((String)localObject).length() == 0) {
      return new Date(l);
    }
    localObject = Calendar.getInstance(TimeZone.getTimeZone((String)localObject));
    ((Calendar)localObject).setTimeInMillis(l);
    return ((Calendar)localObject).getTime();
  }
  
  public double getDouble(int paramInt)
  {
    checkType(paramInt, 5);
    return mParcel.readDouble();
  }
  
  public int getInt(int paramInt)
  {
    checkType(paramInt, 2);
    return mParcel.readInt();
  }
  
  public long getLong(int paramInt)
  {
    checkType(paramInt, 4);
    return mParcel.readLong();
  }
  
  public String getString(int paramInt)
  {
    checkType(paramInt, 1);
    return mParcel.readString();
  }
  
  public boolean has(int paramInt)
  {
    if (checkMetadataId(paramInt)) {
      return mKeyToPosMap.containsKey(Integer.valueOf(paramInt));
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid key: ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public Set<Integer> keySet()
  {
    return mKeyToPosMap.keySet();
  }
  
  public boolean parse(Parcel paramParcel)
  {
    if (paramParcel.dataAvail() < 8)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Not enough data ");
      localStringBuilder.append(paramParcel.dataAvail());
      Log.e("media.Metadata", localStringBuilder.toString());
      return false;
    }
    int i = paramParcel.dataPosition();
    int j = paramParcel.readInt();
    if ((paramParcel.dataAvail() + 4 >= j) && (j >= 8))
    {
      int k = paramParcel.readInt();
      if (k != 1296389185)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Marker missing ");
        localStringBuilder.append(Integer.toHexString(k));
        Log.e("media.Metadata", localStringBuilder.toString());
        paramParcel.setDataPosition(i);
        return false;
      }
      if (!scanAllRecords(paramParcel, j - 8))
      {
        paramParcel.setDataPosition(i);
        return false;
      }
      mParcel = paramParcel;
      return true;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Bad size ");
    localStringBuilder.append(j);
    localStringBuilder.append(" avail ");
    localStringBuilder.append(paramParcel.dataAvail());
    localStringBuilder.append(" position ");
    localStringBuilder.append(i);
    Log.e("media.Metadata", localStringBuilder.toString());
    paramParcel.setDataPosition(i);
    return false;
  }
}
