package android.telephony.mbms;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MbmsUtils
{
  private static final String LOG_TAG = "MbmsUtils";
  
  public MbmsUtils() {}
  
  public static File getEmbmsTempFileDirForService(Context paramContext, String paramString)
  {
    paramString = paramString.replaceAll("[^a-zA-Z0-9_]", "_");
    return new File(MbmsTempFileProvider.getEmbmsTempFileDir(paramContext), paramString);
  }
  
  public static ServiceInfo getMiddlewareServiceInfo(Context paramContext, String paramString)
  {
    PackageManager localPackageManager = paramContext.getPackageManager();
    Intent localIntent = new Intent();
    localIntent.setAction(paramString);
    paramContext = getOverrideServiceName(paramContext, paramString);
    if (paramContext == null)
    {
      paramContext = localPackageManager.queryIntentServices(localIntent, 1048576);
    }
    else
    {
      localIntent.setComponent(paramContext);
      paramContext = localPackageManager.queryIntentServices(localIntent, 131072);
    }
    if ((paramContext != null) && (paramContext.size() != 0))
    {
      if (paramContext.size() > 1)
      {
        Log.w("MbmsUtils", "More than one MBMS service found, cannot get unique service");
        return null;
      }
      return get0serviceInfo;
    }
    Log.w("MbmsUtils", "No MBMS services found, cannot get service info");
    return null;
  }
  
  public static ComponentName getOverrideServiceName(Context paramContext, String paramString)
  {
    Object localObject = null;
    int i = paramString.hashCode();
    if (i != -1374878107)
    {
      if ((i == -407466459) && (paramString.equals("android.telephony.action.EmbmsDownload")))
      {
        i = 0;
        break label52;
      }
    }
    else if (paramString.equals("android.telephony.action.EmbmsStreaming"))
    {
      i = 1;
      break label52;
    }
    i = -1;
    switch (i)
    {
    default: 
      paramString = localObject;
      break;
    case 1: 
      paramString = "mbms-streaming-service-override";
      break;
    case 0: 
      label52:
      paramString = "mbms-download-service-override";
    }
    if (paramString == null) {
      return null;
    }
    try
    {
      paramContext = paramContext.getPackageManager().getApplicationInfo(paramContext.getPackageName(), 128);
      if (metaData == null) {
        return null;
      }
      paramContext = metaData.getString(paramString);
      if (paramContext == null) {
        return null;
      }
      return ComponentName.unflattenFromString(paramContext);
    }
    catch (PackageManager.NameNotFoundException paramContext) {}
    return null;
  }
  
  public static boolean isContainedIn(File paramFile1, File paramFile2)
  {
    try
    {
      paramFile1 = paramFile1.getCanonicalPath();
      boolean bool = paramFile2.getCanonicalPath().startsWith(paramFile1);
      return bool;
    }
    catch (IOException paramFile1)
    {
      paramFile2 = new StringBuilder();
      paramFile2.append("Failed to resolve canonical paths: ");
      paramFile2.append(paramFile1);
      throw new RuntimeException(paramFile2.toString());
    }
  }
  
  public static int startBinding(Context paramContext, String paramString, ServiceConnection paramServiceConnection)
  {
    Intent localIntent = new Intent();
    paramString = getMiddlewareServiceInfo(paramContext, paramString);
    if (paramString == null) {
      return 1;
    }
    localIntent.setComponent(toComponentName(paramString));
    paramContext.bindService(localIntent, paramServiceConnection, 1);
    return 0;
  }
  
  public static ComponentName toComponentName(ComponentInfo paramComponentInfo)
  {
    return new ComponentName(packageName, name);
  }
}
