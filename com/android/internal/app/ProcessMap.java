package com.android.internal.app;

import android.util.ArrayMap;
import android.util.SparseArray;

public class ProcessMap<E>
{
  final ArrayMap<String, SparseArray<E>> mMap = new ArrayMap();
  
  public ProcessMap() {}
  
  public E get(String paramString, int paramInt)
  {
    paramString = (SparseArray)mMap.get(paramString);
    if (paramString == null) {
      return null;
    }
    return paramString.get(paramInt);
  }
  
  public ArrayMap<String, SparseArray<E>> getMap()
  {
    return mMap;
  }
  
  public E put(String paramString, int paramInt, E paramE)
  {
    SparseArray localSparseArray1 = (SparseArray)mMap.get(paramString);
    SparseArray localSparseArray2 = localSparseArray1;
    if (localSparseArray1 == null)
    {
      localSparseArray2 = new SparseArray(2);
      mMap.put(paramString, localSparseArray2);
    }
    localSparseArray2.put(paramInt, paramE);
    return paramE;
  }
  
  public E remove(String paramString, int paramInt)
  {
    SparseArray localSparseArray = (SparseArray)mMap.get(paramString);
    if (localSparseArray != null)
    {
      Object localObject = localSparseArray.removeReturnOld(paramInt);
      if (localSparseArray.size() == 0) {
        mMap.remove(paramString);
      }
      return localObject;
    }
    return null;
  }
  
  public int size()
  {
    return mMap.size();
  }
}
