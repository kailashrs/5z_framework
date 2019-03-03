package android.telecom;

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
    paramCollection = paramCollection.iterator();
    Object localObject;
    while (paramCollection.hasNext())
    {
      localObject = (TimedEvent)paramCollection.next();
      if (localHashMap1.containsKey(((TimedEvent)localObject).getKey()))
      {
        localHashMap1.put(((TimedEvent)localObject).getKey(), Integer.valueOf(((Integer)localHashMap1.get(((TimedEvent)localObject).getKey())).intValue() + 1));
        localHashMap2.put(((TimedEvent)localObject).getKey(), Double.valueOf(((Double)localHashMap2.get(((TimedEvent)localObject).getKey())).doubleValue() + ((TimedEvent)localObject).getTime()));
      }
      else
      {
        localHashMap1.put(((TimedEvent)localObject).getKey(), Integer.valueOf(1));
        localHashMap2.put(((TimedEvent)localObject).getKey(), Double.valueOf(((TimedEvent)localObject).getTime()));
      }
    }
    paramCollection = localHashMap2.entrySet().iterator();
    while (paramCollection.hasNext())
    {
      localObject = (Map.Entry)paramCollection.next();
      localHashMap2.put(((Map.Entry)localObject).getKey(), Double.valueOf(((Double)((Map.Entry)localObject).getValue()).doubleValue() / ((Integer)localHashMap1.get(((Map.Entry)localObject).getKey())).intValue()));
    }
    return localHashMap2;
  }
  
  public abstract T getKey();
  
  public abstract long getTime();
}
