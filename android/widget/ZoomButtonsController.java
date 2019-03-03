package android.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.KeyEvent.DispatcherState;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewRootImpl;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

@Deprecated
public class ZoomButtonsController
  implements View.OnTouchListener
{
  private static final int MSG_DISMISS_ZOOM_CONTROLS = 3;
  private static final int MSG_POST_CONFIGURATION_CHANGED = 2;
  private static final int MSG_POST_SET_VISIBLE = 4;
  private static final String TAG = "ZoomButtonsController";
  private static final int ZOOM_CONTROLS_TIMEOUT = (int)ViewConfiguration.getZoomControlsTimeout();
  private static final int ZOOM_CONTROLS_TOUCH_PADDING = 20;
  private boolean mAutoDismissControls = true;
  private OnZoomListener mCallback;
  private final IntentFilter mConfigurationChangedFilter = new IntentFilter("android.intent.action.CONFIGURATION_CHANGED");
  private final BroadcastReceiver mConfigurationChangedReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      if (!mIsVisible) {
        return;
      }
      mHandler.removeMessages(2);
      mHandler.sendEmptyMessage(2);
    }
  };
  private final FrameLayout mContainer;
  private WindowManager.LayoutParams mContainerLayoutParams;
  private final int[] mContainerRawLocation = new int[2];
  private final Context mContext;
  private ZoomControls mControls;
  private final Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      switch (what)
      {
      default: 
        break;
      case 4: 
        if (mOwnerView.getWindowToken() == null) {
          Log.e("ZoomButtonsController", "Cannot make the zoom controller visible if the owner view is not attached to a window.");
        } else {
          setVisible(true);
        }
        break;
      case 3: 
        setVisible(false);
        break;
      case 2: 
        ZoomButtonsController.this.onPostConfigurationChanged();
      }
    }
  };
  private boolean mIsVisible;
  private final View mOwnerView;
  private final int[] mOwnerViewRawLocation = new int[2];
  private Runnable mPostedVisibleInitializer;
  private boolean mReleaseTouchListenerOnUp;
  private final int[] mTempIntArray = new int[2];
  private final Rect mTempRect = new Rect();
  private int mTouchPaddingScaledSq;
  private View mTouchTargetView;
  private final int[] mTouchTargetWindowLocation = new int[2];
  private final WindowManager mWindowManager;
  
  public ZoomButtonsController(View paramView)
  {
    mContext = paramView.getContext();
    mWindowManager = ((WindowManager)mContext.getSystemService("window"));
    mOwnerView = paramView;
    mTouchPaddingScaledSq = ((int)(20.0F * mContext.getResources().getDisplayMetrics().density));
    mTouchPaddingScaledSq *= mTouchPaddingScaledSq;
    mContainer = createContainer();
  }
  
  private FrameLayout createContainer()
  {
    WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams(-2, -2);
    gravity = 8388659;
    flags = 131608;
    height = -2;
    width = -1;
    type = 1000;
    format = -3;
    windowAnimations = 16974603;
    mContainerLayoutParams = localLayoutParams;
    Container localContainer = new Container(mContext);
    localContainer.setLayoutParams(localLayoutParams);
    localContainer.setMeasureAllChildren(true);
    ((LayoutInflater)mContext.getSystemService("layout_inflater")).inflate(17367364, localContainer);
    mControls = ((ZoomControls)localContainer.findViewById(16909586));
    mControls.setOnZoomInClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        ZoomButtonsController.this.dismissControlsDelayed(ZoomButtonsController.ZOOM_CONTROLS_TIMEOUT);
        if (mCallback != null) {
          mCallback.onZoom(true);
        }
      }
    });
    mControls.setOnZoomOutClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        ZoomButtonsController.this.dismissControlsDelayed(ZoomButtonsController.ZOOM_CONTROLS_TIMEOUT);
        if (mCallback != null) {
          mCallback.onZoom(false);
        }
      }
    });
    return localContainer;
  }
  
  private void dismissControlsDelayed(int paramInt)
  {
    if (mAutoDismissControls)
    {
      mHandler.removeMessages(3);
      mHandler.sendEmptyMessageDelayed(3, paramInt);
    }
  }
  
  private View findViewForTouch(int paramInt1, int paramInt2)
  {
    int i = paramInt1 - mContainerRawLocation[0];
    int j = paramInt2 - mContainerRawLocation[1];
    Rect localRect = mTempRect;
    Object localObject1 = null;
    paramInt2 = Integer.MAX_VALUE;
    paramInt1 = mContainer.getChildCount() - 1;
    while (paramInt1 >= 0)
    {
      View localView = mContainer.getChildAt(paramInt1);
      Object localObject2;
      int k;
      if (localView.getVisibility() != 0)
      {
        localObject2 = localObject1;
        k = paramInt2;
      }
      else
      {
        localView.getHitRect(localRect);
        if (localRect.contains(i, j)) {
          return localView;
        }
        if ((i >= left) && (i <= right)) {
          k = 0;
        } else {
          k = Math.min(Math.abs(left - i), Math.abs(i - right));
        }
        if ((j >= top) && (j <= bottom)) {
          m = 0;
        } else {
          m = Math.min(Math.abs(top - j), Math.abs(j - bottom));
        }
        int m = k * k + m * m;
        localObject2 = localObject1;
        k = paramInt2;
        if (m < mTouchPaddingScaledSq)
        {
          localObject2 = localObject1;
          k = paramInt2;
          if (m < paramInt2)
          {
            localObject2 = localView;
            k = m;
          }
        }
      }
      paramInt1--;
      localObject1 = localObject2;
      paramInt2 = k;
    }
    return localObject1;
  }
  
  private boolean isInterestingKey(int paramInt)
  {
    if ((paramInt != 4) && (paramInt != 66)) {
      switch (paramInt)
      {
      default: 
        return false;
      }
    }
    return true;
  }
  
  private boolean onContainerKey(KeyEvent paramKeyEvent)
  {
    int i = paramKeyEvent.getKeyCode();
    if (isInterestingKey(i))
    {
      if (i == 4)
      {
        if ((paramKeyEvent.getAction() == 0) && (paramKeyEvent.getRepeatCount() == 0))
        {
          if (mOwnerView != null)
          {
            localObject = mOwnerView.getKeyDispatcherState();
            if (localObject != null) {
              ((KeyEvent.DispatcherState)localObject).startTracking(paramKeyEvent, this);
            }
          }
          return true;
        }
        if ((paramKeyEvent.getAction() == 1) && (paramKeyEvent.isTracking()) && (!paramKeyEvent.isCanceled()))
        {
          setVisible(false);
          return true;
        }
      }
      else
      {
        dismissControlsDelayed(ZOOM_CONTROLS_TIMEOUT);
      }
      return false;
    }
    Object localObject = mOwnerView.getViewRootImpl();
    if (localObject != null) {
      ((ViewRootImpl)localObject).dispatchInputEvent(paramKeyEvent);
    }
    return true;
  }
  
  private void onPostConfigurationChanged()
  {
    dismissControlsDelayed(ZOOM_CONTROLS_TIMEOUT);
    refreshPositioningVariables();
  }
  
  private void refreshPositioningVariables()
  {
    if (mOwnerView.getWindowToken() == null) {
      return;
    }
    int i = mOwnerView.getHeight();
    int j = mOwnerView.getWidth();
    i -= mContainer.getHeight();
    mOwnerView.getLocationOnScreen(mOwnerViewRawLocation);
    mContainerRawLocation[0] = mOwnerViewRawLocation[0];
    mContainerRawLocation[1] = (mOwnerViewRawLocation[1] + i);
    int[] arrayOfInt = mTempIntArray;
    mOwnerView.getLocationInWindow(arrayOfInt);
    mContainerLayoutParams.x = arrayOfInt[0];
    mContainerLayoutParams.width = j;
    mContainerLayoutParams.y = (arrayOfInt[1] + i);
    if (mIsVisible) {
      mWindowManager.updateViewLayout(mContainer, mContainerLayoutParams);
    }
  }
  
  private void setTouchTargetView(View paramView)
  {
    mTouchTargetView = paramView;
    if (paramView != null) {
      paramView.getLocationInWindow(mTouchTargetWindowLocation);
    }
  }
  
  public ViewGroup getContainer()
  {
    return mContainer;
  }
  
  public View getZoomControls()
  {
    return mControls;
  }
  
  public boolean isAutoDismissed()
  {
    return mAutoDismissControls;
  }
  
  public boolean isVisible()
  {
    return mIsVisible;
  }
  
  public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction();
    if (paramMotionEvent.getPointerCount() > 1) {
      return false;
    }
    if (mReleaseTouchListenerOnUp)
    {
      if ((i == 1) || (i == 3))
      {
        mOwnerView.setOnTouchListener(null);
        setTouchTargetView(null);
        mReleaseTouchListenerOnUp = false;
      }
      return true;
    }
    dismissControlsDelayed(ZOOM_CONTROLS_TIMEOUT);
    paramView = mTouchTargetView;
    if (i != 3) {
      switch (i)
      {
      default: 
        break;
      case 0: 
        paramView = findViewForTouch((int)paramMotionEvent.getRawX(), (int)paramMotionEvent.getRawY());
        setTouchTargetView(paramView);
        break;
      }
    } else {
      setTouchTargetView(null);
    }
    if (paramView != null)
    {
      int j = mContainerRawLocation[0];
      i = mTouchTargetWindowLocation[0];
      int k = mContainerRawLocation[1];
      int m = mTouchTargetWindowLocation[1];
      paramMotionEvent = MotionEvent.obtain(paramMotionEvent);
      paramMotionEvent.offsetLocation(mOwnerViewRawLocation[0] - (j + i), mOwnerViewRawLocation[1] - (k + m));
      float f1 = paramMotionEvent.getX();
      float f2 = paramMotionEvent.getY();
      if ((f1 < 0.0F) && (f1 > -20.0F)) {
        paramMotionEvent.offsetLocation(-f1, 0.0F);
      }
      if ((f2 < 0.0F) && (f2 > -20.0F)) {
        paramMotionEvent.offsetLocation(0.0F, -f2);
      }
      boolean bool = paramView.dispatchTouchEvent(paramMotionEvent);
      paramMotionEvent.recycle();
      return bool;
    }
    return false;
  }
  
  public void setAutoDismissed(boolean paramBoolean)
  {
    if (mAutoDismissControls == paramBoolean) {
      return;
    }
    mAutoDismissControls = paramBoolean;
  }
  
  public void setFocusable(boolean paramBoolean)
  {
    int i = mContainerLayoutParams.flags;
    WindowManager.LayoutParams localLayoutParams;
    if (paramBoolean)
    {
      localLayoutParams = mContainerLayoutParams;
      flags &= 0xFFFFFFF7;
    }
    else
    {
      localLayoutParams = mContainerLayoutParams;
      flags |= 0x8;
    }
    if ((mContainerLayoutParams.flags != i) && (mIsVisible)) {
      mWindowManager.updateViewLayout(mContainer, mContainerLayoutParams);
    }
  }
  
  public void setOnZoomListener(OnZoomListener paramOnZoomListener)
  {
    mCallback = paramOnZoomListener;
  }
  
  public void setVisible(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      if (mOwnerView.getWindowToken() == null)
      {
        if (!mHandler.hasMessages(4)) {
          mHandler.sendEmptyMessage(4);
        }
        return;
      }
      dismissControlsDelayed(ZOOM_CONTROLS_TIMEOUT);
    }
    if (mIsVisible == paramBoolean) {
      return;
    }
    mIsVisible = paramBoolean;
    if (paramBoolean)
    {
      if (mContainerLayoutParams.token == null) {
        mContainerLayoutParams.token = mOwnerView.getWindowToken();
      }
      mWindowManager.addView(mContainer, mContainerLayoutParams);
      if (mPostedVisibleInitializer == null) {
        mPostedVisibleInitializer = new Runnable()
        {
          public void run()
          {
            ZoomButtonsController.this.refreshPositioningVariables();
            if (mCallback != null) {
              mCallback.onVisibilityChanged(true);
            }
          }
        };
      }
      mHandler.post(mPostedVisibleInitializer);
      mContext.registerReceiver(mConfigurationChangedReceiver, mConfigurationChangedFilter);
      mOwnerView.setOnTouchListener(this);
      mReleaseTouchListenerOnUp = false;
    }
    else
    {
      if (mTouchTargetView != null) {
        mReleaseTouchListenerOnUp = true;
      } else {
        mOwnerView.setOnTouchListener(null);
      }
      mContext.unregisterReceiver(mConfigurationChangedReceiver);
      mWindowManager.removeViewImmediate(mContainer);
      mHandler.removeCallbacks(mPostedVisibleInitializer);
      if (mCallback != null) {
        mCallback.onVisibilityChanged(false);
      }
    }
  }
  
  public void setZoomInEnabled(boolean paramBoolean)
  {
    mControls.setIsZoomInEnabled(paramBoolean);
  }
  
  public void setZoomOutEnabled(boolean paramBoolean)
  {
    mControls.setIsZoomOutEnabled(paramBoolean);
  }
  
  public void setZoomSpeed(long paramLong)
  {
    mControls.setZoomSpeed(paramLong);
  }
  
  private class Container
    extends FrameLayout
  {
    public Container(Context paramContext)
    {
      super();
    }
    
    public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
    {
      boolean bool;
      if (ZoomButtonsController.this.onContainerKey(paramKeyEvent)) {
        bool = true;
      } else {
        bool = super.dispatchKeyEvent(paramKeyEvent);
      }
      return bool;
    }
  }
  
  public static abstract interface OnZoomListener
  {
    public abstract void onVisibilityChanged(boolean paramBoolean);
    
    public abstract void onZoom(boolean paramBoolean);
  }
}
