package android.service.vr;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

public abstract class VrListenerService
  extends Service
{
  private static final int MSG_ON_CURRENT_VR_ACTIVITY_CHANGED = 1;
  public static final String SERVICE_INTERFACE = "android.service.vr.VrListenerService";
  private final IVrListener.Stub mBinder = new IVrListener.Stub()
  {
    public void focusedActivityChanged(ComponentName paramAnonymousComponentName, boolean paramAnonymousBoolean, int paramAnonymousInt)
    {
      mHandler.obtainMessage(1, paramAnonymousBoolean, paramAnonymousInt, paramAnonymousComponentName).sendToTarget();
    }
  };
  private final Handler mHandler = new VrListenerHandler(Looper.getMainLooper());
  
  public VrListenerService() {}
  
  public static final boolean isVrModePackageEnabled(Context paramContext, ComponentName paramComponentName)
  {
    paramContext = (ActivityManager)paramContext.getSystemService(ActivityManager.class);
    if (paramContext == null) {
      return false;
    }
    return paramContext.isVrModePackageEnabled(paramComponentName);
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    return mBinder;
  }
  
  public void onCurrentVrActivityChanged(ComponentName paramComponentName) {}
  
  public void onCurrentVrActivityChanged(ComponentName paramComponentName, boolean paramBoolean, int paramInt)
  {
    if (paramBoolean) {
      paramComponentName = null;
    }
    onCurrentVrActivityChanged(paramComponentName);
  }
  
  private final class VrListenerHandler
    extends Handler
  {
    public VrListenerHandler(Looper paramLooper)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      int i = what;
      boolean bool = true;
      if (i == 1)
      {
        VrListenerService localVrListenerService = VrListenerService.this;
        ComponentName localComponentName = (ComponentName)obj;
        if (arg1 != 1) {
          bool = false;
        }
        localVrListenerService.onCurrentVrActivityChanged(localComponentName, bool, arg2);
      }
    }
  }
}
