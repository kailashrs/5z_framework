package android.telephony;

import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.SparseArray;
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.ITelephony.Stub;
import com.android.internal.util.Preconditions;
import java.util.List;
import java.util.concurrent.Executor;

public final class TelephonyScanManager
{
  public static final int CALLBACK_SCAN_COMPLETE = 3;
  public static final int CALLBACK_SCAN_ERROR = 2;
  public static final int CALLBACK_SCAN_RESULTS = 1;
  public static final String SCAN_RESULT_KEY = "scanResult";
  private static final String TAG = "TelephonyScanManager";
  private final Looper mLooper;
  private final Messenger mMessenger;
  private SparseArray<NetworkScanInfo> mScanInfo = new SparseArray();
  
  public TelephonyScanManager()
  {
    HandlerThread localHandlerThread = new HandlerThread("TelephonyScanManager");
    localHandlerThread.start();
    mLooper = localHandlerThread.getLooper();
    mMessenger = new Messenger(new Handler(mLooper)
    {
      public void handleMessage(Message paramAnonymousMessage)
      {
        Preconditions.checkNotNull(paramAnonymousMessage, "message cannot be null");
        synchronized (mScanInfo)
        {
          Object localObject2 = (TelephonyScanManager.NetworkScanInfo)mScanInfo.get(arg2);
          if (localObject2 != null)
          {
            ??? = TelephonyScanManager.NetworkScanInfo.access$100((TelephonyScanManager.NetworkScanInfo)localObject2);
            localObject2 = TelephonyScanManager.NetworkScanInfo.access$200((TelephonyScanManager.NetworkScanInfo)localObject2);
            if (??? != null)
            {
              if (localObject2 != null)
              {
                Object localObject3;
                int i;
                switch (what)
                {
                default: 
                  ??? = new StringBuilder();
                  ((StringBuilder)???).append("Unhandled message ");
                  ((StringBuilder)???).append(Integer.toHexString(what));
                  Rlog.e("TelephonyScanManager", ((StringBuilder)???).toString());
                  break;
                case 3: 
                  try
                  {
                    localObject3 = new android/telephony/_$$Lambda$TelephonyScanManager$1$tGSpVQaVhc4GKIxjcECV_jCGYw4;
                    ((_..Lambda.TelephonyScanManager.1.tGSpVQaVhc4GKIxjcECV_jCGYw4)localObject3).<init>((TelephonyScanManager.NetworkScanCallback)???);
                    ((Executor)localObject2).execute((Runnable)localObject3);
                    mScanInfo.remove(arg2);
                  }
                  catch (Exception paramAnonymousMessage)
                  {
                    Rlog.e("TelephonyScanManager", "Exception in networkscan callback onComplete", paramAnonymousMessage);
                  }
                case 2: 
                  try
                  {
                    i = arg1;
                    paramAnonymousMessage = new android/telephony/_$$Lambda$TelephonyScanManager$1$X9SMshZoHjJ6SzCbmgVMwQip2Q0;
                    paramAnonymousMessage.<init>(i, (TelephonyScanManager.NetworkScanCallback)???);
                    ((Executor)localObject2).execute(paramAnonymousMessage);
                  }
                  catch (Exception paramAnonymousMessage)
                  {
                    Rlog.e("TelephonyScanManager", "Exception in networkscan callback onError", paramAnonymousMessage);
                  }
                case 1: 
                  try
                  {
                    localObject3 = paramAnonymousMessage.getData().getParcelableArray("scanResult");
                    paramAnonymousMessage = new CellInfo[localObject3.length];
                    for (i = 0; i < localObject3.length; i++) {
                      paramAnonymousMessage[i] = ((CellInfo)localObject3[i]);
                    }
                    localObject3 = new android/telephony/_$$Lambda$TelephonyScanManager$1$jmXulbd8FzO5Qb8_Hi_Z6s_nJWI;
                    ((_..Lambda.TelephonyScanManager.1.jmXulbd8FzO5Qb8_Hi_Z6s_nJWI)localObject3).<init>(paramAnonymousMessage, (TelephonyScanManager.NetworkScanCallback)???);
                    ((Executor)localObject2).execute((Runnable)localObject3);
                  }
                  catch (Exception paramAnonymousMessage)
                  {
                    Rlog.e("TelephonyScanManager", "Exception in networkscan callback onResults", paramAnonymousMessage);
                  }
                }
                return;
              }
              ??? = new StringBuilder();
              ((StringBuilder)???).append("Failed to find Executor with id ");
              ((StringBuilder)???).append(arg2);
              throw new RuntimeException(((StringBuilder)???).toString());
            }
            ??? = new StringBuilder();
            ((StringBuilder)???).append("Failed to find NetworkScanCallback with id ");
            ((StringBuilder)???).append(arg2);
            throw new RuntimeException(((StringBuilder)???).toString());
          }
          ??? = new StringBuilder();
          ((StringBuilder)???).append("Failed to find NetworkScanInfo with id ");
          ((StringBuilder)???).append(arg2);
          throw new RuntimeException(((StringBuilder)???).toString());
        }
      }
    });
  }
  
