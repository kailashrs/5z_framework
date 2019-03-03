package android.telephony.euicc;

import android.annotation.SystemApi;
import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.service.euicc.EuiccProfileInfo;
import android.util.Log;
import com.android.internal.telephony.euicc.IAuthenticateServerCallback.Stub;
import com.android.internal.telephony.euicc.ICancelSessionCallback.Stub;
import com.android.internal.telephony.euicc.IDeleteProfileCallback.Stub;
import com.android.internal.telephony.euicc.IDisableProfileCallback.Stub;
import com.android.internal.telephony.euicc.IEuiccCardController;
import com.android.internal.telephony.euicc.IEuiccCardController.Stub;
import com.android.internal.telephony.euicc.IGetAllProfilesCallback.Stub;
import com.android.internal.telephony.euicc.IGetDefaultSmdpAddressCallback.Stub;
import com.android.internal.telephony.euicc.IGetEuiccChallengeCallback.Stub;
import com.android.internal.telephony.euicc.IGetEuiccInfo1Callback.Stub;
import com.android.internal.telephony.euicc.IGetEuiccInfo2Callback.Stub;
import com.android.internal.telephony.euicc.IGetProfileCallback.Stub;
import com.android.internal.telephony.euicc.IGetRulesAuthTableCallback.Stub;
import com.android.internal.telephony.euicc.IGetSmdsAddressCallback.Stub;
import com.android.internal.telephony.euicc.IListNotificationsCallback.Stub;
import com.android.internal.telephony.euicc.ILoadBoundProfilePackageCallback.Stub;
import com.android.internal.telephony.euicc.IPrepareDownloadCallback.Stub;
import com.android.internal.telephony.euicc.IRemoveNotificationFromListCallback.Stub;
import com.android.internal.telephony.euicc.IResetMemoryCallback.Stub;
import com.android.internal.telephony.euicc.IRetrieveNotificationCallback.Stub;
import com.android.internal.telephony.euicc.IRetrieveNotificationListCallback.Stub;
import com.android.internal.telephony.euicc.ISetDefaultSmdpAddressCallback.Stub;
import com.android.internal.telephony.euicc.ISetNicknameCallback.Stub;
import com.android.internal.telephony.euicc.ISwitchToProfileCallback.Stub;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.Executor;

@SystemApi
public class EuiccCardManager
{
  public static final int CANCEL_REASON_END_USER_REJECTED = 0;
  public static final int CANCEL_REASON_POSTPONED = 1;
  public static final int CANCEL_REASON_PPR_NOT_ALLOWED = 3;
  public static final int CANCEL_REASON_TIMEOUT = 2;
  public static final int RESET_OPTION_DELETE_FIELD_LOADED_TEST_PROFILES = 2;
  public static final int RESET_OPTION_DELETE_OPERATIONAL_PROFILES = 1;
  public static final int RESET_OPTION_RESET_DEFAULT_SMDP_ADDRESS = 4;
  public static final int RESULT_EUICC_NOT_FOUND = -2;
  public static final int RESULT_OK = 0;
  public static final int RESULT_UNKNOWN_ERROR = -1;
  private static final String TAG = "EuiccCardManager";
  private final Context mContext;
  
  public EuiccCardManager(Context paramContext)
  {
    mContext = paramContext;
  }
  
  private IEuiccCardController getIEuiccCardController()
  {
    return IEuiccCardController.Stub.asInterface(ServiceManager.getService("euicc_card_controller"));
  }
  
  public void authenticateServer(String paramString1, String paramString2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4, Executor paramExecutor, ResultCallback<byte[]> paramResultCallback)
  {
    try
    {
      IEuiccCardController localIEuiccCardController = getIEuiccCardController();
      String str = mContext.getOpPackageName();
      IAuthenticateServerCallback.Stub local15 = new android/telephony/euicc/EuiccCardManager$15;
      try
      {
        local15.<init>(this, paramExecutor, paramResultCallback);
        localIEuiccCardController.authenticateServer(str, paramString1, paramString2, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3, paramArrayOfByte4, local15);
        return;
      }
      catch (RemoteException paramString1) {}
      Log.e("EuiccCardManager", "Error calling authenticateServer", paramString1);
    }
    catch (RemoteException paramString1) {}
    throw paramString1.rethrowFromSystemServer();
  }
  
