package com.android.internal.telephony;

import android.app.AppOpsManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.Settings.Global;
import android.provider.Settings.SettingNotFoundException;
import android.telephony.RadioAccessFamily;
import android.telephony.Rlog;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.UiccAccessRule;
import android.telephony.euicc.EuiccManager;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SubscriptionController
  extends ISub.Stub
{
  protected static final boolean DBG = true;
  static final boolean DBG_CACHE = false;
  private static final int DEPRECATED_SETTING = -1;
  static final String LOG_TAG = "SubscriptionController";
  static final int MAX_LOCAL_LOG_LINES = 500;
  private static final Comparator<SubscriptionInfo> SUBSCRIPTION_INFO_COMPARATOR = _..Lambda.SubscriptionController.Nt_ojdeqo4C2mbuwymYLvwgOLGo.INSTANCE;
  protected static final boolean VDBG = false;
  protected static int mDefaultFallbackSubId = -1;
  protected static int mDefaultPhoneId = Integer.MAX_VALUE;
  protected static SubscriptionController sInstance = null;
  protected static Phone[] sPhones;
  private static Map<Integer, Integer> sSlotIndexToSubId = new ConcurrentHashMap();
  private int[] colorArr;
  private AppOpsManager mAppOps;
  protected CallManager mCM;
  private final List<SubscriptionInfo> mCacheActiveSubInfoList = new ArrayList();
  protected Context mContext;
  private long mLastISubServiceRegTime;
  private ScLocalLog mLocalLog = new ScLocalLog(500);
  protected final Object mLock = new Object();
  protected TelephonyManager mTelephonyManager;
  
  protected SubscriptionController(Context paramContext)
  {
    init(paramContext);
    migrateImsSettings();
  }
  
  private SubscriptionController(Phone paramPhone)
  {
    mContext = paramPhone.getContext();
    mCM = CallManager.getInstance();
    mAppOps = ((AppOpsManager)mContext.getSystemService(AppOpsManager.class));
    if (ServiceManager.getService("isub") == null) {
      ServiceManager.addService("isub", this);
    }
    migrateImsSettings();
    logdl("[SubscriptionController] init by Phone");
  }
  
  private void broadcastDefaultSmsSubIdChanged(int paramInt)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("[broadcastDefaultSmsSubIdChanged] subId=");
    ((StringBuilder)localObject).append(paramInt);
    logdl(((StringBuilder)localObject).toString());
    localObject = new Intent("android.telephony.action.DEFAULT_SMS_SUBSCRIPTION_CHANGED");
    ((Intent)localObject).addFlags(553648128);
    ((Intent)localObject).putExtra("subscription", paramInt);
    ((Intent)localObject).putExtra("android.telephony.extra.SUBSCRIPTION_INDEX", paramInt);
    mContext.sendStickyBroadcastAsUser((Intent)localObject, UserHandle.ALL);
  }
  
  private void broadcastSimInfoContentChanged()
  {
    Intent localIntent = new Intent("android.intent.action.ACTION_SUBINFO_CONTENT_CHANGE");
    mContext.sendBroadcast(localIntent);
    localIntent = new Intent("android.intent.action.ACTION_SUBINFO_RECORD_UPDATED");
    mContext.sendBroadcast(localIntent);
  }
  
  private boolean canReadPhoneState(String paramString1, String paramString2)
  {
    boolean bool = true;
    try
    {
      mContext.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", paramString2);
      return true;
    }
    catch (SecurityException localSecurityException)
    {
      mContext.enforceCallingOrSelfPermission("android.permission.READ_PHONE_STATE", paramString2);
      if (mAppOps.noteOp(51, Binder.getCallingUid(), paramString1) != 0) {
        bool = false;
      }
    }
    return bool;
  }
  
  private SubscriptionInfo getActiveSubscriptionInfoForIccIdInternal(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    long l = Binder.clearCallingIdentity();
    try
    {
      Object localObject1 = getActiveSubscriptionInfoList(mContext.getOpPackageName());
      if (localObject1 != null)
      {
        Iterator localIterator = ((List)localObject1).iterator();
        while (localIterator.hasNext())
        {
          localObject2 = (SubscriptionInfo)localIterator.next();
          if (paramString.equals(((SubscriptionInfo)localObject2).getIccId()))
          {
            localObject1 = new java/lang/StringBuilder;
            ((StringBuilder)localObject1).<init>();
            ((StringBuilder)localObject1).append("[getActiveSubInfoUsingIccId]+ iccId=");
            ((StringBuilder)localObject1).append(paramString);
            ((StringBuilder)localObject1).append(" subInfo=");
            ((StringBuilder)localObject1).append(localObject2);
            logd(((StringBuilder)localObject1).toString());
            return localObject2;
          }
        }
      }
      Object localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>();
      ((StringBuilder)localObject2).append("[getActiveSubInfoUsingIccId]+ iccId=");
      ((StringBuilder)localObject2).append(paramString);
      ((StringBuilder)localObject2).append(" subList=");
      ((StringBuilder)localObject2).append(localObject1);
      ((StringBuilder)localObject2).append(" subInfo=null");
      logd(((StringBuilder)localObject2).toString());
      return null;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  public static SubscriptionController getInstance()
  {
    if (sInstance == null) {
      Log.wtf("SubscriptionController", "getInstance null");
    }
    return sInstance;
  }
  
  private List<SubscriptionInfo> getSubInfo(String paramString, Object paramObject)
  {
    Object localObject = null;
    if (paramObject != null) {
      localObject = new String[] { paramObject.toString() };
    }
    SubscriptionInfo localSubscriptionInfo = null;
    paramObject = null;
    localObject = mContext.getContentResolver().query(SubscriptionManager.CONTENT_URI, null, paramString, (String[])localObject, null);
    if (localObject != null)
    {
      paramString = paramObject;
      for (;;)
      {
        paramObject = paramString;
        try
        {
          if (!((Cursor)localObject).moveToNext()) {
            break label112;
          }
          localSubscriptionInfo = getSubInfoRecord((Cursor)localObject);
          paramObject = paramString;
          if (localSubscriptionInfo != null)
          {
            paramObject = paramString;
            if (paramString == null)
            {
              paramObject = new java/util/ArrayList;
              paramObject.<init>();
            }
            paramObject.add(localSubscriptionInfo);
          }
          paramString = paramObject;
        }
        finally
        {
          break label124;
        }
      }
    }
    logd("Query fail");
    paramObject = localSubscriptionInfo;
    label112:
    if (localObject != null) {
      ((Cursor)localObject).close();
    }
    return paramObject;
    label124:
    if (localObject != null) {
      ((Cursor)localObject).close();
    }
    throw paramString;
  }
  
  private SubscriptionInfo getSubInfoRecord(Cursor paramCursor)
  {
    int i = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("_id"));
    String str1 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("icc_id"));
    int j = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("sim_id"));
    String str2 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("display_name"));
    String str3 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("carrier_name"));
    int k = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("name_source"));
    int m = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("color"));
    String str4 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("number"));
    int n = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("data_roaming"));
    Bitmap localBitmap = BitmapFactory.decodeResource(mContext.getResources(), 17303024);
    int i1 = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("mcc"));
    int i2 = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("mnc"));
    String str5 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("card_id"));
    String str6 = getSubscriptionCountryIso(i);
    int i3 = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("is_embedded"));
    boolean bool = true;
    if (i3 != 1) {
      bool = false;
    }
    if (bool) {}
    for (paramCursor = UiccAccessRule.decodeRules(paramCursor.getBlob(paramCursor.getColumnIndexOrThrow("access_rules")));; paramCursor = null) {
      break;
    }
    String str7 = mTelephonyManager.getLine1Number(i);
    String str8 = str4;
    if (!TextUtils.isEmpty(str7))
    {
      str8 = str4;
      if (!str7.equals(str4)) {
        str8 = str7;
      }
    }
    return new SubscriptionInfo(i, str1, j, str2, str3, k, m, str8, n, localBitmap, i1, i2, str6, bool, paramCursor, str5);
  }
  
  private String getSubscriptionCountryIso(int paramInt)
  {
    paramInt = getPhoneId(paramInt);
    if (paramInt < 0) {
      return "";
    }
    return mTelephonyManager.getSimCountryIsoForPhone(paramInt);
  }
  
  private int getUnusedColor(String paramString)
  {
    paramString = getActiveSubscriptionInfoList(paramString);
    colorArr = mContext.getResources().getIntArray(17236081);
    int i = 0;
    if (paramString != null)
    {
      for (i = 0; i < colorArr.length; i++)
      {
        for (int j = 0; (j < paramString.size()) && (colorArr[i] != ((SubscriptionInfo)paramString.get(j)).getIconTint()); j++) {}
        if (j == paramString.size()) {
          return colorArr[i];
        }
      }
      i = paramString.size() % colorArr.length;
    }
    return colorArr[i];
  }
  
  public static SubscriptionController init(Context paramContext, CommandsInterface[] paramArrayOfCommandsInterface)
  {
    try
    {
      if (sInstance == null)
      {
        paramArrayOfCommandsInterface = new com/android/internal/telephony/SubscriptionController;
        paramArrayOfCommandsInterface.<init>(paramContext);
        sInstance = paramArrayOfCommandsInterface;
      }
      else
      {
        paramContext = new java/lang/StringBuilder;
        paramContext.<init>();
        paramContext.append("init() called multiple times!  sInstance = ");
        paramContext.append(sInstance);
        Log.wtf("SubscriptionController", paramContext.toString());
      }
      paramContext = sInstance;
      return paramContext;
    }
    finally {}
  }
  
  public static SubscriptionController init(Phone paramPhone)
  {
    try
    {
      if (sInstance == null)
      {
        SubscriptionController localSubscriptionController = new com/android/internal/telephony/SubscriptionController;
        localSubscriptionController.<init>(paramPhone);
        sInstance = localSubscriptionController;
      }
      else
      {
        paramPhone = new java/lang/StringBuilder;
        paramPhone.<init>();
        paramPhone.append("init() called multiple times!  sInstance = ");
        paramPhone.append(sInstance);
        Log.wtf("SubscriptionController", paramPhone.toString());
      }
      paramPhone = sInstance;
      return paramPhone;
    }
    finally {}
  }
  
  private boolean isSubInfoReady()
  {
    boolean bool;
    if (sSlotIndexToSubId.size() > 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void logd(String paramString)
  {
    Rlog.d("SubscriptionController", paramString);
  }
  
  private void loge(String paramString)
  {
    Rlog.e("SubscriptionController", paramString);
  }
  
  private void migrateImsSettingHelper(String paramString1, String paramString2, int paramInt)
  {
    ContentResolver localContentResolver = mContext.getContentResolver();
    try
    {
      int i = Settings.Global.getInt(localContentResolver, paramString1);
      if (i != -1)
      {
        setSubscriptionPropertyIntoContentResolver(paramInt, paramString2, Integer.toString(i), localContentResolver);
        Settings.Global.putInt(localContentResolver, paramString1, -1);
      }
    }
    catch (Settings.SettingNotFoundException paramString1) {}
  }
  
  protected static void printStackTrace(String paramString)
  {
    RuntimeException localRuntimeException = new RuntimeException();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("StackTrace - ");
    localStringBuilder.append(paramString);
    slogd(localStringBuilder.toString());
    paramString = localRuntimeException.getStackTrace();
    int i = 1;
    int j = paramString.length;
    for (int k = 0; k < j; k++)
    {
      localStringBuilder = paramString[k];
      if (i != 0) {
        i = 0;
      } else {
        slogd(localStringBuilder.toString());
      }
    }
  }
  
  private int setCarrierText(String paramString, int paramInt)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("[setCarrierText]+ text:");
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append(" subId:");
    ((StringBuilder)localObject).append(paramInt);
    logd(((StringBuilder)localObject).toString());
    enforceModifyPhoneState("setCarrierText");
    long l = Binder.clearCallingIdentity();
    try
    {
      localObject = new android/content/ContentValues;
      ((ContentValues)localObject).<init>(1);
      ((ContentValues)localObject).put("carrier_name", paramString);
      paramString = mContext.getContentResolver();
      Uri localUri = SubscriptionManager.CONTENT_URI;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("_id=");
      localStringBuilder.append(Long.toString(paramInt));
      paramInt = paramString.update(localUri, (ContentValues)localObject, localStringBuilder.toString(), null);
      refreshCachedActiveSubscriptionInfoList();
      notifySubscriptionInfoChanged();
      return paramInt;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  private static void setSubscriptionPropertyIntoContentResolver(int paramInt, String paramString1, String paramString2, ContentResolver paramContentResolver)
  {
    ContentValues localContentValues = new ContentValues();
    switch (paramString1.hashCode())
    {
    default: 
      break;
    case 1604840288: 
      if (paramString1.equals("wfc_ims_roaming_enabled")) {
        i = 17;
      }
      break;
    case 1334635646: 
      if (paramString1.equals("wfc_ims_mode")) {
        i = 15;
      }
      break;
    case 1288054979: 
      if (paramString1.equals("enable_channel_50_alerts")) {
        i = 9;
      }
      break;
    case 1270593452: 
      if (paramString1.equals("enable_etws_test_alerts")) {
        i = 8;
      }
      break;
    case 462555599: 
      if (paramString1.equals("alert_reminder_interval")) {
        i = 5;
      }
      break;
    case 407275608: 
      if (paramString1.equals("enable_cmas_severe_threat_alerts")) {
        i = 1;
      }
      break;
    case 240841894: 
      if (paramString1.equals("show_cmas_opt_out_dialog")) {
        i = 11;
      }
      break;
    case 203677434: 
      if (paramString1.equals("enable_cmas_amber_alerts")) {
        i = 2;
      }
      break;
    case 180938212: 
      if (paramString1.equals("wfc_ims_roaming_mode")) {
        i = 16;
      }
      break;
    case -349439993: 
      if (paramString1.equals("alert_sound_duration")) {
        i = 4;
      }
      break;
    case -420099376: 
      if (paramString1.equals("vt_ims_enabled")) {
        i = 13;
      }
      break;
    case -461686719: 
      if (paramString1.equals("enable_emergency_alerts")) {
        i = 3;
      }
      break;
    case -1218173306: 
      if (paramString1.equals("wfc_ims_enabled")) {
        i = 14;
      }
      break;
    case -1390801311: 
      if (paramString1.equals("enable_alert_speech")) {
        i = 7;
      }
      break;
    case -1433878403: 
      if (paramString1.equals("enable_cmas_test_alerts")) {
        i = 10;
      }
      break;
    case -1555340190: 
      if (paramString1.equals("enable_cmas_extreme_threat_alerts")) {
        i = 0;
      }
      break;
    case -1950380197: 
      if (paramString1.equals("volte_vt_enabled")) {
        i = 12;
      }
      break;
    case -2000412720: 
      if (paramString1.equals("enable_alert_vibrate")) {
        i = 6;
      }
      break;
    }
    int i = -1;
    switch (i)
    {
    default: 
      slogd("Invalid column name");
      break;
    case 0: 
    case 1: 
    case 2: 
    case 3: 
    case 4: 
    case 5: 
    case 6: 
    case 7: 
    case 8: 
    case 9: 
    case 10: 
    case 11: 
    case 12: 
    case 13: 
    case 14: 
    case 15: 
    case 16: 
    case 17: 
      localContentValues.put(paramString1, Integer.valueOf(Integer.parseInt(paramString2)));
    }
    paramString1 = SubscriptionManager.CONTENT_URI;
    paramString2 = new StringBuilder();
    paramString2.append("_id=");
    paramString2.append(Integer.toString(paramInt));
    paramContentResolver.update(paramString1, localContentValues, paramString2.toString(), null);
  }
  
  protected static void slogd(String paramString)
  {
    Rlog.d("SubscriptionController", paramString);
  }
  
  private void validateSubId(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("validateSubId subId: ");
    localStringBuilder.append(paramInt);
    logd(localStringBuilder.toString());
    if (SubscriptionManager.isValidSubscriptionId(paramInt))
    {
      if (paramInt != Integer.MAX_VALUE) {
        return;
      }
      throw new RuntimeException("Default sub id passed as parameter");
    }
    throw new RuntimeException("Invalid sub id passed as parameter");
  }
  
  /* Error */
  public int addSubInfoRecord(String paramString, int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: astore_3
    //   2: iload_2
    //   3: invokestatic 620	com/android/internal/telephony/PhoneFactory:getPhone	(I)Lcom/android/internal/telephony/Phone;
    //   6: astore 4
    //   8: invokestatic 625	com/android/internal/telephony/uicc/UiccController:getInstance	()Lcom/android/internal/telephony/uicc/UiccController;
    //   11: iload_2
    //   12: invokevirtual 629	com/android/internal/telephony/uicc/UiccController:getUiccCardForSlot	(I)Lcom/android/internal/telephony/uicc/UiccCard;
    //   15: astore 5
    //   17: aload 4
    //   19: ifnull +2104 -> 2123
    //   22: aload 5
    //   24: ifnull +2099 -> 2123
    //   27: aload 4
    //   29: invokevirtual 632	com/android/internal/telephony/Phone:getFullIccSerialNumber	()Ljava/lang/String;
    //   32: astore 4
    //   34: aload 4
    //   36: astore 5
    //   38: aload 4
    //   40: invokestatic 405	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   43: ifeq +13 -> 56
    //   46: aload_3
    //   47: ldc_w 634
    //   50: invokevirtual 146	com/android/internal/telephony/SubscriptionController:logdl	(Ljava/lang/String;)V
    //   53: aload_1
    //   54: astore 5
    //   56: new 149	java/lang/StringBuilder
    //   59: dup
    //   60: invokespecial 150	java/lang/StringBuilder:<init>	()V
    //   63: astore 4
    //   65: aload 4
    //   67: ldc_w 636
    //   70: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   73: pop
    //   74: aload 4
    //   76: aload_1
    //   77: invokestatic 640	android/telephony/SubscriptionInfo:givePrintableIccid	(Ljava/lang/String;)Ljava/lang/String;
    //   80: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   83: pop
    //   84: aload 4
    //   86: ldc_w 642
    //   89: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   92: pop
    //   93: aload 4
    //   95: iload_2
    //   96: invokevirtual 159	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   99: pop
    //   100: aload_3
    //   101: aload 4
    //   103: invokevirtual 163	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   106: invokevirtual 146	com/android/internal/telephony/SubscriptionController:logdl	(Ljava/lang/String;)V
    //   109: aload_3
    //   110: ldc_w 643
    //   113: invokevirtual 526	com/android/internal/telephony/SubscriptionController:enforceModifyPhoneState	(Ljava/lang/String;)V
    //   116: invokestatic 229	android/os/Binder:clearCallingIdentity	()J
    //   119: lstore 6
    //   121: aload_1
    //   122: ifnonnull +21 -> 143
    //   125: aload_3
    //   126: ldc_w 645
    //   129: invokevirtual 146	com/android/internal/telephony/SubscriptionController:logdl	(Ljava/lang/String;)V
    //   132: lload 6
    //   134: invokestatic 277	android/os/Binder:restoreCallingIdentity	(J)V
    //   137: iconst_m1
    //   138: ireturn
    //   139: astore_1
    //   140: goto +1976 -> 2116
    //   143: aload_3
    //   144: getfield 110	com/android/internal/telephony/SubscriptionController:mContext	Landroid/content/Context;
    //   147: invokevirtual 297	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   150: astore 8
    //   152: ldc_w 413
    //   155: astore 9
    //   157: invokestatic 649	android/telephony/TelephonyManager:getDefault	()Landroid/telephony/TelephonyManager;
    //   160: invokevirtual 652	android/telephony/TelephonyManager:getPhoneCount	()I
    //   163: istore 10
    //   165: iload 10
    //   167: iconst_2
    //   168: if_icmpne +741 -> 909
    //   171: iload_2
    //   172: ifeq +18 -> 190
    //   175: iload_2
    //   176: iconst_1
    //   177: if_icmpne +6 -> 183
    //   180: goto +10 -> 190
    //   183: aload 5
    //   185: astore 4
    //   187: goto +726 -> 913
    //   190: iconst_1
    //   191: iload_2
    //   192: isub
    //   193: invokestatic 620	com/android/internal/telephony/PhoneFactory:getPhone	(I)Lcom/android/internal/telephony/Phone;
    //   196: astore 11
    //   198: invokestatic 625	com/android/internal/telephony/uicc/UiccController:getInstance	()Lcom/android/internal/telephony/uicc/UiccController;
    //   201: iconst_1
    //   202: iload_2
    //   203: isub
    //   204: invokevirtual 629	com/android/internal/telephony/uicc/UiccController:getUiccCardForSlot	(I)Lcom/android/internal/telephony/uicc/UiccCard;
    //   207: astore 4
    //   209: aload 11
    //   211: ifnull +698 -> 909
    //   214: aload 4
    //   216: ifnull +693 -> 909
    //   219: aload 11
    //   221: invokevirtual 632	com/android/internal/telephony/Phone:getFullIccSerialNumber	()Ljava/lang/String;
    //   224: astore 12
    //   226: new 149	java/lang/StringBuilder
    //   229: astore 4
    //   231: aload 4
    //   233: invokespecial 150	java/lang/StringBuilder:<init>	()V
    //   236: aload 4
    //   238: ldc_w 654
    //   241: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   244: pop
    //   245: aload 4
    //   247: aload 12
    //   249: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   252: pop
    //   253: aload 4
    //   255: ldc_w 656
    //   258: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   261: pop
    //   262: aload 4
    //   264: aload 5
    //   266: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   269: pop
    //   270: aload 4
    //   272: ldc_w 658
    //   275: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   278: pop
    //   279: aload 4
    //   281: aload_1
    //   282: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   285: pop
    //   286: aload_3
    //   287: aload 4
    //   289: invokevirtual 163	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   292: invokevirtual 146	com/android/internal/telephony/SubscriptionController:logdl	(Ljava/lang/String;)V
    //   295: aload 12
    //   297: invokestatic 405	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   300: ifne +598 -> 898
    //   303: aload 12
    //   305: aload 5
    //   307: invokevirtual 263	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   310: ifeq +588 -> 898
    //   313: aload 5
    //   315: aload_1
    //   316: invokevirtual 263	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   319: ifne +579 -> 898
    //   322: aload 5
    //   324: invokestatic 663	com/android/internal/telephony/uicc/IccUtils:stripTrailingFs	(Ljava/lang/String;)Ljava/lang/String;
    //   327: astore 13
    //   329: aload 5
    //   331: invokestatic 666	com/android/internal/telephony/uicc/IccUtils:getDecimalSubstring	(Ljava/lang/String;)Ljava/lang/String;
    //   334: astore 11
    //   336: iconst_1
    //   337: istore 10
    //   339: aload 13
    //   341: invokevirtual 669	java/lang/String:length	()I
    //   344: istore 14
    //   346: aload 5
    //   348: invokevirtual 669	java/lang/String:length	()I
    //   351: istore 15
    //   353: iload 14
    //   355: iload 15
    //   357: if_icmpeq +214 -> 571
    //   360: aload_1
    //   361: invokevirtual 669	java/lang/String:length	()I
    //   364: aload 13
    //   366: invokevirtual 669	java/lang/String:length	()I
    //   369: isub
    //   370: iconst_1
    //   371: if_icmpne +200 -> 571
    //   374: new 149	java/lang/StringBuilder
    //   377: astore 4
    //   379: aload 4
    //   381: invokespecial 150	java/lang/StringBuilder:<init>	()V
    //   384: aload 4
    //   386: aload 5
    //   388: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   391: pop
    //   392: aload_1
    //   393: invokevirtual 669	java/lang/String:length	()I
    //   396: istore 15
    //   398: aload 4
    //   400: aload_1
    //   401: iload 15
    //   403: iconst_1
    //   404: isub
    //   405: aload_1
    //   406: invokevirtual 669	java/lang/String:length	()I
    //   409: invokevirtual 673	java/lang/String:substring	(II)Ljava/lang/String;
    //   412: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   415: pop
    //   416: aload 4
    //   418: invokevirtual 163	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   421: astore 4
    //   423: aload 8
    //   425: getstatic 303	android/telephony/SubscriptionManager:CONTENT_URI	Landroid/net/Uri;
    //   428: aconst_null
    //   429: ldc_w 675
    //   432: iconst_1
    //   433: anewarray 259	java/lang/String
    //   436: dup
    //   437: iconst_0
    //   438: aload 4
    //   440: aastore
    //   441: aconst_null
    //   442: invokevirtual 309	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   445: astore 9
    //   447: aload 9
    //   449: ifnull +76 -> 525
    //   452: aload 9
    //   454: invokeinterface 678 1 0
    //   459: ifeq +66 -> 525
    //   462: new 149	java/lang/StringBuilder
    //   465: astore 9
    //   467: aload 9
    //   469: invokespecial 150	java/lang/StringBuilder:<init>	()V
    //   472: aload 9
    //   474: ldc_w 680
    //   477: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   480: pop
    //   481: aload 9
    //   483: aload 5
    //   485: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   488: pop
    //   489: aload 9
    //   491: ldc_w 682
    //   494: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   497: pop
    //   498: aload 9
    //   500: aload 4
    //   502: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   505: pop
    //   506: aload_3
    //   507: aload 9
    //   509: invokevirtual 163	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   512: invokevirtual 146	com/android/internal/telephony/SubscriptionController:logdl	(Ljava/lang/String;)V
    //   515: aload 4
    //   517: astore 5
    //   519: iconst_0
    //   520: istore 10
    //   522: goto +58 -> 580
    //   525: aload 13
    //   527: invokevirtual 669	java/lang/String:length	()I
    //   530: istore 14
    //   532: aload 11
    //   534: invokevirtual 669	java/lang/String:length	()I
    //   537: istore 15
    //   539: aload 4
    //   541: astore 9
    //   543: iload 14
    //   545: iload 15
    //   547: if_icmpgt +29 -> 576
    //   550: iconst_0
    //   551: istore 10
    //   553: aload_1
    //   554: astore 5
    //   556: aload 4
    //   558: astore 9
    //   560: goto +16 -> 576
    //   563: astore_1
    //   564: goto -424 -> 140
    //   567: astore_1
    //   568: goto +1548 -> 2116
    //   571: ldc_w 413
    //   574: astore 9
    //   576: aload 9
    //   578: astore 4
    //   580: aload 13
    //   582: astore 9
    //   584: aload 11
    //   586: astore 13
    //   588: aload 13
    //   590: invokevirtual 669	java/lang/String:length	()I
    //   593: aload 5
    //   595: invokevirtual 669	java/lang/String:length	()I
    //   598: if_icmpeq +163 -> 761
    //   601: iload 10
    //   603: iconst_1
    //   604: if_icmpne +157 -> 761
    //   607: new 149	java/lang/StringBuilder
    //   610: astore 4
    //   612: aload 4
    //   614: invokespecial 150	java/lang/StringBuilder:<init>	()V
    //   617: aload 4
    //   619: aload 13
    //   621: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   624: pop
    //   625: aload 4
    //   627: aload_1
    //   628: aload_1
    //   629: invokevirtual 669	java/lang/String:length	()I
    //   632: iconst_1
    //   633: isub
    //   634: aload_1
    //   635: invokevirtual 669	java/lang/String:length	()I
    //   638: invokevirtual 673	java/lang/String:substring	(II)Ljava/lang/String;
    //   641: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   644: pop
    //   645: aload 4
    //   647: invokevirtual 163	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   650: astore 4
    //   652: getstatic 303	android/telephony/SubscriptionManager:CONTENT_URI	Landroid/net/Uri;
    //   655: astore 11
    //   657: aload 8
    //   659: aload 11
    //   661: aconst_null
    //   662: ldc_w 675
    //   665: iconst_1
    //   666: anewarray 259	java/lang/String
    //   669: dup
    //   670: iconst_0
    //   671: aload 4
    //   673: aastore
    //   674: aconst_null
    //   675: invokevirtual 309	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   678: astore 11
    //   680: aload 11
    //   682: ifnull +73 -> 755
    //   685: aload 11
    //   687: invokeinterface 678 1 0
    //   692: ifeq +63 -> 755
    //   695: new 149	java/lang/StringBuilder
    //   698: astore 11
    //   700: aload 11
    //   702: invokespecial 150	java/lang/StringBuilder:<init>	()V
    //   705: aload 11
    //   707: ldc_w 684
    //   710: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   713: pop
    //   714: aload 11
    //   716: aload 5
    //   718: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   721: pop
    //   722: aload 11
    //   724: ldc_w 682
    //   727: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   730: pop
    //   731: aload 11
    //   733: aload 4
    //   735: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   738: pop
    //   739: aload_3
    //   740: aload 11
    //   742: invokevirtual 163	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   745: invokevirtual 146	com/android/internal/telephony/SubscriptionController:logdl	(Ljava/lang/String;)V
    //   748: aload 4
    //   750: astore 5
    //   752: goto +9 -> 761
    //   755: aload_1
    //   756: astore 5
    //   758: goto +3 -> 761
    //   761: aload 4
    //   763: astore 11
    //   765: aload 9
    //   767: invokevirtual 669	java/lang/String:length	()I
    //   770: aload 13
    //   772: invokevirtual 669	java/lang/String:length	()I
    //   775: if_icmpne +100 -> 875
    //   778: aload 9
    //   780: invokevirtual 669	java/lang/String:length	()I
    //   783: aload 12
    //   785: invokevirtual 669	java/lang/String:length	()I
    //   788: if_icmpne +87 -> 875
    //   791: aload 5
    //   793: astore 4
    //   795: aload 11
    //   797: astore 9
    //   799: aload_1
    //   800: invokevirtual 669	java/lang/String:length	()I
    //   803: aload 5
    //   805: invokevirtual 669	java/lang/String:length	()I
    //   808: isub
    //   809: iconst_1
    //   810: if_icmpne +103 -> 913
    //   813: new 149	java/lang/StringBuilder
    //   816: astore 4
    //   818: aload 4
    //   820: invokespecial 150	java/lang/StringBuilder:<init>	()V
    //   823: aload 4
    //   825: ldc_w 686
    //   828: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   831: pop
    //   832: aload 4
    //   834: aload 5
    //   836: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   839: pop
    //   840: aload 4
    //   842: ldc_w 658
    //   845: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   848: pop
    //   849: aload 4
    //   851: aload_1
    //   852: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   855: pop
    //   856: aload_3
    //   857: aload 4
    //   859: invokevirtual 163	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   862: invokevirtual 146	com/android/internal/telephony/SubscriptionController:logdl	(Ljava/lang/String;)V
    //   865: aload_1
    //   866: astore 4
    //   868: aload 11
    //   870: astore 9
    //   872: goto +41 -> 913
    //   875: aload 5
    //   877: astore 4
    //   879: aload 11
    //   881: astore 9
    //   883: goto +30 -> 913
    //   886: astore_1
    //   887: goto +1229 -> 2116
    //   890: astore_1
    //   891: goto +1225 -> 2116
    //   894: astore_1
    //   895: goto +1221 -> 2116
    //   898: aload 5
    //   900: astore 4
    //   902: goto +11 -> 913
    //   905: astore_1
    //   906: goto +1210 -> 2116
    //   909: aload 5
    //   911: astore 4
    //   913: getstatic 303	android/telephony/SubscriptionManager:CONTENT_URI	Landroid/net/Uri;
    //   916: astore 11
    //   918: aload_1
    //   919: invokestatic 666	com/android/internal/telephony/uicc/IccUtils:getDecimalSubstring	(Ljava/lang/String;)Ljava/lang/String;
    //   922: astore 5
    //   924: aload 8
    //   926: aload 11
    //   928: iconst_5
    //   929: anewarray 259	java/lang/String
    //   932: dup
    //   933: iconst_0
    //   934: ldc_w 330
    //   937: aastore
    //   938: dup
    //   939: iconst_1
    //   940: ldc_w 346
    //   943: aastore
    //   944: dup
    //   945: iconst_2
    //   946: ldc_w 352
    //   949: aastore
    //   950: dup
    //   951: iconst_3
    //   952: ldc_w 340
    //   955: aastore
    //   956: dup
    //   957: iconst_4
    //   958: ldc_w 375
    //   961: aastore
    //   962: ldc_w 688
    //   965: iconst_3
    //   966: anewarray 259	java/lang/String
    //   969: dup
    //   970: iconst_0
    //   971: aload_1
    //   972: aastore
    //   973: dup
    //   974: iconst_1
    //   975: aload 5
    //   977: aastore
    //   978: dup
    //   979: iconst_2
    //   980: aload 4
    //   982: aastore
    //   983: aconst_null
    //   984: invokevirtual 309	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   987: astore 5
    //   989: iconst_0
    //   990: istore 10
    //   992: aload 5
    //   994: ifnull +301 -> 1295
    //   997: aload 5
    //   999: invokeinterface 678 1 0
    //   1004: ifne +6 -> 1010
    //   1007: goto +288 -> 1295
    //   1010: aload 5
    //   1012: iconst_0
    //   1013: invokeinterface 338 2 0
    //   1018: istore 15
    //   1020: aload 5
    //   1022: iconst_1
    //   1023: invokeinterface 338 2 0
    //   1028: istore 16
    //   1030: aload 5
    //   1032: iconst_2
    //   1033: invokeinterface 338 2 0
    //   1038: istore 14
    //   1040: aload 5
    //   1042: iconst_3
    //   1043: invokeinterface 344 2 0
    //   1048: astore 13
    //   1050: aload 5
    //   1052: iconst_4
    //   1053: invokeinterface 344 2 0
    //   1058: astore 11
    //   1060: new 528	android/content/ContentValues
    //   1063: astore 4
    //   1065: aload 4
    //   1067: invokespecial 550	android/content/ContentValues:<init>	()V
    //   1070: iload_2
    //   1071: iload 16
    //   1073: if_icmpeq +22 -> 1095
    //   1076: aload 4
    //   1078: ldc_w 346
    //   1081: iload_2
    //   1082: invokestatic 598	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   1085: invokevirtual 601	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   1088: goto +7 -> 1095
    //   1091: astore_1
    //   1092: goto +998 -> 2090
    //   1095: iload 14
    //   1097: iconst_2
    //   1098: if_icmpeq +6 -> 1104
    //   1101: iconst_1
    //   1102: istore 10
    //   1104: aload 13
    //   1106: ifnull +58 -> 1164
    //   1109: aload 13
    //   1111: invokevirtual 669	java/lang/String:length	()I
    //   1114: aload_1
    //   1115: invokevirtual 669	java/lang/String:length	()I
    //   1118: if_icmpeq +46 -> 1164
    //   1121: aload 13
    //   1123: aload_1
    //   1124: invokestatic 666	com/android/internal/telephony/uicc/IccUtils:getDecimalSubstring	(Ljava/lang/String;)Ljava/lang/String;
    //   1127: invokevirtual 263	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   1130: ifne +25 -> 1155
    //   1133: aload_1
    //   1134: aload 13
    //   1136: invokestatic 663	com/android/internal/telephony/uicc/IccUtils:stripTrailingFs	(Ljava/lang/String;)Ljava/lang/String;
    //   1139: invokevirtual 692	java/lang/String:equalsIgnoreCase	(Ljava/lang/String;)Z
    //   1142: ifne +13 -> 1155
    //   1145: aload 13
    //   1147: aload 9
    //   1149: invokevirtual 263	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   1152: ifeq +12 -> 1164
    //   1155: aload 4
    //   1157: ldc_w 340
    //   1160: aload_1
    //   1161: invokevirtual 532	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   1164: invokestatic 625	com/android/internal/telephony/uicc/UiccController:getInstance	()Lcom/android/internal/telephony/uicc/UiccController;
    //   1167: iload_2
    //   1168: invokevirtual 695	com/android/internal/telephony/uicc/UiccController:getUiccCardForPhone	(I)Lcom/android/internal/telephony/uicc/UiccCard;
    //   1171: astore_1
    //   1172: aload_1
    //   1173: ifnull +38 -> 1211
    //   1176: aload_1
    //   1177: invokevirtual 700	com/android/internal/telephony/uicc/UiccCard:getCardId	()Ljava/lang/String;
    //   1180: astore_1
    //   1181: aload_1
    //   1182: ifnull +29 -> 1211
    //   1185: aload_1
    //   1186: aload 11
    //   1188: if_acmpeq +23 -> 1211
    //   1191: aload 4
    //   1193: ldc_w 375
    //   1196: aload_1
    //   1197: invokevirtual 532	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   1200: goto +11 -> 1211
    //   1203: astore_1
    //   1204: goto -112 -> 1092
    //   1207: astore_1
    //   1208: goto +882 -> 2090
    //   1211: aload 4
    //   1213: invokevirtual 701	android/content/ContentValues:size	()I
    //   1216: ifle +57 -> 1273
    //   1219: getstatic 303	android/telephony/SubscriptionManager:CONTENT_URI	Landroid/net/Uri;
    //   1222: astore 9
    //   1224: new 149	java/lang/StringBuilder
    //   1227: astore_1
    //   1228: aload_1
    //   1229: invokespecial 150	java/lang/StringBuilder:<init>	()V
    //   1232: aload_1
    //   1233: ldc_w 534
    //   1236: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1239: pop
    //   1240: iload 15
    //   1242: i2l
    //   1243: lstore 17
    //   1245: aload_1
    //   1246: lload 17
    //   1248: invokestatic 539	java/lang/Long:toString	(J)Ljava/lang/String;
    //   1251: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1254: pop
    //   1255: aload 8
    //   1257: aload 9
    //   1259: aload 4
    //   1261: aload_1
    //   1262: invokevirtual 163	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1265: aconst_null
    //   1266: invokevirtual 543	android/content/ContentResolver:update	(Landroid/net/Uri;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
    //   1269: pop
    //   1270: goto +3 -> 1273
    //   1273: aload_3
    //   1274: ldc_w 703
    //   1277: invokevirtual 146	com/android/internal/telephony/SubscriptionController:logdl	(Ljava/lang/String;)V
    //   1280: goto +60 -> 1340
    //   1283: astore_1
    //   1284: goto -192 -> 1092
    //   1287: astore_1
    //   1288: goto +802 -> 2090
    //   1291: astore_1
    //   1292: goto +798 -> 2090
    //   1295: iconst_1
    //   1296: istore 10
    //   1298: aload_0
    //   1299: aload_1
    //   1300: iload_2
    //   1301: invokevirtual 707	com/android/internal/telephony/SubscriptionController:insertEmptySubInfoRecord	(Ljava/lang/String;I)Landroid/net/Uri;
    //   1304: astore_1
    //   1305: new 149	java/lang/StringBuilder
    //   1308: astore 4
    //   1310: aload 4
    //   1312: invokespecial 150	java/lang/StringBuilder:<init>	()V
    //   1315: aload 4
    //   1317: ldc_w 709
    //   1320: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1323: pop
    //   1324: aload 4
    //   1326: aload_1
    //   1327: invokevirtual 270	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1330: pop
    //   1331: aload_3
    //   1332: aload 4
    //   1334: invokevirtual 163	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1337: invokevirtual 146	com/android/internal/telephony/SubscriptionController:logdl	(Ljava/lang/String;)V
    //   1340: aload 5
    //   1342: ifnull +17 -> 1359
    //   1345: aload 5
    //   1347: invokeinterface 326 1 0
    //   1352: goto +7 -> 1359
    //   1355: astore_1
    //   1356: goto +750 -> 2106
    //   1359: aload 8
    //   1361: getstatic 303	android/telephony/SubscriptionManager:CONTENT_URI	Landroid/net/Uri;
    //   1364: aconst_null
    //   1365: ldc_w 711
    //   1368: iconst_1
    //   1369: anewarray 259	java/lang/String
    //   1372: dup
    //   1373: iconst_0
    //   1374: iload_2
    //   1375: invokestatic 713	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   1378: aastore
    //   1379: aconst_null
    //   1380: invokevirtual 309	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   1383: astore_1
    //   1384: aload_1
    //   1385: ifnull +405 -> 1790
    //   1388: aload_1
    //   1389: invokeinterface 678 1 0
    //   1394: ifeq +396 -> 1790
    //   1397: aload_1
    //   1398: aload_1
    //   1399: ldc_w 330
    //   1402: invokeinterface 334 2 0
    //   1407: invokeinterface 338 2 0
    //   1412: istore 14
    //   1414: getstatic 72	com/android/internal/telephony/SubscriptionController:sSlotIndexToSubId	Ljava/util/Map;
    //   1417: iload_2
    //   1418: invokestatic 598	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   1421: invokeinterface 716 2 0
    //   1426: checkcast 490	java/lang/Integer
    //   1429: astore 5
    //   1431: aload 5
    //   1433: ifnull +37 -> 1470
    //   1436: aload 5
    //   1438: invokevirtual 719	java/lang/Integer:intValue	()I
    //   1441: iload 14
    //   1443: if_icmpne +27 -> 1470
    //   1446: aload 5
    //   1448: invokevirtual 719	java/lang/Integer:intValue	()I
    //   1451: invokestatic 608	android/telephony/SubscriptionManager:isValidSubscriptionId	(I)Z
    //   1454: ifne +6 -> 1460
    //   1457: goto +13 -> 1470
    //   1460: aload_3
    //   1461: ldc_w 721
    //   1464: invokevirtual 146	com/android/internal/telephony/SubscriptionController:logdl	(Ljava/lang/String;)V
    //   1467: goto +231 -> 1698
    //   1470: getstatic 72	com/android/internal/telephony/SubscriptionController:sSlotIndexToSubId	Ljava/util/Map;
    //   1473: iload_2
    //   1474: invokestatic 598	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   1477: iload 14
    //   1479: invokestatic 598	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   1482: invokeinterface 724 3 0
    //   1487: pop
    //   1488: aload_0
    //   1489: invokevirtual 727	com/android/internal/telephony/SubscriptionController:getActiveSubInfoCountMax	()I
    //   1492: istore 16
    //   1494: aload_0
    //   1495: invokevirtual 730	com/android/internal/telephony/SubscriptionController:getDefaultSubId	()I
    //   1498: istore 15
    //   1500: new 149	java/lang/StringBuilder
    //   1503: astore 5
    //   1505: aload 5
    //   1507: invokespecial 150	java/lang/StringBuilder:<init>	()V
    //   1510: aload 5
    //   1512: ldc_w 732
    //   1515: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1518: pop
    //   1519: aload 5
    //   1521: getstatic 72	com/android/internal/telephony/SubscriptionController:sSlotIndexToSubId	Ljava/util/Map;
    //   1524: invokeinterface 448 1 0
    //   1529: invokevirtual 159	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   1532: pop
    //   1533: aload 5
    //   1535: ldc_w 734
    //   1538: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1541: pop
    //   1542: aload 5
    //   1544: iload_2
    //   1545: invokevirtual 159	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   1548: pop
    //   1549: aload 5
    //   1551: ldc_w 736
    //   1554: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1557: pop
    //   1558: aload 5
    //   1560: iload 14
    //   1562: invokevirtual 159	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   1565: pop
    //   1566: aload 5
    //   1568: ldc_w 738
    //   1571: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1574: pop
    //   1575: aload 5
    //   1577: iload 15
    //   1579: invokevirtual 159	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   1582: pop
    //   1583: aload 5
    //   1585: ldc_w 740
    //   1588: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1591: pop
    //   1592: aload 5
    //   1594: iload 16
    //   1596: invokevirtual 159	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   1599: pop
    //   1600: aload_3
    //   1601: aload 5
    //   1603: invokevirtual 163	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1606: invokevirtual 146	com/android/internal/telephony/SubscriptionController:logdl	(Ljava/lang/String;)V
    //   1609: iload 15
    //   1611: invokestatic 608	android/telephony/SubscriptionManager:isValidSubscriptionId	(I)Z
    //   1614: ifeq +18 -> 1632
    //   1617: iload 16
    //   1619: iconst_1
    //   1620: if_icmpeq +12 -> 1632
    //   1623: aload_3
    //   1624: iload 15
    //   1626: invokevirtual 743	com/android/internal/telephony/SubscriptionController:isActiveSubId	(I)Z
    //   1629: ifne +9 -> 1638
    //   1632: aload_3
    //   1633: iload 14
    //   1635: invokevirtual 746	com/android/internal/telephony/SubscriptionController:setDefaultFallbackSubId	(I)V
    //   1638: iload 16
    //   1640: iconst_1
    //   1641: if_icmpne +57 -> 1698
    //   1644: new 149	java/lang/StringBuilder
    //   1647: astore 5
    //   1649: aload 5
    //   1651: invokespecial 150	java/lang/StringBuilder:<init>	()V
    //   1654: aload 5
    //   1656: ldc_w 748
    //   1659: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1662: pop
    //   1663: aload 5
    //   1665: iload 14
    //   1667: invokevirtual 159	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   1670: pop
    //   1671: aload_3
    //   1672: aload 5
    //   1674: invokevirtual 163	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1677: invokevirtual 146	com/android/internal/telephony/SubscriptionController:logdl	(Ljava/lang/String;)V
    //   1680: aload_3
    //   1681: iload 14
    //   1683: invokevirtual 751	com/android/internal/telephony/SubscriptionController:setDefaultDataSubId	(I)V
    //   1686: aload_3
    //   1687: iload 14
    //   1689: invokevirtual 754	com/android/internal/telephony/SubscriptionController:setDefaultSmsSubId	(I)V
    //   1692: aload_3
    //   1693: iload 14
    //   1695: invokevirtual 757	com/android/internal/telephony/SubscriptionController:setDefaultVoiceSubId	(I)V
    //   1698: new 149	java/lang/StringBuilder
    //   1701: astore 5
    //   1703: aload 5
    //   1705: invokespecial 150	java/lang/StringBuilder:<init>	()V
    //   1708: aload 5
    //   1710: ldc_w 759
    //   1713: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1716: pop
    //   1717: aload 5
    //   1719: iload_2
    //   1720: invokevirtual 159	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   1723: pop
    //   1724: aload 5
    //   1726: ldc_w 761
    //   1729: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1732: pop
    //   1733: aload 5
    //   1735: iload 14
    //   1737: invokevirtual 159	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   1740: pop
    //   1741: aload 5
    //   1743: ldc_w 763
    //   1746: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1749: pop
    //   1750: aload_3
    //   1751: aload 5
    //   1753: invokevirtual 163	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1756: invokevirtual 146	com/android/internal/telephony/SubscriptionController:logdl	(Ljava/lang/String;)V
    //   1759: aload_1
    //   1760: invokeinterface 314 1 0
    //   1765: istore 19
    //   1767: iload 19
    //   1769: ifne -372 -> 1397
    //   1772: goto +18 -> 1790
    //   1775: astore 5
    //   1777: aload_1
    //   1778: ifnull +9 -> 1787
    //   1781: aload_1
    //   1782: invokeinterface 326 1 0
    //   1787: aload 5
    //   1789: athrow
    //   1790: aload_1
    //   1791: ifnull +9 -> 1800
    //   1794: aload_1
    //   1795: invokeinterface 326 1 0
    //   1800: aload_3
    //   1801: iload_2
    //   1802: invokevirtual 766	com/android/internal/telephony/SubscriptionController:getSubIdUsingPhoneId	(I)I
    //   1805: istore 15
    //   1807: iload 15
    //   1809: invokestatic 608	android/telephony/SubscriptionManager:isValidSubscriptionId	(I)Z
    //   1812: ifne +41 -> 1853
    //   1815: new 149	java/lang/StringBuilder
    //   1818: astore_1
    //   1819: aload_1
    //   1820: invokespecial 150	java/lang/StringBuilder:<init>	()V
    //   1823: aload_1
    //   1824: ldc_w 768
    //   1827: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1830: pop
    //   1831: aload_1
    //   1832: iload 15
    //   1834: invokevirtual 159	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   1837: pop
    //   1838: aload_3
    //   1839: aload_1
    //   1840: invokevirtual 163	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1843: invokevirtual 146	com/android/internal/telephony/SubscriptionController:logdl	(Ljava/lang/String;)V
    //   1846: lload 6
    //   1848: invokestatic 277	android/os/Binder:restoreCallingIdentity	(J)V
    //   1851: iconst_m1
    //   1852: ireturn
    //   1853: iload 10
    //   1855: ifeq +174 -> 2029
    //   1858: aload_3
    //   1859: getfield 394	com/android/internal/telephony/SubscriptionController:mTelephonyManager	Landroid/telephony/TelephonyManager;
    //   1862: iload 15
    //   1864: invokevirtual 771	android/telephony/TelephonyManager:getSimOperatorName	(I)Ljava/lang/String;
    //   1867: astore_1
    //   1868: aload_1
    //   1869: invokestatic 405	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   1872: ifne +6 -> 1878
    //   1875: goto +35 -> 1910
    //   1878: new 149	java/lang/StringBuilder
    //   1881: astore_1
    //   1882: aload_1
    //   1883: invokespecial 150	java/lang/StringBuilder:<init>	()V
    //   1886: aload_1
    //   1887: ldc_w 773
    //   1890: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1893: pop
    //   1894: aload_1
    //   1895: iload_2
    //   1896: iconst_1
    //   1897: iadd
    //   1898: invokestatic 492	java/lang/Integer:toString	(I)Ljava/lang/String;
    //   1901: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1904: pop
    //   1905: aload_1
    //   1906: invokevirtual 163	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1909: astore_1
    //   1910: new 528	android/content/ContentValues
    //   1913: astore 5
    //   1915: aload 5
    //   1917: invokespecial 550	android/content/ContentValues:<init>	()V
    //   1920: aload 5
    //   1922: ldc_w 348
    //   1925: aload_1
    //   1926: invokevirtual 532	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   1929: getstatic 303	android/telephony/SubscriptionManager:CONTENT_URI	Landroid/net/Uri;
    //   1932: astore 9
    //   1934: new 149	java/lang/StringBuilder
    //   1937: astore 4
    //   1939: aload 4
    //   1941: invokespecial 150	java/lang/StringBuilder:<init>	()V
    //   1944: aload 4
    //   1946: ldc_w 534
    //   1949: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1952: pop
    //   1953: iload 15
    //   1955: i2l
    //   1956: lstore 17
    //   1958: aload 4
    //   1960: lload 17
    //   1962: invokestatic 539	java/lang/Long:toString	(J)Ljava/lang/String;
    //   1965: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1968: pop
    //   1969: aload 8
    //   1971: aload 9
    //   1973: aload 5
    //   1975: aload 4
    //   1977: invokevirtual 163	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1980: aconst_null
    //   1981: invokevirtual 543	android/content/ContentResolver:update	(Landroid/net/Uri;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
    //   1984: pop
    //   1985: new 149	java/lang/StringBuilder
    //   1988: astore 5
    //   1990: aload 5
    //   1992: invokespecial 150	java/lang/StringBuilder:<init>	()V
    //   1995: aload 5
    //   1997: ldc_w 775
    //   2000: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2003: pop
    //   2004: aload 5
    //   2006: aload_1
    //   2007: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2010: pop
    //   2011: aload 5
    //   2013: invokevirtual 163	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   2016: astore_1
    //   2017: aload_0
    //   2018: aload_1
    //   2019: invokevirtual 146	com/android/internal/telephony/SubscriptionController:logdl	(Ljava/lang/String;)V
    //   2022: goto +7 -> 2029
    //   2025: astore_1
    //   2026: goto +80 -> 2106
    //   2029: aload_0
    //   2030: invokevirtual 546	com/android/internal/telephony/SubscriptionController:refreshCachedActiveSubscriptionInfoList	()V
    //   2033: getstatic 777	com/android/internal/telephony/SubscriptionController:sPhones	[Lcom/android/internal/telephony/Phone;
    //   2036: iload_2
    //   2037: aaload
    //   2038: invokevirtual 780	com/android/internal/telephony/Phone:updateDataConnectionTracker	()V
    //   2041: new 149	java/lang/StringBuilder
    //   2044: astore_1
    //   2045: aload_1
    //   2046: invokespecial 150	java/lang/StringBuilder:<init>	()V
    //   2049: aload_1
    //   2050: ldc_w 782
    //   2053: invokevirtual 156	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2056: pop
    //   2057: aload_1
    //   2058: getstatic 72	com/android/internal/telephony/SubscriptionController:sSlotIndexToSubId	Ljava/util/Map;
    //   2061: invokeinterface 448 1 0
    //   2066: invokevirtual 159	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   2069: pop
    //   2070: aload_0
    //   2071: aload_1
    //   2072: invokevirtual 163	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   2075: invokevirtual 146	com/android/internal/telephony/SubscriptionController:logdl	(Ljava/lang/String;)V
    //   2078: lload 6
    //   2080: invokestatic 277	android/os/Binder:restoreCallingIdentity	(J)V
    //   2083: iconst_0
    //   2084: ireturn
    //   2085: astore_1
    //   2086: goto +30 -> 2116
    //   2089: astore_1
    //   2090: aload 5
    //   2092: ifnull +17 -> 2109
    //   2095: aload 5
    //   2097: invokeinterface 326 1 0
    //   2102: goto +7 -> 2109
    //   2105: astore_1
    //   2106: goto +10 -> 2116
    //   2109: aload_1
    //   2110: athrow
    //   2111: astore_1
    //   2112: goto +4 -> 2116
    //   2115: astore_1
    //   2116: lload 6
    //   2118: invokestatic 277	android/os/Binder:restoreCallingIdentity	(J)V
    //   2121: aload_1
    //   2122: athrow
    //   2123: aload_3
    //   2124: ldc_w 784
    //   2127: invokevirtual 146	com/android/internal/telephony/SubscriptionController:logdl	(Ljava/lang/String;)V
    //   2130: iconst_m1
    //   2131: ireturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	2132	0	this	SubscriptionController
    //   0	2132	1	paramString	String
    //   0	2132	2	paramInt	int
    //   1	2123	3	localSubscriptionController	SubscriptionController
    //   6	1970	4	localObject1	Object
    //   15	1737	5	localObject2	Object
    //   1775	13	5	localObject3	Object
    //   1913	183	5	localObject4	Object
    //   119	1998	6	l1	long
    //   150	1820	8	localContentResolver	ContentResolver
    //   155	1817	9	localObject5	Object
    //   163	1691	10	i	int
    //   196	991	11	localObject6	Object
    //   224	560	12	str	String
    //   327	819	13	localObject7	Object
    //   344	1392	14	j	int
    //   351	1603	15	k	int
    //   1028	614	16	m	int
    //   1243	718	17	l2	long
    //   1765	3	19	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   125	132	139	finally
    //   398	447	563	finally
    //   452	515	563	finally
    //   525	539	563	finally
    //   360	398	567	finally
    //   657	680	886	finally
    //   685	748	886	finally
    //   765	791	886	finally
    //   799	865	886	finally
    //   588	601	890	finally
    //   607	657	890	finally
    //   346	353	894	finally
    //   190	209	905	finally
    //   219	336	905	finally
    //   339	346	905	finally
    //   1076	1088	1091	finally
    //   1109	1155	1091	finally
    //   1155	1164	1091	finally
    //   1191	1200	1203	finally
    //   1176	1181	1207	finally
    //   1245	1270	1283	finally
    //   1273	1280	1283	finally
    //   1211	1240	1287	finally
    //   997	1007	1291	finally
    //   1010	1070	1291	finally
    //   1164	1172	1291	finally
    //   1345	1352	1355	finally
    //   1781	1787	1355	finally
    //   1787	1790	1355	finally
    //   1794	1800	1355	finally
    //   1388	1397	1775	finally
    //   1397	1431	1775	finally
    //   1436	1457	1775	finally
    //   1460	1467	1775	finally
    //   1470	1617	1775	finally
    //   1623	1632	1775	finally
    //   1632	1638	1775	finally
    //   1644	1698	1775	finally
    //   1698	1767	1775	finally
    //   1958	2017	2025	finally
    //   1359	1384	2085	finally
    //   1800	1846	2085	finally
    //   1298	1340	2089	finally
    //   1858	1875	2105	finally
    //   1878	1910	2105	finally
    //   1910	1953	2105	finally
    //   2017	2022	2105	finally
    //   2029	2078	2105	finally
    //   2095	2102	2105	finally
    //   2109	2111	2105	finally
    //   913	989	2111	finally
    //   143	152	2115	finally
    //   157	165	2115	finally
  }
  
  protected void broadcastDefaultDataSubIdChanged(int paramInt)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("[broadcastDefaultDataSubIdChanged] subId=");
    ((StringBuilder)localObject).append(paramInt);
    logdl(((StringBuilder)localObject).toString());
    localObject = new Intent("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
    ((Intent)localObject).addFlags(553648128);
    ((Intent)localObject).putExtra("subscription", paramInt);
    mContext.sendStickyBroadcastAsUser((Intent)localObject, UserHandle.ALL);
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PRIVATE)
  public void broadcastDefaultVoiceSubIdChanged(int paramInt)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("[broadcastDefaultVoiceSubIdChanged] subId=");
    ((StringBuilder)localObject).append(paramInt);
    logdl(((StringBuilder)localObject).toString());
    localObject = new Intent("android.intent.action.ACTION_DEFAULT_VOICE_SUBSCRIPTION_CHANGED");
    ((Intent)localObject).addFlags(553648128);
    ((Intent)localObject).putExtra("subscription", paramInt);
    mContext.sendStickyBroadcastAsUser((Intent)localObject, UserHandle.ALL);
  }
  
  public void clearDefaultsForInactiveSubIds()
  {
    enforceModifyPhoneState("clearDefaultsForInactiveSubIds");
    long l = Binder.clearCallingIdentity();
    try
    {
      List localList = getActiveSubscriptionInfoList(mContext.getOpPackageName());
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("[clearDefaultsForInactiveSubIds] records: ");
      localStringBuilder.append(localList);
      logdl(localStringBuilder.toString());
      if (shouldDefaultBeCleared(localList, getDefaultDataSubId()))
      {
        logd("[clearDefaultsForInactiveSubIds] clearing default data sub id");
        setDefaultDataSubId(-1);
      }
      if (shouldDefaultBeCleared(localList, getDefaultSmsSubId()))
      {
        logdl("[clearDefaultsForInactiveSubIds] clearing default sms sub id");
        setDefaultSmsSubId(-1);
      }
      if (shouldDefaultBeCleared(localList, getDefaultVoiceSubId()))
      {
        logdl("[clearDefaultsForInactiveSubIds] clearing default voice sub id");
        setDefaultVoiceSubId(-1);
      }
      return;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  public int clearSubInfo()
  {
    enforceModifyPhoneState("clearSubInfo");
    long l = Binder.clearCallingIdentity();
    try
    {
      int i = sSlotIndexToSubId.size();
      if (i == 0)
      {
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("[clearSubInfo]- no simInfo size=");
        localStringBuilder.append(i);
        logdl(localStringBuilder.toString());
        return 0;
      }
      sSlotIndexToSubId.clear();
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("[clearSubInfo]- clear size=");
      localStringBuilder.append(i);
      logdl(localStringBuilder.toString());
      return i;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    mContext.enforceCallingOrSelfPermission("android.permission.DUMP", "Requires DUMP");
    long l = Binder.clearCallingIdentity();
    try
    {
      paramPrintWriter.println("SubscriptionController:");
      Object localObject1 = new java/lang/StringBuilder;
      ((StringBuilder)localObject1).<init>();
      ((StringBuilder)localObject1).append(" mLastISubServiceRegTime=");
      ((StringBuilder)localObject1).append(mLastISubServiceRegTime);
      paramPrintWriter.println(((StringBuilder)localObject1).toString());
      localObject1 = new java/lang/StringBuilder;
      ((StringBuilder)localObject1).<init>();
      ((StringBuilder)localObject1).append(" defaultSubId=");
      ((StringBuilder)localObject1).append(getDefaultSubId());
      paramPrintWriter.println(((StringBuilder)localObject1).toString());
      localObject1 = new java/lang/StringBuilder;
      ((StringBuilder)localObject1).<init>();
      ((StringBuilder)localObject1).append(" defaultDataSubId=");
      ((StringBuilder)localObject1).append(getDefaultDataSubId());
      paramPrintWriter.println(((StringBuilder)localObject1).toString());
      localObject1 = new java/lang/StringBuilder;
      ((StringBuilder)localObject1).<init>();
      ((StringBuilder)localObject1).append(" defaultVoiceSubId=");
      ((StringBuilder)localObject1).append(getDefaultVoiceSubId());
      paramPrintWriter.println(((StringBuilder)localObject1).toString());
      localObject1 = new java/lang/StringBuilder;
      ((StringBuilder)localObject1).<init>();
      ((StringBuilder)localObject1).append(" defaultSmsSubId=");
      ((StringBuilder)localObject1).append(getDefaultSmsSubId());
      paramPrintWriter.println(((StringBuilder)localObject1).toString());
      localObject1 = new java/lang/StringBuilder;
      ((StringBuilder)localObject1).<init>();
      ((StringBuilder)localObject1).append(" defaultDataPhoneId=");
      ((StringBuilder)localObject1).append(SubscriptionManager.from(mContext).getDefaultDataPhoneId());
      paramPrintWriter.println(((StringBuilder)localObject1).toString());
      localObject1 = new java/lang/StringBuilder;
      ((StringBuilder)localObject1).<init>();
      ((StringBuilder)localObject1).append(" defaultVoicePhoneId=");
      ((StringBuilder)localObject1).append(SubscriptionManager.getDefaultVoicePhoneId());
      paramPrintWriter.println(((StringBuilder)localObject1).toString());
      localObject1 = new java/lang/StringBuilder;
      ((StringBuilder)localObject1).<init>();
      ((StringBuilder)localObject1).append(" defaultSmsPhoneId=");
      ((StringBuilder)localObject1).append(SubscriptionManager.from(mContext).getDefaultSmsPhoneId());
      paramPrintWriter.println(((StringBuilder)localObject1).toString());
      paramPrintWriter.flush();
      localObject1 = sSlotIndexToSubId.entrySet().iterator();
      Object localObject2;
      Object localObject3;
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (Map.Entry)((Iterator)localObject1).next();
        localObject3 = new java/lang/StringBuilder;
        ((StringBuilder)localObject3).<init>();
        ((StringBuilder)localObject3).append(" sSlotIndexToSubId[");
        ((StringBuilder)localObject3).append(((Map.Entry)localObject2).getKey());
        ((StringBuilder)localObject3).append("]: subId=");
        ((StringBuilder)localObject3).append(((Map.Entry)localObject2).getValue());
        paramPrintWriter.println(((StringBuilder)localObject3).toString());
      }
      paramPrintWriter.flush();
      paramPrintWriter.println("++++++++++++++++++++++++++++++++");
      localObject1 = getActiveSubscriptionInfoList(mContext.getOpPackageName());
      if (localObject1 != null)
      {
        paramPrintWriter.println(" ActiveSubInfoList:");
        localObject3 = ((List)localObject1).iterator();
        while (((Iterator)localObject3).hasNext())
        {
          localObject2 = (SubscriptionInfo)((Iterator)localObject3).next();
          localObject1 = new java/lang/StringBuilder;
          ((StringBuilder)localObject1).<init>();
          ((StringBuilder)localObject1).append("  ");
          ((StringBuilder)localObject1).append(((SubscriptionInfo)localObject2).toString());
          paramPrintWriter.println(((StringBuilder)localObject1).toString());
        }
      }
      paramPrintWriter.println(" ActiveSubInfoList: is null");
      paramPrintWriter.flush();
      paramPrintWriter.println("++++++++++++++++++++++++++++++++");
      localObject1 = getAllSubInfoList(mContext.getOpPackageName());
      if (localObject1 != null)
      {
        paramPrintWriter.println(" AllSubInfoList:");
        localObject1 = ((List)localObject1).iterator();
        while (((Iterator)localObject1).hasNext())
        {
          localObject2 = (SubscriptionInfo)((Iterator)localObject1).next();
          localObject3 = new java/lang/StringBuilder;
          ((StringBuilder)localObject3).<init>();
          ((StringBuilder)localObject3).append("  ");
          ((StringBuilder)localObject3).append(((SubscriptionInfo)localObject2).toString());
          paramPrintWriter.println(((StringBuilder)localObject3).toString());
        }
      }
      paramPrintWriter.println(" AllSubInfoList: is null");
      paramPrintWriter.flush();
      paramPrintWriter.println("++++++++++++++++++++++++++++++++");
      mLocalLog.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      paramPrintWriter.flush();
      paramPrintWriter.println("++++++++++++++++++++++++++++++++");
      paramPrintWriter.flush();
      return;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  protected void enforceModifyPhoneState(String paramString)
  {
    mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", paramString);
  }
  
  public List<SubscriptionInfo> getAccessibleSubscriptionInfoList(String paramString)
  {
    if (!((EuiccManager)mContext.getSystemService("euicc")).isEnabled())
    {
      logdl("[getAccessibleSubInfoList] Embedded subscriptions are disabled");
      return null;
    }
    mAppOps.checkPackage(Binder.getCallingUid(), paramString);
    long l = Binder.clearCallingIdentity();
    try
    {
      List localList = getSubInfo("is_embedded=1", null);
      Binder.restoreCallingIdentity(l);
      if (localList == null)
      {
        logdl("[getAccessibleSubInfoList] No info returned");
        return null;
      }
      return (List)localList.stream().filter(new _..Lambda.SubscriptionController.3VswDVLryax7J6vjeeeQyAns1Mg(this, paramString)).sorted(SUBSCRIPTION_INFO_COMPARATOR).collect(Collectors.toList());
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  public int[] getActiveSubIdList()
  {
    Object localObject = new HashSet(sSlotIndexToSubId.entrySet());
    int[] arrayOfInt = new int[((Set)localObject).size()];
    int i = 0;
    localObject = ((Set)localObject).iterator();
    while (((Iterator)localObject).hasNext())
    {
      arrayOfInt[i] = ((Integer)((Map.Entry)((Iterator)localObject).next()).getValue()).intValue();
      i++;
    }
    return arrayOfInt;
  }
  
  public int getActiveSubInfoCount(String paramString)
  {
    paramString = getActiveSubscriptionInfoList(paramString);
    if (paramString == null) {
      return 0;
    }
    return paramString.size();
  }
  
  public int getActiveSubInfoCountMax()
  {
    return mTelephonyManager.getSimCount();
  }
  
  public SubscriptionInfo getActiveSubscriptionInfo(int paramInt, String paramString)
  {
    if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(mContext, paramInt, paramString, "getActiveSubscriptionInfo")) {
      return null;
    }
    long l = Binder.clearCallingIdentity();
    try
    {
      paramString = getActiveSubscriptionInfoList(mContext.getOpPackageName());
      if (paramString != null)
      {
        Iterator localIterator = paramString.iterator();
        while (localIterator.hasNext())
        {
          localObject = (SubscriptionInfo)localIterator.next();
          if (((SubscriptionInfo)localObject).getSubscriptionId() == paramInt)
          {
            paramString = new java/lang/StringBuilder;
            paramString.<init>();
            paramString.append("[getActiveSubscriptionInfo]+ subId=");
            paramString.append(paramInt);
            paramString.append(" subInfo=");
            paramString.append(localObject);
            logd(paramString.toString());
            return localObject;
          }
        }
      }
      Object localObject = new java/lang/StringBuilder;
      ((StringBuilder)localObject).<init>();
      ((StringBuilder)localObject).append("[getActiveSubInfoForSubscriber]- subId=");
      ((StringBuilder)localObject).append(paramInt);
      ((StringBuilder)localObject).append(" subList=");
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append(" subInfo=null");
      logd(((StringBuilder)localObject).toString());
      return null;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  public SubscriptionInfo getActiveSubscriptionInfoForIccId(String paramString1, String paramString2)
  {
    paramString1 = getActiveSubscriptionInfoForIccIdInternal(paramString1);
    int i;
    if (paramString1 != null) {
      i = paramString1.getSubscriptionId();
    } else {
      i = -1;
    }
    if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(mContext, i, paramString2, "getActiveSubscriptionInfoForIccId")) {
      return null;
    }
    return paramString1;
  }
  
  public SubscriptionInfo getActiveSubscriptionInfoForSimSlotIndex(int paramInt, String paramString)
  {
    Object localObject = PhoneFactory.getPhone(paramInt);
    if (localObject == null)
    {
      paramString = new StringBuilder();
      paramString.append("[getActiveSubscriptionInfoForSimSlotIndex] no phone, slotIndex=");
      paramString.append(paramInt);
      loge(paramString.toString());
      return null;
    }
    if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(mContext, ((Phone)localObject).getSubId(), paramString, "getActiveSubscriptionInfoForSimSlotIndex")) {
      return null;
    }
    long l = Binder.clearCallingIdentity();
    try
    {
      paramString = getActiveSubscriptionInfoList(mContext.getOpPackageName());
      if (paramString != null)
      {
        localObject = paramString.iterator();
        while (((Iterator)localObject).hasNext())
        {
          paramString = (SubscriptionInfo)((Iterator)localObject).next();
          if (paramString.getSimSlotIndex() == paramInt)
          {
            localObject = new java/lang/StringBuilder;
            ((StringBuilder)localObject).<init>();
            ((StringBuilder)localObject).append("[getActiveSubscriptionInfoForSimSlotIndex]+ slotIndex=");
            ((StringBuilder)localObject).append(paramInt);
            ((StringBuilder)localObject).append(" subId=");
            ((StringBuilder)localObject).append(paramString);
            logd(((StringBuilder)localObject).toString());
            return paramString;
          }
        }
        paramString = new java/lang/StringBuilder;
        paramString.<init>();
        paramString.append("[getActiveSubscriptionInfoForSimSlotIndex]+ slotIndex=");
        paramString.append(paramInt);
        paramString.append(" subId=null");
        logd(paramString.toString());
      }
      else
      {
        logd("[getActiveSubscriptionInfoForSimSlotIndex]+ subList=null");
      }
      return null;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  public List<SubscriptionInfo> getActiveSubscriptionInfoList(String paramString)
  {
    if (!isSubInfoReady())
    {
      logdl("[getActiveSubInfoList] Sub Controller not ready");
      return null;
    }
    boolean bool;
    try
    {
      bool = TelephonyPermissions.checkReadPhoneState(mContext, -1, Binder.getCallingPid(), Binder.getCallingUid(), paramString, "getActiveSubscriptionInfoList");
    }
    catch (SecurityException localSecurityException)
    {
      bool = false;
    }
    List localList = mCacheActiveSubInfoList;
    if (bool) {
      try
      {
        paramString = new java/util/ArrayList;
        paramString.<init>(mCacheActiveSubInfoList);
        return paramString;
      }
      finally
      {
        break label120;
      }
    }
    Stream localStream = mCacheActiveSubInfoList.stream();
    _..Lambda.SubscriptionController.tMI7DzRlXdGT29a2mf9_vcxGNO0 localTMI7DzRlXdGT29a2mf9_vcxGNO0 = new com/android/internal/telephony/_$$Lambda$SubscriptionController$tMI7DzRlXdGT29a2mf9_vcxGNO0;
    localTMI7DzRlXdGT29a2mf9_vcxGNO0.<init>(this, paramString);
    paramString = (List)localStream.filter(localTMI7DzRlXdGT29a2mf9_vcxGNO0).collect(Collectors.toList());
    return paramString;
    label120:
    throw paramString;
  }
  
  public int getAllSubInfoCount(String paramString)
  {
    logd("[getAllSubInfoCount]+");
    if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(mContext, -1, paramString, "getAllSubInfoCount")) {
      return 0;
    }
    long l = Binder.clearCallingIdentity();
    try
    {
      paramString = mContext.getContentResolver().query(SubscriptionManager.CONTENT_URI, null, null, null, null);
      if (paramString != null) {
        try
        {
          int i = paramString.getCount();
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("[getAllSubInfoCount]- ");
          localStringBuilder.append(i);
          localStringBuilder.append(" SUB(s) in DB");
          logd(localStringBuilder.toString());
          if (paramString != null) {
            paramString.close();
          }
          return i;
        }
        finally
        {
          if (paramString != null) {
            paramString.close();
          }
        }
      }
      if (paramString != null) {
        paramString.close();
      }
      logd("[getAllSubInfoCount]- no SUB in DB");
      return 0;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  public List<SubscriptionInfo> getAllSubInfoList(String paramString)
  {
    logd("[getAllSubInfoList]+");
    if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(mContext, -1, paramString, "getAllSubInfoList")) {
      return null;
    }
    long l = Binder.clearCallingIdentity();
    try
    {
      List localList = getSubInfo(null, null);
      if (localList != null)
      {
        paramString = new java/lang/StringBuilder;
        paramString.<init>();
        paramString.append("[getAllSubInfoList]- ");
        paramString.append(localList.size());
        paramString.append(" infos return");
        logd(paramString.toString());
      }
      else
      {
        logd("[getAllSubInfoList]- no info return");
      }
      return localList;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  public List<SubscriptionInfo> getAvailableSubscriptionInfoList(String paramString)
  {
    if (TelephonyPermissions.checkCallingOrSelfReadPhoneState(mContext, -1, paramString, "getAvailableSubscriptionInfoList"))
    {
      long l = Binder.clearCallingIdentity();
      try
      {
        if (!((EuiccManager)mContext.getSystemService("euicc")).isEnabled())
        {
          logdl("[getAvailableSubInfoList] Embedded subscriptions are disabled");
          return null;
        }
        paramString = getSubInfo("sim_id>=0 OR is_embedded=1", null);
        if (paramString != null) {
          paramString.sort(SUBSCRIPTION_INFO_COMPARATOR);
        } else {
          logdl("[getAvailableSubInfoList]- no info return");
        }
        return paramString;
      }
      finally
      {
        Binder.restoreCallingIdentity(l);
      }
    }
    throw new SecurityException("Need READ_PHONE_STATE to call  getAvailableSubscriptionInfoList");
  }
  
  public int getDefaultDataSubId()
  {
    return Settings.Global.getInt(mContext.getContentResolver(), "multi_sim_data_call", -1);
  }
  
  public int getDefaultSmsSubId()
  {
    return Settings.Global.getInt(mContext.getContentResolver(), "multi_sim_sms", -1);
  }
  
  public int getDefaultSubId()
  {
    int i;
    if (mContext.getResources().getBoolean(17957076)) {
      i = getDefaultVoiceSubId();
    } else {
      i = getDefaultDataSubId();
    }
    int j = i;
    if (!isActiveSubId(i)) {
      j = mDefaultFallbackSubId;
    }
    return j;
  }
  
  public int getDefaultVoiceSubId()
  {
    return Settings.Global.getInt(mContext.getContentResolver(), "multi_sim_voice_call", -1);
  }
  
  protected int[] getDummySubIds(int paramInt)
  {
    int i = getActiveSubInfoCountMax();
    if (i > 0)
    {
      int[] arrayOfInt = new int[i];
      for (int j = 0; j < i; j++) {
        arrayOfInt[j] = (-2 - paramInt);
      }
      return arrayOfInt;
    }
    return null;
  }
  
  public int getPhoneId(int paramInt)
  {
    int i = paramInt;
    if (paramInt == Integer.MAX_VALUE)
    {
      i = getDefaultSubId();
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("[getPhoneId] asked for default subId=");
      ((StringBuilder)localObject).append(i);
      logdl(((StringBuilder)localObject).toString());
    }
    if (!SubscriptionManager.isValidSubscriptionId(i)) {
      return -1;
    }
    if (sSlotIndexToSubId.size() == 0)
    {
      paramInt = mDefaultPhoneId;
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("[getPhoneId]- no sims, returning default phoneId=");
      ((StringBuilder)localObject).append(paramInt);
      logdl(((StringBuilder)localObject).toString());
      return paramInt;
    }
    Object localObject = sSlotIndexToSubId.entrySet().iterator();
    while (((Iterator)localObject).hasNext())
    {
      Map.Entry localEntry = (Map.Entry)((Iterator)localObject).next();
      paramInt = ((Integer)localEntry.getKey()).intValue();
      if (i == ((Integer)localEntry.getValue()).intValue()) {
        return paramInt;
      }
    }
    paramInt = mDefaultPhoneId;
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("[getPhoneId]- subId=");
    ((StringBuilder)localObject).append(i);
    ((StringBuilder)localObject).append(" not found return default phoneId=");
    ((StringBuilder)localObject).append(paramInt);
    logdl(((StringBuilder)localObject).toString());
    return paramInt;
  }
  
  public int getSimStateForSlotIndex(int paramInt)
  {
    IccCardConstants.State localState;
    Object localObject;
    if (paramInt < 0)
    {
      localState = IccCardConstants.State.UNKNOWN;
      localObject = "invalid slotIndex";
    }
    else
    {
      localObject = PhoneFactory.getPhone(paramInt);
      if (localObject == null)
      {
        localState = IccCardConstants.State.UNKNOWN;
        localObject = "phone == null";
      }
      else
      {
        localObject = ((Phone)localObject).getIccCard();
        if (localObject == null)
        {
          localState = IccCardConstants.State.UNKNOWN;
          localObject = "icc == null";
        }
        else
        {
          localState = ((IccCard)localObject).getState();
          localObject = "";
        }
      }
    }
    return localState.ordinal();
  }
  
  public int getSlotIndex(int paramInt)
  {
    int i = paramInt;
    if (paramInt == Integer.MAX_VALUE) {
      i = getDefaultSubId();
    }
    if (!SubscriptionManager.isValidSubscriptionId(i))
    {
      logd("[getSlotIndex]- subId invalid");
      return -1;
    }
    if (sSlotIndexToSubId.size() == 0)
    {
      logd("[getSlotIndex]- size == 0, return SIM_NOT_INSERTED instead");
      return -1;
    }
    Iterator localIterator = sSlotIndexToSubId.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      paramInt = ((Integer)localEntry.getKey()).intValue();
      if (i == ((Integer)localEntry.getValue()).intValue()) {
        return paramInt;
      }
    }
    logd("[getSlotIndex]- return fail");
    return -1;
  }
  
  @Deprecated
  public int[] getSubId(int paramInt)
  {
    int i = paramInt;
    if (paramInt == Integer.MAX_VALUE) {
      i = getSlotIndex(getDefaultSubId());
    }
    if (!SubscriptionManager.isValidSlotIndex(i))
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("[getSubId]- invalid slotIndex=");
      ((StringBuilder)localObject1).append(i);
      logd(((StringBuilder)localObject1).toString());
      return null;
    }
    if (sSlotIndexToSubId.size() == 0) {
      return getDummySubIds(i);
    }
    Object localObject1 = new ArrayList();
    Iterator localIterator = sSlotIndexToSubId.entrySet().iterator();
    Object localObject2;
    while (localIterator.hasNext())
    {
      localObject2 = (Map.Entry)localIterator.next();
      j = ((Integer)((Map.Entry)localObject2).getKey()).intValue();
      paramInt = ((Integer)((Map.Entry)localObject2).getValue()).intValue();
      if (i == j) {
        ((ArrayList)localObject1).add(Integer.valueOf(paramInt));
      }
    }
    int j = ((ArrayList)localObject1).size();
    if (j > 0)
    {
      localObject2 = new int[j];
      for (paramInt = 0; paramInt < j; paramInt++) {
        localObject2[paramInt] = ((Integer)((ArrayList)localObject1).get(paramInt)).intValue();
      }
      return localObject2;
    }
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("[getSubId]- numSubIds == 0, return DummySubIds slotIndex=");
    ((StringBuilder)localObject1).append(i);
    logd(((StringBuilder)localObject1).toString());
    return getDummySubIds(i);
  }
  
  public int getSubIdUsingPhoneId(int paramInt)
  {
    int[] arrayOfInt = getSubId(paramInt);
    if ((arrayOfInt != null) && (arrayOfInt.length != 0)) {
      return arrayOfInt[0];
    }
    return -1;
  }
  
  @VisibleForTesting
  public List<SubscriptionInfo> getSubInfoUsingSlotIndexPrivileged(int paramInt, boolean paramBoolean)
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("[getSubInfoUsingSlotIndexPrivileged]+ slotIndex:");
    ((StringBuilder)localObject1).append(paramInt);
    logd(((StringBuilder)localObject1).toString());
    int i = paramInt;
    if (paramInt == Integer.MAX_VALUE) {
      i = getSlotIndex(getDefaultSubId());
    }
    boolean bool = SubscriptionManager.isValidSlotIndex(i);
    Object localObject3 = null;
    localObject1 = null;
    if (!bool)
    {
      logd("[getSubInfoUsingSlotIndexPrivileged]- invalid slotIndex");
      return null;
    }
    if ((paramBoolean) && (!isSubInfoReady()))
    {
      logd("[getSubInfoUsingSlotIndexPrivileged]- not ready");
      return null;
    }
    Cursor localCursor = mContext.getContentResolver().query(SubscriptionManager.CONTENT_URI, null, "sim_id=?", new String[] { String.valueOf(i) }, null);
    if (localCursor != null) {
      for (;;)
      {
        localObject3 = localObject1;
        try
        {
          if (localCursor.moveToNext())
          {
            SubscriptionInfo localSubscriptionInfo = getSubInfoRecord(localCursor);
            localObject3 = localObject1;
            if (localSubscriptionInfo != null)
            {
              localObject3 = localObject1;
              if (localObject1 == null)
              {
                localObject3 = new java/util/ArrayList;
                ((ArrayList)localObject3).<init>();
              }
              ((ArrayList)localObject3).add(localSubscriptionInfo);
            }
            localObject1 = localObject3;
          }
        }
        finally
        {
          if (localCursor != null) {
            localCursor.close();
          }
        }
      }
    }
    if (localCursor != null) {
      localCursor.close();
    }
    logd("[getSubInfoUsingSlotIndex]- null info return");
    return localObject3;
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public List<SubscriptionInfo> getSubscriptionInfoListForEmbeddedSubscriptionUpdate(String[] paramArrayOfString, boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("(");
    localStringBuilder.append("is_embedded");
    localStringBuilder.append("=1");
    if (paramBoolean)
    {
      localStringBuilder.append(" AND ");
      localStringBuilder.append("is_removable");
      localStringBuilder.append("=1");
    }
    localStringBuilder.append(") OR ");
    localStringBuilder.append("icc_id");
    localStringBuilder.append(" IN (");
    for (int i = 0; i < paramArrayOfString.length; i++)
    {
      if (i > 0) {
        localStringBuilder.append(",");
      }
      localStringBuilder.append("\"");
      localStringBuilder.append(paramArrayOfString[i]);
      localStringBuilder.append("\"");
    }
    localStringBuilder.append(")");
    paramArrayOfString = getSubInfo(localStringBuilder.toString(), null);
    if (paramArrayOfString == null) {
      return Collections.emptyList();
    }
    return paramArrayOfString;
  }
  
  public String getSubscriptionProperty(int paramInt, String paramString1, String paramString2)
  {
    if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(mContext, paramInt, paramString2, "getSubscriptionProperty")) {
      return null;
    }
    Object localObject1 = null;
    paramString2 = null;
    Object localObject2 = mContext.getContentResolver();
    Uri localUri = SubscriptionManager.CONTENT_URI;
    int i = 1;
    Object localObject3 = new StringBuilder();
    ((StringBuilder)localObject3).append(paramInt);
    ((StringBuilder)localObject3).append("");
    localObject3 = ((StringBuilder)localObject3).toString();
    localObject2 = ((ContentResolver)localObject2).query(localUri, new String[] { paramString1 }, "_id=?", new String[] { localObject3 }, null);
    if (localObject2 != null) {
      try
      {
        if (((Cursor)localObject2).moveToFirst())
        {
          switch (paramString1.hashCode())
          {
          default: 
            break;
          case 1604840288: 
            if (paramString1.equals("wfc_ims_roaming_enabled")) {
              paramInt = 17;
            }
            break;
          case 1334635646: 
            if (paramString1.equals("wfc_ims_mode")) {
              paramInt = 15;
            }
            break;
          case 1288054979: 
            if (paramString1.equals("enable_channel_50_alerts")) {
              paramInt = 9;
            }
            break;
          case 1270593452: 
            if (paramString1.equals("enable_etws_test_alerts")) {
              paramInt = 8;
            }
            break;
          case 462555599: 
            if (paramString1.equals("alert_reminder_interval")) {
              paramInt = 5;
            }
            break;
          case 407275608: 
            if (paramString1.equals("enable_cmas_severe_threat_alerts")) {
              paramInt = i;
            }
            break;
          case 240841894: 
            if (paramString1.equals("show_cmas_opt_out_dialog")) {
              paramInt = 11;
            }
            break;
          case 203677434: 
            if (paramString1.equals("enable_cmas_amber_alerts")) {
              paramInt = 2;
            }
            break;
          case 180938212: 
            if (paramString1.equals("wfc_ims_roaming_mode")) {
              paramInt = 16;
            }
            break;
          case -349439993: 
            if (paramString1.equals("alert_sound_duration")) {
              paramInt = 4;
            }
            break;
          case -420099376: 
            if (paramString1.equals("vt_ims_enabled")) {
              paramInt = 13;
            }
            break;
          case -461686719: 
            if (paramString1.equals("enable_emergency_alerts")) {
              paramInt = 3;
            }
            break;
          case -1218173306: 
            if (paramString1.equals("wfc_ims_enabled")) {
              paramInt = 14;
            }
            break;
          case -1390801311: 
            if (paramString1.equals("enable_alert_speech")) {
              paramInt = 7;
            }
            break;
          case -1433878403: 
            if (paramString1.equals("enable_cmas_test_alerts")) {
              paramInt = 10;
            }
            break;
          case -1555340190: 
            if (paramString1.equals("enable_cmas_extreme_threat_alerts")) {
              paramInt = 0;
            }
            break;
          case -1950380197: 
            if (paramString1.equals("volte_vt_enabled")) {
              paramInt = 12;
            }
            break;
          case -2000412720: 
            if (paramString1.equals("enable_alert_vibrate")) {
              paramInt = 6;
            }
            break;
          }
          paramInt = -1;
          switch (paramInt)
          {
          default: 
            break;
          case 0: 
          case 1: 
          case 2: 
          case 3: 
          case 4: 
          case 5: 
          case 6: 
          case 7: 
          case 8: 
          case 9: 
          case 10: 
          case 11: 
          case 12: 
          case 13: 
          case 14: 
          case 15: 
          case 16: 
          case 17: 
            paramString1 = new java/lang/StringBuilder;
            paramString1.<init>();
            paramString1.append(((Cursor)localObject2).getInt(0));
            paramString1.append("");
            paramString1 = paramString1.toString();
            break;
          }
          logd("Invalid column name");
          paramString1 = paramString2;
        }
        else
        {
          logd("Valid row not present in db");
          paramString1 = localObject1;
        }
      }
      finally
      {
        break label775;
      }
    }
    logd("Query failed");
    paramString1 = localObject1;
    if (localObject2 != null) {
      ((Cursor)localObject2).close();
    }
    paramString2 = new StringBuilder();
    paramString2.append("getSubscriptionProperty Query value = ");
    paramString2.append(paramString1);
    logd(paramString2.toString());
    return paramString1;
    label775:
    if (localObject2 != null) {
      ((Cursor)localObject2).close();
    }
    throw paramString1;
  }
  
  protected void init(Context paramContext)
  {
    mContext = paramContext;
    mCM = CallManager.getInstance();
    mTelephonyManager = TelephonyManager.from(mContext);
    mAppOps = ((AppOpsManager)mContext.getSystemService("appops"));
    if (ServiceManager.getService("isub") == null)
    {
      ServiceManager.addService("isub", this);
      mLastISubServiceRegTime = System.currentTimeMillis();
    }
    logdl("[SubscriptionController] init by Context");
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  public Uri insertEmptySubInfoRecord(String paramString, int paramInt)
  {
    ContentResolver localContentResolver = mContext.getContentResolver();
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("icc_id", paramString);
    localContentValues.put("color", Integer.valueOf(getUnusedColor(mContext.getOpPackageName())));
    localContentValues.put("sim_id", Integer.valueOf(paramInt));
    localContentValues.put("carrier_name", "");
    Object localObject = UiccController.getInstance().getUiccCardForPhone(paramInt);
    if (localObject != null)
    {
      localObject = ((UiccCard)localObject).getCardId();
      if (localObject != null) {
        localContentValues.put("card_id", (String)localObject);
      } else {
        localContentValues.put("card_id", paramString);
      }
    }
    else
    {
      localContentValues.put("card_id", paramString);
    }
    return localContentResolver.insert(SubscriptionManager.CONTENT_URI, localContentValues);
  }
  
  public boolean isActiveSubId(int paramInt)
  {
    boolean bool;
    if ((SubscriptionManager.isValidSubscriptionId(paramInt)) && (sSlotIndexToSubId.containsValue(Integer.valueOf(paramInt)))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected void logdl(String paramString)
  {
    logd(paramString);
    mLocalLog.log(paramString);
  }
  
  protected void logel(String paramString)
  {
    loge(paramString);
    mLocalLog.log(paramString);
  }
  
  protected void logv(String paramString)
  {
    Rlog.v("SubscriptionController", paramString);
  }
  
  protected void logvl(String paramString)
  {
    logv(paramString);
    mLocalLog.log(paramString);
  }
  
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PRIVATE)
  public void migrateImsSettings()
  {
    Object localObject = SubscriptionManager.from(mContext);
    if (localObject != null)
    {
      localObject = ((SubscriptionManager)localObject).getAllSubscriptionInfoList().iterator();
      while (((Iterator)localObject).hasNext())
      {
        int i = ((SubscriptionInfo)((Iterator)localObject).next()).getSubscriptionId();
        if (i != -1)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("volte_vt_enabled");
          localStringBuilder.append(i);
          migrateImsSettingHelper(localStringBuilder.toString(), "volte_vt_enabled", i);
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("vt_ims_enabled");
          localStringBuilder.append(i);
          migrateImsSettingHelper(localStringBuilder.toString(), "vt_ims_enabled", i);
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("wfc_ims_enabled");
          localStringBuilder.append(i);
          migrateImsSettingHelper(localStringBuilder.toString(), "wfc_ims_enabled", i);
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("wfc_ims_mode");
          localStringBuilder.append(i);
          migrateImsSettingHelper(localStringBuilder.toString(), "wfc_ims_mode", i);
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("wfc_ims_roaming_mode");
          localStringBuilder.append(i);
          migrateImsSettingHelper(localStringBuilder.toString(), "wfc_ims_roaming_mode", i);
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("wfc_ims_roaming_enabled");
          localStringBuilder.append(i);
          migrateImsSettingHelper(localStringBuilder.toString(), "wfc_ims_roaming_enabled", i);
        }
      }
    }
  }
  
  public void notifySubscriptionInfoChanged()
  {
    ITelephonyRegistry localITelephonyRegistry = ITelephonyRegistry.Stub.asInterface(ServiceManager.getService("telephony.registry"));
    try
    {
      logd("notifySubscriptionInfoChanged:");
      localITelephonyRegistry.notifySubscriptionInfoChanged();
    }
    catch (RemoteException localRemoteException) {}
    broadcastSimInfoContentChanged();
  }
  
  @VisibleForTesting
  public void refreshCachedActiveSubscriptionInfoList()
  {
    if (!isSubInfoReady()) {
      return;
    }
    synchronized (mCacheActiveSubInfoList)
    {
      mCacheActiveSubInfoList.clear();
      List localList2 = getSubInfo("sim_id>=0", null);
      if (localList2 != null)
      {
        localList2.sort(SUBSCRIPTION_INFO_COMPARATOR);
        mCacheActiveSubInfoList.addAll(localList2);
      }
      return;
    }
  }
  
  public void requestEmbeddedSubscriptionInfoListRefresh()
  {
    mContext.enforceCallingOrSelfPermission("android.permission.WRITE_EMBEDDED_SUBSCRIPTIONS", "requestEmbeddedSubscriptionInfoListRefresh");
    long l = Binder.clearCallingIdentity();
    try
    {
      PhoneFactory.requestEmbeddedSubscriptionInfoListRefresh(null);
      return;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  public void requestEmbeddedSubscriptionInfoListRefresh(Runnable paramRunnable)
  {
    PhoneFactory.requestEmbeddedSubscriptionInfoListRefresh(paramRunnable);
  }
  
  public int setDataRoaming(int paramInt1, int paramInt2)
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("[setDataRoaming]+ roaming:");
    ((StringBuilder)localObject1).append(paramInt1);
    ((StringBuilder)localObject1).append(" subId:");
    ((StringBuilder)localObject1).append(paramInt2);
    logd(((StringBuilder)localObject1).toString());
    enforceModifyPhoneState("setDataRoaming");
    long l = Binder.clearCallingIdentity();
    try
    {
      validateSubId(paramInt2);
      if (paramInt1 < 0)
      {
        logd("[setDataRoaming]- fail");
        return -1;
      }
      localObject1 = new android/content/ContentValues;
      ((ContentValues)localObject1).<init>(1);
      ((ContentValues)localObject1).put("data_roaming", Integer.valueOf(paramInt1));
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("[setDataRoaming]- roaming:");
      localStringBuilder.append(paramInt1);
      localStringBuilder.append(" set");
      logd(localStringBuilder.toString());
      ContentResolver localContentResolver = mContext.getContentResolver();
      Uri localUri = SubscriptionManager.CONTENT_URI;
      localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("_id=");
      localStringBuilder.append(Long.toString(paramInt2));
      paramInt1 = localContentResolver.update(localUri, (ContentValues)localObject1, localStringBuilder.toString(), null);
      refreshCachedActiveSubscriptionInfoList();
      notifySubscriptionInfoChanged();
      return paramInt1;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  public void setDefaultDataSubId(int paramInt)
  {
    enforceModifyPhoneState("setDefaultDataSubId");
    if (paramInt != Integer.MAX_VALUE)
    {
      ProxyController localProxyController = ProxyController.getInstance();
      int i = sPhones.length;
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("[setDefaultDataSubId] num phones=");
      ((StringBuilder)localObject).append(i);
      ((StringBuilder)localObject).append(", subId=");
      ((StringBuilder)localObject).append(paramInt);
      logdl(((StringBuilder)localObject).toString());
      if (SubscriptionManager.isValidSubscriptionId(paramInt))
      {
        localObject = new RadioAccessFamily[i];
        int j = 0;
        for (int k = 0; k < i; k++)
        {
          int m = sPhones[k].getSubId();
          int n;
          if (m == paramInt)
          {
            n = localProxyController.getMaxRafSupported();
            j = 1;
          }
          else
          {
            n = localProxyController.getMinRafSupported();
          }
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("[setDefaultDataSubId] phoneId=");
          localStringBuilder.append(k);
          localStringBuilder.append(" subId=");
          localStringBuilder.append(m);
          localStringBuilder.append(" RAF=");
          localStringBuilder.append(n);
          logdl(localStringBuilder.toString());
          localObject[k] = new RadioAccessFamily(k, n);
        }
        if (j != 0) {
          localProxyController.setRadioCapability((RadioAccessFamily[])localObject);
        } else {
          logdl("[setDefaultDataSubId] no valid subId's found - not updating.");
        }
      }
      Settings.Global.putInt(mContext.getContentResolver(), "multi_sim_data_call", paramInt);
      updateAllDataConnectionTrackers();
      broadcastDefaultDataSubIdChanged(paramInt);
      return;
    }
    throw new RuntimeException("setDefaultDataSubId called with DEFAULT_SUB_ID");
  }
  
  protected void setDefaultFallbackSubId(int paramInt)
  {
    if (paramInt != Integer.MAX_VALUE)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("[setDefaultFallbackSubId] subId=");
      ((StringBuilder)localObject).append(paramInt);
      logdl(((StringBuilder)localObject).toString());
      if (SubscriptionManager.isValidSubscriptionId(paramInt))
      {
        int i = getPhoneId(paramInt);
        if ((i >= 0) && ((i < mTelephonyManager.getPhoneCount()) || (mTelephonyManager.getSimCount() == 1)))
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("[setDefaultFallbackSubId] set mDefaultFallbackSubId=");
          ((StringBuilder)localObject).append(paramInt);
          logdl(((StringBuilder)localObject).toString());
          mDefaultFallbackSubId = paramInt;
          localObject = mTelephonyManager.getSimOperatorNumericForPhone(i);
          MccTable.updateMccMncConfiguration(mContext, (String)localObject, false);
          Intent localIntent = new Intent("android.telephony.action.DEFAULT_SUBSCRIPTION_CHANGED");
          localIntent.addFlags(553648128);
          SubscriptionManager.putPhoneIdAndSubIdExtra(localIntent, i, paramInt);
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("[setDefaultFallbackSubId] broadcast default subId changed phoneId=");
          ((StringBuilder)localObject).append(i);
          ((StringBuilder)localObject).append(" subId=");
          ((StringBuilder)localObject).append(paramInt);
          logdl(((StringBuilder)localObject).toString());
          mContext.sendStickyBroadcastAsUser(localIntent, UserHandle.ALL);
        }
        else
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("[setDefaultFallbackSubId] not set invalid phoneId=");
          ((StringBuilder)localObject).append(i);
          ((StringBuilder)localObject).append(" subId=");
          ((StringBuilder)localObject).append(paramInt);
          logdl(((StringBuilder)localObject).toString());
        }
      }
      return;
    }
    throw new RuntimeException("setDefaultSubId called with DEFAULT_SUB_ID");
  }
  
  public void setDefaultSmsSubId(int paramInt)
  {
    enforceModifyPhoneState("setDefaultSmsSubId");
    if (paramInt != Integer.MAX_VALUE)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("[setDefaultSmsSubId] subId=");
      localStringBuilder.append(paramInt);
      logdl(localStringBuilder.toString());
      Settings.Global.putInt(mContext.getContentResolver(), "multi_sim_sms", paramInt);
      broadcastDefaultSmsSubIdChanged(paramInt);
      return;
    }
    throw new RuntimeException("setDefaultSmsSubId called with DEFAULT_SUB_ID");
  }
  
  public void setDefaultVoiceSubId(int paramInt)
  {
    enforceModifyPhoneState("setDefaultVoiceSubId");
    if (paramInt != Integer.MAX_VALUE)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("[setDefaultVoiceSubId] subId=");
      localStringBuilder.append(paramInt);
      logdl(localStringBuilder.toString());
      Settings.Global.putInt(mContext.getContentResolver(), "multi_sim_voice_call", paramInt);
      broadcastDefaultVoiceSubIdChanged(paramInt);
      return;
    }
    throw new RuntimeException("setDefaultVoiceSubId called with DEFAULT_SUB_ID");
  }
  
  public int setDisplayName(String paramString, int paramInt)
  {
    return setDisplayNameUsingSrc(paramString, paramInt, -1L);
  }
  
  public int setDisplayNameUsingSrc(String paramString, int paramInt, long paramLong)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("[setDisplayName]+  displayName:");
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append(" subId:");
    ((StringBuilder)localObject).append(paramInt);
    ((StringBuilder)localObject).append(" nameSource:");
    ((StringBuilder)localObject).append(paramLong);
    logd(((StringBuilder)localObject).toString());
    enforceModifyPhoneState("setDisplayNameUsingSrc");
    long l = Binder.clearCallingIdentity();
    try
    {
      validateSubId(paramInt);
      if (paramString == null) {
        paramString = mContext.getString(17039374);
      }
      localObject = new android/content/ContentValues;
      ((ContentValues)localObject).<init>(1);
      ((ContentValues)localObject).put("display_name", paramString);
      if (paramLong >= 0L)
      {
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Set nameSource=");
        localStringBuilder.append(paramLong);
        logd(localStringBuilder.toString());
        ((ContentValues)localObject).put("name_source", Long.valueOf(paramLong));
      }
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("[setDisplayName]- mDisplayName:");
      localStringBuilder.append(paramString);
      localStringBuilder.append(" set");
      logd(localStringBuilder.toString());
      paramString = mContext.getContentResolver();
      Uri localUri = SubscriptionManager.CONTENT_URI;
      localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("_id=");
      localStringBuilder.append(Long.toString(paramInt));
      paramInt = paramString.update(localUri, (ContentValues)localObject, localStringBuilder.toString(), null);
      refreshCachedActiveSubscriptionInfoList();
      notifySubscriptionInfoChanged();
      return paramInt;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  public int setDisplayNumber(String paramString, int paramInt)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("[setDisplayNumber]+ subId:");
    ((StringBuilder)localObject).append(paramInt);
    logd(((StringBuilder)localObject).toString());
    enforceModifyPhoneState("setDisplayNumber");
    long l = Binder.clearCallingIdentity();
    try
    {
      validateSubId(paramInt);
      int i = getPhoneId(paramInt);
      if ((paramString != null) && (i >= 0) && (i < mTelephonyManager.getPhoneCount()))
      {
        localObject = new android/content/ContentValues;
        ((ContentValues)localObject).<init>(1);
        ((ContentValues)localObject).put("number", paramString);
        paramString = mContext.getContentResolver();
        Uri localUri = SubscriptionManager.CONTENT_URI;
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("_id=");
        localStringBuilder.append(Long.toString(paramInt));
        paramInt = paramString.update(localUri, (ContentValues)localObject, localStringBuilder.toString(), null);
        refreshCachedActiveSubscriptionInfoList();
        paramString = new java/lang/StringBuilder;
        paramString.<init>();
        paramString.append("[setDisplayNumber]- update result :");
        paramString.append(paramInt);
        logd(paramString.toString());
        notifySubscriptionInfoChanged();
        return paramInt;
      }
      logd("[setDispalyNumber]- fail");
      return -1;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  public int setIconTint(int paramInt1, int paramInt2)
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("[setIconTint]+ tint:");
    ((StringBuilder)localObject1).append(paramInt1);
    ((StringBuilder)localObject1).append(" subId:");
    ((StringBuilder)localObject1).append(paramInt2);
    logd(((StringBuilder)localObject1).toString());
    enforceModifyPhoneState("setIconTint");
    long l = Binder.clearCallingIdentity();
    try
    {
      validateSubId(paramInt2);
      localObject1 = new android/content/ContentValues;
      ((ContentValues)localObject1).<init>(1);
      ((ContentValues)localObject1).put("color", Integer.valueOf(paramInt1));
      Object localObject3 = new java/lang/StringBuilder;
      ((StringBuilder)localObject3).<init>();
      ((StringBuilder)localObject3).append("[setIconTint]- tint:");
      ((StringBuilder)localObject3).append(paramInt1);
      ((StringBuilder)localObject3).append(" set");
      logd(((StringBuilder)localObject3).toString());
      ContentResolver localContentResolver = mContext.getContentResolver();
      localObject3 = SubscriptionManager.CONTENT_URI;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("_id=");
      localStringBuilder.append(Long.toString(paramInt2));
      paramInt1 = localContentResolver.update((Uri)localObject3, (ContentValues)localObject1, localStringBuilder.toString(), null);
      refreshCachedActiveSubscriptionInfoList();
      notifySubscriptionInfoChanged();
      return paramInt1;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }
  
  public int setMccMnc(String paramString, int paramInt)
  {
    int i = 0;
    int j = 0;
    int k;
    try
    {
      k = Integer.parseInt(paramString.substring(0, 3));
      i = k;
      int m = Integer.parseInt(paramString.substring(3));
      i = m;
      j = k;
      k = i;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("[setMccMnc] - couldn't parse mcc/mnc: ");
      ((StringBuilder)localObject).append(paramString);
      loge(((StringBuilder)localObject).toString());
      k = j;
      j = i;
    }
    paramString = new StringBuilder();
    paramString.append("[setMccMnc]+ mcc/mnc:");
    paramString.append(j);
    paramString.append("/");
    paramString.append(k);
    paramString.append(" subId:");
    paramString.append(paramInt);
    logd(paramString.toString());
    ContentValues localContentValues = new ContentValues(2);
    localContentValues.put("mcc", Integer.valueOf(j));
    localContentValues.put("mnc", Integer.valueOf(k));
    Object localObject = mContext.getContentResolver();
    paramString = SubscriptionManager.CONTENT_URI;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("_id=");
    localStringBuilder.append(Long.toString(paramInt));
    paramInt = ((ContentResolver)localObject).update(paramString, localContentValues, localStringBuilder.toString(), null);
    refreshCachedActiveSubscriptionInfoList();
    notifySubscriptionInfoChanged();
    return paramInt;
  }
  
  public boolean setPlmnSpn(int paramInt, boolean paramBoolean1, String paramString1, boolean paramBoolean2, String paramString2)
  {
    synchronized (mLock)
    {
      paramInt = getSubIdUsingPhoneId(paramInt);
      if ((mContext.getPackageManager().resolveContentProvider(SubscriptionManager.CONTENT_URI.getAuthority(), 0) != null) && (SubscriptionManager.isValidSubscriptionId(paramInt)))
      {
        Object localObject2 = "";
        if (paramBoolean1)
        {
          String str = paramString1;
          localObject2 = str;
          if (paramBoolean2)
          {
            localObject2 = str;
            if (!Objects.equals(paramString2, paramString1))
            {
              localObject2 = mContext.getString(17040217).toString();
              paramString1 = new java/lang/StringBuilder;
              paramString1.<init>();
              paramString1.append(str);
              paramString1.append((String)localObject2);
              paramString1.append(paramString2);
              localObject2 = paramString1.toString();
            }
          }
        }
        else if (paramBoolean2)
        {
          localObject2 = paramString2;
        }
        setCarrierText((String)localObject2, paramInt);
        return true;
      }
      logd("[setPlmnSpn] No valid subscription to store info");
      notifySubscriptionInfoChanged();
      return false;
    }
  }
  
  public void setSubscriptionProperty(int paramInt, String paramString1, String paramString2)
  {
    enforceModifyPhoneState("setSubscriptionProperty");
    long l = Binder.clearCallingIdentity();
    setSubscriptionPropertyIntoContentResolver(paramInt, paramString1, paramString2, mContext.getContentResolver());
    refreshCachedActiveSubscriptionInfoList();
    Binder.restoreCallingIdentity(l);
  }
  
  protected boolean shouldDefaultBeCleared(List<SubscriptionInfo> paramList, int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[shouldDefaultBeCleared: subId] ");
    localStringBuilder.append(paramInt);
    logdl(localStringBuilder.toString());
    if (paramList == null)
    {
      paramList = new StringBuilder();
      paramList.append("[shouldDefaultBeCleared] return true no records subId=");
      paramList.append(paramInt);
      logdl(paramList.toString());
      return true;
    }
    if (!SubscriptionManager.isValidSubscriptionId(paramInt))
    {
      paramList = new StringBuilder();
      paramList.append("[shouldDefaultBeCleared] return false only one subId, subId=");
      paramList.append(paramInt);
      logdl(paramList.toString());
      return false;
    }
    paramList = paramList.iterator();
    while (paramList.hasNext())
    {
      int i = ((SubscriptionInfo)paramList.next()).getSubscriptionId();
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("[shouldDefaultBeCleared] Record.id: ");
      localStringBuilder.append(i);
      logdl(localStringBuilder.toString());
      if (i == paramInt)
      {
        paramList = new StringBuilder();
        paramList.append("[shouldDefaultBeCleared] return false subId is active, subId=");
        paramList.append(paramInt);
        logdl(paramList.toString());
        return false;
      }
    }
    paramList = new StringBuilder();
    paramList.append("[shouldDefaultBeCleared] return true not active subId=");
    paramList.append(paramInt);
    logdl(paramList.toString());
    return true;
  }
  
  protected void updateAllDataConnectionTrackers()
  {
    int i = sPhones.length;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[updateAllDataConnectionTrackers] sPhones.length=");
    localStringBuilder.append(i);
    logdl(localStringBuilder.toString());
    for (int j = 0; j < i; j++)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("[updateAllDataConnectionTrackers] phoneId=");
      localStringBuilder.append(j);
      logdl(localStringBuilder.toString());
      sPhones[j].updateDataConnectionTracker();
    }
  }
  
  public void updatePhonesAvailability(Phone[] paramArrayOfPhone)
  {
    sPhones = paramArrayOfPhone;
  }
  
  static class ScLocalLog
  {
    private LinkedList<String> mLog = new LinkedList();
    private int mMaxLines;
    private Time mNow;
    
    public ScLocalLog(int paramInt)
    {
      mMaxLines = paramInt;
      mNow = new Time();
    }
    
    public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
    {
      try
      {
        paramFileDescriptor = mLog;
        int i = 0;
        paramFileDescriptor = paramFileDescriptor.listIterator(0);
        while (paramFileDescriptor.hasNext())
        {
          paramArrayOfString = new java/lang/StringBuilder;
          paramArrayOfString.<init>();
          int j = i + 1;
          paramArrayOfString.append(Integer.toString(i));
          paramArrayOfString.append(": ");
          paramArrayOfString.append((String)paramFileDescriptor.next());
          paramPrintWriter.println(paramArrayOfString.toString());
          if (j % 10 == 0) {
            paramPrintWriter.flush();
          }
          i = j;
        }
        return;
      }
      finally {}
    }
    
    public void log(String paramString)
    {
      try
      {
        if (mMaxLines > 0)
        {
          int i = Process.myPid();
          int j = Process.myTid();
          mNow.setToNow();
          LinkedList localLinkedList = mLog;
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append(mNow.format("%m-%d %H:%M:%S"));
          localStringBuilder.append(" pid=");
          localStringBuilder.append(i);
          localStringBuilder.append(" tid=");
          localStringBuilder.append(j);
          localStringBuilder.append(" ");
          localStringBuilder.append(paramString);
          localLinkedList.add(localStringBuilder.toString());
          while (mLog.size() > mMaxLines) {
            mLog.remove();
          }
        }
        return;
      }
      finally {}
    }
  }
}
