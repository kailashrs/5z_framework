package android.net.wifi.aware;

import android.annotation.SystemApi;
import android.net.NetworkSpecifier;
import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import dalvik.system.CloseGuard;
import java.lang.ref.WeakReference;

public class WifiAwareSession
  implements AutoCloseable
{
  private static final boolean DBG = false;
  private static final String TAG = "WifiAwareSession";
  private static final boolean VDBG = false;
  private final Binder mBinder;
  private final int mClientId;
  private final CloseGuard mCloseGuard = CloseGuard.get();
  private final WeakReference<WifiAwareManager> mMgr;
  private boolean mTerminated = true;
  
  public WifiAwareSession(WifiAwareManager paramWifiAwareManager, Binder paramBinder, int paramInt)
  {
    mMgr = new WeakReference(paramWifiAwareManager);
    mBinder = paramBinder;
    mClientId = paramInt;
    mTerminated = false;
    mCloseGuard.open("close");
  }
  
  public void close()
  {
    WifiAwareManager localWifiAwareManager = (WifiAwareManager)mMgr.get();
    if (localWifiAwareManager == null)
    {
      Log.w("WifiAwareSession", "destroy: called post GC on WifiAwareManager");
      return;
    }
    localWifiAwareManager.disconnect(mClientId, mBinder);
    mTerminated = true;
    mMgr.clear();
    mCloseGuard.close();
  }
  
  public NetworkSpecifier createNetworkSpecifierOpen(int paramInt, byte[] paramArrayOfByte)
  {
    WifiAwareManager localWifiAwareManager = (WifiAwareManager)mMgr.get();
    if (localWifiAwareManager == null)
    {
      Log.e("WifiAwareSession", "createNetworkSpecifierOpen: called post GC on WifiAwareManager");
      return null;
    }
    if (mTerminated)
    {
      Log.e("WifiAwareSession", "createNetworkSpecifierOpen: called after termination");
      return null;
    }
    return localWifiAwareManager.createNetworkSpecifier(mClientId, paramInt, paramArrayOfByte, null, null);
  }
  
  public NetworkSpecifier createNetworkSpecifierPassphrase(int paramInt, byte[] paramArrayOfByte, String paramString)
  {
    WifiAwareManager localWifiAwareManager = (WifiAwareManager)mMgr.get();
    if (localWifiAwareManager == null)
    {
      Log.e("WifiAwareSession", "createNetworkSpecifierPassphrase: called post GC on WifiAwareManager");
      return null;
    }
    if (mTerminated)
    {
      Log.e("WifiAwareSession", "createNetworkSpecifierPassphrase: called after termination");
      return null;
    }
    if (WifiAwareUtils.validatePassphrase(paramString)) {
      return localWifiAwareManager.createNetworkSpecifier(mClientId, paramInt, paramArrayOfByte, null, paramString);
    }
    throw new IllegalArgumentException("Passphrase must meet length requirements");
  }
  
  @SystemApi
  public NetworkSpecifier createNetworkSpecifierPmk(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    WifiAwareManager localWifiAwareManager = (WifiAwareManager)mMgr.get();
    if (localWifiAwareManager == null)
    {
      Log.e("WifiAwareSession", "createNetworkSpecifierPmk: called post GC on WifiAwareManager");
      return null;
    }
    if (mTerminated)
    {
      Log.e("WifiAwareSession", "createNetworkSpecifierPmk: called after termination");
      return null;
    }
    if (WifiAwareUtils.validatePmk(paramArrayOfByte2)) {
      return localWifiAwareManager.createNetworkSpecifier(mClientId, paramInt, paramArrayOfByte1, paramArrayOfByte2, null);
    }
    throw new IllegalArgumentException("PMK must 32 bytes");
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (mCloseGuard != null) {
        mCloseGuard.warnIfOpen();
      }
      if (!mTerminated) {
        close();
      }
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  @VisibleForTesting
  public int getClientId()
  {
    return mClientId;
  }
  
  public void publish(PublishConfig paramPublishConfig, DiscoverySessionCallback paramDiscoverySessionCallback, Handler paramHandler)
  {
    WifiAwareManager localWifiAwareManager = (WifiAwareManager)mMgr.get();
    if (localWifiAwareManager == null)
    {
      Log.e("WifiAwareSession", "publish: called post GC on WifiAwareManager");
      return;
    }
    if (mTerminated)
    {
      Log.e("WifiAwareSession", "publish: called after termination");
      return;
    }
    int i = mClientId;
    if (paramHandler == null) {
      paramHandler = Looper.getMainLooper();
    } else {
      paramHandler = paramHandler.getLooper();
    }
    localWifiAwareManager.publish(i, paramHandler, paramPublishConfig, paramDiscoverySessionCallback);
  }
  
  public void subscribe(SubscribeConfig paramSubscribeConfig, DiscoverySessionCallback paramDiscoverySessionCallback, Handler paramHandler)
  {
    WifiAwareManager localWifiAwareManager = (WifiAwareManager)mMgr.get();
    if (localWifiAwareManager == null)
    {
      Log.e("WifiAwareSession", "publish: called post GC on WifiAwareManager");
      return;
    }
    if (mTerminated)
    {
      Log.e("WifiAwareSession", "publish: called after termination");
      return;
    }
    int i = mClientId;
    if (paramHandler == null) {
      paramHandler = Looper.getMainLooper();
    } else {
      paramHandler = paramHandler.getLooper();
    }
    localWifiAwareManager.subscribe(i, paramHandler, paramSubscribeConfig, paramDiscoverySessionCallback);
  }
}
