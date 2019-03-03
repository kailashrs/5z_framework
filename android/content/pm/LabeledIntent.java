package android.content.pm;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;

public class LabeledIntent
  extends Intent
{
  public static final Parcelable.Creator<LabeledIntent> CREATOR = new Parcelable.Creator()
  {
    public LabeledIntent createFromParcel(Parcel paramAnonymousParcel)
    {
      return new LabeledIntent(paramAnonymousParcel);
    }
    
    public LabeledIntent[] newArray(int paramAnonymousInt)
    {
      return new LabeledIntent[paramAnonymousInt];
    }
  };
  private int mIcon;
  private int mLabelRes;
  private CharSequence mNonLocalizedLabel;
  private String mSourcePackage;
  
  public LabeledIntent(Intent paramIntent, String paramString, int paramInt1, int paramInt2)
  {
    super(paramIntent);
    mSourcePackage = paramString;
    mLabelRes = paramInt1;
    mNonLocalizedLabel = null;
    mIcon = paramInt2;
  }
  
  public LabeledIntent(Intent paramIntent, String paramString, CharSequence paramCharSequence, int paramInt)
  {
    super(paramIntent);
    mSourcePackage = paramString;
    mLabelRes = 0;
    mNonLocalizedLabel = paramCharSequence;
    mIcon = paramInt;
  }
  
  protected LabeledIntent(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public LabeledIntent(String paramString, int paramInt1, int paramInt2)
  {
    mSourcePackage = paramString;
    mLabelRes = paramInt1;
    mNonLocalizedLabel = null;
    mIcon = paramInt2;
  }
  
  public LabeledIntent(String paramString, CharSequence paramCharSequence, int paramInt)
  {
    mSourcePackage = paramString;
    mLabelRes = 0;
    mNonLocalizedLabel = paramCharSequence;
    mIcon = paramInt;
  }
  
  public int getIconResource()
  {
    return mIcon;
  }
  
  public int getLabelResource()
  {
    return mLabelRes;
  }
  
  public CharSequence getNonLocalizedLabel()
  {
    return mNonLocalizedLabel;
  }
  
  public String getSourcePackage()
  {
    return mSourcePackage;
  }
  
  public Drawable loadIcon(PackageManager paramPackageManager)
  {
    if ((mIcon != 0) && (mSourcePackage != null))
    {
      paramPackageManager = paramPackageManager.getDrawable(mSourcePackage, mIcon, null);
      if (paramPackageManager != null) {
        return paramPackageManager;
      }
    }
    return null;
  }
  
  public CharSequence loadLabel(PackageManager paramPackageManager)
  {
    if (mNonLocalizedLabel != null) {
      return mNonLocalizedLabel;
    }
    if ((mLabelRes != 0) && (mSourcePackage != null))
    {
      paramPackageManager = paramPackageManager.getText(mSourcePackage, mLabelRes, null);
      if (paramPackageManager != null) {
        return paramPackageManager;
      }
    }
    return null;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    super.readFromParcel(paramParcel);
    mSourcePackage = paramParcel.readString();
    mLabelRes = paramParcel.readInt();
    mNonLocalizedLabel = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    mIcon = paramParcel.readInt();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, paramInt);
    paramParcel.writeString(mSourcePackage);
    paramParcel.writeInt(mLabelRes);
    TextUtils.writeToParcel(mNonLocalizedLabel, paramParcel, paramInt);
    paramParcel.writeInt(mIcon);
  }
}
