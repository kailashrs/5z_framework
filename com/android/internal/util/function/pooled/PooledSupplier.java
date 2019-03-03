package com.android.internal.util.function.pooled;

import com.android.internal.util.FunctionalUtils.ThrowingSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

public abstract interface PooledSupplier<T>
  extends PooledLambda, Supplier<T>, FunctionalUtils.ThrowingSupplier<T>
{
  public abstract PooledRunnable asRunnable();
  
  public abstract PooledSupplier<T> recycleOnUse();
  
  public static abstract interface OfDouble
    extends DoubleSupplier, PooledLambda
  {
    public abstract OfDouble recycleOnUse();
  }
  
  public static abstract interface OfInt
    extends IntSupplier, PooledLambda
  {
    public abstract OfInt recycleOnUse();
  }
  
  public static abstract interface OfLong
    extends LongSupplier, PooledLambda
  {
    public abstract OfLong recycleOnUse();
  }
}
