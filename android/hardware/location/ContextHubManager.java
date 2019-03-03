package android.hardware.location;

import android.annotation.SuppressLint;
import android.annotation.SystemApi;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceManager.ServiceNotFoundException;
import android.util.Log;
import com.android.internal.util.Preconditions;
import java.util.List;
import java.util.concurrent.Executor;

@SystemApi
public final class ContextHubManager
{
  private static final String TAG = "ContextHubManager";
  private Callback mCallback;
  private Handler mCallbackHandler;
  private final IContextHubCallback.Stub mClientCallback = new IContextHubCallback.Stub()
  {
    public void onMessageReceipt(int paramAnonymousInt1, int paramAnonymousInt2, ContextHubMessage paramAnonymousContextHubMessage)
    {
      if (mCallback != null) {
        try
        {
          ContextHubManager.Callback localCallback = mCallback;
          Handler localHandler;
          if (mCallbackHandler == null)
          {
            localHandler = new android/os/Handler;
            localHandler.<init>(mMainLooper);
          }
          else
          {
            localHandler = mCallbackHandler;
          }
          Runnable local1 = new android/hardware/location/ContextHubManager$4$1;
          local1.<init>(this, localCallback, paramAnonymousInt1, paramAnonymousInt2, paramAnonymousContextHubMessage);
          localHandler.post(local1);
        }
        finally {}
      }
      if (mLocalCallback != null) {
        try
        {
          mLocalCallback.onMessageReceipt(paramAnonymousInt1, paramAnonymousInt2, paramAnonymousContextHubMessage);
        }
        finally {}
      }
    }
  };
  @Deprecated
  private ICallback mLocalCallback;
  private final Looper mMainLooper;
  private final IContextHubService mService;
  
  public ContextHubManager(Context paramContext, Looper paramLooper)
    throws ServiceManager.ServiceNotFoundException
  {
    mMainLooper = paramLooper;
    mService = IContextHubService.Stub.asInterface(ServiceManager.getServiceOrThrow("contexthub"));
    try
    {
      mService.registerCallback(mClientCallback);
      return;
    }
    catch (RemoteException paramContext)
    {
      throw paramContext.rethrowFromSystemServer();
    }
  }
  
  private IContextHubClientCallback createClientCallback(final ContextHubClient paramContextHubClient, final ContextHubClientCallback paramContextHubClientCallback, final Executor paramExecutor)
  {
    new IContextHubClientCallback.Stub()
    {
      public void onHubReset()
      {
        paramExecutor.execute(new _..Lambda.ContextHubManager.3.kLhhBRChCeue1LKohd5lK_lfKTU(paramContextHubClientCallback, paramContextHubClient));
      }
      
      public void onMessageFromNanoApp(NanoAppMessage paramAnonymousNanoAppMessage)
      {
        paramExecutor.execute(new _..Lambda.ContextHubManager.3.U9x_HK_GdADIEQ3mS5mDWMNWMu8(paramContextHubClientCallback, paramContextHubClient, paramAnonymousNanoAppMessage));
      }
      
      public void onNanoAppAborted(long paramAnonymousLong, int paramAnonymousInt)
      {
        paramExecutor.execute(new _..Lambda.ContextHubManager.3.hASoxw9hzmd9l2NpC91O5tXLzxU(paramContextHubClientCallback, paramContextHubClient, paramAnonymousLong, paramAnonymousInt));
      }
      
      public void onNanoAppDisabled(long paramAnonymousLong)
      {
        paramExecutor.execute(new _..Lambda.ContextHubManager.3.On2Q5Obzm4_zLY0UP3Xs4E3P_V0(paramContextHubClientCallback, paramContextHubClient, paramAnonymousLong));
      }
      
      public void onNanoAppEnabled(long paramAnonymousLong)
      {
        paramExecutor.execute(new _..Lambda.ContextHubManager.3.8oeFzBAC_VuH1d32Kod8BVn0Os8(paramContextHubClientCallback, paramContextHubClient, paramAnonymousLong));
      }
      
      public void onNanoAppLoaded(long paramAnonymousLong)
      {
        paramExecutor.execute(new _..Lambda.ContextHubManager.3.5yx25kUuvL9qy3uBcIzI3sQQoL8(paramContextHubClientCallback, paramContextHubClient, paramAnonymousLong));
      }
      
      public void onNanoAppUnloaded(long paramAnonymousLong)
      {
        paramExecutor.execute(new _..Lambda.ContextHubManager.3.KgVQePwT_QpjU9EQTp2L3LsHE5Y(paramContextHubClientCallback, paramContextHubClient, paramAnonymousLong));
      }
    };
  }
  
  private IContextHubTransactionCallback createQueryCallback(final ContextHubTransaction<List<NanoAppState>> paramContextHubTransaction)
  {
    new IContextHubTransactionCallback.Stub()
    {
      public void onQueryResponse(int paramAnonymousInt, List<NanoAppState> paramAnonymousList)
      {
        paramContextHubTransaction.setResponse(new ContextHubTransaction.Response(paramAnonymousInt, paramAnonymousList));
      }
      
      public void onTransactionComplete(int paramAnonymousInt)
      {
        Log.e("ContextHubManager", "Received a non-query callback on a query request");
        paramContextHubTransaction.setResponse(new ContextHubTransaction.Response(7, null));
      }
    };
  }
  
