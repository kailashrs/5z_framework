package android.app.job;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public abstract class JobService
  extends Service
{
  public static final String PERMISSION_BIND = "android.permission.BIND_JOB_SERVICE";
  private static final String TAG = "JobService";
  private JobServiceEngine mEngine;
  
  public JobService() {}
  
  public final void jobFinished(JobParameters paramJobParameters, boolean paramBoolean)
  {
    mEngine.jobFinished(paramJobParameters, paramBoolean);
  }
  
  public final IBinder onBind(Intent paramIntent)
  {
    if (mEngine == null) {
      mEngine = new JobServiceEngine(this)
      {
        public boolean onStartJob(JobParameters paramAnonymousJobParameters)
        {
          return JobService.this.onStartJob(paramAnonymousJobParameters);
        }
        
        public boolean onStopJob(JobParameters paramAnonymousJobParameters)
        {
          return JobService.this.onStopJob(paramAnonymousJobParameters);
        }
      };
    }
    return mEngine.getBinder();
  }
  
  public abstract boolean onStartJob(JobParameters paramJobParameters);
  
  public abstract boolean onStopJob(JobParameters paramJobParameters);
}
