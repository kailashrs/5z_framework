package com.google.android.mms.util;

import java.util.HashMap;

public abstract class AbstractCache<K, V>
{
  private static final boolean DEBUG = false;
  private static final boolean LOCAL_LOGV = false;
  private static final int MAX_CACHED_ITEMS = 500;
  private static final String TAG = "AbstractCache";
  private final HashMap<K, CacheEntry<V>> mCacheMap = new HashMap();
  
  protected AbstractCache() {}
  
  public V get(K paramK)
  {
    if (paramK != null)
    {
      paramK = (CacheEntry)mCacheMap.get(paramK);
      if (paramK != null)
      {
        hit += 1;
        return value;
      }
    }
    return null;
  }
  
  public V purge(K paramK)
  {
    paramK = (CacheEntry)mCacheMap.remove(paramK);
    if (paramK != null) {
      paramK = value;
    } else {
      paramK = null;
    }
    return paramK;
  }
  
  public void purgeAll()
  {
    mCacheMap.clear();
  }
  
  public boolean put(K paramK, V paramV)
  {
    if (mCacheMap.size() >= 500) {
      return false;
    }
    if (paramK != null)
    {
      CacheEntry localCacheEntry = new CacheEntry(null);
      value = paramV;
      mCacheMap.put(paramK, localCacheEntry);
      return true;
    }
    return false;
  }
  
  public int size()
  {
    return mCacheMap.size();
  }
  
  private static class CacheEntry<V>
  {
    int hit;
    V value;
    
    private CacheEntry() {}
  }
}
