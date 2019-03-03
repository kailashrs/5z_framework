package android.service.notification;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IConditionProvider
  extends IInterface
{
  public abstract void onConnected()
    throws RemoteException;
  
  public abstract void onSubscribe(Uri paramUri)
    throws RemoteException;
  
  public abstract void onUnsubscribe(Uri paramUri)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IConditionProvider
  {
    private static final String DESCRIPTOR = "android.service.notification.IConditionProvider";
    static final int TRANSACTION_onConnected = 1;
    static final int TRANSACTION_onSubscribe = 2;
    static final int TRANSACTION_onUnsubscribe = 3;
    
    public Stub()
    {
      attachInterface(this, "android.service.notification.IConditionProvider");
    }
    
    public static IConditionProvider asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.notification.IConditionProvider");
      if ((localIInterface != null) && ((localIInterface instanceof IConditionProvider))) {
        return (IConditionProvider)localIInterface;
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
        Object localObject1 = null;
        Object localObject2 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 3: 
          paramParcel1.enforceInterface("android.service.notification.IConditionProvider");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          onUnsubscribe(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.service.notification.IConditionProvider");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          onSubscribe(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.service.notification.IConditionProvider");
        onConnected();
        return true;
      }
      paramParcel2.writeString("android.service.notification.IConditionProvider");
      return true;
    }
    
    private static class Proxy
      implements IConditionProvider
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
        return "android.service.notification.IConditionProvider";
      }
      
      public void onConnected()
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.notification.IConditionProvider");
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onSubscribe(Uri paramUri)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.notification.IConditionProvider");
          if (paramUri != null)
          {
            localParcel.writeInt(1);
            paramUri.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(2, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onUnsubscribe(Uri paramUri)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.notification.IConditionProvider");
          if (paramUri != null)
          {
            localParcel.writeInt(1);
            paramUri.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(3, localParcel, null, 1);
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
