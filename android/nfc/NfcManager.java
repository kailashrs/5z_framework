package android.nfc;

import android.content.Context;

public final class NfcManager
{
  private final NfcAdapter mAdapter;
  
  public NfcManager(Context paramContext)
  {
    paramContext = paramContext.getApplicationContext();
    if (paramContext != null)
    {
      try
      {
        paramContext = NfcAdapter.getNfcAdapter(paramContext);
      }
      catch (UnsupportedOperationException paramContext)
      {
        paramContext = null;
      }
      mAdapter = paramContext;
      return;
    }
    throw new IllegalArgumentException("context not associated with any application (using a mock context?)");
  }
  
  public NfcAdapter getDefaultAdapter()
  {
    return mAdapter;
  }
}
