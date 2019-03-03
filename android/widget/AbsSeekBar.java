package android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Insets;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import com.android.internal.R.styleable;

public abstract class AbsSeekBar
  extends ProgressBar
{
  private static final int NO_ALPHA = 255;
  private float mDisabledAlpha;
  private boolean mHasThumbTint = false;
  private boolean mHasThumbTintMode = false;
  private boolean mHasTickMarkTint = false;
  private boolean mHasTickMarkTintMode = false;
  private boolean mIsDragging;
  boolean mIsUserSeekable = true;
  private int mKeyProgressIncrement = 1;
  private int mScaledTouchSlop;
  private boolean mSplitTrack;
  private final Rect mTempRect = new Rect();
  private Drawable mThumb;
  private int mThumbOffset;
  private ColorStateList mThumbTintList = null;
  private PorterDuff.Mode mThumbTintMode = null;
  private Drawable mTickMark;
  private ColorStateList mTickMarkTintList = null;
  private PorterDuff.Mode mTickMarkTintMode = null;
  private float mTouchDownX;
  float mTouchProgressOffset;
  
  public AbsSeekBar(Context paramContext)
  {
    super(paramContext);
  }
  
  public AbsSeekBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public AbsSeekBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public AbsSeekBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.SeekBar, paramInt1, paramInt2);
    setThumb(localTypedArray.getDrawable(0));
    if (localTypedArray.hasValue(4))
    {
      mThumbTintMode = Drawable.parseTintMode(localTypedArray.getInt(4, -1), mThumbTintMode);
      mHasThumbTintMode = true;
    }
    if (localTypedArray.hasValue(3))
    {
      mThumbTintList = localTypedArray.getColorStateList(3);
      mHasThumbTint = true;
    }
    setTickMark(localTypedArray.getDrawable(5));
    if (localTypedArray.hasValue(7))
    {
      mTickMarkTintMode = Drawable.parseTintMode(localTypedArray.getInt(7, -1), mTickMarkTintMode);
      mHasTickMarkTintMode = true;
    }
    if (localTypedArray.hasValue(6))
    {
      mTickMarkTintList = localTypedArray.getColorStateList(6);
      mHasTickMarkTint = true;
    }
    mSplitTrack = localTypedArray.getBoolean(2, false);
    setThumbOffset(localTypedArray.getDimensionPixelOffset(1, getThumbOffset()));
    boolean bool = localTypedArray.getBoolean(8, true);
    localTypedArray.recycle();
    if (bool)
    {
      paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.Theme, 0, 0);
      mDisabledAlpha = paramAttributeSet.getFloat(3, 0.5F);
      paramAttributeSet.recycle();
    }
    else
    {
      mDisabledAlpha = 1.0F;
    }
    applyThumbTint();
    applyTickMarkTint();
    mScaledTouchSlop = ViewConfiguration.get(paramContext).getScaledTouchSlop();
  }
  
  private void applyThumbTint()
  {
    if ((mThumb != null) && ((mHasThumbTint) || (mHasThumbTintMode)))
    {
      mThumb = mThumb.mutate();
      if (mHasThumbTint) {
        mThumb.setTintList(mThumbTintList);
      }
      if (mHasThumbTintMode) {
        mThumb.setTintMode(mThumbTintMode);
      }
      if (mThumb.isStateful()) {
        mThumb.setState(getDrawableState());
      }
    }
  }
  
  private void applyTickMarkTint()
  {
    if ((mTickMark != null) && ((mHasTickMarkTint) || (mHasTickMarkTintMode)))
    {
      mTickMark = mTickMark.mutate();
      if (mHasTickMarkTint) {
        mTickMark.setTintList(mTickMarkTintList);
      }
      if (mHasTickMarkTintMode) {
        mTickMark.setTintMode(mTickMarkTintMode);
      }
      if (mTickMark.isStateful()) {
        mTickMark.setState(getDrawableState());
      }
    }
  }
  
  private void attemptClaimDrag()
  {
    if (mParent != null) {
      mParent.requestDisallowInterceptTouchEvent(true);
    }
  }
  
  private float getScale()
  {
    int i = getMin();
    int j = getMax() - i;
    float f;
    if (j > 0) {
      f = (getProgress() - i) / j;
    } else {
      f = 0.0F;
    }
    return f;
  }
  
  private void setHotspot(float paramFloat1, float paramFloat2)
  {
    Drawable localDrawable = getBackground();
    if (localDrawable != null) {
      localDrawable.setHotspot(paramFloat1, paramFloat2);
    }
  }
  
  private void setThumbPos(int paramInt1, Drawable paramDrawable, float paramFloat, int paramInt2)
  {
    int i = mPaddingLeft;
    int j = mPaddingRight;
    int k = paramDrawable.getIntrinsicWidth();
    int m = paramDrawable.getIntrinsicHeight();
    j = paramInt1 - i - j - k + mThumbOffset * 2;
    i = (int)(j * paramFloat + 0.5F);
    if (paramInt2 == Integer.MIN_VALUE)
    {
      localObject = paramDrawable.getBounds();
      paramInt1 = top;
      paramInt2 = bottom;
    }
    else
    {
      paramInt1 = paramInt2;
      paramInt2 += m;
    }
    if ((isLayoutRtl()) && (mMirrorForRtl)) {
      i = j - i;
    }
    j = i + k;
    Object localObject = getBackground();
    if (localObject != null)
    {
      k = mPaddingLeft - mThumbOffset;
      m = mPaddingTop;
      ((Drawable)localObject).setHotspotBounds(i + k, paramInt1 + m, j + k, paramInt2 + m);
    }
    paramDrawable.setBounds(i, paramInt1, j, paramInt2);
  }
  
  private void startDrag(MotionEvent paramMotionEvent)
  {
    setPressed(true);
    if (mThumb != null) {
      invalidate(mThumb.getBounds());
    }
    onStartTrackingTouch();
    trackTouchEvent(paramMotionEvent);
    attemptClaimDrag();
  }
  
  private void trackTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = Math.round(paramMotionEvent.getX());
    int j = Math.round(paramMotionEvent.getY());
    int k = getWidth();
    int m = k - mPaddingLeft - mPaddingRight;
    float f1 = 0.0F;
    float f2;
    if ((isLayoutRtl()) && (mMirrorForRtl))
    {
      if (i > k - mPaddingRight)
      {
        f2 = 0.0F;
      }
      else if (i < mPaddingLeft)
      {
        f2 = 1.0F;
      }
      else
      {
        f2 = (m - i + mPaddingLeft) / m;
        f1 = mTouchProgressOffset;
        break label159;
      }
    }
    else
    {
      if (i >= mPaddingLeft) {
        break label123;
      }
      f2 = 0.0F;
    }
    for (;;)
    {
      break;
      label123:
      if (i > k - mPaddingRight)
      {
        f2 = 1.0F;
      }
      else
      {
        f2 = (i - mPaddingLeft) / m;
        f1 = mTouchProgressOffset;
      }
    }
    label159:
    float f3 = getMax() - getMin();
    float f4 = getMin();
    setHotspot(i, j);
    setProgressInternal(Math.round(f1 + (f3 * f2 + f4)), true, false);
  }
  
  private void updateThumbAndTrackPos(int paramInt1, int paramInt2)
  {
    int i = paramInt2 - mPaddingTop - mPaddingBottom;
    Drawable localDrawable1 = getCurrentDrawable();
    Drawable localDrawable2 = mThumb;
    int j = Math.min(mMaxHeight, i);
    if (localDrawable2 == null) {
      paramInt2 = 0;
    } else {
      paramInt2 = localDrawable2.getIntrinsicHeight();
    }
    int k;
    if (paramInt2 > j)
    {
      i = (i - paramInt2) / 2;
      k = (paramInt2 - j) / 2 + i;
      paramInt2 = i;
      i = k;
    }
    else
    {
      k = (i - j) / 2;
      i = k;
      paramInt2 = k + (j - paramInt2) / 2;
    }
    if (localDrawable1 != null) {
      localDrawable1.setBounds(0, i, paramInt1 - mPaddingRight - mPaddingLeft, i + j);
    }
    if (localDrawable2 != null) {
      setThumbPos(paramInt1, localDrawable2, getScale(), paramInt2);
    }
  }
  
  boolean canUserSetProgress()
  {
    boolean bool;
    if ((!isIndeterminate()) && (isEnabled())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  void drawThumb(Canvas paramCanvas)
  {
    if (mThumb != null)
    {
      int i = paramCanvas.save();
      paramCanvas.translate(mPaddingLeft - mThumbOffset, mPaddingTop);
      mThumb.draw(paramCanvas);
      paramCanvas.restoreToCount(i);
    }
  }
  
  protected void drawTickMarks(Canvas paramCanvas)
  {
    if (mTickMark != null)
    {
      int i = getMax() - getMin();
      int j = 1;
      if (i > 1)
      {
        int k = mTickMark.getIntrinsicWidth();
        int m = mTickMark.getIntrinsicHeight();
        if (k >= 0) {
          k /= 2;
        } else {
          k = 1;
        }
        if (m >= 0) {
          j = m / 2;
        }
        mTickMark.setBounds(-k, -j, k, j);
        float f = (getWidth() - mPaddingLeft - mPaddingRight) / i;
        j = paramCanvas.save();
        paramCanvas.translate(mPaddingLeft, getHeight() / 2);
        for (k = 0; k <= i; k++)
        {
          mTickMark.draw(paramCanvas);
          paramCanvas.translate(f, 0.0F);
        }
        paramCanvas.restoreToCount(j);
      }
    }
  }
  
  void drawTrack(Canvas paramCanvas)
  {
    Drawable localDrawable = mThumb;
    if ((localDrawable != null) && (mSplitTrack))
    {
      Insets localInsets = localDrawable.getOpticalInsets();
      Rect localRect = mTempRect;
      localDrawable.copyBounds(localRect);
      localRect.offset(mPaddingLeft - mThumbOffset, mPaddingTop);
      left += left;
      right -= right;
      int i = paramCanvas.save();
      paramCanvas.clipRect(localRect, Region.Op.DIFFERENCE);
      super.drawTrack(paramCanvas);
      drawTickMarks(paramCanvas);
      paramCanvas.restoreToCount(i);
    }
    else
    {
      super.drawTrack(paramCanvas);
      drawTickMarks(paramCanvas);
    }
  }
  
  public void drawableHotspotChanged(float paramFloat1, float paramFloat2)
  {
    super.drawableHotspotChanged(paramFloat1, paramFloat2);
    if (mThumb != null) {
      mThumb.setHotspot(paramFloat1, paramFloat2);
    }
  }
  
  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    Drawable localDrawable = getProgressDrawable();
    if ((localDrawable != null) && (mDisabledAlpha < 1.0F))
    {
      int i;
      if (isEnabled()) {
        i = 255;
      } else {
        i = (int)(255.0F * mDisabledAlpha);
      }
      localDrawable.setAlpha(i);
    }
    localDrawable = mThumb;
    if ((localDrawable != null) && (localDrawable.isStateful()) && (localDrawable.setState(getDrawableState()))) {
      invalidateDrawable(localDrawable);
    }
    localDrawable = mTickMark;
    if ((localDrawable != null) && (localDrawable.isStateful()) && (localDrawable.setState(getDrawableState()))) {
      invalidateDrawable(localDrawable);
    }
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return AbsSeekBar.class.getName();
  }
  
  public int getKeyProgressIncrement()
  {
    return mKeyProgressIncrement;
  }
  
  public boolean getSplitTrack()
  {
    return mSplitTrack;
  }
  
  public Drawable getThumb()
  {
    return mThumb;
  }
  
  public int getThumbOffset()
  {
    return mThumbOffset;
  }
  
  public ColorStateList getThumbTintList()
  {
    return mThumbTintList;
  }
  
  public PorterDuff.Mode getThumbTintMode()
  {
    return mThumbTintMode;
  }
  
  public Drawable getTickMark()
  {
    return mTickMark;
  }
  
  public ColorStateList getTickMarkTintList()
  {
    return mTickMarkTintList;
  }
  
  public PorterDuff.Mode getTickMarkTintMode()
  {
    return mTickMarkTintMode;
  }
  
  public void jumpDrawablesToCurrentState()
  {
    super.jumpDrawablesToCurrentState();
    if (mThumb != null) {
      mThumb.jumpToCurrentState();
    }
    if (mTickMark != null) {
      mTickMark.jumpToCurrentState();
    }
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    try
    {
      super.onDraw(paramCanvas);
      drawThumb(paramCanvas);
      return;
    }
    finally
    {
      paramCanvas = finally;
      throw paramCanvas;
    }
  }
  
  public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfoInternal(paramAccessibilityNodeInfo);
    if (isEnabled())
    {
      int i = getProgress();
      if (i > getMin()) {
        paramAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_BACKWARD);
      }
      if (i < getMax()) {
        paramAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD);
      }
    }
  }
  
  void onKeyChange() {}
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (isEnabled())
    {
      int i = mKeyProgressIncrement;
      int j = i;
      switch (paramInt)
      {
      default: 
        break;
      case 21: 
      case 69: 
        j = -i;
      case 22: 
      case 70: 
      case 81: 
        if (isLayoutRtl()) {
          j = -j;
        }
        if (setProgressInternal(getProgress() + j, true, true))
        {
          onKeyChange();
          return true;
        }
        break;
      }
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    try
    {
      Drawable localDrawable = getCurrentDrawable();
      if (mThumb == null) {
        i = 0;
      } else {
        i = mThumb.getIntrinsicHeight();
      }
      int j = 0;
      int k = 0;
      if (localDrawable != null)
      {
        j = Math.max(mMinWidth, Math.min(mMaxWidth, localDrawable.getIntrinsicWidth()));
        k = Math.max(i, Math.max(mMinHeight, Math.min(mMaxHeight, localDrawable.getIntrinsicHeight())));
      }
      int m = mPaddingLeft;
      int i = mPaddingRight;
      int n = mPaddingTop;
      int i1 = mPaddingBottom;
      setMeasuredDimension(resolveSizeAndState(j + (m + i), paramInt1, 0), resolveSizeAndState(k + (n + i1), paramInt2, 0));
      return;
    }
    finally {}
  }
  
  public void onResolveDrawables(int paramInt)
  {
    super.onResolveDrawables(paramInt);
    if (mThumb != null) {
      mThumb.setLayoutDirection(paramInt);
    }
  }
  
  public void onRtlPropertiesChanged(int paramInt)
  {
    super.onRtlPropertiesChanged(paramInt);
    Drawable localDrawable = mThumb;
    if (localDrawable != null)
    {
      setThumbPos(getWidth(), localDrawable, getScale(), Integer.MIN_VALUE);
      invalidate();
    }
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    updateThumbAndTrackPos(paramInt1, paramInt2);
  }
  
  void onStartTrackingTouch()
  {
    mIsDragging = true;
  }
  
  void onStopTrackingTouch()
  {
    mIsDragging = false;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if ((mIsUserSeekable) && (isEnabled()))
    {
      switch (paramMotionEvent.getAction())
      {
      default: 
        break;
      case 3: 
        if (mIsDragging)
        {
          onStopTrackingTouch();
          setPressed(false);
        }
        invalidate();
        break;
      case 2: 
        if (mIsDragging) {
          trackTouchEvent(paramMotionEvent);
        } else if (Math.abs(paramMotionEvent.getX() - mTouchDownX) > mScaledTouchSlop) {
          startDrag(paramMotionEvent);
        }
        break;
      case 1: 
        if (mIsDragging)
        {
          trackTouchEvent(paramMotionEvent);
          onStopTrackingTouch();
          setPressed(false);
        }
        else
        {
          onStartTrackingTouch();
          trackTouchEvent(paramMotionEvent);
          onStopTrackingTouch();
        }
        invalidate();
        break;
      case 0: 
        if (isInScrollingContainer()) {
          mTouchDownX = paramMotionEvent.getX();
        } else {
          startDrag(paramMotionEvent);
        }
        break;
      }
      return true;
    }
    return false;
  }
  
  void onVisualProgressChanged(int paramInt, float paramFloat)
  {
    super.onVisualProgressChanged(paramInt, paramFloat);
    if (paramInt == 16908301)
    {
      Drawable localDrawable = mThumb;
      if (localDrawable != null)
      {
        setThumbPos(getWidth(), localDrawable, paramFloat, Integer.MIN_VALUE);
        invalidate();
      }
    }
  }
  
  public boolean performAccessibilityActionInternal(int paramInt, Bundle paramBundle)
  {
    if (super.performAccessibilityActionInternal(paramInt, paramBundle)) {
      return true;
    }
    if (!isEnabled()) {
      return false;
    }
    if ((paramInt != 4096) && (paramInt != 8192))
    {
      if (paramInt != 16908349) {
        return false;
      }
      if (!canUserSetProgress()) {
        return false;
      }
      if ((paramBundle != null) && (paramBundle.containsKey("android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE"))) {
        return setProgressInternal((int)paramBundle.getFloat("android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE"), true, true);
      }
      return false;
    }
    if (!canUserSetProgress()) {
      return false;
    }
    int i = Math.max(1, Math.round((getMax() - getMin()) / 20.0F));
    int j = i;
    if (paramInt == 8192) {
      j = -i;
    }
    if (setProgressInternal(getProgress() + j, true, true))
    {
      onKeyChange();
      return true;
    }
    return false;
  }
  
  public void setKeyProgressIncrement(int paramInt)
  {
    if (paramInt < 0) {
      paramInt = -paramInt;
    }
    mKeyProgressIncrement = paramInt;
  }
  
  public void setMax(int paramInt)
  {
    try
    {
      super.setMax(paramInt);
      paramInt = getMax() - getMin();
      if ((mKeyProgressIncrement == 0) || (paramInt / mKeyProgressIncrement > 20)) {
        setKeyProgressIncrement(Math.max(1, Math.round(paramInt / 20.0F)));
      }
      return;
    }
    finally {}
  }
  
  public void setMin(int paramInt)
  {
    try
    {
      super.setMin(paramInt);
      paramInt = getMax() - getMin();
      if ((mKeyProgressIncrement == 0) || (paramInt / mKeyProgressIncrement > 20)) {
        setKeyProgressIncrement(Math.max(1, Math.round(paramInt / 20.0F)));
      }
      return;
    }
    finally {}
  }
  
  public void setSplitTrack(boolean paramBoolean)
  {
    mSplitTrack = paramBoolean;
    invalidate();
  }
  
  public void setThumb(Drawable paramDrawable)
  {
    int i;
    if ((mThumb != null) && (paramDrawable != mThumb))
    {
      mThumb.setCallback(null);
      i = 1;
    }
    else
    {
      i = 0;
    }
    if (paramDrawable != null)
    {
      paramDrawable.setCallback(this);
      if (canResolveLayoutDirection()) {
        paramDrawable.setLayoutDirection(getLayoutDirection());
      }
      mThumbOffset = (paramDrawable.getIntrinsicWidth() / 2);
      if ((i != 0) && ((paramDrawable.getIntrinsicWidth() != mThumb.getIntrinsicWidth()) || (paramDrawable.getIntrinsicHeight() != mThumb.getIntrinsicHeight()))) {
        requestLayout();
      }
    }
    mThumb = paramDrawable;
    applyThumbTint();
    invalidate();
    if (i != 0)
    {
      updateThumbAndTrackPos(getWidth(), getHeight());
      if ((paramDrawable != null) && (paramDrawable.isStateful())) {
        paramDrawable.setState(getDrawableState());
      }
    }
  }
  
  public void setThumbOffset(int paramInt)
  {
    mThumbOffset = paramInt;
    invalidate();
  }
  
  public void setThumbTintList(ColorStateList paramColorStateList)
  {
    mThumbTintList = paramColorStateList;
    mHasThumbTint = true;
    applyThumbTint();
  }
  
  public void setThumbTintMode(PorterDuff.Mode paramMode)
  {
    mThumbTintMode = paramMode;
    mHasThumbTintMode = true;
    applyThumbTint();
  }
  
  public void setTickMark(Drawable paramDrawable)
  {
    if (mTickMark != null) {
      mTickMark.setCallback(null);
    }
    mTickMark = paramDrawable;
    if (paramDrawable != null)
    {
      paramDrawable.setCallback(this);
      paramDrawable.setLayoutDirection(getLayoutDirection());
      if (paramDrawable.isStateful()) {
        paramDrawable.setState(getDrawableState());
      }
      applyTickMarkTint();
    }
    invalidate();
  }
  
  public void setTickMarkTintList(ColorStateList paramColorStateList)
  {
    mTickMarkTintList = paramColorStateList;
    mHasTickMarkTint = true;
    applyTickMarkTint();
  }
  
  public void setTickMarkTintMode(PorterDuff.Mode paramMode)
  {
    mTickMarkTintMode = paramMode;
    mHasTickMarkTintMode = true;
    applyTickMarkTint();
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable)
  {
    boolean bool;
    if ((paramDrawable != mThumb) && (paramDrawable != mTickMark) && (!super.verifyDrawable(paramDrawable))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
}
