package android.app.slice;

import android.app.PendingIntent;
import android.app.RemoteInput;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Pair;
import android.widget.RemoteViews;
import com.android.internal.util.ArrayUtils;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.List;

public final class SliceItem
  implements Parcelable
{
  public static final Parcelable.Creator<SliceItem> CREATOR = new Parcelable.Creator()
  {
    public SliceItem createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SliceItem(paramAnonymousParcel);
    }
    
    public SliceItem[] newArray(int paramAnonymousInt)
    {
      return new SliceItem[paramAnonymousInt];
    }
  };
  public static final String FORMAT_ACTION = "action";
  public static final String FORMAT_BUNDLE = "bundle";
  public static final String FORMAT_IMAGE = "image";
  public static final String FORMAT_INT = "int";
  public static final String FORMAT_LONG = "long";
  public static final String FORMAT_REMOTE_INPUT = "input";
  public static final String FORMAT_SLICE = "slice";
  public static final String FORMAT_TEXT = "text";
  @Deprecated
  public static final String FORMAT_TIMESTAMP = "long";
  private static final String TAG = "SliceItem";
  private final String mFormat;
  protected String[] mHints;
  private final Object mObj;
  private final String mSubType;
  
  public SliceItem(PendingIntent paramPendingIntent, Slice paramSlice, String paramString1, String paramString2, String[] paramArrayOfString)
  {
    this(new Pair(paramPendingIntent, paramSlice), paramString1, paramString2, paramArrayOfString);
  }
  
  public SliceItem(Parcel paramParcel)
  {
    mHints = paramParcel.readStringArray();
    mFormat = paramParcel.readString();
    mSubType = paramParcel.readString();
    mObj = readObj(mFormat, paramParcel);
  }
  
  public SliceItem(Object paramObject, String paramString1, String paramString2, List<String> paramList)
  {
    this(paramObject, paramString1, paramString2, (String[])paramList.toArray(new String[paramList.size()]));
  }
  
  public SliceItem(Object paramObject, String paramString1, String paramString2, String[] paramArrayOfString)
  {
    mHints = paramArrayOfString;
    mFormat = paramString1;
    mSubType = paramString2;
    mObj = paramObject;
  }
  
  private static String getBaseType(String paramString)
  {
    int i = paramString.indexOf('/');
    if (i >= 0) {
      return paramString.substring(0, i);
    }
    return paramString;
  }
  
  private static Object readObj(String paramString, Parcel paramParcel)
  {
    String str = getBaseType(paramString);
    switch (str.hashCode())
    {
    default: 
      break;
    case 109526418: 
      if (str.equals("slice")) {
        i = 0;
      }
      break;
    case 100358090: 
      if (str.equals("input")) {
        i = 6;
      }
      break;
    case 100313435: 
      if (str.equals("image")) {
        i = 2;
      }
      break;
    case 3556653: 
      if (str.equals("text")) {
        i = 1;
      }
      break;
    case 3327612: 
      if (str.equals("long")) {
        i = 5;
      }
      break;
    case 104431: 
      if (str.equals("int")) {
        i = 4;
      }
      break;
    case -1377881982: 
      if (str.equals("bundle")) {
        i = 7;
      }
      break;
    case -1422950858: 
      if (str.equals("action")) {
        i = 3;
      }
      break;
    }
    int i = -1;
    switch (i)
    {
    default: 
      paramParcel = new StringBuilder();
      paramParcel.append("Unsupported type ");
      paramParcel.append(paramString);
      throw new RuntimeException(paramParcel.toString());
    case 7: 
      return Bundle.CREATOR.createFromParcel(paramParcel);
    case 6: 
      return RemoteInput.CREATOR.createFromParcel(paramParcel);
    case 5: 
      return Long.valueOf(paramParcel.readLong());
    case 4: 
      return Integer.valueOf(paramParcel.readInt());
    case 3: 
      return new Pair((PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel), (Slice)Slice.CREATOR.createFromParcel(paramParcel));
    case 2: 
      return Icon.CREATOR.createFromParcel(paramParcel);
    case 1: 
      return TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel);
    }
    return Slice.CREATOR.createFromParcel(paramParcel);
  }
  
  private static void writeObj(Parcel paramParcel, int paramInt, Object paramObject, String paramString)
  {
    paramString = getBaseType(paramString);
    switch (paramString.hashCode())
    {
    default: 
      break;
    case 109526418: 
      if (paramString.equals("slice")) {
        i = 0;
      }
      break;
    case 100358090: 
      if (paramString.equals("input")) {
        i = 2;
      }
      break;
    case 100313435: 
      if (paramString.equals("image")) {
        i = 1;
      }
      break;
    case 3556653: 
      if (paramString.equals("text")) {
        i = 5;
      }
      break;
    case 3327612: 
      if (paramString.equals("long")) {
        i = 7;
      }
      break;
    case 104431: 
      if (paramString.equals("int")) {
        i = 6;
      }
      break;
    case -1377881982: 
      if (paramString.equals("bundle")) {
        i = 3;
      }
      break;
    case -1422950858: 
      if (paramString.equals("action")) {
        i = 4;
      }
      break;
    }
    int i = -1;
    switch (i)
    {
    default: 
      break;
    case 7: 
      paramParcel.writeLong(((Long)paramObject).longValue());
      break;
    case 6: 
      paramParcel.writeInt(((Integer)paramObject).intValue());
      break;
    case 5: 
      TextUtils.writeToParcel((CharSequence)paramObject, paramParcel, paramInt);
      break;
    case 4: 
      ((PendingIntent)first).writeToParcel(paramParcel, paramInt);
      ((Slice)second).writeToParcel(paramParcel, paramInt);
      break;
    case 0: 
    case 1: 
    case 2: 
    case 3: 
      ((Parcelable)paramObject).writeToParcel(paramParcel, paramInt);
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public PendingIntent getAction()
  {
    return (PendingIntent)mObj).first;
  }
  
  public Bundle getBundle()
  {
    return (Bundle)mObj;
  }
  
  public String getFormat()
  {
    return mFormat;
  }
  
  public List<String> getHints()
  {
    return Arrays.asList(mHints);
  }
  
  public Icon getIcon()
  {
    return (Icon)mObj;
  }
  
  public int getInt()
  {
    return ((Integer)mObj).intValue();
  }
  
  public long getLong()
  {
    return ((Long)mObj).longValue();
  }
  
  public RemoteInput getRemoteInput()
  {
    return (RemoteInput)mObj;
  }
  
  public RemoteViews getRemoteView()
  {
    return (RemoteViews)mObj;
  }
  
  public Slice getSlice()
  {
    if ("action".equals(getFormat())) {
      return (Slice)mObj).second;
    }
    return (Slice)mObj;
  }
  
  public String getSubType()
  {
    return mSubType;
  }
  
  public CharSequence getText()
  {
    return (CharSequence)mObj;
  }
  
  @Deprecated
  public long getTimestamp()
  {
    return ((Long)mObj).longValue();
  }
  
  public boolean hasAnyHints(String[] paramArrayOfString)
  {
    if (paramArrayOfString == null) {
      return false;
    }
    int i = paramArrayOfString.length;
    for (int j = 0; j < i; j++)
    {
      String str = paramArrayOfString[j];
      if (ArrayUtils.contains(mHints, str)) {
        return true;
      }
    }
    return false;
  }
  
  public boolean hasHint(String paramString)
  {
    return ArrayUtils.contains(mHints, paramString);
  }
  
  public boolean hasHints(String[] paramArrayOfString)
  {
    if (paramArrayOfString == null) {
      return true;
    }
    int i = paramArrayOfString.length;
    for (int j = 0; j < i; j++)
    {
      String str = paramArrayOfString[j];
      if ((!TextUtils.isEmpty(str)) && (!ArrayUtils.contains(mHints, str))) {
        return false;
      }
    }
    return true;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeStringArray(mHints);
    paramParcel.writeString(mFormat);
    paramParcel.writeString(mSubType);
    writeObj(paramParcel, paramInt, mObj, mFormat);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SliceType {}
}
