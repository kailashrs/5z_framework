package android.os;

import android.util.Log;
import android.util.Printer;

public class Handler
{
  private static final boolean FIND_POTENTIAL_LEAKS = false;
  private static Handler MAIN_THREAD_HANDLER = null;
  private static final String TAG = "Handler";
  final boolean mAsynchronous;
  final Callback mCallback;
  final Looper mLooper;
  IMessenger mMessenger;
  final MessageQueue mQueue;
  
  public Handler()
  {
    this(null, false);
  }
  
  public Handler(Callback paramCallback)
  {
    this(paramCallback, false);
  }
  
  public Handler(Callback paramCallback, boolean paramBoolean)
  {
    mLooper = Looper.myLooper();
    if (mLooper != null)
    {
      mQueue = mLooper.mQueue;
      mCallback = paramCallback;
      mAsynchronous = paramBoolean;
      return;
    }
    paramCallback = new StringBuilder();
    paramCallback.append("Can't create handler inside thread ");
    paramCallback.append(Thread.currentThread());
    paramCallback.append(" that has not called Looper.prepare()");
    throw new RuntimeException(paramCallback.toString());
  }
  
  public Handler(Looper paramLooper)
  {
    this(paramLooper, null, false);
  }
  
  public Handler(Looper paramLooper, Callback paramCallback)
  {
    this(paramLooper, paramCallback, false);
  }
  
  public Handler(Looper paramLooper, Callback paramCallback, boolean paramBoolean)
  {
    mLooper = paramLooper;
    mQueue = mQueue;
    mCallback = paramCallback;
    mAsynchronous = paramBoolean;
  }
  
  public Handler(boolean paramBoolean)
  {
    this(null, paramBoolean);
  }
  
  public static Handler createAsync(Looper paramLooper)
  {
    if (paramLooper != null) {
      return new Handler(paramLooper, null, true);
    }
    throw new NullPointerException("looper must not be null");
  }
  
  public static Handler createAsync(Looper paramLooper, Callback paramCallback)
  {
    if (paramLooper != null)
    {
      if (paramCallback != null) {
        return new Handler(paramLooper, paramCallback, true);
      }
      throw new NullPointerException("callback must not be null");
    }
    throw new NullPointerException("looper must not be null");
  }
  
  private boolean enqueueMessage(MessageQueue paramMessageQueue, Message paramMessage, long paramLong)
  {
    target = this;
    if (mAsynchronous) {
      paramMessage.setAsynchronous(true);
    }
    return paramMessageQueue.enqueueMessage(paramMessage, paramLong);
  }
  
  public static Handler getMain()
  {
    if (MAIN_THREAD_HANDLER == null) {
      MAIN_THREAD_HANDLER = new Handler(Looper.getMainLooper());
    }
    return MAIN_THREAD_HANDLER;
  }
  
  private static Message getPostMessage(Runnable paramRunnable)
  {
    Message localMessage = Message.obtain();
    callback = paramRunnable;
    return localMessage;
  }
  
  private static Message getPostMessage(Runnable paramRunnable, Object paramObject)
  {
    Message localMessage = Message.obtain();
    obj = paramObject;
    callback = paramRunnable;
    return localMessage;
  }
  
  private static void handleCallback(Message paramMessage)
  {
    callback.run();
  }
  
  public static Handler mainIfNull(Handler paramHandler)
  {
    if (paramHandler == null) {
      paramHandler = getMain();
    }
    return paramHandler;
  }
  
  public void dispatchMessage(Message paramMessage)
  {
    if (callback != null)
    {
      handleCallback(paramMessage);
    }
    else
    {
      if ((mCallback != null) && (mCallback.handleMessage(paramMessage))) {
        return;
      }
      handleMessage(paramMessage);
    }
  }
  
