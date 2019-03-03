package android.net.wifi.rtt;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.WorkSource;

public abstract interface IWifiRttManager
  extends IInterface
{
  public abstract void cancelRanging(WorkSource paramWorkSource)
    throws RemoteException;
  
  public abstract boolean isAvailable()
    throws RemoteException;
  
  public abstract void startRanging(IBinder paramIBinder, String paramString, WorkSource paramWorkSource, RangingRequest paramRangingRequest, IRttCallback paramIRttCallback)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IWifiRttManager
  {
    private static final String DESCRIPTOR = "android.net.wifi.rtt.IWifiRttManager";
    static final int TRANSACTION_cancelRanging = 3;
    static final int TRANSACTION_isAvailable = 1;
    static final int TRANSACTION_startRanging = 2;
    
    public Stub()
    {
      attachInterface(this, "android.net.wifi.rtt.IWifiRttManager");
    }
    
    public static IWifiRttManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.wifi.rtt.IWifiRttManager");
      if ((localIInterface != null) && ((localIInterface instanceof IWifiRttManager))) {
        return (IWifiRttManager)localIInterface;
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
        RangingRequest localRangingRequest = null;
        WorkSource localWorkSource = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 3: 
          paramParcel1.enforceInterface("android.net.wifi.rtt.IWifiRttManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localWorkSource;
          }
          cancelRanging(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.net.wifi.rtt.IWifiRttManager");
          IBinder localIBinder = paramParcel1.readStrongBinder();
          String str = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localWorkSource = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
          } else {
            localWorkSource = null;
          }
          if (paramParcel1.readInt() != 0) {
            localRangingRequest = (RangingRequest)RangingRequest.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          startRanging(localIBinder, str, localWorkSource, localRangingRequest, IRttCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.net.wifi.rtt.IWifiRttManager");
        paramInt1 = isAvailable();
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.net.wifi.rtt.IWifiRttManager");
      return true;
    }
    
    private static class Proxy
      implements IWifiRttManager
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
      
      public void cancelRanging(WorkSource paramWorkSource)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.rtt.IWifiRttManager");
          if (paramWorkSource != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public String getInterfaceDescriptor()
      {
        return "android.net.wifi.rtt.IWifiRttManager";
      }
      
      public boolean isAvailable()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.rtt.IWifiRttManager");
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
      
      public void startRanging(IBinder paramIBinder, String paramString, WorkSource paramWorkSource, RangingRequest paramRangingRequest, IRttCallback paramIRttCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.rtt.IWifiRttManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString);
          if (paramWorkSource != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramRangingRequest != null)
          {
            localParcel1.writeInt(1);
            paramRangingRequest.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIRttCallback != null) {
            paramIBinder = paramIRttCallback.asBinder();
          } else {
            paramIBinder = null;
          }
          localParcel1.writeStrongBinder(paramIBinder);
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
    }
  }
}
