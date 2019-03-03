package android.view.textclassifier;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;
import java.util.Locale;

public final class TextClassificationContext
  implements Parcelable
{
  public static final Parcelable.Creator<TextClassificationContext> CREATOR = new Parcelable.Creator()
  {
    public TextClassificationContext createFromParcel(Parcel paramAnonymousParcel)
    {
      return new TextClassificationContext(paramAnonymousParcel, null);
    }
    
    public TextClassificationContext[] newArray(int paramAnonymousInt)
    {
      return new TextClassificationContext[paramAnonymousInt];
    }
  };
  private final String mPackageName;
  private final String mWidgetType;
  private final String mWidgetVersion;
  
  private TextClassificationContext(Parcel paramParcel)
  {
    mPackageName = paramParcel.readString();
    mWidgetType = paramParcel.readString();
    mWidgetVersion = paramParcel.readString();
  }
  
  private TextClassificationContext(String paramString1, String paramString2, String paramString3)
  {
    mPackageName = ((String)Preconditions.checkNotNull(paramString1));
    mWidgetType = ((String)Preconditions.checkNotNull(paramString2));
    mWidgetVersion = paramString3;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getPackageName()
  {
    return mPackageName;
  }
  
  public String getWidgetType()
  {
    return mWidgetType;
  }
  
  public String getWidgetVersion()
  {
    return mWidgetVersion;
  }
  
  public String toString()
  {
    return String.format(Locale.US, "TextClassificationContext{packageName=%s, widgetType=%s, widgetVersion=%s}", new Object[] { mPackageName, mWidgetType, mWidgetVersion });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mPackageName);
    paramParcel.writeString(mWidgetType);
    paramParcel.writeString(mWidgetVersion);
  }
  
  public static final class Builder
  {
    private final String mPackageName;
    private final String mWidgetType;
    private String mWidgetVersion;
    
    public Builder(String paramString1, String paramString2)
    {
      mPackageName = ((String)Preconditions.checkNotNull(paramString1));
      mWidgetType = ((String)Preconditions.checkNotNull(paramString2));
    }
    
    public TextClassificationContext build()
    {
      return new TextClassificationContext(mPackageName, mWidgetType, mWidgetVersion, null);
    }
    
    public Builder setWidgetVersion(String paramString)
    {
      mWidgetVersion = paramString;
      return this;
    }
  }
}
