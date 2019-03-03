package android.os;

import android.util.Log;
import android.util.Printer;
import android.util.SparseArray;
import android.util.proto.ProtoOutputStream;
import java.io.FileDescriptor;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

public final class MessageQueue
{
  private static final boolean DEBUG = false;
  private static final String TAG = "MessageQueue";
  private boolean mBlocked;
  private SparseArray<FileDescriptorRecord> mFileDescriptorRecords;
  private final ArrayList<IdleHandler> mIdleHandlers = new ArrayList();
  Message mMessages;
  private int mNextBarrierToken;
  private IdleHandler[] mPendingIdleHandlers;
  private long mPtr;
  private final boolean mQuitAllowed;
  private boolean mQuitting;
  
  MessageQueue(boolean paramBoolean)
  {
    mQuitAllowed = paramBoolean;
    mPtr = nativeInit();
  }
  
  private int dispatchEvents(int paramInt1, int paramInt2)
  {
    try
    {
      FileDescriptorRecord localFileDescriptorRecord = (FileDescriptorRecord)mFileDescriptorRecords.get(paramInt1);
      if (localFileDescriptorRecord == null) {
        return 0;
      }
      int i = mEvents;
      paramInt2 &= i;
      if (paramInt2 == 0) {
        return i;
      }
      OnFileDescriptorEventListener localOnFileDescriptorEventListener = mListener;
      int j = mSeq;
      int k = localOnFileDescriptorEventListener.onFileDescriptorEvents(mDescriptor, paramInt2);
      paramInt2 = k;
      if (k != 0) {
        paramInt2 = k | 0x4;
      }
      if (paramInt2 != i) {
        try
        {
          paramInt1 = mFileDescriptorRecords.indexOfKey(paramInt1);
          if ((paramInt1 >= 0) && (mFileDescriptorRecords.valueAt(paramInt1) == localFileDescriptorRecord) && (mSeq == j))
          {
            mEvents = paramInt2;
            if (paramInt2 == 0) {
              mFileDescriptorRecords.removeAt(paramInt1);
            }
          }
        }
        finally {}
      }
      return paramInt2;
    }
    finally {}
  }
  
  private void dispose()
  {
    if (mPtr != 0L)
    {
      nativeDestroy(mPtr);
      mPtr = 0L;
    }
  }
  
