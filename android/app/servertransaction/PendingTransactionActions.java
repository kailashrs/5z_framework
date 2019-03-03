package android.app.servertransaction;

import android.app.ActivityManager;
import android.app.ActivityThread.ActivityClientRecord;
import android.app.IActivityManager;
import android.app.LoadedApk;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.TransactionTooLargeException;
import android.util.Log;
import android.util.LogWriter;
import com.android.internal.util.IndentingPrintWriter;

public class PendingTransactionActions
{
  private boolean mCallOnPostCreate;
  private Bundle mOldState;
  private boolean mReportRelaunchToWM;
  private boolean mRestoreInstanceState;
  private StopInfo mStopInfo;
  
  public PendingTransactionActions()
  {
    clear();
  }
  
  public void clear()
  {
    mRestoreInstanceState = false;
    mCallOnPostCreate = false;
    mOldState = null;
    mStopInfo = null;
  }
  
  public Bundle getOldState()
  {
    return mOldState;
  }
  
  public StopInfo getStopInfo()
  {
    return mStopInfo;
  }
  
  public void setCallOnPostCreate(boolean paramBoolean)
  {
    mCallOnPostCreate = paramBoolean;
  }
  
  public void setOldState(Bundle paramBundle)
  {
    mOldState = paramBundle;
  }
  
  public void setReportRelaunchToWindowManager(boolean paramBoolean)
  {
    mReportRelaunchToWM = paramBoolean;
  }
  
  public void setRestoreInstanceState(boolean paramBoolean)
  {
    mRestoreInstanceState = paramBoolean;
  }
  
  public void setStopInfo(StopInfo paramStopInfo)
  {
    mStopInfo = paramStopInfo;
  }
  
  public boolean shouldCallOnPostCreate()
  {
    return mCallOnPostCreate;
  }
  
  public boolean shouldReportRelaunchToWindowManager()
  {
    return mReportRelaunchToWM;
  }
  
  public boolean shouldRestoreInstanceState()
  {
    return mRestoreInstanceState;
  }
  
  public static class StopInfo
    implements Runnable
  {
    private static final String TAG = "ActivityStopInfo";
    private ActivityThread.ActivityClientRecord mActivity;
    private CharSequence mDescription;
    private PersistableBundle mPersistentState;
    private Bundle mState;
    
    public StopInfo() {}
    
    public void run()
    {
      try
      {
        ActivityManager.getService().activityStopped(mActivity.token, mState, mPersistentState, mDescription);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        IndentingPrintWriter localIndentingPrintWriter = new IndentingPrintWriter(new LogWriter(5, "ActivityStopInfo"), "  ");
        localIndentingPrintWriter.println("Bundle stats:");
        Bundle.dumpStats(localIndentingPrintWriter, mState);
        localIndentingPrintWriter.println("PersistableBundle stats:");
        Bundle.dumpStats(localIndentingPrintWriter, mPersistentState);
        if (((localRemoteException instanceof TransactionTooLargeException)) && (mActivity.packageInfo.getTargetSdkVersion() < 24))
        {
          Log.e("ActivityStopInfo", "App sent too much data in instance state, so it was ignored", localRemoteException);
          return;
        }
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    
    public void setActivity(ActivityThread.ActivityClientRecord paramActivityClientRecord)
    {
      mActivity = paramActivityClientRecord;
    }
    
    public void setDescription(CharSequence paramCharSequence)
    {
      mDescription = paramCharSequence;
    }
    
    public void setPersistentState(PersistableBundle paramPersistableBundle)
    {
      mPersistentState = paramPersistableBundle;
    }
    
    public void setState(Bundle paramBundle)
    {
      mState = paramBundle;
    }
  }
}
