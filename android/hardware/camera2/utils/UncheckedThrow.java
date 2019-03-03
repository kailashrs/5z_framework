package android.hardware.camera2.utils;

public class UncheckedThrow
{
  public UncheckedThrow() {}
  
  public static void throwAnyException(Exception paramException)
  {
    throwAnyImpl(paramException);
  }
  
  public static void throwAnyException(Throwable paramThrowable)
  {
    throwAnyImpl(paramThrowable);
  }
  
  private static <T extends Throwable> void throwAnyImpl(Throwable paramThrowable)
    throws Throwable
  {
    throw paramThrowable;
  }
}
