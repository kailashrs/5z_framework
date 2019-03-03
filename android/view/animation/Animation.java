package android.view.animation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Handler;
import android.os.SystemProperties;
import android.util.AttributeSet;
import android.util.TypedValue;
import com.android.internal.R.styleable;
import dalvik.system.CloseGuard;

public abstract class Animation
  implements Cloneable
{
  public static final int ABSOLUTE = 0;
  public static final int INFINITE = -1;
  public static final int RELATIVE_TO_PARENT = 2;
  public static final int RELATIVE_TO_SELF = 1;
  public static final int RESTART = 1;
  public static final int REVERSE = 2;
  public static final int START_ON_FIRST_FRAME = -1;
  public static final int ZORDER_BOTTOM = -1;
  public static final int ZORDER_NORMAL = 0;
  public static final int ZORDER_TOP = 1;
  private final CloseGuard guard = CloseGuard.get();
  private int mBackgroundColor;
  boolean mCycleFlip = false;
  private boolean mDetachWallpaper = false;
  long mDuration;
  boolean mEnded = false;
  boolean mFillAfter = false;
  boolean mFillBefore = true;
  boolean mFillEnabled = false;
  boolean mInitialized = false;
  Interpolator mInterpolator;
  AnimationListener mListener;
  private Handler mListenerHandler;
  private boolean mMore = true;
  private Runnable mOnEnd;
  private Runnable mOnRepeat;
  private Runnable mOnStart;
  private boolean mOneMoreTime = true;
  RectF mPreviousRegion = new RectF();
  Transformation mPreviousTransformation = new Transformation();
  RectF mRegion = new RectF();
  int mRepeatCount = 0;
  int mRepeatMode = 1;
  int mRepeated = 0;
  private float mScaleFactor = 1.0F;
  private boolean mShowWallpaper;
  long mStartOffset;
  long mStartTime = -1L;
  boolean mStarted = false;
  Transformation mTransformation = new Transformation();
  private int mZAdjustment;
  
  public Animation()
  {
    ensureInterpolator();
  }
  
  public Animation(Context paramContext, AttributeSet paramAttributeSet)
  {
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.Animation);
    setDuration(paramAttributeSet.getInt(2, 0));
    setStartOffset(paramAttributeSet.getInt(5, 0));
    setFillEnabled(paramAttributeSet.getBoolean(9, mFillEnabled));
    setFillBefore(paramAttributeSet.getBoolean(3, mFillBefore));
    setFillAfter(paramAttributeSet.getBoolean(4, mFillAfter));
    setRepeatCount(paramAttributeSet.getInt(6, mRepeatCount));
    setRepeatMode(paramAttributeSet.getInt(7, 1));
    setZAdjustment(paramAttributeSet.getInt(8, 0));
    setBackgroundColor(paramAttributeSet.getInt(0, 0));
    setDetachWallpaper(paramAttributeSet.getBoolean(10, false));
    setShowWallpaper(paramAttributeSet.getBoolean(11, false));
    int i = paramAttributeSet.getResourceId(1, 0);
    paramAttributeSet.recycle();
    if (i > 0) {
      setInterpolator(paramContext, i);
    }
    ensureInterpolator();
  }
  
  private void fireAnimationEnd()
  {
    if (mListener != null) {
      if (mListenerHandler == null) {
        mListener.onAnimationEnd(this);
      } else {
        mListenerHandler.postAtFrontOfQueue(mOnEnd);
      }
    }
  }
  
  private void fireAnimationRepeat()
  {
    if (mListener != null) {
      if (mListenerHandler == null) {
        mListener.onAnimationRepeat(this);
      } else {
        mListenerHandler.postAtFrontOfQueue(mOnRepeat);
      }
    }
  }
  
  private void fireAnimationStart()
  {
    if (mListener != null) {
      if (mListenerHandler == null) {
        mListener.onAnimationStart(this);
      } else {
        mListenerHandler.postAtFrontOfQueue(mOnStart);
      }
    }
  }
  
  private boolean isCanceled()
  {
    boolean bool;
    if (mStartTime == Long.MIN_VALUE) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected void applyTransformation(float paramFloat, Transformation paramTransformation) {}
  
  public void cancel()
  {
    if ((mStarted) && (!mEnded))
    {
      fireAnimationEnd();
      mEnded = true;
      guard.close();
    }
    mStartTime = Long.MIN_VALUE;
    mOneMoreTime = false;
    mMore = false;
  }
  
  protected Animation clone()
    throws CloneNotSupportedException
  {
    Animation localAnimation = (Animation)super.clone();
    mPreviousRegion = new RectF();
    mRegion = new RectF();
    mTransformation = new Transformation();
    mPreviousTransformation = new Transformation();
    return localAnimation;
  }
  
  public long computeDurationHint()
  {
    return (getStartOffset() + getDuration()) * (getRepeatCount() + 1);
  }
  
  public void detach()
  {
    if ((mStarted) && (!mEnded))
    {
      mEnded = true;
      guard.close();
      fireAnimationEnd();
    }
  }
  
  protected void ensureInterpolator()
  {
    if (mInterpolator == null) {
      mInterpolator = new AccelerateDecelerateInterpolator();
    }
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (guard != null) {
        guard.warnIfOpen();
      }
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public int getBackgroundColor()
  {
    return mBackgroundColor;
  }
  
  public boolean getDetachWallpaper()
  {
    return mDetachWallpaper;
  }
  
  public long getDuration()
  {
    return mDuration;
  }
  
  public boolean getFillAfter()
  {
    return mFillAfter;
  }
  
  public boolean getFillBefore()
  {
    return mFillBefore;
  }
  
  public Interpolator getInterpolator()
  {
    return mInterpolator;
  }
  
  public void getInvalidateRegion(int paramInt1, int paramInt2, int paramInt3, int paramInt4, RectF paramRectF, Transformation paramTransformation)
  {
    RectF localRectF = mRegion;
    Object localObject = mPreviousRegion;
    paramRectF.set(paramInt1, paramInt2, paramInt3, paramInt4);
    paramTransformation.getMatrix().mapRect(paramRectF);
    paramRectF.inset(-1.0F, -1.0F);
    localRectF.set(paramRectF);
    paramRectF.union((RectF)localObject);
    ((RectF)localObject).set(localRectF);
    localObject = mTransformation;
    paramRectF = mPreviousTransformation;
    ((Transformation)localObject).set(paramTransformation);
    paramTransformation.set(paramRectF);
    paramRectF.set((Transformation)localObject);
  }
  
  public int getRepeatCount()
  {
    return mRepeatCount;
  }
  
  public int getRepeatMode()
  {
    return mRepeatMode;
  }
  
  protected float getScaleFactor()
  {
    return mScaleFactor;
  }
  
  public boolean getShowWallpaper()
  {
    return mShowWallpaper;
  }
  
  public long getStartOffset()
  {
    return mStartOffset;
  }
  
  public long getStartTime()
  {
    return mStartTime;
  }
  
  public boolean getTransformation(long paramLong, Transformation paramTransformation)
  {
    if (mStartTime == -1L) {
      mStartTime = paramLong;
    }
    long l1 = getStartOffset();
    long l2 = mDuration;
    if (l2 != 0L) {
      f1 = (float)(paramLong - (mStartTime + l1)) / (float)l2;
    } else if (paramLong < mStartTime) {
      f1 = 0.0F;
    } else {
      f1 = 1.0F;
    }
    int i;
    if ((f1 < 1.0F) && (!isCanceled())) {
      i = 0;
    } else {
      i = 1;
    }
    boolean bool;
    if (i == 0) {
      bool = true;
    } else {
      bool = false;
    }
    mMore = bool;
    float f2 = f1;
    if (!mFillEnabled) {
      f2 = Math.max(Math.min(f1, 1.0F), 0.0F);
    }
    if (((f2 < 0.0F) && (!mFillBefore)) || ((f2 > 1.0F) && (!mFillAfter))) {
      break label263;
    }
    if (!mStarted)
    {
      fireAnimationStart();
      mStarted = true;
      if (NoImagePreloadHolder.USE_CLOSEGUARD) {
        guard.open("cancel or detach or getTransformation");
      }
    }
    float f1 = f2;
    if (mFillEnabled) {
      f1 = Math.max(Math.min(f2, 1.0F), 0.0F);
    }
    f2 = f1;
    if (mCycleFlip) {
      f2 = 1.0F - f1;
    }
    applyTransformation(mInterpolator.getInterpolation(f2), paramTransformation);
    label263:
    if (i != 0) {
      if ((mRepeatCount != mRepeated) && (!isCanceled()))
      {
        if (mRepeatCount > 0) {
          mRepeated += 1;
        }
        if (mRepeatMode == 2) {
          mCycleFlip ^= true;
        }
        mStartTime = -1L;
        mMore = true;
        fireAnimationRepeat();
      }
      else if (!mEnded)
      {
        mEnded = true;
        guard.close();
        fireAnimationEnd();
      }
    }
    if ((!mMore) && (mOneMoreTime))
    {
      mOneMoreTime = false;
      return true;
    }
    return mMore;
  }
  
  public boolean getTransformation(long paramLong, Transformation paramTransformation, float paramFloat)
  {
    mScaleFactor = paramFloat;
    return getTransformation(paramLong, paramTransformation);
  }
  
  public int getZAdjustment()
  {
    return mZAdjustment;
  }
  
  public boolean hasAlpha()
  {
    return false;
  }
  
  public boolean hasEnded()
  {
    return mEnded;
  }
  
  public boolean hasStarted()
  {
    return mStarted;
  }
  
  public void initialize(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    reset();
    mInitialized = true;
  }
  
  public void initializeInvalidateRegion(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    Object localObject = mPreviousRegion;
    ((RectF)localObject).set(paramInt1, paramInt2, paramInt3, paramInt4);
    ((RectF)localObject).inset(-1.0F, -1.0F);
    if (mFillBefore)
    {
      localObject = mPreviousTransformation;
      applyTransformation(mInterpolator.getInterpolation(0.0F), (Transformation)localObject);
    }
  }
  
  public boolean isFillEnabled()
  {
    return mFillEnabled;
  }
  
  public boolean isInitialized()
  {
    return mInitialized;
  }
  
  public void reset()
  {
    mPreviousRegion.setEmpty();
    mPreviousTransformation.clear();
    mInitialized = false;
    mCycleFlip = false;
    mRepeated = 0;
    mMore = true;
    mOneMoreTime = true;
    mListenerHandler = null;
  }
  
  protected float resolveSize(int paramInt1, float paramFloat, int paramInt2, int paramInt3)
  {
    switch (paramInt1)
    {
    default: 
      return paramFloat;
    case 2: 
      return paramInt3 * paramFloat;
    case 1: 
      return paramInt2 * paramFloat;
    }
    return paramFloat;
  }
  
  public void restrictDuration(long paramLong)
  {
    if (mStartOffset > paramLong)
    {
      mStartOffset = paramLong;
      mDuration = 0L;
      mRepeatCount = 0;
      return;
    }
    long l1 = mDuration + mStartOffset;
    long l2 = l1;
    if (l1 > paramLong)
    {
      mDuration = (paramLong - mStartOffset);
      l2 = paramLong;
    }
    if (mDuration <= 0L)
    {
      mDuration = 0L;
      mRepeatCount = 0;
      return;
    }
    if ((mRepeatCount < 0) || (mRepeatCount > paramLong) || (mRepeatCount * l2 > paramLong))
    {
      mRepeatCount = ((int)(paramLong / l2) - 1);
      if (mRepeatCount < 0) {
        mRepeatCount = 0;
      }
    }
  }
  
  public void scaleCurrentDuration(float paramFloat)
  {
    mDuration = (((float)mDuration * paramFloat));
    mStartOffset = (((float)mStartOffset * paramFloat));
  }
  
  public void setAnimationListener(AnimationListener paramAnimationListener)
  {
    mListener = paramAnimationListener;
  }
  
  public void setBackgroundColor(int paramInt)
  {
    mBackgroundColor = paramInt;
  }
  
  public void setDetachWallpaper(boolean paramBoolean)
  {
    mDetachWallpaper = paramBoolean;
  }
  
  public void setDuration(long paramLong)
  {
    if (paramLong >= 0L)
    {
      mDuration = paramLong;
      return;
    }
    throw new IllegalArgumentException("Animation duration cannot be negative");
  }
  
  public void setFillAfter(boolean paramBoolean)
  {
    mFillAfter = paramBoolean;
  }
  
  public void setFillBefore(boolean paramBoolean)
  {
    mFillBefore = paramBoolean;
  }
  
  public void setFillEnabled(boolean paramBoolean)
  {
    mFillEnabled = paramBoolean;
  }
  
  public void setInterpolator(Context paramContext, int paramInt)
  {
    setInterpolator(AnimationUtils.loadInterpolator(paramContext, paramInt));
  }
  
  public void setInterpolator(Interpolator paramInterpolator)
  {
    mInterpolator = paramInterpolator;
  }
  
  public void setListenerHandler(Handler paramHandler)
  {
    if (mListenerHandler == null)
    {
      mOnStart = new Runnable()
      {
        public void run()
        {
          if (mListener != null) {
            mListener.onAnimationStart(Animation.this);
          }
        }
      };
      mOnRepeat = new Runnable()
      {
        public void run()
        {
          if (mListener != null) {
            mListener.onAnimationRepeat(Animation.this);
          }
        }
      };
      mOnEnd = new Runnable()
      {
        public void run()
        {
          if (mListener != null) {
            mListener.onAnimationEnd(Animation.this);
          }
        }
      };
    }
    mListenerHandler = paramHandler;
  }
  
  public void setRepeatCount(int paramInt)
  {
    int i = paramInt;
    if (paramInt < 0) {
      i = -1;
    }
    mRepeatCount = i;
  }
  
  public void setRepeatMode(int paramInt)
  {
    mRepeatMode = paramInt;
  }
  
  public void setShowWallpaper(boolean paramBoolean)
  {
    mShowWallpaper = paramBoolean;
  }
  
  public void setStartOffset(long paramLong)
  {
    mStartOffset = paramLong;
  }
  
  public void setStartTime(long paramLong)
  {
    mStartTime = paramLong;
    mEnded = false;
    mStarted = false;
    mCycleFlip = false;
    mRepeated = 0;
    mMore = true;
  }
  
  public void setZAdjustment(int paramInt)
  {
    mZAdjustment = paramInt;
  }
  
  public void start()
  {
    setStartTime(-1L);
  }
  
  public void startNow()
  {
    setStartTime(AnimationUtils.currentAnimationTimeMillis());
  }
  
  public boolean willChangeBounds()
  {
    return true;
  }
  
  public boolean willChangeTransformationMatrix()
  {
    return true;
  }
  
  public static abstract interface AnimationListener
  {
    public abstract void onAnimationEnd(Animation paramAnimation);
    
    public abstract void onAnimationRepeat(Animation paramAnimation);
    
    public abstract void onAnimationStart(Animation paramAnimation);
  }
  
  protected static class Description
  {
    public int type;
    public float value;
    
    protected Description() {}
    
    static Description parseValue(TypedValue paramTypedValue)
    {
      Description localDescription = new Description();
      if (paramTypedValue == null)
      {
        type = 0;
        value = 0.0F;
      }
      else
      {
        if (type == 6)
        {
          int i = data;
          int j = 1;
          if ((i & 0xF) == 1) {
            j = 2;
          }
          type = j;
          value = TypedValue.complexToFloat(data);
          return localDescription;
        }
        if (type == 4)
        {
          type = 0;
          value = paramTypedValue.getFloat();
          return localDescription;
        }
        if ((type >= 16) && (type <= 31))
        {
          type = 0;
          value = data;
          return localDescription;
        }
      }
      type = 0;
      value = 0.0F;
      return localDescription;
    }
  }
  
  private static class NoImagePreloadHolder
  {
    public static final boolean USE_CLOSEGUARD = SystemProperties.getBoolean("log.closeguard.Animation", false);
    
    private NoImagePreloadHolder() {}
  }
}
