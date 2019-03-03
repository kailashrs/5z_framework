package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.MathUtils;
import android.util.proto.ProtoOutputStream;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.IndentingPrintWriter;
import java.io.CharArrayWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ProtocolException;
import java.util.Arrays;
import java.util.Random;
import libcore.util.EmptyArray;

public class NetworkStatsHistory
  implements Parcelable
{
  public static final Parcelable.Creator<NetworkStatsHistory> CREATOR = new Parcelable.Creator()
  {
    public NetworkStatsHistory createFromParcel(Parcel paramAnonymousParcel)
    {
      return new NetworkStatsHistory(paramAnonymousParcel);
    }
    
    public NetworkStatsHistory[] newArray(int paramAnonymousInt)
    {
      return new NetworkStatsHistory[paramAnonymousInt];
    }
  };
  public static final int FIELD_ACTIVE_TIME = 1;
  public static final int FIELD_ALL = -1;
  public static final int FIELD_OPERATIONS = 32;
  public static final int FIELD_RX_BYTES = 2;
  public static final int FIELD_RX_PACKETS = 4;
  public static final int FIELD_TX_BYTES = 8;
  public static final int FIELD_TX_PACKETS = 16;
  private static final int VERSION_ADD_ACTIVE = 3;
  private static final int VERSION_ADD_PACKETS = 2;
  private static final int VERSION_INIT = 1;
  private long[] activeTime;
  private long adjustValidateTime = 0L;
  private int bucketCount;
  private long bucketDuration;
  private long[] bucketStart;
  private long[] operations;
  private long[] rxBytes;
  private long[] rxPackets;
  private long totalBytes;
  private long totalBytesAdjust = 0L;
  private long[] txBytes;
  private long[] txPackets;
  
  public NetworkStatsHistory(long paramLong)
  {
    this(paramLong, 10, -1);
  }
  
  public NetworkStatsHistory(long paramLong, int paramInt)
  {
    this(paramLong, paramInt, -1);
  }
  
  public NetworkStatsHistory(long paramLong, int paramInt1, int paramInt2)
  {
    bucketDuration = paramLong;
    bucketStart = new long[paramInt1];
    if ((paramInt2 & 0x1) != 0) {
      activeTime = new long[paramInt1];
    }
    if ((paramInt2 & 0x2) != 0) {
      rxBytes = new long[paramInt1];
    }
    if ((paramInt2 & 0x4) != 0) {
      rxPackets = new long[paramInt1];
    }
    if ((paramInt2 & 0x8) != 0) {
      txBytes = new long[paramInt1];
    }
    if ((paramInt2 & 0x10) != 0) {
      txPackets = new long[paramInt1];
    }
    if ((paramInt2 & 0x20) != 0) {
      operations = new long[paramInt1];
    }
    bucketCount = 0;
    totalBytes = 0L;
  }
  
  public NetworkStatsHistory(NetworkStatsHistory paramNetworkStatsHistory, long paramLong)
  {
    this(paramLong, paramNetworkStatsHistory.estimateResizeBuckets(paramLong));
    recordEntireHistory(paramNetworkStatsHistory);
  }
  
  public NetworkStatsHistory(Parcel paramParcel)
  {
    bucketDuration = paramParcel.readLong();
    bucketStart = ParcelUtils.readLongArray(paramParcel);
    activeTime = ParcelUtils.readLongArray(paramParcel);
    rxBytes = ParcelUtils.readLongArray(paramParcel);
    rxPackets = ParcelUtils.readLongArray(paramParcel);
    txBytes = ParcelUtils.readLongArray(paramParcel);
    txPackets = ParcelUtils.readLongArray(paramParcel);
    operations = ParcelUtils.readLongArray(paramParcel);
    bucketCount = bucketStart.length;
    totalBytes = paramParcel.readLong();
    totalBytesAdjust = paramParcel.readLong();
    adjustValidateTime = paramParcel.readLong();
  }
  
  public NetworkStatsHistory(DataInputStream paramDataInputStream)
    throws IOException
  {
    int i = paramDataInputStream.readInt();
    switch (i)
    {
    default: 
      paramDataInputStream = new StringBuilder();
      paramDataInputStream.append("unexpected version: ");
      paramDataInputStream.append(i);
      throw new ProtocolException(paramDataInputStream.toString());
    case 2: 
    case 3: 
      bucketDuration = paramDataInputStream.readLong();
      bucketStart = DataStreamUtils.readVarLongArray(paramDataInputStream);
      long[] arrayOfLong;
      if (i >= 3) {
        arrayOfLong = DataStreamUtils.readVarLongArray(paramDataInputStream);
      } else {
        arrayOfLong = new long[bucketStart.length];
      }
      activeTime = arrayOfLong;
      rxBytes = DataStreamUtils.readVarLongArray(paramDataInputStream);
      rxPackets = DataStreamUtils.readVarLongArray(paramDataInputStream);
      txBytes = DataStreamUtils.readVarLongArray(paramDataInputStream);
      txPackets = DataStreamUtils.readVarLongArray(paramDataInputStream);
      operations = DataStreamUtils.readVarLongArray(paramDataInputStream);
      bucketCount = bucketStart.length;
      totalBytes = (ArrayUtils.total(rxBytes) + ArrayUtils.total(txBytes));
      break;
    case 1: 
      bucketDuration = paramDataInputStream.readLong();
      bucketStart = DataStreamUtils.readFullLongArray(paramDataInputStream);
      rxBytes = DataStreamUtils.readFullLongArray(paramDataInputStream);
      rxPackets = new long[bucketStart.length];
      txBytes = DataStreamUtils.readFullLongArray(paramDataInputStream);
      txPackets = new long[bucketStart.length];
      operations = new long[bucketStart.length];
      bucketCount = bucketStart.length;
      totalBytes = (ArrayUtils.total(rxBytes) + ArrayUtils.total(txBytes));
    }
    if ((bucketStart.length == bucketCount) && (rxBytes.length == bucketCount) && (rxPackets.length == bucketCount) && (txBytes.length == bucketCount) && (txPackets.length == bucketCount) && (operations.length == bucketCount)) {
      return;
    }
    throw new ProtocolException("Mismatched history lengths");
  }
  
  private static void addLong(long[] paramArrayOfLong, int paramInt, long paramLong)
  {
    if (paramArrayOfLong != null) {
      paramArrayOfLong[paramInt] += paramLong;
    }
  }
  
  private void ensureBuckets(long paramLong1, long paramLong2)
  {
    long l1 = bucketDuration;
    long l2 = bucketDuration;
    long l3 = bucketDuration;
    long l4 = bucketDuration;
    for (paramLong1 -= paramLong1 % l1; paramLong1 < paramLong2 + (l2 - paramLong2 % l3) % l4; paramLong1 += bucketDuration)
    {
      int i = Arrays.binarySearch(bucketStart, 0, bucketCount, paramLong1);
      if (i < 0) {
        insertBucket(i, paramLong1);
      }
    }
  }
  
  private static long getLong(long[] paramArrayOfLong, int paramInt, long paramLong)
  {
    if (paramArrayOfLong != null) {
      paramLong = paramArrayOfLong[paramInt];
    }
    return paramLong;
  }
  
  private void insertBucket(int paramInt, long paramLong)
  {
    int i;
    if (bucketCount >= bucketStart.length)
    {
      i = Math.max(bucketStart.length, 10) * 3 / 2;
      bucketStart = Arrays.copyOf(bucketStart, i);
      if (activeTime != null) {
        activeTime = Arrays.copyOf(activeTime, i);
      }
      if (rxBytes != null) {
        rxBytes = Arrays.copyOf(rxBytes, i);
      }
      if (rxPackets != null) {
        rxPackets = Arrays.copyOf(rxPackets, i);
      }
      if (txBytes != null) {
        txBytes = Arrays.copyOf(txBytes, i);
      }
      if (txPackets != null) {
        txPackets = Arrays.copyOf(txPackets, i);
      }
      if (operations != null) {
        operations = Arrays.copyOf(operations, i);
      }
    }
    if (paramInt < bucketCount)
    {
      int j = paramInt + 1;
      i = bucketCount - paramInt;
      System.arraycopy(bucketStart, paramInt, bucketStart, j, i);
      if (activeTime != null) {
        System.arraycopy(activeTime, paramInt, activeTime, j, i);
      }
      if (rxBytes != null) {
        System.arraycopy(rxBytes, paramInt, rxBytes, j, i);
      }
      if (rxPackets != null) {
        System.arraycopy(rxPackets, paramInt, rxPackets, j, i);
      }
      if (txBytes != null) {
        System.arraycopy(txBytes, paramInt, txBytes, j, i);
      }
      if (txPackets != null) {
        System.arraycopy(txPackets, paramInt, txPackets, j, i);
      }
      if (operations != null) {
        System.arraycopy(operations, paramInt, operations, j, i);
      }
    }
    bucketStart[paramInt] = paramLong;
    setLong(activeTime, paramInt, 0L);
    setLong(rxBytes, paramInt, 0L);
    setLong(rxPackets, paramInt, 0L);
    setLong(txBytes, paramInt, 0L);
    setLong(txPackets, paramInt, 0L);
    setLong(operations, paramInt, 0L);
    bucketCount += 1;
  }
  
  public static long randomLong(Random paramRandom, long paramLong1, long paramLong2)
  {
    return ((float)paramLong1 + paramRandom.nextFloat() * (float)(paramLong2 - paramLong1));
  }
  
  private static void setLong(long[] paramArrayOfLong, int paramInt, long paramLong)
  {
    if (paramArrayOfLong != null) {
      paramArrayOfLong[paramInt] = paramLong;
    }
  }
  
  private static void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong, long[] paramArrayOfLong, int paramInt)
  {
    if (paramArrayOfLong != null) {
      paramProtoOutputStream.write(paramLong, paramArrayOfLong[paramInt]);
    }
  }
  
  public void clear()
  {
    bucketStart = EmptyArray.LONG;
    if (activeTime != null) {
      activeTime = EmptyArray.LONG;
    }
    if (rxBytes != null) {
      rxBytes = EmptyArray.LONG;
    }
    if (rxPackets != null) {
      rxPackets = EmptyArray.LONG;
    }
    if (txBytes != null) {
      txBytes = EmptyArray.LONG;
    }
    if (txPackets != null) {
      txPackets = EmptyArray.LONG;
    }
    if (operations != null) {
      operations = EmptyArray.LONG;
    }
    bucketCount = 0;
    totalBytes = 0L;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void dump(IndentingPrintWriter paramIndentingPrintWriter, boolean paramBoolean)
  {
    paramIndentingPrintWriter.print("NetworkStatsHistory: bucketDuration=");
    paramIndentingPrintWriter.println(bucketDuration / 1000L);
    paramIndentingPrintWriter.increaseIndent();
    int i = 0;
    if (!paramBoolean) {
      i = Math.max(0, bucketCount - 32);
    }
    if (i > 0)
    {
      paramIndentingPrintWriter.print("(omitting ");
      paramIndentingPrintWriter.print(i);
      paramIndentingPrintWriter.println(" buckets)");
    }
    while (i < bucketCount)
    {
      paramIndentingPrintWriter.print("st=");
      paramIndentingPrintWriter.print(bucketStart[i] / 1000L);
      if (rxBytes != null)
      {
        paramIndentingPrintWriter.print(" rb=");
        paramIndentingPrintWriter.print(rxBytes[i]);
      }
      if (rxPackets != null)
      {
        paramIndentingPrintWriter.print(" rp=");
        paramIndentingPrintWriter.print(rxPackets[i]);
      }
      if (txBytes != null)
      {
        paramIndentingPrintWriter.print(" tb=");
        paramIndentingPrintWriter.print(txBytes[i]);
      }
      if (txPackets != null)
      {
        paramIndentingPrintWriter.print(" tp=");
        paramIndentingPrintWriter.print(txPackets[i]);
      }
      if (operations != null)
      {
        paramIndentingPrintWriter.print(" op=");
        paramIndentingPrintWriter.print(operations[i]);
      }
      paramIndentingPrintWriter.println();
      i++;
    }
    paramIndentingPrintWriter.decreaseIndent();
  }
  
  public void dumpCheckin(PrintWriter paramPrintWriter)
  {
    paramPrintWriter.print("d,");
    paramPrintWriter.print(bucketDuration / 1000L);
    paramPrintWriter.println();
    for (int i = 0; i < bucketCount; i++)
    {
      paramPrintWriter.print("b,");
      paramPrintWriter.print(bucketStart[i] / 1000L);
      paramPrintWriter.print(',');
      if (rxBytes != null) {
        paramPrintWriter.print(rxBytes[i]);
      } else {
        paramPrintWriter.print("*");
      }
      paramPrintWriter.print(',');
      if (rxPackets != null) {
        paramPrintWriter.print(rxPackets[i]);
      } else {
        paramPrintWriter.print("*");
      }
      paramPrintWriter.print(',');
      if (txBytes != null) {
        paramPrintWriter.print(txBytes[i]);
      } else {
        paramPrintWriter.print("*");
      }
      paramPrintWriter.print(',');
      if (txPackets != null) {
        paramPrintWriter.print(txPackets[i]);
      } else {
        paramPrintWriter.print("*");
      }
      paramPrintWriter.print(',');
      if (operations != null) {
        paramPrintWriter.print(operations[i]);
      } else {
        paramPrintWriter.print("*");
      }
      paramPrintWriter.println();
    }
  }
  
  public int estimateResizeBuckets(long paramLong)
  {
    return (int)(size() * getBucketDuration() / paramLong);
  }
  
  @Deprecated
  public void generateRandom(long paramLong1, long paramLong2, long paramLong3)
  {
    Random localRandom = new Random();
    float f = localRandom.nextFloat();
    long l = ((float)paramLong3 * f);
    paramLong3 = ((float)paramLong3 * (1.0F - f));
    generateRandom(paramLong1, paramLong2, l, l / 1024L, paramLong3, paramLong3 / 1024L, l / 2048L, localRandom);
  }
  
  @Deprecated
  public void generateRandom(long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5, long paramLong6, long paramLong7, Random paramRandom)
  {
    ensureBuckets(paramLong1, paramLong2);
    NetworkStats.Entry localEntry = new NetworkStats.Entry(NetworkStats.IFACE_ALL, -1, 0, 0, 0L, 0L, 0L, 0L, 0L);
    long l1 = paramLong3;
    long l2 = paramLong4;
    paramLong4 = paramLong6;
    paramLong3 = paramLong7;
    paramLong6 = l2;
    for (;;)
    {
      paramLong7 = paramLong2;
      if ((l1 <= 1024L) && (paramLong6 <= 128L) && (paramLong5 <= 1024L) && (paramLong4 <= 128L) && (paramLong3 <= 32L)) {
        return;
      }
      l2 = randomLong(paramRandom, paramLong1, paramLong7);
      paramLong7 = randomLong(paramRandom, 0L, (paramLong7 - l2) / 2L);
      rxBytes = randomLong(paramRandom, 0L, l1);
      rxPackets = randomLong(paramRandom, 0L, paramLong6);
      txBytes = randomLong(paramRandom, 0L, paramLong5);
      txPackets = randomLong(paramRandom, 0L, paramLong4);
      operations = randomLong(paramRandom, 0L, paramLong3);
      l1 -= rxBytes;
      paramLong6 -= rxPackets;
      paramLong5 -= txBytes;
      paramLong4 -= txPackets;
      paramLong3 -= operations;
      recordData(l2, l2 + paramLong7, localEntry);
    }
  }
  
  public long getBucketDuration()
  {
    return bucketDuration;
  }
  
  public long getEnd()
  {
    if (bucketCount > 0) {
      return bucketStart[(bucketCount - 1)] + bucketDuration;
    }
    return Long.MIN_VALUE;
  }
  
  public int getIndexAfter(long paramLong)
  {
    int i = Arrays.binarySearch(bucketStart, 0, bucketCount, paramLong);
    if (i < 0) {
      i = i;
    } else {
      i++;
    }
    return MathUtils.constrain(i, 0, bucketCount - 1);
  }
  
  public int getIndexBefore(long paramLong)
  {
    int i = Arrays.binarySearch(bucketStart, 0, bucketCount, paramLong);
    if (i < 0) {
      i -= 1;
    }
    for (;;)
    {
      break;
      i--;
    }
    return MathUtils.constrain(i, 0, bucketCount - 1);
  }
  
  public long getStart()
  {
    if (bucketCount > 0) {
      return bucketStart[0];
    }
    return Long.MAX_VALUE;
  }
  
  public long getTotalBytes()
  {
    return totalBytes + totalBytesAdjust;
  }
  
  public Entry getValues(int paramInt, Entry paramEntry)
  {
    if (paramEntry == null) {
      paramEntry = new Entry();
    }
    bucketStart = bucketStart[paramInt];
    bucketDuration = bucketDuration;
    activeTime = getLong(activeTime, paramInt, -1L);
    rxBytes = getLong(rxBytes, paramInt, -1L);
    rxPackets = getLong(rxPackets, paramInt, -1L);
    txBytes = getLong(txBytes, paramInt, -1L);
    txPackets = getLong(txPackets, paramInt, -1L);
    operations = getLong(operations, paramInt, -1L);
    return paramEntry;
  }
  
  public Entry getValues(long paramLong1, long paramLong2, long paramLong3, Entry paramEntry)
  {
    long l1 = paramLong2;
    if (paramEntry == null) {
      paramEntry = new Entry();
    }
    bucketDuration = (l1 - paramLong1);
    bucketStart = paramLong1;
    long[] arrayOfLong = activeTime;
    long l2 = -1L;
    if (arrayOfLong != null) {
      l3 = 0L;
    } else {
      l3 = -1L;
    }
    activeTime = l3;
    if (rxBytes != null) {
      l3 = 0L;
    } else {
      l3 = -1L;
    }
    rxBytes = l3;
    if (rxPackets != null) {
      l3 = 0L;
    } else {
      l3 = -1L;
    }
    rxPackets = l3;
    if (txBytes != null) {
      l3 = 0L;
    } else {
      l3 = -1L;
    }
    txBytes = l3;
    if (txPackets != null) {
      l3 = 0L;
    } else {
      l3 = -1L;
    }
    txPackets = l3;
    long l3 = l2;
    if (operations != null) {
      l3 = 0L;
    }
    operations = l3;
    for (int i = getIndexAfter(l1);; i--)
    {
      l3 = paramLong2;
      if (i < 0) {
        break;
      }
      l1 = bucketStart[i];
      l2 = l1 + bucketDuration;
      if (l2 <= paramLong1) {
        break;
      }
      if (l1 < l3)
      {
        int j;
        if ((l1 < paramLong3) && (l2 > paramLong3)) {
          j = 1;
        } else {
          j = 0;
        }
        if (j != 0)
        {
          l3 = bucketDuration;
        }
        else
        {
          if (l2 < l3) {
            l3 = l2;
          }
          if (l1 <= paramLong1) {
            l1 = paramLong1;
          }
          l3 -= l1;
        }
        if (l3 > 0L)
        {
          if (activeTime != null) {
            activeTime += activeTime[i] * l3 / bucketDuration;
          }
          if (rxBytes != null) {
            rxBytes += rxBytes[i] * l3 / bucketDuration;
          }
          if (rxPackets != null) {
            rxPackets += rxPackets[i] * l3 / bucketDuration;
          }
          if (txBytes != null) {
            txBytes += txBytes[i] * l3 / bucketDuration;
          }
          if (txPackets != null) {
            txPackets += txPackets[i] * l3 / bucketDuration;
          }
          if (operations != null) {
            operations += operations[i] * l3 / bucketDuration;
          }
        }
      }
    }
    if ((adjustValidateTime >= paramLong1) && (adjustValidateTime <= paramLong2))
    {
      rxBytes += totalBytesAdjust;
      if (rxBytes < 0L)
      {
        txBytes += rxBytes;
        rxBytes = 0L;
      }
    }
    return paramEntry;
  }
  
  public Entry getValues(long paramLong1, long paramLong2, Entry paramEntry)
  {
    return getValues(paramLong1, paramLong2, Long.MAX_VALUE, paramEntry);
  }
  
  public boolean intersects(long paramLong1, long paramLong2)
  {
    long l1 = getStart();
    long l2 = getEnd();
    if ((paramLong1 >= l1) && (paramLong1 <= l2)) {
      return true;
    }
    if ((paramLong2 >= l1) && (paramLong2 <= l2)) {
      return true;
    }
    if ((l1 >= paramLong1) && (l1 <= paramLong2)) {
      return true;
    }
    return (l2 >= paramLong1) && (l2 <= paramLong2);
  }
  
  @Deprecated
  public void recordData(long paramLong1, long paramLong2, long paramLong3, long paramLong4)
  {
    recordData(paramLong1, paramLong2, new NetworkStats.Entry(NetworkStats.IFACE_ALL, -1, 0, 0, paramLong3, 0L, paramLong4, 0L, 0L));
  }
  
  public void recordData(long paramLong1, long paramLong2, NetworkStats.Entry paramEntry)
  {
    long l1 = paramLong2;
    long l2 = rxBytes;
    long l3 = rxPackets;
    long l4 = txBytes;
    long l5 = txPackets;
    long l6 = operations;
    if (!paramEntry.isNegative())
    {
      if (paramEntry.isEmpty()) {
        return;
      }
      ensureBuckets(paramLong1, paramLong2);
      long l7 = l1 - paramLong1;
      int i = getIndexAfter(l1);
      l1 = l2;
      for (;;)
      {
        long l8 = paramLong2;
        l2 = paramLong1;
        if (i < 0) {
          break;
        }
        long l9 = bucketStart[i];
        long l10 = bucketDuration + l9;
        if (l10 < l2) {
          break;
        }
        if (l9 <= l8)
        {
          l8 = Math.min(l10, l8) - Math.max(l9, l2);
          if (l8 <= 0L) {}
        }
        else
        {
          long l11 = l1 * l8 / l7;
          long l12 = l3 * l8 / l7;
          l10 = l4 * l8 / l7;
          l9 = l5 * l8 / l7;
          l2 = l6 * l8 / l7;
          long[] arrayOfLong = activeTime;
          int j = i;
          addLong(arrayOfLong, j, l8);
          addLong(rxBytes, j, l11);
          l1 -= l11;
          addLong(rxPackets, j, l12);
          l3 -= l12;
          addLong(txBytes, j, l10);
          l4 -= l10;
          addLong(txPackets, j, l9);
          l5 -= l9;
          addLong(operations, j, l2);
          l7 -= l8;
          l6 -= l2;
        }
        i--;
      }
      totalBytes += rxBytes + txBytes;
      return;
    }
    throw new IllegalArgumentException("tried recording negative data");
  }
  
  public void recordEntireHistory(NetworkStatsHistory paramNetworkStatsHistory)
  {
    recordHistory(paramNetworkStatsHistory, Long.MIN_VALUE, Long.MAX_VALUE);
  }
  
  public void recordHistory(NetworkStatsHistory paramNetworkStatsHistory, long paramLong1, long paramLong2)
  {
    NetworkStats.Entry localEntry = new NetworkStats.Entry(NetworkStats.IFACE_ALL, -1, 0, 0, 0L, 0L, 0L, 0L, 0L);
    for (int i = 0; i < bucketCount; i++)
    {
      long l1 = bucketStart[i];
      long l2 = bucketDuration + l1;
      if ((l1 >= paramLong1) && (l2 <= paramLong2))
      {
        rxBytes = getLong(rxBytes, i, 0L);
        rxPackets = getLong(rxPackets, i, 0L);
        txBytes = getLong(txBytes, i, 0L);
        txPackets = getLong(txPackets, i, 0L);
        operations = getLong(operations, i, 0L);
        recordData(l1, l2, localEntry);
      }
    }
  }
  
  @Deprecated
  public void removeBucketsBefore(long paramLong)
  {
    for (int i = 0; i < bucketCount; i++)
    {
      long l = bucketStart[i];
      if (bucketDuration + l > paramLong) {
        break;
      }
    }
    if (i > 0)
    {
      int j = bucketStart.length;
      bucketStart = Arrays.copyOfRange(bucketStart, i, j);
      if (activeTime != null) {
        activeTime = Arrays.copyOfRange(activeTime, i, j);
      }
      if (rxBytes != null) {
        rxBytes = Arrays.copyOfRange(rxBytes, i, j);
      }
      if (rxPackets != null) {
        rxPackets = Arrays.copyOfRange(rxPackets, i, j);
      }
      if (txBytes != null) {
        txBytes = Arrays.copyOfRange(txBytes, i, j);
      }
      if (txPackets != null) {
        txPackets = Arrays.copyOfRange(txPackets, i, j);
      }
      if (operations != null) {
        operations = Arrays.copyOfRange(operations, i, j);
      }
      bucketCount -= i;
    }
  }
  
  public void setTotalBytesAdjust(long paramLong1, long paramLong2)
  {
    totalBytesAdjust = paramLong1;
    adjustValidateTime = paramLong2;
  }
  
  public void setValues(int paramInt, Entry paramEntry)
  {
    if (rxBytes != null) {
      totalBytes -= rxBytes[paramInt];
    }
    if (txBytes != null) {
      totalBytes -= txBytes[paramInt];
    }
    bucketStart[paramInt] = bucketStart;
    setLong(activeTime, paramInt, activeTime);
    setLong(rxBytes, paramInt, rxBytes);
    setLong(rxPackets, paramInt, rxPackets);
    setLong(txBytes, paramInt, txBytes);
    setLong(txPackets, paramInt, txPackets);
    setLong(operations, paramInt, operations);
    if (rxBytes != null) {
      totalBytes += rxBytes[paramInt];
    }
    if (txBytes != null) {
      totalBytes += txBytes[paramInt];
    }
  }
  
  public int size()
  {
    return bucketCount;
  }
  
  public String toString()
  {
    CharArrayWriter localCharArrayWriter = new CharArrayWriter();
    dump(new IndentingPrintWriter(localCharArrayWriter, "  "), false);
    return localCharArrayWriter.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(bucketDuration);
    ParcelUtils.writeLongArray(paramParcel, bucketStart, bucketCount);
    ParcelUtils.writeLongArray(paramParcel, activeTime, bucketCount);
    ParcelUtils.writeLongArray(paramParcel, rxBytes, bucketCount);
    ParcelUtils.writeLongArray(paramParcel, rxPackets, bucketCount);
    ParcelUtils.writeLongArray(paramParcel, txBytes, bucketCount);
    ParcelUtils.writeLongArray(paramParcel, txPackets, bucketCount);
    ParcelUtils.writeLongArray(paramParcel, operations, bucketCount);
    paramParcel.writeLong(totalBytes);
    paramParcel.writeLong(totalBytesAdjust);
    paramParcel.writeLong(adjustValidateTime);
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    long l = paramProtoOutputStream.start(paramLong);
    paramProtoOutputStream.write(1112396529665L, bucketDuration);
    for (int i = 0; i < bucketCount; i++)
    {
      paramLong = paramProtoOutputStream.start(2246267895810L);
      paramProtoOutputStream.write(1112396529665L, bucketStart[i]);
      writeToProto(paramProtoOutputStream, 1112396529666L, rxBytes, i);
      writeToProto(paramProtoOutputStream, 1112396529667L, rxPackets, i);
      writeToProto(paramProtoOutputStream, 1112396529668L, txBytes, i);
      writeToProto(paramProtoOutputStream, 1112396529669L, txPackets, i);
      writeToProto(paramProtoOutputStream, 1112396529670L, operations, i);
      paramProtoOutputStream.end(paramLong);
    }
    paramProtoOutputStream.end(l);
  }
  
  public void writeToStream(DataOutputStream paramDataOutputStream)
    throws IOException
  {
    paramDataOutputStream.writeInt(3);
    paramDataOutputStream.writeLong(bucketDuration);
    DataStreamUtils.writeVarLongArray(paramDataOutputStream, bucketStart, bucketCount);
    DataStreamUtils.writeVarLongArray(paramDataOutputStream, activeTime, bucketCount);
    DataStreamUtils.writeVarLongArray(paramDataOutputStream, rxBytes, bucketCount);
    DataStreamUtils.writeVarLongArray(paramDataOutputStream, rxPackets, bucketCount);
    DataStreamUtils.writeVarLongArray(paramDataOutputStream, txBytes, bucketCount);
    DataStreamUtils.writeVarLongArray(paramDataOutputStream, txPackets, bucketCount);
    DataStreamUtils.writeVarLongArray(paramDataOutputStream, operations, bucketCount);
  }
  
  public static class DataStreamUtils
  {
    public DataStreamUtils() {}
    
    @Deprecated
    public static long[] readFullLongArray(DataInputStream paramDataInputStream)
      throws IOException
    {
      int i = paramDataInputStream.readInt();
      if (i >= 0)
      {
        long[] arrayOfLong = new long[i];
        for (i = 0; i < arrayOfLong.length; i++) {
          arrayOfLong[i] = paramDataInputStream.readLong();
        }
        return arrayOfLong;
      }
      throw new ProtocolException("negative array size");
    }
    
    public static long readVarLong(DataInputStream paramDataInputStream)
      throws IOException
    {
      int i = 0;
      long l = 0L;
      while (i < 64)
      {
        int j = paramDataInputStream.readByte();
        l |= (j & 0x7F) << i;
        if ((j & 0x80) == 0) {
          return l;
        }
        i += 7;
      }
      throw new ProtocolException("malformed long");
    }
    
    public static long[] readVarLongArray(DataInputStream paramDataInputStream)
      throws IOException
    {
      int i = paramDataInputStream.readInt();
      if (i == -1) {
        return null;
      }
      if (i >= 0)
      {
        long[] arrayOfLong = new long[i];
        for (i = 0; i < arrayOfLong.length; i++) {
          arrayOfLong[i] = readVarLong(paramDataInputStream);
        }
        return arrayOfLong;
      }
      throw new ProtocolException("negative array size");
    }
    
    public static void writeVarLong(DataOutputStream paramDataOutputStream, long paramLong)
      throws IOException
    {
      for (;;)
      {
        if ((0xFFFFFFFFFFFFFF80 & paramLong) == 0L)
        {
          paramDataOutputStream.writeByte((int)paramLong);
          return;
        }
        paramDataOutputStream.writeByte((int)paramLong & 0x7F | 0x80);
        paramLong >>>= 7;
      }
    }
    
    public static void writeVarLongArray(DataOutputStream paramDataOutputStream, long[] paramArrayOfLong, int paramInt)
      throws IOException
    {
      if (paramArrayOfLong == null)
      {
        paramDataOutputStream.writeInt(-1);
        return;
      }
      if (paramInt <= paramArrayOfLong.length)
      {
        paramDataOutputStream.writeInt(paramInt);
        for (int i = 0; i < paramInt; i++) {
          writeVarLong(paramDataOutputStream, paramArrayOfLong[i]);
        }
        return;
      }
      throw new IllegalArgumentException("size larger than length");
    }
  }
  
  public static class Entry
  {
    public static final long UNKNOWN = -1L;
    public long activeTime;
    public long bucketDuration;
    public long bucketStart;
    public long operations;
    public long rxBytes;
    public long rxPackets;
    public long txBytes;
    public long txPackets;
    
    public Entry() {}
  }
  
  public static class ParcelUtils
  {
    public ParcelUtils() {}
    
    public static long[] readLongArray(Parcel paramParcel)
    {
      int i = paramParcel.readInt();
      if (i == -1) {
        return null;
      }
      long[] arrayOfLong = new long[i];
      for (i = 0; i < arrayOfLong.length; i++) {
        arrayOfLong[i] = paramParcel.readLong();
      }
      return arrayOfLong;
    }
    
    public static void writeLongArray(Parcel paramParcel, long[] paramArrayOfLong, int paramInt)
    {
      if (paramArrayOfLong == null)
      {
        paramParcel.writeInt(-1);
        return;
      }
      if (paramInt <= paramArrayOfLong.length)
      {
        paramParcel.writeInt(paramInt);
        for (int i = 0; i < paramInt; i++) {
          paramParcel.writeLong(paramArrayOfLong[i]);
        }
        return;
      }
      throw new IllegalArgumentException("size larger than length");
    }
  }
}
