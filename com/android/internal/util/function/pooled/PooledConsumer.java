package com.android.internal.util.function.pooled;

import java.util.function.Consumer;

public abstract interface PooledConsumer<T>
  extends PooledLambda, Consumer<T>
{
  public abstract PooledConsumer<T> recycleOnUse();
}
