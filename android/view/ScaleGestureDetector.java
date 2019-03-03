package android.view;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.os.Handler;

public class ScaleGestureDetector
{
  private static final int ANCHORED_SCALE_MODE_DOUBLE_TAP = 1;
  private static final int ANCHORED_SCALE_MODE_NONE = 0;
  private static final int ANCHORED_SCALE_MODE_STYLUS = 2;
  private static final float SCALE_FACTOR = 0.5F;
  private static final String TAG = "ScaleGestureDetector";
  private static final long TOUCH_STABILIZE_TIME = 128L;
  private int mAnchoredScaleMode = 0;
  private float mAnchoredScaleStartX;
  private float mAnchoredScaleStartY;
  private final Context mContext;
  private float mCurrSpan;
  private float mCurrSpanX;
  private float mCurrSpanY;
  private long mCurrTime;
  private boolean mEventBeforeOrAboveStartingGestureEvent;
  private float mFocusX;
  private float mFocusY;
  private GestureDetector mGestureDetector;
  private final Handler mHandler;
  private boolean mInProgress;
  private float mInitialSpan;
  private final InputEventConsistencyVerifier mInputEventConsistencyVerifier;
  private final OnScaleGestureListener mListener;
  private int mMinSpan;
  private float mPrevSpan;
  private float mPrevSpanX;
  private float mPrevSpanY;
  private long mPrevTime;
  private boolean mQuickScaleEnabled;
  private int mSpanSlop;
  private boolean mStylusScaleEnabled;
  
  public ScaleGestureDetector(Context paramContext, OnScaleGestureListener paramOnScaleGestureListener)
  {
    this(paramContext, paramOnScaleGestureListener, null);
  }
  
  public ScaleGestureDetector(Context paramContext, OnScaleGestureListener paramOnScaleGestureListener, Handler paramHandler)
  {
    InputEventConsistencyVerifier localInputEventConsistencyVerifier;
    if (InputEventConsistencyVerifier.isInstrumentationEnabled()) {
      localInputEventConsistencyVerifier = new InputEventConsistencyVerifier(this, 0);
    } else {
      localInputEventConsistencyVerifier = null;
    }
    mInputEventConsistencyVerifier = localInputEventConsistencyVerifier;
    mContext = paramContext;
    mListener = paramOnScaleGestureListener;
    mSpanSlop = (ViewConfiguration.get(paramContext).getScaledTouchSlop() * 2);
    mMinSpan = paramContext.getResources().getDimensionPixelSize(17105080);
    mHandler = paramHandler;
    int i = getApplicationInfotargetSdkVersion;
    if (i > 18) {
      setQuickScaleEnabled(true);
    }
    if (i > 22) {
      setStylusScaleEnabled(true);
    }
  }
  
