package com.android.internal.telephony;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.telephony.Rlog;
import java.util.HashMap;
import java.util.Map;

public class SettingsObserver
  extends ContentObserver
{
  private static final String TAG = "SettingsObserver";
  private final Context mContext;
  private final Handler mHandler;
  private final Map<Uri, Integer> mUriEventMap = new HashMap();
  
  public SettingsObserver(Context paramContext, Handler paramHandler)
  {
    super(null);
    mContext = paramContext;
    mHandler = paramHandler;
  }
  
  public void observe(Uri paramUri, int paramInt)
  {
    mUriEventMap.put(paramUri, Integer.valueOf(paramInt));
    mContext.getContentResolver().registerContentObserver(paramUri, false, this);
  }
  
  public void onChange(boolean paramBoolean)
  {
    Rlog.e("SettingsObserver", "Should never be reached.");
  }
  
  public void onChange(boolean paramBoolean, Uri paramUri)
  {
    Object localObject = (Integer)mUriEventMap.get(paramUri);
    if (localObject != null)
    {
      mHandler.obtainMessage(((Integer)localObject).intValue()).sendToTarget();
    }
    else
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("No matching event to send for URI=");
      ((StringBuilder)localObject).append(paramUri);
      Rlog.e("SettingsObserver", ((StringBuilder)localObject).toString());
    }
  }
  
  public void unobserve()
  {
    mContext.getContentResolver().unregisterContentObserver(this);
  }
}
