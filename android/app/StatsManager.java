package android.app;

import android.annotation.SystemApi;
import android.content.Context;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.IStatsManager;
import android.os.IStatsManager.Stub;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.AndroidException;

@SystemApi
public final class StatsManager
{
  public static final String ACTION_STATSD_STARTED = "android.app.action.STATSD_STARTED";
  private static final boolean DEBUG = false;
  public static final String EXTRA_STATS_BROADCAST_SUBSCRIBER_COOKIES = "android.app.extra.STATS_BROADCAST_SUBSCRIBER_COOKIES";
  public static final String EXTRA_STATS_CONFIG_KEY = "android.app.extra.STATS_CONFIG_KEY";
  public static final String EXTRA_STATS_CONFIG_UID = "android.app.extra.STATS_CONFIG_UID";
  public static final String EXTRA_STATS_DIMENSIONS_VALUE = "android.app.extra.STATS_DIMENSIONS_VALUE";
  public static final String EXTRA_STATS_SUBSCRIPTION_ID = "android.app.extra.STATS_SUBSCRIPTION_ID";
  public static final String EXTRA_STATS_SUBSCRIPTION_RULE_ID = "android.app.extra.STATS_SUBSCRIPTION_RULE_ID";
  private static final String TAG = "StatsManager";
  private final Context mContext;
  private IStatsManager mService;
  
  public StatsManager(Context paramContext)
  {
    mContext = paramContext;
  }
  
  private IStatsManager getIStatsManagerLocked()
    throws StatsManager.StatsUnavailableException
  {
    if (mService != null) {
      return mService;
    }
    mService = IStatsManager.Stub.asInterface(ServiceManager.getService("stats"));
    if (mService != null) {
      try
      {
        IBinder localIBinder = mService.asBinder();
        StatsdDeathRecipient localStatsdDeathRecipient = new android/app/StatsManager$StatsdDeathRecipient;
        localStatsdDeathRecipient.<init>(this, null);
        localIBinder.linkToDeath(localStatsdDeathRecipient, 0);
        return mService;
      }
      catch (RemoteException localRemoteException)
      {
        throw new StatsUnavailableException("could not connect when linkToDeath", localRemoteException);
      }
    }
    throw new StatsUnavailableException("could not be found");
  }
  
