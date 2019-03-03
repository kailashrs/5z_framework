package android.net;

import android.os.SystemProperties;
import android.util.Log;
import java.util.Arrays;

public class SntpClient
{
  private static final String CnNtpServer = "cn.pool.ntp.org";
  private static final boolean DBG = true;
  private static final int NTP_LEAP_NOSYNC = 3;
  private static final int NTP_MODE_BROADCAST = 5;
  private static final int NTP_MODE_CLIENT = 3;
  private static final int NTP_MODE_SERVER = 4;
  private static final int NTP_PACKET_SIZE = 48;
  private static final int NTP_PORT = 123;
  private static final int NTP_STRATUM_DEATH = 0;
  private static final int NTP_STRATUM_MAX = 15;
  private static final int NTP_VERSION = 3;
  private static final long OFFSET_1900_TO_1970 = 2208988800L;
  private static final int ORIGINATE_TIME_OFFSET = 24;
  private static final int RECEIVE_TIME_OFFSET = 32;
  private static final int REFERENCE_TIME_OFFSET = 16;
  private static final String TAG = "SntpClient";
  private static final int TRANSMIT_TIME_OFFSET = 40;
  private long mNtpTime;
  private long mNtpTimeReference;
  private long mRoundTripTime;
  
  public SntpClient() {}
  
  private static void checkValidServerReply(byte paramByte1, byte paramByte2, int paramInt, long paramLong)
    throws SntpClient.InvalidServerReplyException
  {
    if (paramByte1 != 3)
    {
      if ((paramByte2 != 4) && (paramByte2 != 5))
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("untrusted mode: ");
        localStringBuilder.append(paramByte2);
        throw new InvalidServerReplyException(localStringBuilder.toString());
      }
      if ((paramInt != 0) && (paramInt <= 15))
      {
        if (paramLong != 0L) {
          return;
        }
        throw new InvalidServerReplyException("zero transmitTime");
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("untrusted stratum: ");
      localStringBuilder.append(paramInt);
      throw new InvalidServerReplyException(localStringBuilder.toString());
    }
    throw new InvalidServerReplyException("unsynchronized server");
  }
  
  private long read32(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramArrayOfByte[paramInt];
    int j = paramArrayOfByte[(paramInt + 1)];
    int k = paramArrayOfByte[(paramInt + 2)];
    int m = paramArrayOfByte[(paramInt + 3)];
    if ((i & 0x80) == 128) {
      paramInt = (i & 0x7F) + 128;
    } else {
      paramInt = i;
    }
    if ((j & 0x80) == 128) {
      j = (j & 0x7F) + 128;
    }
    if ((k & 0x80) == 128) {
      k = (k & 0x7F) + 128;
    }
    if ((m & 0x80) == 128) {
      m = 128 + (m & 0x7F);
    }
    return (paramInt << 24) + (j << 16) + (k << 8) + m;
  }
  
  private long readTimeStamp(byte[] paramArrayOfByte, int paramInt)
  {
    long l1 = read32(paramArrayOfByte, paramInt);
    long l2 = read32(paramArrayOfByte, paramInt + 4);
    if ((l1 == 0L) && (l2 == 0L)) {
      return 0L;
    }
    return (l1 - 2208988800L) * 1000L + 1000L * l2 / 4294967296L;
  }
  
  private void writeTimeStamp(byte[] paramArrayOfByte, int paramInt, long paramLong)
  {
    if (paramLong == 0L)
    {
      Arrays.fill(paramArrayOfByte, paramInt, paramInt + 8, (byte)0);
      return;
    }
    long l1 = paramLong / 1000L;
    long l2 = l1 + 2208988800L;
    int i = paramInt + 1;
    paramArrayOfByte[paramInt] = ((byte)(byte)(int)(l2 >> 24));
    paramInt = i + 1;
    paramArrayOfByte[i] = ((byte)(byte)(int)(l2 >> 16));
    int j = paramInt + 1;
    paramArrayOfByte[paramInt] = ((byte)(byte)(int)(l2 >> 8));
    i = j + 1;
    paramArrayOfByte[j] = ((byte)(byte)(int)(l2 >> 0));
    paramLong = 4294967296L * (paramLong - l1 * 1000L) / 1000L;
    paramInt = i + 1;
    paramArrayOfByte[i] = ((byte)(byte)(int)(paramLong >> 24));
    i = paramInt + 1;
    paramArrayOfByte[paramInt] = ((byte)(byte)(int)(paramLong >> 16));
    paramInt = i + 1;
    paramArrayOfByte[i] = ((byte)(byte)(int)(paramLong >> 8));
    paramArrayOfByte[paramInt] = ((byte)(byte)(int)(Math.random() * 255.0D));
  }
  
