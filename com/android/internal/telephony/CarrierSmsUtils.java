package com.android.internal.telephony;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.Rlog;
import java.util.Iterator;
import java.util.List;

public class CarrierSmsUtils
{
  private static final String CARRIER_IMS_PACKAGE_KEY = "config_ims_package_override_string";
  protected static final String TAG = CarrierSmsUtils.class.getSimpleName();
  protected static final boolean VDBG = false;
  
  private CarrierSmsUtils() {}
  
  private static String getCarrierImsPackage(Context paramContext, Phone paramPhone)
  {
    paramContext = (CarrierConfigManager)paramContext.getSystemService("carrier_config");
    if (paramContext == null)
    {
      Rlog.e(TAG, "Failed to retrieve CarrierConfigManager");
      return null;
    }
    long l = Binder.clearCallingIdentity();
    try
    {
      paramContext = paramContext.getConfigForSubId(paramPhone.getSubId());
      if (paramContext == null) {
        return null;
      }
      paramContext = paramContext.getString("config_ims_package_override_string", null);
      return paramContext;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  public static String getCarrierImsPackageForIntent(Context paramContext, Phone paramPhone, Intent paramIntent)
  {
    paramPhone = getCarrierImsPackage(paramContext, paramPhone);
    if (paramPhone == null) {
      return null;
    }
    paramContext = paramContext.getPackageManager().queryIntentServices(paramIntent, 0).iterator();
    while (paramContext.hasNext())
    {
      ResolveInfo localResolveInfo = (ResolveInfo)paramContext.next();
      if (serviceInfo == null)
      {
        paramIntent = TAG;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Can't get service information from ");
        localStringBuilder.append(localResolveInfo);
        Rlog.e(paramIntent, localStringBuilder.toString());
      }
      else if (paramPhone.equals(serviceInfo.packageName))
      {
        return paramPhone;
      }
    }
    return null;
  }
}
