package android.service.notification;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IStatusBarNotificationHolder
  extends IInterface
{
  public abstract StatusBarNotification get()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IStatusBarNotificationHolder
  {
    private static final String DESCRIPTOR = "android.service.notification.IStatusBarNotificationHolder";
    static final int TRANSACTION_get = 1;
    
    public Stub()
    {
      attachInterface(this, "android.service.notification.IStatusBarNotificationHolder");
    }
    
    public static IStatusBarNotificationHolder asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.notification.IStatusBarNotificationHolder");
      if ((localIInterface != null) && ((localIInterface instanceof IStatusBarNotificationHolder))) {
        return (IStatusBarNotificationHolder)localIInterface;
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
      if (paramInt1 != 1)
      {
        if (paramInt1 != 1598968902) {
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        }
        paramParcel2.writeString("android.service.notification.IStatusBarNotificationHolder");
        return true;
      }
      paramParcel1.enforceInterface("android.service.notification.IStatusBarNotificationHolder");
      paramParcel1 = get();
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
    
    private static class Proxy
      implements IStatusBarNotificationHolder
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
      
      public StatusBarNotification get()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.service.notification.IStatusBarNotificationHolder");
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          StatusBarNotification localStatusBarNotification;
          if (localParcel2.readInt() != 0) {
            localStatusBarNotification = (StatusBarNotification)StatusBarNotification.CREATOR.createFromParcel(localParcel2);
          } else {
            localStatusBarNotification = null;
          }
          return localStatusBarNotification;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.service.notification.IStatusBarNotificationHolder";
      }
    }
  }
}
