package android.hardware.radio.V1_1;

import android.hardware.radio.V1_0.CdmaCallWaiting;
import android.hardware.radio.V1_0.CdmaInformationRecords;
import android.hardware.radio.V1_0.CdmaSignalInfoRecord;
import android.hardware.radio.V1_0.CdmaSmsMessage;
import android.hardware.radio.V1_0.CellInfo;
import android.hardware.radio.V1_0.HardwareConfig;
import android.hardware.radio.V1_0.LceDataInfo;
import android.hardware.radio.V1_0.PcoDataInfo;
import android.hardware.radio.V1_0.RadioCapability;
import android.hardware.radio.V1_0.SetupDataCallResult;
import android.hardware.radio.V1_0.SignalStrength;
import android.hardware.radio.V1_0.SimRefreshResult;
import android.hardware.radio.V1_0.StkCcUnsolSsResult;
import android.hardware.radio.V1_0.SuppSvcNotification;
import android.hidl.base.V1_0.DebugInfo;
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

public abstract interface IRadioIndication
  extends android.hardware.radio.V1_0.IRadioIndication
{
  public static final String kInterfaceName = "android.hardware.radio@1.1::IRadioIndication";
  
  public static IRadioIndication asInterface(IHwBinder paramIHwBinder)
  {
    if (paramIHwBinder == null) {
      return null;
    }
    Object localObject = paramIHwBinder.queryLocalInterface("android.hardware.radio@1.1::IRadioIndication");
    if ((localObject != null) && ((localObject instanceof IRadioIndication))) {
      return (IRadioIndication)localObject;
    }
    localObject = new Proxy(paramIHwBinder);
    try
    {
      paramIHwBinder = ((IRadioIndication)localObject).interfaceChain().iterator();
      while (paramIHwBinder.hasNext())
      {
        boolean bool = ((String)paramIHwBinder.next()).equals("android.hardware.radio@1.1::IRadioIndication");
        if (bool) {
          return localObject;
        }
      }
    }
    catch (RemoteException paramIHwBinder) {}
    return null;
  }
  
  public static IRadioIndication castFrom(IHwInterface paramIHwInterface)
  {
    if (paramIHwInterface == null) {
      paramIHwInterface = null;
    } else {
      paramIHwInterface = asInterface(paramIHwInterface.asBinder());
    }
    return paramIHwInterface;
  }
  
  public static IRadioIndication getService()
    throws RemoteException
  {
    return getService("default");
  }
  
  public static IRadioIndication getService(String paramString)
    throws RemoteException
  {
    return asInterface(HwBinder.getService("android.hardware.radio@1.1::IRadioIndication", paramString));
  }
  
  public static IRadioIndication getService(String paramString, boolean paramBoolean)
    throws RemoteException
  {
    return asInterface(HwBinder.getService("android.hardware.radio@1.1::IRadioIndication", paramString, paramBoolean));
  }
  
  public static IRadioIndication getService(boolean paramBoolean)
    throws RemoteException
  {
    return getService("default", paramBoolean);
  }
  
  public abstract IHwBinder asBinder();
  
  public abstract void carrierInfoForImsiEncryption(int paramInt)
    throws RemoteException;
  
  public abstract DebugInfo getDebugInfo()
    throws RemoteException;
  
  public abstract ArrayList<byte[]> getHashChain()
    throws RemoteException;
  
  public abstract ArrayList<String> interfaceChain()
    throws RemoteException;
  
  public abstract String interfaceDescriptor()
    throws RemoteException;
  
  public abstract void keepaliveStatus(int paramInt, KeepaliveStatus paramKeepaliveStatus)
    throws RemoteException;
  
  public abstract boolean linkToDeath(IHwBinder.DeathRecipient paramDeathRecipient, long paramLong)
    throws RemoteException;
  
  public abstract void networkScanResult(int paramInt, NetworkScanResult paramNetworkScanResult)
    throws RemoteException;
  
  public abstract void notifySyspropsChanged()
    throws RemoteException;
  
  public abstract void ping()
    throws RemoteException;
  
  public abstract void setHALInstrumentation()
    throws RemoteException;
  
  public abstract boolean unlinkToDeath(IHwBinder.DeathRecipient paramDeathRecipient)
    throws RemoteException;
  
  public static final class Proxy
    implements IRadioIndication
  {
    private IHwBinder mRemote;
    
    public Proxy(IHwBinder paramIHwBinder)
    {
      mRemote = ((IHwBinder)Objects.requireNonNull(paramIHwBinder));
    }
    
    public IHwBinder asBinder()
    {
      return mRemote;
    }
    
    public void callRing(int paramInt, boolean paramBoolean, CdmaSignalInfoRecord paramCdmaSignalInfoRecord)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeBool(paramBoolean);
      paramCdmaSignalInfoRecord.writeToParcel(localHwParcel);
      paramCdmaSignalInfoRecord = new HwParcel();
      try
      {
        mRemote.transact(18, localHwParcel, paramCdmaSignalInfoRecord, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramCdmaSignalInfoRecord.release();
      }
    }
    
    public void callStateChanged(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
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
    
    public void carrierInfoForImsiEncryption(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.1::IRadioIndication");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(46, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void cdmaCallWaiting(int paramInt, CdmaCallWaiting paramCdmaCallWaiting)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel.writeInt32(paramInt);
      paramCdmaCallWaiting.writeToParcel(localHwParcel);
      paramCdmaCallWaiting = new HwParcel();
      try
      {
        mRemote.transact(25, localHwParcel, paramCdmaCallWaiting, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramCdmaCallWaiting.release();
      }
    }
    
    public void cdmaInfoRec(int paramInt, CdmaInformationRecords paramCdmaInformationRecords)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel.writeInt32(paramInt);
      paramCdmaInformationRecords.writeToParcel(localHwParcel);
      paramCdmaInformationRecords = new HwParcel();
      try
      {
        mRemote.transact(27, localHwParcel, paramCdmaInformationRecords, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramCdmaInformationRecords.release();
      }
    }
    
    public void cdmaNewSms(int paramInt, CdmaSmsMessage paramCdmaSmsMessage)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel.writeInt32(paramInt);
      paramCdmaSmsMessage.writeToParcel(localHwParcel);
      paramCdmaSmsMessage = new HwParcel();
      try
      {
        mRemote.transact(20, localHwParcel, paramCdmaSmsMessage, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramCdmaSmsMessage.release();
      }
    }
    
    public void cdmaOtaProvisionStatus(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(26, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void cdmaPrlChanged(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
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
    
    public void cdmaRuimSmsStorageFull(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
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
    
    public void cdmaSubscriptionSourceChanged(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(30, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void cellInfoList(int paramInt, ArrayList<CellInfo> paramArrayList)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel.writeInt32(paramInt);
      CellInfo.writeVectorToParcel(localHwParcel, paramArrayList);
      paramArrayList = new HwParcel();
      try
      {
        mRemote.transact(35, localHwParcel, paramArrayList, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramArrayList.release();
      }
    }
    
    public void currentSignalStrength(int paramInt, SignalStrength paramSignalStrength)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel.writeInt32(paramInt);
      paramSignalStrength.writeToParcel(localHwParcel);
      paramSignalStrength = new HwParcel();
      try
      {
        mRemote.transact(9, localHwParcel, paramSignalStrength, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramSignalStrength.release();
      }
    }
    
    public void dataCallListChanged(int paramInt, ArrayList<SetupDataCallResult> paramArrayList)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel.writeInt32(paramInt);
      SetupDataCallResult.writeVectorToParcel(localHwParcel, paramArrayList);
      paramArrayList = new HwParcel();
      try
      {
        mRemote.transact(10, localHwParcel, paramArrayList, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramArrayList.release();
      }
    }
    
    public void enterEmergencyCallbackMode(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel1.writeInt32(paramInt);
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
    
    public final boolean equals(Object paramObject)
    {
      return HidlSupport.interfacesEqual(this, paramObject);
    }
    
    public void exitEmergencyCallbackMode(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
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
    
    public void hardwareConfigChanged(int paramInt, ArrayList<HardwareConfig> paramArrayList)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel.writeInt32(paramInt);
      HardwareConfig.writeVectorToParcel(localHwParcel, paramArrayList);
      paramArrayList = new HwParcel();
      try
      {
        mRemote.transact(39, localHwParcel, paramArrayList, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramArrayList.release();
      }
    }
    
    public final int hashCode()
    {
      return asBinder().hashCode();
    }
    
    public void imsNetworkStateChanged(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel1.writeInt32(paramInt);
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
    
    public void indicateRingbackTone(int paramInt, boolean paramBoolean)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel1.writeInt32(paramInt);
      localHwParcel1.writeBool(paramBoolean);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(28, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
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
    
    public void keepaliveStatus(int paramInt, KeepaliveStatus paramKeepaliveStatus)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.1::IRadioIndication");
      localHwParcel.writeInt32(paramInt);
      paramKeepaliveStatus.writeToParcel(localHwParcel);
      paramKeepaliveStatus = new HwParcel();
      try
      {
        mRemote.transact(48, localHwParcel, paramKeepaliveStatus, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramKeepaliveStatus.release();
      }
    }
    
    public void lceData(int paramInt, LceDataInfo paramLceDataInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel.writeInt32(paramInt);
      paramLceDataInfo.writeToParcel(localHwParcel);
      paramLceDataInfo = new HwParcel();
      try
      {
        mRemote.transact(43, localHwParcel, paramLceDataInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramLceDataInfo.release();
      }
    }
    
    public boolean linkToDeath(IHwBinder.DeathRecipient paramDeathRecipient, long paramLong)
      throws RemoteException
    {
      return mRemote.linkToDeath(paramDeathRecipient, paramLong);
    }
    
    public void modemReset(int paramInt, String paramString)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeString(paramString);
      paramString = new HwParcel();
      try
      {
        mRemote.transact(45, localHwParcel, paramString, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString.release();
      }
    }
    
    public void networkScanResult(int paramInt, NetworkScanResult paramNetworkScanResult)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.1::IRadioIndication");
      localHwParcel.writeInt32(paramInt);
      paramNetworkScanResult.writeToParcel(localHwParcel);
      paramNetworkScanResult = new HwParcel();
      try
      {
        mRemote.transact(47, localHwParcel, paramNetworkScanResult, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramNetworkScanResult.release();
      }
    }
    
    public void networkStateChanged(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(3, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void newBroadcastSms(int paramInt, ArrayList<Byte> paramArrayList)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeInt8Vector(paramArrayList);
      paramArrayList = new HwParcel();
      try
      {
        mRemote.transact(21, localHwParcel, paramArrayList, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramArrayList.release();
      }
    }
    
    public void newSms(int paramInt, ArrayList<Byte> paramArrayList)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeInt8Vector(paramArrayList);
      paramArrayList = new HwParcel();
      try
      {
        mRemote.transact(4, localHwParcel, paramArrayList, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramArrayList.release();
      }
    }
    
    public void newSmsOnSim(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(6, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void newSmsStatusReport(int paramInt, ArrayList<Byte> paramArrayList)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeInt8Vector(paramArrayList);
      paramArrayList = new HwParcel();
      try
      {
        mRemote.transact(5, localHwParcel, paramArrayList, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramArrayList.release();
      }
    }
    
    public void nitzTimeReceived(int paramInt, String paramString, long paramLong)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeString(paramString);
      localHwParcel.writeInt64(paramLong);
      paramString = new HwParcel();
      try
      {
        mRemote.transact(8, localHwParcel, paramString, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString.release();
      }
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
    
    public void onSupplementaryServiceIndication(int paramInt, StkCcUnsolSsResult paramStkCcUnsolSsResult)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel.writeInt32(paramInt);
      paramStkCcUnsolSsResult.writeToParcel(localHwParcel);
      paramStkCcUnsolSsResult = new HwParcel();
      try
      {
        mRemote.transact(41, localHwParcel, paramStkCcUnsolSsResult, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramStkCcUnsolSsResult.release();
      }
    }
    
    public void onUssd(int paramInt1, int paramInt2, String paramString)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel.writeInt32(paramInt1);
      localHwParcel.writeInt32(paramInt2);
      localHwParcel.writeString(paramString);
      paramString = new HwParcel();
      try
      {
        mRemote.transact(7, localHwParcel, paramString, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString.release();
      }
    }
    
    public void pcoData(int paramInt, PcoDataInfo paramPcoDataInfo)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel.writeInt32(paramInt);
      paramPcoDataInfo.writeToParcel(localHwParcel);
      paramPcoDataInfo = new HwParcel();
      try
      {
        mRemote.transact(44, localHwParcel, paramPcoDataInfo, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramPcoDataInfo.release();
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
    
    public void radioCapabilityIndication(int paramInt, RadioCapability paramRadioCapability)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel.writeInt32(paramInt);
      paramRadioCapability.writeToParcel(localHwParcel);
      paramRadioCapability = new HwParcel();
      try
      {
        mRemote.transact(40, localHwParcel, paramRadioCapability, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramRadioCapability.release();
      }
    }
    
    public void radioStateChanged(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(1, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void resendIncallMute(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(29, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void restrictedStateChanged(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
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
    
    public void rilConnected(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel1.writeInt32(paramInt);
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
    
    public void simRefresh(int paramInt, SimRefreshResult paramSimRefreshResult)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel.writeInt32(paramInt);
      paramSimRefreshResult.writeToParcel(localHwParcel);
      paramSimRefreshResult = new HwParcel();
      try
      {
        mRemote.transact(17, localHwParcel, paramSimRefreshResult, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramSimRefreshResult.release();
      }
    }
    
    public void simSmsStorageFull(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
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
    
    public void simStatusChanged(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
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
    
    public void srvccStateNotify(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel1.writeInt32(paramInt1);
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
    
    public void stkCallControlAlphaNotify(int paramInt, String paramString)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeString(paramString);
      paramString = new HwParcel();
      try
      {
        mRemote.transact(42, localHwParcel, paramString, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString.release();
      }
    }
    
    public void stkCallSetup(int paramInt, long paramLong)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel1.writeInt32(paramInt);
      localHwParcel1.writeInt64(paramLong);
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
    
    public void stkEventNotify(int paramInt, String paramString)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeString(paramString);
      paramString = new HwParcel();
      try
      {
        mRemote.transact(14, localHwParcel, paramString, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString.release();
      }
    }
    
    public void stkProactiveCommand(int paramInt, String paramString)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel.writeInt32(paramInt);
      localHwParcel.writeString(paramString);
      paramString = new HwParcel();
      try
      {
        mRemote.transact(13, localHwParcel, paramString, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramString.release();
      }
    }
    
    public void stkSessionEnd(int paramInt)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel1.writeInt32(paramInt);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(12, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
    
    public void subscriptionStatusChanged(int paramInt, boolean paramBoolean)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel1.writeInt32(paramInt);
      localHwParcel1.writeBool(paramBoolean);
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
    
    public void suppSvcNotify(int paramInt, SuppSvcNotification paramSuppSvcNotification)
      throws RemoteException
    {
      HwParcel localHwParcel = new HwParcel();
      localHwParcel.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel.writeInt32(paramInt);
      paramSuppSvcNotification.writeToParcel(localHwParcel);
      paramSuppSvcNotification = new HwParcel();
      try
      {
        mRemote.transact(11, localHwParcel, paramSuppSvcNotification, 1);
        localHwParcel.releaseTemporaryStorage();
        return;
      }
      finally
      {
        paramSuppSvcNotification.release();
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
      return "[class or subclass of android.hardware.radio@1.1::IRadioIndication]@Proxy";
    }
    
    public boolean unlinkToDeath(IHwBinder.DeathRecipient paramDeathRecipient)
      throws RemoteException
    {
      return mRemote.unlinkToDeath(paramDeathRecipient);
    }
    
    public void voiceRadioTechChanged(int paramInt1, int paramInt2)
      throws RemoteException
    {
      HwParcel localHwParcel1 = new HwParcel();
      localHwParcel1.writeInterfaceToken("android.hardware.radio@1.0::IRadioIndication");
      localHwParcel1.writeInt32(paramInt1);
      localHwParcel1.writeInt32(paramInt2);
      HwParcel localHwParcel2 = new HwParcel();
      try
      {
        mRemote.transact(34, localHwParcel1, localHwParcel2, 1);
        localHwParcel1.releaseTemporaryStorage();
        return;
      }
      finally
      {
        localHwParcel2.release();
      }
    }
  }
  
  public static abstract class Stub
    extends HwBinder
    implements IRadioIndication
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
      byte[] arrayOfByte1 = { 92, -114, -5, -71, -60, 81, -91, -105, 55, -19, 44, 108, 32, 35, 10, -82, 71, 69, -125, -100, -96, 29, -128, -120, -42, -36, -55, 2, 14, 82, -46, -59 };
      byte[] arrayOfByte2 = { -67, -38, -74, 24, 77, 122, 52, 109, -90, -96, 125, -64, -126, -116, -15, -102, 105, 111, 76, -86, 54, 17, -59, 31, 46, 20, 86, 90, 20, -76, 15, -39 };
      return new ArrayList(Arrays.asList(new byte[][] { { -4, -59, -56, -56, -117, -123, -87, -10, 63, -70, 103, -39, -26, 116, -38, 70, 108, 114, -87, -116, -94, -121, -13, 67, -5, 87, 33, -48, -104, 113, 63, -122 }, arrayOfByte1, arrayOfByte2 }));
    }
    
    public final ArrayList<String> interfaceChain()
    {
      return new ArrayList(Arrays.asList(new String[] { "android.hardware.radio@1.1::IRadioIndication", "android.hardware.radio@1.0::IRadioIndication", "android.hidl.base@1.0::IBase" }));
    }
    
    public final String interfaceDescriptor()
    {
      return "android.hardware.radio@1.1::IRadioIndication";
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
      int i49 = 1;
      int i50 = 1;
      int i51 = 1;
      int i52 = 1;
      int i53 = 1;
      int i54 = 1;
      switch (paramInt1)
      {
      default: 
        switch (paramInt1)
        {
        default: 
          break;
        case 257250372: 
          paramInt1 = i48;
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
            paramInt1 = i54;
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
            paramInt1 = i49;
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
            paramInt1 = i50;
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
            ArrayList localArrayList = getHashChain();
            paramHwParcel2.writeStatus(0);
            paramHwParcel1 = new HwBlob(16);
            paramInt2 = localArrayList.size();
            paramHwParcel1.putInt32(8L, paramInt2);
            paramHwParcel1.putBool(12L, false);
            HwBlob localHwBlob = new HwBlob(paramInt2 * 32);
            for (paramInt1 = m; paramInt1 < paramInt2; paramInt1++) {
              localHwBlob.putInt8Array(paramInt1 * 32, (byte[])localArrayList.get(paramInt1));
            }
            paramHwParcel1.putBlob(0L, localHwBlob);
            paramHwParcel2.writeBuffer(paramHwParcel1);
            paramHwParcel2.send();
          }
          break;
        case 256136003: 
          if ((paramInt2 & 0x1) != 0) {
            paramInt1 = i51;
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
            paramInt1 = i52;
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
            paramInt1 = i53;
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
      case 48: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.1::IRadioIndication");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new KeepaliveStatus();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          keepaliveStatus(paramInt1, paramHwParcel2);
        }
        break;
      case 47: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.1::IRadioIndication");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new NetworkScanResult();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          networkScanResult(paramInt1, paramHwParcel2);
        }
        break;
      case 46: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.1::IRadioIndication");
          carrierInfoForImsiEncryption(paramHwParcel1.readInt32());
        }
        break;
      case 45: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          modemReset(paramHwParcel1.readInt32(), paramHwParcel1.readString());
        }
        break;
      case 44: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new PcoDataInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          pcoData(paramInt1, paramHwParcel2);
        }
        break;
      case 43: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new LceDataInfo();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          lceData(paramInt1, paramHwParcel2);
        }
        break;
      case 42: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          stkCallControlAlphaNotify(paramHwParcel1.readInt32(), paramHwParcel1.readString());
        }
        break;
      case 41: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new StkCcUnsolSsResult();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          onSupplementaryServiceIndication(paramInt1, paramHwParcel2);
        }
        break;
      case 40: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new RadioCapability();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          radioCapabilityIndication(paramInt1, paramHwParcel2);
        }
        break;
      case 39: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          hardwareConfigChanged(paramHwParcel1.readInt32(), HardwareConfig.readVectorFromParcel(paramHwParcel1));
        }
        break;
      case 38: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          srvccStateNotify(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 37: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          subscriptionStatusChanged(paramHwParcel1.readInt32(), paramHwParcel1.readBool());
        }
        break;
      case 36: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          imsNetworkStateChanged(paramHwParcel1.readInt32());
        }
        break;
      case 35: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          cellInfoList(paramHwParcel1.readInt32(), CellInfo.readVectorFromParcel(paramHwParcel1));
        }
        break;
      case 34: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          voiceRadioTechChanged(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 33: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          rilConnected(paramHwParcel1.readInt32());
        }
        break;
      case 32: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          exitEmergencyCallbackMode(paramHwParcel1.readInt32());
        }
        break;
      case 31: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          cdmaPrlChanged(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 30: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          cdmaSubscriptionSourceChanged(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 29: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          resendIncallMute(paramHwParcel1.readInt32());
        }
        break;
      case 28: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          indicateRingbackTone(paramHwParcel1.readInt32(), paramHwParcel1.readBool());
        }
        break;
      case 27: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new CdmaInformationRecords();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          cdmaInfoRec(paramInt1, paramHwParcel2);
        }
        break;
      case 26: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          cdmaOtaProvisionStatus(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 25: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new CdmaCallWaiting();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          cdmaCallWaiting(paramInt1, paramHwParcel2);
        }
        break;
      case 24: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          enterEmergencyCallbackMode(paramHwParcel1.readInt32());
        }
        break;
      case 23: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          restrictedStateChanged(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 22: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          cdmaRuimSmsStorageFull(paramHwParcel1.readInt32());
        }
        break;
      case 21: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          newBroadcastSms(paramHwParcel1.readInt32(), paramHwParcel1.readInt8Vector());
        }
        break;
      case 20: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new CdmaSmsMessage();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          cdmaNewSms(paramInt1, paramHwParcel2);
        }
        break;
      case 19: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          simStatusChanged(paramHwParcel1.readInt32());
        }
        break;
      case 18: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          paramInt1 = paramHwParcel1.readInt32();
          boolean bool = paramHwParcel1.readBool();
          paramHwParcel2 = new CdmaSignalInfoRecord();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          callRing(paramInt1, bool, paramHwParcel2);
        }
        break;
      case 17: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new SimRefreshResult();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          simRefresh(paramInt1, paramHwParcel2);
        }
        break;
      case 16: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          simSmsStorageFull(paramHwParcel1.readInt32());
        }
        break;
      case 15: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          stkCallSetup(paramHwParcel1.readInt32(), paramHwParcel1.readInt64());
        }
        break;
      case 14: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          stkEventNotify(paramHwParcel1.readInt32(), paramHwParcel1.readString());
        }
        break;
      case 13: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          stkProactiveCommand(paramHwParcel1.readInt32(), paramHwParcel1.readString());
        }
        break;
      case 12: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          stkSessionEnd(paramHwParcel1.readInt32());
        }
        break;
      case 11: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new SuppSvcNotification();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          suppSvcNotify(paramInt1, paramHwParcel2);
        }
        break;
      case 10: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          dataCallListChanged(paramHwParcel1.readInt32(), SetupDataCallResult.readVectorFromParcel(paramHwParcel1));
        }
        break;
      case 9: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          paramInt1 = paramHwParcel1.readInt32();
          paramHwParcel2 = new SignalStrength();
          paramHwParcel2.readFromParcel(paramHwParcel1);
          currentSignalStrength(paramInt1, paramHwParcel2);
        }
        break;
      case 8: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          nitzTimeReceived(paramHwParcel1.readInt32(), paramHwParcel1.readString(), paramHwParcel1.readInt64());
        }
        break;
      case 7: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          onUssd(paramHwParcel1.readInt32(), paramHwParcel1.readInt32(), paramHwParcel1.readString());
        }
        break;
      case 6: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          newSmsOnSim(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      case 5: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          newSmsStatusReport(paramHwParcel1.readInt32(), paramHwParcel1.readInt8Vector());
        }
        break;
      case 4: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          newSms(paramHwParcel1.readInt32(), paramHwParcel1.readInt8Vector());
        }
        break;
      case 3: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          networkStateChanged(paramHwParcel1.readInt32());
        }
        break;
      case 2: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          callStateChanged(paramHwParcel1.readInt32());
        }
        break;
      case 1: 
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
          paramHwParcel1.enforceInterface("android.hardware.radio@1.0::IRadioIndication");
          radioStateChanged(paramHwParcel1.readInt32(), paramHwParcel1.readInt32());
        }
        break;
      }
    }
    
    public final void ping() {}
    
    public IHwInterface queryLocalInterface(String paramString)
    {
      if ("android.hardware.radio@1.1::IRadioIndication".equals(paramString)) {
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
