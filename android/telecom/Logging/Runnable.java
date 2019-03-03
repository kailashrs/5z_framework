package android.telecom.Logging;

import android.telecom.Log;

public abstract class Runnable
{
  private final Object mLock;
  private final java.lang.Runnable mRunnable = new java.lang.Runnable()
  {
    /* Error */
    public void run()
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 14	android/telecom/Logging/Runnable$1:this$0	Landroid/telecom/Logging/Runnable;
      //   4: invokestatic 23	android/telecom/Logging/Runnable:access$000	(Landroid/telecom/Logging/Runnable;)Ljava/lang/Object;
      //   7: astore_1
      //   8: aload_1
      //   9: monitorenter
      //   10: aload_0
      //   11: getfield 14	android/telecom/Logging/Runnable$1:this$0	Landroid/telecom/Logging/Runnable;
      //   14: invokestatic 27	android/telecom/Logging/Runnable:access$100	(Landroid/telecom/Logging/Runnable;)Landroid/telecom/Logging/Session;
      //   17: aload_0
      //   18: getfield 14	android/telecom/Logging/Runnable$1:this$0	Landroid/telecom/Logging/Runnable;
      //   21: invokestatic 31	android/telecom/Logging/Runnable:access$200	(Landroid/telecom/Logging/Runnable;)Ljava/lang/String;
      //   24: invokestatic 37	android/telecom/Log:continueSession	(Landroid/telecom/Logging/Session;Ljava/lang/String;)V
      //   27: aload_0
      //   28: getfield 14	android/telecom/Logging/Runnable$1:this$0	Landroid/telecom/Logging/Runnable;
      //   31: invokevirtual 40	android/telecom/Logging/Runnable:loggedRun	()V
      //   34: aload_0
      //   35: getfield 14	android/telecom/Logging/Runnable$1:this$0	Landroid/telecom/Logging/Runnable;
      //   38: invokestatic 27	android/telecom/Logging/Runnable:access$100	(Landroid/telecom/Logging/Runnable;)Landroid/telecom/Logging/Session;
      //   41: ifnull +15 -> 56
      //   44: invokestatic 43	android/telecom/Log:endSession	()V
      //   47: aload_0
      //   48: getfield 14	android/telecom/Logging/Runnable$1:this$0	Landroid/telecom/Logging/Runnable;
      //   51: aconst_null
      //   52: invokestatic 47	android/telecom/Logging/Runnable:access$102	(Landroid/telecom/Logging/Runnable;Landroid/telecom/Logging/Session;)Landroid/telecom/Logging/Session;
      //   55: pop
      //   56: aload_1
      //   57: monitorexit
      //   58: return
      //   59: astore_2
      //   60: aload_0
      //   61: getfield 14	android/telecom/Logging/Runnable$1:this$0	Landroid/telecom/Logging/Runnable;
      //   64: invokestatic 27	android/telecom/Logging/Runnable:access$100	(Landroid/telecom/Logging/Runnable;)Landroid/telecom/Logging/Session;
      //   67: ifnull +15 -> 82
      //   70: invokestatic 43	android/telecom/Log:endSession	()V
      //   73: aload_0
      //   74: getfield 14	android/telecom/Logging/Runnable$1:this$0	Landroid/telecom/Logging/Runnable;
      //   77: aconst_null
      //   78: invokestatic 47	android/telecom/Logging/Runnable:access$102	(Landroid/telecom/Logging/Runnable;Landroid/telecom/Logging/Session;)Landroid/telecom/Logging/Session;
      //   81: pop
      //   82: aload_2
      //   83: athrow
      //   84: astore_2
      //   85: aload_1
      //   86: monitorexit
      //   87: aload_2
      //   88: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	89	0	this	1
      //   7	79	1	localObject1	Object
      //   59	24	2	localObject2	Object
      //   84	4	2	localObject3	Object
      // Exception table:
      //   from	to	target	type
      //   10	34	59	finally
      //   34	56	84	finally
      //   56	58	84	finally
      //   60	82	84	finally
      //   82	84	84	finally
      //   85	87	84	finally
    }
  };
  private Session mSubsession;
  private final String mSubsessionName;
  
  public Runnable(String paramString, Object paramObject)
  {
    if (paramObject == null) {
      mLock = new Object();
    } else {
      mLock = paramObject;
    }
    mSubsessionName = paramString;
  }
  
  public void cancel()
  {
    synchronized (mLock)
    {
      Log.cancelSubsession(mSubsession);
      mSubsession = null;
      return;
    }
  }
  
  public final java.lang.Runnable getRunnableToCancel()
  {
    return mRunnable;
  }
  
  public abstract void loggedRun();
  
  public java.lang.Runnable prepare()
  {
    cancel();
    mSubsession = Log.createSubsession();
    return mRunnable;
  }
}
