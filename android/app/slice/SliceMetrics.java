package android.app.slice;

import android.content.Context;
import android.metrics.LogMaker;
import android.net.Uri;
import com.android.internal.logging.MetricsLogger;

public class SliceMetrics
{
  private static final String TAG = "SliceMetrics";
  private LogMaker mLogMaker = new LogMaker(0);
  private MetricsLogger mMetricsLogger = new MetricsLogger();
  
  public SliceMetrics(Context paramContext, Uri paramUri)
  {
    mLogMaker.addTaggedData(1402, paramUri.getAuthority());
    mLogMaker.addTaggedData(1403, paramUri.getPath());
  }
  
  public void logHidden()
  {
    synchronized (mLogMaker)
    {
      mLogMaker.setCategory(1401).setType(2);
      mMetricsLogger.write(mLogMaker);
      return;
    }
  }
  
  public void logTouch(int paramInt, Uri paramUri)
  {
    synchronized (mLogMaker)
    {
      mLogMaker.setCategory(1401).setType(4).addTaggedData(1404, paramUri.getAuthority()).addTaggedData(1405, paramUri.getPath());
      mMetricsLogger.write(mLogMaker);
      return;
    }
  }
  
  public void logVisible()
  {
    synchronized (mLogMaker)
    {
      mLogMaker.setCategory(1401).setType(1);
      mMetricsLogger.write(mLogMaker);
      return;
    }
  }
}
