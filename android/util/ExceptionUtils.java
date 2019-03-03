package android.util;

import android.os.ParcelableException;
import com.android.internal.util.Preconditions;
import java.io.IOException;

public class ExceptionUtils
{
  public ExceptionUtils() {}
  
  public static Throwable appendCause(Throwable paramThrowable1, Throwable paramThrowable2)
  {
    if (paramThrowable2 != null) {
      getRootCause(paramThrowable1).initCause(paramThrowable2);
    }
    return paramThrowable1;
  }
  
  public static String getCompleteMessage(String paramString, Throwable paramThrowable)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (paramString != null)
    {
      localStringBuilder.append(paramString);
      localStringBuilder.append(": ");
    }
    localStringBuilder.append(paramThrowable.getMessage());
    paramString = paramThrowable;
    for (;;)
    {
      paramThrowable = paramString.getCause();
      paramString = paramThrowable;
      if (paramThrowable == null) {
        break;
      }
      localStringBuilder.append(": ");
      localStringBuilder.append(paramString.getMessage());
    }
    return localStringBuilder.toString();
  }
  
  public static String getCompleteMessage(Throwable paramThrowable)
  {
    return getCompleteMessage(null, paramThrowable);
  }
  
  public static Throwable getRootCause(Throwable paramThrowable)
  {
    while (paramThrowable.getCause() != null) {
      paramThrowable = paramThrowable.getCause();
    }
    return paramThrowable;
  }
  
  public static void maybeUnwrapIOException(RuntimeException paramRuntimeException)
    throws IOException
  {
    if ((paramRuntimeException instanceof ParcelableException)) {
      ((ParcelableException)paramRuntimeException).maybeRethrow(IOException.class);
    }
  }
  
  public static RuntimeException propagate(Throwable paramThrowable)
  {
    Preconditions.checkNotNull(paramThrowable);
    propagateIfInstanceOf(paramThrowable, Error.class);
    propagateIfInstanceOf(paramThrowable, RuntimeException.class);
    throw new RuntimeException(paramThrowable);
  }
  
  public static <E extends Exception> RuntimeException propagate(Throwable paramThrowable, Class<E> paramClass)
    throws Exception
  {
    propagateIfInstanceOf(paramThrowable, paramClass);
    return propagate(paramThrowable);
  }
  
  public static <E extends Throwable> void propagateIfInstanceOf(Throwable paramThrowable, Class<E> paramClass)
    throws Throwable
  {
    if ((paramThrowable != null) && (paramClass.isInstance(paramThrowable))) {
      throw ((Throwable)paramClass.cast(paramThrowable));
    }
  }
  
  public static RuntimeException wrap(IOException paramIOException)
  {
    throw new ParcelableException(paramIOException);
  }
}
