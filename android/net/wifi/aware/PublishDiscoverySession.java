package android.net.wifi.aware;

import android.util.Log;
import java.lang.ref.WeakReference;

public class PublishDiscoverySession
  extends DiscoverySession
{
  private static final String TAG = "PublishDiscoverySession";
  
  public PublishDiscoverySession(WifiAwareManager paramWifiAwareManager, int paramInt1, int paramInt2)
  {
    super(paramWifiAwareManager, paramInt1, paramInt2);
  }
  
  public void updatePublish(PublishConfig paramPublishConfig)
  {
    if (mTerminated)
    {
      Log.w("PublishDiscoverySession", "updatePublish: called on terminated session");
      return;
    }
    WifiAwareManager localWifiAwareManager = (WifiAwareManager)mMgr.get();
    if (localWifiAwareManager == null)
    {
      Log.w("PublishDiscoverySession", "updatePublish: called post GC on WifiAwareManager");
      return;
    }
    localWifiAwareManager.updatePublish(mClientId, mSessionId, paramPublishConfig);
  }
}
