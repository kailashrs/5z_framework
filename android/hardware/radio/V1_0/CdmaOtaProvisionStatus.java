package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class CdmaOtaProvisionStatus
{
  public static final int A_KEY_EXCHANGED = 2;
  public static final int COMMITTED = 8;
  public static final int IMSI_DOWNLOADED = 6;
  public static final int MDN_DOWNLOADED = 5;
  public static final int NAM_DOWNLOADED = 4;
  public static final int OTAPA_ABORTED = 11;
  public static final int OTAPA_STARTED = 9;
  public static final int OTAPA_STOPPED = 10;
  public static final int PRL_DOWNLOADED = 7;
  public static final int SPC_RETRIES_EXCEEDED = 1;
  public static final int SPL_UNLOCKED = 0;
  public static final int SSD_UPDATED = 3;
  
  public CdmaOtaProvisionStatus() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("SPL_UNLOCKED");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("SPC_RETRIES_EXCEEDED");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("A_KEY_EXCHANGED");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("SSD_UPDATED");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("NAM_DOWNLOADED");
      j = i | 0x4;
    }
    i = j;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("MDN_DOWNLOADED");
      i = j | 0x5;
    }
    j = i;
    if ((paramInt & 0x6) == 6)
    {
      localArrayList.add("IMSI_DOWNLOADED");
      j = i | 0x6;
    }
    i = j;
    if ((paramInt & 0x7) == 7)
    {
      localArrayList.add("PRL_DOWNLOADED");
      i = j | 0x7;
    }
    j = i;
    if ((paramInt & 0x8) == 8)
    {
      localArrayList.add("COMMITTED");
      j = i | 0x8;
    }
    i = j;
    if ((paramInt & 0x9) == 9)
    {
      localArrayList.add("OTAPA_STARTED");
      i = j | 0x9;
    }
    j = i;
    if ((paramInt & 0xA) == 10)
    {
      localArrayList.add("OTAPA_STOPPED");
      j = i | 0xA;
    }
    i = j;
    if ((paramInt & 0xB) == 11)
    {
      localArrayList.add("OTAPA_ABORTED");
      i = j | 0xB;
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
      return "SPL_UNLOCKED";
    }
    if (paramInt == 1) {
      return "SPC_RETRIES_EXCEEDED";
    }
    if (paramInt == 2) {
      return "A_KEY_EXCHANGED";
    }
    if (paramInt == 3) {
      return "SSD_UPDATED";
    }
    if (paramInt == 4) {
      return "NAM_DOWNLOADED";
    }
    if (paramInt == 5) {
      return "MDN_DOWNLOADED";
    }
    if (paramInt == 6) {
      return "IMSI_DOWNLOADED";
    }
    if (paramInt == 7) {
      return "PRL_DOWNLOADED";
    }
    if (paramInt == 8) {
      return "COMMITTED";
    }
    if (paramInt == 9) {
      return "OTAPA_STARTED";
    }
    if (paramInt == 10) {
      return "OTAPA_STOPPED";
    }
    if (paramInt == 11) {
      return "OTAPA_ABORTED";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
