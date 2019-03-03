package com.android.internal.net;

import android.net.NetworkStats;
import android.net.NetworkStats.Entry;
import android.os.SystemClock;
import com.android.internal.annotations.VisibleForTesting;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkStatsFactory
{
  private static final boolean SANITY_CHECK_NATIVE = false;
  private static final String TAG = "NetworkStatsFactory";
  private static final boolean USE_NATIVE_PARSING = true;
  private static final ConcurrentHashMap<String, String> sStackedIfaces = new ConcurrentHashMap();
  private final File mStatsXtIfaceAll;
  private final File mStatsXtIfaceFmt;
  private final File mStatsXtUid;
  private boolean mUseBpfStats;
  
  public NetworkStatsFactory()
  {
    this(new File("/proc/"), new File("/sys/fs/bpf/traffic_uid_stats_map").exists());
  }
  
  @VisibleForTesting
  public NetworkStatsFactory(File paramFile, boolean paramBoolean)
  {
    mStatsXtIfaceAll = new File(paramFile, "net/xt_qtaguid/iface_stat_all");
    mStatsXtIfaceFmt = new File(paramFile, "net/xt_qtaguid/iface_stat_fmt");
    mStatsXtUid = new File(paramFile, "net/xt_qtaguid/stats");
    mUseBpfStats = paramBoolean;
  }
  
  public static void apply464xlatAdjustments(NetworkStats paramNetworkStats1, NetworkStats paramNetworkStats2)
  {
    NetworkStats.apply464xlatAdjustments(paramNetworkStats1, paramNetworkStats2, sStackedIfaces);
  }
  
  public static String[] augmentWithStackedInterfaces(String[] paramArrayOfString)
  {
    if (paramArrayOfString == NetworkStats.INTERFACES_ALL) {
      return null;
    }
    paramArrayOfString = new HashSet(Arrays.asList(paramArrayOfString));
    Iterator localIterator = sStackedIfaces.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      if (paramArrayOfString.contains(localEntry.getKey())) {
        paramArrayOfString.add((String)localEntry.getValue());
      } else if (paramArrayOfString.contains(localEntry.getValue())) {
        paramArrayOfString.add((String)localEntry.getKey());
      }
    }
    return (String[])paramArrayOfString.toArray(new String[paramArrayOfString.size()]);
  }
  
  @VisibleForTesting
  public static void clearStackedIfaces()
  {
    sStackedIfaces.clear();
  }
  
  /* Error */
  @VisibleForTesting
  public static NetworkStats javaReadNetworkStatsDetail(File paramFile, int paramInt1, String[] paramArrayOfString, int paramInt2)
    throws IOException
  {
    // Byte code:
    //   0: invokestatic 154	android/os/StrictMode:allowThreadDiskReads	()Landroid/os/StrictMode$ThreadPolicy;
    //   3: astore 4
    //   5: new 71	android/net/NetworkStats
    //   8: dup
    //   9: invokestatic 160	android/os/SystemClock:elapsedRealtime	()J
    //   12: bipush 24
    //   14: invokespecial 163	android/net/NetworkStats:<init>	(JI)V
    //   17: astore 5
    //   19: new 165	android/net/NetworkStats$Entry
    //   22: dup
    //   23: invokespecial 166	android/net/NetworkStats$Entry:<init>	()V
    //   26: astore 6
    //   28: iconst_1
    //   29: istore 7
    //   31: iconst_1
    //   32: istore 8
    //   34: iconst_1
    //   35: istore 9
    //   37: aconst_null
    //   38: astore 10
    //   40: aconst_null
    //   41: astore 11
    //   43: aload 11
    //   45: astore 12
    //   47: iload 7
    //   49: istore 13
    //   51: aload 10
    //   53: astore 14
    //   55: new 168	com/android/internal/util/ProcFileReader
    //   58: astore 15
    //   60: aload 11
    //   62: astore 12
    //   64: iload 7
    //   66: istore 13
    //   68: aload 10
    //   70: astore 14
    //   72: new 170	java/io/FileInputStream
    //   75: astore 16
    //   77: aload 11
    //   79: astore 12
    //   81: iload 7
    //   83: istore 13
    //   85: aload 10
    //   87: astore 14
    //   89: aload 16
    //   91: aload_0
    //   92: invokespecial 173	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   95: aload 11
    //   97: astore 12
    //   99: iload 7
    //   101: istore 13
    //   103: aload 10
    //   105: astore 14
    //   107: aload 15
    //   109: aload 16
    //   111: invokespecial 176	com/android/internal/util/ProcFileReader:<init>	(Ljava/io/InputStream;)V
    //   114: aload 15
    //   116: astore_0
    //   117: aload_0
    //   118: astore 12
    //   120: iload 7
    //   122: istore 13
    //   124: aload_0
    //   125: astore 14
    //   127: aload_0
    //   128: invokevirtual 179	com/android/internal/util/ProcFileReader:finishLine	()V
    //   131: aload_0
    //   132: astore 12
    //   134: iload 8
    //   136: istore 13
    //   138: aload_0
    //   139: astore 14
    //   141: aload_0
    //   142: invokevirtual 182	com/android/internal/util/ProcFileReader:hasMoreData	()Z
    //   145: ifeq +443 -> 588
    //   148: aload_0
    //   149: astore 12
    //   151: iload 8
    //   153: istore 13
    //   155: aload_0
    //   156: astore 14
    //   158: aload_0
    //   159: invokevirtual 185	com/android/internal/util/ProcFileReader:nextInt	()I
    //   162: istore 8
    //   164: iload 8
    //   166: iload 9
    //   168: iconst_1
    //   169: iadd
    //   170: if_icmpne +271 -> 441
    //   173: iload 8
    //   175: istore 9
    //   177: aload_0
    //   178: astore 12
    //   180: iload 8
    //   182: istore 13
    //   184: aload_0
    //   185: astore 14
    //   187: aload 6
    //   189: aload_0
    //   190: invokevirtual 189	com/android/internal/util/ProcFileReader:nextString	()Ljava/lang/String;
    //   193: putfield 192	android/net/NetworkStats$Entry:iface	Ljava/lang/String;
    //   196: aload_0
    //   197: astore 12
    //   199: iload 8
    //   201: istore 13
    //   203: aload_0
    //   204: astore 14
    //   206: aload 6
    //   208: aload_0
    //   209: invokevirtual 189	com/android/internal/util/ProcFileReader:nextString	()Ljava/lang/String;
    //   212: invokestatic 198	com/android/server/NetworkManagementSocketTagger:kernelToTag	(Ljava/lang/String;)I
    //   215: putfield 202	android/net/NetworkStats$Entry:tag	I
    //   218: aload_0
    //   219: astore 12
    //   221: iload 8
    //   223: istore 13
    //   225: aload_0
    //   226: astore 14
    //   228: aload 6
    //   230: aload_0
    //   231: invokevirtual 185	com/android/internal/util/ProcFileReader:nextInt	()I
    //   234: putfield 205	android/net/NetworkStats$Entry:uid	I
    //   237: aload_0
    //   238: astore 12
    //   240: iload 8
    //   242: istore 13
    //   244: aload_0
    //   245: astore 14
    //   247: aload 6
    //   249: aload_0
    //   250: invokevirtual 185	com/android/internal/util/ProcFileReader:nextInt	()I
    //   253: putfield 208	android/net/NetworkStats$Entry:set	I
    //   256: aload_0
    //   257: astore 12
    //   259: iload 8
    //   261: istore 13
    //   263: aload_0
    //   264: astore 14
    //   266: aload 6
    //   268: aload_0
    //   269: invokevirtual 211	com/android/internal/util/ProcFileReader:nextLong	()J
    //   272: putfield 215	android/net/NetworkStats$Entry:rxBytes	J
    //   275: aload_0
    //   276: astore 12
    //   278: iload 8
    //   280: istore 13
    //   282: aload_0
    //   283: astore 14
    //   285: aload 6
    //   287: aload_0
    //   288: invokevirtual 211	com/android/internal/util/ProcFileReader:nextLong	()J
    //   291: putfield 218	android/net/NetworkStats$Entry:rxPackets	J
    //   294: aload_0
    //   295: astore 12
    //   297: iload 8
    //   299: istore 13
    //   301: aload_0
    //   302: astore 14
    //   304: aload 6
    //   306: aload_0
    //   307: invokevirtual 211	com/android/internal/util/ProcFileReader:nextLong	()J
    //   310: putfield 221	android/net/NetworkStats$Entry:txBytes	J
    //   313: aload_0
    //   314: astore 12
    //   316: iload 8
    //   318: istore 13
    //   320: aload_0
    //   321: astore 14
    //   323: aload 6
    //   325: aload_0
    //   326: invokevirtual 211	com/android/internal/util/ProcFileReader:nextLong	()J
    //   329: putfield 224	android/net/NetworkStats$Entry:txPackets	J
    //   332: aload_2
    //   333: ifnull +25 -> 358
    //   336: aload_0
    //   337: astore 12
    //   339: iload 8
    //   341: istore 13
    //   343: aload_0
    //   344: astore 14
    //   346: aload_2
    //   347: aload 6
    //   349: getfield 192	android/net/NetworkStats$Entry:iface	Ljava/lang/String;
    //   352: invokestatic 229	com/android/internal/util/ArrayUtils:contains	([Ljava/lang/Object;Ljava/lang/Object;)Z
    //   355: ifeq +69 -> 424
    //   358: iload_1
    //   359: iconst_m1
    //   360: if_icmpeq +22 -> 382
    //   363: aload_0
    //   364: astore 12
    //   366: iload 8
    //   368: istore 13
    //   370: aload_0
    //   371: astore 14
    //   373: iload_1
    //   374: aload 6
    //   376: getfield 205	android/net/NetworkStats$Entry:uid	I
    //   379: if_icmpne +45 -> 424
    //   382: iload_3
    //   383: iconst_m1
    //   384: if_icmpeq +22 -> 406
    //   387: aload_0
    //   388: astore 12
    //   390: iload 8
    //   392: istore 13
    //   394: aload_0
    //   395: astore 14
    //   397: iload_3
    //   398: aload 6
    //   400: getfield 202	android/net/NetworkStats$Entry:tag	I
    //   403: if_icmpne +21 -> 424
    //   406: aload_0
    //   407: astore 12
    //   409: iload 8
    //   411: istore 13
    //   413: aload_0
    //   414: astore 14
    //   416: aload 5
    //   418: aload 6
    //   420: invokevirtual 233	android/net/NetworkStats:addValues	(Landroid/net/NetworkStats$Entry;)Landroid/net/NetworkStats;
    //   423: pop
    //   424: aload_0
    //   425: astore 12
    //   427: iload 8
    //   429: istore 13
    //   431: aload_0
    //   432: astore 14
    //   434: aload_0
    //   435: invokevirtual 179	com/android/internal/util/ProcFileReader:finishLine	()V
    //   438: goto -307 -> 131
    //   441: aload_0
    //   442: astore 12
    //   444: iload 8
    //   446: istore 13
    //   448: aload_0
    //   449: astore 14
    //   451: new 235	java/net/ProtocolException
    //   454: astore_2
    //   455: aload_0
    //   456: astore 12
    //   458: iload 8
    //   460: istore 13
    //   462: aload_0
    //   463: astore 14
    //   465: new 237	java/lang/StringBuilder
    //   468: astore 11
    //   470: aload_0
    //   471: astore 12
    //   473: iload 8
    //   475: istore 13
    //   477: aload_0
    //   478: astore 14
    //   480: aload 11
    //   482: invokespecial 238	java/lang/StringBuilder:<init>	()V
    //   485: aload_0
    //   486: astore 12
    //   488: iload 8
    //   490: istore 13
    //   492: aload_0
    //   493: astore 14
    //   495: aload 11
    //   497: ldc -16
    //   499: invokevirtual 244	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   502: pop
    //   503: aload_0
    //   504: astore 12
    //   506: iload 8
    //   508: istore 13
    //   510: aload_0
    //   511: astore 14
    //   513: aload 11
    //   515: iload 8
    //   517: invokevirtual 247	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   520: pop
    //   521: aload_0
    //   522: astore 12
    //   524: iload 8
    //   526: istore 13
    //   528: aload_0
    //   529: astore 14
    //   531: aload 11
    //   533: ldc -7
    //   535: invokevirtual 244	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   538: pop
    //   539: aload_0
    //   540: astore 12
    //   542: iload 8
    //   544: istore 13
    //   546: aload_0
    //   547: astore 14
    //   549: aload 11
    //   551: iload 9
    //   553: invokevirtual 247	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   556: pop
    //   557: aload_0
    //   558: astore 12
    //   560: iload 8
    //   562: istore 13
    //   564: aload_0
    //   565: astore 14
    //   567: aload_2
    //   568: aload 11
    //   570: invokevirtual 252	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   573: invokespecial 253	java/net/ProtocolException:<init>	(Ljava/lang/String;)V
    //   576: aload_0
    //   577: astore 12
    //   579: iload 8
    //   581: istore 13
    //   583: aload_0
    //   584: astore 14
    //   586: aload_2
    //   587: athrow
    //   588: aload_0
    //   589: invokestatic 259	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   592: aload 4
    //   594: invokestatic 263	android/os/StrictMode:setThreadPolicy	(Landroid/os/StrictMode$ThreadPolicy;)V
    //   597: aload 5
    //   599: areturn
    //   600: astore_0
    //   601: goto +75 -> 676
    //   604: astore_2
    //   605: aload 14
    //   607: astore 12
    //   609: new 235	java/net/ProtocolException
    //   612: astore_0
    //   613: aload 14
    //   615: astore 12
    //   617: new 237	java/lang/StringBuilder
    //   620: astore 11
    //   622: aload 14
    //   624: astore 12
    //   626: aload 11
    //   628: invokespecial 238	java/lang/StringBuilder:<init>	()V
    //   631: aload 14
    //   633: astore 12
    //   635: aload 11
    //   637: ldc_w 265
    //   640: invokevirtual 244	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   643: pop
    //   644: aload 14
    //   646: astore 12
    //   648: aload 11
    //   650: iload 13
    //   652: invokevirtual 247	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   655: pop
    //   656: aload 14
    //   658: astore 12
    //   660: aload_0
    //   661: aload 11
    //   663: invokevirtual 252	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   666: aload_2
    //   667: invokespecial 268	java/net/ProtocolException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   670: aload 14
    //   672: astore 12
    //   674: aload_0
    //   675: athrow
    //   676: aload 12
    //   678: invokestatic 259	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   681: aload 4
    //   683: invokestatic 263	android/os/StrictMode:setThreadPolicy	(Landroid/os/StrictMode$ThreadPolicy;)V
    //   686: aload_0
    //   687: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	688	0	paramFile	File
    //   0	688	1	paramInt1	int
    //   0	688	2	paramArrayOfString	String[]
    //   0	688	3	paramInt2	int
    //   3	679	4	localThreadPolicy	android.os.StrictMode.ThreadPolicy
    //   17	581	5	localNetworkStats	NetworkStats
    //   26	393	6	localEntry	NetworkStats.Entry
    //   29	92	7	i	int
    //   32	548	8	j	int
    //   35	517	9	k	int
    //   38	66	10	localObject1	Object
    //   41	621	11	localStringBuilder	StringBuilder
    //   45	632	12	localObject2	Object
    //   49	602	13	m	int
    //   53	618	14	localObject3	Object
    //   58	57	15	localProcFileReader	com.android.internal.util.ProcFileReader
    //   75	35	16	localFileInputStream	java.io.FileInputStream
    // Exception table:
    //   from	to	target	type
    //   55	60	600	finally
    //   72	77	600	finally
    //   89	95	600	finally
    //   107	114	600	finally
    //   127	131	600	finally
    //   141	148	600	finally
    //   158	164	600	finally
    //   187	196	600	finally
    //   206	218	600	finally
    //   228	237	600	finally
    //   247	256	600	finally
    //   266	275	600	finally
    //   285	294	600	finally
    //   304	313	600	finally
    //   323	332	600	finally
    //   346	358	600	finally
    //   373	382	600	finally
    //   397	406	600	finally
    //   416	424	600	finally
    //   434	438	600	finally
    //   451	455	600	finally
    //   465	470	600	finally
    //   480	485	600	finally
    //   495	503	600	finally
    //   513	521	600	finally
    //   531	539	600	finally
    //   549	557	600	finally
    //   567	576	600	finally
    //   586	588	600	finally
    //   609	613	600	finally
    //   617	622	600	finally
    //   626	631	600	finally
    //   635	644	600	finally
    //   648	656	600	finally
    //   660	670	600	finally
    //   674	676	600	finally
    //   55	60	604	java/lang/NullPointerException
    //   55	60	604	java/lang/NumberFormatException
    //   72	77	604	java/lang/NullPointerException
    //   72	77	604	java/lang/NumberFormatException
    //   89	95	604	java/lang/NullPointerException
    //   89	95	604	java/lang/NumberFormatException
    //   107	114	604	java/lang/NullPointerException
    //   107	114	604	java/lang/NumberFormatException
    //   127	131	604	java/lang/NullPointerException
    //   127	131	604	java/lang/NumberFormatException
    //   141	148	604	java/lang/NullPointerException
    //   141	148	604	java/lang/NumberFormatException
    //   158	164	604	java/lang/NullPointerException
    //   158	164	604	java/lang/NumberFormatException
    //   187	196	604	java/lang/NullPointerException
    //   187	196	604	java/lang/NumberFormatException
    //   206	218	604	java/lang/NullPointerException
    //   206	218	604	java/lang/NumberFormatException
    //   228	237	604	java/lang/NullPointerException
    //   228	237	604	java/lang/NumberFormatException
    //   247	256	604	java/lang/NullPointerException
    //   247	256	604	java/lang/NumberFormatException
    //   266	275	604	java/lang/NullPointerException
    //   266	275	604	java/lang/NumberFormatException
    //   285	294	604	java/lang/NullPointerException
    //   285	294	604	java/lang/NumberFormatException
    //   304	313	604	java/lang/NullPointerException
    //   304	313	604	java/lang/NumberFormatException
    //   323	332	604	java/lang/NullPointerException
    //   323	332	604	java/lang/NumberFormatException
    //   346	358	604	java/lang/NullPointerException
    //   346	358	604	java/lang/NumberFormatException
    //   373	382	604	java/lang/NullPointerException
    //   373	382	604	java/lang/NumberFormatException
    //   397	406	604	java/lang/NullPointerException
    //   397	406	604	java/lang/NumberFormatException
    //   416	424	604	java/lang/NullPointerException
    //   416	424	604	java/lang/NumberFormatException
    //   434	438	604	java/lang/NullPointerException
    //   434	438	604	java/lang/NumberFormatException
    //   451	455	604	java/lang/NullPointerException
    //   451	455	604	java/lang/NumberFormatException
    //   465	470	604	java/lang/NullPointerException
    //   465	470	604	java/lang/NumberFormatException
    //   480	485	604	java/lang/NullPointerException
    //   480	485	604	java/lang/NumberFormatException
    //   495	503	604	java/lang/NullPointerException
    //   495	503	604	java/lang/NumberFormatException
    //   513	521	604	java/lang/NullPointerException
    //   513	521	604	java/lang/NumberFormatException
    //   531	539	604	java/lang/NullPointerException
    //   531	539	604	java/lang/NumberFormatException
    //   549	557	604	java/lang/NullPointerException
    //   549	557	604	java/lang/NumberFormatException
    //   567	576	604	java/lang/NullPointerException
    //   567	576	604	java/lang/NumberFormatException
    //   586	588	604	java/lang/NullPointerException
    //   586	588	604	java/lang/NumberFormatException
  }
  
  @VisibleForTesting
  public static native int nativeReadNetworkStatsDetail(NetworkStats paramNetworkStats, String paramString, int paramInt1, String[] paramArrayOfString, int paramInt2, boolean paramBoolean);
  
  @VisibleForTesting
  public static native int nativeReadNetworkStatsDev(NetworkStats paramNetworkStats);
  
  public static void noteStackedIface(String paramString1, String paramString2)
  {
    if ((paramString1 != null) && (paramString2 != null)) {
      sStackedIfaces.put(paramString1, paramString2);
    }
  }
  
  private NetworkStats readNetworkStatsDetailInternal(int paramInt1, String[] paramArrayOfString, int paramInt2, NetworkStats paramNetworkStats)
    throws IOException
  {
    if (paramNetworkStats != null) {
      paramNetworkStats.setElapsedRealtime(SystemClock.elapsedRealtime());
    } else {
      paramNetworkStats = new NetworkStats(SystemClock.elapsedRealtime(), -1);
    }
    if (nativeReadNetworkStatsDetail(paramNetworkStats, mStatsXtUid.getAbsolutePath(), paramInt1, paramArrayOfString, paramInt2, mUseBpfStats) == 0) {
      return paramNetworkStats;
    }
    throw new IOException("Failed to parse network stats");
  }
  
  public void assertEquals(NetworkStats paramNetworkStats1, NetworkStats paramNetworkStats2)
  {
    if (paramNetworkStats1.size() == paramNetworkStats2.size())
    {
      NetworkStats.Entry localEntry = null;
      localObject = null;
      int i = 0;
      while (i < paramNetworkStats1.size())
      {
        localEntry = paramNetworkStats1.getValues(i, localEntry);
        localObject = paramNetworkStats2.getValues(i, (NetworkStats.Entry)localObject);
        if (localEntry.equals(localObject))
        {
          i++;
        }
        else
        {
          paramNetworkStats1 = new StringBuilder();
          paramNetworkStats1.append("Expected row ");
          paramNetworkStats1.append(i);
          paramNetworkStats1.append(": ");
          paramNetworkStats1.append(localEntry);
          paramNetworkStats1.append(", actual row ");
          paramNetworkStats1.append(localObject);
          throw new AssertionError(paramNetworkStats1.toString());
        }
      }
      return;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Expected size ");
    ((StringBuilder)localObject).append(paramNetworkStats1.size());
    ((StringBuilder)localObject).append(", actual size ");
    ((StringBuilder)localObject).append(paramNetworkStats2.size());
    throw new AssertionError(((StringBuilder)localObject).toString());
  }
  
  public NetworkStats readBpfNetworkStatsDev()
    throws IOException
  {
    NetworkStats localNetworkStats = new NetworkStats(SystemClock.elapsedRealtime(), 6);
    if (nativeReadNetworkStatsDev(localNetworkStats) == 0) {
      return localNetworkStats;
    }
    throw new IOException("Failed to parse bpf iface stats");
  }
  
  public NetworkStats readNetworkStatsDetail()
    throws IOException
  {
    return readNetworkStatsDetail(-1, null, -1, null);
  }
  
  public NetworkStats readNetworkStatsDetail(int paramInt1, String[] paramArrayOfString, int paramInt2, NetworkStats paramNetworkStats)
    throws IOException
  {
    paramArrayOfString = readNetworkStatsDetailInternal(paramInt1, paramArrayOfString, paramInt2, paramNetworkStats);
    paramArrayOfString.apply464xlatAdjustments(sStackedIfaces);
    return paramArrayOfString;
  }
  
  /* Error */
  public NetworkStats readNetworkStatsSummaryDev()
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 66	com/android/internal/net/NetworkStatsFactory:mUseBpfStats	Z
    //   4: ifeq +8 -> 12
    //   7: aload_0
    //   8: invokevirtual 337	com/android/internal/net/NetworkStatsFactory:readBpfNetworkStatsDev	()Landroid/net/NetworkStats;
    //   11: areturn
    //   12: invokestatic 154	android/os/StrictMode:allowThreadDiskReads	()Landroid/os/StrictMode$ThreadPolicy;
    //   15: astore_1
    //   16: new 71	android/net/NetworkStats
    //   19: dup
    //   20: invokestatic 160	android/os/SystemClock:elapsedRealtime	()J
    //   23: bipush 6
    //   25: invokespecial 163	android/net/NetworkStats:<init>	(JI)V
    //   28: astore_2
    //   29: new 165	android/net/NetworkStats$Entry
    //   32: dup
    //   33: invokespecial 166	android/net/NetworkStats$Entry:<init>	()V
    //   36: astore_3
    //   37: aconst_null
    //   38: astore 4
    //   40: aconst_null
    //   41: astore 5
    //   43: aload 5
    //   45: astore 6
    //   47: aload 4
    //   49: astore 7
    //   51: new 168	com/android/internal/util/ProcFileReader
    //   54: astore 8
    //   56: aload 5
    //   58: astore 6
    //   60: aload 4
    //   62: astore 7
    //   64: new 170	java/io/FileInputStream
    //   67: astore 9
    //   69: aload 5
    //   71: astore 6
    //   73: aload 4
    //   75: astore 7
    //   77: aload 9
    //   79: aload_0
    //   80: getfield 56	com/android/internal/net/NetworkStatsFactory:mStatsXtIfaceAll	Ljava/io/File;
    //   83: invokespecial 173	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   86: aload 5
    //   88: astore 6
    //   90: aload 4
    //   92: astore 7
    //   94: aload 8
    //   96: aload 9
    //   98: invokespecial 176	com/android/internal/util/ProcFileReader:<init>	(Ljava/io/InputStream;)V
    //   101: aload 8
    //   103: astore 5
    //   105: aload 5
    //   107: astore 6
    //   109: aload 5
    //   111: astore 7
    //   113: aload 5
    //   115: invokevirtual 182	com/android/internal/util/ProcFileReader:hasMoreData	()Z
    //   118: ifeq +272 -> 390
    //   121: aload 5
    //   123: astore 6
    //   125: aload 5
    //   127: astore 7
    //   129: aload_3
    //   130: aload 5
    //   132: invokevirtual 189	com/android/internal/util/ProcFileReader:nextString	()Ljava/lang/String;
    //   135: putfield 192	android/net/NetworkStats$Entry:iface	Ljava/lang/String;
    //   138: aload 5
    //   140: astore 6
    //   142: aload 5
    //   144: astore 7
    //   146: aload_3
    //   147: iconst_m1
    //   148: putfield 205	android/net/NetworkStats$Entry:uid	I
    //   151: aload 5
    //   153: astore 6
    //   155: aload 5
    //   157: astore 7
    //   159: aload_3
    //   160: iconst_m1
    //   161: putfield 208	android/net/NetworkStats$Entry:set	I
    //   164: iconst_0
    //   165: istore 10
    //   167: aload 5
    //   169: astore 6
    //   171: aload 5
    //   173: astore 7
    //   175: aload_3
    //   176: iconst_0
    //   177: putfield 202	android/net/NetworkStats$Entry:tag	I
    //   180: aload 5
    //   182: astore 6
    //   184: aload 5
    //   186: astore 7
    //   188: aload 5
    //   190: invokevirtual 185	com/android/internal/util/ProcFileReader:nextInt	()I
    //   193: ifeq +6 -> 199
    //   196: iconst_1
    //   197: istore 10
    //   199: aload 5
    //   201: astore 6
    //   203: aload 5
    //   205: astore 7
    //   207: aload_3
    //   208: aload 5
    //   210: invokevirtual 211	com/android/internal/util/ProcFileReader:nextLong	()J
    //   213: putfield 215	android/net/NetworkStats$Entry:rxBytes	J
    //   216: aload 5
    //   218: astore 6
    //   220: aload 5
    //   222: astore 7
    //   224: aload_3
    //   225: aload 5
    //   227: invokevirtual 211	com/android/internal/util/ProcFileReader:nextLong	()J
    //   230: putfield 218	android/net/NetworkStats$Entry:rxPackets	J
    //   233: aload 5
    //   235: astore 6
    //   237: aload 5
    //   239: astore 7
    //   241: aload_3
    //   242: aload 5
    //   244: invokevirtual 211	com/android/internal/util/ProcFileReader:nextLong	()J
    //   247: putfield 221	android/net/NetworkStats$Entry:txBytes	J
    //   250: aload 5
    //   252: astore 6
    //   254: aload 5
    //   256: astore 7
    //   258: aload_3
    //   259: aload 5
    //   261: invokevirtual 211	com/android/internal/util/ProcFileReader:nextLong	()J
    //   264: putfield 224	android/net/NetworkStats$Entry:txPackets	J
    //   267: iload 10
    //   269: ifeq +91 -> 360
    //   272: aload 5
    //   274: astore 6
    //   276: aload 5
    //   278: astore 7
    //   280: aload_3
    //   281: aload_3
    //   282: getfield 215	android/net/NetworkStats$Entry:rxBytes	J
    //   285: aload 5
    //   287: invokevirtual 211	com/android/internal/util/ProcFileReader:nextLong	()J
    //   290: ladd
    //   291: putfield 215	android/net/NetworkStats$Entry:rxBytes	J
    //   294: aload 5
    //   296: astore 6
    //   298: aload 5
    //   300: astore 7
    //   302: aload_3
    //   303: aload_3
    //   304: getfield 218	android/net/NetworkStats$Entry:rxPackets	J
    //   307: aload 5
    //   309: invokevirtual 211	com/android/internal/util/ProcFileReader:nextLong	()J
    //   312: ladd
    //   313: putfield 218	android/net/NetworkStats$Entry:rxPackets	J
    //   316: aload 5
    //   318: astore 6
    //   320: aload 5
    //   322: astore 7
    //   324: aload_3
    //   325: aload_3
    //   326: getfield 221	android/net/NetworkStats$Entry:txBytes	J
    //   329: aload 5
    //   331: invokevirtual 211	com/android/internal/util/ProcFileReader:nextLong	()J
    //   334: ladd
    //   335: putfield 221	android/net/NetworkStats$Entry:txBytes	J
    //   338: aload 5
    //   340: astore 6
    //   342: aload 5
    //   344: astore 7
    //   346: aload_3
    //   347: aload_3
    //   348: getfield 224	android/net/NetworkStats$Entry:txPackets	J
    //   351: aload 5
    //   353: invokevirtual 211	com/android/internal/util/ProcFileReader:nextLong	()J
    //   356: ladd
    //   357: putfield 224	android/net/NetworkStats$Entry:txPackets	J
    //   360: aload 5
    //   362: astore 6
    //   364: aload 5
    //   366: astore 7
    //   368: aload_2
    //   369: aload_3
    //   370: invokevirtual 233	android/net/NetworkStats:addValues	(Landroid/net/NetworkStats$Entry;)Landroid/net/NetworkStats;
    //   373: pop
    //   374: aload 5
    //   376: astore 6
    //   378: aload 5
    //   380: astore 7
    //   382: aload 5
    //   384: invokevirtual 179	com/android/internal/util/ProcFileReader:finishLine	()V
    //   387: goto -282 -> 105
    //   390: aload 5
    //   392: invokestatic 259	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   395: aload_1
    //   396: invokestatic 263	android/os/StrictMode:setThreadPolicy	(Landroid/os/StrictMode$ThreadPolicy;)V
    //   399: aload_2
    //   400: areturn
    //   401: astore 7
    //   403: goto +35 -> 438
    //   406: astore 8
    //   408: aload 7
    //   410: astore 6
    //   412: new 235	java/net/ProtocolException
    //   415: astore 5
    //   417: aload 7
    //   419: astore 6
    //   421: aload 5
    //   423: ldc_w 339
    //   426: aload 8
    //   428: invokespecial 268	java/net/ProtocolException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   431: aload 7
    //   433: astore 6
    //   435: aload 5
    //   437: athrow
    //   438: aload 6
    //   440: invokestatic 259	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   443: aload_1
    //   444: invokestatic 263	android/os/StrictMode:setThreadPolicy	(Landroid/os/StrictMode$ThreadPolicy;)V
    //   447: aload 7
    //   449: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	450	0	this	NetworkStatsFactory
    //   15	429	1	localThreadPolicy	android.os.StrictMode.ThreadPolicy
    //   28	372	2	localNetworkStats	NetworkStats
    //   36	334	3	localEntry	NetworkStats.Entry
    //   38	53	4	localObject1	Object
    //   41	395	5	localObject2	Object
    //   45	394	6	localObject3	Object
    //   49	332	7	localObject4	Object
    //   401	47	7	localObject5	Object
    //   54	48	8	localProcFileReader	com.android.internal.util.ProcFileReader
    //   406	21	8	localNullPointerException	NullPointerException
    //   67	30	9	localFileInputStream	java.io.FileInputStream
    //   165	103	10	i	int
    // Exception table:
    //   from	to	target	type
    //   51	56	401	finally
    //   64	69	401	finally
    //   77	86	401	finally
    //   94	101	401	finally
    //   113	121	401	finally
    //   129	138	401	finally
    //   146	151	401	finally
    //   159	164	401	finally
    //   175	180	401	finally
    //   188	196	401	finally
    //   207	216	401	finally
    //   224	233	401	finally
    //   241	250	401	finally
    //   258	267	401	finally
    //   280	294	401	finally
    //   302	316	401	finally
    //   324	338	401	finally
    //   346	360	401	finally
    //   368	374	401	finally
    //   382	387	401	finally
    //   412	417	401	finally
    //   421	431	401	finally
    //   435	438	401	finally
    //   51	56	406	java/lang/NullPointerException
    //   51	56	406	java/lang/NumberFormatException
    //   64	69	406	java/lang/NullPointerException
    //   64	69	406	java/lang/NumberFormatException
    //   77	86	406	java/lang/NullPointerException
    //   77	86	406	java/lang/NumberFormatException
    //   94	101	406	java/lang/NullPointerException
    //   94	101	406	java/lang/NumberFormatException
    //   113	121	406	java/lang/NullPointerException
    //   113	121	406	java/lang/NumberFormatException
    //   129	138	406	java/lang/NullPointerException
    //   129	138	406	java/lang/NumberFormatException
    //   146	151	406	java/lang/NullPointerException
    //   146	151	406	java/lang/NumberFormatException
    //   159	164	406	java/lang/NullPointerException
    //   159	164	406	java/lang/NumberFormatException
    //   175	180	406	java/lang/NullPointerException
    //   175	180	406	java/lang/NumberFormatException
    //   188	196	406	java/lang/NullPointerException
    //   188	196	406	java/lang/NumberFormatException
    //   207	216	406	java/lang/NullPointerException
    //   207	216	406	java/lang/NumberFormatException
    //   224	233	406	java/lang/NullPointerException
    //   224	233	406	java/lang/NumberFormatException
    //   241	250	406	java/lang/NullPointerException
    //   241	250	406	java/lang/NumberFormatException
    //   258	267	406	java/lang/NullPointerException
    //   258	267	406	java/lang/NumberFormatException
    //   280	294	406	java/lang/NullPointerException
    //   280	294	406	java/lang/NumberFormatException
    //   302	316	406	java/lang/NullPointerException
    //   302	316	406	java/lang/NumberFormatException
    //   324	338	406	java/lang/NullPointerException
    //   324	338	406	java/lang/NumberFormatException
    //   346	360	406	java/lang/NullPointerException
    //   346	360	406	java/lang/NumberFormatException
    //   368	374	406	java/lang/NullPointerException
    //   368	374	406	java/lang/NumberFormatException
    //   382	387	406	java/lang/NullPointerException
    //   382	387	406	java/lang/NumberFormatException
  }
  
  /* Error */
  public NetworkStats readNetworkStatsSummaryXt()
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 66	com/android/internal/net/NetworkStatsFactory:mUseBpfStats	Z
    //   4: ifeq +8 -> 12
    //   7: aload_0
    //   8: invokevirtual 337	com/android/internal/net/NetworkStatsFactory:readBpfNetworkStatsDev	()Landroid/net/NetworkStats;
    //   11: areturn
    //   12: invokestatic 154	android/os/StrictMode:allowThreadDiskReads	()Landroid/os/StrictMode$ThreadPolicy;
    //   15: astore_1
    //   16: aload_0
    //   17: getfield 60	com/android/internal/net/NetworkStatsFactory:mStatsXtIfaceFmt	Ljava/io/File;
    //   20: invokevirtual 44	java/io/File:exists	()Z
    //   23: istore_2
    //   24: aconst_null
    //   25: astore_3
    //   26: aconst_null
    //   27: astore 4
    //   29: iload_2
    //   30: ifne +5 -> 35
    //   33: aconst_null
    //   34: areturn
    //   35: new 71	android/net/NetworkStats
    //   38: dup
    //   39: invokestatic 160	android/os/SystemClock:elapsedRealtime	()J
    //   42: bipush 6
    //   44: invokespecial 163	android/net/NetworkStats:<init>	(JI)V
    //   47: astore 5
    //   49: new 165	android/net/NetworkStats$Entry
    //   52: dup
    //   53: invokespecial 166	android/net/NetworkStats$Entry:<init>	()V
    //   56: astore 6
    //   58: aload 4
    //   60: astore 7
    //   62: aload_3
    //   63: astore 8
    //   65: new 168	com/android/internal/util/ProcFileReader
    //   68: astore 9
    //   70: aload 4
    //   72: astore 7
    //   74: aload_3
    //   75: astore 8
    //   77: new 170	java/io/FileInputStream
    //   80: astore 10
    //   82: aload 4
    //   84: astore 7
    //   86: aload_3
    //   87: astore 8
    //   89: aload 10
    //   91: aload_0
    //   92: getfield 60	com/android/internal/net/NetworkStatsFactory:mStatsXtIfaceFmt	Ljava/io/File;
    //   95: invokespecial 173	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   98: aload 4
    //   100: astore 7
    //   102: aload_3
    //   103: astore 8
    //   105: aload 9
    //   107: aload 10
    //   109: invokespecial 176	com/android/internal/util/ProcFileReader:<init>	(Ljava/io/InputStream;)V
    //   112: aload 9
    //   114: astore 4
    //   116: aload 4
    //   118: astore 7
    //   120: aload 4
    //   122: astore 8
    //   124: aload 4
    //   126: invokevirtual 179	com/android/internal/util/ProcFileReader:finishLine	()V
    //   129: aload 4
    //   131: astore 7
    //   133: aload 4
    //   135: astore 8
    //   137: aload 4
    //   139: invokevirtual 182	com/android/internal/util/ProcFileReader:hasMoreData	()Z
    //   142: ifeq +167 -> 309
    //   145: aload 4
    //   147: astore 7
    //   149: aload 4
    //   151: astore 8
    //   153: aload 6
    //   155: aload 4
    //   157: invokevirtual 189	com/android/internal/util/ProcFileReader:nextString	()Ljava/lang/String;
    //   160: putfield 192	android/net/NetworkStats$Entry:iface	Ljava/lang/String;
    //   163: aload 4
    //   165: astore 7
    //   167: aload 4
    //   169: astore 8
    //   171: aload 6
    //   173: iconst_m1
    //   174: putfield 205	android/net/NetworkStats$Entry:uid	I
    //   177: aload 4
    //   179: astore 7
    //   181: aload 4
    //   183: astore 8
    //   185: aload 6
    //   187: iconst_m1
    //   188: putfield 208	android/net/NetworkStats$Entry:set	I
    //   191: aload 4
    //   193: astore 7
    //   195: aload 4
    //   197: astore 8
    //   199: aload 6
    //   201: iconst_0
    //   202: putfield 202	android/net/NetworkStats$Entry:tag	I
    //   205: aload 4
    //   207: astore 7
    //   209: aload 4
    //   211: astore 8
    //   213: aload 6
    //   215: aload 4
    //   217: invokevirtual 211	com/android/internal/util/ProcFileReader:nextLong	()J
    //   220: putfield 215	android/net/NetworkStats$Entry:rxBytes	J
    //   223: aload 4
    //   225: astore 7
    //   227: aload 4
    //   229: astore 8
    //   231: aload 6
    //   233: aload 4
    //   235: invokevirtual 211	com/android/internal/util/ProcFileReader:nextLong	()J
    //   238: putfield 218	android/net/NetworkStats$Entry:rxPackets	J
    //   241: aload 4
    //   243: astore 7
    //   245: aload 4
    //   247: astore 8
    //   249: aload 6
    //   251: aload 4
    //   253: invokevirtual 211	com/android/internal/util/ProcFileReader:nextLong	()J
    //   256: putfield 221	android/net/NetworkStats$Entry:txBytes	J
    //   259: aload 4
    //   261: astore 7
    //   263: aload 4
    //   265: astore 8
    //   267: aload 6
    //   269: aload 4
    //   271: invokevirtual 211	com/android/internal/util/ProcFileReader:nextLong	()J
    //   274: putfield 224	android/net/NetworkStats$Entry:txPackets	J
    //   277: aload 4
    //   279: astore 7
    //   281: aload 4
    //   283: astore 8
    //   285: aload 5
    //   287: aload 6
    //   289: invokevirtual 233	android/net/NetworkStats:addValues	(Landroid/net/NetworkStats$Entry;)Landroid/net/NetworkStats;
    //   292: pop
    //   293: aload 4
    //   295: astore 7
    //   297: aload 4
    //   299: astore 8
    //   301: aload 4
    //   303: invokevirtual 179	com/android/internal/util/ProcFileReader:finishLine	()V
    //   306: goto -177 -> 129
    //   309: aload 4
    //   311: invokestatic 259	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   314: aload_1
    //   315: invokestatic 263	android/os/StrictMode:setThreadPolicy	(Landroid/os/StrictMode$ThreadPolicy;)V
    //   318: aload 5
    //   320: areturn
    //   321: astore 8
    //   323: goto +35 -> 358
    //   326: astore 9
    //   328: aload 8
    //   330: astore 7
    //   332: new 235	java/net/ProtocolException
    //   335: astore 4
    //   337: aload 8
    //   339: astore 7
    //   341: aload 4
    //   343: ldc_w 339
    //   346: aload 9
    //   348: invokespecial 268	java/net/ProtocolException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   351: aload 8
    //   353: astore 7
    //   355: aload 4
    //   357: athrow
    //   358: aload 7
    //   360: invokestatic 259	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   363: aload_1
    //   364: invokestatic 263	android/os/StrictMode:setThreadPolicy	(Landroid/os/StrictMode$ThreadPolicy;)V
    //   367: aload 8
    //   369: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	370	0	this	NetworkStatsFactory
    //   15	349	1	localThreadPolicy	android.os.StrictMode.ThreadPolicy
    //   23	7	2	bool	boolean
    //   25	78	3	localObject1	Object
    //   27	329	4	localObject2	Object
    //   47	272	5	localNetworkStats	NetworkStats
    //   56	232	6	localEntry	NetworkStats.Entry
    //   60	299	7	localObject3	Object
    //   63	237	8	localObject4	Object
    //   321	47	8	localObject5	Object
    //   68	45	9	localProcFileReader	com.android.internal.util.ProcFileReader
    //   326	21	9	localNullPointerException	NullPointerException
    //   80	28	10	localFileInputStream	java.io.FileInputStream
    // Exception table:
    //   from	to	target	type
    //   65	70	321	finally
    //   77	82	321	finally
    //   89	98	321	finally
    //   105	112	321	finally
    //   124	129	321	finally
    //   137	145	321	finally
    //   153	163	321	finally
    //   171	177	321	finally
    //   185	191	321	finally
    //   199	205	321	finally
    //   213	223	321	finally
    //   231	241	321	finally
    //   249	259	321	finally
    //   267	277	321	finally
    //   285	293	321	finally
    //   301	306	321	finally
    //   332	337	321	finally
    //   341	351	321	finally
    //   355	358	321	finally
    //   65	70	326	java/lang/NullPointerException
    //   65	70	326	java/lang/NumberFormatException
    //   77	82	326	java/lang/NullPointerException
    //   77	82	326	java/lang/NumberFormatException
    //   89	98	326	java/lang/NullPointerException
    //   89	98	326	java/lang/NumberFormatException
    //   105	112	326	java/lang/NullPointerException
    //   105	112	326	java/lang/NumberFormatException
    //   124	129	326	java/lang/NullPointerException
    //   124	129	326	java/lang/NumberFormatException
    //   137	145	326	java/lang/NullPointerException
    //   137	145	326	java/lang/NumberFormatException
    //   153	163	326	java/lang/NullPointerException
    //   153	163	326	java/lang/NumberFormatException
    //   171	177	326	java/lang/NullPointerException
    //   171	177	326	java/lang/NumberFormatException
    //   185	191	326	java/lang/NullPointerException
    //   185	191	326	java/lang/NumberFormatException
    //   199	205	326	java/lang/NullPointerException
    //   199	205	326	java/lang/NumberFormatException
    //   213	223	326	java/lang/NullPointerException
    //   213	223	326	java/lang/NumberFormatException
    //   231	241	326	java/lang/NullPointerException
    //   231	241	326	java/lang/NumberFormatException
    //   249	259	326	java/lang/NullPointerException
    //   249	259	326	java/lang/NumberFormatException
    //   267	277	326	java/lang/NullPointerException
    //   267	277	326	java/lang/NumberFormatException
    //   285	293	326	java/lang/NullPointerException
    //   285	293	326	java/lang/NumberFormatException
    //   301	306	326	java/lang/NullPointerException
    //   301	306	326	java/lang/NumberFormatException
  }
}
