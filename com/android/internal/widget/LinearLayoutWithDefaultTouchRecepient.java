package com.android.internal.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class LinearLayoutWithDefaultTouchRecepient
  extends LinearLayout
{
  private View mDefaultTouchRecepient;
  private final Rect mTempRect = new Rect();
  
  public LinearLayoutWithDefaultTouchRecepient(Context paramContext)
  {
    super(paramContext);
  }
  
  public LinearLayoutWithDefaultTouchRecepient(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
  {
    if (mDefaultTouchRecepient == null) {
      return super.dispatchTouchEvent(paramMotionEvent);
    }
    if (super.dispatchTouchEvent(paramMotionEvent)) {
      return true;
    }
    mTempRect.set(0, 0, 0, 0);
    offsetRectIntoDescendantCoords(mDefaultTouchRecepient, mTempRect);
    paramMotionEvent.setLocation(paramMotionEvent.getX() + mTempRect.left, paramMotionEvent.getY() + mTempRect.top);
    return mDefaultTouchRecepient.dispatchTouchEvent(paramMotionEvent);
  }
  
  public void setDefaultTouchRecepient(View paramView)
  {
    mDefaultTouchRecepient = paramView;
  }
}
