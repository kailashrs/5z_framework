package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IIpSecService
  extends IInterface
{
  public abstract void addAddressToTunnelInterface(int paramInt, LinkAddress paramLinkAddress, String paramString)
    throws RemoteException;
  
  public abstract IpSecSpiResponse allocateSecurityParameterIndex(String paramString, int paramInt, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void applyTransportModeTransform(ParcelFileDescriptor paramParcelFileDescriptor, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void applyTunnelModeTransform(int paramInt1, int paramInt2, int paramInt3, String paramString)
    throws RemoteException;
  
  public abstract void closeUdpEncapsulationSocket(int paramInt)
    throws RemoteException;
  
  public abstract IpSecTransformResponse createTransform(IpSecConfig paramIpSecConfig, IBinder paramIBinder, String paramString)
    throws RemoteException;
  
  public abstract IpSecTunnelInterfaceResponse createTunnelInterface(String paramString1, String paramString2, Network paramNetwork, IBinder paramIBinder, String paramString3)
    throws RemoteException;
  
  public abstract void deleteTransform(int paramInt)
    throws RemoteException;
  
  public abstract void deleteTunnelInterface(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract IpSecUdpEncapResponse openUdpEncapsulationSocket(int paramInt, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void releaseSecurityParameterIndex(int paramInt)
    throws RemoteException;
  
  public abstract void removeAddressFromTunnelInterface(int paramInt, LinkAddress paramLinkAddress, String paramString)
    throws RemoteException;
  
  public abstract void removeTransportModeTransforms(ParcelFileDescriptor paramParcelFileDescriptor)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IIpSecService
  {
    private static final String DESCRIPTOR = "android.net.IIpSecService";
    static final int TRANSACTION_addAddressToTunnelInterface = 6;
    static final int TRANSACTION_allocateSecurityParameterIndex = 1;
    static final int TRANSACTION_applyTransportModeTransform = 11;
    static final int TRANSACTION_applyTunnelModeTransform = 12;
    static final int TRANSACTION_closeUdpEncapsulationSocket = 4;
    static final int TRANSACTION_createTransform = 9;
    static final int TRANSACTION_createTunnelInterface = 5;
    static final int TRANSACTION_deleteTransform = 10;
    static final int TRANSACTION_deleteTunnelInterface = 8;
    static final int TRANSACTION_openUdpEncapsulationSocket = 3;
    static final int TRANSACTION_releaseSecurityParameterIndex = 2;
    static final int TRANSACTION_removeAddressFromTunnelInterface = 7;
    static final int TRANSACTION_removeTransportModeTransforms = 13;
    
    public Stub()
    {
      attachInterface(this, "android.net.IIpSecService");
    }
    
    public static IIpSecService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.IIpSecService");
      if ((localIInterface != null) && ((localIInterface instanceof IIpSecService))) {
        return (IIpSecService)localIInterface;
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
        String str1 = null;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        String str2 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 13: 
          paramParcel1.enforceInterface("android.net.IIpSecService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str2;
          }
          removeTransportModeTransforms(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.net.IIpSecService");
          applyTunnelModeTransform(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.net.IIpSecService");
          if (paramParcel1.readInt() != 0) {
            localObject4 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject4 = str1;
          }
          applyTransportModeTransform((ParcelFileDescriptor)localObject4, paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.net.IIpSecService");
          deleteTransform(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.net.IIpSecService");
          if (paramParcel1.readInt() != 0) {
            localObject4 = (IpSecConfig)IpSecConfig.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject4 = localObject1;
          }
          paramParcel1 = createTransform((IpSecConfig)localObject4, paramParcel1.readStrongBinder(), paramParcel1.readString());
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
        case 8: 
          paramParcel1.enforceInterface("android.net.IIpSecService");
          deleteTunnelInterface(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.net.IIpSecService");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject4 = (LinkAddress)LinkAddress.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject4 = localObject2;
          }
          removeAddressFromTunnelInterface(paramInt1, (LinkAddress)localObject4, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.net.IIpSecService");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject4 = (LinkAddress)LinkAddress.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject4 = localObject3;
          }
          addAddressToTunnelInterface(paramInt1, (LinkAddress)localObject4, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.net.IIpSecService");
          str2 = paramParcel1.readString();
          str1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject4 = (Network)Network.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          paramParcel1 = createTunnelInterface(str2, str1, (Network)localObject4, paramParcel1.readStrongBinder(), paramParcel1.readString());
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
        case 4: 
          paramParcel1.enforceInterface("android.net.IIpSecService");
          closeUdpEncapsulationSocket(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.net.IIpSecService");
          paramParcel1 = openUdpEncapsulationSocket(paramParcel1.readInt(), paramParcel1.readStrongBinder());
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
        case 2: 
          paramParcel1.enforceInterface("android.net.IIpSecService");
          releaseSecurityParameterIndex(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.net.IIpSecService");
        paramParcel1 = allocateSecurityParameterIndex(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readStrongBinder());
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
      }
      paramParcel2.writeString("android.net.IIpSecService");
      return true;
    }
    
    private static class Proxy
      implements IIpSecService
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addAddressToTunnelInterface(int paramInt, LinkAddress paramLinkAddress, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IIpSecService");
          localParcel1.writeInt(paramInt);
          if (paramLinkAddress != null)
          {
            localParcel1.writeInt(1);
            paramLinkAddress.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
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
      
      public IpSecSpiResponse allocateSecurityParameterIndex(String paramString, int paramInt, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IIpSecService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (IpSecSpiResponse)IpSecSpiResponse.CREATOR.createFromParcel(localParcel2);
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
      
      public void applyTransportModeTransform(ParcelFileDescriptor paramParcelFileDescriptor, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IIpSecService");
          if (paramParcelFileDescriptor != null)
          {
            localParcel1.writeInt(1);
            paramParcelFileDescriptor.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void applyTunnelModeTransform(int paramInt1, int paramInt2, int paramInt3, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IIpSecService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeString(paramString);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void closeUdpEncapsulationSocket(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IIpSecService");
          localParcel1.writeInt(paramInt);
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
      
      public IpSecTransformResponse createTransform(IpSecConfig paramIpSecConfig, IBinder paramIBinder, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IIpSecService");
          if (paramIpSecConfig != null)
          {
            localParcel1.writeInt(1);
            paramIpSecConfig.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIpSecConfig = (IpSecTransformResponse)IpSecTransformResponse.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIpSecConfig = null;
          }
          return paramIpSecConfig;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IpSecTunnelInterfaceResponse createTunnelInterface(String paramString1, String paramString2, Network paramNetwork, IBinder paramIBinder, String paramString3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IIpSecService");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          if (paramNetwork != null)
          {
            localParcel1.writeInt(1);
            paramNetwork.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString3);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString1 = (IpSecTunnelInterfaceResponse)IpSecTunnelInterfaceResponse.CREATOR.createFromParcel(localParcel2);
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
      
      public void deleteTransform(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IIpSecService");
          localParcel1.writeInt(paramInt);
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
      
      public void deleteTunnelInterface(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IIpSecService");
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
      
      public String getInterfaceDescriptor()
      {
        return "android.net.IIpSecService";
      }
      
      public IpSecUdpEncapResponse openUdpEncapsulationSocket(int paramInt, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IIpSecService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIBinder = (IpSecUdpEncapResponse)IpSecUdpEncapResponse.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIBinder = null;
          }
          return paramIBinder;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void releaseSecurityParameterIndex(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IIpSecService");
          localParcel1.writeInt(paramInt);
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
      
      public void removeAddressFromTunnelInterface(int paramInt, LinkAddress paramLinkAddress, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IIpSecService");
          localParcel1.writeInt(paramInt);
          if (paramLinkAddress != null)
          {
            localParcel1.writeInt(1);
            paramLinkAddress.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void removeTransportModeTransforms(ParcelFileDescriptor paramParcelFileDescriptor)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IIpSecService");
          if (paramParcelFileDescriptor != null)
          {
            localParcel1.writeInt(1);
            paramParcelFileDescriptor.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
    }
  }
}
