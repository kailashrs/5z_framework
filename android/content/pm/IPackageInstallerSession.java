package android.content.pm;

import android.content.IntentSender;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IPackageInstallerSession
  extends IInterface
{
  public abstract void abandon()
    throws RemoteException;
  
  public abstract void addClientProgress(float paramFloat)
    throws RemoteException;
  
  public abstract void close()
    throws RemoteException;
  
  public abstract void commit(IntentSender paramIntentSender, boolean paramBoolean)
    throws RemoteException;
  
  public abstract String[] getNames()
    throws RemoteException;
  
  public abstract ParcelFileDescriptor openRead(String paramString)
    throws RemoteException;
  
  public abstract ParcelFileDescriptor openWrite(String paramString, long paramLong1, long paramLong2)
    throws RemoteException;
  
  public abstract void removeSplit(String paramString)
    throws RemoteException;
  
  public abstract void setClientProgress(float paramFloat)
    throws RemoteException;
  
  public abstract void transfer(String paramString)
    throws RemoteException;
  
  public abstract void write(String paramString, long paramLong1, long paramLong2, ParcelFileDescriptor paramParcelFileDescriptor)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPackageInstallerSession
  {
    private static final String DESCRIPTOR = "android.content.pm.IPackageInstallerSession";
    static final int TRANSACTION_abandon = 11;
    static final int TRANSACTION_addClientProgress = 2;
    static final int TRANSACTION_close = 8;
    static final int TRANSACTION_commit = 9;
    static final int TRANSACTION_getNames = 3;
    static final int TRANSACTION_openRead = 5;
    static final int TRANSACTION_openWrite = 4;
    static final int TRANSACTION_removeSplit = 7;
    static final int TRANSACTION_setClientProgress = 1;
    static final int TRANSACTION_transfer = 10;
    static final int TRANSACTION_write = 6;
    
    public Stub()
    {
      attachInterface(this, "android.content.pm.IPackageInstallerSession");
    }
    
    public static IPackageInstallerSession asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.pm.IPackageInstallerSession");
      if ((localIInterface != null) && ((localIInterface instanceof IPackageInstallerSession))) {
        return (IPackageInstallerSession)localIInterface;
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
        Object localObject = null;
        String str = null;
        boolean bool = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 11: 
          paramParcel1.enforceInterface("android.content.pm.IPackageInstallerSession");
          abandon();
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.content.pm.IPackageInstallerSession");
          transfer(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.content.pm.IPackageInstallerSession");
          if (paramParcel1.readInt() != 0) {
            localObject = (IntentSender)IntentSender.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject = str;
          }
          if (paramParcel1.readInt() != 0) {
            bool = true;
          }
          commit((IntentSender)localObject, bool);
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.content.pm.IPackageInstallerSession");
          close();
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.content.pm.IPackageInstallerSession");
          removeSplit(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.content.pm.IPackageInstallerSession");
          str = paramParcel1.readString();
          long l1 = paramParcel1.readLong();
          long l2 = paramParcel1.readLong();
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = (Parcel)localObject) {
            break;
          }
          write(str, l1, l2, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.content.pm.IPackageInstallerSession");
          paramParcel1 = openRead(paramParcel1.readString());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.content.pm.IPackageInstallerSession");
          paramParcel1 = openWrite(paramParcel1.readString(), paramParcel1.readLong(), paramParcel1.readLong());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.content.pm.IPackageInstallerSession");
          paramParcel1 = getNames();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.content.pm.IPackageInstallerSession");
          addClientProgress(paramParcel1.readFloat());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.content.pm.IPackageInstallerSession");
        setClientProgress(paramParcel1.readFloat());
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.content.pm.IPackageInstallerSession");
      return true;
    }
    
    private static class Proxy
      implements IPackageInstallerSession
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void abandon()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageInstallerSession");
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void addClientProgress(float paramFloat)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageInstallerSession");
          localParcel1.writeFloat(paramFloat);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void close()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageInstallerSession");
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void commit(IntentSender paramIntentSender, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageInstallerSession");
          if (paramIntentSender != null)
          {
            localParcel1.writeInt(1);
            paramIntentSender.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(9, localParcel1, localParcel2, 0);
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
        return "android.content.pm.IPackageInstallerSession";
      }
      
      public String[] getNames()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageInstallerSession");
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
      
      public ParcelFileDescriptor openRead(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageInstallerSession");
          localParcel1.writeString(paramString);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParcelFileDescriptor openWrite(String paramString, long paramLong1, long paramLong2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageInstallerSession");
          localParcel1.writeString(paramString);
          localParcel1.writeLong(paramLong1);
          localParcel1.writeLong(paramLong2);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeSplit(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageInstallerSession");
          localParcel1.writeString(paramString);
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
      
      public void setClientProgress(float paramFloat)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageInstallerSession");
          localParcel1.writeFloat(paramFloat);
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
      
      public void transfer(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageInstallerSession");
          localParcel1.writeString(paramString);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void write(String paramString, long paramLong1, long paramLong2, ParcelFileDescriptor paramParcelFileDescriptor)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageInstallerSession");
          localParcel1.writeString(paramString);
          localParcel1.writeLong(paramLong1);
          localParcel1.writeLong(paramLong2);
          if (paramParcelFileDescriptor != null)
          {
            localParcel1.writeInt(1);
            paramParcelFileDescriptor.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
    }
  }
}
