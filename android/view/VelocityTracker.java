package android.view;

import android.util.Pools.SynchronizedPool;

public final class VelocityTracker
{
  private static final int ACTIVE_POINTER_ID = -1;
  private static final Pools.SynchronizedPool<VelocityTracker> sPool = new Pools.SynchronizedPool(2);
  private long mPtr;
  private final String mStrategy;
  
  private VelocityTracker(String paramString)
  {
    mPtr = nativeInitialize(paramString);
    mStrategy = paramString;
  }
  
  private static native void nativeAddMovement(long paramLong, MotionEvent paramMotionEvent);
  
  private static native void nativeClear(long paramLong);
  
  private static native void nativeComputeCurrentVelocity(long paramLong, int paramInt, float paramFloat);
  
  private static native void nativeDispose(long paramLong);
  
  private static native boolean nativeGetEstimator(long paramLong, int paramInt, Estimator paramEstimator);
  
  private static native float nativeGetXVelocity(long paramLong, int paramInt);
  
  private static native float nativeGetYVelocity(long paramLong, int paramInt);
  
  private static native long nativeInitialize(String paramString);
  
  public static VelocityTracker obtain()
  {
    VelocityTracker localVelocityTracker = (VelocityTracker)sPool.acquire();
    if (localVelocityTracker == null) {
      localVelocityTracker = new VelocityTracker(null);
    }
    return localVelocityTracker;
  }
  
  public static VelocityTracker obtain(String paramString)
  {
    if (paramString == null) {
      return obtain();
    }
    return new VelocityTracker(paramString);
  }
  
  public void addMovement(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent != null)
    {
      nativeAddMovement(mPtr, paramMotionEvent);
      return;
    }
    throw new IllegalArgumentException("event must not be null");
  }
  
  public void clear()
  {
    nativeClear(mPtr);
  }
  
  public void computeCurrentVelocity(int paramInt)
  {
    nativeComputeCurrentVelocity(mPtr, paramInt, Float.MAX_VALUE);
  }
  
  public void computeCurrentVelocity(int paramInt, float paramFloat)
  {
    nativeComputeCurrentVelocity(mPtr, paramInt, paramFloat);
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (mPtr != 0L)
      {
        nativeDispose(mPtr);
        mPtr = 0L;
      }
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public boolean getEstimator(int paramInt, Estimator paramEstimator)
  {
    if (paramEstimator != null) {
      return nativeGetEstimator(mPtr, paramInt, paramEstimator);
    }
    throw new IllegalArgumentException("outEstimator must not be null");
  }
  
  public float getXVelocity()
  {
    return nativeGetXVelocity(mPtr, -1);
  }
  
  public float getXVelocity(int paramInt)
  {
    return nativeGetXVelocity(mPtr, paramInt);
  }
  
  public float getYVelocity()
  {
    return nativeGetYVelocity(mPtr, -1);
  }
  
  public float getYVelocity(int paramInt)
  {
    return nativeGetYVelocity(mPtr, paramInt);
  }
  
  public void recycle()
  {
    if (mStrategy == null)
    {
      clear();
      sPool.release(this);
    }
  }
  
  public static final class Estimator
  {
    private static final int MAX_DEGREE = 4;
    public float confidence;
    public int degree;
    public final float[] xCoeff = new float[5];
    public final float[] yCoeff = new float[5];
    
    public Estimator() {}
    
    private float estimate(float paramFloat, float[] paramArrayOfFloat)
    {
      float f1 = 0.0F;
      float f2 = 1.0F;
      for (int i = 0; i <= degree; i++)
      {
        f1 += paramArrayOfFloat[i] * f2;
        f2 *= paramFloat;
      }
      return f1;
    }
    
    public float estimateX(float paramFloat)
    {
      return estimate(paramFloat, xCoeff);
    }
    
    public float estimateY(float paramFloat)
    {
      return estimate(paramFloat, yCoeff);
    }
    
    public float getXCoeff(int paramInt)
    {
      float f;
      if (paramInt <= degree) {
        f = xCoeff[paramInt];
      } else {
        f = 0.0F;
      }
      return f;
    }
    
    public float getYCoeff(int paramInt)
    {
      float f;
      if (paramInt <= degree) {
        f = yCoeff[paramInt];
      } else {
        f = 0.0F;
      }
      return f;
    }
  }
}
