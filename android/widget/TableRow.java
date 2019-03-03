package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewGroup.OnHierarchyChangeListener;
import android.view.ViewHierarchyEncoder;
import com.android.internal.R.styleable;

public class TableRow
  extends LinearLayout
{
  private ChildrenTracker mChildrenTracker;
  private SparseIntArray mColumnToChildIndex;
  private int[] mColumnWidths;
  private int[] mConstrainedColumnWidths;
  private int mNumColumns = 0;
  
  public TableRow(Context paramContext)
  {
    super(paramContext);
    initTableRow();
  }
  
  public TableRow(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initTableRow();
  }
  
  private void initTableRow()
  {
    ViewGroup.OnHierarchyChangeListener localOnHierarchyChangeListener = mOnHierarchyChangeListener;
    mChildrenTracker = new ChildrenTracker(null);
    if (localOnHierarchyChangeListener != null) {
      mChildrenTracker.setOnHierarchyChangeListener(localOnHierarchyChangeListener);
    }
    super.setOnHierarchyChangeListener(mChildrenTracker);
  }
  
  private void mapIndexAndColumns()
  {
    if (mColumnToChildIndex == null)
    {
      int i = getChildCount();
      mColumnToChildIndex = new SparseIntArray();
      SparseIntArray localSparseIntArray = mColumnToChildIndex;
      int j = 0;
      for (int k = 0; k < i; k++)
      {
        LayoutParams localLayoutParams = (LayoutParams)getChildAt(k).getLayoutParams();
        int m = j;
        if (column >= j) {
          m = column;
        }
        j = m;
        m = 0;
        while (m < span)
        {
          localSparseIntArray.put(j, k);
          m++;
          j++;
        }
      }
      mNumColumns = j;
    }
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
    return TableRow.class.getName();
  }
  
  int getChildrenSkipCount(View paramView, int paramInt)
  {
    return getLayoutParamsspan - 1;
  }
  
  int[] getColumnsWidths(int paramInt1, int paramInt2)
  {
    int i = getVirtualChildCount();
    if ((mColumnWidths == null) || (i != mColumnWidths.length)) {
      mColumnWidths = new int[i];
    }
    int[] arrayOfInt = mColumnWidths;
    for (int j = 0; j < i; j++)
    {
      View localView = getVirtualChildAt(j);
      if ((localView != null) && (localView.getVisibility() != 8))
      {
        LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
        if (span == 1)
        {
          int k;
          switch (width)
          {
          default: 
            k = View.MeasureSpec.makeMeasureSpec(width, 1073741824);
            break;
          case -1: 
            k = View.MeasureSpec.makeSafeMeasureSpec(View.MeasureSpec.getSize(paramInt2), 0);
            break;
          case -2: 
            k = getChildMeasureSpec(paramInt1, 0, -2);
          }
          localView.measure(k, k);
          arrayOfInt[j] = (localView.getMeasuredWidth() + leftMargin + rightMargin);
        }
        else
        {
          arrayOfInt[j] = 0;
        }
      }
      else
      {
        arrayOfInt[j] = 0;
      }
    }
    return arrayOfInt;
  }
  
  int getLocationOffset(View paramView)
  {
    return getLayoutParamsmOffset[0];
  }
  
  int getNextLocationOffset(View paramView)
  {
    return getLayoutParamsmOffset[1];
  }
  
  public View getVirtualChildAt(int paramInt)
  {
    if (mColumnToChildIndex == null) {
      mapIndexAndColumns();
    }
    paramInt = mColumnToChildIndex.get(paramInt, -1);
    if (paramInt != -1) {
      return getChildAt(paramInt);
    }
    return null;
  }
  
  public int getVirtualChildCount()
  {
    if (mColumnToChildIndex == null) {
      mapIndexAndColumns();
    }
    return mNumColumns;
  }
  
  void measureChildBeforeLayout(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    if (mConstrainedColumnWidths != null)
    {
      LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
      int i = 1073741824;
      int j = span;
      int[] arrayOfInt = mConstrainedColumnWidths;
      paramInt2 = 0;
      for (paramInt3 = 0; paramInt3 < j; paramInt3++) {
        paramInt2 += arrayOfInt[(paramInt1 + paramInt3)];
      }
      paramInt3 = gravity;
      boolean bool = Gravity.isHorizontal(paramInt3);
      paramInt1 = i;
      if (bool) {
        paramInt1 = Integer.MIN_VALUE;
      }
      paramView.measure(View.MeasureSpec.makeMeasureSpec(Math.max(0, paramInt2 - leftMargin - rightMargin), paramInt1), getChildMeasureSpec(paramInt4, mPaddingTop + mPaddingBottom + topMargin + bottomMargin + paramInt5, height));
      if (bool)
      {
        paramInt1 = paramView.getMeasuredWidth();
        mOffset[1] = (paramInt2 - paramInt1);
        paramInt1 = Gravity.getAbsoluteGravity(paramInt3, getLayoutDirection()) & 0x7;
        if (paramInt1 != 1)
        {
          if (paramInt1 != 3)
          {
            if (paramInt1 != 5) {
              return;
            }
            mOffset[0] = mOffset[1];
          }
        }
        else {
          mOffset[0] = (mOffset[1] / 2);
        }
      }
      else
      {
        paramView = mOffset;
        mOffset[1] = 0;
        paramView[0] = 0;
      }
    }
    else
    {
      super.measureChildBeforeLayout(paramView, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
    }
  }
  
  int measureNullChild(int paramInt)
  {
    return mConstrainedColumnWidths[paramInt];
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    layoutHorizontal(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    measureHorizontal(paramInt1, paramInt2);
  }
  
  void setColumnCollapsed(int paramInt, boolean paramBoolean)
  {
    View localView = getVirtualChildAt(paramInt);
    if (localView != null)
    {
      if (paramBoolean) {
        paramInt = 8;
      } else {
        paramInt = 0;
      }
      localView.setVisibility(paramInt);
    }
  }
  
  void setColumnsWidthConstraints(int[] paramArrayOfInt)
  {
    if ((paramArrayOfInt != null) && (paramArrayOfInt.length >= getVirtualChildCount()))
    {
      mConstrainedColumnWidths = paramArrayOfInt;
      return;
    }
    throw new IllegalArgumentException("columnWidths should be >= getVirtualChildCount()");
  }
  
  public void setOnHierarchyChangeListener(ViewGroup.OnHierarchyChangeListener paramOnHierarchyChangeListener)
  {
    mChildrenTracker.setOnHierarchyChangeListener(paramOnHierarchyChangeListener);
  }
  
  private class ChildrenTracker
    implements ViewGroup.OnHierarchyChangeListener
  {
    private ViewGroup.OnHierarchyChangeListener listener;
    
    private ChildrenTracker() {}
    
    private void setOnHierarchyChangeListener(ViewGroup.OnHierarchyChangeListener paramOnHierarchyChangeListener)
    {
      listener = paramOnHierarchyChangeListener;
    }
    
    public void onChildViewAdded(View paramView1, View paramView2)
    {
      TableRow.access$302(TableRow.this, null);
      if (listener != null) {
        listener.onChildViewAdded(paramView1, paramView2);
      }
    }
    
    public void onChildViewRemoved(View paramView1, View paramView2)
    {
      TableRow.access$302(TableRow.this, null);
      if (listener != null) {
        listener.onChildViewRemoved(paramView1, paramView2);
      }
    }
  }
  
  public static class LayoutParams
    extends LinearLayout.LayoutParams
  {
    private static final int LOCATION = 0;
    private static final int LOCATION_NEXT = 1;
    @ViewDebug.ExportedProperty(category="layout")
    public int column;
    private int[] mOffset = new int[2];
    @ViewDebug.ExportedProperty(category="layout")
    public int span;
    
    public LayoutParams()
    {
      super(-2);
      column = -1;
      span = 1;
    }
    
    public LayoutParams(int paramInt)
    {
      this();
      column = paramInt;
    }
    
    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
      column = -1;
      span = 1;
    }
    
    public LayoutParams(int paramInt1, int paramInt2, float paramFloat)
    {
      super(paramInt2, paramFloat);
      column = -1;
      span = 1;
    }
    
    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.TableRow_Cell);
      column = paramContext.getInt(0, -1);
      span = paramContext.getInt(1, 1);
      if (span <= 1) {
        span = 1;
      }
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
    
    protected void encodeProperties(ViewHierarchyEncoder paramViewHierarchyEncoder)
    {
      super.encodeProperties(paramViewHierarchyEncoder);
      paramViewHierarchyEncoder.addProperty("layout:column", column);
      paramViewHierarchyEncoder.addProperty("layout:span", span);
    }
    
    protected void setBaseAttributes(TypedArray paramTypedArray, int paramInt1, int paramInt2)
    {
      if (paramTypedArray.hasValue(paramInt1)) {
        width = paramTypedArray.getLayoutDimension(paramInt1, "layout_width");
      } else {
        width = -1;
      }
      if (paramTypedArray.hasValue(paramInt2)) {
        height = paramTypedArray.getLayoutDimension(paramInt2, "layout_height");
      } else {
        height = -2;
      }
    }
  }
}