  public final void dump(Printer paramPrinter, String paramString)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append(this);
    ((StringBuilder)localObject).append(" @ ");
    ((StringBuilder)localObject).append(SystemClock.uptimeMillis());
    paramPrinter.println(((StringBuilder)localObject).toString());
    if (mLooper == null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("looper uninitialized");
      paramPrinter.println(((StringBuilder)localObject).toString());
    }
    else
    {
      localObject = mLooper;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("  ");
      ((Looper)localObject).dump(paramPrinter, localStringBuilder.toString());
    }
  }
  
  public final void dumpMine(Printer paramPrinter, String paramString)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append(this);
    ((StringBuilder)localObject).append(" @ ");
    ((StringBuilder)localObject).append(SystemClock.uptimeMillis());
    paramPrinter.println(((StringBuilder)localObject).toString());
    if (mLooper == null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("looper uninitialized");
      paramPrinter.println(((StringBuilder)localObject).toString());
    }
    else
    {
      localObject = mLooper;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("  ");
      ((Looper)localObject).dump(paramPrinter, localStringBuilder.toString(), this);
    }
  }
  
  public final boolean executeOrSendMessage(Message paramMessage)
  {
    if (mLooper == Looper.myLooper())
    {
      dispatchMessage(paramMessage);
      return true;
    }
    return sendMessage(paramMessage);
  }
  
  final IMessenger getIMessenger()
  {
    synchronized (mQueue)
    {
      if (mMessenger != null)
      {
        localObject1 = mMessenger;
        return localObject1;
      }
      Object localObject1 = new android/os/Handler$MessengerImpl;
      ((MessengerImpl)localObject1).<init>(this, null);
      mMessenger = ((IMessenger)localObject1);
      localObject1 = mMessenger;
      return localObject1;
    }
  }
  
  public final Looper getLooper()
  {
    return mLooper;
  }
  
  public String getMessageName(Message paramMessage)
  {
    if (callback != null) {
      return callback.getClass().getName();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(what));
    return localStringBuilder.toString();
  }
  
  public String getTraceName(Message paramMessage)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(getClass().getName());
    localStringBuilder.append(": ");
    if (callback != null)
    {
      localStringBuilder.append(callback.getClass().getName());
    }
    else
    {
      localStringBuilder.append("#");
      localStringBuilder.append(what);
    }
    return localStringBuilder.toString();
  }
  
  public void handleMessage(Message paramMessage) {}
  
  public final boolean hasCallbacks(Runnable paramRunnable)
  {
    return mQueue.hasMessages(this, paramRunnable, null);
  }
  
  public final boolean hasMessages(int paramInt)
  {
    return mQueue.hasMessages(this, paramInt, null);
  }
  
  public final boolean hasMessages(int paramInt, Object paramObject)
  {
    return mQueue.hasMessages(this, paramInt, paramObject);
  }
  
  public final boolean hasMessagesOrCallbacks()
  {
    return mQueue.hasMessages(this);
  }
  
  public final Message obtainMessage()
  {
    return Message.obtain(this);
  }
  
  public final Message obtainMessage(int paramInt)
  {
    return Message.obtain(this, paramInt);
  }
  
  public final Message obtainMessage(int paramInt1, int paramInt2, int paramInt3)
  {
    return Message.obtain(this, paramInt1, paramInt2, paramInt3);
  }
  
  public final Message obtainMessage(int paramInt1, int paramInt2, int paramInt3, Object paramObject)
  {
    return Message.obtain(this, paramInt1, paramInt2, paramInt3, paramObject);
  }
  
  public final Message obtainMessage(int paramInt, Object paramObject)
  {
    return Message.obtain(this, paramInt, paramObject);
  }
  
  public final boolean post(Runnable paramRunnable)
  {
    return sendMessageDelayed(getPostMessage(paramRunnable), 0L);
  }
  
  public final boolean postAtFrontOfQueue(Runnable paramRunnable)
  {
    return sendMessageAtFrontOfQueue(getPostMessage(paramRunnable));
  }
  
  public final boolean postAtTime(Runnable paramRunnable, long paramLong)
  {
    return sendMessageAtTime(getPostMessage(paramRunnable), paramLong);
  }
  
  public final boolean postAtTime(Runnable paramRunnable, Object paramObject, long paramLong)
  {
    return sendMessageAtTime(getPostMessage(paramRunnable, paramObject), paramLong);
  }
  
  public final boolean postDelayed(Runnable paramRunnable, long paramLong)
  {
    return sendMessageDelayed(getPostMessage(paramRunnable), paramLong);
  }
  
  public final boolean postDelayed(Runnable paramRunnable, Object paramObject, long paramLong)
  {
    return sendMessageDelayed(getPostMessage(paramRunnable, paramObject), paramLong);
  }
  
  public final void removeCallbacks(Runnable paramRunnable)
  {
    mQueue.removeMessages(this, paramRunnable, null);
  }
  
  public final void removeCallbacks(Runnable paramRunnable, Object paramObject)
  {
    mQueue.removeMessages(this, paramRunnable, paramObject);
  }
  
  public final void removeCallbacksAndMessages(Object paramObject)
  {
    mQueue.removeCallbacksAndMessages(this, paramObject);
  }
  
  public final void removeMessages(int paramInt)
  {
    mQueue.removeMessages(this, paramInt, null);
  }
  
  public final void removeMessages(int paramInt, Object paramObject)
  {
    mQueue.removeMessages(this, paramInt, paramObject);
  }
  
  public final boolean runWithScissors(Runnable paramRunnable, long paramLong)
  {
    if (paramRunnable != null)
    {
      if (paramLong >= 0L)
      {
        if (Looper.myLooper() == mLooper)
        {
          paramRunnable.run();
          return true;
        }
        return new BlockingRunnable(paramRunnable).postAndWait(this, paramLong);
      }
      throw new IllegalArgumentException("timeout must be non-negative");
    }
    throw new IllegalArgumentException("runnable must not be null");
  }
  
  public final boolean sendEmptyMessage(int paramInt)
  {
    return sendEmptyMessageDelayed(paramInt, 0L);
  }
  
  public final boolean sendEmptyMessageAtTime(int paramInt, long paramLong)
  {
    Message localMessage = Message.obtain();
    what = paramInt;
    return sendMessageAtTime(localMessage, paramLong);
  }
  
  public final boolean sendEmptyMessageDelayed(int paramInt, long paramLong)
  {
    Message localMessage = Message.obtain();
    what = paramInt;
    return sendMessageDelayed(localMessage, paramLong);
  }
  
  public final boolean sendMessage(Message paramMessage)
  {
    return sendMessageDelayed(paramMessage, 0L);
  }
  
  public final boolean sendMessageAtFrontOfQueue(Message paramMessage)
  {
    MessageQueue localMessageQueue = mQueue;
    if (localMessageQueue == null)
    {
      paramMessage = new StringBuilder();
      paramMessage.append(this);
      paramMessage.append(" sendMessageAtTime() called with no mQueue");
      paramMessage = new RuntimeException(paramMessage.toString());
      Log.w("Looper", paramMessage.getMessage(), paramMessage);
      return false;
    }
    return enqueueMessage(localMessageQueue, paramMessage, 0L);
  }
  
  public boolean sendMessageAtTime(Message paramMessage, long paramLong)
  {
    MessageQueue localMessageQueue = mQueue;
    if (localMessageQueue == null)
    {
      paramMessage = new StringBuilder();
      paramMessage.append(this);
      paramMessage.append(" sendMessageAtTime() called with no mQueue");
      paramMessage = new RuntimeException(paramMessage.toString());
      Log.w("Looper", paramMessage.getMessage(), paramMessage);
      return false;
    }
    return enqueueMessage(localMessageQueue, paramMessage, paramLong);
  }
  
  public final boolean sendMessageDelayed(Message paramMessage, long paramLong)
  {
    long l = paramLong;
    if (paramLong < 0L) {
      l = 0L;
    }
    return sendMessageAtTime(paramMessage, SystemClock.uptimeMillis() + l);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Handler (");
    localStringBuilder.append(getClass().getName());
    localStringBuilder.append(") {");
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  private static final class BlockingRunnable
    implements Runnable
  {
    private boolean mDone;
    private final Runnable mTask;
    
    public BlockingRunnable(Runnable paramRunnable)
    {
      mTask = paramRunnable;
    }
    
    public boolean postAndWait(Handler paramHandler, long paramLong)
    {
      if (!paramHandler.post(this)) {
        return false;
      }
      if (paramLong > 0L) {
        try
        {
          long l1 = SystemClock.uptimeMillis();
          if (!mDone)
          {
            long l2 = l1 + paramLong - SystemClock.uptimeMillis();
            if (l2 <= 0L) {
              return false;
            }
            try
            {
              wait(l2);
            }
            catch (InterruptedException paramHandler) {}
          }
        }
        finally
        {
          break label97;
        }
      }
      for (;;)
      {
        boolean bool = mDone;
        if (!bool) {
          try
          {
            wait();
          }
          catch (InterruptedException paramHandler)
          {
            for (;;) {}
          }
        }
      }
      return true;
      label97:
      throw paramHandler;
    }
    
    /* Error */
    public void run()
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 20	android/os/Handler$BlockingRunnable:mTask	Ljava/lang/Runnable;
      //   4: invokeinterface 46 1 0
      //   9: aload_0
      //   10: monitorenter
      //   11: aload_0
      //   12: iconst_1
      //   13: putfield 37	android/os/Handler$BlockingRunnable:mDone	Z
      //   16: aload_0
      //   17: invokevirtual 49	java/lang/Object:notifyAll	()V
      //   20: aload_0
      //   21: monitorexit
      //   22: return
      //   23: astore_1
      //   24: aload_0
      //   25: monitorexit
      //   26: aload_1
      //   27: athrow
      //   28: astore_1
      //   29: aload_0
      //   30: monitorenter
      //   31: aload_0
      //   32: iconst_1
      //   33: putfield 37	android/os/Handler$BlockingRunnable:mDone	Z
      //   36: aload_0
      //   37: invokevirtual 49	java/lang/Object:notifyAll	()V
      //   40: aload_0
      //   41: monitorexit
      //   42: aload_1
      //   43: athrow
      //   44: astore_1
      //   45: aload_0
      //   46: monitorexit
      //   47: aload_1
      //   48: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	49	0	this	BlockingRunnable
      //   23	4	1	localObject1	Object
      //   28	15	1	localObject2	Object
      //   44	4	1	localObject3	Object
      // Exception table:
      //   from	to	target	type
      //   11	22	23	finally
      //   24	26	23	finally
      //   0	9	28	finally
      //   31	42	44	finally
      //   45	47	44	finally
    }
  }
  
  public static abstract interface Callback
  {
    public abstract boolean handleMessage(Message paramMessage);
  }
  
  private final class MessengerImpl
    extends IMessenger.Stub
  {
    private MessengerImpl() {}
    
    public void send(Message paramMessage)
    {
      sendingUid = Binder.getCallingUid();
      sendMessage(paramMessage);
    }
  }
}
