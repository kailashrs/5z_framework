package com.android.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;

@Deprecated
public final class PhoneStateIntentReceiver
  extends BroadcastReceiver
{
  private static final boolean DBG = false;
  private static final String LOG_TAG = "PhoneStatIntentReceiver";
  private static final int NOTIF_PHONE = 1;
  private static final int NOTIF_SERVICE = 2;
  private static final int NOTIF_SIGNAL = 4;
  private int mAsuEventWhat;
  private Context mContext;
  private IntentFilter mFilter = new IntentFilter();
  PhoneConstants.State mPhoneState = PhoneConstants.State.IDLE;
  private int mPhoneStateEventWhat;
  ServiceState mServiceState = new ServiceState();
  private int mServiceStateEventWhat;
  SignalStrength mSignalStrength = new SignalStrength();
  private Handler mTarget;
  private int mWants;
  
  public PhoneStateIntentReceiver() {}
  
  public PhoneStateIntentReceiver(Context paramContext, Handler paramHandler)
  {
    this();
    setContext(paramContext);
    setTarget(paramHandler);
  }
  
  public boolean getNotifyPhoneCallState()
  {
    int i = mWants;
    boolean bool = true;
    if ((i & 0x1) == 0) {
      bool = false;
    }
    return bool;
  }
  
  public boolean getNotifyServiceState()
  {
    boolean bool;
    if ((mWants & 0x2) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean getNotifySignalStrength()
  {
    boolean bool;
    if ((mWants & 0x4) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public PhoneConstants.State getPhoneState()
  {
    if ((mWants & 0x1) != 0) {
      return mPhoneState;
    }
    throw new RuntimeException("client must call notifyPhoneCallState(int)");
  }
  
  public ServiceState getServiceState()
  {
    if ((mWants & 0x2) != 0) {
      return mServiceState;
    }
    throw new RuntimeException("client must call notifyServiceState(int)");
  }
  
  public int getSignalStrengthDbm()
  {
    if ((mWants & 0x4) != 0) {
      return mSignalStrength.getDbm();
    }
    throw new RuntimeException("client must call notifySignalStrength(int)");
  }
  
  public int getSignalStrengthLevelAsu()
  {
    if ((mWants & 0x4) != 0) {
      return mSignalStrength.getAsuLevel();
    }
    throw new RuntimeException("client must call notifySignalStrength(int)");
  }
  
  public void notifyPhoneCallState(int paramInt)
  {
    mWants |= 0x1;
    mPhoneStateEventWhat = paramInt;
    mFilter.addAction("android.intent.action.PHONE_STATE");
  }
  
  public void notifyServiceState(int paramInt)
  {
    mWants |= 0x2;
    mServiceStateEventWhat = paramInt;
    mFilter.addAction("android.intent.action.SERVICE_STATE");
  }
  
  public void notifySignalStrength(int paramInt)
  {
    mWants |= 0x4;
    mAsuEventWhat = paramInt;
    mFilter.addAction("android.intent.action.SIG_STR");
  }
  
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    paramContext = paramIntent.getAction();
    try
    {
      if ("android.intent.action.SIG_STR".equals(paramContext))
      {
        mSignalStrength = SignalStrength.newFromBundle(paramIntent.getExtras());
        if ((mTarget != null) && (getNotifySignalStrength()))
        {
          paramContext = Message.obtain(mTarget, mAsuEventWhat);
          mTarget.sendMessage(paramContext);
        }
      }
      else if ("android.intent.action.PHONE_STATE".equals(paramContext))
      {
        mPhoneState = ((PhoneConstants.State)Enum.valueOf(PhoneConstants.State.class, paramIntent.getStringExtra("state")));
        if ((mTarget != null) && (getNotifyPhoneCallState()))
        {
          paramContext = Message.obtain(mTarget, mPhoneStateEventWhat);
          mTarget.sendMessage(paramContext);
        }
      }
      else if ("android.intent.action.SERVICE_STATE".equals(paramContext))
      {
        mServiceState = ServiceState.newFromBundle(paramIntent.getExtras());
        if ((mTarget != null) && (getNotifyServiceState()))
        {
          paramContext = Message.obtain(mTarget, mServiceStateEventWhat);
          mTarget.sendMessage(paramContext);
        }
      }
    }
    catch (Exception paramContext)
    {
      paramIntent = new StringBuilder();
      paramIntent.append("[PhoneStateIntentRecv] caught ");
      paramIntent.append(paramContext);
      Rlog.e("PhoneStatIntentReceiver", paramIntent.toString());
      paramContext.printStackTrace();
    }
  }
  
  public void registerIntent()
  {
    mContext.registerReceiver(this, mFilter);
  }
  
  public void setContext(Context paramContext)
  {
    mContext = paramContext;
  }
  
  public void setTarget(Handler paramHandler)
  {
    mTarget = paramHandler;
  }
  
  public void unregisterIntent()
  {
    mContext.unregisterReceiver(this);
  }
}
