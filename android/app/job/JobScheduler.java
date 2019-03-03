package android.app.job;

import android.annotation.SystemApi;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public abstract class JobScheduler
{
  public static final int RESULT_FAILURE = 0;
  public static final int RESULT_SUCCESS = 1;
  
  public JobScheduler() {}
  
  public abstract void cancel(int paramInt);
  
  public abstract void cancelAll();
  
  public abstract int enqueue(JobInfo paramJobInfo, JobWorkItem paramJobWorkItem);
  
  public abstract List<JobInfo> getAllPendingJobs();
  
  public abstract JobInfo getPendingJob(int paramInt);
  
  public abstract int schedule(JobInfo paramJobInfo);
  
  @SystemApi
  public abstract int scheduleAsPackage(JobInfo paramJobInfo, String paramString1, int paramInt, String paramString2);
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Result {}
}
