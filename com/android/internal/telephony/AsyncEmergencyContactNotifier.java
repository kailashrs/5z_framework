package com.android.internal.telephony;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.BlockedNumberContract.SystemContract;
import android.telephony.Rlog;

public class AsyncEmergencyContactNotifier
  extends AsyncTask<Void, Void, Void>
{
  private static final String TAG = "AsyncEmergencyContactNotifier";
  private final Context mContext;
  
  public AsyncEmergencyContactNotifier(Context paramContext)
  {
    mContext = paramContext;
  }
  
  protected Void doInBackground(Void... paramVarArgs)
  {
    try
    {
      BlockedNumberContract.SystemContract.notifyEmergencyContact(mContext);
    }
    catch (Exception localException)
    {
      paramVarArgs = new StringBuilder();
      paramVarArgs.append("Exception notifying emergency contact: ");
      paramVarArgs.append(localException);
      Rlog.e("AsyncEmergencyContactNotifier", paramVarArgs.toString());
    }
    return null;
  }
}
