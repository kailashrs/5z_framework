package android.widget;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import com.android.internal.view.menu.ShowableListMenu;

public abstract class ForwardingListener
  implements View.OnTouchListener, View.OnAttachStateChangeListener
{
  private int mActivePointerId;
  private Runnable mDisallowIntercept;
  private boolean mForwarding;
  private final int mLongPressTimeout;
  private final float mScaledTouchSlop;
  private final View mSrc;
  private final int mTapTimeout;
  private Runnable mTriggerLongPress;
  
  public ForwardingListener(View paramView)
  {
    mSrc = paramView;
    paramView.setLongClickable(true);
    paramView.addOnAttachStateChangeListener(this);
    mScaledTouchSlop = ViewConfiguration.get(paramView.getContext()).getScaledTouchSlop();
    mTapTimeout = ViewConfiguration.getTapTimeout();
    mLongPressTimeout = ((mTapTimeout + ViewConfiguration.getLongPressTimeout()) / 2);
  }
  
  private void clearCallbacks()
  {
    if (mTriggerLongPress != null) {
      mSrc.removeCallbacks(mTriggerLongPress);
    }
    if (mDisallowIntercept != null) {
      mSrc.removeCallbacks(mDisallowIntercept);
    }
  }
  
  private void onLongPress()
  {
    clearCallbacks();
    View localView = mSrc;
    if ((localView.isEnabled()) && (!localView.isLongClickable()))
    {
      if (!onForwardingStarted()) {
        return;
      }
      localView.getParent().requestDisallowInterceptTouchEvent(true);
      long l = SystemClock.uptimeMillis();
      MotionEvent localMotionEvent = MotionEvent.obtain(l, l, 3, 0.0F, 0.0F, 0);
      localView.onTouchEvent(localMotionEvent);
      localMotionEvent.recycle();
      mForwarding = true;
      return;
    }
  }
  
  private boolean onTouchForwarded(MotionEvent paramMotionEvent)
  {
    View localView = mSrc;
    Object localObject = getPopup();
    boolean bool1 = false;
    if ((localObject != null) && (((ShowableListMenu)localObject).isShowing()))
    {
      localObject = (DropDownListView)((ShowableListMenu)localObject).getListView();
      if ((localObject != null) && (((DropDownListView)localObject).isShown()))
      {
        MotionEvent localMotionEvent = MotionEvent.obtainNoHistory(paramMotionEvent);
        localView.toGlobalMotionEvent(localMotionEvent);
        ((DropDownListView)localObject).toLocalMotionEvent(localMotionEvent);
        boolean bool2 = ((DropDownListView)localObject).onForwardedEvent(localMotionEvent, mActivePointerId);
        localMotionEvent.recycle();
        int i = paramMotionEvent.getActionMasked();
        if ((i != 1) && (i != 3)) {
          i = 1;
        } else {
          i = 0;
        }
        boolean bool3 = bool1;
        if (bool2)
        {
          bool3 = bool1;
          if (i != 0) {
            bool3 = true;
          }
        }
        return bool3;
      }
      return false;
    }
    return false;
  }
  
  private boolean onTouchObserved(MotionEvent paramMotionEvent)
  {
    View localView = mSrc;
    if (!localView.isEnabled()) {
      return false;
    }
    switch (paramMotionEvent.getActionMasked())
    {
    default: 
      break;
    case 2: 
      int i = paramMotionEvent.findPointerIndex(mActivePointerId);
      if (i >= 0) {
        if (!localView.pointInView(paramMotionEvent.getX(i), paramMotionEvent.getY(i), mScaledTouchSlop))
        {
          clearCallbacks();
          localView.getParent().requestDisallowInterceptTouchEvent(true);
          return true;
        }
      }
      break;
    case 1: 
    case 3: 
      clearCallbacks();
      break;
    case 0: 
      mActivePointerId = paramMotionEvent.getPointerId(0);
      if (mDisallowIntercept == null) {
        mDisallowIntercept = new DisallowIntercept(null);
      }
      localView.postDelayed(mDisallowIntercept, mTapTimeout);
      if (mTriggerLongPress == null) {
        mTriggerLongPress = new TriggerLongPress(null);
      }
      localView.postDelayed(mTriggerLongPress, mLongPressTimeout);
    }
    return false;
  }
  
  public abstract ShowableListMenu getPopup();
  
  protected boolean onForwardingStarted()
  {
    ShowableListMenu localShowableListMenu = getPopup();
    if ((localShowableListMenu != null) && (!localShowableListMenu.isShowing())) {
      localShowableListMenu.show();
    }
    return true;
  }
  
  protected boolean onForwardingStopped()
  {
    ShowableListMenu localShowableListMenu = getPopup();
    if ((localShowableListMenu != null) && (localShowableListMenu.isShowing())) {
      localShowableListMenu.dismiss();
    }
    return true;
  }
  
  public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
  {
    boolean bool1 = mForwarding;
    boolean bool2 = true;
    boolean bool4;
    if (bool1)
    {
      if ((!onTouchForwarded(paramMotionEvent)) && (onForwardingStopped())) {
        bool3 = false;
      } else {
        bool3 = true;
      }
      bool4 = bool3;
    }
    else
    {
      if ((onTouchObserved(paramMotionEvent)) && (onForwardingStarted())) {
        bool3 = true;
      } else {
        bool3 = false;
      }
      bool4 = bool3;
      if (bool3)
      {
        long l = SystemClock.uptimeMillis();
        paramView = MotionEvent.obtain(l, l, 3, 0.0F, 0.0F, 0);
        mSrc.onTouchEvent(paramView);
        paramView.recycle();
        bool4 = bool3;
      }
    }
    mForwarding = bool4;
    boolean bool3 = bool2;
    if (!bool4) {
      if (bool1) {
        bool3 = bool2;
      } else {
        bool3 = false;
      }
    }
    return bool3;
  }
  
  public void onViewAttachedToWindow(View paramView) {}
  
  public void onViewDetachedFromWindow(View paramView)
  {
    mForwarding = false;
    mActivePointerId = -1;
    if (mDisallowIntercept != null) {
      mSrc.removeCallbacks(mDisallowIntercept);
    }
  }
  
  private class DisallowIntercept
    implements Runnable
  {
    private DisallowIntercept() {}
    
    public void run()
    {
      ViewParent localViewParent = mSrc.getParent();
      if (localViewParent != null) {
        localViewParent.requestDisallowInterceptTouchEvent(true);
      }
    }
  }
  
  private class TriggerLongPress
    implements Runnable
  {
    private TriggerLongPress() {}
    
    public void run()
    {
      ForwardingListener.this.onLongPress();
    }
  }
}
