package com.android.internal.util.function;

public abstract interface TriFunction<A, B, C, R>
{
  public abstract R apply(A paramA, B paramB, C paramC);
}
