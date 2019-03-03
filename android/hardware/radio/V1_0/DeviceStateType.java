package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class DeviceStateType
{
  public static final int CHARGING_STATE = 1;
  public static final int LOW_DATA_EXPECTED = 2;
  public static final int POWER_SAVE_MODE = 0;
  
  public DeviceStateType() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("POWER_SAVE_MODE");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("CHARGING_STATE");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("LOW_DATA_EXPECTED");
      j = i | 0x2;
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
      return "POWER_SAVE_MODE";
    }
    if (paramInt == 1) {
      return "CHARGING_STATE";
    }
    if (paramInt == 2) {
      return "LOW_DATA_EXPECTED";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
