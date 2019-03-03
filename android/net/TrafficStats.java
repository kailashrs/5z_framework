package android.net;

import android.annotation.SuppressLint;
import android.annotation.SystemApi;
import android.content.Context;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import com.android.server.NetworkManagementSocketTagger;
import dalvik.system.SocketTagger;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

public class TrafficStats
{
  @Deprecated
  public static final long GB_IN_BYTES = 1073741824L;
  @Deprecated
  public static final long KB_IN_BYTES = 1024L;
  private static final String LOOPBACK_IFACE = "lo";
  @Deprecated
  public static final long MB_IN_BYTES = 1048576L;
  @Deprecated
  public static final long PB_IN_BYTES = 1125899906842624L;
  public static final int TAG_SYSTEM_APP = -251;
  public static final int TAG_SYSTEM_BACKUP = -253;
  public static final int TAG_SYSTEM_DHCP = -192;
  public static final int TAG_SYSTEM_DOWNLOAD = -255;
  public static final int TAG_SYSTEM_GPS = -188;
  public static final int TAG_SYSTEM_MEDIA = -254;
  public static final int TAG_SYSTEM_NEIGHBOR = -189;
  public static final int TAG_SYSTEM_NTP = -191;
  public static final int TAG_SYSTEM_PAC = -187;
  public static final int TAG_SYSTEM_PROBE = -190;
  public static final int TAG_SYSTEM_RESTORE = -252;
  @Deprecated
  public static final long TB_IN_BYTES = 1099511627776L;
  private static final int TYPE_DNS_RX_PACKETS = 6;
  private static final int TYPE_DNS_TX_PACKETS = 7;
  private static final int TYPE_RX_BYTES = 0;
  private static final int TYPE_RX_PACKETS = 1;
  private static final int TYPE_TCP_RX_PACKETS = 4;
  private static final int TYPE_TCP_TX_PACKETS = 5;
  private static final int TYPE_TX_BYTES = 2;
  private static final int TYPE_TX_PACKETS = 3;
  public static final int UID_REMOVED = -4;
  public static final int UID_TETHERING = -5;
  public static final int UNSUPPORTED = -1;
  private static NetworkStats sActiveProfilingStart;
  private static Object sProfilingLock = new Object();
  private static INetworkStatsService sStatsService;
  
  public TrafficStats() {}
  
  private static long addIfSupported(long paramLong)
  {
    if (paramLong == -1L) {
      paramLong = 0L;
    }
    return paramLong;
  }
  
  public static void clearThreadStatsTag()
  {
    NetworkManagementSocketTagger.setThreadSocketStatsTag(-1);
  }
  
  @SystemApi
  @SuppressLint({"Doclava125"})
  public static void clearThreadStatsUid()
  {
    NetworkManagementSocketTagger.setThreadSocketStatsUid(-1);
  }
  
  public static void closeQuietly(INetworkStatsSession paramINetworkStatsSession)
  {
    if (paramINetworkStatsSession != null) {
      try
      {
        paramINetworkStatsSession.close();
      }
      catch (Exception paramINetworkStatsSession) {}catch (RuntimeException paramINetworkStatsSession)
      {
        throw paramINetworkStatsSession;
      }
    }
  }
  
  public static int getAndSetThreadStatsTag(int paramInt)
  {
    return NetworkManagementSocketTagger.setThreadSocketStatsTag(paramInt);
  }
  
  private static NetworkStats getDataLayerSnapshotForUid(Context paramContext)
  {
    int i = Process.myUid();
    try
    {
      paramContext = getStatsService().getDataLayerSnapshotForUid(i);
      return paramContext;
    }
    catch (RemoteException paramContext)
    {
      throw paramContext.rethrowFromSystemServer();
    }
  }
  
