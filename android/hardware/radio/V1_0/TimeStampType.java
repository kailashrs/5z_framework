package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class TimeStampType
{
  public static final int ANTENNA = 1;
  public static final int JAVA_RIL = 4;
  public static final int MODEM = 2;
  public static final int OEM_RIL = 3;
  public static final int UNKNOWN = 0;
  
  public TimeStampType() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("UNKNOWN");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("ANTENNA");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("MODEM");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("OEM_RIL");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("JAVA_RIL");
      j = i | 0x4;
    }
    if (paramInt != j)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("0x");
      localStringBuilder.append(Integer.toHexString(j & paramInt));
      localArrayList.add(localStringBuilder.toString());
    }
    return String.join(" | ", localArrayList);
  }
  
  public static final String toString(int paramInt)
  {
    if (paramInt == 0) {
      return "UNKNOWN";
    }
    if (paramInt == 1) {
      return "ANTENNA";
    }
    if (paramInt == 2) {
      return "MODEM";
    }
    if (paramInt == 3) {
      return "OEM_RIL";
    }
    if (paramInt == 4) {
      return "JAVA_RIL";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
