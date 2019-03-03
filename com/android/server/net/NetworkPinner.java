package com.android.server.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.Network;
import android.net.NetworkRequest;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;

public class NetworkPinner
  extends ConnectivityManager.NetworkCallback
{
  private static final String TAG = NetworkPinner.class.getSimpleName();
  @GuardedBy("sLock")
  private static ConnectivityManager sCM;
  @GuardedBy("sLock")
  private static Callback sCallback;
  @VisibleForTesting
  protected static final Object sLock = new Object();
  @GuardedBy("sLock")
  @VisibleForTesting
  protected static Network sNetwork;
  
  public NetworkPinner() {}
  
  private static void maybeInitConnectivityManager(Context paramContext)
  {
    if (sCM == null)
    {
      sCM = (ConnectivityManager)paramContext.getSystemService("connectivity");
      if (sCM == null) {
        throw new IllegalStateException("Bad luck, ConnectivityService not started.");
      }
    }
  }
  
  public static void pin(Context paramContext, NetworkRequest paramNetworkRequest)
  {
    synchronized (sLock)
    {
      if (sCallback == null)
      {
        maybeInitConnectivityManager(paramContext);
        paramContext = new com/android/server/net/NetworkPinner$Callback;
        paramContext.<init>(null);
        sCallback = paramContext;
        try
        {
          sCM.registerNetworkCallback(paramNetworkRequest, sCallback);
        }
        catch (SecurityException paramContext)
        {
          Log.d(TAG, "Failed to register network callback", paramContext);
          sCallback = null;
        }
      }
      return;
    }
  }
  
  public static void unpin()
  {
    synchronized (sLock)
    {
      Callback localCallback = sCallback;
      if (localCallback != null)
      {
        try
        {
          sCM.bindProcessToNetwork(null);
          sCM.unregisterNetworkCallback(sCallback);
        }
        catch (SecurityException localSecurityException)
        {
          Log.d(TAG, "Failed to unregister network callback", localSecurityException);
        }
        sCallback = null;
        sNetwork = null;
      }
      return;
    }
  }
  
  private static class Callback
    extends ConnectivityManager.NetworkCallback
  {
    private Callback() {}
    
    public void onAvailable(Network paramNetwork)
    {
      synchronized (NetworkPinner.sLock)
      {
        if (this != NetworkPinner.sCallback) {
          return;
        }
        if ((NetworkPinner.sCM.getBoundNetworkForProcess() == null) && (NetworkPinner.sNetwork == null))
        {
          NetworkPinner.sCM.bindProcessToNetwork(paramNetwork);
          NetworkPinner.sNetwork = paramNetwork;
          String str = NetworkPinner.TAG;
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Wifi alternate reality enabled on network ");
          localStringBuilder.append(paramNetwork);
          Log.d(str, localStringBuilder.toString());
        }
        NetworkPinner.sLock.notify();
        return;
      }
    }
    
    public void onLost(Network paramNetwork)
    {
      synchronized (NetworkPinner.sLock)
      {
        if (this != NetworkPinner.sCallback) {
          return;
        }
        if ((paramNetwork.equals(NetworkPinner.sNetwork)) && (paramNetwork.equals(NetworkPinner.sCM.getBoundNetworkForProcess())))
        {
          NetworkPinner.unpin();
          String str = NetworkPinner.TAG;
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Wifi alternate reality disabled on network ");
          localStringBuilder.append(paramNetwork);
          Log.d(str, localStringBuilder.toString());
        }
        NetworkPinner.sLock.notify();
        return;
      }
    }
  }
}
