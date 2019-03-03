package android.util;

public final class Pools
{
  private Pools() {}
  
  public static abstract interface Pool<T>
  {
    public abstract T acquire();
    
    public abstract boolean release(T paramT);
  }
  
  public static class SimplePool<T>
    implements Pools.Pool<T>
  {
    private final Object[] mPool;
    private int mPoolSize;
    
    public SimplePool(int paramInt)
    {
      if (paramInt > 0)
      {
        mPool = new Object[paramInt];
        return;
      }
      throw new IllegalArgumentException("The max pool size must be > 0");
    }
    
    private boolean isInPool(T paramT)
    {
      for (int i = 0; i < mPoolSize; i++) {
        if (mPool[i] == paramT) {
          return true;
        }
      }
      return false;
    }
    
    public T acquire()
    {
      if (mPoolSize > 0)
      {
        int i = mPoolSize - 1;
        Object localObject = mPool[i];
        mPool[i] = null;
        mPoolSize -= 1;
        return localObject;
      }
      return null;
    }
    
    public boolean release(T paramT)
    {
      if (!isInPool(paramT))
      {
        if (mPoolSize < mPool.length)
        {
          mPool[mPoolSize] = paramT;
          mPoolSize += 1;
          return true;
        }
        return false;
      }
      throw new IllegalStateException("Already in the pool!");
    }
  }
  
  public static class SynchronizedPool<T>
    extends Pools.SimplePool<T>
  {
    private final Object mLock;
    
    public SynchronizedPool(int paramInt)
    {
      this(paramInt, new Object());
    }
    
    public SynchronizedPool(int paramInt, Object paramObject)
    {
      super();
      mLock = paramObject;
    }
    
    public T acquire()
    {
      synchronized (mLock)
      {
        Object localObject2 = super.acquire();
        return localObject2;
      }
    }
    
    public boolean release(T paramT)
    {
      synchronized (mLock)
      {
        boolean bool = super.release(paramT);
        return bool;
      }
    }
  }
}
