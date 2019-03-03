package android.telephony;

import android.annotation.SystemApi;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.util.Log;

public abstract class VisualVoicemailService
  extends Service
{
  public static final String DATA_PHONE_ACCOUNT_HANDLE = "data_phone_account_handle";
  public static final String DATA_SMS = "data_sms";
  public static final int MSG_ON_CELL_SERVICE_CONNECTED = 1;
  public static final int MSG_ON_SIM_REMOVED = 3;
  public static final int MSG_ON_SMS_RECEIVED = 2;
  public static final int MSG_TASK_ENDED = 4;
  public static final int MSG_TASK_STOPPED = 5;
  public static final String SERVICE_INTERFACE = "android.telephony.VisualVoicemailService";
  private static final String TAG = "VvmService";
  private final Messenger mMessenger = new Messenger(new Handler()
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      PhoneAccountHandle localPhoneAccountHandle = (PhoneAccountHandle)paramAnonymousMessage.getData().getParcelable("data_phone_account_handle");
      VisualVoicemailService.VisualVoicemailTask localVisualVoicemailTask = new VisualVoicemailService.VisualVoicemailTask(replyTo, arg1, null);
      int i = what;
      if (i != 5) {
        switch (i)
        {
        default: 
          super.handleMessage(paramAnonymousMessage);
          break;
        case 3: 
          onSimRemoved(localVisualVoicemailTask, localPhoneAccountHandle);
          break;
        case 2: 
          paramAnonymousMessage = (VisualVoicemailSms)paramAnonymousMessage.getData().getParcelable("data_sms");
          onSmsReceived(localVisualVoicemailTask, paramAnonymousMessage);
          break;
        case 1: 
          onCellServiceConnected(localVisualVoicemailTask, localPhoneAccountHandle);
          break;
        }
      } else {
        onStopped(localVisualVoicemailTask);
      }
    }
  });
  
  public VisualVoicemailService() {}
  
  private static int getSubId(Context paramContext, PhoneAccountHandle paramPhoneAccountHandle)
  {
    TelephonyManager localTelephonyManager = (TelephonyManager)paramContext.getSystemService(TelephonyManager.class);
    paramContext = (TelecomManager)paramContext.getSystemService(TelecomManager.class);
    return localTelephonyManager.getSubIdForPhoneAccount(paramContext.getPhoneAccount(paramPhoneAccountHandle));
  }
  
  @SystemApi
  public static final void sendVisualVoicemailSms(Context paramContext, PhoneAccountHandle paramPhoneAccountHandle, String paramString1, short paramShort, String paramString2, PendingIntent paramPendingIntent)
  {
    ((TelephonyManager)paramContext.getSystemService(TelephonyManager.class)).sendVisualVoicemailSmsForSubscriber(getSubId(paramContext, paramPhoneAccountHandle), paramString1, paramShort, paramString2, paramPendingIntent);
  }
  
  @SystemApi
  public static final void setSmsFilterSettings(Context paramContext, PhoneAccountHandle paramPhoneAccountHandle, VisualVoicemailSmsFilterSettings paramVisualVoicemailSmsFilterSettings)
  {
    TelephonyManager localTelephonyManager = (TelephonyManager)paramContext.getSystemService(TelephonyManager.class);
    int i = getSubId(paramContext, paramPhoneAccountHandle);
    if (paramVisualVoicemailSmsFilterSettings == null) {
      localTelephonyManager.disableVisualVoicemailSmsFilter(i);
    } else {
      localTelephonyManager.enableVisualVoicemailSmsFilter(i, paramVisualVoicemailSmsFilterSettings);
    }
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    return mMessenger.getBinder();
  }
  
  public abstract void onCellServiceConnected(VisualVoicemailTask paramVisualVoicemailTask, PhoneAccountHandle paramPhoneAccountHandle);
  
  public abstract void onSimRemoved(VisualVoicemailTask paramVisualVoicemailTask, PhoneAccountHandle paramPhoneAccountHandle);
  
  public abstract void onSmsReceived(VisualVoicemailTask paramVisualVoicemailTask, VisualVoicemailSms paramVisualVoicemailSms);
  
  public abstract void onStopped(VisualVoicemailTask paramVisualVoicemailTask);
  
  public static class VisualVoicemailTask
  {
    private final Messenger mReplyTo;
    private final int mTaskId;
    
    private VisualVoicemailTask(Messenger paramMessenger, int paramInt)
    {
      mTaskId = paramInt;
      mReplyTo = paramMessenger;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool1 = paramObject instanceof VisualVoicemailTask;
      boolean bool2 = false;
      if (!bool1) {
        return false;
      }
      if (mTaskId == mTaskId) {
        bool2 = true;
      }
      return bool2;
    }
    
    public final void finish()
    {
      Message localMessage = Message.obtain();
      try
      {
        what = 4;
        arg1 = mTaskId;
        mReplyTo.send(localMessage);
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("VvmService", "Cannot send MSG_TASK_ENDED, remote handler no longer exist");
      }
    }
    
    public int hashCode()
    {
      return mTaskId;
    }
  }
}
