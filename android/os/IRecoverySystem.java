package android.os;

public abstract interface IRecoverySystem
  extends IInterface
{
  public abstract boolean clearBcb()
    throws RemoteException;
  
  public abstract void rebootRecoveryWithCommand(String paramString)
    throws RemoteException;
  
  public abstract boolean setupBcb(String paramString)
    throws RemoteException;
  
  public abstract boolean uncrypt(String paramString, IRecoverySystemProgressListener paramIRecoverySystemProgressListener)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IRecoverySystem
  {
    private static final String DESCRIPTOR = "android.os.IRecoverySystem";
    static final int TRANSACTION_clearBcb = 3;
    static final int TRANSACTION_rebootRecoveryWithCommand = 4;
    static final int TRANSACTION_setupBcb = 2;
    static final int TRANSACTION_uncrypt = 1;
    
    public Stub()
    {
      attachInterface(this, "android.os.IRecoverySystem");
    }
    
    public static IRecoverySystem asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.IRecoverySystem");
      if ((localIInterface != null) && ((localIInterface instanceof IRecoverySystem))) {
        return (IRecoverySystem)localIInterface;
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
        case 4: 
          paramParcel1.enforceInterface("android.os.IRecoverySystem");
          rebootRecoveryWithCommand(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.os.IRecoverySystem");
          paramInt1 = clearBcb();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.os.IRecoverySystem");
          paramInt1 = setupBcb(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.os.IRecoverySystem");
        paramInt1 = uncrypt(paramParcel1.readString(), IRecoverySystemProgressListener.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.os.IRecoverySystem");
      return true;
    }
    
    private static class Proxy
      implements IRecoverySystem
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
      
      public boolean clearBcb()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IRecoverySystem");
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
      
      public String getInterfaceDescriptor()
      {
        return "android.os.IRecoverySystem";
      }
      
      public void rebootRecoveryWithCommand(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IRecoverySystem");
          localParcel1.writeString(paramString);
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
      
      public boolean setupBcb(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IRecoverySystem");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(2, localParcel1, localParcel2, 0);
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
      
      public boolean uncrypt(String paramString, IRecoverySystemProgressListener paramIRecoverySystemProgressListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IRecoverySystem");
          localParcel1.writeString(paramString);
          if (paramIRecoverySystemProgressListener != null) {
            paramString = paramIRecoverySystemProgressListener.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(1, localParcel1, localParcel2, 0);
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
