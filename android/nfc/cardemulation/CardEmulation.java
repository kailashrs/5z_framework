package android.nfc.cardemulation;

import android.app.Activity;
import android.app.ActivityThread;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.IPackageManager;
import android.nfc.INfcCardEmulation;
import android.nfc.NfcAdapter;
import android.os.RemoteException;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import java.util.HashMap;
import java.util.List;

public final class CardEmulation
{
  public static final String ACTION_CHANGE_DEFAULT = "android.nfc.cardemulation.action.ACTION_CHANGE_DEFAULT";
  public static final String CATEGORY_OTHER = "other";
  public static final String CATEGORY_PAYMENT = "payment";
  public static final String EXTRA_CATEGORY = "category";
  public static final String EXTRA_SERVICE_COMPONENT = "component";
  public static final int SELECTION_MODE_ALWAYS_ASK = 1;
  public static final int SELECTION_MODE_ASK_IF_CONFLICT = 2;
  public static final int SELECTION_MODE_PREFER_DEFAULT = 0;
  static final String TAG = "CardEmulation";
  static HashMap<Context, CardEmulation> sCardEmus = new HashMap();
  static boolean sIsInitialized = false;
  static INfcCardEmulation sService;
  final Context mContext;
  
  private CardEmulation(Context paramContext, INfcCardEmulation paramINfcCardEmulation)
  {
    mContext = paramContext.getApplicationContext();
    sService = paramINfcCardEmulation;
  }
  
