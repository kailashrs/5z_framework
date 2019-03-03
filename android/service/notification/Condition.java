package android.service.notification;

import android.annotation.SystemApi;
import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.proto.ProtoOutputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

public final class Condition
  implements Parcelable
{
  public static final Parcelable.Creator<Condition> CREATOR = new Parcelable.Creator()
  {
    public Condition createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Condition(paramAnonymousParcel);
    }
    
    public Condition[] newArray(int paramAnonymousInt)
    {
      return new Condition[paramAnonymousInt];
    }
  };
  @SystemApi
  public static final int FLAG_RELEVANT_ALWAYS = 2;
  @SystemApi
  public static final int FLAG_RELEVANT_NOW = 1;
  @SystemApi
  public static final String SCHEME = "condition";
  @SystemApi
  public static final int STATE_ERROR = 3;
  public static final int STATE_FALSE = 0;
  public static final int STATE_TRUE = 1;
  @SystemApi
  public static final int STATE_UNKNOWN = 2;
  @SystemApi
  public final int flags;
  @SystemApi
  public final int icon;
  public final Uri id;
  @SystemApi
  public final String line1;
  @SystemApi
  public final String line2;
  public final int state;
  public final String summary;
  
  public Condition(Uri paramUri, String paramString, int paramInt)
  {
    this(paramUri, paramString, "", "", -1, paramInt, 2);
  }
  
  @SystemApi
  public Condition(Uri paramUri, String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramUri != null)
    {
      if (paramString1 != null)
      {
        if (isValidState(paramInt2))
        {
          id = paramUri;
          summary = paramString1;
          line1 = paramString2;
          line2 = paramString3;
          icon = paramInt1;
          state = paramInt2;
          flags = paramInt3;
          return;
        }
        paramUri = new StringBuilder();
        paramUri.append("state is invalid: ");
        paramUri.append(paramInt2);
        throw new IllegalArgumentException(paramUri.toString());
      }
      throw new IllegalArgumentException("summary is required");
    }
    throw new IllegalArgumentException("id is required");
  }
  
  public Condition(Parcel paramParcel)
  {
    this((Uri)paramParcel.readParcelable(Condition.class.getClassLoader()), paramParcel.readString(), paramParcel.readString(), paramParcel.readString(), paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt());
  }
  
  @SystemApi
  public static boolean isValidId(Uri paramUri, String paramString)
  {
    boolean bool;
    if ((paramUri != null) && ("condition".equals(paramUri.getScheme())) && (paramString.equals(paramUri.getAuthority()))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static boolean isValidState(int paramInt)
  {
    boolean bool;
    if ((paramInt >= 0) && (paramInt <= 3)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @SystemApi
  public static Uri.Builder newId(Context paramContext)
  {
    return new Uri.Builder().scheme("condition").authority(paramContext.getPackageName());
  }
  
  @SystemApi
  public static String relevanceToString(int paramInt)
  {
    int i = 0;
    int j;
    if ((paramInt & 0x1) != 0) {
      j = 1;
    } else {
      j = 0;
    }
    if ((paramInt & 0x2) != 0) {
      i = 1;
    }
    if ((j == 0) && (i == 0)) {
      return "NONE";
    }
    if ((j != 0) && (i != 0)) {
      return "NOW, ALWAYS";
    }
    String str;
    if (j != 0) {
      str = "NOW";
    } else {
      str = "ALWAYS";
    }
    return str;
  }
  
  @SystemApi
  public static String stateToString(int paramInt)
  {
    if (paramInt == 0) {
      return "STATE_FALSE";
    }
    if (paramInt == 1) {
      return "STATE_TRUE";
    }
    if (paramInt == 2) {
      return "STATE_UNKNOWN";
    }
    if (paramInt == 3) {
      return "STATE_ERROR";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("state is invalid: ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  @SystemApi
  public Condition copy()
  {
    Parcel localParcel = Parcel.obtain();
    try
    {
      writeToParcel(localParcel, 0);
      localParcel.setDataPosition(0);
      Condition localCondition = new Condition(localParcel);
      return localCondition;
    }
    finally
    {
      localParcel.recycle();
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof Condition)) {
      return false;
    }
    boolean bool = true;
    if (paramObject == this) {
      return true;
    }
    paramObject = (Condition)paramObject;
    if ((!Objects.equals(id, id)) || (!Objects.equals(summary, summary)) || (!Objects.equals(line1, line1)) || (!Objects.equals(line2, line2)) || (icon != icon) || (state != state) || (flags != flags)) {
      bool = false;
    }
    return bool;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { id, summary, line1, line2, Integer.valueOf(icon), Integer.valueOf(state), Integer.valueOf(flags) });
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(Condition.class.getSimpleName());
    localStringBuilder.append('[');
    localStringBuilder.append("id=");
    localStringBuilder.append(id);
    localStringBuilder.append(",summary=");
    localStringBuilder.append(summary);
    localStringBuilder.append(",line1=");
    localStringBuilder.append(line1);
    localStringBuilder.append(",line2=");
    localStringBuilder.append(line2);
    localStringBuilder.append(",icon=");
    localStringBuilder.append(icon);
    localStringBuilder.append(",state=");
    localStringBuilder.append(stateToString(state));
    localStringBuilder.append(",flags=");
    localStringBuilder.append(flags);
    localStringBuilder.append(']');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(id, 0);
    paramParcel.writeString(summary);
    paramParcel.writeString(line1);
    paramParcel.writeString(line2);
    paramParcel.writeInt(icon);
    paramParcel.writeInt(state);
    paramParcel.writeInt(flags);
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    paramProtoOutputStream.write(1138166333441L, id.toString());
    paramProtoOutputStream.write(1138166333442L, summary);
    paramProtoOutputStream.write(1138166333443L, line1);
    paramProtoOutputStream.write(1138166333444L, line2);
    paramProtoOutputStream.write(1120986464261L, icon);
    paramProtoOutputStream.write(1159641169926L, state);
    paramProtoOutputStream.write(1120986464263L, flags);
    paramProtoOutputStream.end(paramLong);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface State {}
}
