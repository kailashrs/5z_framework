package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class PersoSubstate
{
  public static final int IN_PROGRESS = 1;
  public static final int READY = 2;
  public static final int RUIM_CORPORATE = 16;
  public static final int RUIM_CORPORATE_PUK = 22;
  public static final int RUIM_HRPD = 15;
  public static final int RUIM_HRPD_PUK = 21;
  public static final int RUIM_NETWORK1 = 13;
  public static final int RUIM_NETWORK1_PUK = 19;
  public static final int RUIM_NETWORK2 = 14;
  public static final int RUIM_NETWORK2_PUK = 20;
  public static final int RUIM_RUIM = 18;
  public static final int RUIM_RUIM_PUK = 24;
  public static final int RUIM_SERVICE_PROVIDER = 17;
  public static final int RUIM_SERVICE_PROVIDER_PUK = 23;
  public static final int SIM_CORPORATE = 5;
  public static final int SIM_CORPORATE_PUK = 10;
  public static final int SIM_NETWORK = 3;
  public static final int SIM_NETWORK_PUK = 8;
  public static final int SIM_NETWORK_SUBSET = 4;
  public static final int SIM_NETWORK_SUBSET_PUK = 9;
  public static final int SIM_SERVICE_PROVIDER = 6;
  public static final int SIM_SERVICE_PROVIDER_PUK = 11;
  public static final int SIM_SIM = 7;
  public static final int SIM_SIM_PUK = 12;
  public static final int UNKNOWN = 0;
  
  public PersoSubstate() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("UNKNOWN");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("IN_PROGRESS");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("READY");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("SIM_NETWORK");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("SIM_NETWORK_SUBSET");
      j = i | 0x4;
    }
    i = j;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("SIM_CORPORATE");
      i = j | 0x5;
    }
    j = i;
    if ((paramInt & 0x6) == 6)
    {
      localArrayList.add("SIM_SERVICE_PROVIDER");
      j = i | 0x6;
    }
    i = j;
    if ((paramInt & 0x7) == 7)
    {
      localArrayList.add("SIM_SIM");
      i = j | 0x7;
    }
    int k = i;
    if ((paramInt & 0x8) == 8)
    {
      localArrayList.add("SIM_NETWORK_PUK");
      k = i | 0x8;
    }
    j = k;
    if ((paramInt & 0x9) == 9)
    {
      localArrayList.add("SIM_NETWORK_SUBSET_PUK");
      j = k | 0x9;
    }
    i = j;
    if ((paramInt & 0xA) == 10)
    {
      localArrayList.add("SIM_CORPORATE_PUK");
      i = j | 0xA;
    }
    j = i;
    if ((paramInt & 0xB) == 11)
    {
      localArrayList.add("SIM_SERVICE_PROVIDER_PUK");
      j = i | 0xB;
    }
    i = j;
    if ((paramInt & 0xC) == 12)
    {
      localArrayList.add("SIM_SIM_PUK");
      i = j | 0xC;
    }
    j = i;
    if ((paramInt & 0xD) == 13)
    {
      localArrayList.add("RUIM_NETWORK1");
      j = i | 0xD;
    }
    k = j;
    if ((paramInt & 0xE) == 14)
    {
      localArrayList.add("RUIM_NETWORK2");
      k = j | 0xE;
    }
    i = k;
    if ((paramInt & 0xF) == 15)
    {
      localArrayList.add("RUIM_HRPD");
      i = k | 0xF;
    }
    j = i;
    if ((paramInt & 0x10) == 16)
    {
      localArrayList.add("RUIM_CORPORATE");
      j = i | 0x10;
    }
    i = j;
    if ((paramInt & 0x11) == 17)
    {
      localArrayList.add("RUIM_SERVICE_PROVIDER");
      i = j | 0x11;
    }
    j = i;
    if ((paramInt & 0x12) == 18)
    {
      localArrayList.add("RUIM_RUIM");
      j = i | 0x12;
    }
    k = j;
    if ((paramInt & 0x13) == 19)
    {
      localArrayList.add("RUIM_NETWORK1_PUK");
      k = j | 0x13;
    }
    i = k;
    if ((paramInt & 0x14) == 20)
    {
      localArrayList.add("RUIM_NETWORK2_PUK");
      i = k | 0x14;
    }
    j = i;
    if ((paramInt & 0x15) == 21)
    {
      localArrayList.add("RUIM_HRPD_PUK");
      j = i | 0x15;
    }
    i = j;
    if ((paramInt & 0x16) == 22)
    {
      localArrayList.add("RUIM_CORPORATE_PUK");
      i = j | 0x16;
    }
    j = i;
    if ((paramInt & 0x17) == 23)
    {
      localArrayList.add("RUIM_SERVICE_PROVIDER_PUK");
      j = i | 0x17;
    }
    i = j;
    if ((paramInt & 0x18) == 24)
    {
      localArrayList.add("RUIM_RUIM_PUK");
      i = j | 0x18;
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
      return "UNKNOWN";
    }
    if (paramInt == 1) {
      return "IN_PROGRESS";
    }
    if (paramInt == 2) {
      return "READY";
    }
    if (paramInt == 3) {
      return "SIM_NETWORK";
    }
    if (paramInt == 4) {
      return "SIM_NETWORK_SUBSET";
    }
    if (paramInt == 5) {
      return "SIM_CORPORATE";
    }
    if (paramInt == 6) {
      return "SIM_SERVICE_PROVIDER";
    }
    if (paramInt == 7) {
      return "SIM_SIM";
    }
    if (paramInt == 8) {
      return "SIM_NETWORK_PUK";
    }
    if (paramInt == 9) {
      return "SIM_NETWORK_SUBSET_PUK";
    }
    if (paramInt == 10) {
      return "SIM_CORPORATE_PUK";
    }
    if (paramInt == 11) {
      return "SIM_SERVICE_PROVIDER_PUK";
    }
    if (paramInt == 12) {
      return "SIM_SIM_PUK";
    }
    if (paramInt == 13) {
      return "RUIM_NETWORK1";
    }
    if (paramInt == 14) {
      return "RUIM_NETWORK2";
    }
    if (paramInt == 15) {
      return "RUIM_HRPD";
    }
    if (paramInt == 16) {
      return "RUIM_CORPORATE";
    }
    if (paramInt == 17) {
      return "RUIM_SERVICE_PROVIDER";
    }
    if (paramInt == 18) {
      return "RUIM_RUIM";
    }
    if (paramInt == 19) {
      return "RUIM_NETWORK1_PUK";
    }
    if (paramInt == 20) {
      return "RUIM_NETWORK2_PUK";
    }
    if (paramInt == 21) {
      return "RUIM_HRPD_PUK";
    }
    if (paramInt == 22) {
      return "RUIM_CORPORATE_PUK";
    }
    if (paramInt == 23) {
      return "RUIM_SERVICE_PROVIDER_PUK";
    }
    if (paramInt == 24) {
      return "RUIM_RUIM_PUK";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
