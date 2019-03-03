package com.android.internal.util.function.pooled;

import java.util.function.Predicate;

public abstract interface PooledPredicate<T>
  extends PooledLambda, Predicate<T>
{
  public abstract PooledConsumer<T> asConsumer();
  
  public abstract PooledPredicate<T> recycleOnUse();
}
