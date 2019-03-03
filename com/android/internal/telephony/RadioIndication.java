package com.android.internal.telephony;

import android.hardware.radio.V1_0.CdmaCallWaiting;
import android.hardware.radio.V1_0.CdmaDisplayInfoRecord;
import android.hardware.radio.V1_0.CdmaInformationRecord;
import android.hardware.radio.V1_0.CdmaLineControlInfoRecord;
import android.hardware.radio.V1_0.CdmaNumberInfoRecord;
import android.hardware.radio.V1_0.CdmaRedirectingNumberInfoRecord;
import android.hardware.radio.V1_0.CdmaSignalInfoRecord;
import android.hardware.radio.V1_0.CdmaSmsMessage;
import android.hardware.radio.V1_0.CdmaT53AudioControlInfoRecord;
import android.hardware.radio.V1_0.CdmaT53ClirInfoRecord;
import android.hardware.radio.V1_0.CfData;
import android.hardware.radio.V1_0.HardwareConfig;
import android.hardware.radio.V1_0.LceDataInfo;
import android.hardware.radio.V1_0.PcoDataInfo;
import android.hardware.radio.V1_0.RadioCapability;
import android.hardware.radio.V1_0.SetupDataCallResult;
import android.hardware.radio.V1_0.SimRefreshResult;
import android.hardware.radio.V1_0.SsInfoData;
import android.hardware.radio.V1_0.StkCcUnsolSsResult;
import android.hardware.radio.V1_0.SuppSvcNotification;
import android.hardware.radio.V1_2.IRadioIndication.Stub;
import android.hardware.radio.V1_2.LinkCapacityEstimate;
import android.os.AsyncResult;
import android.os.Registrant;
import android.os.RegistrantList;
import android.os.SystemProperties;
import android.telephony.PcoData;
import android.telephony.ServiceState;
import android.telephony.SmsMessage;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.cdma.CdmaCallWaitingNotification;
import com.android.internal.telephony.cdma.CdmaInformationRecords.CdmaDisplayInfoRec;
import com.android.internal.telephony.cdma.CdmaInformationRecords.CdmaLineControlInfoRec;
import com.android.internal.telephony.cdma.CdmaInformationRecords.CdmaNumberInfoRec;
import com.android.internal.telephony.cdma.CdmaInformationRecords.CdmaRedirectingNumberInfoRec;
import com.android.internal.telephony.cdma.CdmaInformationRecords.CdmaSignalInfoRec;
import com.android.internal.telephony.cdma.CdmaInformationRecords.CdmaT53AudioControlInfoRec;
import com.android.internal.telephony.cdma.CdmaInformationRecords.CdmaT53ClirInfoRec;
import com.android.internal.telephony.cdma.SmsMessageConverter;
import com.android.internal.telephony.gsm.SsData;
import com.android.internal.telephony.gsm.SsData.RequestType;
import com.android.internal.telephony.gsm.SsData.ServiceType;
import com.android.internal.telephony.gsm.SuppServiceNotification;
import com.android.internal.telephony.uicc.IccRefreshResponse;
import com.android.internal.telephony.uicc.IccUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RadioIndication
  extends IRadioIndication.Stub
{
  RIL mRil;
  
  RadioIndication(RIL paramRIL)
  {
    mRil = paramRIL;
  }
  
  private CommandsInterface.RadioState getRadioStateFromInt(int paramInt)
  {
    Object localObject;
    if (paramInt != 10) {
      switch (paramInt)
      {
      default: 
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Unrecognized RadioState: ");
        ((StringBuilder)localObject).append(paramInt);
        throw new RuntimeException(((StringBuilder)localObject).toString());
      case 1: 
        localObject = CommandsInterface.RadioState.RADIO_UNAVAILABLE;
        break;
      case 0: 
        localObject = CommandsInterface.RadioState.RADIO_OFF;
        break;
      }
    } else {
      localObject = CommandsInterface.RadioState.RADIO_ON;
    }
    return localObject;
  }
  
  private void responseCellInfos(int paramInt, android.hardware.radio.V1_1.NetworkScanResult paramNetworkScanResult)
  {
    mRil.processIndication(paramInt);
    ArrayList localArrayList = RIL.convertHalCellInfoList(networkInfos);
    paramNetworkScanResult = new NetworkScanResult(status, error, localArrayList);
    mRil.unsljLogRet(1049, paramNetworkScanResult);
    mRil.mRilNetworkScanResultRegistrants.notifyRegistrants(new AsyncResult(null, paramNetworkScanResult, null));
  }
  
  private void responseCellInfos_1_2(int paramInt, android.hardware.radio.V1_2.NetworkScanResult paramNetworkScanResult)
  {
    mRil.processIndication(paramInt);
    ArrayList localArrayList = RIL.convertHalCellInfoList_1_2(networkInfos);
    paramNetworkScanResult = new NetworkScanResult(status, error, localArrayList);
    mRil.unsljLogRet(1049, paramNetworkScanResult);
    mRil.mRilNetworkScanResultRegistrants.notifyRegistrants(new AsyncResult(null, paramNetworkScanResult, null));
  }
  
  public void callRing(int paramInt, boolean paramBoolean, CdmaSignalInfoRecord paramCdmaSignalInfoRecord)
  {
    mRil.processIndication(paramInt);
    char[] arrayOfChar = null;
    if (!paramBoolean)
    {
      arrayOfChar = new char[4];
      arrayOfChar[0] = ((char)(char)isPresent);
      arrayOfChar[1] = ((char)(char)signalType);
      arrayOfChar[2] = ((char)(char)alertPitch);
      arrayOfChar[3] = ((char)(char)signal);
      mRil.writeMetricsCallRing(arrayOfChar);
    }
    mRil.unsljLogRet(1018, arrayOfChar);
    if (mRil.mRingRegistrant != null) {
      mRil.mRingRegistrant.notifyRegistrant(new AsyncResult(null, arrayOfChar, null));
    }
  }
  
  public void callStateChanged(int paramInt)
  {
    mRil.processIndication(paramInt);
    mRil.unsljLog(1001);
    mRil.mCallStateRegistrants.notifyRegistrants();
  }
  
  public void carrierInfoForImsiEncryption(int paramInt)
  {
    mRil.processIndication(paramInt);
    mRil.unsljLogRet(1048, null);
    mRil.mCarrierInfoForImsiEncryptionRegistrants.notifyRegistrants(new AsyncResult(null, null, null));
  }
  
  public void cdmaCallWaiting(int paramInt, CdmaCallWaiting paramCdmaCallWaiting)
  {
    mRil.processIndication(paramInt);
    CdmaCallWaitingNotification localCdmaCallWaitingNotification = new CdmaCallWaitingNotification();
    number = number;
    numberPresentation = CdmaCallWaitingNotification.presentationFromCLIP(numberPresentation);
    name = name;
    namePresentation = numberPresentation;
    isPresent = signalInfoRecord.isPresent;
    signalType = signalInfoRecord.signalType;
    alertPitch = signalInfoRecord.alertPitch;
    signal = signalInfoRecord.signal;
    numberType = numberType;
    numberPlan = numberPlan;
    mRil.unsljLogRet(1025, localCdmaCallWaitingNotification);
    mRil.mCallWaitingInfoRegistrants.notifyRegistrants(new AsyncResult(null, localCdmaCallWaitingNotification, null));
  }
  
  public void cdmaInfoRec(int paramInt, android.hardware.radio.V1_0.CdmaInformationRecords paramCdmaInformationRecords)
  {
    mRil.processIndication(paramInt);
    int i = infoRec.size();
    for (paramInt = 0; paramInt < i; paramInt++)
    {
      Object localObject = (CdmaInformationRecord)infoRec.get(paramInt);
      int j = name;
      switch (j)
      {
      case 9: 
      default: 
        paramCdmaInformationRecords = new StringBuilder();
        paramCdmaInformationRecords.append("RIL_UNSOL_CDMA_INFO_REC: unsupported record. Got ");
        paramCdmaInformationRecords.append(com.android.internal.telephony.cdma.CdmaInformationRecords.idToString(j));
        paramCdmaInformationRecords.append(" ");
        throw new RuntimeException(paramCdmaInformationRecords.toString());
      case 10: 
        localObject = (CdmaT53AudioControlInfoRecord)audioCtrl.get(0);
        localObject = new com.android.internal.telephony.cdma.CdmaInformationRecords(new CdmaInformationRecords.CdmaT53AudioControlInfoRec(upLink, downLink));
        break;
      case 8: 
        localObject = new com.android.internal.telephony.cdma.CdmaInformationRecords(new CdmaInformationRecords.CdmaT53ClirInfoRec(clir.get(0)).cause));
        break;
      case 6: 
        localObject = (CdmaLineControlInfoRecord)lineCtrl.get(0);
        localObject = new com.android.internal.telephony.cdma.CdmaInformationRecords(new CdmaInformationRecords.CdmaLineControlInfoRec(lineCtrlPolarityIncluded, lineCtrlToggle, lineCtrlReverse, lineCtrlPowerDenial));
        break;
      case 5: 
        localObject = (CdmaRedirectingNumberInfoRecord)redir.get(0);
        localObject = new com.android.internal.telephony.cdma.CdmaInformationRecords(new CdmaInformationRecords.CdmaRedirectingNumberInfoRec(redirectingNumber.number, redirectingNumber.numberType, redirectingNumber.numberPlan, redirectingNumber.pi, redirectingNumber.si, redirectingReason));
        break;
      case 4: 
        localObject = (CdmaSignalInfoRecord)signal.get(0);
        localObject = new com.android.internal.telephony.cdma.CdmaInformationRecords(new CdmaInformationRecords.CdmaSignalInfoRec(isPresent, signalType, alertPitch, signal));
        break;
      case 1: 
      case 2: 
      case 3: 
        localObject = (CdmaNumberInfoRecord)number.get(0);
        localObject = new com.android.internal.telephony.cdma.CdmaInformationRecords(new CdmaInformationRecords.CdmaNumberInfoRec(j, number, numberType, numberPlan, pi, si));
        break;
      case 0: 
      case 7: 
        localObject = new com.android.internal.telephony.cdma.CdmaInformationRecords(new CdmaInformationRecords.CdmaDisplayInfoRec(j, display.get(0)).alphaBuf));
      }
      mRil.unsljLogRet(1027, localObject);
      mRil.notifyRegistrantsCdmaInfoRec((com.android.internal.telephony.cdma.CdmaInformationRecords)localObject);
    }
  }
  
  public void cdmaNewSms(int paramInt, CdmaSmsMessage paramCdmaSmsMessage)
  {
    mRil.processIndication(paramInt);
    mRil.unsljLog(1020);
    mRil.writeMetricsNewSms(2, 2);
    paramCdmaSmsMessage = SmsMessageConverter.newSmsMessageFromCdmaSmsMessage(paramCdmaSmsMessage);
    if (mRil.mCdmaSmsRegistrant != null) {
      mRil.mCdmaSmsRegistrant.notifyRegistrant(new AsyncResult(null, paramCdmaSmsMessage, null));
    }
  }
  
  public void cdmaOtaProvisionStatus(int paramInt1, int paramInt2)
  {
    mRil.processIndication(paramInt1);
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = paramInt2;
    mRil.unsljLogRet(1026, arrayOfInt);
    mRil.mOtaProvisionRegistrants.notifyRegistrants(new AsyncResult(null, arrayOfInt, null));
  }
  
  public void cdmaPrlChanged(int paramInt1, int paramInt2)
  {
    mRil.processIndication(paramInt1);
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = paramInt2;
    mRil.unsljLogRet(1032, arrayOfInt);
    mRil.mCdmaPrlChangedRegistrants.notifyRegistrants(new AsyncResult(null, arrayOfInt, null));
  }
  
  public void cdmaRuimSmsStorageFull(int paramInt)
  {
    mRil.processIndication(paramInt);
    mRil.unsljLog(1022);
    if (mRil.mIccSmsFullRegistrant != null) {
      mRil.mIccSmsFullRegistrant.notifyRegistrant();
    }
  }
  
  public void cdmaSubscriptionSourceChanged(int paramInt1, int paramInt2)
  {
    mRil.processIndication(paramInt1);
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = paramInt2;
    mRil.unsljLogRet(1031, arrayOfInt);
    mRil.mCdmaSubscriptionChangedRegistrants.notifyRegistrants(new AsyncResult(null, arrayOfInt, null));
  }
  
  public void cellInfoList(int paramInt, ArrayList<android.hardware.radio.V1_0.CellInfo> paramArrayList)
  {
    mRil.processIndication(paramInt);
    paramArrayList = RIL.convertHalCellInfoList(paramArrayList);
    mRil.unsljLogRet(1036, paramArrayList);
    mRil.mRilCellInfoListRegistrants.notifyRegistrants(new AsyncResult(null, paramArrayList, null));
  }
  
  public void cellInfoList_1_2(int paramInt, ArrayList<android.hardware.radio.V1_2.CellInfo> paramArrayList)
  {
    mRil.processIndication(paramInt);
    paramArrayList = RIL.convertHalCellInfoList_1_2(paramArrayList);
    mRil.unsljLogRet(1036, paramArrayList);
    mRil.mRilCellInfoListRegistrants.notifyRegistrants(new AsyncResult(null, paramArrayList, null));
  }
  
  public void currentLinkCapacityEstimate(int paramInt, LinkCapacityEstimate paramLinkCapacityEstimate)
  {
    mRil.processIndication(paramInt);
    paramLinkCapacityEstimate = RIL.convertHalLceData(paramLinkCapacityEstimate, mRil);
    mRil.unsljLogRet(1045, paramLinkCapacityEstimate);
    if (mRil.mLceInfoRegistrants != null) {
      mRil.mLceInfoRegistrants.notifyRegistrants(new AsyncResult(null, paramLinkCapacityEstimate, null));
    }
  }
  
  public void currentPhysicalChannelConfigs(int paramInt, ArrayList<android.hardware.radio.V1_2.PhysicalChannelConfig> paramArrayList)
  {
    ArrayList localArrayList = new ArrayList(paramArrayList.size());
    paramArrayList = paramArrayList.iterator();
    while (paramArrayList.hasNext())
    {
      android.hardware.radio.V1_2.PhysicalChannelConfig localPhysicalChannelConfig = (android.hardware.radio.V1_2.PhysicalChannelConfig)paramArrayList.next();
      switch (status)
      {
      default: 
        RIL localRIL = mRil;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unsupported CellConnectionStatus in PhysicalChannelConfig: ");
        localStringBuilder.append(status);
        localRIL.riljLoge(localStringBuilder.toString());
        paramInt = Integer.MAX_VALUE;
        break;
      case 2: 
        paramInt = 2;
        break;
      case 1: 
        paramInt = 1;
      }
      localArrayList.add(new android.telephony.PhysicalChannelConfig(paramInt, cellBandwidthDownlink));
    }
    mRil.unsljLogRet(1052, localArrayList);
    mRil.mPhysicalChannelConfigurationRegistrants.notifyRegistrants(new AsyncResult(null, localArrayList, null));
  }
  
  public void currentSignalStrength(int paramInt, android.hardware.radio.V1_0.SignalStrength paramSignalStrength)
  {
    mRil.processIndication(paramInt);
    paramSignalStrength = RIL.convertHalSignalStrength(paramSignalStrength);
    if (mRil.mSignalStrengthRegistrant != null) {
      mRil.mSignalStrengthRegistrant.notifyRegistrant(new AsyncResult(null, paramSignalStrength, null));
    }
  }
  
  public void currentSignalStrength_1_2(int paramInt, android.hardware.radio.V1_2.SignalStrength paramSignalStrength)
  {
    mRil.processIndication(paramInt);
    paramSignalStrength = RIL.convertHalSignalStrength_1_2(paramSignalStrength);
    if (mRil.mSignalStrengthRegistrant != null) {
      mRil.mSignalStrengthRegistrant.notifyRegistrant(new AsyncResult(null, paramSignalStrength, null));
    }
  }
  
  public void dataCallListChanged(int paramInt, ArrayList<SetupDataCallResult> paramArrayList)
  {
    mRil.processIndication(paramInt);
    mRil.unsljLogRet(1010, paramArrayList);
    mRil.mDataCallListChangedRegistrants.notifyRegistrants(new AsyncResult(null, paramArrayList, null));
  }
  
  public void enterEmergencyCallbackMode(int paramInt)
  {
    mRil.processIndication(paramInt);
    mRil.unsljLog(1024);
    if (mRil.mEmergencyCallbackModeRegistrant != null) {
      mRil.mEmergencyCallbackModeRegistrant.notifyRegistrant();
    }
  }
  
  public void exitEmergencyCallbackMode(int paramInt)
  {
    mRil.processIndication(paramInt);
    mRil.unsljLog(1033);
    mRil.mExitEmergencyCallbackModeRegistrants.notifyRegistrants();
  }
  
  public void hardwareConfigChanged(int paramInt, ArrayList<HardwareConfig> paramArrayList)
  {
    mRil.processIndication(paramInt);
    paramArrayList = RIL.convertHalHwConfigList(paramArrayList, mRil);
    mRil.unsljLogRet(1040, paramArrayList);
    mRil.mHardwareConfigChangeRegistrants.notifyRegistrants(new AsyncResult(null, paramArrayList, null));
  }
  
  public void imsNetworkStateChanged(int paramInt)
  {
    mRil.processIndication(paramInt);
    mRil.unsljLog(1037);
    RIL localRIL1 = mRil;
    StringBuilder localStringBuilder = new StringBuilder();
    RIL localRIL2 = mRil;
    localStringBuilder.append("[RIL] SMS ");
    localStringBuilder.append("RECV");
    localRIL1.asusEvtLog(localStringBuilder.toString());
    mRil.mImsNetworkStateChangedRegistrants.notifyRegistrants();
  }
  
  public void indicateRingbackTone(int paramInt, boolean paramBoolean)
  {
    mRil.processIndication(paramInt);
    mRil.unsljLogvRet(1029, Boolean.valueOf(paramBoolean));
    mRil.mRingbackToneRegistrants.notifyRegistrants(new AsyncResult(null, Boolean.valueOf(paramBoolean), null));
  }
  
  public void keepaliveStatus(int paramInt, android.hardware.radio.V1_1.KeepaliveStatus paramKeepaliveStatus)
  {
    mRil.processIndication(paramInt);
    RIL localRIL = mRil;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("handle=");
    localStringBuilder.append(sessionHandle);
    localStringBuilder.append(" code=");
    localStringBuilder.append(code);
    localRIL.unsljLogRet(1051, localStringBuilder.toString());
    paramKeepaliveStatus = new com.android.internal.telephony.dataconnection.KeepaliveStatus(sessionHandle, code);
    mRil.mNattKeepaliveStatusRegistrants.notifyRegistrants(new AsyncResult(null, paramKeepaliveStatus, null));
  }
  
  public void lceData(int paramInt, LceDataInfo paramLceDataInfo)
  {
    mRil.processIndication(paramInt);
    paramLceDataInfo = RIL.convertHalLceData(paramLceDataInfo, mRil);
    mRil.unsljLogRet(1045, paramLceDataInfo);
    if (mRil.mLceInfoRegistrants != null) {
      mRil.mLceInfoRegistrants.notifyRegistrants(new AsyncResult(null, paramLceDataInfo, null));
    }
  }
  
  public void modemReset(int paramInt, String paramString)
  {
    mRil.processIndication(paramInt);
    mRil.unsljLogRet(1047, paramString);
    mRil.writeMetricsModemRestartEvent(paramString);
    mRil.mModemResetRegistrants.notifyRegistrants(new AsyncResult(null, paramString, null));
  }
  
  public void networkScanResult(int paramInt, android.hardware.radio.V1_1.NetworkScanResult paramNetworkScanResult)
  {
    responseCellInfos(paramInt, paramNetworkScanResult);
  }
  
  public void networkScanResult_1_2(int paramInt, android.hardware.radio.V1_2.NetworkScanResult paramNetworkScanResult)
  {
    responseCellInfos_1_2(paramInt, paramNetworkScanResult);
  }
  
  public void networkStateChanged(int paramInt)
  {
    mRil.processIndication(paramInt);
    mRil.unsljLog(1002);
    mRil.mNetworkStateRegistrants.notifyRegistrants();
  }
  
  public void newBroadcastSms(int paramInt, ArrayList<Byte> paramArrayList)
  {
    mRil.processIndication(paramInt);
    byte[] arrayOfByte = RIL.arrayListToPrimitiveArray(paramArrayList);
    mRil.unsljLogvRet(1021, IccUtils.bytesToHexString(arrayOfByte));
    paramArrayList = mRil;
    StringBuilder localStringBuilder = new StringBuilder();
    RIL localRIL = mRil;
    localStringBuilder.append("[RIL] SMS ");
    localStringBuilder.append("CB");
    paramArrayList.asusEvtLog(localStringBuilder.toString());
    if (mRil.mGsmBroadcastSmsRegistrant != null) {
      mRil.mGsmBroadcastSmsRegistrant.notifyRegistrant(new AsyncResult(null, arrayOfByte, null));
    }
  }
  
  public void newSms(int paramInt, ArrayList<Byte> paramArrayList)
  {
    mRil.processIndication(paramInt);
    byte[] arrayOfByte = RIL.arrayListToPrimitiveArray(paramArrayList);
    mRil.unsljLog(1003);
    paramArrayList = mRil;
    StringBuilder localStringBuilder = new StringBuilder();
    RIL localRIL = mRil;
    localStringBuilder.append("[RIL] SMS ");
    localStringBuilder.append("RECV");
    paramArrayList.asusEvtLog(localStringBuilder.toString());
    mRil.writeMetricsNewSms(1, 1);
    paramArrayList = SmsMessage.newFromCMT(arrayOfByte);
    if (mRil.mGsmSmsRegistrant != null) {
      mRil.mGsmSmsRegistrant.notifyRegistrant(new AsyncResult(null, paramArrayList, null));
    }
  }
  
  public void newSmsOnSim(int paramInt1, int paramInt2)
  {
    mRil.processIndication(paramInt1);
    mRil.unsljLog(1005);
    if (mRil.mSmsOnSimRegistrant != null) {
      mRil.mSmsOnSimRegistrant.notifyRegistrant(new AsyncResult(null, Integer.valueOf(paramInt2), null));
    }
  }
  
  public void newSmsStatusReport(int paramInt, ArrayList<Byte> paramArrayList)
  {
    mRil.processIndication(paramInt);
    paramArrayList = RIL.arrayListToPrimitiveArray(paramArrayList);
    mRil.unsljLog(1004);
    if (mRil.mSmsStatusRegistrant != null) {
      mRil.mSmsStatusRegistrant.notifyRegistrant(new AsyncResult(null, paramArrayList, null));
    }
  }
  
  public void nitzTimeReceived(int paramInt, String paramString, long paramLong)
  {
    mRil.processIndication(paramInt);
    mRil.unsljLogRet(1008, paramString);
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = paramString;
    arrayOfObject[1] = Long.valueOf(paramLong);
    if (SystemProperties.getBoolean("telephony.test.ignore.nitz", false))
    {
      mRil.riljLog("ignoring UNSOL_NITZ_TIME_RECEIVED");
    }
    else
    {
      if (mRil.mNITZTimeRegistrant != null) {
        mRil.mNITZTimeRegistrant.notifyRegistrant(new AsyncResult(null, arrayOfObject, null));
      }
      mRil.mLastNITZTimeInfo = arrayOfObject;
    }
  }
  
  public void onSupplementaryServiceIndication(int paramInt, StkCcUnsolSsResult paramStkCcUnsolSsResult)
  {
    mRil.processIndication(paramInt);
    SsData localSsData = new SsData();
    serviceType = localSsData.ServiceTypeFromRILInt(serviceType);
    requestType = localSsData.RequestTypeFromRILInt(requestType);
    teleserviceType = localSsData.TeleserviceTypeFromRILInt(teleserviceType);
    serviceClass = serviceClass;
    result = result;
    boolean bool = serviceType.isTypeCF();
    int i = 0;
    paramInt = 0;
    Object localObject1;
    Object localObject2;
    if ((bool) && (requestType.isTypeInterrogation()))
    {
      paramStkCcUnsolSsResult = (CfData)cfData.get(0);
      i = cfInfo.size();
      cfInfo = new CallForwardInfo[i];
      while (paramInt < i)
      {
        localObject1 = (android.hardware.radio.V1_0.CallForwardInfo)cfInfo.get(paramInt);
        cfInfo[paramInt] = new CallForwardInfo();
        cfInfo[paramInt].status = status;
        cfInfo[paramInt].reason = reason;
        cfInfo[paramInt].serviceClass = serviceClass;
        cfInfo[paramInt].toa = toa;
        cfInfo[paramInt].number = number;
        cfInfo[paramInt].timeSeconds = timeSeconds;
        localObject2 = mRil;
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("[SS Data] CF Info ");
        ((StringBuilder)localObject1).append(paramInt);
        ((StringBuilder)localObject1).append(" : ");
        ((StringBuilder)localObject1).append(cfInfo[paramInt]);
        ((RIL)localObject2).riljLog(((StringBuilder)localObject1).toString());
        paramInt++;
      }
    }
    else
    {
      localObject2 = (SsInfoData)ssInfo.get(0);
      int j = ssInfo.size();
      ssInfo = new int[j];
      for (paramInt = i; paramInt < j; paramInt++)
      {
        ssInfo[paramInt] = ((Integer)ssInfo.get(paramInt)).intValue();
        paramStkCcUnsolSsResult = mRil;
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("[SS Data] SS Info ");
        ((StringBuilder)localObject1).append(paramInt);
        ((StringBuilder)localObject1).append(" : ");
        ((StringBuilder)localObject1).append(ssInfo[paramInt]);
        paramStkCcUnsolSsResult.riljLog(((StringBuilder)localObject1).toString());
      }
    }
    mRil.unsljLogRet(1043, localSsData);
    if (mRil.mSsRegistrant != null) {
      mRil.mSsRegistrant.notifyRegistrant(new AsyncResult(null, localSsData, null));
    }
  }
  
  public void onUssd(int paramInt1, int paramInt2, String paramString)
  {
    mRil.processIndication(paramInt1);
    Object localObject = mRil;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("");
    localStringBuilder.append(paramInt2);
    ((RIL)localObject).unsljLogMore(1006, localStringBuilder.toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("");
    ((StringBuilder)localObject).append(paramInt2);
    localObject = ((StringBuilder)localObject).toString();
    if (mRil.mUSSDRegistrant != null) {
      mRil.mUSSDRegistrant.notifyRegistrant(new AsyncResult(null, new String[] { localObject, paramString }, null));
    }
  }
  
  public void pcoData(int paramInt, PcoDataInfo paramPcoDataInfo)
  {
    mRil.processIndication(paramInt);
    paramPcoDataInfo = new PcoData(cid, bearerProto, pcoId, RIL.arrayListToPrimitiveArray(contents));
    mRil.unsljLogRet(1046, paramPcoDataInfo);
    mRil.mPcoDataRegistrants.notifyRegistrants(new AsyncResult(null, paramPcoDataInfo, null));
  }
  
  public void radioCapabilityIndication(int paramInt, RadioCapability paramRadioCapability)
  {
    mRil.processIndication(paramInt);
    paramRadioCapability = RIL.convertHalRadioCapability(paramRadioCapability, mRil);
    mRil.unsljLogRet(1042, paramRadioCapability);
    mRil.mPhoneRadioCapabilityChangedRegistrants.notifyRegistrants(new AsyncResult(null, paramRadioCapability, null));
  }
  
  public void radioStateChanged(int paramInt1, int paramInt2)
  {
    mRil.processIndication(paramInt1);
    CommandsInterface.RadioState localRadioState = getRadioStateFromInt(paramInt2);
    RIL localRIL = mRil;
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("radioStateChanged: ");
    ((StringBuilder)localObject).append(localRadioState);
    localRIL.unsljLogMore(1000, ((StringBuilder)localObject).toString());
    if ((localRadioState.isOn()) && (mRil.getProcessStep() == RIL.RejectStep.REJECT_STEP_START))
    {
      mRil.setProcessStep(RIL.RejectStep.REJECT_STEP_RADIO_ON);
      paramInt1 = SubscriptionManager.getPhoneId(mRil.getSubId());
      if ((paramInt1 == 0) && (SystemProperties.get("gsm.vendor.radio.nwrej_sim0", "0").equals("1"))) {
        SystemProperties.set("gsm.vendor.radio.nwrej_sim0", "0");
      }
      if ((paramInt1 == 1) && (SystemProperties.get("gsm.vendor.radio.nwrej_sim1", "0").equals("1"))) {
        SystemProperties.set("gsm.vendor.radio.nwrej_sim1", "0");
      }
    }
    localObject = mRil.getDnoHandler();
    if (localObject != null) {
      ((DnoHandler)localObject).setRadioState(localRadioState);
    }
    mRil.setRadioState(localRadioState);
  }
  
  public void resendIncallMute(int paramInt)
  {
    mRil.processIndication(paramInt);
    mRil.unsljLog(1030);
    mRil.mResendIncallMuteRegistrants.notifyRegistrants();
  }
  
  public void restrictedStateChanged(int paramInt1, int paramInt2)
  {
    mRil.processIndication(paramInt1);
    mRil.unsljLogvRet(1023, Integer.valueOf(paramInt2));
    if (mRil.mRestrictedStateRegistrant != null) {
      mRil.mRestrictedStateRegistrant.notifyRegistrant(new AsyncResult(null, Integer.valueOf(paramInt2), null));
    }
  }
  
  public void rilConnected(int paramInt)
  {
    mRil.processIndication(paramInt);
    mRil.unsljLog(1034);
    mRil.setRadioPower(false, null);
    mRil.setCdmaSubscriptionSource(mRil.mCdmaSubscription, null);
    mRil.setCellInfoListRate();
    mRil.notifyRegistrantsRilConnectionChanged(15);
  }
  
  public void simRefresh(int paramInt, SimRefreshResult paramSimRefreshResult)
  {
    mRil.processIndication(paramInt);
    IccRefreshResponse localIccRefreshResponse = new IccRefreshResponse();
    refreshResult = type;
    efId = efId;
    aid = aid;
    mRil.unsljLogRet(1017, localIccRefreshResponse);
    mRil.mIccRefreshRegistrants.notifyRegistrants(new AsyncResult(null, localIccRefreshResponse, null));
  }
  
  public void simSmsStorageFull(int paramInt)
  {
    mRil.processIndication(paramInt);
    mRil.unsljLog(1016);
    if (mRil.mIccSmsFullRegistrant != null) {
      mRil.mIccSmsFullRegistrant.notifyRegistrant();
    }
  }
  
  public void simStatusChanged(int paramInt)
  {
    mRil.processIndication(paramInt);
    mRil.unsljLog(1019);
    mRil.mIccStatusChangedRegistrants.notifyRegistrants();
  }
  
  public void srvccStateNotify(int paramInt1, int paramInt2)
  {
    mRil.processIndication(paramInt1);
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = paramInt2;
    mRil.unsljLogRet(1039, arrayOfInt);
    mRil.writeMetricsSrvcc(paramInt2);
    mRil.mSrvccStateRegistrants.notifyRegistrants(new AsyncResult(null, arrayOfInt, null));
  }
  
  public void stkCallControlAlphaNotify(int paramInt, String paramString)
  {
    mRil.processIndication(paramInt);
    mRil.unsljLogRet(1044, paramString);
    if (mRil.mCatCcAlphaRegistrant != null) {
      mRil.mCatCcAlphaRegistrant.notifyRegistrant(new AsyncResult(null, paramString, null));
    }
  }
  
  public void stkCallSetup(int paramInt, long paramLong)
  {
    mRil.processIndication(paramInt);
    mRil.unsljLogRet(1015, Long.valueOf(paramLong));
    if (mRil.mCatCallSetUpRegistrant != null) {
      mRil.mCatCallSetUpRegistrant.notifyRegistrant(new AsyncResult(null, Long.valueOf(paramLong), null));
    }
  }
  
  public void stkEventNotify(int paramInt, String paramString)
  {
    mRil.processIndication(paramInt);
    mRil.unsljLog(1014);
    if (mRil.mCatEventRegistrant != null) {
      mRil.mCatEventRegistrant.notifyRegistrant(new AsyncResult(null, paramString, null));
    }
  }
  
  public void stkProactiveCommand(int paramInt, String paramString)
  {
    mRil.processIndication(paramInt);
    mRil.unsljLog(1013);
    if (mRil.mCatProCmdRegistrant != null) {
      mRil.mCatProCmdRegistrant.notifyRegistrant(new AsyncResult(null, paramString, null));
    }
  }
  
  public void stkSessionEnd(int paramInt)
  {
    mRil.processIndication(paramInt);
    mRil.unsljLog(1012);
    if (mRil.mCatSessionEndRegistrant != null) {
      mRil.mCatSessionEndRegistrant.notifyRegistrant(new AsyncResult(null, null, null));
    }
  }
  
  public void subscriptionStatusChanged(int paramInt, boolean paramBoolean)
  {
    mRil.processIndication(paramInt);
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = paramBoolean;
    mRil.unsljLogRet(1038, arrayOfInt);
    mRil.mSubscriptionStatusRegistrants.notifyRegistrants(new AsyncResult(null, arrayOfInt, null));
  }
  
  public void suppSvcNotify(int paramInt, SuppSvcNotification paramSuppSvcNotification)
  {
    mRil.processIndication(paramInt);
    SuppServiceNotification localSuppServiceNotification = new SuppServiceNotification();
    notificationType = isMT;
    code = code;
    index = index;
    type = type;
    number = number;
    mRil.unsljLogRet(1011, localSuppServiceNotification);
    if (mRil.mSsnRegistrant != null) {
      mRil.mSsnRegistrant.notifyRegistrant(new AsyncResult(null, localSuppServiceNotification, null));
    }
  }
  
  public void voiceRadioTechChanged(int paramInt1, int paramInt2)
  {
    mRil.processIndication(paramInt1);
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = paramInt2;
    mRil.unsljLogRet(1035, arrayOfInt);
    DnoHandler localDnoHandler = mRil.getDnoHandler();
    if (localDnoHandler != null)
    {
      if (((ServiceState.isGsm(paramInt2)) && (!ServiceState.isGsm(localDnoHandler.getVoiceTech()))) || ((!ServiceState.isGsm(paramInt2)) && (ServiceState.isGsm(localDnoHandler.getVoiceTech())))) {
        localDnoHandler.ril_snapshot_clear_all();
      }
      localDnoHandler.setVoiceTech(paramInt2);
    }
    mRil.mVoiceRadioTechChangedRegistrants.notifyRegistrants(new AsyncResult(null, arrayOfInt, null));
  }
}
