package android.os.strictmode;

public final class LeakedClosableViolation
  extends Violation
{
  public LeakedClosableViolation(String paramString, Throwable paramThrowable)
  {
    super(paramString);
    initCause(paramThrowable);
  }
}
