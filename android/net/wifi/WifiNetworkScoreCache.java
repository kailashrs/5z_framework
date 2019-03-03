package android.net.wifi;

import android.content.Context;
import android.net.INetworkScoreCache.Stub;
import android.net.NetworkKey;
import android.net.RssiCurve;
import android.net.ScoredNetwork;
import android.net.WifiKey;
import android.os.Handler;
import android.os.Process;
import android.util.Log;
import android.util.LruCache;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WifiNetworkScoreCache
  extends INetworkScoreCache.Stub
{
  private static final boolean DBG = Log.isLoggable("WifiNetworkScoreCache", 3);
  private static final int DEFAULT_MAX_CACHE_SIZE = 100;
  public static final int INVALID_NETWORK_SCORE = -128;
  private static final String TAG = "WifiNetworkScoreCache";
  @GuardedBy("mLock")
  private final LruCache<String, ScoredNetwork> mCache;
  private final Context mContext;
  @GuardedBy("mLock")
  private CacheListener mListener;
  private final Object mLock = new Object();
  
  public WifiNetworkScoreCache(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public WifiNetworkScoreCache(Context paramContext, CacheListener paramCacheListener)
  {
    this(paramContext, paramCacheListener, 100);
  }
  
  public WifiNetworkScoreCache(Context paramContext, CacheListener paramCacheListener, int paramInt)
  {
    mContext = paramContext.getApplicationContext();
    mListener = paramCacheListener;
    mCache = new LruCache(paramInt);
  }
  
  private String buildNetworkKey(NetworkKey paramNetworkKey)
  {
    if (paramNetworkKey == null) {
      return null;
    }
    if (wifiKey == null) {
      return null;
    }
    if (type == 1)
    {
      String str = wifiKey.ssid;
      if (str == null) {
        return null;
      }
      Object localObject = str;
      if (wifiKey.bssid != null)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(str);
        ((StringBuilder)localObject).append(wifiKey.bssid);
        localObject = ((StringBuilder)localObject).toString();
      }
      return localObject;
    }
    return null;
  }
  
  private String buildNetworkKey(ScoredNetwork paramScoredNetwork)
  {
    if (paramScoredNetwork == null) {
      return null;
    }
    return buildNetworkKey(networkKey);
  }
  
  private String buildNetworkKey(ScanResult paramScanResult)
  {
    if ((paramScanResult != null) && (SSID != null))
    {
      StringBuilder localStringBuilder = new StringBuilder("\"");
      localStringBuilder.append(SSID);
      localStringBuilder.append("\"");
      if (BSSID != null) {
        localStringBuilder.append(BSSID);
      }
      return localStringBuilder.toString();
    }
    return null;
  }
  
  public final void clearScores()
  {
    synchronized (mLock)
    {
      mCache.evictAll();
      return;
    }
  }
  
  protected final void dump(FileDescriptor arg1, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    mContext.enforceCallingOrSelfPermission("android.permission.DUMP", "WifiNetworkScoreCache");
    paramPrintWriter.println(String.format("WifiNetworkScoreCache (%s/%d)", new Object[] { mContext.getPackageName(), Integer.valueOf(Process.myUid()) }));
    paramPrintWriter.println("  All score curves:");
    synchronized (mLock)
    {
      Object localObject1 = mCache.snapshot().values().iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (ScoredNetwork)((Iterator)localObject1).next();
        paramArrayOfString = new java/lang/StringBuilder;
        paramArrayOfString.<init>();
        paramArrayOfString.append("    ");
        paramArrayOfString.append(localObject2);
        paramPrintWriter.println(paramArrayOfString.toString());
      }
      paramPrintWriter.println("  Network scores for latest ScanResults:");
      Object localObject2 = ((WifiManager)mContext.getSystemService("wifi")).getScanResults().iterator();
      while (((Iterator)localObject2).hasNext())
      {
        localObject1 = (ScanResult)((Iterator)localObject2).next();
        paramArrayOfString = new java/lang/StringBuilder;
        paramArrayOfString.<init>();
        paramArrayOfString.append("    ");
        paramArrayOfString.append(buildNetworkKey((ScanResult)localObject1));
        paramArrayOfString.append(": ");
        paramArrayOfString.append(getNetworkScore((ScanResult)localObject1));
        paramPrintWriter.println(paramArrayOfString.toString());
      }
      return;
    }
  }
  
  public boolean getMeteredHint(ScanResult paramScanResult)
  {
    paramScanResult = getScoredNetwork(paramScanResult);
    boolean bool;
    if ((paramScanResult != null) && (meteredHint)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public int getNetworkScore(ScanResult paramScanResult)
  {
    int i = -128;
    ScoredNetwork localScoredNetwork = getScoredNetwork(paramScanResult);
    int j = i;
    if (localScoredNetwork != null)
    {
      j = i;
      if (rssiCurve != null)
      {
        i = rssiCurve.lookupScore(level);
        j = i;
        if (DBG)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("getNetworkScore found scored network ");
          localStringBuilder.append(networkKey);
          localStringBuilder.append(" score ");
          localStringBuilder.append(Integer.toString(i));
          localStringBuilder.append(" RSSI ");
          localStringBuilder.append(level);
          Log.d("WifiNetworkScoreCache", localStringBuilder.toString());
          j = i;
        }
      }
    }
    return j;
  }
  
  public int getNetworkScore(ScanResult paramScanResult, boolean paramBoolean)
  {
    int i = -128;
    ScoredNetwork localScoredNetwork = getScoredNetwork(paramScanResult);
    int j = i;
    if (localScoredNetwork != null)
    {
      j = i;
      if (rssiCurve != null)
      {
        i = rssiCurve.lookupScore(level, paramBoolean);
        j = i;
        if (DBG)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("getNetworkScore found scored network ");
          localStringBuilder.append(networkKey);
          localStringBuilder.append(" score ");
          localStringBuilder.append(Integer.toString(i));
          localStringBuilder.append(" RSSI ");
          localStringBuilder.append(level);
          localStringBuilder.append(" isActiveNetwork ");
          localStringBuilder.append(paramBoolean);
          Log.d("WifiNetworkScoreCache", localStringBuilder.toString());
          j = i;
        }
      }
    }
    return j;
  }
  
  public ScoredNetwork getScoredNetwork(NetworkKey arg1)
  {
    Object localObject1 = buildNetworkKey(???);
    if (localObject1 == null)
    {
      if (DBG)
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("Could not build key string for Network Key: ");
        ((StringBuilder)localObject1).append(???);
        Log.d("WifiNetworkScoreCache", ((StringBuilder)localObject1).toString());
      }
      return null;
    }
    synchronized (mLock)
    {
      localObject1 = (ScoredNetwork)mCache.get(localObject1);
      return localObject1;
    }
  }
  
  public ScoredNetwork getScoredNetwork(ScanResult arg1)
  {
    Object localObject1 = buildNetworkKey(???);
    if (localObject1 == null) {
      return null;
    }
    synchronized (mLock)
    {
      localObject1 = (ScoredNetwork)mCache.get(localObject1);
      return localObject1;
    }
  }
  
  public boolean hasScoreCurve(ScanResult paramScanResult)
  {
    paramScanResult = getScoredNetwork(paramScanResult);
    boolean bool;
    if ((paramScanResult != null) && (rssiCurve != null)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isScoredNetwork(ScanResult paramScanResult)
  {
    boolean bool;
    if (getScoredNetwork(paramScanResult) != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void registerListener(CacheListener paramCacheListener)
  {
    synchronized (mLock)
    {
      mListener = paramCacheListener;
      return;
    }
  }
  
  public void unregisterListener()
  {
    synchronized (mLock)
    {
      mListener = null;
      return;
    }
  }
  
  public final void updateScores(List<ScoredNetwork> paramList)
  {
    if ((paramList != null) && (!paramList.isEmpty()))
    {
      if (DBG)
      {
        ??? = new StringBuilder();
        ((StringBuilder)???).append("updateScores list size=");
        ((StringBuilder)???).append(paramList.size());
        Log.d("WifiNetworkScoreCache", ((StringBuilder)???).toString());
      }
      int i = 0;
      synchronized (mLock)
      {
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext())
        {
          ScoredNetwork localScoredNetwork = (ScoredNetwork)localIterator.next();
          Object localObject2 = buildNetworkKey(localScoredNetwork);
          if (localObject2 == null)
          {
            if (DBG)
            {
              localObject2 = new java/lang/StringBuilder;
              ((StringBuilder)localObject2).<init>();
              ((StringBuilder)localObject2).append("Failed to build network key for ScoredNetwork");
              ((StringBuilder)localObject2).append(localScoredNetwork);
              Log.d("WifiNetworkScoreCache", ((StringBuilder)localObject2).toString());
            }
          }
          else
          {
            mCache.put(localObject2, localScoredNetwork);
            i = 1;
          }
        }
        if ((mListener != null) && (i != 0)) {
          mListener.post(paramList);
        }
        return;
      }
    }
  }
  
  public static abstract class CacheListener
  {
    private Handler mHandler;
    
    public CacheListener(Handler paramHandler)
    {
      Preconditions.checkNotNull(paramHandler);
      mHandler = paramHandler;
    }
    
    public abstract void networkCacheUpdated(List<ScoredNetwork> paramList);
    
    void post(final List<ScoredNetwork> paramList)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          networkCacheUpdated(paramList);
        }
      });
    }
  }
}
