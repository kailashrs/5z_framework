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
import java.util.Arrays;

public class MultiCheckPreference
  extends DialogPreference
{
  private CharSequence[] mEntries;
  private String[] mEntryValues;
  private boolean[] mOrigValues;
  private boolean[] mSetValues;
  private String mSummary;
  
  public MultiCheckPreference(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public MultiCheckPreference(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842897);
  }
  
  public MultiCheckPreference(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public MultiCheckPreference(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ListPreference, paramInt1, paramInt2);
    mEntries = localTypedArray.getTextArray(0);
    if (mEntries != null) {
      setEntries(mEntries);
    }
    setEntryValuesCS(localTypedArray.getTextArray(1));
    localTypedArray.recycle();
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.Preference, 0, 0);
    mSummary = paramContext.getString(7);
    paramContext.recycle();
  }
  
  private void setEntryValuesCS(CharSequence[] paramArrayOfCharSequence)
  {
    setValues(null);
    if (paramArrayOfCharSequence != null)
    {
      mEntryValues = new String[paramArrayOfCharSequence.length];
      for (int i = 0; i < paramArrayOfCharSequence.length; i++) {
        mEntryValues[i] = paramArrayOfCharSequence[i].toString();
      }
    }
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
  
  public String[] getEntryValues()
  {
    return mEntryValues;
  }
  
  public CharSequence getSummary()
  {
    if (mSummary == null) {
      return super.getSummary();
    }
    return mSummary;
  }
  
  public boolean getValue(int paramInt)
  {
    return mSetValues[paramInt];
  }
  
  public boolean[] getValues()
  {
    return mSetValues;
  }
  
  protected void onDialogClosed(boolean paramBoolean)
  {
    super.onDialogClosed(paramBoolean);
    if ((paramBoolean) && (callChangeListener(getValues()))) {
      return;
    }
    System.arraycopy(mOrigValues, 0, mSetValues, 0, mSetValues.length);
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
      mOrigValues = Arrays.copyOf(mSetValues, mSetValues.length);
      paramBuilder.setMultiChoiceItems(mEntries, mSetValues, new DialogInterface.OnMultiChoiceClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt, boolean paramAnonymousBoolean)
        {
          mSetValues[paramAnonymousInt] = paramAnonymousBoolean;
        }
      });
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
      setValues(values);
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
    values = getValues();
    return localObject;
  }
  
  protected void onSetInitialValue(boolean paramBoolean, Object paramObject) {}
  
  public void setEntries(int paramInt)
  {
    setEntries(getContext().getResources().getTextArray(paramInt));
  }
  
  public void setEntries(CharSequence[] paramArrayOfCharSequence)
  {
    mEntries = paramArrayOfCharSequence;
    mSetValues = new boolean[paramArrayOfCharSequence.length];
    mOrigValues = new boolean[paramArrayOfCharSequence.length];
  }
  
  public void setEntryValues(int paramInt)
  {
    setEntryValuesCS(getContext().getResources().getTextArray(paramInt));
  }
  
  public void setEntryValues(String[] paramArrayOfString)
  {
    mEntryValues = paramArrayOfString;
    Arrays.fill(mSetValues, false);
    Arrays.fill(mOrigValues, false);
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
  
  public void setValue(int paramInt, boolean paramBoolean)
  {
    mSetValues[paramInt] = paramBoolean;
  }
  
  public void setValues(boolean[] paramArrayOfBoolean)
  {
    if (mSetValues != null)
    {
      Arrays.fill(mSetValues, false);
      Arrays.fill(mOrigValues, false);
      if (paramArrayOfBoolean != null)
      {
        boolean[] arrayOfBoolean = mSetValues;
        int i;
        if (paramArrayOfBoolean.length < mSetValues.length) {
          i = paramArrayOfBoolean.length;
        } else {
          i = mSetValues.length;
        }
        System.arraycopy(paramArrayOfBoolean, 0, arrayOfBoolean, 0, i);
      }
    }
  }
  
  private static class SavedState
    extends Preference.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public MultiCheckPreference.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new MultiCheckPreference.SavedState(paramAnonymousParcel);
      }
      
      public MultiCheckPreference.SavedState[] newArray(int paramAnonymousInt)
      {
        return new MultiCheckPreference.SavedState[paramAnonymousInt];
      }
    };
    boolean[] values;
    
    public SavedState(Parcel paramParcel)
    {
      super();
      values = paramParcel.createBooleanArray();
    }
    
    public SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeBooleanArray(values);
    }
  }
}
