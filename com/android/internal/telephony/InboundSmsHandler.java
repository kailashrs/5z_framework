package com.android.internal.telephony;

import android.app.ActivityManager;
import android.app.BroadcastOptions;
import android.app.IActivityManager;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.IPackageManager;
import android.content.pm.IPackageManager.Stub;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Bundle;
import android.os.IDeviceIdleController;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.storage.StorageManager;
import android.provider.Telephony.Sms;
import android.telephony.Rlog;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.HexDump;
import com.android.internal.util.IState;
import com.android.internal.util.State;
import com.android.internal.util.StateMachine;
import com.android.internal.util.StateMachine.LogRec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class InboundSmsHandler
  extends StateMachine
{
  private static String ACTION_OPEN_SMS_APP = "com.android.internal.telephony.OPEN_DEFAULT_SMS_APP";
  public static final int ADDRESS_COLUMN = 6;
  public static final int COUNT_COLUMN = 5;
  public static final int DATE_COLUMN = 3;
  protected static final boolean DBG = true;
  public static final int DESTINATION_PORT_COLUMN = 2;
  public static final int DISPLAY_ADDRESS_COLUMN = 9;
  private static final int EVENT_BROADCAST_COMPLETE = 3;
  public static final int EVENT_BROADCAST_SMS = 2;
  public static final int EVENT_INJECT_SMS = 8;
  public static final int EVENT_NEW_SMS = 1;
  private static final int EVENT_RELEASE_WAKELOCK = 5;
  private static final int EVENT_RETURN_TO_IDLE = 4;
  public static final int EVENT_START_ACCEPTING_SMS = 6;
  private static final int EVENT_UPDATE_PHONE_OBJECT = 7;
  public static final int ID_COLUMN = 7;
  public static final int MESSAGE_BODY_COLUMN = 8;
  private static final int NOTIFICATION_ID_NEW_MESSAGE = 1;
  private static final String NOTIFICATION_TAG = "InboundSmsHandler";
  public static final int PDU_COLUMN = 0;
  private static final String[] PDU_PROJECTION = { "pdu" };
  private static final String[] PDU_SEQUENCE_PORT_PROJECTION = { "pdu", "sequence", "destination_port", "display_originating_addr" };
  private static final Map<Integer, Integer> PDU_SEQUENCE_PORT_PROJECTION_INDEX_MAPPING = new HashMap() {};
  public static final int REFERENCE_NUMBER_COLUMN = 4;
  public static final String SELECT_BY_ID = "_id=?";
  public static final int SEQUENCE_COLUMN = 1;
  private static final boolean VDBG = false;
  private static final int WAKELOCK_TIMEOUT = 3000;
  protected static final Uri sRawUri = Uri.withAppendedPath(Telephony.Sms.CONTENT_URI, "raw");
  protected static final Uri sRawUriPermanentDelete = Uri.withAppendedPath(Telephony.Sms.CONTENT_URI, "raw/permanentDelete");
  private final int DELETE_PERMANENTLY = 1;
  private final int MARK_DELETED = 2;
  protected CellBroadcastHandler mCellBroadcastHandler;
  protected final Context mContext;
  private final DefaultState mDefaultState = new DefaultState(null);
  private final DeliveringState mDeliveringState = new DeliveringState(null);
  IDeviceIdleController mDeviceIdleController;
  private final IdleState mIdleState = new IdleState(null);
  protected Phone mPhone;
  private final ContentResolver mResolver;
  private final boolean mSmsReceiveDisabled;
  private final StartupState mStartupState = new StartupState(null);
  protected SmsStorageMonitor mStorageMonitor;
  private UserManager mUserManager;
  private final WaitingState mWaitingState = new WaitingState(null);
  private final PowerManager.WakeLock mWakeLock;
  private int mWakeLockTimeout;
  private final WapPushOverSms mWapPush;
  
  protected InboundSmsHandler(String paramString, Context paramContext, SmsStorageMonitor paramSmsStorageMonitor, Phone paramPhone, CellBroadcastHandler paramCellBroadcastHandler)
  {
    super(paramString);
    mContext = paramContext;
    mStorageMonitor = paramSmsStorageMonitor;
    mPhone = paramPhone;
    mCellBroadcastHandler = paramCellBroadcastHandler;
    mResolver = paramContext.getContentResolver();
    mWapPush = new WapPushOverSms(paramContext);
    boolean bool = mContext.getResources().getBoolean(17957033);
    mSmsReceiveDisabled = (TelephonyManager.from(mContext).getSmsReceiveCapableForPhone(mPhone.getPhoneId(), bool) ^ true);
    mWakeLock = ((PowerManager)mContext.getSystemService("power")).newWakeLock(1, paramString);
    mWakeLock.acquire();
    mUserManager = ((UserManager)mContext.getSystemService("user"));
    mDeviceIdleController = TelephonyComponentFactory.getInstance().getIDeviceIdleController();
    addState(mDefaultState);
    addState(mStartupState, mDefaultState);
    addState(mIdleState, mDefaultState);
    addState(mDeliveringState, mDefaultState);
    addState(mWaitingState, mDeliveringState);
    setInitialState(mStartupState);
    log("created InboundSmsHandler");
  }
  
  private int addTrackerToRawTable(InboundSmsTracker paramInboundSmsTracker, boolean paramBoolean)
  {
    if (paramBoolean) {
      try
      {
        paramBoolean = duplicateExists(paramInboundSmsTracker);
        if (paramBoolean) {
          return 5;
        }
      }
      catch (SQLException paramInboundSmsTracker)
      {
        loge("Can't access SMS database", paramInboundSmsTracker);
        return 2;
      }
    }
    logd("Skipped message de-duping logic");
    String str1 = paramInboundSmsTracker.getAddress();
    String str2 = Integer.toString(paramInboundSmsTracker.getReferenceNumber());
    String str3 = Integer.toString(paramInboundSmsTracker.getMessageCount());
    Object localObject = paramInboundSmsTracker.getContentValues();
    localObject = mResolver.insert(sRawUri, (ContentValues)localObject);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("URI of new row -> ");
    localStringBuilder.append(localObject);
    log(localStringBuilder.toString());
    try
    {
      long l = ContentUris.parseId((Uri)localObject);
      if (paramInboundSmsTracker.getMessageCount() == 1) {
        paramInboundSmsTracker.setDeleteWhere("_id=?", new String[] { Long.toString(l) });
      } else {
        paramInboundSmsTracker.setDeleteWhere(paramInboundSmsTracker.getQueryForSegments(), new String[] { str1, str2, str3 });
      }
      return 1;
    }
    catch (Exception localException)
    {
      paramInboundSmsTracker = new StringBuilder();
      paramInboundSmsTracker.append("error parsing URI for new row: ");
      paramInboundSmsTracker.append(localObject);
      loge(paramInboundSmsTracker.toString(), localException);
    }
    return 2;
  }
  
  private static String buildMessageBodyFromPdus(SmsMessage[] paramArrayOfSmsMessage)
  {
    int i = paramArrayOfSmsMessage.length;
    int j = 0;
    if (i == 1) {
      return replaceFormFeeds(paramArrayOfSmsMessage[0].getDisplayMessageBody());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    i = paramArrayOfSmsMessage.length;
    while (j < i)
    {
      localStringBuilder.append(paramArrayOfSmsMessage[j].getDisplayMessageBody());
      j++;
    }
    return replaceFormFeeds(localStringBuilder.toString());
  }
  
  static void cancelNewMessageNotification(Context paramContext)
  {
    ((NotificationManager)paramContext.getSystemService("notification")).cancel("InboundSmsHandler", 1);
  }
  
  private void deleteFromRawTable(String paramString, String[] paramArrayOfString, int paramInt)
  {
    Uri localUri;
    if (paramInt == 1) {
      localUri = sRawUriPermanentDelete;
    } else {
      localUri = sRawUri;
    }
    paramInt = mResolver.delete(localUri, paramString, paramArrayOfString);
    if (paramInt == 0)
    {
      loge("No rows were deleted from raw table!");
    }
    else
    {
      paramString = new StringBuilder();
      paramString.append("Deleted ");
      paramString.append(paramInt);
      paramString.append(" rows from raw table.");
      log(paramString.toString());
    }
  }
  
  private int dispatchMessage(SmsMessageBase paramSmsMessageBase)
  {
    if (paramSmsMessageBase == null)
    {
      loge("dispatchSmsMessage: message is null");
      return 2;
    }
    if (mSmsReceiveDisabled)
    {
      log("Received short message on device which doesn't support receiving SMS. Ignored.");
      return 1;
    }
    boolean bool1 = false;
    boolean bool2;
    try
    {
      bool2 = IPackageManager.Stub.asInterface(ServiceManager.getService("package")).isOnlyCoreApps();
    }
    catch (RemoteException localRemoteException)
    {
      bool2 = bool1;
    }
    if (bool2)
    {
      log("Received a short message in encrypted state. Rejecting.");
      return 2;
    }
    return dispatchMessageRadioSpecific(paramSmsMessageBase);
  }
  
  private void dispatchSmsDeliveryIntent(byte[][] paramArrayOfByte, String paramString, int paramInt, SmsBroadcastReceiver paramSmsBroadcastReceiver)
  {
    Intent localIntent = new Intent();
    localIntent.putExtra("pdus", paramArrayOfByte);
    localIntent.putExtra("format", paramString);
    if (paramInt == -1)
    {
      localIntent.setAction("android.provider.Telephony.SMS_DELIVER");
      paramString = SmsApplication.getDefaultSmsApplication(mContext, true);
      if (paramString != null)
      {
        localIntent.setComponent(paramString);
        paramArrayOfByte = new StringBuilder();
        paramArrayOfByte.append("Delivering SMS to: ");
        paramArrayOfByte.append(paramString.getPackageName());
        paramArrayOfByte.append(" ");
        paramArrayOfByte.append(paramString.getClassName());
        log(paramArrayOfByte.toString());
      }
      else
      {
        localIntent.setComponent(null);
      }
      if (SmsManager.getDefault().getAutoPersisting())
      {
        paramArrayOfByte = writeInboxMessage(localIntent);
        if (paramArrayOfByte != null) {
          localIntent.putExtra("uri", paramArrayOfByte.toString());
        }
      }
      if (mPhone.getAppSmsManager().handleSmsReceivedIntent(localIntent))
      {
        dropSms(paramSmsBroadcastReceiver);
        return;
      }
    }
    else
    {
      localIntent.setAction("android.intent.action.DATA_SMS_RECEIVED");
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("sms://localhost:");
      paramArrayOfByte.append(paramInt);
      localIntent.setData(Uri.parse(paramArrayOfByte.toString()));
      localIntent.setComponent(null);
      localIntent.addFlags(16777216);
    }
    dispatchIntent(localIntent, "android.permission.RECEIVE_SMS", 16, handleSmsWhitelisting(localIntent.getComponent()), paramSmsBroadcastReceiver, UserHandle.SYSTEM);
  }
  
  private void dropSms(SmsBroadcastReceiver paramSmsBroadcastReceiver)
  {
    deleteFromRawTable(mDeleteWhere, mDeleteWhereArgs, 2);
    sendMessage(3);
  }
  
  private boolean duplicateExists(InboundSmsTracker paramInboundSmsTracker)
    throws SQLException
  {
    String str1 = paramInboundSmsTracker.getAddress();
    Object localObject1 = Integer.toString(paramInboundSmsTracker.getReferenceNumber());
    Object localObject2 = Integer.toString(paramInboundSmsTracker.getMessageCount());
    String str2 = Integer.toString(paramInboundSmsTracker.getSequenceNumber());
    String str3 = Long.toString(paramInboundSmsTracker.getTimestamp());
    Object localObject3 = paramInboundSmsTracker.getMessageBody();
    if (paramInboundSmsTracker.getMessageCount() == 1) {}
    for (Object localObject4 = "address=? AND reference_number=? AND count=? AND sequence=? AND date=? AND message_body=?";; localObject4 = paramInboundSmsTracker.getQueryForMultiPartDuplicates()) {
      break;
    }
    try
    {
      localObject4 = mResolver.query(sRawUri, PDU_PROJECTION, (String)localObject4, new String[] { str1, localObject1, localObject2, str2, str3, localObject3 }, null);
      if (localObject4 != null) {
        try
        {
          if (((Cursor)localObject4).moveToNext())
          {
            localObject3 = new java/lang/StringBuilder;
            ((StringBuilder)localObject3).<init>();
            ((StringBuilder)localObject3).append("Discarding duplicate message segment, refNumber=");
            ((StringBuilder)localObject3).append((String)localObject1);
            ((StringBuilder)localObject3).append(" seqNumber=");
            ((StringBuilder)localObject3).append(str2);
            ((StringBuilder)localObject3).append(" count=");
            ((StringBuilder)localObject3).append((String)localObject2);
            loge(((StringBuilder)localObject3).toString());
            localObject1 = ((Cursor)localObject4).getString(0);
            localObject2 = paramInboundSmsTracker.getPdu();
            localObject1 = HexDump.hexStringToByteArray((String)localObject1);
            if (!Arrays.equals((byte[])localObject1, paramInboundSmsTracker.getPdu()))
            {
              paramInboundSmsTracker = new java/lang/StringBuilder;
              paramInboundSmsTracker.<init>();
              paramInboundSmsTracker.append("Warning: dup message segment PDU of length ");
              paramInboundSmsTracker.append(localObject2.length);
              paramInboundSmsTracker.append(" is different from existing PDU of length ");
              paramInboundSmsTracker.append(localObject1.length);
              loge(paramInboundSmsTracker.toString());
            }
            if (localObject4 != null) {
              ((Cursor)localObject4).close();
            }
            return true;
          }
        }
        finally
        {
          break label320;
        }
      }
      if (localObject4 != null) {
        ((Cursor)localObject4).close();
      }
      return false;
    }
    finally
    {
      localObject4 = null;
      label320:
      if (localObject4 != null) {
        ((Cursor)localObject4).close();
      }
    }
  }
  
  private boolean filterSms(byte[][] paramArrayOfByte, int paramInt, InboundSmsTracker paramInboundSmsTracker, SmsBroadcastReceiver paramSmsBroadcastReceiver, boolean paramBoolean)
  {
    CarrierServicesSmsFilterCallback localCarrierServicesSmsFilterCallback = new CarrierServicesSmsFilterCallback(paramArrayOfByte, paramInt, paramInboundSmsTracker.getFormat(), paramSmsBroadcastReceiver, paramBoolean);
    if (new CarrierServicesSmsFilter(mContext, mPhone, paramArrayOfByte, paramInt, paramInboundSmsTracker.getFormat(), localCarrierServicesSmsFilterCallback, getName()).filter()) {
      return true;
    }
    if (VisualVoicemailSmsFilter.filter(mContext, paramArrayOfByte, paramInboundSmsTracker.getFormat(), paramInt, mPhone.getSubId()))
    {
      log("Visual voicemail SMS dropped");
      dropSms(paramSmsBroadcastReceiver);
      return true;
    }
    return false;
  }
  
  private void handleInjectSms(AsyncResult paramAsyncResult)
  {
    Object localObject = null;
    int i;
    try
    {
      SmsDispatchersController.SmsInjectionCallback localSmsInjectionCallback = (SmsDispatchersController.SmsInjectionCallback)userObj;
      localObject = localSmsInjectionCallback;
      paramAsyncResult = (SmsMessage)result;
      if (paramAsyncResult == null)
      {
        i = 2;
      }
      else
      {
        localObject = localSmsInjectionCallback;
        i = dispatchMessage(mWrappedSmsMessage);
      }
      localObject = localSmsInjectionCallback;
    }
    catch (RuntimeException paramAsyncResult)
    {
      loge("Exception dispatching message", paramAsyncResult);
      i = 2;
    }
    if (localObject != null) {
      localObject.onSmsInjectedResult(i);
    }
  }
  
  private void handleNewSms(AsyncResult paramAsyncResult)
  {
    if (exception != null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Exception processing incoming SMS: ");
      localStringBuilder.append(exception);
      loge(localStringBuilder.toString());
      return;
    }
    int i;
    try
    {
      i = dispatchMessage(result).mWrappedSmsMessage);
    }
    catch (RuntimeException paramAsyncResult)
    {
      loge("Exception dispatching message", paramAsyncResult);
      i = 2;
    }
    if (i != -1)
    {
      boolean bool = true;
      if (i != 1) {
        bool = false;
      }
      notifyAndAcknowledgeLastIncomingSms(bool, i, null);
    }
  }
  
  private Bundle handleSmsWhitelisting(ComponentName paramComponentName)
  {
    String str;
    if (paramComponentName != null)
    {
      paramComponentName = paramComponentName.getPackageName();
      str = "sms-app";
    }
    else
    {
      paramComponentName = mContext.getPackageName();
      str = "sms-broadcast";
    }
    try
    {
      long l = mDeviceIdleController.addPowerSaveTempWhitelistAppForSms(paramComponentName, 0, str);
      paramComponentName = BroadcastOptions.makeBasic();
      paramComponentName.setTemporaryAppWhitelistDuration(l);
      paramComponentName = paramComponentName.toBundle();
      return paramComponentName;
    }
    catch (RemoteException paramComponentName) {}
    return null;
  }
  
  static boolean isCurrentFormat3gpp2()
  {
    boolean bool;
    if (2 == TelephonyManager.getDefault().getCurrentPhoneType()) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isSkipNotifyFlagSet(int paramInt)
  {
    boolean bool;
    if ((paramInt & 0x2) > 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void notifyAndAcknowledgeLastIncomingSms(boolean paramBoolean, int paramInt, Message paramMessage)
  {
    if (!paramBoolean)
    {
      Intent localIntent = new Intent("android.provider.Telephony.SMS_REJECTED");
      localIntent.putExtra("result", paramInt);
      localIntent.addFlags(16777216);
      mContext.sendBroadcast(localIntent, "android.permission.RECEIVE_SMS");
    }
    acknowledgeLastIncomingSms(paramBoolean, paramInt, paramMessage);
  }
  
  private static ContentValues parseSmsMessage(SmsMessage[] paramArrayOfSmsMessage)
  {
    SmsMessage localSmsMessage = paramArrayOfSmsMessage[0];
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("address", localSmsMessage.getDisplayOriginatingAddress());
    localContentValues.put("body", buildMessageBodyFromPdus(paramArrayOfSmsMessage));
    localContentValues.put("date_sent", Long.valueOf(localSmsMessage.getTimestampMillis()));
    localContentValues.put("date", Long.valueOf(System.currentTimeMillis()));
    localContentValues.put("protocol", Integer.valueOf(localSmsMessage.getProtocolIdentifier()));
    localContentValues.put("seen", Integer.valueOf(0));
    localContentValues.put("read", Integer.valueOf(0));
    paramArrayOfSmsMessage = localSmsMessage.getPseudoSubject();
    if (!TextUtils.isEmpty(paramArrayOfSmsMessage)) {
      localContentValues.put("subject", paramArrayOfSmsMessage);
    }
    localContentValues.put("reply_path_present", Integer.valueOf(localSmsMessage.isReplyPathPresent()));
    localContentValues.put("service_center", localSmsMessage.getServiceCenterAddress());
    return localContentValues;
  }
  
  /* Error */
  private boolean processMessagePart(InboundSmsTracker paramInboundSmsTracker)
  {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual 396	com/android/internal/telephony/InboundSmsTracker:getMessageCount	()I
    //   4: istore_2
    //   5: aload_1
    //   6: invokevirtual 862	com/android/internal/telephony/InboundSmsTracker:getDestPort	()I
    //   9: istore_3
    //   10: iconst_0
    //   11: istore 4
    //   13: aload_1
    //   14: invokevirtual 384	com/android/internal/telephony/InboundSmsTracker:getAddress	()Ljava/lang/String;
    //   17: astore 5
    //   19: iload_2
    //   20: ifgt +35 -> 55
    //   23: new 408	java/lang/StringBuilder
    //   26: dup
    //   27: invokespecial 409	java/lang/StringBuilder:<init>	()V
    //   30: astore_1
    //   31: aload_1
    //   32: ldc_w 864
    //   35: invokevirtual 415	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   38: pop
    //   39: aload_1
    //   40: iload_2
    //   41: invokevirtual 473	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   44: pop
    //   45: aload_0
    //   46: aload_1
    //   47: invokevirtual 420	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   50: invokevirtual 468	com/android/internal/telephony/InboundSmsHandler:loge	(Ljava/lang/String;)V
    //   53: iconst_0
    //   54: ireturn
    //   55: iload_2
    //   56: iconst_1
    //   57: if_icmpne +37 -> 94
    //   60: aload_1
    //   61: invokevirtual 657	com/android/internal/telephony/InboundSmsTracker:getPdu	()[B
    //   64: astore 6
    //   66: aload_0
    //   67: getfield 186	com/android/internal/telephony/InboundSmsHandler:mContext	Landroid/content/Context;
    //   70: aload_1
    //   71: invokevirtual 867	com/android/internal/telephony/InboundSmsTracker:getDisplayAddress	()Ljava/lang/String;
    //   74: aconst_null
    //   75: invokestatic 873	com/android/internal/telephony/BlockChecker:isBlocked	(Landroid/content/Context;Ljava/lang/String;Landroid/os/Bundle;)Z
    //   78: istore 4
    //   80: iconst_1
    //   81: anewarray 875	[B
    //   84: dup
    //   85: iconst_0
    //   86: aload 6
    //   88: aastore
    //   89: astore 6
    //   91: goto +486 -> 577
    //   94: aconst_null
    //   95: astore 7
    //   97: aconst_null
    //   98: astore 8
    //   100: aload 8
    //   102: astore 6
    //   104: aload 7
    //   106: astore 9
    //   108: aload_1
    //   109: invokevirtual 387	com/android/internal/telephony/InboundSmsTracker:getReferenceNumber	()I
    //   112: invokestatic 393	java/lang/Integer:toString	(I)Ljava/lang/String;
    //   115: astore 10
    //   117: aload 8
    //   119: astore 6
    //   121: aload 7
    //   123: astore 9
    //   125: aload_1
    //   126: invokevirtual 396	com/android/internal/telephony/InboundSmsTracker:getMessageCount	()I
    //   129: invokestatic 393	java/lang/Integer:toString	(I)Ljava/lang/String;
    //   132: astore 11
    //   134: aload 8
    //   136: astore 6
    //   138: aload 7
    //   140: astore 9
    //   142: aload_0
    //   143: getfield 200	com/android/internal/telephony/InboundSmsHandler:mResolver	Landroid/content/ContentResolver;
    //   146: getstatic 150	com/android/internal/telephony/InboundSmsHandler:sRawUri	Landroid/net/Uri;
    //   149: getstatic 130	com/android/internal/telephony/InboundSmsHandler:PDU_SEQUENCE_PORT_PROJECTION	[Ljava/lang/String;
    //   152: aload_1
    //   153: invokevirtual 438	com/android/internal/telephony/InboundSmsTracker:getQueryForSegments	()Ljava/lang/String;
    //   156: iconst_3
    //   157: anewarray 118	java/lang/String
    //   160: dup
    //   161: iconst_0
    //   162: aload 5
    //   164: aastore
    //   165: dup
    //   166: iconst_1
    //   167: aload 10
    //   169: aastore
    //   170: dup
    //   171: iconst_2
    //   172: aload 11
    //   174: aastore
    //   175: aconst_null
    //   176: invokevirtual 639	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   179: astore 8
    //   181: aload 8
    //   183: astore 6
    //   185: aload 8
    //   187: astore 9
    //   189: aload 8
    //   191: invokeinterface 878 1 0
    //   196: istore 12
    //   198: iload 12
    //   200: iload_2
    //   201: if_icmpge +17 -> 218
    //   204: aload 8
    //   206: ifnull +10 -> 216
    //   209: aload 8
    //   211: invokeinterface 676 1 0
    //   216: iconst_0
    //   217: ireturn
    //   218: aload 8
    //   220: astore 6
    //   222: aload 8
    //   224: astore 9
    //   226: iload_2
    //   227: anewarray 875	[B
    //   230: astore 7
    //   232: aload 8
    //   234: astore 6
    //   236: aload 8
    //   238: astore 9
    //   240: aload 8
    //   242: invokeinterface 644 1 0
    //   247: ifeq +314 -> 561
    //   250: aload 8
    //   252: astore 6
    //   254: aload 8
    //   256: astore 9
    //   258: aload 8
    //   260: getstatic 135	com/android/internal/telephony/InboundSmsHandler:PDU_SEQUENCE_PORT_PROJECTION_INDEX_MAPPING	Ljava/util/Map;
    //   263: iconst_1
    //   264: invokestatic 831	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   267: invokeinterface 884 2 0
    //   272: checkcast 389	java/lang/Integer
    //   275: invokevirtual 887	java/lang/Integer:intValue	()I
    //   278: invokeinterface 891 2 0
    //   283: aload_1
    //   284: invokevirtual 894	com/android/internal/telephony/InboundSmsTracker:getIndexOffset	()I
    //   287: isub
    //   288: istore 13
    //   290: aload 8
    //   292: astore 6
    //   294: aload 8
    //   296: astore 9
    //   298: iload 13
    //   300: aload 7
    //   302: arraylength
    //   303: if_icmpge +213 -> 516
    //   306: iload 13
    //   308: ifge +6 -> 314
    //   311: goto +205 -> 516
    //   314: aload 8
    //   316: astore 6
    //   318: aload 8
    //   320: astore 9
    //   322: aload 7
    //   324: iload 13
    //   326: aload 8
    //   328: getstatic 135	com/android/internal/telephony/InboundSmsHandler:PDU_SEQUENCE_PORT_PROJECTION_INDEX_MAPPING	Ljava/util/Map;
    //   331: iconst_0
    //   332: invokestatic 831	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   335: invokeinterface 884 2 0
    //   340: checkcast 389	java/lang/Integer
    //   343: invokevirtual 887	java/lang/Integer:intValue	()I
    //   346: invokeinterface 653 2 0
    //   351: invokestatic 663	com/android/internal/util/HexDump:hexStringToByteArray	(Ljava/lang/String;)[B
    //   354: aastore
    //   355: iload_3
    //   356: istore 12
    //   358: iload 13
    //   360: ifne +93 -> 453
    //   363: iload_3
    //   364: istore 12
    //   366: aload 8
    //   368: astore 6
    //   370: aload 8
    //   372: astore 9
    //   374: aload 8
    //   376: getstatic 135	com/android/internal/telephony/InboundSmsHandler:PDU_SEQUENCE_PORT_PROJECTION_INDEX_MAPPING	Ljava/util/Map;
    //   379: iconst_2
    //   380: invokestatic 831	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   383: invokeinterface 884 2 0
    //   388: checkcast 389	java/lang/Integer
    //   391: invokevirtual 887	java/lang/Integer:intValue	()I
    //   394: invokeinterface 897 2 0
    //   399: ifne +54 -> 453
    //   402: aload 8
    //   404: astore 6
    //   406: aload 8
    //   408: astore 9
    //   410: aload 8
    //   412: getstatic 135	com/android/internal/telephony/InboundSmsHandler:PDU_SEQUENCE_PORT_PROJECTION_INDEX_MAPPING	Ljava/util/Map;
    //   415: iconst_2
    //   416: invokestatic 831	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   419: invokeinterface 884 2 0
    //   424: checkcast 389	java/lang/Integer
    //   427: invokevirtual 887	java/lang/Integer:intValue	()I
    //   430: invokeinterface 891 2 0
    //   435: invokestatic 900	com/android/internal/telephony/InboundSmsTracker:getRealDestPort	(I)I
    //   438: istore 13
    //   440: iload_3
    //   441: istore 12
    //   443: iload 13
    //   445: iconst_m1
    //   446: if_icmpeq +7 -> 453
    //   449: iload 13
    //   451: istore 12
    //   453: iload 4
    //   455: istore 14
    //   457: iload 4
    //   459: ifne +47 -> 506
    //   462: aload 8
    //   464: astore 6
    //   466: aload 8
    //   468: astore 9
    //   470: aload_0
    //   471: getfield 186	com/android/internal/telephony/InboundSmsHandler:mContext	Landroid/content/Context;
    //   474: aload 8
    //   476: getstatic 135	com/android/internal/telephony/InboundSmsHandler:PDU_SEQUENCE_PORT_PROJECTION_INDEX_MAPPING	Ljava/util/Map;
    //   479: bipush 9
    //   481: invokestatic 831	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   484: invokeinterface 884 2 0
    //   489: checkcast 389	java/lang/Integer
    //   492: invokevirtual 887	java/lang/Integer:intValue	()I
    //   495: invokeinterface 653 2 0
    //   500: aconst_null
    //   501: invokestatic 873	com/android/internal/telephony/BlockChecker:isBlocked	(Landroid/content/Context;Ljava/lang/String;Landroid/os/Bundle;)Z
    //   504: istore 14
    //   506: iload 12
    //   508: istore_3
    //   509: iload 14
    //   511: istore 4
    //   513: goto +45 -> 558
    //   516: aload 8
    //   518: astore 6
    //   520: aload 8
    //   522: astore 9
    //   524: aload_0
    //   525: ldc_w 902
    //   528: iconst_2
    //   529: anewarray 904	java/lang/Object
    //   532: dup
    //   533: iconst_0
    //   534: aload_1
    //   535: invokevirtual 894	com/android/internal/telephony/InboundSmsTracker:getIndexOffset	()I
    //   538: iload 13
    //   540: iadd
    //   541: invokestatic 831	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   544: aastore
    //   545: dup
    //   546: iconst_1
    //   547: iload_2
    //   548: invokestatic 831	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   551: aastore
    //   552: invokestatic 907	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   555: invokevirtual 468	com/android/internal/telephony/InboundSmsHandler:loge	(Ljava/lang/String;)V
    //   558: goto -326 -> 232
    //   561: aload 8
    //   563: ifnull +10 -> 573
    //   566: aload 8
    //   568: invokeinterface 676 1 0
    //   573: aload 7
    //   575: astore 6
    //   577: aload 6
    //   579: invokestatic 911	java/util/Arrays:asList	([Ljava/lang/Object;)Ljava/util/List;
    //   582: astore 9
    //   584: aload 9
    //   586: invokeinterface 916 1 0
    //   591: ifeq +269 -> 860
    //   594: aload 9
    //   596: aconst_null
    //   597: invokeinterface 920 2 0
    //   602: ifeq +6 -> 608
    //   605: goto +255 -> 860
    //   608: new 23	com/android/internal/telephony/InboundSmsHandler$SmsBroadcastReceiver
    //   611: dup
    //   612: aload_0
    //   613: aload_1
    //   614: invokespecial 923	com/android/internal/telephony/InboundSmsHandler$SmsBroadcastReceiver:<init>	(Lcom/android/internal/telephony/InboundSmsHandler;Lcom/android/internal/telephony/InboundSmsTracker;)V
    //   617: astore 10
    //   619: aload_0
    //   620: getfield 261	com/android/internal/telephony/InboundSmsHandler:mUserManager	Landroid/os/UserManager;
    //   623: invokevirtual 926	android/os/UserManager:isUserUnlocked	()Z
    //   626: ifne +14 -> 640
    //   629: aload_0
    //   630: aload_1
    //   631: aload 6
    //   633: iload_3
    //   634: aload 10
    //   636: invokespecial 930	com/android/internal/telephony/InboundSmsHandler:processMessagePartWithUserLocked	(Lcom/android/internal/telephony/InboundSmsTracker;[[BILcom/android/internal/telephony/InboundSmsHandler$SmsBroadcastReceiver;)Z
    //   639: ireturn
    //   640: iload_3
    //   641: sipush 2948
    //   644: if_icmpne +167 -> 811
    //   647: new 932	java/io/ByteArrayOutputStream
    //   650: dup
    //   651: invokespecial 933	java/io/ByteArrayOutputStream:<init>	()V
    //   654: astore 7
    //   656: aload 6
    //   658: arraylength
    //   659: istore 12
    //   661: iconst_0
    //   662: istore_3
    //   663: iload_3
    //   664: iload 12
    //   666: if_icmpge +71 -> 737
    //   669: aload 6
    //   671: iload_3
    //   672: aaload
    //   673: astore 8
    //   675: aload 8
    //   677: astore 9
    //   679: aload_1
    //   680: invokevirtual 936	com/android/internal/telephony/InboundSmsTracker:is3gpp2	()Z
    //   683: ifne +37 -> 720
    //   686: aload 8
    //   688: ldc_w 938
    //   691: invokestatic 942	android/telephony/SmsMessage:createFromPdu	([BLjava/lang/String;)Landroid/telephony/SmsMessage;
    //   694: astore 9
    //   696: aload 9
    //   698: ifnull +13 -> 711
    //   701: aload 9
    //   703: invokevirtual 945	android/telephony/SmsMessage:getUserData	()[B
    //   706: astore 9
    //   708: goto +12 -> 720
    //   711: aload_0
    //   712: ldc_w 947
    //   715: invokevirtual 468	com/android/internal/telephony/InboundSmsHandler:loge	(Ljava/lang/String;)V
    //   718: iconst_0
    //   719: ireturn
    //   720: aload 7
    //   722: aload 9
    //   724: iconst_0
    //   725: aload 9
    //   727: arraylength
    //   728: invokevirtual 951	java/io/ByteArrayOutputStream:write	([BII)V
    //   731: iinc 3 1
    //   734: goto -71 -> 663
    //   737: aload_0
    //   738: getfield 207	com/android/internal/telephony/InboundSmsHandler:mWapPush	Lcom/android/internal/telephony/WapPushOverSms;
    //   741: aload 7
    //   743: invokevirtual 954	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   746: aload 10
    //   748: aload_0
    //   749: aload 5
    //   751: invokevirtual 958	com/android/internal/telephony/WapPushOverSms:dispatchWapPdu	([BLandroid/content/BroadcastReceiver;Lcom/android/internal/telephony/InboundSmsHandler;Ljava/lang/String;)I
    //   754: istore_3
    //   755: new 408	java/lang/StringBuilder
    //   758: dup
    //   759: invokespecial 409	java/lang/StringBuilder:<init>	()V
    //   762: astore 6
    //   764: aload 6
    //   766: ldc_w 960
    //   769: invokevirtual 415	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   772: pop
    //   773: aload 6
    //   775: iload_3
    //   776: invokevirtual 473	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   779: pop
    //   780: aload_0
    //   781: aload 6
    //   783: invokevirtual 420	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   786: invokevirtual 288	com/android/internal/telephony/InboundSmsHandler:log	(Ljava/lang/String;)V
    //   789: iload_3
    //   790: iconst_m1
    //   791: if_icmpne +5 -> 796
    //   794: iconst_1
    //   795: ireturn
    //   796: aload_0
    //   797: aload_1
    //   798: invokevirtual 963	com/android/internal/telephony/InboundSmsTracker:getDeleteWhere	()Ljava/lang/String;
    //   801: aload_1
    //   802: invokevirtual 967	com/android/internal/telephony/InboundSmsTracker:getDeleteWhereArgs	()[Ljava/lang/String;
    //   805: iconst_2
    //   806: invokespecial 314	com/android/internal/telephony/InboundSmsHandler:deleteFromRawTable	(Ljava/lang/String;[Ljava/lang/String;I)V
    //   809: iconst_0
    //   810: ireturn
    //   811: iload 4
    //   813: ifeq +18 -> 831
    //   816: aload_0
    //   817: aload_1
    //   818: invokevirtual 963	com/android/internal/telephony/InboundSmsTracker:getDeleteWhere	()Ljava/lang/String;
    //   821: aload_1
    //   822: invokevirtual 967	com/android/internal/telephony/InboundSmsTracker:getDeleteWhereArgs	()[Ljava/lang/String;
    //   825: iconst_1
    //   826: invokespecial 314	com/android/internal/telephony/InboundSmsHandler:deleteFromRawTable	(Ljava/lang/String;[Ljava/lang/String;I)V
    //   829: iconst_0
    //   830: ireturn
    //   831: aload_0
    //   832: aload 6
    //   834: iload_3
    //   835: aload_1
    //   836: aload 10
    //   838: iconst_1
    //   839: invokespecial 969	com/android/internal/telephony/InboundSmsHandler:filterSms	([[BILcom/android/internal/telephony/InboundSmsTracker;Lcom/android/internal/telephony/InboundSmsHandler$SmsBroadcastReceiver;Z)Z
    //   842: ifne +16 -> 858
    //   845: aload_0
    //   846: aload 6
    //   848: aload_1
    //   849: invokevirtual 682	com/android/internal/telephony/InboundSmsTracker:getFormat	()Ljava/lang/String;
    //   852: iload_3
    //   853: aload 10
    //   855: invokespecial 331	com/android/internal/telephony/InboundSmsHandler:dispatchSmsDeliveryIntent	([[BLjava/lang/String;ILcom/android/internal/telephony/InboundSmsHandler$SmsBroadcastReceiver;)V
    //   858: iconst_1
    //   859: ireturn
    //   860: new 408	java/lang/StringBuilder
    //   863: dup
    //   864: invokespecial 409	java/lang/StringBuilder:<init>	()V
    //   867: astore 6
    //   869: aload 6
    //   871: ldc_w 971
    //   874: invokevirtual 415	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   877: pop
    //   878: aload 9
    //   880: invokeinterface 916 1 0
    //   885: ifne +10 -> 895
    //   888: ldc_w 973
    //   891: astore_1
    //   892: goto +7 -> 899
    //   895: ldc_w 975
    //   898: astore_1
    //   899: aload 6
    //   901: aload_1
    //   902: invokevirtual 415	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   905: pop
    //   906: aload_0
    //   907: aload 6
    //   909: invokevirtual 420	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   912: invokevirtual 468	com/android/internal/telephony/InboundSmsHandler:loge	(Ljava/lang/String;)V
    //   915: iconst_0
    //   916: ireturn
    //   917: astore_1
    //   918: goto +30 -> 948
    //   921: astore_1
    //   922: aload 9
    //   924: astore 6
    //   926: aload_0
    //   927: ldc_w 977
    //   930: aload_1
    //   931: invokevirtual 374	com/android/internal/telephony/InboundSmsHandler:loge	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   934: aload 9
    //   936: ifnull +10 -> 946
    //   939: aload 9
    //   941: invokeinterface 676 1 0
    //   946: iconst_0
    //   947: ireturn
    //   948: aload 6
    //   950: ifnull +10 -> 960
    //   953: aload 6
    //   955: invokeinterface 676 1 0
    //   960: aload_1
    //   961: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	962	0	this	InboundSmsHandler
    //   0	962	1	paramInboundSmsTracker	InboundSmsTracker
    //   4	544	2	i	int
    //   9	844	3	j	int
    //   11	801	4	bool1	boolean
    //   17	733	5	str1	String
    //   64	890	6	localObject1	Object
    //   95	647	7	localObject2	Object
    //   98	589	8	localCursor	Cursor
    //   106	834	9	localObject3	Object
    //   115	739	10	localObject4	Object
    //   132	41	11	str2	String
    //   196	471	12	k	int
    //   288	253	13	m	int
    //   455	55	14	bool2	boolean
    // Exception table:
    //   from	to	target	type
    //   108	117	917	finally
    //   125	134	917	finally
    //   142	181	917	finally
    //   189	198	917	finally
    //   226	232	917	finally
    //   240	250	917	finally
    //   258	290	917	finally
    //   298	306	917	finally
    //   322	355	917	finally
    //   374	402	917	finally
    //   410	440	917	finally
    //   470	506	917	finally
    //   524	558	917	finally
    //   926	934	917	finally
    //   108	117	921	android/database/SQLException
    //   125	134	921	android/database/SQLException
    //   142	181	921	android/database/SQLException
    //   189	198	921	android/database/SQLException
    //   226	232	921	android/database/SQLException
    //   240	250	921	android/database/SQLException
    //   258	290	921	android/database/SQLException
    //   298	306	921	android/database/SQLException
    //   322	355	921	android/database/SQLException
    //   374	402	921	android/database/SQLException
    //   410	440	921	android/database/SQLException
    //   470	506	921	android/database/SQLException
    //   524	558	921	android/database/SQLException
  }
  
  private boolean processMessagePartWithUserLocked(InboundSmsTracker paramInboundSmsTracker, byte[][] paramArrayOfByte, int paramInt, SmsBroadcastReceiver paramSmsBroadcastReceiver)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Credential-encrypted storage not available. Port: ");
    localStringBuilder.append(paramInt);
    log(localStringBuilder.toString());
    if ((paramInt == 2948) && (mWapPush.isWapPushForMms(paramArrayOfByte[0], this)))
    {
      showNewMessageNotification();
      return false;
    }
    if (paramInt == -1)
    {
      if (filterSms(paramArrayOfByte, paramInt, paramInboundSmsTracker, paramSmsBroadcastReceiver, false)) {
        return true;
      }
      showNewMessageNotification();
      return false;
    }
    return false;
  }
  
  static void registerNewMessageNotificationActionHandler(Context paramContext)
  {
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction(ACTION_OPEN_SMS_APP);
    paramContext.registerReceiver(new NewMessageNotificationActionReceiver(null), localIntentFilter);
  }
  
  private static String replaceFormFeeds(String paramString)
  {
    if (paramString == null) {
      paramString = "";
    } else {
      paramString = paramString.replace('\f', '\n');
    }
    return paramString;
  }
  
  private void setWakeLockTimeout(int paramInt)
  {
    mWakeLockTimeout = paramInt;
  }
  
  private void showNewMessageNotification()
  {
    if (!StorageManager.isFileEncryptedNativeOrEmulated()) {
      return;
    }
    log("Show new message notification.");
    Object localObject = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_OPEN_SMS_APP), 1073741824);
    localObject = new Notification.Builder(mContext).setSmallIcon(17301646).setAutoCancel(true).setVisibility(1).setDefaults(-1).setContentTitle(mContext.getString(17040471)).setContentText(mContext.getString(17040470)).setContentIntent((PendingIntent)localObject).setChannelId("sms");
    ((NotificationManager)mContext.getSystemService("notification")).notify("InboundSmsHandler", 1, ((Notification.Builder)localObject).build());
  }
  
  /* Error */
  private Uri writeInboxMessage(Intent paramIntent)
  {
    // Byte code:
    //   0: aload_1
    //   1: invokestatic 1073	android/provider/Telephony$Sms$Intents:getMessagesFromIntent	(Landroid/content/Intent;)[Landroid/telephony/SmsMessage;
    //   4: astore_2
    //   5: aload_2
    //   6: ifnull +109 -> 115
    //   9: aload_2
    //   10: arraylength
    //   11: iconst_1
    //   12: if_icmpge +6 -> 18
    //   15: goto +100 -> 115
    //   18: aload_2
    //   19: arraylength
    //   20: istore_3
    //   21: iconst_0
    //   22: istore 4
    //   24: iload 4
    //   26: iload_3
    //   27: if_icmpge +29 -> 56
    //   30: aload_2
    //   31: iload 4
    //   33: aaload
    //   34: astore_1
    //   35: aload_1
    //   36: invokevirtual 447	android/telephony/SmsMessage:getDisplayMessageBody	()Ljava/lang/String;
    //   39: pop
    //   40: iinc 4 1
    //   43: goto -19 -> 24
    //   46: astore_1
    //   47: aload_0
    //   48: ldc_w 1075
    //   51: invokevirtual 468	com/android/internal/telephony/InboundSmsHandler:loge	(Ljava/lang/String;)V
    //   54: aconst_null
    //   55: areturn
    //   56: aload_2
    //   57: invokestatic 1077	com/android/internal/telephony/InboundSmsHandler:parseSmsMessage	([Landroid/telephony/SmsMessage;)Landroid/content/ContentValues;
    //   60: astore_1
    //   61: invokestatic 1082	android/os/Binder:clearCallingIdentity	()J
    //   64: lstore 5
    //   66: aload_0
    //   67: getfield 186	com/android/internal/telephony/InboundSmsHandler:mContext	Landroid/content/Context;
    //   70: invokevirtual 198	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   73: getstatic 1085	android/provider/Telephony$Sms$Inbox:CONTENT_URI	Landroid/net/Uri;
    //   76: aload_1
    //   77: invokevirtual 406	android/content/ContentResolver:insert	(Landroid/net/Uri;Landroid/content/ContentValues;)Landroid/net/Uri;
    //   80: astore_1
    //   81: lload 5
    //   83: invokestatic 1088	android/os/Binder:restoreCallingIdentity	(J)V
    //   86: aload_1
    //   87: areturn
    //   88: astore_1
    //   89: goto +19 -> 108
    //   92: astore_1
    //   93: aload_0
    //   94: ldc_w 1090
    //   97: aload_1
    //   98: invokevirtual 374	com/android/internal/telephony/InboundSmsHandler:loge	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   101: lload 5
    //   103: invokestatic 1088	android/os/Binder:restoreCallingIdentity	(J)V
    //   106: aconst_null
    //   107: areturn
    //   108: lload 5
    //   110: invokestatic 1088	android/os/Binder:restoreCallingIdentity	(J)V
    //   113: aload_1
    //   114: athrow
    //   115: aload_0
    //   116: ldc_w 1092
    //   119: invokevirtual 468	com/android/internal/telephony/InboundSmsHandler:loge	(Ljava/lang/String;)V
    //   122: aconst_null
    //   123: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	124	0	this	InboundSmsHandler
    //   0	124	1	paramIntent	Intent
    //   4	53	2	arrayOfSmsMessage	SmsMessage[]
    //   20	8	3	i	int
    //   22	19	4	j	int
    //   64	45	5	l	long
    // Exception table:
    //   from	to	target	type
    //   35	40	46	java/lang/NullPointerException
    //   66	81	88	finally
    //   93	101	88	finally
    //   66	81	92	java/lang/Exception
  }
  
  protected abstract void acknowledgeLastIncomingSms(boolean paramBoolean, int paramInt, Message paramMessage);
  
  protected int addTrackerToRawTableAndSendMessage(InboundSmsTracker paramInboundSmsTracker, boolean paramBoolean)
  {
    int i = addTrackerToRawTable(paramInboundSmsTracker, paramBoolean);
    if (i != 1)
    {
      if (i != 5) {
        return 2;
      }
      return 1;
    }
    sendMessage(2, paramInboundSmsTracker);
    return 1;
  }
  
  public void dispatchIntent(Intent paramIntent, String paramString, int paramInt, Bundle paramBundle, BroadcastReceiver paramBroadcastReceiver, UserHandle paramUserHandle)
  {
    paramIntent.addFlags(134217728);
    Object localObject1 = paramIntent.getAction();
    if (("android.provider.Telephony.SMS_DELIVER".equals(localObject1)) || ("android.provider.Telephony.SMS_RECEIVED".equals(localObject1)) || ("android.provider.Telephony.WAP_PUSH_DELIVER".equals(localObject1)) || ("android.provider.Telephony.WAP_PUSH_RECEIVED".equals(localObject1))) {
      paramIntent.addFlags(268435456);
    }
    SubscriptionManager.putPhoneIdAndSubIdExtra(paramIntent, mPhone.getPhoneId());
    if (paramUserHandle.equals(UserHandle.ALL))
    {
      localObject1 = null;
      try
      {
        int[] arrayOfInt = ActivityManager.getService().getRunningUserIds();
        localObject1 = arrayOfInt;
      }
      catch (RemoteException localRemoteException) {}
      Object localObject2 = localObject1;
      if (localObject1 == null) {
        localObject2 = new int[] { paramUserHandle.getIdentifier() };
      }
      paramUserHandle = (UserHandle)localObject2;
      for (int i = paramUserHandle.length - 1; i >= 0; i--)
      {
        localObject2 = new UserHandle(paramUserHandle[i]);
        if (paramUserHandle[i] != 0)
        {
          if (!mUserManager.hasUserRestriction("no_sms", (UserHandle)localObject2)) {
            do
            {
              localObject1 = mUserManager.getUserInfo(paramUserHandle[i]);
            } while ((localObject1 == null) || (((UserInfo)localObject1).isManagedProfile()));
          }
        }
        else
        {
          Context localContext = mContext;
          if (paramUserHandle[i] == 0) {
            localObject1 = paramBroadcastReceiver;
          } else {
            localObject1 = null;
          }
          localContext.sendOrderedBroadcastAsUser(paramIntent, (UserHandle)localObject2, paramString, paramInt, paramBundle, (BroadcastReceiver)localObject1, getHandler(), -1, null, null);
        }
      }
    }
    else
    {
      mContext.sendOrderedBroadcastAsUser(paramIntent, paramUserHandle, paramString, paramInt, paramBundle, paramBroadcastReceiver, getHandler(), -1, null, null);
    }
  }
  
  protected abstract int dispatchMessageRadioSpecific(SmsMessageBase paramSmsMessageBase);
  
  protected int dispatchNormalMessage(SmsMessageBase paramSmsMessageBase)
  {
    Object localObject1 = paramSmsMessageBase.getUserDataHeader();
    Object localObject2;
    int i;
    if ((localObject1 != null) && (concatRef != null))
    {
      localObject2 = concatRef;
      localObject1 = portAddrs;
      if (localObject1 != null) {
        i = destPort;
      } else {
        i = -1;
      }
      paramSmsMessageBase = TelephonyComponentFactory.getInstance().makeInboundSmsTracker(paramSmsMessageBase.getPdu(), paramSmsMessageBase.getTimestampMillis(), i, is3gpp2(), paramSmsMessageBase.getOriginatingAddress(), paramSmsMessageBase.getDisplayOriginatingAddress(), refNumber, seqNumber, msgCount, false, paramSmsMessageBase.getMessageBody());
    }
    else
    {
      int j = -1;
      i = j;
      if (localObject1 != null)
      {
        i = j;
        if (portAddrs != null)
        {
          i = portAddrs.destPort;
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("destination port: ");
          ((StringBuilder)localObject2).append(i);
          log(((StringBuilder)localObject2).toString());
        }
      }
      paramSmsMessageBase = TelephonyComponentFactory.getInstance().makeInboundSmsTracker(paramSmsMessageBase.getPdu(), paramSmsMessageBase.getTimestampMillis(), i, is3gpp2(), false, paramSmsMessageBase.getOriginatingAddress(), paramSmsMessageBase.getDisplayOriginatingAddress(), paramSmsMessageBase.getMessageBody());
    }
    boolean bool;
    if (paramSmsMessageBase.getDestPort() == -1) {
      bool = true;
    } else {
      bool = false;
    }
    return addTrackerToRawTableAndSendMessage(paramSmsMessageBase, bool);
  }
  
  public void dispose()
  {
    quit();
  }
  
  public Phone getPhone()
  {
    return mPhone;
  }
  
  @VisibleForTesting
  public PowerManager.WakeLock getWakeLock()
  {
    return mWakeLock;
  }
  
  @VisibleForTesting
  public int getWakeLockTimeout()
  {
    return mWakeLockTimeout;
  }
  
  protected abstract boolean is3gpp2();
  
  protected void log(String paramString)
  {
    Rlog.d(getName(), paramString);
  }
  
  protected void loge(String paramString)
  {
    Rlog.e(getName(), paramString);
  }
  
  protected void loge(String paramString, Throwable paramThrowable)
  {
    Rlog.e(getName(), paramString, paramThrowable);
  }
  
  protected void onQuitting()
  {
    mWapPush.dispose();
    while (mWakeLock.isHeld()) {
      mWakeLock.release();
    }
  }
  
  protected void onUpdatePhoneObject(Phone paramPhone)
  {
    mPhone = paramPhone;
    mStorageMonitor = mPhone.mSmsStorageMonitor;
    paramPhone = new StringBuilder();
    paramPhone.append("onUpdatePhoneObject: phone=");
    paramPhone.append(mPhone.getClass().getSimpleName());
    log(paramPhone.toString());
  }
  
  public void updatePhoneObject(Phone paramPhone)
  {
    sendMessage(7, paramPhone);
  }
  
  private final class CarrierServicesSmsFilterCallback
    implements CarrierServicesSmsFilter.CarrierServicesSmsFilterCallbackInterface
  {
    private final int mDestPort;
    private final byte[][] mPdus;
    private final InboundSmsHandler.SmsBroadcastReceiver mSmsBroadcastReceiver;
    private final String mSmsFormat;
    private final boolean mUserUnlocked;
    
    CarrierServicesSmsFilterCallback(byte[][] paramArrayOfByte, int paramInt, String paramString, InboundSmsHandler.SmsBroadcastReceiver paramSmsBroadcastReceiver, boolean paramBoolean)
    {
      mPdus = paramArrayOfByte;
      mDestPort = paramInt;
      mSmsFormat = paramString;
      mSmsBroadcastReceiver = paramSmsBroadcastReceiver;
      mUserUnlocked = paramBoolean;
    }
    
    public void onFilterComplete(int paramInt)
    {
      InboundSmsHandler localInboundSmsHandler = InboundSmsHandler.this;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onFilterComplete: result is ");
      localStringBuilder.append(paramInt);
      localInboundSmsHandler.logv(localStringBuilder.toString());
      if ((paramInt & 0x1) == 0)
      {
        if (VisualVoicemailSmsFilter.filter(mContext, mPdus, mSmsFormat, mDestPort, mPhone.getSubId()))
        {
          log("Visual voicemail SMS dropped");
          InboundSmsHandler.this.dropSms(mSmsBroadcastReceiver);
          return;
        }
        if (mUserUnlocked)
        {
          InboundSmsHandler.this.dispatchSmsDeliveryIntent(mPdus, mSmsFormat, mDestPort, mSmsBroadcastReceiver);
        }
        else
        {
          if (!InboundSmsHandler.this.isSkipNotifyFlagSet(paramInt)) {
            InboundSmsHandler.this.showNewMessageNotification();
          }
          sendMessage(3);
        }
      }
      else
      {
        InboundSmsHandler.this.dropSms(mSmsBroadcastReceiver);
      }
    }
  }
  
  private class DefaultState
    extends State
  {
    private DefaultState() {}
    
    public boolean processMessage(Message paramMessage)
    {
      if (what != 7)
      {
        Object localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("processMessage: unhandled message type ");
        ((StringBuilder)localObject1).append(what);
        ((StringBuilder)localObject1).append(" currState=");
        ((StringBuilder)localObject1).append(getCurrentState().getName());
        paramMessage = ((StringBuilder)localObject1).toString();
        if (Build.IS_DEBUGGABLE)
        {
          loge("---- Dumping InboundSmsHandler ----");
          Object localObject2 = InboundSmsHandler.this;
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Total records=");
          ((StringBuilder)localObject1).append(getLogRecCount());
          ((InboundSmsHandler)localObject2).loge(((StringBuilder)localObject1).toString());
          for (int i = Math.max(getLogRecSize() - 20, 0); i < getLogRecSize(); i++)
          {
            localObject1 = InboundSmsHandler.this;
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("Rec[%d]: %s\n");
            ((StringBuilder)localObject2).append(i);
            ((StringBuilder)localObject2).append(getLogRec(i).toString());
            ((InboundSmsHandler)localObject1).loge(((StringBuilder)localObject2).toString());
          }
          loge("---- Dumped InboundSmsHandler ----");
          throw new RuntimeException(paramMessage);
        }
        loge(paramMessage);
      }
      else
      {
        onUpdatePhoneObject((Phone)obj);
      }
      return true;
    }
  }
  
  private class DeliveringState
    extends State
  {
    private DeliveringState() {}
    
    public void enter()
    {
      log("entering Delivering state");
    }
    
    public void exit()
    {
      log("leaving Delivering state");
    }
    
    public boolean processMessage(Message paramMessage)
    {
      InboundSmsHandler localInboundSmsHandler = InboundSmsHandler.this;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("DeliveringState.processMessage:");
      localStringBuilder.append(what);
      localInboundSmsHandler.log(localStringBuilder.toString());
      switch (what)
      {
      case 3: 
      case 6: 
      case 7: 
      default: 
        return false;
      case 8: 
        InboundSmsHandler.this.handleInjectSms((AsyncResult)obj);
        sendMessage(4);
        return true;
      case 5: 
        mWakeLock.release();
        if (!mWakeLock.isHeld()) {
          loge("mWakeLock released while delivering/broadcasting!");
        }
        return true;
      case 4: 
        transitionTo(mIdleState);
        return true;
      case 2: 
        paramMessage = (InboundSmsTracker)obj;
        if (InboundSmsHandler.this.processMessagePart(paramMessage))
        {
          transitionTo(mWaitingState);
        }
        else
        {
          log("No broadcast sent on processing EVENT_BROADCAST_SMS in Delivering state. Return to Idle state");
          sendMessage(4);
        }
        return true;
      }
      InboundSmsHandler.this.handleNewSms((AsyncResult)obj);
      sendMessage(4);
      return true;
    }
  }
  
  private class IdleState
    extends State
  {
    private IdleState() {}
    
    public void enter()
    {
      log("entering Idle state");
      sendMessageDelayed(5, getWakeLockTimeout());
    }
    
    public void exit()
    {
      mWakeLock.acquire();
      log("acquired wakelock, leaving Idle state");
    }
    
    public boolean processMessage(Message paramMessage)
    {
      Object localObject1 = InboundSmsHandler.this;
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("IdleState.processMessage:");
      ((StringBuilder)localObject2).append(what);
      ((InboundSmsHandler)localObject1).log(((StringBuilder)localObject2).toString());
      localObject2 = InboundSmsHandler.this;
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Idle state processing message type ");
      ((StringBuilder)localObject1).append(what);
      ((InboundSmsHandler)localObject2).log(((StringBuilder)localObject1).toString());
      switch (what)
      {
      case 3: 
      case 6: 
      case 7: 
      default: 
        return false;
      case 5: 
        mWakeLock.release();
        if (mWakeLock.isHeld()) {
          log("mWakeLock is still held after release");
        } else {
          log("mWakeLock released");
        }
        return true;
      case 4: 
        return true;
      }
      deferMessage(paramMessage);
      transitionTo(mDeliveringState);
      return true;
    }
  }
  
  private static class NewMessageNotificationActionReceiver
    extends BroadcastReceiver
  {
    private NewMessageNotificationActionReceiver() {}
    
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (InboundSmsHandler.ACTION_OPEN_SMS_APP.equals(paramIntent.getAction())) {
        paramContext.startActivity(paramContext.getPackageManager().getLaunchIntentForPackage(Telephony.Sms.getDefaultSmsPackage(paramContext)));
      }
    }
  }
  
  private final class SmsBroadcastReceiver
    extends BroadcastReceiver
  {
    private long mBroadcastTimeNano;
    private final String mDeleteWhere;
    private final String[] mDeleteWhereArgs;
    
    SmsBroadcastReceiver(InboundSmsTracker paramInboundSmsTracker)
    {
      mDeleteWhere = paramInboundSmsTracker.getDeleteWhere();
      mDeleteWhereArgs = paramInboundSmsTracker.getDeleteWhereArgs();
      mBroadcastTimeNano = System.nanoTime();
    }
    
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      paramContext = paramIntent.getAction();
      if (paramContext.equals("android.provider.Telephony.SMS_DELIVER"))
      {
        paramIntent.setAction("android.provider.Telephony.SMS_RECEIVED");
        paramIntent.addFlags(16777216);
        paramIntent.setComponent(null);
        paramContext = InboundSmsHandler.this.handleSmsWhitelisting(null);
        dispatchIntent(paramIntent, "android.permission.RECEIVE_SMS", 16, paramContext, this, UserHandle.ALL);
      }
      else
      {
        Object localObject2;
        if (paramContext.equals("android.provider.Telephony.WAP_PUSH_DELIVER"))
        {
          paramIntent.setAction("android.provider.Telephony.WAP_PUSH_RECEIVED");
          paramIntent.setComponent(null);
          paramIntent.addFlags(16777216);
          paramContext = null;
          try
          {
            long l = mDeviceIdleController.addPowerSaveTempWhitelistAppForMms(mContext.getPackageName(), 0, "mms-broadcast");
            Object localObject1 = BroadcastOptions.makeBasic();
            ((BroadcastOptions)localObject1).setTemporaryAppWhitelistDuration(l);
            localObject1 = ((BroadcastOptions)localObject1).toBundle();
            paramContext = (Context)localObject1;
          }
          catch (RemoteException localRemoteException) {}
          localObject2 = paramIntent.getType();
          dispatchIntent(paramIntent, WapPushOverSms.getPermissionForType((String)localObject2), WapPushOverSms.getAppOpsPermissionForIntent((String)localObject2), paramContext, this, UserHandle.SYSTEM);
        }
        else
        {
          if ((!"android.intent.action.DATA_SMS_RECEIVED".equals(paramContext)) && (!"android.provider.Telephony.SMS_RECEIVED".equals(paramContext)) && (!"android.intent.action.DATA_SMS_RECEIVED".equals(paramContext)) && (!"android.provider.Telephony.WAP_PUSH_RECEIVED".equals(paramContext)))
          {
            paramIntent = InboundSmsHandler.this;
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("unexpected BroadcastReceiver action: ");
            ((StringBuilder)localObject2).append(paramContext);
            paramIntent.loge(((StringBuilder)localObject2).toString());
          }
          int i = getResultCode();
          if ((i != -1) && (i != 1))
          {
            paramContext = InboundSmsHandler.this;
            paramIntent = new StringBuilder();
            paramIntent.append("a broadcast receiver set the result code to ");
            paramIntent.append(i);
            paramIntent.append(", deleting from raw table anyway!");
            paramContext.loge(paramIntent.toString());
          }
          else
          {
            log("successful broadcast, deleting from raw table.");
          }
          InboundSmsHandler.this.deleteFromRawTable(mDeleteWhere, mDeleteWhereArgs, 2);
          sendMessage(3);
          i = (int)((System.nanoTime() - mBroadcastTimeNano) / 1000000L);
          if (i >= 5000)
          {
            paramIntent = InboundSmsHandler.this;
            paramContext = new StringBuilder();
            paramContext.append("Slow ordered broadcast completion time: ");
            paramContext.append(i);
            paramContext.append(" ms");
            paramIntent.loge(paramContext.toString());
          }
          else
          {
            paramIntent = InboundSmsHandler.this;
            paramContext = new StringBuilder();
            paramContext.append("ordered broadcast completed in: ");
            paramContext.append(i);
            paramContext.append(" ms");
            paramIntent.log(paramContext.toString());
          }
        }
      }
    }
  }
  
  private class StartupState
    extends State
  {
    private StartupState() {}
    
    public void enter()
    {
      log("entering Startup state");
      InboundSmsHandler.this.setWakeLockTimeout(0);
    }
    
    public boolean processMessage(Message paramMessage)
    {
      InboundSmsHandler localInboundSmsHandler = InboundSmsHandler.this;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("StartupState.processMessage:");
      localStringBuilder.append(what);
      localInboundSmsHandler.log(localStringBuilder.toString());
      int i = what;
      if (i != 6)
      {
        if (i != 8) {
          switch (i)
          {
          default: 
            return false;
          }
        }
        deferMessage(paramMessage);
        return true;
      }
      transitionTo(mIdleState);
      return true;
    }
  }
  
  private class WaitingState
    extends State
  {
    private WaitingState() {}
    
    public void enter()
    {
      log("entering Waiting state");
    }
    
    public void exit()
    {
      log("exiting Waiting state");
      InboundSmsHandler.this.setWakeLockTimeout(3000);
    }
    
    public boolean processMessage(Message paramMessage)
    {
      InboundSmsHandler localInboundSmsHandler = InboundSmsHandler.this;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("WaitingState.processMessage:");
      localStringBuilder.append(what);
      localInboundSmsHandler.log(localStringBuilder.toString());
      switch (what)
      {
      default: 
        return false;
      case 4: 
        return true;
      case 3: 
        sendMessage(4);
        transitionTo(mDeliveringState);
        return true;
      }
      deferMessage(paramMessage);
      return true;
    }
  }
}
