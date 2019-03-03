package android.net;

import android.annotation.SystemApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.util.Preconditions;
import java.util.concurrent.Executor;

@SystemApi
public abstract class NetworkRecommendationProvider
{
  private static final String TAG = "NetworkRecProvider";
  private static final boolean VERBOSE;
  private final IBinder mService;
  
  static
  {
    boolean bool;
    if ((Build.IS_DEBUGGABLE) && (Log.isLoggable("NetworkRecProvider", 2))) {
      bool = true;
    } else {
      bool = false;
    }
    VERBOSE = bool;
  }
  
  public NetworkRecommendationProvider(Context paramContext, Executor paramExecutor)
  {
    Preconditions.checkNotNull(paramContext);
    Preconditions.checkNotNull(paramExecutor);
    mService = new ServiceWrapper(paramContext, paramExecutor);
  }
  
  public final IBinder getBinder()
  {
    return mService;
  }
  
  public abstract void onRequestScores(NetworkKey[] paramArrayOfNetworkKey);
  
  private final class ServiceWrapper
    extends INetworkRecommendationProvider.Stub
  {
    private final Context mContext;
    private final Executor mExecutor;
    private final Handler mHandler;
    
    ServiceWrapper(Context paramContext, Executor paramExecutor)
    {
      mContext = paramContext;
      mExecutor = paramExecutor;
      mHandler = null;
    }
    
    private void enforceCallingPermission()
    {
      if (mContext != null) {
        mContext.enforceCallingOrSelfPermission("android.permission.REQUEST_NETWORK_SCORES", "Permission denied.");
      }
    }
    
    private void execute(Runnable paramRunnable)
    {
      if (mExecutor != null) {
        mExecutor.execute(paramRunnable);
      } else {
        mHandler.post(paramRunnable);
      }
    }
    
    public void requestScores(final NetworkKey[] paramArrayOfNetworkKey)
      throws RemoteException
    {
      enforceCallingPermission();
      if ((paramArrayOfNetworkKey != null) && (paramArrayOfNetworkKey.length > 0)) {
        execute(new Runnable()
        {
          public void run()
          {
            onRequestScores(paramArrayOfNetworkKey);
          }
        });
      }
    }
  }
}
