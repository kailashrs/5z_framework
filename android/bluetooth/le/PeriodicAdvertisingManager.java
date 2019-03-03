package android.bluetooth.le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.IBluetoothGatt;
import android.bluetooth.IBluetoothManager;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import java.util.IdentityHashMap;
import java.util.Map;

public final class PeriodicAdvertisingManager
{
  private static final int SKIP_MAX = 499;
  private static final int SKIP_MIN = 0;
  private static final int SYNC_STARTING = -1;
  private static final String TAG = "PeriodicAdvertisingManager";
  private static final int TIMEOUT_MAX = 16384;
  private static final int TIMEOUT_MIN = 10;
  private BluetoothAdapter mBluetoothAdapter;
  private final IBluetoothManager mBluetoothManager;
  Map<PeriodicAdvertisingCallback, IPeriodicAdvertisingCallback> mCallbackWrappers;
  
  public PeriodicAdvertisingManager(IBluetoothManager paramIBluetoothManager)
  {
    mBluetoothManager = paramIBluetoothManager;
    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    mCallbackWrappers = new IdentityHashMap();
  }
  
  private IPeriodicAdvertisingCallback wrap(final PeriodicAdvertisingCallback paramPeriodicAdvertisingCallback, final Handler paramHandler)
  {
    new IPeriodicAdvertisingCallback.Stub()
    {
      public void onPeriodicAdvertisingReport(final PeriodicAdvertisingReport paramAnonymousPeriodicAdvertisingReport)
      {
        paramHandler.post(new Runnable()
        {
          public void run()
          {
            val$callback.onPeriodicAdvertisingReport(paramAnonymousPeriodicAdvertisingReport);
          }
        });
      }
      
      public void onSyncEstablished(final int paramAnonymousInt1, final BluetoothDevice paramAnonymousBluetoothDevice, final int paramAnonymousInt2, final int paramAnonymousInt3, final int paramAnonymousInt4, final int paramAnonymousInt5)
      {
        paramHandler.post(new Runnable()
        {
          public void run()
          {
            val$callback.onSyncEstablished(paramAnonymousInt1, paramAnonymousBluetoothDevice, paramAnonymousInt2, paramAnonymousInt3, paramAnonymousInt4, paramAnonymousInt5);
            if (paramAnonymousInt5 != 0) {
              mCallbackWrappers.remove(val$callback);
            }
          }
        });
      }
      
      public void onSyncLost(final int paramAnonymousInt)
      {
        paramHandler.post(new Runnable()
        {
          public void run()
          {
            val$callback.onSyncLost(paramAnonymousInt);
            mCallbackWrappers.remove(val$callback);
          }
        });
      }
    };
  }
  
  public void registerSync(ScanResult paramScanResult, int paramInt1, int paramInt2, PeriodicAdvertisingCallback paramPeriodicAdvertisingCallback)
  {
    registerSync(paramScanResult, paramInt1, paramInt2, paramPeriodicAdvertisingCallback, null);
  }
  
  public void registerSync(ScanResult paramScanResult, int paramInt1, int paramInt2, PeriodicAdvertisingCallback paramPeriodicAdvertisingCallback, Handler paramHandler)
  {
    if (paramPeriodicAdvertisingCallback != null)
    {
      if (paramScanResult != null)
      {
        if (paramScanResult.getAdvertisingSid() != 255)
        {
          if ((paramInt1 >= 0) && (paramInt1 <= 499)) {
            if ((paramInt2 >= 10) && (paramInt2 <= 16384)) {
              try
              {
                IBluetoothGatt localIBluetoothGatt = mBluetoothManager.getBluetoothGatt();
                Handler localHandler = paramHandler;
                if (paramHandler == null) {
                  localHandler = new Handler(Looper.getMainLooper());
                }
                paramHandler = wrap(paramPeriodicAdvertisingCallback, localHandler);
                mCallbackWrappers.put(paramPeriodicAdvertisingCallback, paramHandler);
                try
                {
                  localIBluetoothGatt.registerSync(paramScanResult, paramInt1, paramInt2, paramHandler);
                  return;
                }
                catch (RemoteException paramScanResult)
                {
                  Log.e("PeriodicAdvertisingManager", "Failed to register sync - ", paramScanResult);
                  return;
                }
                throw new IllegalArgumentException("timeout must be between 10 and 16384");
              }
              catch (RemoteException paramHandler)
              {
                Log.e("PeriodicAdvertisingManager", "Failed to get Bluetooth gatt - ", paramHandler);
                paramPeriodicAdvertisingCallback.onSyncEstablished(0, paramScanResult.getDevice(), paramScanResult.getAdvertisingSid(), paramInt1, paramInt2, 2);
                return;
              }
            }
          }
          throw new IllegalArgumentException("timeout must be between 10 and 16384");
        }
        throw new IllegalArgumentException("scanResult must contain a valid sid");
      }
      throw new IllegalArgumentException("scanResult can't be null");
    }
    throw new IllegalArgumentException("callback can't be null");
  }
  
  public void unregisterSync(PeriodicAdvertisingCallback paramPeriodicAdvertisingCallback)
  {
    if (paramPeriodicAdvertisingCallback != null) {
      try
      {
        IBluetoothGatt localIBluetoothGatt = mBluetoothManager.getBluetoothGatt();
        paramPeriodicAdvertisingCallback = (IPeriodicAdvertisingCallback)mCallbackWrappers.remove(paramPeriodicAdvertisingCallback);
        if (paramPeriodicAdvertisingCallback != null) {
          try
          {
            localIBluetoothGatt.unregisterSync(paramPeriodicAdvertisingCallback);
            return;
          }
          catch (RemoteException paramPeriodicAdvertisingCallback)
          {
            Log.e("PeriodicAdvertisingManager", "Failed to cancel sync creation - ", paramPeriodicAdvertisingCallback);
            return;
          }
        }
        throw new IllegalArgumentException("callback was not properly registered");
      }
      catch (RemoteException paramPeriodicAdvertisingCallback)
      {
        Log.e("PeriodicAdvertisingManager", "Failed to get Bluetooth gatt - ", paramPeriodicAdvertisingCallback);
        return;
      }
    }
    throw new IllegalArgumentException("callback can't be null");
  }
}
