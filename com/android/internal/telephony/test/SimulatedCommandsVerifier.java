package com.android.internal.telephony.test;

import android.net.KeepalivePacketData;
import android.net.LinkProperties;
import android.os.Handler;
import android.os.Message;
import android.service.carrier.CarrierIdentifier;
import android.telephony.ImsiEncryptionInfo;
import android.telephony.NetworkScanRequest;
import android.telephony.data.DataProfile;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.CommandsInterface.RadioState;
import com.android.internal.telephony.RadioCapability;
import com.android.internal.telephony.UUSInfo;
import com.android.internal.telephony.cdma.CdmaSmsBroadcastConfigInfo;
import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import java.util.List;

public class SimulatedCommandsVerifier
  implements CommandsInterface
{
  private static SimulatedCommandsVerifier sInstance;
  
  private SimulatedCommandsVerifier() {}
  
  public static SimulatedCommandsVerifier getInstance()
  {
    if (sInstance == null) {
      sInstance = new SimulatedCommandsVerifier();
    }
    return sInstance;
  }
  
  public void acceptCall(Message paramMessage) {}
  
  public void acknowledgeIncomingGsmSmsWithPdu(boolean paramBoolean, String paramString, Message paramMessage) {}
  
  public void acknowledgeLastIncomingCdmaSms(boolean paramBoolean, int paramInt, Message paramMessage) {}
  
  public void acknowledgeLastIncomingGsmSms(boolean paramBoolean, int paramInt, Message paramMessage) {}
  
  public void cancelPendingUssd(Message paramMessage) {}
  
  public void changeBarringPassword(String paramString1, String paramString2, String paramString3, Message paramMessage) {}
  
  public void changeIccPin(String paramString1, String paramString2, Message paramMessage) {}
  
  public void changeIccPin2(String paramString1, String paramString2, Message paramMessage) {}
  
  public void changeIccPin2ForApp(String paramString1, String paramString2, String paramString3, Message paramMessage) {}
  
  public void changeIccPinForApp(String paramString1, String paramString2, String paramString3, Message paramMessage) {}
  
  public void conference(Message paramMessage) {}
  
  public void deactivateDataCall(int paramInt1, int paramInt2, Message paramMessage) {}
  
  public void deleteSmsOnRuim(int paramInt, Message paramMessage) {}
  
  public void deleteSmsOnSim(int paramInt, Message paramMessage) {}
  
  public void dial(String paramString, int paramInt, Message paramMessage) {}
  
  public void dial(String paramString, int paramInt, UUSInfo paramUUSInfo, Message paramMessage) {}
  
  public void exitEmergencyCallbackMode(Message paramMessage) {}
  
  public void explicitCallTransfer(Message paramMessage) {}
  
  public void getAllowedCarriers(Message paramMessage) {}
  
  public void getAvailableNetworks(Message paramMessage) {}
  
  public void getBasebandVersion(Message paramMessage) {}
  
  public void getCDMASubscription(Message paramMessage) {}
  
  public void getCLIR(Message paramMessage) {}
  
  public void getCdmaBroadcastConfig(Message paramMessage) {}
  
  public void getCdmaSubscriptionSource(Message paramMessage) {}
  
  public void getCurrentCalls(Message paramMessage) {}
  
  public void getDataCallList(Message paramMessage) {}
  
  public void getDataRegistrationState(Message paramMessage) {}
  
  public void getDeviceIdentity(Message paramMessage) {}
  
  public void getGsmBroadcastConfig(Message paramMessage) {}
  
  public void getHardwareConfig(Message paramMessage) {}
  
  public void getIMEI(Message paramMessage) {}
  
  public void getIMEISV(Message paramMessage) {}
  
  public void getIMSI(Message paramMessage) {}
  
  public void getIMSIForApp(String paramString, Message paramMessage) {}
  
  public void getIccCardStatus(Message paramMessage) {}
  
  public void getIccSlotsStatus(Message paramMessage) {}
  
  public void getImsRegistrationState(Message paramMessage) {}
  
  public void getLastCallFailCause(Message paramMessage) {}
  
  public void getLastDataCallFailCause(Message paramMessage) {}
  
  public void getLastPdpFailCause(Message paramMessage) {}
  
  public int getLteOnCdmaMode()
  {
    return 0;
  }
  
  public void getModemActivityInfo(Message paramMessage) {}
  
  public void getMute(Message paramMessage) {}
  
  public void getNetworkSelectionMode(Message paramMessage) {}
  
  public void getOperator(Message paramMessage) {}
  
  public void getPDPContextList(Message paramMessage) {}
  
  public void getPreferredNetworkType(Message paramMessage) {}
  
  public void getPreferredVoicePrivacy(Message paramMessage) {}
  
  public void getRadioCapability(Message paramMessage) {}
  
  public CommandsInterface.RadioState getRadioState()
  {
    return null;
  }
  
  public int getRilVersion()
  {
    return 0;
  }
  
  public void getSignalStrength(Message paramMessage) {}
  
  public void getSmscAddress(Message paramMessage) {}
  
  public void getVoiceRadioTechnology(Message paramMessage) {}
  
  public void getVoiceRegistrationState(Message paramMessage) {}
  
  public void handleCallSetupRequestFromSim(boolean paramBoolean, Message paramMessage) {}
  
  public void hangupConnection(int paramInt, Message paramMessage) {}
  
  public void hangupForegroundResumeBackground(Message paramMessage) {}
  
  public void hangupWaitingOrBackground(Message paramMessage) {}
  
  public void iccCloseLogicalChannel(int paramInt, Message paramMessage) {}
  
  public void iccIO(int paramInt1, int paramInt2, String paramString1, int paramInt3, int paramInt4, int paramInt5, String paramString2, String paramString3, Message paramMessage) {}
  
  public void iccIOForApp(int paramInt1, int paramInt2, String paramString1, int paramInt3, int paramInt4, int paramInt5, String paramString2, String paramString3, String paramString4, Message paramMessage) {}
  
  public void iccOpenLogicalChannel(String paramString, int paramInt, Message paramMessage) {}
  
  public void iccTransmitApduBasicChannel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, String paramString, Message paramMessage) {}
  
  public void iccTransmitApduLogicalChannel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, String paramString, Message paramMessage) {}
  
  public void invokeOemRilRequestRaw(byte[] paramArrayOfByte, Message paramMessage) {}
  
  public void invokeOemRilRequestStrings(String[] paramArrayOfString, Message paramMessage) {}
  
  public void logSignal(Object paramObject) {}
  
  public void nvReadItem(int paramInt, Message paramMessage) {}
  
  public void nvResetConfig(int paramInt, Message paramMessage) {}
  
  public void nvWriteCdmaPrl(byte[] paramArrayOfByte, Message paramMessage) {}
  
  public void nvWriteItem(int paramInt, String paramString, Message paramMessage) {}
  
  public void pullLceData(Message paramMessage) {}
  
  public void queryAvailableBandMode(Message paramMessage) {}
  
  public void queryCLIP(Message paramMessage) {}
  
  public void queryCallForwardStatus(int paramInt1, int paramInt2, String paramString, Message paramMessage) {}
  
  public void queryCallWaiting(int paramInt, Message paramMessage) {}
  
  public void queryCdmaRoamingPreference(Message paramMessage) {}
  
  public void queryFacilityLock(String paramString1, String paramString2, int paramInt, Message paramMessage) {}
  
  public void queryFacilityLockForApp(String paramString1, String paramString2, int paramInt, String paramString3, Message paramMessage) {}
  
  public void queryTTYMode(Message paramMessage) {}
  
  public void registerFoT53ClirlInfo(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForAvailable(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForCallStateChanged(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForCallWaitingInfo(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForCarrierInfoForImsiEncryption(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForCdmaOtaProvision(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForCdmaPrlChanged(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForCdmaSubscriptionChanged(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForCellInfoList(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForDataCallListChanged(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForDisplayInfo(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForExitEmergencyCallbackMode(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForHardwareConfigChanged(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForIccRefresh(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForIccSlotStatusChanged(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForIccStatusChanged(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForImsNetworkStateChanged(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForInCallVoicePrivacyOff(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForInCallVoicePrivacyOn(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForLceInfo(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForLineControlInfo(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForModemReset(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForNattKeepaliveStatus(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForNetworkScanResult(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForNetworkStateChanged(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForNotAvailable(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForNumberInfo(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForOffOrNotAvailable(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForOn(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForPcoData(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForPhysicalChannelConfiguration(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForRadioCapabilityChanged(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForRadioStateChanged(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForRedirectedNumberInfo(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForResendIncallMute(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForRilConnected(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForRingbackTone(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForSignalInfo(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForSrvccStateChanged(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForSubscriptionStatusChanged(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForT53AudioControlInfo(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForVoiceRadioTechChanged(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void rejectCall(Message paramMessage) {}
  
  public void reportSmsMemoryStatus(boolean paramBoolean, Message paramMessage) {}
  
  public void reportStkServiceIsRunning(Message paramMessage) {}
  
  public void requestIccSimAuthentication(int paramInt, String paramString1, String paramString2, Message paramMessage) {}
  
  public void requestShutdown(Message paramMessage) {}
  
  public void resetRadio(Message paramMessage) {}
  
  public void sendBurstDtmf(String paramString, int paramInt1, int paramInt2, Message paramMessage) {}
  
  public void sendCDMAFeatureCode(String paramString, Message paramMessage) {}
  
  public void sendCdmaSms(byte[] paramArrayOfByte, Message paramMessage) {}
  
  public void sendCdmaSms(byte[] paramArrayOfByte, Message paramMessage, boolean paramBoolean) {}
  
  public void sendDeviceState(int paramInt, boolean paramBoolean, Message paramMessage) {}
  
  public void sendDtmf(char paramChar, Message paramMessage) {}
  
  public void sendEnvelope(String paramString, Message paramMessage) {}
  
  public void sendEnvelopeWithStatus(String paramString, Message paramMessage) {}
  
  public void sendImsCdmaSms(byte[] paramArrayOfByte, int paramInt1, int paramInt2, Message paramMessage) {}
  
  public void sendImsGsmSms(String paramString1, String paramString2, int paramInt1, int paramInt2, Message paramMessage) {}
  
  public void sendSMS(String paramString1, String paramString2, Message paramMessage) {}
  
  public void sendSMSExpectMore(String paramString1, String paramString2, Message paramMessage) {}
  
  public void sendTerminalResponse(String paramString, Message paramMessage) {}
  
  public void sendUSSD(String paramString, Message paramMessage) {}
  
  public void separateConnection(int paramInt, Message paramMessage) {}
  
  public void setAllowedCarriers(List<CarrierIdentifier> paramList, Message paramMessage) {}
  
  public void setBandMode(int paramInt, Message paramMessage) {}
  
  public void setCLIR(int paramInt, Message paramMessage) {}
  
  public void setCallForward(int paramInt1, int paramInt2, int paramInt3, String paramString, int paramInt4, Message paramMessage) {}
  
  public void setCallWaiting(boolean paramBoolean, int paramInt, Message paramMessage) {}
  
  public void setCarrierInfoForImsiEncryption(ImsiEncryptionInfo paramImsiEncryptionInfo, Message paramMessage) {}
  
  public void setCdmaBroadcastActivation(boolean paramBoolean, Message paramMessage) {}
  
  public void setCdmaBroadcastConfig(CdmaSmsBroadcastConfigInfo[] paramArrayOfCdmaSmsBroadcastConfigInfo, Message paramMessage) {}
  
  public void setCdmaRoamingPreference(int paramInt, Message paramMessage) {}
  
  public void setCdmaSubscriptionSource(int paramInt, Message paramMessage) {}
  
  public void setDataAllowed(boolean paramBoolean, Message paramMessage) {}
  
  public void setDataProfile(DataProfile[] paramArrayOfDataProfile, boolean paramBoolean, Message paramMessage) {}
  
  public void setEmergencyCallbackMode(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void setFacilityLock(String paramString1, boolean paramBoolean, String paramString2, int paramInt, Message paramMessage) {}
  
  public void setFacilityLockForApp(String paramString1, boolean paramBoolean, String paramString2, int paramInt, String paramString3, Message paramMessage) {}
  
  public void setGsmBroadcastActivation(boolean paramBoolean, Message paramMessage) {}
  
  public void setGsmBroadcastConfig(SmsBroadcastConfigInfo[] paramArrayOfSmsBroadcastConfigInfo, Message paramMessage) {}
  
  public void setInitialAttachApn(DataProfile paramDataProfile, boolean paramBoolean, Message paramMessage) {}
  
  public void setLinkCapacityReportingCriteria(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt4, Message paramMessage) {}
  
  public void setLocationUpdates(boolean paramBoolean, Message paramMessage) {}
  
  public void setLogicalToPhysicalSlotMapping(int[] paramArrayOfInt, Message paramMessage) {}
  
  public void setMute(boolean paramBoolean, Message paramMessage) {}
  
  public void setNetworkSelectionModeAutomatic(Message paramMessage) {}
  
  public void setNetworkSelectionModeManual(String paramString, Message paramMessage) {}
  
  public void setOnCallRing(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void setOnCatCallSetUp(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void setOnCatCcAlphaNotify(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void setOnCatEvent(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void setOnCatProactiveCmd(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void setOnCatSessionEnd(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void setOnIccRefresh(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void setOnIccSmsFull(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void setOnNITZTime(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void setOnNewCdmaSms(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void setOnNewGsmBroadcastSms(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void setOnNewGsmSms(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void setOnRestrictedStateChanged(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void setOnSignalStrengthUpdate(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void setOnSmsOnSim(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void setOnSmsStatus(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void setOnSs(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void setOnSuppServiceNotification(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void setOnUSSD(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void setOnUnsolOemHookRaw(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void setPhoneType(int paramInt) {}
  
  public void setPreferredNetworkType(int paramInt, Message paramMessage) {}
  
  public void setPreferredVoicePrivacy(boolean paramBoolean, Message paramMessage) {}
  
  public void setRadioCapability(RadioCapability paramRadioCapability, Message paramMessage) {}
  
  public void setRadioPower(boolean paramBoolean, Message paramMessage) {}
  
  public void setScreenState(boolean paramBoolean) {}
  
  public void setSignalStrengthReportingCriteria(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, Message paramMessage) {}
  
  public void setSimCardPower(int paramInt, Message paramMessage) {}
  
  public void setSmscAddress(String paramString, Message paramMessage) {}
  
  public void setSuppServiceNotifications(boolean paramBoolean, Message paramMessage) {}
  
  public void setTTYMode(int paramInt, Message paramMessage) {}
  
  public void setTransmitPower(int paramInt1, int paramInt2, Message paramMessage) {}
  
  public void setUiccSubscription(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Message paramMessage) {}
  
  public void setUnsolResponseFilter(int paramInt, Message paramMessage) {}
  
  public void setupDataCall(int paramInt1, DataProfile paramDataProfile, boolean paramBoolean1, boolean paramBoolean2, int paramInt2, LinkProperties paramLinkProperties, Message paramMessage) {}
  
  public void startDtmf(char paramChar, Message paramMessage) {}
  
  public void startLceService(int paramInt, boolean paramBoolean, Message paramMessage) {}
  
  public void startNattKeepalive(int paramInt1, KeepalivePacketData paramKeepalivePacketData, int paramInt2, Message paramMessage) {}
  
  public void startNetworkScan(NetworkScanRequest paramNetworkScanRequest, Message paramMessage) {}
  
  public void stopDtmf(Message paramMessage) {}
  
  public void stopLceService(Message paramMessage) {}
  
  public void stopNattKeepalive(int paramInt, Message paramMessage) {}
  
  public void stopNetworkScan(Message paramMessage) {}
  
  public void supplyIccPin(String paramString, Message paramMessage) {}
  
  public void supplyIccPin2(String paramString, Message paramMessage) {}
  
  public void supplyIccPin2ForApp(String paramString1, String paramString2, Message paramMessage) {}
  
  public void supplyIccPinForApp(String paramString1, String paramString2, Message paramMessage) {}
  
  public void supplyIccPuk(String paramString1, String paramString2, Message paramMessage) {}
  
  public void supplyIccPuk2(String paramString1, String paramString2, Message paramMessage) {}
  
  public void supplyIccPuk2ForApp(String paramString1, String paramString2, String paramString3, Message paramMessage) {}
  
  public void supplyIccPukForApp(String paramString1, String paramString2, String paramString3, Message paramMessage) {}
  
  public void supplyNetworkDepersonalization(String paramString, Message paramMessage) {}
  
  public void switchWaitingOrHoldingAndActive(Message paramMessage) {}
  
  public void testingEmergencyCall() {}
  
  public void unSetOnCallRing(Handler paramHandler) {}
  
  public void unSetOnCatCallSetUp(Handler paramHandler) {}
  
  public void unSetOnCatCcAlphaNotify(Handler paramHandler) {}
  
  public void unSetOnCatEvent(Handler paramHandler) {}
  
  public void unSetOnCatProactiveCmd(Handler paramHandler) {}
  
  public void unSetOnCatSessionEnd(Handler paramHandler) {}
  
  public void unSetOnIccSmsFull(Handler paramHandler) {}
  
  public void unSetOnNITZTime(Handler paramHandler) {}
  
  public void unSetOnNewCdmaSms(Handler paramHandler) {}
  
  public void unSetOnNewGsmBroadcastSms(Handler paramHandler) {}
  
  public void unSetOnNewGsmSms(Handler paramHandler) {}
  
  public void unSetOnRestrictedStateChanged(Handler paramHandler) {}
  
  public void unSetOnSignalStrengthUpdate(Handler paramHandler) {}
  
  public void unSetOnSmsOnSim(Handler paramHandler) {}
  
  public void unSetOnSmsStatus(Handler paramHandler) {}
  
  public void unSetOnSs(Handler paramHandler) {}
  
  public void unSetOnSuppServiceNotification(Handler paramHandler) {}
  
  public void unSetOnUSSD(Handler paramHandler) {}
  
  public void unSetOnUnsolOemHookRaw(Handler paramHandler) {}
  
  public void unregisterForAvailable(Handler paramHandler) {}
  
  public void unregisterForCallStateChanged(Handler paramHandler) {}
  
  public void unregisterForCallWaitingInfo(Handler paramHandler) {}
  
  public void unregisterForCarrierInfoForImsiEncryption(Handler paramHandler) {}
  
  public void unregisterForCdmaOtaProvision(Handler paramHandler) {}
  
  public void unregisterForCdmaPrlChanged(Handler paramHandler) {}
  
  public void unregisterForCdmaSubscriptionChanged(Handler paramHandler) {}
  
  public void unregisterForCellInfoList(Handler paramHandler) {}
  
  public void unregisterForDataCallListChanged(Handler paramHandler) {}
  
  public void unregisterForDisplayInfo(Handler paramHandler) {}
  
  public void unregisterForExitEmergencyCallbackMode(Handler paramHandler) {}
  
  public void unregisterForHardwareConfigChanged(Handler paramHandler) {}
  
  public void unregisterForIccRefresh(Handler paramHandler) {}
  
  public void unregisterForIccSlotStatusChanged(Handler paramHandler) {}
  
  public void unregisterForIccStatusChanged(Handler paramHandler) {}
  
  public void unregisterForImsNetworkStateChanged(Handler paramHandler) {}
  
  public void unregisterForInCallVoicePrivacyOff(Handler paramHandler) {}
  
  public void unregisterForInCallVoicePrivacyOn(Handler paramHandler) {}
  
  public void unregisterForLceInfo(Handler paramHandler) {}
  
  public void unregisterForLineControlInfo(Handler paramHandler) {}
  
  public void unregisterForModemReset(Handler paramHandler) {}
  
  public void unregisterForNattKeepaliveStatus(Handler paramHandler) {}
  
  public void unregisterForNetworkScanResult(Handler paramHandler) {}
  
  public void unregisterForNetworkStateChanged(Handler paramHandler) {}
  
  public void unregisterForNotAvailable(Handler paramHandler) {}
  
  public void unregisterForNumberInfo(Handler paramHandler) {}
  
  public void unregisterForOffOrNotAvailable(Handler paramHandler) {}
  
  public void unregisterForOn(Handler paramHandler) {}
  
  public void unregisterForPcoData(Handler paramHandler) {}
  
  public void unregisterForPhysicalChannelConfiguration(Handler paramHandler) {}
  
  public void unregisterForRadioCapabilityChanged(Handler paramHandler) {}
  
  public void unregisterForRadioStateChanged(Handler paramHandler) {}
  
  public void unregisterForRedirectedNumberInfo(Handler paramHandler) {}
  
  public void unregisterForResendIncallMute(Handler paramHandler) {}
  
  public void unregisterForRilConnected(Handler paramHandler) {}
  
  public void unregisterForRingbackTone(Handler paramHandler) {}
  
  public void unregisterForSignalInfo(Handler paramHandler) {}
  
  public void unregisterForSrvccStateChanged(Handler paramHandler) {}
  
  public void unregisterForSubscriptionStatusChanged(Handler paramHandler) {}
  
  public void unregisterForT53AudioControlInfo(Handler paramHandler) {}
  
  public void unregisterForT53ClirInfo(Handler paramHandler) {}
  
  public void unregisterForVoiceRadioTechChanged(Handler paramHandler) {}
  
  public void unsetOnIccRefresh(Handler paramHandler) {}
  
  public void writeSmsToRuim(int paramInt, String paramString, Message paramMessage) {}
  
  public void writeSmsToSim(int paramInt, String paramString1, String paramString2, Message paramMessage) {}
}
