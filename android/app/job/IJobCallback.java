package android.app.job;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IJobCallback
  extends IInterface
{
  public abstract void acknowledgeStartMessage(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void acknowledgeStopMessage(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean completeWork(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract JobWorkItem dequeueWork(int paramInt)
    throws RemoteException;
  
  public abstract void jobFinished(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IJobCallback
  {
    private static final String DESCRIPTOR = "android.app.job.IJobCallback";
    static final int TRANSACTION_acknowledgeStartMessage = 1;
    static final int TRANSACTION_acknowledgeStopMessage = 2;
    static final int TRANSACTION_completeWork = 4;
    static final int TRANSACTION_dequeueWork = 3;
    static final int TRANSACTION_jobFinished = 5;
    
    public Stub()
    {
      attachInterface(this, "android.app.job.IJobCallback");
    }
    
    public static IJobCallback asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.job.IJobCallback");
      if ((localIInterface != null) && ((localIInterface instanceof IJobCallback))) {
        return (IJobCallback)localIInterface;
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
        boolean bool3 = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 5: 
          paramParcel1.enforceInterface("android.app.job.IJobCallback");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          jobFinished(paramInt1, bool3);
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.app.job.IJobCallback");
          paramInt1 = completeWork(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.app.job.IJobCallback");
          paramParcel1 = dequeueWork(paramParcel1.readInt());
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
          paramParcel1.enforceInterface("android.app.job.IJobCallback");
          paramInt1 = paramParcel1.readInt();
          bool3 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool3 = true;
          }
          acknowledgeStopMessage(paramInt1, bool3);
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.app.job.IJobCallback");
        paramInt1 = paramParcel1.readInt();
        bool3 = bool2;
        if (paramParcel1.readInt() != 0) {
          bool3 = true;
        }
        acknowledgeStartMessage(paramInt1, bool3);
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.app.job.IJobCallback");
      return true;
    }
    
    private static class Proxy
      implements IJobCallback
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void acknowledgeStartMessage(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.job.IJobCallback");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
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
      
      public void acknowledgeStopMessage(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.job.IJobCallback");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public boolean completeWork(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.job.IJobCallback");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 != 0) {
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
      
      public JobWorkItem dequeueWork(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.job.IJobCallback");
          localParcel1.writeInt(paramInt);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          JobWorkItem localJobWorkItem;
          if (localParcel2.readInt() != 0) {
            localJobWorkItem = (JobWorkItem)JobWorkItem.CREATOR.createFromParcel(localParcel2);
          } else {
            localJobWorkItem = null;
          }
          return localJobWorkItem;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.app.job.IJobCallback";
      }
      
      public void jobFinished(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.job.IJobCallback");
          localParcel1.writeInt(paramInt);
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
    }
  }
}
