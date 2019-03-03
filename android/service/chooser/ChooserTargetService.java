package android.service.chooser;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import java.util.List;

public abstract class ChooserTargetService
  extends Service
{
  public static final String BIND_PERMISSION = "android.permission.BIND_CHOOSER_TARGET_SERVICE";
  private static final boolean DEBUG = false;
  public static final String META_DATA_NAME = "android.service.chooser.chooser_target_service";
  public static final String SERVICE_INTERFACE = "android.service.chooser.ChooserTargetService";
  private final String TAG;
  private IChooserTargetServiceWrapper mWrapper;
  
  public ChooserTargetService()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(ChooserTargetService.class.getSimpleName());
    localStringBuilder.append('[');
    localStringBuilder.append(getClass().getSimpleName());
    localStringBuilder.append(']');
    TAG = localStringBuilder.toString();
    mWrapper = null;
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    if (!"android.service.chooser.ChooserTargetService".equals(paramIntent.getAction())) {
      return null;
    }
    if (mWrapper == null) {
      mWrapper = new IChooserTargetServiceWrapper(null);
    }
    return mWrapper;
  }
  
  public abstract List<ChooserTarget> onGetChooserTargets(ComponentName paramComponentName, IntentFilter paramIntentFilter);
  
  private class IChooserTargetServiceWrapper
    extends IChooserTargetService.Stub
  {
    private IChooserTargetServiceWrapper() {}
    
    public void getChooserTargets(ComponentName paramComponentName, IntentFilter paramIntentFilter, IChooserTargetResult paramIChooserTargetResult)
      throws RemoteException
    {
      long l = clearCallingIdentity();
      try
      {
        paramComponentName = onGetChooserTargets(paramComponentName, paramIntentFilter);
        restoreCallingIdentity(l);
        paramIChooserTargetResult.sendResult(paramComponentName);
        return;
      }
      finally
      {
        restoreCallingIdentity(l);
        paramIChooserTargetResult.sendResult(null);
      }
    }
  }
}