  public static CardEmulation getInstance(NfcAdapter paramNfcAdapter)
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
              boolean bool = ((IPackageManager)localObject).hasSystemFeature("android.hardware.nfc.hce", 0);
              if (bool)
              {
                sIsInitialized = true;
                break label106;
              }
              Log.e("CardEmulation", "This device does not support card emulation");
              paramNfcAdapter = new java/lang/UnsupportedOperationException;
              paramNfcAdapter.<init>();
              throw paramNfcAdapter;
            }
            catch (RemoteException paramNfcAdapter)
            {
              Log.e("CardEmulation", "PackageManager query failed.");
              paramNfcAdapter = new java/lang/UnsupportedOperationException;
              paramNfcAdapter.<init>();
              throw paramNfcAdapter;
            }
          }
          Log.e("CardEmulation", "Cannot get PackageManager");
          paramNfcAdapter = new java/lang/UnsupportedOperationException;
          paramNfcAdapter.<init>();
          throw paramNfcAdapter;
        }
        label106:
        CardEmulation localCardEmulation = (CardEmulation)sCardEmus.get(localContext);
        Object localObject = localCardEmulation;
        if (localCardEmulation == null)
        {
          paramNfcAdapter = paramNfcAdapter.getCardEmulationService();
          if (paramNfcAdapter != null)
          {
            localObject = new android/nfc/cardemulation/CardEmulation;
            ((CardEmulation)localObject).<init>(localContext, paramNfcAdapter);
            sCardEmus.put(localContext, localObject);
          }
          else
          {
            Log.e("CardEmulation", "This device does not implement the INfcCardEmulation interface.");
            paramNfcAdapter = new java/lang/UnsupportedOperationException;
            paramNfcAdapter.<init>();
            throw paramNfcAdapter;
          }
        }
        return localObject;
      }
      Log.e("CardEmulation", "NfcAdapter context is null.");
      paramNfcAdapter = new java/lang/UnsupportedOperationException;
      paramNfcAdapter.<init>();
      throw paramNfcAdapter;
    }
    finally {}
    paramNfcAdapter = new java/lang/NullPointerException;
    paramNfcAdapter.<init>("NfcAdapter is null");
    throw paramNfcAdapter;
  }
  
  public static boolean isValidAid(String paramString)
  {
    if (paramString == null) {
      return false;
    }
    StringBuilder localStringBuilder;
    if (((paramString.endsWith("*")) || (paramString.endsWith("#"))) && (paramString.length() % 2 == 0))
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("AID ");
      localStringBuilder.append(paramString);
      localStringBuilder.append(" is not a valid AID.");
      Log.e("CardEmulation", localStringBuilder.toString());
      return false;
    }
    if ((!paramString.endsWith("*")) && (!paramString.endsWith("#")) && (paramString.length() % 2 != 0))
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("AID ");
      localStringBuilder.append(paramString);
      localStringBuilder.append(" is not a valid AID.");
      Log.e("CardEmulation", localStringBuilder.toString());
      return false;
    }
    if (!paramString.matches("[0-9A-Fa-f]{10,32}\\*?\\#?"))
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("AID ");
      localStringBuilder.append(paramString);
      localStringBuilder.append(" is not a valid AID.");
      Log.e("CardEmulation", localStringBuilder.toString());
      return false;
    }
    return true;
  }
  
  public boolean categoryAllowsForegroundPreference(String paramString)
  {
    if ("payment".equals(paramString))
    {
      boolean bool1 = false;
      boolean bool2 = false;
      try
      {
        int i = Settings.Secure.getInt(mContext.getContentResolver(), "nfc_payment_foreground");
        bool2 = bool1;
        if (i != 0) {
          bool2 = true;
        }
      }
      catch (Settings.SettingNotFoundException paramString) {}
      return bool2;
    }
    return true;
  }
  
  public List<String> getAidsForService(ComponentName paramComponentName, String paramString)
  {
    Object localObject = null;
    List localList = null;
    try
    {
      AidGroup localAidGroup = sService.getAidGroupForService(mContext.getUserId(), paramComponentName, paramString);
      if (localAidGroup != null) {
        localList = localAidGroup.getAids();
      }
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      recoverService();
      if (sService == null)
      {
        Log.e("CardEmulation", "Failed to recover CardEmulationService.");
        return null;
      }
      try
      {
        paramString = sService.getAidGroupForService(mContext.getUserId(), paramComponentName, paramString);
        paramComponentName = localObject;
        if (paramString != null) {
          paramComponentName = paramString.getAids();
        }
        return paramComponentName;
      }
      catch (RemoteException paramComponentName)
      {
        Log.e("CardEmulation", "Failed to recover CardEmulationService.");
      }
    }
    return null;
  }
  
  public int getSelectionModeForCategory(String paramString)
  {
    if ("payment".equals(paramString))
    {
      if (Settings.Secure.getString(mContext.getContentResolver(), "nfc_payment_default_component") != null) {
        return 0;
      }
      return 1;
    }
    return 2;
  }
  
  public List<ApduServiceInfo> getServices(String paramString)
  {
    try
    {
      List localList = sService.getServices(mContext.getUserId(), paramString);
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      recoverService();
      if (sService == null)
      {
        Log.e("CardEmulation", "Failed to recover CardEmulationService.");
        return null;
      }
      try
      {
        paramString = sService.getServices(mContext.getUserId(), paramString);
        return paramString;
      }
      catch (RemoteException paramString)
      {
        Log.e("CardEmulation", "Failed to reach CardEmulationService.");
      }
    }
    return null;
  }
  
  public boolean isDefaultServiceForAid(ComponentName paramComponentName, String paramString)
  {
    try
    {
      bool = sService.isDefaultServiceForAid(mContext.getUserId(), paramComponentName, paramString);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      boolean bool;
      recoverService();
      if (sService == null)
      {
        Log.e("CardEmulation", "Failed to recover CardEmulationService.");
        return false;
      }
      try
      {
        bool = sService.isDefaultServiceForAid(mContext.getUserId(), paramComponentName, paramString);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        Log.e("CardEmulation", "Failed to reach CardEmulationService.");
      }
    }
    return false;
  }
  
  public boolean isDefaultServiceForCategory(ComponentName paramComponentName, String paramString)
  {
    try
    {
      bool = sService.isDefaultServiceForCategory(mContext.getUserId(), paramComponentName, paramString);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      boolean bool;
      recoverService();
      if (sService == null)
      {
        Log.e("CardEmulation", "Failed to recover CardEmulationService.");
        return false;
      }
      try
      {
        bool = sService.isDefaultServiceForCategory(mContext.getUserId(), paramComponentName, paramString);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        Log.e("CardEmulation", "Failed to recover CardEmulationService.");
      }
    }
    return false;
  }
  
  void recoverService()
  {
    sService = NfcAdapter.getDefaultAdapter(mContext).getCardEmulationService();
  }
  
  public boolean registerAidsForService(ComponentName paramComponentName, String paramString, List<String> paramList)
  {
    paramString = new AidGroup(paramList, paramString);
    try
    {
      bool = sService.registerAidGroupForService(mContext.getUserId(), paramComponentName, paramString);
      return bool;
    }
    catch (RemoteException paramList)
    {
      boolean bool;
      recoverService();
      if (sService == null)
      {
        Log.e("CardEmulation", "Failed to recover CardEmulationService.");
        return false;
      }
      try
      {
        bool = sService.registerAidGroupForService(mContext.getUserId(), paramComponentName, paramString);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        Log.e("CardEmulation", "Failed to reach CardEmulationService.");
      }
    }
    return false;
  }
  
  public boolean removeAidsForService(ComponentName paramComponentName, String paramString)
  {
    try
    {
      bool = sService.removeAidGroupForService(mContext.getUserId(), paramComponentName, paramString);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      boolean bool;
      recoverService();
      if (sService == null)
      {
        Log.e("CardEmulation", "Failed to recover CardEmulationService.");
        return false;
      }
      try
      {
        bool = sService.removeAidGroupForService(mContext.getUserId(), paramComponentName, paramString);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        Log.e("CardEmulation", "Failed to reach CardEmulationService.");
      }
    }
    return false;
  }
  
  public boolean setDefaultForNextTap(ComponentName paramComponentName)
  {
    try
    {
      bool = sService.setDefaultForNextTap(mContext.getUserId(), paramComponentName);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      boolean bool;
      recoverService();
      if (sService == null)
      {
        Log.e("CardEmulation", "Failed to recover CardEmulationService.");
        return false;
      }
      try
      {
        bool = sService.setDefaultForNextTap(mContext.getUserId(), paramComponentName);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        Log.e("CardEmulation", "Failed to reach CardEmulationService.");
      }
    }
    return false;
  }
  
  public boolean setDefaultServiceForCategory(ComponentName paramComponentName, String paramString)
  {
    try
    {
      bool = sService.setDefaultServiceForCategory(mContext.getUserId(), paramComponentName, paramString);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      boolean bool;
      recoverService();
      if (sService == null)
      {
        Log.e("CardEmulation", "Failed to recover CardEmulationService.");
        return false;
      }
      try
      {
        bool = sService.setDefaultServiceForCategory(mContext.getUserId(), paramComponentName, paramString);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        Log.e("CardEmulation", "Failed to reach CardEmulationService.");
      }
    }
    return false;
  }
  
  public boolean setPreferredService(Activity paramActivity, ComponentName paramComponentName)
  {
    if ((paramActivity != null) && (paramComponentName != null))
    {
      if (paramActivity.isResumed()) {
        try
        {
          bool = sService.setPreferredService(paramComponentName);
          return bool;
        }
        catch (RemoteException paramActivity)
        {
          boolean bool;
          recoverService();
          if (sService == null)
          {
            Log.e("CardEmulation", "Failed to recover CardEmulationService.");
            return false;
          }
          try
          {
            bool = sService.setPreferredService(paramComponentName);
            return bool;
          }
          catch (RemoteException paramActivity)
          {
            Log.e("CardEmulation", "Failed to reach CardEmulationService.");
            return false;
          }
        }
      }
      throw new IllegalArgumentException("Activity must be resumed.");
    }
    throw new NullPointerException("activity or service or category is null");
  }
  
  public boolean supportsAidPrefixRegistration()
  {
    try
    {
      bool = sService.supportsAidPrefixRegistration();
      return bool;
    }
    catch (RemoteException localRemoteException1)
    {
      boolean bool;
      recoverService();
      if (sService == null)
      {
        Log.e("CardEmulation", "Failed to recover CardEmulationService.");
        return false;
      }
      try
      {
        bool = sService.supportsAidPrefixRegistration();
        return bool;
      }
      catch (RemoteException localRemoteException2)
      {
        Log.e("CardEmulation", "Failed to reach CardEmulationService.");
      }
    }
    return false;
  }
  
  public boolean unsetPreferredService(Activity paramActivity)
  {
    if (paramActivity != null)
    {
      if (paramActivity.isResumed()) {
        try
        {
          bool = sService.unsetPreferredService();
          return bool;
        }
        catch (RemoteException paramActivity)
        {
          boolean bool;
          recoverService();
          if (sService == null)
          {
            Log.e("CardEmulation", "Failed to recover CardEmulationService.");
            return false;
          }
          try
          {
            bool = sService.unsetPreferredService();
            return bool;
          }
          catch (RemoteException paramActivity)
          {
            Log.e("CardEmulation", "Failed to reach CardEmulationService.");
            return false;
          }
        }
      }
      throw new IllegalArgumentException("Activity must be resumed.");
    }
    throw new NullPointerException("activity is null");
  }
}
