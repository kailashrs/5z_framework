package android.app;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IActivityController
  extends IInterface
{
  public abstract boolean activityResuming(String paramString)
    throws RemoteException;
  
  public abstract boolean activityStarting(Intent paramIntent, String paramString)
    throws RemoteException;
  
  public abstract boolean appCrashed(String paramString1, int paramInt, String paramString2, String paramString3, long paramLong, String paramString4)
    throws RemoteException;
  
  public abstract int appEarlyNotResponding(String paramString1, int paramInt, String paramString2)
    throws RemoteException;
  
  public abstract int appNotResponding(String paramString1, int paramInt, String paramString2)
    throws RemoteException;
  
  public abstract int systemNotResponding(String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IActivityController
  {
    private static final String DESCRIPTOR = "android.app.IActivityController";
    static final int TRANSACTION_activityResuming = 2;
    static final int TRANSACTION_activityStarting = 1;
    static final int TRANSACTION_appCrashed = 3;
    static final int TRANSACTION_appEarlyNotResponding = 4;
    static final int TRANSACTION_appNotResponding = 5;
    static final int TRANSACTION_systemNotResponding = 6;
    
    public Stub()
    {
      attachInterface(this, "android.app.IActivityController");
    }
    
    public static IActivityController asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.IActivityController");
      if ((localIInterface != null) && ((localIInterface instanceof IActivityController))) {
        return (IActivityController)localIInterface;
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
        case 6: 
          paramParcel1.enforceInterface("android.app.IActivityController");
          paramInt1 = systemNotResponding(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.app.IActivityController");
          paramInt1 = appNotResponding(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.app.IActivityController");
          paramInt1 = appEarlyNotResponding(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.app.IActivityController");
          paramInt1 = appCrashed(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readLong(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.app.IActivityController");
          paramInt1 = activityResuming(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.app.IActivityController");
        Intent localIntent;
        if (paramParcel1.readInt() != 0) {
          localIntent = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
        } else {
          localIntent = null;
        }
        paramInt1 = activityStarting(localIntent, paramParcel1.readString());
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.app.IActivityController");
      return true;
    }
    
    private static class Proxy
      implements IActivityController
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public boolean activityResuming(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityController");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(2, localParcel1, localParcel2, 0);
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
      
      public boolean activityStarting(Intent paramIntent, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityController");
          boolean bool = true;
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(1, localParcel1, localParcel2, 0);
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
      
      public boolean appCrashed(String paramString1, int paramInt, String paramString2, String paramString3, long paramLong, String paramString4)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityController");
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          localParcel1.writeLong(paramLong);
          localParcel1.writeString(paramString4);
          paramString1 = mRemote;
          boolean bool = false;
          paramString1.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
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
      
      public int appEarlyNotResponding(String paramString1, int paramInt, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityController");
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString2);
          mRemote.transact(4, localParcel1, localParcel2, 0);
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
      
      public int appNotResponding(String paramString1, int paramInt, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityController");
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString2);
          mRemote.transact(5, localParcel1, localParcel2, 0);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.app.IActivityController";
      }
      
      public int systemNotResponding(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.IActivityController");
          localParcel1.writeString(paramString);
          mRemote.transact(6, localParcel1, localParcel2, 0);
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
