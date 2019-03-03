package android.net.wifi.aware;

import android.annotation.SystemApi;
import android.net.NetworkSpecifier;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import dalvik.system.CloseGuard;
import java.lang.ref.WeakReference;

public class DiscoverySession
  implements AutoCloseable
{
  private static final boolean DBG = false;
  private static final int MAX_SEND_RETRY_COUNT = 5;
  private static final String TAG = "DiscoverySession";
  private static final boolean VDBG = false;
  protected final int mClientId;
  private final CloseGuard mCloseGuard = CloseGuard.get();
  protected WeakReference<WifiAwareManager> mMgr;
  protected final int mSessionId;
  protected boolean mTerminated = false;
  
  public DiscoverySession(WifiAwareManager paramWifiAwareManager, int paramInt1, int paramInt2)
  {
    mMgr = new WeakReference(paramWifiAwareManager);
    mClientId = paramInt1;
    mSessionId = paramInt2;
    mCloseGuard.open("close");
  }
  
  public static int getMaxSendRetryCount()
  {
    return 5;
  }
  
  public void close()
  {
    WifiAwareManager localWifiAwareManager = (WifiAwareManager)mMgr.get();
    if (localWifiAwareManager == null)
    {
      Log.w("DiscoverySession", "destroy: called post GC on WifiAwareManager");
      return;
    }
    localWifiAwareManager.terminateSession(mClientId, mSessionId);
    mTerminated = true;
    mMgr.clear();
    mCloseGuard.close();
  }
  
  public NetworkSpecifier createNetworkSpecifierOpen(PeerHandle paramPeerHandle)
  {
    if (mTerminated)
    {
      Log.w("DiscoverySession", "createNetworkSpecifierOpen: called on terminated session");
      return null;
    }
    WifiAwareManager localWifiAwareManager = (WifiAwareManager)mMgr.get();
    if (localWifiAwareManager == null)
    {
      Log.w("DiscoverySession", "createNetworkSpecifierOpen: called post GC on WifiAwareManager");
      return null;
    }
    if ((this instanceof SubscribeDiscoverySession)) {}
    for (int i = 0;; i = 1) {
      break;
    }
    return localWifiAwareManager.createNetworkSpecifier(mClientId, i, mSessionId, paramPeerHandle, null, null);
  }
  
  public NetworkSpecifier createNetworkSpecifierPassphrase(PeerHandle paramPeerHandle, String paramString)
  {
    if (WifiAwareUtils.validatePassphrase(paramString))
    {
      if (mTerminated)
      {
        Log.w("DiscoverySession", "createNetworkSpecifierPassphrase: called on terminated session");
        return null;
      }
      WifiAwareManager localWifiAwareManager = (WifiAwareManager)mMgr.get();
      if (localWifiAwareManager == null)
      {
        Log.w("DiscoverySession", "createNetworkSpecifierPassphrase: called post GC on WifiAwareManager");
        return null;
      }
      if ((this instanceof SubscribeDiscoverySession)) {}
      for (int i = 0;; i = 1) {
        break;
      }
      return localWifiAwareManager.createNetworkSpecifier(mClientId, i, mSessionId, paramPeerHandle, null, paramString);
    }
    throw new IllegalArgumentException("Passphrase must meet length requirements");
  }
  
  @SystemApi
  public NetworkSpecifier createNetworkSpecifierPmk(PeerHandle paramPeerHandle, byte[] paramArrayOfByte)
  {
    if (WifiAwareUtils.validatePmk(paramArrayOfByte))
    {
      if (mTerminated)
      {
        Log.w("DiscoverySession", "createNetworkSpecifierPmk: called on terminated session");
        return null;
      }
      WifiAwareManager localWifiAwareManager = (WifiAwareManager)mMgr.get();
      if (localWifiAwareManager == null)
      {
        Log.w("DiscoverySession", "createNetworkSpecifierPmk: called post GC on WifiAwareManager");
        return null;
      }
      if ((this instanceof SubscribeDiscoverySession)) {}
      for (int i = 0;; i = 1) {
        break;
      }
      return localWifiAwareManager.createNetworkSpecifier(mClientId, i, mSessionId, paramPeerHandle, paramArrayOfByte, null);
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
  
  @VisibleForTesting
  public int getSessionId()
  {
    return mSessionId;
  }
  
  public void sendMessage(PeerHandle paramPeerHandle, int paramInt, byte[] paramArrayOfByte)
  {
    sendMessage(paramPeerHandle, paramInt, paramArrayOfByte, 0);
  }
  
  public void sendMessage(PeerHandle paramPeerHandle, int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    if (mTerminated)
    {
      Log.w("DiscoverySession", "sendMessage: called on terminated session");
      return;
    }
    WifiAwareManager localWifiAwareManager = (WifiAwareManager)mMgr.get();
    if (localWifiAwareManager == null)
    {
      Log.w("DiscoverySession", "sendMessage: called post GC on WifiAwareManager");
      return;
    }
    localWifiAwareManager.sendMessage(mClientId, mSessionId, paramPeerHandle, paramArrayOfByte, paramInt1, paramInt2);
  }
  
  public void setTerminated()
  {
    if (mTerminated)
    {
      Log.w("DiscoverySession", "terminate: already terminated.");
      return;
    }
    mTerminated = true;
    mMgr.clear();
    mCloseGuard.close();
  }
}
