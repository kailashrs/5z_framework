package android.widget;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.TableMaskFilter;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.RemotableViewMethod;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup.LayoutParams;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.LinearInterpolator;
import com.android.internal.R.styleable;
import java.lang.ref.WeakReference;
import java.util.HashMap;

@RemoteViews.RemoteView
public class StackView
  extends AdapterViewAnimator
{
  private static final int DEFAULT_ANIMATION_DURATION = 400;
  private static final int FRAME_PADDING = 4;
  private static final int GESTURE_NONE = 0;
  private static final int GESTURE_SLIDE_DOWN = 2;
  private static final int GESTURE_SLIDE_UP = 1;
  private static final int INVALID_POINTER = -1;
  private static final int ITEMS_SLIDE_DOWN = 1;
  private static final int ITEMS_SLIDE_UP = 0;
  private static final int MINIMUM_ANIMATION_DURATION = 50;
  private static final int MIN_TIME_BETWEEN_INTERACTION_AND_AUTOADVANCE = 5000;
  private static final long MIN_TIME_BETWEEN_SCROLLS = 100L;
  private static final int NUM_ACTIVE_VIEWS = 5;
  private static final float PERSPECTIVE_SCALE_FACTOR = 0.0F;
  private static final float PERSPECTIVE_SHIFT_FACTOR_X = 0.1F;
  private static final float PERSPECTIVE_SHIFT_FACTOR_Y = 0.1F;
  private static final float SLIDE_UP_RATIO = 0.7F;
  private static final int STACK_RELAYOUT_DURATION = 100;
  private static final float SWIPE_THRESHOLD_RATIO = 0.2F;
  private static HolographicHelper sHolographicHelper;
  private final String TAG = "StackView";
  private int mActivePointerId;
  private int mClickColor;
  private ImageView mClickFeedback;
  private boolean mClickFeedbackIsValid = false;
  private boolean mFirstLayoutHappened = false;
  private int mFramePadding;
  private ImageView mHighlight;
  private float mInitialX;
  private float mInitialY;
  private long mLastInteractionTime = 0L;
  private long mLastScrollTime;
  private int mMaximumVelocity;
  private float mNewPerspectiveShiftX;
  private float mNewPerspectiveShiftY;
  private float mPerspectiveShiftX;
  private float mPerspectiveShiftY;
  private int mResOutColor;
  private int mSlideAmount;
  private int mStackMode;
  private StackSlider mStackSlider;
  private int mSwipeGestureType = 0;
  private int mSwipeThreshold;
  private final Rect mTouchRect = new Rect();
  private int mTouchSlop;
  private boolean mTransitionIsSetup = false;
  private VelocityTracker mVelocityTracker;
  private int mYVelocity = 0;
  private final Rect stackInvalidateRect = new Rect();
  
  public StackView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public StackView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16843838);
  }
  
  public StackView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public StackView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.StackView, paramInt1, paramInt2);
    mResOutColor = paramContext.getColor(1, 0);
    mClickColor = paramContext.getColor(0, 0);
    paramContext.recycle();
    initStackView();
  }
  
  private void beginGestureIfNeeded(float paramFloat)
  {
    if (((int)Math.abs(paramFloat) > mTouchSlop) && (mSwipeGestureType == 0))
    {
      boolean bool = true;
      int i;
      if (paramFloat < 0.0F) {
        i = 1;
      } else {
        i = 2;
      }
      cancelLongPress();
      requestDisallowInterceptTouchEvent(true);
      if (mAdapter == null) {
        return;
      }
      int j = getCount();
      int k;
      if (mStackMode == 0)
      {
        if (i == 2) {
          k = 0;
        } else {
          k = 1;
        }
      }
      else if (i == 2) {
        k = 1;
      } else {
        k = 0;
      }
      int m;
      if ((mLoopViews) && (j == 1) && (((mStackMode == 0) && (i == 1)) || ((mStackMode == 1) && (i == 2)))) {
        m = 1;
      } else {
        m = 0;
      }
      int n;
      if ((mLoopViews) && (j == 1) && (((mStackMode == 1) && (i == 1)) || ((mStackMode == 0) && (i == 2)))) {
        n = 1;
      } else {
        n = 0;
      }
      if ((mLoopViews) && (n == 0) && (m == 0))
      {
        n = 0;
        m = k;
        k = n;
      }
      else if ((mCurrentWindowStartUnbounded + k != -1) && (n == 0))
      {
        if ((mCurrentWindowStartUnbounded + k != j - 1) && (m == 0))
        {
          n = 0;
          m = k;
          k = n;
        }
        else
        {
          n = 2;
          m = k;
          k = n;
        }
      }
      else
      {
        m = k + 1;
        k = 1;
      }
      if (k != 0) {
        bool = false;
      }
      mTransitionIsSetup = bool;
      View localView = getViewAtRelativeIndex(m);
      if (localView == null) {
        return;
      }
      setupStackSlider(localView, k);
      mSwipeGestureType = i;
      cancelHandleClick();
    }
  }
  
  private void handlePointerUp(MotionEvent paramMotionEvent)
  {
    int i = (int)(paramMotionEvent.getY(paramMotionEvent.findPointerIndex(mActivePointerId)) - mInitialY);
    mLastInteractionTime = System.currentTimeMillis();
    if (mVelocityTracker != null)
    {
      mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
      mYVelocity = ((int)mVelocityTracker.getYVelocity(mActivePointerId));
    }
    if (mVelocityTracker != null)
    {
      mVelocityTracker.recycle();
      mVelocityTracker = null;
    }
    if ((i > mSwipeThreshold) && (mSwipeGestureType == 2) && (mStackSlider.mMode == 0))
    {
      mSwipeGestureType = 0;
      if (mStackMode == 0) {
        showPrevious();
      } else {
        showNext();
      }
      mHighlight.bringToFront();
    }
    else if ((i < -mSwipeThreshold) && (mSwipeGestureType == 1) && (mStackSlider.mMode == 0))
    {
      mSwipeGestureType = 0;
      if (mStackMode == 0) {
        showNext();
      } else {
        showPrevious();
      }
      mHighlight.bringToFront();
    }
    else
    {
      i = mSwipeGestureType;
      float f = 1.0F;
      PropertyValuesHolder localPropertyValuesHolder;
      if (i == 1)
      {
        if (mStackMode != 1) {
          f = 0.0F;
        }
        if ((mStackMode != 0) && (mStackSlider.mMode == 0)) {
          i = Math.round(mStackSlider.getDurationForOffscreenPosition());
        } else {
          i = Math.round(mStackSlider.getDurationForNeutralPosition());
        }
        paramMotionEvent = new StackSlider(mStackSlider);
        localPropertyValuesHolder = PropertyValuesHolder.ofFloat("YProgress", new float[] { f });
        paramMotionEvent = ObjectAnimator.ofPropertyValuesHolder(paramMotionEvent, new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat("XProgress", new float[] { 0.0F }), localPropertyValuesHolder });
        paramMotionEvent.setDuration(i);
        paramMotionEvent.setInterpolator(new LinearInterpolator());
        paramMotionEvent.start();
      }
      else if (mSwipeGestureType == 2)
      {
        if (mStackMode == 1) {
          f = 0.0F;
        }
        if ((mStackMode != 1) && (mStackSlider.mMode == 0)) {
          i = Math.round(mStackSlider.getDurationForOffscreenPosition());
        } else {
          i = Math.round(mStackSlider.getDurationForNeutralPosition());
        }
        paramMotionEvent = new StackSlider(mStackSlider);
        localPropertyValuesHolder = PropertyValuesHolder.ofFloat("YProgress", new float[] { f });
        paramMotionEvent = ObjectAnimator.ofPropertyValuesHolder(paramMotionEvent, new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat("XProgress", new float[] { 0.0F }), localPropertyValuesHolder });
        paramMotionEvent.setDuration(i);
        paramMotionEvent.start();
      }
    }
    mActivePointerId = -1;
    mSwipeGestureType = 0;
  }
  
  private void initStackView()
  {
    configureViewAnimator(5, 1);
    setStaticTransformationsEnabled(true);
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(getContext());
    mTouchSlop = localViewConfiguration.getScaledTouchSlop();
    mMaximumVelocity = localViewConfiguration.getScaledMaximumFlingVelocity();
    mActivePointerId = -1;
    mHighlight = new ImageView(getContext());
    mHighlight.setLayoutParams(new LayoutParams(mHighlight));
    addViewInLayout(mHighlight, -1, new LayoutParams(mHighlight));
    mClickFeedback = new ImageView(getContext());
    mClickFeedback.setLayoutParams(new LayoutParams(mClickFeedback));
    addViewInLayout(mClickFeedback, -1, new LayoutParams(mClickFeedback));
    mClickFeedback.setVisibility(4);
    mStackSlider = new StackSlider();
    if (sHolographicHelper == null) {
      sHolographicHelper = new HolographicHelper(mContext);
    }
    setClipChildren(false);
    setClipToPadding(false);
    mStackMode = 1;
    mWhichChild = -1;
    mFramePadding = ((int)Math.ceil(4.0F * mContext.getResources().getDisplayMetrics().density));
  }
  
  private void measureChildren()
  {
    int i = getChildCount();
    int j = getMeasuredWidth();
    int k = getMeasuredHeight();
    int m = Math.round(j * 0.9F) - mPaddingLeft - mPaddingRight;
    int n = Math.round(k * 0.9F) - mPaddingTop - mPaddingBottom;
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    while (i3 < i)
    {
      View localView = getChildAt(i3);
      localView.measure(View.MeasureSpec.makeMeasureSpec(m, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(n, Integer.MIN_VALUE));
      int i4 = i1;
      int i5 = i2;
      if (localView != mHighlight)
      {
        i4 = i1;
        i5 = i2;
        if (localView != mClickFeedback)
        {
          i5 = localView.getMeasuredWidth();
          int i6 = localView.getMeasuredHeight();
          int i7 = i1;
          if (i5 > i1) {
            i7 = i5;
          }
          i4 = i7;
          i5 = i2;
          if (i6 > i2)
          {
            i5 = i6;
            i4 = i7;
          }
        }
      }
      i3++;
      i1 = i4;
      i2 = i5;
    }
    mNewPerspectiveShiftX = (j * 0.1F);
    mNewPerspectiveShiftY = (0.1F * k);
    if ((i1 > 0) && (i > 0) && (i1 < m)) {
      mNewPerspectiveShiftX = (j - i1);
    }
    if ((i2 > 0) && (i > 0) && (i2 < n)) {
      mNewPerspectiveShiftY = (k - i2);
    }
  }
  
  private void onLayout()
  {
    if (!mFirstLayoutHappened)
    {
      mFirstLayoutHappened = true;
      updateChildTransforms();
    }
    int i = Math.round(0.7F * getMeasuredHeight());
    if (mSlideAmount != i)
    {
      mSlideAmount = i;
      mSwipeThreshold = Math.round(0.2F * i);
    }
    if ((Float.compare(mPerspectiveShiftY, mNewPerspectiveShiftY) != 0) || (Float.compare(mPerspectiveShiftX, mNewPerspectiveShiftX) != 0))
    {
      mPerspectiveShiftY = mNewPerspectiveShiftY;
      mPerspectiveShiftX = mNewPerspectiveShiftX;
      updateChildTransforms();
    }
  }
  
  private void onSecondaryPointerUp(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getActionIndex();
    if (paramMotionEvent.getPointerId(i) == mActivePointerId)
    {
      int j = mSwipeGestureType;
      int k = 0;
      if (j == 2) {
        j = 0;
      } else {
        j = 1;
      }
      View localView = getViewAtRelativeIndex(j);
      if (localView == null) {
        return;
      }
      for (j = k; j < paramMotionEvent.getPointerCount(); j++) {
        if (j != i)
        {
          float f1 = paramMotionEvent.getX(j);
          float f2 = paramMotionEvent.getY(j);
          mTouchRect.set(localView.getLeft(), localView.getTop(), localView.getRight(), localView.getBottom());
          if (mTouchRect.contains(Math.round(f1), Math.round(f2)))
          {
            float f3 = paramMotionEvent.getX(i);
            float f4 = paramMotionEvent.getY(i);
            mInitialY += f2 - f4;
            mInitialX += f1 - f3;
            mActivePointerId = paramMotionEvent.getPointerId(j);
            if (mVelocityTracker != null) {
              mVelocityTracker.clear();
            }
            return;
          }
        }
      }
      handlePointerUp(paramMotionEvent);
    }
  }
  
  private void pacedScroll(boolean paramBoolean)
  {
    if (System.currentTimeMillis() - mLastScrollTime > 100L)
    {
      if (paramBoolean) {
        showPrevious();
      } else {
        showNext();
      }
      mLastScrollTime = System.currentTimeMillis();
    }
  }
  
  private void setupStackSlider(View paramView, int paramInt)
  {
    mStackSlider.setMode(paramInt);
    if (paramView != null)
    {
      mHighlight.setImageBitmap(sHolographicHelper.createResOutline(paramView, mResOutColor));
      mHighlight.setRotation(paramView.getRotation());
      mHighlight.setTranslationY(paramView.getTranslationY());
      mHighlight.setTranslationX(paramView.getTranslationX());
      mHighlight.bringToFront();
      paramView.bringToFront();
      mStackSlider.setView(paramView);
      paramView.setVisibility(0);
    }
  }
  
  private void transformViewAtIndex(int paramInt, View paramView, boolean paramBoolean)
  {
    float f1 = mPerspectiveShiftY;
    float f2 = mPerspectiveShiftX;
    int i;
    if (mStackMode == 1)
    {
      i = mMaxNumActiveViews - paramInt - 1;
      paramInt = i;
      if (i == mMaxNumActiveViews - 1) {
        paramInt = i - 1;
      }
    }
    else
    {
      i = paramInt - 1;
      paramInt = i;
      if (i < 0) {
        paramInt = i + 1;
      }
    }
    float f3 = paramInt * 1.0F / (mMaxNumActiveViews - 2);
    float f4 = 1.0F - 0.0F * (1.0F - f3);
    f1 = f3 * f1 + (f4 - 1.0F) * (getMeasuredHeight() * 0.9F / 2.0F);
    f2 = (1.0F - f3) * f2 + (1.0F - f4) * (getMeasuredWidth() * 0.9F / 2.0F);
    if ((paramView instanceof StackFrame)) {
      ((StackFrame)paramView).cancelTransformAnimator();
    }
    if (paramBoolean)
    {
      PropertyValuesHolder localPropertyValuesHolder = PropertyValuesHolder.ofFloat("translationX", new float[] { f2 });
      Object localObject = PropertyValuesHolder.ofFloat("translationY", new float[] { f1 });
      localObject = ObjectAnimator.ofPropertyValuesHolder(paramView, new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat("scaleX", new float[] { f4 }), PropertyValuesHolder.ofFloat("scaleY", new float[] { f4 }), localObject, localPropertyValuesHolder });
      ((ObjectAnimator)localObject).setDuration(100L);
      if ((paramView instanceof StackFrame)) {
        ((StackFrame)paramView).setTransformAnimator((ObjectAnimator)localObject);
      }
      ((ObjectAnimator)localObject).start();
    }
    else
    {
      paramView.setTranslationX(f2);
      paramView.setTranslationY(f1);
      paramView.setScaleX(f4);
      paramView.setScaleY(f4);
    }
  }
  
  private void updateChildTransforms()
  {
    for (int i = 0; i < getNumActiveViews(); i++)
    {
      View localView = getViewAtRelativeIndex(i);
      if (localView != null) {
        transformViewAtIndex(i, localView, false);
      }
    }
  }
  
  public void advance()
  {
    long l1 = System.currentTimeMillis();
    long l2 = mLastInteractionTime;
    if (mAdapter == null) {
      return;
    }
    if ((getCount() == 1) && (mLoopViews)) {
      return;
    }
    if ((mSwipeGestureType == 0) && (l1 - l2 > 5000L)) {
      showNext();
    }
  }
  
  void applyTransformForChildAtIndex(View paramView, int paramInt) {}
  
  LayoutParams createOrReuseLayoutParams(View paramView)
  {
    ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
    if ((localLayoutParams instanceof LayoutParams))
    {
      paramView = (LayoutParams)localLayoutParams;
      paramView.setHorizontalOffset(0);
      paramView.setVerticalOffset(0);
      width = 0;
      width = 0;
      return paramView;
    }
    return new LayoutParams(paramView);
  }
  
  protected void dispatchDraw(Canvas paramCanvas)
  {
    int i = 0;
    paramCanvas.getClipBounds(stackInvalidateRect);
    int j = getChildCount();
    for (int k = 0; k < j; k++)
    {
      Object localObject = getChildAt(k);
      LayoutParams localLayoutParams = (LayoutParams)((View)localObject).getLayoutParams();
      if (((horizontalOffset == 0) && (verticalOffset == 0)) || (((View)localObject).getAlpha() == 0.0F) || (((View)localObject).getVisibility() != 0)) {
        localLayoutParams.resetInvalidateRect();
      }
      localObject = localLayoutParams.getInvalidateRect();
      if (!((Rect)localObject).isEmpty())
      {
        i = 1;
        stackInvalidateRect.union((Rect)localObject);
      }
    }
    if (i != 0)
    {
      paramCanvas.save();
      paramCanvas.clipRectUnion(stackInvalidateRect);
      super.dispatchDraw(paramCanvas);
      paramCanvas.restore();
    }
    else
    {
      super.dispatchDraw(paramCanvas);
    }
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return StackView.class.getName();
  }
  
  FrameLayout getFrameForChild()
  {
    StackFrame localStackFrame = new StackFrame(mContext);
    localStackFrame.setPadding(mFramePadding, mFramePadding, mFramePadding, mFramePadding);
    return localStackFrame;
  }
  
  void hideTapFeedback(View paramView)
  {
    mClickFeedback.setVisibility(4);
    invalidate();
  }
  
  public boolean onGenericMotionEvent(MotionEvent paramMotionEvent)
  {
    if (((paramMotionEvent.getSource() & 0x2) != 0) && (paramMotionEvent.getAction() == 8))
    {
      float f = paramMotionEvent.getAxisValue(9);
      if (f < 0.0F)
      {
        pacedScroll(false);
        return true;
      }
      if (f > 0.0F)
      {
        pacedScroll(true);
        return true;
      }
    }
    return super.onGenericMotionEvent(paramMotionEvent);
  }
  
  public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfoInternal(paramAccessibilityNodeInfo);
    boolean bool;
    if (getChildCount() > 1) {
      bool = true;
    } else {
      bool = false;
    }
    paramAccessibilityNodeInfo.setScrollable(bool);
    if (isEnabled())
    {
      if (getDisplayedChild() < getChildCount() - 1) {
        paramAccessibilityNodeInfo.addAction(4096);
      }
      if (getDisplayedChild() > 0) {
        paramAccessibilityNodeInfo.addAction(8192);
      }
    }
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction() & 0xFF;
    boolean bool = false;
    if (i != 6) {
      switch (i)
      {
      default: 
        break;
      case 2: 
        i = paramMotionEvent.findPointerIndex(mActivePointerId);
        if (i == -1)
        {
          Log.d("StackView", "Error: No data for our primary pointer.");
          return false;
        }
        beginGestureIfNeeded(paramMotionEvent.getY(i) - mInitialY);
        break;
      case 1: 
      case 3: 
        mActivePointerId = -1;
        mSwipeGestureType = 0;
        break;
      case 0: 
        if (mActivePointerId != -1) {
          break;
        }
        mInitialX = paramMotionEvent.getX();
        mInitialY = paramMotionEvent.getY();
        mActivePointerId = paramMotionEvent.getPointerId(0);
        break;
      }
    } else {
      onSecondaryPointerUp(paramMotionEvent);
    }
    if (mSwipeGestureType != 0) {
      bool = true;
    }
    return bool;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    checkForAndHandleDataChanged();
    paramInt2 = getChildCount();
    for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++)
    {
      View localView = getChildAt(paramInt1);
      int i = mPaddingLeft;
      paramInt4 = localView.getMeasuredWidth();
      paramInt3 = mPaddingTop;
      int j = localView.getMeasuredHeight();
      LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
      localView.layout(mPaddingLeft + horizontalOffset, mPaddingTop + verticalOffset, horizontalOffset + (i + paramInt4), verticalOffset + (paramInt3 + j));
    }
    onLayout();
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getSize(paramInt1);
    int j = View.MeasureSpec.getSize(paramInt2);
    int k = View.MeasureSpec.getMode(paramInt1);
    int m = View.MeasureSpec.getMode(paramInt2);
    paramInt1 = mReferenceChildWidth;
    paramInt2 = 0;
    int n;
    if ((paramInt1 != -1) && (mReferenceChildHeight != -1)) {
      n = 1;
    } else {
      n = 0;
    }
    if (m == 0)
    {
      if (n != 0) {
        paramInt1 = Math.round(mReferenceChildHeight * (1.0F + 1.1111112F)) + mPaddingTop + mPaddingBottom;
      } else {
        paramInt1 = 0;
      }
    }
    else
    {
      paramInt1 = j;
      if (m == Integer.MIN_VALUE) {
        if (n != 0)
        {
          paramInt1 = Math.round(mReferenceChildHeight * (1.0F + 1.1111112F)) + mPaddingTop + mPaddingBottom;
          if (paramInt1 > j) {
            paramInt1 = j | 0x1000000;
          }
        }
        else
        {
          paramInt1 = 0;
        }
      }
    }
    if (k == 0)
    {
      if (n != 0) {
        paramInt2 = Math.round(mReferenceChildWidth * (1.0F + 1.1111112F)) + mPaddingLeft + mPaddingRight;
      }
    }
    else
    {
      paramInt2 = i;
      if (m == Integer.MIN_VALUE) {
        if (n != 0)
        {
          paramInt2 = mReferenceChildWidth + mPaddingLeft + mPaddingRight;
          if (paramInt2 > i) {
            paramInt2 = i | 0x1000000;
          }
        }
        else
        {
          paramInt2 = 0;
        }
      }
    }
    setMeasuredDimension(paramInt2, paramInt1);
    measureChildren();
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    super.onTouchEvent(paramMotionEvent);
    int i = paramMotionEvent.getAction();
    int j = paramMotionEvent.findPointerIndex(mActivePointerId);
    if (j == -1)
    {
      Log.d("StackView", "Error: No data for our primary pointer.");
      return false;
    }
    float f1 = paramMotionEvent.getY(j);
    float f2 = paramMotionEvent.getX(j);
    f1 -= mInitialY;
    float f3 = mInitialX;
    if (mVelocityTracker == null) {
      mVelocityTracker = VelocityTracker.obtain();
    }
    mVelocityTracker.addMovement(paramMotionEvent);
    j = i & 0xFF;
    if (j != 6) {
      switch (j)
      {
      default: 
        break;
      case 3: 
        mActivePointerId = -1;
        mSwipeGestureType = 0;
        break;
      case 2: 
        beginGestureIfNeeded(f1);
        f3 = (f2 - f3) / (mSlideAmount * 1.0F);
        if (mSwipeGestureType == 2)
        {
          f1 = (f1 - mTouchSlop * 1.0F) / mSlideAmount * 1.0F;
          f2 = f1;
          if (mStackMode == 1) {
            f2 = 1.0F - f1;
          }
          mStackSlider.setYProgress(1.0F - f2);
          mStackSlider.setXProgress(f3);
          return true;
        }
        if (mSwipeGestureType != 1) {
          break;
        }
        f1 = -(mTouchSlop * 1.0F + f1) / mSlideAmount * 1.0F;
        f2 = f1;
        if (mStackMode == 1) {
          f2 = 1.0F - f1;
        }
        mStackSlider.setYProgress(f2);
        mStackSlider.setXProgress(f3);
        return true;
      case 1: 
        handlePointerUp(paramMotionEvent);
        break;
      }
    } else {
      onSecondaryPointerUp(paramMotionEvent);
    }
    return true;
  }
  
  public boolean performAccessibilityActionInternal(int paramInt, Bundle paramBundle)
  {
    if (super.performAccessibilityActionInternal(paramInt, paramBundle)) {
      return true;
    }
    if (!isEnabled()) {
      return false;
    }
    if (paramInt != 4096)
    {
      if (paramInt != 8192) {
        return false;
      }
      if (getDisplayedChild() > 0)
      {
        showPrevious();
        return true;
      }
      return false;
    }
    if (getDisplayedChild() < getChildCount() - 1)
    {
      showNext();
      return true;
    }
    return false;
  }
  
  @RemotableViewMethod
  public void showNext()
  {
    if (mSwipeGestureType != 0) {
      return;
    }
    if (!mTransitionIsSetup)
    {
      View localView = getViewAtRelativeIndex(1);
      if (localView != null)
      {
        setupStackSlider(localView, 0);
        mStackSlider.setYProgress(0.0F);
        mStackSlider.setXProgress(0.0F);
      }
    }
    super.showNext();
  }
  
  void showOnly(int paramInt, boolean paramBoolean)
  {
    super.showOnly(paramInt, paramBoolean);
    for (paramInt = mCurrentWindowEnd; paramInt >= mCurrentWindowStart; paramInt--)
    {
      int i = modulo(paramInt, getWindowSize());
      if ((AdapterViewAnimator.ViewAndMetaData)mViewsMap.get(Integer.valueOf(i)) != null)
      {
        View localView = mViewsMap.get(Integer.valueOf(i))).view;
        if (localView != null) {
          localView.bringToFront();
        }
      }
    }
    if (mHighlight != null) {
      mHighlight.bringToFront();
    }
    mTransitionIsSetup = false;
    mClickFeedbackIsValid = false;
  }
  
  @RemotableViewMethod
  public void showPrevious()
  {
    if (mSwipeGestureType != 0) {
      return;
    }
    if (!mTransitionIsSetup)
    {
      View localView = getViewAtRelativeIndex(0);
      if (localView != null)
      {
        setupStackSlider(localView, 0);
        mStackSlider.setYProgress(1.0F);
        mStackSlider.setXProgress(0.0F);
      }
    }
    super.showPrevious();
  }
  
  void showTapFeedback(View paramView)
  {
    updateClickFeedback();
    mClickFeedback.setVisibility(0);
    mClickFeedback.bringToFront();
    invalidate();
  }
  
  void transformViewForTransition(int paramInt1, int paramInt2, final View paramView, boolean paramBoolean)
  {
    Object localObject;
    if (!paramBoolean)
    {
      ((StackFrame)paramView).cancelSliderAnimator();
      paramView.setRotationX(0.0F);
      localObject = (LayoutParams)paramView.getLayoutParams();
      ((LayoutParams)localObject).setVerticalOffset(0);
      ((LayoutParams)localObject).setHorizontalOffset(0);
    }
    if ((paramInt1 == -1) && (paramInt2 == getNumActiveViews() - 1))
    {
      transformViewAtIndex(paramInt2, paramView, false);
      paramView.setVisibility(0);
      paramView.setAlpha(1.0F);
    }
    else
    {
      PropertyValuesHolder localPropertyValuesHolder;
      if ((paramInt1 == 0) && (paramInt2 == 1))
      {
        ((StackFrame)paramView).cancelSliderAnimator();
        paramView.setVisibility(0);
        paramInt1 = Math.round(mStackSlider.getDurationForNeutralPosition(mYVelocity));
        localObject = new StackSlider(mStackSlider);
        ((StackSlider)localObject).setView(paramView);
        if (paramBoolean)
        {
          localPropertyValuesHolder = PropertyValuesHolder.ofFloat("YProgress", new float[] { 0.0F });
          localObject = ObjectAnimator.ofPropertyValuesHolder(localObject, new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat("XProgress", new float[] { 0.0F }), localPropertyValuesHolder });
          ((ObjectAnimator)localObject).setDuration(paramInt1);
          ((ObjectAnimator)localObject).setInterpolator(new LinearInterpolator());
          ((StackFrame)paramView).setSliderAnimator((ObjectAnimator)localObject);
          ((ObjectAnimator)localObject).start();
        }
        else
        {
          ((StackSlider)localObject).setYProgress(0.0F);
          ((StackSlider)localObject).setXProgress(0.0F);
        }
      }
      else if ((paramInt1 == 1) && (paramInt2 == 0))
      {
        ((StackFrame)paramView).cancelSliderAnimator();
        paramInt1 = Math.round(mStackSlider.getDurationForOffscreenPosition(mYVelocity));
        localObject = new StackSlider(mStackSlider);
        ((StackSlider)localObject).setView(paramView);
        if (paramBoolean)
        {
          localPropertyValuesHolder = PropertyValuesHolder.ofFloat("YProgress", new float[] { 1.0F });
          localObject = ObjectAnimator.ofPropertyValuesHolder(localObject, new PropertyValuesHolder[] { PropertyValuesHolder.ofFloat("XProgress", new float[] { 0.0F }), localPropertyValuesHolder });
          ((ObjectAnimator)localObject).setDuration(paramInt1);
          ((ObjectAnimator)localObject).setInterpolator(new LinearInterpolator());
          ((StackFrame)paramView).setSliderAnimator((ObjectAnimator)localObject);
          ((ObjectAnimator)localObject).start();
        }
        else
        {
          ((StackSlider)localObject).setYProgress(1.0F);
          ((StackSlider)localObject).setXProgress(0.0F);
        }
      }
      else if (paramInt2 == 0)
      {
        paramView.setAlpha(0.0F);
        paramView.setVisibility(4);
      }
      else if (((paramInt1 == 0) || (paramInt1 == 1)) && (paramInt2 > 1))
      {
        paramView.setVisibility(0);
        paramView.setAlpha(1.0F);
        paramView.setRotationX(0.0F);
        localObject = (LayoutParams)paramView.getLayoutParams();
        ((LayoutParams)localObject).setVerticalOffset(0);
        ((LayoutParams)localObject).setHorizontalOffset(0);
      }
      else if (paramInt1 == -1)
      {
        paramView.setAlpha(1.0F);
        paramView.setVisibility(0);
      }
      else if (paramInt2 == -1)
      {
        if (paramBoolean) {
          postDelayed(new Runnable()
          {
            public void run()
            {
              paramView.setAlpha(0.0F);
            }
          }, 100L);
        } else {
          paramView.setAlpha(0.0F);
        }
      }
    }
    if (paramInt2 != -1) {
      transformViewAtIndex(paramInt2, paramView, paramBoolean);
    }
  }
  
  void updateClickFeedback()
  {
    if (!mClickFeedbackIsValid)
    {
      View localView = getViewAtRelativeIndex(1);
      if (localView != null)
      {
        mClickFeedback.setImageBitmap(sHolographicHelper.createClickOutline(localView, mClickColor));
        mClickFeedback.setTranslationX(localView.getTranslationX());
        mClickFeedback.setTranslationY(localView.getTranslationY());
      }
      mClickFeedbackIsValid = true;
    }
  }
  
  private static class HolographicHelper
  {
    private static final int CLICK_FEEDBACK = 1;
    private static final int RES_OUT = 0;
    private final Paint mBlurPaint = new Paint();
    private final Canvas mCanvas = new Canvas();
    private float mDensity;
    private final Paint mErasePaint = new Paint();
    private final Paint mHolographicPaint = new Paint();
    private final Matrix mIdentityMatrix = new Matrix();
    private BlurMaskFilter mLargeBlurMaskFilter;
    private final Canvas mMaskCanvas = new Canvas();
    private BlurMaskFilter mSmallBlurMaskFilter;
    private final int[] mTmpXY = new int[2];
    
    HolographicHelper(Context paramContext)
    {
      mDensity = getResourcesgetDisplayMetricsdensity;
      mHolographicPaint.setFilterBitmap(true);
      mHolographicPaint.setMaskFilter(TableMaskFilter.CreateClipTable(0, 30));
      mErasePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
      mErasePaint.setFilterBitmap(true);
      mSmallBlurMaskFilter = new BlurMaskFilter(2.0F * mDensity, BlurMaskFilter.Blur.NORMAL);
      mLargeBlurMaskFilter = new BlurMaskFilter(4.0F * mDensity, BlurMaskFilter.Blur.NORMAL);
    }
    
    Bitmap createClickOutline(View paramView, int paramInt)
    {
      return createOutline(paramView, 1, paramInt);
    }
    
    Bitmap createOutline(View paramView, int paramInt1, int paramInt2)
    {
      mHolographicPaint.setColor(paramInt2);
      if (paramInt1 == 0) {
        mBlurPaint.setMaskFilter(mSmallBlurMaskFilter);
      } else if (paramInt1 == 1) {
        mBlurPaint.setMaskFilter(mLargeBlurMaskFilter);
      }
      if ((paramView.getMeasuredWidth() != 0) && (paramView.getMeasuredHeight() != 0))
      {
        Bitmap localBitmap = Bitmap.createBitmap(paramView.getResources().getDisplayMetrics(), paramView.getMeasuredWidth(), paramView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mCanvas.setBitmap(localBitmap);
        float f1 = paramView.getRotationX();
        float f2 = paramView.getRotation();
        float f3 = paramView.getTranslationY();
        float f4 = paramView.getTranslationX();
        paramView.setRotationX(0.0F);
        paramView.setRotation(0.0F);
        paramView.setTranslationY(0.0F);
        paramView.setTranslationX(0.0F);
        paramView.draw(mCanvas);
        paramView.setRotationX(f1);
        paramView.setRotation(f2);
        paramView.setTranslationY(f3);
        paramView.setTranslationX(f4);
        drawOutline(mCanvas, localBitmap);
        mCanvas.setBitmap(null);
        return localBitmap;
      }
      return null;
    }
    
    Bitmap createResOutline(View paramView, int paramInt)
    {
      return createOutline(paramView, 0, paramInt);
    }
    
    void drawOutline(Canvas paramCanvas, Bitmap paramBitmap)
    {
      int[] arrayOfInt = mTmpXY;
      Bitmap localBitmap = paramBitmap.extractAlpha(mBlurPaint, arrayOfInt);
      mMaskCanvas.setBitmap(localBitmap);
      mMaskCanvas.drawBitmap(paramBitmap, -arrayOfInt[0], -arrayOfInt[1], mErasePaint);
      paramCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
      paramCanvas.setMatrix(mIdentityMatrix);
      paramCanvas.drawBitmap(localBitmap, arrayOfInt[0], arrayOfInt[1], mHolographicPaint);
      mMaskCanvas.setBitmap(null);
      localBitmap.recycle();
    }
  }
  
  class LayoutParams
    extends ViewGroup.LayoutParams
  {
    private final Rect globalInvalidateRect = new Rect();
    int horizontalOffset = 0;
    private final Rect invalidateRect = new Rect();
    private final RectF invalidateRectf = new RectF();
    View mView;
    private final Rect parentRect = new Rect();
    int verticalOffset = 0;
    
    LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      width = 0;
      height = 0;
    }
    
    LayoutParams(View paramView)
    {
      super(0);
      width = 0;
      height = 0;
      mView = paramView;
    }
    
    Rect getInvalidateRect()
    {
      return invalidateRect;
    }
    
    void invalidateGlobalRegion(View paramView, Rect paramRect)
    {
      globalInvalidateRect.set(paramRect);
      globalInvalidateRect.union(0, 0, getWidth(), getHeight());
      paramRect = paramView;
      if ((paramView.getParent() != null) && ((paramView.getParent() instanceof View)))
      {
        int i = 1;
        parentRect.set(0, 0, 0, 0);
        while ((paramRect.getParent() != null) && ((paramRect.getParent() instanceof View)) && (!parentRect.contains(globalInvalidateRect)))
        {
          if (i == 0) {
            globalInvalidateRect.offset(paramRect.getLeft() - paramRect.getScrollX(), paramRect.getTop() - paramRect.getScrollY());
          }
          i = 0;
          paramRect = (View)paramRect.getParent();
          parentRect.set(paramRect.getScrollX(), paramRect.getScrollY(), paramRect.getWidth() + paramRect.getScrollX(), paramRect.getHeight() + paramRect.getScrollY());
          paramRect.invalidate(globalInvalidateRect.left, globalInvalidateRect.top, globalInvalidateRect.right, globalInvalidateRect.bottom);
        }
        paramRect.invalidate(globalInvalidateRect.left, globalInvalidateRect.top, globalInvalidateRect.right, globalInvalidateRect.bottom);
        return;
      }
    }
    
    void resetInvalidateRect()
    {
      invalidateRect.set(0, 0, 0, 0);
    }
    
    public void setHorizontalOffset(int paramInt)
    {
      setOffsets(paramInt, verticalOffset);
    }
    
    public void setOffsets(int paramInt1, int paramInt2)
    {
      int i = paramInt1 - horizontalOffset;
      horizontalOffset = paramInt1;
      int j = paramInt2 - verticalOffset;
      verticalOffset = paramInt2;
      if (mView != null)
      {
        mView.requestLayout();
        paramInt1 = Math.min(mView.getLeft() + i, mView.getLeft());
        i = Math.max(mView.getRight() + i, mView.getRight());
        paramInt2 = Math.min(mView.getTop() + j, mView.getTop());
        j = Math.max(mView.getBottom() + j, mView.getBottom());
        invalidateRectf.set(paramInt1, paramInt2, i, j);
        float f1 = -invalidateRectf.left;
        float f2 = -invalidateRectf.top;
        invalidateRectf.offset(f1, f2);
        mView.getMatrix().mapRect(invalidateRectf);
        invalidateRectf.offset(-f1, -f2);
        invalidateRect.set((int)Math.floor(invalidateRectf.left), (int)Math.floor(invalidateRectf.top), (int)Math.ceil(invalidateRectf.right), (int)Math.ceil(invalidateRectf.bottom));
        invalidateGlobalRegion(mView, invalidateRect);
      }
    }
    
    public void setVerticalOffset(int paramInt)
    {
      setOffsets(horizontalOffset, paramInt);
    }
  }
  
  private static class StackFrame
    extends FrameLayout
  {
    WeakReference<ObjectAnimator> sliderAnimator;
    WeakReference<ObjectAnimator> transformAnimator;
    
    public StackFrame(Context paramContext)
    {
      super();
    }
    
    boolean cancelSliderAnimator()
    {
      if (sliderAnimator != null)
      {
        ObjectAnimator localObjectAnimator = (ObjectAnimator)sliderAnimator.get();
        if (localObjectAnimator != null)
        {
          localObjectAnimator.cancel();
          return true;
        }
      }
      return false;
    }
    
    boolean cancelTransformAnimator()
    {
      if (transformAnimator != null)
      {
        ObjectAnimator localObjectAnimator = (ObjectAnimator)transformAnimator.get();
        if (localObjectAnimator != null)
        {
          localObjectAnimator.cancel();
          return true;
        }
      }
      return false;
    }
    
    void setSliderAnimator(ObjectAnimator paramObjectAnimator)
    {
      sliderAnimator = new WeakReference(paramObjectAnimator);
    }
    
    void setTransformAnimator(ObjectAnimator paramObjectAnimator)
    {
      transformAnimator = new WeakReference(paramObjectAnimator);
    }
  }
  
  private class StackSlider
  {
    static final int BEGINNING_OF_STACK_MODE = 1;
    static final int END_OF_STACK_MODE = 2;
    static final int NORMAL_MODE = 0;
    int mMode = 0;
    View mView;
    float mXProgress;
    float mYProgress;
    
    public StackSlider() {}
    
    public StackSlider(StackSlider paramStackSlider)
    {
      mView = mView;
      mYProgress = mYProgress;
      mXProgress = mXProgress;
      mMode = mMode;
    }
    
    private float cubic(float paramFloat)
    {
      return (float)(Math.pow(2.0F * paramFloat - 1.0F, 3.0D) + 1.0D) / 2.0F;
    }
    
    private float getDuration(boolean paramBoolean, float paramFloat)
    {
      if (mView != null)
      {
        StackView.LayoutParams localLayoutParams = (StackView.LayoutParams)mView.getLayoutParams();
        float f1 = (float)Math.hypot(horizontalOffset, verticalOffset);
        float f2 = (float)Math.hypot(mSlideAmount, 0.4F * mSlideAmount);
        float f3 = f1;
        if (f1 > f2) {
          f3 = f2;
        }
        if (paramFloat == 0.0F)
        {
          if (paramBoolean) {
            paramFloat = 1.0F - f3 / f2;
          } else {
            paramFloat = f3 / f2;
          }
          return paramFloat * 400.0F;
        }
        if (paramBoolean) {
          paramFloat = f3 / Math.abs(paramFloat);
        } else {
          paramFloat = (f2 - f3) / Math.abs(paramFloat);
        }
        if ((paramFloat >= 50.0F) && (paramFloat <= 400.0F)) {
          return paramFloat;
        }
        return getDuration(paramBoolean, 0.0F);
      }
      return 0.0F;
    }
    
    private float highlightAlphaInterpolator(float paramFloat)
    {
      if (paramFloat < 0.4F) {
        return 0.85F * cubic(paramFloat / 0.4F);
      }
      return 0.85F * cubic(1.0F - (paramFloat - 0.4F) / (1.0F - 0.4F));
    }
    
    private float rotationInterpolator(float paramFloat)
    {
      if (paramFloat < 0.2F) {
        return 0.0F;
      }
      return (paramFloat - 0.2F) / (1.0F - 0.2F);
    }
    
    private float viewAlphaInterpolator(float paramFloat)
    {
      if (paramFloat > 0.3F) {
        return (paramFloat - 0.3F) / (1.0F - 0.3F);
      }
      return 0.0F;
    }
    
    float getDurationForNeutralPosition()
    {
      return getDuration(false, 0.0F);
    }
    
    float getDurationForNeutralPosition(float paramFloat)
    {
      return getDuration(false, paramFloat);
    }
    
    float getDurationForOffscreenPosition()
    {
      return getDuration(true, 0.0F);
    }
    
    float getDurationForOffscreenPosition(float paramFloat)
    {
      return getDuration(true, paramFloat);
    }
    
    public float getXProgress()
    {
      return mXProgress;
    }
    
    public float getYProgress()
    {
      return mYProgress;
    }
    
    void setMode(int paramInt)
    {
      mMode = paramInt;
    }
    
    void setView(View paramView)
    {
      mView = paramView;
    }
    
    public void setXProgress(float paramFloat)
    {
      paramFloat = Math.max(-2.0F, Math.min(2.0F, paramFloat));
      mXProgress = paramFloat;
      if (mView == null) {
        return;
      }
      StackView.LayoutParams localLayoutParams1 = (StackView.LayoutParams)mView.getLayoutParams();
      StackView.LayoutParams localLayoutParams2 = (StackView.LayoutParams)mHighlight.getLayoutParams();
      paramFloat *= 0.2F;
      localLayoutParams1.setHorizontalOffset(Math.round(mSlideAmount * paramFloat));
      localLayoutParams2.setHorizontalOffset(Math.round(mSlideAmount * paramFloat));
    }
    
    public void setYProgress(float paramFloat)
    {
      float f = Math.max(0.0F, Math.min(1.0F, paramFloat));
      mYProgress = f;
      if (mView == null) {
        return;
      }
      StackView.LayoutParams localLayoutParams1 = (StackView.LayoutParams)mView.getLayoutParams();
      StackView.LayoutParams localLayoutParams2 = (StackView.LayoutParams)mHighlight.getLayoutParams();
      int i;
      if (mStackMode == 0) {
        i = 1;
      } else {
        i = -1;
      }
      if ((Float.compare(0.0F, mYProgress) != 0) && (Float.compare(1.0F, mYProgress) != 0))
      {
        if (mView.getLayerType() == 0) {
          mView.setLayerType(2, null);
        }
      }
      else if (mView.getLayerType() != 0) {
        mView.setLayerType(0, null);
      }
      switch (mMode)
      {
      default: 
        break;
      case 2: 
        paramFloat = f * 0.2F;
        localLayoutParams1.setVerticalOffset(Math.round(-i * paramFloat * mSlideAmount));
        localLayoutParams2.setVerticalOffset(Math.round(-i * paramFloat * mSlideAmount));
        mHighlight.setAlpha(highlightAlphaInterpolator(paramFloat));
        break;
      case 1: 
        paramFloat = (1.0F - f) * 0.2F;
        localLayoutParams1.setVerticalOffset(Math.round(i * paramFloat * mSlideAmount));
        localLayoutParams2.setVerticalOffset(Math.round(i * paramFloat * mSlideAmount));
        mHighlight.setAlpha(highlightAlphaInterpolator(paramFloat));
        break;
      case 0: 
        localLayoutParams1.setVerticalOffset(Math.round(-f * i * mSlideAmount));
        localLayoutParams2.setVerticalOffset(Math.round(-f * i * mSlideAmount));
        mHighlight.setAlpha(highlightAlphaInterpolator(f));
        paramFloat = viewAlphaInterpolator(1.0F - f);
        if ((mView.getAlpha() == 0.0F) && (paramFloat != 0.0F) && (mView.getVisibility() != 0)) {
          mView.setVisibility(0);
        } else if ((paramFloat == 0.0F) && (mView.getAlpha() != 0.0F) && (mView.getVisibility() == 0)) {
          mView.setVisibility(4);
        }
        mView.setAlpha(paramFloat);
        mView.setRotationX(i * 90.0F * rotationInterpolator(f));
        mHighlight.setRotationX(i * 90.0F * rotationInterpolator(f));
      }
    }
  }
}
