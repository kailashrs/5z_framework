package com.android.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.Rlog;
import android.util.LocalLog;
import com.android.internal.util.IndentingPrintWriter;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class SimActivationTracker
{
  private static final boolean DBG = true;
  private static final String LOG_TAG = "SAT";
  private static final boolean VDBG = Rlog.isLoggable("SAT", 2);
  private int mDataActivationState;
  private final LocalLog mDataActivationStateLog = new LocalLog(10);
  private Phone mPhone;
  private final BroadcastReceiver mReceiver;
  private int mVoiceActivationState;
  private final LocalLog mVoiceActivationStateLog = new LocalLog(10);
  
  public SimActivationTracker(Phone paramPhone)
  {
    mPhone = paramPhone;
    mVoiceActivationState = 0;
    mDataActivationState = 0;
    mReceiver = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        paramAnonymousContext = paramAnonymousIntent.getAction();
        if (SimActivationTracker.VDBG)
        {
          SimActivationTracker localSimActivationTracker = SimActivationTracker.this;
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("action: ");
          localStringBuilder.append(paramAnonymousContext);
          localSimActivationTracker.log(localStringBuilder.toString());
        }
        if (("android.intent.action.SIM_STATE_CHANGED".equals(paramAnonymousContext)) && ("ABSENT".equals(paramAnonymousIntent.getStringExtra("ss"))))
        {
          SimActivationTracker.this.log("onSimAbsent, reset activation state to UNKNOWN");
          setVoiceActivationState(0);
          setDataActivationState(0);
        }
      }
    };
    paramPhone = new IntentFilter("android.intent.action.SIM_STATE_CHANGED");
    mPhone.getContext().registerReceiver(mReceiver, paramPhone);
  }
  
  private static boolean isValidActivationState(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return false;
    }
    return true;
  }
  
  private void log(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(mPhone.getPhoneId());
    localStringBuilder.append("]");
    localStringBuilder.append(paramString);
    Rlog.d("SAT", localStringBuilder.toString());
  }
  
  private void loge(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(mPhone.getPhoneId());
    localStringBuilder.append("]");
    localStringBuilder.append(paramString);
    Rlog.e("SAT", localStringBuilder.toString());
  }
  
  private static String toString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "invalid";
    case 4: 
      return "restricted";
    case 3: 
      return "deactivated";
    case 2: 
      return "activated";
    case 1: 
      return "activating";
    }
    return "unknown";
  }
  
  public void dispose()
  {
    mPhone.getContext().unregisterReceiver(mReceiver);
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    IndentingPrintWriter localIndentingPrintWriter = new IndentingPrintWriter(paramPrintWriter, "  ");
    paramPrintWriter.println(" mVoiceActivationState Log:");
    localIndentingPrintWriter.increaseIndent();
    mVoiceActivationStateLog.dump(paramFileDescriptor, localIndentingPrintWriter, paramArrayOfString);
    localIndentingPrintWriter.decreaseIndent();
    paramPrintWriter.println(" mDataActivationState Log:");
    localIndentingPrintWriter.increaseIndent();
    mDataActivationStateLog.dump(paramFileDescriptor, localIndentingPrintWriter, paramArrayOfString);
    localIndentingPrintWriter.decreaseIndent();
  }
  
  public int getDataActivationState()
  {
    return mDataActivationState;
  }
  
  public int getVoiceActivationState()
  {
    return mVoiceActivationState;
  }
  
  public void setDataActivationState(int paramInt)
  {
    if (isValidActivationState(paramInt))
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("setDataActivationState=");
      localStringBuilder.append(paramInt);
      log(localStringBuilder.toString());
      mDataActivationState = paramInt;
      mDataActivationStateLog.log(toString(paramInt));
      mPhone.notifyDataActivationStateChanged(paramInt);
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("invalid data activation state: ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public void setVoiceActivationState(int paramInt)
  {
    if ((isValidActivationState(paramInt)) && (4 != paramInt))
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("setVoiceActivationState=");
      localStringBuilder.append(paramInt);
      log(localStringBuilder.toString());
      mVoiceActivationState = paramInt;
      mVoiceActivationStateLog.log(toString(paramInt));
      mPhone.notifyVoiceActivationStateChanged(paramInt);
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("invalid voice activation state: ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
}
