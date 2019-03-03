package android.nfc.cardemulation;

import android.app.Activity;
import android.app.ActivityThread;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.IPackageManager;
import android.nfc.INfcFCardEmulation;
import android.nfc.NfcAdapter;
import android.os.RemoteException;
import android.util.Log;
import java.util.HashMap;
import java.util.List;

public final class NfcFCardEmulation
{
  static final String TAG = "NfcFCardEmulation";
  static HashMap<Context, NfcFCardEmulation> sCardEmus = new HashMap();
  static boolean sIsInitialized = false;
  static INfcFCardEmulation sService;
  final Context mContext;
  
  private NfcFCardEmulation(Context paramContext, INfcFCardEmulation paramINfcFCardEmulation)
  {
    mContext = paramContext.getApplicationContext();
    sService = paramINfcFCardEmulation;
  }
  
  public static NfcFCardEmulation getInstance(NfcAdapter paramNfcAdapter)
  {
    if (paramNfcAdapter != null) {}
    try
    {
      Context localContext = paramNfcAdapter.getContext();
      if (localContext != null)
      {
        if (!sIsInitialized)
        {
          localObject = ActivityThread.getPackageManager();
          if (localObject != null) {
            try
            {
              boolean bool = ((IPackageManager)localObject).hasSystemFeature("android.hardware.nfc.hcef", 0);
              if (bool)
              {
                sIsInitialized = true;
                break label106;
              }
              Log.e("NfcFCardEmulation", "This device does not support NFC-F card emulation");
              paramNfcAdapter = new java/lang/UnsupportedOperationException;
              paramNfcAdapter.<init>();
              throw paramNfcAdapter;
            }
            catch (RemoteException paramNfcAdapter)
            {
              Log.e("NfcFCardEmulation", "PackageManager query failed.");
              paramNfcAdapter = new java/lang/UnsupportedOperationException;
              paramNfcAdapter.<init>();
              throw paramNfcAdapter;
            }
          }
          Log.e("NfcFCardEmulation", "Cannot get PackageManager");
          paramNfcAdapter = new java/lang/UnsupportedOperationException;
          paramNfcAdapter.<init>();
          throw paramNfcAdapter;
        }
        label106:
        NfcFCardEmulation localNfcFCardEmulation = (NfcFCardEmulation)sCardEmus.get(localContext);
        Object localObject = localNfcFCardEmulation;
        if (localNfcFCardEmulation == null)
        {
          paramNfcAdapter = paramNfcAdapter.getNfcFCardEmulationService();
          if (paramNfcAdapter != null)
          {
            localObject = new android/nfc/cardemulation/NfcFCardEmulation;
            ((NfcFCardEmulation)localObject).<init>(localContext, paramNfcAdapter);
            sCardEmus.put(localContext, localObject);
          }
          else
          {
            Log.e("NfcFCardEmulation", "This device does not implement the INfcFCardEmulation interface.");
            paramNfcAdapter = new java/lang/UnsupportedOperationException;
            paramNfcAdapter.<init>();
            throw paramNfcAdapter;
          }
        }
        return localObject;
      }
      Log.e("NfcFCardEmulation", "NfcAdapter context is null.");
      paramNfcAdapter = new java/lang/UnsupportedOperationException;
      paramNfcAdapter.<init>();
      throw paramNfcAdapter;
    }
    finally {}
    paramNfcAdapter = new java/lang/NullPointerException;
    paramNfcAdapter.<init>("NfcAdapter is null");
    throw paramNfcAdapter;
  }
  
  public static boolean isValidNfcid2(String paramString)
  {
    if (paramString == null) {
      return false;
    }
    StringBuilder localStringBuilder1;
    if (paramString.length() != 16)
    {
      localStringBuilder1 = new StringBuilder();
      localStringBuilder1.append("NFCID2 ");
      localStringBuilder1.append(paramString);
      localStringBuilder1.append(" is not a valid NFCID2.");
      Log.e("NfcFCardEmulation", localStringBuilder1.toString());
      return false;
    }
    if (!paramString.toUpperCase().startsWith("02FE"))
    {
      localStringBuilder1 = new StringBuilder();
      localStringBuilder1.append("NFCID2 ");
      localStringBuilder1.append(paramString);
      localStringBuilder1.append(" is not a valid NFCID2.");
      Log.e("NfcFCardEmulation", localStringBuilder1.toString());
      return false;
    }
    try
    {
      Long.parseLong(paramString, 16);
      return true;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      StringBuilder localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append("NFCID2 ");
      localStringBuilder2.append(paramString);
      localStringBuilder2.append(" is not a valid NFCID2.");
      Log.e("NfcFCardEmulation", localStringBuilder2.toString());
    }
    return false;
  }
  
