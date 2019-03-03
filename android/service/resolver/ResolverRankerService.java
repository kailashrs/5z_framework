package android.service.resolver;

import android.annotation.SystemApi;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.List;

@SystemApi
public abstract class ResolverRankerService
  extends Service
{
  public static final String BIND_PERMISSION = "android.permission.BIND_RESOLVER_RANKER_SERVICE";
  private static final boolean DEBUG = false;
  private static final String HANDLER_THREAD_NAME = "RESOLVER_RANKER_SERVICE";
  public static final String HOLD_PERMISSION = "android.permission.PROVIDE_RESOLVER_RANKER_SERVICE";
  public static final String SERVICE_INTERFACE = "android.service.resolver.ResolverRankerService";
  private static final String TAG = "ResolverRankerService";
  private volatile Handler mHandler;
  private HandlerThread mHandlerThread;
  private ResolverRankerServiceWrapper mWrapper = null;
  
  public ResolverRankerService() {}
  
  private static void sendResult(List<ResolverTarget> paramList, IResolverRankerResult paramIResolverRankerResult)
  {
    try
    {
      paramIResolverRankerResult.sendResult(paramList);
    }
    catch (Exception paramList)
    {
      paramIResolverRankerResult = new StringBuilder();
      paramIResolverRankerResult.append("failed to send results: ");
      paramIResolverRankerResult.append(paramList);
      Log.e("ResolverRankerService", paramIResolverRankerResult.toString());
    }
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    if (!"android.service.resolver.ResolverRankerService".equals(paramIntent.getAction())) {
      return null;
    }
    if (mHandlerThread == null)
    {
      mHandlerThread = new HandlerThread("RESOLVER_RANKER_SERVICE");
      mHandlerThread.start();
      mHandler = new Handler(mHandlerThread.getLooper());
    }
    if (mWrapper == null) {
      mWrapper = new ResolverRankerServiceWrapper(null);
    }
    return mWrapper;
  }
  
  public void onDestroy()
  {
    mHandler = null;
    if (mHandlerThread != null) {
      mHandlerThread.quitSafely();
    }
    super.onDestroy();
  }
  
  public void onPredictSharingProbabilities(List<ResolverTarget> paramList) {}
  
  public void onTrainRankingModel(List<ResolverTarget> paramList, int paramInt) {}
  
  private class ResolverRankerServiceWrapper
    extends IResolverRankerService.Stub
  {
    private ResolverRankerServiceWrapper() {}
    
    public void predict(final List<ResolverTarget> paramList, final IResolverRankerResult paramIResolverRankerResult)
      throws RemoteException
    {
      paramList = new Runnable()
      {
        public void run()
        {
          try
          {
            onPredictSharingProbabilities(paramList);
            ResolverRankerService.sendResult(paramList, paramIResolverRankerResult);
          }
          catch (Exception localException)
          {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("onPredictSharingProbabilities failed; send null results: ");
            localStringBuilder.append(localException);
            Log.e("ResolverRankerService", localStringBuilder.toString());
            ResolverRankerService.sendResult(null, paramIResolverRankerResult);
          }
        }
      };
      paramIResolverRankerResult = mHandler;
      if (paramIResolverRankerResult != null) {
        paramIResolverRankerResult.post(paramList);
      }
    }
    
    public void train(final List<ResolverTarget> paramList, final int paramInt)
      throws RemoteException
    {
      Runnable local2 = new Runnable()
      {
        public void run()
        {
          try
          {
            onTrainRankingModel(paramList, paramInt);
          }
          catch (Exception localException)
          {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("onTrainRankingModel failed; skip train: ");
            localStringBuilder.append(localException);
            Log.e("ResolverRankerService", localStringBuilder.toString());
          }
        }
      };
      paramList = mHandler;
      if (paramList != null) {
        paramList.post(local2);
      }
    }
  }
}
