package com.android.internal.telephony.dataconnection;

import android.content.Context;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import java.util.HashMap;
import java.util.HashSet;

public enum DcFailCause
{
  private static final HashMap<Integer, DcFailCause> sErrorCodeToFailCauseMap;
  private static final HashMap<Integer, HashSet<DcFailCause>> sPermanentFailureCache = new HashMap();
  private final int mErrorCode;
  
  static
  {
    int i = 0;
    NONE = new DcFailCause("NONE", 0, 0);
    OPERATOR_BARRED = new DcFailCause("OPERATOR_BARRED", 1, 8);
    NAS_SIGNALLING = new DcFailCause("NAS_SIGNALLING", 2, 14);
    LLC_SNDCP = new DcFailCause("LLC_SNDCP", 3, 25);
    INSUFFICIENT_RESOURCES = new DcFailCause("INSUFFICIENT_RESOURCES", 4, 26);
    MISSING_UNKNOWN_APN = new DcFailCause("MISSING_UNKNOWN_APN", 5, 27);
    UNKNOWN_PDP_ADDRESS_TYPE = new DcFailCause("UNKNOWN_PDP_ADDRESS_TYPE", 6, 28);
    USER_AUTHENTICATION = new DcFailCause("USER_AUTHENTICATION", 7, 29);
    ACTIVATION_REJECT_GGSN = new DcFailCause("ACTIVATION_REJECT_GGSN", 8, 30);
    ACTIVATION_REJECT_UNSPECIFIED = new DcFailCause("ACTIVATION_REJECT_UNSPECIFIED", 9, 31);
    SERVICE_OPTION_NOT_SUPPORTED = new DcFailCause("SERVICE_OPTION_NOT_SUPPORTED", 10, 32);
    SERVICE_OPTION_NOT_SUBSCRIBED = new DcFailCause("SERVICE_OPTION_NOT_SUBSCRIBED", 11, 33);
    SERVICE_OPTION_OUT_OF_ORDER = new DcFailCause("SERVICE_OPTION_OUT_OF_ORDER", 12, 34);
    NSAPI_IN_USE = new DcFailCause("NSAPI_IN_USE", 13, 35);
    REGULAR_DEACTIVATION = new DcFailCause("REGULAR_DEACTIVATION", 14, 36);
    QOS_NOT_ACCEPTED = new DcFailCause("QOS_NOT_ACCEPTED", 15, 37);
    NETWORK_FAILURE = new DcFailCause("NETWORK_FAILURE", 16, 38);
    UMTS_REACTIVATION_REQ = new DcFailCause("UMTS_REACTIVATION_REQ", 17, 39);
    FEATURE_NOT_SUPP = new DcFailCause("FEATURE_NOT_SUPP", 18, 40);
    TFT_SEMANTIC_ERROR = new DcFailCause("TFT_SEMANTIC_ERROR", 19, 41);
    TFT_SYTAX_ERROR = new DcFailCause("TFT_SYTAX_ERROR", 20, 42);
    UNKNOWN_PDP_CONTEXT = new DcFailCause("UNKNOWN_PDP_CONTEXT", 21, 43);
    FILTER_SEMANTIC_ERROR = new DcFailCause("FILTER_SEMANTIC_ERROR", 22, 44);
    FILTER_SYTAX_ERROR = new DcFailCause("FILTER_SYTAX_ERROR", 23, 45);
    PDP_WITHOUT_ACTIVE_TFT = new DcFailCause("PDP_WITHOUT_ACTIVE_TFT", 24, 46);
    ONLY_IPV4_ALLOWED = new DcFailCause("ONLY_IPV4_ALLOWED", 25, 50);
    ONLY_IPV6_ALLOWED = new DcFailCause("ONLY_IPV6_ALLOWED", 26, 51);
    ONLY_SINGLE_BEARER_ALLOWED = new DcFailCause("ONLY_SINGLE_BEARER_ALLOWED", 27, 52);
    ESM_INFO_NOT_RECEIVED = new DcFailCause("ESM_INFO_NOT_RECEIVED", 28, 53);
    PDN_CONN_DOES_NOT_EXIST = new DcFailCause("PDN_CONN_DOES_NOT_EXIST", 29, 54);
    MULTI_CONN_TO_SAME_PDN_NOT_ALLOWED = new DcFailCause("MULTI_CONN_TO_SAME_PDN_NOT_ALLOWED", 30, 55);
    MAX_ACTIVE_PDP_CONTEXT_REACHED = new DcFailCause("MAX_ACTIVE_PDP_CONTEXT_REACHED", 31, 65);
    UNSUPPORTED_APN_IN_CURRENT_PLMN = new DcFailCause("UNSUPPORTED_APN_IN_CURRENT_PLMN", 32, 66);
    INVALID_TRANSACTION_ID = new DcFailCause("INVALID_TRANSACTION_ID", 33, 81);
    MESSAGE_INCORRECT_SEMANTIC = new DcFailCause("MESSAGE_INCORRECT_SEMANTIC", 34, 95);
    INVALID_MANDATORY_INFO = new DcFailCause("INVALID_MANDATORY_INFO", 35, 96);
    MESSAGE_TYPE_UNSUPPORTED = new DcFailCause("MESSAGE_TYPE_UNSUPPORTED", 36, 97);
    MSG_TYPE_NONCOMPATIBLE_STATE = new DcFailCause("MSG_TYPE_NONCOMPATIBLE_STATE", 37, 98);
    UNKNOWN_INFO_ELEMENT = new DcFailCause("UNKNOWN_INFO_ELEMENT", 38, 99);
    CONDITIONAL_IE_ERROR = new DcFailCause("CONDITIONAL_IE_ERROR", 39, 100);
    MSG_AND_PROTOCOL_STATE_UNCOMPATIBLE = new DcFailCause("MSG_AND_PROTOCOL_STATE_UNCOMPATIBLE", 40, 101);
    PROTOCOL_ERRORS = new DcFailCause("PROTOCOL_ERRORS", 41, 111);
    APN_TYPE_CONFLICT = new DcFailCause("APN_TYPE_CONFLICT", 42, 112);
    INVALID_PCSCF_ADDR = new DcFailCause("INVALID_PCSCF_ADDR", 43, 113);
    INTERNAL_CALL_PREEMPT_BY_HIGH_PRIO_APN = new DcFailCause("INTERNAL_CALL_PREEMPT_BY_HIGH_PRIO_APN", 44, 114);
    EMM_ACCESS_BARRED = new DcFailCause("EMM_ACCESS_BARRED", 45, 115);
    EMERGENCY_IFACE_ONLY = new DcFailCause("EMERGENCY_IFACE_ONLY", 46, 116);
    IFACE_MISMATCH = new DcFailCause("IFACE_MISMATCH", 47, 117);
    COMPANION_IFACE_IN_USE = new DcFailCause("COMPANION_IFACE_IN_USE", 48, 118);
    IP_ADDRESS_MISMATCH = new DcFailCause("IP_ADDRESS_MISMATCH", 49, 119);
    IFACE_AND_POL_FAMILY_MISMATCH = new DcFailCause("IFACE_AND_POL_FAMILY_MISMATCH", 50, 120);
    EMM_ACCESS_BARRED_INFINITE_RETRY = new DcFailCause("EMM_ACCESS_BARRED_INFINITE_RETRY", 51, 121);
    AUTH_FAILURE_ON_EMERGENCY_CALL = new DcFailCause("AUTH_FAILURE_ON_EMERGENCY_CALL", 52, 122);
    OEM_DCFAILCAUSE_1 = new DcFailCause("OEM_DCFAILCAUSE_1", 53, 4097);
    OEM_DCFAILCAUSE_2 = new DcFailCause("OEM_DCFAILCAUSE_2", 54, 4098);
    OEM_DCFAILCAUSE_3 = new DcFailCause("OEM_DCFAILCAUSE_3", 55, 4099);
    OEM_DCFAILCAUSE_4 = new DcFailCause("OEM_DCFAILCAUSE_4", 56, 4100);
    OEM_DCFAILCAUSE_5 = new DcFailCause("OEM_DCFAILCAUSE_5", 57, 4101);
    OEM_DCFAILCAUSE_6 = new DcFailCause("OEM_DCFAILCAUSE_6", 58, 4102);
    OEM_DCFAILCAUSE_7 = new DcFailCause("OEM_DCFAILCAUSE_7", 59, 4103);
    OEM_DCFAILCAUSE_8 = new DcFailCause("OEM_DCFAILCAUSE_8", 60, 4104);
    OEM_DCFAILCAUSE_9 = new DcFailCause("OEM_DCFAILCAUSE_9", 61, 4105);
    OEM_DCFAILCAUSE_10 = new DcFailCause("OEM_DCFAILCAUSE_10", 62, 4106);
    OEM_DCFAILCAUSE_11 = new DcFailCause("OEM_DCFAILCAUSE_11", 63, 4107);
    OEM_DCFAILCAUSE_12 = new DcFailCause("OEM_DCFAILCAUSE_12", 64, 4108);
    OEM_DCFAILCAUSE_13 = new DcFailCause("OEM_DCFAILCAUSE_13", 65, 4109);
    OEM_DCFAILCAUSE_14 = new DcFailCause("OEM_DCFAILCAUSE_14", 66, 4110);
    OEM_DCFAILCAUSE_15 = new DcFailCause("OEM_DCFAILCAUSE_15", 67, 4111);
    REGISTRATION_FAIL = new DcFailCause("REGISTRATION_FAIL", 68, -1);
    GPRS_REGISTRATION_FAIL = new DcFailCause("GPRS_REGISTRATION_FAIL", 69, -2);
    SIGNAL_LOST = new DcFailCause("SIGNAL_LOST", 70, -3);
    PREF_RADIO_TECH_CHANGED = new DcFailCause("PREF_RADIO_TECH_CHANGED", 71, -4);
    RADIO_POWER_OFF = new DcFailCause("RADIO_POWER_OFF", 72, -5);
    TETHERED_CALL_ACTIVE = new DcFailCause("TETHERED_CALL_ACTIVE", 73, -6);
    ERROR_UNSPECIFIED = new DcFailCause("ERROR_UNSPECIFIED", 74, 65535);
    UNKNOWN = new DcFailCause("UNKNOWN", 75, 65536);
    RADIO_NOT_AVAILABLE = new DcFailCause("RADIO_NOT_AVAILABLE", 76, 65537);
    UNACCEPTABLE_NETWORK_PARAMETER = new DcFailCause("UNACCEPTABLE_NETWORK_PARAMETER", 77, 65538);
    CONNECTION_TO_DATACONNECTIONAC_BROKEN = new DcFailCause("CONNECTION_TO_DATACONNECTIONAC_BROKEN", 78, 65539);
    LOST_CONNECTION = new DcFailCause("LOST_CONNECTION", 79, 65540);
    RESET_BY_FRAMEWORK = new DcFailCause("RESET_BY_FRAMEWORK", 80, 65541);
    $VALUES = new DcFailCause[] { NONE, OPERATOR_BARRED, NAS_SIGNALLING, LLC_SNDCP, INSUFFICIENT_RESOURCES, MISSING_UNKNOWN_APN, UNKNOWN_PDP_ADDRESS_TYPE, USER_AUTHENTICATION, ACTIVATION_REJECT_GGSN, ACTIVATION_REJECT_UNSPECIFIED, SERVICE_OPTION_NOT_SUPPORTED, SERVICE_OPTION_NOT_SUBSCRIBED, SERVICE_OPTION_OUT_OF_ORDER, NSAPI_IN_USE, REGULAR_DEACTIVATION, QOS_NOT_ACCEPTED, NETWORK_FAILURE, UMTS_REACTIVATION_REQ, FEATURE_NOT_SUPP, TFT_SEMANTIC_ERROR, TFT_SYTAX_ERROR, UNKNOWN_PDP_CONTEXT, FILTER_SEMANTIC_ERROR, FILTER_SYTAX_ERROR, PDP_WITHOUT_ACTIVE_TFT, ONLY_IPV4_ALLOWED, ONLY_IPV6_ALLOWED, ONLY_SINGLE_BEARER_ALLOWED, ESM_INFO_NOT_RECEIVED, PDN_CONN_DOES_NOT_EXIST, MULTI_CONN_TO_SAME_PDN_NOT_ALLOWED, MAX_ACTIVE_PDP_CONTEXT_REACHED, UNSUPPORTED_APN_IN_CURRENT_PLMN, INVALID_TRANSACTION_ID, MESSAGE_INCORRECT_SEMANTIC, INVALID_MANDATORY_INFO, MESSAGE_TYPE_UNSUPPORTED, MSG_TYPE_NONCOMPATIBLE_STATE, UNKNOWN_INFO_ELEMENT, CONDITIONAL_IE_ERROR, MSG_AND_PROTOCOL_STATE_UNCOMPATIBLE, PROTOCOL_ERRORS, APN_TYPE_CONFLICT, INVALID_PCSCF_ADDR, INTERNAL_CALL_PREEMPT_BY_HIGH_PRIO_APN, EMM_ACCESS_BARRED, EMERGENCY_IFACE_ONLY, IFACE_MISMATCH, COMPANION_IFACE_IN_USE, IP_ADDRESS_MISMATCH, IFACE_AND_POL_FAMILY_MISMATCH, EMM_ACCESS_BARRED_INFINITE_RETRY, AUTH_FAILURE_ON_EMERGENCY_CALL, OEM_DCFAILCAUSE_1, OEM_DCFAILCAUSE_2, OEM_DCFAILCAUSE_3, OEM_DCFAILCAUSE_4, OEM_DCFAILCAUSE_5, OEM_DCFAILCAUSE_6, OEM_DCFAILCAUSE_7, OEM_DCFAILCAUSE_8, OEM_DCFAILCAUSE_9, OEM_DCFAILCAUSE_10, OEM_DCFAILCAUSE_11, OEM_DCFAILCAUSE_12, OEM_DCFAILCAUSE_13, OEM_DCFAILCAUSE_14, OEM_DCFAILCAUSE_15, REGISTRATION_FAIL, GPRS_REGISTRATION_FAIL, SIGNAL_LOST, PREF_RADIO_TECH_CHANGED, RADIO_POWER_OFF, TETHERED_CALL_ACTIVE, ERROR_UNSPECIFIED, UNKNOWN, RADIO_NOT_AVAILABLE, UNACCEPTABLE_NETWORK_PARAMETER, CONNECTION_TO_DATACONNECTIONAC_BROKEN, LOST_CONNECTION, RESET_BY_FRAMEWORK };
    sErrorCodeToFailCauseMap = new HashMap();
    DcFailCause[] arrayOfDcFailCause = values();
    int j = arrayOfDcFailCause.length;
    while (i < j)
    {
      DcFailCause localDcFailCause = arrayOfDcFailCause[i];
      sErrorCodeToFailCauseMap.put(Integer.valueOf(localDcFailCause.getErrorCode()), localDcFailCause);
      i++;
    }
  }
  
