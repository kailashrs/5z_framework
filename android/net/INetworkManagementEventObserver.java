package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface INetworkManagementEventObserver
  extends IInterface
{
  public abstract void addressRemoved(String paramString, LinkAddress paramLinkAddress)
    throws RemoteException;
  
  public abstract void addressUpdated(String paramString, LinkAddress paramLinkAddress)
    throws RemoteException;
  
  public abstract void interfaceAdded(String paramString)
    throws RemoteException;
  
  public abstract void interfaceClassDataActivityChanged(String paramString, boolean paramBoolean, long paramLong)
    throws RemoteException;
  
  public abstract void interfaceDnsServerInfo(String paramString, long paramLong, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void interfaceLinkStateChanged(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void interfaceRemoved(String paramString)
    throws RemoteException;
  
  public abstract void interfaceStatusChanged(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void limitReached(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void routeRemoved(RouteInfo paramRouteInfo)
    throws RemoteException;
  
  public abstract void routeUpdated(RouteInfo paramRouteInfo)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements INetworkManagementEventObserver
  {
    private static final String DESCRIPTOR = "android.net.INetworkManagementEventObserver";
    static final int TRANSACTION_addressRemoved = 6;
    static final int TRANSACTION_addressUpdated = 5;
    static final int TRANSACTION_interfaceAdded = 3;
    static final int TRANSACTION_interfaceClassDataActivityChanged = 8;
    static final int TRANSACTION_interfaceDnsServerInfo = 9;
    static final int TRANSACTION_interfaceLinkStateChanged = 2;
    static final int TRANSACTION_interfaceRemoved = 4;
    static final int TRANSACTION_interfaceStatusChanged = 1;
    static final int TRANSACTION_limitReached = 7;
    static final int TRANSACTION_routeRemoved = 11;
    static final int TRANSACTION_routeUpdated = 10;
    
    public Stub()
    {
      attachInterface(this, "android.net.INetworkManagementEventObserver");
    }
    
    public static INetworkManagementEventObserver asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.INetworkManagementEventObserver");
      if ((localIInterface != null) && ((localIInterface instanceof INetworkManagementEventObserver))) {
        return (INetworkManagementEventObserver)localIInterface;
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
        Object localObject1 = null;
        String str1 = null;
        String str2 = null;
        Object localObject2 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 11: 
          paramParcel1.enforceInterface("android.net.INetworkManagementEventObserver");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (RouteInfo)RouteInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          routeRemoved(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.net.INetworkManagementEventObserver");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (RouteInfo)RouteInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          routeUpdated(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.net.INetworkManagementEventObserver");
          interfaceDnsServerInfo(paramParcel1.readString(), paramParcel1.readLong(), paramParcel1.createStringArray());
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.net.INetworkManagementEventObserver");
          str1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          interfaceClassDataActivityChanged(str1, bool3, paramParcel1.readLong());
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.net.INetworkManagementEventObserver");
          limitReached(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.net.INetworkManagementEventObserver");
          str2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (LinkAddress)LinkAddress.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str1;
          }
          addressRemoved(str2, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.net.INetworkManagementEventObserver");
          str1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (LinkAddress)LinkAddress.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str2;
          }
          addressUpdated(str1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.net.INetworkManagementEventObserver");
          interfaceRemoved(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.net.INetworkManagementEventObserver");
          interfaceAdded(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.net.INetworkManagementEventObserver");
          str1 = paramParcel1.readString();
          bool3 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          interfaceLinkStateChanged(str1, bool3);
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.net.INetworkManagementEventObserver");
        str1 = paramParcel1.readString();
        bool3 = bool2;
        if (paramParcel1.readInt() != 0) {
          bool3 = true;
        }
        interfaceStatusChanged(str1, bool3);
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.net.INetworkManagementEventObserver");
      return true;
    }
    
    private static class Proxy
      implements INetworkManagementEventObserver
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addressRemoved(String paramString, LinkAddress paramLinkAddress)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkManagementEventObserver");
          localParcel1.writeString(paramString);
          if (paramLinkAddress != null)
          {
            localParcel1.writeInt(1);
            paramLinkAddress.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void addressUpdated(String paramString, LinkAddress paramLinkAddress)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkManagementEventObserver");
          localParcel1.writeString(paramString);
          if (paramLinkAddress != null)
          {
            localParcel1.writeInt(1);
            paramLinkAddress.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.net.INetworkManagementEventObserver";
      }
      
      public void interfaceAdded(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkManagementEventObserver");
          localParcel1.writeString(paramString);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void interfaceClassDataActivityChanged(String paramString, boolean paramBoolean, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkManagementEventObserver");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeLong(paramLong);
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
      
      public void interfaceDnsServerInfo(String paramString, long paramLong, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkManagementEventObserver");
          localParcel1.writeString(paramString);
          localParcel1.writeLong(paramLong);
          localParcel1.writeStringArray(paramArrayOfString);
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
      
      public void interfaceLinkStateChanged(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkManagementEventObserver");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
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
      
      public void interfaceRemoved(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkManagementEventObserver");
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
      
      public void interfaceStatusChanged(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkManagementEventObserver");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
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
      
      public void limitReached(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkManagementEventObserver");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
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
      
      public void routeRemoved(RouteInfo paramRouteInfo)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkManagementEventObserver");
          if (paramRouteInfo != null)
          {
            localParcel1.writeInt(1);
            paramRouteInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void routeUpdated(RouteInfo paramRouteInfo)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.INetworkManagementEventObserver");
          if (paramRouteInfo != null)
          {
            localParcel1.writeInt(1);
            paramRouteInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
    }
  }
}
