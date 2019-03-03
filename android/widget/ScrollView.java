package android.widget;

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
import android.os.StrictMode;
import android.os.StrictMode.Span;
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
import com.android.internal.R.styleable;
import java.util.ArrayList;
import java.util.List;

public class ScrollView
  extends FrameLayout
{
  static final int ANIMATED_SCROLL_GAP = 250;
  private static final int INVALID_POINTER = -1;
  static final float MAX_SCROLL_FACTOR = 0.5F;
  private static final String TAG = "ScrollView";
  private int mActivePointerId = -1;
  private View mChildToScrollTo = null;
  private EdgeEffect mEdgeGlowBottom;
  private EdgeEffect mEdgeGlowTop;
  @ViewDebug.ExportedProperty(category="layout")
  private boolean mFillViewport;
  private StrictMode.Span mFlingStrictSpan = null;
  private boolean mIsBeingDragged = false;
  private boolean mIsLayoutDirty = true;
  private int mLastMotionY;
  private long mLastScroll;
  private int mMaximumVelocity;
  private int mMinimumVelocity;
  private int mNestedYOffset;
  private int mOverflingDistance;
  private int mOverscrollDistance;
  private SavedState mSavedState;
  private final int[] mScrollConsumed = new int[2];
  private final int[] mScrollOffset = new int[2];
  private StrictMode.Span mScrollStrictSpan = null;
  private OverScroller mScroller;
  private boolean mSmoothScrollingEnabled = true;
  private final Rect mTempRect = new Rect();
  private int mTouchSlop;
  private VelocityTracker mVelocityTracker;
  private float mVerticalScrollFactor;
  
  public ScrollView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public ScrollView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842880);
  }
  
  public ScrollView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public ScrollView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    initScrollView();
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ScrollView, paramInt1, paramInt2);
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
      int i = localView.getHeight();
      if (getHeight() < mPaddingTop + i + mPaddingBottom) {
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
  
  private void doScrollY(int paramInt)
  {
    if (paramInt != 0) {
      if (mSmoothScrollingEnabled) {
        smoothScrollBy(0, paramInt);
      } else {
        scrollBy(0, paramInt);
      }
    }
  }
  
  private void endDrag()
  {
    mIsBeingDragged = false;
    recycleVelocityTracker();
    if (mEdgeGlowTop != null)
    {
      mEdgeGlowTop.onRelease();
      mEdgeGlowBottom.onRelease();
    }
    if (mScrollStrictSpan != null)
    {
      mScrollStrictSpan.finish();
      mScrollStrictSpan = null;
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
      int m = localView.getTop();
      int n = localView.getBottom();
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
            if (((paramBoolean) && (m < localObject1.getTop())) || ((paramBoolean) || (n <= localObject1.getBottom()))) {
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
  
  private void flingWithNestedDispatch(int paramInt)
  {
    boolean bool;
    if (((mScrollY <= 0) && (paramInt <= 0)) || ((mScrollY >= getScrollRange()) && (paramInt >= 0))) {
      bool = false;
    } else {
      bool = true;
    }
    if (!dispatchNestedPreFling(0.0F, paramInt))
    {
      dispatchNestedFling(0.0F, paramInt, bool);
      if (bool) {
        fling(paramInt);
      }
    }
  }
  
  private int getScrollRange()
  {
    int i = 0;
    if (getChildCount() > 0)
    {
      View localView = getChildAt(0);
      i = Math.max(0, localView.getHeight() - (getHeight() - mPaddingBottom - mPaddingTop));
    }
    return i;
  }
  
  private boolean inChild(int paramInt1, int paramInt2)
  {
    int i = getChildCount();
    boolean bool1 = false;
    if (i > 0)
    {
      i = mScrollY;
      View localView = getChildAt(0);
      boolean bool2 = bool1;
      if (paramInt2 >= localView.getTop() - i)
      {
        bool2 = bool1;
        if (paramInt2 < localView.getBottom() - i)
        {
          bool2 = bool1;
          if (paramInt1 >= localView.getLeft())
          {
            bool2 = bool1;
            if (paramInt1 < localView.getRight()) {
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
    mVerticalScrollFactor = localViewConfiguration.getScaledVerticalScrollFactor();
  }
  
  private void initVelocityTrackerIfNotExists()
  {
    if (mVelocityTracker == null) {
      mVelocityTracker = VelocityTracker.obtain();
    }
  }
  
  private boolean isOffScreen(View paramView)
  {
    return isWithinDeltaOfScreen(paramView, 0, getHeight()) ^ true;
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
  
  private boolean isWithinDeltaOfScreen(View paramView, int paramInt1, int paramInt2)
  {
    paramView.getDrawingRect(mTempRect);
    offsetDescendantRectToMyCoords(paramView, mTempRect);
    boolean bool;
    if ((mTempRect.bottom + paramInt1 >= getScrollY()) && (mTempRect.top - paramInt1 <= getScrollY() + paramInt2)) {
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
      mLastMotionY = ((int)paramMotionEvent.getY(i));
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
    int i = getHeight();
    int j = getScrollY();
    i = j + i;
    boolean bool2;
    if (paramInt1 == 33) {
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
      doScrollY(paramInt2);
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
      scrollBy(0, i);
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
        scrollBy(0, i);
      } else {
        smoothScrollBy(0, i);
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
    throw new IllegalStateException("ScrollView can host only one direct child");
  }
  
  public void addView(View paramView, int paramInt)
  {
    if (getChildCount() <= 0)
    {
      super.addView(paramView, paramInt);
      return;
    }
    throw new IllegalStateException("ScrollView can host only one direct child");
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams)
  {
    if (getChildCount() <= 0)
    {
      super.addView(paramView, paramInt, paramLayoutParams);
      return;
    }
    throw new IllegalStateException("ScrollView can host only one direct child");
  }
  
  public void addView(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    if (getChildCount() <= 0)
    {
      super.addView(paramView, paramLayoutParams);
      return;
    }
    throw new IllegalStateException("ScrollView can host only one direct child");
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
    if ((localView1 != null) && (isWithinDeltaOfScreen(localView1, i, getHeight())))
    {
      localView1.getDrawingRect(mTempRect);
      offsetDescendantRectToMyCoords(localView1, mTempRect);
      doScrollY(computeScrollDeltaToGetChildRectOnScreen(mTempRect));
      localView1.requestFocus(paramInt);
    }
    else
    {
      int j = i;
      int k;
      if ((paramInt == 33) && (getScrollY() < j))
      {
        k = getScrollY();
      }
      else
      {
        k = j;
        if (paramInt == 130)
        {
          k = j;
          if (getChildCount() > 0)
          {
            int m = getChildAt(0).getBottom();
            int n = getScrollY() + getHeight() - mPaddingBottom;
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
      if (paramInt != 130) {
        k = -k;
      }
      doScrollY(k);
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
        overScrollBy(k - i, m - j, i, j, 0, n, 0, mOverflingDistance, false);
        onScrollChanged(mScrollX, mScrollY, i, j);
        if (i3 != 0) {
          if ((m < 0) && (j >= 0)) {
            mEdgeGlowTop.onAbsorb((int)mScroller.getCurrVelocity());
          } else if ((m > n) && (j <= n)) {
            mEdgeGlowBottom.onAbsorb((int)mScroller.getCurrVelocity());
          }
        }
      }
      if (!awakenScrollBars()) {
        postInvalidateOnAnimation();
      }
    }
    else if (mFlingStrictSpan != null)
    {
      mFlingStrictSpan.finish();
      mFlingStrictSpan = null;
    }
  }
  
  protected int computeScrollDeltaToGetChildRectOnScreen(Rect paramRect)
  {
    if (getChildCount() == 0) {
      return 0;
    }
    int i = getHeight();
    int j = getScrollY();
    int k = j + i;
    int m = getVerticalFadingEdgeLength();
    int n = j;
    if (top > 0) {
      n = j + m;
    }
    j = k;
    if (bottom < getChildAt(0).getHeight()) {
      j = k - m;
    }
    m = 0;
    if ((bottom > j) && (top > n))
    {
      if (paramRect.height() > i) {
        k = 0 + (top - n);
      } else {
        k = 0 + (bottom - j);
      }
      k = Math.min(k, getChildAt(0).getBottom() - j);
    }
    else
    {
      k = m;
      if (top < n)
      {
        k = m;
        if (bottom < j)
        {
          if (paramRect.height() > i) {
            k = 0 - (j - bottom);
          } else {
            k = 0 - (n - top);
          }
          k = Math.max(k, -getScrollY());
        }
      }
    }
    return k;
  }
  
  protected int computeVerticalScrollOffset()
  {
    return Math.max(0, super.computeVerticalScrollOffset());
  }
  
  protected int computeVerticalScrollRange()
  {
    int i = getChildCount();
    int j = getHeight() - mPaddingBottom - mPaddingTop;
    if (i == 0) {
      return j;
    }
    i = getChildAt(0).getBottom();
    int k = mScrollY;
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
    if (mEdgeGlowTop != null)
    {
      int i = mScrollY;
      boolean bool = getClipToPadding();
      int j;
      int k;
      int m;
      float f1;
      float f2;
      if (!mEdgeGlowTop.isFinished())
      {
        j = paramCanvas.save();
        if (bool)
        {
          k = getWidth() - mPaddingLeft - mPaddingRight;
          m = getHeight() - mPaddingTop - mPaddingBottom;
          f1 = mPaddingLeft;
          f2 = mPaddingTop;
        }
        else
        {
          k = getWidth();
          m = getHeight();
          f1 = 0.0F;
          f2 = 0.0F;
        }
        paramCanvas.translate(f1, Math.min(0, i) + f2);
        mEdgeGlowTop.setSize(k, m);
        if (mEdgeGlowTop.draw(paramCanvas)) {
          postInvalidateOnAnimation();
        }
        paramCanvas.restoreToCount(j);
      }
      if (!mEdgeGlowBottom.isFinished())
      {
        j = paramCanvas.save();
        if (bool)
        {
          k = getWidth() - mPaddingLeft - mPaddingRight;
          m = getHeight() - mPaddingTop - mPaddingBottom;
          f2 = mPaddingLeft;
          f1 = mPaddingTop;
        }
        else
        {
          k = getWidth();
          m = getHeight();
          f2 = 0.0F;
          f1 = 0.0F;
        }
        paramCanvas.translate(-k + f2, Math.max(getScrollRange(), i) + m + f1);
        paramCanvas.rotate(180.0F, k, 0.0F);
        mEdgeGlowBottom.setSize(k, m);
        if (mEdgeGlowBottom.draw(paramCanvas)) {
          postInvalidateOnAnimation();
        }
        paramCanvas.restoreToCount(j);
      }
    }
  }
  
  protected void encodeProperties(ViewHierarchyEncoder paramViewHierarchyEncoder)
  {
    super.encodeProperties(paramViewHierarchyEncoder);
    paramViewHierarchyEncoder.addProperty("fillViewport", mFillViewport);
  }
  
  public boolean executeKeyEvent(KeyEvent paramKeyEvent)
  {
    mTempRect.setEmpty();
    boolean bool1 = canScroll();
    int i = 130;
    if (!bool1)
    {
      bool1 = isFocused();
      bool2 = false;
      if ((bool1) && (paramKeyEvent.getKeyCode() != 4))
      {
        View localView = findFocus();
        paramKeyEvent = localView;
        if (localView == this) {
          paramKeyEvent = null;
        }
        paramKeyEvent = FocusFinder.getInstance().findNextFocus(this, paramKeyEvent, 130);
        bool1 = bool2;
        if (paramKeyEvent != null)
        {
          bool1 = bool2;
          if (paramKeyEvent != this)
          {
            bool1 = bool2;
            if (paramKeyEvent.requestFocus(130)) {
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
        case 20: 
          if (!paramKeyEvent.isAltPressed()) {
            bool1 = arrowScroll(130);
          } else {
            bool1 = fullScroll(130);
          }
          break;
        case 19: 
          if (!paramKeyEvent.isAltPressed()) {
            bool1 = arrowScroll(33);
          } else {
            bool1 = fullScroll(33);
          }
          break;
        }
      }
      else
      {
        if (paramKeyEvent.isShiftPressed()) {
          i = 33;
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
      int i = getHeight() - mPaddingBottom - mPaddingTop;
      int j = getChildAt(0).getHeight();
      mScroller.fling(mScrollX, mScrollY, 0, paramInt, 0, 0, 0, Math.max(0, j - i), 0, i / 2);
      if (mFlingStrictSpan == null) {
        mFlingStrictSpan = StrictMode.enterCriticalSpan("ScrollView-fling");
      }
      postInvalidateOnAnimation();
    }
  }
  
  public boolean fullScroll(int paramInt)
  {
    int i;
    if (paramInt == 130) {
      i = 1;
    } else {
      i = 0;
    }
    int j = getHeight();
    mTempRect.top = 0;
    mTempRect.bottom = j;
    if (i != 0)
    {
      i = getChildCount();
      if (i > 0)
      {
        View localView = getChildAt(i - 1);
        mTempRect.bottom = (localView.getBottom() + mPaddingBottom);
        mTempRect.top = (mTempRect.bottom - j);
      }
    }
    return scrollAndFocus(paramInt, mTempRect.top, mTempRect.bottom);
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return ScrollView.class.getName();
  }
  
  protected float getBottomFadingEdgeStrength()
  {
    if (getChildCount() == 0) {
      return 0.0F;
    }
    int i = getVerticalFadingEdgeLength();
    int j = getHeight();
    int k = mPaddingBottom;
    k = getChildAt(0).getBottom() - mScrollY - (j - k);
    if (k < i) {
      return k / i;
    }
    return 1.0F;
  }
  
  public int getMaxScrollAmount()
  {
    return (int)(0.5F * (mBottom - mTop));
  }
  
  protected float getTopFadingEdgeStrength()
  {
    if (getChildCount() == 0) {
      return 0.0F;
    }
    int i = getVerticalFadingEdgeLength();
    if (mScrollY < i) {
      return mScrollY / i;
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
    int i = getChildMeasureSpec(paramInt1, mPaddingLeft + mPaddingRight, width);
    paramInt1 = mPaddingTop;
    int j = mPaddingBottom;
    paramView.measure(i, View.MeasureSpec.makeSafeMeasureSpec(Math.max(0, View.MeasureSpec.getSize(paramInt2) - (paramInt1 + j)), 0));
  }
  
  protected void measureChildWithMargins(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    paramInt2 = getChildMeasureSpec(paramInt1, mPaddingLeft + mPaddingRight + leftMargin + rightMargin + paramInt2, width);
    int i = mPaddingTop;
    int j = mPaddingBottom;
    paramInt1 = topMargin;
    int k = bottomMargin;
    paramView.measure(paramInt2, View.MeasureSpec.makeSafeMeasureSpec(Math.max(0, View.MeasureSpec.getSize(paramInt3) - (i + j + paramInt1 + k + paramInt4)), 0));
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    if (mScrollStrictSpan != null)
    {
      mScrollStrictSpan.finish();
      mScrollStrictSpan = null;
    }
    if (mFlingStrictSpan != null)
    {
      mFlingStrictSpan.finish();
      mFlingStrictSpan = null;
    }
  }
  
  public boolean onGenericMotionEvent(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent.getAction() == 8)
    {
      float f;
      if (paramMotionEvent.isFromSource(2)) {
        f = paramMotionEvent.getAxisValue(9);
      }
      for (;;)
      {
        break;
        if (paramMotionEvent.isFromSource(4194304)) {
          f = paramMotionEvent.getAxisValue(26);
        } else {
          f = 0.0F;
        }
      }
      int i = Math.round(mVerticalScrollFactor * f);
      if (i != 0)
      {
        int j = getScrollRange();
        int k = mScrollY;
        int m = k - i;
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
          super.scrollTo(mScrollX, i);
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
    paramAccessibilityEvent.setMaxScrollX(mScrollX);
    paramAccessibilityEvent.setMaxScrollY(getScrollRange());
  }
  
  public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfoInternal(paramAccessibilityNodeInfo);
    if (isEnabled())
    {
      int i = getScrollRange();
      if (i > 0)
      {
        paramAccessibilityNodeInfo.setScrollable(true);
        if (mScrollY > 0)
        {
          paramAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_BACKWARD);
          paramAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_UP);
        }
        if (mScrollY < i)
        {
          paramAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD);
          paramAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_DOWN);
        }
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
    if ((getScrollY() == 0) && (!canScrollVertically(1))) {
      return false;
    }
    i &= 0xFF;
    if (i != 6) {
      switch (i)
      {
      default: 
        break;
      case 2: 
        i = mActivePointerId;
        if (i == -1) {
          break;
        }
        int j = paramMotionEvent.findPointerIndex(i);
        if (j == -1)
        {
          paramMotionEvent = new StringBuilder();
          paramMotionEvent.append("Invalid pointerId=");
          paramMotionEvent.append(i);
          paramMotionEvent.append(" in onInterceptTouchEvent");
          Log.e("ScrollView", paramMotionEvent.toString());
        }
        else
        {
          i = (int)paramMotionEvent.getY(j);
          if ((Math.abs(i - mLastMotionY) > mTouchSlop) && ((0x2 & getNestedScrollAxes()) == 0))
          {
            mIsBeingDragged = true;
            mLastMotionY = i;
            initVelocityTrackerIfNotExists();
            mVelocityTracker.addMovement(paramMotionEvent);
            mNestedYOffset = 0;
            if (mScrollStrictSpan == null) {
              mScrollStrictSpan = StrictMode.enterCriticalSpan("ScrollView-scroll");
            }
            paramMotionEvent = getParent();
            if (paramMotionEvent != null) {
              paramMotionEvent.requestDisallowInterceptTouchEvent(true);
            }
          }
        }
        break;
      case 1: 
      case 3: 
        mIsBeingDragged = false;
        mActivePointerId = -1;
        recycleVelocityTracker();
        if (mScroller.springBack(mScrollX, mScrollY, 0, 0, 0, getScrollRange())) {
          postInvalidateOnAnimation();
        }
        stopNestedScroll();
        break;
      case 0: 
        i = (int)paramMotionEvent.getY();
        if (!inChild((int)paramMotionEvent.getX(), i))
        {
          mIsBeingDragged = false;
          recycleVelocityTracker();
        }
        else
        {
          mLastMotionY = i;
          mActivePointerId = paramMotionEvent.getPointerId(0);
          initOrResetVelocityTracker();
          mVelocityTracker.addMovement(paramMotionEvent);
          mScroller.computeScrollOffset();
          mIsBeingDragged = (true ^ mScroller.isFinished());
          if ((mIsBeingDragged) && (mScrollStrictSpan == null)) {
            mScrollStrictSpan = StrictMode.enterCriticalSpan("ScrollView-scroll");
          }
          startNestedScroll(2);
        }
        break;
      }
    } else {
      onSecondaryPointerUp(paramMotionEvent);
    }
    return mIsBeingDragged;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    mIsLayoutDirty = false;
    if ((mChildToScrollTo != null) && (isViewDescendantOf(mChildToScrollTo, this))) {
      scrollToChild(mChildToScrollTo);
    }
    mChildToScrollTo = null;
    if (!isLaidOut())
    {
      if (mSavedState != null)
      {
        mScrollY = mSavedState.scrollPosition;
        mSavedState = null;
      }
      if (getChildCount() > 0) {
        paramInt1 = getChildAt(0).getMeasuredHeight();
      } else {
        paramInt1 = 0;
      }
      paramInt1 = Math.max(0, paramInt1 - (paramInt4 - paramInt2 - mPaddingBottom - mPaddingTop));
      if (mScrollY > paramInt1) {
        mScrollY = paramInt1;
      } else if (mScrollY < 0) {
        mScrollY = 0;
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
    if (View.MeasureSpec.getMode(paramInt2) == 0) {
      return;
    }
    if (getChildCount() > 0)
    {
      View localView = getChildAt(0);
      paramInt2 = getContextgetApplicationInfotargetSdkVersion;
      FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams)localView.getLayoutParams();
      if (paramInt2 >= 23)
      {
        paramInt2 = mPaddingLeft + mPaddingRight + leftMargin + rightMargin;
        i = mPaddingTop + mPaddingBottom + topMargin + bottomMargin;
      }
      else
      {
        paramInt2 = mPaddingLeft + mPaddingRight;
        i = mPaddingTop + mPaddingBottom;
      }
      int i = getMeasuredHeight() - i;
      if (localView.getMeasuredHeight() < i) {
        localView.measure(getChildMeasureSpec(paramInt1, paramInt2, width), View.MeasureSpec.makeMeasureSpec(i, 1073741824));
      }
    }
  }
  
  public boolean onNestedFling(View paramView, float paramFloat1, float paramFloat2, boolean paramBoolean)
  {
    if (!paramBoolean)
    {
      flingWithNestedDispatch((int)paramFloat2);
      return true;
    }
    return false;
  }
  
  public void onNestedScroll(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    paramInt1 = mScrollY;
    scrollBy(0, paramInt4);
    paramInt1 = mScrollY - paramInt1;
    dispatchNestedScroll(0, paramInt1, 0, paramInt4 - paramInt1, null);
  }
  
  public void onNestedScrollAccepted(View paramView1, View paramView2, int paramInt)
  {
    super.onNestedScrollAccepted(paramView1, paramView2, paramInt);
    startNestedScroll(2);
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
      if (paramBoolean2) {
        mScroller.springBack(mScrollX, mScrollY, 0, 0, 0, getScrollRange());
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
      i = 130;
    }
    else
    {
      i = paramInt;
      if (paramInt == 1) {
        i = 33;
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
    scrollPosition = mScrollY;
    return localSavedState;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    View localView = findFocus();
    if ((localView != null) && (this != localView))
    {
      if (isWithinDeltaOfScreen(localView, 0, paramInt4))
      {
        localView.getDrawingRect(mTempRect);
        offsetDescendantRectToMyCoords(localView, mTempRect);
        doScrollY(computeScrollDeltaToGetChildRectOnScreen(mTempRect));
      }
      return;
    }
  }
  
  public boolean onStartNestedScroll(View paramView1, View paramView2, int paramInt)
  {
    boolean bool;
    if ((paramInt & 0x2) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void onStopNestedScroll(View paramView)
  {
    super.onStopNestedScroll(paramView);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    initVelocityTrackerIfNotExists();
    MotionEvent localMotionEvent = MotionEvent.obtain(paramMotionEvent);
    int i = paramMotionEvent.getActionMasked();
    int j = 0;
    if (i == 0) {
      mNestedYOffset = 0;
    }
    localMotionEvent.offsetLocation(0.0F, mNestedYOffset);
    ViewParent localViewParent;
    switch (i)
    {
    case 4: 
    default: 
      break;
    case 6: 
      onSecondaryPointerUp(paramMotionEvent);
      mLastMotionY = ((int)paramMotionEvent.getY(paramMotionEvent.findPointerIndex(mActivePointerId)));
      break;
    case 5: 
      i = paramMotionEvent.getActionIndex();
      mLastMotionY = ((int)paramMotionEvent.getY(i));
      mActivePointerId = paramMotionEvent.getPointerId(i);
      break;
    case 3: 
      if ((mIsBeingDragged) && (getChildCount() > 0))
      {
        if (mScroller.springBack(mScrollX, mScrollY, 0, 0, 0, getScrollRange())) {
          postInvalidateOnAnimation();
        }
        mActivePointerId = -1;
        endDrag();
      }
      break;
    case 2: 
      int k = paramMotionEvent.findPointerIndex(mActivePointerId);
      if (k == -1)
      {
        paramMotionEvent = new StringBuilder();
        paramMotionEvent.append("Invalid pointerId=");
        paramMotionEvent.append(mActivePointerId);
        paramMotionEvent.append(" in onTouchEvent");
        Log.e("ScrollView", paramMotionEvent.toString());
      }
      else
      {
        int m = (int)paramMotionEvent.getY(k);
        i = mLastMotionY - m;
        int n = i;
        if (dispatchNestedPreScroll(0, i, mScrollConsumed, mScrollOffset))
        {
          n = i - mScrollConsumed[1];
          localMotionEvent.offsetLocation(0.0F, mScrollOffset[1]);
          mNestedYOffset += mScrollOffset[1];
        }
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
          mLastMotionY = (m - mScrollOffset[1]);
          int i1 = mScrollY;
          m = getScrollRange();
          n = getOverScrollMode();
          if ((n != 0) && ((n != 1) || (m <= 0))) {
            n = j;
          } else {
            n = 1;
          }
          if ((overScrollBy(0, i, 0, mScrollY, 0, m, 0, mOverscrollDistance, true)) && (!hasNestedScrollingParent())) {
            mVelocityTracker.clear();
          }
          j = mScrollY - i1;
          if (dispatchNestedScroll(0, j, 0, i - j, mScrollOffset))
          {
            mLastMotionY -= mScrollOffset[1];
            localMotionEvent.offsetLocation(0.0F, mScrollOffset[1]);
            mNestedYOffset += mScrollOffset[1];
          }
          else if (n != 0)
          {
            n = i1 + i;
            if (n < 0)
            {
              mEdgeGlowTop.onPull(i / getHeight(), paramMotionEvent.getX(k) / getWidth());
              if (!mEdgeGlowBottom.isFinished()) {
                mEdgeGlowBottom.onRelease();
              }
            }
            else if (n > m)
            {
              mEdgeGlowBottom.onPull(i / getHeight(), 1.0F - paramMotionEvent.getX(k) / getWidth());
              if (!mEdgeGlowTop.isFinished()) {
                mEdgeGlowTop.onRelease();
              }
            }
            if ((mEdgeGlowTop != null) && ((!mEdgeGlowTop.isFinished()) || (!mEdgeGlowBottom.isFinished()))) {
              postInvalidateOnAnimation();
            }
          }
        }
      }
      break;
    case 1: 
      if (mIsBeingDragged)
      {
        paramMotionEvent = mVelocityTracker;
        paramMotionEvent.computeCurrentVelocity(1000, mMaximumVelocity);
        i = (int)paramMotionEvent.getYVelocity(mActivePointerId);
        if (Math.abs(i) > mMinimumVelocity) {
          flingWithNestedDispatch(-i);
        } else if (mScroller.springBack(mScrollX, mScrollY, 0, 0, 0, getScrollRange())) {
          postInvalidateOnAnimation();
        }
        mActivePointerId = -1;
        endDrag();
      }
      break;
    case 0: 
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
      if (!mScroller.isFinished())
      {
        mScroller.abortAnimation();
        if (mFlingStrictSpan != null)
        {
          mFlingStrictSpan.finish();
          mFlingStrictSpan = null;
        }
      }
      mLastMotionY = ((int)paramMotionEvent.getY());
      mActivePointerId = paramMotionEvent.getPointerId(0);
      startNestedScroll(2);
    }
    if (mVelocityTracker != null) {
      mVelocityTracker.addMovement(localMotionEvent);
    }
    localMotionEvent.recycle();
    return true;
  }
  
  public boolean pageScroll(int paramInt)
  {
    int i;
    if (paramInt == 130) {
      i = 1;
    } else {
      i = 0;
    }
    int j = getHeight();
    if (i != 0)
    {
      mTempRect.top = (getScrollY() + j);
      i = getChildCount();
      if (i > 0)
      {
        View localView = getChildAt(i - 1);
        if (mTempRect.top + j > localView.getBottom()) {
          mTempRect.top = (localView.getBottom() - j);
        }
      }
    }
    else
    {
      mTempRect.top = (getScrollY() - j);
      if (mTempRect.top < 0) {
        mTempRect.top = 0;
      }
    }
    mTempRect.bottom = (mTempRect.top + j);
    return scrollAndFocus(paramInt, mTempRect.top, mTempRect.bottom);
  }
  
  public boolean performAccessibilityActionInternal(int paramInt, Bundle paramBundle)
  {
    if (super.performAccessibilityActionInternal(paramInt, paramBundle)) {
      return true;
    }
    if (!isEnabled()) {
      return false;
    }
    if (paramInt != 4096) {
      if ((paramInt != 8192) && (paramInt != 16908344))
      {
        if (paramInt != 16908346) {
          return false;
        }
      }
      else
      {
        i = getHeight();
        paramInt = mPaddingBottom;
        j = mPaddingTop;
        paramInt = Math.max(mScrollY - (i - paramInt - j), 0);
        if (paramInt != mScrollY)
        {
          smoothScrollTo(0, paramInt);
          return true;
        }
        return false;
      }
    }
    paramInt = getHeight();
    int i = mPaddingBottom;
    int j = mPaddingTop;
    paramInt = Math.min(mScrollY + (paramInt - i - j), getScrollRange());
    if (paramInt != mScrollY)
    {
      smoothScrollTo(0, paramInt);
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
      if (mEdgeGlowTop == null)
      {
        Context localContext = getContext();
        mEdgeGlowTop = new EdgeEffect(localContext);
        mEdgeGlowBottom = new EdgeEffect(localContext);
      }
    }
    else
    {
      mEdgeGlowTop = null;
      mEdgeGlowBottom = null;
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
      int i = getHeight();
      paramInt1 = mPaddingBottom;
      int j = mPaddingTop;
      i = Math.max(0, getChildAt(0).getHeight() - (i - paramInt1 - j));
      paramInt1 = mScrollY;
      paramInt2 = Math.max(0, Math.min(paramInt1 + paramInt2, i));
      mScroller.startScroll(mScrollX, paramInt1, 0, paramInt2 - paramInt1);
      postInvalidateOnAnimation();
    }
    else
    {
      if (!mScroller.isFinished())
      {
        mScroller.abortAnimation();
        if (mFlingStrictSpan != null)
        {
          mFlingStrictSpan.finish();
          mFlingStrictSpan = null;
        }
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
      public ScrollView.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ScrollView.SavedState(paramAnonymousParcel);
      }
      
      public ScrollView.SavedState[] newArray(int paramAnonymousInt)
      {
        return new ScrollView.SavedState[paramAnonymousInt];
      }
    };
    public int scrollPosition;
    
    public SavedState(Parcel paramParcel)
    {
      super();
      scrollPosition = paramParcel.readInt();
    }
    
    SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("ScrollView.SavedState{");
      localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      localStringBuilder.append(" scrollPosition=");
      localStringBuilder.append(scrollPosition);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(scrollPosition);
    }
  }
}
