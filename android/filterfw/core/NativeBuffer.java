package android.filterfw.core;

public class NativeBuffer
{
  private Frame mAttachedFrame;
  private long mDataPointer = 0L;
  private boolean mOwnsData = false;
  private int mRefCount = 1;
  private int mSize = 0;
  
  static
  {
    System.loadLibrary("filterfw");
  }
  
  public NativeBuffer() {}
  
  public NativeBuffer(int paramInt)
  {
    allocate(getElementSize() * paramInt);
    mOwnsData = true;
  }
  
  private native boolean allocate(int paramInt);
  
  private native boolean deallocate(boolean paramBoolean);
  
  private native boolean nativeCopyTo(NativeBuffer paramNativeBuffer);
  
  protected void assertReadable()
  {
    if ((mDataPointer != 0L) && (mSize != 0) && ((mAttachedFrame == null) || (mAttachedFrame.hasNativeAllocation()))) {
      return;
    }
    throw new NullPointerException("Attempting to read from null data frame!");
  }
  
  protected void assertWritable()
  {
    if (!isReadOnly()) {
      return;
    }
    throw new RuntimeException("Attempting to modify read-only native (structured) data!");
  }
  
  void attachToFrame(Frame paramFrame)
  {
    mAttachedFrame = paramFrame;
  }
  
  public int count()
  {
    int i;
    if (mDataPointer != 0L) {
      i = mSize / getElementSize();
    } else {
      i = 0;
    }
    return i;
  }
  
  public int getElementSize()
  {
    return 1;
  }
  
  public boolean isReadOnly()
  {
    boolean bool;
    if (mAttachedFrame != null) {
      bool = mAttachedFrame.isReadOnly();
    } else {
      bool = false;
    }
    return bool;
  }
  
  public NativeBuffer mutableCopy()
  {
    try
    {
      NativeBuffer localNativeBuffer = (NativeBuffer)getClass().newInstance();
      if ((mSize > 0) && (!nativeCopyTo(localNativeBuffer))) {
        throw new RuntimeException("Failed to copy NativeBuffer to mutable instance!");
      }
      return localNativeBuffer;
    }
    catch (Exception localException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unable to allocate a copy of ");
      localStringBuilder.append(getClass());
      localStringBuilder.append("! Make sure the class has a default constructor!");
      throw new RuntimeException(localStringBuilder.toString());
    }
  }
  
  public NativeBuffer release()
  {
    int i = 0;
    Frame localFrame = mAttachedFrame;
    int j = 0;
    int k = 0;
    if (localFrame != null)
    {
      if (mAttachedFrame.release() == null) {
        k = 1;
      }
    }
    else
    {
      k = i;
      if (mOwnsData)
      {
        mRefCount -= 1;
        k = j;
        if (mRefCount == 0) {
          k = 1;
        }
      }
    }
    if (k != 0)
    {
      deallocate(mOwnsData);
      return null;
    }
    return this;
  }
  
  public NativeBuffer retain()
  {
    if (mAttachedFrame != null) {
      mAttachedFrame.retain();
    } else if (mOwnsData) {
      mRefCount += 1;
    }
    return this;
  }
  
  public int size()
  {
    return mSize;
  }
}
