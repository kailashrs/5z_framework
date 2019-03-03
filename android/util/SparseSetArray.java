package android.util;

public class SparseSetArray<T>
{
  private final SparseArray<ArraySet<T>> mData = new SparseArray();
  
  public SparseSetArray() {}
  
  public boolean add(int paramInt, T paramT)
  {
    ArraySet localArraySet1 = (ArraySet)mData.get(paramInt);
    ArraySet localArraySet2 = localArraySet1;
    if (localArraySet1 == null)
    {
      localArraySet2 = new ArraySet();
      mData.put(paramInt, localArraySet2);
    }
    if (localArraySet2.contains(paramT)) {
      return true;
    }
    localArraySet2.add(paramT);
    return false;
  }
  
  public boolean contains(int paramInt, T paramT)
  {
    ArraySet localArraySet = (ArraySet)mData.get(paramInt);
    if (localArraySet == null) {
      return false;
    }
    return localArraySet.contains(paramT);
  }
  
  public int keyAt(int paramInt)
  {
    return mData.keyAt(paramInt);
  }
  
  public void remove(int paramInt)
  {
    mData.remove(paramInt);
  }
  
  public boolean remove(int paramInt, T paramT)
  {
    ArraySet localArraySet = (ArraySet)mData.get(paramInt);
    if (localArraySet == null) {
      return false;
    }
    boolean bool = localArraySet.remove(paramT);
    if (localArraySet.size() == 0) {
      mData.remove(paramInt);
    }
    return bool;
  }
  
  public int size()
  {
    return mData.size();
  }
  
  public int sizeAt(int paramInt)
  {
    ArraySet localArraySet = (ArraySet)mData.valueAt(paramInt);
    if (localArraySet == null) {
      return 0;
    }
    return localArraySet.size();
  }
  
  public T valueAt(int paramInt1, int paramInt2)
  {
    return ((ArraySet)mData.valueAt(paramInt1)).valueAt(paramInt2);
  }
}
