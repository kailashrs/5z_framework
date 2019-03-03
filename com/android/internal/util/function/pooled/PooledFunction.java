package com.android.internal.util.function.pooled;

import java.util.function.Function;

public abstract interface PooledFunction<A, R>
  extends PooledLambda, Function<A, R>
{
  public abstract PooledConsumer<A> asConsumer();
  
  public abstract PooledFunction<A, R> recycleOnUse();
}
