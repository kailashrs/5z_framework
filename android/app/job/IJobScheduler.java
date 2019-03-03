package android.app.job;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

public abstract interface IJobScheduler
  extends IInterface
{
  public abstract void cancel(int paramInt)
    throws RemoteException;
  
  public abstract void cancelAll()
    throws RemoteException;
  
  public abstract int enqueue(JobInfo paramJobInfo, JobWorkItem paramJobWorkItem)
    throws RemoteException;
  
  public abstract List<JobInfo> getAllPendingJobs()
    throws RemoteException;
  
  public abstract JobInfo getPendingJob(int paramInt)
    throws RemoteException;
  
  public abstract int schedule(JobInfo paramJobInfo)
    throws RemoteException;
  
  public abstract int scheduleAsPackage(JobInfo paramJobInfo, String paramString1, int paramInt, String paramString2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IJobScheduler
  {
    private static final String DESCRIPTOR = "android.app.job.IJobScheduler";
    static final int TRANSACTION_cancel = 4;
    static final int TRANSACTION_cancelAll = 5;
    static final int TRANSACTION_enqueue = 2;
    static final int TRANSACTION_getAllPendingJobs = 6;
    static final int TRANSACTION_getPendingJob = 7;
    static final int TRANSACTION_schedule = 1;
    static final int TRANSACTION_scheduleAsPackage = 3;
    
    public Stub()
    {
      attachInterface(this, "android.app.job.IJobScheduler");
    }
    
    public static IJobScheduler asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.job.IJobScheduler");
      if ((localIInterface != null) && ((localIInterface instanceof IJobScheduler))) {
        return (IJobScheduler)localIInterface;
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
        JobInfo localJobInfo = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 7: 
          paramParcel1.enforceInterface("android.app.job.IJobScheduler");
          paramParcel1 = getPendingJob(paramParcel1.readInt());
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
        case 6: 
          paramParcel1.enforceInterface("android.app.job.IJobScheduler");
          paramParcel1 = getAllPendingJobs();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.app.job.IJobScheduler");
          cancelAll();
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.app.job.IJobScheduler");
          cancel(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.app.job.IJobScheduler");
          if (paramParcel1.readInt() != 0) {
            localJobInfo = (JobInfo)JobInfo.CREATOR.createFromParcel(paramParcel1);
          }
          paramInt1 = scheduleAsPackage(localJobInfo, paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.app.job.IJobScheduler");
          if (paramParcel1.readInt() != 0) {
            localJobInfo = (JobInfo)JobInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            localJobInfo = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (JobWorkItem)JobWorkItem.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          paramInt1 = enqueue(localJobInfo, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.app.job.IJobScheduler");
        if (paramParcel1.readInt() != 0) {
          paramParcel1 = (JobInfo)JobInfo.CREATOR.createFromParcel(paramParcel1);
        } else {
          paramParcel1 = localObject2;
        }
        paramInt1 = schedule(paramParcel1);
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.app.job.IJobScheduler");
      return true;
    }
    
    private static class Proxy
      implements IJobScheduler
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
      
      public void cancel(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.job.IJobScheduler");
          localParcel1.writeInt(paramInt);
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
      
      public void cancelAll()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.job.IJobScheduler");
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
      
      public int enqueue(JobInfo paramJobInfo, JobWorkItem paramJobWorkItem)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.job.IJobScheduler");
          if (paramJobInfo != null)
          {
            localParcel1.writeInt(1);
            paramJobInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramJobWorkItem != null)
          {
            localParcel1.writeInt(1);
            paramJobWorkItem.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(2, localParcel1, localParcel2, 0);
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
      
      public List<JobInfo> getAllPendingJobs()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.job.IJobScheduler");
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(JobInfo.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.app.job.IJobScheduler";
      }
      
      public JobInfo getPendingJob(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.job.IJobScheduler");
          localParcel1.writeInt(paramInt);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          JobInfo localJobInfo;
          if (localParcel2.readInt() != 0) {
            localJobInfo = (JobInfo)JobInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localJobInfo = null;
          }
          return localJobInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int schedule(JobInfo paramJobInfo)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.job.IJobScheduler");
          if (paramJobInfo != null)
          {
            localParcel1.writeInt(1);
            paramJobInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(1, localParcel1, localParcel2, 0);
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
      
      public int scheduleAsPackage(JobInfo paramJobInfo, String paramString1, int paramInt, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.job.IJobScheduler");
          if (paramJobInfo != null)
          {
            localParcel1.writeInt(1);
            paramJobInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString2);
          mRemote.transact(3, localParcel1, localParcel2, 0);
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
    }
  }
}
