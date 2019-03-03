package android.net.wifi.aware;

import android.util.Log;
import java.lang.ref.WeakReference;

public class SubscribeDiscoverySession
  extends DiscoverySession
{
  private static final String TAG = "SubscribeDiscSession";
  
  public SubscribeDiscoverySession(WifiAwareManager paramWifiAwareManager, int paramInt1, int paramInt2)
  {
    super(paramWifiAwareManager, paramInt1, paramInt2);
  }
  
  public void updateSubscribe(SubscribeConfig paramSubscribeConfig)
  {
    if (mTerminated)
    {
      Log.w("SubscribeDiscSession", "updateSubscribe: called on terminated session");
      return;
    }
    WifiAwareManager localWifiAwareManager = (WifiAwareManager)mMgr.get();
    if (localWifiAwareManager == null)
    {
      Log.w("SubscribeDiscSession", "updateSubscribe: called post GC on WifiAwareManager");
      return;
    }
    localWifiAwareManager.updateSubscribe(mClientId, mSessionId, paramSubscribeConfig);
  }
}
