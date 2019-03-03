package android.telephony;

public class DisconnectCause
{
  public static final int ACCESS_INFORMATION_DISCARDED = 112;
  public static final int ANSWERED_ELSEWHERE = 52;
  public static final int BEARER_CAPABILITY_NOT_AUTHORIZED = 102;
  public static final int BEARER_CAPABILITY_UNAVAILABLE = 74;
  public static final int BEARER_SERVICE_NOT_IMPLEMENTED = 76;
  public static final int BUSY = 4;
  public static final int CALL_BARRED = 20;
  public static final int CALL_FAIL_DESTINATION_OUT_OF_ORDER = 101;
  public static final int CALL_FAIL_NO_ANSWER_FROM_USER = 100;
  public static final int CALL_FAIL_NO_USER_RESPONDING = 99;
  public static final int CALL_PULLED = 51;
  public static final int CALL_REJECTED = 104;
  public static final int CDMA_ACCESS_BLOCKED = 35;
  public static final int CDMA_ACCESS_FAILURE = 32;
  public static final int CDMA_ALREADY_ACTIVATED = 49;
  public static final int CDMA_CALL_LOST = 41;
  public static final int CDMA_DROP = 27;
  public static final int CDMA_INTERCEPT = 28;
  public static final int CDMA_LOCKED_UNTIL_POWER_CYCLE = 26;
  public static final int CDMA_NOT_EMERGENCY = 34;
  public static final int CDMA_PREEMPTED = 33;
  public static final int CDMA_REORDER = 29;
  public static final int CDMA_RETRY_ORDER = 31;
  public static final int CDMA_SO_REJECT = 30;
  public static final int CHANNEL_UNACCEPTABLE = 103;
  public static final int CONDITIONAL_IE_ERROR = 89;
  public static final int CONGESTION = 5;
  public static final int CS_RESTRICTED = 22;
  public static final int CS_RESTRICTED_EMERGENCY = 24;
  public static final int CS_RESTRICTED_NORMAL = 23;
  public static final int DATA_DISABLED = 54;
  public static final int DATA_LIMIT_REACHED = 55;
  public static final int DIALED_CALL_FORWARDING_WHILE_ROAMING = 57;
  public static final int DIALED_MMI = 39;
  public static final int DIAL_LOW_BATTERY = 62;
  public static final int DIAL_MODIFIED_TO_DIAL = 48;
  public static final int DIAL_MODIFIED_TO_DIAL_VIDEO = 66;
  public static final int DIAL_MODIFIED_TO_SS = 47;
  public static final int DIAL_MODIFIED_TO_USSD = 46;
  public static final int DIAL_VIDEO_MODIFIED_TO_DIAL = 69;
  public static final int DIAL_VIDEO_MODIFIED_TO_DIAL_VIDEO = 70;
  public static final int DIAL_VIDEO_MODIFIED_TO_SS = 67;
  public static final int DIAL_VIDEO_MODIFIED_TO_USSD = 68;
  public static final int EMERGENCY_ONLY = 37;
  public static final int EMERGENCY_PERM_FAILURE = 64;
  public static final int EMERGENCY_TEMP_FAILURE = 63;
  public static final int ERROR_UNSPECIFIED = 36;
  public static final int EXITED_ECM = 42;
  public static final int FACILITY_REJECTED = 107;
  public static final int FDN_BLOCKED = 21;
  public static final int HO_NOT_FEASIBLE = 115;
  public static final int ICC_ERROR = 19;
  public static final int IMEI_NOT_ACCEPTED = 58;
  public static final int IMS_ACCESS_BLOCKED = 60;
  public static final int IMS_MERGED_SUCCESSFULLY = 45;
  public static final int IMS_SIP_ALTERNATE_EMERGENCY_CALL = 117;
  public static final int INCOMING_CALLS_BARRED_WITHIN_CUG = 73;
  public static final int INCOMING_MISSED = 1;
  public static final int INCOMING_REJECTED = 16;
  public static final int INCOMPATIBLE_DESTINATION = 82;
  public static final int INFORMATION_ELEMENT_NON_EXISTENT = 88;
  public static final int INTERWORKING_UNSPECIFIED = 93;
  public static final int INVALID_CREDENTIALS = 10;
  public static final int INVALID_MANDATORY_INFORMATION = 85;
  public static final int INVALID_NUMBER = 7;
  public static final int INVALID_TRANSACTION_IDENTIFIER = 80;
  public static final int INVALID_TRANSIT_NW_SELECTION = 83;
  public static final int LIMIT_EXCEEDED = 15;
  public static final int LOCAL = 3;
  public static final int LOCAL_LOW_BATTERY = 94;
  public static final int LOST_SIGNAL = 14;
  public static final int LOW_BATTERY = 61;
  public static final int MAXIMUM_NUMBER_OF_CALLS_REACHED = 53;
  public static final int MAXIMUM_VALID_VALUE = 116;
  public static final int MESSAGE_NOT_COMPATIBLE_WITH_PROTOCOL_STATE = 90;
  public static final int MESSAGE_TYPE_NON_IMPLEMENTED = 86;
  public static final int MESSAGE_TYPE_NOT_COMPATIBLE_WITH_PROTOCOL_STATE = 87;
  public static final int MINIMUM_VALID_VALUE = 0;
  public static final int MMI = 6;
  public static final int NETWORK_OUT_OF_ORDER = 109;
  public static final int NON_SELECTED_USER_CLEARING = 116;
  public static final int NORMAL = 2;
  public static final int NORMAL_UNSPECIFIED = 65;
  public static final int NOT_DISCONNECTED = 0;
  public static final int NOT_VALID = -1;
  public static final int NO_CIRCUIT_AVAIL = 96;
  public static final int NO_PHONE_NUMBER_SUPPLIED = 38;
  public static final int NO_ROUTE_TO_DESTINATION = 97;
  public static final int NUMBER_CHANGED = 105;
  public static final int NUMBER_UNREACHABLE = 8;
  public static final int ONLY_DIGITAL_INFORMATION_BEARER_AVAILABLE = 78;
  public static final int OPERATOR_DETERMINED_BARRING = 98;
  public static final int OUTGOING_CANCELED = 44;
  public static final int OUTGOING_FAILURE = 43;
  public static final int OUT_OF_NETWORK = 11;
  public static final int OUT_OF_SERVICE = 18;
  public static final int POWER_OFF = 17;
  public static final int PREEMPTION = 106;
  public static final int PROTOCOL_ERROR_UNSPECIFIED = 92;
  public static final int QOS_UNAVAILABLE = 71;
  public static final int RECOVERY_ON_TIMER_EXPIRED = 91;
  public static final int REQUESTED_CIRCUIT_OR_CHANNEL_NOT_AVAILABLE = 113;
  public static final int REQUESTED_FACILITY_NOT_IMPLEMENTED = 77;
  public static final int REQUESTED_FACILITY_NOT_SUBSCRIBED = 72;
  public static final int RESOURCES_UNAVAILABLE_OR_UNSPECIFIED = 114;
  public static final int RESP_TO_STATUS_ENQUIRY = 108;
  public static final int SEMANTICALLY_INCORRECT_MESSAGE = 84;
  public static final int SERVER_ERROR = 12;
  public static final int SERVER_UNREACHABLE = 9;
  public static final int SERVICE_OPTION_NOT_AVAILABLE = 75;
  public static final int SERVICE_OR_OPTION_NOT_IMPLEMENTED = 79;
  public static final int SWITCHING_EQUIPMENT_CONGESTION = 111;
  public static final int TEMPORARY_FAILURE = 110;
  public static final int TIMED_OUT = 13;
  public static final int UNOBTAINABLE_NUMBER = 25;
  public static final int USER_NOT_MEMBER_OF_CUG = 81;
  public static final int VIDEO_CALL_NOT_ALLOWED_WHILE_TTY_ENABLED = 50;
  public static final int VOICEMAIL_NUMBER_MISSING = 40;
  public static final int WIFI_LOST = 59;
  
