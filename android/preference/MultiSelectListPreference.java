package android.preference;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import com.android.internal.R.styleable;
import java.util.HashSet;
import java.util.Set;

public class MultiSelectListPreference
  extends DialogPreference
{
  private boolean[] mCheckedItems;
  private CharSequence[] mEntries;
  private CharSequence[] mEntryValues;
  private Set<String> mNewValues = new HashSet();
  private boolean mPreferenceChanged;
  private boolean mRestoreNewValue;
  private Set<String> mValues = new HashSet();
  
  public MultiSelectListPreference(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public MultiSelectListPreference(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842897);
  }
  
  public MultiSelectListPreference(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public MultiSelectListPreference(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.MultiSelectListPreference, paramInt1, paramInt2);
    mEntries = paramContext.getTextArray(0);
    mEntryValues = paramContext.getTextArray(1);
    paramContext.recycle();
  }
  
  private boolean[] getNewSelectedItems()
  {
    CharSequence[] arrayOfCharSequence = mEntryValues;
    int i = arrayOfCharSequence.length;
    Set localSet = mNewValues;
    boolean[] arrayOfBoolean = new boolean[i];
    for (int j = 0; j < i; j++) {
      arrayOfBoolean[j] = localSet.contains(arrayOfCharSequence[j].toString());
    }
    return arrayOfBoolean;
  }
  
  private boolean[] getSelectedItems()
  {
    CharSequence[] arrayOfCharSequence = mEntryValues;
    int i = arrayOfCharSequence.length;
    Set localSet = mValues;
    boolean[] arrayOfBoolean = new boolean[i];
    for (int j = 0; j < i; j++) {
      arrayOfBoolean[j] = localSet.contains(arrayOfCharSequence[j].toString());
    }
    return arrayOfBoolean;
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
  
  public CharSequence[] getEntryValues()
  {
    return mEntryValues;
  }
  
  public Set<String> getValues()
  {
    return mValues;
  }
  
  protected void onDialogClosed(boolean paramBoolean)
  {
    super.onDialogClosed(paramBoolean);
    if ((paramBoolean) && (mPreferenceChanged))
    {
      Set localSet = mNewValues;
      if (callChangeListener(localSet)) {
        setValues(localSet);
      }
    }
    mPreferenceChanged = false;
  }
  
  protected Object onGetDefaultValue(TypedArray paramTypedArray, int paramInt)
  {
    CharSequence[] arrayOfCharSequence = paramTypedArray.getTextArray(paramInt);
    int i = arrayOfCharSequence.length;
    paramTypedArray = new HashSet();
    for (paramInt = 0; paramInt < i; paramInt++) {
      paramTypedArray.add(arrayOfCharSequence[paramInt].toString());
    }
    return paramTypedArray;
  }
  
  protected void onPrepareDialogBuilder(AlertDialog.Builder paramBuilder)
  {
    super.onPrepareDialogBuilder(paramBuilder);
    if ((mEntries != null) && (mEntryValues != null))
    {
      if (!mRestoreNewValue)
      {
        mCheckedItems = getSelectedItems();
        mNewValues.clear();
        mNewValues.addAll(mValues);
      }
      else
      {
        mCheckedItems = getNewSelectedItems();
      }
      paramBuilder.setMultiChoiceItems(mEntries, mCheckedItems, new DialogInterface.OnMultiChoiceClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt, boolean paramAnonymousBoolean)
        {
          if (paramAnonymousBoolean) {
            MultiSelectListPreference.access$076(MultiSelectListPreference.this, mNewValues.add(mEntryValues[paramAnonymousInt].toString()));
          } else {
            MultiSelectListPreference.access$076(MultiSelectListPreference.this, mNewValues.remove(mEntryValues[paramAnonymousInt].toString()));
          }
        }
      });
      return;
    }
    throw new IllegalStateException("MultiSelectListPreference requires an entries array and an entryValues array.");
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    paramParcelable = (SavedState)paramParcelable;
    mNewValues.clear();
    mNewValues.addAll(newValues);
    mPreferenceChanged = preferenceChanged;
    mRestoreNewValue = restoreNewValue;
    if (!isPersistent()) {
      setValues(values);
    }
    super.onRestoreInstanceState(paramParcelable.getSuperState());
    mRestoreNewValue = false;
  }
  
  protected Parcelable onSaveInstanceState()
  {
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    newValues = mNewValues;
    preferenceChanged = mPreferenceChanged;
    mRestoreNewValue = true;
    restoreNewValue = true;
    values = getValues();
    return localSavedState;
  }
  
  protected void onSetInitialValue(boolean paramBoolean, Object paramObject)
  {
    if (paramBoolean) {
      paramObject = getPersistedStringSet(mValues);
    } else {
      paramObject = (Set)paramObject;
    }
    setValues(paramObject);
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
  
  public void setValues(Set<String> paramSet)
  {
    mValues.clear();
    mValues.addAll(paramSet);
    persistStringSet(paramSet);
  }
  
  private static class SavedState
    extends Preference.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public MultiSelectListPreference.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new MultiSelectListPreference.SavedState(paramAnonymousParcel);
      }
      
      public MultiSelectListPreference.SavedState[] newArray(int paramAnonymousInt)
      {
        return new MultiSelectListPreference.SavedState[paramAnonymousInt];
      }
    };
    Set<String> newValues;
    boolean preferenceChanged;
    boolean restoreNewValue;
    Set<String> values;
    
    public SavedState(Parcel paramParcel)
    {
      super();
      newValues = new HashSet();
      String[] arrayOfString = paramParcel.readStringArray();
      int i = arrayOfString.length;
      int j = 0;
      for (int k = 0; k < i; k++) {
        newValues.add(arrayOfString[k]);
      }
      k = paramParcel.readInt();
      boolean bool1 = true;
      boolean bool2;
      if (k == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      preferenceChanged = bool2;
      if (paramParcel.readInt() == 1) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
      restoreNewValue = bool2;
      values = new HashSet();
      paramParcel = paramParcel.readStringArray();
      i = paramParcel.length;
      for (k = j; k < i; k++) {
        values.add(paramParcel[k]);
      }
    }
    
    public SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeStringArray((String[])newValues.toArray(new String[0]));
      paramParcel.writeInt(preferenceChanged);
      paramParcel.writeInt(restoreNewValue);
      paramParcel.writeStringArray((String[])values.toArray(new String[0]));
    }
  }
}
