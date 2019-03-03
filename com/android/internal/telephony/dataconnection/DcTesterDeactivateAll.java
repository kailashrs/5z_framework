package com.android.internal.telephony.dataconnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.telephony.Rlog;
import com.android.internal.telephony.Phone;
import java.util.ArrayList;
import java.util.Iterator;

public class DcTesterDeactivateAll
{
  private static final boolean DBG = true;
  private static final String LOG_TAG = "DcTesterDeacativateAll";
  public static String sActionDcTesterDeactivateAll = "com.android.internal.telephony.dataconnection.action_deactivate_all";
  private DcController mDcc;
  private Phone mPhone;
  protected BroadcastReceiver sIntentReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      paramAnonymousContext = paramAnonymousIntent.getAction();
      paramAnonymousIntent = new StringBuilder();
      paramAnonymousIntent.append("sIntentReceiver.onReceive: action=");
      paramAnonymousIntent.append(paramAnonymousContext);
      DcTesterDeactivateAll.log(paramAnonymousIntent.toString());
      if ((!paramAnonymousContext.equals(DcTesterDeactivateAll.sActionDcTesterDeactivateAll)) && (!paramAnonymousContext.equals(mPhone.getActionDetached())))
      {
        paramAnonymousIntent = new StringBuilder();
        paramAnonymousIntent.append("onReceive: unknown action=");
        paramAnonymousIntent.append(paramAnonymousContext);
        DcTesterDeactivateAll.log(paramAnonymousIntent.toString());
      }
      else
      {
        DcTesterDeactivateAll.log("Send DEACTIVATE to all Dcc's");
        if (mDcc != null)
        {
          paramAnonymousContext = mDcc.mDcListAll.iterator();
          while (paramAnonymousContext.hasNext()) {
            ((DataConnection)paramAnonymousContext.next()).tearDownNow();
          }
        }
        DcTesterDeactivateAll.log("onReceive: mDcc is null, ignoring");
      }
    }
  };
  
  DcTesterDeactivateAll(Phone paramPhone, DcController paramDcController, Handler paramHandler)
  {
    mPhone = paramPhone;
    mDcc = paramDcController;
    if (Build.IS_DEBUGGABLE)
    {
      paramDcController = new IntentFilter();
      paramDcController.addAction(sActionDcTesterDeactivateAll);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("register for intent action=");
      localStringBuilder.append(sActionDcTesterDeactivateAll);
      log(localStringBuilder.toString());
      paramDcController.addAction(mPhone.getActionDetached());
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("register for intent action=");
      localStringBuilder.append(mPhone.getActionDetached());
      log(localStringBuilder.toString());
      paramPhone.getContext().registerReceiver(sIntentReceiver, paramDcController, null, paramHandler);
    }
  }
  
  private static void log(String paramString)
  {
    Rlog.d("DcTesterDeacativateAll", paramString);
  }
  
  void dispose()
  {
    if (Build.IS_DEBUGGABLE) {
      mPhone.getContext().unregisterReceiver(sIntentReceiver);
    }
  }
}
