package android.content.pm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IPackageManagerNative
  extends IInterface
{
  public abstract String getInstallerForPackage(String paramString)
    throws RemoteException;
  
  public abstract String[] getNamesForUids(int[] paramArrayOfInt)
    throws RemoteException;
  
  public abstract long getVersionCodeForPackage(String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPackageManagerNative
  {
    private static final String DESCRIPTOR = "android.content.pm.IPackageManagerNative";
    static final int TRANSACTION_getInstallerForPackage = 2;
    static final int TRANSACTION_getNamesForUids = 1;
    static final int TRANSACTION_getVersionCodeForPackage = 3;
    
    public Stub()
    {
      attachInterface(this, "android.content.pm.IPackageManagerNative");
    }
    
    public static IPackageManagerNative asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.pm.IPackageManagerNative");
      if ((localIInterface != null) && ((localIInterface instanceof IPackageManagerNative))) {
        return (IPackageManagerNative)localIInterface;
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
          paramParcel1.enforceInterface("android.content.pm.IPackageManagerNative");
          long l = getVersionCodeForPackage(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManagerNative");
          paramParcel1 = getInstallerForPackage(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.content.pm.IPackageManagerNative");
        paramParcel1 = getNamesForUids(paramParcel1.createIntArray());
        paramParcel2.writeNoException();
        paramParcel2.writeStringArray(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.content.pm.IPackageManagerNative");
      return true;
    }
    
    private static class Proxy
      implements IPackageManagerNative
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
      
      public String getInstallerForPackage(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManagerNative");
          localParcel1.writeString(paramString);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.content.pm.IPackageManagerNative";
      }
      
      public String[] getNamesForUids(int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManagerNative");
          localParcel1.writeIntArray(paramArrayOfInt);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramArrayOfInt = localParcel2.createStringArray();
          return paramArrayOfInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long getVersionCodeForPackage(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManagerNative");
          localParcel1.writeString(paramString);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
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
