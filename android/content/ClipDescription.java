package android.content;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class ClipDescription
  implements Parcelable
{
  public static final Parcelable.Creator<ClipDescription> CREATOR = new Parcelable.Creator()
  {
    public ClipDescription createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ClipDescription(paramAnonymousParcel);
    }
    
    public ClipDescription[] newArray(int paramAnonymousInt)
    {
      return new ClipDescription[paramAnonymousInt];
    }
  };
  public static final String EXTRA_TARGET_COMPONENT_NAME = "android.content.extra.TARGET_COMPONENT_NAME";
  public static final String EXTRA_USER_SERIAL_NUMBER = "android.content.extra.USER_SERIAL_NUMBER";
  public static final String MIMETYPE_TEXT_HTML = "text/html";
  public static final String MIMETYPE_TEXT_INTENT = "text/vnd.android.intent";
  public static final String MIMETYPE_TEXT_PLAIN = "text/plain";
  public static final String MIMETYPE_TEXT_URILIST = "text/uri-list";
  private PersistableBundle mExtras;
  final CharSequence mLabel;
  private final ArrayList<String> mMimeTypes;
  private long mTimeStamp;
  
  public ClipDescription(ClipDescription paramClipDescription)
  {
    mLabel = mLabel;
    mMimeTypes = new ArrayList(mMimeTypes);
    mTimeStamp = mTimeStamp;
  }
  
  ClipDescription(Parcel paramParcel)
  {
    mLabel = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    mMimeTypes = paramParcel.createStringArrayList();
    mExtras = paramParcel.readPersistableBundle();
    mTimeStamp = paramParcel.readLong();
  }
  
  public ClipDescription(CharSequence paramCharSequence, String[] paramArrayOfString)
  {
    if (paramArrayOfString != null)
    {
      mLabel = paramCharSequence;
      mMimeTypes = new ArrayList(Arrays.asList(paramArrayOfString));
      return;
    }
    throw new NullPointerException("mimeTypes is null");
  }
  
  public static boolean compareMimeTypes(String paramString1, String paramString2)
  {
    int i = paramString2.length();
    if ((i == 3) && (paramString2.equals("*/*"))) {
      return true;
    }
    int j = paramString2.indexOf('/');
    if (j > 0) {
      if ((i == j + 2) && (paramString2.charAt(j + 1) == '*'))
      {
        if (paramString2.regionMatches(0, paramString1, 0, j + 1)) {
          return true;
        }
      }
      else if (paramString2.equals(paramString1)) {
        return true;
      }
    }
    return false;
  }
  
  void addMimeTypes(String[] paramArrayOfString)
  {
    for (int i = 0; i != paramArrayOfString.length; i++)
    {
      String str = paramArrayOfString[i];
      if (!mMimeTypes.contains(str)) {
        mMimeTypes.add(str);
      }
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String[] filterMimeTypes(String paramString)
  {
    Object localObject1 = null;
    int i = mMimeTypes.size();
    int j = 0;
    while (j < i)
    {
      Object localObject2 = localObject1;
      if (compareMimeTypes((String)mMimeTypes.get(j), paramString))
      {
        localObject2 = localObject1;
        if (localObject1 == null) {
          localObject2 = new ArrayList();
        }
        ((ArrayList)localObject2).add((String)mMimeTypes.get(j));
      }
      j++;
      localObject1 = localObject2;
    }
    if (localObject1 == null) {
      return null;
    }
    paramString = new String[localObject1.size()];
    localObject1.toArray(paramString);
    return paramString;
  }
  
  public PersistableBundle getExtras()
  {
    return mExtras;
  }
  
  public CharSequence getLabel()
  {
    return mLabel;
  }
  
  public String getMimeType(int paramInt)
  {
    return (String)mMimeTypes.get(paramInt);
  }
  
  public int getMimeTypeCount()
  {
    return mMimeTypes.size();
  }
  
  public long getTimestamp()
  {
    return mTimeStamp;
  }
  
  public boolean hasMimeType(String paramString)
  {
    int i = mMimeTypes.size();
    for (int j = 0; j < i; j++) {
      if (compareMimeTypes((String)mMimeTypes.get(j), paramString)) {
        return true;
      }
    }
    return false;
  }
  
  public void setExtras(PersistableBundle paramPersistableBundle)
  {
    mExtras = new PersistableBundle(paramPersistableBundle);
  }
  
  public void setTimestamp(long paramLong)
  {
    mTimeStamp = paramLong;
  }
  
  public boolean toShortString(StringBuilder paramStringBuilder)
  {
    boolean bool1 = toShortStringTypesOnly(paramStringBuilder);
    boolean bool2 = true;
    int i = bool1 ^ true;
    int j = i;
    if (mLabel != null)
    {
      if (i == 0) {
        paramStringBuilder.append(' ');
      }
      j = 0;
      paramStringBuilder.append('"');
      paramStringBuilder.append(mLabel);
      paramStringBuilder.append('"');
    }
    i = j;
    if (mExtras != null)
    {
      if (j == 0) {
        paramStringBuilder.append(' ');
      }
      i = 0;
      paramStringBuilder.append(mExtras.toString());
    }
    j = i;
    if (mTimeStamp > 0L)
    {
      if (i == 0) {
        paramStringBuilder.append(' ');
      }
      j = 0;
      paramStringBuilder.append('<');
      paramStringBuilder.append(TimeUtils.logTimeOfDay(mTimeStamp));
      paramStringBuilder.append('>');
    }
    if (j != 0) {
      bool2 = false;
    }
    return bool2;
  }
  
  public boolean toShortStringTypesOnly(StringBuilder paramStringBuilder)
  {
    int i = mMimeTypes.size();
    boolean bool = false;
    int j = 1;
    for (int k = 0; k < i; k++)
    {
      if (j == 0) {
        paramStringBuilder.append(' ');
      }
      j = 0;
      paramStringBuilder.append((String)mMimeTypes.get(k));
    }
    if (j == 0) {
      bool = true;
    }
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(128);
    localStringBuilder.append("ClipDescription { ");
    toShortString(localStringBuilder);
    localStringBuilder.append(" }");
    return localStringBuilder.toString();
  }
  
  public void validate()
  {
    if (mMimeTypes != null)
    {
      int i = mMimeTypes.size();
      if (i > 0)
      {
        int j = 0;
        while (j < i) {
          if (mMimeTypes.get(j) != null)
          {
            j++;
          }
          else
          {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("mime type at ");
            localStringBuilder.append(j);
            localStringBuilder.append(" is null");
            throw new NullPointerException(localStringBuilder.toString());
          }
        }
        return;
      }
      throw new IllegalArgumentException("must have at least 1 mime type");
    }
    throw new NullPointerException("null mime types");
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    TextUtils.writeToParcel(mLabel, paramParcel, paramInt);
    paramParcel.writeStringList(mMimeTypes);
    paramParcel.writePersistableBundle(mExtras);
    paramParcel.writeLong(mTimeStamp);
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    int i = mMimeTypes.size();
    for (int j = 0; j < i; j++) {
      paramProtoOutputStream.write(2237677961217L, (String)mMimeTypes.get(j));
    }
    if (mLabel != null) {
      paramProtoOutputStream.write(1138166333442L, mLabel.toString());
    }
    if (mExtras != null) {
      mExtras.writeToProto(paramProtoOutputStream, 1146756268035L);
    }
    if (mTimeStamp > 0L) {
      paramProtoOutputStream.write(1112396529668L, mTimeStamp);
    }
    paramProtoOutputStream.end(paramLong);
  }
}
