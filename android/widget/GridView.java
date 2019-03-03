package android.widget;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Trace;
import android.util.AttributeSet;
import android.util.MathUtils;
import android.util.SparseBooleanArray;
import android.view.Gravity;
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
import android.view.animation.GridLayoutAnimationController.AnimationParameters;
import com.android.internal.R.styleable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@RemoteViews.RemoteView
public class GridView
  extends AbsListView
{
  public static final int AUTO_FIT = -1;
  public static final int NO_STRETCH = 0;
  public static final int STRETCH_COLUMN_WIDTH = 2;
  public static final int STRETCH_SPACING = 1;
  public static final int STRETCH_SPACING_UNIFORM = 3;
  private int mColumnWidth;
  private int mGravity = 8388611;
  private int mHorizontalSpacing = 0;
  private int mNumColumns = -1;
  private View mReferenceView = null;
  private View mReferenceViewInSelectedRow = null;
  private int mRequestedColumnWidth;
  private int mRequestedHorizontalSpacing;
  private int mRequestedNumColumns;
  private int mStretchMode = 2;
  private final Rect mTempRect = new Rect();
  private int mVerticalSpacing = 0;
  
  public GridView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public GridView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842865);
  }
  
  public GridView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public GridView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.GridView, paramInt1, paramInt2);
    setHorizontalSpacing(paramContext.getDimensionPixelOffset(1, 0));
    setVerticalSpacing(paramContext.getDimensionPixelOffset(2, 0));
    paramInt1 = paramContext.getInt(3, 2);
    if (paramInt1 >= 0) {
      setStretchMode(paramInt1);
    }
    paramInt1 = paramContext.getDimensionPixelOffset(4, -1);
    if (paramInt1 > 0) {
      setColumnWidth(paramInt1);
    }
    setNumColumns(paramContext.getInt(5, 1));
    paramInt1 = paramContext.getInt(0, -1);
    if (paramInt1 >= 0) {
      setGravity(paramInt1);
    }
    paramContext.recycle();
  }
  
  private void adjustForBottomFadingEdge(View paramView, int paramInt1, int paramInt2)
  {
    if (paramView.getBottom() > paramInt2) {
      offsetChildrenTopAndBottom(-Math.min(paramView.getTop() - paramInt1, paramView.getBottom() - paramInt2));
    }
  }
  
  private void adjustForTopFadingEdge(View paramView, int paramInt1, int paramInt2)
  {
    if (paramView.getTop() < paramInt1) {
      offsetChildrenTopAndBottom(Math.min(paramInt1 - paramView.getTop(), paramInt2 - paramView.getBottom()));
    }
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
          k = j - mVerticalSpacing;
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
          k = j + mVerticalSpacing;
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
  
  private boolean commonKey(int paramInt1, int paramInt2, KeyEvent paramKeyEvent)
  {
    if (mAdapter == null) {
      return false;
    }
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
    if (!bool2)
    {
      bool1 = bool2;
      if (i != 1) {
        switch (paramInt1)
        {
        default: 
          bool1 = bool2;
          break;
        case 123: 
          bool1 = bool2;
          if (paramKeyEvent.hasNoModifiers()) {
            if ((!resurrectSelectionIfNeeded()) && (!fullScroll(130))) {
              bool1 = false;
            } else {
              bool1 = true;
            }
          }
          break;
        case 122: 
          bool1 = bool2;
          if (paramKeyEvent.hasNoModifiers()) {
            if ((!resurrectSelectionIfNeeded()) && (!fullScroll(33))) {
              bool1 = false;
            } else {
              bool1 = true;
            }
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
          }
          else
          {
            bool1 = bool2;
            if (paramKeyEvent.hasModifiers(2)) {
              if ((!resurrectSelectionIfNeeded()) && (!fullScroll(130))) {
                bool1 = false;
              } else {
                bool1 = true;
              }
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
          }
          else
          {
            bool1 = bool2;
            if (paramKeyEvent.hasModifiers(2)) {
              if ((!resurrectSelectionIfNeeded()) && (!fullScroll(33))) {
                bool1 = false;
              } else {
                bool1 = true;
              }
            }
          }
          break;
        case 61: 
          if (paramKeyEvent.hasNoModifiers())
          {
            if ((!resurrectSelectionIfNeeded()) && (!sequenceScroll(2))) {
              bool1 = false;
            } else {
              bool1 = true;
            }
          }
          else
          {
            bool1 = bool2;
            if (paramKeyEvent.hasModifiers(1)) {
              if ((!resurrectSelectionIfNeeded()) && (!sequenceScroll(1))) {
                bool1 = false;
              } else {
                bool1 = true;
              }
            }
          }
          break;
        case 22: 
          bool1 = bool2;
          if (paramKeyEvent.hasNoModifiers()) {
            if ((!resurrectSelectionIfNeeded()) && (!arrowScroll(66))) {
              bool1 = false;
            } else {
              bool1 = true;
            }
          }
          break;
        case 21: 
          bool1 = bool2;
          if (paramKeyEvent.hasNoModifiers()) {
            if ((!resurrectSelectionIfNeeded()) && (!arrowScroll(17))) {
              bool1 = false;
            } else {
              bool1 = true;
            }
          }
          break;
        case 20: 
          if (paramKeyEvent.hasNoModifiers())
          {
            if ((!resurrectSelectionIfNeeded()) && (!arrowScroll(130))) {
              bool1 = false;
            } else {
              bool1 = true;
            }
          }
          else
          {
            bool1 = bool2;
            if (paramKeyEvent.hasModifiers(2)) {
              if ((!resurrectSelectionIfNeeded()) && (!fullScroll(130))) {
                bool1 = false;
              } else {
                bool1 = true;
              }
            }
          }
          break;
        case 19: 
          if (paramKeyEvent.hasNoModifiers())
          {
            if ((!resurrectSelectionIfNeeded()) && (!arrowScroll(33))) {
              bool1 = false;
            } else {
              bool1 = true;
            }
          }
          else
          {
            bool1 = bool2;
            if (paramKeyEvent.hasModifiers(2)) {
              if ((!resurrectSelectionIfNeeded()) && (!fullScroll(33))) {
                bool1 = false;
              } else {
                bool1 = true;
              }
            }
          }
          break;
        }
      }
    }
    if (bool1) {
      return true;
    }
    if (sendToTextFilter(paramInt1, paramInt2, paramKeyEvent)) {
      return true;
    }
    switch (i)
    {
    default: 
      return false;
    case 2: 
      return super.onKeyMultiple(paramInt1, paramInt2, paramKeyEvent);
    case 1: 
      return super.onKeyUp(paramInt1, paramKeyEvent);
    }
    return super.onKeyDown(paramInt1, paramKeyEvent);
  }
  
  private void correctTooHigh(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = mFirstPosition;
    int j = 1;
    if ((i + paramInt3 - 1 == mItemCount - 1) && (paramInt3 > 0))
    {
      paramInt3 = getChildAt(paramInt3 - 1).getBottom();
      i = mBottom - mTop - mListPadding.bottom - paramInt3;
      View localView = getChildAt(0);
      int k = localView.getTop();
      if ((i > 0) && ((mFirstPosition > 0) || (k < mListPadding.top)))
      {
        paramInt3 = i;
        if (mFirstPosition == 0) {
          paramInt3 = Math.min(i, mListPadding.top - k);
        }
        offsetChildrenTopAndBottom(paramInt3);
        if (mFirstPosition > 0)
        {
          paramInt3 = mFirstPosition;
          if (mStackFromBottom) {
            paramInt1 = j;
          }
          fillUp(paramInt3 - paramInt1, localView.getTop() - paramInt2);
          adjustViewsUpOrDown();
        }
      }
    }
  }
  
  private void correctTooLow(int paramInt1, int paramInt2, int paramInt3)
  {
    if ((mFirstPosition == 0) && (paramInt3 > 0))
    {
      int i = getChildAt(0).getTop();
      int j = mListPadding.top;
      int k = mBottom - mTop - mListPadding.bottom;
      j = i - j;
      View localView = getChildAt(paramInt3 - 1);
      int m = localView.getBottom();
      int n = mFirstPosition;
      i = 1;
      n = n + paramInt3 - 1;
      if ((j > 0) && ((n < mItemCount - 1) || (m > k)))
      {
        paramInt3 = j;
        if (n == mItemCount - 1) {
          paramInt3 = Math.min(j, m - k);
        }
        offsetChildrenTopAndBottom(-paramInt3);
        if (n < mItemCount - 1)
        {
          if (!mStackFromBottom) {
            paramInt1 = i;
          }
          fillDown(paramInt1 + n, localView.getBottom() + paramInt2);
          adjustViewsUpOrDown();
        }
      }
    }
  }
  
  private boolean determineColumns(int paramInt)
  {
    int i = mRequestedHorizontalSpacing;
    int j = mStretchMode;
    int k = mRequestedColumnWidth;
    boolean bool1 = false;
    boolean bool2 = false;
    if (mRequestedNumColumns == -1)
    {
      if (k > 0) {
        mNumColumns = ((paramInt + i) / (k + i));
      } else {
        mNumColumns = 2;
      }
    }
    else {
      mNumColumns = mRequestedNumColumns;
    }
    if (mNumColumns <= 0) {
      mNumColumns = 1;
    }
    if (j != 0)
    {
      paramInt = paramInt - mNumColumns * k - (mNumColumns - 1) * i;
      if (paramInt < 0) {
        bool2 = true;
      }
      switch (j)
      {
      default: 
        break;
      case 3: 
        mColumnWidth = k;
        if (mNumColumns > 1) {
          mHorizontalSpacing = (paramInt / (mNumColumns + 1) + i);
        } else {
          mHorizontalSpacing = (i + paramInt);
        }
        break;
      case 2: 
        mColumnWidth = (paramInt / mNumColumns + k);
        mHorizontalSpacing = i;
        break;
      case 1: 
        mColumnWidth = k;
        if (mNumColumns > 1) {
          mHorizontalSpacing = (paramInt / (mNumColumns - 1) + i);
        } else {
          mHorizontalSpacing = (i + paramInt);
        }
        break;
      }
    }
    else
    {
      mColumnWidth = k;
      mHorizontalSpacing = i;
      bool2 = bool1;
    }
    return bool2;
  }
  
  private View fillDown(int paramInt1, int paramInt2)
  {
    View localView1 = null;
    int i = mBottom - mTop;
    View localView2 = localView1;
    int j = i;
    int k = paramInt1;
    int m = paramInt2;
    if ((mGroupFlags & 0x22) == 34)
    {
      j = i - mListPadding.bottom;
      m = paramInt2;
      k = paramInt1;
      localView2 = localView1;
    }
    while ((m < j) && (k < mItemCount))
    {
      localView1 = makeRow(k, m, true);
      if (localView1 != null) {
        localView2 = localView1;
      }
      m = mReferenceView.getBottom() + mVerticalSpacing;
      k += mNumColumns;
    }
    setVisibleRangeHint(mFirstPosition, mFirstPosition + getChildCount() - 1);
    return localView2;
  }
  
  private View fillFromBottom(int paramInt1, int paramInt2)
  {
    paramInt1 = Math.min(Math.max(paramInt1, mSelectedPosition), mItemCount - 1);
    paramInt1 = mItemCount - 1 - paramInt1;
    return fillUp(mItemCount - 1 - (paramInt1 - paramInt1 % mNumColumns), paramInt2);
  }
  
  private View fillFromSelection(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = getVerticalFadingEdgeLength();
    int j = mSelectedPosition;
    int k = mNumColumns;
    int m = mVerticalSpacing;
    int n = -1;
    if (!mStackFromBottom)
    {
      j -= j % k;
    }
    else
    {
      n = mItemCount - 1 - j;
      n = mItemCount - 1 - (n - n % k);
      j = Math.max(0, n - k + 1);
    }
    int i1 = getTopSelectionPixel(paramInt2, i, j);
    paramInt3 = getBottomSelectionPixel(paramInt3, i, k, j);
    if (mStackFromBottom) {
      paramInt2 = n;
    } else {
      paramInt2 = j;
    }
    View localView1 = makeRow(paramInt2, paramInt1, true);
    mFirstPosition = j;
    View localView2 = mReferenceView;
    adjustForTopFadingEdge(localView2, i1, paramInt3);
    adjustForBottomFadingEdge(localView2, i1, paramInt3);
    if (!mStackFromBottom)
    {
      fillUp(j - k, localView2.getTop() - m);
      adjustViewsUpOrDown();
      fillDown(j + k, localView2.getBottom() + m);
    }
    else
    {
      fillDown(n + k, localView2.getBottom() + m);
      adjustViewsUpOrDown();
      fillUp(j - 1, localView2.getTop() - m);
    }
    return localView1;
  }
  
  private View fillFromTop(int paramInt)
  {
    mFirstPosition = Math.min(mFirstPosition, mSelectedPosition);
    mFirstPosition = Math.min(mFirstPosition, mItemCount - 1);
    if (mFirstPosition < 0) {
      mFirstPosition = 0;
    }
    mFirstPosition -= mFirstPosition % mNumColumns;
    return fillDown(mFirstPosition, paramInt);
  }
  
  private View fillSelection(int paramInt1, int paramInt2)
  {
    int i = reconcileSelectedPosition();
    int j = mNumColumns;
    int k = mVerticalSpacing;
    int m = -1;
    if (!mStackFromBottom)
    {
      i -= i % j;
    }
    else
    {
      m = mItemCount - 1 - i;
      m = mItemCount - 1 - (m - m % j);
      i = Math.max(0, m - j + 1);
    }
    int n = getVerticalFadingEdgeLength();
    int i1 = getTopSelectionPixel(paramInt1, n, i);
    int i2;
    if (mStackFromBottom) {
      i2 = m;
    } else {
      i2 = i;
    }
    View localView1 = makeRow(i2, i1, true);
    mFirstPosition = i;
    View localView2 = mReferenceView;
    if (!mStackFromBottom)
    {
      fillDown(i + j, localView2.getBottom() + k);
      pinToBottom(paramInt2);
      fillUp(i - j, localView2.getTop() - k);
      adjustViewsUpOrDown();
    }
    else
    {
      offsetChildrenTopAndBottom(getBottomSelectionPixel(paramInt2, n, j, i) - localView2.getBottom());
      fillUp(i - 1, localView2.getTop() - k);
      pinToTop(paramInt1);
      fillDown(m + j, localView2.getBottom() + k);
      adjustViewsUpOrDown();
    }
    return localView1;
  }
  
  private View fillSpecific(int paramInt1, int paramInt2)
  {
    int i = mNumColumns;
    int j = -1;
    if (!mStackFromBottom)
    {
      paramInt1 -= paramInt1 % i;
    }
    else
    {
      paramInt1 = mItemCount - 1 - paramInt1;
      j = mItemCount - 1 - (paramInt1 - paramInt1 % i);
      paramInt1 = Math.max(0, j - i + 1);
    }
    int k;
    if (mStackFromBottom) {
      k = j;
    } else {
      k = paramInt1;
    }
    View localView1 = makeRow(k, paramInt2, true);
    mFirstPosition = paramInt1;
    Object localObject1 = mReferenceView;
    if (localObject1 == null) {
      return null;
    }
    paramInt2 = mVerticalSpacing;
    Object localObject2;
    if (!mStackFromBottom)
    {
      localObject2 = fillUp(paramInt1 - i, ((View)localObject1).getTop() - paramInt2);
      adjustViewsUpOrDown();
      localObject1 = fillDown(paramInt1 + i, ((View)localObject1).getBottom() + paramInt2);
      paramInt1 = getChildCount();
      if (paramInt1 > 0) {
        correctTooHigh(i, paramInt2, paramInt1);
      }
    }
    else
    {
      View localView2 = fillDown(j + i, ((View)localObject1).getBottom() + paramInt2);
      adjustViewsUpOrDown();
      View localView3 = fillUp(paramInt1 - 1, ((View)localObject1).getTop() - paramInt2);
      paramInt1 = getChildCount();
      localObject2 = localView3;
      localObject1 = localView2;
      if (paramInt1 > 0)
      {
        correctTooLow(i, paramInt2, paramInt1);
        localObject1 = localView2;
        localObject2 = localView3;
      }
    }
    if (localView1 != null) {
      return localView1;
    }
    if (localObject2 != null) {
      return localObject2;
    }
    return localObject1;
  }
  
  private View fillUp(int paramInt1, int paramInt2)
  {
    View localView1 = null;
    int i = 0;
    View localView2 = localView1;
    int j = paramInt1;
    int k = paramInt2;
    if ((mGroupFlags & 0x22) == 34)
    {
      i = mListPadding.top;
      k = paramInt2;
      j = paramInt1;
      localView2 = localView1;
    }
    while ((k > i) && (j >= 0))
    {
      localView1 = makeRow(j, k, false);
      if (localView1 != null) {
        localView2 = localView1;
      }
      k = mReferenceView.getTop() - mVerticalSpacing;
      mFirstPosition = j;
      j -= mNumColumns;
    }
    if (mStackFromBottom) {
      mFirstPosition = Math.max(0, j + 1);
    }
    setVisibleRangeHint(mFirstPosition, mFirstPosition + getChildCount() - 1);
    return localView2;
  }
  
  private int getBottomSelectionPixel(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramInt1;
    if (paramInt4 + paramInt3 - 1 < mItemCount - 1) {
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
  
  private boolean isCandidateSelection(int paramInt1, int paramInt2)
  {
    int i = getChildCount();
    int j = i - 1 - paramInt1;
    boolean bool1 = mStackFromBottom;
    boolean bool2 = false;
    boolean bool3 = false;
    boolean bool4 = false;
    boolean bool5 = false;
    boolean bool6 = false;
    boolean bool7 = false;
    int k;
    if (!bool1)
    {
      j = paramInt1 - paramInt1 % mNumColumns;
      k = Math.min(mNumColumns + j - 1, i);
    }
    else
    {
      k = i - 1 - (j - j % mNumColumns);
      j = Math.max(0, k - mNumColumns + 1);
    }
    if (paramInt2 != 17)
    {
      if (paramInt2 != 33)
      {
        if (paramInt2 != 66)
        {
          if (paramInt2 != 130)
          {
            switch (paramInt2)
            {
            default: 
              throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT, FOCUS_FORWARD, FOCUS_BACKWARD}.");
            case 2: 
              bool3 = bool7;
              if (paramInt1 == j)
              {
                bool3 = bool7;
                if (j == 0) {
                  bool3 = true;
                }
              }
              return bool3;
            }
            bool3 = bool2;
            if (paramInt1 == k)
            {
              bool3 = bool2;
              if (k == i - 1) {
                bool3 = true;
              }
            }
            return bool3;
          }
          if (j == 0) {
            bool3 = true;
          }
          return bool3;
        }
        bool3 = bool4;
        if (paramInt1 == j) {
          bool3 = true;
        }
        return bool3;
      }
      bool3 = bool5;
      if (k == i - 1) {
        bool3 = true;
      }
      return bool3;
    }
    bool3 = bool6;
    if (paramInt1 == k) {
      bool3 = true;
    }
    return bool3;
  }
  
  private View makeAndAddView(int paramInt1, int paramInt2, boolean paramBoolean1, int paramInt3, boolean paramBoolean2, int paramInt4)
  {
    if (!mDataChanged)
    {
      localView = mRecycler.getActiveView(paramInt1);
      if (localView != null)
      {
        setupChild(localView, paramInt1, paramInt2, paramBoolean1, paramInt3, paramBoolean2, true, paramInt4);
        return localView;
      }
    }
    View localView = obtainView(paramInt1, mIsScrap);
    setupChild(localView, paramInt1, paramInt2, paramBoolean1, paramInt3, paramBoolean2, mIsScrap[0], paramInt4);
    return localView;
  }
  
  private View makeRow(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    int i = mColumnWidth;
    int j = mHorizontalSpacing;
    boolean bool1 = isLayoutRtl();
    int k;
    int m;
    int n;
    if (bool1)
    {
      k = getWidth();
      m = mListPadding.right;
      if (mStretchMode == 3) {
        n = j;
      } else {
        n = 0;
      }
      n = k - m - i - n;
    }
    else
    {
      m = mListPadding.left;
      if (mStretchMode == 3) {
        n = j;
      } else {
        n = 0;
      }
      n = m + n;
    }
    if (!mStackFromBottom) {
      m = Math.min(paramInt1 + mNumColumns, mItemCount);
    }
    for (;;)
    {
      break;
      m = paramInt1 + 1;
      k = Math.max(0, paramInt1 - mNumColumns + 1);
      paramInt1 = n;
      if (m - k < mNumColumns)
      {
        i1 = mNumColumns;
        if (bool1) {
          paramInt1 = -1;
        } else {
          paramInt1 = 1;
        }
        paramInt1 = n + paramInt1 * ((i1 - (m - k)) * (i + j));
      }
      n = paramInt1;
      paramInt1 = k;
    }
    boolean bool2 = shouldShowSelector();
    boolean bool3 = touchModeDrawsInPressedState();
    int i1 = mSelectedPosition;
    if (bool1) {
      k = -1;
    } else {
      k = 1;
    }
    Object localObject1 = null;
    View localView = null;
    int i2 = paramInt1;
    while (i2 < m)
    {
      if (i2 == i1) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      if (paramBoolean) {
        i3 = -1;
      } else {
        i3 = i2 - paramInt1;
      }
      localView = makeAndAddView(i2, paramInt2, paramBoolean, n, bool1, i3);
      int i3 = n + k * i;
      n = i3;
      if (i2 < m - 1) {
        n = i3 + k * j;
      }
      Object localObject2 = localObject1;
      if (bool1) {
        if (!bool2)
        {
          localObject2 = localObject1;
          if (!bool3) {}
        }
        else
        {
          localObject2 = localView;
        }
      }
      i2++;
      localObject1 = localObject2;
    }
    mReferenceView = localView;
    if (localObject1 != null) {
      mReferenceViewInSelectedRow = mReferenceView;
    }
    return localObject1;
  }
  
  private View moveSelection(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = getVerticalFadingEdgeLength();
    int j = mSelectedPosition;
    int k = mNumColumns;
    int m = mVerticalSpacing;
    int n = -1;
    int i2;
    if (!mStackFromBottom)
    {
      i1 = j - paramInt1 - (j - paramInt1) % k;
      i2 = j - j % k;
      paramInt1 = n;
    }
    else
    {
      i2 = mItemCount - 1 - j;
      n = mItemCount - 1 - (i2 - i2 % k);
      i2 = Math.max(0, n - k + 1);
      paramInt1 = mItemCount - 1 - (j - paramInt1);
      i1 = Math.max(0, mItemCount - 1 - (paramInt1 - paramInt1 % k) - k + 1);
      paramInt1 = n;
    }
    j = i2 - i1;
    int i1 = getTopSelectionPixel(paramInt2, i, i2);
    n = getBottomSelectionPixel(paramInt3, i, k, i2);
    mFirstPosition = i2;
    View localView1;
    View localView2;
    if (j > 0)
    {
      if (mReferenceViewInSelectedRow == null) {
        paramInt2 = 0;
      } else {
        paramInt2 = mReferenceViewInSelectedRow.getBottom();
      }
      if (mStackFromBottom) {
        paramInt3 = paramInt1;
      } else {
        paramInt3 = i2;
      }
      localView1 = makeRow(paramInt3, paramInt2 + m, true);
      localView2 = mReferenceView;
      adjustForBottomFadingEdge(localView2, i1, n);
    }
    else if (j < 0)
    {
      if (mReferenceViewInSelectedRow == null) {
        paramInt2 = 0;
      } else {
        paramInt2 = mReferenceViewInSelectedRow.getTop();
      }
      if (mStackFromBottom) {
        paramInt3 = paramInt1;
      } else {
        paramInt3 = i2;
      }
      localView1 = makeRow(paramInt3, paramInt2 - m, false);
      localView2 = mReferenceView;
      adjustForTopFadingEdge(localView2, i1, n);
    }
    else
    {
      if (mReferenceViewInSelectedRow == null) {
        paramInt2 = 0;
      } else {
        paramInt2 = mReferenceViewInSelectedRow.getTop();
      }
      if (mStackFromBottom) {
        paramInt3 = paramInt1;
      } else {
        paramInt3 = i2;
      }
      localView1 = makeRow(paramInt3, paramInt2, true);
      localView2 = mReferenceView;
    }
    if (!mStackFromBottom)
    {
      fillUp(i2 - k, localView2.getTop() - m);
      adjustViewsUpOrDown();
      fillDown(i2 + k, localView2.getBottom() + m);
    }
    else
    {
      fillDown(paramInt1 + k, localView2.getBottom() + m);
      adjustViewsUpOrDown();
      fillUp(i2 - 1, localView2.getTop() - m);
    }
    return localView1;
  }
  
  private void pinToBottom(int paramInt)
  {
    int i = getChildCount();
    if (mFirstPosition + i == mItemCount)
    {
      paramInt -= getChildAt(i - 1).getBottom();
      if (paramInt > 0) {
        offsetChildrenTopAndBottom(paramInt);
      }
    }
  }
  
  private void pinToTop(int paramInt)
  {
    if (mFirstPosition == 0)
    {
      paramInt -= getChildAt(0).getTop();
      if (paramInt < 0) {
        offsetChildrenTopAndBottom(paramInt);
      }
    }
  }
  
  private void setupChild(View paramView, int paramInt1, int paramInt2, boolean paramBoolean1, int paramInt3, boolean paramBoolean2, boolean paramBoolean3, int paramInt4)
  {
    Trace.traceBegin(8L, "setupGridItem");
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
    int k;
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
    if (i != 0)
    {
      paramView.setSelected(paramBoolean2);
      if (paramBoolean2) {
        requestFocus();
      }
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
    if ((paramBoolean3) && (!forceAdd))
    {
      attachViewToParent(paramView, paramInt4, localLayoutParams2);
      if ((!paramBoolean3) || (getLayoutParamsscrappedFromPosition != paramInt1)) {}
    }
    for (;;)
    {
      break;
      paramView.jumpDrawablesToCurrentState();
      continue;
      forceAdd = false;
      addViewInLayout(paramView, paramInt4, localLayoutParams2, true);
    }
    if (j != 0)
    {
      paramInt1 = ViewGroup.getChildMeasureSpec(View.MeasureSpec.makeMeasureSpec(0, 0), 0, height);
      paramView.measure(ViewGroup.getChildMeasureSpec(View.MeasureSpec.makeMeasureSpec(mColumnWidth, 1073741824), 0, width), paramInt1);
    }
    else
    {
      cleanupLayoutState(paramView);
    }
    int i = paramView.getMeasuredWidth();
    paramInt4 = paramView.getMeasuredHeight();
    if (!paramBoolean1) {
      paramInt2 -= paramInt4;
    }
    paramInt1 = getLayoutDirection();
    paramInt1 = Gravity.getAbsoluteGravity(mGravity, paramInt1) & 0x7;
    if (paramInt1 != 1)
    {
      if (paramInt1 != 3)
      {
        if (paramInt1 != 5) {
          paramInt1 = paramInt3;
        } else {
          paramInt1 = paramInt3 + mColumnWidth - i;
        }
      }
      else {
        paramInt1 = paramInt3;
      }
    }
    else {
      paramInt1 = paramInt3 + (mColumnWidth - i) / 2;
    }
    if (j != 0)
    {
      paramView.layout(paramInt1, paramInt2, paramInt1 + i, paramInt2 + paramInt4);
    }
    else
    {
      paramView.offsetLeftAndRight(paramInt1 - paramView.getLeft());
      paramView.offsetTopAndBottom(paramInt2 - paramView.getTop());
    }
    if ((mCachingStarted) && (!paramView.isDrawingCacheEnabled())) {
      paramView.setDrawingCacheEnabled(true);
    }
    Trace.traceEnd(8L);
  }
  
  boolean arrowScroll(int paramInt)
  {
    int i = mSelectedPosition;
    int j = mNumColumns;
    boolean bool1 = false;
    int k;
    int m;
    if (!mStackFromBottom)
    {
      k = i / j * j;
      m = Math.min(k + j - 1, mItemCount - 1);
    }
    else
    {
      k = mItemCount;
      m = mItemCount - 1 - (k - 1 - i) / j * j;
      k = Math.max(0, m - j + 1);
    }
    if (paramInt != 33)
    {
      if ((paramInt == 130) && (m < mItemCount - 1))
      {
        mLayoutMode = 6;
        setSelectionInt(Math.min(i + j, mItemCount - 1));
        bool1 = true;
      }
    }
    else if (k > 0)
    {
      mLayoutMode = 6;
      setSelectionInt(Math.max(0, i - j));
      bool1 = true;
    }
    boolean bool2 = isLayoutRtl();
    boolean bool3;
    if ((i > k) && (((paramInt == 17) && (!bool2)) || ((paramInt == 66) && (bool2))))
    {
      mLayoutMode = 6;
      setSelectionInt(Math.max(0, i - 1));
      bool3 = true;
    }
    else
    {
      bool3 = bool1;
      if (i < m) {
        if ((paramInt != 17) || (!bool2))
        {
          bool3 = bool1;
          if (paramInt == 66)
          {
            bool3 = bool1;
            if (bool2) {}
          }
        }
        else
        {
          mLayoutMode = 6;
          setSelectionInt(Math.min(i + 1, mItemCount - 1));
          bool3 = true;
        }
      }
    }
    if (bool3)
    {
      playSoundEffect(SoundEffectConstants.getContantForFocusDirection(paramInt));
      invokeOnItemScrollListener();
    }
    if (bool3) {
      awakenScrollBars();
    }
    return bool3;
  }
  
  protected void attachLayoutAnimationParameters(View paramView, ViewGroup.LayoutParams paramLayoutParams, int paramInt1, int paramInt2)
  {
    GridLayoutAnimationController.AnimationParameters localAnimationParameters = (GridLayoutAnimationController.AnimationParameters)layoutAnimationParameters;
    paramView = localAnimationParameters;
    if (localAnimationParameters == null)
    {
      paramView = new GridLayoutAnimationController.AnimationParameters();
      layoutAnimationParameters = paramView;
    }
    count = paramInt2;
    index = paramInt1;
    columnsCount = mNumColumns;
    rowsCount = (paramInt2 / mNumColumns);
    if (!mStackFromBottom)
    {
      column = (paramInt1 % mNumColumns);
      row = (paramInt1 / mNumColumns);
    }
    else
    {
      paramInt1 = paramInt2 - 1 - paramInt1;
      column = (mNumColumns - 1 - paramInt1 % mNumColumns);
      row = (rowsCount - 1 - paramInt1 / mNumColumns);
    }
  }
  
  protected int computeVerticalScrollExtent()
  {
    int i = getChildCount();
    if (i > 0)
    {
      int j = mNumColumns;
      int k = (i + j - 1) / j * 100;
      View localView = getChildAt(0);
      int m = localView.getTop();
      int n = localView.getHeight();
      j = k;
      if (n > 0) {
        j = k + m * 100 / n;
      }
      localView = getChildAt(i - 1);
      m = localView.getBottom();
      i = localView.getHeight();
      k = j;
      if (i > 0) {
        k = j - (m - getHeight()) * 100 / i;
      }
      return k;
    }
    return 0;
  }
  
  protected int computeVerticalScrollOffset()
  {
    if ((mFirstPosition >= 0) && (getChildCount() > 0))
    {
      View localView = getChildAt(0);
      int i = localView.getTop();
      int j = localView.getHeight();
      if (j > 0)
      {
        int k = mNumColumns;
        int m = (mItemCount + k - 1) / k;
        int n;
        if (isStackFromBottom()) {
          n = m * k - mItemCount;
        } else {
          n = 0;
        }
        return Math.max((mFirstPosition + n) / k * 100 - i * 100 / j + (int)(mScrollY / getHeight() * m * 100.0F), 0);
      }
    }
    return 0;
  }
  
  protected int computeVerticalScrollRange()
  {
    int i = mNumColumns;
    int j = (mItemCount + i - 1) / i;
    int k = Math.max(j * 100, 0);
    i = k;
    if (mScrollY != 0) {
      i = k + Math.abs((int)(mScrollY / getHeight() * j * 100.0F));
    }
    return i;
  }
  
  protected void encodeProperties(ViewHierarchyEncoder paramViewHierarchyEncoder)
  {
    super.encodeProperties(paramViewHierarchyEncoder);
    paramViewHierarchyEncoder.addProperty("numColumns", getNumColumns());
  }
  
  void fillGap(boolean paramBoolean)
  {
    int i = mNumColumns;
    int j = mVerticalSpacing;
    int k = getChildCount();
    int m;
    if (paramBoolean)
    {
      m = 0;
      if ((mGroupFlags & 0x22) == 34) {
        m = getListPaddingTop();
      }
      if (k > 0) {
        m = getChildAt(k - 1).getBottom() + j;
      }
      int n = mFirstPosition + k;
      k = n;
      if (mStackFromBottom) {
        k = n + (i - 1);
      }
      fillDown(k, m);
      correctTooHigh(i, j, getChildCount());
    }
    else
    {
      m = 0;
      if ((mGroupFlags & 0x22) == 34) {
        m = getListPaddingBottom();
      }
      if (k > 0) {
        m = getChildAt(0).getTop() - j;
      } else {
        m = getHeight() - m;
      }
      k = mFirstPosition;
      if (!mStackFromBottom) {
        k -= i;
      } else {
        k--;
      }
      fillUp(k, m);
      correctTooLow(i, j, getChildCount());
    }
  }
  
  int findMotionRow(int paramInt)
  {
    int i = getChildCount();
    if (i > 0)
    {
      int j = mNumColumns;
      if (!mStackFromBottom)
      {
        k = 0;
        while (k < i)
        {
          if (paramInt <= getChildAt(k).getBottom()) {
            return mFirstPosition + k;
          }
          k += j;
        }
      }
      int k = i - 1;
      while (k >= 0)
      {
        if (paramInt >= getChildAt(k).getTop()) {
          return mFirstPosition + k;
        }
        k -= j;
      }
    }
    return -1;
  }
  
  boolean fullScroll(int paramInt)
  {
    boolean bool = false;
    if (paramInt == 33)
    {
      mLayoutMode = 2;
      setSelectionInt(0);
      invokeOnItemScrollListener();
      bool = true;
    }
    else if (paramInt == 130)
    {
      mLayoutMode = 2;
      setSelectionInt(mItemCount - 1);
      invokeOnItemScrollListener();
      bool = true;
    }
    if (bool) {
      awakenScrollBars();
    }
    return bool;
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return GridView.class.getName();
  }
  
  public ListAdapter getAdapter()
  {
    return mAdapter;
  }
  
  public int getColumnWidth()
  {
    return mColumnWidth;
  }
  
  public int getGravity()
  {
    return mGravity;
  }
  
  public int getHorizontalSpacing()
  {
    return mHorizontalSpacing;
  }
  
  @ViewDebug.ExportedProperty
  public int getNumColumns()
  {
    return mNumColumns;
  }
  
  public int getRequestedColumnWidth()
  {
    return mRequestedColumnWidth;
  }
  
  public int getRequestedHorizontalSpacing()
  {
    return mRequestedHorizontalSpacing;
  }
  
  public int getStretchMode()
  {
    return mStretchMode;
  }
  
  public int getVerticalSpacing()
  {
    return mVerticalSpacing;
  }
  
  protected void layoutChildren()
  {
    GridView localGridView = this;
    boolean bool1 = mBlockLayoutRequests;
    if (!bool1) {
      mBlockLayoutRequests = true;
    }
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
          if (!bool1) {
            mBlockLayoutRequests = false;
          }
          return;
        }
        finally
        {
          break label1503;
        }
      }
      int i = mListPadding.top;
      int j = mBottom - mTop - mListPadding.bottom;
      int k = getChildCount();
      int m = 0;
      Object localObject7 = null;
      Object localObject8 = null;
      Object localObject2 = null;
      Object localObject9 = null;
      switch (mLayoutMode)
      {
      default: 
        n = mSelectedPosition;
        break;
      case 6: 
        n = m;
        localObject10 = localObject7;
        localObject11 = localObject2;
        localObject8 = localObject9;
        if (mNextSelectedPosition < 0) {
          break label349;
        }
        n = mNextSelectedPosition - mSelectedPosition;
        localObject10 = localObject7;
        localObject11 = localObject2;
        localObject8 = localObject9;
        break;
      case 2: 
        i1 = mNextSelectedPosition - mFirstPosition;
        n = m;
        localObject10 = localObject7;
        localObject11 = localObject2;
        localObject8 = localObject9;
        if (i1 < 0) {
          break label349;
        }
        n = m;
        localObject10 = localObject7;
        localObject11 = localObject2;
        localObject8 = localObject9;
        if (i1 >= k) {
          break label349;
        }
        localObject8 = localGridView.getChildAt(i1);
        n = m;
        localObject10 = localObject7;
        localObject11 = localObject2;
        break;
      case 1: 
      case 3: 
      case 4: 
      case 5: 
        n = m;
        localObject10 = localObject7;
        localObject11 = localObject2;
        localObject8 = localObject9;
        break;
      }
      int i1 = mFirstPosition;
      n -= i1;
      localObject2 = localObject8;
      if (n >= 0)
      {
        localObject2 = localObject8;
        if (n < k) {
          localObject2 = localGridView.getChildAt(n);
        }
      }
      Object localObject11 = localGridView.getChildAt(0);
      localObject8 = localObject9;
      Object localObject10 = localObject2;
      int n = m;
      label349:
      boolean bool2 = mDataChanged;
      if (bool2) {
        handleDataChanged();
      }
      m = mItemCount;
      if (m == 0)
      {
        resetList();
        invokeOnItemScrollListener();
        if (!bool1) {
          mBlockLayoutRequests = false;
        }
        return;
      }
      localGridView.setSelectedPositionInt(mNextSelectedPosition);
      localObject7 = null;
      localObject2 = null;
      m = -1;
      ViewRootImpl localViewRootImpl = getViewRootImpl();
      if (localViewRootImpl != null)
      {
        localObject9 = localViewRootImpl.getAccessibilityFocusedHost();
        if (localObject9 != null)
        {
          localObject12 = localGridView.getAccessibilityFocusedChild((View)localObject9);
          if (localObject12 != null)
          {
            if ((bool2) && (!((View)localObject12).hasTransientState()) && (!mAdapterHasStableIds))
            {
              localObject9 = null;
            }
            else
            {
              localObject7 = localViewRootImpl.getAccessibilityFocusedVirtualView();
              localObject2 = localObject9;
              localObject9 = localObject7;
            }
            m = localGridView.getPositionForView((View)localObject12);
            localObject7 = localObject9;
            localObject9 = localObject2;
            break label515;
          }
        }
      }
      localObject2 = null;
      localObject9 = localObject7;
      localObject7 = localObject2;
      label515:
      int i2 = mFirstPosition;
      Object localObject12 = mRecycler;
      if (bool2) {
        i1 = 0;
      }
      for (;;)
      {
        boolean bool3;
        if (i1 < k)
        {
          bool3 = bool1;
          bool1 = bool3;
        }
        try
        {
          ((AbsListView.RecycleBin)localObject12).addScrapView(localGridView.getChildAt(i1), i2 + i1);
          i1++;
          bool1 = bool3;
        }
        finally
        {
          Object localObject4;
          break label1503;
        }
      }
      bool3 = bool1;
      break label594;
      bool3 = bool1;
      bool1 = bool3;
      ((AbsListView.RecycleBin)localObject12).fillActiveViews(k, i2);
      label594:
      bool1 = bool3;
      detachAllViewsFromParent();
      bool1 = bool3;
      ((AbsListView.RecycleBin)localObject12).removeSkippedScrap();
      bool1 = bool3;
      switch (mLayoutMode)
      {
      default: 
        if (k == 0)
        {
          bool1 = bool3;
          bool2 = mStackFromBottom;
        }
        break;
      case 6: 
        bool1 = bool3;
        localObject2 = localGridView.moveSelection(n, i, j);
        break;
      case 5: 
        bool1 = bool3;
        localObject2 = localGridView.fillSpecific(mSyncPosition, mSpecificTop);
        break;
      case 4: 
        bool1 = bool3;
        localObject2 = localGridView.fillSpecific(mSelectedPosition, mSpecificTop);
        break;
      case 3: 
        bool1 = bool3;
        localObject2 = localGridView.fillUp(mItemCount - 1, j);
        bool1 = bool3;
        adjustViewsUpOrDown();
        break;
      case 2: 
        if (localObject8 != null)
        {
          bool1 = bool3;
          localObject2 = localGridView.fillFromSelection(((View)localObject8).getTop(), i, j);
        }
        break;
      }
      for (;;)
      {
        break;
        bool1 = bool3;
        localObject2 = localGridView.fillSelection(i, j);
        break;
        bool1 = bool3;
        mFirstPosition = 0;
        bool1 = bool3;
        localObject2 = localGridView.fillFromTop(i);
        bool1 = bool3;
        adjustViewsUpOrDown();
        break;
        if (!bool2)
        {
          bool1 = bool3;
          if (mAdapter != null)
          {
            bool1 = bool3;
            if (!isInTouchMode())
            {
              n = 0;
              break label859;
            }
          }
          n = -1;
          label859:
          bool1 = bool3;
          localGridView.setSelectedPositionInt(n);
          bool1 = bool3;
          localObject2 = localGridView.fillFromTop(i);
        }
        else
        {
          bool1 = bool3;
          n = mItemCount - 1;
          bool1 = bool3;
          if (mAdapter != null)
          {
            bool1 = bool3;
            if (!isInTouchMode())
            {
              i = n;
              break label925;
            }
          }
          i = -1;
          label925:
          bool1 = bool3;
          localGridView.setSelectedPositionInt(i);
          bool1 = bool3;
          localObject2 = localGridView.fillFromBottom(n, j);
          break;
          bool1 = bool3;
          if (mSelectedPosition >= 0)
          {
            bool1 = bool3;
            if (mSelectedPosition < mItemCount)
            {
              bool1 = bool3;
              n = mSelectedPosition;
              if (localObject10 != null)
              {
                bool1 = bool3;
                i = localObject10.getTop();
              }
              bool1 = bool3;
              localObject2 = localGridView.fillSpecific(n, i);
              continue;
            }
          }
          bool1 = bool3;
          if (mFirstPosition < mItemCount)
          {
            bool1 = bool3;
            n = mFirstPosition;
            if (localObject11 != null)
            {
              bool1 = bool3;
              i = ((View)localObject11).getTop();
            }
            bool1 = bool3;
            localObject2 = localGridView.fillSpecific(n, i);
          }
          else
          {
            bool1 = bool3;
            localObject2 = localGridView.fillSpecific(0, i);
          }
        }
      }
      bool1 = bool3;
      ((AbsListView.RecycleBin)localObject12).scrapActiveViews();
      if (localObject2 != null)
      {
        bool1 = bool3;
        localGridView.positionSelector(-1, (View)localObject2);
        bool1 = bool3;
        mSelectedTop = ((View)localObject2).getTop();
      }
      else
      {
        bool1 = bool3;
        if (mTouchMode > 0)
        {
          bool1 = bool3;
          if (mTouchMode < 3)
          {
            i = 1;
            break label1147;
          }
        }
        i = 0;
        label1147:
        if (i != 0)
        {
          bool1 = bool3;
          localObject2 = localGridView.getChildAt(mMotionPosition - mFirstPosition);
          if (localObject2 != null)
          {
            bool1 = bool3;
            localGridView.positionSelector(mMotionPosition, (View)localObject2);
          }
        }
        else
        {
          bool1 = bool3;
          if (mSelectedPosition != -1)
          {
            bool1 = bool3;
            localObject2 = localGridView.getChildAt(mSelectorPosition - mFirstPosition);
            if (localObject2 != null)
            {
              bool1 = bool3;
              localGridView.positionSelector(mSelectorPosition, (View)localObject2);
            }
          }
          else
          {
            bool1 = bool3;
            mSelectedTop = 0;
            bool1 = bool3;
            mSelectorRect.setEmpty();
          }
        }
      }
      if (localViewRootImpl != null)
      {
        bool1 = bool3;
        if (localViewRootImpl.getAccessibilityFocusedHost() == null)
        {
          if (localObject9 != null)
          {
            bool1 = bool3;
            if (((View)localObject9).isAttachedToWindow())
            {
              bool1 = bool3;
              localObject2 = ((View)localObject9).getAccessibilityNodeProvider();
              if ((localObject7 != null) && (localObject2 != null)) {}
              try
              {
                ((AccessibilityNodeProvider)localObject2).performAction(AccessibilityNodeInfo.getVirtualDescendantId(((AccessibilityNodeInfo)localObject7).getSourceNodeId()), 64, null);
                break label1328;
                ((View)localObject9).requestAccessibilityFocus();
              }
              finally
              {
                label1328:
                bool1 = bool3;
                break label1503;
              }
            }
          }
          if (m != -1)
          {
            localObject4 = this;
            bool1 = bool3;
            localObject4 = ((GridView)localObject4).getChildAt(MathUtils.constrain(m - mFirstPosition, 0, getChildCount() - 1));
            if (localObject4 != null)
            {
              bool1 = bool3;
              ((View)localObject4).requestAccessibilityFocus();
            }
          }
          else {}
        }
      }
      localObject4 = this;
      bool1 = bool3;
      mLayoutMode = 0;
      bool1 = bool3;
      mDataChanged = false;
      bool1 = bool3;
      if (mPositionScrollAfterLayout != null)
      {
        bool1 = bool3;
        ((GridView)localObject4).post(mPositionScrollAfterLayout);
        bool1 = bool3;
        mPositionScrollAfterLayout = null;
      }
      bool1 = bool3;
      mNeedSync = false;
      bool1 = bool3;
      ((GridView)localObject4).setNextSelectedPositionInt(mSelectedPosition);
      bool1 = bool3;
      updateScrollIndicators();
      bool1 = bool3;
      if (mItemCount > 0)
      {
        bool1 = bool3;
        checkSelectionChanged();
      }
      bool1 = bool3;
      invokeOnItemScrollListener();
      if (!bool3) {
        mBlockLayoutRequests = false;
      }
      return;
    }
    finally {}
    label1503:
    if (!bool1) {
      mBlockLayoutRequests = false;
    }
    throw localObject6;
  }
  
  int lookForSelectablePosition(int paramInt, boolean paramBoolean)
  {
    if ((mAdapter != null) && (!isInTouchMode()))
    {
      if ((paramInt >= 0) && (paramInt < mItemCount)) {
        return paramInt;
      }
      return -1;
    }
    return -1;
  }
  
  protected void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect)
  {
    super.onFocusChanged(paramBoolean, paramInt, paramRect);
    int i = -1;
    int j = i;
    if (paramBoolean)
    {
      j = i;
      if (paramRect != null)
      {
        paramRect.offset(mScrollX, mScrollY);
        Rect localRect = mTempRect;
        int k = Integer.MAX_VALUE;
        int m = getChildCount();
        int n = 0;
        for (;;)
        {
          j = i;
          if (n >= m) {
            break;
          }
          if (!isCandidateSelection(n, paramInt))
          {
            j = k;
          }
          else
          {
            View localView = getChildAt(n);
            localView.getDrawingRect(localRect);
            offsetDescendantRectToMyCoords(localView, localRect);
            int i1 = getDistance(paramRect, localRect, paramInt);
            j = k;
            if (i1 < k)
            {
              j = i1;
              i = n;
            }
          }
          n++;
          k = j;
        }
      }
    }
    if (j >= 0) {
      setSelection(mFirstPosition + j);
    } else {
      requestLayout();
    }
  }
  
  public void onInitializeAccessibilityNodeInfoForItem(View paramView, int paramInt, AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfoForItem(paramView, paramInt, paramAccessibilityNodeInfo);
    int i = getCount();
    int j = getNumColumns();
    int k = i / j;
    if (!mStackFromBottom)
    {
      i = paramInt % j;
      k = paramInt / j;
    }
    else
    {
      i = i - 1 - paramInt;
      k = k - 1 - i / j;
      i = j - 1 - i % j;
    }
    paramView = (AbsListView.LayoutParams)paramView.getLayoutParams();
    boolean bool;
    if ((paramView != null) && (viewType == -2)) {
      bool = true;
    } else {
      bool = false;
    }
    paramAccessibilityNodeInfo.setCollectionItemInfo(AccessibilityNodeInfo.CollectionItemInfo.obtain(k, 1, i, 1, bool, isItemChecked(paramInt)));
  }
  
  public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfoInternal(paramAccessibilityNodeInfo);
    int i = getNumColumns();
    int j = getCount() / i;
    paramAccessibilityNodeInfo.setCollectionInfo(AccessibilityNodeInfo.CollectionInfo.obtain(j, i, false, getSelectionModeForAccessibility()));
    if ((i > 0) || (j > 0)) {
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
    if (i == 0)
    {
      if (mColumnWidth > 0) {}
      for (k = mColumnWidth + mListPadding.left + mListPadding.right;; k = mListPadding.left + mListPadding.right) {
        break;
      }
      k = getVerticalScrollbarWidth() + k;
    }
    boolean bool = determineColumns(k - mListPadding.left - mListPadding.right);
    int n = 0;
    if (mAdapter == null) {
      i1 = 0;
    } else {
      i1 = mAdapter.getCount();
    }
    mItemCount = i1;
    int i2 = mItemCount;
    if (i2 > 0)
    {
      View localView = obtainView(0, mIsScrap);
      AbsListView.LayoutParams localLayoutParams1 = (AbsListView.LayoutParams)localView.getLayoutParams();
      AbsListView.LayoutParams localLayoutParams2 = localLayoutParams1;
      if (localLayoutParams1 == null)
      {
        localLayoutParams2 = (AbsListView.LayoutParams)generateDefaultLayoutParams();
        localView.setLayoutParams(localLayoutParams2);
      }
      viewType = mAdapter.getItemViewType(0);
      isEnabled = mAdapter.isEnabled(0);
      forceAdd = true;
      paramInt2 = getChildMeasureSpec(View.MeasureSpec.makeSafeMeasureSpec(View.MeasureSpec.getSize(paramInt2), 0), 0, height);
      localView.measure(getChildMeasureSpec(View.MeasureSpec.makeMeasureSpec(mColumnWidth, 1073741824), 0, width), paramInt2);
      paramInt2 = localView.getMeasuredHeight();
      combineMeasuredStates(0, localView.getMeasuredState());
      n = paramInt2;
      if (mRecycler.shouldRecycleViewType(viewType))
      {
        mRecycler.addScrapView(localView, -1);
        n = paramInt2;
      }
    }
    paramInt2 = m;
    if (j == 0) {
      paramInt2 = mListPadding.top + mListPadding.bottom + n + getVerticalFadingEdgeLength() * 2;
    }
    int i1 = paramInt2;
    if (j == Integer.MIN_VALUE)
    {
      i1 = mListPadding.top + mListPadding.bottom;
      int i3 = mNumColumns;
      j = 0;
      for (;;)
      {
        m = i1;
        if (j >= i2) {
          break;
        }
        m = i1 + n;
        i1 = m;
        if (j + i3 < i2) {
          i1 = m + mVerticalSpacing;
        }
        if (i1 >= paramInt2)
        {
          m = paramInt2;
          break;
        }
        j += i3;
      }
      i1 = m;
    }
    paramInt2 = k;
    if (i == Integer.MIN_VALUE)
    {
      paramInt2 = k;
      if (mRequestedNumColumns != -1) {
        if (mRequestedNumColumns * mColumnWidth + (mRequestedNumColumns - 1) * mHorizontalSpacing + mListPadding.left + mListPadding.right <= k)
        {
          paramInt2 = k;
          if (!bool) {}
        }
        else
        {
          paramInt2 = k | 0x1000000;
        }
      }
    }
    setMeasuredDimension(paramInt2, i1);
    mWidthMeasureSpec = paramInt1;
  }
  
  boolean pageScroll(int paramInt)
  {
    int i = -1;
    if (paramInt == 33) {
      i = Math.max(0, mSelectedPosition - getChildCount());
    } else if (paramInt == 130) {
      i = Math.min(mItemCount - 1, mSelectedPosition + getChildCount());
    }
    if (i >= 0)
    {
      setSelectionInt(i);
      invokeOnItemScrollListener();
      awakenScrollBars();
      return true;
    }
    return false;
  }
  
  public boolean performAccessibilityActionInternal(int paramInt, Bundle paramBundle)
  {
    if (super.performAccessibilityActionInternal(paramInt, paramBundle)) {
      return true;
    }
    if (paramInt == 16908343)
    {
      int i = getNumColumns();
      paramInt = paramBundle.getInt("android.view.accessibility.action.ARGUMENT_ROW_INT", -1);
      i = Math.min(paramInt * i, getCount() - 1);
      if (paramInt >= 0)
      {
        smoothScrollToPosition(i);
        return true;
      }
    }
    return false;
  }
  
  boolean sequenceScroll(int paramInt)
  {
    int i = mSelectedPosition;
    int j = mNumColumns;
    int k = mItemCount;
    boolean bool = mStackFromBottom;
    int m = 0;
    int n = 0;
    int i1;
    int i2;
    if (!bool)
    {
      i1 = i / j * j;
      i2 = Math.min(i1 + j - 1, k - 1);
    }
    else
    {
      i2 = k - 1 - (k - 1 - i) / j * j;
      i1 = Math.max(0, i2 - j + 1);
    }
    bool = false;
    j = 0;
    switch (paramInt)
    {
    default: 
      break;
    case 2: 
      if (i < k - 1)
      {
        mLayoutMode = 6;
        setSelectionInt(i + 1);
        bool = true;
        j = n;
        if (i == i2) {
          j = 1;
        }
      }
      break;
    case 1: 
      if (i > 0)
      {
        mLayoutMode = 6;
        setSelectionInt(i - 1);
        bool = true;
        j = m;
        if (i == i1) {
          j = 1;
        }
      }
      break;
    }
    if (bool)
    {
      playSoundEffect(SoundEffectConstants.getContantForFocusDirection(paramInt));
      invokeOnItemScrollListener();
    }
    if (j != 0) {
      awakenScrollBars();
    }
    return bool;
  }
  
  public void setAdapter(ListAdapter paramListAdapter)
  {
    if ((mAdapter != null) && (mDataSetObserver != null)) {
      mAdapter.unregisterDataSetObserver(mDataSetObserver);
    }
    resetList();
    mRecycler.clear();
    mAdapter = paramListAdapter;
    mOldSelectedPosition = -1;
    mOldSelectedRowId = Long.MIN_VALUE;
    super.setAdapter(paramListAdapter);
    if (mAdapter != null)
    {
      mOldItemCount = mItemCount;
      mItemCount = mAdapter.getCount();
      mDataChanged = true;
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
      checkSelectionChanged();
    }
    else
    {
      checkFocus();
      checkSelectionChanged();
    }
    requestLayout();
  }
  
  public void setColumnWidth(int paramInt)
  {
    if (paramInt != mRequestedColumnWidth)
    {
      mRequestedColumnWidth = paramInt;
      requestLayoutIfNecessary();
    }
  }
  
  public void setGravity(int paramInt)
  {
    if (mGravity != paramInt)
    {
      mGravity = paramInt;
      requestLayoutIfNecessary();
    }
  }
  
  public void setHorizontalSpacing(int paramInt)
  {
    if (paramInt != mRequestedHorizontalSpacing)
    {
      mRequestedHorizontalSpacing = paramInt;
      requestLayoutIfNecessary();
    }
  }
  
  public void setNumColumns(int paramInt)
  {
    if (paramInt != mRequestedNumColumns)
    {
      mRequestedNumColumns = paramInt;
      requestLayoutIfNecessary();
    }
  }
  
  @RemotableViewMethod(asyncImpl="setRemoteViewsAdapterAsync")
  public void setRemoteViewsAdapter(Intent paramIntent)
  {
    super.setRemoteViewsAdapter(paramIntent);
  }
  
  public void setSelection(int paramInt)
  {
    if (!isInTouchMode()) {
      setNextSelectedPositionInt(paramInt);
    } else {
      mResurrectToPosition = paramInt;
    }
    mLayoutMode = 2;
    if (mPositionScroller != null) {
      mPositionScroller.stop();
    }
    requestLayout();
  }
  
  void setSelectionInt(int paramInt)
  {
    int i = mNextSelectedPosition;
    if (mPositionScroller != null) {
      mPositionScroller.stop();
    }
    setNextSelectedPositionInt(paramInt);
    layoutChildren();
    if (mStackFromBottom) {
      paramInt = mItemCount - 1 - mNextSelectedPosition;
    } else {
      paramInt = mNextSelectedPosition;
    }
    if (mStackFromBottom) {
      i = mItemCount - 1 - i;
    }
    if (paramInt / mNumColumns != i / mNumColumns) {
      awakenScrollBars();
    }
  }
  
  public void setStretchMode(int paramInt)
  {
    if (paramInt != mStretchMode)
    {
      mStretchMode = paramInt;
      requestLayoutIfNecessary();
    }
  }
  
  public void setVerticalSpacing(int paramInt)
  {
    if (paramInt != mVerticalSpacing)
    {
      mVerticalSpacing = paramInt;
      requestLayoutIfNecessary();
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
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface StretchMode {}
}
