package android.telephony;

public class AccessNetworkUtils
{
  public static final int INVALID_BAND = -1;
  
  private AccessNetworkUtils() {}
  
  public static int getDuplexModeForEutranBand(int paramInt)
  {
    if (paramInt == -1) {
      return 0;
    }
    if (paramInt >= 68) {
      return 0;
    }
    if (paramInt >= 65) {
      return 1;
    }
    if (paramInt >= 47) {
      return 0;
    }
    if (paramInt >= 33) {
      return 2;
    }
    if (paramInt >= 1) {
      return 1;
    }
    return 0;
  }
  
  public static int getOperatingBandForEarfcn(int paramInt)
  {
    if (paramInt > 67535) {
      return -1;
    }
    if (paramInt >= 67366) {
      return -1;
    }
    if (paramInt >= 66436) {
      return 66;
    }
    if (paramInt >= 65536) {
      return 65;
    }
    if (paramInt > 54339) {
      return -1;
    }
    if (paramInt >= 46790) {
      return 46;
    }
    if (paramInt >= 46590) {
      return 45;
    }
    if (paramInt >= 45590) {
      return 44;
    }
    if (paramInt >= 43590) {
      return 43;
    }
    if (paramInt >= 41590) {
      return 42;
    }
    if (paramInt >= 39650) {
      return 41;
    }
    if (paramInt >= 38650) {
      return 40;
    }
    if (paramInt >= 38250) {
      return 39;
    }
    if (paramInt >= 37750) {
      return 38;
    }
    if (paramInt >= 37550) {
      return 37;
    }
    if (paramInt >= 36950) {
      return 36;
    }
    if (paramInt >= 36350) {
      return 35;
    }
    if (paramInt >= 36200) {
      return 34;
    }
    if (paramInt >= 36000) {
      return 33;
    }
    if (paramInt > 10359) {
      return -1;
    }
    if (paramInt >= 9920) {
      return -1;
    }
    if (paramInt >= 9870) {
      return 31;
    }
    if (paramInt >= 9770) {
      return 30;
    }
    if (paramInt >= 9660) {
      return -1;
    }
    if (paramInt >= 9210) {
      return 28;
    }
    if (paramInt >= 9040) {
      return 27;
    }
    if (paramInt >= 8690) {
      return 26;
    }
    if (paramInt >= 8040) {
      return 25;
    }
    if (paramInt >= 7700) {
      return 24;
    }
    if (paramInt >= 7500) {
      return 23;
    }
    if (paramInt >= 6600) {
      return 22;
    }
    if (paramInt >= 6450) {
      return 21;
    }
    if (paramInt >= 6150) {
      return 20;
    }
    if (paramInt >= 6000) {
      return 19;
    }
    if (paramInt >= 5850) {
      return 18;
    }
    if (paramInt >= 5730) {
      return 17;
    }
    if (paramInt > 5379) {
      return -1;
    }
    if (paramInt >= 5280) {
      return 14;
    }
    if (paramInt >= 5180) {
      return 13;
    }
    if (paramInt >= 5010) {
      return 12;
    }
    if (paramInt >= 4750) {
      return 11;
    }
    if (paramInt >= 4150) {
      return 10;
    }
    if (paramInt >= 3800) {
      return 9;
    }
    if (paramInt >= 3450) {
      return 8;
    }
    if (paramInt >= 2750) {
      return 7;
    }
    if (paramInt >= 2650) {
      return 6;
    }
    if (paramInt >= 2400) {
      return 5;
    }
    if (paramInt >= 1950) {
      return 4;
    }
    if (paramInt >= 1200) {
      return 3;
    }
    if (paramInt >= 600) {
      return 2;
    }
    if (paramInt >= 0) {
      return 1;
    }
    return -1;
  }
}
