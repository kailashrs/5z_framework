package android.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.res.ResourceId;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.method.MovementMethod;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.android.internal.app.AlertController;
import com.android.internal.app.AlertController.AlertParams;

public class AlertDialog
  extends Dialog
  implements DialogInterface
{
  public static final int LAYOUT_HINT_NONE = 0;
  public static final int LAYOUT_HINT_SIDE = 1;
  @Deprecated
  public static final int THEME_DEVICE_DEFAULT_DARK = 4;
  @Deprecated
  public static final int THEME_DEVICE_DEFAULT_LIGHT = 5;
  @Deprecated
  public static final int THEME_HOLO_DARK = 2;
  @Deprecated
  public static final int THEME_HOLO_LIGHT = 3;
  @Deprecated
  public static final int THEME_TRADITIONAL = 1;
  private AlertController mAlert;
  
  protected AlertDialog(Context paramContext)
  {
    this(paramContext, 0);
  }
  
  protected AlertDialog(Context paramContext, int paramInt)
  {
    this(paramContext, paramInt, true);
  }
  
  AlertDialog(Context paramContext, int paramInt, boolean paramBoolean)
  {
    super(paramContext, paramInt, paramBoolean);
    mWindow.alwaysReadCloseOnTouchAttr();
    mAlert = AlertController.create(getContext(), this, getWindow());
  }
  
  protected AlertDialog(Context paramContext, boolean paramBoolean, DialogInterface.OnCancelListener paramOnCancelListener)
  {
    this(paramContext, 0);
    setCancelable(paramBoolean);
    setOnCancelListener(paramOnCancelListener);
  }
  
  static int resolveDialogTheme(Context paramContext, int paramInt)
  {
    if (paramInt == 1) {
      return 16975043;
    }
    if (paramInt == 2) {
      return 16975050;
    }
    if (paramInt == 3) {
      return 16975057;
    }
    if (paramInt == 4) {
      return 16974545;
    }
    if (paramInt == 5) {
      return 16974546;
    }
    if (ResourceId.isValid(paramInt)) {
      return paramInt;
    }
    TypedValue localTypedValue = new TypedValue();
    paramContext.getTheme().resolveAttribute(16843529, localTypedValue, true);
    return resourceId;
  }
  
  public Button getButton(int paramInt)
  {
    return mAlert.getButton(paramInt);
  }
  
  public ListView getListView()
  {
    return mAlert.getListView();
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    mAlert.installContent();
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (mAlert.onKeyDown(paramInt, paramKeyEvent)) {
      return true;
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    if (mAlert.onKeyUp(paramInt, paramKeyEvent)) {
      return true;
    }
    return super.onKeyUp(paramInt, paramKeyEvent);
  }
  
  public void setButton(int paramInt, CharSequence paramCharSequence, DialogInterface.OnClickListener paramOnClickListener)
  {
    mAlert.setButton(paramInt, paramCharSequence, paramOnClickListener, null);
  }
  
  public void setButton(int paramInt, CharSequence paramCharSequence, Message paramMessage)
  {
    mAlert.setButton(paramInt, paramCharSequence, null, paramMessage);
  }
  
  @Deprecated
  public void setButton(CharSequence paramCharSequence, DialogInterface.OnClickListener paramOnClickListener)
  {
    setButton(-1, paramCharSequence, paramOnClickListener);
  }
  
  @Deprecated
  public void setButton(CharSequence paramCharSequence, Message paramMessage)
  {
    setButton(-1, paramCharSequence, paramMessage);
  }
  
  @Deprecated
  public void setButton2(CharSequence paramCharSequence, DialogInterface.OnClickListener paramOnClickListener)
  {
    setButton(-2, paramCharSequence, paramOnClickListener);
  }
  
  @Deprecated
  public void setButton2(CharSequence paramCharSequence, Message paramMessage)
  {
    setButton(-2, paramCharSequence, paramMessage);
  }
  
  @Deprecated
  public void setButton3(CharSequence paramCharSequence, DialogInterface.OnClickListener paramOnClickListener)
  {
    setButton(-3, paramCharSequence, paramOnClickListener);
  }
  
  @Deprecated
  public void setButton3(CharSequence paramCharSequence, Message paramMessage)
  {
    setButton(-3, paramCharSequence, paramMessage);
  }
  
  void setButtonPanelLayoutHint(int paramInt)
  {
    mAlert.setButtonPanelLayoutHint(paramInt);
  }
  
  public void setCustomTitle(View paramView)
  {
    mAlert.setCustomTitle(paramView);
  }
  
  public void setIcon(int paramInt)
  {
    mAlert.setIcon(paramInt);
  }
  
  public void setIcon(Drawable paramDrawable)
  {
    mAlert.setIcon(paramDrawable);
  }
  
  public void setIconAttribute(int paramInt)
  {
    TypedValue localTypedValue = new TypedValue();
    mContext.getTheme().resolveAttribute(paramInt, localTypedValue, true);
    mAlert.setIcon(resourceId);
  }
  
  public void setInverseBackgroundForced(boolean paramBoolean)
  {
    mAlert.setInverseBackgroundForced(paramBoolean);
  }
  
  public void setMessage(CharSequence paramCharSequence)
  {
    mAlert.setMessage(paramCharSequence);
  }
  
  public void setMessageHyphenationFrequency(int paramInt)
  {
    mAlert.setMessageHyphenationFrequency(paramInt);
  }
  
  public void setMessageMovementMethod(MovementMethod paramMovementMethod)
  {
    mAlert.setMessageMovementMethod(paramMovementMethod);
  }
  
  public void setTitle(CharSequence paramCharSequence)
  {
    super.setTitle(paramCharSequence);
    mAlert.setTitle(paramCharSequence);
  }
  
  public void setView(View paramView)
  {
    mAlert.setView(paramView);
  }
  
  public void setView(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mAlert.setView(paramView, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public static class Builder
  {
    private final AlertController.AlertParams P;
    
    public Builder(Context paramContext)
    {
      this(paramContext, AlertDialog.resolveDialogTheme(paramContext, 0));
    }
    
    public Builder(Context paramContext, int paramInt)
    {
      P = new AlertController.AlertParams(new ContextThemeWrapper(paramContext, AlertDialog.resolveDialogTheme(paramContext, paramInt)));
    }
    
    public AlertDialog create()
    {
      AlertDialog localAlertDialog = new AlertDialog(P.mContext, 0, false);
      P.apply(mAlert);
      localAlertDialog.setCancelable(P.mCancelable);
      if (P.mCancelable) {
        localAlertDialog.setCanceledOnTouchOutside(true);
      }
      localAlertDialog.setOnCancelListener(P.mOnCancelListener);
      localAlertDialog.setOnDismissListener(P.mOnDismissListener);
      if (P.mOnKeyListener != null) {
        localAlertDialog.setOnKeyListener(P.mOnKeyListener);
      }
      return localAlertDialog;
    }
    
    public Context getContext()
    {
      return P.mContext;
    }
    
    public Builder setAdapter(ListAdapter paramListAdapter, DialogInterface.OnClickListener paramOnClickListener)
    {
      P.mAdapter = paramListAdapter;
      P.mOnClickListener = paramOnClickListener;
      return this;
    }
    
    public Builder setCancelable(boolean paramBoolean)
    {
      P.mCancelable = paramBoolean;
      return this;
    }
    
    public Builder setCursor(Cursor paramCursor, DialogInterface.OnClickListener paramOnClickListener, String paramString)
    {
      P.mCursor = paramCursor;
      P.mLabelColumn = paramString;
      P.mOnClickListener = paramOnClickListener;
      return this;
    }
    
    public Builder setCustomTitle(View paramView)
    {
      P.mCustomTitleView = paramView;
      return this;
    }
    
    public Builder setIcon(int paramInt)
    {
      P.mIconId = paramInt;
      return this;
    }
    
    public Builder setIcon(Drawable paramDrawable)
    {
      P.mIcon = paramDrawable;
      return this;
    }
    
    public Builder setIconAttribute(int paramInt)
    {
      TypedValue localTypedValue = new TypedValue();
      P.mContext.getTheme().resolveAttribute(paramInt, localTypedValue, true);
      P.mIconId = resourceId;
      return this;
    }
    
    @Deprecated
    public Builder setInverseBackgroundForced(boolean paramBoolean)
    {
      P.mForceInverseBackground = paramBoolean;
      return this;
    }
    
    public Builder setItems(int paramInt, DialogInterface.OnClickListener paramOnClickListener)
    {
      P.mItems = P.mContext.getResources().getTextArray(paramInt);
      P.mOnClickListener = paramOnClickListener;
      return this;
    }
    
    public Builder setItems(CharSequence[] paramArrayOfCharSequence, DialogInterface.OnClickListener paramOnClickListener)
    {
      P.mItems = paramArrayOfCharSequence;
      P.mOnClickListener = paramOnClickListener;
      return this;
    }
    
    public Builder setMessage(int paramInt)
    {
      P.mMessage = P.mContext.getText(paramInt);
      return this;
    }
    
    public Builder setMessage(CharSequence paramCharSequence)
    {
      P.mMessage = paramCharSequence;
      return this;
    }
    
    public Builder setMultiChoiceItems(int paramInt, boolean[] paramArrayOfBoolean, DialogInterface.OnMultiChoiceClickListener paramOnMultiChoiceClickListener)
    {
      P.mItems = P.mContext.getResources().getTextArray(paramInt);
      P.mOnCheckboxClickListener = paramOnMultiChoiceClickListener;
      P.mCheckedItems = paramArrayOfBoolean;
      P.mIsMultiChoice = true;
      return this;
    }
    
    public Builder setMultiChoiceItems(Cursor paramCursor, String paramString1, String paramString2, DialogInterface.OnMultiChoiceClickListener paramOnMultiChoiceClickListener)
    {
      P.mCursor = paramCursor;
      P.mOnCheckboxClickListener = paramOnMultiChoiceClickListener;
      P.mIsCheckedColumn = paramString1;
      P.mLabelColumn = paramString2;
      P.mIsMultiChoice = true;
      return this;
    }
    
    public Builder setMultiChoiceItems(CharSequence[] paramArrayOfCharSequence, boolean[] paramArrayOfBoolean, DialogInterface.OnMultiChoiceClickListener paramOnMultiChoiceClickListener)
    {
      P.mItems = paramArrayOfCharSequence;
      P.mOnCheckboxClickListener = paramOnMultiChoiceClickListener;
      P.mCheckedItems = paramArrayOfBoolean;
      P.mIsMultiChoice = true;
      return this;
    }
    
    public Builder setNegativeButton(int paramInt, DialogInterface.OnClickListener paramOnClickListener)
    {
      P.mNegativeButtonText = P.mContext.getText(paramInt);
      P.mNegativeButtonListener = paramOnClickListener;
      return this;
    }
    
    public Builder setNegativeButton(CharSequence paramCharSequence, DialogInterface.OnClickListener paramOnClickListener)
    {
      P.mNegativeButtonText = paramCharSequence;
      P.mNegativeButtonListener = paramOnClickListener;
      return this;
    }
    
    public Builder setNeutralButton(int paramInt, DialogInterface.OnClickListener paramOnClickListener)
    {
      P.mNeutralButtonText = P.mContext.getText(paramInt);
      P.mNeutralButtonListener = paramOnClickListener;
      return this;
    }
    
    public Builder setNeutralButton(CharSequence paramCharSequence, DialogInterface.OnClickListener paramOnClickListener)
    {
      P.mNeutralButtonText = paramCharSequence;
      P.mNeutralButtonListener = paramOnClickListener;
      return this;
    }
    
    public Builder setOnCancelListener(DialogInterface.OnCancelListener paramOnCancelListener)
    {
      P.mOnCancelListener = paramOnCancelListener;
      return this;
    }
    
    public Builder setOnDismissListener(DialogInterface.OnDismissListener paramOnDismissListener)
    {
      P.mOnDismissListener = paramOnDismissListener;
      return this;
    }
    
    public Builder setOnItemSelectedListener(AdapterView.OnItemSelectedListener paramOnItemSelectedListener)
    {
      P.mOnItemSelectedListener = paramOnItemSelectedListener;
      return this;
    }
    
    public Builder setOnKeyListener(DialogInterface.OnKeyListener paramOnKeyListener)
    {
      P.mOnKeyListener = paramOnKeyListener;
      return this;
    }
    
    public Builder setPositiveButton(int paramInt, DialogInterface.OnClickListener paramOnClickListener)
    {
      P.mPositiveButtonText = P.mContext.getText(paramInt);
      P.mPositiveButtonListener = paramOnClickListener;
      return this;
    }
    
    public Builder setPositiveButton(CharSequence paramCharSequence, DialogInterface.OnClickListener paramOnClickListener)
    {
      P.mPositiveButtonText = paramCharSequence;
      P.mPositiveButtonListener = paramOnClickListener;
      return this;
    }
    
    public Builder setRecycleOnMeasureEnabled(boolean paramBoolean)
    {
      P.mRecycleOnMeasure = paramBoolean;
      return this;
    }
    
    public Builder setSingleChoiceItems(int paramInt1, int paramInt2, DialogInterface.OnClickListener paramOnClickListener)
    {
      P.mItems = P.mContext.getResources().getTextArray(paramInt1);
      P.mOnClickListener = paramOnClickListener;
      P.mCheckedItem = paramInt2;
      P.mIsSingleChoice = true;
      return this;
    }
    
    public Builder setSingleChoiceItems(Cursor paramCursor, int paramInt, String paramString, DialogInterface.OnClickListener paramOnClickListener)
    {
      P.mCursor = paramCursor;
      P.mOnClickListener = paramOnClickListener;
      P.mCheckedItem = paramInt;
      P.mLabelColumn = paramString;
      P.mIsSingleChoice = true;
      return this;
    }
    
    public Builder setSingleChoiceItems(ListAdapter paramListAdapter, int paramInt, DialogInterface.OnClickListener paramOnClickListener)
    {
      P.mAdapter = paramListAdapter;
      P.mOnClickListener = paramOnClickListener;
      P.mCheckedItem = paramInt;
      P.mIsSingleChoice = true;
      return this;
    }
    
    public Builder setSingleChoiceItems(CharSequence[] paramArrayOfCharSequence, int paramInt, DialogInterface.OnClickListener paramOnClickListener)
    {
      P.mItems = paramArrayOfCharSequence;
      P.mOnClickListener = paramOnClickListener;
      P.mCheckedItem = paramInt;
      P.mIsSingleChoice = true;
      return this;
    }
    
    public Builder setTitle(int paramInt)
    {
      P.mTitle = P.mContext.getText(paramInt);
      return this;
    }
    
    public Builder setTitle(CharSequence paramCharSequence)
    {
      P.mTitle = paramCharSequence;
      return this;
    }
    
    public Builder setView(int paramInt)
    {
      P.mView = null;
      P.mViewLayoutResId = paramInt;
      P.mViewSpacingSpecified = false;
      return this;
    }
    
    public Builder setView(View paramView)
    {
      P.mView = paramView;
      P.mViewLayoutResId = 0;
      P.mViewSpacingSpecified = false;
      return this;
    }
    
    @Deprecated
    public Builder setView(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      P.mView = paramView;
      P.mViewLayoutResId = 0;
      P.mViewSpacingSpecified = true;
      P.mViewSpacingLeft = paramInt1;
      P.mViewSpacingTop = paramInt2;
      P.mViewSpacingRight = paramInt3;
      P.mViewSpacingBottom = paramInt4;
      return this;
    }
    
    public AlertDialog show()
    {
      AlertDialog localAlertDialog = create();
      localAlertDialog.show();
      return localAlertDialog;
    }
  }
}
