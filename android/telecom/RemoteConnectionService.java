package android.telecom;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.telecom.Logging.Session.Info;
import com.android.internal.telecom.IConnectionService;
import com.android.internal.telecom.IConnectionServiceAdapter;
import com.android.internal.telecom.IVideoProvider;
import com.android.internal.telecom.RemoteServiceCallback;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

final class RemoteConnectionService
{
  private static final RemoteConference NULL_CONFERENCE = new RemoteConference("NULL", null);
  private static final RemoteConnection NULL_CONNECTION = new RemoteConnection("NULL", null, (ConnectionRequest)null);
  private final Map<String, RemoteConference> mConferenceById = new HashMap();
  private final Map<String, RemoteConnection> mConnectionById = new HashMap();
  private final IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient()
  {
    public void binderDied()
    {
      Iterator localIterator = mConnectionById.values().iterator();
      while (localIterator.hasNext()) {
        ((RemoteConnection)localIterator.next()).setDestroyed();
      }
      localIterator = mConferenceById.values().iterator();
      while (localIterator.hasNext()) {
        ((RemoteConference)localIterator.next()).setDestroyed();
      }
      mConnectionById.clear();
      mConferenceById.clear();
      mPendingConnections.clear();
      mOutgoingConnectionServiceRpc.asBinder().unlinkToDeath(mDeathRecipient, 0);
    }
  };
  private final ConnectionService mOurConnectionServiceImpl;
  private final IConnectionService mOutgoingConnectionServiceRpc;
  private final Set<RemoteConnection> mPendingConnections = new HashSet();
  private final ConnectionServiceAdapterServant mServant = new ConnectionServiceAdapterServant(mServantDelegate);
  private final IConnectionServiceAdapter mServantDelegate = new IConnectionServiceAdapter()
  {
    public void addConferenceCall(final String paramAnonymousString, ParcelableConference paramAnonymousParcelableConference, Session.Info paramAnonymousInfo)
    {
      paramAnonymousInfo = new RemoteConference(paramAnonymousString, mOutgoingConnectionServiceRpc);
      Iterator localIterator = paramAnonymousParcelableConference.getConnectionIds().iterator();
      while (localIterator.hasNext())
      {
        Object localObject = (String)localIterator.next();
        localObject = (RemoteConnection)mConnectionById.get(localObject);
        if (localObject != null) {
          paramAnonymousInfo.addConnection((RemoteConnection)localObject);
        }
      }
      if (paramAnonymousInfo.getConnections().size() == 0)
      {
        Log.d(this, "addConferenceCall - skipping", new Object[0]);
        return;
      }
      paramAnonymousInfo.setState(paramAnonymousParcelableConference.getState());
      paramAnonymousInfo.setConnectionCapabilities(paramAnonymousParcelableConference.getConnectionCapabilities());
      paramAnonymousInfo.setConnectionProperties(paramAnonymousParcelableConference.getConnectionProperties());
      paramAnonymousInfo.putExtras(paramAnonymousParcelableConference.getExtras());
      mConferenceById.put(paramAnonymousString, paramAnonymousInfo);
      paramAnonymousParcelableConference = new Bundle();
      paramAnonymousParcelableConference.putString("android.telecom.extra.ORIGINAL_CONNECTION_ID", paramAnonymousString);
      paramAnonymousInfo.putExtras(paramAnonymousParcelableConference);
      paramAnonymousInfo.registerCallback(new RemoteConference.Callback()
      {
        public void onDestroyed(RemoteConference paramAnonymous2RemoteConference)
        {
          mConferenceById.remove(paramAnonymousString);
          RemoteConnectionService.this.maybeDisconnectAdapter();
        }
      });
      mOurConnectionServiceImpl.addRemoteConference(paramAnonymousInfo);
    }
    
    public void addExistingConnection(final String paramAnonymousString, ParcelableConnection paramAnonymousParcelableConnection, Session.Info paramAnonymousInfo)
    {
      paramAnonymousInfo = mOurConnectionServiceImpl.getApplicationContext().getOpPackageName();
      int i = mOurConnectionServiceImpl.getApplicationInfo().targetSdkVersion;
      paramAnonymousParcelableConnection = new RemoteConnection(paramAnonymousString, mOutgoingConnectionServiceRpc, paramAnonymousParcelableConnection, paramAnonymousInfo, i);
      mConnectionById.put(paramAnonymousString, paramAnonymousParcelableConnection);
      paramAnonymousParcelableConnection.registerCallback(new RemoteConnection.Callback()
      {
        public void onDestroyed(RemoteConnection paramAnonymous2RemoteConnection)
        {
          mConnectionById.remove(paramAnonymousString);
          RemoteConnectionService.this.maybeDisconnectAdapter();
        }
      });
      mOurConnectionServiceImpl.addRemoteExistingConnection(paramAnonymousParcelableConnection);
    }
    
    public IBinder asBinder()
    {
      throw new UnsupportedOperationException();
    }
    
    public void handleCreateConnectionComplete(String paramAnonymousString, ConnectionRequest paramAnonymousConnectionRequest, ParcelableConnection paramAnonymousParcelableConnection, Session.Info paramAnonymousInfo)
    {
      RemoteConnection localRemoteConnection = RemoteConnectionService.this.findConnectionForAction(paramAnonymousString, "handleCreateConnectionSuccessful");
      if ((localRemoteConnection != RemoteConnectionService.NULL_CONNECTION) && (mPendingConnections.contains(localRemoteConnection)))
      {
        mPendingConnections.remove(localRemoteConnection);
        localRemoteConnection.setConnectionCapabilities(paramAnonymousParcelableConnection.getConnectionCapabilities());
        localRemoteConnection.setConnectionProperties(paramAnonymousParcelableConnection.getConnectionProperties());
        if ((paramAnonymousParcelableConnection.getHandle() != null) || (paramAnonymousParcelableConnection.getState() != 6)) {
          localRemoteConnection.setAddress(paramAnonymousParcelableConnection.getHandle(), paramAnonymousParcelableConnection.getHandlePresentation());
        }
        if ((paramAnonymousParcelableConnection.getCallerDisplayName() != null) || (paramAnonymousParcelableConnection.getState() != 6)) {
          localRemoteConnection.setCallerDisplayName(paramAnonymousParcelableConnection.getCallerDisplayName(), paramAnonymousParcelableConnection.getCallerDisplayNamePresentation());
        }
        if (paramAnonymousParcelableConnection.getState() == 6) {
          localRemoteConnection.setDisconnected(paramAnonymousParcelableConnection.getDisconnectCause());
        } else {
          localRemoteConnection.setState(paramAnonymousParcelableConnection.getState());
        }
        paramAnonymousConnectionRequest = new ArrayList();
        paramAnonymousString = paramAnonymousParcelableConnection.getConferenceableConnectionIds().iterator();
        while (paramAnonymousString.hasNext())
        {
          paramAnonymousInfo = (String)paramAnonymousString.next();
          if (mConnectionById.containsKey(paramAnonymousInfo)) {
            paramAnonymousConnectionRequest.add((RemoteConnection)mConnectionById.get(paramAnonymousInfo));
          }
        }
        localRemoteConnection.setConferenceableConnections(paramAnonymousConnectionRequest);
        localRemoteConnection.setVideoState(paramAnonymousParcelableConnection.getVideoState());
        if (localRemoteConnection.getState() == 6) {
          localRemoteConnection.setDestroyed();
        }
        localRemoteConnection.setStatusHints(paramAnonymousParcelableConnection.getStatusHints());
        localRemoteConnection.setIsVoipAudioMode(paramAnonymousParcelableConnection.getIsVoipAudioMode());
        localRemoteConnection.setRingbackRequested(paramAnonymousParcelableConnection.isRingbackRequested());
        localRemoteConnection.putExtras(paramAnonymousParcelableConnection.getExtras());
      }
    }
    
    public void onConnectionEvent(String paramAnonymousString1, String paramAnonymousString2, Bundle paramAnonymousBundle, Session.Info paramAnonymousInfo)
    {
      if (mConnectionById.containsKey(paramAnonymousString1)) {
        RemoteConnectionService.this.findConnectionForAction(paramAnonymousString1, "onConnectionEvent").onConnectionEvent(paramAnonymousString2, paramAnonymousBundle);
      }
    }
    
    public void onConnectionServiceFocusReleased(Session.Info paramAnonymousInfo) {}
    
    public void onPhoneAccountChanged(String paramAnonymousString, PhoneAccountHandle paramAnonymousPhoneAccountHandle, Session.Info paramAnonymousInfo) {}
    
    public void onPostDialChar(String paramAnonymousString, char paramAnonymousChar, Session.Info paramAnonymousInfo)
    {
      RemoteConnectionService.this.findConnectionForAction(paramAnonymousString, "onPostDialChar").onPostDialChar(paramAnonymousChar);
    }
    
    public void onPostDialWait(String paramAnonymousString1, String paramAnonymousString2, Session.Info paramAnonymousInfo)
    {
      RemoteConnectionService.this.findConnectionForAction(paramAnonymousString1, "onPostDialWait").setPostDialWait(paramAnonymousString2);
    }
    
    public void onRemoteRttRequest(String paramAnonymousString, Session.Info paramAnonymousInfo)
      throws RemoteException
    {
      if (RemoteConnectionService.this.hasConnection(paramAnonymousString)) {
        RemoteConnectionService.this.findConnectionForAction(paramAnonymousString, "onRemoteRttRequest").onRemoteRttRequest();
      } else {
        Log.w(this, "onRemoteRttRequest called on a remote conference", new Object[0]);
      }
    }
    
    public void onRttInitiationFailure(String paramAnonymousString, int paramAnonymousInt, Session.Info paramAnonymousInfo)
      throws RemoteException
    {
      if (RemoteConnectionService.this.hasConnection(paramAnonymousString)) {
        RemoteConnectionService.this.findConnectionForAction(paramAnonymousString, "onRttInitiationFailure").onRttInitiationFailure(paramAnonymousInt);
      } else {
        Log.w(this, "onRttInitiationFailure called on a remote conference", new Object[0]);
      }
    }
    
    public void onRttInitiationSuccess(String paramAnonymousString, Session.Info paramAnonymousInfo)
      throws RemoteException
    {
      if (RemoteConnectionService.this.hasConnection(paramAnonymousString)) {
        RemoteConnectionService.this.findConnectionForAction(paramAnonymousString, "onRttInitiationSuccess").onRttInitiationSuccess();
      } else {
        Log.w(this, "onRttInitiationSuccess called on a remote conference", new Object[0]);
      }
    }
    
    public void onRttSessionRemotelyTerminated(String paramAnonymousString, Session.Info paramAnonymousInfo)
      throws RemoteException
    {
      if (RemoteConnectionService.this.hasConnection(paramAnonymousString)) {
        RemoteConnectionService.this.findConnectionForAction(paramAnonymousString, "onRttSessionRemotelyTerminated").onRttSessionRemotelyTerminated();
      } else {
        Log.w(this, "onRttSessionRemotelyTerminated called on a remote conference", new Object[0]);
      }
    }
    
    public void putExtras(String paramAnonymousString, Bundle paramAnonymousBundle, Session.Info paramAnonymousInfo)
    {
      if (RemoteConnectionService.this.hasConnection(paramAnonymousString)) {
        RemoteConnectionService.this.findConnectionForAction(paramAnonymousString, "putExtras").putExtras(paramAnonymousBundle);
      } else {
        RemoteConnectionService.this.findConferenceForAction(paramAnonymousString, "putExtras").putExtras(paramAnonymousBundle);
      }
    }
    
    public void queryRemoteConnectionServices(RemoteServiceCallback paramAnonymousRemoteServiceCallback, Session.Info paramAnonymousInfo) {}
    
    public void removeCall(String paramAnonymousString, Session.Info paramAnonymousInfo)
    {
      if (mConnectionById.containsKey(paramAnonymousString)) {
        RemoteConnectionService.this.findConnectionForAction(paramAnonymousString, "removeCall").setDestroyed();
      } else {
        RemoteConnectionService.this.findConferenceForAction(paramAnonymousString, "removeCall").setDestroyed();
      }
    }
    
    public void removeExtras(String paramAnonymousString, List<String> paramAnonymousList, Session.Info paramAnonymousInfo)
    {
      if (RemoteConnectionService.this.hasConnection(paramAnonymousString)) {
        RemoteConnectionService.this.findConnectionForAction(paramAnonymousString, "removeExtra").removeExtras(paramAnonymousList);
      } else {
        RemoteConnectionService.this.findConferenceForAction(paramAnonymousString, "removeExtra").removeExtras(paramAnonymousList);
      }
    }
    
    public void resetCdmaConnectionTime(String paramAnonymousString, Session.Info paramAnonymousInfo) {}
    
    public void setActive(String paramAnonymousString, Session.Info paramAnonymousInfo)
    {
      if (mConnectionById.containsKey(paramAnonymousString)) {
        RemoteConnectionService.this.findConnectionForAction(paramAnonymousString, "setActive").setState(4);
      } else {
        RemoteConnectionService.this.findConferenceForAction(paramAnonymousString, "setActive").setState(4);
      }
    }
    
    public void setAddress(String paramAnonymousString, Uri paramAnonymousUri, int paramAnonymousInt, Session.Info paramAnonymousInfo)
    {
      RemoteConnectionService.this.findConnectionForAction(paramAnonymousString, "setAddress").setAddress(paramAnonymousUri, paramAnonymousInt);
    }
    
    public void setAudioRoute(String paramAnonymousString1, int paramAnonymousInt, String paramAnonymousString2, Session.Info paramAnonymousInfo)
    {
      RemoteConnectionService.this.hasConnection(paramAnonymousString1);
    }
    
    public void setCallerDisplayName(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt, Session.Info paramAnonymousInfo)
    {
      RemoteConnectionService.this.findConnectionForAction(paramAnonymousString1, "setCallerDisplayName").setCallerDisplayName(paramAnonymousString2, paramAnonymousInt);
    }
    
    public void setConferenceMergeFailed(String paramAnonymousString, Session.Info paramAnonymousInfo) {}
    
    public final void setConferenceableConnections(String paramAnonymousString, List<String> paramAnonymousList, Session.Info paramAnonymousInfo)
    {
      paramAnonymousInfo = new ArrayList();
      paramAnonymousList = paramAnonymousList.iterator();
      while (paramAnonymousList.hasNext())
      {
        String str = (String)paramAnonymousList.next();
        if (mConnectionById.containsKey(str)) {
          paramAnonymousInfo.add((RemoteConnection)mConnectionById.get(str));
        }
      }
      if (RemoteConnectionService.this.hasConnection(paramAnonymousString)) {
        RemoteConnectionService.this.findConnectionForAction(paramAnonymousString, "setConferenceableConnections").setConferenceableConnections(paramAnonymousInfo);
      } else {
        RemoteConnectionService.this.findConferenceForAction(paramAnonymousString, "setConferenceableConnections").setConferenceableConnections(paramAnonymousInfo);
      }
    }
    
    public void setConnectionCapabilities(String paramAnonymousString, int paramAnonymousInt, Session.Info paramAnonymousInfo)
    {
      if (mConnectionById.containsKey(paramAnonymousString)) {
        RemoteConnectionService.this.findConnectionForAction(paramAnonymousString, "setConnectionCapabilities").setConnectionCapabilities(paramAnonymousInt);
      } else {
        RemoteConnectionService.this.findConferenceForAction(paramAnonymousString, "setConnectionCapabilities").setConnectionCapabilities(paramAnonymousInt);
      }
    }
    
    public void setConnectionProperties(String paramAnonymousString, int paramAnonymousInt, Session.Info paramAnonymousInfo)
    {
      if (mConnectionById.containsKey(paramAnonymousString)) {
        RemoteConnectionService.this.findConnectionForAction(paramAnonymousString, "setConnectionProperties").setConnectionProperties(paramAnonymousInt);
      } else {
        RemoteConnectionService.this.findConferenceForAction(paramAnonymousString, "setConnectionProperties").setConnectionProperties(paramAnonymousInt);
      }
    }
    
    public void setDialing(String paramAnonymousString, Session.Info paramAnonymousInfo)
    {
      RemoteConnectionService.this.findConnectionForAction(paramAnonymousString, "setDialing").setState(3);
    }
    
    public void setDisconnected(String paramAnonymousString, DisconnectCause paramAnonymousDisconnectCause, Session.Info paramAnonymousInfo)
    {
      if (mConnectionById.containsKey(paramAnonymousString)) {
        RemoteConnectionService.this.findConnectionForAction(paramAnonymousString, "setDisconnected").setDisconnected(paramAnonymousDisconnectCause);
      } else {
        RemoteConnectionService.this.findConferenceForAction(paramAnonymousString, "setDisconnected").setDisconnected(paramAnonymousDisconnectCause);
      }
    }
    
    public void setIsConferenced(String paramAnonymousString1, String paramAnonymousString2, Session.Info paramAnonymousInfo)
    {
      paramAnonymousString1 = RemoteConnectionService.this.findConnectionForAction(paramAnonymousString1, "setIsConferenced");
      if (paramAnonymousString1 != RemoteConnectionService.NULL_CONNECTION) {
        if (paramAnonymousString2 == null)
        {
          if (paramAnonymousString1.getConference() != null) {
            paramAnonymousString1.getConference().removeConnection(paramAnonymousString1);
          }
        }
        else
        {
          paramAnonymousString2 = RemoteConnectionService.this.findConferenceForAction(paramAnonymousString2, "setIsConferenced");
          if (paramAnonymousString2 != RemoteConnectionService.NULL_CONFERENCE) {
            paramAnonymousString2.addConnection(paramAnonymousString1);
          }
        }
      }
    }
    
    public void setIsVoipAudioMode(String paramAnonymousString, boolean paramAnonymousBoolean, Session.Info paramAnonymousInfo)
    {
      RemoteConnectionService.this.findConnectionForAction(paramAnonymousString, "setIsVoipAudioMode").setIsVoipAudioMode(paramAnonymousBoolean);
    }
    
    public void setOnHold(String paramAnonymousString, Session.Info paramAnonymousInfo)
    {
      if (mConnectionById.containsKey(paramAnonymousString)) {
        RemoteConnectionService.this.findConnectionForAction(paramAnonymousString, "setOnHold").setState(5);
      } else {
        RemoteConnectionService.this.findConferenceForAction(paramAnonymousString, "setOnHold").setState(5);
      }
    }
    
    public void setPulling(String paramAnonymousString, Session.Info paramAnonymousInfo)
    {
      RemoteConnectionService.this.findConnectionForAction(paramAnonymousString, "setPulling").setState(7);
    }
    
    public void setRingbackRequested(String paramAnonymousString, boolean paramAnonymousBoolean, Session.Info paramAnonymousInfo)
    {
      RemoteConnectionService.this.findConnectionForAction(paramAnonymousString, "setRingbackRequested").setRingbackRequested(paramAnonymousBoolean);
    }
    
    public void setRinging(String paramAnonymousString, Session.Info paramAnonymousInfo)
    {
      RemoteConnectionService.this.findConnectionForAction(paramAnonymousString, "setRinging").setState(2);
    }
    
    public void setStatusHints(String paramAnonymousString, StatusHints paramAnonymousStatusHints, Session.Info paramAnonymousInfo)
    {
      RemoteConnectionService.this.findConnectionForAction(paramAnonymousString, "setStatusHints").setStatusHints(paramAnonymousStatusHints);
    }
    
    public void setVideoProvider(String paramAnonymousString, IVideoProvider paramAnonymousIVideoProvider, Session.Info paramAnonymousInfo)
    {
      String str = mOurConnectionServiceImpl.getApplicationContext().getOpPackageName();
      int i = mOurConnectionServiceImpl.getApplicationInfo().targetSdkVersion;
      paramAnonymousInfo = null;
      if (paramAnonymousIVideoProvider != null) {
        paramAnonymousInfo = new RemoteConnection.VideoProvider(paramAnonymousIVideoProvider, str, i);
      }
      RemoteConnectionService.this.findConnectionForAction(paramAnonymousString, "setVideoProvider").setVideoProvider(paramAnonymousInfo);
    }
    
    public void setVideoState(String paramAnonymousString, int paramAnonymousInt, Session.Info paramAnonymousInfo)
    {
      RemoteConnectionService.this.findConnectionForAction(paramAnonymousString, "setVideoState").setVideoState(paramAnonymousInt);
    }
  };
  
