package android.printservice.recommendation;

import android.annotation.SystemApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import java.util.List;

@SystemApi
public abstract class RecommendationService
  extends Service
{
  private static final String LOG_TAG = "PrintServiceRecS";
  public static final String SERVICE_INTERFACE = "android.printservice.recommendation.RecommendationService";
  private IRecommendationServiceCallbacks mCallbacks;
  private Handler mHandler;
  
  public RecommendationService() {}
  
  protected void attachBaseContext(Context paramContext)
  {
    super.attachBaseContext(paramContext);
    mHandler = new MyHandler();
  }
  
  public final IBinder onBind(Intent paramIntent)
  {
    new IRecommendationService.Stub()
    {
      public void registerCallbacks(IRecommendationServiceCallbacks paramAnonymousIRecommendationServiceCallbacks)
      {
        if (paramAnonymousIRecommendationServiceCallbacks != null) {
          mHandler.obtainMessage(1, paramAnonymousIRecommendationServiceCallbacks).sendToTarget();
        } else {
          mHandler.obtainMessage(2).sendToTarget();
        }
      }
    };
  }
  
  public abstract void onConnected();
  
  public abstract void onDisconnected();
  
  public final void updateRecommendations(List<RecommendationInfo> paramList)
  {
    mHandler.obtainMessage(3, paramList).sendToTarget();
  }
  
  private class MyHandler
    extends Handler
  {
    static final int MSG_CONNECT = 1;
    static final int MSG_DISCONNECT = 2;
    static final int MSG_UPDATE = 3;
    
    MyHandler()
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      switch (what)
      {
      default: 
        break;
      case 3: 
        try
        {
          mCallbacks.onRecommendationsUpdated((List)obj);
        }
        catch (RemoteException|NullPointerException paramMessage)
        {
          Log.e("PrintServiceRecS", "Could not update recommended services", paramMessage);
        }
      case 2: 
        onDisconnected();
        RecommendationService.access$102(RecommendationService.this, null);
        break;
      case 1: 
        RecommendationService.access$102(RecommendationService.this, (IRecommendationServiceCallbacks)obj);
        onConnected();
      }
    }
  }
}
