package android.telecom;

import android.annotation.SystemApi;
import android.bluetooth.BluetoothDevice;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.ArraySet;
import android.view.Surface;
import com.android.internal.os.SomeArgs;
import com.android.internal.telecom.IVideoCallback;
import com.android.internal.telecom.IVideoCallback.Stub;
import com.android.internal.telecom.IVideoProvider;
import com.android.internal.telecom.IVideoProvider.Stub;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Connection
  extends Conferenceable
{
  public static final int CAPABILITY_ADD_PARTICIPANT = 67108864;
  public static final int CAPABILITY_CANNOT_DOWNGRADE_VIDEO_TO_AUDIO = 8388608;
  public static final int CAPABILITY_CAN_PAUSE_VIDEO = 1048576;
  public static final int CAPABILITY_CAN_PULL_CALL = 16777216;
  public static final int CAPABILITY_CAN_SEND_RESPONSE_VIA_CONNECTION = 4194304;
  public static final int CAPABILITY_CAN_UPGRADE_TO_VIDEO = 524288;
  public static final int CAPABILITY_CONFERENCE_HAS_NO_CHILDREN = 2097152;
  public static final int CAPABILITY_DISCONNECT_FROM_CONFERENCE = 8192;
  public static final int CAPABILITY_HOLD = 1;
  public static final int CAPABILITY_MANAGE_CONFERENCE = 128;
  public static final int CAPABILITY_MERGE_CONFERENCE = 4;
  public static final int CAPABILITY_MUTE = 64;
  public static final int CAPABILITY_RESPOND_VIA_TEXT = 32;
  public static final int CAPABILITY_SEPARATE_FROM_CONFERENCE = 4096;
  public static final int CAPABILITY_SPEED_UP_MT_AUDIO = 262144;
  public static final int CAPABILITY_SUPPORTS_RTT_REMOTE = 134217728;
  public static final int CAPABILITY_SUPPORTS_VT_LOCAL_BIDIRECTIONAL = 768;
  public static final int CAPABILITY_SUPPORTS_VT_LOCAL_RX = 256;
  public static final int CAPABILITY_SUPPORTS_VT_LOCAL_TX = 512;
  public static final int CAPABILITY_SUPPORTS_VT_REMOTE_BIDIRECTIONAL = 3072;
  public static final int CAPABILITY_SUPPORTS_VT_REMOTE_RX = 1024;
  public static final int CAPABILITY_SUPPORTS_VT_REMOTE_TX = 2048;
  public static final int CAPABILITY_SUPPORT_DEFLECT = 33554432;
  public static final int CAPABILITY_SUPPORT_HOLD = 2;
  public static final int CAPABILITY_SWAP_CONFERENCE = 8;
  public static final int CAPABILITY_UNUSED = 16;
  public static final int CAPABILITY_UNUSED_2 = 16384;
  public static final int CAPABILITY_UNUSED_3 = 32768;
  public static final int CAPABILITY_UNUSED_4 = 65536;
  public static final int CAPABILITY_UNUSED_5 = 131072;
  public static final String EVENT_CALL_MERGE_FAILED = "android.telecom.event.CALL_MERGE_FAILED";
  public static final String EVENT_CALL_PROPERTY_CHANGED = "android.telecom.event.EVENT_CALL_PROPERTY_CHANGED";
  public static final String EVENT_CALL_PULL_FAILED = "android.telecom.event.CALL_PULL_FAILED";
  public static final String EVENT_CALL_REMOTELY_HELD = "android.telecom.event.CALL_REMOTELY_HELD";
  public static final String EVENT_CALL_REMOTELY_UNHELD = "android.telecom.event.CALL_REMOTELY_UNHELD";
  public static final String EVENT_HANDOVER_COMPLETE = "android.telecom.event.HANDOVER_COMPLETE";
  public static final String EVENT_HANDOVER_FAILED = "android.telecom.event.HANDOVER_FAILED";
  public static final String EVENT_MERGE_COMPLETE = "android.telecom.event.MERGE_COMPLETE";
  public static final String EVENT_MERGE_START = "android.telecom.event.MERGE_START";
  public static final String EVENT_ON_HOLD_TONE_END = "android.telecom.event.ON_HOLD_TONE_END";
  public static final String EVENT_ON_HOLD_TONE_START = "android.telecom.event.ON_HOLD_TONE_START";
  public static final String EXTRA_ANSWERING_DROPS_FG_CALL = "android.telecom.extra.ANSWERING_DROPS_FG_CALL";
  public static final String EXTRA_ANSWERING_DROPS_FG_CALL_APP_NAME = "android.telecom.extra.ANSWERING_DROPS_FG_CALL_APP_NAME";
  public static final String EXTRA_CALL_PROPERTY = "android.telecom.extra.EXTRA_CALL_PROPERTY";
  public static final String EXTRA_CALL_SUBJECT = "android.telecom.extra.CALL_SUBJECT";
  public static final String EXTRA_CHILD_ADDRESS = "android.telecom.extra.CHILD_ADDRESS";
  public static final String EXTRA_DISABLE_ADD_CALL = "android.telecom.extra.DISABLE_ADD_CALL";
  public static final String EXTRA_LAST_FORWARDED_NUMBER = "android.telecom.extra.LAST_FORWARDED_NUMBER";
  public static final String EXTRA_ORIGINAL_CONNECTION_ID = "android.telecom.extra.ORIGINAL_CONNECTION_ID";
  private static final boolean PII_DEBUG = Log.isLoggable(3);
  public static final int PROPERTY_ASSISTED_DIALING_USED = 512;
  public static final int PROPERTY_EMERGENCY_CALLBACK_MODE = 1;
  public static final int PROPERTY_GENERIC_CONFERENCE = 2;
  public static final int PROPERTY_HAS_CDMA_VOICE_PRIVACY = 32;
  public static final int PROPERTY_HIGH_DEF_AUDIO = 4;
  public static final int PROPERTY_IS_DOWNGRADED_CONFERENCE = 64;
  public static final int PROPERTY_IS_EXTERNAL_CALL = 16;
  public static final int PROPERTY_IS_RTT = 256;
  public static final int PROPERTY_RTT_AUDIO_SPEECH = 1;
  public static final int PROPERTY_SELF_MANAGED = 128;
  public static final int PROPERTY_WIFI = 8;
  public static final int STATE_ACTIVE = 4;
  public static final int STATE_DIALING = 3;
  public static final int STATE_DISCONNECTED = 6;
  public static final int STATE_HOLDING = 5;
  public static final int STATE_INITIALIZING = 0;
  public static final int STATE_NEW = 1;
  public static final int STATE_PULLING_CALL = 7;
  public static final int STATE_RINGING = 2;
  private Uri mAddress;
  private int mAddressPresentation;
  private boolean mAudioModeIsVoip;
  private CallAudioState mCallAudioState;
  private String mCallerDisplayName;
  private int mCallerDisplayNamePresentation;
  private Conference mConference;
  private final Conference.Listener mConferenceDeathListener = new Conference.Listener()
  {
    public void onDestroyed(Conference paramAnonymousConference)
    {
      if (mConferenceables.remove(paramAnonymousConference)) {
        Connection.this.fireOnConferenceableConnectionsChanged();
      }
    }
  };
  private final List<Conferenceable> mConferenceables = new ArrayList();
  private long mConnectElapsedTimeMillis = 0L;
  private long mConnectTimeMillis = 0L;
  private int mConnectionCapabilities;
  private final Listener mConnectionDeathListener = new Listener()
  {
    public void onDestroyed(Connection paramAnonymousConnection)
    {
      if (mConferenceables.remove(paramAnonymousConnection)) {
        Connection.this.fireOnConferenceableConnectionsChanged();
      }
    }
  };
  private int mConnectionProperties;
  private ConnectionService mConnectionService;
  private DisconnectCause mDisconnectCause;
  private Bundle mExtras;
  private final Object mExtrasLock = new Object();
  private final Set<Listener> mListeners = Collections.newSetFromMap(new ConcurrentHashMap(8, 0.9F, 1));
  private PhoneAccountHandle mPhoneAccountHandle;
  private Set<String> mPreviousExtraKeys;
  private boolean mRingbackRequested = false;
  private int mState = 1;
  private StatusHints mStatusHints;
  private int mSupportedAudioRoutes = 15;
  private String mTelecomCallId;
  private final List<Conferenceable> mUnmodifiableConferenceables = Collections.unmodifiableList(mConferenceables);
  private VideoProvider mVideoProvider;
  private int mVideoState;
  private int mVideoStateHistory = 0;
  
  public Connection() {}
  
  public static boolean can(int paramInt1, int paramInt2)
  {
    boolean bool;
    if ((paramInt1 & paramInt2) == paramInt2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static String capabilitiesToString(int paramInt)
  {
    return capabilitiesToStringInternal(paramInt, true);
  }
  
  private static String capabilitiesToStringInternal(int paramInt, boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    if (paramBoolean) {
      localStringBuilder.append("Capabilities:");
    }
    String str;
    if (can(paramInt, 1))
    {
      if (paramBoolean) {
        str = " CAPABILITY_HOLD";
      } else {
        str = " hld";
      }
      localStringBuilder.append(str);
    }
    if (can(paramInt, 2))
    {
      if (paramBoolean) {
        str = " CAPABILITY_SUPPORT_HOLD";
      } else {
        str = " sup_hld";
      }
      localStringBuilder.append(str);
    }
    if (can(paramInt, 4))
    {
      if (paramBoolean) {
        str = " CAPABILITY_MERGE_CONFERENCE";
      } else {
        str = " mrg_cnf";
      }
      localStringBuilder.append(str);
    }
    if (can(paramInt, 8))
    {
      if (paramBoolean) {
        str = " CAPABILITY_SWAP_CONFERENCE";
      } else {
        str = " swp_cnf";
      }
      localStringBuilder.append(str);
    }
    if (can(paramInt, 32))
    {
      if (paramBoolean) {
        str = " CAPABILITY_RESPOND_VIA_TEXT";
      } else {
        str = " txt";
      }
      localStringBuilder.append(str);
    }
    if (can(paramInt, 64))
    {
      if (paramBoolean) {
        str = " CAPABILITY_MUTE";
      } else {
        str = " mut";
      }
      localStringBuilder.append(str);
    }
    if (can(paramInt, 128))
    {
      if (paramBoolean) {
        str = " CAPABILITY_MANAGE_CONFERENCE";
      } else {
        str = " mng_cnf";
      }
      localStringBuilder.append(str);
    }
    if (can(paramInt, 256))
    {
      if (paramBoolean) {
        str = " CAPABILITY_SUPPORTS_VT_LOCAL_RX";
      } else {
        str = " VTlrx";
      }
      localStringBuilder.append(str);
    }
    if (can(paramInt, 512))
    {
      if (paramBoolean) {
        str = " CAPABILITY_SUPPORTS_VT_LOCAL_TX";
      } else {
        str = " VTltx";
      }
      localStringBuilder.append(str);
    }
    if (can(paramInt, 768))
    {
      if (paramBoolean) {
        str = " CAPABILITY_SUPPORTS_VT_LOCAL_BIDIRECTIONAL";
      } else {
        str = " VTlbi";
      }
      localStringBuilder.append(str);
    }
    if (can(paramInt, 1024))
    {
      if (paramBoolean) {
        str = " CAPABILITY_SUPPORTS_VT_REMOTE_RX";
      } else {
        str = " VTrrx";
      }
      localStringBuilder.append(str);
    }
    if (can(paramInt, 2048))
    {
      if (paramBoolean) {
        str = " CAPABILITY_SUPPORTS_VT_REMOTE_TX";
      } else {
        str = " VTrtx";
      }
      localStringBuilder.append(str);
    }
    if (can(paramInt, 3072))
    {
      if (paramBoolean) {
        str = " CAPABILITY_SUPPORTS_VT_REMOTE_BIDIRECTIONAL";
      } else {
        str = " VTrbi";
      }
      localStringBuilder.append(str);
    }
    if (can(paramInt, 8388608))
    {
      if (paramBoolean) {
        str = " CAPABILITY_CANNOT_DOWNGRADE_VIDEO_TO_AUDIO";
      } else {
        str = " !v2a";
      }
      localStringBuilder.append(str);
    }
    if (can(paramInt, 262144))
    {
      if (paramBoolean) {
        str = " CAPABILITY_SPEED_UP_MT_AUDIO";
      } else {
        str = " spd_aud";
      }
      localStringBuilder.append(str);
    }
    if (can(paramInt, 524288))
    {
      if (paramBoolean) {
        str = " CAPABILITY_CAN_UPGRADE_TO_VIDEO";
      } else {
        str = " a2v";
      }
      localStringBuilder.append(str);
    }
    if (can(paramInt, 1048576))
    {
      if (paramBoolean) {
        str = " CAPABILITY_CAN_PAUSE_VIDEO";
      } else {
        str = " paus_VT";
      }
      localStringBuilder.append(str);
    }
    if (can(paramInt, 2097152))
    {
      if (paramBoolean) {
        str = " CAPABILITY_SINGLE_PARTY_CONFERENCE";
      } else {
        str = " 1p_cnf";
      }
      localStringBuilder.append(str);
    }
    if (can(paramInt, 4194304))
    {
      if (paramBoolean) {
        str = " CAPABILITY_CAN_SEND_RESPONSE_VIA_CONNECTION";
      } else {
        str = " rsp_by_con";
      }
      localStringBuilder.append(str);
    }
    if (can(paramInt, 16777216))
    {
      if (paramBoolean) {
        str = " CAPABILITY_CAN_PULL_CALL";
      } else {
        str = " pull";
      }
      localStringBuilder.append(str);
    }
    if (can(paramInt, 33554432))
    {
      if (paramBoolean) {
        str = " CAPABILITY_SUPPORT_DEFLECT";
      } else {
        str = " sup_def";
      }
      localStringBuilder.append(str);
    }
    if (can(paramInt, 134217728))
    {
      if (paramBoolean) {
        str = " CAPABILITY_SUPPORTS_RTT_REMOTE";
      } else {
        str = " sup_rtt";
      }
      localStringBuilder.append(str);
    }
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public static String capabilitiesToStringShort(int paramInt)
  {
    return capabilitiesToStringInternal(paramInt, false);
  }
  
  private final void clearConferenceableList()
  {
    Iterator localIterator = mConferenceables.iterator();
    while (localIterator.hasNext())
    {
      Conferenceable localConferenceable = (Conferenceable)localIterator.next();
      if ((localConferenceable instanceof Connection)) {
        ((Connection)localConferenceable).removeConnectionListener(mConnectionDeathListener);
      } else if ((localConferenceable instanceof Conference)) {
        ((Conference)localConferenceable).removeListener(mConferenceDeathListener);
      }
    }
    mConferenceables.clear();
  }
  
  public static Connection createCanceledConnection()
  {
    return new FailureSignalingConnection(new DisconnectCause(4));
  }
  
  public static Connection createFailedConnection(DisconnectCause paramDisconnectCause)
  {
    return new FailureSignalingConnection(paramDisconnectCause);
  }
  
  private final void fireConferenceChanged()
  {
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onConferenceChanged(this, mConference);
    }
  }
  
  private final void fireOnConferenceableConnectionsChanged()
  {
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onConferenceablesChanged(this, getConferenceables());
    }
  }
  
  public static String propertiesToString(int paramInt)
  {
    return propertiesToStringInternal(paramInt, true);
  }
  
  private static String propertiesToStringInternal(int paramInt, boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    if (paramBoolean) {
      localStringBuilder.append("Properties:");
    }
    String str;
    if (can(paramInt, 128))
    {
      if (paramBoolean) {
        str = " PROPERTY_SELF_MANAGED";
      } else {
        str = " self_mng";
      }
      localStringBuilder.append(str);
    }
    if (can(paramInt, 1))
    {
      if (paramBoolean) {
        str = " PROPERTY_EMERGENCY_CALLBACK_MODE";
      } else {
        str = " ecbm";
      }
      localStringBuilder.append(str);
    }
    if (can(paramInt, 4))
    {
      if (paramBoolean) {
        str = " PROPERTY_HIGH_DEF_AUDIO";
      } else {
        str = " HD";
      }
      localStringBuilder.append(str);
    }
    if (can(paramInt, 8))
    {
      if (paramBoolean) {
        str = " PROPERTY_WIFI";
      } else {
        str = " wifi";
      }
      localStringBuilder.append(str);
    }
    if (can(paramInt, 2))
    {
      if (paramBoolean) {
        str = " PROPERTY_GENERIC_CONFERENCE";
      } else {
        str = " gen_conf";
      }
      localStringBuilder.append(str);
    }
    if (can(paramInt, 16))
    {
      if (paramBoolean) {
        str = " PROPERTY_IS_EXTERNAL_CALL";
      } else {
        str = " xtrnl";
      }
      localStringBuilder.append(str);
    }
    if (can(paramInt, 32))
    {
      if (paramBoolean) {
        str = " PROPERTY_HAS_CDMA_VOICE_PRIVACY";
      } else {
        str = " priv";
      }
      localStringBuilder.append(str);
    }
    if (can(paramInt, 256))
    {
      if (paramBoolean) {
        str = " PROPERTY_IS_RTT";
      } else {
        str = " rtt";
      }
      localStringBuilder.append(str);
    }
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public static String propertiesToStringShort(int paramInt)
  {
    return propertiesToStringInternal(paramInt, false);
  }
  
  private void setState(int paramInt)
  {
    checkImmutable();
    if ((mState == 6) && (mState != paramInt))
    {
      Log.d(this, "Connection already DISCONNECTED; cannot transition out of this state.", new Object[0]);
      return;
    }
    if (mState != paramInt)
    {
      Log.d(this, "setState: %s", new Object[] { stateToString(paramInt) });
      mState = paramInt;
      onStateChanged(paramInt);
      Iterator localIterator = mListeners.iterator();
      while (localIterator.hasNext()) {
        ((Listener)localIterator.next()).onStateChanged(this, paramInt);
      }
    }
  }
  
  public static String stateToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      Log.wtf(Connection.class, "Unknown state %d", new Object[] { Integer.valueOf(paramInt) });
      return "UNKNOWN";
    case 7: 
      return "PULLING_CALL";
    case 6: 
      return "DISCONNECTED";
    case 5: 
      return "HOLDING";
    case 4: 
      return "ACTIVE";
    case 3: 
      return "DIALING";
    case 2: 
      return "RINGING";
    case 1: 
      return "NEW";
    }
    return "INITIALIZING";
  }
  
  static String toLogSafePhoneNumber(String paramString)
  {
    if (paramString == null) {
      return "";
    }
    if (PII_DEBUG) {
      return paramString;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    for (int i = 0; i < paramString.length(); i++)
    {
      char c = paramString.charAt(i);
      if ((c != '-') && (c != '@') && (c != '.')) {
        localStringBuilder.append('x');
      } else {
        localStringBuilder.append(c);
      }
    }
    return localStringBuilder.toString();
  }
  
  public void addCapability(int paramInt)
  {
    mConnectionCapabilities |= paramInt;
  }
  
  public final Connection addConnectionListener(Listener paramListener)
  {
    mListeners.add(paramListener);
    return this;
  }
  
  public boolean can(int paramInt)
  {
    return can(mConnectionCapabilities, paramInt);
  }
  
  public void checkImmutable() {}
  
  public final void destroy()
  {
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onDestroyed(this);
    }
  }
  
  public final Uri getAddress()
  {
    return mAddress;
  }
  
  public final int getAddressPresentation()
  {
    return mAddressPresentation;
  }
  
  public final boolean getAudioModeIsVoip()
  {
    return mAudioModeIsVoip;
  }
  
  @SystemApi
  @Deprecated
  public final AudioState getAudioState()
  {
    if (mCallAudioState == null) {
      return null;
    }
    return new AudioState(mCallAudioState);
  }
  
  public final CallAudioState getCallAudioState()
  {
    return mCallAudioState;
  }
  
  public final String getCallerDisplayName()
  {
    return mCallerDisplayName;
  }
  
  public final int getCallerDisplayNamePresentation()
  {
    return mCallerDisplayNamePresentation;
  }
  
  public final Conference getConference()
  {
    return mConference;
  }
  
  public final List<Conferenceable> getConferenceables()
  {
    return mUnmodifiableConferenceables;
  }
  
  public final long getConnectElapsedTimeMillis()
  {
    return mConnectElapsedTimeMillis;
  }
  
  public final long getConnectTimeMillis()
  {
    return mConnectTimeMillis;
  }
  
  public final int getConnectionCapabilities()
  {
    return mConnectionCapabilities;
  }
  
  public final int getConnectionProperties()
  {
    return mConnectionProperties;
  }
  
  public final ConnectionService getConnectionService()
  {
    return mConnectionService;
  }
  
  public final DisconnectCause getDisconnectCause()
  {
    return mDisconnectCause;
  }
  
  public final Bundle getExtras()
  {
    Bundle localBundle = null;
    synchronized (mExtrasLock)
    {
      if (mExtras != null)
      {
        localBundle = new android/os/Bundle;
        localBundle.<init>(mExtras);
      }
      return localBundle;
    }
  }
  
  public PhoneAccountHandle getPhoneAccountHandle()
  {
    return mPhoneAccountHandle;
  }
  
  public final int getState()
  {
    return mState;
  }
  
  public final StatusHints getStatusHints()
  {
    return mStatusHints;
  }
  
  public final int getSupportedAudioRoutes()
  {
    return mSupportedAudioRoutes;
  }
  
  public final String getTelecomCallId()
  {
    return mTelecomCallId;
  }
  
  public final VideoProvider getVideoProvider()
  {
    return mVideoProvider;
  }
  
  public final int getVideoState()
  {
    return mVideoState;
  }
  
  public int getVideoStateHistory()
  {
    return mVideoStateHistory;
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
  
  public void handleRttUpgradeResponse(RttTextStream paramRttTextStream) {}
  
  public final boolean isRingbackRequested()
  {
    return mRingbackRequested;
  }
  
  protected final void notifyConferenceMergeFailed()
  {
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onConferenceMergeFailed(this);
    }
  }
  
  protected void notifyConferenceStarted()
  {
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onConferenceStarted();
    }
  }
  
  protected void notifyConferenceSupportedChanged(boolean paramBoolean)
  {
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onConferenceSupportedChanged(this, paramBoolean);
    }
  }
  
  public void notifyPhoneAccountChanged(PhoneAccountHandle paramPhoneAccountHandle)
  {
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onPhoneAccountChanged(this, paramPhoneAccountHandle);
    }
  }
  
  public void onAbort() {}
  
  public void onAnswer()
  {
    onAnswer(0);
  }
  
  public void onAnswer(int paramInt) {}
  
  @SystemApi
  @Deprecated
  public void onAudioStateChanged(AudioState paramAudioState) {}
  
  public void onCallAudioStateChanged(CallAudioState paramCallAudioState) {}
  
  public void onCallEvent(String paramString, Bundle paramBundle) {}
  
  public void onDeflect(Uri paramUri) {}
  
  public void onDisconnect() {}
  
  public void onDisconnectConferenceParticipant(Uri paramUri) {}
  
  public void onExtrasChanged(Bundle paramBundle) {}
  
  public void onHandoverComplete() {}
  
  public void onHold() {}
  
  public void onPlayDtmfTone(char paramChar) {}
  
  public void onPostDialContinue(boolean paramBoolean) {}
  
  public void onPullExternalCall() {}
  
  public void onReject() {}
  
  public void onReject(String paramString) {}
  
  public void onSeparate() {}
  
  public void onShowIncomingCallUi() {}
  
  public void onSilence() {}
  
  public void onStartRtt(RttTextStream paramRttTextStream) {}
  
  public void onStateChanged(int paramInt) {}
  
  public void onStopDtmfTone() {}
  
  public void onStopRtt() {}
  
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
    checkImmutable();
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
    mConnectionCapabilities &= paramInt;
  }
  
  public final Connection removeConnectionListener(Listener paramListener)
  {
    if (paramListener != null) {
      mListeners.remove(paramListener);
    }
    return this;
  }
  
  public final void removeExtras(List<String> paramList)
  {
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
      paramList = Collections.unmodifiableList(paramList);
      ??? = mListeners.iterator();
      while (((Iterator)???).hasNext()) {
        ((Listener)((Iterator)???).next()).onExtrasRemoved(this, paramList);
      }
      return;
    }
  }
  
  public final void removeExtras(String... paramVarArgs)
  {
    removeExtras(Arrays.asList(paramVarArgs));
  }
  
  public void requestBluetoothAudio(BluetoothDevice paramBluetoothDevice)
  {
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onAudioRouteChanged(this, 2, paramBluetoothDevice.getAddress());
    }
  }
  
  public final void resetCdmaConnectionTime()
  {
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onCdmaConnectionTimeReset(this);
    }
  }
  
  public final void resetConference()
  {
    if (mConference != null)
    {
      Log.d(this, "Conference reset", new Object[0]);
      mConference = null;
      fireConferenceChanged();
    }
  }
  
  public void sendConnectionEvent(String paramString, Bundle paramBundle)
  {
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onConnectionEvent(this, paramString, paramBundle);
    }
  }
  
  public final void sendRemoteRttRequest()
  {
    mListeners.forEach(new _..Lambda.Connection.lnfFNF0t9fPLEf01JE291g4chSk(this));
  }
  
  public final void sendRttInitiationFailure(int paramInt)
  {
    mListeners.forEach(new _..Lambda.Connection.noXZvls4rxmO_SOjgkFMZLLrfSg(this, paramInt));
  }
  
  public final void sendRttInitiationSuccess()
  {
    mListeners.forEach(new _..Lambda.Connection.8xeoCKtoHEwnDqv6gbuSfOMODH0(this));
  }
  
  public final void sendRttSessionRemotelyTerminated()
  {
    mListeners.forEach(new _..Lambda.Connection.SYsjtKchY2AYvOeGveCrqxSfKTU(this));
  }
  
  public final void setActive()
  {
    checkImmutable();
    setRingbackRequested(false);
    setState(4);
  }
  
  public final void setAddress(Uri paramUri, int paramInt)
  {
    checkImmutable();
    Log.d(this, "setAddress %s", new Object[] { paramUri });
    mAddress = paramUri;
    mAddressPresentation = paramInt;
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onAddressChanged(this, paramUri, paramInt);
    }
  }
  
  public final void setAudioModeIsVoip(boolean paramBoolean)
  {
    checkImmutable();
    mAudioModeIsVoip = paramBoolean;
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onAudioModeIsVoipChanged(this, paramBoolean);
    }
  }
  
  public final void setAudioRoute(int paramInt)
  {
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onAudioRouteChanged(this, paramInt, null);
    }
  }
  
  final void setCallAudioState(CallAudioState paramCallAudioState)
  {
    checkImmutable();
    Log.d(this, "setAudioState %s", new Object[] { paramCallAudioState });
    mCallAudioState = paramCallAudioState;
    onAudioStateChanged(getAudioState());
    onCallAudioStateChanged(paramCallAudioState);
  }
  
  public final void setCallerDisplayName(String paramString, int paramInt)
  {
    checkImmutable();
    Log.d(this, "setCallerDisplayName %s", new Object[] { paramString });
    mCallerDisplayName = paramString;
    mCallerDisplayNamePresentation = paramInt;
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onCallerDisplayNameChanged(this, paramString, paramInt);
    }
  }
  
  public final boolean setConference(Conference paramConference)
  {
    checkImmutable();
    if (mConference == null)
    {
      mConference = paramConference;
      if ((mConnectionService != null) && (mConnectionService.containsConference(paramConference))) {
        fireConferenceChanged();
      }
      return true;
    }
    return false;
  }
  
  public final void setConferenceableConnections(List<Connection> paramList)
  {
    checkImmutable();
    clearConferenceableList();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      paramList = (Connection)localIterator.next();
      if (!mConferenceables.contains(paramList))
      {
        paramList.addConnectionListener(mConnectionDeathListener);
        mConferenceables.add(paramList);
      }
    }
    fireOnConferenceableConnectionsChanged();
  }
  
  public final void setConferenceables(List<Conferenceable> paramList)
  {
    clearConferenceableList();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      paramList = (Conferenceable)localIterator.next();
      if (!mConferenceables.contains(paramList))
      {
        if ((paramList instanceof Connection)) {
          ((Connection)paramList).addConnectionListener(mConnectionDeathListener);
        } else if ((paramList instanceof Conference)) {
          ((Conference)paramList).addListener(mConferenceDeathListener);
        }
        mConferenceables.add(paramList);
      }
    }
    fireOnConferenceableConnectionsChanged();
  }
  
  public final void setConnectTimeMillis(long paramLong)
  {
    mConnectTimeMillis = paramLong;
  }
  
  public final void setConnectionCapabilities(int paramInt)
  {
    checkImmutable();
    if (mConnectionCapabilities != paramInt)
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
    checkImmutable();
    if (mConnectionProperties != paramInt)
    {
      mConnectionProperties = paramInt;
      Iterator localIterator = mListeners.iterator();
      while (localIterator.hasNext()) {
        ((Listener)localIterator.next()).onConnectionPropertiesChanged(this, mConnectionProperties);
      }
    }
  }
  
  public final void setConnectionService(ConnectionService paramConnectionService)
  {
    checkImmutable();
    if (mConnectionService != null) {
      Log.e(this, new Exception(), "Trying to set ConnectionService on a connection which is already associated with another ConnectionService.", new Object[0]);
    } else {
      mConnectionService = paramConnectionService;
    }
  }
  
  public final void setConnectionStartElapsedRealTime(long paramLong)
  {
    mConnectElapsedTimeMillis = paramLong;
  }
  
  public final void setDialing()
  {
    checkImmutable();
    setState(3);
  }
  
  public final void setDisconnected(DisconnectCause paramDisconnectCause)
  {
    checkImmutable();
    mDisconnectCause = paramDisconnectCause;
    setState(6);
    Log.d(this, "Disconnected with cause %s", new Object[] { paramDisconnectCause });
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onDisconnected(this, paramDisconnectCause);
    }
  }
  
  public final void setExtras(Bundle paramBundle)
  {
    checkImmutable();
    putExtras(paramBundle);
    if (mPreviousExtraKeys != null)
    {
      ArrayList localArrayList = new ArrayList();
      Iterator localIterator = mPreviousExtraKeys.iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        if ((paramBundle == null) || (!paramBundle.containsKey(str))) {
          localArrayList.add(str);
        }
      }
      if (!localArrayList.isEmpty()) {
        removeExtras(localArrayList);
      }
    }
    if (mPreviousExtraKeys == null) {
      mPreviousExtraKeys = new ArraySet();
    }
    mPreviousExtraKeys.clear();
    if (paramBundle != null) {
      mPreviousExtraKeys.addAll(paramBundle.keySet());
    }
  }
  
  public final void setInitialized()
  {
    checkImmutable();
    setState(1);
  }
  
  public final void setInitializing()
  {
    checkImmutable();
    setState(0);
  }
  
  public final void setNextPostDialChar(char paramChar)
  {
    checkImmutable();
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onPostDialChar(this, paramChar);
    }
  }
  
  public final void setOnHold()
  {
    checkImmutable();
    setState(5);
  }
  
  public void setPhoneAccountHandle(PhoneAccountHandle paramPhoneAccountHandle)
  {
    if (mPhoneAccountHandle != paramPhoneAccountHandle)
    {
      mPhoneAccountHandle = paramPhoneAccountHandle;
      notifyPhoneAccountChanged(paramPhoneAccountHandle);
    }
  }
  
  public final void setPostDialWait(String paramString)
  {
    checkImmutable();
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onPostDialWait(this, paramString);
    }
  }
  
  public final void setPulling()
  {
    checkImmutable();
    setState(7);
  }
  
  public final void setRingbackRequested(boolean paramBoolean)
  {
    checkImmutable();
    if (mRingbackRequested != paramBoolean)
    {
      mRingbackRequested = paramBoolean;
      Iterator localIterator = mListeners.iterator();
      while (localIterator.hasNext()) {
        ((Listener)localIterator.next()).onRingbackRequested(this, paramBoolean);
      }
    }
  }
  
  public final void setRinging()
  {
    checkImmutable();
    setState(2);
  }
  
  public final void setStatusHints(StatusHints paramStatusHints)
  {
    checkImmutable();
    mStatusHints = paramStatusHints;
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onStatusHintsChanged(this, paramStatusHints);
    }
  }
  
  public final void setSupportedAudioRoutes(int paramInt)
  {
    if ((paramInt & 0x9) != 0)
    {
      if (mSupportedAudioRoutes != paramInt)
      {
        mSupportedAudioRoutes = paramInt;
        Iterator localIterator = mListeners.iterator();
        while (localIterator.hasNext()) {
          ((Listener)localIterator.next()).onSupportedAudioRoutesChanged(this, mSupportedAudioRoutes);
        }
      }
      return;
    }
    throw new IllegalArgumentException("supported audio routes must include either speaker or earpiece");
  }
  
  public void setTelecomCallId(String paramString)
  {
    mTelecomCallId = paramString;
  }
  
  public final void setVideoProvider(VideoProvider paramVideoProvider)
  {
    checkImmutable();
    mVideoProvider = paramVideoProvider;
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onVideoProviderChanged(this, paramVideoProvider);
    }
  }
  
  public final void setVideoState(int paramInt)
  {
    checkImmutable();
    Log.d(this, "setVideoState %d", new Object[] { Integer.valueOf(paramInt) });
    mVideoState = paramInt;
    mVideoStateHistory |= paramInt;
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onVideoStateChanged(this, mVideoState);
    }
  }
  
  public final void unsetConnectionService(ConnectionService paramConnectionService)
  {
    if (mConnectionService != paramConnectionService) {
      Log.e(this, new Exception(), "Trying to remove ConnectionService from a Connection that does not belong to the ConnectionService.", new Object[0]);
    } else {
      mConnectionService = null;
    }
  }
  
  protected final void updateConferenceMaxnumUserCount(int paramInt)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("updateConferenceMaxnumUserCount num: ");
    ((StringBuilder)localObject).append(paramInt);
    Log.d(this, ((StringBuilder)localObject).toString(), new Object[0]);
    localObject = mListeners.iterator();
    while (((Iterator)localObject).hasNext()) {
      ((Listener)((Iterator)localObject).next()).onConferenceMaxnumUserCountChanged(this, paramInt);
    }
  }
  
  protected final void updateConferenceParticipants(List<ConferenceParticipant> paramList)
  {
    Iterator localIterator = mListeners.iterator();
    while (localIterator.hasNext()) {
      ((Listener)localIterator.next()).onConferenceParticipantsChanged(this, paramList);
    }
  }
  
  private static class FailureSignalingConnection
    extends Connection
  {
    private boolean mImmutable = false;
    
    public FailureSignalingConnection(DisconnectCause paramDisconnectCause)
    {
      setDisconnected(paramDisconnectCause);
      mImmutable = true;
    }
    
    public void checkImmutable()
    {
      if (!mImmutable) {
        return;
      }
      throw new UnsupportedOperationException("Connection is immutable");
    }
  }
  
  public static abstract class Listener
  {
    public Listener() {}
    
    public void onAddressChanged(Connection paramConnection, Uri paramUri, int paramInt) {}
    
    public void onAudioModeIsVoipChanged(Connection paramConnection, boolean paramBoolean) {}
    
    public void onAudioRouteChanged(Connection paramConnection, int paramInt, String paramString) {}
    
    public void onCallerDisplayNameChanged(Connection paramConnection, String paramString, int paramInt) {}
    
    public void onCdmaConnectionTimeReset(Connection paramConnection) {}
    
    public void onConferenceChanged(Connection paramConnection, Conference paramConference) {}
    
    public void onConferenceMaxnumUserCountChanged(Connection paramConnection, int paramInt) {}
    
    public void onConferenceMergeFailed(Connection paramConnection) {}
    
    public void onConferenceParticipantsChanged(Connection paramConnection, List<ConferenceParticipant> paramList) {}
    
    public void onConferenceStarted() {}
    
    public void onConferenceSupportedChanged(Connection paramConnection, boolean paramBoolean) {}
    
    public void onConferenceablesChanged(Connection paramConnection, List<Conferenceable> paramList) {}
    
    public void onConnectionCapabilitiesChanged(Connection paramConnection, int paramInt) {}
    
    public void onConnectionEvent(Connection paramConnection, String paramString, Bundle paramBundle) {}
    
    public void onConnectionPropertiesChanged(Connection paramConnection, int paramInt) {}
    
    public void onDestroyed(Connection paramConnection) {}
    
    public void onDisconnected(Connection paramConnection, DisconnectCause paramDisconnectCause) {}
    
    public void onExtrasChanged(Connection paramConnection, Bundle paramBundle) {}
    
    public void onExtrasRemoved(Connection paramConnection, List<String> paramList) {}
    
    public void onPhoneAccountChanged(Connection paramConnection, PhoneAccountHandle paramPhoneAccountHandle) {}
    
    public void onPostDialChar(Connection paramConnection, char paramChar) {}
    
    public void onPostDialWait(Connection paramConnection, String paramString) {}
    
    public void onRemoteRttRequest(Connection paramConnection) {}
    
    public void onRingbackRequested(Connection paramConnection, boolean paramBoolean) {}
    
    public void onRttInitiationFailure(Connection paramConnection, int paramInt) {}
    
    public void onRttInitiationSuccess(Connection paramConnection) {}
    
    public void onRttSessionRemotelyTerminated(Connection paramConnection) {}
    
    public void onStateChanged(Connection paramConnection, int paramInt) {}
    
    public void onStatusHintsChanged(Connection paramConnection, StatusHints paramStatusHints) {}
    
    public void onSupportedAudioRoutesChanged(Connection paramConnection, int paramInt) {}
    
    public void onVideoProviderChanged(Connection paramConnection, Connection.VideoProvider paramVideoProvider) {}
    
    public void onVideoStateChanged(Connection paramConnection, int paramInt) {}
  }
  
  public static final class RttModifyStatus
  {
    public static final int SESSION_MODIFY_REQUEST_FAIL = 2;
    public static final int SESSION_MODIFY_REQUEST_INVALID = 3;
    public static final int SESSION_MODIFY_REQUEST_REJECTED_BY_REMOTE = 5;
    public static final int SESSION_MODIFY_REQUEST_SUCCESS = 1;
    public static final int SESSION_MODIFY_REQUEST_TIMED_OUT = 4;
    
    private RttModifyStatus() {}
  }
  
  public static final class RttTextStream
  {
    private static final int READ_BUFFER_SIZE = 1000;
    private final ParcelFileDescriptor mFdFromInCall;
    private final ParcelFileDescriptor mFdToInCall;
    private final InputStreamReader mPipeFromInCall;
    private final OutputStreamWriter mPipeToInCall;
    private char[] mReadBuffer = new char['Ϩ'];
    
    public RttTextStream(ParcelFileDescriptor paramParcelFileDescriptor1, ParcelFileDescriptor paramParcelFileDescriptor2)
    {
      mFdFromInCall = paramParcelFileDescriptor2;
      mFdToInCall = paramParcelFileDescriptor1;
      mPipeFromInCall = new InputStreamReader(new FileInputStream(paramParcelFileDescriptor2.getFileDescriptor()));
      mPipeToInCall = new OutputStreamWriter(new FileOutputStream(paramParcelFileDescriptor1.getFileDescriptor()));
    }
    
    public ParcelFileDescriptor getFdFromInCall()
    {
      return mFdFromInCall;
    }
    
    public ParcelFileDescriptor getFdToInCall()
    {
      return mFdToInCall;
    }
    
    public String read()
      throws IOException
    {
      int i = mPipeFromInCall.read(mReadBuffer, 0, 1000);
      if (i < 0) {
        return null;
      }
      return new String(mReadBuffer, 0, i);
    }
    
    public String readImmediately()
      throws IOException
    {
      if (mPipeFromInCall.ready()) {
        return read();
      }
      return null;
    }
    
    public void write(String paramString)
      throws IOException
    {
      mPipeToInCall.write(paramString);
      mPipeToInCall.flush();
    }
  }
  
  public static abstract class VideoProvider
  {
    private static final int MSG_ADD_VIDEO_CALLBACK = 1;
    private static final int MSG_REMOVE_VIDEO_CALLBACK = 12;
    private static final int MSG_REQUEST_CAMERA_CAPABILITIES = 9;
    private static final int MSG_REQUEST_CONNECTION_DATA_USAGE = 10;
    private static final int MSG_SEND_SESSION_MODIFY_REQUEST = 7;
    private static final int MSG_SEND_SESSION_MODIFY_RESPONSE = 8;
    private static final int MSG_SET_CAMERA = 2;
    private static final int MSG_SET_DEVICE_ORIENTATION = 5;
    private static final int MSG_SET_DISPLAY_SURFACE = 4;
    private static final int MSG_SET_PAUSE_IMAGE = 11;
    private static final int MSG_SET_PREVIEW_SURFACE = 3;
    private static final int MSG_SET_ZOOM = 6;
    public static final int SESSION_EVENT_CAMERA_FAILURE = 5;
    private static final String SESSION_EVENT_CAMERA_FAILURE_STR = "CAMERA_FAIL";
    public static final int SESSION_EVENT_CAMERA_PERMISSION_ERROR = 7;
    private static final String SESSION_EVENT_CAMERA_PERMISSION_ERROR_STR = "CAMERA_PERMISSION_ERROR";
    public static final int SESSION_EVENT_CAMERA_READY = 6;
    private static final String SESSION_EVENT_CAMERA_READY_STR = "CAMERA_READY";
    public static final int SESSION_EVENT_RX_PAUSE = 1;
    private static final String SESSION_EVENT_RX_PAUSE_STR = "RX_PAUSE";
    public static final int SESSION_EVENT_RX_RESUME = 2;
    private static final String SESSION_EVENT_RX_RESUME_STR = "RX_RESUME";
    public static final int SESSION_EVENT_TX_START = 3;
    private static final String SESSION_EVENT_TX_START_STR = "TX_START";
    public static final int SESSION_EVENT_TX_STOP = 4;
    private static final String SESSION_EVENT_TX_STOP_STR = "TX_STOP";
    private static final String SESSION_EVENT_UNKNOWN_STR = "UNKNOWN";
    public static final int SESSION_MODIFY_REQUEST_FAIL = 2;
    public static final int SESSION_MODIFY_REQUEST_INVALID = 3;
    public static final int SESSION_MODIFY_REQUEST_REJECTED_BY_REMOTE = 5;
    public static final int SESSION_MODIFY_REQUEST_SUCCESS = 1;
    public static final int SESSION_MODIFY_REQUEST_TIMED_OUT = 4;
    private final VideoProviderBinder mBinder = new VideoProviderBinder(null);
    private VideoProviderHandler mMessageHandler;
    private ConcurrentHashMap<IBinder, IVideoCallback> mVideoCallbacks = new ConcurrentHashMap(8, 0.9F, 1);
    
    public VideoProvider()
    {
      mMessageHandler = new VideoProviderHandler(Looper.getMainLooper());
    }
    
    public VideoProvider(Looper paramLooper)
    {
      mMessageHandler = new VideoProviderHandler(paramLooper);
    }
    
    public static String sessionEventToString(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("UNKNOWN ");
        localStringBuilder.append(paramInt);
        return localStringBuilder.toString();
      case 7: 
        return "CAMERA_PERMISSION_ERROR";
      case 6: 
        return "CAMERA_READY";
      case 5: 
        return "CAMERA_FAIL";
      case 4: 
        return "TX_STOP";
      case 3: 
        return "TX_START";
      case 2: 
        return "RX_RESUME";
      }
      return "RX_PAUSE";
    }
    
    public void changeCallDataUsage(long paramLong)
    {
      setCallDataUsage(paramLong);
    }
    
    public void changeCameraCapabilities(VideoProfile.CameraCapabilities paramCameraCapabilities)
    {
      if (mVideoCallbacks != null)
      {
        Iterator localIterator = mVideoCallbacks.values().iterator();
        while (localIterator.hasNext())
        {
          IVideoCallback localIVideoCallback = (IVideoCallback)localIterator.next();
          try
          {
            localIVideoCallback.changeCameraCapabilities(paramCameraCapabilities);
          }
          catch (RemoteException localRemoteException)
          {
            Log.w(this, "changeCameraCapabilities callback failed", new Object[] { localRemoteException });
          }
        }
      }
    }
    
    public void changePeerDimensions(int paramInt1, int paramInt2)
    {
      if (mVideoCallbacks != null)
      {
        Iterator localIterator = mVideoCallbacks.values().iterator();
        while (localIterator.hasNext())
        {
          IVideoCallback localIVideoCallback = (IVideoCallback)localIterator.next();
          try
          {
            localIVideoCallback.changePeerDimensions(paramInt1, paramInt2);
          }
          catch (RemoteException localRemoteException)
          {
            Log.w(this, "changePeerDimensions callback failed", new Object[] { localRemoteException });
          }
        }
      }
    }
    
    public void changeVideoQuality(int paramInt)
    {
      if (mVideoCallbacks != null)
      {
        Iterator localIterator = mVideoCallbacks.values().iterator();
        while (localIterator.hasNext())
        {
          IVideoCallback localIVideoCallback = (IVideoCallback)localIterator.next();
          try
          {
            localIVideoCallback.changeVideoQuality(paramInt);
          }
          catch (RemoteException localRemoteException)
          {
            Log.w(this, "changeVideoQuality callback failed", new Object[] { localRemoteException });
          }
        }
      }
    }
    
    public final IVideoProvider getInterface()
    {
      return mBinder;
    }
    
    public void handleCallSessionEvent(int paramInt)
    {
      if (mVideoCallbacks != null)
      {
        Iterator localIterator = mVideoCallbacks.values().iterator();
        while (localIterator.hasNext())
        {
          IVideoCallback localIVideoCallback = (IVideoCallback)localIterator.next();
          try
          {
            localIVideoCallback.handleCallSessionEvent(paramInt);
          }
          catch (RemoteException localRemoteException)
          {
            Log.w(this, "handleCallSessionEvent callback failed", new Object[] { localRemoteException });
          }
        }
      }
    }
    
    public abstract void onRequestCameraCapabilities();
    
    public abstract void onRequestConnectionDataUsage();
    
    public abstract void onSendSessionModifyRequest(VideoProfile paramVideoProfile1, VideoProfile paramVideoProfile2);
    
    public abstract void onSendSessionModifyResponse(VideoProfile paramVideoProfile);
    
    public abstract void onSetCamera(String paramString);
    
    public void onSetCamera(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3) {}
    
    public abstract void onSetDeviceOrientation(int paramInt);
    
    public abstract void onSetDisplaySurface(Surface paramSurface);
    
    public abstract void onSetPauseImage(Uri paramUri);
    
    public abstract void onSetPreviewSurface(Surface paramSurface);
    
    public abstract void onSetZoom(float paramFloat);
    
    public void receiveSessionModifyRequest(VideoProfile paramVideoProfile)
    {
      if (mVideoCallbacks != null)
      {
        Iterator localIterator = mVideoCallbacks.values().iterator();
        while (localIterator.hasNext())
        {
          IVideoCallback localIVideoCallback = (IVideoCallback)localIterator.next();
          try
          {
            localIVideoCallback.receiveSessionModifyRequest(paramVideoProfile);
          }
          catch (RemoteException localRemoteException)
          {
            Log.w(this, "receiveSessionModifyRequest callback failed", new Object[] { localRemoteException });
          }
        }
      }
    }
    
    public void receiveSessionModifyResponse(int paramInt, VideoProfile paramVideoProfile1, VideoProfile paramVideoProfile2)
    {
      if (mVideoCallbacks != null)
      {
        Iterator localIterator = mVideoCallbacks.values().iterator();
        while (localIterator.hasNext())
        {
          IVideoCallback localIVideoCallback = (IVideoCallback)localIterator.next();
          try
          {
            localIVideoCallback.receiveSessionModifyResponse(paramInt, paramVideoProfile1, paramVideoProfile2);
          }
          catch (RemoteException localRemoteException)
          {
            Log.w(this, "receiveSessionModifyResponse callback failed", new Object[] { localRemoteException });
          }
        }
      }
    }
    
    public void setCallDataUsage(long paramLong)
    {
      if (mVideoCallbacks != null)
      {
        Iterator localIterator = mVideoCallbacks.values().iterator();
        while (localIterator.hasNext())
        {
          IVideoCallback localIVideoCallback = (IVideoCallback)localIterator.next();
          try
          {
            localIVideoCallback.changeCallDataUsage(paramLong);
          }
          catch (RemoteException localRemoteException)
          {
            Log.w(this, "setCallDataUsage callback failed", new Object[] { localRemoteException });
          }
        }
      }
    }
    
    private final class VideoProviderBinder
      extends IVideoProvider.Stub
    {
      private VideoProviderBinder() {}
      
      public void addVideoCallback(IBinder paramIBinder)
      {
        mMessageHandler.obtainMessage(1, paramIBinder).sendToTarget();
      }
      
      public void removeVideoCallback(IBinder paramIBinder)
      {
        mMessageHandler.obtainMessage(12, paramIBinder).sendToTarget();
      }
      
      public void requestCallDataUsage()
      {
        mMessageHandler.obtainMessage(10).sendToTarget();
      }
      
      public void requestCameraCapabilities()
      {
        mMessageHandler.obtainMessage(9).sendToTarget();
      }
      
      public void sendSessionModifyRequest(VideoProfile paramVideoProfile1, VideoProfile paramVideoProfile2)
      {
        SomeArgs localSomeArgs = SomeArgs.obtain();
        arg1 = paramVideoProfile1;
        arg2 = paramVideoProfile2;
        mMessageHandler.obtainMessage(7, localSomeArgs).sendToTarget();
      }
      
      public void sendSessionModifyResponse(VideoProfile paramVideoProfile)
      {
        mMessageHandler.obtainMessage(8, paramVideoProfile).sendToTarget();
      }
      
      public void setCamera(String paramString1, String paramString2, int paramInt)
      {
        SomeArgs localSomeArgs = SomeArgs.obtain();
        arg1 = paramString1;
        arg2 = paramString2;
        argi1 = Binder.getCallingUid();
        argi2 = Binder.getCallingPid();
        argi3 = paramInt;
        mMessageHandler.obtainMessage(2, localSomeArgs).sendToTarget();
      }
      
      public void setDeviceOrientation(int paramInt)
      {
        mMessageHandler.obtainMessage(5, paramInt, 0).sendToTarget();
      }
      
      public void setDisplaySurface(Surface paramSurface)
      {
        mMessageHandler.obtainMessage(4, paramSurface).sendToTarget();
      }
      
      public void setPauseImage(Uri paramUri)
      {
        mMessageHandler.obtainMessage(11, paramUri).sendToTarget();
      }
      
      public void setPreviewSurface(Surface paramSurface)
      {
        mMessageHandler.obtainMessage(3, paramSurface).sendToTarget();
      }
      
      public void setZoom(float paramFloat)
      {
        mMessageHandler.obtainMessage(6, Float.valueOf(paramFloat)).sendToTarget();
      }
    }
    
    private final class VideoProviderHandler
      extends Handler
    {
      public VideoProviderHandler() {}
      
      public VideoProviderHandler(Looper paramLooper)
      {
        super();
      }
      
      public void handleMessage(Message paramMessage)
      {
        Object localObject2;
        switch (what)
        {
        default: 
          break;
        case 12: 
          IBinder localIBinder = (IBinder)obj;
          IVideoCallback.Stub.asInterface((IBinder)obj);
          if (!mVideoCallbacks.containsKey(localIBinder)) {
            Log.i(this, "removeVideoProvider - skipped; not present.", new Object[0]);
          } else {
            mVideoCallbacks.remove(localIBinder);
          }
          break;
        case 11: 
          onSetPauseImage((Uri)obj);
          break;
        case 10: 
          onRequestConnectionDataUsage();
          break;
        case 9: 
          onRequestCameraCapabilities();
          break;
        case 8: 
          onSendSessionModifyResponse((VideoProfile)obj);
          break;
        case 7: 
          paramMessage = (SomeArgs)obj;
        case 6: 
          try
          {
            onSendSessionModifyRequest((VideoProfile)arg1, (VideoProfile)arg2);
            paramMessage.recycle();
          }
          finally
          {
            paramMessage.recycle();
          }
          break;
        case 5: 
          onSetDeviceOrientation(arg1);
          break;
        case 4: 
          onSetDisplaySurface((Surface)obj);
          break;
        case 3: 
          onSetPreviewSurface((Surface)obj);
          break;
        case 2: 
          localObject2 = (SomeArgs)obj;
        case 1: 
          try
          {
            onSetCamera((String)arg1);
            onSetCamera((String)arg1, (String)arg2, argi1, argi2, argi3);
            ((SomeArgs)localObject2).recycle();
          }
          finally
          {
            ((SomeArgs)localObject2).recycle();
          }
          paramMessage = IVideoCallback.Stub.asInterface((IBinder)obj);
          if (paramMessage == null) {
            Log.w(this, "addVideoProvider - skipped; callback is null.", new Object[0]);
          } else if (mVideoCallbacks.containsKey(localObject2)) {
            Log.i(this, "addVideoProvider - skipped; already present.", new Object[0]);
          } else {
            mVideoCallbacks.put(localObject2, paramMessage);
          }
          break;
        }
      }
    }
  }
}
