package com.android.internal.util.function;

public abstract interface QuadFunction<A, B, C, D, R>
{
  public abstract R apply(A paramA, B paramB, C paramC, D paramD);
}
