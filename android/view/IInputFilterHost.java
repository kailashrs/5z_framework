package android.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IInputFilterHost
  extends IInterface
{
  public abstract void sendInputEvent(InputEvent paramInputEvent, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IInputFilterHost
  {
    private static final String DESCRIPTOR = "android.view.IInputFilterHost";
    static final int TRANSACTION_sendInputEvent = 1;
    
    public Stub()
    {
      attachInterface(this, "android.view.IInputFilterHost");
    }
    
    public static IInputFilterHost asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.view.IInputFilterHost");
      if ((localIInterface != null) && ((localIInterface instanceof IInputFilterHost))) {
        return (IInputFilterHost)localIInterface;
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
        paramParcel2.writeString("android.view.IInputFilterHost");
        return true;
      }
      paramParcel1.enforceInterface("android.view.IInputFilterHost");
      if (paramParcel1.readInt() != 0) {
        paramParcel2 = (InputEvent)InputEvent.CREATOR.createFromParcel(paramParcel1);
      } else {
        paramParcel2 = null;
      }
      sendInputEvent(paramParcel2, paramParcel1.readInt());
      return true;
    }
    
    private static class Proxy
      implements IInputFilterHost
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
        return "android.view.IInputFilterHost";
      }
      
      public void sendInputEvent(InputEvent paramInputEvent, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IInputFilterHost");
          if (paramInputEvent != null)
          {
            localParcel.writeInt(1);
            paramInputEvent.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
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
