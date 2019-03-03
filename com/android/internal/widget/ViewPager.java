package com.android.internal.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.MathUtils;
import android.view.AbsSavedState;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.animation.Interpolator;
import android.widget.EdgeEffect;
import android.widget.Scroller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ViewPager
  extends ViewGroup
{
  private static final int CLOSE_ENOUGH = 2;
  private static final Comparator<ItemInfo> COMPARATOR = new Comparator()
  {
    public int compare(ViewPager.ItemInfo paramAnonymousItemInfo1, ViewPager.ItemInfo paramAnonymousItemInfo2)
    {
      return position - position;
    }
  };
  private static final boolean DEBUG = false;
  private static final int DEFAULT_GUTTER_SIZE = 16;
  private static final int DEFAULT_OFFSCREEN_PAGES = 1;
  private static final int DRAW_ORDER_DEFAULT = 0;
  private static final int DRAW_ORDER_FORWARD = 1;
  private static final int DRAW_ORDER_REVERSE = 2;
  private static final int INVALID_POINTER = -1;
  private static final int[] LAYOUT_ATTRS = { 16842931 };
  private static final int MAX_SCROLL_X = 16777216;
  private static final int MAX_SETTLE_DURATION = 600;
  private static final int MIN_DISTANCE_FOR_FLING = 25;
  private static final int MIN_FLING_VELOCITY = 400;
  public static final int SCROLL_STATE_DRAGGING = 1;
  public static final int SCROLL_STATE_IDLE = 0;
  public static final int SCROLL_STATE_SETTLING = 2;
  private static final String TAG = "ViewPager";
  private static final boolean USE_CACHE = false;
  private static final Interpolator sInterpolator = new Interpolator()
  {
    public float getInterpolation(float paramAnonymousFloat)
    {
      paramAnonymousFloat -= 1.0F;
      return paramAnonymousFloat * paramAnonymousFloat * paramAnonymousFloat * paramAnonymousFloat * paramAnonymousFloat + 1.0F;
    }
  };
  private static final ViewPositionComparator sPositionComparator = new ViewPositionComparator();
  private int mActivePointerId = -1;
  private PagerAdapter mAdapter;
  private OnAdapterChangeListener mAdapterChangeListener;
  private int mBottomPageBounds;
  private boolean mCalledSuper;
  private int mChildHeightMeasureSpec;
  private int mChildWidthMeasureSpec;
  private final int mCloseEnough;
  private int mCurItem;
  private int mDecorChildCount;
  private final int mDefaultGutterSize;
  private int mDrawingOrder;
  private ArrayList<View> mDrawingOrderedChildren;
  private final Runnable mEndScrollRunnable = new Runnable()
  {
    public void run()
    {
      ViewPager.this.setScrollState(0);
      populate();
    }
  };
  private int mExpectedAdapterCount;
  private boolean mFirstLayout = true;
  private float mFirstOffset = -3.4028235E38F;
  private final int mFlingDistance;
  private int mGutterSize;
  private boolean mInLayout;
  private float mInitialMotionX;
  private float mInitialMotionY;
  private OnPageChangeListener mInternalPageChangeListener;
  private boolean mIsBeingDragged;
  private boolean mIsUnableToDrag;
  private final ArrayList<ItemInfo> mItems = new ArrayList();
  private float mLastMotionX;
  private float mLastMotionY;
  private float mLastOffset = Float.MAX_VALUE;
  private final EdgeEffect mLeftEdge;
  private int mLeftIncr = -1;
  private Drawable mMarginDrawable;
  private final int mMaximumVelocity;
  private final int mMinimumVelocity;
  private PagerObserver mObserver;
  private int mOffscreenPageLimit = 1;
  private OnPageChangeListener mOnPageChangeListener;
  private int mPageMargin;
  private PageTransformer mPageTransformer;
  private boolean mPopulatePending;
  private Parcelable mRestoredAdapterState = null;
  private ClassLoader mRestoredClassLoader = null;
  private int mRestoredCurItem = -1;
  private final EdgeEffect mRightEdge;
  private int mScrollState = 0;
  private final Scroller mScroller;
  private boolean mScrollingCacheEnabled;
  private final ItemInfo mTempItem = new ItemInfo();
  private final Rect mTempRect = new Rect();
  private int mTopPageBounds;
  private final int mTouchSlop;
  private VelocityTracker mVelocityTracker;
  
  public ViewPager(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public ViewPager(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public ViewPager(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public ViewPager(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    setWillNotDraw(false);
    setDescendantFocusability(262144);
    setFocusable(true);
    mScroller = new Scroller(paramContext, sInterpolator);
    paramAttributeSet = ViewConfiguration.get(paramContext);
    float f = getResourcesgetDisplayMetricsdensity;
    mTouchSlop = paramAttributeSet.getScaledPagingTouchSlop();
    mMinimumVelocity = ((int)(400.0F * f));
    mMaximumVelocity = paramAttributeSet.getScaledMaximumFlingVelocity();
    mLeftEdge = new EdgeEffect(paramContext);
    mRightEdge = new EdgeEffect(paramContext);
    mFlingDistance = ((int)(25.0F * f));
    mCloseEnough = ((int)(2.0F * f));
    mDefaultGutterSize = ((int)(16.0F * f));
    if (getImportantForAccessibility() == 0) {
      setImportantForAccessibility(1);
    }
  }
  
  private void calculatePageOffsets(ItemInfo paramItemInfo1, int paramInt, ItemInfo paramItemInfo2)
  {
    int i = mAdapter.getCount();
    int j = getPaddedWidth();
    float f1;
    if (j > 0) {
      f1 = mPageMargin / j;
    } else {
      f1 = 0.0F;
    }
    if (paramItemInfo2 != null)
    {
      j = position;
      if (j < position)
      {
        k = 0;
        f2 = offset + widthFactor + f1;
        j++;
        while ((j <= position) && (k < mItems.size()))
        {
          for (paramItemInfo2 = (ItemInfo)mItems.get(k);; paramItemInfo2 = (ItemInfo)mItems.get(k))
          {
            f3 = f2;
            m = j;
            if (j <= position) {
              break;
            }
            f3 = f2;
            m = j;
            if (k >= mItems.size() - 1) {
              break;
            }
            k++;
          }
          while (m < position)
          {
            f3 += mAdapter.getPageWidth(m) + f1;
            m++;
          }
          offset = f3;
          f2 = f3 + (widthFactor + f1);
          j = m + 1;
        }
      }
      else if (j > position)
      {
        k = mItems.size() - 1;
        f2 = offset;
        j--;
        while ((j >= position) && (k >= 0))
        {
          for (paramItemInfo2 = (ItemInfo)mItems.get(k);; paramItemInfo2 = (ItemInfo)mItems.get(k))
          {
            f3 = f2;
            m = j;
            if (j >= position) {
              break;
            }
            f3 = f2;
            m = j;
            if (k <= 0) {
              break;
            }
            k--;
          }
          while (m > position)
          {
            f3 -= mAdapter.getPageWidth(m) + f1;
            m--;
          }
          f2 = f3 - (widthFactor + f1);
          offset = f2;
          j = m - 1;
        }
      }
    }
    int m = mItems.size();
    float f3 = offset;
    j = position - 1;
    if (position == 0) {
      f2 = offset;
    } else {
      f2 = -3.4028235E38F;
    }
    mFirstOffset = f2;
    if (position == i - 1) {
      f2 = offset + widthFactor - 1.0F;
    } else {
      f2 = Float.MAX_VALUE;
    }
    mLastOffset = f2;
    int k = paramInt - 1;
    float f2 = f3;
    while (k >= 0)
    {
      paramItemInfo2 = (ItemInfo)mItems.get(k);
      while (j > position)
      {
        f2 -= mAdapter.getPageWidth(j) + f1;
        j--;
      }
      f2 -= widthFactor + f1;
      offset = f2;
      if (position == 0) {
        mFirstOffset = f2;
      }
      k--;
      j--;
    }
    f2 = offset + widthFactor + f1;
    k = position + 1;
    j = paramInt + 1;
    for (paramInt = k; j < m; paramInt++)
    {
      paramItemInfo1 = (ItemInfo)mItems.get(j);
      while (paramInt < position)
      {
        f2 += mAdapter.getPageWidth(paramInt) + f1;
        paramInt++;
      }
      if (position == i - 1) {
        mLastOffset = (widthFactor + f2 - 1.0F);
      }
      offset = f2;
      f2 += widthFactor + f1;
      j++;
    }
  }
  
  private boolean canScroll()
  {
    PagerAdapter localPagerAdapter = mAdapter;
    boolean bool = true;
    if ((localPagerAdapter == null) || (mAdapter.getCount() <= 1)) {
      bool = false;
    }
    return bool;
  }
  
  private void completeScroll(boolean paramBoolean)
  {
    if (mScrollState == 2) {
      i = 1;
    } else {
      i = 0;
    }
    if (i != 0)
    {
      setScrollingCacheEnabled(false);
      mScroller.abortAnimation();
      j = getScrollX();
      int k = getScrollY();
      m = mScroller.getCurrX();
      int n = mScroller.getCurrY();
      if ((j != m) || (k != n)) {
        scrollTo(m, n);
      }
    }
    mPopulatePending = false;
    int m = 0;
    int j = i;
    for (int i = m; i < mItems.size(); i++)
    {
      ItemInfo localItemInfo = (ItemInfo)mItems.get(i);
      if (scrolling)
      {
        j = 1;
        scrolling = false;
      }
    }
    if (j != 0) {
      if (paramBoolean) {
        postOnAnimation(mEndScrollRunnable);
      } else {
        mEndScrollRunnable.run();
      }
    }
  }
  
  private int determineTargetPage(int paramInt1, float paramFloat, int paramInt2, int paramInt3)
  {
    if ((Math.abs(paramInt3) > mFlingDistance) && (Math.abs(paramInt2) > mMinimumVelocity))
    {
      if (paramInt2 < 0) {
        paramInt2 = mLeftIncr;
      } else {
        paramInt2 = 0;
      }
      paramInt1 -= paramInt2;
    }
    else
    {
      float f;
      if (paramInt1 >= mCurItem) {
        f = 0.4F;
      } else {
        f = 0.6F;
      }
      paramInt1 = (int)(paramInt1 - mLeftIncr * (paramFloat + f));
    }
    paramInt2 = paramInt1;
    if (mItems.size() > 0)
    {
      ItemInfo localItemInfo1 = (ItemInfo)mItems.get(0);
      ItemInfo localItemInfo2 = (ItemInfo)mItems.get(mItems.size() - 1);
      paramInt2 = MathUtils.constrain(paramInt1, position, position);
    }
    return paramInt2;
  }
  
  private void enableLayers(boolean paramBoolean)
  {
    int i = getChildCount();
    for (int j = 0; j < i; j++)
    {
      int k;
      if (paramBoolean) {
        k = 2;
      } else {
        k = 0;
      }
      getChildAt(j).setLayerType(k, null);
    }
  }
  
  private void endDrag()
  {
    mIsBeingDragged = false;
    mIsUnableToDrag = false;
    if (mVelocityTracker != null)
    {
      mVelocityTracker.recycle();
      mVelocityTracker = null;
    }
  }
  
  private Rect getChildRectInPagerCoordinates(Rect paramRect, View paramView)
  {
    Rect localRect = paramRect;
    if (paramRect == null) {
      localRect = new Rect();
    }
    if (paramView == null)
    {
      localRect.set(0, 0, 0, 0);
      return localRect;
    }
    left = paramView.getLeft();
    right = paramView.getRight();
    top = paramView.getTop();
    bottom = paramView.getBottom();
    for (paramRect = paramView.getParent(); ((paramRect instanceof ViewGroup)) && (paramRect != this); paramRect = paramRect.getParent())
    {
      paramRect = (ViewGroup)paramRect;
      left += paramRect.getLeft();
      right += paramRect.getRight();
      top += paramRect.getTop();
      bottom += paramRect.getBottom();
    }
    return localRect;
  }
  
  private int getLeftEdgeForItem(int paramInt)
  {
    ItemInfo localItemInfo = infoForPosition(paramInt);
    if (localItemInfo == null) {
      return 0;
    }
    int i = getPaddedWidth();
    paramInt = (int)(i * MathUtils.constrain(offset, mFirstOffset, mLastOffset));
    if (isLayoutRtl()) {
      return 16777216 - (int)(i * widthFactor + 0.5F) - paramInt;
    }
    return paramInt;
  }
  
  private int getPaddedWidth()
  {
    return getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
  }
  
  private int getScrollStart()
  {
    if (isLayoutRtl()) {
      return 16777216 - getScrollX();
    }
    return getScrollX();
  }
  
  private ItemInfo infoForFirstVisiblePage()
  {
    int i = getScrollStart();
    int j = getPaddedWidth();
    float f1 = 0.0F;
    float f2;
    if (j > 0) {
      f2 = i / j;
    } else {
      f2 = 0.0F;
    }
    if (j > 0) {
      f1 = mPageMargin / j;
    }
    int k = -1;
    float f3 = 0.0F;
    float f4 = 0.0F;
    int m = 1;
    Object localObject = null;
    int n = mItems.size();
    j = 0;
    while (j < n)
    {
      ItemInfo localItemInfo1 = (ItemInfo)mItems.get(j);
      int i1 = j;
      ItemInfo localItemInfo2 = localItemInfo1;
      if (m == 0)
      {
        i1 = j;
        localItemInfo2 = localItemInfo1;
        if (position != k + 1)
        {
          localItemInfo2 = mTempItem;
          offset = (f3 + f4 + f1);
          position = (k + 1);
          widthFactor = mAdapter.getPageWidth(position);
          i1 = j - 1;
        }
      }
      f3 = offset;
      if ((m == 0) && (f2 < f3)) {
        return localObject;
      }
      if ((f2 >= widthFactor + f3 + f1) && (i1 != mItems.size() - 1))
      {
        m = 0;
        k = position;
        f4 = widthFactor;
        j = i1 + 1;
        localObject = localItemInfo2;
      }
      else
      {
        return localItemInfo2;
      }
    }
    return localObject;
  }
  
  private boolean isGutterDrag(float paramFloat1, float paramFloat2)
  {
    boolean bool;
    if (((paramFloat1 < mGutterSize) && (paramFloat2 > 0.0F)) || ((paramFloat1 > getWidth() - mGutterSize) && (paramFloat2 < 0.0F))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
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
      mLastMotionX = paramMotionEvent.getX(i);
      mActivePointerId = paramMotionEvent.getPointerId(i);
      if (mVelocityTracker != null) {
        mVelocityTracker.clear();
      }
    }
  }
  
  private boolean pageScrolled(int paramInt)
  {
    if (mItems.size() == 0)
    {
      mCalledSuper = false;
      onPageScrolled(0, 0.0F, 0);
      if (mCalledSuper) {
        return false;
      }
      throw new IllegalStateException("onPageScrolled did not call superclass implementation");
    }
    if (isLayoutRtl()) {
      paramInt = 16777216 - paramInt;
    }
    ItemInfo localItemInfo = infoForFirstVisiblePage();
    int i = getPaddedWidth();
    int j = mPageMargin;
    float f = mPageMargin / i;
    int k = position;
    f = (paramInt / i - offset) / (widthFactor + f);
    paramInt = (int)((j + i) * f);
    mCalledSuper = false;
    onPageScrolled(k, f, paramInt);
    if (mCalledSuper) {
      return true;
    }
    throw new IllegalStateException("onPageScrolled did not call superclass implementation");
  }
  
  private boolean performDrag(float paramFloat)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    int i = getPaddedWidth();
    float f1 = mLastMotionX;
    mLastMotionX = paramFloat;
    EdgeEffect localEdgeEffect1;
    EdgeEffect localEdgeEffect2;
    if (isLayoutRtl())
    {
      localEdgeEffect1 = mRightEdge;
      localEdgeEffect2 = mLeftEdge;
    }
    else
    {
      localEdgeEffect1 = mLeftEdge;
      localEdgeEffect2 = mRightEdge;
    }
    paramFloat = getScrollX() + (f1 - paramFloat);
    if (isLayoutRtl()) {
      paramFloat = 1.6777216E7F - paramFloat;
    }
    ItemInfo localItemInfo = (ItemInfo)mItems.get(0);
    int j = position;
    int k = 1;
    if (j == 0) {
      j = 1;
    } else {
      j = 0;
    }
    if (j != 0) {
      f1 = offset * i;
    } else {
      f1 = i * mFirstOffset;
    }
    localItemInfo = (ItemInfo)mItems.get(mItems.size() - 1);
    if (position != mAdapter.getCount() - 1) {
      k = 0;
    }
    float f2;
    if (k != 0) {
      f2 = offset * i;
    } else {
      f2 = i * mLastOffset;
    }
    if (paramFloat < f1)
    {
      if (j != 0)
      {
        localEdgeEffect1.onPull(Math.abs(f1 - paramFloat) / i);
        bool3 = true;
      }
      paramFloat = f1;
    }
    for (;;)
    {
      break;
      if (paramFloat > f2)
      {
        bool3 = bool1;
        if (k != 0)
        {
          localEdgeEffect2.onPull(Math.abs(paramFloat - f2) / i);
          bool3 = true;
        }
        paramFloat = f2;
      }
      else
      {
        bool3 = bool2;
      }
    }
    if (isLayoutRtl()) {
      paramFloat = 1.6777216E7F - paramFloat;
    }
    mLastMotionX += paramFloat - (int)paramFloat;
    scrollTo((int)paramFloat, getScrollY());
    pageScrolled((int)paramFloat);
    return bool3;
  }
  
  private void recomputeScrollPosition(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    float f;
    ItemInfo localItemInfo;
    if ((paramInt2 > 0) && (!mItems.isEmpty()))
    {
      int i = getPaddingLeft();
      int j = getPaddingRight();
      int k = getPaddingLeft();
      int m = getPaddingRight();
      f = getScrollX() / (paramInt2 - k - m + paramInt4);
      paramInt4 = (int)((paramInt1 - i - j + paramInt3) * f);
      scrollTo(paramInt4, getScrollY());
      if (!mScroller.isFinished())
      {
        paramInt2 = mScroller.getDuration();
        paramInt3 = mScroller.timePassed();
        localItemInfo = infoForPosition(mCurItem);
        mScroller.startScroll(paramInt4, 0, (int)(offset * paramInt1), 0, paramInt2 - paramInt3);
      }
    }
    else
    {
      localItemInfo = infoForPosition(mCurItem);
      if (localItemInfo != null) {
        f = Math.min(offset, mLastOffset);
      } else {
        f = 0.0F;
      }
      paramInt1 = (int)((paramInt1 - getPaddingLeft() - getPaddingRight()) * f);
      if (paramInt1 != getScrollX())
      {
        completeScroll(false);
        scrollTo(paramInt1, getScrollY());
      }
    }
  }
  
  private void removeNonDecorViews()
  {
    int j;
    for (int i = 0; i < getChildCount(); i = j + 1)
    {
      j = i;
      if (!getChildAtgetLayoutParamsisDecor)
      {
        removeViewAt(i);
        j = i - 1;
      }
    }
  }
  
  private void requestParentDisallowInterceptTouchEvent(boolean paramBoolean)
  {
    ViewParent localViewParent = getParent();
    if (localViewParent != null) {
      localViewParent.requestDisallowInterceptTouchEvent(paramBoolean);
    }
  }
  
  private void scrollToItem(int paramInt1, boolean paramBoolean1, int paramInt2, boolean paramBoolean2)
  {
    int i = getLeftEdgeForItem(paramInt1);
    if (paramBoolean1)
    {
      smoothScrollTo(i, 0, paramInt2);
      if ((paramBoolean2) && (mOnPageChangeListener != null)) {
        mOnPageChangeListener.onPageSelected(paramInt1);
      }
      if ((paramBoolean2) && (mInternalPageChangeListener != null)) {
        mInternalPageChangeListener.onPageSelected(paramInt1);
      }
    }
    else
    {
      if ((paramBoolean2) && (mOnPageChangeListener != null)) {
        mOnPageChangeListener.onPageSelected(paramInt1);
      }
      if ((paramBoolean2) && (mInternalPageChangeListener != null)) {
        mInternalPageChangeListener.onPageSelected(paramInt1);
      }
      completeScroll(false);
      scrollTo(i, 0);
      pageScrolled(i);
    }
  }
  
  private void setScrollState(int paramInt)
  {
    if (mScrollState == paramInt) {
      return;
    }
    mScrollState = paramInt;
    if (mPageTransformer != null)
    {
      boolean bool;
      if (paramInt != 0) {
        bool = true;
      } else {
        bool = false;
      }
      enableLayers(bool);
    }
    if (mOnPageChangeListener != null) {
      mOnPageChangeListener.onPageScrollStateChanged(paramInt);
    }
  }
  
  private void setScrollingCacheEnabled(boolean paramBoolean)
  {
    if (mScrollingCacheEnabled != paramBoolean) {
      mScrollingCacheEnabled = paramBoolean;
    }
  }
  
  private void sortChildDrawingOrder()
  {
    if (mDrawingOrder != 0)
    {
      if (mDrawingOrderedChildren == null) {
        mDrawingOrderedChildren = new ArrayList();
      } else {
        mDrawingOrderedChildren.clear();
      }
      int i = getChildCount();
      for (int j = 0; j < i; j++)
      {
        View localView = getChildAt(j);
        mDrawingOrderedChildren.add(localView);
      }
      Collections.sort(mDrawingOrderedChildren, sPositionComparator);
    }
  }
  
  public void addFocusables(ArrayList<View> paramArrayList, int paramInt1, int paramInt2)
  {
    int i = paramArrayList.size();
    int j = getDescendantFocusability();
    if (j != 393216) {
      for (int k = 0; k < getChildCount(); k++)
      {
        View localView = getChildAt(k);
        if (localView.getVisibility() == 0)
        {
          ItemInfo localItemInfo = infoForChild(localView);
          if ((localItemInfo != null) && (position == mCurItem)) {
            localView.addFocusables(paramArrayList, paramInt1, paramInt2);
          }
        }
      }
    }
    if ((j != 262144) || (i == paramArrayList.size()))
    {
      if (!isFocusable()) {
        return;
      }
      if (((paramInt2 & 0x1) == 1) && (isInTouchMode()) && (!isFocusableInTouchMode())) {
        return;
      }
      if (paramArrayList != null) {
        paramArrayList.add(this);
      }
    }
  }
  
  ItemInfo addNewItem(int paramInt1, int paramInt2)
  {
    ItemInfo localItemInfo = new ItemInfo();
    position = paramInt1;
    object = mAdapter.instantiateItem(this, paramInt1);
    widthFactor = mAdapter.getPageWidth(paramInt1);
    if ((paramInt2 >= 0) && (paramInt2 < mItems.size())) {
      mItems.add(paramInt2, localItemInfo);
    } else {
      mItems.add(localItemInfo);
    }
    return localItemInfo;
  }
  
  public void addTouchables(ArrayList<View> paramArrayList)
  {
    for (int i = 0; i < getChildCount(); i++)
    {
      View localView = getChildAt(i);
      if (localView.getVisibility() == 0)
      {
        ItemInfo localItemInfo = infoForChild(localView);
        if ((localItemInfo != null) && (position == mCurItem)) {
          localView.addTouchables(paramArrayList);
        }
      }
    }
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams)
  {
    ViewGroup.LayoutParams localLayoutParams = paramLayoutParams;
    if (!checkLayoutParams(paramLayoutParams)) {
      localLayoutParams = generateLayoutParams(paramLayoutParams);
    }
    paramLayoutParams = (LayoutParams)localLayoutParams;
    isDecor |= paramView instanceof Decor;
    if (mInLayout)
    {
      if ((paramLayoutParams != null) && (isDecor)) {
        throw new IllegalStateException("Cannot add pager decor view during layout");
      }
      needsMeasure = true;
      addViewInLayout(paramView, paramInt, localLayoutParams);
    }
    else
    {
      super.addView(paramView, paramInt, localLayoutParams);
    }
  }
  
  public boolean arrowScroll(int paramInt)
  {
    View localView = findFocus();
    Object localObject;
    int i;
    int j;
    if (localView == this)
    {
      localObject = null;
    }
    else
    {
      localObject = localView;
      if (localView != null)
      {
        i = 0;
        for (localObject = localView.getParent();; localObject = ((ViewParent)localObject).getParent())
        {
          j = i;
          if (!(localObject instanceof ViewGroup)) {
            break;
          }
          if (localObject == this)
          {
            j = 1;
            break;
          }
        }
        localObject = localView;
        if (j == 0)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append(localView.getClass().getSimpleName());
          for (localObject = localView.getParent(); (localObject instanceof ViewGroup); localObject = ((ViewParent)localObject).getParent())
          {
            localStringBuilder.append(" => ");
            localStringBuilder.append(localObject.getClass().getSimpleName());
          }
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("arrowScroll tried to find focus based on non-child current focused view ");
          ((StringBuilder)localObject).append(localStringBuilder.toString());
          Log.e("ViewPager", ((StringBuilder)localObject).toString());
          localObject = null;
        }
      }
    }
    boolean bool = false;
    localView = FocusFinder.getInstance().findNextFocus(this, (View)localObject, paramInt);
    if ((localView != null) && (localView != localObject))
    {
      if (paramInt == 17)
      {
        i = getChildRectInPagerCoordinatesmTempRect, localView).left;
        j = getChildRectInPagerCoordinatesmTempRect, (View)localObject).left;
        if ((localObject != null) && (i >= j)) {
          bool = pageLeft();
        } else {
          bool = localView.requestFocus();
        }
      }
      else if (paramInt == 66)
      {
        j = getChildRectInPagerCoordinatesmTempRect, localView).left;
        i = getChildRectInPagerCoordinatesmTempRect, (View)localObject).left;
        if ((localObject != null) && (j <= i)) {
          bool = pageRight();
        } else {
          bool = localView.requestFocus();
        }
      }
    }
    else if ((paramInt != 17) && (paramInt != 1))
    {
      if ((paramInt == 66) || (paramInt == 2)) {
        bool = pageRight();
      }
    }
    else {
      bool = pageLeft();
    }
    if (bool) {
      playSoundEffect(SoundEffectConstants.getContantForFocusDirection(paramInt));
    }
    return bool;
  }
  
  protected boolean canScroll(View paramView, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3)
  {
    boolean bool1 = paramView instanceof ViewGroup;
    boolean bool2 = true;
    if (bool1)
    {
      ViewGroup localViewGroup = (ViewGroup)paramView;
      int i = paramView.getScrollX();
      int j = paramView.getScrollY();
      for (int k = localViewGroup.getChildCount() - 1; k >= 0; k--)
      {
        View localView = localViewGroup.getChildAt(k);
        if ((paramInt2 + i >= localView.getLeft()) && (paramInt2 + i < localView.getRight()) && (paramInt3 + j >= localView.getTop()) && (paramInt3 + j < localView.getBottom()) && (canScroll(localView, true, paramInt1, paramInt2 + i - localView.getLeft(), paramInt3 + j - localView.getTop()))) {
          return true;
        }
      }
    }
    if ((paramBoolean) && (paramView.canScrollHorizontally(-paramInt1))) {
      paramBoolean = bool2;
    } else {
      paramBoolean = false;
    }
    return paramBoolean;
  }
  
  public boolean canScrollHorizontally(int paramInt)
  {
    PagerAdapter localPagerAdapter = mAdapter;
    boolean bool1 = false;
    boolean bool2 = false;
    if (localPagerAdapter == null) {
      return false;
    }
    int i = getPaddedWidth();
    int j = getScrollX();
    if (paramInt < 0)
    {
      if (j > (int)(i * mFirstOffset)) {
        bool2 = true;
      }
      return bool2;
    }
    if (paramInt > 0)
    {
      bool2 = bool1;
      if (j < (int)(i * mLastOffset)) {
        bool2 = true;
      }
      return bool2;
    }
    return false;
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    boolean bool;
    if (((paramLayoutParams instanceof LayoutParams)) && (super.checkLayoutParams(paramLayoutParams))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void computeScroll()
  {
    if ((!mScroller.isFinished()) && (mScroller.computeScrollOffset()))
    {
      int i = getScrollX();
      int j = getScrollY();
      int k = mScroller.getCurrX();
      int m = mScroller.getCurrY();
      if ((i != k) || (j != m))
      {
        scrollTo(k, m);
        if (!pageScrolled(k))
        {
          mScroller.abortAnimation();
          scrollTo(0, m);
        }
      }
      postInvalidateOnAnimation();
      return;
    }
    completeScroll(true);
  }
  
  void dataSetChanged()
  {
    int i = mAdapter.getCount();
    mExpectedAdapterCount = i;
    int j;
    if ((mItems.size() < mOffscreenPageLimit * 2 + 1) && (mItems.size() < i)) {
      j = 1;
    } else {
      j = 0;
    }
    int k = mCurItem;
    int m = 0;
    int n = 0;
    Object localObject;
    while (n < mItems.size())
    {
      localObject = (ItemInfo)mItems.get(n);
      int i1 = mAdapter.getItemPosition(object);
      int i2;
      int i3;
      int i4;
      if (i1 == -1)
      {
        i2 = n;
        i3 = m;
        i4 = k;
      }
      else if (i1 == -2)
      {
        mItems.remove(n);
        i1 = n - 1;
        n = m;
        if (m == 0)
        {
          mAdapter.startUpdate(this);
          n = 1;
        }
        mAdapter.destroyItem(this, position, object);
        j = 1;
        i2 = i1;
        i3 = n;
        i4 = k;
        if (mCurItem == position)
        {
          i4 = Math.max(0, Math.min(mCurItem, i - 1));
          j = 1;
          i2 = i1;
          i3 = n;
        }
      }
      else
      {
        i2 = n;
        i3 = m;
        i4 = k;
        if (position != i1)
        {
          if (position == mCurItem) {
            k = i1;
          }
          position = i1;
          j = 1;
          i4 = k;
          i3 = m;
          i2 = n;
        }
      }
      n = i2 + 1;
      m = i3;
      k = i4;
    }
    if (m != 0) {
      mAdapter.finishUpdate(this);
    }
    Collections.sort(mItems, COMPARATOR);
    if (j != 0)
    {
      m = getChildCount();
      for (j = 0; j < m; j++)
      {
        localObject = (LayoutParams)getChildAt(j).getLayoutParams();
        if (!isDecor) {
          widthFactor = 0.0F;
        }
      }
      setCurrentItemInternal(k, false, true);
      requestLayout();
    }
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
  
  float distanceInfluenceForSnapDuration(float paramFloat)
  {
    return (float)Math.sin((float)((paramFloat - 0.5F) * 0.4712389167638204D));
  }
  
  public void draw(Canvas paramCanvas)
  {
    super.draw(paramCanvas);
    int i = 0;
    int j = 0;
    int m = getOverScrollMode();
    boolean bool;
    if ((m != 0) && ((m != 1) || (mAdapter == null) || (mAdapter.getCount() <= 1)))
    {
      mLeftEdge.finish();
      mRightEdge.finish();
    }
    else
    {
      int k;
      if (!mLeftEdge.isFinished())
      {
        i = paramCanvas.save();
        m = getHeight() - getPaddingTop() - getPaddingBottom();
        j = getWidth();
        paramCanvas.rotate(270.0F);
        paramCanvas.translate(-m + getPaddingTop(), mFirstOffset * j);
        mLeftEdge.setSize(m, j);
        k = false | mLeftEdge.draw(paramCanvas);
        paramCanvas.restoreToCount(i);
      }
      i = k;
      if (!mRightEdge.isFinished())
      {
        m = paramCanvas.save();
        int n = getWidth();
        int i1 = getHeight();
        int i2 = getPaddingTop();
        i = getPaddingBottom();
        paramCanvas.rotate(90.0F);
        paramCanvas.translate(-getPaddingTop(), -(mLastOffset + 1.0F) * n);
        mRightEdge.setSize(i1 - i2 - i, n);
        bool = k | mRightEdge.draw(paramCanvas);
        paramCanvas.restoreToCount(m);
      }
    }
    if (bool) {
      postInvalidateOnAnimation();
    }
  }
  
  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    Drawable localDrawable = mMarginDrawable;
    if ((localDrawable != null) && (localDrawable.isStateful()) && (localDrawable.setState(getDrawableState()))) {
      invalidateDrawable(localDrawable);
    }
  }
  
  public boolean executeKeyEvent(KeyEvent paramKeyEvent)
  {
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (paramKeyEvent.getAction() == 0)
    {
      int i = paramKeyEvent.getKeyCode();
      if (i != 61)
      {
        switch (i)
        {
        default: 
          bool2 = bool1;
          break;
        case 22: 
          bool2 = arrowScroll(66);
          break;
        case 21: 
          bool2 = arrowScroll(17);
          break;
        }
      }
      else if (paramKeyEvent.hasNoModifiers())
      {
        bool2 = arrowScroll(2);
      }
      else
      {
        bool2 = bool1;
        if (paramKeyEvent.hasModifiers(1)) {
          bool2 = arrowScroll(1);
        }
      }
    }
    return bool2;
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams()
  {
    return new LayoutParams();
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return generateDefaultLayoutParams();
  }
  
  public PagerAdapter getAdapter()
  {
    return mAdapter;
  }
  
  protected int getChildDrawingOrder(int paramInt1, int paramInt2)
  {
    if (mDrawingOrder == 2) {
      paramInt1 = paramInt1 - 1 - paramInt2;
    } else {
      paramInt1 = paramInt2;
    }
    return mDrawingOrderedChildren.get(paramInt1)).getLayoutParams()).childIndex;
  }
  
  public Object getCurrent()
  {
    Object localObject = infoForPosition(getCurrentItem());
    if (localObject == null) {
      localObject = null;
    } else {
      localObject = object;
    }
    return localObject;
  }
  
  public int getCurrentItem()
  {
    return mCurItem;
  }
  
  public int getOffscreenPageLimit()
  {
    return mOffscreenPageLimit;
  }
  
  public int getPageMargin()
  {
    return mPageMargin;
  }
  
  ItemInfo infoForAnyChild(View paramView)
  {
    for (;;)
    {
      ViewParent localViewParent = paramView.getParent();
      if (localViewParent == this) {
        break label34;
      }
      if ((localViewParent == null) || (!(localViewParent instanceof View))) {
        break;
      }
      paramView = (View)localViewParent;
    }
    return null;
    label34:
    return infoForChild(paramView);
  }
  
  ItemInfo infoForChild(View paramView)
  {
    for (int i = 0; i < mItems.size(); i++)
    {
      ItemInfo localItemInfo = (ItemInfo)mItems.get(i);
      if (mAdapter.isViewFromObject(paramView, object)) {
        return localItemInfo;
      }
    }
    return null;
  }
  
  ItemInfo infoForPosition(int paramInt)
  {
    for (int i = 0; i < mItems.size(); i++)
    {
      ItemInfo localItemInfo = (ItemInfo)mItems.get(i);
      if (position == paramInt) {
        return localItemInfo;
      }
    }
    return null;
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    mFirstLayout = true;
  }
  
  protected void onDetachedFromWindow()
  {
    removeCallbacks(mEndScrollRunnable);
    super.onDetachedFromWindow();
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    if ((mPageMargin > 0) && (mMarginDrawable != null) && (mItems.size() > 0) && (mAdapter != null))
    {
      int i = getScrollX();
      int j = getWidth();
      float f1 = mPageMargin / j;
      Object localObject = (ItemInfo)mItems.get(0);
      float f2 = offset;
      int k = mItems.size();
      int m = position;
      int n = mItems.get(k - 1)).position;
      int i1 = 0;
      while (m < n)
      {
        while ((m > position) && (i1 < k))
        {
          localObject = mItems;
          i1++;
          localObject = (ItemInfo)((ArrayList)localObject).get(i1);
        }
        float f3;
        if (m == position)
        {
          f2 = offset;
          f3 = widthFactor;
        }
        else
        {
          f3 = mAdapter.getPageWidth(m);
        }
        float f4 = j * f2;
        if (isLayoutRtl()) {
          f4 = 1.6777216E7F - f4;
        } else {
          f4 = j * f3 + f4;
        }
        f2 = f2 + f3 + f1;
        if (mPageMargin + f4 > i)
        {
          mMarginDrawable.setBounds((int)f4, mTopPageBounds, (int)(mPageMargin + f4 + 0.5F), mBottomPageBounds);
          mMarginDrawable.draw(paramCanvas);
        }
        if (f4 > i + j) {
          break;
        }
        m++;
      }
    }
  }
  
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
    paramAccessibilityEvent.setClassName(ViewPager.class.getName());
    paramAccessibilityEvent.setScrollable(canScroll());
    if ((paramAccessibilityEvent.getEventType() == 4096) && (mAdapter != null))
    {
      paramAccessibilityEvent.setItemCount(mAdapter.getCount());
      paramAccessibilityEvent.setFromIndex(mCurItem);
      paramAccessibilityEvent.setToIndex(mCurItem);
    }
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setClassName(ViewPager.class.getName());
    paramAccessibilityNodeInfo.setScrollable(canScroll());
    if (canScrollHorizontally(1))
    {
      paramAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD);
      paramAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_RIGHT);
    }
    if (canScrollHorizontally(-1))
    {
      paramAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_BACKWARD);
      paramAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_LEFT);
    }
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction() & 0xFF;
    if ((i != 3) && (i != 1))
    {
      if (i != 0)
      {
        if (mIsBeingDragged) {
          return true;
        }
        if (mIsUnableToDrag) {
          return false;
        }
      }
      float f3;
      if (i != 0)
      {
        if (i != 2)
        {
          if (i == 6) {
            onSecondaryPointerUp(paramMotionEvent);
          }
        }
        else
        {
          i = mActivePointerId;
          if (i != -1)
          {
            i = paramMotionEvent.findPointerIndex(i);
            float f1 = paramMotionEvent.getX(i);
            float f2 = f1 - mLastMotionX;
            f3 = Math.abs(f2);
            float f4 = paramMotionEvent.getY(i);
            float f5 = Math.abs(f4 - mInitialMotionY);
            if ((f2 != 0.0F) && (!isGutterDrag(mLastMotionX, f2)) && (canScroll(this, false, (int)f2, (int)f1, (int)f4)))
            {
              mLastMotionX = f1;
              mLastMotionY = f4;
              mIsUnableToDrag = true;
              return false;
            }
            if ((f3 > mTouchSlop) && (0.5F * f3 > f5))
            {
              mIsBeingDragged = true;
              requestParentDisallowInterceptTouchEvent(true);
              setScrollState(1);
              if (f2 > 0.0F) {
                f3 = mInitialMotionX + mTouchSlop;
              } else {
                f3 = mInitialMotionX - mTouchSlop;
              }
              mLastMotionX = f3;
              mLastMotionY = f4;
              setScrollingCacheEnabled(true);
            }
            else if (f5 > mTouchSlop)
            {
              mIsUnableToDrag = true;
            }
            if ((mIsBeingDragged) && (performDrag(f1))) {
              postInvalidateOnAnimation();
            }
          }
        }
      }
      else
      {
        f3 = paramMotionEvent.getX();
        mInitialMotionX = f3;
        mLastMotionX = f3;
        f3 = paramMotionEvent.getY();
        mInitialMotionY = f3;
        mLastMotionY = f3;
        mActivePointerId = paramMotionEvent.getPointerId(0);
        mIsUnableToDrag = false;
        mScroller.computeScrollOffset();
        if ((mScrollState == 2) && (Math.abs(mScroller.getFinalX() - mScroller.getCurrX()) > mCloseEnough))
        {
          mScroller.abortAnimation();
          mPopulatePending = false;
          populate();
          mIsBeingDragged = true;
          requestParentDisallowInterceptTouchEvent(true);
          setScrollState(1);
        }
        else
        {
          completeScroll(false);
          mIsBeingDragged = false;
        }
      }
      if (mVelocityTracker == null) {
        mVelocityTracker = VelocityTracker.obtain();
      }
      mVelocityTracker.addMovement(paramMotionEvent);
      return mIsBeingDragged;
    }
    mIsBeingDragged = false;
    mIsUnableToDrag = false;
    mActivePointerId = -1;
    if (mVelocityTracker != null)
    {
      mVelocityTracker.recycle();
      mVelocityTracker = null;
    }
    return false;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = getChildCount();
    int j = paramInt3 - paramInt1;
    int k = paramInt4 - paramInt2;
    paramInt2 = getPaddingLeft();
    paramInt1 = getPaddingTop();
    int m = getPaddingRight();
    paramInt4 = getPaddingBottom();
    int n = getScrollX();
    int i1 = 0;
    int i2 = 0;
    Object localObject;
    int i4;
    int i5;
    LayoutParams localLayoutParams;
    while (i2 < i)
    {
      localObject = getChildAt(i2);
      i3 = paramInt2;
      i4 = m;
      i5 = paramInt1;
      i6 = paramInt4;
      paramInt3 = i1;
      if (((View)localObject).getVisibility() != 8)
      {
        localLayoutParams = (LayoutParams)((View)localObject).getLayoutParams();
        i3 = paramInt2;
        i4 = m;
        i5 = paramInt1;
        i6 = paramInt4;
        paramInt3 = i1;
        if (isDecor)
        {
          paramInt3 = gravity & 0x7;
          i6 = gravity & 0x70;
          if (paramInt3 != 1)
          {
            if (paramInt3 != 3)
            {
              if (paramInt3 != 5)
              {
                paramInt3 = paramInt2;
                i3 = paramInt2;
              }
              else
              {
                paramInt3 = j - m - ((View)localObject).getMeasuredWidth();
                m += ((View)localObject).getMeasuredWidth();
                i3 = paramInt2;
              }
            }
            else
            {
              paramInt3 = paramInt2;
              i3 = paramInt2 + ((View)localObject).getMeasuredWidth();
            }
          }
          else
          {
            paramInt3 = Math.max((j - ((View)localObject).getMeasuredWidth()) / 2, paramInt2);
            i3 = paramInt2;
          }
          if (i6 != 16)
          {
            if (i6 != 48)
            {
              if (i6 != 80)
              {
                paramInt2 = paramInt1;
              }
              else
              {
                paramInt2 = k - paramInt4 - ((View)localObject).getMeasuredHeight();
                paramInt4 += ((View)localObject).getMeasuredHeight();
              }
            }
            else
            {
              paramInt2 = paramInt1;
              paramInt1 += ((View)localObject).getMeasuredHeight();
            }
          }
          else {
            paramInt2 = Math.max((k - ((View)localObject).getMeasuredHeight()) / 2, paramInt1);
          }
          paramInt3 += n;
          ((View)localObject).layout(paramInt3, paramInt2, paramInt3 + ((View)localObject).getMeasuredWidth(), paramInt2 + ((View)localObject).getMeasuredHeight());
          paramInt3 = i1 + 1;
          i6 = paramInt4;
          i5 = paramInt1;
          i4 = m;
        }
      }
      i2++;
      paramInt2 = i3;
      m = i4;
      paramInt1 = i5;
      paramInt4 = i6;
      i1 = paramInt3;
    }
    int i3 = j - paramInt2 - m;
    int i6 = 0;
    paramInt3 = j;
    i2 = i;
    while (i6 < i2)
    {
      View localView = getChildAt(i6);
      if (localView.getVisibility() != 8)
      {
        do
        {
          do
          {
            localLayoutParams = (LayoutParams)localView.getLayoutParams();
          } while (isDecor);
          localObject = infoForChild(localView);
        } while (localObject == null);
        if (needsMeasure)
        {
          needsMeasure = false;
          localView.measure(View.MeasureSpec.makeMeasureSpec((int)(i3 * widthFactor), 1073741824), View.MeasureSpec.makeMeasureSpec(k - paramInt1 - paramInt4, 1073741824));
        }
        i4 = localView.getMeasuredWidth();
        i5 = (int)(i3 * offset);
        if (isLayoutRtl()) {
          i5 = 16777216 - m - i5 - i4;
        } else {
          i5 = paramInt2 + i5;
        }
        localView.layout(i5, paramInt1, i5 + i4, paramInt1 + localView.getMeasuredHeight());
      }
      i6++;
    }
    mTopPageBounds = paramInt1;
    mBottomPageBounds = (k - paramInt4);
    mDecorChildCount = i1;
    if (mFirstLayout) {
      scrollToItem(mCurItem, false, 0, false);
    }
    mFirstLayout = false;
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    setMeasuredDimension(getDefaultSize(0, paramInt1), getDefaultSize(0, paramInt2));
    int i = getMeasuredWidth();
    int j = i / 10;
    mGutterSize = Math.min(j, mDefaultGutterSize);
    int k = getPaddingLeft();
    paramInt1 = getPaddingRight();
    int m = getMeasuredHeight();
    paramInt2 = getPaddingTop();
    int n = getPaddingBottom();
    int i1 = getChildCount();
    paramInt2 = m - paramInt2 - n;
    paramInt1 = i - k - paramInt1;
    m = 0;
    Object localObject1;
    Object localObject2;
    while (m < i1)
    {
      localObject1 = getChildAt(m);
      if (((View)localObject1).getVisibility() != 8)
      {
        localObject2 = (LayoutParams)((View)localObject1).getLayoutParams();
        if ((localObject2 != null) && (isDecor))
        {
          n = gravity & 0x7;
          int i2 = gravity & 0x70;
          int i3 = Integer.MIN_VALUE;
          k = Integer.MIN_VALUE;
          if ((i2 != 48) && (i2 != 80)) {
            i2 = 0;
          } else {
            i2 = 1;
          }
          int i4;
          if ((n != 3) && (n != 5)) {
            i4 = 0;
          } else {
            i4 = 1;
          }
          if (i2 != 0)
          {
            n = 1073741824;
          }
          else
          {
            n = i3;
            if (i4 != 0)
            {
              k = 1073741824;
              n = i3;
            }
          }
          if (width != -2)
          {
            i3 = 1073741824;
            n = i3;
            if (width != -1)
            {
              n = width;
              break label298;
            }
          }
          int i5 = paramInt1;
          i3 = n;
          n = i5;
          label298:
          if (height != -2)
          {
            k = 1073741824;
            if (height != -1)
            {
              i5 = height;
              k = 1073741824;
              break label340;
            }
          }
          i5 = paramInt2;
          label340:
          ((View)localObject1).measure(View.MeasureSpec.makeMeasureSpec(n, i3), View.MeasureSpec.makeMeasureSpec(i5, k));
          if (i2 != 0)
          {
            n = paramInt2 - ((View)localObject1).getMeasuredHeight();
            k = paramInt1;
            break label411;
          }
          k = paramInt1;
          n = paramInt2;
          if (i4 == 0) {
            break label411;
          }
          k = paramInt1 - ((View)localObject1).getMeasuredWidth();
          n = paramInt2;
          break label411;
        }
      }
      n = paramInt2;
      k = paramInt1;
      label411:
      m++;
      paramInt1 = k;
      paramInt2 = n;
    }
    mChildWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824);
    mChildHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824);
    mInLayout = true;
    populate();
    paramInt2 = 0;
    mInLayout = false;
    k = getChildCount();
    while (paramInt2 < k)
    {
      localObject2 = getChildAt(paramInt2);
      if (((View)localObject2).getVisibility() != 8)
      {
        localObject1 = (LayoutParams)((View)localObject2).getLayoutParams();
        if ((localObject1 == null) || (!isDecor)) {
          ((View)localObject2).measure(View.MeasureSpec.makeMeasureSpec((int)(paramInt1 * widthFactor), 1073741824), mChildHeightMeasureSpec);
        }
      }
      paramInt2++;
    }
  }
  
  protected void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
  {
    int j;
    Object localObject;
    if (mDecorChildCount > 0)
    {
      int i = getScrollX();
      j = getPaddingLeft();
      int k = getPaddingRight();
      int m = getWidth();
      int n = getChildCount();
      int i1 = 0;
      while (i1 < n)
      {
        View localView = getChildAt(i1);
        localObject = (LayoutParams)localView.getLayoutParams();
        int i2;
        int i3;
        if (!isDecor)
        {
          i2 = j;
          i3 = k;
        }
        else
        {
          i2 = gravity & 0x7;
          if (i2 != 1)
          {
            if (i2 != 3)
            {
              if (i2 != 5)
              {
                i2 = j;
              }
              else
              {
                i2 = m - k - localView.getMeasuredWidth();
                k += localView.getMeasuredWidth();
              }
            }
            else
            {
              i2 = j;
              j += localView.getWidth();
            }
          }
          else {
            i2 = Math.max((m - localView.getMeasuredWidth()) / 2, j);
          }
          int i4 = i2 + i - localView.getLeft();
          i2 = j;
          i3 = k;
          if (i4 != 0)
          {
            localView.offsetLeftAndRight(i4);
            i3 = k;
            i2 = j;
          }
        }
        i1++;
        j = i2;
        k = i3;
      }
    }
    if (mOnPageChangeListener != null) {
      mOnPageChangeListener.onPageScrolled(paramInt1, paramFloat, paramInt2);
    }
    if (mInternalPageChangeListener != null) {
      mInternalPageChangeListener.onPageScrolled(paramInt1, paramFloat, paramInt2);
    }
    if (mPageTransformer != null)
    {
      paramInt2 = getScrollX();
      j = getChildCount();
      for (paramInt1 = 0; paramInt1 < j; paramInt1++)
      {
        localObject = getChildAt(paramInt1);
        if (!getLayoutParamsisDecor)
        {
          paramFloat = (((View)localObject).getLeft() - paramInt2) / getPaddedWidth();
          mPageTransformer.transformPage((View)localObject, paramFloat);
        }
      }
    }
    mCalledSuper = true;
  }
  
  protected boolean onRequestFocusInDescendants(int paramInt, Rect paramRect)
  {
    int i = getChildCount();
    int j;
    int k;
    if ((paramInt & 0x2) != 0)
    {
      j = 0;
      k = 1;
    }
    else
    {
      j = i - 1;
      k = -1;
      i = -1;
    }
    while (j != i)
    {
      View localView = getChildAt(j);
      if (localView.getVisibility() == 0)
      {
        ItemInfo localItemInfo = infoForChild(localView);
        if ((localItemInfo != null) && (position == mCurItem) && (localView.requestFocus(paramInt, paramRect))) {
          return true;
        }
      }
      j += k;
    }
    return false;
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if (!(paramParcelable instanceof SavedState))
    {
      super.onRestoreInstanceState(paramParcelable);
      return;
    }
    paramParcelable = (SavedState)paramParcelable;
    super.onRestoreInstanceState(paramParcelable.getSuperState());
    if (mAdapter != null)
    {
      mAdapter.restoreState(adapterState, loader);
      setCurrentItemInternal(position, false, true);
    }
    else
    {
      mRestoredCurItem = position;
      mRestoredAdapterState = adapterState;
      mRestoredClassLoader = loader;
    }
  }
  
  public void onRtlPropertiesChanged(int paramInt)
  {
    super.onRtlPropertiesChanged(paramInt);
    if (paramInt == 0) {
      mLeftIncr = -1;
    } else {
      mLeftIncr = 1;
    }
  }
  
  public Parcelable onSaveInstanceState()
  {
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    position = mCurItem;
    if (mAdapter != null) {
      adapterState = mAdapter.saveState();
    }
    return localSavedState;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramInt1 != paramInt3) {
      recomputeScrollPosition(paramInt1, paramInt3, mPageMargin, mPageMargin);
    }
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if ((paramMotionEvent.getAction() == 0) && (paramMotionEvent.getEdgeFlags() != 0)) {
      return false;
    }
    if ((mAdapter != null) && (mAdapter.getCount() != 0))
    {
      if (mVelocityTracker == null) {
        mVelocityTracker = VelocityTracker.obtain();
      }
      mVelocityTracker.addMovement(paramMotionEvent);
      int i = paramMotionEvent.getAction();
      boolean bool = false;
      float f2;
      Object localObject;
      int j;
      switch (i & 0xFF)
      {
      case 4: 
      default: 
        break;
      case 6: 
        onSecondaryPointerUp(paramMotionEvent);
        mLastMotionX = paramMotionEvent.getX(paramMotionEvent.findPointerIndex(mActivePointerId));
        break;
      case 5: 
        i = paramMotionEvent.getActionIndex();
        mLastMotionX = paramMotionEvent.getX(i);
        mActivePointerId = paramMotionEvent.getPointerId(i);
        break;
      case 3: 
        if (mIsBeingDragged)
        {
          scrollToItem(mCurItem, true, 0, false);
          mActivePointerId = -1;
          endDrag();
          mLeftEdge.onRelease();
          mRightEdge.onRelease();
          bool = true;
        }
        break;
      case 2: 
        if (!mIsBeingDragged)
        {
          i = paramMotionEvent.findPointerIndex(mActivePointerId);
          float f1 = paramMotionEvent.getX(i);
          f2 = Math.abs(f1 - mLastMotionX);
          float f3 = paramMotionEvent.getY(i);
          float f4 = Math.abs(f3 - mLastMotionY);
          if ((f2 > mTouchSlop) && (f2 > f4))
          {
            mIsBeingDragged = true;
            requestParentDisallowInterceptTouchEvent(true);
            if (f1 - mInitialMotionX > 0.0F) {
              f2 = mInitialMotionX + mTouchSlop;
            } else {
              f2 = mInitialMotionX - mTouchSlop;
            }
            mLastMotionX = f2;
            mLastMotionY = f3;
            setScrollState(1);
            setScrollingCacheEnabled(true);
            localObject = getParent();
            if (localObject != null) {
              ((ViewParent)localObject).requestDisallowInterceptTouchEvent(true);
            }
          }
        }
        if (mIsBeingDragged) {
          bool = false | performDrag(paramMotionEvent.getX(paramMotionEvent.findPointerIndex(mActivePointerId)));
        }
        break;
      case 1: 
        if (mIsBeingDragged)
        {
          localObject = mVelocityTracker;
          ((VelocityTracker)localObject).computeCurrentVelocity(1000, mMaximumVelocity);
          j = (int)((VelocityTracker)localObject).getXVelocity(mActivePointerId);
          mPopulatePending = true;
          f2 = getScrollStart() / getPaddedWidth();
          localObject = infoForFirstVisiblePage();
          i = position;
          if (isLayoutRtl()) {
            f2 = (offset - f2) / widthFactor;
          } else {
            f2 = (f2 - offset) / widthFactor;
          }
          setCurrentItemInternal(determineTargetPage(i, f2, j, (int)(paramMotionEvent.getX(paramMotionEvent.findPointerIndex(mActivePointerId)) - mInitialMotionX)), true, true, j);
          mActivePointerId = -1;
          endDrag();
          mLeftEdge.onRelease();
          mRightEdge.onRelease();
          j = 1;
        }
        break;
      case 0: 
        mScroller.abortAnimation();
        mPopulatePending = false;
        populate();
        f2 = paramMotionEvent.getX();
        mInitialMotionX = f2;
        mLastMotionX = f2;
        f2 = paramMotionEvent.getY();
        mInitialMotionY = f2;
        mLastMotionY = f2;
        mActivePointerId = paramMotionEvent.getPointerId(0);
      }
      if (j != 0) {
        postInvalidateOnAnimation();
      }
      return true;
    }
    return false;
  }
  
  boolean pageLeft()
  {
    return setCurrentItemInternal(mCurItem + mLeftIncr, true, false);
  }
  
  boolean pageRight()
  {
    return setCurrentItemInternal(mCurItem - mLeftIncr, true, false);
  }
  
  public boolean performAccessibilityAction(int paramInt, Bundle paramBundle)
  {
    if (super.performAccessibilityAction(paramInt, paramBundle)) {
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
        if (canScrollHorizontally(-1))
        {
          setCurrentItem(mCurItem - 1);
          return true;
        }
        return false;
      }
    }
    if (canScrollHorizontally(1))
    {
      setCurrentItem(mCurItem + 1);
      return true;
    }
    return false;
  }
  
  public void populate()
  {
    populate(mCurItem);
  }
  
  void populate(int paramInt)
  {
    Object localObject1 = null;
    int i = 2;
    int j;
    if (mCurItem != paramInt)
    {
      if (mCurItem < paramInt) {
        j = 66;
      } else {
        j = 17;
      }
      localObject1 = infoForPosition(mCurItem);
      mCurItem = paramInt;
      i = j;
    }
    if (mAdapter == null)
    {
      sortChildDrawingOrder();
      return;
    }
    if (mPopulatePending)
    {
      sortChildDrawingOrder();
      return;
    }
    if (getWindowToken() == null) {
      return;
    }
    mAdapter.startUpdate(this);
    int k = mOffscreenPageLimit;
    int m = Math.max(0, mCurItem - k);
    int n = mAdapter.getCount();
    int i1 = Math.min(n - 1, mCurItem + k);
    Object localObject3;
    if (n == mExpectedAdapterCount)
    {
      Object localObject2 = null;
      Object localObject4;
      for (paramInt = 0;; paramInt++)
      {
        localObject3 = localObject2;
        if (paramInt >= mItems.size()) {
          break;
        }
        localObject4 = (ItemInfo)mItems.get(paramInt);
        if (position >= mCurItem)
        {
          localObject3 = localObject2;
          if (position != mCurItem) {
            break;
          }
          localObject3 = localObject4;
          break;
        }
      }
      localObject2 = localObject3;
      if (localObject3 == null)
      {
        localObject2 = localObject3;
        if (n > 0) {
          localObject2 = addNewItem(mCurItem, paramInt);
        }
      }
      if (localObject2 != null)
      {
        float f1 = 0.0F;
        j = paramInt - 1;
        if (j >= 0) {
          localObject3 = (ItemInfo)mItems.get(j);
        } else {
          localObject3 = null;
        }
        int i2 = getPaddedWidth();
        if (i2 <= 0)
        {
          f2 = 0.0F;
        }
        else
        {
          f2 = widthFactor;
          f2 = getPaddingLeft() / i2 + (2.0F - f2);
        }
        float f3 = f2;
        int i3 = mCurItem - 1;
        float f2 = f1;
        int i4;
        int i5;
        while (i3 >= 0)
        {
          if ((f2 >= f3) && (i3 < m))
          {
            if (localObject3 == null) {
              break;
            }
            i4 = paramInt;
            f1 = f2;
            i5 = j;
            localObject4 = localObject3;
            if (i3 != position) {
              break label590;
            }
            i4 = paramInt;
            f1 = f2;
            i5 = j;
            localObject4 = localObject3;
            if (scrolling) {
              break label590;
            }
            mItems.remove(j);
            mAdapter.destroyItem(this, i3, object);
            j--;
            paramInt--;
            if (j >= 0) {
              localObject3 = (ItemInfo)mItems.get(j);
            } else {
              localObject3 = null;
            }
          }
          else
          {
            if ((localObject3 == null) || (i3 != position)) {
              break label529;
            }
            f2 += widthFactor;
            j--;
            if (j >= 0) {
              localObject3 = (ItemInfo)mItems.get(j);
            } else {
              localObject3 = null;
            }
          }
          break label575;
          label529:
          f2 += addNewItem1widthFactor;
          paramInt++;
          if (j >= 0) {
            localObject3 = (ItemInfo)mItems.get(j);
          } else {
            localObject3 = null;
          }
          label575:
          localObject4 = localObject3;
          i5 = j;
          f1 = f2;
          i4 = paramInt;
          label590:
          i3--;
          paramInt = i4;
          f2 = f1;
          j = i5;
          localObject3 = localObject4;
        }
        f2 = widthFactor;
        j = paramInt + 1;
        if (f2 < 2.0F)
        {
          if (j < mItems.size()) {
            localObject3 = (ItemInfo)mItems.get(j);
          } else {
            localObject3 = null;
          }
          if (i2 <= 0) {
            f3 = 0.0F;
          } else {
            f3 = getPaddingRight() / i2 + 2.0F;
          }
          i5 = mCurItem + 1;
          i3 = i2;
          i4 = k;
          while (i5 < n)
          {
            if ((f2 >= f3) && (i5 > i1))
            {
              if (localObject3 == null) {
                break;
              }
              if ((i5 == position) && (!scrolling))
              {
                mItems.remove(j);
                mAdapter.destroyItem(this, i5, object);
                if (j < mItems.size()) {
                  localObject3 = (ItemInfo)mItems.get(j);
                } else {
                  localObject3 = null;
                }
              }
            }
            else if ((localObject3 != null) && (i5 == position))
            {
              f2 += widthFactor;
              j++;
              if (j < mItems.size()) {
                localObject3 = (ItemInfo)mItems.get(j);
              } else {
                localObject3 = null;
              }
            }
            else
            {
              localObject3 = addNewItem(i5, j);
              j++;
              f2 += widthFactor;
              if (j < mItems.size()) {
                localObject3 = (ItemInfo)mItems.get(j);
              } else {
                localObject3 = null;
              }
            }
            i5++;
          }
        }
        calculatePageOffsets((ItemInfo)localObject2, paramInt, (ItemInfo)localObject1);
      }
      localObject1 = mAdapter;
      paramInt = mCurItem;
      if (localObject2 != null) {
        localObject3 = object;
      } else {
        localObject3 = null;
      }
      ((PagerAdapter)localObject1).setPrimaryItem(this, paramInt, localObject3);
      mAdapter.finishUpdate(this);
      j = getChildCount();
      for (paramInt = 0; paramInt < j; paramInt++)
      {
        localObject1 = getChildAt(paramInt);
        localObject3 = (LayoutParams)((View)localObject1).getLayoutParams();
        childIndex = paramInt;
        if ((!isDecor) && (widthFactor == 0.0F))
        {
          localObject1 = infoForChild((View)localObject1);
          if (localObject1 != null)
          {
            widthFactor = widthFactor;
            position = position;
          }
        }
      }
      sortChildDrawingOrder();
      if (hasFocus())
      {
        localObject1 = findFocus();
        if (localObject1 != null) {
          localObject3 = infoForAnyChild((View)localObject1);
        } else {
          localObject3 = null;
        }
        if ((localObject3 == null) || (position != mCurItem)) {
          for (paramInt = 0; paramInt < getChildCount(); paramInt++)
          {
            localObject2 = getChildAt(paramInt);
            localObject3 = infoForChild((View)localObject2);
            if ((localObject3 != null) && (position == mCurItem))
            {
              if (localObject1 == null)
              {
                localObject3 = null;
              }
              else
              {
                localObject3 = mTempRect;
                ((View)localObject1).getFocusedRect(mTempRect);
                offsetDescendantRectToMyCoords((View)localObject1, mTempRect);
                offsetRectIntoDescendantCoords((View)localObject2, mTempRect);
              }
              if (((View)localObject2).requestFocus(i, (Rect)localObject3)) {
                break;
              }
            }
          }
        }
      }
      return;
    }
    String str;
    try
    {
      localObject3 = getResources().getResourceName(getId());
    }
    catch (Resources.NotFoundException localNotFoundException)
    {
      str = Integer.toHexString(getId());
    }
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: ");
    ((StringBuilder)localObject1).append(mExpectedAdapterCount);
    ((StringBuilder)localObject1).append(", found: ");
    ((StringBuilder)localObject1).append(n);
    ((StringBuilder)localObject1).append(" Pager id: ");
    ((StringBuilder)localObject1).append(str);
    ((StringBuilder)localObject1).append(" Pager class: ");
    ((StringBuilder)localObject1).append(getClass());
    ((StringBuilder)localObject1).append(" Problematic adapter: ");
    ((StringBuilder)localObject1).append(mAdapter.getClass());
    throw new IllegalStateException(((StringBuilder)localObject1).toString());
  }
  
  public void removeView(View paramView)
  {
    if (mInLayout) {
      removeViewInLayout(paramView);
    } else {
      super.removeView(paramView);
    }
  }
  
  public void setAdapter(PagerAdapter paramPagerAdapter)
  {
    if (mAdapter != null)
    {
      mAdapter.unregisterDataSetObserver(mObserver);
      mAdapter.startUpdate(this);
      for (int i = 0; i < mItems.size(); i++)
      {
        localObject = (ItemInfo)mItems.get(i);
        mAdapter.destroyItem(this, position, object);
      }
      mAdapter.finishUpdate(this);
      mItems.clear();
      removeNonDecorViews();
      mCurItem = 0;
      scrollTo(0, 0);
    }
    Object localObject = mAdapter;
    mAdapter = paramPagerAdapter;
    mExpectedAdapterCount = 0;
    if (mAdapter != null)
    {
      if (mObserver == null) {
        mObserver = new PagerObserver(null);
      }
      mAdapter.registerDataSetObserver(mObserver);
      mPopulatePending = false;
      boolean bool = mFirstLayout;
      mFirstLayout = true;
      mExpectedAdapterCount = mAdapter.getCount();
      if (mRestoredCurItem >= 0)
      {
        mAdapter.restoreState(mRestoredAdapterState, mRestoredClassLoader);
        setCurrentItemInternal(mRestoredCurItem, false, true);
        mRestoredCurItem = -1;
        mRestoredAdapterState = null;
        mRestoredClassLoader = null;
      }
      else if (!bool)
      {
        populate();
      }
      else
      {
        requestLayout();
      }
    }
    if ((mAdapterChangeListener != null) && (localObject != paramPagerAdapter)) {
      mAdapterChangeListener.onAdapterChanged((PagerAdapter)localObject, paramPagerAdapter);
    }
  }
  
  public void setCurrentItem(int paramInt)
  {
    mPopulatePending = false;
    setCurrentItemInternal(paramInt, mFirstLayout ^ true, false);
  }
  
  public void setCurrentItem(int paramInt, boolean paramBoolean)
  {
    mPopulatePending = false;
    setCurrentItemInternal(paramInt, paramBoolean, false);
  }
  
  boolean setCurrentItemInternal(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    return setCurrentItemInternal(paramInt, paramBoolean1, paramBoolean2, 0);
  }
  
  boolean setCurrentItemInternal(int paramInt1, boolean paramBoolean1, boolean paramBoolean2, int paramInt2)
  {
    PagerAdapter localPagerAdapter = mAdapter;
    boolean bool = false;
    if ((localPagerAdapter != null) && (mAdapter.getCount() > 0))
    {
      int i = MathUtils.constrain(paramInt1, 0, mAdapter.getCount() - 1);
      if ((!paramBoolean2) && (mCurItem == i) && (mItems.size() != 0))
      {
        setScrollingCacheEnabled(false);
        return false;
      }
      paramInt1 = mOffscreenPageLimit;
      if ((i > mCurItem + paramInt1) || (i < mCurItem - paramInt1)) {
        for (paramInt1 = 0; paramInt1 < mItems.size(); paramInt1++) {
          mItems.get(paramInt1)).scrolling = true;
        }
      }
      paramBoolean2 = bool;
      if (mCurItem != i) {
        paramBoolean2 = true;
      }
      if (mFirstLayout)
      {
        mCurItem = i;
        if ((paramBoolean2) && (mOnPageChangeListener != null)) {
          mOnPageChangeListener.onPageSelected(i);
        }
        if ((paramBoolean2) && (mInternalPageChangeListener != null)) {
          mInternalPageChangeListener.onPageSelected(i);
        }
        requestLayout();
      }
      else
      {
        populate(i);
        scrollToItem(i, paramBoolean1, paramInt2, paramBoolean2);
      }
      return true;
    }
    setScrollingCacheEnabled(false);
    return false;
  }
  
  OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener paramOnPageChangeListener)
  {
    OnPageChangeListener localOnPageChangeListener = mInternalPageChangeListener;
    mInternalPageChangeListener = paramOnPageChangeListener;
    return localOnPageChangeListener;
  }
  
  public void setOffscreenPageLimit(int paramInt)
  {
    int i = paramInt;
    if (paramInt < 1)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Requested offscreen page limit ");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(" too small; defaulting to ");
      localStringBuilder.append(1);
      Log.w("ViewPager", localStringBuilder.toString());
      i = 1;
    }
    if (i != mOffscreenPageLimit)
    {
      mOffscreenPageLimit = i;
      populate();
    }
  }
  
  void setOnAdapterChangeListener(OnAdapterChangeListener paramOnAdapterChangeListener)
  {
    mAdapterChangeListener = paramOnAdapterChangeListener;
  }
  
  public void setOnPageChangeListener(OnPageChangeListener paramOnPageChangeListener)
  {
    mOnPageChangeListener = paramOnPageChangeListener;
  }
  
  public void setPageMargin(int paramInt)
  {
    int i = mPageMargin;
    mPageMargin = paramInt;
    int j = getWidth();
    recomputeScrollPosition(j, j, paramInt, i);
    requestLayout();
  }
  
  public void setPageMarginDrawable(int paramInt)
  {
    setPageMarginDrawable(getContext().getDrawable(paramInt));
  }
  
  public void setPageMarginDrawable(Drawable paramDrawable)
  {
    mMarginDrawable = paramDrawable;
    if (paramDrawable != null) {
      refreshDrawableState();
    }
    boolean bool;
    if (paramDrawable == null) {
      bool = true;
    } else {
      bool = false;
    }
    setWillNotDraw(bool);
    invalidate();
  }
  
  public void setPageTransformer(boolean paramBoolean, PageTransformer paramPageTransformer)
  {
    int i = 1;
    boolean bool1;
    if (paramPageTransformer != null) {
      bool1 = true;
    } else {
      bool1 = false;
    }
    boolean bool2;
    if (mPageTransformer != null) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    int j;
    if (bool1 != bool2) {
      j = 1;
    } else {
      j = 0;
    }
    mPageTransformer = paramPageTransformer;
    setChildrenDrawingOrderEnabled(bool1);
    if (bool1)
    {
      if (paramBoolean) {
        i = 2;
      }
      mDrawingOrder = i;
    }
    else
    {
      mDrawingOrder = 0;
    }
    if (j != 0) {
      populate();
    }
  }
  
  void smoothScrollTo(int paramInt1, int paramInt2)
  {
    smoothScrollTo(paramInt1, paramInt2, 0);
  }
  
  void smoothScrollTo(int paramInt1, int paramInt2, int paramInt3)
  {
    if (getChildCount() == 0)
    {
      setScrollingCacheEnabled(false);
      return;
    }
    int i = getScrollX();
    int j = getScrollY();
    int k = paramInt1 - i;
    paramInt2 -= j;
    if ((k == 0) && (paramInt2 == 0))
    {
      completeScroll(false);
      populate();
      setScrollState(0);
      return;
    }
    setScrollingCacheEnabled(true);
    setScrollState(2);
    paramInt1 = getPaddedWidth();
    int m = paramInt1 / 2;
    float f1 = Math.min(1.0F, Math.abs(k) * 1.0F / paramInt1);
    float f2 = m;
    float f3 = m;
    f1 = distanceInfluenceForSnapDuration(f1);
    paramInt3 = Math.abs(paramInt3);
    if (paramInt3 > 0)
    {
      paramInt1 = 4 * Math.round(1000.0F * Math.abs((f2 + f3 * f1) / paramInt3));
    }
    else
    {
      f3 = paramInt1;
      f2 = mAdapter.getPageWidth(mCurItem);
      paramInt1 = (int)((1.0F + Math.abs(k) / (mPageMargin + f3 * f2)) * 100.0F);
    }
    paramInt1 = Math.min(paramInt1, 600);
    mScroller.startScroll(i, j, k, paramInt2, paramInt1);
    postInvalidateOnAnimation();
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable)
  {
    boolean bool;
    if ((!super.verifyDrawable(paramDrawable)) && (paramDrawable != mMarginDrawable)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  static abstract interface Decor {}
  
  static class ItemInfo
  {
    Object object;
    float offset;
    int position;
    boolean scrolling;
    float widthFactor;
    
    ItemInfo() {}
  }
  
  public static class LayoutParams
    extends ViewGroup.LayoutParams
  {
    int childIndex;
    public int gravity;
    public boolean isDecor;
    boolean needsMeasure;
    int position;
    float widthFactor = 0.0F;
    
    public LayoutParams()
    {
      super(-1);
    }
    
    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, ViewPager.LAYOUT_ATTRS);
      gravity = paramContext.getInteger(0, 48);
      paramContext.recycle();
    }
  }
  
  static abstract interface OnAdapterChangeListener
  {
    public abstract void onAdapterChanged(PagerAdapter paramPagerAdapter1, PagerAdapter paramPagerAdapter2);
  }
  
  public static abstract interface OnPageChangeListener
  {
    public abstract void onPageScrollStateChanged(int paramInt);
    
    public abstract void onPageScrolled(int paramInt1, float paramFloat, int paramInt2);
    
    public abstract void onPageSelected(int paramInt);
  }
  
  public static abstract interface PageTransformer
  {
    public abstract void transformPage(View paramView, float paramFloat);
  }
  
  private class PagerObserver
    extends DataSetObserver
  {
    private PagerObserver() {}
    
    public void onChanged()
    {
      dataSetChanged();
    }
    
    public void onInvalidated()
    {
      dataSetChanged();
    }
  }
  
  public static class SavedState
    extends AbsSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator()
    {
      public ViewPager.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ViewPager.SavedState(paramAnonymousParcel, null);
      }
      
      public ViewPager.SavedState createFromParcel(Parcel paramAnonymousParcel, ClassLoader paramAnonymousClassLoader)
      {
        return new ViewPager.SavedState(paramAnonymousParcel, paramAnonymousClassLoader);
      }
      
      public ViewPager.SavedState[] newArray(int paramAnonymousInt)
      {
        return new ViewPager.SavedState[paramAnonymousInt];
      }
    };
    Parcelable adapterState;
    ClassLoader loader;
    int position;
    
    SavedState(Parcel paramParcel, ClassLoader paramClassLoader)
    {
      super(paramClassLoader);
      ClassLoader localClassLoader = paramClassLoader;
      if (paramClassLoader == null) {
        localClassLoader = getClass().getClassLoader();
      }
      position = paramParcel.readInt();
      adapterState = paramParcel.readParcelable(localClassLoader);
      loader = localClassLoader;
    }
    
    public SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("FragmentPager.SavedState{");
      localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      localStringBuilder.append(" position=");
      localStringBuilder.append(position);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(position);
      paramParcel.writeParcelable(adapterState, paramInt);
    }
  }
  
  public static class SimpleOnPageChangeListener
    implements ViewPager.OnPageChangeListener
  {
    public SimpleOnPageChangeListener() {}
    
    public void onPageScrollStateChanged(int paramInt) {}
    
    public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {}
    
    public void onPageSelected(int paramInt) {}
  }
  
  static class ViewPositionComparator
    implements Comparator<View>
  {
    ViewPositionComparator() {}
    
    public int compare(View paramView1, View paramView2)
    {
      paramView1 = (ViewPager.LayoutParams)paramView1.getLayoutParams();
      paramView2 = (ViewPager.LayoutParams)paramView2.getLayoutParams();
      if (isDecor != isDecor)
      {
        int i;
        if (isDecor) {
          i = 1;
        } else {
          i = -1;
        }
        return i;
      }
      return position - position;
    }
  }
}
