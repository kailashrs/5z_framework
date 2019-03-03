package com.android.internal.telephony.imsphone;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.telecom.VideoProfile;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsExternalCallState;
import android.util.ArrayMap;
import android.util.Log;
import com.android.ims.ImsExternalCallStateListener;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.Call.State;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants.State;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ImsExternalCallTracker
  implements ImsPhoneCallTracker.PhoneStateListener
{
  private static final int EVENT_VIDEO_CAPABILITIES_CHANGED = 1;
  public static final String EXTRA_IMS_EXTERNAL_CALL_ID = "android.telephony.ImsExternalCallTracker.extra.EXTERNAL_CALL_ID";
  public static final String TAG = "ImsExternalCallTracker";
  private ImsPullCall mCallPuller;
  private final ImsCallNotify mCallStateNotifier;
  private Map<Integer, Boolean> mExternalCallPullableState = new ArrayMap();
  private final ExternalCallStateListener mExternalCallStateListener;
  private final ExternalConnectionListener mExternalConnectionListener = new ExternalConnectionListener();
  private Map<Integer, ImsExternalConnection> mExternalConnections = new ArrayMap();
  private final Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      if (what == 1) {
        ImsExternalCallTracker.this.handleVideoCapabilitiesChanged((AsyncResult)obj);
      }
    }
  };
  private boolean mHasActiveCalls;
  private boolean mIsVideoCapable;
  private final ImsPhone mPhone;
  
  public ImsExternalCallTracker(ImsPhone paramImsPhone)
  {
    mPhone = paramImsPhone;
    mCallStateNotifier = new ImsCallNotify()
    {
      public void notifyPreciseCallStateChanged()
      {
        mPhone.notifyPreciseCallStateChanged();
      }
      
      public void notifyUnknownConnection(Connection paramAnonymousConnection)
      {
        mPhone.notifyUnknownConnection(paramAnonymousConnection);
      }
    };
    mExternalCallStateListener = new ExternalCallStateListener();
    registerForNotifications();
  }
  
  @VisibleForTesting
  public ImsExternalCallTracker(ImsPhone paramImsPhone, ImsPullCall paramImsPullCall, ImsCallNotify paramImsCallNotify)
  {
    mPhone = paramImsPhone;
    mCallStateNotifier = paramImsCallNotify;
    mExternalCallStateListener = new ExternalCallStateListener();
    mCallPuller = paramImsPullCall;
  }
  
  private boolean containsCallId(List<ImsExternalCallState> paramList, int paramInt)
  {
    if (paramList == null) {
      return false;
    }
    paramList = paramList.iterator();
    while (paramList.hasNext()) {
      if (((ImsExternalCallState)paramList.next()).getCallId() == paramInt) {
        return true;
      }
    }
    return false;
  }
  
  private void createExternalConnection(ImsExternalCallState paramImsExternalCallState)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("createExternalConnection : state = ");
    ((StringBuilder)localObject).append(paramImsExternalCallState);
    Log.i("ImsExternalCallTracker", ((StringBuilder)localObject).toString());
    int i = ImsCallProfile.getVideoStateFromCallType(paramImsExternalCallState.getCallType());
    boolean bool = isCallPullPermitted(paramImsExternalCallState.isCallPullable(), i);
    localObject = new ImsExternalConnection(mPhone, paramImsExternalCallState.getCallId(), paramImsExternalCallState.getAddress(), bool);
    ((ImsExternalConnection)localObject).setVideoState(i);
    ((ImsExternalConnection)localObject).addListener(mExternalConnectionListener);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("createExternalConnection - pullable state : externalCallId = ");
    localStringBuilder.append(((ImsExternalConnection)localObject).getCallId());
    localStringBuilder.append(" ; isPullable = ");
    localStringBuilder.append(bool);
    localStringBuilder.append(" ; networkPullable = ");
    localStringBuilder.append(paramImsExternalCallState.isCallPullable());
    localStringBuilder.append(" ; isVideo = ");
    localStringBuilder.append(VideoProfile.isVideo(i));
    localStringBuilder.append(" ; videoEnabled = ");
    localStringBuilder.append(mIsVideoCapable);
    localStringBuilder.append(" ; hasActiveCalls = ");
    localStringBuilder.append(mHasActiveCalls);
    Log.d("ImsExternalCallTracker", localStringBuilder.toString());
    mExternalConnections.put(Integer.valueOf(((ImsExternalConnection)localObject).getCallId()), localObject);
    mExternalCallPullableState.put(Integer.valueOf(((ImsExternalConnection)localObject).getCallId()), Boolean.valueOf(paramImsExternalCallState.isCallPullable()));
    mCallStateNotifier.notifyUnknownConnection((Connection)localObject);
  }
  
  private void handleVideoCapabilitiesChanged(AsyncResult paramAsyncResult)
  {
    mIsVideoCapable = ((Boolean)result).booleanValue();
    paramAsyncResult = new StringBuilder();
    paramAsyncResult.append("handleVideoCapabilitiesChanged : isVideoCapable = ");
    paramAsyncResult.append(mIsVideoCapable);
    Log.i("ImsExternalCallTracker", paramAsyncResult.toString());
    refreshCallPullState();
  }
  
  private boolean isCallPullPermitted(boolean paramBoolean, int paramInt)
  {
    if ((VideoProfile.isVideo(paramInt)) && (!mIsVideoCapable)) {
      return false;
    }
    if (mHasActiveCalls) {
      return false;
    }
    return paramBoolean;
  }
  
  private void refreshCallPullState()
  {
    Log.d("ImsExternalCallTracker", "refreshCallPullState");
    Iterator localIterator = mExternalConnections.values().iterator();
    while (localIterator.hasNext())
    {
      ImsExternalConnection localImsExternalConnection = (ImsExternalConnection)localIterator.next();
      boolean bool1 = ((Boolean)mExternalCallPullableState.get(Integer.valueOf(localImsExternalConnection.getCallId()))).booleanValue();
      boolean bool2 = isCallPullPermitted(bool1, localImsExternalConnection.getVideoState());
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("refreshCallPullState : externalCallId = ");
      localStringBuilder.append(localImsExternalConnection.getCallId());
      localStringBuilder.append(" ; isPullable = ");
      localStringBuilder.append(bool2);
      localStringBuilder.append(" ; networkPullable = ");
      localStringBuilder.append(bool1);
      localStringBuilder.append(" ; isVideo = ");
      localStringBuilder.append(VideoProfile.isVideo(localImsExternalConnection.getVideoState()));
      localStringBuilder.append(" ; videoEnabled = ");
      localStringBuilder.append(mIsVideoCapable);
      localStringBuilder.append(" ; hasActiveCalls = ");
      localStringBuilder.append(mHasActiveCalls);
      Log.d("ImsExternalCallTracker", localStringBuilder.toString());
      localImsExternalConnection.setIsPullable(bool2);
    }
  }
  
  private void registerForNotifications()
  {
    if (mPhone != null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Registering: ");
      localStringBuilder.append(mPhone);
      Log.d("ImsExternalCallTracker", localStringBuilder.toString());
      mPhone.getDefaultPhone().registerForVideoCapabilityChanged(mHandler, 1, null);
    }
  }
  
  private void unregisterForNotifications()
  {
    if (mPhone != null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unregistering: ");
      localStringBuilder.append(mPhone);
      Log.d("ImsExternalCallTracker", localStringBuilder.toString());
      mPhone.unregisterForVideoCapabilityChanged(mHandler);
    }
  }
  
  private void updateExistingConnection(ImsExternalConnection paramImsExternalConnection, ImsExternalCallState paramImsExternalCallState)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("updateExistingConnection : state = ");
    ((StringBuilder)localObject).append(paramImsExternalCallState);
    Log.i("ImsExternalCallTracker", ((StringBuilder)localObject).toString());
    Call.State localState = paramImsExternalConnection.getState();
    if (paramImsExternalCallState.getCallState() == 1) {
      localObject = Call.State.ACTIVE;
    } else {
      localObject = Call.State.DISCONNECTED;
    }
    if (localState != localObject) {
      if (localObject == Call.State.ACTIVE)
      {
        paramImsExternalConnection.setActive();
      }
      else
      {
        paramImsExternalConnection.setTerminated();
        paramImsExternalConnection.removeListener(mExternalConnectionListener);
        mExternalConnections.remove(Integer.valueOf(paramImsExternalConnection.getCallId()));
        mExternalCallPullableState.remove(Integer.valueOf(paramImsExternalConnection.getCallId()));
        mCallStateNotifier.notifyPreciseCallStateChanged();
      }
    }
    int i = ImsCallProfile.getVideoStateFromCallType(paramImsExternalCallState.getCallType());
    if (i != paramImsExternalConnection.getVideoState()) {
      paramImsExternalConnection.setVideoState(i);
    }
    mExternalCallPullableState.put(Integer.valueOf(paramImsExternalCallState.getCallId()), Boolean.valueOf(paramImsExternalCallState.isCallPullable()));
    boolean bool = isCallPullPermitted(paramImsExternalCallState.isCallPullable(), i);
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("updateExistingConnection - pullable state : externalCallId = ");
    ((StringBuilder)localObject).append(paramImsExternalConnection.getCallId());
    ((StringBuilder)localObject).append(" ; isPullable = ");
    ((StringBuilder)localObject).append(bool);
    ((StringBuilder)localObject).append(" ; networkPullable = ");
    ((StringBuilder)localObject).append(paramImsExternalCallState.isCallPullable());
    ((StringBuilder)localObject).append(" ; isVideo = ");
    ((StringBuilder)localObject).append(VideoProfile.isVideo(paramImsExternalConnection.getVideoState()));
    ((StringBuilder)localObject).append(" ; videoEnabled = ");
    ((StringBuilder)localObject).append(mIsVideoCapable);
    ((StringBuilder)localObject).append(" ; hasActiveCalls = ");
    ((StringBuilder)localObject).append(mHasActiveCalls);
    Log.d("ImsExternalCallTracker", ((StringBuilder)localObject).toString());
    paramImsExternalConnection.setIsPullable(bool);
  }
  
  public Connection getConnectionById(int paramInt)
  {
    return (Connection)mExternalConnections.get(Integer.valueOf(paramInt));
  }
  
  public ExternalCallStateListener getExternalCallStateListener()
  {
    return mExternalCallStateListener;
  }
  
  public void onPhoneStateChanged(PhoneConstants.State paramState1, PhoneConstants.State paramState2)
  {
    boolean bool;
    if (paramState2 != PhoneConstants.State.IDLE) {
      bool = true;
    } else {
      bool = false;
    }
    mHasActiveCalls = bool;
    paramState1 = new StringBuilder();
    paramState1.append("onPhoneStateChanged : hasActiveCalls = ");
    paramState1.append(mHasActiveCalls);
    Log.i("ImsExternalCallTracker", paramState1.toString());
    refreshCallPullState();
  }
  
  public void refreshExternalCallState(List<ImsExternalCallState> paramList)
  {
    Log.d("ImsExternalCallTracker", "refreshExternalCallState");
    Iterator localIterator = mExternalConnections.entrySet().iterator();
    int i = 0;
    Object localObject;
    while (localIterator.hasNext())
    {
      localObject = (Map.Entry)localIterator.next();
      if (!containsCallId(paramList, ((Integer)((Map.Entry)localObject).getKey()).intValue()))
      {
        localObject = (ImsExternalConnection)((Map.Entry)localObject).getValue();
        ((ImsExternalConnection)localObject).setTerminated();
        ((ImsExternalConnection)localObject).removeListener(mExternalConnectionListener);
        localIterator.remove();
        i = 1;
      }
    }
    if (i != 0) {
      mCallStateNotifier.notifyPreciseCallStateChanged();
    }
    if ((paramList != null) && (!paramList.isEmpty()))
    {
      localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        localObject = (ImsExternalCallState)localIterator.next();
        if (!mExternalConnections.containsKey(Integer.valueOf(((ImsExternalCallState)localObject).getCallId())))
        {
          paramList = new StringBuilder();
          paramList.append("refreshExternalCallState: got = ");
          paramList.append(localObject);
          Log.d("ImsExternalCallTracker", paramList.toString());
          if (((ImsExternalCallState)localObject).getCallState() == 1) {
            createExternalConnection((ImsExternalCallState)localObject);
          }
        }
        else
        {
          updateExistingConnection((ImsExternalConnection)mExternalConnections.get(Integer.valueOf(((ImsExternalCallState)localObject).getCallId())), (ImsExternalCallState)localObject);
        }
      }
    }
  }
  
  public void setCallPuller(ImsPullCall paramImsPullCall)
  {
    mCallPuller = paramImsPullCall;
  }
  
  public void tearDown()
  {
    unregisterForNotifications();
  }
  
  public class ExternalCallStateListener
    extends ImsExternalCallStateListener
  {
    public ExternalCallStateListener() {}
    
    public void onImsExternalCallStateUpdate(List<ImsExternalCallState> paramList)
    {
      refreshExternalCallState(paramList);
    }
  }
  
  public class ExternalConnectionListener
    implements ImsExternalConnection.Listener
  {
    public ExternalConnectionListener() {}
    
    public void onPullExternalCall(ImsExternalConnection paramImsExternalConnection)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onPullExternalCall: connection = ");
      localStringBuilder.append(paramImsExternalConnection);
      Log.d("ImsExternalCallTracker", localStringBuilder.toString());
      if (mCallPuller == null)
      {
        Log.e("ImsExternalCallTracker", "onPullExternalCall : No call puller defined");
        return;
      }
      mCallPuller.pullExternalCall(paramImsExternalConnection.getAddress(), paramImsExternalConnection.getVideoState(), paramImsExternalConnection.getCallId());
    }
  }
  
  public static abstract interface ImsCallNotify
  {
    public abstract void notifyPreciseCallStateChanged();
    
    public abstract void notifyUnknownConnection(Connection paramConnection);
  }
}
