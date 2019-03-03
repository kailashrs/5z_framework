package com.android.internal.util.function.pooled;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;

public abstract interface PooledRunnable
  extends PooledLambda, Runnable, FunctionalUtils.ThrowingRunnable
{
  public abstract PooledRunnable recycleOnUse();
}
