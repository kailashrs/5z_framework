package android.os.strictmode;

public final class SqliteObjectLeakedViolation
  extends Violation
{
  public SqliteObjectLeakedViolation(String paramString, Throwable paramThrowable)
  {
    super(paramString);
    initCause(paramThrowable);
  }
}
