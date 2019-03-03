package android.app;

import android.annotation.SystemApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.WorkSource;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.proto.ProtoOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import libcore.util.ZoneInfoDB;
import libcore.util.ZoneInfoDB.TzData;

public class AlarmManager
{
  public static final String ACTION_NEXT_ALARM_CLOCK_CHANGED = "android.app.action.NEXT_ALARM_CLOCK_CHANGED";
  public static final int ELAPSED_REALTIME = 3;
  public static final int ELAPSED_REALTIME_WAKEUP = 2;
  public static final int FLAG_ALLOW_WHILE_IDLE = 4;
  public static final int FLAG_ALLOW_WHILE_IDLE_UNRESTRICTED = 8;
  public static final int FLAG_IDLE_UNTIL = 16;
  public static final int FLAG_STANDALONE = 1;
  public static final int FLAG_WAKE_FROM_IDLE = 2;
  public static final long INTERVAL_DAY = 86400000L;
  public static final long INTERVAL_FIFTEEN_MINUTES = 900000L;
  public static final long INTERVAL_HALF_DAY = 43200000L;
  public static final long INTERVAL_HALF_HOUR = 1800000L;
  public static final long INTERVAL_HOUR = 3600000L;
  public static final int RTC = 1;
  public static final int RTC_WAKEUP = 0;
  private static final String TAG = "AlarmManager";
  public static final long WINDOW_EXACT = 0L;
  public static final long WINDOW_HEURISTIC = -1L;
  private static ArrayMap<OnAlarmListener, ListenerWrapper> sWrappers;
  private final boolean mAlwaysExact;
  private final Context mContext;
  private final Handler mMainThreadHandler;
  private final String mPackageName;
  private final IAlarmManager mService;
  private final int mTargetSdkVersion;
  
  AlarmManager(IAlarmManager paramIAlarmManager, Context paramContext)
  {
    mService = paramIAlarmManager;
    mContext = paramContext;
    mPackageName = paramContext.getPackageName();
    mTargetSdkVersion = getApplicationInfotargetSdkVersion;
    boolean bool;
    if (mTargetSdkVersion < 19) {
      bool = true;
    } else {
      bool = false;
    }
    mAlwaysExact = bool;
    mMainThreadHandler = new Handler(paramContext.getMainLooper());
  }
  
  private long legacyExactLength()
  {
    long l;
    if (mAlwaysExact) {
      l = 0L;
    } else {
      l = -1L;
    }
    return l;
  }
  
  private void setImpl(int paramInt1, long paramLong1, long paramLong2, long paramLong3, int paramInt2, PendingIntent paramPendingIntent, OnAlarmListener paramOnAlarmListener, String paramString, Handler paramHandler, WorkSource paramWorkSource, AlarmClockInfo paramAlarmClockInfo)
  {
    if (paramLong1 < 0L) {
      paramLong1 = 0L;
    }
    Object localObject = null;
    if (paramOnAlarmListener != null) {
      try
      {
        if (sWrappers == null)
        {
          localObject = new android/util/ArrayMap;
          ((ArrayMap)localObject).<init>();
          sWrappers = (ArrayMap)localObject;
        }
        ListenerWrapper localListenerWrapper = (ListenerWrapper)sWrappers.get(paramOnAlarmListener);
        localObject = localListenerWrapper;
        if (localListenerWrapper == null)
        {
          localObject = new android/app/AlarmManager$ListenerWrapper;
          ((ListenerWrapper)localObject).<init>(this, paramOnAlarmListener);
          sWrappers.put(paramOnAlarmListener, localObject);
        }
        if (paramHandler != null) {
          paramOnAlarmListener = paramHandler;
        } else {
          paramOnAlarmListener = mMainThreadHandler;
        }
        ((ListenerWrapper)localObject).setHandler(paramOnAlarmListener);
      }
      finally {}
    }
    try
    {
      mService.set(mPackageName, paramInt1, paramLong1, paramLong2, paramLong3, paramInt2, paramPendingIntent, (IAlarmListener)localObject, paramString, paramWorkSource, paramAlarmClockInfo);
      return;
    }
    catch (RemoteException paramPendingIntent)
    {
      throw paramPendingIntent.rethrowFromSystemServer();
    }
  }
  
