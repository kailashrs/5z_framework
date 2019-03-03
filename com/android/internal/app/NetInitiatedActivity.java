package com.android.internal.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class NetInitiatedActivity
  extends AlertActivity
  implements DialogInterface.OnClickListener
{
  private static final boolean DEBUG = true;
  private static final int GPS_NO_RESPONSE_TIME_OUT = 1;
  private static final int NEGATIVE_BUTTON = -2;
  private static final int POSITIVE_BUTTON = -1;
  private static final String TAG = "NetInitiatedActivity";
  private static final boolean VERBOSE = false;
  private int default_response = -1;
  private int default_response_timeout = 6;
  private final Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      if (what == 1)
      {
        if (notificationId != -1) {
          NetInitiatedActivity.this.sendUserResponse(default_response);
        }
        finish();
      }
    }
  };
  private BroadcastReceiver mNetInitiatedReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      paramAnonymousContext = new StringBuilder();
      paramAnonymousContext.append("NetInitiatedReceiver onReceive: ");
      paramAnonymousContext.append(paramAnonymousIntent.getAction());
      Log.d("NetInitiatedActivity", paramAnonymousContext.toString());
      if (paramAnonymousIntent.getAction() == "android.intent.action.NETWORK_INITIATED_VERIFY") {
        NetInitiatedActivity.this.handleNIVerify(paramAnonymousIntent);
      }
    }
  };
  private int notificationId = -1;
  private int timeout = -1;
  
  public NetInitiatedActivity() {}
  
  private void handleNIVerify(Intent paramIntent)
  {
    notificationId = paramIntent.getIntExtra("notif_id", -1);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("handleNIVerify action: ");
    localStringBuilder.append(paramIntent.getAction());
    Log.d("NetInitiatedActivity", localStringBuilder.toString());
  }
  
  private void sendUserResponse(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("sendUserResponse, response: ");
    localStringBuilder.append(paramInt);
    Log.d("NetInitiatedActivity", localStringBuilder.toString());
    ((LocationManager)getSystemService("location")).sendNiResponse(notificationId, paramInt);
  }
  
  private void showNIError()
  {
    Toast.makeText(this, "NI error", 1).show();
  }
  
  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    if (paramInt == -1) {
      sendUserResponse(1);
    }
    if (paramInt == -2) {
      sendUserResponse(2);
    }
    finish();
    notificationId = -1;
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Intent localIntent = getIntent();
    AlertController.AlertParams localAlertParams = mAlertParams;
    paramBundle = getApplicationContext();
    mTitle = localIntent.getStringExtra("title");
    mMessage = localIntent.getStringExtra("message");
    mPositiveButtonText = String.format(paramBundle.getString(17040070), new Object[0]);
    mPositiveButtonListener = this;
    mNegativeButtonText = String.format(paramBundle.getString(17040069), new Object[0]);
    mNegativeButtonListener = this;
    notificationId = localIntent.getIntExtra("notif_id", -1);
    timeout = localIntent.getIntExtra("timeout", default_response_timeout);
    default_response = localIntent.getIntExtra("default_resp", 1);
    paramBundle = new StringBuilder();
    paramBundle.append("onCreate() : notificationId: ");
    paramBundle.append(notificationId);
    paramBundle.append(" timeout: ");
    paramBundle.append(timeout);
    paramBundle.append(" default_response:");
    paramBundle.append(default_response);
    Log.d("NetInitiatedActivity", paramBundle.toString());
    mHandler.sendMessageDelayed(mHandler.obtainMessage(1), timeout * 1000);
    setupAlert();
  }
  
  protected void onPause()
  {
    super.onPause();
    Log.d("NetInitiatedActivity", "onPause");
    unregisterReceiver(mNetInitiatedReceiver);
  }
  
  protected void onResume()
  {
    super.onResume();
    Log.d("NetInitiatedActivity", "onResume");
    registerReceiver(mNetInitiatedReceiver, new IntentFilter("android.intent.action.NETWORK_INITIATED_VERIFY"));
  }
}
