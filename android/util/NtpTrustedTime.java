package android.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.SntpClient;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings.Global;
import android.text.TextUtils;

public class NtpTrustedTime
  implements TrustedTime
{
  private static final String BACKUP_SERVER = "persist.backup.ntpServer";
  private static final boolean LOGD = false;
  private static final String TAG = "NtpTrustedTime";
  private static String mBackupServer = "";
  private static int mNtpRetries = 0;
  private static int mNtpRetriesMax = 0;
  private static Context sContext;
  private static NtpTrustedTime sSingleton;
  private boolean mBackupmode = false;
  private ConnectivityManager mCM;
  private long mCachedNtpCertainty;
  private long mCachedNtpElapsedRealtime;
  private long mCachedNtpTime;
  private boolean mHasCache;
  private final String mServer;
  private final long mTimeout;
  
  private NtpTrustedTime(String paramString, long paramLong)
  {
    mServer = paramString;
    mTimeout = paramLong;
  }
  
  private void countInBackupmode()
  {
    if (isBackupSupported())
    {
      mNtpRetries += 1;
      if (mNtpRetries >= mNtpRetriesMax)
      {
        mNtpRetries = 0;
        setBackupmode(true);
      }
    }
  }
  
  private boolean getBackupmode()
  {
    return mBackupmode;
  }
  
  public static NtpTrustedTime getInstance(Context paramContext)
  {
    try
    {
      if (sSingleton == null)
      {
        Resources localResources = paramContext.getResources();
        ContentResolver localContentResolver = paramContext.getContentResolver();
        Object localObject1 = localResources.getString(17039739);
        long l = localResources.getInteger(17694852);
        Object localObject2 = Settings.Global.getString(localContentResolver, "ntp_server");
        l = Settings.Global.getLong(localContentResolver, "ntp_timeout", l);
        if (localObject2 != null) {
          localObject1 = localObject2;
        }
        localObject2 = new android/util/NtpTrustedTime;
        ((NtpTrustedTime)localObject2).<init>((String)localObject1, l);
        sSingleton = (NtpTrustedTime)localObject2;
        sContext = paramContext;
        paramContext = Settings.Global.getString(localContentResolver, "ntp_server_2");
        if ((paramContext == null) || (paramContext.length() <= 0)) {
          paramContext = "persist.backup.ntpServer";
        }
        paramContext = SystemProperties.get(paramContext);
        if ((paramContext != null) && (paramContext.length() > 0))
        {
          int i = localResources.getInteger(17694850);
          if (i > 0)
          {
            localObject1 = sSingleton;
            mNtpRetriesMax = i;
            localObject1 = sSingleton;
            mBackupServer = paramContext.trim().replace("\"", "");
          }
        }
      }
      paramContext = sSingleton;
      return paramContext;
    }
    finally {}
  }
  
  private boolean isBackupSupported()
  {
    boolean bool;
    if ((mNtpRetriesMax > 0) && (mBackupServer != null) && (mBackupServer.length() != 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public long currentTimeMillis()
  {
    if (mHasCache) {
      return mCachedNtpTime + getCacheAge();
    }
    throw new IllegalStateException("Missing authoritative time source");
  }
  
  public boolean forceRefresh()
  {
    boolean bool;
    if (hasCache()) {
      bool = forceSync();
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean forceRefresh(Network paramNetwork)
  {
    if (TextUtils.isEmpty(mServer)) {
      return false;
    }
    try
    {
      if (mCM == null) {
        mCM = ((ConnectivityManager)sContext.getSystemService(ConnectivityManager.class));
      }
      Object localObject;
      if (mCM == null) {
        localObject = null;
      } else {
        localObject = mCM.getNetworkInfo(paramNetwork);
      }
      if ((localObject != null) && (((NetworkInfo)localObject).isConnected()))
      {
        SntpClient localSntpClient = new SntpClient();
        localObject = mServer;
        if (getBackupmode())
        {
          setBackupmode(false);
          localObject = mBackupServer;
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Ntp Server to access at:");
        localStringBuilder.append((String)localObject);
        Log.d("NtpTrustedTime", localStringBuilder.toString());
        if (localSntpClient.requestTime((String)localObject, (int)mTimeout, paramNetwork))
        {
          mHasCache = true;
          mCachedNtpTime = localSntpClient.getNtpTime();
          mCachedNtpElapsedRealtime = localSntpClient.getNtpTimeReference();
          mCachedNtpCertainty = (localSntpClient.getRoundTripTime() / 2L);
          return true;
        }
        countInBackupmode();
        return false;
      }
      return false;
    }
    finally {}
  }
  
  public boolean forceSync()
  {
    try
    {
      if (mCM == null) {
        mCM = ((ConnectivityManager)sContext.getSystemService(ConnectivityManager.class));
      }
      Network localNetwork;
      if (mCM == null) {
        localNetwork = null;
      } else {
        localNetwork = mCM.getActiveNetwork();
      }
      return forceRefresh(localNetwork);
    }
    finally {}
  }
  
  public long getCacheAge()
  {
    if (mHasCache) {
      return SystemClock.elapsedRealtime() - mCachedNtpElapsedRealtime;
    }
    return Long.MAX_VALUE;
  }
  
  public long getCacheCertainty()
  {
    if (mHasCache) {
      return mCachedNtpCertainty;
    }
    return Long.MAX_VALUE;
  }
  
  public long getCachedNtpTime()
  {
    return mCachedNtpTime;
  }
  
  public long getCachedNtpTimeReference()
  {
    return mCachedNtpElapsedRealtime;
  }
  
  public boolean hasCache()
  {
    return mHasCache;
  }
  
  public void setBackupmode(boolean paramBoolean)
  {
    if (isBackupSupported()) {
      mBackupmode = paramBoolean;
    }
  }
}
