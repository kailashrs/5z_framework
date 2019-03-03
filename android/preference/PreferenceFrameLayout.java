package android.preference;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import com.android.internal.R.styleable;

public class PreferenceFrameLayout
  extends FrameLayout
{
  private static final int DEFAULT_BORDER_BOTTOM = 0;
  private static final int DEFAULT_BORDER_LEFT = 0;
  private static final int DEFAULT_BORDER_RIGHT = 0;
  private static final int DEFAULT_BORDER_TOP = 0;
  private final int mBorderBottom;
  private final int mBorderLeft;
  private final int mBorderRight;
  private final int mBorderTop;
  private boolean mPaddingApplied;
  
  public PreferenceFrameLayout(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public PreferenceFrameLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 17891499);
  }
  
  public PreferenceFrameLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public PreferenceFrameLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.PreferenceFrameLayout, paramInt1, paramInt2);
    float f = getResourcesgetDisplayMetricsdensity;
    int i = (int)(f * 0.0F + 0.5F);
    int j = (int)(f * 0.0F + 0.5F);
    paramInt1 = (int)(f * 0.0F + 0.5F);
    paramInt2 = (int)(0.0F * f + 0.5F);
    mBorderTop = paramAttributeSet.getDimensionPixelSize(3, i);
    mBorderBottom = paramAttributeSet.getDimensionPixelSize(0, j);
    mBorderLeft = paramAttributeSet.getDimensionPixelSize(1, paramInt1);
    mBorderRight = paramAttributeSet.getDimensionPixelSize(2, paramInt2);
    paramAttributeSet.recycle();
  }
  
  public void addView(View paramView)
  {
    int i = getPaddingTop();
    int j = getPaddingBottom();
    int k = getPaddingLeft();
    int m = getPaddingRight();
    LayoutParams localLayoutParams;
    if ((paramView.getLayoutParams() instanceof LayoutParams)) {
      localLayoutParams = (LayoutParams)paramView.getLayoutParams();
    } else {
      localLayoutParams = null;
    }
    int n;
    int i1;
    int i2;
    int i3;
    if ((localLayoutParams != null) && (removeBorders))
    {
      n = i;
      i1 = j;
      i2 = k;
      i3 = m;
      if (mPaddingApplied)
      {
        n = i - mBorderTop;
        i1 = j - mBorderBottom;
        i2 = k - mBorderLeft;
        i3 = m - mBorderRight;
        mPaddingApplied = false;
      }
    }
    else
    {
      n = i;
      i1 = j;
      i2 = k;
      i3 = m;
      if (!mPaddingApplied)
      {
        n = i + mBorderTop;
        i1 = j + mBorderBottom;
        i2 = k + mBorderLeft;
        i3 = m + mBorderRight;
        mPaddingApplied = true;
      }
    }
    m = getPaddingTop();
    i = getPaddingBottom();
    k = getPaddingLeft();
    j = getPaddingRight();
    if ((m != n) || (i != i1) || (k != i2) || (j != i3)) {
      setPadding(i2, n, i3, i1);
    }
    super.addView(paramView);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  public static class LayoutParams
    extends FrameLayout.LayoutParams
  {
    public boolean removeBorders = false;
    
    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
    }
    
    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.PreferenceFrameLayout_Layout);
      removeBorders = paramContext.getBoolean(0, false);
      paramContext.recycle();
    }
  }
}
