package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Transformation;
import com.android.internal.R.styleable;

@Deprecated
public class Gallery
  extends AbsSpinner
  implements GestureDetector.OnGestureListener
{
  private static final int SCROLL_TO_FLING_UNCERTAINTY_TIMEOUT = 250;
  private static final String TAG = "Gallery";
  private static final boolean localLOGV = false;
  private int mAnimationDuration = 400;
  private AdapterView.AdapterContextMenuInfo mContextMenuInfo;
  private Runnable mDisableSuppressSelectionChangedRunnable = new Runnable()
  {
    public void run()
    {
      Gallery.access$002(Gallery.this, false);
      selectionChanged();
    }
  };
  private int mDownTouchPosition;
  private View mDownTouchView;
  private FlingRunnable mFlingRunnable = new FlingRunnable();
  private GestureDetector mGestureDetector;
  private int mGravity;
  private boolean mIsFirstScroll;
  private boolean mIsRtl = true;
  private int mLeftMost;
  private boolean mReceivedInvokeKeyDown;
  private int mRightMost;
  private int mSelectedCenterOffset;
  private View mSelectedChild;
  private boolean mShouldCallbackDuringFling = true;
  private boolean mShouldCallbackOnUnselectedItemClick = true;
  private boolean mShouldStopFling;
  private int mSpacing = 0;
  private boolean mSuppressSelectionChanged;
  private float mUnselectedAlpha;
  
  public Gallery(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public Gallery(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842864);
  }
  
  public Gallery(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public Gallery(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.Gallery, paramInt1, paramInt2);
    paramInt1 = paramContext.getInt(0, -1);
    if (paramInt1 >= 0) {
      setGravity(paramInt1);
    }
    paramInt1 = paramContext.getInt(1, -1);
    if (paramInt1 > 0) {
      setAnimationDuration(paramInt1);
    }
    setSpacing(paramContext.getDimensionPixelOffset(2, 0));
    setUnselectedAlpha(paramContext.getFloat(3, 0.5F));
    paramContext.recycle();
    mGroupFlags |= 0x400;
    mGroupFlags |= 0x800;
  }
  
  private int calculateTop(View paramView, boolean paramBoolean)
  {
    int i;
    if (paramBoolean) {
      i = getMeasuredHeight();
    } else {
      i = getHeight();
    }
    int j;
    if (paramBoolean) {
      j = paramView.getMeasuredHeight();
    } else {
      j = paramView.getHeight();
    }
    int k = 0;
    int m = mGravity;
    if (m != 16)
    {
      if (m != 48)
      {
        if (m != 80) {
          i = k;
        } else {
          i = i - mSpinnerPadding.bottom - j;
        }
      }
      else {
        i = mSpinnerPadding.top;
      }
    }
    else
    {
      k = mSpinnerPadding.bottom;
      m = mSpinnerPadding.top;
      i = mSpinnerPadding.top + (i - k - m - j) / 2;
    }
    return i;
  }
  
  private void detachOffScreenChildren(boolean paramBoolean)
  {
    int i = getChildCount();
    int j = mFirstPosition;
    int k = 0;
    int m = 0;
    int n = 0;
    int i1 = 0;
    int i2;
    int i3;
    View localView;
    if (paramBoolean)
    {
      i2 = mPaddingLeft;
      i3 = 0;
      n = i1;
      k = m;
      while (i3 < i)
      {
        if (mIsRtl) {
          m = i - 1 - i3;
        } else {
          m = i3;
        }
        localView = getChildAt(m);
        if (localView.getRight() >= i2) {
          break;
        }
        k = m;
        n++;
        mRecycler.put(j + m, localView);
        i3++;
      }
      if (!mIsRtl) {
        k = 0;
      }
      i3 = n;
    }
    else
    {
      i2 = getWidth();
      i1 = mPaddingRight;
      for (i3 = i - 1; i3 >= 0; i3--)
      {
        if (mIsRtl) {
          m = i - 1 - i3;
        } else {
          m = i3;
        }
        localView = getChildAt(m);
        if (localView.getLeft() <= i2 - i1) {
          break;
        }
        k = m;
        n++;
        mRecycler.put(j + m, localView);
      }
      i3 = n;
      if (mIsRtl)
      {
        k = 0;
        i3 = n;
      }
    }
    detachViewsFromParent(k, i3);
    if (paramBoolean != mIsRtl) {
      mFirstPosition += i3;
    }
  }
  
  private boolean dispatchLongPress(View paramView, int paramInt, long paramLong, float paramFloat1, float paramFloat2, boolean paramBoolean)
  {
    boolean bool1 = false;
    if (mOnItemLongClickListener != null) {
      bool1 = mOnItemLongClickListener.onItemLongClick(this, mDownTouchView, mDownTouchPosition, paramLong);
    }
    boolean bool2 = bool1;
    if (!bool1)
    {
      mContextMenuInfo = new AdapterView.AdapterContextMenuInfo(paramView, paramInt, paramLong);
      if (paramBoolean) {
        bool2 = super.showContextMenuForChild(paramView, paramFloat1, paramFloat2);
      } else {
        bool2 = super.showContextMenuForChild(this);
      }
    }
    if (bool2) {
      performHapticFeedback(0);
    }
    return bool2;
  }
  
  private void dispatchPress(View paramView)
  {
    if (paramView != null) {
      paramView.setPressed(true);
    }
    setPressed(true);
  }
  
  private void dispatchUnpress()
  {
    for (int i = getChildCount() - 1; i >= 0; i--) {
      getChildAt(i).setPressed(false);
    }
    setPressed(false);
  }
  
  private void fillToGalleryLeft()
  {
    if (mIsRtl) {
      fillToGalleryLeftRtl();
    } else {
      fillToGalleryLeftLtr();
    }
  }
  
  private void fillToGalleryLeftLtr()
  {
    int i = mSpacing;
    int j = mPaddingLeft;
    View localView = getChildAt(0);
    int k;
    int m;
    if (localView != null)
    {
      k = mFirstPosition - 1;
      m = localView.getLeft() - i;
    }
    else
    {
      k = 0;
      int n = mRight;
      m = mLeft;
      int i1 = mPaddingRight;
      mShouldStopFling = true;
      m = n - m - i1;
    }
    while ((m > j) && (k >= 0))
    {
      localView = makeAndAddView(k, k - mSelectedPosition, m, false);
      mFirstPosition = k;
      m = localView.getLeft() - i;
      k--;
    }
  }
  
  private void fillToGalleryLeftRtl()
  {
    int i = mSpacing;
    int j = mPaddingLeft;
    int k = getChildCount();
    int m = mItemCount;
    View localView = getChildAt(k - 1);
    if (localView != null)
    {
      int n = mFirstPosition;
      m = localView.getLeft();
      k = n + k;
      m -= i;
    }
    else
    {
      m = mItemCount - 1;
      k = m;
      mFirstPosition = m;
      m = mRight - mLeft - mPaddingRight;
      mShouldStopFling = true;
    }
    while ((m > j) && (k < mItemCount))
    {
      m = makeAndAddView(k, k - mSelectedPosition, m, false).getLeft() - i;
      k++;
    }
  }
  
  private void fillToGalleryRight()
  {
    if (mIsRtl) {
      fillToGalleryRightRtl();
    } else {
      fillToGalleryRightLtr();
    }
  }
  
  private void fillToGalleryRightLtr()
  {
    int i = mSpacing;
    int j = mRight;
    int k = mLeft;
    int m = mPaddingRight;
    int n = getChildCount();
    int i1 = mItemCount;
    View localView = getChildAt(n - 1);
    int i2;
    int i3;
    if (localView != null)
    {
      i2 = mFirstPosition;
      i3 = localView.getRight();
      i2 += n;
      i3 += i;
    }
    else
    {
      i3 = mItemCount - 1;
      i2 = i3;
      mFirstPosition = i3;
      i3 = mPaddingLeft;
      mShouldStopFling = true;
    }
    while ((i3 < j - k - m) && (i2 < i1))
    {
      i3 = makeAndAddView(i2, i2 - mSelectedPosition, i3, true).getRight() + i;
      i2++;
    }
  }
  
  private void fillToGalleryRightRtl()
  {
    int i = mSpacing;
    int j = mRight;
    int k = mLeft;
    int m = mPaddingRight;
    View localView = getChildAt(0);
    int n;
    int i1;
    if (localView != null)
    {
      n = mFirstPosition - 1;
      i1 = localView.getRight() + i;
    }
    else
    {
      n = 0;
      i1 = mPaddingLeft;
      mShouldStopFling = true;
    }
    while ((i1 < j - k - m) && (n >= 0))
    {
      localView = makeAndAddView(n, n - mSelectedPosition, i1, true);
      mFirstPosition = n;
      i1 = localView.getRight() + i;
      n--;
    }
  }
  
  private int getCenterOfGallery()
  {
    return (getWidth() - mPaddingLeft - mPaddingRight) / 2 + mPaddingLeft;
  }
  
  private static int getCenterOfView(View paramView)
  {
    return paramView.getLeft() + paramView.getWidth() / 2;
  }
  
  private View makeAndAddView(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    if (!mDataChanged)
    {
      localView = mRecycler.get(paramInt1);
      if (localView != null)
      {
        paramInt1 = localView.getLeft();
        mRightMost = Math.max(mRightMost, localView.getMeasuredWidth() + paramInt1);
        mLeftMost = Math.min(mLeftMost, paramInt1);
        setUpChild(localView, paramInt2, paramInt3, paramBoolean);
        return localView;
      }
    }
    View localView = mAdapter.getView(paramInt1, null, this);
    setUpChild(localView, paramInt2, paramInt3, paramBoolean);
    return localView;
  }
  
  private void offsetChildrenLeftAndRight(int paramInt)
  {
    for (int i = getChildCount() - 1; i >= 0; i--) {
      getChildAt(i).offsetLeftAndRight(paramInt);
    }
  }
  
  private void onFinishedMovement()
  {
    if (mSuppressSelectionChanged)
    {
      mSuppressSelectionChanged = false;
      super.selectionChanged();
    }
    mSelectedCenterOffset = 0;
    invalidate();
  }
  
  private void scrollIntoSlots()
  {
    if ((getChildCount() != 0) && (mSelectedChild != null))
    {
      int i = getCenterOfView(mSelectedChild);
      i = getCenterOfGallery() - i;
      if (i != 0) {
        mFlingRunnable.startUsingDistance(i);
      } else {
        onFinishedMovement();
      }
      return;
    }
  }
  
  private boolean scrollToChild(int paramInt)
  {
    View localView = getChildAt(paramInt);
    if (localView != null)
    {
      paramInt = getCenterOfGallery();
      int i = getCenterOfView(localView);
      mFlingRunnable.startUsingDistance(paramInt - i);
      return true;
    }
    return false;
  }
  
  private void setSelectionToCenterChild()
  {
    View localView = mSelectedChild;
    if (mSelectedChild == null) {
      return;
    }
    int i = getCenterOfGallery();
    if ((localView.getLeft() <= i) && (localView.getRight() >= i)) {
      return;
    }
    int j = Integer.MAX_VALUE;
    int k = 0;
    int m = getChildCount() - 1;
    int n;
    for (;;)
    {
      n = k;
      if (m < 0) {
        break;
      }
      localView = getChildAt(m);
      if ((localView.getLeft() <= i) && (localView.getRight() >= i))
      {
        n = m;
        break;
      }
      int i1 = Math.min(Math.abs(localView.getLeft() - i), Math.abs(localView.getRight() - i));
      n = j;
      if (i1 < j)
      {
        n = i1;
        k = m;
      }
      m--;
      j = n;
    }
    m = mFirstPosition + n;
    if (m != mSelectedPosition)
    {
      setSelectedPositionInt(m);
      setNextSelectedPositionInt(m);
      checkSelectionChanged();
    }
  }
  
  private void setUpChild(View paramView, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    LayoutParams localLayoutParams1 = (LayoutParams)paramView.getLayoutParams();
    LayoutParams localLayoutParams2 = localLayoutParams1;
    if (localLayoutParams1 == null) {
      localLayoutParams2 = (LayoutParams)generateDefaultLayoutParams();
    }
    boolean bool1 = mIsRtl;
    boolean bool2 = false;
    int i;
    if (paramBoolean != bool1) {
      i = -1;
    } else {
      i = 0;
    }
    addViewInLayout(paramView, i, localLayoutParams2, true);
    if (paramInt1 == 0) {
      bool2 = true;
    }
    paramView.setSelected(bool2);
    paramInt1 = ViewGroup.getChildMeasureSpec(mHeightMeasureSpec, mSpinnerPadding.top + mSpinnerPadding.bottom, height);
    paramView.measure(ViewGroup.getChildMeasureSpec(mWidthMeasureSpec, mSpinnerPadding.left + mSpinnerPadding.right, width), paramInt1);
    int j = calculateTop(paramView, true);
    int k = paramView.getMeasuredHeight();
    paramInt1 = paramView.getMeasuredWidth();
    if (paramBoolean)
    {
      paramInt1 = paramInt2 + paramInt1;
    }
    else
    {
      i = paramInt2 - paramInt1;
      paramInt1 = paramInt2;
      paramInt2 = i;
    }
    paramView.layout(paramInt2, j, paramInt1, k + j);
  }
  
  private boolean showContextMenuForChildInternal(View paramView, float paramFloat1, float paramFloat2, boolean paramBoolean)
  {
    int i = getPositionForView(paramView);
    if (i < 0) {
      return false;
    }
    return dispatchLongPress(paramView, i, mAdapter.getItemId(i), paramFloat1, paramFloat2, paramBoolean);
  }
  
  private boolean showContextMenuInternal(float paramFloat1, float paramFloat2, boolean paramBoolean)
  {
    if ((isPressed()) && (mSelectedPosition >= 0)) {
      return dispatchLongPress(getChildAt(mSelectedPosition - mFirstPosition), mSelectedPosition, mSelectedRowId, paramFloat1, paramFloat2, paramBoolean);
    }
    return false;
  }
  
  private void updateSelectedItemMetadata()
  {
    View localView1 = mSelectedChild;
    View localView2 = getChildAt(mSelectedPosition - mFirstPosition);
    mSelectedChild = localView2;
    if (localView2 == null) {
      return;
    }
    localView2.setSelected(true);
    localView2.setFocusable(true);
    if (hasFocus()) {
      localView2.requestFocus();
    }
    if ((localView1 != null) && (localView1 != localView2))
    {
      localView1.setSelected(false);
      localView1.setFocusable(false);
    }
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  protected int computeHorizontalScrollExtent()
  {
    return 1;
  }
  
  protected int computeHorizontalScrollOffset()
  {
    return mSelectedPosition;
  }
  
  protected int computeHorizontalScrollRange()
  {
    return mItemCount;
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    return paramKeyEvent.dispatch(this, null, null);
  }
  
  protected void dispatchSetPressed(boolean paramBoolean)
  {
    if (mSelectedChild != null) {
      mSelectedChild.setPressed(paramBoolean);
    }
  }
  
  public void dispatchSetSelected(boolean paramBoolean) {}
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams()
  {
    return new LayoutParams(-2, -2);
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return new LayoutParams(paramLayoutParams);
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return Gallery.class.getName();
  }
  
  protected int getChildDrawingOrder(int paramInt1, int paramInt2)
  {
    int i = mSelectedPosition - mFirstPosition;
    if (i < 0) {
      return paramInt2;
    }
    if (paramInt2 == paramInt1 - 1) {
      return i;
    }
    if (paramInt2 >= i) {
      return paramInt2 + 1;
    }
    return paramInt2;
  }
  
  int getChildHeight(View paramView)
  {
    return paramView.getMeasuredHeight();
  }
  
  protected boolean getChildStaticTransformation(View paramView, Transformation paramTransformation)
  {
    paramTransformation.clear();
    float f;
    if (paramView == mSelectedChild) {
      f = 1.0F;
    } else {
      f = mUnselectedAlpha;
    }
    paramTransformation.setAlpha(f);
    return true;
  }
  
  protected ContextMenu.ContextMenuInfo getContextMenuInfo()
  {
    return mContextMenuInfo;
  }
  
  int getLimitedMotionScrollAmount(boolean paramBoolean, int paramInt)
  {
    if (paramBoolean != mIsRtl) {
      i = mItemCount - 1;
    } else {
      i = 0;
    }
    View localView = getChildAt(i - mFirstPosition);
    if (localView == null) {
      return paramInt;
    }
    int j = getCenterOfView(localView);
    int i = getCenterOfGallery();
    if (paramBoolean)
    {
      if (j <= i) {
        return 0;
      }
    }
    else if (j >= i) {
      return 0;
    }
    i -= j;
    if (paramBoolean) {
      paramInt = Math.max(i, paramInt);
    } else {
      paramInt = Math.min(i, paramInt);
    }
    return paramInt;
  }
  
  void layout(int paramInt, boolean paramBoolean)
  {
    mIsRtl = isLayoutRtl();
    int i = mSpinnerPadding.left;
    int j = mRight;
    paramInt = mLeft;
    int k = mSpinnerPadding.left;
    int m = mSpinnerPadding.right;
    if (mDataChanged) {
      handleDataChanged();
    }
    if (mItemCount == 0)
    {
      resetList();
      return;
    }
    if (mNextSelectedPosition >= 0) {
      setSelectedPositionInt(mNextSelectedPosition);
    }
    recycleAllViews();
    detachAllViewsFromParent();
    mRightMost = 0;
    mLeftMost = 0;
    mFirstPosition = mSelectedPosition;
    View localView = makeAndAddView(mSelectedPosition, 0, 0, true);
    localView.offsetLeftAndRight((j - paramInt - k - m) / 2 + i - localView.getWidth() / 2 + mSelectedCenterOffset);
    fillToGalleryRight();
    fillToGalleryLeft();
    mRecycler.clear();
    invalidate();
    checkSelectionChanged();
    mDataChanged = false;
    mNeedSync = false;
    setNextSelectedPositionInt(mSelectedPosition);
    updateSelectedItemMetadata();
  }
  
  boolean moveDirection(int paramInt)
  {
    if (isLayoutRtl()) {
      paramInt = -paramInt;
    }
    paramInt = mSelectedPosition + paramInt;
    if ((mItemCount > 0) && (paramInt >= 0) && (paramInt < mItemCount))
    {
      scrollToChild(paramInt - mFirstPosition);
      return true;
    }
    return false;
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    if (mGestureDetector == null)
    {
      mGestureDetector = new GestureDetector(getContext(), this);
      mGestureDetector.setIsLongpressEnabled(true);
    }
  }
  
  void onCancel()
  {
    onUp();
  }
  
  public boolean onDown(MotionEvent paramMotionEvent)
  {
    mFlingRunnable.stop(false);
    mDownTouchPosition = pointToPosition((int)paramMotionEvent.getX(), (int)paramMotionEvent.getY());
    if (mDownTouchPosition >= 0)
    {
      mDownTouchView = getChildAt(mDownTouchPosition - mFirstPosition);
      mDownTouchView.setPressed(true);
    }
    mIsFirstScroll = true;
    return true;
  }
  
  public boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
  {
    if (!mShouldCallbackDuringFling)
    {
      removeCallbacks(mDisableSuppressSelectionChangedRunnable);
      if (!mSuppressSelectionChanged) {
        mSuppressSelectionChanged = true;
      }
    }
    mFlingRunnable.startUsingVelocity((int)-paramFloat1);
    return true;
  }
  
  protected void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect)
  {
    super.onFocusChanged(paramBoolean, paramInt, paramRect);
    if ((paramBoolean) && (mSelectedChild != null))
    {
      mSelectedChild.requestFocus(paramInt);
      mSelectedChild.setSelected(true);
    }
  }
  
  public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfoInternal(paramAccessibilityNodeInfo);
    boolean bool;
    if (mItemCount > 1) {
      bool = true;
    } else {
      bool = false;
    }
    paramAccessibilityNodeInfo.setScrollable(bool);
    if (isEnabled())
    {
      if ((mItemCount > 0) && (mSelectedPosition < mItemCount - 1)) {
        paramAccessibilityNodeInfo.addAction(4096);
      }
      if ((isEnabled()) && (mItemCount > 0) && (mSelectedPosition > 0)) {
        paramAccessibilityNodeInfo.addAction(8192);
      }
    }
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt != 66) {
      switch (paramInt)
      {
      default: 
        break;
      case 22: 
        if (!moveDirection(1)) {
          break;
        }
        playSoundEffect(3);
        return true;
      case 21: 
        if (!moveDirection(-1)) {
          break;
        }
        playSoundEffect(1);
        return true;
      }
    } else {
      mReceivedInvokeKeyDown = true;
    }
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    if (KeyEvent.isConfirmKey(paramInt))
    {
      if ((mReceivedInvokeKeyDown) && (mItemCount > 0))
      {
        dispatchPress(mSelectedChild);
        postDelayed(new Runnable()
        {
          public void run()
          {
            Gallery.this.dispatchUnpress();
          }
        }, ViewConfiguration.getPressedStateDuration());
        performItemClick(getChildAt(mSelectedPosition - mFirstPosition), mSelectedPosition, mAdapter.getItemId(mSelectedPosition));
      }
      mReceivedInvokeKeyDown = false;
      return true;
    }
    return super.onKeyUp(paramInt, paramKeyEvent);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    mInLayout = true;
    layout(0, false);
    mInLayout = false;
  }
  
  public void onLongPress(MotionEvent paramMotionEvent)
  {
    if (mDownTouchPosition < 0) {
      return;
    }
    performHapticFeedback(0);
    long l = getItemIdAtPosition(mDownTouchPosition);
    dispatchLongPress(mDownTouchView, mDownTouchPosition, l, paramMotionEvent.getX(), paramMotionEvent.getY(), true);
  }
  
  public boolean onScroll(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
  {
    mParent.requestDisallowInterceptTouchEvent(true);
    if (!mShouldCallbackDuringFling)
    {
      if (mIsFirstScroll)
      {
        if (!mSuppressSelectionChanged) {
          mSuppressSelectionChanged = true;
        }
        postDelayed(mDisableSuppressSelectionChangedRunnable, 250L);
      }
    }
    else if (mSuppressSelectionChanged) {
      mSuppressSelectionChanged = false;
    }
    trackMotionScroll(-1 * (int)paramFloat1);
    mIsFirstScroll = false;
    return true;
  }
  
  public void onShowPress(MotionEvent paramMotionEvent) {}
  
  public boolean onSingleTapUp(MotionEvent paramMotionEvent)
  {
    if (mDownTouchPosition >= 0)
    {
      scrollToChild(mDownTouchPosition - mFirstPosition);
      if ((mShouldCallbackOnUnselectedItemClick) || (mDownTouchPosition == mSelectedPosition)) {
        performItemClick(mDownTouchView, mDownTouchPosition, mAdapter.getItemId(mDownTouchPosition));
      }
      return true;
    }
    return false;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool = mGestureDetector.onTouchEvent(paramMotionEvent);
    int i = paramMotionEvent.getAction();
    if (i == 1) {
      onUp();
    } else if (i == 3) {
      onCancel();
    }
    return bool;
  }
  
  void onUp()
  {
    if (mFlingRunnable.mScroller.isFinished()) {
      scrollIntoSlots();
    }
    dispatchUnpress();
  }
  
  public boolean performAccessibilityActionInternal(int paramInt, Bundle paramBundle)
  {
    if (super.performAccessibilityActionInternal(paramInt, paramBundle)) {
      return true;
    }
    if (paramInt != 4096)
    {
      if (paramInt != 8192) {
        return false;
      }
      if ((isEnabled()) && (mItemCount > 0) && (mSelectedPosition > 0)) {
        return scrollToChild(mSelectedPosition - mFirstPosition - 1);
      }
      return false;
    }
    if ((isEnabled()) && (mItemCount > 0) && (mSelectedPosition < mItemCount - 1)) {
      return scrollToChild(mSelectedPosition - mFirstPosition + 1);
    }
    return false;
  }
  
  void selectionChanged()
  {
    if (!mSuppressSelectionChanged) {
      super.selectionChanged();
    }
  }
  
  public void setAnimationDuration(int paramInt)
  {
    mAnimationDuration = paramInt;
  }
  
  public void setCallbackDuringFling(boolean paramBoolean)
  {
    mShouldCallbackDuringFling = paramBoolean;
  }
  
  public void setCallbackOnUnselectedItemClick(boolean paramBoolean)
  {
    mShouldCallbackOnUnselectedItemClick = paramBoolean;
  }
  
  public void setGravity(int paramInt)
  {
    if (mGravity != paramInt)
    {
      mGravity = paramInt;
      requestLayout();
    }
  }
  
  void setSelectedPositionInt(int paramInt)
  {
    super.setSelectedPositionInt(paramInt);
    updateSelectedItemMetadata();
  }
  
  public void setSpacing(int paramInt)
  {
    mSpacing = paramInt;
  }
  
  public void setUnselectedAlpha(float paramFloat)
  {
    mUnselectedAlpha = paramFloat;
  }
  
  public boolean showContextMenu()
  {
    return showContextMenuInternal(0.0F, 0.0F, false);
  }
  
  public boolean showContextMenu(float paramFloat1, float paramFloat2)
  {
    return showContextMenuInternal(paramFloat1, paramFloat2, true);
  }
  
  public boolean showContextMenuForChild(View paramView)
  {
    if (isShowingContextMenuWithCoords()) {
      return false;
    }
    return showContextMenuForChildInternal(paramView, 0.0F, 0.0F, false);
  }
  
  public boolean showContextMenuForChild(View paramView, float paramFloat1, float paramFloat2)
  {
    return showContextMenuForChildInternal(paramView, paramFloat1, paramFloat2, true);
  }
  
  void trackMotionScroll(int paramInt)
  {
    if (getChildCount() == 0) {
      return;
    }
    boolean bool;
    if (paramInt < 0) {
      bool = true;
    } else {
      bool = false;
    }
    int i = getLimitedMotionScrollAmount(bool, paramInt);
    if (i != paramInt)
    {
      mFlingRunnable.endFling(false);
      onFinishedMovement();
    }
    offsetChildrenLeftAndRight(i);
    detachOffScreenChildren(bool);
    if (bool) {
      fillToGalleryRight();
    } else {
      fillToGalleryLeft();
    }
    mRecycler.clear();
    setSelectionToCenterChild();
    View localView = mSelectedChild;
    if (localView != null) {
      mSelectedCenterOffset = (localView.getLeft() + localView.getWidth() / 2 - getWidth() / 2);
    }
    onScrollChanged(0, 0, 0, 0);
    invalidate();
  }
  
  private class FlingRunnable
    implements Runnable
  {
    private int mLastFlingX;
    private Scroller mScroller = new Scroller(getContext());
    
    public FlingRunnable() {}
    
    private void endFling(boolean paramBoolean)
    {
      mScroller.forceFinished(true);
      if (paramBoolean) {
        Gallery.this.scrollIntoSlots();
      }
    }
    
    private void startCommon()
    {
      removeCallbacks(this);
    }
    
    public void run()
    {
      if (mItemCount == 0)
      {
        endFling(true);
        return;
      }
      Gallery.access$602(Gallery.this, false);
      Object localObject = mScroller;
      boolean bool = ((Scroller)localObject).computeScrollOffset();
      int i = ((Scroller)localObject).getCurrX();
      int j = mLastFlingX - i;
      int k;
      if (j > 0)
      {
        localObject = Gallery.this;
        if (mIsRtl) {
          k = mFirstPosition + getChildCount() - 1;
        } else {
          k = mFirstPosition;
        }
        Gallery.access$702((Gallery)localObject, k);
        k = Math.min(getWidth() - mPaddingLeft - mPaddingRight - 1, j);
      }
      else
      {
        getChildCount();
        localObject = Gallery.this;
        if (mIsRtl) {
          k = mFirstPosition;
        } else {
          k = mFirstPosition + getChildCount() - 1;
        }
        Gallery.access$702((Gallery)localObject, k);
        k = Math.max(-(getWidth() - mPaddingRight - mPaddingLeft - 1), j);
      }
      trackMotionScroll(k);
      if ((bool) && (!mShouldStopFling))
      {
        mLastFlingX = i;
        post(this);
      }
      else
      {
        endFling(true);
      }
    }
    
    public void startUsingDistance(int paramInt)
    {
      if (paramInt == 0) {
        return;
      }
      startCommon();
      mLastFlingX = 0;
      mScroller.startScroll(0, 0, -paramInt, 0, mAnimationDuration);
      post(this);
    }
    
    public void startUsingVelocity(int paramInt)
    {
      if (paramInt == 0) {
        return;
      }
      startCommon();
      int i;
      if (paramInt < 0) {
        i = Integer.MAX_VALUE;
      } else {
        i = 0;
      }
      mLastFlingX = i;
      mScroller.fling(i, 0, paramInt, 0, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
      post(this);
    }
    
    public void stop(boolean paramBoolean)
    {
      removeCallbacks(this);
      endFling(paramBoolean);
    }
  }
  
  public static class LayoutParams
    extends ViewGroup.LayoutParams
  {
    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
    }
    
    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
    }
    
    public LayoutParams(ViewGroup.LayoutParams paramLayoutParams)
    {
      super();
    }
  }
}
