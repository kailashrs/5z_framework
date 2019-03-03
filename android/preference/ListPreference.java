package android.preference;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.AttributeSet;
import com.android.internal.R.styleable;

public class ListPreference
  extends DialogPreference
{
  private int mClickedDialogEntryIndex;
  private CharSequence[] mEntries;
  private CharSequence[] mEntryValues;
  private String mSummary;
  private String mValue;
  private boolean mValueSet;
  
  public ListPreference(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public ListPreference(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842897);
  }
  
  public ListPreference(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public ListPreference(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ListPreference, paramInt1, paramInt2);
    mEntries = localTypedArray.getTextArray(0);
    mEntryValues = localTypedArray.getTextArray(1);
    localTypedArray.recycle();
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.Preference, paramInt1, paramInt2);
    mSummary = paramContext.getString(7);
    paramContext.recycle();
  }
  
  private int getValueIndex()
  {
    return findIndexOfValue(mValue);
  }
  
  public int findIndexOfValue(String paramString)
  {
    if ((paramString != null) && (mEntryValues != null)) {
      for (int i = mEntryValues.length - 1; i >= 0; i--) {
        if (mEntryValues[i].equals(paramString)) {
          return i;
        }
      }
    }
    return -1;
  }
  
  public CharSequence[] getEntries()
  {
    return mEntries;
  }
  
  public CharSequence getEntry()
  {
    int i = getValueIndex();
    CharSequence localCharSequence;
    if ((i >= 0) && (mEntries != null)) {
      localCharSequence = mEntries[i];
    } else {
      localCharSequence = null;
    }
    return localCharSequence;
  }
  
  public CharSequence[] getEntryValues()
  {
    return mEntryValues;
  }
  
  public CharSequence getSummary()
  {
    Object localObject = getEntry();
    if (mSummary == null) {
      return super.getSummary();
    }
    String str = mSummary;
    if (localObject == null) {
      localObject = "";
    }
    return String.format(str, new Object[] { localObject });
  }
  
  public String getValue()
  {
    return mValue;
  }
  
  protected void onDialogClosed(boolean paramBoolean)
  {
    super.onDialogClosed(paramBoolean);
    if ((paramBoolean) && (mClickedDialogEntryIndex >= 0) && (mEntryValues != null))
    {
      String str = mEntryValues[mClickedDialogEntryIndex].toString();
      if (callChangeListener(str)) {
        setValue(str);
      }
    }
  }
  
  protected Object onGetDefaultValue(TypedArray paramTypedArray, int paramInt)
  {
    return paramTypedArray.getString(paramInt);
  }
  
  protected void onPrepareDialogBuilder(AlertDialog.Builder paramBuilder)
  {
    super.onPrepareDialogBuilder(paramBuilder);
    if ((mEntries != null) && (mEntryValues != null))
    {
      mClickedDialogEntryIndex = getValueIndex();
      paramBuilder.setSingleChoiceItems(mEntries, mClickedDialogEntryIndex, new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          ListPreference.access$002(ListPreference.this, paramAnonymousInt);
          ListPreference.this.onClick(paramAnonymousDialogInterface, -1);
          paramAnonymousDialogInterface.dismiss();
        }
      });
      paramBuilder.setPositiveButton(null, null);
      return;
    }
    throw new IllegalStateException("ListPreference requires an entries array and an entryValues array.");
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if ((paramParcelable != null) && (paramParcelable.getClass().equals(SavedState.class)))
    {
      paramParcelable = (SavedState)paramParcelable;
      super.onRestoreInstanceState(paramParcelable.getSuperState());
      setValue(value);
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
    value = getValue();
    return localObject;
  }
  
  protected void onSetInitialValue(boolean paramBoolean, Object paramObject)
  {
    if (paramBoolean) {
      paramObject = getPersistedString(mValue);
    } else {
      paramObject = (String)paramObject;
    }
    setValue(paramObject);
  }
  
  public void setEntries(int paramInt)
  {
    setEntries(getContext().getResources().getTextArray(paramInt));
  }
  
  public void setEntries(CharSequence[] paramArrayOfCharSequence)
  {
    mEntries = paramArrayOfCharSequence;
  }
  
  public void setEntryValues(int paramInt)
  {
    setEntryValues(getContext().getResources().getTextArray(paramInt));
  }
  
  public void setEntryValues(CharSequence[] paramArrayOfCharSequence)
  {
    mEntryValues = paramArrayOfCharSequence;
  }
  
  public void setSummary(CharSequence paramCharSequence)
  {
    super.setSummary(paramCharSequence);
    if ((paramCharSequence == null) && (mSummary != null)) {
      mSummary = null;
    } else if ((paramCharSequence != null) && (!paramCharSequence.equals(mSummary))) {
      mSummary = paramCharSequence.toString();
    }
  }
  
  public void setValue(String paramString)
  {
    boolean bool = TextUtils.equals(mValue, paramString) ^ true;
    if ((bool) || (!mValueSet))
    {
      mValue = paramString;
      mValueSet = true;
      persistString(paramString);
      if (bool) {
        notifyChanged();
      }
    }
  }
  
  public void setValueIndex(int paramInt)
  {
    if (mEntryValues != null) {
      setValue(mEntryValues[paramInt].toString());
    }
  }
  
  private static class SavedState
    extends Preference.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public ListPreference.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ListPreference.SavedState(paramAnonymousParcel);
      }
      
      public ListPreference.SavedState[] newArray(int paramAnonymousInt)
      {
        return new ListPreference.SavedState[paramAnonymousInt];
      }
    };
    String value;
    
    public SavedState(Parcel paramParcel)
    {
      super();
      value = paramParcel.readString();
    }
    
    public SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeString(value);
    }
  }
}
