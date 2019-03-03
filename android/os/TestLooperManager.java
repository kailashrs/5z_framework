package android.os;

import android.util.ArraySet;
import java.util.concurrent.LinkedBlockingQueue;

public class TestLooperManager
{
  private static final ArraySet<Looper> sHeldLoopers = new ArraySet();
  private final LinkedBlockingQueue<MessageExecution> mExecuteQueue = new LinkedBlockingQueue();
  private final Looper mLooper;
  private boolean mLooperBlocked;
  private final MessageQueue mQueue;
  private boolean mReleased;
  
  public TestLooperManager(Looper paramLooper)
  {
    synchronized (sHeldLoopers)
    {
      if (!sHeldLoopers.contains(paramLooper))
      {
        sHeldLoopers.add(paramLooper);
        mLooper = paramLooper;
        mQueue = mLooper.getQueue();
        new Handler(paramLooper).post(new LooperHolder(null));
        return;
      }
      paramLooper = new java/lang/RuntimeException;
      paramLooper.<init>("TestLooperManager already held for this looper");
      throw paramLooper;
    }
  }
  
  private void checkReleased()
  {
    if (!mReleased) {
      return;
    }
    throw new RuntimeException("release() has already be called");
  }
  
  public void execute(Message paramMessage)
  {
    checkReleased();
    MessageExecution localMessageExecution;
    if (Looper.myLooper() == mLooper)
    {
      target.dispatchMessage(paramMessage);
    }
    else
    {
      localMessageExecution = new MessageExecution(null);
      MessageExecution.access$202(localMessageExecution, paramMessage);
    }
    try
    {
      mExecuteQueue.add(localMessageExecution);
      try
      {
        localMessageExecution.wait();
      }
      catch (InterruptedException paramMessage) {}
      if (response == null) {
        return;
      }
      paramMessage = new java/lang/RuntimeException;
      paramMessage.<init>(response);
      throw paramMessage;
    }
    finally {}
  }
  
  public MessageQueue getMessageQueue()
  {
    checkReleased();
    return mQueue;
  }
  
  @Deprecated
  public MessageQueue getQueue()
  {
    return getMessageQueue();
  }
  
  public boolean hasMessages(Handler paramHandler, Object paramObject, int paramInt)
  {
    checkReleased();
    return mQueue.hasMessages(paramHandler, paramInt, paramObject);
  }
  
  public boolean hasMessages(Handler paramHandler, Object paramObject, Runnable paramRunnable)
  {
    checkReleased();
    return mQueue.hasMessages(paramHandler, paramRunnable, paramObject);
  }
  
  /* Error */
  public Message next()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 81	android/os/TestLooperManager:mLooperBlocked	Z
    //   4: ifne +26 -> 30
    //   7: aload_0
    //   8: monitorenter
    //   9: aload_0
    //   10: invokevirtual 120	java/lang/Object:wait	()V
    //   13: goto +8 -> 21
    //   16: astore_1
    //   17: goto +9 -> 26
    //   20: astore_1
    //   21: aload_0
    //   22: monitorexit
    //   23: goto -23 -> 0
    //   26: aload_0
    //   27: monitorexit
    //   28: aload_1
    //   29: athrow
    //   30: aload_0
    //   31: invokespecial 96	android/os/TestLooperManager:checkReleased	()V
    //   34: aload_0
    //   35: getfield 59	android/os/TestLooperManager:mQueue	Landroid/os/MessageQueue;
    //   38: invokevirtual 147	android/os/MessageQueue:next	()Landroid/os/Message;
    //   41: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	42	0	this	TestLooperManager
    //   16	1	1	localObject	Object
    //   20	9	1	localInterruptedException	InterruptedException
    // Exception table:
    //   from	to	target	type
    //   9	13	16	finally
    //   21	23	16	finally
    //   26	28	16	finally
    //   9	13	20	java/lang/InterruptedException
  }
  
  public void recycle(Message paramMessage)
  {
    checkReleased();
    paramMessage.recycleUnchecked();
  }
  
  public void release()
  {
    synchronized (sHeldLoopers)
    {
      sHeldLoopers.remove(mLooper);
      checkReleased();
      mReleased = true;
      mExecuteQueue.add(new MessageExecution(null));
      return;
    }
  }
  
  private class LooperHolder
    implements Runnable
  {
    private LooperHolder() {}
    
    /* Error */
    private void processMessage(TestLooperManager.MessageExecution paramMessageExecution)
    {
      // Byte code:
      //   0: aload_1
      //   1: monitorenter
      //   2: aload_1
      //   3: invokestatic 32	android/os/TestLooperManager$MessageExecution:access$200	(Landroid/os/TestLooperManager$MessageExecution;)Landroid/os/Message;
      //   6: getfield 38	android/os/Message:target	Landroid/os/Handler;
      //   9: aload_1
      //   10: invokestatic 32	android/os/TestLooperManager$MessageExecution:access$200	(Landroid/os/TestLooperManager$MessageExecution;)Landroid/os/Message;
      //   13: invokevirtual 44	android/os/Handler:dispatchMessage	(Landroid/os/Message;)V
      //   16: aload_1
      //   17: aconst_null
      //   18: invokestatic 48	android/os/TestLooperManager$MessageExecution:access$302	(Landroid/os/TestLooperManager$MessageExecution;Ljava/lang/Throwable;)Ljava/lang/Throwable;
      //   21: pop
      //   22: goto +14 -> 36
      //   25: astore_2
      //   26: goto +17 -> 43
      //   29: astore_2
      //   30: aload_1
      //   31: aload_2
      //   32: invokestatic 48	android/os/TestLooperManager$MessageExecution:access$302	(Landroid/os/TestLooperManager$MessageExecution;Ljava/lang/Throwable;)Ljava/lang/Throwable;
      //   35: pop
      //   36: aload_1
      //   37: invokevirtual 51	java/lang/Object:notifyAll	()V
      //   40: aload_1
      //   41: monitorexit
      //   42: return
      //   43: aload_1
      //   44: monitorexit
      //   45: aload_2
      //   46: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	47	0	this	LooperHolder
      //   0	47	1	paramMessageExecution	TestLooperManager.MessageExecution
      //   25	1	2	localObject	Object
      //   29	17	2	localThrowable	Throwable
      // Exception table:
      //   from	to	target	type
      //   2	22	25	finally
      //   30	36	25	finally
      //   36	42	25	finally
      //   43	45	25	finally
      //   2	22	29	java/lang/Throwable
    }
    
    public void run()
    {
      synchronized (TestLooperManager.this)
      {
        TestLooperManager.access$402(TestLooperManager.this, true);
        notify();
        while (!mReleased) {
          try
          {
            ??? = (TestLooperManager.MessageExecution)mExecuteQueue.take();
            if (TestLooperManager.MessageExecution.access$200((TestLooperManager.MessageExecution)???) != null) {
              processMessage((TestLooperManager.MessageExecution)???);
            }
          }
          catch (InterruptedException localInterruptedException) {}
        }
        synchronized (TestLooperManager.this)
        {
          TestLooperManager.access$402(TestLooperManager.this, false);
          return;
        }
      }
    }
  }
  
  private static class MessageExecution
  {
    private Message m;
    private Throwable response;
    
    private MessageExecution() {}
  }
}
