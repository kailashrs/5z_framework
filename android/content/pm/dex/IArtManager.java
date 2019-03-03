package android.content.pm.dex;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IArtManager
  extends IInterface
{
  public abstract boolean isRuntimeProfilingEnabled(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void snapshotRuntimeProfile(int paramInt, String paramString1, String paramString2, ISnapshotRuntimeProfileCallback paramISnapshotRuntimeProfileCallback, String paramString3)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IArtManager
  {
    private static final String DESCRIPTOR = "android.content.pm.dex.IArtManager";
    static final int TRANSACTION_isRuntimeProfilingEnabled = 2;
    static final int TRANSACTION_snapshotRuntimeProfile = 1;
    
    public Stub()
    {
      attachInterface(this, "android.content.pm.dex.IArtManager");
    }
    
    public static IArtManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.pm.dex.IArtManager");
      if ((localIInterface != null) && ((localIInterface instanceof IArtManager))) {
        return (IArtManager)localIInterface;
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
          paramParcel1.enforceInterface("android.content.pm.dex.IArtManager");
          paramInt1 = isRuntimeProfilingEnabled(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.content.pm.dex.IArtManager");
        snapshotRuntimeProfile(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString(), ISnapshotRuntimeProfileCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString());
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.content.pm.dex.IArtManager");
      return true;
    }
    
    private static class Proxy
      implements IArtManager
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
        return "android.content.pm.dex.IArtManager";
      }
      
      public boolean isRuntimeProfilingEnabled(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.dex.IArtManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void snapshotRuntimeProfile(int paramInt, String paramString1, String paramString2, ISnapshotRuntimeProfileCallback paramISnapshotRuntimeProfileCallback, String paramString3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.dex.IArtManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          if (paramISnapshotRuntimeProfileCallback != null) {
            paramString1 = paramISnapshotRuntimeProfileCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel1.writeStrongBinder(paramString1);
          localParcel1.writeString(paramString3);
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
