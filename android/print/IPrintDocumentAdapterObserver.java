package android.print;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IPrintDocumentAdapterObserver
  extends IInterface
{
  public abstract void onDestroy()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPrintDocumentAdapterObserver
  {
    private static final String DESCRIPTOR = "android.print.IPrintDocumentAdapterObserver";
    static final int TRANSACTION_onDestroy = 1;
    
    public Stub()
    {
      attachInterface(this, "android.print.IPrintDocumentAdapterObserver");
    }
    
    public static IPrintDocumentAdapterObserver asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.print.IPrintDocumentAdapterObserver");
      if ((localIInterface != null) && ((localIInterface instanceof IPrintDocumentAdapterObserver))) {
        return (IPrintDocumentAdapterObserver)localIInterface;
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
        paramParcel2.writeString("android.print.IPrintDocumentAdapterObserver");
        return true;
      }
      paramParcel1.enforceInterface("android.print.IPrintDocumentAdapterObserver");
      onDestroy();
      return true;
    }
    
    private static class Proxy
      implements IPrintDocumentAdapterObserver
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
        return "android.print.IPrintDocumentAdapterObserver";
      }
      
      public void onDestroy()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintDocumentAdapterObserver");
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
