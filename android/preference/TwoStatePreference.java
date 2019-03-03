package android.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public abstract class TwoStatePreference
  extends Preference
{
  boolean mChecked;
  private boolean mCheckedSet;
  private boolean mDisableDependentsState;
  private CharSequence mSummaryOff;
  private CharSequence mSummaryOn;
  
  public TwoStatePreference(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public TwoStatePreference(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public TwoStatePreference(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public TwoStatePreference(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  public boolean getDisableDependentsState()
  {
    return mDisableDependentsState;
  }
  
  public CharSequence getSummaryOff()
  {
    return mSummaryOff;
  }
  
  public CharSequence getSummaryOn()
  {
    return mSummaryOn;
  }
  
  public boolean isChecked()
  {
    return mChecked;
  }
  
  protected void onClick()
  {
    super.onClick();
    boolean bool = isChecked() ^ true;
    if (callChangeListener(Boolean.valueOf(bool))) {
      setChecked(bool);
    }
  }
  
  protected Object onGetDefaultValue(TypedArray paramTypedArray, int paramInt)
  {
    return Boolean.valueOf(paramTypedArray.getBoolean(paramInt, false));
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if ((paramParcelable != null) && (paramParcelable.getClass().equals(SavedState.class)))
    {
      paramParcelable = (SavedState)paramParcelable;
      super.onRestoreInstanceState(paramParcelable.getSuperState());
      setChecked(checked);
      return;
    }
    super.onRestoreInstanceState(paramParcelable);
  }
  
  protected Parcelable onSaveInstanceState()
  {
    Object localObject = super.onSaveInstanceState();
    if (isPersistent()) {
      return localObject;
    }
    localObject = new SavedState((Parcelable)localObject);
    checked = isChecked();
    return localObject;
  }
  
  protected void onSetInitialValue(boolean paramBoolean, Object paramObject)
  {
    if (paramBoolean) {
      paramBoolean = getPersistedBoolean(mChecked);
    } else {
      paramBoolean = ((Boolean)paramObject).booleanValue();
    }
    setChecked(paramBoolean);
  }
  
  public void setChecked(boolean paramBoolean)
  {
    int i;
    if (mChecked != paramBoolean) {
      i = 1;
    } else {
      i = 0;
    }
    if ((i != 0) || (!mCheckedSet))
    {
      mChecked = paramBoolean;
      mCheckedSet = true;
      persistBoolean(paramBoolean);
      if (i != 0)
      {
        notifyDependencyChange(shouldDisableDependents());
        notifyChanged();
      }
    }
  }
  
  public void setDisableDependentsState(boolean paramBoolean)
  {
    mDisableDependentsState = paramBoolean;
  }
  
  public void setSummaryOff(int paramInt)
  {
    setSummaryOff(getContext().getString(paramInt));
  }
  
  public void setSummaryOff(CharSequence paramCharSequence)
  {
    mSummaryOff = paramCharSequence;
    if (!isChecked()) {
      notifyChanged();
    }
  }
  
  public void setSummaryOn(int paramInt)
  {
    setSummaryOn(getContext().getString(paramInt));
  }
  
  public void setSummaryOn(CharSequence paramCharSequence)
  {
    mSummaryOn = paramCharSequence;
    if (isChecked()) {
      notifyChanged();
    }
  }
  
  public boolean shouldDisableDependents()
  {
    boolean bool1 = mDisableDependentsState;
    boolean bool2 = false;
    if (bool1) {
      bool1 = mChecked;
    } else if (!mChecked) {
      bool1 = true;
    } else {
      bool1 = false;
    }
    if ((!bool1) && (!super.shouldDisableDependents())) {
      bool1 = bool2;
    } else {
      bool1 = true;
    }
    return bool1;
  }
  
  void syncSummaryView(View paramView)
  {
    paramView = (TextView)paramView.findViewById(16908304);
    if (paramView != null)
    {
      int i = 1;
      if ((mChecked) && (!TextUtils.isEmpty(mSummaryOn)))
      {
        paramView.setText(mSummaryOn);
        j = 0;
      }
      else
      {
        j = i;
        if (!mChecked)
        {
          j = i;
          if (!TextUtils.isEmpty(mSummaryOff))
          {
            paramView.setText(mSummaryOff);
            j = 0;
          }
        }
      }
      i = j;
      if (j != 0)
      {
        CharSequence localCharSequence = getSummary();
        i = j;
        if (!TextUtils.isEmpty(localCharSequence))
        {
          paramView.setText(localCharSequence);
          i = 0;
        }
      }
      int j = 8;
      if (i == 0) {
        j = 0;
      }
      if (j != paramView.getVisibility()) {
        paramView.setVisibility(j);
      }
    }
  }
  
  static class SavedState
    extends Preference.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public TwoStatePreference.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new TwoStatePreference.SavedState(paramAnonymousParcel);
      }
      
      public TwoStatePreference.SavedState[] newArray(int paramAnonymousInt)
      {
        return new TwoStatePreference.SavedState[paramAnonymousInt];
      }
    };
    boolean checked;
    
    public SavedState(Parcel paramParcel)
    {
      super();
      int i = paramParcel.readInt();
      boolean bool = true;
      if (i != 1) {
        bool = false;
      }
      checked = bool;
    }
    
    public SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(checked);
    }
  }
}
