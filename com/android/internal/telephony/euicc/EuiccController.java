package com.android.internal.telephony.euicc;

import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Binder;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.ServiceManager;
import android.provider.Settings.Global;
import android.service.euicc.GetDefaultDownloadableSubscriptionListResult;
import android.service.euicc.GetDownloadableSubscriptionMetadataResult;
import android.service.euicc.GetEuiccProfileInfoListResult;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.UiccAccessRule;
import android.telephony.euicc.DownloadableSubscription;
import android.telephony.euicc.EuiccInfo;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.telephony.SubscriptionController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class EuiccController
  extends IEuiccController.Stub
{
  private static final int ERROR = 2;
  private static final String EXTRA_EMBEDDED_SUBSCRIPTION_DOWNLOADABLE_SUBSCRIPTION = "android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_DOWNLOADABLE_SUBSCRIPTION";
  @VisibleForTesting
  static final String EXTRA_OPERATION = "operation";
  private static final int OK = 0;
  private static final int RESOLVABLE_ERROR = 1;
  private static final String TAG = "EuiccController";
  private static EuiccController sInstance;
  private final AppOpsManager mAppOpsManager;
  private final EuiccConnector mConnector;
  private final Context mContext;
  private final PackageManager mPackageManager;
  private final SubscriptionManager mSubscriptionManager;
  
  private EuiccController(Context paramContext)
  {
    this(paramContext, new EuiccConnector(paramContext));
    ServiceManager.addService("econtroller", this);
  }
  
  @VisibleForTesting
  public EuiccController(Context paramContext, EuiccConnector paramEuiccConnector)
  {
    mContext = paramContext;
    mConnector = paramEuiccConnector;
    mSubscriptionManager = ((SubscriptionManager)paramContext.getSystemService("telephony_subscription_service"));
    mAppOpsManager = ((AppOpsManager)paramContext.getSystemService("appops"));
    mPackageManager = paramContext.getPackageManager();
  }
  
  private static <T> T awaitResult(CountDownLatch paramCountDownLatch, AtomicReference<T> paramAtomicReference)
  {
    try
    {
      paramCountDownLatch.await();
    }
    catch (InterruptedException paramCountDownLatch)
    {
      Thread.currentThread().interrupt();
    }
    return paramAtomicReference.get();
  }
  
  private String blockingGetEidFromEuiccService()
  {
    final CountDownLatch localCountDownLatch = new CountDownLatch(1);
    final AtomicReference localAtomicReference = new AtomicReference();
    mConnector.getEid(new EuiccConnector.GetEidCommandCallback()
    {
      public void onEuiccServiceUnavailable()
      {
        localCountDownLatch.countDown();
      }
      
      public void onGetEidComplete(String paramAnonymousString)
      {
        localAtomicReference.set(paramAnonymousString);
        localCountDownLatch.countDown();
      }
    });
    return (String)awaitResult(localCountDownLatch, localAtomicReference);
  }
  
  private EuiccInfo blockingGetEuiccInfoFromEuiccService()
  {
    final CountDownLatch localCountDownLatch = new CountDownLatch(1);
    final AtomicReference localAtomicReference = new AtomicReference();
    mConnector.getEuiccInfo(new EuiccConnector.GetEuiccInfoCommandCallback()
    {
      public void onEuiccServiceUnavailable()
      {
        localCountDownLatch.countDown();
      }
      
      public void onGetEuiccInfoComplete(EuiccInfo paramAnonymousEuiccInfo)
      {
        localAtomicReference.set(paramAnonymousEuiccInfo);
        localCountDownLatch.countDown();
      }
    });
    return (EuiccInfo)awaitResult(localCountDownLatch, localAtomicReference);
  }
  
  private int blockingGetOtaStatusFromEuiccService()
  {
    final CountDownLatch localCountDownLatch = new CountDownLatch(1);
    final AtomicReference localAtomicReference = new AtomicReference(Integer.valueOf(5));
    mConnector.getOtaStatus(new EuiccConnector.GetOtaStatusCommandCallback()
    {
      public void onEuiccServiceUnavailable()
      {
        localCountDownLatch.countDown();
      }
      
      public void onGetOtaStatusComplete(int paramAnonymousInt)
      {
        localAtomicReference.set(Integer.valueOf(paramAnonymousInt));
        localCountDownLatch.countDown();
      }
    });
    return ((Integer)awaitResult(localCountDownLatch, localAtomicReference)).intValue();
  }
  
  private boolean callerCanReadPhoneStatePrivileged()
  {
    boolean bool;
    if (mContext.checkCallingPermission("android.permission.READ_PRIVILEGED_PHONE_STATE") == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean callerCanWriteEmbeddedSubscriptions()
  {
    boolean bool;
    if (mContext.checkCallingPermission("android.permission.WRITE_EMBEDDED_SUBSCRIPTIONS") == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean callerHasCarrierPrivilegesForActiveSubscription()
  {
    return ((TelephonyManager)mContext.getSystemService("phone")).hasCarrierPrivileges();
  }
  
  private boolean canManageActiveSubscription(String paramString)
  {
    List localList = mSubscriptionManager.getActiveSubscriptionInfoList();
    if (localList == null) {
      return false;
    }
    int i = localList.size();
    for (int j = 0; j < i; j++)
    {
      SubscriptionInfo localSubscriptionInfo = (SubscriptionInfo)localList.get(j);
      if ((localSubscriptionInfo.isEmbedded()) && (mSubscriptionManager.canManageSubscription(localSubscriptionInfo, paramString))) {
        return true;
      }
    }
    return false;
  }
  
  public static EuiccController get()
  {
    if (sInstance == null) {
      try
      {
        if (sInstance != null) {
          break label39;
        }
        IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
        localIllegalStateException.<init>("get() called before init()");
        throw localIllegalStateException;
      }
      finally {}
    }
    label39:
    return sInstance;
  }
  
  private SubscriptionInfo getSubscriptionForSubscriptionId(int paramInt)
  {
    List localList = mSubscriptionManager.getAvailableSubscriptionInfoList();
    int i = localList.size();
    for (int j = 0; j < i; j++)
    {
      SubscriptionInfo localSubscriptionInfo = (SubscriptionInfo)localList.get(j);
      if (paramInt == localSubscriptionInfo.getSubscriptionId()) {
        return localSubscriptionInfo;
      }
    }
    return null;
  }
  
  public static EuiccController init(Context paramContext)
  {
    try
    {
      if (sInstance == null)
      {
        EuiccController localEuiccController = new com/android/internal/telephony/euicc/EuiccController;
        localEuiccController.<init>(paramContext);
        sInstance = localEuiccController;
      }
      else
      {
        paramContext = new java/lang/StringBuilder;
        paramContext.<init>();
        paramContext.append("init() called multiple times! sInstance = ");
        paramContext.append(sInstance);
        Log.wtf("EuiccController", paramContext.toString());
      }
      return sInstance;
    }
    finally {}
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PRIVATE)
  public void addResolutionIntent(Intent paramIntent, String paramString1, String paramString2, boolean paramBoolean, EuiccOperation paramEuiccOperation)
  {
    Intent localIntent = new Intent("android.telephony.euicc.action.RESOLVE_ERROR");
    localIntent.putExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_RESOLUTION_ACTION", paramString1);
    localIntent.putExtra("android.service.euicc.extra.RESOLUTION_CALLING_PACKAGE", paramString2);
    localIntent.putExtra("android.service.euicc.extra.RESOLUTION_CONFIRMATION_CODE_RETRIED", paramBoolean);
    localIntent.putExtra("operation", paramEuiccOperation);
    paramIntent.putExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_RESOLUTION_INTENT", PendingIntent.getActivity(mContext, 0, localIntent, 1073741824));
  }
  
  public GetEuiccProfileInfoListResult blockingGetEuiccProfileInfoList()
  {
    final CountDownLatch localCountDownLatch = new CountDownLatch(1);
    final AtomicReference localAtomicReference = new AtomicReference();
    mConnector.getEuiccProfileInfoList(new EuiccConnector.GetEuiccProfileInfoListCommandCallback()
    {
      public void onEuiccServiceUnavailable()
      {
        localCountDownLatch.countDown();
      }
      
      public void onListComplete(GetEuiccProfileInfoListResult paramAnonymousGetEuiccProfileInfoListResult)
      {
        localAtomicReference.set(paramAnonymousGetEuiccProfileInfoListResult);
        localCountDownLatch.countDown();
      }
    });
    try
    {
      localCountDownLatch.await();
    }
    catch (InterruptedException localInterruptedException)
    {
      Thread.currentThread().interrupt();
    }
    return (GetEuiccProfileInfoListResult)localAtomicReference.get();
  }
  
  public void continueOperation(Intent paramIntent, Bundle paramBundle)
  {
    if (callerCanWriteEmbeddedSubscriptions())
    {
      long l = Binder.clearCallingIdentity();
      try
      {
        EuiccOperation localEuiccOperation = (EuiccOperation)paramIntent.getParcelableExtra("operation");
        if (localEuiccOperation != null)
        {
          localEuiccOperation.continueOperation(paramBundle, (PendingIntent)paramIntent.getParcelableExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_RESOLUTION_CALLBACK_INTENT"));
          return;
        }
        paramIntent = new java/lang/IllegalArgumentException;
        paramIntent.<init>("Invalid resolution intent");
        throw paramIntent;
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
    throw new SecurityException("Must have WRITE_EMBEDDED_SUBSCRIPTIONS to continue operation");
  }
  
  public void deleteSubscription(int paramInt, String paramString, PendingIntent paramPendingIntent)
  {
    boolean bool = callerCanWriteEmbeddedSubscriptions();
    mAppOpsManager.checkPackage(Binder.getCallingUid(), paramString);
    long l = Binder.clearCallingIdentity();
    try
    {
      SubscriptionInfo localSubscriptionInfo = getSubscriptionForSubscriptionId(paramInt);
      if (localSubscriptionInfo == null)
      {
        paramString = new java/lang/StringBuilder;
        paramString.<init>();
        paramString.append("Cannot delete nonexistent subscription: ");
        paramString.append(paramInt);
        Log.e("EuiccController", paramString.toString());
        sendResult(paramPendingIntent, 2, null);
        return;
      }
      if ((!bool) && (!localSubscriptionInfo.canManageSubscription(mContext, paramString)))
      {
        paramString = new java/lang/StringBuilder;
        paramString.<init>();
        paramString.append("No permissions: ");
        paramString.append(paramInt);
        Log.e("EuiccController", paramString.toString());
        sendResult(paramPendingIntent, 2, null);
        return;
      }
      deleteSubscriptionPrivileged(localSubscriptionInfo.getIccId(), paramPendingIntent);
      return;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  void deleteSubscriptionPrivileged(String paramString, final PendingIntent paramPendingIntent)
  {
    mConnector.deleteSubscription(paramString, new EuiccConnector.DeleteCommandCallback()
    {
      public void onDeleteComplete(int paramAnonymousInt)
      {
        Intent localIntent = new Intent();
        if (paramAnonymousInt != 0)
        {
          localIntent.putExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_DETAILED_CODE", paramAnonymousInt);
          sendResult(paramPendingIntent, 2, localIntent);
          return;
        }
        refreshSubscriptionsAndSendResult(paramPendingIntent, 0, localIntent);
      }
      
      public void onEuiccServiceUnavailable()
      {
        sendResult(paramPendingIntent, 2, null);
      }
    });
  }
  
  public void downloadSubscription(DownloadableSubscription paramDownloadableSubscription, boolean paramBoolean, String paramString, PendingIntent paramPendingIntent)
  {
    downloadSubscription(paramDownloadableSubscription, paramBoolean, paramString, false, paramPendingIntent);
  }
  
  void downloadSubscription(DownloadableSubscription paramDownloadableSubscription, boolean paramBoolean1, String paramString, boolean paramBoolean2, PendingIntent paramPendingIntent)
  {
    boolean bool = callerCanWriteEmbeddedSubscriptions();
    mAppOpsManager.checkPackage(Binder.getCallingUid(), paramString);
    long l = Binder.clearCallingIdentity();
    if (bool) {
      try
      {
        downloadSubscriptionPrivileged(l, paramDownloadableSubscription, paramBoolean1, paramBoolean2, paramString, paramPendingIntent);
        Binder.restoreCallingIdentity(l);
        return;
      }
      finally
      {
        break label90;
      }
    }
    EuiccConnector localEuiccConnector = mConnector;
    paramString = new DownloadSubscriptionGetMetadataCommandCallback(l, paramDownloadableSubscription, paramBoolean1, paramString, paramBoolean2, paramPendingIntent);
    try
    {
      localEuiccConnector.getDownloadableSubscriptionMetadata(paramDownloadableSubscription, paramBoolean2, paramString);
      Binder.restoreCallingIdentity(l);
      return;
    }
    finally {}
    label90:
    Binder.restoreCallingIdentity(l);
    throw paramDownloadableSubscription;
  }
  
  void downloadSubscriptionPrivileged(final long paramLong, final DownloadableSubscription paramDownloadableSubscription, final boolean paramBoolean1, boolean paramBoolean2, final String paramString, final PendingIntent paramPendingIntent)
  {
    mConnector.downloadSubscription(paramDownloadableSubscription, paramBoolean1, paramBoolean2, new EuiccConnector.DownloadCommandCallback()
    {
      public void onDownloadComplete(int paramAnonymousInt)
      {
        Intent localIntent = new Intent();
        switch (paramAnonymousInt)
        {
        default: 
          int i = 2;
          localIntent.putExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_DETAILED_CODE", paramAnonymousInt);
          paramAnonymousInt = i;
          break;
        case 0: 
          paramAnonymousInt = 0;
          Settings.Global.putInt(mContext.getContentResolver(), "euicc_provisioned", 1);
          localIntent.putExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_DOWNLOADABLE_SUBSCRIPTION", paramDownloadableSubscription);
          if (!paramBoolean1)
          {
            refreshSubscriptionsAndSendResult(paramPendingIntent, 0, localIntent);
            return;
          }
          break;
        case -1: 
          addResolutionIntent(localIntent, "android.service.euicc.action.RESOLVE_DEACTIVATE_SIM", paramString, false, EuiccOperation.forDownloadDeactivateSim(paramLong, paramDownloadableSubscription, paramBoolean1, paramString));
        case -2: 
          for (;;)
          {
            paramAnonymousInt = 1;
            break;
            boolean bool = false;
            if (!TextUtils.isEmpty(paramDownloadableSubscription.getConfirmationCode())) {
              bool = true;
            }
            addResolutionIntent(localIntent, "android.service.euicc.action.RESOLVE_CONFIRMATION_CODE", paramString, bool, EuiccOperation.forDownloadConfirmationCode(paramLong, paramDownloadableSubscription, paramBoolean1, paramString));
          }
        }
        sendResult(paramPendingIntent, paramAnonymousInt, localIntent);
      }
      
      public void onEuiccServiceUnavailable()
      {
        sendResult(paramPendingIntent, 2, null);
      }
    });
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    mContext.enforceCallingOrSelfPermission("android.permission.DUMP", "Requires DUMP");
    long l = Binder.clearCallingIdentity();
    try
    {
      mConnector.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      return;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  public void eraseSubscriptions(PendingIntent paramPendingIntent)
  {
    if (callerCanWriteEmbeddedSubscriptions())
    {
      long l = Binder.clearCallingIdentity();
      try
      {
        EuiccConnector localEuiccConnector = mConnector;
        EuiccConnector.EraseCommandCallback local7 = new com/android/internal/telephony/euicc/EuiccController$7;
        local7.<init>(this, paramPendingIntent);
        localEuiccConnector.eraseSubscriptions(local7);
        return;
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
    throw new SecurityException("Must have WRITE_EMBEDDED_SUBSCRIPTIONS to erase subscriptions");
  }
  
  public void getDefaultDownloadableSubscriptionList(String paramString, PendingIntent paramPendingIntent)
  {
    getDefaultDownloadableSubscriptionList(false, paramString, paramPendingIntent);
  }
  
  void getDefaultDownloadableSubscriptionList(boolean paramBoolean, String paramString, PendingIntent paramPendingIntent)
  {
    if (callerCanWriteEmbeddedSubscriptions())
    {
      mAppOpsManager.checkPackage(Binder.getCallingUid(), paramString);
      long l = Binder.clearCallingIdentity();
      try
      {
        EuiccConnector localEuiccConnector = mConnector;
        GetDefaultListCommandCallback localGetDefaultListCommandCallback = new com/android/internal/telephony/euicc/EuiccController$GetDefaultListCommandCallback;
        localGetDefaultListCommandCallback.<init>(this, l, paramString, paramPendingIntent);
        localEuiccConnector.getDefaultDownloadableSubscriptionList(paramBoolean, localGetDefaultListCommandCallback);
        return;
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
    throw new SecurityException("Must have WRITE_EMBEDDED_SUBSCRIPTIONS to get default list");
  }
  
  public void getDownloadableSubscriptionMetadata(DownloadableSubscription paramDownloadableSubscription, String paramString, PendingIntent paramPendingIntent)
  {
    getDownloadableSubscriptionMetadata(paramDownloadableSubscription, false, paramString, paramPendingIntent);
  }
  
  void getDownloadableSubscriptionMetadata(DownloadableSubscription paramDownloadableSubscription, boolean paramBoolean, String paramString, PendingIntent paramPendingIntent)
  {
    if (callerCanWriteEmbeddedSubscriptions())
    {
      mAppOpsManager.checkPackage(Binder.getCallingUid(), paramString);
      long l = Binder.clearCallingIdentity();
      try
      {
        EuiccConnector localEuiccConnector = mConnector;
        GetMetadataCommandCallback localGetMetadataCommandCallback = new com/android/internal/telephony/euicc/EuiccController$GetMetadataCommandCallback;
        localGetMetadataCommandCallback.<init>(this, l, paramDownloadableSubscription, paramString, paramPendingIntent);
        localEuiccConnector.getDownloadableSubscriptionMetadata(paramDownloadableSubscription, paramBoolean, localGetMetadataCommandCallback);
        return;
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
    throw new SecurityException("Must have WRITE_EMBEDDED_SUBSCRIPTIONS to get metadata");
  }
  
  public String getEid()
  {
    if ((!callerCanReadPhoneStatePrivileged()) && (!callerHasCarrierPrivilegesForActiveSubscription())) {
      throw new SecurityException("Must have carrier privileges on active subscription to read EID");
    }
    long l = Binder.clearCallingIdentity();
    try
    {
      String str = blockingGetEidFromEuiccService();
      return str;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  public EuiccInfo getEuiccInfo()
  {
    long l = Binder.clearCallingIdentity();
    try
    {
      EuiccInfo localEuiccInfo = blockingGetEuiccInfoFromEuiccService();
      return localEuiccInfo;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  public int getOtaStatus()
  {
    if (callerCanWriteEmbeddedSubscriptions())
    {
      long l = Binder.clearCallingIdentity();
      try
      {
        int i = blockingGetOtaStatusFromEuiccService();
        return i;
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
    throw new SecurityException("Must have WRITE_EMBEDDED_SUBSCRIPTIONS to get OTA status");
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PRIVATE)
  public void refreshSubscriptionsAndSendResult(PendingIntent paramPendingIntent, int paramInt, Intent paramIntent)
  {
    SubscriptionController.getInstance().requestEmbeddedSubscriptionInfoListRefresh(new _..Lambda.EuiccController.aZ8yEHh32lS1TctCOFmVEa57ekc(this, paramPendingIntent, paramInt, paramIntent));
  }
  
  public void retainSubscriptionsForFactoryReset(PendingIntent paramPendingIntent)
  {
    mContext.enforceCallingPermission("android.permission.MASTER_CLEAR", "Must have MASTER_CLEAR to retain subscriptions for factory reset");
    long l = Binder.clearCallingIdentity();
    try
    {
      EuiccConnector localEuiccConnector = mConnector;
      EuiccConnector.RetainSubscriptionsCommandCallback local8 = new com/android/internal/telephony/euicc/EuiccController$8;
      local8.<init>(this, paramPendingIntent);
      localEuiccConnector.retainSubscriptions(local8);
      return;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PRIVATE)
  public void sendOtaStatusChangedBroadcast()
  {
    Intent localIntent = new Intent("android.telephony.euicc.action.OTA_STATUS_CHANGED");
    Object localObject = mConnector;
    localObject = EuiccConnector.findBestComponent(mContext.getPackageManager());
    if (localObject != null) {
      localIntent.setPackage(packageName);
    }
    mContext.sendBroadcast(localIntent, "android.permission.WRITE_EMBEDDED_SUBSCRIPTIONS");
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PRIVATE)
  public void sendResult(PendingIntent paramPendingIntent, int paramInt, Intent paramIntent)
  {
    try
    {
      paramPendingIntent.send(mContext, paramInt, paramIntent);
    }
    catch (PendingIntent.CanceledException paramPendingIntent) {}
  }
  
  public void startOtaUpdatingIfNecessary()
  {
    mConnector.startOtaIfNecessary(new EuiccConnector.OtaStatusChangedCallback()
    {
      public void onEuiccServiceUnavailable() {}
      
      public void onOtaStatusChanged(int paramAnonymousInt)
      {
        sendOtaStatusChangedBroadcast();
      }
    });
  }
  
  public void switchToSubscription(int paramInt, String paramString, PendingIntent paramPendingIntent)
  {
    switchToSubscription(paramInt, false, paramString, paramPendingIntent);
  }
  
  void switchToSubscription(int paramInt, boolean paramBoolean, String paramString, PendingIntent paramPendingIntent)
  {
    boolean bool = callerCanWriteEmbeddedSubscriptions();
    mAppOpsManager.checkPackage(Binder.getCallingUid(), paramString);
    long l = Binder.clearCallingIdentity();
    if (bool) {
      paramBoolean = true;
    }
    Object localObject;
    if (paramInt == -1)
    {
      if (!bool) {
        try
        {
          Log.e("EuiccController", "Not permitted to switch to empty subscription");
          sendResult(paramPendingIntent, 2, null);
          Binder.restoreCallingIdentity(l);
          return;
        }
        finally
        {
          break label284;
        }
      }
      localObject = null;
    }
    try
    {
      localObject = getSubscriptionForSubscriptionId(paramInt);
      if (localObject == null)
      {
        paramString = new java/lang/StringBuilder;
        paramString.<init>();
        paramString.append("Cannot switch to nonexistent subscription: ");
        paramString.append(paramInt);
        Log.e("EuiccController", paramString.toString());
        sendResult(paramPendingIntent, 2, null);
        Binder.restoreCallingIdentity(l);
        return;
      }
      if ((!bool) && (!mSubscriptionManager.canManageSubscription((SubscriptionInfo)localObject, paramString)))
      {
        paramString = new java/lang/StringBuilder;
        paramString.<init>();
        paramString.append("Not permitted to switch to subscription: ");
        paramString.append(paramInt);
        Log.e("EuiccController", paramString.toString());
        sendResult(paramPendingIntent, 2, null);
        Binder.restoreCallingIdentity(l);
        return;
      }
      localObject = ((SubscriptionInfo)localObject).getIccId();
      if ((!bool) && (!canManageActiveSubscription(paramString)))
      {
        localObject = new android/content/Intent;
        ((Intent)localObject).<init>();
        addResolutionIntent((Intent)localObject, "android.service.euicc.action.RESOLVE_NO_PRIVILEGES", paramString, false, EuiccOperation.forSwitchNoPrivileges(l, paramInt, paramString));
        sendResult(paramPendingIntent, 1, (Intent)localObject);
        Binder.restoreCallingIdentity(l);
        return;
      }
      try
      {
        switchToSubscriptionPrivileged(l, paramInt, (String)localObject, paramBoolean, paramString, paramPendingIntent);
        Binder.restoreCallingIdentity(l);
        return;
      }
      finally {}
      Binder.restoreCallingIdentity(l);
    }
    finally {}
    label284:
    throw paramString;
  }
  
  void switchToSubscriptionPrivileged(final long paramLong, int paramInt, String paramString1, boolean paramBoolean, final String paramString2, final PendingIntent paramPendingIntent)
  {
    mConnector.switchToSubscription(paramString1, paramBoolean, new EuiccConnector.SwitchCommandCallback()
    {
      public void onEuiccServiceUnavailable()
      {
        sendResult(val$callbackIntent, 2, null);
      }
      
      public void onSwitchComplete(int paramAnonymousInt)
      {
        Intent localIntent = new Intent();
        switch (paramAnonymousInt)
        {
        default: 
          int i = 2;
          localIntent.putExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_DETAILED_CODE", paramAnonymousInt);
          paramAnonymousInt = i;
          break;
        case 0: 
          paramAnonymousInt = 0;
          break;
        case -1: 
          addResolutionIntent(localIntent, "android.service.euicc.action.RESOLVE_DEACTIVATE_SIM", paramString2, false, EuiccOperation.forSwitchDeactivateSim(paramLong, paramPendingIntent, paramString2));
          paramAnonymousInt = 1;
        }
        sendResult(val$callbackIntent, paramAnonymousInt, localIntent);
      }
    });
  }
  
  void switchToSubscriptionPrivileged(long paramLong, int paramInt, boolean paramBoolean, String paramString, PendingIntent paramPendingIntent)
  {
    String str = null;
    SubscriptionInfo localSubscriptionInfo = getSubscriptionForSubscriptionId(paramInt);
    if (localSubscriptionInfo != null) {
      str = localSubscriptionInfo.getIccId();
    }
    switchToSubscriptionPrivileged(paramLong, paramInt, str, paramBoolean, paramString, paramPendingIntent);
  }
  
  public void updateSubscriptionNickname(int paramInt, String paramString, PendingIntent paramPendingIntent)
  {
    if (callerCanWriteEmbeddedSubscriptions())
    {
      long l = Binder.clearCallingIdentity();
      try
      {
        Object localObject = getSubscriptionForSubscriptionId(paramInt);
        if (localObject == null)
        {
          paramString = new java/lang/StringBuilder;
          paramString.<init>();
          paramString.append("Cannot update nickname to nonexistent subscription: ");
          paramString.append(paramInt);
          Log.e("EuiccController", paramString.toString());
          sendResult(paramPendingIntent, 2, null);
          return;
        }
        EuiccConnector localEuiccConnector = mConnector;
        String str = ((SubscriptionInfo)localObject).getIccId();
        localObject = new com/android/internal/telephony/euicc/EuiccController$6;
        ((6)localObject).<init>(this, paramPendingIntent);
        localEuiccConnector.updateSubscriptionNickname(str, paramString, (EuiccConnector.UpdateNicknameCommandCallback)localObject);
        return;
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
    throw new SecurityException("Must have WRITE_EMBEDDED_SUBSCRIPTIONS to update nickname");
  }
  
  class DownloadSubscriptionGetMetadataCommandCallback
    extends EuiccController.GetMetadataCommandCallback
  {
    private final boolean mForceDeactivateSim;
    private final boolean mSwitchAfterDownload;
    
    DownloadSubscriptionGetMetadataCommandCallback(long paramLong, DownloadableSubscription paramDownloadableSubscription, boolean paramBoolean1, String paramString, boolean paramBoolean2, PendingIntent paramPendingIntent)
    {
      super(paramLong, paramDownloadableSubscription, paramString, paramPendingIntent);
      mSwitchAfterDownload = paramBoolean1;
      mForceDeactivateSim = paramBoolean2;
    }
    
    protected EuiccOperation getOperationForDeactivateSim()
    {
      return EuiccOperation.forDownloadDeactivateSim(mCallingToken, mSubscription, mSwitchAfterDownload, mCallingPackage);
    }
    
    public void onGetMetadataComplete(GetDownloadableSubscriptionMetadataResult paramGetDownloadableSubscriptionMetadataResult)
    {
      if (paramGetDownloadableSubscriptionMetadataResult.getResult() == -1)
      {
        paramGetDownloadableSubscriptionMetadataResult = new Intent();
        addResolutionIntent(paramGetDownloadableSubscriptionMetadataResult, "android.service.euicc.action.RESOLVE_NO_PRIVILEGES", mCallingPackage, false, EuiccOperation.forDownloadNoPrivileges(mCallingToken, mSubscription, mSwitchAfterDownload, mCallingPackage));
        sendResult(mCallbackIntent, 1, paramGetDownloadableSubscriptionMetadataResult);
        return;
      }
      if (paramGetDownloadableSubscriptionMetadataResult.getResult() != 0)
      {
        super.onGetMetadataComplete(paramGetDownloadableSubscriptionMetadataResult);
        return;
      }
      DownloadableSubscription localDownloadableSubscription = paramGetDownloadableSubscriptionMetadataResult.getDownloadableSubscription();
      paramGetDownloadableSubscriptionMetadataResult = null;
      Object localObject = localDownloadableSubscription.getAccessRules();
      if (localObject != null) {
        paramGetDownloadableSubscriptionMetadataResult = (UiccAccessRule[])((List)localObject).toArray(new UiccAccessRule[((List)localObject).size()]);
      }
      if (paramGetDownloadableSubscriptionMetadataResult == null)
      {
        Log.e("EuiccController", "No access rules but caller is unprivileged");
        sendResult(mCallbackIntent, 2, null);
        return;
      }
      try
      {
        localObject = mPackageManager.getPackageInfo(mCallingPackage, 64);
        for (int i = 0; i < paramGetDownloadableSubscriptionMetadataResult.length; i++) {
          if (paramGetDownloadableSubscriptionMetadataResult[i].getCarrierPrivilegeStatus((PackageInfo)localObject) == 1)
          {
            if (EuiccController.this.canManageActiveSubscription(mCallingPackage))
            {
              downloadSubscriptionPrivileged(mCallingToken, localDownloadableSubscription, mSwitchAfterDownload, mForceDeactivateSim, mCallingPackage, mCallbackIntent);
              return;
            }
            paramGetDownloadableSubscriptionMetadataResult = new Intent();
            addResolutionIntent(paramGetDownloadableSubscriptionMetadataResult, "android.service.euicc.action.RESOLVE_NO_PRIVILEGES", mCallingPackage, false, EuiccOperation.forDownloadNoPrivileges(mCallingToken, localDownloadableSubscription, mSwitchAfterDownload, mCallingPackage));
            sendResult(mCallbackIntent, 1, paramGetDownloadableSubscriptionMetadataResult);
            return;
          }
        }
        Log.e("EuiccController", "Caller is not permitted to download this profile");
        sendResult(mCallbackIntent, 2, null);
        return;
      }
      catch (PackageManager.NameNotFoundException paramGetDownloadableSubscriptionMetadataResult)
      {
        Log.e("EuiccController", "Calling package valid but gone");
        sendResult(mCallbackIntent, 2, null);
      }
    }
  }
  
  class GetDefaultListCommandCallback
    implements EuiccConnector.GetDefaultListCommandCallback
  {
    final PendingIntent mCallbackIntent;
    final String mCallingPackage;
    final long mCallingToken;
    
    GetDefaultListCommandCallback(long paramLong, String paramString, PendingIntent paramPendingIntent)
    {
      mCallingToken = paramLong;
      mCallingPackage = paramString;
      mCallbackIntent = paramPendingIntent;
    }
    
    public void onEuiccServiceUnavailable()
    {
      sendResult(mCallbackIntent, 2, null);
    }
    
    public void onGetDefaultListComplete(GetDefaultDownloadableSubscriptionListResult paramGetDefaultDownloadableSubscriptionListResult)
    {
      Intent localIntent = new Intent();
      int i;
      switch (paramGetDefaultDownloadableSubscriptionListResult.getResult())
      {
      default: 
        i = 2;
        localIntent.putExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_DETAILED_CODE", paramGetDefaultDownloadableSubscriptionListResult.getResult());
        break;
      case 0: 
        int j = 0;
        paramGetDefaultDownloadableSubscriptionListResult = paramGetDefaultDownloadableSubscriptionListResult.getDownloadableSubscriptions();
        i = j;
        if (paramGetDefaultDownloadableSubscriptionListResult != null)
        {
          i = j;
          if (paramGetDefaultDownloadableSubscriptionListResult.size() > 0)
          {
            localIntent.putExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_DOWNLOADABLE_SUBSCRIPTIONS", (Parcelable[])paramGetDefaultDownloadableSubscriptionListResult.toArray(new DownloadableSubscription[paramGetDefaultDownloadableSubscriptionListResult.size()]));
            i = j;
          }
        }
        break;
      case -1: 
        addResolutionIntent(localIntent, "android.service.euicc.action.RESOLVE_DEACTIVATE_SIM", mCallingPackage, false, EuiccOperation.forGetDefaultListDeactivateSim(mCallingToken, mCallingPackage));
        i = 1;
      }
      sendResult(mCallbackIntent, i, localIntent);
    }
  }
  
  class GetMetadataCommandCallback
    implements EuiccConnector.GetMetadataCommandCallback
  {
    protected final PendingIntent mCallbackIntent;
    protected final String mCallingPackage;
    protected final long mCallingToken;
    protected final DownloadableSubscription mSubscription;
    
    GetMetadataCommandCallback(long paramLong, DownloadableSubscription paramDownloadableSubscription, String paramString, PendingIntent paramPendingIntent)
    {
      mCallingToken = paramLong;
      mSubscription = paramDownloadableSubscription;
      mCallingPackage = paramString;
      mCallbackIntent = paramPendingIntent;
    }
    
    protected EuiccOperation getOperationForDeactivateSim()
    {
      return EuiccOperation.forGetMetadataDeactivateSim(mCallingToken, mSubscription, mCallingPackage);
    }
    
    public void onEuiccServiceUnavailable()
    {
      sendResult(mCallbackIntent, 2, null);
    }
    
    public void onGetMetadataComplete(GetDownloadableSubscriptionMetadataResult paramGetDownloadableSubscriptionMetadataResult)
    {
      Intent localIntent = new Intent();
      int i;
      switch (paramGetDownloadableSubscriptionMetadataResult.getResult())
      {
      default: 
        i = 2;
        localIntent.putExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_DETAILED_CODE", paramGetDownloadableSubscriptionMetadataResult.getResult());
        break;
      case 0: 
        i = 0;
        localIntent.putExtra("android.telephony.euicc.extra.EMBEDDED_SUBSCRIPTION_DOWNLOADABLE_SUBSCRIPTION", paramGetDownloadableSubscriptionMetadataResult.getDownloadableSubscription());
        break;
      case -1: 
        addResolutionIntent(localIntent, "android.service.euicc.action.RESOLVE_DEACTIVATE_SIM", mCallingPackage, false, getOperationForDeactivateSim());
        i = 1;
      }
      sendResult(mCallbackIntent, i, localIntent);
    }
  }
}
