package android.telecom.Logging;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class TimedEvent<T>
{
  public TimedEvent() {}
  
  public static <T> Map<T, Double> averageTimings(Collection<? extends TimedEvent<T>> paramCollection)
  {
    HashMap localHashMap1 = new HashMap();
    HashMap localHashMap2 = new HashMap();
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      paramCollection = (TimedEvent)localIterator.next();
      if (localHashMap1.containsKey(paramCollection.getKey()))
      {
        localHashMap1.put(paramCollection.getKey(), Integer.valueOf(((Integer)localHashMap1.get(paramCollection.getKey())).intValue() + 1));
        localHashMap2.put(paramCollection.getKey(), Double.valueOf(((Double)localHashMap2.get(paramCollection.getKey())).doubleValue() + paramCollection.getTime()));
      }
      else
      {
        localHashMap1.put(paramCollection.getKey(), Integer.valueOf(1));
        localHashMap2.put(paramCollection.getKey(), Double.valueOf(paramCollection.getTime()));
      }
    }
    localIterator = localHashMap2.entrySet().iterator();
    while (localIterator.hasNext())
    {
      paramCollection = (Map.Entry)localIterator.next();
      localHashMap2.put(paramCollection.getKey(), Double.valueOf(((Double)paramCollection.getValue()).doubleValue() / ((Integer)localHashMap1.get(paramCollection.getKey())).intValue()));
    }
    return localHashMap2;
  }
  
  public abstract T getKey();
  
  public abstract long getTime();
}
