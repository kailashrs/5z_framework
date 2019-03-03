package android.media.update;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public abstract class ViewGroupHelper<T extends ViewGroupProvider>
  extends ViewGroup
{
  public final T mProvider;
  
  public ViewGroupHelper(ProviderCreator<T> paramProviderCreator, Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    mProvider = paramProviderCreator.createProvider(this, new SuperProvider(), new PrivateProvider());
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return mProvider.checkLayoutParams_impl(paramLayoutParams);
  }
  
  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
  {
    return mProvider.dispatchTouchEvent_impl(paramMotionEvent);
  }
  
  protected ViewGroup.LayoutParams generateDefaultLayoutParams()
  {
    return mProvider.generateDefaultLayoutParams_impl();
  }
  
  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return mProvider.generateLayoutParams_impl(paramAttributeSet);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return mProvider.generateLayoutParams_impl(paramLayoutParams);
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return mProvider.getAccessibilityClassName_impl();
  }
  
  public T getProvider()
  {
    return mProvider;
  }
  
  protected int getSuggestedMinimumHeight()
  {
    return mProvider.getSuggestedMinimumHeight_impl();
  }
  
  protected int getSuggestedMinimumWidth()
  {
    return mProvider.getSuggestedMinimumWidth_impl();
  }
  
  protected void measureChildWithMargins(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mProvider.measureChildWithMargins_impl(paramView, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  protected void onAttachedToWindow()
  {
    mProvider.onAttachedToWindow_impl();
  }
  
  protected void onDetachedFromWindow()
  {
    mProvider.onDetachedFromWindow_impl();
  }
  
  public void onFinishInflate()
  {
    mProvider.onFinishInflate_impl();
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mProvider.onLayout_impl(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    mProvider.onMeasure_impl(paramInt1, paramInt2);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    return mProvider.onTouchEvent_impl(paramMotionEvent);
  }
  
  public boolean onTrackballEvent(MotionEvent paramMotionEvent)
  {
    return mProvider.onTrackballEvent_impl(paramMotionEvent);
  }
  
  public void onVisibilityAggregated(boolean paramBoolean)
  {
    mProvider.onVisibilityAggregated_impl(paramBoolean);
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    mProvider.setEnabled_impl(paramBoolean);
  }
  
  public boolean shouldDelayChildPressedState()
  {
    return mProvider.shouldDelayChildPressedState_impl();
  }
  
  public class PrivateProvider
    implements ViewGroupProvider
  {
    public PrivateProvider() {}
    
    public boolean checkLayoutParams_impl(ViewGroup.LayoutParams paramLayoutParams)
    {
      return checkLayoutParams(paramLayoutParams);
    }
    
    public boolean dispatchTouchEvent_impl(MotionEvent paramMotionEvent)
    {
      return dispatchTouchEvent(paramMotionEvent);
    }
    
    public ViewGroup.LayoutParams generateDefaultLayoutParams_impl()
    {
      return generateDefaultLayoutParams();
    }
    
    public ViewGroup.LayoutParams generateLayoutParams_impl(AttributeSet paramAttributeSet)
    {
      return generateLayoutParams(paramAttributeSet);
    }
    
    public ViewGroup.LayoutParams generateLayoutParams_impl(ViewGroup.LayoutParams paramLayoutParams)
    {
      return generateLayoutParams(paramLayoutParams);
    }
    
    public CharSequence getAccessibilityClassName_impl()
    {
      return getAccessibilityClassName();
    }
    
    public int getSuggestedMinimumHeight_impl()
    {
      return getSuggestedMinimumHeight();
    }
    
    public int getSuggestedMinimumWidth_impl()
    {
      return getSuggestedMinimumWidth();
    }
    
    public void measureChildWithMargins_impl(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      measureChildWithMargins(paramView, paramInt1, paramInt2, paramInt3, paramInt4);
    }
    
    public void onAttachedToWindow_impl()
    {
      onAttachedToWindow();
    }
    
    public void onDetachedFromWindow_impl()
    {
      onDetachedFromWindow();
    }
    
    public void onFinishInflate_impl()
    {
      onFinishInflate();
    }
    
    public void onLayout_impl(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    }
    
    public void onMeasure_impl(int paramInt1, int paramInt2)
    {
      onMeasure(paramInt1, paramInt2);
    }
    
    public boolean onTouchEvent_impl(MotionEvent paramMotionEvent)
    {
      return onTouchEvent(paramMotionEvent);
    }
    
    public boolean onTrackballEvent_impl(MotionEvent paramMotionEvent)
    {
      return onTrackballEvent(paramMotionEvent);
    }
    
    public void onVisibilityAggregated_impl(boolean paramBoolean)
    {
      onVisibilityAggregated(paramBoolean);
    }
    
    public void setEnabled_impl(boolean paramBoolean)
    {
      setEnabled(paramBoolean);
    }
    
    public void setMeasuredDimension_impl(int paramInt1, int paramInt2)
    {
      setMeasuredDimension(paramInt1, paramInt2);
    }
    
    public boolean shouldDelayChildPressedState_impl()
    {
      return shouldDelayChildPressedState();
    }
  }
  
  @FunctionalInterface
  public static abstract interface ProviderCreator<T extends ViewGroupProvider>
  {
    public abstract T createProvider(ViewGroupHelper<T> paramViewGroupHelper, ViewGroupProvider paramViewGroupProvider1, ViewGroupProvider paramViewGroupProvider2);
  }
  
  public class SuperProvider
    implements ViewGroupProvider
  {
    public SuperProvider() {}
    
    public boolean checkLayoutParams_impl(ViewGroup.LayoutParams paramLayoutParams)
    {
      return ViewGroupHelper.this.checkLayoutParams(paramLayoutParams);
    }
    
    public boolean dispatchTouchEvent_impl(MotionEvent paramMotionEvent)
    {
      return ViewGroupHelper.this.dispatchTouchEvent(paramMotionEvent);
    }
    
    public ViewGroup.LayoutParams generateDefaultLayoutParams_impl()
    {
      return ViewGroupHelper.this.generateDefaultLayoutParams();
    }
    
    public ViewGroup.LayoutParams generateLayoutParams_impl(AttributeSet paramAttributeSet)
    {
      return ViewGroupHelper.this.generateLayoutParams(paramAttributeSet);
    }
    
    public ViewGroup.LayoutParams generateLayoutParams_impl(ViewGroup.LayoutParams paramLayoutParams)
    {
      return ViewGroupHelper.this.generateLayoutParams(paramLayoutParams);
    }
    
    public CharSequence getAccessibilityClassName_impl()
    {
      return ViewGroupHelper.this.getAccessibilityClassName();
    }
    
    public int getSuggestedMinimumHeight_impl()
    {
      return ViewGroupHelper.this.getSuggestedMinimumHeight();
    }
    
    public int getSuggestedMinimumWidth_impl()
    {
      return ViewGroupHelper.this.getSuggestedMinimumWidth();
    }
    
    public void measureChildWithMargins_impl(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      ViewGroupHelper.this.measureChildWithMargins(paramView, paramInt1, paramInt2, paramInt3, paramInt4);
    }
    
    public void onAttachedToWindow_impl()
    {
      ViewGroupHelper.this.onAttachedToWindow();
    }
    
    public void onDetachedFromWindow_impl()
    {
      ViewGroupHelper.this.onDetachedFromWindow();
    }
    
    public void onFinishInflate_impl()
    {
      ViewGroupHelper.this.onFinishInflate();
    }
    
    public void onLayout_impl(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {}
    
    public void onMeasure_impl(int paramInt1, int paramInt2)
    {
      ViewGroupHelper.this.onMeasure(paramInt1, paramInt2);
    }
    
    public boolean onTouchEvent_impl(MotionEvent paramMotionEvent)
    {
      return ViewGroupHelper.this.onTouchEvent(paramMotionEvent);
    }
    
    public boolean onTrackballEvent_impl(MotionEvent paramMotionEvent)
    {
      return ViewGroupHelper.this.onTrackballEvent(paramMotionEvent);
    }
    
    public void onVisibilityAggregated_impl(boolean paramBoolean)
    {
      ViewGroupHelper.this.onVisibilityAggregated(paramBoolean);
    }
    
    public void setEnabled_impl(boolean paramBoolean)
    {
      ViewGroupHelper.this.setEnabled(paramBoolean);
    }
    
    public void setMeasuredDimension_impl(int paramInt1, int paramInt2)
    {
      ViewGroupHelper.this.setMeasuredDimension(paramInt1, paramInt2);
    }
    
    public boolean shouldDelayChildPressedState_impl()
    {
      return ViewGroupHelper.this.shouldDelayChildPressedState();
    }
  }
}
