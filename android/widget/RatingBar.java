package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import com.android.internal.R.styleable;

public class RatingBar
  extends AbsSeekBar
{
  private int mNumStars = 5;
  private OnRatingBarChangeListener mOnRatingBarChangeListener;
  private int mProgressOnStartTracking;
  
  public RatingBar(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public RatingBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842876);
  }
  
  public RatingBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public RatingBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.RatingBar, paramInt1, paramInt2);
    paramInt1 = paramContext.getInt(0, mNumStars);
    setIsIndicator(paramContext.getBoolean(3, mIsUserSeekable ^ true));
    float f1 = paramContext.getFloat(1, -1.0F);
    float f2 = paramContext.getFloat(2, -1.0F);
    paramContext.recycle();
    if ((paramInt1 > 0) && (paramInt1 != mNumStars)) {
      setNumStars(paramInt1);
    }
    if (f2 >= 0.0F) {
      setStepSize(f2);
    } else {
      setStepSize(0.5F);
    }
    if (f1 >= 0.0F) {
      setRating(f1);
    }
    mTouchProgressOffset = 0.6F;
  }
  
  private float getProgressPerStar()
  {
    if (mNumStars > 0) {
      return 1.0F * getMax() / mNumStars;
    }
    return 1.0F;
  }
  
  private void updateSecondaryProgress(int paramInt)
  {
    float f = getProgressPerStar();
    if (f > 0.0F) {
      setSecondaryProgress((int)(Math.ceil(paramInt / f) * f));
    }
  }
  
  boolean canUserSetProgress()
  {
    boolean bool;
    if ((super.canUserSetProgress()) && (!isIndicator())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  void dispatchRatingChange(boolean paramBoolean)
  {
    if (mOnRatingBarChangeListener != null) {
      mOnRatingBarChangeListener.onRatingChanged(this, getRating(), paramBoolean);
    }
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return RatingBar.class.getName();
  }
  
  Shape getDrawableShape()
  {
    return new RectShape();
  }
  
  public int getNumStars()
  {
    return mNumStars;
  }
  
  public OnRatingBarChangeListener getOnRatingBarChangeListener()
  {
    return mOnRatingBarChangeListener;
  }
  
  public float getRating()
  {
    return getProgress() / getProgressPerStar();
  }
  
  public float getStepSize()
  {
    return getNumStars() / getMax();
  }
  
  public boolean isIndicator()
  {
    return mIsUserSeekable ^ true;
  }
  
  public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfoInternal(paramAccessibilityNodeInfo);
    if (canUserSetProgress()) {
      paramAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_PROGRESS);
    }
  }
  
  void onKeyChange()
  {
    super.onKeyChange();
    dispatchRatingChange(true);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    try
    {
      super.onMeasure(paramInt1, paramInt2);
      if (mSampleWidth > 0) {
        setMeasuredDimension(resolveSizeAndState(mSampleWidth * mNumStars, paramInt1, 0), getMeasuredHeight());
      }
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  void onProgressRefresh(float paramFloat, boolean paramBoolean, int paramInt)
  {
    super.onProgressRefresh(paramFloat, paramBoolean, paramInt);
    updateSecondaryProgress(paramInt);
    if (!paramBoolean) {
      dispatchRatingChange(false);
    }
  }
  
  void onStartTrackingTouch()
  {
    mProgressOnStartTracking = getProgress();
    super.onStartTrackingTouch();
  }
  
  void onStopTrackingTouch()
  {
    super.onStopTrackingTouch();
    if (getProgress() != mProgressOnStartTracking) {
      dispatchRatingChange(true);
    }
  }
  
  public void setIsIndicator(boolean paramBoolean)
  {
    mIsUserSeekable = (paramBoolean ^ true);
    if (paramBoolean) {
      setFocusable(16);
    } else {
      setFocusable(1);
    }
  }
  
  public void setMax(int paramInt)
  {
    if (paramInt <= 0) {
      return;
    }
    try
    {
      super.setMax(paramInt);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void setNumStars(int paramInt)
  {
    if (paramInt <= 0) {
      return;
    }
    mNumStars = paramInt;
    requestLayout();
  }
  
  public void setOnRatingBarChangeListener(OnRatingBarChangeListener paramOnRatingBarChangeListener)
  {
    mOnRatingBarChangeListener = paramOnRatingBarChangeListener;
  }
  
  public void setRating(float paramFloat)
  {
    setProgress(Math.round(getProgressPerStar() * paramFloat));
  }
  
  public void setStepSize(float paramFloat)
  {
    if (paramFloat <= 0.0F) {
      return;
    }
    paramFloat = mNumStars / paramFloat;
    int i = (int)(paramFloat / getMax() * getProgress());
    setMax((int)paramFloat);
    setProgress(i);
  }
  
  public static abstract interface OnRatingBarChangeListener
  {
    public abstract void onRatingChanged(RatingBar paramRatingBar, float paramFloat, boolean paramBoolean);
  }
}
