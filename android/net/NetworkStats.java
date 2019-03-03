package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.util.Slog;
import android.util.SparseBooleanArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import libcore.util.EmptyArray;

public class NetworkStats
  implements Parcelable
{
  private static final String CLATD_INTERFACE_PREFIX = "v4-";
  public static final Parcelable.Creator<NetworkStats> CREATOR = new Parcelable.Creator()
  {
    public NetworkStats createFromParcel(Parcel paramAnonymousParcel)
    {
      return new NetworkStats(paramAnonymousParcel);
    }
    
    public NetworkStats[] newArray(int paramAnonymousInt)
    {
      return new NetworkStats[paramAnonymousInt];
    }
  };
  public static final int DEFAULT_NETWORK_ALL = -1;
  public static final int DEFAULT_NETWORK_NO = 0;
  public static final int DEFAULT_NETWORK_YES = 1;
  public static final String IFACE_ALL = null;
  public static final String[] INTERFACES_ALL = null;
  private static final int IPV4V6_HEADER_DELTA = 20;
  public static final int METERED_ALL = -1;
  public static final int METERED_NO = 0;
  public static final int METERED_YES = 1;
  public static final int ROAMING_ALL = -1;
  public static final int ROAMING_NO = 0;
  public static final int ROAMING_YES = 1;
  public static final int SET_ALL = -1;
  public static final int SET_DBG_VPN_IN = 1001;
  public static final int SET_DBG_VPN_OUT = 1002;
  public static final int SET_DEBUG_START = 1000;
  public static final int SET_DEFAULT = 0;
  public static final int SET_FOREGROUND = 1;
  public static final int STATS_PER_IFACE = 0;
  public static final int STATS_PER_UID = 1;
  private static final String TAG = "NetworkStats";
  public static final int TAG_ALL = -1;
  public static final int TAG_NONE = 0;
  public static final int UID_ALL = -1;
  private int capacity;
  private int[] defaultNetwork;
  private long elapsedRealtime;
  private String[] iface;
  private int[] metered;
  private long[] operations;
  private int[] roaming;
  private long[] rxBytes;
  private long[] rxPackets;
  private int[] set;
  private int size;
  private int[] tag;
  private long[] txBytes;
  private long[] txPackets;
  private int[] uid;
  
  public NetworkStats(long paramLong, int paramInt)
  {
    elapsedRealtime = paramLong;
    size = 0;
    if (paramInt > 0)
    {
      capacity = paramInt;
      iface = new String[paramInt];
      uid = new int[paramInt];
      set = new int[paramInt];
      tag = new int[paramInt];
      metered = new int[paramInt];
      roaming = new int[paramInt];
      defaultNetwork = new int[paramInt];
      rxBytes = new long[paramInt];
      rxPackets = new long[paramInt];
      txBytes = new long[paramInt];
      txPackets = new long[paramInt];
      operations = new long[paramInt];
    }
    else
    {
      clear();
    }
  }
  
  public NetworkStats(Parcel paramParcel)
  {
    elapsedRealtime = paramParcel.readLong();
    size = paramParcel.readInt();
    capacity = paramParcel.readInt();
    iface = paramParcel.createStringArray();
    uid = paramParcel.createIntArray();
    set = paramParcel.createIntArray();
    tag = paramParcel.createIntArray();
    metered = paramParcel.createIntArray();
    roaming = paramParcel.createIntArray();
    defaultNetwork = paramParcel.createIntArray();
    rxBytes = paramParcel.createLongArray();
    rxPackets = paramParcel.createLongArray();
    txBytes = paramParcel.createLongArray();
    txPackets = paramParcel.createLongArray();
    operations = paramParcel.createLongArray();
  }
  
  private Entry addTrafficToApplications(int paramInt, String paramString1, String paramString2, Entry paramEntry1, Entry paramEntry2)
  {
    Entry localEntry1 = new Entry();
    Entry localEntry2 = new Entry();
    iface = paramString2;
    for (int i = 0; i < size; i++) {
      if ((Objects.equals(iface[i], paramString1)) && (uid[i] != paramInt))
      {
        if (rxBytes > 0L) {
          rxBytes = (rxBytes * rxBytes[i] / rxBytes);
        } else {
          rxBytes = 0L;
        }
        if (rxPackets > 0L) {
          rxPackets = (rxPackets * rxPackets[i] / rxPackets);
        } else {
          rxPackets = 0L;
        }
        if (txBytes > 0L) {
          txBytes = (txBytes * txBytes[i] / txBytes);
        } else {
          txBytes = 0L;
        }
        if (txPackets > 0L) {
          txPackets = (txPackets * txPackets[i] / txPackets);
        } else {
          txPackets = 0L;
        }
        if (operations > 0L) {
          operations = (operations * operations[i] / operations);
        } else {
          operations = 0L;
        }
        uid = uid[i];
        tag = tag[i];
        set = set[i];
        metered = metered[i];
        roaming = roaming[i];
        defaultNetwork = defaultNetwork[i];
        combineValues(localEntry2);
        if (tag[i] == 0)
        {
          localEntry1.add(localEntry2);
          set = 1001;
          combineValues(localEntry2);
        }
      }
    }
    return localEntry1;
  }
  
  public static void apply464xlatAdjustments(NetworkStats paramNetworkStats1, NetworkStats paramNetworkStats2, Map<String, String> paramMap)
  {
    NetworkStats localNetworkStats = new NetworkStats(0L, paramMap.size());
    Entry localEntry1 = null;
    Entry localEntry2 = new Entry(IFACE_ALL, 0, 0, 0, 0, 0, 0, 0L, 0L, 0L, 0L, 0L);
    for (int i = 0; i < size; i++)
    {
      localEntry1 = paramNetworkStats2.getValues(i, localEntry1);
      if ((iface != null) && (iface.startsWith("v4-")))
      {
        String str = (String)paramMap.get(iface);
        if (str != null)
        {
          iface = str;
          rxBytes = (-(rxBytes + rxPackets * 20L));
          txBytes = (-(txBytes + txPackets * 20L));
          rxPackets = (-rxPackets);
          txPackets = (-txPackets);
          localNetworkStats.combineValues(localEntry2);
          rxBytes += rxPackets * 20L;
          txBytes += txPackets * 20L;
          paramNetworkStats2.setValues(i, localEntry1);
        }
      }
    }
    paramNetworkStats1.combineAllValues(localNetworkStats);
  }
  
  private void deductTrafficFromVpnApp(int paramInt, String paramString, Entry paramEntry)
  {
    uid = paramInt;
    set = 1002;
    tag = 0;
    iface = paramString;
    metered = -1;
    roaming = -1;
    defaultNetwork = -1;
    combineValues(paramEntry);
    int i = findIndex(paramString, paramInt, 0, 0, 0, 0, 0);
    if (i != -1) {
      tunSubtract(i, this, paramEntry);
    }
    paramInt = findIndex(paramString, paramInt, 1, 0, 0, 0, 0);
    if (paramInt != -1) {
      tunSubtract(paramInt, this, paramEntry);
    }
  }
  
  public static String defaultNetworkToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "UNKNOWN";
    case 1: 
      return "YES";
    case 0: 
      return "NO";
    }
    return "ALL";
  }
  
  private Entry getTotal(Entry paramEntry, HashSet<String> paramHashSet, int paramInt, boolean paramBoolean)
  {
    if (paramEntry == null) {
      paramEntry = new Entry();
    }
    iface = IFACE_ALL;
    uid = paramInt;
    set = -1;
    tag = 0;
    metered = -1;
    roaming = -1;
    defaultNetwork = -1;
    rxBytes = 0L;
    rxPackets = 0L;
    txBytes = 0L;
    txPackets = 0L;
    operations = 0L;
    for (int i = 0; i < size; i++)
    {
      int j = 1;
      int k;
      if ((paramInt != -1) && (paramInt != uid[i])) {
        k = 0;
      } else {
        k = 1;
      }
      int m = j;
      if (paramHashSet != null) {
        if (paramHashSet.contains(iface[i])) {
          m = j;
        } else {
          m = 0;
        }
      }
      if ((k != 0) && (m != 0) && ((tag[i] == 0) || (paramBoolean)))
      {
        rxBytes += rxBytes[i];
        rxPackets += rxPackets[i];
        txBytes += txBytes[i];
        txPackets += txPackets[i];
        operations += operations[i];
      }
    }
    return paramEntry;
  }
  
  public static String meteredToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "UNKNOWN";
    case 1: 
      return "YES";
    case 0: 
      return "NO";
    }
    return "ALL";
  }
  
  public static String roamingToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "UNKNOWN";
    case 1: 
      return "YES";
    case 0: 
      return "NO";
    }
    return "ALL";
  }
  
  public static boolean setMatches(int paramInt1, int paramInt2)
  {
    boolean bool = true;
    if (paramInt1 == paramInt2) {
      return true;
    }
    if ((paramInt1 != -1) || (paramInt2 >= 1000)) {
      bool = false;
    }
    return bool;
  }
  
  public static String setToCheckinString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      switch (paramInt)
      {
      default: 
        return "unk";
      case 1002: 
        return "vpnout";
      }
      return "vpnin";
    case 1: 
      return "fg";
    case 0: 
      return "def";
    }
    return "all";
  }
  
  public static String setToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      switch (paramInt)
      {
      default: 
        return "UNKNOWN";
      case 1002: 
        return "DBG_VPN_OUT";
      }
      return "DBG_VPN_IN";
    case 1: 
      return "FOREGROUND";
    case 0: 
      return "DEFAULT";
    }
    return "ALL";
  }
  
  private void setValues(int paramInt, Entry paramEntry)
  {
    iface[paramInt] = iface;
    uid[paramInt] = uid;
    set[paramInt] = set;
    tag[paramInt] = tag;
    metered[paramInt] = metered;
    roaming[paramInt] = roaming;
    defaultNetwork[paramInt] = defaultNetwork;
    rxBytes[paramInt] = rxBytes;
    rxPackets[paramInt] = rxPackets;
    txBytes[paramInt] = txBytes;
    txPackets[paramInt] = txPackets;
    operations[paramInt] = operations;
  }
  
  public static <C> NetworkStats subtract(NetworkStats paramNetworkStats1, NetworkStats paramNetworkStats2, NonMonotonicObserver<C> paramNonMonotonicObserver, C paramC)
  {
    return subtract(paramNetworkStats1, paramNetworkStats2, paramNonMonotonicObserver, paramC, null);
  }
  
  public static <C> NetworkStats subtract(NetworkStats paramNetworkStats1, NetworkStats paramNetworkStats2, NonMonotonicObserver<C> paramNonMonotonicObserver, C paramC, NetworkStats paramNetworkStats3)
  {
    NetworkStats localNetworkStats = paramNetworkStats2;
    long l1 = elapsedRealtime - elapsedRealtime;
    long l2 = l1;
    if (l1 < 0L)
    {
      if (paramNonMonotonicObserver != null) {
        paramNonMonotonicObserver.foundNonMonotonic(paramNetworkStats1, -1, localNetworkStats, -1, paramC);
      }
      l2 = 0L;
    }
    Object localObject = new Entry();
    int i = 0;
    if ((paramNetworkStats3 != null) && (capacity >= size))
    {
      size = 0;
      elapsedRealtime = l2;
    }
    else
    {
      paramNetworkStats3 = new NetworkStats(l2, size);
    }
    localNetworkStats = paramNetworkStats3;
    paramNetworkStats3 = (NetworkStats)localObject;
    for (;;)
    {
      localObject = paramNetworkStats2;
      if (i >= size) {
        break;
      }
      iface = iface[i];
      uid = uid[i];
      set = set[i];
      tag = tag[i];
      metered = metered[i];
      roaming = roaming[i];
      defaultNetwork = defaultNetwork[i];
      rxBytes = rxBytes[i];
      rxPackets = rxPackets[i];
      txBytes = txBytes[i];
      txPackets = txPackets[i];
      operations = operations[i];
      int j = ((NetworkStats)localObject).findIndexHinted(iface, uid, set, tag, metered, roaming, defaultNetwork, i);
      if (j != -1)
      {
        rxBytes -= rxBytes[j];
        rxPackets -= rxPackets[j];
        txBytes -= txBytes[j];
        txPackets -= txPackets[j];
        operations -= operations[j];
      }
      if (paramNetworkStats3.isNegative())
      {
        if (paramNonMonotonicObserver != null) {
          paramNonMonotonicObserver.foundNonMonotonic(paramNetworkStats1, i, paramNetworkStats2, j, paramC);
        }
        localObject = paramNetworkStats3;
        rxBytes = Math.max(rxBytes, 0L);
        rxPackets = Math.max(rxPackets, 0L);
        txBytes = Math.max(txBytes, 0L);
        txPackets = Math.max(txPackets, 0L);
        operations = Math.max(operations, 0L);
      }
      localNetworkStats.addValues(paramNetworkStats3);
      i++;
    }
    return localNetworkStats;
  }
  
  public static String tagToString(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
  
  private void tunAdjustmentInit(int paramInt, String paramString1, String paramString2, Entry paramEntry1, Entry paramEntry2)
  {
    Entry localEntry = new Entry();
    int i = 0;
    while (i < size)
    {
      getValues(i, localEntry);
      if (uid != -1)
      {
        if ((set != 1001) && (set != 1002))
        {
          if ((uid == paramInt) && (tag == 0) && (Objects.equals(paramString2, iface))) {
            paramEntry2.add(localEntry);
          }
          if ((uid != paramInt) && (tag == 0) && (Objects.equals(paramString1, iface))) {
            paramEntry1.add(localEntry);
          }
          i++;
        }
        else
        {
          throw new IllegalStateException("Cannot adjust VPN accounting on a NetworkStats containing SET_DBG_VPN_*");
        }
      }
      else {
        throw new IllegalStateException("Cannot adjust VPN accounting on an iface aggregated NetworkStats.");
      }
    }
  }
  
  private static Entry tunGetPool(Entry paramEntry1, Entry paramEntry2)
  {
    Entry localEntry = new Entry();
    rxBytes = Math.min(rxBytes, rxBytes);
    rxPackets = Math.min(rxPackets, rxPackets);
    txBytes = Math.min(txBytes, txBytes);
    txPackets = Math.min(txPackets, txPackets);
    operations = Math.min(operations, operations);
    return localEntry;
  }
  
  private static void tunSubtract(int paramInt, NetworkStats paramNetworkStats, Entry paramEntry)
  {
    long l = Math.min(rxBytes[paramInt], rxBytes);
    long[] arrayOfLong = rxBytes;
    arrayOfLong[paramInt] -= l;
    rxBytes -= l;
    l = Math.min(rxPackets[paramInt], rxPackets);
    arrayOfLong = rxPackets;
    arrayOfLong[paramInt] -= l;
    rxPackets -= l;
    l = Math.min(txBytes[paramInt], txBytes);
    arrayOfLong = txBytes;
    arrayOfLong[paramInt] -= l;
    txBytes -= l;
    l = Math.min(txPackets[paramInt], txPackets);
    paramNetworkStats = txPackets;
    paramNetworkStats[paramInt] -= l;
    txPackets -= l;
  }
  
  @VisibleForTesting
  public NetworkStats addIfaceValues(String paramString, long paramLong1, long paramLong2, long paramLong3, long paramLong4)
  {
    return addValues(paramString, -1, 0, 0, paramLong1, paramLong2, paramLong3, paramLong4, 0L);
  }
  
  public NetworkStats addValues(Entry paramEntry)
  {
    if (size >= capacity)
    {
      int i = Math.max(size, 10) * 3 / 2;
      iface = ((String[])Arrays.copyOf(iface, i));
      uid = Arrays.copyOf(uid, i);
      set = Arrays.copyOf(set, i);
      tag = Arrays.copyOf(tag, i);
      metered = Arrays.copyOf(metered, i);
      roaming = Arrays.copyOf(roaming, i);
      defaultNetwork = Arrays.copyOf(defaultNetwork, i);
      rxBytes = Arrays.copyOf(rxBytes, i);
      rxPackets = Arrays.copyOf(rxPackets, i);
      txBytes = Arrays.copyOf(txBytes, i);
      txPackets = Arrays.copyOf(txPackets, i);
      operations = Arrays.copyOf(operations, i);
      capacity = i;
    }
    setValues(size, paramEntry);
    size += 1;
    return this;
  }
  
  @VisibleForTesting
  public NetworkStats addValues(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5)
  {
    return addValues(new Entry(paramString, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramLong1, paramLong2, paramLong3, paramLong4, paramLong5));
  }
  
  @VisibleForTesting
  public NetworkStats addValues(String paramString, int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5)
  {
    return addValues(new Entry(paramString, paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, paramLong3, paramLong4, paramLong5));
  }
  
  public void apply464xlatAdjustments(Map<String, String> paramMap)
  {
    apply464xlatAdjustments(this, this, paramMap);
  }
  
  public void clear()
  {
    capacity = 0;
    iface = EmptyArray.STRING;
    uid = EmptyArray.INT;
    set = EmptyArray.INT;
    tag = EmptyArray.INT;
    metered = EmptyArray.INT;
    roaming = EmptyArray.INT;
    defaultNetwork = EmptyArray.INT;
    rxBytes = EmptyArray.LONG;
    rxPackets = EmptyArray.LONG;
    txBytes = EmptyArray.LONG;
    txPackets = EmptyArray.LONG;
    operations = EmptyArray.LONG;
  }
  
  public NetworkStats clone()
  {
    NetworkStats localNetworkStats = new NetworkStats(elapsedRealtime, size);
    Entry localEntry = null;
    for (int i = 0; i < size; i++)
    {
      localEntry = getValues(i, localEntry);
      localNetworkStats.addValues(localEntry);
    }
    return localNetworkStats;
  }
  
  public void combineAllValues(NetworkStats paramNetworkStats)
  {
    Entry localEntry = null;
    for (int i = 0; i < size; i++)
    {
      localEntry = paramNetworkStats.getValues(i, localEntry);
      combineValues(localEntry);
    }
  }
  
  public NetworkStats combineValues(Entry paramEntry)
  {
    int i = findIndex(iface, uid, set, tag, metered, roaming, defaultNetwork);
    if (i == -1)
    {
      addValues(paramEntry);
    }
    else
    {
      long[] arrayOfLong = rxBytes;
      arrayOfLong[i] += rxBytes;
      arrayOfLong = rxPackets;
      arrayOfLong[i] += rxPackets;
      arrayOfLong = txBytes;
      arrayOfLong[i] += txBytes;
      arrayOfLong = txPackets;
      arrayOfLong[i] += txPackets;
      arrayOfLong = operations;
      arrayOfLong[i] += operations;
    }
    return this;
  }
  
  public NetworkStats combineValues(String paramString, int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5)
  {
    return combineValues(new Entry(paramString, paramInt1, paramInt2, paramInt3, paramLong1, paramLong2, paramLong3, paramLong4, paramLong5));
  }
  
  @Deprecated
  public NetworkStats combineValues(String paramString, int paramInt1, int paramInt2, long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5)
  {
    return combineValues(paramString, paramInt1, 0, paramInt2, paramLong1, paramLong2, paramLong3, paramLong4, paramLong5);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void dump(String paramString, PrintWriter paramPrintWriter)
  {
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("NetworkStats: elapsedRealtime=");
    paramPrintWriter.println(elapsedRealtime);
    for (int i = 0; i < size; i++)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("  [");
      paramPrintWriter.print(i);
      paramPrintWriter.print("]");
      paramPrintWriter.print(" iface=");
      paramPrintWriter.print(iface[i]);
      paramPrintWriter.print(" uid=");
      paramPrintWriter.print(uid[i]);
      paramPrintWriter.print(" set=");
      paramPrintWriter.print(setToString(set[i]));
      paramPrintWriter.print(" tag=");
      paramPrintWriter.print(tagToString(tag[i]));
      paramPrintWriter.print(" metered=");
      paramPrintWriter.print(meteredToString(metered[i]));
      paramPrintWriter.print(" roaming=");
      paramPrintWriter.print(roamingToString(roaming[i]));
      paramPrintWriter.print(" defaultNetwork=");
      paramPrintWriter.print(defaultNetworkToString(defaultNetwork[i]));
      paramPrintWriter.print(" rxBytes=");
      paramPrintWriter.print(rxBytes[i]);
      paramPrintWriter.print(" rxPackets=");
      paramPrintWriter.print(rxPackets[i]);
      paramPrintWriter.print(" txBytes=");
      paramPrintWriter.print(txBytes[i]);
      paramPrintWriter.print(" txPackets=");
      paramPrintWriter.print(txPackets[i]);
      paramPrintWriter.print(" operations=");
      paramPrintWriter.println(operations[i]);
    }
  }
  
  public void filter(int paramInt1, String[] paramArrayOfString, int paramInt2)
  {
    if ((paramInt1 == -1) && (paramInt2 == -1) && (paramArrayOfString == INTERFACES_ALL)) {
      return;
    }
    Entry localEntry = new Entry();
    int i = 0;
    int j = 0;
    while (j < size)
    {
      localEntry = getValues(j, localEntry);
      int k;
      if (((paramInt1 != -1) && (paramInt1 != uid)) || ((paramInt2 != -1) && (paramInt2 != tag)) || ((paramArrayOfString != INTERFACES_ALL) && (!ArrayUtils.contains(paramArrayOfString, iface)))) {
        k = 0;
      } else {
        k = 1;
      }
      int m = i;
      if (k != 0)
      {
        setValues(i, localEntry);
        m = i + 1;
      }
      j++;
      i = m;
    }
    size = i;
  }
  
  public int findIndex(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    for (int i = 0; i < size; i++) {
      if ((paramInt1 == uid[i]) && (paramInt2 == set[i]) && (paramInt3 == tag[i]) && (paramInt4 == metered[i]) && (paramInt5 == roaming[i]) && (paramInt6 == defaultNetwork[i]) && (Objects.equals(paramString, iface[i]))) {
        return i;
      }
    }
    return -1;
  }
  
  @VisibleForTesting
  public int findIndexHinted(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
  {
    for (int i = 0; i < size; i++)
    {
      int j = i / 2;
      if (i % 2 == 0) {
        j = (paramInt7 + j) % size;
      } else {
        j = (size + paramInt7 - j - 1) % size;
      }
      if ((paramInt1 == uid[j]) && (paramInt2 == set[j]) && (paramInt3 == tag[j]) && (paramInt4 == metered[j]) && (paramInt5 == roaming[j]) && (paramInt6 == defaultNetwork[j]) && (Objects.equals(paramString, iface[j]))) {
        return j;
      }
    }
    return -1;
  }
  
  public long getElapsedRealtime()
  {
    return elapsedRealtime;
  }
  
  public long getElapsedRealtimeAge()
  {
    return SystemClock.elapsedRealtime() - elapsedRealtime;
  }
  
  public Entry getTotal(Entry paramEntry)
  {
    return getTotal(paramEntry, null, -1, false);
  }
  
  public Entry getTotal(Entry paramEntry, int paramInt)
  {
    return getTotal(paramEntry, null, paramInt, false);
  }
  
  public Entry getTotal(Entry paramEntry, HashSet<String> paramHashSet)
  {
    return getTotal(paramEntry, paramHashSet, -1, false);
  }
  
  public long getTotalBytes()
  {
    Entry localEntry = getTotal(null);
    return rxBytes + txBytes;
  }
  
  public Entry getTotalIncludingTags(Entry paramEntry)
  {
    return getTotal(paramEntry, null, -1, true);
  }
  
  public long getTotalPackets()
  {
    long l = 0L;
    for (int i = size - 1; i >= 0; i--) {
      l += rxPackets[i] + txPackets[i];
    }
    return l;
  }
  
  public String[] getUniqueIfaces()
  {
    HashSet localHashSet = new HashSet();
    for (String str : iface) {
      if (str != IFACE_ALL) {
        localHashSet.add(str);
      }
    }
    return (String[])localHashSet.toArray(new String[localHashSet.size()]);
  }
  
  public int[] getUniqueUids()
  {
    SparseBooleanArray localSparseBooleanArray = new SparseBooleanArray();
    int[] arrayOfInt = uid;
    int i = arrayOfInt.length;
    int j = 0;
    for (int k = 0; k < i; k++) {
      localSparseBooleanArray.put(arrayOfInt[k], true);
    }
    i = localSparseBooleanArray.size();
    arrayOfInt = new int[i];
    for (k = j; k < i; k++) {
      arrayOfInt[k] = localSparseBooleanArray.keyAt(k);
    }
    return arrayOfInt;
  }
  
  public Entry getValues(int paramInt, Entry paramEntry)
  {
    if (paramEntry == null) {
      paramEntry = new Entry();
    }
    iface = iface[paramInt];
    uid = uid[paramInt];
    set = set[paramInt];
    tag = tag[paramInt];
    metered = metered[paramInt];
    roaming = roaming[paramInt];
    defaultNetwork = defaultNetwork[paramInt];
    rxBytes = rxBytes[paramInt];
    rxPackets = rxPackets[paramInt];
    txBytes = txBytes[paramInt];
    txPackets = txPackets[paramInt];
    operations = operations[paramInt];
    return paramEntry;
  }
  
  public NetworkStats groupedByIface()
  {
    NetworkStats localNetworkStats = new NetworkStats(elapsedRealtime, 10);
    Entry localEntry = new Entry();
    uid = -1;
    set = -1;
    int i = 0;
    tag = 0;
    metered = -1;
    roaming = -1;
    defaultNetwork = -1;
    operations = 0L;
    while (i < size)
    {
      if (tag[i] == 0)
      {
        iface = iface[i];
        rxBytes = rxBytes[i];
        rxPackets = rxPackets[i];
        txBytes = txBytes[i];
        txPackets = txPackets[i];
        localNetworkStats.combineValues(localEntry);
      }
      i++;
    }
    return localNetworkStats;
  }
  
  public NetworkStats groupedByUid()
  {
    NetworkStats localNetworkStats = new NetworkStats(elapsedRealtime, 10);
    Entry localEntry = new Entry();
    iface = IFACE_ALL;
    set = -1;
    int i = 0;
    tag = 0;
    metered = -1;
    roaming = -1;
    defaultNetwork = -1;
    while (i < size)
    {
      if (tag[i] == 0)
      {
        uid = uid[i];
        rxBytes = rxBytes[i];
        rxPackets = rxPackets[i];
        txBytes = txBytes[i];
        txPackets = txPackets[i];
        operations = operations[i];
        localNetworkStats.combineValues(localEntry);
      }
      i++;
    }
    return localNetworkStats;
  }
  
  @VisibleForTesting
  public int internalSize()
  {
    return capacity;
  }
  
  public boolean migrateTun(int paramInt, String paramString1, String paramString2)
  {
    Entry localEntry1 = new Entry();
    Entry localEntry2 = new Entry();
    tunAdjustmentInit(paramInt, paramString1, paramString2, localEntry1, localEntry2);
    localEntry2 = tunGetPool(localEntry1, localEntry2);
    if (localEntry2.isEmpty()) {
      return true;
    }
    paramString1 = addTrafficToApplications(paramInt, paramString1, paramString2, localEntry1, localEntry2);
    deductTrafficFromVpnApp(paramInt, paramString2, paramString1);
    if (!paramString1.isEmpty())
    {
      paramString2 = new StringBuilder();
      paramString2.append("Failed to deduct underlying network traffic from VPN package. Moved=");
      paramString2.append(paramString1);
      Slog.wtf("NetworkStats", paramString2.toString());
      return false;
    }
    return true;
  }
  
  public void setElapsedRealtime(long paramLong)
  {
    elapsedRealtime = paramLong;
  }
  
  public int size()
  {
    return size;
  }
  
  public void spliceOperationsFrom(NetworkStats paramNetworkStats)
  {
    for (int i = 0; i < size; i++)
    {
      int j = paramNetworkStats.findIndex(iface[i], uid[i], set[i], tag[i], metered[i], roaming[i], defaultNetwork[i]);
      if (j == -1) {
        operations[i] = 0L;
      } else {
        operations[i] = operations[j];
      }
    }
  }
  
  public NetworkStats subtract(NetworkStats paramNetworkStats)
  {
    return subtract(this, paramNetworkStats, null, null);
  }
  
  public String toString()
  {
    CharArrayWriter localCharArrayWriter = new CharArrayWriter();
    dump("", new PrintWriter(localCharArrayWriter));
    return localCharArrayWriter.toString();
  }
  
  public NetworkStats withoutUids(int[] paramArrayOfInt)
  {
    NetworkStats localNetworkStats = new NetworkStats(elapsedRealtime, 10);
    Entry localEntry = new Entry();
    for (int i = 0; i < size; i++)
    {
      localEntry = getValues(i, localEntry);
      if (!ArrayUtils.contains(paramArrayOfInt, uid)) {
        localNetworkStats.addValues(localEntry);
      }
    }
    return localNetworkStats;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(elapsedRealtime);
    paramParcel.writeInt(size);
    paramParcel.writeInt(capacity);
    paramParcel.writeStringArray(iface);
    paramParcel.writeIntArray(uid);
    paramParcel.writeIntArray(set);
    paramParcel.writeIntArray(tag);
    paramParcel.writeIntArray(metered);
    paramParcel.writeIntArray(roaming);
    paramParcel.writeIntArray(defaultNetwork);
    paramParcel.writeLongArray(rxBytes);
    paramParcel.writeLongArray(rxPackets);
    paramParcel.writeLongArray(txBytes);
    paramParcel.writeLongArray(txPackets);
    paramParcel.writeLongArray(operations);
  }
  
  public static class Entry
  {
    public int defaultNetwork;
    public String iface;
    public int metered;
    public long operations;
    public int roaming;
    public long rxBytes;
    public long rxPackets;
    public int set;
    public int tag;
    public long txBytes;
    public long txPackets;
    public int uid;
    
    public Entry()
    {
      this(NetworkStats.IFACE_ALL, -1, 0, 0, 0L, 0L, 0L, 0L, 0L);
    }
    
    public Entry(long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5)
    {
      this(NetworkStats.IFACE_ALL, -1, 0, 0, paramLong1, paramLong2, paramLong3, paramLong4, paramLong5);
    }
    
    public Entry(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5)
    {
      iface = paramString;
      uid = paramInt1;
      set = paramInt2;
      tag = paramInt3;
      metered = paramInt4;
      roaming = paramInt5;
      defaultNetwork = paramInt6;
      rxBytes = paramLong1;
      rxPackets = paramLong2;
      txBytes = paramLong3;
      txPackets = paramLong4;
      operations = paramLong5;
    }
    
    public Entry(String paramString, int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5)
    {
      this(paramString, paramInt1, paramInt2, paramInt3, 0, 0, 0, paramLong1, paramLong2, paramLong3, paramLong4, paramLong5);
    }
    
    public void add(Entry paramEntry)
    {
      rxBytes += rxBytes;
      rxPackets += rxPackets;
      txBytes += txBytes;
      txPackets += txPackets;
      operations += operations;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool1 = paramObject instanceof Entry;
      boolean bool2 = false;
      if (bool1)
      {
        paramObject = (Entry)paramObject;
        bool1 = bool2;
        if (uid == uid)
        {
          bool1 = bool2;
          if (set == set)
          {
            bool1 = bool2;
            if (tag == tag)
            {
              bool1 = bool2;
              if (metered == metered)
              {
                bool1 = bool2;
                if (roaming == roaming)
                {
                  bool1 = bool2;
                  if (defaultNetwork == defaultNetwork)
                  {
                    bool1 = bool2;
                    if (rxBytes == rxBytes)
                    {
                      bool1 = bool2;
                      if (rxPackets == rxPackets)
                      {
                        bool1 = bool2;
                        if (txBytes == txBytes)
                        {
                          bool1 = bool2;
                          if (txPackets == txPackets)
                          {
                            bool1 = bool2;
                            if (operations == operations)
                            {
                              bool1 = bool2;
                              if (iface.equals(iface)) {
                                bool1 = true;
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        return bool1;
      }
      return false;
    }
    
    public int hashCode()
    {
      return Objects.hash(new Object[] { Integer.valueOf(uid), Integer.valueOf(set), Integer.valueOf(tag), Integer.valueOf(metered), Integer.valueOf(roaming), Integer.valueOf(defaultNetwork), iface });
    }
    
    public boolean isEmpty()
    {
      boolean bool;
      if ((rxBytes == 0L) && (rxPackets == 0L) && (txBytes == 0L) && (txPackets == 0L) && (operations == 0L)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isNegative()
    {
      boolean bool;
      if ((rxBytes >= 0L) && (rxPackets >= 0L) && (txBytes >= 0L) && (txPackets >= 0L) && (operations >= 0L)) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("iface=");
      localStringBuilder.append(iface);
      localStringBuilder.append(" uid=");
      localStringBuilder.append(uid);
      localStringBuilder.append(" set=");
      localStringBuilder.append(NetworkStats.setToString(set));
      localStringBuilder.append(" tag=");
      localStringBuilder.append(NetworkStats.tagToString(tag));
      localStringBuilder.append(" metered=");
      localStringBuilder.append(NetworkStats.meteredToString(metered));
      localStringBuilder.append(" roaming=");
      localStringBuilder.append(NetworkStats.roamingToString(roaming));
      localStringBuilder.append(" defaultNetwork=");
      localStringBuilder.append(NetworkStats.defaultNetworkToString(defaultNetwork));
      localStringBuilder.append(" rxBytes=");
      localStringBuilder.append(rxBytes);
      localStringBuilder.append(" rxPackets=");
      localStringBuilder.append(rxPackets);
      localStringBuilder.append(" txBytes=");
      localStringBuilder.append(txBytes);
      localStringBuilder.append(" txPackets=");
      localStringBuilder.append(txPackets);
      localStringBuilder.append(" operations=");
      localStringBuilder.append(operations);
      return localStringBuilder.toString();
    }
  }
  
  public static abstract interface NonMonotonicObserver<C>
  {
    public abstract void foundNonMonotonic(NetworkStats paramNetworkStats1, int paramInt1, NetworkStats paramNetworkStats2, int paramInt2, C paramC);
    
    public abstract void foundNonMonotonic(NetworkStats paramNetworkStats, int paramInt, C paramC);
  }
}
