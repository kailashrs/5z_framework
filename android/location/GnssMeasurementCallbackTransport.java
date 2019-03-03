package android.location;

import android.content.Context;
import android.os.RemoteException;

class GnssMeasurementCallbackTransport
  extends LocalListenerHelper<GnssMeasurementsEvent.Callback>
{
  private final IGnssMeasurementsListener mListenerTransport = new ListenerTransport(null);
  private final ILocationManager mLocationManager;
  
  public GnssMeasurementCallbackTransport(Context paramContext, ILocationManager paramILocationManager)
  {
    super(paramContext, "GnssMeasurementListenerTransport");
    mLocationManager = paramILocationManager;
  }
  
  protected boolean registerWithServer()
    throws RemoteException
  {
    return mLocationManager.addGnssMeasurementsListener(mListenerTransport, getContext().getPackageName());
  }
  
  protected void unregisterFromServer()
    throws RemoteException
  {
    mLocationManager.removeGnssMeasurementsListener(mListenerTransport);
  }
  
  private class ListenerTransport
    extends IGnssMeasurementsListener.Stub
  {
    private ListenerTransport() {}
    
    public void onGnssMeasurementsReceived(final GnssMeasurementsEvent paramGnssMeasurementsEvent)
    {
      paramGnssMeasurementsEvent = new LocalListenerHelper.ListenerOperation()
      {
        public void execute(GnssMeasurementsEvent.Callback paramAnonymousCallback)
          throws RemoteException
        {
          paramAnonymousCallback.onGnssMeasurementsReceived(paramGnssMeasurementsEvent);
        }
      };
      foreach(paramGnssMeasurementsEvent);
    }
    
    public void onStatusChanged(final int paramInt)
    {
      LocalListenerHelper.ListenerOperation local2 = new LocalListenerHelper.ListenerOperation()
      {
        public void execute(GnssMeasurementsEvent.Callback paramAnonymousCallback)
          throws RemoteException
        {
          paramAnonymousCallback.onStatusChanged(paramInt);
        }
      };
      foreach(local2);
    }
  }
}
