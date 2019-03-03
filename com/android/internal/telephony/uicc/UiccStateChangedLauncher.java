package com.android.internal.telephony.uicc;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;

public class UiccStateChangedLauncher
  extends Handler
{
  private static final int EVENT_ICC_CHANGED = 1;
  private static final String TAG = UiccStateChangedLauncher.class.getName();
  private static String sDeviceProvisioningPackage = null;
  private Context mContext;
  private boolean[] mIsRestricted = null;
  private UiccController mUiccController;
  
  public UiccStateChangedLauncher(Context paramContext, UiccController paramUiccController)
  {
    sDeviceProvisioningPackage = paramContext.getResources().getString(17039697);
    if ((sDeviceProvisioningPackage != null) && (!sDeviceProvisioningPackage.isEmpty()))
    {
      mContext = paramContext;
      mUiccController = paramUiccController;
      mUiccController.registerForIccChanged(this, 1, null);
    }
  }
  
  private void notifyStateChanged()
  {
    Intent localIntent = new Intent("android.intent.action.SIM_STATE_CHANGED");
    localIntent.setPackage(sDeviceProvisioningPackage);
    try
    {
      mContext.sendBroadcast(localIntent);
    }
    catch (Exception localException)
    {
      Log.e(TAG, localException.toString());
    }
  }
  
  public void handleMessage(Message paramMessage)
  {
    if (what == 1)
    {
      int i = 0;
      if (mIsRestricted == null)
      {
        mIsRestricted = new boolean[TelephonyManager.getDefault().getPhoneCount()];
        i = 1;
      }
      int j = 0;
      int k = i;
      for (i = j; i < mIsRestricted.length; i++)
      {
        paramMessage = mUiccController.getUiccCardForPhone(i);
        int m;
        if ((paramMessage != null) && (paramMessage.getCardState() == IccCardStatus.CardState.CARDSTATE_RESTRICTED)) {
          m = 0;
        } else {
          m = 1;
        }
        if (m != mIsRestricted[i])
        {
          mIsRestricted[i] ^= 0x1;
          k = 1;
        }
      }
      if (k != 0) {
        notifyStateChanged();
      }
      return;
    }
    throw new RuntimeException("unexpected event not handled");
  }
}
