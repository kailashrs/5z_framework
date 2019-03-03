package com.google.android.mms.util;

import android.content.Context;
import android.drm.DrmConvertedStatus;
import android.drm.DrmManagerClient;
import android.util.Log;

public class DrmConvertSession
{
  private static final String TAG = "DrmConvertSession";
  private int mConvertSessionId;
  private DrmManagerClient mDrmClient;
  
  private DrmConvertSession(DrmManagerClient paramDrmManagerClient, int paramInt)
  {
    mDrmClient = paramDrmManagerClient;
    mConvertSessionId = paramInt;
  }
  
  public static DrmConvertSession open(Context paramContext, String paramString)
  {
    StringBuilder localStringBuilder = null;
    Object localObject1 = null;
    Object localObject2 = null;
    int i = -1;
    Object localObject3 = localObject1;
    int j = i;
    if (paramContext != null)
    {
      localObject3 = localObject1;
      j = i;
      if (paramString != null)
      {
        localObject3 = localObject1;
        j = i;
        if (!paramString.equals(""))
        {
          localObject3 = localObject2;
          localObject1 = localStringBuilder;
          try
          {
            DrmManagerClient localDrmManagerClient = new android/drm/DrmManagerClient;
            localObject3 = localObject2;
            localObject1 = localStringBuilder;
            localDrmManagerClient.<init>(paramContext);
            paramContext = localDrmManagerClient;
            try
            {
              j = paramContext.openConvertSession(paramString);
              i = j;
            }
            catch (IllegalStateException paramString)
            {
              localObject3 = paramContext;
              localObject1 = paramContext;
              Log.w("DrmConvertSession", "Could not access Open DrmFramework.", paramString);
            }
            catch (IllegalArgumentException localIllegalArgumentException)
            {
              for (;;)
              {
                localObject3 = paramContext;
                localObject1 = paramContext;
                localStringBuilder = new java/lang/StringBuilder;
                localObject3 = paramContext;
                localObject1 = paramContext;
                localStringBuilder.<init>();
                localObject3 = paramContext;
                localObject1 = paramContext;
                localStringBuilder.append("Conversion of Mimetype: ");
                localObject3 = paramContext;
                localObject1 = paramContext;
                localStringBuilder.append(paramString);
                localObject3 = paramContext;
                localObject1 = paramContext;
                localStringBuilder.append(" is not supported.");
                localObject3 = paramContext;
                localObject1 = paramContext;
                Log.w("DrmConvertSession", localStringBuilder.toString(), localIllegalArgumentException);
              }
            }
            if (localObject3 == null) {
              break label240;
            }
          }
          catch (IllegalStateException paramContext)
          {
            Log.w("DrmConvertSession", "DrmManagerClient didn't initialize properly.");
            j = i;
          }
          catch (IllegalArgumentException paramContext)
          {
            Log.w("DrmConvertSession", "DrmManagerClient instance could not be created, context is Illegal.");
            paramContext = (Context)localObject1;
            j = i;
            localObject3 = paramContext;
          }
        }
      }
    }
    if (j >= 0) {
      return new DrmConvertSession((DrmManagerClient)localObject3, j);
    }
    label240:
    return null;
  }
  
