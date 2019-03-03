package com.android.internal.telephony;

import android.util.EventLog;

public class EventLogTags
{
  public static final int BAD_IP_ADDRESS = 50117;
  public static final int CALL_DROP = 50106;
  public static final int CDMA_DATA_DROP = 50111;
  public static final int CDMA_DATA_SETUP_FAILED = 50110;
  public static final int CDMA_DATA_STATE_CHANGE = 50115;
  public static final int CDMA_SERVICE_STATE_CHANGE = 50116;
  public static final int DATA_NETWORK_REGISTRATION_FAIL = 50107;
  public static final int DATA_NETWORK_STATUS_ON_RADIO_OFF = 50108;
  public static final int DATA_STALL_RECOVERY_CLEANUP = 50119;
  public static final int DATA_STALL_RECOVERY_GET_DATA_CALL_LIST = 50118;
  public static final int DATA_STALL_RECOVERY_RADIO_RESTART = 50121;
  public static final int DATA_STALL_RECOVERY_RADIO_RESTART_WITH_PROP = 50122;
  public static final int DATA_STALL_RECOVERY_REREGISTER = 50120;
  public static final int EXP_DET_SMS_DENIED_BY_USER = 50125;
  public static final int EXP_DET_SMS_SENT_BY_USER = 50128;
  public static final int GSM_DATA_STATE_CHANGE = 50113;
  public static final int GSM_RAT_SWITCHED = 50112;
  public static final int GSM_RAT_SWITCHED_NEW = 50123;
  public static final int GSM_SERVICE_STATE_CHANGE = 50114;
  public static final int PDP_BAD_DNS_ADDRESS = 50100;
  public static final int PDP_CONTEXT_RESET = 50103;
  public static final int PDP_NETWORK_DROP = 50109;
  public static final int PDP_RADIO_RESET = 50102;
  public static final int PDP_RADIO_RESET_COUNTDOWN_TRIGGERED = 50101;
  public static final int PDP_REREGISTER_NETWORK = 50104;
  public static final int PDP_SETUP_FAIL = 50105;
  
  private EventLogTags() {}
  
  public static void writeBadIpAddress(String paramString)
  {
    EventLog.writeEvent(50117, paramString);
  }
  
  public static void writeCallDrop(int paramInt1, int paramInt2, int paramInt3)
  {
    EventLog.writeEvent(50106, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3) });
  }
  
  public static void writeCdmaDataDrop(int paramInt1, int paramInt2)
  {
    EventLog.writeEvent(50111, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
  }
  
  public static void writeCdmaDataSetupFailed(int paramInt1, int paramInt2, int paramInt3)
  {
    EventLog.writeEvent(50110, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3) });
  }
  
  public static void writeCdmaDataStateChange(String paramString1, String paramString2)
  {
    EventLog.writeEvent(50115, new Object[] { paramString1, paramString2 });
  }
  
  public static void writeCdmaServiceStateChange(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    EventLog.writeEvent(50116, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3), Integer.valueOf(paramInt4) });
  }
  
  public static void writeDataNetworkRegistrationFail(int paramInt1, int paramInt2)
  {
    EventLog.writeEvent(50107, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
  }
  
  public static void writeDataNetworkStatusOnRadioOff(String paramString, int paramInt)
  {
    EventLog.writeEvent(50108, new Object[] { paramString, Integer.valueOf(paramInt) });
  }
  
  public static void writeDataStallRecoveryCleanup(int paramInt)
  {
    EventLog.writeEvent(50119, paramInt);
  }
  
  public static void writeDataStallRecoveryGetDataCallList(int paramInt)
  {
    EventLog.writeEvent(50118, paramInt);
  }
  
  public static void writeDataStallRecoveryRadioRestart(int paramInt)
  {
    EventLog.writeEvent(50121, paramInt);
  }
  
  public static void writeDataStallRecoveryRadioRestartWithProp(int paramInt)
  {
    EventLog.writeEvent(50122, paramInt);
  }
  
  public static void writeDataStallRecoveryReregister(int paramInt)
  {
    EventLog.writeEvent(50120, paramInt);
  }
  
  public static void writeExpDetSmsDeniedByUser(String paramString)
  {
    EventLog.writeEvent(50125, paramString);
  }
  
  public static void writeExpDetSmsSentByUser(String paramString)
  {
    EventLog.writeEvent(50128, paramString);
  }
  
  public static void writeGsmDataStateChange(String paramString1, String paramString2)
  {
    EventLog.writeEvent(50113, new Object[] { paramString1, paramString2 });
  }
  
  public static void writeGsmRatSwitched(int paramInt1, int paramInt2, int paramInt3)
  {
    EventLog.writeEvent(50112, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3) });
  }
  
  public static void writeGsmRatSwitchedNew(int paramInt1, int paramInt2, int paramInt3)
  {
    EventLog.writeEvent(50123, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3) });
  }
  
  public static void writeGsmServiceStateChange(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    EventLog.writeEvent(50114, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3), Integer.valueOf(paramInt4) });
  }
  
  public static void writePdpBadDnsAddress(String paramString)
  {
    EventLog.writeEvent(50100, paramString);
  }
  
  public static void writePdpContextReset(int paramInt)
  {
    EventLog.writeEvent(50103, paramInt);
  }
  
  public static void writePdpNetworkDrop(int paramInt1, int paramInt2)
  {
    EventLog.writeEvent(50109, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
  }
  
  public static void writePdpRadioReset(int paramInt)
  {
    EventLog.writeEvent(50102, paramInt);
  }
  
  public static void writePdpRadioResetCountdownTriggered(int paramInt)
  {
    EventLog.writeEvent(50101, paramInt);
  }
  
  public static void writePdpReregisterNetwork(int paramInt)
  {
    EventLog.writeEvent(50104, paramInt);
  }
  
  public static void writePdpSetupFail(int paramInt1, int paramInt2, int paramInt3)
  {
    EventLog.writeEvent(50105, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3) });
  }
}
