package android.print;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IPrintJobStateChangeListener
  extends IInterface
{
  public abstract void onPrintJobStateChanged(PrintJobId paramPrintJobId)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPrintJobStateChangeListener
  {
    private static final String DESCRIPTOR = "android.print.IPrintJobStateChangeListener";
    static final int TRANSACTION_onPrintJobStateChanged = 1;
    
    public Stub()
    {
      attachInterface(this, "android.print.IPrintJobStateChangeListener");
    }
    
    public static IPrintJobStateChangeListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.print.IPrintJobStateChangeListener");
      if ((localIInterface != null) && ((localIInterface instanceof IPrintJobStateChangeListener))) {
        return (IPrintJobStateChangeListener)localIInterface;
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
        paramParcel2.writeString("android.print.IPrintJobStateChangeListener");
        return true;
      }
      paramParcel1.enforceInterface("android.print.IPrintJobStateChangeListener");
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (PrintJobId)PrintJobId.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      onPrintJobStateChanged(paramParcel1);
      return true;
    }
    
    private static class Proxy
      implements IPrintJobStateChangeListener
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
        return "android.print.IPrintJobStateChangeListener";
      }
      
      public void onPrintJobStateChanged(PrintJobId paramPrintJobId)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.print.IPrintJobStateChangeListener");
          if (paramPrintJobId != null)
          {
            localParcel.writeInt(1);
            paramPrintJobId.writeToParcel(localParcel, 0);
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
    }
  }
}
