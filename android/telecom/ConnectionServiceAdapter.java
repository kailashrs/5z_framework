package android.telecom;

import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import com.android.internal.telecom.IConnectionServiceAdapter;
import com.android.internal.telecom.IVideoProvider;
import com.android.internal.telecom.RemoteServiceCallback;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

final class ConnectionServiceAdapter
  implements IBinder.DeathRecipient
{
  private final Set<IConnectionServiceAdapter> mAdapters = Collections.newSetFromMap(new ConcurrentHashMap(8, 0.9F, 1));
  
  ConnectionServiceAdapter() {}
  
  void addAdapter(IConnectionServiceAdapter paramIConnectionServiceAdapter)
  {
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext()) {
      if (((IConnectionServiceAdapter)localIterator.next()).asBinder() == paramIConnectionServiceAdapter.asBinder())
      {
        Log.w(this, "Ignoring duplicate adapter addition.", new Object[0]);
        return;
      }
    }
    if (mAdapters.add(paramIConnectionServiceAdapter)) {
      try
      {
        paramIConnectionServiceAdapter.asBinder().linkToDeath(this, 0);
      }
      catch (RemoteException localRemoteException)
      {
        mAdapters.remove(paramIConnectionServiceAdapter);
      }
    }
  }
  
  void addConferenceCall(String paramString, ParcelableConference paramParcelableConference)
  {
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.addConferenceCall(paramString, paramParcelableConference, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void addExistingConnection(String paramString, ParcelableConnection paramParcelableConnection)
  {
    Log.v(this, "addExistingConnection: %s", new Object[] { paramString });
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.addExistingConnection(paramString, paramParcelableConnection, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  public void binderDied()
  {
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      if (!localIConnectionServiceAdapter.asBinder().isBinderAlive())
      {
        localIterator.remove();
        localIConnectionServiceAdapter.asBinder().unlinkToDeath(this, 0);
      }
    }
  }
  
  void handleCreateConnectionComplete(String paramString, ConnectionRequest paramConnectionRequest, ParcelableConnection paramParcelableConnection)
  {
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.handleCreateConnectionComplete(paramString, paramConnectionRequest, paramParcelableConnection, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void onConferenceMergeFailed(String paramString)
  {
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        Log.d(this, "merge failed for call %s", new Object[] { paramString });
        localIConnectionServiceAdapter.setConferenceMergeFailed(paramString, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void onConnectionEvent(String paramString1, String paramString2, Bundle paramBundle)
  {
    Log.v(this, "onConnectionEvent: %s", new Object[] { paramString2 });
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.onConnectionEvent(paramString1, paramString2, paramBundle, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void onConnectionServiceFocusReleased()
  {
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        Log.d(this, "onConnectionServiceFocusReleased", new Object[0]);
        localIConnectionServiceAdapter.onConnectionServiceFocusReleased(Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void onPhoneAccountChanged(String paramString, PhoneAccountHandle paramPhoneAccountHandle)
  {
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        Log.d(this, "onPhoneAccountChanged %s", new Object[] { paramString });
        localIConnectionServiceAdapter.onPhoneAccountChanged(paramString, paramPhoneAccountHandle, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void onPostDialChar(String paramString, char paramChar)
  {
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.onPostDialChar(paramString, paramChar, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void onPostDialWait(String paramString1, String paramString2)
  {
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.onPostDialWait(paramString1, paramString2, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void onRemoteRttRequest(String paramString)
  {
    Log.v(this, "onRemoteRttRequest: %s", new Object[] { paramString });
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.onRemoteRttRequest(paramString, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void onRttInitiationFailure(String paramString, int paramInt)
  {
    Log.v(this, "onRttInitiationFailure: %s", new Object[] { paramString });
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.onRttInitiationFailure(paramString, paramInt, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void onRttInitiationSuccess(String paramString)
  {
    Log.v(this, "onRttInitiationSuccess: %s", new Object[] { paramString });
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.onRttInitiationSuccess(paramString, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void onRttSessionRemotelyTerminated(String paramString)
  {
    Log.v(this, "onRttSessionRemotelyTerminated: %s", new Object[] { paramString });
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.onRttSessionRemotelyTerminated(paramString, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void putExtra(String paramString1, String paramString2, int paramInt)
  {
    Log.v(this, "putExtra: %s %s=%d", new Object[] { paramString1, paramString2, Integer.valueOf(paramInt) });
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        Bundle localBundle = new android/os/Bundle;
        localBundle.<init>();
        localBundle.putInt(paramString2, paramInt);
        localIConnectionServiceAdapter.putExtras(paramString1, localBundle, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void putExtra(String paramString1, String paramString2, String paramString3)
  {
    Log.v(this, "putExtra: %s %s=%s", new Object[] { paramString1, paramString2, paramString3 });
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        Bundle localBundle = new android/os/Bundle;
        localBundle.<init>();
        localBundle.putString(paramString2, paramString3);
        localIConnectionServiceAdapter.putExtras(paramString1, localBundle, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void putExtra(String paramString1, String paramString2, boolean paramBoolean)
  {
    Log.v(this, "putExtra: %s %s=%b", new Object[] { paramString1, paramString2, Boolean.valueOf(paramBoolean) });
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        Bundle localBundle = new android/os/Bundle;
        localBundle.<init>();
        localBundle.putBoolean(paramString2, paramBoolean);
        localIConnectionServiceAdapter.putExtras(paramString1, localBundle, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void putExtras(String paramString, Bundle paramBundle)
  {
    Log.v(this, "putExtras: %s", new Object[] { paramString });
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.putExtras(paramString, paramBundle, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void queryRemoteConnectionServices(RemoteServiceCallback paramRemoteServiceCallback)
  {
    if (mAdapters.size() == 1) {
      try
      {
        ((IConnectionServiceAdapter)mAdapters.iterator().next()).queryRemoteConnectionServices(paramRemoteServiceCallback, Log.getExternalSession());
      }
      catch (RemoteException paramRemoteServiceCallback)
      {
        Log.e(this, paramRemoteServiceCallback, "Exception trying to query for remote CSs", new Object[0]);
      }
    }
  }
  
  void removeAdapter(IConnectionServiceAdapter paramIConnectionServiceAdapter)
  {
    if (paramIConnectionServiceAdapter != null)
    {
      Iterator localIterator = mAdapters.iterator();
      while (localIterator.hasNext())
      {
        IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
        if ((localIConnectionServiceAdapter.asBinder() == paramIConnectionServiceAdapter.asBinder()) && (mAdapters.remove(localIConnectionServiceAdapter)))
        {
          paramIConnectionServiceAdapter.asBinder().unlinkToDeath(this, 0);
          break;
        }
      }
    }
  }
  
  void removeCall(String paramString)
  {
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.removeCall(paramString, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void removeExtras(String paramString, List<String> paramList)
  {
    Log.v(this, "removeExtras: %s %s", new Object[] { paramString, paramList });
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.removeExtras(paramString, paramList, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void resetCdmaConnectionTime(String paramString)
  {
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.resetCdmaConnectionTime(paramString, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void setActive(String paramString)
  {
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.setActive(paramString, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void setAddress(String paramString, Uri paramUri, int paramInt)
  {
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.setAddress(paramString, paramUri, paramInt, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void setAudioRoute(String paramString1, int paramInt, String paramString2)
  {
    Log.v(this, "setAudioRoute: %s %s %s", new Object[] { paramString1, CallAudioState.audioRouteToString(paramInt), paramString2 });
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.setAudioRoute(paramString1, paramInt, paramString2, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void setCallerDisplayName(String paramString1, String paramString2, int paramInt)
  {
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.setCallerDisplayName(paramString1, paramString2, paramInt, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void setConferenceableConnections(String paramString, List<String> paramList)
  {
    Log.v(this, "setConferenceableConnections: %s, %s", new Object[] { paramString, paramList });
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.setConferenceableConnections(paramString, paramList, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void setConnectionCapabilities(String paramString, int paramInt)
  {
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.setConnectionCapabilities(paramString, paramInt, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void setConnectionProperties(String paramString, int paramInt)
  {
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.setConnectionProperties(paramString, paramInt, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void setDialing(String paramString)
  {
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.setDialing(paramString, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void setDisconnected(String paramString, DisconnectCause paramDisconnectCause)
  {
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.setDisconnected(paramString, paramDisconnectCause, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void setIsConferenced(String paramString1, String paramString2)
  {
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        Log.d(this, "sending connection %s with conference %s", new Object[] { paramString1, paramString2 });
        localIConnectionServiceAdapter.setIsConferenced(paramString1, paramString2, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void setIsVoipAudioMode(String paramString, boolean paramBoolean)
  {
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.setIsVoipAudioMode(paramString, paramBoolean, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void setOnHold(String paramString)
  {
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.setOnHold(paramString, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void setPulling(String paramString)
  {
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.setPulling(paramString, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void setRingbackRequested(String paramString, boolean paramBoolean)
  {
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.setRingbackRequested(paramString, paramBoolean, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void setRinging(String paramString)
  {
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.setRinging(paramString, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void setStatusHints(String paramString, StatusHints paramStatusHints)
  {
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.setStatusHints(paramString, paramStatusHints, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void setVideoProvider(String paramString, Connection.VideoProvider paramVideoProvider)
  {
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      IVideoProvider localIVideoProvider;
      if (paramVideoProvider == null) {
        localIVideoProvider = null;
      }
      try
      {
        localIVideoProvider = paramVideoProvider.getInterface();
        localIConnectionServiceAdapter.setVideoProvider(paramString, localIVideoProvider, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  void setVideoState(String paramString, int paramInt)
  {
    Log.v(this, "setVideoState: %d", new Object[] { Integer.valueOf(paramInt) });
    Iterator localIterator = mAdapters.iterator();
    while (localIterator.hasNext())
    {
      IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)localIterator.next();
      try
      {
        localIConnectionServiceAdapter.setVideoState(paramString, paramInt, Log.getExternalSession());
      }
      catch (RemoteException localRemoteException) {}
    }
  }
}
