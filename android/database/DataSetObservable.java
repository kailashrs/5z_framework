package android.database;

import java.util.ArrayList;

public class DataSetObservable
  extends Observable<DataSetObserver>
{
  public DataSetObservable() {}
  
  public void notifyChanged()
  {
    synchronized (mObservers)
    {
      for (int i = mObservers.size() - 1; i >= 0; i--) {
        ((DataSetObserver)mObservers.get(i)).onChanged();
      }
      return;
    }
  }
  
  public void notifyInvalidated()
  {
    synchronized (mObservers)
    {
      for (int i = mObservers.size() - 1; i >= 0; i--) {
        ((DataSetObserver)mObservers.get(i)).onInvalidated();
      }
      return;
    }
  }
}
