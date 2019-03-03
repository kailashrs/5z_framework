package com.android.internal.os;

import android.content.Context;
import com.android.internal.annotations.VisibleForTesting;
import java.util.HashMap;

public class PowerProfile
{
  private static final String ATTR_NAME = "name";
  private static final String CPU_CLUSTER_POWER_COUNT = "cpu.cluster_power.cluster";
  private static final String CPU_CORE_POWER_PREFIX = "cpu.core_power.cluster";
  private static final String CPU_CORE_SPEED_PREFIX = "cpu.core_speeds.cluster";
  private static final String CPU_PER_CLUSTER_CORE_COUNT = "cpu.clusters.cores";
  public static final String POWER_AMBIENT_DISPLAY = "ambient.on";
  public static final String POWER_AUDIO = "audio";
  public static final String POWER_BATTERY_CAPACITY = "battery.capacity";
  @Deprecated
  public static final String POWER_BLUETOOTH_ACTIVE = "bluetooth.active";
  @Deprecated
  public static final String POWER_BLUETOOTH_AT_CMD = "bluetooth.at";
  public static final String POWER_BLUETOOTH_CONTROLLER_IDLE = "bluetooth.controller.idle";
  public static final String POWER_BLUETOOTH_CONTROLLER_OPERATING_VOLTAGE = "bluetooth.controller.voltage";
  public static final String POWER_BLUETOOTH_CONTROLLER_RX = "bluetooth.controller.rx";
  public static final String POWER_BLUETOOTH_CONTROLLER_TX = "bluetooth.controller.tx";
  @Deprecated
  public static final String POWER_BLUETOOTH_ON = "bluetooth.on";
  public static final String POWER_CAMERA = "camera.avg";
  public static final String POWER_CPU_ACTIVE = "cpu.active";
  public static final String POWER_CPU_IDLE = "cpu.idle";
  public static final String POWER_CPU_SUSPEND = "cpu.suspend";
  public static final String POWER_FLASHLIGHT = "camera.flashlight";
  public static final String POWER_GPS_ON = "gps.on";
  public static final String POWER_GPS_OPERATING_VOLTAGE = "gps.voltage";
  public static final String POWER_GPS_SIGNAL_QUALITY_BASED = "gps.signalqualitybased";
  public static final String POWER_MEMORY = "memory.bandwidths";
  public static final String POWER_MODEM_CONTROLLER_IDLE = "modem.controller.idle";
  public static final String POWER_MODEM_CONTROLLER_OPERATING_VOLTAGE = "modem.controller.voltage";
  public static final String POWER_MODEM_CONTROLLER_RX = "modem.controller.rx";
  public static final String POWER_MODEM_CONTROLLER_SLEEP = "modem.controller.sleep";
  public static final String POWER_MODEM_CONTROLLER_TX = "modem.controller.tx";
  public static final String POWER_RADIO_ACTIVE = "radio.active";
  public static final String POWER_RADIO_ON = "radio.on";
  public static final String POWER_RADIO_SCANNING = "radio.scanning";
  public static final String POWER_SCREEN_FULL = "screen.full";
  public static final String POWER_SCREEN_ON = "screen.on";
  public static final String POWER_VIDEO = "video";
  public static final String POWER_WIFI_ACTIVE = "wifi.active";
  public static final String POWER_WIFI_BATCHED_SCAN = "wifi.batchedscan";
  public static final String POWER_WIFI_CONTROLLER_IDLE = "wifi.controller.idle";
  public static final String POWER_WIFI_CONTROLLER_OPERATING_VOLTAGE = "wifi.controller.voltage";
  public static final String POWER_WIFI_CONTROLLER_RX = "wifi.controller.rx";
  public static final String POWER_WIFI_CONTROLLER_TX = "wifi.controller.tx";
  public static final String POWER_WIFI_CONTROLLER_TX_LEVELS = "wifi.controller.tx_levels";
  public static final String POWER_WIFI_ON = "wifi.on";
  public static final String POWER_WIFI_SCAN = "wifi.scan";
  private static final String TAG_ARRAY = "array";
  private static final String TAG_ARRAYITEM = "value";
  private static final String TAG_DEVICE = "device";
  private static final String TAG_ITEM = "item";
  private static final Object sLock = new Object();
  static final HashMap<String, Double[]> sPowerArrayMap;
  static final HashMap<String, Double> sPowerItemMap = new HashMap();
  private CpuClusterKey[] mCpuClusters;
  
