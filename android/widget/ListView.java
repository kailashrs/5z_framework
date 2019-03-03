package android.widget;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Trace;
import android.util.AttributeSet;
import android.util.Log;
import android.util.MathUtils;
import android.util.SparseBooleanArray;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.RemotableViewMethod;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewHierarchyEncoder;
import android.view.ViewRootImpl;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.accessibility.AccessibilityNodeInfo.CollectionInfo;
import android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import com.android.internal.R.styleable;
import com.google.android.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@RemoteViews.RemoteView
public class ListView
  extends AbsListView
{
  private static final float MAX_SCROLL_FACTOR = 0.33F;
  private static final int MIN_SCROLL_PREVIEW_PIXELS = 2;
  static final int NO_POSITION = -1;
  static final String TAG = "ListView";
  private boolean mAreAllItemsSelectable = true;
  private final ArrowScrollFocusResult mArrowScrollFocusResult = new ArrowScrollFocusResult(null);
  Drawable mDivider;
  int mDividerHeight;
  private boolean mDividerIsOpaque;
  private Paint mDividerPaint;
  private FillNextGap mFillNextGap = null;
  private FocusSelector mFocusSelector;
  private boolean mFooterDividersEnabled;
  ArrayList<FixedViewInfo> mFooterViewInfos = Lists.newArrayList();
  private Handler mHandler = null;
  private boolean mHeaderDividersEnabled;
  ArrayList<FixedViewInfo> mHeaderViewInfos = Lists.newArrayList();
  private boolean mIsCacheColorOpaque;
  private boolean mItemsCanFocus = false;
  Drawable mOverScrollFooter;
  Drawable mOverScrollHeader;
  private final Rect mTempRect = new Rect();
  
  public ListView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public ListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842868);
  }
  
  public ListView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public ListView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ListView, paramInt1, paramInt2);
    CharSequence[] arrayOfCharSequence = paramAttributeSet.getTextArray(0);
    if (arrayOfCharSequence != null) {
      setAdapter(new ArrayAdapter(paramContext, 17367043, arrayOfCharSequence));
    }
    paramContext = paramAttributeSet.getDrawable(1);
    if (paramContext != null) {
      setDivider(paramContext);
    }
    paramContext = paramAttributeSet.getDrawable(5);
    if (paramContext != null) {
      setOverscrollHeader(paramContext);
    }
    paramContext = paramAttributeSet.getDrawable(6);
    if (paramContext != null) {
      setOverscrollFooter(paramContext);
    }
    if (paramAttributeSet.hasValueOrEmpty(2))
    {
      paramInt1 = paramAttributeSet.getDimensionPixelSize(2, 0);
      if (paramInt1 != 0) {
        setDividerHeight(paramInt1);
      }
    }
    mHeaderDividersEnabled = paramAttributeSet.getBoolean(3, true);
    mFooterDividersEnabled = paramAttributeSet.getBoolean(4, true);
    paramAttributeSet.recycle();
  }
  
  private View addViewAbove(View paramView, int paramInt)
  {
    paramInt--;
    View localView = obtainView(paramInt, mIsScrap);
    setupChild(localView, paramInt, paramView.getTop() - mDividerHeight, false, mListPadding.left, false, mIsScrap[0]);
    return localView;
  }
  
  private View addViewBelow(View paramView, int paramInt)
  {
    paramInt++;
    View localView = obtainView(paramInt, mIsScrap);
    setupChild(localView, paramInt, paramView.getBottom() + mDividerHeight, true, mListPadding.left, false, mIsScrap[0]);
    return localView;
  }
  
  private void adjustViewsUpOrDown()
  {
    int i = getChildCount();
    if (i > 0)
    {
      int j;
      int k;
      if (!mStackFromBottom)
      {
        j = getChildAt(0).getTop() - mListPadding.top;
        k = j;
        if (mFirstPosition != 0) {
          k = j - mDividerHeight;
        }
        j = k;
        if (k < 0) {
          j = 0;
        }
      }
      else
      {
        j = getChildAt(i - 1).getBottom() - (getHeight() - mListPadding.bottom);
        k = j;
        if (mFirstPosition + i < mItemCount) {
          k = j + mDividerHeight;
        }
        j = k;
        if (k > 0) {
          j = 0;
        }
      }
      if (j != 0) {
        offsetChildrenTopAndBottom(-j);
      }
    }
  }
  
  private int amountToScroll(int paramInt1, int paramInt2)
  {
    int i = getHeight() - mListPadding.bottom;
    int j = mListPadding.top;
    int k = getChildCount();
    if (paramInt1 == 130)
    {
      m = k - 1;
      paramInt1 = k;
      if (paramInt2 != -1) {
        m = paramInt2 - mFirstPosition;
      }
      for (paramInt1 = k; paramInt1 <= m; paramInt1++) {
        addViewBelow(getChildAt(paramInt1 - 1), mFirstPosition + paramInt1 - 1);
      }
      int n = mFirstPosition;
      localView = getChildAt(m);
      k = i;
      j = k;
      if (n + m < mItemCount - 1) {
        j = k - getArrowScrollPreviewLength();
      }
      if (localView.getBottom() <= j) {
        return 0;
      }
      if ((paramInt2 != -1) && (j - localView.getTop() >= getMaxScrollAmount())) {
        return 0;
      }
      m = localView.getBottom() - j;
      paramInt2 = m;
      if (mFirstPosition + paramInt1 == mItemCount) {
        paramInt2 = Math.min(m, getChildAt(paramInt1 - 1).getBottom() - i);
      }
      return Math.min(paramInt2, getMaxScrollAmount());
    }
    paramInt1 = 0;
    if (paramInt2 != -1) {}
    for (paramInt1 = paramInt2 - mFirstPosition; paramInt1 < 0; paramInt1 = paramInt2 - mFirstPosition)
    {
      addViewAbove(getChildAt(0), mFirstPosition);
      mFirstPosition -= 1;
    }
    i = mFirstPosition;
    View localView = getChildAt(paramInt1);
    int m = j;
    k = m;
    if (i + paramInt1 > 0) {
      k = m + getArrowScrollPreviewLength();
    }
    if (localView.getTop() >= k) {
      return 0;
    }
    if ((paramInt2 != -1) && (localView.getBottom() - k >= getMaxScrollAmount())) {
      return 0;
    }
    paramInt2 = k - localView.getTop();
    paramInt1 = paramInt2;
    if (mFirstPosition == 0) {
      paramInt1 = Math.min(paramInt2, j - getChildAt(0).getTop());
    }
    return Math.min(paramInt1, getMaxScrollAmount());
  }
  
  private int amountToScrollToNewFocus(int paramInt1, View paramView, int paramInt2)
  {
    int i = 0;
    paramView.getDrawingRect(mTempRect);
    offsetDescendantRectToMyCoords(paramView, mTempRect);
    if (paramInt1 == 33)
    {
      paramInt1 = i;
      if (mTempRect.top < mListPadding.top)
      {
        i = mListPadding.top - mTempRect.top;
        paramInt1 = i;
        if (paramInt2 > 0) {
          paramInt1 = i + getArrowScrollPreviewLength();
        }
      }
    }
    else
    {
      int j = getHeight() - mListPadding.bottom;
      paramInt1 = i;
      if (mTempRect.bottom > j)
      {
        i = mTempRect.bottom - j;
        paramInt1 = i;
        if (paramInt2 < mItemCount - 1) {
          paramInt1 = i + getArrowScrollPreviewLength();
        }
      }
    }
    return paramInt1;
  }
  
  private ArrowScrollFocusResult arrowScrollFocused(int paramInt)
  {
    View localView = getSelectedView();
    int i;
    int j;
    int k;
    if ((localView != null) && (localView.hasFocus()))
    {
      localView = localView.findFocus();
      localView = FocusFinder.getInstance().findNextFocus(this, localView, paramInt);
    }
    else
    {
      i = 1;
      j = 1;
      if (paramInt == 130)
      {
        if (mFirstPosition <= 0) {
          j = 0;
        }
        i = mListPadding.top;
        if (j != 0) {
          j = getArrowScrollPreviewLength();
        } else {
          j = 0;
        }
        j = i + j;
        if ((localView != null) && (localView.getTop() > j)) {
          j = localView.getTop();
        }
        mTempRect.set(0, j, 0, j);
      }
      else
      {
        if (mFirstPosition + getChildCount() - 1 < mItemCount) {
          j = i;
        } else {
          j = 0;
        }
        k = getHeight();
        i = mListPadding.bottom;
        if (j != 0) {
          j = getArrowScrollPreviewLength();
        } else {
          j = 0;
        }
        j = k - i - j;
        if ((localView != null) && (localView.getBottom() < j)) {
          j = localView.getBottom();
        }
        mTempRect.set(0, j, 0, j);
      }
      localView = FocusFinder.getInstance().findNextFocusFromRect(this, mTempRect, paramInt);
    }
    if (localView != null)
    {
      j = positionOfNewFocus(localView);
      if ((mSelectedPosition != -1) && (j != mSelectedPosition))
      {
        i = lookForSelectablePositionOnScreen(paramInt);
        if ((i != -1) && (((paramInt == 130) && (i < j)) || ((paramInt == 33) && (i > j)))) {
          return null;
        }
      }
      i = amountToScrollToNewFocus(paramInt, localView, j);
      k = getMaxScrollAmount();
      if (i < k)
      {
        localView.requestFocus(paramInt);
        mArrowScrollFocusResult.populate(j, i);
        return mArrowScrollFocusResult;
      }
      if (distanceToView(localView) < k)
      {
        localView.requestFocus(paramInt);
        mArrowScrollFocusResult.populate(j, k);
        return mArrowScrollFocusResult;
      }
    }
    return null;
  }
  
  private boolean arrowScrollImpl(int paramInt)
  {
    if (getChildCount() <= 0) {
      return false;
    }
    View localView1 = getSelectedView();
    int i = mSelectedPosition;
    int j = nextSelectedPositionForDirection(localView1, i, paramInt);
    int k = amountToScroll(paramInt, j);
    if (mItemsCanFocus) {
      localObject = arrowScrollFocused(paramInt);
    } else {
      localObject = null;
    }
    if (localObject != null)
    {
      j = ((ArrowScrollFocusResult)localObject).getSelectedPosition();
      k = ((ArrowScrollFocusResult)localObject).getAmountToScroll();
    }
    int m;
    if (localObject != null) {
      m = 1;
    } else {
      m = 0;
    }
    View localView2 = localView1;
    if (j != -1)
    {
      boolean bool;
      if (localObject != null) {
        bool = true;
      } else {
        bool = false;
      }
      handleNewSelectionChange(localView1, paramInt, j, bool);
      setSelectedPositionInt(j);
      setNextSelectedPositionInt(j);
      localView2 = getSelectedView();
      i = j;
      if ((mItemsCanFocus) && (localObject == null))
      {
        localView1 = getFocusedChild();
        if (localView1 != null) {
          localView1.clearFocus();
        }
      }
      m = 1;
      checkSelectionChanged();
    }
    if (k > 0)
    {
      if (paramInt != 33) {
        k = -k;
      }
      scrollListItemsBy(k);
      m = 1;
    }
    if ((mItemsCanFocus) && (localObject == null) && (localView2 != null) && (localView2.hasFocus()))
    {
      localObject = localView2.findFocus();
      if ((localObject != null) && ((!isViewAncestorOf((View)localObject, this)) || (distanceToView((View)localObject) > 0))) {
        ((View)localObject).clearFocus();
      }
    }
    Object localObject = localView2;
    if (j == -1)
    {
      localObject = localView2;
      if (localView2 != null)
      {
        localObject = localView2;
        if (!isViewAncestorOf(localView2, this))
        {
          localObject = null;
          hideSelector();
          mResurrectToPosition = -1;
        }
      }
    }
    if (m != 0)
    {
      if (localObject != null)
      {
        positionSelectorLikeFocus(i, (View)localObject);
        mSelectedTop = ((View)localObject).getTop();
      }
      if (!awakenScrollBars()) {
        invalidate();
      }
      invokeOnItemScrollListener();
      return true;
    }
    return false;
  }
  
  private void clearRecycledState(ArrayList<FixedViewInfo> paramArrayList)
  {
    if (paramArrayList != null)
    {
      int i = paramArrayList.size();
      for (int j = 0; j < i; j++)
      {
        ViewGroup.LayoutParams localLayoutParams = getview.getLayoutParams();
        if (checkLayoutParams(localLayoutParams)) {
          recycledHeaderFooter = false;
        }
      }
    }
  }
  
  private boolean commonKey(int paramInt1, int paramInt2, KeyEvent paramKeyEvent)
  {
    if ((mAdapter != null) && (isAttachedToWindow()))
    {
      if (mDataChanged) {
        layoutChildren();
      }
      boolean bool1 = false;
      int i = paramKeyEvent.getAction();
      boolean bool2 = bool1;
      if (KeyEvent.isConfirmKey(paramInt1))
      {
        bool2 = bool1;
        if (paramKeyEvent.hasNoModifiers())
        {
          bool2 = bool1;
          if (i != 1)
          {
            bool1 = resurrectSelectionIfNeeded();
            bool2 = bool1;
            if (!bool1)
            {
              bool2 = bool1;
              if (paramKeyEvent.getRepeatCount() == 0)
              {
                bool2 = bool1;
                if (getChildCount() > 0)
                {
                  keyPressed();
                  bool2 = true;
                }
              }
            }
          }
        }
      }
      bool1 = bool2;
      int j = paramInt2;
      if (!bool2)
      {
        bool1 = bool2;
        j = paramInt2;
        if (i != 1)
        {
          int k;
          switch (paramInt1)
          {
          default: 
            bool1 = bool2;
            j = paramInt2;
            break;
          case 123: 
            bool1 = bool2;
            j = paramInt2;
            if (paramKeyEvent.hasNoModifiers())
            {
              if ((!resurrectSelectionIfNeeded()) && (!fullScroll(130))) {
                bool1 = false;
              } else {
                bool1 = true;
              }
              j = paramInt2;
            }
            break;
          case 122: 
            bool1 = bool2;
            j = paramInt2;
            if (paramKeyEvent.hasNoModifiers())
            {
              if ((!resurrectSelectionIfNeeded()) && (!fullScroll(33))) {
                bool1 = false;
              } else {
                bool1 = true;
              }
              j = paramInt2;
            }
            break;
          case 93: 
            if (paramKeyEvent.hasNoModifiers())
            {
              if ((!resurrectSelectionIfNeeded()) && (!pageScroll(130))) {
                bool1 = false;
              } else {
                bool1 = true;
              }
              j = paramInt2;
            }
            else
            {
              bool1 = bool2;
              j = paramInt2;
              if (paramKeyEvent.hasModifiers(2))
              {
                if ((!resurrectSelectionIfNeeded()) && (!fullScroll(130))) {
                  bool1 = false;
                } else {
                  bool1 = true;
                }
                j = paramInt2;
              }
            }
            break;
          case 92: 
            if (paramKeyEvent.hasNoModifiers())
            {
              if ((!resurrectSelectionIfNeeded()) && (!pageScroll(33))) {
                bool1 = false;
              } else {
                bool1 = true;
              }
              j = paramInt2;
            }
            else
            {
              bool1 = bool2;
              j = paramInt2;
              if (paramKeyEvent.hasModifiers(2))
              {
                if ((!resurrectSelectionIfNeeded()) && (!fullScroll(33))) {
                  bool1 = false;
                } else {
                  bool1 = true;
                }
                j = paramInt2;
              }
            }
            break;
          case 61: 
            if (paramKeyEvent.hasNoModifiers())
            {
              if ((!resurrectSelectionIfNeeded()) && (!arrowScroll(130))) {
                bool1 = false;
              } else {
                bool1 = true;
              }
              j = paramInt2;
            }
            else
            {
              bool1 = bool2;
              j = paramInt2;
              if (paramKeyEvent.hasModifiers(1))
              {
                if ((!resurrectSelectionIfNeeded()) && (!arrowScroll(33))) {
                  bool1 = false;
                } else {
                  bool1 = true;
                }
                j = paramInt2;
              }
            }
            break;
          case 22: 
            bool1 = bool2;
            j = paramInt2;
            if (paramKeyEvent.hasNoModifiers())
            {
              bool1 = handleHorizontalFocusWithinListItem(66);
              j = paramInt2;
            }
            break;
          case 21: 
            bool1 = bool2;
            j = paramInt2;
            if (paramKeyEvent.hasNoModifiers())
            {
              bool1 = handleHorizontalFocusWithinListItem(17);
              j = paramInt2;
            }
            break;
          case 20: 
            if (paramKeyEvent.hasNoModifiers())
            {
              bool2 = resurrectSelectionIfNeeded();
              bool1 = bool2;
              j = paramInt2;
              if (!bool2) {
                for (k = paramInt2;; k = j)
                {
                  j = k - 1;
                  bool1 = bool2;
                  paramInt2 = j;
                  if (k <= 0) {
                    break;
                  }
                  bool1 = bool2;
                  paramInt2 = j;
                  if (!arrowScroll(130)) {
                    break;
                  }
                  bool2 = true;
                }
              }
            }
            else
            {
              bool1 = bool2;
              j = paramInt2;
              if (paramKeyEvent.hasModifiers(2))
              {
                if ((!resurrectSelectionIfNeeded()) && (!fullScroll(130))) {
                  bool1 = false;
                } else {
                  bool1 = true;
                }
                j = paramInt2;
              }
            }
            break;
          case 19: 
            if (paramKeyEvent.hasNoModifiers())
            {
              bool2 = resurrectSelectionIfNeeded();
              bool1 = bool2;
              j = paramInt2;
              if (!bool2)
              {
                for (j = paramInt2;; j = k)
                {
                  k = j - 1;
                  bool1 = bool2;
                  paramInt2 = k;
                  if (j <= 0) {
                    break;
                  }
                  bool1 = bool2;
                  paramInt2 = k;
                  if (!arrowScroll(33)) {
                    break;
                  }
                  bool2 = true;
                }
                j = paramInt2;
              }
            }
            else
            {
              bool1 = bool2;
              j = paramInt2;
              if (paramKeyEvent.hasModifiers(2))
              {
                if ((!resurrectSelectionIfNeeded()) && (!fullScroll(33))) {
                  bool1 = false;
                } else {
                  bool1 = true;
                }
                j = paramInt2;
              }
            }
            break;
          }
        }
      }
      if (bool1) {
        return true;
      }
      if (sendToTextFilter(paramInt1, j, paramKeyEvent)) {
        return true;
      }
      switch (i)
      {
      default: 
        return false;
      case 2: 
        return super.onKeyMultiple(paramInt1, j, paramKeyEvent);
      case 1: 
        return super.onKeyUp(paramInt1, paramKeyEvent);
      }
      return super.onKeyDown(paramInt1, paramKeyEvent);
    }
    return false;
  }
  
  private void correctTooHigh(int paramInt)
  {
    if ((mFirstPosition + paramInt - 1 == mItemCount - 1) && (paramInt > 0))
    {
      paramInt = getChildAt(paramInt - 1).getBottom();
      int i = mBottom - mTop - mListPadding.bottom - paramInt;
      View localView = getChildAt(0);
      int j = localView.getTop();
      if ((i > 0) && ((mFirstPosition > 0) || (j < mListPadding.top)))
      {
        paramInt = i;
        if (mFirstPosition == 0) {
          paramInt = Math.min(i, mListPadding.top - j);
        }
        offsetChildrenTopAndBottom(paramInt);
        if (mFirstPosition > 0)
        {
          fillUp(mFirstPosition - 1, localView.getTop() - mDividerHeight);
          adjustViewsUpOrDown();
        }
      }
    }
  }
  
  private void correctTooLow(int paramInt)
  {
    if ((mFirstPosition == 0) && (paramInt > 0))
    {
      int i = getChildAt(0).getTop();
      int j = mListPadding.top;
      int k = mBottom - mTop - mListPadding.bottom;
      j = i - j;
      View localView = getChildAt(paramInt - 1);
      i = localView.getBottom();
      int m = mFirstPosition + paramInt - 1;
      if (j > 0) {
        if ((m >= mItemCount - 1) && (i <= k))
        {
          if (m == mItemCount - 1) {
            adjustViewsUpOrDown();
          }
        }
        else
        {
          paramInt = j;
          if (m == mItemCount - 1) {
            paramInt = Math.min(j, i - k);
          }
          offsetChildrenTopAndBottom(-paramInt);
          if (m < mItemCount - 1)
          {
            fillDown(m + 1, localView.getBottom() + mDividerHeight);
            adjustViewsUpOrDown();
          }
        }
      }
    }
  }
  
  private int distanceToView(View paramView)
  {
    int i = 0;
    paramView.getDrawingRect(mTempRect);
    offsetDescendantRectToMyCoords(paramView, mTempRect);
    int j = mBottom - mTop - mListPadding.bottom;
    if (mTempRect.bottom < mListPadding.top) {
      i = mListPadding.top - mTempRect.bottom;
    } else if (mTempRect.top > j) {
      i = mTempRect.top - j;
    }
    return i;
  }
  
  private void fillAboveAndBelow(View paramView, int paramInt)
  {
    int i = mDividerHeight;
    if (!mStackFromBottom)
    {
      fillUp(paramInt - 1, paramView.getTop() - i);
      adjustViewsUpOrDown();
      fillDown(paramInt + 1, paramView.getBottom() + i);
    }
    else
    {
      fillDown(paramInt + 1, paramView.getBottom() + i);
      adjustViewsUpOrDown();
      fillUp(paramInt - 1, paramView.getTop() - i);
    }
  }
  
  private View fillDown(int paramInt1, int paramInt2)
  {
    return fillDown(paramInt1, paramInt2, false);
  }
  
  private View fillDown(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    View localView1 = null;
    int i = 0;
    int j = mBottom - mTop;
    int k = j;
    if ((mGroupFlags & 0x22) == 34) {
      k = j - mListPadding.bottom;
    }
    View localView2 = localView1;
    j = paramInt1;
    int m = paramInt2;
    if (paramBoolean)
    {
      i = mOldIncrementalDeltaY;
      m = paramInt2;
      j = paramInt1;
      localView2 = localView1;
    }
    for (;;)
    {
      paramBoolean = true;
      if ((m - i >= k) || (j >= mItemCount)) {
        break;
      }
      if (j != mSelectedPosition) {
        paramBoolean = false;
      }
      localView1 = makeAndAddView(j, m, true, mListPadding.left, paramBoolean);
      m = localView1.getBottom() + mDividerHeight;
      if (paramBoolean) {
        localView2 = localView1;
      }
      j++;
    }
    setVisibleRangeHint(mFirstPosition, mFirstPosition + getChildCount() - 1);
    return localView2;
  }
  
  private View fillFromMiddle(int paramInt1, int paramInt2)
  {
    int i = paramInt2 - paramInt1;
    paramInt2 = reconcileSelectedPosition();
    View localView = makeAndAddView(paramInt2, paramInt1, true, mListPadding.left, true);
    mFirstPosition = paramInt2;
    paramInt1 = localView.getMeasuredHeight();
    if (paramInt1 <= i) {
      localView.offsetTopAndBottom((i - paramInt1) / 2);
    }
    fillAboveAndBelow(localView, paramInt2);
    if (!mStackFromBottom) {
      correctTooHigh(getChildCount());
    } else {
      correctTooLow(getChildCount());
    }
    return localView;
  }
  
  private View fillFromSelection(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = getVerticalFadingEdgeLength();
    int j = mSelectedPosition;
    paramInt2 = getTopSelectionPixel(paramInt2, i, j);
    paramInt3 = getBottomSelectionPixel(paramInt3, i, j);
    View localView = makeAndAddView(j, paramInt1, true, mListPadding.left, true);
    if (localView.getBottom() > paramInt3) {
      localView.offsetTopAndBottom(-Math.min(localView.getTop() - paramInt2, localView.getBottom() - paramInt3));
    } else if (localView.getTop() < paramInt2) {
      localView.offsetTopAndBottom(Math.min(paramInt2 - localView.getTop(), paramInt3 - localView.getBottom()));
    }
    fillAboveAndBelow(localView, j);
    if (!mStackFromBottom) {
      correctTooHigh(getChildCount());
    } else {
      correctTooLow(getChildCount());
    }
    return localView;
  }
  
  private View fillFromTop(int paramInt)
  {
    mFirstPosition = Math.min(mFirstPosition, mSelectedPosition);
    mFirstPosition = Math.min(mFirstPosition, mItemCount - 1);
    if (mFirstPosition < 0) {
      mFirstPosition = 0;
    }
    return fillDown(mFirstPosition, paramInt);
  }
  
  private void fillGap(boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = getChildCount();
    int j;
    if (paramBoolean1)
    {
      j = 0;
      if ((mGroupFlags & 0x22) == 34) {
        j = getListPaddingTop();
      }
      if (i > 0) {
        j = getChildAt(i - 1).getBottom() + mDividerHeight;
      }
      fillDown(mFirstPosition + i, j, paramBoolean2);
      correctTooHigh(getChildCount());
    }
    else
    {
      j = 0;
      if ((mGroupFlags & 0x22) == 34) {
        j = getListPaddingBottom();
      }
      if (i > 0) {
        j = getChildAt(0).getTop() - mDividerHeight;
      } else {
        j = getHeight() - j;
      }
      fillUp(mFirstPosition - 1, j, paramBoolean2);
      correctTooLow(getChildCount());
    }
  }
  
  private View fillSpecific(int paramInt1, int paramInt2)
  {
    boolean bool;
    if (paramInt1 == mSelectedPosition) {
      bool = true;
    } else {
      bool = false;
    }
    View localView1 = makeAndAddView(paramInt1, paramInt2, true, mListPadding.left, bool);
    mFirstPosition = paramInt1;
    paramInt2 = mDividerHeight;
    Object localObject1;
    Object localObject2;
    if (!mStackFromBottom)
    {
      localObject1 = fillUp(paramInt1 - 1, localView1.getTop() - paramInt2);
      adjustViewsUpOrDown();
      localObject2 = fillDown(paramInt1 + 1, localView1.getBottom() + paramInt2);
      paramInt1 = getChildCount();
      if (paramInt1 > 0) {
        correctTooHigh(paramInt1);
      }
    }
    else
    {
      View localView2 = fillDown(paramInt1 + 1, localView1.getBottom() + paramInt2);
      adjustViewsUpOrDown();
      View localView3 = fillUp(paramInt1 - 1, localView1.getTop() - paramInt2);
      paramInt1 = getChildCount();
      localObject1 = localView3;
      localObject2 = localView2;
      if (paramInt1 > 0)
      {
        correctTooLow(paramInt1);
        localObject2 = localView2;
        localObject1 = localView3;
      }
    }
    if (bool) {
      return localView1;
    }
    if (localObject1 != null) {
      return localObject1;
    }
    return localObject2;
  }
  
  private View fillUp(int paramInt1, int paramInt2)
  {
    return fillUp(paramInt1, paramInt2, false);
  }
  
  private View fillUp(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    View localView1 = null;
    int i = 0;
    int j = 0;
    if ((mGroupFlags & 0x22) == 34) {
      j = mListPadding.top;
    }
    View localView2 = localView1;
    int k = paramInt1;
    int m = paramInt2;
    if (paramBoolean)
    {
      i = mOldIncrementalDeltaY;
      m = paramInt2;
      k = paramInt1;
      localView2 = localView1;
    }
    for (;;)
    {
      paramBoolean = true;
      if ((m + i <= j) || (k < 0)) {
        break;
      }
      if (k != mSelectedPosition) {
        paramBoolean = false;
      }
      localView1 = makeAndAddView(k, m, false, mListPadding.left, paramBoolean);
      m = localView1.getTop() - mDividerHeight;
      if (paramBoolean) {
        localView2 = localView1;
      }
      k--;
    }
    mFirstPosition = (k + 1);
    setVisibleRangeHint(mFirstPosition, mFirstPosition + getChildCount() - 1);
    return localView2;
  }
  
  private int getArrowScrollPreviewLength()
  {
    return Math.max(2, getVerticalFadingEdgeLength());
  }
  
  private int getBottomSelectionPixel(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = paramInt1;
    if (paramInt3 != mItemCount - 1) {
      i = paramInt1 - paramInt2;
    }
    return i;
  }
  
  private int getTopSelectionPixel(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = paramInt1;
    if (paramInt3 > 0) {
      i = paramInt1 + paramInt2;
    }
    return i;
  }
  
  private boolean handleHorizontalFocusWithinListItem(int paramInt)
  {
    if ((paramInt != 17) && (paramInt != 66)) {
      throw new IllegalArgumentException("direction must be one of {View.FOCUS_LEFT, View.FOCUS_RIGHT}");
    }
    int i = getChildCount();
    if ((mItemsCanFocus) && (i > 0) && (mSelectedPosition != -1))
    {
      Object localObject = getSelectedView();
      if ((localObject != null) && (((View)localObject).hasFocus()) && ((localObject instanceof ViewGroup)))
      {
        View localView1 = ((View)localObject).findFocus();
        View localView2 = FocusFinder.getInstance().findNextFocus((ViewGroup)localObject, localView1, paramInt);
        if (localView2 != null)
        {
          localObject = mTempRect;
          if (localView1 != null)
          {
            localView1.getFocusedRect((Rect)localObject);
            offsetDescendantRectToMyCoords(localView1, (Rect)localObject);
            offsetRectIntoDescendantCoords(localView2, (Rect)localObject);
          }
          else
          {
            localObject = null;
          }
          if (localView2.requestFocus(paramInt, (Rect)localObject)) {
            return true;
          }
        }
        localObject = FocusFinder.getInstance().findNextFocus((ViewGroup)getRootView(), localView1, paramInt);
        if (localObject != null) {
          return isViewAncestorOf((View)localObject, this);
        }
      }
    }
    return false;
  }
  
  private void handleNewSelectionChange(View paramView, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if (paramInt2 != -1)
    {
      int i = 0;
      int j = mSelectedPosition - mFirstPosition;
      paramInt2 -= mFirstPosition;
      View localView2;
      if (paramInt1 == 33)
      {
        View localView1 = getChildAt(paramInt2);
        localView2 = paramView;
        paramInt1 = 1;
        paramView = localView1;
      }
      else
      {
        paramInt1 = j;
        localView2 = getChildAt(paramInt2);
        j = paramInt2;
        paramInt2 = paramInt1;
        paramInt1 = i;
      }
      i = getChildCount();
      boolean bool1 = true;
      if (paramView != null)
      {
        boolean bool2;
        if ((!paramBoolean) && (paramInt1 != 0)) {
          bool2 = true;
        } else {
          bool2 = false;
        }
        paramView.setSelected(bool2);
        measureAndAdjustDown(paramView, paramInt2, i);
      }
      if (localView2 != null)
      {
        if ((!paramBoolean) && (paramInt1 == 0)) {
          paramBoolean = bool1;
        } else {
          paramBoolean = false;
        }
        localView2.setSelected(paramBoolean);
        measureAndAdjustDown(localView2, j, i);
      }
      return;
    }
    throw new IllegalArgumentException("newSelectedPosition needs to be valid");
  }
  
  private boolean isDirectChildHeaderOrFooter(View paramView)
  {
    ArrayList localArrayList = mHeaderViewInfos;
    int i = localArrayList.size();
    for (int j = 0; j < i; j++) {
      if (paramView == getview) {
        return true;
      }
    }
    localArrayList = mFooterViewInfos;
    i = localArrayList.size();
    for (j = 0; j < i; j++) {
      if (paramView == getview) {
        return true;
      }
    }
    return false;
  }
  
  private boolean isViewAncestorOf(View paramView1, View paramView2)
  {
    boolean bool = true;
    if (paramView1 == paramView2) {
      return true;
    }
    paramView1 = paramView1.getParent();
    if ((!(paramView1 instanceof ViewGroup)) || (!isViewAncestorOf((View)paramView1, paramView2))) {
      bool = false;
    }
    return bool;
  }
  
  private int lookForSelectablePositionOnScreen(int paramInt)
  {
    int i = mFirstPosition;
    int j;
    ListAdapter localListAdapter;
    if (paramInt == 130)
    {
      if (mSelectedPosition != -1) {
        j = mSelectedPosition + 1;
      } else {
        j = i;
      }
      if (j >= mAdapter.getCount()) {
        return -1;
      }
      paramInt = j;
      if (j < i) {
        paramInt = i;
      }
      j = getLastVisiblePosition();
      localListAdapter = getAdapter();
      while (paramInt <= j)
      {
        if ((localListAdapter.isEnabled(paramInt)) && (getChildAt(paramInt - i).getVisibility() == 0)) {
          return paramInt;
        }
        paramInt++;
      }
    }
    else
    {
      int k = getChildCount() + i - 1;
      if (mSelectedPosition != -1) {
        paramInt = mSelectedPosition - 1;
      } else {
        paramInt = getChildCount() + i - 1;
      }
      if ((paramInt < 0) || (paramInt >= mAdapter.getCount())) {
        break label220;
      }
      j = paramInt;
      if (paramInt > k) {
        j = k;
      }
      localListAdapter = getAdapter();
      while (j >= i)
      {
        if ((localListAdapter.isEnabled(j)) && (getChildAt(j - i).getVisibility() == 0)) {
          return j;
        }
        j--;
      }
    }
    return -1;
    label220:
    return -1;
  }
  
  private View makeAndAddView(int paramInt1, int paramInt2, boolean paramBoolean1, int paramInt3, boolean paramBoolean2)
  {
    if (!mDataChanged)
    {
      localView = mRecycler.getActiveView(paramInt1);
      if (localView != null)
      {
        setupChild(localView, paramInt1, paramInt2, paramBoolean1, paramInt3, paramBoolean2, true);
        return localView;
      }
    }
    View localView = obtainView(paramInt1, mIsScrap);
    setupChild(localView, paramInt1, paramInt2, paramBoolean1, paramInt3, paramBoolean2, mIsScrap[0]);
    return localView;
  }
  
  private void measureAndAdjustDown(View paramView, int paramInt1, int paramInt2)
  {
    int i = paramView.getHeight();
    measureItem(paramView);
    if (paramView.getMeasuredHeight() != i)
    {
      relayoutMeasuredItem(paramView);
      int j = paramView.getMeasuredHeight();
      paramInt1++;
      while (paramInt1 < paramInt2)
      {
        getChildAt(paramInt1).offsetTopAndBottom(j - i);
        paramInt1++;
      }
    }
  }
  
  private void measureItem(View paramView)
  {
    ViewGroup.LayoutParams localLayoutParams1 = paramView.getLayoutParams();
    ViewGroup.LayoutParams localLayoutParams2 = localLayoutParams1;
    if (localLayoutParams1 == null) {
      localLayoutParams2 = new ViewGroup.LayoutParams(-1, -2);
    }
    int i = ViewGroup.getChildMeasureSpec(mWidthMeasureSpec, mListPadding.left + mListPadding.right, width);
    int j = height;
    if (j > 0) {
      j = View.MeasureSpec.makeMeasureSpec(j, 1073741824);
    } else {
      j = View.MeasureSpec.makeSafeMeasureSpec(getMeasuredHeight(), 0);
    }
    paramView.measure(i, j);
  }
  
  private void measureScrapChild(View paramView, int paramInt1, int paramInt2, int paramInt3)
  {
    AbsListView.LayoutParams localLayoutParams1 = (AbsListView.LayoutParams)paramView.getLayoutParams();
    AbsListView.LayoutParams localLayoutParams2 = localLayoutParams1;
    if (localLayoutParams1 == null)
    {
      localLayoutParams2 = (AbsListView.LayoutParams)generateDefaultLayoutParams();
      paramView.setLayoutParams(localLayoutParams2);
    }
    viewType = mAdapter.getItemViewType(paramInt1);
    isEnabled = mAdapter.isEnabled(paramInt1);
    forceAdd = true;
    paramInt2 = ViewGroup.getChildMeasureSpec(paramInt2, mListPadding.left + mListPadding.right, width);
    paramInt1 = height;
    if (paramInt1 > 0) {
      paramInt1 = View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824);
    } else {
      paramInt1 = View.MeasureSpec.makeSafeMeasureSpec(paramInt3, 0);
    }
    paramView.measure(paramInt2, paramInt1);
    paramView.forceLayout();
  }
  
  private View moveSelection(View paramView1, View paramView2, int paramInt1, int paramInt2, int paramInt3)
  {
    int i = getVerticalFadingEdgeLength();
    int j = mSelectedPosition;
    int k = getTopSelectionPixel(paramInt2, i, j);
    i = getBottomSelectionPixel(paramInt2, i, j);
    int m;
    if (paramInt1 > 0)
    {
      paramView2 = makeAndAddView(j - 1, paramView1.getTop(), true, mListPadding.left, false);
      paramInt1 = mDividerHeight;
      paramView1 = makeAndAddView(j, paramView2.getBottom() + paramInt1, true, mListPadding.left, true);
      if (paramView1.getBottom() > i)
      {
        m = paramView1.getTop();
        j = paramView1.getBottom();
        paramInt2 = (paramInt3 - paramInt2) / 2;
        paramInt2 = Math.min(Math.min(m - k, j - i), paramInt2);
        paramView2.offsetTopAndBottom(-paramInt2);
        paramView1.offsetTopAndBottom(-paramInt2);
      }
      if (!mStackFromBottom)
      {
        fillUp(mSelectedPosition - 2, paramView1.getTop() - paramInt1);
        adjustViewsUpOrDown();
        fillDown(mSelectedPosition + 1, paramView1.getBottom() + paramInt1);
      }
      else
      {
        fillDown(mSelectedPosition + 1, paramView1.getBottom() + paramInt1);
        adjustViewsUpOrDown();
        fillUp(mSelectedPosition - 2, paramView1.getTop() - paramInt1);
      }
    }
    else if (paramInt1 < 0)
    {
      if (paramView2 != null) {
        paramView1 = makeAndAddView(j, paramView2.getTop(), true, mListPadding.left, true);
      } else {
        paramView1 = makeAndAddView(j, paramView1.getTop(), false, mListPadding.left, true);
      }
      if (paramView1.getTop() < k)
      {
        paramInt1 = paramView1.getTop();
        m = paramView1.getBottom();
        paramInt2 = (paramInt3 - paramInt2) / 2;
        paramView1.offsetTopAndBottom(Math.min(Math.min(k - paramInt1, i - m), paramInt2));
      }
      fillAboveAndBelow(paramView1, j);
    }
    else
    {
      paramInt1 = paramView1.getTop();
      paramView1 = makeAndAddView(j, paramInt1, true, mListPadding.left, true);
      if ((paramInt1 < paramInt2) && (paramView1.getBottom() < paramInt2 + 20)) {
        paramView1.offsetTopAndBottom(paramInt2 - paramView1.getTop());
      }
      fillAboveAndBelow(paramView1, j);
    }
    return paramView1;
  }
  
  private final int nextSelectedPositionForDirection(View paramView, int paramInt1, int paramInt2)
  {
    boolean bool = true;
    int i;
    if (paramInt2 == 130)
    {
      i = getHeight();
      int j = mListPadding.bottom;
      if ((paramView != null) && (paramView.getBottom() <= i - j))
      {
        if ((paramInt1 != -1) && (paramInt1 >= mFirstPosition)) {
          paramInt1++;
        } else {
          paramInt1 = mFirstPosition;
        }
      }
      else {
        return -1;
      }
    }
    else
    {
      i = mListPadding.top;
      if ((paramView == null) || (paramView.getTop() < i)) {
        break label168;
      }
      i = mFirstPosition + getChildCount() - 1;
      if ((paramInt1 != -1) && (paramInt1 <= i)) {
        paramInt1--;
      } else {
        paramInt1 = i;
      }
    }
    if ((paramInt1 >= 0) && (paramInt1 < mAdapter.getCount()))
    {
      if (paramInt2 != 130) {
        bool = false;
      }
      return lookForSelectablePosition(paramInt1, bool);
    }
    return -1;
    label168:
    return -1;
  }
  
  private int positionOfNewFocus(View paramView)
  {
    int i = getChildCount();
    for (int j = 0; j < i; j++) {
      if (isViewAncestorOf(paramView, getChildAt(j))) {
        return mFirstPosition + j;
      }
    }
    throw new IllegalArgumentException("newFocus is not a child of any of the children of the list!");
  }
  
  private void relayoutMeasuredItem(View paramView)
  {
    int i = paramView.getMeasuredWidth();
    int j = paramView.getMeasuredHeight();
    int k = mListPadding.left;
    int m = paramView.getTop();
    paramView.layout(k, m, k + i, m + j);
  }
  
  private void removeFixedViewInfo(View paramView, ArrayList<FixedViewInfo> paramArrayList)
  {
    int i = paramArrayList.size();
    for (int j = 0; j < i; j++) {
      if (getview == paramView)
      {
        paramArrayList.remove(j);
        break;
      }
    }
  }
  
  private void removeUnusedFixedViews(List<FixedViewInfo> paramList)
  {
    if (paramList == null) {
      return;
    }
    for (int i = paramList.size() - 1; i >= 0; i--)
    {
      View localView = getview;
      AbsListView.LayoutParams localLayoutParams = (AbsListView.LayoutParams)localView.getLayoutParams();
      if ((localView.getParent() == null) && (localLayoutParams != null) && (recycledHeaderFooter))
      {
        removeDetachedView(localView, false);
        recycledHeaderFooter = false;
      }
    }
  }
  
  private void scrollListItemsBy(int paramInt)
  {
    offsetChildrenTopAndBottom(paramInt);
    int i = getHeight() - mListPadding.bottom;
    int j = mListPadding.top;
    AbsListView.RecycleBin localRecycleBin = mRecycler;
    View localView;
    if (paramInt < 0)
    {
      paramInt = getChildCount();
      localView = getChildAt(paramInt - 1);
      while (localView.getBottom() < i)
      {
        int k = mFirstPosition + paramInt - 1;
        if (k >= mItemCount - 1) {
          break;
        }
        localView = addViewBelow(localView, k);
        paramInt++;
      }
      if (localView.getBottom() < i) {
        offsetChildrenTopAndBottom(i - localView.getBottom());
      }
      localView = getChildAt(0);
      while (localView.getBottom() < j)
      {
        if (localRecycleBin.shouldRecycleViewType(getLayoutParamsviewType)) {
          localRecycleBin.addScrapView(localView, mFirstPosition);
        }
        detachViewFromParent(localView);
        localView = getChildAt(0);
        mFirstPosition += 1;
      }
    }
    else
    {
      localView = getChildAt(0);
      while ((localView.getTop() > j) && (mFirstPosition > 0))
      {
        localView = addViewAbove(localView, mFirstPosition);
        mFirstPosition -= 1;
      }
      if (localView.getTop() > j) {
        offsetChildrenTopAndBottom(j - localView.getTop());
      }
      paramInt = getChildCount() - 1;
      for (localView = getChildAt(paramInt); localView.getTop() > i; localView = getChildAt(paramInt))
      {
        if (localRecycleBin.shouldRecycleViewType(getLayoutParamsviewType)) {
          localRecycleBin.addScrapView(localView, mFirstPosition + paramInt);
        }
        detachViewFromParent(localView);
        paramInt--;
      }
    }
    localRecycleBin.fullyDetachScrapViews();
    removeUnusedFixedViews(mHeaderViewInfos);
    removeUnusedFixedViews(mFooterViewInfos);
  }
  
  private void setupChild(View paramView, int paramInt1, int paramInt2, boolean paramBoolean1, int paramInt3, boolean paramBoolean2, boolean paramBoolean3)
  {
    Trace.traceBegin(8L, "setupListItem");
    if ((paramBoolean2) && (shouldShowSelector())) {
      paramBoolean2 = true;
    } else {
      paramBoolean2 = false;
    }
    if (paramBoolean2 != paramView.isSelected()) {
      i = 1;
    } else {
      i = 0;
    }
    int j = mTouchMode;
    boolean bool;
    if ((j > 0) && (j < 3) && (mMotionPosition == paramInt1)) {
      bool = true;
    } else {
      bool = false;
    }
    if (bool != paramView.isPressed()) {
      k = 1;
    } else {
      k = 0;
    }
    if ((paramBoolean3) && (i == 0) && (!paramView.isLayoutRequested())) {
      j = 0;
    } else {
      j = 1;
    }
    AbsListView.LayoutParams localLayoutParams1 = (AbsListView.LayoutParams)paramView.getLayoutParams();
    AbsListView.LayoutParams localLayoutParams2 = localLayoutParams1;
    if (localLayoutParams1 == null) {
      localLayoutParams2 = (AbsListView.LayoutParams)generateDefaultLayoutParams();
    }
    viewType = mAdapter.getItemViewType(paramInt1);
    isEnabled = mAdapter.isEnabled(paramInt1);
    if (i != 0) {
      paramView.setSelected(paramBoolean2);
    }
    if (k != 0) {
      paramView.setPressed(bool);
    }
    if ((mChoiceMode != 0) && (mCheckStates != null)) {
      if ((paramView instanceof Checkable)) {
        ((Checkable)paramView).setChecked(mCheckStates.get(paramInt1));
      } else if (getContextgetApplicationInfotargetSdkVersion >= 11) {
        paramView.setActivated(mCheckStates.get(paramInt1));
      }
    }
    if (((paramBoolean3) && (!forceAdd)) || ((recycledHeaderFooter) && (viewType == -2)))
    {
      if (paramBoolean1) {
        i = -1;
      } else {
        i = 0;
      }
      attachViewToParent(paramView, i, localLayoutParams2);
      if ((paramBoolean3) && (getLayoutParamsscrappedFromPosition != paramInt1)) {
        paramView.jumpDrawablesToCurrentState();
      }
    }
    else
    {
      forceAdd = false;
      if (viewType == -2) {
        recycledHeaderFooter = true;
      }
      if (paramBoolean1) {
        paramInt1 = -1;
      } else {
        paramInt1 = 0;
      }
      addViewInLayout(paramView, paramInt1, localLayoutParams2, true);
      paramView.resolveRtlPropertiesIfNeeded();
    }
    if (j != 0)
    {
      i = ViewGroup.getChildMeasureSpec(mWidthMeasureSpec, mListPadding.left + mListPadding.right, width);
      paramInt1 = height;
      if (paramInt1 > 0) {
        paramInt1 = View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824);
      } else {
        paramInt1 = View.MeasureSpec.makeSafeMeasureSpec(getMeasuredHeight(), 0);
      }
      paramView.measure(i, paramInt1);
    }
    else
    {
      cleanupLayoutState(paramView);
    }
    int i = paramView.getMeasuredWidth();
    int k = paramView.getMeasuredHeight();
    if (paramBoolean1) {
      paramInt1 = paramInt2;
    } else {
      paramInt1 = paramInt2 - k;
    }
    if (j != 0)
    {
      paramView.layout(paramInt3, paramInt1, paramInt3 + i, paramInt1 + k);
    }
    else
    {
      paramView.offsetLeftAndRight(paramInt3 - paramView.getLeft());
      paramView.offsetTopAndBottom(paramInt1 - paramView.getTop());
    }
    if ((mCachingStarted) && (!paramView.isDrawingCacheEnabled())) {
      paramView.setDrawingCacheEnabled(true);
    }
    Trace.traceEnd(8L);
  }
  
  private boolean shouldAdjustHeightForDivider(int paramInt)
  {
    int i = mDividerHeight;
    Object localObject = mOverScrollHeader;
    Drawable localDrawable = mOverScrollFooter;
    int j;
    if (localObject != null) {
      j = 1;
    } else {
      j = 0;
    }
    int k;
    if (localDrawable != null) {
      k = 1;
    } else {
      k = 0;
    }
    if ((i > 0) && (mDivider != null)) {
      i = 1;
    } else {
      i = 0;
    }
    if (i != 0)
    {
      if ((isOpaque()) && (!super.isOpaque())) {
        i = 1;
      } else {
        i = 0;
      }
      int m = mItemCount;
      int n = getHeaderViewsCount();
      int i1 = m - mFooterViewInfos.size();
      int i2;
      if (paramInt < n) {
        i2 = 1;
      } else {
        i2 = 0;
      }
      int i3;
      if (paramInt >= i1) {
        i3 = 1;
      } else {
        i3 = 0;
      }
      boolean bool1 = mHeaderDividersEnabled;
      boolean bool2 = mFooterDividersEnabled;
      if ((!bool1) && (i2 != 0)) {}
      for (;;)
      {
        break;
        if ((bool2) || (i3 == 0))
        {
          localObject = mAdapter;
          if (!mStackFromBottom)
          {
            if (paramInt == m - 1) {
              j = 1;
            } else {
              j = 0;
            }
            if ((k == 0) || (j == 0))
            {
              k = paramInt + 1;
              if ((((ListAdapter)localObject).isEnabled(paramInt)) && ((bool1) || ((i2 == 0) && (k >= n))) && ((j != 0) || ((((ListAdapter)localObject).isEnabled(k)) && ((bool2) || ((i3 == 0) && (k < i1)))))) {
                return true;
              }
              if (i != 0) {
                return true;
              }
            }
          }
          else
          {
            if (j != 0) {
              k = 1;
            } else {
              k = 0;
            }
            if (paramInt == k) {
              k = 1;
            } else {
              k = 0;
            }
            if (k == 0)
            {
              j = paramInt - 1;
              if ((((ListAdapter)localObject).isEnabled(paramInt)) && ((bool1) || ((i2 == 0) && (j >= n))) && ((k != 0) || ((((ListAdapter)localObject).isEnabled(j)) && ((bool2) || ((i3 == 0) && (j < i1)))))) {
                return true;
              }
              if (i != 0) {
                return true;
              }
            }
          }
        }
      }
    }
    return false;
  }
  
  private boolean showingBottomFadingEdge()
  {
    int i = getChildCount();
    int j = getChildAt(i - 1).getBottom();
    int k = mFirstPosition;
    boolean bool1 = true;
    int m = mScrollY;
    int n = getHeight();
    int i1 = mListPadding.bottom;
    boolean bool2 = bool1;
    if (k + i - 1 >= mItemCount - 1) {
      if (j < m + n - i1) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
    }
    return bool2;
  }
  
  private boolean showingTopFadingEdge()
  {
    int i = mScrollY;
    int j = mListPadding.top;
    int k = mFirstPosition;
    boolean bool = false;
    if ((k <= 0) && (getChildAt(0).getTop() <= i + j)) {
      break label48;
    }
    bool = true;
    label48:
    return bool;
  }
  
  public void addFooterView(View paramView)
  {
    addFooterView(paramView, null, true);
  }
  
  public void addFooterView(View paramView, Object paramObject, boolean paramBoolean)
  {
    if ((paramView.getParent() != null) && (paramView.getParent() != this) && (Log.isLoggable("ListView", 5))) {
      Log.w("ListView", "The specified child already has a parent. You must call removeView() on the child's parent first.");
    }
    FixedViewInfo localFixedViewInfo = new FixedViewInfo();
    view = paramView;
    data = paramObject;
    isSelectable = paramBoolean;
    mFooterViewInfos.add(localFixedViewInfo);
    mAreAllItemsSelectable &= paramBoolean;
    if (mAdapter != null)
    {
      if (!(mAdapter instanceof HeaderViewListAdapter)) {
        wrapHeaderListAdapterInternal();
      }
      if (mDataSetObserver != null) {
        mDataSetObserver.onChanged();
      }
    }
  }
  
  public void addHeaderView(View paramView)
  {
    addHeaderView(paramView, null, true);
  }
  
  public void addHeaderView(View paramView, Object paramObject, boolean paramBoolean)
  {
    if ((paramView.getParent() != null) && (paramView.getParent() != this) && (Log.isLoggable("ListView", 5))) {
      Log.w("ListView", "The specified child already has a parent. You must call removeView() on the child's parent first.");
    }
    FixedViewInfo localFixedViewInfo = new FixedViewInfo();
    view = paramView;
    data = paramObject;
    isSelectable = paramBoolean;
    mHeaderViewInfos.add(localFixedViewInfo);
    mAreAllItemsSelectable &= paramBoolean;
    if (mAdapter != null)
    {
      if (!(mAdapter instanceof HeaderViewListAdapter)) {
        wrapHeaderListAdapterInternal();
      }
      if (mDataSetObserver != null) {
        mDataSetObserver.onChanged();
      }
    }
  }
  
  public boolean areFooterDividersEnabled()
  {
    return mFooterDividersEnabled;
  }
  
  public boolean areHeaderDividersEnabled()
  {
    return mHeaderDividersEnabled;
  }
  
  boolean arrowScroll(int paramInt)
  {
    try
    {
      mInLayout = true;
      boolean bool = arrowScrollImpl(paramInt);
      if (bool) {
        playSoundEffect(SoundEffectConstants.getContantForFocusDirection(paramInt));
      }
      return bool;
    }
    finally
    {
      mInLayout = false;
    }
  }
  
  protected boolean canAnimate()
  {
    boolean bool;
    if ((super.canAnimate()) && (mItemCount > 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected void dispatchDataSetObserverOnChangedInternal()
  {
    if (mDataSetObserver != null) {
      mDataSetObserver.onChanged();
    }
  }
  
  protected void dispatchDraw(Canvas paramCanvas)
  {
    if (mCachingStarted) {
      mCachingActive = true;
    }
    int i = mDividerHeight;
    Drawable localDrawable1 = mOverScrollHeader;
    Drawable localDrawable2 = mOverScrollFooter;
    int j;
    if (localDrawable1 != null) {
      j = 1;
    } else {
      j = 0;
    }
    int k;
    if (localDrawable2 != null) {
      k = 1;
    } else {
      k = 0;
    }
    int m;
    if ((i > 0) && (mDivider != null)) {
      m = 1;
    } else {
      m = 0;
    }
    if ((m == 0) && (j == 0) && (k == 0)) {
      break label1195;
    }
    Rect localRect = mTempRect;
    left = mPaddingLeft;
    right = (mRight - mLeft - mPaddingRight);
    int n = getChildCount();
    int i1 = getHeaderViewsCount();
    int i2 = mItemCount;
    int i3 = i2 - mFooterViewInfos.size();
    boolean bool1 = mHeaderDividersEnabled;
    boolean bool2 = mFooterDividersEnabled;
    int i4 = mFirstPosition;
    boolean bool3 = mAreAllItemsSelectable;
    ListAdapter localListAdapter1 = mAdapter;
    int i5;
    if ((isOpaque()) && (!super.isOpaque())) {
      i5 = 1;
    } else {
      i5 = 0;
    }
    if (i5 != 0) {
      if ((mDividerPaint == null) && (mIsCacheColorOpaque))
      {
        mDividerPaint = new Paint();
        mDividerPaint.setColor(getCacheColorHint());
      }
      else {}
    }
    Paint localPaint = mDividerPaint;
    int i6 = mGroupFlags;
    int i7;
    if ((i6 & 0x22) == 34)
    {
      i6 = mListPadding.top;
      i7 = mListPadding.bottom;
    }
    else
    {
      i7 = 0;
      i6 = 0;
    }
    int i8 = i6;
    int i9 = mBottom - mTop - i7 + mScrollY;
    int i10;
    int i11;
    int i12;
    int i13;
    if (!mStackFromBottom)
    {
      i6 = mScrollY;
      if ((n > 0) && (i6 < 0)) {
        if (j != 0)
        {
          bottom = 0;
          top = i6;
          drawOverscrollHeader(paramCanvas, localDrawable1, localRect);
        }
        else if (m != 0)
        {
          bottom = 0;
          top = (-i);
          drawDivider(paramCanvas, localRect, -1);
        }
      }
      i10 = 0;
      i11 = 0;
      i8 = i9;
      i6 = m;
      i7 = j;
      j = i10;
      while (i11 < n)
      {
        i12 = i4 + i11;
        if (i12 < i1) {
          i10 = 1;
        } else {
          i10 = 0;
        }
        if (i12 >= i3) {
          i13 = 1;
        } else {
          i13 = 0;
        }
        if (((!bool1) && (i10 != 0)) || ((!bool2) && (i13 != 0)))
        {
          m = j;
          j = m;
        }
        for (;;)
        {
          break label712;
          j = getChildAt(i11).getBottom();
          if (i11 == n - 1) {
            i9 = 1;
          } else {
            i9 = 0;
          }
          if ((i6 != 0) && (j < i8))
          {
            if (k != 0)
            {
              m = j;
              if (i9 != 0) {
                break;
              }
            }
            m = i12 + 1;
            ListAdapter localListAdapter2 = localListAdapter1;
            if (localListAdapter2.isEnabled(i12))
            {
              if ((!bool1) && ((i10 != 0) || (m < i1))) {}
              for (;;)
              {
                break label677;
                if ((i9 != 0) || ((localListAdapter2.isEnabled(m)) && ((bool2) || ((i13 == 0) && (m < i3)))))
                {
                  top = j;
                  bottom = (j + i);
                  drawDivider(paramCanvas, localRect, i11);
                  break;
                }
              }
            }
            label677:
            if (i5 != 0)
            {
              top = j;
              bottom = (j + i);
              paramCanvas.drawRect(localRect, localPaint);
            }
            else {}
          }
        }
        label712:
        i11++;
      }
      m = mBottom + mScrollY;
      if (k != 0) {
        if ((i4 + n == i2) && (m > j))
        {
          top = j;
          bottom = m;
          drawOverscrollFooter(paramCanvas, localDrawable2, localRect);
        }
        else {}
      }
    }
    else
    {
      i2 = mScrollY;
      if ((n > 0) && (j != 0))
      {
        top = i2;
        bottom = getChildAt(0).getTop();
        drawOverscrollHeader(paramCanvas, localDrawable1, localRect);
      }
      if (j != 0) {
        j = 1;
      } else {
        j = 0;
      }
      i6 = j;
      i7 = j;
      j = i4;
      while (i7 < n)
      {
        i4 = j + i7;
        if (i4 < i1) {
          i11 = 1;
        } else {
          i11 = 0;
        }
        if (i4 >= i3) {
          i10 = 1;
        } else {
          i10 = 0;
        }
        if ((!bool1) && (i11 != 0)) {}
        for (;;)
        {
          break;
          if ((bool2) || (i10 == 0))
          {
            i12 = getChildAt(i7).getTop();
            if (m != 0) {
              if (i12 > i8)
              {
                if (i7 == i6) {
                  i13 = 1;
                } else {
                  i13 = 0;
                }
                int i14 = i4 - 1;
                if (localListAdapter1.isEnabled(i4))
                {
                  if ((!bool1) && ((i11 != 0) || (i14 < i1))) {}
                  for (;;)
                  {
                    break;
                    if ((i13 != 0) || ((localListAdapter1.isEnabled(i14)) && ((bool2) || ((i10 == 0) && (i14 < i3)))))
                    {
                      top = (i12 - i);
                      bottom = i12;
                      drawDivider(paramCanvas, localRect, i7 - 1);
                      break label1107;
                    }
                  }
                }
                if (i5 != 0)
                {
                  top = (i12 - i);
                  bottom = i12;
                  paramCanvas.drawRect(localRect, localPaint);
                }
              }
              else {}
            }
          }
        }
        label1107:
        i7++;
      }
      if ((n > 0) && (i2 > 0)) {
        if (k != 0)
        {
          j = mBottom;
          top = j;
          bottom = (j + i2);
          drawOverscrollFooter(paramCanvas, localDrawable2, localRect);
        }
        else if (m != 0)
        {
          top = i9;
          bottom = (i9 + i);
          drawDivider(paramCanvas, localRect, -1);
        }
      }
    }
    label1195:
    super.dispatchDraw(paramCanvas);
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    boolean bool1 = super.dispatchKeyEvent(paramKeyEvent);
    boolean bool2 = bool1;
    if (!bool1)
    {
      bool2 = bool1;
      if (getFocusedChild() != null)
      {
        bool2 = bool1;
        if (paramKeyEvent.getAction() == 0) {
          bool2 = onKeyDown(paramKeyEvent.getKeyCode(), paramKeyEvent);
        }
      }
    }
    return bool2;
  }
  
  protected boolean drawChild(Canvas paramCanvas, View paramView, long paramLong)
  {
    boolean bool = super.drawChild(paramCanvas, paramView, paramLong);
    if ((mCachingActive) && (mCachingFailed)) {
      mCachingActive = false;
    }
    return bool;
  }
  
  void drawDivider(Canvas paramCanvas, Rect paramRect, int paramInt)
  {
    Drawable localDrawable = mDivider;
    localDrawable.setBounds(paramRect);
    localDrawable.draw(paramCanvas);
  }
  
  void drawOverscrollFooter(Canvas paramCanvas, Drawable paramDrawable, Rect paramRect)
  {
    int i = paramDrawable.getMinimumHeight();
    paramCanvas.save();
    paramCanvas.clipRect(paramRect);
    if (bottom - top < i) {
      bottom = (top + i);
    }
    paramDrawable.setBounds(paramRect);
    paramDrawable.draw(paramCanvas);
    paramCanvas.restore();
  }
  
  void drawOverscrollHeader(Canvas paramCanvas, Drawable paramDrawable, Rect paramRect)
  {
    int i = paramDrawable.getMinimumHeight();
    paramCanvas.save();
    paramCanvas.clipRect(paramRect);
    if (bottom - top < i) {
      top = (bottom - i);
    }
    paramDrawable.setBounds(paramRect);
    paramDrawable.draw(paramCanvas);
    paramCanvas.restore();
  }
  
  protected void encodeProperties(ViewHierarchyEncoder paramViewHierarchyEncoder)
  {
    super.encodeProperties(paramViewHierarchyEncoder);
    paramViewHierarchyEncoder.addProperty("recycleOnMeasure", recycleOnMeasure());
  }
  
  void fillGap(boolean paramBoolean)
  {
    fillGap(paramBoolean, false);
  }
  
  int findMotionRow(int paramInt)
  {
    int i = getChildCount();
    if (i > 0)
    {
      if (!mStackFromBottom) {
        for (j = 0; j < i; j++) {
          if (paramInt <= getChildAt(j).getBottom()) {
            return mFirstPosition + j;
          }
        }
      }
      for (int j = i - 1; j >= 0; j--) {
        if (paramInt >= getChildAt(j).getTop()) {
          return mFirstPosition + j;
        }
      }
    }
    return -1;
  }
  
  void findNextGap(boolean paramBoolean)
  {
    int i = getChildCount();
    if (mHandler == null) {
      mHandler = new Handler();
    }
    if (mFillNextGap == null) {
      mFillNextGap = new FillNextGap(null);
    }
    int j;
    if (paramBoolean)
    {
      j = 0;
      if ((mGroupFlags & 0x22) == 34) {
        j = getListPaddingTop();
      }
      if (i > 0) {
        j = getChildAt(i - 1).getBottom() + mDividerHeight;
      }
      int k = mBottom - mTop;
      i = k;
      if ((mGroupFlags & 0x22) == 34) {
        i = k - mListPadding.bottom;
      }
      if ((j - mOldIncrementalDeltaY < i) && (mFillNextGap != null) && (mHandler != null))
      {
        mFillNextGap.down = paramBoolean;
        mHandler.post(mFillNextGap);
      }
    }
    else
    {
      j = 0;
      if ((mGroupFlags & 0x22) == 34) {
        j = getListPaddingBottom();
      }
      if (i > 0) {
        j = getChildAt(0).getTop() - mDividerHeight;
      } else {
        j = getHeight() - j;
      }
      i = 0;
      if ((mGroupFlags & 0x22) == 34) {
        i = mListPadding.top;
      }
      if ((mOldIncrementalDeltaY + j > i) && (mFillNextGap != null) && (mHandler != null))
      {
        mFillNextGap.down = paramBoolean;
        mHandler.post(mFillNextGap);
      }
    }
  }
  
  View findViewByPredicateInHeadersOrFooters(ArrayList<FixedViewInfo> paramArrayList, Predicate<View> paramPredicate, View paramView)
  {
    if (paramArrayList != null)
    {
      int i = paramArrayList.size();
      for (int j = 0; j < i; j++)
      {
        View localView = getview;
        if ((localView != paramView) && (!localView.isRootNamespace()))
        {
          localView = localView.findViewByPredicate(paramPredicate);
          if (localView != null) {
            return localView;
          }
        }
      }
    }
    return null;
  }
  
  protected <T extends View> T findViewByPredicateTraversal(Predicate<View> paramPredicate, View paramView)
  {
    View localView = super.findViewByPredicateTraversal(paramPredicate, paramView);
    Object localObject = localView;
    if (localView == null)
    {
      localObject = findViewByPredicateInHeadersOrFooters(mHeaderViewInfos, paramPredicate, paramView);
      if (localObject != null) {
        return localObject;
      }
      paramPredicate = findViewByPredicateInHeadersOrFooters(mFooterViewInfos, paramPredicate, paramView);
      localObject = paramPredicate;
      if (paramPredicate != null) {
        return paramPredicate;
      }
    }
    return localObject;
  }
  
  View findViewInHeadersOrFooters(ArrayList<FixedViewInfo> paramArrayList, int paramInt)
  {
    if (paramArrayList != null)
    {
      int i = paramArrayList.size();
      for (int j = 0; j < i; j++)
      {
        View localView = getview;
        if (!localView.isRootNamespace())
        {
          localView = localView.findViewById(paramInt);
          if (localView != null) {
            return localView;
          }
        }
      }
    }
    return null;
  }
  
  protected <T extends View> T findViewTraversal(int paramInt)
  {
    View localView1 = super.findViewTraversal(paramInt);
    View localView2 = localView1;
    if (localView1 == null)
    {
      localView2 = findViewInHeadersOrFooters(mHeaderViewInfos, paramInt);
      if (localView2 != null) {
        return localView2;
      }
      localView1 = findViewInHeadersOrFooters(mFooterViewInfos, paramInt);
      localView2 = localView1;
      if (localView1 != null) {
        return localView1;
      }
    }
    return localView2;
  }
  
  View findViewWithTagInHeadersOrFooters(ArrayList<FixedViewInfo> paramArrayList, Object paramObject)
  {
    if (paramArrayList != null)
    {
      int i = paramArrayList.size();
      for (int j = 0; j < i; j++)
      {
        View localView = getview;
        if (!localView.isRootNamespace())
        {
          localView = localView.findViewWithTag(paramObject);
          if (localView != null) {
            return localView;
          }
        }
      }
    }
    return null;
  }
  
  protected <T extends View> T findViewWithTagTraversal(Object paramObject)
  {
    View localView = super.findViewWithTagTraversal(paramObject);
    Object localObject = localView;
    if (localView == null)
    {
      localObject = findViewWithTagInHeadersOrFooters(mHeaderViewInfos, paramObject);
      if (localObject != null) {
        return localObject;
      }
      paramObject = findViewWithTagInHeadersOrFooters(mFooterViewInfos, paramObject);
      localObject = paramObject;
      if (paramObject != null) {
        return paramObject;
      }
    }
    return localObject;
  }
  
  boolean fullScroll(int paramInt)
  {
    boolean bool1 = false;
    boolean bool2;
    if (paramInt == 33)
    {
      bool2 = bool1;
      if (mSelectedPosition != 0)
      {
        paramInt = lookForSelectablePositionAfter(mSelectedPosition, 0, true);
        if (paramInt >= 0)
        {
          mLayoutMode = 1;
          setSelectionInt(paramInt);
          invokeOnItemScrollListener();
        }
        bool2 = true;
      }
    }
    else
    {
      bool2 = bool1;
      if (paramInt == 130)
      {
        paramInt = mItemCount - 1;
        bool2 = bool1;
        if (mSelectedPosition < paramInt)
        {
          paramInt = lookForSelectablePositionAfter(mSelectedPosition, paramInt, false);
          if (paramInt >= 0)
          {
            mLayoutMode = 3;
            setSelectionInt(paramInt);
            invokeOnItemScrollListener();
          }
          bool2 = true;
        }
      }
    }
    if ((bool2) && (!awakenScrollBars()))
    {
      awakenScrollBars();
      invalidate();
    }
    return bool2;
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return ListView.class.getName();
  }
  
  public ListAdapter getAdapter()
  {
    return mAdapter;
  }
  
  @Deprecated
  public long[] getCheckItemIds()
  {
    if ((mAdapter != null) && (mAdapter.hasStableIds())) {
      return getCheckedItemIds();
    }
    if ((mChoiceMode != 0) && (mCheckStates != null) && (mAdapter != null))
    {
      SparseBooleanArray localSparseBooleanArray = mCheckStates;
      int i = localSparseBooleanArray.size();
      long[] arrayOfLong = new long[i];
      Object localObject = mAdapter;
      int j = 0;
      int k = 0;
      while (k < i)
      {
        int m = j;
        if (localSparseBooleanArray.valueAt(k))
        {
          arrayOfLong[j] = ((ListAdapter)localObject).getItemId(localSparseBooleanArray.keyAt(k));
          m = j + 1;
        }
        k++;
        j = m;
      }
      if (j == i) {
        return arrayOfLong;
      }
      localObject = new long[j];
      System.arraycopy(arrayOfLong, 0, localObject, 0, j);
      return localObject;
    }
    return new long[0];
  }
  
  public Drawable getDivider()
  {
    return mDivider;
  }
  
  public int getDividerHeight()
  {
    return mDividerHeight;
  }
  
  public int getFooterViewsCount()
  {
    return mFooterViewInfos.size();
  }
  
  public int getHeaderViewsCount()
  {
    return mHeaderViewInfos.size();
  }
  
  int getHeightForPosition(int paramInt)
  {
    int i = super.getHeightForPosition(paramInt);
    if (shouldAdjustHeightForDivider(paramInt)) {
      return mDividerHeight + i;
    }
    return i;
  }
  
  public boolean getItemsCanFocus()
  {
    return mItemsCanFocus;
  }
  
  public int getMaxScrollAmount()
  {
    return (int)(0.33F * (mBottom - mTop));
  }
  
  public Drawable getOverscrollFooter()
  {
    return mOverScrollFooter;
  }
  
  public Drawable getOverscrollHeader()
  {
    return mOverScrollHeader;
  }
  
  public boolean isOpaque()
  {
    boolean bool;
    if (((mCachingActive) && (mIsCacheColorOpaque) && (mDividerIsOpaque) && (hasOpaqueScrollbars())) || (super.isOpaque())) {
      bool = true;
    } else {
      bool = false;
    }
    if (bool)
    {
      int i;
      if (mListPadding != null) {
        i = mListPadding.top;
      } else {
        i = mPaddingTop;
      }
      View localView = getChildAt(0);
      if ((localView != null) && (localView.getTop() <= i))
      {
        int j = getHeight();
        if (mListPadding != null) {
          i = mListPadding.bottom;
        } else {
          i = mPaddingBottom;
        }
        localView = getChildAt(getChildCount() - 1);
        if ((localView == null) || (localView.getBottom() < j - i)) {
          return false;
        }
      }
      else
      {
        return false;
      }
    }
    return bool;
  }
  
  protected void layoutChildren()
  {
    boolean bool1 = mBlockLayoutRequests;
    if (bool1) {
      return;
    }
    mBlockLayoutRequests = true;
    try
    {
      super.layoutChildren();
      invalidate();
      ListAdapter localListAdapter = mAdapter;
      if (localListAdapter == null) {
        try
        {
          resetList();
          invokeOnItemScrollListener();
          if (mFocusSelector != null) {
            mFocusSelector.onLayoutComplete();
          }
          if (!bool1) {
            mBlockLayoutRequests = false;
          }
          return;
        }
        finally
        {
          break label1751;
        }
      }
      int i = mListPadding.top;
      int j = mBottom - mTop - mListPadding.bottom;
      int k = getChildCount();
      int m = 0;
      int n = 0;
      int i1 = 0;
      Object localObject5 = null;
      Object localObject6 = null;
      Object localObject7 = null;
      Object localObject8 = null;
      int i2;
      Object localObject9;
      Object localObject10;
      switch (mLayoutMode)
      {
      default: 
        m = mSelectedPosition;
        break;
      case 2: 
        i2 = mNextSelectedPosition - mFirstPosition;
        m = i2;
        n = i1;
        localObject9 = localObject6;
        localObject10 = localObject7;
        localObject2 = localObject8;
        if (i2 >= 0)
        {
          m = i2;
          n = i1;
          localObject9 = localObject6;
          localObject10 = localObject7;
          localObject2 = localObject8;
          if (i2 < k)
          {
            localObject2 = getChildAt(i2);
            m = i2;
            n = i1;
            localObject9 = localObject6;
            localObject10 = localObject7;
          }
        }
        break;
      case 1: 
      case 3: 
      case 4: 
      case 5: 
        localObject2 = localObject8;
        localObject10 = localObject7;
        localObject9 = localObject6;
        n = i1;
      }
      for (;;)
      {
        break;
        i1 = mFirstPosition;
        m -= i1;
        localObject2 = localObject5;
        if (m >= 0)
        {
          localObject2 = localObject5;
          if (m < k) {
            localObject2 = getChildAt(m);
          }
        }
        localObject10 = getChildAt(0);
        i1 = mNextSelectedPosition;
        if (i1 >= 0)
        {
          n = mNextSelectedPosition;
          i1 = mSelectedPosition;
          n -= i1;
        }
        localObject7 = getChildAt(m + n);
        localObject9 = localObject2;
        localObject2 = localObject7;
      }
      boolean bool2 = mDataChanged;
      if (bool2) {
        handleDataChanged();
      }
      m = mItemCount;
      if (m == 0)
      {
        resetList();
        invokeOnItemScrollListener();
        if (mFocusSelector != null) {
          mFocusSelector.onLayoutComplete();
        }
        if (!bool1) {
          mBlockLayoutRequests = false;
        }
        return;
      }
      AbsListView.RecycleBin localRecycleBin;
      Object localObject12;
      ViewRootImpl localViewRootImpl;
      if (mItemCount == mAdapter.getCount())
      {
        setSelectedPositionInt(mNextSelectedPosition);
        Object localObject11 = null;
        localObject6 = null;
        localRecycleBin = null;
        localObject12 = null;
        i1 = -1;
        localViewRootImpl = getViewRootImpl();
        localObject7 = localObject11;
        localObject5 = localRecycleBin;
        m = i1;
        if (localViewRootImpl != null)
        {
          localObject8 = localViewRootImpl.getAccessibilityFocusedHost();
          localObject7 = localObject11;
          localObject5 = localRecycleBin;
          m = i1;
          if (localObject8 != null)
          {
            View localView = getAccessibilityFocusedChild((View)localObject8);
            localObject7 = localObject11;
            localObject5 = localRecycleBin;
            m = i1;
            if (localView != null)
            {
              if ((bool2) && (!isDirectChildHeaderOrFooter(localView)))
              {
                localObject7 = localObject6;
                localObject5 = localObject12;
                if (localView.hasTransientState())
                {
                  localObject7 = localObject6;
                  localObject5 = localObject12;
                  if (!mAdapterHasStableIds) {}
                }
              }
              else
              {
                localObject5 = localObject8;
                localObject7 = localViewRootImpl.getAccessibilityFocusedVirtualView();
              }
              m = getPositionForView(localView);
            }
          }
        }
        localObject12 = null;
        localRecycleBin = null;
        localObject6 = null;
        localObject11 = null;
        localObject8 = getFocusedChild();
        if (localObject8 != null)
        {
          if ((bool2) && (!isDirectChildHeaderOrFooter((View)localObject8)) && (!((View)localObject8).hasTransientState()))
          {
            localObject12 = localRecycleBin;
            localObject6 = localObject11;
            if (!mAdapterHasStableIds) {}
          }
          else
          {
            localObject6 = findFocus();
            if (localObject6 != null) {
              ((View)localObject6).dispatchStartTemporaryDetach();
            }
            localObject12 = localObject8;
          }
          requestFocus();
        }
        i2 = mFirstPosition;
        localRecycleBin = mRecycler;
        if (bool2)
        {
          for (i1 = 0; i1 < k; i1++) {
            localRecycleBin.addScrapView(getChildAt(i1), i2 + i1);
          }
          localObject11 = localObject8;
          localObject8 = localObject5;
        }
        else
        {
          localObject11 = localObject8;
          localObject8 = localObject5;
          localRecycleBin.fillActiveViews(k, i2);
        }
        detachAllViewsFromParent();
        localRecycleBin.removeSkippedScrap();
        i1 = mLayoutMode;
        switch (i1)
        {
        default: 
          if (k != 0) {
            break;
          }
        }
      }
      try
      {
        bool2 = mStackFromBottom;
        break label996;
        localObject2 = moveSelection(localObject9, (View)localObject2, n, i, j);
        break label1157;
        localObject2 = fillSpecific(mSyncPosition, mSpecificTop);
        break label1157;
        n = reconcileSelectedPosition();
        localObject5 = fillSpecific(n, mSpecificTop);
        localObject2 = localObject5;
        if (localObject5 == null)
        {
          localObject2 = localObject5;
          if (mFocusSelector != null)
          {
            localObject2 = mFocusSelector.setupFocusIfValid(n);
            if (localObject2 != null) {
              post((Runnable)localObject2);
            }
          }
        }
        for (localObject2 = localObject5;; localObject2 = fillSpecific(0, i))
        {
          break;
          localObject2 = fillUp(mItemCount - 1, j);
          adjustViewsUpOrDown();
          break;
          if (localObject2 != null) {
            localObject2 = fillFromSelection(((View)localObject2).getTop(), i, j);
          }
          for (;;)
          {
            break label1157;
            localObject2 = fillFromMiddle(i, j);
            break label1157;
            mFirstPosition = 0;
            localObject2 = fillFromTop(i);
            adjustViewsUpOrDown();
            break label1157;
            label996:
            if (!bool2)
            {
              setSelectedPositionInt(lookForSelectablePosition(0, true));
              localObject2 = fillFromTop(i);
            }
            else
            {
              setSelectedPositionInt(lookForSelectablePosition(mItemCount - 1, false));
              localObject2 = fillUp(mItemCount - 1, j);
              break label1157;
              if ((mSelectedPosition >= 0) && (mSelectedPosition < mItemCount))
              {
                i1 = mSelectedPosition;
                if (localObject9 == null) {
                  n = i;
                } else {
                  n = localObject9.getTop();
                }
                localObject2 = fillSpecific(i1, n);
                continue;
              }
              if (mFirstPosition < mItemCount)
              {
                n = mFirstPosition;
                if (localObject10 != null) {
                  i = ((View)localObject10).getTop();
                }
                localObject2 = fillSpecific(n, i);
              }
            }
          }
        }
        label1157:
        n = m;
        localRecycleBin.scrapActiveViews();
        removeUnusedFixedViews(mHeaderViewInfos);
        removeUnusedFixedViews(mFooterViewInfos);
        if (localObject2 != null)
        {
          if ((mItemsCanFocus) && (hasFocus()) && (!((View)localObject2).hasFocus()))
          {
            if (((localObject2 == localObject12) && (localObject6 != null) && (((View)localObject6).requestFocus())) || (((View)localObject2).requestFocus())) {
              m = 1;
            } else {
              m = 0;
            }
            if (m == 0)
            {
              localObject5 = getFocusedChild();
              if (localObject5 != null) {
                ((View)localObject5).clearFocus();
              }
              positionSelector(-1, (View)localObject2);
            }
            else
            {
              ((View)localObject2).setSelected(false);
              mSelectorRect.setEmpty();
            }
          }
          else
          {
            positionSelector(-1, (View)localObject2);
          }
          mSelectedTop = ((View)localObject2).getTop();
        }
        else
        {
          if ((mTouchMode != 1) && (mTouchMode != 2)) {
            m = 0;
          } else {
            m = 1;
          }
          if (m != 0)
          {
            localObject2 = getChildAt(mMotionPosition - mFirstPosition);
            if (localObject2 != null) {
              positionSelector(mMotionPosition, (View)localObject2);
            }
          }
          else if (mSelectorPosition != -1)
          {
            localObject2 = getChildAt(mSelectorPosition - mFirstPosition);
            if (localObject2 != null) {
              positionSelector(mSelectorPosition, (View)localObject2);
            }
          }
          else
          {
            mSelectedTop = 0;
            mSelectorRect.setEmpty();
          }
          if ((hasFocus()) && (localObject6 != null)) {
            ((View)localObject6).requestFocus();
          }
        }
        if ((localViewRootImpl != null) && (localViewRootImpl.getAccessibilityFocusedHost() == null))
        {
          if (localObject8 != null) {
            if (((View)localObject8).isAttachedToWindow())
            {
              localObject2 = ((View)localObject8).getAccessibilityNodeProvider();
              if ((localObject7 != null) && (localObject2 != null)) {
                ((AccessibilityNodeProvider)localObject2).performAction(AccessibilityNodeInfo.getVirtualDescendantId(((AccessibilityNodeInfo)localObject7).getSourceNodeId()), 64, null);
              } else {
                ((View)localObject8).requestAccessibilityFocus();
              }
              break label1547;
            }
          }
          if (n != -1)
          {
            localObject2 = getChildAt(MathUtils.constrain(n - mFirstPosition, 0, getChildCount() - 1));
            if (localObject2 != null) {
              ((View)localObject2).requestAccessibilityFocus();
            }
          }
        }
        label1547:
        if ((localObject6 != null) && (((View)localObject6).getWindowToken() != null)) {
          ((View)localObject6).dispatchFinishTemporaryDetach();
        }
        mLayoutMode = 0;
        mDataChanged = false;
        if (mPositionScrollAfterLayout != null)
        {
          post(mPositionScrollAfterLayout);
          mPositionScrollAfterLayout = null;
        }
        mNeedSync = false;
        setNextSelectedPositionInt(mSelectedPosition);
        updateScrollIndicators();
        if (mItemCount > 0) {
          checkSelectionChanged();
        }
        invokeOnItemScrollListener();
        if (mFocusSelector != null) {
          mFocusSelector.onLayoutComplete();
        }
        if (!bool1) {
          mBlockLayoutRequests = false;
        }
        return;
      }
      finally
      {
        break label1751;
      }
      Object localObject2 = new java/lang/IllegalStateException;
      localObject7 = new java/lang/StringBuilder;
      ((StringBuilder)localObject7).<init>();
      ((StringBuilder)localObject7).append("The content of the adapter has changed but ListView did not receive a notification. Make sure the content of your adapter is not modified from a background thread, but only from the UI thread. Make sure your adapter calls notifyDataSetChanged() when its content changes. [in ListView(");
      ((StringBuilder)localObject7).append(getId());
      ((StringBuilder)localObject7).append(", ");
      ((StringBuilder)localObject7).append(getClass());
      ((StringBuilder)localObject7).append(") with Adapter(");
      ((StringBuilder)localObject7).append(mAdapter.getClass());
      ((StringBuilder)localObject7).append(")]");
      ((IllegalStateException)localObject2).<init>(((StringBuilder)localObject7).toString());
      throw ((Throwable)localObject2);
    }
    finally {}
    label1751:
    if (mFocusSelector != null) {
      mFocusSelector.onLayoutComplete();
    }
    if (!bool1) {
      mBlockLayoutRequests = false;
    }
    throw localObject4;
  }
  
  int lookForSelectablePosition(int paramInt, boolean paramBoolean)
  {
    ListAdapter localListAdapter = mAdapter;
    if ((localListAdapter != null) && (!isInTouchMode()))
    {
      int i = localListAdapter.getCount();
      int j = paramInt;
      if (!mAreAllItemsSelectable)
      {
        if (paramBoolean) {
          for (paramInt = Math.max(0, paramInt);; paramInt++)
          {
            j = paramInt;
            if (paramInt >= i) {
              break;
            }
            j = paramInt;
            if (localListAdapter.isEnabled(paramInt)) {
              break;
            }
          }
        }
        for (paramInt = Math.min(paramInt, i - 1);; paramInt--)
        {
          j = paramInt;
          if (paramInt < 0) {
            break;
          }
          j = paramInt;
          if (localListAdapter.isEnabled(paramInt)) {
            break;
          }
        }
      }
      if ((j >= 0) && (j < i)) {
        return j;
      }
      return -1;
    }
    return -1;
  }
  
  int lookForSelectablePositionAfter(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    ListAdapter localListAdapter = mAdapter;
    if ((localListAdapter != null) && (!isInTouchMode()))
    {
      int i = lookForSelectablePosition(paramInt2, paramBoolean);
      if (i != -1) {
        return i;
      }
      int j = localListAdapter.getCount();
      i = MathUtils.constrain(paramInt1, -1, j - 1);
      if (paramBoolean)
      {
        for (paramInt1 = Math.min(paramInt2 - 1, j - 1); (paramInt1 > i) && (!localListAdapter.isEnabled(paramInt1)); paramInt1--) {}
        paramInt2 = paramInt1;
        if (paramInt1 <= i) {
          return -1;
        }
      }
      else
      {
        for (paramInt1 = Math.max(0, paramInt2 + 1); (paramInt1 < i) && (!localListAdapter.isEnabled(paramInt1)); paramInt1++) {}
        paramInt2 = paramInt1;
        if (paramInt1 >= i) {
          return -1;
        }
      }
      return paramInt2;
    }
    return -1;
  }
  
  final int measureHeightOfChildren(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    Object localObject = mAdapter;
    if (localObject == null) {
      return mListPadding.top + mListPadding.bottom;
    }
    int i = mListPadding.top;
    int j = mListPadding.bottom;
    int k = mDividerHeight;
    int m;
    if (paramInt3 == -1) {
      m = ((ListAdapter)localObject).getCount() - 1;
    } else {
      m = paramInt3;
    }
    localObject = mRecycler;
    boolean bool = recycleOnMeasure();
    boolean[] arrayOfBoolean = mIsScrap;
    paramInt3 = 0;
    j = i + j;
    i = paramInt2;
    paramInt2 = paramInt3;
    paramInt3 = j;
    while (i <= m)
    {
      View localView = obtainView(i, arrayOfBoolean);
      measureScrapChild(localView, i, paramInt1, paramInt4);
      j = paramInt3;
      if (i > 0) {
        j = paramInt3 + k;
      }
      if ((bool) && (((AbsListView.RecycleBin)localObject).shouldRecycleViewType(getLayoutParamsviewType))) {
        ((AbsListView.RecycleBin)localObject).addScrapView(localView, -1);
      }
      paramInt3 = j + localView.getMeasuredHeight();
      if (paramInt3 >= paramInt4)
      {
        if ((paramInt5 >= 0) && (i > paramInt5) && (paramInt2 > 0) && (paramInt3 != paramInt4)) {
          paramInt4 = paramInt2;
        }
        return paramInt4;
      }
      j = paramInt2;
      if (paramInt5 >= 0)
      {
        j = paramInt2;
        if (i >= paramInt5) {
          j = paramInt3;
        }
      }
      i++;
      paramInt2 = j;
    }
    return paramInt3;
  }
  
  protected void onDetachedFromWindow()
  {
    if (mFocusSelector != null)
    {
      removeCallbacks(mFocusSelector);
      mFocusSelector = null;
    }
    super.onDetachedFromWindow();
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    int i = getChildCount();
    if (i > 0)
    {
      for (int j = 0; j < i; j++) {
        addHeaderView(getChildAt(j));
      }
      removeAllViews();
    }
  }
  
  protected void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect)
  {
    super.onFocusChanged(paramBoolean, paramInt, paramRect);
    ListAdapter localListAdapter = mAdapter;
    int i = -1;
    int j = 0;
    int k = 0;
    int m = i;
    int n = j;
    if (localListAdapter != null)
    {
      m = i;
      n = j;
      if (paramBoolean)
      {
        m = i;
        n = j;
        if (paramRect != null)
        {
          paramRect.offset(mScrollX, mScrollY);
          m = localListAdapter.getCount();
          int i1 = getChildCount();
          n = mFirstPosition;
          j = 0;
          if (m < i1 + n)
          {
            mLayoutMode = 0;
            layoutChildren();
          }
          Rect localRect = mTempRect;
          i1 = Integer.MAX_VALUE;
          int i2 = getChildCount();
          int i3 = mFirstPosition;
          for (;;)
          {
            m = i;
            n = k;
            if (j >= i2) {
              break;
            }
            if (!localListAdapter.isEnabled(i3 + j))
            {
              m = i1;
            }
            else
            {
              View localView = getChildAt(j);
              localView.getDrawingRect(localRect);
              offsetDescendantRectToMyCoords(localView, localRect);
              n = getDistance(paramRect, localRect, paramInt);
              m = i1;
              if (n < i1)
              {
                m = n;
                i = j;
                k = localView.getTop();
              }
            }
            j++;
            i1 = m;
          }
        }
      }
    }
    if (m >= 0) {
      setSelectionFromTop(mFirstPosition + m, n);
    } else {
      requestLayout();
    }
  }
  
  public void onInitializeAccessibilityNodeInfoForItem(View paramView, int paramInt, AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfoForItem(paramView, paramInt, paramAccessibilityNodeInfo);
    paramView = (AbsListView.LayoutParams)paramView.getLayoutParams();
    boolean bool;
    if ((paramView != null) && (viewType == -2)) {
      bool = true;
    } else {
      bool = false;
    }
    paramAccessibilityNodeInfo.setCollectionItemInfo(AccessibilityNodeInfo.CollectionItemInfo.obtain(paramInt, 1, 0, 1, bool, isItemChecked(paramInt)));
  }
  
  public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfoInternal(paramAccessibilityNodeInfo);
    int i = getCount();
    paramAccessibilityNodeInfo.setCollectionInfo(AccessibilityNodeInfo.CollectionInfo.obtain(i, 1, false, getSelectionModeForAccessibility()));
    if (i > 0) {
      paramAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_TO_POSITION);
    }
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    return commonKey(paramInt, 1, paramKeyEvent);
  }
  
  public boolean onKeyMultiple(int paramInt1, int paramInt2, KeyEvent paramKeyEvent)
  {
    return commonKey(paramInt1, paramInt2, paramKeyEvent);
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    return commonKey(paramInt, 1, paramKeyEvent);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = View.MeasureSpec.getMode(paramInt2);
    int k = View.MeasureSpec.getSize(paramInt1);
    int m = View.MeasureSpec.getSize(paramInt2);
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    if (mAdapter == null) {
      paramInt2 = 0;
    } else {
      paramInt2 = mAdapter.getCount();
    }
    mItemCount = paramInt2;
    int i3 = n;
    paramInt2 = i1;
    int i4 = i2;
    if (mItemCount > 0) {
      if (i != 0)
      {
        i3 = n;
        paramInt2 = i1;
        i4 = i2;
        if (j != 0) {}
      }
      else
      {
        View localView = obtainView(0, mIsScrap);
        measureScrapChild(localView, 0, paramInt1, m);
        i1 = localView.getMeasuredWidth();
        n = localView.getMeasuredHeight();
        i2 = combineMeasuredStates(0, localView.getMeasuredState());
        i3 = i1;
        paramInt2 = n;
        i4 = i2;
        if (recycleOnMeasure())
        {
          i3 = i1;
          paramInt2 = n;
          i4 = i2;
          if (mRecycler.shouldRecycleViewType(getLayoutParamsviewType))
          {
            mRecycler.addScrapView(localView, 0);
            i4 = i2;
            paramInt2 = n;
            i3 = i1;
          }
        }
      }
    }
    if (i == 0) {}
    for (i4 = mListPadding.left + mListPadding.right + i3 + getVerticalScrollbarWidth();; i4 = 0xFF000000 & i4 | k) {
      break;
    }
    if (j == 0) {
      paramInt2 = mListPadding.top + mListPadding.bottom + paramInt2 + getVerticalFadingEdgeLength() * 2;
    } else {
      paramInt2 = m;
    }
    i3 = paramInt2;
    if (j == Integer.MIN_VALUE) {
      i3 = measureHeightOfChildren(paramInt1, 0, -1, paramInt2, -1);
    }
    setMeasuredDimension(i4, i3);
    mWidthMeasureSpec = paramInt1;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (getChildCount() > 0)
    {
      View localView = getFocusedChild();
      if (localView != null)
      {
        int i = mFirstPosition;
        int j = indexOfChild(localView);
        int k = Math.max(0, localView.getBottom() - (paramInt2 - mPaddingTop));
        int m = localView.getTop();
        if (mFocusSelector == null) {
          mFocusSelector = new FocusSelector(null);
        }
        post(mFocusSelector.setupForSetSelection(i + j, m - k));
      }
    }
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  boolean pageScroll(int paramInt)
  {
    if (paramInt == 33) {
      paramInt = Math.max(0, mSelectedPosition - getChildCount() - 1);
    }
    for (boolean bool = false;; bool = true)
    {
      break;
      if (paramInt != 130) {
        break label160;
      }
      paramInt = Math.min(mItemCount - 1, mSelectedPosition + getChildCount() - 1);
    }
    if (paramInt >= 0)
    {
      paramInt = lookForSelectablePositionAfter(mSelectedPosition, paramInt, bool);
      if (paramInt >= 0)
      {
        mLayoutMode = 4;
        mSpecificTop = (mPaddingTop + getVerticalFadingEdgeLength());
        if ((bool) && (paramInt > mItemCount - getChildCount())) {
          mLayoutMode = 3;
        }
        if ((!bool) && (paramInt < getChildCount())) {
          mLayoutMode = 1;
        }
        setSelectionInt(paramInt);
        invokeOnItemScrollListener();
        if (!awakenScrollBars()) {
          invalidate();
        }
        return true;
      }
    }
    return false;
    label160:
    return false;
  }
  
  public boolean performAccessibilityActionInternal(int paramInt, Bundle paramBundle)
  {
    if (super.performAccessibilityActionInternal(paramInt, paramBundle)) {
      return true;
    }
    if (paramInt == 16908343)
    {
      paramInt = paramBundle.getInt("android.view.accessibility.action.ARGUMENT_ROW_INT", -1);
      int i = Math.min(paramInt, getCount() - 1);
      if (paramInt >= 0)
      {
        smoothScrollToPosition(i);
        return true;
      }
    }
    return false;
  }
  
  @ViewDebug.ExportedProperty(category="list")
  protected boolean recycleOnMeasure()
  {
    return true;
  }
  
  public boolean removeFooterView(View paramView)
  {
    if (mFooterViewInfos.size() > 0)
    {
      boolean bool1 = false;
      boolean bool2 = bool1;
      if (mAdapter != null)
      {
        bool2 = bool1;
        if (((HeaderViewListAdapter)mAdapter).removeFooter(paramView))
        {
          if (mDataSetObserver != null) {
            mDataSetObserver.onChanged();
          }
          bool2 = true;
        }
      }
      removeFixedViewInfo(paramView, mFooterViewInfos);
      return bool2;
    }
    return false;
  }
  
  public boolean removeHeaderView(View paramView)
  {
    if (mHeaderViewInfos.size() > 0)
    {
      boolean bool1 = false;
      boolean bool2 = bool1;
      if (mAdapter != null)
      {
        bool2 = bool1;
        if (((HeaderViewListAdapter)mAdapter).removeHeader(paramView))
        {
          if (mDataSetObserver != null) {
            mDataSetObserver.onChanged();
          }
          bool2 = true;
        }
      }
      removeFixedViewInfo(paramView, mHeaderViewInfos);
      return bool2;
    }
    return false;
  }
  
  public boolean requestChildRectangleOnScreen(View paramView, Rect paramRect, boolean paramBoolean)
  {
    int i = top;
    paramRect.offset(paramView.getLeft(), paramView.getTop());
    paramRect.offset(-paramView.getScrollX(), -paramView.getScrollY());
    int j = getHeight();
    int k = getScrollY();
    int m = k + j;
    int n = getVerticalFadingEdgeLength();
    int i1 = k;
    if (showingTopFadingEdge()) {
      if (mSelectedPosition <= 0)
      {
        i1 = k;
        if (i <= n) {}
      }
      else
      {
        i1 = k + n;
      }
    }
    i = getChildAt(getChildCount() - 1).getBottom();
    k = m;
    if (showingBottomFadingEdge()) {
      if (mSelectedPosition >= mItemCount - 1)
      {
        k = m;
        if (bottom >= i - n) {}
      }
      else
      {
        k = m - n;
      }
    }
    n = 0;
    m = bottom;
    paramBoolean = false;
    if ((m > k) && (top > i1))
    {
      if (paramRect.height() > j) {
        m = 0 + (top - i1);
      } else {
        m = 0 + (bottom - k);
      }
      m = Math.min(m, i - k);
    }
    else
    {
      m = n;
      if (top < i1)
      {
        m = n;
        if (bottom < k)
        {
          if (paramRect.height() > j) {
            m = 0 - (k - bottom);
          } else {
            m = 0 - (i1 - top);
          }
          m = Math.max(m, getChildAt(0).getTop() - i1);
        }
      }
    }
    if (m != 0) {
      paramBoolean = true;
    }
    if (paramBoolean)
    {
      scrollListItemsBy(-m);
      positionSelector(-1, paramView);
      mSelectedTop = paramView.getTop();
      invalidate();
    }
    return paramBoolean;
  }
  
  void resetList()
  {
    clearRecycledState(mHeaderViewInfos);
    clearRecycledState(mFooterViewInfos);
    super.resetList();
    mLayoutMode = 0;
  }
  
  public void setAdapter(ListAdapter paramListAdapter)
  {
    if ((mAdapter != null) && (mDataSetObserver != null)) {
      mAdapter.unregisterDataSetObserver(mDataSetObserver);
    }
    resetList();
    mRecycler.clear();
    if ((mHeaderViewInfos.size() <= 0) && (mFooterViewInfos.size() <= 0)) {
      mAdapter = paramListAdapter;
    } else {
      mAdapter = wrapHeaderListAdapterInternal(mHeaderViewInfos, mFooterViewInfos, paramListAdapter);
    }
    mOldSelectedPosition = -1;
    mOldSelectedRowId = Long.MIN_VALUE;
    super.setAdapter(paramListAdapter);
    if (mAdapter != null)
    {
      mAreAllItemsSelectable = mAdapter.areAllItemsEnabled();
      mOldItemCount = mItemCount;
      mItemCount = mAdapter.getCount();
      checkFocus();
      mDataSetObserver = new AbsListView.AdapterDataSetObserver(this);
      mAdapter.registerDataSetObserver(mDataSetObserver);
      mRecycler.setViewTypeCount(mAdapter.getViewTypeCount());
      int i;
      if (mStackFromBottom) {
        i = lookForSelectablePosition(mItemCount - 1, false);
      } else {
        i = lookForSelectablePosition(0, true);
      }
      setSelectedPositionInt(i);
      setNextSelectedPositionInt(i);
      if (mItemCount == 0) {
        checkSelectionChanged();
      }
    }
    else
    {
      mAreAllItemsSelectable = true;
      checkFocus();
      checkSelectionChanged();
    }
    requestLayout();
  }
  
  public void setCacheColorHint(int paramInt)
  {
    boolean bool;
    if (paramInt >>> 24 == 255) {
      bool = true;
    } else {
      bool = false;
    }
    mIsCacheColorOpaque = bool;
    if (bool)
    {
      if (mDividerPaint == null) {
        mDividerPaint = new Paint();
      }
      mDividerPaint.setColor(paramInt);
    }
    super.setCacheColorHint(paramInt);
  }
  
  public void setDivider(Drawable paramDrawable)
  {
    boolean bool = false;
    if (paramDrawable != null) {
      mDividerHeight = paramDrawable.getIntrinsicHeight();
    } else {
      mDividerHeight = 0;
    }
    mDivider = paramDrawable;
    if ((paramDrawable != null) && (paramDrawable.getOpacity() != -1)) {
      break label47;
    }
    bool = true;
    label47:
    mDividerIsOpaque = bool;
    requestLayout();
    invalidate();
  }
  
  public void setDividerHeight(int paramInt)
  {
    mDividerHeight = paramInt;
    requestLayout();
    invalidate();
  }
  
  public void setFooterDividersEnabled(boolean paramBoolean)
  {
    mFooterDividersEnabled = paramBoolean;
    invalidate();
  }
  
  public void setHeaderDividersEnabled(boolean paramBoolean)
  {
    mHeaderDividersEnabled = paramBoolean;
    invalidate();
  }
  
  public void setItemsCanFocus(boolean paramBoolean)
  {
    mItemsCanFocus = paramBoolean;
    if (!paramBoolean) {
      setDescendantFocusability(393216);
    }
  }
  
  public void setOverscrollFooter(Drawable paramDrawable)
  {
    mOverScrollFooter = paramDrawable;
    invalidate();
  }
  
  public void setOverscrollHeader(Drawable paramDrawable)
  {
    mOverScrollHeader = paramDrawable;
    if (mScrollY < 0) {
      invalidate();
    }
  }
  
  @RemotableViewMethod(asyncImpl="setRemoteViewsAdapterAsync")
  public void setRemoteViewsAdapter(Intent paramIntent)
  {
    super.setRemoteViewsAdapter(paramIntent);
  }
  
  public void setSelection(int paramInt)
  {
    setSelectionFromTop(paramInt, 0);
  }
  
  public void setSelectionAfterHeaderView()
  {
    int i = getHeaderViewsCount();
    if (i > 0)
    {
      mNextSelectedPosition = 0;
      return;
    }
    if (mAdapter != null)
    {
      setSelection(i);
    }
    else
    {
      mNextSelectedPosition = i;
      mLayoutMode = 2;
    }
  }
  
  void setSelectionInt(int paramInt)
  {
    setNextSelectedPositionInt(paramInt);
    int i = 0;
    int j = mSelectedPosition;
    int k = i;
    if (j >= 0) {
      if (paramInt == j - 1)
      {
        k = 1;
      }
      else
      {
        k = i;
        if (paramInt == j + 1) {
          k = 1;
        }
      }
    }
    if (mPositionScroller != null) {
      mPositionScroller.stop();
    }
    layoutChildren();
    if (k != 0) {
      awakenScrollBars();
    }
  }
  
  @RemotableViewMethod
  public void smoothScrollByOffset(int paramInt)
  {
    super.smoothScrollByOffset(paramInt);
  }
  
  @RemotableViewMethod
  public void smoothScrollToPosition(int paramInt)
  {
    super.smoothScrollToPosition(paramInt);
  }
  
  boolean trackMotionScroll(int paramInt1, int paramInt2)
  {
    boolean bool = super.trackMotionScroll(paramInt1, paramInt2);
    removeUnusedFixedViews(mHeaderViewInfos);
    removeUnusedFixedViews(mFooterViewInfos);
    return bool;
  }
  
  protected HeaderViewListAdapter wrapHeaderListAdapterInternal(ArrayList<FixedViewInfo> paramArrayList1, ArrayList<FixedViewInfo> paramArrayList2, ListAdapter paramListAdapter)
  {
    return new HeaderViewListAdapter(paramArrayList1, paramArrayList2, paramListAdapter);
  }
  
  protected void wrapHeaderListAdapterInternal()
  {
    mAdapter = wrapHeaderListAdapterInternal(mHeaderViewInfos, mFooterViewInfos, mAdapter);
  }
  
  private static class ArrowScrollFocusResult
  {
    private int mAmountToScroll;
    private int mSelectedPosition;
    
    private ArrowScrollFocusResult() {}
    
    public int getAmountToScroll()
    {
      return mAmountToScroll;
    }
    
    public int getSelectedPosition()
    {
      return mSelectedPosition;
    }
    
    void populate(int paramInt1, int paramInt2)
    {
      mSelectedPosition = paramInt1;
      mAmountToScroll = paramInt2;
    }
  }
  
  private class FillNextGap
    implements Runnable
  {
    public boolean down;
    
    private FillNextGap() {}
    
    public void run()
    {
      ListView.this.fillGap(down, true);
    }
  }
  
  public class FixedViewInfo
  {
    public Object data;
    public boolean isSelectable;
    public View view;
    
    public FixedViewInfo() {}
  }
  
  private class FocusSelector
    implements Runnable
  {
    private static final int STATE_REQUEST_FOCUS = 3;
    private static final int STATE_SET_SELECTION = 1;
    private static final int STATE_WAIT_FOR_LAYOUT = 2;
    private int mAction;
    private int mPosition;
    private int mPositionTop;
    
    private FocusSelector() {}
    
    void onLayoutComplete()
    {
      if (mAction == 2) {
        mAction = -1;
      }
    }
    
    public void run()
    {
      if (mAction == 1)
      {
        setSelectionFromTop(mPosition, mPositionTop);
        mAction = 2;
      }
      else if (mAction == 3)
      {
        int i = mPosition;
        int j = mFirstPosition;
        View localView = getChildAt(i - j);
        if (localView != null) {
          localView.requestFocus();
        }
        mAction = -1;
      }
    }
    
    Runnable setupFocusIfValid(int paramInt)
    {
      if ((mAction == 2) && (paramInt == mPosition))
      {
        mAction = 3;
        return this;
      }
      return null;
    }
    
    FocusSelector setupForSetSelection(int paramInt1, int paramInt2)
    {
      mPosition = paramInt1;
      mPositionTop = paramInt2;
      mAction = 1;
      return this;
    }
  }
}
