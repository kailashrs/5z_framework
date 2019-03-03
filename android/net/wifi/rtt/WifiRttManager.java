package android.net.wifi.rtt;

import android.annotation.SystemApi;
import android.content.Context;
import android.os.Binder;
import android.os.RemoteException;
import android.os.WorkSource;
import java.util.List;
import java.util.concurrent.Executor;

public class WifiRttManager
{
  public static final String ACTION_WIFI_RTT_STATE_CHANGED = "android.net.wifi.rtt.action.WIFI_RTT_STATE_CHANGED";
  private static final String TAG = "WifiRttManager";
  private static final boolean VDBG = false;
  private final Context mContext;
  private final IWifiRttManager mService;
  
  public WifiRttManager(Context paramContext, IWifiRttManager paramIWifiRttManager)
  {
    mContext = paramContext;
    mService = paramIWifiRttManager;
  }
  
  @SystemApi
  public void cancelRanging(WorkSource paramWorkSource)
  {
    try
    {
      mService.cancelRanging(paramWorkSource);
      return;
    }
    catch (RemoteException paramWorkSource)
    {
      throw paramWorkSource.rethrowFromSystemServer();
    }
  }
  
  public boolean isAvailable()
  {
    try
    {
      boolean bool = mService.isAvailable();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void startRanging(RangingRequest paramRangingRequest, Executor paramExecutor, RangingResultCallback paramRangingResultCallback)
  {
    startRanging(null, paramRangingRequest, paramExecutor, paramRangingResultCallback);
  }
  
  @SystemApi
  public void startRanging(WorkSource paramWorkSource, RangingRequest paramRangingRequest, Executor paramExecutor, RangingResultCallback paramRangingResultCallback)
  {
    if (paramExecutor != null)
    {
      if (paramRangingResultCallback != null)
      {
        Binder localBinder = new Binder();
        try
        {
          IWifiRttManager localIWifiRttManager = mService;
          String str = mContext.getOpPackageName();
          IRttCallback.Stub local1 = new android/net/wifi/rtt/WifiRttManager$1;
          local1.<init>(this, paramExecutor, paramRangingResultCallback);
          localIWifiRttManager.startRanging(localBinder, str, paramWorkSource, paramRangingRequest, local1);
          return;
        }
        catch (RemoteException paramWorkSource)
        {
          throw paramWorkSource.rethrowFromSystemServer();
        }
      }
      throw new IllegalArgumentException("Null callback provided");
    }
    throw new IllegalArgumentException("Null executor provided");
  }
}