  static
  {
    sPowerArrayMap = new HashMap();
  }
  
  @VisibleForTesting
  public PowerProfile(Context paramContext)
  {
    this(paramContext, false);
  }
  
  @VisibleForTesting
  public PowerProfile(Context paramContext, boolean paramBoolean)
  {
    synchronized (sLock)
    {
      if ((sPowerItemMap.size() == 0) && (sPowerArrayMap.size() == 0)) {
        readPowerValuesFromXml(paramContext, paramBoolean);
      }
      initCpuClusters();
      return;
    }
  }
  
  private void initCpuClusters()
  {
    boolean bool = sPowerArrayMap.containsKey("cpu.clusters.cores");
    int i = 0;
    if (bool)
    {
      Double[] arrayOfDouble = (Double[])sPowerArrayMap.get("cpu.clusters.cores");
      mCpuClusters = new CpuClusterKey[arrayOfDouble.length];
      while (i < arrayOfDouble.length)
      {
        int j = (int)Math.round(arrayOfDouble[i].doubleValue());
        CpuClusterKey[] arrayOfCpuClusterKey = mCpuClusters;
        Object localObject = new StringBuilder();
        ((StringBuilder)localObject).append("cpu.core_speeds.cluster");
        ((StringBuilder)localObject).append(i);
        localObject = ((StringBuilder)localObject).toString();
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("cpu.cluster_power.cluster");
        localStringBuilder.append(i);
        String str = localStringBuilder.toString();
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("cpu.core_power.cluster");
        localStringBuilder.append(i);
        arrayOfCpuClusterKey[i] = new CpuClusterKey((String)localObject, str, localStringBuilder.toString(), j, null);
        i++;
      }
    }
    else
    {
      mCpuClusters = new CpuClusterKey[1];
      i = 1;
      if (sPowerItemMap.containsKey("cpu.clusters.cores")) {
        i = (int)Math.round(((Double)sPowerItemMap.get("cpu.clusters.cores")).doubleValue());
      }
      mCpuClusters[0] = new CpuClusterKey("cpu.core_speeds.cluster0", "cpu.cluster_power.cluster0", "cpu.core_power.cluster0", i, null);
    }
  }
  
