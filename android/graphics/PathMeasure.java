package android.graphics;

public class PathMeasure
{
  public static final int POSITION_MATRIX_FLAG = 1;
  public static final int TANGENT_MATRIX_FLAG = 2;
  private Path mPath;
  private long native_instance;
  
  public PathMeasure()
  {
    mPath = null;
    native_instance = native_create(0L, false);
  }
  
  public PathMeasure(Path paramPath, boolean paramBoolean)
  {
    mPath = paramPath;
    long l;
    if (paramPath != null) {
      l = paramPath.readOnlyNI();
    } else {
      l = 0L;
    }
    native_instance = native_create(l, paramBoolean);
  }
  
  private static native long native_create(long paramLong, boolean paramBoolean);
  
  private static native void native_destroy(long paramLong);
  
  private static native float native_getLength(long paramLong);
  
  private static native boolean native_getMatrix(long paramLong1, float paramFloat, long paramLong2, int paramInt);
  
  private static native boolean native_getPosTan(long paramLong, float paramFloat, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2);
  
  private static native boolean native_getSegment(long paramLong1, float paramFloat1, float paramFloat2, long paramLong2, boolean paramBoolean);
  
  private static native boolean native_isClosed(long paramLong);
  
  private static native boolean native_nextContour(long paramLong);
  
  private static native void native_setPath(long paramLong1, long paramLong2, boolean paramBoolean);
  
  protected void finalize()
    throws Throwable
  {
    native_destroy(native_instance);
    native_instance = 0L;
  }
  
  public float getLength()
  {
    return native_getLength(native_instance);
  }
  
  public boolean getMatrix(float paramFloat, Matrix paramMatrix, int paramInt)
  {
    return native_getMatrix(native_instance, paramFloat, native_instance, paramInt);
  }
  
  public boolean getPosTan(float paramFloat, float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    if (((paramArrayOfFloat1 != null) && (paramArrayOfFloat1.length < 2)) || ((paramArrayOfFloat2 != null) && (paramArrayOfFloat2.length < 2))) {
      throw new ArrayIndexOutOfBoundsException();
    }
    return native_getPosTan(native_instance, paramFloat, paramArrayOfFloat1, paramArrayOfFloat2);
  }
  
  public boolean getSegment(float paramFloat1, float paramFloat2, Path paramPath, boolean paramBoolean)
  {
    float f1 = getLength();
    float f2 = paramFloat1;
    if (paramFloat1 < 0.0F) {
      f2 = 0.0F;
    }
    paramFloat1 = paramFloat2;
    if (paramFloat2 > f1) {
      paramFloat1 = f1;
    }
    if (f2 >= paramFloat1) {
      return false;
    }
    return native_getSegment(native_instance, f2, paramFloat1, paramPath.mutateNI(), paramBoolean);
  }
  
  public boolean isClosed()
  {
    return native_isClosed(native_instance);
  }
  
  public boolean nextContour()
  {
    return native_nextContour(native_instance);
  }
  
  public void setPath(Path paramPath, boolean paramBoolean)
  {
    mPath = paramPath;
    long l1 = native_instance;
    long l2;
    if (paramPath != null) {
      l2 = paramPath.readOnlyNI();
    } else {
      l2 = 0L;
    }
    native_setPath(l1, l2, paramBoolean);
  }
}
