package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class UusType
{
  public static final int TYPE1_IMPLICIT = 0;
  public static final int TYPE1_NOT_REQUIRED = 2;
  public static final int TYPE1_REQUIRED = 1;
  public static final int TYPE2_NOT_REQUIRED = 4;
  public static final int TYPE2_REQUIRED = 3;
  public static final int TYPE3_NOT_REQUIRED = 6;
  public static final int TYPE3_REQUIRED = 5;
  
  public UusType() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("TYPE1_IMPLICIT");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("TYPE1_REQUIRED");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("TYPE1_NOT_REQUIRED");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("TYPE2_REQUIRED");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("TYPE2_NOT_REQUIRED");
      j = i | 0x4;
    }
    i = j;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("TYPE3_REQUIRED");
      i = j | 0x5;
    }
    j = i;
    if ((paramInt & 0x6) == 6)
    {
      localArrayList.add("TYPE3_NOT_REQUIRED");
      j = i | 0x6;
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
      return "TYPE1_IMPLICIT";
    }
    if (paramInt == 1) {
      return "TYPE1_REQUIRED";
    }
    if (paramInt == 2) {
      return "TYPE1_NOT_REQUIRED";
    }
    if (paramInt == 3) {
      return "TYPE2_REQUIRED";
    }
    if (paramInt == 4) {
      return "TYPE2_NOT_REQUIRED";
    }
    if (paramInt == 5) {
      return "TYPE3_REQUIRED";
    }
    if (paramInt == 6) {
      return "TYPE3_NOT_REQUIRED";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
