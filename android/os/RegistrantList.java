package android.os;

import java.util.ArrayList;

public class RegistrantList
{
  ArrayList registrants = new ArrayList();
  
  public RegistrantList() {}
  
  private void internalNotifyRegistrants(Object paramObject, Throwable paramThrowable)
  {
    int i = 0;
    try
    {
      int j = registrants.size();
      while (i < j)
      {
        ((Registrant)registrants.get(i)).internalNotifyRegistrant(paramObject, paramThrowable);
        i++;
      }
      return;
    }
    finally {}
  }
  
  public void add(Handler paramHandler, int paramInt, Object paramObject)
  {
    try
    {
      Registrant localRegistrant = new android/os/Registrant;
      localRegistrant.<init>(paramHandler, paramInt, paramObject);
      add(localRegistrant);
      return;
    }
    finally
    {
      paramHandler = finally;
      throw paramHandler;
    }
  }
  
  public void add(Registrant paramRegistrant)
  {
    try
    {
      removeCleared();
      registrants.add(paramRegistrant);
      return;
    }
    finally
    {
      paramRegistrant = finally;
      throw paramRegistrant;
    }
  }
  
  public void addUnique(Handler paramHandler, int paramInt, Object paramObject)
  {
    try
    {
      remove(paramHandler);
      Registrant localRegistrant = new android/os/Registrant;
      localRegistrant.<init>(paramHandler, paramInt, paramObject);
      add(localRegistrant);
      return;
    }
    finally
    {
      paramHandler = finally;
      throw paramHandler;
    }
  }
  
  public Object get(int paramInt)
  {
    try
    {
      Object localObject1 = registrants.get(paramInt);
      return localObject1;
    }
    finally
    {
      localObject2 = finally;
      throw localObject2;
    }
  }
  
  public void notifyException(Throwable paramThrowable)
  {
    internalNotifyRegistrants(null, paramThrowable);
  }
  
  public void notifyRegistrants()
  {
    internalNotifyRegistrants(null, null);
  }
  
  public void notifyRegistrants(AsyncResult paramAsyncResult)
  {
    internalNotifyRegistrants(result, exception);
  }
  
  public void notifyResult(Object paramObject)
  {
    internalNotifyRegistrants(paramObject, null);
  }
  
  public void remove(Handler paramHandler)
  {
    int i = 0;
    try
    {
      int j = registrants.size();
      while (i < j)
      {
        Registrant localRegistrant = (Registrant)registrants.get(i);
        Handler localHandler = localRegistrant.getHandler();
        if ((localHandler == null) || (localHandler == paramHandler)) {
          localRegistrant.clear();
        }
        i++;
      }
      removeCleared();
      return;
    }
    finally {}
  }
  
  public void removeCleared()
  {
    try
    {
      for (int i = registrants.size() - 1; i >= 0; i--) {
        if (registrants.get(i)).refH == null) {
          registrants.remove(i);
        }
      }
      return;
    }
    finally {}
  }
  
  public int size()
  {
    try
    {
      int i = registrants.size();
      return i;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
}
