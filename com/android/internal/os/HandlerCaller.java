package com.android.internal.os;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class HandlerCaller
{
  final Callback mCallback;
  final Handler mH;
  final Looper mMainLooper;
  
  public HandlerCaller(Context paramContext, Looper paramLooper, Callback paramCallback, boolean paramBoolean)
  {
    if (paramLooper != null) {
      paramContext = paramLooper;
    } else {
      paramContext = paramContext.getMainLooper();
    }
    mMainLooper = paramContext;
    mH = new MyHandler(mMainLooper, paramBoolean);
    mCallback = paramCallback;
  }
  
  public void executeOrSendMessage(Message paramMessage)
  {
    if (Looper.myLooper() == mMainLooper)
    {
      mCallback.executeMessage(paramMessage);
      paramMessage.recycle();
      return;
    }
    mH.sendMessage(paramMessage);
  }
  
  public Handler getHandler()
  {
    return mH;
  }
  
  public boolean hasMessages(int paramInt)
  {
    return mH.hasMessages(paramInt);
  }
  
  public Message obtainMessage(int paramInt)
  {
    return mH.obtainMessage(paramInt);
  }
  
  public Message obtainMessageBO(int paramInt, boolean paramBoolean, Object paramObject)
  {
    return mH.obtainMessage(paramInt, paramBoolean, 0, paramObject);
  }
  
  public Message obtainMessageBOO(int paramInt, boolean paramBoolean, Object paramObject1, Object paramObject2)
  {
    SomeArgs localSomeArgs = SomeArgs.obtain();
    arg1 = paramObject1;
    arg2 = paramObject2;
    return mH.obtainMessage(paramInt, paramBoolean, 0, localSomeArgs);
  }
  
  public Message obtainMessageI(int paramInt1, int paramInt2)
  {
    return mH.obtainMessage(paramInt1, paramInt2, 0);
  }
  
  public Message obtainMessageII(int paramInt1, int paramInt2, int paramInt3)
  {
    return mH.obtainMessage(paramInt1, paramInt2, paramInt3);
  }
  
  public Message obtainMessageIIII(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    SomeArgs localSomeArgs = SomeArgs.obtain();
    argi1 = paramInt2;
    argi2 = paramInt3;
    argi3 = paramInt4;
    argi4 = paramInt5;
    return mH.obtainMessage(paramInt1, 0, 0, localSomeArgs);
  }
  
  public Message obtainMessageIIIIII(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
  {
    SomeArgs localSomeArgs = SomeArgs.obtain();
    argi1 = paramInt2;
    argi2 = paramInt3;
    argi3 = paramInt4;
    argi4 = paramInt5;
    argi5 = paramInt6;
    argi6 = paramInt7;
    return mH.obtainMessage(paramInt1, 0, 0, localSomeArgs);
  }
  
  public Message obtainMessageIIIIO(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, Object paramObject)
  {
    SomeArgs localSomeArgs = SomeArgs.obtain();
    arg1 = paramObject;
    argi1 = paramInt2;
    argi2 = paramInt3;
    argi3 = paramInt4;
    argi4 = paramInt5;
    return mH.obtainMessage(paramInt1, 0, 0, localSomeArgs);
  }
  
  public Message obtainMessageIIO(int paramInt1, int paramInt2, int paramInt3, Object paramObject)
  {
    return mH.obtainMessage(paramInt1, paramInt2, paramInt3, paramObject);
  }
  
  public Message obtainMessageIIOO(int paramInt1, int paramInt2, int paramInt3, Object paramObject1, Object paramObject2)
  {
    SomeArgs localSomeArgs = SomeArgs.obtain();
    arg1 = paramObject1;
    arg2 = paramObject2;
    return mH.obtainMessage(paramInt1, paramInt2, paramInt3, localSomeArgs);
  }
  
  public Message obtainMessageIIOOO(int paramInt1, int paramInt2, int paramInt3, Object paramObject1, Object paramObject2, Object paramObject3)
  {
    SomeArgs localSomeArgs = SomeArgs.obtain();
    arg1 = paramObject1;
    arg2 = paramObject2;
    arg3 = paramObject3;
    return mH.obtainMessage(paramInt1, paramInt2, paramInt3, localSomeArgs);
  }
  
  public Message obtainMessageIIOOOO(int paramInt1, int paramInt2, int paramInt3, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4)
  {
    SomeArgs localSomeArgs = SomeArgs.obtain();
    arg1 = paramObject1;
    arg2 = paramObject2;
    arg3 = paramObject3;
    arg4 = paramObject4;
    return mH.obtainMessage(paramInt1, paramInt2, paramInt3, localSomeArgs);
  }
  
  public Message obtainMessageIO(int paramInt1, int paramInt2, Object paramObject)
  {
    return mH.obtainMessage(paramInt1, paramInt2, 0, paramObject);
  }
  
  public Message obtainMessageIOO(int paramInt1, int paramInt2, Object paramObject1, Object paramObject2)
  {
    SomeArgs localSomeArgs = SomeArgs.obtain();
    arg1 = paramObject1;
    arg2 = paramObject2;
    return mH.obtainMessage(paramInt1, paramInt2, 0, localSomeArgs);
  }
  
  public Message obtainMessageIOOO(int paramInt1, int paramInt2, Object paramObject1, Object paramObject2, Object paramObject3)
  {
    SomeArgs localSomeArgs = SomeArgs.obtain();
    arg1 = paramObject1;
    arg2 = paramObject2;
    arg3 = paramObject3;
    return mH.obtainMessage(paramInt1, paramInt2, 0, localSomeArgs);
  }
  
  public Message obtainMessageO(int paramInt, Object paramObject)
  {
    return mH.obtainMessage(paramInt, 0, 0, paramObject);
  }
  
  public Message obtainMessageOO(int paramInt, Object paramObject1, Object paramObject2)
  {
    SomeArgs localSomeArgs = SomeArgs.obtain();
    arg1 = paramObject1;
    arg2 = paramObject2;
    return mH.obtainMessage(paramInt, 0, 0, localSomeArgs);
  }
  
  public Message obtainMessageOOO(int paramInt, Object paramObject1, Object paramObject2, Object paramObject3)
  {
    SomeArgs localSomeArgs = SomeArgs.obtain();
    arg1 = paramObject1;
    arg2 = paramObject2;
    arg3 = paramObject3;
    return mH.obtainMessage(paramInt, 0, 0, localSomeArgs);
  }
  
  public Message obtainMessageOOOO(int paramInt, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4)
  {
    SomeArgs localSomeArgs = SomeArgs.obtain();
    arg1 = paramObject1;
    arg2 = paramObject2;
    arg3 = paramObject3;
    arg4 = paramObject4;
    return mH.obtainMessage(paramInt, 0, 0, localSomeArgs);
  }
  
  public Message obtainMessageOOOOII(int paramInt1, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, int paramInt2, int paramInt3)
  {
    SomeArgs localSomeArgs = SomeArgs.obtain();
    arg1 = paramObject1;
    arg2 = paramObject2;
    arg3 = paramObject3;
    arg4 = paramObject4;
    argi5 = paramInt2;
    argi6 = paramInt3;
    return mH.obtainMessage(paramInt1, 0, 0, localSomeArgs);
  }
  
  public Message obtainMessageOOOOO(int paramInt, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5)
  {
    SomeArgs localSomeArgs = SomeArgs.obtain();
    arg1 = paramObject1;
    arg2 = paramObject2;
    arg3 = paramObject3;
    arg4 = paramObject4;
    arg5 = paramObject5;
    return mH.obtainMessage(paramInt, 0, 0, localSomeArgs);
  }
  
  public void removeMessages(int paramInt)
  {
    mH.removeMessages(paramInt);
  }
  
  public void removeMessages(int paramInt, Object paramObject)
  {
    mH.removeMessages(paramInt, paramObject);
  }
  
  public void sendMessage(Message paramMessage)
  {
    mH.sendMessage(paramMessage);
  }
  
  public SomeArgs sendMessageAndWait(Message paramMessage)
  {
    if (Looper.myLooper() != mH.getLooper())
    {
      SomeArgs localSomeArgs = (SomeArgs)obj;
      mWaitState = 1;
      mH.sendMessage(paramMessage);
      try
      {
        for (;;)
        {
          int i = mWaitState;
          if (i == 1) {
            try
            {
              localSomeArgs.wait();
            }
            catch (InterruptedException paramMessage)
            {
              return null;
            }
          }
        }
        mWaitState = 0;
        return localSomeArgs;
      }
      finally {}
    }
    throw new IllegalStateException("Can't wait on same thread as looper");
  }
  
  public void sendMessageDelayed(Message paramMessage, long paramLong)
  {
    mH.sendMessageDelayed(paramMessage, paramLong);
  }
  
  public static abstract interface Callback
  {
    public abstract void executeMessage(Message paramMessage);
  }
  
  class MyHandler
    extends Handler
  {
    MyHandler(Looper paramLooper, boolean paramBoolean)
    {
      super(null, paramBoolean);
    }
    
    public void handleMessage(Message paramMessage)
    {
      mCallback.executeMessage(paramMessage);
    }
  }
}