  /* Error */
  private void readPowerValuesFromXml(Context paramContext, boolean paramBoolean)
  {
    // Byte code:
    //   0: iload_2
    //   1: ifeq +9 -> 10
    //   4: ldc -7
    //   6: istore_3
    //   7: goto +6 -> 13
    //   10: ldc -6
    //   12: istore_3
    //   13: aload_1
    //   14: invokevirtual 256	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   17: astore 4
    //   19: aload 4
    //   21: iload_3
    //   22: invokevirtual 262	android/content/res/Resources:getXml	(I)Landroid/content/res/XmlResourceParser;
    //   25: astore 5
    //   27: iconst_0
    //   28: istore 6
    //   30: new 264	java/util/ArrayList
    //   33: dup
    //   34: invokespecial 265	java/util/ArrayList:<init>	()V
    //   37: astore 7
    //   39: aconst_null
    //   40: astore_1
    //   41: aload 5
    //   43: ldc -104
    //   45: invokestatic 271	com/android/internal/util/XmlUtils:beginDocument	(Lorg/xmlpull/v1/XmlPullParser;Ljava/lang/String;)V
    //   48: aload 5
    //   50: invokestatic 275	com/android/internal/util/XmlUtils:nextElement	(Lorg/xmlpull/v1/XmlPullParser;)V
    //   53: aload 5
    //   55: invokeinterface 280 1 0
    //   60: astore 8
    //   62: aload 8
    //   64: ifnonnull +175 -> 239
    //   67: iload 6
    //   69: ifeq +27 -> 96
    //   72: getstatic 175	com/android/internal/os/PowerProfile:sPowerArrayMap	Ljava/util/HashMap;
    //   75: aload_1
    //   76: aload 7
    //   78: aload 7
    //   80: invokevirtual 281	java/util/ArrayList:size	()I
    //   83: anewarray 209	java/lang/Double
    //   86: invokevirtual 285	java/util/ArrayList:toArray	([Ljava/lang/Object;)[Ljava/lang/Object;
    //   89: checkcast 205	[Ljava/lang/Double;
    //   92: invokevirtual 289	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   95: pop
    //   96: aload 5
    //   98: invokeinterface 292 1 0
    //   103: iconst_4
    //   104: newarray int
    //   106: astore 9
    //   108: aload 9
    //   110: dup
    //   111: iconst_0
    //   112: ldc_w 293
    //   115: iastore
    //   116: dup
    //   117: iconst_1
    //   118: ldc_w 294
    //   121: iastore
    //   122: dup
    //   123: iconst_2
    //   124: ldc_w 295
    //   127: iastore
    //   128: dup
    //   129: iconst_3
    //   130: ldc_w 296
    //   133: iastore
    //   134: pop
    //   135: iconst_0
    //   136: istore_3
    //   137: iload_3
    //   138: aload 9
    //   140: arraylength
    //   141: if_icmpge +97 -> 238
    //   144: iconst_4
    //   145: anewarray 298	java/lang/String
    //   148: dup
    //   149: iconst_0
    //   150: ldc 44
    //   152: aastore
    //   153: dup
    //   154: iconst_1
    //   155: ldc 50
    //   157: aastore
    //   158: dup
    //   159: iconst_2
    //   160: ldc 53
    //   162: aastore
    //   163: dup
    //   164: iconst_3
    //   165: ldc 47
    //   167: aastore
    //   168: iload_3
    //   169: aaload
    //   170: astore_1
    //   171: getstatic 173	com/android/internal/os/PowerProfile:sPowerItemMap	Ljava/util/HashMap;
    //   174: aload_1
    //   175: invokevirtual 199	java/util/HashMap:containsKey	(Ljava/lang/Object;)Z
    //   178: ifeq +24 -> 202
    //   181: getstatic 173	com/android/internal/os/PowerProfile:sPowerItemMap	Ljava/util/HashMap;
    //   184: aload_1
    //   185: invokevirtual 203	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   188: checkcast 209	java/lang/Double
    //   191: invokevirtual 213	java/lang/Double:doubleValue	()D
    //   194: dconst_0
    //   195: dcmpl
    //   196: ifle +6 -> 202
    //   199: goto +33 -> 232
    //   202: aload 4
    //   204: aload 9
    //   206: iload_3
    //   207: iaload
    //   208: invokevirtual 302	android/content/res/Resources:getInteger	(I)I
    //   211: istore 6
    //   213: iload 6
    //   215: ifle +17 -> 232
    //   218: getstatic 173	com/android/internal/os/PowerProfile:sPowerItemMap	Ljava/util/HashMap;
    //   221: aload_1
    //   222: iload 6
    //   224: i2d
    //   225: invokestatic 306	java/lang/Double:valueOf	(D)Ljava/lang/Double;
    //   228: invokevirtual 289	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   231: pop
    //   232: iinc 3 1
    //   235: goto -98 -> 137
    //   238: return
    //   239: iload 6
    //   241: istore_3
    //   242: iload 6
    //   244: ifeq +42 -> 286
    //   247: iload 6
    //   249: istore_3
    //   250: aload 8
    //   252: ldc -107
    //   254: invokevirtual 309	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   257: ifne +29 -> 286
    //   260: getstatic 175	com/android/internal/os/PowerProfile:sPowerArrayMap	Ljava/util/HashMap;
    //   263: aload_1
    //   264: aload 7
    //   266: aload 7
    //   268: invokevirtual 281	java/util/ArrayList:size	()I
    //   271: anewarray 209	java/lang/Double
    //   274: invokevirtual 285	java/util/ArrayList:toArray	([Ljava/lang/Object;)[Ljava/lang/Object;
    //   277: checkcast 205	[Ljava/lang/Double;
    //   280: invokevirtual 289	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   283: pop
    //   284: iconst_0
    //   285: istore_3
    //   286: aload 8
    //   288: ldc -110
    //   290: invokevirtual 309	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   293: ifeq +26 -> 319
    //   296: iconst_1
    //   297: istore 6
    //   299: aload 7
    //   301: invokevirtual 312	java/util/ArrayList:clear	()V
    //   304: aload 5
    //   306: aconst_null
    //   307: ldc 13
    //   309: invokeinterface 316 3 0
    //   314: astore 10
    //   316: goto +156 -> 472
    //   319: aload 8
    //   321: ldc -101
    //   323: invokevirtual 309	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   326: ifne +19 -> 345
    //   329: iload_3
    //   330: istore 6
    //   332: aload_1
    //   333: astore 10
    //   335: aload 8
    //   337: ldc -107
    //   339: invokevirtual 309	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   342: ifeq +130 -> 472
    //   345: aconst_null
    //   346: astore 9
    //   348: iload_3
    //   349: ifne +15 -> 364
    //   352: aload 5
    //   354: aconst_null
    //   355: ldc 13
    //   357: invokeinterface 316 3 0
    //   362: astore 9
    //   364: iload_3
    //   365: istore 6
    //   367: aload_1
    //   368: astore 10
    //   370: aload 5
    //   372: invokeinterface 319 1 0
    //   377: iconst_4
    //   378: if_icmpne +94 -> 472
    //   381: aload 5
    //   383: invokeinterface 322 1 0
    //   388: astore 10
    //   390: dconst_0
    //   391: dstore 11
    //   393: aload 10
    //   395: invokestatic 325	java/lang/Double:valueOf	(Ljava/lang/String;)Ljava/lang/Double;
    //   398: invokevirtual 213	java/lang/Double:doubleValue	()D
    //   401: dstore 13
    //   403: dload 13
    //   405: dstore 11
    //   407: goto +5 -> 412
    //   410: astore 10
    //   412: aload 8
    //   414: ldc -101
    //   416: invokevirtual 309	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   419: ifeq +26 -> 445
    //   422: getstatic 173	com/android/internal/os/PowerProfile:sPowerItemMap	Ljava/util/HashMap;
    //   425: aload 9
    //   427: dload 11
    //   429: invokestatic 306	java/lang/Double:valueOf	(D)Ljava/lang/Double;
    //   432: invokevirtual 289	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   435: pop
    //   436: iload_3
    //   437: istore 6
    //   439: aload_1
    //   440: astore 10
    //   442: goto +30 -> 472
    //   445: iload_3
    //   446: istore 6
    //   448: aload_1
    //   449: astore 10
    //   451: iload_3
    //   452: ifeq +20 -> 472
    //   455: aload 7
    //   457: dload 11
    //   459: invokestatic 306	java/lang/Double:valueOf	(D)Ljava/lang/Double;
    //   462: invokevirtual 328	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   465: pop
    //   466: aload_1
    //   467: astore 10
    //   469: iload_3
    //   470: istore 6
    //   472: aload 10
    //   474: astore_1
    //   475: goto -427 -> 48
    //   478: astore_1
    //   479: goto +31 -> 510
    //   482: astore 9
    //   484: new 330	java/lang/RuntimeException
    //   487: astore_1
    //   488: aload_1
    //   489: aload 9
    //   491: invokespecial 333	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
    //   494: aload_1
    //   495: athrow
    //   496: astore 9
    //   498: new 330	java/lang/RuntimeException
    //   501: astore_1
    //   502: aload_1
    //   503: aload 9
    //   505: invokespecial 333	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
    //   508: aload_1
    //   509: athrow
    //   510: aload 5
    //   512: invokeinterface 292 1 0
    //   517: aload_1
    //   518: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	519	0	this	PowerProfile
    //   0	519	1	paramContext	Context
    //   0	519	2	paramBoolean	boolean
    //   6	464	3	i	int
    //   17	186	4	localResources	android.content.res.Resources
    //   25	486	5	localXmlResourceParser	android.content.res.XmlResourceParser
    //   28	443	6	j	int
    //   37	419	7	localArrayList	java.util.ArrayList
    //   60	353	8	str	String
    //   106	320	9	localObject1	Object
    //   482	8	9	localIOException	java.io.IOException
    //   496	8	9	localXmlPullParserException	org.xmlpull.v1.XmlPullParserException
    //   314	80	10	localObject2	Object
    //   410	1	10	localNumberFormatException	NumberFormatException
    //   440	33	10	localContext	Context
    //   391	67	11	d1	double
    //   401	3	13	d2	double
    // Exception table:
    //   from	to	target	type
    //   393	403	410	java/lang/NumberFormatException
    //   41	48	478	finally
    //   48	62	478	finally
    //   72	96	478	finally
    //   250	284	478	finally
    //   286	296	478	finally
    //   299	316	478	finally
    //   319	329	478	finally
    //   335	345	478	finally
    //   352	364	478	finally
    //   370	390	478	finally
    //   393	403	478	finally
    //   412	436	478	finally
    //   455	466	478	finally
    //   484	496	478	finally
    //   498	510	478	finally
    //   41	48	482	java/io/IOException
    //   48	62	482	java/io/IOException
    //   72	96	482	java/io/IOException
    //   250	284	482	java/io/IOException
    //   286	296	482	java/io/IOException
    //   299	316	482	java/io/IOException
    //   319	329	482	java/io/IOException
    //   335	345	482	java/io/IOException
    //   352	364	482	java/io/IOException
    //   370	390	482	java/io/IOException
    //   393	403	482	java/io/IOException
    //   412	436	482	java/io/IOException
    //   455	466	482	java/io/IOException
    //   41	48	496	org/xmlpull/v1/XmlPullParserException
    //   48	62	496	org/xmlpull/v1/XmlPullParserException
    //   72	96	496	org/xmlpull/v1/XmlPullParserException
    //   250	284	496	org/xmlpull/v1/XmlPullParserException
    //   286	296	496	org/xmlpull/v1/XmlPullParserException
    //   299	316	496	org/xmlpull/v1/XmlPullParserException
    //   319	329	496	org/xmlpull/v1/XmlPullParserException
    //   335	345	496	org/xmlpull/v1/XmlPullParserException
    //   352	364	496	org/xmlpull/v1/XmlPullParserException
    //   370	390	496	org/xmlpull/v1/XmlPullParserException
    //   393	403	496	org/xmlpull/v1/XmlPullParserException
    //   412	436	496	org/xmlpull/v1/XmlPullParserException
    //   455	466	496	org/xmlpull/v1/XmlPullParserException
  }
  
