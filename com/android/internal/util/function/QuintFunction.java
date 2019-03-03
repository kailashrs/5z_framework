package com.android.internal.util.function;

public abstract interface QuintFunction<A, B, C, D, E, R>
{
  public abstract R apply(A paramA, B paramB, C paramC, D paramD, E paramE);
}
