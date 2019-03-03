package android.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Point;
import android.hardware.display.DisplayManagerGlobal;
import android.os.Handler;
import android.os.Message;
import android.util.Slog;

public class AsusSwipeGestureDetector
{
  private static boolean DEBUG = false;
  private static final int MAX_TRACKED_POINTERS = 5;
  private static final int MSG_UPDATE_SCREEN_SIZE = 10020;
  private static final int SWIPE_FROM_BOTTOM = 2;
  private static final int SWIPE_FROM_LEFT = 4;
  private static final int SWIPE_FROM_RIGHT = 3;
  private static final int SWIPE_FROM_TOP = 1;
  private static final int SWIPE_NONE = 0;
  private static final long SWIPE_TIMEOUT = 500L;
  private static final String TAG = "AsusSwipeGestureDetector";
  private static final int UNTRACKED_POINTER = -1;
  private Context mContext;
  private final int[] mDownPointerId = new int[5];
  private int mDownPointers;
  private final long[] mDownTime = new long[5];
  private final float[] mDownX = new float[5];
  private final float[] mDownY = new float[5];
  private SwipeHandler mHandler;
  private OnSwipeGestureListener mListener;
  private LocalReceiver mReceiver;
  private int mScreenHeight;
  private int mScreenWidth;
  private final int mSwipeDistanceThreshold;
  private boolean mSwipeFireable;
  private final int mSwipeStartThreshold;
  
  public AsusSwipeGestureDetector(Context paramContext, OnSwipeGestureListener paramOnSwipeGestureListener, Handler paramHandler)
  {
    mContext = paramContext;
    mListener = paramOnSwipeGestureListener;
    if (paramHandler != null) {
      mHandler = new SwipeHandler(paramHandler);
    } else {
      mHandler = new SwipeHandler();
    }
    mSwipeStartThreshold = paramContext.getResources().getDimensionPixelSize(17105427);
    mSwipeDistanceThreshold = mSwipeStartThreshold;
    paramOnSwipeGestureListener = new IntentFilter("android.intent.action.CONFIGURATION_CHANGED");
    mReceiver = new LocalReceiver(mHandler);
    paramContext.registerReceiver(mReceiver, paramOnSwipeGestureListener);
    updateScreenSize();
  }
  
  private void captureDown(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getActionIndex();
    int j = paramMotionEvent.getPointerId(i);
    int k = findIndex(j);
    if (DEBUG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("pointer ");
      localStringBuilder.append(j);
      localStringBuilder.append(" down pointerIndex=");
      localStringBuilder.append(i);
      localStringBuilder.append(" trackingIndex=");
      localStringBuilder.append(k);
      Slog.d("AsusSwipeGestureDetector", localStringBuilder.toString());
    }
    if (k != -1)
    {
      mDownX[k] = paramMotionEvent.getX(i);
      mDownY[k] = paramMotionEvent.getY(i);
      mDownTime[k] = paramMotionEvent.getEventTime();
      if (DEBUG)
      {
        paramMotionEvent = new StringBuilder();
        paramMotionEvent.append("pointer ");
        paramMotionEvent.append(j);
        paramMotionEvent.append(" down x=");
        paramMotionEvent.append(mDownX[k]);
        paramMotionEvent.append(" y=");
        paramMotionEvent.append(mDownY[k]);
        Slog.d("AsusSwipeGestureDetector", paramMotionEvent.toString());
      }
    }
  }
  
  private int detectSwipe(int paramInt, long paramLong, float paramFloat1, float paramFloat2)
  {
    float f1 = mDownX[paramInt];
    float f2 = mDownY[paramInt];
    paramLong -= mDownTime[paramInt];
    if (DEBUG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("pointer ");
      localStringBuilder.append(mDownPointerId[paramInt]);
      localStringBuilder.append(" moved (");
      localStringBuilder.append(f1);
      localStringBuilder.append("->");
      localStringBuilder.append(paramFloat1);
      localStringBuilder.append(",");
      localStringBuilder.append(f2);
      localStringBuilder.append("->");
      localStringBuilder.append(paramFloat2);
      localStringBuilder.append(") in ");
      localStringBuilder.append(paramLong);
      Slog.d("AsusSwipeGestureDetector", localStringBuilder.toString());
    }
    if ((f2 <= mSwipeStartThreshold) && (paramFloat2 > mSwipeDistanceThreshold + f2) && (paramLong < 500L)) {
      return 1;
    }
    if ((f2 >= mScreenHeight - mSwipeStartThreshold) && (paramFloat2 < f2 - mSwipeDistanceThreshold) && (paramLong < 500L)) {
      return 2;
    }
    if ((f1 >= mScreenWidth - mSwipeStartThreshold) && (paramFloat1 < f1 - mSwipeDistanceThreshold) && (paramLong < 500L)) {
      return 3;
    }
    if ((f1 <= mSwipeStartThreshold) && (paramFloat1 > mSwipeDistanceThreshold + f1) && (paramLong < 500L)) {
      return 4;
    }
    return 0;
  }
  
