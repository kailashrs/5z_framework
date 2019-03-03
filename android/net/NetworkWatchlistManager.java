package android.net;

import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.android.internal.net.INetworkWatchlistManager;
import com.android.internal.net.INetworkWatchlistManager.Stub;
import com.android.internal.util.Preconditions;

public class NetworkWatchlistManager
{
  private static final String SHARED_MEMORY_TAG = "NETWORK_WATCHLIST_SHARED_MEMORY";
  private static final String TAG = "NetworkWatchlistManager";
  private final Context mContext;
  private final INetworkWatchlistManager mNetworkWatchlistManager;
  
  public NetworkWatchlistManager(Context paramContext)
  {
    mContext = ((Context)Preconditions.checkNotNull(paramContext, "missing context"));
    mNetworkWatchlistManager = INetworkWatchlistManager.Stub.asInterface(ServiceManager.getService("network_watchlist"));
  }
  
  public NetworkWatchlistManager(Context paramContext, INetworkWatchlistManager paramINetworkWatchlistManager)
  {
    mContext = paramContext;
    mNetworkWatchlistManager = paramINetworkWatchlistManager;
  }
  
  public byte[] getWatchlistConfigHash()
  {
    try
    {
      byte[] arrayOfByte = mNetworkWatchlistManager.getWatchlistConfigHash();
      return arrayOfByte;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("NetworkWatchlistManager", "Unable to get watchlist config hash");
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void reloadWatchlist()
  {
    try
    {
      mNetworkWatchlistManager.reloadWatchlist();
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("NetworkWatchlistManager", "Unable to reload watchlist");
      localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void reportWatchlistIfNecessary()
  {
    try
    {
      mNetworkWatchlistManager.reportWatchlistIfNecessary();
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("NetworkWatchlistManager", "Cannot report records", localRemoteException);
      localRemoteException.rethrowFromSystemServer();
    }
  }
}
