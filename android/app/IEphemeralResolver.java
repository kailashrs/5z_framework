package android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.IRemoteCallback;
import android.os.IRemoteCallback.Stub;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IEphemeralResolver
  extends IInterface
{
  public abstract void getEphemeralIntentFilterList(IRemoteCallback paramIRemoteCallback, String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void getEphemeralResolveInfoList(IRemoteCallback paramIRemoteCallback, int[] paramArrayOfInt, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IEphemeralResolver
  {
    private static final String DESCRIPTOR = "android.app.IEphemeralResolver";
    static final int TRANSACTION_getEphemeralIntentFilterList = 2;
    static final int TRANSACTION_getEphemeralResolveInfoList = 1;
    
    public Stub()
    {
      attachInterface(this, "android.app.IEphemeralResolver");
    }
    
    public static IEphemeralResolver asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.IEphemeralResolver");
      if ((localIInterface != null) && ((localIInterface instanceof IEphemeralResolver))) {
        return (IEphemeralResolver)localIInterface;
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
          paramParcel1.enforceInterface("android.app.IEphemeralResolver");
          getEphemeralIntentFilterList(IRemoteCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString(), paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.app.IEphemeralResolver");
        getEphemeralResolveInfoList(IRemoteCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.createIntArray(), paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.app.IEphemeralResolver");
      return true;
    }
    
    private static class Proxy
      implements IEphemeralResolver
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
      
      public void getEphemeralIntentFilterList(IRemoteCallback paramIRemoteCallback, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IEphemeralResolver");
          if (paramIRemoteCallback != null) {
            paramIRemoteCallback = paramIRemoteCallback.asBinder();
          } else {
            paramIRemoteCallback = null;
          }
          localParcel.writeStrongBinder(paramIRemoteCallback);
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void getEphemeralResolveInfoList(IRemoteCallback paramIRemoteCallback, int[] paramArrayOfInt, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.IEphemeralResolver");
          if (paramIRemoteCallback != null) {
            paramIRemoteCallback = paramIRemoteCallback.asBinder();
          } else {
            paramIRemoteCallback = null;
          }
          localParcel.writeStrongBinder(paramIRemoteCallback);
          localParcel.writeIntArray(paramArrayOfInt);
          localParcel.writeInt(paramInt);
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
        return "android.app.IEphemeralResolver";
      }
    }
  }
}
