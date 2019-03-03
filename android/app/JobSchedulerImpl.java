package android.app;

import android.app.job.IJobScheduler;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.job.JobWorkItem;
import android.os.RemoteException;
import java.util.List;

public class JobSchedulerImpl
  extends JobScheduler
{
  IJobScheduler mBinder;
  
  JobSchedulerImpl(IJobScheduler paramIJobScheduler)
  {
    mBinder = paramIJobScheduler;
  }
  
  public void cancel(int paramInt)
  {
    try
    {
      mBinder.cancel(paramInt);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void cancelAll()
  {
    try
    {
      mBinder.cancelAll();
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public int enqueue(JobInfo paramJobInfo, JobWorkItem paramJobWorkItem)
  {
    try
    {
      int i = mBinder.enqueue(paramJobInfo, paramJobWorkItem);
      return i;
    }
    catch (RemoteException paramJobInfo) {}
    return 0;
  }
  
  public List<JobInfo> getAllPendingJobs()
  {
    try
    {
      List localList = mBinder.getAllPendingJobs();
      return localList;
    }
    catch (RemoteException localRemoteException) {}
    return null;
  }
  
  public JobInfo getPendingJob(int paramInt)
  {
    try
    {
      JobInfo localJobInfo = mBinder.getPendingJob(paramInt);
      return localJobInfo;
    }
    catch (RemoteException localRemoteException) {}
    return null;
  }
  
  public int schedule(JobInfo paramJobInfo)
  {
    try
    {
      int i = mBinder.schedule(paramJobInfo);
      return i;
    }
    catch (RemoteException paramJobInfo) {}
    return 0;
  }
  
  public int scheduleAsPackage(JobInfo paramJobInfo, String paramString1, int paramInt, String paramString2)
  {
    try
    {
      paramInt = mBinder.scheduleAsPackage(paramJobInfo, paramString1, paramInt, paramString2);
      return paramInt;
    }
    catch (RemoteException paramJobInfo) {}
    return 0;
  }
}
