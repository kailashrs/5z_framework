package android.telecom;

import android.annotation.SystemApi;
import android.os.Bundle;
import android.util.ArraySet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class Conference
  extends Conferenceable
{
  public static final long CONNECT_TIME_NOT_SPECIFIED = 0L;
  private CallAudioState mCallAudioState;
  private final List<Connection> mChildConnections = new CopyOnWriteArrayList();
  private final List<Connection> mConferenceableConnections = new ArrayList();
  private long mConnectTimeMillis = 0L;
  private int mConnectionCapabilities;
  private final Connection.Listener mConnectionDeathListener = new Connection.Listener()
  {
    public void onDestroyed(Connection paramAnonymousConnection)
    {
      if (mConferenceableConnections.remove(paramAnonymousConnection)) {
        Conference.this.fireOnConferenceableConnectionsChanged();
      }
    }
  };
  private int mConnectionProperties;
  private long mConnectionStartElapsedRealTime = 0L;
  private DisconnectCause mDisconnectCause;
  private String mDisconnectMessage;
  private Bundle mExtras;
  private final Object mExtrasLock = new Object();
  private final Set<Listener> mListeners = new CopyOnWriteArraySet();
  private PhoneAccountHandle mPhoneAccount;
  private Set<String> mPreviousExtraKeys;
  private int mState = 1;
  private StatusHints mStatusHints;
  private String mTelecomCallId;
  private final List<Connection> mUnmodifiableChildConnections = Collections.unmodifiableList(mChildConnections);
  private final List<Connection> mUnmodifiableConferenceableConnections = Collections.unmodifiableList(mConferenceableConnections);
  
  public Conference(PhoneAccountHandle paramPhoneAccountHandle)
  {
    mPhoneAccount = paramPhoneAccountHandle;
  }
  
  public static boolean can(int paramInt1, int paramInt2)
  {
    boolean bool;
    if ((paramInt1 & paramInt2) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private final void clearConferenceableList()
  {
    Iterator localIterator = mConferenceableConnections.iterator();
    while (localIterator.hasNext()) {
      ((Connection)localIterator.next()).removeConnectionListener(mConnectionDeathListener);
    }
    mConferenceableConnections.clear();
  }
  
  private final void fireOnConferenceableConnectionsChanged()
  {
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onConferenceableConnectionsChanged(this, getConferenceableConnections());
    }
  }
  
  private void setState(int paramInt)
  {
    if ((paramInt != 4) && (paramInt != 5) && (paramInt != 6))
    {
      Log.w(this, "Unsupported state transition for Conference call.", new Object[] { Connection.stateToString(paramInt) });
      return;
    }
    if (mState != paramInt)
    {
      int i = mState;
      mState = paramInt;
      Iterator localIterator = mListeners.iterator();
      while (localIterator.hasNext()) {
        ((Listener)localIterator.next()).onStateChanged(this, i, paramInt);
      }
    }
  }
  
  public void addCapability(int paramInt)
  {
    setConnectionCapabilities(mConnectionCapabilities | paramInt);
  }
  
  public final boolean addConnection(Connection paramConnection)
  {
    Log.d(this, "Connection=%s, connection=", new Object[] { paramConnection });
    if ((paramConnection != null) && (!mChildConnections.contains(paramConnection)) && (paramConnection.setConference(this)))
    {
      mChildConnections.add(paramConnection);
      onConnectionAdded(paramConnection);
      Iterator localIterator = mListeners.iterator();
      while (localIterator.hasNext()) {
        ((Listener)localIterator.next()).onConnectionAdded(this, paramConnection);
      }
      return true;
    }
    return false;
  }
  
  public final Conference addListener(Listener paramListener)
  {
    mListeners.add(paramListener);
    return this;
  }
  
  public boolean can(int paramInt)
  {
    return can(mConnectionCapabilities, paramInt);
  }
  
  public final void destroy()
  {
    Log.d(this, "destroying conference : %s", new Object[] { this });
    Iterator localIterator = mChildConnections.iterator();
    while (localIterator.hasNext())
    {
      Connection localConnection = (Connection)localIterator.next();
      Log.d(this, "removing connection %s", new Object[] { localConnection });
      removeConnection(localConnection);
    }
    if (mState != 6)
    {
      Log.d(this, "setting to disconnected", new Object[0]);
      setDisconnected(new DisconnectCause(2));
    }
    localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onDestroyed(this);
    }
  }
  
  @SystemApi
  @Deprecated
  public final AudioState getAudioState()
  {
    return new AudioState(mCallAudioState);
  }
  
  public final CallAudioState getCallAudioState()
  {
    return mCallAudioState;
  }
  
  public final List<Connection> getConferenceableConnections()
  {
    return mUnmodifiableConferenceableConnections;
  }
  
  @SystemApi
  @Deprecated
  public final long getConnectTimeMillis()
  {
    return getConnectionTime();
  }
  
  public final int getConnectionCapabilities()
  {
    return mConnectionCapabilities;
  }
  
  public final int getConnectionProperties()
  {
    return mConnectionProperties;
  }
  
  public final long getConnectionStartElapsedRealTime()
  {
    return mConnectionStartElapsedRealTime;
  }
  
  public final long getConnectionTime()
  {
    return mConnectTimeMillis;
  }
  
  public final List<Connection> getConnections()
  {
    return mUnmodifiableChildConnections;
  }
  
  public final DisconnectCause getDisconnectCause()
  {
    return mDisconnectCause;
  }
  
  public final Bundle getExtras()
  {
    return mExtras;
  }
  
  public final PhoneAccountHandle getPhoneAccountHandle()
  {
    return mPhoneAccount;
  }
  
  @SystemApi
  public Connection getPrimaryConnection()
  {
    if ((mUnmodifiableChildConnections != null) && (!mUnmodifiableChildConnections.isEmpty())) {
      return (Connection)mUnmodifiableChildConnections.get(0);
    }
    return null;
  }
  
  public final int getState()
  {
    return mState;
  }
  
  public final StatusHints getStatusHints()
  {
    return mStatusHints;
  }
  
  public final String getTelecomCallId()
  {
    return mTelecomCallId;
  }
  
  public Connection.VideoProvider getVideoProvider()
  {
    return null;
  }
  
  public int getVideoState()
  {
    return 0;
  }
  
  final void handleExtrasChanged(Bundle paramBundle)
  {
    Object localObject1 = null;
    synchronized (mExtrasLock)
    {
      mExtras = paramBundle;
      paramBundle = localObject1;
      if (mExtras != null)
      {
        paramBundle = new android/os/Bundle;
        paramBundle.<init>(mExtras);
      }
      onExtrasChanged(paramBundle);
      return;
    }
  }
  
  public void onAddParticipant(String paramString) {}
  
  @SystemApi
  @Deprecated
  public void onAudioStateChanged(AudioState paramAudioState) {}
  
  public void onCallAudioStateChanged(CallAudioState paramCallAudioState) {}
  
  public void onConnectionAdded(Connection paramConnection) {}
  
  public void onDisconnect() {}
  
  public void onExtrasChanged(Bundle paramBundle) {}
  
  public void onHold() {}
  
  public void onMerge() {}
  
  public void onMerge(Connection paramConnection) {}
  
  public void onPlayDtmfTone(char paramChar) {}
  
  public void onSeparate(Connection paramConnection) {}
  
  public void onStopDtmfTone() {}
  
  public void onSwap() {}
  
  public void onUnhold() {}
  
  public final void putExtra(String paramString, int paramInt)
  {
    Bundle localBundle = new Bundle();
    localBundle.putInt(paramString, paramInt);
    putExtras(localBundle);
  }
  
  public final void putExtra(String paramString1, String paramString2)
  {
    Bundle localBundle = new Bundle();
    localBundle.putString(paramString1, paramString2);
    putExtras(localBundle);
  }
  
  public final void putExtra(String paramString, boolean paramBoolean)
  {
    Bundle localBundle = new Bundle();
    localBundle.putBoolean(paramString, paramBoolean);
    putExtras(localBundle);
  }
  
  public final void putExtras(Bundle paramBundle)
  {
    if (paramBundle == null) {
      return;
    }
    synchronized (mExtrasLock)
    {
      if (mExtras == null)
      {
        Bundle localBundle = new android/os/Bundle;
        localBundle.<init>();
        mExtras = localBundle;
      }
      mExtras.putAll(paramBundle);
      paramBundle = new android/os/Bundle;
      paramBundle.<init>(mExtras);
      ??? = mListeners.iterator();
      while (((Iterator)???).hasNext()) {
        ((Listener)((Iterator)???).next()).onExtrasChanged(this, new Bundle(paramBundle));
      }
      return;
    }
  }
  
  public void removeCapability(int paramInt)
  {
    setConnectionCapabilities(mConnectionCapabilities & paramInt);
  }
  
  public final void removeConnection(Connection paramConnection)
  {
    Log.d(this, "removing %s from %s", new Object[] { paramConnection, mChildConnections });
    if ((paramConnection != null) && (mChildConnections.remove(paramConnection)))
    {
      paramConnection.resetConference();
      Iterator localIterator = mListeners.iterator();
      while (localIterator.hasNext()) {
        ((Listener)localIterator.next()).onConnectionRemoved(this, paramConnection);
      }
    }
  }
  
  public final void removeExtras(List<String> paramList)
  {
    if ((paramList != null) && (!paramList.isEmpty())) {
      synchronized (mExtrasLock)
      {
        if (mExtras != null)
        {
          Iterator localIterator = paramList.iterator();
          while (localIterator.hasNext())
          {
            String str = (String)localIterator.next();
            mExtras.remove(str);
          }
        }
        ??? = Collections.unmodifiableList(paramList);
        paramList = mListeners.iterator();
        while (paramList.hasNext()) {
          ((Listener)paramList.next()).onExtrasRemoved(this, (List)???);
        }
        return;
      }
    }
  }
  
  public final void removeExtras(String... paramVarArgs)
  {
    removeExtras(Arrays.asList(paramVarArgs));
  }
  
  public final Conference removeListener(Listener paramListener)
  {
    mListeners.remove(paramListener);
    return this;
  }
  
  public final void setActive()
  {
    setState(4);
  }
  
  final void setCallAudioState(CallAudioState paramCallAudioState)
  {
    Log.d(this, "setCallAudioState %s", new Object[] { paramCallAudioState });
    mCallAudioState = paramCallAudioState;
    onAudioStateChanged(getAudioState());
    onCallAudioStateChanged(paramCallAudioState);
  }
  
  public final void setConferenceableConnections(List<Connection> paramList)
  {
    clearConferenceableList();
    paramList = paramList.iterator();
    while (paramList.hasNext())
    {
      Connection localConnection = (Connection)paramList.next();
      if (!mConferenceableConnections.contains(localConnection))
      {
        localConnection.addConnectionListener(mConnectionDeathListener);
        mConferenceableConnections.add(localConnection);
      }
    }
    fireOnConferenceableConnectionsChanged();
  }
  
  @SystemApi
  @Deprecated
  public final void setConnectTimeMillis(long paramLong)
  {
    setConnectionTime(paramLong);
  }
  
  public final void setConnectionCapabilities(int paramInt)
  {
    if (paramInt != mConnectionCapabilities)
    {
      mConnectionCapabilities = paramInt;
      Iterator localIterator = mListeners.iterator();
      while (localIterator.hasNext()) {
        ((Listener)localIterator.next()).onConnectionCapabilitiesChanged(this, mConnectionCapabilities);
      }
    }
  }
  
  public final void setConnectionProperties(int paramInt)
  {
    if (paramInt != mConnectionProperties)
    {
      mConnectionProperties = paramInt;
      Iterator localIterator = mListeners.iterator();
      while (localIterator.hasNext()) {
        ((Listener)localIterator.next()).onConnectionPropertiesChanged(this, mConnectionProperties);
      }
    }
  }
  
  public final void setConnectionStartElapsedRealTime(long paramLong)
  {
    mConnectionStartElapsedRealTime = paramLong;
  }
  
  public final void setConnectionTime(long paramLong)
  {
    mConnectTimeMillis = paramLong;
  }
  
  public final void setDialing()
  {
    setState(3);
  }
  
  public final void setDisconnected(DisconnectCause paramDisconnectCause)
  {
    mDisconnectCause = paramDisconnectCause;
    setState(6);
    paramDisconnectCause = mListeners.iterator();
    while (paramDisconnectCause.hasNext()) {
      ((Listener)paramDisconnectCause.next()).onDisconnected(this, mDisconnectCause);
    }
  }
  
  public final void setExtras(Bundle paramBundle)
  {
    synchronized (mExtrasLock)
    {
      putExtras(paramBundle);
      Object localObject2;
      if (mPreviousExtraKeys != null)
      {
        localObject2 = new java/util/ArrayList;
        ((ArrayList)localObject2).<init>();
        Iterator localIterator = mPreviousExtraKeys.iterator();
        while (localIterator.hasNext())
        {
          String str = (String)localIterator.next();
          if ((paramBundle == null) || (!paramBundle.containsKey(str))) {
            ((List)localObject2).add(str);
          }
        }
        if (!((List)localObject2).isEmpty()) {
          removeExtras((List)localObject2);
        }
      }
      if (mPreviousExtraKeys == null)
      {
        localObject2 = new android/util/ArraySet;
        ((ArraySet)localObject2).<init>();
        mPreviousExtraKeys = ((Set)localObject2);
      }
      mPreviousExtraKeys.clear();
      if (paramBundle != null) {
        mPreviousExtraKeys.addAll(paramBundle.keySet());
      }
      return;
    }
  }
  
  public final void setOnHold()
  {
    setState(5);
  }
  
  public final void setStatusHints(StatusHints paramStatusHints)
  {
    mStatusHints = paramStatusHints;
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onStatusHintsChanged(this, paramStatusHints);
    }
  }
  
  public final void setTelecomCallId(String paramString)
  {
    mTelecomCallId = paramString;
  }
  
  public final void setVideoProvider(Connection paramConnection, Connection.VideoProvider paramVideoProvider)
  {
    Log.d(this, "setVideoProvider Conference: %s Connection: %s VideoState: %s", new Object[] { this, paramConnection, paramVideoProvider });
    paramConnection = mListeners.iterator();
    while (paramConnection.hasNext()) {
      ((Listener)paramConnection.next()).onVideoProviderChanged(this, paramVideoProvider);
    }
  }
  
  public final void setVideoState(Connection paramConnection, int paramInt)
  {
    Log.d(this, "setVideoState Conference: %s Connection: %s VideoState: %s", new Object[] { this, paramConnection, Integer.valueOf(paramInt) });
    paramConnection = mListeners.iterator();
    while (paramConnection.hasNext()) {
      ((Listener)paramConnection.next()).onVideoStateChanged(this, paramInt);
    }
  }
  
  public String toString()
  {
    return String.format(Locale.US, "[State: %s,Capabilites: %s, VideoState: %s, VideoProvider: %s, ThisObject %s]", new Object[] { Connection.stateToString(mState), Call.Details.capabilitiesToString(mConnectionCapabilities), Integer.valueOf(getVideoState()), getVideoProvider(), super.toString() });
  }
  
  public static abstract class Listener
  {
    public Listener() {}
    
    public void onConferenceableConnectionsChanged(Conference paramConference, List<Connection> paramList) {}
    
    public void onConnectionAdded(Conference paramConference, Connection paramConnection) {}
    
    public void onConnectionCapabilitiesChanged(Conference paramConference, int paramInt) {}
    
    public void onConnectionPropertiesChanged(Conference paramConference, int paramInt) {}
    
    public void onConnectionRemoved(Conference paramConference, Connection paramConnection) {}
    
    public void onDestroyed(Conference paramConference) {}
    
    public void onDisconnected(Conference paramConference, DisconnectCause paramDisconnectCause) {}
    
    public void onExtrasChanged(Conference paramConference, Bundle paramBundle) {}
    
    public void onExtrasRemoved(Conference paramConference, List<String> paramList) {}
    
    public void onStateChanged(Conference paramConference, int paramInt1, int paramInt2) {}
    
    public void onStatusHintsChanged(Conference paramConference, StatusHints paramStatusHints) {}
    
    public void onVideoProviderChanged(Conference paramConference, Connection.VideoProvider paramVideoProvider) {}
    
    public void onVideoStateChanged(Conference paramConference, int paramInt) {}
  }
}