  private DcFailCause(int paramInt)
  {
    mErrorCode = paramInt;
  }
  
  public static DcFailCause fromInt(int paramInt)
  {
    DcFailCause localDcFailCause1 = (DcFailCause)sErrorCodeToFailCauseMap.get(Integer.valueOf(paramInt));
    DcFailCause localDcFailCause2 = localDcFailCause1;
    if (localDcFailCause1 == null) {
      localDcFailCause2 = UNKNOWN;
    }
    return localDcFailCause2;
  }
  
  public int getErrorCode()
  {
    return mErrorCode;
  }
  
  public boolean isEventLoggable()
  {
    boolean bool;
    if ((this != OPERATOR_BARRED) && (this != INSUFFICIENT_RESOURCES) && (this != UNKNOWN_PDP_ADDRESS_TYPE) && (this != USER_AUTHENTICATION) && (this != ACTIVATION_REJECT_GGSN) && (this != ACTIVATION_REJECT_UNSPECIFIED) && (this != SERVICE_OPTION_NOT_SUBSCRIBED) && (this != SERVICE_OPTION_NOT_SUPPORTED) && (this != SERVICE_OPTION_OUT_OF_ORDER) && (this != NSAPI_IN_USE) && (this != ONLY_IPV4_ALLOWED) && (this != ONLY_IPV6_ALLOWED) && (this != PROTOCOL_ERRORS) && (this != SIGNAL_LOST) && (this != RADIO_POWER_OFF) && (this != TETHERED_CALL_ACTIVE) && (this != UNACCEPTABLE_NETWORK_PARAMETER)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isPermanentFailure(Context paramContext, int paramInt)
  {
    synchronized (sPermanentFailureCache)
    {
      Object localObject1 = (HashSet)sPermanentFailureCache.get(Integer.valueOf(paramInt));
      Object localObject2 = localObject1;
      if (localObject1 == null)
      {
        localObject2 = (CarrierConfigManager)paramContext.getSystemService("carrier_config");
        paramContext = (Context)localObject1;
        if (localObject2 != null)
        {
          localObject2 = ((CarrierConfigManager)localObject2).getConfigForSubId(paramInt);
          paramContext = (Context)localObject1;
          if (localObject2 != null)
          {
            localObject2 = ((PersistableBundle)localObject2).getStringArray("carrier_data_call_permanent_failure_strings");
            paramContext = (Context)localObject1;
            if (localObject2 != null)
            {
              localObject1 = new java/util/HashSet;
              ((HashSet)localObject1).<init>();
              int i = localObject2.length;
              for (int j = 0;; j++)
              {
                paramContext = (Context)localObject1;
                if (j >= i) {
                  break;
                }
                ((HashSet)localObject1).add(valueOf(localObject2[j]));
              }
            }
          }
        }
        localObject1 = paramContext;
        if (paramContext == null)
        {
          localObject1 = new com/android/internal/telephony/dataconnection/DcFailCause$1;
          ((1)localObject1).<init>(this);
        }
        sPermanentFailureCache.put(Integer.valueOf(paramInt), localObject1);
        localObject2 = localObject1;
      }
      boolean bool = ((HashSet)localObject2).contains(this);
      return bool;
    }
  }
  
  public boolean isRestartRadioFail(Context paramContext, int paramInt)
  {
    if (this == REGULAR_DEACTIVATION)
    {
      paramContext = (CarrierConfigManager)paramContext.getSystemService("carrier_config");
      if (paramContext != null)
      {
        paramContext = paramContext.getConfigForSubId(paramInt);
        if (paramContext != null) {
          return paramContext.getBoolean("restart_radio_on_pdp_fail_regular_deactivation_bool");
        }
      }
    }
    return false;
  }
}
