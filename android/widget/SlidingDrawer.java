package android.widget;

import android.R.styleable;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

@Deprecated
public class SlidingDrawer
  extends ViewGroup
{
  private static final int ANIMATION_FRAME_DURATION = 16;
  private static final int COLLAPSED_FULL_CLOSED = -10002;
  private static final int EXPANDED_FULL_OPEN = -10001;
  private static final float MAXIMUM_ACCELERATION = 2000.0F;
  private static final float MAXIMUM_MAJOR_VELOCITY = 200.0F;
  private static final float MAXIMUM_MINOR_VELOCITY = 150.0F;
  private static final float MAXIMUM_TAP_VELOCITY = 100.0F;
  public static final int ORIENTATION_HORIZONTAL = 0;
  public static final int ORIENTATION_VERTICAL = 1;
  private static final int TAP_THRESHOLD = 6;
  private static final int VELOCITY_UNITS = 1000;
  private boolean mAllowSingleTap;
  private boolean mAnimateOnClick;
  private float mAnimatedAcceleration;
  private float mAnimatedVelocity;
  private boolean mAnimating;
  private long mAnimationLastTime;
  private float mAnimationPosition;
  private int mBottomOffset;
  private View mContent;
  private final int mContentId;
  private long mCurrentAnimationTime;
  private boolean mExpanded;
  private final Rect mFrame = new Rect();
  private View mHandle;
  private int mHandleHeight;
  private final int mHandleId;
  private int mHandleWidth;
  private final Rect mInvalidate = new Rect();
  private boolean mLocked;
  private final int mMaximumAcceleration;
  private final int mMaximumMajorVelocity;
  private final int mMaximumMinorVelocity;
  private final int mMaximumTapVelocity;
  private OnDrawerCloseListener mOnDrawerCloseListener;
  private OnDrawerOpenListener mOnDrawerOpenListener;
  private OnDrawerScrollListener mOnDrawerScrollListener;
  private final Runnable mSlidingRunnable = new Runnable()
  {
    public void run()
    {
      SlidingDrawer.this.doAnimation();
    }
  };
  private final int mTapThreshold;
  private int mTopOffset;
  private int mTouchDelta;
  private boolean mTracking;
  private VelocityTracker mVelocityTracker;
  private final int mVelocityUnits;
  private boolean mVertical;
  
  public SlidingDrawer(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public SlidingDrawer(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public SlidingDrawer(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.SlidingDrawer, paramInt1, paramInt2);
    boolean bool;
    if (paramContext.getInt(0, 1) == 1) {
      bool = true;
    } else {
      bool = false;
    }
    mVertical = bool;
    mBottomOffset = ((int)paramContext.getDimension(1, 0.0F));
    mTopOffset = ((int)paramContext.getDimension(2, 0.0F));
    mAllowSingleTap = paramContext.getBoolean(3, true);
    mAnimateOnClick = paramContext.getBoolean(6, true);
    paramInt2 = paramContext.getResourceId(4, 0);
    if (paramInt2 != 0)
    {
      paramInt1 = paramContext.getResourceId(5, 0);
      if (paramInt1 != 0)
      {
        if (paramInt2 != paramInt1)
        {
          mHandleId = paramInt2;
          mContentId = paramInt1;
          float f = getResourcesgetDisplayMetricsdensity;
          mTapThreshold = ((int)(6.0F * f + 0.5F));
          mMaximumTapVelocity = ((int)(100.0F * f + 0.5F));
          mMaximumMinorVelocity = ((int)(150.0F * f + 0.5F));
          mMaximumMajorVelocity = ((int)(200.0F * f + 0.5F));
          mMaximumAcceleration = ((int)(2000.0F * f + 0.5F));
          mVelocityUnits = ((int)(1000.0F * f + 0.5F));
          paramContext.recycle();
          setAlwaysDrawnWithCacheEnabled(false);
          return;
        }
        throw new IllegalArgumentException("The content and handle attributes must refer to different children.");
      }
      throw new IllegalArgumentException("The content attribute is required and must refer to a valid child.");
    }
    throw new IllegalArgumentException("The handle attribute is required and must refer to a valid child.");
  }
  
  private void animateClose(int paramInt, boolean paramBoolean)
  {
    prepareTracking(paramInt);
    performFling(paramInt, mMaximumAcceleration, true, paramBoolean);
  }
  
  private void animateOpen(int paramInt, boolean paramBoolean)
  {
    prepareTracking(paramInt);
    performFling(paramInt, -mMaximumAcceleration, true, paramBoolean);
  }
  
  private void closeDrawer()
  {
    moveHandle(55534);
    mContent.setVisibility(8);
    mContent.destroyDrawingCache();
    if (!mExpanded) {
      return;
    }
    mExpanded = false;
    if (mOnDrawerCloseListener != null) {
      mOnDrawerCloseListener.onDrawerClosed();
    }
  }
  
  private void doAnimation()
  {
    if (mAnimating)
    {
      incrementAnimation();
      float f = mAnimationPosition;
      int i = mBottomOffset;
      int j;
      if (mVertical) {
        j = getHeight();
      } else {
        j = getWidth();
      }
      if (f >= i + j - 1)
      {
        mAnimating = false;
        closeDrawer();
      }
      else if (mAnimationPosition < mTopOffset)
      {
        mAnimating = false;
        openDrawer();
      }
      else
      {
        moveHandle((int)mAnimationPosition);
        mCurrentAnimationTime += 16L;
        postDelayed(mSlidingRunnable, 16L);
      }
    }
  }
  
  private void incrementAnimation()
  {
    long l = SystemClock.uptimeMillis();
    float f1 = (float)(l - mAnimationLastTime) / 1000.0F;
    float f2 = mAnimationPosition;
    float f3 = mAnimatedVelocity;
    float f4 = mAnimatedAcceleration;
    mAnimationPosition = (f3 * f1 + f2 + 0.5F * f4 * f1 * f1);
    mAnimatedVelocity = (f4 * f1 + f3);
    mAnimationLastTime = l;
  }
  
  private void moveHandle(int paramInt)
  {
    View localView = mHandle;
    int i;
    int j;
    Rect localRect1;
    Rect localRect2;
    if (mVertical)
    {
      if (paramInt == 55535)
      {
        localView.offsetTopAndBottom(mTopOffset - localView.getTop());
        invalidate();
      }
      else if (paramInt == 55534)
      {
        localView.offsetTopAndBottom(mBottomOffset + mBottom - mTop - mHandleHeight - localView.getTop());
        invalidate();
      }
      else
      {
        i = localView.getTop();
        j = paramInt - i;
        if (paramInt < mTopOffset)
        {
          paramInt = mTopOffset - i;
        }
        else
        {
          paramInt = j;
          if (j > mBottomOffset + mBottom - mTop - mHandleHeight - i) {
            paramInt = mBottomOffset + mBottom - mTop - mHandleHeight - i;
          }
        }
        localView.offsetTopAndBottom(paramInt);
        localRect1 = mFrame;
        localRect2 = mInvalidate;
        localView.getHitRect(localRect1);
        localRect2.set(localRect1);
        localRect2.union(left, top - paramInt, right, bottom - paramInt);
        localRect2.union(0, bottom - paramInt, getWidth(), bottom - paramInt + mContent.getHeight());
        invalidate(localRect2);
      }
    }
    else if (paramInt == 55535)
    {
      localView.offsetLeftAndRight(mTopOffset - localView.getLeft());
      invalidate();
    }
    else if (paramInt == 55534)
    {
      localView.offsetLeftAndRight(mBottomOffset + mRight - mLeft - mHandleWidth - localView.getLeft());
      invalidate();
    }
    else
    {
      i = localView.getLeft();
      j = paramInt - i;
      if (paramInt < mTopOffset)
      {
        paramInt = mTopOffset - i;
      }
      else
      {
        paramInt = j;
        if (j > mBottomOffset + mRight - mLeft - mHandleWidth - i) {
          paramInt = mBottomOffset + mRight - mLeft - mHandleWidth - i;
        }
      }
      localView.offsetLeftAndRight(paramInt);
      localRect2 = mFrame;
      localRect1 = mInvalidate;
      localView.getHitRect(localRect2);
      localRect1.set(localRect2);
      localRect1.union(left - paramInt, top, right - paramInt, bottom);
      localRect1.union(right - paramInt, 0, right - paramInt + mContent.getWidth(), getHeight());
      invalidate(localRect1);
    }
  }
  
  private void openDrawer()
  {
    moveHandle(55535);
    mContent.setVisibility(0);
    if (mExpanded) {
      return;
    }
    mExpanded = true;
    if (mOnDrawerOpenListener != null) {
      mOnDrawerOpenListener.onDrawerOpened();
    }
  }
  
  private void performFling(int paramInt, float paramFloat, boolean paramBoolean1, boolean paramBoolean2)
  {
    mAnimationPosition = paramInt;
    mAnimatedVelocity = paramFloat;
    int j;
    if (mExpanded)
    {
      if ((!paramBoolean1) && (paramFloat <= mMaximumMajorVelocity))
      {
        int i = mTopOffset;
        if (mVertical) {
          j = mHandleHeight;
        } else {
          j = mHandleWidth;
        }
        if ((paramInt <= i + j) || (paramFloat <= -mMaximumMajorVelocity))
        {
          mAnimatedAcceleration = (-mMaximumAcceleration);
          if (paramFloat <= 0.0F) {
            break label229;
          }
          mAnimatedVelocity = 0.0F;
          break label229;
        }
      }
      mAnimatedAcceleration = mMaximumAcceleration;
      if (paramFloat < 0.0F) {
        mAnimatedVelocity = 0.0F;
      }
    }
    else
    {
      if (!paramBoolean1) {
        if (paramFloat <= mMaximumMajorVelocity)
        {
          if (mVertical) {
            j = getHeight();
          } else {
            j = getWidth();
          }
          if ((paramInt <= j / 2) || (paramFloat <= -mMaximumMajorVelocity)) {}
        }
        else
        {
          mAnimatedAcceleration = mMaximumAcceleration;
          if (paramFloat >= 0.0F) {
            break label229;
          }
          mAnimatedVelocity = 0.0F;
          break label229;
        }
      }
      mAnimatedAcceleration = (-mMaximumAcceleration);
      if (paramFloat > 0.0F) {
        mAnimatedVelocity = 0.0F;
      }
    }
    label229:
    long l = SystemClock.uptimeMillis();
    mAnimationLastTime = l;
    mCurrentAnimationTime = (l + 16L);
    mAnimating = true;
    removeCallbacks(mSlidingRunnable);
    postDelayed(mSlidingRunnable, 16L);
    stopTracking(paramBoolean2);
  }
  
  private void prepareContent()
  {
    if (mAnimating) {
      return;
    }
    View localView = mContent;
    if (localView.isLayoutRequested())
    {
      int j;
      if (mVertical)
      {
        int i = mHandleHeight;
        j = mBottom;
        int k = mTop;
        int m = mTopOffset;
        localView.measure(View.MeasureSpec.makeMeasureSpec(mRight - mLeft, 1073741824), View.MeasureSpec.makeMeasureSpec(j - k - i - m, 1073741824));
        localView.layout(0, mTopOffset + i, localView.getMeasuredWidth(), mTopOffset + i + localView.getMeasuredHeight());
      }
      else
      {
        j = mHandle.getWidth();
        localView.measure(View.MeasureSpec.makeMeasureSpec(mRight - mLeft - j - mTopOffset, 1073741824), View.MeasureSpec.makeMeasureSpec(mBottom - mTop, 1073741824));
        localView.layout(mTopOffset + j, 0, mTopOffset + j + localView.getMeasuredWidth(), localView.getMeasuredHeight());
      }
    }
    localView.getViewTreeObserver().dispatchOnPreDraw();
    if (!localView.isHardwareAccelerated()) {
      localView.buildDrawingCache();
    }
    localView.setVisibility(8);
  }
  
  private void prepareTracking(int paramInt)
  {
    mTracking = true;
    mVelocityTracker = VelocityTracker.obtain();
    if ((mExpanded ^ true))
    {
      mAnimatedAcceleration = mMaximumAcceleration;
      mAnimatedVelocity = mMaximumMajorVelocity;
      int i = mBottomOffset;
      int j;
      if (mVertical) {
        j = getHeight();
      }
      for (paramInt = mHandleHeight;; paramInt = mHandleWidth)
      {
        break;
        j = getWidth();
      }
      mAnimationPosition = (i + (j - paramInt));
      moveHandle((int)mAnimationPosition);
      mAnimating = true;
      removeCallbacks(mSlidingRunnable);
      long l = SystemClock.uptimeMillis();
      mAnimationLastTime = l;
      mCurrentAnimationTime = (16L + l);
      mAnimating = true;
    }
    else
    {
      if (mAnimating)
      {
        mAnimating = false;
        removeCallbacks(mSlidingRunnable);
      }
      moveHandle(paramInt);
    }
  }
  
  private void stopTracking(boolean paramBoolean)
  {
    mHandle.setPressed(false);
    mTracking = false;
    if ((paramBoolean) && (mOnDrawerScrollListener != null)) {
      mOnDrawerScrollListener.onScrollEnded();
    }
    if (mVelocityTracker != null)
    {
      mVelocityTracker.recycle();
      mVelocityTracker = null;
    }
  }
  
  public void animateClose()
  {
    prepareContent();
    OnDrawerScrollListener localOnDrawerScrollListener = mOnDrawerScrollListener;
    if (localOnDrawerScrollListener != null) {
      localOnDrawerScrollListener.onScrollStarted();
    }
    int i;
    if (mVertical) {
      i = mHandle.getTop();
    } else {
      i = mHandle.getLeft();
    }
    animateClose(i, false);
    if (localOnDrawerScrollListener != null) {
      localOnDrawerScrollListener.onScrollEnded();
    }
  }
  
  public void animateOpen()
  {
    prepareContent();
    OnDrawerScrollListener localOnDrawerScrollListener = mOnDrawerScrollListener;
    if (localOnDrawerScrollListener != null) {
      localOnDrawerScrollListener.onScrollStarted();
    }
    int i;
    if (mVertical) {
      i = mHandle.getTop();
    } else {
      i = mHandle.getLeft();
    }
    animateOpen(i, false);
    sendAccessibilityEvent(32);
    if (localOnDrawerScrollListener != null) {
      localOnDrawerScrollListener.onScrollEnded();
    }
  }
  
  public void animateToggle()
  {
    if (!mExpanded) {
      animateOpen();
    } else {
      animateClose();
    }
  }
  
  public void close()
  {
    closeDrawer();
    invalidate();
    requestLayout();
  }
  
  protected void dispatchDraw(Canvas paramCanvas)
  {
    long l = getDrawingTime();
    View localView = mHandle;
    boolean bool = mVertical;
    drawChild(paramCanvas, localView, l);
    if ((!mTracking) && (!mAnimating))
    {
      if (mExpanded) {
        drawChild(paramCanvas, mContent, l);
      }
    }
    else
    {
      Bitmap localBitmap = mContent.getDrawingCache();
      float f1 = 0.0F;
      if (localBitmap != null)
      {
        if (bool) {
          paramCanvas.drawBitmap(localBitmap, 0.0F, localView.getBottom(), null);
        } else {
          paramCanvas.drawBitmap(localBitmap, localView.getRight(), 0.0F, null);
        }
      }
      else
      {
        paramCanvas.save();
        float f2;
        if (bool) {
          f2 = 0.0F;
        } else {
          f2 = localView.getLeft() - mTopOffset;
        }
        if (bool) {
          f1 = localView.getTop() - mTopOffset;
        }
        paramCanvas.translate(f2, f1);
        drawChild(paramCanvas, mContent, l);
        paramCanvas.restore();
      }
    }
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return SlidingDrawer.class.getName();
  }
  
  public View getContent()
  {
    return mContent;
  }
  
  public View getHandle()
  {
    return mHandle;
  }
  
  public boolean isMoving()
  {
    boolean bool;
    if ((!mTracking) && (!mAnimating)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isOpened()
  {
    return mExpanded;
  }
  
  public void lock()
  {
    mLocked = true;
  }
  
  protected void onFinishInflate()
  {
    mHandle = findViewById(mHandleId);
    if (mHandle != null)
    {
      mHandle.setOnClickListener(new DrawerToggler(null));
      mContent = findViewById(mContentId);
      if (mContent != null)
      {
        mContent.setVisibility(8);
        return;
      }
      throw new IllegalArgumentException("The content attribute is must refer to an existing child.");
    }
    throw new IllegalArgumentException("The handle attribute is must refer to an existing child.");
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    if (mLocked) {
      return false;
    }
    int i = paramMotionEvent.getAction();
    float f1 = paramMotionEvent.getX();
    float f2 = paramMotionEvent.getY();
    Rect localRect = mFrame;
    View localView = mHandle;
    localView.getHitRect(localRect);
    if ((!mTracking) && (!localRect.contains((int)f1, (int)f2))) {
      return false;
    }
    if (i == 0)
    {
      mTracking = true;
      localView.setPressed(true);
      prepareContent();
      if (mOnDrawerScrollListener != null) {
        mOnDrawerScrollListener.onScrollStarted();
      }
      if (mVertical)
      {
        i = mHandle.getTop();
        mTouchDelta = ((int)f2 - i);
        prepareTracking(i);
      }
      else
      {
        i = mHandle.getLeft();
        mTouchDelta = ((int)f1 - i);
        prepareTracking(i);
      }
      mVelocityTracker.addMovement(paramMotionEvent);
    }
    return true;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (mTracking) {
      return;
    }
    paramInt1 = paramInt3 - paramInt1;
    paramInt2 = paramInt4 - paramInt2;
    View localView1 = mHandle;
    paramInt4 = localView1.getMeasuredWidth();
    int i = localView1.getMeasuredHeight();
    View localView2 = mContent;
    if (mVertical)
    {
      paramInt3 = (paramInt1 - paramInt4) / 2;
      if (mExpanded) {
        paramInt1 = mTopOffset;
      } else {
        paramInt1 = paramInt2 - i + mBottomOffset;
      }
      localView2.layout(0, mTopOffset + i, localView2.getMeasuredWidth(), mTopOffset + i + localView2.getMeasuredHeight());
      paramInt2 = paramInt1;
    }
    else
    {
      if (mExpanded) {
        paramInt1 = mTopOffset;
      } else {
        paramInt1 = paramInt1 - paramInt4 + mBottomOffset;
      }
      paramInt2 = (paramInt2 - i) / 2;
      localView2.layout(mTopOffset + paramInt4, 0, mTopOffset + paramInt4 + localView2.getMeasuredWidth(), localView2.getMeasuredHeight());
      paramInt3 = paramInt1;
    }
    localView1.layout(paramInt3, paramInt2, paramInt3 + paramInt4, paramInt2 + i);
    mHandleHeight = localView1.getHeight();
    mHandleWidth = localView1.getWidth();
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = View.MeasureSpec.getSize(paramInt1);
    int k = View.MeasureSpec.getMode(paramInt2);
    int m = View.MeasureSpec.getSize(paramInt2);
    if ((i != 0) && (k != 0))
    {
      View localView = mHandle;
      measureChild(localView, paramInt1, paramInt2);
      if (mVertical)
      {
        paramInt2 = localView.getMeasuredHeight();
        paramInt1 = mTopOffset;
        mContent.measure(View.MeasureSpec.makeMeasureSpec(j, 1073741824), View.MeasureSpec.makeMeasureSpec(m - paramInt2 - paramInt1, 1073741824));
      }
      else
      {
        paramInt1 = localView.getMeasuredWidth();
        paramInt2 = mTopOffset;
        mContent.measure(View.MeasureSpec.makeMeasureSpec(j - paramInt1 - paramInt2, 1073741824), View.MeasureSpec.makeMeasureSpec(m, 1073741824));
      }
      setMeasuredDimension(j, m);
      return;
    }
    throw new RuntimeException("SlidingDrawer cannot have UNSPECIFIED dimensions");
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool1 = mLocked;
    boolean bool2 = true;
    if (bool1) {
      return true;
    }
    if (mTracking)
    {
      mVelocityTracker.addMovement(paramMotionEvent);
      float f1;
      switch (paramMotionEvent.getAction())
      {
      default: 
        break;
      case 2: 
        if (mVertical) {
          f1 = paramMotionEvent.getY();
        } else {
          f1 = paramMotionEvent.getX();
        }
        moveHandle((int)f1 - mTouchDelta);
        break;
      case 1: 
      case 3: 
        VelocityTracker localVelocityTracker = mVelocityTracker;
        localVelocityTracker.computeCurrentVelocity(mVelocityUnits);
        float f2 = localVelocityTracker.getYVelocity();
        float f3 = localVelocityTracker.getXVelocity();
        bool1 = mVertical;
        float f4;
        if (bool1)
        {
          if (f2 < 0.0F) {
            i = 1;
          } else {
            i = 0;
          }
          f4 = f3;
          if (f3 < 0.0F) {
            f4 = -f3;
          }
          f5 = f2;
          f1 = f4;
          j = i;
          if (f4 > mMaximumMinorVelocity)
          {
            f1 = mMaximumMinorVelocity;
            f5 = f2;
            j = i;
          }
        }
        else
        {
          if (f3 < 0.0F) {
            i = 1;
          } else {
            i = 0;
          }
          f4 = f2;
          if (f2 < 0.0F) {
            f4 = -f2;
          }
          f5 = f4;
          f1 = f3;
          j = i;
          if (f4 > mMaximumMinorVelocity)
          {
            f5 = mMaximumMinorVelocity;
            j = i;
            f1 = f3;
          }
        }
        float f5 = (float)Math.hypot(f1, f5);
        f1 = f5;
        if (j != 0) {
          f1 = -f5;
        }
        int j = mHandle.getTop();
        int i = mHandle.getLeft();
        if (Math.abs(f1) < mMaximumTapVelocity)
        {
          if (bool1 ? ((mExpanded) && (j < mTapThreshold + mTopOffset)) || ((!mExpanded) && (j > mBottomOffset + mBottom - mTop - mHandleHeight - mTapThreshold)) : ((mExpanded) && (i < mTapThreshold + mTopOffset)) || ((!mExpanded) && (i > mBottomOffset + mRight - mLeft - mHandleWidth - mTapThreshold)))
          {
            if (mAllowSingleTap)
            {
              playSoundEffect(0);
              if (mExpanded)
              {
                if (bool1) {
                  i = j;
                }
                animateClose(i, true);
              }
              else
              {
                if (bool1) {
                  i = j;
                }
                animateOpen(i, true);
              }
            }
            else
            {
              if (bool1) {
                i = j;
              }
              performFling(i, f1, false, true);
            }
          }
          else
          {
            if (bool1) {
              i = j;
            }
            performFling(i, f1, false, true);
          }
        }
        else
        {
          if (!bool1) {
            j = i;
          }
          performFling(j, f1, false, true);
        }
        break;
      }
    }
    bool1 = bool2;
    if (!mTracking)
    {
      bool1 = bool2;
      if (!mAnimating) {
        if (super.onTouchEvent(paramMotionEvent)) {
          bool1 = bool2;
        } else {
          bool1 = false;
        }
      }
    }
    return bool1;
  }
  
  public void open()
  {
    openDrawer();
    invalidate();
    requestLayout();
    sendAccessibilityEvent(32);
  }
  
  public void setOnDrawerCloseListener(OnDrawerCloseListener paramOnDrawerCloseListener)
  {
    mOnDrawerCloseListener = paramOnDrawerCloseListener;
  }
  
  public void setOnDrawerOpenListener(OnDrawerOpenListener paramOnDrawerOpenListener)
  {
    mOnDrawerOpenListener = paramOnDrawerOpenListener;
  }
  
  public void setOnDrawerScrollListener(OnDrawerScrollListener paramOnDrawerScrollListener)
  {
    mOnDrawerScrollListener = paramOnDrawerScrollListener;
  }
  
  public void toggle()
  {
    if (!mExpanded) {
      openDrawer();
    } else {
      closeDrawer();
    }
    invalidate();
    requestLayout();
  }
  
  public void unlock()
  {
    mLocked = false;
  }
  
  private class DrawerToggler
    implements View.OnClickListener
  {
    private DrawerToggler() {}
    
    public void onClick(View paramView)
    {
      if (mLocked) {
        return;
      }
      if (mAnimateOnClick) {
        animateToggle();
      } else {
        toggle();
      }
    }
  }
  
  public static abstract interface OnDrawerCloseListener
  {
    public abstract void onDrawerClosed();
  }
  
  public static abstract interface OnDrawerOpenListener
  {
    public abstract void onDrawerOpened();
  }
  
  public static abstract interface OnDrawerScrollListener
  {
    public abstract void onScrollEnded();
    
    public abstract void onScrollStarted();
  }
}