  public static long getLoopbackRxBytes()
  {
    try
    {
      long l = getStatsService().getIfaceStats("lo", 0);
      return l;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public static long getLoopbackRxPackets()
  {
    try
    {
      long l = getStatsService().getIfaceStats("lo", 1);
      return l;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public static long getLoopbackTxBytes()
  {
    try
    {
      long l = getStatsService().getIfaceStats("lo", 2);
      return l;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public static long getLoopbackTxPackets()
  {
    try
    {
      long l = getStatsService().getIfaceStats("lo", 3);
      return l;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public static long getMobileDnsRxPackets()
  {
    long l1 = 0L;
    String[] arrayOfString = getMobileIfaces();
    int i = arrayOfString.length;
    int j = 0;
    while (j < i)
    {
      String str = arrayOfString[j];
      try
      {
        long l2 = getStatsService().getIfaceStats(str, 6);
        l1 += addIfSupported(l2);
        j++;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return l1;
  }
  
  public static long getMobileDnsTxPackets()
  {
    long l1 = 0L;
    String[] arrayOfString = getMobileIfaces();
    int i = arrayOfString.length;
    int j = 0;
    while (j < i)
    {
      String str = arrayOfString[j];
      try
      {
        long l2 = getStatsService().getIfaceStats(str, 7);
        l1 += addIfSupported(l2);
        j++;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return l1;
  }
  
  private static String[] getMobileIfaces()
  {
    try
    {
      String[] arrayOfString = getStatsService().getMobileIfaces();
      return arrayOfString;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public static long getMobileRxBytes()
  {
    long l = 0L;
    String[] arrayOfString = getMobileIfaces();
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++) {
      l += addIfSupported(getRxBytes(arrayOfString[j]));
    }
    return l;
  }
  
  public static long getMobileRxPackets()
  {
    long l = 0L;
    String[] arrayOfString = getMobileIfaces();
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++) {
      l += addIfSupported(getRxPackets(arrayOfString[j]));
    }
    return l;
  }
  
  public static long getMobileTcpRxPackets()
  {
    long l1 = 0L;
    String[] arrayOfString = getMobileIfaces();
    int i = arrayOfString.length;
    int j = 0;
    while (j < i)
    {
      String str = arrayOfString[j];
      try
      {
        long l2 = getStatsService().getIfaceStats(str, 4);
        l1 += addIfSupported(l2);
        j++;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return l1;
  }
  
  public static long getMobileTcpTxPackets()
  {
    long l1 = 0L;
    String[] arrayOfString = getMobileIfaces();
    int i = arrayOfString.length;
    int j = 0;
    while (j < i)
    {
      String str = arrayOfString[j];
      try
      {
        long l2 = getStatsService().getIfaceStats(str, 5);
        l1 += addIfSupported(l2);
        j++;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return l1;
  }
  
  public static long getMobileTxBytes()
  {
    long l = 0L;
    String[] arrayOfString = getMobileIfaces();
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++) {
      l += addIfSupported(getTxBytes(arrayOfString[j]));
    }
    return l;
  }
  
  public static long getMobileTxPackets()
  {
    long l = 0L;
    String[] arrayOfString = getMobileIfaces();
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++) {
      l += addIfSupported(getTxPackets(arrayOfString[j]));
    }
    return l;
  }
  
  public static long getRxBytes(String paramString)
  {
    try
    {
      long l = getStatsService().getIfaceStats(paramString, 0);
      return l;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public static long getRxPackets(String paramString)
  {
    try
    {
      long l = getStatsService().getIfaceStats(paramString, 1);
      return l;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  private static INetworkStatsService getStatsService()
  {
    try
    {
      if (sStatsService == null) {
        sStatsService = INetworkStatsService.Stub.asInterface(ServiceManager.getService("netstats"));
      }
      INetworkStatsService localINetworkStatsService = sStatsService;
      return localINetworkStatsService;
    }
    finally {}
  }
  
  public static int getThreadStatsTag()
  {
    return NetworkManagementSocketTagger.getThreadSocketStatsTag();
  }
  
  public static int getThreadStatsUid()
  {
    return NetworkManagementSocketTagger.getThreadSocketStatsUid();
  }
  
  public static long getTotalRxBytes()
  {
    try
    {
      long l = getStatsService().getTotalStats(0);
      return l;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public static long getTotalRxPackets()
  {
    try
    {
      long l = getStatsService().getTotalStats(1);
      return l;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public static long getTotalTxBytes()
  {
    try
    {
      long l = getStatsService().getTotalStats(2);
      return l;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public static long getTotalTxPackets()
  {
    try
    {
      long l = getStatsService().getTotalStats(3);
      return l;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public static long getTxBytes(String paramString)
  {
    try
    {
      long l = getStatsService().getIfaceStats(paramString, 2);
      return l;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public static long getTxPackets(String paramString)
  {
    try
    {
      long l = getStatsService().getIfaceStats(paramString, 3);
      return l;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public static long getUidRxBytes(int paramInt)
  {
    int i = Process.myUid();
    if ((i != 1000) && (i != paramInt)) {
      return -1L;
    }
    try
    {
      long l = getStatsService().getUidStats(paramInt, 0);
      return l;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public static long getUidRxPackets(int paramInt)
  {
    int i = Process.myUid();
    if ((i != 1000) && (i != paramInt)) {
      return -1L;
    }
    try
    {
      long l = getStatsService().getUidStats(paramInt, 1);
      return l;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public static long getUidTcpRxBytes(int paramInt)
  {
    return -1L;
  }
  
  @Deprecated
  public static long getUidTcpRxSegments(int paramInt)
  {
    return -1L;
  }
  
  @Deprecated
  public static long getUidTcpTxBytes(int paramInt)
  {
    return -1L;
  }
  
  @Deprecated
  public static long getUidTcpTxSegments(int paramInt)
  {
    return -1L;
  }
  
  public static long getUidTxBytes(int paramInt)
  {
    int i = Process.myUid();
    if ((i != 1000) && (i != paramInt)) {
      return -1L;
    }
    try
    {
      long l = getStatsService().getUidStats(paramInt, 2);
      return l;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public static long getUidTxPackets(int paramInt)
  {
    int i = Process.myUid();
    if ((i != 1000) && (i != paramInt)) {
      return -1L;
    }
    try
    {
      long l = getStatsService().getUidStats(paramInt, 3);
      return l;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public static long getUidUdpRxBytes(int paramInt)
  {
    return -1L;
  }
  
  @Deprecated
  public static long getUidUdpRxPackets(int paramInt)
  {
    return -1L;
  }
  
  @Deprecated
  public static long getUidUdpTxBytes(int paramInt)
  {
    return -1L;
  }
  
  @Deprecated
  public static long getUidUdpTxPackets(int paramInt)
  {
    return -1L;
  }
  
  public static void incrementOperationCount(int paramInt)
  {
    incrementOperationCount(getThreadStatsTag(), paramInt);
  }
  
  public static void incrementOperationCount(int paramInt1, int paramInt2)
  {
    int i = Process.myUid();
    try
    {
      getStatsService().incrementOperationCount(i, paramInt1, paramInt2);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public static void setThreadStatsTag(int paramInt)
  {
    NetworkManagementSocketTagger.setThreadSocketStatsTag(paramInt);
  }
  
  @SystemApi
  public static void setThreadStatsTagApp()
  {
    setThreadStatsTag(65285);
  }
  
  @SystemApi
  public static void setThreadStatsTagBackup()
  {
    setThreadStatsTag(65283);
  }
  
  @SystemApi
  public static void setThreadStatsTagRestore()
  {
    setThreadStatsTag(65284);
  }
  
  @SystemApi
  @SuppressLint({"Doclava125"})
  public static void setThreadStatsUid(int paramInt)
  {
    NetworkManagementSocketTagger.setThreadSocketStatsUid(paramInt);
  }
  
  @Deprecated
  public static void setThreadStatsUidSelf()
  {
    setThreadStatsUid(Process.myUid());
  }
  
  public static void startDataProfiling(Context paramContext)
  {
    synchronized (sProfilingLock)
    {
      if (sActiveProfilingStart == null)
      {
        sActiveProfilingStart = getDataLayerSnapshotForUid(paramContext);
        return;
      }
      paramContext = new java/lang/IllegalStateException;
      paramContext.<init>("already profiling data");
      throw paramContext;
    }
  }
  
  public static NetworkStats stopDataProfiling(Context paramContext)
  {
    synchronized (sProfilingLock)
    {
      if (sActiveProfilingStart != null)
      {
        paramContext = NetworkStats.subtract(getDataLayerSnapshotForUid(paramContext), sActiveProfilingStart, null, null);
        sActiveProfilingStart = null;
        return paramContext;
      }
      paramContext = new java/lang/IllegalStateException;
      paramContext.<init>("not profiling data");
      throw paramContext;
    }
  }
  
  public static void tagDatagramSocket(DatagramSocket paramDatagramSocket)
    throws SocketException
  {
    SocketTagger.get().tag(paramDatagramSocket);
  }
  
  public static void tagFileDescriptor(FileDescriptor paramFileDescriptor)
    throws IOException
  {
    SocketTagger.get().tag(paramFileDescriptor);
  }
  
  public static void tagSocket(Socket paramSocket)
    throws SocketException
  {
    SocketTagger.get().tag(paramSocket);
  }
  
  public static void untagDatagramSocket(DatagramSocket paramDatagramSocket)
    throws SocketException
  {
    SocketTagger.get().untag(paramDatagramSocket);
  }
  
  public static void untagFileDescriptor(FileDescriptor paramFileDescriptor)
    throws IOException
  {
    SocketTagger.get().untag(paramFileDescriptor);
  }
  
  public static void untagSocket(Socket paramSocket)
    throws SocketException
  {
    SocketTagger.get().untag(paramSocket);
  }
}
