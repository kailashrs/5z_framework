package com.android.internal.util.function.pooled;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;
import com.android.internal.util.FunctionalUtils.ThrowingSupplier;
import com.android.internal.util.function.HexConsumer;
import com.android.internal.util.function.HexFunction;
import com.android.internal.util.function.QuadConsumer;
import com.android.internal.util.function.QuadFunction;
import com.android.internal.util.function.QuintConsumer;
import com.android.internal.util.function.QuintFunction;
import com.android.internal.util.function.TriConsumer;
import com.android.internal.util.function.TriFunction;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

abstract class OmniFunction<A, B, C, D, E, F, R>
  implements PooledFunction<A, R>, BiFunction<A, B, R>, TriFunction<A, B, C, R>, QuadFunction<A, B, C, D, R>, QuintFunction<A, B, C, D, E, R>, HexFunction<A, B, C, D, E, F, R>, PooledConsumer<A>, BiConsumer<A, B>, TriConsumer<A, B, C>, QuadConsumer<A, B, C, D>, QuintConsumer<A, B, C, D, E>, HexConsumer<A, B, C, D, E, F>, PooledPredicate<A>, BiPredicate<A, B>, PooledSupplier<R>, PooledRunnable, FunctionalUtils.ThrowingRunnable, FunctionalUtils.ThrowingSupplier<R>, PooledSupplier.OfInt, PooledSupplier.OfLong, PooledSupplier.OfDouble
{
  OmniFunction() {}
  
  public void accept(A paramA)
  {
    invoke(paramA, null, null, null, null, null);
  }
  
  public void accept(A paramA, B paramB)
  {
    invoke(paramA, paramB, null, null, null, null);
  }
  
  public void accept(A paramA, B paramB, C paramC)
  {
    invoke(paramA, paramB, paramC, null, null, null);
  }
  
  public void accept(A paramA, B paramB, C paramC, D paramD)
  {
    invoke(paramA, paramB, paramC, paramD, null, null);
  }
  
  public void accept(A paramA, B paramB, C paramC, D paramD, E paramE)
  {
    invoke(paramA, paramB, paramC, paramD, paramE, null);
  }
  
  public void accept(A paramA, B paramB, C paramC, D paramD, E paramE, F paramF)
  {
    invoke(paramA, paramB, paramC, paramD, paramE, paramF);
  }
  
  public abstract <V> OmniFunction<A, B, C, D, E, F, V> andThen(Function<? super R, ? extends V> paramFunction);
  
  public R apply(A paramA)
  {
    return invoke(paramA, null, null, null, null, null);
  }
  
  public R apply(A paramA, B paramB)
  {
    return invoke(paramA, paramB, null, null, null, null);
  }
  
  public R apply(A paramA, B paramB, C paramC)
  {
    return invoke(paramA, paramB, paramC, null, null, null);
  }
  
  public R apply(A paramA, B paramB, C paramC, D paramD)
  {
    return invoke(paramA, paramB, paramC, paramD, null, null);
  }
  
  public R apply(A paramA, B paramB, C paramC, D paramD, E paramE)
  {
    return invoke(paramA, paramB, paramC, paramD, paramE, null);
  }
  
  public R apply(A paramA, B paramB, C paramC, D paramD, E paramE, F paramF)
  {
    return invoke(paramA, paramB, paramC, paramD, paramE, paramF);
  }
  
  public PooledConsumer<A> asConsumer()
  {
    return this;
  }
  
  public PooledRunnable asRunnable()
  {
    return this;
  }
  
  public R get()
  {
    return invoke(null, null, null, null, null, null);
  }
  
  public R getOrThrow()
    throws Exception
  {
    return get();
  }
  
  abstract R invoke(A paramA, B paramB, C paramC, D paramD, E paramE, F paramF);
  
  public abstract OmniFunction<A, B, C, D, E, F, R> negate();
  
  public abstract OmniFunction<A, B, C, D, E, F, R> recycleOnUse();
  
  public void run()
  {
    invoke(null, null, null, null, null, null);
  }
  
  public void runOrThrow()
    throws Exception
  {
    run();
  }
  
  public boolean test(A paramA)
  {
    return ((Boolean)invoke(paramA, null, null, null, null, null)).booleanValue();
  }
  
  public boolean test(A paramA, B paramB)
  {
    return ((Boolean)invoke(paramA, paramB, null, null, null, null)).booleanValue();
  }
}
