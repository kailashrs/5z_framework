package com.android.internal.telephony;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.NetworkStats;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.service.carrier.CarrierIdentifier;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telephony.CellInfo;
import android.telephony.ClientRequestStats;
import android.telephony.IccOpenLogicalChannelResponse;
import android.telephony.NeighboringCellInfo;
import android.telephony.NetworkScanRequest;
import android.telephony.RadioAccessFamily;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyHistogram;
import android.telephony.UiccSlotInfo;
import android.telephony.VisualVoicemailSmsFilterSettings;
import android.telephony.ims.aidl.IImsConfig;
import android.telephony.ims.aidl.IImsConfig.Stub;
import android.telephony.ims.aidl.IImsMmTelFeature;
import android.telephony.ims.aidl.IImsMmTelFeature.Stub;
import android.telephony.ims.aidl.IImsRcsFeature;
import android.telephony.ims.aidl.IImsRcsFeature.Stub;
import android.telephony.ims.aidl.IImsRegistration;
import android.telephony.ims.aidl.IImsRegistration.Stub;
import com.android.ims.internal.IImsServiceFeatureCallback;
import com.android.ims.internal.IImsServiceFeatureCallback.Stub;
import java.util.ArrayList;
import java.util.List;

public abstract interface ITelephony
  extends IInterface
{
  public abstract void answerRingingCall()
    throws RemoteException;
  
  public abstract void answerRingingCallForSubscriber(int paramInt)
    throws RemoteException;
  
  public abstract void call(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract boolean canChangeDtmfToneLength()
    throws RemoteException;
  
  public abstract void carrierActionReportDefaultNetworkStatus(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void carrierActionSetMeteredApnsEnabled(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void carrierActionSetRadioEnabled(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract int checkCarrierPrivilegesForPackage(String paramString)
    throws RemoteException;
  
  public abstract int checkCarrierPrivilegesForPackageAnyPhone(String paramString)
    throws RemoteException;
  
  public abstract void dial(String paramString)
    throws RemoteException;
  
  public abstract boolean disableDataConnectivity()
    throws RemoteException;
  
  public abstract void disableIms(int paramInt)
    throws RemoteException;
  
  public abstract void disableLocationUpdates()
    throws RemoteException;
  
  public abstract void disableLocationUpdatesForSubscriber(int paramInt)
    throws RemoteException;
  
  public abstract void disableVisualVoicemailSmsFilter(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean enableDataConnectivity()
    throws RemoteException;
  
  public abstract void enableIms(int paramInt)
    throws RemoteException;
  
  public abstract void enableLocationUpdates()
    throws RemoteException;
  
  public abstract void enableLocationUpdatesForSubscriber(int paramInt)
    throws RemoteException;
  
  public abstract void enableVideoCalling(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void enableVisualVoicemailSmsFilter(String paramString, int paramInt, VisualVoicemailSmsFilterSettings paramVisualVoicemailSmsFilterSettings)
    throws RemoteException;
  
  public abstract boolean endCall()
    throws RemoteException;
  
  public abstract boolean endCallForSubscriber(int paramInt)
    throws RemoteException;
  
  public abstract void factoryReset(int paramInt)
    throws RemoteException;
  
  public abstract int getActivePhoneType()
    throws RemoteException;
  
  public abstract int getActivePhoneTypeForSlot(int paramInt)
    throws RemoteException;
  
  public abstract VisualVoicemailSmsFilterSettings getActiveVisualVoicemailSmsFilterSettings(int paramInt)
    throws RemoteException;
  
  public abstract String getAidForAppType(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract List<CellInfo> getAllCellInfo(String paramString)
    throws RemoteException;
  
  public abstract List<CarrierIdentifier> getAllowedCarriers(int paramInt)
    throws RemoteException;
  
  public abstract int getCalculatedPreferredNetworkType(String paramString)
    throws RemoteException;
  
  public abstract int getCallState()
    throws RemoteException;
  
  public abstract int getCallStateForSlot(int paramInt)
    throws RemoteException;
  
  public abstract int getCarrierIdListVersion(int paramInt)
    throws RemoteException;
  
  public abstract List<String> getCarrierPackageNamesForIntentAndPhone(Intent paramIntent, int paramInt)
    throws RemoteException;
  
  public abstract int getCarrierPrivilegeStatus(int paramInt)
    throws RemoteException;
  
  public abstract int getCarrierPrivilegeStatusForUid(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract int getCdmaEriIconIndex(String paramString)
    throws RemoteException;
  
  public abstract int getCdmaEriIconIndexForSubscriber(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract int getCdmaEriIconMode(String paramString)
    throws RemoteException;
  
  public abstract int getCdmaEriIconModeForSubscriber(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract String getCdmaEriText(String paramString)
    throws RemoteException;
  
  public abstract String getCdmaEriTextForSubscriber(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract String getCdmaMdn(int paramInt)
    throws RemoteException;
  
  public abstract String getCdmaMin(int paramInt)
    throws RemoteException;
  
  public abstract String getCdmaPrlVersion(int paramInt)
    throws RemoteException;
  
  public abstract Bundle getCellLocation(String paramString)
    throws RemoteException;
  
  public abstract CellNetworkScanResult getCellNetworkScanResults(int paramInt)
    throws RemoteException;
  
  public abstract List<ClientRequestStats> getClientRequestStats(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract int getDataActivationState(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract int getDataActivity()
    throws RemoteException;
  
  public abstract boolean getDataEnabled(int paramInt)
    throws RemoteException;
  
  public abstract int getDataNetworkType(String paramString)
    throws RemoteException;
  
  public abstract int getDataNetworkTypeForSubscriber(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract int getDataState()
    throws RemoteException;
  
  public abstract int getDefaultSim()
    throws RemoteException;
  
  public abstract String getDeviceId(String paramString)
    throws RemoteException;
  
  public abstract String getDeviceSoftwareVersionForSlot(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract boolean getEmergencyCallbackMode(int paramInt)
    throws RemoteException;
  
  public abstract String getEsn(int paramInt)
    throws RemoteException;
  
  public abstract String[] getForbiddenPlmns(int paramInt1, int paramInt2, String paramString)
    throws RemoteException;
  
  public abstract String getImeiForSlot(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract IImsConfig getImsConfig(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract int getImsRegTechnologyForMmTel(int paramInt)
    throws RemoteException;
  
  public abstract IImsRegistration getImsRegistration(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract String getImsService(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract String getLine1AlphaTagForDisplay(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract String getLine1NumberForDisplay(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract String getLocaleFromDefaultSim()
    throws RemoteException;
  
  public abstract int getLteOnCdmaMode(String paramString)
    throws RemoteException;
  
  public abstract int getLteOnCdmaModeForSubscriber(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract String getMeidForSlot(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract String[] getMergedSubscriberIds(String paramString)
    throws RemoteException;
  
  public abstract IImsMmTelFeature getMmTelFeatureAndListen(int paramInt, IImsServiceFeatureCallback paramIImsServiceFeatureCallback)
    throws RemoteException;
  
  public abstract List<NeighboringCellInfo> getNeighboringCellInfo(String paramString)
    throws RemoteException;
  
  public abstract String getNetworkCountryIsoForPhone(int paramInt)
    throws RemoteException;
  
  public abstract int getNetworkType()
    throws RemoteException;
  
  public abstract int getNetworkTypeForSubscriber(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract List<String> getPackagesWithCarrierPrivileges()
    throws RemoteException;
  
  public abstract String[] getPcscfAddress(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract int getPreferredNetworkType(int paramInt)
    throws RemoteException;
  
  public abstract int getRadioAccessFamily(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract IImsRcsFeature getRcsFeatureAndListen(int paramInt, IImsServiceFeatureCallback paramIImsServiceFeatureCallback)
    throws RemoteException;
  
  public abstract ServiceState getServiceStateForSubscriber(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract SignalStrength getSignalStrength(int paramInt)
    throws RemoteException;
  
  public abstract int getSubIdForPhoneAccount(PhoneAccount paramPhoneAccount)
    throws RemoteException;
  
  public abstract int getSubscriptionCarrierId(int paramInt)
    throws RemoteException;
  
  public abstract String getSubscriptionCarrierName(int paramInt)
    throws RemoteException;
  
  public abstract List<TelephonyHistogram> getTelephonyHistograms()
    throws RemoteException;
  
  public abstract int getTetherApnRequired()
    throws RemoteException;
  
  public abstract UiccSlotInfo[] getUiccSlotsInfo()
    throws RemoteException;
  
  public abstract String getVisualVoicemailPackageName(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract Bundle getVisualVoicemailSettings(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract VisualVoicemailSmsFilterSettings getVisualVoicemailSmsFilterSettings(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract int getVoiceActivationState(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract int getVoiceMessageCount()
    throws RemoteException;
  
  public abstract int getVoiceMessageCountForSubscriber(int paramInt)
    throws RemoteException;
  
  public abstract int getVoiceNetworkTypeForSubscriber(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract Uri getVoicemailRingtoneUri(PhoneAccountHandle paramPhoneAccountHandle)
    throws RemoteException;
  
  public abstract NetworkStats getVtDataUsage(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean handlePinMmi(String paramString)
    throws RemoteException;
  
  public abstract boolean handlePinMmiForSubscriber(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void handleUssdRequest(int paramInt, String paramString, ResultReceiver paramResultReceiver)
    throws RemoteException;
  
  public abstract boolean hasIccCard()
    throws RemoteException;
  
  public abstract boolean hasIccCardUsingSlotIndex(int paramInt)
    throws RemoteException;
  
  public abstract boolean iccCloseLogicalChannel(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract byte[] iccExchangeSimIO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, String paramString)
    throws RemoteException;
  
  public abstract IccOpenLogicalChannelResponse iccOpenLogicalChannel(int paramInt1, String paramString1, String paramString2, int paramInt2)
    throws RemoteException;
  
  public abstract String iccTransmitApduBasicChannel(int paramInt1, String paramString1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, String paramString2)
    throws RemoteException;
  
  public abstract String iccTransmitApduLogicalChannel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, String paramString)
    throws RemoteException;
  
  public abstract int invokeOemRilRequestRaw(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws RemoteException;
  
  public abstract boolean isConcurrentVoiceAndDataAllowed(int paramInt)
    throws RemoteException;
  
  public abstract boolean isDataConnectivityPossible(int paramInt)
    throws RemoteException;
  
  public abstract boolean isDataEnabled(int paramInt)
    throws RemoteException;
  
  public abstract boolean isHearingAidCompatibilitySupported()
    throws RemoteException;
  
  public abstract boolean isIdle(String paramString)
    throws RemoteException;
  
  public abstract boolean isIdleForSubscriber(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract boolean isImsRegistered(int paramInt)
    throws RemoteException;
  
  public abstract boolean isOffhook(String paramString)
    throws RemoteException;
  
  public abstract boolean isOffhookForSubscriber(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract boolean isRadioOn(String paramString)
    throws RemoteException;
  
  public abstract boolean isRadioOnForSubscriber(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract boolean isResolvingImsBinding()
    throws RemoteException;
  
  public abstract boolean isRinging(String paramString)
    throws RemoteException;
  
  public abstract boolean isRingingForSubscriber(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract boolean isTtyModeSupported()
    throws RemoteException;
  
  public abstract boolean isUserDataEnabled(int paramInt)
    throws RemoteException;
  
  public abstract boolean isVideoCallingEnabled(String paramString)
    throws RemoteException;
  
  public abstract boolean isVideoTelephonyAvailable(int paramInt)
    throws RemoteException;
  
  public abstract boolean isVoicemailVibrationEnabled(PhoneAccountHandle paramPhoneAccountHandle)
    throws RemoteException;
  
  public abstract boolean isVolteAvailable(int paramInt)
    throws RemoteException;
  
  public abstract boolean isWifiCallingAvailable(int paramInt)
    throws RemoteException;
  
  public abstract boolean isWorldPhone()
    throws RemoteException;
  
  public abstract boolean needMobileRadioShutdown()
    throws RemoteException;
  
  public abstract boolean needsOtaServiceProvisioning()
    throws RemoteException;
  
  public abstract String nvReadItem(int paramInt)
    throws RemoteException;
  
  public abstract boolean nvResetConfig(int paramInt)
    throws RemoteException;
  
  public abstract boolean nvWriteCdmaPrl(byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract boolean nvWriteItem(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void refreshUiccProfile(int paramInt)
    throws RemoteException;
  
  public abstract void requestModemActivityInfo(ResultReceiver paramResultReceiver)
    throws RemoteException;
  
  public abstract int requestNetworkScan(int paramInt, NetworkScanRequest paramNetworkScanRequest, Messenger paramMessenger, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void sendDialerSpecialCode(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract String sendEnvelopeWithStatus(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void sendVisualVoicemailSmsForSubscriber(String paramString1, int paramInt1, String paramString2, int paramInt2, String paramString3, PendingIntent paramPendingIntent)
    throws RemoteException;
  
  public abstract int setAllowedCarriers(int paramInt, List<CarrierIdentifier> paramList)
    throws RemoteException;
  
  public abstract void setCarrierTestOverride(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7)
    throws RemoteException;
  
  public abstract void setCellInfoListRate(int paramInt)
    throws RemoteException;
  
  public abstract void setDataActivationState(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setImsRegistrationState(boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean setImsService(int paramInt, boolean paramBoolean, String paramString)
    throws RemoteException;
  
  public abstract boolean setLine1NumberForDisplayForSubscriber(int paramInt, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void setNetworkSelectionModeAutomatic(int paramInt)
    throws RemoteException;
  
  public abstract boolean setNetworkSelectionModeManual(int paramInt, String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean setOperatorBrandOverride(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void setPolicyDataEnabled(boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract boolean setPreferredNetworkType(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract boolean setRadio(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setRadioCapability(RadioAccessFamily[] paramArrayOfRadioAccessFamily)
    throws RemoteException;
  
  public abstract boolean setRadioForSubscriber(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setRadioIndicationUpdateMode(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract boolean setRadioPower(boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean setRoamingOverride(int paramInt, List<String> paramList1, List<String> paramList2, List<String> paramList3, List<String> paramList4)
    throws RemoteException;
  
  public abstract void setSimPowerStateForSlot(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setUserDataEnabled(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setVoiceActivationState(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract boolean setVoiceMailNumber(int paramInt, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void setVoicemailRingtoneUri(String paramString, PhoneAccountHandle paramPhoneAccountHandle, Uri paramUri)
    throws RemoteException;
  
  public abstract void setVoicemailVibrationEnabled(String paramString, PhoneAccountHandle paramPhoneAccountHandle, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void shutdownMobileRadios()
    throws RemoteException;
  
  public abstract void silenceRinger()
    throws RemoteException;
  
  public abstract void stopNetworkScan(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract boolean supplyPin(String paramString)
    throws RemoteException;
  
  public abstract boolean supplyPinForSubscriber(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract int[] supplyPinReportResult(String paramString)
    throws RemoteException;
  
  public abstract int[] supplyPinReportResultForSubscriber(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract boolean supplyPuk(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract boolean supplyPukForSubscriber(int paramInt, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract int[] supplyPukReportResult(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract int[] supplyPukReportResultForSubscriber(int paramInt, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract boolean switchSlots(int[] paramArrayOfInt)
    throws RemoteException;
  
  public abstract void toggleRadioOnOff()
    throws RemoteException;
  
  public abstract void toggleRadioOnOffForSubscriber(int paramInt)
    throws RemoteException;
  
  public abstract void updateServiceLocation()
    throws RemoteException;
  
  public abstract void updateServiceLocationForSubscriber(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ITelephony
  {
    private static final String DESCRIPTOR = "com.android.internal.telephony.ITelephony";
    static final int TRANSACTION_answerRingingCall = 5;
    static final int TRANSACTION_answerRingingCallForSubscriber = 6;
    static final int TRANSACTION_call = 2;
    static final int TRANSACTION_canChangeDtmfToneLength = 139;
    static final int TRANSACTION_carrierActionReportDefaultNetworkStatus = 172;
    static final int TRANSACTION_carrierActionSetMeteredApnsEnabled = 170;
    static final int TRANSACTION_carrierActionSetRadioEnabled = 171;
    static final int TRANSACTION_checkCarrierPrivilegesForPackage = 123;
    static final int TRANSACTION_checkCarrierPrivilegesForPackageAnyPhone = 124;
    static final int TRANSACTION_dial = 1;
    static final int TRANSACTION_disableDataConnectivity = 39;
    static final int TRANSACTION_disableIms = 99;
    static final int TRANSACTION_disableLocationUpdates = 36;
    static final int TRANSACTION_disableLocationUpdatesForSubscriber = 37;
    static final int TRANSACTION_disableVisualVoicemailSmsFilter = 68;
    static final int TRANSACTION_enableDataConnectivity = 38;
    static final int TRANSACTION_enableIms = 98;
    static final int TRANSACTION_enableLocationUpdates = 34;
    static final int TRANSACTION_enableLocationUpdatesForSubscriber = 35;
    static final int TRANSACTION_enableVideoCalling = 137;
    static final int TRANSACTION_enableVisualVoicemailSmsFilter = 67;
    static final int TRANSACTION_endCall = 3;
    static final int TRANSACTION_endCallForSubscriber = 4;
    static final int TRANSACTION_factoryReset = 153;
    static final int TRANSACTION_getActivePhoneType = 48;
    static final int TRANSACTION_getActivePhoneTypeForSlot = 49;
    static final int TRANSACTION_getActiveVisualVoicemailSmsFilterSettings = 70;
    static final int TRANSACTION_getAidForAppType = 162;
    static final int TRANSACTION_getAllCellInfo = 82;
    static final int TRANSACTION_getAllowedCarriers = 167;
    static final int TRANSACTION_getCalculatedPreferredNetworkType = 95;
    static final int TRANSACTION_getCallState = 44;
    static final int TRANSACTION_getCallStateForSlot = 45;
    static final int TRANSACTION_getCarrierIdListVersion = 184;
    static final int TRANSACTION_getCarrierPackageNamesForIntentAndPhone = 125;
    static final int TRANSACTION_getCarrierPrivilegeStatus = 121;
    static final int TRANSACTION_getCarrierPrivilegeStatusForUid = 122;
    static final int TRANSACTION_getCdmaEriIconIndex = 50;
    static final int TRANSACTION_getCdmaEriIconIndexForSubscriber = 51;
    static final int TRANSACTION_getCdmaEriIconMode = 52;
    static final int TRANSACTION_getCdmaEriIconModeForSubscriber = 53;
    static final int TRANSACTION_getCdmaEriText = 54;
    static final int TRANSACTION_getCdmaEriTextForSubscriber = 55;
    static final int TRANSACTION_getCdmaMdn = 119;
    static final int TRANSACTION_getCdmaMin = 120;
    static final int TRANSACTION_getCdmaPrlVersion = 164;
    static final int TRANSACTION_getCellLocation = 41;
    static final int TRANSACTION_getCellNetworkScanResults = 108;
    static final int TRANSACTION_getClientRequestStats = 175;
    static final int TRANSACTION_getDataActivationState = 61;
    static final int TRANSACTION_getDataActivity = 46;
    static final int TRANSACTION_getDataEnabled = 114;
    static final int TRANSACTION_getDataNetworkType = 75;
    static final int TRANSACTION_getDataNetworkTypeForSubscriber = 76;
    static final int TRANSACTION_getDataState = 47;
    static final int TRANSACTION_getDefaultSim = 84;
    static final int TRANSACTION_getDeviceId = 148;
    static final int TRANSACTION_getDeviceSoftwareVersionForSlot = 151;
    static final int TRANSACTION_getEmergencyCallbackMode = 178;
    static final int TRANSACTION_getEsn = 163;
    static final int TRANSACTION_getForbiddenPlmns = 177;
    static final int TRANSACTION_getImeiForSlot = 149;
    static final int TRANSACTION_getImsConfig = 103;
    static final int TRANSACTION_getImsRegTechnologyForMmTel = 147;
    static final int TRANSACTION_getImsRegistration = 102;
    static final int TRANSACTION_getImsService = 106;
    static final int TRANSACTION_getLine1AlphaTagForDisplay = 128;
    static final int TRANSACTION_getLine1NumberForDisplay = 127;
    static final int TRANSACTION_getLocaleFromDefaultSim = 154;
    static final int TRANSACTION_getLteOnCdmaMode = 80;
    static final int TRANSACTION_getLteOnCdmaModeForSubscriber = 81;
    static final int TRANSACTION_getMeidForSlot = 150;
    static final int TRANSACTION_getMergedSubscriberIds = 129;
    static final int TRANSACTION_getMmTelFeatureAndListen = 100;
    static final int TRANSACTION_getNeighboringCellInfo = 43;
    static final int TRANSACTION_getNetworkCountryIsoForPhone = 42;
    static final int TRANSACTION_getNetworkType = 73;
    static final int TRANSACTION_getNetworkTypeForSubscriber = 74;
    static final int TRANSACTION_getPackagesWithCarrierPrivileges = 161;
    static final int TRANSACTION_getPcscfAddress = 117;
    static final int TRANSACTION_getPreferredNetworkType = 96;
    static final int TRANSACTION_getRadioAccessFamily = 136;
    static final int TRANSACTION_getRcsFeatureAndListen = 101;
    static final int TRANSACTION_getServiceStateForSubscriber = 156;
    static final int TRANSACTION_getSignalStrength = 179;
    static final int TRANSACTION_getSubIdForPhoneAccount = 152;
    static final int TRANSACTION_getSubscriptionCarrierId = 168;
    static final int TRANSACTION_getSubscriptionCarrierName = 169;
    static final int TRANSACTION_getTelephonyHistograms = 165;
    static final int TRANSACTION_getTetherApnRequired = 97;
    static final int TRANSACTION_getUiccSlotsInfo = 180;
    static final int TRANSACTION_getVisualVoicemailPackageName = 66;
    static final int TRANSACTION_getVisualVoicemailSettings = 65;
    static final int TRANSACTION_getVisualVoicemailSmsFilterSettings = 69;
    static final int TRANSACTION_getVoiceActivationState = 60;
    static final int TRANSACTION_getVoiceMessageCount = 62;
    static final int TRANSACTION_getVoiceMessageCountForSubscriber = 63;
    static final int TRANSACTION_getVoiceNetworkTypeForSubscriber = 77;
    static final int TRANSACTION_getVoicemailRingtoneUri = 157;
    static final int TRANSACTION_getVtDataUsage = 173;
    static final int TRANSACTION_handlePinMmi = 24;
    static final int TRANSACTION_handlePinMmiForSubscriber = 26;
    static final int TRANSACTION_handleUssdRequest = 25;
    static final int TRANSACTION_hasIccCard = 78;
    static final int TRANSACTION_hasIccCardUsingSlotIndex = 79;
    static final int TRANSACTION_iccCloseLogicalChannel = 86;
    static final int TRANSACTION_iccExchangeSimIO = 89;
    static final int TRANSACTION_iccOpenLogicalChannel = 85;
    static final int TRANSACTION_iccTransmitApduBasicChannel = 88;
    static final int TRANSACTION_iccTransmitApduLogicalChannel = 87;
    static final int TRANSACTION_invokeOemRilRequestRaw = 132;
    static final int TRANSACTION_isConcurrentVoiceAndDataAllowed = 64;
    static final int TRANSACTION_isDataConnectivityPossible = 40;
    static final int TRANSACTION_isDataEnabled = 116;
    static final int TRANSACTION_isHearingAidCompatibilitySupported = 142;
    static final int TRANSACTION_isIdle = 12;
    static final int TRANSACTION_isIdleForSubscriber = 13;
    static final int TRANSACTION_isImsRegistered = 143;
    static final int TRANSACTION_isOffhook = 8;
    static final int TRANSACTION_isOffhookForSubscriber = 9;
    static final int TRANSACTION_isRadioOn = 14;
    static final int TRANSACTION_isRadioOnForSubscriber = 15;
    static final int TRANSACTION_isResolvingImsBinding = 104;
    static final int TRANSACTION_isRinging = 11;
    static final int TRANSACTION_isRingingForSubscriber = 10;
    static final int TRANSACTION_isTtyModeSupported = 141;
    static final int TRANSACTION_isUserDataEnabled = 115;
    static final int TRANSACTION_isVideoCallingEnabled = 138;
    static final int TRANSACTION_isVideoTelephonyAvailable = 146;
    static final int TRANSACTION_isVoicemailVibrationEnabled = 159;
    static final int TRANSACTION_isVolteAvailable = 145;
    static final int TRANSACTION_isWifiCallingAvailable = 144;
    static final int TRANSACTION_isWorldPhone = 140;
    static final int TRANSACTION_needMobileRadioShutdown = 133;
    static final int TRANSACTION_needsOtaServiceProvisioning = 56;
    static final int TRANSACTION_nvReadItem = 91;
    static final int TRANSACTION_nvResetConfig = 94;
    static final int TRANSACTION_nvWriteCdmaPrl = 93;
    static final int TRANSACTION_nvWriteItem = 92;
    static final int TRANSACTION_refreshUiccProfile = 185;
    static final int TRANSACTION_requestModemActivityInfo = 155;
    static final int TRANSACTION_requestNetworkScan = 109;
    static final int TRANSACTION_sendDialerSpecialCode = 72;
    static final int TRANSACTION_sendEnvelopeWithStatus = 90;
    static final int TRANSACTION_sendVisualVoicemailSmsForSubscriber = 71;
    static final int TRANSACTION_setAllowedCarriers = 166;
    static final int TRANSACTION_setCarrierTestOverride = 183;
    static final int TRANSACTION_setCellInfoListRate = 83;
    static final int TRANSACTION_setDataActivationState = 59;
    static final int TRANSACTION_setImsRegistrationState = 118;
    static final int TRANSACTION_setImsService = 105;
    static final int TRANSACTION_setLine1NumberForDisplayForSubscriber = 126;
    static final int TRANSACTION_setNetworkSelectionModeAutomatic = 107;
    static final int TRANSACTION_setNetworkSelectionModeManual = 111;
    static final int TRANSACTION_setOperatorBrandOverride = 130;
    static final int TRANSACTION_setPolicyDataEnabled = 174;
    static final int TRANSACTION_setPreferredNetworkType = 112;
    static final int TRANSACTION_setRadio = 29;
    static final int TRANSACTION_setRadioCapability = 135;
    static final int TRANSACTION_setRadioForSubscriber = 30;
    static final int TRANSACTION_setRadioIndicationUpdateMode = 182;
    static final int TRANSACTION_setRadioPower = 31;
    static final int TRANSACTION_setRoamingOverride = 131;
    static final int TRANSACTION_setSimPowerStateForSlot = 176;
    static final int TRANSACTION_setUserDataEnabled = 113;
    static final int TRANSACTION_setVoiceActivationState = 58;
    static final int TRANSACTION_setVoiceMailNumber = 57;
    static final int TRANSACTION_setVoicemailRingtoneUri = 158;
    static final int TRANSACTION_setVoicemailVibrationEnabled = 160;
    static final int TRANSACTION_shutdownMobileRadios = 134;
    static final int TRANSACTION_silenceRinger = 7;
    static final int TRANSACTION_stopNetworkScan = 110;
    static final int TRANSACTION_supplyPin = 16;
    static final int TRANSACTION_supplyPinForSubscriber = 17;
    static final int TRANSACTION_supplyPinReportResult = 20;
    static final int TRANSACTION_supplyPinReportResultForSubscriber = 21;
    static final int TRANSACTION_supplyPuk = 18;
    static final int TRANSACTION_supplyPukForSubscriber = 19;
    static final int TRANSACTION_supplyPukReportResult = 22;
    static final int TRANSACTION_supplyPukReportResultForSubscriber = 23;
    static final int TRANSACTION_switchSlots = 181;
    static final int TRANSACTION_toggleRadioOnOff = 27;
    static final int TRANSACTION_toggleRadioOnOffForSubscriber = 28;
    static final int TRANSACTION_updateServiceLocation = 32;
    static final int TRANSACTION_updateServiceLocationForSubscriber = 33;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.telephony.ITelephony");
    }
    
    public static ITelephony asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.telephony.ITelephony");
      if ((localIInterface != null) && ((localIInterface instanceof ITelephony))) {
        return (ITelephony)localIInterface;
      }
      return new Proxy(paramIBinder);
    }
    
    public IBinder asBinder()
    {
      return this;
    }
    
    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      if (paramInt1 != 1598968902)
      {
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        String str1 = null;
        Object localObject7 = null;
        Object localObject8 = null;
        Object localObject9 = null;
        Object localObject10 = null;
        Object localObject11 = null;
        Object localObject12 = null;
        Object localObject13 = null;
        String str2 = null;
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        boolean bool4 = false;
        boolean bool5 = false;
        boolean bool6 = false;
        boolean bool7 = false;
        boolean bool8 = false;
        boolean bool9 = false;
        boolean bool10 = false;
        boolean bool11 = false;
        boolean bool12 = false;
        boolean bool13 = false;
        boolean bool14 = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 185: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          refreshUiccProfile(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 184: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getCarrierIdListVersion(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 183: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          setCarrierTestOverride(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 182: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          setRadioIndicationUpdateMode(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 181: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = switchSlots(paramParcel1.createIntArray());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 180: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getUiccSlotsInfo();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 179: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getSignalStrength(paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 178: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getEmergencyCallbackMode(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 177: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getForbiddenPlmns(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 176: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          setSimPowerStateForSlot(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 175: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getClientRequestStats(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 174: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          }
          setPolicyDataEnabled(bool14, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 173: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          } else {
            bool14 = false;
          }
          paramParcel1 = getVtDataUsage(paramInt1, bool14);
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 172: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = paramParcel1.readInt();
          bool14 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          }
          carrierActionReportDefaultNetworkStatus(paramInt1, bool14);
          paramParcel2.writeNoException();
          return true;
        case 171: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = paramParcel1.readInt();
          bool14 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          }
          carrierActionSetRadioEnabled(paramInt1, bool14);
          paramParcel2.writeNoException();
          return true;
        case 170: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = paramParcel1.readInt();
          bool14 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          }
          carrierActionSetMeteredApnsEnabled(paramInt1, bool14);
          paramParcel2.writeNoException();
          return true;
        case 169: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getSubscriptionCarrierName(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 168: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getSubscriptionCarrierId(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 167: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getAllowedCarriers(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 166: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = setAllowedCarriers(paramParcel1.readInt(), paramParcel1.createTypedArrayList(CarrierIdentifier.CREATOR));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 165: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getTelephonyHistograms();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 164: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getCdmaPrlVersion(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 163: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getEsn(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 162: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getAidForAppType(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 161: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getPackagesWithCarrierPrivileges();
          paramParcel2.writeNoException();
          paramParcel2.writeStringList(paramParcel1);
          return true;
        case 160: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          localObject2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject11 = (PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject11 = str2;
          }
          bool14 = bool4;
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          }
          setVoicemailVibrationEnabled((String)localObject2, (PhoneAccountHandle)localObject11, bool14);
          paramParcel2.writeNoException();
          return true;
        case 159: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          paramInt1 = isVoicemailVibrationEnabled(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 158: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          str1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject11 = (PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject11 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject2;
          }
          setVoicemailRingtoneUri(str1, (PhoneAccountHandle)localObject11, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 157: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PhoneAccountHandle)PhoneAccountHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          paramParcel1 = getVoicemailRingtoneUri(paramParcel1);
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 156: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getServiceStateForSubscriber(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 155: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          requestModemActivityInfo(paramParcel1);
          return true;
        case 154: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getLocaleFromDefaultSim();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 153: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          factoryReset(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 152: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PhoneAccount)PhoneAccount.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject5;
          }
          paramInt1 = getSubIdForPhoneAccount(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 151: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getDeviceSoftwareVersionForSlot(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 150: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getMeidForSlot(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 149: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getImeiForSlot(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 148: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getDeviceId(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 147: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getImsRegTechnologyForMmTel(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 146: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = isVideoTelephonyAvailable(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 145: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = isVolteAvailable(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 144: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = isWifiCallingAvailable(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 143: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = isImsRegistered(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 142: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = isHearingAidCompatibilitySupported();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 141: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = isTtyModeSupported();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 140: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = isWorldPhone();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 139: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = canChangeDtmfToneLength();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 138: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = isVideoCallingEnabled(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 137: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          bool14 = bool5;
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          }
          enableVideoCalling(bool14);
          paramParcel2.writeNoException();
          return true;
        case 136: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getRadioAccessFamily(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 135: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          setRadioCapability((RadioAccessFamily[])paramParcel1.createTypedArray(RadioAccessFamily.CREATOR));
          paramParcel2.writeNoException();
          return true;
        case 134: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          shutdownMobileRadios();
          paramParcel2.writeNoException();
          return true;
        case 133: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = needMobileRadioShutdown();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 132: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          localObject11 = paramParcel1.createByteArray();
          paramInt1 = paramParcel1.readInt();
          if (paramInt1 < 0) {
            paramParcel1 = null;
          } else {
            paramParcel1 = new byte[paramInt1];
          }
          paramInt1 = invokeOemRilRequestRaw((byte[])localObject11, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        case 131: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = setRoamingOverride(paramParcel1.readInt(), paramParcel1.createStringArrayList(), paramParcel1.createStringArrayList(), paramParcel1.createStringArrayList(), paramParcel1.createStringArrayList());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 130: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = setOperatorBrandOverride(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 129: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getMergedSubscriberIds(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 128: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getLine1AlphaTagForDisplay(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 127: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getLine1NumberForDisplay(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 126: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = setLine1NumberForDisplayForSubscriber(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 125: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          if (paramParcel1.readInt() != 0) {
            localObject11 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject11 = localObject6;
          }
          paramParcel1 = getCarrierPackageNamesForIntentAndPhone((Intent)localObject11, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeStringList(paramParcel1);
          return true;
        case 124: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = checkCarrierPrivilegesForPackageAnyPhone(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 123: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = checkCarrierPrivilegesForPackage(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 122: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getCarrierPrivilegeStatusForUid(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 121: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getCarrierPrivilegeStatus(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 120: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getCdmaMin(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 119: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getCdmaMdn(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 118: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          bool14 = bool6;
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          }
          setImsRegistrationState(bool14);
          paramParcel2.writeNoException();
          return true;
        case 117: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getPcscfAddress(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 116: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = isDataEnabled(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 115: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = isUserDataEnabled(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 114: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getDataEnabled(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 113: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = paramParcel1.readInt();
          bool14 = bool7;
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          }
          setUserDataEnabled(paramInt1, bool14);
          paramParcel2.writeNoException();
          return true;
        case 112: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = setPreferredNetworkType(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 111: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = paramParcel1.readInt();
          localObject11 = paramParcel1.readString();
          bool14 = bool8;
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          }
          paramInt1 = setNetworkSelectionModeManual(paramInt1, (String)localObject11, bool14);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 110: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          stopNetworkScan(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 109: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject11 = (NetworkScanRequest)NetworkScanRequest.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject11 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject2 = (Messenger)Messenger.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = str1;
          }
          paramInt1 = requestNetworkScan(paramInt1, (NetworkScanRequest)localObject11, (Messenger)localObject2, paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 108: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getCellNetworkScanResults(paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 107: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          setNetworkSelectionModeAutomatic(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 106: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = paramParcel1.readInt();
          bool14 = bool9;
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          }
          paramParcel1 = getImsService(paramInt1, bool14);
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 105: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = paramParcel1.readInt();
          bool14 = bool10;
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          }
          paramInt1 = setImsService(paramInt1, bool14, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 104: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = isResolvingImsBinding();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 103: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          localObject11 = getImsConfig(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel1 = localObject7;
          if (localObject11 != null) {
            paramParcel1 = ((IImsConfig)localObject11).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 102: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          localObject11 = getImsRegistration(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel1 = localObject8;
          if (localObject11 != null) {
            paramParcel1 = ((IImsRegistration)localObject11).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 101: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          localObject11 = getRcsFeatureAndListen(paramParcel1.readInt(), IImsServiceFeatureCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel1 = localObject9;
          if (localObject11 != null) {
            paramParcel1 = ((IImsRcsFeature)localObject11).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 100: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          localObject11 = getMmTelFeatureAndListen(paramParcel1.readInt(), IImsServiceFeatureCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel1 = localObject10;
          if (localObject11 != null) {
            paramParcel1 = ((IImsMmTelFeature)localObject11).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 99: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          disableIms(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 98: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          enableIms(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 97: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getTetherApnRequired();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 96: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getPreferredNetworkType(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 95: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getCalculatedPreferredNetworkType(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 94: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = nvResetConfig(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 93: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = nvWriteCdmaPrl(paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 92: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = nvWriteItem(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 91: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = nvReadItem(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 90: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = sendEnvelopeWithStatus(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 89: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = iccExchangeSimIO(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        case 88: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = iccTransmitApduBasicChannel(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 87: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = iccTransmitApduLogicalChannel(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 86: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = iccCloseLogicalChannel(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 85: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = iccOpenLogicalChannel(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 84: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getDefaultSim();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 83: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          setCellInfoListRate(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 82: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getAllCellInfo(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 81: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getLteOnCdmaModeForSubscriber(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 80: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getLteOnCdmaMode(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 79: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = hasIccCardUsingSlotIndex(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 78: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = hasIccCard();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 77: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getVoiceNetworkTypeForSubscriber(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 76: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getDataNetworkTypeForSubscriber(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 75: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getDataNetworkType(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 74: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getNetworkTypeForSubscriber(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 73: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getNetworkType();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 72: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          sendDialerSpecialCode(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 71: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          str1 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          localObject2 = paramParcel1.readString();
          paramInt2 = paramParcel1.readInt();
          str2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = (Parcel)localObject11) {
            break;
          }
          sendVisualVoicemailSmsForSubscriber(str1, paramInt1, (String)localObject2, paramInt2, str2, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 70: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getActiveVisualVoicemailSmsFilterSettings(paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 69: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getVisualVoicemailSmsFilterSettings(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 68: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          disableVisualVoicemailSmsFilter(paramParcel1.readString(), paramParcel1.readInt());
          return true;
        case 67: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          localObject11 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (VisualVoicemailSmsFilterSettings)VisualVoicemailSmsFilterSettings.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject12;
          }
          enableVisualVoicemailSmsFilter((String)localObject11, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 66: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getVisualVoicemailPackageName(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 65: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getVisualVoicemailSettings(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 64: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = isConcurrentVoiceAndDataAllowed(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 63: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getVoiceMessageCountForSubscriber(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 62: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getVoiceMessageCount();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 61: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getDataActivationState(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 60: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getVoiceActivationState(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 59: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          setDataActivationState(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 58: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          setVoiceActivationState(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 57: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = setVoiceMailNumber(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 56: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = needsOtaServiceProvisioning();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 55: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getCdmaEriTextForSubscriber(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 54: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getCdmaEriText(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 53: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getCdmaEriIconModeForSubscriber(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 52: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getCdmaEriIconMode(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 51: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getCdmaEriIconIndexForSubscriber(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 50: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getCdmaEriIconIndex(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 49: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getActivePhoneTypeForSlot(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 48: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getActivePhoneType();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 47: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getDataState();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 46: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getDataActivity();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 45: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getCallStateForSlot(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 44: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = getCallState();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 43: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getNeighboringCellInfo(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 42: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getNetworkCountryIsoForPhone(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 41: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = getCellLocation(paramParcel1.readString());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 40: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = isDataConnectivityPossible(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 39: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = disableDataConnectivity();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 38: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = enableDataConnectivity();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 37: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          disableLocationUpdatesForSubscriber(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 36: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          disableLocationUpdates();
          paramParcel2.writeNoException();
          return true;
        case 35: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          enableLocationUpdatesForSubscriber(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 34: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          enableLocationUpdates();
          paramParcel2.writeNoException();
          return true;
        case 33: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          updateServiceLocationForSubscriber(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 32: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          updateServiceLocation();
          paramParcel2.writeNoException();
          return true;
        case 31: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          bool14 = bool11;
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          }
          paramInt1 = setRadioPower(bool14);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 30: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = paramParcel1.readInt();
          bool14 = bool12;
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          }
          paramInt1 = setRadioForSubscriber(paramInt1, bool14);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 29: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          bool14 = bool13;
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          }
          paramInt1 = setRadio(bool14);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 28: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          toggleRadioOnOffForSubscriber(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 27: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          toggleRadioOnOff();
          paramParcel2.writeNoException();
          return true;
        case 26: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = handlePinMmiForSubscriber(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 25: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = paramParcel1.readInt();
          localObject11 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject13;
          }
          handleUssdRequest(paramInt1, (String)localObject11, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 24: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = handlePinMmi(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 23: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = supplyPukReportResultForSubscriber(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 22: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = supplyPukReportResult(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 21: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = supplyPinReportResultForSubscriber(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 20: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramParcel1 = supplyPinReportResult(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = supplyPukForSubscriber(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 18: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = supplyPuk(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = supplyPinForSubscriber(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = supplyPin(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = isRadioOnForSubscriber(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = isRadioOn(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = isIdleForSubscriber(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = isIdle(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = isRinging(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = isRingingForSubscriber(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = isOffhookForSubscriber(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = isOffhook(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          silenceRinger();
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          answerRingingCallForSubscriber(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          answerRingingCall();
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = endCallForSubscriber(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          paramInt1 = endCall();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
          call(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.telephony.ITelephony");
        dial(paramParcel1.readString());
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("com.android.internal.telephony.ITelephony");
      return true;
    }
    
    private static class Proxy
      implements ITelephony
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void answerRingingCall()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void answerRingingCallForSubscriber(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void call(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean canChangeDtmfToneLength()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(139, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void carrierActionReportDefaultNetworkStatus(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(172, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void carrierActionSetMeteredApnsEnabled(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(170, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void carrierActionSetRadioEnabled(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(171, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int checkCarrierPrivilegesForPackage(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          mRemote.transact(123, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int checkCarrierPrivilegesForPackageAnyPhone(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          mRemote.transact(124, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void dial(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean disableDataConnectivity()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(39, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void disableIms(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(99, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void disableLocationUpdates()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          mRemote.transact(36, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void disableLocationUpdatesForSubscriber(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(37, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void disableVisualVoicemailSmsFilter(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          mRemote.transact(68, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public boolean enableDataConnectivity()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(38, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void enableIms(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(98, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void enableLocationUpdates()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          mRemote.transact(34, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void enableLocationUpdatesForSubscriber(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(35, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void enableVideoCalling(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(137, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void enableVisualVoicemailSmsFilter(String paramString, int paramInt, VisualVoicemailSmsFilterSettings paramVisualVoicemailSmsFilterSettings)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          if (paramVisualVoicemailSmsFilterSettings != null)
          {
            localParcel1.writeInt(1);
            paramVisualVoicemailSmsFilterSettings.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(67, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean endCall()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean endCallForSubscriber(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void factoryReset(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(153, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getActivePhoneType()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          mRemote.transact(48, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getActivePhoneTypeForSlot(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(49, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public VisualVoicemailSmsFilterSettings getActiveVisualVoicemailSmsFilterSettings(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(70, localParcel1, localParcel2, 0);
          localParcel2.readException();
          VisualVoicemailSmsFilterSettings localVisualVoicemailSmsFilterSettings;
          if (localParcel2.readInt() != 0) {
            localVisualVoicemailSmsFilterSettings = (VisualVoicemailSmsFilterSettings)VisualVoicemailSmsFilterSettings.CREATOR.createFromParcel(localParcel2);
          } else {
            localVisualVoicemailSmsFilterSettings = null;
          }
          return localVisualVoicemailSmsFilterSettings;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getAidForAppType(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(162, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<CellInfo> getAllCellInfo(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          mRemote.transact(82, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createTypedArrayList(CellInfo.CREATOR);
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<CarrierIdentifier> getAllowedCarriers(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(167, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(CarrierIdentifier.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getCalculatedPreferredNetworkType(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          mRemote.transact(95, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getCallState()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          mRemote.transact(44, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getCallStateForSlot(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(45, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getCarrierIdListVersion(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(184, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<String> getCarrierPackageNamesForIntentAndPhone(Intent paramIntent, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(125, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramIntent = localParcel2.createStringArrayList();
          return paramIntent;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getCarrierPrivilegeStatus(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(121, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getCarrierPrivilegeStatusForUid(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(122, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getCdmaEriIconIndex(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          mRemote.transact(50, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getCdmaEriIconIndexForSubscriber(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(51, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getCdmaEriIconMode(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          mRemote.transact(52, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getCdmaEriIconModeForSubscriber(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(53, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getCdmaEriText(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          mRemote.transact(54, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getCdmaEriTextForSubscriber(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(55, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getCdmaMdn(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(119, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getCdmaMin(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(120, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getCdmaPrlVersion(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(164, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Bundle getCellLocation(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          mRemote.transact(41, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (Bundle)Bundle.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public CellNetworkScanResult getCellNetworkScanResults(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(108, localParcel1, localParcel2, 0);
          localParcel2.readException();
          CellNetworkScanResult localCellNetworkScanResult;
          if (localParcel2.readInt() != 0) {
            localCellNetworkScanResult = (CellNetworkScanResult)CellNetworkScanResult.CREATOR.createFromParcel(localParcel2);
          } else {
            localCellNetworkScanResult = null;
          }
          return localCellNetworkScanResult;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<ClientRequestStats> getClientRequestStats(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(175, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createTypedArrayList(ClientRequestStats.CREATOR);
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getDataActivationState(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(61, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getDataActivity()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          mRemote.transact(46, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean getDataEnabled(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(114, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getDataNetworkType(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          mRemote.transact(75, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getDataNetworkTypeForSubscriber(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(76, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getDataState()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          mRemote.transact(47, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getDefaultSim()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          mRemote.transact(84, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getDeviceId(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          mRemote.transact(148, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getDeviceSoftwareVersionForSlot(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(151, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean getEmergencyCallbackMode(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(178, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getEsn(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(163, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getForbiddenPlmns(int paramInt1, int paramInt2, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString);
          mRemote.transact(177, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createStringArray();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getImeiForSlot(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(149, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IImsConfig getImsConfig(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(103, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IImsConfig localIImsConfig = IImsConfig.Stub.asInterface(localParcel2.readStrongBinder());
          return localIImsConfig;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getImsRegTechnologyForMmTel(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(147, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IImsRegistration getImsRegistration(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(102, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IImsRegistration localIImsRegistration = IImsRegistration.Stub.asInterface(localParcel2.readStrongBinder());
          return localIImsRegistration;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getImsService(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(106, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.telephony.ITelephony";
      }
      
      public String getLine1AlphaTagForDisplay(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(128, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getLine1NumberForDisplay(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(127, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getLocaleFromDefaultSim()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          mRemote.transact(154, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getLteOnCdmaMode(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          mRemote.transact(80, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getLteOnCdmaModeForSubscriber(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(81, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getMeidForSlot(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(150, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getMergedSubscriberIds(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          mRemote.transact(129, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createStringArray();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IImsMmTelFeature getMmTelFeatureAndListen(int paramInt, IImsServiceFeatureCallback paramIImsServiceFeatureCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          if (paramIImsServiceFeatureCallback != null) {
            paramIImsServiceFeatureCallback = paramIImsServiceFeatureCallback.asBinder();
          } else {
            paramIImsServiceFeatureCallback = null;
          }
          localParcel1.writeStrongBinder(paramIImsServiceFeatureCallback);
          mRemote.transact(100, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramIImsServiceFeatureCallback = IImsMmTelFeature.Stub.asInterface(localParcel2.readStrongBinder());
          return paramIImsServiceFeatureCallback;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<NeighboringCellInfo> getNeighboringCellInfo(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          mRemote.transact(43, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createTypedArrayList(NeighboringCellInfo.CREATOR);
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getNetworkCountryIsoForPhone(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(42, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getNetworkType()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          mRemote.transact(73, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getNetworkTypeForSubscriber(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(74, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<String> getPackagesWithCarrierPrivileges()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          mRemote.transact(161, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createStringArrayList();
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getPcscfAddress(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(117, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString1 = localParcel2.createStringArray();
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getPreferredNetworkType(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(96, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getRadioAccessFamily(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(136, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IImsRcsFeature getRcsFeatureAndListen(int paramInt, IImsServiceFeatureCallback paramIImsServiceFeatureCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          if (paramIImsServiceFeatureCallback != null) {
            paramIImsServiceFeatureCallback = paramIImsServiceFeatureCallback.asBinder();
          } else {
            paramIImsServiceFeatureCallback = null;
          }
          localParcel1.writeStrongBinder(paramIImsServiceFeatureCallback);
          mRemote.transact(101, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramIImsServiceFeatureCallback = IImsRcsFeature.Stub.asInterface(localParcel2.readStrongBinder());
          return paramIImsServiceFeatureCallback;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ServiceState getServiceStateForSubscriber(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(156, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ServiceState)ServiceState.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public SignalStrength getSignalStrength(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(179, localParcel1, localParcel2, 0);
          localParcel2.readException();
          SignalStrength localSignalStrength;
          if (localParcel2.readInt() != 0) {
            localSignalStrength = (SignalStrength)SignalStrength.CREATOR.createFromParcel(localParcel2);
          } else {
            localSignalStrength = null;
          }
          return localSignalStrength;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getSubIdForPhoneAccount(PhoneAccount paramPhoneAccount)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          if (paramPhoneAccount != null)
          {
            localParcel1.writeInt(1);
            paramPhoneAccount.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(152, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getSubscriptionCarrierId(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(168, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getSubscriptionCarrierName(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(169, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<TelephonyHistogram> getTelephonyHistograms()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          mRemote.transact(165, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(TelephonyHistogram.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getTetherApnRequired()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          mRemote.transact(97, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public UiccSlotInfo[] getUiccSlotsInfo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          mRemote.transact(180, localParcel1, localParcel2, 0);
          localParcel2.readException();
          UiccSlotInfo[] arrayOfUiccSlotInfo = (UiccSlotInfo[])localParcel2.createTypedArray(UiccSlotInfo.CREATOR);
          return arrayOfUiccSlotInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getVisualVoicemailPackageName(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(66, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Bundle getVisualVoicemailSettings(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(65, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (Bundle)Bundle.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public VisualVoicemailSmsFilterSettings getVisualVoicemailSmsFilterSettings(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(69, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (VisualVoicemailSmsFilterSettings)VisualVoicemailSmsFilterSettings.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getVoiceActivationState(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(60, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getVoiceMessageCount()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          mRemote.transact(62, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getVoiceMessageCountForSubscriber(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(63, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getVoiceNetworkTypeForSubscriber(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(77, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Uri getVoicemailRingtoneUri(PhoneAccountHandle paramPhoneAccountHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          if (paramPhoneAccountHandle != null)
          {
            localParcel1.writeInt(1);
            paramPhoneAccountHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(157, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramPhoneAccountHandle = (Uri)Uri.CREATOR.createFromParcel(localParcel2);
          } else {
            paramPhoneAccountHandle = null;
          }
          return paramPhoneAccountHandle;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NetworkStats getVtDataUsage(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(173, localParcel1, localParcel2, 0);
          localParcel2.readException();
          NetworkStats localNetworkStats;
          if (localParcel2.readInt() != 0) {
            localNetworkStats = (NetworkStats)NetworkStats.CREATOR.createFromParcel(localParcel2);
          } else {
            localNetworkStats = null;
          }
          return localNetworkStats;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean handlePinMmi(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean handlePinMmiForSubscriber(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(26, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void handleUssdRequest(int paramInt, String paramString, ResultReceiver paramResultReceiver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          if (paramResultReceiver != null)
          {
            localParcel1.writeInt(1);
            paramResultReceiver.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean hasIccCard()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(78, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean hasIccCardUsingSlotIndex(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(79, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean iccCloseLogicalChannel(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(86, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public byte[] iccExchangeSimIO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          localParcel1.writeInt(paramInt5);
          localParcel1.writeInt(paramInt6);
          localParcel1.writeString(paramString);
          mRemote.transact(89, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createByteArray();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IccOpenLogicalChannelResponse iccOpenLogicalChannel(int paramInt1, String paramString1, String paramString2, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(85, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString1 = (IccOpenLogicalChannelResponse)IccOpenLogicalChannelResponse.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString1 = null;
          }
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String iccTransmitApduBasicChannel(int paramInt1, String paramString1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          localParcel1.writeInt(paramInt5);
          localParcel1.writeInt(paramInt6);
          localParcel1.writeString(paramString2);
          mRemote.transact(88, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString1 = localParcel2.readString();
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String iccTransmitApduLogicalChannel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          localParcel1.writeInt(paramInt5);
          localParcel1.writeInt(paramInt6);
          localParcel1.writeInt(paramInt7);
          localParcel1.writeString(paramString);
          mRemote.transact(87, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int invokeOemRilRequestRaw(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeByteArray(paramArrayOfByte1);
          if (paramArrayOfByte2 == null) {
            localParcel1.writeInt(-1);
          } else {
            localParcel1.writeInt(paramArrayOfByte2.length);
          }
          mRemote.transact(132, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          localParcel2.readByteArray(paramArrayOfByte2);
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isConcurrentVoiceAndDataAllowed(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(64, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isDataConnectivityPossible(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(40, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isDataEnabled(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(116, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isHearingAidCompatibilitySupported()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(142, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isIdle(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isIdleForSubscriber(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isImsRegistered(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(143, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isOffhook(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isOffhookForSubscriber(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isRadioOn(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isRadioOnForSubscriber(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isResolvingImsBinding()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(104, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isRinging(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isRingingForSubscriber(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isTtyModeSupported()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(141, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isUserDataEnabled(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(115, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isVideoCallingEnabled(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(138, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isVideoTelephonyAvailable(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(146, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isVoicemailVibrationEnabled(PhoneAccountHandle paramPhoneAccountHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          boolean bool = true;
          if (paramPhoneAccountHandle != null)
          {
            localParcel1.writeInt(1);
            paramPhoneAccountHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(159, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isVolteAvailable(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(145, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isWifiCallingAvailable(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(144, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isWorldPhone()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(140, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean needMobileRadioShutdown()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(133, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean needsOtaServiceProvisioning()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(56, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String nvReadItem(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(91, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean nvResetConfig(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(94, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean nvWriteCdmaPrl(byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeByteArray(paramArrayOfByte);
          paramArrayOfByte = mRemote;
          boolean bool = false;
          paramArrayOfByte.transact(93, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean nvWriteItem(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(92, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void refreshUiccProfile(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(185, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void requestModemActivityInfo(ResultReceiver paramResultReceiver)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          if (paramResultReceiver != null)
          {
            localParcel.writeInt(1);
            paramResultReceiver.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(155, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public int requestNetworkScan(int paramInt, NetworkScanRequest paramNetworkScanRequest, Messenger paramMessenger, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          if (paramNetworkScanRequest != null)
          {
            localParcel1.writeInt(1);
            paramNetworkScanRequest.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramMessenger != null)
          {
            localParcel1.writeInt(1);
            paramMessenger.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(109, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void sendDialerSpecialCode(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(72, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String sendEnvelopeWithStatus(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(90, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void sendVisualVoicemailSmsForSubscriber(String paramString1, int paramInt1, String paramString2, int paramInt2, String paramString3, PendingIntent paramPendingIntent)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString3);
          if (paramPendingIntent != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(71, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int setAllowedCarriers(int paramInt, List<CarrierIdentifier> paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeTypedList(paramList);
          mRemote.transact(166, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setCarrierTestOverride(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          localParcel1.writeString(paramString4);
          localParcel1.writeString(paramString5);
          localParcel1.writeString(paramString6);
          localParcel1.writeString(paramString7);
          mRemote.transact(183, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setCellInfoListRate(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(83, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setDataActivationState(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(59, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setImsRegistrationState(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(118, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setImsService(int paramInt, boolean paramBoolean, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(105, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setLine1NumberForDisplayForSubscriber(int paramInt, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          paramString1 = mRemote;
          boolean bool = false;
          paramString1.transact(126, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setNetworkSelectionModeAutomatic(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(107, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setNetworkSelectionModeManual(int paramInt, String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(111, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setOperatorBrandOverride(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(130, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setPolicyDataEnabled(boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
          mRemote.transact(174, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setPreferredNetworkType(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(112, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setRadio(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramBoolean);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(29, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (paramBoolean) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setRadioCapability(RadioAccessFamily[] paramArrayOfRadioAccessFamily)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeTypedArray(paramArrayOfRadioAccessFamily, 0);
          mRemote.transact(135, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setRadioForSubscriber(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(30, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setRadioIndicationUpdateMode(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(182, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setRadioPower(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramBoolean);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(31, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (paramBoolean) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setRoamingOverride(int paramInt, List<String> paramList1, List<String> paramList2, List<String> paramList3, List<String> paramList4)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeStringList(paramList1);
          localParcel1.writeStringList(paramList2);
          localParcel1.writeStringList(paramList3);
          localParcel1.writeStringList(paramList4);
          paramList1 = mRemote;
          boolean bool = false;
          paramList1.transact(131, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setSimPowerStateForSlot(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(176, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setUserDataEnabled(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(113, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setVoiceActivationState(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(58, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setVoiceMailNumber(int paramInt, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          paramString1 = mRemote;
          boolean bool = false;
          paramString1.transact(57, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setVoicemailRingtoneUri(String paramString, PhoneAccountHandle paramPhoneAccountHandle, Uri paramUri)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          if (paramPhoneAccountHandle != null)
          {
            localParcel1.writeInt(1);
            paramPhoneAccountHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(158, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setVoicemailVibrationEnabled(String paramString, PhoneAccountHandle paramPhoneAccountHandle, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          if (paramPhoneAccountHandle != null)
          {
            localParcel1.writeInt(1);
            paramPhoneAccountHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(160, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void shutdownMobileRadios()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          mRemote.transact(134, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void silenceRinger()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void stopNetworkScan(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(110, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean supplyPin(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean supplyPinForSubscriber(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int[] supplyPinReportResult(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString);
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createIntArray();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int[] supplyPinReportResultForSubscriber(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createIntArray();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean supplyPuk(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          paramString1 = mRemote;
          boolean bool = false;
          paramString1.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean supplyPukForSubscriber(int paramInt, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          paramString1 = mRemote;
          boolean bool = false;
          paramString1.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int[] supplyPukReportResult(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString1 = localParcel2.createIntArray();
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int[] supplyPukReportResultForSubscriber(int paramInt, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString1 = localParcel2.createIntArray();
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean switchSlots(int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeIntArray(paramArrayOfInt);
          paramArrayOfInt = mRemote;
          boolean bool = false;
          paramArrayOfInt.transact(181, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void toggleRadioOnOff()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          mRemote.transact(27, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void toggleRadioOnOffForSubscriber(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(28, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void updateServiceLocation()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          mRemote.transact(32, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void updateServiceLocationForSubscriber(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.telephony.ITelephony");
          localParcel1.writeInt(paramInt);
          mRemote.transact(33, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
    }
  }
}
