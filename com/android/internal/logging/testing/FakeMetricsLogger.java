package com.android.internal.logging.testing;

import android.metrics.LogMaker;
import com.android.internal.logging.MetricsLogger;
import java.util.LinkedList;
import java.util.Queue;

public class FakeMetricsLogger
  extends MetricsLogger
{
  private Queue<LogMaker> logs = new LinkedList();
  
  public FakeMetricsLogger() {}
  
  public Queue<LogMaker> getLogs()
  {
    return logs;
  }
  
  public void reset()
  {
    logs.clear();
  }
  
  protected void saveLog(Object[] paramArrayOfObject)
  {
    logs.offer(new LogMaker(paramArrayOfObject));
  }
}
