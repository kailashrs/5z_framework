package android.app.job;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IJobService
  extends IInterface
{
  public abstract void startJob(JobParameters paramJobParameters)
    throws RemoteException;
  
  public abstract void stopJob(JobParameters paramJobParameters)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IJobService
  {
    private static final String DESCRIPTOR = "android.app.job.IJobService";
    static final int TRANSACTION_startJob = 1;
    static final int TRANSACTION_stopJob = 2;
    
    public Stub()
    {
      attachInterface(this, "android.app.job.IJobService");
    }
    
    public static IJobService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.job.IJobService");
      if ((localIInterface != null) && ((localIInterface instanceof IJobService))) {
        return (IJobService)localIInterface;
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
        case 2: 
          paramParcel1.enforceInterface("android.app.job.IJobService");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (JobParameters)JobParameters.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          stopJob(paramParcel1);
          return true;
        }
        paramParcel1.enforceInterface("android.app.job.IJobService");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (JobParameters)JobParameters.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject1;
        }
        startJob(paramParcel1);
        return true;
      }
      paramParcel2.writeString("android.app.job.IJobService");
      return true;
    }
    
    private static class Proxy
      implements IJobService
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
        return "android.app.job.IJobService";
      }
      
      public void startJob(JobParameters paramJobParameters)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.job.IJobService");
          if (paramJobParameters != null)
          {
            localParcel.writeInt(1);
            paramJobParameters.writeToParcel(localParcel, 0);
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
      
      public void stopJob(JobParameters paramJobParameters)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.app.job.IJobService");
          if (paramJobParameters != null)
          {
            localParcel.writeInt(1);
            paramJobParameters.writeToParcel(localParcel, 0);
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
    }
  }
}
