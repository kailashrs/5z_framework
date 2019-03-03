package android.security;

public abstract class ConfirmationCallback
{
  public ConfirmationCallback() {}
  
  public void onCanceled() {}
  
  public void onConfirmed(byte[] paramArrayOfByte) {}
  
  public void onDismissed() {}
  
  public void onError(Throwable paramThrowable) {}
}
