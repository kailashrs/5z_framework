package android.view;

public final class SurfaceSession
{
  private long mNativeClient;
  
  public SurfaceSession()
  {
    mNativeClient = nativeCreate();
  }
  
  public SurfaceSession(Surface paramSurface)
  {
    mNativeClient = nativeCreateScoped(mNativeObject);
  }
  
  private static native long nativeCreate();
  
  private static native long nativeCreateScoped(long paramLong);
  
  private static native void nativeDestroy(long paramLong);
  
  private static native void nativeKill(long paramLong);
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (mNativeClient != 0L) {
        nativeDestroy(mNativeClient);
      }
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public void kill()
  {
    nativeKill(mNativeClient);
  }
}
