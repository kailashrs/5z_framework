package android.view;

import android.graphics.Rect;

public class TouchDelegate
{
  public static final int ABOVE = 1;
  public static final int BELOW = 2;
  public static final int TO_LEFT = 4;
  public static final int TO_RIGHT = 8;
  private Rect mBounds;
  private boolean mDelegateTargeted;
  private View mDelegateView;
  private int mSlop;
  private Rect mSlopBounds;
  
  public TouchDelegate(Rect paramRect, View paramView)
  {
    mBounds = paramRect;
    mSlop = ViewConfiguration.get(paramView.getContext()).getScaledTouchSlop();
    mSlopBounds = new Rect(paramRect);
    mSlopBounds.inset(-mSlop, -mSlop);
    mDelegateView = paramView;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = (int)paramMotionEvent.getX();
    int j = (int)paramMotionEvent.getY();
    boolean bool1 = false;
    int k = 1;
    int m = 1;
    boolean bool2 = false;
    switch (paramMotionEvent.getActionMasked())
    {
    case 4: 
    default: 
      break;
    case 3: 
      bool1 = mDelegateTargeted;
      mDelegateTargeted = false;
      break;
    case 1: 
    case 2: 
    case 5: 
    case 6: 
      boolean bool3 = mDelegateTargeted;
      bool1 = bool3;
      if (bool3)
      {
        k = m;
        if (!mSlopBounds.contains(i, j)) {
          k = 0;
        }
        bool1 = bool3;
      }
      break;
    case 0: 
      mDelegateTargeted = mBounds.contains(i, j);
      bool1 = mDelegateTargeted;
    }
    if (bool1)
    {
      View localView = mDelegateView;
      if (k != 0)
      {
        paramMotionEvent.setLocation(localView.getWidth() / 2, localView.getHeight() / 2);
      }
      else
      {
        k = mSlop;
        paramMotionEvent.setLocation(-(k * 2), -(k * 2));
      }
      bool2 = localView.dispatchTouchEvent(paramMotionEvent);
    }
    return bool2;
  }
}
