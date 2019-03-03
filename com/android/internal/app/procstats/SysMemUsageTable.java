package com.android.internal.app.procstats;

import android.util.DebugUtils;
import java.io.PrintWriter;

public class SysMemUsageTable
  extends SparseMappingTable.Table
{
  public SysMemUsageTable(SparseMappingTable paramSparseMappingTable)
  {
    super(paramSparseMappingTable);
  }
  
  private void dumpCategory(PrintWriter paramPrintWriter, String paramString1, String paramString2, int paramInt1, int paramInt2)
  {
    paramPrintWriter.print(paramString1);
    paramPrintWriter.print(paramString2);
    paramPrintWriter.print(": ");
    DebugUtils.printSizeValue(paramPrintWriter, getValueForId((byte)paramInt1, paramInt2) * 1024L);
    paramPrintWriter.print(" min, ");
    DebugUtils.printSizeValue(paramPrintWriter, getValueForId((byte)paramInt1, paramInt2 + 1) * 1024L);
    paramPrintWriter.print(" avg, ");
    DebugUtils.printSizeValue(paramPrintWriter, getValueForId((byte)paramInt1, paramInt2 + 2) * 1024L);
    paramPrintWriter.println(" max");
  }
  
  public static void mergeSysMemUsage(long[] paramArrayOfLong1, int paramInt1, long[] paramArrayOfLong2, int paramInt2)
  {
    long l1 = paramArrayOfLong1[(paramInt1 + 0)];
    long l2 = paramArrayOfLong2[(paramInt2 + 0)];
    int i = 1;
    if (l1 == 0L)
    {
      paramArrayOfLong1[(paramInt1 + 0)] = l2;
      while (i < 16)
      {
        paramArrayOfLong1[(paramInt1 + i)] = paramArrayOfLong2[(paramInt2 + i)];
        i++;
      }
    }
    if (l2 > 0L)
    {
      paramArrayOfLong1[(paramInt1 + 0)] = (l1 + l2);
      for (i = 1; i < 16; i += 3)
      {
        if (paramArrayOfLong1[(paramInt1 + i)] > paramArrayOfLong2[(paramInt2 + i)]) {
          paramArrayOfLong1[(paramInt1 + i)] = paramArrayOfLong2[(paramInt2 + i)];
        }
        paramArrayOfLong1[(paramInt1 + i + 1)] = (((paramArrayOfLong1[(paramInt1 + i + 1)] * l1 + paramArrayOfLong2[(paramInt2 + i + 1)] * l2) / (l1 + l2)));
        if (paramArrayOfLong1[(paramInt1 + i + 2)] < paramArrayOfLong2[(paramInt2 + i + 2)]) {
          paramArrayOfLong1[(paramInt1 + i + 2)] = paramArrayOfLong2[(paramInt2 + i + 2)];
        }
      }
    }
  }
  
  public void dump(PrintWriter paramPrintWriter, String paramString, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    int i = -1;
    for (int j = 0; j < paramArrayOfInt1.length; j++)
    {
      int k = -1;
      int m = 0;
      while (m < paramArrayOfInt2.length)
      {
        int n = paramArrayOfInt1[j];
        int i1 = paramArrayOfInt2[m];
        int i2 = (n + i1) * 14;
        long l = getValueForId((byte)i2, 0);
        int i3 = k;
        int i4 = i;
        if (l > 0L)
        {
          paramPrintWriter.print(paramString);
          if (paramArrayOfInt1.length > 1)
          {
            if (i != n) {
              i = n;
            } else {
              i = -1;
            }
            DumpUtils.printScreenLabel(paramPrintWriter, i);
            i = n;
          }
          n = k;
          if (paramArrayOfInt2.length > 1)
          {
            if (k != i1) {
              k = i1;
            } else {
              k = -1;
            }
            DumpUtils.printMemLabel(paramPrintWriter, k, '\000');
            n = i1;
          }
          paramPrintWriter.print(": ");
          paramPrintWriter.print(l);
          paramPrintWriter.println(" samples:");
          dumpCategory(paramPrintWriter, paramString, "  Cached", i2, 1);
          dumpCategory(paramPrintWriter, paramString, "  Free", i2, 4);
          dumpCategory(paramPrintWriter, paramString, "  ZRam", i2, 7);
          dumpCategory(paramPrintWriter, paramString, "  Kernel", i2, 10);
          dumpCategory(paramPrintWriter, paramString, "  Native", i2, 13);
          i4 = i;
          i3 = n;
        }
        m++;
        k = i3;
        i = i4;
      }
    }
  }
  
  public long[] getTotalMemUsage()
  {
    long[] arrayOfLong = new long[16];
    int i = getKeyCount();
    for (int j = 0; j < i; j++)
    {
      int k = getKeyAt(j);
      mergeSysMemUsage(arrayOfLong, 0, getArrayForKey(k), SparseMappingTable.getIndexFromKey(k));
    }
    return arrayOfLong;
  }
  
  public void mergeStats(int paramInt1, long[] paramArrayOfLong, int paramInt2)
  {
    paramInt1 = getOrAddKey((byte)paramInt1, 16);
    mergeSysMemUsage(getArrayForKey(paramInt1), SparseMappingTable.getIndexFromKey(paramInt1), paramArrayOfLong, paramInt2);
  }
  
  public void mergeStats(SysMemUsageTable paramSysMemUsageTable)
  {
    int i = paramSysMemUsageTable.getKeyCount();
    for (int j = 0; j < i; j++)
    {
      int k = paramSysMemUsageTable.getKeyAt(j);
      mergeStats(SparseMappingTable.getIdFromKey(k), paramSysMemUsageTable.getArrayForKey(k), SparseMappingTable.getIndexFromKey(k));
    }
  }
}
