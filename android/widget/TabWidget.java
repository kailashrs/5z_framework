package android.widget;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.accessibility.AccessibilityEvent;
import com.android.internal.R.styleable;

public class TabWidget
  extends LinearLayout
  implements View.OnFocusChangeListener
{
  private final Rect mBounds = new Rect();
  private boolean mDrawBottomStrips = true;
  private int[] mImposedTabWidths;
  private int mImposedTabsHeight = -1;
  private Drawable mLeftStrip;
  private Drawable mRightStrip;
  private int mSelectedTab = -1;
  private OnTabSelectionChanged mSelectionChangedListener;
  private boolean mStripMoved;
  
  public TabWidget(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public TabWidget(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842883);
  }
  
  public TabWidget(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public TabWidget(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.TabWidget, paramInt1, paramInt2);
    mDrawBottomStrips = paramAttributeSet.getBoolean(3, mDrawBottomStrips);
    if (getApplicationInfotargetSdkVersion <= 4) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    }
    if (paramAttributeSet.hasValueOrEmpty(1)) {
      mLeftStrip = paramAttributeSet.getDrawable(1);
    } else if (paramInt1 != 0) {
      mLeftStrip = paramContext.getDrawable(17303864);
    } else {
      mLeftStrip = paramContext.getDrawable(17303863);
    }
    if (paramAttributeSet.hasValueOrEmpty(2)) {
      mRightStrip = paramAttributeSet.getDrawable(2);
    } else if (paramInt1 != 0) {
      mRightStrip = paramContext.getDrawable(17303866);
    } else {
      mRightStrip = paramContext.getDrawable(17303865);
    }
    paramAttributeSet.recycle();
    setChildrenDrawingOrderEnabled(true);
  }
  
  public void addView(View paramView)
  {
    if (paramView.getLayoutParams() == null)
    {
      LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(0, -1, 1.0F);
      localLayoutParams.setMargins(0, 0, 0, 0);
      paramView.setLayoutParams(localLayoutParams);
    }
    paramView.setFocusable(true);
    paramView.setClickable(true);
    if (paramView.getPointerIcon() == null) {
      paramView.setPointerIcon(PointerIcon.getSystemIcon(getContext(), 1002));
    }
    super.addView(paramView);
    paramView.setOnClickListener(new TabClickListener(getTabCount() - 1, null));
  }
  
  public void childDrawableStateChanged(View paramView)
  {
    if ((getTabCount() > 0) && (paramView == getChildTabViewAt(mSelectedTab))) {
      invalidate();
    }
    super.childDrawableStateChanged(paramView);
  }
  
  public void dispatchDraw(Canvas paramCanvas)
  {
    super.dispatchDraw(paramCanvas);
    if (getTabCount() == 0) {
      return;
    }
    if (!mDrawBottomStrips) {
      return;
    }
    View localView = getChildTabViewAt(mSelectedTab);
    Drawable localDrawable1 = mLeftStrip;
    Drawable localDrawable2 = mRightStrip;
    if (localDrawable1 != null) {
      localDrawable1.setState(localView.getDrawableState());
    }
    if (localDrawable2 != null) {
      localDrawable2.setState(localView.getDrawableState());
    }
    if (mStripMoved)
    {
      Rect localRect = mBounds;
      left = localView.getLeft();
      right = localView.getRight();
      int i = getHeight();
      if (localDrawable1 != null) {
        localDrawable1.setBounds(Math.min(0, left - localDrawable1.getIntrinsicWidth()), i - localDrawable1.getIntrinsicHeight(), left, i);
      }
      if (localDrawable2 != null) {
        localDrawable2.setBounds(right, i - localDrawable2.getIntrinsicHeight(), Math.max(getWidth(), right + localDrawable2.getIntrinsicWidth()), i);
      }
      mStripMoved = false;
    }
    if (localDrawable1 != null) {
      localDrawable1.draw(paramCanvas);
    }
    if (localDrawable2 != null) {
      localDrawable2.draw(paramCanvas);
    }
  }
  
  public void focusCurrentTab(int paramInt)
  {
    int i = mSelectedTab;
    setCurrentTab(paramInt);
    if (i != paramInt) {
      getChildTabViewAt(paramInt).requestFocus();
    }
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return TabWidget.class.getName();
  }
  
  protected int getChildDrawingOrder(int paramInt1, int paramInt2)
  {
    if (mSelectedTab == -1) {
      return paramInt2;
    }
    if (paramInt2 == paramInt1 - 1) {
      return mSelectedTab;
    }
    if (paramInt2 >= mSelectedTab) {
      return paramInt2 + 1;
    }
    return paramInt2;
  }
  
  public View getChildTabViewAt(int paramInt)
  {
    return getChildAt(paramInt);
  }
  
  public Drawable getLeftStripDrawable()
  {
    return mLeftStrip;
  }
  
  public Drawable getRightStripDrawable()
  {
    return mRightStrip;
  }
  
  public int getTabCount()
  {
    return getChildCount();
  }
  
  public boolean isStripEnabled()
  {
    return mDrawBottomStrips;
  }
  
  void measureChildBeforeLayout(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    int i = paramInt2;
    int j = paramInt4;
    if (!isMeasureWithLargestChildEnabled())
    {
      i = paramInt2;
      j = paramInt4;
      if (mImposedTabsHeight >= 0)
      {
        i = View.MeasureSpec.makeMeasureSpec(mImposedTabWidths[paramInt1] + paramInt3, 1073741824);
        j = View.MeasureSpec.makeMeasureSpec(mImposedTabsHeight, 1073741824);
      }
    }
    super.measureChildBeforeLayout(paramView, paramInt1, i, paramInt3, j, paramInt5);
  }
  
  void measureHorizontal(int paramInt1, int paramInt2)
  {
    if (View.MeasureSpec.getMode(paramInt1) == 0)
    {
      super.measureHorizontal(paramInt1, paramInt2);
      return;
    }
    int i = View.MeasureSpec.getSize(paramInt1);
    int j = View.MeasureSpec.makeSafeMeasureSpec(i, 0);
    mImposedTabsHeight = -1;
    super.measureHorizontal(j, paramInt2);
    int k = getMeasuredWidth() - i;
    if (k > 0)
    {
      int m = getChildCount();
      i = 0;
      for (j = 0; j < m; j++) {
        if (getChildAt(j).getVisibility() != 8) {
          i++;
        }
      }
      if (i > 0)
      {
        if ((mImposedTabWidths == null) || (mImposedTabWidths.length != m)) {
          mImposedTabWidths = new int[m];
        }
        for (j = 0; j < m; j++)
        {
          View localView = getChildAt(j);
          if (localView.getVisibility() != 8)
          {
            int n = localView.getMeasuredWidth();
            int i1 = Math.max(0, n - k / i);
            mImposedTabWidths[j] = i1;
            k -= n - i1;
            i--;
            mImposedTabsHeight = Math.max(mImposedTabsHeight, localView.getMeasuredHeight());
          }
        }
      }
    }
    super.measureHorizontal(paramInt1, paramInt2);
  }
  
  public void onFocusChange(View paramView, boolean paramBoolean) {}
  
  public void onInitializeAccessibilityEventInternal(AccessibilityEvent paramAccessibilityEvent)
  {
    super.onInitializeAccessibilityEventInternal(paramAccessibilityEvent);
    paramAccessibilityEvent.setItemCount(getTabCount());
    paramAccessibilityEvent.setCurrentItemIndex(mSelectedTab);
  }
  
  public PointerIcon onResolvePointerIcon(MotionEvent paramMotionEvent, int paramInt)
  {
    if (!isEnabled()) {
      return null;
    }
    return super.onResolvePointerIcon(paramMotionEvent, paramInt);
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mStripMoved = true;
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public void removeAllViews()
  {
    super.removeAllViews();
    mSelectedTab = -1;
  }
  
  public void setCurrentTab(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < getTabCount()) && (paramInt != mSelectedTab))
    {
      if (mSelectedTab != -1) {
        getChildTabViewAt(mSelectedTab).setSelected(false);
      }
      mSelectedTab = paramInt;
      getChildTabViewAt(mSelectedTab).setSelected(true);
      mStripMoved = true;
      return;
    }
  }
  
  public void setDividerDrawable(int paramInt)
  {
    setDividerDrawable(mContext.getDrawable(paramInt));
  }
  
  public void setDividerDrawable(Drawable paramDrawable)
  {
    super.setDividerDrawable(paramDrawable);
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    super.setEnabled(paramBoolean);
    int i = getTabCount();
    for (int j = 0; j < i; j++) {
      getChildTabViewAt(j).setEnabled(paramBoolean);
    }
  }
  
  public void setLeftStripDrawable(int paramInt)
  {
    setLeftStripDrawable(mContext.getDrawable(paramInt));
  }
  
  public void setLeftStripDrawable(Drawable paramDrawable)
  {
    mLeftStrip = paramDrawable;
    requestLayout();
    invalidate();
  }
  
  public void setRightStripDrawable(int paramInt)
  {
    setRightStripDrawable(mContext.getDrawable(paramInt));
  }
  
  public void setRightStripDrawable(Drawable paramDrawable)
  {
    mRightStrip = paramDrawable;
    requestLayout();
    invalidate();
  }
  
  public void setStripEnabled(boolean paramBoolean)
  {
    mDrawBottomStrips = paramBoolean;
    invalidate();
  }
  
  void setTabSelectionListener(OnTabSelectionChanged paramOnTabSelectionChanged)
  {
    mSelectionChangedListener = paramOnTabSelectionChanged;
  }
  
  static abstract interface OnTabSelectionChanged
  {
    public abstract void onTabSelectionChanged(int paramInt, boolean paramBoolean);
  }
  
  private class TabClickListener
    implements View.OnClickListener
  {
    private final int mTabIndex;
    
    private TabClickListener(int paramInt)
    {
      mTabIndex = paramInt;
    }
    
    public void onClick(View paramView)
    {
      mSelectionChangedListener.onTabSelectionChanged(mTabIndex, true);
    }
  }
}
