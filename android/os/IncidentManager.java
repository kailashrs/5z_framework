package android.os;

import android.annotation.SystemApi;
import android.content.Context;
import android.util.Slog;

@SystemApi
public class IncidentManager
{
  private static final String TAG = "IncidentManager";
  private final Context mContext;
  private IIncidentManager mService;
  
  public IncidentManager(Context paramContext)
  {
    mContext = paramContext;
  }
  
  private IIncidentManager getIIncidentManagerLocked()
    throws RemoteException
  {
    if (mService != null) {
      return mService;
    }
    try
    {
      if (mService != null)
      {
        localObject1 = mService;
        return localObject1;
      }
      mService = IIncidentManager.Stub.asInterface(ServiceManager.getService("incident"));
      if (mService != null)
      {
        IBinder localIBinder = mService.asBinder();
        localObject1 = new android/os/IncidentManager$IncidentdDeathRecipient;
        ((IncidentdDeathRecipient)localObject1).<init>(this, null);
        localIBinder.linkToDeath((IBinder.DeathRecipient)localObject1, 0);
      }
      Object localObject1 = mService;
      return localObject1;
    }
    finally {}
  }
  
  private void reportIncidentInternal(IncidentReportArgs paramIncidentReportArgs)
  {
    try
    {
      IIncidentManager localIIncidentManager = getIIncidentManagerLocked();
      if (localIIncidentManager == null)
      {
        Slog.e("IncidentManager", "reportIncident can't find incident binder service");
        return;
      }
      localIIncidentManager.reportIncident(paramIncidentReportArgs);
    }
    catch (RemoteException paramIncidentReportArgs)
    {
      Slog.e("IncidentManager", "reportIncident failed", paramIncidentReportArgs);
    }
  }
  
  public void reportIncident(IncidentReportArgs paramIncidentReportArgs)
  {
    reportIncidentInternal(paramIncidentReportArgs);
  }
  
  private class IncidentdDeathRecipient
    implements IBinder.DeathRecipient
  {
    private IncidentdDeathRecipient() {}
    
    public void binderDied()
    {
      try
      {
        IncidentManager.access$002(IncidentManager.this, null);
        return;
      }
      finally {}
    }
  }
}
