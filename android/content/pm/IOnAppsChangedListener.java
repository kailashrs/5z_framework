package android.content.pm;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.UserHandle;

public abstract interface IOnAppsChangedListener
  extends IInterface
{
  public abstract void onPackageAdded(UserHandle paramUserHandle, String paramString)
    throws RemoteException;
  
  public abstract void onPackageChanged(UserHandle paramUserHandle, String paramString)
    throws RemoteException;
  
  public abstract void onPackageRemoved(UserHandle paramUserHandle, String paramString)
    throws RemoteException;
  
  public abstract void onPackagesAvailable(UserHandle paramUserHandle, String[] paramArrayOfString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onPackagesSuspended(UserHandle paramUserHandle, String[] paramArrayOfString, Bundle paramBundle)
    throws RemoteException;
  
  public abstract void onPackagesUnavailable(UserHandle paramUserHandle, String[] paramArrayOfString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void onPackagesUnsuspended(UserHandle paramUserHandle, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void onShortcutChanged(UserHandle paramUserHandle, String paramString, ParceledListSlice paramParceledListSlice)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IOnAppsChangedListener
  {
    private static final String DESCRIPTOR = "android.content.pm.IOnAppsChangedListener";
    static final int TRANSACTION_onPackageAdded = 2;
    static final int TRANSACTION_onPackageChanged = 3;
    static final int TRANSACTION_onPackageRemoved = 1;
    static final int TRANSACTION_onPackagesAvailable = 4;
    static final int TRANSACTION_onPackagesSuspended = 6;
    static final int TRANSACTION_onPackagesUnavailable = 5;
    static final int TRANSACTION_onPackagesUnsuspended = 7;
    static final int TRANSACTION_onShortcutChanged = 8;
    
    public Stub()
    {
      attachInterface(this, "android.content.pm.IOnAppsChangedListener");
    }
    
    public static IOnAppsChangedListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.pm.IOnAppsChangedListener");
      if ((localIInterface != null) && ((localIInterface instanceof IOnAppsChangedListener))) {
        return (IOnAppsChangedListener)localIInterface;
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
        Object localObject1 = null;
        String str = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        String[] arrayOfString = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 8: 
          paramParcel1.enforceInterface("android.content.pm.IOnAppsChangedListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          str = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = arrayOfString;
          }
          onShortcutChanged(paramParcel2, str, paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.content.pm.IOnAppsChangedListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject1;
          }
          onPackagesUnsuspended(paramParcel2, paramParcel1.createStringArray());
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.content.pm.IOnAppsChangedListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          arrayOfString = paramParcel1.createStringArray();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str;
          }
          onPackagesSuspended(paramParcel2, arrayOfString, paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.content.pm.IOnAppsChangedListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject2;
          }
          arrayOfString = paramParcel1.createStringArray();
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          onPackagesUnavailable(paramParcel2, arrayOfString, bool2);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.content.pm.IOnAppsChangedListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject3;
          }
          arrayOfString = paramParcel1.createStringArray();
          bool2 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          onPackagesAvailable(paramParcel2, arrayOfString, bool2);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.content.pm.IOnAppsChangedListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject4;
          }
          onPackageChanged(paramParcel2, paramParcel1.readString());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.content.pm.IOnAppsChangedListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = localObject5;
          }
          onPackageAdded(paramParcel2, paramParcel1.readString());
          return true;
        }
        paramParcel1.enforceInterface("android.content.pm.IOnAppsChangedListener");
        if (paramParcel1.readInt() != 0) {
          paramParcel2 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel2 = localObject6;
        }
        onPackageRemoved(paramParcel2, paramParcel1.readString());
        return true;
      }
      paramParcel2.writeString("android.content.pm.IOnAppsChangedListener");
      return true;
    }
    
    private static class Proxy
      implements IOnAppsChangedListener
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
        return "android.content.pm.IOnAppsChangedListener";
      }
      
      public void onPackageAdded(UserHandle paramUserHandle, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.IOnAppsChangedListener");
          if (paramUserHandle != null)
          {
            localParcel.writeInt(1);
            paramUserHandle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeString(paramString);
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPackageChanged(UserHandle paramUserHandle, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.IOnAppsChangedListener");
          if (paramUserHandle != null)
          {
            localParcel.writeInt(1);
            paramUserHandle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeString(paramString);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPackageRemoved(UserHandle paramUserHandle, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.IOnAppsChangedListener");
          if (paramUserHandle != null)
          {
            localParcel.writeInt(1);
            paramUserHandle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeString(paramString);
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPackagesAvailable(UserHandle paramUserHandle, String[] paramArrayOfString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.IOnAppsChangedListener");
          if (paramUserHandle != null)
          {
            localParcel.writeInt(1);
            paramUserHandle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeStringArray(paramArrayOfString);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPackagesSuspended(UserHandle paramUserHandle, String[] paramArrayOfString, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.IOnAppsChangedListener");
          if (paramUserHandle != null)
          {
            localParcel.writeInt(1);
            paramUserHandle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeStringArray(paramArrayOfString);
          if (paramBundle != null)
          {
            localParcel.writeInt(1);
            paramBundle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPackagesUnavailable(UserHandle paramUserHandle, String[] paramArrayOfString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.IOnAppsChangedListener");
          if (paramUserHandle != null)
          {
            localParcel.writeInt(1);
            paramUserHandle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeStringArray(paramArrayOfString);
          localParcel.writeInt(paramBoolean);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onPackagesUnsuspended(UserHandle paramUserHandle, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.IOnAppsChangedListener");
          if (paramUserHandle != null)
          {
            localParcel.writeInt(1);
            paramUserHandle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeStringArray(paramArrayOfString);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onShortcutChanged(UserHandle paramUserHandle, String paramString, ParceledListSlice paramParceledListSlice)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.IOnAppsChangedListener");
          if (paramUserHandle != null)
          {
            localParcel.writeInt(1);
            paramUserHandle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeString(paramString);
          if (paramParceledListSlice != null)
          {
            localParcel.writeInt(1);
            paramParceledListSlice.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(8, localParcel, null, 1);
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
