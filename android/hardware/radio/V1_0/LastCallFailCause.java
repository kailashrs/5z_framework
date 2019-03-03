package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class LastCallFailCause
{
  public static final int ACCESS_CLASS_BLOCKED = 260;
  public static final int ACCESS_INFORMATION_DISCARDED = 43;
  public static final int ACM_LIMIT_EXCEEDED = 68;
  public static final int BEARER_CAPABILITY_NOT_AUTHORIZED = 57;
  public static final int BEARER_CAPABILITY_UNAVAILABLE = 58;
  public static final int BEARER_SERVICE_NOT_IMPLEMENTED = 65;
  public static final int BUSY = 17;
  public static final int CALL_BARRED = 240;
  public static final int CALL_REJECTED = 21;
  public static final int CDMA_ACCESS_BLOCKED = 1009;
  public static final int CDMA_ACCESS_FAILURE = 1006;
  public static final int CDMA_DROP = 1001;
  public static final int CDMA_INTERCEPT = 1002;
  public static final int CDMA_LOCKED_UNTIL_POWER_CYCLE = 1000;
  public static final int CDMA_NOT_EMERGENCY = 1008;
  public static final int CDMA_PREEMPTED = 1007;
  public static final int CDMA_REORDER = 1003;
  public static final int CDMA_RETRY_ORDER = 1005;
  public static final int CDMA_SO_REJECT = 1004;
  public static final int CHANNEL_UNACCEPTABLE = 6;
  public static final int CONDITIONAL_IE_ERROR = 100;
  public static final int CONGESTION = 34;
  public static final int DESTINATION_OUT_OF_ORDER = 27;
  public static final int DIAL_MODIFIED_TO_DIAL = 246;
  public static final int DIAL_MODIFIED_TO_SS = 245;
  public static final int DIAL_MODIFIED_TO_USSD = 244;
  public static final int ERROR_UNSPECIFIED = 65535;
  public static final int FACILITY_REJECTED = 29;
  public static final int FDN_BLOCKED = 241;
  public static final int IMEI_NOT_ACCEPTED = 243;
  public static final int IMSI_UNKNOWN_IN_VLR = 242;
  public static final int INCOMING_CALLS_BARRED_WITHIN_CUG = 55;
  public static final int INCOMPATIBLE_DESTINATION = 88;
  public static final int INFORMATION_ELEMENT_NON_EXISTENT = 99;
  public static final int INTERWORKING_UNSPECIFIED = 127;
  public static final int INVALID_MANDATORY_INFORMATION = 96;
  public static final int INVALID_NUMBER_FORMAT = 28;
  public static final int INVALID_TRANSACTION_IDENTIFIER = 81;
  public static final int INVALID_TRANSIT_NW_SELECTION = 91;
  public static final int MESSAGE_NOT_COMPATIBLE_WITH_PROTOCOL_STATE = 101;
  public static final int MESSAGE_TYPE_NON_IMPLEMENTED = 97;
  public static final int MESSAGE_TYPE_NOT_COMPATIBLE_WITH_PROTOCOL_STATE = 98;
  public static final int NETWORK_DETACH = 261;
  public static final int NETWORK_OUT_OF_ORDER = 38;
  public static final int NETWORK_REJECT = 252;
  public static final int NETWORK_RESP_TIMEOUT = 251;
  public static final int NORMAL = 16;
  public static final int NORMAL_UNSPECIFIED = 31;
  public static final int NO_ANSWER_FROM_USER = 19;
  public static final int NO_ROUTE_TO_DESTINATION = 3;
  public static final int NO_USER_RESPONDING = 18;
  public static final int NO_VALID_SIM = 249;
  public static final int NUMBER_CHANGED = 22;
  public static final int OEM_CAUSE_1 = 61441;
  public static final int OEM_CAUSE_10 = 61450;
  public static final int OEM_CAUSE_11 = 61451;
  public static final int OEM_CAUSE_12 = 61452;
  public static final int OEM_CAUSE_13 = 61453;
  public static final int OEM_CAUSE_14 = 61454;
  public static final int OEM_CAUSE_15 = 61455;
  public static final int OEM_CAUSE_2 = 61442;
  public static final int OEM_CAUSE_3 = 61443;
  public static final int OEM_CAUSE_4 = 61444;
  public static final int OEM_CAUSE_5 = 61445;
  public static final int OEM_CAUSE_6 = 61446;
  public static final int OEM_CAUSE_7 = 61447;
  public static final int OEM_CAUSE_8 = 61448;
  public static final int OEM_CAUSE_9 = 61449;
  public static final int ONLY_DIGITAL_INFORMATION_BEARER_AVAILABLE = 70;
  public static final int OPERATOR_DETERMINED_BARRING = 8;
  public static final int OUT_OF_SERVICE = 248;
  public static final int PREEMPTION = 25;
  public static final int PROTOCOL_ERROR_UNSPECIFIED = 111;
  public static final int QOS_UNAVAILABLE = 49;
  public static final int RADIO_ACCESS_FAILURE = 253;
  public static final int RADIO_INTERNAL_ERROR = 250;
  public static final int RADIO_LINK_FAILURE = 254;
  public static final int RADIO_LINK_LOST = 255;
  public static final int RADIO_OFF = 247;
  public static final int RADIO_RELEASE_ABNORMAL = 259;
  public static final int RADIO_RELEASE_NORMAL = 258;
  public static final int RADIO_SETUP_FAILURE = 257;
  public static final int RADIO_UPLINK_FAILURE = 256;
  public static final int RECOVERY_ON_TIMER_EXPIRED = 102;
  public static final int REQUESTED_CIRCUIT_OR_CHANNEL_NOT_AVAILABLE = 44;
  public static final int REQUESTED_FACILITY_NOT_IMPLEMENTED = 69;
  public static final int REQUESTED_FACILITY_NOT_SUBSCRIBED = 50;
  public static final int RESOURCES_UNAVAILABLE_OR_UNSPECIFIED = 47;
  public static final int RESP_TO_STATUS_ENQUIRY = 30;
  public static final int SEMANTICALLY_INCORRECT_MESSAGE = 95;
  public static final int SERVICE_OPTION_NOT_AVAILABLE = 63;
  public static final int SERVICE_OR_OPTION_NOT_IMPLEMENTED = 79;
  public static final int SWITCHING_EQUIPMENT_CONGESTION = 42;
  public static final int TEMPORARY_FAILURE = 41;
  public static final int UNOBTAINABLE_NUMBER = 1;
  public static final int USER_NOT_MEMBER_OF_CUG = 87;
  
  public LastCallFailCause() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("UNOBTAINABLE_NUMBER");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("NO_ROUTE_TO_DESTINATION");
      j = i | 0x3;
    }
    i = j;
    if ((paramInt & 0x6) == 6)
    {
      localArrayList.add("CHANNEL_UNACCEPTABLE");
      i = j | 0x6;
    }
    j = i;
    if ((paramInt & 0x8) == 8)
    {
      localArrayList.add("OPERATOR_DETERMINED_BARRING");
      j = i | 0x8;
    }
    i = j;
    if ((paramInt & 0x10) == 16)
    {
      localArrayList.add("NORMAL");
      i = j | 0x10;
    }
    j = i;
    if ((paramInt & 0x11) == 17)
    {
      localArrayList.add("BUSY");
      j = i | 0x11;
    }
    i = j;
    if ((paramInt & 0x12) == 18)
    {
      localArrayList.add("NO_USER_RESPONDING");
      i = j | 0x12;
    }
    j = i;
    if ((paramInt & 0x13) == 19)
    {
      localArrayList.add("NO_ANSWER_FROM_USER");
      j = i | 0x13;
    }
    i = j;
    if ((paramInt & 0x15) == 21)
    {
      localArrayList.add("CALL_REJECTED");
      i = j | 0x15;
    }
    j = i;
    if ((paramInt & 0x16) == 22)
    {
      localArrayList.add("NUMBER_CHANGED");
      j = i | 0x16;
    }
    int k = j;
    if ((paramInt & 0x19) == 25)
    {
      localArrayList.add("PREEMPTION");
      k = j | 0x19;
    }
    i = k;
    if ((paramInt & 0x1B) == 27)
    {
      localArrayList.add("DESTINATION_OUT_OF_ORDER");
      i = k | 0x1B;
    }
    j = i;
    if ((paramInt & 0x1C) == 28)
    {
      localArrayList.add("INVALID_NUMBER_FORMAT");
      j = i | 0x1C;
    }
    i = j;
    if ((paramInt & 0x1D) == 29)
    {
      localArrayList.add("FACILITY_REJECTED");
      i = j | 0x1D;
    }
    j = i;
    if ((paramInt & 0x1E) == 30)
    {
      localArrayList.add("RESP_TO_STATUS_ENQUIRY");
      j = i | 0x1E;
    }
    i = j;
    if ((paramInt & 0x1F) == 31)
    {
      localArrayList.add("NORMAL_UNSPECIFIED");
      i = j | 0x1F;
    }
    j = i;
    if ((paramInt & 0x22) == 34)
    {
      localArrayList.add("CONGESTION");
      j = i | 0x22;
    }
    i = j;
    if ((paramInt & 0x26) == 38)
    {
      localArrayList.add("NETWORK_OUT_OF_ORDER");
      i = j | 0x26;
    }
    j = i;
    if ((paramInt & 0x29) == 41)
    {
      localArrayList.add("TEMPORARY_FAILURE");
      j = i | 0x29;
    }
    i = j;
    if ((paramInt & 0x2A) == 42)
    {
      localArrayList.add("SWITCHING_EQUIPMENT_CONGESTION");
      i = j | 0x2A;
    }
    k = i;
    if ((paramInt & 0x2B) == 43)
    {
      localArrayList.add("ACCESS_INFORMATION_DISCARDED");
      k = i | 0x2B;
    }
    j = k;
    if ((paramInt & 0x2C) == 44)
    {
      localArrayList.add("REQUESTED_CIRCUIT_OR_CHANNEL_NOT_AVAILABLE");
      j = k | 0x2C;
    }
    i = j;
    if ((paramInt & 0x2F) == 47)
    {
      localArrayList.add("RESOURCES_UNAVAILABLE_OR_UNSPECIFIED");
      i = j | 0x2F;
    }
    j = i;
    if ((paramInt & 0x31) == 49)
    {
      localArrayList.add("QOS_UNAVAILABLE");
      j = i | 0x31;
    }
    i = j;
    if ((paramInt & 0x32) == 50)
    {
      localArrayList.add("REQUESTED_FACILITY_NOT_SUBSCRIBED");
      i = j | 0x32;
    }
    j = i;
    if ((paramInt & 0x37) == 55)
    {
      localArrayList.add("INCOMING_CALLS_BARRED_WITHIN_CUG");
      j = i | 0x37;
    }
    k = j;
    if ((paramInt & 0x39) == 57)
    {
      localArrayList.add("BEARER_CAPABILITY_NOT_AUTHORIZED");
      k = j | 0x39;
    }
    i = k;
    if ((paramInt & 0x3A) == 58)
    {
      localArrayList.add("BEARER_CAPABILITY_UNAVAILABLE");
      i = k | 0x3A;
    }
    k = i;
    if ((paramInt & 0x3F) == 63)
    {
      localArrayList.add("SERVICE_OPTION_NOT_AVAILABLE");
      k = i | 0x3F;
    }
    j = k;
    if ((paramInt & 0x41) == 65)
    {
      localArrayList.add("BEARER_SERVICE_NOT_IMPLEMENTED");
      j = k | 0x41;
    }
    i = j;
    if ((paramInt & 0x44) == 68)
    {
      localArrayList.add("ACM_LIMIT_EXCEEDED");
      i = j | 0x44;
    }
    j = i;
    if ((paramInt & 0x45) == 69)
    {
      localArrayList.add("REQUESTED_FACILITY_NOT_IMPLEMENTED");
      j = i | 0x45;
    }
    i = j;
    if ((paramInt & 0x46) == 70)
    {
      localArrayList.add("ONLY_DIGITAL_INFORMATION_BEARER_AVAILABLE");
      i = j | 0x46;
    }
    k = i;
    if ((paramInt & 0x4F) == 79)
    {
      localArrayList.add("SERVICE_OR_OPTION_NOT_IMPLEMENTED");
      k = i | 0x4F;
    }
    j = k;
    if ((paramInt & 0x51) == 81)
    {
      localArrayList.add("INVALID_TRANSACTION_IDENTIFIER");
      j = k | 0x51;
    }
    i = j;
    if ((paramInt & 0x57) == 87)
    {
      localArrayList.add("USER_NOT_MEMBER_OF_CUG");
      i = j | 0x57;
    }
    j = i;
    if ((paramInt & 0x58) == 88)
    {
      localArrayList.add("INCOMPATIBLE_DESTINATION");
      j = i | 0x58;
    }
    k = j;
    if ((paramInt & 0x5B) == 91)
    {
      localArrayList.add("INVALID_TRANSIT_NW_SELECTION");
      k = j | 0x5B;
    }
    i = k;
    if ((paramInt & 0x5F) == 95)
    {
      localArrayList.add("SEMANTICALLY_INCORRECT_MESSAGE");
      i = k | 0x5F;
    }
    j = i;
    if ((paramInt & 0x60) == 96)
    {
      localArrayList.add("INVALID_MANDATORY_INFORMATION");
      j = i | 0x60;
    }
    i = j;
    if ((paramInt & 0x61) == 97)
    {
      localArrayList.add("MESSAGE_TYPE_NON_IMPLEMENTED");
      i = j | 0x61;
    }
    j = i;
    if ((paramInt & 0x62) == 98)
    {
      localArrayList.add("MESSAGE_TYPE_NOT_COMPATIBLE_WITH_PROTOCOL_STATE");
      j = i | 0x62;
    }
    i = j;
    if ((paramInt & 0x63) == 99)
    {
      localArrayList.add("INFORMATION_ELEMENT_NON_EXISTENT");
      i = j | 0x63;
    }
    j = i;
    if ((paramInt & 0x64) == 100)
    {
      localArrayList.add("CONDITIONAL_IE_ERROR");
      j = i | 0x64;
    }
    i = j;
    if ((paramInt & 0x65) == 101)
    {
      localArrayList.add("MESSAGE_NOT_COMPATIBLE_WITH_PROTOCOL_STATE");
      i = j | 0x65;
    }
    j = i;
    if ((paramInt & 0x66) == 102)
    {
      localArrayList.add("RECOVERY_ON_TIMER_EXPIRED");
      j = i | 0x66;
    }
    i = j;
    if ((paramInt & 0x6F) == 111)
    {
      localArrayList.add("PROTOCOL_ERROR_UNSPECIFIED");
      i = j | 0x6F;
    }
    j = i;
    if ((paramInt & 0x7F) == 127)
    {
      localArrayList.add("INTERWORKING_UNSPECIFIED");
      j = i | 0x7F;
    }
    i = j;
    if ((paramInt & 0xF0) == 240)
    {
      localArrayList.add("CALL_BARRED");
      i = j | 0xF0;
    }
    j = i;
    if ((paramInt & 0xF1) == 241)
    {
      localArrayList.add("FDN_BLOCKED");
      j = i | 0xF1;
    }
    i = j;
    if ((paramInt & 0xF2) == 242)
    {
      localArrayList.add("IMSI_UNKNOWN_IN_VLR");
      i = j | 0xF2;
    }
    j = i;
    if ((paramInt & 0xF3) == 243)
    {
      localArrayList.add("IMEI_NOT_ACCEPTED");
      j = i | 0xF3;
    }
    i = j;
    if ((paramInt & 0xF4) == 244)
    {
      localArrayList.add("DIAL_MODIFIED_TO_USSD");
      i = j | 0xF4;
    }
    j = i;
    if ((paramInt & 0xF5) == 245)
    {
      localArrayList.add("DIAL_MODIFIED_TO_SS");
      j = i | 0xF5;
    }
    k = j;
    if ((paramInt & 0xF6) == 246)
    {
      localArrayList.add("DIAL_MODIFIED_TO_DIAL");
      k = j | 0xF6;
    }
    i = k;
    if ((paramInt & 0xF7) == 247)
    {
      localArrayList.add("RADIO_OFF");
      i = k | 0xF7;
    }
    j = i;
    if ((paramInt & 0xF8) == 248)
    {
      localArrayList.add("OUT_OF_SERVICE");
      j = i | 0xF8;
    }
    i = j;
    if ((paramInt & 0xF9) == 249)
    {
      localArrayList.add("NO_VALID_SIM");
      i = j | 0xF9;
    }
    j = i;
    if ((paramInt & 0xFA) == 250)
    {
      localArrayList.add("RADIO_INTERNAL_ERROR");
      j = i | 0xFA;
    }
    i = j;
    if ((paramInt & 0xFB) == 251)
    {
      localArrayList.add("NETWORK_RESP_TIMEOUT");
      i = j | 0xFB;
    }
    k = i;
    if ((paramInt & 0xFC) == 252)
    {
      localArrayList.add("NETWORK_REJECT");
      k = i | 0xFC;
    }
    j = k;
    if ((paramInt & 0xFD) == 253)
    {
      localArrayList.add("RADIO_ACCESS_FAILURE");
      j = k | 0xFD;
    }
    i = j;
    if ((paramInt & 0xFE) == 254)
    {
      localArrayList.add("RADIO_LINK_FAILURE");
      i = j | 0xFE;
    }
    j = i;
    if ((paramInt & 0xFF) == 255)
    {
      localArrayList.add("RADIO_LINK_LOST");
      j = i | 0xFF;
    }
    i = j;
    if ((paramInt & 0x100) == 256)
    {
      localArrayList.add("RADIO_UPLINK_FAILURE");
      i = j | 0x100;
    }
    j = i;
    if ((paramInt & 0x101) == 257)
    {
      localArrayList.add("RADIO_SETUP_FAILURE");
      j = i | 0x101;
    }
    i = j;
    if ((paramInt & 0x102) == 258)
    {
      localArrayList.add("RADIO_RELEASE_NORMAL");
      i = j | 0x102;
    }
    j = i;
    if ((paramInt & 0x103) == 259)
    {
      localArrayList.add("RADIO_RELEASE_ABNORMAL");
      j = i | 0x103;
    }
    i = j;
    if ((paramInt & 0x104) == 260)
    {
      localArrayList.add("ACCESS_CLASS_BLOCKED");
      i = j | 0x104;
    }
    j = i;
    if ((paramInt & 0x105) == 261)
    {
      localArrayList.add("NETWORK_DETACH");
      j = i | 0x105;
    }
    i = j;
    if ((paramInt & 0x3E8) == 1000)
    {
      localArrayList.add("CDMA_LOCKED_UNTIL_POWER_CYCLE");
      i = j | 0x3E8;
    }
    k = i;
    if ((paramInt & 0x3E9) == 1001)
    {
      localArrayList.add("CDMA_DROP");
      k = i | 0x3E9;
    }
    j = k;
    if ((paramInt & 0x3EA) == 1002)
    {
      localArrayList.add("CDMA_INTERCEPT");
      j = k | 0x3EA;
    }
    i = j;
    if ((paramInt & 0x3EB) == 1003)
    {
      localArrayList.add("CDMA_REORDER");
      i = j | 0x3EB;
    }
    j = i;
    if ((paramInt & 0x3EC) == 1004)
    {
      localArrayList.add("CDMA_SO_REJECT");
      j = i | 0x3EC;
    }
    k = j;
    if ((paramInt & 0x3ED) == 1005)
    {
      localArrayList.add("CDMA_RETRY_ORDER");
      k = j | 0x3ED;
    }
    i = k;
    if ((paramInt & 0x3EE) == 1006)
    {
      localArrayList.add("CDMA_ACCESS_FAILURE");
      i = k | 0x3EE;
    }
    j = i;
    if ((paramInt & 0x3EF) == 1007)
    {
      localArrayList.add("CDMA_PREEMPTED");
      j = i | 0x3EF;
    }
    i = j;
    if ((paramInt & 0x3F0) == 1008)
    {
      localArrayList.add("CDMA_NOT_EMERGENCY");
      i = j | 0x3F0;
    }
    j = i;
    if ((paramInt & 0x3F1) == 1009)
    {
      localArrayList.add("CDMA_ACCESS_BLOCKED");
      j = i | 0x3F1;
    }
    i = j;
    if ((0xF001 & paramInt) == 61441)
    {
      localArrayList.add("OEM_CAUSE_1");
      i = j | 0xF001;
    }
    j = i;
    if ((0xF002 & paramInt) == 61442)
    {
      localArrayList.add("OEM_CAUSE_2");
      j = i | 0xF002;
    }
    i = j;
    if ((0xF003 & paramInt) == 61443)
    {
      localArrayList.add("OEM_CAUSE_3");
      i = j | 0xF003;
    }
    j = i;
    if ((0xF004 & paramInt) == 61444)
    {
      localArrayList.add("OEM_CAUSE_4");
      j = i | 0xF004;
    }
    k = j;
    if ((0xF005 & paramInt) == 61445)
    {
      localArrayList.add("OEM_CAUSE_5");
      k = j | 0xF005;
    }
    i = k;
    if ((0xF006 & paramInt) == 61446)
    {
      localArrayList.add("OEM_CAUSE_6");
      i = k | 0xF006;
    }
    j = i;
    if ((0xF007 & paramInt) == 61447)
    {
      localArrayList.add("OEM_CAUSE_7");
      j = i | 0xF007;
    }
    i = j;
    if ((0xF008 & paramInt) == 61448)
    {
      localArrayList.add("OEM_CAUSE_8");
      i = j | 0xF008;
    }
    j = i;
    if ((0xF009 & paramInt) == 61449)
    {
      localArrayList.add("OEM_CAUSE_9");
      j = i | 0xF009;
    }
    k = j;
    if ((0xF00A & paramInt) == 61450)
    {
      localArrayList.add("OEM_CAUSE_10");
      k = j | 0xF00A;
    }
    i = k;
    if ((0xF00B & paramInt) == 61451)
    {
      localArrayList.add("OEM_CAUSE_11");
      i = k | 0xF00B;
    }
    j = i;
    if ((0xF00C & paramInt) == 61452)
    {
      localArrayList.add("OEM_CAUSE_12");
      j = i | 0xF00C;
    }
    k = j;
    if ((0xF00D & paramInt) == 61453)
    {
      localArrayList.add("OEM_CAUSE_13");
      k = j | 0xF00D;
    }
    i = k;
    if ((0xF00E & paramInt) == 61454)
    {
      localArrayList.add("OEM_CAUSE_14");
      i = k | 0xF00E;
    }
    j = i;
    if ((0xF00F & paramInt) == 61455)
    {
      localArrayList.add("OEM_CAUSE_15");
      j = i | 0xF00F;
    }
    i = j;
    if ((0xFFFF & paramInt) == 65535)
    {
      localArrayList.add("ERROR_UNSPECIFIED");
      i = j | 0xFFFF;
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
    if (paramInt == 1) {
      return "UNOBTAINABLE_NUMBER";
    }
    if (paramInt == 3) {
      return "NO_ROUTE_TO_DESTINATION";
    }
    if (paramInt == 6) {
      return "CHANNEL_UNACCEPTABLE";
    }
    if (paramInt == 8) {
      return "OPERATOR_DETERMINED_BARRING";
    }
    if (paramInt == 16) {
      return "NORMAL";
    }
    if (paramInt == 17) {
      return "BUSY";
    }
    if (paramInt == 18) {
      return "NO_USER_RESPONDING";
    }
    if (paramInt == 19) {
      return "NO_ANSWER_FROM_USER";
    }
    if (paramInt == 21) {
      return "CALL_REJECTED";
    }
    if (paramInt == 22) {
      return "NUMBER_CHANGED";
    }
    if (paramInt == 25) {
      return "PREEMPTION";
    }
    if (paramInt == 27) {
      return "DESTINATION_OUT_OF_ORDER";
    }
    if (paramInt == 28) {
      return "INVALID_NUMBER_FORMAT";
    }
    if (paramInt == 29) {
      return "FACILITY_REJECTED";
    }
    if (paramInt == 30) {
      return "RESP_TO_STATUS_ENQUIRY";
    }
    if (paramInt == 31) {
      return "NORMAL_UNSPECIFIED";
    }
    if (paramInt == 34) {
      return "CONGESTION";
    }
    if (paramInt == 38) {
      return "NETWORK_OUT_OF_ORDER";
    }
    if (paramInt == 41) {
      return "TEMPORARY_FAILURE";
    }
    if (paramInt == 42) {
      return "SWITCHING_EQUIPMENT_CONGESTION";
    }
    if (paramInt == 43) {
      return "ACCESS_INFORMATION_DISCARDED";
    }
    if (paramInt == 44) {
      return "REQUESTED_CIRCUIT_OR_CHANNEL_NOT_AVAILABLE";
    }
    if (paramInt == 47) {
      return "RESOURCES_UNAVAILABLE_OR_UNSPECIFIED";
    }
    if (paramInt == 49) {
      return "QOS_UNAVAILABLE";
    }
    if (paramInt == 50) {
      return "REQUESTED_FACILITY_NOT_SUBSCRIBED";
    }
    if (paramInt == 55) {
      return "INCOMING_CALLS_BARRED_WITHIN_CUG";
    }
    if (paramInt == 57) {
      return "BEARER_CAPABILITY_NOT_AUTHORIZED";
    }
    if (paramInt == 58) {
      return "BEARER_CAPABILITY_UNAVAILABLE";
    }
    if (paramInt == 63) {
      return "SERVICE_OPTION_NOT_AVAILABLE";
    }
    if (paramInt == 65) {
      return "BEARER_SERVICE_NOT_IMPLEMENTED";
    }
    if (paramInt == 68) {
      return "ACM_LIMIT_EXCEEDED";
    }
    if (paramInt == 69) {
      return "REQUESTED_FACILITY_NOT_IMPLEMENTED";
    }
    if (paramInt == 70) {
      return "ONLY_DIGITAL_INFORMATION_BEARER_AVAILABLE";
    }
    if (paramInt == 79) {
      return "SERVICE_OR_OPTION_NOT_IMPLEMENTED";
    }
    if (paramInt == 81) {
      return "INVALID_TRANSACTION_IDENTIFIER";
    }
    if (paramInt == 87) {
      return "USER_NOT_MEMBER_OF_CUG";
    }
    if (paramInt == 88) {
      return "INCOMPATIBLE_DESTINATION";
    }
    if (paramInt == 91) {
      return "INVALID_TRANSIT_NW_SELECTION";
    }
    if (paramInt == 95) {
      return "SEMANTICALLY_INCORRECT_MESSAGE";
    }
    if (paramInt == 96) {
      return "INVALID_MANDATORY_INFORMATION";
    }
    if (paramInt == 97) {
      return "MESSAGE_TYPE_NON_IMPLEMENTED";
    }
    if (paramInt == 98) {
      return "MESSAGE_TYPE_NOT_COMPATIBLE_WITH_PROTOCOL_STATE";
    }
    if (paramInt == 99) {
      return "INFORMATION_ELEMENT_NON_EXISTENT";
    }
    if (paramInt == 100) {
      return "CONDITIONAL_IE_ERROR";
    }
    if (paramInt == 101) {
      return "MESSAGE_NOT_COMPATIBLE_WITH_PROTOCOL_STATE";
    }
    if (paramInt == 102) {
      return "RECOVERY_ON_TIMER_EXPIRED";
    }
    if (paramInt == 111) {
      return "PROTOCOL_ERROR_UNSPECIFIED";
    }
    if (paramInt == 127) {
      return "INTERWORKING_UNSPECIFIED";
    }
    if (paramInt == 240) {
      return "CALL_BARRED";
    }
    if (paramInt == 241) {
      return "FDN_BLOCKED";
    }
    if (paramInt == 242) {
      return "IMSI_UNKNOWN_IN_VLR";
    }
    if (paramInt == 243) {
      return "IMEI_NOT_ACCEPTED";
    }
    if (paramInt == 244) {
      return "DIAL_MODIFIED_TO_USSD";
    }
    if (paramInt == 245) {
      return "DIAL_MODIFIED_TO_SS";
    }
    if (paramInt == 246) {
      return "DIAL_MODIFIED_TO_DIAL";
    }
    if (paramInt == 247) {
      return "RADIO_OFF";
    }
    if (paramInt == 248) {
      return "OUT_OF_SERVICE";
    }
    if (paramInt == 249) {
      return "NO_VALID_SIM";
    }
    if (paramInt == 250) {
      return "RADIO_INTERNAL_ERROR";
    }
    if (paramInt == 251) {
      return "NETWORK_RESP_TIMEOUT";
    }
    if (paramInt == 252) {
      return "NETWORK_REJECT";
    }
    if (paramInt == 253) {
      return "RADIO_ACCESS_FAILURE";
    }
    if (paramInt == 254) {
      return "RADIO_LINK_FAILURE";
    }
    if (paramInt == 255) {
      return "RADIO_LINK_LOST";
    }
    if (paramInt == 256) {
      return "RADIO_UPLINK_FAILURE";
    }
    if (paramInt == 257) {
      return "RADIO_SETUP_FAILURE";
    }
    if (paramInt == 258) {
      return "RADIO_RELEASE_NORMAL";
    }
    if (paramInt == 259) {
      return "RADIO_RELEASE_ABNORMAL";
    }
    if (paramInt == 260) {
      return "ACCESS_CLASS_BLOCKED";
    }
    if (paramInt == 261) {
      return "NETWORK_DETACH";
    }
    if (paramInt == 1000) {
      return "CDMA_LOCKED_UNTIL_POWER_CYCLE";
    }
    if (paramInt == 1001) {
      return "CDMA_DROP";
    }
    if (paramInt == 1002) {
      return "CDMA_INTERCEPT";
    }
    if (paramInt == 1003) {
      return "CDMA_REORDER";
    }
    if (paramInt == 1004) {
      return "CDMA_SO_REJECT";
    }
    if (paramInt == 1005) {
      return "CDMA_RETRY_ORDER";
    }
    if (paramInt == 1006) {
      return "CDMA_ACCESS_FAILURE";
    }
    if (paramInt == 1007) {
      return "CDMA_PREEMPTED";
    }
    if (paramInt == 1008) {
      return "CDMA_NOT_EMERGENCY";
    }
    if (paramInt == 1009) {
      return "CDMA_ACCESS_BLOCKED";
    }
    if (paramInt == 61441) {
      return "OEM_CAUSE_1";
    }
    if (paramInt == 61442) {
      return "OEM_CAUSE_2";
    }
    if (paramInt == 61443) {
      return "OEM_CAUSE_3";
    }
    if (paramInt == 61444) {
      return "OEM_CAUSE_4";
    }
    if (paramInt == 61445) {
      return "OEM_CAUSE_5";
    }
    if (paramInt == 61446) {
      return "OEM_CAUSE_6";
    }
    if (paramInt == 61447) {
      return "OEM_CAUSE_7";
    }
    if (paramInt == 61448) {
      return "OEM_CAUSE_8";
    }
    if (paramInt == 61449) {
      return "OEM_CAUSE_9";
    }
    if (paramInt == 61450) {
      return "OEM_CAUSE_10";
    }
    if (paramInt == 61451) {
      return "OEM_CAUSE_11";
    }
    if (paramInt == 61452) {
      return "OEM_CAUSE_12";
    }
    if (paramInt == 61453) {
      return "OEM_CAUSE_13";
    }
    if (paramInt == 61454) {
      return "OEM_CAUSE_14";
    }
    if (paramInt == 61455) {
      return "OEM_CAUSE_15";
    }
    if (paramInt == 65535) {
      return "ERROR_UNSPECIFIED";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
