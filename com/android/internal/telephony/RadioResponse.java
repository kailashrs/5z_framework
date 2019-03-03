package com.android.internal.telephony;

import android.content.Context;
import android.hardware.radio.V1_0.ActivityStatsInfo;
import android.hardware.radio.V1_0.AppStatus;
import android.hardware.radio.V1_0.Carrier;
import android.hardware.radio.V1_0.CarrierRestrictions;
import android.hardware.radio.V1_0.CdmaBroadcastSmsConfigInfo;
import android.hardware.radio.V1_0.GsmBroadcastSmsConfigInfo;
import android.hardware.radio.V1_0.HardwareConfig;
import android.hardware.radio.V1_0.LastCallFailCauseInfo;
import android.hardware.radio.V1_0.LceDataInfo;
import android.hardware.radio.V1_0.LceStatusInfo;
import android.hardware.radio.V1_0.NeighboringCell;
import android.hardware.radio.V1_0.RadioCapability;
import android.hardware.radio.V1_0.RadioResponseInfo;
import android.hardware.radio.V1_0.SendSmsResult;
import android.hardware.radio.V1_0.SetupDataCallResult;
import android.hardware.radio.V1_0.UusInfo;
import android.hardware.radio.V1_2.IRadioResponse.Stub;
import android.os.AsyncResult;
import android.os.Message;
import android.os.Registrant;
import android.os.RegistrantList;
import android.os.SystemClock;
import android.service.carrier.CarrierIdentifier;
import android.telephony.ModemActivityInfo;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneNumberUtils;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import com.android.internal.telephony.gsm.SmsBroadcastConfigInfo;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.IccCardApplicationStatus.AppState;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccCardStatus.CardState;
import com.android.internal.telephony.uicc.IccUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RadioResponse
  extends IRadioResponse.Stub
{
  private static final int CDMA_BROADCAST_SMS_NO_OF_SERVICE_CATEGORIES = 31;
  private static final int CDMA_BSI_NO_OF_INTS_STRUCT = 3;
  private static final int DNO_SETUP_PDP_RETRY_DELAY = 10000;
  RIL mRil;
  
  public RadioResponse(RIL paramRIL)
  {
    mRil = paramRIL;
  }
  
  private IccCardStatus convertHalCardStatus(android.hardware.radio.V1_0.CardStatus paramCardStatus)
  {
    DnoHandler localDnoHandler = mRil.getDnoHandler();
    IccCardStatus localIccCardStatus = new IccCardStatus();
    localIccCardStatus.setCardState(cardState);
    if (localDnoHandler != null) {
      localDnoHandler.setCardState(mCardState);
    }
    if ((mCardState == IccCardStatus.CardState.CARDSTATE_ABSENT) && (RIL.RejectStep.REJECT_STEP_RADIO_ON == mRil.getProcessStep()))
    {
      mRil.setProcessStep(RIL.RejectStep.REJECT_STEP_NONE);
      mRil.riljLog("responseIccCardStatus: reset by sim removed");
    }
    localIccCardStatus.setUniversalPinState(universalPinState);
    mGsmUmtsSubscriptionAppIndex = gsmUmtsSubscriptionAppIndex;
    mCdmaSubscriptionAppIndex = cdmaSubscriptionAppIndex;
    mImsSubscriptionAppIndex = imsSubscriptionAppIndex;
    int i = applications.size();
    int j = i;
    if (i > 8) {
      j = 8;
    }
    Object localObject1 = mRil;
    Object localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("responseIccCardStatus, numApplications: ");
    ((StringBuilder)localObject2).append(j);
    ((StringBuilder)localObject2).append(", mGsmUmtsSubscriptionAppIndex: ");
    ((StringBuilder)localObject2).append(mGsmUmtsSubscriptionAppIndex);
    ((StringBuilder)localObject2).append(", mCdmaSubscriptionAppIndex: ");
    ((StringBuilder)localObject2).append(mCdmaSubscriptionAppIndex);
    ((StringBuilder)localObject2).append(", mImsSubscriptionAppIndex: ");
    ((StringBuilder)localObject2).append(mImsSubscriptionAppIndex);
    ((RIL)localObject1).riljLog(((StringBuilder)localObject2).toString());
    mApplications = new IccCardApplicationStatus[j];
    int k = 0;
    for (i = 0; i < j; i++)
    {
      localObject2 = (AppStatus)applications.get(i);
      localObject1 = new IccCardApplicationStatus();
      app_type = ((IccCardApplicationStatus)localObject1).AppTypeFromRILInt(appType);
      app_state = ((IccCardApplicationStatus)localObject1).AppStateFromRILInt(appState);
      perso_substate = ((IccCardApplicationStatus)localObject1).PersoSubstateFromRILInt(persoSubstate);
      aid = aidPtr;
      app_label = appLabelPtr;
      pin1_replaced = pin1Replaced;
      pin1 = ((IccCardApplicationStatus)localObject1).PinStateFromRILInt(pin1);
      pin2 = ((IccCardApplicationStatus)localObject1).PinStateFromRILInt(pin2);
      mApplications[i] = localObject1;
      if (app_state == IccCardApplicationStatus.AppState.APPSTATE_PIN) {
        k = 1;
      }
      RIL localRIL = mRil;
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("IccCardApplicationStatus ");
      ((StringBuilder)localObject2).append(i);
      ((StringBuilder)localObject2).append(":");
      ((StringBuilder)localObject2).append(((IccCardApplicationStatus)localObject1).toString());
      localRIL.riljLog(((StringBuilder)localObject2).toString());
    }
    if (k != 0) {
      localDnoHandler.setPinLock(true);
    } else {
      localDnoHandler.setPinLock(false);
    }
    return localIccCardStatus;
  }
  
  private int convertHalKeepaliveStatusCode(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      RIL localRIL = mRil;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Invalid Keepalive Status");
      localStringBuilder.append(paramInt);
      localRIL.riljLog(localStringBuilder.toString());
      return -1;
    case 2: 
      return 2;
    case 1: 
      return 1;
    }
    return 0;
  }
  
  private static String convertOpertatorInfoToString(int paramInt)
  {
    if (paramInt == 0) {
      return "unknown";
    }
    if (paramInt == 1) {
      return "available";
    }
    if (paramInt == 2) {
      return "current";
    }
    if (paramInt == 3) {
      return "forbidden";
    }
    return "";
  }
  
  private void responseActivityData(RadioResponseInfo paramRadioResponseInfo, ActivityStatsInfo paramActivityStatsInfo)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      int i = error;
      int j = 0;
      if (i == 0)
      {
        i = sleepModeTimeMs;
        int k = idleModeTimeMs;
        int[] arrayOfInt = new int[5];
        while (j < 5)
        {
          arrayOfInt[j] = txmModetimeMs[j];
          j++;
        }
        j = rxModeTimeMs;
        paramActivityStatsInfo = new ModemActivityInfo(SystemClock.elapsedRealtime(), i, k, arrayOfInt, j, 0);
      }
      else
      {
        paramActivityStatsInfo = new ModemActivityInfo(0L, 0, 0, new int[5], 0, 0);
        error = 0;
      }
      sendMessageResponse(mResult, paramActivityStatsInfo);
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, paramActivityStatsInfo);
    }
  }
  
  private void responseCallForwardInfo(RadioResponseInfo paramRadioResponseInfo, ArrayList<android.hardware.radio.V1_0.CallForwardInfo> paramArrayList)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      CallForwardInfo[] arrayOfCallForwardInfo = new CallForwardInfo[paramArrayList.size()];
      for (int i = 0; i < paramArrayList.size(); i++)
      {
        arrayOfCallForwardInfo[i] = new CallForwardInfo();
        status = getstatus;
        reason = getreason;
        serviceClass = getserviceClass;
        toa = gettoa;
        number = getnumber;
        timeSeconds = gettimeSeconds;
      }
      if (error == 0) {
        sendMessageResponse(mResult, arrayOfCallForwardInfo);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, arrayOfCallForwardInfo);
    }
  }
  
  private void responseCarrierIdentifiers(RadioResponseInfo paramRadioResponseInfo, boolean paramBoolean, CarrierRestrictions paramCarrierRestrictions)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      ArrayList localArrayList = new ArrayList();
      for (int i = 0;; i++)
      {
        Object localObject1 = paramCarrierRestrictions;
        if (i >= allowedCarriers.size()) {
          break;
        }
        String str1 = allowedCarriers.get(i)).mcc;
        String str2 = allowedCarriers.get(i)).mnc;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        int j = allowedCarriers.get(i)).matchType;
        localObject1 = allowedCarriers.get(i)).matchData;
        Object localObject6;
        Object localObject7;
        Object localObject8;
        if (j == 1)
        {
          localObject6 = localObject4;
          localObject7 = localObject3;
          localObject8 = localObject1;
        }
        for (;;)
        {
          break;
          if (j == 2)
          {
            localObject8 = localObject2;
            localObject7 = localObject1;
            localObject6 = localObject4;
          }
          else if (j == 3)
          {
            localObject8 = localObject2;
            localObject7 = localObject3;
            localObject6 = localObject1;
          }
          else
          {
            localObject8 = localObject2;
            localObject7 = localObject3;
            localObject6 = localObject4;
            if (j == 4)
            {
              localObject8 = localObject2;
              localObject7 = localObject3;
              localObject6 = localObject4;
              localObject5 = localObject1;
            }
          }
        }
        localArrayList.add(new CarrierIdentifier(str1, str2, localObject8, localObject7, localObject6, localObject5));
      }
      if (error == 0) {
        sendMessageResponse(mResult, localArrayList);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, localArrayList);
    }
  }
  
  private void responseCdmaBroadcastConfig(RadioResponseInfo paramRadioResponseInfo, ArrayList<CdmaBroadcastSmsConfigInfo> paramArrayList)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      int i = paramArrayList.size();
      int j = 0;
      Object localObject;
      if (i == 0)
      {
        localObject = new int[94];
        localObject[0] = 31;
        for (i = 1; i < 94; i += 3)
        {
          localObject[(i + 0)] = (i / 3);
          localObject[(i + 1)] = 1;
          localObject[(i + 2)] = 0;
        }
      }
      else
      {
        int[] arrayOfInt = new int[i * 3 + 1];
        arrayOfInt[0] = i;
        for (i = 1;; i += 3)
        {
          localObject = arrayOfInt;
          if (j >= paramArrayList.size()) {
            break;
          }
          arrayOfInt[i] = getserviceCategory;
          arrayOfInt[(i + 1)] = getlanguage;
          arrayOfInt[(i + 2)] = getselected;
          j++;
        }
      }
      if (error == 0) {
        sendMessageResponse(mResult, localObject);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, localObject);
    }
  }
  
  private void responseCellInfoList(RadioResponseInfo paramRadioResponseInfo, ArrayList<android.hardware.radio.V1_0.CellInfo> paramArrayList)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      paramArrayList = RIL.convertHalCellInfoList(paramArrayList);
      if (error == 0) {
        sendMessageResponse(mResult, paramArrayList);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, paramArrayList);
    }
  }
  
  private void responseCellInfoList_1_2(RadioResponseInfo paramRadioResponseInfo, ArrayList<android.hardware.radio.V1_2.CellInfo> paramArrayList)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      paramArrayList = RIL.convertHalCellInfoList_1_2(paramArrayList);
      if (error == 0) {
        sendMessageResponse(mResult, paramArrayList);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, paramArrayList);
    }
  }
  
  private void responseCellList(RadioResponseInfo paramRadioResponseInfo, ArrayList<NeighboringCell> paramArrayList)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      ArrayList localArrayList = new ArrayList();
      int[] arrayOfInt = SubscriptionManager.getSubId(mRil.mPhoneId.intValue());
      TelephonyManager localTelephonyManager = (TelephonyManager)mRil.mContext.getSystemService("phone");
      int i = 0;
      int j = localTelephonyManager.getDataNetworkType(arrayOfInt[0]);
      if (j != 0) {
        while (i < paramArrayList.size())
        {
          localArrayList.add(new NeighboringCellInfo(getrssi, getcid, j));
          i++;
        }
      }
      if (error == 0) {
        sendMessageResponse(mResult, localArrayList);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, localArrayList);
    }
  }
  
  private void responseCurrentCalls(RadioResponseInfo paramRadioResponseInfo, ArrayList<android.hardware.radio.V1_0.Call> paramArrayList)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      int i = paramArrayList.size();
      ArrayList localArrayList = new ArrayList(i);
      for (int j = 0; j < i; j++)
      {
        DriverCall localDriverCall = new DriverCall();
        state = DriverCall.stateFromCLCC(getstate);
        index = getindex;
        TOA = gettoa;
        isMpty = getisMpty;
        isMT = getisMT;
        als = getals;
        isVoice = getisVoice;
        isVoicePrivacy = getisVoicePrivacy;
        number = getnumber;
        numberPresentation = DriverCall.presentationFromCLIP(getnumberPresentation);
        name = getname;
        namePresentation = DriverCall.presentationFromCLIP(getnamePresentation);
        if (getuusInfo.size() == 1)
        {
          uusInfo = new UUSInfo();
          uusInfo.setType(getuusInfo.get(0)).uusType);
          uusInfo.setDcs(getuusInfo.get(0)).uusDcs);
          if (!TextUtils.isEmpty(getuusInfo.get(0)).uusData))
          {
            localObject = getuusInfo.get(0)).uusData.getBytes();
            uusInfo.setUserData((byte[])localObject);
          }
          else
          {
            mRil.riljLog("responseCurrentCalls: uusInfo data is null or empty");
          }
          mRil.riljLogv(String.format("Incoming UUS : type=%d, dcs=%d, length=%d", new Object[] { Integer.valueOf(uusInfo.getType()), Integer.valueOf(uusInfo.getDcs()), Integer.valueOf(uusInfo.getUserData().length) }));
          Object localObject = mRil;
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Incoming UUS : data (hex): ");
          localStringBuilder.append(IccUtils.bytesToHexString(uusInfo.getUserData()));
          ((RIL)localObject).riljLogv(localStringBuilder.toString());
        }
        else
        {
          mRil.riljLogv("Incoming UUS : NOT present!");
        }
        number = PhoneNumberUtils.stringFromStringAndTOA(number, TOA);
        localArrayList.add(localDriverCall);
        if (isVoicePrivacy)
        {
          mRil.mVoicePrivacyOnRegistrants.notifyRegistrants();
          mRil.riljLog("InCall VoicePrivacy is enabled");
        }
        else
        {
          mRil.mVoicePrivacyOffRegistrants.notifyRegistrants();
          mRil.riljLog("InCall VoicePrivacy is disabled");
        }
      }
      Collections.sort(localArrayList);
      if ((i == 0) && (mRil.mTestingEmergencyCall.getAndSet(false)) && (mRil.mEmergencyCallbackModeRegistrant != null))
      {
        mRil.riljLog("responseCurrentCalls: call ended, testing emergency call, notify ECM Registrants");
        mRil.mEmergencyCallbackModeRegistrant.notifyRegistrant();
      }
      if (error == 0) {
        sendMessageResponse(mResult, localArrayList);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, localArrayList);
    }
  }
  
  private void responseCurrentCalls_1_2(RadioResponseInfo paramRadioResponseInfo, ArrayList<android.hardware.radio.V1_2.Call> paramArrayList)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      int i = paramArrayList.size();
      ArrayList localArrayList = new ArrayList(i);
      Object localObject1 = mRil.getDnoHandler();
      int j = 1;
      if (localObject1 != null)
      {
        boolean bool;
        if (i > 0) {
          bool = true;
        } else {
          bool = false;
        }
        ((DnoHandler)localObject1).setHasVoiceCall(bool);
      }
      localObject1 = new int[6];
      Arrays.fill((int[])localObject1, 6);
      Object localObject2;
      Object localObject3;
      for (int k = 0; k < i; k++)
      {
        localObject2 = new DriverCall();
        int m = getbase.state;
        state = DriverCall.stateFromCLCC(m);
        index = getbase.index;
        if ((index >= 0) && (index < 6)) {
          localObject1[index] = m;
        }
        TOA = getbase.toa;
        isMpty = getbase.isMpty;
        isMT = getbase.isMT;
        als = getbase.als;
        isVoice = getbase.isVoice;
        isVoicePrivacy = getbase.isVoicePrivacy;
        number = getbase.number;
        numberPresentation = DriverCall.presentationFromCLIP(getbase.numberPresentation);
        name = getbase.name;
        namePresentation = DriverCall.presentationFromCLIP(getbase.namePresentation);
        if (getbase.uusInfo.size() == j)
        {
          uusInfo = new UUSInfo();
          uusInfo.setType(getbase.uusInfo.get(0)).uusType);
          uusInfo.setDcs(getbase.uusInfo.get(0)).uusDcs);
          if (!TextUtils.isEmpty(getbase.uusInfo.get(0)).uusData))
          {
            localObject3 = getbase.uusInfo.get(0)).uusData.getBytes();
            uusInfo.setUserData((byte[])localObject3);
          }
          else
          {
            mRil.riljLog("responseCurrentCalls: uusInfo data is null or empty");
          }
          localObject3 = mRil;
          int n = uusInfo.getType();
          m = uusInfo.getDcs();
          j = 1;
          ((RIL)localObject3).riljLogv(String.format("Incoming UUS : type=%d, dcs=%d, length=%d", new Object[] { Integer.valueOf(n), Integer.valueOf(m), Integer.valueOf(uusInfo.getUserData().length) }));
          RIL localRIL = mRil;
          localObject3 = new StringBuilder();
          ((StringBuilder)localObject3).append("Incoming UUS : data (hex): ");
          ((StringBuilder)localObject3).append(IccUtils.bytesToHexString(uusInfo.getUserData()));
          localRIL.riljLogv(((StringBuilder)localObject3).toString());
        }
        else
        {
          mRil.riljLogv("Incoming UUS : NOT present!");
        }
        number = PhoneNumberUtils.stringFromStringAndTOA(number, TOA);
        audioQuality = getaudioQuality;
        localArrayList.add(localObject2);
        if (isVoicePrivacy)
        {
          mRil.mVoicePrivacyOnRegistrants.notifyRegistrants();
          mRil.riljLog("InCall VoicePrivacy is enabled");
        }
        else
        {
          mRil.mVoicePrivacyOffRegistrants.notifyRegistrants();
          mRil.riljLog("InCall VoicePrivacy is disabled");
        }
      }
      while (j < 6)
      {
        if ((mRil.mCallCache[j] != localObject1[j]) && (localObject1[j] >= 0) && (localObject1[j] <= 6))
        {
          mRil.mCallCache[j] = localObject1[j];
          localObject2 = mRil;
          paramArrayList = new StringBuilder();
          localObject3 = mRil;
          paramArrayList.append("[RIL] CALL ");
          paramArrayList.append(j);
          paramArrayList.append(" ");
          localObject3 = mRil;
          paramArrayList.append(RIL.STR_CALL_STATE[mRil.mCallCache[j]]);
          ((RIL)localObject2).asusEvtLog(paramArrayList.toString());
        }
        j++;
      }
      Collections.sort(localArrayList);
      if ((i == 0) && (mRil.mTestingEmergencyCall.getAndSet(false)) && (mRil.mEmergencyCallbackModeRegistrant != null))
      {
        mRil.riljLog("responseCurrentCalls: call ended, testing emergency call, notify ECM Registrants");
        mRil.mEmergencyCallbackModeRegistrant.notifyRegistrant();
      }
      if (error == 0) {
        sendMessageResponse(mResult, localArrayList);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, localArrayList);
    }
  }
  
  private void responseDataCallList(RadioResponseInfo paramRadioResponseInfo, ArrayList<SetupDataCallResult> paramArrayList)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      int i = 0;
      DnoHandler localDnoHandler = mRil.getDnoHandler();
      Object localObject = paramArrayList.iterator();
      while (((Iterator)localObject).hasNext())
      {
        int j = i;
        if (nextactive != 0) {
          j = i + 1;
        }
        i = j;
      }
      localObject = mRil;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("responseDataCallList activenum=");
      localStringBuilder.append(i);
      ((RIL)localObject).riljLog(localStringBuilder.toString());
      if (localDnoHandler != null) {
        localDnoHandler.setNumDataCalls(i);
      }
      if (error == 0) {
        sendMessageResponse(mResult, paramArrayList);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, paramArrayList);
    }
  }
  
  private void responseGmsBroadcastConfig(RadioResponseInfo paramRadioResponseInfo, ArrayList<GsmBroadcastSmsConfigInfo> paramArrayList)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      ArrayList localArrayList = new ArrayList();
      for (int i = 0; i < paramArrayList.size(); i++) {
        localArrayList.add(new SmsBroadcastConfigInfo(getfromServiceId, gettoServiceId, getfromCodeScheme, gettoCodeScheme, getselected));
      }
      if (error == 0) {
        sendMessageResponse(mResult, localArrayList);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, localArrayList);
    }
  }
  
  private void responseHardwareConfig(RadioResponseInfo paramRadioResponseInfo, ArrayList<HardwareConfig> paramArrayList)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      paramArrayList = RIL.convertHalHwConfigList(paramArrayList, mRil);
      if (error == 0) {
        sendMessageResponse(mResult, paramArrayList);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, paramArrayList);
    }
  }
  
  private void responseICC_IOBase64(RadioResponseInfo paramRadioResponseInfo, android.hardware.radio.V1_0.IccIoResult paramIccIoResult)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      int i = sw1;
      int j = sw2;
      if (!simResponse.equals("")) {
        paramIccIoResult = Base64.decode(simResponse, 0);
      } else {
        paramIccIoResult = (byte[])null;
      }
      paramIccIoResult = new com.android.internal.telephony.uicc.IccIoResult(i, j, paramIccIoResult);
      if (error == 0) {
        sendMessageResponse(mResult, paramIccIoResult);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, paramIccIoResult);
    }
  }
  
  private void responseIccCardStatus(RadioResponseInfo paramRadioResponseInfo, android.hardware.radio.V1_0.CardStatus paramCardStatus)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      IccCardStatus localIccCardStatus = convertHalCardStatus(paramCardStatus);
      paramCardStatus = mRil;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("responseIccCardStatus: from HIDL: ");
      localStringBuilder.append(localIccCardStatus);
      paramCardStatus.riljLog(localStringBuilder.toString());
      if (error == 0) {
        sendMessageResponse(mResult, localIccCardStatus);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, localIccCardStatus);
    }
  }
  
  private void responseIccCardStatus_1_2(RadioResponseInfo paramRadioResponseInfo, android.hardware.radio.V1_2.CardStatus paramCardStatus)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      IccCardStatus localIccCardStatus = convertHalCardStatus(base);
      physicalSlotIndex = physicalSlotId;
      atr = atr;
      iccid = iccid;
      paramCardStatus = mRil;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("responseIccCardStatus: from HIDL: ");
      localStringBuilder.append(localIccCardStatus);
      paramCardStatus.riljLog(localStringBuilder.toString());
      if (error == 0) {
        sendMessageResponse(mResult, localIccCardStatus);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, localIccCardStatus);
    }
  }
  
  private void responseIccIo(RadioResponseInfo paramRadioResponseInfo, android.hardware.radio.V1_0.IccIoResult paramIccIoResult)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      paramIccIoResult = new com.android.internal.telephony.uicc.IccIoResult(sw1, sw2, simResponse);
      if (error == 0) {
        sendMessageResponse(mResult, paramIccIoResult);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, paramIccIoResult);
    }
  }
  
  private void responseIntArrayList(RadioResponseInfo paramRadioResponseInfo, ArrayList<Integer> paramArrayList)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      int[] arrayOfInt = new int[paramArrayList.size()];
      for (int i = 0; i < paramArrayList.size(); i++) {
        arrayOfInt[i] = ((Integer)paramArrayList.get(i)).intValue();
      }
      if (error == 0) {
        sendMessageResponse(mResult, arrayOfInt);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, arrayOfInt);
    }
  }
  
  private void responseInts(RadioResponseInfo paramRadioResponseInfo, int... paramVarArgs)
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; i < paramVarArgs.length; i++) {
      localArrayList.add(Integer.valueOf(paramVarArgs[i]));
    }
    responseIntArrayList(paramRadioResponseInfo, localArrayList);
  }
  
  private void responseLastCallFailCauseInfo(RadioResponseInfo paramRadioResponseInfo, LastCallFailCauseInfo paramLastCallFailCauseInfo)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      LastCallFailCause localLastCallFailCause = new LastCallFailCause();
      causeCode = causeCode;
      vendorCause = vendorCause;
      if (error == 0) {
        sendMessageResponse(mResult, localLastCallFailCause);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, localLastCallFailCause);
    }
  }
  
  private void responseLceData(RadioResponseInfo paramRadioResponseInfo, LceDataInfo paramLceDataInfo)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      paramLceDataInfo = RIL.convertHalLceData(paramLceDataInfo, mRil);
      if (error == 0) {
        sendMessageResponse(mResult, paramLceDataInfo);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, paramLceDataInfo);
    }
  }
  
  private void responseLceStatus(RadioResponseInfo paramRadioResponseInfo, LceStatusInfo paramLceStatusInfo)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(Integer.valueOf(lceStatus));
      localArrayList.add(Integer.valueOf(Byte.toUnsignedInt(actualIntervalMs)));
      if (error == 0) {
        sendMessageResponse(mResult, localArrayList);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, localArrayList);
    }
  }
  
  private void responseOperatorInfos(RadioResponseInfo paramRadioResponseInfo, ArrayList<android.hardware.radio.V1_0.OperatorInfo> paramArrayList)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      ArrayList localArrayList = new ArrayList();
      for (int i = 0; i < paramArrayList.size(); i++)
      {
        String str = PlmnTableAsus.GetCustomPlmn(getoperatorNumeric, getalphaShort);
        RIL localRIL = mRil;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("[ASIM RIL_RSP][responseOperatorInfos], i=");
        localStringBuilder.append(i);
        localStringBuilder.append(", plmnName: ");
        localStringBuilder.append(str);
        localStringBuilder.append(", old_plmn: ");
        localStringBuilder.append(getalphaShort);
        localRIL.riljLog(localStringBuilder.toString());
        if (str == null) {
          str = getalphaLong;
        }
        localArrayList.add(new OperatorInfo(str, getalphaShort, getoperatorNumeric, convertOpertatorInfoToString(getstatus)));
      }
      if (error == 0) {
        sendMessageResponse(mResult, localArrayList);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, localArrayList);
    }
  }
  
  private void responseRadioCapability(RadioResponseInfo paramRadioResponseInfo, RadioCapability paramRadioCapability)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      paramRadioCapability = RIL.convertHalRadioCapability(paramRadioCapability, mRil);
      if (error == 0) {
        sendMessageResponse(mResult, paramRadioCapability);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, paramRadioCapability);
    }
  }
  
  private void responseScanStatus(RadioResponseInfo paramRadioResponseInfo)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      NetworkScanResult localNetworkScanResult = null;
      if (error == 0)
      {
        localNetworkScanResult = new NetworkScanResult(1, 0, null);
        sendMessageResponse(mResult, localNetworkScanResult);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, localNetworkScanResult);
    }
  }
  
  private void responseSetupDataCall(RadioResponseInfo paramRadioResponseInfo, SetupDataCallResult paramSetupDataCallResult)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      if ((mRil.DNO_PDP_LIST != null) && (!mRil.DNO_PDP_LIST.isEmpty()) && (mRil.DNO_PDP_LIST.contains(localRILRequest.serialString())))
      {
        mRil.DNO_PDP_LIST.remove(localRILRequest.serialString());
        if ((-3 == status) && (suggestedRetryTime < 0)) {
          suggestedRetryTime = 10000;
        }
      }
      if (error == 0) {
        sendMessageResponse(mResult, paramSetupDataCallResult);
      }
      mRil.getDataCallList(null);
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, paramSetupDataCallResult);
    }
  }
  
  private void responseSignalStrength(RadioResponseInfo paramRadioResponseInfo, android.hardware.radio.V1_0.SignalStrength paramSignalStrength)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      paramSignalStrength = RIL.convertHalSignalStrength(paramSignalStrength);
      if (error == 0) {
        sendMessageResponse(mResult, paramSignalStrength);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, paramSignalStrength);
    }
  }
  
  private void responseSignalStrength_1_2(RadioResponseInfo paramRadioResponseInfo, android.hardware.radio.V1_2.SignalStrength paramSignalStrength)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      paramSignalStrength = RIL.convertHalSignalStrength_1_2(paramSignalStrength);
      if (error == 0) {
        sendMessageResponse(mResult, paramSignalStrength);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, paramSignalStrength);
    }
  }
  
  private void responseSms(RadioResponseInfo paramRadioResponseInfo, SendSmsResult paramSendSmsResult)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      paramSendSmsResult = new SmsResponse(messageRef, ackPDU, errorCode);
      if (error == 0) {
        sendMessageResponse(mResult, paramSendSmsResult);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, paramSendSmsResult);
    }
  }
  
  private void responseString(RadioResponseInfo paramRadioResponseInfo, String paramString)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      if (error == 0) {
        sendMessageResponse(mResult, paramString);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, paramString);
    }
  }
  
  static void responseStringArrayList(RIL paramRIL, RadioResponseInfo paramRadioResponseInfo, ArrayList<String> paramArrayList)
  {
    RILRequest localRILRequest = paramRIL.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      String[] arrayOfString = new String[paramArrayList.size()];
      for (int i = 0; i < paramArrayList.size(); i++) {
        arrayOfString[i] = ((String)paramArrayList.get(i));
      }
      if (error == 0) {
        sendMessageResponse(mResult, arrayOfString);
      }
      paramRIL.processResponseDone(localRILRequest, paramRadioResponseInfo, arrayOfString);
    }
  }
  
  private void responseStrings(RadioResponseInfo paramRadioResponseInfo, String... paramVarArgs)
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; i < paramVarArgs.length; i++) {
      localArrayList.add(paramVarArgs[i]);
    }
    responseStringArrayList(mRil, paramRadioResponseInfo, localArrayList);
  }
  
  private void responseVoid(RadioResponseInfo paramRadioResponseInfo)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      if (error == 0) {
        sendMessageResponse(mResult, null);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, null);
    }
  }
  
  static void sendMessageResponse(Message paramMessage, Object paramObject)
  {
    if (paramMessage != null)
    {
      AsyncResult.forMessage(paramMessage, paramObject, null);
      paramMessage.sendToTarget();
    }
  }
  
  public void acceptCallResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void acknowledgeIncomingGsmSmsWithPduResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void acknowledgeLastIncomingCdmaSmsResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void acknowledgeLastIncomingGsmSmsResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void acknowledgeRequest(int paramInt)
  {
    mRil.processRequestAck(paramInt);
  }
  
  public void cancelPendingUssdResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void changeIccPin2ForAppResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
  {
    responseInts(paramRadioResponseInfo, new int[] { paramInt });
  }
  
  public void changeIccPinForAppResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
  {
    responseInts(paramRadioResponseInfo, new int[] { paramInt });
  }
  
  public void conferenceResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void deactivateDataCallResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
    mRil.getDataCallList(null);
  }
  
  public void deleteSmsOnRuimResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void deleteSmsOnSimResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void dialResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void exitEmergencyCallbackModeResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void explicitCallTransferResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void getAllowedCarriersResponse(RadioResponseInfo paramRadioResponseInfo, boolean paramBoolean, CarrierRestrictions paramCarrierRestrictions)
  {
    responseCarrierIdentifiers(paramRadioResponseInfo, paramBoolean, paramCarrierRestrictions);
  }
  
  public void getAvailableBandModesResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<Integer> paramArrayList)
  {
    responseIntArrayList(paramRadioResponseInfo, paramArrayList);
  }
  
  public void getAvailableNetworksResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<android.hardware.radio.V1_0.OperatorInfo> paramArrayList)
  {
    responseOperatorInfos(paramRadioResponseInfo, paramArrayList);
  }
  
  public void getBasebandVersionResponse(RadioResponseInfo paramRadioResponseInfo, String paramString)
  {
    responseString(paramRadioResponseInfo, paramString);
  }
  
  public void getCDMASubscriptionResponse(RadioResponseInfo paramRadioResponseInfo, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
  {
    responseStrings(paramRadioResponseInfo, new String[] { paramString1, paramString2, paramString3, paramString4, paramString5 });
  }
  
  public void getCallForwardStatusResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<android.hardware.radio.V1_0.CallForwardInfo> paramArrayList)
  {
    responseCallForwardInfo(paramRadioResponseInfo, paramArrayList);
  }
  
  public void getCallWaitingResponse(RadioResponseInfo paramRadioResponseInfo, boolean paramBoolean, int paramInt)
  {
    responseInts(paramRadioResponseInfo, new int[] { paramBoolean, paramInt });
  }
  
  public void getCdmaBroadcastConfigResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<CdmaBroadcastSmsConfigInfo> paramArrayList)
  {
    responseCdmaBroadcastConfig(paramRadioResponseInfo, paramArrayList);
  }
  
  public void getCdmaRoamingPreferenceResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
  {
    responseInts(paramRadioResponseInfo, new int[] { paramInt });
  }
  
  public void getCdmaSubscriptionSourceResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
  {
    responseInts(paramRadioResponseInfo, new int[] { paramInt });
  }
  
  public void getCellInfoListResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<android.hardware.radio.V1_0.CellInfo> paramArrayList)
  {
    responseCellInfoList(paramRadioResponseInfo, paramArrayList);
  }
  
  public void getCellInfoListResponse_1_2(RadioResponseInfo paramRadioResponseInfo, ArrayList<android.hardware.radio.V1_2.CellInfo> paramArrayList)
  {
    responseCellInfoList_1_2(paramRadioResponseInfo, paramArrayList);
  }
  
  public void getClipResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
  {
    responseInts(paramRadioResponseInfo, new int[] { paramInt });
  }
  
  public void getClirResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt1, int paramInt2)
  {
    responseInts(paramRadioResponseInfo, new int[] { paramInt1, paramInt2 });
  }
  
  public void getCurrentCallsResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<android.hardware.radio.V1_0.Call> paramArrayList)
  {
    responseCurrentCalls(paramRadioResponseInfo, paramArrayList);
  }
  
  public void getCurrentCallsResponse_1_2(RadioResponseInfo paramRadioResponseInfo, ArrayList<android.hardware.radio.V1_2.Call> paramArrayList)
  {
    responseCurrentCalls_1_2(paramRadioResponseInfo, paramArrayList);
  }
  
  public void getDataCallListResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<SetupDataCallResult> paramArrayList)
  {
    responseDataCallList(paramRadioResponseInfo, paramArrayList);
  }
  
  public void getDataRegistrationStateResponse(RadioResponseInfo paramRadioResponseInfo, android.hardware.radio.V1_0.DataRegStateResult paramDataRegStateResult)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      if (error == 0) {
        sendMessageResponse(mResult, paramDataRegStateResult);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, paramDataRegStateResult);
    }
  }
  
  public void getDataRegistrationStateResponse_1_2(RadioResponseInfo paramRadioResponseInfo, android.hardware.radio.V1_2.DataRegStateResult paramDataRegStateResult)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      if (error == 0) {
        sendMessageResponse(mResult, paramDataRegStateResult);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, paramDataRegStateResult);
    }
  }
  
  public void getDeviceIdentityResponse(RadioResponseInfo paramRadioResponseInfo, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    responseStrings(paramRadioResponseInfo, new String[] { paramString1, paramString2, paramString3, paramString4 });
  }
  
  public void getFacilityLockForAppResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
  {
    responseInts(paramRadioResponseInfo, new int[] { paramInt });
  }
  
  public void getGsmBroadcastConfigResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<GsmBroadcastSmsConfigInfo> paramArrayList)
  {
    responseGmsBroadcastConfig(paramRadioResponseInfo, paramArrayList);
  }
  
  public void getHardwareConfigResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<HardwareConfig> paramArrayList)
  {
    responseHardwareConfig(paramRadioResponseInfo, paramArrayList);
  }
  
  public void getIMSIForAppResponse(RadioResponseInfo paramRadioResponseInfo, String paramString)
  {
    responseString(paramRadioResponseInfo, paramString);
    mRil.setIMSIAsus(paramString);
  }
  
  public void getIccCardStatusResponse(RadioResponseInfo paramRadioResponseInfo, android.hardware.radio.V1_0.CardStatus paramCardStatus)
  {
    responseIccCardStatus(paramRadioResponseInfo, paramCardStatus);
  }
  
  public void getIccCardStatusResponse_1_2(RadioResponseInfo paramRadioResponseInfo, android.hardware.radio.V1_2.CardStatus paramCardStatus)
  {
    responseIccCardStatus_1_2(paramRadioResponseInfo, paramCardStatus);
  }
  
  public void getImsRegistrationStateResponse(RadioResponseInfo paramRadioResponseInfo, boolean paramBoolean, int paramInt)
  {
    responseInts(paramRadioResponseInfo, new int[] { paramBoolean, paramInt });
  }
  
  public void getLastCallFailCauseResponse(RadioResponseInfo paramRadioResponseInfo, LastCallFailCauseInfo paramLastCallFailCauseInfo)
  {
    responseLastCallFailCauseInfo(paramRadioResponseInfo, paramLastCallFailCauseInfo);
  }
  
  public void getModemActivityInfoResponse(RadioResponseInfo paramRadioResponseInfo, ActivityStatsInfo paramActivityStatsInfo)
  {
    responseActivityData(paramRadioResponseInfo, paramActivityStatsInfo);
  }
  
  public void getMuteResponse(RadioResponseInfo paramRadioResponseInfo, boolean paramBoolean)
  {
    responseInts(paramRadioResponseInfo, new int[] { paramBoolean });
  }
  
  public void getNeighboringCidsResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<NeighboringCell> paramArrayList)
  {
    responseCellList(paramRadioResponseInfo, paramArrayList);
  }
  
  public void getNetworkSelectionModeResponse(RadioResponseInfo paramRadioResponseInfo, boolean paramBoolean)
  {
    responseInts(paramRadioResponseInfo, new int[] { paramBoolean });
  }
  
  public void getOperatorResponse(RadioResponseInfo paramRadioResponseInfo, String paramString1, String paramString2, String paramString3)
  {
    String[] arrayOfString = new String[3];
    arrayOfString[0] = paramString1;
    arrayOfString[1] = paramString2;
    arrayOfString[2] = paramString3;
    DnoHandler localDnoHandler = mRil.getDnoHandler();
    Object localObject;
    if (error == 0)
    {
      localObject = mRil.responseOperator(paramString1, paramString2, paramString3);
      mRil.logPlmn(paramString1, paramString3);
    }
    else
    {
      localObject = arrayOfString;
      if (error == 9) {
        if ((localDnoHandler != null) && (localDnoHandler.is_delay_no_service_pending()))
        {
          paramString1 = mRil.responseOperator(paramString1, paramString2, paramString3);
          localObject = paramString1;
          if (paramString1 != null)
          {
            error = 0;
            localObject = paramString1;
          }
        }
        else
        {
          mRil.logPlmn(paramString1, paramString3);
          localObject = arrayOfString;
        }
      }
    }
    responseStrings(paramRadioResponseInfo, new String[] { localObject[0], localObject[1], localObject[2] });
  }
  
  public void getPreferredNetworkTypeResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
  {
    mRil.mPreferredNetworkType = paramInt;
    responseInts(paramRadioResponseInfo, new int[] { paramInt });
  }
  
  public void getPreferredVoicePrivacyResponse(RadioResponseInfo paramRadioResponseInfo, boolean paramBoolean)
  {
    responseInts(paramRadioResponseInfo, new int[] { paramBoolean });
  }
  
  public void getRadioCapabilityResponse(RadioResponseInfo paramRadioResponseInfo, RadioCapability paramRadioCapability)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      paramRadioCapability = RIL.convertHalRadioCapability(paramRadioCapability, mRil);
      if ((error == 6) || (error == 2))
      {
        paramRadioCapability = mRil.makeStaticRadioCapability();
        error = 0;
      }
      if (error == 0) {
        sendMessageResponse(mResult, paramRadioCapability);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, paramRadioCapability);
    }
  }
  
  public void getSignalStrengthResponse(RadioResponseInfo paramRadioResponseInfo, android.hardware.radio.V1_0.SignalStrength paramSignalStrength)
  {
    responseSignalStrength(paramRadioResponseInfo, paramSignalStrength);
  }
  
  public void getSignalStrengthResponse_1_2(RadioResponseInfo paramRadioResponseInfo, android.hardware.radio.V1_2.SignalStrength paramSignalStrength)
  {
    responseSignalStrength_1_2(paramRadioResponseInfo, paramSignalStrength);
  }
  
  public void getSmscAddressResponse(RadioResponseInfo paramRadioResponseInfo, String paramString)
  {
    responseString(paramRadioResponseInfo, paramString);
  }
  
  public void getTTYModeResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
  {
    responseInts(paramRadioResponseInfo, new int[] { paramInt });
  }
  
  public void getVoiceRadioTechnologyResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
  {
    responseInts(paramRadioResponseInfo, new int[] { paramInt });
  }
  
  public void getVoiceRegistrationStateResponse(RadioResponseInfo paramRadioResponseInfo, android.hardware.radio.V1_0.VoiceRegStateResult paramVoiceRegStateResult)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      if (error == 0) {
        sendMessageResponse(mResult, paramVoiceRegStateResult);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, paramVoiceRegStateResult);
    }
  }
  
  public void getVoiceRegistrationStateResponse_1_2(RadioResponseInfo paramRadioResponseInfo, android.hardware.radio.V1_2.VoiceRegStateResult paramVoiceRegStateResult)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      if (error == 0) {
        sendMessageResponse(mResult, paramVoiceRegStateResult);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, paramVoiceRegStateResult);
    }
  }
  
  public void handleStkCallSetupRequestFromSimResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void hangupConnectionResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void hangupForegroundResumeBackgroundResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void hangupWaitingOrBackgroundResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void iccCloseLogicalChannelResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void iccIOForAppResponse(RadioResponseInfo paramRadioResponseInfo, android.hardware.radio.V1_0.IccIoResult paramIccIoResult)
  {
    responseIccIo(paramRadioResponseInfo, paramIccIoResult);
  }
  
  public void iccOpenLogicalChannelResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt, ArrayList<Byte> paramArrayList)
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(Integer.valueOf(paramInt));
    for (paramInt = 0; paramInt < paramArrayList.size(); paramInt++) {
      localArrayList.add(Integer.valueOf(((Byte)paramArrayList.get(paramInt)).byteValue()));
    }
    responseIntArrayList(paramRadioResponseInfo, localArrayList);
  }
  
  public void iccTransmitApduBasicChannelResponse(RadioResponseInfo paramRadioResponseInfo, android.hardware.radio.V1_0.IccIoResult paramIccIoResult)
  {
    responseIccIo(paramRadioResponseInfo, paramIccIoResult);
  }
  
  public void iccTransmitApduLogicalChannelResponse(RadioResponseInfo paramRadioResponseInfo, android.hardware.radio.V1_0.IccIoResult paramIccIoResult)
  {
    responseIccIo(paramRadioResponseInfo, paramIccIoResult);
  }
  
  public void nvReadItemResponse(RadioResponseInfo paramRadioResponseInfo, String paramString)
  {
    responseString(paramRadioResponseInfo, paramString);
  }
  
  public void nvResetConfigResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void nvWriteCdmaPrlResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void nvWriteItemResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void pullLceDataResponse(RadioResponseInfo paramRadioResponseInfo, LceDataInfo paramLceDataInfo)
  {
    responseLceData(paramRadioResponseInfo, paramLceDataInfo);
  }
  
  public void rejectCallResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void reportSmsMemoryStatusResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void reportStkServiceIsRunningResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void requestIccSimAuthenticationResponse(RadioResponseInfo paramRadioResponseInfo, android.hardware.radio.V1_0.IccIoResult paramIccIoResult)
  {
    responseICC_IOBase64(paramRadioResponseInfo, paramIccIoResult);
  }
  
  public void requestIsimAuthenticationResponse(RadioResponseInfo paramRadioResponseInfo, String paramString)
  {
    throw new RuntimeException("Inexplicable response received for requestIsimAuthentication");
  }
  
  public void requestShutdownResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void sendBurstDtmfResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void sendCDMAFeatureCodeResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void sendCdmaSmsResponse(RadioResponseInfo paramRadioResponseInfo, SendSmsResult paramSendSmsResult)
  {
    responseSms(paramRadioResponseInfo, paramSendSmsResult);
  }
  
  public void sendDeviceStateResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void sendDtmfResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void sendEnvelopeResponse(RadioResponseInfo paramRadioResponseInfo, String paramString)
  {
    responseString(paramRadioResponseInfo, paramString);
  }
  
  public void sendEnvelopeWithStatusResponse(RadioResponseInfo paramRadioResponseInfo, android.hardware.radio.V1_0.IccIoResult paramIccIoResult)
  {
    responseIccIo(paramRadioResponseInfo, paramIccIoResult);
  }
  
  public void sendImsSmsResponse(RadioResponseInfo paramRadioResponseInfo, SendSmsResult paramSendSmsResult)
  {
    responseSms(paramRadioResponseInfo, paramSendSmsResult);
  }
  
  public void sendOemRilRequestRawResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<Byte> paramArrayList) {}
  
  public void sendSMSExpectMoreResponse(RadioResponseInfo paramRadioResponseInfo, SendSmsResult paramSendSmsResult)
  {
    responseSms(paramRadioResponseInfo, paramSendSmsResult);
  }
  
  public void sendSmsResponse(RadioResponseInfo paramRadioResponseInfo, SendSmsResult paramSendSmsResult)
  {
    responseSms(paramRadioResponseInfo, paramSendSmsResult);
  }
  
  public void sendTerminalResponseToSimResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void sendUssdResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void separateConnectionResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setAllowedCarriersResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
  {
    responseInts(paramRadioResponseInfo, new int[] { paramInt });
  }
  
  public void setBandModeResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setBarringPasswordResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setCallForwardResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setCallWaitingResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setCarrierInfoForImsiEncryptionResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setCdmaBroadcastActivationResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setCdmaBroadcastConfigResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setCdmaRoamingPreferenceResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setCdmaSubscriptionSourceResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setCellInfoListRateResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setClirResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setDataAllowedResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setDataProfileResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setFacilityLockForAppResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
  {
    responseInts(paramRadioResponseInfo, new int[] { paramInt });
  }
  
  public void setGsmBroadcastActivationResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setGsmBroadcastConfigResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setIndicationFilterResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setInitialAttachApnResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setLinkCapacityReportingCriteriaResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setLocationUpdatesResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setMuteResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setNetworkSelectionModeAutomaticResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    mRil.getDnoHandler().ril_snapshot_clear_all();
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setNetworkSelectionModeManualResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    mRil.getDnoHandler().ril_snapshot_clear_all();
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setPreferredNetworkTypeResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setPreferredVoicePrivacyResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setRadioCapabilityResponse(RadioResponseInfo paramRadioResponseInfo, RadioCapability paramRadioCapability)
  {
    responseRadioCapability(paramRadioResponseInfo, paramRadioCapability);
  }
  
  public void setRadioPowerResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setSignalStrengthReportingCriteriaResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setSimCardPowerResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setSimCardPowerResponse_1_1(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setSmscAddressResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setSuppServiceNotificationsResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setTTYModeResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setUiccSubscriptionResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void setupDataCallResponse(RadioResponseInfo paramRadioResponseInfo, SetupDataCallResult paramSetupDataCallResult)
  {
    responseSetupDataCall(paramRadioResponseInfo, paramSetupDataCallResult);
  }
  
  public void startDtmfResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void startKeepaliveResponse(RadioResponseInfo paramRadioResponseInfo, android.hardware.radio.V1_1.KeepaliveStatus paramKeepaliveStatus)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    mRil.getDnoHandler();
    if (localRILRequest == null) {
      return;
    }
    int i = error;
    if (i != 0)
    {
      if (i != 6)
      {
        if (i != 42) {
          paramKeepaliveStatus = new com.android.internal.telephony.dataconnection.KeepaliveStatus(3);
        } else {
          paramKeepaliveStatus = new com.android.internal.telephony.dataconnection.KeepaliveStatus(2);
        }
      }
      else
      {
        paramKeepaliveStatus = new com.android.internal.telephony.dataconnection.KeepaliveStatus(1);
        error = 0;
      }
    }
    else
    {
      i = convertHalKeepaliveStatusCode(code);
      if (i < 0) {
        paramKeepaliveStatus = new com.android.internal.telephony.dataconnection.KeepaliveStatus(1);
      } else {
        paramKeepaliveStatus = new com.android.internal.telephony.dataconnection.KeepaliveStatus(sessionHandle, i);
      }
    }
    sendMessageResponse(mResult, paramKeepaliveStatus);
    mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, paramKeepaliveStatus);
  }
  
  public void startLceServiceResponse(RadioResponseInfo paramRadioResponseInfo, LceStatusInfo paramLceStatusInfo)
  {
    responseLceStatus(paramRadioResponseInfo, paramLceStatusInfo);
  }
  
  public void startNetworkScanResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseScanStatus(paramRadioResponseInfo);
  }
  
  public void stopDtmfResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void stopKeepaliveResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest == null) {
      return;
    }
    if (error == 0)
    {
      sendMessageResponse(mResult, null);
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, null);
    }
  }
  
  public void stopLceServiceResponse(RadioResponseInfo paramRadioResponseInfo, LceStatusInfo paramLceStatusInfo)
  {
    responseLceStatus(paramRadioResponseInfo, paramLceStatusInfo);
  }
  
  public void stopNetworkScanResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseScanStatus(paramRadioResponseInfo);
  }
  
  public void supplyIccPin2ForAppResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
  {
    responseInts(paramRadioResponseInfo, new int[] { paramInt });
  }
  
  public void supplyIccPinForAppResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
  {
    responseInts(paramRadioResponseInfo, new int[] { paramInt });
  }
  
  public void supplyIccPuk2ForAppResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
  {
    responseInts(paramRadioResponseInfo, new int[] { paramInt });
  }
  
  public void supplyIccPukForAppResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
  {
    responseInts(paramRadioResponseInfo, new int[] { paramInt });
  }
  
  public void supplyNetworkDepersonalizationResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
  {
    responseInts(paramRadioResponseInfo, new int[] { paramInt });
  }
  
  public void switchWaitingOrHoldingAndActiveResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    responseVoid(paramRadioResponseInfo);
  }
  
  public void writeSmsToRuimResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
  {
    responseInts(paramRadioResponseInfo, new int[] { paramInt });
  }
  
  public void writeSmsToSimResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
  {
    responseInts(paramRadioResponseInfo, new int[] { paramInt });
  }
}
