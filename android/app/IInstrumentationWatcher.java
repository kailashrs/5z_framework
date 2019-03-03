package android.app;

import android.content.ComponentName;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IInstrumentationWatcher
  extends IInterface
{
  public abstract void instrumentationFinished(ComponentName paramComponentName, int paramInt, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void instrumentationStatus(ComponentName paramComponentName, int paramInt, Bundle paramBundle)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IInstrumentationWatcher
  {
    private static final String DESCRIPTOR = "android.app.IInstrumentationWatcher";
    static final int TRANSACTION_instrumentationFinished = 2;
    static final int TRANSACTION_instrumentationStatus = 1;
    
    public Stub()
    {
      attachInterface(this, "android.app.IInstrumentationWatcher");
    }
    
    public static IInstrumentationWatcher asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.IInstrumentationWatcher");
      if ((localIInterface != null) && ((localIInterface instanceof IInstrumentationWatcher))) {
        return (IInstrumentationWatcher)localIInterface;
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
        ComponentName localComponentName;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 2: 
          paramParcel1.enforceInterface("android.app.IInstrumentationWatcher");
          if (paramParcel1.readInt() != 0) {
            localComponentName = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localComponentName = null;
          }
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          instrumentationFinished(localComponentName, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.app.IInstrumentationWatcher");
        if (paramParcel1.readInt() != 0) {
          localComponentName = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
        } else {
          localComponentName = null;
        }
        paramInt1 = paramParcel1.readInt();
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject1;
        }
        instrumentationStatus(localComponentName, paramInt1, paramParcel1);
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.app.IInstrumentationWatcher");
      return true;
    }
    
    private static class Proxy
      implements IInstrumentationWatcher
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
        return "android.app.IInstrumentationWatcher";
      }
      
      public void instrumentationFinished(ComponentName paramComponentName, int paramInt, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IInstrumentationWatcher");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void instrumentationStatus(ComponentName paramComponentName, int paramInt, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IInstrumentationWatcher");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(1, localParcel1, localParcel2, 0);
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
