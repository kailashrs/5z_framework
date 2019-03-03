package com.android.internal.telephony;

import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Binder;
import android.os.Process;
import android.os.UserHandle;
import android.provider.Settings.Secure;
import android.telephony.Rlog;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import com.android.internal.content.PackageMonitor;
import com.android.internal.logging.MetricsLogger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public final class SmsApplication
{
  private static final String BLUETOOTH_PACKAGE_NAME = "com.android.bluetooth";
  private static final boolean DEBUG_MULTIUSER = false;
  static final String LOG_TAG = "SmsApplication";
  private static final String MMS_SERVICE_PACKAGE_NAME = "com.android.mms.service";
  private static final String PHONE_PACKAGE_NAME = "com.android.phone";
  private static final String SCHEME_MMS = "mms";
  private static final String SCHEME_MMSTO = "mmsto";
  private static final String SCHEME_SMS = "sms";
  private static final String SCHEME_SMSTO = "smsto";
  private static final String TELEPHONY_PROVIDER_PACKAGE_NAME = "com.android.providers.telephony";
  private static SmsPackageMonitor sSmsPackageMonitor = null;
  
  public SmsApplication() {}
  
  private static void assignWriteSmsPermissionToSystemApp(Context paramContext, PackageManager paramPackageManager, AppOpsManager paramAppOpsManager, String paramString)
  {
    if (paramPackageManager.checkSignatures(paramContext.getPackageName(), paramString) != 0)
    {
      paramContext = new StringBuilder();
      paramContext.append(paramString);
      paramContext.append(" does not have system signature");
      Rlog.e("SmsApplication", paramContext.toString());
      return;
    }
    try
    {
      paramPackageManager = paramPackageManager.getPackageInfo(paramString, 0);
      if (paramAppOpsManager.checkOp(15, applicationInfo.uid, paramString) != 0)
      {
        paramContext = new java/lang/StringBuilder;
        paramContext.<init>();
        paramContext.append(paramString);
        paramContext.append(" does not have OP_WRITE_SMS:  (fixing)");
        Rlog.w("SmsApplication", paramContext.toString());
        paramAppOpsManager.setMode(15, applicationInfo.uid, paramString, 0);
      }
    }
    catch (PackageManager.NameNotFoundException paramContext)
    {
      paramContext = new StringBuilder();
      paramContext.append("Package not found: ");
      paramContext.append(paramString);
      Rlog.e("SmsApplication", paramContext.toString());
    }
  }
  
  private static void assignWriteSmsPermissionToSystemUid(AppOpsManager paramAppOpsManager, int paramInt)
  {
    paramAppOpsManager.setUidMode(15, paramInt, 0);
  }
  
  private static void configurePreferredActivity(PackageManager paramPackageManager, ComponentName paramComponentName, int paramInt)
  {
    replacePreferredActivity(paramPackageManager, paramComponentName, paramInt, "sms");
    replacePreferredActivity(paramPackageManager, paramComponentName, paramInt, "smsto");
    replacePreferredActivity(paramPackageManager, paramComponentName, paramInt, "mms");
    replacePreferredActivity(paramPackageManager, paramComponentName, paramInt, "mmsto");
  }
  
  private static SmsApplicationData getApplication(Context paramContext, boolean paramBoolean, int paramInt)
  {
    if (!((TelephonyManager)paramContext.getSystemService("phone")).isSmsCapable()) {
      return null;
    }
    Object localObject1 = getApplicationCollectionInternal(paramContext, paramInt);
    Object localObject2 = Settings.Secure.getStringForUser(paramContext.getContentResolver(), "sms_default_application", paramInt);
    Object localObject3 = null;
    if (localObject2 != null) {
      localObject3 = getApplicationForPackage((Collection)localObject1, (String)localObject2);
    }
    localObject2 = localObject3;
    if (paramBoolean)
    {
      localObject2 = localObject3;
      if (localObject3 == null)
      {
        localObject2 = getApplicationForPackage((Collection)localObject1, paramContext.getResources().getString(17039874));
        localObject3 = localObject2;
        if (localObject2 == null)
        {
          localObject3 = localObject2;
          if (((Collection)localObject1).size() != 0) {
            localObject3 = (SmsApplicationData)localObject1.toArray()[0];
          }
        }
        localObject2 = localObject3;
        if (localObject3 != null)
        {
          setDefaultApplicationInternal(mPackageName, paramContext, paramInt);
          localObject2 = localObject3;
        }
      }
    }
    localObject1 = localObject2;
    if (localObject2 != null)
    {
      AppOpsManager localAppOpsManager = (AppOpsManager)paramContext.getSystemService("appops");
      if (!paramBoolean)
      {
        localObject3 = localObject2;
        if (mUid != Process.myUid()) {}
      }
      else
      {
        localObject3 = localObject2;
        if (localAppOpsManager.checkOp(15, mUid, mPackageName) != 0)
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append(mPackageName);
          ((StringBuilder)localObject1).append(" lost OP_WRITE_SMS: ");
          if (paramBoolean) {
            localObject3 = " (fixing)";
          } else {
            localObject3 = " (no permission to fix)";
          }
          ((StringBuilder)localObject1).append((String)localObject3);
          Rlog.e("SmsApplication", ((StringBuilder)localObject1).toString());
          if (paramBoolean)
          {
            localAppOpsManager.setMode(15, mUid, mPackageName, 0);
            localObject3 = localObject2;
          }
          else
          {
            localObject3 = null;
          }
        }
      }
      localObject1 = localObject3;
      if (paramBoolean)
      {
        localObject2 = paramContext.getPackageManager();
        configurePreferredActivity((PackageManager)localObject2, new ComponentName(mPackageName, mSendToClass), paramInt);
        assignWriteSmsPermissionToSystemApp(paramContext, (PackageManager)localObject2, localAppOpsManager, "com.android.phone");
        assignWriteSmsPermissionToSystemApp(paramContext, (PackageManager)localObject2, localAppOpsManager, "com.android.bluetooth");
        assignWriteSmsPermissionToSystemApp(paramContext, (PackageManager)localObject2, localAppOpsManager, "com.android.mms.service");
        assignWriteSmsPermissionToSystemApp(paramContext, (PackageManager)localObject2, localAppOpsManager, "com.android.providers.telephony");
        assignWriteSmsPermissionToSystemUid(localAppOpsManager, 1001);
        localObject1 = localObject3;
      }
    }
    return localObject1;
  }
  
  public static Collection<SmsApplicationData> getApplicationCollection(Context paramContext)
  {
    int i = getIncomingUserId(paramContext);
    long l = Binder.clearCallingIdentity();
    try
    {
      paramContext = getApplicationCollectionInternal(paramContext, i);
      return paramContext;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  private static Collection<SmsApplicationData> getApplicationCollectionInternal(Context paramContext, int paramInt)
  {
    Object localObject1 = paramContext.getPackageManager();
    Object localObject2 = ((PackageManager)localObject1).queryBroadcastReceiversAsUser(new Intent("android.provider.Telephony.SMS_DELIVER"), 0, paramInt);
    paramContext = new HashMap();
    Object localObject3 = ((List)localObject2).iterator();
    while (((Iterator)localObject3).hasNext())
    {
      ActivityInfo localActivityInfo = nextactivityInfo;
      if ((localActivityInfo != null) && ("android.permission.BROADCAST_SMS".equals(permission)))
      {
        localObject4 = packageName;
        if (!paramContext.containsKey(localObject4))
        {
          localObject5 = new SmsApplicationData((String)localObject4, applicationInfo.uid);
          SmsApplicationData.access$002((SmsApplicationData)localObject5, name);
          paramContext.put(localObject4, localObject5);
        }
      }
    }
    localObject3 = new Intent("android.provider.Telephony.WAP_PUSH_DELIVER");
    ((Intent)localObject3).setDataAndType(null, "application/vnd.wap.mms-message");
    Object localObject4 = ((PackageManager)localObject1).queryBroadcastReceiversAsUser((Intent)localObject3, 0, paramInt).iterator();
    while (((Iterator)localObject4).hasNext())
    {
      localObject5 = nextactivityInfo;
      if ((localObject5 != null) && ("android.permission.BROADCAST_WAP_PUSH".equals(permission)))
      {
        localObject3 = (SmsApplicationData)paramContext.get(packageName);
        if (localObject3 != null) {
          SmsApplicationData.access$102((SmsApplicationData)localObject3, name);
        }
      }
    }
    localObject4 = ((PackageManager)localObject1).queryIntentServicesAsUser(new Intent("android.intent.action.RESPOND_VIA_MESSAGE", Uri.fromParts("smsto", "", null)), 0, paramInt).iterator();
    while (((Iterator)localObject4).hasNext())
    {
      localObject3 = nextserviceInfo;
      if ((localObject3 != null) && ("android.permission.SEND_RESPOND_VIA_MESSAGE".equals(permission)))
      {
        localObject5 = (SmsApplicationData)paramContext.get(packageName);
        if (localObject5 != null) {
          SmsApplicationData.access$202((SmsApplicationData)localObject5, name);
        }
      }
    }
    localObject3 = ((PackageManager)localObject1).queryIntentActivitiesAsUser(new Intent("android.intent.action.SENDTO", Uri.fromParts("smsto", "", null)), 0, paramInt).iterator();
    while (((Iterator)localObject3).hasNext())
    {
      localObject5 = nextactivityInfo;
      if (localObject5 != null)
      {
        localObject4 = (SmsApplicationData)paramContext.get(packageName);
        if (localObject4 != null) {
          SmsApplicationData.access$302((SmsApplicationData)localObject4, name);
        }
      }
    }
    localObject3 = new Intent("android.provider.action.DEFAULT_SMS_PACKAGE_CHANGED");
    localObject4 = ((PackageManager)localObject1).queryBroadcastReceiversAsUser((Intent)localObject3, 0, paramInt).iterator();
    while (((Iterator)localObject4).hasNext())
    {
      localObject3 = nextactivityInfo;
      if (localObject3 != null)
      {
        localObject5 = (SmsApplicationData)paramContext.get(packageName);
        if (localObject5 != null) {
          SmsApplicationData.access$402((SmsApplicationData)localObject5, name);
        }
      }
    }
    localObject3 = new Intent("android.provider.action.EXTERNAL_PROVIDER_CHANGE");
    localObject4 = ((PackageManager)localObject1).queryBroadcastReceiversAsUser((Intent)localObject3, 0, paramInt).iterator();
    while (((Iterator)localObject4).hasNext())
    {
      localObject3 = nextactivityInfo;
      if (localObject3 != null)
      {
        localObject5 = (SmsApplicationData)paramContext.get(packageName);
        if (localObject5 != null) {
          SmsApplicationData.access$502((SmsApplicationData)localObject5, name);
        }
      }
    }
    localObject3 = new Intent("android.provider.Telephony.SIM_FULL");
    Object localObject5 = ((PackageManager)localObject1).queryBroadcastReceiversAsUser((Intent)localObject3, 0, paramInt).iterator();
    while (((Iterator)localObject5).hasNext())
    {
      localObject3 = nextactivityInfo;
      if (localObject3 != null)
      {
        localObject1 = (SmsApplicationData)paramContext.get(packageName);
        if (localObject1 != null) {
          SmsApplicationData.access$602((SmsApplicationData)localObject1, name);
        }
      }
    }
    localObject2 = ((List)localObject2).iterator();
    while (((Iterator)localObject2).hasNext())
    {
      localObject1 = nextactivityInfo;
      if (localObject1 != null)
      {
        localObject3 = packageName;
        localObject1 = (SmsApplicationData)paramContext.get(localObject3);
        if ((localObject1 != null) && (!((SmsApplicationData)localObject1).isComplete())) {
          paramContext.remove(localObject3);
        }
      }
    }
    return paramContext.values();
  }
  
  private static SmsApplicationData getApplicationForPackage(Collection<SmsApplicationData> paramCollection, String paramString)
  {
    if (paramString == null) {
      return null;
    }
    paramCollection = paramCollection.iterator();
    while (paramCollection.hasNext())
    {
      SmsApplicationData localSmsApplicationData = (SmsApplicationData)paramCollection.next();
      if (mPackageName.contentEquals(paramString)) {
        return localSmsApplicationData;
      }
    }
    return null;
  }
  
  public static ComponentName getDefaultExternalTelephonyProviderChangedApplication(Context paramContext, boolean paramBoolean)
  {
    int i = getIncomingUserId(paramContext);
    long l = Binder.clearCallingIdentity();
    Object localObject = null;
    try
    {
      SmsApplicationData localSmsApplicationData = getApplication(paramContext, paramBoolean, i);
      paramContext = localObject;
      if (localSmsApplicationData != null)
      {
        paramContext = localObject;
        if (mProviderChangedReceiverClass != null) {
          paramContext = new ComponentName(mPackageName, mProviderChangedReceiverClass);
        }
      }
      return paramContext;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  public static ComponentName getDefaultMmsApplication(Context paramContext, boolean paramBoolean)
  {
    int i = getIncomingUserId(paramContext);
    long l = Binder.clearCallingIdentity();
    Object localObject = null;
    try
    {
      SmsApplicationData localSmsApplicationData = getApplication(paramContext, paramBoolean, i);
      paramContext = localObject;
      if (localSmsApplicationData != null) {
        paramContext = new ComponentName(mPackageName, mMmsReceiverClass);
      }
      return paramContext;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  public static ComponentName getDefaultRespondViaMessageApplication(Context paramContext, boolean paramBoolean)
  {
    int i = getIncomingUserId(paramContext);
    long l = Binder.clearCallingIdentity();
    Object localObject = null;
    try
    {
      SmsApplicationData localSmsApplicationData = getApplication(paramContext, paramBoolean, i);
      paramContext = localObject;
      if (localSmsApplicationData != null) {
        paramContext = new ComponentName(mPackageName, mRespondViaMessageClass);
      }
      return paramContext;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  public static ComponentName getDefaultSendToApplication(Context paramContext, boolean paramBoolean)
  {
    int i = getIncomingUserId(paramContext);
    long l = Binder.clearCallingIdentity();
    Object localObject = null;
    try
    {
      SmsApplicationData localSmsApplicationData = getApplication(paramContext, paramBoolean, i);
      paramContext = localObject;
      if (localSmsApplicationData != null) {
        paramContext = new ComponentName(mPackageName, mSendToClass);
      }
      return paramContext;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  public static ComponentName getDefaultSimFullApplication(Context paramContext, boolean paramBoolean)
  {
    int i = getIncomingUserId(paramContext);
    long l = Binder.clearCallingIdentity();
    Object localObject = null;
    try
    {
      SmsApplicationData localSmsApplicationData = getApplication(paramContext, paramBoolean, i);
      paramContext = localObject;
      if (localSmsApplicationData != null)
      {
        paramContext = localObject;
        if (mSimFullReceiverClass != null) {
          paramContext = new ComponentName(mPackageName, mSimFullReceiverClass);
        }
      }
      return paramContext;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  public static ComponentName getDefaultSmsApplication(Context paramContext, boolean paramBoolean)
  {
    int i = getIncomingUserId(paramContext);
    long l = Binder.clearCallingIdentity();
    Object localObject = null;
    try
    {
      SmsApplicationData localSmsApplicationData = getApplication(paramContext, paramBoolean, i);
      paramContext = localObject;
      if (localSmsApplicationData != null) {
        paramContext = new ComponentName(mPackageName, mSmsReceiverClass);
      }
      return paramContext;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  private static String getDefaultSmsApplicationPackageName(Context paramContext)
  {
    paramContext = getDefaultSmsApplication(paramContext, false);
    if (paramContext != null) {
      return paramContext.getPackageName();
    }
    return null;
  }
  
  private static int getIncomingUserId(Context paramContext)
  {
    int i = paramContext.getUserId();
    int j = Binder.getCallingUid();
    if (UserHandle.getAppId(j) < 10000) {
      return i;
    }
    return UserHandle.getUserId(j);
  }
  
  public static SmsApplicationData getSmsApplicationData(String paramString, Context paramContext)
  {
    return getApplicationForPackage(getApplicationCollection(paramContext), paramString);
  }
  
  public static void initSmsPackageMonitor(Context paramContext)
  {
    sSmsPackageMonitor = new SmsPackageMonitor(paramContext);
    sSmsPackageMonitor.register(paramContext, paramContext.getMainLooper(), UserHandle.ALL, false);
  }
  
  public static boolean isDefaultSmsApplication(Context paramContext, String paramString)
  {
    if (paramString == null) {
      return false;
    }
    boolean bool = paramString.contains("bluetooth");
    paramContext = getDefaultSmsApplicationPackageName(paramContext);
    return ((paramContext != null) && (paramContext.equals(paramString))) || ("com.android.bluetooth".equals(paramString)) || (bool);
  }
  
  private static void replacePreferredActivity(PackageManager paramPackageManager, ComponentName paramComponentName, int paramInt, String paramString)
  {
    Object localObject = paramPackageManager.queryIntentActivitiesAsUser(new Intent("android.intent.action.SENDTO", Uri.fromParts(paramString, "", null)), 65600, paramInt);
    int i = ((List)localObject).size();
    ComponentName[] arrayOfComponentName = new ComponentName[i];
    for (int j = 0; j < i; j++)
    {
      ResolveInfo localResolveInfo = (ResolveInfo)((List)localObject).get(j);
      arrayOfComponentName[j] = new ComponentName(activityInfo.packageName, activityInfo.name);
    }
    localObject = new IntentFilter();
    ((IntentFilter)localObject).addAction("android.intent.action.SENDTO");
    ((IntentFilter)localObject).addCategory("android.intent.category.DEFAULT");
    ((IntentFilter)localObject).addDataScheme(paramString);
    paramPackageManager.replacePreferredActivityAsUser((IntentFilter)localObject, 2129920, arrayOfComponentName, paramComponentName, paramInt);
  }
  
  public static void setDefaultApplication(String paramString, Context paramContext)
  {
    if (!((TelephonyManager)paramContext.getSystemService("phone")).isSmsCapable()) {
      return;
    }
    int i = getIncomingUserId(paramContext);
    long l = Binder.clearCallingIdentity();
    try
    {
      setDefaultApplicationInternal(paramString, paramContext, i);
      return;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  private static void setDefaultApplicationInternal(String paramString, Context paramContext, int paramInt)
  {
    String str = Settings.Secure.getStringForUser(paramContext.getContentResolver(), "sms_default_application", paramInt);
    if ((paramString != null) && (str != null) && (paramString.equals(str))) {
      return;
    }
    Object localObject1 = paramContext.getPackageManager();
    Object localObject2 = getApplicationCollection(paramContext);
    Object localObject3;
    if (str != null) {
      localObject3 = getApplicationForPackage((Collection)localObject2, str);
    } else {
      localObject3 = null;
    }
    paramString = getApplicationForPackage((Collection)localObject2, paramString);
    if (paramString != null)
    {
      localObject2 = (AppOpsManager)paramContext.getSystemService("appops");
      if (str != null) {
        try
        {
          ((AppOpsManager)localObject2).setMode(15, getPackageInfoAsUser0applicationInfo.uid, str, 1);
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Old SMS package not found: ");
          localStringBuilder.append(str);
          Rlog.w("SmsApplication", localStringBuilder.toString());
        }
      }
      Settings.Secure.putStringForUser(paramContext.getContentResolver(), "sms_default_application", mPackageName, paramInt);
      configurePreferredActivity((PackageManager)localObject1, new ComponentName(mPackageName, mSendToClass), paramInt);
      ((AppOpsManager)localObject2).setMode(15, mUid, mPackageName, 0);
      assignWriteSmsPermissionToSystemApp(paramContext, (PackageManager)localObject1, (AppOpsManager)localObject2, "com.android.phone");
      assignWriteSmsPermissionToSystemApp(paramContext, (PackageManager)localObject1, (AppOpsManager)localObject2, "com.android.bluetooth");
      assignWriteSmsPermissionToSystemApp(paramContext, (PackageManager)localObject1, (AppOpsManager)localObject2, "com.android.mms.service");
      assignWriteSmsPermissionToSystemApp(paramContext, (PackageManager)localObject1, (AppOpsManager)localObject2, "com.android.providers.telephony");
      assignWriteSmsPermissionToSystemUid((AppOpsManager)localObject2, 1001);
      if ((localObject3 != null) && (mSmsAppChangedReceiverClass != null))
      {
        localObject1 = new Intent("android.provider.action.DEFAULT_SMS_PACKAGE_CHANGED");
        ((Intent)localObject1).setComponent(new ComponentName(mPackageName, mSmsAppChangedReceiverClass));
        ((Intent)localObject1).putExtra("android.provider.extra.IS_DEFAULT_SMS_APP", false);
        paramContext.sendBroadcast((Intent)localObject1);
      }
      if (mSmsAppChangedReceiverClass != null)
      {
        localObject3 = new Intent("android.provider.action.DEFAULT_SMS_PACKAGE_CHANGED");
        ((Intent)localObject3).setComponent(new ComponentName(mPackageName, mSmsAppChangedReceiverClass));
        ((Intent)localObject3).putExtra("android.provider.extra.IS_DEFAULT_SMS_APP", true);
        paramContext.sendBroadcast((Intent)localObject3);
      }
      MetricsLogger.action(paramContext, 266, mPackageName);
    }
  }
  
  public static boolean shouldWriteMessageForPackage(String paramString, Context paramContext)
  {
    if (SmsManager.getDefault().getAutoPersisting()) {
      return true;
    }
    return isDefaultSmsApplication(paramContext, paramString) ^ true;
  }
  
  public static class SmsApplicationData
  {
    private String mApplicationName;
    private String mMmsReceiverClass;
    public String mPackageName;
    private String mProviderChangedReceiverClass;
    private String mRespondViaMessageClass;
    private String mSendToClass;
    private String mSimFullReceiverClass;
    private String mSmsAppChangedReceiverClass;
    private String mSmsReceiverClass;
    private int mUid;
    
    public SmsApplicationData(String paramString, int paramInt)
    {
      mPackageName = paramString;
      mUid = paramInt;
    }
    
    public String getApplicationName(Context paramContext)
    {
      if (mApplicationName == null)
      {
        Object localObject = paramContext.getPackageManager();
        paramContext = null;
        try
        {
          ApplicationInfo localApplicationInfo = ((PackageManager)localObject).getApplicationInfoAsUser(mPackageName, 0, UserHandle.getUserId(mUid));
          if (localApplicationInfo != null)
          {
            localObject = ((PackageManager)localObject).getApplicationLabel(localApplicationInfo);
            if (localObject != null) {
              paramContext = ((CharSequence)localObject).toString();
            }
            mApplicationName = paramContext;
          }
        }
        catch (PackageManager.NameNotFoundException paramContext)
        {
          return null;
        }
      }
      return mApplicationName;
    }
    
    public boolean isComplete()
    {
      boolean bool;
      if ((mSmsReceiverClass != null) && (mMmsReceiverClass != null) && (mRespondViaMessageClass != null) && (mSendToClass != null)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(" mPackageName: ");
      localStringBuilder.append(mPackageName);
      localStringBuilder.append(" mSmsReceiverClass: ");
      localStringBuilder.append(mSmsReceiverClass);
      localStringBuilder.append(" mMmsReceiverClass: ");
      localStringBuilder.append(mMmsReceiverClass);
      localStringBuilder.append(" mRespondViaMessageClass: ");
      localStringBuilder.append(mRespondViaMessageClass);
      localStringBuilder.append(" mSendToClass: ");
      localStringBuilder.append(mSendToClass);
      localStringBuilder.append(" mSmsAppChangedClass: ");
      localStringBuilder.append(mSmsAppChangedReceiverClass);
      localStringBuilder.append(" mProviderChangedReceiverClass: ");
      localStringBuilder.append(mProviderChangedReceiverClass);
      localStringBuilder.append(" mSimFullReceiverClass: ");
      localStringBuilder.append(mSimFullReceiverClass);
      localStringBuilder.append(" mUid: ");
      localStringBuilder.append(mUid);
      return localStringBuilder.toString();
    }
  }
  
  private static final class SmsPackageMonitor
    extends PackageMonitor
  {
    final Context mContext;
    
    public SmsPackageMonitor(Context paramContext)
    {
      mContext = paramContext;
    }
    
    private void onPackageChanged()
    {
      PackageManager localPackageManager = mContext.getPackageManager();
      Context localContext1 = mContext;
      int i = getSendingUserId();
      Object localObject1 = localContext1;
      if (i != 0) {
        try
        {
          Context localContext2 = mContext;
          String str = mContext.getPackageName();
          localObject1 = new android/os/UserHandle;
          ((UserHandle)localObject1).<init>(i);
          localObject1 = localContext2.createPackageContextAsUser(str, 0, (UserHandle)localObject1);
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {
          localObject2 = localContext1;
        }
      }
      Object localObject2 = SmsApplication.getDefaultSendToApplication((Context)localObject2, true);
      if (localObject2 != null) {
        SmsApplication.configurePreferredActivity(localPackageManager, (ComponentName)localObject2, i);
      }
    }
    
    public void onPackageAppeared(String paramString, int paramInt)
    {
      onPackageChanged();
    }
    
    public void onPackageDisappeared(String paramString, int paramInt)
    {
      onPackageChanged();
    }
    
    public void onPackageModified(String paramString)
    {
      onPackageChanged();
    }
  }
}
