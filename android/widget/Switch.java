package android.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.ColorStateList;
import android.content.res.CompatibilityInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Insets;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout.Builder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.AllCapsTransformationMethod;
import android.text.method.TransformationMethod2;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.FloatProperty;
import android.util.MathUtils;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.ViewStructure;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.android.internal.R.styleable;
import java.util.List;

public class Switch
  extends CompoundButton
{
  private static final int[] CHECKED_STATE_SET = { 16842912 };
  private static final int MONOSPACE = 3;
  private static final int SANS = 1;
  private static final int SERIF = 2;
  private static final int THUMB_ANIMATION_DURATION = 250;
  private static final FloatProperty<Switch> THUMB_POS = new FloatProperty("thumbPos")
  {
    public Float get(Switch paramAnonymousSwitch)
    {
      return Float.valueOf(mThumbPosition);
    }
    
    public void setValue(Switch paramAnonymousSwitch, float paramAnonymousFloat)
    {
      paramAnonymousSwitch.setThumbPosition(paramAnonymousFloat);
    }
  };
  private static final int TOUCH_MODE_DOWN = 1;
  private static final int TOUCH_MODE_DRAGGING = 2;
  private static final int TOUCH_MODE_IDLE = 0;
  private static final int WEATHERFONTREG = 4;
  private static final int WEATHERFONTREG2 = 5;
  private static final int WEATHERFONTZENUI5 = 6;
  private boolean mHasThumbTint = false;
  private boolean mHasThumbTintMode = false;
  private boolean mHasTrackTint = false;
  private boolean mHasTrackTintMode = false;
  private int mMinFlingVelocity;
  private Layout mOffLayout;
  private Layout mOnLayout;
  private ObjectAnimator mPositionAnimator;
  private boolean mShowText;
  private boolean mSplitTrack;
  private int mSwitchBottom;
  private int mSwitchHeight;
  private int mSwitchLeft;
  private int mSwitchMinWidth;
  private int mSwitchPadding;
  private int mSwitchRight;
  private int mSwitchTop;
  private TransformationMethod2 mSwitchTransformationMethod;
  private int mSwitchWidth;
  private final Rect mTempRect = new Rect();
  private ColorStateList mTextColors;
  private CharSequence mTextOff;
  private CharSequence mTextOn;
  private TextPaint mTextPaint = new TextPaint(1);
  private Drawable mThumbDrawable;
  private float mThumbPosition;
  private int mThumbTextPadding;
  private ColorStateList mThumbTintList = null;
  private PorterDuff.Mode mThumbTintMode = null;
  private int mThumbWidth;
  private int mTouchMode;
  private int mTouchSlop;
  private float mTouchX;
  private float mTouchY;
  private Drawable mTrackDrawable;
  private ColorStateList mTrackTintList = null;
  private PorterDuff.Mode mTrackTintMode = null;
  private boolean mUseFallbackLineSpacing;
  private VelocityTracker mVelocityTracker = VelocityTracker.obtain();
  
  public Switch(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public Switch(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16843839);
  }
  
  public Switch(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public Switch(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    Object localObject = getResources();
    mTextPaint.density = getDisplayMetricsdensity;
    mTextPaint.setCompatibilityScaling(getCompatibilityInfoapplicationScale);
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.Switch, paramInt1, paramInt2);
    mThumbDrawable = paramAttributeSet.getDrawable(2);
    if (mThumbDrawable != null) {
      mThumbDrawable.setCallback(this);
    }
    mTrackDrawable = paramAttributeSet.getDrawable(4);
    if (mTrackDrawable != null) {
      mTrackDrawable.setCallback(this);
    }
    mTextOn = paramAttributeSet.getText(0);
    mTextOff = paramAttributeSet.getText(1);
    mShowText = paramAttributeSet.getBoolean(11, true);
    mThumbTextPadding = paramAttributeSet.getDimensionPixelSize(7, 0);
    mSwitchMinWidth = paramAttributeSet.getDimensionPixelSize(5, 0);
    mSwitchPadding = paramAttributeSet.getDimensionPixelSize(6, 0);
    mSplitTrack = paramAttributeSet.getBoolean(8, false);
    boolean bool;
    if (getApplicationInfotargetSdkVersion >= 28) {
      bool = true;
    } else {
      bool = false;
    }
    mUseFallbackLineSpacing = bool;
    localObject = paramAttributeSet.getColorStateList(9);
    if (localObject != null)
    {
      mThumbTintList = ((ColorStateList)localObject);
      mHasThumbTint = true;
    }
    localObject = Drawable.parseTintMode(paramAttributeSet.getInt(10, -1), null);
    if (mThumbTintMode != localObject)
    {
      mThumbTintMode = ((PorterDuff.Mode)localObject);
      mHasThumbTintMode = true;
    }
    if ((mHasThumbTint) || (mHasThumbTintMode)) {
      applyThumbTint();
    }
    localObject = paramAttributeSet.getColorStateList(12);
    if (localObject != null)
    {
      mTrackTintList = ((ColorStateList)localObject);
      mHasTrackTint = true;
    }
    localObject = Drawable.parseTintMode(paramAttributeSet.getInt(13, -1), null);
    if (mTrackTintMode != localObject)
    {
      mTrackTintMode = ((PorterDuff.Mode)localObject);
      mHasTrackTintMode = true;
    }
    if ((mHasTrackTint) || (mHasTrackTintMode)) {
      applyTrackTint();
    }
    paramInt1 = paramAttributeSet.getResourceId(3, 0);
    if (paramInt1 != 0) {
      setSwitchTextAppearance(paramContext, paramInt1);
    }
    paramAttributeSet.recycle();
    paramContext = ViewConfiguration.get(paramContext);
    mTouchSlop = paramContext.getScaledTouchSlop();
    mMinFlingVelocity = paramContext.getScaledMinimumFlingVelocity();
    refreshDrawableState();
    setChecked(isChecked());
  }
  
  private void animateThumbToCheckedState(boolean paramBoolean)
  {
    float f;
    if (paramBoolean) {
      f = 1.0F;
    } else {
      f = 0.0F;
    }
    mPositionAnimator = ObjectAnimator.ofFloat(this, THUMB_POS, new float[] { f });
    mPositionAnimator.setDuration(250L);
    mPositionAnimator.setAutoCancel(true);
    mPositionAnimator.start();
  }
  
  private void applyThumbTint()
  {
    if ((mThumbDrawable != null) && ((mHasThumbTint) || (mHasThumbTintMode)))
    {
      mThumbDrawable = mThumbDrawable.mutate();
      if (mHasThumbTint) {
        mThumbDrawable.setTintList(mThumbTintList);
      }
      if (mHasThumbTintMode) {
        mThumbDrawable.setTintMode(mThumbTintMode);
      }
      if (mThumbDrawable.isStateful()) {
        mThumbDrawable.setState(getDrawableState());
      }
    }
  }
  
  private void applyTrackTint()
  {
    if ((mTrackDrawable != null) && ((mHasTrackTint) || (mHasTrackTintMode)))
    {
      mTrackDrawable = mTrackDrawable.mutate();
      if (mHasTrackTint) {
        mTrackDrawable.setTintList(mTrackTintList);
      }
      if (mHasTrackTintMode) {
        mTrackDrawable.setTintMode(mTrackTintMode);
      }
      if (mTrackDrawable.isStateful()) {
        mTrackDrawable.setState(getDrawableState());
      }
    }
  }
  
  private void cancelPositionAnimator()
  {
    if (mPositionAnimator != null) {
      mPositionAnimator.cancel();
    }
  }
  
  private void cancelSuperTouch(MotionEvent paramMotionEvent)
  {
    paramMotionEvent = MotionEvent.obtain(paramMotionEvent);
    paramMotionEvent.setAction(3);
    super.onTouchEvent(paramMotionEvent);
    paramMotionEvent.recycle();
  }
  
  private boolean getTargetCheckedState()
  {
    boolean bool;
    if (mThumbPosition > 0.5F) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private int getThumbOffset()
  {
    float f;
    if (isLayoutRtl()) {
      f = 1.0F - mThumbPosition;
    } else {
      f = mThumbPosition;
    }
    return (int)(getThumbScrollRange() * f + 0.5F);
  }
  
  private int getThumbScrollRange()
  {
    if (mTrackDrawable != null)
    {
      Rect localRect = mTempRect;
      mTrackDrawable.getPadding(localRect);
      Insets localInsets;
      if (mThumbDrawable != null) {
        localInsets = mThumbDrawable.getOpticalInsets();
      } else {
        localInsets = Insets.NONE;
      }
      return mSwitchWidth - mThumbWidth - left - right - left - right;
    }
    return 0;
  }
  
  private boolean hitThumb(float paramFloat1, float paramFloat2)
  {
    Drawable localDrawable = mThumbDrawable;
    boolean bool1 = false;
    if (localDrawable == null) {
      return false;
    }
    int i = getThumbOffset();
    mThumbDrawable.getPadding(mTempRect);
    int j = mSwitchTop;
    int k = mTouchSlop;
    int m = mSwitchLeft + i - mTouchSlop;
    int n = mThumbWidth;
    int i1 = mTempRect.left;
    int i2 = mTempRect.right;
    i = mTouchSlop;
    int i3 = mSwitchBottom;
    int i4 = mTouchSlop;
    boolean bool2 = bool1;
    if (paramFloat1 > m)
    {
      bool2 = bool1;
      if (paramFloat1 < n + m + i1 + i2 + i)
      {
        bool2 = bool1;
        if (paramFloat2 > j - k)
        {
          bool2 = bool1;
          if (paramFloat2 < i3 + i4) {
            bool2 = true;
          }
        }
      }
    }
    return bool2;
  }
  
  private Layout makeLayout(CharSequence paramCharSequence)
  {
    if (mSwitchTransformationMethod != null) {
      paramCharSequence = mSwitchTransformationMethod.getTransformation(paramCharSequence, this);
    }
    int i = (int)Math.ceil(Layout.getDesiredWidth(paramCharSequence, 0, paramCharSequence.length(), mTextPaint, getTextDirectionHeuristic()));
    return StaticLayout.Builder.obtain(paramCharSequence, 0, paramCharSequence.length(), mTextPaint, i).setUseLineSpacingFromFallbacks(mUseFallbackLineSpacing).build();
  }
  
  private void onProvideAutoFillStructureForAssistOrAutofill(ViewStructure paramViewStructure)
  {
    CharSequence localCharSequence1;
    if (isChecked()) {
      localCharSequence1 = mTextOn;
    } else {
      localCharSequence1 = mTextOff;
    }
    if (!TextUtils.isEmpty(localCharSequence1))
    {
      CharSequence localCharSequence2 = paramViewStructure.getText();
      if (TextUtils.isEmpty(localCharSequence2))
      {
        paramViewStructure.setText(localCharSequence1);
      }
      else
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(localCharSequence2);
        localStringBuilder.append(' ');
        localStringBuilder.append(localCharSequence1);
        paramViewStructure.setText(localStringBuilder);
      }
    }
  }
  
  private void setSwitchTypefaceByIndex(int paramInt1, int paramInt2)
  {
    Typeface localTypeface = null;
    switch (paramInt1)
    {
    default: 
      break;
    case 6: 
      localTypeface = Typeface.WEATHERFONTZENUI5;
      break;
    case 5: 
      localTypeface = Typeface.WEATHERFONTREG2;
      break;
    case 4: 
      localTypeface = Typeface.WEATHERFONTREG;
      break;
    case 3: 
      localTypeface = Typeface.MONOSPACE;
      break;
    case 2: 
      localTypeface = Typeface.SERIF;
      break;
    case 1: 
      localTypeface = Typeface.SANS_SERIF;
    }
    setSwitchTypeface(localTypeface, paramInt2);
  }
  
  private void setThumbPosition(float paramFloat)
  {
    mThumbPosition = paramFloat;
    invalidate();
  }
  
  private void stopDrag(MotionEvent paramMotionEvent)
  {
    mTouchMode = 0;
    int i = paramMotionEvent.getAction();
    boolean bool1 = true;
    if ((i == 1) && (isEnabled())) {
      i = 1;
    } else {
      i = 0;
    }
    boolean bool2 = isChecked();
    if (i != 0)
    {
      mVelocityTracker.computeCurrentVelocity(1000);
      float f = mVelocityTracker.getXVelocity();
      if (Math.abs(f) > mMinFlingVelocity)
      {
        if (isLayoutRtl())
        {
          if (f < 0.0F) {}
        }
        else {
          for (;;)
          {
            bool1 = false;
            break;
            if (f <= 0.0F) {}
          }
        }
      }
      else {
        bool1 = getTargetCheckedState();
      }
    }
    else
    {
      bool1 = bool2;
    }
    if (bool1 != bool2) {
      playSoundEffect(0);
    }
    setChecked(bool1);
    cancelSuperTouch(paramMotionEvent);
  }
  
  public void draw(Canvas paramCanvas)
  {
    Rect localRect = mTempRect;
    int i = mSwitchLeft;
    int j = mSwitchTop;
    int k = mSwitchRight;
    int m = mSwitchBottom;
    int n = getThumbOffset() + i;
    Object localObject;
    if (mThumbDrawable != null) {
      localObject = mThumbDrawable.getOpticalInsets();
    } else {
      localObject = Insets.NONE;
    }
    int i1 = n;
    if (mTrackDrawable != null)
    {
      mTrackDrawable.getPadding(localRect);
      int i2 = n + left;
      n = j;
      int i3 = m;
      int i4 = i;
      int i5 = n;
      int i6 = k;
      int i7 = i3;
      if (localObject != Insets.NONE)
      {
        i1 = i;
        if (left > left) {
          i1 = i + (left - left);
        }
        i = n;
        if (top > top) {
          i = n + (top - top);
        }
        n = k;
        if (right > right) {
          n = k - (right - right);
        }
        i4 = i1;
        i5 = i;
        i6 = n;
        i7 = i3;
        if (bottom > bottom)
        {
          i7 = i3 - (bottom - bottom);
          i6 = n;
          i5 = i;
          i4 = i1;
        }
      }
      mTrackDrawable.setBounds(i4, i5, i6, i7);
      i1 = i2;
    }
    if (mThumbDrawable != null)
    {
      mThumbDrawable.getPadding(localRect);
      k = i1 - left;
      i1 = mThumbWidth + i1 + right;
      mThumbDrawable.setBounds(k, j, i1, m);
      localObject = getBackground();
      if (localObject != null) {
        ((Drawable)localObject).setHotspotBounds(k, j, i1, m);
      }
    }
    super.draw(paramCanvas);
  }
  
  public void drawableHotspotChanged(float paramFloat1, float paramFloat2)
  {
    super.drawableHotspotChanged(paramFloat1, paramFloat2);
    if (mThumbDrawable != null) {
      mThumbDrawable.setHotspot(paramFloat1, paramFloat2);
    }
    if (mTrackDrawable != null) {
      mTrackDrawable.setHotspot(paramFloat1, paramFloat2);
    }
  }
  
  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    int[] arrayOfInt = getDrawableState();
    boolean bool1 = false;
    Drawable localDrawable = mThumbDrawable;
    boolean bool2 = bool1;
    if (localDrawable != null)
    {
      bool2 = bool1;
      if (localDrawable.isStateful()) {
        bool2 = false | localDrawable.setState(arrayOfInt);
      }
    }
    localDrawable = mTrackDrawable;
    bool1 = bool2;
    if (localDrawable != null)
    {
      bool1 = bool2;
      if (localDrawable.isStateful()) {
        bool1 = bool2 | localDrawable.setState(arrayOfInt);
      }
    }
    if (bool1) {
      invalidate();
    }
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return Switch.class.getName();
  }
  
  public int getCompoundPaddingLeft()
  {
    if (!isLayoutRtl()) {
      return super.getCompoundPaddingLeft();
    }
    int i = super.getCompoundPaddingLeft() + mSwitchWidth;
    int j = i;
    if (!TextUtils.isEmpty(getText())) {
      j = i + mSwitchPadding;
    }
    return j;
  }
  
  public int getCompoundPaddingRight()
  {
    if (isLayoutRtl()) {
      return super.getCompoundPaddingRight();
    }
    int i = super.getCompoundPaddingRight() + mSwitchWidth;
    int j = i;
    if (!TextUtils.isEmpty(getText())) {
      j = i + mSwitchPadding;
    }
    return j;
  }
  
  public boolean getShowText()
  {
    return mShowText;
  }
  
  public boolean getSplitTrack()
  {
    return mSplitTrack;
  }
  
  public int getSwitchMinWidth()
  {
    return mSwitchMinWidth;
  }
  
  public int getSwitchPadding()
  {
    return mSwitchPadding;
  }
  
  public CharSequence getTextOff()
  {
    return mTextOff;
  }
  
  public CharSequence getTextOn()
  {
    return mTextOn;
  }
  
  public Drawable getThumbDrawable()
  {
    return mThumbDrawable;
  }
  
  public int getThumbTextPadding()
  {
    return mThumbTextPadding;
  }
  
  public ColorStateList getThumbTintList()
  {
    return mThumbTintList;
  }
  
  public PorterDuff.Mode getThumbTintMode()
  {
    return mThumbTintMode;
  }
  
  public Drawable getTrackDrawable()
  {
    return mTrackDrawable;
  }
  
  public ColorStateList getTrackTintList()
  {
    return mTrackTintList;
  }
  
  public PorterDuff.Mode getTrackTintMode()
  {
    return mTrackTintMode;
  }
  
  public void jumpDrawablesToCurrentState()
  {
    super.jumpDrawablesToCurrentState();
    if (mThumbDrawable != null) {
      mThumbDrawable.jumpToCurrentState();
    }
    if (mTrackDrawable != null) {
      mTrackDrawable.jumpToCurrentState();
    }
    if ((mPositionAnimator != null) && (mPositionAnimator.isStarted()))
    {
      mPositionAnimator.end();
      mPositionAnimator = null;
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
    Object localObject1 = mTempRect;
    Drawable localDrawable = mTrackDrawable;
    if (localDrawable != null) {
      localDrawable.getPadding((Rect)localObject1);
    } else {
      ((Rect)localObject1).setEmpty();
    }
    int i = mSwitchTop;
    int j = mSwitchBottom;
    int k = top;
    int m = bottom;
    Object localObject2 = mThumbDrawable;
    Object localObject3;
    int n;
    if (localDrawable != null) {
      if ((mSplitTrack) && (localObject2 != null))
      {
        localObject3 = ((Drawable)localObject2).getOpticalInsets();
        ((Drawable)localObject2).copyBounds((Rect)localObject1);
        left += left;
        right -= right;
        n = paramCanvas.save();
        paramCanvas.clipRect((Rect)localObject1, Region.Op.DIFFERENCE);
        localDrawable.draw(paramCanvas);
        paramCanvas.restoreToCount(n);
      }
      else
      {
        localDrawable.draw(paramCanvas);
      }
    }
    int i1 = paramCanvas.save();
    if (localObject2 != null) {
      ((Drawable)localObject2).draw(paramCanvas);
    }
    if (getTargetCheckedState()) {
      localObject1 = mOnLayout;
    } else {
      localObject1 = mOffLayout;
    }
    if (localObject1 != null)
    {
      localObject3 = getDrawableState();
      if (mTextColors != null) {
        mTextPaint.setColor(mTextColors.getColorForState((int[])localObject3, 0));
      }
      mTextPaint.drawableState = ((int[])localObject3);
      if (localObject2 != null)
      {
        localObject2 = ((Drawable)localObject2).getBounds();
        n = left + right;
      }
      else
      {
        n = getWidth();
      }
      int i2 = n / 2;
      n = ((Layout)localObject1).getWidth() / 2;
      i = (k + i + (j - m)) / 2;
      j = ((Layout)localObject1).getHeight() / 2;
      paramCanvas.translate(i2 - n, i - j);
      ((Layout)localObject1).draw(paramCanvas);
    }
    paramCanvas.restoreToCount(i1);
  }
  
  public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfoInternal(paramAccessibilityNodeInfo);
    CharSequence localCharSequence1;
    if (isChecked()) {
      localCharSequence1 = mTextOn;
    } else {
      localCharSequence1 = mTextOff;
    }
    if (!TextUtils.isEmpty(localCharSequence1))
    {
      CharSequence localCharSequence2 = paramAccessibilityNodeInfo.getText();
      if (TextUtils.isEmpty(localCharSequence2))
      {
        paramAccessibilityNodeInfo.setText(localCharSequence1);
      }
      else
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(localCharSequence2);
        localStringBuilder.append(' ');
        localStringBuilder.append(localCharSequence1);
        paramAccessibilityNodeInfo.setText(localStringBuilder);
      }
    }
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    paramInt2 = 0;
    paramInt1 = 0;
    if (mThumbDrawable != null)
    {
      Rect localRect = mTempRect;
      if (mTrackDrawable != null) {
        mTrackDrawable.getPadding(localRect);
      } else {
        localRect.setEmpty();
      }
      Insets localInsets = mThumbDrawable.getOpticalInsets();
      paramInt2 = Math.max(0, left - left);
      paramInt1 = Math.max(0, right - right);
    }
    if (isLayoutRtl())
    {
      paramInt3 = getPaddingLeft() + paramInt2;
      paramInt4 = mSwitchWidth + paramInt3 - paramInt2 - paramInt1;
    }
    else
    {
      paramInt4 = getWidth() - getPaddingRight() - paramInt1;
      paramInt3 = paramInt4 - mSwitchWidth + paramInt2 + paramInt1;
    }
    paramInt1 = getGravity() & 0x70;
    if (paramInt1 != 16)
    {
      if (paramInt1 != 80)
      {
        paramInt1 = getPaddingTop();
        paramInt2 = mSwitchHeight + paramInt1;
      }
      else
      {
        paramInt2 = getHeight() - getPaddingBottom();
        paramInt1 = paramInt2 - mSwitchHeight;
      }
    }
    else
    {
      paramInt1 = (getPaddingTop() + getHeight() - getPaddingBottom()) / 2 - mSwitchHeight / 2;
      paramInt2 = mSwitchHeight + paramInt1;
    }
    mSwitchLeft = paramInt3;
    mSwitchTop = paramInt1;
    mSwitchBottom = paramInt2;
    mSwitchRight = paramInt4;
  }
  
  public void onMeasure(int paramInt1, int paramInt2)
  {
    if (mShowText)
    {
      if (mOnLayout == null) {
        mOnLayout = makeLayout(mTextOn);
      }
      if (mOffLayout == null) {
        mOffLayout = makeLayout(mTextOff);
      }
    }
    Rect localRect = mTempRect;
    Object localObject = mThumbDrawable;
    int i = 0;
    int j;
    if (localObject != null)
    {
      mThumbDrawable.getPadding(localRect);
      j = mThumbDrawable.getIntrinsicWidth() - left - right;
      k = mThumbDrawable.getIntrinsicHeight();
    }
    else
    {
      j = 0;
      k = 0;
    }
    if (mShowText) {
      m = Math.max(mOnLayout.getWidth(), mOffLayout.getWidth()) + mThumbTextPadding * 2;
    } else {
      m = 0;
    }
    mThumbWidth = Math.max(m, j);
    if (mTrackDrawable != null)
    {
      mTrackDrawable.getPadding(localRect);
      j = mTrackDrawable.getIntrinsicHeight();
    }
    else
    {
      localRect.setEmpty();
      j = i;
    }
    int n = left;
    int i1 = right;
    i = n;
    int m = i1;
    if (mThumbDrawable != null)
    {
      localObject = mThumbDrawable.getOpticalInsets();
      i = Math.max(n, left);
      m = Math.max(i1, right);
    }
    m = Math.max(mSwitchMinWidth, 2 * mThumbWidth + i + m);
    int k = Math.max(j, k);
    mSwitchWidth = m;
    mSwitchHeight = k;
    super.onMeasure(paramInt1, paramInt2);
    if (getMeasuredHeight() < k) {
      setMeasuredDimension(getMeasuredWidthAndState(), k);
    }
  }
  
  public void onPopulateAccessibilityEventInternal(AccessibilityEvent paramAccessibilityEvent)
  {
    super.onPopulateAccessibilityEventInternal(paramAccessibilityEvent);
    CharSequence localCharSequence;
    if (isChecked()) {
      localCharSequence = mTextOn;
    } else {
      localCharSequence = mTextOff;
    }
    if (localCharSequence != null) {
      paramAccessibilityEvent.getText().add(localCharSequence);
    }
  }
  
  public void onProvideAutofillStructure(ViewStructure paramViewStructure, int paramInt)
  {
    super.onProvideAutofillStructure(paramViewStructure, paramInt);
    onProvideAutoFillStructureForAssistOrAutofill(paramViewStructure);
  }
  
  public void onProvideStructure(ViewStructure paramViewStructure)
  {
    super.onProvideStructure(paramViewStructure);
    onProvideAutoFillStructureForAssistOrAutofill(paramViewStructure);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    mVelocityTracker.addMovement(paramMotionEvent);
    float f2;
    float f3;
    switch (paramMotionEvent.getActionMasked())
    {
    default: 
      break;
    case 2: 
      switch (mTouchMode)
      {
      default: 
        break;
      case 2: 
        float f1 = paramMotionEvent.getX();
        int i = getThumbScrollRange();
        f2 = f1 - mTouchX;
        if (i != 0) {
          f2 /= i;
        } else if (f2 > 0.0F) {
          f2 = 1.0F;
        } else {
          f2 = -1.0F;
        }
        f3 = f2;
        if (isLayoutRtl()) {
          f3 = -f2;
        }
        f2 = MathUtils.constrain(mThumbPosition + f3, 0.0F, 1.0F);
        if (f2 != mThumbPosition)
        {
          mTouchX = f1;
          setThumbPosition(f2);
        }
        return true;
      case 1: 
        f3 = paramMotionEvent.getX();
        f2 = paramMotionEvent.getY();
        if ((Math.abs(f3 - mTouchX) > mTouchSlop) || (Math.abs(f2 - mTouchY) > mTouchSlop))
        {
          mTouchMode = 2;
          getParent().requestDisallowInterceptTouchEvent(true);
          mTouchX = f3;
          mTouchY = f2;
          return true;
        }
        break;
      }
      break;
    case 1: 
    case 3: 
      if (mTouchMode == 2)
      {
        stopDrag(paramMotionEvent);
        super.onTouchEvent(paramMotionEvent);
        return true;
      }
      mTouchMode = 0;
      mVelocityTracker.clear();
      break;
    case 0: 
      f2 = paramMotionEvent.getX();
      f3 = paramMotionEvent.getY();
      if ((isEnabled()) && (hitThumb(f2, f3)))
      {
        mTouchMode = 1;
        mTouchX = f2;
        mTouchY = f3;
      }
      break;
    }
    return super.onTouchEvent(paramMotionEvent);
  }
  
  public void setChecked(boolean paramBoolean)
  {
    super.setChecked(paramBoolean);
    paramBoolean = isChecked();
    if ((isAttachedToWindow()) && (isLaidOut()))
    {
      animateThumbToCheckedState(paramBoolean);
    }
    else
    {
      cancelPositionAnimator();
      float f;
      if (paramBoolean) {
        f = 1.0F;
      } else {
        f = 0.0F;
      }
      setThumbPosition(f);
    }
  }
  
  public void setShowText(boolean paramBoolean)
  {
    if (mShowText != paramBoolean)
    {
      mShowText = paramBoolean;
      requestLayout();
    }
  }
  
  public void setSplitTrack(boolean paramBoolean)
  {
    mSplitTrack = paramBoolean;
    invalidate();
  }
  
  public void setSwitchMinWidth(int paramInt)
  {
    mSwitchMinWidth = paramInt;
    requestLayout();
  }
  
  public void setSwitchPadding(int paramInt)
  {
    mSwitchPadding = paramInt;
    requestLayout();
  }
  
  public void setSwitchTextAppearance(Context paramContext, int paramInt)
  {
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramInt, R.styleable.TextAppearance);
    paramContext = localTypedArray.getColorStateList(3);
    if (paramContext != null) {
      mTextColors = paramContext;
    } else {
      mTextColors = getTextColors();
    }
    paramInt = localTypedArray.getDimensionPixelSize(0, 0);
    if ((paramInt != 0) && (paramInt != mTextPaint.getTextSize()))
    {
      mTextPaint.setTextSize(paramInt);
      requestLayout();
    }
    setSwitchTypefaceByIndex(localTypedArray.getInt(1, -1), localTypedArray.getInt(2, -1));
    if (localTypedArray.getBoolean(11, false))
    {
      mSwitchTransformationMethod = new AllCapsTransformationMethod(getContext());
      mSwitchTransformationMethod.setLengthChangesAllowed(true);
    }
    else
    {
      mSwitchTransformationMethod = null;
    }
    localTypedArray.recycle();
  }
  
  public void setSwitchTypeface(Typeface paramTypeface)
  {
    if (mTextPaint.getTypeface() != paramTypeface)
    {
      mTextPaint.setTypeface(paramTypeface);
      requestLayout();
      invalidate();
    }
  }
  
  public void setSwitchTypeface(Typeface paramTypeface, int paramInt)
  {
    float f = 0.0F;
    boolean bool = false;
    if (paramInt > 0)
    {
      if (paramTypeface == null) {
        paramTypeface = Typeface.defaultFromStyle(paramInt);
      } else {
        paramTypeface = Typeface.create(paramTypeface, paramInt);
      }
      setSwitchTypeface(paramTypeface);
      int i;
      if (paramTypeface != null) {
        i = paramTypeface.getStyle();
      } else {
        i = 0;
      }
      paramInt = i & paramInt;
      paramTypeface = mTextPaint;
      if ((paramInt & 0x1) != 0) {
        bool = true;
      }
      paramTypeface.setFakeBoldText(bool);
      paramTypeface = mTextPaint;
      if ((paramInt & 0x2) != 0) {
        f = -0.25F;
      }
      paramTypeface.setTextSkewX(f);
    }
    else
    {
      mTextPaint.setFakeBoldText(false);
      mTextPaint.setTextSkewX(0.0F);
      setSwitchTypeface(paramTypeface);
    }
  }
  
  public void setTextOff(CharSequence paramCharSequence)
  {
    mTextOff = paramCharSequence;
    requestLayout();
  }
  
  public void setTextOn(CharSequence paramCharSequence)
  {
    mTextOn = paramCharSequence;
    requestLayout();
  }
  
  public void setThumbDrawable(Drawable paramDrawable)
  {
    if (mThumbDrawable != null) {
      mThumbDrawable.setCallback(null);
    }
    mThumbDrawable = paramDrawable;
    if (paramDrawable != null) {
      paramDrawable.setCallback(this);
    }
    requestLayout();
  }
  
  public void setThumbResource(int paramInt)
  {
    setThumbDrawable(getContext().getDrawable(paramInt));
  }
  
  public void setThumbTextPadding(int paramInt)
  {
    mThumbTextPadding = paramInt;
    requestLayout();
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
  
  public void setTrackDrawable(Drawable paramDrawable)
  {
    if (mTrackDrawable != null) {
      mTrackDrawable.setCallback(null);
    }
    mTrackDrawable = paramDrawable;
    if (paramDrawable != null) {
      paramDrawable.setCallback(this);
    }
    requestLayout();
  }
  
  public void setTrackResource(int paramInt)
  {
    setTrackDrawable(getContext().getDrawable(paramInt));
  }
  
  public void setTrackTintList(ColorStateList paramColorStateList)
  {
    mTrackTintList = paramColorStateList;
    mHasTrackTint = true;
    applyTrackTint();
  }
  
  public void setTrackTintMode(PorterDuff.Mode paramMode)
  {
    mTrackTintMode = paramMode;
    mHasTrackTintMode = true;
    applyTrackTint();
  }
  
  public void toggle()
  {
    setChecked(isChecked() ^ true);
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable)
  {
    boolean bool;
    if ((!super.verifyDrawable(paramDrawable)) && (paramDrawable != mThumbDrawable) && (paramDrawable != mTrackDrawable)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
}