  /* Error */
  public int close(String paramString)
  {
    // Byte code:
    //   0: sipush 491
    //   3: istore_2
    //   4: iload_2
    //   5: istore_3
    //   6: aload_0
    //   7: getfield 19	com/google/android/mms/util/DrmConvertSession:mDrmClient	Landroid/drm/DrmManagerClient;
    //   10: ifnull +1140 -> 1150
    //   13: iload_2
    //   14: istore_3
    //   15: aload_0
    //   16: getfield 21	com/google/android/mms/util/DrmConvertSession:mConvertSessionId	I
    //   19: iflt +1131 -> 1150
    //   22: iload_2
    //   23: istore_3
    //   24: aload_0
    //   25: getfield 19	com/google/android/mms/util/DrmConvertSession:mDrmClient	Landroid/drm/DrmManagerClient;
    //   28: aload_0
    //   29: getfield 21	com/google/android/mms/util/DrmConvertSession:mConvertSessionId	I
    //   32: invokevirtual 88	android/drm/DrmManagerClient:closeConvertSession	(I)Landroid/drm/DrmConvertedStatus;
    //   35: astore 4
    //   37: aload 4
    //   39: ifnull +1066 -> 1105
    //   42: iload_2
    //   43: istore_3
    //   44: aload 4
    //   46: getfield 93	android/drm/DrmConvertedStatus:statusCode	I
    //   49: iconst_1
    //   50: if_icmpne +1055 -> 1105
    //   53: iload_2
    //   54: istore_3
    //   55: aload 4
    //   57: getfield 97	android/drm/DrmConvertedStatus:convertedData	[B
    //   60: astore 5
    //   62: aload 5
    //   64: ifnonnull +6 -> 70
    //   67: goto +1038 -> 1105
    //   70: aconst_null
    //   71: astore 6
    //   73: aconst_null
    //   74: astore 7
    //   76: aconst_null
    //   77: astore 8
    //   79: aconst_null
    //   80: astore 9
    //   82: aconst_null
    //   83: astore 10
    //   85: iload_2
    //   86: istore 11
    //   88: aload 10
    //   90: astore 5
    //   92: aload 6
    //   94: astore 12
    //   96: aload 7
    //   98: astore 13
    //   100: aload 8
    //   102: astore 14
    //   104: aload 9
    //   106: astore 15
    //   108: new 99	java/io/RandomAccessFile
    //   111: astore 16
    //   113: iload_2
    //   114: istore 11
    //   116: aload 10
    //   118: astore 5
    //   120: aload 6
    //   122: astore 12
    //   124: aload 7
    //   126: astore 13
    //   128: aload 8
    //   130: astore 14
    //   132: aload 9
    //   134: astore 15
    //   136: aload 16
    //   138: aload_1
    //   139: ldc 101
    //   141: invokespecial 104	java/io/RandomAccessFile:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   144: iload_2
    //   145: istore 11
    //   147: aload 16
    //   149: astore 5
    //   151: aload 16
    //   153: astore 12
    //   155: aload 16
    //   157: astore 13
    //   159: aload 16
    //   161: astore 14
    //   163: aload 16
    //   165: astore 15
    //   167: aload 16
    //   169: aload 4
    //   171: getfield 107	android/drm/DrmConvertedStatus:offset	I
    //   174: i2l
    //   175: invokevirtual 111	java/io/RandomAccessFile:seek	(J)V
    //   178: iload_2
    //   179: istore 11
    //   181: aload 16
    //   183: astore 5
    //   185: aload 16
    //   187: astore 12
    //   189: aload 16
    //   191: astore 13
    //   193: aload 16
    //   195: astore 14
    //   197: aload 16
    //   199: astore 15
    //   201: aload 16
    //   203: aload 4
    //   205: getfield 97	android/drm/DrmConvertedStatus:convertedData	[B
    //   208: invokevirtual 115	java/io/RandomAccessFile:write	([B)V
    //   211: sipush 200
    //   214: istore 11
    //   216: iload 11
    //   218: istore_3
    //   219: aload 16
    //   221: invokevirtual 117	java/io/RandomAccessFile:close	()V
    //   224: iload 11
    //   226: istore_3
    //   227: goto +400 -> 627
    //   230: astore 5
    //   232: sipush 492
    //   235: istore 11
    //   237: iload 11
    //   239: istore_3
    //   240: new 55	java/lang/StringBuilder
    //   243: astore 16
    //   245: iload 11
    //   247: istore_3
    //   248: aload 16
    //   250: invokespecial 56	java/lang/StringBuilder:<init>	()V
    //   253: iload 11
    //   255: istore_3
    //   256: aload 16
    //   258: ldc 119
    //   260: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   263: pop
    //   264: iload 11
    //   266: istore_3
    //   267: aload 16
    //   269: aload_1
    //   270: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   273: pop
    //   274: iload 11
    //   276: istore_3
    //   277: aload 16
    //   279: ldc 121
    //   281: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   284: pop
    //   285: iload 11
    //   287: istore_3
    //   288: aload 16
    //   290: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   293: astore 16
    //   295: aload 5
    //   297: astore_1
    //   298: aload 16
    //   300: astore 5
    //   302: goto +185 -> 487
    //   305: astore 16
    //   307: goto +699 -> 1006
    //   310: astore 16
    //   312: iload_2
    //   313: istore 11
    //   315: aload 12
    //   317: astore 5
    //   319: new 55	java/lang/StringBuilder
    //   322: astore 14
    //   324: iload_2
    //   325: istore 11
    //   327: aload 12
    //   329: astore 5
    //   331: aload 14
    //   333: invokespecial 56	java/lang/StringBuilder:<init>	()V
    //   336: iload_2
    //   337: istore 11
    //   339: aload 12
    //   341: astore 5
    //   343: aload 14
    //   345: ldc 123
    //   347: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   350: pop
    //   351: iload_2
    //   352: istore 11
    //   354: aload 12
    //   356: astore 5
    //   358: aload 14
    //   360: aload_1
    //   361: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   364: pop
    //   365: iload_2
    //   366: istore 11
    //   368: aload 12
    //   370: astore 5
    //   372: aload 14
    //   374: ldc 125
    //   376: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   379: pop
    //   380: iload_2
    //   381: istore 11
    //   383: aload 12
    //   385: astore 5
    //   387: ldc 8
    //   389: aload 14
    //   391: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   394: aload 16
    //   396: invokestatic 53	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   399: pop
    //   400: iload_2
    //   401: istore_3
    //   402: aload 12
    //   404: ifnull +705 -> 1109
    //   407: iload_2
    //   408: istore_3
    //   409: aload 12
    //   411: invokevirtual 117	java/io/RandomAccessFile:close	()V
    //   414: iload_2
    //   415: istore_3
    //   416: goto +211 -> 627
    //   419: astore 16
    //   421: sipush 492
    //   424: istore 11
    //   426: iload 11
    //   428: istore_3
    //   429: new 55	java/lang/StringBuilder
    //   432: astore 5
    //   434: iload 11
    //   436: istore_3
    //   437: aload 5
    //   439: invokespecial 56	java/lang/StringBuilder:<init>	()V
    //   442: iload 11
    //   444: istore_3
    //   445: aload 5
    //   447: ldc 119
    //   449: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   452: pop
    //   453: iload 11
    //   455: istore_3
    //   456: aload 5
    //   458: aload_1
    //   459: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   462: pop
    //   463: iload 11
    //   465: istore_3
    //   466: aload 5
    //   468: ldc 121
    //   470: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   473: pop
    //   474: iload 11
    //   476: istore_3
    //   477: aload 5
    //   479: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   482: astore 5
    //   484: aload 16
    //   486: astore_1
    //   487: sipush 492
    //   490: istore 11
    //   492: iload 11
    //   494: istore_3
    //   495: ldc 8
    //   497: aload 5
    //   499: aload_1
    //   500: invokestatic 53	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   503: pop
    //   504: iload 11
    //   506: istore_3
    //   507: goto +120 -> 627
    //   510: astore 16
    //   512: sipush 492
    //   515: istore_2
    //   516: iload_2
    //   517: istore 11
    //   519: aload 13
    //   521: astore 5
    //   523: ldc 8
    //   525: ldc 127
    //   527: aload 16
    //   529: invokestatic 53	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   532: pop
    //   533: iload_2
    //   534: istore_3
    //   535: aload 13
    //   537: ifnull +572 -> 1109
    //   540: iload_2
    //   541: istore_3
    //   542: aload 13
    //   544: invokevirtual 117	java/io/RandomAccessFile:close	()V
    //   547: iload_2
    //   548: istore_3
    //   549: goto +78 -> 627
    //   552: astore 5
    //   554: sipush 492
    //   557: istore 11
    //   559: iload 11
    //   561: istore_3
    //   562: new 55	java/lang/StringBuilder
    //   565: astore 16
    //   567: iload 11
    //   569: istore_3
    //   570: aload 16
    //   572: invokespecial 56	java/lang/StringBuilder:<init>	()V
    //   575: iload 11
    //   577: istore_3
    //   578: aload 16
    //   580: ldc 119
    //   582: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   585: pop
    //   586: iload 11
    //   588: istore_3
    //   589: aload 16
    //   591: aload_1
    //   592: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   595: pop
    //   596: iload 11
    //   598: istore_3
    //   599: aload 16
    //   601: ldc 121
    //   603: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   606: pop
    //   607: iload 11
    //   609: istore_3
    //   610: aload 16
    //   612: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   615: astore 16
    //   617: aload 5
    //   619: astore_1
    //   620: aload 16
    //   622: astore 5
    //   624: goto -137 -> 487
    //   627: goto +482 -> 1109
    //   630: astore 12
    //   632: sipush 492
    //   635: istore_2
    //   636: iload_2
    //   637: istore 11
    //   639: aload 14
    //   641: astore 5
    //   643: new 55	java/lang/StringBuilder
    //   646: astore 16
    //   648: iload_2
    //   649: istore 11
    //   651: aload 14
    //   653: astore 5
    //   655: aload 16
    //   657: invokespecial 56	java/lang/StringBuilder:<init>	()V
    //   660: iload_2
    //   661: istore 11
    //   663: aload 14
    //   665: astore 5
    //   667: aload 16
    //   669: ldc -127
    //   671: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   674: pop
    //   675: iload_2
    //   676: istore 11
    //   678: aload 14
    //   680: astore 5
    //   682: aload 16
    //   684: aload_1
    //   685: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   688: pop
    //   689: iload_2
    //   690: istore 11
    //   692: aload 14
    //   694: astore 5
    //   696: aload 16
    //   698: ldc -125
    //   700: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   703: pop
    //   704: iload_2
    //   705: istore 11
    //   707: aload 14
    //   709: astore 5
    //   711: ldc 8
    //   713: aload 16
    //   715: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   718: aload 12
    //   720: invokestatic 53	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   723: pop
    //   724: iload_2
    //   725: istore_3
    //   726: aload 14
    //   728: ifnull +381 -> 1109
    //   731: iload_2
    //   732: istore_3
    //   733: aload 14
    //   735: invokevirtual 117	java/io/RandomAccessFile:close	()V
    //   738: iload_2
    //   739: istore_3
    //   740: goto -113 -> 627
    //   743: astore 5
    //   745: sipush 492
    //   748: istore 11
    //   750: iload 11
    //   752: istore_3
    //   753: new 55	java/lang/StringBuilder
    //   756: astore 16
    //   758: iload 11
    //   760: istore_3
    //   761: aload 16
    //   763: invokespecial 56	java/lang/StringBuilder:<init>	()V
    //   766: iload 11
    //   768: istore_3
    //   769: aload 16
    //   771: ldc 119
    //   773: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   776: pop
    //   777: iload 11
    //   779: istore_3
    //   780: aload 16
    //   782: aload_1
    //   783: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   786: pop
    //   787: iload 11
    //   789: istore_3
    //   790: aload 16
    //   792: ldc 121
    //   794: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   797: pop
    //   798: iload 11
    //   800: istore_3
    //   801: aload 16
    //   803: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   806: astore 16
    //   808: aload 5
    //   810: astore_1
    //   811: aload 16
    //   813: astore 5
    //   815: goto -328 -> 487
    //   818: astore 12
    //   820: sipush 492
    //   823: istore_2
    //   824: iload_2
    //   825: istore 11
    //   827: aload 15
    //   829: astore 5
    //   831: new 55	java/lang/StringBuilder
    //   834: astore 16
    //   836: iload_2
    //   837: istore 11
    //   839: aload 15
    //   841: astore 5
    //   843: aload 16
    //   845: invokespecial 56	java/lang/StringBuilder:<init>	()V
    //   848: iload_2
    //   849: istore 11
    //   851: aload 15
    //   853: astore 5
    //   855: aload 16
    //   857: ldc -123
    //   859: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   862: pop
    //   863: iload_2
    //   864: istore 11
    //   866: aload 15
    //   868: astore 5
    //   870: aload 16
    //   872: aload_1
    //   873: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   876: pop
    //   877: iload_2
    //   878: istore 11
    //   880: aload 15
    //   882: astore 5
    //   884: aload 16
    //   886: ldc -121
    //   888: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   891: pop
    //   892: iload_2
    //   893: istore 11
    //   895: aload 15
    //   897: astore 5
    //   899: ldc 8
    //   901: aload 16
    //   903: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   906: aload 12
    //   908: invokestatic 53	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   911: pop
    //   912: iload_2
    //   913: istore_3
    //   914: aload 15
    //   916: ifnull +193 -> 1109
    //   919: iload_2
    //   920: istore_3
    //   921: aload 15
    //   923: invokevirtual 117	java/io/RandomAccessFile:close	()V
    //   926: iload_2
    //   927: istore_3
    //   928: goto -301 -> 627
    //   931: astore 5
    //   933: sipush 492
    //   936: istore 11
    //   938: iload 11
    //   940: istore_3
    //   941: new 55	java/lang/StringBuilder
    //   944: astore 16
    //   946: iload 11
    //   948: istore_3
    //   949: aload 16
    //   951: invokespecial 56	java/lang/StringBuilder:<init>	()V
    //   954: iload 11
    //   956: istore_3
    //   957: aload 16
    //   959: ldc 119
    //   961: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   964: pop
    //   965: iload 11
    //   967: istore_3
    //   968: aload 16
    //   970: aload_1
    //   971: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   974: pop
    //   975: iload 11
    //   977: istore_3
    //   978: aload 16
    //   980: ldc 121
    //   982: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   985: pop
    //   986: iload 11
    //   988: istore_3
    //   989: aload 16
    //   991: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   994: astore 16
    //   996: aload 5
    //   998: astore_1
    //   999: aload 16
    //   1001: astore 5
    //   1003: goto -516 -> 487
    //   1006: iload 11
    //   1008: istore_3
    //   1009: aload 5
    //   1011: ifnull +91 -> 1102
    //   1014: iload 11
    //   1016: istore_3
    //   1017: aload 5
    //   1019: invokevirtual 117	java/io/RandomAccessFile:close	()V
    //   1022: iload 11
    //   1024: istore_3
    //   1025: goto +77 -> 1102
    //   1028: astore 5
    //   1030: sipush 492
    //   1033: istore 11
    //   1035: iload 11
    //   1037: istore_3
    //   1038: new 55	java/lang/StringBuilder
    //   1041: astore 12
    //   1043: iload 11
    //   1045: istore_3
    //   1046: aload 12
    //   1048: invokespecial 56	java/lang/StringBuilder:<init>	()V
    //   1051: iload 11
    //   1053: istore_3
    //   1054: aload 12
    //   1056: ldc 119
    //   1058: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1061: pop
    //   1062: iload 11
    //   1064: istore_3
    //   1065: aload 12
    //   1067: aload_1
    //   1068: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1071: pop
    //   1072: iload 11
    //   1074: istore_3
    //   1075: aload 12
    //   1077: ldc 121
    //   1079: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1082: pop
    //   1083: iload 11
    //   1085: istore_3
    //   1086: ldc 8
    //   1088: aload 12
    //   1090: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1093: aload 5
    //   1095: invokestatic 53	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   1098: pop
    //   1099: iload 11
    //   1101: istore_3
    //   1102: aload 16
    //   1104: athrow
    //   1105: sipush 406
    //   1108: istore_3
    //   1109: goto +41 -> 1150
    //   1112: astore 5
    //   1114: new 55	java/lang/StringBuilder
    //   1117: dup
    //   1118: invokespecial 56	java/lang/StringBuilder:<init>	()V
    //   1121: astore_1
    //   1122: aload_1
    //   1123: ldc -119
    //   1125: invokevirtual 62	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1128: pop
    //   1129: aload_1
    //   1130: aload_0
    //   1131: getfield 21	com/google/android/mms/util/DrmConvertSession:mConvertSessionId	I
    //   1134: invokevirtual 140	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   1137: pop
    //   1138: ldc 8
    //   1140: aload_1
    //   1141: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1144: aload 5
    //   1146: invokestatic 53	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   1149: pop
    //   1150: iload_3
    //   1151: ireturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	1152	0	this	DrmConvertSession
    //   0	1152	1	paramString	String
    //   3	924	2	i	int
    //   5	1146	3	j	int
    //   35	169	4	localDrmConvertedStatus	DrmConvertedStatus
    //   60	124	5	localObject1	Object
    //   230	66	5	localIOException1	java.io.IOException
    //   300	222	5	localObject2	Object
    //   552	66	5	localIOException2	java.io.IOException
    //   622	88	5	localObject3	Object
    //   743	66	5	localIOException3	java.io.IOException
    //   813	85	5	localObject4	Object
    //   931	66	5	localIOException4	java.io.IOException
    //   1001	17	5	localObject5	Object
    //   1028	66	5	localIOException5	java.io.IOException
    //   1112	33	5	localIllegalStateException	IllegalStateException
    //   71	50	6	localObject6	Object
    //   74	51	7	localObject7	Object
    //   77	52	8	localObject8	Object
    //   80	53	9	localObject9	Object
    //   83	34	10	localObject10	Object
    //   86	1014	11	k	int
    //   94	316	12	localObject11	Object
    //   630	89	12	localIOException6	java.io.IOException
    //   818	89	12	localFileNotFoundException	java.io.FileNotFoundException
    //   1041	48	12	localStringBuilder	StringBuilder
    //   98	445	13	localObject12	Object
    //   102	632	14	localObject13	Object
    //   106	816	15	localObject14	Object
    //   111	188	16	localObject15	Object
    //   305	1	16	localObject16	Object
    //   310	85	16	localSecurityException	SecurityException
    //   419	66	16	localIOException7	java.io.IOException
    //   510	18	16	localIllegalArgumentException	IllegalArgumentException
    //   565	538	16	localObject17	Object
    // Exception table:
    //   from	to	target	type
    //   219	224	230	java/io/IOException
    //   108	113	305	finally
    //   136	144	305	finally
    //   167	178	305	finally
    //   201	211	305	finally
    //   319	324	305	finally
    //   331	336	305	finally
    //   343	351	305	finally
    //   358	365	305	finally
    //   372	380	305	finally
    //   387	400	305	finally
    //   523	533	305	finally
    //   643	648	305	finally
    //   655	660	305	finally
    //   667	675	305	finally
    //   682	689	305	finally
    //   696	704	305	finally
    //   711	724	305	finally
    //   831	836	305	finally
    //   843	848	305	finally
    //   855	863	305	finally
    //   870	877	305	finally
    //   884	892	305	finally
    //   899	912	305	finally
    //   108	113	310	java/lang/SecurityException
    //   136	144	310	java/lang/SecurityException
    //   167	178	310	java/lang/SecurityException
    //   201	211	310	java/lang/SecurityException
    //   409	414	419	java/io/IOException
    //   108	113	510	java/lang/IllegalArgumentException
    //   136	144	510	java/lang/IllegalArgumentException
    //   167	178	510	java/lang/IllegalArgumentException
    //   201	211	510	java/lang/IllegalArgumentException
    //   542	547	552	java/io/IOException
    //   108	113	630	java/io/IOException
    //   136	144	630	java/io/IOException
    //   167	178	630	java/io/IOException
    //   201	211	630	java/io/IOException
    //   733	738	743	java/io/IOException
    //   108	113	818	java/io/FileNotFoundException
    //   136	144	818	java/io/FileNotFoundException
    //   167	178	818	java/io/FileNotFoundException
    //   201	211	818	java/io/FileNotFoundException
    //   921	926	931	java/io/IOException
    //   1017	1022	1028	java/io/IOException
    //   24	37	1112	java/lang/IllegalStateException
    //   44	53	1112	java/lang/IllegalStateException
    //   55	62	1112	java/lang/IllegalStateException
    //   219	224	1112	java/lang/IllegalStateException
    //   240	245	1112	java/lang/IllegalStateException
    //   248	253	1112	java/lang/IllegalStateException
    //   256	264	1112	java/lang/IllegalStateException
    //   267	274	1112	java/lang/IllegalStateException
    //   277	285	1112	java/lang/IllegalStateException
    //   288	295	1112	java/lang/IllegalStateException
    //   409	414	1112	java/lang/IllegalStateException
    //   429	434	1112	java/lang/IllegalStateException
    //   437	442	1112	java/lang/IllegalStateException
    //   445	453	1112	java/lang/IllegalStateException
    //   456	463	1112	java/lang/IllegalStateException
    //   466	474	1112	java/lang/IllegalStateException
    //   477	484	1112	java/lang/IllegalStateException
    //   495	504	1112	java/lang/IllegalStateException
    //   542	547	1112	java/lang/IllegalStateException
    //   562	567	1112	java/lang/IllegalStateException
    //   570	575	1112	java/lang/IllegalStateException
    //   578	586	1112	java/lang/IllegalStateException
    //   589	596	1112	java/lang/IllegalStateException
    //   599	607	1112	java/lang/IllegalStateException
    //   610	617	1112	java/lang/IllegalStateException
    //   733	738	1112	java/lang/IllegalStateException
    //   753	758	1112	java/lang/IllegalStateException
    //   761	766	1112	java/lang/IllegalStateException
    //   769	777	1112	java/lang/IllegalStateException
    //   780	787	1112	java/lang/IllegalStateException
    //   790	798	1112	java/lang/IllegalStateException
    //   801	808	1112	java/lang/IllegalStateException
    //   921	926	1112	java/lang/IllegalStateException
    //   941	946	1112	java/lang/IllegalStateException
    //   949	954	1112	java/lang/IllegalStateException
    //   957	965	1112	java/lang/IllegalStateException
    //   968	975	1112	java/lang/IllegalStateException
    //   978	986	1112	java/lang/IllegalStateException
    //   989	996	1112	java/lang/IllegalStateException
    //   1017	1022	1112	java/lang/IllegalStateException
    //   1038	1043	1112	java/lang/IllegalStateException
    //   1046	1051	1112	java/lang/IllegalStateException
    //   1054	1062	1112	java/lang/IllegalStateException
    //   1065	1072	1112	java/lang/IllegalStateException
    //   1075	1083	1112	java/lang/IllegalStateException
    //   1086	1099	1112	java/lang/IllegalStateException
    //   1102	1105	1112	java/lang/IllegalStateException
  }
  