  /* Error */
  public void addConfig(long paramLong, byte[] paramArrayOfByte)
    throws StatsManager.StatsUnavailableException
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial 107	android/app/StatsManager:getIStatsManagerLocked	()Landroid/os/IStatsManager;
    //   6: lload_1
    //   7: aload_3
    //   8: aload_0
    //   9: getfield 52	android/app/StatsManager:mContext	Landroid/content/Context;
    //   12: invokevirtual 113	android/content/Context:getOpPackageName	()Ljava/lang/String;
    //   15: invokeinterface 117 5 0
    //   20: aload_0
    //   21: monitorexit
    //   22: return
    //   23: astore_3
    //   24: goto +46 -> 70
    //   27: astore 4
    //   29: new 8	android/app/StatsManager$StatsUnavailableException
    //   32: astore_3
    //   33: aload_3
    //   34: aload 4
    //   36: invokevirtual 120	java/lang/SecurityException:getMessage	()Ljava/lang/String;
    //   39: aload 4
    //   41: invokespecial 95	android/app/StatsManager$StatsUnavailableException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   44: aload_3
    //   45: athrow
    //   46: astore 4
    //   48: ldc 41
    //   50: ldc 122
    //   52: invokestatic 128	android/util/Slog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   55: pop
    //   56: new 8	android/app/StatsManager$StatsUnavailableException
    //   59: astore_3
    //   60: aload_3
    //   61: ldc -126
    //   63: aload 4
    //   65: invokespecial 95	android/app/StatsManager$StatsUnavailableException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   68: aload_3
    //   69: athrow
    //   70: aload_0
    //   71: monitorexit
    //   72: aload_3
    //   73: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	74	0	this	StatsManager
    //   0	74	1	paramLong	long
    //   0	74	3	paramArrayOfByte	byte[]
    //   27	13	4	localSecurityException	SecurityException
    //   46	18	4	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   2	20	23	finally
    //   20	22	23	finally
    //   29	46	23	finally
    //   48	70	23	finally
    //   70	72	23	finally
    //   2	20	27	java/lang/SecurityException
    //   2	20	46	android/os/RemoteException
  }
  
  public boolean addConfiguration(long paramLong, byte[] paramArrayOfByte)
  {
    try
    {
      addConfig(paramLong, paramArrayOfByte);
      return true;
    }
    catch (StatsUnavailableException|IllegalArgumentException paramArrayOfByte) {}
    return false;
  }
  
  public byte[] getData(long paramLong)
  {
    try
    {
      byte[] arrayOfByte = getReports(paramLong);
      return arrayOfByte;
    }
    catch (StatsUnavailableException localStatsUnavailableException) {}
    return null;
  }
  
  public byte[] getMetadata()
  {
    try
    {
      byte[] arrayOfByte = getStatsMetadata();
      return arrayOfByte;
    }
    catch (StatsUnavailableException localStatsUnavailableException) {}
    return null;
  }
  
  /* Error */
  public byte[] getReports(long paramLong)
    throws StatsManager.StatsUnavailableException
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial 107	android/app/StatsManager:getIStatsManagerLocked	()Landroid/os/IStatsManager;
    //   6: lload_1
    //   7: aload_0
    //   8: getfield 52	android/app/StatsManager:mContext	Landroid/content/Context;
    //   11: invokevirtual 113	android/content/Context:getOpPackageName	()Ljava/lang/String;
    //   14: invokeinterface 148 4 0
    //   19: astore_3
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_3
    //   23: areturn
    //   24: astore_3
    //   25: goto +46 -> 71
    //   28: astore_3
    //   29: new 8	android/app/StatsManager$StatsUnavailableException
    //   32: astore 4
    //   34: aload 4
    //   36: aload_3
    //   37: invokevirtual 120	java/lang/SecurityException:getMessage	()Ljava/lang/String;
    //   40: aload_3
    //   41: invokespecial 95	android/app/StatsManager$StatsUnavailableException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   44: aload 4
    //   46: athrow
    //   47: astore 4
    //   49: ldc 41
    //   51: ldc -106
    //   53: invokestatic 128	android/util/Slog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   56: pop
    //   57: new 8	android/app/StatsManager$StatsUnavailableException
    //   60: astore_3
    //   61: aload_3
    //   62: ldc -126
    //   64: aload 4
    //   66: invokespecial 95	android/app/StatsManager$StatsUnavailableException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   69: aload_3
    //   70: athrow
    //   71: aload_0
    //   72: monitorexit
    //   73: aload_3
    //   74: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	75	0	this	StatsManager
    //   0	75	1	paramLong	long
    //   19	4	3	arrayOfByte	byte[]
    //   24	1	3	localObject	Object
    //   28	13	3	localSecurityException	SecurityException
    //   60	14	3	localStatsUnavailableException1	StatsUnavailableException
    //   32	13	4	localStatsUnavailableException2	StatsUnavailableException
    //   47	18	4	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   2	20	24	finally
    //   20	22	24	finally
    //   29	47	24	finally
    //   49	71	24	finally
    //   71	73	24	finally
    //   2	20	28	java/lang/SecurityException
    //   2	20	47	android/os/RemoteException
  }
  
  /* Error */
  public byte[] getStatsMetadata()
    throws StatsManager.StatsUnavailableException
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial 107	android/app/StatsManager:getIStatsManagerLocked	()Landroid/os/IStatsManager;
    //   6: aload_0
    //   7: getfield 52	android/app/StatsManager:mContext	Landroid/content/Context;
    //   10: invokevirtual 113	android/content/Context:getOpPackageName	()Ljava/lang/String;
    //   13: invokeinterface 153 2 0
    //   18: astore_1
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_1
    //   22: areturn
    //   23: astore_1
    //   24: goto +41 -> 65
    //   27: astore_2
    //   28: new 8	android/app/StatsManager$StatsUnavailableException
    //   31: astore_1
    //   32: aload_1
    //   33: aload_2
    //   34: invokevirtual 120	java/lang/SecurityException:getMessage	()Ljava/lang/String;
    //   37: aload_2
    //   38: invokespecial 95	android/app/StatsManager$StatsUnavailableException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   41: aload_1
    //   42: athrow
    //   43: astore_2
    //   44: ldc 41
    //   46: ldc -101
    //   48: invokestatic 128	android/util/Slog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   51: pop
    //   52: new 8	android/app/StatsManager$StatsUnavailableException
    //   55: astore_1
    //   56: aload_1
    //   57: ldc -126
    //   59: aload_2
    //   60: invokespecial 95	android/app/StatsManager$StatsUnavailableException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   63: aload_1
    //   64: athrow
    //   65: aload_0
    //   66: monitorexit
    //   67: aload_1
    //   68: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	69	0	this	StatsManager
    //   18	4	1	arrayOfByte	byte[]
    //   23	1	1	localObject	Object
    //   31	37	1	localStatsUnavailableException	StatsUnavailableException
    //   27	11	2	localSecurityException	SecurityException
    //   43	17	2	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   2	19	23	finally
    //   19	21	23	finally
    //   28	43	23	finally
    //   44	65	23	finally
    //   65	67	23	finally
    //   2	19	27	java/lang/SecurityException
    //   2	19	43	android/os/RemoteException
  }
  
  /* Error */
  public void removeConfig(long paramLong)
    throws StatsManager.StatsUnavailableException
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial 107	android/app/StatsManager:getIStatsManagerLocked	()Landroid/os/IStatsManager;
    //   6: lload_1
    //   7: aload_0
    //   8: getfield 52	android/app/StatsManager:mContext	Landroid/content/Context;
    //   11: invokevirtual 113	android/content/Context:getOpPackageName	()Ljava/lang/String;
    //   14: invokeinterface 161 4 0
    //   19: aload_0
    //   20: monitorexit
    //   21: return
    //   22: astore_3
    //   23: goto +47 -> 70
    //   26: astore 4
    //   28: new 8	android/app/StatsManager$StatsUnavailableException
    //   31: astore_3
    //   32: aload_3
    //   33: aload 4
    //   35: invokevirtual 120	java/lang/SecurityException:getMessage	()Ljava/lang/String;
    //   38: aload 4
    //   40: invokespecial 95	android/app/StatsManager$StatsUnavailableException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   43: aload_3
    //   44: athrow
    //   45: astore_3
    //   46: ldc 41
    //   48: ldc -93
    //   50: invokestatic 128	android/util/Slog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   53: pop
    //   54: new 8	android/app/StatsManager$StatsUnavailableException
    //   57: astore 4
    //   59: aload 4
    //   61: ldc -126
    //   63: aload_3
    //   64: invokespecial 95	android/app/StatsManager$StatsUnavailableException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   67: aload 4
    //   69: athrow
    //   70: aload_0
    //   71: monitorexit
    //   72: aload_3
    //   73: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	74	0	this	StatsManager
    //   0	74	1	paramLong	long
    //   22	1	3	localObject	Object
    //   31	13	3	localStatsUnavailableException1	StatsUnavailableException
    //   45	28	3	localRemoteException	RemoteException
    //   26	13	4	localSecurityException	SecurityException
    //   57	11	4	localStatsUnavailableException2	StatsUnavailableException
    // Exception table:
    //   from	to	target	type
    //   2	19	22	finally
    //   19	21	22	finally
    //   28	45	22	finally
    //   46	70	22	finally
    //   70	72	22	finally
    //   2	19	26	java/lang/SecurityException
    //   2	19	45	android/os/RemoteException
  }
  
  public boolean removeConfiguration(long paramLong)
  {
    try
    {
      removeConfig(paramLong);
      return true;
    }
    catch (StatsUnavailableException localStatsUnavailableException) {}
    return false;
  }
  
  /* Error */
  public void setBroadcastSubscriber(PendingIntent paramPendingIntent, long paramLong1, long paramLong2)
    throws StatsManager.StatsUnavailableException
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial 107	android/app/StatsManager:getIStatsManagerLocked	()Landroid/os/IStatsManager;
    //   6: astore 6
    //   8: aload_1
    //   9: ifnull +32 -> 41
    //   12: aload 6
    //   14: lload_2
    //   15: lload 4
    //   17: aload_1
    //   18: invokevirtual 174	android/app/PendingIntent:getTarget	()Landroid/content/IIntentSender;
    //   21: invokeinterface 177 1 0
    //   26: aload_0
    //   27: getfield 52	android/app/StatsManager:mContext	Landroid/content/Context;
    //   30: invokevirtual 113	android/content/Context:getOpPackageName	()Ljava/lang/String;
    //   33: invokeinterface 180 7 0
    //   38: goto +20 -> 58
    //   41: aload 6
    //   43: lload_2
    //   44: lload 4
    //   46: aload_0
    //   47: getfield 52	android/app/StatsManager:mContext	Landroid/content/Context;
    //   50: invokevirtual 113	android/content/Context:getOpPackageName	()Ljava/lang/String;
    //   53: invokeinterface 184 6 0
    //   58: aload_0
    //   59: monitorexit
    //   60: return
    //   61: astore_1
    //   62: goto +48 -> 110
    //   65: astore_1
    //   66: new 8	android/app/StatsManager$StatsUnavailableException
    //   69: astore 6
    //   71: aload 6
    //   73: aload_1
    //   74: invokevirtual 120	java/lang/SecurityException:getMessage	()Ljava/lang/String;
    //   77: aload_1
    //   78: invokespecial 95	android/app/StatsManager$StatsUnavailableException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   81: aload 6
    //   83: athrow
    //   84: astore 6
    //   86: ldc 41
    //   88: ldc -70
    //   90: aload 6
    //   92: invokestatic 189	android/util/Slog:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   95: pop
    //   96: new 8	android/app/StatsManager$StatsUnavailableException
    //   99: astore_1
    //   100: aload_1
    //   101: ldc -126
    //   103: aload 6
    //   105: invokespecial 95	android/app/StatsManager$StatsUnavailableException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   108: aload_1
    //   109: athrow
    //   110: aload_0
    //   111: monitorexit
    //   112: aload_1
    //   113: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	114	0	this	StatsManager
    //   0	114	1	paramPendingIntent	PendingIntent
    //   0	114	2	paramLong1	long
    //   0	114	4	paramLong2	long
    //   6	76	6	localObject	Object
    //   84	20	6	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   2	8	61	finally
    //   12	38	61	finally
    //   41	58	61	finally
    //   58	60	61	finally
    //   66	84	61	finally
    //   86	110	61	finally
    //   110	112	61	finally
    //   2	8	65	java/lang/SecurityException
    //   12	38	65	java/lang/SecurityException
    //   41	58	65	java/lang/SecurityException
    //   2	8	84	android/os/RemoteException
    //   12	38	84	android/os/RemoteException
    //   41	58	84	android/os/RemoteException
  }
  
  public boolean setBroadcastSubscriber(long paramLong1, long paramLong2, PendingIntent paramPendingIntent)
  {
    try
    {
      setBroadcastSubscriber(paramPendingIntent, paramLong1, paramLong2);
      return true;
    }
    catch (StatsUnavailableException paramPendingIntent) {}
    return false;
  }
  
  public boolean setDataFetchOperation(long paramLong, PendingIntent paramPendingIntent)
  {
    try
    {
      setFetchReportsOperation(paramPendingIntent, paramLong);
      return true;
    }
    catch (StatsUnavailableException paramPendingIntent) {}
    return false;
  }
  
  /* Error */
  public void setFetchReportsOperation(PendingIntent paramPendingIntent, long paramLong)
    throws StatsManager.StatsUnavailableException
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial 107	android/app/StatsManager:getIStatsManagerLocked	()Landroid/os/IStatsManager;
    //   6: astore 4
    //   8: aload_1
    //   9: ifnonnull +21 -> 30
    //   12: aload 4
    //   14: lload_2
    //   15: aload_0
    //   16: getfield 52	android/app/StatsManager:mContext	Landroid/content/Context;
    //   19: invokevirtual 113	android/content/Context:getOpPackageName	()Ljava/lang/String;
    //   22: invokeinterface 201 4 0
    //   27: goto +27 -> 54
    //   30: aload 4
    //   32: lload_2
    //   33: aload_1
    //   34: invokevirtual 174	android/app/PendingIntent:getTarget	()Landroid/content/IIntentSender;
    //   37: invokeinterface 177 1 0
    //   42: aload_0
    //   43: getfield 52	android/app/StatsManager:mContext	Landroid/content/Context;
    //   46: invokevirtual 113	android/content/Context:getOpPackageName	()Ljava/lang/String;
    //   49: invokeinterface 204 5 0
    //   54: aload_0
    //   55: monitorexit
    //   56: return
    //   57: astore_1
    //   58: goto +46 -> 104
    //   61: astore 4
    //   63: new 8	android/app/StatsManager$StatsUnavailableException
    //   66: astore_1
    //   67: aload_1
    //   68: aload 4
    //   70: invokevirtual 120	java/lang/SecurityException:getMessage	()Ljava/lang/String;
    //   73: aload 4
    //   75: invokespecial 95	android/app/StatsManager$StatsUnavailableException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   78: aload_1
    //   79: athrow
    //   80: astore 4
    //   82: ldc 41
    //   84: ldc -50
    //   86: invokestatic 128	android/util/Slog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   89: pop
    //   90: new 8	android/app/StatsManager$StatsUnavailableException
    //   93: astore_1
    //   94: aload_1
    //   95: ldc -126
    //   97: aload 4
    //   99: invokespecial 95	android/app/StatsManager$StatsUnavailableException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   102: aload_1
    //   103: athrow
    //   104: aload_0
    //   105: monitorexit
    //   106: aload_1
    //   107: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	108	0	this	StatsManager
    //   0	108	1	paramPendingIntent	PendingIntent
    //   0	108	2	paramLong	long
    //   6	25	4	localIStatsManager	IStatsManager
    //   61	13	4	localSecurityException	SecurityException
    //   80	18	4	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   2	8	57	finally
    //   12	27	57	finally
    //   30	54	57	finally
    //   54	56	57	finally
    //   63	80	57	finally
    //   82	104	57	finally
    //   104	106	57	finally
    //   2	8	61	java/lang/SecurityException
    //   12	27	61	java/lang/SecurityException
    //   30	54	61	java/lang/SecurityException
    //   2	8	80	android/os/RemoteException
    //   12	27	80	android/os/RemoteException
    //   30	54	80	android/os/RemoteException
  }
  
  public static class StatsUnavailableException
    extends AndroidException
  {
    public StatsUnavailableException(String paramString)
    {
      super();
    }
    
    public StatsUnavailableException(String paramString, Throwable paramThrowable)
    {
      super(paramThrowable);
    }
  }
  
  private class StatsdDeathRecipient
    implements IBinder.DeathRecipient
  {
    private StatsdDeathRecipient() {}
    
    public void binderDied()
    {
      try
      {
        StatsManager.access$002(StatsManager.this, null);
        return;
      }
      finally {}
    }
  }
}
