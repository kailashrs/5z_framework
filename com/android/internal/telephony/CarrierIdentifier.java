package com.android.internal.telephony;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Telephony.CarrierId;
import android.provider.Telephony.CarrierId.All;
import android.provider.Telephony.Carriers;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.SubscriptionManager.OnSubscriptionsChangedListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.LocalLog;
import com.android.internal.telephony.metrics.TelephonyMetrics;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccProfile;
import com.android.internal.util.IndentingPrintWriter;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CarrierIdentifier
  extends Handler
{
  private static final int CARRIER_ID_DB_UPDATE_EVENT = 6;
  private static final Uri CONTENT_URL_PREFER_APN = Uri.withAppendedPath(Telephony.Carriers.CONTENT_URI, "preferapn");
  private static final boolean DBG = true;
  private static final int ICC_CHANGED_EVENT = 4;
  private static final String LOG_TAG = CarrierIdentifier.class.getSimpleName();
  private static final String OPERATOR_BRAND_OVERRIDE_PREFIX = "operator_branding_";
  private static final int PREFER_APN_UPDATE_EVENT = 5;
  private static final int SIM_ABSENT_EVENT = 2;
  private static final int SIM_LOAD_EVENT = 1;
  private static final int SPN_OVERRIDE_EVENT = 3;
  private static final boolean VDBG = Rlog.isLoggable(LOG_TAG, 2);
  private int mCarrierId = -1;
  private final LocalLog mCarrierIdLocalLog = new LocalLog(20);
  private List<CarrierMatchingRule> mCarrierMatchingRulesOnMccMnc = new ArrayList();
  private String mCarrierName;
  private final ContentObserver mContentObserver = new ContentObserver(this)
  {
    public void onChange(boolean paramAnonymousBoolean, Uri paramAnonymousUri)
    {
      StringBuilder localStringBuilder;
      if (CarrierIdentifier.CONTENT_URL_PREFER_APN.equals(paramAnonymousUri.getLastPathSegment()))
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("onChange URI: ");
        localStringBuilder.append(paramAnonymousUri);
        CarrierIdentifier.logd(localStringBuilder.toString());
        sendEmptyMessage(5);
      }
      else if (Telephony.CarrierId.All.CONTENT_URI.equals(paramAnonymousUri))
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("onChange URI: ");
        localStringBuilder.append(paramAnonymousUri);
        CarrierIdentifier.logd(localStringBuilder.toString());
        sendEmptyMessage(6);
      }
    }
  };
  private Context mContext;
  private IccRecords mIccRecords;
  private final SubscriptionsChangedListener mOnSubscriptionsChangedListener = new SubscriptionsChangedListener(null);
  private Phone mPhone;
  private String mPreferApn;
  private String mSpn = "";
  private final TelephonyManager mTelephonyMgr;
  private UiccProfile mUiccProfile;
  
  public CarrierIdentifier(Phone paramPhone)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Creating CarrierIdentifier[");
    localStringBuilder.append(paramPhone.getPhoneId());
    localStringBuilder.append("]");
    logd(localStringBuilder.toString());
    mContext = paramPhone.getContext();
    mPhone = paramPhone;
    mTelephonyMgr = TelephonyManager.from(mContext);
    mContext.getContentResolver().registerContentObserver(CONTENT_URL_PREFER_APN, false, mContentObserver);
    mContext.getContentResolver().registerContentObserver(Telephony.CarrierId.All.CONTENT_URI, false, mContentObserver);
    SubscriptionManager.from(mContext).addOnSubscriptionsChangedListener(mOnSubscriptionsChangedListener);
    UiccController.getInstance().registerForIccChanged(this, 4, null);
  }
  
  private static boolean equals(String paramString1, String paramString2, boolean paramBoolean)
  {
    if ((paramString1 == null) && (paramString2 == null)) {
      return true;
    }
    if ((paramString1 != null) && (paramString2 != null))
    {
      if (paramBoolean) {
        paramBoolean = paramString1.equalsIgnoreCase(paramString2);
      } else {
        paramBoolean = paramString1.equals(paramString2);
      }
      return paramBoolean;
    }
    return false;
  }
  
  /* Error */
  private String getPreferApn()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 157	com/android/internal/telephony/CarrierIdentifier:mContext	Landroid/content/Context;
    //   4: invokevirtual 173	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   7: astore_1
    //   8: getstatic 81	android/provider/Telephony$Carriers:CONTENT_URI	Landroid/net/Uri;
    //   11: astore_2
    //   12: new 126	java/lang/StringBuilder
    //   15: dup
    //   16: invokespecial 127	java/lang/StringBuilder:<init>	()V
    //   19: astore_3
    //   20: aload_3
    //   21: ldc -31
    //   23: invokevirtual 133	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   26: pop
    //   27: aload_3
    //   28: aload_0
    //   29: getfield 159	com/android/internal/telephony/CarrierIdentifier:mPhone	Lcom/android/internal/telephony/Phone;
    //   32: invokevirtual 228	com/android/internal/telephony/Phone:getSubId	()I
    //   35: invokevirtual 142	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   38: pop
    //   39: aload_1
    //   40: aload_2
    //   41: aload_3
    //   42: invokevirtual 147	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   45: invokestatic 89	android/net/Uri:withAppendedPath	(Landroid/net/Uri;Ljava/lang/String;)Landroid/net/Uri;
    //   48: iconst_1
    //   49: anewarray 213	java/lang/String
    //   52: dup
    //   53: iconst_0
    //   54: ldc -26
    //   56: aastore
    //   57: aconst_null
    //   58: aconst_null
    //   59: aconst_null
    //   60: invokevirtual 234	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   63: astore_2
    //   64: aload_2
    //   65: ifnull +166 -> 231
    //   68: getstatic 76	com/android/internal/telephony/CarrierIdentifier:VDBG	Z
    //   71: ifeq +43 -> 114
    //   74: new 126	java/lang/StringBuilder
    //   77: astore_3
    //   78: aload_3
    //   79: invokespecial 127	java/lang/StringBuilder:<init>	()V
    //   82: aload_3
    //   83: ldc -20
    //   85: invokevirtual 133	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   88: pop
    //   89: aload_3
    //   90: aload_2
    //   91: invokeinterface 241 1 0
    //   96: invokevirtual 142	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   99: pop
    //   100: aload_3
    //   101: ldc -13
    //   103: invokevirtual 133	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   106: pop
    //   107: aload_3
    //   108: invokevirtual 147	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   111: invokestatic 151	com/android/internal/telephony/CarrierIdentifier:logd	(Ljava/lang/String;)V
    //   114: aload_2
    //   115: invokeinterface 247 1 0
    //   120: ifeq +111 -> 231
    //   123: aload_2
    //   124: aload_2
    //   125: ldc -26
    //   127: invokeinterface 251 2 0
    //   132: invokeinterface 255 2 0
    //   137: astore_1
    //   138: new 126	java/lang/StringBuilder
    //   141: astore_3
    //   142: aload_3
    //   143: invokespecial 127	java/lang/StringBuilder:<init>	()V
    //   146: aload_3
    //   147: ldc -20
    //   149: invokevirtual 133	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   152: pop
    //   153: aload_3
    //   154: aload_1
    //   155: invokevirtual 133	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   158: pop
    //   159: aload_3
    //   160: invokevirtual 147	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   163: invokestatic 151	com/android/internal/telephony/CarrierIdentifier:logd	(Ljava/lang/String;)V
    //   166: aload_2
    //   167: ifnull +9 -> 176
    //   170: aload_2
    //   171: invokeinterface 258 1 0
    //   176: aload_1
    //   177: areturn
    //   178: astore_3
    //   179: goto +40 -> 219
    //   182: astore_1
    //   183: new 126	java/lang/StringBuilder
    //   186: astore_3
    //   187: aload_3
    //   188: invokespecial 127	java/lang/StringBuilder:<init>	()V
    //   191: aload_3
    //   192: ldc_w 260
    //   195: invokevirtual 133	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   198: pop
    //   199: aload_3
    //   200: aload_1
    //   201: invokevirtual 263	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   204: pop
    //   205: aload_3
    //   206: invokevirtual 147	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   209: invokestatic 266	com/android/internal/telephony/CarrierIdentifier:loge	(Ljava/lang/String;)V
    //   212: aload_2
    //   213: ifnull +28 -> 241
    //   216: goto +19 -> 235
    //   219: aload_2
    //   220: ifnull +9 -> 229
    //   223: aload_2
    //   224: invokeinterface 258 1 0
    //   229: aload_3
    //   230: athrow
    //   231: aload_2
    //   232: ifnull +9 -> 241
    //   235: aload_2
    //   236: invokeinterface 258 1 0
    //   241: aconst_null
    //   242: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	243	0	this	CarrierIdentifier
    //   7	170	1	localObject1	Object
    //   182	19	1	localException	Exception
    //   11	225	2	localObject2	Object
    //   19	141	3	localStringBuilder1	StringBuilder
    //   178	1	3	localObject3	Object
    //   186	44	3	localStringBuilder2	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   68	114	178	finally
    //   114	166	178	finally
    //   183	212	178	finally
    //   68	114	182	java/lang/Exception
    //   114	166	182	java/lang/Exception
  }
  
  private void loadCarrierMatchingRulesOnMccMnc()
  {
    try
    {
      String str = mTelephonyMgr.getSimOperatorNumericForPhone(mPhone.getPhoneId());
      localObject1 = mContext.getContentResolver().query(Telephony.CarrierId.All.CONTENT_URI, null, "mccmnc=?", new String[] { str }, null);
      if (localObject1 != null) {
        try
        {
          if (VDBG)
          {
            StringBuilder localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append("[loadCarrierMatchingRules]- ");
            localStringBuilder.append(((Cursor)localObject1).getCount());
            localStringBuilder.append(" Records(s) in DB mccmnc: ");
            localStringBuilder.append(str);
            logd(localStringBuilder.toString());
          }
          mCarrierMatchingRulesOnMccMnc.clear();
          while (((Cursor)localObject1).moveToNext()) {
            mCarrierMatchingRulesOnMccMnc.add(makeCarrierMatchingRule((Cursor)localObject1));
          }
          matchCarrier();
        }
        finally
        {
          if (localObject1 != null) {
            ((Cursor)localObject1).close();
          }
        }
      }
      if (localObject1 != null) {
        ((Cursor)localObject1).close();
      }
    }
    catch (Exception localException)
    {
      Object localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("[loadCarrierMatchingRules]- ex: ");
      ((StringBuilder)localObject1).append(localException);
      loge(((StringBuilder)localObject1).toString());
    }
  }
  
  private static void logd(String paramString)
  {
    Rlog.d(LOG_TAG, paramString);
  }
  
  private static void loge(String paramString)
  {
    Rlog.e(LOG_TAG, paramString);
  }
  
  private CarrierMatchingRule makeCarrierMatchingRule(Cursor paramCursor)
  {
    return new CarrierMatchingRule(paramCursor.getString(paramCursor.getColumnIndexOrThrow("mccmnc")), paramCursor.getString(paramCursor.getColumnIndexOrThrow("imsi_prefix_xpattern")), paramCursor.getString(paramCursor.getColumnIndexOrThrow("iccid_prefix")), paramCursor.getString(paramCursor.getColumnIndexOrThrow("gid1")), paramCursor.getString(paramCursor.getColumnIndexOrThrow("gid2")), paramCursor.getString(paramCursor.getColumnIndexOrThrow("plmn")), paramCursor.getString(paramCursor.getColumnIndexOrThrow("spn")), paramCursor.getString(paramCursor.getColumnIndexOrThrow("apn")), paramCursor.getInt(paramCursor.getColumnIndexOrThrow("carrier_id")), paramCursor.getString(paramCursor.getColumnIndexOrThrow("carrier_name")));
  }
  
  private void matchCarrier()
  {
    if (!SubscriptionManager.isValidSubscriptionId(mPhone.getSubId()))
    {
      logd("[matchCarrier]skip before sim records loaded");
      return;
    }
    Object localObject1 = mTelephonyMgr.getSimOperatorNumericForPhone(mPhone.getPhoneId());
    Object localObject2 = mPhone.getIccSerialNumber();
    String str1 = mPhone.getGroupIdLevel1();
    String str2 = mPhone.getGroupIdLevel2();
    String str3 = mPhone.getSubscriberId();
    Object localObject3 = mPhone.getPlmn();
    Object localObject4 = mSpn;
    String str4 = mPreferApn;
    if (VDBG)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("[matchCarrier] mnnmnc:");
      localStringBuilder.append((String)localObject1);
      localStringBuilder.append(" gid1: ");
      localStringBuilder.append(str1);
      localStringBuilder.append(" gid2: ");
      localStringBuilder.append(str2);
      localStringBuilder.append(" imsi: ");
      localStringBuilder.append(Rlog.pii(LOG_TAG, str3));
      localStringBuilder.append(" iccid: ");
      localStringBuilder.append(Rlog.pii(LOG_TAG, localObject2));
      localStringBuilder.append(" plmn: ");
      localStringBuilder.append((String)localObject3);
      localStringBuilder.append(" spn: ");
      localStringBuilder.append((String)localObject4);
      localStringBuilder.append(" apn: ");
      localStringBuilder.append(str4);
      logd(localStringBuilder.toString());
    }
    localObject4 = new CarrierMatchingRule((String)localObject1, str3, (String)localObject2, str1, str2, (String)localObject3, (String)localObject4, str4, -1, null);
    int i = -1;
    localObject2 = null;
    localObject1 = mCarrierMatchingRulesOnMccMnc.iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject3 = (CarrierMatchingRule)((Iterator)localObject1).next();
      ((CarrierMatchingRule)localObject3).match((CarrierMatchingRule)localObject4);
      int j = i;
      if (mScore > i)
      {
        j = mScore;
        localObject2 = localObject3;
      }
      i = j;
    }
    localObject1 = null;
    if (i == -1)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("[matchCarrier - no match] cid: -1 name: ");
      ((StringBuilder)localObject2).append(null);
      logd(((StringBuilder)localObject2).toString());
      updateCarrierIdAndName(-1, null);
    }
    else
    {
      localObject3 = new StringBuilder();
      ((StringBuilder)localObject3).append("[matchCarrier] cid: ");
      ((StringBuilder)localObject3).append(mCid);
      ((StringBuilder)localObject3).append(" name: ");
      ((StringBuilder)localObject3).append(mName);
      logd(((StringBuilder)localObject3).toString());
      updateCarrierIdAndName(mCid, mName);
    }
    if (((i & 0x10) == 0) && (!TextUtils.isEmpty(mGid1))) {
      localObject2 = mGid1;
    } else {
      localObject2 = null;
    }
    if (i != -1)
    {
      localObject3 = localObject1;
      if ((i & 0x10) != 0) {}
    }
    else
    {
      localObject3 = localObject1;
      if (!TextUtils.isEmpty(mMccMnc)) {
        localObject3 = mMccMnc;
      }
    }
    TelephonyMetrics.getInstance().writeCarrierIdMatchingEvent(mPhone.getPhoneId(), getCarrierListVersion(), mCarrierId, (String)localObject3, (String)localObject2);
  }
  
  private void updateCarrierIdAndName(int paramInt, String paramString)
  {
    int i = 0;
    Object localObject;
    if (!equals(paramString, mCarrierName, true))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("[updateCarrierName] from:");
      ((StringBuilder)localObject).append(mCarrierName);
      ((StringBuilder)localObject).append(" to:");
      ((StringBuilder)localObject).append(paramString);
      logd(((StringBuilder)localObject).toString());
      mCarrierName = paramString;
      i = 1;
    }
    if (paramInt != mCarrierId)
    {
      paramString = new StringBuilder();
      paramString.append("[updateCarrierId] from:");
      paramString.append(mCarrierId);
      paramString.append(" to:");
      paramString.append(paramInt);
      logd(paramString.toString());
      mCarrierId = paramInt;
      i = 1;
    }
    if (i != 0)
    {
      localObject = mCarrierIdLocalLog;
      paramString = new StringBuilder();
      paramString.append("[updateCarrierIdAndName] cid:");
      paramString.append(mCarrierId);
      paramString.append(" name:");
      paramString.append(mCarrierName);
      ((LocalLog)localObject).log(paramString.toString());
      paramString = new Intent("android.telephony.action.SUBSCRIPTION_CARRIER_IDENTITY_CHANGED");
      paramString.putExtra("android.telephony.extra.CARRIER_ID", mCarrierId);
      paramString.putExtra("android.telephony.extra.CARRIER_NAME", mCarrierName);
      paramString.putExtra("android.telephony.extra.SUBSCRIPTION_ID", mPhone.getSubId());
      mContext.sendBroadcast(paramString);
      paramString = new ContentValues();
      paramString.put("carrier_id", Integer.valueOf(mCarrierId));
      paramString.put("carrier_name", mCarrierName);
      mContext.getContentResolver().update(Uri.withAppendedPath(Telephony.CarrierId.CONTENT_URI, Integer.toString(mPhone.getSubId())), paramString, null, null);
    }
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    IndentingPrintWriter localIndentingPrintWriter = new IndentingPrintWriter(paramPrintWriter, "  ");
    localIndentingPrintWriter.println("mCarrierIdLocalLogs:");
    localIndentingPrintWriter.increaseIndent();
    mCarrierIdLocalLog.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    localIndentingPrintWriter.decreaseIndent();
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("mCarrierId: ");
    paramFileDescriptor.append(mCarrierId);
    localIndentingPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("mCarrierName: ");
    paramFileDescriptor.append(mCarrierName);
    localIndentingPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("version: ");
    paramFileDescriptor.append(getCarrierListVersion());
    localIndentingPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("mCarrierMatchingRules on mccmnc: ");
    paramFileDescriptor.append(mTelephonyMgr.getSimOperatorNumericForPhone(mPhone.getPhoneId()));
    localIndentingPrintWriter.println(paramFileDescriptor.toString());
    localIndentingPrintWriter.increaseIndent();
    paramFileDescriptor = mCarrierMatchingRulesOnMccMnc.iterator();
    while (paramFileDescriptor.hasNext()) {
      localIndentingPrintWriter.println(((CarrierMatchingRule)paramFileDescriptor.next()).toString());
    }
    localIndentingPrintWriter.decreaseIndent();
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("mSpn: ");
    paramFileDescriptor.append(mSpn);
    localIndentingPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("mPreferApn: ");
    paramFileDescriptor.append(mPreferApn);
    localIndentingPrintWriter.println(paramFileDescriptor.toString());
    localIndentingPrintWriter.flush();
  }
  
  public int getCarrierId()
  {
    return mCarrierId;
  }
  
  public int getCarrierListVersion()
  {
    Cursor localCursor = mContext.getContentResolver().query(Uri.withAppendedPath(Telephony.CarrierId.All.CONTENT_URI, "get_version"), null, null, null);
    localCursor.moveToFirst();
    return localCursor.getInt(0);
  }
  
  public String getCarrierName()
  {
    return mCarrierName;
  }
  
  public void handleMessage(Message paramMessage)
  {
    Object localObject;
    if (VDBG)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("handleMessage: ");
      ((StringBuilder)localObject).append(what);
      logd(((StringBuilder)localObject).toString());
    }
    switch (what)
    {
    default: 
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("invalid msg: ");
      ((StringBuilder)localObject).append(what);
      loge(((StringBuilder)localObject).toString());
      break;
    case 5: 
      localObject = getPreferApn();
      if (!equals(mPreferApn, (String)localObject, true))
      {
        paramMessage = new StringBuilder();
        paramMessage.append("[updatePreferApn] from:");
        paramMessage.append(mPreferApn);
        paramMessage.append(" to:");
        paramMessage.append((String)localObject);
        logd(paramMessage.toString());
        mPreferApn = ((String)localObject);
        matchCarrier();
      }
      break;
    case 4: 
      paramMessage = UiccController.getInstance().getIccRecords(mPhone.getPhoneId(), 1);
      if (mIccRecords != paramMessage)
      {
        if (mIccRecords != null)
        {
          logd("Removing stale icc objects.");
          mIccRecords.unregisterForRecordsLoaded(this);
          mIccRecords.unregisterForRecordsOverride(this);
          mIccRecords = null;
        }
        if (paramMessage != null)
        {
          logd("new Icc object");
          paramMessage.registerForRecordsLoaded(this, 1, null);
          paramMessage.registerForRecordsOverride(this, 1, null);
          mIccRecords = paramMessage;
        }
      }
      paramMessage = UiccController.getInstance().getUiccProfileForPhone(mPhone.getPhoneId());
      if (mUiccProfile != paramMessage)
      {
        if (mUiccProfile != null)
        {
          logd("unregister operatorBrandOverride");
          mUiccProfile.unregisterForOperatorBrandOverride(this);
          mUiccProfile = null;
        }
        if (paramMessage != null)
        {
          logd("register operatorBrandOverride");
          paramMessage.registerForOpertorBrandOverride(this, 3, null);
          mUiccProfile = paramMessage;
        }
      }
      break;
    case 3: 
      localObject = mTelephonyMgr.getSimOperatorNameForPhone(mPhone.getPhoneId());
      if (!equals(mSpn, (String)localObject, true))
      {
        paramMessage = new StringBuilder();
        paramMessage.append("[updateSpn] from:");
        paramMessage.append(mSpn);
        paramMessage.append(" to:");
        paramMessage.append((String)localObject);
        logd(paramMessage.toString());
        mSpn = ((String)localObject);
        matchCarrier();
      }
      break;
    case 2: 
      mCarrierMatchingRulesOnMccMnc.clear();
      mSpn = null;
      mPreferApn = null;
      updateCarrierIdAndName(-1, null);
      break;
    case 1: 
    case 6: 
      mSpn = mTelephonyMgr.getSimOperatorNameForPhone(mPhone.getPhoneId());
      mPreferApn = getPreferApn();
      loadCarrierMatchingRulesOnMccMnc();
    }
  }
  
  private static class CarrierMatchingRule
  {
    private static final int SCORE_APN = 1;
    private static final int SCORE_GID1 = 16;
    private static final int SCORE_GID2 = 8;
    private static final int SCORE_ICCID_PREFIX = 32;
    private static final int SCORE_IMSI_PREFIX = 64;
    private static final int SCORE_INVALID = -1;
    private static final int SCORE_MCCMNC = 128;
    private static final int SCORE_PLMN = 4;
    private static final int SCORE_SPN = 2;
    private String mApn;
    private int mCid;
    private String mGid1;
    private String mGid2;
    private String mIccidPrefix;
    private String mImsiPrefixPattern;
    private String mMccMnc;
    private String mName;
    private String mPlmn;
    private int mScore = 0;
    private String mSpn;
    
    CarrierMatchingRule(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, int paramInt, String paramString9)
    {
      mMccMnc = paramString1;
      mImsiPrefixPattern = paramString2;
      mIccidPrefix = paramString3;
      mGid1 = paramString4;
      mGid2 = paramString5;
      mPlmn = paramString6;
      mSpn = paramString7;
      mApn = paramString8;
      mCid = paramInt;
      mName = paramString9;
    }
    
    private boolean iccidPrefixMatch(String paramString1, String paramString2)
    {
      if ((paramString1 != null) && (paramString2 != null)) {
        return paramString1.startsWith(paramString2);
      }
      return false;
    }
    
    private boolean imsiPrefixMatch(String paramString1, String paramString2)
    {
      if (TextUtils.isEmpty(paramString2)) {
        return true;
      }
      if (TextUtils.isEmpty(paramString1)) {
        return false;
      }
      if (paramString1.length() < paramString2.length()) {
        return false;
      }
      for (int i = 0; i < paramString2.length(); i++) {
        if ((paramString2.charAt(i) != 'x') && (paramString2.charAt(i) != 'X') && (paramString2.charAt(i) != paramString1.charAt(i))) {
          return false;
        }
      }
      return true;
    }
    
    public void match(CarrierMatchingRule paramCarrierMatchingRule)
    {
      mScore = 0;
      if (mMccMnc != null)
      {
        if (!CarrierIdentifier.equals(mMccMnc, mMccMnc, false))
        {
          mScore = -1;
          return;
        }
        mScore += 128;
      }
      if (mImsiPrefixPattern != null)
      {
        if (!imsiPrefixMatch(mImsiPrefixPattern, mImsiPrefixPattern))
        {
          mScore = -1;
          return;
        }
        mScore += 64;
      }
      if (mIccidPrefix != null)
      {
        if (!iccidPrefixMatch(mIccidPrefix, mIccidPrefix))
        {
          mScore = -1;
          return;
        }
        mScore += 32;
      }
      if (mGid1 != null)
      {
        if (!CarrierIdentifier.equals(mGid1, mGid1, true))
        {
          mScore = -1;
          return;
        }
        mScore += 16;
      }
      if (mGid2 != null)
      {
        if (!CarrierIdentifier.equals(mGid2, mGid2, true))
        {
          mScore = -1;
          return;
        }
        mScore += 8;
      }
      if (mPlmn != null)
      {
        if (!CarrierIdentifier.equals(mPlmn, mPlmn, true))
        {
          mScore = -1;
          return;
        }
        mScore += 4;
      }
      if (mSpn != null)
      {
        if (!CarrierIdentifier.equals(mSpn, mSpn, true))
        {
          mScore = -1;
          return;
        }
        mScore += 2;
      }
      if (mApn != null)
      {
        if (!CarrierIdentifier.equals(mApn, mApn, true))
        {
          mScore = -1;
          return;
        }
        mScore += 1;
      }
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("[CarrierMatchingRule] - mccmnc: ");
      localStringBuilder.append(mMccMnc);
      localStringBuilder.append(" gid1: ");
      localStringBuilder.append(mGid1);
      localStringBuilder.append(" gid2: ");
      localStringBuilder.append(mGid2);
      localStringBuilder.append(" plmn: ");
      localStringBuilder.append(mPlmn);
      localStringBuilder.append(" imsi_prefix: ");
      localStringBuilder.append(mImsiPrefixPattern);
      localStringBuilder.append(" iccid_prefix");
      localStringBuilder.append(mIccidPrefix);
      localStringBuilder.append(" spn: ");
      localStringBuilder.append(mSpn);
      localStringBuilder.append(" apn: ");
      localStringBuilder.append(mApn);
      localStringBuilder.append(" name: ");
      localStringBuilder.append(mName);
      localStringBuilder.append(" cid: ");
      localStringBuilder.append(mCid);
      localStringBuilder.append(" score: ");
      localStringBuilder.append(mScore);
      return localStringBuilder.toString();
    }
  }
  
  private class SubscriptionsChangedListener
    extends SubscriptionManager.OnSubscriptionsChangedListener
  {
    final AtomicInteger mPreviousSubId = new AtomicInteger(-1);
    
    private SubscriptionsChangedListener() {}
    
    public void onSubscriptionsChanged()
    {
      int i = mPhone.getSubId();
      if (mPreviousSubId.getAndSet(i) != i)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("SubscriptionListener.onSubscriptionInfoChanged subId: ");
        localStringBuilder.append(mPreviousSubId);
        CarrierIdentifier.logd(localStringBuilder.toString());
        if (SubscriptionManager.isValidSubscriptionId(i)) {
          sendEmptyMessage(1);
        } else {
          sendEmptyMessage(2);
        }
      }
    }
  }
}
