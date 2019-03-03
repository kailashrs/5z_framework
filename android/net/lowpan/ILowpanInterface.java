package android.net.lowpan;

import android.net.IpPrefix;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.Map;

public abstract interface ILowpanInterface
  extends IInterface
{
  public static final int ERROR_ALREADY = 9;
  public static final int ERROR_BUSY = 8;
  public static final int ERROR_CANCELED = 10;
  public static final int ERROR_DISABLED = 3;
  public static final int ERROR_FEATURE_NOT_SUPPORTED = 11;
  public static final int ERROR_FORM_FAILED_AT_SCAN = 15;
  public static final int ERROR_INVALID_ARGUMENT = 2;
  public static final int ERROR_IO_FAILURE = 6;
  public static final int ERROR_JOIN_FAILED_AT_AUTH = 14;
  public static final int ERROR_JOIN_FAILED_AT_SCAN = 13;
  public static final int ERROR_JOIN_FAILED_UNKNOWN = 12;
  public static final int ERROR_NCP_PROBLEM = 7;
  public static final int ERROR_TIMEOUT = 5;
  public static final int ERROR_UNSPECIFIED = 1;
  public static final int ERROR_WRONG_STATE = 4;
  public static final String KEY_CHANNEL_MASK = "android.net.lowpan.property.CHANNEL_MASK";
  public static final String KEY_MAX_TX_POWER = "android.net.lowpan.property.MAX_TX_POWER";
  public static final String NETWORK_TYPE_THREAD_V1 = "org.threadgroup.thread.v1";
  public static final String NETWORK_TYPE_UNKNOWN = "unknown";
  public static final String PERM_ACCESS_LOWPAN_STATE = "android.permission.ACCESS_LOWPAN_STATE";
  public static final String PERM_CHANGE_LOWPAN_STATE = "android.permission.CHANGE_LOWPAN_STATE";
  public static final String PERM_READ_LOWPAN_CREDENTIAL = "android.permission.READ_LOWPAN_CREDENTIAL";
  public static final String ROLE_COORDINATOR = "coordinator";
  public static final String ROLE_DETACHED = "detached";
  public static final String ROLE_END_DEVICE = "end-device";
  public static final String ROLE_LEADER = "leader";
  public static final String ROLE_ROUTER = "router";
  public static final String ROLE_SLEEPY_END_DEVICE = "sleepy-end-device";
  public static final String ROLE_SLEEPY_ROUTER = "sleepy-router";
  public static final String STATE_ATTACHED = "attached";
  public static final String STATE_ATTACHING = "attaching";
  public static final String STATE_COMMISSIONING = "commissioning";
  public static final String STATE_FAULT = "fault";
  public static final String STATE_OFFLINE = "offline";
  
  public abstract void addExternalRoute(IpPrefix paramIpPrefix, int paramInt)
    throws RemoteException;
  
  public abstract void addListener(ILowpanInterfaceListener paramILowpanInterfaceListener)
    throws RemoteException;
  
  public abstract void addOnMeshPrefix(IpPrefix paramIpPrefix, int paramInt)
    throws RemoteException;
  
  public abstract void attach(LowpanProvision paramLowpanProvision)
    throws RemoteException;
  
  public abstract void beginLowPower()
    throws RemoteException;
  
  public abstract void closeCommissioningSession()
    throws RemoteException;
  
  public abstract void form(LowpanProvision paramLowpanProvision)
    throws RemoteException;
  
  public abstract String getDriverVersion()
    throws RemoteException;
  
  public abstract byte[] getExtendedAddress()
    throws RemoteException;
  
  public abstract String[] getLinkAddresses()
    throws RemoteException;
  
  public abstract IpPrefix[] getLinkNetworks()
    throws RemoteException;
  
  public abstract LowpanCredential getLowpanCredential()
    throws RemoteException;
  
  public abstract LowpanIdentity getLowpanIdentity()
    throws RemoteException;
  
  public abstract byte[] getMacAddress()
    throws RemoteException;
  
  public abstract String getName()
    throws RemoteException;
  
  public abstract String getNcpVersion()
    throws RemoteException;
  
  public abstract String getPartitionId()
    throws RemoteException;
  
  public abstract String getRole()
    throws RemoteException;
  
  public abstract String getState()
    throws RemoteException;
  
  public abstract LowpanChannelInfo[] getSupportedChannels()
    throws RemoteException;
  
  public abstract String[] getSupportedNetworkTypes()
    throws RemoteException;
  
  public abstract boolean isCommissioned()
    throws RemoteException;
  
  public abstract boolean isConnected()
    throws RemoteException;
  
  public abstract boolean isEnabled()
    throws RemoteException;
  
  public abstract boolean isUp()
    throws RemoteException;
  
  public abstract void join(LowpanProvision paramLowpanProvision)
    throws RemoteException;
  
  public abstract void leave()
    throws RemoteException;
  
  public abstract void onHostWake()
    throws RemoteException;
  
  public abstract void pollForData()
    throws RemoteException;
  
  public abstract void removeExternalRoute(IpPrefix paramIpPrefix)
    throws RemoteException;
  
  public abstract void removeListener(ILowpanInterfaceListener paramILowpanInterfaceListener)
    throws RemoteException;
  
  public abstract void removeOnMeshPrefix(IpPrefix paramIpPrefix)
    throws RemoteException;
  
  public abstract void reset()
    throws RemoteException;
  
  public abstract void sendToCommissioner(byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void setEnabled(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void startCommissioningSession(LowpanBeaconInfo paramLowpanBeaconInfo)
    throws RemoteException;
  
  public abstract void startEnergyScan(Map paramMap, ILowpanEnergyScanCallback paramILowpanEnergyScanCallback)
    throws RemoteException;
  
  public abstract void startNetScan(Map paramMap, ILowpanNetScanCallback paramILowpanNetScanCallback)
    throws RemoteException;
  
  public abstract void stopEnergyScan()
    throws RemoteException;
  
  public abstract void stopNetScan()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ILowpanInterface
  {
    private static final String DESCRIPTOR = "android.net.lowpan.ILowpanInterface";
    static final int TRANSACTION_addExternalRoute = 39;
    static final int TRANSACTION_addListener = 31;
    static final int TRANSACTION_addOnMeshPrefix = 37;
    static final int TRANSACTION_attach = 22;
    static final int TRANSACTION_beginLowPower = 28;
    static final int TRANSACTION_closeCommissioningSession = 26;
    static final int TRANSACTION_form = 21;
    static final int TRANSACTION_getDriverVersion = 3;
    static final int TRANSACTION_getExtendedAddress = 15;
    static final int TRANSACTION_getLinkAddresses = 18;
    static final int TRANSACTION_getLinkNetworks = 19;
    static final int TRANSACTION_getLowpanCredential = 17;
    static final int TRANSACTION_getLowpanIdentity = 16;
    static final int TRANSACTION_getMacAddress = 6;
    static final int TRANSACTION_getName = 1;
    static final int TRANSACTION_getNcpVersion = 2;
    static final int TRANSACTION_getPartitionId = 14;
    static final int TRANSACTION_getRole = 13;
    static final int TRANSACTION_getState = 12;
    static final int TRANSACTION_getSupportedChannels = 4;
    static final int TRANSACTION_getSupportedNetworkTypes = 5;
    static final int TRANSACTION_isCommissioned = 10;
    static final int TRANSACTION_isConnected = 11;
    static final int TRANSACTION_isEnabled = 7;
    static final int TRANSACTION_isUp = 9;
    static final int TRANSACTION_join = 20;
    static final int TRANSACTION_leave = 23;
    static final int TRANSACTION_onHostWake = 30;
    static final int TRANSACTION_pollForData = 29;
    static final int TRANSACTION_removeExternalRoute = 40;
    static final int TRANSACTION_removeListener = 32;
    static final int TRANSACTION_removeOnMeshPrefix = 38;
    static final int TRANSACTION_reset = 24;
    static final int TRANSACTION_sendToCommissioner = 27;
    static final int TRANSACTION_setEnabled = 8;
    static final int TRANSACTION_startCommissioningSession = 25;
    static final int TRANSACTION_startEnergyScan = 35;
    static final int TRANSACTION_startNetScan = 33;
    static final int TRANSACTION_stopEnergyScan = 36;
    static final int TRANSACTION_stopNetScan = 34;
    
    public Stub()
    {
      attachInterface(this, "android.net.lowpan.ILowpanInterface");
    }
    
    public static ILowpanInterface asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.lowpan.ILowpanInterface");
      if ((localIInterface != null) && ((localIInterface instanceof ILowpanInterface))) {
        return (ILowpanInterface)localIInterface;
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
        boolean bool = false;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        Object localObject7 = null;
        Object localObject8 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 40: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (IpPrefix)IpPrefix.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject8;
          }
          removeExternalRoute(paramParcel1);
          return true;
        case 39: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          if (paramParcel1.readInt() != 0) {
            localObject8 = (IpPrefix)IpPrefix.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject8 = localObject1;
          }
          addExternalRoute((IpPrefix)localObject8, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 38: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (IpPrefix)IpPrefix.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          removeOnMeshPrefix(paramParcel1);
          return true;
        case 37: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          if (paramParcel1.readInt() != 0) {
            localObject8 = (IpPrefix)IpPrefix.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject8 = localObject3;
          }
          addOnMeshPrefix((IpPrefix)localObject8, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 36: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          stopEnergyScan();
          return true;
        case 35: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          startEnergyScan(paramParcel1.readHashMap(getClass().getClassLoader()), ILowpanEnergyScanCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 34: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          stopNetScan();
          return true;
        case 33: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          startNetScan(paramParcel1.readHashMap(getClass().getClassLoader()), ILowpanNetScanCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 32: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          removeListener(ILowpanInterfaceListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 31: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          addListener(ILowpanInterfaceListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 30: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          onHostWake();
          return true;
        case 29: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          pollForData();
          return true;
        case 28: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          beginLowPower();
          paramParcel2.writeNoException();
          return true;
        case 27: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          sendToCommissioner(paramParcel1.createByteArray());
          return true;
        case 26: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          closeCommissioningSession();
          paramParcel2.writeNoException();
          return true;
        case 25: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (LowpanBeaconInfo)LowpanBeaconInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          startCommissioningSession(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          reset();
          paramParcel2.writeNoException();
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          leave();
          paramParcel2.writeNoException();
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (LowpanProvision)LowpanProvision.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject5;
          }
          attach(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (LowpanProvision)LowpanProvision.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject6;
          }
          form(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (LowpanProvision)LowpanProvision.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject7;
          }
          join(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          paramParcel1 = getLinkNetworks();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          paramParcel1 = getLinkAddresses();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          paramParcel1 = getLowpanCredential();
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
        case 16: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          paramParcel1 = getLowpanIdentity();
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
        case 15: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          paramParcel1 = getExtendedAddress();
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          paramParcel1 = getPartitionId();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          paramParcel1 = getRole();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          paramParcel1 = getState();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          paramInt1 = isConnected();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          paramInt1 = isCommissioned();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          paramInt1 = isUp();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          if (paramParcel1.readInt() != 0) {
            bool = true;
          }
          setEnabled(bool);
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          paramInt1 = isEnabled();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          paramParcel1 = getMacAddress();
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          paramParcel1 = getSupportedNetworkTypes();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          paramParcel1 = getSupportedChannels();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          paramParcel1 = getDriverVersion();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
          paramParcel1 = getNcpVersion();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterface");
        paramParcel1 = getName();
        paramParcel2.writeNoException();
        paramParcel2.writeString(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.net.lowpan.ILowpanInterface");
      return true;
    }
    
    private static class Proxy
      implements ILowpanInterface
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addExternalRoute(IpPrefix paramIpPrefix, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          if (paramIpPrefix != null)
          {
            localParcel1.writeInt(1);
            paramIpPrefix.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(39, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void addListener(ILowpanInterfaceListener paramILowpanInterfaceListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          if (paramILowpanInterfaceListener != null) {
            paramILowpanInterfaceListener = paramILowpanInterfaceListener.asBinder();
          } else {
            paramILowpanInterfaceListener = null;
          }
          localParcel1.writeStrongBinder(paramILowpanInterfaceListener);
          mRemote.transact(31, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void addOnMeshPrefix(IpPrefix paramIpPrefix, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          if (paramIpPrefix != null)
          {
            localParcel1.writeInt(1);
            paramIpPrefix.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void attach(LowpanProvision paramLowpanProvision)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          if (paramLowpanProvision != null)
          {
            localParcel1.writeInt(1);
            paramLowpanProvision.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void beginLowPower()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
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
      
      public void closeCommissioningSession()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          mRemote.transact(26, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void form(LowpanProvision paramLowpanProvision)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          if (paramLowpanProvision != null)
          {
            localParcel1.writeInt(1);
            paramLowpanProvision.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getDriverVersion()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          mRemote.transact(3, localParcel1, localParcel2, 0);
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
      
      public byte[] getExtendedAddress()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          byte[] arrayOfByte = localParcel2.createByteArray();
          return arrayOfByte;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.net.lowpan.ILowpanInterface";
      }
      
      public String[] getLinkAddresses()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IpPrefix[] getLinkNetworks()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IpPrefix[] arrayOfIpPrefix = (IpPrefix[])localParcel2.createTypedArray(IpPrefix.CREATOR);
          return arrayOfIpPrefix;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public LowpanCredential getLowpanCredential()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          LowpanCredential localLowpanCredential;
          if (localParcel2.readInt() != 0) {
            localLowpanCredential = (LowpanCredential)LowpanCredential.CREATOR.createFromParcel(localParcel2);
          } else {
            localLowpanCredential = null;
          }
          return localLowpanCredential;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public LowpanIdentity getLowpanIdentity()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          LowpanIdentity localLowpanIdentity;
          if (localParcel2.readInt() != 0) {
            localLowpanIdentity = (LowpanIdentity)LowpanIdentity.CREATOR.createFromParcel(localParcel2);
          } else {
            localLowpanIdentity = null;
          }
          return localLowpanIdentity;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public byte[] getMacAddress()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          byte[] arrayOfByte = localParcel2.createByteArray();
          return arrayOfByte;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getName()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          mRemote.transact(1, localParcel1, localParcel2, 0);
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
      
      public String getNcpVersion()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          mRemote.transact(2, localParcel1, localParcel2, 0);
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
      
      public String getPartitionId()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          mRemote.transact(14, localParcel1, localParcel2, 0);
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
      
      public String getRole()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          mRemote.transact(13, localParcel1, localParcel2, 0);
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
      
      public String getState()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          mRemote.transact(12, localParcel1, localParcel2, 0);
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
      
      public LowpanChannelInfo[] getSupportedChannels()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          LowpanChannelInfo[] arrayOfLowpanChannelInfo = (LowpanChannelInfo[])localParcel2.createTypedArray(LowpanChannelInfo.CREATOR);
          return arrayOfLowpanChannelInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getSupportedNetworkTypes()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isCommissioned()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(10, localParcel1, localParcel2, 0);
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
      
      public boolean isConnected()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(11, localParcel1, localParcel2, 0);
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
      
      public boolean isEnabled()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(7, localParcel1, localParcel2, 0);
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
      
      public boolean isUp()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(9, localParcel1, localParcel2, 0);
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
      
      public void join(LowpanProvision paramLowpanProvision)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          if (paramLowpanProvision != null)
          {
            localParcel1.writeInt(1);
            paramLowpanProvision.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void leave()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          mRemote.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void onHostWake()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          mRemote.transact(30, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void pollForData()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          mRemote.transact(29, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void removeExternalRoute(IpPrefix paramIpPrefix)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          if (paramIpPrefix != null)
          {
            localParcel.writeInt(1);
            paramIpPrefix.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(40, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void removeListener(ILowpanInterfaceListener paramILowpanInterfaceListener)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          if (paramILowpanInterfaceListener != null) {
            paramILowpanInterfaceListener = paramILowpanInterfaceListener.asBinder();
          } else {
            paramILowpanInterfaceListener = null;
          }
          localParcel.writeStrongBinder(paramILowpanInterfaceListener);
          mRemote.transact(32, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void removeOnMeshPrefix(IpPrefix paramIpPrefix)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          if (paramIpPrefix != null)
          {
            localParcel.writeInt(1);
            paramIpPrefix.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(38, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void reset()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          mRemote.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void sendToCommissioner(byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          localParcel.writeByteArray(paramArrayOfByte);
          mRemote.transact(27, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setEnabled(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startCommissioningSession(LowpanBeaconInfo paramLowpanBeaconInfo)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          if (paramLowpanBeaconInfo != null)
          {
            localParcel1.writeInt(1);
            paramLowpanBeaconInfo.writeToParcel(localParcel1, 0);
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
      
      public void startEnergyScan(Map paramMap, ILowpanEnergyScanCallback paramILowpanEnergyScanCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          localParcel1.writeMap(paramMap);
          if (paramILowpanEnergyScanCallback != null) {
            paramMap = paramILowpanEnergyScanCallback.asBinder();
          } else {
            paramMap = null;
          }
          localParcel1.writeStrongBinder(paramMap);
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
      
      public void startNetScan(Map paramMap, ILowpanNetScanCallback paramILowpanNetScanCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          localParcel1.writeMap(paramMap);
          if (paramILowpanNetScanCallback != null) {
            paramMap = paramILowpanNetScanCallback.asBinder();
          } else {
            paramMap = null;
          }
          localParcel1.writeStrongBinder(paramMap);
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
      
      public void stopEnergyScan()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          mRemote.transact(36, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void stopNetScan()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.lowpan.ILowpanInterface");
          mRemote.transact(34, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
    }
  }
}
