package android.telecom;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.telecom.Logging.Session.Info;
import com.android.internal.os.SomeArgs;
import com.android.internal.telecom.IConnectionServiceAdapter;
import com.android.internal.telecom.IConnectionServiceAdapter.Stub;
import com.android.internal.telecom.IVideoProvider;
import com.android.internal.telecom.RemoteServiceCallback;
import java.util.List;

final class ConnectionServiceAdapterServant
{
  private static final int MSG_ADD_CONFERENCE_CALL = 10;
  private static final int MSG_ADD_EXISTING_CONNECTION = 21;
  private static final int MSG_CONNECTION_SERVICE_FOCUS_RELEASED = 35;
  private static final int MSG_HANDLE_CREATE_CONNECTION_COMPLETE = 1;
  private static final int MSG_ON_CONNECTION_EVENT = 26;
  private static final int MSG_ON_POST_DIAL_CHAR = 22;
  private static final int MSG_ON_POST_DIAL_WAIT = 12;
  private static final int MSG_ON_RTT_INITIATION_FAILURE = 31;
  private static final int MSG_ON_RTT_INITIATION_SUCCESS = 30;
  private static final int MSG_ON_RTT_REMOTELY_TERMINATED = 32;
  private static final int MSG_ON_RTT_UPGRADE_REQUEST = 33;
  private static final int MSG_PUT_EXTRAS = 24;
  private static final int MSG_QUERY_REMOTE_CALL_SERVICES = 13;
  private static final int MSG_REMOVE_CALL = 11;
  private static final int MSG_REMOVE_EXTRAS = 25;
  private static final int MSG_SET_ACTIVE = 2;
  private static final int MSG_SET_ADDRESS = 18;
  private static final int MSG_SET_AUDIO_ROUTE = 29;
  private static final int MSG_SET_CALLER_DISPLAY_NAME = 19;
  private static final int MSG_SET_CONFERENCEABLE_CONNECTIONS = 20;
  private static final int MSG_SET_CONFERENCE_MERGE_FAILED = 23;
  private static final int MSG_SET_CONNECTION_CAPABILITIES = 8;
  private static final int MSG_SET_CONNECTION_PROPERTIES = 27;
  private static final int MSG_SET_DIALING = 4;
  private static final int MSG_SET_DISCONNECTED = 5;
  private static final int MSG_SET_IS_CONFERENCED = 9;
  private static final int MSG_SET_IS_VOIP_AUDIO_MODE = 16;
  private static final int MSG_SET_ON_HOLD = 6;
  private static final int MSG_SET_PHONE_ACCOUNT_CHANGED = 34;
  private static final int MSG_SET_PULLING = 28;
  private static final int MSG_SET_RINGBACK_REQUESTED = 7;
  private static final int MSG_SET_RINGING = 3;
  private static final int MSG_SET_STATUS_HINTS = 17;
  private static final int MSG_SET_VIDEO_CALL_PROVIDER = 15;
  private static final int MSG_SET_VIDEO_STATE = 14;
  private final IConnectionServiceAdapter mDelegate;
  private final Handler mHandler = new Handler()
  {
    private void internalHandleMessage(Message paramAnonymousMessage)
      throws RemoteException
    {
      int i = what;
      boolean bool1 = false;
      boolean bool2 = false;
      SomeArgs localSomeArgs1;
      SomeArgs localSomeArgs2;
      String str;
      Object localObject9;
      switch (i)
      {
      default: 
        break;
      case 35: 
        mDelegate.onConnectionServiceFocusReleased(null);
        break;
      case 34: 
        paramAnonymousMessage = (SomeArgs)obj;
      case 33: 
        try
        {
          mDelegate.onPhoneAccountChanged((String)arg1, (PhoneAccountHandle)arg2, null);
          paramAnonymousMessage.recycle();
        }
        finally
        {
          paramAnonymousMessage.recycle();
        }
        break;
      case 32: 
        mDelegate.onRttSessionRemotelyTerminated((String)obj, null);
        break;
      case 31: 
        mDelegate.onRttInitiationFailure((String)obj, arg1, null);
        break;
      case 30: 
        mDelegate.onRttInitiationSuccess((String)obj, null);
        break;
      case 29: 
        localSomeArgs1 = (SomeArgs)obj;
      case 28: 
        try
        {
          mDelegate.setAudioRoute((String)arg1, argi1, (String)arg2, (Session.Info)arg3);
          localSomeArgs1.recycle();
        }
        finally
        {
          localSomeArgs1.recycle();
        }
        break;
      case 27: 
        mDelegate.setConnectionProperties((String)obj, arg1, null);
        break;
      case 26: 
        localSomeArgs1 = (SomeArgs)obj;
      case 25: 
        try
        {
          mDelegate.onConnectionEvent((String)arg1, (String)arg2, (Bundle)arg3, null);
          localSomeArgs1.recycle();
        }
        finally
        {
          localSomeArgs1.recycle();
        }
      case 24: 
        try
        {
          mDelegate.removeExtras((String)arg1, (List)arg2, null);
          localSomeArgs1.recycle();
        }
        finally
        {
          localSomeArgs1.recycle();
        }
      case 23: 
        try
        {
          mDelegate.putExtras((String)arg1, (Bundle)arg2, null);
          paramAnonymousMessage.recycle();
        }
        finally
        {
          paramAnonymousMessage.recycle();
        }
      case 22: 
        try
        {
          mDelegate.setConferenceMergeFailed((String)arg1, null);
          paramAnonymousMessage.recycle();
        }
        finally
        {
          paramAnonymousMessage.recycle();
        }
      case 21: 
        try
        {
          mDelegate.onPostDialChar((String)arg1, (char)argi1, null);
          paramAnonymousMessage.recycle();
        }
        finally
        {
          paramAnonymousMessage.recycle();
        }
      case 20: 
        try
        {
          mDelegate.addExistingConnection((String)arg1, (ParcelableConnection)arg2, null);
          localSomeArgs2.recycle();
        }
        finally
        {
          localSomeArgs2.recycle();
        }
      case 19: 
        try
        {
          mDelegate.setConferenceableConnections((String)arg1, (List)arg2, null);
          localSomeArgs2.recycle();
        }
        finally
        {
          localSomeArgs2.recycle();
        }
      case 18: 
        try
        {
          mDelegate.setCallerDisplayName((String)arg1, (String)arg2, argi1, null);
          paramAnonymousMessage.recycle();
        }
        finally
        {
          paramAnonymousMessage.recycle();
        }
      case 17: 
        try
        {
          mDelegate.setAddress((String)arg1, (Uri)arg2, argi1, null);
          paramAnonymousMessage.recycle();
        }
        finally
        {
          paramAnonymousMessage.recycle();
        }
      case 16: 
        IConnectionServiceAdapter localIConnectionServiceAdapter;
        try
        {
          mDelegate.setStatusHints((String)arg1, (StatusHints)arg2, null);
          paramAnonymousMessage.recycle();
        }
        finally
        {
          paramAnonymousMessage.recycle();
        }
        str = (String)obj;
        if (arg1 == 1) {
          bool2 = true;
        }
        localIConnectionServiceAdapter.setIsVoipAudioMode(str, bool2, null);
        break;
      case 15: 
        paramAnonymousMessage = (SomeArgs)obj;
      case 14: 
        try
        {
          mDelegate.setVideoProvider((String)arg1, (IVideoProvider)arg2, null);
          paramAnonymousMessage.recycle();
        }
        finally
        {
          paramAnonymousMessage.recycle();
        }
        break;
      case 13: 
        mDelegate.queryRemoteConnectionServices((RemoteServiceCallback)obj, null);
        break;
      case 12: 
        localObject9 = (SomeArgs)obj;
      case 11: 
        try
        {
          mDelegate.onPostDialWait((String)arg1, (String)arg2, null);
          ((SomeArgs)localObject9).recycle();
        }
        finally
        {
          ((SomeArgs)localObject9).recycle();
        }
        break;
      case 10: 
        localObject9 = (SomeArgs)obj;
      case 9: 
        try
        {
          mDelegate.addConferenceCall((String)arg1, (ParcelableConference)arg2, null);
          ((SomeArgs)localObject9).recycle();
        }
        finally
        {
          ((SomeArgs)localObject9).recycle();
        }
      case 8: 
        try
        {
          mDelegate.setIsConferenced((String)arg1, (String)arg2, null);
          ((SomeArgs)localObject9).recycle();
        }
        finally
        {
          ((SomeArgs)localObject9).recycle();
        }
        break;
      case 7: 
        localObject9 = mDelegate;
        str = (String)obj;
        bool2 = bool1;
        if (arg1 == 1) {
          bool2 = true;
        }
        ((IConnectionServiceAdapter)localObject9).setRingbackRequested(str, bool2, null);
        break;
      case 6: 
        mDelegate.setOnHold((String)obj, null);
        break;
      case 5: 
        paramAnonymousMessage = (SomeArgs)obj;
      case 4: 
        try
        {
          mDelegate.setDisconnected((String)arg1, (DisconnectCause)arg2, null);
          paramAnonymousMessage.recycle();
        }
        finally
        {
          paramAnonymousMessage.recycle();
        }
        break;
      case 3: 
        mDelegate.setRinging((String)obj, null);
        break;
      case 2: 
        mDelegate.setActive((String)obj, null);
        break;
      case 1: 
        SomeArgs localSomeArgs3 = (SomeArgs)obj;
        try
        {
          mDelegate.handleCreateConnectionComplete((String)arg1, (ConnectionRequest)arg2, (ParcelableConnection)arg3, null);
          localSomeArgs3.recycle();
        }
        finally
        {
          localSomeArgs3.recycle();
        }
      }
    }
    
    public void handleMessage(Message paramAnonymousMessage)
    {
      try
      {
        internalHandleMessage(paramAnonymousMessage);
      }
      catch (RemoteException paramAnonymousMessage) {}
    }
  };
  private final IConnectionServiceAdapter mStub = new IConnectionServiceAdapter.Stub()
  {
    public void addConferenceCall(String paramAnonymousString, ParcelableConference paramAnonymousParcelableConference, Session.Info paramAnonymousInfo)
    {
      paramAnonymousInfo = SomeArgs.obtain();
      arg1 = paramAnonymousString;
      arg2 = paramAnonymousParcelableConference;
      mHandler.obtainMessage(10, paramAnonymousInfo).sendToTarget();
    }
    
    public final void addExistingConnection(String paramAnonymousString, ParcelableConnection paramAnonymousParcelableConnection, Session.Info paramAnonymousInfo)
    {
      paramAnonymousInfo = SomeArgs.obtain();
      arg1 = paramAnonymousString;
      arg2 = paramAnonymousParcelableConnection;
      mHandler.obtainMessage(21, paramAnonymousInfo).sendToTarget();
    }
    
    public void handleCreateConnectionComplete(String paramAnonymousString, ConnectionRequest paramAnonymousConnectionRequest, ParcelableConnection paramAnonymousParcelableConnection, Session.Info paramAnonymousInfo)
    {
      paramAnonymousInfo = SomeArgs.obtain();
      arg1 = paramAnonymousString;
      arg2 = paramAnonymousConnectionRequest;
      arg3 = paramAnonymousParcelableConnection;
      mHandler.obtainMessage(1, paramAnonymousInfo).sendToTarget();
    }
    
    public final void onConnectionEvent(String paramAnonymousString1, String paramAnonymousString2, Bundle paramAnonymousBundle, Session.Info paramAnonymousInfo)
    {
      paramAnonymousInfo = SomeArgs.obtain();
      arg1 = paramAnonymousString1;
      arg2 = paramAnonymousString2;
      arg3 = paramAnonymousBundle;
      mHandler.obtainMessage(26, paramAnonymousInfo).sendToTarget();
    }
    
    public void onConnectionServiceFocusReleased(Session.Info paramAnonymousInfo)
    {
      mHandler.obtainMessage(35).sendToTarget();
    }
    
    public void onPhoneAccountChanged(String paramAnonymousString, PhoneAccountHandle paramAnonymousPhoneAccountHandle, Session.Info paramAnonymousInfo)
    {
      paramAnonymousInfo = SomeArgs.obtain();
      arg1 = paramAnonymousString;
      arg2 = paramAnonymousPhoneAccountHandle;
      mHandler.obtainMessage(34, paramAnonymousInfo).sendToTarget();
    }
    
    public void onPostDialChar(String paramAnonymousString, char paramAnonymousChar, Session.Info paramAnonymousInfo)
    {
      paramAnonymousInfo = SomeArgs.obtain();
      arg1 = paramAnonymousString;
      argi1 = paramAnonymousChar;
      mHandler.obtainMessage(22, paramAnonymousInfo).sendToTarget();
    }
    
    public void onPostDialWait(String paramAnonymousString1, String paramAnonymousString2, Session.Info paramAnonymousInfo)
    {
      paramAnonymousInfo = SomeArgs.obtain();
      arg1 = paramAnonymousString1;
      arg2 = paramAnonymousString2;
      mHandler.obtainMessage(12, paramAnonymousInfo).sendToTarget();
    }
    
    public void onRemoteRttRequest(String paramAnonymousString, Session.Info paramAnonymousInfo)
      throws RemoteException
    {
      mHandler.obtainMessage(33, paramAnonymousString).sendToTarget();
    }
    
    public void onRttInitiationFailure(String paramAnonymousString, int paramAnonymousInt, Session.Info paramAnonymousInfo)
      throws RemoteException
    {
      mHandler.obtainMessage(31, paramAnonymousInt, 0, paramAnonymousString).sendToTarget();
    }
    
    public void onRttInitiationSuccess(String paramAnonymousString, Session.Info paramAnonymousInfo)
      throws RemoteException
    {
      mHandler.obtainMessage(30, paramAnonymousString).sendToTarget();
    }
    
    public void onRttSessionRemotelyTerminated(String paramAnonymousString, Session.Info paramAnonymousInfo)
      throws RemoteException
    {
      mHandler.obtainMessage(32, paramAnonymousString).sendToTarget();
    }
    
    public final void putExtras(String paramAnonymousString, Bundle paramAnonymousBundle, Session.Info paramAnonymousInfo)
    {
      paramAnonymousInfo = SomeArgs.obtain();
      arg1 = paramAnonymousString;
      arg2 = paramAnonymousBundle;
      mHandler.obtainMessage(24, paramAnonymousInfo).sendToTarget();
    }
    
    public void queryRemoteConnectionServices(RemoteServiceCallback paramAnonymousRemoteServiceCallback, Session.Info paramAnonymousInfo)
    {
      mHandler.obtainMessage(13, paramAnonymousRemoteServiceCallback).sendToTarget();
    }
    
    public void removeCall(String paramAnonymousString, Session.Info paramAnonymousInfo)
    {
      mHandler.obtainMessage(11, paramAnonymousString).sendToTarget();
    }
    
    public final void removeExtras(String paramAnonymousString, List<String> paramAnonymousList, Session.Info paramAnonymousInfo)
    {
      paramAnonymousInfo = SomeArgs.obtain();
      arg1 = paramAnonymousString;
      arg2 = paramAnonymousList;
      mHandler.obtainMessage(25, paramAnonymousInfo).sendToTarget();
    }
    
    public void resetCdmaConnectionTime(String paramAnonymousString, Session.Info paramAnonymousInfo) {}
    
    public void setActive(String paramAnonymousString, Session.Info paramAnonymousInfo)
    {
      mHandler.obtainMessage(2, paramAnonymousString).sendToTarget();
    }
    
    public final void setAddress(String paramAnonymousString, Uri paramAnonymousUri, int paramAnonymousInt, Session.Info paramAnonymousInfo)
    {
      paramAnonymousInfo = SomeArgs.obtain();
      arg1 = paramAnonymousString;
      arg2 = paramAnonymousUri;
      argi1 = paramAnonymousInt;
      mHandler.obtainMessage(18, paramAnonymousInfo).sendToTarget();
    }
    
    public final void setAudioRoute(String paramAnonymousString1, int paramAnonymousInt, String paramAnonymousString2, Session.Info paramAnonymousInfo)
    {
      SomeArgs localSomeArgs = SomeArgs.obtain();
      arg1 = paramAnonymousString1;
      argi1 = paramAnonymousInt;
      arg2 = paramAnonymousString2;
      arg3 = paramAnonymousInfo;
      mHandler.obtainMessage(29, localSomeArgs).sendToTarget();
    }
    
    public final void setCallerDisplayName(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt, Session.Info paramAnonymousInfo)
    {
      paramAnonymousInfo = SomeArgs.obtain();
      arg1 = paramAnonymousString1;
      arg2 = paramAnonymousString2;
      argi1 = paramAnonymousInt;
      mHandler.obtainMessage(19, paramAnonymousInfo).sendToTarget();
    }
    
    public void setConferenceMergeFailed(String paramAnonymousString, Session.Info paramAnonymousInfo)
    {
      paramAnonymousInfo = SomeArgs.obtain();
      arg1 = paramAnonymousString;
      mHandler.obtainMessage(23, paramAnonymousInfo).sendToTarget();
    }
    
    public final void setConferenceableConnections(String paramAnonymousString, List<String> paramAnonymousList, Session.Info paramAnonymousInfo)
    {
      paramAnonymousInfo = SomeArgs.obtain();
      arg1 = paramAnonymousString;
      arg2 = paramAnonymousList;
      mHandler.obtainMessage(20, paramAnonymousInfo).sendToTarget();
    }
    
    public void setConnectionCapabilities(String paramAnonymousString, int paramAnonymousInt, Session.Info paramAnonymousInfo)
    {
      mHandler.obtainMessage(8, paramAnonymousInt, 0, paramAnonymousString).sendToTarget();
    }
    
    public void setConnectionProperties(String paramAnonymousString, int paramAnonymousInt, Session.Info paramAnonymousInfo)
    {
      mHandler.obtainMessage(27, paramAnonymousInt, 0, paramAnonymousString).sendToTarget();
    }
    
    public void setDialing(String paramAnonymousString, Session.Info paramAnonymousInfo)
    {
      mHandler.obtainMessage(4, paramAnonymousString).sendToTarget();
    }
    
    public void setDisconnected(String paramAnonymousString, DisconnectCause paramAnonymousDisconnectCause, Session.Info paramAnonymousInfo)
    {
      paramAnonymousInfo = SomeArgs.obtain();
      arg1 = paramAnonymousString;
      arg2 = paramAnonymousDisconnectCause;
      mHandler.obtainMessage(5, paramAnonymousInfo).sendToTarget();
    }
    
    public void setIsConferenced(String paramAnonymousString1, String paramAnonymousString2, Session.Info paramAnonymousInfo)
    {
      paramAnonymousInfo = SomeArgs.obtain();
      arg1 = paramAnonymousString1;
      arg2 = paramAnonymousString2;
      mHandler.obtainMessage(9, paramAnonymousInfo).sendToTarget();
    }
    
    public final void setIsVoipAudioMode(String paramAnonymousString, boolean paramAnonymousBoolean, Session.Info paramAnonymousInfo)
    {
      mHandler.obtainMessage(16, paramAnonymousBoolean, 0, paramAnonymousString).sendToTarget();
    }
    
    public void setOnHold(String paramAnonymousString, Session.Info paramAnonymousInfo)
    {
      mHandler.obtainMessage(6, paramAnonymousString).sendToTarget();
    }
    
    public void setPulling(String paramAnonymousString, Session.Info paramAnonymousInfo)
    {
      mHandler.obtainMessage(28, paramAnonymousString).sendToTarget();
    }
    
    public void setRingbackRequested(String paramAnonymousString, boolean paramAnonymousBoolean, Session.Info paramAnonymousInfo)
    {
      mHandler.obtainMessage(7, paramAnonymousBoolean, 0, paramAnonymousString).sendToTarget();
    }
    
    public void setRinging(String paramAnonymousString, Session.Info paramAnonymousInfo)
    {
      mHandler.obtainMessage(3, paramAnonymousString).sendToTarget();
    }
    
    public final void setStatusHints(String paramAnonymousString, StatusHints paramAnonymousStatusHints, Session.Info paramAnonymousInfo)
    {
      paramAnonymousInfo = SomeArgs.obtain();
      arg1 = paramAnonymousString;
      arg2 = paramAnonymousStatusHints;
      mHandler.obtainMessage(17, paramAnonymousInfo).sendToTarget();
    }
    
    public void setVideoProvider(String paramAnonymousString, IVideoProvider paramAnonymousIVideoProvider, Session.Info paramAnonymousInfo)
    {
      paramAnonymousInfo = SomeArgs.obtain();
      arg1 = paramAnonymousString;
      arg2 = paramAnonymousIVideoProvider;
      mHandler.obtainMessage(15, paramAnonymousInfo).sendToTarget();
    }
    
    public void setVideoState(String paramAnonymousString, int paramAnonymousInt, Session.Info paramAnonymousInfo)
    {
      mHandler.obtainMessage(14, paramAnonymousInt, 0, paramAnonymousString).sendToTarget();
    }
  };
  
  public ConnectionServiceAdapterServant(IConnectionServiceAdapter paramIConnectionServiceAdapter)
  {
    mDelegate = paramIConnectionServiceAdapter;
  }
  
  public IConnectionServiceAdapter getStub()
  {
    return mStub;
  }
}
