package android.app.servertransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class ObjectPool
{
  private static final int MAX_POOL_SIZE = 50;
  private static final Map<Class, ArrayList<? extends ObjectPoolItem>> sPoolMap = new HashMap();
  private static final Object sPoolSync = new Object();
  
  ObjectPool() {}
  
  public static <T extends ObjectPoolItem> T obtain(Class<T> paramClass)
  {
    synchronized (sPoolSync)
    {
      paramClass = (ArrayList)sPoolMap.get(paramClass);
      if ((paramClass != null) && (!paramClass.isEmpty()))
      {
        paramClass = (ObjectPoolItem)paramClass.remove(paramClass.size() - 1);
        return paramClass;
      }
      return null;
    }
  }
  
  public static <T extends ObjectPoolItem> void recycle(T paramT)
  {
    synchronized (sPoolSync)
    {
      ArrayList localArrayList1 = (ArrayList)sPoolMap.get(paramT.getClass());
      ArrayList localArrayList2 = localArrayList1;
      if (localArrayList1 == null)
      {
        localArrayList2 = new java/util/ArrayList;
        localArrayList2.<init>();
        sPoolMap.put(paramT.getClass(), localArrayList2);
      }
      int i = localArrayList2.size();
      int j = 0;
      while (j < i) {
        if (localArrayList2.get(j) != paramT)
        {
          j++;
        }
        else
        {
          paramT = new java/lang/IllegalStateException;
          paramT.<init>("Trying to recycle already recycled item");
          throw paramT;
        }
      }
      if (i < 50) {
        localArrayList2.add(paramT);
      }
      return;
    }
  }
}
