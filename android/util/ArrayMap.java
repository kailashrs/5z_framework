package android.util;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import libcore.util.EmptyArray;

public final class ArrayMap<K, V>
  implements Map<K, V>
{
  private static final int BASE_SIZE = 4;
  private static final int CACHE_SIZE = 10;
  private static final boolean CONCURRENT_MODIFICATION_EXCEPTIONS = true;
  private static final boolean DEBUG = false;
  public static final ArrayMap EMPTY = new ArrayMap(-1);
  static final int[] EMPTY_IMMUTABLE_INTS = new int[0];
  private static final String TAG = "ArrayMap";
  static Object[] mBaseCache;
  static int mBaseCacheSize;
  static Object[] mTwiceBaseCache;
  static int mTwiceBaseCacheSize;
  Object[] mArray;
  MapCollections<K, V> mCollections;
  int[] mHashes;
  final boolean mIdentityHashCode;
  int mSize;
  
  public ArrayMap()
  {
    this(0, false);
  }
  
  public ArrayMap(int paramInt)
  {
    this(paramInt, false);
  }
  
  public ArrayMap(int paramInt, boolean paramBoolean)
  {
    mIdentityHashCode = paramBoolean;
    if (paramInt < 0)
    {
      mHashes = EMPTY_IMMUTABLE_INTS;
      mArray = EmptyArray.OBJECT;
    }
    else if (paramInt == 0)
    {
      mHashes = EmptyArray.INT;
      mArray = EmptyArray.OBJECT;
    }
    else
    {
      allocArrays(paramInt);
    }
    mSize = 0;
  }
  
  public ArrayMap(ArrayMap<K, V> paramArrayMap)
  {
    this();
    if (paramArrayMap != null) {
      putAll(paramArrayMap);
    }
  }
  
  private void allocArrays(int paramInt)
  {
    if (mHashes != EMPTY_IMMUTABLE_INTS)
    {
      if (paramInt == 8) {
        try
        {
          if (mTwiceBaseCache != null)
          {
            Object[] arrayOfObject1 = mTwiceBaseCache;
            mArray = arrayOfObject1;
            mTwiceBaseCache = (Object[])arrayOfObject1[0];
            mHashes = ((int[])arrayOfObject1[1]);
            arrayOfObject1[1] = null;
            arrayOfObject1[0] = null;
            mTwiceBaseCacheSize -= 1;
            return;
          }
        }
        finally {}
      }
      if (paramInt == 4) {
        try
        {
          if (mBaseCache != null)
          {
            Object[] arrayOfObject2 = mBaseCache;
            mArray = arrayOfObject2;
            mBaseCache = (Object[])arrayOfObject2[0];
            mHashes = ((int[])arrayOfObject2[1]);
            arrayOfObject2[1] = null;
            arrayOfObject2[0] = null;
            mBaseCacheSize -= 1;
            return;
          }
        }
        finally {}
      }
      mHashes = new int[paramInt];
      mArray = new Object[paramInt << 1];
      return;
    }
    throw new UnsupportedOperationException("ArrayMap is immutable");
  }
  
  private static int binarySearchHashes(int[] paramArrayOfInt, int paramInt1, int paramInt2)
  {
    try
    {
      paramInt1 = ContainerHelpers.binarySearch(paramArrayOfInt, paramInt1, paramInt2);
      return paramInt1;
    }
    catch (ArrayIndexOutOfBoundsException paramArrayOfInt)
    {
      throw new ConcurrentModificationException();
    }
  }
  
  private static void freeArrays(int[] paramArrayOfInt, Object[] paramArrayOfObject, int paramInt)
  {
    if (paramArrayOfInt.length == 8) {
      try
      {
        if (mTwiceBaseCacheSize < 10)
        {
          paramArrayOfObject[0] = mTwiceBaseCache;
          paramArrayOfObject[1] = paramArrayOfInt;
          for (paramInt = (paramInt << 1) - 1; paramInt >= 2; paramInt--) {
            paramArrayOfObject[paramInt] = null;
          }
          mTwiceBaseCache = paramArrayOfObject;
          mTwiceBaseCacheSize += 1;
        }
      }
      finally {}
    }
    if (paramArrayOfInt.length == 4) {
      try
      {
        if (mBaseCacheSize < 10)
        {
          paramArrayOfObject[0] = mBaseCache;
          paramArrayOfObject[1] = paramArrayOfInt;
          for (paramInt = (paramInt << 1) - 1; paramInt >= 2; paramInt--) {
            paramArrayOfObject[paramInt] = null;
          }
          mBaseCache = paramArrayOfObject;
          mBaseCacheSize += 1;
        }
      }
      finally {}
    }
  }
  
  private MapCollections<K, V> getCollection()
  {
    if (mCollections == null) {
      mCollections = new MapCollections()
      {
        protected void colClear()
        {
          clear();
        }
        
        protected Object colGetEntry(int paramAnonymousInt1, int paramAnonymousInt2)
        {
          return mArray[((paramAnonymousInt1 << 1) + paramAnonymousInt2)];
        }
        
        protected Map<K, V> colGetMap()
        {
          return ArrayMap.this;
        }
        
        protected int colGetSize()
        {
          return mSize;
        }
        
        protected int colIndexOfKey(Object paramAnonymousObject)
        {
          return indexOfKey(paramAnonymousObject);
        }
        
        protected int colIndexOfValue(Object paramAnonymousObject)
        {
          return indexOfValue(paramAnonymousObject);
        }
        
        protected void colPut(K paramAnonymousK, V paramAnonymousV)
        {
          put(paramAnonymousK, paramAnonymousV);
        }
        
        protected void colRemoveAt(int paramAnonymousInt)
        {
          removeAt(paramAnonymousInt);
        }
        
        protected V colSetValue(int paramAnonymousInt, V paramAnonymousV)
        {
          return setValueAt(paramAnonymousInt, paramAnonymousV);
        }
      };
    }
    return mCollections;
  }
  
  public void append(K paramK, V paramV)
  {
    int i = mSize;
    int j;
    if (paramK == null) {
      j = 0;
    } else if (mIdentityHashCode) {
      j = System.identityHashCode(paramK);
    } else {
      j = paramK.hashCode();
    }
    if (i < mHashes.length)
    {
      if ((i > 0) && (mHashes[(i - 1)] > j))
      {
        RuntimeException localRuntimeException = new RuntimeException("here");
        localRuntimeException.fillInStackTrace();
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("New hash ");
        localStringBuilder.append(j);
        localStringBuilder.append(" is before end of array hash ");
        localStringBuilder.append(mHashes[(i - 1)]);
        localStringBuilder.append(" at index ");
        localStringBuilder.append(i);
        localStringBuilder.append(" key ");
        localStringBuilder.append(paramK);
        Log.w("ArrayMap", localStringBuilder.toString(), localRuntimeException);
        put(paramK, paramV);
        return;
      }
      mSize = (i + 1);
      mHashes[i] = j;
      j = i << 1;
      mArray[j] = paramK;
      mArray[(j + 1)] = paramV;
      return;
    }
    throw new IllegalStateException("Array is full");
  }
  
  public void clear()
  {
    if (mSize > 0)
    {
      int[] arrayOfInt = mHashes;
      Object[] arrayOfObject = mArray;
      int i = mSize;
      mHashes = EmptyArray.INT;
      mArray = EmptyArray.OBJECT;
      mSize = 0;
      freeArrays(arrayOfInt, arrayOfObject, i);
    }
    if (mSize <= 0) {
      return;
    }
    throw new ConcurrentModificationException();
  }
  
  public boolean containsAll(Collection<?> paramCollection)
  {
    return MapCollections.containsAllHelper(this, paramCollection);
  }
  
  public boolean containsKey(Object paramObject)
  {
    boolean bool;
    if (indexOfKey(paramObject) >= 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean containsValue(Object paramObject)
  {
    boolean bool;
    if (indexOfValue(paramObject) >= 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void ensureCapacity(int paramInt)
  {
    int i = mSize;
    if (mHashes.length < paramInt)
    {
      int[] arrayOfInt = mHashes;
      Object[] arrayOfObject = mArray;
      allocArrays(paramInt);
      if (mSize > 0)
      {
        System.arraycopy(arrayOfInt, 0, mHashes, 0, i);
        System.arraycopy(arrayOfObject, 0, mArray, 0, i << 1);
      }
      freeArrays(arrayOfInt, arrayOfObject, i);
    }
    if (mSize == i) {
      return;
    }
    throw new ConcurrentModificationException();
  }
  
  public Set<Map.Entry<K, V>> entrySet()
  {
    return getCollection().getEntrySet();
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if ((paramObject instanceof Map))
    {
      paramObject = (Map)paramObject;
      if (size() != paramObject.size()) {
        return false;
      }
      int i = 0;
      try
      {
        while (i < mSize)
        {
          Object localObject1 = keyAt(i);
          Object localObject2 = valueAt(i);
          Object localObject3 = paramObject.get(localObject1);
          if (localObject2 == null)
          {
            if ((localObject3 != null) || (!paramObject.containsKey(localObject1))) {
              return false;
            }
          }
          else
          {
            boolean bool = localObject2.equals(localObject3);
            if (!bool) {
              return false;
            }
          }
          i++;
        }
        return true;
      }
      catch (ClassCastException paramObject)
      {
        return false;
      }
      catch (NullPointerException paramObject)
      {
        return false;
      }
    }
    return false;
  }
  
  public void erase()
  {
    if (mSize > 0)
    {
      int i = mSize;
      Object[] arrayOfObject = mArray;
      for (int j = 0; j < i << 1; j++) {
        arrayOfObject[j] = null;
      }
      mSize = 0;
    }
  }
  
  public V get(Object paramObject)
  {
    int i = indexOfKey(paramObject);
    if (i >= 0) {
      paramObject = mArray[((i << 1) + 1)];
    } else {
      paramObject = null;
    }
    return paramObject;
  }
  
  public int hashCode()
  {
    int[] arrayOfInt = mHashes;
    Object[] arrayOfObject = mArray;
    int i = 0;
    int j = 0;
    int k = 1;
    int m = mSize;
    while (j < m)
    {
      Object localObject = arrayOfObject[k];
      int n = arrayOfInt[j];
      int i1;
      if (localObject == null) {
        i1 = 0;
      } else {
        i1 = localObject.hashCode();
      }
      i += (n ^ i1);
      j++;
      k += 2;
    }
    return i;
  }
  
  int indexOf(Object paramObject, int paramInt)
  {
    int i = mSize;
    if (i == 0) {
      return -1;
    }
    int j = binarySearchHashes(mHashes, i, paramInt);
    if (j < 0) {
      return j;
    }
    if (paramObject.equals(mArray[(j << 1)])) {
      return j;
    }
    for (int k = j + 1; (k < i) && (mHashes[k] == paramInt); k++) {
      if (paramObject.equals(mArray[(k << 1)])) {
        return k;
      }
    }
    j--;
    while ((j >= 0) && (mHashes[j] == paramInt))
    {
      if (paramObject.equals(mArray[(j << 1)])) {
        return j;
      }
      j--;
    }
    return k;
  }
  
  public int indexOfKey(Object paramObject)
  {
    int i;
    if (paramObject == null)
    {
      i = indexOfNull();
    }
    else
    {
      if (mIdentityHashCode) {
        i = System.identityHashCode(paramObject);
      } else {
        i = paramObject.hashCode();
      }
      i = indexOf(paramObject, i);
    }
    return i;
  }
  
  int indexOfNull()
  {
    int i = mSize;
    if (i == 0) {
      return -1;
    }
    int j = binarySearchHashes(mHashes, i, 0);
    if (j < 0) {
      return j;
    }
    if (mArray[(j << 1)] == null) {
      return j;
    }
    for (int k = j + 1; (k < i) && (mHashes[k] == 0); k++) {
      if (mArray[(k << 1)] == null) {
        return k;
      }
    }
    for (i = j - 1; (i >= 0) && (mHashes[i] == 0); i--) {
      if (mArray[(i << 1)] == null) {
        return i;
      }
    }
    return k;
  }
  
  int indexOfValue(Object paramObject)
  {
    int i = mSize * 2;
    Object[] arrayOfObject = mArray;
    int j = 1;
    int k = 1;
    if (paramObject == null) {
      while (k < i)
      {
        if (arrayOfObject[k] == null) {
          return k >> 1;
        }
        k += 2;
      }
    }
    for (k = j; k < i; k += 2) {
      if (paramObject.equals(arrayOfObject[k])) {
        return k >> 1;
      }
    }
    return -1;
  }
  
  public boolean isEmpty()
  {
    boolean bool;
    if (mSize <= 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public K keyAt(int paramInt)
  {
    return mArray[(paramInt << 1)];
  }
  
  public Set<K> keySet()
  {
    return getCollection().getKeySet();
  }
  
  public V put(K paramK, V paramV)
  {
    int i = mSize;
    int j;
    int m;
    if (paramK == null)
    {
      j = 0;
      k = indexOfNull();
    }
    else
    {
      if (mIdentityHashCode) {
        m = System.identityHashCode(paramK);
      } else {
        m = paramK.hashCode();
      }
      k = indexOf(paramK, m);
      j = m;
    }
    if (k >= 0)
    {
      m = (k << 1) + 1;
      paramK = mArray[m];
      mArray[m] = paramV;
      return paramK;
    }
    int k = k;
    if (i >= mHashes.length)
    {
      m = 4;
      if (i >= 8) {
        m = (i >> 1) + i;
      } else if (i >= 4) {
        m = 8;
      }
      int[] arrayOfInt = mHashes;
      Object[] arrayOfObject = mArray;
      allocArrays(m);
      if (i == mSize)
      {
        if (mHashes.length > 0)
        {
          System.arraycopy(arrayOfInt, 0, mHashes, 0, arrayOfInt.length);
          System.arraycopy(arrayOfObject, 0, mArray, 0, arrayOfObject.length);
        }
        freeArrays(arrayOfInt, arrayOfObject, i);
      }
      else
      {
        throw new ConcurrentModificationException();
      }
    }
    if (k < i)
    {
      System.arraycopy(mHashes, k, mHashes, k + 1, i - k);
      System.arraycopy(mArray, k << 1, mArray, k + 1 << 1, mSize - k << 1);
    }
    if ((i == mSize) && (k < mHashes.length))
    {
      mHashes[k] = j;
      mArray[(k << 1)] = paramK;
      mArray[((k << 1) + 1)] = paramV;
      mSize += 1;
      return null;
    }
    throw new ConcurrentModificationException();
  }
  
  public void putAll(ArrayMap<? extends K, ? extends V> paramArrayMap)
  {
    int i = mSize;
    ensureCapacity(mSize + i);
    int j = mSize;
    int k = 0;
    if (j == 0)
    {
      if (i > 0)
      {
        System.arraycopy(mHashes, 0, mHashes, 0, i);
        System.arraycopy(mArray, 0, mArray, 0, i << 1);
        mSize = i;
      }
    }
    else {
      while (k < i)
      {
        put(paramArrayMap.keyAt(k), paramArrayMap.valueAt(k));
        k++;
      }
    }
  }
  
  public void putAll(Map<? extends K, ? extends V> paramMap)
  {
    ensureCapacity(mSize + paramMap.size());
    paramMap = paramMap.entrySet().iterator();
    while (paramMap.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)paramMap.next();
      put(localEntry.getKey(), localEntry.getValue());
    }
  }
  
  public V remove(Object paramObject)
  {
    int i = indexOfKey(paramObject);
    if (i >= 0) {
      return removeAt(i);
    }
    return null;
  }
  
  public boolean removeAll(Collection<?> paramCollection)
  {
    return MapCollections.removeAllHelper(this, paramCollection);
  }
  
  public V removeAt(int paramInt)
  {
    Object localObject1 = mArray[((paramInt << 1) + 1)];
    int i = mSize;
    Object localObject2;
    Object localObject3;
    if (i <= 1)
    {
      localObject2 = mHashes;
      localObject3 = mArray;
      mHashes = EmptyArray.INT;
      mArray = EmptyArray.OBJECT;
      freeArrays((int[])localObject2, (Object[])localObject3, i);
      paramInt = 0;
    }
    else
    {
      int j = i - 1;
      int k = mHashes.length;
      int m = 8;
      if ((k > 8) && (mSize < mHashes.length / 3))
      {
        if (i > 8) {
          m = i + (i >> 1);
        }
        localObject3 = mHashes;
        localObject2 = mArray;
        allocArrays(m);
        if (i == mSize)
        {
          if (paramInt > 0)
          {
            System.arraycopy(localObject3, 0, mHashes, 0, paramInt);
            System.arraycopy(localObject2, 0, mArray, 0, paramInt << 1);
          }
          if (paramInt < j)
          {
            System.arraycopy(localObject3, paramInt + 1, mHashes, paramInt, j - paramInt);
            System.arraycopy(localObject2, paramInt + 1 << 1, mArray, paramInt << 1, j - paramInt << 1);
          }
        }
        else
        {
          throw new ConcurrentModificationException();
        }
      }
      else
      {
        if (paramInt < j)
        {
          System.arraycopy(mHashes, paramInt + 1, mHashes, paramInt, j - paramInt);
          System.arraycopy(mArray, paramInt + 1 << 1, mArray, paramInt << 1, j - paramInt << 1);
        }
        mArray[(j << 1)] = null;
        mArray[((j << 1) + 1)] = null;
      }
      paramInt = j;
    }
    if (i == mSize)
    {
      mSize = paramInt;
      return localObject1;
    }
    throw new ConcurrentModificationException();
  }
  
  public boolean retainAll(Collection<?> paramCollection)
  {
    return MapCollections.retainAllHelper(this, paramCollection);
  }
  
  public V setValueAt(int paramInt, V paramV)
  {
    paramInt = (paramInt << 1) + 1;
    Object localObject = mArray[paramInt];
    mArray[paramInt] = paramV;
    return localObject;
  }
  
  public int size()
  {
    return mSize;
  }
  
  public String toString()
  {
    if (isEmpty()) {
      return "{}";
    }
    StringBuilder localStringBuilder = new StringBuilder(mSize * 28);
    localStringBuilder.append('{');
    for (int i = 0; i < mSize; i++)
    {
      if (i > 0) {
        localStringBuilder.append(", ");
      }
      Object localObject = keyAt(i);
      if (localObject != this) {
        localStringBuilder.append(localObject);
      } else {
        localStringBuilder.append("(this Map)");
      }
      localStringBuilder.append('=');
      localObject = valueAt(i);
      if (localObject != this) {
        localStringBuilder.append(localObject);
      } else {
        localStringBuilder.append("(this Map)");
      }
    }
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void validate()
  {
    int i = mSize;
    int j = 1;
    if (i <= 1) {
      return;
    }
    int k = mHashes[0];
    int i1;
    for (int m = 0; j < i; m = i1)
    {
      int n = mHashes[j];
      if (n != k)
      {
        i1 = j;
      }
      else
      {
        Object localObject1 = mArray[(j << 1)];
        for (int i2 = j - 1;; i2--)
        {
          n = k;
          i1 = m;
          if (i2 < m) {
            break label202;
          }
          localObject2 = mArray[(i2 << 1)];
          if (localObject1 == localObject2) {
            break;
          }
          if ((localObject1 != null) && (localObject2 != null) && (localObject1.equals(localObject2)))
          {
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("Duplicate key in ArrayMap: ");
            ((StringBuilder)localObject2).append(localObject1);
            throw new IllegalArgumentException(((StringBuilder)localObject2).toString());
          }
        }
        Object localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Duplicate key in ArrayMap: ");
        ((StringBuilder)localObject2).append(localObject1);
        throw new IllegalArgumentException(((StringBuilder)localObject2).toString());
      }
      label202:
      j++;
      k = n;
    }
  }
  
  public V valueAt(int paramInt)
  {
    return mArray[((paramInt << 1) + 1)];
  }
  
  public Collection<V> values()
  {
    return getCollection().getValues();
  }
}
