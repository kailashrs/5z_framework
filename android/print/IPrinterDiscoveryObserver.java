package android.print;

import android.content.pm.ParceledListSlice;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.RemoteException;

public abstract interface IPrinterDiscoveryObserver
  extends IInterface
{
  public abstract void onPrintersAdded(ParceledListSlice paramParceledListSlice)
    throws RemoteException;
  
  public abstract void onPrintersRemoved(ParceledListSlice paramParceledListSlice)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPrinterDiscoveryObserver
  {
    private static final String DESCRIPTOR = "android.print.IPrinterDiscoveryObserver";
    static final int TRANSACTION_onPrintersAdded = 1;
    static final int TRANSACTION_onPrintersRemoved = 2;
    
    public Stub()
    {
      attachInterface(this, "android.print.IPrinterDiscoveryObserver");
    }
    
    public static IPrinterDiscoveryObserver asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.print.IPrinterDiscoveryObserver");
      if ((localIInterface != null) && ((localIInterface instanceof IPrinterDiscoveryObserver))) {
        return (IPrinterDiscoveryObserver)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 2: 
          paramParcel1.enforceInterface("android.print.IPrinterDiscoveryObserver");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          onPrintersRemoved(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.print.IPrinterDiscoveryObserver");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject1;
        }
        onPrintersAdded(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.print.IPrinterDiscoveryObserver");
      return true;
    }
    
    private static class Proxy
      implements IPrinterDiscoveryObserver
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
        return "android.print.IPrinterDiscoveryObserver";
      }
      
      public void onPrintersAdded(ParceledListSlice paramParceledListSlice)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrinterDiscoveryObserver");
          if (paramParceledListSlice != null)
          {
            localParcel.writeInt(1);
            paramParceledListSlice.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPrintersRemoved(ParceledListSlice paramParceledListSlice)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrinterDiscoveryObserver");
          if (paramParceledListSlice != null)
          {
            localParcel.writeInt(1);
            paramParceledListSlice.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(2, localParcel, null, 1);
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
