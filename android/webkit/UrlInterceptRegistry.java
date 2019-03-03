package android.webkit;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

@Deprecated
public final class UrlInterceptRegistry
{
  private static final String LOGTAG = "intercept";
  private static boolean mDisabled = false;
  private static LinkedList mHandlerList;
  
  public UrlInterceptRegistry() {}
  
  private static LinkedList getHandlers()
  {
    try
    {
      if (mHandlerList == null)
      {
        localLinkedList = new java/util/LinkedList;
        localLinkedList.<init>();
        mHandlerList = localLinkedList;
      }
      LinkedList localLinkedList = mHandlerList;
      return localLinkedList;
    }
    finally {}
  }
  
  @Deprecated
  public static PluginData getPluginData(String paramString, Map<String, String> paramMap)
  {
    try
    {
      boolean bool = urlInterceptDisabled();
      if (bool) {
        return null;
      }
      ListIterator localListIterator = getHandlers().listIterator();
      while (localListIterator.hasNext())
      {
        PluginData localPluginData = ((UrlInterceptHandler)localListIterator.next()).getPluginData(paramString, paramMap);
        if (localPluginData != null) {
          return localPluginData;
        }
      }
      return null;
    }
    finally {}
  }
  
  @Deprecated
  public static CacheManager.CacheResult getSurrogate(String paramString, Map<String, String> paramMap)
  {
    try
    {
      boolean bool = urlInterceptDisabled();
      if (bool) {
        return null;
      }
      ListIterator localListIterator = getHandlers().listIterator();
      while (localListIterator.hasNext())
      {
        CacheManager.CacheResult localCacheResult = ((UrlInterceptHandler)localListIterator.next()).service(paramString, paramMap);
        if (localCacheResult != null) {
          return localCacheResult;
        }
      }
      return null;
    }
    finally {}
  }
  
  @Deprecated
  public static boolean registerHandler(UrlInterceptHandler paramUrlInterceptHandler)
  {
    try
    {
      if (!getHandlers().contains(paramUrlInterceptHandler))
      {
        getHandlers().addFirst(paramUrlInterceptHandler);
        return true;
      }
      return false;
    }
    finally
    {
      paramUrlInterceptHandler = finally;
      throw paramUrlInterceptHandler;
    }
  }
  
  @Deprecated
  public static void setUrlInterceptDisabled(boolean paramBoolean)
  {
    try
    {
      mDisabled = paramBoolean;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  @Deprecated
  public static boolean unregisterHandler(UrlInterceptHandler paramUrlInterceptHandler)
  {
    try
    {
      boolean bool = getHandlers().remove(paramUrlInterceptHandler);
      return bool;
    }
    finally
    {
      paramUrlInterceptHandler = finally;
      throw paramUrlInterceptHandler;
    }
  }
  
  @Deprecated
  public static boolean urlInterceptDisabled()
  {
    try
    {
      boolean bool = mDisabled;
      return bool;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
}
