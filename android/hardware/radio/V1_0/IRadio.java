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

public abstract interface IRadio
  extends IBase
{
  public static final String kInterfaceName = "android.hardware.radio@1.0::IRadio";
  
  public static IRadio asInterface(IHwBinder paramIHwBinder)
  {
    if (paramIHwBinder == null) {
      return null;
    }
    Object localObject = paramIHwBinder.queryLocalInterface("android.hardware.radio@1.0::IRadio");
    if ((localObject != null) && ((localObject instanceof IRadio))) {
      return (IRadio)localObject;
    }
    paramIHwBinder = new Proxy(paramIHwBinder);
    try
    {
      localObject = paramIHwBinder.interfaceChain().iterator();
      while (((Iterator)localObject).hasNext())
      {
        boolean bool = ((String)((Iterator)localObject).next()).equals("android.hardware.radio@1.0::IRadio");
        if (bool) {
          return paramIHwBinder;
        }
      }
    }
    catch (RemoteException paramIHwBinder) {}
    return null;
  }
  
  public static IRadio castFrom(IHwInterface paramIHwInterface)
  {
    if (paramIHwInterface == null) {
      paramIHwInterface = null;
    } else {
      paramIHwInterface = asInterface(paramIHwInterface.asBinder());
    }
    return paramIHwInterface;
  }
  
  public static IRadio getService()
    throws RemoteException
  {
    return getService("default");
  }
  
  public static IRadio getService(String paramString)
    throws RemoteException
  {
    return asInterface(HwBinder.getService("android.hardware.radio@1.0::IRadio", paramString));
  }
  
  public static IRadio getService(String paramString, boolean paramBoolean)
    throws RemoteException
  {
    return asInterface(HwBinder.getService("android.hardware.radio@1.0::IRadio", paramString, paramBoolean));
  }
  
  public static IRadio getService(boolean paramBoolean)
    throws RemoteException
  {
    return getService("default", paramBoolean);
  }
  
  public abstract void acceptCall(int paramInt)
    throws RemoteException;
  
  public abstract void acknowledgeIncomingGsmSmsWithPdu(int paramInt, boolean paramBoolean, String paramString)
    throws RemoteException;
  
  public abstract void acknowledgeLastIncomingCdmaSms(int paramInt, CdmaSmsAck paramCdmaSmsAck)
    throws RemoteException;
  
  public abstract void acknowledgeLastIncomingGsmSms(int paramInt1, boolean paramBoolean, int paramInt2)
    throws RemoteException;
  
  public abstract IHwBinder asBinder();
  
  public abstract void cancelPendingUssd(int paramInt)
    throws RemoteException;
  
  public abstract void changeIccPin2ForApp(int paramInt, String paramString1, String paramString2, String paramString3)
    throws RemoteException;
  
  public abstract void changeIccPinForApp(int paramInt, String paramString1, String paramString2, String paramString3)
    throws RemoteException;
  
  public abstract void conference(int paramInt)
    throws RemoteException;
  
  public abstract void deactivateDataCall(int paramInt1, int paramInt2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void deleteSmsOnRuim(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void deleteSmsOnSim(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void dial(int paramInt, Dial paramDial)
    throws RemoteException;
  
  public abstract void exitEmergencyCallbackMode(int paramInt)
    throws RemoteException;
  
  public abstract void explicitCallTransfer(int paramInt)
    throws RemoteException;
  
  public abstract void getAllowedCarriers(int paramInt)
    throws RemoteException;
  
  public abstract void getAvailableBandModes(int paramInt)
    throws RemoteException;
  
  public abstract void getAvailableNetworks(int paramInt)
    throws RemoteException;
  
  public abstract void getBasebandVersion(int paramInt)
    throws RemoteException;
  
  public abstract void getCDMASubscription(int paramInt)
    throws RemoteException;
  
  public abstract void getCallForwardStatus(int paramInt, CallForwardInfo paramCallForwardInfo)
    throws RemoteException;
  
  public abstract void getCallWaiting(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void getCdmaBroadcastConfig(int paramInt)
    throws RemoteException;
  
  public abstract void getCdmaRoamingPreference(int paramInt)
    throws RemoteException;
  
  public abstract void getCdmaSubscriptionSource(int paramInt)
    throws RemoteException;
  
  public abstract void getCellInfoList(int paramInt)
    throws RemoteException;
  
  public abstract void getClip(int paramInt)
    throws RemoteException;
  
  public abstract void getClir(int paramInt)
    throws RemoteException;
  
  public abstract void getCurrentCalls(int paramInt)
    throws RemoteException;
  
  public abstract void getDataCallList(int paramInt)
    throws RemoteException;
  
  public abstract void getDataRegistrationState(int paramInt)
    throws RemoteException;
  
  public abstract DebugInfo getDebugInfo()
    throws RemoteException;
  
  public abstract void getDeviceIdentity(int paramInt)
    throws RemoteException;
  
  public abstract void getFacilityLockForApp(int paramInt1, String paramString1, String paramString2, int paramInt2, String paramString3)
    throws RemoteException;
  
  public abstract void getGsmBroadcastConfig(int paramInt)
    throws RemoteException;
  
  public abstract void getHardwareConfig(int paramInt)
    throws RemoteException;
  
  public abstract ArrayList<byte[]> getHashChain()
    throws RemoteException;
  
  public abstract void getIccCardStatus(int paramInt)
    throws RemoteException;
  
  public abstract void getImsRegistrationState(int paramInt)
    throws RemoteException;
  
  public abstract void getImsiForApp(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void getLastCallFailCause(int paramInt)
    throws RemoteException;
  
  public abstract void getModemActivityInfo(int paramInt)
    throws RemoteException;
  
  public abstract void getMute(int paramInt)
    throws RemoteException;
  
  public abstract void getNeighboringCids(int paramInt)
    throws RemoteException;
  
  public abstract void getNetworkSelectionMode(int paramInt)
    throws RemoteException;
  
  public abstract void getOperator(int paramInt)
    throws RemoteException;
  
  public abstract void getPreferredNetworkType(int paramInt)
    throws RemoteException;
  
  public abstract void getPreferredVoicePrivacy(int paramInt)
    throws RemoteException;
  
  public abstract void getRadioCapability(int paramInt)
    throws RemoteException;
  
  public abstract void getSignalStrength(int paramInt)
    throws RemoteException;
  
  public abstract void getSmscAddress(int paramInt)
    throws RemoteException;
  
  public abstract void getTTYMode(int paramInt)
    throws RemoteException;
  
  public abstract void getVoiceRadioTechnology(int paramInt)
    throws RemoteException;
  
  public abstract void getVoiceRegistrationState(int paramInt)
    throws RemoteException;
  
  public abstract void handleStkCallSetupRequestFromSim(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void hangup(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void hangupForegroundResumeBackground(int paramInt)
    throws RemoteException;
  
  public abstract void hangupWaitingOrBackground(int paramInt)
    throws RemoteException;
  
  public abstract void iccCloseLogicalChannel(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void iccIOForApp(int paramInt, IccIo paramIccIo)
    throws RemoteException;
  
  public abstract void iccOpenLogicalChannel(int paramInt1, String paramString, int paramInt2)
    throws RemoteException;
  
  public abstract void iccTransmitApduBasicChannel(int paramInt, SimApdu paramSimApdu)
    throws RemoteException;
  
  public abstract void iccTransmitApduLogicalChannel(int paramInt, SimApdu paramSimApdu)
    throws RemoteException;
  
  public abstract ArrayList<String> interfaceChain()
    throws RemoteException;
  
  public abstract String interfaceDescriptor()
    throws RemoteException;
  
  public abstract boolean linkToDeath(IHwBinder.DeathRecipient paramDeathRecipient, long paramLong)
    throws RemoteException;
  
  public abstract void notifySyspropsChanged()
    throws RemoteException;
  
  public abstract void nvReadItem(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void nvResetConfig(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void nvWriteCdmaPrl(int paramInt, ArrayList<Byte> paramArrayList)
    throws RemoteException;
  
  public abstract void nvWriteItem(int paramInt, NvWriteItem paramNvWriteItem)
    throws RemoteException;
  
  public abstract void ping()
    throws RemoteException;
  
  public abstract void pullLceData(int paramInt)
    throws RemoteException;
  
  public abstract void rejectCall(int paramInt)
    throws RemoteException;
  
  public abstract void reportSmsMemoryStatus(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void reportStkServiceIsRunning(int paramInt)
    throws RemoteException;
  
  public abstract void requestIccSimAuthentication(int paramInt1, int paramInt2, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void requestIsimAuthentication(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void requestShutdown(int paramInt)
    throws RemoteException;
  
  public abstract void responseAcknowledgement()
    throws RemoteException;
  
  public abstract void sendBurstDtmf(int paramInt1, String paramString, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void sendCDMAFeatureCode(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void sendCdmaSms(int paramInt, CdmaSmsMessage paramCdmaSmsMessage)
    throws RemoteException;
  
  public abstract void sendDeviceState(int paramInt1, int paramInt2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void sendDtmf(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void sendEnvelope(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void sendEnvelopeWithStatus(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void sendImsSms(int paramInt, ImsSmsMessage paramImsSmsMessage)
    throws RemoteException;
  
  public abstract void sendSMSExpectMore(int paramInt, GsmSmsMessage paramGsmSmsMessage)
    throws RemoteException;
  
  public abstract void sendSms(int paramInt, GsmSmsMessage paramGsmSmsMessage)
    throws RemoteException;
  
  public abstract void sendTerminalResponseToSim(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void sendUssd(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void separateConnection(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setAllowedCarriers(int paramInt, boolean paramBoolean, CarrierRestrictions paramCarrierRestrictions)
    throws RemoteException;
  
  public abstract void setBandMode(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setBarringPassword(int paramInt, String paramString1, String paramString2, String paramString3)
    throws RemoteException;
  
  public abstract void setCallForward(int paramInt, CallForwardInfo paramCallForwardInfo)
    throws RemoteException;
  
  public abstract void setCallWaiting(int paramInt1, boolean paramBoolean, int paramInt2)
    throws RemoteException;
  
  public abstract void setCdmaBroadcastActivation(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setCdmaBroadcastConfig(int paramInt, ArrayList<CdmaBroadcastSmsConfigInfo> paramArrayList)
    throws RemoteException;
  
  public abstract void setCdmaRoamingPreference(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setCdmaSubscriptionSource(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setCellInfoListRate(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setClir(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setDataAllowed(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setDataProfile(int paramInt, ArrayList<DataProfileInfo> paramArrayList, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setFacilityLockForApp(int paramInt1, String paramString1, boolean paramBoolean, String paramString2, int paramInt2, String paramString3)
    throws RemoteException;
  
  public abstract void setGsmBroadcastActivation(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setGsmBroadcastConfig(int paramInt, ArrayList<GsmBroadcastSmsConfigInfo> paramArrayList)
    throws RemoteException;
  
  public abstract void setHALInstrumentation()
    throws RemoteException;
  
  public abstract void setIndicationFilter(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setInitialAttachApn(int paramInt, DataProfileInfo paramDataProfileInfo, boolean paramBoolean1, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract void setLocationUpdates(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setMute(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setNetworkSelectionModeAutomatic(int paramInt)
    throws RemoteException;
  
  public abstract void setNetworkSelectionModeManual(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void setPreferredNetworkType(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setPreferredVoicePrivacy(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setRadioCapability(int paramInt, RadioCapability paramRadioCapability)
    throws RemoteException;
  
  public abstract void setRadioPower(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setResponseFunctions(IRadioResponse paramIRadioResponse, IRadioIndication paramIRadioIndication)
    throws RemoteException;
  
  public abstract void setSimCardPower(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setSmscAddress(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void setSuppServiceNotifications(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setTTYMode(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setUiccSubscription(int paramInt, SelectUiccSub paramSelectUiccSub)
    throws RemoteException;
  
  public abstract void setupDataCall(int paramInt1, int paramInt2, DataProfileInfo paramDataProfileInfo, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
    throws RemoteException;
  
  public abstract void startDtmf(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void startLceService(int paramInt1, int paramInt2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void stopDtmf(int paramInt)
    throws RemoteException;
  
  public abstract void stopLceService(int paramInt)
    throws RemoteException;
  
  public abstract void supplyIccPin2ForApp(int paramInt, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void supplyIccPinForApp(int paramInt, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void supplyIccPuk2ForApp(int paramInt, String paramString1, String paramString2, String paramString3)
    throws RemoteException;
  
  public abstract void supplyIccPukForApp(int paramInt, String paramString1, String paramString2, String paramString3)
    throws RemoteException;
  
  public abstract void supplyNetworkDepersonalization(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void switchWaitingOrHoldingAndActive(int paramInt)
    throws RemoteException;
  
  public abstract boolean unlinkToDeath(IHwBinder.DeathRecipient paramDeathRecipient)
    throws RemoteException;
  
  public abstract void writeSmsToRuim(int paramInt, CdmaSmsWriteArgs paramCdmaSmsWriteArgs)
    throws RemoteException;
  
  public abstract void writeSmsToSim(int paramInt, SmsWriteArgs paramSmsWriteArgs)
    throws RemoteException;
  
  public static final class Proxy
    implements IRadio
  {
    private IHwBinder mRemote;
    
    public Proxy(IHwBinder paramIHwBinder)
    {
      mRemote = ((IHwBinder)Objects.requireNonNull(paramIHwBinder));
    }
    
    public void acceptCall(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(39, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void acknowledgeIncomingGsmSmsWithPdu(int paramInt, boolean paramBoolean, String paramString)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeBool(paramBoolean);
      localHwParcel.writeString(paramString);
      paramString = new HwParcel();
      try
      {
        mRemote.transact(97, localHwParcel, paramString, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString.release();
      }
    }
    
    public void acknowledgeLastIncomingCdmaSms(int paramInt, CdmaSmsAck paramCdmaSmsAck)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      paramCdmaSmsAck.writeToParcel(localHwParcel);
      paramCdmaSmsAck = new HwParcel();
      try
      {
        mRemote.transact(79, localHwParcel, paramCdmaSmsAck, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramCdmaSmsAck.release();
      }
    }
    
    public void acknowledgeLastIncomingGsmSms(int paramInt1, boolean paramBoolean, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeBool(paramBoolean);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(38, localHwParcel1, localHwParcel2, 1);
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
    
    public void cancelPendingUssd(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(31, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void changeIccPin2ForApp(int paramInt, String paramString1, String paramString2, String paramString3)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeString(paramString1);
      localHwParcel.writeString(paramString2);
      localHwParcel.writeString(paramString3);
      paramString1 = new HwParcel();
      try
      {
        mRemote.transact(8, localHwParcel, paramString1, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString1.release();
      }
    }
    
    public void changeIccPinForApp(int paramInt, String paramString1, String paramString2, String paramString3)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeString(paramString1);
      localHwParcel.writeString(paramString2);
      localHwParcel.writeString(paramString3);
      paramString1 = new HwParcel();
      try
      {
        mRemote.transact(7, localHwParcel, paramString1, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString1.release();
      }
    }
    
    public void conference(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(17, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void deactivateDataCall(int paramInt1, int paramInt2, boolean paramBoolean)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      localHwParcel1.writeBool(paramBoolean);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(40, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void deleteSmsOnRuim(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(88, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void deleteSmsOnSim(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(58, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void dial(int paramInt, Dial paramDial)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      paramDial.writeToParcel(localHwParcel);
      paramDial = new HwParcel();
      try
      {
        mRemote.transact(11, localHwParcel, paramDial, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramDial.release();
      }
    }
    
    public final boolean equals(Object paramObject)
    {
      return HidlSupport.interfacesEqual(this, paramObject);
    }
    
    public void exitEmergencyCallbackMode(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(90, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void explicitCallTransfer(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(64, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getAllowedCarriers(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(126, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getAvailableBandModes(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(60, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getAvailableNetworks(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(47, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getBasebandVersion(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(50, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getCDMASubscription(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(86, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getCallForwardStatus(int paramInt, CallForwardInfo paramCallForwardInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      paramCallForwardInfo.writeToParcel(localHwParcel);
      paramCallForwardInfo = new HwParcel();
      try
      {
        mRemote.transact(34, localHwParcel, paramCallForwardInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramCallForwardInfo.release();
      }
    }
    
    public void getCallWaiting(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(36, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getCdmaBroadcastConfig(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(83, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getCdmaRoamingPreference(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(71, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getCdmaSubscriptionSource(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(95, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getCellInfoList(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(100, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getClip(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(54, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getClir(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(32, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getCurrentCalls(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(10, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getDataCallList(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(55, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getDataRegistrationState(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(22, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
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
    
    public void getDeviceIdentity(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(89, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getFacilityLockForApp(int paramInt1, String paramString1, String paramString2, int paramInt2, String paramString3)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt1);
      localHwParcel.writeString(paramString1);
      localHwParcel.writeString(paramString2);
      localHwParcel.writeInt32(paramInt2);
      localHwParcel.writeString(paramString3);
      paramString1 = new HwParcel();
      try
      {
        mRemote.transact(41, localHwParcel, paramString1, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString1.release();
      }
    }
    
    public void getGsmBroadcastConfig(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(80, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getHardwareConfig(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(115, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
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
        Object localObject2 = mRemote;
        int i = 0;
        ((IHwBinder)localObject2).transact(256398152, (HwParcel)localObject1, localHwParcel, 0);
        localHwParcel.verifySuccess();
        ((HwParcel)localObject1).releaseTemporaryStorage();
        localObject2 = new java/util/ArrayList;
        ((ArrayList)localObject2).<init>();
        localObject1 = localHwParcel.readBuffer(16L);
        int j = ((HwBlob)localObject1).getInt32(8L);
        localObject1 = localHwParcel.readEmbeddedBuffer(j * 32, ((HwBlob)localObject1).handle(), 0L, true);
        ((ArrayList)localObject2).clear();
        while (i < j)
        {
          byte[] arrayOfByte = new byte[32];
          ((HwBlob)localObject1).copyToInt8Array(i * 32, arrayOfByte, 32);
          ((ArrayList)localObject2).add(arrayOfByte);
          i++;
        }
        return localObject2;
      }
      finally
      {
        localHwParcel.release();
      }
    }
    
    public void getIccCardStatus(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(2, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getImsRegistrationState(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(103, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getImsiForApp(int paramInt, String paramString)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeString(paramString);
      paramString = new HwParcel();
      try
      {
        mRemote.transact(12, localHwParcel, paramString, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString.release();
      }
    }
    
    public void getLastCallFailCause(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(19, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getModemActivityInfo(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(124, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getMute(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(53, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getNeighboringCids(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(67, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getNetworkSelectionMode(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(44, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getOperator(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(23, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getPreferredNetworkType(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(66, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getPreferredVoicePrivacy(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(75, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getRadioCapability(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(119, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getSignalStrength(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(20, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getSmscAddress(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(91, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getTTYMode(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(73, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getVoiceRadioTechnology(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(99, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void getVoiceRegistrationState(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(21, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void handleStkCallSetupRequestFromSim(int paramInt, boolean paramBoolean)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      localHwParcel1.writeBool(paramBoolean);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(63, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void hangup(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(13, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void hangupForegroundResumeBackground(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(15, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void hangupWaitingOrBackground(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(14, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public final int hashCode()
    {
      return asBinder().hashCode();
    }
    
    public void iccCloseLogicalChannel(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(107, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void iccIOForApp(int paramInt, IccIo paramIccIo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      paramIccIo.writeToParcel(localHwParcel);
      paramIccIo = new HwParcel();
      try
      {
        mRemote.transact(29, localHwParcel, paramIccIo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramIccIo.release();
      }
    }
    
    public void iccOpenLogicalChannel(int paramInt1, String paramString, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt1);
      localHwParcel.writeString(paramString);
      localHwParcel.writeInt32(paramInt2);
      paramString = new HwParcel();
      try
      {
        mRemote.transact(106, localHwParcel, paramString, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString.release();
      }
    }
    
    public void iccTransmitApduBasicChannel(int paramInt, SimApdu paramSimApdu)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      paramSimApdu.writeToParcel(localHwParcel);
      paramSimApdu = new HwParcel();
      try
      {
        mRemote.transact(105, localHwParcel, paramSimApdu, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramSimApdu.release();
      }
    }
    
    public void iccTransmitApduLogicalChannel(int paramInt, SimApdu paramSimApdu)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      paramSimApdu.writeToParcel(localHwParcel);
      paramSimApdu = new HwParcel();
      try
      {
        mRemote.transact(108, localHwParcel, paramSimApdu, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramSimApdu.release();
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
    
    public void nvReadItem(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(109, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void nvResetConfig(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(112, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void nvWriteCdmaPrl(int paramInt, ArrayList<Byte> paramArrayList)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeInt8Vector(paramArrayList);
      paramArrayList = new HwParcel();
      try
      {
        mRemote.transact(111, localHwParcel, paramArrayList, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramArrayList.release();
      }
    }
    
    public void nvWriteItem(int paramInt, NvWriteItem paramNvWriteItem)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      paramNvWriteItem.writeToParcel(localHwParcel);
      paramNvWriteItem = new HwParcel();
      try
      {
        mRemote.transact(110, localHwParcel, paramNvWriteItem, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramNvWriteItem.release();
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
    
    public void pullLceData(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(123, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void rejectCall(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(18, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void reportSmsMemoryStatus(int paramInt, boolean paramBoolean)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      localHwParcel1.writeBool(paramBoolean);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(93, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void reportStkServiceIsRunning(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(94, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void requestIccSimAuthentication(int paramInt1, int paramInt2, String paramString1, String paramString2)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt1);
      localHwParcel.writeInt32(paramInt2);
      localHwParcel.writeString(paramString1);
      localHwParcel.writeString(paramString2);
      paramString1 = new HwParcel();
      try
      {
        mRemote.transact(116, localHwParcel, paramString1, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString1.release();
      }
    }
    
    public void requestIsimAuthentication(int paramInt, String paramString)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeString(paramString);
      paramString = new HwParcel();
      try
      {
        mRemote.transact(96, localHwParcel, paramString, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString.release();
      }
    }
    
    public void requestShutdown(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(118, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void responseAcknowledgement()
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(130, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void sendBurstDtmf(int paramInt1, String paramString, int paramInt2, int paramInt3)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt1);
      localHwParcel.writeString(paramString);
      localHwParcel.writeInt32(paramInt2);
      localHwParcel.writeInt32(paramInt3);
      paramString = new HwParcel();
      try
      {
        mRemote.transact(77, localHwParcel, paramString, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString.release();
      }
    }
    
    public void sendCDMAFeatureCode(int paramInt, String paramString)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeString(paramString);
      paramString = new HwParcel();
      try
      {
        mRemote.transact(76, localHwParcel, paramString, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString.release();
      }
    }
    
    public void sendCdmaSms(int paramInt, CdmaSmsMessage paramCdmaSmsMessage)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      paramCdmaSmsMessage.writeToParcel(localHwParcel);
      paramCdmaSmsMessage = new HwParcel();
      try
      {
        mRemote.transact(78, localHwParcel, paramCdmaSmsMessage, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramCdmaSmsMessage.release();
      }
    }
    
    public void sendDeviceState(int paramInt1, int paramInt2, boolean paramBoolean)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      localHwParcel1.writeBool(paramBoolean);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(127, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void sendDtmf(int paramInt, String paramString)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeString(paramString);
      paramString = new HwParcel();
      try
      {
        mRemote.transact(25, localHwParcel, paramString, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString.release();
      }
    }
    
    public void sendEnvelope(int paramInt, String paramString)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeString(paramString);
      paramString = new HwParcel();
      try
      {
        mRemote.transact(61, localHwParcel, paramString, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString.release();
      }
    }
    
    public void sendEnvelopeWithStatus(int paramInt, String paramString)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeString(paramString);
      paramString = new HwParcel();
      try
      {
        mRemote.transact(98, localHwParcel, paramString, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString.release();
      }
    }
    
    public void sendImsSms(int paramInt, ImsSmsMessage paramImsSmsMessage)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      paramImsSmsMessage.writeToParcel(localHwParcel);
      paramImsSmsMessage = new HwParcel();
      try
      {
        mRemote.transact(104, localHwParcel, paramImsSmsMessage, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramImsSmsMessage.release();
      }
    }
    
    public void sendSMSExpectMore(int paramInt, GsmSmsMessage paramGsmSmsMessage)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      paramGsmSmsMessage.writeToParcel(localHwParcel);
      paramGsmSmsMessage = new HwParcel();
      try
      {
        mRemote.transact(27, localHwParcel, paramGsmSmsMessage, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramGsmSmsMessage.release();
      }
    }
    
    public void sendSms(int paramInt, GsmSmsMessage paramGsmSmsMessage)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      paramGsmSmsMessage.writeToParcel(localHwParcel);
      paramGsmSmsMessage = new HwParcel();
      try
      {
        mRemote.transact(26, localHwParcel, paramGsmSmsMessage, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramGsmSmsMessage.release();
      }
    }
    
    public void sendTerminalResponseToSim(int paramInt, String paramString)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeString(paramString);
      paramString = new HwParcel();
      try
      {
        mRemote.transact(62, localHwParcel, paramString, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString.release();
      }
    }
    
    public void sendUssd(int paramInt, String paramString)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeString(paramString);
      paramString = new HwParcel();
      try
      {
        mRemote.transact(30, localHwParcel, paramString, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString.release();
      }
    }
    
    public void separateConnection(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(51, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void setAllowedCarriers(int paramInt, boolean paramBoolean, CarrierRestrictions paramCarrierRestrictions)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeBool(paramBoolean);
      paramCarrierRestrictions.writeToParcel(localHwParcel);
      paramCarrierRestrictions = new HwParcel();
      try
      {
        mRemote.transact(125, localHwParcel, paramCarrierRestrictions, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramCarrierRestrictions.release();
      }
    }
    
    public void setBandMode(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(59, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void setBarringPassword(int paramInt, String paramString1, String paramString2, String paramString3)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeString(paramString1);
      localHwParcel.writeString(paramString2);
      localHwParcel.writeString(paramString3);
      paramString1 = new HwParcel();
      try
      {
        mRemote.transact(43, localHwParcel, paramString1, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString1.release();
      }
    }
    
    public void setCallForward(int paramInt, CallForwardInfo paramCallForwardInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      paramCallForwardInfo.writeToParcel(localHwParcel);
      paramCallForwardInfo = new HwParcel();
      try
      {
        mRemote.transact(35, localHwParcel, paramCallForwardInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramCallForwardInfo.release();
      }
    }
    
    public void setCallWaiting(int paramInt1, boolean paramBoolean, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeBool(paramBoolean);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(37, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void setCdmaBroadcastActivation(int paramInt, boolean paramBoolean)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      localHwParcel1.writeBool(paramBoolean);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(85, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void setCdmaBroadcastConfig(int paramInt, ArrayList<CdmaBroadcastSmsConfigInfo> paramArrayList)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      CdmaBroadcastSmsConfigInfo.writeVectorToParcel(localHwParcel, paramArrayList);
      paramArrayList = new HwParcel();
      try
      {
        mRemote.transact(84, localHwParcel, paramArrayList, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramArrayList.release();
      }
    }
    
    public void setCdmaRoamingPreference(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(70, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void setCdmaSubscriptionSource(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(69, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void setCellInfoListRate(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(101, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void setClir(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(33, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void setDataAllowed(int paramInt, boolean paramBoolean)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      localHwParcel1.writeBool(paramBoolean);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(114, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void setDataProfile(int paramInt, ArrayList<DataProfileInfo> paramArrayList, boolean paramBoolean)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      DataProfileInfo.writeVectorToParcel(localHwParcel, paramArrayList);
      localHwParcel.writeBool(paramBoolean);
      paramArrayList = new HwParcel();
      try
      {
        mRemote.transact(117, localHwParcel, paramArrayList, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramArrayList.release();
      }
    }
    
    public void setFacilityLockForApp(int paramInt1, String paramString1, boolean paramBoolean, String paramString2, int paramInt2, String paramString3)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt1);
      localHwParcel.writeString(paramString1);
      localHwParcel.writeBool(paramBoolean);
      localHwParcel.writeString(paramString2);
      localHwParcel.writeInt32(paramInt2);
      localHwParcel.writeString(paramString3);
      paramString1 = new HwParcel();
      try
      {
        mRemote.transact(42, localHwParcel, paramString1, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString1.release();
      }
    }
    
    public void setGsmBroadcastActivation(int paramInt, boolean paramBoolean)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      localHwParcel1.writeBool(paramBoolean);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(82, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void setGsmBroadcastConfig(int paramInt, ArrayList<GsmBroadcastSmsConfigInfo> paramArrayList)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      GsmBroadcastSmsConfigInfo.writeVectorToParcel(localHwParcel, paramArrayList);
      paramArrayList = new HwParcel();
      try
      {
        mRemote.transact(81, localHwParcel, paramArrayList, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramArrayList.release();
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
    
    public void setIndicationFilter(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(128, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void setInitialAttachApn(int paramInt, DataProfileInfo paramDataProfileInfo, boolean paramBoolean1, boolean paramBoolean2)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      paramDataProfileInfo.writeToParcel(localHwParcel);
      localHwParcel.writeBool(paramBoolean1);
      localHwParcel.writeBool(paramBoolean2);
      paramDataProfileInfo = new HwParcel();
      try
      {
        mRemote.transact(102, localHwParcel, paramDataProfileInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramDataProfileInfo.release();
      }
    }
    
    public void setLocationUpdates(int paramInt, boolean paramBoolean)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      localHwParcel1.writeBool(paramBoolean);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(68, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void setMute(int paramInt, boolean paramBoolean)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      localHwParcel1.writeBool(paramBoolean);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(52, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void setNetworkSelectionModeAutomatic(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(45, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void setNetworkSelectionModeManual(int paramInt, String paramString)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeString(paramString);
      paramString = new HwParcel();
      try
      {
        mRemote.transact(46, localHwParcel, paramString, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString.release();
      }
    }
    
    public void setPreferredNetworkType(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(65, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void setPreferredVoicePrivacy(int paramInt, boolean paramBoolean)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      localHwParcel1.writeBool(paramBoolean);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(74, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void setRadioCapability(int paramInt, RadioCapability paramRadioCapability)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      paramRadioCapability.writeToParcel(localHwParcel);
      paramRadioCapability = new HwParcel();
      try
      {
        mRemote.transact(120, localHwParcel, paramRadioCapability, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioCapability.release();
      }
    }
    
    public void setRadioPower(int paramInt, boolean paramBoolean)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      localHwParcel1.writeBool(paramBoolean);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(24, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void setResponseFunctions(IRadioResponse paramIRadioResponse, IRadioIndication paramIRadioIndication)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      Object localObject = null;
      if (paramIRadioResponse == null) {
        paramIRadioResponse = null;
      } else {
        paramIRadioResponse = paramIRadioResponse.asBinder();
      }
      localHwParcel.writeStrongBinder(paramIRadioResponse);
      if (paramIRadioIndication == null) {
        paramIRadioResponse = localObject;
      } else {
        paramIRadioResponse = paramIRadioIndication.asBinder();
      }
      localHwParcel.writeStrongBinder(paramIRadioResponse);
      paramIRadioResponse = new HwParcel();
      try
      {
        mRemote.transact(1, localHwParcel, paramIRadioResponse, 0);
        paramIRadioResponse.verifySuccess();
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramIRadioResponse.release();
      }
    }
    
    public void setSimCardPower(int paramInt, boolean paramBoolean)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      localHwParcel1.writeBool(paramBoolean);
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
    
    public void setSmscAddress(int paramInt, String paramString)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeString(paramString);
      paramString = new HwParcel();
      try
      {
        mRemote.transact(92, localHwParcel, paramString, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString.release();
      }
    }
    
    public void setSuppServiceNotifications(int paramInt, boolean paramBoolean)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      localHwParcel1.writeBool(paramBoolean);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(56, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void setTTYMode(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(72, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void setUiccSubscription(int paramInt, SelectUiccSub paramSelectUiccSub)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      paramSelectUiccSub.writeToParcel(localHwParcel);
      paramSelectUiccSub = new HwParcel();
      try
      {
        mRemote.transact(113, localHwParcel, paramSelectUiccSub, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramSelectUiccSub.release();
      }
    }
    
    public void setupDataCall(int paramInt1, int paramInt2, DataProfileInfo paramDataProfileInfo, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt1);
      localHwParcel.writeInt32(paramInt2);
      paramDataProfileInfo.writeToParcel(localHwParcel);
      localHwParcel.writeBool(paramBoolean1);
      localHwParcel.writeBool(paramBoolean2);
      localHwParcel.writeBool(paramBoolean3);
      paramDataProfileInfo = new HwParcel();
      try
      {
        mRemote.transact(28, localHwParcel, paramDataProfileInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramDataProfileInfo.release();
      }
    }
    
    public void startDtmf(int paramInt, String paramString)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeString(paramString);
      paramString = new HwParcel();
      try
      {
        mRemote.transact(48, localHwParcel, paramString, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString.release();
      }
    }
    
    public void startLceService(int paramInt1, int paramInt2, boolean paramBoolean)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      localHwParcel1.writeBool(paramBoolean);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(121, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void stopDtmf(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(49, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void stopLceService(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(122, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void supplyIccPin2ForApp(int paramInt, String paramString1, String paramString2)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeString(paramString1);
      localHwParcel.writeString(paramString2);
      paramString1 = new HwParcel();
      try
      {
        mRemote.transact(5, localHwParcel, paramString1, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString1.release();
      }
    }
    
    public void supplyIccPinForApp(int paramInt, String paramString1, String paramString2)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeString(paramString1);
      localHwParcel.writeString(paramString2);
      paramString1 = new HwParcel();
      try
      {
        mRemote.transact(3, localHwParcel, paramString1, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString1.release();
      }
    }
    
    public void supplyIccPuk2ForApp(int paramInt, String paramString1, String paramString2, String paramString3)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeString(paramString1);
      localHwParcel.writeString(paramString2);
      localHwParcel.writeString(paramString3);
      paramString1 = new HwParcel();
      try
      {
        mRemote.transact(6, localHwParcel, paramString1, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString1.release();
      }
    }
    
    public void supplyIccPukForApp(int paramInt, String paramString1, String paramString2, String paramString3)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeString(paramString1);
      localHwParcel.writeString(paramString2);
      localHwParcel.writeString(paramString3);
      paramString1 = new HwParcel();
      try
      {
        mRemote.transact(4, localHwParcel, paramString1, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString1.release();
      }
    }
    
    public void supplyNetworkDepersonalization(int paramInt, String paramString)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeString(paramString);
      paramString = new HwParcel();
      try
      {
        mRemote.transact(9, localHwParcel, paramString, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString.release();
      }
    }
    
    public void switchWaitingOrHoldingAndActive(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(16, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
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
      return "[class or subclass of android.hardware.radio@1.0::IRadio]@Proxy";
    }
    
    public boolean unlinkToDeath(IHwBinder.DeathRecipient paramDeathRecipient)
      throws RemoteException
    {
      return mRemote.unlinkToDeath(paramDeathRecipient);
    }
    
    public void writeSmsToRuim(int paramInt, CdmaSmsWriteArgs paramCdmaSmsWriteArgs)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      paramCdmaSmsWriteArgs.writeToParcel(localHwParcel);
      paramCdmaSmsWriteArgs = new HwParcel();
      try
      {
        mRemote.transact(87, localHwParcel, paramCdmaSmsWriteArgs, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramCdmaSmsWriteArgs.release();
      }
    }
    
    public void writeSmsToSim(int paramInt, SmsWriteArgs paramSmsWriteArgs)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadio");
      localHwParcel.writeInt32(paramInt);
      paramSmsWriteArgs.writeToParcel(localHwParcel);
      paramSmsWriteArgs = new HwParcel();
      try
      {
        mRemote.transact(57, localHwParcel, paramSmsWriteArgs, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramSmsWriteArgs.release();
      }
    }
  }
  
  public static abstract class Stub
    extends HwBinder
    implements IRadio
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
      byte[] arrayOfByte = { -67, -38, -74, 24, 77, 122, 52, 109, -90, -96, 125, -64, -126, -116, -15, -102, 105, 111, 76, -86, 54, 17, -59, 31, 46, 20, 86, 90, 20, -76, 15, -39 };
      return new ArrayList(Arrays.asList(new byte[][] { { -101, 90, -92, -103, -20, 59, 66, 38, -15, 95, 72, -11, -19, 8, -119, 110, 47, -64, 103, 111, -105, -116, -98, 25, -100, 29, -94, 29, -86, -16, 2, -90 }, arrayOfByte }));
    }
    
    public final ArrayList<String> interfaceChain()
    {
      return new ArrayList(Arrays.asList(new String[] { "android.hardware.radio@1.0::IRadio", "android.hidl.base@1.0::IBase" }));
    }
    
    public final String interfaceDescriptor()
    {
      return "android.hardware.radio@1.0::IRadio";
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
      int i136 = 1;
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
            paramInt1 = i136;
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
            HwBlob localHwBlob1 = new HwBlob(16);
            paramInt2 = paramHwParcel1.size();
            localHwBlob1.putInt32(8L, paramInt2);
            localHwBlob1.putBool(12L, false);
            HwBlob localHwBlob2 = new HwBlob(paramInt2 * 32);
            for (paramInt1 = m; paramInt1 < paramInt2; paramInt1++) {
              localHwBlob2.putInt8Array(paramInt1 * 32, (byte[])paramHwParcel1.get(paramInt1));
            }
            localHwBlob1.putBlob(0L, localHwBlob2);
            paramHwParcel2.writeBuffer(localHwBlob1);
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
      case 130: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          responseAcknowledgement();
        }
        break;
      case 129: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          setSimCardPower(paramHwParcel1.readInt32(), paramHwParcel1.readBool());
        }
        break;
      case 128: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          setIndicationFilter(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 127: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          sendDeviceState(paramHwParcel1.readInt32(), paramHwParcel1.readInt32(), paramHwParcel1.readBool());
        }
        break;
      case 126: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getAllowedCarriers(paramHwParcel1.readInt32());
        }
        break;
      case 125: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          paramInt1 = paramHwParcel1.readInt32();
          boolean bool = paramHwParcel1.readBool();
          paramHwParcel2 = new CarrierRestrictions();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setAllowedCarriers(paramInt1, bool, paramHwParcel2);
        }
        break;
      case 124: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getModemActivityInfo(paramHwParcel1.readInt32());
        }
        break;
      case 123: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          pullLceData(paramHwParcel1.readInt32());
        }
        break;
      case 122: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          stopLceService(paramHwParcel1.readInt32());
        }
        break;
      case 121: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          startLceService(paramHwParcel1.readInt32(), paramHwParcel1.readInt32(), paramHwParcel1.readBool());
        }
        break;
      case 120: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new RadioCapability();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setRadioCapability(paramInt1, paramHwParcel2);
        }
        break;
      case 119: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getRadioCapability(paramHwParcel1.readInt32());
        }
        break;
      case 118: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          requestShutdown(paramHwParcel1.readInt32());
        }
        break;
      case 117: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          setDataProfile(paramHwParcel1.readInt32(), DataProfileInfo.readVectorFromParcel(paramHwParcel1), paramHwParcel1.readBool());
        }
        break;
      case 116: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          requestIccSimAuthentication(paramHwParcel1.readInt32(), paramHwParcel1.readInt32(), paramHwParcel1.readString(), paramHwParcel1.readString());
        }
        break;
      case 115: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getHardwareConfig(paramHwParcel1.readInt32());
        }
        break;
      case 114: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          setDataAllowed(paramHwParcel1.readInt32(), paramHwParcel1.readBool());
        }
        break;
      case 113: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new SelectUiccSub();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setUiccSubscription(paramInt1, paramHwParcel2);
        }
        break;
      case 112: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          nvResetConfig(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 111: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          nvWriteCdmaPrl(paramHwParcel1.readInt32(), paramHwParcel1.readInt8Vector());
        }
        break;
      case 110: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new NvWriteItem();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          nvWriteItem(paramInt1, paramHwParcel2);
        }
        break;
      case 109: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          nvReadItem(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 108: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new SimApdu();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          iccTransmitApduLogicalChannel(paramInt1, paramHwParcel2);
        }
        break;
      case 107: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          iccCloseLogicalChannel(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 106: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          iccOpenLogicalChannel(paramHwParcel1.readInt32(), paramHwParcel1.readString(), paramHwParcel1.readInt32());
        }
        break;
      case 105: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new SimApdu();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          iccTransmitApduBasicChannel(paramInt1, paramHwParcel2);
        }
        break;
      case 104: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new ImsSmsMessage();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          sendImsSms(paramInt1, paramHwParcel2);
        }
        break;
      case 103: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getImsRegistrationState(paramHwParcel1.readInt32());
        }
        break;
      case 102: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new DataProfileInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setInitialAttachApn(paramInt1, paramHwParcel2, paramHwParcel1.readBool(), paramHwParcel1.readBool());
        }
        break;
      case 101: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          setCellInfoListRate(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 100: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getCellInfoList(paramHwParcel1.readInt32());
        }
        break;
      case 99: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getVoiceRadioTechnology(paramHwParcel1.readInt32());
        }
        break;
      case 98: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          sendEnvelopeWithStatus(paramHwParcel1.readInt32(), paramHwParcel1.readString());
        }
        break;
      case 97: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          acknowledgeIncomingGsmSmsWithPdu(paramHwParcel1.readInt32(), paramHwParcel1.readBool(), paramHwParcel1.readString());
        }
        break;
      case 96: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          requestIsimAuthentication(paramHwParcel1.readInt32(), paramHwParcel1.readString());
        }
        break;
      case 95: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getCdmaSubscriptionSource(paramHwParcel1.readInt32());
        }
        break;
      case 94: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          reportStkServiceIsRunning(paramHwParcel1.readInt32());
        }
        break;
      case 93: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          reportSmsMemoryStatus(paramHwParcel1.readInt32(), paramHwParcel1.readBool());
        }
        break;
      case 92: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          setSmscAddress(paramHwParcel1.readInt32(), paramHwParcel1.readString());
        }
        break;
      case 91: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getSmscAddress(paramHwParcel1.readInt32());
        }
        break;
      case 90: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          exitEmergencyCallbackMode(paramHwParcel1.readInt32());
        }
        break;
      case 89: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getDeviceIdentity(paramHwParcel1.readInt32());
        }
        break;
      case 88: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          deleteSmsOnRuim(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 87: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new CdmaSmsWriteArgs();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          writeSmsToRuim(paramInt1, paramHwParcel2);
        }
        break;
      case 86: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getCDMASubscription(paramHwParcel1.readInt32());
        }
        break;
      case 85: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          setCdmaBroadcastActivation(paramHwParcel1.readInt32(), paramHwParcel1.readBool());
        }
        break;
      case 84: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          setCdmaBroadcastConfig(paramHwParcel1.readInt32(), CdmaBroadcastSmsConfigInfo.readVectorFromParcel(paramHwParcel1));
        }
        break;
      case 83: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getCdmaBroadcastConfig(paramHwParcel1.readInt32());
        }
        break;
      case 82: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          setGsmBroadcastActivation(paramHwParcel1.readInt32(), paramHwParcel1.readBool());
        }
        break;
      case 81: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          setGsmBroadcastConfig(paramHwParcel1.readInt32(), GsmBroadcastSmsConfigInfo.readVectorFromParcel(paramHwParcel1));
        }
        break;
      case 80: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getGsmBroadcastConfig(paramHwParcel1.readInt32());
        }
        break;
      case 79: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new CdmaSmsAck();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          acknowledgeLastIncomingCdmaSms(paramInt1, paramHwParcel2);
        }
        break;
      case 78: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new CdmaSmsMessage();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          sendCdmaSms(paramInt1, paramHwParcel2);
        }
        break;
      case 77: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          sendBurstDtmf(paramHwParcel1.readInt32(), paramHwParcel1.readString(), paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 76: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          sendCDMAFeatureCode(paramHwParcel1.readInt32(), paramHwParcel1.readString());
        }
        break;
      case 75: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getPreferredVoicePrivacy(paramHwParcel1.readInt32());
        }
        break;
      case 74: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          setPreferredVoicePrivacy(paramHwParcel1.readInt32(), paramHwParcel1.readBool());
        }
        break;
      case 73: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getTTYMode(paramHwParcel1.readInt32());
        }
        break;
      case 72: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          setTTYMode(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 71: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getCdmaRoamingPreference(paramHwParcel1.readInt32());
        }
        break;
      case 70: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          setCdmaRoamingPreference(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 69: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          setCdmaSubscriptionSource(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 68: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          setLocationUpdates(paramHwParcel1.readInt32(), paramHwParcel1.readBool());
        }
        break;
      case 67: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getNeighboringCids(paramHwParcel1.readInt32());
        }
        break;
      case 66: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getPreferredNetworkType(paramHwParcel1.readInt32());
        }
        break;
      case 65: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          setPreferredNetworkType(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 64: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          explicitCallTransfer(paramHwParcel1.readInt32());
        }
        break;
      case 63: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          handleStkCallSetupRequestFromSim(paramHwParcel1.readInt32(), paramHwParcel1.readBool());
        }
        break;
      case 62: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          sendTerminalResponseToSim(paramHwParcel1.readInt32(), paramHwParcel1.readString());
        }
        break;
      case 61: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          sendEnvelope(paramHwParcel1.readInt32(), paramHwParcel1.readString());
        }
        break;
      case 60: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getAvailableBandModes(paramHwParcel1.readInt32());
        }
        break;
      case 59: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          setBandMode(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 58: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          deleteSmsOnSim(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 57: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new SmsWriteArgs();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          writeSmsToSim(paramInt1, paramHwParcel2);
        }
        break;
      case 56: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          setSuppServiceNotifications(paramHwParcel1.readInt32(), paramHwParcel1.readBool());
        }
        break;
      case 55: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getDataCallList(paramHwParcel1.readInt32());
        }
        break;
      case 54: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getClip(paramHwParcel1.readInt32());
        }
        break;
      case 53: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getMute(paramHwParcel1.readInt32());
        }
        break;
      case 52: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          setMute(paramHwParcel1.readInt32(), paramHwParcel1.readBool());
        }
        break;
      case 51: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          separateConnection(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 50: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getBasebandVersion(paramHwParcel1.readInt32());
        }
        break;
      case 49: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          stopDtmf(paramHwParcel1.readInt32());
        }
        break;
      case 48: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          startDtmf(paramHwParcel1.readInt32(), paramHwParcel1.readString());
        }
        break;
      case 47: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getAvailableNetworks(paramHwParcel1.readInt32());
        }
        break;
      case 46: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          setNetworkSelectionModeManual(paramHwParcel1.readInt32(), paramHwParcel1.readString());
        }
        break;
      case 45: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          setNetworkSelectionModeAutomatic(paramHwParcel1.readInt32());
        }
        break;
      case 44: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getNetworkSelectionMode(paramHwParcel1.readInt32());
        }
        break;
      case 43: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          setBarringPassword(paramHwParcel1.readInt32(), paramHwParcel1.readString(), paramHwParcel1.readString(), paramHwParcel1.readString());
        }
        break;
      case 42: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          setFacilityLockForApp(paramHwParcel1.readInt32(), paramHwParcel1.readString(), paramHwParcel1.readBool(), paramHwParcel1.readString(), paramHwParcel1.readInt32(), paramHwParcel1.readString());
        }
        break;
      case 41: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getFacilityLockForApp(paramHwParcel1.readInt32(), paramHwParcel1.readString(), paramHwParcel1.readString(), paramHwParcel1.readInt32(), paramHwParcel1.readString());
        }
        break;
      case 40: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          deactivateDataCall(paramHwParcel1.readInt32(), paramHwParcel1.readInt32(), paramHwParcel1.readBool());
        }
        break;
      case 39: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          acceptCall(paramHwParcel1.readInt32());
        }
        break;
      case 38: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          acknowledgeLastIncomingGsmSms(paramHwParcel1.readInt32(), paramHwParcel1.readBool(), paramHwParcel1.readInt32());
        }
        break;
      case 37: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          setCallWaiting(paramHwParcel1.readInt32(), paramHwParcel1.readBool(), paramHwParcel1.readInt32());
        }
        break;
      case 36: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getCallWaiting(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 35: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new CallForwardInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setCallForward(paramInt1, paramHwParcel2);
        }
        break;
      case 34: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new CallForwardInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          getCallForwardStatus(paramInt1, paramHwParcel2);
        }
        break;
      case 33: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          setClir(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 32: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getClir(paramHwParcel1.readInt32());
        }
        break;
      case 31: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          cancelPendingUssd(paramHwParcel1.readInt32());
        }
        break;
      case 30: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          sendUssd(paramHwParcel1.readInt32(), paramHwParcel1.readString());
        }
        break;
      case 29: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new IccIo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          iccIOForApp(paramInt1, paramHwParcel2);
        }
        break;
      case 28: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          paramInt2 = paramHwParcel1.readInt32();
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new DataProfileInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          setupDataCall(paramInt2, paramInt1, paramHwParcel2, paramHwParcel1.readBool(), paramHwParcel1.readBool(), paramHwParcel1.readBool());
        }
        break;
      case 27: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new GsmSmsMessage();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          sendSMSExpectMore(paramInt1, paramHwParcel2);
        }
        break;
      case 26: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new GsmSmsMessage();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          sendSms(paramInt1, paramHwParcel2);
        }
        break;
      case 25: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          sendDtmf(paramHwParcel1.readInt32(), paramHwParcel1.readString());
        }
        break;
      case 24: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          setRadioPower(paramHwParcel1.readInt32(), paramHwParcel1.readBool());
        }
        break;
      case 23: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getOperator(paramHwParcel1.readInt32());
        }
        break;
      case 22: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getDataRegistrationState(paramHwParcel1.readInt32());
        }
        break;
      case 21: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getVoiceRegistrationState(paramHwParcel1.readInt32());
        }
        break;
      case 20: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getSignalStrength(paramHwParcel1.readInt32());
        }
        break;
      case 19: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getLastCallFailCause(paramHwParcel1.readInt32());
        }
        break;
      case 18: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          rejectCall(paramHwParcel1.readInt32());
        }
        break;
      case 17: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          conference(paramHwParcel1.readInt32());
        }
        break;
      case 16: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          switchWaitingOrHoldingAndActive(paramHwParcel1.readInt32());
        }
        break;
      case 15: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          hangupForegroundResumeBackground(paramHwParcel1.readInt32());
        }
        break;
      case 14: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          hangupWaitingOrBackground(paramHwParcel1.readInt32());
        }
        break;
      case 13: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          hangup(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 12: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getImsiForApp(paramHwParcel1.readInt32(), paramHwParcel1.readString());
        }
        break;
      case 11: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new Dial();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          dial(paramInt1, paramHwParcel2);
        }
        break;
      case 10: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getCurrentCalls(paramHwParcel1.readInt32());
        }
        break;
      case 9: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          supplyNetworkDepersonalization(paramHwParcel1.readInt32(), paramHwParcel1.readString());
        }
        break;
      case 8: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          changeIccPin2ForApp(paramHwParcel1.readInt32(), paramHwParcel1.readString(), paramHwParcel1.readString(), paramHwParcel1.readString());
        }
        break;
      case 7: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          changeIccPinForApp(paramHwParcel1.readInt32(), paramHwParcel1.readString(), paramHwParcel1.readString(), paramHwParcel1.readString());
        }
        break;
      case 6: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          supplyIccPuk2ForApp(paramHwParcel1.readInt32(), paramHwParcel1.readString(), paramHwParcel1.readString(), paramHwParcel1.readString());
        }
        break;
      case 5: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          supplyIccPin2ForApp(paramHwParcel1.readInt32(), paramHwParcel1.readString(), paramHwParcel1.readString());
        }
        break;
      case 4: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          supplyIccPukForApp(paramHwParcel1.readInt32(), paramHwParcel1.readString(), paramHwParcel1.readString(), paramHwParcel1.readString());
        }
        break;
      case 3: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          supplyIccPinForApp(paramHwParcel1.readInt32(), paramHwParcel1.readString(), paramHwParcel1.readString());
        }
        break;
      case 2: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          getIccCardStatus(paramHwParcel1.readInt32());
        }
        break;
      case 1: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadio");
          setResponseFunctions(IRadioResponse.asInterface(paramHwParcel1.readStrongBinder()), IRadioIndication.asInterface(paramHwParcel1.readStrongBinder()));
          paramHwParcel2.writeStatus(0);
          paramHwParcel2.send();
        }
        break;
      }
    }
    
    public final void ping() {}
    
    public IHwInterface queryLocalInterface(String paramString)
    {
      if ("android.hardware.radio@1.0::IRadio".equals(paramString)) {
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
