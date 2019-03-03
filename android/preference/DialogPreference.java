package android.preference;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.android.internal.R.styleable;

public abstract class DialogPreference
  extends Preference
  implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener, PreferenceManager.OnActivityDestroyListener
{
  private AlertDialog.Builder mBuilder;
  private Dialog mDialog;
  private Drawable mDialogIcon;
  private int mDialogLayoutResId;
  private CharSequence mDialogMessage;
  private CharSequence mDialogTitle;
  private CharSequence mNegativeButtonText;
  private CharSequence mPositiveButtonText;
  private int mWhichButtonClicked;
  
  public DialogPreference(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public DialogPreference(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842897);
  }
  
  public DialogPreference(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public DialogPreference(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.DialogPreference, paramInt1, paramInt2);
    mDialogTitle = paramContext.getString(0);
    if (mDialogTitle == null) {
      mDialogTitle = getTitle();
    }
    mDialogMessage = paramContext.getString(1);
    mDialogIcon = paramContext.getDrawable(2);
    mPositiveButtonText = paramContext.getString(3);
    mNegativeButtonText = paramContext.getString(4);
    mDialogLayoutResId = paramContext.getResourceId(5, mDialogLayoutResId);
    paramContext.recycle();
  }
  
  private void requestInputMethod(Dialog paramDialog)
  {
    paramDialog.getWindow().setSoftInputMode(5);
  }
  
  public Dialog getDialog()
  {
    return mDialog;
  }
  
  public Drawable getDialogIcon()
  {
    return mDialogIcon;
  }
  
  public int getDialogLayoutResource()
  {
    return mDialogLayoutResId;
  }
  
  public CharSequence getDialogMessage()
  {
    return mDialogMessage;
  }
  
  public CharSequence getDialogTitle()
  {
    return mDialogTitle;
  }
  
  public CharSequence getNegativeButtonText()
  {
    return mNegativeButtonText;
  }
  
  public CharSequence getPositiveButtonText()
  {
    return mPositiveButtonText;
  }
  
  protected boolean needInputMethod()
  {
    return false;
  }
  
  public void onActivityDestroy()
  {
    if ((mDialog != null) && (mDialog.isShowing()))
    {
      mDialog.dismiss();
      return;
    }
  }
  
  protected void onBindDialogView(View paramView)
  {
    View localView = paramView.findViewById(16908299);
    if (localView != null)
    {
      paramView = getDialogMessage();
      int i = 8;
      if (!TextUtils.isEmpty(paramView))
      {
        if ((localView instanceof TextView)) {
          ((TextView)localView).setText(paramView);
        }
        i = 0;
      }
      if (localView.getVisibility() != i) {
        localView.setVisibility(i);
      }
    }
  }
  
  protected void onClick()
  {
    if ((mDialog != null) && (mDialog.isShowing())) {
      return;
    }
    showDialog(null);
  }
  
  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    mWhichButtonClicked = paramInt;
  }
  
  protected View onCreateDialogView()
  {
    if (mDialogLayoutResId == 0) {
      return null;
    }
    return LayoutInflater.from(mBuilder.getContext()).inflate(mDialogLayoutResId, null);
  }
  
  protected void onDialogClosed(boolean paramBoolean) {}
  
  public void onDismiss(DialogInterface paramDialogInterface)
  {
    getPreferenceManager().unregisterOnActivityDestroyListener(this);
    mDialog = null;
    boolean bool;
    if (mWhichButtonClicked == -1) {
      bool = true;
    } else {
      bool = false;
    }
    onDialogClosed(bool);
  }
  
  protected void onPrepareDialogBuilder(AlertDialog.Builder paramBuilder) {}
  
  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if ((paramParcelable != null) && (paramParcelable.getClass().equals(SavedState.class)))
    {
      paramParcelable = (SavedState)paramParcelable;
      super.onRestoreInstanceState(paramParcelable.getSuperState());
      if (isDialogShowing) {
        showDialog(dialogBundle);
      }
      return;
    }
    super.onRestoreInstanceState(paramParcelable);
  }
  
  protected Parcelable onSaveInstanceState()
  {
    Object localObject = super.onSaveInstanceState();
    if ((mDialog != null) && (mDialog.isShowing()))
    {
      localObject = new SavedState((Parcelable)localObject);
      isDialogShowing = true;
      dialogBundle = mDialog.onSaveInstanceState();
      return localObject;
    }
    return localObject;
  }
  
  public void setDialogIcon(int paramInt)
  {
    mDialogIcon = getContext().getDrawable(paramInt);
  }
  
  public void setDialogIcon(Drawable paramDrawable)
  {
    mDialogIcon = paramDrawable;
  }
  
  public void setDialogLayoutResource(int paramInt)
  {
    mDialogLayoutResId = paramInt;
  }
  
  public void setDialogMessage(int paramInt)
  {
    setDialogMessage(getContext().getString(paramInt));
  }
  
  public void setDialogMessage(CharSequence paramCharSequence)
  {
    mDialogMessage = paramCharSequence;
  }
  
  public void setDialogTitle(int paramInt)
  {
    setDialogTitle(getContext().getString(paramInt));
  }
  
  public void setDialogTitle(CharSequence paramCharSequence)
  {
    mDialogTitle = paramCharSequence;
  }
  
  public void setNegativeButtonText(int paramInt)
  {
    setNegativeButtonText(getContext().getString(paramInt));
  }
  
  public void setNegativeButtonText(CharSequence paramCharSequence)
  {
    mNegativeButtonText = paramCharSequence;
  }
  
  public void setPositiveButtonText(int paramInt)
  {
    setPositiveButtonText(getContext().getString(paramInt));
  }
  
  public void setPositiveButtonText(CharSequence paramCharSequence)
  {
    mPositiveButtonText = paramCharSequence;
  }
  
  protected void showDialog(Bundle paramBundle)
  {
    Object localObject = getContext();
    mWhichButtonClicked = -2;
    mBuilder = new AlertDialog.Builder((Context)localObject).setTitle(mDialogTitle).setIcon(mDialogIcon).setPositiveButton(mPositiveButtonText, this).setNegativeButton(mNegativeButtonText, this);
    localObject = onCreateDialogView();
    if (localObject != null)
    {
      onBindDialogView((View)localObject);
      mBuilder.setView((View)localObject);
    }
    else
    {
      mBuilder.setMessage(mDialogMessage);
    }
    onPrepareDialogBuilder(mBuilder);
    getPreferenceManager().registerOnActivityDestroyListener(this);
    localObject = mBuilder.create();
    mDialog = ((Dialog)localObject);
    if (paramBundle != null) {
      ((Dialog)localObject).onRestoreInstanceState(paramBundle);
    }
    if (needInputMethod()) {
      requestInputMethod((Dialog)localObject);
    }
    ((Dialog)localObject).setOnDismissListener(this);
    ((Dialog)localObject).show();
  }
  
  private static class SavedState
    extends Preference.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public DialogPreference.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new DialogPreference.SavedState(paramAnonymousParcel);
      }
      
      public DialogPreference.SavedState[] newArray(int paramAnonymousInt)
      {
        return new DialogPreference.SavedState[paramAnonymousInt];
      }
    };
    Bundle dialogBundle;
    boolean isDialogShowing;
    
    public SavedState(Parcel paramParcel)
    {
      super();
      int i = paramParcel.readInt();
      boolean bool = true;
      if (i != 1) {
        bool = false;
      }
      isDialogShowing = bool;
      dialogBundle = paramParcel.readBundle();
    }
    
    public SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(isDialogShowing);
      paramParcel.writeBundle(dialogBundle);
    }
  }
}