  private DisconnectCause() {}
  
  public static String toString(int paramInt)
  {
    switch (paramInt)
    {
    case 6: 
    case 56: 
    case 95: 
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("INVALID: ");
      localStringBuilder.append(paramInt);
      return localStringBuilder.toString();
    case 117: 
      return "IMS_SIP_ALTERNATE_EMERGENCY_CALL";
    case 116: 
      return "NON_SELECTED_USER_CLEARING";
    case 115: 
      return "HO_NOT_FEASIBLE";
    case 114: 
      return "RESOURCES_UNAVAILABLE_OR_UNSPECIFIED";
    case 113: 
      return "REQUESTED_CIRCUIT_OR_CHANNEL_NOT_AVAILABLE";
    case 112: 
      return "ACCESS_INFORMATION_DISCARDED";
    case 111: 
      return "SWITCHING_EQUIPMENT_CONGESTION";
    case 110: 
      return "TEMPORARY_FAILURE";
    case 109: 
      return "NETWORK_OUT_OF_ORDER";
    case 108: 
      return "RESP_TO_STATUS_ENQUIRY";
    case 107: 
      return "FACILITY_REJECTED";
    case 106: 
      return "PREEMPTION";
    case 105: 
      return "NUMBER_CHANGED";
    case 104: 
      return "CALL_REJECTED";
    case 103: 
      return "CHANNEL_UNACCEPTABLE";
    case 102: 
      return "BEARER_CAPABILITY_NOT_AUTHORIZED";
    case 101: 
      return "CALL_FAIL_DESTINATION_OUT_OF_ORDER";
    case 100: 
      return "CALL_FAIL_NO_ANSWER_FROM_USER";
    case 99: 
      return "CALL_FAIL_NO_USER_RESPONDING";
    case 98: 
      return "OPERATOR_DETERMINED_BARRING";
    case 97: 
      return "NO_ROUTE_TO_DESTINATION";
    case 96: 
      return "NO_CIRCUIT_AVAIL";
    case 94: 
      return "LOCAL_LOW_BATTERY";
    case 93: 
      return "INTERWORKING_UNSPECIFIED";
    case 92: 
      return "PROTOCOL_ERROR_UNSPECIFIED";
    case 91: 
      return "RECOVERY_ON_TIMER_EXPIRED";
    case 90: 
      return "MESSAGE_NOT_COMPATIBLE_WITH_PROTOCOL_STATE";
    case 89: 
      return "CONDITIONAL_IE_ERROR";
    case 88: 
      return "INFORMATION_ELEMENT_NON_EXISTENT";
    case 87: 
      return "MESSAGE_TYPE_NOT_COMPATIBLE_WITH_PROTOCOL_STATE";
    case 86: 
      return "MESSAGE_TYPE_NON_IMPLEMENTED";
    case 85: 
      return "INVALID_MANDATORY_INFORMATION";
    case 84: 
      return "SEMANTICALLY_INCORRECT_MESSAGE";
    case 83: 
      return "INVALID_TRANSIT_NW_SELECTION";
    case 82: 
      return "INCOMPATIBLE_DESTINATION";
    case 81: 
      return "USER_NOT_MEMBER_OF_CUG";
    case 80: 
      return "INVALID_TRANSACTION_IDENTIFIER";
    case 79: 
      return "SERVICE_OR_OPTION_NOT_IMPLEMENTED";
    case 78: 
      return "ONLY_DIGITAL_INFORMATION_BEARER_AVAILABLE";
    case 77: 
      return "REQUESTED_FACILITY_NOT_IMPLEMENTED";
    case 76: 
      return "BEARER_SERVICE_NOT_IMPLEMENTED";
    case 75: 
      return "SERVICE_OPTION_NOT_AVAILABLE";
    case 74: 
      return "BEARER_CAPABILITY_UNAVAILABLE";
    case 73: 
      return "INCOMING_CALLS_BARRED_WITHIN_CUG";
    case 72: 
      return "REQUESTED_FACILITY_NOT_SUBSCRIBED";
    case 71: 
      return "QOS_UNAVAILABLE";
    case 70: 
      return "DIAL_VIDEO_MODIFIED_TO_DIAL_VIDEO";
    case 69: 
      return "DIAL_VIDEO_MODIFIED_TO_DIAL";
    case 68: 
      return "DIAL_VIDEO_MODIFIED_TO_USSD";
    case 67: 
      return "DIAL_VIDEO_MODIFIED_TO_SS";
    case 66: 
      return "DIAL_MODIFIED_TO_DIAL_VIDEO";
    case 65: 
      return "NORMAL_UNSPECIFIED";
    case 64: 
      return "EMERGENCY_PERM_FAILURE";
    case 63: 
      return "EMERGENCY_TEMP_FAILURE";
    case 62: 
      return "DIAL_LOW_BATTERY";
    case 61: 
      return "LOW_BATTERY";
    case 60: 
      return "IMS_ACCESS_BLOCKED";
    case 59: 
      return "WIFI_LOST";
    case 58: 
      return "IMEI_NOT_ACCEPTED";
    case 57: 
      return "DIALED_CALL_FORWARDING_WHILE_ROAMING";
    case 55: 
      return "DATA_LIMIT_REACHED";
    case 54: 
      return "DATA_DISABLED";
    case 53: 
      return "MAXIMUM_NUMER_OF_CALLS_REACHED";
    case 52: 
      return "ANSWERED_ELSEWHERE";
    case 51: 
      return "CALL_PULLED";
    case 50: 
      return "VIDEO_CALL_NOT_ALLOWED_WHILE_TTY_ENABLED";
    case 49: 
      return "CDMA_ALREADY_ACTIVATED";
    case 48: 
      return "DIAL_MODIFIED_TO_DIAL";
    case 47: 
      return "DIAL_MODIFIED_TO_SS";
    case 46: 
      return "DIAL_MODIFIED_TO_USSD";
    case 45: 
      return "IMS_MERGED_SUCCESSFULLY";
    case 44: 
      return "OUTGOING_CANCELED";
    case 43: 
      return "OUTGOING_FAILURE";
    case 42: 
      return "EXITED_ECM";
    case 41: 
      return "CDMA_CALL_LOST";
    case 40: 
      return "VOICEMAIL_NUMBER_MISSING";
    case 39: 
      return "DIALED_MMI";
    case 38: 
      return "NO_PHONE_NUMBER_SUPPLIED";
    case 37: 
      return "EMERGENCY_ONLY";
    case 36: 
      return "ERROR_UNSPECIFIED";
    case 35: 
      return "CDMA_ACCESS_BLOCKED";
    case 34: 
      return "CDMA_NOT_EMERGENCY";
    case 33: 
      return "CDMA_PREEMPTED";
    case 32: 
      return "CDMA_ACCESS_FAILURE";
    case 31: 
      return "CDMA_RETRY_ORDER";
    case 30: 
      return "CDMA_SO_REJECT";
    case 29: 
      return "CDMA_REORDER";
    case 28: 
      return "CDMA_INTERCEPT";
    case 27: 
      return "CDMA_DROP";
    case 26: 
      return "CDMA_LOCKED_UNTIL_POWER_CYCLE";
    case 25: 
      return "UNOBTAINABLE_NUMBER";
    case 24: 
      return "CS_RESTRICTED_EMERGENCY";
    case 23: 
      return "CS_RESTRICTED_NORMAL";
    case 22: 
      return "CS_RESTRICTED";
    case 21: 
      return "FDN_BLOCKED";
    case 20: 
      return "CALL_BARRED";
    case 19: 
      return "ICC_ERROR";
    case 18: 
      return "OUT_OF_SERVICE";
    case 17: 
      return "POWER_OFF";
    case 16: 
      return "INCOMING_REJECTED";
    case 15: 
      return "LIMIT_EXCEEDED";
    case 14: 
      return "LOST_SIGNAL";
    case 13: 
      return "TIMED_OUT";
    case 12: 
      return "SERVER_ERROR";
    case 11: 
      return "OUT_OF_NETWORK";
    case 10: 
      return "INVALID_CREDENTIALS";
    case 9: 
      return "SERVER_UNREACHABLE";
    case 8: 
      return "NUMBER_UNREACHABLE";
    case 7: 
      return "INVALID_NUMBER";
    case 5: 
      return "CONGESTION";
    case 4: 
      return "BUSY";
    case 3: 
      return "LOCAL";
    case 2: 
      return "NORMAL";
    case 1: 
      return "INCOMING_MISSED";
    }
    return "NOT_DISCONNECTED";
  }
}
