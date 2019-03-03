package android.content.pm;

import android.appwidget.AppWidgetProviderInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IPinItemRequest
  extends IInterface
{
  public abstract boolean accept(Bundle paramBundle)
    throws RemoteException;
  
  public abstract AppWidgetProviderInfo getAppWidgetProviderInfo()
    throws RemoteException;
  
  public abstract Bundle getExtras()
    throws RemoteException;
  
  public abstract ShortcutInfo getShortcutInfo()
    throws RemoteException;
  
  public abstract boolean isValid()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPinItemRequest
  {
    private static final String DESCRIPTOR = "android.content.pm.IPinItemRequest";
    static final int TRANSACTION_accept = 2;
    static final int TRANSACTION_getAppWidgetProviderInfo = 4;
    static final int TRANSACTION_getExtras = 5;
    static final int TRANSACTION_getShortcutInfo = 3;
    static final int TRANSACTION_isValid = 1;
    
    public Stub()
    {
      attachInterface(this, "android.content.pm.IPinItemRequest");
    }
    
    public static IPinItemRequest asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.pm.IPinItemRequest");
      if ((localIInterface != null) && ((localIInterface instanceof IPinItemRequest))) {
        return (IPinItemRequest)localIInterface;
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
          paramParcel1.enforceInterface("android.content.pm.IPinItemRequest");
          paramParcel1 = getExtras();
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
          paramParcel1.enforceInterface("android.content.pm.IPinItemRequest");
          paramParcel1 = getAppWidgetProviderInfo();
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
          paramParcel1.enforceInterface("android.content.pm.IPinItemRequest");
          paramParcel1 = getShortcutInfo();
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
        case 2: 
          paramParcel1.enforceInterface("android.content.pm.IPinItemRequest");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          paramInt1 = accept(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.content.pm.IPinItemRequest");
        paramInt1 = isValid();
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.content.pm.IPinItemRequest");
      return true;
    }
    
    private static class Proxy
      implements IPinItemRequest
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public boolean accept(Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPinItemRequest");
          boolean bool = true;
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
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
      
      public AppWidgetProviderInfo getAppWidgetProviderInfo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPinItemRequest");
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          AppWidgetProviderInfo localAppWidgetProviderInfo;
          if (localParcel2.readInt() != 0) {
            localAppWidgetProviderInfo = (AppWidgetProviderInfo)AppWidgetProviderInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localAppWidgetProviderInfo = null;
          }
          return localAppWidgetProviderInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Bundle getExtras()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPinItemRequest");
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Bundle localBundle;
          if (localParcel2.readInt() != 0) {
            localBundle = (Bundle)Bundle.CREATOR.createFromParcel(localParcel2);
          } else {
            localBundle = null;
          }
          return localBundle;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.content.pm.IPinItemRequest";
      }
      
      public ShortcutInfo getShortcutInfo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPinItemRequest");
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ShortcutInfo localShortcutInfo;
          if (localParcel2.readInt() != 0) {
            localShortcutInfo = (ShortcutInfo)ShortcutInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localShortcutInfo = null;
          }
          return localShortcutInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isValid()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPinItemRequest");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(1, localParcel1, localParcel2, 0);
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
