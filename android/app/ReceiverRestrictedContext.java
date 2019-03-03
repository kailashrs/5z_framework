package android.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ReceiverCallNotAllowedException;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.UserHandle;

class ReceiverRestrictedContext
  extends ContextWrapper
{
  ReceiverRestrictedContext(Context paramContext)
  {
    super(paramContext);
  }
  
  public boolean bindService(Intent paramIntent, ServiceConnection paramServiceConnection, int paramInt)
  {
    throw new ReceiverCallNotAllowedException("BroadcastReceiver components are not allowed to bind to services");
  }
  
  public Intent registerReceiver(BroadcastReceiver paramBroadcastReceiver, IntentFilter paramIntentFilter)
  {
    return registerReceiver(paramBroadcastReceiver, paramIntentFilter, null, null);
  }
  
  public Intent registerReceiver(BroadcastReceiver paramBroadcastReceiver, IntentFilter paramIntentFilter, String paramString, Handler paramHandler)
  {
    if (paramBroadcastReceiver == null) {
      return super.registerReceiver(null, paramIntentFilter, paramString, paramHandler);
    }
    throw new ReceiverCallNotAllowedException("BroadcastReceiver components are not allowed to register to receive intents");
  }
  
  public Intent registerReceiverAsUser(BroadcastReceiver paramBroadcastReceiver, UserHandle paramUserHandle, IntentFilter paramIntentFilter, String paramString, Handler paramHandler)
  {
    if (paramBroadcastReceiver == null) {
      return super.registerReceiverAsUser(null, paramUserHandle, paramIntentFilter, paramString, paramHandler);
    }
    throw new ReceiverCallNotAllowedException("BroadcastReceiver components are not allowed to register to receive intents");
  }
}
