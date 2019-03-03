package android.os.strictmode;

public final class WebViewMethodCalledOnWrongThreadViolation
  extends Violation
{
  public WebViewMethodCalledOnWrongThreadViolation(Throwable paramThrowable)
  {
    super(null);
    setStackTrace(paramThrowable.getStackTrace());
  }
}
