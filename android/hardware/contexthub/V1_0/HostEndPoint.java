package android.hardware.contexthub.V1_0;

import java.util.ArrayList;

public final class HostEndPoint
{
  public static final short BROADCAST = -1;
  public static final short UNSPECIFIED = -2;
  
  public HostEndPoint() {}
  
  public static final String dumpBitfield(short paramShort)
  {
    ArrayList localArrayList = new ArrayList();
    short s1 = 0;
    if ((paramShort & 0xFFFFFFFF) == -1)
    {
      localArrayList.add("BROADCAST");
      s1 = (short)(0x0 | 0xFFFFFFFF);
    }
    short s2 = s1;
    if ((paramShort & 0xFFFFFFFE) == -2)
    {
      localArrayList.add("UNSPECIFIED");
      s2 = (short)(s1 | 0xFFFFFFFE);
    }
    if (paramShort != s2)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("0x");
      localStringBuilder.append(Integer.toHexString(Short.toUnsignedInt((short)(s2 & paramShort))));
      localArrayList.add(localStringBuilder.toString());
    }
    return String.join(" | ", localArrayList);
  }
  
  public static final String toString(short paramShort)
  {
    if (paramShort == -1) {
      return "BROADCAST";
    }
    if (paramShort == -2) {
      return "UNSPECIFIED";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(Short.toUnsignedInt(paramShort)));
    return localStringBuilder.toString();
  }
}
