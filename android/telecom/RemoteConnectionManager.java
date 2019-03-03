package android.telecom;

import android.content.ComponentName;
import android.os.RemoteException;
import com.android.internal.telecom.IConnectionService;
import java.util.HashMap;
import java.util.Map;

public class RemoteConnectionManager
{
  private final ConnectionService mOurConnectionServiceImpl;
  private final Map<ComponentName, RemoteConnectionService> mRemoteConnectionServices = new HashMap();
  
  public RemoteConnectionManager(ConnectionService paramConnectionService)
  {
    mOurConnectionServiceImpl = paramConnectionService;
  }
  
  void addConnectionService(ComponentName paramComponentName, IConnectionService paramIConnectionService)
  {
    if (!mRemoteConnectionServices.containsKey(paramComponentName)) {
      try
      {
        RemoteConnectionService localRemoteConnectionService = new android/telecom/RemoteConnectionService;
        localRemoteConnectionService.<init>(paramIConnectionService, mOurConnectionServiceImpl);
        mRemoteConnectionServices.put(paramComponentName, localRemoteConnectionService);
      }
      catch (RemoteException paramComponentName) {}
    }
  }
  
  public void conferenceRemoteConnections(RemoteConnection paramRemoteConnection1, RemoteConnection paramRemoteConnection2)
  {
    if (paramRemoteConnection1.getConnectionService() == paramRemoteConnection2.getConnectionService()) {
      try
      {
        paramRemoteConnection1.getConnectionService().conference(paramRemoteConnection1.getId(), paramRemoteConnection2.getId(), null);
      }
      catch (RemoteException paramRemoteConnection1) {}
    } else {
      Log.w(this, "Request to conference incompatible remote connections (%s,%s) (%s,%s)", new Object[] { paramRemoteConnection1.getConnectionService(), paramRemoteConnection1.getId(), paramRemoteConnection2.getConnectionService(), paramRemoteConnection2.getId() });
    }
  }
  
  public RemoteConnection createRemoteConnection(PhoneAccountHandle paramPhoneAccountHandle, ConnectionRequest paramConnectionRequest, boolean paramBoolean)
  {
    if (paramConnectionRequest.getAccountHandle() != null)
    {
      Object localObject = paramConnectionRequest.getAccountHandle().getComponentName();
      if (mRemoteConnectionServices.containsKey(localObject))
      {
        localObject = (RemoteConnectionService)mRemoteConnectionServices.get(localObject);
        if (localObject != null) {
          return ((RemoteConnectionService)localObject).createRemoteConnection(paramPhoneAccountHandle, paramConnectionRequest, paramBoolean);
        }
        return null;
      }
      paramPhoneAccountHandle = new StringBuilder();
      paramPhoneAccountHandle.append("accountHandle not supported: ");
      paramPhoneAccountHandle.append(localObject);
      throw new UnsupportedOperationException(paramPhoneAccountHandle.toString());
    }
    throw new IllegalArgumentException("accountHandle must be specified.");
  }
}
