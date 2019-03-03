package com.android.internal.app.procstats;

import android.os.UserHandle;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public final class DumpUtils
{
  public static final String[] ADJ_MEM_NAMES_CSV;
  static final int[] ADJ_MEM_PROTO_ENUMS = { 1, 2, 3, 4 };
  static final String[] ADJ_MEM_TAGS;
  public static final String[] ADJ_SCREEN_NAMES_CSV;
  static final int[] ADJ_SCREEN_PROTO_ENUMS;
  static final String[] ADJ_SCREEN_TAGS;
  static final String CSV_SEP = "\t";
  public static final String[] STATE_NAMES = new String[14];
  public static final String[] STATE_NAMES_CSV;
  static final int[] STATE_PROTO_ENUMS;
  static final String[] STATE_TAGS;
  
  static
  {
    STATE_NAMES[0] = "Persist";
    STATE_NAMES[1] = "Top";
    STATE_NAMES[2] = "ImpFg";
    STATE_NAMES[3] = "ImpBg";
    STATE_NAMES[4] = "Backup";
    STATE_NAMES[5] = "Service";
    STATE_NAMES[6] = "ServRst";
    STATE_NAMES[7] = "Receivr";
    STATE_NAMES[8] = "HeavyWt";
    STATE_NAMES[9] = "Home";
    STATE_NAMES[10] = "LastAct";
    STATE_NAMES[11] = "CchAct";
    STATE_NAMES[12] = "CchCAct";
    STATE_NAMES[13] = "CchEmty";
    STATE_NAMES_CSV = new String[14];
    STATE_NAMES_CSV[0] = "pers";
    STATE_NAMES_CSV[1] = "top";
    STATE_NAMES_CSV[2] = "impfg";
    STATE_NAMES_CSV[3] = "impbg";
    STATE_NAMES_CSV[4] = "backup";
    STATE_NAMES_CSV[5] = "service";
    STATE_NAMES_CSV[6] = "service-rs";
    STATE_NAMES_CSV[7] = "receiver";
    STATE_NAMES_CSV[8] = "heavy";
    STATE_NAMES_CSV[9] = "home";
    STATE_NAMES_CSV[10] = "lastact";
    STATE_NAMES_CSV[11] = "cch-activity";
    STATE_NAMES_CSV[12] = "cch-aclient";
    STATE_NAMES_CSV[13] = "cch-empty";
    STATE_TAGS = new String[14];
    STATE_TAGS[0] = "p";
    STATE_TAGS[1] = "t";
    STATE_TAGS[2] = "f";
    STATE_TAGS[3] = "b";
    STATE_TAGS[4] = "u";
    STATE_TAGS[5] = "s";
    STATE_TAGS[6] = "x";
    STATE_TAGS[7] = "r";
    STATE_TAGS[8] = "w";
    STATE_TAGS[9] = "h";
    STATE_TAGS[10] = "l";
    STATE_TAGS[11] = "a";
    STATE_TAGS[12] = "c";
    STATE_TAGS[13] = "e";
    STATE_PROTO_ENUMS = new int[14];
    STATE_PROTO_ENUMS[0] = 1;
    STATE_PROTO_ENUMS[1] = 2;
    STATE_PROTO_ENUMS[2] = 3;
    STATE_PROTO_ENUMS[3] = 4;
    STATE_PROTO_ENUMS[4] = 5;
    STATE_PROTO_ENUMS[5] = 6;
    STATE_PROTO_ENUMS[6] = 7;
    STATE_PROTO_ENUMS[7] = 8;
    STATE_PROTO_ENUMS[8] = 9;
    STATE_PROTO_ENUMS[9] = 10;
    STATE_PROTO_ENUMS[10] = 11;
    STATE_PROTO_ENUMS[11] = 12;
    STATE_PROTO_ENUMS[12] = 13;
    STATE_PROTO_ENUMS[13] = 14;
    ADJ_SCREEN_NAMES_CSV = new String[] { "off", "on" };
    ADJ_MEM_NAMES_CSV = new String[] { "norm", "mod", "low", "crit" };
    ADJ_SCREEN_TAGS = new String[] { "0", "1" };
    ADJ_SCREEN_PROTO_ENUMS = new int[] { 1, 2 };
    ADJ_MEM_TAGS = new String[] { "n", "m", "l", "c" };
  }
  
  private DumpUtils() {}
  
  public static String collapseString(String paramString1, String paramString2)
  {
    if (paramString2.startsWith(paramString1))
    {
      int i = paramString2.length();
      int j = paramString1.length();
      if (i == j) {
        return "";
      }
      if ((i >= j) && (paramString2.charAt(j) == '.')) {
        return paramString2.substring(j);
      }
    }
    return paramString2;
  }
  
  public static void dumpAdjTimesCheckin(PrintWriter paramPrintWriter, String paramString, long[] paramArrayOfLong, int paramInt, long paramLong1, long paramLong2)
  {
    for (int i = 0; i < 8; i += 4) {
      for (int j = 0; j < 4; j++)
      {
        int k = j + i;
        long l1 = paramArrayOfLong[k];
        long l2 = l1;
        if (paramInt == k) {
          l2 = l1 + (paramLong2 - paramLong1);
        }
        if (l2 != 0L) {
          printAdjTagAndValue(paramPrintWriter, k, l2);
        }
      }
    }
  }
  
  public static void dumpProcessListCsv(PrintWriter paramPrintWriter, ArrayList<ProcessState> paramArrayList, boolean paramBoolean1, int[] paramArrayOfInt1, boolean paramBoolean2, int[] paramArrayOfInt2, boolean paramBoolean3, int[] paramArrayOfInt3, long paramLong)
  {
    paramPrintWriter.print("process");
    paramPrintWriter.print("\t");
    paramPrintWriter.print("uid");
    paramPrintWriter.print("\t");
    paramPrintWriter.print("vers");
    int[] arrayOfInt1 = null;
    Object localObject;
    if (paramBoolean1) {
      localObject = paramArrayOfInt1;
    } else {
      localObject = null;
    }
    int[] arrayOfInt2;
    if (paramBoolean2) {
      arrayOfInt2 = paramArrayOfInt2;
    } else {
      arrayOfInt2 = null;
    }
    if (paramBoolean3) {
      arrayOfInt1 = paramArrayOfInt3;
    }
    dumpStateHeadersCsv(paramPrintWriter, "\t", (int[])localObject, arrayOfInt2, arrayOfInt1);
    paramPrintWriter.println();
    for (int i = paramArrayList.size() - 1; i >= 0; i--)
    {
      localObject = (ProcessState)paramArrayList.get(i);
      paramPrintWriter.print(((ProcessState)localObject).getName());
      paramPrintWriter.print("\t");
      UserHandle.formatUid(paramPrintWriter, ((ProcessState)localObject).getUid());
      paramPrintWriter.print("\t");
      paramPrintWriter.print(((ProcessState)localObject).getVersion());
      ((ProcessState)localObject).dumpCsv(paramPrintWriter, paramBoolean1, paramArrayOfInt1, paramBoolean2, paramArrayOfInt2, paramBoolean3, paramArrayOfInt3, paramLong);
      paramPrintWriter.println();
    }
  }
  
  public static void dumpProcessSummaryLocked(PrintWriter paramPrintWriter, String paramString, ArrayList<ProcessState> paramArrayList, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, long paramLong1, long paramLong2)
  {
    for (int i = paramArrayList.size() - 1; i >= 0; i--) {
      ((ProcessState)paramArrayList.get(i)).dumpSummary(paramPrintWriter, paramString, paramArrayOfInt1, paramArrayOfInt2, paramArrayOfInt3, paramLong1, paramLong2);
    }
  }
  
  public static long dumpSingleTime(PrintWriter paramPrintWriter, String paramString, long[] paramArrayOfLong, int paramInt, long paramLong1, long paramLong2)
  {
    int i = -1;
    long l1 = 0L;
    for (int j = 0; j < 8; j += 4)
    {
      int k = -1;
      int m = 0;
      while (m < 4)
      {
        int n = m + j;
        long l2 = paramArrayOfLong[n];
        String str1 = "";
        long l3 = l2;
        String str2 = str1;
        if (paramInt == n)
        {
          l2 += paramLong2 - paramLong1;
          l3 = l2;
          str2 = str1;
          if (paramPrintWriter != null)
          {
            str2 = " (running)";
            l3 = l2;
          }
        }
        int i1 = i;
        n = k;
        l2 = l1;
        if (l3 != 0L)
        {
          i1 = i;
          n = k;
          if (paramPrintWriter != null)
          {
            paramPrintWriter.print(paramString);
            if (i != j) {
              i = j;
            } else {
              i = -1;
            }
            printScreenLabel(paramPrintWriter, i);
            i1 = j;
            if (k != m) {
              k = m;
            } else {
              k = -1;
            }
            printMemLabel(paramPrintWriter, k, '\000');
            n = m;
            paramPrintWriter.print(": ");
            TimeUtils.formatDuration(l3, paramPrintWriter);
            paramPrintWriter.println(str2);
          }
          l2 = l1 + l3;
        }
        m++;
        i = i1;
        k = n;
        l1 = l2;
      }
    }
    if ((l1 != 0L) && (paramPrintWriter != null))
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("    TOTAL: ");
      TimeUtils.formatDuration(l1, paramPrintWriter);
      paramPrintWriter.println();
    }
    return l1;
  }
  
  private static void dumpStateHeadersCsv(PrintWriter paramPrintWriter, String paramString, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3)
  {
    int i;
    if (paramArrayOfInt1 != null) {
      i = paramArrayOfInt1.length;
    } else {
      i = 1;
    }
    int j;
    if (paramArrayOfInt2 != null) {
      j = paramArrayOfInt2.length;
    } else {
      j = 1;
    }
    int k;
    if (paramArrayOfInt3 != null) {
      k = paramArrayOfInt3.length;
    } else {
      k = 1;
    }
    for (int m = 0; m < i; m++) {
      for (int n = 0; n < j; n++) {
        for (int i1 = 0; i1 < k; i1++)
        {
          paramPrintWriter.print(paramString);
          int i2 = 0;
          int i3 = i2;
          if (paramArrayOfInt1 != null)
          {
            i3 = i2;
            if (paramArrayOfInt1.length > 1)
            {
              printScreenLabelCsv(paramPrintWriter, paramArrayOfInt1[m]);
              i3 = 1;
            }
          }
          i2 = i3;
          if (paramArrayOfInt2 != null)
          {
            i2 = i3;
            if (paramArrayOfInt2.length > 1)
            {
              if (i3 != 0) {
                paramPrintWriter.print("-");
              }
              printMemLabelCsv(paramPrintWriter, paramArrayOfInt2[n]);
              i2 = 1;
            }
          }
          if ((paramArrayOfInt3 != null) && (paramArrayOfInt3.length > 1))
          {
            if (i2 != 0) {
              paramPrintWriter.print("-");
            }
            paramPrintWriter.print(STATE_NAMES_CSV[paramArrayOfInt3[i1]]);
          }
        }
      }
    }
  }
  
  public static void printAdjTag(PrintWriter paramPrintWriter, int paramInt)
  {
    paramInt = printArrayEntry(paramPrintWriter, ADJ_SCREEN_TAGS, paramInt, 4);
    printArrayEntry(paramPrintWriter, ADJ_MEM_TAGS, paramInt, 1);
  }
  
  public static void printAdjTagAndValue(PrintWriter paramPrintWriter, int paramInt, long paramLong)
  {
    paramPrintWriter.print(',');
    printAdjTag(paramPrintWriter, paramInt);
    paramPrintWriter.print(':');
    paramPrintWriter.print(paramLong);
  }
  
  public static int printArrayEntry(PrintWriter paramPrintWriter, String[] paramArrayOfString, int paramInt1, int paramInt2)
  {
    int i = paramInt1 / paramInt2;
    if ((i >= 0) && (i < paramArrayOfString.length)) {
      paramPrintWriter.print(paramArrayOfString[i]);
    } else {
      paramPrintWriter.print('?');
    }
    return paramInt1 - i * paramInt2;
  }
  
  public static void printMemLabel(PrintWriter paramPrintWriter, int paramInt, char paramChar)
  {
    switch (paramInt)
    {
    default: 
      paramPrintWriter.print("????");
      if (paramChar != 0) {
        paramPrintWriter.print(paramChar);
      }
      break;
    case 3: 
      paramPrintWriter.print("Crit");
      if (paramChar != 0) {
        paramPrintWriter.print(paramChar);
      }
      break;
    case 2: 
      paramPrintWriter.print("Low ");
      if (paramChar != 0) {
        paramPrintWriter.print(paramChar);
      }
      break;
    case 1: 
      paramPrintWriter.print("Mod ");
      if (paramChar != 0) {
        paramPrintWriter.print(paramChar);
      }
      break;
    case 0: 
      paramPrintWriter.print("Norm");
      if (paramChar != 0) {
        paramPrintWriter.print(paramChar);
      }
      break;
    case -1: 
      paramPrintWriter.print("    ");
      if (paramChar != 0) {
        paramPrintWriter.print(' ');
      }
      break;
    }
  }
  
  public static void printMemLabelCsv(PrintWriter paramPrintWriter, int paramInt)
  {
    if (paramInt >= 0) {
      if (paramInt <= 3) {
        paramPrintWriter.print(ADJ_MEM_NAMES_CSV[paramInt]);
      } else {
        paramPrintWriter.print("???");
      }
    }
  }
  
  public static void printPercent(PrintWriter paramPrintWriter, double paramDouble)
  {
    paramDouble *= 100.0D;
    if (paramDouble < 1.0D) {
      paramPrintWriter.print(String.format("%.2f", new Object[] { Double.valueOf(paramDouble) }));
    } else if (paramDouble < 10.0D) {
      paramPrintWriter.print(String.format("%.1f", new Object[] { Double.valueOf(paramDouble) }));
    } else {
      paramPrintWriter.print(String.format("%.0f", new Object[] { Double.valueOf(paramDouble) }));
    }
    paramPrintWriter.print("%");
  }
  
  public static void printProcStateTag(PrintWriter paramPrintWriter, int paramInt)
  {
    paramInt = printArrayEntry(paramPrintWriter, ADJ_SCREEN_TAGS, paramInt, 56);
    paramInt = printArrayEntry(paramPrintWriter, ADJ_MEM_TAGS, paramInt, 14);
    printArrayEntry(paramPrintWriter, STATE_TAGS, paramInt, 1);
  }
  
  public static void printProcStateTagAndValue(PrintWriter paramPrintWriter, int paramInt, long paramLong)
  {
    paramPrintWriter.print(',');
    printProcStateTag(paramPrintWriter, paramInt);
    paramPrintWriter.print(':');
    paramPrintWriter.print(paramLong);
  }
  
  public static void printProcStateTagProto(ProtoOutputStream paramProtoOutputStream, long paramLong1, long paramLong2, long paramLong3, int paramInt)
  {
    paramInt = printProto(paramProtoOutputStream, paramLong1, ADJ_SCREEN_PROTO_ENUMS, paramInt, 56);
    paramInt = printProto(paramProtoOutputStream, paramLong2, ADJ_MEM_PROTO_ENUMS, paramInt, 14);
    printProto(paramProtoOutputStream, paramLong3, STATE_PROTO_ENUMS, paramInt, 1);
  }
  
  public static int printProto(ProtoOutputStream paramProtoOutputStream, long paramLong, int[] paramArrayOfInt, int paramInt1, int paramInt2)
  {
    int i = paramInt1 / paramInt2;
    if ((i >= 0) && (i < paramArrayOfInt.length)) {
      paramProtoOutputStream.write(paramLong, paramArrayOfInt[i]);
    }
    return paramInt1 - i * paramInt2;
  }
  
  public static void printScreenLabel(PrintWriter paramPrintWriter, int paramInt)
  {
    if (paramInt != 4) {
      switch (paramInt)
      {
      default: 
        paramPrintWriter.print("????/");
        break;
      case 0: 
        paramPrintWriter.print("SOff/");
        break;
      case -1: 
        paramPrintWriter.print("     ");
        break;
      }
    } else {
      paramPrintWriter.print("SOn /");
    }
  }
  
  public static void printScreenLabelCsv(PrintWriter paramPrintWriter, int paramInt)
  {
    if (paramInt != 4) {
      switch (paramInt)
      {
      default: 
        paramPrintWriter.print("???");
        break;
      case 0: 
        paramPrintWriter.print(ADJ_SCREEN_NAMES_CSV[0]);
        break;
      case -1: 
        break;
      }
    } else {
      paramPrintWriter.print(ADJ_SCREEN_NAMES_CSV[1]);
    }
  }
}
