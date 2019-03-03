package android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View.BaseSavedState;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewHierarchyEncoder;
import android.view.ViewStructure;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.autofill.AutofillManager;
import android.view.autofill.AutofillValue;
import com.android.internal.R.styleable;

public abstract class CompoundButton
  extends Button
  implements Checkable
{
  private static final int[] CHECKED_STATE_SET = { 16842912 };
  private static final String LOG_TAG = CompoundButton.class.getSimpleName();
  private boolean mBroadcasting;
  private Drawable mButtonDrawable;
  private ColorStateList mButtonTintList = null;
  private PorterDuff.Mode mButtonTintMode = null;
  private boolean mChecked;
  private boolean mCheckedFromResource = false;
  private boolean mHasButtonTint = false;
  private boolean mHasButtonTintMode = false;
  private OnCheckedChangeListener mOnCheckedChangeListener;
  private OnCheckedChangeListener mOnCheckedChangeWidgetListener;
  
  public CompoundButton(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public CompoundButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public CompoundButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public CompoundButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.CompoundButton, paramInt1, paramInt2);
    paramContext = paramAttributeSet.getDrawable(1);
    if (paramContext != null) {
      setButtonDrawable(paramContext);
    }
    if (paramAttributeSet.hasValue(3))
    {
      mButtonTintMode = Drawable.parseTintMode(paramAttributeSet.getInt(3, -1), mButtonTintMode);
      mHasButtonTintMode = true;
    }
    if (paramAttributeSet.hasValue(2))
    {
      mButtonTintList = paramAttributeSet.getColorStateList(2);
      mHasButtonTint = true;
    }
    setChecked(paramAttributeSet.getBoolean(0, false));
    mCheckedFromResource = true;
    paramAttributeSet.recycle();
    applyButtonTint();
  }
  
  private void applyButtonTint()
  {
    if ((mButtonDrawable != null) && ((mHasButtonTint) || (mHasButtonTintMode)))
    {
      mButtonDrawable = mButtonDrawable.mutate();
      if (mHasButtonTint) {
        mButtonDrawable.setTintList(mButtonTintList);
      }
      if (mHasButtonTintMode) {
        mButtonDrawable.setTintMode(mButtonTintMode);
      }
      if (mButtonDrawable.isStateful()) {
        mButtonDrawable.setState(getDrawableState());
      }
    }
  }
  
  public void autofill(AutofillValue paramAutofillValue)
  {
    if (!isEnabled()) {
      return;
    }
    if (!paramAutofillValue.isToggle())
    {
      String str = LOG_TAG;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramAutofillValue);
      localStringBuilder.append(" could not be autofilled into ");
      localStringBuilder.append(this);
      Log.w(str, localStringBuilder.toString());
      return;
    }
    setChecked(paramAutofillValue.getToggleValue());
  }
  
  public void drawableHotspotChanged(float paramFloat1, float paramFloat2)
  {
    super.drawableHotspotChanged(paramFloat1, paramFloat2);
    if (mButtonDrawable != null) {
      mButtonDrawable.setHotspot(paramFloat1, paramFloat2);
    }
  }
  
  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    Drawable localDrawable = mButtonDrawable;
    if ((localDrawable != null) && (localDrawable.isStateful()) && (localDrawable.setState(getDrawableState()))) {
      invalidateDrawable(localDrawable);
    }
  }
  
  protected void encodeProperties(ViewHierarchyEncoder paramViewHierarchyEncoder)
  {
    super.encodeProperties(paramViewHierarchyEncoder);
    paramViewHierarchyEncoder.addProperty("checked", isChecked());
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return CompoundButton.class.getName();
  }
  
  public int getAutofillType()
  {
    int i;
    if (isEnabled()) {
      i = 2;
    } else {
      i = 0;
    }
    return i;
  }
  
  public AutofillValue getAutofillValue()
  {
    AutofillValue localAutofillValue;
    if (isEnabled()) {
      localAutofillValue = AutofillValue.forToggle(isChecked());
    } else {
      localAutofillValue = null;
    }
    return localAutofillValue;
  }
  
  public Drawable getButtonDrawable()
  {
    return mButtonDrawable;
  }
  
  public ColorStateList getButtonTintList()
  {
    return mButtonTintList;
  }
  
  public PorterDuff.Mode getButtonTintMode()
  {
    return mButtonTintMode;
  }
  
  public int getCompoundPaddingLeft()
  {
    int i = super.getCompoundPaddingLeft();
    int j = i;
    if (!isLayoutRtl())
    {
      Drawable localDrawable = mButtonDrawable;
      j = i;
      if (localDrawable != null) {
        j = i + localDrawable.getIntrinsicWidth();
      }
    }
    return j;
  }
  
  public int getCompoundPaddingRight()
  {
    int i = super.getCompoundPaddingRight();
    int j = i;
    if (isLayoutRtl())
    {
      Drawable localDrawable = mButtonDrawable;
      j = i;
      if (localDrawable != null) {
        j = i + localDrawable.getIntrinsicWidth();
      }
    }
    return j;
  }
  
  public int getHorizontalOffsetForDrawables()
  {
    Drawable localDrawable = mButtonDrawable;
    int i;
    if (localDrawable != null) {
      i = localDrawable.getIntrinsicWidth();
    } else {
      i = 0;
    }
    return i;
  }
  
  @ViewDebug.ExportedProperty
  public boolean isChecked()
  {
    return mChecked;
  }
  
  public void jumpDrawablesToCurrentState()
  {
    super.jumpDrawablesToCurrentState();
    if (mButtonDrawable != null) {
      mButtonDrawable.jumpToCurrentState();
    }
  }
  
  protected int[] onCreateDrawableState(int paramInt)
  {
    int[] arrayOfInt = super.onCreateDrawableState(paramInt + 1);
    if (isChecked()) {
      mergeDrawableStates(arrayOfInt, CHECKED_STATE_SET);
    }
    return arrayOfInt;
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    Drawable localDrawable1 = mButtonDrawable;
    int i;
    int m;
    if (localDrawable1 != null)
    {
      i = getGravity() & 0x70;
      int j = localDrawable1.getIntrinsicHeight();
      int k = localDrawable1.getIntrinsicWidth();
      m = 0;
      if (i != 16)
      {
        if (i != 80) {
          i = 0;
        } else {
          i = getHeight() - j;
        }
      }
      else {
        i = (getHeight() - j) / 2;
      }
      j = i + j;
      if (isLayoutRtl()) {
        m = getWidth() - k;
      }
      if (isLayoutRtl()) {
        k = getWidth();
      }
      localDrawable1.setBounds(m, i, k, j);
      Drawable localDrawable2 = getBackground();
      if (localDrawable2 != null) {
        localDrawable2.setHotspotBounds(m, i, k, j);
      }
    }
    super.onDraw(paramCanvas);
    if (localDrawable1 != null)
    {
      i = mScrollX;
      m = mScrollY;
      if ((i == 0) && (m == 0))
      {
        localDrawable1.draw(paramCanvas);
      }
      else
      {
        paramCanvas.translate(i, m);
        localDrawable1.draw(paramCanvas);
        paramCanvas.translate(-i, -m);
      }
    }
  }
  
  public void onInitializeAccessibilityEventInternal(AccessibilityEvent paramAccessibilityEvent)
  {
    super.onInitializeAccessibilityEventInternal(paramAccessibilityEvent);
    paramAccessibilityEvent.setChecked(mChecked);
  }
  
  public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfoInternal(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setCheckable(true);
    paramAccessibilityNodeInfo.setChecked(mChecked);
  }
  
  public void onProvideAutofillStructure(ViewStructure paramViewStructure, int paramInt)
  {
    super.onProvideAutofillStructure(paramViewStructure, paramInt);
    paramViewStructure.setDataIsSensitive(mCheckedFromResource ^ true);
  }
  
  public void onResolveDrawables(int paramInt)
  {
    super.onResolveDrawables(paramInt);
    if (mButtonDrawable != null) {
      mButtonDrawable.setLayoutDirection(paramInt);
    }
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    paramParcelable = (SavedState)paramParcelable;
    super.onRestoreInstanceState(paramParcelable.getSuperState());
    setChecked(checked);
    requestLayout();
  }
  
  public Parcelable onSaveInstanceState()
  {
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    checked = isChecked();
    return localSavedState;
  }
  
  public boolean performClick()
  {
    toggle();
    boolean bool = super.performClick();
    if (!bool) {
      playSoundEffect(0);
    }
    return bool;
  }
  
  public void setButtonDrawable(int paramInt)
  {
    Drawable localDrawable;
    if (paramInt != 0) {
      localDrawable = getContext().getDrawable(paramInt);
    } else {
      localDrawable = null;
    }
    setButtonDrawable(localDrawable);
  }
  
  public void setButtonDrawable(Drawable paramDrawable)
  {
    if (mButtonDrawable != paramDrawable)
    {
      if (mButtonDrawable != null)
      {
        mButtonDrawable.setCallback(null);
        unscheduleDrawable(mButtonDrawable);
      }
      mButtonDrawable = paramDrawable;
      if (paramDrawable != null)
      {
        paramDrawable.setCallback(this);
        paramDrawable.setLayoutDirection(getLayoutDirection());
        if (paramDrawable.isStateful()) {
          paramDrawable.setState(getDrawableState());
        }
        boolean bool;
        if (getVisibility() == 0) {
          bool = true;
        } else {
          bool = false;
        }
        paramDrawable.setVisible(bool, false);
        setMinHeight(paramDrawable.getIntrinsicHeight());
        applyButtonTint();
      }
    }
  }
  
  public void setButtonTintList(ColorStateList paramColorStateList)
  {
    mButtonTintList = paramColorStateList;
    mHasButtonTint = true;
    applyButtonTint();
  }
  
  public void setButtonTintMode(PorterDuff.Mode paramMode)
  {
    mButtonTintMode = paramMode;
    mHasButtonTintMode = true;
    applyButtonTint();
  }
  
  public void setChecked(boolean paramBoolean)
  {
    if (mChecked != paramBoolean)
    {
      mCheckedFromResource = false;
      mChecked = paramBoolean;
      refreshDrawableState();
      notifyViewAccessibilityStateChangedIfNeeded(0);
      if (mBroadcasting) {
        return;
      }
      mBroadcasting = true;
      if (mOnCheckedChangeListener != null) {
        mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
      }
      if (mOnCheckedChangeWidgetListener != null) {
        mOnCheckedChangeWidgetListener.onCheckedChanged(this, mChecked);
      }
      AutofillManager localAutofillManager = (AutofillManager)mContext.getSystemService(AutofillManager.class);
      if (localAutofillManager != null) {
        localAutofillManager.notifyValueChanged(this);
      }
      mBroadcasting = false;
    }
  }
  
  public void setOnCheckedChangeListener(OnCheckedChangeListener paramOnCheckedChangeListener)
  {
    mOnCheckedChangeListener = paramOnCheckedChangeListener;
  }
  
  void setOnCheckedChangeWidgetListener(OnCheckedChangeListener paramOnCheckedChangeListener)
  {
    mOnCheckedChangeWidgetListener = paramOnCheckedChangeListener;
  }
  
  public void toggle()
  {
    setChecked(mChecked ^ true);
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable)
  {
    boolean bool;
    if ((!super.verifyDrawable(paramDrawable)) && (paramDrawable != mButtonDrawable)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static abstract interface OnCheckedChangeListener
  {
    public abstract void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean);
  }
  
  static class SavedState
    extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public CompoundButton.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new CompoundButton.SavedState(paramAnonymousParcel, null);
      }
      
      public CompoundButton.SavedState[] newArray(int paramAnonymousInt)
      {
        return new CompoundButton.SavedState[paramAnonymousInt];
      }
    };
    boolean checked;
    
    private SavedState(Parcel paramParcel)
    {
      super();
      checked = ((Boolean)paramParcel.readValue(null)).booleanValue();
    }
    
    SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("CompoundButton.SavedState{");
      localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      localStringBuilder.append(" checked=");
      localStringBuilder.append(checked);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeValue(Boolean.valueOf(checked));
    }
  }
}
