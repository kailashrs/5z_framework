package android.location;

import android.content.Context;
import android.os.RemoteException;
import java.util.List;

class BatchedLocationCallbackTransport
  extends LocalListenerHelper<BatchedLocationCallback>
{
  private final IBatchedLocationCallback mCallbackTransport = new CallbackTransport(null);
  private final ILocationManager mLocationManager;
  
  public BatchedLocationCallbackTransport(Context paramContext, ILocationManager paramILocationManager)
  {
    super(paramContext, "BatchedLocationCallbackTransport");
    mLocationManager = paramILocationManager;
  }
  
  protected boolean registerWithServer()
    throws RemoteException
  {
    return mLocationManager.addGnssBatchingCallback(mCallbackTransport, getContext().getPackageName());
  }
  
  protected void unregisterFromServer()
    throws RemoteException
  {
    mLocationManager.removeGnssBatchingCallback();
  }
  
  private class CallbackTransport
    extends IBatchedLocationCallback.Stub
  {
    private CallbackTransport() {}
    
    public void onLocationBatch(final List<Location> paramList)
    {
      paramList = new LocalListenerHelper.ListenerOperation()
      {
        public void execute(BatchedLocationCallback paramAnonymousBatchedLocationCallback)
          throws RemoteException
        {
          paramAnonymousBatchedLocationCallback.onLocationBatch(paramList);
        }
      };
      foreach(paramList);
    }
  }
}
