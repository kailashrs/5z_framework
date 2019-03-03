package android.os.strictmode;

public final class IntentReceiverLeakedViolation
  extends Violation
{
  public IntentReceiverLeakedViolation(Throwable paramThrowable)
  {
    super(null);
    setStackTrace(paramThrowable.getStackTrace());
  }
}