  public void cancelSession(String paramString, byte[] paramArrayOfByte, int paramInt, Executor paramExecutor, ResultCallback<byte[]> paramResultCallback)
  {
    try
    {
      IEuiccCardController localIEuiccCardController = getIEuiccCardController();
      String str = mContext.getOpPackageName();
      ICancelSessionCallback.Stub local18 = new android/telephony/euicc/EuiccCardManager$18;
      local18.<init>(this, paramExecutor, paramResultCallback);
      localIEuiccCardController.cancelSession(str, paramString, paramArrayOfByte, paramInt, local18);
      return;
    }
    catch (RemoteException paramString)
    {
      Log.e("EuiccCardManager", "Error calling cancelSession", paramString);
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void deleteProfile(String paramString1, String paramString2, Executor paramExecutor, ResultCallback<Void> paramResultCallback)
  {
    try
    {
      IEuiccCardController localIEuiccCardController = getIEuiccCardController();
      String str = mContext.getOpPackageName();
      IDeleteProfileCallback.Stub local6 = new android/telephony/euicc/EuiccCardManager$6;
      local6.<init>(this, paramExecutor, paramResultCallback);
      localIEuiccCardController.deleteProfile(str, paramString1, paramString2, local6);
      return;
    }
    catch (RemoteException paramString1)
    {
      Log.e("EuiccCardManager", "Error calling deleteProfile", paramString1);
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  public void disableProfile(String paramString1, String paramString2, boolean paramBoolean, Executor paramExecutor, ResultCallback<Void> paramResultCallback)
  {
    try
    {
      IEuiccCardController localIEuiccCardController = getIEuiccCardController();
      String str = mContext.getOpPackageName();
      IDisableProfileCallback.Stub local3 = new android/telephony/euicc/EuiccCardManager$3;
      local3.<init>(this, paramExecutor, paramResultCallback);
      localIEuiccCardController.disableProfile(str, paramString1, paramString2, paramBoolean, local3);
      return;
    }
    catch (RemoteException paramString1)
    {
      Log.e("EuiccCardManager", "Error calling disableProfile", paramString1);
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  public void listNotifications(String paramString, int paramInt, Executor paramExecutor, ResultCallback<EuiccNotification[]> paramResultCallback)
  {
    try
    {
      IEuiccCardController localIEuiccCardController = getIEuiccCardController();
      String str = mContext.getOpPackageName();
      IListNotificationsCallback.Stub local19 = new android/telephony/euicc/EuiccCardManager$19;
      local19.<init>(this, paramExecutor, paramResultCallback);
      localIEuiccCardController.listNotifications(str, paramString, paramInt, local19);
      return;
    }
    catch (RemoteException paramString)
    {
      Log.e("EuiccCardManager", "Error calling listNotifications", paramString);
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void loadBoundProfilePackage(String paramString, byte[] paramArrayOfByte, Executor paramExecutor, ResultCallback<byte[]> paramResultCallback)
  {
    try
    {
      IEuiccCardController localIEuiccCardController = getIEuiccCardController();
      String str = mContext.getOpPackageName();
      ILoadBoundProfilePackageCallback.Stub local17 = new android/telephony/euicc/EuiccCardManager$17;
      local17.<init>(this, paramExecutor, paramResultCallback);
      localIEuiccCardController.loadBoundProfilePackage(str, paramString, paramArrayOfByte, local17);
      return;
    }
    catch (RemoteException paramString)
    {
      Log.e("EuiccCardManager", "Error calling loadBoundProfilePackage", paramString);
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void prepareDownload(String paramString, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4, Executor paramExecutor, ResultCallback<byte[]> paramResultCallback)
  {
    try
    {
      IEuiccCardController localIEuiccCardController = getIEuiccCardController();
      String str = mContext.getOpPackageName();
      IPrepareDownloadCallback.Stub local16 = new android/telephony/euicc/EuiccCardManager$16;
      local16.<init>(this, paramExecutor, paramResultCallback);
      localIEuiccCardController.prepareDownload(str, paramString, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3, paramArrayOfByte4, local16);
      return;
    }
    catch (RemoteException paramString)
    {
      Log.e("EuiccCardManager", "Error calling prepareDownload", paramString);
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void removeNotificationFromList(String paramString, int paramInt, Executor paramExecutor, ResultCallback<Void> paramResultCallback)
  {
    try
    {
      IEuiccCardController localIEuiccCardController = getIEuiccCardController();
      String str = mContext.getOpPackageName();
      IRemoveNotificationFromListCallback.Stub local22 = new android/telephony/euicc/EuiccCardManager$22;
      local22.<init>(this, paramExecutor, paramResultCallback);
      localIEuiccCardController.removeNotificationFromList(str, paramString, paramInt, local22);
      return;
    }
    catch (RemoteException paramString)
    {
      Log.e("EuiccCardManager", "Error calling removeNotificationFromList", paramString);
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void requestAllProfiles(String paramString, Executor paramExecutor, ResultCallback<EuiccProfileInfo[]> paramResultCallback)
  {
    try
    {
      IEuiccCardController localIEuiccCardController = getIEuiccCardController();
      String str = mContext.getOpPackageName();
      IGetAllProfilesCallback.Stub local1 = new android/telephony/euicc/EuiccCardManager$1;
      local1.<init>(this, paramExecutor, paramResultCallback);
      localIEuiccCardController.getAllProfiles(str, paramString, local1);
      return;
    }
    catch (RemoteException paramString)
    {
      Log.e("EuiccCardManager", "Error calling getAllProfiles", paramString);
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void requestDefaultSmdpAddress(String paramString, Executor paramExecutor, ResultCallback<String> paramResultCallback)
  {
    try
    {
      IEuiccCardController localIEuiccCardController = getIEuiccCardController();
      String str = mContext.getOpPackageName();
      IGetDefaultSmdpAddressCallback.Stub local8 = new android/telephony/euicc/EuiccCardManager$8;
      local8.<init>(this, paramExecutor, paramResultCallback);
      localIEuiccCardController.getDefaultSmdpAddress(str, paramString, local8);
      return;
    }
    catch (RemoteException paramString)
    {
      Log.e("EuiccCardManager", "Error calling getDefaultSmdpAddress", paramString);
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void requestEuiccChallenge(String paramString, Executor paramExecutor, ResultCallback<byte[]> paramResultCallback)
  {
    try
    {
      IEuiccCardController localIEuiccCardController = getIEuiccCardController();
      String str = mContext.getOpPackageName();
      IGetEuiccChallengeCallback.Stub local12 = new android/telephony/euicc/EuiccCardManager$12;
      local12.<init>(this, paramExecutor, paramResultCallback);
      localIEuiccCardController.getEuiccChallenge(str, paramString, local12);
      return;
    }
    catch (RemoteException paramString)
    {
      Log.e("EuiccCardManager", "Error calling getEuiccChallenge", paramString);
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void requestEuiccInfo1(String paramString, Executor paramExecutor, ResultCallback<byte[]> paramResultCallback)
  {
    try
    {
      IEuiccCardController localIEuiccCardController = getIEuiccCardController();
      String str = mContext.getOpPackageName();
      IGetEuiccInfo1Callback.Stub local13 = new android/telephony/euicc/EuiccCardManager$13;
      local13.<init>(this, paramExecutor, paramResultCallback);
      localIEuiccCardController.getEuiccInfo1(str, paramString, local13);
      return;
    }
    catch (RemoteException paramString)
    {
      Log.e("EuiccCardManager", "Error calling getEuiccInfo1", paramString);
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void requestEuiccInfo2(String paramString, Executor paramExecutor, ResultCallback<byte[]> paramResultCallback)
  {
    try
    {
      IEuiccCardController localIEuiccCardController = getIEuiccCardController();
      String str = mContext.getOpPackageName();
      IGetEuiccInfo2Callback.Stub local14 = new android/telephony/euicc/EuiccCardManager$14;
      local14.<init>(this, paramExecutor, paramResultCallback);
      localIEuiccCardController.getEuiccInfo2(str, paramString, local14);
      return;
    }
    catch (RemoteException paramString)
    {
      Log.e("EuiccCardManager", "Error calling getEuiccInfo2", paramString);
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void requestProfile(String paramString1, String paramString2, Executor paramExecutor, ResultCallback<EuiccProfileInfo> paramResultCallback)
  {
    try
    {
      IEuiccCardController localIEuiccCardController = getIEuiccCardController();
      String str = mContext.getOpPackageName();
      IGetProfileCallback.Stub local2 = new android/telephony/euicc/EuiccCardManager$2;
      local2.<init>(this, paramExecutor, paramResultCallback);
      localIEuiccCardController.getProfile(str, paramString1, paramString2, local2);
      return;
    }
    catch (RemoteException paramString1)
    {
      Log.e("EuiccCardManager", "Error calling getProfile", paramString1);
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  public void requestRulesAuthTable(String paramString, Executor paramExecutor, ResultCallback<EuiccRulesAuthTable> paramResultCallback)
  {
    try
    {
      IEuiccCardController localIEuiccCardController = getIEuiccCardController();
      String str = mContext.getOpPackageName();
      IGetRulesAuthTableCallback.Stub local11 = new android/telephony/euicc/EuiccCardManager$11;
      local11.<init>(this, paramExecutor, paramResultCallback);
      localIEuiccCardController.getRulesAuthTable(str, paramString, local11);
      return;
    }
    catch (RemoteException paramString)
    {
      Log.e("EuiccCardManager", "Error calling getRulesAuthTable", paramString);
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void requestSmdsAddress(String paramString, Executor paramExecutor, ResultCallback<String> paramResultCallback)
  {
    try
    {
      IEuiccCardController localIEuiccCardController = getIEuiccCardController();
      String str = mContext.getOpPackageName();
      IGetSmdsAddressCallback.Stub local9 = new android/telephony/euicc/EuiccCardManager$9;
      local9.<init>(this, paramExecutor, paramResultCallback);
      localIEuiccCardController.getSmdsAddress(str, paramString, local9);
      return;
    }
    catch (RemoteException paramString)
    {
      Log.e("EuiccCardManager", "Error calling getSmdsAddress", paramString);
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void resetMemory(String paramString, int paramInt, Executor paramExecutor, ResultCallback<Void> paramResultCallback)
  {
    try
    {
      IEuiccCardController localIEuiccCardController = getIEuiccCardController();
      String str = mContext.getOpPackageName();
      IResetMemoryCallback.Stub local7 = new android/telephony/euicc/EuiccCardManager$7;
      local7.<init>(this, paramExecutor, paramResultCallback);
      localIEuiccCardController.resetMemory(str, paramString, paramInt, local7);
      return;
    }
    catch (RemoteException paramString)
    {
      Log.e("EuiccCardManager", "Error calling resetMemory", paramString);
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void retrieveNotification(String paramString, int paramInt, Executor paramExecutor, ResultCallback<EuiccNotification> paramResultCallback)
  {
    try
    {
      IEuiccCardController localIEuiccCardController = getIEuiccCardController();
      String str = mContext.getOpPackageName();
      IRetrieveNotificationCallback.Stub local21 = new android/telephony/euicc/EuiccCardManager$21;
      local21.<init>(this, paramExecutor, paramResultCallback);
      localIEuiccCardController.retrieveNotification(str, paramString, paramInt, local21);
      return;
    }
    catch (RemoteException paramString)
    {
      Log.e("EuiccCardManager", "Error calling retrieveNotification", paramString);
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void retrieveNotificationList(String paramString, int paramInt, Executor paramExecutor, ResultCallback<EuiccNotification[]> paramResultCallback)
  {
    try
    {
      IEuiccCardController localIEuiccCardController = getIEuiccCardController();
      String str = mContext.getOpPackageName();
      IRetrieveNotificationListCallback.Stub local20 = new android/telephony/euicc/EuiccCardManager$20;
      local20.<init>(this, paramExecutor, paramResultCallback);
      localIEuiccCardController.retrieveNotificationList(str, paramString, paramInt, local20);
      return;
    }
    catch (RemoteException paramString)
    {
      Log.e("EuiccCardManager", "Error calling retrieveNotificationList", paramString);
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void setDefaultSmdpAddress(String paramString1, String paramString2, Executor paramExecutor, ResultCallback<Void> paramResultCallback)
  {
    try
    {
      IEuiccCardController localIEuiccCardController = getIEuiccCardController();
      String str = mContext.getOpPackageName();
      ISetDefaultSmdpAddressCallback.Stub local10 = new android/telephony/euicc/EuiccCardManager$10;
      local10.<init>(this, paramExecutor, paramResultCallback);
      localIEuiccCardController.setDefaultSmdpAddress(str, paramString1, paramString2, local10);
      return;
    }
    catch (RemoteException paramString1)
    {
      Log.e("EuiccCardManager", "Error calling setDefaultSmdpAddress", paramString1);
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  public void setNickname(String paramString1, String paramString2, String paramString3, Executor paramExecutor, ResultCallback<Void> paramResultCallback)
  {
    try
    {
      IEuiccCardController localIEuiccCardController = getIEuiccCardController();
      String str = mContext.getOpPackageName();
      ISetNicknameCallback.Stub local5 = new android/telephony/euicc/EuiccCardManager$5;
      local5.<init>(this, paramExecutor, paramResultCallback);
      localIEuiccCardController.setNickname(str, paramString1, paramString2, paramString3, local5);
      return;
    }
    catch (RemoteException paramString1)
    {
      Log.e("EuiccCardManager", "Error calling setNickname", paramString1);
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  public void switchToProfile(String paramString1, String paramString2, boolean paramBoolean, Executor paramExecutor, ResultCallback<EuiccProfileInfo> paramResultCallback)
  {
    try
    {
      IEuiccCardController localIEuiccCardController = getIEuiccCardController();
      String str = mContext.getOpPackageName();
      ISwitchToProfileCallback.Stub local4 = new android/telephony/euicc/EuiccCardManager$4;
      local4.<init>(this, paramExecutor, paramResultCallback);
      localIEuiccCardController.switchToProfile(str, paramString1, paramString2, paramBoolean, local4);
      return;
    }
    catch (RemoteException paramString1)
    {
      Log.e("EuiccCardManager", "Error calling switchToProfile", paramString1);
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface CancelReason {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ResetOption {}
  
  public static abstract interface ResultCallback<T>
  {
    public abstract void onComplete(int paramInt, T paramT);
  }
}
