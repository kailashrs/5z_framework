package android.net.lowpan;

import android.os.Handler;
import android.os.RemoteException;
import android.os.ServiceSpecificException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LowpanScanner
{
  private static final String TAG = LowpanScanner.class.getSimpleName();
  private ILowpanInterface mBinder;
  private Callback mCallback = null;
  private ArrayList<Integer> mChannelMask = null;
  private Handler mHandler = null;
  private int mTxPower = Integer.MAX_VALUE;
  
  LowpanScanner(ILowpanInterface paramILowpanInterface)
  {
    mBinder = paramILowpanInterface;
  }
  
  private Map<String, Object> createScanOptionMap()
  {
    HashMap localHashMap = new HashMap();
    if (mChannelMask != null) {
      LowpanProperties.KEY_CHANNEL_MASK.putInMap(localHashMap, mChannelMask.stream().mapToInt(_..Lambda.LowpanScanner.b0nnjTe02JXonssLsm5Kp4EaFqs.INSTANCE).toArray());
    }
    if (mTxPower != Integer.MAX_VALUE) {
      LowpanProperties.KEY_MAX_TX_POWER.putInMap(localHashMap, Integer.valueOf(mTxPower));
    }
    return localHashMap;
  }
  
  public void addChannel(int paramInt)
  {
    if (mChannelMask == null) {
      mChannelMask = new ArrayList();
    }
    mChannelMask.add(Integer.valueOf(paramInt));
  }
  
  public Collection<Integer> getChannelMask()
  {
    return (Collection)mChannelMask.clone();
  }
  
  public int getTxPower()
  {
    return mTxPower;
  }
  
  public void setCallback(Callback paramCallback)
  {
    setCallback(paramCallback, null);
  }
  
  public void setCallback(Callback paramCallback, Handler paramHandler)
  {
    try
    {
      mCallback = paramCallback;
      mHandler = paramHandler;
      return;
    }
    finally
    {
      paramCallback = finally;
      throw paramCallback;
    }
  }
  
  public void setChannelMask(Collection<Integer> paramCollection)
  {
    if (paramCollection == null)
    {
      mChannelMask = null;
    }
    else
    {
      if (mChannelMask == null) {
        mChannelMask = new ArrayList();
      } else {
        mChannelMask.clear();
      }
      mChannelMask.addAll(paramCollection);
    }
  }
  
  public void setTxPower(int paramInt)
  {
    mTxPower = paramInt;
  }
  
  public void startEnergyScan()
    throws LowpanException
  {
    Map localMap = createScanOptionMap();
    ILowpanEnergyScanCallback.Stub local2 = new ILowpanEnergyScanCallback.Stub()
    {
      public void onEnergyScanFinished()
      {
        Object localObject = mCallback;
        Handler localHandler = mHandler;
        if (localObject == null) {
          return;
        }
        localObject = new _..Lambda.LowpanScanner.2.n8MSb22N9MEsazioSumvyQhW3Z4((LowpanScanner.Callback)localObject);
        if (localHandler != null) {
          localHandler.post((Runnable)localObject);
        } else {
          ((Runnable)localObject).run();
        }
      }
      
      public void onEnergyScanResult(int paramAnonymousInt1, int paramAnonymousInt2)
      {
        Object localObject = mCallback;
        Handler localHandler = mHandler;
        if (localObject == null) {
          return;
        }
        localObject = new _..Lambda.LowpanScanner.2.GBDCgjndr24KQueHMX2qGNtfLPg((LowpanScanner.Callback)localObject, paramAnonymousInt1, paramAnonymousInt2);
        if (localHandler != null) {
          localHandler.post((Runnable)localObject);
        } else {
          ((Runnable)localObject).run();
        }
      }
    };
    try
    {
      mBinder.startEnergyScan(localMap, local2);
      return;
    }
    catch (ServiceSpecificException localServiceSpecificException)
    {
      throw LowpanException.rethrowFromServiceSpecificException(localServiceSpecificException);
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowAsRuntimeException();
    }
  }
  
  public void startNetScan()
    throws LowpanException
  {
    Map localMap = createScanOptionMap();
    ILowpanNetScanCallback.Stub local1 = new ILowpanNetScanCallback.Stub()
    {
      public void onNetScanBeacon(LowpanBeaconInfo paramAnonymousLowpanBeaconInfo)
      {
        synchronized (LowpanScanner.this)
        {
          LowpanScanner.Callback localCallback = mCallback;
          Handler localHandler = mHandler;
          if (localCallback == null) {
            return;
          }
          paramAnonymousLowpanBeaconInfo = new _..Lambda.LowpanScanner.1.47buDsybUOrvvSl0JOZR_FC9ISg(localCallback, paramAnonymousLowpanBeaconInfo);
          if (localHandler != null) {
            localHandler.post(paramAnonymousLowpanBeaconInfo);
          } else {
            paramAnonymousLowpanBeaconInfo.run();
          }
          return;
        }
      }
      
      public void onNetScanFinished()
      {
        synchronized (LowpanScanner.this)
        {
          LowpanScanner.Callback localCallback = mCallback;
          Handler localHandler = mHandler;
          if (localCallback == null) {
            return;
          }
          ??? = new _..Lambda.LowpanScanner.1.lUw1npYnRpaO9LS5odGyASQYaic(localCallback);
          if (localHandler != null) {
            localHandler.post((Runnable)???);
          } else {
            ((Runnable)???).run();
          }
          return;
        }
      }
    };
    try
    {
      mBinder.startNetScan(localMap, local1);
      return;
    }
    catch (ServiceSpecificException localServiceSpecificException)
    {
      throw LowpanException.rethrowFromServiceSpecificException(localServiceSpecificException);
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowAsRuntimeException();
    }
  }
  
  public void stopEnergyScan()
  {
    try
    {
      mBinder.stopEnergyScan();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowAsRuntimeException();
    }
  }
  
  public void stopNetScan()
  {
    try
    {
      mBinder.stopNetScan();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowAsRuntimeException();
    }
  }
  
  public static abstract class Callback
  {
    public Callback() {}
    
    public void onEnergyScanResult(LowpanEnergyScanResult paramLowpanEnergyScanResult) {}
    
    public void onNetScanBeacon(LowpanBeaconInfo paramLowpanBeaconInfo) {}
    
    public void onScanFinished() {}
  }
}
