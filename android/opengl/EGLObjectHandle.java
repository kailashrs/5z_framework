package android.opengl;

public abstract class EGLObjectHandle
{
  private final long mHandle;
  
  @Deprecated
  protected EGLObjectHandle(int paramInt)
  {
    mHandle = paramInt;
  }
  
  protected EGLObjectHandle(long paramLong)
  {
    mHandle = paramLong;
  }
  
  @Deprecated
  public int getHandle()
  {
    if ((mHandle & 0xFFFFFFFF) == mHandle) {
      return (int)mHandle;
    }
    throw new UnsupportedOperationException();
  }
  
  public long getNativeHandle()
  {
    return mHandle;
  }
  
  public int hashCode()
  {
    return 31 * 17 + (int)(mHandle ^ mHandle >>> 32);
  }
}
