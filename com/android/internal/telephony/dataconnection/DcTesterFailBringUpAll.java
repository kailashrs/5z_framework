package com.android.internal.telephony.dataconnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.telephony.Rlog;
import com.android.internal.telephony.Phone;

public class DcTesterFailBringUpAll
{
  private static final boolean DBG = true;
  private static final String LOG_TAG = "DcTesterFailBrinupAll";
  private String mActionFailBringUp;
  private DcFailBringUp mFailBringUp;
  private BroadcastReceiver mIntentReceiver;
  private Phone mPhone;
  
  DcTesterFailBringUpAll(Phone paramPhone, Handler paramHandler)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(DcFailBringUp.INTENT_BASE);
    ((StringBuilder)localObject).append(".");
    ((StringBuilder)localObject).append("action_fail_bringup");
    mActionFailBringUp = ((StringBuilder)localObject).toString();
    mFailBringUp = new DcFailBringUp();
    mIntentReceiver = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        paramAnonymousContext = paramAnonymousIntent.getAction();
        DcTesterFailBringUpAll localDcTesterFailBringUpAll = DcTesterFailBringUpAll.this;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("sIntentReceiver.onReceive: action=");
        localStringBuilder.append(paramAnonymousContext);
        localDcTesterFailBringUpAll.log(localStringBuilder.toString());
        if (paramAnonymousContext.equals(mActionFailBringUp))
        {
          mFailBringUp.saveParameters(paramAnonymousIntent, "sFailBringUp");
        }
        else if (paramAnonymousContext.equals(mPhone.getActionDetached()))
        {
          DcTesterFailBringUpAll.this.log("simulate detaching");
          mFailBringUp.saveParameters(Integer.MAX_VALUE, DcFailCause.LOST_CONNECTION.getErrorCode(), -1);
        }
        else if (paramAnonymousContext.equals(mPhone.getActionAttached()))
        {
          DcTesterFailBringUpAll.this.log("simulate attaching");
          mFailBringUp.saveParameters(0, DcFailCause.NONE.getErrorCode(), -1);
        }
        else
        {
          paramAnonymousIntent = DcTesterFailBringUpAll.this;
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("onReceive: unknown action=");
          localStringBuilder.append(paramAnonymousContext);
          paramAnonymousIntent.log(localStringBuilder.toString());
        }
      }
    };
    mPhone = paramPhone;
    if (Build.IS_DEBUGGABLE)
    {
      localObject = new IntentFilter();
      ((IntentFilter)localObject).addAction(mActionFailBringUp);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("register for intent action=");
      localStringBuilder.append(mActionFailBringUp);
      log(localStringBuilder.toString());
      ((IntentFilter)localObject).addAction(mPhone.getActionDetached());
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("register for intent action=");
      localStringBuilder.append(mPhone.getActionDetached());
      log(localStringBuilder.toString());
      ((IntentFilter)localObject).addAction(mPhone.getActionAttached());
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("register for intent action=");
      localStringBuilder.append(mPhone.getActionAttached());
      log(localStringBuilder.toString());
      paramPhone.getContext().registerReceiver(mIntentReceiver, (IntentFilter)localObject, null, paramHandler);
    }
  }
  
  private void log(String paramString)
  {
    Rlog.d("DcTesterFailBrinupAll", paramString);
  }
  
  void dispose()
  {
    if (Build.IS_DEBUGGABLE) {
      mPhone.getContext().unregisterReceiver(mIntentReceiver);
    }
  }
  
  public DcFailBringUp getDcFailBringUp()
  {
    return mFailBringUp;
  }
}
