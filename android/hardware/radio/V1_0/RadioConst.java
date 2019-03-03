package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class RadioConst
{
  public static final int CARD_MAX_APPS = 8;
  public static final int CDMA_ALPHA_INFO_BUFFER_LENGTH = 64;
  public static final int CDMA_MAX_NUMBER_OF_INFO_RECS = 10;
  public static final int CDMA_NUMBER_INFO_BUFFER_LENGTH = 81;
  public static final int MAX_CLIENT_ID_LENGTH = 2;
  public static final int MAX_DEBUG_SOCKET_NAME_LENGTH = 12;
  public static final int MAX_QEMU_PIPE_NAME_LENGTH = 11;
  public static final int MAX_RILDS = 3;
  public static final int MAX_SOCKET_NAME_LENGTH = 6;
  public static final int MAX_UUID_LENGTH = 64;
  public static final int NUM_SERVICE_CLASSES = 7;
  public static final int NUM_TX_POWER_LEVELS = 5;
  public static final int SS_INFO_MAX = 4;
  
  public RadioConst() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    if ((paramInt & 0x40) == 64)
    {
      localArrayList.add("CDMA_ALPHA_INFO_BUFFER_LENGTH");
      i = 0x0 | 0x40;
    }
    int j = i;
    if ((paramInt & 0x51) == 81)
    {
      localArrayList.add("CDMA_NUMBER_INFO_BUFFER_LENGTH");
      j = i | 0x51;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("MAX_RILDS");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x6) == 6)
    {
      localArrayList.add("MAX_SOCKET_NAME_LENGTH");
      j = i | 0x6;
    }
    i = j;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("MAX_CLIENT_ID_LENGTH");
      i = j | 0x2;
    }
    j = i;
    if ((paramInt & 0xC) == 12)
    {
      localArrayList.add("MAX_DEBUG_SOCKET_NAME_LENGTH");
      j = i | 0xC;
    }
    i = j;
    if ((paramInt & 0xB) == 11)
    {
      localArrayList.add("MAX_QEMU_PIPE_NAME_LENGTH");
      i = j | 0xB;
    }
    int k = i;
    if ((paramInt & 0x40) == 64)
    {
      localArrayList.add("MAX_UUID_LENGTH");
      k = i | 0x40;
    }
    j = k;
    if ((paramInt & 0x8) == 8)
    {
      localArrayList.add("CARD_MAX_APPS");
      j = k | 0x8;
    }
    i = j;
    if ((paramInt & 0xA) == 10)
    {
      localArrayList.add("CDMA_MAX_NUMBER_OF_INFO_RECS");
      i = j | 0xA;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("SS_INFO_MAX");
      j = i | 0x4;
    }
    i = j;
    if ((paramInt & 0x7) == 7)
    {
      localArrayList.add("NUM_SERVICE_CLASSES");
      i = j | 0x7;
    }
    j = i;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("NUM_TX_POWER_LEVELS");
      j = i | 0x5;
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
    if (paramInt == 64) {
      return "CDMA_ALPHA_INFO_BUFFER_LENGTH";
    }
    if (paramInt == 81) {
      return "CDMA_NUMBER_INFO_BUFFER_LENGTH";
    }
    if (paramInt == 3) {
      return "MAX_RILDS";
    }
    if (paramInt == 6) {
      return "MAX_SOCKET_NAME_LENGTH";
    }
    if (paramInt == 2) {
      return "MAX_CLIENT_ID_LENGTH";
    }
    if (paramInt == 12) {
      return "MAX_DEBUG_SOCKET_NAME_LENGTH";
    }
    if (paramInt == 11) {
      return "MAX_QEMU_PIPE_NAME_LENGTH";
    }
    if (paramInt == 64) {
      return "MAX_UUID_LENGTH";
    }
    if (paramInt == 8) {
      return "CARD_MAX_APPS";
    }
    if (paramInt == 10) {
      return "CDMA_MAX_NUMBER_OF_INFO_RECS";
    }
    if (paramInt == 4) {
      return "SS_INFO_MAX";
    }
    if (paramInt == 7) {
      return "NUM_SERVICE_CLASSES";
    }
    if (paramInt == 5) {
      return "NUM_TX_POWER_LEVELS";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
