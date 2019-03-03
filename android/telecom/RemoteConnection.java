package android.telecom;

import android.annotation.SystemApi;
import android.net.Uri;
import android.os.BadParcelableException;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.Surface;
import com.android.internal.telecom.IConnectionService;
import com.android.internal.telecom.IVideoCallback;
import com.android.internal.telecom.IVideoProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class RemoteConnection
{
  private Uri mAddress;
  private int mAddressPresentation;
  private final Set<CallbackRecord> mCallbackRecords = Collections.newSetFromMap(new ConcurrentHashMap(8, 0.9F, 1));
  private String mCallerDisplayName;
  private int mCallerDisplayNamePresentation;
  private RemoteConference mConference;
  private final List<RemoteConnection> mConferenceableConnections = new ArrayList();
  private boolean mConnected;
  private int mConnectionCapabilities;
  private final String mConnectionId;
  private int mConnectionProperties;
  private IConnectionService mConnectionService;
  private DisconnectCause mDisconnectCause;
  private Bundle mExtras;
  private boolean mIsVoipAudioMode;
  private boolean mRingbackRequested;
  private int mState = 1;
  private StatusHints mStatusHints;
  private final List<RemoteConnection> mUnmodifiableconferenceableConnections = Collections.unmodifiableList(mConferenceableConnections);
  private VideoProvider mVideoProvider;
  private int mVideoState;
  
  RemoteConnection(DisconnectCause paramDisconnectCause)
  {
    mConnectionId = "NULL";
    mConnected = false;
    mState = 6;
    mDisconnectCause = paramDisconnectCause;
  }
  
  RemoteConnection(String paramString, IConnectionService paramIConnectionService, ConnectionRequest paramConnectionRequest)
  {
    mConnectionId = paramString;
    mConnectionService = paramIConnectionService;
    mConnected = true;
    mState = 0;
  }
  
  RemoteConnection(String paramString1, IConnectionService paramIConnectionService, ParcelableConnection paramParcelableConnection, String paramString2, int paramInt)
  {
    mConnectionId = paramString1;
    mConnectionService = paramIConnectionService;
    mConnected = true;
    mState = paramParcelableConnection.getState();
    mDisconnectCause = paramParcelableConnection.getDisconnectCause();
    mRingbackRequested = paramParcelableConnection.isRingbackRequested();
    mConnectionCapabilities = paramParcelableConnection.getConnectionCapabilities();
    mConnectionProperties = paramParcelableConnection.getConnectionProperties();
    mVideoState = paramParcelableConnection.getVideoState();
    paramIConnectionService = paramParcelableConnection.getVideoProvider();
    if (paramIConnectionService != null) {
      mVideoProvider = new VideoProvider(paramIConnectionService, paramString2, paramInt);
    } else {
      mVideoProvider = null;
    }
    mIsVoipAudioMode = paramParcelableConnection.getIsVoipAudioMode();
    mStatusHints = paramParcelableConnection.getStatusHints();
    mAddress = paramParcelableConnection.getHandle();
    mAddressPresentation = paramParcelableConnection.getHandlePresentation();
    mCallerDisplayName = paramParcelableConnection.getCallerDisplayName();
    mCallerDisplayNamePresentation = paramParcelableConnection.getCallerDisplayNamePresentation();
    mConference = null;
    putExtras(paramParcelableConnection.getExtras());
    paramIConnectionService = new Bundle();
    paramIConnectionService.putString("android.telecom.extra.ORIGINAL_CONNECTION_ID", paramString1);
    putExtras(paramIConnectionService);
  }
  
  public static RemoteConnection failure(DisconnectCause paramDisconnectCause)
  {
    return new RemoteConnection(paramDisconnectCause);
  }
  
  private void notifyExtrasChanged()
  {
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      final Callback localCallback = localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new Runnable()
      {
        public void run()
        {
          localCallback.onExtrasChanged(jdField_this, mExtras);
        }
      });
    }
  }
  
  public void abort()
  {
    try
    {
      if (mConnected) {
        mConnectionService.abort(mConnectionId, null);
      }
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void answer()
  {
    try
    {
      if (mConnected) {
        mConnectionService.answer(mConnectionId, null);
      }
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void answer(int paramInt)
  {
    try
    {
      if (mConnected) {
        mConnectionService.answerVideo(mConnectionId, paramInt, null);
      }
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void disconnect()
  {
    try
    {
      if (mConnected) {
        mConnectionService.disconnect(mConnectionId, null);
      }
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public Uri getAddress()
  {
    return mAddress;
  }
  
  public int getAddressPresentation()
  {
    return mAddressPresentation;
  }
  
  public CharSequence getCallerDisplayName()
  {
    return mCallerDisplayName;
  }
  
  public int getCallerDisplayNamePresentation()
  {
    return mCallerDisplayNamePresentation;
  }
  
  public RemoteConference getConference()
  {
    return mConference;
  }
  
  public List<RemoteConnection> getConferenceableConnections()
  {
    return mUnmodifiableconferenceableConnections;
  }
  
  public int getConnectionCapabilities()
  {
    return mConnectionCapabilities;
  }
  
  public int getConnectionProperties()
  {
    return mConnectionProperties;
  }
  
  IConnectionService getConnectionService()
  {
    return mConnectionService;
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
    return mConnectionId;
  }
  
  public int getState()
  {
    return mState;
  }
  
  public StatusHints getStatusHints()
  {
    return mStatusHints;
  }
  
  public final VideoProvider getVideoProvider()
  {
    return mVideoProvider;
  }
  
  public int getVideoState()
  {
    return mVideoState;
  }
  
  public void hold()
  {
    try
    {
      if (mConnected) {
        mConnectionService.hold(mConnectionId, null);
      }
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public boolean isRingbackRequested()
  {
    return mRingbackRequested;
  }
  
  public boolean isVoipAudioMode()
  {
    return mIsVoipAudioMode;
  }
  
  void onConnectionEvent(final String paramString, final Bundle paramBundle)
  {
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      final Callback localCallback = localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new Runnable()
      {
        public void run()
        {
          localCallback.onConnectionEvent(jdField_this, paramString, paramBundle);
        }
      });
    }
  }
  
  void onPostDialChar(char paramChar)
  {
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      final Callback localCallback = localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new Runnable()
      {
        public void run()
        {
          localCallback.onPostDialChar(jdField_this, val$nextChar);
        }
      });
    }
  }
  
  void onRemoteRttRequest()
  {
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      Callback localCallback = localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new _..Lambda.RemoteConnection.yp1cNJ53RzQGFz3RZRlC3urzQv4(localCallback, this));
    }
  }
  
  void onRttInitiationFailure(int paramInt)
  {
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      Callback localCallback = localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new _..Lambda.RemoteConnection.AwagQDJDcNDplrFif6DlYZldL5E(localCallback, this, paramInt));
    }
  }
  
  void onRttInitiationSuccess()
  {
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      Callback localCallback = localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new _..Lambda.RemoteConnection.C4t0J6QK31Ef1UFsdPVwkew1VaQ(localCallback, this));
    }
  }
  
  void onRttSessionRemotelyTerminated()
  {
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      Callback localCallback = localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new _..Lambda.RemoteConnection.mmHouQhUco_u9PRJ9qkMqlkKzAs(localCallback, this));
    }
  }
  
  public void playDtmfTone(char paramChar)
  {
    try
    {
      if (mConnected) {
        mConnectionService.playDtmfTone(mConnectionId, paramChar, null);
      }
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void postDialContinue(boolean paramBoolean)
  {
    try
    {
      if (mConnected) {
        mConnectionService.onPostDialContinue(mConnectionId, paramBoolean, null);
      }
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void pullExternalCall()
  {
    try
    {
      if (mConnected) {
        mConnectionService.pullExternalCall(mConnectionId, null);
      }
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
    try
    {
      mExtras.putAll(paramBundle);
    }
    catch (BadParcelableException localBadParcelableException)
    {
      paramBundle = new StringBuilder();
      paramBundle.append("putExtras: could not unmarshal extras; exception = ");
      paramBundle.append(localBadParcelableException);
      Log.w(this, paramBundle.toString(), new Object[0]);
    }
    notifyExtrasChanged();
  }
  
  public void registerCallback(Callback paramCallback)
  {
    registerCallback(paramCallback, new Handler());
  }
  
  public void registerCallback(Callback paramCallback, Handler paramHandler)
  {
    unregisterCallback(paramCallback);
    if ((paramCallback != null) && (paramHandler != null)) {
      mCallbackRecords.add(new CallbackRecord(paramCallback, paramHandler));
    }
  }
  
  public void reject()
  {
    try
    {
      if (mConnected) {
        mConnectionService.reject(mConnectionId, null);
      }
    }
    catch (RemoteException localRemoteException) {}
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
  
  public void sendRttUpgradeResponse(Connection.RttTextStream paramRttTextStream)
  {
    try
    {
      if (mConnected) {
        if (paramRttTextStream == null) {
          mConnectionService.respondToRttUpgradeRequest(mConnectionId, null, null, null);
        } else {
          mConnectionService.respondToRttUpgradeRequest(mConnectionId, paramRttTextStream.getFdFromInCall(), paramRttTextStream.getFdToInCall(), null);
        }
      }
    }
    catch (RemoteException paramRttTextStream) {}
  }
  
  void setAddress(final Uri paramUri, final int paramInt)
  {
    mAddress = paramUri;
    mAddressPresentation = paramInt;
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      final Callback localCallback = localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new Runnable()
      {
        public void run()
        {
          localCallback.onAddressChanged(jdField_this, paramUri, paramInt);
        }
      });
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
      if (mConnected) {
        mConnectionService.onCallAudioStateChanged(mConnectionId, paramCallAudioState, null);
      }
    }
    catch (RemoteException paramCallAudioState) {}
  }
  
  void setCallerDisplayName(final String paramString, final int paramInt)
  {
    mCallerDisplayName = paramString;
    mCallerDisplayNamePresentation = paramInt;
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      final Callback localCallback = localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new Runnable()
      {
        public void run()
        {
          localCallback.onCallerDisplayNameChanged(jdField_this, paramString, paramInt);
        }
      });
    }
  }
  
  void setConference(final RemoteConference paramRemoteConference)
  {
    if (mConference != paramRemoteConference)
    {
      mConference = paramRemoteConference;
      Iterator localIterator = mCallbackRecords.iterator();
      while (localIterator.hasNext())
      {
        CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
        final Callback localCallback = localCallbackRecord.getCallback();
        localCallbackRecord.getHandler().post(new Runnable()
        {
          public void run()
          {
            localCallback.onConferenceChanged(jdField_this, paramRemoteConference);
          }
        });
      }
    }
  }
  
  void setConferenceableConnections(final List<RemoteConnection> paramList)
  {
    mConferenceableConnections.clear();
    mConferenceableConnections.addAll(paramList);
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      paramList = localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new Runnable()
      {
        public void run()
        {
          paramList.onConferenceableConnectionsChanged(jdField_this, mUnmodifiableconferenceableConnections);
        }
      });
    }
  }
  
  void setConnectionCapabilities(final int paramInt)
  {
    mConnectionCapabilities = paramInt;
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      final Callback localCallback = localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new Runnable()
      {
        public void run()
        {
          localCallback.onConnectionCapabilitiesChanged(jdField_this, paramInt);
        }
      });
    }
  }
  
  void setConnectionProperties(final int paramInt)
  {
    mConnectionProperties = paramInt;
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      final Callback localCallback = localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new Runnable()
      {
        public void run()
        {
          localCallback.onConnectionPropertiesChanged(jdField_this, paramInt);
        }
      });
    }
  }
  
  void setDestroyed()
  {
    if (!mCallbackRecords.isEmpty())
    {
      if (mState != 6) {
        setDisconnected(new DisconnectCause(1, "Connection destroyed."));
      }
      Iterator localIterator = mCallbackRecords.iterator();
      while (localIterator.hasNext())
      {
        CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
        final Callback localCallback = localCallbackRecord.getCallback();
        localCallbackRecord.getHandler().post(new Runnable()
        {
          public void run()
          {
            localCallback.onDestroyed(jdField_this);
          }
        });
      }
      mCallbackRecords.clear();
      mConnected = false;
    }
  }
  
  void setDisconnected(final DisconnectCause paramDisconnectCause)
  {
    if (mState != 6)
    {
      mState = 6;
      mDisconnectCause = paramDisconnectCause;
      Iterator localIterator = mCallbackRecords.iterator();
      while (localIterator.hasNext())
      {
        CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
        final Callback localCallback = localCallbackRecord.getCallback();
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
  
  void setIsVoipAudioMode(final boolean paramBoolean)
  {
    mIsVoipAudioMode = paramBoolean;
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      final Callback localCallback = localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new Runnable()
      {
        public void run()
        {
          localCallback.onVoipAudioChanged(jdField_this, paramBoolean);
        }
      });
    }
  }
  
  void setPostDialWait(final String paramString)
  {
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      final Callback localCallback = localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new Runnable()
      {
        public void run()
        {
          localCallback.onPostDialWait(jdField_this, paramString);
        }
      });
    }
  }
  
  void setRingbackRequested(final boolean paramBoolean)
  {
    if (mRingbackRequested != paramBoolean)
    {
      mRingbackRequested = paramBoolean;
      Iterator localIterator = mCallbackRecords.iterator();
      while (localIterator.hasNext())
      {
        CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
        final Callback localCallback = localCallbackRecord.getCallback();
        localCallbackRecord.getHandler().post(new Runnable()
        {
          public void run()
          {
            localCallback.onRingbackRequested(jdField_this, paramBoolean);
          }
        });
      }
    }
  }
  
  void setState(final int paramInt)
  {
    if (mState != paramInt)
    {
      mState = paramInt;
      Iterator localIterator = mCallbackRecords.iterator();
      while (localIterator.hasNext())
      {
        CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
        final Callback localCallback = localCallbackRecord.getCallback();
        localCallbackRecord.getHandler().post(new Runnable()
        {
          public void run()
          {
            localCallback.onStateChanged(jdField_this, paramInt);
          }
        });
      }
    }
  }
  
  void setStatusHints(final StatusHints paramStatusHints)
  {
    mStatusHints = paramStatusHints;
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      final Callback localCallback = localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new Runnable()
      {
        public void run()
        {
          localCallback.onStatusHintsChanged(jdField_this, paramStatusHints);
        }
      });
    }
  }
  
  void setVideoProvider(final VideoProvider paramVideoProvider)
  {
    mVideoProvider = paramVideoProvider;
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      final Callback localCallback = localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new Runnable()
      {
        public void run()
        {
          localCallback.onVideoProviderChanged(jdField_this, paramVideoProvider);
        }
      });
    }
  }
  
  void setVideoState(final int paramInt)
  {
    mVideoState = paramInt;
    Iterator localIterator = mCallbackRecords.iterator();
    while (localIterator.hasNext())
    {
      CallbackRecord localCallbackRecord = (CallbackRecord)localIterator.next();
      final Callback localCallback = localCallbackRecord.getCallback();
      localCallbackRecord.getHandler().post(new Runnable()
      {
        public void run()
        {
          localCallback.onVideoStateChanged(jdField_this, paramInt);
        }
      });
    }
  }
  
  public void startRtt(Connection.RttTextStream paramRttTextStream)
  {
    try
    {
      if (mConnected) {
        mConnectionService.startRtt(mConnectionId, paramRttTextStream.getFdFromInCall(), paramRttTextStream.getFdToInCall(), null);
      }
    }
    catch (RemoteException paramRttTextStream) {}
  }
  
  public void stopDtmfTone()
  {
    try
    {
      if (mConnected) {
        mConnectionService.stopDtmfTone(mConnectionId, null);
      }
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void stopRtt()
  {
    try
    {
      if (mConnected) {
        mConnectionService.stopRtt(mConnectionId, null);
      }
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void unhold()
  {
    try
    {
      if (mConnected) {
        mConnectionService.unhold(mConnectionId, null);
      }
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void unregisterCallback(Callback paramCallback)
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
    
    public void onAddressChanged(RemoteConnection paramRemoteConnection, Uri paramUri, int paramInt) {}
    
    public void onCallerDisplayNameChanged(RemoteConnection paramRemoteConnection, String paramString, int paramInt) {}
    
    public void onConferenceChanged(RemoteConnection paramRemoteConnection, RemoteConference paramRemoteConference) {}
    
    public void onConferenceableConnectionsChanged(RemoteConnection paramRemoteConnection, List<RemoteConnection> paramList) {}
    
    public void onConnectionCapabilitiesChanged(RemoteConnection paramRemoteConnection, int paramInt) {}
    
    public void onConnectionEvent(RemoteConnection paramRemoteConnection, String paramString, Bundle paramBundle) {}
    
    public void onConnectionPropertiesChanged(RemoteConnection paramRemoteConnection, int paramInt) {}
    
    public void onDestroyed(RemoteConnection paramRemoteConnection) {}
    
    public void onDisconnected(RemoteConnection paramRemoteConnection, DisconnectCause paramDisconnectCause) {}
    
    public void onExtrasChanged(RemoteConnection paramRemoteConnection, Bundle paramBundle) {}
    
    public void onPostDialChar(RemoteConnection paramRemoteConnection, char paramChar) {}
    
    public void onPostDialWait(RemoteConnection paramRemoteConnection, String paramString) {}
    
    public void onRemoteRttRequest(RemoteConnection paramRemoteConnection) {}
    
    public void onRingbackRequested(RemoteConnection paramRemoteConnection, boolean paramBoolean) {}
    
    public void onRttInitiationFailure(RemoteConnection paramRemoteConnection, int paramInt) {}
    
    public void onRttInitiationSuccess(RemoteConnection paramRemoteConnection) {}
    
    public void onRttSessionRemotelyTerminated(RemoteConnection paramRemoteConnection) {}
    
    public void onStateChanged(RemoteConnection paramRemoteConnection, int paramInt) {}
    
    public void onStatusHintsChanged(RemoteConnection paramRemoteConnection, StatusHints paramStatusHints) {}
    
    public void onVideoProviderChanged(RemoteConnection paramRemoteConnection, RemoteConnection.VideoProvider paramVideoProvider) {}
    
    public void onVideoStateChanged(RemoteConnection paramRemoteConnection, int paramInt) {}
    
    public void onVoipAudioChanged(RemoteConnection paramRemoteConnection, boolean paramBoolean) {}
  }
  
  private static final class CallbackRecord
    extends RemoteConnection.Callback
  {
    private final RemoteConnection.Callback mCallback;
    private final Handler mHandler;
    
    public CallbackRecord(RemoteConnection.Callback paramCallback, Handler paramHandler)
    {
      mCallback = paramCallback;
      mHandler = paramHandler;
    }
    
    public RemoteConnection.Callback getCallback()
    {
      return mCallback;
    }
    
    public Handler getHandler()
    {
      return mHandler;
    }
  }
  
  public static class VideoProvider
  {
    private final Set<Callback> mCallbacks = Collections.newSetFromMap(new ConcurrentHashMap(8, 0.9F, 1));
    private final String mCallingPackage;
    private final int mTargetSdkVersion;
    private final IVideoCallback mVideoCallbackDelegate = new IVideoCallback()
    {
      public IBinder asBinder()
      {
        return null;
      }
      
      public void changeCallDataUsage(long paramAnonymousLong)
      {
        Iterator localIterator = mCallbacks.iterator();
        while (localIterator.hasNext()) {
          ((RemoteConnection.VideoProvider.Callback)localIterator.next()).onCallDataUsageChanged(RemoteConnection.VideoProvider.this, paramAnonymousLong);
        }
      }
      
      public void changeCameraCapabilities(VideoProfile.CameraCapabilities paramAnonymousCameraCapabilities)
      {
        Iterator localIterator = mCallbacks.iterator();
        while (localIterator.hasNext()) {
          ((RemoteConnection.VideoProvider.Callback)localIterator.next()).onCameraCapabilitiesChanged(RemoteConnection.VideoProvider.this, paramAnonymousCameraCapabilities);
        }
      }
      
      public void changePeerDimensions(int paramAnonymousInt1, int paramAnonymousInt2)
      {
        Iterator localIterator = mCallbacks.iterator();
        while (localIterator.hasNext()) {
          ((RemoteConnection.VideoProvider.Callback)localIterator.next()).onPeerDimensionsChanged(RemoteConnection.VideoProvider.this, paramAnonymousInt1, paramAnonymousInt2);
        }
      }
      
      public void changeVideoQuality(int paramAnonymousInt)
      {
        Iterator localIterator = mCallbacks.iterator();
        while (localIterator.hasNext()) {
          ((RemoteConnection.VideoProvider.Callback)localIterator.next()).onVideoQualityChanged(RemoteConnection.VideoProvider.this, paramAnonymousInt);
        }
      }
      
      public void handleCallSessionEvent(int paramAnonymousInt)
      {
        Iterator localIterator = mCallbacks.iterator();
        while (localIterator.hasNext()) {
          ((RemoteConnection.VideoProvider.Callback)localIterator.next()).onCallSessionEvent(RemoteConnection.VideoProvider.this, paramAnonymousInt);
        }
      }
      
      public void receiveSessionModifyRequest(VideoProfile paramAnonymousVideoProfile)
      {
        Iterator localIterator = mCallbacks.iterator();
        while (localIterator.hasNext()) {
          ((RemoteConnection.VideoProvider.Callback)localIterator.next()).onSessionModifyRequestReceived(RemoteConnection.VideoProvider.this, paramAnonymousVideoProfile);
        }
      }
      
      public void receiveSessionModifyResponse(int paramAnonymousInt, VideoProfile paramAnonymousVideoProfile1, VideoProfile paramAnonymousVideoProfile2)
      {
        Iterator localIterator = mCallbacks.iterator();
        while (localIterator.hasNext()) {
          ((RemoteConnection.VideoProvider.Callback)localIterator.next()).onSessionModifyResponseReceived(RemoteConnection.VideoProvider.this, paramAnonymousInt, paramAnonymousVideoProfile1, paramAnonymousVideoProfile2);
        }
      }
    };
    private final VideoCallbackServant mVideoCallbackServant = new VideoCallbackServant(mVideoCallbackDelegate);
    private final IVideoProvider mVideoProviderBinder;
    
    VideoProvider(IVideoProvider paramIVideoProvider, String paramString, int paramInt)
    {
      mVideoProviderBinder = paramIVideoProvider;
      mCallingPackage = paramString;
      mTargetSdkVersion = paramInt;
      try
      {
        mVideoProviderBinder.addVideoCallback(mVideoCallbackServant.getStub().asBinder());
      }
      catch (RemoteException paramIVideoProvider) {}
    }
    
    public void registerCallback(Callback paramCallback)
    {
      mCallbacks.add(paramCallback);
    }
    
    public void requestCallDataUsage()
    {
      try
      {
        mVideoProviderBinder.requestCallDataUsage();
      }
      catch (RemoteException localRemoteException) {}
    }
    
    public void requestCameraCapabilities()
    {
      try
      {
        mVideoProviderBinder.requestCameraCapabilities();
      }
      catch (RemoteException localRemoteException) {}
    }
    
    public void sendSessionModifyRequest(VideoProfile paramVideoProfile1, VideoProfile paramVideoProfile2)
    {
      try
      {
        mVideoProviderBinder.sendSessionModifyRequest(paramVideoProfile1, paramVideoProfile2);
      }
      catch (RemoteException paramVideoProfile1) {}
    }
    
    public void sendSessionModifyResponse(VideoProfile paramVideoProfile)
    {
      try
      {
        mVideoProviderBinder.sendSessionModifyResponse(paramVideoProfile);
      }
      catch (RemoteException paramVideoProfile) {}
    }
    
    public void setCamera(String paramString)
    {
      try
      {
        mVideoProviderBinder.setCamera(paramString, mCallingPackage, mTargetSdkVersion);
      }
      catch (RemoteException paramString) {}
    }
    
    public void setDeviceOrientation(int paramInt)
    {
      try
      {
        mVideoProviderBinder.setDeviceOrientation(paramInt);
      }
      catch (RemoteException localRemoteException) {}
    }
    
    public void setDisplaySurface(Surface paramSurface)
    {
      try
      {
        mVideoProviderBinder.setDisplaySurface(paramSurface);
      }
      catch (RemoteException paramSurface) {}
    }
    
    public void setPauseImage(Uri paramUri)
    {
      try
      {
        mVideoProviderBinder.setPauseImage(paramUri);
      }
      catch (RemoteException paramUri) {}
    }
    
    public void setPreviewSurface(Surface paramSurface)
    {
      try
      {
        mVideoProviderBinder.setPreviewSurface(paramSurface);
      }
      catch (RemoteException paramSurface) {}
    }
    
    public void setZoom(float paramFloat)
    {
      try
      {
        mVideoProviderBinder.setZoom(paramFloat);
      }
      catch (RemoteException localRemoteException) {}
    }
    
    public void unregisterCallback(Callback paramCallback)
    {
      mCallbacks.remove(paramCallback);
    }
    
    public static abstract class Callback
    {
      public Callback() {}
      
      public void onCallDataUsageChanged(RemoteConnection.VideoProvider paramVideoProvider, long paramLong) {}
      
      public void onCallSessionEvent(RemoteConnection.VideoProvider paramVideoProvider, int paramInt) {}
      
      public void onCameraCapabilitiesChanged(RemoteConnection.VideoProvider paramVideoProvider, VideoProfile.CameraCapabilities paramCameraCapabilities) {}
      
      public void onPeerDimensionsChanged(RemoteConnection.VideoProvider paramVideoProvider, int paramInt1, int paramInt2) {}
      
      public void onSessionModifyRequestReceived(RemoteConnection.VideoProvider paramVideoProvider, VideoProfile paramVideoProfile) {}
      
      public void onSessionModifyResponseReceived(RemoteConnection.VideoProvider paramVideoProvider, int paramInt, VideoProfile paramVideoProfile1, VideoProfile paramVideoProfile2) {}
      
      public void onVideoQualityChanged(RemoteConnection.VideoProvider paramVideoProvider, int paramInt) {}
    }
  }
}