  private IContextHubTransactionCallback createTransactionCallback(final ContextHubTransaction<Void> paramContextHubTransaction)
  {
    new IContextHubTransactionCallback.Stub()
    {
      public void onQueryResponse(int paramAnonymousInt, List<NanoAppState> paramAnonymousList)
      {
        Log.e("ContextHubManager", "Received a query callback on a non-query request");
        paramContextHubTransaction.setResponse(new ContextHubTransaction.Response(7, null));
      }
      
      public void onTransactionComplete(int paramAnonymousInt)
      {
        paramContextHubTransaction.setResponse(new ContextHubTransaction.Response(paramAnonymousInt, null));
      }
    };
  }
  
  public ContextHubClient createClient(ContextHubInfo paramContextHubInfo, ContextHubClientCallback paramContextHubClientCallback)
  {
    return createClient(paramContextHubInfo, paramContextHubClientCallback, new HandlerExecutor(Handler.getMain()));
  }
  
  public ContextHubClient createClient(ContextHubInfo paramContextHubInfo, ContextHubClientCallback paramContextHubClientCallback, Executor paramExecutor)
  {
    Preconditions.checkNotNull(paramContextHubClientCallback, "Callback cannot be null");
    Preconditions.checkNotNull(paramContextHubInfo, "ContextHubInfo cannot be null");
    Preconditions.checkNotNull(paramExecutor, "Executor cannot be null");
    ContextHubClient localContextHubClient = new ContextHubClient(paramContextHubInfo);
    paramContextHubClientCallback = createClientCallback(localContextHubClient, paramContextHubClientCallback, paramExecutor);
    try
    {
      paramContextHubInfo = mService.createClient(paramContextHubClientCallback, paramContextHubInfo.getId());
      localContextHubClient.setClientProxy(paramContextHubInfo);
      return localContextHubClient;
    }
    catch (RemoteException paramContextHubInfo)
    {
      throw paramContextHubInfo.rethrowFromSystemServer();
    }
  }
  
  public ContextHubTransaction<Void> disableNanoApp(ContextHubInfo paramContextHubInfo, long paramLong)
  {
    Preconditions.checkNotNull(paramContextHubInfo, "ContextHubInfo cannot be null");
    ContextHubTransaction localContextHubTransaction = new ContextHubTransaction(3);
    IContextHubTransactionCallback localIContextHubTransactionCallback = createTransactionCallback(localContextHubTransaction);
    try
    {
      mService.disableNanoApp(paramContextHubInfo.getId(), localIContextHubTransactionCallback, paramLong);
      return localContextHubTransaction;
    }
    catch (RemoteException paramContextHubInfo)
    {
      throw paramContextHubInfo.rethrowFromSystemServer();
    }
  }
  
