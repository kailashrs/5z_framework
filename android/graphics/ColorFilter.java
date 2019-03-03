package android.graphics;

import libcore.util.NativeAllocationRegistry;

public class ColorFilter
{
  private Runnable mCleaner;
  private long mNativeInstance;
  
  @Deprecated
  public ColorFilter() {}
  
  private static native long nativeGetFinalizer();
  
  long createNativeInstance()
  {
    return 0L;
  }
  
  void discardNativeInstance()
  {
    if (mNativeInstance != 0L)
    {
      mCleaner.run();
      mCleaner = null;
      mNativeInstance = 0L;
    }
  }
  
  public long getNativeInstance()
  {
    if (mNativeInstance == 0L)
    {
      mNativeInstance = createNativeInstance();
      if (mNativeInstance != 0L) {
        mCleaner = NoImagePreloadHolder.sRegistry.registerNativeAllocation(this, mNativeInstance);
      }
    }
    return mNativeInstance;
  }
  
  private static class NoImagePreloadHolder
  {
    public static final NativeAllocationRegistry sRegistry = new NativeAllocationRegistry(ColorFilter.class.getClassLoader(), ColorFilter.access$000(), 50L);
    
    private NoImagePreloadHolder() {}
  }
}