  RemoteConnectionService(IConnectionService paramIConnectionService, ConnectionService paramConnectionService)
    throws RemoteException
  {
    mOutgoingConnectionServiceRpc = paramIConnectionService;
    mOutgoingConnectionServiceRpc.asBinder().linkToDeath(mDeathRecipient, 0);
    mOurConnectionServiceImpl = paramConnectionService;
  }
  
  private RemoteConference findConferenceForAction(String paramString1, String paramString2)
  {
    if (mConferenceById.containsKey(paramString1)) {
      return (RemoteConference)mConferenceById.get(paramString1);
    }
    Log.w(this, "%s - Cannot find Conference %s", new Object[] { paramString2, paramString1 });
    return NULL_CONFERENCE;
  }
  
  private RemoteConnection findConnectionForAction(String paramString1, String paramString2)
  {
    if (mConnectionById.containsKey(paramString1)) {
      return (RemoteConnection)mConnectionById.get(paramString1);
    }
    Log.w(this, "%s - Cannot find Connection %s", new Object[] { paramString2, paramString1 });
    return NULL_CONNECTION;
  }
  
  private boolean hasConnection(String paramString)
  {
    return mConnectionById.containsKey(paramString);
  }
  
  private void maybeDisconnectAdapter()
  {
    if ((mConnectionById.isEmpty()) && (mConferenceById.isEmpty())) {
      try
      {
        mOutgoingConnectionServiceRpc.removeConnectionServiceAdapter(mServant.getStub(), null);
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  final RemoteConnection createRemoteConnection(PhoneAccountHandle paramPhoneAccountHandle, ConnectionRequest paramConnectionRequest, boolean paramBoolean)
  {
    String str = UUID.randomUUID().toString();
    ConnectionRequest localConnectionRequest = new ConnectionRequest.Builder().setAccountHandle(paramConnectionRequest.getAccountHandle()).setAddress(paramConnectionRequest.getAddress()).setExtras(paramConnectionRequest.getExtras()).setVideoState(paramConnectionRequest.getVideoState()).setRttPipeFromInCall(paramConnectionRequest.getRttPipeFromInCall()).setRttPipeToInCall(paramConnectionRequest.getRttPipeToInCall()).build();
    try
    {
      if (mConnectionById.isEmpty()) {
        mOutgoingConnectionServiceRpc.addConnectionServiceAdapter(mServant.getStub(), null);
      }
      paramConnectionRequest = new android/telecom/RemoteConnection;
      paramConnectionRequest.<init>(str, mOutgoingConnectionServiceRpc, localConnectionRequest);
      mPendingConnections.add(paramConnectionRequest);
      mConnectionById.put(str, paramConnectionRequest);
      mOutgoingConnectionServiceRpc.createConnection(paramPhoneAccountHandle, str, localConnectionRequest, paramBoolean, false, null);
      paramPhoneAccountHandle = new android/telecom/RemoteConnectionService$3;
      paramPhoneAccountHandle.<init>(this, str);
      paramConnectionRequest.registerCallback(paramPhoneAccountHandle);
      return paramConnectionRequest;
    }
    catch (RemoteException paramPhoneAccountHandle) {}
    return RemoteConnection.failure(new DisconnectCause(1, paramPhoneAccountHandle.toString()));
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[RemoteCS - ");
    localStringBuilder.append(mOutgoingConnectionServiceRpc.asBinder().toString());
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
}
