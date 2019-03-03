package android.content.pm;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IPackageMoveObserver
  extends IInterface
{
  public abstract void onCreated(int paramInt, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void onStatusChanged(int paramInt1, int paramInt2, long paramLong)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPackageMoveObserver
  {
    private static final String DESCRIPTOR = "android.content.pm.IPackageMoveObserver";
    static final int TRANSACTION_onCreated = 1;
    static final int TRANSACTION_onStatusChanged = 2;
    
    public Stub()
    {
      attachInterface(this, "android.content.pm.IPackageMoveObserver");
    }
    
    public static IPackageMoveObserver asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.pm.IPackageMoveObserver");
      if ((localIInterface != null) && ((localIInterface instanceof IPackageMoveObserver))) {
        return (IPackageMoveObserver)localIInterface;
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
        case 2: 
          paramParcel1.enforceInterface("android.content.pm.IPackageMoveObserver");
          onStatusChanged(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readLong());
          return true;
        }
        paramParcel1.enforceInterface("android.content.pm.IPackageMoveObserver");
        paramInt1 = paramParcel1.readInt();
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = null;
        }
        onCreated(paramInt1, paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.content.pm.IPackageMoveObserver");
      return true;
    }
    
    private static class Proxy
      implements IPackageMoveObserver
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
        return "android.content.pm.IPackageMoveObserver";
      }
      
      public void onCreated(int paramInt, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.IPackageMoveObserver");
          localParcel.writeInt(paramInt);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
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
      
      public void onStatusChanged(int paramInt1, int paramInt2, long paramLong)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.IPackageMoveObserver");
          localParcel.writeInt(paramInt1);
          localParcel.writeInt(paramInt2);
          localParcel.writeLong(paramLong);
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
