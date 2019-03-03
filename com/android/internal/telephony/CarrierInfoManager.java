package com.android.internal.telephony;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.provider.Telephony.CarrierColumns;
import android.telephony.ImsiEncryptionInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

public class CarrierInfoManager
{
  private static final String KEY_TYPE = "KEY_TYPE";
  private static final String LOG_TAG = "CarrierInfoManager";
  private static final int RESET_CARRIER_KEY_RATE_LIMIT = 43200000;
  private long mLastAccessResetCarrierKey = 0L;
  
  public CarrierInfoManager() {}
  
  public static void deleteAllCarrierKeysForImsiEncryption(Context paramContext)
  {
    Log.i("CarrierInfoManager", "deleting ALL carrier keys from db");
    paramContext = paramContext.getContentResolver();
    try
    {
      paramContext.delete(Telephony.CarrierColumns.CONTENT_URI, null, null);
    }
    catch (Exception localException)
    {
      paramContext = new StringBuilder();
      paramContext.append("Delete failed");
      paramContext.append(localException);
      Log.e("CarrierInfoManager", paramContext.toString());
    }
  }
  
  public static void deleteCarrierInfoForImsiEncryption(Context paramContext)
  {
    Log.i("CarrierInfoManager", "deleting carrier key from db");
    String str = ((TelephonyManager)paramContext.getSystemService("phone")).getSimOperator();
    if (!TextUtils.isEmpty(str))
    {
      Object localObject = str.substring(0, 3);
      str = str.substring(3);
      paramContext = paramContext.getContentResolver();
      try
      {
        paramContext.delete(Telephony.CarrierColumns.CONTENT_URI, "mcc=? and mnc=?", new String[] { localObject, str });
      }
      catch (Exception paramContext)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Delete failed");
        ((StringBuilder)localObject).append(paramContext);
        Log.e("CarrierInfoManager", ((StringBuilder)localObject).toString());
      }
      return;
    }
    paramContext = new StringBuilder();
    paramContext.append("Invalid networkOperator: ");
    paramContext.append(str);
    Log.e("CarrierInfoManager", paramContext.toString());
  }
  
  /* Error */
  public static ImsiEncryptionInfo getCarrierInfoForImsiEncryption(int paramInt, Context paramContext)
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc 76
    //   3: invokevirtual 80	android/content/Context:getSystemService	(Ljava/lang/String;)Ljava/lang/Object;
    //   6: checkcast 82	android/telephony/TelephonyManager
    //   9: invokevirtual 85	android/telephony/TelephonyManager:getSimOperator	()Ljava/lang/String;
    //   12: astore_2
    //   13: aload_2
    //   14: invokestatic 91	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   17: ifne +485 -> 502
    //   20: aload_2
    //   21: iconst_0
    //   22: iconst_3
    //   23: invokevirtual 97	java/lang/String:substring	(II)Ljava/lang/String;
    //   26: astore_3
    //   27: aload_2
    //   28: iconst_3
    //   29: invokevirtual 100	java/lang/String:substring	(I)Ljava/lang/String;
    //   32: astore 4
    //   34: new 54	java/lang/StringBuilder
    //   37: dup
    //   38: invokespecial 55	java/lang/StringBuilder:<init>	()V
    //   41: astore_2
    //   42: aload_2
    //   43: ldc 110
    //   45: invokevirtual 61	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   48: pop
    //   49: aload_2
    //   50: aload 4
    //   52: invokevirtual 61	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   55: pop
    //   56: aload_2
    //   57: ldc 112
    //   59: invokevirtual 61	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   62: pop
    //   63: aload_2
    //   64: aload_3
    //   65: invokevirtual 61	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   68: pop
    //   69: ldc 10
    //   71: aload_2
    //   72: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   75: invokestatic 34	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   78: pop
    //   79: aconst_null
    //   80: astore 5
    //   82: aconst_null
    //   83: astore 6
    //   85: aconst_null
    //   86: astore_2
    //   87: aload_1
    //   88: invokevirtual 40	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   91: getstatic 46	android/provider/Telephony$CarrierColumns:CONTENT_URI	Landroid/net/Uri;
    //   94: iconst_3
    //   95: anewarray 93	java/lang/String
    //   98: dup
    //   99: iconst_0
    //   100: ldc 114
    //   102: aastore
    //   103: dup
    //   104: iconst_1
    //   105: ldc 116
    //   107: aastore
    //   108: dup
    //   109: iconst_2
    //   110: ldc 118
    //   112: aastore
    //   113: ldc 120
    //   115: iconst_3
    //   116: anewarray 93	java/lang/String
    //   119: dup
    //   120: iconst_0
    //   121: aload_3
    //   122: aastore
    //   123: dup
    //   124: iconst_1
    //   125: aload 4
    //   127: aastore
    //   128: dup
    //   129: iconst_2
    //   130: iload_0
    //   131: invokestatic 123	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   134: aastore
    //   135: aconst_null
    //   136: invokevirtual 127	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   139: astore_1
    //   140: aload_1
    //   141: ifnull +162 -> 303
    //   144: aload_1
    //   145: invokeinterface 133 1 0
    //   150: ifne +6 -> 156
    //   153: goto +150 -> 303
    //   156: aload_1
    //   157: invokeinterface 137 1 0
    //   162: istore 7
    //   164: iload 7
    //   166: iconst_1
    //   167: if_icmple +53 -> 220
    //   170: new 54	java/lang/StringBuilder
    //   173: astore_2
    //   174: aload_2
    //   175: invokespecial 55	java/lang/StringBuilder:<init>	()V
    //   178: aload_2
    //   179: ldc -117
    //   181: invokevirtual 61	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   184: pop
    //   185: aload_2
    //   186: iload_0
    //   187: invokevirtual 142	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   190: pop
    //   191: ldc 10
    //   193: aload_2
    //   194: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   197: invokestatic 71	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   200: pop
    //   201: goto +19 -> 220
    //   204: astore_3
    //   205: aload_1
    //   206: astore_2
    //   207: aload_3
    //   208: astore_1
    //   209: goto +281 -> 490
    //   212: astore_3
    //   213: goto +159 -> 372
    //   216: astore_3
    //   217: goto +218 -> 435
    //   220: aload_1
    //   221: iconst_0
    //   222: invokeinterface 146 2 0
    //   227: astore 5
    //   229: new 148	java/util/Date
    //   232: astore 8
    //   234: aload 8
    //   236: aload_1
    //   237: iconst_1
    //   238: invokeinterface 152 2 0
    //   243: invokespecial 155	java/util/Date:<init>	(J)V
    //   246: aload_1
    //   247: iconst_2
    //   248: invokeinterface 158 2 0
    //   253: astore 6
    //   255: aload_1
    //   256: astore_2
    //   257: new 160	android/telephony/ImsiEncryptionInfo
    //   260: dup
    //   261: aload_3
    //   262: aload 4
    //   264: iload_0
    //   265: aload 6
    //   267: aload 5
    //   269: aload 8
    //   271: invokespecial 163	android/telephony/ImsiEncryptionInfo:<init>	(Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;[BLjava/util/Date;)V
    //   274: astore_3
    //   275: aload_2
    //   276: ifnull +9 -> 285
    //   279: aload_2
    //   280: invokeinterface 166 1 0
    //   285: aload_3
    //   286: areturn
    //   287: astore_3
    //   288: aload_1
    //   289: astore_2
    //   290: aload_3
    //   291: astore_1
    //   292: goto +198 -> 490
    //   295: astore_3
    //   296: goto +76 -> 372
    //   299: astore_3
    //   300: goto +135 -> 435
    //   303: aload_1
    //   304: astore_2
    //   305: new 54	java/lang/StringBuilder
    //   308: astore_3
    //   309: aload_3
    //   310: invokespecial 55	java/lang/StringBuilder:<init>	()V
    //   313: aload_3
    //   314: ldc -88
    //   316: invokevirtual 61	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   319: pop
    //   320: aload_3
    //   321: iload_0
    //   322: invokevirtual 142	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   325: pop
    //   326: ldc 10
    //   328: aload_3
    //   329: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   332: invokestatic 171	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   335: pop
    //   336: aload_2
    //   337: ifnull +9 -> 346
    //   340: aload_2
    //   341: invokeinterface 166 1 0
    //   346: aconst_null
    //   347: areturn
    //   348: astore_3
    //   349: aload_1
    //   350: astore_2
    //   351: aload_3
    //   352: astore_1
    //   353: goto +137 -> 490
    //   356: astore_3
    //   357: goto +15 -> 372
    //   360: astore_3
    //   361: goto +74 -> 435
    //   364: astore_1
    //   365: goto +125 -> 490
    //   368: astore_3
    //   369: aload 5
    //   371: astore_1
    //   372: aload_1
    //   373: astore_2
    //   374: new 54	java/lang/StringBuilder
    //   377: astore 5
    //   379: aload_1
    //   380: astore_2
    //   381: aload 5
    //   383: invokespecial 55	java/lang/StringBuilder:<init>	()V
    //   386: aload_1
    //   387: astore_2
    //   388: aload 5
    //   390: ldc -83
    //   392: invokevirtual 61	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   395: pop
    //   396: aload_1
    //   397: astore_2
    //   398: aload 5
    //   400: aload_3
    //   401: invokevirtual 64	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   404: pop
    //   405: aload_1
    //   406: astore_2
    //   407: ldc 10
    //   409: aload 5
    //   411: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   414: invokestatic 71	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   417: pop
    //   418: aload_1
    //   419: ifnull +69 -> 488
    //   422: aload_1
    //   423: invokeinterface 166 1 0
    //   428: goto +60 -> 488
    //   431: astore_3
    //   432: aload 6
    //   434: astore_1
    //   435: aload_1
    //   436: astore_2
    //   437: new 54	java/lang/StringBuilder
    //   440: astore 5
    //   442: aload_1
    //   443: astore_2
    //   444: aload 5
    //   446: invokespecial 55	java/lang/StringBuilder:<init>	()V
    //   449: aload_1
    //   450: astore_2
    //   451: aload 5
    //   453: ldc -81
    //   455: invokevirtual 61	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   458: pop
    //   459: aload_1
    //   460: astore_2
    //   461: aload 5
    //   463: aload_3
    //   464: invokevirtual 64	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   467: pop
    //   468: aload_1
    //   469: astore_2
    //   470: ldc 10
    //   472: aload 5
    //   474: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   477: invokestatic 71	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   480: pop
    //   481: aload_1
    //   482: ifnull +6 -> 488
    //   485: goto -63 -> 422
    //   488: aconst_null
    //   489: areturn
    //   490: aload_2
    //   491: ifnull +9 -> 500
    //   494: aload_2
    //   495: invokeinterface 166 1 0
    //   500: aload_1
    //   501: athrow
    //   502: new 54	java/lang/StringBuilder
    //   505: dup
    //   506: invokespecial 55	java/lang/StringBuilder:<init>	()V
    //   509: astore_1
    //   510: aload_1
    //   511: ldc 104
    //   513: invokevirtual 61	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   516: pop
    //   517: aload_1
    //   518: aload_2
    //   519: invokevirtual 61	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   522: pop
    //   523: ldc 10
    //   525: aload_1
    //   526: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   529: invokestatic 71	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   532: pop
    //   533: aconst_null
    //   534: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	535	0	paramInt	int
    //   0	535	1	paramContext	Context
    //   12	507	2	localObject1	Object
    //   26	96	3	str1	String
    //   204	4	3	localObject2	Object
    //   212	1	3	localException1	Exception
    //   216	46	3	localIllegalArgumentException1	IllegalArgumentException
    //   274	12	3	localImsiEncryptionInfo	ImsiEncryptionInfo
    //   287	4	3	localObject3	Object
    //   295	1	3	localException2	Exception
    //   299	1	3	localIllegalArgumentException2	IllegalArgumentException
    //   308	21	3	localStringBuilder	StringBuilder
    //   348	4	3	localObject4	Object
    //   356	1	3	localException3	Exception
    //   360	1	3	localIllegalArgumentException3	IllegalArgumentException
    //   368	33	3	localException4	Exception
    //   431	33	3	localIllegalArgumentException4	IllegalArgumentException
    //   32	231	4	str2	String
    //   80	393	5	localObject5	Object
    //   83	350	6	str3	String
    //   162	6	7	i	int
    //   232	38	8	localDate	java.util.Date
    // Exception table:
    //   from	to	target	type
    //   170	201	204	finally
    //   170	201	212	java/lang/Exception
    //   170	201	216	java/lang/IllegalArgumentException
    //   144	153	287	finally
    //   156	164	287	finally
    //   220	255	287	finally
    //   144	153	295	java/lang/Exception
    //   156	164	295	java/lang/Exception
    //   220	255	295	java/lang/Exception
    //   144	153	299	java/lang/IllegalArgumentException
    //   156	164	299	java/lang/IllegalArgumentException
    //   220	255	299	java/lang/IllegalArgumentException
    //   257	275	348	finally
    //   305	336	348	finally
    //   257	275	356	java/lang/Exception
    //   305	336	356	java/lang/Exception
    //   257	275	360	java/lang/IllegalArgumentException
    //   305	336	360	java/lang/IllegalArgumentException
    //   87	140	364	finally
    //   374	379	364	finally
    //   381	386	364	finally
    //   388	396	364	finally
    //   398	405	364	finally
    //   407	418	364	finally
    //   437	442	364	finally
    //   444	449	364	finally
    //   451	459	364	finally
    //   461	468	364	finally
    //   470	481	364	finally
    //   87	140	368	java/lang/Exception
    //   87	140	431	java/lang/IllegalArgumentException
  }
  
  public static void setCarrierInfoForImsiEncryption(ImsiEncryptionInfo paramImsiEncryptionInfo, Context paramContext, int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("inserting carrier key: ");
    localStringBuilder.append(paramImsiEncryptionInfo);
    Log.i("CarrierInfoManager", localStringBuilder.toString());
    updateOrInsertCarrierKey(paramImsiEncryptionInfo, paramContext, paramInt);
  }
  
  /* Error */
  public static void updateOrInsertCarrierKey(ImsiEncryptionInfo paramImsiEncryptionInfo, Context paramContext, int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 188	android/telephony/ImsiEncryptionInfo:getPublicKey	()Ljava/security/PublicKey;
    //   4: invokeinterface 194 1 0
    //   9: astore_3
    //   10: aload_1
    //   11: invokevirtual 40	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   14: astore 4
    //   16: invokestatic 200	com/android/internal/telephony/metrics/TelephonyMetrics:getInstance	()Lcom/android/internal/telephony/metrics/TelephonyMetrics;
    //   19: astore_1
    //   20: new 202	android/content/ContentValues
    //   23: dup
    //   24: invokespecial 203	android/content/ContentValues:<init>	()V
    //   27: astore 5
    //   29: aload 5
    //   31: ldc -51
    //   33: aload_0
    //   34: invokevirtual 208	android/telephony/ImsiEncryptionInfo:getMcc	()Ljava/lang/String;
    //   37: invokevirtual 212	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   40: aload 5
    //   42: ldc -42
    //   44: aload_0
    //   45: invokevirtual 217	android/telephony/ImsiEncryptionInfo:getMnc	()Ljava/lang/String;
    //   48: invokevirtual 212	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   51: aload 5
    //   53: ldc -37
    //   55: aload_0
    //   56: invokevirtual 222	android/telephony/ImsiEncryptionInfo:getKeyType	()I
    //   59: invokestatic 227	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   62: invokevirtual 230	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   65: aload 5
    //   67: ldc 118
    //   69: aload_0
    //   70: invokevirtual 233	android/telephony/ImsiEncryptionInfo:getKeyIdentifier	()Ljava/lang/String;
    //   73: invokevirtual 212	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   76: aload 5
    //   78: ldc 114
    //   80: aload_3
    //   81: invokevirtual 236	android/content/ContentValues:put	(Ljava/lang/String;[B)V
    //   84: aload 5
    //   86: ldc 116
    //   88: aload_0
    //   89: invokevirtual 240	android/telephony/ImsiEncryptionInfo:getExpirationTime	()Ljava/util/Date;
    //   92: invokevirtual 244	java/util/Date:getTime	()J
    //   95: invokestatic 249	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   98: invokevirtual 252	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Long;)V
    //   101: iconst_1
    //   102: istore 6
    //   104: iconst_1
    //   105: istore 7
    //   107: ldc 10
    //   109: ldc -2
    //   111: invokestatic 34	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   114: pop
    //   115: aload 4
    //   117: getstatic 46	android/provider/Telephony$CarrierColumns:CONTENT_URI	Landroid/net/Uri;
    //   120: aload 5
    //   122: invokevirtual 258	android/content/ContentResolver:insert	(Landroid/net/Uri;Landroid/content/ContentValues;)Landroid/net/Uri;
    //   125: pop
    //   126: iload 7
    //   128: istore 6
    //   130: aload_1
    //   131: iload_2
    //   132: aload_0
    //   133: invokevirtual 222	android/telephony/ImsiEncryptionInfo:getKeyType	()I
    //   136: iload 6
    //   138: invokevirtual 262	com/android/internal/telephony/metrics/TelephonyMetrics:writeCarrierKeyEvent	(IIZ)V
    //   141: goto +243 -> 384
    //   144: astore_3
    //   145: goto +240 -> 385
    //   148: astore 4
    //   150: new 54	java/lang/StringBuilder
    //   153: astore_3
    //   154: aload_3
    //   155: invokespecial 55	java/lang/StringBuilder:<init>	()V
    //   158: aload_3
    //   159: ldc_w 264
    //   162: invokevirtual 61	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   165: pop
    //   166: aload_3
    //   167: aload_0
    //   168: invokevirtual 64	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   171: pop
    //   172: aload_3
    //   173: aload 4
    //   175: invokevirtual 64	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   178: pop
    //   179: ldc 10
    //   181: aload_3
    //   182: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   185: invokestatic 171	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   188: pop
    //   189: iconst_0
    //   190: istore 6
    //   192: goto -62 -> 130
    //   195: astore 5
    //   197: ldc 10
    //   199: ldc_w 266
    //   202: invokestatic 34	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   205: pop
    //   206: new 202	android/content/ContentValues
    //   209: astore 5
    //   211: aload 5
    //   213: invokespecial 203	android/content/ContentValues:<init>	()V
    //   216: aload 5
    //   218: ldc 114
    //   220: aload_3
    //   221: invokevirtual 236	android/content/ContentValues:put	(Ljava/lang/String;[B)V
    //   224: aload 5
    //   226: ldc 116
    //   228: aload_0
    //   229: invokevirtual 240	android/telephony/ImsiEncryptionInfo:getExpirationTime	()Ljava/util/Date;
    //   232: invokevirtual 244	java/util/Date:getTime	()J
    //   235: invokestatic 249	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   238: invokevirtual 252	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Long;)V
    //   241: aload 5
    //   243: ldc 118
    //   245: aload_0
    //   246: invokevirtual 233	android/telephony/ImsiEncryptionInfo:getKeyIdentifier	()Ljava/lang/String;
    //   249: invokevirtual 212	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/String;)V
    //   252: aload 4
    //   254: getstatic 46	android/provider/Telephony$CarrierColumns:CONTENT_URI	Landroid/net/Uri;
    //   257: aload 5
    //   259: ldc 120
    //   261: iconst_3
    //   262: anewarray 93	java/lang/String
    //   265: dup
    //   266: iconst_0
    //   267: aload_0
    //   268: invokevirtual 208	android/telephony/ImsiEncryptionInfo:getMcc	()Ljava/lang/String;
    //   271: aastore
    //   272: dup
    //   273: iconst_1
    //   274: aload_0
    //   275: invokevirtual 217	android/telephony/ImsiEncryptionInfo:getMnc	()Ljava/lang/String;
    //   278: aastore
    //   279: dup
    //   280: iconst_2
    //   281: aload_0
    //   282: invokevirtual 222	android/telephony/ImsiEncryptionInfo:getKeyType	()I
    //   285: invokestatic 123	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   288: aastore
    //   289: invokevirtual 270	android/content/ContentResolver:update	(Landroid/net/Uri;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
    //   292: ifne +38 -> 330
    //   295: new 54	java/lang/StringBuilder
    //   298: astore_3
    //   299: aload_3
    //   300: invokespecial 55	java/lang/StringBuilder:<init>	()V
    //   303: aload_3
    //   304: ldc_w 272
    //   307: invokevirtual 61	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   310: pop
    //   311: aload_3
    //   312: aload_0
    //   313: invokevirtual 64	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   316: pop
    //   317: ldc 10
    //   319: aload_3
    //   320: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   323: invokestatic 171	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   326: pop
    //   327: iconst_0
    //   328: istore 6
    //   330: goto -200 -> 130
    //   333: astore_3
    //   334: new 54	java/lang/StringBuilder
    //   337: astore 4
    //   339: aload 4
    //   341: invokespecial 55	java/lang/StringBuilder:<init>	()V
    //   344: aload 4
    //   346: ldc_w 272
    //   349: invokevirtual 61	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   352: pop
    //   353: aload 4
    //   355: aload_0
    //   356: invokevirtual 64	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   359: pop
    //   360: aload 4
    //   362: aload_3
    //   363: invokevirtual 64	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   366: pop
    //   367: ldc 10
    //   369: aload 4
    //   371: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   374: invokestatic 171	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   377: pop
    //   378: iconst_0
    //   379: istore 6
    //   381: goto -251 -> 130
    //   384: return
    //   385: aload_1
    //   386: iload_2
    //   387: aload_0
    //   388: invokevirtual 222	android/telephony/ImsiEncryptionInfo:getKeyType	()I
    //   391: iload 7
    //   393: invokevirtual 262	com/android/internal/telephony/metrics/TelephonyMetrics:writeCarrierKeyEvent	(IIZ)V
    //   396: aload_3
    //   397: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	398	0	paramImsiEncryptionInfo	ImsiEncryptionInfo
    //   0	398	1	paramContext	Context
    //   0	398	2	paramInt	int
    //   9	72	3	arrayOfByte	byte[]
    //   144	1	3	localObject	Object
    //   153	167	3	localStringBuilder1	StringBuilder
    //   333	64	3	localException1	Exception
    //   14	102	4	localContentResolver	ContentResolver
    //   148	105	4	localException2	Exception
    //   337	33	4	localStringBuilder2	StringBuilder
    //   27	94	5	localContentValues1	android.content.ContentValues
    //   195	1	5	localSQLiteConstraintException	android.database.sqlite.SQLiteConstraintException
    //   209	49	5	localContentValues2	android.content.ContentValues
    //   102	278	6	bool1	boolean
    //   105	287	7	bool2	boolean
    // Exception table:
    //   from	to	target	type
    //   107	126	144	finally
    //   150	189	144	finally
    //   197	252	144	finally
    //   252	327	144	finally
    //   334	378	144	finally
    //   107	126	148	java/lang/Exception
    //   107	126	195	android/database/sqlite/SQLiteConstraintException
    //   252	327	333	java/lang/Exception
  }
  
  public void resetCarrierKeysForImsiEncryption(Context paramContext, int paramInt)
  {
    Log.i("CarrierInfoManager", "resetting carrier key");
    long l = System.currentTimeMillis();
    if (l - mLastAccessResetCarrierKey < 43200000L)
    {
      Log.i("CarrierInfoManager", "resetCarrierKeysForImsiEncryption: Access rate exceeded");
      return;
    }
    mLastAccessResetCarrierKey = l;
    deleteCarrierInfoForImsiEncryption(paramContext);
    Intent localIntent = new Intent("com.android.internal.telephony.ACTION_CARRIER_CERTIFICATE_DOWNLOAD");
    localIntent.putExtra("phone", paramInt);
    paramContext.sendBroadcastAsUser(localIntent, UserHandle.ALL);
  }
}
