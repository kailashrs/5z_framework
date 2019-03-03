package android.widget;

import android.R.styleable;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewHierarchyEncoder;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.animation.AnimationUtils;
import java.util.ArrayList;
import java.util.List;

public class HorizontalScrollView
  extends FrameLayout
{
  private static final int ANIMATED_SCROLL_GAP = 250;
  private static final int INVALID_POINTER = -1;
  private static final float MAX_SCROLL_FACTOR = 0.5F;
  private static final String TAG = "HorizontalScrollView";
  private int mActivePointerId = -1;
  private View mChildToScrollTo = null;
  private EdgeEffect mEdgeGlowLeft;
  private EdgeEffect mEdgeGlowRight;
  @ViewDebug.ExportedProperty(category="layout")
  private boolean mFillViewport;
  private float mHorizontalScrollFactor;
  private boolean mIsBeingDragged = false;
  private boolean mIsLayoutDirty = true;
  private int mLastMotionX;
  private long mLastScroll;
  private int mMaximumVelocity;
  private int mMinimumVelocity;
  private int mOverflingDistance;
  private int mOverscrollDistance;
  private SavedState mSavedState;
  private OverScroller mScroller;
  private boolean mSmoothScrollingEnabled = true;
  private final Rect mTempRect = new Rect();
  private int mTouchSlop;
  private VelocityTracker mVelocityTracker;
  
  public HorizontalScrollView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public HorizontalScrollView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16843603);
  }
  
  public HorizontalScrollView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public HorizontalScrollView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    initScrollView();
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.HorizontalScrollView, paramInt1, paramInt2);
    setFillViewport(paramAttributeSet.getBoolean(0, false));
    paramAttributeSet.recycle();
    if (getResourcesgetConfigurationuiMode == 6) {
      setRevealOnFocusHint(false);
    }
  }
  
  private boolean canScroll()
  {
    boolean bool = false;
    View localView = getChildAt(0);
    if (localView != null)
    {
      int i = localView.getWidth();
      if (getWidth() < mPaddingLeft + i + mPaddingRight) {
        bool = true;
      }
      return bool;
    }
    return false;
  }
  
  private static int clamp(int paramInt1, int paramInt2, int paramInt3)
  {
    if ((paramInt2 < paramInt3) && (paramInt1 >= 0))
    {
      if (paramInt2 + paramInt1 > paramInt3) {
        return paramInt3 - paramInt2;
      }
      return paramInt1;
    }
    return 0;
  }
  
  private void doScrollX(int paramInt)
  {
    if (paramInt != 0) {
      if (mSmoothScrollingEnabled) {
        smoothScrollBy(paramInt, 0);
      } else {
        scrollBy(paramInt, 0);
      }
    }
  }
  
  private View findFocusableViewInBounds(boolean paramBoolean, int paramInt1, int paramInt2)
  {
    ArrayList localArrayList = getFocusables(2);
    int i = localArrayList.size();
    int j = 0;
    Object localObject1 = null;
    int k = 0;
    while (k < i)
    {
      View localView = (View)localArrayList.get(k);
      int m = localView.getLeft();
      int n = localView.getRight();
      Object localObject2 = localObject1;
      int i1 = j;
      if (paramInt1 < n)
      {
        localObject2 = localObject1;
        i1 = j;
        if (m < paramInt2)
        {
          int i2 = 1;
          int i3;
          if ((paramInt1 < m) && (n < paramInt2)) {
            i3 = 1;
          } else {
            i3 = 0;
          }
          if (localObject1 == null)
          {
            localObject2 = localView;
            i1 = i3;
          }
          else
          {
            if (((paramBoolean) && (m < localObject1.getLeft())) || ((paramBoolean) || (n <= localObject1.getRight()))) {
              i2 = 0;
            }
            if (j != 0)
            {
              localObject2 = localObject1;
              i1 = j;
              if (i3 != 0)
              {
                localObject2 = localObject1;
                i1 = j;
                if (i2 != 0)
                {
                  localObject2 = localView;
                  i1 = j;
                }
              }
            }
            else if (i3 != 0)
            {
              localObject2 = localView;
              i1 = 1;
            }
            else
            {
              localObject2 = localObject1;
              i1 = j;
              if (i2 != 0)
              {
                localObject2 = localView;
                i1 = j;
              }
            }
          }
        }
      }
      k++;
      localObject1 = localObject2;
      j = i1;
    }
    return localObject1;
  }
  
  private View findFocusableViewInMyBounds(boolean paramBoolean, int paramInt, View paramView)
  {
    int i = getHorizontalFadingEdgeLength() / 2;
    int j = paramInt + i;
    paramInt = getWidth() + paramInt - i;
    if ((paramView != null) && (paramView.getLeft() < paramInt) && (paramView.getRight() > j)) {
      return paramView;
    }
    return findFocusableViewInBounds(paramBoolean, j, paramInt);
  }
  
  private int getScrollRange()
  {
    int i = 0;
    if (getChildCount() > 0)
    {
      View localView = getChildAt(0);
      i = Math.max(0, localView.getWidth() - (getWidth() - mPaddingLeft - mPaddingRight));
    }
    return i;
  }
  
  private boolean inChild(int paramInt1, int paramInt2)
  {
    int i = getChildCount();
    boolean bool1 = false;
    if (i > 0)
    {
      i = mScrollX;
      View localView = getChildAt(0);
      boolean bool2 = bool1;
      if (paramInt2 >= localView.getTop())
      {
        bool2 = bool1;
        if (paramInt2 < localView.getBottom())
        {
          bool2 = bool1;
          if (paramInt1 >= localView.getLeft() - i)
          {
            bool2 = bool1;
            if (paramInt1 < localView.getRight() - i) {
              bool2 = true;
            }
          }
        }
      }
      return bool2;
    }
    return false;
  }
  
  private void initOrResetVelocityTracker()
  {
    if (mVelocityTracker == null) {
      mVelocityTracker = VelocityTracker.obtain();
    } else {
      mVelocityTracker.clear();
    }
  }
  
  private void initScrollView()
  {
    mScroller = new OverScroller(getContext());
    setFocusable(true);
    setDescendantFocusability(262144);
    setWillNotDraw(false);
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(mContext);
    mTouchSlop = localViewConfiguration.getScaledTouchSlop();
    mMinimumVelocity = localViewConfiguration.getScaledMinimumFlingVelocity();
    mMaximumVelocity = localViewConfiguration.getScaledMaximumFlingVelocity();
    mOverscrollDistance = localViewConfiguration.getScaledOverscrollDistance();
    mOverflingDistance = localViewConfiguration.getScaledOverflingDistance();
    mHorizontalScrollFactor = localViewConfiguration.getScaledHorizontalScrollFactor();
  }
  
  private void initVelocityTrackerIfNotExists()
  {
    if (mVelocityTracker == null) {
      mVelocityTracker = VelocityTracker.obtain();
    }
  }
  
  private boolean isOffScreen(View paramView)
  {
    return isWithinDeltaOfScreen(paramView, 0) ^ true;
  }
  
  private static boolean isViewDescendantOf(View paramView1, View paramView2)
  {
    boolean bool = true;
    if (paramView1 == paramView2) {
      return true;
    }
    paramView1 = paramView1.getParent();
    if ((!(paramView1 instanceof ViewGroup)) || (!isViewDescendantOf((View)paramView1, paramView2))) {
      bool = false;
    }
    return bool;
  }
  
  private boolean isWithinDeltaOfScreen(View paramView, int paramInt)
  {
    paramView.getDrawingRect(mTempRect);
    offsetDescendantRectToMyCoords(paramView, mTempRect);
    boolean bool;
    if ((mTempRect.right + paramInt >= getScrollX()) && (mTempRect.left - paramInt <= getScrollX() + getWidth())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void onSecondaryPointerUp(MotionEvent paramMotionEvent)
  {
    int i = (paramMotionEvent.getAction() & 0xFF00) >> 8;
    if (paramMotionEvent.getPointerId(i) == mActivePointerId)
    {
      if (i == 0) {
        i = 1;
      } else {
        i = 0;
      }
      mLastMotionX = ((int)paramMotionEvent.getX(i));
      mActivePointerId = paramMotionEvent.getPointerId(i);
      if (mVelocityTracker != null) {
        mVelocityTracker.clear();
      }
    }
  }
  
  private void recycleVelocityTracker()
  {
    if (mVelocityTracker != null)
    {
      mVelocityTracker.recycle();
      mVelocityTracker = null;
    }
  }
  
  private boolean scrollAndFocus(int paramInt1, int paramInt2, int paramInt3)
  {
    boolean bool1 = true;
    int i = getWidth();
    int j = getScrollX();
    i = j + i;
    boolean bool2;
    if (paramInt1 == 17) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    View localView = findFocusableViewInBounds(bool2, paramInt2, paramInt3);
    Object localObject = localView;
    if (localView == null) {
      localObject = this;
    }
    if ((paramInt2 >= j) && (paramInt3 <= i))
    {
      bool2 = false;
    }
    else
    {
      if (bool2) {
        paramInt2 -= j;
      } else {
        paramInt2 = paramInt3 - i;
      }
      doScrollX(paramInt2);
      bool2 = bool1;
    }
    if (localObject != findFocus()) {
      ((View)localObject).requestFocus(paramInt1);
    }
    return bool2;
  }
  
  private void scrollToChild(View paramView)
  {
    paramView.getDrawingRect(mTempRect);
    offsetDescendantRectToMyCoords(paramView, mTempRect);
    int i = computeScrollDeltaToGetChildRectOnScreen(mTempRect);
    if (i != 0) {
      scrollBy(i, 0);
    }
  }
  
  private boolean scrollToChildRect(Rect paramRect, boolean paramBoolean)
  {
    int i = computeScrollDeltaToGetChildRectOnScreen(paramRect);
    boolean bool;
    if (i != 0) {
      bool = true;
    } else {
      bool = false;
    }
    if (bool) {
      if (paramBoolean) {
        scrollBy(i, 0);
      } else {
        smoothScrollBy(i, 0);
      }
    }
    return bool;
  }
  
  public void addView(View paramView)
  {
    if (getChildCount() <= 0)
    {
      super.addView(paramView);
      return;
    }
    throw new IllegalStateException("HorizontalScrollView can host only one direct child");
  }
  
  public void addView(View paramView, int paramInt)
  {
    if (getChildCount() <= 0)
    {
      super.addView(paramView, paramInt);
      return;
    }
    throw new IllegalStateException("HorizontalScrollView can host only one direct child");
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams)
  {
    if (getChildCount() <= 0)
    {
      super.addView(paramView, paramInt, paramLayoutParams);
      return;
    }
    throw new IllegalStateException("HorizontalScrollView can host only one direct child");
  }
  
  public void addView(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    if (getChildCount() <= 0)
    {
      super.addView(paramView, paramLayoutParams);
      return;
    }
    throw new IllegalStateException("HorizontalScrollView can host only one direct child");
  }
  
  public boolean arrowScroll(int paramInt)
  {
    View localView1 = findFocus();
    View localView2 = localView1;
    if (localView1 == this) {
      localView2 = null;
    }
    localView1 = FocusFinder.getInstance().findNextFocus(this, localView2, paramInt);
    int i = getMaxScrollAmount();
    if ((localView1 != null) && (isWithinDeltaOfScreen(localView1, i)))
    {
      localView1.getDrawingRect(mTempRect);
      offsetDescendantRectToMyCoords(localView1, mTempRect);
      doScrollX(computeScrollDeltaToGetChildRectOnScreen(mTempRect));
      localView1.requestFocus(paramInt);
    }
    else
    {
      int j = i;
      int k;
      if ((paramInt == 17) && (getScrollX() < j))
      {
        k = getScrollX();
      }
      else
      {
        k = j;
        if (paramInt == 66)
        {
          k = j;
          if (getChildCount() > 0)
          {
            int m = getChildAt(0).getRight();
            int n = getScrollX() + getWidth();
            k = j;
            if (m - n < i) {
              k = m - n;
            }
          }
        }
      }
      if (k == 0) {
        return false;
      }
      if (paramInt == 66) {
        paramInt = k;
      } else {
        paramInt = -k;
      }
      doScrollX(paramInt);
    }
    if ((localView2 != null) && (localView2.isFocused()) && (isOffScreen(localView2)))
    {
      paramInt = getDescendantFocusability();
      setDescendantFocusability(131072);
      requestFocus();
      setDescendantFocusability(paramInt);
    }
    return true;
  }
  
  protected int computeHorizontalScrollOffset()
  {
    return Math.max(0, super.computeHorizontalScrollOffset());
  }
  
  protected int computeHorizontalScrollRange()
  {
    int i = getChildCount();
    int j = getWidth() - mPaddingLeft - mPaddingRight;
    if (i == 0) {
      return j;
    }
    i = getChildAt(0).getRight();
    int k = mScrollX;
    int m = Math.max(0, i - j);
    if (k < 0)
    {
      j = i - k;
    }
    else
    {
      j = i;
      if (k > m) {
        j = i + (k - m);
      }
    }
    return j;
  }
  
  public void computeScroll()
  {
    if (mScroller.computeScrollOffset())
    {
      int i = mScrollX;
      int j = mScrollY;
      int k = mScroller.getCurrX();
      int m = mScroller.getCurrY();
      if ((i != k) || (j != m))
      {
        int n = getScrollRange();
        int i1 = getOverScrollMode();
        int i2 = 1;
        int i3 = i2;
        if (i1 != 0) {
          if ((i1 == 1) && (n > 0)) {
            i3 = i2;
          } else {
            i3 = 0;
          }
        }
        overScrollBy(k - i, m - j, i, j, n, 0, mOverflingDistance, 0, false);
        onScrollChanged(mScrollX, mScrollY, i, j);
        if (i3 != 0) {
          if ((k < 0) && (i >= 0)) {
            mEdgeGlowLeft.onAbsorb((int)mScroller.getCurrVelocity());
          } else if ((k > n) && (i <= n)) {
            mEdgeGlowRight.onAbsorb((int)mScroller.getCurrVelocity());
          }
        }
      }
      if (!awakenScrollBars()) {
        postInvalidateOnAnimation();
      }
    }
  }
  
  protected int computeScrollDeltaToGetChildRectOnScreen(Rect paramRect)
  {
    if (getChildCount() == 0) {
      return 0;
    }
    int i = getWidth();
    int j = getScrollX();
    int k = j + i;
    int m = getHorizontalFadingEdgeLength();
    int n = j;
    if (left > 0) {
      n = j + m;
    }
    j = k;
    if (right < getChildAt(0).getWidth()) {
      j = k - m;
    }
    m = 0;
    if ((right > j) && (left > n))
    {
      if (paramRect.width() > i) {
        k = 0 + (left - n);
      } else {
        k = 0 + (right - j);
      }
      k = Math.min(k, getChildAt(0).getRight() - j);
    }
    else
    {
      k = m;
      if (left < n)
      {
        k = m;
        if (right < j)
        {
          if (paramRect.width() > i) {
            k = 0 - (j - right);
          } else {
            k = 0 - (n - left);
          }
          k = Math.max(k, -getScrollX());
        }
      }
    }
    return k;
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    boolean bool;
    if ((!super.dispatchKeyEvent(paramKeyEvent)) && (!executeKeyEvent(paramKeyEvent))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public void draw(Canvas paramCanvas)
  {
    super.draw(paramCanvas);
    if (mEdgeGlowLeft != null)
    {
      int i = mScrollX;
      int j;
      int k;
      if (!mEdgeGlowLeft.isFinished())
      {
        j = paramCanvas.save();
        k = getHeight() - mPaddingTop - mPaddingBottom;
        paramCanvas.rotate(270.0F);
        paramCanvas.translate(-k + mPaddingTop, Math.min(0, i));
        mEdgeGlowLeft.setSize(k, getWidth());
        if (mEdgeGlowLeft.draw(paramCanvas)) {
          postInvalidateOnAnimation();
        }
        paramCanvas.restoreToCount(j);
      }
      if (!mEdgeGlowRight.isFinished())
      {
        k = paramCanvas.save();
        j = getWidth();
        int m = getHeight();
        int n = mPaddingTop;
        int i1 = mPaddingBottom;
        paramCanvas.rotate(90.0F);
        paramCanvas.translate(-mPaddingTop, -(Math.max(getScrollRange(), i) + j));
        mEdgeGlowRight.setSize(m - n - i1, j);
        if (mEdgeGlowRight.draw(paramCanvas)) {
          postInvalidateOnAnimation();
        }
        paramCanvas.restoreToCount(k);
      }
    }
  }
  
  protected void encodeProperties(ViewHierarchyEncoder paramViewHierarchyEncoder)
  {
    super.encodeProperties(paramViewHierarchyEncoder);
    paramViewHierarchyEncoder.addProperty("layout:fillViewPort", mFillViewport);
  }
  
  public boolean executeKeyEvent(KeyEvent paramKeyEvent)
  {
    mTempRect.setEmpty();
    boolean bool1 = canScroll();
    int i = 66;
    if (!bool1)
    {
      bool1 = isFocused();
      bool2 = false;
      if (bool1)
      {
        View localView = findFocus();
        paramKeyEvent = localView;
        if (localView == this) {
          paramKeyEvent = null;
        }
        paramKeyEvent = FocusFinder.getInstance().findNextFocus(this, paramKeyEvent, 66);
        bool1 = bool2;
        if (paramKeyEvent != null)
        {
          bool1 = bool2;
          if (paramKeyEvent != this)
          {
            bool1 = bool2;
            if (paramKeyEvent.requestFocus(66)) {
              bool1 = true;
            }
          }
        }
        return bool1;
      }
      return false;
    }
    boolean bool2 = false;
    bool1 = bool2;
    if (paramKeyEvent.getAction() == 0)
    {
      int j = paramKeyEvent.getKeyCode();
      if (j != 62)
      {
        switch (j)
        {
        default: 
          bool1 = bool2;
          break;
        case 22: 
          if (!paramKeyEvent.isAltPressed()) {
            bool1 = arrowScroll(66);
          } else {
            bool1 = fullScroll(66);
          }
          break;
        case 21: 
          if (!paramKeyEvent.isAltPressed()) {
            bool1 = arrowScroll(17);
          } else {
            bool1 = fullScroll(17);
          }
          break;
        }
      }
      else
      {
        if (paramKeyEvent.isShiftPressed()) {
          i = 17;
        }
        pageScroll(i);
        bool1 = bool2;
      }
    }
    return bool1;
  }
  
  public void fling(int paramInt)
  {
    if (getChildCount() > 0)
    {
      int i = getWidth() - mPaddingRight - mPaddingLeft;
      boolean bool = false;
      int j = getChildAt(0).getWidth();
      mScroller.fling(mScrollX, mScrollY, paramInt, 0, 0, Math.max(0, j - i), 0, 0, i / 2, 0);
      if (paramInt > 0) {
        bool = true;
      }
      View localView1 = findFocus();
      View localView2 = findFocusableViewInMyBounds(bool, mScroller.getFinalX(), localView1);
      Object localObject = localView2;
      if (localView2 == null) {
        localObject = this;
      }
      if (localObject != localView1)
      {
        if (bool) {
          paramInt = 66;
        } else {
          paramInt = 17;
        }
        ((View)localObject).requestFocus(paramInt);
      }
      postInvalidateOnAnimation();
    }
  }
  
  public boolean fullScroll(int paramInt)
  {
    int i;
    if (paramInt == 66) {
      i = 1;
    } else {
      i = 0;
    }
    int j = getWidth();
    mTempRect.left = 0;
    mTempRect.right = j;
    if ((i != 0) && (getChildCount() > 0))
    {
      View localView = getChildAt(0);
      mTempRect.right = localView.getRight();
      mTempRect.left = (mTempRect.right - j);
    }
    return scrollAndFocus(paramInt, mTempRect.left, mTempRect.right);
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return HorizontalScrollView.class.getName();
  }
  
  protected float getLeftFadingEdgeStrength()
  {
    if (getChildCount() == 0) {
      return 0.0F;
    }
    int i = getHorizontalFadingEdgeLength();
    if (mScrollX < i) {
      return mScrollX / i;
    }
    return 1.0F;
  }
  
  public int getMaxScrollAmount()
  {
    return (int)(0.5F * (mRight - mLeft));
  }
  
  protected float getRightFadingEdgeStrength()
  {
    if (getChildCount() == 0) {
      return 0.0F;
    }
    int i = getHorizontalFadingEdgeLength();
    int j = getWidth();
    int k = mPaddingRight;
    k = getChildAt(0).getRight() - mScrollX - (j - k);
    if (k < i) {
      return k / i;
    }
    return 1.0F;
  }
  
  public boolean isFillViewport()
  {
    return mFillViewport;
  }
  
  public boolean isSmoothScrollingEnabled()
  {
    return mSmoothScrollingEnabled;
  }
  
  protected void measureChild(View paramView, int paramInt1, int paramInt2)
  {
    ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
    int i = mPaddingLeft;
    int j = mPaddingRight;
    paramView.measure(View.MeasureSpec.makeSafeMeasureSpec(Math.max(0, View.MeasureSpec.getSize(paramInt1) - (i + j)), 0), getChildMeasureSpec(paramInt2, mPaddingTop + mPaddingBottom, height));
  }
  
  protected void measureChildWithMargins(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    int i = getChildMeasureSpec(paramInt3, mPaddingTop + mPaddingBottom + topMargin + bottomMargin + paramInt4, height);
    int j = mPaddingLeft;
    int k = mPaddingRight;
    paramInt3 = leftMargin;
    paramInt4 = rightMargin;
    paramView.measure(View.MeasureSpec.makeSafeMeasureSpec(Math.max(0, View.MeasureSpec.getSize(paramInt1) - (j + k + paramInt3 + paramInt4 + paramInt2)), 0), i);
  }
  
  public boolean onGenericMotionEvent(MotionEvent paramMotionEvent)
  {
    if ((paramMotionEvent.getAction() == 8) && (!mIsBeingDragged))
    {
      float f;
      if (paramMotionEvent.isFromSource(2)) {
        if ((paramMotionEvent.getMetaState() & 0x1) != 0) {
          f = -paramMotionEvent.getAxisValue(9);
        }
      }
      for (;;)
      {
        break;
        f = paramMotionEvent.getAxisValue(10);
        continue;
        if (paramMotionEvent.isFromSource(4194304)) {
          f = paramMotionEvent.getAxisValue(26);
        } else {
          f = 0.0F;
        }
      }
      int i = Math.round(mHorizontalScrollFactor * f);
      if (i != 0)
      {
        int j = getScrollRange();
        int k = mScrollX;
        int m = k + i;
        if (m < 0)
        {
          i = 0;
        }
        else
        {
          i = m;
          if (m > j) {
            i = j;
          }
        }
        if (i != k)
        {
          super.scrollTo(i, mScrollY);
          return true;
        }
      }
    }
    return super.onGenericMotionEvent(paramMotionEvent);
  }
  
  public void onInitializeAccessibilityEventInternal(AccessibilityEvent paramAccessibilityEvent)
  {
    super.onInitializeAccessibilityEventInternal(paramAccessibilityEvent);
    boolean bool;
    if (getScrollRange() > 0) {
      bool = true;
    } else {
      bool = false;
    }
    paramAccessibilityEvent.setScrollable(bool);
    paramAccessibilityEvent.setScrollX(mScrollX);
    paramAccessibilityEvent.setScrollY(mScrollY);
    paramAccessibilityEvent.setMaxScrollX(getScrollRange());
    paramAccessibilityEvent.setMaxScrollY(mScrollY);
  }
  
  public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfoInternal(paramAccessibilityNodeInfo);
    int i = getScrollRange();
    if (i > 0)
    {
      paramAccessibilityNodeInfo.setScrollable(true);
      if ((isEnabled()) && (mScrollX > 0))
      {
        paramAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_BACKWARD);
        paramAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_LEFT);
      }
      if ((isEnabled()) && (mScrollX < i))
      {
        paramAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD);
        paramAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_RIGHT);
      }
    }
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction();
    if ((i == 2) && (mIsBeingDragged)) {
      return true;
    }
    if (super.onInterceptTouchEvent(paramMotionEvent)) {
      return true;
    }
    switch (i & 0xFF)
    {
    case 4: 
    default: 
      break;
    case 6: 
      onSecondaryPointerUp(paramMotionEvent);
      mLastMotionX = ((int)paramMotionEvent.getX(paramMotionEvent.findPointerIndex(mActivePointerId)));
      break;
    case 5: 
      i = paramMotionEvent.getActionIndex();
      mLastMotionX = ((int)paramMotionEvent.getX(i));
      mActivePointerId = paramMotionEvent.getPointerId(i);
      break;
    case 2: 
      i = mActivePointerId;
      if (i != -1)
      {
        int j = paramMotionEvent.findPointerIndex(i);
        if (j == -1)
        {
          paramMotionEvent = new StringBuilder();
          paramMotionEvent.append("Invalid pointerId=");
          paramMotionEvent.append(i);
          paramMotionEvent.append(" in onInterceptTouchEvent");
          Log.e("HorizontalScrollView", paramMotionEvent.toString());
        }
        else
        {
          i = (int)paramMotionEvent.getX(j);
          if (Math.abs(i - mLastMotionX) > mTouchSlop)
          {
            mIsBeingDragged = true;
            mLastMotionX = i;
            initVelocityTrackerIfNotExists();
            mVelocityTracker.addMovement(paramMotionEvent);
            if (mParent != null) {
              mParent.requestDisallowInterceptTouchEvent(true);
            }
          }
        }
      }
      break;
    case 1: 
    case 3: 
      mIsBeingDragged = false;
      mActivePointerId = -1;
      if (mScroller.springBack(mScrollX, mScrollY, 0, getScrollRange(), 0, 0)) {
        postInvalidateOnAnimation();
      }
      break;
    case 0: 
      i = (int)paramMotionEvent.getX();
      if (!inChild(i, (int)paramMotionEvent.getY()))
      {
        mIsBeingDragged = false;
        recycleVelocityTracker();
      }
      else
      {
        mLastMotionX = i;
        mActivePointerId = paramMotionEvent.getPointerId(0);
        initOrResetVelocityTracker();
        mVelocityTracker.addMovement(paramMotionEvent);
        mIsBeingDragged = (true ^ mScroller.isFinished());
      }
      break;
    }
    return mIsBeingDragged;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = 0;
    int j = 0;
    if (getChildCount() > 0)
    {
      i = getChildAt(0).getMeasuredWidth();
      FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams)getChildAt(0).getLayoutParams();
      j = leftMargin + rightMargin;
    }
    if (i > paramInt3 - paramInt1 - getPaddingLeftWithForeground() - getPaddingRightWithForeground() - j) {
      paramBoolean = true;
    } else {
      paramBoolean = false;
    }
    layoutChildren(paramInt1, paramInt2, paramInt3, paramInt4, paramBoolean);
    mIsLayoutDirty = false;
    if ((mChildToScrollTo != null) && (isViewDescendantOf(mChildToScrollTo, this))) {
      scrollToChild(mChildToScrollTo);
    }
    mChildToScrollTo = null;
    if (!isLaidOut())
    {
      paramInt2 = Math.max(0, i - (paramInt3 - paramInt1 - mPaddingLeft - mPaddingRight));
      if (mSavedState != null)
      {
        if (isLayoutRtl()) {
          paramInt1 = paramInt2 - mSavedState.scrollOffsetFromStart;
        } else {
          paramInt1 = mSavedState.scrollOffsetFromStart;
        }
        mScrollX = paramInt1;
        mSavedState = null;
      }
      else if (isLayoutRtl())
      {
        mScrollX = (paramInt2 - mScrollX);
      }
      if (mScrollX > paramInt2) {
        mScrollX = paramInt2;
      } else if (mScrollX < 0) {
        mScrollX = 0;
      }
    }
    scrollTo(mScrollX, mScrollY);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    if (!mFillViewport) {
      return;
    }
    if (View.MeasureSpec.getMode(paramInt1) == 0) {
      return;
    }
    if (getChildCount() > 0)
    {
      View localView = getChildAt(0);
      FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams)localView.getLayoutParams();
      if (getContextgetApplicationInfotargetSdkVersion >= 23)
      {
        i = mPaddingLeft + mPaddingRight + leftMargin + rightMargin;
        paramInt1 = mPaddingTop + mPaddingBottom + topMargin + bottomMargin;
      }
      else
      {
        i = mPaddingLeft + mPaddingRight;
        paramInt1 = mPaddingTop + mPaddingBottom;
      }
      int i = getMeasuredWidth() - i;
      if (localView.getMeasuredWidth() < i) {
        localView.measure(View.MeasureSpec.makeMeasureSpec(i, 1073741824), getChildMeasureSpec(paramInt2, paramInt1, height));
      }
    }
  }
  
  protected void onOverScrolled(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (!mScroller.isFinished())
    {
      int i = mScrollX;
      int j = mScrollY;
      mScrollX = paramInt1;
      mScrollY = paramInt2;
      invalidateParentIfNeeded();
      onScrollChanged(mScrollX, mScrollY, i, j);
      if (paramBoolean1) {
        mScroller.springBack(mScrollX, mScrollY, 0, getScrollRange(), 0, 0);
      }
    }
    else
    {
      super.scrollTo(paramInt1, paramInt2);
    }
    awakenScrollBars();
  }
  
  protected boolean onRequestFocusInDescendants(int paramInt, Rect paramRect)
  {
    int i;
    if (paramInt == 2)
    {
      i = 66;
    }
    else
    {
      i = paramInt;
      if (paramInt == 1) {
        i = 17;
      }
    }
    View localView;
    if (paramRect == null) {
      localView = FocusFinder.getInstance().findNextFocus(this, null, i);
    } else {
      localView = FocusFinder.getInstance().findNextFocusFromRect(this, paramRect, i);
    }
    if (localView == null) {
      return false;
    }
    if (isOffScreen(localView)) {
      return false;
    }
    return localView.requestFocus(i, paramRect);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if (mContext.getApplicationInfo().targetSdkVersion <= 18)
    {
      super.onRestoreInstanceState(paramParcelable);
      return;
    }
    paramParcelable = (SavedState)paramParcelable;
    super.onRestoreInstanceState(paramParcelable.getSuperState());
    mSavedState = paramParcelable;
    requestLayout();
  }
  
  protected Parcelable onSaveInstanceState()
  {
    if (mContext.getApplicationInfo().targetSdkVersion <= 18) {
      return super.onSaveInstanceState();
    }
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    int i;
    if (isLayoutRtl()) {
      i = -mScrollX;
    } else {
      i = mScrollX;
    }
    scrollOffsetFromStart = i;
    return localSavedState;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    View localView = findFocus();
    if ((localView != null) && (this != localView))
    {
      if (isWithinDeltaOfScreen(localView, mRight - mLeft))
      {
        localView.getDrawingRect(mTempRect);
        offsetDescendantRectToMyCoords(localView, mTempRect);
        doScrollX(computeScrollDeltaToGetChildRectOnScreen(mTempRect));
      }
      return;
    }
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    initVelocityTrackerIfNotExists();
    mVelocityTracker.addMovement(paramMotionEvent);
    int i = paramMotionEvent.getAction() & 0xFF;
    if (i != 6)
    {
      int j = 0;
      ViewParent localViewParent;
      switch (i)
      {
      default: 
      case 3: 
      case 2: 
        int k;
        for (;;)
        {
          break label758;
          if ((mIsBeingDragged) && (getChildCount() > 0))
          {
            if (mScroller.springBack(mScrollX, mScrollY, 0, getScrollRange(), 0, 0)) {
              postInvalidateOnAnimation();
            }
            mActivePointerId = -1;
            mIsBeingDragged = false;
            recycleVelocityTracker();
            if (mEdgeGlowLeft != null)
            {
              mEdgeGlowLeft.onRelease();
              mEdgeGlowRight.onRelease();
              continue;
              k = paramMotionEvent.findPointerIndex(mActivePointerId);
              if (k != -1) {
                break;
              }
              paramMotionEvent = new StringBuilder();
              paramMotionEvent.append("Invalid pointerId=");
              paramMotionEvent.append(mActivePointerId);
              paramMotionEvent.append(" in onTouchEvent");
              Log.e("HorizontalScrollView", paramMotionEvent.toString());
            }
          }
        }
        int m = (int)paramMotionEvent.getX(k);
        int n = mLastMotionX - m;
        i = n;
        if (!mIsBeingDragged)
        {
          i = n;
          if (Math.abs(n) > mTouchSlop)
          {
            localViewParent = getParent();
            if (localViewParent != null) {
              localViewParent.requestDisallowInterceptTouchEvent(true);
            }
            mIsBeingDragged = true;
            if (n > 0) {
              i = n - mTouchSlop;
            } else {
              i = n + mTouchSlop;
            }
          }
        }
        if (mIsBeingDragged)
        {
          mLastMotionX = m;
          n = mScrollX;
          m = mScrollY;
          m = getScrollRange();
          int i1 = getOverScrollMode();
          if ((i1 != 0) && ((i1 != 1) || (m <= 0))) {
            break label358;
          }
          j = 1;
          if (overScrollBy(i, 0, mScrollX, 0, m, 0, mOverscrollDistance, 0, true)) {
            mVelocityTracker.clear();
          }
          if (j != 0)
          {
            j = n + i;
            if (j < 0)
            {
              mEdgeGlowLeft.onPull(i / getWidth(), 1.0F - paramMotionEvent.getY(k) / getHeight());
              if (!mEdgeGlowRight.isFinished()) {
                mEdgeGlowRight.onRelease();
              }
            }
            else if (j > m)
            {
              mEdgeGlowRight.onPull(i / getWidth(), paramMotionEvent.getY(k) / getHeight());
              if (!mEdgeGlowLeft.isFinished()) {
                mEdgeGlowLeft.onRelease();
              }
            }
            if ((mEdgeGlowLeft != null) && ((!mEdgeGlowLeft.isFinished()) || (!mEdgeGlowRight.isFinished()))) {
              postInvalidateOnAnimation();
            }
          }
          break;
        }
        break;
      case 1: 
        if (!mIsBeingDragged) {
          break;
        }
        paramMotionEvent = mVelocityTracker;
        paramMotionEvent.computeCurrentVelocity(1000, mMaximumVelocity);
        i = (int)paramMotionEvent.getXVelocity(mActivePointerId);
        if (getChildCount() > 0) {
          if (Math.abs(i) > mMinimumVelocity) {
            fling(-i);
          } else if (mScroller.springBack(mScrollX, mScrollY, 0, getScrollRange(), 0, 0)) {
            postInvalidateOnAnimation();
          }
        }
        mActivePointerId = -1;
        mIsBeingDragged = false;
        recycleVelocityTracker();
        if (mEdgeGlowLeft != null)
        {
          mEdgeGlowLeft.onRelease();
          mEdgeGlowRight.onRelease();
        }
        break;
      case 0: 
        label358:
        if (getChildCount() == 0) {
          return false;
        }
        boolean bool = mScroller.isFinished() ^ true;
        mIsBeingDragged = bool;
        if (bool)
        {
          localViewParent = getParent();
          if (localViewParent != null) {
            localViewParent.requestDisallowInterceptTouchEvent(true);
          }
        }
        if (!mScroller.isFinished()) {
          mScroller.abortAnimation();
        }
        mLastMotionX = ((int)paramMotionEvent.getX());
        mActivePointerId = paramMotionEvent.getPointerId(0);
        break;
      }
    }
    else
    {
      onSecondaryPointerUp(paramMotionEvent);
    }
    label758:
    return true;
  }
  
  public boolean pageScroll(int paramInt)
  {
    int i;
    if (paramInt == 66) {
      i = 1;
    } else {
      i = 0;
    }
    int j = getWidth();
    if (i != 0)
    {
      mTempRect.left = (getScrollX() + j);
      if (getChildCount() > 0)
      {
        View localView = getChildAt(0);
        if (mTempRect.left + j > localView.getRight()) {
          mTempRect.left = (localView.getRight() - j);
        }
      }
    }
    else
    {
      mTempRect.left = (getScrollX() - j);
      if (mTempRect.left < 0) {
        mTempRect.left = 0;
      }
    }
    mTempRect.right = (mTempRect.left + j);
    return scrollAndFocus(paramInt, mTempRect.left, mTempRect.right);
  }
  
  public boolean performAccessibilityActionInternal(int paramInt, Bundle paramBundle)
  {
    if (super.performAccessibilityActionInternal(paramInt, paramBundle)) {
      return true;
    }
    if (paramInt != 4096) {
      if ((paramInt != 8192) && (paramInt != 16908345))
      {
        if (paramInt != 16908347) {
          return false;
        }
      }
      else
      {
        if (!isEnabled()) {
          return false;
        }
        i = getWidth();
        paramInt = mPaddingLeft;
        j = mPaddingRight;
        paramInt = Math.max(0, mScrollX - (i - paramInt - j));
        if (paramInt != mScrollX)
        {
          smoothScrollTo(paramInt, 0);
          return true;
        }
        return false;
      }
    }
    if (!isEnabled()) {
      return false;
    }
    int i = getWidth();
    int j = mPaddingLeft;
    paramInt = mPaddingRight;
    paramInt = Math.min(mScrollX + (i - j - paramInt), getScrollRange());
    if (paramInt != mScrollX)
    {
      smoothScrollTo(paramInt, 0);
      return true;
    }
    return false;
  }
  
  public void requestChildFocus(View paramView1, View paramView2)
  {
    if ((paramView2 != null) && (paramView2.getRevealOnFocusHint())) {
      if (!mIsLayoutDirty) {
        scrollToChild(paramView2);
      } else {
        mChildToScrollTo = paramView2;
      }
    }
    super.requestChildFocus(paramView1, paramView2);
  }
  
  public boolean requestChildRectangleOnScreen(View paramView, Rect paramRect, boolean paramBoolean)
  {
    paramRect.offset(paramView.getLeft() - paramView.getScrollX(), paramView.getTop() - paramView.getScrollY());
    return scrollToChildRect(paramRect, paramBoolean);
  }
  
  public void requestDisallowInterceptTouchEvent(boolean paramBoolean)
  {
    if (paramBoolean) {
      recycleVelocityTracker();
    }
    super.requestDisallowInterceptTouchEvent(paramBoolean);
  }
  
  public void requestLayout()
  {
    mIsLayoutDirty = true;
    super.requestLayout();
  }
  
  public void scrollTo(int paramInt1, int paramInt2)
  {
    if (getChildCount() > 0)
    {
      View localView = getChildAt(0);
      paramInt1 = clamp(paramInt1, getWidth() - mPaddingRight - mPaddingLeft, localView.getWidth());
      paramInt2 = clamp(paramInt2, getHeight() - mPaddingBottom - mPaddingTop, localView.getHeight());
      if ((paramInt1 != mScrollX) || (paramInt2 != mScrollY)) {
        super.scrollTo(paramInt1, paramInt2);
      }
    }
  }
  
  public void setFillViewport(boolean paramBoolean)
  {
    if (paramBoolean != mFillViewport)
    {
      mFillViewport = paramBoolean;
      requestLayout();
    }
  }
  
  public void setOverScrollMode(int paramInt)
  {
    if (paramInt != 2)
    {
      if (mEdgeGlowLeft == null)
      {
        Context localContext = getContext();
        mEdgeGlowLeft = new EdgeEffect(localContext);
        mEdgeGlowRight = new EdgeEffect(localContext);
      }
    }
    else
    {
      mEdgeGlowLeft = null;
      mEdgeGlowRight = null;
    }
    super.setOverScrollMode(paramInt);
  }
  
  public void setSmoothScrollingEnabled(boolean paramBoolean)
  {
    mSmoothScrollingEnabled = paramBoolean;
  }
  
  public boolean shouldDelayChildPressedState()
  {
    return true;
  }
  
  public final void smoothScrollBy(int paramInt1, int paramInt2)
  {
    if (getChildCount() == 0) {
      return;
    }
    if (AnimationUtils.currentAnimationTimeMillis() - mLastScroll > 250L)
    {
      int i = getWidth();
      paramInt2 = mPaddingRight;
      int j = mPaddingLeft;
      j = Math.max(0, getChildAt(0).getWidth() - (i - paramInt2 - j));
      paramInt2 = mScrollX;
      paramInt1 = Math.max(0, Math.min(paramInt2 + paramInt1, j));
      mScroller.startScroll(paramInt2, mScrollY, paramInt1 - paramInt2, 0);
      postInvalidateOnAnimation();
    }
    else
    {
      if (!mScroller.isFinished()) {
        mScroller.abortAnimation();
      }
      scrollBy(paramInt1, paramInt2);
    }
    mLastScroll = AnimationUtils.currentAnimationTimeMillis();
  }
  
  public final void smoothScrollTo(int paramInt1, int paramInt2)
  {
    smoothScrollBy(paramInt1 - mScrollX, paramInt2 - mScrollY);
  }
  
  static class SavedState
    extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public HorizontalScrollView.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new HorizontalScrollView.SavedState(paramAnonymousParcel);
      }
      
      public HorizontalScrollView.SavedState[] newArray(int paramAnonymousInt)
      {
        return new HorizontalScrollView.SavedState[paramAnonymousInt];
      }
    };
    public int scrollOffsetFromStart;
    
    public SavedState(Parcel paramParcel)
    {
      super();
      scrollOffsetFromStart = paramParcel.readInt();
    }
    
    SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("HorizontalScrollView.SavedState{");
      localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      localStringBuilder.append(" scrollPosition=");
      localStringBuilder.append(scrollOffsetFromStart);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(scrollOffsetFromStart);
    }
  }
}
