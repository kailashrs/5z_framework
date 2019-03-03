package com.google.android.mms.util;

import android.content.Context;
import android.drm.DrmManagerClient;
import android.util.Log;

public class DownloadDrmHelper
{
  public static final String EXTENSION_DRM_MESSAGE = ".dm";
  public static final String EXTENSION_INTERNAL_FWDL = ".fl";
  public static final String MIMETYPE_DRM_MESSAGE = "application/vnd.oma.drm.message";
  private static final String TAG = "DownloadDrmHelper";
  
  public DownloadDrmHelper() {}
  
  public static String getOriginalMimeType(Context paramContext, String paramString1, String paramString2)
  {
    DrmManagerClient localDrmManagerClient = new DrmManagerClient(paramContext);
    paramContext = paramString2;
    try
    {
      if (localDrmManagerClient.canHandle(paramString1, null)) {
        paramContext = localDrmManagerClient.getOriginalMimeType(paramString1);
      }
    }
    catch (IllegalStateException paramContext)
    {
      Log.w("DownloadDrmHelper", "DrmManagerClient didn't initialize properly.");
      paramContext = paramString2;
    }
    catch (IllegalArgumentException paramContext)
    {
      for (;;)
      {
        Log.w("DownloadDrmHelper", "Can't get original mime type since path is null or empty string.");
        paramContext = paramString2;
      }
    }
    return paramContext;
  }
  
  public static boolean isDrmConvertNeeded(String paramString)
  {
    return "application/vnd.oma.drm.message".equals(paramString);
  }
  
  public static boolean isDrmMimeType(Context paramContext, String paramString)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = bool1;
    if (paramContext != null) {
      try
      {
        DrmManagerClient localDrmManagerClient = new android/drm/DrmManagerClient;
        localDrmManagerClient.<init>(paramContext);
        bool3 = bool2;
        if (paramString != null)
        {
          bool3 = bool2;
          if (paramString.length() > 0) {
            bool3 = localDrmManagerClient.canHandle("", paramString);
          }
        }
      }
      catch (IllegalStateException paramContext)
      {
        Log.w("DownloadDrmHelper", "DrmManagerClient didn't initialize properly.");
        bool3 = bool1;
      }
      catch (IllegalArgumentException paramContext)
      {
        for (;;)
        {
          Log.w("DownloadDrmHelper", "DrmManagerClient instance could not be created, context is Illegal.");
          bool3 = bool2;
        }
      }
    }
    return bool3;
  }
  
  public static String modifyDrmFwLockFileExtension(String paramString)
  {
    String str = paramString;
    if (paramString != null)
    {
      int i = paramString.lastIndexOf(".");
      str = paramString;
      if (i != -1) {
        str = paramString.substring(0, i);
      }
      str = str.concat(".fl");
    }
    return str;
  }
}
