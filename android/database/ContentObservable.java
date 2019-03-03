package android.database;

import android.net.Uri;
import java.util.ArrayList;
import java.util.Iterator;

public class ContentObservable
  extends Observable<ContentObserver>
{
  public ContentObservable() {}
  
  @Deprecated
  public void dispatchChange(boolean paramBoolean)
  {
    dispatchChange(paramBoolean, null);
  }
  
  public void dispatchChange(boolean paramBoolean, Uri paramUri)
  {
    synchronized (mObservers)
    {
      Iterator localIterator = mObservers.iterator();
      while (localIterator.hasNext())
      {
        ContentObserver localContentObserver = (ContentObserver)localIterator.next();
        if ((!paramBoolean) || (localContentObserver.deliverSelfNotifications())) {
          localContentObserver.dispatchChange(paramBoolean, paramUri);
        }
      }
      return;
    }
  }
  
  @Deprecated
  public void notifyChange(boolean paramBoolean)
  {
    synchronized (mObservers)
    {
      Iterator localIterator = mObservers.iterator();
      while (localIterator.hasNext()) {
        ((ContentObserver)localIterator.next()).onChange(paramBoolean, null);
      }
      return;
    }
  }
  
  public void registerObserver(ContentObserver paramContentObserver)
  {
    super.registerObserver(paramContentObserver);
  }
}
