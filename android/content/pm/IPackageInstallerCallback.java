package android.content.pm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IPackageInstallerCallback
  extends IInterface
{
  public abstract void onSessionActiveChanged(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onSessionBadgingChanged(int paramInt)
    throws RemoteException;
  
  public abstract void onSessionCreated(int paramInt)
    throws RemoteException;
  
  public abstract void onSessionFinished(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onSessionProgressChanged(int paramInt, float paramFloat)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPackageInstallerCallback
  {
    private static final String DESCRIPTOR = "android.content.pm.IPackageInstallerCallback";
    static final int TRANSACTION_onSessionActiveChanged = 3;
    static final int TRANSACTION_onSessionBadgingChanged = 2;
    static final int TRANSACTION_onSessionCreated = 1;
    static final int TRANSACTION_onSessionFinished = 5;
    static final int TRANSACTION_onSessionProgressChanged = 4;
    
    public Stub()
    {
      attachInterface(this, "android.content.pm.IPackageInstallerCallback");
    }
    
    public static IPackageInstallerCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.pm.IPackageInstallerCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IPackageInstallerCallback))) {
        return (IPackageInstallerCallback)localIInterface;
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
        boolean bool1 = false;
        boolean bool2 = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 5: 
          paramParcel1.enforceInterface("android.content.pm.IPackageInstallerCallback");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          onSessionFinished(paramInt1, bool2);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.content.pm.IPackageInstallerCallback");
          onSessionProgressChanged(paramParcel1.readInt(), paramParcel1.readFloat());
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.content.pm.IPackageInstallerCallback");
          paramInt1 = paramParcel1.readInt();
          bool2 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          onSessionActiveChanged(paramInt1, bool2);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.content.pm.IPackageInstallerCallback");
          onSessionBadgingChanged(paramParcel1.readInt());
          return true;
        }
        paramParcel1.enforceInterface("android.content.pm.IPackageInstallerCallback");
        onSessionCreated(paramParcel1.readInt());
        return true;
      }
      paramParcel2.writeString("android.content.pm.IPackageInstallerCallback");
      return true;
    }
    
    private static class Proxy
      implements IPackageInstallerCallback
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
        return "android.content.pm.IPackageInstallerCallback";
      }
      
      public void onSessionActiveChanged(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.IPackageInstallerCallback");
          localParcel.writeInt(paramInt);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSessionBadgingChanged(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.IPackageInstallerCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSessionCreated(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.IPackageInstallerCallback");
          localParcel.writeInt(paramInt);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSessionFinished(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.IPackageInstallerCallback");
          localParcel.writeInt(paramInt);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSessionProgressChanged(int paramInt, float paramFloat)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.IPackageInstallerCallback");
          localParcel.writeInt(paramInt);
          localParcel.writeFloat(paramFloat);
          mRemote.transact(4, localParcel, null, 1);
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
