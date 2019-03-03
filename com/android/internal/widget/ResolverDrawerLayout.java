package com.android.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnTouchModeChangeListener;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.OverScroller;
import android.widget.ScrollView;
import com.android.internal.R.styleable;

public class ResolverDrawerLayout
  extends ViewGroup
{
  private static final String TAG = "ResolverDrawerLayout";
  private int mActivePointerId = -1;
  private float mCollapseOffset;
  private int mCollapsibleHeight;
  private int mCollapsibleHeightReserved;
  private boolean mDismissLocked;
  private boolean mDismissOnScrollerFinished;
  private float mInitialTouchX;
  private float mInitialTouchY;
  private boolean mIsDragging;
  private float mLastTouchY;
  private int mMaxCollapsedHeight;
  private int mMaxCollapsedHeightSmall;
  private int mMaxWidth;
  private final float mMinFlingVelocity;
  private OnDismissedListener mOnDismissedListener;
  private boolean mOpenOnClick;
  private boolean mOpenOnLayout;
  private RunOnDismissedListener mRunOnDismissedListener;
  private Drawable mScrollIndicatorDrawable;
  private final OverScroller mScroller;
  private boolean mShowAtTop;
  private boolean mSmallCollapsed;
  private final Rect mTempRect = new Rect();
  private int mTopOffset;
  private final ViewTreeObserver.OnTouchModeChangeListener mTouchModeChangeListener = new ViewTreeObserver.OnTouchModeChangeListener()
  {
    public void onTouchModeChanged(boolean paramAnonymousBoolean)
    {
      if ((!paramAnonymousBoolean) && (hasFocus()) && (ResolverDrawerLayout.this.isDescendantClipped(getFocusedChild()))) {
        ResolverDrawerLayout.this.smoothScrollTo(0, 0.0F);
      }
    }
  };
  private final int mTouchSlop;
  private int mUncollapsibleHeight;
  private final VelocityTracker mVelocityTracker;
  
  public ResolverDrawerLayout(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public ResolverDrawerLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public ResolverDrawerLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ResolverDrawerLayout, paramInt, 0);
    mMaxWidth = paramAttributeSet.getDimensionPixelSize(0, -1);
    mMaxCollapsedHeight = paramAttributeSet.getDimensionPixelSize(1, 0);
    mMaxCollapsedHeightSmall = paramAttributeSet.getDimensionPixelSize(2, mMaxCollapsedHeight);
    mShowAtTop = paramAttributeSet.getBoolean(3, false);
    paramAttributeSet.recycle();
    mScrollIndicatorDrawable = mContext.getDrawable(17303594);
    mScroller = new OverScroller(paramContext, AnimationUtils.loadInterpolator(paramContext, 17563653));
    mVelocityTracker = VelocityTracker.obtain();
    paramContext = ViewConfiguration.get(paramContext);
    mTouchSlop = paramContext.getScaledTouchSlop();
    mMinFlingVelocity = paramContext.getScaledMinimumFlingVelocity();
    setImportantForAccessibility(1);
  }
  
  private void abortAnimation()
  {
    mScroller.abortAnimation();
    mRunOnDismissedListener = null;
    mDismissOnScrollerFinished = false;
  }
  
  private void dismiss()
  {
    mRunOnDismissedListener = new RunOnDismissedListener(null);
    post(mRunOnDismissedListener);
  }
  
  private float distanceInfluenceForSnapDuration(float paramFloat)
  {
    return (float)Math.sin((float)((paramFloat - 0.5F) * 0.4712389167638204D));
  }
  
  private View findChildUnder(float paramFloat1, float paramFloat2)
  {
    return findChildUnder(this, paramFloat1, paramFloat2);
  }
  
  private static View findChildUnder(ViewGroup paramViewGroup, float paramFloat1, float paramFloat2)
  {
    for (int i = paramViewGroup.getChildCount() - 1; i >= 0; i--)
    {
      View localView = paramViewGroup.getChildAt(i);
      if (isChildUnder(localView, paramFloat1, paramFloat2)) {
        return localView;
      }
    }
    return null;
  }
  
  private View findListChildUnder(float paramFloat1, float paramFloat2)
  {
    View localView = findChildUnder(paramFloat1, paramFloat2);
    while (localView != null)
    {
      paramFloat1 -= localView.getX();
      paramFloat2 -= localView.getY();
      if ((localView instanceof AbsListView)) {
        return findChildUnder((ViewGroup)localView, paramFloat1, paramFloat2);
      }
      if ((localView instanceof ViewGroup)) {
        localView = findChildUnder((ViewGroup)localView, paramFloat1, paramFloat2);
      } else {
        localView = null;
      }
    }
    return localView;
  }
  
  private int getMaxCollapsedHeight()
  {
    int i;
    if (isSmallCollapsed()) {
      i = mMaxCollapsedHeightSmall;
    } else {
      i = mMaxCollapsedHeight;
    }
    return i + mCollapsibleHeightReserved;
  }
  
  private static boolean isChildUnder(View paramView, float paramFloat1, float paramFloat2)
  {
    float f1 = paramView.getX();
    float f2 = paramView.getY();
    float f3 = paramView.getWidth();
    float f4 = paramView.getHeight();
    boolean bool;
    if ((paramFloat1 >= f1) && (paramFloat2 >= f2) && (paramFloat1 < f3 + f1) && (paramFloat2 < f4 + f2)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isDescendantClipped(View paramView)
  {
    Object localObject = mTempRect;
    int i = paramView.getWidth();
    int j = paramView.getHeight();
    boolean bool = false;
    ((Rect)localObject).set(0, 0, i, j);
    offsetDescendantRectToMyCoords(paramView, mTempRect);
    if (paramView.getParent() != this)
    {
      localObject = paramView;
      ViewParent localViewParent = paramView.getParent();
      paramView = (View)localObject;
      while (localViewParent != this)
      {
        paramView = (View)localViewParent;
        localViewParent = paramView.getParent();
      }
    }
    i = getHeight() - getPaddingBottom();
    int k = getChildCount();
    for (j = indexOfChild(paramView) + 1; j < k; j++)
    {
      paramView = getChildAt(j);
      if (paramView.getVisibility() != 8) {
        i = Math.min(i, paramView.getTop());
      }
    }
    if (mTempRect.top > i) {
      bool = true;
    }
    return bool;
  }
  
  private boolean isDismissable()
  {
    boolean bool;
    if ((mOnDismissedListener != null) && (!mDismissLocked)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isDragging()
  {
    boolean bool;
    if ((!mIsDragging) && (getNestedScrollAxes() != 2)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private boolean isListChildUnderClipped(float paramFloat1, float paramFloat2)
  {
    View localView = findListChildUnder(paramFloat1, paramFloat2);
    boolean bool;
    if ((localView != null) && (isDescendantClipped(localView))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isMoving()
  {
    boolean bool;
    if ((!mIsDragging) && (mScroller.isFinished())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private void onCollapsedChanged(boolean paramBoolean)
  {
    notifyViewAccessibilityStateChangedIfNeeded(0);
    if (mScrollIndicatorDrawable != null) {
      setWillNotDraw(paramBoolean ^ true);
    }
  }
  
  private void onSecondaryPointerUp(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getActionIndex();
    if (paramMotionEvent.getPointerId(i) == mActivePointerId)
    {
      if (i == 0) {
        i = 1;
      } else {
        i = 0;
      }
      mInitialTouchX = paramMotionEvent.getX(i);
      float f = paramMotionEvent.getY(i);
      mLastTouchY = f;
      mInitialTouchY = f;
      mActivePointerId = paramMotionEvent.getPointerId(i);
    }
  }
  
  private float performDrag(float paramFloat)
  {
    if (getShowAtTop()) {
      return 0.0F;
    }
    paramFloat = Math.max(0.0F, Math.min(mCollapseOffset + paramFloat, mCollapsibleHeight + mUncollapsibleHeight));
    if (paramFloat != mCollapseOffset)
    {
      float f = paramFloat - mCollapseOffset;
      int i = getChildCount();
      boolean bool1 = false;
      for (int j = 0; j < i; j++)
      {
        View localView = getChildAt(j);
        if (!getLayoutParamsignoreOffset) {
          localView.offsetTopAndBottom((int)f);
        }
      }
      boolean bool2;
      if (mCollapseOffset != 0.0F) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      mCollapseOffset = paramFloat;
      mTopOffset = ((int)(mTopOffset + f));
      if (paramFloat != 0.0F) {
        bool1 = true;
      }
      if (bool2 != bool1) {
        onCollapsedChanged(bool1);
      }
      postInvalidateOnAnimation();
      return f;
    }
    return 0.0F;
  }
  
  private void resetTouch()
  {
    mActivePointerId = -1;
    mIsDragging = false;
    mOpenOnClick = false;
    mLastTouchY = 0.0F;
    mInitialTouchY = 0.0F;
    mInitialTouchX = 0.0F;
    mVelocityTracker.clear();
  }
  
  private void smoothScrollTo(int paramInt, float paramFloat)
  {
    abortAnimation();
    int i = (int)mCollapseOffset;
    int j = paramInt - i;
    if (j == 0) {
      return;
    }
    int k = getHeight();
    paramInt = k / 2;
    float f1 = Math.min(1.0F, Math.abs(j) * 1.0F / k);
    float f2 = paramInt;
    float f3 = paramInt;
    f1 = distanceInfluenceForSnapDuration(f1);
    paramFloat = Math.abs(paramFloat);
    if (paramFloat > 0.0F) {
      paramInt = 4 * Math.round(1000.0F * Math.abs((f2 + f3 * f1) / paramFloat));
    } else {
      paramInt = (int)((1.0F + Math.abs(j) / k) * 100.0F);
    }
    paramInt = Math.min(paramInt, 300);
    mScroller.startScroll(0, i, 0, j, paramInt);
    postInvalidateOnAnimation();
  }
  
  private boolean updateCollapseOffset(int paramInt, boolean paramBoolean)
  {
    int i = mCollapsibleHeight;
    boolean bool1 = false;
    if (paramInt == i) {
      return false;
    }
    boolean bool2 = getShowAtTop();
    float f = 0.0F;
    if (bool2)
    {
      mCollapseOffset = 0.0F;
      return false;
    }
    if (isLaidOut())
    {
      if (mCollapseOffset != 0.0F) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      if ((paramBoolean) && (paramInt < mCollapsibleHeight) && (mCollapseOffset == paramInt)) {
        mCollapseOffset = mCollapsibleHeight;
      } else {
        mCollapseOffset = Math.min(mCollapseOffset, mCollapsibleHeight);
      }
      paramBoolean = bool1;
      if (mCollapseOffset != 0.0F) {
        paramBoolean = true;
      }
      if (bool2 != paramBoolean) {
        onCollapsedChanged(paramBoolean);
      }
    }
    else
    {
      if (!mOpenOnLayout) {
        f = mCollapsibleHeight;
      }
      mCollapseOffset = f;
    }
    return true;
  }
  
  public void computeScroll()
  {
    super.computeScroll();
    if (mScroller.computeScrollOffset())
    {
      boolean bool = mScroller.isFinished();
      performDrag(mScroller.getCurrY() - mCollapseOffset);
      if ((bool ^ true)) {
        postInvalidateOnAnimation();
      } else if ((mDismissOnScrollerFinished) && (mOnDismissedListener != null)) {
        dismiss();
      }
    }
  }
  
  void dispatchOnDismissed()
  {
    if (mOnDismissedListener != null) {
      mOnDismissedListener.onDismissed();
    }
    if (mRunOnDismissedListener != null)
    {
      removeCallbacks(mRunOnDismissedListener);
      mRunOnDismissedListener = null;
    }
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams()
  {
    return new LayoutParams(-1, -2);
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    if ((paramLayoutParams instanceof LayoutParams)) {
      return new LayoutParams((LayoutParams)paramLayoutParams);
    }
    if ((paramLayoutParams instanceof ViewGroup.MarginLayoutParams)) {
      return new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams);
    }
    return new LayoutParams(paramLayoutParams);
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return ScrollView.class.getName();
  }
  
  public boolean getShowAtTop()
  {
    return mShowAtTop;
  }
  
  public boolean isCollapsed()
  {
    boolean bool;
    if (mCollapseOffset > 0.0F) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isSmallCollapsed()
  {
    return mSmallCollapsed;
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    getViewTreeObserver().addOnTouchModeChangeListener(mTouchModeChangeListener);
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    getViewTreeObserver().removeOnTouchModeChangeListener(mTouchModeChangeListener);
    abortAnimation();
  }
  
  public void onDrawForeground(Canvas paramCanvas)
  {
    if (mScrollIndicatorDrawable != null) {
      mScrollIndicatorDrawable.draw(paramCanvas);
    }
    super.onDrawForeground(paramCanvas);
  }
  
  public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfoInternal(paramAccessibilityNodeInfo);
    if ((isEnabled()) && (mCollapseOffset != 0.0F))
    {
      paramAccessibilityNodeInfo.addAction(4096);
      paramAccessibilityNodeInfo.setScrollable(true);
    }
    paramAccessibilityNodeInfo.removeAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_ACCESSIBILITY_FOCUS);
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getActionMasked();
    if (i == 0) {
      mVelocityTracker.clear();
    }
    mVelocityTracker.addMovement(paramMotionEvent);
    boolean bool1 = true;
    if (i != 6)
    {
      float f1;
      float f2;
      switch (i)
      {
      default: 
        break;
      case 2: 
        f1 = paramMotionEvent.getX();
        f2 = paramMotionEvent.getY();
        float f3 = f2 - mInitialTouchY;
        if ((Math.abs(f3) > mTouchSlop) && (findChildUnder(f1, f2) != null) && ((getNestedScrollAxes() & 0x2) == 0))
        {
          mActivePointerId = paramMotionEvent.getPointerId(0);
          mIsDragging = true;
          mLastTouchY = Math.max(mLastTouchY - mTouchSlop, Math.min(mLastTouchY + f3, mLastTouchY + mTouchSlop));
        }
        break;
      case 1: 
      case 3: 
        resetTouch();
        break;
      case 0: 
        f2 = paramMotionEvent.getX();
        f1 = paramMotionEvent.getY();
        mInitialTouchX = f2;
        mLastTouchY = f1;
        mInitialTouchY = f1;
        if ((isListChildUnderClipped(f2, f1)) && (mCollapseOffset > 0.0F)) {
          bool2 = true;
        } else {
          bool2 = false;
        }
        mOpenOnClick = bool2;
        break;
      }
    }
    else
    {
      onSecondaryPointerUp(paramMotionEvent);
    }
    if (mIsDragging) {
      abortAnimation();
    }
    boolean bool2 = bool1;
    if (!mIsDragging) {
      if (mOpenOnClick) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
    }
    return bool2;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    paramInt4 = getWidth();
    Object localObject = null;
    paramInt3 = mTopOffset;
    int i = getPaddingLeft();
    int j = getPaddingRight();
    int k = getChildCount();
    paramInt2 = 0;
    paramInt1 = paramInt4;
    while (paramInt2 < k)
    {
      View localView = getChildAt(paramInt2);
      LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
      if (hasNestedScrollIndicator) {
        localObject = localView;
      }
      if (localView.getVisibility() != 8)
      {
        int m = topMargin + paramInt3;
        paramInt3 = m;
        if (ignoreOffset) {
          paramInt3 = (int)(m - mCollapseOffset);
        }
        int n = localView.getMeasuredHeight() + paramInt3;
        m = localView.getMeasuredWidth();
        int i1 = (paramInt4 - j - i - m) / 2 + i;
        localView.layout(i1, paramInt3, i1 + m, n);
        paramInt3 = n + bottomMargin;
      }
      paramInt2++;
    }
    if (mScrollIndicatorDrawable != null) {
      if (localObject != null)
      {
        paramInt1 = localObject.getLeft();
        paramInt2 = localObject.getRight();
        paramInt3 = localObject.getTop();
        paramInt4 = mScrollIndicatorDrawable.getIntrinsicHeight();
        mScrollIndicatorDrawable.setBounds(paramInt1, paramInt3 - paramInt4, paramInt2, paramInt3);
        setWillNotDraw(true ^ isCollapsed());
      }
      else
      {
        mScrollIndicatorDrawable = null;
        setWillNotDraw(true);
      }
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getSize(paramInt1);
    int j = i;
    int k = View.MeasureSpec.getSize(paramInt2);
    paramInt1 = j;
    if (mMaxWidth >= 0) {
      paramInt1 = Math.min(j, mMaxWidth);
    }
    j = paramInt1;
    int m = View.MeasureSpec.makeMeasureSpec(j, 1073741824);
    int n = View.MeasureSpec.makeMeasureSpec(k, 1073741824);
    int i1 = getPaddingLeft() + getPaddingRight();
    paramInt2 = getPaddingTop();
    paramInt1 = getPaddingBottom();
    int i2 = getChildCount();
    paramInt1 = paramInt2 + paramInt1;
    int i3;
    View localView;
    for (paramInt2 = 0;; paramInt2++)
    {
      i3 = 8;
      if (paramInt2 >= i2) {
        break;
      }
      localView = getChildAt(paramInt2);
      if ((getLayoutParamsalwaysShow) && (localView.getVisibility() != 8))
      {
        measureChildWithMargins(localView, m, i1, n, paramInt1);
        paramInt1 += localView.getMeasuredHeight();
      }
    }
    int i4 = 0;
    paramInt2 = paramInt1;
    while (i4 < i2)
    {
      localView = getChildAt(i4);
      if ((!getLayoutParamsalwaysShow) && (localView.getVisibility() != i3))
      {
        measureChildWithMargins(localView, m, i1, n, paramInt2);
        paramInt2 += localView.getMeasuredHeight();
      }
      i4++;
    }
    j = mCollapsibleHeight;
    mCollapsibleHeight = Math.max(0, paramInt2 - paramInt1 - getMaxCollapsedHeight());
    mUncollapsibleHeight = (paramInt2 - mCollapsibleHeight);
    updateCollapseOffset(j, isDragging() ^ true);
    if (getShowAtTop()) {
      mTopOffset = 0;
    } else {
      mTopOffset = (Math.max(0, k - paramInt2) + (int)mCollapseOffset);
    }
    setMeasuredDimension(i, k);
  }
  
  public boolean onNestedFling(View paramView, float paramFloat1, float paramFloat2, boolean paramBoolean)
  {
    int i = 0;
    int j = 0;
    if ((!paramBoolean) && (Math.abs(paramFloat2) > mMinFlingVelocity))
    {
      if (getShowAtTop())
      {
        if ((isDismissable()) && (paramFloat2 > 0.0F))
        {
          abortAnimation();
          dismiss();
        }
        else
        {
          if (paramFloat2 < 0.0F) {
            j = mCollapsibleHeight;
          }
          smoothScrollTo(j, paramFloat2);
        }
      }
      else if ((isDismissable()) && (paramFloat2 < 0.0F) && (mCollapseOffset > mCollapsibleHeight))
      {
        smoothScrollTo(mCollapsibleHeight + mUncollapsibleHeight, paramFloat2);
        mDismissOnScrollerFinished = true;
      }
      else
      {
        if (paramFloat2 > 0.0F) {
          j = i;
        } else {
          j = mCollapsibleHeight;
        }
        smoothScrollTo(j, paramFloat2);
      }
      return true;
    }
    return false;
  }
  
  public boolean onNestedPreFling(View paramView, float paramFloat1, float paramFloat2)
  {
    if ((!getShowAtTop()) && (paramFloat2 > mMinFlingVelocity) && (mCollapseOffset != 0.0F))
    {
      smoothScrollTo(0, paramFloat2);
      return true;
    }
    return false;
  }
  
  public boolean onNestedPrePerformAccessibilityAction(View paramView, int paramInt, Bundle paramBundle)
  {
    if (super.onNestedPrePerformAccessibilityAction(paramView, paramInt, paramBundle)) {
      return true;
    }
    if ((paramInt == 4096) && (mCollapseOffset != 0.0F))
    {
      smoothScrollTo(0, 0.0F);
      return true;
    }
    return false;
  }
  
  public void onNestedPreScroll(View paramView, int paramInt1, int paramInt2, int[] paramArrayOfInt)
  {
    if (paramInt2 > 0) {
      paramArrayOfInt[1] = ((int)-performDrag(-paramInt2));
    }
  }
  
  public void onNestedScroll(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (paramInt4 < 0) {
      performDrag(-paramInt4);
    }
  }
  
  public void onNestedScrollAccepted(View paramView1, View paramView2, int paramInt)
  {
    super.onNestedScrollAccepted(paramView1, paramView2, paramInt);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    paramParcelable = (SavedState)paramParcelable;
    super.onRestoreInstanceState(paramParcelable.getSuperState());
    mOpenOnLayout = open;
  }
  
  protected Parcelable onSaveInstanceState()
  {
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    boolean bool;
    if ((mCollapsibleHeight > 0) && (mCollapseOffset == 0.0F)) {
      bool = true;
    } else {
      bool = false;
    }
    open = bool;
    return localSavedState;
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
    if (mScroller.isFinished())
    {
      int i;
      if (mCollapseOffset < mCollapsibleHeight / 2) {
        i = 0;
      } else {
        i = mCollapsibleHeight;
      }
      smoothScrollTo(i, 0.0F);
    }
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getActionMasked();
    mVelocityTracker.addMovement(paramMotionEvent);
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = true;
    int j = 0;
    int k = 0;
    int m = 0;
    int n = 0;
    float f1;
    float f3;
    switch (i)
    {
    case 4: 
    default: 
      break;
    case 6: 
      onSecondaryPointerUp(paramMotionEvent);
      break;
    case 5: 
      n = paramMotionEvent.getActionIndex();
      mActivePointerId = paramMotionEvent.getPointerId(n);
      mInitialTouchX = paramMotionEvent.getX(n);
      f1 = paramMotionEvent.getY(n);
      mLastTouchY = f1;
      mInitialTouchY = f1;
      break;
    case 3: 
      if (mIsDragging)
      {
        if (mCollapseOffset >= mCollapsibleHeight / 2) {
          n = mCollapsibleHeight;
        }
        smoothScrollTo(n, 0.0F);
      }
      resetTouch();
      return true;
    case 2: 
      j = paramMotionEvent.findPointerIndex(mActivePointerId);
      n = j;
      if (j < 0)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Bad pointer id ");
        localStringBuilder.append(mActivePointerId);
        localStringBuilder.append(", resetting");
        Log.e("ResolverDrawerLayout", localStringBuilder.toString());
        n = 0;
        mActivePointerId = paramMotionEvent.getPointerId(0);
        mInitialTouchX = paramMotionEvent.getX();
        f1 = paramMotionEvent.getY();
        mLastTouchY = f1;
        mInitialTouchY = f1;
      }
      float f2 = paramMotionEvent.getX(n);
      f3 = paramMotionEvent.getY(n);
      bool1 = bool2;
      if (!mIsDragging)
      {
        f1 = f3 - mInitialTouchY;
        bool1 = bool2;
        if (Math.abs(f1) > mTouchSlop)
        {
          bool1 = bool2;
          if (findChildUnder(f2, f3) != null)
          {
            mIsDragging = true;
            bool1 = true;
            mLastTouchY = Math.max(mLastTouchY - mTouchSlop, Math.min(mLastTouchY + f1, mLastTouchY + mTouchSlop));
          }
        }
      }
      if (mIsDragging) {
        performDrag(f3 - mLastTouchY);
      }
      mLastTouchY = f3;
      break;
    case 1: 
      bool3 = mIsDragging;
      mIsDragging = false;
      if ((!bool3) && (findChildUnder(mInitialTouchX, mInitialTouchY) == null) && (findChildUnder(paramMotionEvent.getX(), paramMotionEvent.getY()) == null) && (isDismissable()))
      {
        dispatchOnDismissed();
        resetTouch();
        return true;
      }
      if ((mOpenOnClick) && (Math.abs(paramMotionEvent.getX() - mInitialTouchX) < mTouchSlop) && (Math.abs(paramMotionEvent.getY() - mInitialTouchY) < mTouchSlop))
      {
        smoothScrollTo(0, 0.0F);
        return true;
      }
      mVelocityTracker.computeCurrentVelocity(1000);
      f1 = mVelocityTracker.getYVelocity(mActivePointerId);
      if (Math.abs(f1) > mMinFlingVelocity)
      {
        if (getShowAtTop())
        {
          if ((isDismissable()) && (f1 < 0.0F))
          {
            abortAnimation();
            dismiss();
          }
          else
          {
            if (f1 < 0.0F) {
              n = j;
            } else {
              n = mCollapsibleHeight;
            }
            smoothScrollTo(n, f1);
          }
        }
        else if ((isDismissable()) && (f1 > 0.0F) && (mCollapseOffset > mCollapsibleHeight))
        {
          smoothScrollTo(mCollapsibleHeight + mUncollapsibleHeight, f1);
          mDismissOnScrollerFinished = true;
        }
        else
        {
          if (f1 < 0.0F) {
            n = k;
          } else {
            n = mCollapsibleHeight;
          }
          smoothScrollTo(n, f1);
        }
      }
      else
      {
        if (mCollapseOffset < mCollapsibleHeight / 2) {
          n = m;
        } else {
          n = mCollapsibleHeight;
        }
        smoothScrollTo(n, 0.0F);
      }
      resetTouch();
      break;
    case 0: 
      f1 = paramMotionEvent.getX();
      f3 = paramMotionEvent.getY();
      mInitialTouchX = f1;
      mLastTouchY = f3;
      mInitialTouchY = f3;
      mActivePointerId = paramMotionEvent.getPointerId(0);
      if (findChildUnder(mInitialTouchX, mInitialTouchY) != null) {
        n = 1;
      } else {
        n = 0;
      }
      if ((!isDismissable()) && (mCollapsibleHeight <= 0)) {
        bool1 = false;
      } else {
        bool1 = true;
      }
      if ((n == 0) || (!bool1)) {
        bool3 = false;
      }
      mIsDragging = bool3;
      abortAnimation();
    }
    return bool1;
  }
  
  public boolean performAccessibilityActionInternal(int paramInt, Bundle paramBundle)
  {
    if (paramInt == AccessibilityNodeInfo.AccessibilityAction.ACTION_ACCESSIBILITY_FOCUS.getId()) {
      return false;
    }
    if (super.performAccessibilityActionInternal(paramInt, paramBundle)) {
      return true;
    }
    if ((paramInt == 4096) && (mCollapseOffset != 0.0F))
    {
      smoothScrollTo(0, 0.0F);
      return true;
    }
    return false;
  }
  
  public void requestChildFocus(View paramView1, View paramView2)
  {
    super.requestChildFocus(paramView1, paramView2);
    if ((!isInTouchMode()) && (isDescendantClipped(paramView2))) {
      smoothScrollTo(0, 0.0F);
    }
  }
  
  public void setCollapsed(boolean paramBoolean)
  {
    if (!isLaidOut())
    {
      mOpenOnLayout = paramBoolean;
    }
    else
    {
      int i;
      if (paramBoolean) {
        i = mCollapsibleHeight;
      } else {
        i = 0;
      }
      smoothScrollTo(i, 0.0F);
    }
  }
  
  public void setCollapsibleHeightReserved(int paramInt)
  {
    int i = mCollapsibleHeightReserved;
    mCollapsibleHeightReserved = paramInt;
    paramInt = mCollapsibleHeightReserved - i;
    if ((paramInt != 0) && (mIsDragging)) {
      mLastTouchY -= paramInt;
    }
    paramInt = mCollapsibleHeight;
    mCollapsibleHeight = Math.max(mCollapsibleHeight, getMaxCollapsedHeight());
    if (updateCollapseOffset(paramInt, isDragging() ^ true)) {
      return;
    }
    invalidate();
  }
  
  public void setDismissLocked(boolean paramBoolean)
  {
    mDismissLocked = paramBoolean;
  }
  
  public void setOnDismissedListener(OnDismissedListener paramOnDismissedListener)
  {
    mOnDismissedListener = paramOnDismissedListener;
  }
  
  public void setShowAtTop(boolean paramBoolean)
  {
    mShowAtTop = paramBoolean;
    invalidate();
    requestLayout();
  }
  
  public void setSmallCollapsed(boolean paramBoolean)
  {
    mSmallCollapsed = paramBoolean;
    requestLayout();
  }
  
  public static class LayoutParams
    extends ViewGroup.MarginLayoutParams
  {
    public boolean alwaysShow;
    public boolean hasNestedScrollIndicator;
    public boolean ignoreOffset;
    
    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
    }
    
    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ResolverDrawerLayout_LayoutParams);
      alwaysShow = paramContext.getBoolean(1, false);
      ignoreOffset = paramContext.getBoolean(3, false);
      hasNestedScrollIndicator = paramContext.getBoolean(2, false);
      paramContext.recycle();
    }
    
    public LayoutParams(ViewGroup.LayoutParams paramLayoutParams)
    {
      super();
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams paramMarginLayoutParams)
    {
      super();
    }
    
    public LayoutParams(LayoutParams paramLayoutParams)
    {
      super();
      alwaysShow = alwaysShow;
      ignoreOffset = ignoreOffset;
      hasNestedScrollIndicator = hasNestedScrollIndicator;
    }
  }
  
  public static abstract interface OnDismissedListener
  {
    public abstract void onDismissed();
  }
  
  private class RunOnDismissedListener
    implements Runnable
  {
    private RunOnDismissedListener() {}
    
    public void run()
    {
      dispatchOnDismissed();
    }
  }
  
  static class SavedState
    extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public ResolverDrawerLayout.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ResolverDrawerLayout.SavedState(paramAnonymousParcel, null);
      }
      
      public ResolverDrawerLayout.SavedState[] newArray(int paramAnonymousInt)
      {
        return new ResolverDrawerLayout.SavedState[paramAnonymousInt];
      }
    };
    boolean open;
    
    private SavedState(Parcel paramParcel)
    {
      super();
      boolean bool;
      if (paramParcel.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      open = bool;
    }
    
    SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(open);
    }
  }
}
