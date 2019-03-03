package com.android.internal.util.function.pooled;

import android.os.Message;
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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract interface PooledLambda
{
  public static <R> ArgumentPlaceholder<R> __()
  {
    return ArgumentPlaceholder.INSTANCE;
  }
  
  public static <R> ArgumentPlaceholder<R> __(Class<R> paramClass)
  {
    return __();
  }
  
  public static <A, B, C, D> PooledConsumer<A> obtainConsumer(QuadConsumer<? super A, ? super B, ? super C, ? super D> paramQuadConsumer, ArgumentPlaceholder<A> paramArgumentPlaceholder, B paramB, C paramC, D paramD)
  {
    return (PooledConsumer)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramQuadConsumer, 4, 1, 1, paramArgumentPlaceholder, paramB, paramC, paramD, null, null);
  }
  
  public static <A, B, C, D> PooledConsumer<B> obtainConsumer(QuadConsumer<? super A, ? super B, ? super C, ? super D> paramQuadConsumer, A paramA, ArgumentPlaceholder<B> paramArgumentPlaceholder, C paramC, D paramD)
  {
    return (PooledConsumer)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramQuadConsumer, 4, 1, 1, paramA, paramArgumentPlaceholder, paramC, paramD, null, null);
  }
  
  public static <A, B, C, D> PooledConsumer<C> obtainConsumer(QuadConsumer<? super A, ? super B, ? super C, ? super D> paramQuadConsumer, A paramA, B paramB, ArgumentPlaceholder<C> paramArgumentPlaceholder, D paramD)
  {
    return (PooledConsumer)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramQuadConsumer, 4, 1, 1, paramA, paramB, paramArgumentPlaceholder, paramD, null, null);
  }
  
  public static <A, B, C, D> PooledConsumer<D> obtainConsumer(QuadConsumer<? super A, ? super B, ? super C, ? super D> paramQuadConsumer, A paramA, B paramB, C paramC, ArgumentPlaceholder<D> paramArgumentPlaceholder)
  {
    return (PooledConsumer)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramQuadConsumer, 4, 1, 1, paramA, paramB, paramC, paramArgumentPlaceholder, null, null);
  }
  
  public static <A, B, C> PooledConsumer<A> obtainConsumer(TriConsumer<? super A, ? super B, ? super C> paramTriConsumer, ArgumentPlaceholder<A> paramArgumentPlaceholder, B paramB, C paramC)
  {
    return (PooledConsumer)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramTriConsumer, 3, 1, 1, paramArgumentPlaceholder, paramB, paramC, null, null, null);
  }
  
  public static <A, B, C> PooledConsumer<B> obtainConsumer(TriConsumer<? super A, ? super B, ? super C> paramTriConsumer, A paramA, ArgumentPlaceholder<B> paramArgumentPlaceholder, C paramC)
  {
    return (PooledConsumer)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramTriConsumer, 3, 1, 1, paramA, paramArgumentPlaceholder, paramC, null, null, null);
  }
  
  public static <A, B, C> PooledConsumer<C> obtainConsumer(TriConsumer<? super A, ? super B, ? super C> paramTriConsumer, A paramA, B paramB, ArgumentPlaceholder<C> paramArgumentPlaceholder)
  {
    return (PooledConsumer)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramTriConsumer, 3, 1, 1, paramA, paramB, paramArgumentPlaceholder, null, null, null);
  }
  
  public static <A, B> PooledConsumer<A> obtainConsumer(BiConsumer<? super A, ? super B> paramBiConsumer, ArgumentPlaceholder<A> paramArgumentPlaceholder, B paramB)
  {
    return (PooledConsumer)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramBiConsumer, 2, 1, 1, paramArgumentPlaceholder, paramB, null, null, null, null);
  }
  
  public static <A, B> PooledConsumer<B> obtainConsumer(BiConsumer<? super A, ? super B> paramBiConsumer, A paramA, ArgumentPlaceholder<B> paramArgumentPlaceholder)
  {
    return (PooledConsumer)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramBiConsumer, 2, 1, 1, paramA, paramArgumentPlaceholder, null, null, null, null);
  }
  
  public static <A, B, C, D, R> PooledFunction<A, R> obtainFunction(QuadFunction<? super A, ? super B, ? super C, ? super D, ? extends R> paramQuadFunction, ArgumentPlaceholder<A> paramArgumentPlaceholder, B paramB, C paramC, D paramD)
  {
    return (PooledFunction)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramQuadFunction, 4, 1, 3, paramArgumentPlaceholder, paramB, paramC, paramD, null, null);
  }
  
  public static <A, B, C, D, R> PooledFunction<B, R> obtainFunction(QuadFunction<? super A, ? super B, ? super C, ? super D, ? extends R> paramQuadFunction, A paramA, ArgumentPlaceholder<B> paramArgumentPlaceholder, C paramC, D paramD)
  {
    return (PooledFunction)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramQuadFunction, 4, 1, 3, paramA, paramArgumentPlaceholder, paramC, paramD, null, null);
  }
  
  public static <A, B, C, D, R> PooledFunction<C, R> obtainFunction(QuadFunction<? super A, ? super B, ? super C, ? super D, ? extends R> paramQuadFunction, A paramA, B paramB, ArgumentPlaceholder<C> paramArgumentPlaceholder, D paramD)
  {
    return (PooledFunction)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramQuadFunction, 4, 1, 3, paramA, paramB, paramArgumentPlaceholder, paramD, null, null);
  }
  
  public static <A, B, C, D, R> PooledFunction<D, R> obtainFunction(QuadFunction<? super A, ? super B, ? super C, ? super D, ? extends R> paramQuadFunction, A paramA, B paramB, C paramC, ArgumentPlaceholder<D> paramArgumentPlaceholder)
  {
    return (PooledFunction)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramQuadFunction, 4, 1, 3, paramA, paramB, paramC, paramArgumentPlaceholder, null, null);
  }
  
  public static <A, B, C, R> PooledFunction<A, R> obtainFunction(TriFunction<? super A, ? super B, ? super C, ? extends R> paramTriFunction, ArgumentPlaceholder<A> paramArgumentPlaceholder, B paramB, C paramC)
  {
    return (PooledFunction)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramTriFunction, 3, 1, 3, paramArgumentPlaceholder, paramB, paramC, null, null, null);
  }
  
  public static <A, B, C, R> PooledFunction<B, R> obtainFunction(TriFunction<? super A, ? super B, ? super C, ? extends R> paramTriFunction, A paramA, ArgumentPlaceholder<B> paramArgumentPlaceholder, C paramC)
  {
    return (PooledFunction)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramTriFunction, 3, 1, 3, paramA, paramArgumentPlaceholder, paramC, null, null, null);
  }
  
  public static <A, B, C, R> PooledFunction<C, R> obtainFunction(TriFunction<? super A, ? super B, ? super C, ? extends R> paramTriFunction, A paramA, B paramB, ArgumentPlaceholder<C> paramArgumentPlaceholder)
  {
    return (PooledFunction)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramTriFunction, 3, 1, 3, paramA, paramB, paramArgumentPlaceholder, null, null, null);
  }
  
  public static <A, B, R> PooledFunction<A, R> obtainFunction(BiFunction<? super A, ? super B, ? extends R> paramBiFunction, ArgumentPlaceholder<A> paramArgumentPlaceholder, B paramB)
  {
    return (PooledFunction)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramBiFunction, 2, 1, 3, paramArgumentPlaceholder, paramB, null, null, null, null);
  }
  
  public static <A, B, R> PooledFunction<B, R> obtainFunction(BiFunction<? super A, ? super B, ? extends R> paramBiFunction, A paramA, ArgumentPlaceholder<B> paramArgumentPlaceholder)
  {
    return (PooledFunction)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramBiFunction, 2, 1, 3, paramA, paramArgumentPlaceholder, null, null, null, null);
  }
  
  public static <A, B, C, D, E, F> Message obtainMessage(HexConsumer<? super A, ? super B, ? super C, ? super D, ? super E, ? super F> paramHexConsumer, A paramA, B paramB, C paramC, D paramD, E paramE, F paramF)
  {
    synchronized (Message.sPoolSync)
    {
      paramHexConsumer = (PooledRunnable)PooledLambdaImpl.acquire(PooledLambdaImpl.sMessageCallbacksPool, paramHexConsumer, 6, 0, 1, paramA, paramB, paramC, paramD, paramE, paramF);
      paramHexConsumer = Message.obtain().setCallback(paramHexConsumer.recycleOnUse());
      return paramHexConsumer;
    }
  }
  
  public static <A, B, C, D> Message obtainMessage(QuadConsumer<? super A, ? super B, ? super C, ? super D> paramQuadConsumer, A paramA, B paramB, C paramC, D paramD)
  {
    synchronized (Message.sPoolSync)
    {
      paramQuadConsumer = (PooledRunnable)PooledLambdaImpl.acquire(PooledLambdaImpl.sMessageCallbacksPool, paramQuadConsumer, 4, 0, 1, paramA, paramB, paramC, paramD, null, null);
      paramQuadConsumer = Message.obtain().setCallback(paramQuadConsumer.recycleOnUse());
      return paramQuadConsumer;
    }
  }
  
  public static <A, B, C, D, E> Message obtainMessage(QuintConsumer<? super A, ? super B, ? super C, ? super D, ? super E> paramQuintConsumer, A paramA, B paramB, C paramC, D paramD, E paramE)
  {
    synchronized (Message.sPoolSync)
    {
      paramQuintConsumer = (PooledRunnable)PooledLambdaImpl.acquire(PooledLambdaImpl.sMessageCallbacksPool, paramQuintConsumer, 5, 0, 1, paramA, paramB, paramC, paramD, paramE, null);
      paramQuintConsumer = Message.obtain().setCallback(paramQuintConsumer.recycleOnUse());
      return paramQuintConsumer;
    }
  }
  
  public static <A, B, C> Message obtainMessage(TriConsumer<? super A, ? super B, ? super C> paramTriConsumer, A paramA, B paramB, C paramC)
  {
    synchronized (Message.sPoolSync)
    {
      paramTriConsumer = (PooledRunnable)PooledLambdaImpl.acquire(PooledLambdaImpl.sMessageCallbacksPool, paramTriConsumer, 3, 0, 1, paramA, paramB, paramC, null, null, null);
      paramTriConsumer = Message.obtain().setCallback(paramTriConsumer.recycleOnUse());
      return paramTriConsumer;
    }
  }
  
  public static <A, B> Message obtainMessage(BiConsumer<? super A, ? super B> paramBiConsumer, A paramA, B paramB)
  {
    synchronized (Message.sPoolSync)
    {
      paramBiConsumer = (PooledRunnable)PooledLambdaImpl.acquire(PooledLambdaImpl.sMessageCallbacksPool, paramBiConsumer, 2, 0, 1, paramA, paramB, null, null, null, null);
      paramBiConsumer = Message.obtain().setCallback(paramBiConsumer.recycleOnUse());
      return paramBiConsumer;
    }
  }
  
  public static <A> Message obtainMessage(Consumer<? super A> paramConsumer, A paramA)
  {
    synchronized (Message.sPoolSync)
    {
      paramConsumer = (PooledRunnable)PooledLambdaImpl.acquire(PooledLambdaImpl.sMessageCallbacksPool, paramConsumer, 1, 0, 1, paramA, null, null, null, null, null);
      paramConsumer = Message.obtain().setCallback(paramConsumer.recycleOnUse());
      return paramConsumer;
    }
  }
  
  public static <A, B> PooledPredicate<A> obtainPredicate(BiPredicate<? super A, ? super B> paramBiPredicate, ArgumentPlaceholder<A> paramArgumentPlaceholder, B paramB)
  {
    return (PooledPredicate)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramBiPredicate, 2, 1, 2, paramArgumentPlaceholder, paramB, null, null, null, null);
  }
  
  public static <A, B> PooledPredicate<B> obtainPredicate(BiPredicate<? super A, ? super B> paramBiPredicate, A paramA, ArgumentPlaceholder<B> paramArgumentPlaceholder)
  {
    return (PooledPredicate)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramBiPredicate, 2, 1, 2, paramA, paramArgumentPlaceholder, null, null, null, null);
  }
  
  public static <A, B, C, D, E, F> PooledRunnable obtainRunnable(HexConsumer<? super A, ? super B, ? super C, ? super D, ? super E, ? super F> paramHexConsumer, A paramA, B paramB, C paramC, D paramD, E paramE, F paramF)
  {
    return (PooledRunnable)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramHexConsumer, 6, 0, 1, paramA, paramB, paramC, paramD, paramE, paramF);
  }
  
  public static <A, B, C, D> PooledRunnable obtainRunnable(QuadConsumer<? super A, ? super B, ? super C, ? super D> paramQuadConsumer, A paramA, B paramB, C paramC, D paramD)
  {
    return (PooledRunnable)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramQuadConsumer, 4, 0, 1, paramA, paramB, paramC, paramD, null, null);
  }
  
  public static <A, B, C, D, E> PooledRunnable obtainRunnable(QuintConsumer<? super A, ? super B, ? super C, ? super D, ? super E> paramQuintConsumer, A paramA, B paramB, C paramC, D paramD, E paramE)
  {
    return (PooledRunnable)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramQuintConsumer, 5, 0, 1, paramA, paramB, paramC, paramD, paramE, null);
  }
  
  public static <A, B, C> PooledRunnable obtainRunnable(TriConsumer<? super A, ? super B, ? super C> paramTriConsumer, A paramA, B paramB, C paramC)
  {
    return (PooledRunnable)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramTriConsumer, 3, 0, 1, paramA, paramB, paramC, null, null, null);
  }
  
  public static <A, B> PooledRunnable obtainRunnable(BiConsumer<? super A, ? super B> paramBiConsumer, A paramA, B paramB)
  {
    return (PooledRunnable)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramBiConsumer, 2, 0, 1, paramA, paramB, null, null, null, null);
  }
  
  public static <A> PooledRunnable obtainRunnable(Consumer<? super A> paramConsumer, A paramA)
  {
    return (PooledRunnable)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramConsumer, 1, 0, 1, paramA, null, null, null, null, null);
  }
  
  public static PooledSupplier.OfDouble obtainSupplier(double paramDouble)
  {
    PooledLambdaImpl localPooledLambdaImpl = PooledLambdaImpl.acquireConstSupplier(6);
    mConstValue = Double.doubleToRawLongBits(paramDouble);
    return localPooledLambdaImpl;
  }
  
  public static PooledSupplier.OfInt obtainSupplier(int paramInt)
  {
    PooledLambdaImpl localPooledLambdaImpl = PooledLambdaImpl.acquireConstSupplier(4);
    mConstValue = paramInt;
    return localPooledLambdaImpl;
  }
  
  public static PooledSupplier.OfLong obtainSupplier(long paramLong)
  {
    PooledLambdaImpl localPooledLambdaImpl = PooledLambdaImpl.acquireConstSupplier(5);
    mConstValue = paramLong;
    return localPooledLambdaImpl;
  }
  
  public static <A, B, C, D, E, F, R> PooledSupplier<R> obtainSupplier(HexFunction<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? extends R> paramHexFunction, A paramA, B paramB, C paramC, D paramD, E paramE, F paramF)
  {
    return (PooledSupplier)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramHexFunction, 6, 0, 3, paramA, paramB, paramC, paramD, paramE, paramF);
  }
  
  public static <A, B, C, D, R> PooledSupplier<R> obtainSupplier(QuadFunction<? super A, ? super B, ? super C, ? super D, ? extends R> paramQuadFunction, A paramA, B paramB, C paramC, D paramD)
  {
    return (PooledSupplier)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramQuadFunction, 4, 0, 3, paramA, paramB, paramC, paramD, null, null);
  }
  
  public static <A, B, C, D, E, R> PooledSupplier<R> obtainSupplier(QuintFunction<? super A, ? super B, ? super C, ? super D, ? super E, ? extends R> paramQuintFunction, A paramA, B paramB, C paramC, D paramD, E paramE)
  {
    return (PooledSupplier)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramQuintFunction, 5, 0, 3, paramA, paramB, paramC, paramD, paramE, null);
  }
  
  public static <A, B, C, R> PooledSupplier<R> obtainSupplier(TriFunction<? super A, ? super B, ? super C, ? extends R> paramTriFunction, A paramA, B paramB, C paramC)
  {
    return (PooledSupplier)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramTriFunction, 3, 0, 3, paramA, paramB, paramC, null, null, null);
  }
  
  public static <R> PooledSupplier<R> obtainSupplier(R paramR)
  {
    PooledLambdaImpl localPooledLambdaImpl = PooledLambdaImpl.acquireConstSupplier(3);
    mFunc = paramR;
    return localPooledLambdaImpl;
  }
  
  public static <A, B, R> PooledSupplier<R> obtainSupplier(BiFunction<? super A, ? super B, ? extends R> paramBiFunction, A paramA, B paramB)
  {
    return (PooledSupplier)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramBiFunction, 2, 0, 3, paramA, paramB, null, null, null, null);
  }
  
  public static <A, B> PooledSupplier<Boolean> obtainSupplier(BiPredicate<? super A, ? super B> paramBiPredicate, A paramA, B paramB)
  {
    return (PooledSupplier)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramBiPredicate, 2, 0, 2, paramA, paramB, null, null, null, null);
  }
  
  public static <A, R> PooledSupplier<R> obtainSupplier(Function<? super A, ? extends R> paramFunction, A paramA)
  {
    return (PooledSupplier)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramFunction, 1, 0, 3, paramA, null, null, null, null, null);
  }
  
  public static <A> PooledSupplier<Boolean> obtainSupplier(Predicate<? super A> paramPredicate, A paramA)
  {
    return (PooledSupplier)PooledLambdaImpl.acquire(PooledLambdaImpl.sPool, paramPredicate, 1, 0, 2, paramA, null, null, null, null, null);
  }
  
  public abstract void recycle();
  
  public abstract PooledLambda recycleOnUse();
}