  private boolean isPollingLocked()
  {
    boolean bool;
    if ((!mQuitting) && (nativeIsPolling(mPtr))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static native void nativeDestroy(long paramLong);
  
  private static native long nativeInit();
  
  private static native boolean nativeIsPolling(long paramLong);
  
  private native void nativePollOnce(long paramLong, int paramInt);
  
  private static native void nativeSetFileDescriptorEvents(long paramLong, int paramInt1, int paramInt2);
  
  private static native void nativeWake(long paramLong);
  
  private int postSyncBarrier(long paramLong)
  {
    try
    {
      int i = mNextBarrierToken;
      mNextBarrierToken = (i + 1);
      Message localMessage1 = Message.obtain();
      localMessage1.markInUse();
      when = paramLong;
      arg1 = i;
      Object localObject1 = null;
      Object localObject2 = null;
      Message localMessage2 = mMessages;
      Message localMessage3 = localMessage2;
      if (paramLong != 0L) {
        for (;;)
        {
          localObject1 = localObject2;
          localMessage3 = localMessage2;
          if (localMessage2 == null) {
            break;
          }
          localObject1 = localObject2;
          localMessage3 = localMessage2;
          if (when > paramLong) {
            break;
          }
          localObject2 = localMessage2;
          localMessage2 = next;
        }
      }
      if (localObject1 != null)
      {
        next = localMessage3;
        next = localMessage1;
      }
      else
      {
        next = localMessage3;
        mMessages = localMessage1;
      }
      return i;
    }
    finally {}
  }
  
  private void removeAllFutureMessagesLocked()
  {
    long l = SystemClock.uptimeMillis();
    Message localMessage1 = mMessages;
    if (localMessage1 != null)
    {
      Message localMessage2 = localMessage1;
      if (when > l) {
        removeAllMessagesLocked();
      } else {
        for (;;)
        {
          localMessage1 = next;
          if (localMessage1 == null) {
            return;
          }
          if (when > l)
          {
            next = null;
            localMessage2 = localMessage1;
            do
            {
              localMessage1 = next;
              localMessage2.recycleUnchecked();
              localMessage2 = localMessage1;
            } while (localMessage1 != null);
            break;
          }
          localMessage2 = localMessage1;
        }
      }
    }
  }
  
  private void removeAllMessagesLocked()
  {
    Message localMessage;
    for (Object localObject = mMessages; localObject != null; localObject = localMessage)
    {
      localMessage = next;
      ((Message)localObject).recycleUnchecked();
    }
    mMessages = null;
  }
  
  private void updateOnFileDescriptorEventListenerLocked(FileDescriptor paramFileDescriptor, int paramInt, OnFileDescriptorEventListener paramOnFileDescriptorEventListener)
  {
    int i = paramFileDescriptor.getInt$();
    int j = -1;
    FileDescriptorRecord localFileDescriptorRecord1 = null;
    FileDescriptorRecord localFileDescriptorRecord2 = localFileDescriptorRecord1;
    if (mFileDescriptorRecords != null)
    {
      int k = mFileDescriptorRecords.indexOfKey(i);
      j = k;
      localFileDescriptorRecord2 = localFileDescriptorRecord1;
      if (k >= 0)
      {
        localFileDescriptorRecord1 = (FileDescriptorRecord)mFileDescriptorRecords.valueAt(k);
        j = k;
        localFileDescriptorRecord2 = localFileDescriptorRecord1;
        if (localFileDescriptorRecord1 != null)
        {
          j = k;
          localFileDescriptorRecord2 = localFileDescriptorRecord1;
          if (mEvents == paramInt) {
            return;
          }
        }
      }
    }
    if (paramInt != 0)
    {
      paramInt |= 0x4;
      if (localFileDescriptorRecord2 == null)
      {
        if (mFileDescriptorRecords == null) {
          mFileDescriptorRecords = new SparseArray();
        }
        paramFileDescriptor = new FileDescriptorRecord(paramFileDescriptor, paramInt, paramOnFileDescriptorEventListener);
        mFileDescriptorRecords.put(i, paramFileDescriptor);
      }
      else
      {
        mListener = paramOnFileDescriptorEventListener;
        mEvents = paramInt;
        mSeq += 1;
      }
      nativeSetFileDescriptorEvents(mPtr, i, paramInt);
    }
    else if (localFileDescriptorRecord2 != null)
    {
      mEvents = 0;
      mFileDescriptorRecords.removeAt(j);
      nativeSetFileDescriptorEvents(mPtr, i, 0);
    }
  }
  
  public void addIdleHandler(IdleHandler paramIdleHandler)
  {
    if (paramIdleHandler != null) {
      try
      {
        mIdleHandlers.add(paramIdleHandler);
        return;
      }
      finally {}
    }
    throw new NullPointerException("Can't add a null IdleHandler");
  }
  
  public void addOnFileDescriptorEventListener(FileDescriptor paramFileDescriptor, int paramInt, OnFileDescriptorEventListener paramOnFileDescriptorEventListener)
  {
    if (paramFileDescriptor != null)
    {
      if (paramOnFileDescriptorEventListener != null) {
        try
        {
          updateOnFileDescriptorEventListenerLocked(paramFileDescriptor, paramInt, paramOnFileDescriptorEventListener);
          return;
        }
        finally {}
      }
      throw new IllegalArgumentException("listener must not be null");
    }
    throw new IllegalArgumentException("fd must not be null");
  }
  
  void dump(Printer paramPrinter, String paramString, Handler paramHandler)
  {
    try
    {
      long l = SystemClock.uptimeMillis();
      int i = 0;
      for (Message localMessage = mMessages; localMessage != null; localMessage = next)
      {
        if ((paramHandler == null) || (paramHandler == target))
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append(paramString);
          localStringBuilder.append("Message ");
          localStringBuilder.append(i);
          localStringBuilder.append(": ");
          localStringBuilder.append(localMessage.toString(l));
          paramPrinter.println(localStringBuilder.toString());
        }
        i++;
      }
      paramHandler = new java/lang/StringBuilder;
      paramHandler.<init>();
      paramHandler.append(paramString);
      paramHandler.append("(Total messages: ");
      paramHandler.append(i);
      paramHandler.append(", polling=");
      paramHandler.append(isPollingLocked());
      paramHandler.append(", quitting=");
      paramHandler.append(mQuitting);
      paramHandler.append(")");
      paramPrinter.println(paramHandler.toString());
      return;
    }
    finally {}
  }
  
  boolean enqueueMessage(Message paramMessage, long paramLong)
  {
    if (target != null)
    {
      if (!paramMessage.isInUse()) {
        try
        {
          boolean bool1 = mQuitting;
          boolean bool2 = false;
          if (bool1)
          {
            localObject1 = new java/lang/IllegalStateException;
            localObject2 = new java/lang/StringBuilder;
            ((StringBuilder)localObject2).<init>();
            ((StringBuilder)localObject2).append(target);
            ((StringBuilder)localObject2).append(" sending message to a Handler on a dead thread");
            ((IllegalStateException)localObject1).<init>(((StringBuilder)localObject2).toString());
            Log.w("MessageQueue", ((IllegalStateException)localObject1).getMessage(), (Throwable)localObject1);
            paramMessage.recycle();
            return false;
          }
          paramMessage.markInUse();
          when = paramLong;
          Object localObject1 = mMessages;
          if ((localObject1 != null) && (paramLong != 0L) && (paramLong >= when))
          {
            localObject2 = localObject1;
            bool1 = bool2;
            if (mBlocked)
            {
              localObject2 = localObject1;
              bool1 = bool2;
              if (target == null)
              {
                localObject2 = localObject1;
                bool1 = bool2;
                if (paramMessage.isAsynchronous())
                {
                  bool1 = true;
                  localObject2 = localObject1;
                }
              }
            }
            for (;;)
            {
              localObject1 = next;
              if ((localObject1 == null) || (paramLong < when)) {
                break;
              }
              localObject2 = localObject1;
              if (bool1)
              {
                localObject2 = localObject1;
                if (((Message)localObject1).isAsynchronous())
                {
                  bool1 = false;
                  localObject2 = localObject1;
                }
              }
            }
            next = ((Message)localObject1);
            next = paramMessage;
          }
          else
          {
            next = ((Message)localObject1);
            mMessages = paramMessage;
            bool1 = mBlocked;
          }
          if (bool1) {
            nativeWake(mPtr);
          }
          return true;
        }
        finally {}
      }
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append(paramMessage);
      ((StringBuilder)localObject2).append(" This message is already in use.");
      throw new IllegalStateException(((StringBuilder)localObject2).toString());
    }
    throw new IllegalArgumentException("Message must have a target.");
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      dispose();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  boolean hasMessages(Handler paramHandler)
  {
    if (paramHandler == null) {
      return false;
    }
    try
    {
      for (Message localMessage = mMessages; localMessage != null; localMessage = next) {
        if (target == paramHandler) {
          return true;
        }
      }
      return false;
    }
    finally {}
  }
  
  boolean hasMessages(Handler paramHandler, int paramInt, Object paramObject)
  {
    if (paramHandler == null) {
      return false;
    }
    try
    {
      for (Message localMessage = mMessages; localMessage != null; localMessage = next) {
        if ((target == paramHandler) && (what == paramInt) && ((paramObject == null) || (obj == paramObject))) {
          return true;
        }
      }
      return false;
    }
    finally {}
  }
  
  boolean hasMessages(Handler paramHandler, Runnable paramRunnable, Object paramObject)
  {
    if (paramHandler == null) {
      return false;
    }
    try
    {
      for (Message localMessage = mMessages; localMessage != null; localMessage = next) {
        if ((target == paramHandler) && (callback == paramRunnable) && ((paramObject == null) || (obj == paramObject))) {
          return true;
        }
      }
      return false;
    }
    finally {}
  }
  
  public boolean isIdle()
  {
    try
    {
      long l = SystemClock.uptimeMillis();
      boolean bool;
      if ((mMessages != null) && (l >= mMessages.when)) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    finally {}
  }
  
  public boolean isPolling()
  {
    try
    {
      boolean bool = isPollingLocked();
      return bool;
    }
    finally {}
  }
  
  Message next()
  {
    long l1 = mPtr;
    if (l1 == 0L) {
      return null;
    }
    int i = -1;
    int j = 0;
    for (;;)
    {
      if (j != 0) {
        Binder.flushPendingCommands();
      }
      nativePollOnce(l1, j);
      try
      {
        long l2 = SystemClock.uptimeMillis();
        Object localObject1 = null;
        Message localMessage1 = mMessages;
        Object localObject2 = localObject1;
        Message localMessage2 = localMessage1;
        if (localMessage1 != null)
        {
          localObject2 = localObject1;
          localMessage2 = localMessage1;
          if (target == null)
          {
            localMessage2 = localMessage1;
            do
            {
              localObject1 = localMessage2;
              localMessage1 = next;
              localObject2 = localObject1;
              localMessage2 = localMessage1;
              if (localMessage1 == null) {
                break;
              }
              localMessage2 = localMessage1;
            } while (!localMessage1.isAsynchronous());
            localMessage2 = localMessage1;
            localObject2 = localObject1;
          }
        }
        if (localMessage2 != null)
        {
          if (l2 < when)
          {
            j = (int)Math.min(when - l2, 2147483647L);
          }
          else
          {
            mBlocked = false;
            if (localObject2 != null) {
              next = next;
            } else {
              mMessages = next;
            }
            next = null;
            localMessage2.markInUse();
            return localMessage2;
          }
        }
        else {
          j = -1;
        }
        if (mQuitting)
        {
          dispose();
          return null;
        }
        int k = i;
        if (i < 0) {
          if (mMessages != null)
          {
            k = i;
            if (l2 >= mMessages.when) {}
          }
          else
          {
            k = mIdleHandlers.size();
          }
        }
        if (k <= 0)
        {
          mBlocked = true;
          i = k;
          continue;
        }
        if (mPendingIdleHandlers == null) {
          mPendingIdleHandlers = new IdleHandler[Math.max(k, 4)];
        }
        mPendingIdleHandlers = ((IdleHandler[])mIdleHandlers.toArray(mPendingIdleHandlers));
        for (i = 0; i < k; i++)
        {
          localObject2 = mPendingIdleHandlers[i];
          mPendingIdleHandlers[i] = null;
          int m = 0;
          try
          {
            boolean bool = ((IdleHandler)localObject2).queueIdle();
            m = bool;
          }
          catch (Throwable localThrowable)
          {
            Log.wtf("MessageQueue", "IdleHandler threw exception", localThrowable);
          }
          if (m == 0) {
            try
            {
              mIdleHandlers.remove(localObject2);
            }
            finally {}
          }
        }
        i = 0;
        j = 0;
      }
      finally {}
    }
  }
  
  public int postSyncBarrier()
  {
    return postSyncBarrier(SystemClock.uptimeMillis());
  }
  
  void quit(boolean paramBoolean)
  {
    if (mQuitAllowed) {
      try
      {
        if (mQuitting) {
          return;
        }
        mQuitting = true;
        if (paramBoolean) {
          removeAllFutureMessagesLocked();
        } else {
          removeAllMessagesLocked();
        }
        nativeWake(mPtr);
        return;
      }
      finally {}
    }
    throw new IllegalStateException("Main thread not allowed to quit.");
  }
  
  void removeCallbacksAndMessages(Handler paramHandler, Object paramObject)
  {
    if (paramHandler == null) {
      return;
    }
    try
    {
      Object localObject2;
      for (Object localObject1 = mMessages;; localObject1 = localObject2)
      {
        localObject2 = localObject1;
        if (localObject1 == null) {
          break;
        }
        localObject2 = localObject1;
        if (target != paramHandler) {
          break;
        }
        if (paramObject != null)
        {
          localObject2 = localObject1;
          if (obj != paramObject) {
            break;
          }
        }
        localObject2 = next;
        mMessages = ((Message)localObject2);
        ((Message)localObject1).recycleUnchecked();
      }
      while (localObject2 != null)
      {
        localObject1 = next;
        if ((localObject1 != null) && (target == paramHandler) && ((paramObject == null) || (obj == paramObject)))
        {
          Message localMessage = next;
          ((Message)localObject1).recycleUnchecked();
          next = localMessage;
        }
        else
        {
          localObject2 = localObject1;
        }
      }
      return;
    }
    finally {}
  }
  
  public void removeIdleHandler(IdleHandler paramIdleHandler)
  {
    try
    {
      mIdleHandlers.remove(paramIdleHandler);
      return;
    }
    finally {}
  }
  
  void removeMessages(Handler paramHandler, int paramInt, Object paramObject)
  {
    if (paramHandler == null) {
      return;
    }
    try
    {
      Object localObject2;
      for (Object localObject1 = mMessages;; localObject1 = localObject2)
      {
        localObject2 = localObject1;
        if (localObject1 == null) {
          break;
        }
        localObject2 = localObject1;
        if (target != paramHandler) {
          break;
        }
        localObject2 = localObject1;
        if (what != paramInt) {
          break;
        }
        if (paramObject != null)
        {
          localObject2 = localObject1;
          if (obj != paramObject) {
            break;
          }
        }
        localObject2 = next;
        mMessages = ((Message)localObject2);
        ((Message)localObject1).recycleUnchecked();
      }
      while (localObject2 != null)
      {
        localObject1 = next;
        if ((localObject1 != null) && (target == paramHandler) && (what == paramInt) && ((paramObject == null) || (obj == paramObject)))
        {
          Message localMessage = next;
          ((Message)localObject1).recycleUnchecked();
          next = localMessage;
        }
        else
        {
          localObject2 = localObject1;
        }
      }
      return;
    }
    finally {}
  }
  
  void removeMessages(Handler paramHandler, Runnable paramRunnable, Object paramObject)
  {
    if ((paramHandler != null) && (paramRunnable != null)) {
      try
      {
        Object localObject2;
        for (Object localObject1 = mMessages;; localObject1 = localObject2)
        {
          localObject2 = localObject1;
          if (localObject1 == null) {
            break;
          }
          localObject2 = localObject1;
          if (target != paramHandler) {
            break;
          }
          localObject2 = localObject1;
          if (callback != paramRunnable) {
            break;
          }
          if (paramObject != null)
          {
            localObject2 = localObject1;
            if (obj != paramObject) {
              break;
            }
          }
          localObject2 = next;
          mMessages = ((Message)localObject2);
          ((Message)localObject1).recycleUnchecked();
        }
        while (localObject2 != null)
        {
          localObject1 = next;
          if ((localObject1 != null) && (target == paramHandler) && (callback == paramRunnable) && ((paramObject == null) || (obj == paramObject)))
          {
            Message localMessage = next;
            ((Message)localObject1).recycleUnchecked();
            next = localMessage;
          }
          else
          {
            localObject2 = localObject1;
          }
        }
        return;
      }
      finally {}
    }
  }
  
  public void removeOnFileDescriptorEventListener(FileDescriptor paramFileDescriptor)
  {
    if (paramFileDescriptor != null) {
      try
      {
        updateOnFileDescriptorEventListenerLocked(paramFileDescriptor, 0, null);
        return;
      }
      finally {}
    }
    throw new IllegalArgumentException("fd must not be null");
  }
  
  public void removeSyncBarrier(int paramInt)
  {
    Object localObject1 = null;
    try
    {
      for (Object localObject2 = mMessages; (localObject2 != null) && ((target != null) || (arg1 != paramInt)); localObject2 = next) {
        localObject1 = localObject2;
      }
      if (localObject2 != null)
      {
        if (localObject1 != null)
        {
          next = next;
          paramInt = 0;
        }
        else
        {
          mMessages = next;
          if ((mMessages != null) && (mMessages.target == null)) {
            paramInt = 0;
          } else {
            paramInt = 1;
          }
        }
        ((Message)localObject2).recycleUnchecked();
        if ((paramInt != 0) && (!mQuitting)) {
          nativeWake(mPtr);
        }
        return;
      }
      localObject2 = new java/lang/IllegalStateException;
      ((IllegalStateException)localObject2).<init>("The specified message queue synchronization  barrier token has not been posted or has already been removed.");
      throw ((Throwable)localObject2);
    }
    finally {}
  }
  
  void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    try
    {
      for (Message localMessage = mMessages; localMessage != null; localMessage = next) {
        localMessage.writeToProto(paramProtoOutputStream, 2246267895809L);
      }
      paramProtoOutputStream.write(1133871366146L, isPollingLocked());
      paramProtoOutputStream.write(1133871366147L, mQuitting);
      paramProtoOutputStream.end(paramLong);
      return;
    }
    finally {}
  }
  
  private static final class FileDescriptorRecord
  {
    public final FileDescriptor mDescriptor;
    public int mEvents;
    public MessageQueue.OnFileDescriptorEventListener mListener;
    public int mSeq;
    
    public FileDescriptorRecord(FileDescriptor paramFileDescriptor, int paramInt, MessageQueue.OnFileDescriptorEventListener paramOnFileDescriptorEventListener)
    {
      mDescriptor = paramFileDescriptor;
      mEvents = paramInt;
      mListener = paramOnFileDescriptorEventListener;
    }
  }
  
  public static abstract interface IdleHandler
  {
    public abstract boolean queueIdle();
  }
  
  public static abstract interface OnFileDescriptorEventListener
  {
    public static final int EVENT_ERROR = 4;
    public static final int EVENT_INPUT = 1;
    public static final int EVENT_OUTPUT = 2;
    
    public abstract int onFileDescriptorEvents(FileDescriptor paramFileDescriptor, int paramInt);
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface Events {}
  }
}
