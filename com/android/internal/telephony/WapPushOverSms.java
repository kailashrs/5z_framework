package com.android.internal.telephony;

import android.app.BroadcastOptions;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.database.sqlite.SqliteWrapper;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IDeviceIdleController;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Telephony.Mms.Inbox;
import android.telephony.Rlog;
import android.telephony.SmsManager;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.mms.MmsException;
import com.google.android.mms.pdu.EncodedStringValue;
import com.google.android.mms.pdu.GenericPdu;
import com.google.android.mms.pdu.NotificationInd;
import com.google.android.mms.pdu.PduParser;
import com.google.android.mms.pdu.PduPersister;
import java.util.HashMap;

public class WapPushOverSms
  implements ServiceConnection
{
  private static final boolean DBG = false;
  private static final String LOCATION_SELECTION = "m_type=? AND ct_l =?";
  private static final String TAG = "WAP PUSH";
  private static final String THREAD_ID_SELECTION = "m_id=? AND m_type=?";
  private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      paramAnonymousContext = new StringBuilder();
      paramAnonymousContext.append("Received broadcast ");
      paramAnonymousContext.append(paramAnonymousIntent.getAction());
      Rlog.d("WAP PUSH", paramAnonymousContext.toString());
      if ("android.intent.action.USER_UNLOCKED".equals(paramAnonymousIntent.getAction())) {
        new WapPushOverSms.BindServiceThread(WapPushOverSms.this, mContext, null).start();
      }
    }
  };
  private final Context mContext;
  private IDeviceIdleController mDeviceIdleController;
  private volatile IWapPushManager mWapPushManager;
  private String mWapPushManagerPackage;
  
  public WapPushOverSms(Context paramContext)
  {
    mContext = paramContext;
    mDeviceIdleController = TelephonyComponentFactory.getInstance().getIDeviceIdleController();
    if (((UserManager)mContext.getSystemService("user")).isUserUnlocked())
    {
      bindWapPushManagerService(mContext);
    }
    else
    {
      IntentFilter localIntentFilter = new IntentFilter();
      localIntentFilter.addAction("android.intent.action.USER_UNLOCKED");
      paramContext.registerReceiver(mBroadcastReceiver, localIntentFilter);
    }
  }
  
  private void bindWapPushManagerService(Context paramContext)
  {
    Intent localIntent = new Intent(IWapPushManager.class.getName());
    ComponentName localComponentName = localIntent.resolveSystemService(paramContext.getPackageManager(), 0);
    localIntent.setComponent(localComponentName);
    if ((localComponentName != null) && (paramContext.bindService(localIntent, this, 1))) {
      try
      {
        mWapPushManagerPackage = localComponentName.getPackageName();
      }
      finally {}
    } else {
      Rlog.e("WAP PUSH", "bindService() for wappush manager failed");
    }
  }
  
  private DecodedResult decodeWapPdu(byte[] paramArrayOfByte, InboundSmsHandler paramInboundSmsHandler)
  {
    DecodedResult localDecodedResult = new DecodedResult(null);
    int i = 0 + 1;
    int j = paramArrayOfByte[0] & 0xFF;
    int k = i + 1;
    int m = paramArrayOfByte[i] & 0xFF;
    try
    {
      int n = paramInboundSmsHandler.getPhone().getPhoneId();
      i = j;
      int i1 = m;
      int i2 = k;
      if (m != 6)
      {
        i = j;
        i1 = m;
        i2 = k;
        if (m != 7)
        {
          i1 = mContext.getResources().getInteger(17694885);
          if (i1 != -1)
          {
            i = i1 + 1;
            k = paramArrayOfByte[i1] & 0xFF;
            j = i + 1;
            m = paramArrayOfByte[i] & 0xFF;
            i = k;
            i1 = m;
            i2 = j;
            if (m != 6)
            {
              i = k;
              i1 = m;
              i2 = j;
              if (m != 7)
              {
                statusCode = 1;
                return localDecodedResult;
              }
            }
          }
          else
          {
            statusCode = 1;
            return localDecodedResult;
          }
        }
      }
      WspTypeDecoder localWspTypeDecoder = TelephonyComponentFactory.getInstance().makeWspTypeDecoder(paramArrayOfByte);
      if (!localWspTypeDecoder.decodeUintvarInteger(i2))
      {
        statusCode = 2;
        return localDecodedResult;
      }
      j = (int)localWspTypeDecoder.getValue32();
      i2 += localWspTypeDecoder.getDecodedDataLength();
      if (!localWspTypeDecoder.decodeContentType(i2))
      {
        statusCode = 2;
        return localDecodedResult;
      }
      String str1 = localWspTypeDecoder.getValueString();
      long l = localWspTypeDecoder.getValue32();
      k = i2 + localWspTypeDecoder.getDecodedDataLength();
      byte[] arrayOfByte = new byte[j];
      System.arraycopy(paramArrayOfByte, i2, arrayOfByte, 0, arrayOfByte.length);
      if ((str1 == null) || (!str1.equals("application/vnd.wap.coc")))
      {
        i2 += j;
        paramInboundSmsHandler = new byte[paramArrayOfByte.length - i2];
        System.arraycopy(paramArrayOfByte, i2, paramInboundSmsHandler, 0, paramInboundSmsHandler.length);
        paramArrayOfByte = paramInboundSmsHandler;
      }
      paramInboundSmsHandler = SubscriptionManager.getSubId(n);
      if ((paramInboundSmsHandler != null) && (paramInboundSmsHandler.length > 0)) {
        i2 = paramInboundSmsHandler[0];
      } else {
        i2 = SmsManager.getDefaultSmsSubscriptionId();
      }
      Object localObject;
      try
      {
        paramInboundSmsHandler = new com/google/android/mms/pdu/PduParser;
        paramInboundSmsHandler.<init>(paramArrayOfByte, shouldParseContentDisposition(i2));
        paramInboundSmsHandler = paramInboundSmsHandler.parse();
      }
      catch (Exception paramInboundSmsHandler)
      {
        localObject = new java/lang/StringBuilder;
        ((StringBuilder)localObject).<init>();
        ((StringBuilder)localObject).append("Unable to parse PDU: ");
        ((StringBuilder)localObject).append(paramInboundSmsHandler.toString());
        Rlog.e("WAP PUSH", ((StringBuilder)localObject).toString());
        paramInboundSmsHandler = null;
      }
      if ((paramInboundSmsHandler != null) && (paramInboundSmsHandler.getMessageType() == 130))
      {
        localObject = (NotificationInd)paramInboundSmsHandler;
        if ((((NotificationInd)localObject).getFrom() != null) && (BlockChecker.isBlocked(mContext, ((NotificationInd)localObject).getFrom().getString(), null)))
        {
          statusCode = 1;
          return localDecodedResult;
        }
      }
      if (localWspTypeDecoder.seekXWapApplicationId(k, k + j - 1))
      {
        localWspTypeDecoder.decodeXWapApplicationId((int)localWspTypeDecoder.getValue32());
        String str2 = localWspTypeDecoder.getValueString();
        localObject = str2;
        if (str2 == null) {
          localObject = Integer.toString((int)localWspTypeDecoder.getValue32());
        }
        wapAppId = ((String)localObject);
        if (str1 == null) {
          localObject = Long.toString(l);
        } else {
          localObject = str1;
        }
        contentType = ((String)localObject);
      }
      subId = i2;
      phoneId = n;
      parsedPdu = paramInboundSmsHandler;
      mimeType = str1;
      transactionId = i;
      pduType = i1;
      header = arrayOfByte;
      intentData = paramArrayOfByte;
      contentTypeParameters = localWspTypeDecoder.getContentParameters();
      statusCode = -1;
    }
    catch (ArrayIndexOutOfBoundsException paramArrayOfByte)
    {
      paramInboundSmsHandler = new StringBuilder();
      paramInboundSmsHandler.append("ignoring dispatchWapPdu() array index exception: ");
      paramInboundSmsHandler.append(paramArrayOfByte);
      Rlog.e("WAP PUSH", paramInboundSmsHandler.toString());
      statusCode = 2;
    }
    return localDecodedResult;
  }
  
  public static int getAppOpsPermissionForIntent(String paramString)
  {
    int i;
    if ("application/vnd.wap.mms-message".equals(paramString)) {
      i = 18;
    } else {
      i = 19;
    }
    return i;
  }
  
  /* Error */
  private static long getDeliveryOrReadReportThreadId(Context paramContext, GenericPdu paramGenericPdu)
  {
    // Byte code:
    //   0: aload_1
    //   1: instanceof 341
    //   4: ifeq +21 -> 25
    //   7: new 207	java/lang/String
    //   10: dup
    //   11: aload_1
    //   12: checkcast 341	com/google/android/mms/pdu/DeliveryInd
    //   15: invokevirtual 345	com/google/android/mms/pdu/DeliveryInd:getMessageId	()[B
    //   18: invokespecial 348	java/lang/String:<init>	([B)V
    //   21: astore_2
    //   22: goto +28 -> 50
    //   25: aload_1
    //   26: instanceof 350
    //   29: ifeq +218 -> 247
    //   32: new 207	java/lang/String
    //   35: dup
    //   36: aload_1
    //   37: checkcast 350	com/google/android/mms/pdu/ReadOrigInd
    //   40: invokevirtual 351	com/google/android/mms/pdu/ReadOrigInd:getMessageId	()[B
    //   43: invokespecial 348	java/lang/String:<init>	([B)V
    //   46: astore_2
    //   47: goto -25 -> 22
    //   50: aconst_null
    //   51: astore_3
    //   52: aconst_null
    //   53: astore 4
    //   55: aload 4
    //   57: astore 5
    //   59: aload_3
    //   60: astore_1
    //   61: aload_0
    //   62: invokevirtual 355	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   65: astore 6
    //   67: aload 4
    //   69: astore 5
    //   71: aload_3
    //   72: astore_1
    //   73: getstatic 361	android/provider/Telephony$Mms:CONTENT_URI	Landroid/net/Uri;
    //   76: astore 7
    //   78: aload 4
    //   80: astore 5
    //   82: aload_3
    //   83: astore_1
    //   84: aload_2
    //   85: invokestatic 367	android/database/DatabaseUtils:sqlEscapeString	(Ljava/lang/String;)Ljava/lang/String;
    //   88: astore 8
    //   90: aload 4
    //   92: astore 5
    //   94: aload_3
    //   95: astore_1
    //   96: sipush 128
    //   99: invokestatic 281	java/lang/Integer:toString	(I)Ljava/lang/String;
    //   102: astore_2
    //   103: aload 4
    //   105: astore 5
    //   107: aload_3
    //   108: astore_1
    //   109: aload_0
    //   110: aload 6
    //   112: aload 7
    //   114: iconst_1
    //   115: anewarray 207	java/lang/String
    //   118: dup
    //   119: iconst_0
    //   120: ldc_w 369
    //   123: aastore
    //   124: ldc 27
    //   126: iconst_2
    //   127: anewarray 207	java/lang/String
    //   130: dup
    //   131: iconst_0
    //   132: aload 8
    //   134: aastore
    //   135: dup
    //   136: iconst_1
    //   137: aload_2
    //   138: aastore
    //   139: aconst_null
    //   140: invokestatic 375	android/database/sqlite/SqliteWrapper:query	(Landroid/content/Context;Landroid/content/ContentResolver;Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   143: astore_0
    //   144: aload_0
    //   145: ifnull +44 -> 189
    //   148: aload_0
    //   149: astore 5
    //   151: aload_0
    //   152: astore_1
    //   153: aload_0
    //   154: invokeinterface 380 1 0
    //   159: ifeq +30 -> 189
    //   162: aload_0
    //   163: astore 5
    //   165: aload_0
    //   166: astore_1
    //   167: aload_0
    //   168: iconst_0
    //   169: invokeinterface 384 2 0
    //   174: lstore 9
    //   176: aload_0
    //   177: ifnull +9 -> 186
    //   180: aload_0
    //   181: invokeinterface 387 1 0
    //   186: lload 9
    //   188: lreturn
    //   189: aload_0
    //   190: ifnull +39 -> 229
    //   193: aload_0
    //   194: invokeinterface 387 1 0
    //   199: goto +30 -> 229
    //   202: astore_0
    //   203: goto +30 -> 233
    //   206: astore_0
    //   207: aload_1
    //   208: astore 5
    //   210: ldc 24
    //   212: ldc_w 389
    //   215: aload_0
    //   216: invokestatic 392	android/telephony/Rlog:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   219: pop
    //   220: aload_1
    //   221: ifnull +8 -> 229
    //   224: aload_1
    //   225: astore_0
    //   226: goto -33 -> 193
    //   229: ldc2_w 393
    //   232: lreturn
    //   233: aload 5
    //   235: ifnull +10 -> 245
    //   238: aload 5
    //   240: invokeinterface 387 1 0
    //   245: aload_0
    //   246: athrow
    //   247: new 236	java/lang/StringBuilder
    //   250: dup
    //   251: invokespecial 237	java/lang/StringBuilder:<init>	()V
    //   254: astore_0
    //   255: aload_0
    //   256: ldc_w 396
    //   259: invokevirtual 243	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   262: pop
    //   263: aload_0
    //   264: aload_1
    //   265: invokevirtual 400	java/lang/Object:getClass	()Ljava/lang/Class;
    //   268: invokevirtual 403	java/lang/Class:getCanonicalName	()Ljava/lang/String;
    //   271: invokevirtual 243	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   274: pop
    //   275: ldc 24
    //   277: aload_0
    //   278: invokevirtual 247	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   281: invokestatic 138	android/telephony/Rlog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   284: pop
    //   285: ldc2_w 393
    //   288: lreturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	289	0	paramContext	Context
    //   0	289	1	paramGenericPdu	GenericPdu
    //   21	117	2	str1	String
    //   51	57	3	localObject1	Object
    //   53	51	4	localObject2	Object
    //   57	182	5	localObject3	Object
    //   65	46	6	localContentResolver	android.content.ContentResolver
    //   76	37	7	localUri	android.net.Uri
    //   88	45	8	str2	String
    //   174	13	9	l	long
    // Exception table:
    //   from	to	target	type
    //   61	67	202	finally
    //   73	78	202	finally
    //   84	90	202	finally
    //   96	103	202	finally
    //   109	144	202	finally
    //   153	162	202	finally
    //   167	176	202	finally
    //   210	220	202	finally
    //   61	67	206	android/database/sqlite/SQLiteException
    //   73	78	206	android/database/sqlite/SQLiteException
    //   84	90	206	android/database/sqlite/SQLiteException
    //   96	103	206	android/database/sqlite/SQLiteException
    //   109	144	206	android/database/sqlite/SQLiteException
    //   153	162	206	android/database/sqlite/SQLiteException
    //   167	176	206	android/database/sqlite/SQLiteException
  }
  
  public static String getPermissionForType(String paramString)
  {
    if ("application/vnd.wap.mms-message".equals(paramString)) {
      paramString = "android.permission.RECEIVE_MMS";
    } else {
      paramString = "android.permission.RECEIVE_WAP_PUSH";
    }
    return paramString;
  }
  
  /* Error */
  private static boolean isDuplicateNotification(Context paramContext, NotificationInd paramNotificationInd)
  {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual 413	com/google/android/mms/pdu/NotificationInd:getContentLocation	()[B
    //   4: astore_2
    //   5: aload_2
    //   6: ifnull +207 -> 213
    //   9: new 207	java/lang/String
    //   12: dup
    //   13: aload_2
    //   14: invokespecial 348	java/lang/String:<init>	([B)V
    //   17: pop
    //   18: aconst_null
    //   19: astore_3
    //   20: aconst_null
    //   21: astore 4
    //   23: aload 4
    //   25: astore 5
    //   27: aload_3
    //   28: astore_1
    //   29: aload_0
    //   30: invokevirtual 355	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   33: astore 6
    //   35: aload 4
    //   37: astore 5
    //   39: aload_3
    //   40: astore_1
    //   41: getstatic 361	android/provider/Telephony$Mms:CONTENT_URI	Landroid/net/Uri;
    //   44: astore 7
    //   46: aload 4
    //   48: astore 5
    //   50: aload_3
    //   51: astore_1
    //   52: sipush 130
    //   55: invokestatic 281	java/lang/Integer:toString	(I)Ljava/lang/String;
    //   58: astore 8
    //   60: aload 4
    //   62: astore 5
    //   64: aload_3
    //   65: astore_1
    //   66: new 207	java/lang/String
    //   69: astore 9
    //   71: aload 4
    //   73: astore 5
    //   75: aload_3
    //   76: astore_1
    //   77: aload 9
    //   79: aload_2
    //   80: invokespecial 348	java/lang/String:<init>	([B)V
    //   83: aload 4
    //   85: astore 5
    //   87: aload_3
    //   88: astore_1
    //   89: aload_0
    //   90: aload 6
    //   92: aload 7
    //   94: iconst_1
    //   95: anewarray 207	java/lang/String
    //   98: dup
    //   99: iconst_0
    //   100: ldc_w 415
    //   103: aastore
    //   104: ldc 21
    //   106: iconst_2
    //   107: anewarray 207	java/lang/String
    //   110: dup
    //   111: iconst_0
    //   112: aload 8
    //   114: aastore
    //   115: dup
    //   116: iconst_1
    //   117: aload 9
    //   119: aastore
    //   120: aconst_null
    //   121: invokestatic 375	android/database/sqlite/SqliteWrapper:query	(Landroid/content/Context;Landroid/content/ContentResolver;Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   124: astore_0
    //   125: aload_0
    //   126: ifnull +33 -> 159
    //   129: aload_0
    //   130: astore 5
    //   132: aload_0
    //   133: astore_1
    //   134: aload_0
    //   135: invokeinterface 418 1 0
    //   140: istore 10
    //   142: iload 10
    //   144: ifle +15 -> 159
    //   147: aload_0
    //   148: ifnull +9 -> 157
    //   151: aload_0
    //   152: invokeinterface 387 1 0
    //   157: iconst_1
    //   158: ireturn
    //   159: aload_0
    //   160: ifnull +53 -> 213
    //   163: goto +27 -> 190
    //   166: astore_0
    //   167: goto +32 -> 199
    //   170: astore_0
    //   171: aload_1
    //   172: astore 5
    //   174: ldc 24
    //   176: ldc_w 420
    //   179: aload_0
    //   180: invokestatic 392	android/telephony/Rlog:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   183: pop
    //   184: aload_1
    //   185: ifnull +28 -> 213
    //   188: aload_1
    //   189: astore_0
    //   190: aload_0
    //   191: invokeinterface 387 1 0
    //   196: goto +17 -> 213
    //   199: aload 5
    //   201: ifnull +10 -> 211
    //   204: aload 5
    //   206: invokeinterface 387 1 0
    //   211: aload_0
    //   212: athrow
    //   213: iconst_0
    //   214: ireturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	215	0	paramContext	Context
    //   0	215	1	paramNotificationInd	NotificationInd
    //   4	76	2	arrayOfByte	byte[]
    //   19	69	3	localObject1	Object
    //   21	63	4	localObject2	Object
    //   25	180	5	localObject3	Object
    //   33	58	6	localContentResolver	android.content.ContentResolver
    //   44	49	7	localUri	android.net.Uri
    //   58	55	8	str1	String
    //   69	49	9	str2	String
    //   140	3	10	i	int
    // Exception table:
    //   from	to	target	type
    //   29	35	166	finally
    //   41	46	166	finally
    //   52	60	166	finally
    //   66	71	166	finally
    //   77	83	166	finally
    //   89	125	166	finally
    //   134	142	166	finally
    //   174	184	166	finally
    //   29	35	170	android/database/sqlite/SQLiteException
    //   41	46	170	android/database/sqlite/SQLiteException
    //   52	60	170	android/database/sqlite/SQLiteException
    //   66	71	170	android/database/sqlite/SQLiteException
    //   77	83	170	android/database/sqlite/SQLiteException
    //   89	125	170	android/database/sqlite/SQLiteException
    //   134	142	170	android/database/sqlite/SQLiteException
  }
  
  private static boolean shouldParseContentDisposition(int paramInt)
  {
    return SmsManager.getSmsManagerForSubscriptionId(paramInt).getCarrierConfigValues().getBoolean("supportMmsContentDisposition", true);
  }
  
  private void writeInboxMessage(int paramInt, GenericPdu paramGenericPdu)
  {
    if (paramGenericPdu == null) {
      Rlog.e("WAP PUSH", "Invalid PUSH PDU");
    }
    Object localObject1 = PduPersister.getPduPersister(mContext);
    int i = paramGenericPdu.getMessageType();
    if (i != 130)
    {
      if ((i != 134) && (i != 136))
      {
        try
        {
          Log.e("WAP PUSH", "Received unrecognized WAP Push PDU.");
        }
        catch (RuntimeException paramGenericPdu)
        {
          break label378;
        }
        catch (MmsException paramGenericPdu)
        {
          break label391;
        }
      }
      else
      {
        long l = getDeliveryOrReadReportThreadId(mContext, paramGenericPdu);
        if (l == -1L)
        {
          Rlog.e("WAP PUSH", "Failed to find delivery or read report's thread id");
          return;
        }
        paramGenericPdu = ((PduPersister)localObject1).persist(paramGenericPdu, Telephony.Mms.Inbox.CONTENT_URI, true, true, null);
        if (paramGenericPdu == null)
        {
          Rlog.e("WAP PUSH", "Failed to persist delivery or read report");
          return;
        }
        localObject2 = new android/content/ContentValues;
        ((ContentValues)localObject2).<init>(1);
        ((ContentValues)localObject2).put("thread_id", Long.valueOf(l));
        if (SqliteWrapper.update(mContext, mContext.getContentResolver(), paramGenericPdu, (ContentValues)localObject2, null, null) == 1) {
          return;
        }
        Rlog.e("WAP PUSH", "Failed to update delivery or read report thread id");
        return;
      }
    }
    else
    {
      localObject2 = (NotificationInd)paramGenericPdu;
      Object localObject3 = SmsManager.getSmsManagerForSubscriptionId(paramInt).getCarrierConfigValues();
      if ((localObject3 != null) && (((Bundle)localObject3).getBoolean("enabledTransID", false)))
      {
        localObject3 = ((NotificationInd)localObject2).getContentLocation();
        if (61 == localObject3[(localObject3.length - 1)])
        {
          byte[] arrayOfByte1 = ((NotificationInd)localObject2).getTransactionId();
          byte[] arrayOfByte2 = new byte[localObject3.length + arrayOfByte1.length];
          System.arraycopy((byte[])localObject3, 0, arrayOfByte2, 0, localObject3.length);
          System.arraycopy(arrayOfByte1, 0, arrayOfByte2, localObject3.length, arrayOfByte1.length);
          ((NotificationInd)localObject2).setContentLocation(arrayOfByte2);
        }
      }
      if (!isDuplicateNotification(mContext, (NotificationInd)localObject2))
      {
        if (((PduPersister)localObject1).persist(paramGenericPdu, Telephony.Mms.Inbox.CONTENT_URI, true, true, null) == null) {
          Rlog.e("WAP PUSH", "Failed to save MMS WAP push notification ind");
        }
        return;
      }
      localObject1 = new java/lang/StringBuilder;
      ((StringBuilder)localObject1).<init>();
      ((StringBuilder)localObject1).append("Skip storing duplicate MMS WAP push notification ind: ");
      paramGenericPdu = new java/lang/String;
      paramGenericPdu.<init>(((NotificationInd)localObject2).getContentLocation());
      ((StringBuilder)localObject1).append(paramGenericPdu);
      Rlog.d("WAP PUSH", ((StringBuilder)localObject1).toString());
      return;
    }
    label378:
    Log.e("WAP PUSH", "Unexpected RuntimeException in persisting MMS WAP push data", paramGenericPdu);
    return;
    label391:
    Object localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("Failed to save MMS WAP push data: type=");
    ((StringBuilder)localObject2).append(i);
    Log.e("WAP PUSH", ((StringBuilder)localObject2).toString(), paramGenericPdu);
  }
  
  public int dispatchWapPdu(byte[] paramArrayOfByte, BroadcastReceiver paramBroadcastReceiver, InboundSmsHandler paramInboundSmsHandler)
  {
    return dispatchWapPdu(paramArrayOfByte, paramBroadcastReceiver, paramInboundSmsHandler, null);
  }
  
  public int dispatchWapPdu(byte[] paramArrayOfByte, BroadcastReceiver paramBroadcastReceiver, InboundSmsHandler paramInboundSmsHandler, String paramString)
  {
    DecodedResult localDecodedResult = decodeWapPdu(paramArrayOfByte, paramInboundSmsHandler);
    if (statusCode != -1) {
      return statusCode;
    }
    if (SmsManager.getDefault().getAutoPersisting()) {
      writeInboxMessage(subId, parsedPdu);
    }
    if (wapAppId != null)
    {
      int i = 1;
      try
      {
        localObject = mWapPushManager;
        int j;
        if (localObject == null) {
          j = i;
        }
        try
        {
          mDeviceIdleController.addPowerSaveTempWhitelistAppForMms(mWapPushManagerPackage, 0, "mms-mgr");
          paramArrayOfByte = new android/content/Intent;
          paramArrayOfByte.<init>();
          paramArrayOfByte.putExtra("transactionId", transactionId);
          paramArrayOfByte.putExtra("pduType", pduType);
          paramArrayOfByte.putExtra("header", header);
          paramArrayOfByte.putExtra("data", intentData);
          paramArrayOfByte.putExtra("contentTypeParameters", contentTypeParameters);
          SubscriptionManager.putPhoneIdAndSubIdExtra(paramArrayOfByte, phoneId);
          if (!TextUtils.isEmpty(paramString)) {
            paramArrayOfByte.putExtra("address", paramString);
          }
          int k = ((IWapPushManager)localObject).processMessage(wapAppId, contentType, paramArrayOfByte);
          j = i;
          if ((k & 0x1) > 0)
          {
            j = i;
            if ((0x8000 & k) == 0) {
              j = 0;
            }
          }
          if (j == 0) {
            return 1;
          }
        }
        finally {}
        if (mimeType != null) {
          break label270;
        }
      }
      catch (RemoteException paramArrayOfByte) {}
    }
    return 2;
    label270:
    Object localObject = new Intent("android.provider.Telephony.WAP_PUSH_DELIVER");
    ((Intent)localObject).setType(mimeType);
    ((Intent)localObject).putExtra("transactionId", transactionId);
    ((Intent)localObject).putExtra("pduType", pduType);
    ((Intent)localObject).putExtra("header", header);
    ((Intent)localObject).putExtra("data", intentData);
    ((Intent)localObject).putExtra("contentTypeParameters", contentTypeParameters);
    SubscriptionManager.putPhoneIdAndSubIdExtra((Intent)localObject, phoneId);
    if (!TextUtils.isEmpty(paramString)) {
      ((Intent)localObject).putExtra("address", paramString);
    }
    paramArrayOfByte = SmsApplication.getDefaultMmsApplication(mContext, true);
    if (paramArrayOfByte != null)
    {
      ((Intent)localObject).setComponent(paramArrayOfByte);
      try
      {
        long l = mDeviceIdleController.addPowerSaveTempWhitelistAppForMms(paramArrayOfByte.getPackageName(), 0, "mms-app");
        paramArrayOfByte = BroadcastOptions.makeBasic();
        paramArrayOfByte.setTemporaryAppWhitelistDuration(l);
        paramArrayOfByte = paramArrayOfByte.toBundle();
      }
      catch (RemoteException paramArrayOfByte) {}
    }
    else
    {
      paramArrayOfByte = null;
    }
    paramInboundSmsHandler.dispatchIntent((Intent)localObject, getPermissionForType(mimeType), getAppOpsPermissionForIntent(mimeType), paramArrayOfByte, paramBroadcastReceiver, UserHandle.SYSTEM);
    return -1;
  }
  
  public void dispose()
  {
    if (mWapPushManager != null) {
      mContext.unbindService(this);
    } else {
      Rlog.e("WAP PUSH", "dispose: not bound to a wappush manager");
    }
  }
  
  public boolean isWapPushForMms(byte[] paramArrayOfByte, InboundSmsHandler paramInboundSmsHandler)
  {
    paramArrayOfByte = decodeWapPdu(paramArrayOfByte, paramInboundSmsHandler);
    boolean bool;
    if ((statusCode == -1) && ("application/vnd.wap.mms-message".equals(mimeType))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
  {
    mWapPushManager = IWapPushManager.Stub.asInterface(paramIBinder);
  }
  
  public void onServiceDisconnected(ComponentName paramComponentName)
  {
    mWapPushManager = null;
  }
  
  private class BindServiceThread
    extends Thread
  {
    private final Context context;
    
    private BindServiceThread(Context paramContext)
    {
      context = paramContext;
    }
    
    public void run()
    {
      WapPushOverSms.this.bindWapPushManagerService(context);
    }
  }
  
  private final class DecodedResult
  {
    String contentType;
    HashMap<String, String> contentTypeParameters;
    byte[] header;
    byte[] intentData;
    String mimeType;
    GenericPdu parsedPdu;
    int pduType;
    int phoneId;
    int statusCode;
    int subId;
    int transactionId;
    String wapAppId;
    
    private DecodedResult() {}
  }
}
