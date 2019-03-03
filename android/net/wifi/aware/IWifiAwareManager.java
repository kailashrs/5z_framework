package android.net.wifi.aware;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.List;

public abstract interface IWifiAwareManager
  extends IInterface
{
  public abstract void connect(IBinder paramIBinder, String paramString, IWifiAwareEventCallback paramIWifiAwareEventCallback, ConfigRequest paramConfigRequest, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void disconnect(int paramInt, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract Characteristics getCharacteristics()
    throws RemoteException;
  
  public abstract boolean isUsageEnabled()
    throws RemoteException;
  
  public abstract void publish(String paramString, int paramInt, PublishConfig paramPublishConfig, IWifiAwareDiscoverySessionCallback paramIWifiAwareDiscoverySessionCallback)
    throws RemoteException;
  
  public abstract void requestMacAddresses(int paramInt, List paramList, IWifiAwareMacAddressProvider paramIWifiAwareMacAddressProvider)
    throws RemoteException;
  
  public abstract void sendMessage(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte, int paramInt4, int paramInt5)
    throws RemoteException;
  
  public abstract void subscribe(String paramString, int paramInt, SubscribeConfig paramSubscribeConfig, IWifiAwareDiscoverySessionCallback paramIWifiAwareDiscoverySessionCallback)
    throws RemoteException;
  
  public abstract void terminateSession(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void updatePublish(int paramInt1, int paramInt2, PublishConfig paramPublishConfig)
    throws RemoteException;
  
  public abstract void updateSubscribe(int paramInt1, int paramInt2, SubscribeConfig paramSubscribeConfig)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IWifiAwareManager
  {
    private static final String DESCRIPTOR = "android.net.wifi.aware.IWifiAwareManager";
    static final int TRANSACTION_connect = 3;
    static final int TRANSACTION_disconnect = 4;
    static final int TRANSACTION_getCharacteristics = 2;
    static final int TRANSACTION_isUsageEnabled = 1;
    static final int TRANSACTION_publish = 5;
    static final int TRANSACTION_requestMacAddresses = 11;
    static final int TRANSACTION_sendMessage = 9;
    static final int TRANSACTION_subscribe = 6;
    static final int TRANSACTION_terminateSession = 10;
    static final int TRANSACTION_updatePublish = 7;
    static final int TRANSACTION_updateSubscribe = 8;
    
    public Stub()
    {
      attachInterface(this, "android.net.wifi.aware.IWifiAwareManager");
    }
    
    public static IWifiAwareManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.wifi.aware.IWifiAwareManager");
      if ((localIInterface != null) && ((localIInterface instanceof IWifiAwareManager))) {
        return (IWifiAwareManager)localIInterface;
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
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        String str = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 11: 
          paramParcel1.enforceInterface("android.net.wifi.aware.IWifiAwareManager");
          requestMacAddresses(paramParcel1.readInt(), paramParcel1.readArrayList(getClass().getClassLoader()), IWifiAwareMacAddressProvider.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.net.wifi.aware.IWifiAwareManager");
          terminateSession(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.net.wifi.aware.IWifiAwareManager");
          sendMessage(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.createByteArray(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.net.wifi.aware.IWifiAwareManager");
          paramInt2 = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (SubscribeConfig)SubscribeConfig.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str;
          }
          updateSubscribe(paramInt2, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.net.wifi.aware.IWifiAwareManager");
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PublishConfig)PublishConfig.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          updatePublish(paramInt1, paramInt2, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.net.wifi.aware.IWifiAwareManager");
          localObject3 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject4 = (SubscribeConfig)SubscribeConfig.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject4 = localObject2;
          }
          subscribe((String)localObject3, paramInt1, (SubscribeConfig)localObject4, IWifiAwareDiscoverySessionCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.net.wifi.aware.IWifiAwareManager");
          localObject2 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject4 = (PublishConfig)PublishConfig.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject4 = localObject3;
          }
          publish((String)localObject2, paramInt1, (PublishConfig)localObject4, IWifiAwareDiscoverySessionCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.net.wifi.aware.IWifiAwareManager");
          disconnect(paramParcel1.readInt(), paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.net.wifi.aware.IWifiAwareManager");
          localObject3 = paramParcel1.readStrongBinder();
          str = paramParcel1.readString();
          localObject2 = IWifiAwareEventCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            localObject4 = (ConfigRequest)ConfigRequest.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          boolean bool;
          if (paramParcel1.readInt() != 0) {
            bool = true;
          } else {
            bool = false;
          }
          connect((IBinder)localObject3, str, (IWifiAwareEventCallback)localObject2, (ConfigRequest)localObject4, bool);
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.net.wifi.aware.IWifiAwareManager");
          paramParcel1 = getCharacteristics();
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
        paramParcel1.enforceInterface("android.net.wifi.aware.IWifiAwareManager");
        paramInt1 = isUsageEnabled();
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.net.wifi.aware.IWifiAwareManager");
      return true;
    }
    
    private static class Proxy
      implements IWifiAwareManager
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
      
      public void connect(IBinder paramIBinder, String paramString, IWifiAwareEventCallback paramIWifiAwareEventCallback, ConfigRequest paramConfigRequest, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.aware.IWifiAwareManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString);
          if (paramIWifiAwareEventCallback != null) {
            paramIBinder = paramIWifiAwareEventCallback.asBinder();
          } else {
            paramIBinder = null;
          }
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramConfigRequest != null)
          {
            localParcel1.writeInt(1);
            paramConfigRequest.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
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
      
      public void disconnect(int paramInt, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.aware.IWifiAwareManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeStrongBinder(paramIBinder);
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
      
      public Characteristics getCharacteristics()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.aware.IWifiAwareManager");
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Characteristics localCharacteristics;
          if (localParcel2.readInt() != 0) {
            localCharacteristics = (Characteristics)Characteristics.CREATOR.createFromParcel(localParcel2);
          } else {
            localCharacteristics = null;
          }
          return localCharacteristics;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.net.wifi.aware.IWifiAwareManager";
      }
      
      public boolean isUsageEnabled()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.aware.IWifiAwareManager");
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
      
      public void publish(String paramString, int paramInt, PublishConfig paramPublishConfig, IWifiAwareDiscoverySessionCallback paramIWifiAwareDiscoverySessionCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.aware.IWifiAwareManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          if (paramPublishConfig != null)
          {
            localParcel1.writeInt(1);
            paramPublishConfig.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIWifiAwareDiscoverySessionCallback != null) {
            paramString = paramIWifiAwareDiscoverySessionCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
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
      
      public void requestMacAddresses(int paramInt, List paramList, IWifiAwareMacAddressProvider paramIWifiAwareMacAddressProvider)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.aware.IWifiAwareManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeList(paramList);
          if (paramIWifiAwareMacAddressProvider != null) {
            paramList = paramIWifiAwareMacAddressProvider.asBinder();
          } else {
            paramList = null;
          }
          localParcel1.writeStrongBinder(paramList);
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
      
      public void sendMessage(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte, int paramInt4, int paramInt5)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.aware.IWifiAwareManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeInt(paramInt4);
          localParcel1.writeInt(paramInt5);
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
      
      public void subscribe(String paramString, int paramInt, SubscribeConfig paramSubscribeConfig, IWifiAwareDiscoverySessionCallback paramIWifiAwareDiscoverySessionCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.aware.IWifiAwareManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          if (paramSubscribeConfig != null)
          {
            localParcel1.writeInt(1);
            paramSubscribeConfig.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIWifiAwareDiscoverySessionCallback != null) {
            paramString = paramIWifiAwareDiscoverySessionCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
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
      
      public void terminateSession(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.aware.IWifiAwareManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void updatePublish(int paramInt1, int paramInt2, PublishConfig paramPublishConfig)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.aware.IWifiAwareManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          if (paramPublishConfig != null)
          {
            localParcel1.writeInt(1);
            paramPublishConfig.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void updateSubscribe(int paramInt1, int paramInt2, SubscribeConfig paramSubscribeConfig)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.aware.IWifiAwareManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          if (paramSubscribeConfig != null)
          {
            localParcel1.writeInt(1);
            paramSubscribeConfig.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
    }
  }
}
