package android.os;

public abstract interface IPermissionController
  extends IInterface
{
  public abstract boolean checkPermission(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract int getPackageUid(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract String[] getPackagesForUid(int paramInt)
    throws RemoteException;
  
  public abstract boolean isRuntimePermission(String paramString)
    throws RemoteException;
  
  public abstract int noteOp(String paramString1, int paramInt, String paramString2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPermissionController
  {
    private static final String DESCRIPTOR = "android.os.IPermissionController";
    static final int TRANSACTION_checkPermission = 1;
    static final int TRANSACTION_getPackageUid = 5;
    static final int TRANSACTION_getPackagesForUid = 3;
    static final int TRANSACTION_isRuntimePermission = 4;
    static final int TRANSACTION_noteOp = 2;
    
    public Stub()
    {
      attachInterface(this, "android.os.IPermissionController");
    }
    
    public static IPermissionController asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.IPermissionController");
      if ((localIInterface != null) && ((localIInterface instanceof IPermissionController))) {
        return (IPermissionController)localIInterface;
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
        case 5: 
          paramParcel1.enforceInterface("android.os.IPermissionController");
          paramInt1 = getPackageUid(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.os.IPermissionController");
          paramInt1 = isRuntimePermission(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.os.IPermissionController");
          paramParcel1 = getPackagesForUid(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.os.IPermissionController");
          paramInt1 = noteOp(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.os.IPermissionController");
        paramInt1 = checkPermission(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.os.IPermissionController");
      return true;
    }
    
    private static class Proxy
      implements IPermissionController
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
      
      public boolean checkPermission(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPermissionController");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 != 0) {
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
        return "android.os.IPermissionController";
      }
      
      public int getPackageUid(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPermissionController");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getPackagesForUid(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPermissionController");
          localParcel1.writeInt(paramInt);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isRuntimePermission(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPermissionController");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(4, localParcel1, localParcel2, 0);
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
      
      public int noteOp(String paramString1, int paramInt, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.IPermissionController");
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString2);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
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
