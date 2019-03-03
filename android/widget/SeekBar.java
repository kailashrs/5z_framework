package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;

public class SeekBar
  extends AbsSeekBar
{
  private OnSeekBarChangeListener mOnSeekBarChangeListener;
  
  public SeekBar(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public SeekBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842875);
  }
  
  public SeekBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public SeekBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return SeekBar.class.getName();
  }
  
  public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfoInternal(paramAccessibilityNodeInfo);
    if (canUserSetProgress()) {
      paramAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_PROGRESS);
    }
  }
  
  void onProgressRefresh(float paramFloat, boolean paramBoolean, int paramInt)
  {
    super.onProgressRefresh(paramFloat, paramBoolean, paramInt);
    if (mOnSeekBarChangeListener != null) {
      mOnSeekBarChangeListener.onProgressChanged(this, paramInt, paramBoolean);
    }
  }
  
  void onStartTrackingTouch()
  {
    super.onStartTrackingTouch();
    if (mOnSeekBarChangeListener != null) {
      mOnSeekBarChangeListener.onStartTrackingTouch(this);
    }
  }
  
  void onStopTrackingTouch()
  {
    super.onStopTrackingTouch();
    if (mOnSeekBarChangeListener != null) {
      mOnSeekBarChangeListener.onStopTrackingTouch(this);
    }
  }
  
  public void setOnSeekBarChangeListener(OnSeekBarChangeListener paramOnSeekBarChangeListener)
  {
    mOnSeekBarChangeListener = paramOnSeekBarChangeListener;
  }
  
  public static abstract interface OnSeekBarChangeListener
  {
    public abstract void onProgressChanged(SeekBar paramSeekBar, int paramInt, boolean paramBoolean);
    
    public abstract void onStartTrackingTouch(SeekBar paramSeekBar);
    
    public abstract void onStopTrackingTouch(SeekBar paramSeekBar);
  }
}
