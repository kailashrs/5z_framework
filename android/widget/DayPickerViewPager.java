package android.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import com.android.internal.widget.ViewPager;
import com.android.internal.widget.ViewPager.LayoutParams;
import java.util.ArrayList;
import java.util.function.Predicate;

class DayPickerViewPager
  extends ViewPager
{
  private final ArrayList<View> mMatchParentChildren = new ArrayList(1);
  
  public DayPickerViewPager(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public DayPickerViewPager(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public DayPickerViewPager(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public DayPickerViewPager(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  protected <T extends View> T findViewByPredicateTraversal(Predicate<View> paramPredicate, View paramView)
  {
    if (paramPredicate.test(this)) {
      return this;
    }
    SimpleMonthView localSimpleMonthView = ((DayPickerPagerAdapter)getAdapter()).getView(getCurrent());
    View localView;
    if ((localSimpleMonthView != paramView) && (localSimpleMonthView != null))
    {
      localView = localSimpleMonthView.findViewByPredicate(paramPredicate);
      if (localView != null) {
        return localView;
      }
    }
    int i = getChildCount();
    for (int j = 0; j < i; j++)
    {
      localView = getChildAt(j);
      if ((localView != paramView) && (localView != localSimpleMonthView))
      {
        localView = localView.findViewByPredicate(paramPredicate);
        if (localView != null) {
          return localView;
        }
      }
    }
    return null;
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    populate();
    int i = getChildCount();
    int j = View.MeasureSpec.getMode(paramInt1);
    int k = 0;
    if ((j == 1073741824) && (View.MeasureSpec.getMode(paramInt2) == 1073741824)) {
      m = 0;
    } else {
      m = 1;
    }
    int n = 0;
    j = 0;
    int i1 = 0;
    int i2 = 0;
    Object localObject1;
    while (i2 < i)
    {
      localObject1 = getChildAt(i2);
      int i3 = n;
      i4 = i1;
      i5 = j;
      if (((View)localObject1).getVisibility() != 8)
      {
        measureChild((View)localObject1, paramInt1, paramInt2);
        localObject2 = (ViewPager.LayoutParams)((View)localObject1).getLayoutParams();
        n = Math.max(n, ((View)localObject1).getMeasuredWidth());
        i1 = Math.max(i1, ((View)localObject1).getMeasuredHeight());
        j = combineMeasuredStates(j, ((View)localObject1).getMeasuredState());
        i3 = n;
        i4 = i1;
        i5 = j;
        if (m != 0) {
          if (width != -1)
          {
            i3 = n;
            i4 = i1;
            i5 = j;
            if (height != -1) {}
          }
          else
          {
            mMatchParentChildren.add(localObject1);
            i5 = j;
            i4 = i1;
            i3 = n;
          }
        }
      }
      i2++;
      n = i3;
      i1 = i4;
      j = i5;
    }
    int i5 = getPaddingLeft();
    int m = getPaddingRight();
    int i4 = Math.max(i1 + (getPaddingTop() + getPaddingBottom()), getSuggestedMinimumHeight());
    i5 = Math.max(n + (i5 + m), getSuggestedMinimumWidth());
    Object localObject2 = getForeground();
    i1 = i4;
    m = i5;
    if (localObject2 != null)
    {
      i1 = Math.max(i4, ((Drawable)localObject2).getMinimumHeight());
      m = Math.max(i5, ((Drawable)localObject2).getMinimumWidth());
    }
    setMeasuredDimension(resolveSizeAndState(m, paramInt1, j), resolveSizeAndState(i1, paramInt2, j << 16));
    i4 = mMatchParentChildren.size();
    if (i4 > 1) {
      for (j = k; j < i4; j++)
      {
        localObject2 = (View)mMatchParentChildren.get(j);
        localObject1 = (ViewPager.LayoutParams)((View)localObject2).getLayoutParams();
        if (width == -1) {
          m = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), 1073741824);
        } else {
          m = getChildMeasureSpec(paramInt1, getPaddingLeft() + getPaddingRight(), width);
        }
        if (height == -1) {
          i1 = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), 1073741824);
        } else {
          i1 = getChildMeasureSpec(paramInt2, getPaddingTop() + getPaddingBottom(), height);
        }
        ((View)localObject2).measure(m, i1);
      }
    }
    mMatchParentChildren.clear();
  }
}
