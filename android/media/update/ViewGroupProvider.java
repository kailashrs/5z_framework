package android.media.update;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public abstract interface ViewGroupProvider
{
  public abstract boolean checkLayoutParams_impl(ViewGroup.LayoutParams paramLayoutParams);
  
  public abstract boolean dispatchTouchEvent_impl(MotionEvent paramMotionEvent);
  
  public abstract ViewGroup.LayoutParams generateDefaultLayoutParams_impl();
  
  public abstract ViewGroup.LayoutParams generateLayoutParams_impl(AttributeSet paramAttributeSet);
  
  public abstract ViewGroup.LayoutParams generateLayoutParams_impl(ViewGroup.LayoutParams paramLayoutParams);
  
  public abstract CharSequence getAccessibilityClassName_impl();
  
  public abstract int getSuggestedMinimumHeight_impl();
  
  public abstract int getSuggestedMinimumWidth_impl();
  
  public abstract void measureChildWithMargins_impl(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract void onAttachedToWindow_impl();
  
  public abstract void onDetachedFromWindow_impl();
  
  public abstract void onFinishInflate_impl();
  
  public abstract void onLayout_impl(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract void onMeasure_impl(int paramInt1, int paramInt2);
  
  public abstract boolean onTouchEvent_impl(MotionEvent paramMotionEvent);
  
  public abstract boolean onTrackballEvent_impl(MotionEvent paramMotionEvent);
  
  public abstract void onVisibilityAggregated_impl(boolean paramBoolean);
  
  public abstract void setEnabled_impl(boolean paramBoolean);
  
  public abstract void setMeasuredDimension_impl(int paramInt1, int paramInt2);
  
  public abstract boolean shouldDelayChildPressedState_impl();
}