  private ITelephony getITelephony()
  {
    return ITelephony.Stub.asInterface(ServiceManager.getService("phone"));
  }
  
  private void saveScanInfo(int paramInt, NetworkScanRequest paramNetworkScanRequest, Executor paramExecutor, NetworkScanCallback paramNetworkScanCallback)
  {
    synchronized (mScanInfo)
    {
      SparseArray localSparseArray2 = mScanInfo;
      NetworkScanInfo localNetworkScanInfo = new android/telephony/TelephonyScanManager$NetworkScanInfo;
      localNetworkScanInfo.<init>(paramNetworkScanRequest, paramExecutor, paramNetworkScanCallback);
      localSparseArray2.put(paramInt, localNetworkScanInfo);
      return;
    }
  }
  
  public NetworkScan requestNetworkScan(int paramInt, NetworkScanRequest paramNetworkScanRequest, Executor paramExecutor, NetworkScanCallback paramNetworkScanCallback)
  {
    try
    {
      ITelephony localITelephony = getITelephony();
      if (localITelephony != null)
      {
        Messenger localMessenger = mMessenger;
        Binder localBinder = new android/os/Binder;
        localBinder.<init>();
        int i = localITelephony.requestNetworkScan(paramInt, paramNetworkScanRequest, localMessenger, localBinder);
        saveScanInfo(i, paramNetworkScanRequest, paramExecutor, paramNetworkScanCallback);
        paramNetworkScanRequest = new NetworkScan(i, paramInt);
        return paramNetworkScanRequest;
      }
    }
    catch (NullPointerException paramNetworkScanRequest)
    {
      Rlog.e("TelephonyScanManager", "requestNetworkScan NPE", paramNetworkScanRequest);
    }
    catch (RemoteException paramNetworkScanRequest)
    {
      Rlog.e("TelephonyScanManager", "requestNetworkScan RemoteException", paramNetworkScanRequest);
    }
    return null;
  }
  
  public static abstract class NetworkScanCallback
  {
    public NetworkScanCallback() {}
    
    public void onComplete() {}
    
    public void onError(int paramInt) {}
    
    public void onResults(List<CellInfo> paramList) {}
  }
  
  private static class NetworkScanInfo
  {
    private final TelephonyScanManager.NetworkScanCallback mCallback;
    private final Executor mExecutor;
    private final NetworkScanRequest mRequest;
    
    NetworkScanInfo(NetworkScanRequest paramNetworkScanRequest, Executor paramExecutor, TelephonyScanManager.NetworkScanCallback paramNetworkScanCallback)
    {
      mRequest = paramNetworkScanRequest;
      mExecutor = paramExecutor;
      mCallback = paramNetworkScanCallback;
    }
  }
}