  public long getNtpTime()
  {
    return mNtpTime;
  }
  
  public long getNtpTimeReference()
  {
    return mNtpTimeReference;
  }
  
  public long getRoundTripTime()
  {
    return mRoundTripTime;
  }
  
  @Deprecated
  public boolean requestTime(String paramString, int paramInt)
  {
    Log.w("SntpClient", "Shame on you for calling the hidden API requestTime()!");
    return false;
  }
  
  public boolean requestTime(String paramString, int paramInt, Network paramNetwork)
  {
    paramNetwork.setPrivateDnsBypass(true);
    String str = paramString;
    if (SystemProperties.get("gsm.operator.iso-country", "").toLowerCase().contains("cn"))
    {
      str = paramString;
      if ("time.android.com".equals(paramString))
      {
        str = "cn.pool.ntp.org";
        paramString = new StringBuilder();
        paramString.append(" CN network, change host to:");
        paramString.append("cn.pool.ntp.org");
        Log.d("SntpClient", paramString.toString());
      }
    }
    try
    {
      paramString = paramNetwork.getByName(str);
      return requestTime(paramString, 123, paramInt, paramNetwork);
    }
    catch (Exception paramString)
    {
      EventLogTags.writeNtpFailure(str, paramString.toString());
      paramNetwork = new StringBuilder();
      paramNetwork.append("request time failed: ");
      paramNetwork.append(paramString);
      Log.d("SntpClient", paramNetwork.toString());
    }
    return false;
  }
  
