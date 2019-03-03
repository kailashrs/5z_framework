package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import com.android.internal.R.styleable;

@RemoteViews.RemoteView
@Deprecated
public class AbsoluteLayout
  extends ViewGroup
{
  public AbsoluteLayout(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public AbsoluteLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public AbsoluteLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public AbsoluteLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams()
  {
    return new LayoutParams(-2, -2, 0, 0);
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return new LayoutParams(paramLayoutParams);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    paramInt2 = getChildCount();
    for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++)
    {
      View localView = getChildAt(paramInt1);
      if (localView.getVisibility() != 8)
      {
        LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
        paramInt4 = mPaddingLeft + x;
        paramInt3 = mPaddingTop + y;
        localView.layout(paramInt4, paramInt3, localView.getMeasuredWidth() + paramInt4, localView.getMeasuredHeight() + paramInt3);
      }
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = getChildCount();
    int j = 0;
    measureChildren(paramInt1, paramInt2);
    int k = 0;
    int m = 0;
    while (m < i)
    {
      View localView = getChildAt(m);
      int n = j;
      i1 = k;
      if (localView.getVisibility() != 8)
      {
        LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
        n = x;
        int i2 = localView.getMeasuredWidth();
        int i3 = y;
        i1 = localView.getMeasuredHeight();
        n = Math.max(j, n + i2);
        i1 = Math.max(k, i3 + i1);
      }
      m++;
      j = n;
      k = i1;
    }
    m = mPaddingLeft;
    int i1 = mPaddingRight;
    k = Math.max(k + (mPaddingTop + mPaddingBottom), getSuggestedMinimumHeight());
    setMeasuredDimension(resolveSizeAndState(Math.max(j + (m + i1), getSuggestedMinimumWidth()), paramInt1, 0), resolveSizeAndState(k, paramInt2, 0));
  }
  
  public boolean shouldDelayChildPressedState()
  {
    return false;
  }
  
  public static class LayoutParams
    extends ViewGroup.LayoutParams
  {
    public int x;
    public int y;
    
    public LayoutParams(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      super(paramInt2);
      x = paramInt3;
      y = paramInt4;
    }
    
    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.AbsoluteLayout_Layout);
      x = paramContext.getDimensionPixelOffset(0, 0);
      y = paramContext.getDimensionPixelOffset(1, 0);
      paramContext.recycle();
    }
    
    public LayoutParams(ViewGroup.LayoutParams paramLayoutParams)
    {
      super();
    }
    
    public String debug(String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("Absolute.LayoutParams={width=");
      localStringBuilder.append(sizeToString(width));
      localStringBuilder.append(", height=");
      localStringBuilder.append(sizeToString(height));
      localStringBuilder.append(" x=");
      localStringBuilder.append(x);
      localStringBuilder.append(" y=");
      localStringBuilder.append(y);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
  }
}
