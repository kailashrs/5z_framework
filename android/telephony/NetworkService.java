package android.telephony;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class NetworkService
  extends Service
{
  private static final int NETWORK_SERVICE_CREATE_NETWORK_SERVICE_PROVIDER = 1;
  public static final String NETWORK_SERVICE_EXTRA_SLOT_ID = "android.telephony.extra.SLOT_ID";
  private static final int NETWORK_SERVICE_GET_REGISTRATION_STATE = 4;
  private static final int NETWORK_SERVICE_INDICATION_NETWORK_STATE_CHANGED = 7;
  public static final String NETWORK_SERVICE_INTERFACE = "android.telephony.NetworkService";
  private static final int NETWORK_SERVICE_REGISTER_FOR_STATE_CHANGE = 5;
  private static final int NETWORK_SERVICE_REMOVE_ALL_NETWORK_SERVICE_PROVIDERS = 3;
  private static final int NETWORK_SERVICE_REMOVE_NETWORK_SERVICE_PROVIDER = 2;
  private static final int NETWORK_SERVICE_UNREGISTER_FOR_STATE_CHANGE = 6;
  private final String TAG = NetworkService.class.getSimpleName();
  @VisibleForTesting
  public final INetworkServiceWrapper mBinder = new INetworkServiceWrapper(null);
  private final NetworkServiceHandler mHandler;
  private final HandlerThread mHandlerThread = new HandlerThread(TAG);
  private final SparseArray<NetworkServiceProvider> mServiceMap = new SparseArray();
  
  public NetworkService()
  {
    mHandlerThread.start();
    mHandler = new NetworkServiceHandler(mHandlerThread.getLooper());
    log("network service created");
  }
  
  private final void log(String paramString)
  {
    Rlog.d(TAG, paramString);
  }
  
  private final void loge(String paramString)
  {
    Rlog.e(TAG, paramString);
  }
  
  protected abstract NetworkServiceProvider createNetworkServiceProvider(int paramInt);
  
  public IBinder onBind(Intent paramIntent)
  {
    if ((paramIntent != null) && ("android.telephony.NetworkService".equals(paramIntent.getAction()))) {
      return mBinder;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unexpected intent ");
    localStringBuilder.append(paramIntent);
    loge(localStringBuilder.toString());
    return null;
  }
  
  public void onDestroy()
  {
    mHandlerThread.quit();
  }
  
  public boolean onUnbind(Intent paramIntent)
  {
    mHandler.obtainMessage(3, 0, 0, null).sendToTarget();
    return false;
  }
  
  private class INetworkServiceWrapper
    extends INetworkService.Stub
  {
    private INetworkServiceWrapper() {}
    
    public void createNetworkServiceProvider(int paramInt)
    {
      mHandler.obtainMessage(1, paramInt, 0, null).sendToTarget();
    }
    
    public void getNetworkRegistrationState(int paramInt1, int paramInt2, INetworkServiceCallback paramINetworkServiceCallback)
    {
      mHandler.obtainMessage(4, paramInt1, paramInt2, paramINetworkServiceCallback).sendToTarget();
    }
    
    public void registerForNetworkRegistrationStateChanged(int paramInt, INetworkServiceCallback paramINetworkServiceCallback)
    {
      mHandler.obtainMessage(5, paramInt, 0, paramINetworkServiceCallback).sendToTarget();
    }
    
    public void removeNetworkServiceProvider(int paramInt)
    {
      mHandler.obtainMessage(2, paramInt, 0, null).sendToTarget();
    }
    
    public void unregisterForNetworkRegistrationStateChanged(int paramInt, INetworkServiceCallback paramINetworkServiceCallback)
    {
      mHandler.obtainMessage(6, paramInt, 0, paramINetworkServiceCallback).sendToTarget();
    }
  }
  
  private class NetworkServiceHandler
    extends Handler
  {
    NetworkServiceHandler(Looper paramLooper)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      int i = arg1;
      INetworkServiceCallback localINetworkServiceCallback = (INetworkServiceCallback)obj;
      NetworkService.NetworkServiceProvider localNetworkServiceProvider = (NetworkService.NetworkServiceProvider)mServiceMap.get(i);
      switch (what)
      {
      default: 
        break;
      case 7: 
        if (localNetworkServiceProvider != null) {
          NetworkService.NetworkServiceProvider.access$500(localNetworkServiceProvider);
        }
        break;
      case 6: 
        if (localNetworkServiceProvider != null) {
          NetworkService.NetworkServiceProvider.access$400(localNetworkServiceProvider, localINetworkServiceCallback);
        }
        break;
      case 5: 
        if (localNetworkServiceProvider != null) {
          NetworkService.NetworkServiceProvider.access$300(localNetworkServiceProvider, localINetworkServiceCallback);
        }
        break;
      case 4: 
        if (localNetworkServiceProvider != null) {
          localNetworkServiceProvider.getNetworkRegistrationState(arg2, new NetworkServiceCallback(localINetworkServiceCallback));
        }
        break;
      case 3: 
        for (i = 0; i < mServiceMap.size(); i++)
        {
          paramMessage = (NetworkService.NetworkServiceProvider)mServiceMap.get(i);
          if (paramMessage != null) {
            paramMessage.onDestroy();
          }
        }
        mServiceMap.clear();
        break;
      case 2: 
        if (localNetworkServiceProvider != null)
        {
          localNetworkServiceProvider.onDestroy();
          mServiceMap.remove(i);
        }
        break;
      case 1: 
        if (localNetworkServiceProvider == null) {
          mServiceMap.put(i, createNetworkServiceProvider(i));
        }
        break;
      }
    }
  }
  
  public class NetworkServiceProvider
  {
    private final List<INetworkServiceCallback> mNetworkRegistrationStateChangedCallbacks = new ArrayList();
    private final int mSlotId;
    
    public NetworkServiceProvider(int paramInt)
    {
      mSlotId = paramInt;
    }
    
    private void notifyStateChangedToCallbacks()
    {
      Iterator localIterator = mNetworkRegistrationStateChangedCallbacks.iterator();
      while (localIterator.hasNext())
      {
        INetworkServiceCallback localINetworkServiceCallback = (INetworkServiceCallback)localIterator.next();
        try
        {
          localINetworkServiceCallback.onNetworkStateChanged();
        }
        catch (RemoteException localRemoteException) {}
      }
    }
    
    private void registerForStateChanged(INetworkServiceCallback paramINetworkServiceCallback)
    {
      synchronized (mNetworkRegistrationStateChangedCallbacks)
      {
        mNetworkRegistrationStateChangedCallbacks.add(paramINetworkServiceCallback);
        return;
      }
    }
    
    private void unregisterForStateChanged(INetworkServiceCallback paramINetworkServiceCallback)
    {
      synchronized (mNetworkRegistrationStateChangedCallbacks)
      {
        mNetworkRegistrationStateChangedCallbacks.remove(paramINetworkServiceCallback);
        return;
      }
    }
    
    public void getNetworkRegistrationState(int paramInt, NetworkServiceCallback paramNetworkServiceCallback)
    {
      paramNetworkServiceCallback.onGetNetworkRegistrationStateComplete(1, null);
    }
    
    public final int getSlotId()
    {
      return mSlotId;
    }
    
    public final void notifyNetworkRegistrationStateChanged()
    {
      mHandler.obtainMessage(7, mSlotId, 0, null).sendToTarget();
    }
    
    protected void onDestroy()
    {
      mNetworkRegistrationStateChangedCallbacks.clear();
    }
  }
}
