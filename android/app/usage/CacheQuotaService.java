package android.app.usage;

import android.annotation.SystemApi;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteCallback;
import android.util.Log;
import android.util.Pair;
import java.util.List;

@SystemApi
public abstract class CacheQuotaService
  extends Service
{
  public static final String REQUEST_LIST_KEY = "requests";
  public static final String SERVICE_INTERFACE = "android.app.usage.CacheQuotaService";
  private static final String TAG = "CacheQuotaService";
  private Handler mHandler;
  private CacheQuotaServiceWrapper mWrapper;
  
  public CacheQuotaService() {}
  
  public IBinder onBind(Intent paramIntent)
  {
    return mWrapper;
  }
  
  public abstract List<CacheQuotaHint> onComputeCacheQuotaHints(List<CacheQuotaHint> paramList);
  
  public void onCreate()
  {
    super.onCreate();
    mWrapper = new CacheQuotaServiceWrapper(null);
    mHandler = new ServiceHandler(getMainLooper());
  }
  
  private final class CacheQuotaServiceWrapper
    extends ICacheQuotaService.Stub
  {
    private CacheQuotaServiceWrapper() {}
    
    public void computeCacheQuotaHints(RemoteCallback paramRemoteCallback, List<CacheQuotaHint> paramList)
    {
      paramRemoteCallback = Pair.create(paramRemoteCallback, paramList);
      paramRemoteCallback = mHandler.obtainMessage(1, paramRemoteCallback);
      mHandler.sendMessage(paramRemoteCallback);
    }
  }
  
  private final class ServiceHandler
    extends Handler
  {
    public static final int MSG_SEND_LIST = 1;
    
    public ServiceHandler(Looper paramLooper)
    {
      super(null, true);
    }
    
    public void handleMessage(Message paramMessage)
    {
      int i = what;
      if (i != 1)
      {
        paramMessage = new StringBuilder();
        paramMessage.append("Handling unknown message: ");
        paramMessage.append(i);
        Log.w("CacheQuotaService", paramMessage.toString());
      }
      else
      {
        paramMessage = (Pair)obj;
        List localList = onComputeCacheQuotaHints((List)second);
        Bundle localBundle = new Bundle();
        localBundle.putParcelableList("requests", localList);
        ((RemoteCallback)first).sendResult(localBundle);
      }
    }
  }
}