  public static boolean isValidSystemCode(String paramString)
  {
    if (paramString == null) {
      return false;
    }
    if (paramString.length() != 4)
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      localStringBuilder1.append("System Code ");
      localStringBuilder1.append(paramString);
      localStringBuilder1.append(" is not a valid System Code.");
      Log.e("NfcFCardEmulation", localStringBuilder1.toString());
      return false;
    }
    if ((paramString.startsWith("4")) && (!paramString.toUpperCase().endsWith("FF"))) {
      try
      {
        Integer.parseInt(paramString, 16);
        return true;
      }
      catch (NumberFormatException localNumberFormatException)
      {
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append("System Code ");
        localStringBuilder2.append(paramString);
        localStringBuilder2.append(" is not a valid System Code.");
        Log.e("NfcFCardEmulation", localStringBuilder2.toString());
        return false;
      }
    }
    StringBuilder localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append("System Code ");
    localStringBuilder2.append(paramString);
    localStringBuilder2.append(" is not a valid System Code.");
    Log.e("NfcFCardEmulation", localStringBuilder2.toString());
    return false;
  }
  
  public boolean disableService(Activity paramActivity)
    throws RuntimeException
  {
    if (paramActivity != null)
    {
      if (paramActivity.isResumed()) {
        try
        {
          bool = sService.disableNfcFForegroundService();
          return bool;
        }
        catch (RemoteException paramActivity)
        {
          boolean bool;
          recoverService();
          if (sService == null)
          {
            Log.e("NfcFCardEmulation", "Failed to recover CardEmulationService.");
            return false;
          }
          try
          {
            bool = sService.disableNfcFForegroundService();
            return bool;
          }
          catch (RemoteException paramActivity)
          {
            Log.e("NfcFCardEmulation", "Failed to reach CardEmulationService.");
            paramActivity.rethrowAsRuntimeException();
            return false;
          }
        }
      }
      throw new IllegalArgumentException("Activity must be resumed.");
    }
    throw new NullPointerException("activity is null");
  }
  
  public boolean enableService(Activity paramActivity, ComponentName paramComponentName)
    throws RuntimeException
  {
    if ((paramActivity != null) && (paramComponentName != null))
    {
      if (paramActivity.isResumed()) {
        try
        {
          bool = sService.enableNfcFForegroundService(paramComponentName);
          return bool;
        }
        catch (RemoteException paramActivity)
        {
          boolean bool;
          recoverService();
          if (sService == null)
          {
            Log.e("NfcFCardEmulation", "Failed to recover CardEmulationService.");
            return false;
          }
          try
          {
            bool = sService.enableNfcFForegroundService(paramComponentName);
            return bool;
          }
          catch (RemoteException paramActivity)
          {
            Log.e("NfcFCardEmulation", "Failed to reach CardEmulationService.");
            paramActivity.rethrowAsRuntimeException();
            return false;
          }
        }
      }
      throw new IllegalArgumentException("Activity must be resumed.");
    }
    throw new NullPointerException("activity or service is null");
  }
  
  public int getMaxNumOfRegisterableSystemCodes()
  {
    try
    {
      i = sService.getMaxNumOfRegisterableSystemCodes();
      return i;
    }
    catch (RemoteException localRemoteException1)
    {
      int i;
      recoverService();
      if (sService == null)
      {
        Log.e("NfcFCardEmulation", "Failed to recover CardEmulationService.");
        return -1;
      }
      try
      {
        i = sService.getMaxNumOfRegisterableSystemCodes();
        return i;
      }
      catch (RemoteException localRemoteException2)
      {
        Log.e("NfcFCardEmulation", "Failed to reach CardEmulationService.");
      }
    }
    return -1;
  }
  
  public List<NfcFServiceInfo> getNfcFServices()
  {
    try
    {
      List localList1 = sService.getNfcFServices(mContext.getUserId());
      return localList1;
    }
    catch (RemoteException localRemoteException1)
    {
      recoverService();
      if (sService == null)
      {
        Log.e("NfcFCardEmulation", "Failed to recover CardEmulationService.");
        return null;
      }
      try
      {
        List localList2 = sService.getNfcFServices(mContext.getUserId());
        return localList2;
      }
      catch (RemoteException localRemoteException2)
      {
        Log.e("NfcFCardEmulation", "Failed to reach CardEmulationService.");
      }
    }
    return null;
  }
  
  public String getNfcid2ForService(ComponentName paramComponentName)
    throws RuntimeException
  {
    if (paramComponentName != null) {
      try
      {
        String str = sService.getNfcid2ForService(mContext.getUserId(), paramComponentName);
        return str;
      }
      catch (RemoteException localRemoteException)
      {
        recoverService();
        if (sService == null)
        {
          Log.e("NfcFCardEmulation", "Failed to recover CardEmulationService.");
          return null;
        }
        try
        {
          paramComponentName = sService.getNfcid2ForService(mContext.getUserId(), paramComponentName);
          return paramComponentName;
        }
        catch (RemoteException paramComponentName)
        {
          Log.e("NfcFCardEmulation", "Failed to reach CardEmulationService.");
          paramComponentName.rethrowAsRuntimeException();
          return null;
        }
      }
    }
    throw new NullPointerException("service is null");
  }
  
  public String getSystemCodeForService(ComponentName paramComponentName)
    throws RuntimeException
  {
    if (paramComponentName != null) {
      try
      {
        String str = sService.getSystemCodeForService(mContext.getUserId(), paramComponentName);
        return str;
      }
      catch (RemoteException localRemoteException)
      {
        recoverService();
        if (sService == null)
        {
          Log.e("NfcFCardEmulation", "Failed to recover CardEmulationService.");
          return null;
        }
        try
        {
          paramComponentName = sService.getSystemCodeForService(mContext.getUserId(), paramComponentName);
          return paramComponentName;
        }
        catch (RemoteException paramComponentName)
        {
          Log.e("NfcFCardEmulation", "Failed to reach CardEmulationService.");
          paramComponentName.rethrowAsRuntimeException();
          return null;
        }
      }
    }
    throw new NullPointerException("service is null");
  }
  
  void recoverService()
  {
    sService = NfcAdapter.getDefaultAdapter(mContext).getNfcFCardEmulationService();
  }
  
  public boolean registerSystemCodeForService(ComponentName paramComponentName, String paramString)
    throws RuntimeException
  {
    if ((paramComponentName != null) && (paramString != null)) {
      try
      {
        bool = sService.registerSystemCodeForService(mContext.getUserId(), paramComponentName, paramString);
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        boolean bool;
        recoverService();
        if (sService == null)
        {
          Log.e("NfcFCardEmulation", "Failed to recover CardEmulationService.");
          return false;
        }
        try
        {
          bool = sService.registerSystemCodeForService(mContext.getUserId(), paramComponentName, paramString);
          return bool;
        }
        catch (RemoteException paramComponentName)
        {
          Log.e("NfcFCardEmulation", "Failed to reach CardEmulationService.");
          paramComponentName.rethrowAsRuntimeException();
          return false;
        }
      }
    }
    throw new NullPointerException("service or systemCode is null");
  }
  
  public boolean setNfcid2ForService(ComponentName paramComponentName, String paramString)
    throws RuntimeException
  {
    if ((paramComponentName != null) && (paramString != null)) {
      try
      {
        bool = sService.setNfcid2ForService(mContext.getUserId(), paramComponentName, paramString);
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        boolean bool;
        recoverService();
        if (sService == null)
        {
          Log.e("NfcFCardEmulation", "Failed to recover CardEmulationService.");
          return false;
        }
        try
        {
          bool = sService.setNfcid2ForService(mContext.getUserId(), paramComponentName, paramString);
          return bool;
        }
        catch (RemoteException paramComponentName)
        {
          Log.e("NfcFCardEmulation", "Failed to reach CardEmulationService.");
          paramComponentName.rethrowAsRuntimeException();
          return false;
        }
      }
    }
    throw new NullPointerException("service or nfcid2 is null");
  }
  
  public boolean unregisterSystemCodeForService(ComponentName paramComponentName)
    throws RuntimeException
  {
    if (paramComponentName != null) {
      try
      {
        bool = sService.removeSystemCodeForService(mContext.getUserId(), paramComponentName);
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        boolean bool;
        recoverService();
        if (sService == null)
        {
          Log.e("NfcFCardEmulation", "Failed to recover CardEmulationService.");
          return false;
        }
        try
        {
          bool = sService.removeSystemCodeForService(mContext.getUserId(), paramComponentName);
          return bool;
        }
        catch (RemoteException paramComponentName)
        {
          Log.e("NfcFCardEmulation", "Failed to reach CardEmulationService.");
          paramComponentName.rethrowAsRuntimeException();
          return false;
        }
      }
    }
    throw new NullPointerException("service is null");
  }
}
