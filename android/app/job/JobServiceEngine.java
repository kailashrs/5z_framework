package android.app.job;

import android.app.Service;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import java.lang.ref.WeakReference;

public abstract class JobServiceEngine
{
  private static final int MSG_EXECUTE_JOB = 0;
  private static final int MSG_JOB_FINISHED = 2;
  private static final int MSG_STOP_JOB = 1;
  private static final String TAG = "JobServiceEngine";
  private final IJobService mBinder = new JobInterface(this);
  JobHandler mHandler;
  
  public JobServiceEngine(Service paramService)
  {
    mHandler = new JobHandler(paramService.getMainLooper());
  }
  
  public final IBinder getBinder()
  {
    return mBinder.asBinder();
  }
  
  public void jobFinished(JobParameters paramJobParameters, boolean paramBoolean)
  {
    if (paramJobParameters != null)
    {
      paramJobParameters = Message.obtain(mHandler, 2, paramJobParameters);
      arg2 = paramBoolean;
      paramJobParameters.sendToTarget();
      return;
    }
    throw new NullPointerException("params");
  }
  
  public abstract boolean onStartJob(JobParameters paramJobParameters);
  
  public abstract boolean onStopJob(JobParameters paramJobParameters);
  
  class JobHandler
    extends Handler
  {
    JobHandler(Looper paramLooper)
    {
      super();
    }
    
    private void ackStartMessage(JobParameters paramJobParameters, boolean paramBoolean)
    {
      IJobCallback localIJobCallback = paramJobParameters.getCallback();
      int i = paramJobParameters.getJobId();
      if (localIJobCallback != null) {
        try
        {
          localIJobCallback.acknowledgeStartMessage(i, paramBoolean);
        }
        catch (RemoteException paramJobParameters)
        {
          for (;;)
          {
            Log.e("JobServiceEngine", "System unreachable for starting job.");
          }
        }
      } else if (Log.isLoggable("JobServiceEngine", 3)) {
        Log.d("JobServiceEngine", "Attempting to ack a job that has already been processed.");
      }
    }
    
    private void ackStopMessage(JobParameters paramJobParameters, boolean paramBoolean)
    {
      IJobCallback localIJobCallback = paramJobParameters.getCallback();
      int i = paramJobParameters.getJobId();
      if (localIJobCallback != null) {
        try
        {
          localIJobCallback.acknowledgeStopMessage(i, paramBoolean);
        }
        catch (RemoteException paramJobParameters)
        {
          for (;;)
          {
            Log.e("JobServiceEngine", "System unreachable for stopping job.");
          }
        }
      } else if (Log.isLoggable("JobServiceEngine", 3)) {
        Log.d("JobServiceEngine", "Attempting to ack a job that has already been processed.");
      }
    }
    
    public void handleMessage(Message paramMessage)
    {
      JobParameters localJobParameters = (JobParameters)obj;
      switch (what)
      {
      default: 
        Log.e("JobServiceEngine", "Unrecognised message received.");
        break;
      case 2: 
        int i = arg2;
        boolean bool = true;
        if (i != 1) {
          bool = false;
        }
        paramMessage = localJobParameters.getCallback();
        if (paramMessage != null) {
          try
          {
            paramMessage.jobFinished(localJobParameters.getJobId(), bool);
          }
          catch (RemoteException paramMessage)
          {
            Log.e("JobServiceEngine", "Error reporting job finish to system: binder has goneaway.");
          }
        } else {
          Log.e("JobServiceEngine", "finishJob() called for a nonexistent job id.");
        }
        break;
      case 1: 
        try
        {
          ackStopMessage(localJobParameters, onStopJob(localJobParameters));
        }
        catch (Exception paramMessage)
        {
          Log.e("JobServiceEngine", "Application unable to handle onStopJob.", paramMessage);
          throw new RuntimeException(paramMessage);
        }
      case 0: 
        try
        {
          ackStartMessage(localJobParameters, onStartJob(localJobParameters));
        }
        catch (Exception paramMessage)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Error while executing job: ");
          localStringBuilder.append(localJobParameters.getJobId());
          Log.e("JobServiceEngine", localStringBuilder.toString());
          throw new RuntimeException(paramMessage);
        }
      }
    }
  }
  
  static final class JobInterface
    extends IJobService.Stub
  {
    final WeakReference<JobServiceEngine> mService;
    
    JobInterface(JobServiceEngine paramJobServiceEngine)
    {
      mService = new WeakReference(paramJobServiceEngine);
    }
    
    public void startJob(JobParameters paramJobParameters)
      throws RemoteException
    {
      JobServiceEngine localJobServiceEngine = (JobServiceEngine)mService.get();
      if (localJobServiceEngine != null) {
        Message.obtain(mHandler, 0, paramJobParameters).sendToTarget();
      }
    }
    
    public void stopJob(JobParameters paramJobParameters)
      throws RemoteException
    {
      JobServiceEngine localJobServiceEngine = (JobServiceEngine)mService.get();
      if (localJobServiceEngine != null) {
        Message.obtain(mHandler, 1, paramJobParameters).sendToTarget();
      }
    }
  }
}
