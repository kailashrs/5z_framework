package android.net.metrics;

import android.net.ConnectivityMetricsEvent;
import android.net.IIpConnectivityMetrics;
import android.net.IIpConnectivityMetrics.Stub;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.BitUtils;

public class IpConnectivityLog
{
  private static final boolean DBG = false;
  public static final String SERVICE_NAME = "connmetrics";
  private static final String TAG = IpConnectivityLog.class.getSimpleName();
  private IIpConnectivityMetrics mService;
  
  public IpConnectivityLog() {}
  
  @VisibleForTesting
  public IpConnectivityLog(IIpConnectivityMetrics paramIIpConnectivityMetrics)
  {
    mService = paramIIpConnectivityMetrics;
  }
  
  private boolean checkLoggerService()
  {
    if (mService != null) {
      return true;
    }
    IIpConnectivityMetrics localIIpConnectivityMetrics = IIpConnectivityMetrics.Stub.asInterface(ServiceManager.getService("connmetrics"));
    if (localIIpConnectivityMetrics == null) {
      return false;
    }
    mService = localIIpConnectivityMetrics;
    return true;
  }
  
  private static ConnectivityMetricsEvent makeEv(Parcelable paramParcelable)
  {
    ConnectivityMetricsEvent localConnectivityMetricsEvent = new ConnectivityMetricsEvent();
    data = paramParcelable;
    return localConnectivityMetricsEvent;
  }
  
  public boolean log(int paramInt, int[] paramArrayOfInt, Parcelable paramParcelable)
  {
    paramParcelable = makeEv(paramParcelable);
    netId = paramInt;
    transports = BitUtils.packBits(paramArrayOfInt);
    return log(paramParcelable);
  }
  
  public boolean log(long paramLong, Parcelable paramParcelable)
  {
    paramParcelable = makeEv(paramParcelable);
    timestamp = paramLong;
    return log(paramParcelable);
  }
  
  public boolean log(ConnectivityMetricsEvent paramConnectivityMetricsEvent)
  {
    boolean bool1 = checkLoggerService();
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    if (timestamp == 0L) {
      timestamp = System.currentTimeMillis();
    }
    try
    {
      int i = mService.logEvent(paramConnectivityMetricsEvent);
      if (i >= 0) {
        bool2 = true;
      }
      return bool2;
    }
    catch (RemoteException paramConnectivityMetricsEvent)
    {
      Log.e(TAG, "Error logging event", paramConnectivityMetricsEvent);
    }
    return false;
  }
  
  public boolean log(Parcelable paramParcelable)
  {
    return log(makeEv(paramParcelable));
  }
  
  public boolean log(String paramString, Parcelable paramParcelable)
  {
    paramParcelable = makeEv(paramParcelable);
    ifname = paramString;
    return log(paramParcelable);
  }
}
