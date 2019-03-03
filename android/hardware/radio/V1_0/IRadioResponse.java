package android.hardware.radio.V1_0;

import android.hidl.base.V1_0.DebugInfo;
import android.hidl.base.V1_0.IBase;
import android.os.HidlSupport;
import android.os.HwBinder;
import android.os.HwBlob;
import android.os.HwParcel;
import android.os.IHwBinder;
import android.os.IHwBinder.DeathRecipient;
import android.os.IHwInterface;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public abstract interface IRadioResponse
  extends IBase
{
  public static final String kInterfaceName = "android.hardware.radio@1.0::IRadioResponse";
  
  public static IRadioResponse asInterface(IHwBinder paramIHwBinder)
  {
    if (paramIHwBinder == null) {
      return null;
    }
    Object localObject = paramIHwBinder.queryLocalInterface("android.hardware.radio@1.0::IRadioResponse");
    if ((localObject != null) && ((localObject instanceof IRadioResponse))) {
      return (IRadioResponse)localObject;
    }
    localObject = new Proxy(paramIHwBinder);
    try
    {
      paramIHwBinder = ((IRadioResponse)localObject).interfaceChain().iterator();
      while (paramIHwBinder.hasNext())
      {
        boolean bool = ((String)paramIHwBinder.next()).equals("android.hardware.radio@1.0::IRadioResponse");
        if (bool) {
          return localObject;
        }
      }
    }
    catch (RemoteException paramIHwBinder) {}
    return null;
  }
  
  public static IRadioResponse castFrom(IHwInterface paramIHwInterface)
  {
    if (paramIHwInterface == null) {
      paramIHwInterface = null;
    } else {
      paramIHwInterface = asInterface(paramIHwInterface.asBinder());
    }
    return paramIHwInterface;
  }
  
  public static IRadioResponse getService()
    throws RemoteException
  {
    return getService("default");
  }
  
  public static IRadioResponse getService(String paramString)
    throws RemoteException
  {
    return asInterface(HwBinder.getService("android.hardware.radio@1.0::IRadioResponse", paramString));
  }
  
  public static IRadioResponse getService(String paramString, boolean paramBoolean)
    throws RemoteException
  {
    return asInterface(HwBinder.getService("android.hardware.radio@1.0::IRadioResponse", paramString, paramBoolean));
  }
  
  public static IRadioResponse getService(boolean paramBoolean)
    throws RemoteException
  {
    return getService("default", paramBoolean);
  }
  
  public abstract void acceptCallResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void acknowledgeIncomingGsmSmsWithPduResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void acknowledgeLastIncomingCdmaSmsResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void acknowledgeLastIncomingGsmSmsResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void acknowledgeRequest(int paramInt)
    throws RemoteException;
  
  public abstract IHwBinder asBinder();
  
  public abstract void cancelPendingUssdResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void changeIccPin2ForAppResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
    throws RemoteException;
  
  public abstract void changeIccPinForAppResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
    throws RemoteException;
  
  public abstract void conferenceResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void deactivateDataCallResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void deleteSmsOnRuimResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void deleteSmsOnSimResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void dialResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void exitEmergencyCallbackModeResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void explicitCallTransferResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void getAllowedCarriersResponse(RadioResponseInfo paramRadioResponseInfo, boolean paramBoolean, CarrierRestrictions paramCarrierRestrictions)
    throws RemoteException;
  
  public abstract void getAvailableBandModesResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<Integer> paramArrayList)
    throws RemoteException;
  
  public abstract void getAvailableNetworksResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<OperatorInfo> paramArrayList)
    throws RemoteException;
  
  public abstract void getBasebandVersionResponse(RadioResponseInfo paramRadioResponseInfo, String paramString)
    throws RemoteException;
  
  public abstract void getCDMASubscriptionResponse(RadioResponseInfo paramRadioResponseInfo, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
    throws RemoteException;
  
  public abstract void getCallForwardStatusResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<CallForwardInfo> paramArrayList)
    throws RemoteException;
  
  public abstract void getCallWaitingResponse(RadioResponseInfo paramRadioResponseInfo, boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void getCdmaBroadcastConfigResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<CdmaBroadcastSmsConfigInfo> paramArrayList)
    throws RemoteException;
  
  public abstract void getCdmaRoamingPreferenceResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
    throws RemoteException;
  
  public abstract void getCdmaSubscriptionSourceResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
    throws RemoteException;
  
  public abstract void getCellInfoListResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<CellInfo> paramArrayList)
    throws RemoteException;
  
  public abstract void getClipResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
    throws RemoteException;
  
  public abstract void getClirResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void getCurrentCallsResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<Call> paramArrayList)
    throws RemoteException;
  
  public abstract void getDataCallListResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<SetupDataCallResult> paramArrayList)
    throws RemoteException;
  
  public abstract void getDataRegistrationStateResponse(RadioResponseInfo paramRadioResponseInfo, DataRegStateResult paramDataRegStateResult)
    throws RemoteException;
  
  public abstract DebugInfo getDebugInfo()
    throws RemoteException;
  
  public abstract void getDeviceIdentityResponse(RadioResponseInfo paramRadioResponseInfo, String paramString1, String paramString2, String paramString3, String paramString4)
    throws RemoteException;
  
  public abstract void getFacilityLockForAppResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
    throws RemoteException;
  
  public abstract void getGsmBroadcastConfigResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<GsmBroadcastSmsConfigInfo> paramArrayList)
    throws RemoteException;
  
  public abstract void getHardwareConfigResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<HardwareConfig> paramArrayList)
    throws RemoteException;
  
  public abstract ArrayList<byte[]> getHashChain()
    throws RemoteException;
  
  public abstract void getIMSIForAppResponse(RadioResponseInfo paramRadioResponseInfo, String paramString)
    throws RemoteException;
  
  public abstract void getIccCardStatusResponse(RadioResponseInfo paramRadioResponseInfo, CardStatus paramCardStatus)
    throws RemoteException;
  
  public abstract void getImsRegistrationStateResponse(RadioResponseInfo paramRadioResponseInfo, boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void getLastCallFailCauseResponse(RadioResponseInfo paramRadioResponseInfo, LastCallFailCauseInfo paramLastCallFailCauseInfo)
    throws RemoteException;
  
  public abstract void getModemActivityInfoResponse(RadioResponseInfo paramRadioResponseInfo, ActivityStatsInfo paramActivityStatsInfo)
    throws RemoteException;
  
  public abstract void getMuteResponse(RadioResponseInfo paramRadioResponseInfo, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void getNeighboringCidsResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<NeighboringCell> paramArrayList)
    throws RemoteException;
  
  public abstract void getNetworkSelectionModeResponse(RadioResponseInfo paramRadioResponseInfo, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void getOperatorResponse(RadioResponseInfo paramRadioResponseInfo, String paramString1, String paramString2, String paramString3)
    throws RemoteException;
  
  public abstract void getPreferredNetworkTypeResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
    throws RemoteException;
  
  public abstract void getPreferredVoicePrivacyResponse(RadioResponseInfo paramRadioResponseInfo, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void getRadioCapabilityResponse(RadioResponseInfo paramRadioResponseInfo, RadioCapability paramRadioCapability)
    throws RemoteException;
  
  public abstract void getSignalStrengthResponse(RadioResponseInfo paramRadioResponseInfo, SignalStrength paramSignalStrength)
    throws RemoteException;
  
  public abstract void getSmscAddressResponse(RadioResponseInfo paramRadioResponseInfo, String paramString)
    throws RemoteException;
  
  public abstract void getTTYModeResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
    throws RemoteException;
  
  public abstract void getVoiceRadioTechnologyResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
    throws RemoteException;
  
  public abstract void getVoiceRegistrationStateResponse(RadioResponseInfo paramRadioResponseInfo, VoiceRegStateResult paramVoiceRegStateResult)
    throws RemoteException;
  
  public abstract void handleStkCallSetupRequestFromSimResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void hangupConnectionResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void hangupForegroundResumeBackgroundResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void hangupWaitingOrBackgroundResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void iccCloseLogicalChannelResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void iccIOForAppResponse(RadioResponseInfo paramRadioResponseInfo, IccIoResult paramIccIoResult)
    throws RemoteException;
  
  public abstract void iccOpenLogicalChannelResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt, ArrayList<Byte> paramArrayList)
    throws RemoteException;
  
  public abstract void iccTransmitApduBasicChannelResponse(RadioResponseInfo paramRadioResponseInfo, IccIoResult paramIccIoResult)
    throws RemoteException;
  
  public abstract void iccTransmitApduLogicalChannelResponse(RadioResponseInfo paramRadioResponseInfo, IccIoResult paramIccIoResult)
    throws RemoteException;
  
  public abstract ArrayList<String> interfaceChain()
    throws RemoteException;
  
  public abstract String interfaceDescriptor()
    throws RemoteException;
  
  public abstract boolean linkToDeath(IHwBinder.DeathRecipient paramDeathRecipient, long paramLong)
    throws RemoteException;
  
  public abstract void notifySyspropsChanged()
    throws RemoteException;
  
  public abstract void nvReadItemResponse(RadioResponseInfo paramRadioResponseInfo, String paramString)
    throws RemoteException;
  
  public abstract void nvResetConfigResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void nvWriteCdmaPrlResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void nvWriteItemResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void ping()
    throws RemoteException;
  
  public abstract void pullLceDataResponse(RadioResponseInfo paramRadioResponseInfo, LceDataInfo paramLceDataInfo)
    throws RemoteException;
  
  public abstract void rejectCallResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void reportSmsMemoryStatusResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void reportStkServiceIsRunningResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void requestIccSimAuthenticationResponse(RadioResponseInfo paramRadioResponseInfo, IccIoResult paramIccIoResult)
    throws RemoteException;
  
  public abstract void requestIsimAuthenticationResponse(RadioResponseInfo paramRadioResponseInfo, String paramString)
    throws RemoteException;
  
  public abstract void requestShutdownResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void sendBurstDtmfResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void sendCDMAFeatureCodeResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void sendCdmaSmsResponse(RadioResponseInfo paramRadioResponseInfo, SendSmsResult paramSendSmsResult)
    throws RemoteException;
  
  public abstract void sendDeviceStateResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void sendDtmfResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void sendEnvelopeResponse(RadioResponseInfo paramRadioResponseInfo, String paramString)
    throws RemoteException;
  
  public abstract void sendEnvelopeWithStatusResponse(RadioResponseInfo paramRadioResponseInfo, IccIoResult paramIccIoResult)
    throws RemoteException;
  
  public abstract void sendImsSmsResponse(RadioResponseInfo paramRadioResponseInfo, SendSmsResult paramSendSmsResult)
    throws RemoteException;
  
  public abstract void sendSMSExpectMoreResponse(RadioResponseInfo paramRadioResponseInfo, SendSmsResult paramSendSmsResult)
    throws RemoteException;
  
  public abstract void sendSmsResponse(RadioResponseInfo paramRadioResponseInfo, SendSmsResult paramSendSmsResult)
    throws RemoteException;
  
  public abstract void sendTerminalResponseToSimResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void sendUssdResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void separateConnectionResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setAllowedCarriersResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
    throws RemoteException;
  
  public abstract void setBandModeResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setBarringPasswordResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setCallForwardResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setCallWaitingResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setCdmaBroadcastActivationResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setCdmaBroadcastConfigResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setCdmaRoamingPreferenceResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setCdmaSubscriptionSourceResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setCellInfoListRateResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setClirResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setDataAllowedResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setDataProfileResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setFacilityLockForAppResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
    throws RemoteException;
  
  public abstract void setGsmBroadcastActivationResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setGsmBroadcastConfigResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setHALInstrumentation()
    throws RemoteException;
  
  public abstract void setIndicationFilterResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setInitialAttachApnResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setLocationUpdatesResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setMuteResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setNetworkSelectionModeAutomaticResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setNetworkSelectionModeManualResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setPreferredNetworkTypeResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setPreferredVoicePrivacyResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setRadioCapabilityResponse(RadioResponseInfo paramRadioResponseInfo, RadioCapability paramRadioCapability)
    throws RemoteException;
  
  public abstract void setRadioPowerResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setSimCardPowerResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setSmscAddressResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setSuppServiceNotificationsResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setTTYModeResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setUiccSubscriptionResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void setupDataCallResponse(RadioResponseInfo paramRadioResponseInfo, SetupDataCallResult paramSetupDataCallResult)
    throws RemoteException;
  
  public abstract void startDtmfResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void startLceServiceResponse(RadioResponseInfo paramRadioResponseInfo, LceStatusInfo paramLceStatusInfo)
    throws RemoteException;
  
  public abstract void stopDtmfResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract void stopLceServiceResponse(RadioResponseInfo paramRadioResponseInfo, LceStatusInfo paramLceStatusInfo)
    throws RemoteException;
  
  public abstract void supplyIccPin2ForAppResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
    throws RemoteException;
  
  public abstract void supplyIccPinForAppResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
    throws RemoteException;
  
  public abstract void supplyIccPuk2ForAppResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
    throws RemoteException;
  
  public abstract void supplyIccPukForAppResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
    throws RemoteException;
  
  public abstract void supplyNetworkDepersonalizationResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
    throws RemoteException;
  
  public abstract void switchWaitingOrHoldingAndActiveResponse(RadioResponseInfo paramRadioResponseInfo)
    throws RemoteException;
  
  public abstract boolean unlinkToDeath(IHwBinder.DeathRecipient paramDeathRecipient)
    throws RemoteException;
  
  public abstract void writeSmsToRuimResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
    throws RemoteException;
  
  public abstract void writeSmsToSimResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
    throws RemoteException;
  
  public static final class Proxy
    implements IRadioResponse
  {
    private IHwBinder mRemote;
    
    public Proxy(IHwBinder paramIHwBinder)
    {
      mRemote = ((IHwBinder)Objects.requireNonNull(paramIHwBinder));
    }
    
    public void acceptCallResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(38, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void acknowledgeIncomingGsmSmsWithPduResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(96, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void acknowledgeLastIncomingCdmaSmsResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(78, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void acknowledgeLastIncomingGsmSmsResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(37, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void acknowledgeRequest(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(129, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public IHwBinder asBinder()
    {
      return mRemote;
    }
    
    public void cancelPendingUssdResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(30, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void changeIccPin2ForAppResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeInt32(paramInt);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(7, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void changeIccPinForAppResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeInt32(paramInt);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(6, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void conferenceResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(16, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void deactivateDataCallResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(39, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void deleteSmsOnRuimResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(87, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void deleteSmsOnSimResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(57, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void dialResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(10, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public final boolean equals(Object paramObject)
    {
      return HidlSupport.interfacesEqual(this, paramObject);
    }
    
    public void exitEmergencyCallbackModeResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(89, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void explicitCallTransferResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(63, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getAllowedCarriersResponse(RadioResponseInfo paramRadioResponseInfo, boolean paramBoolean, CarrierRestrictions paramCarrierRestrictions)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeBool(paramBoolean);
      paramCarrierRestrictions.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(125, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getAvailableBandModesResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<Integer> paramArrayList)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeInt32Vector(paramArrayList);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(59, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getAvailableNetworksResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<OperatorInfo> paramArrayList)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      OperatorInfo.writeVectorToParcel(localHwParcel, paramArrayList);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(46, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getBasebandVersionResponse(RadioResponseInfo paramRadioResponseInfo, String paramString)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeString(paramString);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(49, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getCDMASubscriptionResponse(RadioResponseInfo paramRadioResponseInfo, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeString(paramString1);
      localHwParcel.writeString(paramString2);
      localHwParcel.writeString(paramString3);
      localHwParcel.writeString(paramString4);
      localHwParcel.writeString(paramString5);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(85, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getCallForwardStatusResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<CallForwardInfo> paramArrayList)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      CallForwardInfo.writeVectorToParcel(localHwParcel, paramArrayList);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(33, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getCallWaitingResponse(RadioResponseInfo paramRadioResponseInfo, boolean paramBoolean, int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeBool(paramBoolean);
      localHwParcel.writeInt32(paramInt);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(35, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getCdmaBroadcastConfigResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<CdmaBroadcastSmsConfigInfo> paramArrayList)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      CdmaBroadcastSmsConfigInfo.writeVectorToParcel(localHwParcel, paramArrayList);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(82, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getCdmaRoamingPreferenceResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeInt32(paramInt);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(70, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getCdmaSubscriptionSourceResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeInt32(paramInt);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(94, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getCellInfoListResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<CellInfo> paramArrayList)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      CellInfo.writeVectorToParcel(localHwParcel, paramArrayList);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(99, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getClipResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeInt32(paramInt);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(53, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getClirResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeInt32(paramInt1);
      localHwParcel.writeInt32(paramInt2);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(31, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getCurrentCallsResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<Call> paramArrayList)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      Call.writeVectorToParcel(localHwParcel, paramArrayList);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(9, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getDataCallListResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<SetupDataCallResult> paramArrayList)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      SetupDataCallResult.writeVectorToParcel(localHwParcel, paramArrayList);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(54, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getDataRegistrationStateResponse(RadioResponseInfo paramRadioResponseInfo, DataRegStateResult paramDataRegStateResult)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramDataRegStateResult.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(21, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public DebugInfo getDebugInfo()
      throws RemoteException
    {
      Object localObject1 = new HwParcel();
      ((HwParcel)localObject1).writeInterfaceToken("android.hidl.base@1.0::IBase");
      HwParcel localHwParcel = new HwParcel();
      try
      {
        mRemote.transact(257049926, (HwParcel)localObject1, localHwParcel, 0);
        localHwParcel.verifySuccess();
        ((HwParcel)localObject1).releaseTemporaryStorage();
        localObject1 = new android/hidl/base/V1_0/DebugInfo;
        ((DebugInfo)localObject1).<init>();
        ((DebugInfo)localObject1).readFromParcel(localHwParcel);
        return localObject1;
      }
      finally
      {
        localHwParcel.release();
      }
    }
    
    public void getDeviceIdentityResponse(RadioResponseInfo paramRadioResponseInfo, String paramString1, String paramString2, String paramString3, String paramString4)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeString(paramString1);
      localHwParcel.writeString(paramString2);
      localHwParcel.writeString(paramString3);
      localHwParcel.writeString(paramString4);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(88, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getFacilityLockForAppResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeInt32(paramInt);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(40, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getGsmBroadcastConfigResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<GsmBroadcastSmsConfigInfo> paramArrayList)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      GsmBroadcastSmsConfigInfo.writeVectorToParcel(localHwParcel, paramArrayList);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(79, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getHardwareConfigResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<HardwareConfig> paramArrayList)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      HardwareConfig.writeVectorToParcel(localHwParcel, paramArrayList);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(114, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public ArrayList<byte[]> getHashChain()
      throws RemoteException
    {
      Object localObject1 = new HwParcel();
      ((HwParcel)localObject1).writeInterfaceToken("android.hidl.base@1.0::IBase");
      HwParcel localHwParcel = new HwParcel();
      try
      {
        Object localObject3 = mRemote;
        int i = 0;
        ((IHwBinder)localObject3).transact(256398152, (HwParcel)localObject1, localHwParcel, 0);
        localHwParcel.verifySuccess();
        ((HwParcel)localObject1).releaseTemporaryStorage();
        localObject1 = new java/util/ArrayList;
        ((ArrayList)localObject1).<init>();
        localObject3 = localHwParcel.readBuffer(16L);
        int j = ((HwBlob)localObject3).getInt32(8L);
        HwBlob localHwBlob = localHwParcel.readEmbeddedBuffer(j * 32, ((HwBlob)localObject3).handle(), 0L, true);
        ((ArrayList)localObject1).clear();
        while (i < j)
        {
          localObject3 = new byte[32];
          localHwBlob.copyToInt8Array(i * 32, (byte[])localObject3, 32);
          ((ArrayList)localObject1).add(localObject3);
          i++;
        }
        return localObject1;
      }
      finally
      {
        localHwParcel.release();
      }
    }
    
    public void getIMSIForAppResponse(RadioResponseInfo paramRadioResponseInfo, String paramString)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeString(paramString);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(11, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getIccCardStatusResponse(RadioResponseInfo paramRadioResponseInfo, CardStatus paramCardStatus)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramCardStatus.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(1, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getImsRegistrationStateResponse(RadioResponseInfo paramRadioResponseInfo, boolean paramBoolean, int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeBool(paramBoolean);
      localHwParcel.writeInt32(paramInt);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(102, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getLastCallFailCauseResponse(RadioResponseInfo paramRadioResponseInfo, LastCallFailCauseInfo paramLastCallFailCauseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramLastCallFailCauseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(18, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getModemActivityInfoResponse(RadioResponseInfo paramRadioResponseInfo, ActivityStatsInfo paramActivityStatsInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramActivityStatsInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(123, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getMuteResponse(RadioResponseInfo paramRadioResponseInfo, boolean paramBoolean)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeBool(paramBoolean);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(52, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getNeighboringCidsResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<NeighboringCell> paramArrayList)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      NeighboringCell.writeVectorToParcel(localHwParcel, paramArrayList);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(66, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getNetworkSelectionModeResponse(RadioResponseInfo paramRadioResponseInfo, boolean paramBoolean)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeBool(paramBoolean);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(43, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getOperatorResponse(RadioResponseInfo paramRadioResponseInfo, String paramString1, String paramString2, String paramString3)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeString(paramString1);
      localHwParcel.writeString(paramString2);
      localHwParcel.writeString(paramString3);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(22, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getPreferredNetworkTypeResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeInt32(paramInt);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(65, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getPreferredVoicePrivacyResponse(RadioResponseInfo paramRadioResponseInfo, boolean paramBoolean)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeBool(paramBoolean);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(74, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getRadioCapabilityResponse(RadioResponseInfo paramRadioResponseInfo, RadioCapability paramRadioCapability)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioCapability.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(118, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getSignalStrengthResponse(RadioResponseInfo paramRadioResponseInfo, SignalStrength paramSignalStrength)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramSignalStrength.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(19, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getSmscAddressResponse(RadioResponseInfo paramRadioResponseInfo, String paramString)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeString(paramString);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(90, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getTTYModeResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeInt32(paramInt);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(72, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getVoiceRadioTechnologyResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeInt32(paramInt);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(98, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void getVoiceRegistrationStateResponse(RadioResponseInfo paramRadioResponseInfo, VoiceRegStateResult paramVoiceRegStateResult)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramVoiceRegStateResult.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(20, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void handleStkCallSetupRequestFromSimResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(62, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void hangupConnectionResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(12, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void hangupForegroundResumeBackgroundResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(14, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void hangupWaitingOrBackgroundResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(13, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public final int hashCode()
    {
      return asBinder().hashCode();
    }
    
    public void iccCloseLogicalChannelResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(106, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void iccIOForAppResponse(RadioResponseInfo paramRadioResponseInfo, IccIoResult paramIccIoResult)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramIccIoResult.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(28, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void iccOpenLogicalChannelResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt, ArrayList<Byte> paramArrayList)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeInt8Vector(paramArrayList);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(105, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void iccTransmitApduBasicChannelResponse(RadioResponseInfo paramRadioResponseInfo, IccIoResult paramIccIoResult)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramIccIoResult.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(104, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void iccTransmitApduLogicalChannelResponse(RadioResponseInfo paramRadioResponseInfo, IccIoResult paramIccIoResult)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramIccIoResult.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(107, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public ArrayList<String> interfaceChain()
      throws RemoteException
    {
      Object localObject1 = new HwParcel();
      ((HwParcel)localObject1).writeInterfaceToken("android.hidl.base@1.0::IBase");
      HwParcel localHwParcel = new HwParcel();
      try
      {
        mRemote.transact(256067662, (HwParcel)localObject1, localHwParcel, 0);
        localHwParcel.verifySuccess();
        ((HwParcel)localObject1).releaseTemporaryStorage();
        localObject1 = localHwParcel.readStringVector();
        return localObject1;
      }
      finally
      {
        localHwParcel.release();
      }
    }
    
    public String interfaceDescriptor()
      throws RemoteException
    {
      Object localObject1 = new HwParcel();
      ((HwParcel)localObject1).writeInterfaceToken("android.hidl.base@1.0::IBase");
      HwParcel localHwParcel = new HwParcel();
      try
      {
        mRemote.transact(256136003, (HwParcel)localObject1, localHwParcel, 0);
        localHwParcel.verifySuccess();
        ((HwParcel)localObject1).releaseTemporaryStorage();
        localObject1 = localHwParcel.readString();
        return localObject1;
      }
      finally
      {
        localHwParcel.release();
      }
    }
    
    public boolean linkToDeath(IHwBinder.DeathRecipient paramDeathRecipient, long paramLong)
      throws RemoteException
    {
      return mRemote.linkToDeath(paramDeathRecipient, paramLong);
    }
    
    public void notifySyspropsChanged()
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hidl.base@1.0::IBase");
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(257120595, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void nvReadItemResponse(RadioResponseInfo paramRadioResponseInfo, String paramString)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeString(paramString);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(108, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void nvResetConfigResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(111, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void nvWriteCdmaPrlResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(110, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void nvWriteItemResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(109, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void ping()
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hidl.base@1.0::IBase");
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(256921159, localHwParcel1, localHwParcel2, 0);
        localHwParcel2.verifySuccess();
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void pullLceDataResponse(RadioResponseInfo paramRadioResponseInfo, LceDataInfo paramLceDataInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramLceDataInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(122, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void rejectCallResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(17, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void reportSmsMemoryStatusResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(92, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void reportStkServiceIsRunningResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(93, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void requestIccSimAuthenticationResponse(RadioResponseInfo paramRadioResponseInfo, IccIoResult paramIccIoResult)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramIccIoResult.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(115, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void requestIsimAuthenticationResponse(RadioResponseInfo paramRadioResponseInfo, String paramString)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeString(paramString);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(95, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void requestShutdownResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(117, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void sendBurstDtmfResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(76, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void sendCDMAFeatureCodeResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(75, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void sendCdmaSmsResponse(RadioResponseInfo paramRadioResponseInfo, SendSmsResult paramSendSmsResult)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramSendSmsResult.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(77, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void sendDeviceStateResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(126, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void sendDtmfResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(24, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void sendEnvelopeResponse(RadioResponseInfo paramRadioResponseInfo, String paramString)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeString(paramString);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(60, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void sendEnvelopeWithStatusResponse(RadioResponseInfo paramRadioResponseInfo, IccIoResult paramIccIoResult)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramIccIoResult.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(97, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void sendImsSmsResponse(RadioResponseInfo paramRadioResponseInfo, SendSmsResult paramSendSmsResult)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramSendSmsResult.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(103, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void sendSMSExpectMoreResponse(RadioResponseInfo paramRadioResponseInfo, SendSmsResult paramSendSmsResult)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramSendSmsResult.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(26, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void sendSmsResponse(RadioResponseInfo paramRadioResponseInfo, SendSmsResult paramSendSmsResult)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramSendSmsResult.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(25, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void sendTerminalResponseToSimResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(61, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void sendUssdResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(29, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void separateConnectionResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(50, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setAllowedCarriersResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeInt32(paramInt);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(124, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setBandModeResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(58, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setBarringPasswordResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(42, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setCallForwardResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(34, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setCallWaitingResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(36, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setCdmaBroadcastActivationResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(84, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setCdmaBroadcastConfigResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(83, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setCdmaRoamingPreferenceResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(69, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setCdmaSubscriptionSourceResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(68, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setCellInfoListRateResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(100, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setClirResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(32, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setDataAllowedResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(113, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setDataProfileResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(116, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setFacilityLockForAppResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeInt32(paramInt);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(41, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setGsmBroadcastActivationResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(81, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setGsmBroadcastConfigResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(80, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setHALInstrumentation()
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hidl.base@1.0::IBase");
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(256462420, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void setIndicationFilterResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(127, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setInitialAttachApnResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(101, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setLocationUpdatesResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(67, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setMuteResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(51, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setNetworkSelectionModeAutomaticResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(44, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setNetworkSelectionModeManualResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(45, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setPreferredNetworkTypeResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(64, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setPreferredVoicePrivacyResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(73, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setRadioCapabilityResponse(RadioResponseInfo paramRadioResponseInfo, RadioCapability paramRadioCapability)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioCapability.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(119, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setRadioPowerResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(23, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setSimCardPowerResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(128, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setSmscAddressResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(91, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setSuppServiceNotificationsResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(55, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setTTYModeResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(71, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setUiccSubscriptionResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(112, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void setupDataCallResponse(RadioResponseInfo paramRadioResponseInfo, SetupDataCallResult paramSetupDataCallResult)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramSetupDataCallResult.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(27, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void startDtmfResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(47, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void startLceServiceResponse(RadioResponseInfo paramRadioResponseInfo, LceStatusInfo paramLceStatusInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramLceStatusInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(120, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void stopDtmfResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(48, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void stopLceServiceResponse(RadioResponseInfo paramRadioResponseInfo, LceStatusInfo paramLceStatusInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramLceStatusInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(121, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void supplyIccPin2ForAppResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeInt32(paramInt);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(4, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void supplyIccPinForAppResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeInt32(paramInt);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(2, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void supplyIccPuk2ForAppResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeInt32(paramInt);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(5, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void supplyIccPukForAppResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeInt32(paramInt);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(3, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void supplyNetworkDepersonalizationResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeInt32(paramInt);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(8, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void switchWaitingOrHoldingAndActiveResponse(RadioResponseInfo paramRadioResponseInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(15, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public String toString()
    {
      try
      {
        Object localObject = new java/lang/StringBuilder;
        ((StringBuilder)localObject).<init>();
        ((StringBuilder)localObject).append(interfaceDescriptor());
        ((StringBuilder)localObject).append("@Proxy");
        localObject = ((StringBuilder)localObject).toString();
        return localObject;
      }
      catch (RemoteException localRemoteException) {}
      return "[class or subclass of android.hardware.radio@1.0::IRadioResponse]@Proxy";
    }
    
    public boolean unlinkToDeath(IHwBinder.DeathRecipient paramDeathRecipient)
      throws RemoteException
    {
      return mRemote.unlinkToDeath(paramDeathRecipient);
    }
    
    public void writeSmsToRuimResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeInt32(paramInt);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(86, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
    
    public void writeSmsToSimResponse(RadioResponseInfo paramRadioResponseInfo, int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioResponse");
      paramRadioResponseInfo.writeToParcel(localHwParcel);
      localHwParcel.writeInt32(paramInt);
      paramRadioResponseInfo = new HwParcel();
      try
      {
        mRemote.transact(56, localHwParcel, paramRadioResponseInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioResponseInfo.release();
      }
    }
  }
  
  public static abstract class Stub
    extends HwBinder
    implements IRadioResponse
  {
    public Stub() {}
    
    public IHwBinder asBinder()
    {
      return this;
    }
    
    public final DebugInfo getDebugInfo()
    {
      DebugInfo localDebugInfo = new DebugInfo();
      pid = HidlSupport.getPidIfSharable();
      ptr = 0L;
      arch = 0;
      return localDebugInfo;
    }
    
    public final ArrayList<byte[]> getHashChain()
    {
      return new ArrayList(Arrays.asList(new byte[][] { { 45, -125, 58, -18, -48, -51, 29, 89, 67, 122, -54, 33, 11, -27, -112, -87, 83, -49, 50, -68, -74, 104, 60, -42, 61, 8, -105, 98, -90, 67, -5, 73 }, { -67, -38, -74, 24, 77, 122, 52, 109, -90, -96, 125, -64, -126, -116, -15, -102, 105, 111, 76, -86, 54, 17, -59, 31, 46, 20, 86, 90, 20, -76, 15, -39 } }));
    }
    
    public final ArrayList<String> interfaceChain()
    {
      return new ArrayList(Arrays.asList(new String[] { "android.hardware.radio@1.0::IRadioResponse", "android.hidl.base@1.0::IBase" }));
    }
    
    public final String interfaceDescriptor()
    {
      return "android.hardware.radio@1.0::IRadioResponse";
    }
    
    public final boolean linkToDeath(IHwBinder.DeathRecipient paramDeathRecipient, long paramLong)
    {
      return true;
    }
    
    public final void notifySyspropsChanged() {}
    
    public void onTransact(int paramInt1, HwParcel paramHwParcel1, HwParcel paramHwParcel2, int paramInt2)
      throws RemoteException
    {
      int i = 0;
      int j = 0;
      int k = 0;
      int m = 0;
      int n = 0;
      int i1 = 0;
      int i2 = 0;
      int i3 = 0;
      int i4 = 0;
      int i5 = 0;
      int i6 = 0;
      int i7 = 0;
      int i8 = 0;
      int i9 = 0;
      int i10 = 0;
      int i11 = 0;
      int i12 = 0;
      int i13 = 0;
      int i14 = 0;
      int i15 = 0;
      int i16 = 0;
      int i17 = 0;
      int i18 = 0;
      int i19 = 0;
      int i20 = 0;
      int i21 = 0;
      int i22 = 0;
      int i23 = 0;
      int i24 = 0;
      int i25 = 0;
      int i26 = 0;
      int i27 = 0;
      int i28 = 0;
      int i29 = 0;
      int i30 = 0;
      int i31 = 0;
      int i32 = 0;
      int i33 = 0;
      int i34 = 0;
      int i35 = 0;
      int i36 = 0;
      int i37 = 0;
      int i38 = 0;
      int i39 = 0;
      int i40 = 0;
      int i41 = 0;
      int i42 = 0;
      int i43 = 0;
      int i44 = 0;
      int i45 = 0;
      int i46 = 0;
      int i47 = 0;
      int i48 = 0;
      int i49 = 0;
      int i50 = 0;
      int i51 = 0;
      int i52 = 0;
      int i53 = 0;
      int i54 = 0;
      int i55 = 0;
      int i56 = 0;
      int i57 = 0;
      int i58 = 0;
      int i59 = 0;
      int i60 = 0;
      int i61 = 0;
      int i62 = 0;
      int i63 = 0;
      int i64 = 0;
      int i65 = 0;
      int i66 = 0;
      int i67 = 0;
      int i68 = 0;
      int i69 = 0;
      int i70 = 0;
      int i71 = 0;
      int i72 = 0;
      int i73 = 0;
      int i74 = 0;
      int i75 = 0;
      int i76 = 0;
      int i77 = 0;
      int i78 = 0;
      int i79 = 0;
      int i80 = 0;
      int i81 = 0;
      int i82 = 0;
      int i83 = 0;
      int i84 = 0;
      int i85 = 0;
      int i86 = 0;
      int i87 = 0;
      int i88 = 0;
      int i89 = 0;
      int i90 = 0;
      int i91 = 0;
      int i92 = 0;
      int i93 = 0;
      int i94 = 0;
      int i95 = 0;
      int i96 = 0;
      int i97 = 0;
      int i98 = 0;
      int i99 = 0;
      int i100 = 0;
      int i101 = 0;
      int i102 = 0;
      int i103 = 0;
      int i104 = 0;
      int i105 = 0;
      int i106 = 0;
      int i107 = 0;
      int i108 = 0;
      int i109 = 0;
      int i110 = 0;
      int i111 = 0;
      int i112 = 0;
      int i113 = 0;
      int i114 = 0;
      int i115 = 0;
      int i116 = 0;
      int i117 = 0;
      int i118 = 0;
      int i119 = 0;
      int i120 = 0;
      int i121 = 0;
      int i122 = 0;
      int i123 = 0;
      int i124 = 0;
      int i125 = 0;
      int i126 = 0;
      int i127 = 0;
      int i128 = 0;
      int i129 = 0;
      int i130 = 1;
      int i131 = 1;
      int i132 = 1;
      int i133 = 1;
      int i134 = 1;
      int i135 = 1;
      Object localObject;
      switch (paramInt1)
      {
      default: 
        switch (paramInt1)
        {
        default: 
          break;
        case 257250372: 
          paramInt1 = i129;
          if ((paramInt2 & 0x1) != 0) {
            paramInt1 = 1;
          }
          if (paramInt1 != 0)
          {
            paramHwParcel2.writeStatus(Integer.MIN_VALUE);
            paramHwParcel2.send();
          }
          break;
        case 257120595: 
          paramInt1 = i;
          if ((paramInt2 & 0x1) != 0) {
            paramInt1 = 1;
          }
          if (paramInt1 != 1)
          {
            paramHwParcel2.writeStatus(Integer.MIN_VALUE);
            paramHwParcel2.send();
          }
          else
          {
            paramHwParcel1.enforceInterface("android.hidl.base@1.0::IBase");
            notifySyspropsChanged();
          }
          break;
        case 257049926: 
          if ((paramInt2 & 0x1) != 0) {
            paramInt1 = i135;
          } else {
            paramInt1 = 0;
          }
          if (paramInt1 != 0)
          {
            paramHwParcel2.writeStatus(Integer.MIN_VALUE);
            paramHwParcel2.send();
          }
          else
          {
            paramHwParcel1.enforceInterface("android.hidl.base@1.0::IBase");
            paramHwParcel1 = getDebugInfo();
            paramHwParcel2.writeStatus(0);
            paramHwParcel1.writeToParcel(paramHwParcel2);
            paramHwParcel2.send();
          }
          break;
        case 256921159: 
          if ((paramInt2 & 0x1) != 0) {
            paramInt1 = i130;
          } else {
            paramInt1 = 0;
          }
          if (paramInt1 != 0)
          {
            paramHwParcel2.writeStatus(Integer.MIN_VALUE);
            paramHwParcel2.send();
          }
          else
          {
            paramHwParcel1.enforceInterface("android.hidl.base@1.0::IBase");
            ping();
            paramHwParcel2.writeStatus(0);
            paramHwParcel2.send();
          }
          break;
        case 256660548: 
          paramInt1 = j;
          if ((paramInt2 & 0x1) != 0) {
            paramInt1 = 1;
          }
          if (paramInt1 != 0)
          {
            paramHwParcel2.writeStatus(Integer.MIN_VALUE);
            paramHwParcel2.send();
          }
          break;
        case 256462420: 
          paramInt1 = k;
          if ((paramInt2 & 0x1) != 0) {
            paramInt1 = 1;
          }
          if (paramInt1 != 1)
          {
            paramHwParcel2.writeStatus(Integer.MIN_VALUE);
            paramHwParcel2.send();
          }
          else
          {
            paramHwParcel1.enforceInterface("android.hidl.base@1.0::IBase");
            setHALInstrumentation();
          }
          break;
        case 256398152: 
          if ((paramInt2 & 0x1) != 0) {
            paramInt1 = i131;
          } else {
            paramInt1 = 0;
          }
          if (paramInt1 != 0)
          {
            paramHwParcel2.writeStatus(Integer.MIN_VALUE);
            paramHwParcel2.send();
          }
          else
          {
            paramHwParcel1.enforceInterface("android.hidl.base@1.0::IBase");
            paramHwParcel1 = getHashChain();
            paramHwParcel2.writeStatus(0);
            localObject = new HwBlob(16);
            paramInt2 = paramHwParcel1.size();
            ((HwBlob)localObject).putInt32(8L, paramInt2);
            ((HwBlob)localObject).putBool(12L, false);
            HwBlob localHwBlob = new HwBlob(paramInt2 * 32);
            for (paramInt1 = m; paramInt1 < paramInt2; paramInt1++) {
              localHwBlob.putInt8Array(paramInt1 * 32, (byte[])paramHwParcel1.get(paramInt1));
            }
            ((HwBlob)localObject).putBlob(0L, localHwBlob);
            paramHwParcel2.writeBuffer((HwBlob)localObject);
            paramHwParcel2.send();
          }
          break;
        case 256136003: 
          if ((paramInt2 & 0x1) != 0) {
            paramInt1 = i132;
          } else {
            paramInt1 = 0;
          }
          if (paramInt1 != 0)
          {
            paramHwParcel2.writeStatus(Integer.MIN_VALUE);
            paramHwParcel2.send();
          }
          else
          {
            paramHwParcel1.enforceInterface("android.hidl.base@1.0::IBase");
            paramHwParcel1 = interfaceDescriptor();
            paramHwParcel2.writeStatus(0);
            paramHwParcel2.writeString(paramHwParcel1);
            paramHwParcel2.send();
          }
          break;
        case 256131655: 
          if ((paramInt2 & 0x1) != 0) {
            paramInt1 = i133;
          } else {
            paramInt1 = 0;
          }
          if (paramInt1 != 0)
          {
            paramHwParcel2.writeStatus(Integer.MIN_VALUE);
            paramHwParcel2.send();
          }
          else
          {
            paramHwParcel1.enforceInterface("android.hidl.base@1.0::IBase");
            paramHwParcel2.writeStatus(0);
            paramHwParcel2.send();
          }
          break;
        case 256067662: 
          if ((paramInt2 & 0x1) != 0) {
            paramInt1 = i134;
          } else {
            paramInt1 = 0;
          }
          if (paramInt1 != 0)
          {
            paramHwParcel2.writeStatus(Integer.MIN_VALUE);
            paramHwParcel2.send();
          }
          else
          {
            paramHwParcel1.enforceInterface("android.hidl.base@1.0::IBase");
            paramHwParcel1 = interfaceChain();
            paramHwParcel2.writeStatus(0);
            paramHwParcel2.writeStringVector(paramHwParcel1);
            paramHwParcel2.send();
          }
          break;
        }
        break;
      case 129: 
        paramInt1 = n;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          acknowledgeRequest(paramHwParcel1.readInt32());
        }
        break;
      case 128: 
        paramInt1 = i1;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setSimCardPowerResponse(paramHwParcel2);
        }
        break;
      case 127: 
        paramInt1 = i2;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setIndicationFilterResponse(paramHwParcel2);
        }
        break;
      case 126: 
        paramInt1 = i3;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          sendDeviceStateResponse(paramHwParcel2);
        }
        break;
      case 125: 
        paramInt1 = i4;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          boolean bool = paramHwParcel1.readBool();
          localObject = new CarrierRestrictions();
          ((CarrierRestrictions)localObject).readFromParcel(paramHwParcel1);
          getAllowedCarriersResponse(paramHwParcel2, bool, (CarrierRestrictions)localObject);
        }
        break;
      case 124: 
        paramInt1 = i5;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setAllowedCarriersResponse(paramHwParcel2, paramHwParcel1.readInt32());
        }
        break;
      case 123: 
        paramInt1 = i6;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          localObject = new ActivityStatsInfo();
          ((ActivityStatsInfo)localObject).readFromParcel(paramHwParcel1);
          getModemActivityInfoResponse(paramHwParcel2, (ActivityStatsInfo)localObject);
        }
        break;
      case 122: 
        paramInt1 = i7;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          localObject = new LceDataInfo();
          ((LceDataInfo)localObject).readFromParcel(paramHwParcel1);
          pullLceDataResponse(paramHwParcel2, (LceDataInfo)localObject);
        }
        break;
      case 121: 
        paramInt1 = i8;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          localObject = new RadioResponseInfo();
          ((RadioResponseInfo)localObject).readFromParcel(paramHwParcel1);
          paramHwParcel2 = new LceStatusInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          stopLceServiceResponse((RadioResponseInfo)localObject, paramHwParcel2);
        }
        break;
      case 120: 
        paramInt1 = i9;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          localObject = new LceStatusInfo();
          ((LceStatusInfo)localObject).readFromParcel(paramHwParcel1);
          startLceServiceResponse(paramHwParcel2, (LceStatusInfo)localObject);
        }
        break;
      case 119: 
        paramInt1 = i10;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          localObject = new RadioCapability();
          ((RadioCapability)localObject).readFromParcel(paramHwParcel1);
          setRadioCapabilityResponse(paramHwParcel2, (RadioCapability)localObject);
        }
        break;
      case 118: 
        paramInt1 = i11;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          localObject = new RadioCapability();
          ((RadioCapability)localObject).readFromParcel(paramHwParcel1);
          getRadioCapabilityResponse(paramHwParcel2, (RadioCapability)localObject);
        }
        break;
      case 117: 
        paramInt1 = i12;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          requestShutdownResponse(paramHwParcel2);
        }
        break;
      case 116: 
        paramInt1 = i13;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setDataProfileResponse(paramHwParcel2);
        }
        break;
      case 115: 
        paramInt1 = i14;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          localObject = new RadioResponseInfo();
          ((RadioResponseInfo)localObject).readFromParcel(paramHwParcel1);
          paramHwParcel2 = new IccIoResult();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          requestIccSimAuthenticationResponse((RadioResponseInfo)localObject, paramHwParcel2);
        }
        break;
      case 114: 
        paramInt1 = i15;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getHardwareConfigResponse(paramHwParcel2, HardwareConfig.readVectorFromParcel(paramHwParcel1));
        }
        break;
      case 113: 
        paramInt1 = i16;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setDataAllowedResponse(paramHwParcel2);
        }
        break;
      case 112: 
        paramInt1 = i17;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setUiccSubscriptionResponse(paramHwParcel2);
        }
        break;
      case 111: 
        paramInt1 = i18;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          nvResetConfigResponse(paramHwParcel2);
        }
        break;
      case 110: 
        paramInt1 = i19;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          nvWriteCdmaPrlResponse(paramHwParcel2);
        }
        break;
      case 109: 
        paramInt1 = i20;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          nvWriteItemResponse(paramHwParcel2);
        }
        break;
      case 108: 
        paramInt1 = i21;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          nvReadItemResponse(paramHwParcel2, paramHwParcel1.readString());
        }
        break;
      case 107: 
        paramInt1 = i22;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          localObject = new RadioResponseInfo();
          ((RadioResponseInfo)localObject).readFromParcel(paramHwParcel1);
          paramHwParcel2 = new IccIoResult();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          iccTransmitApduLogicalChannelResponse((RadioResponseInfo)localObject, paramHwParcel2);
        }
        break;
      case 106: 
        paramInt1 = i23;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          iccCloseLogicalChannelResponse(paramHwParcel2);
        }
        break;
      case 105: 
        paramInt1 = i24;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          iccOpenLogicalChannelResponse(paramHwParcel2, paramHwParcel1.readInt32(), paramHwParcel1.readInt8Vector());
        }
        break;
      case 104: 
        paramInt1 = i25;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          localObject = new RadioResponseInfo();
          ((RadioResponseInfo)localObject).readFromParcel(paramHwParcel1);
          paramHwParcel2 = new IccIoResult();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          iccTransmitApduBasicChannelResponse((RadioResponseInfo)localObject, paramHwParcel2);
        }
        break;
      case 103: 
        paramInt1 = i26;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          localObject = new SendSmsResult();
          ((SendSmsResult)localObject).readFromParcel(paramHwParcel1);
          sendImsSmsResponse(paramHwParcel2, (SendSmsResult)localObject);
        }
        break;
      case 102: 
        paramInt1 = i27;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getImsRegistrationStateResponse(paramHwParcel2, paramHwParcel1.readBool(), paramHwParcel1.readInt32());
        }
        break;
      case 101: 
        paramInt1 = i28;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setInitialAttachApnResponse(paramHwParcel2);
        }
        break;
      case 100: 
        paramInt1 = i29;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setCellInfoListRateResponse(paramHwParcel2);
        }
        break;
      case 99: 
        paramInt1 = i30;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getCellInfoListResponse(paramHwParcel2, CellInfo.readVectorFromParcel(paramHwParcel1));
        }
        break;
      case 98: 
        paramInt1 = i31;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getVoiceRadioTechnologyResponse(paramHwParcel2, paramHwParcel1.readInt32());
        }
        break;
      case 97: 
        paramInt1 = i32;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          localObject = new IccIoResult();
          ((IccIoResult)localObject).readFromParcel(paramHwParcel1);
          sendEnvelopeWithStatusResponse(paramHwParcel2, (IccIoResult)localObject);
        }
        break;
      case 96: 
        paramInt1 = i33;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          acknowledgeIncomingGsmSmsWithPduResponse(paramHwParcel2);
        }
        break;
      case 95: 
        paramInt1 = i34;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          requestIsimAuthenticationResponse(paramHwParcel2, paramHwParcel1.readString());
        }
        break;
      case 94: 
        paramInt1 = i35;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getCdmaSubscriptionSourceResponse(paramHwParcel2, paramHwParcel1.readInt32());
        }
        break;
      case 93: 
        paramInt1 = i36;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          reportStkServiceIsRunningResponse(paramHwParcel2);
        }
        break;
      case 92: 
        paramInt1 = i37;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          reportSmsMemoryStatusResponse(paramHwParcel2);
        }
        break;
      case 91: 
        paramInt1 = i38;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setSmscAddressResponse(paramHwParcel2);
        }
        break;
      case 90: 
        paramInt1 = i39;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getSmscAddressResponse(paramHwParcel2, paramHwParcel1.readString());
        }
        break;
      case 89: 
        paramInt1 = i40;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          exitEmergencyCallbackModeResponse(paramHwParcel2);
        }
        break;
      case 88: 
        paramInt1 = i41;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getDeviceIdentityResponse(paramHwParcel2, paramHwParcel1.readString(), paramHwParcel1.readString(), paramHwParcel1.readString(), paramHwParcel1.readString());
        }
        break;
      case 87: 
        paramInt1 = i42;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          deleteSmsOnRuimResponse(paramHwParcel2);
        }
        break;
      case 86: 
        paramInt1 = i43;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          writeSmsToRuimResponse(paramHwParcel2, paramHwParcel1.readInt32());
        }
        break;
      case 85: 
        paramInt1 = i44;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getCDMASubscriptionResponse(paramHwParcel2, paramHwParcel1.readString(), paramHwParcel1.readString(), paramHwParcel1.readString(), paramHwParcel1.readString(), paramHwParcel1.readString());
        }
        break;
      case 84: 
        paramInt1 = i45;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setCdmaBroadcastActivationResponse(paramHwParcel2);
        }
        break;
      case 83: 
        paramInt1 = i46;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setCdmaBroadcastConfigResponse(paramHwParcel2);
        }
        break;
      case 82: 
        paramInt1 = i47;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getCdmaBroadcastConfigResponse(paramHwParcel2, CdmaBroadcastSmsConfigInfo.readVectorFromParcel(paramHwParcel1));
        }
        break;
      case 81: 
        paramInt1 = i48;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setGsmBroadcastActivationResponse(paramHwParcel2);
        }
        break;
      case 80: 
        paramInt1 = i49;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setGsmBroadcastConfigResponse(paramHwParcel2);
        }
        break;
      case 79: 
        paramInt1 = i50;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getGsmBroadcastConfigResponse(paramHwParcel2, GsmBroadcastSmsConfigInfo.readVectorFromParcel(paramHwParcel1));
        }
        break;
      case 78: 
        paramInt1 = i51;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          acknowledgeLastIncomingCdmaSmsResponse(paramHwParcel2);
        }
        break;
      case 77: 
        paramInt1 = i52;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          localObject = new SendSmsResult();
          ((SendSmsResult)localObject).readFromParcel(paramHwParcel1);
          sendCdmaSmsResponse(paramHwParcel2, (SendSmsResult)localObject);
        }
        break;
      case 76: 
        paramInt1 = i53;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          sendBurstDtmfResponse(paramHwParcel2);
        }
        break;
      case 75: 
        paramInt1 = i54;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          sendCDMAFeatureCodeResponse(paramHwParcel2);
        }
        break;
      case 74: 
        paramInt1 = i55;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getPreferredVoicePrivacyResponse(paramHwParcel2, paramHwParcel1.readBool());
        }
        break;
      case 73: 
        paramInt1 = i56;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setPreferredVoicePrivacyResponse(paramHwParcel2);
        }
        break;
      case 72: 
        paramInt1 = i57;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getTTYModeResponse(paramHwParcel2, paramHwParcel1.readInt32());
        }
        break;
      case 71: 
        paramInt1 = i58;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setTTYModeResponse(paramHwParcel2);
        }
        break;
      case 70: 
        paramInt1 = i59;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getCdmaRoamingPreferenceResponse(paramHwParcel2, paramHwParcel1.readInt32());
        }
        break;
      case 69: 
        paramInt1 = i60;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setCdmaRoamingPreferenceResponse(paramHwParcel2);
        }
        break;
      case 68: 
        paramInt1 = i61;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setCdmaSubscriptionSourceResponse(paramHwParcel2);
        }
        break;
      case 67: 
        paramInt1 = i62;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setLocationUpdatesResponse(paramHwParcel2);
        }
        break;
      case 66: 
        paramInt1 = i63;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getNeighboringCidsResponse(paramHwParcel2, NeighboringCell.readVectorFromParcel(paramHwParcel1));
        }
        break;
      case 65: 
        paramInt1 = i64;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getPreferredNetworkTypeResponse(paramHwParcel2, paramHwParcel1.readInt32());
        }
        break;
      case 64: 
        paramInt1 = i65;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setPreferredNetworkTypeResponse(paramHwParcel2);
        }
        break;
      case 63: 
        paramInt1 = i66;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          explicitCallTransferResponse(paramHwParcel2);
        }
        break;
      case 62: 
        paramInt1 = i67;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          handleStkCallSetupRequestFromSimResponse(paramHwParcel2);
        }
        break;
      case 61: 
        paramInt1 = i68;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          sendTerminalResponseToSimResponse(paramHwParcel2);
        }
        break;
      case 60: 
        paramInt1 = i69;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          sendEnvelopeResponse(paramHwParcel2, paramHwParcel1.readString());
        }
        break;
      case 59: 
        paramInt1 = i70;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getAvailableBandModesResponse(paramHwParcel2, paramHwParcel1.readInt32Vector());
        }
        break;
      case 58: 
        paramInt1 = i71;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setBandModeResponse(paramHwParcel2);
        }
        break;
      case 57: 
        paramInt1 = i72;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          deleteSmsOnSimResponse(paramHwParcel2);
        }
        break;
      case 56: 
        paramInt1 = i73;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          writeSmsToSimResponse(paramHwParcel2, paramHwParcel1.readInt32());
        }
        break;
      case 55: 
        paramInt1 = i74;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setSuppServiceNotificationsResponse(paramHwParcel2);
        }
        break;
      case 54: 
        paramInt1 = i75;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getDataCallListResponse(paramHwParcel2, SetupDataCallResult.readVectorFromParcel(paramHwParcel1));
        }
        break;
      case 53: 
        paramInt1 = i76;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getClipResponse(paramHwParcel2, paramHwParcel1.readInt32());
        }
        break;
      case 52: 
        paramInt1 = i77;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getMuteResponse(paramHwParcel2, paramHwParcel1.readBool());
        }
        break;
      case 51: 
        paramInt1 = i78;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setMuteResponse(paramHwParcel2);
        }
        break;
      case 50: 
        paramInt1 = i79;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          separateConnectionResponse(paramHwParcel2);
        }
        break;
      case 49: 
        paramInt1 = i80;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getBasebandVersionResponse(paramHwParcel2, paramHwParcel1.readString());
        }
        break;
      case 48: 
        paramInt1 = i81;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          stopDtmfResponse(paramHwParcel2);
        }
        break;
      case 47: 
        paramInt1 = i82;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          startDtmfResponse(paramHwParcel2);
        }
        break;
      case 46: 
        paramInt1 = i83;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getAvailableNetworksResponse(paramHwParcel2, OperatorInfo.readVectorFromParcel(paramHwParcel1));
        }
        break;
      case 45: 
        paramInt1 = i84;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setNetworkSelectionModeManualResponse(paramHwParcel2);
        }
        break;
      case 44: 
        paramInt1 = i85;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setNetworkSelectionModeAutomaticResponse(paramHwParcel2);
        }
        break;
      case 43: 
        paramInt1 = i86;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getNetworkSelectionModeResponse(paramHwParcel2, paramHwParcel1.readBool());
        }
        break;
      case 42: 
        paramInt1 = i87;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setBarringPasswordResponse(paramHwParcel2);
        }
        break;
      case 41: 
        paramInt1 = i88;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setFacilityLockForAppResponse(paramHwParcel2, paramHwParcel1.readInt32());
        }
        break;
      case 40: 
        paramInt1 = i89;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getFacilityLockForAppResponse(paramHwParcel2, paramHwParcel1.readInt32());
        }
        break;
      case 39: 
        paramInt1 = i90;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          deactivateDataCallResponse(paramHwParcel2);
        }
        break;
      case 38: 
        paramInt1 = i91;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          acceptCallResponse(paramHwParcel2);
        }
        break;
      case 37: 
        paramInt1 = i92;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          acknowledgeLastIncomingGsmSmsResponse(paramHwParcel2);
        }
        break;
      case 36: 
        paramInt1 = i93;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setCallWaitingResponse(paramHwParcel2);
        }
        break;
      case 35: 
        paramInt1 = i94;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getCallWaitingResponse(paramHwParcel2, paramHwParcel1.readBool(), paramHwParcel1.readInt32());
        }
        break;
      case 34: 
        paramInt1 = i95;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setCallForwardResponse(paramHwParcel2);
        }
        break;
      case 33: 
        paramInt1 = i96;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getCallForwardStatusResponse(paramHwParcel2, CallForwardInfo.readVectorFromParcel(paramHwParcel1));
        }
        break;
      case 32: 
        paramInt1 = i97;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setClirResponse(paramHwParcel2);
        }
        break;
      case 31: 
        paramInt1 = i98;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getClirResponse(paramHwParcel2, paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 30: 
        paramInt1 = i99;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          cancelPendingUssdResponse(paramHwParcel2);
        }
        break;
      case 29: 
        paramInt1 = i100;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          sendUssdResponse(paramHwParcel2);
        }
        break;
      case 28: 
        paramInt1 = i101;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          localObject = new IccIoResult();
          ((IccIoResult)localObject).readFromParcel(paramHwParcel1);
          iccIOForAppResponse(paramHwParcel2, (IccIoResult)localObject);
        }
        break;
      case 27: 
        paramInt1 = i102;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          localObject = new SetupDataCallResult();
          ((SetupDataCallResult)localObject).readFromParcel(paramHwParcel1);
          setupDataCallResponse(paramHwParcel2, (SetupDataCallResult)localObject);
        }
        break;
      case 26: 
        paramInt1 = i103;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          localObject = new SendSmsResult();
          ((SendSmsResult)localObject).readFromParcel(paramHwParcel1);
          sendSMSExpectMoreResponse(paramHwParcel2, (SendSmsResult)localObject);
        }
        break;
      case 25: 
        paramInt1 = i104;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          localObject = new SendSmsResult();
          ((SendSmsResult)localObject).readFromParcel(paramHwParcel1);
          sendSmsResponse(paramHwParcel2, (SendSmsResult)localObject);
        }
        break;
      case 24: 
        paramInt1 = i105;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          sendDtmfResponse(paramHwParcel2);
        }
        break;
      case 23: 
        paramInt1 = i106;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setRadioPowerResponse(paramHwParcel2);
        }
        break;
      case 22: 
        paramInt1 = i107;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getOperatorResponse(paramHwParcel2, paramHwParcel1.readString(), paramHwParcel1.readString(), paramHwParcel1.readString());
        }
        break;
      case 21: 
        paramInt1 = i108;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          localObject = new DataRegStateResult();
          ((DataRegStateResult)localObject).readFromParcel(paramHwParcel1);
          getDataRegistrationStateResponse(paramHwParcel2, (DataRegStateResult)localObject);
        }
        break;
      case 20: 
        paramInt1 = i109;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          localObject = new RadioResponseInfo();
          ((RadioResponseInfo)localObject).readFromParcel(paramHwParcel1);
          paramHwParcel2 = new VoiceRegStateResult();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getVoiceRegistrationStateResponse((RadioResponseInfo)localObject, paramHwParcel2);
        }
        break;
      case 19: 
        paramInt1 = i110;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          localObject = new SignalStrength();
          ((SignalStrength)localObject).readFromParcel(paramHwParcel1);
          getSignalStrengthResponse(paramHwParcel2, (SignalStrength)localObject);
        }
        break;
      case 18: 
        paramInt1 = i111;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          localObject = new LastCallFailCauseInfo();
          ((LastCallFailCauseInfo)localObject).readFromParcel(paramHwParcel1);
          getLastCallFailCauseResponse(paramHwParcel2, (LastCallFailCauseInfo)localObject);
        }
        break;
      case 17: 
        paramInt1 = i112;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          rejectCallResponse(paramHwParcel2);
        }
        break;
      case 16: 
        paramInt1 = i113;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          conferenceResponse(paramHwParcel2);
        }
        break;
      case 15: 
        paramInt1 = i114;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          switchWaitingOrHoldingAndActiveResponse(paramHwParcel2);
        }
        break;
      case 14: 
        paramInt1 = i115;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          hangupForegroundResumeBackgroundResponse(paramHwParcel2);
        }
        break;
      case 13: 
        paramInt1 = i116;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          hangupWaitingOrBackgroundResponse(paramHwParcel2);
        }
        break;
      case 12: 
        paramInt1 = i117;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          hangupConnectionResponse(paramHwParcel2);
        }
        break;
      case 11: 
        paramInt1 = i118;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getIMSIForAppResponse(paramHwParcel2, paramHwParcel1.readString());
        }
        break;
      case 10: 
        paramInt1 = i119;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          dialResponse(paramHwParcel2);
        }
        break;
      case 9: 
        paramInt1 = i120;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getCurrentCallsResponse(paramHwParcel2, Call.readVectorFromParcel(paramHwParcel1));
        }
        break;
      case 8: 
        paramInt1 = i121;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          supplyNetworkDepersonalizationResponse(paramHwParcel2, paramHwParcel1.readInt32());
        }
        break;
      case 7: 
        paramInt1 = i122;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          changeIccPin2ForAppResponse(paramHwParcel2, paramHwParcel1.readInt32());
        }
        break;
      case 6: 
        paramInt1 = i123;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          changeIccPinForAppResponse(paramHwParcel2, paramHwParcel1.readInt32());
        }
        break;
      case 5: 
        paramInt1 = i124;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          supplyIccPuk2ForAppResponse(paramHwParcel2, paramHwParcel1.readInt32());
        }
        break;
      case 4: 
        paramInt1 = i125;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          supplyIccPin2ForAppResponse(paramHwParcel2, paramHwParcel1.readInt32());
        }
        break;
      case 3: 
        paramInt1 = i126;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          supplyIccPukForAppResponse(paramHwParcel2, paramHwParcel1.readInt32());
        }
        break;
      case 2: 
        paramInt1 = i127;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          paramHwParcel2 = new RadioResponseInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          supplyIccPinForAppResponse(paramHwParcel2, paramHwParcel1.readInt32());
        }
        break;
      case 1: 
        paramInt1 = i128;
        if ((paramInt2 & 0x1) != 0) {
          paramInt1 = 1;
        }
        if (paramInt1 != 1)
        {
          paramHwParcel2.writeStatus(Integer.MIN_VALUE);
          paramHwParcel2.send();
        }
        else
        {
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioResponse");
          localObject = new RadioResponseInfo();
          ((RadioResponseInfo)localObject).readFromParcel(paramHwParcel1);
          paramHwParcel2 = new CardStatus();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getIccCardStatusResponse((RadioResponseInfo)localObject, paramHwParcel2);
        }
        break;
      }
    }
    
    public final void ping() {}
    
    public IHwInterface queryLocalInterface(String paramString)
    {
      if ("android.hardware.radio@1.0::IRadioResponse".equals(paramString)) {
        return this;
      }
      return null;
    }
    
    public void registerAsService(String paramString)
      throws RemoteException
    {
      registerService(paramString);
    }
    
    public final void setHALInstrumentation() {}
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(interfaceDescriptor());
      localStringBuilder.append("@Stub");
      return localStringBuilder.toString();
    }
    
    public final boolean unlinkToDeath(IHwBinder.DeathRecipient paramDeathRecipient)
    {
      return true;
    }
  }
}
