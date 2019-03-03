package com.android.internal.textservice;

import android.util.SparseIntArray;
import com.android.internal.annotations.VisibleForTesting;
import java.util.function.IntUnaryOperator;

@VisibleForTesting
public final class LazyIntToIntMap
{
  private final SparseIntArray mMap = new SparseIntArray();
  private final IntUnaryOperator mMappingFunction;
  
  public LazyIntToIntMap(IntUnaryOperator paramIntUnaryOperator)
  {
    mMappingFunction = paramIntUnaryOperator;
  }
  
  public void delete(int paramInt)
  {
    mMap.delete(paramInt);
  }
  
  public int get(int paramInt)
  {
    int i = mMap.indexOfKey(paramInt);
    if (i >= 0) {
      return mMap.valueAt(i);
    }
    i = mMappingFunction.applyAsInt(paramInt);
    mMap.append(paramInt, i);
    return i;
  }
}
