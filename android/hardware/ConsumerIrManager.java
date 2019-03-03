package android.hardware;

import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceManager.ServiceNotFoundException;
import android.util.Log;

public final class ConsumerIrManager
{
  private static final String TAG = "ConsumerIr";
  private final String mPackageName;
  private final IConsumerIrService mService;
  
  public ConsumerIrManager(Context paramContext)
    throws ServiceManager.ServiceNotFoundException
  {
    mPackageName = paramContext.getPackageName();
    mService = IConsumerIrService.Stub.asInterface(ServiceManager.getServiceOrThrow("consumer_ir"));
  }
  
  public CarrierFrequencyRange[] getCarrierFrequencies()
  {
    if (mService == null)
    {
      Log.w("ConsumerIr", "no consumer ir service.");
      return null;
    }
    try
    {
      int[] arrayOfInt = mService.getCarrierFrequencies();
      if (arrayOfInt.length % 2 != 0)
      {
        Log.w("ConsumerIr", "consumer ir service returned an uneven number of frequencies.");
        return null;
      }
      CarrierFrequencyRange[] arrayOfCarrierFrequencyRange = new CarrierFrequencyRange[arrayOfInt.length / 2];
      for (int i = 0; i < arrayOfInt.length; i += 2) {
        arrayOfCarrierFrequencyRange[(i / 2)] = new CarrierFrequencyRange(arrayOfInt[i], arrayOfInt[(i + 1)]);
      }
      return arrayOfCarrierFrequencyRange;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean hasIrEmitter()
  {
    if (mService == null)
    {
      Log.w("ConsumerIr", "no consumer ir service.");
      return false;
    }
    try
    {
      boolean bool = mService.hasIrEmitter();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void transmit(int paramInt, int[] paramArrayOfInt)
  {
    if (mService == null)
    {
      Log.w("ConsumerIr", "failed to transmit; no consumer ir service.");
      return;
    }
    try
    {
      mService.transmit(mPackageName, paramInt, paramArrayOfInt);
      return;
    }
    catch (RemoteException paramArrayOfInt)
    {
      throw paramArrayOfInt.rethrowFromSystemServer();
    }
  }
  
  public final class CarrierFrequencyRange
  {
    private final int mMaxFrequency;
    private final int mMinFrequency;
    
    public CarrierFrequencyRange(int paramInt1, int paramInt2)
    {
      mMinFrequency = paramInt1;
      mMaxFrequency = paramInt2;
    }
    
    public int getMaxFrequency()
    {
      return mMaxFrequency;
    }
    
    public int getMinFrequency()
    {
      return mMinFrequency;
    }
  }
}
