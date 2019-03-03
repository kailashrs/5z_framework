package android.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface ICountryDetector
  extends IInterface
{
  public abstract void addCountryListener(ICountryListener paramICountryListener)
    throws RemoteException;
  
  public abstract Country detectCountry()
    throws RemoteException;
  
  public abstract void removeCountryListener(ICountryListener paramICountryListener)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements ICountryDetector
  {
    private static final String DESCRIPTOR = "android.location.ICountryDetector";
    static final int TRANSACTION_addCountryListener = 2;
    static final int TRANSACTION_detectCountry = 1;
    static final int TRANSACTION_removeCountryListener = 3;
    
    public Stub()
    {
      attachInterface(this, "android.location.ICountryDetector");
    }
    
    public static ICountryDetector asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.location.ICountryDetector");
      if ((localIInterface != null) && ((localIInterface instanceof ICountryDetector))) {
        return (ICountryDetector)localIInterface;
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
          paramParcel1.enforceInterface("android.location.ICountryDetector");
          removeCountryListener(ICountryListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.location.ICountryDetector");
          addCountryListener(ICountryListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.location.ICountryDetector");
        paramParcel1 = detectCountry();
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
      paramParcel2.writeString("android.location.ICountryDetector");
      return true;
    }
    
    private static class Proxy
      implements ICountryDetector
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addCountryListener(ICountryListener paramICountryListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ICountryDetector");
          if (paramICountryListener != null) {
            paramICountryListener = paramICountryListener.asBinder();
          } else {
            paramICountryListener = null;
          }
          localParcel1.writeStrongBinder(paramICountryListener);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public Country detectCountry()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ICountryDetector");
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Country localCountry;
          if (localParcel2.readInt() != 0) {
            localCountry = (Country)Country.CREATOR.createFromParcel(localParcel2);
          } else {
            localCountry = null;
          }
          return localCountry;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.location.ICountryDetector";
      }
      
      public void removeCountryListener(ICountryListener paramICountryListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.location.ICountryDetector");
          if (paramICountryListener != null) {
            paramICountryListener = paramICountryListener.asBinder();
          } else {
            paramICountryListener = null;
          }
          localParcel1.writeStrongBinder(paramICountryListener);
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
    }
  }
}