  public byte[] convert(byte[] paramArrayOfByte, int paramInt)
  {
    Object localObject1 = null;
    Object localObject2 = null;
    if (paramArrayOfByte != null)
    {
      try
      {
        if (paramInt != paramArrayOfByte.length)
        {
          localObject3 = new byte[paramInt];
          System.arraycopy(paramArrayOfByte, 0, (byte[])localObject3, 0, paramInt);
          localObject3 = mDrmClient.convertData(mConvertSessionId, (byte[])localObject3);
        }
        else
        {
          localObject3 = mDrmClient.convertData(mConvertSessionId, paramArrayOfByte);
        }
        paramArrayOfByte = localObject2;
        if (localObject3 != null)
        {
          paramArrayOfByte = localObject2;
          if (statusCode == 1)
          {
            paramArrayOfByte = localObject2;
            if (convertedData != null) {
              paramArrayOfByte = convertedData;
            }
          }
        }
      }
      catch (IllegalStateException paramArrayOfByte)
      {
        localObject3 = new StringBuilder();
        ((StringBuilder)localObject3).append("Could not convert data. Convertsession: ");
        ((StringBuilder)localObject3).append(mConvertSessionId);
        Log.w("DrmConvertSession", ((StringBuilder)localObject3).toString(), paramArrayOfByte);
        paramArrayOfByte = localObject1;
      }
      catch (IllegalArgumentException paramArrayOfByte)
      {
        for (;;)
        {
          Object localObject3 = new StringBuilder();
          ((StringBuilder)localObject3).append("Buffer with data to convert is illegal. Convertsession: ");
          ((StringBuilder)localObject3).append(mConvertSessionId);
          Log.w("DrmConvertSession", ((StringBuilder)localObject3).toString(), paramArrayOfByte);
          paramArrayOfByte = localObject2;
        }
      }
      return paramArrayOfByte;
    }
    throw new IllegalArgumentException("Parameter inBuffer is null");
  }
}
