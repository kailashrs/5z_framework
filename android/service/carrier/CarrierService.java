package android.service.carrier;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.util.Log;
import com.android.internal.telephony.ITelephonyRegistry;
import com.android.internal.telephony.ITelephonyRegistry.Stub;

public abstract class CarrierService
  extends Service
{
  public static final String CARRIER_SERVICE_INTERFACE = "android.service.carrier.CarrierService";
  private static final String LOG_TAG = "CarrierService";
  private static ITelephonyRegistry sRegistry;
  private final ICarrierService.Stub mStubWrapper = new ICarrierServiceWrapper();
  
  public CarrierService()
  {
    if (sRegistry == null) {
      sRegistry = ITelephonyRegistry.Stub.asInterface(ServiceManager.getService("telephony.registry"));
    }
  }
  
  public final void notifyCarrierNetworkChange(boolean paramBoolean)
  {
    try
    {
      if (sRegistry != null) {
        sRegistry.notifyCarrierNetworkChange(paramBoolean);
      }
    }
    catch (RemoteException|NullPointerException localRemoteException) {}
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    return mStubWrapper;
  }
  
  public abstract PersistableBundle onLoadConfig(CarrierIdentifier paramCarrierIdentifier);
  
  public class ICarrierServiceWrapper
    extends ICarrierService.Stub
  {
    public static final String KEY_CONFIG_BUNDLE = "config_bundle";
    public static final int RESULT_ERROR = 1;
    public static final int RESULT_OK = 0;
    
    public ICarrierServiceWrapper() {}
    
    public void getCarrierConfig(CarrierIdentifier paramCarrierIdentifier, ResultReceiver paramResultReceiver)
    {
      try
      {
        localObject = new android/os/Bundle;
        ((Bundle)localObject).<init>();
        ((Bundle)localObject).putParcelable("config_bundle", onLoadConfig(paramCarrierIdentifier));
        paramResultReceiver.send(0, (Bundle)localObject);
      }
      catch (Exception paramCarrierIdentifier)
      {
        Object localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Error in onLoadConfig: ");
        ((StringBuilder)localObject).append(paramCarrierIdentifier.getMessage());
        Log.e("CarrierService", ((StringBuilder)localObject).toString(), paramCarrierIdentifier);
        paramResultReceiver.send(1, null);
      }
    }
  }
}
