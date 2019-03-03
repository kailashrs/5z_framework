package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class SmsWriteArgsStatus
{
  public static final int REC_READ = 1;
  public static final int REC_UNREAD = 0;
  public static final int STO_SENT = 3;
  public static final int STO_UNSENT = 2;
  
  public SmsWriteArgsStatus() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("REC_UNREAD");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("REC_READ");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("STO_UNSENT");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("STO_SENT");
      i = j | 0x3;
    }
    if (paramInt != i)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("0x");
      localStringBuilder.append(Integer.toHexString(i & paramInt));
      localArrayList.add(localStringBuilder.toString());
    }
    return String.join(" | ", localArrayList);
  }
  
  public static final String toString(int paramInt)
  {
    if (paramInt == 0) {
      return "REC_UNREAD";
    }
    if (paramInt == 1) {
      return "REC_READ";
    }
    if (paramInt == 2) {
      return "STO_UNSENT";
    }
    if (paramInt == 3) {
      return "STO_SENT";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