  public void cancel(OnAlarmListener paramOnAlarmListener)
  {
    if (paramOnAlarmListener != null)
    {
      Object localObject = null;
      try
      {
        if (sWrappers != null) {
          localObject = (ListenerWrapper)sWrappers.get(paramOnAlarmListener);
        }
        if (localObject == null)
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Unrecognized alarm listener ");
          ((StringBuilder)localObject).append(paramOnAlarmListener);
          Log.w("AlarmManager", ((StringBuilder)localObject).toString());
          return;
        }
        ((ListenerWrapper)localObject).cancel();
        return;
      }
      finally {}
    }
    throw new NullPointerException("cancel() called with a null OnAlarmListener");
  }
  
  public void cancel(PendingIntent paramPendingIntent)
  {
    if (paramPendingIntent == null)
    {
      if (mTargetSdkVersion < 24)
      {
        Log.e("AlarmManager", "cancel() called with a null PendingIntent");
        return;
      }
      throw new NullPointerException("cancel() called with a null PendingIntent");
    }
    try
    {
      mService.remove(paramPendingIntent, null);
      return;
    }
    catch (RemoteException paramPendingIntent)
    {
      throw paramPendingIntent.rethrowFromSystemServer();
    }
  }
  
  public AlarmClockInfo getNextAlarmClock()
  {
    return getNextAlarmClock(mContext.getUserId());
  }
  
  public AlarmClockInfo getNextAlarmClock(int paramInt)
  {
    try
    {
      AlarmClockInfo localAlarmClockInfo = mService.getNextAlarmClock(paramInt);
      return localAlarmClockInfo;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public long getNextWakeFromIdleTime()
  {
    try
    {
      long l = mService.getNextWakeFromIdleTime();
      return l;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void set(int paramInt, long paramLong1, long paramLong2, long paramLong3, OnAlarmListener paramOnAlarmListener, Handler paramHandler, WorkSource paramWorkSource)
  {
    setImpl(paramInt, paramLong1, paramLong2, paramLong3, 0, null, paramOnAlarmListener, null, paramHandler, paramWorkSource, null);
  }
  
  @SystemApi
  public void set(int paramInt, long paramLong1, long paramLong2, long paramLong3, PendingIntent paramPendingIntent, WorkSource paramWorkSource)
  {
    setImpl(paramInt, paramLong1, paramLong2, paramLong3, 0, paramPendingIntent, null, null, null, paramWorkSource, null);
  }
  
  public void set(int paramInt, long paramLong1, long paramLong2, long paramLong3, String paramString, OnAlarmListener paramOnAlarmListener, Handler paramHandler, WorkSource paramWorkSource)
  {
    setImpl(paramInt, paramLong1, paramLong2, paramLong3, 0, null, paramOnAlarmListener, paramString, paramHandler, paramWorkSource, null);
  }
  
  public void set(int paramInt, long paramLong, PendingIntent paramPendingIntent)
  {
    setImpl(paramInt, paramLong, legacyExactLength(), 0L, 0, paramPendingIntent, null, null, null, null, null);
  }
  
  public void set(int paramInt, long paramLong, String paramString, OnAlarmListener paramOnAlarmListener, Handler paramHandler)
  {
    setImpl(paramInt, paramLong, legacyExactLength(), 0L, 0, null, paramOnAlarmListener, paramString, paramHandler, null, null);
  }
  
  public void setAlarmClock(AlarmClockInfo paramAlarmClockInfo, PendingIntent paramPendingIntent)
  {
    setImpl(0, paramAlarmClockInfo.getTriggerTime(), 0L, 0L, 0, paramPendingIntent, null, null, null, null, paramAlarmClockInfo);
  }
  
  public void setAndAllowWhileIdle(int paramInt, long paramLong, PendingIntent paramPendingIntent)
  {
    setImpl(paramInt, paramLong, -1L, 0L, 4, paramPendingIntent, null, null, null, null, null);
  }
  
  public void setExact(int paramInt, long paramLong, PendingIntent paramPendingIntent)
  {
    setImpl(paramInt, paramLong, 0L, 0L, 0, paramPendingIntent, null, null, null, null, null);
  }
  
  public void setExact(int paramInt, long paramLong, String paramString, OnAlarmListener paramOnAlarmListener, Handler paramHandler)
  {
    setImpl(paramInt, paramLong, 0L, 0L, 0, null, paramOnAlarmListener, paramString, paramHandler, null, null);
  }
  
  public void setExactAndAllowWhileIdle(int paramInt, long paramLong, PendingIntent paramPendingIntent)
  {
    setImpl(paramInt, paramLong, 0L, 0L, 4, paramPendingIntent, null, null, null, null, null);
  }
  
  public void setIdleUntil(int paramInt, long paramLong, String paramString, OnAlarmListener paramOnAlarmListener, Handler paramHandler)
  {
    setImpl(paramInt, paramLong, 0L, 0L, 16, null, paramOnAlarmListener, paramString, paramHandler, null, null);
  }
  
  public void setInexactRepeating(int paramInt, long paramLong1, long paramLong2, PendingIntent paramPendingIntent)
  {
    setImpl(paramInt, paramLong1, -1L, paramLong2, 0, paramPendingIntent, null, null, null, null, null);
  }
  
  public void setRepeating(int paramInt, long paramLong1, long paramLong2, PendingIntent paramPendingIntent)
  {
    setImpl(paramInt, paramLong1, legacyExactLength(), paramLong2, 0, paramPendingIntent, null, null, null, null, null);
  }
  
  public void setTime(long paramLong)
  {
    try
    {
      mService.setTime(paramLong);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setTimeZone(String paramString)
  {
    if (TextUtils.isEmpty(paramString)) {
      return;
    }
    if (mTargetSdkVersion >= 23)
    {
      int i = 0;
      try
      {
        boolean bool = ZoneInfoDB.getInstance().hasTimeZone(paramString);
        i = bool;
      }
      catch (IOException localIOException) {}
      if (i == 0)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Timezone: ");
        localStringBuilder.append(paramString);
        localStringBuilder.append(" is not an Olson ID");
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
    }
    try
    {
      mService.setTimeZone(paramString);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void setWindow(int paramInt, long paramLong1, long paramLong2, PendingIntent paramPendingIntent)
  {
    setImpl(paramInt, paramLong1, paramLong2, 0L, 0, paramPendingIntent, null, null, null, null, null);
  }
  
  public void setWindow(int paramInt, long paramLong1, long paramLong2, String paramString, OnAlarmListener paramOnAlarmListener, Handler paramHandler)
  {
    setImpl(paramInt, paramLong1, paramLong2, 0L, 0, null, paramOnAlarmListener, paramString, paramHandler, null, null);
  }
  
  public static final class AlarmClockInfo
    implements Parcelable
  {
    public static final Parcelable.Creator<AlarmClockInfo> CREATOR = new Parcelable.Creator()
    {
      public AlarmManager.AlarmClockInfo createFromParcel(Parcel paramAnonymousParcel)
      {
        return new AlarmManager.AlarmClockInfo(paramAnonymousParcel);
      }
      
      public AlarmManager.AlarmClockInfo[] newArray(int paramAnonymousInt)
      {
        return new AlarmManager.AlarmClockInfo[paramAnonymousInt];
      }
    };
    private final PendingIntent mShowIntent;
    private final long mTriggerTime;
    
    public AlarmClockInfo(long paramLong, PendingIntent paramPendingIntent)
    {
      mTriggerTime = paramLong;
      mShowIntent = paramPendingIntent;
    }
    
    AlarmClockInfo(Parcel paramParcel)
    {
      mTriggerTime = paramParcel.readLong();
      mShowIntent = ((PendingIntent)paramParcel.readParcelable(PendingIntent.class.getClassLoader()));
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public PendingIntent getShowIntent()
    {
      return mShowIntent;
    }
    
    public long getTriggerTime()
    {
      return mTriggerTime;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeLong(mTriggerTime);
      paramParcel.writeParcelable(mShowIntent, paramInt);
    }
    
    public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
    {
      paramLong = paramProtoOutputStream.start(paramLong);
      paramProtoOutputStream.write(1112396529665L, mTriggerTime);
      mShowIntent.writeToProto(paramProtoOutputStream, 1146756268034L);
      paramProtoOutputStream.end(paramLong);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface AlarmType {}
  
  final class ListenerWrapper
    extends IAlarmListener.Stub
    implements Runnable
  {
    IAlarmCompleteListener mCompletion;
    Handler mHandler;
    final AlarmManager.OnAlarmListener mListener;
    
    public ListenerWrapper(AlarmManager.OnAlarmListener paramOnAlarmListener)
    {
      mListener = paramOnAlarmListener;
    }
    
    /* Error */
    public void cancel()
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 21	android/app/AlarmManager$ListenerWrapper:this$0	Landroid/app/AlarmManager;
      //   4: invokestatic 34	android/app/AlarmManager:access$000	(Landroid/app/AlarmManager;)Landroid/app/IAlarmManager;
      //   7: aconst_null
      //   8: aload_0
      //   9: invokeinterface 40 3 0
      //   14: ldc 8
      //   16: monitorenter
      //   17: invokestatic 44	android/app/AlarmManager:access$100	()Landroid/util/ArrayMap;
      //   20: ifnull +14 -> 34
      //   23: invokestatic 44	android/app/AlarmManager:access$100	()Landroid/util/ArrayMap;
      //   26: aload_0
      //   27: getfield 26	android/app/AlarmManager$ListenerWrapper:mListener	Landroid/app/AlarmManager$OnAlarmListener;
      //   30: invokevirtual 49	android/util/ArrayMap:remove	(Ljava/lang/Object;)Ljava/lang/Object;
      //   33: pop
      //   34: ldc 8
      //   36: monitorexit
      //   37: return
      //   38: astore_1
      //   39: ldc 8
      //   41: monitorexit
      //   42: aload_1
      //   43: athrow
      //   44: astore_1
      //   45: aload_1
      //   46: invokevirtual 53	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
      //   49: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	50	0	this	ListenerWrapper
      //   38	5	1	localObject	Object
      //   44	2	1	localRemoteException	RemoteException
      // Exception table:
      //   from	to	target	type
      //   17	34	38	finally
      //   34	37	38	finally
      //   39	42	38	finally
      //   0	14	44	android/os/RemoteException
    }
    
    public void doAlarm(IAlarmCompleteListener paramIAlarmCompleteListener)
    {
      mCompletion = paramIAlarmCompleteListener;
      try
      {
        if (AlarmManager.sWrappers != null) {
          AlarmManager.sWrappers.remove(mListener);
        }
        mHandler.post(this);
        return;
      }
      finally {}
    }
    
    public void run()
    {
      try
      {
        mListener.onAlarm();
        return;
      }
      finally
      {
        try
        {
          mCompletion.alarmComplete(this);
        }
        catch (Exception localException2)
        {
          Log.e("AlarmManager", "Unable to report completion to Alarm Manager!", localException2);
        }
      }
    }
    
    public void setHandler(Handler paramHandler)
    {
      mHandler = paramHandler;
    }
  }
  
  public static abstract interface OnAlarmListener
  {
    public abstract void onAlarm();
  }
}
