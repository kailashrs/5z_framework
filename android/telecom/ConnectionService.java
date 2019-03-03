package android.telecom;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.telecom.Logging.Session;
import android.telecom.Logging.Session.Info;
import com.android.internal.os.SomeArgs;
import com.android.internal.telecom.IConnectionService.Stub;
import com.android.internal.telecom.IConnectionServiceAdapter;
import com.android.internal.telecom.IVideoProvider;
import com.android.internal.telecom.RemoteServiceCallback.Stub;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ConnectionService
  extends Service
{
  public static final String EXTRA_IS_HANDOVER = "android.telecom.extra.IS_HANDOVER";
  private static final int MSG_ABORT = 3;
  private static final int MSG_ADD_CONNECTION_SERVICE_ADAPTER = 1;
  private static final int MSG_ADD_PARTICIPANT_WITH_CONFERENCE = 40;
  private static final int MSG_ANSWER = 4;
  private static final int MSG_ANSWER_VIDEO = 17;
  private static final int MSG_CONFERENCE = 12;
  private static final int MSG_CONNECTION_SERVICE_FOCUS_GAINED = 31;
  private static final int MSG_CONNECTION_SERVICE_FOCUS_LOST = 30;
  private static final int MSG_CREATE_CONNECTION = 2;
  private static final int MSG_CREATE_CONNECTION_COMPLETE = 29;
  private static final int MSG_CREATE_CONNECTION_FAILED = 25;
  private static final int MSG_DEFLECT = 34;
  private static final int MSG_DISCONNECT = 6;
  private static final int MSG_HANDOVER_COMPLETE = 33;
  private static final int MSG_HANDOVER_FAILED = 32;
  private static final int MSG_HOLD = 7;
  private static final int MSG_MERGE_CONFERENCE = 18;
  private static final int MSG_ON_CALL_AUDIO_STATE_CHANGED = 9;
  private static final int MSG_ON_EXTRAS_CHANGED = 24;
  private static final int MSG_ON_POST_DIAL_CONTINUE = 14;
  private static final int MSG_ON_START_RTT = 26;
  private static final int MSG_ON_STOP_RTT = 27;
  private static final int MSG_PLAY_DTMF_TONE = 10;
  private static final int MSG_PULL_EXTERNAL_CALL = 22;
  private static final int MSG_REJECT = 5;
  private static final int MSG_REJECT_WITH_MESSAGE = 20;
  private static final int MSG_REMOVE_CONNECTION_SERVICE_ADAPTER = 16;
  private static final int MSG_RTT_UPGRADE_RESPONSE = 28;
  private static final int MSG_SEND_CALL_EVENT = 23;
  private static final int MSG_SILENCE = 21;
  private static final int MSG_SPLIT_FROM_CONFERENCE = 13;
  private static final int MSG_STOP_DTMF_TONE = 11;
  private static final int MSG_SWAP_CONFERENCE = 19;
  private static final int MSG_UNHOLD = 8;
  private static final boolean PII_DEBUG = Log.isLoggable(3);
  public static final String SERVICE_INTERFACE = "android.telecom.ConnectionService";
  private static final String SESSION_ABORT = "CS.ab";
  private static final String SESSION_ADD_CS_ADAPTER = "CS.aCSA";
  private static final String SESSION_ANSWER = "CS.an";
  private static final String SESSION_ANSWER_VIDEO = "CS.anV";
  private static final String SESSION_CALL_AUDIO_SC = "CS.cASC";
  private static final String SESSION_CONFERENCE = "CS.c";
  private static final String SESSION_CONNECTION_SERVICE_FOCUS_GAINED = "CS.cSFG";
  private static final String SESSION_CONNECTION_SERVICE_FOCUS_LOST = "CS.cSFL";
  private static final String SESSION_CREATE_CONN = "CS.crCo";
  private static final String SESSION_CREATE_CONN_COMPLETE = "CS.crCoC";
  private static final String SESSION_CREATE_CONN_FAILED = "CS.crCoF";
  private static final String SESSION_DEFLECT = "CS.def";
  private static final String SESSION_DISCONNECT = "CS.d";
  private static final String SESSION_EXTRAS_CHANGED = "CS.oEC";
  private static final String SESSION_HANDLER = "H.";
  private static final String SESSION_HANDOVER_COMPLETE = "CS.hC";
  private static final String SESSION_HANDOVER_FAILED = "CS.haF";
  private static final String SESSION_HOLD = "CS.h";
  private static final String SESSION_MERGE_CONFERENCE = "CS.mC";
  private static final String SESSION_PLAY_DTMF = "CS.pDT";
  private static final String SESSION_POST_DIAL_CONT = "CS.oPDC";
  private static final String SESSION_PULL_EXTERNAL_CALL = "CS.pEC";
  private static final String SESSION_REJECT = "CS.r";
  private static final String SESSION_REJECT_MESSAGE = "CS.rWM";
  private static final String SESSION_REMOVE_CS_ADAPTER = "CS.rCSA";
  private static final String SESSION_RTT_UPGRADE_RESPONSE = "CS.rTRUR";
  private static final String SESSION_SEND_CALL_EVENT = "CS.sCE";
  private static final String SESSION_SILENCE = "CS.s";
  private static final String SESSION_SPLIT_CONFERENCE = "CS.sFC";
  private static final String SESSION_START_RTT = "CS.+RTT";
  private static final String SESSION_STOP_DTMF = "CS.sDT";
  private static final String SESSION_STOP_RTT = "CS.-RTT";
  private static final String SESSION_SWAP_CONFERENCE = "CS.sC";
  private static final String SESSION_UNHOLD = "CS.u";
  private static final String SESSION_UPDATE_RTT_PIPES = "CS.uRTT";
  private static Connection sNullConnection;
  private final ConnectionServiceAdapter mAdapter = new ConnectionServiceAdapter();
  private boolean mAreAccountsInitialized = false;
  private final IBinder mBinder = new IConnectionService.Stub()
  {
    public void abort(String paramAnonymousString, Session.Info paramAnonymousInfo)
    {
      Log.startSession(paramAnonymousInfo, "CS.ab");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousString;
        arg2 = Log.createSubsession();
        mHandler.obtainMessage(3, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void addConnectionServiceAdapter(IConnectionServiceAdapter paramAnonymousIConnectionServiceAdapter, Session.Info paramAnonymousInfo)
    {
      Log.startSession(paramAnonymousInfo, "CS.aCSA");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousIConnectionServiceAdapter;
        arg2 = Log.createSubsession();
        mHandler.obtainMessage(1, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void addParticipantWithConference(String paramAnonymousString1, String paramAnonymousString2)
    {
      SomeArgs localSomeArgs = SomeArgs.obtain();
      arg1 = paramAnonymousString1;
      arg2 = paramAnonymousString2;
      mHandler.obtainMessage(40, localSomeArgs).sendToTarget();
    }
    
    public void answer(String paramAnonymousString, Session.Info paramAnonymousInfo)
    {
      Log.startSession(paramAnonymousInfo, "CS.an");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousString;
        arg2 = Log.createSubsession();
        mHandler.obtainMessage(4, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void answerVideo(String paramAnonymousString, int paramAnonymousInt, Session.Info paramAnonymousInfo)
    {
      Log.startSession(paramAnonymousInfo, "CS.anV");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousString;
        arg2 = Log.createSubsession();
        argi1 = paramAnonymousInt;
        mHandler.obtainMessage(17, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void conference(String paramAnonymousString1, String paramAnonymousString2, Session.Info paramAnonymousInfo)
    {
      Log.startSession(paramAnonymousInfo, "CS.c");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousString1;
        arg2 = paramAnonymousString2;
        arg3 = Log.createSubsession();
        mHandler.obtainMessage(12, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void connectionServiceFocusGained(Session.Info paramAnonymousInfo)
      throws RemoteException
    {
      Log.startSession(paramAnonymousInfo, "CS.cSFG");
      try
      {
        mHandler.obtainMessage(31).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void connectionServiceFocusLost(Session.Info paramAnonymousInfo)
      throws RemoteException
    {
      Log.startSession(paramAnonymousInfo, "CS.cSFL");
      try
      {
        mHandler.obtainMessage(30).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void createConnection(PhoneAccountHandle paramAnonymousPhoneAccountHandle, String paramAnonymousString, ConnectionRequest paramAnonymousConnectionRequest, boolean paramAnonymousBoolean1, boolean paramAnonymousBoolean2, Session.Info paramAnonymousInfo)
    {
      Log.startSession(paramAnonymousInfo, "CS.crCo");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousPhoneAccountHandle;
        arg2 = paramAnonymousString;
        arg3 = paramAnonymousConnectionRequest;
        arg4 = Log.createSubsession();
        argi1 = paramAnonymousBoolean1;
        argi2 = paramAnonymousBoolean2;
        mHandler.obtainMessage(2, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void createConnectionComplete(String paramAnonymousString, Session.Info paramAnonymousInfo)
    {
      Log.startSession(paramAnonymousInfo, "CS.crCoC");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousString;
        arg2 = Log.createSubsession();
        mHandler.obtainMessage(29, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void createConnectionFailed(PhoneAccountHandle paramAnonymousPhoneAccountHandle, String paramAnonymousString, ConnectionRequest paramAnonymousConnectionRequest, boolean paramAnonymousBoolean, Session.Info paramAnonymousInfo)
    {
      Log.startSession(paramAnonymousInfo, "CS.crCoF");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousString;
        arg2 = paramAnonymousConnectionRequest;
        arg3 = Log.createSubsession();
        arg4 = paramAnonymousPhoneAccountHandle;
        argi1 = paramAnonymousBoolean;
        mHandler.obtainMessage(25, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void deflect(String paramAnonymousString, Uri paramAnonymousUri, Session.Info paramAnonymousInfo)
    {
      Log.startSession(paramAnonymousInfo, "CS.def");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousString;
        arg2 = paramAnonymousUri;
        arg3 = Log.createSubsession();
        mHandler.obtainMessage(34, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void disconnect(String paramAnonymousString, Session.Info paramAnonymousInfo)
    {
      Log.startSession(paramAnonymousInfo, "CS.d");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousString;
        arg2 = Log.createSubsession();
        mHandler.obtainMessage(6, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void handoverComplete(String paramAnonymousString, Session.Info paramAnonymousInfo)
    {
      Log.startSession(paramAnonymousInfo, "CS.hC");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousString;
        arg2 = Log.createSubsession();
        mHandler.obtainMessage(33, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void handoverFailed(String paramAnonymousString, ConnectionRequest paramAnonymousConnectionRequest, int paramAnonymousInt, Session.Info paramAnonymousInfo)
    {
      Log.startSession(paramAnonymousInfo, "CS.haF");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousString;
        arg2 = paramAnonymousConnectionRequest;
        arg3 = Log.createSubsession();
        arg4 = Integer.valueOf(paramAnonymousInt);
        mHandler.obtainMessage(32, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void hold(String paramAnonymousString, Session.Info paramAnonymousInfo)
    {
      Log.startSession(paramAnonymousInfo, "CS.h");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousString;
        arg2 = Log.createSubsession();
        mHandler.obtainMessage(7, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void mergeConference(String paramAnonymousString, Session.Info paramAnonymousInfo)
    {
      Log.startSession(paramAnonymousInfo, "CS.mC");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousString;
        arg2 = Log.createSubsession();
        mHandler.obtainMessage(18, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void onCallAudioStateChanged(String paramAnonymousString, CallAudioState paramAnonymousCallAudioState, Session.Info paramAnonymousInfo)
    {
      Log.startSession(paramAnonymousInfo, "CS.cASC");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousString;
        arg2 = paramAnonymousCallAudioState;
        arg3 = Log.createSubsession();
        mHandler.obtainMessage(9, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void onExtrasChanged(String paramAnonymousString, Bundle paramAnonymousBundle, Session.Info paramAnonymousInfo)
    {
      Log.startSession(paramAnonymousInfo, "CS.oEC");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousString;
        arg2 = paramAnonymousBundle;
        arg3 = Log.createSubsession();
        mHandler.obtainMessage(24, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void onPostDialContinue(String paramAnonymousString, boolean paramAnonymousBoolean, Session.Info paramAnonymousInfo)
    {
      Log.startSession(paramAnonymousInfo, "CS.oPDC");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousString;
        arg2 = Log.createSubsession();
        argi1 = paramAnonymousBoolean;
        mHandler.obtainMessage(14, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void playDtmfTone(String paramAnonymousString, char paramAnonymousChar, Session.Info paramAnonymousInfo)
    {
      Log.startSession(paramAnonymousInfo, "CS.pDT");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = Character.valueOf(paramAnonymousChar);
        arg2 = paramAnonymousString;
        arg3 = Log.createSubsession();
        mHandler.obtainMessage(10, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void pullExternalCall(String paramAnonymousString, Session.Info paramAnonymousInfo)
    {
      Log.startSession(paramAnonymousInfo, "CS.pEC");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousString;
        arg2 = Log.createSubsession();
        mHandler.obtainMessage(22, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void reject(String paramAnonymousString, Session.Info paramAnonymousInfo)
    {
      Log.startSession(paramAnonymousInfo, "CS.r");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousString;
        arg2 = Log.createSubsession();
        mHandler.obtainMessage(5, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void rejectWithMessage(String paramAnonymousString1, String paramAnonymousString2, Session.Info paramAnonymousInfo)
    {
      Log.startSession(paramAnonymousInfo, "CS.rWM");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousString1;
        arg2 = paramAnonymousString2;
        arg3 = Log.createSubsession();
        mHandler.obtainMessage(20, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void removeConnectionServiceAdapter(IConnectionServiceAdapter paramAnonymousIConnectionServiceAdapter, Session.Info paramAnonymousInfo)
    {
      Log.startSession(paramAnonymousInfo, "CS.rCSA");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousIConnectionServiceAdapter;
        arg2 = Log.createSubsession();
        mHandler.obtainMessage(16, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void respondToRttUpgradeRequest(String paramAnonymousString, ParcelFileDescriptor paramAnonymousParcelFileDescriptor1, ParcelFileDescriptor paramAnonymousParcelFileDescriptor2, Session.Info paramAnonymousInfo)
      throws RemoteException
    {
      Log.startSession(paramAnonymousInfo, "CS.rTRUR");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousString;
        if ((paramAnonymousParcelFileDescriptor2 != null) && (paramAnonymousParcelFileDescriptor1 != null))
        {
          paramAnonymousString = new android/telecom/Connection$RttTextStream;
          paramAnonymousString.<init>(paramAnonymousParcelFileDescriptor2, paramAnonymousParcelFileDescriptor1);
          arg2 = paramAnonymousString;
        }
        else
        {
          arg2 = null;
        }
        arg3 = Log.createSubsession();
        mHandler.obtainMessage(28, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void sendCallEvent(String paramAnonymousString1, String paramAnonymousString2, Bundle paramAnonymousBundle, Session.Info paramAnonymousInfo)
    {
      Log.startSession(paramAnonymousInfo, "CS.sCE");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousString1;
        arg2 = paramAnonymousString2;
        arg3 = paramAnonymousBundle;
        arg4 = Log.createSubsession();
        mHandler.obtainMessage(23, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void silence(String paramAnonymousString, Session.Info paramAnonymousInfo)
    {
      Log.startSession(paramAnonymousInfo, "CS.s");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousString;
        arg2 = Log.createSubsession();
        mHandler.obtainMessage(21, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void splitFromConference(String paramAnonymousString, Session.Info paramAnonymousInfo)
    {
      Log.startSession(paramAnonymousInfo, "CS.sFC");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousString;
        arg2 = Log.createSubsession();
        mHandler.obtainMessage(13, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void startRtt(String paramAnonymousString, ParcelFileDescriptor paramAnonymousParcelFileDescriptor1, ParcelFileDescriptor paramAnonymousParcelFileDescriptor2, Session.Info paramAnonymousInfo)
      throws RemoteException
    {
      Log.startSession(paramAnonymousInfo, "CS.+RTT");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousString;
        paramAnonymousString = new android/telecom/Connection$RttTextStream;
        paramAnonymousString.<init>(paramAnonymousParcelFileDescriptor2, paramAnonymousParcelFileDescriptor1);
        arg2 = paramAnonymousString;
        arg3 = Log.createSubsession();
        mHandler.obtainMessage(26, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void stopDtmfTone(String paramAnonymousString, Session.Info paramAnonymousInfo)
    {
      Log.startSession(paramAnonymousInfo, "CS.sDT");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousString;
        arg2 = Log.createSubsession();
        mHandler.obtainMessage(11, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void stopRtt(String paramAnonymousString, Session.Info paramAnonymousInfo)
      throws RemoteException
    {
      Log.startSession(paramAnonymousInfo, "CS.-RTT");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousString;
        arg2 = Log.createSubsession();
        mHandler.obtainMessage(27, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void swapConference(String paramAnonymousString, Session.Info paramAnonymousInfo)
    {
      Log.startSession(paramAnonymousInfo, "CS.sC");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousString;
        arg2 = Log.createSubsession();
        mHandler.obtainMessage(19, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
    
    public void unhold(String paramAnonymousString, Session.Info paramAnonymousInfo)
    {
      Log.startSession(paramAnonymousInfo, "CS.u");
      try
      {
        paramAnonymousInfo = SomeArgs.obtain();
        arg1 = paramAnonymousString;
        arg2 = Log.createSubsession();
        mHandler.obtainMessage(8, paramAnonymousInfo).sendToTarget();
        return;
      }
      finally
      {
        Log.endSession();
      }
    }
  };
  private final Map<String, Conference> mConferenceById = new ConcurrentHashMap();
  private final Conference.Listener mConferenceListener = new Conference.Listener()
  {
    public void onConferenceableConnectionsChanged(Conference paramAnonymousConference, List<Connection> paramAnonymousList)
    {
      mAdapter.setConferenceableConnections((String)mIdByConference.get(paramAnonymousConference), ConnectionService.this.createConnectionIdList(paramAnonymousList));
    }
    
    public void onConnectionAdded(Conference paramAnonymousConference, Connection paramAnonymousConnection) {}
    
    public void onConnectionCapabilitiesChanged(Conference paramAnonymousConference, int paramAnonymousInt)
    {
      paramAnonymousConference = (String)mIdByConference.get(paramAnonymousConference);
      Log.d(this, "call capabilities: conference: %s", new Object[] { Connection.capabilitiesToString(paramAnonymousInt) });
      mAdapter.setConnectionCapabilities(paramAnonymousConference, paramAnonymousInt);
    }
    
    public void onConnectionPropertiesChanged(Conference paramAnonymousConference, int paramAnonymousInt)
    {
      paramAnonymousConference = (String)mIdByConference.get(paramAnonymousConference);
      Log.d(this, "call capabilities: conference: %s", new Object[] { Connection.propertiesToString(paramAnonymousInt) });
      mAdapter.setConnectionProperties(paramAnonymousConference, paramAnonymousInt);
    }
    
    public void onConnectionRemoved(Conference paramAnonymousConference, Connection paramAnonymousConnection) {}
    
    public void onDestroyed(Conference paramAnonymousConference)
    {
      ConnectionService.this.removeConference(paramAnonymousConference);
    }
    
    public void onDisconnected(Conference paramAnonymousConference, DisconnectCause paramAnonymousDisconnectCause)
    {
      paramAnonymousConference = (String)mIdByConference.get(paramAnonymousConference);
      mAdapter.setDisconnected(paramAnonymousConference, paramAnonymousDisconnectCause);
    }
    
    public void onExtrasChanged(Conference paramAnonymousConference, Bundle paramAnonymousBundle)
    {
      paramAnonymousConference = (String)mIdByConference.get(paramAnonymousConference);
      if (paramAnonymousConference != null) {
        mAdapter.putExtras(paramAnonymousConference, paramAnonymousBundle);
      }
    }
    
    public void onExtrasRemoved(Conference paramAnonymousConference, List<String> paramAnonymousList)
    {
      paramAnonymousConference = (String)mIdByConference.get(paramAnonymousConference);
      if (paramAnonymousConference != null) {
        mAdapter.removeExtras(paramAnonymousConference, paramAnonymousList);
      }
    }
    
    public void onStateChanged(Conference paramAnonymousConference, int paramAnonymousInt1, int paramAnonymousInt2)
    {
      paramAnonymousConference = (String)mIdByConference.get(paramAnonymousConference);
      switch (paramAnonymousInt2)
      {
      default: 
        break;
      case 5: 
        mAdapter.setOnHold(paramAnonymousConference);
        break;
      case 4: 
        mAdapter.setActive(paramAnonymousConference);
      }
    }
    
    public void onStatusHintsChanged(Conference paramAnonymousConference, StatusHints paramAnonymousStatusHints)
    {
      paramAnonymousConference = (String)mIdByConference.get(paramAnonymousConference);
      if (paramAnonymousConference != null) {
        mAdapter.setStatusHints(paramAnonymousConference, paramAnonymousStatusHints);
      }
    }
    
    public void onVideoProviderChanged(Conference paramAnonymousConference, Connection.VideoProvider paramAnonymousVideoProvider)
    {
      String str = (String)mIdByConference.get(paramAnonymousConference);
      Log.d(this, "onVideoProviderChanged: Connection: %s, VideoProvider: %s", new Object[] { paramAnonymousConference, paramAnonymousVideoProvider });
      mAdapter.setVideoProvider(str, paramAnonymousVideoProvider);
    }
    
    public void onVideoStateChanged(Conference paramAnonymousConference, int paramAnonymousInt)
    {
      paramAnonymousConference = (String)mIdByConference.get(paramAnonymousConference);
      Log.d(this, "onVideoStateChanged set video state %d", new Object[] { Integer.valueOf(paramAnonymousInt) });
      mAdapter.setVideoState(paramAnonymousConference, paramAnonymousInt);
    }
  };
  private final Map<String, Connection> mConnectionById = new ConcurrentHashMap();
  private final Connection.Listener mConnectionListener = new Connection.Listener()
  {
    public void onAddressChanged(Connection paramAnonymousConnection, Uri paramAnonymousUri, int paramAnonymousInt)
    {
      paramAnonymousConnection = (String)mIdByConnection.get(paramAnonymousConnection);
      mAdapter.setAddress(paramAnonymousConnection, paramAnonymousUri, paramAnonymousInt);
    }
    
    public void onAudioModeIsVoipChanged(Connection paramAnonymousConnection, boolean paramAnonymousBoolean)
    {
      paramAnonymousConnection = (String)mIdByConnection.get(paramAnonymousConnection);
      mAdapter.setIsVoipAudioMode(paramAnonymousConnection, paramAnonymousBoolean);
    }
    
    public void onAudioRouteChanged(Connection paramAnonymousConnection, int paramAnonymousInt, String paramAnonymousString)
    {
      paramAnonymousConnection = (String)mIdByConnection.get(paramAnonymousConnection);
      if (paramAnonymousConnection != null) {
        mAdapter.setAudioRoute(paramAnonymousConnection, paramAnonymousInt, paramAnonymousString);
      }
    }
    
    public void onCallerDisplayNameChanged(Connection paramAnonymousConnection, String paramAnonymousString, int paramAnonymousInt)
    {
      paramAnonymousConnection = (String)mIdByConnection.get(paramAnonymousConnection);
      mAdapter.setCallerDisplayName(paramAnonymousConnection, paramAnonymousString, paramAnonymousInt);
    }
    
    public void onCdmaConnectionTimeReset(Connection paramAnonymousConnection)
    {
      paramAnonymousConnection = (String)mIdByConnection.get(paramAnonymousConnection);
      mAdapter.resetCdmaConnectionTime(paramAnonymousConnection);
    }
    
    public void onConferenceChanged(Connection paramAnonymousConnection, Conference paramAnonymousConference)
    {
      String str = (String)mIdByConnection.get(paramAnonymousConnection);
      if (str != null)
      {
        paramAnonymousConnection = null;
        if (paramAnonymousConference != null) {
          paramAnonymousConnection = (String)mIdByConference.get(paramAnonymousConference);
        }
        mAdapter.setIsConferenced(str, paramAnonymousConnection);
      }
    }
    
    public void onConferenceMergeFailed(Connection paramAnonymousConnection)
    {
      paramAnonymousConnection = (String)mIdByConnection.get(paramAnonymousConnection);
      if (paramAnonymousConnection != null) {
        mAdapter.onConferenceMergeFailed(paramAnonymousConnection);
      }
    }
    
    public void onConferenceablesChanged(Connection paramAnonymousConnection, List<Conferenceable> paramAnonymousList)
    {
      mAdapter.setConferenceableConnections((String)mIdByConnection.get(paramAnonymousConnection), ConnectionService.this.createIdList(paramAnonymousList));
    }
    
    public void onConnectionCapabilitiesChanged(Connection paramAnonymousConnection, int paramAnonymousInt)
    {
      paramAnonymousConnection = (String)mIdByConnection.get(paramAnonymousConnection);
      Log.d(this, "capabilities: parcelableconnection: %s", new Object[] { Connection.capabilitiesToString(paramAnonymousInt) });
      mAdapter.setConnectionCapabilities(paramAnonymousConnection, paramAnonymousInt);
    }
    
    public void onConnectionEvent(Connection paramAnonymousConnection, String paramAnonymousString, Bundle paramAnonymousBundle)
    {
      paramAnonymousConnection = (String)mIdByConnection.get(paramAnonymousConnection);
      if (paramAnonymousConnection != null) {
        mAdapter.onConnectionEvent(paramAnonymousConnection, paramAnonymousString, paramAnonymousBundle);
      }
    }
    
    public void onConnectionPropertiesChanged(Connection paramAnonymousConnection, int paramAnonymousInt)
    {
      paramAnonymousConnection = (String)mIdByConnection.get(paramAnonymousConnection);
      Log.d(this, "properties: parcelableconnection: %s", new Object[] { Connection.propertiesToString(paramAnonymousInt) });
      mAdapter.setConnectionProperties(paramAnonymousConnection, paramAnonymousInt);
    }
    
    public void onDestroyed(Connection paramAnonymousConnection)
    {
      removeConnection(paramAnonymousConnection);
    }
    
    public void onDisconnected(Connection paramAnonymousConnection, DisconnectCause paramAnonymousDisconnectCause)
    {
      paramAnonymousConnection = (String)mIdByConnection.get(paramAnonymousConnection);
      Log.d(this, "Adapter set disconnected %s", new Object[] { paramAnonymousDisconnectCause });
      mAdapter.setDisconnected(paramAnonymousConnection, paramAnonymousDisconnectCause);
    }
    
    public void onExtrasChanged(Connection paramAnonymousConnection, Bundle paramAnonymousBundle)
    {
      paramAnonymousConnection = (String)mIdByConnection.get(paramAnonymousConnection);
      if (paramAnonymousConnection != null) {
        mAdapter.putExtras(paramAnonymousConnection, paramAnonymousBundle);
      }
    }
    
    public void onExtrasRemoved(Connection paramAnonymousConnection, List<String> paramAnonymousList)
    {
      paramAnonymousConnection = (String)mIdByConnection.get(paramAnonymousConnection);
      if (paramAnonymousConnection != null) {
        mAdapter.removeExtras(paramAnonymousConnection, paramAnonymousList);
      }
    }
    
    public void onPhoneAccountChanged(Connection paramAnonymousConnection, PhoneAccountHandle paramAnonymousPhoneAccountHandle)
    {
      paramAnonymousConnection = (String)mIdByConnection.get(paramAnonymousConnection);
      if (paramAnonymousConnection != null) {
        mAdapter.onPhoneAccountChanged(paramAnonymousConnection, paramAnonymousPhoneAccountHandle);
      }
    }
    
    public void onPostDialChar(Connection paramAnonymousConnection, char paramAnonymousChar)
    {
      String str = (String)mIdByConnection.get(paramAnonymousConnection);
      Log.d(this, "Adapter onPostDialChar %s, %s", new Object[] { paramAnonymousConnection, Character.valueOf(paramAnonymousChar) });
      mAdapter.onPostDialChar(str, paramAnonymousChar);
    }
    
    public void onPostDialWait(Connection paramAnonymousConnection, String paramAnonymousString)
    {
      String str = (String)mIdByConnection.get(paramAnonymousConnection);
      Log.d(this, "Adapter onPostDialWait %s, %s", new Object[] { paramAnonymousConnection, paramAnonymousString });
      mAdapter.onPostDialWait(str, paramAnonymousString);
    }
    
    public void onRemoteRttRequest(Connection paramAnonymousConnection)
    {
      paramAnonymousConnection = (String)mIdByConnection.get(paramAnonymousConnection);
      if (paramAnonymousConnection != null) {
        mAdapter.onRemoteRttRequest(paramAnonymousConnection);
      }
    }
    
    public void onRingbackRequested(Connection paramAnonymousConnection, boolean paramAnonymousBoolean)
    {
      paramAnonymousConnection = (String)mIdByConnection.get(paramAnonymousConnection);
      Log.d(this, "Adapter onRingback %b", new Object[] { Boolean.valueOf(paramAnonymousBoolean) });
      mAdapter.setRingbackRequested(paramAnonymousConnection, paramAnonymousBoolean);
    }
    
    public void onRttInitiationFailure(Connection paramAnonymousConnection, int paramAnonymousInt)
    {
      paramAnonymousConnection = (String)mIdByConnection.get(paramAnonymousConnection);
      if (paramAnonymousConnection != null) {
        mAdapter.onRttInitiationFailure(paramAnonymousConnection, paramAnonymousInt);
      }
    }
    
    public void onRttInitiationSuccess(Connection paramAnonymousConnection)
    {
      paramAnonymousConnection = (String)mIdByConnection.get(paramAnonymousConnection);
      if (paramAnonymousConnection != null) {
        mAdapter.onRttInitiationSuccess(paramAnonymousConnection);
      }
    }
    
    public void onRttSessionRemotelyTerminated(Connection paramAnonymousConnection)
    {
      paramAnonymousConnection = (String)mIdByConnection.get(paramAnonymousConnection);
      if (paramAnonymousConnection != null) {
        mAdapter.onRttSessionRemotelyTerminated(paramAnonymousConnection);
      }
    }
    
    public void onStateChanged(Connection paramAnonymousConnection, int paramAnonymousInt)
    {
      paramAnonymousConnection = (String)mIdByConnection.get(paramAnonymousConnection);
      Log.d(this, "Adapter set state %s %s", new Object[] { paramAnonymousConnection, Connection.stateToString(paramAnonymousInt) });
      switch (paramAnonymousInt)
      {
      default: 
        break;
      case 7: 
        mAdapter.setPulling(paramAnonymousConnection);
        break;
      case 6: 
        break;
      case 5: 
        mAdapter.setOnHold(paramAnonymousConnection);
        break;
      case 4: 
        mAdapter.setActive(paramAnonymousConnection);
        break;
      case 3: 
        mAdapter.setDialing(paramAnonymousConnection);
        break;
      case 2: 
        mAdapter.setRinging(paramAnonymousConnection);
        break;
      }
    }
    
    public void onStatusHintsChanged(Connection paramAnonymousConnection, StatusHints paramAnonymousStatusHints)
    {
      paramAnonymousConnection = (String)mIdByConnection.get(paramAnonymousConnection);
      mAdapter.setStatusHints(paramAnonymousConnection, paramAnonymousStatusHints);
    }
    
    public void onVideoProviderChanged(Connection paramAnonymousConnection, Connection.VideoProvider paramAnonymousVideoProvider)
    {
      String str = (String)mIdByConnection.get(paramAnonymousConnection);
      Log.d(this, "onVideoProviderChanged: Connection: %s, VideoProvider: %s", new Object[] { paramAnonymousConnection, paramAnonymousVideoProvider });
      mAdapter.setVideoProvider(str, paramAnonymousVideoProvider);
    }
    
    public void onVideoStateChanged(Connection paramAnonymousConnection, int paramAnonymousInt)
    {
      paramAnonymousConnection = (String)mIdByConnection.get(paramAnonymousConnection);
      Log.d(this, "Adapter set video state %d", new Object[] { Integer.valueOf(paramAnonymousInt) });
      mAdapter.setVideoState(paramAnonymousConnection, paramAnonymousInt);
    }
  };
  private final Handler mHandler = new Handler(Looper.getMainLooper())
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      int i = what;
      Object localObject33;
      if (i != 40)
      {
        boolean bool1 = false;
        Object localObject31;
        Object localObject32;
        Object localObject34;
        Object localObject16;
        SomeArgs localSomeArgs2;
        switch (i)
        {
        default: 
          Object localObject1;
          SomeArgs localSomeArgs1;
          Object localObject14;
          switch (i)
          {
          default: 
            break;
          case 34: 
            localObject1 = (SomeArgs)obj;
            Log.continueSession((Session)arg3, "H.CS.def");
          case 33: 
            try
            {
              ConnectionService.this.deflect((String)arg1, (Uri)arg2);
              ((SomeArgs)localObject1).recycle();
              Log.endSession();
            }
            finally
            {
              ((SomeArgs)localObject1).recycle();
              Log.endSession();
            }
          case 32: 
            try
            {
              Log.continueSession((Session)arg2, "H.CS.hC");
              localObject1 = (String)arg1;
              ConnectionService.this.notifyHandoverComplete((String)localObject1);
              paramAnonymousMessage.recycle();
              Log.endSession();
            }
            finally
            {
              paramAnonymousMessage.recycle();
              Log.endSession();
            }
            try
            {
              String str1 = (String)arg1;
              localObject31 = (ConnectionRequest)arg2;
              i = ((Integer)arg4).intValue();
              if (!mAreAccountsInitialized)
              {
                Log.d(this, "Enqueueing pre-init request %s", new Object[] { str1 });
                localObject32 = mPreInitializationConnectionRequests;
                localObject33 = new android/telecom/ConnectionService$2$4;
                ((4)localObject33).<init>(this, "H.CS.haF.pICR", null, str1, (ConnectionRequest)localObject31, i);
                ((List)localObject32).add(((4)localObject33).prepare());
              }
              else
              {
                Log.i(this, "createConnectionFailed %s", new Object[] { str1 });
                ConnectionService.this.handoverFailed(str1, (ConnectionRequest)localObject31, i);
              }
            }
            finally
            {
              paramAnonymousMessage.recycle();
              Log.endSession();
            }
          case 31: 
            onConnectionServiceFocusGained();
            break;
          case 30: 
            onConnectionServiceFocusLost();
            break;
          case 29: 
            paramAnonymousMessage = (SomeArgs)obj;
            Log.continueSession((Session)arg2, "H.CS.crCoC");
            try
            {
              localObject31 = (String)arg1;
              if (!mAreAccountsInitialized)
              {
                Log.d(this, "Enqueueing pre-init request %s", new Object[] { localObject31 });
                localObject33 = mPreInitializationConnectionRequests;
                android.telecom.Logging.Runnable local2 = new android/telecom/ConnectionService$2$2;
                local2.<init>(this, "H.CS.crCoC.pICR", null, (String)localObject31);
                ((List)localObject33).add(local2.prepare());
              }
              else
              {
                ConnectionService.this.notifyCreateConnectionComplete((String)localObject31);
              }
            }
            finally
            {
              paramAnonymousMessage.recycle();
              Log.endSession();
            }
          case 28: 
            paramAnonymousMessage = (SomeArgs)obj;
          case 27: 
            try
            {
              Log.continueSession((Session)arg3, "H.CS.rTRUR");
              String str2 = (String)arg1;
              localObject33 = (Connection.RttTextStream)arg2;
              ConnectionService.this.handleRttUpgradeResponse(str2, (Connection.RttTextStream)localObject33);
              paramAnonymousMessage.recycle();
              Log.endSession();
            }
            finally
            {
              paramAnonymousMessage.recycle();
              Log.endSession();
            }
          case 26: 
            try
            {
              Log.continueSession((Session)arg2, "H.CS.-RTT");
              String str3 = (String)arg1;
              ConnectionService.this.stopRtt(str3);
              paramAnonymousMessage.recycle();
              Log.endSession();
            }
            finally
            {
              paramAnonymousMessage.recycle();
              Log.endSession();
            }
          case 25: 
            try
            {
              Log.continueSession((Session)arg3, "H.CS.+RTT");
              localObject33 = (String)arg1;
              Connection.RttTextStream localRttTextStream = (Connection.RttTextStream)arg2;
              ConnectionService.this.startRtt((String)localObject33, localRttTextStream);
              paramAnonymousMessage.recycle();
              Log.endSession();
            }
            finally
            {
              paramAnonymousMessage.recycle();
              Log.endSession();
            }
            try
            {
              String str4 = (String)arg1;
              localObject32 = (ConnectionRequest)arg2;
              if (argi1 == 1) {
                bool1 = true;
              } else {
                bool1 = false;
              }
              localObject31 = (PhoneAccountHandle)arg4;
              if (!mAreAccountsInitialized)
              {
                Log.d(this, "Enqueueing pre-init request %s", new Object[] { str4 });
                localObject33 = mPreInitializationConnectionRequests;
                localObject34 = new android/telecom/ConnectionService$2$3;
                ((3)localObject34).<init>(this, "H.CS.crCoF.pICR", null, (PhoneAccountHandle)localObject31, str4, (ConnectionRequest)localObject32, bool1);
                ((List)localObject33).add(((3)localObject34).prepare());
              }
              else
              {
                Log.i(this, "createConnectionFailed %s", new Object[] { str4 });
                ConnectionService.this.createConnectionFailed((PhoneAccountHandle)localObject31, str4, (ConnectionRequest)localObject32, bool1);
              }
            }
            finally
            {
              paramAnonymousMessage.recycle();
              Log.endSession();
            }
          case 24: 
            paramAnonymousMessage = (SomeArgs)obj;
          case 23: 
            try
            {
              Log.continueSession((Session)arg3, "H.CS.oEC");
              String str5 = (String)arg1;
              localObject33 = (Bundle)arg2;
              ConnectionService.this.handleExtrasChanged(str5, (Bundle)localObject33);
              paramAnonymousMessage.recycle();
              Log.endSession();
            }
            finally
            {
              paramAnonymousMessage.recycle();
              Log.endSession();
            }
          case 22: 
            try
            {
              Log.continueSession((Session)arg4, "H.CS.sCE");
              String str6 = (String)arg1;
              localObject31 = (String)arg2;
              localObject33 = (Bundle)arg3;
              ConnectionService.this.sendCallEvent(str6, (String)localObject31, (Bundle)localObject33);
              paramAnonymousMessage.recycle();
              Log.endSession();
            }
            finally
            {
              paramAnonymousMessage.recycle();
              Log.endSession();
            }
          case 21: 
            try
            {
              Log.continueSession((Session)arg2, "H.CS.pEC");
              ConnectionService.this.pullExternalCall((String)arg1);
              paramAnonymousMessage.recycle();
              Log.endSession();
            }
            finally
            {
              paramAnonymousMessage.recycle();
              Log.endSession();
            }
          case 20: 
            try
            {
              ConnectionService.this.silence((String)arg1);
              localSomeArgs1.recycle();
              Log.endSession();
            }
            finally
            {
              localSomeArgs1.recycle();
              Log.endSession();
            }
          case 19: 
            try
            {
              ConnectionService.this.reject((String)arg1, (String)arg2);
              paramAnonymousMessage.recycle();
              Log.endSession();
            }
            finally
            {
              paramAnonymousMessage.recycle();
              Log.endSession();
            }
          case 18: 
            try
            {
              Log.continueSession((Session)arg2, "H.CS.sC");
              ConnectionService.this.swapConference((String)arg1);
              paramAnonymousMessage.recycle();
              Log.endSession();
            }
            finally
            {
              paramAnonymousMessage.recycle();
              Log.endSession();
            }
          case 17: 
            try
            {
              Log.continueSession((Session)arg2, "H.CS.mC");
              ConnectionService.this.mergeConference((String)arg1);
              ((SomeArgs)localObject14).recycle();
              Log.endSession();
            }
            finally
            {
              ((SomeArgs)localObject14).recycle();
              Log.endSession();
            }
          case 16: 
            try
            {
              localObject14 = (String)arg1;
              i = argi1;
              ConnectionService.this.answerVideo((String)localObject14, i);
              paramAnonymousMessage.recycle();
              Log.endSession();
            }
            finally
            {
              paramAnonymousMessage.recycle();
              Log.endSession();
            }
          }
          break;
        case 14: 
          try
          {
            Log.continueSession((Session)arg2, "H.CS.rCSA");
            mAdapter.removeAdapter((IConnectionServiceAdapter)arg1);
            ((SomeArgs)localObject16).recycle();
            Log.endSession();
          }
          finally
          {
            ((SomeArgs)localObject16).recycle();
            Log.endSession();
          }
          try
          {
            Log.continueSession((Session)arg2, "H.CS.oPDC");
            localObject16 = (String)arg1;
            if (argi1 == 1) {
              bool1 = true;
            }
            ConnectionService.this.onPostDialContinue((String)localObject16, bool1);
          }
          finally
          {
            paramAnonymousMessage.recycle();
            Log.endSession();
          }
        case 13: 
          paramAnonymousMessage = (SomeArgs)obj;
        case 12: 
          try
          {
            Log.continueSession((Session)arg2, "H.CS.sFC");
            ConnectionService.this.splitFromConference((String)arg1);
            paramAnonymousMessage.recycle();
            Log.endSession();
          }
          finally
          {
            paramAnonymousMessage.recycle();
            Log.endSession();
          }
        case 11: 
          try
          {
            Log.continueSession((Session)arg3, "H.CS.c");
            localObject33 = (String)arg1;
            String str7 = (String)arg2;
            ConnectionService.this.conference((String)localObject33, str7);
            paramAnonymousMessage.recycle();
            Log.endSession();
          }
          finally
          {
            paramAnonymousMessage.recycle();
            Log.endSession();
          }
        case 10: 
          try
          {
            Log.continueSession((Session)arg2, "H.CS.sDT");
            ConnectionService.this.stopDtmfTone((String)arg1);
            localSomeArgs2.recycle();
            Log.endSession();
          }
          finally
          {
            localSomeArgs2.recycle();
            Log.endSession();
          }
        case 9: 
          try
          {
            Log.continueSession((Session)arg3, "H.CS.pDT");
            ConnectionService.this.playDtmfTone((String)arg2, ((Character)arg1).charValue());
            paramAnonymousMessage.recycle();
            Log.endSession();
          }
          finally
          {
            paramAnonymousMessage.recycle();
            Log.endSession();
          }
        case 8: 
          try
          {
            localObject33 = (String)arg1;
            localObject31 = (CallAudioState)arg2;
            localObject32 = ConnectionService.this;
            CallAudioState localCallAudioState = new android/telecom/CallAudioState;
            localCallAudioState.<init>((CallAudioState)localObject31);
            ((ConnectionService)localObject32).onCallAudioStateChanged((String)localObject33, localCallAudioState);
            paramAnonymousMessage.recycle();
            Log.endSession();
          }
          finally
          {
            paramAnonymousMessage.recycle();
            Log.endSession();
          }
        case 7: 
          try
          {
            ConnectionService.this.unhold((String)arg1);
            paramAnonymousMessage.recycle();
            Log.endSession();
          }
          finally
          {
            paramAnonymousMessage.recycle();
            Log.endSession();
          }
        case 6: 
          try
          {
            ConnectionService.this.hold((String)arg1);
            paramAnonymousMessage.recycle();
            Log.endSession();
          }
          finally
          {
            paramAnonymousMessage.recycle();
            Log.endSession();
          }
        case 5: 
          try
          {
            ConnectionService.this.disconnect((String)arg1);
            paramAnonymousMessage.recycle();
            Log.endSession();
          }
          finally
          {
            paramAnonymousMessage.recycle();
            Log.endSession();
          }
        case 4: 
          try
          {
            ConnectionService.this.reject((String)arg1);
            paramAnonymousMessage.recycle();
            Log.endSession();
          }
          finally
          {
            paramAnonymousMessage.recycle();
            Log.endSession();
          }
        case 3: 
          try
          {
            ConnectionService.this.answer((String)arg1);
            paramAnonymousMessage.recycle();
            Log.endSession();
          }
          finally
          {
            paramAnonymousMessage.recycle();
            Log.endSession();
          }
        case 2: 
          try
          {
            ConnectionService.this.abort((String)arg1);
            paramAnonymousMessage.recycle();
            Log.endSession();
          }
          finally
          {
            paramAnonymousMessage.recycle();
            Log.endSession();
          }
          try
          {
            localObject34 = (PhoneAccountHandle)arg1;
            String str8 = (String)arg2;
            localObject33 = (ConnectionRequest)arg3;
            if (argi1 == 1) {
              bool1 = true;
            } else {
              bool1 = false;
            }
            boolean bool2;
            if (argi2 == 1) {
              bool2 = true;
            } else {
              bool2 = false;
            }
            if (!mAreAccountsInitialized)
            {
              Log.d(this, "Enqueueing pre-init request %s", new Object[] { str8 });
              localObject31 = mPreInitializationConnectionRequests;
              localObject32 = new android/telecom/ConnectionService$2$1;
              ((1)localObject32).<init>(this, "H.CS.crCo.pICR", null, (PhoneAccountHandle)localObject34, str8, (ConnectionRequest)localObject33, bool1, bool2);
              ((List)localObject31).add(((1)localObject32).prepare());
            }
            else
            {
              ConnectionService.this.createConnection((PhoneAccountHandle)localObject34, str8, (ConnectionRequest)localObject33, bool1, bool2);
            }
          }
          finally
          {
            paramAnonymousMessage.recycle();
            Log.endSession();
          }
        case 1: 
          paramAnonymousMessage = (SomeArgs)obj;
        }
      }
      else
      {
        try
        {
          IConnectionServiceAdapter localIConnectionServiceAdapter = (IConnectionServiceAdapter)arg1;
          Log.continueSession((Session)arg2, "H.CS.aCSA");
          mAdapter.addAdapter(localIConnectionServiceAdapter);
          ConnectionService.this.onAdapterAttached();
          paramAnonymousMessage.recycle();
          Log.endSession();
        }
        finally
        {
          paramAnonymousMessage.recycle();
          Log.endSession();
        }
      }
      try
      {
        localObject33 = (String)arg1;
        String str9 = (String)arg2;
        ConnectionService.this.addParticipantWithConference((String)localObject33, str9);
        return;
      }
      finally
      {
        paramAnonymousMessage.recycle();
      }
    }
  };
  private int mId = 0;
  private final Map<Conference, String> mIdByConference = new ConcurrentHashMap();
  private final Map<Connection, String> mIdByConnection = new ConcurrentHashMap();
  private Object mIdSyncRoot = new Object();
  private final List<Runnable> mPreInitializationConnectionRequests = new ArrayList();
  private final RemoteConnectionManager mRemoteConnectionManager = new RemoteConnectionManager(this);
  private Conference sNullConference;
  
  public ConnectionService() {}
  
  private void abort(String paramString)
  {
    Log.d(this, "abort %s", new Object[] { paramString });
    findConnectionForAction(paramString, "abort").onAbort();
  }
  
  private String addConferenceInternal(Conference paramConference)
  {
    Object localObject1 = null;
    Object localObject2 = localObject1;
    if (paramConference.getExtras() != null)
    {
      localObject2 = localObject1;
      if (paramConference.getExtras().containsKey("android.telecom.extra.ORIGINAL_CONNECTION_ID"))
      {
        localObject2 = paramConference.getExtras().getString("android.telecom.extra.ORIGINAL_CONNECTION_ID");
        Log.d(this, "addConferenceInternal: conf %s reusing original id %s", new Object[] { paramConference.getTelecomCallId(), localObject2 });
      }
    }
    if (mIdByConference.containsKey(paramConference))
    {
      Log.w(this, "Re-adding an existing conference: %s.", new Object[] { paramConference });
    }
    else if (paramConference != null)
    {
      if (localObject2 == null) {
        localObject2 = UUID.randomUUID().toString();
      }
      mConferenceById.put(localObject2, paramConference);
      mIdByConference.put(paramConference, localObject2);
      paramConference.addListener(mConferenceListener);
      return localObject2;
    }
    return null;
  }
  
  private void addConnection(PhoneAccountHandle paramPhoneAccountHandle, String paramString, Connection paramConnection)
  {
    if ((paramString != null) && (paramConnection != null))
    {
      paramConnection.setTelecomCallId(paramString);
      mConnectionById.put(paramString, paramConnection);
      mIdByConnection.put(paramConnection, paramString);
      paramConnection.addConnectionListener(mConnectionListener);
      paramConnection.setConnectionService(this);
      paramConnection.setPhoneAccountHandle(paramPhoneAccountHandle);
      onConnectionAdded(paramConnection);
    }
  }
  
  private String addExistingConnectionInternal(PhoneAccountHandle paramPhoneAccountHandle, Connection paramConnection)
  {
    Object localObject;
    if ((paramConnection.getExtras() != null) && (paramConnection.getExtras().containsKey("android.telecom.extra.ORIGINAL_CONNECTION_ID")))
    {
      localObject = paramConnection.getExtras().getString("android.telecom.extra.ORIGINAL_CONNECTION_ID");
      Log.d(this, "addExistingConnectionInternal - conn %s reusing original id %s", new Object[] { paramConnection.getTelecomCallId(), localObject });
    }
    else if (paramPhoneAccountHandle == null)
    {
      localObject = UUID.randomUUID().toString();
    }
    else
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramPhoneAccountHandle.getComponentName().getClassName());
      ((StringBuilder)localObject).append("@");
      ((StringBuilder)localObject).append(getNextCallId());
      localObject = ((StringBuilder)localObject).toString();
    }
    addConnection(paramPhoneAccountHandle, (String)localObject, paramConnection);
    return localObject;
  }
  
  private void addParticipantWithConference(String paramString1, String paramString2)
  {
    Log.d(this, "ConnectionService addParticipantWithConference(%s, %s)", new Object[] { paramString2, paramString1 });
    Conference localConference = findConferenceForAction(paramString1, "addParticipantWithConference");
    paramString1 = findConnectionForAction(paramString1, "addParticipantWithConnection");
    if (paramString1 != getNullConnection()) {
      onAddParticipant(paramString1, paramString2);
    } else if (localConference != getNullConference()) {
      localConference.onAddParticipant(paramString2);
    }
  }
  
  private void answer(String paramString)
  {
    Log.d(this, "answer %s", new Object[] { paramString });
    findConnectionForAction(paramString, "answer").onAnswer();
  }
  
  private void answerVideo(String paramString, int paramInt)
  {
    Log.d(this, "answerVideo %s", new Object[] { paramString });
    findConnectionForAction(paramString, "answer").onAnswer(paramInt);
  }
  
  private void conference(String paramString1, String paramString2)
  {
    Log.d(this, "conference %s, %s", new Object[] { paramString1, paramString2 });
    Connection localConnection = findConnectionForAction(paramString2, "conference");
    Object localObject = getNullConference();
    if (localConnection == getNullConnection())
    {
      Conference localConference = findConferenceForAction(paramString2, "conference");
      localObject = localConference;
      if (localConference == getNullConference())
      {
        Log.w(this, "Connection2 or Conference2 missing in conference request %s.", new Object[] { paramString2 });
        return;
      }
    }
    paramString2 = findConnectionForAction(paramString1, "conference");
    if (paramString2 == getNullConnection())
    {
      paramString2 = findConferenceForAction(paramString1, "addConnection");
      if (paramString2 == getNullConference())
      {
        Log.w(this, "Connection1 or Conference1 missing in conference request %s.", new Object[] { paramString1 });
      }
      else
      {
        if (localConnection == getNullConnection()) {
          break label146;
        }
        paramString2.onMerge(localConnection);
      }
      return;
      label146:
      Log.wtf(this, "There can only be one conference and an attempt was made to merge two conferences.", new Object[0]);
    }
    else if (localObject != getNullConference())
    {
      ((Conference)localObject).onMerge(paramString2);
    }
    else
    {
      onConference(paramString2, localConnection);
    }
  }
  
  private void createConnection(PhoneAccountHandle paramPhoneAccountHandle, String paramString, ConnectionRequest paramConnectionRequest, boolean paramBoolean1, boolean paramBoolean2)
  {
    boolean bool1;
    if ((paramConnectionRequest.getExtras() != null) && (paramConnectionRequest.getExtras().getBoolean("android.telecom.extra.IS_HANDOVER", false))) {
      bool1 = true;
    } else {
      bool1 = false;
    }
    boolean bool2;
    if ((paramConnectionRequest.getExtras() != null) && (paramConnectionRequest.getExtras().getBoolean("android.telecom.extra.IS_HANDOVER_CONNECTION", false))) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    Log.d(this, "createConnection, callManagerAccount: %s, callId: %s, request: %s, isIncoming: %b, isUnknown: %b, isLegacyHandover: %b, isHandover: %b", new Object[] { paramPhoneAccountHandle, paramString, paramConnectionRequest, Boolean.valueOf(paramBoolean1), Boolean.valueOf(paramBoolean2), Boolean.valueOf(bool1), Boolean.valueOf(bool2) });
    if (bool2)
    {
      if (paramConnectionRequest.getExtras() != null) {
        paramPhoneAccountHandle = (PhoneAccountHandle)paramConnectionRequest.getExtras().getParcelable("android.telecom.extra.HANDOVER_FROM_PHONE_ACCOUNT");
      } else {
        paramPhoneAccountHandle = null;
      }
      if (!paramBoolean1) {
        paramPhoneAccountHandle = onCreateOutgoingHandoverConnection(paramPhoneAccountHandle, paramConnectionRequest);
      } else {
        paramPhoneAccountHandle = onCreateIncomingHandoverConnection(paramPhoneAccountHandle, paramConnectionRequest);
      }
    }
    else if (paramBoolean2)
    {
      paramPhoneAccountHandle = onCreateUnknownConnection(paramPhoneAccountHandle, paramConnectionRequest);
    }
    else if (paramBoolean1)
    {
      paramPhoneAccountHandle = onCreateIncomingConnection(paramPhoneAccountHandle, paramConnectionRequest);
    }
    else
    {
      paramPhoneAccountHandle = onCreateOutgoingConnection(paramPhoneAccountHandle, paramConnectionRequest);
    }
    Log.d(this, "createConnection, connection: %s", new Object[] { paramPhoneAccountHandle });
    Object localObject = paramPhoneAccountHandle;
    if (paramPhoneAccountHandle == null)
    {
      Log.i(this, "createConnection, implementation returned null connection.", new Object[0]);
      localObject = Connection.createFailedConnection(new DisconnectCause(1, "IMPL_RETURNED_NULL_CONNECTION"));
    }
    ((Connection)localObject).setTelecomCallId(paramString);
    if (((Connection)localObject).getState() != 6) {
      addConnection(paramConnectionRequest.getAccountHandle(), paramString, (Connection)localObject);
    }
    paramPhoneAccountHandle = ((Connection)localObject).getAddress();
    if (paramPhoneAccountHandle == null) {
      paramPhoneAccountHandle = "null";
    } else {
      paramPhoneAccountHandle = paramPhoneAccountHandle.getSchemeSpecificPart();
    }
    Log.v(this, "createConnection, number: %s, state: %s, capabilities: %s, properties: %s", new Object[] { Connection.toLogSafePhoneNumber(paramPhoneAccountHandle), Connection.stateToString(((Connection)localObject).getState()), Connection.capabilitiesToString(((Connection)localObject).getConnectionCapabilities()), Connection.propertiesToString(((Connection)localObject).getConnectionProperties()) });
    Log.d(this, "createConnection, calling handleCreateConnectionSuccessful %s", new Object[] { paramString });
    ConnectionServiceAdapter localConnectionServiceAdapter = mAdapter;
    PhoneAccountHandle localPhoneAccountHandle = paramConnectionRequest.getAccountHandle();
    int i = ((Connection)localObject).getState();
    int j = ((Connection)localObject).getConnectionCapabilities();
    int k = ((Connection)localObject).getConnectionProperties();
    int m = ((Connection)localObject).getSupportedAudioRoutes();
    Uri localUri = ((Connection)localObject).getAddress();
    int n = ((Connection)localObject).getAddressPresentation();
    String str = ((Connection)localObject).getCallerDisplayName();
    int i1 = ((Connection)localObject).getCallerDisplayNamePresentation();
    if (((Connection)localObject).getVideoProvider() == null) {
      paramPhoneAccountHandle = null;
    } else {
      paramPhoneAccountHandle = ((Connection)localObject).getVideoProvider().getInterface();
    }
    localConnectionServiceAdapter.handleCreateConnectionComplete(paramString, paramConnectionRequest, new ParcelableConnection(localPhoneAccountHandle, i, j, k, m, localUri, n, str, i1, paramPhoneAccountHandle, ((Connection)localObject).getVideoState(), ((Connection)localObject).isRingbackRequested(), ((Connection)localObject).getAudioModeIsVoip(), ((Connection)localObject).getConnectTimeMillis(), ((Connection)localObject).getConnectElapsedTimeMillis(), ((Connection)localObject).getStatusHints(), ((Connection)localObject).getDisconnectCause(), createIdList(((Connection)localObject).getConferenceables()), ((Connection)localObject).getExtras()));
    if ((paramBoolean1) && (paramConnectionRequest.shouldShowIncomingCallUi()) && ((((Connection)localObject).getConnectionProperties() & 0x80) == 128)) {
      ((Connection)localObject).onShowIncomingCallUi();
    }
    if (paramBoolean2) {
      triggerConferenceRecalculate();
    }
  }
  
  private void createConnectionFailed(PhoneAccountHandle paramPhoneAccountHandle, String paramString, ConnectionRequest paramConnectionRequest, boolean paramBoolean)
  {
    Log.i(this, "createConnectionFailed %s", new Object[] { paramString });
    if (paramBoolean) {
      onCreateIncomingConnectionFailed(paramPhoneAccountHandle, paramConnectionRequest);
    } else {
      onCreateOutgoingConnectionFailed(paramPhoneAccountHandle, paramConnectionRequest);
    }
  }
  
  private List<String> createConnectionIdList(List<Connection> paramList)
  {
    ArrayList localArrayList = new ArrayList();
    paramList = paramList.iterator();
    while (paramList.hasNext())
    {
      Connection localConnection = (Connection)paramList.next();
      if (mIdByConnection.containsKey(localConnection)) {
        localArrayList.add((String)mIdByConnection.get(localConnection));
      }
    }
    Collections.sort(localArrayList);
    return localArrayList;
  }
  
  private List<String> createIdList(List<Conferenceable> paramList)
  {
    ArrayList localArrayList = new ArrayList();
    paramList = paramList.iterator();
    while (paramList.hasNext())
    {
      Object localObject = (Conferenceable)paramList.next();
      if ((localObject instanceof Connection))
      {
        localObject = (Connection)localObject;
        if (mIdByConnection.containsKey(localObject)) {
          localArrayList.add((String)mIdByConnection.get(localObject));
        }
      }
      else if ((localObject instanceof Conference))
      {
        localObject = (Conference)localObject;
        if (mIdByConference.containsKey(localObject)) {
          localArrayList.add((String)mIdByConference.get(localObject));
        }
      }
    }
    Collections.sort(localArrayList);
    return localArrayList;
  }
  
  private void deflect(String paramString, Uri paramUri)
  {
    Log.d(this, "deflect %s", new Object[] { paramString });
    findConnectionForAction(paramString, "deflect").onDeflect(paramUri);
  }
  
  private void disconnect(String paramString)
  {
    Log.d(this, "disconnect %s", new Object[] { paramString });
    if (mConnectionById.containsKey(paramString)) {
      findConnectionForAction(paramString, "disconnect").onDisconnect();
    } else {
      findConferenceForAction(paramString, "disconnect").onDisconnect();
    }
  }
  
  private void endAllConnections()
  {
    Iterator localIterator = mIdByConnection.keySet().iterator();
    while (localIterator.hasNext())
    {
      localObject = (Connection)localIterator.next();
      if (((Connection)localObject).getConference() == null) {
        ((Connection)localObject).onDisconnect();
      }
    }
    Object localObject = mIdByConference.keySet().iterator();
    while (((Iterator)localObject).hasNext()) {
      ((Conference)((Iterator)localObject).next()).onDisconnect();
    }
  }
  
  private Conference findConferenceForAction(String paramString1, String paramString2)
  {
    if (mConferenceById.containsKey(paramString1)) {
      return (Conference)mConferenceById.get(paramString1);
    }
    Log.w(this, "%s - Cannot find conference %s", new Object[] { paramString2, paramString1 });
    return getNullConference();
  }
  
  private Connection findConnectionForAction(String paramString1, String paramString2)
  {
    if ((paramString1 != null) && (mConnectionById.containsKey(paramString1))) {
      return (Connection)mConnectionById.get(paramString1);
    }
    Log.w(this, "%s - Cannot find Connection %s", new Object[] { paramString2, paramString1 });
    return getNullConnection();
  }
  
  private int getNextCallId()
  {
    synchronized (mIdSyncRoot)
    {
      int i = mId + 1;
      mId = i;
      return i;
    }
  }
  
  private Conference getNullConference()
  {
    if (sNullConference == null) {
      sNullConference = new Conference(null) {};
    }
    return sNullConference;
  }
  
  static Connection getNullConnection()
  {
    try
    {
      if (sNullConnection == null)
      {
        localObject1 = new android/telecom/ConnectionService$6;
        ((6)localObject1).<init>();
        sNullConnection = (Connection)localObject1;
      }
      Object localObject1 = sNullConnection;
      return localObject1;
    }
    finally {}
  }
  
  private void handleExtrasChanged(String paramString, Bundle paramBundle)
  {
    Log.d(this, "handleExtrasChanged(%s, %s)", new Object[] { paramString, paramBundle });
    if (mConnectionById.containsKey(paramString)) {
      findConnectionForAction(paramString, "handleExtrasChanged").handleExtrasChanged(paramBundle);
    } else if (mConferenceById.containsKey(paramString)) {
      findConferenceForAction(paramString, "handleExtrasChanged").handleExtrasChanged(paramBundle);
    }
  }
  
  private void handleRttUpgradeResponse(String paramString, Connection.RttTextStream paramRttTextStream)
  {
    boolean bool;
    if (paramRttTextStream == null) {
      bool = true;
    } else {
      bool = false;
    }
    Log.d(this, "handleRttUpgradeResponse(%s, %s)", new Object[] { paramString, Boolean.valueOf(bool) });
    if (mConnectionById.containsKey(paramString)) {
      findConnectionForAction(paramString, "handleRttUpgradeResponse").handleRttUpgradeResponse(paramRttTextStream);
    } else if (mConferenceById.containsKey(paramString)) {
      Log.w(this, "handleRttUpgradeResponse called on a conference.", new Object[0]);
    }
  }
  
  private void handoverFailed(String paramString, ConnectionRequest paramConnectionRequest, int paramInt)
  {
    Log.i(this, "handoverFailed %s", new Object[] { paramString });
    onHandoverFailed(paramConnectionRequest, paramInt);
  }
  
  private void hold(String paramString)
  {
    Log.d(this, "hold %s", new Object[] { paramString });
    if (mConnectionById.containsKey(paramString)) {
      findConnectionForAction(paramString, "hold").onHold();
    } else {
      findConferenceForAction(paramString, "hold").onHold();
    }
  }
  
  private void mergeConference(String paramString)
  {
    Log.d(this, "mergeConference(%s)", new Object[] { paramString });
    paramString = findConferenceForAction(paramString, "mergeConference");
    if (paramString != null) {
      paramString.onMerge();
    }
  }
  
  private void notifyCreateConnectionComplete(String paramString)
  {
    Log.i(this, "notifyCreateConnectionComplete %s", new Object[] { paramString });
    if (paramString == null)
    {
      Log.w(this, "notifyCreateConnectionComplete: callId is null.", new Object[0]);
      return;
    }
    onCreateConnectionComplete(findConnectionForAction(paramString, "notifyCreateConnectionComplete"));
  }
  
  private void notifyHandoverComplete(String paramString)
  {
    Log.d(this, "notifyHandoverComplete(%s)", new Object[] { paramString });
    paramString = findConnectionForAction(paramString, "notifyHandoverComplete");
    if (paramString != null) {
      paramString.onHandoverComplete();
    }
  }
  
  private void onAccountsInitialized()
  {
    mAreAccountsInitialized = true;
    Iterator localIterator = mPreInitializationConnectionRequests.iterator();
    while (localIterator.hasNext()) {
      ((Runnable)localIterator.next()).run();
    }
    mPreInitializationConnectionRequests.clear();
  }
  
  private void onAdapterAttached()
  {
    if (mAreAccountsInitialized) {
      return;
    }
    mAdapter.queryRemoteConnectionServices(new RemoteServiceCallback.Stub()
    {
      public void onError()
      {
        mHandler.post(new android.telecom.Logging.Runnable("oAA.qRCS.oE", null)
        {
          public void loggedRun()
          {
            ConnectionService.access$302(ConnectionService.this, true);
          }
        }.prepare());
      }
      
      public void onResult(final List<ComponentName> paramAnonymousList, final List<IBinder> paramAnonymousList1)
      {
        mHandler.post(new android.telecom.Logging.Runnable("oAA.qRCS.oR", null)
        {
          public void loggedRun()
          {
            for (int i = 0; (i < paramAnonymousList.size()) && (i < paramAnonymousList1.size()); i++) {
              mRemoteConnectionManager.addConnectionService((ComponentName)paramAnonymousList.get(i), IConnectionService.Stub.asInterface((IBinder)paramAnonymousList1.get(i)));
            }
            ConnectionService.this.onAccountsInitialized();
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("remote connection services found: ");
            localStringBuilder.append(paramAnonymousList1);
            Log.d(this, localStringBuilder.toString(), new Object[0]);
          }
        }.prepare());
      }
    });
  }
  
  private void onCallAudioStateChanged(String paramString, CallAudioState paramCallAudioState)
  {
    Log.d(this, "onAudioStateChanged %s %s", new Object[] { paramString, paramCallAudioState });
    if (mConnectionById.containsKey(paramString)) {
      findConnectionForAction(paramString, "onCallAudioStateChanged").setCallAudioState(paramCallAudioState);
    } else {
      findConferenceForAction(paramString, "onCallAudioStateChanged").setCallAudioState(paramCallAudioState);
    }
  }
  
  private void onPostDialContinue(String paramString, boolean paramBoolean)
  {
    Log.d(this, "onPostDialContinue(%s)", new Object[] { paramString });
    findConnectionForAction(paramString, "stopDtmfTone").onPostDialContinue(paramBoolean);
  }
  
  private void playDtmfTone(String paramString, char paramChar)
  {
    Log.d(this, "playDtmfTone %s %c", new Object[] { paramString, Character.valueOf(paramChar) });
    if (mConnectionById.containsKey(paramString)) {
      findConnectionForAction(paramString, "playDtmfTone").onPlayDtmfTone(paramChar);
    } else {
      findConferenceForAction(paramString, "playDtmfTone").onPlayDtmfTone(paramChar);
    }
  }
  
  private void pullExternalCall(String paramString)
  {
    Log.d(this, "pullExternalCall(%s)", new Object[] { paramString });
    paramString = findConnectionForAction(paramString, "pullExternalCall");
    if (paramString != null) {
      paramString.onPullExternalCall();
    }
  }
  
  private void reject(String paramString)
  {
    Log.d(this, "reject %s", new Object[] { paramString });
    findConnectionForAction(paramString, "reject").onReject();
  }
  
  private void reject(String paramString1, String paramString2)
  {
    Log.d(this, "reject %s with message", new Object[] { paramString1 });
    findConnectionForAction(paramString1, "reject").onReject(paramString2);
  }
  
  private void removeConference(Conference paramConference)
  {
    if (mIdByConference.containsKey(paramConference))
    {
      paramConference.removeListener(mConferenceListener);
      String str = (String)mIdByConference.get(paramConference);
      mConferenceById.remove(str);
      mIdByConference.remove(paramConference);
      mAdapter.removeCall(str);
      onConferenceRemoved(paramConference);
    }
  }
  
  private void sendCallEvent(String paramString1, String paramString2, Bundle paramBundle)
  {
    Log.d(this, "sendCallEvent(%s, %s)", new Object[] { paramString1, paramString2 });
    paramString1 = findConnectionForAction(paramString1, "sendCallEvent");
    if (paramString1 != null) {
      paramString1.onCallEvent(paramString2, paramBundle);
    }
  }
  
  private void silence(String paramString)
  {
    Log.d(this, "silence %s", new Object[] { paramString });
    findConnectionForAction(paramString, "silence").onSilence();
  }
  
  private void splitFromConference(String paramString)
  {
    Log.d(this, "splitFromConference(%s)", new Object[] { paramString });
    Connection localConnection = findConnectionForAction(paramString, "splitFromConference");
    if (localConnection == getNullConnection())
    {
      Log.w(this, "Connection missing in conference request %s.", new Object[] { paramString });
      return;
    }
    paramString = localConnection.getConference();
    if (paramString != null) {
      paramString.onSeparate(localConnection);
    }
  }
  
  private void startRtt(String paramString, Connection.RttTextStream paramRttTextStream)
  {
    Log.d(this, "startRtt(%s)", new Object[] { paramString });
    if (mConnectionById.containsKey(paramString)) {
      findConnectionForAction(paramString, "startRtt").onStartRtt(paramRttTextStream);
    } else if (mConferenceById.containsKey(paramString)) {
      Log.w(this, "startRtt called on a conference.", new Object[0]);
    }
  }
  
  private void stopDtmfTone(String paramString)
  {
    Log.d(this, "stopDtmfTone %s", new Object[] { paramString });
    if (mConnectionById.containsKey(paramString)) {
      findConnectionForAction(paramString, "stopDtmfTone").onStopDtmfTone();
    } else {
      findConferenceForAction(paramString, "stopDtmfTone").onStopDtmfTone();
    }
  }
  
  private void stopRtt(String paramString)
  {
    Log.d(this, "stopRtt(%s)", new Object[] { paramString });
    if (mConnectionById.containsKey(paramString)) {
      findConnectionForAction(paramString, "stopRtt").onStopRtt();
    } else if (mConferenceById.containsKey(paramString)) {
      Log.w(this, "stopRtt called on a conference.", new Object[0]);
    }
  }
  
  private void swapConference(String paramString)
  {
    Log.d(this, "swapConference(%s)", new Object[] { paramString });
    paramString = findConferenceForAction(paramString, "swapConference");
    if (paramString != null) {
      paramString.onSwap();
    }
  }
  
  private void unhold(String paramString)
  {
    Log.d(this, "unhold %s", new Object[] { paramString });
    if (mConnectionById.containsKey(paramString)) {
      findConnectionForAction(paramString, "unhold").onUnhold();
    } else {
      findConferenceForAction(paramString, "unhold").onUnhold();
    }
  }
  
  public final void addConference(Conference paramConference)
  {
    Log.d(this, "addConference: conference=%s", new Object[] { paramConference });
    String str = addConferenceInternal(paramConference);
    if (str != null)
    {
      Object localObject1 = new ArrayList(2);
      Object localObject2 = paramConference.getConnections().iterator();
      while (((Iterator)localObject2).hasNext())
      {
        localObject3 = (Connection)((Iterator)localObject2).next();
        if (mIdByConnection.containsKey(localObject3)) {
          ((List)localObject1).add((String)mIdByConnection.get(localObject3));
        }
      }
      paramConference.setTelecomCallId(str);
      localObject2 = paramConference.getPhoneAccountHandle();
      int i = paramConference.getState();
      int j = paramConference.getConnectionCapabilities();
      int k = paramConference.getConnectionProperties();
      if (paramConference.getVideoProvider() == null) {}
      for (Object localObject3 = null;; localObject3 = paramConference.getVideoProvider().getInterface()) {
        break;
      }
      localObject3 = new ParcelableConference((PhoneAccountHandle)localObject2, i, j, k, (List)localObject1, (IVideoProvider)localObject3, paramConference.getVideoState(), paramConference.getConnectTimeMillis(), paramConference.getConnectionStartElapsedRealTime(), paramConference.getStatusHints(), paramConference.getExtras());
      mAdapter.addConferenceCall(str, (ParcelableConference)localObject3);
      mAdapter.setVideoProvider(str, paramConference.getVideoProvider());
      mAdapter.setVideoState(str, paramConference.getVideoState());
      localObject3 = paramConference.getConnections().iterator();
      while (((Iterator)localObject3).hasNext())
      {
        localObject1 = (Connection)((Iterator)localObject3).next();
        localObject1 = (String)mIdByConnection.get(localObject1);
        if (localObject1 != null) {
          mAdapter.setIsConferenced((String)localObject1, str);
        }
      }
      onConferenceAdded(paramConference);
    }
  }
  
  public final void addExistingConnection(PhoneAccountHandle paramPhoneAccountHandle, Connection paramConnection)
  {
    addExistingConnection(paramPhoneAccountHandle, paramConnection, null);
  }
  
  public final void addExistingConnection(PhoneAccountHandle paramPhoneAccountHandle, Connection paramConnection, Conference paramConference)
  {
    String str1 = addExistingConnectionInternal(paramPhoneAccountHandle, paramConnection);
    if (str1 != null)
    {
      ArrayList localArrayList = new ArrayList(0);
      String str2 = null;
      if (paramConference != null) {
        str2 = (String)mIdByConference.get(paramConference);
      }
      int i = paramConnection.getState();
      int j = paramConnection.getConnectionCapabilities();
      int k = paramConnection.getConnectionProperties();
      int m = paramConnection.getSupportedAudioRoutes();
      Uri localUri = paramConnection.getAddress();
      int n = paramConnection.getAddressPresentation();
      String str3 = paramConnection.getCallerDisplayName();
      int i1 = paramConnection.getCallerDisplayNamePresentation();
      if (paramConnection.getVideoProvider() == null) {}
      for (paramConference = null;; paramConference = paramConnection.getVideoProvider().getInterface()) {
        break;
      }
      paramPhoneAccountHandle = new ParcelableConnection(paramPhoneAccountHandle, i, j, k, m, localUri, n, str3, i1, paramConference, paramConnection.getVideoState(), paramConnection.isRingbackRequested(), paramConnection.getAudioModeIsVoip(), paramConnection.getConnectTimeMillis(), paramConnection.getConnectElapsedTimeMillis(), paramConnection.getStatusHints(), paramConnection.getDisconnectCause(), localArrayList, paramConnection.getExtras(), str2);
      mAdapter.addExistingConnection(str1, paramPhoneAccountHandle);
    }
  }
  
  void addRemoteConference(RemoteConference paramRemoteConference)
  {
    onRemoteConferenceAdded(paramRemoteConference);
  }
  
  void addRemoteExistingConnection(RemoteConnection paramRemoteConnection)
  {
    onRemoteExistingConnectionAdded(paramRemoteConnection);
  }
  
  public final void conferenceRemoteConnections(RemoteConnection paramRemoteConnection1, RemoteConnection paramRemoteConnection2)
  {
    mRemoteConnectionManager.conferenceRemoteConnections(paramRemoteConnection1, paramRemoteConnection2);
  }
  
  public final void connectionServiceFocusReleased()
  {
    mAdapter.onConnectionServiceFocusReleased();
  }
  
  public boolean containsConference(Conference paramConference)
  {
    return mIdByConference.containsKey(paramConference);
  }
  
  public final RemoteConnection createRemoteIncomingConnection(PhoneAccountHandle paramPhoneAccountHandle, ConnectionRequest paramConnectionRequest)
  {
    return mRemoteConnectionManager.createRemoteConnection(paramPhoneAccountHandle, paramConnectionRequest, true);
  }
  
  public final RemoteConnection createRemoteOutgoingConnection(PhoneAccountHandle paramPhoneAccountHandle, ConnectionRequest paramConnectionRequest)
  {
    return mRemoteConnectionManager.createRemoteConnection(paramPhoneAccountHandle, paramConnectionRequest, false);
  }
  
  public final Collection<Conference> getAllConferences()
  {
    return mConferenceById.values();
  }
  
  public final Collection<Connection> getAllConnections()
  {
    return mConnectionById.values();
  }
  
  public void onAddParticipant(Connection paramConnection, String paramString) {}
  
  public final IBinder onBind(Intent paramIntent)
  {
    return mBinder;
  }
  
  public void onConference(Connection paramConnection1, Connection paramConnection2) {}
  
  public void onConferenceAdded(Conference paramConference) {}
  
  public void onConferenceRemoved(Conference paramConference) {}
  
  public void onConnectionAdded(Connection paramConnection) {}
  
  public void onConnectionRemoved(Connection paramConnection) {}
  
  public void onConnectionServiceFocusGained() {}
  
  public void onConnectionServiceFocusLost() {}
  
  public void onCreateConnectionComplete(Connection paramConnection) {}
  
  public Connection onCreateIncomingConnection(PhoneAccountHandle paramPhoneAccountHandle, ConnectionRequest paramConnectionRequest)
  {
    return null;
  }
  
  public void onCreateIncomingConnectionFailed(PhoneAccountHandle paramPhoneAccountHandle, ConnectionRequest paramConnectionRequest) {}
  
  public Connection onCreateIncomingHandoverConnection(PhoneAccountHandle paramPhoneAccountHandle, ConnectionRequest paramConnectionRequest)
  {
    return null;
  }
  
  public Connection onCreateOutgoingConnection(PhoneAccountHandle paramPhoneAccountHandle, ConnectionRequest paramConnectionRequest)
  {
    return null;
  }
  
  public void onCreateOutgoingConnectionFailed(PhoneAccountHandle paramPhoneAccountHandle, ConnectionRequest paramConnectionRequest) {}
  
  public Connection onCreateOutgoingHandoverConnection(PhoneAccountHandle paramPhoneAccountHandle, ConnectionRequest paramConnectionRequest)
  {
    return null;
  }
  
  public Connection onCreateUnknownConnection(PhoneAccountHandle paramPhoneAccountHandle, ConnectionRequest paramConnectionRequest)
  {
    return null;
  }
  
  public void onHandoverFailed(ConnectionRequest paramConnectionRequest, int paramInt) {}
  
  public void onRemoteConferenceAdded(RemoteConference paramRemoteConference) {}
  
  public void onRemoteExistingConnectionAdded(RemoteConnection paramRemoteConnection) {}
  
  public boolean onUnbind(Intent paramIntent)
  {
    endAllConnections();
    return super.onUnbind(paramIntent);
  }
  
  protected void removeConnection(Connection paramConnection)
  {
    paramConnection.unsetConnectionService(this);
    paramConnection.removeConnectionListener(mConnectionListener);
    String str = (String)mIdByConnection.get(paramConnection);
    if (str != null)
    {
      mConnectionById.remove(str);
      mIdByConnection.remove(paramConnection);
      mAdapter.removeCall(str);
      onConnectionRemoved(paramConnection);
    }
  }
  
  public void triggerConferenceRecalculate() {}
}
