package android.net;

import android.annotation.SystemApi;
import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceManager.ServiceNotFoundException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

@SystemApi
public class NetworkScoreManager
{
  public static final String ACTION_CHANGE_ACTIVE = "android.net.scoring.CHANGE_ACTIVE";
  public static final String ACTION_CUSTOM_ENABLE = "android.net.scoring.CUSTOM_ENABLE";
  public static final String ACTION_RECOMMEND_NETWORKS = "android.net.action.RECOMMEND_NETWORKS";
  public static final String ACTION_SCORER_CHANGED = "android.net.scoring.SCORER_CHANGED";
  public static final String ACTION_SCORE_NETWORKS = "android.net.scoring.SCORE_NETWORKS";
  public static final int CACHE_FILTER_CURRENT_NETWORK = 1;
  public static final int CACHE_FILTER_NONE = 0;
  public static final int CACHE_FILTER_SCAN_RESULTS = 2;
  public static final String EXTRA_NETWORKS_TO_SCORE = "networksToScore";
  public static final String EXTRA_NEW_SCORER = "newScorer";
  public static final String EXTRA_PACKAGE_NAME = "packageName";
  public static final String NETWORK_AVAILABLE_NOTIFICATION_CHANNEL_ID_META_DATA = "android.net.wifi.notification_channel_id_network_available";
  public static final int RECOMMENDATIONS_ENABLED_FORCED_OFF = -1;
  public static final int RECOMMENDATIONS_ENABLED_OFF = 0;
  public static final int RECOMMENDATIONS_ENABLED_ON = 1;
  public static final String RECOMMENDATION_SERVICE_LABEL_META_DATA = "android.net.scoring.recommendation_service_label";
  public static final String USE_OPEN_WIFI_PACKAGE_META_DATA = "android.net.wifi.use_open_wifi_package";
  private final Context mContext;
  private final INetworkScoreService mService;
  
  public NetworkScoreManager(Context paramContext)
    throws ServiceManager.ServiceNotFoundException
  {
    mContext = paramContext;
    mService = INetworkScoreService.Stub.asInterface(ServiceManager.getServiceOrThrow("network_score"));
  }
  
  public boolean clearScores()
    throws SecurityException
  {
    try
    {
      boolean bool = mService.clearScores();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void disableScoring()
    throws SecurityException
  {
    try
    {
      mService.disableScoring();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public NetworkScorerAppData getActiveScorer()
  {
    try
    {
      NetworkScorerAppData localNetworkScorerAppData = mService.getActiveScorer();
      return localNetworkScorerAppData;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public String getActiveScorerPackage()
  {
    try
    {
      String str = mService.getActiveScorerPackage();
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<NetworkScorerAppData> getAllValidScorers()
  {
    try
    {
      List localList = mService.getAllValidScorers();
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isCallerActiveScorer(int paramInt)
  {
    try
    {
      boolean bool = mService.isCallerActiveScorer(paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void registerNetworkScoreCache(int paramInt, INetworkScoreCache paramINetworkScoreCache)
  {
    registerNetworkScoreCache(paramInt, paramINetworkScoreCache, 0);
  }
  
  public void registerNetworkScoreCache(int paramInt1, INetworkScoreCache paramINetworkScoreCache, int paramInt2)
  {
    try
    {
      mService.registerNetworkScoreCache(paramInt1, paramINetworkScoreCache, paramInt2);
      return;
    }
    catch (RemoteException paramINetworkScoreCache)
    {
      throw paramINetworkScoreCache.rethrowFromSystemServer();
    }
  }
  
  public boolean requestScores(NetworkKey[] paramArrayOfNetworkKey)
    throws SecurityException
  {
    try
    {
      boolean bool = mService.requestScores(paramArrayOfNetworkKey);
      return bool;
    }
    catch (RemoteException paramArrayOfNetworkKey)
    {
      throw paramArrayOfNetworkKey.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public boolean setActiveScorer(String paramString)
    throws SecurityException
  {
    try
    {
      boolean bool = mService.setActiveScorer(paramString);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void unregisterNetworkScoreCache(int paramInt, INetworkScoreCache paramINetworkScoreCache)
  {
    try
    {
      mService.unregisterNetworkScoreCache(paramInt, paramINetworkScoreCache);
      return;
    }
    catch (RemoteException paramINetworkScoreCache)
    {
      throw paramINetworkScoreCache.rethrowFromSystemServer();
    }
  }
  
  public boolean updateScores(ScoredNetwork[] paramArrayOfScoredNetwork)
    throws SecurityException
  {
    try
    {
      boolean bool = mService.updateScores(paramArrayOfScoredNetwork);
      return bool;
    }
    catch (RemoteException paramArrayOfScoredNetwork)
    {
      throw paramArrayOfScoredNetwork.rethrowFromSystemServer();
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface CacheUpdateFilter {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface RecommendationsEnabledSetting {}
}
