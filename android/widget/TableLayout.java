package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewGroup.OnHierarchyChangeListener;
import com.android.internal.R.styleable;
import java.util.regex.Pattern;

public class TableLayout
  extends LinearLayout
{
  private SparseBooleanArray mCollapsedColumns;
  private boolean mInitialized;
  private int[] mMaxWidths;
  private PassThroughHierarchyChangeListener mPassThroughListener;
  private boolean mShrinkAllColumns;
  private SparseBooleanArray mShrinkableColumns;
  private boolean mStretchAllColumns;
  private SparseBooleanArray mStretchableColumns;
  
  public TableLayout(Context paramContext)
  {
    super(paramContext);
    initTableLayout();
  }
  
  public TableLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.TableLayout);
    paramAttributeSet = paramContext.getString(0);
    if (paramAttributeSet != null) {
      if (paramAttributeSet.charAt(0) == '*') {
        mStretchAllColumns = true;
      } else {
        mStretchableColumns = parseColumns(paramAttributeSet);
      }
    }
    paramAttributeSet = paramContext.getString(1);
    if (paramAttributeSet != null) {
      if (paramAttributeSet.charAt(0) == '*') {
        mShrinkAllColumns = true;
      } else {
        mShrinkableColumns = parseColumns(paramAttributeSet);
      }
    }
    paramAttributeSet = paramContext.getString(2);
    if (paramAttributeSet != null) {
      mCollapsedColumns = parseColumns(paramAttributeSet);
    }
    paramContext.recycle();
    initTableLayout();
  }
  
  private void findLargestCells(int paramInt1, int paramInt2)
  {
    int i = getChildCount();
    int j = 1;
    int k = 0;
    while (k < i)
    {
      Object localObject = getChildAt(k);
      if (((View)localObject).getVisibility() != 8)
      {
        if (!(localObject instanceof TableRow)) {
          break label250;
        }
        localObject = (TableRow)localObject;
        getLayoutParamsheight = -2;
        localObject = ((TableRow)localObject).getColumnsWidths(paramInt1, paramInt2);
        m = localObject.length;
        if (j != 0)
        {
          if ((mMaxWidths == null) || (mMaxWidths.length != m)) {
            mMaxWidths = new int[m];
          }
          System.arraycopy(localObject, 0, mMaxWidths, 0, m);
          j = 0;
        }
      }
      else
      {
        n = j;
        break label254;
      }
      int n = mMaxWidths.length;
      int i1 = m - n;
      if (i1 > 0)
      {
        arrayOfInt = mMaxWidths;
        mMaxWidths = new int[m];
        System.arraycopy(arrayOfInt, 0, mMaxWidths, 0, arrayOfInt.length);
        System.arraycopy(localObject, arrayOfInt.length, mMaxWidths, arrayOfInt.length, i1);
      }
      int[] arrayOfInt = mMaxWidths;
      int m = Math.min(n, m);
      for (i1 = 0;; i1++)
      {
        n = j;
        if (i1 >= m) {
          break;
        }
        arrayOfInt[i1] = Math.max(arrayOfInt[i1], localObject[i1]);
      }
      label250:
      n = j;
      label254:
      k++;
      j = n;
    }
  }
  
  private void initTableLayout()
  {
    if (mCollapsedColumns == null) {
      mCollapsedColumns = new SparseBooleanArray();
    }
    if (mStretchableColumns == null) {
      mStretchableColumns = new SparseBooleanArray();
    }
    if (mShrinkableColumns == null) {
      mShrinkableColumns = new SparseBooleanArray();
    }
    setOrientation(1);
    mPassThroughListener = new PassThroughHierarchyChangeListener(null);
    super.setOnHierarchyChangeListener(mPassThroughListener);
    mInitialized = true;
  }
  
  private void mutateColumnsWidth(SparseBooleanArray paramSparseBooleanArray, boolean paramBoolean, int paramInt1, int paramInt2)
  {
    int[] arrayOfInt = mMaxWidths;
    int i = arrayOfInt.length;
    int j;
    if (paramBoolean) {
      j = i;
    } else {
      j = paramSparseBooleanArray.size();
    }
    int k = (paramInt1 - paramInt2) / j;
    int m = getChildCount();
    paramInt2 = 0;
    for (paramInt1 = 0; paramInt1 < m; paramInt1++)
    {
      View localView = getChildAt(paramInt1);
      if ((localView instanceof TableRow)) {
        localView.forceLayout();
      }
    }
    if (!paramBoolean)
    {
      paramInt2 = 0;
      m = 0;
      while (m < j)
      {
        int n = paramSparseBooleanArray.keyAt(m);
        paramInt1 = paramInt2;
        if (paramSparseBooleanArray.valueAt(m)) {
          if (n < i)
          {
            arrayOfInt[n] += k;
            paramInt1 = paramInt2;
          }
          else
          {
            paramInt1 = paramInt2 + 1;
          }
        }
        m++;
        paramInt2 = paramInt1;
      }
      if ((paramInt2 > 0) && (paramInt2 < j))
      {
        paramInt2 = paramInt2 * k / (j - paramInt2);
        for (paramInt1 = 0; paramInt1 < j; paramInt1++)
        {
          m = paramSparseBooleanArray.keyAt(paramInt1);
          if ((paramSparseBooleanArray.valueAt(paramInt1)) && (m < i)) {
            if (paramInt2 > arrayOfInt[m]) {
              arrayOfInt[m] = 0;
            } else {
              arrayOfInt[m] += paramInt2;
            }
          }
        }
      }
      return;
    }
    for (paramInt1 = paramInt2; paramInt1 < j; paramInt1++) {
      arrayOfInt[paramInt1] += k;
    }
  }
  
  private static SparseBooleanArray parseColumns(String paramString)
  {
    SparseBooleanArray localSparseBooleanArray = new SparseBooleanArray();
    for (String str : Pattern.compile("\\s*,\\s*").split(paramString)) {
      try
      {
        int k = Integer.parseInt(str);
        if (k >= 0) {
          localSparseBooleanArray.put(k, true);
        }
      }
      catch (NumberFormatException localNumberFormatException) {}
    }
    return localSparseBooleanArray;
  }
  
  private void requestRowsLayout()
  {
    if (mInitialized)
    {
      int i = getChildCount();
      for (int j = 0; j < i; j++) {
        getChildAt(j).requestLayout();
      }
    }
  }
  
  private void shrinkAndStretchColumns(int paramInt)
  {
    if (mMaxWidths == null) {
      return;
    }
    int i = 0;
    int[] arrayOfInt = mMaxWidths;
    int j = arrayOfInt.length;
    for (int k = 0; k < j; k++) {
      i += arrayOfInt[k];
    }
    paramInt = View.MeasureSpec.getSize(paramInt) - mPaddingLeft - mPaddingRight;
    if ((i > paramInt) && ((mShrinkAllColumns) || (mShrinkableColumns.size() > 0))) {
      mutateColumnsWidth(mShrinkableColumns, mShrinkAllColumns, paramInt, i);
    } else if ((i < paramInt) && ((mStretchAllColumns) || (mStretchableColumns.size() > 0))) {
      mutateColumnsWidth(mStretchableColumns, mStretchAllColumns, paramInt, i);
    }
  }
  
  private void trackCollapsedColumns(View paramView)
  {
    if ((paramView instanceof TableRow))
    {
      paramView = (TableRow)paramView;
      SparseBooleanArray localSparseBooleanArray = mCollapsedColumns;
      int i = localSparseBooleanArray.size();
      for (int j = 0; j < i; j++)
      {
        int k = localSparseBooleanArray.keyAt(j);
        boolean bool = localSparseBooleanArray.valueAt(j);
        if (bool) {
          paramView.setColumnCollapsed(k, bool);
        }
      }
    }
  }
  
  public void addView(View paramView)
  {
    super.addView(paramView);
    requestRowsLayout();
  }
  
  public void addView(View paramView, int paramInt)
  {
    super.addView(paramView, paramInt);
    requestRowsLayout();
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams)
  {
    super.addView(paramView, paramInt, paramLayoutParams);
    requestRowsLayout();
  }
  
  public void addView(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    super.addView(paramView, paramLayoutParams);
    requestRowsLayout();
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  protected LinearLayout.LayoutParams generateDefaultLayoutParams()
  {
    return new LayoutParams();
  }
  
  protected LinearLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return new LayoutParams(paramLayoutParams);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return TableLayout.class.getName();
  }
  
  public boolean isColumnCollapsed(int paramInt)
  {
    return mCollapsedColumns.get(paramInt);
  }
  
  public boolean isColumnShrinkable(int paramInt)
  {
    boolean bool;
    if ((!mShrinkAllColumns) && (!mShrinkableColumns.get(paramInt))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isColumnStretchable(int paramInt)
  {
    boolean bool;
    if ((!mStretchAllColumns) && (!mStretchableColumns.get(paramInt))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isShrinkAllColumns()
  {
    return mShrinkAllColumns;
  }
  
  public boolean isStretchAllColumns()
  {
    return mStretchAllColumns;
  }
  
  void measureChildBeforeLayout(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    if ((paramView instanceof TableRow)) {
      ((TableRow)paramView).setColumnsWidthConstraints(mMaxWidths);
    }
    super.measureChildBeforeLayout(paramView, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
  }
  
  void measureVertical(int paramInt1, int paramInt2)
  {
    findLargestCells(paramInt1, paramInt2);
    shrinkAndStretchColumns(paramInt1);
    super.measureVertical(paramInt1, paramInt2);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    layoutVertical(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    measureVertical(paramInt1, paramInt2);
  }
  
  public void requestLayout()
  {
    if (mInitialized)
    {
      int i = getChildCount();
      for (int j = 0; j < i; j++) {
        getChildAt(j).forceLayout();
      }
    }
    super.requestLayout();
  }
  
  public void setColumnCollapsed(int paramInt, boolean paramBoolean)
  {
    mCollapsedColumns.put(paramInt, paramBoolean);
    int i = getChildCount();
    for (int j = 0; j < i; j++)
    {
      View localView = getChildAt(j);
      if ((localView instanceof TableRow)) {
        ((TableRow)localView).setColumnCollapsed(paramInt, paramBoolean);
      }
    }
    requestRowsLayout();
  }
  
  public void setColumnShrinkable(int paramInt, boolean paramBoolean)
  {
    mShrinkableColumns.put(paramInt, paramBoolean);
    requestRowsLayout();
  }
  
  public void setColumnStretchable(int paramInt, boolean paramBoolean)
  {
    mStretchableColumns.put(paramInt, paramBoolean);
    requestRowsLayout();
  }
  
  public void setOnHierarchyChangeListener(ViewGroup.OnHierarchyChangeListener paramOnHierarchyChangeListener)
  {
    PassThroughHierarchyChangeListener.access$102(mPassThroughListener, paramOnHierarchyChangeListener);
  }
  
  public void setShrinkAllColumns(boolean paramBoolean)
  {
    mShrinkAllColumns = paramBoolean;
  }
  
  public void setStretchAllColumns(boolean paramBoolean)
  {
    mStretchAllColumns = paramBoolean;
  }
  
  public static class LayoutParams
    extends LinearLayout.LayoutParams
  {
    public LayoutParams()
    {
      super(-2);
    }
    
    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
    }
    
    public LayoutParams(int paramInt1, int paramInt2, float paramFloat)
    {
      super(paramInt2, paramFloat);
    }
    
    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
    }
    
    public LayoutParams(ViewGroup.LayoutParams paramLayoutParams)
    {
      super();
      width = -1;
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams paramMarginLayoutParams)
    {
      super();
      width = -1;
      if ((paramMarginLayoutParams instanceof LayoutParams)) {
        weight = weight;
      }
    }
    
    protected void setBaseAttributes(TypedArray paramTypedArray, int paramInt1, int paramInt2)
    {
      width = -1;
      if (paramTypedArray.hasValue(paramInt2)) {
        height = paramTypedArray.getLayoutDimension(paramInt2, "layout_height");
      } else {
        height = -2;
      }
    }
  }
  
  private class PassThroughHierarchyChangeListener
    implements ViewGroup.OnHierarchyChangeListener
  {
    private ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;
    
    private PassThroughHierarchyChangeListener() {}
    
    public void onChildViewAdded(View paramView1, View paramView2)
    {
      TableLayout.this.trackCollapsedColumns(paramView2);
      if (mOnHierarchyChangeListener != null) {
        mOnHierarchyChangeListener.onChildViewAdded(paramView1, paramView2);
      }
    }
    
    public void onChildViewRemoved(View paramView1, View paramView2)
    {
      if (mOnHierarchyChangeListener != null) {
        mOnHierarchyChangeListener.onChildViewRemoved(paramView1, paramView2);
      }
    }
  }
}
