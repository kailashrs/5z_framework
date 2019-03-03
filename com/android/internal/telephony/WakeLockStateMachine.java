package com.android.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telephony.Rlog;
import com.android.internal.util.State;
import com.android.internal.util.StateMachine;

public abstract class WakeLockStateMachine
  extends StateMachine
{
  protected static final boolean DBG = true;
  protected static final int EVENT_BROADCAST_COMPLETE = 2;
  public static final int EVENT_NEW_SMS_MESSAGE = 1;
  static final int EVENT_RELEASE_WAKE_LOCK = 3;
  static final int EVENT_UPDATE_PHONE_OBJECT = 4;
  private static final int WAKE_LOCK_TIMEOUT = 3000;
  protected Context mContext;
  private final DefaultState mDefaultState = new DefaultState();
  private final IdleState mIdleState = new IdleState();
  protected Phone mPhone;
  protected final BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      sendMessage(2);
    }
  };
  private final WaitingState mWaitingState = new WaitingState();
  private final PowerManager.WakeLock mWakeLock;
  
  protected WakeLockStateMachine(String paramString, Context paramContext, Phone paramPhone)
  {
    super(paramString);
    mContext = paramContext;
    mPhone = paramPhone;
    mWakeLock = ((PowerManager)paramContext.getSystemService("power")).newWakeLock(1, paramString);
    mWakeLock.acquire();
    addState(mDefaultState);
    addState(mIdleState, mDefaultState);
    addState(mWaitingState, mDefaultState);
    setInitialState(mIdleState);
  }
  
  public final void dispatchSmsMessage(Object paramObject)
  {
    sendMessage(1, paramObject);
  }
  
  public final void dispose()
  {
    quit();
  }
  
  protected abstract boolean handleSmsMessage(Message paramMessage);
  
  protected void log(String paramString)
  {
    Rlog.d(getName(), paramString);
  }
  
  protected void loge(String paramString)
  {
    Rlog.e(getName(), paramString);
  }
  
  protected void loge(String paramString, Throwable paramThrowable)
  {
    Rlog.e(getName(), paramString, paramThrowable);
  }
  
  protected void onQuitting()
  {
    while (mWakeLock.isHeld()) {
      mWakeLock.release();
    }
  }
  
  public void updatePhoneObject(Phone paramPhone)
  {
    sendMessage(4, paramPhone);
  }
  
  class DefaultState
    extends State
  {
    DefaultState() {}
    
    public boolean processMessage(Message paramMessage)
    {
      StringBuilder localStringBuilder;
      if (what != 4)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("processMessage: unhandled message type ");
        localStringBuilder.append(what);
        paramMessage = localStringBuilder.toString();
        if (!Build.IS_DEBUGGABLE) {
          loge(paramMessage);
        } else {
          throw new RuntimeException(paramMessage);
        }
      }
      else
      {
        mPhone = ((Phone)obj);
        paramMessage = WakeLockStateMachine.this;
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("updatePhoneObject: phone=");
        localStringBuilder.append(mPhone.getClass().getSimpleName());
        paramMessage.log(localStringBuilder.toString());
      }
      return true;
    }
  }
  
  class IdleState
    extends State
  {
    IdleState() {}
    
    public void enter()
    {
      sendMessageDelayed(3, 3000L);
    }
    
    public void exit()
    {
      mWakeLock.acquire();
      log("acquired wakelock, leaving Idle state");
    }
    
    public boolean processMessage(Message paramMessage)
    {
      int i = what;
      if (i != 1)
      {
        if (i != 3) {
          return false;
        }
        mWakeLock.release();
        if (mWakeLock.isHeld()) {
          log("mWakeLock is still held after release");
        } else {
          log("mWakeLock released");
        }
        return true;
      }
      if (handleSmsMessage(paramMessage)) {
        transitionTo(mWaitingState);
      }
      return true;
    }
  }
  
  class WaitingState
    extends State
  {
    WaitingState() {}
    
    public boolean processMessage(Message paramMessage)
    {
      switch (what)
      {
      default: 
        return false;
      case 3: 
        mWakeLock.release();
        if (!mWakeLock.isHeld()) {
          loge("mWakeLock released while still in WaitingState!");
        }
        return true;
      case 2: 
        log("broadcast complete, returning to idle");
        transitionTo(mIdleState);
        return true;
      }
      log("deferring message until return to idle");
      deferMessage(paramMessage);
      return true;
    }
  }
}
