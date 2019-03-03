package com.android.internal.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.Gravity;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.LinearLayout;
import android.widget.RemoteViews.RemoteView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Comparator;

@RemoteViews.RemoteView
public class NotificationActionListLayout
  extends LinearLayout
{
  public static final Comparator<Pair<Integer, TextView>> MEASURE_ORDER_COMPARATOR = _..Lambda.NotificationActionListLayout.uFZFEmIEBpI3kn6c3tNvvgmMSv8.INSTANCE;
  private int mDefaultPaddingBottom;
  private int mDefaultPaddingTop;
  private int mEmphasizedHeight;
  private boolean mEmphasizedMode;
  private final int mGravity;
  private ArrayList<View> mMeasureOrderOther = new ArrayList();
  private ArrayList<Pair<Integer, TextView>> mMeasureOrderTextViews = new ArrayList();
  private int mRegularHeight;
  private int mTotalWidth = 0;
  
  public NotificationActionListLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public NotificationActionListLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public NotificationActionListLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, new int[] { 16842927 }, paramInt1, paramInt2);
    mGravity = paramContext.getInt(0, 0);
    paramContext.recycle();
  }
  
  private void clearMeasureOrder()
  {
    mMeasureOrderOther.clear();
    mMeasureOrderTextViews.clear();
  }
  
  private void rebuildMeasureOrder(int paramInt1, int paramInt2)
  {
    clearMeasureOrder();
    mMeasureOrderTextViews.ensureCapacity(paramInt1);
    mMeasureOrderOther.ensureCapacity(paramInt2);
    paramInt2 = getChildCount();
    for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++)
    {
      View localView = getChildAt(paramInt1);
      if (((localView instanceof TextView)) && (((TextView)localView).getText().length() > 0)) {
        mMeasureOrderTextViews.add(Pair.create(Integer.valueOf(((TextView)localView).getText().length()), (TextView)localView));
      } else {
        mMeasureOrderOther.add(localView);
      }
    }
    mMeasureOrderTextViews.sort(MEASURE_ORDER_COMPARATOR);
  }
  
  private void updateHeights()
  {
    int i = getResources().getDimensionPixelSize(17105316);
    mEmphasizedHeight = (getResources().getDimensionPixelSize(17105317) + i + getResources().getDimensionPixelSize(17105306));
    mRegularHeight = getResources().getDimensionPixelSize(17105307);
  }
  
  public int getExtraMeasureHeight()
  {
    if (mEmphasizedMode) {
      return mEmphasizedHeight - mRegularHeight;
    }
    return 0;
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    mDefaultPaddingBottom = getPaddingBottom();
    mDefaultPaddingTop = getPaddingTop();
    updateHeights();
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    Object localObject = this;
    if (mEmphasizedMode)
    {
      super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
      return;
    }
    paramBoolean = isLayoutRtl();
    int i = mPaddingTop;
    int j = mGravity;
    int k = 1;
    int m = 0;
    if ((j & 0x1) == 0) {
      k = 0;
    }
    int n = k;
    if (n != 0)
    {
      k = mPaddingLeft + paramInt1 + (paramInt3 - paramInt1) / 2 - mTotalWidth / 2;
    }
    else
    {
      j = mPaddingLeft;
      k = j;
      if (Gravity.getAbsoluteGravity(8388611, getLayoutDirection()) == 5) {
        k = j + (paramInt3 - paramInt1 - mTotalWidth);
      }
    }
    int i1 = mPaddingBottom;
    int i2 = getChildCount();
    j = 0;
    paramInt3 = 1;
    if (paramBoolean)
    {
      j = i2 - 1;
      paramInt3 = -1;
    }
    paramInt1 = i;
    while (m < i2)
    {
      localObject = getChildAt(paramInt3 * m + j);
      if (((View)localObject).getVisibility() != 8)
      {
        int i3 = ((View)localObject).getMeasuredWidth();
        int i4 = ((View)localObject).getMeasuredHeight();
        ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)((View)localObject).getLayoutParams();
        int i5 = paramInt1 + (paramInt4 - paramInt2 - i - i1 - i4) / 2 + topMargin - bottomMargin;
        k += leftMargin;
        ((View)localObject).layout(k, i5, k + i3, i5 + i4);
        k += rightMargin + i3;
      }
      m++;
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (mEmphasizedMode)
    {
      super.onMeasure(paramInt1, paramInt2);
      return;
    }
    int i = getChildCount();
    int j = 0;
    int k = 0;
    int m = 0;
    int n = 0;
    int i1 = 0;
    Object localObject;
    while (i1 < i)
    {
      localObject = getChildAt(i1);
      if ((localObject instanceof TextView)) {
        k++;
      } else {
        m++;
      }
      i2 = n;
      if (((View)localObject).getVisibility() != 8) {
        i2 = n + 1;
      }
      i1++;
      n = i2;
    }
    i1 = 0;
    if ((k != mMeasureOrderTextViews.size()) || (m != mMeasureOrderOther.size())) {
      i1 = 1;
    }
    if (i1 == 0)
    {
      i3 = mMeasureOrderTextViews.size();
      for (i2 = 0; i2 < i3; i2++)
      {
        localObject = (Pair)mMeasureOrderTextViews.get(i2);
        if (((Integer)first).intValue() != ((TextView)second).getText().length()) {
          i1 = 1;
        }
      }
    }
    if (i1 != 0) {
      rebuildMeasureOrder(k, m);
    }
    if (View.MeasureSpec.getMode(paramInt1) != 0) {
      m = 1;
    } else {
      m = 0;
    }
    int i4 = View.MeasureSpec.getSize(paramInt1) - mPaddingLeft - mPaddingRight;
    int i3 = mMeasureOrderOther.size();
    i1 = 0;
    int i2 = 0;
    k = i;
    while (j < k)
    {
      if (j < i3) {
        localObject = (View)mMeasureOrderOther.get(j);
      } else {
        localObject = (View)mMeasureOrderTextViews.get(j - i3)).second;
      }
      if (((View)localObject).getVisibility() != 8)
      {
        ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)((View)localObject).getLayoutParams();
        i = i1;
        if (m != 0) {
          i = i4 - (i4 - i1) / (n - i2);
        }
        measureChildWithMargins((View)localObject, paramInt1, i, paramInt2, 0);
        i1 += ((View)localObject).getMeasuredWidth() + rightMargin + leftMargin;
        i2++;
      }
      j++;
    }
    mTotalWidth = (i1 + mPaddingRight + mPaddingLeft);
    setMeasuredDimension(resolveSize(getSuggestedMinimumWidth(), paramInt1), resolveSize(getSuggestedMinimumHeight(), paramInt2));
  }
  
  public void onViewAdded(View paramView)
  {
    super.onViewAdded(paramView);
    clearMeasureOrder();
    if ((paramView.getBackground() instanceof RippleDrawable)) {
      ((RippleDrawable)paramView.getBackground()).setForceSoftware(true);
    }
  }
  
  public void onViewRemoved(View paramView)
  {
    super.onViewRemoved(paramView);
    clearMeasureOrder();
  }
  
  @RemotableViewMethod
  public void setEmphasizedMode(boolean paramBoolean)
  {
    mEmphasizedMode = paramBoolean;
    int k;
    if (paramBoolean)
    {
      int i = getResources().getDimensionPixelSize(17105316);
      int j = getResources().getDimensionPixelSize(17105317);
      k = mEmphasizedHeight;
      int m = getResources().getDimensionPixelSize(17105058);
      setPaddingRelative(getPaddingStart(), i - m, getPaddingEnd(), j - m);
    }
    else
    {
      setPaddingRelative(getPaddingStart(), mDefaultPaddingTop, getPaddingEnd(), mDefaultPaddingBottom);
      k = mRegularHeight;
    }
    ViewGroup.LayoutParams localLayoutParams = getLayoutParams();
    height = k;
    setLayoutParams(localLayoutParams);
  }
}
