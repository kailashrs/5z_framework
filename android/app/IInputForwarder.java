package android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.view.InputEvent;

public abstract interface IInputForwarder
  extends IInterface
{
  public abstract boolean forwardEvent(InputEvent paramInputEvent)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IInputForwarder
  {
    private static final String DESCRIPTOR = "android.app.IInputForwarder";
    static final int TRANSACTION_forwardEvent = 1;
    
    public Stub()
    {
      attachInterface(this, "android.app.IInputForwarder");
    }
    
    public static IInputForwarder asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.IInputForwarder");
      if ((localIInterface != null) && ((localIInterface instanceof IInputForwarder))) {
        return (IInputForwarder)localIInterface;
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
        paramParcel2.writeString("android.app.IInputForwarder");
        return true;
      }
      paramParcel1.enforceInterface("android.app.IInputForwarder");
      if (paramParcel1.readInt() != 0) {
        paramParcel1 = (InputEvent)InputEvent.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel1 = null;
      }
      paramInt1 = forwardEvent(paramParcel1);
      paramParcel2.writeNoException();
      paramParcel2.writeInt(paramInt1);
      return true;
    }
    
    private static class Proxy
      implements IInputForwarder
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
      
      public boolean forwardEvent(InputEvent paramInputEvent)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IInputForwarder");
          boolean bool = true;
          if (paramInputEvent != null)
          {
            localParcel1.writeInt(1);
            paramInputEvent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.app.IInputForwarder";
      }
    }
  }
}