  public double getAveragePower(String paramString)
  {
    return getAveragePowerOrDefault(paramString, 0.0D);
  }
  
  public double getAveragePower(String paramString, int paramInt)
  {
    if (sPowerItemMap.containsKey(paramString)) {
      return ((Double)sPowerItemMap.get(paramString)).doubleValue();
    }
    if (sPowerArrayMap.containsKey(paramString))
    {
      paramString = (Double[])sPowerArrayMap.get(paramString);
      if ((paramString.length > paramInt) && (paramInt >= 0)) {
        return paramString[paramInt].doubleValue();
      }
      if ((paramInt >= 0) && (paramString.length != 0)) {
        return paramString[(paramString.length - 1)].doubleValue();
      }
      return 0.0D;
    }
    return 0.0D;
  }
  
  public double getAveragePowerForCpuCluster(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < mCpuClusters.length)) {
      return getAveragePower(mCpuClusters[paramInt].clusterPowerKey);
    }
    return 0.0D;
  }
  
  public double getAveragePowerForCpuCore(int paramInt1, int paramInt2)
  {
    if ((paramInt1 >= 0) && (paramInt1 < mCpuClusters.length)) {
      return getAveragePower(mCpuClusters[paramInt1].corePowerKey, paramInt2);
    }
    return 0.0D;
  }
  
  public double getAveragePowerOrDefault(String paramString, double paramDouble)
  {
    if (sPowerItemMap.containsKey(paramString)) {
      return ((Double)sPowerItemMap.get(paramString)).doubleValue();
    }
    if (sPowerArrayMap.containsKey(paramString)) {
      return ((Double[])sPowerArrayMap.get(paramString))[0].doubleValue();
    }
    return paramDouble;
  }
  
  public double getBatteryCapacity()
  {
    return getAveragePower("battery.capacity");
  }
  
  public int getNumCoresInCpuCluster(int paramInt)
  {
    return mCpuClusters[paramInt].numCpus;
  }
  
  public int getNumCpuClusters()
  {
    return mCpuClusters.length;
  }
  
  public int getNumElements(String paramString)
  {
    if (sPowerItemMap.containsKey(paramString)) {
      return 1;
    }
    if (sPowerArrayMap.containsKey(paramString)) {
      return ((Double[])sPowerArrayMap.get(paramString)).length;
    }
    return 0;
  }
  
  public int getNumSpeedStepsInCpuCluster(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < mCpuClusters.length))
    {
      if (sPowerArrayMap.containsKey(mCpuClusters[paramInt].freqKey)) {
        return ((Double[])sPowerArrayMap.get(mCpuClusters[paramInt].freqKey)).length;
      }
      return 1;
    }
    return 0;
  }
  
  public static class CpuClusterKey
  {
    private final String clusterPowerKey;
    private final String corePowerKey;
    private final String freqKey;
    private final int numCpus;
    
    private CpuClusterKey(String paramString1, String paramString2, String paramString3, int paramInt)
    {
      freqKey = paramString1;
      clusterPowerKey = paramString2;
      corePowerKey = paramString3;
      numCpus = paramInt;
    }
  }
}
