package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class RadioError
{
  public static final int ABORTED = 65;
  public static final int CANCELLED = 7;
  public static final int DEVICE_IN_USE = 64;
  public static final int DIAL_MODIFIED_TO_DIAL = 20;
  public static final int DIAL_MODIFIED_TO_SS = 19;
  public static final int DIAL_MODIFIED_TO_USSD = 18;
  public static final int EMPTY_RECORD = 55;
  public static final int ENCODING_ERR = 57;
  public static final int FDN_CHECK_FAILURE = 14;
  public static final int GENERIC_FAILURE = 2;
  public static final int ILLEGAL_SIM_OR_ME = 15;
  public static final int INTERNAL_ERR = 38;
  public static final int INVALID_ARGUMENTS = 44;
  public static final int INVALID_CALL_ID = 47;
  public static final int INVALID_MODEM_STATE = 46;
  public static final int INVALID_RESPONSE = 66;
  public static final int INVALID_SIM_STATE = 45;
  public static final int INVALID_SMSC_ADDRESS = 58;
  public static final int INVALID_SMS_FORMAT = 56;
  public static final int INVALID_STATE = 41;
  public static final int LCE_NOT_SUPPORTED = 36;
  public static final int MISSING_RESOURCE = 16;
  public static final int MODEM_ERR = 40;
  public static final int MODE_NOT_SUPPORTED = 13;
  public static final int NETWORK_ERR = 49;
  public static final int NETWORK_NOT_READY = 60;
  public static final int NETWORK_REJECT = 53;
  public static final int NONE = 0;
  public static final int NOT_PROVISIONED = 61;
  public static final int NO_MEMORY = 37;
  public static final int NO_NETWORK_FOUND = 63;
  public static final int NO_RESOURCES = 42;
  public static final int NO_SMS_TO_ACK = 48;
  public static final int NO_SUBSCRIPTION = 62;
  public static final int NO_SUCH_ELEMENT = 17;
  public static final int NO_SUCH_ENTRY = 59;
  public static final int OEM_ERROR_1 = 501;
  public static final int OEM_ERROR_10 = 510;
  public static final int OEM_ERROR_11 = 511;
  public static final int OEM_ERROR_12 = 512;
  public static final int OEM_ERROR_13 = 513;
  public static final int OEM_ERROR_14 = 514;
  public static final int OEM_ERROR_15 = 515;
  public static final int OEM_ERROR_16 = 516;
  public static final int OEM_ERROR_17 = 517;
  public static final int OEM_ERROR_18 = 518;
  public static final int OEM_ERROR_19 = 519;
  public static final int OEM_ERROR_2 = 502;
  public static final int OEM_ERROR_20 = 520;
  public static final int OEM_ERROR_21 = 521;
  public static final int OEM_ERROR_22 = 522;
  public static final int OEM_ERROR_23 = 523;
  public static final int OEM_ERROR_24 = 524;
  public static final int OEM_ERROR_25 = 525;
  public static final int OEM_ERROR_3 = 503;
  public static final int OEM_ERROR_4 = 504;
  public static final int OEM_ERROR_5 = 505;
  public static final int OEM_ERROR_6 = 506;
  public static final int OEM_ERROR_7 = 507;
  public static final int OEM_ERROR_8 = 508;
  public static final int OEM_ERROR_9 = 509;
  public static final int OPERATION_NOT_ALLOWED = 54;
  public static final int OP_NOT_ALLOWED_BEFORE_REG_TO_NW = 9;
  public static final int OP_NOT_ALLOWED_DURING_VOICE_CALL = 8;
  public static final int PASSWORD_INCORRECT = 3;
  public static final int RADIO_NOT_AVAILABLE = 1;
  public static final int REQUEST_NOT_SUPPORTED = 6;
  public static final int REQUEST_RATE_LIMITED = 50;
  public static final int SIM_ABSENT = 11;
  public static final int SIM_BUSY = 51;
  public static final int SIM_ERR = 43;
  public static final int SIM_FULL = 52;
  public static final int SIM_PIN2 = 4;
  public static final int SIM_PUK2 = 5;
  public static final int SMS_SEND_FAIL_RETRY = 10;
  public static final int SS_MODIFIED_TO_DIAL = 24;
  public static final int SS_MODIFIED_TO_SS = 27;
  public static final int SS_MODIFIED_TO_USSD = 25;
  public static final int SUBSCRIPTION_NOT_AVAILABLE = 12;
  public static final int SUBSCRIPTION_NOT_SUPPORTED = 26;
  public static final int SYSTEM_ERR = 39;
  public static final int USSD_MODIFIED_TO_DIAL = 21;
  public static final int USSD_MODIFIED_TO_SS = 22;
  public static final int USSD_MODIFIED_TO_USSD = 23;
  
  public RadioError() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("NONE");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("RADIO_NOT_AVAILABLE");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("GENERIC_FAILURE");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("PASSWORD_INCORRECT");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("SIM_PIN2");
      j = i | 0x4;
    }
    i = j;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("SIM_PUK2");
      i = j | 0x5;
    }
    j = i;
    if ((paramInt & 0x6) == 6)
    {
      localArrayList.add("REQUEST_NOT_SUPPORTED");
      j = i | 0x6;
    }
    int k = j;
    if ((paramInt & 0x7) == 7)
    {
      localArrayList.add("CANCELLED");
      k = j | 0x7;
    }
    i = k;
    if ((paramInt & 0x8) == 8)
    {
      localArrayList.add("OP_NOT_ALLOWED_DURING_VOICE_CALL");
      i = k | 0x8;
    }
    j = i;
    if ((paramInt & 0x9) == 9)
    {
      localArrayList.add("OP_NOT_ALLOWED_BEFORE_REG_TO_NW");
      j = i | 0x9;
    }
    i = j;
    if ((paramInt & 0xA) == 10)
    {
      localArrayList.add("SMS_SEND_FAIL_RETRY");
      i = j | 0xA;
    }
    j = i;
    if ((paramInt & 0xB) == 11)
    {
      localArrayList.add("SIM_ABSENT");
      j = i | 0xB;
    }
    i = j;
    if ((paramInt & 0xC) == 12)
    {
      localArrayList.add("SUBSCRIPTION_NOT_AVAILABLE");
      i = j | 0xC;
    }
    j = i;
    if ((paramInt & 0xD) == 13)
    {
      localArrayList.add("MODE_NOT_SUPPORTED");
      j = i | 0xD;
    }
    i = j;
    if ((paramInt & 0xE) == 14)
    {
      localArrayList.add("FDN_CHECK_FAILURE");
      i = j | 0xE;
    }
    j = i;
    if ((paramInt & 0xF) == 15)
    {
      localArrayList.add("ILLEGAL_SIM_OR_ME");
      j = i | 0xF;
    }
    i = j;
    if ((paramInt & 0x10) == 16)
    {
      localArrayList.add("MISSING_RESOURCE");
      i = j | 0x10;
    }
    j = i;
    if ((paramInt & 0x11) == 17)
    {
      localArrayList.add("NO_SUCH_ELEMENT");
      j = i | 0x11;
    }
    i = j;
    if ((paramInt & 0x12) == 18)
    {
      localArrayList.add("DIAL_MODIFIED_TO_USSD");
      i = j | 0x12;
    }
    j = i;
    if ((paramInt & 0x13) == 19)
    {
      localArrayList.add("DIAL_MODIFIED_TO_SS");
      j = i | 0x13;
    }
    i = j;
    if ((paramInt & 0x14) == 20)
    {
      localArrayList.add("DIAL_MODIFIED_TO_DIAL");
      i = j | 0x14;
    }
    j = i;
    if ((paramInt & 0x15) == 21)
    {
      localArrayList.add("USSD_MODIFIED_TO_DIAL");
      j = i | 0x15;
    }
    k = j;
    if ((paramInt & 0x16) == 22)
    {
      localArrayList.add("USSD_MODIFIED_TO_SS");
      k = j | 0x16;
    }
    i = k;
    if ((paramInt & 0x17) == 23)
    {
      localArrayList.add("USSD_MODIFIED_TO_USSD");
      i = k | 0x17;
    }
    j = i;
    if ((paramInt & 0x18) == 24)
    {
      localArrayList.add("SS_MODIFIED_TO_DIAL");
      j = i | 0x18;
    }
    k = j;
    if ((paramInt & 0x19) == 25)
    {
      localArrayList.add("SS_MODIFIED_TO_USSD");
      k = j | 0x19;
    }
    i = k;
    if ((paramInt & 0x1A) == 26)
    {
      localArrayList.add("SUBSCRIPTION_NOT_SUPPORTED");
      i = k | 0x1A;
    }
    j = i;
    if ((paramInt & 0x1B) == 27)
    {
      localArrayList.add("SS_MODIFIED_TO_SS");
      j = i | 0x1B;
    }
    i = j;
    if ((paramInt & 0x24) == 36)
    {
      localArrayList.add("LCE_NOT_SUPPORTED");
      i = j | 0x24;
    }
    j = i;
    if ((paramInt & 0x25) == 37)
    {
      localArrayList.add("NO_MEMORY");
      j = i | 0x25;
    }
    i = j;
    if ((paramInt & 0x26) == 38)
    {
      localArrayList.add("INTERNAL_ERR");
      i = j | 0x26;
    }
    k = i;
    if ((paramInt & 0x27) == 39)
    {
      localArrayList.add("SYSTEM_ERR");
      k = i | 0x27;
    }
    j = k;
    if ((paramInt & 0x28) == 40)
    {
      localArrayList.add("MODEM_ERR");
      j = k | 0x28;
    }
    i = j;
    if ((paramInt & 0x29) == 41)
    {
      localArrayList.add("INVALID_STATE");
      i = j | 0x29;
    }
    j = i;
    if ((paramInt & 0x2A) == 42)
    {
      localArrayList.add("NO_RESOURCES");
      j = i | 0x2A;
    }
    i = j;
    if ((paramInt & 0x2B) == 43)
    {
      localArrayList.add("SIM_ERR");
      i = j | 0x2B;
    }
    j = i;
    if ((paramInt & 0x2C) == 44)
    {
      localArrayList.add("INVALID_ARGUMENTS");
      j = i | 0x2C;
    }
    i = j;
    if ((paramInt & 0x2D) == 45)
    {
      localArrayList.add("INVALID_SIM_STATE");
      i = j | 0x2D;
    }
    j = i;
    if ((paramInt & 0x2E) == 46)
    {
      localArrayList.add("INVALID_MODEM_STATE");
      j = i | 0x2E;
    }
    k = j;
    if ((paramInt & 0x2F) == 47)
    {
      localArrayList.add("INVALID_CALL_ID");
      k = j | 0x2F;
    }
    i = k;
    if ((paramInt & 0x30) == 48)
    {
      localArrayList.add("NO_SMS_TO_ACK");
      i = k | 0x30;
    }
    j = i;
    if ((paramInt & 0x31) == 49)
    {
      localArrayList.add("NETWORK_ERR");
      j = i | 0x31;
    }
    i = j;
    if ((paramInt & 0x32) == 50)
    {
      localArrayList.add("REQUEST_RATE_LIMITED");
      i = j | 0x32;
    }
    j = i;
    if ((paramInt & 0x33) == 51)
    {
      localArrayList.add("SIM_BUSY");
      j = i | 0x33;
    }
    i = j;
    if ((paramInt & 0x34) == 52)
    {
      localArrayList.add("SIM_FULL");
      i = j | 0x34;
    }
    j = i;
    if ((paramInt & 0x35) == 53)
    {
      localArrayList.add("NETWORK_REJECT");
      j = i | 0x35;
    }
    i = j;
    if ((paramInt & 0x36) == 54)
    {
      localArrayList.add("OPERATION_NOT_ALLOWED");
      i = j | 0x36;
    }
    k = i;
    if ((paramInt & 0x37) == 55)
    {
      localArrayList.add("EMPTY_RECORD");
      k = i | 0x37;
    }
    j = k;
    if ((paramInt & 0x38) == 56)
    {
      localArrayList.add("INVALID_SMS_FORMAT");
      j = k | 0x38;
    }
    i = j;
    if ((paramInt & 0x39) == 57)
    {
      localArrayList.add("ENCODING_ERR");
      i = j | 0x39;
    }
    j = i;
    if ((paramInt & 0x3A) == 58)
    {
      localArrayList.add("INVALID_SMSC_ADDRESS");
      j = i | 0x3A;
    }
    k = j;
    if ((paramInt & 0x3B) == 59)
    {
      localArrayList.add("NO_SUCH_ENTRY");
      k = j | 0x3B;
    }
    i = k;
    if ((paramInt & 0x3C) == 60)
    {
      localArrayList.add("NETWORK_NOT_READY");
      i = k | 0x3C;
    }
    j = i;
    if ((paramInt & 0x3D) == 61)
    {
      localArrayList.add("NOT_PROVISIONED");
      j = i | 0x3D;
    }
    k = j;
    if ((paramInt & 0x3E) == 62)
    {
      localArrayList.add("NO_SUBSCRIPTION");
      k = j | 0x3E;
    }
    i = k;
    if ((paramInt & 0x3F) == 63)
    {
      localArrayList.add("NO_NETWORK_FOUND");
      i = k | 0x3F;
    }
    k = i;
    if ((paramInt & 0x40) == 64)
    {
      localArrayList.add("DEVICE_IN_USE");
      k = i | 0x40;
    }
    j = k;
    if ((paramInt & 0x41) == 65)
    {
      localArrayList.add("ABORTED");
      j = k | 0x41;
    }
    i = j;
    if ((paramInt & 0x42) == 66)
    {
      localArrayList.add("INVALID_RESPONSE");
      i = j | 0x42;
    }
    k = i;
    if ((paramInt & 0x1F5) == 501)
    {
      localArrayList.add("OEM_ERROR_1");
      k = i | 0x1F5;
    }
    j = k;
    if ((paramInt & 0x1F6) == 502)
    {
      localArrayList.add("OEM_ERROR_2");
      j = k | 0x1F6;
    }
    i = j;
    if ((paramInt & 0x1F7) == 503)
    {
      localArrayList.add("OEM_ERROR_3");
      i = j | 0x1F7;
    }
    k = i;
    if ((paramInt & 0x1F8) == 504)
    {
      localArrayList.add("OEM_ERROR_4");
      k = i | 0x1F8;
    }
    j = k;
    if ((paramInt & 0x1F9) == 505)
    {
      localArrayList.add("OEM_ERROR_5");
      j = k | 0x1F9;
    }
    i = j;
    if ((paramInt & 0x1FA) == 506)
    {
      localArrayList.add("OEM_ERROR_6");
      i = j | 0x1FA;
    }
    j = i;
    if ((paramInt & 0x1FB) == 507)
    {
      localArrayList.add("OEM_ERROR_7");
      j = i | 0x1FB;
    }
    i = j;
    if ((paramInt & 0x1FC) == 508)
    {
      localArrayList.add("OEM_ERROR_8");
      i = j | 0x1FC;
    }
    j = i;
    if ((paramInt & 0x1FD) == 509)
    {
      localArrayList.add("OEM_ERROR_9");
      j = i | 0x1FD;
    }
    k = j;
    if ((paramInt & 0x1FE) == 510)
    {
      localArrayList.add("OEM_ERROR_10");
      k = j | 0x1FE;
    }
    i = k;
    if ((paramInt & 0x1FF) == 511)
    {
      localArrayList.add("OEM_ERROR_11");
      i = k | 0x1FF;
    }
    j = i;
    if ((paramInt & 0x200) == 512)
    {
      localArrayList.add("OEM_ERROR_12");
      j = i | 0x200;
    }
    i = j;
    if ((paramInt & 0x201) == 513)
    {
      localArrayList.add("OEM_ERROR_13");
      i = j | 0x201;
    }
    k = i;
    if ((paramInt & 0x202) == 514)
    {
      localArrayList.add("OEM_ERROR_14");
      k = i | 0x202;
    }
    j = k;
    if ((paramInt & 0x203) == 515)
    {
      localArrayList.add("OEM_ERROR_15");
      j = k | 0x203;
    }
    i = j;
    if ((paramInt & 0x204) == 516)
    {
      localArrayList.add("OEM_ERROR_16");
      i = j | 0x204;
    }
    j = i;
    if ((paramInt & 0x205) == 517)
    {
      localArrayList.add("OEM_ERROR_17");
      j = i | 0x205;
    }
    k = j;
    if ((paramInt & 0x206) == 518)
    {
      localArrayList.add("OEM_ERROR_18");
      k = j | 0x206;
    }
    i = k;
    if ((paramInt & 0x207) == 519)
    {
      localArrayList.add("OEM_ERROR_19");
      i = k | 0x207;
    }
    j = i;
    if ((paramInt & 0x208) == 520)
    {
      localArrayList.add("OEM_ERROR_20");
      j = i | 0x208;
    }
    k = j;
    if ((paramInt & 0x209) == 521)
    {
      localArrayList.add("OEM_ERROR_21");
      k = j | 0x209;
    }
    i = k;
    if ((paramInt & 0x20A) == 522)
    {
      localArrayList.add("OEM_ERROR_22");
      i = k | 0x20A;
    }
    j = i;
    if ((paramInt & 0x20B) == 523)
    {
      localArrayList.add("OEM_ERROR_23");
      j = i | 0x20B;
    }
    i = j;
    if ((paramInt & 0x20C) == 524)
    {
      localArrayList.add("OEM_ERROR_24");
      i = j | 0x20C;
    }
    j = i;
    if ((paramInt & 0x20D) == 525)
    {
      localArrayList.add("OEM_ERROR_25");
      j = i | 0x20D;
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
      return "NONE";
    }
    if (paramInt == 1) {
      return "RADIO_NOT_AVAILABLE";
    }
    if (paramInt == 2) {
      return "GENERIC_FAILURE";
    }
    if (paramInt == 3) {
      return "PASSWORD_INCORRECT";
    }
    if (paramInt == 4) {
      return "SIM_PIN2";
    }
    if (paramInt == 5) {
      return "SIM_PUK2";
    }
    if (paramInt == 6) {
      return "REQUEST_NOT_SUPPORTED";
    }
    if (paramInt == 7) {
      return "CANCELLED";
    }
    if (paramInt == 8) {
      return "OP_NOT_ALLOWED_DURING_VOICE_CALL";
    }
    if (paramInt == 9) {
      return "OP_NOT_ALLOWED_BEFORE_REG_TO_NW";
    }
    if (paramInt == 10) {
      return "SMS_SEND_FAIL_RETRY";
    }
    if (paramInt == 11) {
      return "SIM_ABSENT";
    }
    if (paramInt == 12) {
      return "SUBSCRIPTION_NOT_AVAILABLE";
    }
    if (paramInt == 13) {
      return "MODE_NOT_SUPPORTED";
    }
    if (paramInt == 14) {
      return "FDN_CHECK_FAILURE";
    }
    if (paramInt == 15) {
      return "ILLEGAL_SIM_OR_ME";
    }
    if (paramInt == 16) {
      return "MISSING_RESOURCE";
    }
    if (paramInt == 17) {
      return "NO_SUCH_ELEMENT";
    }
    if (paramInt == 18) {
      return "DIAL_MODIFIED_TO_USSD";
    }
    if (paramInt == 19) {
      return "DIAL_MODIFIED_TO_SS";
    }
    if (paramInt == 20) {
      return "DIAL_MODIFIED_TO_DIAL";
    }
    if (paramInt == 21) {
      return "USSD_MODIFIED_TO_DIAL";
    }
    if (paramInt == 22) {
      return "USSD_MODIFIED_TO_SS";
    }
    if (paramInt == 23) {
      return "USSD_MODIFIED_TO_USSD";
    }
    if (paramInt == 24) {
      return "SS_MODIFIED_TO_DIAL";
    }
    if (paramInt == 25) {
      return "SS_MODIFIED_TO_USSD";
    }
    if (paramInt == 26) {
      return "SUBSCRIPTION_NOT_SUPPORTED";
    }
    if (paramInt == 27) {
      return "SS_MODIFIED_TO_SS";
    }
    if (paramInt == 36) {
      return "LCE_NOT_SUPPORTED";
    }
    if (paramInt == 37) {
      return "NO_MEMORY";
    }
    if (paramInt == 38) {
      return "INTERNAL_ERR";
    }
    if (paramInt == 39) {
      return "SYSTEM_ERR";
    }
    if (paramInt == 40) {
      return "MODEM_ERR";
    }
    if (paramInt == 41) {
      return "INVALID_STATE";
    }
    if (paramInt == 42) {
      return "NO_RESOURCES";
    }
    if (paramInt == 43) {
      return "SIM_ERR";
    }
    if (paramInt == 44) {
      return "INVALID_ARGUMENTS";
    }
    if (paramInt == 45) {
      return "INVALID_SIM_STATE";
    }
    if (paramInt == 46) {
      return "INVALID_MODEM_STATE";
    }
    if (paramInt == 47) {
      return "INVALID_CALL_ID";
    }
    if (paramInt == 48) {
      return "NO_SMS_TO_ACK";
    }
    if (paramInt == 49) {
      return "NETWORK_ERR";
    }
    if (paramInt == 50) {
      return "REQUEST_RATE_LIMITED";
    }
    if (paramInt == 51) {
      return "SIM_BUSY";
    }
    if (paramInt == 52) {
      return "SIM_FULL";
    }
    if (paramInt == 53) {
      return "NETWORK_REJECT";
    }
    if (paramInt == 54) {
      return "OPERATION_NOT_ALLOWED";
    }
    if (paramInt == 55) {
      return "EMPTY_RECORD";
    }
    if (paramInt == 56) {
      return "INVALID_SMS_FORMAT";
    }
    if (paramInt == 57) {
      return "ENCODING_ERR";
    }
    if (paramInt == 58) {
      return "INVALID_SMSC_ADDRESS";
    }
    if (paramInt == 59) {
      return "NO_SUCH_ENTRY";
    }
    if (paramInt == 60) {
      return "NETWORK_NOT_READY";
    }
    if (paramInt == 61) {
      return "NOT_PROVISIONED";
    }
    if (paramInt == 62) {
      return "NO_SUBSCRIPTION";
    }
    if (paramInt == 63) {
      return "NO_NETWORK_FOUND";
    }
    if (paramInt == 64) {
      return "DEVICE_IN_USE";
    }
    if (paramInt == 65) {
      return "ABORTED";
    }
    if (paramInt == 66) {
      return "INVALID_RESPONSE";
    }
    if (paramInt == 501) {
      return "OEM_ERROR_1";
    }
    if (paramInt == 502) {
      return "OEM_ERROR_2";
    }
    if (paramInt == 503) {
      return "OEM_ERROR_3";
    }
    if (paramInt == 504) {
      return "OEM_ERROR_4";
    }
    if (paramInt == 505) {
      return "OEM_ERROR_5";
    }
    if (paramInt == 506) {
      return "OEM_ERROR_6";
    }
    if (paramInt == 507) {
      return "OEM_ERROR_7";
    }
    if (paramInt == 508) {
      return "OEM_ERROR_8";
    }
    if (paramInt == 509) {
      return "OEM_ERROR_9";
    }
    if (paramInt == 510) {
      return "OEM_ERROR_10";
    }
    if (paramInt == 511) {
      return "OEM_ERROR_11";
    }
    if (paramInt == 512) {
      return "OEM_ERROR_12";
    }
    if (paramInt == 513) {
      return "OEM_ERROR_13";
    }
    if (paramInt == 514) {
      return "OEM_ERROR_14";
    }
    if (paramInt == 515) {
      return "OEM_ERROR_15";
    }
    if (paramInt == 516) {
      return "OEM_ERROR_16";
    }
    if (paramInt == 517) {
      return "OEM_ERROR_17";
    }
    if (paramInt == 518) {
      return "OEM_ERROR_18";
    }
    if (paramInt == 519) {
      return "OEM_ERROR_19";
    }
    if (paramInt == 520) {
      return "OEM_ERROR_20";
    }
    if (paramInt == 521) {
      return "OEM_ERROR_21";
    }
    if (paramInt == 522) {
      return "OEM_ERROR_22";
    }
    if (paramInt == 523) {
      return "OEM_ERROR_23";
    }
    if (paramInt == 524) {
      return "OEM_ERROR_24";
    }
    if (paramInt == 525) {
      return "OEM_ERROR_25";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
