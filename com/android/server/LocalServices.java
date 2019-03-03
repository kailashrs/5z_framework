package com.android.server;

import android.util.ArrayMap;
import com.android.internal.annotations.VisibleForTesting;

public final class LocalServices
{
  private static final ArrayMap<Class<?>, Object> sLocalServiceObjects = new ArrayMap();
  
  private LocalServices() {}
  
  public static <T> void addService(Class<T> paramClass, T paramT)
  {
    synchronized (sLocalServiceObjects)
    {
      if (!sLocalServiceObjects.containsKey(paramClass))
      {
        sLocalServiceObjects.put(paramClass, paramT);
        return;
      }
      paramClass = new java/lang/IllegalStateException;
      paramClass.<init>("Overriding service registration");
      throw paramClass;
    }
  }
  
  public static <T> T getService(Class<T> paramClass)
  {
    synchronized (sLocalServiceObjects)
    {
      paramClass = sLocalServiceObjects.get(paramClass);
      return paramClass;
    }
  }
  
  @VisibleForTesting
  public static <T> void removeServiceForTest(Class<T> paramClass)
  {
    synchronized (sLocalServiceObjects)
    {
      sLocalServiceObjects.remove(paramClass);
      return;
    }
  }
}
