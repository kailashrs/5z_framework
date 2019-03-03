package android.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import libcore.util.EmptyArray;

public final class ArraySet<E>
  implements Collection<E>, Set<E>
{
  private static final int BASE_SIZE = 4;
  private static final int CACHE_SIZE = 10;
  private static final boolean DEBUG = false;
  private static final String TAG = "ArraySet";
  static Object[] sBaseCache;
  static int sBaseCacheSize;
  static Object[] sTwiceBaseCache;
  static int sTwiceBaseCacheSize;
  Object[] mArray;
  MapCollections<E, E> mCollections;
  int[] mHashes;
  final boolean mIdentityHashCode;
  int mSize;
  
  public ArraySet()
  {
    this(0, false);
  }
  
  public ArraySet(int paramInt)
  {
    this(paramInt, false);
  }
  
  public ArraySet(int paramInt, boolean paramBoolean)
  {
    mIdentityHashCode = paramBoolean;
    if (paramInt == 0)
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
  
  public ArraySet(ArraySet<E> paramArraySet)
  {
    this();
    if (paramArraySet != null) {
      addAll(paramArraySet);
    }
  }
  
  public ArraySet(Collection<E> paramCollection)
  {
    this();
    if (paramCollection != null) {
      addAll(paramCollection);
    }
  }
  
  private void allocArrays(int paramInt)
  {
    if (paramInt == 8) {
      try
      {
        if (sTwiceBaseCache != null)
        {
          Object[] arrayOfObject1 = sTwiceBaseCache;
          try
          {
            mArray = arrayOfObject1;
            sTwiceBaseCache = (Object[])arrayOfObject1[0];
            mHashes = ((int[])arrayOfObject1[1]);
            arrayOfObject1[1] = null;
            arrayOfObject1[0] = null;
            sTwiceBaseCacheSize -= 1;
            return;
          }
          catch (ClassCastException localClassCastException1)
          {
            StringBuilder localStringBuilder1 = new java/lang/StringBuilder;
            localStringBuilder1.<init>();
            localStringBuilder1.append("Found corrupt ArraySet cache: [0]=");
            localStringBuilder1.append(arrayOfObject1[0]);
            localStringBuilder1.append(" [1]=");
            localStringBuilder1.append(arrayOfObject1[1]);
            Slog.wtf("ArraySet", localStringBuilder1.toString());
            sTwiceBaseCache = null;
            sTwiceBaseCacheSize = 0;
          }
        }
      }
      finally {}
    }
    if (paramInt == 4) {
      try
      {
        if (sBaseCache != null)
        {
          Object[] arrayOfObject2 = sBaseCache;
          try
          {
            mArray = arrayOfObject2;
            sBaseCache = (Object[])arrayOfObject2[0];
            mHashes = ((int[])arrayOfObject2[1]);
            arrayOfObject2[1] = null;
            arrayOfObject2[0] = null;
            sBaseCacheSize -= 1;
            return;
          }
          catch (ClassCastException localClassCastException2)
          {
            StringBuilder localStringBuilder2 = new java/lang/StringBuilder;
            localStringBuilder2.<init>();
            localStringBuilder2.append("Found corrupt ArraySet cache: [0]=");
            localStringBuilder2.append(arrayOfObject2[0]);
            localStringBuilder2.append(" [1]=");
            localStringBuilder2.append(arrayOfObject2[1]);
            Slog.wtf("ArraySet", localStringBuilder2.toString());
            sBaseCache = null;
            sBaseCacheSize = 0;
          }
        }
      }
      finally {}
    }
    mHashes = new int[paramInt];
    mArray = new Object[paramInt];
  }
  
  private static void freeArrays(int[] paramArrayOfInt, Object[] paramArrayOfObject, int paramInt)
  {
    if (paramArrayOfInt.length == 8) {
      try
      {
        if (sTwiceBaseCacheSize < 10)
        {
          paramArrayOfObject[0] = sTwiceBaseCache;
          paramArrayOfObject[1] = paramArrayOfInt;
          paramInt--;
          while (paramInt >= 2)
          {
            paramArrayOfObject[paramInt] = null;
            paramInt--;
          }
          sTwiceBaseCache = paramArrayOfObject;
          sTwiceBaseCacheSize += 1;
        }
      }
      finally {}
    }
    if (paramArrayOfInt.length == 4) {
      try
      {
        if (sBaseCacheSize < 10)
        {
          paramArrayOfObject[0] = sBaseCache;
          paramArrayOfObject[1] = paramArrayOfInt;
          paramInt--;
          while (paramInt >= 2)
          {
            paramArrayOfObject[paramInt] = null;
            paramInt--;
          }
          sBaseCache = paramArrayOfObject;
          sBaseCacheSize += 1;
        }
      }
      finally {}
    }
  }
  
  private MapCollections<E, E> getCollection()
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
          return mArray[paramAnonymousInt1];
        }
        
        protected Map<E, E> colGetMap()
        {
          throw new UnsupportedOperationException("not a map");
        }
        
        protected int colGetSize()
        {
          return mSize;
        }
        
        protected int colIndexOfKey(Object paramAnonymousObject)
        {
          return indexOf(paramAnonymousObject);
        }
        
        protected int colIndexOfValue(Object paramAnonymousObject)
        {
          return indexOf(paramAnonymousObject);
        }
        
        protected void colPut(E paramAnonymousE1, E paramAnonymousE2)
        {
          add(paramAnonymousE1);
        }
        
        protected void colRemoveAt(int paramAnonymousInt)
        {
          removeAt(paramAnonymousInt);
        }
        
        protected E colSetValue(int paramAnonymousInt, E paramAnonymousE)
        {
          throw new UnsupportedOperationException("not a map");
        }
      };
    }
    return mCollections;
  }
  
  private int indexOf(Object paramObject, int paramInt)
  {
    int i = mSize;
    if (i == 0) {
      return -1;
    }
    int j = ContainerHelpers.binarySearch(mHashes, i, paramInt);
    if (j < 0) {
      return j;
    }
    if (paramObject.equals(mArray[j])) {
      return j;
    }
    for (int k = j + 1; (k < i) && (mHashes[k] == paramInt); k++) {
      if (paramObject.equals(mArray[k])) {
        return k;
      }
    }
    j--;
    while ((j >= 0) && (mHashes[j] == paramInt))
    {
      if (paramObject.equals(mArray[j])) {
        return j;
      }
      j--;
    }
    return k;
  }
  
  private int indexOfNull()
  {
    int i = mSize;
    if (i == 0) {
      return -1;
    }
    int j = ContainerHelpers.binarySearch(mHashes, i, 0);
    if (j < 0) {
      return j;
    }
    if (mArray[j] == null) {
      return j;
    }
    for (int k = j + 1; (k < i) && (mHashes[k] == 0); k++) {
      if (mArray[k] == null) {
        return k;
      }
    }
    for (i = j - 1; (i >= 0) && (mHashes[i] == 0); i--) {
      if (mArray[i] == null) {
        return i;
      }
    }
    return k;
  }
  
  public boolean add(E paramE)
  {
    int i;
    int k;
    if (paramE == null)
    {
      i = 0;
      j = indexOfNull();
    }
    else
    {
      if (mIdentityHashCode) {
        k = System.identityHashCode(paramE);
      } else {
        k = paramE.hashCode();
      }
      j = indexOf(paramE, k);
      i = k;
    }
    if (j >= 0) {
      return false;
    }
    int j = j;
    if (mSize >= mHashes.length)
    {
      int m = mSize;
      k = 4;
      if (m >= 8)
      {
        k = mSize;
        k = (mSize >> 1) + k;
      }
      else if (mSize >= 4)
      {
        k = 8;
      }
      int[] arrayOfInt = mHashes;
      Object[] arrayOfObject = mArray;
      allocArrays(k);
      if (mHashes.length > 0)
      {
        System.arraycopy(arrayOfInt, 0, mHashes, 0, arrayOfInt.length);
        System.arraycopy(arrayOfObject, 0, mArray, 0, arrayOfObject.length);
      }
      freeArrays(arrayOfInt, arrayOfObject, mSize);
    }
    if (j < mSize)
    {
      System.arraycopy(mHashes, j, mHashes, j + 1, mSize - j);
      System.arraycopy(mArray, j, mArray, j + 1, mSize - j);
    }
    mHashes[j] = i;
    mArray[j] = paramE;
    mSize += 1;
    return true;
  }
  
  public void addAll(ArraySet<? extends E> paramArraySet)
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
        System.arraycopy(mArray, 0, mArray, 0, i);
        mSize = i;
      }
    }
    else {
      while (k < i)
      {
        add(paramArraySet.valueAt(k));
        k++;
      }
    }
  }
  
  public boolean addAll(Collection<? extends E> paramCollection)
  {
    ensureCapacity(mSize + paramCollection.size());
    boolean bool = false;
    paramCollection = paramCollection.iterator();
    while (paramCollection.hasNext()) {
      bool |= add(paramCollection.next());
    }
    return bool;
  }
  
  public void append(E paramE)
  {
    int i = mSize;
    int j;
    if (paramE == null) {
      j = 0;
    } else if (mIdentityHashCode) {
      j = System.identityHashCode(paramE);
    } else {
      j = paramE.hashCode();
    }
    if (i < mHashes.length)
    {
      if ((i > 0) && (mHashes[(i - 1)] > j))
      {
        add(paramE);
        return;
      }
      mSize = (i + 1);
      mHashes[i] = j;
      mArray[i] = paramE;
      return;
    }
    throw new IllegalStateException("Array is full");
  }
  
  public void clear()
  {
    if (mSize != 0)
    {
      freeArrays(mHashes, mArray, mSize);
      mHashes = EmptyArray.INT;
      mArray = EmptyArray.OBJECT;
      mSize = 0;
    }
  }
  
  public boolean contains(Object paramObject)
  {
    boolean bool;
    if (indexOf(paramObject) >= 0) {
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
  
  public void ensureCapacity(int paramInt)
  {
    if (mHashes.length < paramInt)
    {
      int[] arrayOfInt = mHashes;
      Object[] arrayOfObject = mArray;
      allocArrays(paramInt);
      if (mSize > 0)
      {
        System.arraycopy(arrayOfInt, 0, mHashes, 0, mSize);
        System.arraycopy(arrayOfObject, 0, mArray, 0, mSize);
      }
      freeArrays(arrayOfInt, arrayOfObject, mSize);
    }
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if ((paramObject instanceof Set))
    {
      paramObject = (Set)paramObject;
      if (size() != paramObject.size()) {
        return false;
      }
      int i = 0;
      try
      {
        while (i < mSize)
        {
          boolean bool = paramObject.contains(valueAt(i));
          if (!bool) {
            return false;
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
  
  public int hashCode()
  {
    int[] arrayOfInt = mHashes;
    int i = 0;
    int j = 0;
    int k = mSize;
    while (j < k)
    {
      i += arrayOfInt[j];
      j++;
    }
    return i;
  }
  
  public int indexOf(Object paramObject)
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
  
  public Iterator<E> iterator()
  {
    return getCollection().getKeySet().iterator();
  }
  
  public boolean remove(Object paramObject)
  {
    int i = indexOf(paramObject);
    if (i >= 0)
    {
      removeAt(i);
      return true;
    }
    return false;
  }
  
  public boolean removeAll(ArraySet<? extends E> paramArraySet)
  {
    int i = mSize;
    int j = mSize;
    boolean bool = false;
    for (int k = 0; k < i; k++) {
      remove(paramArraySet.valueAt(k));
    }
    if (j != mSize) {
      bool = true;
    }
    return bool;
  }
  
  public boolean removeAll(Collection<?> paramCollection)
  {
    boolean bool = false;
    paramCollection = paramCollection.iterator();
    while (paramCollection.hasNext()) {
      bool |= remove(paramCollection.next());
    }
    return bool;
  }
  
  public E removeAt(int paramInt)
  {
    Object localObject = mArray[paramInt];
    if (mSize <= 1)
    {
      freeArrays(mHashes, mArray, mSize);
      mHashes = EmptyArray.INT;
      mArray = EmptyArray.OBJECT;
      mSize = 0;
    }
    else
    {
      int i = mHashes.length;
      int j = 8;
      if ((i > 8) && (mSize < mHashes.length / 3))
      {
        if (mSize > 8)
        {
          j = mSize;
          j = (mSize >> 1) + j;
        }
        int[] arrayOfInt = mHashes;
        Object[] arrayOfObject = mArray;
        allocArrays(j);
        mSize -= 1;
        if (paramInt > 0)
        {
          System.arraycopy(arrayOfInt, 0, mHashes, 0, paramInt);
          System.arraycopy(arrayOfObject, 0, mArray, 0, paramInt);
        }
        if (paramInt < mSize)
        {
          System.arraycopy(arrayOfInt, paramInt + 1, mHashes, paramInt, mSize - paramInt);
          System.arraycopy(arrayOfObject, paramInt + 1, mArray, paramInt, mSize - paramInt);
        }
      }
      else
      {
        mSize -= 1;
        if (paramInt < mSize)
        {
          System.arraycopy(mHashes, paramInt + 1, mHashes, paramInt, mSize - paramInt);
          System.arraycopy(mArray, paramInt + 1, mArray, paramInt, mSize - paramInt);
        }
        mArray[mSize] = null;
      }
    }
    return localObject;
  }
  
  public boolean retainAll(Collection<?> paramCollection)
  {
    boolean bool = false;
    for (int i = mSize - 1; i >= 0; i--) {
      if (!paramCollection.contains(mArray[i]))
      {
        removeAt(i);
        bool = true;
      }
    }
    return bool;
  }
  
  public int size()
  {
    return mSize;
  }
  
  public Object[] toArray()
  {
    Object[] arrayOfObject = new Object[mSize];
    System.arraycopy(mArray, 0, arrayOfObject, 0, mSize);
    return arrayOfObject;
  }
  
  public <T> T[] toArray(T[] paramArrayOfT)
  {
    Object localObject = paramArrayOfT;
    if (paramArrayOfT.length < mSize) {
      localObject = (Object[])Array.newInstance(paramArrayOfT.getClass().getComponentType(), mSize);
    }
    System.arraycopy(mArray, 0, localObject, 0, mSize);
    if (localObject.length > mSize) {
      localObject[mSize] = null;
    }
    return localObject;
  }
  
  public String toString()
  {
    if (isEmpty()) {
      return "{}";
    }
    StringBuilder localStringBuilder = new StringBuilder(mSize * 14);
    localStringBuilder.append('{');
    for (int i = 0; i < mSize; i++)
    {
      if (i > 0) {
        localStringBuilder.append(", ");
      }
      Object localObject = valueAt(i);
      if (localObject != this) {
        localStringBuilder.append(localObject);
      } else {
        localStringBuilder.append("(this Set)");
      }
    }
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public E valueAt(int paramInt)
  {
    return mArray[paramInt];
  }
}
