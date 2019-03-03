package android.location;

import android.content.Context;
import android.os.RemoteException;

class GnssNavigationMessageCallbackTransport
  extends LocalListenerHelper<GnssNavigationMessage.Callback>
{
  private final IGnssNavigationMessageListener mListenerTransport = new ListenerTransport(null);
  private final ILocationManager mLocationManager;
  
  public GnssNavigationMessageCallbackTransport(Context paramContext, ILocationManager paramILocationManager)
  {
    super(paramContext, "GnssNavigationMessageCallbackTransport");
    mLocationManager = paramILocationManager;
  }
  
  protected boolean registerWithServer()
    throws RemoteException
  {
    return mLocationManager.addGnssNavigationMessageListener(mListenerTransport, getContext().getPackageName());
  }
  
  protected void unregisterFromServer()
    throws RemoteException
  {
    mLocationManager.removeGnssNavigationMessageListener(mListenerTransport);
  }
  
  private class ListenerTransport
    extends IGnssNavigationMessageListener.Stub
  {
    private ListenerTransport() {}
    
    public void onGnssNavigationMessageReceived(final GnssNavigationMessage paramGnssNavigationMessage)
    {
      paramGnssNavigationMessage = new LocalListenerHelper.ListenerOperation()
      {
        public void execute(GnssNavigationMessage.Callback paramAnonymousCallback)
          throws RemoteException
        {
          paramAnonymousCallback.onGnssNavigationMessageReceived(paramGnssNavigationMessage);
        }
      };
      foreach(paramGnssNavigationMessage);
    }
    
    public void onStatusChanged(final int paramInt)
    {
      LocalListenerHelper.ListenerOperation local2 = new LocalListenerHelper.ListenerOperation()
      {
        public void execute(GnssNavigationMessage.Callback paramAnonymousCallback)
          throws RemoteException
        {
          paramAnonymousCallback.onStatusChanged(paramInt);
        }
      };
      foreach(local2);
    }
  }
}
