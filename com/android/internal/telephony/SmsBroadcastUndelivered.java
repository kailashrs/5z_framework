package com.android.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.os.UserManager;
import android.telephony.CarrierConfigManager;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.cdma.CdmaInboundSmsHandler;
import com.android.internal.telephony.gsm.GsmInboundSmsHandler;

public class SmsBroadcastUndelivered
{
  private static final boolean DBG = true;
  static final long DEFAULT_PARTIAL_SEGMENT_EXPIRE_AGE = 2592000000L;
  private static final String[] PDU_PENDING_MESSAGE_PROJECTION = { "pdu", "sequence", "destination_port", "date", "reference_number", "count", "address", "_id", "message_body", "display_originating_addr" };
  private static final String TAG = "SmsBroadcastUndelivered";
  private static SmsBroadcastUndelivered instance;
  private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Received broadcast ");
      localStringBuilder.append(paramAnonymousIntent.getAction());
      Rlog.d("SmsBroadcastUndelivered", localStringBuilder.toString());
      if ("android.intent.action.USER_UNLOCKED".equals(paramAnonymousIntent.getAction())) {
        new SmsBroadcastUndelivered.ScanRawTableThread(SmsBroadcastUndelivered.this, paramAnonymousContext, null).start();
      }
    }
  };
  private final CdmaInboundSmsHandler mCdmaInboundSmsHandler;
  private final GsmInboundSmsHandler mGsmInboundSmsHandler;
  private final ContentResolver mResolver;
  
  private SmsBroadcastUndelivered(Context paramContext, GsmInboundSmsHandler paramGsmInboundSmsHandler, CdmaInboundSmsHandler paramCdmaInboundSmsHandler)
  {
    mResolver = paramContext.getContentResolver();
    mGsmInboundSmsHandler = paramGsmInboundSmsHandler;
    mCdmaInboundSmsHandler = paramCdmaInboundSmsHandler;
    if (((UserManager)paramContext.getSystemService("user")).isUserUnlocked())
    {
      new ScanRawTableThread(paramContext, null).start();
    }
    else
    {
      paramGsmInboundSmsHandler = new IntentFilter();
      paramGsmInboundSmsHandler.addAction("android.intent.action.USER_UNLOCKED");
      paramContext.registerReceiver(mBroadcastReceiver, paramGsmInboundSmsHandler);
    }
  }
  
  private void broadcastSms(InboundSmsTracker paramInboundSmsTracker)
  {
    Object localObject;
    if (paramInboundSmsTracker.is3gpp2()) {
      localObject = mCdmaInboundSmsHandler;
    } else {
      localObject = mGsmInboundSmsHandler;
    }
    if (localObject != null)
    {
      ((InboundSmsHandler)localObject).sendMessage(2, paramInboundSmsTracker);
    }
    else
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("null handler for ");
      ((StringBuilder)localObject).append(paramInboundSmsTracker.getFormat());
      ((StringBuilder)localObject).append(" format, can't deliver.");
      Rlog.e("SmsBroadcastUndelivered", ((StringBuilder)localObject).toString());
    }
  }
  
  private long getUndeliveredSmsExpirationTime(Context paramContext)
  {
    int i = SubscriptionManager.getDefaultSmsSubscriptionId();
    paramContext = ((CarrierConfigManager)paramContext.getSystemService("carrier_config")).getConfigForSubId(i);
    if (paramContext != null) {
      return paramContext.getLong("undelivered_sms_message_expiration_time", 2592000000L);
    }
    return 2592000000L;
  }
  
  public static void initialize(Context paramContext, GsmInboundSmsHandler paramGsmInboundSmsHandler, CdmaInboundSmsHandler paramCdmaInboundSmsHandler)
  {
    if (instance == null) {
      instance = new SmsBroadcastUndelivered(paramContext, paramGsmInboundSmsHandler, paramCdmaInboundSmsHandler);
    }
    if (paramGsmInboundSmsHandler != null) {
      paramGsmInboundSmsHandler.sendMessage(6);
    }
    if (paramCdmaInboundSmsHandler != null) {
      paramCdmaInboundSmsHandler.sendMessage(6);
    }
  }
  
  /* Error */
  private void scanRawTable(Context paramContext)
  {
    // Byte code:
    //   0: ldc 25
    //   2: ldc -56
    //   4: invokestatic 203	android/telephony/Rlog:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   7: pop
    //   8: invokestatic 209	java/lang/System:nanoTime	()J
    //   11: lstore_2
    //   12: new 211	java/util/HashMap
    //   15: dup
    //   16: iconst_4
    //   17: invokespecial 213	java/util/HashMap:<init>	(I)V
    //   20: astore 4
    //   22: new 215	java/util/HashSet
    //   25: dup
    //   26: iconst_4
    //   27: invokespecial 216	java/util/HashSet:<init>	(I)V
    //   30: astore 5
    //   32: aconst_null
    //   33: astore 6
    //   35: aconst_null
    //   36: astore 7
    //   38: aload_0
    //   39: getfield 79	com/android/internal/telephony/SmsBroadcastUndelivered:mResolver	Landroid/content/ContentResolver;
    //   42: getstatic 220	com/android/internal/telephony/InboundSmsHandler:sRawUri	Landroid/net/Uri;
    //   45: getstatic 61	com/android/internal/telephony/SmsBroadcastUndelivered:PDU_PENDING_MESSAGE_PROJECTION	[Ljava/lang/String;
    //   48: ldc -34
    //   50: aconst_null
    //   51: aconst_null
    //   52: invokevirtual 228	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   55: astore 8
    //   57: aload 8
    //   59: ifnonnull +78 -> 137
    //   62: aload 8
    //   64: astore 7
    //   66: aload 8
    //   68: astore 6
    //   70: ldc 25
    //   72: ldc -26
    //   74: invokestatic 157	android/telephony/Rlog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   77: pop
    //   78: aload 8
    //   80: ifnull +10 -> 90
    //   83: aload 8
    //   85: invokeinterface 235 1 0
    //   90: new 135	java/lang/StringBuilder
    //   93: dup
    //   94: invokespecial 136	java/lang/StringBuilder:<init>	()V
    //   97: astore_1
    //   98: aload_1
    //   99: ldc -19
    //   101: invokevirtual 142	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   104: pop
    //   105: aload_1
    //   106: invokestatic 209	java/lang/System:nanoTime	()J
    //   109: lload_2
    //   110: lsub
    //   111: ldc2_w 238
    //   114: ldiv
    //   115: invokevirtual 242	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   118: pop
    //   119: aload_1
    //   120: ldc -12
    //   122: invokevirtual 142	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   125: pop
    //   126: ldc 25
    //   128: aload_1
    //   129: invokevirtual 151	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   132: invokestatic 203	android/telephony/Rlog:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   135: pop
    //   136: return
    //   137: aload 8
    //   139: astore 7
    //   141: aload 8
    //   143: astore 6
    //   145: invokestatic 247	com/android/internal/telephony/InboundSmsHandler:isCurrentFormat3gpp2	()Z
    //   148: istore 9
    //   150: aload 8
    //   152: astore 7
    //   154: aload 8
    //   156: astore 6
    //   158: aload 8
    //   160: invokeinterface 250 1 0
    //   165: istore 10
    //   167: iload 10
    //   169: ifeq +379 -> 548
    //   172: aload 8
    //   174: astore 7
    //   176: aload 8
    //   178: astore 6
    //   180: invokestatic 256	com/android/internal/telephony/TelephonyComponentFactory:getInstance	()Lcom/android/internal/telephony/TelephonyComponentFactory;
    //   183: aload 8
    //   185: iload 9
    //   187: invokevirtual 260	com/android/internal/telephony/TelephonyComponentFactory:makeInboundSmsTracker	(Landroid/database/Cursor;Z)Lcom/android/internal/telephony/InboundSmsTracker;
    //   190: astore 11
    //   192: aload 8
    //   194: astore 7
    //   196: aload 8
    //   198: astore 6
    //   200: aload 11
    //   202: invokevirtual 263	com/android/internal/telephony/InboundSmsTracker:getMessageCount	()I
    //   205: iconst_1
    //   206: if_icmpne +20 -> 226
    //   209: aload 8
    //   211: astore 7
    //   213: aload 8
    //   215: astore 6
    //   217: aload_0
    //   218: aload 11
    //   220: invokespecial 265	com/android/internal/telephony/SmsBroadcastUndelivered:broadcastSms	(Lcom/android/internal/telephony/InboundSmsTracker;)V
    //   223: goto +239 -> 462
    //   226: aload 8
    //   228: astore 7
    //   230: aload 8
    //   232: astore 6
    //   234: new 11	com/android/internal/telephony/SmsBroadcastUndelivered$SmsReferenceKey
    //   237: astore 12
    //   239: aload 8
    //   241: astore 7
    //   243: aload 8
    //   245: astore 6
    //   247: aload 12
    //   249: aload 11
    //   251: invokespecial 267	com/android/internal/telephony/SmsBroadcastUndelivered$SmsReferenceKey:<init>	(Lcom/android/internal/telephony/InboundSmsTracker;)V
    //   254: aload 8
    //   256: astore 7
    //   258: aload 8
    //   260: astore 6
    //   262: aload 4
    //   264: aload 12
    //   266: invokevirtual 271	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   269: checkcast 273	java/lang/Integer
    //   272: astore 13
    //   274: aload 13
    //   276: ifnonnull +80 -> 356
    //   279: aload 8
    //   281: astore 7
    //   283: aload 8
    //   285: astore 6
    //   287: aload 4
    //   289: aload 12
    //   291: iconst_1
    //   292: invokestatic 277	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   295: invokevirtual 281	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   298: pop
    //   299: aload 8
    //   301: astore 7
    //   303: aload 8
    //   305: astore 6
    //   307: aload_0
    //   308: aload_1
    //   309: invokespecial 283	com/android/internal/telephony/SmsBroadcastUndelivered:getUndeliveredSmsExpirationTime	(Landroid/content/Context;)J
    //   312: lstore 14
    //   314: aload 8
    //   316: astore 7
    //   318: aload 8
    //   320: astore 6
    //   322: aload 11
    //   324: invokevirtual 286	com/android/internal/telephony/InboundSmsTracker:getTimestamp	()J
    //   327: invokestatic 289	java/lang/System:currentTimeMillis	()J
    //   330: lload 14
    //   332: lsub
    //   333: lcmp
    //   334: ifge +19 -> 353
    //   337: aload 8
    //   339: astore 7
    //   341: aload 8
    //   343: astore 6
    //   345: aload 5
    //   347: aload 12
    //   349: invokevirtual 293	java/util/HashSet:add	(Ljava/lang/Object;)Z
    //   352: pop
    //   353: goto +109 -> 462
    //   356: aload 8
    //   358: astore 7
    //   360: aload 8
    //   362: astore 6
    //   364: aload 13
    //   366: invokevirtual 296	java/lang/Integer:intValue	()I
    //   369: iconst_1
    //   370: iadd
    //   371: istore 16
    //   373: aload 8
    //   375: astore 7
    //   377: aload 8
    //   379: astore 6
    //   381: iload 16
    //   383: aload 11
    //   385: invokevirtual 263	com/android/internal/telephony/InboundSmsTracker:getMessageCount	()I
    //   388: if_icmpne +53 -> 441
    //   391: aload 8
    //   393: astore 7
    //   395: aload 8
    //   397: astore 6
    //   399: ldc 25
    //   401: ldc_w 298
    //   404: invokestatic 203	android/telephony/Rlog:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   407: pop
    //   408: aload 8
    //   410: astore 7
    //   412: aload 8
    //   414: astore 6
    //   416: aload_0
    //   417: aload 11
    //   419: invokespecial 265	com/android/internal/telephony/SmsBroadcastUndelivered:broadcastSms	(Lcom/android/internal/telephony/InboundSmsTracker;)V
    //   422: aload 8
    //   424: astore 7
    //   426: aload 8
    //   428: astore 6
    //   430: aload 5
    //   432: aload 12
    //   434: invokevirtual 301	java/util/HashSet:remove	(Ljava/lang/Object;)Z
    //   437: pop
    //   438: goto +24 -> 462
    //   441: aload 8
    //   443: astore 7
    //   445: aload 8
    //   447: astore 6
    //   449: aload 4
    //   451: aload 12
    //   453: iload 16
    //   455: invokestatic 277	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   458: invokevirtual 281	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   461: pop
    //   462: goto +83 -> 545
    //   465: astore 13
    //   467: aload 8
    //   469: astore 7
    //   471: aload 8
    //   473: astore 6
    //   475: new 135	java/lang/StringBuilder
    //   478: astore 12
    //   480: aload 8
    //   482: astore 7
    //   484: aload 8
    //   486: astore 6
    //   488: aload 12
    //   490: invokespecial 136	java/lang/StringBuilder:<init>	()V
    //   493: aload 8
    //   495: astore 7
    //   497: aload 8
    //   499: astore 6
    //   501: aload 12
    //   503: ldc_w 303
    //   506: invokevirtual 142	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   509: pop
    //   510: aload 8
    //   512: astore 7
    //   514: aload 8
    //   516: astore 6
    //   518: aload 12
    //   520: aload 13
    //   522: invokevirtual 306	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   525: pop
    //   526: aload 8
    //   528: astore 7
    //   530: aload 8
    //   532: astore 6
    //   534: ldc 25
    //   536: aload 12
    //   538: invokevirtual 151	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   541: invokestatic 157	android/telephony/Rlog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   544: pop
    //   545: goto -395 -> 150
    //   548: aload 8
    //   550: astore 7
    //   552: aload 8
    //   554: astore 6
    //   556: aload 5
    //   558: invokevirtual 310	java/util/HashSet:iterator	()Ljava/util/Iterator;
    //   561: astore 5
    //   563: aload 8
    //   565: astore 7
    //   567: aload 8
    //   569: astore 6
    //   571: aload 5
    //   573: invokeinterface 315 1 0
    //   578: ifeq +204 -> 782
    //   581: aload 8
    //   583: astore 7
    //   585: aload 8
    //   587: astore 6
    //   589: aload 5
    //   591: invokeinterface 319 1 0
    //   596: checkcast 11	com/android/internal/telephony/SmsBroadcastUndelivered$SmsReferenceKey
    //   599: astore 4
    //   601: aload 8
    //   603: astore 7
    //   605: aload 8
    //   607: astore 6
    //   609: aload_0
    //   610: getfield 79	com/android/internal/telephony/SmsBroadcastUndelivered:mResolver	Landroid/content/ContentResolver;
    //   613: getstatic 322	com/android/internal/telephony/InboundSmsHandler:sRawUriPermanentDelete	Landroid/net/Uri;
    //   616: aload 4
    //   618: invokevirtual 325	com/android/internal/telephony/SmsBroadcastUndelivered$SmsReferenceKey:getDeleteWhere	()Ljava/lang/String;
    //   621: aload 4
    //   623: invokevirtual 329	com/android/internal/telephony/SmsBroadcastUndelivered$SmsReferenceKey:getDeleteWhereArgs	()[Ljava/lang/String;
    //   626: invokevirtual 333	android/content/ContentResolver:delete	(Landroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)I
    //   629: istore 16
    //   631: iload 16
    //   633: ifne +23 -> 656
    //   636: aload 8
    //   638: astore 7
    //   640: aload 8
    //   642: astore 6
    //   644: ldc 25
    //   646: ldc_w 335
    //   649: invokestatic 157	android/telephony/Rlog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   652: pop
    //   653: goto +126 -> 779
    //   656: aload 8
    //   658: astore 7
    //   660: aload 8
    //   662: astore 6
    //   664: new 135	java/lang/StringBuilder
    //   667: astore_1
    //   668: aload 8
    //   670: astore 7
    //   672: aload 8
    //   674: astore 6
    //   676: aload_1
    //   677: invokespecial 136	java/lang/StringBuilder:<init>	()V
    //   680: aload 8
    //   682: astore 7
    //   684: aload 8
    //   686: astore 6
    //   688: aload_1
    //   689: ldc_w 337
    //   692: invokevirtual 142	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   695: pop
    //   696: aload 8
    //   698: astore 7
    //   700: aload 8
    //   702: astore 6
    //   704: aload_1
    //   705: iload 16
    //   707: invokevirtual 340	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   710: pop
    //   711: aload 8
    //   713: astore 7
    //   715: aload 8
    //   717: astore 6
    //   719: aload_1
    //   720: ldc_w 342
    //   723: invokevirtual 142	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   726: pop
    //   727: aload 8
    //   729: astore 7
    //   731: aload 8
    //   733: astore 6
    //   735: aload_1
    //   736: aload 4
    //   738: getfield 346	com/android/internal/telephony/SmsBroadcastUndelivered$SmsReferenceKey:mMessageCount	I
    //   741: invokevirtual 340	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   744: pop
    //   745: aload 8
    //   747: astore 7
    //   749: aload 8
    //   751: astore 6
    //   753: aload_1
    //   754: ldc_w 348
    //   757: invokevirtual 142	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   760: pop
    //   761: aload 8
    //   763: astore 7
    //   765: aload 8
    //   767: astore 6
    //   769: ldc 25
    //   771: aload_1
    //   772: invokevirtual 151	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   775: invokestatic 203	android/telephony/Rlog:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   778: pop
    //   779: goto -216 -> 563
    //   782: aload 8
    //   784: ifnull +10 -> 794
    //   787: aload 8
    //   789: invokeinterface 235 1 0
    //   794: new 135	java/lang/StringBuilder
    //   797: dup
    //   798: invokespecial 136	java/lang/StringBuilder:<init>	()V
    //   801: astore_1
    //   802: goto +42 -> 844
    //   805: astore_1
    //   806: goto +77 -> 883
    //   809: astore_1
    //   810: aload 6
    //   812: astore 7
    //   814: ldc 25
    //   816: ldc_w 350
    //   819: aload_1
    //   820: invokestatic 353	android/telephony/Rlog:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   823: pop
    //   824: aload 6
    //   826: ifnull +10 -> 836
    //   829: aload 6
    //   831: invokeinterface 235 1 0
    //   836: new 135	java/lang/StringBuilder
    //   839: dup
    //   840: invokespecial 136	java/lang/StringBuilder:<init>	()V
    //   843: astore_1
    //   844: aload_1
    //   845: ldc -19
    //   847: invokevirtual 142	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   850: pop
    //   851: aload_1
    //   852: invokestatic 209	java/lang/System:nanoTime	()J
    //   855: lload_2
    //   856: lsub
    //   857: ldc2_w 238
    //   860: ldiv
    //   861: invokevirtual 242	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   864: pop
    //   865: aload_1
    //   866: ldc -12
    //   868: invokevirtual 142	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   871: pop
    //   872: ldc 25
    //   874: aload_1
    //   875: invokevirtual 151	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   878: invokestatic 203	android/telephony/Rlog:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   881: pop
    //   882: return
    //   883: aload 7
    //   885: ifnull +10 -> 895
    //   888: aload 7
    //   890: invokeinterface 235 1 0
    //   895: new 135	java/lang/StringBuilder
    //   898: dup
    //   899: invokespecial 136	java/lang/StringBuilder:<init>	()V
    //   902: astore 7
    //   904: aload 7
    //   906: ldc -19
    //   908: invokevirtual 142	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   911: pop
    //   912: aload 7
    //   914: invokestatic 209	java/lang/System:nanoTime	()J
    //   917: lload_2
    //   918: lsub
    //   919: ldc2_w 238
    //   922: ldiv
    //   923: invokevirtual 242	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   926: pop
    //   927: aload 7
    //   929: ldc -12
    //   931: invokevirtual 142	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   934: pop
    //   935: ldc 25
    //   937: aload 7
    //   939: invokevirtual 151	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   942: invokestatic 203	android/telephony/Rlog:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   945: pop
    //   946: aload_1
    //   947: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	948	0	this	SmsBroadcastUndelivered
    //   0	948	1	paramContext	Context
    //   11	907	2	l1	long
    //   20	717	4	localObject1	Object
    //   30	560	5	localObject2	Object
    //   33	797	6	localObject3	Object
    //   36	902	7	localObject4	Object
    //   55	733	8	localCursor	android.database.Cursor
    //   148	38	9	bool1	boolean
    //   165	3	10	bool2	boolean
    //   190	228	11	localInboundSmsTracker	InboundSmsTracker
    //   237	300	12	localObject5	Object
    //   272	93	13	localInteger	Integer
    //   465	56	13	localIllegalArgumentException	IllegalArgumentException
    //   312	19	14	l2	long
    //   371	335	16	i	int
    // Exception table:
    //   from	to	target	type
    //   180	192	465	java/lang/IllegalArgumentException
    //   38	57	805	finally
    //   70	78	805	finally
    //   145	150	805	finally
    //   158	167	805	finally
    //   180	192	805	finally
    //   200	209	805	finally
    //   217	223	805	finally
    //   234	239	805	finally
    //   247	254	805	finally
    //   262	274	805	finally
    //   287	299	805	finally
    //   307	314	805	finally
    //   322	337	805	finally
    //   345	353	805	finally
    //   364	373	805	finally
    //   381	391	805	finally
    //   399	408	805	finally
    //   416	422	805	finally
    //   430	438	805	finally
    //   449	462	805	finally
    //   475	480	805	finally
    //   488	493	805	finally
    //   501	510	805	finally
    //   518	526	805	finally
    //   534	545	805	finally
    //   556	563	805	finally
    //   571	581	805	finally
    //   589	601	805	finally
    //   609	631	805	finally
    //   644	653	805	finally
    //   664	668	805	finally
    //   676	680	805	finally
    //   688	696	805	finally
    //   704	711	805	finally
    //   719	727	805	finally
    //   735	745	805	finally
    //   753	761	805	finally
    //   769	779	805	finally
    //   814	824	805	finally
    //   38	57	809	android/database/SQLException
    //   70	78	809	android/database/SQLException
    //   145	150	809	android/database/SQLException
    //   158	167	809	android/database/SQLException
    //   180	192	809	android/database/SQLException
    //   200	209	809	android/database/SQLException
    //   217	223	809	android/database/SQLException
    //   234	239	809	android/database/SQLException
    //   247	254	809	android/database/SQLException
    //   262	274	809	android/database/SQLException
    //   287	299	809	android/database/SQLException
    //   307	314	809	android/database/SQLException
    //   322	337	809	android/database/SQLException
    //   345	353	809	android/database/SQLException
    //   364	373	809	android/database/SQLException
    //   381	391	809	android/database/SQLException
    //   399	408	809	android/database/SQLException
    //   416	422	809	android/database/SQLException
    //   430	438	809	android/database/SQLException
    //   449	462	809	android/database/SQLException
    //   475	480	809	android/database/SQLException
    //   488	493	809	android/database/SQLException
    //   501	510	809	android/database/SQLException
    //   518	526	809	android/database/SQLException
    //   534	545	809	android/database/SQLException
    //   556	563	809	android/database/SQLException
    //   571	581	809	android/database/SQLException
    //   589	601	809	android/database/SQLException
    //   609	631	809	android/database/SQLException
    //   644	653	809	android/database/SQLException
    //   664	668	809	android/database/SQLException
    //   676	680	809	android/database/SQLException
    //   688	696	809	android/database/SQLException
    //   704	711	809	android/database/SQLException
    //   719	727	809	android/database/SQLException
    //   735	745	809	android/database/SQLException
    //   753	761	809	android/database/SQLException
    //   769	779	809	android/database/SQLException
  }
  
  private class ScanRawTableThread
    extends Thread
  {
    private final Context context;
    
    private ScanRawTableThread(Context paramContext)
    {
      context = paramContext;
    }
    
    public void run()
    {
      SmsBroadcastUndelivered.this.scanRawTable(context);
      InboundSmsHandler.cancelNewMessageNotification(context);
    }
  }
  
  private static class SmsReferenceKey
  {
    final String mAddress;
    final int mMessageCount;
    final String mQuery;
    final int mReferenceNumber;
    
    SmsReferenceKey(InboundSmsTracker paramInboundSmsTracker)
    {
      mAddress = paramInboundSmsTracker.getAddress();
      mReferenceNumber = paramInboundSmsTracker.getReferenceNumber();
      mMessageCount = paramInboundSmsTracker.getMessageCount();
      mQuery = paramInboundSmsTracker.getQueryForSegments();
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool1 = paramObject instanceof SmsReferenceKey;
      boolean bool2 = false;
      if (bool1)
      {
        paramObject = (SmsReferenceKey)paramObject;
        bool1 = bool2;
        if (mAddress.equals(mAddress))
        {
          bool1 = bool2;
          if (mReferenceNumber == mReferenceNumber)
          {
            bool1 = bool2;
            if (mMessageCount == mMessageCount) {
              bool1 = true;
            }
          }
        }
        return bool1;
      }
      return false;
    }
    
    String getDeleteWhere()
    {
      return mQuery;
    }
    
    String[] getDeleteWhereArgs()
    {
      return new String[] { mAddress, Integer.toString(mReferenceNumber), Integer.toString(mMessageCount) };
    }
    
    public int hashCode()
    {
      return (mReferenceNumber * 31 + mMessageCount) * 31 + mAddress.hashCode();
    }
  }
}
