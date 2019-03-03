package android.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;

public class EditTextPreference
  extends DialogPreference
{
  private EditText mEditText;
  private String mText;
  private boolean mTextSet;
  
  public EditTextPreference(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public EditTextPreference(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842898);
  }
  
  public EditTextPreference(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public EditTextPreference(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    mEditText = new EditText(paramContext, paramAttributeSet);
    mEditText.setId(16908291);
    mEditText.setEnabled(true);
  }
  
  public EditText getEditText()
  {
    return mEditText;
  }
  
  public String getText()
  {
    return mText;
  }
  
  protected boolean needInputMethod()
  {
    return true;
  }
  
  protected void onAddEditTextToDialogView(View paramView, EditText paramEditText)
  {
    paramView = (ViewGroup)paramView.findViewById(16908914);
    if (paramView != null) {
      paramView.addView(paramEditText, -1, -2);
    }
  }
  
  protected void onBindDialogView(View paramView)
  {
    super.onBindDialogView(paramView);
    EditText localEditText = mEditText;
    localEditText.setText(getText());
    ViewParent localViewParent = localEditText.getParent();
    if (localViewParent != paramView)
    {
      if (localViewParent != null) {
        ((ViewGroup)localViewParent).removeView(localEditText);
      }
      onAddEditTextToDialogView(paramView, localEditText);
    }
  }
  
  protected void onDialogClosed(boolean paramBoolean)
  {
    super.onDialogClosed(paramBoolean);
    if (paramBoolean)
    {
      String str = mEditText.getText().toString();
      if (callChangeListener(str)) {
        setText(str);
      }
    }
  }
  
  protected Object onGetDefaultValue(TypedArray paramTypedArray, int paramInt)
  {
    return paramTypedArray.getString(paramInt);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if ((paramParcelable != null) && (paramParcelable.getClass().equals(SavedState.class)))
    {
      paramParcelable = (SavedState)paramParcelable;
      super.onRestoreInstanceState(paramParcelable.getSuperState());
      setText(text);
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
    text = getText();
    return localObject;
  }
  
  protected void onSetInitialValue(boolean paramBoolean, Object paramObject)
  {
    if (paramBoolean) {
      paramObject = getPersistedString(mText);
    } else {
      paramObject = (String)paramObject;
    }
    setText(paramObject);
  }
  
  public void setText(String paramString)
  {
    boolean bool = TextUtils.equals(mText, paramString) ^ true;
    if ((bool) || (!mTextSet))
    {
      mText = paramString;
      mTextSet = true;
      persistString(paramString);
      if (bool)
      {
        notifyDependencyChange(shouldDisableDependents());
        notifyChanged();
      }
    }
  }
  
  public boolean shouldDisableDependents()
  {
    boolean bool;
    if ((!TextUtils.isEmpty(mText)) && (!super.shouldDisableDependents())) {
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
      public EditTextPreference.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new EditTextPreference.SavedState(paramAnonymousParcel);
      }
      
      public EditTextPreference.SavedState[] newArray(int paramAnonymousInt)
      {
        return new EditTextPreference.SavedState[paramAnonymousInt];
      }
    };
    String text;
    
    public SavedState(Parcel paramParcel)
    {
      super();
      text = paramParcel.readString();
    }
    
    public SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeString(text);
    }
  }
}
