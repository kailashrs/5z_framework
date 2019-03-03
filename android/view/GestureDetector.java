package android.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class GestureDetector
{
  private static final int DOUBLE_TAP_MIN_TIME = ViewConfiguration.getDoubleTapMinTime();
  private static final int DOUBLE_TAP_TIMEOUT;
  private static final int LONGPRESS_TIMEOUT = ;
  private static final int LONG_PRESS = 2;
  private static final int SHOW_PRESS = 1;
  private static final int TAP = 3;
  private static final int TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
  private boolean mAlwaysInBiggerTapRegion;
  private boolean mAlwaysInTapRegion;
  private OnContextClickListener mContextClickListener;
  private MotionEvent mCurrentDownEvent;
  private boolean mDeferConfirmSingleTap;
  private OnDoubleTapListener mDoubleTapListener;
  private int mDoubleTapSlopSquare;
  private int mDoubleTapTouchSlopSquare;
  private float mDownFocusX;
  private float mDownFocusY;
  private final Handler mHandler;
  private boolean mIgnoreNextUpEvent;
  private boolean mInContextClick;
  private boolean mInLongPress;
  private final InputEventConsistencyVerifier mInputEventConsistencyVerifier;
  private boolean mIsDoubleTapping;
  private boolean mIsLongpressEnabled;
  private float mLastFocusX;
  private float mLastFocusY;
  private final OnGestureListener mListener;
  private int mMaximumFlingVelocity;
  private int mMinimumFlingVelocity;
  private MotionEvent mPreviousUpEvent;
  private boolean mStillDown;
  private int mTouchSlopSquare;
  private VelocityTracker mVelocityTracker;
  
  static
  {
    DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
  }
  
  public GestureDetector(Context paramContext, OnGestureListener paramOnGestureListener)
  {
    this(paramContext, paramOnGestureListener, null);
  }
  
  public GestureDetector(Context paramContext, OnGestureListener paramOnGestureListener, Handler paramHandler)
  {
    InputEventConsistencyVerifier localInputEventConsistencyVerifier;
    if (InputEventConsistencyVerifier.isInstrumentationEnabled()) {
      localInputEventConsistencyVerifier = new InputEventConsistencyVerifier(this, 0);
    } else {
      localInputEventConsistencyVerifier = null;
    }
    mInputEventConsistencyVerifier = localInputEventConsistencyVerifier;
    if (paramHandler != null) {
      mHandler = new GestureHandler(paramHandler);
    } else {
      mHandler = new GestureHandler();
    }
    mListener = paramOnGestureListener;
    if ((paramOnGestureListener instanceof OnDoubleTapListener)) {
      setOnDoubleTapListener((OnDoubleTapListener)paramOnGestureListener);
    }
    if ((paramOnGestureListener instanceof OnContextClickListener)) {
      setContextClickListener((OnContextClickListener)paramOnGestureListener);
    }
    init(paramContext);
  }
  
  public GestureDetector(Context paramContext, OnGestureListener paramOnGestureListener, Handler paramHandler, boolean paramBoolean)
  {
    this(paramContext, paramOnGestureListener, paramHandler);
  }
  
  @Deprecated
  public GestureDetector(OnGestureListener paramOnGestureListener)
  {
    this(null, paramOnGestureListener, null);
  }
  
  @Deprecated
  public GestureDetector(OnGestureListener paramOnGestureListener, Handler paramHandler)
  {
    this(null, paramOnGestureListener, paramHandler);
  }
  
  private void cancel()
  {
    mHandler.removeMessages(1);
    mHandler.removeMessages(2);
    mHandler.removeMessages(3);
    mVelocityTracker.recycle();
    mVelocityTracker = null;
    mIsDoubleTapping = false;
    mStillDown = false;
    mAlwaysInTapRegion = false;
    mAlwaysInBiggerTapRegion = false;
    mDeferConfirmSingleTap = false;
    mInLongPress = false;
    mInContextClick = false;
    mIgnoreNextUpEvent = false;
  }
  
  private void cancelTaps()
  {
    mHandler.removeMessages(1);
    mHandler.removeMessages(2);
    mHandler.removeMessages(3);
    mIsDoubleTapping = false;
    mAlwaysInTapRegion = false;
    mAlwaysInBiggerTapRegion = false;
    mDeferConfirmSingleTap = false;
    mInLongPress = false;
    mInContextClick = false;
    mIgnoreNextUpEvent = false;
  }
  
  private void dispatchLongPress()
  {
    mHandler.removeMessages(3);
    mDeferConfirmSingleTap = false;
    mInLongPress = true;
    mListener.onLongPress(mCurrentDownEvent);
  }
  
  private void init(Context paramContext)
  {
    if (mListener != null)
    {
      mIsLongpressEnabled = true;
      int i;
      int j;
      int k;
      if (paramContext == null)
      {
        i = ViewConfiguration.getTouchSlop();
        j = i;
        k = ViewConfiguration.getDoubleTapSlop();
        mMinimumFlingVelocity = ViewConfiguration.getMinimumFlingVelocity();
        mMaximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity();
      }
      else
      {
        paramContext = ViewConfiguration.get(paramContext);
        i = paramContext.getScaledTouchSlop();
        j = paramContext.getScaledDoubleTapTouchSlop();
        k = paramContext.getScaledDoubleTapSlop();
        mMinimumFlingVelocity = paramContext.getScaledMinimumFlingVelocity();
        mMaximumFlingVelocity = paramContext.getScaledMaximumFlingVelocity();
      }
      mTouchSlopSquare = (i * i);
      mDoubleTapTouchSlopSquare = (j * j);
      mDoubleTapSlopSquare = (k * k);
      return;
    }
    throw new NullPointerException("OnGestureListener must not be null");
  }
  
  private boolean isConsideredDoubleTap(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, MotionEvent paramMotionEvent3)
  {
    boolean bool1 = mAlwaysInBiggerTapRegion;
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    long l = paramMotionEvent3.getEventTime() - paramMotionEvent2.getEventTime();
    if ((l <= DOUBLE_TAP_TIMEOUT) && (l >= DOUBLE_TAP_MIN_TIME))
    {
      int i = (int)paramMotionEvent1.getX() - (int)paramMotionEvent3.getX();
      int j = (int)paramMotionEvent1.getY() - (int)paramMotionEvent3.getY();
      int k;
      if ((paramMotionEvent1.getFlags() & 0x8) != 0) {
        k = 1;
      } else {
        k = 0;
      }
      if (k != 0) {
        k = 0;
      } else {
        k = mDoubleTapSlopSquare;
      }
      if (i * i + j * j < k) {
        bool2 = true;
      }
      return bool2;
    }
    return false;
  }
  
  public boolean isLongpressEnabled()
  {
    return mIsLongpressEnabled;
  }
  
  public boolean onGenericMotionEvent(MotionEvent paramMotionEvent)
  {
    if (mInputEventConsistencyVerifier != null) {
      mInputEventConsistencyVerifier.onGenericMotionEvent(paramMotionEvent, 0);
    }
    int i = paramMotionEvent.getActionButton();
    switch (paramMotionEvent.getActionMasked())
    {
    default: 
      break;
    case 12: 
      if ((mInContextClick) && ((i == 32) || (i == 2)))
      {
        mInContextClick = false;
        mIgnoreNextUpEvent = true;
      }
      break;
    case 11: 
      if ((mContextClickListener != null) && (!mInContextClick) && (!mInLongPress) && ((i == 32) || (i == 2)) && (mContextClickListener.onContextClick(paramMotionEvent)))
      {
        mInContextClick = true;
        mHandler.removeMessages(2);
        mHandler.removeMessages(3);
        return true;
      }
      break;
    }
    return false;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (mInputEventConsistencyVerifier != null) {
      mInputEventConsistencyVerifier.onTouchEvent(paramMotionEvent, 0);
    }
    int i = paramMotionEvent.getAction();
    if (mVelocityTracker == null) {
      mVelocityTracker = VelocityTracker.obtain();
    }
    mVelocityTracker.addMovement(paramMotionEvent);
    int j;
    if ((i & 0xFF) == 6) {
      j = 1;
    } else {
      j = 0;
    }
    int k;
    if (j != 0) {
      k = paramMotionEvent.getActionIndex();
    } else {
      k = -1;
    }
    int m;
    if ((paramMotionEvent.getFlags() & 0x8) != 0) {
      m = 1;
    } else {
      m = 0;
    }
    int n = paramMotionEvent.getPointerCount();
    float f1 = 0.0F;
    float f2 = 0.0F;
    for (int i1 = 0; i1 < n; i1++) {
      if (k != i1)
      {
        f2 += paramMotionEvent.getX(i1);
        f1 += paramMotionEvent.getY(i1);
      }
    }
    if (j != 0) {
      i1 = n - 1;
    } else {
      i1 = n;
    }
    f2 /= i1;
    float f3 = f1 / i1;
    boolean bool2 = false;
    boolean bool3 = false;
    i1 = 0;
    boolean bool4 = false;
    boolean bool5 = false;
    float f4;
    switch (i & 0xFF)
    {
    case 4: 
    default: 
      break;
    case 6: 
      mLastFocusX = f2;
      mDownFocusX = f2;
      mLastFocusY = f3;
      mDownFocusY = f3;
      mVelocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
      i1 = paramMotionEvent.getActionIndex();
      m = paramMotionEvent.getPointerId(i1);
      f2 = mVelocityTracker.getXVelocity(m);
      f1 = mVelocityTracker.getYVelocity(m);
      for (i = 0; i < n; i++) {
        if (i != i1)
        {
          int i2 = paramMotionEvent.getPointerId(i);
          if (mVelocityTracker.getXVelocity(i2) * f2 + mVelocityTracker.getYVelocity(i2) * f1 < 0.0F)
          {
            mVelocityTracker.clear();
            break;
          }
        }
      }
      bool4 = bool5;
      break;
    case 5: 
      mLastFocusX = f2;
      mDownFocusX = f2;
      mLastFocusY = f3;
      mDownFocusY = f3;
      cancelTaps();
      bool4 = bool5;
      break;
    case 3: 
      cancel();
      bool4 = bool5;
      break;
    case 2: 
      bool4 = bool5;
      if (!mInLongPress) {
        if (mInContextClick)
        {
          bool4 = bool5;
        }
        else
        {
          f1 = mLastFocusX - f2;
          f4 = mLastFocusY - f3;
          if (!mIsDoubleTapping) {
            break label520;
          }
          bool4 = false | mDoubleTapListener.onDoubleTapEvent(paramMotionEvent);
        }
      }
      break;
    }
    for (;;)
    {
      break;
      label520:
      if (mAlwaysInTapRegion)
      {
        k = (int)(f2 - mDownFocusX);
        j = (int)(f3 - mDownFocusY);
        k = k * k + j * j;
        if (m != 0) {
          j = 0;
        } else {
          j = mTouchSlopSquare;
        }
        if (k > j)
        {
          bool4 = mListener.onScroll(mCurrentDownEvent, paramMotionEvent, f1, f4);
          mLastFocusX = f2;
          mLastFocusY = f3;
          mAlwaysInTapRegion = false;
          mHandler.removeMessages(3);
          mHandler.removeMessages(1);
          mHandler.removeMessages(2);
        }
        else
        {
          bool4 = bool2;
        }
        if (m != 0) {
          j = 0;
        } else {
          j = mDoubleTapTouchSlopSquare;
        }
        if (k > j) {
          mAlwaysInBiggerTapRegion = false;
        }
      }
      else if (Math.abs(f1) < 1.0F)
      {
        bool4 = bool5;
        if (Math.abs(f4) < 1.0F) {
          break;
        }
      }
      else
      {
        bool4 = mListener.onScroll(mCurrentDownEvent, paramMotionEvent, f1, f4);
        mLastFocusX = f2;
        mLastFocusY = f3;
        continue;
        mStillDown = false;
        MotionEvent localMotionEvent = MotionEvent.obtain(paramMotionEvent);
        if (mIsDoubleTapping)
        {
          bool4 = false | mDoubleTapListener.onDoubleTapEvent(paramMotionEvent);
        }
        else if (mInLongPress)
        {
          mHandler.removeMessages(3);
          mInLongPress = false;
          bool4 = bool3;
        }
        else if ((mAlwaysInTapRegion) && (!mIgnoreNextUpEvent))
        {
          bool5 = mListener.onSingleTapUp(paramMotionEvent);
          bool4 = bool5;
          if (mDeferConfirmSingleTap)
          {
            bool4 = bool5;
            if (mDoubleTapListener != null)
            {
              mDoubleTapListener.onSingleTapConfirmed(paramMotionEvent);
              bool4 = bool5;
            }
          }
        }
        else
        {
          bool4 = bool3;
          if (!mIgnoreNextUpEvent)
          {
            VelocityTracker localVelocityTracker = mVelocityTracker;
            j = paramMotionEvent.getPointerId(0);
            localVelocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
            f2 = localVelocityTracker.getYVelocity(j);
            f1 = localVelocityTracker.getXVelocity(j);
            if (Math.abs(f2) <= mMinimumFlingVelocity)
            {
              bool4 = bool3;
              if (Math.abs(f1) <= mMinimumFlingVelocity) {}
            }
            else
            {
              bool4 = mListener.onFling(mCurrentDownEvent, paramMotionEvent, f1, f2);
            }
          }
        }
        if (mPreviousUpEvent != null) {
          mPreviousUpEvent.recycle();
        }
        mPreviousUpEvent = localMotionEvent;
        if (mVelocityTracker != null)
        {
          mVelocityTracker.recycle();
          mVelocityTracker = null;
        }
        mIsDoubleTapping = false;
        mDeferConfirmSingleTap = false;
        mIgnoreNextUpEvent = false;
        mHandler.removeMessages(1);
        mHandler.removeMessages(2);
        continue;
        j = i1;
        boolean bool1;
        if (mDoubleTapListener != null)
        {
          bool4 = mHandler.hasMessages(3);
          if (bool4) {
            mHandler.removeMessages(3);
          }
          if ((mCurrentDownEvent != null) && (mPreviousUpEvent != null) && (bool4) && (isConsideredDoubleTap(mCurrentDownEvent, mPreviousUpEvent, paramMotionEvent)))
          {
            mIsDoubleTapping = true;
            bool1 = mDoubleTapListener.onDoubleTap(mCurrentDownEvent) | false | mDoubleTapListener.onDoubleTapEvent(paramMotionEvent);
          }
          else
          {
            mHandler.sendEmptyMessageDelayed(3, DOUBLE_TAP_TIMEOUT);
            bool1 = i1;
          }
        }
        mLastFocusX = f2;
        mDownFocusX = f2;
        mLastFocusY = f3;
        mDownFocusY = f3;
        if (mCurrentDownEvent != null) {
          mCurrentDownEvent.recycle();
        }
        mCurrentDownEvent = MotionEvent.obtain(paramMotionEvent);
        mAlwaysInTapRegion = true;
        mAlwaysInBiggerTapRegion = true;
        mStillDown = true;
        mInLongPress = false;
        mDeferConfirmSingleTap = false;
        if (mIsLongpressEnabled)
        {
          mHandler.removeMessages(2);
          mHandler.sendEmptyMessageAtTime(2, mCurrentDownEvent.getDownTime() + LONGPRESS_TIMEOUT);
        }
        mHandler.sendEmptyMessageAtTime(1, mCurrentDownEvent.getDownTime() + TAP_TIMEOUT);
        bool4 = bool1 | mListener.onDown(paramMotionEvent);
      }
    }
    if ((!bool4) && (mInputEventConsistencyVerifier != null)) {
      mInputEventConsistencyVerifier.onUnhandledEvent(paramMotionEvent, 0);
    }
    return bool4;
  }
  
  public void setContextClickListener(OnContextClickListener paramOnContextClickListener)
  {
    mContextClickListener = paramOnContextClickListener;
  }
  
  public void setIsLongpressEnabled(boolean paramBoolean)
  {
    mIsLongpressEnabled = paramBoolean;
  }
  
  public void setOnDoubleTapListener(OnDoubleTapListener paramOnDoubleTapListener)
  {
    mDoubleTapListener = paramOnDoubleTapListener;
  }
  
  private class GestureHandler
    extends Handler
  {
    GestureHandler() {}
    
    GestureHandler(Handler paramHandler)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      switch (what)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unknown message ");
        localStringBuilder.append(paramMessage);
        throw new RuntimeException(localStringBuilder.toString());
      case 3: 
        if (mDoubleTapListener != null) {
          if (!mStillDown) {
            mDoubleTapListener.onSingleTapConfirmed(mCurrentDownEvent);
          } else {
            GestureDetector.access$502(GestureDetector.this, true);
          }
        }
        break;
      case 2: 
        GestureDetector.this.dispatchLongPress();
        break;
      case 1: 
        mListener.onShowPress(mCurrentDownEvent);
      }
    }
  }
  
  public static abstract interface OnContextClickListener
  {
    public abstract boolean onContextClick(MotionEvent paramMotionEvent);
  }
  
  public static abstract interface OnDoubleTapListener
  {
    public abstract boolean onDoubleTap(MotionEvent paramMotionEvent);
    
    public abstract boolean onDoubleTapEvent(MotionEvent paramMotionEvent);
    
    public abstract boolean onSingleTapConfirmed(MotionEvent paramMotionEvent);
  }
  
  public static abstract interface OnGestureListener
  {
    public abstract boolean onDown(MotionEvent paramMotionEvent);
    
    public abstract boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2);
    
    public abstract void onLongPress(MotionEvent paramMotionEvent);
    
    public abstract boolean onScroll(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2);
    
    public abstract void onShowPress(MotionEvent paramMotionEvent);
    
    public abstract boolean onSingleTapUp(MotionEvent paramMotionEvent);
  }
  
  public static class SimpleOnGestureListener
    implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, GestureDetector.OnContextClickListener
  {
    public SimpleOnGestureListener() {}
    
    public boolean onContextClick(MotionEvent paramMotionEvent)
    {
      return false;
    }
    
    public boolean onDoubleTap(MotionEvent paramMotionEvent)
    {
      return false;
    }
    
    public boolean onDoubleTapEvent(MotionEvent paramMotionEvent)
    {
      return false;
    }
    
    public boolean onDown(MotionEvent paramMotionEvent)
    {
      return false;
    }
    
    public boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
    {
      return false;
    }
    
    public void onLongPress(MotionEvent paramMotionEvent) {}
    
    public boolean onScroll(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
    {
      return false;
    }
    
    public void onShowPress(MotionEvent paramMotionEvent) {}
    
    public boolean onSingleTapConfirmed(MotionEvent paramMotionEvent)
    {
      return false;
    }
    
    public boolean onSingleTapUp(MotionEvent paramMotionEvent)
    {
      return false;
    }
  }
}
