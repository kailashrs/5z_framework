package com.android.internal.telephony;

import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.provider.Telephony.Sms.Intents;
import android.telephony.SmsMessage;
import android.util.ArrayMap;
import android.util.Base64;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class AppSmsManager
{
  private static final String LOG_TAG = "AppSmsManager";
  private final Context mContext;
  private final Object mLock = new Object();
  @GuardedBy("mLock")
  private final Map<String, AppRequestInfo> mPackageMap = new ArrayMap();
  private final SecureRandom mRandom = new SecureRandom();
  @GuardedBy("mLock")
  private final Map<String, AppRequestInfo> mTokenMap = new ArrayMap();
  
  public AppSmsManager(Context paramContext)
  {
    mContext = paramContext;
  }
  
  private void addRequestLocked(AppRequestInfo paramAppRequestInfo)
  {
    mTokenMap.put(token, paramAppRequestInfo);
    mPackageMap.put(packageName, paramAppRequestInfo);
  }
  
  private AppRequestInfo findAppRequestInfoSmsIntentLocked(Intent paramIntent)
  {
    Object localObject1 = Telephony.Sms.Intents.getMessagesFromIntent(paramIntent);
    if (localObject1 == null) {
      return null;
    }
    Object localObject2 = new StringBuilder();
    int i = localObject1.length;
    for (int j = 0; j < i; j++)
    {
      paramIntent = localObject1[j];
      if ((paramIntent != null) && (paramIntent.getMessageBody() != null)) {
        ((StringBuilder)localObject2).append(paramIntent.getMessageBody());
      }
    }
    paramIntent = ((StringBuilder)localObject2).toString();
    localObject1 = mTokenMap.keySet().iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (String)((Iterator)localObject1).next();
      if (paramIntent.contains((CharSequence)localObject2)) {
        return (AppRequestInfo)mTokenMap.get(localObject2);
      }
    }
    return null;
  }
  
  private String generateNonce()
  {
    byte[] arrayOfByte = new byte[8];
    mRandom.nextBytes(arrayOfByte);
    return Base64.encodeToString(arrayOfByte, 11);
  }
  
  private void removeRequestLocked(AppRequestInfo paramAppRequestInfo)
  {
    mTokenMap.remove(token);
    mPackageMap.remove(packageName);
  }
  
  public String createAppSpecificSmsToken(String paramString, PendingIntent paramPendingIntent)
  {
    ((AppOpsManager)mContext.getSystemService("appops")).checkPackage(Binder.getCallingUid(), paramString);
    String str = generateNonce();
    synchronized (mLock)
    {
      if (mPackageMap.containsKey(paramString)) {
        removeRequestLocked((AppRequestInfo)mPackageMap.get(paramString));
      }
      AppRequestInfo localAppRequestInfo = new com/android/internal/telephony/AppSmsManager$AppRequestInfo;
      localAppRequestInfo.<init>(this, paramString, paramPendingIntent, str);
      addRequestLocked(localAppRequestInfo);
      return str;
    }
  }
  
  public boolean handleSmsReceivedIntent(Intent paramIntent)
  {
    if (paramIntent.getAction() != "android.provider.Telephony.SMS_DELIVER")
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("Got intent with incorrect action: ");
      ((StringBuilder)???).append(paramIntent.getAction());
      Log.wtf("AppSmsManager", ((StringBuilder)???).toString());
      return false;
    }
    synchronized (mLock)
    {
      AppRequestInfo localAppRequestInfo = findAppRequestInfoSmsIntentLocked(paramIntent);
      if (localAppRequestInfo == null) {
        return false;
      }
      try
      {
        Intent localIntent = new android/content/Intent;
        localIntent.<init>();
        localIntent.putExtras(paramIntent.getExtras());
        pendingIntent.send(mContext, 0, localIntent);
        removeRequestLocked(localAppRequestInfo);
        return true;
      }
      catch (PendingIntent.CanceledException paramIntent)
      {
        removeRequestLocked(localAppRequestInfo);
        return false;
      }
    }
  }
  
  private final class AppRequestInfo
  {
    public final String packageName;
    public final PendingIntent pendingIntent;
    public final String token;
    
    AppRequestInfo(String paramString1, PendingIntent paramPendingIntent, String paramString2)
    {
      packageName = paramString1;
      pendingIntent = paramPendingIntent;
      token = paramString2;
    }
  }
}
