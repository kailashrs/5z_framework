package android.content.pm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IOtaDexopt
  extends IInterface
{
  public abstract void cleanup()
    throws RemoteException;
  
  public abstract void dexoptNextPackage()
    throws RemoteException;
  
  public abstract float getProgress()
    throws RemoteException;
  
  public abstract boolean isDone()
    throws RemoteException;
  
  public abstract String nextDexoptCommand()
    throws RemoteException;
  
  public abstract void prepare()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IOtaDexopt
  {
    private static final String DESCRIPTOR = "android.content.pm.IOtaDexopt";
    static final int TRANSACTION_cleanup = 2;
    static final int TRANSACTION_dexoptNextPackage = 5;
    static final int TRANSACTION_getProgress = 4;
    static final int TRANSACTION_isDone = 3;
    static final int TRANSACTION_nextDexoptCommand = 6;
    static final int TRANSACTION_prepare = 1;
    
    public Stub()
    {
      attachInterface(this, "android.content.pm.IOtaDexopt");
    }
    
    public static IOtaDexopt asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.pm.IOtaDexopt");
      if ((localIInterface != null) && ((localIInterface instanceof IOtaDexopt))) {
        return (IOtaDexopt)localIInterface;
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
        case 6: 
          paramParcel1.enforceInterface("android.content.pm.IOtaDexopt");
          paramParcel1 = nextDexoptCommand();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.content.pm.IOtaDexopt");
          dexoptNextPackage();
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.content.pm.IOtaDexopt");
          float f = getProgress();
          paramParcel2.writeNoException();
          paramParcel2.writeFloat(f);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.content.pm.IOtaDexopt");
          paramInt1 = isDone();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.content.pm.IOtaDexopt");
          cleanup();
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.content.pm.IOtaDexopt");
        prepare();
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.content.pm.IOtaDexopt");
      return true;
    }
    
    private static class Proxy
      implements IOtaDexopt
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
      
      public void cleanup()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IOtaDexopt");
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
      
      public void dexoptNextPackage()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IOtaDexopt");
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.content.pm.IOtaDexopt";
      }
      
      public float getProgress()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IOtaDexopt");
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          float f = localParcel2.readFloat();
          return f;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isDone()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IOtaDexopt");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
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
      
      public String nextDexoptCommand()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IOtaDexopt");
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void prepare()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IOtaDexopt");
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
