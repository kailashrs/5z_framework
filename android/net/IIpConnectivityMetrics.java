package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IIpConnectivityMetrics
  extends IInterface
{
  public abstract boolean addNetdEventCallback(int paramInt, INetdEventCallback paramINetdEventCallback)
    throws RemoteException;
  
  public abstract int logEvent(ConnectivityMetricsEvent paramConnectivityMetricsEvent)
    throws RemoteException;
  
  public abstract boolean removeNetdEventCallback(int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IIpConnectivityMetrics
  {
    private static final String DESCRIPTOR = "android.net.IIpConnectivityMetrics";
    static final int TRANSACTION_addNetdEventCallback = 2;
    static final int TRANSACTION_logEvent = 1;
    static final int TRANSACTION_removeNetdEventCallback = 3;
    
    public Stub()
    {
      attachInterface(this, "android.net.IIpConnectivityMetrics");
    }
    
    public static IIpConnectivityMetrics asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.IIpConnectivityMetrics");
      if ((localIInterface != null) && ((localIInterface instanceof IIpConnectivityMetrics))) {
        return (IIpConnectivityMetrics)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 3: 
          paramParcel1.enforceInterface("android.net.IIpConnectivityMetrics");
          paramInt1 = removeNetdEventCallback(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.net.IIpConnectivityMetrics");
          paramInt1 = addNetdEventCallback(paramParcel1.readInt(), INetdEventCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.net.IIpConnectivityMetrics");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (ConnectivityMetricsEvent)ConnectivityMetricsEvent.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = null;
        }
        paramInt1 = logEvent(paramParcel1);
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.net.IIpConnectivityMetrics");
      return true;
    }
    
    private static class Proxy
      implements IIpConnectivityMetrics
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public boolean addNetdEventCallback(int paramInt, INetdEventCallback paramINetdEventCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IIpConnectivityMetrics");
          localParcel1.writeInt(paramInt);
          if (paramINetdEventCallback != null) {
            paramINetdEventCallback = paramINetdEventCallback.asBinder();
          } else {
            paramINetdEventCallback = null;
          }
          localParcel1.writeStrongBinder(paramINetdEventCallback);
          paramINetdEventCallback = mRemote;
          boolean bool = false;
          paramINetdEventCallback.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.net.IIpConnectivityMetrics";
      }
      
      public int logEvent(ConnectivityMetricsEvent paramConnectivityMetricsEvent)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IIpConnectivityMetrics");
          if (paramConnectivityMetricsEvent != null)
          {
            localParcel1.writeInt(1);
            paramConnectivityMetricsEvent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(1, localParcel1, localParcel2, 0);
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
      
      public boolean removeNetdEventCallback(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IIpConnectivityMetrics");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
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
    }
  }
}
