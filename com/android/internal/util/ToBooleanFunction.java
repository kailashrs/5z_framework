package com.android.internal.util;

@FunctionalInterface
public abstract interface ToBooleanFunction<T>
{
  public abstract boolean apply(T paramT);
}
