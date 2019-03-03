package com.android.internal.util.function;

public abstract interface HexFunction<A, B, C, D, E, F, R>
{
  public abstract R apply(A paramA, B paramB, C paramC, D paramD, E paramE, F paramF);
}
