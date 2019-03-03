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
import android.view.Gravity;
import android.view.RemotableViewMethod;
import android.view.View.BaseSavedState;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewHierarchyEncoder;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.android.internal.R.styleable;

public class CheckedTextView
  extends TextView
  implements Checkable
{
  private static final int[] CHECKED_STATE_SET = { 16842912 };
  private int mBasePadding;
  private Drawable mCheckMarkDrawable;
  private int mCheckMarkGravity = 8388613;
  private int mCheckMarkResource;
  private ColorStateList mCheckMarkTintList = null;
  private PorterDuff.Mode mCheckMarkTintMode = null;
  private int mCheckMarkWidth;
  private boolean mChecked;
  private boolean mHasCheckMarkTint = false;
  private boolean mHasCheckMarkTintMode = false;
  private boolean mNeedRequestlayout;
  
  public CheckedTextView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public CheckedTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16843720);
  }
  
  public CheckedTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public CheckedTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.CheckedTextView, paramInt1, paramInt2);
    paramAttributeSet = paramContext.getDrawable(1);
    if (paramAttributeSet != null) {
      setCheckMarkDrawable(paramAttributeSet);
    }
    if (paramContext.hasValue(3))
    {
      mCheckMarkTintMode = Drawable.parseTintMode(paramContext.getInt(3, -1), mCheckMarkTintMode);
      mHasCheckMarkTintMode = true;
    }
    if (paramContext.hasValue(2))
    {
      mCheckMarkTintList = paramContext.getColorStateList(2);
      mHasCheckMarkTint = true;
    }
    mCheckMarkGravity = paramContext.getInt(4, 8388613);
    setChecked(paramContext.getBoolean(0, false));
    paramContext.recycle();
    applyCheckMarkTint();
  }
  
  private void applyCheckMarkTint()
  {
    if ((mCheckMarkDrawable != null) && ((mHasCheckMarkTint) || (mHasCheckMarkTintMode)))
    {
      mCheckMarkDrawable = mCheckMarkDrawable.mutate();
      if (mHasCheckMarkTint) {
        mCheckMarkDrawable.setTintList(mCheckMarkTintList);
      }
      if (mHasCheckMarkTintMode) {
        mCheckMarkDrawable.setTintMode(mCheckMarkTintMode);
      }
      if (mCheckMarkDrawable.isStateful()) {
        mCheckMarkDrawable.setState(getDrawableState());
      }
    }
  }
  
  private boolean isCheckMarkAtStart()
  {
    boolean bool;
    if ((Gravity.getAbsoluteGravity(mCheckMarkGravity, getLayoutDirection()) & 0x7) == 3) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void setBasePadding(boolean paramBoolean)
  {
    if (paramBoolean) {
      mBasePadding = mPaddingLeft;
    } else {
      mBasePadding = mPaddingRight;
    }
  }
  
  private void setCheckMarkDrawableInternal(Drawable paramDrawable, int paramInt)
  {
    if (mCheckMarkDrawable != null)
    {
      mCheckMarkDrawable.setCallback(null);
      unscheduleDrawable(mCheckMarkDrawable);
    }
    Drawable localDrawable = mCheckMarkDrawable;
    boolean bool1 = true;
    boolean bool2;
    if (paramDrawable != localDrawable) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mNeedRequestlayout = bool2;
    if (paramDrawable != null)
    {
      paramDrawable.setCallback(this);
      if (getVisibility() == 0) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
      paramDrawable.setVisible(bool2, false);
      paramDrawable.setState(CHECKED_STATE_SET);
      setMinHeight(paramDrawable.getIntrinsicHeight());
      mCheckMarkWidth = paramDrawable.getIntrinsicWidth();
      paramDrawable.setState(getDrawableState());
    }
    else
    {
      mCheckMarkWidth = 0;
    }
    mCheckMarkDrawable = paramDrawable;
    mCheckMarkResource = paramInt;
    applyCheckMarkTint();
    resolvePadding();
  }
  
  private void updatePadding()
  {
    resetPaddingToInitialValues();
    int i;
    if (mCheckMarkDrawable != null) {
      i = mCheckMarkWidth + mBasePadding;
    } else {
      i = mBasePadding;
    }
    boolean bool1 = isCheckMarkAtStart();
    boolean bool2 = true;
    boolean bool3 = true;
    if (bool1)
    {
      bool1 = mNeedRequestlayout;
      if (mPaddingLeft == i) {
        bool3 = false;
      }
      mNeedRequestlayout = (bool1 | bool3);
      mPaddingLeft = i;
    }
    else
    {
      bool1 = mNeedRequestlayout;
      if (mPaddingRight != i) {
        bool3 = bool2;
      } else {
        bool3 = false;
      }
      mNeedRequestlayout = (bool1 | bool3);
      mPaddingRight = i;
    }
    if (mNeedRequestlayout)
    {
      requestLayout();
      mNeedRequestlayout = false;
    }
  }
  
  public void drawableHotspotChanged(float paramFloat1, float paramFloat2)
  {
    super.drawableHotspotChanged(paramFloat1, paramFloat2);
    if (mCheckMarkDrawable != null) {
      mCheckMarkDrawable.setHotspot(paramFloat1, paramFloat2);
    }
  }
  
  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    Drawable localDrawable = mCheckMarkDrawable;
    if ((localDrawable != null) && (localDrawable.isStateful()) && (localDrawable.setState(getDrawableState()))) {
      invalidateDrawable(localDrawable);
    }
  }
  
  protected void encodeProperties(ViewHierarchyEncoder paramViewHierarchyEncoder)
  {
    super.encodeProperties(paramViewHierarchyEncoder);
    paramViewHierarchyEncoder.addProperty("text:checked", isChecked());
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return CheckedTextView.class.getName();
  }
  
  public Drawable getCheckMarkDrawable()
  {
    return mCheckMarkDrawable;
  }
  
  public ColorStateList getCheckMarkTintList()
  {
    return mCheckMarkTintList;
  }
  
  public PorterDuff.Mode getCheckMarkTintMode()
  {
    return mCheckMarkTintMode;
  }
  
  protected void internalSetPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.internalSetPadding(paramInt1, paramInt2, paramInt3, paramInt4);
    setBasePadding(isCheckMarkAtStart());
  }
  
  @ViewDebug.ExportedProperty
  public boolean isChecked()
  {
    return mChecked;
  }
  
  public void jumpDrawablesToCurrentState()
  {
    super.jumpDrawablesToCurrentState();
    if (mCheckMarkDrawable != null) {
      mCheckMarkDrawable.jumpToCurrentState();
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
    super.onDraw(paramCanvas);
    Drawable localDrawable = mCheckMarkDrawable;
    if (localDrawable != null)
    {
      int i = getGravity() & 0x70;
      int j = localDrawable.getIntrinsicHeight();
      int k = 0;
      if (i != 16)
      {
        if (i == 80) {
          k = getHeight() - j;
        }
      }
      else {
        k = (getHeight() - j) / 2;
      }
      boolean bool = isCheckMarkAtStart();
      i = getWidth();
      int m = k + j;
      if (bool)
      {
        j = mBasePadding;
        i = mCheckMarkWidth + j;
      }
      else
      {
        i -= mBasePadding;
        j = i - mCheckMarkWidth;
      }
      localDrawable.setBounds(mScrollX + j, k, mScrollX + i, m);
      localDrawable.draw(paramCanvas);
      paramCanvas = getBackground();
      if (paramCanvas != null) {
        paramCanvas.setHotspotBounds(mScrollX + j, k, mScrollX + i, m);
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
  
  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    paramParcelable = (SavedState)paramParcelable;
    super.onRestoreInstanceState(paramParcelable.getSuperState());
    setChecked(checked);
    requestLayout();
  }
  
  public void onRtlPropertiesChanged(int paramInt)
  {
    super.onRtlPropertiesChanged(paramInt);
    updatePadding();
  }
  
  public Parcelable onSaveInstanceState()
  {
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    checked = isChecked();
    return localSavedState;
  }
  
  public void setCheckMarkDrawable(int paramInt)
  {
    if ((paramInt != 0) && (paramInt == mCheckMarkResource)) {
      return;
    }
    Drawable localDrawable;
    if (paramInt != 0) {
      localDrawable = getContext().getDrawable(paramInt);
    } else {
      localDrawable = null;
    }
    setCheckMarkDrawableInternal(localDrawable, paramInt);
  }
  
  public void setCheckMarkDrawable(Drawable paramDrawable)
  {
    setCheckMarkDrawableInternal(paramDrawable, 0);
  }
  
  public void setCheckMarkTintList(ColorStateList paramColorStateList)
  {
    mCheckMarkTintList = paramColorStateList;
    mHasCheckMarkTint = true;
    applyCheckMarkTint();
  }
  
  public void setCheckMarkTintMode(PorterDuff.Mode paramMode)
  {
    mCheckMarkTintMode = paramMode;
    mHasCheckMarkTintMode = true;
    applyCheckMarkTint();
  }
  
  public void setChecked(boolean paramBoolean)
  {
    if (mChecked != paramBoolean)
    {
      mChecked = paramBoolean;
      refreshDrawableState();
      notifyViewAccessibilityStateChangedIfNeeded(0);
    }
  }
  
  @RemotableViewMethod
  public void setVisibility(int paramInt)
  {
    super.setVisibility(paramInt);
    if (mCheckMarkDrawable != null)
    {
      Drawable localDrawable = mCheckMarkDrawable;
      boolean bool;
      if (paramInt == 0) {
        bool = true;
      } else {
        bool = false;
      }
      localDrawable.setVisible(bool, false);
    }
  }
  
  public void toggle()
  {
    setChecked(mChecked ^ true);
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable)
  {
    boolean bool;
    if ((paramDrawable != mCheckMarkDrawable) && (!super.verifyDrawable(paramDrawable))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  static class SavedState
    extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public CheckedTextView.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new CheckedTextView.SavedState(paramAnonymousParcel, null);
      }
      
      public CheckedTextView.SavedState[] newArray(int paramAnonymousInt)
      {
        return new CheckedTextView.SavedState[paramAnonymousInt];
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
      localStringBuilder.append("CheckedTextView.SavedState{");
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
