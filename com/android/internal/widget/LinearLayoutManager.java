package com.android.internal.widget;

import android.content.Context;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import com.android.internal.widget.helper.ItemTouchHelper.ViewDropHandler;
import java.util.List;

public class LinearLayoutManager
  extends RecyclerView.LayoutManager
  implements ItemTouchHelper.ViewDropHandler, RecyclerView.SmoothScroller.ScrollVectorProvider
{
  static final boolean DEBUG = false;
  public static final int HORIZONTAL = 0;
  public static final int INVALID_OFFSET = Integer.MIN_VALUE;
  private static final float MAX_SCROLL_FACTOR = 0.33333334F;
  private static final String TAG = "LinearLayoutManager";
  public static final int VERTICAL = 1;
  final AnchorInfo mAnchorInfo = new AnchorInfo();
  private int mInitialItemPrefetchCount = 2;
  private boolean mLastStackFromEnd;
  private final LayoutChunkResult mLayoutChunkResult = new LayoutChunkResult();
  private LayoutState mLayoutState;
  int mOrientation;
  OrientationHelper mOrientationHelper;
  SavedState mPendingSavedState = null;
  int mPendingScrollPosition = -1;
  int mPendingScrollPositionOffset = Integer.MIN_VALUE;
  private boolean mRecycleChildrenOnDetach;
  private boolean mReverseLayout = false;
  boolean mShouldReverseLayout = false;
  private boolean mSmoothScrollbarEnabled = true;
  private boolean mStackFromEnd = false;
  
  public LinearLayoutManager(Context paramContext)
  {
    this(paramContext, 1, false);
  }
  
  public LinearLayoutManager(Context paramContext, int paramInt, boolean paramBoolean)
  {
    setOrientation(paramInt);
    setReverseLayout(paramBoolean);
    setAutoMeasureEnabled(true);
  }
  
  public LinearLayoutManager(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    paramContext = getProperties(paramContext, paramAttributeSet, paramInt1, paramInt2);
    setOrientation(orientation);
    setReverseLayout(reverseLayout);
    setStackFromEnd(stackFromEnd);
    setAutoMeasureEnabled(true);
  }
  
  private int computeScrollExtent(RecyclerView.State paramState)
  {
    if (getChildCount() == 0) {
      return 0;
    }
    ensureLayoutState();
    return ScrollbarHelper.computeScrollExtent(paramState, mOrientationHelper, findFirstVisibleChildClosestToStart(mSmoothScrollbarEnabled ^ true, true), findFirstVisibleChildClosestToEnd(mSmoothScrollbarEnabled ^ true, true), this, mSmoothScrollbarEnabled);
  }
  
  private int computeScrollOffset(RecyclerView.State paramState)
  {
    if (getChildCount() == 0) {
      return 0;
    }
    ensureLayoutState();
    return ScrollbarHelper.computeScrollOffset(paramState, mOrientationHelper, findFirstVisibleChildClosestToStart(mSmoothScrollbarEnabled ^ true, true), findFirstVisibleChildClosestToEnd(mSmoothScrollbarEnabled ^ true, true), this, mSmoothScrollbarEnabled, mShouldReverseLayout);
  }
  
  private int computeScrollRange(RecyclerView.State paramState)
  {
    if (getChildCount() == 0) {
      return 0;
    }
    ensureLayoutState();
    return ScrollbarHelper.computeScrollRange(paramState, mOrientationHelper, findFirstVisibleChildClosestToStart(mSmoothScrollbarEnabled ^ true, true), findFirstVisibleChildClosestToEnd(mSmoothScrollbarEnabled ^ true, true), this, mSmoothScrollbarEnabled);
  }
  
  private View findFirstReferenceChild(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState)
  {
    return findReferenceChild(paramRecycler, paramState, 0, getChildCount(), paramState.getItemCount());
  }
  
  private View findFirstVisibleChildClosestToEnd(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (mShouldReverseLayout) {
      return findOneVisibleChild(0, getChildCount(), paramBoolean1, paramBoolean2);
    }
    return findOneVisibleChild(getChildCount() - 1, -1, paramBoolean1, paramBoolean2);
  }
  
  private View findFirstVisibleChildClosestToStart(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (mShouldReverseLayout) {
      return findOneVisibleChild(getChildCount() - 1, -1, paramBoolean1, paramBoolean2);
    }
    return findOneVisibleChild(0, getChildCount(), paramBoolean1, paramBoolean2);
  }
  
  private View findLastReferenceChild(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState)
  {
    return findReferenceChild(paramRecycler, paramState, getChildCount() - 1, -1, paramState.getItemCount());
  }
  
  private View findReferenceChildClosestToEnd(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState)
  {
    if (mShouldReverseLayout) {
      paramRecycler = findFirstReferenceChild(paramRecycler, paramState);
    } else {
      paramRecycler = findLastReferenceChild(paramRecycler, paramState);
    }
    return paramRecycler;
  }
  
  private View findReferenceChildClosestToStart(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState)
  {
    if (mShouldReverseLayout) {
      paramRecycler = findLastReferenceChild(paramRecycler, paramState);
    } else {
      paramRecycler = findFirstReferenceChild(paramRecycler, paramState);
    }
    return paramRecycler;
  }
  
  private int fixLayoutEndGap(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, boolean paramBoolean)
  {
    int i = mOrientationHelper.getEndAfterPadding() - paramInt;
    if (i > 0)
    {
      i = -scrollBy(-i, paramRecycler, paramState);
      if (paramBoolean)
      {
        paramInt = mOrientationHelper.getEndAfterPadding() - (paramInt + i);
        if (paramInt > 0)
        {
          mOrientationHelper.offsetChildren(paramInt);
          return paramInt + i;
        }
      }
      return i;
    }
    return 0;
  }
  
  private int fixLayoutStartGap(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, boolean paramBoolean)
  {
    int i = paramInt - mOrientationHelper.getStartAfterPadding();
    if (i > 0)
    {
      i = -scrollBy(i, paramRecycler, paramState);
      if (paramBoolean)
      {
        paramInt = paramInt + i - mOrientationHelper.getStartAfterPadding();
        if (paramInt > 0)
        {
          mOrientationHelper.offsetChildren(-paramInt);
          return i - paramInt;
        }
      }
      return i;
    }
    return 0;
  }
  
  private View getChildClosestToEnd()
  {
    int i;
    if (mShouldReverseLayout) {
      i = 0;
    } else {
      i = getChildCount() - 1;
    }
    return getChildAt(i);
  }
  
  private View getChildClosestToStart()
  {
    int i;
    if (mShouldReverseLayout) {
      i = getChildCount() - 1;
    } else {
      i = 0;
    }
    return getChildAt(i);
  }
  
  private void layoutForPredictiveAnimations(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt1, int paramInt2)
  {
    if ((paramState.willRunPredictiveAnimations()) && (getChildCount() != 0) && (!paramState.isPreLayout()) && (supportsPredictiveItemAnimations()))
    {
      List localList = paramRecycler.getScrapList();
      int i = localList.size();
      int j = getPosition(getChildAt(0));
      int k = 0;
      int m = 0;
      for (int n = 0; n < i; n++)
      {
        RecyclerView.ViewHolder localViewHolder = (RecyclerView.ViewHolder)localList.get(n);
        if (!localViewHolder.isRemoved())
        {
          int i1 = localViewHolder.getLayoutPosition();
          int i2 = 1;
          int i3;
          if (i1 < j) {
            i3 = 1;
          } else {
            i3 = 0;
          }
          if (i3 != mShouldReverseLayout) {
            i2 = -1;
          }
          if (i2 == -1) {
            m += mOrientationHelper.getDecoratedMeasurement(itemView);
          } else {
            k += mOrientationHelper.getDecoratedMeasurement(itemView);
          }
        }
      }
      mLayoutState.mScrapList = localList;
      if (m > 0)
      {
        updateLayoutStateToFillStart(getPosition(getChildClosestToStart()), paramInt1);
        mLayoutState.mExtra = m;
        mLayoutState.mAvailable = 0;
        mLayoutState.assignPositionFromScrapList();
        fill(paramRecycler, mLayoutState, paramState, false);
      }
      if (k > 0)
      {
        updateLayoutStateToFillEnd(getPosition(getChildClosestToEnd()), paramInt2);
        mLayoutState.mExtra = k;
        mLayoutState.mAvailable = 0;
        mLayoutState.assignPositionFromScrapList();
        fill(paramRecycler, mLayoutState, paramState, false);
      }
      mLayoutState.mScrapList = null;
      return;
    }
  }
  
  private void logChildren()
  {
    Log.d("LinearLayoutManager", "internal representation of views on the screen");
    for (int i = 0; i < getChildCount(); i++)
    {
      View localView = getChildAt(i);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("item ");
      localStringBuilder.append(getPosition(localView));
      localStringBuilder.append(", coord:");
      localStringBuilder.append(mOrientationHelper.getDecoratedStart(localView));
      Log.d("LinearLayoutManager", localStringBuilder.toString());
    }
    Log.d("LinearLayoutManager", "==============");
  }
  
  private void recycleByLayoutState(RecyclerView.Recycler paramRecycler, LayoutState paramLayoutState)
  {
    if ((mRecycle) && (!mInfinite))
    {
      if (mLayoutDirection == -1) {
        recycleViewsFromEnd(paramRecycler, mScrollingOffset);
      } else {
        recycleViewsFromStart(paramRecycler, mScrollingOffset);
      }
      return;
    }
  }
  
  private void recycleChildren(RecyclerView.Recycler paramRecycler, int paramInt1, int paramInt2)
  {
    if (paramInt1 == paramInt2) {
      return;
    }
    if (paramInt2 > paramInt1)
    {
      paramInt2--;
      while (paramInt2 >= paramInt1)
      {
        removeAndRecycleViewAt(paramInt2, paramRecycler);
        paramInt2--;
      }
    }
    while (paramInt1 > paramInt2)
    {
      removeAndRecycleViewAt(paramInt1, paramRecycler);
      paramInt1--;
    }
  }
  
  private void recycleViewsFromEnd(RecyclerView.Recycler paramRecycler, int paramInt)
  {
    int i = getChildCount();
    if (paramInt < 0) {
      return;
    }
    int j = mOrientationHelper.getEnd() - paramInt;
    View localView;
    if (mShouldReverseLayout)
    {
      for (paramInt = 0;; paramInt++)
      {
        if (paramInt >= i) {
          return;
        }
        localView = getChildAt(paramInt);
        if ((mOrientationHelper.getDecoratedStart(localView) < j) || (mOrientationHelper.getTransformedStartWithDecoration(localView) < j)) {
          break;
        }
      }
      recycleChildren(paramRecycler, 0, paramInt);
      return;
    }
    paramInt = i - 1;
    while (paramInt >= 0)
    {
      localView = getChildAt(paramInt);
      if ((mOrientationHelper.getDecoratedStart(localView) >= j) && (mOrientationHelper.getTransformedStartWithDecoration(localView) >= j))
      {
        paramInt--;
      }
      else
      {
        recycleChildren(paramRecycler, i - 1, paramInt);
        return;
      }
    }
  }
  
  private void recycleViewsFromStart(RecyclerView.Recycler paramRecycler, int paramInt)
  {
    if (paramInt < 0) {
      return;
    }
    int i = getChildCount();
    View localView;
    if (mShouldReverseLayout)
    {
      for (j = i - 1;; j--)
      {
        if (j < 0) {
          return;
        }
        localView = getChildAt(j);
        if ((mOrientationHelper.getDecoratedEnd(localView) > paramInt) || (mOrientationHelper.getTransformedEndWithDecoration(localView) > paramInt)) {
          break;
        }
      }
      recycleChildren(paramRecycler, i - 1, j);
      return;
    }
    int j = 0;
    while (j < i)
    {
      localView = getChildAt(j);
      if ((mOrientationHelper.getDecoratedEnd(localView) <= paramInt) && (mOrientationHelper.getTransformedEndWithDecoration(localView) <= paramInt))
      {
        j++;
      }
      else
      {
        recycleChildren(paramRecycler, 0, j);
        return;
      }
    }
  }
  
  private void resolveShouldLayoutReverse()
  {
    if ((mOrientation != 1) && (isLayoutRTL())) {
      mShouldReverseLayout = (mReverseLayout ^ true);
    } else {
      mShouldReverseLayout = mReverseLayout;
    }
  }
  
  private boolean updateAnchorFromChildren(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, AnchorInfo paramAnchorInfo)
  {
    int i = getChildCount();
    int j = 0;
    if (i == 0) {
      return false;
    }
    View localView = getFocusedChild();
    if ((localView != null) && (paramAnchorInfo.isViewValidAsAnchor(localView, paramState)))
    {
      paramAnchorInfo.assignFromViewAndKeepVisibleRect(localView);
      return true;
    }
    if (mLastStackFromEnd != mStackFromEnd) {
      return false;
    }
    if (mLayoutFromEnd) {
      paramRecycler = findReferenceChildClosestToEnd(paramRecycler, paramState);
    } else {
      paramRecycler = findReferenceChildClosestToStart(paramRecycler, paramState);
    }
    if (paramRecycler != null)
    {
      paramAnchorInfo.assignFromView(paramRecycler);
      if ((!paramState.isPreLayout()) && (supportsPredictiveItemAnimations()))
      {
        if ((mOrientationHelper.getDecoratedStart(paramRecycler) < mOrientationHelper.getEndAfterPadding()) && (mOrientationHelper.getDecoratedEnd(paramRecycler) >= mOrientationHelper.getStartAfterPadding())) {
          break label150;
        }
        j = 1;
        label150:
        if (j != 0)
        {
          if (mLayoutFromEnd) {
            j = mOrientationHelper.getEndAfterPadding();
          } else {
            j = mOrientationHelper.getStartAfterPadding();
          }
          mCoordinate = j;
        }
      }
      return true;
    }
    return false;
  }
  
  private boolean updateAnchorFromPendingData(RecyclerView.State paramState, AnchorInfo paramAnchorInfo)
  {
    boolean bool1 = paramState.isPreLayout();
    boolean bool2 = false;
    if ((!bool1) && (mPendingScrollPosition != -1))
    {
      if ((mPendingScrollPosition >= 0) && (mPendingScrollPosition < paramState.getItemCount()))
      {
        mPosition = mPendingScrollPosition;
        if ((mPendingSavedState != null) && (mPendingSavedState.hasValidAnchor()))
        {
          mLayoutFromEnd = mPendingSavedState.mAnchorLayoutFromEnd;
          if (mLayoutFromEnd) {
            mCoordinate = (mOrientationHelper.getEndAfterPadding() - mPendingSavedState.mAnchorOffset);
          } else {
            mCoordinate = (mOrientationHelper.getStartAfterPadding() + mPendingSavedState.mAnchorOffset);
          }
          return true;
        }
        if (mPendingScrollPositionOffset == Integer.MIN_VALUE)
        {
          paramState = findViewByPosition(mPendingScrollPosition);
          int i;
          if (paramState != null)
          {
            if (mOrientationHelper.getDecoratedMeasurement(paramState) > mOrientationHelper.getTotalSpace())
            {
              paramAnchorInfo.assignCoordinateFromPadding();
              return true;
            }
            if (mOrientationHelper.getDecoratedStart(paramState) - mOrientationHelper.getStartAfterPadding() < 0)
            {
              mCoordinate = mOrientationHelper.getStartAfterPadding();
              mLayoutFromEnd = false;
              return true;
            }
            if (mOrientationHelper.getEndAfterPadding() - mOrientationHelper.getDecoratedEnd(paramState) < 0)
            {
              mCoordinate = mOrientationHelper.getEndAfterPadding();
              mLayoutFromEnd = true;
              return true;
            }
            if (mLayoutFromEnd) {
              i = mOrientationHelper.getDecoratedEnd(paramState) + mOrientationHelper.getTotalSpaceChange();
            } else {
              i = mOrientationHelper.getDecoratedStart(paramState);
            }
            mCoordinate = i;
          }
          else
          {
            if (getChildCount() > 0)
            {
              i = getPosition(getChildAt(0));
              if (mPendingScrollPosition < i) {
                bool1 = true;
              } else {
                bool1 = false;
              }
              if (bool1 == mShouldReverseLayout) {
                bool2 = true;
              }
              mLayoutFromEnd = bool2;
            }
            paramAnchorInfo.assignCoordinateFromPadding();
          }
          return true;
        }
        mLayoutFromEnd = mShouldReverseLayout;
        if (mShouldReverseLayout) {
          mCoordinate = (mOrientationHelper.getEndAfterPadding() - mPendingScrollPositionOffset);
        } else {
          mCoordinate = (mOrientationHelper.getStartAfterPadding() + mPendingScrollPositionOffset);
        }
        return true;
      }
      mPendingScrollPosition = -1;
      mPendingScrollPositionOffset = Integer.MIN_VALUE;
      return false;
    }
    return false;
  }
  
  private void updateAnchorInfoForLayout(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, AnchorInfo paramAnchorInfo)
  {
    if (updateAnchorFromPendingData(paramState, paramAnchorInfo)) {
      return;
    }
    if (updateAnchorFromChildren(paramRecycler, paramState, paramAnchorInfo)) {
      return;
    }
    paramAnchorInfo.assignCoordinateFromPadding();
    int i;
    if (mStackFromEnd) {
      i = paramState.getItemCount() - 1;
    } else {
      i = 0;
    }
    mPosition = i;
  }
  
  private void updateLayoutState(int paramInt1, int paramInt2, boolean paramBoolean, RecyclerView.State paramState)
  {
    mLayoutState.mInfinite = resolveIsInfinite();
    mLayoutState.mExtra = getExtraLayoutSpace(paramState);
    mLayoutState.mLayoutDirection = paramInt1;
    int i = -1;
    LayoutState localLayoutState;
    if (paramInt1 == 1)
    {
      paramState = mLayoutState;
      mExtra += mOrientationHelper.getEndPadding();
      paramState = getChildClosestToEnd();
      localLayoutState = mLayoutState;
      if (!mShouldReverseLayout) {
        i = 1;
      }
      mItemDirection = i;
      mLayoutState.mCurrentPosition = (getPosition(paramState) + mLayoutState.mItemDirection);
      mLayoutState.mOffset = mOrientationHelper.getDecoratedEnd(paramState);
      paramInt1 = mOrientationHelper.getDecoratedEnd(paramState) - mOrientationHelper.getEndAfterPadding();
    }
    else
    {
      paramState = getChildClosestToStart();
      localLayoutState = mLayoutState;
      mExtra += mOrientationHelper.getStartAfterPadding();
      localLayoutState = mLayoutState;
      if (mShouldReverseLayout) {
        i = 1;
      }
      mItemDirection = i;
      mLayoutState.mCurrentPosition = (getPosition(paramState) + mLayoutState.mItemDirection);
      mLayoutState.mOffset = mOrientationHelper.getDecoratedStart(paramState);
      paramInt1 = -mOrientationHelper.getDecoratedStart(paramState) + mOrientationHelper.getStartAfterPadding();
    }
    mLayoutState.mAvailable = paramInt2;
    if (paramBoolean)
    {
      paramState = mLayoutState;
      mAvailable -= paramInt1;
    }
    mLayoutState.mScrollingOffset = paramInt1;
  }
  
  private void updateLayoutStateToFillEnd(int paramInt1, int paramInt2)
  {
    mLayoutState.mAvailable = (mOrientationHelper.getEndAfterPadding() - paramInt2);
    LayoutState localLayoutState = mLayoutState;
    int i;
    if (mShouldReverseLayout) {
      i = -1;
    } else {
      i = 1;
    }
    mItemDirection = i;
    mLayoutState.mCurrentPosition = paramInt1;
    mLayoutState.mLayoutDirection = 1;
    mLayoutState.mOffset = paramInt2;
    mLayoutState.mScrollingOffset = Integer.MIN_VALUE;
  }
  
  private void updateLayoutStateToFillEnd(AnchorInfo paramAnchorInfo)
  {
    updateLayoutStateToFillEnd(mPosition, mCoordinate);
  }
  
  private void updateLayoutStateToFillStart(int paramInt1, int paramInt2)
  {
    mLayoutState.mAvailable = (paramInt2 - mOrientationHelper.getStartAfterPadding());
    mLayoutState.mCurrentPosition = paramInt1;
    LayoutState localLayoutState = mLayoutState;
    if (mShouldReverseLayout) {
      paramInt1 = 1;
    } else {
      paramInt1 = -1;
    }
    mItemDirection = paramInt1;
    mLayoutState.mLayoutDirection = -1;
    mLayoutState.mOffset = paramInt2;
    mLayoutState.mScrollingOffset = Integer.MIN_VALUE;
  }
  
  private void updateLayoutStateToFillStart(AnchorInfo paramAnchorInfo)
  {
    updateLayoutStateToFillStart(mPosition, mCoordinate);
  }
  
  public void assertNotInLayoutOrScroll(String paramString)
  {
    if (mPendingSavedState == null) {
      super.assertNotInLayoutOrScroll(paramString);
    }
  }
  
  public boolean canScrollHorizontally()
  {
    boolean bool;
    if (mOrientation == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean canScrollVertically()
  {
    int i = mOrientation;
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    return bool;
  }
  
  public void collectAdjacentPrefetchPositions(int paramInt1, int paramInt2, RecyclerView.State paramState, RecyclerView.LayoutManager.LayoutPrefetchRegistry paramLayoutPrefetchRegistry)
  {
    if (mOrientation != 0) {
      paramInt1 = paramInt2;
    }
    if ((getChildCount() != 0) && (paramInt1 != 0))
    {
      if (paramInt1 > 0) {
        paramInt2 = 1;
      } else {
        paramInt2 = -1;
      }
      updateLayoutState(paramInt2, Math.abs(paramInt1), true, paramState);
      collectPrefetchPositionsForLayoutState(paramState, mLayoutState, paramLayoutPrefetchRegistry);
      return;
    }
  }
  
  public void collectInitialPrefetchPositions(int paramInt, RecyclerView.LayoutManager.LayoutPrefetchRegistry paramLayoutPrefetchRegistry)
  {
    SavedState localSavedState = mPendingSavedState;
    int i = -1;
    boolean bool;
    if ((localSavedState != null) && (mPendingSavedState.hasValidAnchor()))
    {
      bool = mPendingSavedState.mAnchorLayoutFromEnd;
      j = mPendingSavedState.mAnchorPosition;
    }
    for (;;)
    {
      break;
      resolveShouldLayoutReverse();
      bool = mShouldReverseLayout;
      if (mPendingScrollPosition == -1)
      {
        if (bool) {
          j = paramInt - 1;
        } else {
          j = 0;
        }
      }
      else {
        j = mPendingScrollPosition;
      }
    }
    if (!bool) {
      i = 1;
    }
    int k = 0;
    int m = j;
    for (int j = k; (j < mInitialItemPrefetchCount) && (m >= 0) && (m < paramInt); j++)
    {
      paramLayoutPrefetchRegistry.addPosition(m, 0);
      m += i;
    }
  }
  
  void collectPrefetchPositionsForLayoutState(RecyclerView.State paramState, LayoutState paramLayoutState, RecyclerView.LayoutManager.LayoutPrefetchRegistry paramLayoutPrefetchRegistry)
  {
    int i = mCurrentPosition;
    if ((i >= 0) && (i < paramState.getItemCount())) {
      paramLayoutPrefetchRegistry.addPosition(i, mScrollingOffset);
    }
  }
  
  public int computeHorizontalScrollExtent(RecyclerView.State paramState)
  {
    return computeScrollExtent(paramState);
  }
  
  public int computeHorizontalScrollOffset(RecyclerView.State paramState)
  {
    return computeScrollOffset(paramState);
  }
  
  public int computeHorizontalScrollRange(RecyclerView.State paramState)
  {
    return computeScrollRange(paramState);
  }
  
  public PointF computeScrollVectorForPosition(int paramInt)
  {
    if (getChildCount() == 0) {
      return null;
    }
    int i = 0;
    int j = getPosition(getChildAt(0));
    int k = 1;
    if (paramInt < j) {
      i = 1;
    }
    paramInt = k;
    if (i != mShouldReverseLayout) {
      paramInt = -1;
    }
    if (mOrientation == 0) {
      return new PointF(paramInt, 0.0F);
    }
    return new PointF(0.0F, paramInt);
  }
  
  public int computeVerticalScrollExtent(RecyclerView.State paramState)
  {
    return computeScrollExtent(paramState);
  }
  
  public int computeVerticalScrollOffset(RecyclerView.State paramState)
  {
    return computeScrollOffset(paramState);
  }
  
  public int computeVerticalScrollRange(RecyclerView.State paramState)
  {
    return computeScrollRange(paramState);
  }
  
  int convertFocusDirectionToLayoutDirection(int paramInt)
  {
    int i = -1;
    int j = Integer.MIN_VALUE;
    if (paramInt != 17)
    {
      if (paramInt != 33)
      {
        if (paramInt != 66)
        {
          if (paramInt != 130)
          {
            switch (paramInt)
            {
            default: 
              return Integer.MIN_VALUE;
            case 2: 
              if (mOrientation == 1) {
                return 1;
              }
              if (isLayoutRTL()) {
                return -1;
              }
              return 1;
            }
            if (mOrientation == 1) {
              return -1;
            }
            if (isLayoutRTL()) {
              return 1;
            }
            return -1;
          }
          if (mOrientation == 1) {
            j = 1;
          }
          return j;
        }
        if (mOrientation == 0) {
          j = 1;
        }
        return j;
      }
      if (mOrientation != 1) {
        i = Integer.MIN_VALUE;
      }
      return i;
    }
    if (mOrientation != 0) {
      i = Integer.MIN_VALUE;
    }
    return i;
  }
  
  LayoutState createLayoutState()
  {
    return new LayoutState();
  }
  
  void ensureLayoutState()
  {
    if (mLayoutState == null) {
      mLayoutState = createLayoutState();
    }
    if (mOrientationHelper == null) {
      mOrientationHelper = OrientationHelper.createOrientationHelper(this, mOrientation);
    }
  }
  
  int fill(RecyclerView.Recycler paramRecycler, LayoutState paramLayoutState, RecyclerView.State paramState, boolean paramBoolean)
  {
    int i = mAvailable;
    if (mScrollingOffset != Integer.MIN_VALUE)
    {
      if (mAvailable < 0) {
        mScrollingOffset += mAvailable;
      }
      recycleByLayoutState(paramRecycler, paramLayoutState);
    }
    int j = mAvailable + mExtra;
    LayoutChunkResult localLayoutChunkResult = mLayoutChunkResult;
    do
    {
      int k;
      do
      {
        if (((!mInfinite) && (j <= 0)) || (!paramLayoutState.hasMore(paramState))) {
          break;
        }
        localLayoutChunkResult.resetInternal();
        layoutChunk(paramRecycler, paramState, paramLayoutState, localLayoutChunkResult);
        if (mFinished) {
          break;
        }
        mOffset += mConsumed * mLayoutDirection;
        if ((mIgnoreConsumed) && (mLayoutState.mScrapList == null))
        {
          k = j;
          if (paramState.isPreLayout()) {}
        }
        else
        {
          mAvailable -= mConsumed;
          k = j - mConsumed;
        }
        if (mScrollingOffset != Integer.MIN_VALUE)
        {
          mScrollingOffset += mConsumed;
          if (mAvailable < 0) {
            mScrollingOffset += mAvailable;
          }
          recycleByLayoutState(paramRecycler, paramLayoutState);
        }
        j = k;
      } while (!paramBoolean);
      j = k;
    } while (!mFocusable);
    return i - mAvailable;
  }
  
  public int findFirstCompletelyVisibleItemPosition()
  {
    View localView = findOneVisibleChild(0, getChildCount(), true, false);
    int i;
    if (localView == null) {
      i = -1;
    } else {
      i = getPosition(localView);
    }
    return i;
  }
  
  public int findFirstVisibleItemPosition()
  {
    View localView = findOneVisibleChild(0, getChildCount(), false, true);
    int i;
    if (localView == null) {
      i = -1;
    } else {
      i = getPosition(localView);
    }
    return i;
  }
  
  public int findLastCompletelyVisibleItemPosition()
  {
    int i = getChildCount();
    int j = -1;
    View localView = findOneVisibleChild(i - 1, -1, true, false);
    if (localView != null) {
      j = getPosition(localView);
    }
    return j;
  }
  
  public int findLastVisibleItemPosition()
  {
    int i = getChildCount();
    int j = -1;
    View localView = findOneVisibleChild(i - 1, -1, false, true);
    if (localView != null) {
      j = getPosition(localView);
    }
    return j;
  }
  
  View findOneVisibleChild(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
  {
    ensureLayoutState();
    int i = mOrientationHelper.getStartAfterPadding();
    int j = mOrientationHelper.getEndAfterPadding();
    int k;
    if (paramInt2 > paramInt1) {
      k = 1;
    } else {
      k = -1;
    }
    Object localObject2;
    for (Object localObject1 = null; paramInt1 != paramInt2; localObject1 = localObject2)
    {
      View localView = getChildAt(paramInt1);
      int m = mOrientationHelper.getDecoratedStart(localView);
      int n = mOrientationHelper.getDecoratedEnd(localView);
      localObject2 = localObject1;
      if (m < j)
      {
        localObject2 = localObject1;
        if (n > i) {
          if (paramBoolean1)
          {
            if ((m >= i) && (n <= j)) {
              return localView;
            }
            localObject2 = localObject1;
            if (paramBoolean2)
            {
              localObject2 = localObject1;
              if (localObject1 == null) {
                localObject2 = localView;
              }
            }
          }
          else
          {
            return localView;
          }
        }
      }
      paramInt1 += k;
    }
    return localObject1;
  }
  
  View findReferenceChild(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, int paramInt1, int paramInt2, int paramInt3)
  {
    ensureLayoutState();
    paramState = null;
    int i = mOrientationHelper.getStartAfterPadding();
    int j = mOrientationHelper.getEndAfterPadding();
    int k;
    if (paramInt2 > paramInt1) {
      k = 1;
    } else {
      k = -1;
    }
    Object localObject2;
    for (paramRecycler = null; paramInt1 != paramInt2; paramRecycler = (RecyclerView.Recycler)localObject2)
    {
      View localView = getChildAt(paramInt1);
      int m = getPosition(localView);
      Object localObject1 = paramState;
      localObject2 = paramRecycler;
      if (m >= 0)
      {
        localObject1 = paramState;
        localObject2 = paramRecycler;
        if (m < paramInt3) {
          if (((RecyclerView.LayoutParams)localView.getLayoutParams()).isItemRemoved())
          {
            localObject1 = paramState;
            localObject2 = paramRecycler;
            if (paramRecycler == null)
            {
              localObject2 = localView;
              localObject1 = paramState;
            }
          }
          else
          {
            if ((mOrientationHelper.getDecoratedStart(localView) < j) && (mOrientationHelper.getDecoratedEnd(localView) >= i)) {
              return localView;
            }
            localObject1 = paramState;
            localObject2 = paramRecycler;
            if (paramState == null)
            {
              localObject1 = localView;
              localObject2 = paramRecycler;
            }
          }
        }
      }
      paramInt1 += k;
      paramState = (RecyclerView.State)localObject1;
    }
    if (paramState == null) {
      paramState = paramRecycler;
    }
    return paramState;
  }
  
  public View findViewByPosition(int paramInt)
  {
    int i = getChildCount();
    if (i == 0) {
      return null;
    }
    int j = paramInt - getPosition(getChildAt(0));
    if ((j >= 0) && (j < i))
    {
      View localView = getChildAt(j);
      if (getPosition(localView) == paramInt) {
        return localView;
      }
    }
    return super.findViewByPosition(paramInt);
  }
  
  public RecyclerView.LayoutParams generateDefaultLayoutParams()
  {
    return new RecyclerView.LayoutParams(-2, -2);
  }
  
  protected int getExtraLayoutSpace(RecyclerView.State paramState)
  {
    if (paramState.hasTargetScrollPosition()) {
      return mOrientationHelper.getTotalSpace();
    }
    return 0;
  }
  
  public int getInitialItemPrefetchCount()
  {
    return mInitialItemPrefetchCount;
  }
  
  public int getOrientation()
  {
    return mOrientation;
  }
  
  public boolean getRecycleChildrenOnDetach()
  {
    return mRecycleChildrenOnDetach;
  }
  
  public boolean getReverseLayout()
  {
    return mReverseLayout;
  }
  
  public boolean getStackFromEnd()
  {
    return mStackFromEnd;
  }
  
  protected boolean isLayoutRTL()
  {
    int i = getLayoutDirection();
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isSmoothScrollbarEnabled()
  {
    return mSmoothScrollbarEnabled;
  }
  
  void layoutChunk(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, LayoutState paramLayoutState, LayoutChunkResult paramLayoutChunkResult)
  {
    paramRecycler = paramLayoutState.next(paramRecycler);
    if (paramRecycler == null)
    {
      mFinished = true;
      return;
    }
    paramState = (RecyclerView.LayoutParams)paramRecycler.getLayoutParams();
    boolean bool1;
    boolean bool2;
    if (mScrapList == null)
    {
      bool1 = mShouldReverseLayout;
      if (mLayoutDirection == -1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      if (bool1 == bool2) {
        addView(paramRecycler);
      } else {
        addView(paramRecycler, 0);
      }
    }
    else
    {
      bool1 = mShouldReverseLayout;
      if (mLayoutDirection == -1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      if (bool1 == bool2) {
        addDisappearingView(paramRecycler);
      } else {
        addDisappearingView(paramRecycler, 0);
      }
    }
    measureChildWithMargins(paramRecycler, 0, 0);
    mConsumed = mOrientationHelper.getDecoratedMeasurement(paramRecycler);
    int i;
    int j;
    int k;
    int m;
    int n;
    int i1;
    if (mOrientation == 1)
    {
      if (isLayoutRTL())
      {
        i = getWidth() - getPaddingRight();
        j = i - mOrientationHelper.getDecoratedMeasurementInOther(paramRecycler);
      }
      else
      {
        j = getPaddingLeft();
        i = mOrientationHelper.getDecoratedMeasurementInOther(paramRecycler) + j;
      }
      if (mLayoutDirection == -1)
      {
        k = mOffset;
        m = mOffset;
        n = mConsumed;
        i1 = j;
        n = m - n;
        j = i;
        i = i1;
        i1 = n;
      }
      else
      {
        i1 = mOffset;
        n = mOffset;
        m = mConsumed;
        k = j;
        n += m;
        j = i;
        i = k;
        k = n;
      }
    }
    else
    {
      i1 = getPaddingTop();
      j = mOrientationHelper.getDecoratedMeasurementInOther(paramRecycler) + i1;
      if (mLayoutDirection == -1)
      {
        i = mOffset;
        m = mOffset;
        n = mConsumed;
        k = j;
        n = m - n;
        j = i;
        i = n;
      }
      else
      {
        i = mOffset;
        n = mOffset;
        k = mConsumed;
        n += k;
        k = j;
        j = n;
      }
    }
    layoutDecoratedWithMargins(paramRecycler, i, i1, j, k);
    if ((paramState.isItemRemoved()) || (paramState.isItemChanged())) {
      mIgnoreConsumed = true;
    }
    mFocusable = paramRecycler.isFocusable();
  }
  
  void onAnchorReady(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState, AnchorInfo paramAnchorInfo, int paramInt) {}
  
  public void onDetachedFromWindow(RecyclerView paramRecyclerView, RecyclerView.Recycler paramRecycler)
  {
    super.onDetachedFromWindow(paramRecyclerView, paramRecycler);
    if (mRecycleChildrenOnDetach)
    {
      removeAndRecycleAllViews(paramRecycler);
      paramRecycler.clear();
    }
  }
  
  public View onFocusSearchFailed(View paramView, int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState)
  {
    resolveShouldLayoutReverse();
    if (getChildCount() == 0) {
      return null;
    }
    paramInt = convertFocusDirectionToLayoutDirection(paramInt);
    if (paramInt == Integer.MIN_VALUE) {
      return null;
    }
    ensureLayoutState();
    if (paramInt == -1) {
      paramView = findReferenceChildClosestToStart(paramRecycler, paramState);
    } else {
      paramView = findReferenceChildClosestToEnd(paramRecycler, paramState);
    }
    if (paramView == null) {
      return null;
    }
    ensureLayoutState();
    updateLayoutState(paramInt, (int)(0.33333334F * mOrientationHelper.getTotalSpace()), false, paramState);
    mLayoutState.mScrollingOffset = Integer.MIN_VALUE;
    mLayoutState.mRecycle = false;
    fill(paramRecycler, mLayoutState, paramState, true);
    if (paramInt == -1) {
      paramRecycler = getChildClosestToStart();
    } else {
      paramRecycler = getChildClosestToEnd();
    }
    if ((paramRecycler != paramView) && (paramRecycler.isFocusable())) {
      return paramRecycler;
    }
    return null;
  }
  
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
    if (getChildCount() > 0)
    {
      paramAccessibilityEvent.setFromIndex(findFirstVisibleItemPosition());
      paramAccessibilityEvent.setToIndex(findLastVisibleItemPosition());
    }
  }
  
  public void onLayoutChildren(RecyclerView.Recycler paramRecycler, RecyclerView.State paramState)
  {
    Object localObject = mPendingSavedState;
    int i = -1;
    if (((localObject != null) || (mPendingScrollPosition != -1)) && (paramState.getItemCount() == 0))
    {
      removeAndRecycleAllViews(paramRecycler);
      return;
    }
    if ((mPendingSavedState != null) && (mPendingSavedState.hasValidAnchor())) {
      mPendingScrollPosition = mPendingSavedState.mAnchorPosition;
    }
    ensureLayoutState();
    mLayoutState.mRecycle = false;
    resolveShouldLayoutReverse();
    if ((!mAnchorInfo.mValid) || (mPendingScrollPosition != -1) || (mPendingSavedState != null))
    {
      mAnchorInfo.reset();
      mAnchorInfo.mLayoutFromEnd = (mShouldReverseLayout ^ mStackFromEnd);
      updateAnchorInfoForLayout(paramRecycler, paramState, mAnchorInfo);
      mAnchorInfo.mValid = true;
    }
    int j = getExtraLayoutSpace(paramState);
    if (mLayoutState.mLastScrollDelta >= 0)
    {
      k = 0;
    }
    else
    {
      k = j;
      j = 0;
    }
    int m = k + mOrientationHelper.getStartAfterPadding();
    int n = j + mOrientationHelper.getEndPadding();
    j = n;
    int k = m;
    if (paramState.isPreLayout())
    {
      j = n;
      k = m;
      if (mPendingScrollPosition != -1)
      {
        j = n;
        k = m;
        if (mPendingScrollPositionOffset != Integer.MIN_VALUE)
        {
          localObject = findViewByPosition(mPendingScrollPosition);
          j = n;
          k = m;
          if (localObject != null)
          {
            if (mShouldReverseLayout)
            {
              j = mOrientationHelper.getEndAfterPadding() - mOrientationHelper.getDecoratedEnd((View)localObject) - mPendingScrollPositionOffset;
            }
            else
            {
              k = mOrientationHelper.getDecoratedStart((View)localObject);
              j = mOrientationHelper.getStartAfterPadding();
              j = mPendingScrollPositionOffset - (k - j);
            }
            if (j > 0)
            {
              k = m + j;
              j = n;
            }
            else
            {
              j = n - j;
              k = m;
            }
          }
        }
      }
    }
    if (mAnchorInfo.mLayoutFromEnd)
    {
      if (mShouldReverseLayout) {
        i = 1;
      }
    }
    else if (!mShouldReverseLayout) {
      i = 1;
    }
    onAnchorReady(paramRecycler, paramState, mAnchorInfo, i);
    detachAndScrapAttachedViews(paramRecycler);
    mLayoutState.mInfinite = resolveIsInfinite();
    mLayoutState.mIsPreLayout = paramState.isPreLayout();
    if (mAnchorInfo.mLayoutFromEnd)
    {
      updateLayoutStateToFillStart(mAnchorInfo);
      mLayoutState.mExtra = k;
      fill(paramRecycler, mLayoutState, paramState, false);
      i = mLayoutState.mOffset;
      n = mLayoutState.mCurrentPosition;
      k = j;
      if (mLayoutState.mAvailable > 0) {
        k = j + mLayoutState.mAvailable;
      }
      updateLayoutStateToFillEnd(mAnchorInfo);
      mLayoutState.mExtra = k;
      localObject = mLayoutState;
      mCurrentPosition += mLayoutState.mItemDirection;
      fill(paramRecycler, mLayoutState, paramState, false);
      m = mLayoutState.mOffset;
      j = i;
      if (mLayoutState.mAvailable > 0)
      {
        j = mLayoutState.mAvailable;
        updateLayoutStateToFillStart(n, i);
        mLayoutState.mExtra = j;
        fill(paramRecycler, mLayoutState, paramState, false);
        j = mLayoutState.mOffset;
      }
      k = j;
      j = m;
    }
    else
    {
      updateLayoutStateToFillEnd(mAnchorInfo);
      mLayoutState.mExtra = j;
      fill(paramRecycler, mLayoutState, paramState, false);
      i = mLayoutState.mOffset;
      n = mLayoutState.mCurrentPosition;
      j = k;
      if (mLayoutState.mAvailable > 0) {
        j = k + mLayoutState.mAvailable;
      }
      updateLayoutStateToFillStart(mAnchorInfo);
      mLayoutState.mExtra = j;
      localObject = mLayoutState;
      mCurrentPosition += mLayoutState.mItemDirection;
      fill(paramRecycler, mLayoutState, paramState, false);
      m = mLayoutState.mOffset;
      k = m;
      j = i;
      if (mLayoutState.mAvailable > 0)
      {
        j = mLayoutState.mAvailable;
        updateLayoutStateToFillEnd(n, i);
        mLayoutState.mExtra = j;
        fill(paramRecycler, mLayoutState, paramState, false);
        j = mLayoutState.mOffset;
        k = m;
      }
    }
    i = k;
    m = j;
    if (getChildCount() > 0) {
      if ((mShouldReverseLayout ^ mStackFromEnd))
      {
        m = fixLayoutEndGap(j, paramRecycler, paramState, true);
        i = k + m;
        k = fixLayoutStartGap(i, paramRecycler, paramState, false);
        i += k;
        m = j + m + k;
      }
      else
      {
        i = fixLayoutStartGap(k, paramRecycler, paramState, true);
        m = j + i;
        j = fixLayoutEndGap(m, paramRecycler, paramState, false);
        i = k + i + j;
        m += j;
      }
    }
    layoutForPredictiveAnimations(paramRecycler, paramState, i, m);
    if (!paramState.isPreLayout()) {
      mOrientationHelper.onLayoutComplete();
    } else {
      mAnchorInfo.reset();
    }
    mLastStackFromEnd = mStackFromEnd;
  }
  
  public void onLayoutCompleted(RecyclerView.State paramState)
  {
    super.onLayoutCompleted(paramState);
    mPendingSavedState = null;
    mPendingScrollPosition = -1;
    mPendingScrollPositionOffset = Integer.MIN_VALUE;
    mAnchorInfo.reset();
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if ((paramParcelable instanceof SavedState))
    {
      mPendingSavedState = ((SavedState)paramParcelable);
      requestLayout();
    }
  }
  
  public Parcelable onSaveInstanceState()
  {
    if (mPendingSavedState != null) {
      return new SavedState(mPendingSavedState);
    }
    SavedState localSavedState = new SavedState();
    if (getChildCount() > 0)
    {
      ensureLayoutState();
      boolean bool = mLastStackFromEnd ^ mShouldReverseLayout;
      mAnchorLayoutFromEnd = bool;
      View localView;
      if (bool)
      {
        localView = getChildClosestToEnd();
        mAnchorOffset = (mOrientationHelper.getEndAfterPadding() - mOrientationHelper.getDecoratedEnd(localView));
        mAnchorPosition = getPosition(localView);
      }
      else
      {
        localView = getChildClosestToStart();
        mAnchorPosition = getPosition(localView);
        mAnchorOffset = (mOrientationHelper.getDecoratedStart(localView) - mOrientationHelper.getStartAfterPadding());
      }
    }
    else
    {
      localSavedState.invalidateAnchor();
    }
    return localSavedState;
  }
  
  public void prepareForDrop(View paramView1, View paramView2, int paramInt1, int paramInt2)
  {
    assertNotInLayoutOrScroll("Cannot drop a view during a scroll or layout calculation");
    ensureLayoutState();
    resolveShouldLayoutReverse();
    paramInt1 = getPosition(paramView1);
    paramInt2 = getPosition(paramView2);
    if (paramInt1 < paramInt2) {
      paramInt1 = 1;
    } else {
      paramInt1 = -1;
    }
    if (mShouldReverseLayout)
    {
      if (paramInt1 == 1) {
        scrollToPositionWithOffset(paramInt2, mOrientationHelper.getEndAfterPadding() - (mOrientationHelper.getDecoratedStart(paramView2) + mOrientationHelper.getDecoratedMeasurement(paramView1)));
      } else {
        scrollToPositionWithOffset(paramInt2, mOrientationHelper.getEndAfterPadding() - mOrientationHelper.getDecoratedEnd(paramView2));
      }
    }
    else if (paramInt1 == -1) {
      scrollToPositionWithOffset(paramInt2, mOrientationHelper.getDecoratedStart(paramView2));
    } else {
      scrollToPositionWithOffset(paramInt2, mOrientationHelper.getDecoratedEnd(paramView2) - mOrientationHelper.getDecoratedMeasurement(paramView1));
    }
  }
  
  boolean resolveIsInfinite()
  {
    boolean bool;
    if ((mOrientationHelper.getMode() == 0) && (mOrientationHelper.getEnd() == 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  int scrollBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState)
  {
    if ((getChildCount() != 0) && (paramInt != 0))
    {
      mLayoutState.mRecycle = true;
      ensureLayoutState();
      int i;
      if (paramInt > 0) {
        i = 1;
      } else {
        i = -1;
      }
      int j = Math.abs(paramInt);
      updateLayoutState(i, j, true, paramState);
      int k = mLayoutState.mScrollingOffset + fill(paramRecycler, mLayoutState, paramState, false);
      if (k < 0) {
        return 0;
      }
      if (j > k) {
        paramInt = i * k;
      }
      mOrientationHelper.offsetChildren(-paramInt);
      mLayoutState.mLastScrollDelta = paramInt;
      return paramInt;
    }
    return 0;
  }
  
  public int scrollHorizontallyBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState)
  {
    if (mOrientation == 1) {
      return 0;
    }
    return scrollBy(paramInt, paramRecycler, paramState);
  }
  
  public void scrollToPosition(int paramInt)
  {
    mPendingScrollPosition = paramInt;
    mPendingScrollPositionOffset = Integer.MIN_VALUE;
    if (mPendingSavedState != null) {
      mPendingSavedState.invalidateAnchor();
    }
    requestLayout();
  }
  
  public void scrollToPositionWithOffset(int paramInt1, int paramInt2)
  {
    mPendingScrollPosition = paramInt1;
    mPendingScrollPositionOffset = paramInt2;
    if (mPendingSavedState != null) {
      mPendingSavedState.invalidateAnchor();
    }
    requestLayout();
  }
  
  public int scrollVerticallyBy(int paramInt, RecyclerView.Recycler paramRecycler, RecyclerView.State paramState)
  {
    if (mOrientation == 0) {
      return 0;
    }
    return scrollBy(paramInt, paramRecycler, paramState);
  }
  
  public void setInitialPrefetchItemCount(int paramInt)
  {
    mInitialItemPrefetchCount = paramInt;
  }
  
  public void setOrientation(int paramInt)
  {
    if ((paramInt != 0) && (paramInt != 1))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("invalid orientation:");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    assertNotInLayoutOrScroll(null);
    if (paramInt == mOrientation) {
      return;
    }
    mOrientation = paramInt;
    mOrientationHelper = null;
    requestLayout();
  }
  
  public void setRecycleChildrenOnDetach(boolean paramBoolean)
  {
    mRecycleChildrenOnDetach = paramBoolean;
  }
  
  public void setReverseLayout(boolean paramBoolean)
  {
    assertNotInLayoutOrScroll(null);
    if (paramBoolean == mReverseLayout) {
      return;
    }
    mReverseLayout = paramBoolean;
    requestLayout();
  }
  
  public void setSmoothScrollbarEnabled(boolean paramBoolean)
  {
    mSmoothScrollbarEnabled = paramBoolean;
  }
  
  public void setStackFromEnd(boolean paramBoolean)
  {
    assertNotInLayoutOrScroll(null);
    if (mStackFromEnd == paramBoolean) {
      return;
    }
    mStackFromEnd = paramBoolean;
    requestLayout();
  }
  
  boolean shouldMeasureTwice()
  {
    boolean bool;
    if ((getHeightMode() != 1073741824) && (getWidthMode() != 1073741824) && (hasFlexibleChildInBothOrientations())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void smoothScrollToPosition(RecyclerView paramRecyclerView, RecyclerView.State paramState, int paramInt)
  {
    paramRecyclerView = new LinearSmoothScroller(paramRecyclerView.getContext());
    paramRecyclerView.setTargetPosition(paramInt);
    startSmoothScroll(paramRecyclerView);
  }
  
  public boolean supportsPredictiveItemAnimations()
  {
    boolean bool;
    if ((mPendingSavedState == null) && (mLastStackFromEnd == mStackFromEnd)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  void validateChildOrder()
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("validating child count ");
    ((StringBuilder)localObject).append(getChildCount());
    Log.d("LinearLayoutManager", ((StringBuilder)localObject).toString());
    if (getChildCount() < 1) {
      return;
    }
    boolean bool1 = false;
    boolean bool2 = false;
    int i = getPosition(getChildAt(0));
    int j = mOrientationHelper.getDecoratedStart(getChildAt(0));
    int m;
    int n;
    if (mShouldReverseLayout)
    {
      for (k = 1;; k++)
      {
        if (k >= getChildCount()) {
          return;
        }
        localObject = getChildAt(k);
        m = getPosition((View)localObject);
        n = mOrientationHelper.getDecoratedStart((View)localObject);
        if (m < i)
        {
          logChildren();
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("detected invalid position. loc invalid? ");
          if (n < j) {
            bool2 = true;
          }
          ((StringBuilder)localObject).append(bool2);
          throw new RuntimeException(((StringBuilder)localObject).toString());
        }
        if (n > j) {
          break;
        }
      }
      logChildren();
      throw new RuntimeException("detected invalid location");
    }
    int k = 1;
    while (k < getChildCount())
    {
      localObject = getChildAt(k);
      n = getPosition((View)localObject);
      m = mOrientationHelper.getDecoratedStart((View)localObject);
      if (n < i)
      {
        logChildren();
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("detected invalid position. loc invalid? ");
        bool2 = bool1;
        if (m < j) {
          bool2 = true;
        }
        ((StringBuilder)localObject).append(bool2);
        throw new RuntimeException(((StringBuilder)localObject).toString());
      }
      if (m >= j)
      {
        k++;
      }
      else
      {
        logChildren();
        throw new RuntimeException("detected invalid location");
      }
    }
  }
  
  class AnchorInfo
  {
    int mCoordinate;
    boolean mLayoutFromEnd;
    int mPosition;
    boolean mValid;
    
    AnchorInfo()
    {
      reset();
    }
    
    void assignCoordinateFromPadding()
    {
      int i;
      if (mLayoutFromEnd) {
        i = mOrientationHelper.getEndAfterPadding();
      } else {
        i = mOrientationHelper.getStartAfterPadding();
      }
      mCoordinate = i;
    }
    
    public void assignFromView(View paramView)
    {
      if (mLayoutFromEnd) {
        mCoordinate = (mOrientationHelper.getDecoratedEnd(paramView) + mOrientationHelper.getTotalSpaceChange());
      } else {
        mCoordinate = mOrientationHelper.getDecoratedStart(paramView);
      }
      mPosition = getPosition(paramView);
    }
    
    public void assignFromViewAndKeepVisibleRect(View paramView)
    {
      int i = mOrientationHelper.getTotalSpaceChange();
      if (i >= 0)
      {
        assignFromView(paramView);
        return;
      }
      mPosition = getPosition(paramView);
      int j;
      int k;
      int m;
      int n;
      if (mLayoutFromEnd)
      {
        j = mOrientationHelper.getEndAfterPadding() - i - mOrientationHelper.getDecoratedEnd(paramView);
        mCoordinate = (mOrientationHelper.getEndAfterPadding() - j);
        if (j > 0)
        {
          k = mOrientationHelper.getDecoratedMeasurement(paramView);
          m = mCoordinate;
          n = mOrientationHelper.getStartAfterPadding();
          n = m - k - (Math.min(mOrientationHelper.getDecoratedStart(paramView) - n, 0) + n);
          if (n < 0) {
            mCoordinate += Math.min(j, -n);
          }
        }
      }
      else
      {
        n = mOrientationHelper.getDecoratedStart(paramView);
        j = n - mOrientationHelper.getStartAfterPadding();
        mCoordinate = n;
        if (j > 0)
        {
          m = mOrientationHelper.getDecoratedMeasurement(paramView);
          int i1 = mOrientationHelper.getEndAfterPadding();
          k = mOrientationHelper.getDecoratedEnd(paramView);
          n = mOrientationHelper.getEndAfterPadding() - Math.min(0, i1 - i - k) - (m + n);
          if (n < 0) {
            mCoordinate -= Math.min(j, -n);
          }
        }
      }
    }
    
    boolean isViewValidAsAnchor(View paramView, RecyclerView.State paramState)
    {
      paramView = (RecyclerView.LayoutParams)paramView.getLayoutParams();
      boolean bool;
      if ((!paramView.isItemRemoved()) && (paramView.getViewLayoutPosition() >= 0) && (paramView.getViewLayoutPosition() < paramState.getItemCount())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    void reset()
    {
      mPosition = -1;
      mCoordinate = Integer.MIN_VALUE;
      mLayoutFromEnd = false;
      mValid = false;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("AnchorInfo{mPosition=");
      localStringBuilder.append(mPosition);
      localStringBuilder.append(", mCoordinate=");
      localStringBuilder.append(mCoordinate);
      localStringBuilder.append(", mLayoutFromEnd=");
      localStringBuilder.append(mLayoutFromEnd);
      localStringBuilder.append(", mValid=");
      localStringBuilder.append(mValid);
      localStringBuilder.append('}');
      return localStringBuilder.toString();
    }
  }
  
  protected static class LayoutChunkResult
  {
    public int mConsumed;
    public boolean mFinished;
    public boolean mFocusable;
    public boolean mIgnoreConsumed;
    
    protected LayoutChunkResult() {}
    
    void resetInternal()
    {
      mConsumed = 0;
      mFinished = false;
      mIgnoreConsumed = false;
      mFocusable = false;
    }
  }
  
  static class LayoutState
  {
    static final int INVALID_LAYOUT = Integer.MIN_VALUE;
    static final int ITEM_DIRECTION_HEAD = -1;
    static final int ITEM_DIRECTION_TAIL = 1;
    static final int LAYOUT_END = 1;
    static final int LAYOUT_START = -1;
    static final int SCROLLING_OFFSET_NaN = Integer.MIN_VALUE;
    static final String TAG = "LLM#LayoutState";
    int mAvailable;
    int mCurrentPosition;
    int mExtra = 0;
    boolean mInfinite;
    boolean mIsPreLayout = false;
    int mItemDirection;
    int mLastScrollDelta;
    int mLayoutDirection;
    int mOffset;
    boolean mRecycle = true;
    List<RecyclerView.ViewHolder> mScrapList = null;
    int mScrollingOffset;
    
    LayoutState() {}
    
    private View nextViewFromScrapList()
    {
      int i = mScrapList.size();
      for (int j = 0; j < i; j++)
      {
        View localView = mScrapList.get(j)).itemView;
        RecyclerView.LayoutParams localLayoutParams = (RecyclerView.LayoutParams)localView.getLayoutParams();
        if ((!localLayoutParams.isItemRemoved()) && (mCurrentPosition == localLayoutParams.getViewLayoutPosition()))
        {
          assignPositionFromScrapList(localView);
          return localView;
        }
      }
      return null;
    }
    
    public void assignPositionFromScrapList()
    {
      assignPositionFromScrapList(null);
    }
    
    public void assignPositionFromScrapList(View paramView)
    {
      paramView = nextViewInLimitedList(paramView);
      if (paramView == null) {
        mCurrentPosition = -1;
      } else {
        mCurrentPosition = ((RecyclerView.LayoutParams)paramView.getLayoutParams()).getViewLayoutPosition();
      }
    }
    
    boolean hasMore(RecyclerView.State paramState)
    {
      boolean bool;
      if ((mCurrentPosition >= 0) && (mCurrentPosition < paramState.getItemCount())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    void log()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("avail:");
      localStringBuilder.append(mAvailable);
      localStringBuilder.append(", ind:");
      localStringBuilder.append(mCurrentPosition);
      localStringBuilder.append(", dir:");
      localStringBuilder.append(mItemDirection);
      localStringBuilder.append(", offset:");
      localStringBuilder.append(mOffset);
      localStringBuilder.append(", layoutDir:");
      localStringBuilder.append(mLayoutDirection);
      Log.d("LLM#LayoutState", localStringBuilder.toString());
    }
    
    View next(RecyclerView.Recycler paramRecycler)
    {
      if (mScrapList != null) {
        return nextViewFromScrapList();
      }
      paramRecycler = paramRecycler.getViewForPosition(mCurrentPosition);
      mCurrentPosition += mItemDirection;
      return paramRecycler;
    }
    
    public View nextViewInLimitedList(View paramView)
    {
      int i = mScrapList.size();
      Object localObject1 = null;
      int j = Integer.MAX_VALUE;
      int k = 0;
      Object localObject2;
      for (;;)
      {
        localObject2 = localObject1;
        if (k >= i) {
          break;
        }
        View localView = mScrapList.get(k)).itemView;
        RecyclerView.LayoutParams localLayoutParams = (RecyclerView.LayoutParams)localView.getLayoutParams();
        localObject2 = localObject1;
        int m = j;
        if (localView != paramView) {
          if (localLayoutParams.isItemRemoved())
          {
            localObject2 = localObject1;
            m = j;
          }
          else
          {
            int n = (localLayoutParams.getViewLayoutPosition() - mCurrentPosition) * mItemDirection;
            if (n < 0)
            {
              localObject2 = localObject1;
              m = j;
            }
            else
            {
              localObject2 = localObject1;
              m = j;
              if (n < j)
              {
                localObject1 = localView;
                m = n;
                localObject2 = localObject1;
                if (n == 0)
                {
                  localObject2 = localObject1;
                  break;
                }
              }
            }
          }
        }
        k++;
        localObject1 = localObject2;
        j = m;
      }
      return localObject2;
    }
  }
  
  public static class SavedState
    implements Parcelable
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public LinearLayoutManager.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new LinearLayoutManager.SavedState(paramAnonymousParcel);
      }
      
      public LinearLayoutManager.SavedState[] newArray(int paramAnonymousInt)
      {
        return new LinearLayoutManager.SavedState[paramAnonymousInt];
      }
    };
    boolean mAnchorLayoutFromEnd;
    int mAnchorOffset;
    int mAnchorPosition;
    
    public SavedState() {}
    
    SavedState(Parcel paramParcel)
    {
      mAnchorPosition = paramParcel.readInt();
      mAnchorOffset = paramParcel.readInt();
      int i = paramParcel.readInt();
      boolean bool = true;
      if (i != 1) {
        bool = false;
      }
      mAnchorLayoutFromEnd = bool;
    }
    
    public SavedState(SavedState paramSavedState)
    {
      mAnchorPosition = mAnchorPosition;
      mAnchorOffset = mAnchorOffset;
      mAnchorLayoutFromEnd = mAnchorLayoutFromEnd;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    boolean hasValidAnchor()
    {
      boolean bool;
      if (mAnchorPosition >= 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    void invalidateAnchor()
    {
      mAnchorPosition = -1;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(mAnchorPosition);
      paramParcel.writeInt(mAnchorOffset);
      paramParcel.writeInt(mAnchorLayoutFromEnd);
    }
  }
}
