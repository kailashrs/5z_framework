package com.android.internal.telephony.uicc;

import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.Registrant;
import android.os.RegistrantList;
import android.preference.PreferenceManager;
import android.provider.Settings.Global;
import android.telephony.CarrierConfigManager;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.UiccAccessRule;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.LocalLog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccCardConstants.State;
import com.android.internal.telephony.MccTable;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.SubscriptionController;
import com.android.internal.telephony.cat.CatService;
import com.android.internal.telephony.uicc.euicc.EuiccCard;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UiccProfile
  extends IccCard
{
  protected static final boolean DBG = true;
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PRIVATE)
  public static final int EVENT_APP_READY = 3;
  private static final int EVENT_CARRIER_CONFIG_CHANGED = 14;
  private static final int EVENT_CARRIER_PRIVILEGES_LOADED = 13;
  private static final int EVENT_CLOSE_LOGICAL_CHANNEL_DONE = 9;
  private static final int EVENT_EID_READY = 6;
  private static final int EVENT_ICC_LOCKED = 2;
  private static final int EVENT_ICC_RECORD_EVENTS = 7;
  private static final int EVENT_NETWORK_LOCKED = 5;
  private static final int EVENT_OPEN_LOGICAL_CHANNEL_DONE = 8;
  private static final int EVENT_RADIO_OFF_OR_UNAVAILABLE = 1;
  private static final int EVENT_RECORDS_LOADED = 4;
  private static final int EVENT_SIM_IO_DONE = 12;
  private static final int EVENT_TRANSMIT_APDU_BASIC_CHANNEL_DONE = 11;
  private static final int EVENT_TRANSMIT_APDU_LOGICAL_CHANNEL_DONE = 10;
  protected static final String LOG_TAG = "UiccProfile";
  private static final String OPERATOR_BRAND_OVERRIDE_PREFIX = "operator_branding_";
  private static final boolean VDBG = false;
  private RegistrantList mCarrierPrivilegeRegistrants;
  private UiccCarrierPrivilegeRules mCarrierPrivilegeRules;
  private CatService mCatService;
  private int mCdmaSubscriptionAppIndex;
  private CommandsInterface mCi;
  private Context mContext;
  private int mCurrentAppType;
  private boolean mDisposed;
  private IccCardConstants.State mExternalState;
  private int mGsmUmtsSubscriptionAppIndex;
  @VisibleForTesting
  public final Handler mHandler;
  private IccRecords mIccRecords;
  private int mImsSubscriptionAppIndex;
  private final Object mLock;
  private RegistrantList mNetworkLockedRegistrants;
  private RegistrantList mOperatorBrandOverrideRegistrants;
  private final int mPhoneId;
  private final ContentObserver mProvisionCompleteContentObserver;
  private final BroadcastReceiver mReceiver;
  private TelephonyManager mTelephonyManager;
  private UiccCardApplication mUiccApplication;
  private UiccCardApplication[] mUiccApplications = new UiccCardApplication[8];
  private final UiccCard mUiccCard;
  private IccCardStatus.PinState mUniversalPinState;
  
  public UiccProfile(Context paramContext, CommandsInterface paramCommandsInterface, IccCardStatus paramIccCardStatus, int paramInt, UiccCard paramUiccCard, Object paramObject)
  {
    boolean bool = false;
    mDisposed = false;
    mCarrierPrivilegeRegistrants = new RegistrantList();
    mOperatorBrandOverrideRegistrants = new RegistrantList();
    mNetworkLockedRegistrants = new RegistrantList();
    mCurrentAppType = 1;
    mUiccApplication = null;
    mIccRecords = null;
    mExternalState = IccCardConstants.State.UNKNOWN;
    mProvisionCompleteContentObserver = new ContentObserver(new Handler())
    {
      public void onChange(boolean paramAnonymousBoolean)
      {
        mContext.getContentResolver().unregisterContentObserver(this);
        Iterator localIterator = UiccProfile.this.getUninstalledCarrierPackages().iterator();
        while (localIterator.hasNext())
        {
          String str = (String)localIterator.next();
          InstallCarrierAppUtils.showNotification(mContext, str);
          InstallCarrierAppUtils.registerPackageInstallReceiver(mContext);
        }
      }
    };
    mReceiver = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        if (paramAnonymousIntent.getAction().equals("android.telephony.action.CARRIER_CONFIG_CHANGED")) {
          mHandler.sendMessage(mHandler.obtainMessage(14));
        }
      }
    };
    mHandler = new Handler()
    {
      public void handleMessage(Message paramAnonymousMessage)
      {
        if ((mDisposed) && (what != 8) && (what != 9) && (what != 10) && (what != 11) && (what != 12))
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("handleMessage: Received ");
          ((StringBuilder)localObject1).append(what);
          ((StringBuilder)localObject1).append(" after dispose(); ignoring the message");
          UiccProfile.loge(((StringBuilder)localObject1).toString());
          return;
        }
        Object localObject2 = UiccProfile.this;
        Object localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("handleMessage: Received ");
        ((StringBuilder)localObject1).append(what);
        ((StringBuilder)localObject1).append(" for phoneId ");
        ((StringBuilder)localObject1).append(mPhoneId);
        ((UiccProfile)localObject2).loglocal(((StringBuilder)localObject1).toString());
        switch (what)
        {
        default: 
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("handleMessage: Unhandled message with number: ");
          ((StringBuilder)localObject1).append(what);
          UiccProfile.loge(((StringBuilder)localObject1).toString());
          break;
        case 14: 
          UiccProfile.this.handleCarrierNameOverride();
          break;
        case 13: 
          UiccProfile.this.onCarrierPrivilegesLoadedMessage();
          updateExternalState();
          break;
        case 8: 
        case 9: 
        case 10: 
        case 11: 
        case 12: 
          paramAnonymousMessage = (AsyncResult)obj;
          if (exception != null)
          {
            localObject1 = UiccProfile.this;
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("handleMessage: Exception ");
            ((StringBuilder)localObject2).append(exception);
            ((UiccProfile)localObject1).loglocal(((StringBuilder)localObject2).toString());
            localObject1 = new StringBuilder();
            ((StringBuilder)localObject1).append("handleMessage: Error in SIM access with exception");
            ((StringBuilder)localObject1).append(exception);
            UiccProfile.log(((StringBuilder)localObject1).toString());
          }
          AsyncResult.forMessage((Message)userObj, result, exception);
          ((Message)userObj).sendToTarget();
          break;
        case 7: 
          if ((mCurrentAppType == 1) && (mIccRecords != null)) {
            if (((Integer)obj).result).intValue() == 2) {
              mTelephonyManager.setSimOperatorNameForPhone(mPhoneId, mIccRecords.getServiceProviderName());
            }
          }
          break;
        case 5: 
          mNetworkLockedRegistrants.notifyRegistrants(new AsyncResult(null, Integer.valueOf(mUiccApplication.getPersoSubState().ordinal()), null));
        case 1: 
        case 2: 
        case 3: 
        case 4: 
        case 6: 
          updateExternalState();
        }
      }
    };
    log("Creating profile");
    mLock = paramObject;
    mUiccCard = paramUiccCard;
    mPhoneId = paramInt;
    paramUiccCard = PhoneFactory.getPhone(paramInt);
    if (paramUiccCard != null)
    {
      if (paramUiccCard.getPhoneType() == 1) {
        bool = true;
      }
      setCurrentAppType(bool);
    }
    if ((mUiccCard instanceof EuiccCard)) {
      ((EuiccCard)mUiccCard).registerForEidReady(mHandler, 6, null);
    }
    update(paramContext, paramCommandsInterface, paramIccCardStatus);
    paramCommandsInterface.registerForOffOrNotAvailable(mHandler, 1, null);
    resetProperties();
    paramCommandsInterface = new IntentFilter();
    paramCommandsInterface.addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
    paramContext.registerReceiver(mReceiver, paramCommandsInterface);
  }
  
  private boolean areReadyAppsRecordsLoaded()
  {
    UiccCardApplication[] arrayOfUiccCardApplication = mUiccApplications;
    int i = arrayOfUiccCardApplication.length;
    boolean bool = false;
    for (int j = 0; j < i; j++)
    {
      Object localObject = arrayOfUiccCardApplication[j];
      if ((localObject != null) && (isSupportedApplication((UiccCardApplication)localObject)) && (((UiccCardApplication)localObject).isReady()) && (!((UiccCardApplication)localObject).isAppIgnored()))
      {
        localObject = ((UiccCardApplication)localObject).getIccRecords();
        if ((localObject == null) || (!((IccRecords)localObject).isLoaded())) {
          return false;
        }
      }
    }
    if (mUiccApplication != null) {
      bool = true;
    }
    return bool;
  }
  
  private void checkAndUpdateIfAnyAppToBeIgnored()
  {
    boolean[] arrayOfBoolean = new boolean[IccCardApplicationStatus.AppType.APPTYPE_ISIM.ordinal() + 1];
    UiccCardApplication[] arrayOfUiccCardApplication = mUiccApplications;
    int i = arrayOfUiccCardApplication.length;
    int j = 0;
    UiccCardApplication localUiccCardApplication;
    for (int k = 0; k < i; k++)
    {
      localUiccCardApplication = arrayOfUiccCardApplication[k];
      if ((localUiccCardApplication != null) && (isSupportedApplication(localUiccCardApplication)) && (localUiccCardApplication.isReady())) {
        arrayOfBoolean[localUiccCardApplication.getType().ordinal()] = true;
      }
    }
    arrayOfUiccCardApplication = mUiccApplications;
    i = arrayOfUiccCardApplication.length;
    for (k = j; k < i; k++)
    {
      localUiccCardApplication = arrayOfUiccCardApplication[k];
      if ((localUiccCardApplication != null) && (isSupportedApplication(localUiccCardApplication)) && (!localUiccCardApplication.isReady()) && (arrayOfBoolean[localUiccCardApplication.getType().ordinal()] != 0)) {
        localUiccCardApplication.setAppIgnoreState(true);
      }
    }
  }
  
  private int checkIndexLocked(int paramInt, IccCardApplicationStatus.AppType paramAppType1, IccCardApplicationStatus.AppType paramAppType2)
  {
    if ((mUiccApplications != null) && (paramInt < mUiccApplications.length))
    {
      if (paramInt < 0) {
        return -1;
      }
      if ((mUiccApplications[paramInt].getType() != paramAppType1) && (mUiccApplications[paramInt].getType() != paramAppType2))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("App index ");
        localStringBuilder.append(paramInt);
        localStringBuilder.append(" is invalid since it's not ");
        localStringBuilder.append(paramAppType1);
        localStringBuilder.append(" and not ");
        localStringBuilder.append(paramAppType2);
        loge(localStringBuilder.toString());
        return -1;
      }
      return paramInt;
    }
    paramAppType1 = new StringBuilder();
    paramAppType1.append("App index ");
    paramAppType1.append(paramInt);
    paramAppType1.append(" is invalid since there are no applications");
    loge(paramAppType1.toString());
    return -1;
  }
  
  private void createAndUpdateCatServiceLocked()
  {
    if ((mUiccApplications.length > 0) && (mUiccApplications[0] != null))
    {
      if (mCatService == null) {
        mCatService = CatService.getInstance(mCi, mContext, this, mPhoneId);
      } else {
        mCatService.update(mCi, mContext, this);
      }
    }
    else
    {
      if (mCatService != null) {
        mCatService.dispose();
      }
      mCatService = null;
    }
  }
  
  private UiccCarrierPrivilegeRules getCarrierPrivilegeRules()
  {
    synchronized (mLock)
    {
      UiccCarrierPrivilegeRules localUiccCarrierPrivilegeRules = mCarrierPrivilegeRules;
      return localUiccCarrierPrivilegeRules;
    }
  }
  
  public static String getIccStateIntentString(IccCardConstants.State paramState)
  {
    switch (4.$SwitchMap$com$android$internal$telephony$IccCardConstants$State[paramState.ordinal()])
    {
    default: 
      return "UNKNOWN";
    case 10: 
      return "LOADED";
    case 9: 
      return "CARD_RESTRICTED";
    case 8: 
      return "CARD_IO_ERROR";
    case 7: 
      return "LOCKED";
    case 6: 
      return "NOT_READY";
    case 5: 
      return "READY";
    case 4: 
      return "LOCKED";
    case 3: 
      return "LOCKED";
    case 2: 
      return "LOCKED";
    }
    return "ABSENT";
  }
  
  public static String getIccStateReason(IccCardConstants.State paramState)
  {
    switch (4.$SwitchMap$com$android$internal$telephony$IccCardConstants$State[paramState.ordinal()])
    {
    case 5: 
    case 6: 
    default: 
      return null;
    case 9: 
      return "CARD_RESTRICTED";
    case 8: 
      return "CARD_IO_ERROR";
    case 7: 
      return "PERM_DISABLED";
    case 4: 
      return "NETWORK";
    case 3: 
      return "PUK";
    }
    return "PIN";
  }
  
  private Set<String> getUninstalledCarrierPackages()
  {
    Object localObject = Settings.Global.getString(mContext.getContentResolver(), "carrier_app_whitelist");
    if (TextUtils.isEmpty((CharSequence)localObject)) {
      return Collections.emptySet();
    }
    Map localMap = parseToCertificateToPackageMap((String)localObject);
    if (localMap.isEmpty()) {
      return Collections.emptySet();
    }
    ArraySet localArraySet = new ArraySet();
    localObject = mCarrierPrivilegeRules.getAccessRules().iterator();
    while (((Iterator)localObject).hasNext())
    {
      String str = (String)localMap.get(((UiccAccessRule)((Iterator)localObject).next()).getCertificateHexString().toUpperCase());
      if ((!TextUtils.isEmpty(str)) && (!isPackageInstalled(mContext, str))) {
        localArraySet.add(str);
      }
    }
    return localArraySet;
  }
  
  private void handleCarrierNameOverride()
  {
    Object localObject1 = SubscriptionController.getInstance();
    int i = ((SubscriptionController)localObject1).getSubIdUsingPhoneId(mPhoneId);
    if (i == -1)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("subId not valid for Phone ");
      ((StringBuilder)localObject1).append(mPhoneId);
      loge(((StringBuilder)localObject1).toString());
      return;
    }
    Object localObject2 = (CarrierConfigManager)mContext.getSystemService("carrier_config");
    if (localObject2 == null)
    {
      loge("Failed to load a Carrier Config");
      return;
    }
    localObject2 = ((CarrierConfigManager)localObject2).getConfigForSubId(i);
    boolean bool = ((PersistableBundle)localObject2).getBoolean("carrier_name_override_bool", false);
    localObject2 = ((PersistableBundle)localObject2).getString("carrier_name_string");
    if ((bool) || ((TextUtils.isEmpty(getServiceProviderName())) && (!TextUtils.isEmpty((CharSequence)localObject2))))
    {
      if (mIccRecords != null) {
        mIccRecords.setServiceProviderName((String)localObject2);
      }
      mTelephonyManager.setSimOperatorNameForPhone(mPhoneId, (String)localObject2);
      mOperatorBrandOverrideRegistrants.notifyRegistrants();
    }
    updateCarrierNameForSubscription((SubscriptionController)localObject1, i);
  }
  
  static boolean isPackageInstalled(Context paramContext, String paramString)
  {
    paramContext = paramContext.getPackageManager();
    try
    {
      paramContext.getPackageInfo(paramString, 1);
      paramContext = new java/lang/StringBuilder;
      paramContext.<init>();
      paramContext.append(paramString);
      paramContext.append(" is installed.");
      log(paramContext.toString());
      return true;
    }
    catch (PackageManager.NameNotFoundException paramContext)
    {
      paramContext = new StringBuilder();
      paramContext.append(paramString);
      paramContext.append(" is not installed.");
      log(paramContext.toString());
    }
    return false;
  }
  
  private boolean isSupportedApplication(UiccCardApplication paramUiccCardApplication)
  {
    return (paramUiccCardApplication.getType() == IccCardApplicationStatus.AppType.APPTYPE_USIM) || (paramUiccCardApplication.getType() == IccCardApplicationStatus.AppType.APPTYPE_CSIM) || (paramUiccCardApplication.getType() == IccCardApplicationStatus.AppType.APPTYPE_SIM) || (paramUiccCardApplication.getType() == IccCardApplicationStatus.AppType.APPTYPE_RUIM);
  }
  
  private static void log(String paramString)
  {
    Rlog.d("UiccProfile", paramString);
  }
  
  private static void loge(String paramString)
  {
    Rlog.e("UiccProfile", paramString);
  }
  
  private void loglocal(String paramString)
  {
    LocalLog localLocalLog = UiccController.sLocalLog;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("UiccProfile[");
    localStringBuilder.append(mPhoneId);
    localStringBuilder.append("]: ");
    localStringBuilder.append(paramString);
    localLocalLog.log(localStringBuilder.toString());
  }
  
  private void onCarrierPrivilegesLoadedMessage()
  {
    ??? = (UsageStatsManager)mContext.getSystemService("usagestats");
    if (??? != null) {
      ((UsageStatsManager)???).onCarrierPrivilegedAppsChanged();
    }
    InstallCarrierAppUtils.hideAllNotifications(mContext);
    InstallCarrierAppUtils.unregisterPackageInstallReceiver(mContext);
    synchronized (mLock)
    {
      mCarrierPrivilegeRegistrants.notifyRegistrants();
      Object localObject2 = mContext.getContentResolver();
      int i = 1;
      if (Settings.Global.getInt((ContentResolver)localObject2, "device_provisioned", 1) != 1) {
        i = 0;
      }
      if (i != 0)
      {
        localObject2 = getUninstalledCarrierPackages().iterator();
        while (((Iterator)localObject2).hasNext()) {
          promptInstallCarrierApp((String)((Iterator)localObject2).next());
        }
      }
      localObject2 = Settings.Global.getUriFor("device_provisioned");
      mContext.getContentResolver().registerContentObserver((Uri)localObject2, false, mProvisionCompleteContentObserver);
      return;
    }
  }
  
  @VisibleForTesting
  public static Map<String, String> parseToCertificateToPackageMap(String paramString)
  {
    Object localObject = Arrays.asList(paramString.split("\\s*;\\s*"));
    if (((List)localObject).isEmpty()) {
      return Collections.emptyMap();
    }
    paramString = new ArrayMap(((List)localObject).size());
    localObject = ((List)localObject).iterator();
    while (((Iterator)localObject).hasNext())
    {
      String[] arrayOfString = ((String)((Iterator)localObject).next()).split("\\s*:\\s*");
      if (arrayOfString.length == 2) {
        paramString.put(arrayOfString[0].toUpperCase(), arrayOfString[1]);
      } else {
        loge("Incorrect length of key-value pair in carrier app whitelist map.  Length should be exactly 2");
      }
    }
    return paramString;
  }
  
  private void promptInstallCarrierApp(String paramString)
  {
    paramString = InstallCarrierAppTrampolineActivity.get(mContext, paramString);
    mContext.startActivity(paramString);
  }
  
  private void registerAllAppEvents()
  {
    for (Object localObject : mUiccApplications) {
      if (localObject != null)
      {
        ((UiccCardApplication)localObject).registerForReady(mHandler, 3, null);
        localObject = ((UiccCardApplication)localObject).getIccRecords();
        if (localObject != null)
        {
          ((IccRecords)localObject).registerForRecordsLoaded(mHandler, 4, null);
          ((IccRecords)localObject).registerForRecordsEvents(mHandler, 7, null);
        }
      }
    }
  }
  
  private void registerCurrAppEvents()
  {
    if (mIccRecords != null)
    {
      mIccRecords.registerForLockedRecordsLoaded(mHandler, 2, null);
      mIccRecords.registerForNetworkLockedRecordsLoaded(mHandler, 5, null);
    }
  }
  
  private void sanitizeApplicationIndexesLocked()
  {
    mGsmUmtsSubscriptionAppIndex = checkIndexLocked(mGsmUmtsSubscriptionAppIndex, IccCardApplicationStatus.AppType.APPTYPE_SIM, IccCardApplicationStatus.AppType.APPTYPE_USIM);
    mCdmaSubscriptionAppIndex = checkIndexLocked(mCdmaSubscriptionAppIndex, IccCardApplicationStatus.AppType.APPTYPE_RUIM, IccCardApplicationStatus.AppType.APPTYPE_CSIM);
    mImsSubscriptionAppIndex = checkIndexLocked(mImsSubscriptionAppIndex, IccCardApplicationStatus.AppType.APPTYPE_ISIM, null);
  }
  
  private void setCurrentAppType(boolean paramBoolean)
  {
    Object localObject1 = mLock;
    if (paramBoolean) {
      try
      {
        mCurrentAppType = 1;
      }
      finally
      {
        break label31;
      }
    } else {
      mCurrentAppType = 2;
    }
    return;
    label31:
    throw localObject2;
  }
  
  private void setExternalState(IccCardConstants.State paramState)
  {
    setExternalState(paramState, false);
  }
  
  private void setExternalState(IccCardConstants.State paramState, boolean paramBoolean)
  {
    synchronized (mLock)
    {
      if (!SubscriptionManager.isValidSlotIndex(mPhoneId))
      {
        paramState = new java/lang/StringBuilder;
        paramState.<init>();
        paramState.append("setExternalState: mPhoneId=");
        paramState.append(mPhoneId);
        paramState.append(" is invalid; Return!!");
        loge(paramState.toString());
        return;
      }
      Object localObject2;
      if ((!paramBoolean) && (paramState == mExternalState))
      {
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("setExternalState: !override and newstate unchanged from ");
        ((StringBuilder)localObject2).append(paramState);
        log(((StringBuilder)localObject2).toString());
        return;
      }
      mExternalState = paramState;
      if ((mExternalState == IccCardConstants.State.LOADED) && (mIccRecords != null))
      {
        localObject2 = mIccRecords.getOperatorNumeric();
        paramState = new java/lang/StringBuilder;
        paramState.<init>();
        paramState.append("setExternalState: operator=");
        paramState.append((String)localObject2);
        paramState.append(" mPhoneId=");
        paramState.append(mPhoneId);
        log(paramState.toString());
        if (!TextUtils.isEmpty((CharSequence)localObject2))
        {
          mTelephonyManager.setSimOperatorNumericForPhone(mPhoneId, (String)localObject2);
          paramState = ((String)localObject2).substring(0, 3);
          if (paramState != null) {
            mTelephonyManager.setSimCountryIsoForPhone(mPhoneId, MccTable.countryCodeForMcc(Integer.parseInt(paramState)));
          } else {
            loge("setExternalState: state LOADED; Country code is null");
          }
        }
        else
        {
          loge("setExternalState: state LOADED; Operator name is null");
        }
      }
      paramState = new java/lang/StringBuilder;
      paramState.<init>();
      paramState.append("setExternalState: set mPhoneId=");
      paramState.append(mPhoneId);
      paramState.append(" mExternalState=");
      paramState.append(mExternalState);
      log(paramState.toString());
      mTelephonyManager.setSimStateForPhone(mPhoneId, getState().toString());
      UiccController.updateInternalIccState(getIccStateIntentString(mExternalState), getIccStateReason(mExternalState), mPhoneId);
      return;
    }
  }
  
  private void unregisterAllAppEvents()
  {
    for (Object localObject : mUiccApplications) {
      if (localObject != null)
      {
        ((UiccCardApplication)localObject).unregisterForReady(mHandler);
        localObject = ((UiccCardApplication)localObject).getIccRecords();
        if (localObject != null)
        {
          ((IccRecords)localObject).unregisterForRecordsLoaded(mHandler);
          ((IccRecords)localObject).unregisterForRecordsEvents(mHandler);
        }
      }
    }
  }
  
  private void unregisterCurrAppEvents()
  {
    if (mIccRecords != null)
    {
      mIccRecords.unregisterForLockedRecordsLoaded(mHandler);
      mIccRecords.unregisterForNetworkLockedRecordsLoaded(mHandler);
    }
  }
  
  private void updateCarrierNameForSubscription(SubscriptionController paramSubscriptionController, int paramInt)
  {
    Object localObject1 = paramSubscriptionController.getActiveSubscriptionInfo(paramInt, mContext.getOpPackageName());
    if ((localObject1 != null) && (((SubscriptionInfo)localObject1).getNameSource() != 2))
    {
      Object localObject2 = ((SubscriptionInfo)localObject1).getDisplayName();
      localObject1 = mTelephonyManager.getSimOperatorName(paramInt);
      if ((!TextUtils.isEmpty((CharSequence)localObject1)) && (!((String)localObject1).equals(localObject2)))
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("sim name[");
        ((StringBuilder)localObject2).append(mPhoneId);
        ((StringBuilder)localObject2).append("] = ");
        ((StringBuilder)localObject2).append((String)localObject1);
        log(((StringBuilder)localObject2).toString());
        paramSubscriptionController.setDisplayName((String)localObject1, paramInt);
      }
      return;
    }
  }
  
  private void updateIccAvailability(boolean paramBoolean)
  {
    Object localObject1 = mLock;
    IccRecords localIccRecords = null;
    try
    {
      Object localObject2 = getApplication(mCurrentAppType);
      Object localObject3 = new java/lang/StringBuilder;
      ((StringBuilder)localObject3).<init>();
      ((StringBuilder)localObject3).append("[ACDMA] newApp is : ");
      ((StringBuilder)localObject3).append(localObject2);
      ((StringBuilder)localObject3).append(", mCurrentAppType: ");
      ((StringBuilder)localObject3).append(mCurrentAppType);
      log(((StringBuilder)localObject3).toString());
      localObject3 = localObject2;
      if (localObject2 == null)
      {
        localObject3 = getApplication(mCurrentAppType ^ 0x3);
        if (localObject3 != null)
        {
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("[ACDMA] appType: ");
          ((StringBuilder)localObject2).append(((UiccCardApplication)localObject3).getType());
          ((StringBuilder)localObject2).append(", appState: ");
          ((StringBuilder)localObject2).append(((UiccCardApplication)localObject3).getState());
          ((StringBuilder)localObject2).append(", mCurrentAppType: ");
          ((StringBuilder)localObject2).append(mCurrentAppType);
          ((StringBuilder)localObject2).append(", mPhoneId: ");
          ((StringBuilder)localObject2).append(mPhoneId);
          log(((StringBuilder)localObject2).toString());
        }
        else
        {
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("[ACDMA] newApp is null and mCurrentAppType: ");
          ((StringBuilder)localObject2).append(mCurrentAppType);
          ((StringBuilder)localObject2).append(", mPhoneId: ");
          ((StringBuilder)localObject2).append(mPhoneId);
          log(((StringBuilder)localObject2).toString());
        }
      }
      if (localObject3 != null) {
        localIccRecords = ((UiccCardApplication)localObject3).getIccRecords();
      }
      if (paramBoolean)
      {
        unregisterAllAppEvents();
        registerAllAppEvents();
      }
      if ((mIccRecords != localIccRecords) || (mUiccApplication != localObject3))
      {
        log("Icc changed. Reregistering.");
        unregisterCurrAppEvents();
        mUiccApplication = ((UiccCardApplication)localObject3);
        mIccRecords = localIccRecords;
        registerCurrAppEvents();
      }
      updateExternalState();
      return;
    }
    finally {}
  }
  
  public boolean areCarrierPriviligeRulesLoaded()
  {
    UiccCarrierPrivilegeRules localUiccCarrierPrivilegeRules = getCarrierPrivilegeRules();
    boolean bool;
    if ((localUiccCarrierPrivilegeRules != null) && (!localUiccCarrierPrivilegeRules.areCarrierPriviligeRulesLoaded())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public void changeIccFdnPassword(String paramString1, String paramString2, Message paramMessage)
  {
    synchronized (mLock)
    {
      if (mUiccApplication != null)
      {
        mUiccApplication.changeIccFdnPassword(paramString1, paramString2, paramMessage);
      }
      else if (paramMessage != null)
      {
        paramString1 = new java/lang/RuntimeException;
        paramString1.<init>("ICC card is absent.");
        forMessageexception = paramString1;
        paramMessage.sendToTarget();
        return;
      }
      return;
    }
  }
  
  public void changeIccLockPassword(String paramString1, String paramString2, Message paramMessage)
  {
    synchronized (mLock)
    {
      if (mUiccApplication != null)
      {
        mUiccApplication.changeIccLockPassword(paramString1, paramString2, paramMessage);
      }
      else if (paramMessage != null)
      {
        paramString1 = new java/lang/RuntimeException;
        paramString1.<init>("ICC card is absent.");
        forMessageexception = paramString1;
        paramMessage.sendToTarget();
        return;
      }
      return;
    }
  }
  
  public void dispose()
  {
    log("Disposing profile");
    if ((mUiccCard instanceof EuiccCard)) {
      ((EuiccCard)mUiccCard).unregisterForEidReady(mHandler);
    }
    synchronized (mLock)
    {
      unregisterAllAppEvents();
      unregisterCurrAppEvents();
      InstallCarrierAppUtils.hideAllNotifications(mContext);
      InstallCarrierAppUtils.unregisterPackageInstallReceiver(mContext);
      mCi.unregisterForOffOrNotAvailable(mHandler);
      mContext.unregisterReceiver(mReceiver);
      if (mCatService != null) {
        mCatService.dispose();
      }
      for (UiccCardApplication localUiccCardApplication : mUiccApplications) {
        if (localUiccCardApplication != null) {
          localUiccCardApplication.dispose();
        }
      }
      mCatService = null;
      mUiccApplications = null;
      mCarrierPrivilegeRules = null;
      mDisposed = true;
      return;
    }
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter.println("UiccProfile:");
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mCi=");
    ((StringBuilder)localObject).append(mCi);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mCatService=");
    ((StringBuilder)localObject).append(mCatService);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    int i = 0;
    for (int j = 0; j < mCarrierPrivilegeRegistrants.size(); j++)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("  mCarrierPrivilegeRegistrants[");
      ((StringBuilder)localObject).append(j);
      ((StringBuilder)localObject).append("]=");
      ((StringBuilder)localObject).append(((Registrant)mCarrierPrivilegeRegistrants.get(j)).getHandler());
      paramPrintWriter.println(((StringBuilder)localObject).toString());
    }
    for (j = 0; j < mOperatorBrandOverrideRegistrants.size(); j++)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("  mOperatorBrandOverrideRegistrants[");
      ((StringBuilder)localObject).append(j);
      ((StringBuilder)localObject).append("]=");
      ((StringBuilder)localObject).append(((Registrant)mOperatorBrandOverrideRegistrants.get(j)).getHandler());
      paramPrintWriter.println(((StringBuilder)localObject).toString());
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mUniversalPinState=");
    ((StringBuilder)localObject).append(mUniversalPinState);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mGsmUmtsSubscriptionAppIndex=");
    ((StringBuilder)localObject).append(mGsmUmtsSubscriptionAppIndex);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mCdmaSubscriptionAppIndex=");
    ((StringBuilder)localObject).append(mCdmaSubscriptionAppIndex);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mImsSubscriptionAppIndex=");
    ((StringBuilder)localObject).append(mImsSubscriptionAppIndex);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mUiccApplications: length=");
    ((StringBuilder)localObject).append(mUiccApplications.length);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    for (j = 0; j < mUiccApplications.length; j++) {
      if (mUiccApplications[j] == null)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("  mUiccApplications[");
        ((StringBuilder)localObject).append(j);
        ((StringBuilder)localObject).append("]=");
        ((StringBuilder)localObject).append(null);
        paramPrintWriter.println(((StringBuilder)localObject).toString());
      }
      else
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("  mUiccApplications[");
        ((StringBuilder)localObject).append(j);
        ((StringBuilder)localObject).append("]=");
        ((StringBuilder)localObject).append(mUiccApplications[j].getType());
        ((StringBuilder)localObject).append(" ");
        ((StringBuilder)localObject).append(mUiccApplications[j]);
        paramPrintWriter.println(((StringBuilder)localObject).toString());
      }
    }
    paramPrintWriter.println();
    IccRecords localIccRecords;
    for (localIccRecords : mUiccApplications) {
      if (localIccRecords != null)
      {
        localIccRecords.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
        paramPrintWriter.println();
      }
    }
    for (localIccRecords : mUiccApplications) {
      if (localIccRecords != null)
      {
        localIccRecords = localIccRecords.getIccRecords();
        if (localIccRecords != null)
        {
          localIccRecords.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
          paramPrintWriter.println();
        }
      }
    }
    if (mCarrierPrivilegeRules == null)
    {
      paramPrintWriter.println(" mCarrierPrivilegeRules: null");
    }
    else
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(" mCarrierPrivilegeRules: ");
      ((StringBuilder)localObject).append(mCarrierPrivilegeRules);
      paramPrintWriter.println(((StringBuilder)localObject).toString());
      mCarrierPrivilegeRules.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    }
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mCarrierPrivilegeRegistrants: size=");
    paramFileDescriptor.append(mCarrierPrivilegeRegistrants.size());
    paramPrintWriter.println(paramFileDescriptor.toString());
    for (j = 0; j < mCarrierPrivilegeRegistrants.size(); j++)
    {
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append("  mCarrierPrivilegeRegistrants[");
      paramFileDescriptor.append(j);
      paramFileDescriptor.append("]=");
      paramFileDescriptor.append(((Registrant)mCarrierPrivilegeRegistrants.get(j)).getHandler());
      paramPrintWriter.println(paramFileDescriptor.toString());
    }
    paramPrintWriter.flush();
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mNetworkLockedRegistrants: size=");
    paramFileDescriptor.append(mNetworkLockedRegistrants.size());
    paramPrintWriter.println(paramFileDescriptor.toString());
    for (j = i; j < mNetworkLockedRegistrants.size(); j++)
    {
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append("  mNetworkLockedRegistrants[");
      paramFileDescriptor.append(j);
      paramFileDescriptor.append("]=");
      paramFileDescriptor.append(((Registrant)mNetworkLockedRegistrants.get(j)).getHandler());
      paramPrintWriter.println(paramFileDescriptor.toString());
    }
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mCurrentAppType=");
    paramFileDescriptor.append(mCurrentAppType);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mUiccCard=");
    paramFileDescriptor.append(mUiccCard);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mUiccApplication=");
    paramFileDescriptor.append(mUiccApplication);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mIccRecords=");
    paramFileDescriptor.append(mIccRecords);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mExternalState=");
    paramFileDescriptor.append(mExternalState);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramPrintWriter.flush();
  }
  
  protected void finalize()
  {
    log("UiccProfile finalized");
  }
  
  public UiccCardApplication getApplication(int paramInt)
  {
    Object localObject1 = mLock;
    int i = 8;
    switch (paramInt)
    {
    default: 
      paramInt = i;
      break;
    case 3: 
    case 2: 
    case 1: 
      try
      {
        paramInt = mImsSubscriptionAppIndex;
      }
      finally
      {
        break label100;
      }
      paramInt = mCdmaSubscriptionAppIndex;
      break;
      paramInt = mGsmUmtsSubscriptionAppIndex;
      break;
    }
    UiccCardApplication localUiccCardApplication;
    if ((paramInt >= 0) && (paramInt < mUiccApplications.length))
    {
      localUiccCardApplication = mUiccApplications[paramInt];
      return localUiccCardApplication;
    }
    return null;
    label100:
    throw localUiccCardApplication;
  }
  
  public UiccCardApplication getApplicationByType(int paramInt)
  {
    Object localObject1 = mLock;
    int i = 0;
    try
    {
      while (i < mUiccApplications.length)
      {
        if ((mUiccApplications[i] != null) && (mUiccApplications[i].getType().ordinal() == paramInt))
        {
          UiccCardApplication localUiccCardApplication = mUiccApplications[i];
          return localUiccCardApplication;
        }
        i++;
      }
      return null;
    }
    finally {}
  }
  
  public UiccCardApplication getApplicationIndex(int paramInt)
  {
    Object localObject1 = mLock;
    if (paramInt >= 0) {
      try
      {
        if (paramInt < mUiccApplications.length)
        {
          UiccCardApplication localUiccCardApplication = mUiccApplications[paramInt];
          return localUiccCardApplication;
        }
      }
      finally
      {
        break label39;
      }
    }
    return null;
    label39:
    throw localObject2;
  }
  
  public List<String> getCarrierPackageNamesForIntent(PackageManager paramPackageManager, Intent paramIntent)
  {
    UiccCarrierPrivilegeRules localUiccCarrierPrivilegeRules = getCarrierPrivilegeRules();
    if (localUiccCarrierPrivilegeRules == null) {
      paramPackageManager = null;
    } else {
      paramPackageManager = localUiccCarrierPrivilegeRules.getCarrierPackageNamesForIntent(paramPackageManager, paramIntent);
    }
    return paramPackageManager;
  }
  
  public int getCarrierPrivilegeStatus(PackageInfo paramPackageInfo)
  {
    UiccCarrierPrivilegeRules localUiccCarrierPrivilegeRules = getCarrierPrivilegeRules();
    int i;
    if (localUiccCarrierPrivilegeRules == null) {
      i = -1;
    } else {
      i = localUiccCarrierPrivilegeRules.getCarrierPrivilegeStatus(paramPackageInfo);
    }
    return i;
  }
  
  public int getCarrierPrivilegeStatus(PackageManager paramPackageManager, String paramString)
  {
    UiccCarrierPrivilegeRules localUiccCarrierPrivilegeRules = getCarrierPrivilegeRules();
    int i;
    if (localUiccCarrierPrivilegeRules == null) {
      i = -1;
    } else {
      i = localUiccCarrierPrivilegeRules.getCarrierPrivilegeStatus(paramPackageManager, paramString);
    }
    return i;
  }
  
  public int getCarrierPrivilegeStatus(Signature paramSignature, String paramString)
  {
    UiccCarrierPrivilegeRules localUiccCarrierPrivilegeRules = getCarrierPrivilegeRules();
    int i;
    if (localUiccCarrierPrivilegeRules == null) {
      i = -1;
    } else {
      i = localUiccCarrierPrivilegeRules.getCarrierPrivilegeStatus(paramSignature, paramString);
    }
    return i;
  }
  
  public int getCarrierPrivilegeStatusForCurrentTransaction(PackageManager paramPackageManager)
  {
    UiccCarrierPrivilegeRules localUiccCarrierPrivilegeRules = getCarrierPrivilegeRules();
    int i;
    if (localUiccCarrierPrivilegeRules == null) {
      i = -1;
    } else {
      i = localUiccCarrierPrivilegeRules.getCarrierPrivilegeStatusForCurrentTransaction(paramPackageManager);
    }
    return i;
  }
  
  public int getCarrierPrivilegeStatusForUid(PackageManager paramPackageManager, int paramInt)
  {
    UiccCarrierPrivilegeRules localUiccCarrierPrivilegeRules = getCarrierPrivilegeRules();
    if (localUiccCarrierPrivilegeRules == null) {
      paramInt = -1;
    } else {
      paramInt = localUiccCarrierPrivilegeRules.getCarrierPrivilegeStatusForUid(paramPackageManager, paramInt);
    }
    return paramInt;
  }
  
  public boolean getIccFdnEnabled()
  {
    synchronized (mLock)
    {
      boolean bool;
      if ((mUiccApplication != null) && (mUiccApplication.getIccFdnEnabled())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
  }
  
  public String getIccId()
  {
    for (Object localObject : mUiccApplications) {
      if (localObject != null)
      {
        localObject = ((UiccCardApplication)localObject).getIccRecords();
        if ((localObject != null) && (((IccRecords)localObject).getIccId() != null)) {
          return ((IccRecords)localObject).getIccId();
        }
      }
    }
    return null;
  }
  
  public boolean getIccLockEnabled()
  {
    synchronized (mLock)
    {
      boolean bool;
      if ((mUiccApplication != null) && (mUiccApplication.getIccLockEnabled())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
  }
  
  public boolean getIccPin2Blocked()
  {
    boolean bool;
    if ((mUiccApplication != null) && (mUiccApplication.getIccPin2Blocked())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean getIccPuk2Blocked()
  {
    boolean bool;
    if ((mUiccApplication != null) && (mUiccApplication.getIccPuk2Blocked())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public IccRecords getIccRecords()
  {
    synchronized (mLock)
    {
      IccRecords localIccRecords = mIccRecords;
      return localIccRecords;
    }
  }
  
  public boolean getIccRecordsLoaded()
  {
    synchronized (mLock)
    {
      if (mIccRecords != null)
      {
        boolean bool = mIccRecords.getRecordsLoaded();
        return bool;
      }
      return false;
    }
  }
  
  public int getNumApplications()
  {
    int i = 0;
    UiccCardApplication[] arrayOfUiccCardApplication = mUiccApplications;
    int j = arrayOfUiccCardApplication.length;
    int k = 0;
    while (k < j)
    {
      int m = i;
      if (arrayOfUiccCardApplication[k] != null) {
        m = i + 1;
      }
      k++;
      i = m;
    }
    return i;
  }
  
  public String getOperatorBrandOverride()
  {
    String str = getIccId();
    if (TextUtils.isEmpty(str)) {
      return null;
    }
    SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("operator_branding_");
    localStringBuilder.append(str);
    return localSharedPreferences.getString(localStringBuilder.toString(), null);
  }
  
  public int getPhoneId()
  {
    return mPhoneId;
  }
  
  public String getServiceProviderName()
  {
    synchronized (mLock)
    {
      if (mIccRecords != null)
      {
        String str = mIccRecords.getServiceProviderName();
        return str;
      }
      return null;
    }
  }
  
  public IccCardConstants.State getState()
  {
    synchronized (mLock)
    {
      IccCardConstants.State localState = mExternalState;
      return localState;
    }
  }
  
  public IccCardStatus.PinState getUniversalPinState()
  {
    synchronized (mLock)
    {
      IccCardStatus.PinState localPinState = mUniversalPinState;
      return localPinState;
    }
  }
  
  public boolean hasCarrierPrivilegeRules()
  {
    UiccCarrierPrivilegeRules localUiccCarrierPrivilegeRules = getCarrierPrivilegeRules();
    boolean bool;
    if ((localUiccCarrierPrivilegeRules != null) && (localUiccCarrierPrivilegeRules.hasCarrierPrivilegeRules())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasIccCard()
  {
    if (mUiccCard.getCardState() != IccCardStatus.CardState.CARDSTATE_ABSENT) {
      return true;
    }
    loge("hasIccCard: UiccProfile is not null but UiccCard is null or card state is ABSENT");
    return false;
  }
  
  public void iccCloseLogicalChannel(int paramInt, Message paramMessage)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("iccCloseLogicalChannel: ");
    localStringBuilder.append(paramInt);
    loglocal(localStringBuilder.toString());
    mCi.iccCloseLogicalChannel(paramInt, mHandler.obtainMessage(9, paramMessage));
  }
  
  public void iccExchangeSimIO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, String paramString, Message paramMessage)
  {
    mCi.iccIO(paramInt2, paramInt1, paramString, paramInt3, paramInt4, paramInt5, null, null, mHandler.obtainMessage(12, paramMessage));
  }
  
  public void iccOpenLogicalChannel(String paramString, int paramInt, Message paramMessage)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("iccOpenLogicalChannel: ");
    localStringBuilder.append(paramString);
    localStringBuilder.append(" , ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append(" by pid:");
    localStringBuilder.append(Binder.getCallingPid());
    localStringBuilder.append(" uid:");
    localStringBuilder.append(Binder.getCallingUid());
    loglocal(localStringBuilder.toString());
    mCi.iccOpenLogicalChannel(paramString, paramInt, mHandler.obtainMessage(8, paramMessage));
  }
  
  public void iccTransmitApduBasicChannel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, String paramString, Message paramMessage)
  {
    mCi.iccTransmitApduBasicChannel(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramString, mHandler.obtainMessage(11, paramMessage));
  }
  
  public void iccTransmitApduLogicalChannel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, String paramString, Message paramMessage)
  {
    mCi.iccTransmitApduLogicalChannel(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramString, mHandler.obtainMessage(10, paramMessage));
  }
  
  public boolean isApplicationOnIcc(IccCardApplicationStatus.AppType paramAppType)
  {
    Object localObject = mLock;
    int i = 0;
    try
    {
      while (i < mUiccApplications.length)
      {
        if ((mUiccApplications[i] != null) && (mUiccApplications[i].getType() == paramAppType)) {
          return true;
        }
        i++;
      }
      return false;
    }
    finally {}
  }
  
  @VisibleForTesting
  public void refresh()
  {
    mHandler.sendMessage(mHandler.obtainMessage(13));
  }
  
  public void registerForCarrierPrivilegeRulesLoaded(Handler paramHandler, int paramInt, Object paramObject)
  {
    synchronized (mLock)
    {
      Registrant localRegistrant = new android/os/Registrant;
      localRegistrant.<init>(paramHandler, paramInt, paramObject);
      mCarrierPrivilegeRegistrants.add(localRegistrant);
      if (areCarrierPriviligeRulesLoaded()) {
        localRegistrant.notifyRegistrant();
      }
      return;
    }
  }
  
  public void registerForNetworkLocked(Handler paramHandler, int paramInt, Object paramObject)
  {
    synchronized (mLock)
    {
      Registrant localRegistrant = new android/os/Registrant;
      localRegistrant.<init>(paramHandler, paramInt, paramObject);
      mNetworkLockedRegistrants.add(localRegistrant);
      if (getState() == IccCardConstants.State.NETWORK_LOCKED)
      {
        paramHandler = new android/os/AsyncResult;
        paramHandler.<init>(null, Integer.valueOf(mUiccApplication.getPersoSubState().ordinal()), null);
        localRegistrant.notifyRegistrant(paramHandler);
      }
      return;
    }
  }
  
  public void registerForOpertorBrandOverride(Handler paramHandler, int paramInt, Object paramObject)
  {
    synchronized (mLock)
    {
      Registrant localRegistrant = new android/os/Registrant;
      localRegistrant.<init>(paramHandler, paramInt, paramObject);
      mOperatorBrandOverrideRegistrants.add(localRegistrant);
      return;
    }
  }
  
  public boolean resetAppWithAid(String paramString)
  {
    Object localObject = mLock;
    boolean bool1 = false;
    int i = 0;
    try
    {
      while (i < mUiccApplications.length)
      {
        bool2 = bool1;
        if (mUiccApplications[i] != null) {
          if (!TextUtils.isEmpty(paramString))
          {
            bool2 = bool1;
            if (!paramString.equals(mUiccApplications[i].getAid())) {}
          }
          else
          {
            mUiccApplications[i].dispose();
            mUiccApplications[i] = null;
            bool2 = true;
          }
        }
        i++;
        bool1 = bool2;
      }
      boolean bool2 = bool1;
      if (TextUtils.isEmpty(paramString))
      {
        if (mCarrierPrivilegeRules != null)
        {
          mCarrierPrivilegeRules = null;
          bool1 = true;
        }
        bool2 = bool1;
        if (mCatService != null)
        {
          mCatService.dispose();
          mCatService = null;
          bool2 = true;
        }
      }
      return bool2;
    }
    finally {}
  }
  
  void resetProperties()
  {
    if (mCurrentAppType == 1)
    {
      log("update icc_operator_numeric=");
      mTelephonyManager.setSimOperatorNumericForPhone(mPhoneId, "");
      mTelephonyManager.setSimCountryIsoForPhone(mPhoneId, "");
      mTelephonyManager.setSimOperatorNameForPhone(mPhoneId, "");
    }
  }
  
  public void sendEnvelopeWithStatus(String paramString, Message paramMessage)
  {
    mCi.sendEnvelopeWithStatus(paramString, paramMessage);
  }
  
  public void setIccFdnEnabled(boolean paramBoolean, String paramString, Message paramMessage)
  {
    synchronized (mLock)
    {
      if (mUiccApplication != null)
      {
        mUiccApplication.setIccFdnEnabled(paramBoolean, paramString, paramMessage);
      }
      else if (paramMessage != null)
      {
        paramString = new java/lang/RuntimeException;
        paramString.<init>("ICC card is absent.");
        forMessageexception = paramString;
        paramMessage.sendToTarget();
        return;
      }
      return;
    }
  }
  
  public void setIccLockEnabled(boolean paramBoolean, String paramString, Message paramMessage)
  {
    synchronized (mLock)
    {
      if (mUiccApplication != null)
      {
        mUiccApplication.setIccLockEnabled(paramBoolean, paramString, paramMessage);
      }
      else if (paramMessage != null)
      {
        paramString = new java/lang/RuntimeException;
        paramString.<init>("ICC card is absent.");
        forMessageexception = paramString;
        paramMessage.sendToTarget();
        return;
      }
      return;
    }
  }
  
  public boolean setOperatorBrandOverride(String paramString)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("setOperatorBrandOverride: ");
    ((StringBuilder)localObject).append(paramString);
    log(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("current iccId: ");
    ((StringBuilder)localObject).append(SubscriptionInfo.givePrintableIccid(getIccId()));
    log(((StringBuilder)localObject).toString());
    String str = getIccId();
    if (TextUtils.isEmpty(str)) {
      return false;
    }
    localObject = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("operator_branding_");
    localStringBuilder.append(str);
    str = localStringBuilder.toString();
    if (paramString == null) {
      ((SharedPreferences.Editor)localObject).remove(str).commit();
    } else {
      ((SharedPreferences.Editor)localObject).putString(str, paramString).commit();
    }
    mOperatorBrandOverrideRegistrants.notifyRegistrants();
    return true;
  }
  
  public void setVoiceRadioTech(int paramInt)
  {
    synchronized (mLock)
    {
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Setting radio tech ");
      localStringBuilder.append(ServiceState.rilRadioTechnologyToString(paramInt));
      log(localStringBuilder.toString());
      setCurrentAppType(ServiceState.isGsm(paramInt));
      updateIccAvailability(false);
      return;
    }
  }
  
  public void supplyNetworkDepersonalization(String paramString, Message paramMessage)
  {
    synchronized (mLock)
    {
      if (mUiccApplication != null)
      {
        mUiccApplication.supplyNetworkDepersonalization(paramString, paramMessage);
      }
      else if (paramMessage != null)
      {
        paramString = new java/lang/RuntimeException;
        paramString.<init>("CommandsInterface is not set.");
        forMessageexception = paramString;
        paramMessage.sendToTarget();
        return;
      }
      return;
    }
  }
  
  public void supplyPin(String paramString, Message paramMessage)
  {
    synchronized (mLock)
    {
      if (mUiccApplication != null)
      {
        mUiccApplication.supplyPin(paramString, paramMessage);
      }
      else if (paramMessage != null)
      {
        paramString = new java/lang/RuntimeException;
        paramString.<init>("ICC card is absent.");
        forMessageexception = paramString;
        paramMessage.sendToTarget();
        return;
      }
      return;
    }
  }
  
  public void supplyPin2(String paramString, Message paramMessage)
  {
    synchronized (mLock)
    {
      if (mUiccApplication != null)
      {
        mUiccApplication.supplyPin2(paramString, paramMessage);
      }
      else if (paramMessage != null)
      {
        paramString = new java/lang/RuntimeException;
        paramString.<init>("ICC card is absent.");
        forMessageexception = paramString;
        paramMessage.sendToTarget();
        return;
      }
      return;
    }
  }
  
  public void supplyPuk(String paramString1, String paramString2, Message paramMessage)
  {
    synchronized (mLock)
    {
      if (mUiccApplication != null)
      {
        mUiccApplication.supplyPuk(paramString1, paramString2, paramMessage);
      }
      else if (paramMessage != null)
      {
        paramString1 = new java/lang/RuntimeException;
        paramString1.<init>("ICC card is absent.");
        forMessageexception = paramString1;
        paramMessage.sendToTarget();
        return;
      }
      return;
    }
  }
  
  public void supplyPuk2(String paramString1, String paramString2, Message paramMessage)
  {
    synchronized (mLock)
    {
      if (mUiccApplication != null)
      {
        mUiccApplication.supplyPuk2(paramString1, paramString2, paramMessage);
      }
      else if (paramMessage != null)
      {
        paramString1 = new java/lang/RuntimeException;
        paramString1.<init>("ICC card is absent.");
        forMessageexception = paramString1;
        paramMessage.sendToTarget();
        return;
      }
      return;
    }
  }
  
  public void unregisterForCarrierPrivilegeRulesLoaded(Handler paramHandler)
  {
    synchronized (mLock)
    {
      mCarrierPrivilegeRegistrants.remove(paramHandler);
      return;
    }
  }
  
  public void unregisterForNetworkLocked(Handler paramHandler)
  {
    synchronized (mLock)
    {
      mNetworkLockedRegistrants.remove(paramHandler);
      return;
    }
  }
  
  public void unregisterForOperatorBrandOverride(Handler paramHandler)
  {
    synchronized (mLock)
    {
      mOperatorBrandOverrideRegistrants.remove(paramHandler);
      return;
    }
  }
  
  public void update(Context paramContext, CommandsInterface paramCommandsInterface, IccCardStatus paramIccCardStatus)
  {
    synchronized (mLock)
    {
      mUniversalPinState = mUniversalPinState;
      mGsmUmtsSubscriptionAppIndex = mGsmUmtsSubscriptionAppIndex;
      mCdmaSubscriptionAppIndex = mCdmaSubscriptionAppIndex;
      mImsSubscriptionAppIndex = mImsSubscriptionAppIndex;
      mContext = paramContext;
      mCi = paramCommandsInterface;
      mTelephonyManager = ((TelephonyManager)mContext.getSystemService("phone"));
      paramContext = new java/lang/StringBuilder;
      paramContext.<init>();
      paramContext.append(mApplications.length);
      paramContext.append(" applications");
      log(paramContext.toString());
      for (int i = 0; i < mUiccApplications.length; i++) {
        if (mUiccApplications[i] == null)
        {
          if (i < mApplications.length) {
            mUiccApplications[i] = new UiccCardApplication(this, mApplications[i], mContext, mCi);
          }
        }
        else if (i >= mApplications.length)
        {
          mUiccApplications[i].dispose();
          mUiccApplications[i] = null;
        }
        else
        {
          mUiccApplications[i].update(mApplications[i], mContext, mCi);
        }
      }
      createAndUpdateCatServiceLocked();
      paramContext = new java/lang/StringBuilder;
      paramContext.<init>();
      paramContext.append("Before privilege rules: ");
      paramContext.append(mCarrierPrivilegeRules);
      paramContext.append(" : ");
      paramContext.append(mCardState);
      log(paramContext.toString());
      if ((mCarrierPrivilegeRules == null) && (mCardState == IccCardStatus.CardState.CARDSTATE_PRESENT))
      {
        paramContext = new com/android/internal/telephony/uicc/UiccCarrierPrivilegeRules;
        paramContext.<init>(this, mHandler.obtainMessage(13));
        mCarrierPrivilegeRules = paramContext;
      }
      else if ((mCarrierPrivilegeRules != null) && (mCardState != IccCardStatus.CardState.CARDSTATE_PRESENT))
      {
        mCarrierPrivilegeRules = null;
      }
      sanitizeApplicationIndexesLocked();
      updateIccAvailability(true);
      return;
    }
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PRIVATE)
  public void updateExternalState()
  {
    if (mUiccCard.getCardState() == IccCardStatus.CardState.CARDSTATE_ERROR)
    {
      setExternalState(IccCardConstants.State.CARD_IO_ERROR);
      return;
    }
    if (mUiccCard.getCardState() == IccCardStatus.CardState.CARDSTATE_RESTRICTED)
    {
      setExternalState(IccCardConstants.State.CARD_RESTRICTED);
      return;
    }
    if (((mUiccCard instanceof EuiccCard)) && (((EuiccCard)mUiccCard).getEid() == null))
    {
      log("EID is not ready yet.");
      return;
    }
    if (mUiccApplication == null)
    {
      loge("updateExternalState: setting state to NOT_READY because mUiccApplication is null");
      setExternalState(IccCardConstants.State.NOT_READY);
      return;
    }
    int i = 0;
    Object localObject1 = null;
    IccCardApplicationStatus.AppState localAppState = mUiccApplication.getState();
    int j;
    Object localObject2;
    if (mUiccApplication.getPin1State() == IccCardStatus.PinState.PINSTATE_ENABLED_PERM_BLOCKED)
    {
      j = 1;
      localObject2 = IccCardConstants.State.PERM_DISABLED;
    }
    else if (localAppState == IccCardApplicationStatus.AppState.APPSTATE_PIN)
    {
      j = 1;
      localObject2 = IccCardConstants.State.PIN_REQUIRED;
    }
    else if (localAppState == IccCardApplicationStatus.AppState.APPSTATE_PUK)
    {
      j = 1;
      localObject2 = IccCardConstants.State.PUK_REQUIRED;
    }
    else
    {
      j = i;
      localObject2 = localObject1;
      if (localAppState == IccCardApplicationStatus.AppState.APPSTATE_SUBSCRIPTION_PERSO)
      {
        j = i;
        localObject2 = localObject1;
        if (mUiccApplication.isPersoLocked())
        {
          j = 1;
          localObject2 = IccCardConstants.State.NETWORK_LOCKED;
        }
      }
    }
    if (j != 0)
    {
      if ((mIccRecords != null) && ((mIccRecords.getLockedRecordsLoaded()) || (mIccRecords.getNetworkLockedRecordsLoaded()))) {
        setExternalState((IccCardConstants.State)localObject2);
      } else {
        setExternalState(IccCardConstants.State.NOT_READY);
      }
      return;
    }
    switch (4.$SwitchMap$com$android$internal$telephony$uicc$IccCardApplicationStatus$AppState[localAppState.ordinal()])
    {
    default: 
      break;
    case 2: 
      checkAndUpdateIfAnyAppToBeIgnored();
      if ((areReadyAppsRecordsLoaded()) && (areCarrierPriviligeRulesLoaded())) {
        setExternalState(IccCardConstants.State.LOADED);
      } else {
        setExternalState(IccCardConstants.State.READY);
      }
      break;
    case 1: 
      setExternalState(IccCardConstants.State.NOT_READY);
    }
  }
  
  public void updateRadioOn()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[updateRadioOn] mExternalState: ");
    localStringBuilder.append(mExternalState);
    localStringBuilder.append(", mPhoneId: ");
    localStringBuilder.append(mPhoneId);
    log(localStringBuilder.toString());
    if ((mExternalState == IccCardConstants.State.PIN_REQUIRED) || (mExternalState == IccCardConstants.State.PUK_REQUIRED) || (mExternalState == IccCardConstants.State.ABSENT)) {
      UiccController.updateInternalIccState(getIccStateIntentString(mExternalState), getIccStateReason(mExternalState), mPhoneId);
    }
  }
}