  /* Error */
  public boolean requestTime(java.net.InetAddress paramInetAddress, int paramInt1, int paramInt2, Network paramNetwork)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 5
    //   3: aconst_null
    //   4: astore 6
    //   6: sipush 65345
    //   9: invokestatic 195	android/net/TrafficStats:getAndSetThreadStatsTag	(I)I
    //   12: istore 7
    //   14: aload 6
    //   16: astore 8
    //   18: aload 5
    //   20: astore 9
    //   22: new 197	java/net/DatagramSocket
    //   25: astore 10
    //   27: aload 6
    //   29: astore 8
    //   31: aload 5
    //   33: astore 9
    //   35: aload 10
    //   37: invokespecial 198	java/net/DatagramSocket:<init>	()V
    //   40: aload 10
    //   42: astore 8
    //   44: aload 10
    //   46: astore 9
    //   48: aload 4
    //   50: aload 10
    //   52: invokevirtual 202	android/net/Network:bindSocket	(Ljava/net/DatagramSocket;)V
    //   55: aload 10
    //   57: astore 8
    //   59: aload 10
    //   61: astore 9
    //   63: aload 10
    //   65: iload_3
    //   66: invokevirtual 206	java/net/DatagramSocket:setSoTimeout	(I)V
    //   69: aload 10
    //   71: astore 8
    //   73: aload 10
    //   75: astore 9
    //   77: bipush 48
    //   79: newarray byte
    //   81: astore 6
    //   83: aload 10
    //   85: astore 8
    //   87: aload 10
    //   89: astore 9
    //   91: new 208	java/net/DatagramPacket
    //   94: astore 5
    //   96: aload 10
    //   98: astore 8
    //   100: aload 10
    //   102: astore 9
    //   104: aload 6
    //   106: arraylength
    //   107: istore_3
    //   108: aload 10
    //   110: astore 4
    //   112: aload 5
    //   114: aload 6
    //   116: iload_3
    //   117: aload_1
    //   118: iload_2
    //   119: invokespecial 211	java/net/DatagramPacket:<init>	([BILjava/net/InetAddress;I)V
    //   122: aload 6
    //   124: iconst_0
    //   125: bipush 27
    //   127: i2b
    //   128: bastore
    //   129: aload 10
    //   131: astore 4
    //   133: invokestatic 216	java/lang/System:currentTimeMillis	()J
    //   136: lstore 11
    //   138: aload 10
    //   140: astore 4
    //   142: invokestatic 221	android/os/SystemClock:elapsedRealtime	()J
    //   145: lstore 13
    //   147: aload 10
    //   149: astore 4
    //   151: aload_0
    //   152: aload 6
    //   154: bipush 40
    //   156: lload 11
    //   158: invokespecial 223	android/net/SntpClient:writeTimeStamp	([BIJ)V
    //   161: aload 10
    //   163: astore 4
    //   165: aload 10
    //   167: aload 5
    //   169: invokevirtual 227	java/net/DatagramSocket:send	(Ljava/net/DatagramPacket;)V
    //   172: aload 10
    //   174: astore 4
    //   176: new 208	java/net/DatagramPacket
    //   179: astore 8
    //   181: aload 10
    //   183: astore 4
    //   185: aload 8
    //   187: aload 6
    //   189: aload 6
    //   191: arraylength
    //   192: invokespecial 230	java/net/DatagramPacket:<init>	([BI)V
    //   195: aload 10
    //   197: astore 4
    //   199: aload 10
    //   201: aload 8
    //   203: invokevirtual 233	java/net/DatagramSocket:receive	(Ljava/net/DatagramPacket;)V
    //   206: aload 10
    //   208: astore 4
    //   210: invokestatic 221	android/os/SystemClock:elapsedRealtime	()J
    //   213: lstore 15
    //   215: lload 11
    //   217: lload 15
    //   219: lload 13
    //   221: lsub
    //   222: ladd
    //   223: lstore 11
    //   225: aload 6
    //   227: iconst_0
    //   228: baload
    //   229: bipush 6
    //   231: ishr
    //   232: iconst_3
    //   233: iand
    //   234: i2b
    //   235: istore 17
    //   237: aload 6
    //   239: iconst_0
    //   240: baload
    //   241: bipush 7
    //   243: iand
    //   244: i2b
    //   245: istore 18
    //   247: aload 6
    //   249: iconst_1
    //   250: baload
    //   251: istore_2
    //   252: aload 10
    //   254: astore 4
    //   256: aload_0
    //   257: aload 6
    //   259: bipush 24
    //   261: invokespecial 235	android/net/SntpClient:readTimeStamp	([BI)J
    //   264: lstore 19
    //   266: aload 10
    //   268: astore 4
    //   270: aload_0
    //   271: aload 6
    //   273: bipush 32
    //   275: invokespecial 235	android/net/SntpClient:readTimeStamp	([BI)J
    //   278: lstore 21
    //   280: aload 10
    //   282: astore 4
    //   284: aload_0
    //   285: aload 6
    //   287: bipush 40
    //   289: invokespecial 235	android/net/SntpClient:readTimeStamp	([BI)J
    //   292: lstore 23
    //   294: aload 10
    //   296: astore 4
    //   298: iload 17
    //   300: iload 18
    //   302: iload_2
    //   303: sipush 255
    //   306: iand
    //   307: lload 23
    //   309: invokestatic 237	android/net/SntpClient:checkValidServerReply	(BBIJ)V
    //   312: lload 15
    //   314: lload 13
    //   316: lsub
    //   317: lload 23
    //   319: lload 21
    //   321: lsub
    //   322: lsub
    //   323: lstore 13
    //   325: aload 10
    //   327: astore 4
    //   329: lload 21
    //   331: lload 19
    //   333: lsub
    //   334: lload 23
    //   336: lload 11
    //   338: lsub
    //   339: ladd
    //   340: ldc2_w 238
    //   343: ldiv
    //   344: lstore 23
    //   346: aload 10
    //   348: astore 4
    //   350: aload_1
    //   351: invokevirtual 242	java/net/InetAddress:toString	()Ljava/lang/String;
    //   354: lload 13
    //   356: lload 23
    //   358: invokestatic 246	android/net/EventLogTags:writeNtpSuccess	(Ljava/lang/String;JJ)V
    //   361: aload 10
    //   363: astore 4
    //   365: new 58	java/lang/StringBuilder
    //   368: astore 8
    //   370: aload 10
    //   372: astore 4
    //   374: aload 8
    //   376: invokespecial 59	java/lang/StringBuilder:<init>	()V
    //   379: aload 10
    //   381: astore 4
    //   383: aload 8
    //   385: ldc -8
    //   387: invokevirtual 65	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   390: pop
    //   391: aload 10
    //   393: astore 4
    //   395: aload 8
    //   397: lload 13
    //   399: invokevirtual 251	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   402: pop
    //   403: aload 10
    //   405: astore 4
    //   407: aload 8
    //   409: ldc -3
    //   411: invokevirtual 65	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   414: pop
    //   415: aload 10
    //   417: astore 4
    //   419: aload 8
    //   421: lload 23
    //   423: invokevirtual 251	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   426: pop
    //   427: aload 10
    //   429: astore 4
    //   431: aload 8
    //   433: ldc -1
    //   435: invokevirtual 65	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   438: pop
    //   439: aload 10
    //   441: astore 4
    //   443: ldc 44
    //   445: aload 8
    //   447: invokevirtual 72	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   450: invokestatic 170	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   453: pop
    //   454: aload 10
    //   456: astore 4
    //   458: aload_0
    //   459: lload 11
    //   461: lload 23
    //   463: ladd
    //   464: putfield 111	android/net/SntpClient:mNtpTime	J
    //   467: aload 10
    //   469: astore 4
    //   471: aload_0
    //   472: lload 15
    //   474: putfield 114	android/net/SntpClient:mNtpTimeReference	J
    //   477: aload 10
    //   479: astore 4
    //   481: aload_0
    //   482: lload 13
    //   484: putfield 117	android/net/SntpClient:mRoundTripTime	J
    //   487: aload 10
    //   489: invokevirtual 258	java/net/DatagramSocket:close	()V
    //   492: iload 7
    //   494: invokestatic 261	android/net/TrafficStats:setThreadStatsTag	(I)V
    //   497: iconst_1
    //   498: ireturn
    //   499: astore 8
    //   501: goto +17 -> 518
    //   504: astore_1
    //   505: aload 8
    //   507: astore 4
    //   509: goto +95 -> 604
    //   512: astore 8
    //   514: aload 9
    //   516: astore 10
    //   518: aload 10
    //   520: astore 4
    //   522: aload_1
    //   523: invokevirtual 242	java/net/InetAddress:toString	()Ljava/lang/String;
    //   526: aload 8
    //   528: invokevirtual 178	java/lang/Exception:toString	()Ljava/lang/String;
    //   531: invokestatic 184	android/net/EventLogTags:writeNtpFailure	(Ljava/lang/String;Ljava/lang/String;)V
    //   534: aload 10
    //   536: astore 4
    //   538: new 58	java/lang/StringBuilder
    //   541: astore_1
    //   542: aload 10
    //   544: astore 4
    //   546: aload_1
    //   547: invokespecial 59	java/lang/StringBuilder:<init>	()V
    //   550: aload 10
    //   552: astore 4
    //   554: aload_1
    //   555: ldc -70
    //   557: invokevirtual 65	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   560: pop
    //   561: aload 10
    //   563: astore 4
    //   565: aload_1
    //   566: aload 8
    //   568: invokevirtual 189	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   571: pop
    //   572: aload 10
    //   574: astore 4
    //   576: ldc 44
    //   578: aload_1
    //   579: invokevirtual 72	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   582: invokestatic 170	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   585: pop
    //   586: aload 10
    //   588: ifnull +8 -> 596
    //   591: aload 10
    //   593: invokevirtual 258	java/net/DatagramSocket:close	()V
    //   596: iload 7
    //   598: invokestatic 261	android/net/TrafficStats:setThreadStatsTag	(I)V
    //   601: iconst_0
    //   602: ireturn
    //   603: astore_1
    //   604: aload 4
    //   606: ifnull +8 -> 614
    //   609: aload 4
    //   611: invokevirtual 258	java/net/DatagramSocket:close	()V
    //   614: iload 7
    //   616: invokestatic 261	android/net/TrafficStats:setThreadStatsTag	(I)V
    //   619: aload_1
    //   620: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	621	0	this	SntpClient
    //   0	621	1	paramInetAddress	java.net.InetAddress
    //   0	621	2	paramInt1	int
    //   0	621	3	paramInt2	int
    //   0	621	4	paramNetwork	Network
    //   1	167	5	localDatagramPacket	java.net.DatagramPacket
    //   4	282	6	arrayOfByte	byte[]
    //   12	603	7	i	int
    //   16	430	8	localObject1	Object
    //   499	7	8	localException1	Exception
    //   512	55	8	localException2	Exception
    //   20	495	9	localObject2	Object
    //   25	567	10	localObject3	Object
    //   136	324	11	l1	long
    //   145	338	13	l2	long
    //   213	260	15	l3	long
    //   235	64	17	b1	byte
    //   245	56	18	b2	byte
    //   264	68	19	l4	long
    //   278	52	21	l5	long
    //   292	170	23	l6	long
    // Exception table:
    //   from	to	target	type
    //   112	122	499	java/lang/Exception
    //   133	138	499	java/lang/Exception
    //   142	147	499	java/lang/Exception
    //   151	161	499	java/lang/Exception
    //   165	172	499	java/lang/Exception
    //   176	181	499	java/lang/Exception
    //   185	195	499	java/lang/Exception
    //   199	206	499	java/lang/Exception
    //   210	215	499	java/lang/Exception
    //   256	266	499	java/lang/Exception
    //   270	280	499	java/lang/Exception
    //   284	294	499	java/lang/Exception
    //   298	312	499	java/lang/Exception
    //   329	346	499	java/lang/Exception
    //   350	361	499	java/lang/Exception
    //   365	370	499	java/lang/Exception
    //   374	379	499	java/lang/Exception
    //   383	391	499	java/lang/Exception
    //   395	403	499	java/lang/Exception
    //   407	415	499	java/lang/Exception
    //   419	427	499	java/lang/Exception
    //   431	439	499	java/lang/Exception
    //   443	454	499	java/lang/Exception
    //   458	467	499	java/lang/Exception
    //   471	477	499	java/lang/Exception
    //   481	487	499	java/lang/Exception
    //   22	27	504	finally
    //   35	40	504	finally
    //   48	55	504	finally
    //   63	69	504	finally
    //   77	83	504	finally
    //   91	96	504	finally
    //   104	108	504	finally
    //   22	27	512	java/lang/Exception
    //   35	40	512	java/lang/Exception
    //   48	55	512	java/lang/Exception
    //   63	69	512	java/lang/Exception
    //   77	83	512	java/lang/Exception
    //   91	96	512	java/lang/Exception
    //   104	108	512	java/lang/Exception
    //   112	122	603	finally
    //   133	138	603	finally
    //   142	147	603	finally
    //   151	161	603	finally
    //   165	172	603	finally
    //   176	181	603	finally
    //   185	195	603	finally
    //   199	206	603	finally
    //   210	215	603	finally
    //   256	266	603	finally
    //   270	280	603	finally
    //   284	294	603	finally
    //   298	312	603	finally
    //   329	346	603	finally
    //   350	361	603	finally
    //   365	370	603	finally
    //   374	379	603	finally
    //   383	391	603	finally
    //   395	403	603	finally
    //   407	415	603	finally
    //   419	427	603	finally
    //   431	439	603	finally
    //   443	454	603	finally
    //   458	467	603	finally
    //   471	477	603	finally
    //   481	487	603	finally
    //   522	534	603	finally
    //   538	542	603	finally
    //   546	550	603	finally
    //   554	561	603	finally
    //   565	572	603	finally
    //   576	586	603	finally
  }
  
  private static class InvalidServerReplyException
    extends Exception
  {
    public InvalidServerReplyException(String paramString)
    {
      super();
    }
  }
}
