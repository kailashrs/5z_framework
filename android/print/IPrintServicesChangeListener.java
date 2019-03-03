package android.print;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IPrintServicesChangeListener
  extends IInterface
{
  public abstract void onPrintServicesChanged()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPrintServicesChangeListener
  {
    private static final String DESCRIPTOR = "android.print.IPrintServicesChangeListener";
    static final int TRANSACTION_onPrintServicesChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.print.IPrintServicesChangeListener");
    }
    
    public static IPrintServicesChangeListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.print.IPrintServicesChangeListener");
      if ((localIInterface != null) && ((localIInterface instanceof IPrintServicesChangeListener))) {
        return (IPrintServicesChangeListener)localIInterface;
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
      if (paramInt1 != 1)
      {
        if (paramInt1 != 1598968902) {
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        }
        paramParcel2.writeString("android.print.IPrintServicesChangeListener");
        return true;
      }
      paramParcel1.enforceInterface("android.print.IPrintServicesChangeListener");
      onPrintServicesChanged();
      return true;
    }
    
    private static class Proxy
      implements IPrintServicesChangeListener
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
        return "android.print.IPrintServicesChangeListener";
      }
      
      public void onPrintServicesChanged()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintServicesChangeListener");
          mRemote.transact(1, localParcel, null, 1);
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
