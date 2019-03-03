package com.android.internal.telephony.euicc;

import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ComponentInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.preference.PreferenceManager;
import android.provider.Settings.Global;
import android.service.euicc.EuiccProfileInfo;
import android.telephony.euicc.EuiccNotification;
import android.telephony.euicc.EuiccRulesAuthTable;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.telephony.SubscriptionController;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccSlot;
import com.android.internal.telephony.uicc.euicc.EuiccCard;
import com.android.internal.telephony.uicc.euicc.EuiccCardErrorException;
import com.android.internal.telephony.uicc.euicc.async.AsyncResultCallback;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class EuiccCardController
  extends IEuiccCardController.Stub
{
  private static final String KEY_LAST_BOOT_COUNT = "last_boot_count";
  private static final String TAG = "EuiccCardController";
  private static EuiccCardController sInstance;
  private AppOpsManager mAppOps;
  private ComponentInfo mBestComponent;
  private String mCallingPackage;
  private final Context mContext;
  private EuiccController mEuiccController;
  private Handler mEuiccMainThreadHandler;
  private SimSlotStatusChangedBroadcastReceiver mSimSlotStatusChangeReceiver;
  private UiccController mUiccController;
  
  private EuiccCardController(Context paramContext)
  {
    this(paramContext, new Handler(), EuiccController.get(), UiccController.getInstance());
    ServiceManager.addService("euicc_card_controller", this);
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PRIVATE)
  public EuiccCardController(Context paramContext, Handler paramHandler, EuiccController paramEuiccController, UiccController paramUiccController)
  {
    mContext = paramContext;
    mAppOps = ((AppOpsManager)paramContext.getSystemService("appops"));
    mEuiccMainThreadHandler = paramHandler;
    mUiccController = paramUiccController;
    mEuiccController = paramEuiccController;
    if (isBootUp(mContext))
    {
      mSimSlotStatusChangeReceiver = new SimSlotStatusChangedBroadcastReceiver(null);
      mContext.registerReceiver(mSimSlotStatusChangeReceiver, new IntentFilter("android.telephony.action.SIM_SLOT_STATUS_CHANGED"));
    }
  }
  
  private void checkCallingPackage(String paramString)
  {
    mAppOps.checkPackage(Binder.getCallingUid(), paramString);
    mCallingPackage = paramString;
    mBestComponent = EuiccConnector.findBestComponent(mContext.getPackageManager());
    if ((mBestComponent != null) && (TextUtils.equals(mCallingPackage, mBestComponent.packageName))) {
      return;
    }
    throw new SecurityException("The calling package can only be LPA.");
  }
  
  public static EuiccCardController get()
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
  
  private EuiccCard getEuiccCard(String paramString)
  {
    Object localObject = UiccController.getInstance();
    int i = ((UiccController)localObject).getUiccSlotForCardId(paramString);
    if ((i != -1) && (((UiccController)localObject).getUiccSlot(i).isEuicc())) {
      return (EuiccCard)((UiccController)localObject).getUiccCardForSlot(i);
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("EuiccCard is null. CardId : ");
    ((StringBuilder)localObject).append(paramString);
    loge(((StringBuilder)localObject).toString());
    return null;
  }
  
  private int getResultCode(Throwable paramThrowable)
  {
    if ((paramThrowable instanceof EuiccCardErrorException)) {
      return ((EuiccCardErrorException)paramThrowable).getErrorCode();
    }
    return -1;
  }
  
  public static EuiccCardController init(Context paramContext)
  {
    try
    {
      if (sInstance == null)
      {
        EuiccCardController localEuiccCardController = new com/android/internal/telephony/euicc/EuiccCardController;
        localEuiccCardController.<init>(paramContext);
        sInstance = localEuiccCardController;
      }
      else
      {
        paramContext = new java/lang/StringBuilder;
        paramContext.<init>();
        paramContext.append("init() called multiple times! sInstance = ");
        paramContext.append(sInstance);
        Log.wtf("EuiccCardController", paramContext.toString());
      }
      return sInstance;
    }
    finally {}
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PRIVATE)
  public static boolean isBootUp(Context paramContext)
  {
    int i = Settings.Global.getInt(paramContext.getContentResolver(), "boot_count", -1);
    paramContext = PreferenceManager.getDefaultSharedPreferences(paramContext);
    int j = paramContext.getInt("last_boot_count", -1);
    if ((i != -1) && (j != -1) && (i == j)) {
      return false;
    }
    paramContext.edit().putInt("last_boot_count", i).apply();
    return true;
  }
  
  private static void loge(String paramString)
  {
    Log.e("EuiccCardController", paramString);
  }
  
  private static void loge(String paramString, Throwable paramThrowable)
  {
    Log.e("EuiccCardController", paramString, paramThrowable);
  }
  
  public void authenticateServer(String paramString1, String paramString2, String paramString3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4, final IAuthenticateServerCallback paramIAuthenticateServerCallback)
  {
    checkCallingPackage(paramString1);
    paramString1 = getEuiccCard(paramString2);
    if (paramString1 == null)
    {
      try
      {
        paramIAuthenticateServerCallback.onComplete(-2, null);
      }
      catch (RemoteException paramString1)
      {
        loge("authenticateServer callback failure.", paramString1);
      }
      return;
    }
    paramString1.authenticateServer(paramString3, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3, paramArrayOfByte4, new AsyncResultCallback()
    {
      public void onException(Throwable paramAnonymousThrowable)
      {
        try
        {
          EuiccCardController.loge("authenticateServer callback onException: ", paramAnonymousThrowable);
          paramIAuthenticateServerCallback.onComplete(EuiccCardController.this.getResultCode(paramAnonymousThrowable), null);
        }
        catch (RemoteException paramAnonymousThrowable)
        {
          EuiccCardController.loge("authenticateServer callback failure.", paramAnonymousThrowable);
        }
      }
      
      public void onResult(byte[] paramAnonymousArrayOfByte)
      {
        try
        {
          paramIAuthenticateServerCallback.onComplete(0, paramAnonymousArrayOfByte);
        }
        catch (RemoteException paramAnonymousArrayOfByte)
        {
          EuiccCardController.loge("authenticateServer callback failure.", paramAnonymousArrayOfByte);
        }
      }
    }, mEuiccMainThreadHandler);
  }
  
  public void cancelSession(String paramString1, String paramString2, byte[] paramArrayOfByte, int paramInt, final ICancelSessionCallback paramICancelSessionCallback)
  {
    checkCallingPackage(paramString1);
    paramString1 = getEuiccCard(paramString2);
    if (paramString1 == null)
    {
      try
      {
        paramICancelSessionCallback.onComplete(-2, null);
      }
      catch (RemoteException paramString1)
      {
        loge("cancelSession callback failure.", paramString1);
      }
      return;
    }
    paramString1.cancelSession(paramArrayOfByte, paramInt, new AsyncResultCallback()
    {
      public void onException(Throwable paramAnonymousThrowable)
      {
        try
        {
          EuiccCardController.loge("cancelSession callback onException: ", paramAnonymousThrowable);
          paramICancelSessionCallback.onComplete(EuiccCardController.this.getResultCode(paramAnonymousThrowable), null);
        }
        catch (RemoteException paramAnonymousThrowable)
        {
          EuiccCardController.loge("cancelSession callback failure.", paramAnonymousThrowable);
        }
      }
      
      public void onResult(byte[] paramAnonymousArrayOfByte)
      {
        try
        {
          paramICancelSessionCallback.onComplete(0, paramAnonymousArrayOfByte);
        }
        catch (RemoteException paramAnonymousArrayOfByte)
        {
          EuiccCardController.loge("cancelSession callback failure.", paramAnonymousArrayOfByte);
        }
      }
    }, mEuiccMainThreadHandler);
  }
  
  public void deleteProfile(String paramString1, String paramString2, String paramString3, final IDeleteProfileCallback paramIDeleteProfileCallback)
  {
    checkCallingPackage(paramString1);
    paramString1 = getEuiccCard(paramString2);
    if (paramString1 == null)
    {
      try
      {
        paramIDeleteProfileCallback.onComplete(-2);
      }
      catch (RemoteException paramString1)
      {
        loge("deleteProfile callback failure.", paramString1);
      }
      return;
    }
    paramString1.deleteProfile(paramString3, new AsyncResultCallback()
    {
      public void onException(Throwable paramAnonymousThrowable)
      {
        try
        {
          EuiccCardController.loge("deleteProfile callback onException: ", paramAnonymousThrowable);
          paramIDeleteProfileCallback.onComplete(EuiccCardController.this.getResultCode(paramAnonymousThrowable));
        }
        catch (RemoteException paramAnonymousThrowable)
        {
          EuiccCardController.loge("deleteProfile callback failure.", paramAnonymousThrowable);
        }
      }
      
      public void onResult(Void paramAnonymousVoid)
      {
        Log.i("EuiccCardController", "Request subscription info list refresh after delete.");
        SubscriptionController.getInstance().requestEmbeddedSubscriptionInfoListRefresh();
        try
        {
          paramIDeleteProfileCallback.onComplete(0);
        }
        catch (RemoteException paramAnonymousVoid)
        {
          EuiccCardController.loge("deleteProfile callback failure.", paramAnonymousVoid);
        }
      }
    }, mEuiccMainThreadHandler);
  }
  
  public void disableProfile(String paramString1, String paramString2, String paramString3, boolean paramBoolean, final IDisableProfileCallback paramIDisableProfileCallback)
  {
    checkCallingPackage(paramString1);
    paramString1 = getEuiccCard(paramString2);
    if (paramString1 == null)
    {
      try
      {
        paramIDisableProfileCallback.onComplete(-2);
      }
      catch (RemoteException paramString1)
      {
        loge("disableProfile callback failure.", paramString1);
      }
      return;
    }
    paramString1.disableProfile(paramString3, paramBoolean, new AsyncResultCallback()
    {
      public void onException(Throwable paramAnonymousThrowable)
      {
        try
        {
          EuiccCardController.loge("disableProfile callback onException: ", paramAnonymousThrowable);
          paramIDisableProfileCallback.onComplete(EuiccCardController.this.getResultCode(paramAnonymousThrowable));
        }
        catch (RemoteException paramAnonymousThrowable)
        {
          EuiccCardController.loge("disableProfile callback failure.", paramAnonymousThrowable);
        }
      }
      
      public void onResult(Void paramAnonymousVoid)
      {
        try
        {
          paramIDisableProfileCallback.onComplete(0);
        }
        catch (RemoteException paramAnonymousVoid)
        {
          EuiccCardController.loge("disableProfile callback failure.", paramAnonymousVoid);
        }
      }
    }, mEuiccMainThreadHandler);
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    mContext.enforceCallingOrSelfPermission("android.permission.DUMP", "Requires DUMP");
    long l = Binder.clearCallingIdentity();
    super.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("mCallingPackage=");
    paramFileDescriptor.append(mCallingPackage);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("mBestComponent=");
    paramFileDescriptor.append(mBestComponent);
    paramPrintWriter.println(paramFileDescriptor.toString());
    Binder.restoreCallingIdentity(l);
  }
  
  public void getAllProfiles(String paramString1, String paramString2, final IGetAllProfilesCallback paramIGetAllProfilesCallback)
  {
    checkCallingPackage(paramString1);
    paramString1 = getEuiccCard(paramString2);
    if (paramString1 == null)
    {
      try
      {
        paramIGetAllProfilesCallback.onComplete(-2, null);
      }
      catch (RemoteException paramString1)
      {
        loge("getAllProfiles callback failure.", paramString1);
      }
      return;
    }
    paramString1.getAllProfiles(new AsyncResultCallback()
    {
      public void onException(Throwable paramAnonymousThrowable)
      {
        try
        {
          EuiccCardController.loge("getAllProfiles callback onException: ", paramAnonymousThrowable);
          paramIGetAllProfilesCallback.onComplete(EuiccCardController.this.getResultCode(paramAnonymousThrowable), null);
        }
        catch (RemoteException paramAnonymousThrowable)
        {
          EuiccCardController.loge("getAllProfiles callback failure.", paramAnonymousThrowable);
        }
      }
      
      public void onResult(EuiccProfileInfo[] paramAnonymousArrayOfEuiccProfileInfo)
      {
        try
        {
          paramIGetAllProfilesCallback.onComplete(0, paramAnonymousArrayOfEuiccProfileInfo);
        }
        catch (RemoteException paramAnonymousArrayOfEuiccProfileInfo)
        {
          EuiccCardController.loge("getAllProfiles callback failure.", paramAnonymousArrayOfEuiccProfileInfo);
        }
      }
    }, mEuiccMainThreadHandler);
  }
  
  public void getDefaultSmdpAddress(String paramString1, String paramString2, final IGetDefaultSmdpAddressCallback paramIGetDefaultSmdpAddressCallback)
  {
    checkCallingPackage(paramString1);
    paramString1 = getEuiccCard(paramString2);
    if (paramString1 == null)
    {
      try
      {
        paramIGetDefaultSmdpAddressCallback.onComplete(-2, null);
      }
      catch (RemoteException paramString1)
      {
        loge("getDefaultSmdpAddress callback failure.", paramString1);
      }
      return;
    }
    paramString1.getDefaultSmdpAddress(new AsyncResultCallback()
    {
      public void onException(Throwable paramAnonymousThrowable)
      {
        try
        {
          EuiccCardController.loge("getDefaultSmdpAddress callback onException: ", paramAnonymousThrowable);
          paramIGetDefaultSmdpAddressCallback.onComplete(EuiccCardController.this.getResultCode(paramAnonymousThrowable), null);
        }
        catch (RemoteException paramAnonymousThrowable)
        {
          EuiccCardController.loge("getDefaultSmdpAddress callback failure.", paramAnonymousThrowable);
        }
      }
      
      public void onResult(String paramAnonymousString)
      {
        try
        {
          paramIGetDefaultSmdpAddressCallback.onComplete(0, paramAnonymousString);
        }
        catch (RemoteException paramAnonymousString)
        {
          EuiccCardController.loge("getDefaultSmdpAddress callback failure.", paramAnonymousString);
        }
      }
    }, mEuiccMainThreadHandler);
  }
  
  public void getEuiccChallenge(String paramString1, String paramString2, final IGetEuiccChallengeCallback paramIGetEuiccChallengeCallback)
  {
    checkCallingPackage(paramString1);
    paramString1 = getEuiccCard(paramString2);
    if (paramString1 == null)
    {
      try
      {
        paramIGetEuiccChallengeCallback.onComplete(-2, null);
      }
      catch (RemoteException paramString1)
      {
        loge("getEuiccChallenge callback failure.", paramString1);
      }
      return;
    }
    paramString1.getEuiccChallenge(new AsyncResultCallback()
    {
      public void onException(Throwable paramAnonymousThrowable)
      {
        try
        {
          EuiccCardController.loge("getEuiccChallenge callback onException: ", paramAnonymousThrowable);
          paramIGetEuiccChallengeCallback.onComplete(EuiccCardController.this.getResultCode(paramAnonymousThrowable), null);
        }
        catch (RemoteException paramAnonymousThrowable)
        {
          EuiccCardController.loge("getEuiccChallenge callback failure.", paramAnonymousThrowable);
        }
      }
      
      public void onResult(byte[] paramAnonymousArrayOfByte)
      {
        try
        {
          paramIGetEuiccChallengeCallback.onComplete(0, paramAnonymousArrayOfByte);
        }
        catch (RemoteException paramAnonymousArrayOfByte)
        {
          EuiccCardController.loge("getEuiccChallenge callback failure.", paramAnonymousArrayOfByte);
        }
      }
    }, mEuiccMainThreadHandler);
  }
  
  public void getEuiccInfo1(String paramString1, String paramString2, final IGetEuiccInfo1Callback paramIGetEuiccInfo1Callback)
  {
    checkCallingPackage(paramString1);
    paramString1 = getEuiccCard(paramString2);
    if (paramString1 == null)
    {
      try
      {
        paramIGetEuiccInfo1Callback.onComplete(-2, null);
      }
      catch (RemoteException paramString1)
      {
        loge("getEuiccInfo1 callback failure.", paramString1);
      }
      return;
    }
    paramString1.getEuiccInfo1(new AsyncResultCallback()
    {
      public void onException(Throwable paramAnonymousThrowable)
      {
        try
        {
          EuiccCardController.loge("getEuiccInfo1 callback onException: ", paramAnonymousThrowable);
          paramIGetEuiccInfo1Callback.onComplete(EuiccCardController.this.getResultCode(paramAnonymousThrowable), null);
        }
        catch (RemoteException paramAnonymousThrowable)
        {
          EuiccCardController.loge("getEuiccInfo1 callback failure.", paramAnonymousThrowable);
        }
      }
      
      public void onResult(byte[] paramAnonymousArrayOfByte)
      {
        try
        {
          paramIGetEuiccInfo1Callback.onComplete(0, paramAnonymousArrayOfByte);
        }
        catch (RemoteException paramAnonymousArrayOfByte)
        {
          EuiccCardController.loge("getEuiccInfo1 callback failure.", paramAnonymousArrayOfByte);
        }
      }
    }, mEuiccMainThreadHandler);
  }
  
  public void getEuiccInfo2(String paramString1, String paramString2, final IGetEuiccInfo2Callback paramIGetEuiccInfo2Callback)
  {
    checkCallingPackage(paramString1);
    paramString1 = getEuiccCard(paramString2);
    if (paramString1 == null)
    {
      try
      {
        paramIGetEuiccInfo2Callback.onComplete(-2, null);
      }
      catch (RemoteException paramString1)
      {
        loge("getEuiccInfo2 callback failure.", paramString1);
      }
      return;
    }
    paramString1.getEuiccInfo2(new AsyncResultCallback()
    {
      public void onException(Throwable paramAnonymousThrowable)
      {
        try
        {
          EuiccCardController.loge("getEuiccInfo2 callback onException: ", paramAnonymousThrowable);
          paramIGetEuiccInfo2Callback.onComplete(EuiccCardController.this.getResultCode(paramAnonymousThrowable), null);
        }
        catch (RemoteException paramAnonymousThrowable)
        {
          EuiccCardController.loge("getEuiccInfo2 callback failure.", paramAnonymousThrowable);
        }
      }
      
      public void onResult(byte[] paramAnonymousArrayOfByte)
      {
        try
        {
          paramIGetEuiccInfo2Callback.onComplete(0, paramAnonymousArrayOfByte);
        }
        catch (RemoteException paramAnonymousArrayOfByte)
        {
          EuiccCardController.loge("getEuiccInfo2 callback failure.", paramAnonymousArrayOfByte);
        }
      }
    }, mEuiccMainThreadHandler);
  }
  
  public void getProfile(String paramString1, String paramString2, String paramString3, final IGetProfileCallback paramIGetProfileCallback)
  {
    checkCallingPackage(paramString1);
    paramString1 = getEuiccCard(paramString2);
    if (paramString1 == null)
    {
      try
      {
        paramIGetProfileCallback.onComplete(-2, null);
      }
      catch (RemoteException paramString1)
      {
        loge("getProfile callback failure.", paramString1);
      }
      return;
    }
    paramString1.getProfile(paramString3, new AsyncResultCallback()
    {
      public void onException(Throwable paramAnonymousThrowable)
      {
        try
        {
          EuiccCardController.loge("getProfile callback onException: ", paramAnonymousThrowable);
          paramIGetProfileCallback.onComplete(EuiccCardController.this.getResultCode(paramAnonymousThrowable), null);
        }
        catch (RemoteException paramAnonymousThrowable)
        {
          EuiccCardController.loge("getProfile callback failure.", paramAnonymousThrowable);
        }
      }
      
      public void onResult(EuiccProfileInfo paramAnonymousEuiccProfileInfo)
      {
        try
        {
          paramIGetProfileCallback.onComplete(0, paramAnonymousEuiccProfileInfo);
        }
        catch (RemoteException paramAnonymousEuiccProfileInfo)
        {
          EuiccCardController.loge("getProfile callback failure.", paramAnonymousEuiccProfileInfo);
        }
      }
    }, mEuiccMainThreadHandler);
  }
  
  public void getRulesAuthTable(String paramString1, String paramString2, final IGetRulesAuthTableCallback paramIGetRulesAuthTableCallback)
  {
    checkCallingPackage(paramString1);
    paramString1 = getEuiccCard(paramString2);
    if (paramString1 == null)
    {
      try
      {
        paramIGetRulesAuthTableCallback.onComplete(-2, null);
      }
      catch (RemoteException paramString1)
      {
        loge("getRulesAuthTable callback failure.", paramString1);
      }
      return;
    }
    paramString1.getRulesAuthTable(new AsyncResultCallback()
    {
      public void onException(Throwable paramAnonymousThrowable)
      {
        try
        {
          EuiccCardController.loge("getRulesAuthTable callback onException: ", paramAnonymousThrowable);
          paramIGetRulesAuthTableCallback.onComplete(EuiccCardController.this.getResultCode(paramAnonymousThrowable), null);
        }
        catch (RemoteException paramAnonymousThrowable)
        {
          EuiccCardController.loge("getRulesAuthTable callback failure.", paramAnonymousThrowable);
        }
      }
      
      public void onResult(EuiccRulesAuthTable paramAnonymousEuiccRulesAuthTable)
      {
        try
        {
          paramIGetRulesAuthTableCallback.onComplete(0, paramAnonymousEuiccRulesAuthTable);
        }
        catch (RemoteException paramAnonymousEuiccRulesAuthTable)
        {
          EuiccCardController.loge("getRulesAuthTable callback failure.", paramAnonymousEuiccRulesAuthTable);
        }
      }
    }, mEuiccMainThreadHandler);
  }
  
  public void getSmdsAddress(String paramString1, String paramString2, final IGetSmdsAddressCallback paramIGetSmdsAddressCallback)
  {
    checkCallingPackage(paramString1);
    paramString1 = getEuiccCard(paramString2);
    if (paramString1 == null)
    {
      try
      {
        paramIGetSmdsAddressCallback.onComplete(-2, null);
      }
      catch (RemoteException paramString1)
      {
        loge("getSmdsAddress callback failure.", paramString1);
      }
      return;
    }
    paramString1.getSmdsAddress(new AsyncResultCallback()
    {
      public void onException(Throwable paramAnonymousThrowable)
      {
        try
        {
          EuiccCardController.loge("getSmdsAddress callback onException: ", paramAnonymousThrowable);
          paramIGetSmdsAddressCallback.onComplete(EuiccCardController.this.getResultCode(paramAnonymousThrowable), null);
        }
        catch (RemoteException paramAnonymousThrowable)
        {
          EuiccCardController.loge("getSmdsAddress callback failure.", paramAnonymousThrowable);
        }
      }
      
      public void onResult(String paramAnonymousString)
      {
        try
        {
          paramIGetSmdsAddressCallback.onComplete(0, paramAnonymousString);
        }
        catch (RemoteException paramAnonymousString)
        {
          EuiccCardController.loge("getSmdsAddress callback failure.", paramAnonymousString);
        }
      }
    }, mEuiccMainThreadHandler);
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PRIVATE)
  public boolean isEmbeddedSlotActivated()
  {
    UiccSlot[] arrayOfUiccSlot = mUiccController.getUiccSlots();
    if (arrayOfUiccSlot == null) {
      return false;
    }
    for (int i = 0; i < arrayOfUiccSlot.length; i++)
    {
      UiccSlot localUiccSlot = arrayOfUiccSlot[i];
      if ((localUiccSlot.isEuicc()) && (localUiccSlot.isActive())) {
        return true;
      }
    }
    return false;
  }
  
  public void listNotifications(String paramString1, String paramString2, int paramInt, final IListNotificationsCallback paramIListNotificationsCallback)
  {
    checkCallingPackage(paramString1);
    paramString1 = getEuiccCard(paramString2);
    if (paramString1 == null)
    {
      try
      {
        paramIListNotificationsCallback.onComplete(-2, null);
      }
      catch (RemoteException paramString1)
      {
        loge("listNotifications callback failure.", paramString1);
      }
      return;
    }
    paramString1.listNotifications(paramInt, new AsyncResultCallback()
    {
      public void onException(Throwable paramAnonymousThrowable)
      {
        try
        {
          EuiccCardController.loge("listNotifications callback onException: ", paramAnonymousThrowable);
          paramIListNotificationsCallback.onComplete(EuiccCardController.this.getResultCode(paramAnonymousThrowable), null);
        }
        catch (RemoteException paramAnonymousThrowable)
        {
          EuiccCardController.loge("listNotifications callback failure.", paramAnonymousThrowable);
        }
      }
      
      public void onResult(EuiccNotification[] paramAnonymousArrayOfEuiccNotification)
      {
        try
        {
          paramIListNotificationsCallback.onComplete(0, paramAnonymousArrayOfEuiccNotification);
        }
        catch (RemoteException paramAnonymousArrayOfEuiccNotification)
        {
          EuiccCardController.loge("listNotifications callback failure.", paramAnonymousArrayOfEuiccNotification);
        }
      }
    }, mEuiccMainThreadHandler);
  }
  
  public void loadBoundProfilePackage(String paramString1, String paramString2, byte[] paramArrayOfByte, final ILoadBoundProfilePackageCallback paramILoadBoundProfilePackageCallback)
  {
    checkCallingPackage(paramString1);
    paramString1 = getEuiccCard(paramString2);
    if (paramString1 == null)
    {
      try
      {
        paramILoadBoundProfilePackageCallback.onComplete(-2, null);
      }
      catch (RemoteException paramString1)
      {
        loge("loadBoundProfilePackage callback failure.", paramString1);
      }
      return;
    }
    paramString1.loadBoundProfilePackage(paramArrayOfByte, new AsyncResultCallback()
    {
      public void onException(Throwable paramAnonymousThrowable)
      {
        try
        {
          EuiccCardController.loge("loadBoundProfilePackage callback onException: ", paramAnonymousThrowable);
          paramILoadBoundProfilePackageCallback.onComplete(EuiccCardController.this.getResultCode(paramAnonymousThrowable), null);
        }
        catch (RemoteException paramAnonymousThrowable)
        {
          EuiccCardController.loge("loadBoundProfilePackage callback failure.", paramAnonymousThrowable);
        }
      }
      
      public void onResult(byte[] paramAnonymousArrayOfByte)
      {
        Log.i("EuiccCardController", "Request subscription info list refresh after install.");
        SubscriptionController.getInstance().requestEmbeddedSubscriptionInfoListRefresh();
        try
        {
          paramILoadBoundProfilePackageCallback.onComplete(0, paramAnonymousArrayOfByte);
        }
        catch (RemoteException paramAnonymousArrayOfByte)
        {
          EuiccCardController.loge("loadBoundProfilePackage callback failure.", paramAnonymousArrayOfByte);
        }
      }
    }, mEuiccMainThreadHandler);
  }
  
  public void prepareDownload(String paramString1, String paramString2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4, final IPrepareDownloadCallback paramIPrepareDownloadCallback)
  {
    checkCallingPackage(paramString1);
    paramString1 = getEuiccCard(paramString2);
    if (paramString1 == null)
    {
      try
      {
        paramIPrepareDownloadCallback.onComplete(-2, null);
      }
      catch (RemoteException paramString1)
      {
        loge("prepareDownload callback failure.", paramString1);
      }
      return;
    }
    paramString1.prepareDownload(paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3, paramArrayOfByte4, new AsyncResultCallback()
    {
      public void onException(Throwable paramAnonymousThrowable)
      {
        try
        {
          EuiccCardController.loge("prepareDownload callback onException: ", paramAnonymousThrowable);
          paramIPrepareDownloadCallback.onComplete(EuiccCardController.this.getResultCode(paramAnonymousThrowable), null);
        }
        catch (RemoteException paramAnonymousThrowable)
        {
          EuiccCardController.loge("prepareDownload callback failure.", paramAnonymousThrowable);
        }
      }
      
      public void onResult(byte[] paramAnonymousArrayOfByte)
      {
        try
        {
          paramIPrepareDownloadCallback.onComplete(0, paramAnonymousArrayOfByte);
        }
        catch (RemoteException paramAnonymousArrayOfByte)
        {
          EuiccCardController.loge("prepareDownload callback failure.", paramAnonymousArrayOfByte);
        }
      }
    }, mEuiccMainThreadHandler);
  }
  
  public void removeNotificationFromList(String paramString1, String paramString2, int paramInt, final IRemoveNotificationFromListCallback paramIRemoveNotificationFromListCallback)
  {
    checkCallingPackage(paramString1);
    paramString1 = getEuiccCard(paramString2);
    if (paramString1 == null)
    {
      try
      {
        paramIRemoveNotificationFromListCallback.onComplete(-2);
      }
      catch (RemoteException paramString1)
      {
        loge("removeNotificationFromList callback failure.", paramString1);
      }
      return;
    }
    paramString1.removeNotificationFromList(paramInt, new AsyncResultCallback()
    {
      public void onException(Throwable paramAnonymousThrowable)
      {
        try
        {
          EuiccCardController.loge("removeNotificationFromList callback onException: ", paramAnonymousThrowable);
          paramIRemoveNotificationFromListCallback.onComplete(EuiccCardController.this.getResultCode(paramAnonymousThrowable));
        }
        catch (RemoteException paramAnonymousThrowable)
        {
          EuiccCardController.loge("removeNotificationFromList callback failure.", paramAnonymousThrowable);
        }
      }
      
      public void onResult(Void paramAnonymousVoid)
      {
        try
        {
          paramIRemoveNotificationFromListCallback.onComplete(0);
        }
        catch (RemoteException paramAnonymousVoid)
        {
          EuiccCardController.loge("removeNotificationFromList callback failure.", paramAnonymousVoid);
        }
      }
    }, mEuiccMainThreadHandler);
  }
  
  public void resetMemory(String paramString1, String paramString2, int paramInt, final IResetMemoryCallback paramIResetMemoryCallback)
  {
    checkCallingPackage(paramString1);
    paramString1 = getEuiccCard(paramString2);
    if (paramString1 == null)
    {
      try
      {
        paramIResetMemoryCallback.onComplete(-2);
      }
      catch (RemoteException paramString1)
      {
        loge("resetMemory callback failure.", paramString1);
      }
      return;
    }
    paramString1.resetMemory(paramInt, new AsyncResultCallback()
    {
      public void onException(Throwable paramAnonymousThrowable)
      {
        try
        {
          EuiccCardController.loge("resetMemory callback onException: ", paramAnonymousThrowable);
          paramIResetMemoryCallback.onComplete(EuiccCardController.this.getResultCode(paramAnonymousThrowable));
        }
        catch (RemoteException paramAnonymousThrowable)
        {
          EuiccCardController.loge("resetMemory callback failure.", paramAnonymousThrowable);
        }
      }
      
      public void onResult(Void paramAnonymousVoid)
      {
        Log.i("EuiccCardController", "Request subscription info list refresh after reset memory.");
        SubscriptionController.getInstance().requestEmbeddedSubscriptionInfoListRefresh();
        try
        {
          paramIResetMemoryCallback.onComplete(0);
        }
        catch (RemoteException paramAnonymousVoid)
        {
          EuiccCardController.loge("resetMemory callback failure.", paramAnonymousVoid);
        }
      }
    }, mEuiccMainThreadHandler);
  }
  
  public void retrieveNotification(String paramString1, String paramString2, int paramInt, final IRetrieveNotificationCallback paramIRetrieveNotificationCallback)
  {
    checkCallingPackage(paramString1);
    paramString1 = getEuiccCard(paramString2);
    if (paramString1 == null)
    {
      try
      {
        paramIRetrieveNotificationCallback.onComplete(-2, null);
      }
      catch (RemoteException paramString1)
      {
        loge("retrieveNotification callback failure.", paramString1);
      }
      return;
    }
    paramString1.retrieveNotification(paramInt, new AsyncResultCallback()
    {
      public void onException(Throwable paramAnonymousThrowable)
      {
        try
        {
          EuiccCardController.loge("retrieveNotification callback onException: ", paramAnonymousThrowable);
          paramIRetrieveNotificationCallback.onComplete(EuiccCardController.this.getResultCode(paramAnonymousThrowable), null);
        }
        catch (RemoteException paramAnonymousThrowable)
        {
          EuiccCardController.loge("retrieveNotification callback failure.", paramAnonymousThrowable);
        }
      }
      
      public void onResult(EuiccNotification paramAnonymousEuiccNotification)
      {
        try
        {
          paramIRetrieveNotificationCallback.onComplete(0, paramAnonymousEuiccNotification);
        }
        catch (RemoteException paramAnonymousEuiccNotification)
        {
          EuiccCardController.loge("retrieveNotification callback failure.", paramAnonymousEuiccNotification);
        }
      }
    }, mEuiccMainThreadHandler);
  }
  
  public void retrieveNotificationList(String paramString1, String paramString2, int paramInt, final IRetrieveNotificationListCallback paramIRetrieveNotificationListCallback)
  {
    checkCallingPackage(paramString1);
    paramString1 = getEuiccCard(paramString2);
    if (paramString1 == null)
    {
      try
      {
        paramIRetrieveNotificationListCallback.onComplete(-2, null);
      }
      catch (RemoteException paramString1)
      {
        loge("retrieveNotificationList callback failure.", paramString1);
      }
      return;
    }
    paramString1.retrieveNotificationList(paramInt, new AsyncResultCallback()
    {
      public void onException(Throwable paramAnonymousThrowable)
      {
        try
        {
          EuiccCardController.loge("retrieveNotificationList callback onException: ", paramAnonymousThrowable);
          paramIRetrieveNotificationListCallback.onComplete(EuiccCardController.this.getResultCode(paramAnonymousThrowable), null);
        }
        catch (RemoteException paramAnonymousThrowable)
        {
          EuiccCardController.loge("retrieveNotificationList callback failure.", paramAnonymousThrowable);
        }
      }
      
      public void onResult(EuiccNotification[] paramAnonymousArrayOfEuiccNotification)
      {
        try
        {
          paramIRetrieveNotificationListCallback.onComplete(0, paramAnonymousArrayOfEuiccNotification);
        }
        catch (RemoteException paramAnonymousArrayOfEuiccNotification)
        {
          EuiccCardController.loge("retrieveNotificationList callback failure.", paramAnonymousArrayOfEuiccNotification);
        }
      }
    }, mEuiccMainThreadHandler);
  }
  
  public void setDefaultSmdpAddress(String paramString1, String paramString2, String paramString3, final ISetDefaultSmdpAddressCallback paramISetDefaultSmdpAddressCallback)
  {
    checkCallingPackage(paramString1);
    paramString1 = getEuiccCard(paramString2);
    if (paramString1 == null)
    {
      try
      {
        paramISetDefaultSmdpAddressCallback.onComplete(-2);
      }
      catch (RemoteException paramString1)
      {
        loge("setDefaultSmdpAddress callback failure.", paramString1);
      }
      return;
    }
    paramString1.setDefaultSmdpAddress(paramString3, new AsyncResultCallback()
    {
      public void onException(Throwable paramAnonymousThrowable)
      {
        try
        {
          EuiccCardController.loge("setDefaultSmdpAddress callback onException: ", paramAnonymousThrowable);
          paramISetDefaultSmdpAddressCallback.onComplete(EuiccCardController.this.getResultCode(paramAnonymousThrowable));
        }
        catch (RemoteException paramAnonymousThrowable)
        {
          EuiccCardController.loge("setDefaultSmdpAddress callback failure.", paramAnonymousThrowable);
        }
      }
      
      public void onResult(Void paramAnonymousVoid)
      {
        try
        {
          paramISetDefaultSmdpAddressCallback.onComplete(0);
        }
        catch (RemoteException paramAnonymousVoid)
        {
          EuiccCardController.loge("setDefaultSmdpAddress callback failure.", paramAnonymousVoid);
        }
      }
    }, mEuiccMainThreadHandler);
  }
  
  public void setNickname(String paramString1, String paramString2, String paramString3, String paramString4, final ISetNicknameCallback paramISetNicknameCallback)
  {
    checkCallingPackage(paramString1);
    paramString1 = getEuiccCard(paramString2);
    if (paramString1 == null)
    {
      try
      {
        paramISetNicknameCallback.onComplete(-2);
      }
      catch (RemoteException paramString1)
      {
        loge("setNickname callback failure.", paramString1);
      }
      return;
    }
    paramString1.setNickname(paramString3, paramString4, new AsyncResultCallback()
    {
      public void onException(Throwable paramAnonymousThrowable)
      {
        try
        {
          EuiccCardController.loge("setNickname callback onException: ", paramAnonymousThrowable);
          paramISetNicknameCallback.onComplete(EuiccCardController.this.getResultCode(paramAnonymousThrowable));
        }
        catch (RemoteException paramAnonymousThrowable)
        {
          EuiccCardController.loge("setNickname callback failure.", paramAnonymousThrowable);
        }
      }
      
      public void onResult(Void paramAnonymousVoid)
      {
        try
        {
          paramISetNicknameCallback.onComplete(0);
        }
        catch (RemoteException paramAnonymousVoid)
        {
          EuiccCardController.loge("setNickname callback failure.", paramAnonymousVoid);
        }
      }
    }, mEuiccMainThreadHandler);
  }
  
  public void switchToProfile(final String paramString1, String paramString2, final String paramString3, final boolean paramBoolean, final ISwitchToProfileCallback paramISwitchToProfileCallback)
  {
    checkCallingPackage(paramString1);
    paramString1 = getEuiccCard(paramString2);
    if (paramString1 == null)
    {
      try
      {
        paramISwitchToProfileCallback.onComplete(-2, null);
      }
      catch (RemoteException paramString1)
      {
        loge("switchToProfile callback failure.", paramString1);
      }
      return;
    }
    paramString1.getProfile(paramString3, new AsyncResultCallback()
    {
      public void onException(Throwable paramAnonymousThrowable)
      {
        try
        {
          EuiccCardController.loge("getProfile in switchToProfile callback onException: ", paramAnonymousThrowable);
          paramISwitchToProfileCallback.onComplete(EuiccCardController.this.getResultCode(paramAnonymousThrowable), null);
        }
        catch (RemoteException paramAnonymousThrowable)
        {
          EuiccCardController.loge("switchToProfile callback failure.", paramAnonymousThrowable);
        }
      }
      
      public void onResult(final EuiccProfileInfo paramAnonymousEuiccProfileInfo)
      {
        paramAnonymousEuiccProfileInfo = new AsyncResultCallback()
        {
          public void onException(Throwable paramAnonymous2Throwable)
          {
            try
            {
              EuiccCardController.loge("switchToProfile callback onException: ", paramAnonymous2Throwable);
              val$callback.onComplete(EuiccCardController.this.getResultCode(paramAnonymous2Throwable), paramAnonymousEuiccProfileInfo);
            }
            catch (RemoteException paramAnonymous2Throwable)
            {
              EuiccCardController.loge("switchToProfile callback failure.", paramAnonymous2Throwable);
            }
          }
          
          public void onResult(Void paramAnonymous2Void)
          {
            try
            {
              val$callback.onComplete(0, paramAnonymousEuiccProfileInfo);
            }
            catch (RemoteException paramAnonymous2Void)
            {
              EuiccCardController.loge("switchToProfile callback failure.", paramAnonymous2Void);
            }
          }
        };
        paramString1.switchToProfile(paramString3, paramBoolean, paramAnonymousEuiccProfileInfo, mEuiccMainThreadHandler);
      }
    }, mEuiccMainThreadHandler);
  }
  
  private class SimSlotStatusChangedBroadcastReceiver
    extends BroadcastReceiver
  {
    private SimSlotStatusChangedBroadcastReceiver() {}
    
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if ("android.telephony.action.SIM_SLOT_STATUS_CHANGED".equals(paramIntent.getAction()))
      {
        if (isEmbeddedSlotActivated()) {
          mEuiccController.startOtaUpdatingIfNecessary();
        }
        mContext.unregisterReceiver(mSimSlotStatusChangeReceiver);
      }
    }
  }
}
