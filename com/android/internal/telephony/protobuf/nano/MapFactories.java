package com.android.internal.telephony.protobuf.nano;

import java.util.HashMap;
import java.util.Map;

public final class MapFactories
{
  private static volatile MapFactory mapFactory = new DefaultMapFactory(null);
  
  private MapFactories() {}
  
  public static MapFactory getMapFactory()
  {
    return mapFactory;
  }
  
  static void setMapFactory(MapFactory paramMapFactory)
  {
    mapFactory = paramMapFactory;
  }
  
  private static class DefaultMapFactory
    implements MapFactories.MapFactory
  {
    private DefaultMapFactory() {}
    
    public <K, V> Map<K, V> forMap(Map<K, V> paramMap)
    {
      if (paramMap == null) {
        return new HashMap();
      }
      return paramMap;
    }
  }
  
  public static abstract interface MapFactory
  {
    public abstract <K, V> Map<K, V> forMap(Map<K, V> paramMap);
  }
}
