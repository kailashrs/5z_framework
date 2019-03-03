package android.net.lowpan;

import android.net.IpPrefix;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface ILowpanInterfaceListener
  extends IInterface
{
  public abstract void onConnectedChanged(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onEnabledChanged(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onLinkAddressAdded(String paramString)
    throws RemoteException;
  
  public abstract void onLinkAddressRemoved(String paramString)
    throws RemoteException;
  
  public abstract void onLinkNetworkAdded(IpPrefix paramIpPrefix)
    throws RemoteException;
  
  public abstract void onLinkNetworkRemoved(IpPrefix paramIpPrefix)
    throws RemoteException;
  
  public abstract void onLowpanIdentityChanged(LowpanIdentity paramLowpanIdentity)
    throws RemoteException;
  
  public abstract void onReceiveFromCommissioner(byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void onRoleChanged(String paramString)
    throws RemoteException;
  
  public abstract void onStateChanged(String paramString)
    throws RemoteException;
  
  public abstract void onUpChanged(boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ILowpanInterfaceListener
  {
    private static final String DESCRIPTOR = "android.net.lowpan.ILowpanInterfaceListener";
    static final int TRANSACTION_onConnectedChanged = 2;
    static final int TRANSACTION_onEnabledChanged = 1;
    static final int TRANSACTION_onLinkAddressAdded = 9;
    static final int TRANSACTION_onLinkAddressRemoved = 10;
    static final int TRANSACTION_onLinkNetworkAdded = 7;
    static final int TRANSACTION_onLinkNetworkRemoved = 8;
    static final int TRANSACTION_onLowpanIdentityChanged = 6;
    static final int TRANSACTION_onReceiveFromCommissioner = 11;
    static final int TRANSACTION_onRoleChanged = 4;
    static final int TRANSACTION_onStateChanged = 5;
    static final int TRANSACTION_onUpChanged = 3;
    
    public Stub()
    {
      attachInterface(this, "android.net.lowpan.ILowpanInterfaceListener");
    }
    
    public static ILowpanInterfaceListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.lowpan.ILowpanInterfaceListener");
      if ((localIInterface != null) && ((localIInterface instanceof ILowpanInterfaceListener))) {
        return (ILowpanInterfaceListener)localIInterface;
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
        Object localObject2 = null;
        Object localObject3 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 11: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterfaceListener");
          onReceiveFromCommissioner(paramParcel1.createByteArray());
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterfaceListener");
          onLinkAddressRemoved(paramParcel1.readString());
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterfaceListener");
          onLinkAddressAdded(paramParcel1.readString());
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterfaceListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (IpPrefix)IpPrefix.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          onLinkNetworkRemoved(paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterfaceListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (IpPrefix)IpPrefix.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          onLinkNetworkAdded(paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterfaceListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (LowpanIdentity)LowpanIdentity.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          onLowpanIdentityChanged(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterfaceListener");
          onStateChanged(paramParcel1.readString());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterfaceListener");
          onRoleChanged(paramParcel1.readString());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterfaceListener");
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          onUpChanged(bool3);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterfaceListener");
          bool3 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          onConnectedChanged(bool3);
          return true;
        }
        paramParcel1.enforceInterface("android.net.lowpan.ILowpanInterfaceListener");
        bool3 = bool2;
        if (paramParcel1.readInt() != 0) {
          bool3 = true;
        }
        onEnabledChanged(bool3);
        return true;
      }
      paramParcel2.writeString("android.net.lowpan.ILowpanInterfaceListener");
      return true;
    }
    
    private static class Proxy
      implements ILowpanInterfaceListener
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.net.lowpan.ILowpanInterfaceListener";
      }
      
      public void onConnectedChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.lowpan.ILowpanInterfaceListener");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onEnabledChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.lowpan.ILowpanInterfaceListener");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onLinkAddressAdded(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.lowpan.ILowpanInterfaceListener");
          localParcel.writeString(paramString);
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onLinkAddressRemoved(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.lowpan.ILowpanInterfaceListener");
          localParcel.writeString(paramString);
          mRemote.transact(10, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onLinkNetworkAdded(IpPrefix paramIpPrefix)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.lowpan.ILowpanInterfaceListener");
          if (paramIpPrefix != null)
          {
            localParcel.writeInt(1);
            paramIpPrefix.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onLinkNetworkRemoved(IpPrefix paramIpPrefix)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.lowpan.ILowpanInterfaceListener");
          if (paramIpPrefix != null)
          {
            localParcel.writeInt(1);
            paramIpPrefix.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onLowpanIdentityChanged(LowpanIdentity paramLowpanIdentity)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.lowpan.ILowpanInterfaceListener");
          if (paramLowpanIdentity != null)
          {
            localParcel.writeInt(1);
            paramLowpanIdentity.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onReceiveFromCommissioner(byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.lowpan.ILowpanInterfaceListener");
          localParcel.writeByteArray(paramArrayOfByte);
          mRemote.transact(11, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onRoleChanged(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.lowpan.ILowpanInterfaceListener");
          localParcel.writeString(paramString);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onStateChanged(String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.lowpan.ILowpanInterfaceListener");
          localParcel.writeString(paramString);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onUpChanged(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.lowpan.ILowpanInterfaceListener");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(3, localParcel, null, 1);
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
