package com.android.internal.telephony;

import android.app.ActivityManager;
import android.app.IActivityManager;
import android.app.UserSwitchObserver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.IPackageManager;
import android.content.pm.IPackageManager.Stub;
import android.content.pm.UserInfo;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.IRemoteCallback;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.provider.Settings.SettingNotFoundException;
import android.service.euicc.EuiccProfileInfo;
import android.service.euicc.GetEuiccProfileInfoListResult;
import android.telephony.CarrierConfigManager;
import android.telephony.Rlog;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.UiccAccessRule;
import android.telephony.euicc.EuiccManager;
import android.text.TextUtils;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.telephony.euicc.EuiccController;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.IccUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionInfoUpdater
  extends Handler
{
  public static final String CURR_SUBID = "curr_subid";
  private static final int EVENT_GET_NETWORK_SELECTION_MODE_DONE = 2;
  private static final int EVENT_INVALID = -1;
  private static final int EVENT_REFRESH_EMBEDDED_SUBSCRIPTIONS = 12;
  private static final int EVENT_SIM_ABSENT = 4;
  private static final int EVENT_SIM_IMSI = 11;
  private static final int EVENT_SIM_IO_ERROR = 6;
  private static final int EVENT_SIM_LOADED = 3;
  private static final int EVENT_SIM_LOCKED = 5;
  private static final int EVENT_SIM_NOT_READY = 9;
  private static final int EVENT_SIM_READY = 10;
  private static final int EVENT_SIM_RESTRICTED = 8;
  private static final int EVENT_SIM_UNKNOWN = 7;
  private static final String ICCID_STRING_FOR_NO_SIM = "";
  private static final String LOG_TAG = "SubscriptionInfoUpdater";
  private static final int PROJECT_SIM_NUM = TelephonyManager.getDefault().getPhoneCount();
  public static final int SIM_CHANGED = -1;
  public static final int SIM_NEW = -2;
  public static final int SIM_NOT_CHANGE = 0;
  public static final int SIM_NOT_INSERT = -99;
  public static final int SIM_REPOSITION = -3;
  public static final int STATUS_NO_SIM_INSERTED = 0;
  public static final int STATUS_SIM1_INSERTED = 1;
  public static final int STATUS_SIM2_INSERTED = 2;
  public static final int STATUS_SIM3_INSERTED = 4;
  public static final int STATUS_SIM4_INSERTED = 8;
  private static Context mContext = null;
  protected static String[] mIccId = new String[PROJECT_SIM_NUM];
  private static int[] mInsertSimState = new int[PROJECT_SIM_NUM];
  private static Phone[] mPhone;
  private static int[] sSimApplicationState = new int[PROJECT_SIM_NUM];
  private static int[] sSimCardState = new int[PROJECT_SIM_NUM];
  private CarrierServiceBindHelper mCarrierServiceBindHelper;
  private int mCurrentlyActiveUserId;
  private EuiccManager mEuiccManager;
  private boolean[] mIsRecordLoaded = new boolean[PROJECT_SIM_NUM];
  private IPackageManager mPackageManager;
  private SubscriptionManager mSubscriptionManager = null;
  
  public SubscriptionInfoUpdater(Looper paramLooper, Context paramContext, Phone[] paramArrayOfPhone, CommandsInterface[] paramArrayOfCommandsInterface)
  {
    super(paramLooper);
    logd("Constructor invoked");
    mContext = paramContext;
    mPhone = paramArrayOfPhone;
    mSubscriptionManager = SubscriptionManager.from(mContext);
    mEuiccManager = ((EuiccManager)mContext.getSystemService("euicc"));
    mPackageManager = IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
    mCarrierServiceBindHelper = new CarrierServiceBindHelper(mContext);
    initializeCarrierApps();
    for (int i = 0; i < PROJECT_SIM_NUM; i++) {
      mIsRecordLoaded[i] = false;
    }
  }
  
  private void broadcastSimApplicationStateChanged(int paramInt1, int paramInt2)
  {
    if ((paramInt2 != sSimApplicationState[paramInt1]) && ((paramInt2 != 6) || (sSimApplicationState[paramInt1] != 0)))
    {
      sSimApplicationState[paramInt1] = paramInt2;
      Intent localIntent = new Intent("android.telephony.action.SIM_APPLICATION_STATE_CHANGED");
      localIntent.addFlags(16777216);
      localIntent.addFlags(67108864);
      localIntent.putExtra("android.telephony.extra.SIM_STATE", paramInt2);
      SubscriptionManager.putPhoneIdAndSubIdExtra(localIntent, paramInt1);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Broadcasting intent ACTION_SIM_APPLICATION_STATE_CHANGED ");
      localStringBuilder.append(simStateString(paramInt2));
      localStringBuilder.append(" for phone: ");
      localStringBuilder.append(paramInt1);
      logd(localStringBuilder.toString());
      mContext.sendBroadcast(localIntent, "android.permission.READ_PRIVILEGED_PHONE_STATE");
    }
  }
  
  private void broadcastSimCardStateChanged(int paramInt1, int paramInt2)
  {
    if (paramInt2 != sSimCardState[paramInt1])
    {
      sSimCardState[paramInt1] = paramInt2;
      Intent localIntent = new Intent("android.telephony.action.SIM_CARD_STATE_CHANGED");
      localIntent.addFlags(67108864);
      localIntent.addFlags(16777216);
      localIntent.putExtra("android.telephony.extra.SIM_STATE", paramInt2);
      SubscriptionManager.putPhoneIdAndSubIdExtra(localIntent, paramInt1);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Broadcasting intent ACTION_SIM_CARD_STATE_CHANGED ");
      localStringBuilder.append(simStateString(paramInt2));
      localStringBuilder.append(" for phone: ");
      localStringBuilder.append(paramInt1);
      logd(localStringBuilder.toString());
      mContext.sendBroadcast(localIntent, "android.permission.READ_PRIVILEGED_PHONE_STATE");
    }
  }
  
  private void broadcastSimStateChanged(int paramInt, String paramString1, String paramString2)
  {
    Intent localIntent = new Intent("android.intent.action.SIM_STATE_CHANGED");
    localIntent.addFlags(67108864);
    localIntent.putExtra("phoneName", "Phone");
    localIntent.putExtra("ss", paramString1);
    localIntent.putExtra("reason", paramString2);
    SubscriptionManager.putPhoneIdAndSubIdExtra(localIntent, paramInt);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Broadcasting intent ACTION_SIM_STATE_CHANGED ");
    localStringBuilder.append(paramString1);
    localStringBuilder.append(" reason ");
    localStringBuilder.append(paramString2);
    localStringBuilder.append(" for mCardIndex: ");
    localStringBuilder.append(paramInt);
    logd(localStringBuilder.toString());
    IntentBroadcaster.getInstance().broadcastStickyIntent(localIntent, paramInt);
  }
  
  private static int findSubscriptionInfoForIccid(List<SubscriptionInfo> paramList, String paramString)
  {
    for (int i = 0; i < paramList.size(); i++) {
      if (TextUtils.equals(paramString, ((SubscriptionInfo)paramList.get(i)).getIccId())) {
        return i;
      }
    }
    return -1;
  }
  
  private static int getSimStateFromLockedReason(String paramString)
  {
    int i = paramString.hashCode();
    if (i != -1733499378)
    {
      if (i != 79221)
      {
        if (i != 79590)
        {
          if ((i == 190660331) && (paramString.equals("PERM_DISABLED")))
          {
            i = 3;
            break label98;
          }
        }
        else if (paramString.equals("PUK"))
        {
          i = 1;
          break label98;
        }
      }
      else if (paramString.equals("PIN"))
      {
        i = 0;
        break label98;
      }
    }
    else if (paramString.equals("NETWORK"))
    {
      i = 2;
      break label98;
    }
    i = -1;
    switch (i)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unexpected SIM locked reason ");
      localStringBuilder.append(paramString);
      Rlog.e("SubscriptionInfoUpdater", localStringBuilder.toString());
      return 0;
    case 3: 
      return 7;
    case 2: 
      return 4;
    case 1: 
      label98:
      return 3;
    }
    return 2;
  }
  
  private void initializeCarrierApps()
  {
    mCurrentlyActiveUserId = 0;
    try
    {
      localObject = ActivityManager.getService();
      UserSwitchObserver local1 = new com/android/internal/telephony/SubscriptionInfoUpdater$1;
      local1.<init>(this);
      ((IActivityManager)localObject).registerUserSwitchObserver(local1, "SubscriptionInfoUpdater");
      mCurrentlyActiveUserId = getServicegetCurrentUserid;
    }
    catch (RemoteException localRemoteException)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Couldn't get current user ID; guessing it's 0: ");
      ((StringBuilder)localObject).append(localRemoteException.getMessage());
      logd(((StringBuilder)localObject).toString());
    }
    CarrierAppUtils.disableCarrierAppsUntilPrivileged(mContext.getOpPackageName(), mPackageManager, TelephonyManager.getDefault(), mContext.getContentResolver(), mCurrentlyActiveUserId);
  }
  
  private int internalIccStateToMessage(String paramString)
  {
    switch (paramString.hashCode())
    {
    default: 
      break;
    case 1924388665: 
      if (paramString.equals("ABSENT")) {
        i = 0;
      }
      break;
    case 1599753450: 
      if (paramString.equals("CARD_RESTRICTED")) {
        i = 3;
      }
      break;
    case 1034051831: 
      if (paramString.equals("NOT_READY")) {
        i = 4;
      }
      break;
    case 433141802: 
      if (paramString.equals("UNKNOWN")) {
        i = 1;
      }
      break;
    case 77848963: 
      if (paramString.equals("READY")) {
        i = 7;
      }
      break;
    case 2251386: 
      if (paramString.equals("IMSI")) {
        i = 8;
      }
      break;
    case -1830845986: 
      if (paramString.equals("CARD_IO_ERROR")) {
        i = 2;
      }
      break;
    case -2044123382: 
      if (paramString.equals("LOCKED")) {
        i = 5;
      }
      break;
    case -2044189691: 
      if (paramString.equals("LOADED")) {
        i = 6;
      }
      break;
    }
    int i = -1;
    switch (i)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Ignoring simStatus: ");
      localStringBuilder.append(paramString);
      logd(localStringBuilder.toString());
      return -1;
    case 8: 
      return 11;
    case 7: 
      return 10;
    case 6: 
      return 3;
    case 5: 
      return 5;
    case 4: 
      return 9;
    case 3: 
      return 8;
    case 2: 
      return 6;
    case 1: 
      return 7;
    }
    return 4;
  }
  
  private boolean isNewSim(String paramString1, String paramString2, String[] paramArrayOfString)
  {
    boolean bool1 = true;
    boolean bool2;
    for (int i = 0;; i++)
    {
      bool2 = bool1;
      if (i >= PROJECT_SIM_NUM) {
        break;
      }
      if (paramString1.equals(paramArrayOfString[i]))
      {
        bool2 = false;
        break;
      }
      if ((paramString2 != null) && (paramString2.equals(paramArrayOfString[i])))
      {
        bool2 = false;
        break;
      }
    }
    paramString1 = new StringBuilder();
    paramString1.append("newSim = ");
    paramString1.append(bool2);
    logd(paramString1.toString());
    return bool2;
  }
  
  private void logd(String paramString)
  {
    Rlog.d("SubscriptionInfoUpdater", paramString);
  }
  
  private static String simStateString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "INVALID";
    case 11: 
      return "PRESENT";
    case 10: 
      return "LOADED";
    case 9: 
      return "CARD_RESTRICTED";
    case 8: 
      return "CARD_IO_ERROR";
    case 7: 
      return "PERM_DISABLED";
    case 6: 
      return "NOT_READY";
    case 5: 
      return "READY";
    case 4: 
      return "NETWORK_LOCKED";
    case 3: 
      return "PUK_REQUIRED";
    case 2: 
      return "PIN_REQUIRED";
    case 1: 
      return "ABSENT";
    }
    return "UNKNOWN";
  }
  
  private void updateCarrierServices(int paramInt, String paramString)
  {
    ((CarrierConfigManager)mContext.getSystemService("carrier_config")).updateConfigForPhoneId(paramInt, paramString);
    mCarrierServiceBindHelper.updateForPhoneId(paramInt, paramString);
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter.println("SubscriptionInfoUpdater:");
    mCarrierServiceBindHelper.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
  }
  
  public void handleMessage(Message paramMessage)
  {
    Object localObject;
    switch (what)
    {
    default: 
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Unknown msg:");
      ((StringBuilder)localObject).append(what);
      logd(((StringBuilder)localObject).toString());
      break;
    case 11: 
      broadcastSimStateChanged(arg1, "IMSI", null);
      break;
    case 10: 
      broadcastSimStateChanged(arg1, "READY", null);
      broadcastSimCardStateChanged(arg1, 11);
      broadcastSimApplicationStateChanged(arg1, 6);
      break;
    case 9: 
      broadcastSimStateChanged(arg1, "NOT_READY", null);
      broadcastSimCardStateChanged(arg1, 11);
      broadcastSimApplicationStateChanged(arg1, 6);
    case 12: 
      if (updateEmbeddedSubscriptions()) {
        SubscriptionController.getInstance().notifySubscriptionInfoChanged();
      }
      if (obj != null) {
        ((Runnable)obj).run();
      }
      break;
    case 8: 
      updateCarrierServices(arg1, "CARD_RESTRICTED");
      broadcastSimStateChanged(arg1, "CARD_RESTRICTED", "CARD_RESTRICTED");
      broadcastSimCardStateChanged(arg1, 9);
      broadcastSimApplicationStateChanged(arg1, 6);
      break;
    case 7: 
      updateCarrierServices(arg1, "UNKNOWN");
      broadcastSimStateChanged(arg1, "UNKNOWN", null);
      broadcastSimCardStateChanged(arg1, 0);
      broadcastSimApplicationStateChanged(arg1, 0);
      break;
    case 6: 
      handleSimError(arg1);
      break;
    case 5: 
      handleSimLocked(arg1, (String)obj);
      break;
    case 4: 
      handleSimAbsent(arg1);
      break;
    case 3: 
      handleSimLoaded(arg1);
      break;
    case 2: 
      localObject = (AsyncResult)obj;
      paramMessage = (Integer)userObj;
      if ((exception == null) && (result != null))
      {
        if (((int[])result)[0] == 1) {
          mPhone[paramMessage.intValue()].setNetworkSelectionModeAutomatic(null);
        }
      }
      else {
        logd("EVENT_GET_NETWORK_SELECTION_MODE_DONE: error getting network mode.");
      }
      break;
    }
  }
  
  protected void handleSimAbsent(int paramInt)
  {
    if ((mIccId[paramInt] != null) && (!mIccId[paramInt].equals("")))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("SIM");
      localStringBuilder.append(paramInt + 1);
      localStringBuilder.append(" hot plug out or error.");
      logd(localStringBuilder.toString());
    }
    mIccId[paramInt] = "";
    if (isAllIccIdQueryDone()) {
      updateSubscriptionInfoByIccId();
    }
    if (paramInt == 0) {
      SystemProperties.set("gsm.sim1.present", "0");
    } else if (paramInt == 1) {
      SystemProperties.set("gsm.sim2.present", "0");
    }
    updateCarrierServices(paramInt, "ABSENT");
    broadcastSimStateChanged(paramInt, "ABSENT", null);
    broadcastSimCardStateChanged(paramInt, 1);
    broadcastSimApplicationStateChanged(paramInt, 6);
  }
  
  protected void handleSimError(int paramInt)
  {
    if ((mIccId[paramInt] != null) && (!mIccId[paramInt].equals("")))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("SIM");
      localStringBuilder.append(paramInt + 1);
      localStringBuilder.append(" Error ");
      logd(localStringBuilder.toString());
    }
    mIccId[paramInt] = "";
    if (isAllIccIdQueryDone()) {
      updateSubscriptionInfoByIccId();
    }
    updateCarrierServices(paramInt, "CARD_IO_ERROR");
    broadcastSimStateChanged(paramInt, "CARD_IO_ERROR", "CARD_IO_ERROR");
    broadcastSimCardStateChanged(paramInt, 8);
    broadcastSimApplicationStateChanged(paramInt, 6);
  }
  
  protected void handleSimLoaded(int paramInt)
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("handleSimLoaded: slotId: ");
    ((StringBuilder)localObject1).append(paramInt);
    logd(((StringBuilder)localObject1).toString());
    int i = paramInt;
    IccCard localIccCard = mPhone[paramInt].getIccCard();
    if (localIccCard == null)
    {
      logd("handleSimLoaded: IccCard null");
      return;
    }
    localObject1 = localIccCard.getIccRecords();
    if (localObject1 == null)
    {
      logd("handleSimLoaded: IccRecords null");
      return;
    }
    if (IccUtils.stripTrailingFs(((IccRecords)localObject1).getFullIccId()) == null)
    {
      logd("handleSimLoaded: IccID null");
      return;
    }
    mIccId[paramInt] = IccUtils.stripTrailingFs(((IccRecords)localObject1).getFullIccId());
    mIsRecordLoaded[paramInt] = true;
    int j = i;
    Object localObject2 = localIccCard;
    localObject2 = localObject1;
    if (isAllIccIdQueryDone())
    {
      updateSubscriptionInfoByIccId();
      int[] arrayOfInt = mSubscriptionManager.getActiveSubscriptionIdList();
      int k = arrayOfInt.length;
      boolean bool = false;
      int m = 0;
      paramInt = i;
      for (;;)
      {
        j = paramInt;
        localObject2 = localIccCard;
        localObject2 = localObject1;
        if (m >= k) {
          break;
        }
        int n = arrayOfInt[m];
        int i1 = SubscriptionController.getInstance().getPhoneId(n);
        localObject2 = mPhone[i1].getOperatorNumeric();
        if ((localObject2 != null) && (!TextUtils.isEmpty((CharSequence)localObject2)))
        {
          if (n == SubscriptionController.getInstance().getDefaultSubId()) {
            MccTable.updateMccMncConfiguration(mContext, (String)localObject2, bool);
          }
          SubscriptionController.getInstance().setMccMnc((String)localObject2, n);
        }
        else
        {
          logd("EVENT_RECORDS_LOADED Operator name is null");
        }
        localObject2 = TelephonyManager.getDefault();
        Object localObject3 = ((TelephonyManager)localObject2).getLine1Number(n);
        mContext.getContentResolver();
        if (localObject3 != null) {
          SubscriptionController.getInstance().setDisplayNumber((String)localObject3, n);
        }
        localObject3 = mSubscriptionManager.getActiveSubscriptionInfo(n);
        localObject2 = ((TelephonyManager)localObject2).getSimOperatorName(n);
        if ((localObject3 != null) && (((SubscriptionInfo)localObject3).getNameSource() != 2))
        {
          if (TextUtils.isEmpty((CharSequence)localObject2))
          {
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("SIM ");
            ((StringBuilder)localObject2).append(Integer.toString(i1 + 1));
            localObject2 = ((StringBuilder)localObject2).toString();
          }
          localObject3 = new StringBuilder();
          ((StringBuilder)localObject3).append("sim name = ");
          ((StringBuilder)localObject3).append((String)localObject2);
          logd(((StringBuilder)localObject3).toString());
          SubscriptionController.getInstance().setDisplayName((String)localObject2, n);
        }
        localObject2 = PreferenceManager.getDefaultSharedPreferences(mContext);
        localObject3 = new StringBuilder();
        ((StringBuilder)localObject3).append("curr_subid");
        ((StringBuilder)localObject3).append(i1);
        if (((SharedPreferences)localObject2).getInt(((StringBuilder)localObject3).toString(), -1) != n)
        {
          j = RILConstants.PREFERRED_NETWORK_MODE;
          try
          {
            i = TelephonyManager.getIntAtIndex(mContext.getContentResolver(), "preferred_network_mode", i1);
          }
          catch (Settings.SettingNotFoundException localSettingNotFoundException)
          {
            Rlog.e("SubscriptionInfoUpdater", "Settings Exception Reading Value At Index for Settings.Global.PREFERRED_NETWORK_MODE");
            i = j;
          }
          mPhone[i1].setPreferredNetworkType(i, null);
          mPhone[i1].getNetworkSelectionMode(obtainMessage(2, new Integer(i1)));
          localObject2 = ((SharedPreferences)localObject2).edit();
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("curr_subid");
          localStringBuilder.append(i1);
          ((SharedPreferences.Editor)localObject2).putInt(localStringBuilder.toString(), n);
          ((SharedPreferences.Editor)localObject2).apply();
        }
        CarrierAppUtils.disableCarrierAppsUntilPrivileged(mContext.getOpPackageName(), mPackageManager, TelephonyManager.getDefault(), mContext.getContentResolver(), mCurrentlyActiveUserId);
        if (mIsRecordLoaded[i1] == 1)
        {
          broadcastSimStateChanged(i1, "LOADED", null);
          broadcastSimCardStateChanged(i1, 11);
          broadcastSimApplicationStateChanged(i1, 10);
          updateCarrierServices(i1, "LOADED");
          mIsRecordLoaded[i1] = false;
        }
        m++;
        bool = false;
      }
    }
  }
  
  protected void handleSimLocked(int paramInt, String paramString)
  {
    Object localObject;
    if ((mIccId[paramInt] != null) && (mIccId[paramInt].equals("")))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("SIM");
      ((StringBuilder)localObject).append(paramInt + 1);
      ((StringBuilder)localObject).append(" hot plug in");
      logd(((StringBuilder)localObject).toString());
      mIccId[paramInt] = null;
    }
    String str = mIccId[paramInt];
    if (str == null)
    {
      localObject = mPhone[paramInt].getIccCard();
      if (localObject == null)
      {
        logd("handleSimLocked: IccCard null");
        return;
      }
      localObject = ((IccCard)localObject).getIccRecords();
      if (localObject == null)
      {
        logd("handleSimLocked: IccRecords null");
        return;
      }
      if (IccUtils.stripTrailingFs(((IccRecords)localObject).getFullIccId()) == null)
      {
        logd("handleSimLocked: IccID null");
        return;
      }
      mIccId[paramInt] = IccUtils.stripTrailingFs(((IccRecords)localObject).getFullIccId());
    }
    else
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("NOT Querying IccId its already set sIccid[");
      ((StringBuilder)localObject).append(paramInt);
      ((StringBuilder)localObject).append("]=");
      ((StringBuilder)localObject).append(str);
      logd(((StringBuilder)localObject).toString());
    }
    if (isAllIccIdQueryDone()) {
      updateSubscriptionInfoByIccId();
    }
    updateCarrierServices(paramInt, "LOCKED");
    broadcastSimStateChanged(paramInt, "LOCKED", paramString);
    broadcastSimCardStateChanged(paramInt, 11);
    broadcastSimApplicationStateChanged(paramInt, getSimStateFromLockedReason(paramString));
  }
  
  protected boolean isAllIccIdQueryDone()
  {
    for (int i = 0; i < PROJECT_SIM_NUM; i++) {
      if (mIccId[i] == null)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Wait for SIM");
        localStringBuilder.append(i + 1);
        localStringBuilder.append(" IccId");
        logd(localStringBuilder.toString());
        return false;
      }
    }
    logd("All IccIds query complete");
    return true;
  }
  
  void requestEmbeddedSubscriptionInfoListRefresh(Runnable paramRunnable)
  {
    sendMessage(obtainMessage(12, paramRunnable));
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PRIVATE)
  public boolean updateEmbeddedSubscriptions()
  {
    if (!mEuiccManager.isEnabled()) {
      return false;
    }
    Object localObject1 = EuiccController.get().blockingGetEuiccProfileInfoList();
    if (localObject1 == null) {
      return false;
    }
    Object localObject2;
    if (((GetEuiccProfileInfoListResult)localObject1).getResult() == 0)
    {
      localObject2 = ((GetEuiccProfileInfoListResult)localObject1).getProfiles();
      if ((localObject2 != null) && (((List)localObject2).size() != 0)) {
        localObject2 = (EuiccProfileInfo[])((List)localObject2).toArray(new EuiccProfileInfo[((List)localObject2).size()]);
      } else {
        localObject2 = new EuiccProfileInfo[0];
      }
    }
    else
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("updatedEmbeddedSubscriptions: error ");
      ((StringBuilder)localObject2).append(((GetEuiccProfileInfoListResult)localObject1).getResult());
      ((StringBuilder)localObject2).append(" listing profiles");
      logd(((StringBuilder)localObject2).toString());
      localObject2 = new EuiccProfileInfo[0];
    }
    boolean bool1 = ((GetEuiccProfileInfoListResult)localObject1).getIsRemovable();
    Object localObject3 = new String[localObject2.length];
    for (int i = 0; i < localObject2.length; i++) {
      localObject3[i] = localObject2[i].getIccid();
    }
    List localList = SubscriptionController.getInstance().getSubscriptionInfoListForEmbeddedSubscriptionUpdate((String[])localObject3, bool1);
    ContentResolver localContentResolver = mContext.getContentResolver();
    int j = localObject2.length;
    boolean bool2 = false;
    for (i = 0; i < j; i++)
    {
      Object localObject4 = localObject2[i];
      int k = findSubscriptionInfoForIccid(localList, localObject4.getIccid());
      if (k < 0) {
        SubscriptionController.getInstance().insertEmptySubInfoRecord(localObject4.getIccid(), -1);
      } else {
        localList.remove(k);
      }
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("is_embedded", Integer.valueOf(1));
      localObject3 = localObject4.getUiccAccessRules();
      k = 0;
      if ((localObject3 == null) || (((List)localObject3).size() == 0)) {
        k = 1;
      }
      if (k != 0) {
        localObject3 = null;
      } else {
        localObject3 = UiccAccessRule.encodeRules((UiccAccessRule[])((List)localObject3).toArray(new UiccAccessRule[((List)localObject3).size()]));
      }
      localContentValues.put("access_rules", (byte[])localObject3);
      localContentValues.put("is_removable", Boolean.valueOf(bool1));
      localContentValues.put("display_name", localObject4.getNickname());
      localContentValues.put("name_source", Integer.valueOf(2));
      bool2 = true;
      localObject3 = SubscriptionManager.CONTENT_URI;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("icc_id=\"");
      localStringBuilder.append(localObject4.getIccid());
      localStringBuilder.append("\"");
      localContentResolver.update((Uri)localObject3, localContentValues, localStringBuilder.toString(), null);
      SubscriptionController.getInstance().refreshCachedActiveSubscriptionInfoList();
    }
    if (!localList.isEmpty())
    {
      localObject2 = new ArrayList();
      for (i = 0; i < localList.size(); i++)
      {
        localObject3 = (SubscriptionInfo)localList.get(i);
        if (((SubscriptionInfo)localObject3).isEmbedded())
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("\"");
          ((StringBuilder)localObject1).append(((SubscriptionInfo)localObject3).getIccId());
          ((StringBuilder)localObject1).append("\"");
          ((List)localObject2).add(((StringBuilder)localObject1).toString());
        }
      }
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("icc_id IN (");
      ((StringBuilder)localObject1).append(TextUtils.join(",", (Iterable)localObject2));
      ((StringBuilder)localObject1).append(")");
      localObject1 = ((StringBuilder)localObject1).toString();
      localObject2 = new ContentValues();
      ((ContentValues)localObject2).put("is_embedded", Integer.valueOf(0));
      bool2 = true;
      localContentResolver.update(SubscriptionManager.CONTENT_URI, (ContentValues)localObject2, (String)localObject1, null);
      SubscriptionController.getInstance().refreshCachedActiveSubscriptionInfoList();
    }
    return bool2;
  }
  
  public void updateInternalIccState(String paramString1, String paramString2, int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("updateInternalIccState to simStatus ");
    localStringBuilder.append(paramString1);
    localStringBuilder.append(" reason ");
    localStringBuilder.append(paramString2);
    localStringBuilder.append(" slotId ");
    localStringBuilder.append(paramInt);
    logd(localStringBuilder.toString());
    int i = internalIccStateToMessage(paramString1);
    if (i != -1) {
      sendMessage(obtainMessage(i, paramInt, -1, paramString2));
    }
  }
  
  protected void updateSubscriptionInfoByIccId()
  {
    try
    {
      logd("updateSubscriptionInfoByIccId:+ Start");
      for (int i = 0; i < PROJECT_SIM_NUM; i++) {
        mInsertSimState[i] = 0;
      }
      int j = PROJECT_SIM_NUM;
      int k = 0;
      while (k < PROJECT_SIM_NUM)
      {
        i = j;
        if ("".equals(mIccId[k]))
        {
          i = j - 1;
          mInsertSimState[k] = -99;
        }
        k++;
        j = i;
      }
      Object localObject1 = new java/lang/StringBuilder;
      ((StringBuilder)localObject1).<init>();
      ((StringBuilder)localObject1).append("insertedSimCount = ");
      ((StringBuilder)localObject1).append(j);
      logd(((StringBuilder)localObject1).toString());
      if (SubscriptionController.getInstance().getActiveSubIdList().length > j) {
        SubscriptionController.getInstance().clearSubInfo();
      }
      int m;
      for (i = 0; i < PROJECT_SIM_NUM; i++) {
        if (mInsertSimState[i] != -99)
        {
          k = 2;
          j = i + 1;
          while (j < PROJECT_SIM_NUM)
          {
            m = k;
            if (mInsertSimState[j] == 0)
            {
              m = k;
              if (mIccId[i].equals(mIccId[j]))
              {
                mInsertSimState[i] = 1;
                mInsertSimState[j] = k;
                m = k + 1;
              }
            }
            j++;
            k = m;
          }
        }
      }
      localObject1 = mContext.getContentResolver();
      Object localObject3 = new String[PROJECT_SIM_NUM];
      Object localObject4 = new String[PROJECT_SIM_NUM];
      Object localObject5;
      Object localObject6;
      Object localObject7;
      for (i = 0; i < PROJECT_SIM_NUM; i++)
      {
        localObject3[i] = null;
        localObject5 = SubscriptionController.getInstance().getSubInfoUsingSlotIndexPrivileged(i, false);
        localObject4[i] = IccUtils.getDecimalSubstring(mIccId[i]);
        if ((localObject5 != null) && (((List)localObject5).size() > 0))
        {
          localObject3[i] = ((SubscriptionInfo)((List)localObject5).get(0)).getIccId();
          localObject6 = new java/lang/StringBuilder;
          ((StringBuilder)localObject6).<init>();
          ((StringBuilder)localObject6).append("updateSubscriptionInfoByIccId: oldSubId = ");
          ((StringBuilder)localObject6).append(((SubscriptionInfo)((List)localObject5).get(0)).getSubscriptionId());
          logd(((StringBuilder)localObject6).toString());
          if ((mInsertSimState[i] == 0) && (!mIccId[i].equals(localObject3[i])) && ((localObject4[i] == null) || (!localObject4[i].equals(localObject3[i])))) {
            mInsertSimState[i] = -1;
          }
          if (mInsertSimState[i] != 0)
          {
            localObject6 = new android/content/ContentValues;
            ((ContentValues)localObject6).<init>(1);
            ((ContentValues)localObject6).put("sim_id", Integer.valueOf(-1));
            Uri localUri = SubscriptionManager.CONTENT_URI;
            localObject7 = new java/lang/StringBuilder;
            ((StringBuilder)localObject7).<init>();
            ((StringBuilder)localObject7).append("_id=");
            ((StringBuilder)localObject7).append(Integer.toString(((SubscriptionInfo)((List)localObject5).get(0)).getSubscriptionId()));
            ((ContentResolver)localObject1).update(localUri, (ContentValues)localObject6, ((StringBuilder)localObject7).toString(), null);
            SubscriptionController.getInstance().refreshCachedActiveSubscriptionInfoList();
          }
        }
        else
        {
          if (mInsertSimState[i] == 0) {
            mInsertSimState[i] = -1;
          }
          localObject3[i] = "";
          localObject5 = new java/lang/StringBuilder;
          ((StringBuilder)localObject5).<init>();
          ((StringBuilder)localObject5).append("updateSubscriptionInfoByIccId: No SIM in slot ");
          ((StringBuilder)localObject5).append(i);
          ((StringBuilder)localObject5).append(" last time");
          logd(((StringBuilder)localObject5).toString());
        }
      }
      for (i = 0; i < PROJECT_SIM_NUM; i++)
      {
        localObject5 = new java/lang/StringBuilder;
        ((StringBuilder)localObject5).<init>();
        ((StringBuilder)localObject5).append("updateSubscriptionInfoByIccId: oldIccId[");
        ((StringBuilder)localObject5).append(i);
        ((StringBuilder)localObject5).append("] = ");
        ((StringBuilder)localObject5).append(localObject3[i]);
        ((StringBuilder)localObject5).append(", sIccId[");
        ((StringBuilder)localObject5).append(i);
        ((StringBuilder)localObject5).append("] = ");
        ((StringBuilder)localObject5).append(mIccId[i]);
        logd(((StringBuilder)localObject5).toString());
      }
      i = 0;
      int n = 0;
      j = 0;
      while (j < PROJECT_SIM_NUM)
      {
        if (mInsertSimState[j] == -99)
        {
          localObject5 = new java/lang/StringBuilder;
          ((StringBuilder)localObject5).<init>();
          ((StringBuilder)localObject5).append("updateSubscriptionInfoByIccId: No SIM inserted in slot ");
          ((StringBuilder)localObject5).append(j);
          ((StringBuilder)localObject5).append(" this time");
          logd(((StringBuilder)localObject5).toString());
          m = n;
          k = i;
        }
        else
        {
          if (mInsertSimState[j] > 0)
          {
            localObject5 = mSubscriptionManager;
            localObject6 = new java/lang/StringBuilder;
            ((StringBuilder)localObject6).<init>();
            ((StringBuilder)localObject6).append(mIccId[j]);
            ((StringBuilder)localObject6).append(Integer.toString(mInsertSimState[j]));
            ((SubscriptionManager)localObject5).addSubscriptionInfoRecord(((StringBuilder)localObject6).toString(), j);
            localObject5 = new java/lang/StringBuilder;
            ((StringBuilder)localObject5).<init>();
            ((StringBuilder)localObject5).append("SUB");
            ((StringBuilder)localObject5).append(j + 1);
            ((StringBuilder)localObject5).append(" has invalid IccId");
            logd(((StringBuilder)localObject5).toString());
          }
          else
          {
            localObject5 = new java/lang/StringBuilder;
            ((StringBuilder)localObject5).<init>();
            ((StringBuilder)localObject5).append("updateSubscriptionInfoByIccId: adding subscription info record: iccid: ");
            ((StringBuilder)localObject5).append(mIccId[j]);
            ((StringBuilder)localObject5).append("slot: ");
            ((StringBuilder)localObject5).append(j);
            logd(((StringBuilder)localObject5).toString());
            mSubscriptionManager.addSubscriptionInfoRecord(mIccId[j], j);
          }
          m = n;
          k = i;
          if (isNewSim(mIccId[j], localObject4[j], (String[])localObject3))
          {
            m = n + 1;
            switch (j)
            {
            default: 
              break;
            case 2: 
              i |= 0x4;
              break;
            case 1: 
              i |= 0x2;
              break;
            case 0: 
              i |= 0x1;
            }
            mInsertSimState[j] = -2;
            k = i;
          }
        }
        j++;
        n = m;
        i = k;
      }
      for (i = 0; i < PROJECT_SIM_NUM; i++)
      {
        if (mInsertSimState[i] == -1) {
          mInsertSimState[i] = -3;
        }
        localObject4 = new java/lang/StringBuilder;
        ((StringBuilder)localObject4).<init>();
        ((StringBuilder)localObject4).append("updateSubscriptionInfoByIccId: sInsertSimState[");
        ((StringBuilder)localObject4).append(i);
        ((StringBuilder)localObject4).append("] = ");
        ((StringBuilder)localObject4).append(mInsertSimState[i]);
        logd(((StringBuilder)localObject4).toString());
      }
      localObject4 = mSubscriptionManager.getActiveSubscriptionInfoList();
      if (localObject4 == null) {
        i = 0;
      } else {
        i = ((List)localObject4).size();
      }
      localObject3 = new java/lang/StringBuilder;
      ((StringBuilder)localObject3).<init>();
      ((StringBuilder)localObject3).append("updateSubscriptionInfoByIccId: nSubCount = ");
      ((StringBuilder)localObject3).append(i);
      logd(((StringBuilder)localObject3).toString());
      for (j = 0; j < i; j++)
      {
        localObject5 = (SubscriptionInfo)((List)localObject4).get(j);
        localObject6 = TelephonyManager.getDefault().getLine1Number(((SubscriptionInfo)localObject5).getSubscriptionId());
        if (localObject6 != null)
        {
          localObject3 = new android/content/ContentValues;
          ((ContentValues)localObject3).<init>(1);
          ((ContentValues)localObject3).put("number", (String)localObject6);
          localObject7 = SubscriptionManager.CONTENT_URI;
          localObject6 = new java/lang/StringBuilder;
          ((StringBuilder)localObject6).<init>();
          ((StringBuilder)localObject6).append("_id=");
          ((StringBuilder)localObject6).append(Integer.toString(((SubscriptionInfo)localObject5).getSubscriptionId()));
          ((ContentResolver)localObject1).update((Uri)localObject7, (ContentValues)localObject3, ((StringBuilder)localObject6).toString(), null);
          SubscriptionController.getInstance().refreshCachedActiveSubscriptionInfoList();
        }
      }
      k = SystemProperties.getInt("persist.asus.mobile_slot", 0);
      j = -1;
      localObject1 = mSubscriptionManager;
      localObject1 = SubscriptionManager.getSubId(k);
      i = j;
      if (localObject1 != null)
      {
        i = j;
        if (localObject1.length != 0) {
          i = localObject1[0];
        }
      }
      localObject1 = new java/lang/StringBuilder;
      ((StringBuilder)localObject1).<init>();
      ((StringBuilder)localObject1).append("updateSubscriptionInfoByIccId: asusSlotId = ");
      ((StringBuilder)localObject1).append(k);
      ((StringBuilder)localObject1).append(", DefaultDataPhoneId=");
      ((StringBuilder)localObject1).append(mSubscriptionManager.getDefaultDataPhoneId());
      ((StringBuilder)localObject1).append(",subId=");
      ((StringBuilder)localObject1).append(i);
      logd(((StringBuilder)localObject1).toString());
      mSubscriptionManager.setDefaultDataSubId(i);
      updateEmbeddedSubscriptions();
      SubscriptionController.getInstance().notifySubscriptionInfoChanged();
      logd("updateSubscriptionInfoByIccId:- SubscriptionInfo update complete");
      return;
    }
    finally {}
  }
}
