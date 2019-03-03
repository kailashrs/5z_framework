package android.service.chooser;

import android.content.ComponentName;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class ChooserTarget
  implements Parcelable
{
  public static final Parcelable.Creator<ChooserTarget> CREATOR = new Parcelable.Creator()
  {
    public ChooserTarget createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ChooserTarget(paramAnonymousParcel);
    }
    
    public ChooserTarget[] newArray(int paramAnonymousInt)
    {
      return new ChooserTarget[paramAnonymousInt];
    }
  };
  private static final String TAG = "ChooserTarget";
  private ComponentName mComponentName;
  private Icon mIcon;
  private Bundle mIntentExtras;
  private float mScore;
  private CharSequence mTitle;
  
  ChooserTarget(Parcel paramParcel)
  {
    mTitle = paramParcel.readCharSequence();
    if (paramParcel.readInt() != 0) {
      mIcon = ((Icon)Icon.CREATOR.createFromParcel(paramParcel));
    } else {
      mIcon = null;
    }
    mScore = paramParcel.readFloat();
    mComponentName = ComponentName.readFromParcel(paramParcel);
    mIntentExtras = paramParcel.readBundle();
  }
  
  public ChooserTarget(CharSequence paramCharSequence, Icon paramIcon, float paramFloat, ComponentName paramComponentName, Bundle paramBundle)
  {
    mTitle = paramCharSequence;
    mIcon = paramIcon;
    if ((paramFloat <= 1.0F) && (paramFloat >= 0.0F))
    {
      mScore = paramFloat;
      mComponentName = paramComponentName;
      mIntentExtras = paramBundle;
      return;
    }
    paramCharSequence = new StringBuilder();
    paramCharSequence.append("Score ");
    paramCharSequence.append(paramFloat);
    paramCharSequence.append(" out of range; must be between 0.0f and 1.0f");
    throw new IllegalArgumentException(paramCharSequence.toString());
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public ComponentName getComponentName()
  {
    return mComponentName;
  }
  
  public Icon getIcon()
  {
    return mIcon;
  }
  
  public Bundle getIntentExtras()
  {
    return mIntentExtras;
  }
  
  public float getScore()
  {
    return mScore;
  }
  
  public CharSequence getTitle()
  {
    return mTitle;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ChooserTarget{");
    localStringBuilder.append(mComponentName);
    localStringBuilder.append(", ");
    localStringBuilder.append(mIntentExtras);
    localStringBuilder.append(", '");
    localStringBuilder.append(mTitle);
    localStringBuilder.append("', ");
    localStringBuilder.append(mScore);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeCharSequence(mTitle);
    if (mIcon != null)
    {
      paramParcel.writeInt(1);
      mIcon.writeToParcel(paramParcel, 0);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeFloat(mScore);
    ComponentName.writeToParcel(mComponentName, paramParcel);
    paramParcel.writeBundle(mIntentExtras);
  }
}
