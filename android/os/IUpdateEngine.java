package android.os;

public abstract interface IUpdateEngine
  extends IInterface
{
  public abstract void applyPayload(String paramString, long paramLong1, long paramLong2, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract boolean bind(IUpdateEngineCallback paramIUpdateEngineCallback)
    throws RemoteException;
  
  public abstract void cancel()
    throws RemoteException;
  
  public abstract void resetStatus()
    throws RemoteException;
  
  public abstract void resume()
    throws RemoteException;
  
  public abstract void suspend()
    throws RemoteException;
  
  public abstract boolean unbind(IUpdateEngineCallback paramIUpdateEngineCallback)
    throws RemoteException;
  
  public abstract boolean verifyPayloadApplicable(String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IUpdateEngine
  {
    private static final String DESCRIPTOR = "android.os.IUpdateEngine";
    static final int TRANSACTION_applyPayload = 1;
    static final int TRANSACTION_bind = 2;
    static final int TRANSACTION_cancel = 6;
    static final int TRANSACTION_resetStatus = 7;
    static final int TRANSACTION_resume = 5;
    static final int TRANSACTION_suspend = 4;
    static final int TRANSACTION_unbind = 3;
    static final int TRANSACTION_verifyPayloadApplicable = 8;
    
    public Stub()
    {
      attachInterface(this, "android.os.IUpdateEngine");
    }
    
    public static IUpdateEngine asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.IUpdateEngine");
      if ((localIInterface != null) && ((localIInterface instanceof IUpdateEngine))) {
        return (IUpdateEngine)localIInterface;
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
        case 8: 
          paramParcel1.enforceInterface("android.os.IUpdateEngine");
          paramInt1 = verifyPayloadApplicable(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.os.IUpdateEngine");
          resetStatus();
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.os.IUpdateEngine");
          cancel();
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.os.IUpdateEngine");
          resume();
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.os.IUpdateEngine");
          suspend();
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.os.IUpdateEngine");
          paramInt1 = unbind(IUpdateEngineCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.os.IUpdateEngine");
          paramInt1 = bind(IUpdateEngineCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.os.IUpdateEngine");
        applyPayload(paramParcel1.readString(), paramParcel1.readLong(), paramParcel1.readLong(), paramParcel1.createStringArray());
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.os.IUpdateEngine");
      return true;
    }
    
    private static class Proxy
      implements IUpdateEngine
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void applyPayload(String paramString, long paramLong1, long paramLong2, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUpdateEngine");
          localParcel1.writeString(paramString);
          localParcel1.writeLong(paramLong1);
          localParcel1.writeLong(paramLong2);
          localParcel1.writeStringArray(paramArrayOfString);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public boolean bind(IUpdateEngineCallback paramIUpdateEngineCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUpdateEngine");
          if (paramIUpdateEngineCallback != null) {
            paramIUpdateEngineCallback = paramIUpdateEngineCallback.asBinder();
          } else {
            paramIUpdateEngineCallback = null;
          }
          localParcel1.writeStrongBinder(paramIUpdateEngineCallback);
          paramIUpdateEngineCallback = mRemote;
          boolean bool = false;
          paramIUpdateEngineCallback.transact(2, localParcel1, localParcel2, 0);
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
      
      public void cancel()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUpdateEngine");
          mRemote.transact(6, localParcel1, localParcel2, 0);
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
        return "android.os.IUpdateEngine";
      }
      
      public void resetStatus()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUpdateEngine");
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void resume()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUpdateEngine");
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
      
      public void suspend()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUpdateEngine");
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean unbind(IUpdateEngineCallback paramIUpdateEngineCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUpdateEngine");
          if (paramIUpdateEngineCallback != null) {
            paramIUpdateEngineCallback = paramIUpdateEngineCallback.asBinder();
          } else {
            paramIUpdateEngineCallback = null;
          }
          localParcel1.writeStrongBinder(paramIUpdateEngineCallback);
          paramIUpdateEngineCallback = mRemote;
          boolean bool = false;
          paramIUpdateEngineCallback.transact(3, localParcel1, localParcel2, 0);
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
      
      public boolean verifyPayloadApplicable(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IUpdateEngine");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(8, localParcel1, localParcel2, 0);
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
    }
  }
}
