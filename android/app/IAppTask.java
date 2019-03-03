package android.app;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IAppTask
  extends IInterface
{
  public abstract void finishAndRemoveTask()
    throws RemoteException;
  
  public abstract ActivityManager.RecentTaskInfo getTaskInfo()
    throws RemoteException;
  
  public abstract void moveToFront()
    throws RemoteException;
  
  public abstract void setExcludeFromRecents(boolean paramBoolean)
    throws RemoteException;
  
  public abstract int startActivity(IBinder paramIBinder, String paramString1, Intent paramIntent, String paramString2, Bundle paramBundle)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IAppTask
  {
    private static final String DESCRIPTOR = "android.app.IAppTask";
    static final int TRANSACTION_finishAndRemoveTask = 1;
    static final int TRANSACTION_getTaskInfo = 2;
    static final int TRANSACTION_moveToFront = 3;
    static final int TRANSACTION_setExcludeFromRecents = 5;
    static final int TRANSACTION_startActivity = 4;
    
    public Stub()
    {
      attachInterface(this, "android.app.IAppTask");
    }
    
    public static IAppTask asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.IAppTask");
      if ((localIInterface != null) && ((localIInterface instanceof IAppTask))) {
        return (IAppTask)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 5: 
          paramParcel1.enforceInterface("android.app.IAppTask");
          if (paramParcel1.readInt() != 0) {
            bool = true;
          }
          setExcludeFromRecents(bool);
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.app.IAppTask");
          IBinder localIBinder = paramParcel1.readStrongBinder();
          String str1 = paramParcel1.readString();
          Intent localIntent;
          if (paramParcel1.readInt() != 0) {
            localIntent = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localIntent = null;
          }
          String str2 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = null;
          }
          paramInt1 = startActivity(localIBinder, str1, localIntent, str2, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.app.IAppTask");
          moveToFront();
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.app.IAppTask");
          paramParcel1 = getTaskInfo();
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
        paramParcel1.enforceInterface("android.app.IAppTask");
        finishAndRemoveTask();
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.app.IAppTask");
      return true;
    }
    
    private static class Proxy
      implements IAppTask
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
      
      public void finishAndRemoveTask()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IAppTask");
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
      
      public String getInterfaceDescriptor()
      {
        return "android.app.IAppTask";
      }
      
      public ActivityManager.RecentTaskInfo getTaskInfo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IAppTask");
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ActivityManager.RecentTaskInfo localRecentTaskInfo;
          if (localParcel2.readInt() != 0) {
            localRecentTaskInfo = (ActivityManager.RecentTaskInfo)ActivityManager.RecentTaskInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localRecentTaskInfo = null;
          }
          return localRecentTaskInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void moveToFront()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IAppTask");
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
      
      public void setExcludeFromRecents(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IAppTask");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int startActivity(IBinder paramIBinder, String paramString1, Intent paramIntent, String paramString2, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IAppTask");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString1);
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString2);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
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
