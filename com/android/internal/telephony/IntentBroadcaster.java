package com.android.internal.telephony;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class IntentBroadcaster
{
  private static final String TAG = "IntentBroadcaster";
  private static IntentBroadcaster sIntentBroadcaster;
  private Map<Integer, Intent> mRebroadcastIntents = new HashMap();
  private final BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context arg1, Intent paramAnonymousIntent)
    {
      if (paramAnonymousIntent.getAction().equals("android.intent.action.USER_UNLOCKED")) {
        synchronized (mRebroadcastIntents)
        {
          Iterator localIterator = mRebroadcastIntents.entrySet().iterator();
          while (localIterator.hasNext())
          {
            paramAnonymousIntent = (Map.Entry)localIterator.next();
            Intent localIntent = (Intent)paramAnonymousIntent.getValue();
            localIntent.putExtra("rebroadcastOnUnlock", true);
            localIterator.remove();
            IntentBroadcaster localIntentBroadcaster = IntentBroadcaster.this;
            StringBuilder localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append("Rebroadcasting intent ");
            localStringBuilder.append(localIntent.getAction());
            localStringBuilder.append(" ");
            localStringBuilder.append(localIntent.getStringExtra("ss"));
            localStringBuilder.append(" for slotId ");
            localStringBuilder.append(paramAnonymousIntent.getKey());
            localIntentBroadcaster.logd(localStringBuilder.toString());
            ActivityManager.broadcastStickyIntent(localIntent, -1);
          }
        }
      }
    }
  };
  
  private IntentBroadcaster(Context paramContext)
  {
    paramContext.registerReceiver(mReceiver, new IntentFilter("android.intent.action.USER_UNLOCKED"));
  }
  
  public static IntentBroadcaster getInstance()
  {
    return sIntentBroadcaster;
  }
  
  public static IntentBroadcaster getInstance(Context paramContext)
  {
    if (sIntentBroadcaster == null) {
      sIntentBroadcaster = new IntentBroadcaster(paramContext);
    }
    return sIntentBroadcaster;
  }
  
  private void logd(String paramString)
  {
    Log.d("IntentBroadcaster", paramString);
  }
  
  public void broadcastStickyIntent(Intent paramIntent, int paramInt)
  {
    ??? = new StringBuilder();
    ((StringBuilder)???).append("Broadcasting and adding intent for rebroadcast: ");
    ((StringBuilder)???).append(paramIntent.getAction());
    ((StringBuilder)???).append(" ");
    ((StringBuilder)???).append(paramIntent.getStringExtra("ss"));
    ((StringBuilder)???).append(" for slotId ");
    ((StringBuilder)???).append(paramInt);
    logd(((StringBuilder)???).toString());
    synchronized (mRebroadcastIntents)
    {
      ActivityManager.broadcastStickyIntent(paramIntent, -1);
      mRebroadcastIntents.put(Integer.valueOf(paramInt), paramIntent);
      return;
    }
  }
}
