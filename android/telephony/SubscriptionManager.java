package android.telephony;

import android.annotation.SystemApi;
import android.app.BroadcastOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.INetworkPolicyManager;
import android.net.INetworkPolicyManager.Stub;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.DisplayMetrics;
import com.android.internal.telephony.IOnSubscriptionsChangedListener;
import com.android.internal.telephony.IOnSubscriptionsChangedListener.Stub;
import com.android.internal.telephony.ISub;
import com.android.internal.telephony.ISub.Stub;
import com.android.internal.telephony.ITelephonyRegistry;
import com.android.internal.telephony.ITelephonyRegistry.Stub;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SubscriptionManager
{
  public static final String ACCESS_RULES = "access_rules";
  public static final String ACTION_DEFAULT_SMS_SUBSCRIPTION_CHANGED = "android.telephony.action.DEFAULT_SMS_SUBSCRIPTION_CHANGED";
  public static final String ACTION_DEFAULT_SUBSCRIPTION_CHANGED = "android.telephony.action.DEFAULT_SUBSCRIPTION_CHANGED";
  @SystemApi
  public static final String ACTION_MANAGE_SUBSCRIPTION_PLANS = "android.telephony.action.MANAGE_SUBSCRIPTION_PLANS";
  @SystemApi
  public static final String ACTION_REFRESH_SUBSCRIPTION_PLANS = "android.telephony.action.REFRESH_SUBSCRIPTION_PLANS";
  public static final String ACTION_SUBSCRIPTION_PLANS_CHANGED = "android.telephony.action.SUBSCRIPTION_PLANS_CHANGED";
  public static final String CARD_ID = "card_id";
  public static final String CARRIER_NAME = "carrier_name";
  public static final String CB_ALERT_REMINDER_INTERVAL = "alert_reminder_interval";
  public static final String CB_ALERT_SOUND_DURATION = "alert_sound_duration";
  public static final String CB_ALERT_SPEECH = "enable_alert_speech";
  public static final String CB_ALERT_VIBRATE = "enable_alert_vibrate";
  public static final String CB_AMBER_ALERT = "enable_cmas_amber_alerts";
  public static final String CB_CHANNEL_50_ALERT = "enable_channel_50_alerts";
  public static final String CB_CMAS_TEST_ALERT = "enable_cmas_test_alerts";
  public static final String CB_EMERGENCY_ALERT = "enable_emergency_alerts";
  public static final String CB_ETWS_TEST_ALERT = "enable_etws_test_alerts";
  public static final String CB_EXTREME_THREAT_ALERT = "enable_cmas_extreme_threat_alerts";
  public static final String CB_OPT_OUT_DIALOG = "show_cmas_opt_out_dialog";
  public static final String CB_SEVERE_THREAT_ALERT = "enable_cmas_severe_threat_alerts";
  public static final String COLOR = "color";
  public static final int COLOR_1 = 0;
  public static final int COLOR_2 = 1;
  public static final int COLOR_3 = 2;
  public static final int COLOR_4 = 3;
  public static final int COLOR_DEFAULT = 0;
  public static final Uri CONTENT_URI = Uri.parse("content://telephony/siminfo");
  public static final String DATA_ROAMING = "data_roaming";
  public static final int DATA_ROAMING_DEFAULT = 0;
  public static final int DATA_ROAMING_DISABLE = 0;
  public static final int DATA_ROAMING_ENABLE = 1;
  private static final boolean DBG = false;
  public static final int DEFAULT_NAME_RES = 17039374;
  public static final int DEFAULT_PHONE_INDEX = Integer.MAX_VALUE;
  public static final int DEFAULT_SIM_SLOT_INDEX = Integer.MAX_VALUE;
  public static final int DEFAULT_SUBSCRIPTION_ID = Integer.MAX_VALUE;
  public static final String DISPLAY_NAME = "display_name";
  public static final int DISPLAY_NUMBER_DEFAULT = 1;
  public static final int DISPLAY_NUMBER_FIRST = 1;
  public static final String DISPLAY_NUMBER_FORMAT = "display_number_format";
  public static final int DISPLAY_NUMBER_LAST = 2;
  public static final int DISPLAY_NUMBER_NONE = 0;
  public static final int DUMMY_SUBSCRIPTION_ID_BASE = -2;
  public static final String ENHANCED_4G_MODE_ENABLED = "volte_vt_enabled";
  public static final String EXTRA_SUBSCRIPTION_INDEX = "android.telephony.extra.SUBSCRIPTION_INDEX";
  public static final String ICC_ID = "icc_id";
  public static final int INVALID_PHONE_INDEX = -1;
  public static final int INVALID_SIM_SLOT_INDEX = -1;
  public static final int INVALID_SUBSCRIPTION_ID = -1;
  public static final String IS_EMBEDDED = "is_embedded";
  public static final String IS_REMOVABLE = "is_removable";
  private static final String LOG_TAG = "SubscriptionManager";
  public static final int MAX_SUBSCRIPTION_ID_VALUE = 2147483646;
  public static final String MCC = "mcc";
  public static final int MIN_SUBSCRIPTION_ID_VALUE = 0;
  public static final String MNC = "mnc";
  public static final String NAME_SOURCE = "name_source";
  public static final int NAME_SOURCE_DEFAULT_SOURCE = 0;
  public static final int NAME_SOURCE_SIM_SOURCE = 1;
  public static final int NAME_SOURCE_UNDEFINDED = -1;
  public static final int NAME_SOURCE_USER_INPUT = 2;
  public static final String NUMBER = "number";
  public static final int SIM_NOT_INSERTED = -1;
  public static final int SIM_PROVISIONED = 0;
  public static final String SIM_PROVISIONING_STATUS = "sim_provisioning_status";
  public static final String SIM_SLOT_INDEX = "sim_id";
  public static final String SUB_DEFAULT_CHANGED_ACTION = "android.intent.action.SUB_DEFAULT_CHANGED";
  public static final String UNIQUE_KEY_SUBSCRIPTION_ID = "_id";
  private static final boolean VDBG = false;
  public static final String VT_IMS_ENABLED = "vt_ims_enabled";
  public static final String WFC_IMS_ENABLED = "wfc_ims_enabled";
  public static final String WFC_IMS_MODE = "wfc_ims_mode";
  public static final String WFC_IMS_ROAMING_ENABLED = "wfc_ims_roaming_enabled";
  public static final String WFC_IMS_ROAMING_MODE = "wfc_ims_roaming_mode";
  private final Context mContext;
  private volatile INetworkPolicyManager mNetworkPolicy;
  
  public SubscriptionManager(Context paramContext)
  {
    mContext = paramContext;
  }
  
  private Intent createRefreshSubscriptionIntent(int paramInt)
  {
    String str = getSubscriptionPlansOwner(paramInt);
    if (str == null) {
      return null;
    }
    if (getSubscriptionPlans(paramInt).isEmpty()) {
      return null;
    }
    Intent localIntent = new Intent("android.telephony.action.REFRESH_SUBSCRIPTION_PLANS");
    localIntent.addFlags(268435456);
    localIntent.setPackage(str);
    localIntent.putExtra("android.telephony.extra.SUBSCRIPTION_INDEX", paramInt);
    if (mContext.getPackageManager().queryBroadcastReceivers(localIntent, 0).isEmpty()) {
      return null;
    }
    return localIntent;
  }
  
  @Deprecated
  public static SubscriptionManager from(Context paramContext)
  {
    return (SubscriptionManager)paramContext.getSystemService("telephony_subscription_service");
  }
  
  public static boolean getBooleanSubscriptionProperty(int paramInt, String paramString, boolean paramBoolean, Context paramContext)
  {
    paramString = getSubscriptionProperty(paramInt, paramString, paramContext);
    if (paramString != null) {
      try
      {
        paramInt = Integer.parseInt(paramString);
        paramBoolean = true;
        if (paramInt != 1) {
          paramBoolean = false;
        }
        return paramBoolean;
      }
      catch (NumberFormatException paramString)
      {
        logd("getBooleanSubscriptionProperty NumberFormat exception");
      }
    }
    return paramBoolean;
  }
  
  public static int getDefaultDataSubscriptionId()
  {
    int i = -1;
    int j;
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      j = i;
      if (localISub != null) {
        j = localISub.getDefaultDataSubId();
      }
    }
    catch (RemoteException localRemoteException)
    {
      j = i;
    }
    return j;
  }
  
  public static int getDefaultSmsSubscriptionId()
  {
    int i = -1;
    int j;
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      j = i;
      if (localISub != null) {
        j = localISub.getDefaultSmsSubId();
      }
    }
    catch (RemoteException localRemoteException)
    {
      j = i;
    }
    return j;
  }
  
  public static int getDefaultSubscriptionId()
  {
    int i = -1;
    int j;
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      j = i;
      if (localISub != null) {
        j = localISub.getDefaultSubId();
      }
    }
    catch (RemoteException localRemoteException)
    {
      j = i;
    }
    return j;
  }
  
  public static int getDefaultVoicePhoneId()
  {
    return getPhoneId(getDefaultVoiceSubscriptionId());
  }
  
  public static int getDefaultVoiceSubscriptionId()
  {
    int i = -1;
    int j;
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      j = i;
      if (localISub != null) {
        j = localISub.getDefaultVoiceSubId();
      }
    }
    catch (RemoteException localRemoteException)
    {
      j = i;
    }
    return j;
  }
  
  public static int getIntegerSubscriptionProperty(int paramInt1, String paramString, int paramInt2, Context paramContext)
  {
    paramString = getSubscriptionProperty(paramInt1, paramString, paramContext);
    if (paramString != null) {
      try
      {
        paramInt1 = Integer.parseInt(paramString);
        return paramInt1;
      }
      catch (NumberFormatException paramString)
      {
        logd("getBooleanSubscriptionProperty NumberFormat exception");
      }
    }
    return paramInt2;
  }
  
  private final INetworkPolicyManager getNetworkPolicy()
  {
    if (mNetworkPolicy == null) {
      mNetworkPolicy = INetworkPolicyManager.Stub.asInterface(ServiceManager.getService("netpolicy"));
    }
    return mNetworkPolicy;
  }
  
  public static int getPhoneId(int paramInt)
  {
    if (!isValidSubscriptionId(paramInt)) {
      return -1;
    }
    int i = -1;
    int j;
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      j = i;
      if (localISub != null) {
        j = localISub.getPhoneId(paramInt);
      }
    }
    catch (RemoteException localRemoteException)
    {
      j = i;
    }
    return j;
  }
  
  public static Resources getResourcesForSubId(Context paramContext, int paramInt)
  {
    Object localObject1 = from(paramContext).getActiveSubscriptionInfo(paramInt);
    Object localObject2 = paramContext.getResources().getConfiguration();
    Configuration localConfiguration = new Configuration();
    localConfiguration.setTo((Configuration)localObject2);
    if (localObject1 != null)
    {
      mcc = ((SubscriptionInfo)localObject1).getMcc();
      mnc = ((SubscriptionInfo)localObject1).getMnc();
      if (mnc == 0) {
        mnc = 65535;
      }
    }
    localObject1 = paramContext.getResources().getDisplayMetrics();
    localObject2 = new DisplayMetrics();
    ((DisplayMetrics)localObject2).setTo((DisplayMetrics)localObject1);
    return new Resources(paramContext.getResources().getAssets(), (DisplayMetrics)localObject2, localConfiguration);
  }
  
  public static int getSimStateForSlotIndex(int paramInt)
  {
    int i = 0;
    int j = 0;
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      if (localISub != null) {
        j = localISub.getSimStateForSlotIndex(paramInt);
      }
    }
    catch (RemoteException localRemoteException)
    {
      j = i;
    }
    return j;
  }
  
  public static int getSlotIndex(int paramInt)
  {
    isValidSubscriptionId(paramInt);
    int i = -1;
    int j;
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      j = i;
      if (localISub != null) {
        j = localISub.getSlotIndex(paramInt);
      }
    }
    catch (RemoteException localRemoteException)
    {
      j = i;
    }
    return j;
  }
  
  public static int[] getSubId(int paramInt)
  {
    if (!isValidSlotIndex(paramInt))
    {
      logd("[getSubId]- fail");
      return null;
    }
    Object localObject1 = null;
    int[] arrayOfInt = null;
    Object localObject2;
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      if (localISub != null) {
        arrayOfInt = localISub.getSubId(paramInt);
      }
    }
    catch (RemoteException localRemoteException)
    {
      localObject2 = localObject1;
    }
    return localObject2;
  }
  
  private String getSubscriptionPlansOwner(int paramInt)
  {
    try
    {
      String str = getNetworkPolicy().getSubscriptionPlansOwner(paramInt);
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  private static String getSubscriptionProperty(int paramInt, String paramString, Context paramContext)
  {
    Object localObject1 = null;
    Object localObject2 = null;
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      if (localISub != null) {
        localObject2 = localISub.getSubscriptionProperty(paramInt, paramString, paramContext.getOpPackageName());
      }
    }
    catch (RemoteException paramString)
    {
      localObject2 = localObject1;
    }
    return localObject2;
  }
  
  public static boolean isUsableSubIdValue(int paramInt)
  {
    boolean bool;
    if ((paramInt >= 0) && (paramInt <= 2147483646)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isValidPhoneId(int paramInt)
  {
    boolean bool;
    if ((paramInt >= 0) && (paramInt < TelephonyManager.getDefault().getPhoneCount())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isValidSlotIndex(int paramInt)
  {
    boolean bool;
    if ((paramInt >= 0) && (paramInt < TelephonyManager.getDefault().getSimCount())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isValidSubscriptionId(int paramInt)
  {
    boolean bool;
    if (paramInt > -1) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static void logd(String paramString)
  {
    Rlog.d("SubscriptionManager", paramString);
  }
  
  public static void putPhoneIdAndSubIdExtra(Intent paramIntent, int paramInt)
  {
    int[] arrayOfInt = getSubId(paramInt);
    if ((arrayOfInt != null) && (arrayOfInt.length > 0)) {
      putPhoneIdAndSubIdExtra(paramIntent, paramInt, arrayOfInt[0]);
    } else {
      logd("putPhoneIdAndSubIdExtra: no valid subs");
    }
  }
  
  public static void putPhoneIdAndSubIdExtra(Intent paramIntent, int paramInt1, int paramInt2)
  {
    paramIntent.putExtra("subscription", paramInt2);
    paramIntent.putExtra("android.telephony.extra.SUBSCRIPTION_INDEX", paramInt2);
    paramIntent.putExtra("phone", paramInt1);
    paramIntent.putExtra("slot", paramInt1);
  }
  
  public static void setSubscriptionProperty(int paramInt, String paramString1, String paramString2)
  {
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      if (localISub != null) {
        localISub.setSubscriptionProperty(paramInt, paramString1, paramString2);
      }
    }
    catch (RemoteException paramString1) {}
  }
  
  public void addOnSubscriptionsChangedListener(OnSubscriptionsChangedListener paramOnSubscriptionsChangedListener)
  {
    String str;
    if (mContext != null) {
      str = mContext.getOpPackageName();
    } else {
      str = "<unknown>";
    }
    try
    {
      ITelephonyRegistry localITelephonyRegistry = ITelephonyRegistry.Stub.asInterface(ServiceManager.getService("telephony.registry"));
      if (localITelephonyRegistry != null) {
        localITelephonyRegistry.addOnSubscriptionsChangedListener(str, callback);
      }
    }
    catch (RemoteException paramOnSubscriptionsChangedListener) {}
  }
  
  public Uri addSubscriptionInfoRecord(String paramString, int paramInt)
  {
    if (paramString == null) {
      logd("[addSubscriptionInfoRecord]- null iccId");
    }
    if (!isValidSlotIndex(paramInt)) {
      logd("[addSubscriptionInfoRecord]- invalid slotIndex");
    }
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      if (localISub != null) {
        localISub.addSubInfoRecord(paramString, paramInt);
      } else {
        logd("[addSubscriptionInfoRecord]- ISub service is null");
      }
    }
    catch (RemoteException paramString) {}
    return null;
  }
  
  public boolean allDefaultsSelected()
  {
    if (!isValidSubscriptionId(getDefaultDataSubscriptionId())) {
      return false;
    }
    if (!isValidSubscriptionId(getDefaultSmsSubscriptionId())) {
      return false;
    }
    return isValidSubscriptionId(getDefaultVoiceSubscriptionId());
  }
  
  public boolean canManageSubscription(SubscriptionInfo paramSubscriptionInfo)
  {
    return canManageSubscription(paramSubscriptionInfo, mContext.getPackageName());
  }
  
  public boolean canManageSubscription(SubscriptionInfo paramSubscriptionInfo, String paramString)
  {
    if (paramSubscriptionInfo.isEmbedded())
    {
      if (paramSubscriptionInfo.getAccessRules() == null) {
        return false;
      }
      Object localObject = mContext.getPackageManager();
      try
      {
        localObject = ((PackageManager)localObject).getPackageInfo(paramString, 64);
        paramSubscriptionInfo = paramSubscriptionInfo.getAccessRules().iterator();
        while (paramSubscriptionInfo.hasNext()) {
          if (((UiccAccessRule)paramSubscriptionInfo.next()).getCarrierPrivilegeStatus((PackageInfo)localObject) == 1) {
            return true;
          }
        }
        return false;
      }
      catch (PackageManager.NameNotFoundException paramSubscriptionInfo)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Unknown package: ");
        ((StringBuilder)localObject).append(paramString);
        throw new IllegalArgumentException(((StringBuilder)localObject).toString(), paramSubscriptionInfo);
      }
    }
    throw new IllegalArgumentException("Not an embedded subscription");
  }
  
  public void clearDefaultsForInactiveSubIds()
  {
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      if (localISub != null) {
        localISub.clearDefaultsForInactiveSubIds();
      }
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void clearSubscriptionInfo()
  {
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      if (localISub != null) {
        localISub.clearSubInfo();
      }
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public Intent createManageSubscriptionIntent(int paramInt)
  {
    String str = getSubscriptionPlansOwner(paramInt);
    if (str == null) {
      return null;
    }
    if (getSubscriptionPlans(paramInt).isEmpty()) {
      return null;
    }
    Intent localIntent = new Intent("android.telephony.action.MANAGE_SUBSCRIPTION_PLANS");
    localIntent.setPackage(str);
    localIntent.putExtra("android.telephony.extra.SUBSCRIPTION_INDEX", paramInt);
    if (mContext.getPackageManager().queryIntentActivities(localIntent, 65536).isEmpty()) {
      return null;
    }
    return localIntent;
  }
  
  public List<SubscriptionInfo> getAccessibleSubscriptionInfoList()
  {
    Object localObject1 = null;
    List localList = null;
    Object localObject2;
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      if (localISub != null) {
        localList = localISub.getAccessibleSubscriptionInfoList(mContext.getOpPackageName());
      }
    }
    catch (RemoteException localRemoteException)
    {
      localObject2 = localObject1;
    }
    return localObject2;
  }
  
  public int[] getActiveSubscriptionIdList()
  {
    Object localObject1 = null;
    int[] arrayOfInt = null;
    Object localObject2;
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      if (localISub != null) {
        arrayOfInt = localISub.getActiveSubIdList();
      }
    }
    catch (RemoteException localRemoteException)
    {
      localObject2 = localObject1;
    }
    localObject1 = localObject2;
    if (localObject2 == null) {
      localObject1 = new int[0];
    }
    return localObject1;
  }
  
  public SubscriptionInfo getActiveSubscriptionInfo(int paramInt)
  {
    if (!isValidSubscriptionId(paramInt)) {
      return null;
    }
    Object localObject1 = null;
    SubscriptionInfo localSubscriptionInfo = null;
    Object localObject2;
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      if (localISub != null) {
        localSubscriptionInfo = localISub.getActiveSubscriptionInfo(paramInt, mContext.getOpPackageName());
      }
    }
    catch (RemoteException localRemoteException)
    {
      localObject2 = localObject1;
    }
    return localObject2;
  }
  
  public int getActiveSubscriptionInfoCount()
  {
    int i = 0;
    int j = 0;
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      if (localISub != null) {
        j = localISub.getActiveSubInfoCount(mContext.getOpPackageName());
      }
    }
    catch (RemoteException localRemoteException)
    {
      j = i;
    }
    return j;
  }
  
  public int getActiveSubscriptionInfoCountMax()
  {
    int i = 0;
    int j = 0;
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      if (localISub != null) {
        j = localISub.getActiveSubInfoCountMax();
      }
    }
    catch (RemoteException localRemoteException)
    {
      j = i;
    }
    return j;
  }
  
  public SubscriptionInfo getActiveSubscriptionInfoForIccIndex(String paramString)
  {
    Object localObject1 = null;
    Object localObject2 = null;
    if (paramString == null)
    {
      logd("[getActiveSubscriptionInfoForIccIndex]- null iccid");
      return null;
    }
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      if (localISub != null) {
        localObject2 = localISub.getActiveSubscriptionInfoForIccId(paramString, mContext.getOpPackageName());
      }
    }
    catch (RemoteException paramString)
    {
      localObject2 = localObject1;
    }
    return localObject2;
  }
  
  public SubscriptionInfo getActiveSubscriptionInfoForSimSlotIndex(int paramInt)
  {
    if (!isValidSlotIndex(paramInt))
    {
      logd("[getActiveSubscriptionInfoForSimSlotIndex]- invalid slotIndex");
      return null;
    }
    Object localObject1 = null;
    SubscriptionInfo localSubscriptionInfo = null;
    Object localObject2;
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      if (localISub != null) {
        localSubscriptionInfo = localISub.getActiveSubscriptionInfoForSimSlotIndex(paramInt, mContext.getOpPackageName());
      }
    }
    catch (RemoteException localRemoteException)
    {
      localObject2 = localObject1;
    }
    return localObject2;
  }
  
  public List<SubscriptionInfo> getActiveSubscriptionInfoList()
  {
    Object localObject1 = null;
    List localList = null;
    Object localObject2;
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      if (localISub != null) {
        localList = localISub.getActiveSubscriptionInfoList(mContext.getOpPackageName());
      }
    }
    catch (RemoteException localRemoteException)
    {
      localObject2 = localObject1;
    }
    return localObject2;
  }
  
  public int getAllSubscriptionInfoCount()
  {
    int i = 0;
    int j = 0;
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      if (localISub != null) {
        j = localISub.getAllSubInfoCount(mContext.getOpPackageName());
      }
    }
    catch (RemoteException localRemoteException)
    {
      j = i;
    }
    return j;
  }
  
  public List<SubscriptionInfo> getAllSubscriptionInfoList()
  {
    Object localObject1 = null;
    List localList = null;
    Object localObject2;
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      if (localISub != null) {
        localList = localISub.getAllSubInfoList(mContext.getOpPackageName());
      }
    }
    catch (RemoteException localRemoteException)
    {
      localObject2 = localObject1;
    }
    localObject1 = localObject2;
    if (localObject2 == null) {
      localObject1 = new ArrayList();
    }
    return localObject1;
  }
  
  @SystemApi
  public List<SubscriptionInfo> getAvailableSubscriptionInfoList()
  {
    Object localObject1 = null;
    List localList = null;
    Object localObject2;
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      if (localISub != null) {
        localList = localISub.getAvailableSubscriptionInfoList(mContext.getOpPackageName());
      }
    }
    catch (RemoteException localRemoteException)
    {
      localObject2 = localObject1;
    }
    return localObject2;
  }
  
  public int getDefaultDataPhoneId()
  {
    return getPhoneId(getDefaultDataSubscriptionId());
  }
  
  public SubscriptionInfo getDefaultDataSubscriptionInfo()
  {
    return getActiveSubscriptionInfo(getDefaultDataSubscriptionId());
  }
  
  public int getDefaultSmsPhoneId()
  {
    return getPhoneId(getDefaultSmsSubscriptionId());
  }
  
  public SubscriptionInfo getDefaultSmsSubscriptionInfo()
  {
    return getActiveSubscriptionInfo(getDefaultSmsSubscriptionId());
  }
  
  public SubscriptionInfo getDefaultVoiceSubscriptionInfo()
  {
    return getActiveSubscriptionInfo(getDefaultVoiceSubscriptionId());
  }
  
  @SystemApi
  public List<SubscriptionPlan> getSubscriptionPlans(int paramInt)
  {
    try
    {
      Object localObject = getNetworkPolicy().getSubscriptionPlans(paramInt, mContext.getOpPackageName());
      if (localObject == null) {
        localObject = Collections.emptyList();
      } else {
        localObject = Arrays.asList((Object[])localObject);
      }
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isActiveSubId(int paramInt)
  {
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      if (localISub != null)
      {
        boolean bool = localISub.isActiveSubId(paramInt);
        return bool;
      }
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean isNetworkRoaming(int paramInt)
  {
    if (getPhoneId(paramInt) < 0) {
      return false;
    }
    return TelephonyManager.getDefault().isNetworkRoaming(paramInt);
  }
  
  public boolean isSubscriptionPlansRefreshSupported(int paramInt)
  {
    boolean bool;
    if (createRefreshSubscriptionIntent(paramInt) != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void removeOnSubscriptionsChangedListener(OnSubscriptionsChangedListener paramOnSubscriptionsChangedListener)
  {
    String str;
    if (mContext != null) {
      str = mContext.getOpPackageName();
    } else {
      str = "<unknown>";
    }
    try
    {
      ITelephonyRegistry localITelephonyRegistry = ITelephonyRegistry.Stub.asInterface(ServiceManager.getService("telephony.registry"));
      if (localITelephonyRegistry != null) {
        localITelephonyRegistry.removeOnSubscriptionsChangedListener(str, callback);
      }
    }
    catch (RemoteException paramOnSubscriptionsChangedListener) {}
  }
  
  @SystemApi
  public void requestEmbeddedSubscriptionInfoListRefresh()
  {
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      if (localISub != null) {
        localISub.requestEmbeddedSubscriptionInfoListRefresh();
      }
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void requestSubscriptionPlansRefresh(int paramInt)
  {
    Intent localIntent = createRefreshSubscriptionIntent(paramInt);
    BroadcastOptions localBroadcastOptions = BroadcastOptions.makeBasic();
    localBroadcastOptions.setTemporaryAppWhitelistDuration(TimeUnit.MINUTES.toMillis(1L));
    mContext.sendBroadcast(localIntent, null, localBroadcastOptions.toBundle());
  }
  
  public int setDataRoaming(int paramInt1, int paramInt2)
  {
    if ((paramInt1 >= 0) && (isValidSubscriptionId(paramInt2)))
    {
      int i = 0;
      int j = 0;
      try
      {
        ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
        if (localISub != null) {
          j = localISub.setDataRoaming(paramInt1, paramInt2);
        }
      }
      catch (RemoteException localRemoteException)
      {
        j = i;
      }
      return j;
    }
    logd("[setDataRoaming]- fail");
    return -1;
  }
  
  public void setDefaultDataSubId(int paramInt)
  {
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      if (localISub != null) {
        localISub.setDefaultDataSubId(paramInt);
      }
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void setDefaultSmsSubId(int paramInt)
  {
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      if (localISub != null) {
        localISub.setDefaultSmsSubId(paramInt);
      }
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void setDefaultVoiceSubId(int paramInt)
  {
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      if (localISub != null) {
        localISub.setDefaultVoiceSubId(paramInt);
      }
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public int setDisplayName(String paramString, int paramInt)
  {
    return setDisplayName(paramString, paramInt, -1L);
  }
  
  public int setDisplayName(String paramString, int paramInt, long paramLong)
  {
    if (!isValidSubscriptionId(paramInt))
    {
      logd("[setDisplayName]- fail");
      return -1;
    }
    int i = 0;
    int j = 0;
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      if (localISub != null) {
        j = localISub.setDisplayNameUsingSrc(paramString, paramInt, paramLong);
      }
    }
    catch (RemoteException paramString)
    {
      j = i;
    }
    return j;
  }
  
  public int setDisplayNumber(String paramString, int paramInt)
  {
    if ((paramString != null) && (isValidSubscriptionId(paramInt)))
    {
      int i = 0;
      int j = 0;
      try
      {
        ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
        if (localISub != null) {
          j = localISub.setDisplayNumber(paramString, paramInt);
        }
      }
      catch (RemoteException paramString)
      {
        j = i;
      }
      return j;
    }
    logd("[setDisplayNumber]- fail");
    return -1;
  }
  
  public int setIconTint(int paramInt1, int paramInt2)
  {
    if (!isValidSubscriptionId(paramInt2))
    {
      logd("[setIconTint]- fail");
      return -1;
    }
    int i = 0;
    int j = 0;
    try
    {
      ISub localISub = ISub.Stub.asInterface(ServiceManager.getService("isub"));
      if (localISub != null) {
        j = localISub.setIconTint(paramInt1, paramInt2);
      }
    }
    catch (RemoteException localRemoteException)
    {
      j = i;
    }
    return j;
  }
  
  @SystemApi
  public void setSubscriptionOverrideCongested(int paramInt, boolean paramBoolean, long paramLong)
  {
    if (paramBoolean) {}
    for (int i = 2;; i = 0) {
      break;
    }
    try
    {
      getNetworkPolicy().setSubscriptionOverride(paramInt, 2, i, paramLong, mContext.getOpPackageName());
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void setSubscriptionOverrideUnmetered(int paramInt, boolean paramBoolean, long paramLong)
  {
    try
    {
      getNetworkPolicy().setSubscriptionOverride(paramInt, 1, paramBoolean, paramLong, mContext.getOpPackageName());
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void setSubscriptionPlans(int paramInt, List<SubscriptionPlan> paramList)
  {
    try
    {
      getNetworkPolicy().setSubscriptionPlans(paramInt, (SubscriptionPlan[])paramList.toArray(new SubscriptionPlan[paramList.size()]), mContext.getOpPackageName());
      return;
    }
    catch (RemoteException paramList)
    {
      throw paramList.rethrowFromSystemServer();
    }
  }
  
  public static class OnSubscriptionsChangedListener
  {
    IOnSubscriptionsChangedListener callback = new IOnSubscriptionsChangedListener.Stub()
    {
      public void onSubscriptionsChanged()
      {
        mHandler.sendEmptyMessage(0);
      }
    };
    private final Handler mHandler;
    
    public OnSubscriptionsChangedListener()
    {
      mHandler = new OnSubscriptionsChangedListenerHandler();
    }
    
    public OnSubscriptionsChangedListener(Looper paramLooper)
    {
      mHandler = new OnSubscriptionsChangedListenerHandler(paramLooper);
    }
    
    private void log(String paramString)
    {
      Rlog.d("SubscriptionManager", paramString);
    }
    
    public void onSubscriptionsChanged() {}
    
    private class OnSubscriptionsChangedListenerHandler
      extends Handler
    {
      OnSubscriptionsChangedListenerHandler() {}
      
      OnSubscriptionsChangedListenerHandler(Looper paramLooper)
      {
        super();
      }
      
      public void handleMessage(Message paramMessage)
      {
        onSubscriptionsChanged();
      }
    }
  }
}
