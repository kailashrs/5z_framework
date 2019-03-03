package android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IAlarmListener
  extends IInterface
{
  public abstract void doAlarm(IAlarmCompleteListener paramIAlarmCompleteListener)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAlarmListener
  {
    private static final String DESCRIPTOR = "android.app.IAlarmListener";
    static final int TRANSACTION_doAlarm = 1;
    
    public Stub()
    {
      attachInterface(this, "android.app.IAlarmListener");
    }
    
    public static IAlarmListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.IAlarmListener");
      if ((localIInterface != null) && ((localIInterface instanceof IAlarmListener))) {
        return (IAlarmListener)localIInterface;
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
        paramParcel2.writeString("android.app.IAlarmListener");
        return true;
      }
      paramParcel1.enforceInterface("android.app.IAlarmListener");
      doAlarm(IAlarmCompleteListener.Stub.asInterface(paramParcel1.readStrongBinder()));
      return true;
    }
    
    private static class Proxy
      implements IAlarmListener
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
      
      public void doAlarm(IAlarmCompleteListener paramIAlarmCompleteListener)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IAlarmListener");
          if (paramIAlarmCompleteListener != null) {
            paramIAlarmCompleteListener = paramIAlarmCompleteListener.asBinder();
          } else {
            paramIAlarmCompleteListener = null;
          }
          localParcel.writeStrongBinder(paramIAlarmCompleteListener);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.app.IAlarmListener";
      }
    }
  }
}
