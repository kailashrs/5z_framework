package com.android.internal.util;

import android.os.RemoteException;
import android.util.ExceptionUtils;
import java.util.function.Consumer;

public class FunctionalUtils
{
  private FunctionalUtils() {}
  
  public static Runnable handleExceptions(ThrowingRunnable paramThrowingRunnable, Consumer<Throwable> paramConsumer)
  {
    return new _..Lambda.FunctionalUtils.koCSI8D7Nu5vOJTVTEj0m3leo_U(paramThrowingRunnable, paramConsumer);
  }
  
  public static <T> Consumer<T> ignoreRemoteException(RemoteExceptionIgnoringConsumer<T> paramRemoteExceptionIgnoringConsumer)
  {
    return paramRemoteExceptionIgnoringConsumer;
  }
  
  public static <T> Consumer<T> uncheckExceptions(ThrowingConsumer<T> paramThrowingConsumer)
  {
    return paramThrowingConsumer;
  }
  
  @FunctionalInterface
  public static abstract interface RemoteExceptionIgnoringConsumer<T>
    extends Consumer<T>
  {
    public void accept(T paramT)
    {
      try
      {
        acceptOrThrow(paramT);
      }
      catch (RemoteException paramT) {}
    }
    
    public abstract void acceptOrThrow(T paramT)
      throws RemoteException;
  }
  
  @FunctionalInterface
  public static abstract interface ThrowingConsumer<T>
    extends Consumer<T>
  {
    public void accept(T paramT)
    {
      try
      {
        acceptOrThrow(paramT);
        return;
      }
      catch (Exception paramT)
      {
        throw ExceptionUtils.propagate(paramT);
      }
    }
    
    public abstract void acceptOrThrow(T paramT)
      throws Exception;
  }
  
  @FunctionalInterface
  public static abstract interface ThrowingRunnable
    extends Runnable
  {
    public void run()
    {
      try
      {
        runOrThrow();
        return;
      }
      catch (Exception localException)
      {
        throw ExceptionUtils.propagate(localException);
      }
    }
    
    public abstract void runOrThrow()
      throws Exception;
  }
  
  @FunctionalInterface
  public static abstract interface ThrowingSupplier<T>
  {
    public abstract T getOrThrow()
      throws Exception;
  }
}
