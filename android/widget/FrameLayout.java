package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewHierarchyEncoder;
import com.android.internal.R.styleable;
import java.util.ArrayList;

@RemoteViews.RemoteView
public class FrameLayout
  extends ViewGroup
{
  private static final int DEFAULT_CHILD_GRAVITY = 8388659;
  @ViewDebug.ExportedProperty(category="padding")
  private int mForegroundPaddingBottom = 0;
  @ViewDebug.ExportedProperty(category="padding")
  private int mForegroundPaddingLeft = 0;
  @ViewDebug.ExportedProperty(category="padding")
  private int mForegroundPaddingRight = 0;
  @ViewDebug.ExportedProperty(category="padding")
  private int mForegroundPaddingTop = 0;
  private final ArrayList<View> mMatchParentChildren = new ArrayList(1);
  @ViewDebug.ExportedProperty(category="measurement")
  boolean mMeasureAllChildren = false;
  
  public FrameLayout(Context paramContext)
  {
    super(paramContext);
  }
  
  public FrameLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public FrameLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public FrameLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.FrameLayout, paramInt1, paramInt2);
    if (paramContext.getBoolean(0, false)) {
      setMeasureAllChildren(true);
    }
    paramContext.recycle();
  }
  
  private int getPaddingBottomWithForeground()
  {
    int i;
    if (isForegroundInsidePadding()) {
      i = Math.max(mPaddingBottom, mForegroundPaddingBottom);
    } else {
      i = mPaddingBottom + mForegroundPaddingBottom;
    }
    return i;
  }
  
  private int getPaddingTopWithForeground()
  {
    int i;
    if (isForegroundInsidePadding()) {
      i = Math.max(mPaddingTop, mForegroundPaddingTop);
    } else {
      i = mPaddingTop + mForegroundPaddingTop;
    }
    return i;
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  protected void encodeProperties(ViewHierarchyEncoder paramViewHierarchyEncoder)
  {
    super.encodeProperties(paramViewHierarchyEncoder);
    paramViewHierarchyEncoder.addProperty("measurement:measureAllChildren", mMeasureAllChildren);
    paramViewHierarchyEncoder.addProperty("padding:foregroundPaddingLeft", mForegroundPaddingLeft);
    paramViewHierarchyEncoder.addProperty("padding:foregroundPaddingTop", mForegroundPaddingTop);
    paramViewHierarchyEncoder.addProperty("padding:foregroundPaddingRight", mForegroundPaddingRight);
    paramViewHierarchyEncoder.addProperty("padding:foregroundPaddingBottom", mForegroundPaddingBottom);
  }
  
  protected LayoutParams generateDefaultLayoutParams()
  {
    return new LayoutParams(-1, -1);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    if (sPreserveMarginParamsInLayoutParamConversion)
    {
      if ((paramLayoutParams instanceof LayoutParams)) {
        return new LayoutParams((LayoutParams)paramLayoutParams);
      }
      if ((paramLayoutParams instanceof ViewGroup.MarginLayoutParams)) {
        return new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams);
      }
    }
    return new LayoutParams(paramLayoutParams);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return FrameLayout.class.getName();
  }
  
  @Deprecated
  public boolean getConsiderGoneChildrenWhenMeasuring()
  {
    return getMeasureAllChildren();
  }
  
  public boolean getMeasureAllChildren()
  {
    return mMeasureAllChildren;
  }
  
  int getPaddingLeftWithForeground()
  {
    int i;
    if (isForegroundInsidePadding()) {
      i = Math.max(mPaddingLeft, mForegroundPaddingLeft);
    } else {
      i = mPaddingLeft + mForegroundPaddingLeft;
    }
    return i;
  }
  
  int getPaddingRightWithForeground()
  {
    int i;
    if (isForegroundInsidePadding()) {
      i = Math.max(mPaddingRight, mForegroundPaddingRight);
    } else {
      i = mPaddingRight + mForegroundPaddingRight;
    }
    return i;
  }
  
  void layoutChildren(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
  {
    int i = getChildCount();
    int j = getPaddingLeftWithForeground();
    int k = paramInt3 - paramInt1 - getPaddingRightWithForeground();
    int m = getPaddingTopWithForeground();
    int n = paramInt4 - paramInt2 - getPaddingBottomWithForeground();
    int i1 = 0;
    paramInt3 = j;
    paramInt4 = i;
    while (i1 < paramInt4)
    {
      View localView = getChildAt(i1);
      if (localView.getVisibility() != 8)
      {
        LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
        j = localView.getMeasuredWidth();
        i = localView.getMeasuredHeight();
        paramInt2 = gravity;
        paramInt1 = paramInt2;
        if (paramInt2 == -1) {
          paramInt1 = 8388659;
        }
        paramInt2 = Gravity.getAbsoluteGravity(paramInt1, getLayoutDirection());
        paramInt1 &= 0x70;
        paramInt2 &= 0x7;
        if (paramInt2 != 1)
        {
          if ((paramInt2 == 5) && (!paramBoolean)) {
            paramInt2 = k - j - rightMargin;
          } else {
            paramInt2 = paramInt3 + leftMargin;
          }
        }
        else {
          paramInt2 = (k - paramInt3 - j) / 2 + paramInt3 + leftMargin - rightMargin;
        }
        if (paramInt1 != 16)
        {
          if (paramInt1 != 48)
          {
            if (paramInt1 != 80) {
              paramInt1 = topMargin + m;
            } else {
              paramInt1 = n - i - bottomMargin;
            }
          }
          else {
            paramInt1 = m + topMargin;
          }
        }
        else {
          paramInt1 = (n - m - i) / 2 + m + topMargin - bottomMargin;
        }
        localView.layout(paramInt2, paramInt1, paramInt2 + j, paramInt1 + i);
      }
      i1++;
    }
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    layoutChildren(paramInt1, paramInt2, paramInt3, paramInt4, false);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = getChildCount();
    if ((View.MeasureSpec.getMode(paramInt1) == 1073741824) && (View.MeasureSpec.getMode(paramInt2) == 1073741824)) {
      j = 0;
    } else {
      j = 1;
    }
    mMatchParentChildren.clear();
    int k = 0;
    int m = 0;
    int n = 0;
    Object localObject2;
    for (int i1 = 0; i1 < i; i1++)
    {
      localObject1 = getChildAt(i1);
      if ((!mMeasureAllChildren) && (((View)localObject1).getVisibility() == 8)) {
        continue;
      }
      measureChildWithMargins((View)localObject1, paramInt1, 0, paramInt2, 0);
      localObject2 = (LayoutParams)((View)localObject1).getLayoutParams();
      m = Math.max(m, ((View)localObject1).getMeasuredWidth() + leftMargin + rightMargin);
      k = Math.max(k, ((View)localObject1).getMeasuredHeight() + topMargin + bottomMargin);
      n = combineMeasuredStates(n, ((View)localObject1).getMeasuredState());
      if ((j != 0) && ((width == -1) || (height == -1))) {
        mMatchParentChildren.add(localObject1);
      }
    }
    i1 = getPaddingLeftWithForeground();
    int j = getPaddingRightWithForeground();
    k = Math.max(k + (getPaddingTopWithForeground() + getPaddingBottomWithForeground()), getSuggestedMinimumHeight());
    m = Math.max(m + (i1 + j), getSuggestedMinimumWidth());
    Object localObject1 = getForeground();
    i1 = k;
    j = m;
    if (localObject1 != null)
    {
      i1 = Math.max(k, ((Drawable)localObject1).getMinimumHeight());
      j = Math.max(m, ((Drawable)localObject1).getMinimumWidth());
    }
    setMeasuredDimension(resolveSizeAndState(j, paramInt1, n), resolveSizeAndState(i1, paramInt2, n << 16));
    k = mMatchParentChildren.size();
    if (k > 1) {
      for (j = 0; j < k; j++)
      {
        localObject1 = (View)mMatchParentChildren.get(j);
        localObject2 = (ViewGroup.MarginLayoutParams)((View)localObject1).getLayoutParams();
        if (width == -1) {
          n = View.MeasureSpec.makeMeasureSpec(Math.max(0, getMeasuredWidth() - getPaddingLeftWithForeground() - getPaddingRightWithForeground() - leftMargin - rightMargin), 1073741824);
        } else {
          n = getChildMeasureSpec(paramInt1, getPaddingLeftWithForeground() + getPaddingRightWithForeground() + leftMargin + rightMargin, width);
        }
        if (height == -1) {
          i1 = View.MeasureSpec.makeMeasureSpec(Math.max(0, getMeasuredHeight() - getPaddingTopWithForeground() - getPaddingBottomWithForeground() - topMargin - bottomMargin), 1073741824);
        } else {
          i1 = getChildMeasureSpec(paramInt2, getPaddingTopWithForeground() + getPaddingBottomWithForeground() + topMargin + bottomMargin, height);
        }
        ((View)localObject1).measure(n, i1);
      }
    }
  }
  
  @RemotableViewMethod
  public void setForegroundGravity(int paramInt)
  {
    if (getForegroundGravity() != paramInt)
    {
      super.setForegroundGravity(paramInt);
      Drawable localDrawable = getForeground();
      if ((getForegroundGravity() == 119) && (localDrawable != null))
      {
        Rect localRect = new Rect();
        if (localDrawable.getPadding(localRect))
        {
          mForegroundPaddingLeft = left;
          mForegroundPaddingTop = top;
          mForegroundPaddingRight = right;
          mForegroundPaddingBottom = bottom;
        }
      }
      else
      {
        mForegroundPaddingLeft = 0;
        mForegroundPaddingTop = 0;
        mForegroundPaddingRight = 0;
        mForegroundPaddingBottom = 0;
      }
      requestLayout();
    }
  }
  
  @RemotableViewMethod
  public void setMeasureAllChildren(boolean paramBoolean)
  {
    mMeasureAllChildren = paramBoolean;
  }
  
  public boolean shouldDelayChildPressedState()
  {
    return false;
  }
  
  public static class LayoutParams
    extends ViewGroup.MarginLayoutParams
  {
    public static final int UNSPECIFIED_GRAVITY = -1;
    public int gravity = -1;
    
    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
    }
    
    public LayoutParams(int paramInt1, int paramInt2, int paramInt3)
    {
      super(paramInt2);
      gravity = paramInt3;
    }
    
    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.FrameLayout_Layout);
      gravity = paramContext.getInt(0, -1);
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
    
    public LayoutParams(LayoutParams paramLayoutParams)
    {
      super();
      gravity = gravity;
    }
  }
}
