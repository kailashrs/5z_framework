package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.PersistableBundle;
import android.os.RemoteException;
import java.io.FileDescriptor;

public abstract interface INetd
  extends IInterface
{
  public static final int CONF = 1;
  public static final String IPSEC_INTERFACE_PREFIX = "ipsec";
  public static final int IPV4 = 4;
  public static final int IPV6 = 6;
  public static final int IPV6_ADDR_GEN_MODE_DEFAULT = 0;
  public static final int IPV6_ADDR_GEN_MODE_EUI64 = 0;
  public static final int IPV6_ADDR_GEN_MODE_NONE = 1;
  public static final int IPV6_ADDR_GEN_MODE_RANDOM = 3;
  public static final int IPV6_ADDR_GEN_MODE_STABLE_PRIVACY = 2;
  public static final int NEIGH = 2;
  public static final String PERMISSION_NETWORK = "NETWORK";
  public static final String PERMISSION_SYSTEM = "SYSTEM";
  public static final int RESOLVER_PARAMS_COUNT = 4;
  public static final int RESOLVER_PARAMS_MAX_SAMPLES = 3;
  public static final int RESOLVER_PARAMS_MIN_SAMPLES = 2;
  public static final int RESOLVER_PARAMS_SAMPLE_VALIDITY = 0;
  public static final int RESOLVER_PARAMS_SUCCESS_THRESHOLD = 1;
  public static final int RESOLVER_STATS_COUNT = 7;
  public static final int RESOLVER_STATS_ERRORS = 1;
  public static final int RESOLVER_STATS_INTERNAL_ERRORS = 3;
  public static final int RESOLVER_STATS_LAST_SAMPLE_TIME = 5;
  public static final int RESOLVER_STATS_RTT_AVG = 4;
  public static final int RESOLVER_STATS_SUCCESSES = 0;
  public static final int RESOLVER_STATS_TIMEOUTS = 2;
  public static final int RESOLVER_STATS_USABLE = 6;
  public static final int TETHER_STATS_ARRAY_SIZE = 4;
  public static final int TETHER_STATS_RX_BYTES = 0;
  public static final int TETHER_STATS_RX_PACKETS = 1;
  public static final int TETHER_STATS_TX_BYTES = 2;
  public static final int TETHER_STATS_TX_PACKETS = 3;
  
  public abstract void addVirtualTunnelInterface(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract boolean bandwidthEnableDataSaver(boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean firewallReplaceUidChain(String paramString, boolean paramBoolean, int[] paramArrayOfInt)
    throws RemoteException;
  
  public abstract int getMetricsReportingLevel()
    throws RemoteException;
  
  public abstract void getResolverInfo(int paramInt, String[] paramArrayOfString1, String[] paramArrayOfString2, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
    throws RemoteException;
  
  public abstract void interfaceAddAddress(String paramString1, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract void interfaceDelAddress(String paramString1, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract void ipSecAddSecurityAssociation(int paramInt1, int paramInt2, String paramString1, String paramString2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, String paramString3, byte[] paramArrayOfByte1, int paramInt7, String paramString4, byte[] paramArrayOfByte2, int paramInt8, String paramString5, byte[] paramArrayOfByte3, int paramInt9, int paramInt10, int paramInt11, int paramInt12)
    throws RemoteException;
  
  public abstract void ipSecAddSecurityPolicy(int paramInt1, int paramInt2, String paramString1, String paramString2, int paramInt3, int paramInt4, int paramInt5)
    throws RemoteException;
  
  public abstract int ipSecAllocateSpi(int paramInt1, String paramString1, String paramString2, int paramInt2)
    throws RemoteException;
  
  public abstract void ipSecApplyTransportModeTransform(FileDescriptor paramFileDescriptor, int paramInt1, int paramInt2, String paramString1, String paramString2, int paramInt3)
    throws RemoteException;
  
  public abstract void ipSecDeleteSecurityAssociation(int paramInt1, String paramString1, String paramString2, int paramInt2, int paramInt3, int paramInt4)
    throws RemoteException;
  
  public abstract void ipSecDeleteSecurityPolicy(int paramInt1, int paramInt2, String paramString1, String paramString2, int paramInt3, int paramInt4)
    throws RemoteException;
  
  public abstract void ipSecRemoveTransportModeTransform(FileDescriptor paramFileDescriptor)
    throws RemoteException;
  
  public abstract void ipSecSetEncapSocketOwner(FileDescriptor paramFileDescriptor, int paramInt)
    throws RemoteException;
  
  public abstract void ipSecUpdateSecurityPolicy(int paramInt1, int paramInt2, String paramString1, String paramString2, int paramInt3, int paramInt4, int paramInt5)
    throws RemoteException;
  
  public abstract boolean isAlive()
    throws RemoteException;
  
  public abstract void networkAddInterface(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void networkAddUidRanges(int paramInt, UidRange[] paramArrayOfUidRange)
    throws RemoteException;
  
  public abstract void networkCreatePhysical(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void networkCreateVpn(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract void networkDestroy(int paramInt)
    throws RemoteException;
  
  public abstract void networkRejectNonSecureVpn(boolean paramBoolean, UidRange[] paramArrayOfUidRange)
    throws RemoteException;
  
  public abstract void networkRemoveInterface(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void networkRemoveUidRanges(int paramInt, UidRange[] paramArrayOfUidRange)
    throws RemoteException;
  
  public abstract void removeVirtualTunnelInterface(String paramString)
    throws RemoteException;
  
  public abstract void setIPv6AddrGenMode(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void setMetricsReportingLevel(int paramInt)
    throws RemoteException;
  
  public abstract void setProcSysNet(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3)
    throws RemoteException;
  
  public abstract void setResolverConfiguration(int paramInt, String[] paramArrayOfString1, String[] paramArrayOfString2, int[] paramArrayOfInt, String paramString, String[] paramArrayOfString3, String[] paramArrayOfString4)
    throws RemoteException;
  
  public abstract void socketDestroy(UidRange[] paramArrayOfUidRange, int[] paramArrayOfInt)
    throws RemoteException;
  
  public abstract boolean tetherApplyDnsInterfaces()
    throws RemoteException;
  
  public abstract PersistableBundle tetherGetStats()
    throws RemoteException;
  
  public abstract boolean trafficCheckBpfStatsEnable()
    throws RemoteException;
  
  public abstract void updateVirtualTunnelInterface(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void wakeupAddInterface(String paramString1, String paramString2, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void wakeupDelInterface(String paramString1, String paramString2, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements INetd
  {
    private static final String DESCRIPTOR = "android.net.INetd";
    static final int TRANSACTION_addVirtualTunnelInterface = 31;
    static final int TRANSACTION_bandwidthEnableDataSaver = 3;
    static final int TRANSACTION_firewallReplaceUidChain = 2;
    static final int TRANSACTION_getMetricsReportingLevel = 20;
    static final int TRANSACTION_getResolverInfo = 14;
    static final int TRANSACTION_interfaceAddAddress = 17;
    static final int TRANSACTION_interfaceDelAddress = 18;
    static final int TRANSACTION_ipSecAddSecurityAssociation = 24;
    static final int TRANSACTION_ipSecAddSecurityPolicy = 28;
    static final int TRANSACTION_ipSecAllocateSpi = 23;
    static final int TRANSACTION_ipSecApplyTransportModeTransform = 26;
    static final int TRANSACTION_ipSecDeleteSecurityAssociation = 25;
    static final int TRANSACTION_ipSecDeleteSecurityPolicy = 30;
    static final int TRANSACTION_ipSecRemoveTransportModeTransform = 27;
    static final int TRANSACTION_ipSecSetEncapSocketOwner = 22;
    static final int TRANSACTION_ipSecUpdateSecurityPolicy = 29;
    static final int TRANSACTION_isAlive = 1;
    static final int TRANSACTION_networkAddInterface = 7;
    static final int TRANSACTION_networkAddUidRanges = 9;
    static final int TRANSACTION_networkCreatePhysical = 4;
    static final int TRANSACTION_networkCreateVpn = 5;
    static final int TRANSACTION_networkDestroy = 6;
    static final int TRANSACTION_networkRejectNonSecureVpn = 11;
    static final int TRANSACTION_networkRemoveInterface = 8;
    static final int TRANSACTION_networkRemoveUidRanges = 10;
    static final int TRANSACTION_removeVirtualTunnelInterface = 33;
    static final int TRANSACTION_setIPv6AddrGenMode = 36;
    static final int TRANSACTION_setMetricsReportingLevel = 21;
    static final int TRANSACTION_setProcSysNet = 19;
    static final int TRANSACTION_setResolverConfiguration = 13;
    static final int TRANSACTION_socketDestroy = 12;
    static final int TRANSACTION_tetherApplyDnsInterfaces = 15;
    static final int TRANSACTION_tetherGetStats = 16;
    static final int TRANSACTION_trafficCheckBpfStatsEnable = 37;
    static final int TRANSACTION_updateVirtualTunnelInterface = 32;
    static final int TRANSACTION_wakeupAddInterface = 34;
    static final int TRANSACTION_wakeupDelInterface = 35;
    
    public Stub()
    {
      attachInterface(this, "android.net.INetd");
    }
    
    public static INetd asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.INetd");
      if ((localIInterface != null) && ((localIInterface instanceof INetd))) {
        return (INetd)localIInterface;
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
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        boolean bool4 = false;
        Object localObject;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 37: 
          paramParcel1.enforceInterface("android.net.INetd");
          paramInt1 = trafficCheckBpfStatsEnable();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 36: 
          paramParcel1.enforceInterface("android.net.INetd");
          setIPv6AddrGenMode(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 35: 
          paramParcel1.enforceInterface("android.net.INetd");
          wakeupDelInterface(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 34: 
          paramParcel1.enforceInterface("android.net.INetd");
          wakeupAddInterface(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 33: 
          paramParcel1.enforceInterface("android.net.INetd");
          removeVirtualTunnelInterface(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 32: 
          paramParcel1.enforceInterface("android.net.INetd");
          updateVirtualTunnelInterface(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 31: 
          paramParcel1.enforceInterface("android.net.INetd");
          addVirtualTunnelInterface(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 30: 
          paramParcel1.enforceInterface("android.net.INetd");
          ipSecDeleteSecurityPolicy(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 29: 
          paramParcel1.enforceInterface("android.net.INetd");
          ipSecUpdateSecurityPolicy(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 28: 
          paramParcel1.enforceInterface("android.net.INetd");
          ipSecAddSecurityPolicy(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 27: 
          paramParcel1.enforceInterface("android.net.INetd");
          ipSecRemoveTransportModeTransform(paramParcel1.readRawFileDescriptor());
          paramParcel2.writeNoException();
          return true;
        case 26: 
          paramParcel1.enforceInterface("android.net.INetd");
          ipSecApplyTransportModeTransform(paramParcel1.readRawFileDescriptor(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 25: 
          paramParcel1.enforceInterface("android.net.INetd");
          ipSecDeleteSecurityAssociation(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.net.INetd");
          ipSecAddSecurityAssociation(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.createByteArray(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.createByteArray(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.createByteArray(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.net.INetd");
          paramInt1 = ipSecAllocateSpi(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.net.INetd");
          ipSecSetEncapSocketOwner(paramParcel1.readRawFileDescriptor(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.net.INetd");
          setMetricsReportingLevel(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.net.INetd");
          paramInt1 = getMetricsReportingLevel();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.net.INetd");
          setProcSysNet(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.net.INetd");
          interfaceDelAddress(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.net.INetd");
          interfaceAddAddress(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.net.INetd");
          paramParcel1 = tetherGetStats();
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
          paramParcel1.enforceInterface("android.net.INetd");
          paramInt1 = tetherApplyDnsInterfaces();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.net.INetd");
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          if (paramInt2 < 0) {
            localObject = null;
          } else {
            localObject = new String[paramInt2];
          }
          paramInt2 = paramParcel1.readInt();
          String[] arrayOfString;
          if (paramInt2 < 0) {
            arrayOfString = null;
          } else {
            arrayOfString = new String[paramInt2];
          }
          paramInt2 = paramParcel1.readInt();
          int[] arrayOfInt;
          if (paramInt2 < 0) {
            arrayOfInt = null;
          } else {
            arrayOfInt = new int[paramInt2];
          }
          paramInt2 = paramParcel1.readInt();
          if (paramInt2 < 0) {
            paramParcel1 = null;
          } else {
            paramParcel1 = new int[paramInt2];
          }
          getResolverInfo(paramInt1, (String[])localObject, arrayOfString, arrayOfInt, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray((String[])localObject);
          paramParcel2.writeStringArray(arrayOfString);
          paramParcel2.writeIntArray(arrayOfInt);
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.net.INetd");
          setResolverConfiguration(paramParcel1.readInt(), paramParcel1.createStringArray(), paramParcel1.createStringArray(), paramParcel1.createIntArray(), paramParcel1.readString(), paramParcel1.createStringArray(), paramParcel1.createStringArray());
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.net.INetd");
          socketDestroy((UidRange[])paramParcel1.createTypedArray(UidRange.CREATOR), paramParcel1.createIntArray());
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.net.INetd");
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          networkRejectNonSecureVpn(bool4, (UidRange[])paramParcel1.createTypedArray(UidRange.CREATOR));
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.net.INetd");
          networkRemoveUidRanges(paramParcel1.readInt(), (UidRange[])paramParcel1.createTypedArray(UidRange.CREATOR));
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.net.INetd");
          networkAddUidRanges(paramParcel1.readInt(), (UidRange[])paramParcel1.createTypedArray(UidRange.CREATOR));
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.net.INetd");
          networkRemoveInterface(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.net.INetd");
          networkAddInterface(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.net.INetd");
          networkDestroy(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.net.INetd");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          } else {
            bool4 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          }
          networkCreateVpn(paramInt1, bool4, bool1);
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.net.INetd");
          networkCreatePhysical(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.net.INetd");
          bool4 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          paramInt1 = bandwidthEnableDataSaver(bool4);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.net.INetd");
          localObject = paramParcel1.readString();
          bool4 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool4 = true;
          }
          paramInt1 = firewallReplaceUidChain((String)localObject, bool4, paramParcel1.createIntArray());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.net.INetd");
        paramInt1 = isAlive();
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.net.INetd");
      return true;
    }
    
    private static class Proxy
      implements INetd
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addVirtualTunnelInterface(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public boolean bandwidthEnableDataSaver(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeInt(paramBoolean);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(3, localParcel1, localParcel2, 0);
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
      
      public boolean firewallReplaceUidChain(String paramString, boolean paramBoolean, int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeIntArray(paramArrayOfInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(2, localParcel1, localParcel2, 0);
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
      
      public String getInterfaceDescriptor()
      {
        return "android.net.INetd";
      }
      
      public int getMetricsReportingLevel()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          mRemote.transact(20, localParcel1, localParcel2, 0);
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
      
      public void getResolverInfo(int paramInt, String[] paramArrayOfString1, String[] paramArrayOfString2, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeInt(paramInt);
          if (paramArrayOfString1 == null) {
            localParcel1.writeInt(-1);
          } else {
            localParcel1.writeInt(paramArrayOfString1.length);
          }
          if (paramArrayOfString2 == null) {
            localParcel1.writeInt(-1);
          } else {
            localParcel1.writeInt(paramArrayOfString2.length);
          }
          if (paramArrayOfInt1 == null) {
            localParcel1.writeInt(-1);
          } else {
            localParcel1.writeInt(paramArrayOfInt1.length);
          }
          if (paramArrayOfInt2 == null) {
            localParcel1.writeInt(-1);
          } else {
            localParcel1.writeInt(paramArrayOfInt2.length);
          }
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          localParcel2.readStringArray(paramArrayOfString1);
          localParcel2.readStringArray(paramArrayOfString2);
          localParcel2.readIntArray(paramArrayOfInt1);
          localParcel2.readIntArray(paramArrayOfInt2);
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void interfaceAddAddress(String paramString1, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void interfaceDelAddress(String paramString1, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      /* Error */
      public void ipSecAddSecurityAssociation(int paramInt1, int paramInt2, String paramString1, String paramString2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, String paramString3, byte[] paramArrayOfByte1, int paramInt7, String paramString4, byte[] paramArrayOfByte2, int paramInt8, String paramString5, byte[] paramArrayOfByte3, int paramInt9, int paramInt10, int paramInt11, int paramInt12)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 30	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 21
        //   5: invokestatic 30	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   8: astore 22
        //   10: aload 21
        //   12: ldc 32
        //   14: invokevirtual 36	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   17: aload 21
        //   19: iload_1
        //   20: invokevirtual 43	android/os/Parcel:writeInt	(I)V
        //   23: aload 21
        //   25: iload_2
        //   26: invokevirtual 43	android/os/Parcel:writeInt	(I)V
        //   29: aload 21
        //   31: aload_3
        //   32: invokevirtual 39	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   35: aload 21
        //   37: aload 4
        //   39: invokevirtual 39	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   42: aload 21
        //   44: iload 5
        //   46: invokevirtual 43	android/os/Parcel:writeInt	(I)V
        //   49: aload 21
        //   51: iload 6
        //   53: invokevirtual 43	android/os/Parcel:writeInt	(I)V
        //   56: aload 21
        //   58: iload 7
        //   60: invokevirtual 43	android/os/Parcel:writeInt	(I)V
        //   63: aload 21
        //   65: iload 8
        //   67: invokevirtual 43	android/os/Parcel:writeInt	(I)V
        //   70: aload 21
        //   72: aload 9
        //   74: invokevirtual 39	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   77: aload 21
        //   79: aload 10
        //   81: invokevirtual 91	android/os/Parcel:writeByteArray	([B)V
        //   84: aload 21
        //   86: iload 11
        //   88: invokevirtual 43	android/os/Parcel:writeInt	(I)V
        //   91: aload 21
        //   93: aload 12
        //   95: invokevirtual 39	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   98: aload 21
        //   100: aload 13
        //   102: invokevirtual 91	android/os/Parcel:writeByteArray	([B)V
        //   105: aload 21
        //   107: iload 14
        //   109: invokevirtual 43	android/os/Parcel:writeInt	(I)V
        //   112: aload 21
        //   114: aload 15
        //   116: invokevirtual 39	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   119: aload 21
        //   121: aload 16
        //   123: invokevirtual 91	android/os/Parcel:writeByteArray	([B)V
        //   126: aload 21
        //   128: iload 17
        //   130: invokevirtual 43	android/os/Parcel:writeInt	(I)V
        //   133: aload 21
        //   135: iload 18
        //   137: invokevirtual 43	android/os/Parcel:writeInt	(I)V
        //   140: aload 21
        //   142: iload 19
        //   144: invokevirtual 43	android/os/Parcel:writeInt	(I)V
        //   147: aload 21
        //   149: iload 20
        //   151: invokevirtual 43	android/os/Parcel:writeInt	(I)V
        //   154: aload_0
        //   155: getfield 19	android/net/INetd$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   158: bipush 24
        //   160: aload 21
        //   162: aload 22
        //   164: iconst_0
        //   165: invokeinterface 49 5 0
        //   170: pop
        //   171: aload 22
        //   173: invokevirtual 52	android/os/Parcel:readException	()V
        //   176: aload 22
        //   178: invokevirtual 55	android/os/Parcel:recycle	()V
        //   181: aload 21
        //   183: invokevirtual 55	android/os/Parcel:recycle	()V
        //   186: return
        //   187: astore_3
        //   188: goto +44 -> 232
        //   191: astore_3
        //   192: goto +40 -> 232
        //   195: astore_3
        //   196: goto +36 -> 232
        //   199: astore_3
        //   200: goto +32 -> 232
        //   203: astore_3
        //   204: goto +28 -> 232
        //   207: astore_3
        //   208: goto +24 -> 232
        //   211: astore_3
        //   212: goto +20 -> 232
        //   215: astore_3
        //   216: goto +16 -> 232
        //   219: astore_3
        //   220: goto +12 -> 232
        //   223: astore_3
        //   224: goto +8 -> 232
        //   227: astore_3
        //   228: goto +4 -> 232
        //   231: astore_3
        //   232: aload 22
        //   234: invokevirtual 55	android/os/Parcel:recycle	()V
        //   237: aload 21
        //   239: invokevirtual 55	android/os/Parcel:recycle	()V
        //   242: aload_3
        //   243: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	244	0	this	Proxy
        //   0	244	1	paramInt1	int
        //   0	244	2	paramInt2	int
        //   0	244	3	paramString1	String
        //   0	244	4	paramString2	String
        //   0	244	5	paramInt3	int
        //   0	244	6	paramInt4	int
        //   0	244	7	paramInt5	int
        //   0	244	8	paramInt6	int
        //   0	244	9	paramString3	String
        //   0	244	10	paramArrayOfByte1	byte[]
        //   0	244	11	paramInt7	int
        //   0	244	12	paramString4	String
        //   0	244	13	paramArrayOfByte2	byte[]
        //   0	244	14	paramInt8	int
        //   0	244	15	paramString5	String
        //   0	244	16	paramArrayOfByte3	byte[]
        //   0	244	17	paramInt9	int
        //   0	244	18	paramInt10	int
        //   0	244	19	paramInt11	int
        //   0	244	20	paramInt12	int
        //   3	235	21	localParcel1	Parcel
        //   8	225	22	localParcel2	Parcel
        // Exception table:
        //   from	to	target	type
        //   98	176	187	finally
        //   91	98	191	finally
        //   84	91	195	finally
        //   77	84	199	finally
        //   70	77	203	finally
        //   63	70	207	finally
        //   56	63	211	finally
        //   49	56	215	finally
        //   42	49	219	finally
        //   35	42	223	finally
        //   29	35	227	finally
        //   10	29	231	finally
      }
      
      public void ipSecAddSecurityPolicy(int paramInt1, int paramInt2, String paramString1, String paramString2, int paramInt3, int paramInt4, int paramInt5)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          localParcel1.writeInt(paramInt5);
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
      
      public int ipSecAllocateSpi(int paramInt1, String paramString1, String paramString2, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(23, localParcel1, localParcel2, 0);
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
      
      public void ipSecApplyTransportModeTransform(FileDescriptor paramFileDescriptor, int paramInt1, int paramInt2, String paramString1, String paramString2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeRawFileDescriptor(paramFileDescriptor);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt3);
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
      
      public void ipSecDeleteSecurityAssociation(int paramInt1, String paramString1, String paramString2, int paramInt2, int paramInt3, int paramInt4)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
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
      
      public void ipSecDeleteSecurityPolicy(int paramInt1, int paramInt2, String paramString1, String paramString2, int paramInt3, int paramInt4)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          mRemote.transact(30, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void ipSecRemoveTransportModeTransform(FileDescriptor paramFileDescriptor)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeRawFileDescriptor(paramFileDescriptor);
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
      
      public void ipSecSetEncapSocketOwner(FileDescriptor paramFileDescriptor, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeRawFileDescriptor(paramFileDescriptor);
          localParcel1.writeInt(paramInt);
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
      
      public void ipSecUpdateSecurityPolicy(int paramInt1, int paramInt2, String paramString1, String paramString2, int paramInt3, int paramInt4, int paramInt5)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          localParcel1.writeInt(paramInt5);
          mRemote.transact(29, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isAlive()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(1, localParcel1, localParcel2, 0);
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
      
      public void networkAddInterface(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
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
      
      public void networkAddUidRanges(int paramInt, UidRange[] paramArrayOfUidRange)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeInt(paramInt);
          localParcel1.writeTypedArray(paramArrayOfUidRange, 0);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void networkCreatePhysical(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void networkCreateVpn(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeInt(paramBoolean2);
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
      
      public void networkDestroy(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
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
      
      public void networkRejectNonSecureVpn(boolean paramBoolean, UidRange[] paramArrayOfUidRange)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeTypedArray(paramArrayOfUidRange, 0);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void networkRemoveInterface(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
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
      
      public void networkRemoveUidRanges(int paramInt, UidRange[] paramArrayOfUidRange)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeInt(paramInt);
          localParcel1.writeTypedArray(paramArrayOfUidRange, 0);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeVirtualTunnelInterface(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeString(paramString);
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
      
      public void setIPv6AddrGenMode(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
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
      
      public void setMetricsReportingLevel(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeInt(paramInt);
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
      
      public void setProcSysNet(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setResolverConfiguration(int paramInt, String[] paramArrayOfString1, String[] paramArrayOfString2, int[] paramArrayOfInt, String paramString, String[] paramArrayOfString3, String[] paramArrayOfString4)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeInt(paramInt);
          localParcel1.writeStringArray(paramArrayOfString1);
          localParcel1.writeStringArray(paramArrayOfString2);
          localParcel1.writeIntArray(paramArrayOfInt);
          localParcel1.writeString(paramString);
          localParcel1.writeStringArray(paramArrayOfString3);
          localParcel1.writeStringArray(paramArrayOfString4);
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void socketDestroy(UidRange[] paramArrayOfUidRange, int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeTypedArray(paramArrayOfUidRange, 0);
          localParcel1.writeIntArray(paramArrayOfInt);
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean tetherApplyDnsInterfaces()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(15, localParcel1, localParcel2, 0);
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
      
      public PersistableBundle tetherGetStats()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          PersistableBundle localPersistableBundle;
          if (localParcel2.readInt() != 0) {
            localPersistableBundle = (PersistableBundle)PersistableBundle.CREATOR.createFromParcel(localParcel2);
          } else {
            localPersistableBundle = null;
          }
          return localPersistableBundle;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean trafficCheckBpfStatsEnable()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(37, localParcel1, localParcel2, 0);
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
      
      public void updateVirtualTunnelInterface(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void wakeupAddInterface(String paramString1, String paramString2, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void wakeupDelInterface(String paramString1, String paramString2, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetd");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
    }
  }
}
