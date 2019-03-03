package android.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class LruCache<K, V>
{
  private int createCount;
  private int evictionCount;
  private int hitCount;
  private final LinkedHashMap<K, V> map;
  private int maxSize;
  private int missCount;
  private int putCount;
  private int size;
  
  public LruCache(int paramInt)
  {
    if (paramInt > 0)
    {
      maxSize = paramInt;
      map = new LinkedHashMap(0, 0.75F, true);
      return;
    }
    throw new IllegalArgumentException("maxSize <= 0");
  }
  
  private int safeSizeOf(K paramK, V paramV)
  {
    int i = sizeOf(paramK, paramV);
    if (i >= 0) {
      return i;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Negative size: ");
    localStringBuilder.append(paramK);
    localStringBuilder.append("=");
    localStringBuilder.append(paramV);
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  protected V create(K paramK)
  {
    return null;
  }
  
  public final int createCount()
  {
    try
    {
      int i = createCount;
      return i;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  protected void entryRemoved(boolean paramBoolean, K paramK, V paramV1, V paramV2) {}
  
  public final void evictAll()
  {
    trimToSize(-1);
  }
  
  public final int evictionCount()
  {
    try
    {
      int i = evictionCount;
      return i;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public final V get(K paramK)
  {
    if (paramK != null) {
      try
      {
        Object localObject1 = map.get(paramK);
        if (localObject1 != null)
        {
          hitCount += 1;
          return localObject1;
        }
        missCount += 1;
        localObject1 = create(paramK);
        if (localObject1 == null) {
          return null;
        }
        try
        {
          createCount += 1;
          Object localObject2 = map.put(paramK, localObject1);
          if (localObject2 != null) {
            map.put(paramK, localObject2);
          } else {
            size += safeSizeOf(paramK, localObject1);
          }
          if (localObject2 != null)
          {
            entryRemoved(false, paramK, localObject1, localObject2);
            return localObject2;
          }
          trimToSize(maxSize);
          return localObject1;
        }
        finally {}
        throw new NullPointerException("key == null");
      }
      finally {}
    }
  }
  
  public final int hitCount()
  {
    try
    {
      int i = hitCount;
      return i;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public final int maxSize()
  {
    try
    {
      int i = maxSize;
      return i;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public final int missCount()
  {
    try
    {
      int i = missCount;
      return i;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public final V put(K paramK, V paramV)
  {
    if ((paramK != null) && (paramV != null)) {
      try
      {
        putCount += 1;
        size += safeSizeOf(paramK, paramV);
        Object localObject = map.put(paramK, paramV);
        if (localObject != null) {
          size -= safeSizeOf(paramK, localObject);
        }
        if (localObject != null) {
          entryRemoved(false, paramK, localObject, paramV);
        }
        trimToSize(maxSize);
        return localObject;
      }
      finally {}
    }
    throw new NullPointerException("key == null || value == null");
  }
  
  public final int putCount()
  {
    try
    {
      int i = putCount;
      return i;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public final V remove(K paramK)
  {
    if (paramK != null) {
      try
      {
        Object localObject = map.remove(paramK);
        if (localObject != null) {
          size -= safeSizeOf(paramK, localObject);
        }
        if (localObject != null) {
          entryRemoved(false, paramK, localObject, null);
        }
        return localObject;
      }
      finally {}
    }
    throw new NullPointerException("key == null");
  }
  
  public void resize(int paramInt)
  {
    if (paramInt > 0) {
      try
      {
        maxSize = paramInt;
        trimToSize(paramInt);
        return;
      }
      finally {}
    }
    throw new IllegalArgumentException("maxSize <= 0");
  }
  
  public final int size()
  {
    try
    {
      int i = size;
      return i;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  protected int sizeOf(K paramK, V paramV)
  {
    return 1;
  }
  
  public final Map<K, V> snapshot()
  {
    try
    {
      LinkedHashMap localLinkedHashMap = new LinkedHashMap(map);
      return localLinkedHashMap;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public final String toString()
  {
    try
    {
      int i = hitCount + missCount;
      if (i != 0) {
        i = 100 * hitCount / i;
      } else {
        i = 0;
      }
      String str = String.format("LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", new Object[] { Integer.valueOf(maxSize), Integer.valueOf(hitCount), Integer.valueOf(missCount), Integer.valueOf(i) });
      return str;
    }
    finally {}
  }
  
  public void trimToSize(int paramInt)
  {
    for (;;)
    {
      try
      {
        if ((size >= 0) && ((!map.isEmpty()) || (size == 0)))
        {
          if (size <= paramInt) {
            break label53;
          }
          localObject1 = map.eldest();
          if (localObject1 == null) {
            label53:
            return;
          }
          localObject2 = ((Map.Entry)localObject1).getKey();
          localObject1 = ((Map.Entry)localObject1).getValue();
          map.remove(localObject2);
          size -= safeSizeOf(localObject2, localObject1);
          evictionCount += 1;
          entryRemoved(true, localObject2, localObject1, null);
          continue;
        }
        Object localObject1 = new java/lang/IllegalStateException;
        Object localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append(getClass().getName());
        ((StringBuilder)localObject2).append(".sizeOf() is reporting inconsistent results!");
        ((IllegalStateException)localObject1).<init>(((StringBuilder)localObject2).toString());
        throw ((Throwable)localObject1);
      }
      finally {}
    }
  }
}
