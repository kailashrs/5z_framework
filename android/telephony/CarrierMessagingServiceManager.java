package android.telephony;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.service.carrier.ICarrierMessagingService;
import android.service.carrier.ICarrierMessagingService.Stub;
import com.android.internal.util.Preconditions;

public abstract class CarrierMessagingServiceManager
{
  private volatile CarrierMessagingServiceConnection mCarrierMessagingServiceConnection;
  
  public CarrierMessagingServiceManager() {}
  
  public boolean bindToCarrierMessagingService(Context paramContext, String paramString)
  {
    boolean bool;
    if (mCarrierMessagingServiceConnection == null) {
      bool = true;
    } else {
      bool = false;
    }
    Preconditions.checkState(bool);
    Intent localIntent = new Intent("android.service.carrier.CarrierMessagingService");
    localIntent.setPackage(paramString);
    mCarrierMessagingServiceConnection = new CarrierMessagingServiceConnection(null);
    return paramContext.bindService(localIntent, mCarrierMessagingServiceConnection, 1);
  }
  
  public void disposeConnection(Context paramContext)
  {
    Preconditions.checkNotNull(mCarrierMessagingServiceConnection);
    paramContext.unbindService(mCarrierMessagingServiceConnection);
    mCarrierMessagingServiceConnection = null;
  }
  
  protected abstract void onServiceReady(ICarrierMessagingService paramICarrierMessagingService);
  
  private final class CarrierMessagingServiceConnection
    implements ServiceConnection
  {
    private CarrierMessagingServiceConnection() {}
    
    public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
    {
      onServiceReady(ICarrierMessagingService.Stub.asInterface(paramIBinder));
    }
    
    public void onServiceDisconnected(ComponentName paramComponentName) {}
  }
}
