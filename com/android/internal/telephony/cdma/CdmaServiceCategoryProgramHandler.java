package com.android.internal.telephony.cdma;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Message;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.WakeLockStateMachine;
import java.util.ArrayList;

public final class CdmaServiceCategoryProgramHandler
  extends WakeLockStateMachine
{
  final CommandsInterface mCi;
  private final BroadcastReceiver mScpResultsReceiver = new BroadcastReceiver()
  {
    /* Error */
    private void sendScpResults()
    {
      // Byte code:
      //   0: aload_0
      //   1: invokevirtual 23	com/android/internal/telephony/cdma/CdmaServiceCategoryProgramHandler$1:getResultCode	()I
      //   4: istore_1
      //   5: iload_1
      //   6: iconst_m1
      //   7: if_icmpeq +43 -> 50
      //   10: iload_1
      //   11: iconst_1
      //   12: if_icmpeq +38 -> 50
      //   15: aload_0
      //   16: getfield 12	com/android/internal/telephony/cdma/CdmaServiceCategoryProgramHandler$1:this$0	Lcom/android/internal/telephony/cdma/CdmaServiceCategoryProgramHandler;
      //   19: astore_2
      //   20: new 25	java/lang/StringBuilder
      //   23: dup
      //   24: invokespecial 26	java/lang/StringBuilder:<init>	()V
      //   27: astore_3
      //   28: aload_3
      //   29: ldc 28
      //   31: invokevirtual 32	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   34: pop
      //   35: aload_3
      //   36: iload_1
      //   37: invokevirtual 35	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   40: pop
      //   41: aload_2
      //   42: aload_3
      //   43: invokevirtual 39	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   46: invokestatic 43	com/android/internal/telephony/cdma/CdmaServiceCategoryProgramHandler:access$100	(Lcom/android/internal/telephony/cdma/CdmaServiceCategoryProgramHandler;Ljava/lang/String;)V
      //   49: return
      //   50: aload_0
      //   51: iconst_0
      //   52: invokevirtual 47	com/android/internal/telephony/cdma/CdmaServiceCategoryProgramHandler$1:getResultExtras	(Z)Landroid/os/Bundle;
      //   55: astore_2
      //   56: aload_2
      //   57: ifnonnull +13 -> 70
      //   60: aload_0
      //   61: getfield 12	com/android/internal/telephony/cdma/CdmaServiceCategoryProgramHandler$1:this$0	Lcom/android/internal/telephony/cdma/CdmaServiceCategoryProgramHandler;
      //   64: ldc 49
      //   66: invokestatic 52	com/android/internal/telephony/cdma/CdmaServiceCategoryProgramHandler:access$200	(Lcom/android/internal/telephony/cdma/CdmaServiceCategoryProgramHandler;Ljava/lang/String;)V
      //   69: return
      //   70: aload_2
      //   71: ldc 54
      //   73: invokevirtual 60	android/os/Bundle:getString	(Ljava/lang/String;)Ljava/lang/String;
      //   76: astore_3
      //   77: aload_3
      //   78: ifnonnull +13 -> 91
      //   81: aload_0
      //   82: getfield 12	com/android/internal/telephony/cdma/CdmaServiceCategoryProgramHandler$1:this$0	Lcom/android/internal/telephony/cdma/CdmaServiceCategoryProgramHandler;
      //   85: ldc 62
      //   87: invokestatic 65	com/android/internal/telephony/cdma/CdmaServiceCategoryProgramHandler:access$300	(Lcom/android/internal/telephony/cdma/CdmaServiceCategoryProgramHandler;Ljava/lang/String;)V
      //   90: return
      //   91: aload_2
      //   92: ldc 67
      //   94: invokevirtual 71	android/os/Bundle:getParcelableArrayList	(Ljava/lang/String;)Ljava/util/ArrayList;
      //   97: astore 4
      //   99: aload 4
      //   101: ifnonnull +13 -> 114
      //   104: aload_0
      //   105: getfield 12	com/android/internal/telephony/cdma/CdmaServiceCategoryProgramHandler$1:this$0	Lcom/android/internal/telephony/cdma/CdmaServiceCategoryProgramHandler;
      //   108: ldc 73
      //   110: invokestatic 76	com/android/internal/telephony/cdma/CdmaServiceCategoryProgramHandler:access$400	(Lcom/android/internal/telephony/cdma/CdmaServiceCategoryProgramHandler;Ljava/lang/String;)V
      //   113: return
      //   114: new 78	com/android/internal/telephony/cdma/sms/BearerData
      //   117: dup
      //   118: invokespecial 79	com/android/internal/telephony/cdma/sms/BearerData:<init>	()V
      //   121: astore_2
      //   122: aload_2
      //   123: iconst_2
      //   124: putfield 83	com/android/internal/telephony/cdma/sms/BearerData:messageType	I
      //   127: aload_2
      //   128: invokestatic 88	com/android/internal/telephony/cdma/SmsMessage:getNextMessageId	()I
      //   131: putfield 91	com/android/internal/telephony/cdma/sms/BearerData:messageId	I
      //   134: aload_2
      //   135: aload 4
      //   137: putfield 95	com/android/internal/telephony/cdma/sms/BearerData:serviceCategoryProgramResults	Ljava/util/ArrayList;
      //   140: aload_2
      //   141: invokestatic 99	com/android/internal/telephony/cdma/sms/BearerData:encode	(Lcom/android/internal/telephony/cdma/sms/BearerData;)[B
      //   144: astore 4
      //   146: new 101	java/io/ByteArrayOutputStream
      //   149: dup
      //   150: bipush 100
      //   152: invokespecial 104	java/io/ByteArrayOutputStream:<init>	(I)V
      //   155: astore 5
      //   157: new 106	java/io/DataOutputStream
      //   160: dup
      //   161: aload 5
      //   163: invokespecial 109	java/io/DataOutputStream:<init>	(Ljava/io/OutputStream;)V
      //   166: astore_2
      //   167: aload_2
      //   168: sipush 4102
      //   171: invokevirtual 112	java/io/DataOutputStream:writeInt	(I)V
      //   174: aload_2
      //   175: iconst_0
      //   176: invokevirtual 112	java/io/DataOutputStream:writeInt	(I)V
      //   179: aload_2
      //   180: iconst_0
      //   181: invokevirtual 112	java/io/DataOutputStream:writeInt	(I)V
      //   184: aload_3
      //   185: invokestatic 117	android/telephony/PhoneNumberUtils:cdmaCheckAndProcessPlusCodeForSms	(Ljava/lang/String;)Ljava/lang/String;
      //   188: invokestatic 123	com/android/internal/telephony/cdma/sms/CdmaSmsAddress:parse	(Ljava/lang/String;)Lcom/android/internal/telephony/cdma/sms/CdmaSmsAddress;
      //   191: astore_3
      //   192: aload_2
      //   193: aload_3
      //   194: getfield 126	com/android/internal/telephony/cdma/sms/CdmaSmsAddress:digitMode	I
      //   197: invokevirtual 129	java/io/DataOutputStream:write	(I)V
      //   200: aload_2
      //   201: aload_3
      //   202: getfield 132	com/android/internal/telephony/cdma/sms/CdmaSmsAddress:numberMode	I
      //   205: invokevirtual 129	java/io/DataOutputStream:write	(I)V
      //   208: aload_2
      //   209: aload_3
      //   210: getfield 135	com/android/internal/telephony/cdma/sms/CdmaSmsAddress:ton	I
      //   213: invokevirtual 129	java/io/DataOutputStream:write	(I)V
      //   216: aload_2
      //   217: aload_3
      //   218: getfield 138	com/android/internal/telephony/cdma/sms/CdmaSmsAddress:numberPlan	I
      //   221: invokevirtual 129	java/io/DataOutputStream:write	(I)V
      //   224: aload_2
      //   225: aload_3
      //   226: getfield 141	com/android/internal/telephony/cdma/sms/CdmaSmsAddress:numberOfDigits	I
      //   229: invokevirtual 129	java/io/DataOutputStream:write	(I)V
      //   232: aload_2
      //   233: aload_3
      //   234: getfield 145	com/android/internal/telephony/cdma/sms/CdmaSmsAddress:origBytes	[B
      //   237: iconst_0
      //   238: aload_3
      //   239: getfield 145	com/android/internal/telephony/cdma/sms/CdmaSmsAddress:origBytes	[B
      //   242: arraylength
      //   243: invokevirtual 148	java/io/DataOutputStream:write	([BII)V
      //   246: aload_2
      //   247: iconst_0
      //   248: invokevirtual 129	java/io/DataOutputStream:write	(I)V
      //   251: aload_2
      //   252: iconst_0
      //   253: invokevirtual 129	java/io/DataOutputStream:write	(I)V
      //   256: aload_2
      //   257: iconst_0
      //   258: invokevirtual 129	java/io/DataOutputStream:write	(I)V
      //   261: aload_2
      //   262: aload 4
      //   264: arraylength
      //   265: invokevirtual 129	java/io/DataOutputStream:write	(I)V
      //   268: aload_2
      //   269: aload 4
      //   271: iconst_0
      //   272: aload 4
      //   274: arraylength
      //   275: invokevirtual 148	java/io/DataOutputStream:write	([BII)V
      //   278: aload_0
      //   279: getfield 12	com/android/internal/telephony/cdma/CdmaServiceCategoryProgramHandler$1:this$0	Lcom/android/internal/telephony/cdma/CdmaServiceCategoryProgramHandler;
      //   282: getfield 152	com/android/internal/telephony/cdma/CdmaServiceCategoryProgramHandler:mCi	Lcom/android/internal/telephony/CommandsInterface;
      //   285: aload 5
      //   287: invokevirtual 156	java/io/ByteArrayOutputStream:toByteArray	()[B
      //   290: aconst_null
      //   291: invokeinterface 162 3 0
      //   296: aload_2
      //   297: invokevirtual 165	java/io/DataOutputStream:close	()V
      //   300: goto +22 -> 322
      //   303: astore_3
      //   304: goto +23 -> 327
      //   307: astore_3
      //   308: aload_0
      //   309: getfield 12	com/android/internal/telephony/cdma/CdmaServiceCategoryProgramHandler$1:this$0	Lcom/android/internal/telephony/cdma/CdmaServiceCategoryProgramHandler;
      //   312: ldc -89
      //   314: aload_3
      //   315: invokestatic 171	com/android/internal/telephony/cdma/CdmaServiceCategoryProgramHandler:access$500	(Lcom/android/internal/telephony/cdma/CdmaServiceCategoryProgramHandler;Ljava/lang/String;Ljava/lang/Throwable;)V
      //   318: aload_2
      //   319: invokevirtual 165	java/io/DataOutputStream:close	()V
      //   322: goto +4 -> 326
      //   325: astore_3
      //   326: return
      //   327: aload_2
      //   328: invokevirtual 165	java/io/DataOutputStream:close	()V
      //   331: goto +4 -> 335
      //   334: astore_2
      //   335: aload_3
      //   336: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	337	0	this	1
      //   4	33	1	i	int
      //   19	309	2	localObject1	Object
      //   334	1	2	localIOException1	java.io.IOException
      //   27	212	3	localObject2	Object
      //   303	1	3	localObject3	Object
      //   307	8	3	localIOException2	java.io.IOException
      //   325	11	3	localIOException3	java.io.IOException
      //   97	176	4	localObject4	Object
      //   155	131	5	localByteArrayOutputStream	java.io.ByteArrayOutputStream
      // Exception table:
      //   from	to	target	type
      //   167	184	303	finally
      //   184	296	303	finally
      //   308	318	303	finally
      //   167	184	307	java/io/IOException
      //   184	296	307	java/io/IOException
      //   296	300	325	java/io/IOException
      //   318	322	325	java/io/IOException
      //   327	331	334	java/io/IOException
    }
    
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      sendScpResults();
      log("mScpResultsReceiver finished");
      sendMessage(2);
    }
  };
  
  CdmaServiceCategoryProgramHandler(Context paramContext, CommandsInterface paramCommandsInterface)
  {
    super("CdmaServiceCategoryProgramHandler", paramContext, null);
    mContext = paramContext;
    mCi = paramCommandsInterface;
  }
  
  private boolean handleServiceCategoryProgramData(SmsMessage paramSmsMessage)
  {
    ArrayList localArrayList = paramSmsMessage.getSmsCbProgramData();
    if (localArrayList == null)
    {
      loge("handleServiceCategoryProgramData: program data list is null!");
      return false;
    }
    Intent localIntent = new Intent("android.provider.Telephony.SMS_SERVICE_CATEGORY_PROGRAM_DATA_RECEIVED");
    localIntent.setPackage(mContext.getResources().getString(17039688));
    localIntent.putExtra("sender", paramSmsMessage.getOriginatingAddress());
    localIntent.putParcelableArrayListExtra("program_data", localArrayList);
    SubscriptionManager.putPhoneIdAndSubIdExtra(localIntent, mPhone.getPhoneId());
    mContext.sendOrderedBroadcast(localIntent, "android.permission.RECEIVE_SMS", 16, mScpResultsReceiver, getHandler(), -1, null, null);
    return true;
  }
  
  static CdmaServiceCategoryProgramHandler makeScpHandler(Context paramContext, CommandsInterface paramCommandsInterface)
  {
    paramContext = new CdmaServiceCategoryProgramHandler(paramContext, paramCommandsInterface);
    paramContext.start();
    return paramContext;
  }
  
  protected boolean handleSmsMessage(Message paramMessage)
  {
    if ((obj instanceof SmsMessage)) {
      return handleServiceCategoryProgramData((SmsMessage)obj);
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("handleMessage got object of type: ");
    localStringBuilder.append(obj.getClass().getName());
    loge(localStringBuilder.toString());
    return false;
  }
}
