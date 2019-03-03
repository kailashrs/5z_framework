package com.android.internal.telephony;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.provider.Settings.Global;
import android.service.carrier.ICarrierMessagingCallback.Stub;
import android.service.carrier.ICarrierMessagingService;
import android.telephony.CarrierConfigManager;
import android.telephony.CarrierMessagingServiceManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spanned;
import android.util.EventLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.cdma.sms.UserData;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class SMSDispatcher
  extends Handler
{
  static final boolean DBG = false;
  private static final int EVENT_CONFIRM_SEND_TO_POSSIBLE_PREMIUM_SHORT_CODE = 8;
  private static final int EVENT_CONFIRM_SEND_TO_PREMIUM_SHORT_CODE = 9;
  protected static final int EVENT_GET_IMS_SERVICE = 16;
  protected static final int EVENT_HANDLE_STATUS_REPORT = 10;
  protected static final int EVENT_ICC_CHANGED = 15;
  protected static final int EVENT_NEW_ICC_SMS = 14;
  static final int EVENT_SEND_CONFIRMED_SMS = 5;
  private static final int EVENT_SEND_LIMIT_REACHED_CONFIRMATION = 4;
  protected static final int EVENT_SEND_RETRY = 3;
  protected static final int EVENT_SEND_SMS_COMPLETE = 2;
  static final int EVENT_STOP_SENDING = 7;
  protected static final String MAP_KEY_DATA = "data";
  protected static final String MAP_KEY_DEST_ADDR = "destAddr";
  protected static final String MAP_KEY_DEST_PORT = "destPort";
  protected static final String MAP_KEY_PDU = "pdu";
  protected static final String MAP_KEY_SC_ADDR = "scAddr";
  protected static final String MAP_KEY_SMSC = "smsc";
  protected static final String MAP_KEY_TEXT = "text";
  protected static final int MAX_SEND_RETRIES = 3;
  private static final int MO_MSG_QUEUE_LIMIT = 5;
  private static final int PREMIUM_RULE_USE_BOTH = 3;
  private static final int PREMIUM_RULE_USE_NETWORK = 2;
  private static final int PREMIUM_RULE_USE_SIM = 1;
  private static final String SEND_NEXT_MSG_EXTRA = "SendNextMsg";
  protected static final int SEND_RETRY_DELAY = 2000;
  private static final int SINGLE_PART_SMS = 1;
  static final String TAG = "SMSDispatcher";
  private static int sConcatenatedRef = new Random().nextInt(256);
  protected final ArrayList<SmsTracker> deliveryPendingList = new ArrayList();
  protected final CommandsInterface mCi;
  protected final Context mContext;
  private int mPendingTrackerCount;
  protected Phone mPhone;
  private final AtomicInteger mPremiumSmsRule = new AtomicInteger(1);
  protected final ContentResolver mResolver;
  private final SettingsObserver mSettingsObserver;
  protected boolean mSmsCapable = true;
  protected SmsDispatchersController mSmsDispatchersController;
  protected boolean mSmsSendDisabled;
  protected final TelephonyManager mTelephonyManager;
  
  protected SMSDispatcher(Phone paramPhone, SmsDispatchersController paramSmsDispatchersController)
  {
    mPhone = paramPhone;
    mSmsDispatchersController = paramSmsDispatchersController;
    mContext = paramPhone.getContext();
    mResolver = mContext.getContentResolver();
    mCi = mCi;
    mTelephonyManager = ((TelephonyManager)mContext.getSystemService("phone"));
    mSettingsObserver = new SettingsObserver(this, mPremiumSmsRule, mContext);
    mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("sms_short_code_rule"), false, mSettingsObserver);
    mSmsCapable = mContext.getResources().getBoolean(17957033);
    mSmsSendDisabled = (mTelephonyManager.getSmsSendCapableForPhone(mPhone.getPhoneId(), mSmsCapable) ^ true);
    paramPhone = new StringBuilder();
    paramPhone.append("SMSDispatcher: ctor mSmsCapable=");
    paramPhone.append(mSmsCapable);
    paramPhone.append(" format=");
    paramPhone.append(getFormat());
    paramPhone.append(" mSmsSendDisabled=");
    paramPhone.append(mSmsSendDisabled);
    Rlog.d("SMSDispatcher", paramPhone.toString());
  }
  
  private void checkCallerIsPhoneOrCarrierApp()
  {
    int i = Binder.getCallingUid();
    if ((UserHandle.getAppId(i) != 1001) && (i != 0)) {
      try
      {
        if (UserHandle.isSameApp(mContext.getPackageManager().getApplicationInfo(getCarrierAppPackageName(), 0).uid, Binder.getCallingUid())) {
          return;
        }
        SecurityException localSecurityException = new java/lang/SecurityException;
        localSecurityException.<init>("Caller is not phone or carrier app!");
        throw localSecurityException;
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
        throw new SecurityException("Caller is not phone or carrier app!");
      }
    }
  }
  
  private boolean denyIfQueueLimitReached(SmsTracker paramSmsTracker)
  {
    if (mPendingTrackerCount >= 5)
    {
      Rlog.e("SMSDispatcher", "Denied because queue limit reached");
      paramSmsTracker.onFailed(mContext, 5, 0);
      return true;
    }
    mPendingTrackerCount += 1;
    return false;
  }
  
  private CharSequence getAppLabel(String paramString, int paramInt)
  {
    Object localObject = mContext.getPackageManager();
    try
    {
      localObject = ((PackageManager)localObject).getApplicationInfoAsUser(paramString, 0, paramInt).loadSafeLabel((PackageManager)localObject);
      return localObject;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("PackageManager Name Not Found for package ");
      localStringBuilder.append(paramString);
      Rlog.e("SMSDispatcher", localStringBuilder.toString());
    }
    return paramString;
  }
  
  private String getMultipartMessageText(ArrayList<String> paramArrayList)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Iterator localIterator = paramArrayList.iterator();
    while (localIterator.hasNext())
    {
      paramArrayList = (String)localIterator.next();
      if (paramArrayList != null) {
        localStringBuilder.append(paramArrayList);
      }
    }
    return localStringBuilder.toString();
  }
  
  private SmsTracker getNewSubmitPduTracker(String paramString1, String paramString2, String paramString3, SmsHeader paramSmsHeader, int paramInt1, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, boolean paramBoolean1, AtomicInteger paramAtomicInteger, AtomicBoolean paramAtomicBoolean, Uri paramUri, String paramString4, int paramInt2, boolean paramBoolean2, int paramInt3)
  {
    boolean bool1 = isCdmaMo();
    boolean bool2 = false;
    boolean bool3 = false;
    if (bool1)
    {
      localObject = new UserData();
      payloadStr = paramString3;
      userDataHeader = paramSmsHeader;
      if (paramInt1 == 1)
      {
        if (isAscii7bitSupportedForLongMessage()) {
          paramInt1 = 2;
        } else {
          paramInt1 = 9;
        }
        msgEncoding = paramInt1;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Message encoding for proper 7 bit: ");
        localStringBuilder.append(msgEncoding);
        Rlog.d("SMSDispatcher", localStringBuilder.toString());
      }
      else
      {
        msgEncoding = 4;
      }
      msgEncodingSet = true;
      if ((paramPendingIntent2 != null) && (paramBoolean1)) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      paramString2 = getSmsTrackerMap(paramString1, paramString2, paramString3, com.android.internal.telephony.cdma.SmsMessage.getSubmitPdu(paramString1, (UserData)localObject, bool1, paramInt2));
      paramString1 = getFormat();
      if ((paramBoolean1) && (!paramBoolean2)) {
        paramBoolean1 = bool3;
      } else {
        paramBoolean1 = true;
      }
      return getSmsTracker(paramString2, paramPendingIntent1, paramPendingIntent2, paramString1, paramAtomicInteger, paramAtomicBoolean, paramUri, paramSmsHeader, paramBoolean1, paramString4, true, true, paramInt2, paramInt3);
    }
    if (paramPendingIntent2 != null) {
      bool1 = true;
    } else {
      bool1 = false;
    }
    Object localObject = com.android.internal.telephony.gsm.SmsMessage.getSubmitPdu(paramString2, paramString1, paramString3, bool1, SmsHeader.toByteArray(paramSmsHeader), paramInt1, languageTable, languageShiftTable, paramInt3);
    if (localObject != null)
    {
      paramString2 = getSmsTrackerMap(paramString1, paramString2, paramString3, (SmsMessageBase.SubmitPduBase)localObject);
      paramString1 = getFormat();
      if ((paramBoolean1) && (!paramBoolean2)) {
        paramBoolean1 = bool2;
      } else {
        paramBoolean1 = true;
      }
      return getSmsTracker(paramString2, paramPendingIntent1, paramPendingIntent2, paramString1, paramAtomicInteger, paramAtomicBoolean, paramUri, paramSmsHeader, paramBoolean1, paramString4, true, false, paramInt2, paramInt3);
    }
    Rlog.e("SMSDispatcher", "GsmSMSDispatcher.sendNewSubmitPdu(): getSubmitPdu() returned null");
    return null;
  }
  
  protected static int getNextConcatenatedRef()
  {
    sConcatenatedRef += 1;
    return sConcatenatedRef;
  }
  
  protected static int getNotInServiceError(int paramInt)
  {
    if (paramInt == 3) {
      return 2;
    }
    return 4;
  }
  
  private static int getSendSmsFlag(PendingIntent paramPendingIntent)
  {
    if (paramPendingIntent == null) {
      return 0;
    }
    return 1;
  }
  
  protected static void handleNotInService(int paramInt, PendingIntent paramPendingIntent)
  {
    if (paramPendingIntent != null)
    {
      if (paramInt == 3) {
        try
        {
          paramPendingIntent.send(2);
        }
        catch (PendingIntent.CanceledException paramPendingIntent)
        {
          break label29;
        }
      } else {
        paramPendingIntent.send(4);
      }
      return;
      label29:
      Rlog.e("SMSDispatcher", "Failed to send result");
    }
  }
  
  private boolean isAscii7bitSupportedForLongMessage()
  {
    Object localObject1 = (CarrierConfigManager)mContext.getSystemService("carrier_config");
    long l = Binder.clearCallingIdentity();
    try
    {
      localObject1 = ((CarrierConfigManager)localObject1).getConfigForSubId(mPhone.getSubId());
      Binder.restoreCallingIdentity(l);
      if (localObject1 != null) {
        return ((PersistableBundle)localObject1).getBoolean("ascii_7_bit_support_for_long_message");
      }
      return false;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  private void processSendSmsResponse(SmsTracker paramSmsTracker, int paramInt1, int paramInt2)
  {
    if (paramSmsTracker == null)
    {
      Rlog.e("SMSDispatcher", "processSendSmsResponse: null tracker");
      return;
    }
    Object localObject = new SmsResponse(paramInt2, null, -1);
    switch (paramInt1)
    {
    default: 
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Unknown result ");
      ((StringBuilder)localObject).append(paramInt1);
      ((StringBuilder)localObject).append(" Retry on carrier network.");
      Rlog.d("SMSDispatcher", ((StringBuilder)localObject).toString());
      sendSubmitPdu(paramSmsTracker);
      break;
    case 2: 
      Rlog.d("SMSDispatcher", "Sending SMS by IP failed.");
      sendMessage(obtainMessage(2, new AsyncResult(paramSmsTracker, localObject, new CommandException(CommandException.Error.GENERIC_FAILURE))));
      break;
    case 1: 
      Rlog.d("SMSDispatcher", "Sending SMS by IP failed. Retry on carrier network.");
      sendSubmitPdu(paramSmsTracker);
      break;
    case 0: 
      Rlog.d("SMSDispatcher", "Sending SMS by IP succeeded.");
      sendMessage(obtainMessage(2, new AsyncResult(paramSmsTracker, localObject, null)));
    }
  }
  
  private void sendMultipartSms(SmsTracker paramSmsTracker)
  {
    Object localObject = paramSmsTracker.getData();
    String str1 = (String)((HashMap)localObject).get("destination");
    String str2 = (String)((HashMap)localObject).get("scaddress");
    ArrayList localArrayList1 = (ArrayList)((HashMap)localObject).get("parts");
    ArrayList localArrayList2 = (ArrayList)((HashMap)localObject).get("sentIntents");
    localObject = (ArrayList)((HashMap)localObject).get("deliveryIntents");
    int i = mPhone.getServiceState().getState();
    if ((!isIms()) && (i != 0))
    {
      int j = 0;
      int k = localArrayList1.size();
      while (j < k)
      {
        localArrayList1 = null;
        paramSmsTracker = localArrayList1;
        if (localArrayList2 != null)
        {
          paramSmsTracker = localArrayList1;
          if (localArrayList2.size() > j) {
            paramSmsTracker = (PendingIntent)localArrayList2.get(j);
          }
        }
        handleNotInService(i, paramSmsTracker);
        j++;
      }
      return;
    }
    sendMultipartText(str1, str2, localArrayList1, localArrayList2, (ArrayList)localObject, null, null, mPersistMessage, mPriority, mExpectMore, mValidityPeriod);
  }
  
  private boolean sendSmsByCarrierApp(boolean paramBoolean, SmsTracker paramSmsTracker)
  {
    String str = getCarrierAppPackageName();
    if (str != null)
    {
      Rlog.d("SMSDispatcher", "Found carrier package.");
      if (paramBoolean) {
        paramSmsTracker = new DataSmsSender(paramSmsTracker);
      } else {
        paramSmsTracker = new TextSmsSender(paramSmsTracker);
      }
      paramSmsTracker.sendSmsByCarrierApp(str, new SmsSenderCallback(paramSmsTracker));
      return true;
    }
    return false;
  }
  
  private void sendSubmitPdu(SmsTracker paramSmsTracker)
  {
    if (shouldBlockSmsForEcbm())
    {
      Rlog.d("SMSDispatcher", "Block SMS in Emergency Callback mode");
      paramSmsTracker.onFailed(mContext, 4, 0);
    }
    else
    {
      sendRawPdu(paramSmsTracker);
    }
  }
  
  private void triggerSentIntentForFailure(PendingIntent paramPendingIntent)
  {
    if (paramPendingIntent != null) {
      try
      {
        paramPendingIntent.send(1);
      }
      catch (PendingIntent.CanceledException paramPendingIntent)
      {
        Rlog.e("SMSDispatcher", "Intent has been canceled!");
      }
    }
  }
  
  protected abstract GsmAlphabet.TextEncodingDetails calculateLength(CharSequence paramCharSequence, boolean paramBoolean);
  
  boolean checkDestination(SmsTracker paramSmsTracker)
  {
    if (mContext.checkCallingOrSelfPermission("android.permission.SEND_SMS_NO_CONFIRMATION") == 0) {
      return true;
    }
    int i = mPremiumSmsRule.get();
    int j = 0;
    String str1;
    String str2;
    if ((i == 1) || (i == 3))
    {
      str1 = mTelephonyManager.getSimCountryIso();
      if (str1 != null)
      {
        str2 = str1;
        if (str1.length() == 2) {}
      }
      else
      {
        Rlog.e("SMSDispatcher", "Can't get SIM country Iso: trying network country Iso");
        str2 = mTelephonyManager.getNetworkCountryIso();
      }
      j = mSmsDispatchersController.getUsageMonitor().checkDestination(mDestAddress, str2);
    }
    int k;
    if (i != 2)
    {
      k = j;
      if (i != 3) {}
    }
    else
    {
      str1 = mTelephonyManager.getNetworkCountryIso();
      if (str1 != null)
      {
        str2 = str1;
        if (str1.length() == 2) {}
      }
      else
      {
        Rlog.e("SMSDispatcher", "Can't get Network country Iso: trying SIM country Iso");
        str2 = mTelephonyManager.getSimCountryIso();
      }
      k = SmsUsageMonitor.mergeShortCodeCategories(j, mSmsDispatchersController.getUsageMonitor().checkDestination(mDestAddress, str2));
    }
    if ((k != 0) && (k != 1) && (k != 2))
    {
      if (Settings.Global.getInt(mResolver, "device_provisioned", 0) == 0)
      {
        Rlog.e("SMSDispatcher", "Can't send premium sms during Setup Wizard");
        return false;
      }
      i = mSmsDispatchersController.getUsageMonitor().getPremiumSmsPermission(paramSmsTracker.getAppPackageName());
      j = i;
      if (i == 0) {
        j = 1;
      }
      switch (j)
      {
      default: 
        if (k == 3) {
          j = 8;
        }
        break;
      case 3: 
        Rlog.d("SMSDispatcher", "User approved this app to send to premium SMS");
        return true;
      case 2: 
        Rlog.w("SMSDispatcher", "User denied this app from sending to premium SMS");
        paramSmsTracker = obtainMessage(7, paramSmsTracker);
        arg1 = 0;
        arg2 = 1;
        sendMessage(paramSmsTracker);
        return false;
      }
      j = 9;
      sendMessage(obtainMessage(j, paramSmsTracker));
      return false;
    }
    return true;
  }
  
  public void dispose()
  {
    mContext.getContentResolver().unregisterContentObserver(mSettingsObserver);
  }
  
  protected String getCarrierAppPackageName()
  {
    Object localObject = UiccController.getInstance().getUiccCard(mPhone.getPhoneId());
    if (localObject == null) {
      return null;
    }
    localObject = ((UiccCard)localObject).getCarrierPackageNamesForIntent(mContext.getPackageManager(), new Intent("android.service.carrier.CarrierMessagingService"));
    if ((localObject != null) && (((List)localObject).size() == 1)) {
      return (String)((List)localObject).get(0);
    }
    return CarrierSmsUtils.getCarrierImsPackageForIntent(mContext, mPhone, new Intent("android.service.carrier.CarrierMessagingService"));
  }
  
  protected abstract String getFormat();
  
  protected SmsTracker getSmsTracker(HashMap<String, Object> paramHashMap, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, String paramString1, Uri paramUri, boolean paramBoolean1, String paramString2, boolean paramBoolean2, boolean paramBoolean3)
  {
    return getSmsTracker(paramHashMap, paramPendingIntent1, paramPendingIntent2, paramString1, null, null, paramUri, null, paramBoolean1, paramString2, paramBoolean2, paramBoolean3, -1, -1);
  }
  
  protected SmsTracker getSmsTracker(HashMap<String, Object> paramHashMap, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, String paramString1, Uri paramUri, boolean paramBoolean1, String paramString2, boolean paramBoolean2, boolean paramBoolean3, int paramInt1, int paramInt2)
  {
    return getSmsTracker(paramHashMap, paramPendingIntent1, paramPendingIntent2, paramString1, null, null, paramUri, null, paramBoolean1, paramString2, paramBoolean2, paramBoolean3, paramInt1, paramInt2);
  }
  
  protected SmsTracker getSmsTracker(HashMap<String, Object> paramHashMap, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, String paramString1, AtomicInteger paramAtomicInteger, AtomicBoolean paramAtomicBoolean, Uri paramUri, SmsHeader paramSmsHeader, boolean paramBoolean1, String paramString2, boolean paramBoolean2, boolean paramBoolean3, int paramInt1, int paramInt2)
  {
    Object localObject = mContext.getPackageManager();
    String[] arrayOfString = ((PackageManager)localObject).getPackagesForUid(Binder.getCallingUid());
    int i = UserHandle.getCallingUserId();
    PackageInfo localPackageInfo;
    if ((arrayOfString != null) && (arrayOfString.length > 0)) {
      try
      {
        localObject = ((PackageManager)localObject).getPackageInfoAsUser(arrayOfString[0], 64, i);
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException) {}
    } else {
      localPackageInfo = null;
    }
    return new SmsTracker(paramHashMap, paramPendingIntent1, paramPendingIntent2, localPackageInfo, PhoneNumberUtils.extractNetworkPortion((String)paramHashMap.get("destAddr")), paramString1, paramAtomicInteger, paramAtomicBoolean, paramUri, paramSmsHeader, paramBoolean1, paramString2, getSubId(), paramBoolean2, paramBoolean3, i, paramInt1, paramInt2, null);
  }
  
  protected HashMap<String, Object> getSmsTrackerMap(String paramString1, String paramString2, int paramInt, byte[] paramArrayOfByte, SmsMessageBase.SubmitPduBase paramSubmitPduBase)
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("destAddr", paramString1);
    localHashMap.put("scAddr", paramString2);
    localHashMap.put("destPort", Integer.valueOf(paramInt));
    localHashMap.put("data", paramArrayOfByte);
    localHashMap.put("smsc", encodedScAddress);
    localHashMap.put("pdu", encodedMessage);
    return localHashMap;
  }
  
  protected HashMap<String, Object> getSmsTrackerMap(String paramString1, String paramString2, String paramString3, SmsMessageBase.SubmitPduBase paramSubmitPduBase)
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("destAddr", paramString1);
    localHashMap.put("scAddr", paramString2);
    localHashMap.put("text", paramString3);
    localHashMap.put("smsc", encodedScAddress);
    localHashMap.put("pdu", encodedMessage);
    return localHashMap;
  }
  
  protected int getSubId()
  {
    return SubscriptionController.getInstance().getSubIdUsingPhoneId(mPhone.getPhoneId());
  }
  
  protected abstract SmsMessageBase.SubmitPduBase getSubmitPdu(String paramString1, String paramString2, int paramInt, byte[] paramArrayOfByte, boolean paramBoolean);
  
  protected abstract SmsMessageBase.SubmitPduBase getSubmitPdu(String paramString1, String paramString2, String paramString3, boolean paramBoolean, SmsHeader paramSmsHeader, int paramInt1, int paramInt2);
  
  protected void handleConfirmShortCode(boolean paramBoolean, SmsTracker paramSmsTracker)
  {
    if (denyIfQueueLimitReached(paramSmsTracker)) {
      return;
    }
    int i;
    if (paramBoolean) {
      i = 17041031;
    } else {
      i = 17041037;
    }
    Object localObject1 = getAppLabel(paramSmsTracker.getAppPackageName(), mUserId);
    Object localObject2 = Resources.getSystem();
    Spanned localSpanned = Html.fromHtml(((Resources)localObject2).getString(17041035, new Object[] { localObject1, mDestAddress }));
    localObject1 = ((LayoutInflater)mContext.getSystemService("layout_inflater")).inflate(17367321, null);
    paramSmsTracker = new ConfirmDialogListener(paramSmsTracker, (TextView)((View)localObject1).findViewById(16909386), 0);
    ((TextView)((View)localObject1).findViewById(16909381)).setText(localSpanned);
    ((TextView)((ViewGroup)((View)localObject1).findViewById(16909382)).findViewById(16909383)).setText(i);
    ((CheckBox)((View)localObject1).findViewById(16909384)).setOnCheckedChangeListener(paramSmsTracker);
    localObject2 = new AlertDialog.Builder(mContext).setView((View)localObject1).setPositiveButton(((Resources)localObject2).getString(17041032), paramSmsTracker).setNegativeButton(((Resources)localObject2).getString(17041034), paramSmsTracker).setOnCancelListener(paramSmsTracker).create();
    ((AlertDialog)localObject2).getWindow().setType(2003);
    ((AlertDialog)localObject2).show();
    paramSmsTracker.setPositiveButton(((AlertDialog)localObject2).getButton(-1));
    paramSmsTracker.setNegativeButton(((AlertDialog)localObject2).getButton(-2));
  }
  
  public void handleMessage(Message paramMessage)
  {
    Object localObject;
    switch (what)
    {
    case 6: 
    default: 
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("handleMessage() ignoring message of unexpected type ");
      ((StringBuilder)localObject).append(what);
      Rlog.e("SMSDispatcher", ((StringBuilder)localObject).toString());
      break;
    case 10: 
      handleStatusReport(obj);
      break;
    case 9: 
      handleConfirmShortCode(true, (SmsTracker)obj);
      break;
    case 8: 
      handleConfirmShortCode(false, (SmsTracker)obj);
      break;
    case 7: 
      localObject = (SmsTracker)obj;
      if (arg1 == 0)
      {
        if (arg2 == 1)
        {
          ((SmsTracker)localObject).onFailed(mContext, 8, 0);
          Rlog.d("SMSDispatcher", "SMSDispatcher: EVENT_STOP_SENDING - sending SHORT_CODE_NEVER_ALLOWED error code.");
        }
        else
        {
          ((SmsTracker)localObject).onFailed(mContext, 7, 0);
          Rlog.d("SMSDispatcher", "SMSDispatcher: EVENT_STOP_SENDING - sending SHORT_CODE_NOT_ALLOWED error code.");
        }
      }
      else if (arg1 == 1)
      {
        ((SmsTracker)localObject).onFailed(mContext, 5, 0);
        Rlog.d("SMSDispatcher", "SMSDispatcher: EVENT_STOP_SENDING - sending LIMIT_EXCEEDED error code.");
      }
      else
      {
        Rlog.e("SMSDispatcher", "SMSDispatcher: EVENT_STOP_SENDING - unexpected cases.");
      }
      mPendingTrackerCount -= 1;
      break;
    case 5: 
      paramMessage = (SmsTracker)obj;
      if (paramMessage.isMultipart())
      {
        sendMultipartSms(paramMessage);
      }
      else
      {
        if (mPendingTrackerCount > 1) {
          mExpectMore = true;
        } else {
          mExpectMore = false;
        }
        sendSms(paramMessage);
      }
      mPendingTrackerCount -= 1;
      break;
    case 4: 
      handleReachSentLimit((SmsTracker)obj);
      break;
    case 3: 
      Rlog.d("SMSDispatcher", "SMS retry..");
      sendRetrySms((SmsTracker)obj);
      break;
    case 2: 
      handleSendComplete((AsyncResult)obj);
    }
  }
  
  protected void handleReachSentLimit(SmsTracker paramSmsTracker)
  {
    if (denyIfQueueLimitReached(paramSmsTracker)) {
      return;
    }
    Object localObject = getAppLabel(paramSmsTracker.getAppPackageName(), mUserId);
    Resources localResources = Resources.getSystem();
    localObject = Html.fromHtml(localResources.getString(17041026, new Object[] { localObject }));
    paramSmsTracker = new ConfirmDialogListener(paramSmsTracker, null, 1);
    paramSmsTracker = new AlertDialog.Builder(mContext).setTitle(17041028).setIcon(17301642).setMessage((CharSequence)localObject).setPositiveButton(localResources.getString(17041029), paramSmsTracker).setNegativeButton(localResources.getString(17041027), paramSmsTracker).setOnCancelListener(paramSmsTracker).create();
    paramSmsTracker.getWindow().setType(2003);
    paramSmsTracker.show();
  }
  
  protected void handleSendComplete(AsyncResult paramAsyncResult)
  {
    SmsTracker localSmsTracker = (SmsTracker)userObj;
    Object localObject = mSentIntent;
    if (result != null) {
      mMessageRef = result).mMessageRef;
    } else {
      Rlog.d("SMSDispatcher", "SmsResponse was null");
    }
    if (exception == null)
    {
      if (mDeliveryIntent != null) {
        deliveryPendingList.add(localSmsTracker);
      }
      localSmsTracker.onSent(mContext);
    }
    else
    {
      int i = mPhone.getServiceState().getState();
      if ((mImsRetry > 0) && (i != 0))
      {
        mRetryCount = 3;
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("handleSendComplete: Skipping retry:  isIms()=");
        ((StringBuilder)localObject).append(isIms());
        ((StringBuilder)localObject).append(" mRetryCount=");
        ((StringBuilder)localObject).append(mRetryCount);
        ((StringBuilder)localObject).append(" mImsRetry=");
        ((StringBuilder)localObject).append(mImsRetry);
        ((StringBuilder)localObject).append(" mMessageRef=");
        ((StringBuilder)localObject).append(mMessageRef);
        ((StringBuilder)localObject).append(" SS= ");
        ((StringBuilder)localObject).append(mPhone.getServiceState().getState());
        Rlog.d("SMSDispatcher", ((StringBuilder)localObject).toString());
      }
      if ((!isIms()) && (i != 0))
      {
        localSmsTracker.onFailed(mContext, getNotInServiceError(i), 0);
      }
      else if ((((CommandException)exception).getCommandError() == CommandException.Error.SMS_FAIL_RETRY) && (mRetryCount < 3))
      {
        mRetryCount += 1;
        sendMessageDelayed(obtainMessage(3, localSmsTracker), 2000L);
      }
      else
      {
        i = 0;
        if (result != null) {
          i = result).mErrorCode;
        }
        int j = 1;
        if (((CommandException)exception).getCommandError() == CommandException.Error.FDN_CHECK_FAILURE) {
          j = 6;
        }
        localSmsTracker.onFailed(mContext, j, i);
      }
    }
  }
  
  protected void handleStatusReport(Object paramObject)
  {
    Rlog.d("SMSDispatcher", "handleStatusReport() called with no subclass.");
  }
  
  protected boolean isCdmaMo()
  {
    return mSmsDispatchersController.isCdmaMo();
  }
  
  public boolean isIms()
  {
    if (mSmsDispatchersController != null) {
      return mSmsDispatchersController.isIms();
    }
    Rlog.e("SMSDispatcher", "mSmsDispatchersController  is null");
    return false;
  }
  
  protected void sendData(String paramString1, String paramString2, int paramInt, byte[] paramArrayOfByte, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2)
  {
    boolean bool;
    if (paramPendingIntent2 != null) {
      bool = true;
    } else {
      bool = false;
    }
    SmsMessageBase.SubmitPduBase localSubmitPduBase = getSubmitPdu(paramString2, paramString1, paramInt, paramArrayOfByte, bool);
    if (localSubmitPduBase != null)
    {
      paramString1 = getSmsTracker(getSmsTrackerMap(paramString1, paramString2, paramInt, paramArrayOfByte, localSubmitPduBase), paramPendingIntent1, paramPendingIntent2, getFormat(), null, false, null, false, true);
      if (!sendSmsByCarrierApp(true, paramString1)) {
        sendSubmitPdu(paramString1);
      }
    }
    else
    {
      Rlog.e("SMSDispatcher", "SMSDispatcher.sendData(): getSubmitPdu() returned null");
      triggerSentIntentForFailure(paramPendingIntent1);
    }
  }
  
  protected void sendMultipartText(String paramString1, String paramString2, ArrayList<String> paramArrayList, ArrayList<PendingIntent> paramArrayList1, ArrayList<PendingIntent> paramArrayList2, Uri paramUri, String paramString3, boolean paramBoolean1, int paramInt1, boolean paramBoolean2, int paramInt2)
  {
    paramString3 = this;
    Object localObject1 = paramArrayList;
    String str = paramString3.getMultipartMessageText((ArrayList)localObject1);
    int i = getNextConcatenatedRef() & 0xFF;
    int j = paramArrayList.size();
    GsmAlphabet.TextEncodingDetails[] arrayOfTextEncodingDetails = new GsmAlphabet.TextEncodingDetails[j];
    int k = 0;
    int m = 0;
    Object localObject2;
    while (m < j)
    {
      localObject2 = paramString3.calculateLength((CharSequence)((ArrayList)localObject1).get(m), false);
      n = k;
      if (k != codeUnitSize) {
        if (k != 0)
        {
          n = k;
          if (k != 1) {}
        }
        else
        {
          n = codeUnitSize;
        }
      }
      arrayOfTextEncodingDetails[m] = localObject2;
      m++;
      k = n;
    }
    paramString3 = new SmsTracker[j];
    AtomicInteger localAtomicInteger = new AtomicInteger(j);
    AtomicBoolean localAtomicBoolean = new AtomicBoolean(false);
    int i1 = 0;
    m = i;
    int n = j;
    for (j = i1;; j++)
    {
      localObject2 = paramArrayList1;
      Object localObject3 = paramArrayList2;
      if (j >= n) {
        break;
      }
      localObject1 = new SmsHeader.ConcatRef();
      refNumber = m;
      seqNumber = (j + 1);
      msgCount = n;
      isEightBits = true;
      SmsHeader localSmsHeader = new SmsHeader();
      concatRef = ((SmsHeader.ConcatRef)localObject1);
      if (k == 1)
      {
        languageTable = languageTable;
        languageShiftTable = languageShiftTable;
      }
      Object localObject4 = null;
      localObject1 = localObject4;
      if (localObject2 != null)
      {
        localObject1 = localObject4;
        if (paramArrayList1.size() > j) {
          localObject1 = (PendingIntent)((ArrayList)localObject2).get(j);
        }
      }
      localObject4 = null;
      localObject2 = localObject4;
      if (localObject3 != null)
      {
        localObject2 = localObject4;
        if (paramArrayList2.size() > j) {
          localObject2 = (PendingIntent)((ArrayList)localObject3).get(j);
        }
      }
      localObject3 = (String)paramArrayList.get(j);
      boolean bool;
      if (j == n - 1) {
        bool = true;
      } else {
        bool = false;
      }
      paramString3[j] = getNewSubmitPduTracker(paramString1, paramString2, (String)localObject3, localSmsHeader, k, (PendingIntent)localObject1, (PendingIntent)localObject2, bool, localAtomicInteger, localAtomicBoolean, paramUri, str, paramInt1, paramBoolean2, paramInt2);
      SmsTracker.access$302(paramString3[j], paramBoolean1);
    }
    if ((paramArrayList != null) && (paramString3.length != 0) && (paramString3[0] != null))
    {
      paramString1 = getCarrierAppPackageName();
      if (paramString1 != null)
      {
        Rlog.d("SMSDispatcher", "Found carrier package.");
        paramString2 = new MultipartSmsSender(paramArrayList, paramString3);
        paramString2.sendSmsByCarrierApp(paramString1, new MultipartSmsSenderCallback(paramString2));
      }
      else
      {
        Rlog.v("SMSDispatcher", "No carrier package.");
        paramInt2 = paramString3.length;
        for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++)
        {
          paramString1 = paramString3[paramInt1];
          if (paramString1 != null) {
            sendSubmitPdu(paramString1);
          } else {
            Rlog.e("SMSDispatcher", "Null tracker.");
          }
        }
      }
      return;
    }
    paramString1 = new StringBuilder();
    paramString1.append("Cannot send multipart text. parts=");
    paramString1.append(paramArrayList);
    paramString1.append(" trackers=");
    paramString1.append(paramString3);
    Rlog.e("SMSDispatcher", paramString1.toString());
  }
  
  @VisibleForTesting
  public void sendRawPdu(final SmsTracker paramSmsTracker)
  {
    Object localObject = (byte[])paramSmsTracker.getData().get("pdu");
    if (mSmsSendDisabled)
    {
      Rlog.e("SMSDispatcher", "Device does not support sending sms.");
      paramSmsTracker.onFailed(mContext, 4, 0);
      return;
    }
    if (localObject == null)
    {
      Rlog.e("SMSDispatcher", "Empty PDU");
      paramSmsTracker.onFailed(mContext, 3, 0);
      return;
    }
    localObject = mContext.getPackageManager();
    String[] arrayOfString = ((PackageManager)localObject).getPackagesForUid(Binder.getCallingUid());
    if ((arrayOfString != null) && (arrayOfString.length != 0)) {
      try
      {
        localObject = ((PackageManager)localObject).getPackageInfoAsUser(arrayOfString[0], 64, mUserId);
        if (checkDestination(paramSmsTracker))
        {
          if (!mSmsDispatchersController.getUsageMonitor().check(packageName, 1))
          {
            sendMessage(obtainMessage(4, paramSmsTracker));
            return;
          }
          if (mSmsDispatchersController.getUsageMonitor().isSmsAuthorizationEnabled())
          {
            localObject = new SmsUsageMonitor.SmsAuthorizationCallback()
            {
              public void onAuthorizationResult(boolean paramAnonymousBoolean)
              {
                if (paramAnonymousBoolean) {
                  sendSms(paramSmsTracker);
                } else {
                  paramSmsTracker.onFailed(mContext, 1, 191286);
                }
              }
            };
            mSmsDispatchersController.getUsageMonitor().authorizeOutgoingSms(mAppInfo, mDestAddress, mFullMessageText, (SmsUsageMonitor.SmsAuthorizationCallback)localObject, this);
          }
          else
          {
            sendSms(paramSmsTracker);
          }
        }
        if (PhoneNumberUtils.isLocalEmergencyNumber(mContext, mDestAddress)) {
          new AsyncEmergencyContactNotifier(mContext).execute(new Void[0]);
        }
        return;
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
        Rlog.e("SMSDispatcher", "Can't get calling app package info: refusing to send SMS");
        paramSmsTracker.onFailed(mContext, 1, 0);
        return;
      }
    }
    Rlog.e("SMSDispatcher", "Can't get calling app package name: refusing to send SMS");
    paramSmsTracker.onFailed(mContext, 1, 0);
  }
  
  public void sendRetrySms(SmsTracker paramSmsTracker)
  {
    if (mSmsDispatchersController != null)
    {
      mSmsDispatchersController.sendRetrySms(paramSmsTracker);
    }
    else
    {
      paramSmsTracker = new StringBuilder();
      paramSmsTracker.append(mSmsDispatchersController);
      paramSmsTracker.append(" is null. Retry failed");
      Rlog.e("SMSDispatcher", paramSmsTracker.toString());
    }
  }
  
  protected abstract void sendSms(SmsTracker paramSmsTracker);
  
  public void sendText(String paramString1, String paramString2, String paramString3, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, Uri paramUri, String paramString4, boolean paramBoolean1, int paramInt1, boolean paramBoolean2, int paramInt2)
  {
    Rlog.d("SMSDispatcher", "sendText");
    boolean bool;
    if (paramPendingIntent2 != null) {
      bool = true;
    } else {
      bool = false;
    }
    paramString4 = getSubmitPdu(paramString2, paramString1, paramString3, bool, null, paramInt1, paramInt2);
    if (paramString4 != null)
    {
      paramString1 = getSmsTracker(getSmsTrackerMap(paramString1, paramString2, paramString3, paramString4), paramPendingIntent1, paramPendingIntent2, getFormat(), paramUri, paramBoolean2, paramString3, true, paramBoolean1, paramInt1, paramInt2);
      if (!sendSmsByCarrierApp(false, paramString1)) {
        sendSubmitPdu(paramString1);
      }
    }
    else
    {
      Rlog.e("SMSDispatcher", "SmsDispatcher.sendText(): getSubmitPdu() returned null");
      triggerSentIntentForFailure(paramPendingIntent1);
    }
  }
  
  protected abstract boolean shouldBlockSmsForEcbm();
  
  protected void updatePhoneObject(Phone paramPhone)
  {
    mPhone = paramPhone;
    paramPhone = new StringBuilder();
    paramPhone.append("Active phone changed to ");
    paramPhone.append(mPhone.getPhoneName());
    Rlog.d("SMSDispatcher", paramPhone.toString());
  }
  
  private final class ConfirmDialogListener
    implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener, CompoundButton.OnCheckedChangeListener
  {
    private static final int NEVER_ALLOW = 1;
    private static final int RATE_LIMIT = 1;
    private static final int SHORT_CODE_MSG = 0;
    private int mConfirmationType;
    private Button mNegativeButton;
    private Button mPositiveButton;
    private boolean mRememberChoice;
    private final TextView mRememberUndoInstruction;
    private final SMSDispatcher.SmsTracker mTracker;
    
    ConfirmDialogListener(SMSDispatcher.SmsTracker paramSmsTracker, TextView paramTextView, int paramInt)
    {
      mTracker = paramSmsTracker;
      mRememberUndoInstruction = paramTextView;
      mConfirmationType = paramInt;
    }
    
    public void onCancel(DialogInterface paramDialogInterface)
    {
      Rlog.d("SMSDispatcher", "dialog dismissed: don't send SMS");
      paramDialogInterface = obtainMessage(7, mTracker);
      arg1 = mConfirmationType;
      sendMessage(paramDialogInterface);
    }
    
    public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean)
    {
      paramCompoundButton = new StringBuilder();
      paramCompoundButton.append("remember this choice: ");
      paramCompoundButton.append(paramBoolean);
      Rlog.d("SMSDispatcher", paramCompoundButton.toString());
      mRememberChoice = paramBoolean;
      if (paramBoolean)
      {
        mPositiveButton.setText(17041033);
        mNegativeButton.setText(17041036);
        if (mRememberUndoInstruction != null)
        {
          mRememberUndoInstruction.setText(17041039);
          mRememberUndoInstruction.setPadding(0, 0, 0, 32);
        }
      }
      else
      {
        mPositiveButton.setText(17041032);
        mNegativeButton.setText(17041034);
        if (mRememberUndoInstruction != null)
        {
          mRememberUndoInstruction.setText("");
          mRememberUndoInstruction.setPadding(0, 0, 0, 0);
        }
      }
    }
    
    public void onClick(DialogInterface paramDialogInterface, int paramInt)
    {
      int i = 1;
      int j = 1;
      int k = -1;
      if (paramInt == -1)
      {
        Rlog.d("SMSDispatcher", "CONFIRM sending SMS");
        if (mTracker.mAppInfo.applicationInfo != null) {
          k = mTracker.mAppInfo.applicationInfo.uid;
        }
        EventLog.writeEvent(50128, k);
        sendMessage(obtainMessage(5, mTracker));
        if (mRememberChoice) {
          i = 3;
        }
      }
      else if (paramInt == -2)
      {
        Rlog.d("SMSDispatcher", "DENY sending SMS");
        if (mTracker.mAppInfo.applicationInfo != null) {
          k = mTracker.mAppInfo.applicationInfo.uid;
        }
        EventLog.writeEvent(50125, k);
        paramDialogInterface = obtainMessage(7, mTracker);
        arg1 = mConfirmationType;
        i = j;
        if (mRememberChoice)
        {
          i = 2;
          arg2 = 1;
        }
        sendMessage(paramDialogInterface);
      }
      mSmsDispatchersController.setPremiumSmsPermission(mTracker.getAppPackageName(), i);
    }
    
    void setNegativeButton(Button paramButton)
    {
      mNegativeButton = paramButton;
    }
    
    void setPositiveButton(Button paramButton)
    {
      mPositiveButton = paramButton;
    }
  }
  
  protected final class DataSmsSender
    extends SMSDispatcher.SmsSender
  {
    public DataSmsSender(SMSDispatcher.SmsTracker paramSmsTracker)
    {
      super(paramSmsTracker);
    }
    
    protected void onServiceReady(ICarrierMessagingService paramICarrierMessagingService)
    {
      HashMap localHashMap = mTracker.getData();
      Object localObject = (byte[])localHashMap.get("data");
      int i = ((Integer)localHashMap.get("destPort")).intValue();
      if (localObject != null) {
        try
        {
          paramICarrierMessagingService.sendDataSms((byte[])localObject, getSubId(), mTracker.mDestAddress, i, SMSDispatcher.getSendSmsFlag(mTracker.mDeliveryIntent), mSenderCallback);
        }
        catch (RemoteException paramICarrierMessagingService)
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Exception sending the SMS: ");
          ((StringBuilder)localObject).append(paramICarrierMessagingService);
          Rlog.e("SMSDispatcher", ((StringBuilder)localObject).toString());
          mSenderCallback.onSendSmsComplete(1, 0);
        }
      } else {
        mSenderCallback.onSendSmsComplete(1, 0);
      }
    }
  }
  
  private final class MultipartSmsSender
    extends CarrierMessagingServiceManager
  {
    private final List<String> mParts;
    private volatile SMSDispatcher.MultipartSmsSenderCallback mSenderCallback;
    public final SMSDispatcher.SmsTracker[] mTrackers;
    
    MultipartSmsSender(SMSDispatcher.SmsTracker[] paramArrayOfSmsTracker)
    {
      mParts = paramArrayOfSmsTracker;
      Object localObject;
      mTrackers = localObject;
    }
    
    protected void onServiceReady(ICarrierMessagingService paramICarrierMessagingService)
    {
      try
      {
        paramICarrierMessagingService.sendMultipartTextSms(mParts, getSubId(), mTrackers[0].mDestAddress, SMSDispatcher.getSendSmsFlag(mTrackers[0].mDeliveryIntent), mSenderCallback);
      }
      catch (RemoteException localRemoteException)
      {
        paramICarrierMessagingService = new StringBuilder();
        paramICarrierMessagingService.append("Exception sending the SMS: ");
        paramICarrierMessagingService.append(localRemoteException);
        Rlog.e("SMSDispatcher", paramICarrierMessagingService.toString());
        mSenderCallback.onSendMultipartSmsComplete(1, null);
      }
    }
    
    void sendSmsByCarrierApp(String paramString, SMSDispatcher.MultipartSmsSenderCallback paramMultipartSmsSenderCallback)
    {
      mSenderCallback = paramMultipartSmsSenderCallback;
      if (!bindToCarrierMessagingService(mContext, paramString))
      {
        Rlog.e("SMSDispatcher", "bindService() for carrier messaging service failed");
        mSenderCallback.onSendMultipartSmsComplete(1, null);
      }
      else
      {
        Rlog.d("SMSDispatcher", "bindService() for carrier messaging service succeeded");
      }
    }
  }
  
  private final class MultipartSmsSenderCallback
    extends ICarrierMessagingCallback.Stub
  {
    private final SMSDispatcher.MultipartSmsSender mSmsSender;
    
    MultipartSmsSenderCallback(SMSDispatcher.MultipartSmsSender paramMultipartSmsSender)
    {
      mSmsSender = paramMultipartSmsSender;
    }
    
    public void onDownloadMmsComplete(int paramInt)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unexpected onDownloadMmsComplete call with result: ");
      localStringBuilder.append(paramInt);
      Rlog.e("SMSDispatcher", localStringBuilder.toString());
    }
    
    public void onFilterComplete(int paramInt)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unexpected onFilterComplete call with result: ");
      localStringBuilder.append(paramInt);
      Rlog.e("SMSDispatcher", localStringBuilder.toString());
    }
    
    public void onSendMmsComplete(int paramInt, byte[] paramArrayOfByte)
    {
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("Unexpected onSendMmsComplete call with result: ");
      paramArrayOfByte.append(paramInt);
      Rlog.e("SMSDispatcher", paramArrayOfByte.toString());
    }
    
    public void onSendMultipartSmsComplete(int paramInt, int[] paramArrayOfInt)
    {
      mSmsSender.disposeConnection(mContext);
      if (mSmsSender.mTrackers == null)
      {
        Rlog.e("SMSDispatcher", "Unexpected onSendMultipartSmsComplete call with null trackers.");
        return;
      }
      SMSDispatcher.this.checkCallerIsPhoneOrCarrierApp();
      long l = Binder.clearCallingIdentity();
      int i = 0;
      try
      {
        while (i < mSmsSender.mTrackers.length)
        {
          int j = 0;
          int k = j;
          if (paramArrayOfInt != null)
          {
            k = j;
            if (paramArrayOfInt.length > i) {
              k = paramArrayOfInt[i];
            }
          }
          SMSDispatcher.this.processSendSmsResponse(mSmsSender.mTrackers[i], paramInt, k);
          i++;
        }
        return;
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
    
    public void onSendSmsComplete(int paramInt1, int paramInt2)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unexpected onSendSmsComplete call with result: ");
      localStringBuilder.append(paramInt1);
      Rlog.e("SMSDispatcher", localStringBuilder.toString());
    }
  }
  
  private static class SettingsObserver
    extends ContentObserver
  {
    private final Context mContext;
    private final AtomicInteger mPremiumSmsRule;
    
    SettingsObserver(Handler paramHandler, AtomicInteger paramAtomicInteger, Context paramContext)
    {
      super();
      mPremiumSmsRule = paramAtomicInteger;
      mContext = paramContext;
      onChange(false);
    }
    
    public void onChange(boolean paramBoolean)
    {
      mPremiumSmsRule.set(Settings.Global.getInt(mContext.getContentResolver(), "sms_short_code_rule", 1));
    }
  }
  
  protected abstract class SmsSender
    extends CarrierMessagingServiceManager
  {
    protected volatile SMSDispatcher.SmsSenderCallback mSenderCallback;
    protected final SMSDispatcher.SmsTracker mTracker;
    
    protected SmsSender(SMSDispatcher.SmsTracker paramSmsTracker)
    {
      mTracker = paramSmsTracker;
    }
    
    public void sendSmsByCarrierApp(String paramString, SMSDispatcher.SmsSenderCallback paramSmsSenderCallback)
    {
      mSenderCallback = paramSmsSenderCallback;
      if (!bindToCarrierMessagingService(mContext, paramString))
      {
        Rlog.e("SMSDispatcher", "bindService() for carrier messaging service failed");
        mSenderCallback.onSendSmsComplete(1, 0);
      }
      else
      {
        Rlog.d("SMSDispatcher", "bindService() for carrier messaging service succeeded");
      }
    }
  }
  
  protected final class SmsSenderCallback
    extends ICarrierMessagingCallback.Stub
  {
    private final SMSDispatcher.SmsSender mSmsSender;
    
    public SmsSenderCallback(SMSDispatcher.SmsSender paramSmsSender)
    {
      mSmsSender = paramSmsSender;
    }
    
    public void onDownloadMmsComplete(int paramInt)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unexpected onDownloadMmsComplete call with result: ");
      localStringBuilder.append(paramInt);
      Rlog.e("SMSDispatcher", localStringBuilder.toString());
    }
    
    public void onFilterComplete(int paramInt)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unexpected onFilterComplete call with result: ");
      localStringBuilder.append(paramInt);
      Rlog.e("SMSDispatcher", localStringBuilder.toString());
    }
    
    public void onSendMmsComplete(int paramInt, byte[] paramArrayOfByte)
    {
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("Unexpected onSendMmsComplete call with result: ");
      paramArrayOfByte.append(paramInt);
      Rlog.e("SMSDispatcher", paramArrayOfByte.toString());
    }
    
    public void onSendMultipartSmsComplete(int paramInt, int[] paramArrayOfInt)
    {
      paramArrayOfInt = new StringBuilder();
      paramArrayOfInt.append("Unexpected onSendMultipartSmsComplete call with result: ");
      paramArrayOfInt.append(paramInt);
      Rlog.e("SMSDispatcher", paramArrayOfInt.toString());
    }
    
    public void onSendSmsComplete(int paramInt1, int paramInt2)
    {
      SMSDispatcher.this.checkCallerIsPhoneOrCarrierApp();
      long l = Binder.clearCallingIdentity();
      try
      {
        mSmsSender.disposeConnection(mContext);
        SMSDispatcher.this.processSendSmsResponse(mSmsSender.mTracker, paramInt1, paramInt2);
        return;
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
  }
  
  public static class SmsTracker
  {
    private AtomicBoolean mAnyPartFailed;
    public final PackageInfo mAppInfo;
    private final HashMap<String, Object> mData;
    public final PendingIntent mDeliveryIntent;
    public final String mDestAddress;
    public boolean mExpectMore;
    String mFormat;
    private String mFullMessageText;
    public int mImsRetry;
    public boolean mIsFallBackRetry;
    private boolean mIsText;
    public int mMessageRef;
    public Uri mMessageUri;
    private boolean mPersistMessage;
    public int mPriority;
    public int mRetryCount;
    public final PendingIntent mSentIntent;
    public final SmsHeader mSmsHeader;
    private int mSubId;
    private long mTimestamp = System.currentTimeMillis();
    private AtomicInteger mUnsentPartCount;
    private final int mUserId;
    public boolean mUsesImsServiceForIms;
    public int mValidityPeriod;
    
    private SmsTracker(HashMap<String, Object> paramHashMap, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, PackageInfo paramPackageInfo, String paramString1, String paramString2, AtomicInteger paramAtomicInteger, AtomicBoolean paramAtomicBoolean, Uri paramUri, SmsHeader paramSmsHeader, boolean paramBoolean1, String paramString3, int paramInt1, boolean paramBoolean2, boolean paramBoolean3, int paramInt2, int paramInt3, int paramInt4)
    {
      mData = paramHashMap;
      mSentIntent = paramPendingIntent1;
      mDeliveryIntent = paramPendingIntent2;
      mRetryCount = 0;
      mAppInfo = paramPackageInfo;
      mDestAddress = paramString1;
      mFormat = paramString2;
      mExpectMore = paramBoolean1;
      mImsRetry = 0;
      mUsesImsServiceForIms = false;
      mMessageRef = 0;
      mUnsentPartCount = paramAtomicInteger;
      mAnyPartFailed = paramAtomicBoolean;
      mMessageUri = paramUri;
      mSmsHeader = paramSmsHeader;
      mFullMessageText = paramString3;
      mSubId = paramInt1;
      mIsText = paramBoolean2;
      mPersistMessage = paramBoolean3;
      mUserId = paramInt2;
      mPriority = paramInt3;
      mValidityPeriod = paramInt4;
      mIsFallBackRetry = false;
    }
    
    private void persistOrUpdateMessage(Context paramContext, int paramInt1, int paramInt2)
    {
      if (mMessageUri != null) {
        updateMessageState(paramContext, paramInt1, paramInt2);
      } else {
        mMessageUri = persistSentMessageIfRequired(paramContext, paramInt1, paramInt2);
      }
    }
    
    /* Error */
    private Uri persistSentMessageIfRequired(Context paramContext, int paramInt1, int paramInt2)
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 92	com/android/internal/telephony/SMSDispatcher$SmsTracker:mIsText	Z
      //   4: ifeq +332 -> 336
      //   7: aload_0
      //   8: getfield 94	com/android/internal/telephony/SMSDispatcher$SmsTracker:mPersistMessage	Z
      //   11: ifeq +325 -> 336
      //   14: aload_0
      //   15: getfield 66	com/android/internal/telephony/SMSDispatcher$SmsTracker:mAppInfo	Landroid/content/pm/PackageInfo;
      //   18: getfield 132	android/content/pm/PackageInfo:packageName	Ljava/lang/String;
      //   21: aload_1
      //   22: invokestatic 138	com/android/internal/telephony/SmsApplication:shouldWriteMessageForPackage	(Ljava/lang/String;Landroid/content/Context;)Z
      //   25: ifne +6 -> 31
      //   28: goto +308 -> 336
      //   31: new 140	java/lang/StringBuilder
      //   34: dup
      //   35: invokespecial 141	java/lang/StringBuilder:<init>	()V
      //   38: astore 4
      //   40: aload 4
      //   42: ldc -113
      //   44: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   47: pop
      //   48: iload_2
      //   49: iconst_5
      //   50: if_icmpne +10 -> 60
      //   53: ldc -107
      //   55: astore 5
      //   57: goto +7 -> 64
      //   60: ldc -105
      //   62: astore 5
      //   64: aload 4
      //   66: aload 5
      //   68: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   71: pop
      //   72: ldc -103
      //   74: aload 4
      //   76: invokevirtual 157	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   79: invokestatic 163	android/telephony/Rlog:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   82: pop
      //   83: new 165	android/content/ContentValues
      //   86: dup
      //   87: invokespecial 166	android/content/ContentValues:<init>	()V
      //   90: astore 4
      //   92: aload 4
      //   94: ldc -88
      //   96: aload_0
      //   97: getfield 90	com/android/internal/telephony/SMSDispatcher$SmsTracker:mSubId	I
      //   100: invokestatic 174	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
      //   103: invokevirtual 178	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
      //   106: aload 4
      //   108: ldc -76
      //   110: aload_0
      //   111: getfield 68	com/android/internal/telephony/SMSDispatcher$SmsTracker:mDestAddress	Ljava/lang/String;
      //   114: invokevirtual 183	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
      //   117: aload 4
      //   119: ldc -71
      //   121: aload_0
      //   122: getfield 88	com/android/internal/telephony/SMSDispatcher$SmsTracker:mFullMessageText	Ljava/lang/String;
      //   125: invokevirtual 183	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
      //   128: aload 4
      //   130: ldc -69
      //   132: invokestatic 54	java/lang/System:currentTimeMillis	()J
      //   135: invokestatic 192	java/lang/Long:valueOf	(J)Ljava/lang/Long;
      //   138: invokevirtual 195	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Long;)V
      //   141: aload 4
      //   143: ldc -59
      //   145: iconst_1
      //   146: invokestatic 174	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
      //   149: invokevirtual 178	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
      //   152: aload 4
      //   154: ldc -57
      //   156: iconst_1
      //   157: invokestatic 174	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
      //   160: invokevirtual 178	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
      //   163: aload_0
      //   164: getfield 66	com/android/internal/telephony/SMSDispatcher$SmsTracker:mAppInfo	Landroid/content/pm/PackageInfo;
      //   167: ifnull +15 -> 182
      //   170: aload_0
      //   171: getfield 66	com/android/internal/telephony/SMSDispatcher$SmsTracker:mAppInfo	Landroid/content/pm/PackageInfo;
      //   174: getfield 132	android/content/pm/PackageInfo:packageName	Ljava/lang/String;
      //   177: astore 5
      //   179: goto +6 -> 185
      //   182: aconst_null
      //   183: astore 5
      //   185: aload 5
      //   187: invokestatic 205	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
      //   190: ifne +12 -> 202
      //   193: aload 4
      //   195: ldc -49
      //   197: aload 5
      //   199: invokevirtual 183	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
      //   202: aload_0
      //   203: getfield 62	com/android/internal/telephony/SMSDispatcher$SmsTracker:mDeliveryIntent	Landroid/app/PendingIntent;
      //   206: ifnull +15 -> 221
      //   209: aload 4
      //   211: ldc -47
      //   213: bipush 32
      //   215: invokestatic 174	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
      //   218: invokevirtual 178	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
      //   221: iload_3
      //   222: ifeq +14 -> 236
      //   225: aload 4
      //   227: ldc -45
      //   229: iload_3
      //   230: invokestatic 174	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
      //   233: invokevirtual 178	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
      //   236: invokestatic 216	android/os/Binder:clearCallingIdentity	()J
      //   239: lstore 6
      //   241: aload_1
      //   242: invokevirtual 222	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
      //   245: astore_1
      //   246: aload_1
      //   247: getstatic 227	android/provider/Telephony$Sms$Sent:CONTENT_URI	Landroid/net/Uri;
      //   250: aload 4
      //   252: invokevirtual 233	android/content/ContentResolver:insert	(Landroid/net/Uri;Landroid/content/ContentValues;)Landroid/net/Uri;
      //   255: astore 5
      //   257: aload 5
      //   259: ifnull +41 -> 300
      //   262: iload_2
      //   263: iconst_5
      //   264: if_icmpne +36 -> 300
      //   267: new 165	android/content/ContentValues
      //   270: astore 4
      //   272: aload 4
      //   274: iconst_1
      //   275: invokespecial 236	android/content/ContentValues:<init>	(I)V
      //   278: aload 4
      //   280: ldc -18
      //   282: iconst_5
      //   283: invokestatic 174	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
      //   286: invokevirtual 178	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
      //   289: aload_1
      //   290: aload 5
      //   292: aload 4
      //   294: aconst_null
      //   295: aconst_null
      //   296: invokevirtual 242	android/content/ContentResolver:update	(Landroid/net/Uri;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
      //   299: pop
      //   300: lload 6
      //   302: invokestatic 246	android/os/Binder:restoreCallingIdentity	(J)V
      //   305: aload 5
      //   307: areturn
      //   308: astore_1
      //   309: goto +20 -> 329
      //   312: astore_1
      //   313: ldc -103
      //   315: ldc -8
      //   317: aload_1
      //   318: invokestatic 252	android/telephony/Rlog:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   321: pop
      //   322: lload 6
      //   324: invokestatic 246	android/os/Binder:restoreCallingIdentity	(J)V
      //   327: aconst_null
      //   328: areturn
      //   329: lload 6
      //   331: invokestatic 246	android/os/Binder:restoreCallingIdentity	(J)V
      //   334: aload_1
      //   335: athrow
      //   336: aconst_null
      //   337: areturn
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	338	0	this	SmsTracker
      //   0	338	1	paramContext	Context
      //   0	338	2	paramInt1	int
      //   0	338	3	paramInt2	int
      //   38	255	4	localObject1	Object
      //   55	251	5	localObject2	Object
      //   239	91	6	l	long
      // Exception table:
      //   from	to	target	type
      //   246	257	308	finally
      //   267	300	308	finally
      //   313	322	308	finally
      //   246	257	312	java/lang/Exception
      //   267	300	312	java/lang/Exception
    }
    
    private void updateMessageState(Context paramContext, int paramInt1, int paramInt2)
    {
      if (mMessageUri == null) {
        return;
      }
      ContentValues localContentValues = new ContentValues(2);
      localContentValues.put("type", Integer.valueOf(paramInt1));
      localContentValues.put("error_code", Integer.valueOf(paramInt2));
      long l = Binder.clearCallingIdentity();
      try
      {
        if (SqliteWrapper.update(paramContext, paramContext.getContentResolver(), mMessageUri, localContentValues, null, null) != 1)
        {
          paramContext = new java/lang/StringBuilder;
          paramContext.<init>();
          paramContext.append("Failed to move message to ");
          paramContext.append(paramInt1);
          Rlog.e("SMSDispatcher", paramContext.toString());
        }
        return;
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
    
    public String getAppPackageName()
    {
      String str;
      if (mAppInfo != null) {
        str = mAppInfo.packageName;
      } else {
        str = null;
      }
      return str;
    }
    
    public HashMap<String, Object> getData()
    {
      return mData;
    }
    
    boolean isMultipart()
    {
      return mData.containsKey("parts");
    }
    
    public void onFailed(Context paramContext, int paramInt1, int paramInt2)
    {
      if (mAnyPartFailed != null) {
        mAnyPartFailed.set(true);
      }
      int i = 1;
      if (mUnsentPartCount != null) {
        if (mUnsentPartCount.decrementAndGet() == 0) {
          i = 1;
        } else {
          i = 0;
        }
      }
      if (i != 0) {
        persistOrUpdateMessage(paramContext, 5, paramInt2);
      }
      if (mSentIntent != null) {
        try
        {
          Intent localIntent = new android/content/Intent;
          localIntent.<init>();
          if (mMessageUri != null) {
            localIntent.putExtra("uri", mMessageUri.toString());
          }
          if (paramInt2 != 0) {
            localIntent.putExtra("errorCode", paramInt2);
          }
          if ((mUnsentPartCount != null) && (i != 0)) {
            localIntent.putExtra("SendNextMsg", true);
          }
          mSentIntent.send(paramContext, paramInt1, localIntent);
        }
        catch (PendingIntent.CanceledException paramContext)
        {
          Rlog.e("SMSDispatcher", "Failed to send result");
        }
      }
    }
    
    public void onSent(Context paramContext)
    {
      int i = 1;
      if (mUnsentPartCount != null) {
        if (mUnsentPartCount.decrementAndGet() == 0) {
          i = 1;
        } else {
          i = 0;
        }
      }
      if (i != 0)
      {
        int j = 2;
        int k = j;
        if (mAnyPartFailed != null)
        {
          k = j;
          if (mAnyPartFailed.get()) {
            k = 5;
          }
        }
        persistOrUpdateMessage(paramContext, k, 0);
      }
      if (mSentIntent != null) {
        try
        {
          Intent localIntent = new android/content/Intent;
          localIntent.<init>();
          if (mMessageUri != null) {
            localIntent.putExtra("uri", mMessageUri.toString());
          }
          if ((mUnsentPartCount != null) && (i != 0)) {
            localIntent.putExtra("SendNextMsg", true);
          }
          mSentIntent.send(paramContext, -1, localIntent);
        }
        catch (PendingIntent.CanceledException paramContext)
        {
          Rlog.e("SMSDispatcher", "Failed to send result");
        }
      }
    }
    
    public void updateSentMessageStatus(Context paramContext, int paramInt)
    {
      if (mMessageUri != null)
      {
        ContentValues localContentValues = new ContentValues(1);
        localContentValues.put("status", Integer.valueOf(paramInt));
        SqliteWrapper.update(paramContext, paramContext.getContentResolver(), mMessageUri, localContentValues, null, null);
      }
    }
  }
  
  protected final class TextSmsSender
    extends SMSDispatcher.SmsSender
  {
    public TextSmsSender(SMSDispatcher.SmsTracker paramSmsTracker)
    {
      super(paramSmsTracker);
    }
    
    protected void onServiceReady(ICarrierMessagingService paramICarrierMessagingService)
    {
      String str = (String)mTracker.getData().get("text");
      if (str != null) {
        try
        {
          paramICarrierMessagingService.sendTextSms(str, getSubId(), mTracker.mDestAddress, SMSDispatcher.getSendSmsFlag(mTracker.mDeliveryIntent), mSenderCallback);
        }
        catch (RemoteException localRemoteException)
        {
          paramICarrierMessagingService = new StringBuilder();
          paramICarrierMessagingService.append("Exception sending the SMS: ");
          paramICarrierMessagingService.append(localRemoteException);
          Rlog.e("SMSDispatcher", paramICarrierMessagingService.toString());
          mSenderCallback.onSendSmsComplete(1, 0);
        }
      } else {
        mSenderCallback.onSendSmsComplete(1, 0);
      }
    }
  }
}
