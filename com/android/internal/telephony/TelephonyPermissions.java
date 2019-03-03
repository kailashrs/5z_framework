package com.android.internal.telephony;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.RemoteException;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import com.android.internal.annotations.VisibleForTesting;
import java.util.function.Supplier;

public final class TelephonyPermissions
{
  private static final boolean DBG = false;
  private static final String LOG_TAG = "TelephonyPermissions";
  private static final Supplier<ITelephony> TELEPHONY_SUPPLIER = _..Lambda.TelephonyPermissions.LxEEC4irBSbjD1lSC4EeVLgFY9I.INSTANCE;
  
  private TelephonyPermissions() {}
  
  public static boolean checkCallingOrSelfReadPhoneNumber(Context paramContext, int paramInt, String paramString1, String paramString2)
  {
    return checkReadPhoneNumber(paramContext, TELEPHONY_SUPPLIER, paramInt, Binder.getCallingPid(), Binder.getCallingUid(), paramString1, paramString2);
  }
  
  public static boolean checkCallingOrSelfReadPhoneState(Context paramContext, int paramInt, String paramString1, String paramString2)
  {
    return checkReadPhoneState(paramContext, paramInt, Binder.getCallingPid(), Binder.getCallingUid(), paramString1, paramString2);
  }
  
  public static boolean checkReadCallLog(Context paramContext, int paramInt1, int paramInt2, int paramInt3, String paramString)
  {
    return checkReadCallLog(paramContext, TELEPHONY_SUPPLIER, paramInt1, paramInt2, paramInt3, paramString);
  }
  
  @VisibleForTesting
  public static boolean checkReadCallLog(Context paramContext, Supplier<ITelephony> paramSupplier, int paramInt1, int paramInt2, int paramInt3, String paramString)
  {
    paramInt2 = paramContext.checkPermission("android.permission.READ_CALL_LOG", paramInt2, paramInt3);
    boolean bool = false;
    if (paramInt2 != 0)
    {
      if (SubscriptionManager.isValidSubscriptionId(paramInt1))
      {
        enforceCarrierPrivilege(paramSupplier, paramInt1, paramInt3, "readCallLog");
        return true;
      }
      return false;
    }
    if (((AppOpsManager)paramContext.getSystemService("appops")).noteOp(6, paramInt3, paramString) == 0) {
      bool = true;
    }
    return bool;
  }
  
  @VisibleForTesting
  public static boolean checkReadPhoneNumber(Context paramContext, Supplier<ITelephony> paramSupplier, int paramInt1, int paramInt2, int paramInt3, String paramString1, String paramString2)
  {
    AppOpsManager localAppOpsManager = (AppOpsManager)paramContext.getSystemService("appops");
    if (localAppOpsManager.noteOp(15, paramInt3, paramString1) == 0) {
      return true;
    }
    try
    {
      bool1 = checkReadPhoneState(paramContext, paramSupplier, paramInt1, paramInt2, paramInt3, paramString1, paramString2);
      return bool1;
    }
    catch (SecurityException paramSupplier)
    {
      boolean bool2 = false;
      boolean bool1 = false;
      try
      {
        paramContext.enforcePermission("android.permission.READ_SMS", paramInt2, paramInt3, paramString2);
        paramInt1 = AppOpsManager.permissionToOpCode("android.permission.READ_SMS");
        if (paramInt1 != -1)
        {
          paramInt1 = localAppOpsManager.noteOp(paramInt1, paramInt3, paramString1);
          if (paramInt1 == 0) {
            bool1 = true;
          }
          return bool1;
        }
        return true;
      }
      catch (SecurityException paramSupplier)
      {
        try
        {
          paramContext.enforcePermission("android.permission.READ_PHONE_NUMBERS", paramInt2, paramInt3, paramString2);
          paramInt1 = AppOpsManager.permissionToOpCode("android.permission.READ_PHONE_NUMBERS");
          if (paramInt1 != -1)
          {
            paramInt1 = localAppOpsManager.noteOp(paramInt1, paramInt3, paramString1);
            bool1 = bool2;
            if (paramInt1 == 0) {
              bool1 = true;
            }
            return bool1;
          }
          return true;
        }
        catch (SecurityException paramContext)
        {
          paramContext = new StringBuilder();
          paramContext.append(paramString2);
          paramContext.append(": Neither user ");
          paramContext.append(paramInt3);
          paramContext.append(" nor current process has ");
          paramContext.append("android.permission.READ_PHONE_STATE");
          paramContext.append(", ");
          paramContext.append("android.permission.READ_SMS");
          paramContext.append(", or ");
          paramContext.append("android.permission.READ_PHONE_NUMBERS");
          throw new SecurityException(paramContext.toString());
        }
      }
    }
  }
  
  public static boolean checkReadPhoneState(Context paramContext, int paramInt1, int paramInt2, int paramInt3, String paramString1, String paramString2)
  {
    return checkReadPhoneState(paramContext, TELEPHONY_SUPPLIER, paramInt1, paramInt2, paramInt3, paramString1, paramString2);
  }
  
  @VisibleForTesting
  public static boolean checkReadPhoneState(Context paramContext, Supplier<ITelephony> paramSupplier, int paramInt1, int paramInt2, int paramInt3, String paramString1, String paramString2)
  {
    boolean bool = true;
    try
    {
      paramContext.enforcePermission("android.permission.READ_PRIVILEGED_PHONE_STATE", paramInt2, paramInt3, paramString2);
      return true;
    }
    catch (SecurityException localSecurityException)
    {
      try
      {
        paramContext.enforcePermission("android.permission.READ_PHONE_STATE", paramInt2, paramInt3, paramString2);
        if (((AppOpsManager)paramContext.getSystemService("appops")).noteOp(51, paramInt3, paramString1) != 0) {
          bool = false;
        }
        return bool;
      }
      catch (SecurityException paramContext)
      {
        if (SubscriptionManager.isValidSubscriptionId(paramInt1))
        {
          enforceCarrierPrivilege(paramSupplier, paramInt1, paramInt3, paramString2);
          return true;
        }
        throw paramContext;
      }
    }
  }
  
  public static void enforceCallingOrSelfCarrierPrivilege(int paramInt, String paramString)
  {
    enforceCarrierPrivilege(paramInt, Binder.getCallingUid(), paramString);
  }
  
  public static void enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(Context paramContext, int paramInt, String paramString)
  {
    if (paramContext.checkCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE") == 0) {
      return;
    }
    enforceCallingOrSelfCarrierPrivilege(paramInt, paramString);
  }
  
  private static void enforceCarrierPrivilege(int paramInt1, int paramInt2, String paramString)
  {
    enforceCarrierPrivilege(TELEPHONY_SUPPLIER, paramInt1, paramInt2, paramString);
  }
  
  private static void enforceCarrierPrivilege(Supplier<ITelephony> paramSupplier, int paramInt1, int paramInt2, String paramString)
  {
    if (getCarrierPrivilegeStatus(paramSupplier, paramInt1, paramInt2) == 1) {
      return;
    }
    throw new SecurityException(paramString);
  }
  
  private static int getCarrierPrivilegeStatus(Supplier<ITelephony> paramSupplier, int paramInt1, int paramInt2)
  {
    paramSupplier = (ITelephony)paramSupplier.get();
    if (paramSupplier != null) {
      try
      {
        paramInt1 = paramSupplier.getCarrierPrivilegeStatusForUid(paramInt1, paramInt2);
        return paramInt1;
      }
      catch (RemoteException paramSupplier) {}
    }
    Rlog.e("TelephonyPermissions", "Phone process is down, cannot check carrier privileges");
    return 0;
  }
}
