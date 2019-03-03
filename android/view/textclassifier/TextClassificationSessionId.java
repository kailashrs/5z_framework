package android.view.textclassifier;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;
import java.util.Locale;
import java.util.UUID;

public final class TextClassificationSessionId
  implements Parcelable
{
  public static final Parcelable.Creator<TextClassificationSessionId> CREATOR = new Parcelable.Creator()
  {
    public TextClassificationSessionId createFromParcel(Parcel paramAnonymousParcel)
    {
      return new TextClassificationSessionId((String)Preconditions.checkNotNull(paramAnonymousParcel.readString()));
    }
    
    public TextClassificationSessionId[] newArray(int paramAnonymousInt)
    {
      return new TextClassificationSessionId[paramAnonymousInt];
    }
  };
  private final String mValue;
  
  public TextClassificationSessionId()
  {
    this(UUID.randomUUID().toString());
  }
  
  public TextClassificationSessionId(String paramString)
  {
    mValue = paramString;
  }
  
  public static TextClassificationSessionId unflattenFromString(String paramString)
  {
    return new TextClassificationSessionId(paramString);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (paramObject == null) {
      return false;
    }
    if (getClass() != paramObject.getClass()) {
      return false;
    }
    paramObject = (TextClassificationSessionId)paramObject;
    return mValue.equals(mValue);
  }
  
  public String flattenToString()
  {
    return mValue;
  }
  
  public int hashCode()
  {
    return 31 * 1 + mValue.hashCode();
  }
  
  public String toString()
  {
    return String.format(Locale.US, "TextClassificationSessionId {%s}", new Object[] { mValue });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mValue);
  }
}
