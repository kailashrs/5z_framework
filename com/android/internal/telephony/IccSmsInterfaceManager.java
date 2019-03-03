package com.android.internal.telephony;

import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.Handler;
import android.os.Message;
import android.os.UserManager;
import android.telephony.Rlog;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.cdma.CdmaSmsBroadcastConfigInfo;
import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.util.HexDump;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class IccSmsInterfaceManager
{
  static final boolean DBG = true;
  private static final int EVENT_LOAD_DONE = 1;
  protected static final int EVENT_SET_BROADCAST_ACTIVATION_DONE = 3;
  protected static final int EVENT_SET_BROADCAST_CONFIG_DONE = 4;
  private static final int EVENT_UPDATE_DONE = 2;
  static final String LOG_TAG = "IccSmsInterfaceManager";
  private static final int SMS_CB_CODE_SCHEME_MAX = 255;
  private static final int SMS_CB_CODE_SCHEME_MIN = 0;
  public static final int SMS_MESSAGE_PERIOD_NOT_SPECIFIED = -1;
  public static final int SMS_MESSAGE_PRIORITY_NOT_SPECIFIED = -1;
  protected final AppOpsManager mAppOps;
  private CdmaBroadcastRangeManager mCdmaBroadcastRangeManager = new CdmaBroadcastRangeManager();
  private CellBroadcastRangeManager mCellBroadcastRangeManager = new CellBroadcastRangeManager();
  protected final Context mContext;
  protected SmsDispatchersController mDispatchersController;
  protected Handler mHandler = new Handler()
  {
    public void handleMessage(Message arg1)
    {
      int i = what;
      boolean bool1 = false;
      boolean bool2 = false;
      Object localObject1;
      switch (i)
      {
      default: 
        break;
      case 3: 
      case 4: 
        localObject1 = (AsyncResult)obj;
        synchronized (mLock)
        {
          IccSmsInterfaceManager localIccSmsInterfaceManager = IccSmsInterfaceManager.this;
          if (exception == null) {
            bool2 = true;
          }
          mSuccess = bool2;
          mLock.notifyAll();
        }
      case 2: 
        AsyncResult localAsyncResult1 = (AsyncResult)obj;
        synchronized (mLock)
        {
          localObject1 = IccSmsInterfaceManager.this;
          bool2 = bool1;
          if (exception == null) {
            bool2 = true;
          }
          mSuccess = bool2;
          mLock.notifyAll();
        }
      case 1: 
        AsyncResult localAsyncResult2 = (AsyncResult)obj;
        synchronized (mLock)
        {
          if (exception == null)
          {
            IccSmsInterfaceManager.access$002(IccSmsInterfaceManager.this, buildValidRawData((ArrayList)result));
            markMessagesAsRead((ArrayList)result);
          }
          else
          {
            if (Rlog.isLoggable("SMS", 3)) {
              log("Cannot load Sms records");
            }
            IccSmsInterfaceManager.access$002(IccSmsInterfaceManager.this, null);
          }
          mLock.notifyAll();
        }
      }
    }
  };
  protected final Object mLock = new Object();
  protected Phone mPhone;
  private List<SmsRawData> mSms;
  protected boolean mSuccess;
  private final UserManager mUserManager;
  
  protected IccSmsInterfaceManager(Phone paramPhone)
  {
    this(paramPhone, paramPhone.getContext(), (AppOpsManager)paramPhone.getContext().getSystemService("appops"), (UserManager)paramPhone.getContext().getSystemService("user"), new SmsDispatchersController(paramPhone, mSmsStorageMonitor, mSmsUsageMonitor));
  }
  
  @VisibleForTesting
  public IccSmsInterfaceManager(Phone paramPhone, Context paramContext, AppOpsManager paramAppOpsManager, UserManager paramUserManager, SmsDispatchersController paramSmsDispatchersController)
  {
    mPhone = paramPhone;
    mContext = paramContext;
    mAppOps = paramAppOpsManager;
    mUserManager = paramUserManager;
    mDispatchersController = paramSmsDispatchersController;
  }
  
  private boolean checkCallingOrSelfSendSmsPermission(String paramString1, String paramString2)
  {
    mContext.enforceCallingOrSelfPermission("android.permission.SEND_SMS", paramString2);
    boolean bool;
    if (mAppOps.noteOp(20, Binder.getCallingUid(), paramString1) == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean checkCallingSendSmsPermission(String paramString1, String paramString2)
  {
    mContext.enforceCallingPermission("android.permission.SEND_SMS", paramString2);
    boolean bool;
    if (mAppOps.noteOp(20, Binder.getCallingUid(), paramString1) == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private String filterDestAddress(String paramString)
  {
    String str = SmsNumberUtils.filterDestAddr(mPhone, paramString);
    if (str != null) {
      paramString = str;
    }
    return paramString;
  }
  
  /* Error */
  private boolean isFailedOrDraft(ContentResolver paramContentResolver, Uri paramUri)
  {
    // Byte code:
    //   0: invokestatic 177	android/os/Binder:clearCallingIdentity	()J
    //   3: lstore_3
    //   4: aconst_null
    //   5: astore 5
    //   7: aconst_null
    //   8: astore 6
    //   10: iconst_0
    //   11: istore 7
    //   13: aload_1
    //   14: aload_2
    //   15: iconst_1
    //   16: anewarray 179	java/lang/String
    //   19: dup
    //   20: iconst_0
    //   21: ldc -75
    //   23: aastore
    //   24: aconst_null
    //   25: aconst_null
    //   26: aconst_null
    //   27: invokevirtual 187	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   30: astore_1
    //   31: aload_1
    //   32: ifnull +71 -> 103
    //   35: aload_1
    //   36: astore 6
    //   38: aload_1
    //   39: astore 5
    //   41: aload_1
    //   42: invokeinterface 193 1 0
    //   47: ifeq +56 -> 103
    //   50: aload_1
    //   51: astore 6
    //   53: aload_1
    //   54: astore 5
    //   56: aload_1
    //   57: iconst_0
    //   58: invokeinterface 197 2 0
    //   63: istore 8
    //   65: iload 8
    //   67: iconst_3
    //   68: if_icmpeq +15 -> 83
    //   71: iload 8
    //   73: iconst_5
    //   74: if_icmpne +6 -> 80
    //   77: goto +6 -> 83
    //   80: goto +6 -> 86
    //   83: iconst_1
    //   84: istore 7
    //   86: aload_1
    //   87: ifnull +9 -> 96
    //   90: aload_1
    //   91: invokeinterface 200 1 0
    //   96: lload_3
    //   97: invokestatic 204	android/os/Binder:restoreCallingIdentity	(J)V
    //   100: iload 7
    //   102: ireturn
    //   103: aload_1
    //   104: ifnull +38 -> 142
    //   107: goto +29 -> 136
    //   110: astore_1
    //   111: goto +37 -> 148
    //   114: astore_1
    //   115: aload 5
    //   117: astore 6
    //   119: ldc 27
    //   121: ldc -50
    //   123: aload_1
    //   124: invokestatic 212	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   127: pop
    //   128: aload 5
    //   130: ifnull +12 -> 142
    //   133: aload 5
    //   135: astore_1
    //   136: aload_1
    //   137: invokeinterface 200 1 0
    //   142: lload_3
    //   143: invokestatic 204	android/os/Binder:restoreCallingIdentity	(J)V
    //   146: iconst_0
    //   147: ireturn
    //   148: aload 6
    //   150: ifnull +10 -> 160
    //   153: aload 6
    //   155: invokeinterface 200 1 0
    //   160: lload_3
    //   161: invokestatic 204	android/os/Binder:restoreCallingIdentity	(J)V
    //   164: aload_1
    //   165: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	166	0	this	IccSmsInterfaceManager
    //   0	166	1	paramContentResolver	ContentResolver
    //   0	166	2	paramUri	Uri
    //   3	158	3	l	long
    //   5	129	5	localContentResolver1	ContentResolver
    //   8	146	6	localContentResolver2	ContentResolver
    //   11	90	7	bool	boolean
    //   63	12	8	i	int
    // Exception table:
    //   from	to	target	type
    //   13	31	110	finally
    //   41	50	110	finally
    //   56	65	110	finally
    //   119	128	110	finally
    //   13	31	114	android/database/sqlite/SQLiteException
    //   41	50	114	android/database/sqlite/SQLiteException
    //   56	65	114	android/database/sqlite/SQLiteException
  }
  
  /* Error */
  private String[] loadTextAndAddress(ContentResolver paramContentResolver, Uri paramUri)
  {
    // Byte code:
    //   0: invokestatic 177	android/os/Binder:clearCallingIdentity	()J
    //   3: lstore_3
    //   4: aconst_null
    //   5: astore 5
    //   7: aconst_null
    //   8: astore 6
    //   10: aload_1
    //   11: aload_2
    //   12: iconst_2
    //   13: anewarray 179	java/lang/String
    //   16: dup
    //   17: iconst_0
    //   18: ldc -22
    //   20: aastore
    //   21: dup
    //   22: iconst_1
    //   23: ldc -20
    //   25: aastore
    //   26: aconst_null
    //   27: aconst_null
    //   28: aconst_null
    //   29: invokevirtual 187	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   32: astore_1
    //   33: aload_1
    //   34: ifnull +75 -> 109
    //   37: aload_1
    //   38: astore 6
    //   40: aload_1
    //   41: astore 5
    //   43: aload_1
    //   44: invokeinterface 193 1 0
    //   49: ifeq +60 -> 109
    //   52: aload_1
    //   53: astore 6
    //   55: aload_1
    //   56: astore 5
    //   58: aload_1
    //   59: iconst_0
    //   60: invokeinterface 240 2 0
    //   65: astore_2
    //   66: aload_1
    //   67: astore 6
    //   69: aload_1
    //   70: astore 5
    //   72: aload_1
    //   73: iconst_1
    //   74: invokeinterface 240 2 0
    //   79: astore 7
    //   81: aload_1
    //   82: ifnull +9 -> 91
    //   85: aload_1
    //   86: invokeinterface 200 1 0
    //   91: lload_3
    //   92: invokestatic 204	android/os/Binder:restoreCallingIdentity	(J)V
    //   95: iconst_2
    //   96: anewarray 179	java/lang/String
    //   99: dup
    //   100: iconst_0
    //   101: aload_2
    //   102: aastore
    //   103: dup
    //   104: iconst_1
    //   105: aload 7
    //   107: aastore
    //   108: areturn
    //   109: aload_1
    //   110: ifnull +38 -> 148
    //   113: goto +29 -> 142
    //   116: astore_1
    //   117: goto +37 -> 154
    //   120: astore_1
    //   121: aload 5
    //   123: astore 6
    //   125: ldc 27
    //   127: ldc -14
    //   129: aload_1
    //   130: invokestatic 212	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   133: pop
    //   134: aload 5
    //   136: ifnull +12 -> 148
    //   139: aload 5
    //   141: astore_1
    //   142: aload_1
    //   143: invokeinterface 200 1 0
    //   148: lload_3
    //   149: invokestatic 204	android/os/Binder:restoreCallingIdentity	(J)V
    //   152: aconst_null
    //   153: areturn
    //   154: aload 6
    //   156: ifnull +10 -> 166
    //   159: aload 6
    //   161: invokeinterface 200 1 0
    //   166: lload_3
    //   167: invokestatic 204	android/os/Binder:restoreCallingIdentity	(J)V
    //   170: aload_1
    //   171: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	172	0	this	IccSmsInterfaceManager
    //   0	172	1	paramContentResolver	ContentResolver
    //   0	172	2	paramUri	Uri
    //   3	164	3	l	long
    //   5	135	5	localContentResolver1	ContentResolver
    //   8	152	6	localContentResolver2	ContentResolver
    //   79	27	7	str	String
    // Exception table:
    //   from	to	target	type
    //   10	33	116	finally
    //   43	52	116	finally
    //   58	66	116	finally
    //   72	81	116	finally
    //   125	134	116	finally
    //   10	33	120	android/database/sqlite/SQLiteException
    //   43	52	120	android/database/sqlite/SQLiteException
    //   58	66	120	android/database/sqlite/SQLiteException
    //   72	81	120	android/database/sqlite/SQLiteException
  }
  
  private void returnUnspecifiedFailure(PendingIntent paramPendingIntent)
  {
    if (paramPendingIntent != null) {
      try
      {
        paramPendingIntent.send(1);
      }
      catch (PendingIntent.CanceledException paramPendingIntent) {}
    }
  }
  
  private void returnUnspecifiedFailure(List<PendingIntent> paramList)
  {
    if (paramList == null) {
      return;
    }
    paramList = paramList.iterator();
    while (paramList.hasNext()) {
      returnUnspecifiedFailure((PendingIntent)paramList.next());
    }
  }
  
  private void sendDataInternal(String paramString1, String paramString2, int paramInt, byte[] paramArrayOfByte, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2)
  {
    if (Rlog.isLoggable("SMS", 2))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("sendData: destAddr=");
      localStringBuilder.append(paramString1);
      localStringBuilder.append(" scAddr=");
      localStringBuilder.append(paramString2);
      localStringBuilder.append(" destPort=");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(" data='");
      localStringBuilder.append(HexDump.toHexString(paramArrayOfByte));
      localStringBuilder.append("' sentIntent=");
      localStringBuilder.append(paramPendingIntent1);
      localStringBuilder.append(" deliveryIntent=");
      localStringBuilder.append(paramPendingIntent2);
      log(localStringBuilder.toString());
    }
    paramString1 = filterDestAddress(paramString1);
    mDispatchersController.sendData(paramString1, paramString2, paramInt, paramArrayOfByte, paramPendingIntent1, paramPendingIntent2);
  }
  
  private void sendTextInternal(String paramString1, String paramString2, String paramString3, String paramString4, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, boolean paramBoolean1, int paramInt1, boolean paramBoolean2, int paramInt2)
  {
    if (Rlog.isLoggable("SMS", 2))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("sendText: destAddr=");
      localStringBuilder.append(paramString2);
      localStringBuilder.append(" scAddr=");
      localStringBuilder.append(paramString3);
      localStringBuilder.append(" text='");
      localStringBuilder.append(paramString4);
      localStringBuilder.append("' sentIntent=");
      localStringBuilder.append(paramPendingIntent1);
      localStringBuilder.append(" deliveryIntent=");
      localStringBuilder.append(paramPendingIntent2);
      localStringBuilder.append(" priority=");
      localStringBuilder.append(paramInt1);
      localStringBuilder.append(" expectMore=");
      localStringBuilder.append(paramBoolean2);
      localStringBuilder.append(" validityPeriod=");
      localStringBuilder.append(paramInt2);
      log(localStringBuilder.toString());
    }
    paramString2 = filterDestAddress(paramString2);
    mDispatchersController.sendText(paramString2, paramString3, paramString4, paramPendingIntent1, paramPendingIntent2, null, paramString1, paramBoolean1, paramInt1, paramBoolean2, paramInt2);
  }
  
  private boolean setCdmaBroadcastActivation(boolean paramBoolean)
  {
    ??? = new StringBuilder();
    ((StringBuilder)???).append("Calling setCdmaBroadcastActivation(");
    ((StringBuilder)???).append(paramBoolean);
    ((StringBuilder)???).append(")");
    log(((StringBuilder)???).toString());
    synchronized (mLock)
    {
      Message localMessage = mHandler.obtainMessage(3);
      mSuccess = false;
      mPhone.mCi.setCdmaBroadcastActivation(paramBoolean, localMessage);
      try
      {
        mLock.wait();
      }
      catch (InterruptedException localInterruptedException)
      {
        log("interrupted while trying to set cdma broadcast activation");
      }
      return mSuccess;
    }
  }
  
  private boolean setCdmaBroadcastConfig(CdmaSmsBroadcastConfigInfo[] paramArrayOfCdmaSmsBroadcastConfigInfo)
  {
    ??? = new StringBuilder();
    ((StringBuilder)???).append("Calling setCdmaBroadcastConfig with ");
    ((StringBuilder)???).append(paramArrayOfCdmaSmsBroadcastConfigInfo.length);
    ((StringBuilder)???).append(" configurations");
    log(((StringBuilder)???).toString());
    synchronized (mLock)
    {
      Message localMessage = mHandler.obtainMessage(4);
      mSuccess = false;
      mPhone.mCi.setCdmaBroadcastConfig(paramArrayOfCdmaSmsBroadcastConfigInfo, localMessage);
      try
      {
        mLock.wait();
      }
      catch (InterruptedException paramArrayOfCdmaSmsBroadcastConfigInfo)
      {
        log("interrupted while trying to set cdma broadcast config");
      }
      return mSuccess;
    }
  }
  
  private boolean setCellBroadcastActivation(boolean paramBoolean)
  {
    ??? = new StringBuilder();
    ((StringBuilder)???).append("Calling setCellBroadcastActivation(");
    ((StringBuilder)???).append(paramBoolean);
    ((StringBuilder)???).append(')');
    log(((StringBuilder)???).toString());
    synchronized (mLock)
    {
      Message localMessage = mHandler.obtainMessage(3);
      mSuccess = false;
      mPhone.mCi.setGsmBroadcastActivation(paramBoolean, localMessage);
      try
      {
        mLock.wait();
      }
      catch (InterruptedException localInterruptedException)
      {
        log("interrupted while trying to set cell broadcast activation");
      }
      return mSuccess;
    }
  }
  
  private boolean setCellBroadcastConfig(SmsBroadcastConfigInfo[] paramArrayOfSmsBroadcastConfigInfo)
  {
    ??? = new StringBuilder();
    ((StringBuilder)???).append("Calling setGsmBroadcastConfig with ");
    ((StringBuilder)???).append(paramArrayOfSmsBroadcastConfigInfo.length);
    ((StringBuilder)???).append(" configurations");
    log(((StringBuilder)???).toString());
    synchronized (mLock)
    {
      Message localMessage = mHandler.obtainMessage(4);
      mSuccess = false;
      mPhone.mCi.setGsmBroadcastConfig(paramArrayOfSmsBroadcastConfigInfo, localMessage);
      try
      {
        mLock.wait();
      }
      catch (InterruptedException paramArrayOfSmsBroadcastConfigInfo)
      {
        log("interrupted while trying to set cell broadcast config");
      }
      return mSuccess;
    }
  }
  
  protected ArrayList<SmsRawData> buildValidRawData(ArrayList<byte[]> paramArrayList)
  {
    int i = paramArrayList.size();
    ArrayList localArrayList = new ArrayList(i);
    for (int j = 0; j < i; j++) {
      if (((byte[])paramArrayList.get(j))[0] == 0) {
        localArrayList.add(null);
      } else {
        localArrayList.add(new SmsRawData((byte[])paramArrayList.get(j)));
      }
    }
    return localArrayList;
  }
  
  @VisibleForTesting
  public boolean checkCallingSendTextPermissions(boolean paramBoolean, String paramString1, String paramString2)
  {
    if (!paramBoolean) {
      try
      {
        enforceCallerIsImsAppOrCarrierApp(paramString2);
        return true;
      }
      catch (SecurityException localSecurityException)
      {
        mContext.enforceCallingPermission("android.permission.MODIFY_PHONE_STATE", paramString2);
      }
    }
    return checkCallingSendSmsPermission(paramString1, paramString2);
  }
  
  public boolean copyMessageToIccEf(String arg1, int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("copyMessageToIccEf: status=");
    ((StringBuilder)localObject).append(paramInt);
    ((StringBuilder)localObject).append(" ==> pdu=(");
    ((StringBuilder)localObject).append(Arrays.toString(paramArrayOfByte1));
    ((StringBuilder)localObject).append("), smsc=(");
    ((StringBuilder)localObject).append(Arrays.toString(paramArrayOfByte2));
    ((StringBuilder)localObject).append(")");
    log(((StringBuilder)localObject).toString());
    enforceReceiveAndSend("Copying message to Icc");
    if (mAppOps.noteOp(22, Binder.getCallingUid(), ???) != 0) {
      return false;
    }
    synchronized (mLock)
    {
      mSuccess = false;
      localObject = mHandler.obtainMessage(2);
      if (1 == mPhone.getPhoneType()) {
        mPhone.mCi.writeSmsToSim(paramInt, IccUtils.bytesToHexString(paramArrayOfByte2), IccUtils.bytesToHexString(paramArrayOfByte1), (Message)localObject);
      } else {
        mPhone.mCi.writeSmsToRuim(paramInt, IccUtils.bytesToHexString(paramArrayOfByte1), (Message)localObject);
      }
      try
      {
        mLock.wait();
      }
      catch (InterruptedException paramArrayOfByte1)
      {
        log("interrupted while trying to update by index");
      }
      return mSuccess;
    }
  }
  
  public boolean disableCdmaBroadcastRange(int paramInt1, int paramInt2)
  {
    try
    {
      mContext.enforceCallingPermission("android.permission.RECEIVE_SMS", "Disabling cell broadcast SMS");
      String str = mContext.getPackageManager().getNameForUid(Binder.getCallingUid());
      if (!mCdmaBroadcastRangeManager.disableRange(paramInt1, paramInt2, str))
      {
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Failed to remove cdma broadcast subscription for MID range ");
        localStringBuilder.append(paramInt1);
        localStringBuilder.append(" to ");
        localStringBuilder.append(paramInt2);
        localStringBuilder.append(" from client ");
        localStringBuilder.append(str);
        log(localStringBuilder.toString());
        return false;
      }
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Removed cdma broadcast subscription for MID range ");
      localStringBuilder.append(paramInt1);
      localStringBuilder.append(" to ");
      localStringBuilder.append(paramInt2);
      localStringBuilder.append(" from client ");
      localStringBuilder.append(str);
      log(localStringBuilder.toString());
      setCdmaBroadcastActivation(mCdmaBroadcastRangeManager.isEmpty() ^ true);
      return true;
    }
    finally {}
  }
  
  public boolean disableCellBroadcast(int paramInt1, int paramInt2)
  {
    return disableCellBroadcastRange(paramInt1, paramInt1, paramInt2);
  }
  
  public boolean disableCellBroadcastRange(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt3 == 0) {
      return disableGsmBroadcastRange(paramInt1, paramInt2);
    }
    if (paramInt3 == 1) {
      return disableCdmaBroadcastRange(paramInt1, paramInt2);
    }
    throw new IllegalArgumentException("Not a supportted RAN Type");
  }
  
  public boolean disableGsmBroadcastRange(int paramInt1, int paramInt2)
  {
    try
    {
      mContext.enforceCallingPermission("android.permission.RECEIVE_SMS", "Disabling cell broadcast SMS");
      String str = mContext.getPackageManager().getNameForUid(Binder.getCallingUid());
      if (!mCellBroadcastRangeManager.disableRange(paramInt1, paramInt2, str))
      {
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Failed to remove GSM cell broadcast subscription for MID range ");
        localStringBuilder.append(paramInt1);
        localStringBuilder.append(" to ");
        localStringBuilder.append(paramInt2);
        localStringBuilder.append(" from client ");
        localStringBuilder.append(str);
        log(localStringBuilder.toString());
        return false;
      }
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Removed GSM cell broadcast subscription for MID range ");
      localStringBuilder.append(paramInt1);
      localStringBuilder.append(" to ");
      localStringBuilder.append(paramInt2);
      localStringBuilder.append(" from client ");
      localStringBuilder.append(str);
      log(localStringBuilder.toString());
      setCellBroadcastActivation(mCellBroadcastRangeManager.isEmpty() ^ true);
      return true;
    }
    finally {}
  }
  
  public boolean enableCdmaBroadcastRange(int paramInt1, int paramInt2)
  {
    try
    {
      mContext.enforceCallingPermission("android.permission.RECEIVE_SMS", "Enabling cdma broadcast SMS");
      String str = mContext.getPackageManager().getNameForUid(Binder.getCallingUid());
      if (!mCdmaBroadcastRangeManager.enableRange(paramInt1, paramInt2, str))
      {
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Failed to add cdma broadcast subscription for MID range ");
        localStringBuilder.append(paramInt1);
        localStringBuilder.append(" to ");
        localStringBuilder.append(paramInt2);
        localStringBuilder.append(" from client ");
        localStringBuilder.append(str);
        log(localStringBuilder.toString());
        return false;
      }
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Added cdma broadcast subscription for MID range ");
      localStringBuilder.append(paramInt1);
      localStringBuilder.append(" to ");
      localStringBuilder.append(paramInt2);
      localStringBuilder.append(" from client ");
      localStringBuilder.append(str);
      log(localStringBuilder.toString());
      setCdmaBroadcastActivation(mCdmaBroadcastRangeManager.isEmpty() ^ true);
      return true;
    }
    finally {}
  }
  
  public boolean enableCellBroadcast(int paramInt1, int paramInt2)
  {
    return enableCellBroadcastRange(paramInt1, paramInt1, paramInt2);
  }
  
  public boolean enableCellBroadcastRange(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt3 == 0) {
      return enableGsmBroadcastRange(paramInt1, paramInt2);
    }
    if (paramInt3 == 1) {
      return enableCdmaBroadcastRange(paramInt1, paramInt2);
    }
    throw new IllegalArgumentException("Not a supportted RAN Type");
  }
  
  public boolean enableGsmBroadcastRange(int paramInt1, int paramInt2)
  {
    try
    {
      mContext.enforceCallingPermission("android.permission.RECEIVE_SMS", "Enabling cell broadcast SMS");
      String str = mContext.getPackageManager().getNameForUid(Binder.getCallingUid());
      if (!mCellBroadcastRangeManager.enableRange(paramInt1, paramInt2, str))
      {
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Failed to add GSM cell broadcast subscription for MID range ");
        localStringBuilder.append(paramInt1);
        localStringBuilder.append(" to ");
        localStringBuilder.append(paramInt2);
        localStringBuilder.append(" from client ");
        localStringBuilder.append(str);
        log(localStringBuilder.toString());
        return false;
      }
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Added GSM cell broadcast subscription for MID range ");
      localStringBuilder.append(paramInt1);
      localStringBuilder.append(" to ");
      localStringBuilder.append(paramInt2);
      localStringBuilder.append(" from client ");
      localStringBuilder.append(str);
      log(localStringBuilder.toString());
      setCellBroadcastActivation(mCellBroadcastRangeManager.isEmpty() ^ true);
      return true;
    }
    finally {}
  }
  
  @VisibleForTesting
  public void enforceCallerIsImsAppOrCarrierApp(String paramString)
  {
    int i = Binder.getCallingUid();
    String str = CarrierSmsUtils.getCarrierImsPackageForIntent(mContext, mPhone, new Intent("android.service.carrier.CarrierMessagingService"));
    if (str != null) {
      try
      {
        int j = mContext.getPackageManager().getPackageUid(str, 0);
        if (i == j) {
          return;
        }
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
        if (Rlog.isLoggable("SMS", 3)) {
          log("Cannot find configured carrier ims package");
        }
      }
    }
    TelephonyPermissions.enforceCallingOrSelfCarrierPrivilege(mPhone.getSubId(), paramString);
  }
  
  protected void enforceReceiveAndSend(String paramString)
  {
    mContext.enforceCallingOrSelfPermission("android.permission.RECEIVE_SMS", paramString);
    mContext.enforceCallingOrSelfPermission("android.permission.SEND_SMS", paramString);
  }
  
  public List<SmsRawData> getAllMessagesFromIccEf(String arg1)
  {
    log("getAllMessagesFromEF");
    mContext.enforceCallingOrSelfPermission("android.permission.RECEIVE_SMS", "Reading messages from Icc");
    if (mAppOps.noteOp(21, Binder.getCallingUid(), ???) != 0) {
      return new ArrayList();
    }
    synchronized (mLock)
    {
      Object localObject1 = mPhone.getIccFileHandler();
      if (localObject1 == null)
      {
        Rlog.e("IccSmsInterfaceManager", "Cannot load Sms records. No icc card?");
        mSms = null;
        localObject1 = mSms;
        return localObject1;
      }
      ((IccFileHandler)localObject1).loadEFLinearFixedAll(28476, mHandler.obtainMessage(1));
      try
      {
        mLock.wait();
      }
      catch (InterruptedException localInterruptedException)
      {
        log("interrupted while trying to load from the Icc");
      }
      return mSms;
    }
  }
  
  public String getImsSmsFormat()
  {
    return mDispatchersController.getImsSmsFormat();
  }
  
  public int getPremiumSmsPermission(String paramString)
  {
    return mDispatchersController.getPremiumSmsPermission(paramString);
  }
  
  public int getSmsCapacityOnIcc()
  {
    int i = -1;
    Object localObject = mPhone.getIccRecords();
    if (localObject != null) {
      i = ((IccRecords)localObject).getSmsCapacityOnIcc();
    } else {
      log("getSmsCapacityOnIcc - aborting, no icc card present.");
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("getSmsCapacityOnIcc().numberOnIcc = ");
    ((StringBuilder)localObject).append(i);
    log(((StringBuilder)localObject).toString());
    return i;
  }
  
  public void injectSmsPdu(byte[] paramArrayOfByte, String paramString, PendingIntent paramPendingIntent)
  {
    if (mContext.checkCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE") != 0) {
      enforceCallerIsImsAppOrCarrierApp("injectSmsPdu");
    }
    if (Rlog.isLoggable("SMS", 2))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("pdu: ");
      localStringBuilder.append(paramArrayOfByte);
      localStringBuilder.append("\n format=");
      localStringBuilder.append(paramString);
      localStringBuilder.append("\n receivedIntent=");
      localStringBuilder.append(paramPendingIntent);
      log(localStringBuilder.toString());
    }
    mDispatchersController.injectSmsPdu(paramArrayOfByte, paramString, new _..Lambda.IccSmsInterfaceManager.rB1zRNxMbL7VadRMSxZ5tebvHwM(paramPendingIntent));
  }
  
  public boolean isImsSmsSupported()
  {
    return mDispatchersController.isIms();
  }
  
  protected void log(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[IccSmsInterfaceManager] ");
    localStringBuilder.append(paramString);
    Log.d("IccSmsInterfaceManager", localStringBuilder.toString());
  }
  
  protected byte[] makeSmsRecordData(int paramInt, byte[] paramArrayOfByte)
  {
    byte[] arrayOfByte;
    if (1 == mPhone.getPhoneType()) {
      arrayOfByte = new byte['°'];
    } else {
      arrayOfByte = new byte['ÿ'];
    }
    arrayOfByte[0] = ((byte)(byte)(paramInt & 0x7));
    System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 1, paramArrayOfByte.length);
    for (paramInt = paramArrayOfByte.length + 1; paramInt < arrayOfByte.length; paramInt++) {
      arrayOfByte[paramInt] = ((byte)-1);
    }
    return arrayOfByte;
  }
  
  protected void markMessagesAsRead(ArrayList<byte[]> paramArrayList)
  {
    if (paramArrayList == null) {
      return;
    }
    IccFileHandler localIccFileHandler = mPhone.getIccFileHandler();
    if (localIccFileHandler == null)
    {
      if (Rlog.isLoggable("SMS", 3)) {
        log("markMessagesAsRead - aborting, no icc card present.");
      }
      return;
    }
    int i = paramArrayList.size();
    for (int j = 0; j < i; j++)
    {
      Object localObject = (byte[])paramArrayList.get(j);
      if (localObject[0] == 3)
      {
        int k = localObject.length;
        byte[] arrayOfByte = new byte[k - 1];
        System.arraycopy((byte[])localObject, 1, arrayOfByte, 0, k - 1);
        localIccFileHandler.updateEFLinearFixed(28476, j + 1, makeSmsRecordData(1, arrayOfByte), null, null);
        if (Rlog.isLoggable("SMS", 3))
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("SMS ");
          ((StringBuilder)localObject).append(j + 1);
          ((StringBuilder)localObject).append(" marked as read");
          log(((StringBuilder)localObject).toString());
        }
      }
    }
  }
  
  public void sendData(String paramString1, String paramString2, String paramString3, int paramInt, byte[] paramArrayOfByte, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2)
  {
    if (!checkCallingSendSmsPermission(paramString1, "Sending SMS message")) {
      return;
    }
    sendDataInternal(paramString2, paramString3, paramInt, paramArrayOfByte, paramPendingIntent1, paramPendingIntent2);
  }
  
  public void sendDataWithSelfPermissions(String paramString1, String paramString2, String paramString3, int paramInt, byte[] paramArrayOfByte, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2)
  {
    if (!checkCallingOrSelfSendSmsPermission(paramString1, "Sending SMS message")) {
      return;
    }
    sendDataInternal(paramString2, paramString3, paramInt, paramArrayOfByte, paramPendingIntent1, paramPendingIntent2);
  }
  
  public void sendMultipartText(String paramString1, String paramString2, String paramString3, List<String> paramList, List<PendingIntent> paramList1, List<PendingIntent> paramList2, boolean paramBoolean)
  {
    sendMultipartTextWithOptions(paramString1, paramString2, paramString3, paramList, paramList1, paramList2, paramBoolean, -1, false, -1);
  }
  
  public void sendMultipartTextWithOptions(String paramString1, String paramString2, String paramString3, List<String> paramList, List<PendingIntent> paramList1, List<PendingIntent> paramList2, boolean paramBoolean1, int paramInt1, boolean paramBoolean2, int paramInt2)
  {
    if (!checkCallingSendTextPermissions(paramBoolean1, paramString1, "Sending SMS message")) {
      return;
    }
    int i;
    Object localObject1;
    Object localObject2;
    StringBuilder localStringBuilder;
    if (Rlog.isLoggable("SMS", 2))
    {
      i = 0;
      localObject1 = paramList.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (String)((Iterator)localObject1).next();
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("sendMultipartTextWithOptions: destAddr=");
        localStringBuilder.append(paramString2);
        localStringBuilder.append(", srAddr=");
        localStringBuilder.append(paramString3);
        localStringBuilder.append(", part[");
        localStringBuilder.append(i);
        localStringBuilder.append("]=");
        localStringBuilder.append((String)localObject2);
        log(localStringBuilder.toString());
        i++;
      }
    }
    String str = filterDestAddress(paramString2);
    if ((paramList.size() > 1) && (paramList.size() < 10) && (!SmsMessage.hasEmsSupport()))
    {
      for (i = 0; i < paramList.size(); i++)
      {
        paramString2 = (String)paramList.get(i);
        if (SmsMessage.shouldAppendPageNumberAsPrefix())
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append(String.valueOf(i + 1));
          ((StringBuilder)localObject1).append('/');
          ((StringBuilder)localObject1).append(paramList.size());
          ((StringBuilder)localObject1).append(' ');
          ((StringBuilder)localObject1).append(paramString2);
        }
        for (paramString2 = ((StringBuilder)localObject1).toString();; paramString2 = paramString2.concat(((StringBuilder)localObject1).toString()))
        {
          break;
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append(' ');
          ((StringBuilder)localObject1).append(String.valueOf(i + 1));
          ((StringBuilder)localObject1).append('/');
          ((StringBuilder)localObject1).append(paramList.size());
        }
        localObject2 = null;
        localObject1 = localObject2;
        if (paramList1 != null)
        {
          localObject1 = localObject2;
          if (paramList1.size() > i) {
            localObject1 = (PendingIntent)paramList1.get(i);
          }
        }
        localStringBuilder = null;
        localObject2 = localStringBuilder;
        if (paramList2 != null)
        {
          localObject2 = localStringBuilder;
          if (paramList2.size() > i) {
            localObject2 = (PendingIntent)paramList2.get(i);
          }
        }
        mDispatchersController.sendText(str, paramString3, paramString2, (PendingIntent)localObject1, (PendingIntent)localObject2, null, paramString1, paramBoolean1, paramInt1, paramBoolean2, paramInt2);
      }
      return;
    }
    mDispatchersController.sendMultipartText(str, paramString3, (ArrayList)paramList, (ArrayList)paramList1, (ArrayList)paramList2, null, paramString1, paramBoolean1, paramInt1, paramBoolean2, paramInt2);
  }
  
  public void sendStoredMultipartText(String paramString1, Uri paramUri, String paramString2, List<PendingIntent> paramList1, List<PendingIntent> paramList2)
  {
    if (!checkCallingSendSmsPermission(paramString1, "Sending SMS message")) {
      return;
    }
    ContentResolver localContentResolver = mContext.getContentResolver();
    if (!isFailedOrDraft(localContentResolver, paramUri))
    {
      Log.e("IccSmsInterfaceManager", "[IccSmsInterfaceManager]sendStoredMultipartText: not FAILED or DRAFT message");
      returnUnspecifiedFailure(paramList1);
      return;
    }
    String[] arrayOfString = loadTextAndAddress(localContentResolver, paramUri);
    if (arrayOfString == null)
    {
      Log.e("IccSmsInterfaceManager", "[IccSmsInterfaceManager]sendStoredMultipartText: can not load text");
      returnUnspecifiedFailure(paramList1);
      return;
    }
    Object localObject1 = SmsManager.getDefault();
    int i = 0;
    localObject1 = ((SmsManager)localObject1).divideMessage(arrayOfString[0]);
    if (localObject1 != null)
    {
      int j = ((ArrayList)localObject1).size();
      int k = 1;
      if (j >= 1)
      {
        arrayOfString[1] = filterDestAddress(arrayOfString[1]);
        if ((((ArrayList)localObject1).size() > 1) && (((ArrayList)localObject1).size() < 10) && (!SmsMessage.hasEmsSupport()))
        {
          while (i < ((ArrayList)localObject1).size())
          {
            String str = (String)((ArrayList)localObject1).get(i);
            if (SmsMessage.shouldAppendPageNumberAsPrefix())
            {
              localObject2 = new StringBuilder();
              ((StringBuilder)localObject2).append(String.valueOf(i + 1));
              ((StringBuilder)localObject2).append('/');
              ((StringBuilder)localObject2).append(((ArrayList)localObject1).size());
              ((StringBuilder)localObject2).append(' ');
              ((StringBuilder)localObject2).append(str);
            }
            for (str = ((StringBuilder)localObject2).toString();; str = str.concat(((StringBuilder)localObject2).toString()))
            {
              break;
              localObject2 = new StringBuilder();
              ((StringBuilder)localObject2).append(' ');
              ((StringBuilder)localObject2).append(String.valueOf(i + 1));
              ((StringBuilder)localObject2).append('/');
              ((StringBuilder)localObject2).append(((ArrayList)localObject1).size());
            }
            Object localObject3 = null;
            Object localObject2 = localObject3;
            if (paramList1 != null)
            {
              localObject2 = localObject3;
              if (paramList1.size() > i) {
                localObject2 = (PendingIntent)paramList1.get(i);
              }
            }
            Object localObject4 = null;
            localObject3 = localObject4;
            if (paramList2 != null)
            {
              localObject3 = localObject4;
              if (paramList2.size() > i) {
                localObject3 = (PendingIntent)paramList2.get(i);
              }
            }
            mDispatchersController.sendText(arrayOfString[k], paramString2, str, (PendingIntent)localObject2, (PendingIntent)localObject3, paramUri, paramString1, true, -1, false, -1);
            i++;
          }
          return;
        }
        mDispatchersController.sendMultipartText(arrayOfString[1], paramString2, (ArrayList)localObject1, (ArrayList)paramList1, (ArrayList)paramList2, paramUri, paramString1, true, -1, false, -1);
        return;
      }
    }
    Log.e("IccSmsInterfaceManager", "[IccSmsInterfaceManager]sendStoredMultipartText: can not divide text");
    returnUnspecifiedFailure(paramList1);
  }
  
  public void sendStoredText(String paramString1, Uri paramUri, String paramString2, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2)
  {
    if (!checkCallingSendSmsPermission(paramString1, "Sending SMS message")) {
      return;
    }
    if (Rlog.isLoggable("SMS", 2))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("sendStoredText: scAddr=");
      ((StringBuilder)localObject).append(paramString2);
      ((StringBuilder)localObject).append(" messageUri=");
      ((StringBuilder)localObject).append(paramUri);
      ((StringBuilder)localObject).append(" sentIntent=");
      ((StringBuilder)localObject).append(paramPendingIntent1);
      ((StringBuilder)localObject).append(" deliveryIntent=");
      ((StringBuilder)localObject).append(paramPendingIntent2);
      log(((StringBuilder)localObject).toString());
    }
    Object localObject = mContext.getContentResolver();
    if (!isFailedOrDraft((ContentResolver)localObject, paramUri))
    {
      Log.e("IccSmsInterfaceManager", "[IccSmsInterfaceManager]sendStoredText: not FAILED or DRAFT message");
      returnUnspecifiedFailure(paramPendingIntent1);
      return;
    }
    localObject = loadTextAndAddress((ContentResolver)localObject, paramUri);
    if (localObject == null)
    {
      Log.e("IccSmsInterfaceManager", "[IccSmsInterfaceManager]sendStoredText: can not load text");
      returnUnspecifiedFailure(paramPendingIntent1);
      return;
    }
    localObject[1] = filterDestAddress(localObject[1]);
    mDispatchersController.sendText(localObject[1], paramString2, localObject[0], paramPendingIntent1, paramPendingIntent2, paramUri, paramString1, true, -1, false, -1);
  }
  
  public void sendText(String paramString1, String paramString2, String paramString3, String paramString4, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, boolean paramBoolean)
  {
    if (!checkCallingSendTextPermissions(paramBoolean, paramString1, "Sending SMS message")) {
      return;
    }
    sendTextInternal(paramString1, paramString2, paramString3, paramString4, paramPendingIntent1, paramPendingIntent2, paramBoolean, -1, false, -1);
  }
  
  public void sendTextWithOptions(String paramString1, String paramString2, String paramString3, String paramString4, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, boolean paramBoolean1, int paramInt1, boolean paramBoolean2, int paramInt2)
  {
    if (!checkCallingOrSelfSendSmsPermission(paramString1, "Sending SMS message")) {
      return;
    }
    sendTextInternal(paramString1, paramString2, paramString3, paramString4, paramPendingIntent1, paramPendingIntent2, paramBoolean1, paramInt1, paramBoolean2, paramInt2);
  }
  
  public void sendTextWithSelfPermissions(String paramString1, String paramString2, String paramString3, String paramString4, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, boolean paramBoolean)
  {
    if (!checkCallingOrSelfSendSmsPermission(paramString1, "Sending SMS message")) {
      return;
    }
    sendTextInternal(paramString1, paramString2, paramString3, paramString4, paramPendingIntent1, paramPendingIntent2, paramBoolean, -1, false, -1);
  }
  
  public void setPremiumSmsPermission(String paramString, int paramInt)
  {
    mDispatchersController.setPremiumSmsPermission(paramString, paramInt);
  }
  
  public boolean updateMessageOnIccEf(String arg1, int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("updateMessageOnIccEf: index=");
    ((StringBuilder)localObject).append(paramInt1);
    ((StringBuilder)localObject).append(" status=");
    ((StringBuilder)localObject).append(paramInt2);
    ((StringBuilder)localObject).append(" ==> (");
    ((StringBuilder)localObject).append(Arrays.toString(paramArrayOfByte));
    ((StringBuilder)localObject).append(")");
    log(((StringBuilder)localObject).toString());
    enforceReceiveAndSend("Updating message on Icc");
    if (mAppOps.noteOp(22, Binder.getCallingUid(), ???) != 0) {
      return false;
    }
    synchronized (mLock)
    {
      mSuccess = false;
      localObject = mHandler.obtainMessage(2);
      if (paramInt2 == 0)
      {
        if (1 == mPhone.getPhoneType()) {
          mPhone.mCi.deleteSmsOnSim(paramInt1, (Message)localObject);
        } else {
          mPhone.mCi.deleteSmsOnRuim(paramInt1, (Message)localObject);
        }
      }
      else
      {
        IccFileHandler localIccFileHandler = mPhone.getIccFileHandler();
        if (localIccFileHandler == null)
        {
          ((Message)localObject).recycle();
          boolean bool = mSuccess;
          return bool;
        }
        localIccFileHandler.updateEFLinearFixed(28476, paramInt1, makeSmsRecordData(paramInt2, paramArrayOfByte), null, (Message)localObject);
      }
      try
      {
        mLock.wait();
      }
      catch (InterruptedException paramArrayOfByte)
      {
        log("interrupted while trying to update by index");
      }
      return mSuccess;
    }
  }
  
  protected void updatePhoneObject(Phone paramPhone)
  {
    mPhone = paramPhone;
    mDispatchersController.updatePhoneObject(paramPhone);
  }
  
  class CdmaBroadcastRangeManager
    extends IntRangeManager
  {
    private ArrayList<CdmaSmsBroadcastConfigInfo> mConfigList = new ArrayList();
    
    CdmaBroadcastRangeManager() {}
    
    protected void addRange(int paramInt1, int paramInt2, boolean paramBoolean)
    {
      mConfigList.add(new CdmaSmsBroadcastConfigInfo(paramInt1, paramInt2, 1, paramBoolean));
    }
    
    protected boolean finishUpdate()
    {
      if (mConfigList.isEmpty()) {
        return true;
      }
      CdmaSmsBroadcastConfigInfo[] arrayOfCdmaSmsBroadcastConfigInfo = (CdmaSmsBroadcastConfigInfo[])mConfigList.toArray(new CdmaSmsBroadcastConfigInfo[mConfigList.size()]);
      return IccSmsInterfaceManager.this.setCdmaBroadcastConfig(arrayOfCdmaSmsBroadcastConfigInfo);
    }
    
    protected void startUpdate()
    {
      mConfigList.clear();
    }
  }
  
  class CellBroadcastRangeManager
    extends IntRangeManager
  {
    private ArrayList<SmsBroadcastConfigInfo> mConfigList = new ArrayList();
    
    CellBroadcastRangeManager() {}
    
    protected void addRange(int paramInt1, int paramInt2, boolean paramBoolean)
    {
      mConfigList.add(new SmsBroadcastConfigInfo(paramInt1, paramInt2, 0, 255, paramBoolean));
    }
    
    protected boolean finishUpdate()
    {
      if (mConfigList.isEmpty()) {
        return true;
      }
      SmsBroadcastConfigInfo[] arrayOfSmsBroadcastConfigInfo = (SmsBroadcastConfigInfo[])mConfigList.toArray(new SmsBroadcastConfigInfo[mConfigList.size()]);
      return IccSmsInterfaceManager.this.setCellBroadcastConfig(arrayOfSmsBroadcastConfigInfo);
    }
    
    protected void startUpdate()
    {
      mConfigList.clear();
    }
  }
}
