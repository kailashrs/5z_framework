package android.content;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.PersistableBundle;
import android.os.RemoteException;

public abstract interface IRestrictionsManager
  extends IInterface
{
  public abstract Intent createLocalApprovalIntent()
    throws RemoteException;
  
  public abstract Bundle getApplicationRestrictions(String paramString)
    throws RemoteException;
  
  public abstract boolean hasRestrictionsProvider()
    throws RemoteException;
  
  public abstract void notifyPermissionResponse(String paramString, PersistableBundle paramPersistableBundle)
    throws RemoteException;
  
  public abstract void requestPermission(String paramString1, String paramString2, String paramString3, PersistableBundle paramPersistableBundle)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IRestrictionsManager
  {
    private static final String DESCRIPTOR = "android.content.IRestrictionsManager";
    static final int TRANSACTION_createLocalApprovalIntent = 5;
    static final int TRANSACTION_getApplicationRestrictions = 1;
    static final int TRANSACTION_hasRestrictionsProvider = 2;
    static final int TRANSACTION_notifyPermissionResponse = 4;
    static final int TRANSACTION_requestPermission = 3;
    
    public Stub()
    {
      attachInterface(this, "android.content.IRestrictionsManager");
    }
    
    public static IRestrictionsManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.IRestrictionsManager");
      if ((localIInterface != null) && ((localIInterface instanceof IRestrictionsManager))) {
        return (IRestrictionsManager)localIInterface;
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
        String str1 = null;
        String str2 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 5: 
          paramParcel1.enforceInterface("android.content.IRestrictionsManager");
          paramParcel1 = createLocalApprovalIntent();
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
          paramParcel1.enforceInterface("android.content.IRestrictionsManager");
          str1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PersistableBundle)PersistableBundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str2;
          }
          notifyPermissionResponse(str1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.content.IRestrictionsManager");
          String str3 = paramParcel1.readString();
          str2 = paramParcel1.readString();
          String str4 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PersistableBundle)PersistableBundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str1;
          }
          requestPermission(str3, str2, str4, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.content.IRestrictionsManager");
          paramInt1 = hasRestrictionsProvider();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.content.IRestrictionsManager");
        paramParcel1 = getApplicationRestrictions(paramParcel1.readString());
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
      }
      paramParcel2.writeString("android.content.IRestrictionsManager");
      return true;
    }
    
    private static class Proxy
      implements IRestrictionsManager
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
      
      public Intent createLocalApprovalIntent()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IRestrictionsManager");
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Intent localIntent;
          if (localParcel2.readInt() != 0) {
            localIntent = (Intent)Intent.CREATOR.createFromParcel(localParcel2);
          } else {
            localIntent = null;
          }
          return localIntent;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Bundle getApplicationRestrictions(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IRestrictionsManager");
          localParcel1.writeString(paramString);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (Bundle)Bundle.CREATOR.createFromParcel(localParcel2);
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
      
      public String getInterfaceDescriptor()
      {
        return "android.content.IRestrictionsManager";
      }
      
      public boolean hasRestrictionsProvider()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IRestrictionsManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(2, localParcel1, localParcel2, 0);
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
      
      public void notifyPermissionResponse(String paramString, PersistableBundle paramPersistableBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IRestrictionsManager");
          localParcel1.writeString(paramString);
          if (paramPersistableBundle != null)
          {
            localParcel1.writeInt(1);
            paramPersistableBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
      
      public void requestPermission(String paramString1, String paramString2, String paramString3, PersistableBundle paramPersistableBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.IRestrictionsManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          if (paramPersistableBundle != null)
          {
            localParcel1.writeInt(1);
            paramPersistableBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
