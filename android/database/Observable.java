package android.database;

import java.util.ArrayList;

public abstract class Observable<T>
{
  protected final ArrayList<T> mObservers = new ArrayList();
  
  public Observable() {}
  
  public void registerObserver(T paramT)
  {
    if (paramT != null) {
      synchronized (mObservers)
      {
        if (!mObservers.contains(paramT))
        {
          mObservers.add(paramT);
          return;
        }
        IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Observer ");
        localStringBuilder.append(paramT);
        localStringBuilder.append(" is already registered.");
        localIllegalStateException.<init>(localStringBuilder.toString());
        throw localIllegalStateException;
      }
    }
    throw new IllegalArgumentException("The observer is null.");
  }
  
  public void unregisterAll()
  {
    synchronized (mObservers)
    {
      mObservers.clear();
      return;
    }
  }
  
  public void unregisterObserver(T paramT)
  {
    if (paramT != null) {
      synchronized (mObservers)
      {
        int i = mObservers.indexOf(paramT);
        if (i != -1)
        {
          mObservers.remove(i);
          return;
        }
        IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Observer ");
        localStringBuilder.append(paramT);
        localStringBuilder.append(" was not registered.");
        localIllegalStateException.<init>(localStringBuilder.toString());
        throw localIllegalStateException;
      }
    }
    throw new IllegalArgumentException("The observer is null.");
  }
}