  private int detectSwipe(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getHistorySize();
    int j = paramMotionEvent.getPointerCount();
    for (int k = 0; k < j; k++)
    {
      int m = findIndex(paramMotionEvent.getPointerId(k));
      if (m != -1)
      {
        for (int n = 0; n < i; n++)
        {
          int i1 = detectSwipe(m, paramMotionEvent.getHistoricalEventTime(n), paramMotionEvent.getHistoricalX(k, n), paramMotionEvent.getHistoricalY(k, n));
          if (i1 != 0) {
            return i1;
          }
        }
        n = detectSwipe(m, paramMotionEvent.getEventTime(), paramMotionEvent.getX(k), paramMotionEvent.getY(k));
        if (n != 0) {
          return n;
        }
      }
    }
    return 0;
  }
  
  private int findIndex(int paramInt)
  {
    for (int i = 0; i < mDownPointers; i++) {
      if (mDownPointerId[i] == paramInt) {
        return i;
      }
    }
    if ((mDownPointers != 5) && (paramInt != -1))
    {
      int[] arrayOfInt = mDownPointerId;
      i = mDownPointers;
      mDownPointers = (i + 1);
      arrayOfInt[i] = paramInt;
      return mDownPointers - 1;
    }
    return -1;
  }
  
  private void updateScreenSize()
  {
    Object localObject = DisplayManagerGlobal.getInstance().getRealDisplay(0);
    Point localPoint = new Point();
    ((Display)localObject).getRealSize(localPoint);
    mScreenWidth = x;
    mScreenHeight = y;
    if (DEBUG)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Screen width = ");
      ((StringBuilder)localObject).append(mScreenWidth);
      ((StringBuilder)localObject).append(" ; height = ");
      ((StringBuilder)localObject).append(mScreenHeight);
      Slog.d("AsusSwipeGestureDetector", ((StringBuilder)localObject).toString());
    }
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool = false;
    if (paramMotionEvent == null) {
      return false;
    }
    StringBuilder localStringBuilder;
    switch (paramMotionEvent.getActionMasked())
    {
    case 3: 
    case 4: 
    default: 
      break;
    case 6: 
      if (DEBUG) {
        Slog.d("AsusSwipeGestureDetector", "ACTION_POINTER_UP");
      }
      break;
    case 5: 
      if (DEBUG) {
        Slog.d("AsusSwipeGestureDetector", "ACTION_POINTER_DOWN");
      }
      captureDown(paramMotionEvent);
      break;
    case 2: 
      if (DEBUG) {
        Slog.d("AsusSwipeGestureDetector", "ACTION_MOVE");
      }
      if (mSwipeFireable)
      {
        int i = detectSwipe(paramMotionEvent);
        if (i == 0) {
          bool = true;
        }
        mSwipeFireable = bool;
        if (i == 1)
        {
          if (DEBUG) {
            Slog.d("AsusSwipeGestureDetector", "onSwipeFromTop");
          }
          mListener.onSwipeFromTop();
        }
        else if (i == 2)
        {
          if (DEBUG) {
            Slog.d("AsusSwipeGestureDetector", "onSwipeFromBottom");
          }
          mListener.onSwipeFromBottom();
        }
        else if (i == 4)
        {
          if (DEBUG) {
            Slog.d("AsusSwipeGestureDetector", "onSwipeFromLeft");
          }
          mListener.onSwipeFromLeft();
        }
        else if (i == 3)
        {
          if (DEBUG) {
            Slog.d("AsusSwipeGestureDetector", "onSwipeFromRight");
          }
          mListener.onSwipeFromRight();
        }
      }
      break;
    case 1: 
      if (DEBUG)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("ACTION_UP: event = ");
        localStringBuilder.append(paramMotionEvent);
        Slog.d("AsusSwipeGestureDetector", localStringBuilder.toString());
      }
      mSwipeFireable = false;
      break;
    case 0: 
      if (DEBUG)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("ACTION_DOWN: event = ");
        localStringBuilder.append(paramMotionEvent);
        Slog.d("AsusSwipeGestureDetector", localStringBuilder.toString());
      }
      mSwipeFireable = true;
      captureDown(paramMotionEvent);
    }
    return true;
  }
  
  private class LocalReceiver
    extends BroadcastReceiver
  {
    private Handler mHandler;
    
    public LocalReceiver(Handler paramHandler)
    {
      mHandler = paramHandler;
    }
    
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ("android.intent.action.CONFIGURATION_CHANGED".equals(paramIntent.getAction()))
      {
        mHandler.removeMessages(10020);
        mHandler.sendMessage(Message.obtain(mHandler, 10020));
      }
    }
  }
  
  public static abstract interface OnSwipeGestureListener
  {
    public abstract void onSwipeFromBottom();
    
    public abstract void onSwipeFromLeft();
    
    public abstract void onSwipeFromRight();
    
    public abstract void onSwipeFromTop();
  }
  
  public static class SimpleOnSwipeGestureListener
    implements AsusSwipeGestureDetector.OnSwipeGestureListener
  {
    public SimpleOnSwipeGestureListener() {}
    
    public void onSwipeFromBottom() {}
    
    public void onSwipeFromLeft() {}
    
    public void onSwipeFromRight() {}
    
    public void onSwipeFromTop() {}
  }
  
  private class SwipeHandler
    extends Handler
  {
    public SwipeHandler() {}
    
    public SwipeHandler(Handler paramHandler)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      if (what == 10020) {
        AsusSwipeGestureDetector.this.updateScreenSize();
      }
    }
  }
}
