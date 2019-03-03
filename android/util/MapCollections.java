package android.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

abstract class MapCollections<K, V>
{
  MapCollections<K, V>.EntrySet mEntrySet;
  MapCollections<K, V>.KeySet mKeySet;
  MapCollections<K, V>.ValuesCollection mValues;
  
  MapCollections() {}
  
  public static <K, V> boolean containsAllHelper(Map<K, V> paramMap, Collection<?> paramCollection)
  {
    paramCollection = paramCollection.iterator();
    while (paramCollection.hasNext()) {
      if (!paramMap.containsKey(paramCollection.next())) {
        return false;
      }
    }
    return true;
  }
  
  public static <T> boolean equalsSetHelper(Set<T> paramSet, Object paramObject)
  {
    boolean bool1 = true;
    if (paramSet == paramObject) {
      return true;
    }
    if ((paramObject instanceof Set))
    {
      paramObject = (Set)paramObject;
      try
      {
        if (paramSet.size() == paramObject.size())
        {
          boolean bool2 = paramSet.containsAll(paramObject);
          if (bool2) {}
        }
        else
        {
          bool1 = false;
        }
        return bool1;
      }
      catch (ClassCastException paramSet)
      {
        return false;
      }
      catch (NullPointerException paramSet)
      {
        return false;
      }
    }
    return false;
  }
  
