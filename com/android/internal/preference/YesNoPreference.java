package com.android.internal.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.preference.DialogPreference;
import android.preference.Preference.BaseSavedState;
import android.util.AttributeSet;

public class YesNoPreference
  extends DialogPreference
{
  private boolean mWasPositiveResult;
  
  public YesNoPreference(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public YesNoPreference(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842896);
  }
  
  public YesNoPreference(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public YesNoPreference(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  public boolean getValue()
  {
    return mWasPositiveResult;
  }
  
  protected void onDialogClosed(boolean paramBoolean)
  {
    super.onDialogClosed(paramBoolean);
    if (callChangeListener(Boolean.valueOf(paramBoolean))) {
      setValue(paramBoolean);
    }
  }
  
  protected Object onGetDefaultValue(TypedArray paramTypedArray, int paramInt)
  {
    return Boolean.valueOf(paramTypedArray.getBoolean(paramInt, false));
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if (!paramParcelable.getClass().equals(SavedState.class))
    {
      super.onRestoreInstanceState(paramParcelable);
      return;
    }
    paramParcelable = (SavedState)paramParcelable;
    super.onRestoreInstanceState(paramParcelable.getSuperState());
    setValue(wasPositiveResult);
  }
  
  protected Parcelable onSaveInstanceState()
  {
    Object localObject = super.onSaveInstanceState();
    if (isPersistent()) {
      return localObject;
    }
    localObject = new SavedState((Parcelable)localObject);
    wasPositiveResult = getValue();
    return localObject;
  }
  
  protected void onSetInitialValue(boolean paramBoolean, Object paramObject)
  {
    if (paramBoolean) {
      paramBoolean = getPersistedBoolean(mWasPositiveResult);
    } else {
      paramBoolean = ((Boolean)paramObject).booleanValue();
    }
    setValue(paramBoolean);
  }
  
  public void setValue(boolean paramBoolean)
  {
    mWasPositiveResult = paramBoolean;
    persistBoolean(paramBoolean);
    notifyDependencyChange(paramBoolean ^ true);
  }
  
  public boolean shouldDisableDependents()
  {
    boolean bool;
    if ((mWasPositiveResult) && (!super.shouldDisableDependents())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private static class SavedState
    extends Preference.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public YesNoPreference.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new YesNoPreference.SavedState(paramAnonymousParcel);
      }
      
      public YesNoPreference.SavedState[] newArray(int paramAnonymousInt)
      {
        return new YesNoPreference.SavedState[paramAnonymousInt];
      }
    };
    boolean wasPositiveResult;
    
    public SavedState(Parcel paramParcel)
    {
      super();
      int i = paramParcel.readInt();
      boolean bool = true;
      if (i != 1) {
        bool = false;
      }
      wasPositiveResult = bool;
    }
    
    public SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(wasPositiveResult);
    }
  }
}
