package com.android.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;

public class SmsStorageMonitor
  extends Handler
{
  private static final int EVENT_ICC_FULL = 1;
  private static final int EVENT_RADIO_ON = 3;
  private static final int EVENT_REPORT_MEMORY_STATUS_DONE = 2;
  private static final String TAG = "SmsStorageMonitor";
  private static final int WAKE_LOCK_TIMEOUT = 5000;
  final CommandsInterface mCi;
  private final Context mContext;
  Phone mPhone;
  private boolean mReportMemoryStatusPending;
  private final BroadcastReceiver mResultReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      if (paramAnonymousIntent.getAction().equals("android.intent.action.DEVICE_STORAGE_FULL"))
      {
        mStorageAvailable = false;
        mCi.reportSmsMemoryStatus(false, obtainMessage(2));
      }
      else if (paramAnonymousIntent.getAction().equals("android.intent.action.DEVICE_STORAGE_NOT_FULL"))
      {
        mStorageAvailable = true;
        mCi.reportSmsMemoryStatus(true, obtainMessage(2));
      }
    }
  };
  boolean mStorageAvailable = true;
  private PowerManager.WakeLock mWakeLock;
  
  public SmsStorageMonitor(Phone paramPhone)
  {
    mPhone = paramPhone;
    mContext = paramPhone.getContext();
    mCi = mCi;
    createWakelock();
    mCi.setOnIccSmsFull(this, 1, null);
    mCi.registerForOn(this, 3, null);
    paramPhone = new IntentFilter();
    paramPhone.addAction("android.intent.action.DEVICE_STORAGE_FULL");
    paramPhone.addAction("android.intent.action.DEVICE_STORAGE_NOT_FULL");
    mContext.registerReceiver(mResultReceiver, paramPhone);
  }
  
  private void createWakelock()
  {
    mWakeLock = ((PowerManager)mContext.getSystemService("power")).newWakeLock(1, "SmsStorageMonitor");
    mWakeLock.setReferenceCounted(true);
  }
  
  private void handleIccFull()
  {
    Intent localIntent = new Intent("android.provider.Telephony.SIM_FULL");
    localIntent.setComponent(SmsApplication.getDefaultSimFullApplication(mContext, false));
    mWakeLock.acquire(5000L);
    SubscriptionManager.putPhoneIdAndSubIdExtra(localIntent, mPhone.getPhoneId());
    mContext.sendBroadcast(localIntent, "android.permission.RECEIVE_SMS");
  }
  
  public void dispose()
  {
    mCi.unSetOnIccSmsFull(this);
    mCi.unregisterForOn(this);
    mContext.unregisterReceiver(mResultReceiver);
  }
  
  public void handleMessage(Message paramMessage)
  {
    switch (what)
    {
    default: 
      break;
    case 3: 
      if (mReportMemoryStatusPending)
      {
        paramMessage = new StringBuilder();
        paramMessage.append("Sending pending memory status report : mStorageAvailable = ");
        paramMessage.append(mStorageAvailable);
        Rlog.v("SmsStorageMonitor", paramMessage.toString());
        mCi.reportSmsMemoryStatus(mStorageAvailable, obtainMessage(2));
      }
      break;
    case 2: 
      if (obj).exception != null)
      {
        mReportMemoryStatusPending = true;
        paramMessage = new StringBuilder();
        paramMessage.append("Memory status report to modem pending : mStorageAvailable = ");
        paramMessage.append(mStorageAvailable);
        Rlog.v("SmsStorageMonitor", paramMessage.toString());
      }
      else
      {
        mReportMemoryStatusPending = false;
      }
      break;
    case 1: 
      handleIccFull();
    }
  }
  
  public boolean isStorageAvailable()
  {
    return mStorageAvailable;
  }
}
