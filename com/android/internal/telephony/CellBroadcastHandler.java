package com.android.internal.telephony;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Message;
import android.os.UserHandle;
import android.provider.Settings.Secure;
import android.telephony.SmsCbMessage;
import android.telephony.SubscriptionManager;
import com.android.internal.telephony.metrics.TelephonyMetrics;

public class CellBroadcastHandler
  extends WakeLockStateMachine
{
  private CellBroadcastHandler(Context paramContext, Phone paramPhone)
  {
    this("CellBroadcastHandler", paramContext, paramPhone);
  }
  
  protected CellBroadcastHandler(String paramString, Context paramContext, Phone paramPhone)
  {
    super(paramString, paramContext, paramPhone);
  }
  
  public static CellBroadcastHandler makeCellBroadcastHandler(Context paramContext, Phone paramPhone)
  {
    paramContext = new CellBroadcastHandler(paramContext, paramPhone);
    paramContext.start();
    return paramContext;
  }
  
  protected void handleBroadcastSms(SmsCbMessage paramSmsCbMessage)
  {
    TelephonyMetrics.getInstance().writeNewCBSms(mPhone.getPhoneId(), paramSmsCbMessage.getMessageFormat(), paramSmsCbMessage.getMessagePriority(), paramSmsCbMessage.isCmasMessage(), paramSmsCbMessage.isEtwsMessage(), paramSmsCbMessage.getServiceCategory());
    Object localObject;
    Intent localIntent1;
    if (paramSmsCbMessage.isEmergencyMessage())
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Dispatching emergency SMS CB, SmsCbMessage is: ");
      ((StringBuilder)localObject).append(paramSmsCbMessage);
      log(((StringBuilder)localObject).toString());
      localIntent1 = new Intent("android.provider.Telephony.SMS_EMERGENCY_CB_RECEIVED");
      localIntent1.setPackage(mContext.getResources().getString(17039688));
      localObject = "android.permission.RECEIVE_EMERGENCY_BROADCAST";
    }
    for (int i = 17;; i = 16)
    {
      break;
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Dispatching SMS CB, SmsCbMessage is: ");
      ((StringBuilder)localObject).append(paramSmsCbMessage);
      log(((StringBuilder)localObject).toString());
      localIntent1 = new Intent("android.provider.Telephony.SMS_CB_RECEIVED");
      localIntent1.addFlags(16777216);
      localObject = "android.permission.RECEIVE_SMS";
    }
    localIntent1.putExtra("message", paramSmsCbMessage);
    SubscriptionManager.putPhoneIdAndSubIdExtra(localIntent1, mPhone.getPhoneId());
    if (Build.IS_DEBUGGABLE)
    {
      paramSmsCbMessage = Settings.Secure.getString(mContext.getContentResolver(), "cmas_additional_broadcast_pkg");
      if (paramSmsCbMessage != null)
      {
        Intent localIntent2 = new Intent(localIntent1);
        localIntent2.setPackage(paramSmsCbMessage);
        mContext.sendOrderedBroadcastAsUser(localIntent2, UserHandle.ALL, (String)localObject, i, null, getHandler(), -1, null, null);
      }
    }
    mContext.sendOrderedBroadcastAsUser(localIntent1, UserHandle.ALL, (String)localObject, i, mReceiver, getHandler(), -1, null, null);
  }
  
  protected boolean handleSmsMessage(Message paramMessage)
  {
    if ((obj instanceof SmsCbMessage))
    {
      handleBroadcastSms((SmsCbMessage)obj);
      return true;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("handleMessage got object of type: ");
    localStringBuilder.append(obj.getClass().getName());
    loge(localStringBuilder.toString());
    return false;
  }
}
