package android.hardware.vibrator.V1_0;

import java.util.ArrayList;

public final class EffectStrength
{
  public static final byte LIGHT = 0;
  public static final byte MEDIUM = 1;
  public static final byte STRONG = 2;
  
  public EffectStrength() {}
  
  public static final String dumpBitfield(byte paramByte)
  {
    ArrayList localArrayList = new ArrayList();
    byte b1 = 0;
    localArrayList.add("LIGHT");
    if ((paramByte & 0x1) == 1)
    {
      localArrayList.add("MEDIUM");
      b1 = (byte)(0x0 | 0x1);
    }
    byte b2 = b1;
    if ((paramByte & 0x2) == 2)
    {
      localArrayList.add("STRONG");
      b2 = (byte)(b1 | 0x2);
    }
    if (paramByte != b2)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("0x");
      localStringBuilder.append(Integer.toHexString(Byte.toUnsignedInt((byte)(b2 & paramByte))));
      localArrayList.add(localStringBuilder.toString());
    }
    return String.join(" | ", localArrayList);
  }
  
  public static final String toString(byte paramByte)
  {
    if (paramByte == 0) {
      return "LIGHT";
    }
    if (paramByte == 1) {
      return "MEDIUM";
    }
    if (paramByte == 2) {
      return "STRONG";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(Byte.toUnsignedInt(paramByte)));
    return localStringBuilder.toString();
  }
}
