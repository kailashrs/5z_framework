package android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;

public class ZoomControls
  extends LinearLayout
{
  private final ZoomButton mZoomIn;
  private final ZoomButton mZoomOut;
  
  public ZoomControls(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public ZoomControls(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setFocusable(false);
    ((LayoutInflater)paramContext.getSystemService("layout_inflater")).inflate(17367365, this, true);
    mZoomIn = ((ZoomButton)findViewById(16909587));
    mZoomOut = ((ZoomButton)findViewById(16909589));
  }
  
  private void fade(int paramInt, float paramFloat1, float paramFloat2)
  {
    AlphaAnimation localAlphaAnimation = new AlphaAnimation(paramFloat1, paramFloat2);
    localAlphaAnimation.setDuration(500L);
    startAnimation(localAlphaAnimation);
    setVisibility(paramInt);
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return ZoomControls.class.getName();
  }
  
  public boolean hasFocus()
  {
    boolean bool;
    if ((!mZoomIn.hasFocus()) && (!mZoomOut.hasFocus())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public void hide()
  {
    fade(8, 1.0F, 0.0F);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    return true;
  }
  
  public void setIsZoomInEnabled(boolean paramBoolean)
  {
    mZoomIn.setEnabled(paramBoolean);
  }
  
  public void setIsZoomOutEnabled(boolean paramBoolean)
  {
    mZoomOut.setEnabled(paramBoolean);
  }
  
  public void setOnZoomInClickListener(View.OnClickListener paramOnClickListener)
  {
    mZoomIn.setOnClickListener(paramOnClickListener);
  }
  
  public void setOnZoomOutClickListener(View.OnClickListener paramOnClickListener)
  {
    mZoomOut.setOnClickListener(paramOnClickListener);
  }
  
  public void setZoomSpeed(long paramLong)
  {
    mZoomIn.setZoomSpeed(paramLong);
    mZoomOut.setZoomSpeed(paramLong);
  }
  
  public void show()
  {
    fade(0, 0.0F, 1.0F);
  }
}