  private boolean inAnchoredScaleMode()
  {
    boolean bool;
    if (mAnchoredScaleMode != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public float getCurrentSpan()
  {
    return mCurrSpan;
  }
  
  public float getCurrentSpanX()
  {
    return mCurrSpanX;
  }
  
  public float getCurrentSpanY()
  {
    return mCurrSpanY;
  }
  
  public long getEventTime()
  {
    return mCurrTime;
  }
  
  public float getFocusX()
  {
    return mFocusX;
  }
  
  public float getFocusY()
  {
    return mFocusY;
  }
  
  public float getPreviousSpan()
  {
    return mPrevSpan;
  }
  
  public float getPreviousSpanX()
  {
    return mPrevSpanX;
  }
  
  public float getPreviousSpanY()
  {
    return mPrevSpanY;
  }
  
  public float getScaleFactor()
  {
    boolean bool = inAnchoredScaleMode();
    float f1 = 1.0F;
    if (bool)
    {
      int i;
      if (((mEventBeforeOrAboveStartingGestureEvent) && (mCurrSpan < mPrevSpan)) || ((!mEventBeforeOrAboveStartingGestureEvent) && (mCurrSpan > mPrevSpan))) {
        i = 1;
      } else {
        i = 0;
      }
      float f2 = Math.abs(1.0F - mCurrSpan / mPrevSpan) * 0.5F;
      if (mPrevSpan > 0.0F) {
        if (i != 0) {
          f1 = 1.0F + f2;
        } else {
          f1 = 1.0F - f2;
        }
      }
      return f1;
    }
    if (mPrevSpan > 0.0F) {
      f1 = mCurrSpan / mPrevSpan;
    }
    return f1;
  }
  
  public long getTimeDelta()
  {
    return mCurrTime - mPrevTime;
  }
  
  public boolean isInProgress()
  {
    return mInProgress;
  }
  
  public boolean isQuickScaleEnabled()
  {
    return mQuickScaleEnabled;
  }
  
  public boolean isStylusScaleEnabled()
  {
    return mStylusScaleEnabled;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (mInputEventConsistencyVerifier != null) {
      mInputEventConsistencyVerifier.onTouchEvent(paramMotionEvent, 0);
    }
    mCurrTime = paramMotionEvent.getEventTime();
    int i = paramMotionEvent.getActionMasked();
    if (mQuickScaleEnabled) {
      mGestureDetector.onTouchEvent(paramMotionEvent);
    }
    int j = paramMotionEvent.getPointerCount();
    int k;
    if ((paramMotionEvent.getButtonState() & 0x20) != 0) {
      k = 1;
    } else {
      k = 0;
    }
    int m;
    if ((mAnchoredScaleMode == 2) && (k == 0)) {
      m = 1;
    } else {
      m = 0;
    }
    int n;
    if ((i != 1) && (i != 3) && (m == 0)) {
      n = 0;
    } else {
      n = 1;
    }
    if ((i == 0) || (n != 0))
    {
      if (mInProgress)
      {
        mListener.onScaleEnd(this);
        mInProgress = false;
        mInitialSpan = 0.0F;
        mAnchoredScaleMode = 0;
      }
      else if ((inAnchoredScaleMode()) && (n != 0))
      {
        mInProgress = false;
        mInitialSpan = 0.0F;
        mAnchoredScaleMode = 0;
      }
      if (n != 0) {
        return true;
      }
    }
    if ((!mInProgress) && (mStylusScaleEnabled) && (!inAnchoredScaleMode()) && (n == 0) && (k != 0))
    {
      mAnchoredScaleStartX = paramMotionEvent.getX();
      mAnchoredScaleStartY = paramMotionEvent.getY();
      mAnchoredScaleMode = 2;
      mInitialSpan = 0.0F;
    }
    if ((i != 0) && (i != 6) && (i != 5) && (m == 0)) {
      k = 0;
    } else {
      k = 1;
    }
    if (i == 6) {
      n = 1;
    } else {
      n = 0;
    }
    if (n != 0) {
      m = paramMotionEvent.getActionIndex();
    } else {
      m = -1;
    }
    float f1 = 0.0F;
    float f2 = 0.0F;
    if (n != 0) {
      n = j - 1;
    } else {
      n = j;
    }
    if (inAnchoredScaleMode())
    {
      f1 = mAnchoredScaleStartX;
      f2 = mAnchoredScaleStartY;
      if (paramMotionEvent.getY() < f2) {
        mEventBeforeOrAboveStartingGestureEvent = true;
      } else {
        mEventBeforeOrAboveStartingGestureEvent = false;
      }
    }
    else
    {
      for (i1 = 0; i1 < j; i1++) {
        if (m != i1)
        {
          f1 += paramMotionEvent.getX(i1);
          f2 += paramMotionEvent.getY(i1);
        }
      }
      f1 /= n;
      f2 /= n;
    }
    float f3 = 0.0F;
    float f4 = 0.0F;
    for (int i1 = 0; i1 < j; i1++) {
      if (m != i1)
      {
        f4 += Math.abs(paramMotionEvent.getX(i1) - f1);
        f3 += Math.abs(paramMotionEvent.getY(i1) - f2);
      }
    }
    f4 /= n;
    f3 /= n;
    float f5 = f4 * 2.0F;
    f4 = f3 * 2.0F;
    if (inAnchoredScaleMode()) {
      f3 = f4;
    } else {
      f3 = (float)Math.hypot(f5, f4);
    }
    boolean bool = mInProgress;
    mFocusX = f1;
    mFocusY = f2;
    if ((!inAnchoredScaleMode()) && (mInProgress) && ((f3 < mMinSpan) || (k != 0)))
    {
      mListener.onScaleEnd(this);
      mInProgress = false;
      mInitialSpan = f3;
    }
    if (k != 0)
    {
      mCurrSpanX = f5;
      mPrevSpanX = f5;
      mCurrSpanY = f4;
      mPrevSpanY = f4;
      mCurrSpan = f3;
      mPrevSpan = f3;
      mInitialSpan = f3;
    }
    if (inAnchoredScaleMode()) {
      j = mSpanSlop;
    } else {
      j = mMinSpan;
    }
    if ((!mInProgress) && (f3 >= j))
    {
      if ((!bool) && (Math.abs(f3 - mInitialSpan) <= mSpanSlop)) {
        break label808;
      }
      mCurrSpanX = f5;
      mPrevSpanX = f5;
      mCurrSpanY = f4;
      mPrevSpanY = f4;
      mCurrSpan = f3;
      mPrevSpan = f3;
      mPrevTime = mCurrTime;
      mInProgress = mListener.onScaleBegin(this);
    }
    label808:
    if (i == 2)
    {
      mCurrSpanX = f5;
      mCurrSpanY = f4;
      mCurrSpan = f3;
      bool = true;
      if (mInProgress) {
        bool = mListener.onScale(this);
      }
      if (bool)
      {
        mPrevSpanX = mCurrSpanX;
        mPrevSpanY = mCurrSpanY;
        mPrevSpan = mCurrSpan;
        mPrevTime = mCurrTime;
      }
    }
    return true;
  }
  
  public void setQuickScaleEnabled(boolean paramBoolean)
  {
    mQuickScaleEnabled = paramBoolean;
    if ((mQuickScaleEnabled) && (mGestureDetector == null))
    {
      GestureDetector.SimpleOnGestureListener local1 = new GestureDetector.SimpleOnGestureListener()
      {
        public boolean onDoubleTap(MotionEvent paramAnonymousMotionEvent)
        {
          ScaleGestureDetector.access$002(ScaleGestureDetector.this, paramAnonymousMotionEvent.getX());
          ScaleGestureDetector.access$102(ScaleGestureDetector.this, paramAnonymousMotionEvent.getY());
          ScaleGestureDetector.access$202(ScaleGestureDetector.this, 1);
          return true;
        }
      };
      mGestureDetector = new GestureDetector(mContext, local1, mHandler);
    }
  }
  
  public void setStylusScaleEnabled(boolean paramBoolean)
  {
    mStylusScaleEnabled = paramBoolean;
  }
  
  public static abstract interface OnScaleGestureListener
  {
    public abstract boolean onScale(ScaleGestureDetector paramScaleGestureDetector);
    
    public abstract boolean onScaleBegin(ScaleGestureDetector paramScaleGestureDetector);
    
    public abstract void onScaleEnd(ScaleGestureDetector paramScaleGestureDetector);
  }
  
  public static class SimpleOnScaleGestureListener
    implements ScaleGestureDetector.OnScaleGestureListener
  {
    public SimpleOnScaleGestureListener() {}
    
    public boolean onScale(ScaleGestureDetector paramScaleGestureDetector)
    {
      return false;
    }
    
    public boolean onScaleBegin(ScaleGestureDetector paramScaleGestureDetector)
    {
      return true;
    }
    
    public void onScaleEnd(ScaleGestureDetector paramScaleGestureDetector) {}
  }
}
