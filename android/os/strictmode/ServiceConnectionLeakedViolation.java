package android.os.strictmode;

public final class ServiceConnectionLeakedViolation
  extends Violation
{
  public ServiceConnectionLeakedViolation(Throwable paramThrowable)
  {
    super(null);
    setStackTrace(paramThrowable.getStackTrace());
  }
}
