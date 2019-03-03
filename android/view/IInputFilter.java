package android.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IInputFilter
  extends IInterface
{
  public abstract void filterInputEvent(InputEvent paramInputEvent, int paramInt)
    throws RemoteException;
  
  public abstract void install(IInputFilterHost paramIInputFilterHost)
    throws RemoteException;
  
  public abstract void uninstall()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IInputFilter
  {
    private static final String DESCRIPTOR = "android.view.IInputFilter";
    static final int TRANSACTION_filterInputEvent = 3;
    static final int TRANSACTION_install = 1;
    static final int TRANSACTION_uninstall = 2;
    
    public Stub()
    {
      attachInterface(this, "android.view.IInputFilter");
    }
    
    public static IInputFilter asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.view.IInputFilter");
      if ((localIInterface != null) && ((localIInterface instanceof IInputFilter))) {
        return (IInputFilter)localIInterface;
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
          paramParcel1.enforceInterface("android.view.IInputFilter");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (InputEvent)InputEvent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          filterInputEvent(paramParcel2, paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.view.IInputFilter");
          uninstall();
          return true;
        }
        paramParcel1.enforceInterface("android.view.IInputFilter");
        install(IInputFilterHost.Stub.asInterface(paramParcel1.readStrongBinder()));
        return true;
      }
      paramParcel2.writeString("android.view.IInputFilter");
      return true;
    }
    
    private static class Proxy
      implements IInputFilter
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
      
      public void filterInputEvent(InputEvent paramInputEvent, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IInputFilter");
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
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.view.IInputFilter";
      }
      
      public void install(IInputFilterHost paramIInputFilterHost)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IInputFilter");
          if (paramIInputFilterHost != null) {
            paramIInputFilterHost = paramIInputFilterHost.asBinder();
          } else {
            paramIInputFilterHost = null;
          }
          localParcel.writeStrongBinder(paramIInputFilterHost);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void uninstall()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IInputFilter");
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
