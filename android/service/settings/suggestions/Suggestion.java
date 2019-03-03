package android.service.settings.suggestions;

import android.annotation.SystemApi;
import android.app.PendingIntent;
import android.graphics.drawable.Icon;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SystemApi
public final class Suggestion
  implements Parcelable
{
  public static final Parcelable.Creator<Suggestion> CREATOR = new Parcelable.Creator()
  {
    public Suggestion createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Suggestion(paramAnonymousParcel, null);
    }
    
    public Suggestion[] newArray(int paramAnonymousInt)
    {
      return new Suggestion[paramAnonymousInt];
    }
  };
  public static final int FLAG_HAS_BUTTON = 1;
  public static final int FLAG_ICON_TINTABLE = 2;
  private final int mFlags;
  private final Icon mIcon;
  private final String mId;
  private final PendingIntent mPendingIntent;
  private final CharSequence mSummary;
  private final CharSequence mTitle;
  
  private Suggestion(Parcel paramParcel)
  {
    mId = paramParcel.readString();
    mTitle = paramParcel.readCharSequence();
    mSummary = paramParcel.readCharSequence();
    mIcon = ((Icon)paramParcel.readParcelable(Icon.class.getClassLoader()));
    mFlags = paramParcel.readInt();
    mPendingIntent = ((PendingIntent)paramParcel.readParcelable(PendingIntent.class.getClassLoader()));
  }
  
  private Suggestion(Builder paramBuilder)
  {
    mId = mId;
    mTitle = mTitle;
    mSummary = mSummary;
    mIcon = mIcon;
    mFlags = mFlags;
    mPendingIntent = mPendingIntent;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getFlags()
  {
    return mFlags;
  }
  
  public Icon getIcon()
  {
    return mIcon;
  }
  
  public String getId()
  {
    return mId;
  }
  
  public PendingIntent getPendingIntent()
  {
    return mPendingIntent;
  }
  
  public CharSequence getSummary()
  {
    return mSummary;
  }
  
  public CharSequence getTitle()
  {
    return mTitle;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mId);
    paramParcel.writeCharSequence(mTitle);
    paramParcel.writeCharSequence(mSummary);
    paramParcel.writeParcelable(mIcon, paramInt);
    paramParcel.writeInt(mFlags);
    paramParcel.writeParcelable(mPendingIntent, paramInt);
  }
  
  public static class Builder
  {
    private int mFlags;
    private Icon mIcon;
    private final String mId;
    private PendingIntent mPendingIntent;
    private CharSequence mSummary;
    private CharSequence mTitle;
    
    public Builder(String paramString)
    {
      if (!TextUtils.isEmpty(paramString))
      {
        mId = paramString;
        return;
      }
      throw new IllegalArgumentException("Suggestion id cannot be empty");
    }
    
    public Suggestion build()
    {
      return new Suggestion(this, null);
    }
    
    public Builder setFlags(int paramInt)
    {
      mFlags = paramInt;
      return this;
    }
    
    public Builder setIcon(Icon paramIcon)
    {
      mIcon = paramIcon;
      return this;
    }
    
    public Builder setPendingIntent(PendingIntent paramPendingIntent)
    {
      mPendingIntent = paramPendingIntent;
      return this;
    }
    
    public Builder setSummary(CharSequence paramCharSequence)
    {
      mSummary = paramCharSequence;
      return this;
    }
    
    public Builder setTitle(CharSequence paramCharSequence)
    {
      mTitle = paramCharSequence;
      return this;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Flags {}
}