  public static <K, V> boolean removeAllHelper(Map<K, V> paramMap, Collection<?> paramCollection)
  {
    int i = paramMap.size();
    paramCollection = paramCollection.iterator();
    while (paramCollection.hasNext()) {
      paramMap.remove(paramCollection.next());
    }
    boolean bool;
    if (i != paramMap.size()) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static <K, V> boolean retainAllHelper(Map<K, V> paramMap, Collection<?> paramCollection)
  {
    int i = paramMap.size();
    Iterator localIterator = paramMap.keySet().iterator();
    while (localIterator.hasNext()) {
      if (!paramCollection.contains(localIterator.next())) {
        localIterator.remove();
      }
    }
    boolean bool;
    if (i != paramMap.size()) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected abstract void colClear();
  
  protected abstract Object colGetEntry(int paramInt1, int paramInt2);
  
  protected abstract Map<K, V> colGetMap();
  
  protected abstract int colGetSize();
  
  protected abstract int colIndexOfKey(Object paramObject);
  
  protected abstract int colIndexOfValue(Object paramObject);
  
  protected abstract void colPut(K paramK, V paramV);
  
  protected abstract void colRemoveAt(int paramInt);
  
  protected abstract V colSetValue(int paramInt, V paramV);
  
  public Set<Map.Entry<K, V>> getEntrySet()
  {
    if (mEntrySet == null) {
      mEntrySet = new EntrySet();
    }
    return mEntrySet;
  }
  
  public Set<K> getKeySet()
  {
    if (mKeySet == null) {
      mKeySet = new KeySet();
    }
    return mKeySet;
  }
  
  public Collection<V> getValues()
  {
    if (mValues == null) {
      mValues = new ValuesCollection();
    }
    return mValues;
  }
  
  public Object[] toArrayHelper(int paramInt)
  {
    int i = colGetSize();
    Object[] arrayOfObject = new Object[i];
    for (int j = 0; j < i; j++) {
      arrayOfObject[j] = colGetEntry(j, paramInt);
    }
    return arrayOfObject;
  }
  
  public <T> T[] toArrayHelper(T[] paramArrayOfT, int paramInt)
  {
    int i = colGetSize();
    Object localObject = paramArrayOfT;
    if (paramArrayOfT.length < i) {
      localObject = (Object[])Array.newInstance(paramArrayOfT.getClass().getComponentType(), i);
    }
    for (int j = 0; j < i; j++) {
      localObject[j] = colGetEntry(j, paramInt);
    }
    if (localObject.length > i) {
      localObject[i] = null;
    }
    return localObject;
  }
  
  final class ArrayIterator<T>
    implements Iterator<T>
  {
    boolean mCanRemove = false;
    int mIndex;
    final int mOffset;
    int mSize;
    
    ArrayIterator(int paramInt)
    {
      mOffset = paramInt;
      mSize = colGetSize();
    }
    
    public boolean hasNext()
    {
      boolean bool;
      if (mIndex < mSize) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public T next()
    {
      if (hasNext())
      {
        Object localObject = colGetEntry(mIndex, mOffset);
        mIndex += 1;
        mCanRemove = true;
        return localObject;
      }
      throw new NoSuchElementException();
    }
    
    public void remove()
    {
      if (mCanRemove)
      {
        mIndex -= 1;
        mSize -= 1;
        mCanRemove = false;
        colRemoveAt(mIndex);
        return;
      }
      throw new IllegalStateException();
    }
  }
  
  final class EntrySet
    implements Set<Map.Entry<K, V>>
  {
    EntrySet() {}
    
    public boolean add(Map.Entry<K, V> paramEntry)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean addAll(Collection<? extends Map.Entry<K, V>> paramCollection)
    {
      int i = colGetSize();
      paramCollection = paramCollection.iterator();
      while (paramCollection.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)paramCollection.next();
        colPut(localEntry.getKey(), localEntry.getValue());
      }
      boolean bool;
      if (i != colGetSize()) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void clear()
    {
      colClear();
    }
    
    public boolean contains(Object paramObject)
    {
      if (!(paramObject instanceof Map.Entry)) {
        return false;
      }
      paramObject = (Map.Entry)paramObject;
      int i = colIndexOfKey(paramObject.getKey());
      if (i < 0) {
        return false;
      }
      return Objects.equals(colGetEntry(i, 1), paramObject.getValue());
    }
    
    public boolean containsAll(Collection<?> paramCollection)
    {
      paramCollection = paramCollection.iterator();
      while (paramCollection.hasNext()) {
        if (!contains(paramCollection.next())) {
          return false;
        }
      }
      return true;
    }
    
    public boolean equals(Object paramObject)
    {
      return MapCollections.equalsSetHelper(this, paramObject);
    }
    
    public int hashCode()
    {
      int i = 0;
      for (int j = colGetSize() - 1; j >= 0; j--)
      {
        Object localObject1 = MapCollections.this;
        int k = 0;
        Object localObject2 = ((MapCollections)localObject1).colGetEntry(j, 0);
        localObject1 = colGetEntry(j, 1);
        int m;
        if (localObject2 == null) {
          m = 0;
        } else {
          m = localObject2.hashCode();
        }
        if (localObject1 != null) {
          k = localObject1.hashCode();
        }
        i += (k ^ m);
      }
      return i;
    }
    
    public boolean isEmpty()
    {
      boolean bool;
      if (colGetSize() == 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public Iterator<Map.Entry<K, V>> iterator()
    {
      return new MapCollections.MapIterator(MapCollections.this);
    }
    
    public boolean remove(Object paramObject)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean removeAll(Collection<?> paramCollection)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean retainAll(Collection<?> paramCollection)
    {
      throw new UnsupportedOperationException();
    }
    
    public int size()
    {
      return colGetSize();
    }
    
    public Object[] toArray()
    {
      throw new UnsupportedOperationException();
    }
    
    public <T> T[] toArray(T[] paramArrayOfT)
    {
      throw new UnsupportedOperationException();
    }
  }
  
  final class KeySet
    implements Set<K>
  {
    KeySet() {}
    
    public boolean add(K paramK)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean addAll(Collection<? extends K> paramCollection)
    {
      throw new UnsupportedOperationException();
    }
    
    public void clear()
    {
      colClear();
    }
    
    public boolean contains(Object paramObject)
    {
      boolean bool;
      if (colIndexOfKey(paramObject) >= 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean containsAll(Collection<?> paramCollection)
    {
      return MapCollections.containsAllHelper(colGetMap(), paramCollection);
    }
    
    public boolean equals(Object paramObject)
    {
      return MapCollections.equalsSetHelper(this, paramObject);
    }
    
    public int hashCode()
    {
      int i = 0;
      for (int j = colGetSize() - 1; j >= 0; j--)
      {
        Object localObject = MapCollections.this;
        int k = 0;
        localObject = ((MapCollections)localObject).colGetEntry(j, 0);
        if (localObject != null) {
          k = localObject.hashCode();
        }
        i += k;
      }
      return i;
    }
    
    public boolean isEmpty()
    {
      boolean bool;
      if (colGetSize() == 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public Iterator<K> iterator()
    {
      return new MapCollections.ArrayIterator(MapCollections.this, 0);
    }
    
    public boolean remove(Object paramObject)
    {
      int i = colIndexOfKey(paramObject);
      if (i >= 0)
      {
        colRemoveAt(i);
        return true;
      }
      return false;
    }
    
    public boolean removeAll(Collection<?> paramCollection)
    {
      return MapCollections.removeAllHelper(colGetMap(), paramCollection);
    }
    
    public boolean retainAll(Collection<?> paramCollection)
    {
      return MapCollections.retainAllHelper(colGetMap(), paramCollection);
    }
    
    public int size()
    {
      return colGetSize();
    }
    
    public Object[] toArray()
    {
      return toArrayHelper(0);
    }
    
    public <T> T[] toArray(T[] paramArrayOfT)
    {
      return toArrayHelper(paramArrayOfT, 0);
    }
  }
  
  final class MapIterator
    implements Iterator<Map.Entry<K, V>>, Map.Entry<K, V>
  {
    int mEnd = colGetSize() - 1;
    boolean mEntryValid = false;
    int mIndex = -1;
    
    MapIterator() {}
    
    public final boolean equals(Object paramObject)
    {
      if (mEntryValid)
      {
        boolean bool1 = paramObject instanceof Map.Entry;
        boolean bool2 = false;
        if (!bool1) {
          return false;
        }
        paramObject = (Map.Entry)paramObject;
        if ((Objects.equals(paramObject.getKey(), colGetEntry(mIndex, 0))) && (Objects.equals(paramObject.getValue(), colGetEntry(mIndex, 1)))) {
          bool2 = true;
        }
        return bool2;
      }
      throw new IllegalStateException("This container does not support retaining Map.Entry objects");
    }
    
    public K getKey()
    {
      if (mEntryValid) {
        return colGetEntry(mIndex, 0);
      }
      throw new IllegalStateException("This container does not support retaining Map.Entry objects");
    }
    
    public V getValue()
    {
      if (mEntryValid) {
        return colGetEntry(mIndex, 1);
      }
      throw new IllegalStateException("This container does not support retaining Map.Entry objects");
    }
    
    public boolean hasNext()
    {
      boolean bool;
      if (mIndex < mEnd) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public final int hashCode()
    {
      if (mEntryValid)
      {
        Object localObject1 = MapCollections.this;
        int i = mIndex;
        int j = 0;
        Object localObject2 = ((MapCollections)localObject1).colGetEntry(i, 0);
        localObject1 = colGetEntry(mIndex, 1);
        if (localObject2 == null) {
          i = 0;
        } else {
          i = localObject2.hashCode();
        }
        if (localObject1 != null) {
          j = localObject1.hashCode();
        }
        return j ^ i;
      }
      throw new IllegalStateException("This container does not support retaining Map.Entry objects");
    }
    
    public Map.Entry<K, V> next()
    {
      if (hasNext())
      {
        mIndex += 1;
        mEntryValid = true;
        return this;
      }
      throw new NoSuchElementException();
    }
    
    public void remove()
    {
      if (mEntryValid)
      {
        colRemoveAt(mIndex);
        mIndex -= 1;
        mEnd -= 1;
        mEntryValid = false;
        return;
      }
      throw new IllegalStateException();
    }
    
    public V setValue(V paramV)
    {
      if (mEntryValid) {
        return colSetValue(mIndex, paramV);
      }
      throw new IllegalStateException("This container does not support retaining Map.Entry objects");
    }
    
    public final String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(getKey());
      localStringBuilder.append("=");
      localStringBuilder.append(getValue());
      return localStringBuilder.toString();
    }
  }
  
  final class ValuesCollection
    implements Collection<V>
  {
    ValuesCollection() {}
    
    public boolean add(V paramV)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean addAll(Collection<? extends V> paramCollection)
    {
      throw new UnsupportedOperationException();
    }
    
    public void clear()
    {
      colClear();
    }
    
    public boolean contains(Object paramObject)
    {
      boolean bool;
      if (colIndexOfValue(paramObject) >= 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean containsAll(Collection<?> paramCollection)
    {
      paramCollection = paramCollection.iterator();
      while (paramCollection.hasNext()) {
        if (!contains(paramCollection.next())) {
          return false;
        }
      }
      return true;
    }
    
    public boolean isEmpty()
    {
      boolean bool;
      if (colGetSize() == 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public Iterator<V> iterator()
    {
      return new MapCollections.ArrayIterator(MapCollections.this, 1);
    }
    
    public boolean remove(Object paramObject)
    {
      int i = colIndexOfValue(paramObject);
      if (i >= 0)
      {
        colRemoveAt(i);
        return true;
      }
      return false;
    }
    
    public boolean removeAll(Collection<?> paramCollection)
    {
      int i = colGetSize();
      boolean bool = false;
      int j = 0;
      while (j < i)
      {
        int k = i;
        int m = j;
        if (paramCollection.contains(colGetEntry(j, 1)))
        {
          colRemoveAt(j);
          m = j - 1;
          k = i - 1;
          bool = true;
        }
        j = m + 1;
        i = k;
      }
      return bool;
    }
    
    public boolean retainAll(Collection<?> paramCollection)
    {
      int i = colGetSize();
      boolean bool = false;
      int j = 0;
      while (j < i)
      {
        int k = i;
        int m = j;
        if (!paramCollection.contains(colGetEntry(j, 1)))
        {
          colRemoveAt(j);
          m = j - 1;
          k = i - 1;
          bool = true;
        }
        j = m + 1;
        i = k;
      }
      return bool;
    }
    
    public int size()
    {
      return colGetSize();
    }
    
    public Object[] toArray()
    {
      return toArrayHelper(1);
    }
    
    public <T> T[] toArray(T[] paramArrayOfT)
    {
      return toArrayHelper(paramArrayOfT, 1);
    }
  }
}
