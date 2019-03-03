package android.nfc.dta;

import android.content.Context;
import android.nfc.INfcDta;
import android.nfc.NfcAdapter;
import android.os.RemoteException;
import android.util.Log;
import java.util.HashMap;

public final class NfcDta
{
  private static final String TAG = "NfcDta";
  private static HashMap<Context, NfcDta> sNfcDtas = new HashMap();
  private static INfcDta sService;
  private final Context mContext;
  
  private NfcDta(Context paramContext, INfcDta paramINfcDta)
  {
    mContext = paramContext.getApplicationContext();
    sService = paramINfcDta;
  }
  
  public static NfcDta getInstance(NfcAdapter paramNfcAdapter)
  {
    if (paramNfcAdapter != null) {}
    try
    {
      Context localContext = paramNfcAdapter.getContext();
      if (localContext != null)
      {
        NfcDta localNfcDta1 = (NfcDta)sNfcDtas.get(localContext);
        NfcDta localNfcDta2 = localNfcDta1;
        if (localNfcDta1 == null)
        {
          paramNfcAdapter = paramNfcAdapter.getNfcDtaInterface();
          if (paramNfcAdapter != null)
          {
            localNfcDta2 = new android/nfc/dta/NfcDta;
            localNfcDta2.<init>(localContext, paramNfcAdapter);
            sNfcDtas.put(localContext, localNfcDta2);
          }
          else
          {
            Log.e("NfcDta", "This device does not implement the INfcDta interface.");
            paramNfcAdapter = new java/lang/UnsupportedOperationException;
            paramNfcAdapter.<init>();
            throw paramNfcAdapter;
          }
        }
        return localNfcDta2;
      }
      Log.e("NfcDta", "NfcAdapter context is null.");
      paramNfcAdapter = new java/lang/UnsupportedOperationException;
      paramNfcAdapter.<init>();
      throw paramNfcAdapter;
    }
    finally {}
    paramNfcAdapter = new java/lang/NullPointerException;
    paramNfcAdapter.<init>("NfcAdapter is null");
    throw paramNfcAdapter;
  }
  
  public boolean disableClient()
  {
    try
    {
      sService.disableClient();
      return true;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean disableDta()
  {
    try
    {
      sService.disableDta();
      return true;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean disableServer()
  {
    try
    {
      sService.disableServer();
      return true;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean enableClient(String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    try
    {
      boolean bool = sService.enableClient(paramString, paramInt1, paramInt2, paramInt3);
      return bool;
    }
    catch (RemoteException paramString) {}
    return false;
  }
  
  public boolean enableDta()
  {
    try
    {
      sService.enableDta();
      return true;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean enableServer(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    try
    {
      boolean bool = sService.enableServer(paramString, paramInt1, paramInt2, paramInt3, paramInt4);
      return bool;
    }
    catch (RemoteException paramString) {}
    return false;
  }
  
  public boolean registerMessageService(String paramString)
  {
    try
    {
      boolean bool = sService.registerMessageService(paramString);
      return bool;
    }
    catch (RemoteException paramString) {}
    return false;
  }
}
