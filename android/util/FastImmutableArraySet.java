package android.util;

import java.util.AbstractSet;
import java.util.Iterator;

public final class FastImmutableArraySet<T>
  extends AbstractSet<T>
{
  T[] mContents;
  FastIterator<T> mIterator;
  
  public FastImmutableArraySet(T[] paramArrayOfT)
  {
    mContents = paramArrayOfT;
  }
  
  public Iterator<T> iterator()
  {
    FastIterator localFastIterator = mIterator;
    if (localFastIterator == null)
    {
      localFastIterator = new FastIterator(mContents);
      mIterator = localFastIterator;
    }
    else
    {
      mIndex = 0;
    }
    return localFastIterator;
  }
  
  public int size()
  {
    return mContents.length;
  }
  
  private static final class FastIterator<T>
    implements Iterator<T>
  {
    private final T[] mContents;
    int mIndex;
    
    public FastIterator(T[] paramArrayOfT)
    {
      mContents = paramArrayOfT;
    }
    
    public boolean hasNext()
    {
      boolean bool;
      if (mIndex != mContents.length) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public T next()
    {
      Object[] arrayOfObject = mContents;
      int i = mIndex;
      mIndex = (i + 1);
      return arrayOfObject[i];
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
}
