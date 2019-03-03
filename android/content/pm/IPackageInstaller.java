package android.content.pm;

import android.content.IntentSender;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IPackageInstaller
  extends IInterface
{
  public abstract void abandonSession(int paramInt)
    throws RemoteException;
  
  public abstract int createSession(PackageInstaller.SessionParams paramSessionParams, String paramString, int paramInt)
    throws RemoteException;
  
  public abstract ParceledListSlice getAllSessions(int paramInt)
    throws RemoteException;
  
  public abstract ParceledListSlice getMySessions(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract PackageInstaller.SessionInfo getSessionInfo(int paramInt)
    throws RemoteException;
  
  public abstract IPackageInstallerSession openSession(int paramInt)
    throws RemoteException;
  
  public abstract void registerCallback(IPackageInstallerCallback paramIPackageInstallerCallback, int paramInt)
    throws RemoteException;
  
  public abstract void setPermissionsResult(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void uninstall(VersionedPackage paramVersionedPackage, String paramString, int paramInt1, IntentSender paramIntentSender, int paramInt2)
    throws RemoteException;
  
  public abstract void uninstallBundledSystemPackage(VersionedPackage paramVersionedPackage, String paramString, int paramInt1, IntentSender paramIntentSender, int paramInt2)
    throws RemoteException;
  
  public abstract void unregisterCallback(IPackageInstallerCallback paramIPackageInstallerCallback)
    throws RemoteException;
  
  public abstract void updateSessionAppIcon(int paramInt, Bitmap paramBitmap)
    throws RemoteException;
  
  public abstract void updateSessionAppLabel(int paramInt, String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPackageInstaller
  {
    private static final String DESCRIPTOR = "android.content.pm.IPackageInstaller";
    static final int TRANSACTION_abandonSession = 4;
    static final int TRANSACTION_createSession = 1;
    static final int TRANSACTION_getAllSessions = 7;
    static final int TRANSACTION_getMySessions = 8;
    static final int TRANSACTION_getSessionInfo = 6;
    static final int TRANSACTION_openSession = 5;
    static final int TRANSACTION_registerCallback = 9;
    static final int TRANSACTION_setPermissionsResult = 12;
    static final int TRANSACTION_uninstall = 11;
    static final int TRANSACTION_uninstallBundledSystemPackage = 13;
    static final int TRANSACTION_unregisterCallback = 10;
    static final int TRANSACTION_updateSessionAppIcon = 2;
    static final int TRANSACTION_updateSessionAppLabel = 3;
    
    public Stub()
    {
      attachInterface(this, "android.content.pm.IPackageInstaller");
    }
    
    public static IPackageInstaller asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.pm.IPackageInstaller");
      if ((localIInterface != null) && ((localIInterface instanceof IPackageInstaller))) {
        return (IPackageInstaller)localIInterface;
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
        boolean bool = false;
        String str1 = null;
        Object localObject1 = null;
        String str2 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 13: 
          paramParcel1.enforceInterface("android.content.pm.IPackageInstaller");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (VersionedPackage)VersionedPackage.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          str1 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject3 = (IntentSender)IntentSender.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          uninstallBundledSystemPackage((VersionedPackage)localObject1, str1, paramInt1, (IntentSender)localObject3, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.content.pm.IPackageInstaller");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool = true;
          }
          setPermissionsResult(paramInt1, bool);
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.content.pm.IPackageInstaller");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (VersionedPackage)VersionedPackage.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          str2 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {}
          for (localObject3 = (IntentSender)IntentSender.CREATOR.createFromParcel(paramParcel1);; localObject3 = str1) {
            break;
          }
          uninstall((VersionedPackage)localObject1, str2, paramInt1, (IntentSender)localObject3, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.content.pm.IPackageInstaller");
          unregisterCallback(IPackageInstallerCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.content.pm.IPackageInstaller");
          registerCallback(IPackageInstallerCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.content.pm.IPackageInstaller");
          paramParcel1 = getMySessions(paramParcel1.readString(), paramParcel1.readInt());
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
        case 7: 
          paramParcel1.enforceInterface("android.content.pm.IPackageInstaller");
          paramParcel1 = getAllSessions(paramParcel1.readInt());
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
        case 6: 
          paramParcel1.enforceInterface("android.content.pm.IPackageInstaller");
          paramParcel1 = getSessionInfo(paramParcel1.readInt());
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
        case 5: 
          paramParcel1.enforceInterface("android.content.pm.IPackageInstaller");
          localObject3 = openSession(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel1 = (Parcel)localObject1;
          if (localObject3 != null) {
            paramParcel1 = ((IPackageInstallerSession)localObject3).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.content.pm.IPackageInstaller");
          abandonSession(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.content.pm.IPackageInstaller");
          updateSessionAppLabel(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.content.pm.IPackageInstaller");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bitmap)Bitmap.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str2;
          }
          updateSessionAppIcon(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.content.pm.IPackageInstaller");
        if (paramParcel1.readInt() != 0) {
          localObject1 = (PackageInstaller.SessionParams)PackageInstaller.SessionParams.CREATOR.createFromParcel(paramParcel1);
        } else {
          localObject1 = localObject2;
        }
        paramInt1 = createSession((PackageInstaller.SessionParams)localObject1, paramParcel1.readString(), paramParcel1.readInt());
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.content.pm.IPackageInstaller");
      return true;
    }
    
    private static class Proxy
      implements IPackageInstaller
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void abandonSession(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageInstaller");
          localParcel1.writeInt(paramInt);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public int createSession(PackageInstaller.SessionParams paramSessionParams, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageInstaller");
          if (paramSessionParams != null)
          {
            localParcel1.writeInt(1);
            paramSessionParams.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(1, localParcel1, localParcel2, 0);
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
      
      public ParceledListSlice getAllSessions(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageInstaller");
          localParcel1.writeInt(paramInt);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ParceledListSlice localParceledListSlice;
          if (localParcel2.readInt() != 0) {
            localParceledListSlice = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            localParceledListSlice = null;
          }
          return localParceledListSlice;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.content.pm.IPackageInstaller";
      }
      
      public ParceledListSlice getMySessions(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageInstaller");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
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
      
      public PackageInstaller.SessionInfo getSessionInfo(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageInstaller");
          localParcel1.writeInt(paramInt);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          PackageInstaller.SessionInfo localSessionInfo;
          if (localParcel2.readInt() != 0) {
            localSessionInfo = (PackageInstaller.SessionInfo)PackageInstaller.SessionInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localSessionInfo = null;
          }
          return localSessionInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IPackageInstallerSession openSession(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageInstaller");
          localParcel1.writeInt(paramInt);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IPackageInstallerSession localIPackageInstallerSession = IPackageInstallerSession.Stub.asInterface(localParcel2.readStrongBinder());
          return localIPackageInstallerSession;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerCallback(IPackageInstallerCallback paramIPackageInstallerCallback, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageInstaller");
          if (paramIPackageInstallerCallback != null) {
            paramIPackageInstallerCallback = paramIPackageInstallerCallback.asBinder();
          } else {
            paramIPackageInstallerCallback = null;
          }
          localParcel1.writeStrongBinder(paramIPackageInstallerCallback);
          localParcel1.writeInt(paramInt);
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
      
      public void setPermissionsResult(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageInstaller");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void uninstall(VersionedPackage paramVersionedPackage, String paramString, int paramInt1, IntentSender paramIntentSender, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageInstaller");
          if (paramVersionedPackage != null)
          {
            localParcel1.writeInt(1);
            paramVersionedPackage.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          if (paramIntentSender != null)
          {
            localParcel1.writeInt(1);
            paramIntentSender.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt2);
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
      
      public void uninstallBundledSystemPackage(VersionedPackage paramVersionedPackage, String paramString, int paramInt1, IntentSender paramIntentSender, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageInstaller");
          if (paramVersionedPackage != null)
          {
            localParcel1.writeInt(1);
            paramVersionedPackage.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          if (paramIntentSender != null)
          {
            localParcel1.writeInt(1);
            paramIntentSender.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt2);
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unregisterCallback(IPackageInstallerCallback paramIPackageInstallerCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageInstaller");
          if (paramIPackageInstallerCallback != null) {
            paramIPackageInstallerCallback = paramIPackageInstallerCallback.asBinder();
          } else {
            paramIPackageInstallerCallback = null;
          }
          localParcel1.writeStrongBinder(paramIPackageInstallerCallback);
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
      
      public void updateSessionAppIcon(int paramInt, Bitmap paramBitmap)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageInstaller");
          localParcel1.writeInt(paramInt);
          if (paramBitmap != null)
          {
            localParcel1.writeInt(1);
            paramBitmap.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void updateSessionAppLabel(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageInstaller");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(3, localParcel1, localParcel2, 0);
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
