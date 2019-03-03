package android.service.notification;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.UserHandle;

public abstract interface INotificationListener
  extends IInterface
{
  public abstract void onInterruptionFilterChanged(int paramInt)
    throws RemoteException;
  
  public abstract void onListenerConnected(NotificationRankingUpdate paramNotificationRankingUpdate)
    throws RemoteException;
  
  public abstract void onListenerHintsChanged(int paramInt)
    throws RemoteException;
  
  public abstract void onNotificationChannelGroupModification(String paramString, UserHandle paramUserHandle, NotificationChannelGroup paramNotificationChannelGroup, int paramInt)
    throws RemoteException;
  
  public abstract void onNotificationChannelModification(String paramString, UserHandle paramUserHandle, NotificationChannel paramNotificationChannel, int paramInt)
    throws RemoteException;
  
  public abstract void onNotificationEnqueued(IStatusBarNotificationHolder paramIStatusBarNotificationHolder)
    throws RemoteException;
  
  public abstract void onNotificationPosted(IStatusBarNotificationHolder paramIStatusBarNotificationHolder, NotificationRankingUpdate paramNotificationRankingUpdate)
    throws RemoteException;
  
  public abstract void onNotificationRankingUpdate(NotificationRankingUpdate paramNotificationRankingUpdate)
    throws RemoteException;
  
  public abstract void onNotificationRemoved(IStatusBarNotificationHolder paramIStatusBarNotificationHolder, NotificationRankingUpdate paramNotificationRankingUpdate, NotificationStats paramNotificationStats, int paramInt)
    throws RemoteException;
  
  public abstract void onNotificationSnoozedUntilContext(IStatusBarNotificationHolder paramIStatusBarNotificationHolder, String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements INotificationListener
  {
    private static final String DESCRIPTOR = "android.service.notification.INotificationListener";
    static final int TRANSACTION_onInterruptionFilterChanged = 6;
    static final int TRANSACTION_onListenerConnected = 1;
    static final int TRANSACTION_onListenerHintsChanged = 5;
    static final int TRANSACTION_onNotificationChannelGroupModification = 8;
    static final int TRANSACTION_onNotificationChannelModification = 7;
    static final int TRANSACTION_onNotificationEnqueued = 9;
    static final int TRANSACTION_onNotificationPosted = 2;
    static final int TRANSACTION_onNotificationRankingUpdate = 4;
    static final int TRANSACTION_onNotificationRemoved = 3;
    static final int TRANSACTION_onNotificationSnoozedUntilContext = 10;
    
    public Stub()
    {
      attachInterface(this, "android.service.notification.INotificationListener");
    }
    
    public static INotificationListener asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.service.notification.INotificationListener");
      if ((localIInterface != null) && ((localIInterface instanceof INotificationListener))) {
        return (INotificationListener)localIInterface;
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
        String str = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 10: 
          paramParcel1.enforceInterface("android.service.notification.INotificationListener");
          onNotificationSnoozedUntilContext(IStatusBarNotificationHolder.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString());
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.service.notification.INotificationListener");
          onNotificationEnqueued(IStatusBarNotificationHolder.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.service.notification.INotificationListener");
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject5 = (NotificationChannelGroup)NotificationChannelGroup.CREATOR.createFromParcel(paramParcel1);
          }
          onNotificationChannelGroupModification((String)localObject1, paramParcel2, (NotificationChannelGroup)localObject5, paramParcel1.readInt());
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.service.notification.INotificationListener");
          str = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject5 = (NotificationChannel)NotificationChannel.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject5 = localObject1;
          }
          onNotificationChannelModification(str, paramParcel2, (NotificationChannel)localObject5, paramParcel1.readInt());
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.service.notification.INotificationListener");
          onInterruptionFilterChanged(paramParcel1.readInt());
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.service.notification.INotificationListener");
          onListenerHintsChanged(paramParcel1.readInt());
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.service.notification.INotificationListener");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (NotificationRankingUpdate)NotificationRankingUpdate.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          onNotificationRankingUpdate(paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.service.notification.INotificationListener");
          localObject1 = IStatusBarNotificationHolder.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel2 = (NotificationRankingUpdate)NotificationRankingUpdate.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel2 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject5 = (NotificationStats)NotificationStats.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject5 = str;
          }
          onNotificationRemoved((IStatusBarNotificationHolder)localObject1, paramParcel2, (NotificationStats)localObject5, paramParcel1.readInt());
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.service.notification.INotificationListener");
          paramParcel2 = IStatusBarNotificationHolder.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (NotificationRankingUpdate)NotificationRankingUpdate.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          onNotificationPosted(paramParcel2, paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.service.notification.INotificationListener");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (NotificationRankingUpdate)NotificationRankingUpdate.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject4;
        }
        onListenerConnected(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.service.notification.INotificationListener");
      return true;
    }
    
    private static class Proxy
      implements INotificationListener
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
        return "android.service.notification.INotificationListener";
      }
      
      public void onInterruptionFilterChanged(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.notification.INotificationListener");
          localParcel.writeInt(paramInt);
          mRemote.transact(6, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onListenerConnected(NotificationRankingUpdate paramNotificationRankingUpdate)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.notification.INotificationListener");
          if (paramNotificationRankingUpdate != null)
          {
            localParcel.writeInt(1);
            paramNotificationRankingUpdate.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(1, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onListenerHintsChanged(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.notification.INotificationListener");
          localParcel.writeInt(paramInt);
          mRemote.transact(5, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onNotificationChannelGroupModification(String paramString, UserHandle paramUserHandle, NotificationChannelGroup paramNotificationChannelGroup, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.notification.INotificationListener");
          localParcel.writeString(paramString);
          if (paramUserHandle != null)
          {
            localParcel.writeInt(1);
            paramUserHandle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramNotificationChannelGroup != null)
          {
            localParcel.writeInt(1);
            paramNotificationChannelGroup.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          mRemote.transact(8, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onNotificationChannelModification(String paramString, UserHandle paramUserHandle, NotificationChannel paramNotificationChannel, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.notification.INotificationListener");
          localParcel.writeString(paramString);
          if (paramUserHandle != null)
          {
            localParcel.writeInt(1);
            paramUserHandle.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramNotificationChannel != null)
          {
            localParcel.writeInt(1);
            paramNotificationChannel.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          mRemote.transact(7, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onNotificationEnqueued(IStatusBarNotificationHolder paramIStatusBarNotificationHolder)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.notification.INotificationListener");
          if (paramIStatusBarNotificationHolder != null) {
            paramIStatusBarNotificationHolder = paramIStatusBarNotificationHolder.asBinder();
          } else {
            paramIStatusBarNotificationHolder = null;
          }
          localParcel.writeStrongBinder(paramIStatusBarNotificationHolder);
          mRemote.transact(9, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onNotificationPosted(IStatusBarNotificationHolder paramIStatusBarNotificationHolder, NotificationRankingUpdate paramNotificationRankingUpdate)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.notification.INotificationListener");
          if (paramIStatusBarNotificationHolder != null) {
            paramIStatusBarNotificationHolder = paramIStatusBarNotificationHolder.asBinder();
          } else {
            paramIStatusBarNotificationHolder = null;
          }
          localParcel.writeStrongBinder(paramIStatusBarNotificationHolder);
          if (paramNotificationRankingUpdate != null)
          {
            localParcel.writeInt(1);
            paramNotificationRankingUpdate.writeToParcel(localParcel, 0);
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
      
      public void onNotificationRankingUpdate(NotificationRankingUpdate paramNotificationRankingUpdate)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.notification.INotificationListener");
          if (paramNotificationRankingUpdate != null)
          {
            localParcel.writeInt(1);
            paramNotificationRankingUpdate.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(4, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onNotificationRemoved(IStatusBarNotificationHolder paramIStatusBarNotificationHolder, NotificationRankingUpdate paramNotificationRankingUpdate, NotificationStats paramNotificationStats, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.notification.INotificationListener");
          if (paramIStatusBarNotificationHolder != null) {
            paramIStatusBarNotificationHolder = paramIStatusBarNotificationHolder.asBinder();
          } else {
            paramIStatusBarNotificationHolder = null;
          }
          localParcel.writeStrongBinder(paramIStatusBarNotificationHolder);
          if (paramNotificationRankingUpdate != null)
          {
            localParcel.writeInt(1);
            paramNotificationRankingUpdate.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          if (paramNotificationStats != null)
          {
            localParcel.writeInt(1);
            paramNotificationStats.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          localParcel.writeInt(paramInt);
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void onNotificationSnoozedUntilContext(IStatusBarNotificationHolder paramIStatusBarNotificationHolder, String paramString)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.service.notification.INotificationListener");
          if (paramIStatusBarNotificationHolder != null) {
            paramIStatusBarNotificationHolder = paramIStatusBarNotificationHolder.asBinder();
          } else {
            paramIStatusBarNotificationHolder = null;
          }
          localParcel.writeStrongBinder(paramIStatusBarNotificationHolder);
          localParcel.writeString(paramString);
          mRemote.transact(10, localParcel, null, 1);
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
