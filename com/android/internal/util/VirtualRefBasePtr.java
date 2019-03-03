package com.android.internal.util;

public final class VirtualRefBasePtr
{
  private long mNativePtr;
  
  public VirtualRefBasePtr(long paramLong)
  {
    mNativePtr = paramLong;
    nIncStrong(mNativePtr);
  }
  
  private static native void nDecStrong(long paramLong);
  
  private static native void nIncStrong(long paramLong);
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      release();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public long get()
  {
    return mNativePtr;
  }
  
  public void release()
  {
    if (mNativePtr != 0L)
    {
      nDecStrong(mNativePtr);
      mNativePtr = 0L;
    }
  }
}
