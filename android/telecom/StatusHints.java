package android.telecom;

import android.annotation.SystemApi;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public final class StatusHints
  implements Parcelable
{
  public static final Parcelable.Creator<StatusHints> CREATOR = new Parcelable.Creator()
  {
    public StatusHints createFromParcel(Parcel paramAnonymousParcel)
    {
      return new StatusHints(paramAnonymousParcel, null);
    }
    
    public StatusHints[] newArray(int paramAnonymousInt)
    {
      return new StatusHints[paramAnonymousInt];
    }
  };
  private final Bundle mExtras;
  private final Icon mIcon;
  private final CharSequence mLabel;
  
  @SystemApi
  @Deprecated
  public StatusHints(ComponentName paramComponentName, CharSequence paramCharSequence, int paramInt, Bundle paramBundle)
  {
    this(paramCharSequence, paramComponentName, paramBundle);
  }
  
  private StatusHints(Parcel paramParcel)
  {
    mLabel = paramParcel.readCharSequence();
    mIcon = ((Icon)paramParcel.readParcelable(getClass().getClassLoader()));
    mExtras = ((Bundle)paramParcel.readParcelable(getClass().getClassLoader()));
  }
  
  public StatusHints(CharSequence paramCharSequence, Icon paramIcon, Bundle paramBundle)
  {
    mLabel = paramCharSequence;
    mIcon = paramIcon;
    mExtras = paramBundle;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = false;
    if ((paramObject != null) && ((paramObject instanceof StatusHints)))
    {
      paramObject = (StatusHints)paramObject;
      boolean bool2 = bool1;
      if (Objects.equals(paramObject.getLabel(), getLabel()))
      {
        bool2 = bool1;
        if (Objects.equals(paramObject.getIcon(), getIcon()))
        {
          bool2 = bool1;
          if (Objects.equals(paramObject.getExtras(), getExtras())) {
            bool2 = true;
          }
        }
      }
      return bool2;
    }
    return false;
  }
  
  public Bundle getExtras()
  {
    return mExtras;
  }
  
  @SystemApi
  @Deprecated
  public Drawable getIcon(Context paramContext)
  {
    return mIcon.loadDrawable(paramContext);
  }
  
  public Icon getIcon()
  {
    return mIcon;
  }
  
  @SystemApi
  @Deprecated
  public int getIconResId()
  {
    return 0;
  }
  
  public CharSequence getLabel()
  {
    return mLabel;
  }
  
  @SystemApi
  @Deprecated
  public ComponentName getPackageName()
  {
    return new ComponentName("", "");
  }
  
  public int hashCode()
  {
    return Objects.hashCode(mLabel) + Objects.hashCode(mIcon) + Objects.hashCode(mExtras);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeCharSequence(mLabel);
    paramParcel.writeParcelable(mIcon, 0);
    paramParcel.writeParcelable(mExtras, 0);
  }
}
