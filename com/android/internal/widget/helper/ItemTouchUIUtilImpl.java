package com.android.internal.widget.helper;

import android.graphics.Canvas;
import android.view.View;
import com.android.internal.widget.RecyclerView;

class ItemTouchUIUtilImpl
  implements ItemTouchUIUtil
{
  ItemTouchUIUtilImpl() {}
  
  private float findMaxElevation(RecyclerView paramRecyclerView, View paramView)
  {
    int i = paramRecyclerView.getChildCount();
    float f1 = 0.0F;
    int j = 0;
    while (j < i)
    {
      View localView = paramRecyclerView.getChildAt(j);
      float f2;
      if (localView == paramView)
      {
        f2 = f1;
      }
      else
      {
        float f3 = localView.getElevation();
        f2 = f1;
        if (f3 > f1) {
          f2 = f3;
        }
      }
      j++;
      f1 = f2;
    }
    return f1;
  }
  
  public void clearView(View paramView)
  {
    Object localObject = paramView.getTag(16909064);
    if ((localObject != null) && ((localObject instanceof Float))) {
      paramView.setElevation(((Float)localObject).floatValue());
    }
    paramView.setTag(16909064, null);
    paramView.setTranslationX(0.0F);
    paramView.setTranslationY(0.0F);
  }
  
  public void onDraw(Canvas paramCanvas, RecyclerView paramRecyclerView, View paramView, float paramFloat1, float paramFloat2, int paramInt, boolean paramBoolean)
  {
    if ((paramBoolean) && (paramView.getTag(16909064) == null))
    {
      float f = paramView.getElevation();
      paramView.setElevation(1.0F + findMaxElevation(paramRecyclerView, paramView));
      paramView.setTag(16909064, Float.valueOf(f));
    }
    paramView.setTranslationX(paramFloat1);
    paramView.setTranslationY(paramFloat2);
  }
  
  public void onDrawOver(Canvas paramCanvas, RecyclerView paramRecyclerView, View paramView, float paramFloat1, float paramFloat2, int paramInt, boolean paramBoolean) {}
  
  public void onSelected(View paramView) {}
}