  public ContextHubTransaction<Void> enableNanoApp(ContextHubInfo paramContextHubInfo, long paramLong)
  {
    Preconditions.checkNotNull(paramContextHubInfo, "ContextHubInfo cannot be null");
    ContextHubTransaction localContextHubTransaction = new ContextHubTransaction(2);
    IContextHubTransactionCallback localIContextHubTransactionCallback = createTransactionCallback(localContextHubTransaction);
    try
    {
      mService.enableNanoApp(paramContextHubInfo.getId(), localIContextHubTransactionCallback, paramLong);
      return localContextHubTransaction;
    }
    catch (RemoteException paramContextHubInfo)
    {
      throw paramContextHubInfo.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public int[] findNanoAppOnHub(int paramInt, NanoAppFilter paramNanoAppFilter)
  {
    try
    {
      paramNanoAppFilter = mService.findNanoAppOnHub(paramInt, paramNanoAppFilter);
      return paramNanoAppFilter;
    }
    catch (RemoteException paramNanoAppFilter)
    {
      throw paramNanoAppFilter.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public int[] getContextHubHandles()
  {
    try
    {
      int[] arrayOfInt = mService.getContextHubHandles();
      return arrayOfInt;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public ContextHubInfo getContextHubInfo(int paramInt)
  {
    try
    {
      ContextHubInfo localContextHubInfo = mService.getContextHubInfo(paramInt);
      return localContextHubInfo;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<ContextHubInfo> getContextHubs()
  {
    try
    {
      List localList = mService.getContextHubs();
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public NanoAppInstanceInfo getNanoAppInstanceInfo(int paramInt)
  {
    try
    {
      NanoAppInstanceInfo localNanoAppInstanceInfo = mService.getNanoAppInstanceInfo(paramInt);
      return localNanoAppInstanceInfo;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public int loadNanoApp(int paramInt, NanoApp paramNanoApp)
  {
    try
    {
      paramInt = mService.loadNanoApp(paramInt, paramNanoApp);
      return paramInt;
    }
    catch (RemoteException paramNanoApp)
    {
      throw paramNanoApp.rethrowFromSystemServer();
    }
  }
  
  public ContextHubTransaction<Void> loadNanoApp(ContextHubInfo paramContextHubInfo, NanoAppBinary paramNanoAppBinary)
  {
    Preconditions.checkNotNull(paramContextHubInfo, "ContextHubInfo cannot be null");
    Preconditions.checkNotNull(paramNanoAppBinary, "NanoAppBinary cannot be null");
    ContextHubTransaction localContextHubTransaction = new ContextHubTransaction(0);
    IContextHubTransactionCallback localIContextHubTransactionCallback = createTransactionCallback(localContextHubTransaction);
    try
    {
      mService.loadNanoAppOnHub(paramContextHubInfo.getId(), localIContextHubTransactionCallback, paramNanoAppBinary);
      return localContextHubTransaction;
    }
    catch (RemoteException paramContextHubInfo)
    {
      throw paramContextHubInfo.rethrowFromSystemServer();
    }
  }
  
  public ContextHubTransaction<List<NanoAppState>> queryNanoApps(ContextHubInfo paramContextHubInfo)
  {
    Preconditions.checkNotNull(paramContextHubInfo, "ContextHubInfo cannot be null");
    ContextHubTransaction localContextHubTransaction = new ContextHubTransaction(4);
    IContextHubTransactionCallback localIContextHubTransactionCallback = createQueryCallback(localContextHubTransaction);
    try
    {
      mService.queryNanoApps(paramContextHubInfo.getId(), localIContextHubTransactionCallback);
      return localContextHubTransaction;
    }
    catch (RemoteException paramContextHubInfo)
    {
      throw paramContextHubInfo.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  @SuppressLint({"Doclava125"})
  public int registerCallback(Callback paramCallback)
  {
    return registerCallback(paramCallback, null);
  }
  
  @Deprecated
  @SuppressLint({"Doclava125"})
  public int registerCallback(Callback paramCallback, Handler paramHandler)
  {
    try
    {
      if (mCallback != null)
      {
        Log.w("ContextHubManager", "Max number of callbacks reached!");
        return -1;
      }
      mCallback = paramCallback;
      mCallbackHandler = paramHandler;
      return 0;
    }
    finally {}
  }
  
  @Deprecated
  public int registerCallback(ICallback paramICallback)
  {
    if (mLocalCallback != null)
    {
      Log.w("ContextHubManager", "Max number of local callbacks reached!");
      return -1;
    }
    mLocalCallback = paramICallback;
    return 0;
  }
  
  @Deprecated
  public int sendMessage(int paramInt1, int paramInt2, ContextHubMessage paramContextHubMessage)
  {
    try
    {
      paramInt1 = mService.sendMessage(paramInt1, paramInt2, paramContextHubMessage);
      return paramInt1;
    }
    catch (RemoteException paramContextHubMessage)
    {
      throw paramContextHubMessage.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public int unloadNanoApp(int paramInt)
  {
    try
    {
      paramInt = mService.unloadNanoApp(paramInt);
      return paramInt;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public ContextHubTransaction<Void> unloadNanoApp(ContextHubInfo paramContextHubInfo, long paramLong)
  {
    Preconditions.checkNotNull(paramContextHubInfo, "ContextHubInfo cannot be null");
    ContextHubTransaction localContextHubTransaction = new ContextHubTransaction(1);
    IContextHubTransactionCallback localIContextHubTransactionCallback = createTransactionCallback(localContextHubTransaction);
    try
    {
      mService.unloadNanoAppFromHub(paramContextHubInfo.getId(), localIContextHubTransactionCallback, paramLong);
      return localContextHubTransaction;
    }
    catch (RemoteException paramContextHubInfo)
    {
      throw paramContextHubInfo.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  @SuppressLint({"Doclava125"})
  public int unregisterCallback(Callback paramCallback)
  {
    try
    {
      if (paramCallback != mCallback)
      {
        Log.w("ContextHubManager", "Cannot recognize callback!");
        return -1;
      }
      mCallback = null;
      mCallbackHandler = null;
      return 0;
    }
    finally {}
  }
  
  @Deprecated
  public int unregisterCallback(ICallback paramICallback)
  {
    try
    {
      if (paramICallback != mLocalCallback)
      {
        Log.w("ContextHubManager", "Cannot recognize local callback!");
        return -1;
      }
      mLocalCallback = null;
      return 0;
    }
    finally {}
  }
  
  @Deprecated
  public static abstract class Callback
  {
    protected Callback() {}
    
    public abstract void onMessageReceipt(int paramInt1, int paramInt2, ContextHubMessage paramContextHubMessage);
  }
  
  @Deprecated
  public static abstract interface ICallback
  {
    public abstract void onMessageReceipt(int paramInt1, int paramInt2, ContextHubMessage paramContextHubMessage);
  }
}
