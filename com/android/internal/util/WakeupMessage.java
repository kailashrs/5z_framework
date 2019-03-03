package com.android.internal.util;

import android.app.AlarmManager;
import android.app.AlarmManager.OnAlarmListener;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.android.internal.annotations.VisibleForTesting;

public class WakeupMessage
  implements AlarmManager.OnAlarmListener
{
  private final AlarmManager mAlarmManager;
  @VisibleForTesting
  protected final int mArg1;
  @VisibleForTesting
  protected final int mArg2;
  @VisibleForTesting
  protected final int mCmd;
  @VisibleForTesting
  protected final String mCmdName;
  @VisibleForTesting
  protected final Handler mHandler;
  @VisibleForTesting
  protected final Object mObj;
  private final Runnable mRunnable;
  private boolean mScheduled;
  
  public WakeupMessage(Context paramContext, Handler paramHandler, String paramString, int paramInt)
  {
    this(paramContext, paramHandler, paramString, paramInt, 0, 0, null);
  }
  
  public WakeupMessage(Context paramContext, Handler paramHandler, String paramString, int paramInt1, int paramInt2)
  {
    this(paramContext, paramHandler, paramString, paramInt1, paramInt2, 0, null);
  }
  
  public WakeupMessage(Context paramContext, Handler paramHandler, String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    this(paramContext, paramHandler, paramString, paramInt1, paramInt2, paramInt3, null);
  }
  
  public WakeupMessage(Context paramContext, Handler paramHandler, String paramString, int paramInt1, int paramInt2, int paramInt3, Object paramObject)
  {
    mAlarmManager = getAlarmManager(paramContext);
    mHandler = paramHandler;
    mCmdName = paramString;
    mCmd = paramInt1;
    mArg1 = paramInt2;
    mArg2 = paramInt3;
    mObj = paramObject;
    mRunnable = null;
  }
  
  public WakeupMessage(Context paramContext, Handler paramHandler, String paramString, Runnable paramRunnable)
  {
    mAlarmManager = getAlarmManager(paramContext);
    mHandler = paramHandler;
    mCmdName = paramString;
    mCmd = 0;
    mArg1 = 0;
    mArg2 = 0;
    mObj = null;
    mRunnable = paramRunnable;
  }
  
  private static AlarmManager getAlarmManager(Context paramContext)
  {
    return (AlarmManager)paramContext.getSystemService("alarm");
  }
  
  public void cancel()
  {
    try
    {
      if (mScheduled)
      {
        mAlarmManager.cancel(this);
        mScheduled = false;
      }
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void onAlarm()
  {
    try
    {
      boolean bool = mScheduled;
      mScheduled = false;
      if (bool)
      {
        Message localMessage;
        if (mRunnable == null) {
          localMessage = mHandler.obtainMessage(mCmd, mArg1, mArg2, mObj);
        } else {
          localMessage = Message.obtain(mHandler, mRunnable);
        }
        mHandler.dispatchMessage(localMessage);
        localMessage.recycle();
      }
      return;
    }
    finally {}
  }
  
  public void schedule(long paramLong)
  {
    try
    {
      mAlarmManager.setExact(2, paramLong, mCmdName, this, mHandler);
      mScheduled = true;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
}
