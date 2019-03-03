package android.util;

import android.os.IStatsManager;
import android.os.IStatsManager.Stub;
import android.os.RemoteException;
import android.os.ServiceManager;

public final class StatsLog
  extends StatsLogInternal
{
  private static final boolean DEBUG = false;
  private static final String TAG = "StatsLog";
  private static IStatsManager sService;
  
  private StatsLog() {}
  
  private static IStatsManager getIStatsManagerLocked()
    throws RemoteException
  {
    if (sService != null) {
      return sService;
    }
    sService = IStatsManager.Stub.asInterface(ServiceManager.getService("stats"));
    return sService;
  }
  
  /* Error */
  public static boolean logEvent(int paramInt)
  {
    // Byte code:
    //   0: ldc 2
    //   2: monitorenter
    //   3: invokestatic 43	android/util/StatsLog:getIStatsManagerLocked	()Landroid/os/IStatsManager;
    //   6: astore_1
    //   7: aload_1
    //   8: ifnonnull +8 -> 16
    //   11: ldc 2
    //   13: monitorexit
    //   14: iconst_0
    //   15: ireturn
    //   16: aload_1
    //   17: iload_0
    //   18: iconst_1
    //   19: invokeinterface 49 3 0
    //   24: ldc 2
    //   26: monitorexit
    //   27: iconst_1
    //   28: ireturn
    //   29: astore_1
    //   30: goto +13 -> 43
    //   33: astore_1
    //   34: aconst_null
    //   35: putstatic 24	android/util/StatsLog:sService	Landroid/os/IStatsManager;
    //   38: ldc 2
    //   40: monitorexit
    //   41: iconst_0
    //   42: ireturn
    //   43: ldc 2
    //   45: monitorexit
    //   46: aload_1
    //   47: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	48	0	paramInt	int
    //   6	11	1	localIStatsManager	IStatsManager
    //   29	1	1	localObject	Object
    //   33	14	1	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   3	7	29	finally
    //   11	14	29	finally
    //   16	24	29	finally
    //   24	27	29	finally
    //   34	41	29	finally
    //   43	46	29	finally
    //   3	7	33	android/os/RemoteException
    //   16	24	33	android/os/RemoteException
  }
  
  /* Error */
  public static boolean logStart(int paramInt)
  {
    // Byte code:
    //   0: ldc 2
    //   2: monitorenter
    //   3: invokestatic 43	android/util/StatsLog:getIStatsManagerLocked	()Landroid/os/IStatsManager;
    //   6: astore_1
    //   7: aload_1
    //   8: ifnonnull +8 -> 16
    //   11: ldc 2
    //   13: monitorexit
    //   14: iconst_0
    //   15: ireturn
    //   16: aload_1
    //   17: iload_0
    //   18: iconst_3
    //   19: invokeinterface 49 3 0
    //   24: ldc 2
    //   26: monitorexit
    //   27: iconst_1
    //   28: ireturn
    //   29: astore_1
    //   30: goto +13 -> 43
    //   33: astore_1
    //   34: aconst_null
    //   35: putstatic 24	android/util/StatsLog:sService	Landroid/os/IStatsManager;
    //   38: ldc 2
    //   40: monitorexit
    //   41: iconst_0
    //   42: ireturn
    //   43: ldc 2
    //   45: monitorexit
    //   46: aload_1
    //   47: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	48	0	paramInt	int
    //   6	11	1	localIStatsManager	IStatsManager
    //   29	1	1	localObject	Object
    //   33	14	1	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   3	7	29	finally
    //   11	14	29	finally
    //   16	24	29	finally
    //   24	27	29	finally
    //   34	41	29	finally
    //   43	46	29	finally
    //   3	7	33	android/os/RemoteException
    //   16	24	33	android/os/RemoteException
  }
  
  /* Error */
  public static boolean logStop(int paramInt)
  {
    // Byte code:
    //   0: ldc 2
    //   2: monitorenter
    //   3: invokestatic 43	android/util/StatsLog:getIStatsManagerLocked	()Landroid/os/IStatsManager;
    //   6: astore_1
    //   7: aload_1
    //   8: ifnonnull +8 -> 16
    //   11: ldc 2
    //   13: monitorexit
    //   14: iconst_0
    //   15: ireturn
    //   16: aload_1
    //   17: iload_0
    //   18: iconst_2
    //   19: invokeinterface 49 3 0
    //   24: ldc 2
    //   26: monitorexit
    //   27: iconst_1
    //   28: ireturn
    //   29: astore_1
    //   30: goto +13 -> 43
    //   33: astore_1
    //   34: aconst_null
    //   35: putstatic 24	android/util/StatsLog:sService	Landroid/os/IStatsManager;
    //   38: ldc 2
    //   40: monitorexit
    //   41: iconst_0
    //   42: ireturn
    //   43: ldc 2
    //   45: monitorexit
    //   46: aload_1
    //   47: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	48	0	paramInt	int
    //   6	11	1	localIStatsManager	IStatsManager
    //   29	1	1	localObject	Object
    //   33	14	1	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   3	7	29	finally
    //   11	14	29	finally
    //   16	24	29	finally
    //   24	27	29	finally
    //   34	41	29	finally
    //   43	46	29	finally
    //   3	7	33	android/os/RemoteException
    //   16	24	33	android/os/RemoteException
  }
}
