package android.telecom;

import android.annotation.SystemApi;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import com.android.internal.telecom.IConnectionService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public final class RemoteConference
{
  private final Set<CallbackRecord<Callback>> mCallbackRecords = new CopyOnWriteArraySet();
  private final List<RemoteConnection> mChildConnections = new CopyOnWriteArrayList();
  private final List<RemoteConnection> mConferenceableConnections = new ArrayList();
  private int mConnectionCapabilities;
  private int mConnectionProperties;
  private final IConnectionService mConnectionService;
  private DisconnectCause mDisconnectCause;
  private Bundle mExtras;
  private final String mId;
  private int mState = 1;
  private final List<RemoteConnection> mUnmodifiableChildConnections = Collections.unmodifiableList(mChildConnections);
  private final List<RemoteConnection> mUnmodifiableConferenceableConnections = Collections.unmodifiableList(mConferenceableConnections);
  
  RemoteConference(String paramString, IConnectionService paramIConnectionService)
  {
    mId = paramString;
    mConnectionService = paramIConnectionService;
  }
  
  private void notifyExtrasChanged()
  {
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      final Callback localCallback = (Callback)localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new Runnable()
      {
        public void run()
        {
          localCallback.onExtrasChanged(jdField_this, mExtras);
        }
      });
    }
  }
  
  void addConnection(final RemoteConnection paramRemoteConnection)
  {
    if (!mChildConnections.contains(paramRemoteConnection))
    {
      mChildConnections.add(paramRemoteConnection);
      paramRemoteConnection.setConference(this);
      Iterator localIterator = mCallbackRecords.iterator();
      while (localIterator.hasNext())
      {
        CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
        final Callback localCallback = (Callback)localCallbackRecord.getCallback();
        localCallbackRecord.getHandler().post(new Runnable()
        {
          public void run()
          {
            localCallback.onConnectionAdded(jdField_this, paramRemoteConnection);
          }
        });
      }
    }
  }
  
  public void disconnect()
  {
    try
    {
      mConnectionService.disconnect(mId, null);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public List<RemoteConnection> getConferenceableConnections()
  {
    return mUnmodifiableConferenceableConnections;
  }
  
  public final int getConnectionCapabilities()
  {
    return mConnectionCapabilities;
  }
  
  public final int getConnectionProperties()
  {
    return mConnectionProperties;
  }
  
  public final List<RemoteConnection> getConnections()
  {
    return mUnmodifiableChildConnections;
  }
  
  public DisconnectCause getDisconnectCause()
  {
    return mDisconnectCause;
  }
  
  public final Bundle getExtras()
  {
    return mExtras;
  }
  
  String getId()
  {
    return mId;
  }
  
  public final int getState()
  {
    return mState;
  }
  
  public void hold()
  {
    try
    {
      mConnectionService.hold(mId, null);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void merge()
  {
    try
    {
      mConnectionService.mergeConference(mId, null);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void playDtmfTone(char paramChar)
  {
    try
    {
      mConnectionService.playDtmfTone(mId, paramChar, null);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  void putExtras(Bundle paramBundle)
  {
    if (paramBundle == null) {
      return;
    }
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putAll(paramBundle);
    notifyExtrasChanged();
  }
  
  public final void registerCallback(Callback paramCallback)
  {
    registerCallback(paramCallback, new Handler());
  }
  
  public final void registerCallback(Callback paramCallback, Handler paramHandler)
  {
    unregisterCallback(paramCallback);
    if ((paramCallback != null) && (paramHandler != null)) {
      mCallbackRecords.add(new CallbackRecord(paramCallback, paramHandler));
    }
  }
  
  void removeConnection(final RemoteConnection paramRemoteConnection)
  {
    if (mChildConnections.contains(paramRemoteConnection))
    {
      mChildConnections.remove(paramRemoteConnection);
      paramRemoteConnection.setConference(null);
      Iterator localIterator = mCallbackRecords.iterator();
      while (localIterator.hasNext())
      {
        CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
        final Callback localCallback = (Callback)localCallbackRecord.getCallback();
        localCallbackRecord.getHandler().post(new Runnable()
        {
          public void run()
          {
            localCallback.onConnectionRemoved(jdField_this, paramRemoteConnection);
          }
        });
      }
    }
  }
  
  void removeExtras(List<String> paramList)
  {
    if ((mExtras != null) && (paramList != null) && (!paramList.isEmpty()))
    {
      paramList = paramList.iterator();
      while (paramList.hasNext())
      {
        String str = (String)paramList.next();
        mExtras.remove(str);
      }
      notifyExtrasChanged();
      return;
    }
  }
  
  public void separate(RemoteConnection paramRemoteConnection)
  {
    if (mChildConnections.contains(paramRemoteConnection)) {
      try
      {
        mConnectionService.splitFromConference(paramRemoteConnection.getId(), null);
      }
      catch (RemoteException paramRemoteConnection) {}
    }
  }
  
  @SystemApi
  @Deprecated
  public void setAudioState(AudioState paramAudioState)
  {
    setCallAudioState(new CallAudioState(paramAudioState));
  }
  
  public void setCallAudioState(CallAudioState paramCallAudioState)
  {
    try
    {
      mConnectionService.onCallAudioStateChanged(mId, paramCallAudioState, null);
    }
    catch (RemoteException paramCallAudioState) {}
  }
  
  void setConferenceableConnections(final List<RemoteConnection> paramList)
  {
    mConferenceableConnections.clear();
    mConferenceableConnections.addAll(paramList);
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      paramList = (Callback)localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new Runnable()
      {
        public void run()
        {
          paramList.onConferenceableConnectionsChanged(jdField_this, mUnmodifiableConferenceableConnections);
        }
      });
    }
  }
  
  void setConnectionCapabilities(int paramInt)
  {
    if (mConnectionCapabilities != paramInt)
    {
      mConnectionCapabilities = paramInt;
      Iterator localIterator = mCallbackRecords.iterator();
      while (localIterator.hasNext())
      {
        CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
        final Callback localCallback = (Callback)localCallbackRecord.getCallback();
        localCallbackRecord.getHandler().post(new Runnable()
        {
          public void run()
          {
            localCallback.onConnectionCapabilitiesChanged(jdField_this, mConnectionCapabilities);
          }
        });
      }
    }
  }
  
  void setConnectionProperties(int paramInt)
  {
    if (mConnectionProperties != paramInt)
    {
      mConnectionProperties = paramInt;
      Iterator localIterator = mCallbackRecords.iterator();
      while (localIterator.hasNext())
      {
        CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
        final Callback localCallback = (Callback)localCallbackRecord.getCallback();
        localCallbackRecord.getHandler().post(new Runnable()
        {
          public void run()
          {
            localCallback.onConnectionPropertiesChanged(jdField_this, mConnectionProperties);
          }
        });
      }
    }
  }
  
  void setDestroyed()
  {
    Object localObject = mChildConnections.iterator();
    while (((Iterator)localObject).hasNext()) {
      ((RemoteConnection)((Iterator)localObject).next()).setConference(null);
    }
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      localObject = (Callback)localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new Runnable()
      {
        public void run()
        {
          val$callback.onDestroyed(jdField_this);
        }
      });
    }
  }
  
  void setDisconnected(final DisconnectCause paramDisconnectCause)
  {
    if (mState != 6)
    {
      mDisconnectCause = paramDisconnectCause;
      setState(6);
      Iterator localIterator = mCallbackRecords.iterator();
      while (localIterator.hasNext())
      {
        CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
        final Callback localCallback = (Callback)localCallbackRecord.getCallback();
        localCallbackRecord.getHandler().post(new Runnable()
        {
          public void run()
          {
            localCallback.onDisconnected(jdField_this, paramDisconnectCause);
          }
        });
      }
    }
  }
  
  void setState(final int paramInt)
  {
    if ((paramInt != 4) && (paramInt != 5) && (paramInt != 6))
    {
      Log.w(this, "Unsupported state transition for Conference call.", new Object[] { Connection.stateToString(paramInt) });
      return;
    }
    if (mState != paramInt)
    {
      final int i = mState;
      mState = paramInt;
      Iterator localIterator = mCallbackRecords.iterator();
      while (localIterator.hasNext())
      {
        CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
        final Callback localCallback = (Callback)localCallbackRecord.getCallback();
        localCallbackRecord.getHandler().post(new Runnable()
        {
          public void run()
          {
            localCallback.onStateChanged(jdField_this, i, paramInt);
          }
        });
      }
    }
  }
  
  public void stopDtmfTone()
  {
    try
    {
      mConnectionService.stopDtmfTone(mId, null);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void swap()
  {
    try
    {
      mConnectionService.swapConference(mId, null);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void unhold()
  {
    try
    {
      mConnectionService.unhold(mId, null);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public final void unregisterCallback(Callback paramCallback)
  {
    if (paramCallback != null)
    {
      Iterator localIterator = mCallbackRecords.iterator();
      while (localIterator.hasNext())
      {
        CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
        if (localCallbackRecord.getCallback() == paramCallback)
        {
          mCallbackRecords.remove(localCallbackRecord);
          break;
        }
      }
    }
  }
  
  public static abstract class Callback
  {
    public Callback() {}
    
    public void onConferenceableConnectionsChanged(RemoteConference paramRemoteConference, List<RemoteConnection> paramList) {}
    
    public void onConnectionAdded(RemoteConference paramRemoteConference, RemoteConnection paramRemoteConnection) {}
    
    public void onConnectionCapabilitiesChanged(RemoteConference paramRemoteConference, int paramInt) {}
    
    public void onConnectionPropertiesChanged(RemoteConference paramRemoteConference, int paramInt) {}
    
    public void onConnectionRemoved(RemoteConference paramRemoteConference, RemoteConnection paramRemoteConnection) {}
    
    public void onDestroyed(RemoteConference paramRemoteConference) {}
    
    public void onDisconnected(RemoteConference paramRemoteConference, DisconnectCause paramDisconnectCause) {}
    
    public void onExtrasChanged(RemoteConference paramRemoteConference, Bundle paramBundle) {}
    
    public void onStateChanged(RemoteConference paramRemoteConference, int paramInt1, int paramInt2) {}
  }
}
