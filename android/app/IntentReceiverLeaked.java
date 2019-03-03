package android.app;

import android.util.AndroidRuntimeException;

final class IntentReceiverLeaked
  extends AndroidRuntimeException
{
  public IntentReceiverLeaked(String paramString)
  {
    super(paramString);
  }
}
