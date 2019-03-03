package com.android.internal.telephony.imsphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest.Builder;
import android.net.NetworkStats;
import android.net.NetworkStats.Entry;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Build.FEATURES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.Registrant;
import android.os.RegistrantList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings.Secure;
import android.telecom.ConferenceParticipant;
import android.telecom.Connection.VideoProvider;
import android.telecom.TelecomManager;
import android.telecom.VideoProfile;
import android.telephony.CarrierConfigManager;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsCallSession;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsStreamMediaProfile;
import android.telephony.ims.ImsSuppServiceNotification;
import android.telephony.ims.feature.ImsFeature.Capabilities;
import android.telephony.ims.feature.ImsFeature.CapabilityCallback;
import android.telephony.ims.feature.MmTelFeature.Listener;
import android.telephony.ims.feature.MmTelFeature.MmTelCapabilities;
import android.telephony.ims.stub.ImsConfigImplBase.Callback;
import android.telephony.ims.stub.ImsRegistrationImplBase.Callback;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import android.util.SparseIntArray;
import com.android.ims.ImsCall;
import com.android.ims.ImsCall.Listener;
import com.android.ims.ImsConfig;
import com.android.ims.ImsConfigListener.Stub;
import com.android.ims.ImsEcbm;
import com.android.ims.ImsException;
import com.android.ims.ImsManager;
import com.android.ims.ImsManager.Connector;
import com.android.ims.ImsManager.Connector.Listener;
import com.android.ims.ImsManager.Connector.RetryTimeout;
import com.android.ims.ImsMultiEndpoint;
import com.android.ims.ImsUtInterface;
import com.android.ims.internal.IImsCallSession;
import com.android.ims.internal.IImsVideoCallProvider;
import com.android.ims.internal.ImsVideoCallProviderWrapper;
import com.android.ims.internal.ImsVideoCallProviderWrapper.ImsVideoProviderWrapperCallback;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.SomeArgs;
import com.android.internal.telephony.Call.SrvccState;
import com.android.internal.telephony.Call.State;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.CallTracker;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandException.Error;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants.State;
import com.android.internal.telephony.PhoneInternalInterface.SuppService;
import com.android.internal.telephony.SubscriptionController;
import com.android.internal.telephony.gsm.SuppServiceNotification;
import com.android.internal.telephony.metrics.TelephonyMetrics;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import org.codeaurora.ims.utils.QtiImsExtUtils;

public class ImsPhoneCallTracker
  extends CallTracker
  implements ImsPullCall
{
  private static final boolean DBG = true;
  private static final int EVENT_CHECK_FOR_WIFI_HANDOVER = 25;
  private static final int EVENT_DATA_ENABLED_CHANGED = 23;
  private static final int EVENT_DIAL_PENDINGMO = 20;
  private static final int EVENT_EXIT_ECBM_BEFORE_PENDINGMO = 21;
  private static final int EVENT_HANGUP_PENDINGMO = 18;
  private static final int EVENT_ON_FEATURE_CAPABILITY_CHANGED = 26;
  private static final int EVENT_RESUME_BACKGROUND = 19;
  private static final int EVENT_SUPP_SERVICE_INDICATION = 27;
  private static final int EVENT_VT_DATA_USAGE_UPDATE = 22;
  private static final boolean FORCE_VERBOSE_STATE_LOGGING = false;
  private static final int HANDOVER_TO_WIFI_TIMEOUT_MS = 60000;
  static final String LOG_TAG = "ImsPhoneCallTracker";
  static final int MAX_CONNECTIONS = 7;
  static final int MAX_CONNECTIONS_PER_CALL = 5;
  private static final SparseIntArray PRECISE_CAUSE_MAP;
  private static final int TIMEOUT_HANGUP_PENDINGMO = 500;
  private static final boolean VERBOSE_STATE_LOGGING = Rlog.isLoggable("IPCTState", 2);
  static final String VERBOSE_STATE_TAG = "IPCTState";
  private boolean mAllowAddCallDuringVideoCall = true;
  private boolean mAllowEmergencyVideoCalls = false;
  private boolean mAllowHoldingVideoCall = true;
  private boolean mAlwaysPlayRemoteHoldTone = false;
  public ImsPhoneCall mBackgroundCall = new ImsPhoneCall(this, "BG");
  private ImsCall mCallExpectedToResume = null;
  private boolean mCarrierConfigLoaded = false;
  private int mClirMode = 0;
  private final ImsConfigImplBase.Callback mConfigCallback = new ImsConfigImplBase.Callback()
  {
    private void sendConfigChangedIntent(int paramAnonymousInt, String paramAnonymousString)
    {
      Object localObject = ImsPhoneCallTracker.this;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("sendConfigChangedIntent - [");
      localStringBuilder.append(paramAnonymousInt);
      localStringBuilder.append(", ");
      localStringBuilder.append(paramAnonymousString);
      localStringBuilder.append("]");
      ((ImsPhoneCallTracker)localObject).log(localStringBuilder.toString());
      localObject = new Intent("com.android.intent.action.IMS_CONFIG_CHANGED");
      ((Intent)localObject).putExtra("item", paramAnonymousInt);
      ((Intent)localObject).putExtra("value", paramAnonymousString);
      if ((mPhone != null) && (mPhone.getContext() != null)) {
        mPhone.getContext().sendBroadcast((Intent)localObject);
      }
    }
    
    public void onConfigChanged(int paramAnonymousInt1, int paramAnonymousInt2)
    {
      sendConfigChangedIntent(paramAnonymousInt1, Integer.toString(paramAnonymousInt2));
    }
    
    public void onConfigChanged(int paramAnonymousInt, String paramAnonymousString)
    {
      sendConfigChangedIntent(paramAnonymousInt, paramAnonymousString);
    }
  };
  private ArrayList<ImsPhoneConnection> mConnections = new ArrayList();
  private final AtomicInteger mDefaultDialerUid = new AtomicInteger(-1);
  private boolean mDesiredMute = false;
  private boolean mDropVideoCallWhenAnsweringAudioCall = false;
  public ImsPhoneCall mForegroundCall = new ImsPhoneCall(this, "FG");
  public ImsPhoneCall mHandoverCall = new ImsPhoneCall(this, "HO");
  private boolean mHasPerformedStartOfCallHandover = false;
  private boolean mIgnoreDataEnabledChangedForVideoCalls = false;
  private boolean mIgnoreResetUtCapability = false;
  private ImsCall.Listener mImsCallListener = new ImsCall.Listener()
  {
    public void onCallHandover(ImsCall paramAnonymousImsCall, int paramAnonymousInt1, int paramAnonymousInt2, ImsReasonInfo paramAnonymousImsReasonInfo)
    {
      Object localObject = ImsPhoneCallTracker.this;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onCallHandover ::  srcAccessTech=");
      localStringBuilder.append(paramAnonymousInt1);
      localStringBuilder.append(", targetAccessTech=");
      localStringBuilder.append(paramAnonymousInt2);
      localStringBuilder.append(", reasonInfo=");
      localStringBuilder.append(paramAnonymousImsReasonInfo);
      ((ImsPhoneCallTracker)localObject).log(localStringBuilder.toString());
      int i = 0;
      int j;
      if ((paramAnonymousInt1 != 0) && (paramAnonymousInt1 != 18) && (paramAnonymousInt2 == 18)) {
        j = 1;
      } else {
        j = 0;
      }
      int k = i;
      if (paramAnonymousInt1 == 18)
      {
        k = i;
        if (paramAnonymousInt2 != 0)
        {
          k = i;
          if (paramAnonymousInt2 != 18) {
            k = 1;
          }
        }
      }
      localObject = ImsPhoneCallTracker.this.findConnection(paramAnonymousImsCall);
      if (localObject != null)
      {
        if (((ImsPhoneConnection)localObject).getDisconnectCause() == 0) {
          if (j != 0)
          {
            removeMessages(25);
            if (mIsViLteDataMetered) {
              ((ImsPhoneConnection)localObject).setVideoEnabled(true);
            }
            if ((mNotifyHandoverVideoFromLTEToWifi) && (mHasPerformedStartOfCallHandover)) {
              ((ImsPhoneConnection)localObject).onConnectionEvent("android.telephony.event.EVENT_HANDOVER_VIDEO_FROM_LTE_TO_WIFI", null);
            }
            ImsPhoneCallTracker.this.unregisterForConnectivityChanges();
          }
          else if ((k != 0) && (paramAnonymousImsCall.isVideoCall()))
          {
            ImsPhoneCallTracker.this.registerForConnectivityChanges();
          }
        }
        if ((k != 0) && (paramAnonymousImsCall.isVideoCall()))
        {
          if (mIsViLteDataMetered) {
            ((ImsPhoneConnection)localObject).setVideoEnabled(mIsDataEnabled);
          }
          if ((mNotifyHandoverVideoFromWifiToLTE) && (mIsDataEnabled)) {
            if (((ImsPhoneConnection)localObject).getDisconnectCause() == 0)
            {
              log("onCallHandover :: notifying of WIFI to LTE handover.");
              ((ImsPhoneConnection)localObject).onConnectionEvent("android.telephony.event.EVENT_HANDOVER_VIDEO_FROM_WIFI_TO_LTE", null);
            }
            else
            {
              log("onCallHandover :: skip notify of WIFI to LTE handover for disconnected call.");
            }
          }
          if ((!mIsDataEnabled) && (mIsViLteDataMetered)) {
            ImsPhoneCallTracker.this.downgradeVideoCall(1407, (ImsPhoneConnection)localObject);
          }
        }
      }
      else
      {
        loge("onCallHandover :: connection null.");
      }
      if (!mHasPerformedStartOfCallHandover) {
        ImsPhoneCallTracker.access$2602(ImsPhoneCallTracker.this, true);
      }
      mMetrics.writeOnImsCallHandoverEvent(mPhone.getPhoneId(), 18, paramAnonymousImsCall.getCallSession(), paramAnonymousInt1, paramAnonymousInt2, paramAnonymousImsReasonInfo);
    }
    
    public void onCallHandoverFailed(ImsCall paramAnonymousImsCall, int paramAnonymousInt1, int paramAnonymousInt2, ImsReasonInfo paramAnonymousImsReasonInfo)
    {
      ImsPhoneCallTracker localImsPhoneCallTracker = ImsPhoneCallTracker.this;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onCallHandoverFailed :: srcAccessTech=");
      localStringBuilder.append(paramAnonymousInt1);
      localStringBuilder.append(", targetAccessTech=");
      localStringBuilder.append(paramAnonymousInt2);
      localStringBuilder.append(", reasonInfo=");
      localStringBuilder.append(paramAnonymousImsReasonInfo);
      localImsPhoneCallTracker.log(localStringBuilder.toString());
      mMetrics.writeOnImsCallHandoverEvent(mPhone.getPhoneId(), 19, paramAnonymousImsCall.getCallSession(), paramAnonymousInt1, paramAnonymousInt2, paramAnonymousImsReasonInfo);
      if ((paramAnonymousInt1 != 18) && (paramAnonymousInt2 == 18)) {
        paramAnonymousInt1 = 1;
      } else {
        paramAnonymousInt1 = 0;
      }
      paramAnonymousImsReasonInfo = ImsPhoneCallTracker.this.findConnection(paramAnonymousImsCall);
      if ((paramAnonymousImsReasonInfo != null) && (paramAnonymousInt1 != 0))
      {
        log("onCallHandoverFailed - handover to WIFI Failed");
        removeMessages(25);
        if ((paramAnonymousImsCall.isVideoCall()) && (paramAnonymousImsReasonInfo.getDisconnectCause() == 0)) {
          ImsPhoneCallTracker.this.registerForConnectivityChanges();
        }
        if (mNotifyVtHandoverToWifiFail) {
          paramAnonymousImsReasonInfo.onHandoverToWifiFailed();
        }
      }
      if (!mHasPerformedStartOfCallHandover) {
        ImsPhoneCallTracker.access$2602(ImsPhoneCallTracker.this, true);
      }
    }
    
    public void onCallHeld(ImsCall paramAnonymousImsCall)
    {
      Object localObject2;
      if (mForegroundCall.getImsCall() == paramAnonymousImsCall)
      {
        ??? = ImsPhoneCallTracker.this;
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("onCallHeld (fg) ");
        ((StringBuilder)localObject2).append(paramAnonymousImsCall);
        ((ImsPhoneCallTracker)???).log(((StringBuilder)localObject2).toString());
      }
      else if (mBackgroundCall.getImsCall() == paramAnonymousImsCall)
      {
        localObject2 = ImsPhoneCallTracker.this;
        ??? = new StringBuilder();
        ((StringBuilder)???).append("onCallHeld (bg) ");
        ((StringBuilder)???).append(paramAnonymousImsCall);
        ((ImsPhoneCallTracker)localObject2).log(((StringBuilder)???).toString());
      }
      synchronized (mSyncHold)
      {
        localObject2 = mBackgroundCall.getState();
        ImsPhoneCallTracker.this.processCallStateChange(paramAnonymousImsCall, Call.State.HOLDING, 0);
        if (localObject2 == Call.State.ACTIVE)
        {
          if ((mForegroundCall.getState() != Call.State.HOLDING) && (mRingingCall.getState() != Call.State.WAITING))
          {
            if (mPendingMO != null) {
              ImsPhoneCallTracker.this.dialPendingMO();
            }
            ImsPhoneCallTracker.access$2102(ImsPhoneCallTracker.this, false);
          }
          else
          {
            sendEmptyMessage(19);
          }
        }
        else if ((localObject2 == Call.State.IDLE) && (mSwitchingFgAndBgCalls) && (mForegroundCall.getState() == Call.State.HOLDING))
        {
          sendEmptyMessage(19);
          ImsPhoneCallTracker.access$2102(ImsPhoneCallTracker.this, false);
          ImsPhoneCallTracker.access$2202(ImsPhoneCallTracker.this, null);
        }
        mMetrics.writeOnImsCallHeld(mPhone.getPhoneId(), paramAnonymousImsCall.getCallSession());
        return;
      }
    }
    
    public void onCallHoldFailed(ImsCall paramAnonymousImsCall, ImsReasonInfo paramAnonymousImsReasonInfo)
    {
      ??? = ImsPhoneCallTracker.this;
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("onCallHoldFailed reasonCode=");
      ((StringBuilder)localObject2).append(paramAnonymousImsReasonInfo.getCode());
      ((ImsPhoneCallTracker)???).log(((StringBuilder)localObject2).toString());
      synchronized (mSyncHold)
      {
        localObject2 = mBackgroundCall.getState();
        if (paramAnonymousImsReasonInfo.getCode() == 148)
        {
          if (mPendingMO != null) {
            ImsPhoneCallTracker.this.dialPendingMO();
          }
        }
        else if (localObject2 == Call.State.ACTIVE)
        {
          mForegroundCall.switchWith(mBackgroundCall);
          if (mPendingMO != null)
          {
            mPendingMO.setDisconnectCause(36);
            sendEmptyMessageDelayed(18, 500L);
          }
          if (paramAnonymousImsCall != mCallExpectedToResume) {
            ImsPhoneCallTracker.access$2202(ImsPhoneCallTracker.this, null);
          }
        }
        mPhone.notifySuppServiceFailed(PhoneInternalInterface.SuppService.HOLD);
        mMetrics.writeOnImsCallHoldFailed(mPhone.getPhoneId(), paramAnonymousImsCall.getCallSession(), paramAnonymousImsReasonInfo);
        return;
      }
    }
    
    public void onCallHoldReceived(ImsCall paramAnonymousImsCall)
    {
      ImsPhoneCallTracker.this.onCallHoldReceived(paramAnonymousImsCall);
    }
    
    public void onCallMergeFailed(ImsCall paramAnonymousImsCall, ImsReasonInfo paramAnonymousImsReasonInfo)
    {
      ImsPhoneCallTracker localImsPhoneCallTracker = ImsPhoneCallTracker.this;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onCallMergeFailed reasonInfo=");
      localStringBuilder.append(paramAnonymousImsReasonInfo);
      localImsPhoneCallTracker.log(localStringBuilder.toString());
      mPhone.notifySuppServiceFailed(PhoneInternalInterface.SuppService.CONFERENCE);
      paramAnonymousImsCall.resetIsMergeRequestedByConf(false);
      paramAnonymousImsCall = ImsPhoneCallTracker.this.findConnection(paramAnonymousImsCall);
      if (paramAnonymousImsCall != null)
      {
        paramAnonymousImsCall.onConferenceMergeFailed();
        paramAnonymousImsCall.handleMergeComplete();
      }
    }
    
    public void onCallMerged(ImsCall paramAnonymousImsCall1, ImsCall paramAnonymousImsCall2, boolean paramAnonymousBoolean)
    {
      log("onCallMerged");
      Object localObject = ImsPhoneCallTracker.this.findConnection(paramAnonymousImsCall1).getCall();
      ImsPhoneConnection localImsPhoneConnection = ImsPhoneCallTracker.this.findConnection(paramAnonymousImsCall2);
      if (localImsPhoneConnection == null) {
        paramAnonymousImsCall2 = null;
      } else {
        paramAnonymousImsCall2 = localImsPhoneConnection.getCall();
      }
      if (paramAnonymousBoolean) {
        ImsPhoneCallTracker.this.switchAfterConferenceSuccess();
      }
      ((ImsPhoneCall)localObject).merge(paramAnonymousImsCall2, Call.State.ACTIVE);
      try
      {
        paramAnonymousImsCall2 = ImsPhoneCallTracker.this.findConnection(paramAnonymousImsCall1);
        localObject = ImsPhoneCallTracker.this;
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("onCallMerged: ImsPhoneConnection=");
        localStringBuilder.append(paramAnonymousImsCall2);
        ((ImsPhoneCallTracker)localObject).log(localStringBuilder.toString());
        localObject = ImsPhoneCallTracker.this;
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("onCallMerged: CurrentVideoProvider=");
        localStringBuilder.append(paramAnonymousImsCall2.getVideoProvider());
        ((ImsPhoneCallTracker)localObject).log(localStringBuilder.toString());
        ImsPhoneCallTracker.this.setVideoCallProvider(paramAnonymousImsCall2, paramAnonymousImsCall1);
        localObject = ImsPhoneCallTracker.this;
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("onCallMerged: CurrentVideoProvider=");
        localStringBuilder.append(paramAnonymousImsCall2.getVideoProvider());
        ((ImsPhoneCallTracker)localObject).log(localStringBuilder.toString());
      }
      catch (Exception localException)
      {
        localObject = ImsPhoneCallTracker.this;
        paramAnonymousImsCall2 = new StringBuilder();
        paramAnonymousImsCall2.append("onCallMerged: exception ");
        paramAnonymousImsCall2.append(localException);
        ((ImsPhoneCallTracker)localObject).loge(paramAnonymousImsCall2.toString());
      }
      ImsPhoneCallTracker.this.processCallStateChange(mForegroundCall.getImsCall(), Call.State.ACTIVE, 0);
      if (localImsPhoneConnection != null) {
        ImsPhoneCallTracker.this.processCallStateChange(mBackgroundCall.getImsCall(), Call.State.HOLDING, 0);
      }
      if (!paramAnonymousImsCall1.isMergeRequestedByConf())
      {
        log("onCallMerged :: calling onMultipartyStateChanged()");
        onMultipartyStateChanged(paramAnonymousImsCall1, true);
      }
      else
      {
        log("onCallMerged :: Merge requested by existing conference.");
        paramAnonymousImsCall1.resetIsMergeRequestedByConf(false);
      }
      logState();
    }
    
    public void onCallProgressing(ImsCall paramAnonymousImsCall)
    {
      log("onCallProgressing");
      ImsPhoneCallTracker.access$1802(ImsPhoneCallTracker.this, null);
      ImsPhoneCallTracker.this.processCallStateChange(paramAnonymousImsCall, Call.State.ALERTING, 0);
      mMetrics.writeOnImsCallProgressing(mPhone.getPhoneId(), paramAnonymousImsCall.getCallSession());
    }
    
    public void onCallResumeFailed(ImsCall paramAnonymousImsCall, ImsReasonInfo paramAnonymousImsReasonInfo)
    {
      if (mSwitchingFgAndBgCalls)
      {
        if (paramAnonymousImsCall == mCallExpectedToResume)
        {
          ImsPhoneCallTracker localImsPhoneCallTracker = ImsPhoneCallTracker.this;
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("onCallResumeFailed : switching ");
          localStringBuilder.append(mForegroundCall);
          localStringBuilder.append(" with ");
          localStringBuilder.append(mBackgroundCall);
          localImsPhoneCallTracker.log(localStringBuilder.toString());
          mForegroundCall.switchWith(mBackgroundCall);
          if (mForegroundCall.getState() == Call.State.HOLDING) {
            sendEmptyMessage(19);
          }
        }
        ImsPhoneCallTracker.access$2202(ImsPhoneCallTracker.this, null);
        ImsPhoneCallTracker.access$2102(ImsPhoneCallTracker.this, false);
      }
      mPhone.notifySuppServiceFailed(PhoneInternalInterface.SuppService.RESUME);
      mMetrics.writeOnImsCallResumeFailed(mPhone.getPhoneId(), paramAnonymousImsCall.getCallSession(), paramAnonymousImsReasonInfo);
    }
    
    public void onCallResumeReceived(ImsCall paramAnonymousImsCall)
    {
      log("onCallResumeReceived");
      Object localObject = ImsPhoneCallTracker.this.findConnection(paramAnonymousImsCall);
      if (localObject != null)
      {
        if (mOnHoldToneStarted)
        {
          mPhone.stopOnHoldTone((Connection)localObject);
          ImsPhoneCallTracker.access$3202(ImsPhoneCallTracker.this, false);
        }
        ((ImsPhoneConnection)localObject).onConnectionEvent("android.telecom.event.CALL_REMOTELY_UNHELD", null);
      }
      if ((mPhone.getContext().getResources().getBoolean(17957070)) && (mSupportPauseVideo) && (VideoProfile.isVideo(((ImsPhoneConnection)localObject).getVideoState()))) {
        ((ImsPhoneConnection)localObject).changeToUnPausedState();
      }
      localObject = new SuppServiceNotification();
      notificationType = 1;
      code = 3;
      mPhone.notifySuppSvcNotification((SuppServiceNotification)localObject);
      mMetrics.writeOnImsCallResumeReceived(mPhone.getPhoneId(), paramAnonymousImsCall.getCallSession());
    }
    
    public void onCallResumed(ImsCall paramAnonymousImsCall)
    {
      log("onCallResumed");
      if (mSwitchingFgAndBgCalls)
      {
        if (paramAnonymousImsCall != mCallExpectedToResume)
        {
          ImsPhoneCallTracker localImsPhoneCallTracker = ImsPhoneCallTracker.this;
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("onCallResumed : switching ");
          localStringBuilder.append(mForegroundCall);
          localStringBuilder.append(" with ");
          localStringBuilder.append(mBackgroundCall);
          localImsPhoneCallTracker.log(localStringBuilder.toString());
          mForegroundCall.switchWith(mBackgroundCall);
        }
        else
        {
          log("onCallResumed : expected call resumed.");
        }
        ImsPhoneCallTracker.access$2102(ImsPhoneCallTracker.this, false);
        ImsPhoneCallTracker.access$2202(ImsPhoneCallTracker.this, null);
      }
      ImsPhoneCallTracker.this.processCallStateChange(paramAnonymousImsCall, Call.State.ACTIVE, 0);
      mMetrics.writeOnImsCallResumed(mPhone.getPhoneId(), paramAnonymousImsCall.getCallSession());
    }
    
    public void onCallSessionPropertyChanged(ImsCall paramAnonymousImsCall, int paramAnonymousInt)
    {
      paramAnonymousImsCall = ImsPhoneCallTracker.this.findConnection(paramAnonymousImsCall);
      if (paramAnonymousImsCall != null) {
        paramAnonymousImsCall.onCallSessionPropertyChanged(paramAnonymousInt);
      }
    }
    
    public void onCallSessionTtyModeReceived(ImsCall paramAnonymousImsCall, int paramAnonymousInt)
    {
      mPhone.onTtyModeReceived(paramAnonymousInt);
    }
    
    public void onCallStartFailed(ImsCall paramAnonymousImsCall, ImsReasonInfo paramAnonymousImsReasonInfo)
    {
      Object localObject1 = ImsPhoneCallTracker.this;
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("onCallStartFailed reasonCode=");
      ((StringBuilder)localObject2).append(paramAnonymousImsReasonInfo.getCode());
      ((ImsPhoneCallTracker)localObject1).log(((StringBuilder)localObject2).toString());
      if ((mSwitchingFgAndBgCalls) && (mCallExpectedToResume != null) && (mCallExpectedToResume == paramAnonymousImsCall))
      {
        log("onCallStarted: starting a call as a result of a switch.");
        ImsPhoneCallTracker.access$2102(ImsPhoneCallTracker.this, false);
        ImsPhoneCallTracker.access$2202(ImsPhoneCallTracker.this, null);
      }
      if (mPendingMO != null)
      {
        if ((paramAnonymousImsReasonInfo.getCode() == 146) && (mBackgroundCall.getState() == Call.State.IDLE) && (mRingingCall.getState() == Call.State.IDLE))
        {
          mForegroundCall.detach(mPendingMO);
          ImsPhoneCallTracker.this.removeConnection(mPendingMO);
          mPendingMO.finalize();
          ImsPhoneCallTracker.access$1802(ImsPhoneCallTracker.this, null);
          mPhone.initiateSilentRedial();
          return;
        }
        ImsPhoneCallTracker.access$1802(ImsPhoneCallTracker.this, null);
        localObject1 = ImsPhoneCallTracker.this.findConnection(paramAnonymousImsCall);
        if (localObject1 != null) {
          localObject2 = ((ImsPhoneConnection)localObject1).getState();
        } else {
          localObject2 = Call.State.DIALING;
        }
        int i = getDisconnectCauseFromReasonInfo(paramAnonymousImsReasonInfo, (Call.State)localObject2);
        if (localObject1 != null) {
          ((ImsPhoneConnection)localObject1).setPreciseDisconnectCause(ImsPhoneCallTracker.this.getPreciseDisconnectCauseFromReasonInfo(paramAnonymousImsReasonInfo));
        }
        ImsPhoneCallTracker.this.processCallStateChange(paramAnonymousImsCall, Call.State.DISCONNECTED, i);
        mMetrics.writeOnImsCallStartFailed(mPhone.getPhoneId(), paramAnonymousImsCall.getCallSession(), paramAnonymousImsReasonInfo);
      }
    }
    
    public void onCallStarted(ImsCall paramAnonymousImsCall)
    {
      log("onCallStarted");
      if ((mSwitchingFgAndBgCalls) && (mCallExpectedToResume != null) && (mCallExpectedToResume == paramAnonymousImsCall))
      {
        log("onCallStarted: starting a call as a result of a switch.");
        ImsPhoneCallTracker.access$2102(ImsPhoneCallTracker.this, false);
        ImsPhoneCallTracker.access$2202(ImsPhoneCallTracker.this, null);
      }
      ImsPhoneCallTracker.access$1802(ImsPhoneCallTracker.this, null);
      ImsPhoneCallTracker.this.processCallStateChange(paramAnonymousImsCall, Call.State.ACTIVE, 0);
      if ((mNotifyVtHandoverToWifiFail) && (paramAnonymousImsCall.isVideoCall()) && (!paramAnonymousImsCall.isWifiCall())) {
        if (ImsPhoneCallTracker.this.isWifiConnected()) {
          sendMessageDelayed(obtainMessage(25, paramAnonymousImsCall), 60000L);
        } else {
          ImsPhoneCallTracker.this.registerForConnectivityChanges();
        }
      }
      ImsPhoneCallTracker.access$2602(ImsPhoneCallTracker.this, false);
      mMetrics.writeOnImsCallStarted(mPhone.getPhoneId(), paramAnonymousImsCall.getCallSession());
    }
    
    public void onCallSuppServiceReceived(ImsCall paramAnonymousImsCall, ImsSuppServiceNotification paramAnonymousImsSuppServiceNotification)
    {
      ImsPhoneCallTracker localImsPhoneCallTracker = ImsPhoneCallTracker.this;
      paramAnonymousImsCall = new StringBuilder();
      paramAnonymousImsCall.append("onCallSuppServiceReceived: suppServiceInfo=");
      paramAnonymousImsCall.append(paramAnonymousImsSuppServiceNotification);
      localImsPhoneCallTracker.log(paramAnonymousImsCall.toString());
      paramAnonymousImsCall = new SuppServiceNotification();
      notificationType = notificationType;
      code = code;
      index = index;
      number = number;
      history = history;
      mPhone.notifySuppSvcNotification(paramAnonymousImsCall);
    }
    
    public void onCallTerminated(ImsCall paramAnonymousImsCall, ImsReasonInfo paramAnonymousImsReasonInfo)
    {
      Object localObject1 = ImsPhoneCallTracker.this;
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("onCallTerminated reasonCode=");
      ((StringBuilder)localObject2).append(paramAnonymousImsReasonInfo.getCode());
      ((ImsPhoneCallTracker)localObject1).log(((StringBuilder)localObject2).toString());
      localObject2 = ImsPhoneCallTracker.this.findConnection(paramAnonymousImsCall);
      if (localObject2 != null) {
        localObject1 = ((ImsPhoneConnection)localObject2).getState();
      } else {
        localObject1 = Call.State.ACTIVE;
      }
      int i = getDisconnectCauseFromReasonInfo(paramAnonymousImsReasonInfo, (Call.State)localObject1);
      localObject1 = ImsPhoneCallTracker.this;
      Object localObject3 = new StringBuilder();
      ((StringBuilder)localObject3).append("cause = ");
      ((StringBuilder)localObject3).append(i);
      ((StringBuilder)localObject3).append(" conn = ");
      ((StringBuilder)localObject3).append(localObject2);
      ((ImsPhoneCallTracker)localObject1).log(((StringBuilder)localObject3).toString());
      if (localObject2 != null)
      {
        localObject1 = ((ImsPhoneConnection)localObject2).getVideoProvider();
        if ((localObject1 instanceof ImsVideoCallProviderWrapper)) {
          ((ImsVideoCallProviderWrapper)localObject1).removeImsVideoProviderCallback((ImsVideoCallProviderWrapper.ImsVideoProviderWrapperCallback)localObject2);
        }
      }
      if (mOnHoldToneId == System.identityHashCode(localObject2))
      {
        if ((localObject2 != null) && (mOnHoldToneStarted)) {
          mPhone.stopOnHoldTone((Connection)localObject2);
        }
        ImsPhoneCallTracker.access$3202(ImsPhoneCallTracker.this, false);
        ImsPhoneCallTracker.access$3102(ImsPhoneCallTracker.this, -1);
      }
      int j = i;
      if (localObject2 != null) {
        if ((((ImsPhoneConnection)localObject2).isPulledCall()) && ((paramAnonymousImsReasonInfo.getCode() == 1015) || (paramAnonymousImsReasonInfo.getCode() == 336) || (paramAnonymousImsReasonInfo.getCode() == 332)) && (mPhone != null) && (mPhone.getExternalCallTracker() != null))
        {
          log("Call pull failed.");
          ((ImsPhoneConnection)localObject2).onCallPullFailed(mPhone.getExternalCallTracker().getConnectionById(((ImsPhoneConnection)localObject2).getPulledDialogId()));
          j = 0;
        }
        else
        {
          j = i;
          if (((ImsPhoneConnection)localObject2).isIncoming())
          {
            j = i;
            if (((ImsPhoneConnection)localObject2).getConnectTime() == 0L)
            {
              j = i;
              if (i != 52)
              {
                if (i == 2) {
                  j = 1;
                } else {
                  j = 16;
                }
                localObject3 = ImsPhoneCallTracker.this;
                localObject1 = new StringBuilder();
                ((StringBuilder)localObject1).append("Incoming connection of 0 connect time detected - translated cause = ");
                ((StringBuilder)localObject1).append(j);
                ((ImsPhoneCallTracker)localObject3).log(((StringBuilder)localObject1).toString());
              }
            }
          }
        }
      }
      i = j;
      if (j == 2)
      {
        i = j;
        if (localObject2 != null)
        {
          i = j;
          if (((ImsPhoneConnection)localObject2).getImsCall().isMerged()) {
            i = 45;
          }
        }
      }
      mMetrics.writeOnImsCallTerminated(mPhone.getPhoneId(), paramAnonymousImsCall.getCallSession(), paramAnonymousImsReasonInfo);
      if (localObject2 != null) {
        ((ImsPhoneConnection)localObject2).setPreciseDisconnectCause(ImsPhoneCallTracker.this.getPreciseDisconnectCauseFromReasonInfo(paramAnonymousImsReasonInfo));
      }
      ImsPhoneCallTracker.this.processCallStateChange(paramAnonymousImsCall, Call.State.DISCONNECTED, i);
      if (mForegroundCall.getState() != Call.State.ACTIVE) {
        if (mRingingCall.getState().isRinging()) {
          ImsPhoneCallTracker.access$1802(ImsPhoneCallTracker.this, null);
        } else if ((mPendingMO != null) && (!mPendingMO.getState().isDialing())) {
          sendEmptyMessage(20);
        }
      }
      if (mSwitchingFgAndBgCalls)
      {
        log("onCallTerminated: Call terminated in the midst of Switching Fg and Bg calls.");
        if (paramAnonymousImsCall == mCallExpectedToResume)
        {
          paramAnonymousImsReasonInfo = ImsPhoneCallTracker.this;
          paramAnonymousImsCall = new StringBuilder();
          paramAnonymousImsCall.append("onCallTerminated: switching ");
          paramAnonymousImsCall.append(mForegroundCall);
          paramAnonymousImsCall.append(" with ");
          paramAnonymousImsCall.append(mBackgroundCall);
          paramAnonymousImsReasonInfo.log(paramAnonymousImsCall.toString());
          mForegroundCall.switchWith(mBackgroundCall);
        }
        paramAnonymousImsReasonInfo = ImsPhoneCallTracker.this;
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("onCallTerminated: foreground call in state ");
        ((StringBuilder)localObject1).append(mForegroundCall.getState());
        ((StringBuilder)localObject1).append(" and ringing call in state ");
        if (mRingingCall == null) {
          paramAnonymousImsCall = "null";
        } else {
          paramAnonymousImsCall = mRingingCall.getState().toString();
        }
        ((StringBuilder)localObject1).append(paramAnonymousImsCall);
        paramAnonymousImsReasonInfo.log(((StringBuilder)localObject1).toString());
        if ((mForegroundCall.getState() == Call.State.HOLDING) || (mRingingCall.getState() == Call.State.WAITING))
        {
          sendEmptyMessage(19);
          ImsPhoneCallTracker.access$2102(ImsPhoneCallTracker.this, false);
          ImsPhoneCallTracker.access$2202(ImsPhoneCallTracker.this, null);
        }
      }
      if (mShouldUpdateImsConfigOnDisconnect)
      {
        if (mImsManager != null) {
          mImsManager.updateImsServiceConfig(true);
        }
        ImsPhoneCallTracker.access$3302(ImsPhoneCallTracker.this, false);
      }
    }
    
    public void onCallUpdated(ImsCall paramAnonymousImsCall)
    {
      log("onCallUpdated");
      if (paramAnonymousImsCall == null) {
        return;
      }
      ImsPhoneConnection localImsPhoneConnection = ImsPhoneCallTracker.this.findConnection(paramAnonymousImsCall);
      if (localImsPhoneConnection != null)
      {
        ImsPhoneCallTracker localImsPhoneCallTracker = ImsPhoneCallTracker.this;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("onCallUpdated: profile is ");
        localStringBuilder.append(paramAnonymousImsCall.getCallProfile());
        localImsPhoneCallTracker.log(localStringBuilder.toString());
        ImsPhoneCallTracker.this.processCallStateChange(paramAnonymousImsCall, getCallmState, 0, true);
        mMetrics.writeImsCallState(mPhone.getPhoneId(), paramAnonymousImsCall.getCallSession(), getCallmState);
      }
    }
    
    public void onConferenceMaxnumUserCountUpdated(ImsCall paramAnonymousImsCall, int paramAnonymousInt)
    {
      ImsPhoneCallTracker localImsPhoneCallTracker = ImsPhoneCallTracker.this;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onConferenceMaxnumUserCountUpdated num: ");
      localStringBuilder.append(paramAnonymousInt);
      localStringBuilder.append(" call: ");
      localStringBuilder.append(paramAnonymousImsCall);
      localImsPhoneCallTracker.log(localStringBuilder.toString());
      paramAnonymousImsCall = ImsPhoneCallTracker.this.findConnection(paramAnonymousImsCall);
      if (paramAnonymousImsCall != null) {
        paramAnonymousImsCall.updateConferenceMaxnumUserCount(paramAnonymousInt);
      }
    }
    
    public void onConferenceParticipantsStateChanged(ImsCall paramAnonymousImsCall, List<ConferenceParticipant> paramAnonymousList)
    {
      log("onConferenceParticipantsStateChanged");
      Object localObject1 = ImsPhoneCallTracker.this;
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("onConferenceParticipantsStateChanged participants = ");
      ((StringBuilder)localObject2).append(paramAnonymousList);
      ((ImsPhoneCallTracker)localObject1).log(((StringBuilder)localObject2).toString());
      if (paramAnonymousList != null)
      {
        localObject2 = ImsPhoneCallTracker.this;
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("onConferenceParticipantsStateChanged participants.size() = ");
        ((StringBuilder)localObject1).append(paramAnonymousList.size());
        ((ImsPhoneCallTracker)localObject2).log(((StringBuilder)localObject1).toString());
      }
      paramAnonymousImsCall = ImsPhoneCallTracker.this.findConnection(paramAnonymousImsCall);
      if (paramAnonymousImsCall != null) {
        paramAnonymousImsCall.updateConferenceParticipants(paramAnonymousList);
      }
    }
    
    public void onMultipartyStateChanged(ImsCall paramAnonymousImsCall, boolean paramAnonymousBoolean)
    {
      ImsPhoneCallTracker localImsPhoneCallTracker = ImsPhoneCallTracker.this;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onMultipartyStateChanged to ");
      String str;
      if (paramAnonymousBoolean) {
        str = "Y";
      } else {
        str = "N";
      }
      localStringBuilder.append(str);
      localImsPhoneCallTracker.log(localStringBuilder.toString());
      paramAnonymousImsCall = ImsPhoneCallTracker.this.findConnection(paramAnonymousImsCall);
      if (paramAnonymousImsCall != null) {
        paramAnonymousImsCall.updateMultipartyState(paramAnonymousBoolean);
      }
    }
    
    public void onRttMessageReceived(ImsCall paramAnonymousImsCall, String paramAnonymousString)
    {
      paramAnonymousImsCall = ImsPhoneCallTracker.this.findConnection(paramAnonymousImsCall);
      if (paramAnonymousImsCall != null) {
        paramAnonymousImsCall.onRttMessageReceived(paramAnonymousString);
      }
    }
    
    public void onRttModifyRequestReceived(ImsCall paramAnonymousImsCall)
    {
      paramAnonymousImsCall = ImsPhoneCallTracker.this.findConnection(paramAnonymousImsCall);
      if (paramAnonymousImsCall != null) {
        paramAnonymousImsCall.onRttModifyRequestReceived();
      }
    }
    
    public void onRttModifyResponseReceived(ImsCall paramAnonymousImsCall, int paramAnonymousInt)
    {
      paramAnonymousImsCall = ImsPhoneCallTracker.this.findConnection(paramAnonymousImsCall);
      if (paramAnonymousImsCall != null) {
        paramAnonymousImsCall.onRttModifyResponseReceived(paramAnonymousInt);
      }
    }
  };
  private final ImsFeature.CapabilityCallback mImsCapabilityCallback = new ImsFeature.CapabilityCallback()
  {
    public void onCapabilitiesStatusChanged(ImsFeature.Capabilities paramAnonymousCapabilities)
    {
      ImsPhoneCallTracker localImsPhoneCallTracker = ImsPhoneCallTracker.this;
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("onCapabilitiesStatusChanged: ");
      ((StringBuilder)localObject).append(paramAnonymousCapabilities);
      localImsPhoneCallTracker.log(((StringBuilder)localObject).toString());
      localObject = SomeArgs.obtain();
      arg1 = paramAnonymousCapabilities;
      removeMessages(26);
      obtainMessage(26, localObject).sendToTarget();
    }
  };
  private ImsConfigListener.Stub mImsConfigListener = new ImsConfigListener.Stub()
  {
    public void onGetFeatureResponse(int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, int paramAnonymousInt4) {}
    
    public void onGetVideoQuality(int paramAnonymousInt1, int paramAnonymousInt2) {}
    
    public void onSetFeatureResponse(int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, int paramAnonymousInt4)
    {
      mMetrics.writeImsSetFeatureValue(mPhone.getPhoneId(), paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3);
    }
    
    public void onSetVideoQuality(int paramAnonymousInt) {}
  };
  private ImsManager mImsManager;
  private final ImsManager.Connector mImsManagerConnector;
  private Map<Pair<Integer, String>, Integer> mImsReasonCodeMap = new ArrayMap();
  private final ImsRegistrationImplBase.Callback mImsRegistrationCallback = new ImsRegistrationImplBase.Callback()
  {
    public void onDeregistered(ImsReasonInfo paramAnonymousImsReasonInfo)
    {
      ImsPhoneCallTracker localImsPhoneCallTracker = ImsPhoneCallTracker.this;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onImsDisconnected imsReasonInfo=");
      localStringBuilder.append(paramAnonymousImsReasonInfo);
      localImsPhoneCallTracker.log(localStringBuilder.toString());
      mPhone.setServiceState(1);
      mPhone.setImsRegistered(false);
      mPhone.processDisconnectReason(paramAnonymousImsReasonInfo);
      mMetrics.writeOnImsConnectionState(mPhone.getPhoneId(), 3, paramAnonymousImsReasonInfo);
    }
    
    public void onRegistered(int paramAnonymousInt)
    {
      ImsPhoneCallTracker localImsPhoneCallTracker = ImsPhoneCallTracker.this;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onImsConnected imsRadioTech=");
      localStringBuilder.append(paramAnonymousInt);
      localImsPhoneCallTracker.log(localStringBuilder.toString());
      mPhone.setServiceState(0);
      mPhone.setImsRegistered(true);
      mMetrics.writeOnImsConnectionState(mPhone.getPhoneId(), 1, null);
    }
    
    public void onRegistering(int paramAnonymousInt)
    {
      ImsPhoneCallTracker localImsPhoneCallTracker = ImsPhoneCallTracker.this;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onImsProgressing imsRadioTech=");
      localStringBuilder.append(paramAnonymousInt);
      localImsPhoneCallTracker.log(localStringBuilder.toString());
      mPhone.setServiceState(1);
      mPhone.setImsRegistered(false);
      mMetrics.writeOnImsConnectionState(mPhone.getPhoneId(), 2, null);
    }
    
    public void onSubscriberAssociatedUriChanged(Uri[] paramAnonymousArrayOfUri)
    {
      log("registrationAssociatedUriChanged");
      mPhone.setCurrentSubscriberUris(paramAnonymousArrayOfUri);
    }
  };
  private ImsCall.Listener mImsUssdListener = new ImsCall.Listener()
  {
    public void onCallStartFailed(ImsCall paramAnonymousImsCall, ImsReasonInfo paramAnonymousImsReasonInfo)
    {
      ImsPhoneCallTracker localImsPhoneCallTracker = ImsPhoneCallTracker.this;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("mImsUssdListener onCallStartFailed reasonCode=");
      localStringBuilder.append(paramAnonymousImsReasonInfo.getCode());
      localImsPhoneCallTracker.log(localStringBuilder.toString());
      onCallTerminated(paramAnonymousImsCall, paramAnonymousImsReasonInfo);
    }
    
    public void onCallStarted(ImsCall paramAnonymousImsCall)
    {
      log("mImsUssdListener onCallStarted");
      if ((paramAnonymousImsCall == mUssdSession) && (mPendingUssd != null))
      {
        AsyncResult.forMessage(mPendingUssd);
        mPendingUssd.sendToTarget();
        ImsPhoneCallTracker.access$4402(ImsPhoneCallTracker.this, null);
      }
    }
    
    public void onCallTerminated(ImsCall paramAnonymousImsCall, ImsReasonInfo paramAnonymousImsReasonInfo)
    {
      ImsPhoneCallTracker localImsPhoneCallTracker = ImsPhoneCallTracker.this;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("mImsUssdListener onCallTerminated reasonCode=");
      localStringBuilder.append(paramAnonymousImsReasonInfo.getCode());
      localImsPhoneCallTracker.log(localStringBuilder.toString());
      removeMessages(25);
      ImsPhoneCallTracker.access$2602(ImsPhoneCallTracker.this, false);
      ImsPhoneCallTracker.this.unregisterForConnectivityChanges();
      if (paramAnonymousImsCall == mUssdSession)
      {
        ImsPhoneCallTracker.access$202(ImsPhoneCallTracker.this, null);
        if (mPendingUssd != null)
        {
          paramAnonymousImsReasonInfo = new CommandException(CommandException.Error.GENERIC_FAILURE);
          AsyncResult.forMessage(mPendingUssd, null, paramAnonymousImsReasonInfo);
          mPendingUssd.sendToTarget();
          ImsPhoneCallTracker.access$4402(ImsPhoneCallTracker.this, null);
        }
      }
      paramAnonymousImsCall.close();
    }
    
    public void onCallUssdMessageReceived(ImsCall paramAnonymousImsCall, int paramAnonymousInt, String paramAnonymousString)
    {
      ImsPhoneCallTracker localImsPhoneCallTracker = ImsPhoneCallTracker.this;
      paramAnonymousImsCall = new StringBuilder();
      paramAnonymousImsCall.append("mImsUssdListener onCallUssdMessageReceived mode=");
      paramAnonymousImsCall.append(paramAnonymousInt);
      localImsPhoneCallTracker.log(paramAnonymousImsCall.toString());
      int i = -1;
      switch (paramAnonymousInt)
      {
      default: 
        paramAnonymousInt = i;
        break;
      case 1: 
        paramAnonymousInt = 1;
        break;
      case 0: 
        paramAnonymousInt = 0;
      }
      mPhone.onIncomingUSSD(paramAnonymousInt, paramAnonymousString);
    }
  };
  private boolean mIsDataEnabled = false;
  private boolean mIsInEmergencyCall = false;
  private boolean mIsMonitoringConnectivity = false;
  private boolean mIsViLteDataMetered = false;
  private TelephonyMetrics mMetrics;
  private MmTelFeature.MmTelCapabilities mMmTelCapabilities = new MmTelFeature.MmTelCapabilities();
  private final MmTelFeatureListener mMmTelFeatureListener = new MmTelFeatureListener(null);
  private ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback()
  {
    public void onAvailable(Network paramAnonymousNetwork)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Network available: ");
      localStringBuilder.append(paramAnonymousNetwork);
      Rlog.i("ImsPhoneCallTracker", localStringBuilder.toString());
      ImsPhoneCallTracker.this.scheduleHandoverCheck();
    }
  };
  private boolean mNotifyHandoverVideoFromLTEToWifi = false;
  private boolean mNotifyHandoverVideoFromWifiToLTE = false;
  private boolean mNotifyVtHandoverToWifiFail = false;
  private int mOnHoldToneId = -1;
  private boolean mOnHoldToneStarted = false;
  private int mPendingCallVideoState;
  private Bundle mPendingIntentExtras;
  private ImsPhoneConnection mPendingMO;
  private Message mPendingUssd = null;
  ImsPhone mPhone;
  private PhoneNumberUtilsProxy mPhoneNumberUtilsProxy = new _..Lambda.ImsPhoneCallTracker.j007jv8ZzpH3GXLLwqHDShCqh70(this);
  private List<PhoneStateListener> mPhoneStateListeners = new ArrayList();
  private BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      if (paramAnonymousIntent.getAction().equals("android.telephony.action.CARRIER_CONFIG_CHANGED"))
      {
        int i = paramAnonymousIntent.getIntExtra("subscription", -1);
        if (i == mPhone.getSubId())
        {
          ImsPhoneCallTracker.this.cacheCarrierConfiguration(i);
          paramAnonymousIntent = ImsPhoneCallTracker.this;
          paramAnonymousContext = new StringBuilder();
          paramAnonymousContext.append("onReceive : Updating mAllowEmergencyVideoCalls = ");
          paramAnonymousContext.append(mAllowEmergencyVideoCalls);
          paramAnonymousIntent.log(paramAnonymousContext.toString());
        }
      }
      else if ("android.telecom.action.CHANGE_DEFAULT_DIALER".equals(paramAnonymousIntent.getAction()))
      {
        mDefaultDialerUid.set(ImsPhoneCallTracker.this.getPackageUid(paramAnonymousContext, paramAnonymousIntent.getStringExtra("android.telecom.extra.CHANGE_DEFAULT_DIALER_PACKAGE_NAME")));
      }
    }
  };
  public ImsPhoneCall mRingingCall = new ImsPhoneCall(this, "RG");
  private SharedPreferenceProxy mSharedPreferenceProxy = _..Lambda.ImsPhoneCallTracker.Zw03itjXT6_LrhiYuD_9nKFg2Wg.INSTANCE;
  private boolean mShouldUpdateImsConfigOnDisconnect = false;
  private Call.SrvccState mSrvccState = Call.SrvccState.NONE;
  private PhoneConstants.State mState = PhoneConstants.State.IDLE;
  private boolean mSupportDowngradeVtToAudio = false;
  private boolean mSupportPauseVideo = false;
  private boolean mSwitchingFgAndBgCalls = false;
  private Object mSyncHold = new Object();
  private boolean mTreatDowngradedVideoCallsAsVideoCalls = false;
  private ImsCall mUssdSession = null;
  private ImsUtInterface mUtInterface;
  private RegistrantList mVoiceCallEndedRegistrants = new RegistrantList();
  private RegistrantList mVoiceCallStartedRegistrants = new RegistrantList();
  private final HashMap<Integer, Long> mVtDataUsageMap = new HashMap();
  private volatile NetworkStats mVtDataUsageSnapshot = null;
  private volatile NetworkStats mVtDataUsageUidSnapshot = null;
  private int pendingCallClirMode;
  private boolean pendingCallInEcm = false;
  
  static
  {
    PRECISE_CAUSE_MAP = new SparseIntArray();
    PRECISE_CAUSE_MAP.append(101, 1200);
    PRECISE_CAUSE_MAP.append(102, 1201);
    PRECISE_CAUSE_MAP.append(103, 1202);
    PRECISE_CAUSE_MAP.append(106, 1203);
    PRECISE_CAUSE_MAP.append(107, 1204);
    PRECISE_CAUSE_MAP.append(108, 16);
    PRECISE_CAUSE_MAP.append(111, 1205);
    PRECISE_CAUSE_MAP.append(112, 1206);
    PRECISE_CAUSE_MAP.append(121, 1207);
    PRECISE_CAUSE_MAP.append(122, 1208);
    PRECISE_CAUSE_MAP.append(123, 1209);
    PRECISE_CAUSE_MAP.append(124, 1210);
    PRECISE_CAUSE_MAP.append(131, 1211);
    PRECISE_CAUSE_MAP.append(132, 1212);
    PRECISE_CAUSE_MAP.append(141, 1213);
    PRECISE_CAUSE_MAP.append(143, 1214);
    PRECISE_CAUSE_MAP.append(144, 1215);
    PRECISE_CAUSE_MAP.append(145, 1216);
    PRECISE_CAUSE_MAP.append(146, 1217);
    PRECISE_CAUSE_MAP.append(147, 1218);
    PRECISE_CAUSE_MAP.append(148, 1219);
    PRECISE_CAUSE_MAP.append(149, 1220);
    PRECISE_CAUSE_MAP.append(201, 1221);
    PRECISE_CAUSE_MAP.append(202, 1222);
    PRECISE_CAUSE_MAP.append(203, 1223);
    PRECISE_CAUSE_MAP.append(241, 241);
    PRECISE_CAUSE_MAP.append(321, 1300);
    PRECISE_CAUSE_MAP.append(331, 1310);
    PRECISE_CAUSE_MAP.append(332, 1311);
    PRECISE_CAUSE_MAP.append(333, 1312);
    PRECISE_CAUSE_MAP.append(334, 1313);
    PRECISE_CAUSE_MAP.append(335, 1314);
    PRECISE_CAUSE_MAP.append(336, 1315);
    PRECISE_CAUSE_MAP.append(337, 1316);
    PRECISE_CAUSE_MAP.append(338, 1317);
    PRECISE_CAUSE_MAP.append(339, 1318);
    PRECISE_CAUSE_MAP.append(340, 1319);
    PRECISE_CAUSE_MAP.append(341, 1320);
    PRECISE_CAUSE_MAP.append(342, 1321);
    PRECISE_CAUSE_MAP.append(351, 1330);
    PRECISE_CAUSE_MAP.append(352, 1331);
    PRECISE_CAUSE_MAP.append(353, 1332);
    PRECISE_CAUSE_MAP.append(354, 1333);
    PRECISE_CAUSE_MAP.append(361, 1340);
    PRECISE_CAUSE_MAP.append(362, 1341);
    PRECISE_CAUSE_MAP.append(363, 1342);
    PRECISE_CAUSE_MAP.append(364, 1343);
    PRECISE_CAUSE_MAP.append(401, 1400);
    PRECISE_CAUSE_MAP.append(402, 1401);
    PRECISE_CAUSE_MAP.append(403, 1402);
    PRECISE_CAUSE_MAP.append(404, 1403);
    PRECISE_CAUSE_MAP.append(501, 1500);
    PRECISE_CAUSE_MAP.append(502, 1501);
    PRECISE_CAUSE_MAP.append(503, 1502);
    PRECISE_CAUSE_MAP.append(504, 1503);
    PRECISE_CAUSE_MAP.append(505, 1504);
    PRECISE_CAUSE_MAP.append(506, 1505);
    PRECISE_CAUSE_MAP.append(510, 1510);
    PRECISE_CAUSE_MAP.append(801, 1800);
    PRECISE_CAUSE_MAP.append(802, 1801);
    PRECISE_CAUSE_MAP.append(803, 1802);
    PRECISE_CAUSE_MAP.append(804, 1803);
    PRECISE_CAUSE_MAP.append(821, 1804);
    PRECISE_CAUSE_MAP.append(901, 1900);
    PRECISE_CAUSE_MAP.append(902, 1901);
    PRECISE_CAUSE_MAP.append(1100, 2000);
    PRECISE_CAUSE_MAP.append(1014, 2100);
    PRECISE_CAUSE_MAP.append(1015, 2101);
    PRECISE_CAUSE_MAP.append(1016, 2102);
    PRECISE_CAUSE_MAP.append(1201, 2300);
    PRECISE_CAUSE_MAP.append(1202, 2301);
    PRECISE_CAUSE_MAP.append(1203, 2302);
    PRECISE_CAUSE_MAP.append(1300, 2400);
    PRECISE_CAUSE_MAP.append(1400, 2500);
    PRECISE_CAUSE_MAP.append(1401, 2501);
    PRECISE_CAUSE_MAP.append(1402, 2502);
    PRECISE_CAUSE_MAP.append(1403, 2503);
    PRECISE_CAUSE_MAP.append(1404, 2504);
    PRECISE_CAUSE_MAP.append(1405, 2505);
    PRECISE_CAUSE_MAP.append(1406, 2506);
    PRECISE_CAUSE_MAP.append(1407, 2507);
    PRECISE_CAUSE_MAP.append(1500, 247);
    PRECISE_CAUSE_MAP.append(1501, 249);
    PRECISE_CAUSE_MAP.append(1502, 250);
    PRECISE_CAUSE_MAP.append(1503, 251);
    PRECISE_CAUSE_MAP.append(1504, 252);
    PRECISE_CAUSE_MAP.append(1505, 253);
    PRECISE_CAUSE_MAP.append(1506, 254);
    PRECISE_CAUSE_MAP.append(1507, 255);
    PRECISE_CAUSE_MAP.append(1508, 256);
    PRECISE_CAUSE_MAP.append(1509, 257);
    PRECISE_CAUSE_MAP.append(1510, 258);
    PRECISE_CAUSE_MAP.append(1511, 259);
    PRECISE_CAUSE_MAP.append(1512, 260);
    PRECISE_CAUSE_MAP.append(1513, 261);
    PRECISE_CAUSE_MAP.append(1515, 1);
    PRECISE_CAUSE_MAP.append(61441, 61441);
    PRECISE_CAUSE_MAP.append(61442, 61442);
    PRECISE_CAUSE_MAP.append(61443, 61443);
    PRECISE_CAUSE_MAP.append(61444, 61444);
    PRECISE_CAUSE_MAP.append(61445, 61445);
    PRECISE_CAUSE_MAP.append(61446, 61446);
    PRECISE_CAUSE_MAP.append(61447, 61447);
    PRECISE_CAUSE_MAP.append(61448, 61448);
    PRECISE_CAUSE_MAP.append(61449, 61449);
    PRECISE_CAUSE_MAP.append(61450, 61450);
    PRECISE_CAUSE_MAP.append(61451, 61451);
    PRECISE_CAUSE_MAP.append(61452, 61452);
    PRECISE_CAUSE_MAP.append(61453, 61453);
    PRECISE_CAUSE_MAP.append(61454, 61454);
    PRECISE_CAUSE_MAP.append(61455, 61455);
  }
  
  public ImsPhoneCallTracker(ImsPhone paramImsPhone)
  {
    mPhone = paramImsPhone;
    mMetrics = TelephonyMetrics.getInstance();
    Object localObject = new IntentFilter();
    ((IntentFilter)localObject).addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
    ((IntentFilter)localObject).addAction("android.telecom.action.CHANGE_DEFAULT_DIALER");
    mPhone.getContext().registerReceiver(mReceiver, (IntentFilter)localObject);
    cacheCarrierConfiguration(mPhone.getSubId());
    mPhone.getDefaultPhone().registerForDataEnabledChanged(this, 23, null);
    localObject = (TelecomManager)mPhone.getContext().getSystemService("telecom");
    mDefaultDialerUid.set(getPackageUid(mPhone.getContext(), ((TelecomManager)localObject).getDefaultDialerPackage()));
    long l = SystemClock.elapsedRealtime();
    mVtDataUsageSnapshot = new NetworkStats(l, 1);
    mVtDataUsageUidSnapshot = new NetworkStats(l, 1);
    mImsManagerConnector = new ImsManager.Connector(paramImsPhone.getContext(), paramImsPhone.getPhoneId(), new ImsManager.Connector.Listener()
    {
      public void connectionReady(ImsManager paramAnonymousImsManager)
        throws ImsException
      {
        ImsPhoneCallTracker.access$102(ImsPhoneCallTracker.this, paramAnonymousImsManager);
        ImsPhoneCallTracker.this.startListeningForCalls();
      }
      
      public void connectionUnavailable()
      {
        ImsPhoneCallTracker.this.stopListeningForCalls();
      }
    });
    mImsManagerConnector.connect();
  }
  
  private void addConnection(ImsPhoneConnection paramImsPhoneConnection)
  {
    try
    {
      mConnections.add(paramImsPhoneConnection);
      if (paramImsPhoneConnection.isEmergency())
      {
        mIsInEmergencyCall = true;
        mPhone.sendEmergencyCallStateChange(true);
      }
      return;
    }
    finally
    {
      paramImsPhoneConnection = finally;
      throw paramImsPhoneConnection;
    }
  }
  
  private ImsStreamMediaProfile addRttAttributeIfRequired(ImsCall paramImsCall, ImsStreamMediaProfile paramImsStreamMediaProfile)
  {
    if (!mPhone.isRttSupported()) {
      return paramImsStreamMediaProfile;
    }
    ImsCallProfile localImsCallProfile = paramImsCall.getCallProfile();
    if ((mMediaProfile != null) && (mMediaProfile.isRttCall()) && (mPhone.isRttVtCallAllowed(paramImsCall)))
    {
      paramImsCall = new StringBuilder();
      paramImsCall.append("RTT: addRttAttributeIfRequired = ");
      paramImsCall.append(mMediaProfile.isRttCall());
      log(paramImsCall.toString());
      paramImsStreamMediaProfile.setRttMode(1);
    }
    return paramImsStreamMediaProfile;
  }
  
  private void cacheCarrierConfiguration(int paramInt)
  {
    Object localObject = (CarrierConfigManager)mPhone.getContext().getSystemService("carrier_config");
    if ((localObject != null) && (SubscriptionController.getInstance().isActiveSubId(paramInt)))
    {
      localObject = ((CarrierConfigManager)localObject).getConfigForSubId(paramInt);
      if (localObject == null)
      {
        loge("cacheCarrierConfiguration: Empty carrier config.");
        mCarrierConfigLoaded = false;
        return;
      }
      mCarrierConfigLoaded = true;
      updateCarrierConfigCache((PersistableBundle)localObject);
      return;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("cacheCarrierConfiguration: No carrier config service found or not active subId = ");
    ((StringBuilder)localObject).append(paramInt);
    loge(((StringBuilder)localObject).toString());
    mCarrierConfigLoaded = false;
  }
  
  private boolean canAddVideoCallDuringImsAudioCall(int paramInt)
  {
    boolean bool1 = mAllowHoldingVideoCall;
    boolean bool2 = true;
    if (bool1) {
      return true;
    }
    ImsCall localImsCall1 = mForegroundCall.getImsCall();
    ImsCall localImsCall2 = localImsCall1;
    if (localImsCall1 == null) {
      localImsCall2 = mBackgroundCall.getImsCall();
    }
    int i;
    if (((mForegroundCall.getState() == Call.State.ACTIVE) || (mBackgroundCall.getState() == Call.State.HOLDING)) && (localImsCall2 != null) && (!localImsCall2.isVideoCall())) {
      i = 1;
    } else {
      i = 0;
    }
    bool1 = bool2;
    if (i != 0) {
      if (!VideoProfile.isVideo(paramInt)) {
        bool1 = bool2;
      } else {
        bool1 = false;
      }
    }
    return bool1;
  }
  
  private String cleanseInstantLetteringMessage(String paramString)
  {
    if (TextUtils.isEmpty(paramString)) {
      return paramString;
    }
    Object localObject1 = (CarrierConfigManager)mPhone.getContext().getSystemService("carrier_config");
    if (localObject1 == null) {
      return paramString;
    }
    Object localObject2 = ((CarrierConfigManager)localObject1).getConfigForSubId(mPhone.getSubId());
    if (localObject2 == null) {
      return paramString;
    }
    String str = ((PersistableBundle)localObject2).getString("carrier_instant_lettering_invalid_chars_string");
    localObject1 = paramString;
    if (!TextUtils.isEmpty(str)) {
      localObject1 = paramString.replaceAll(str, "");
    }
    localObject2 = ((PersistableBundle)localObject2).getString("carrier_instant_lettering_escaped_chars_string");
    paramString = (String)localObject1;
    if (!TextUtils.isEmpty((CharSequence)localObject2)) {
      paramString = escapeChars((String)localObject2, (String)localObject1);
    }
    return paramString;
  }
  
  private void dialInternal(ImsPhoneConnection paramImsPhoneConnection, int paramInt1, int paramInt2, Bundle paramBundle)
  {
    if (paramImsPhoneConnection == null) {
      return;
    }
    boolean bool1 = false;
    boolean bool2 = false;
    if (paramBundle != null)
    {
      bool1 = paramBundle.getBoolean("org.codeaurora.extra.DIAL_CONFERENCE_URI", false);
      bool2 = paramBundle.getBoolean("org.codeaurora.extra.SKIP_SCHEMA_PARSING", false);
    }
    if ((!bool1) && (!bool2) && ((paramImsPhoneConnection.getAddress() == null) || (paramImsPhoneConnection.getAddress().length() == 0) || (paramImsPhoneConnection.getAddress().indexOf('N') >= 0)))
    {
      paramImsPhoneConnection.setDisconnectCause(7);
      sendEmptyMessageDelayed(18, 500L);
      return;
    }
    setMute(false);
    int i;
    if (mPhoneNumberUtilsProxy.isEmergencyNumber(paramImsPhoneConnection.getAddress())) {
      i = 2;
    } else {
      i = 1;
    }
    int j = ImsCallProfile.getCallTypeFromVideoState(paramInt2);
    paramImsPhoneConnection.setVideoState(paramInt2);
    Object localObject1;
    try
    {
      localObject1 = paramImsPhoneConnection.getAddress();
      Object localObject2 = mImsManager.createCallProfile(i, j);
      try
      {
        ((ImsCallProfile)localObject2).setCallExtraInt("oir", paramInt1);
        ((ImsCallProfile)localObject2).setCallExtraBoolean("isConferenceUri", bool1);
        if (paramBundle != null)
        {
          if (paramBundle.containsKey("android.telecom.extra.CALL_SUBJECT")) {
            paramBundle.putString("DisplayText", cleanseInstantLetteringMessage(paramBundle.getString("android.telecom.extra.CALL_SUBJECT")));
          }
          if (paramImsPhoneConnection.hasRttTextStream()) {
            mMediaProfile.mRttMode = 1;
          }
          if (paramBundle.containsKey("CallPull"))
          {
            mCallExtras.putBoolean("CallPull", paramBundle.getBoolean("CallPull"));
            paramInt1 = paramBundle.getInt("android.telephony.ImsExternalCallTracker.extra.EXTERNAL_CALL_ID");
            paramImsPhoneConnection.setIsPulledCall(true);
            paramImsPhoneConnection.setPulledDialogId(paramInt1);
          }
          mCallExtras.putBundle("OemCallExtras", paramBundle);
        }
        paramBundle = setRttModeBasedOnOperator((ImsCallProfile)localObject2);
        localObject2 = mImsManager;
        ImsCall.Listener localListener = mImsCallListener;
        paramBundle = ((ImsManager)localObject2).makeCall(paramBundle, new String[] { localObject1 }, localListener);
        paramImsPhoneConnection.setImsCall(paramBundle);
        mMetrics.writeOnImsCallStart(mPhone.getPhoneId(), paramBundle.getSession());
        setVideoCallProvider(paramImsPhoneConnection, paramBundle);
        paramImsPhoneConnection.setAllowAddCallDuringVideoCall(mAllowAddCallDuringVideoCall);
        paramImsPhoneConnection.setAllowHoldingVideoCall(mAllowHoldingVideoCall);
      }
      catch (RemoteException paramImsPhoneConnection) {}catch (ImsException paramBundle) {}
      localObject1 = new StringBuilder();
    }
    catch (RemoteException paramImsPhoneConnection) {}catch (ImsException paramBundle) {}
    ((StringBuilder)localObject1).append("dialInternal : ");
    ((StringBuilder)localObject1).append(paramBundle);
    loge(((StringBuilder)localObject1).toString());
    paramImsPhoneConnection.setDisconnectCause(36);
    sendEmptyMessageDelayed(18, 500L);
    retryGetImsService();
  }
  
  private void dialPendingMO()
  {
    boolean bool1 = isPhoneInEcbMode();
    boolean bool2 = mPendingMO.isEmergency();
    if ((bool1) && ((!bool1) || (!bool2))) {
      sendEmptyMessage(21);
    } else {
      sendEmptyMessage(20);
    }
  }
  
  private void downgradeVideoCall(int paramInt, ImsPhoneConnection paramImsPhoneConnection)
  {
    ImsCall localImsCall = paramImsPhoneConnection.getImsCall();
    if (localImsCall != null) {
      if ((paramImsPhoneConnection.hasCapabilities(3)) && (!mSupportPauseVideo))
      {
        modifyVideoCall(localImsCall, 0);
      }
      else if ((mSupportPauseVideo) && (paramInt != 1407))
      {
        mShouldUpdateImsConfigOnDisconnect = true;
        paramImsPhoneConnection.pauseVideo(2);
      }
      else
      {
        try
        {
          localImsCall.terminate(501, paramInt);
        }
        catch (ImsException paramImsPhoneConnection)
        {
          paramImsPhoneConnection = new StringBuilder();
          paramImsPhoneConnection.append("Couldn't terminate call ");
          paramImsPhoneConnection.append(localImsCall);
          loge(paramImsPhoneConnection.toString());
        }
      }
    }
  }
  
  private void dumpState()
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Phone State:");
    ((StringBuilder)localObject).append(mState);
    log(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Ringing call: ");
    ((StringBuilder)localObject).append(mRingingCall.toString());
    log(((StringBuilder)localObject).toString());
    localObject = mRingingCall.getConnections();
    int i = 0;
    int j = ((List)localObject).size();
    while (i < j)
    {
      log(((List)localObject).get(i).toString());
      i++;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Foreground call: ");
    ((StringBuilder)localObject).append(mForegroundCall.toString());
    log(((StringBuilder)localObject).toString());
    localObject = mForegroundCall.getConnections();
    i = 0;
    j = ((List)localObject).size();
    while (i < j)
    {
      log(((List)localObject).get(i).toString());
      i++;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Background call: ");
    ((StringBuilder)localObject).append(mBackgroundCall.toString());
    log(((StringBuilder)localObject).toString());
    localObject = mBackgroundCall.getConnections();
    i = 0;
    j = ((List)localObject).size();
    while (i < j)
    {
      log(((List)localObject).get(i).toString());
      i++;
    }
  }
  
  private String escapeChars(String paramString1, String paramString2)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    for (char c : paramString2.toCharArray())
    {
      if (paramString1.contains(Character.toString(c))) {
        localStringBuilder.append("\\");
      }
      localStringBuilder.append(c);
    }
    return localStringBuilder.toString();
  }
  
  private ImsPhoneConnection findConnection(ImsCall paramImsCall)
  {
    try
    {
      Iterator localIterator = mConnections.iterator();
      while (localIterator.hasNext())
      {
        ImsPhoneConnection localImsPhoneConnection = (ImsPhoneConnection)localIterator.next();
        ImsCall localImsCall = localImsPhoneConnection.getImsCall();
        if (localImsCall == paramImsCall) {
          return localImsPhoneConnection;
        }
      }
      return null;
    }
    finally {}
  }
  
  private ImsException getImsManagerIsNullException()
  {
    return new ImsException("no ims manager", 102);
  }
  
  private int getPackageUid(Context paramContext, String paramString)
  {
    int i = -1;
    if (paramString == null) {
      return -1;
    }
    int j;
    try
    {
      j = paramContext.getPackageManager().getPackageUid(paramString, 0);
    }
    catch (PackageManager.NameNotFoundException paramContext)
    {
      paramContext = new StringBuilder();
      paramContext.append("Cannot find package uid. pkg = ");
      paramContext.append(paramString);
      loge(paramContext.toString());
      j = i;
    }
    return j;
  }
  
  private int getPreciseDisconnectCauseFromReasonInfo(ImsReasonInfo paramImsReasonInfo)
  {
    return PRECISE_CAUSE_MAP.get(maybeRemapReasonCode(paramImsReasonInfo), 65535);
  }
  
  private void handleDataEnabledChange(boolean paramBoolean, int paramInt)
  {
    Object localObject1;
    Object localObject2;
    Object localObject3;
    if (!paramBoolean)
    {
      localObject1 = mConnections.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (ImsPhoneConnection)((Iterator)localObject1).next();
        localObject3 = ((ImsPhoneConnection)localObject2).getImsCall();
        if ((localObject3 != null) && (((ImsCall)localObject3).isVideoCall()) && (!((ImsCall)localObject3).isWifiCall()))
        {
          localObject3 = new StringBuilder();
          ((StringBuilder)localObject3).append("handleDataEnabledChange - downgrading ");
          ((StringBuilder)localObject3).append(localObject2);
          log(((StringBuilder)localObject3).toString());
          downgradeVideoCall(paramInt, (ImsPhoneConnection)localObject2);
        }
      }
    }
    if (mSupportPauseVideo)
    {
      localObject2 = mConnections.iterator();
      while (((Iterator)localObject2).hasNext())
      {
        localObject3 = (ImsPhoneConnection)((Iterator)localObject2).next();
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("handleDataEnabledChange - resuming ");
        ((StringBuilder)localObject1).append(localObject3);
        log(((StringBuilder)localObject1).toString());
        if ((VideoProfile.isPaused(((ImsPhoneConnection)localObject3).getVideoState())) && (((ImsPhoneConnection)localObject3).wasVideoPausedFromSource(2))) {
          ((ImsPhoneConnection)localObject3).resumeVideo(2);
        }
      }
      mShouldUpdateImsConfigOnDisconnect = false;
    }
  }
  
  private void handleEcmTimer(int paramInt)
  {
    mPhone.handleTimerInEmergencyCallbackMode(paramInt);
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("handleEcmTimer, unsupported action ");
      localStringBuilder.append(paramInt);
      log(localStringBuilder.toString());
      break;
    case 1: 
      break;
    }
  }
  
  private void handleFeatureCapabilityChanged(ImsFeature.Capabilities paramCapabilities)
  {
    boolean bool1 = isVideoCallEnabled();
    StringBuilder localStringBuilder = new StringBuilder(120);
    localStringBuilder.append("handleFeatureCapabilityChanged: ");
    localStringBuilder.append(paramCapabilities);
    mMmTelCapabilities = new MmTelFeature.MmTelCapabilities(paramCapabilities);
    boolean bool2 = isVideoCallEnabled();
    if (bool1 != bool2) {
      bool1 = true;
    } else {
      bool1 = false;
    }
    localStringBuilder.append(" isVideoEnabledStateChanged=");
    localStringBuilder.append(bool1);
    if (bool1)
    {
      paramCapabilities = new StringBuilder();
      paramCapabilities.append("handleFeatureCapabilityChanged - notifyForVideoCapabilityChanged=");
      paramCapabilities.append(bool2);
      log(paramCapabilities.toString());
      mPhone.notifyForVideoCapabilityChanged(bool2);
    }
    log(localStringBuilder.toString());
    paramCapabilities = new StringBuilder();
    paramCapabilities.append("handleFeatureCapabilityChanged: isVolteEnabled=");
    paramCapabilities.append(isVolteEnabled());
    paramCapabilities.append(", isVideoCallEnabled=");
    paramCapabilities.append(isVideoCallEnabled());
    paramCapabilities.append(", isVowifiEnabled=");
    paramCapabilities.append(isVowifiEnabled());
    paramCapabilities.append(", isUtEnabled=");
    paramCapabilities.append(isUtEnabled());
    log(paramCapabilities.toString());
    mPhone.onFeatureCapabilityChanged();
    if ((!isVolteEnabled()) && (!isVideoCallEnabled()) && (!isVowifiEnabled())) {
      mPhone.setServiceState(1);
    } else {
      mPhone.setServiceState(0);
    }
    mMetrics.writeOnImsCapabilities(mPhone.getPhoneId(), getImsRegistrationTech(), mMmTelCapabilities);
  }
  
  private void handleRadioNotAvailable()
  {
    pollCallsWhenSafe();
  }
  
  private void internalClearDisconnected()
  {
    mRingingCall.clearDisconnected();
    mForegroundCall.clearDisconnected();
    mBackgroundCall.clearDisconnected();
    mHandoverCall.clearDisconnected();
  }
  
  private boolean isPhoneInEcbMode()
  {
    boolean bool;
    if ((mPhone != null) && (mPhone.isInEcm())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isWifiConnected()
  {
    Object localObject = (ConnectivityManager)mPhone.getContext().getSystemService("connectivity");
    boolean bool = false;
    if (localObject != null)
    {
      localObject = ((ConnectivityManager)localObject).getActiveNetworkInfo();
      if ((localObject != null) && (((NetworkInfo)localObject).isConnected()))
      {
        if (((NetworkInfo)localObject).getType() == 1) {
          bool = true;
        }
        return bool;
      }
    }
    return false;
  }
  
  private void maybeNotifyDataDisabled(boolean paramBoolean, int paramInt)
  {
    if (!paramBoolean)
    {
      Iterator localIterator = mConnections.iterator();
      while (localIterator.hasNext())
      {
        ImsPhoneConnection localImsPhoneConnection = (ImsPhoneConnection)localIterator.next();
        ImsCall localImsCall = localImsPhoneConnection.getImsCall();
        if ((localImsCall != null) && (localImsCall.isVideoCall()) && (!localImsCall.isWifiCall()) && (localImsPhoneConnection.hasCapabilities(3))) {
          if (paramInt == 1406) {
            localImsPhoneConnection.onConnectionEvent("android.telephony.event.EVENT_DOWNGRADE_DATA_DISABLED", null);
          } else if (paramInt == 1405) {
            localImsPhoneConnection.onConnectionEvent("android.telephony.event.EVENT_DOWNGRADE_DATA_LIMIT_REACHED", null);
          }
        }
      }
    }
  }
  
  private void maybeSetVideoCallProvider(ImsPhoneConnection paramImsPhoneConnection, ImsCall paramImsCall)
  {
    if ((paramImsPhoneConnection.getVideoProvider() == null) && (paramImsCall.getCallSession().getVideoCallProvider() != null))
    {
      try
      {
        setVideoCallProvider(paramImsPhoneConnection, paramImsCall);
      }
      catch (RemoteException paramImsCall)
      {
        paramImsPhoneConnection = new StringBuilder();
        paramImsPhoneConnection.append("maybeSetVideoCallProvider: exception ");
        paramImsPhoneConnection.append(paramImsCall);
        loge(paramImsPhoneConnection.toString());
      }
      return;
    }
  }
  
  private void modifyVideoCall(ImsCall paramImsCall, int paramInt)
  {
    paramImsCall = findConnection(paramImsCall);
    if (paramImsCall != null)
    {
      int i = paramImsCall.getVideoState();
      if (paramImsCall.getVideoProvider() != null) {
        paramImsCall.getVideoProvider().onSendSessionModifyRequest(new VideoProfile(i), new VideoProfile(paramInt));
      }
    }
  }
  
  private void notifyPhoneStateChanged(PhoneConstants.State paramState1, PhoneConstants.State paramState2)
  {
    Iterator localIterator = mPhoneStateListeners.iterator();
    while (localIterator.hasNext()) {
      ((PhoneStateListener)localIterator.next()).onPhoneStateChanged(paramState1, paramState2);
    }
  }
  
  private void onDataEnabledChanged(boolean paramBoolean, int paramInt)
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("onDataEnabledChanged: enabled=");
    ((StringBuilder)localObject1).append(paramBoolean);
    ((StringBuilder)localObject1).append(", reason=");
    ((StringBuilder)localObject1).append(paramInt);
    log(((StringBuilder)localObject1).toString());
    mIsDataEnabled = paramBoolean;
    Object localObject2;
    if (!mIsViLteDataMetered)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("Ignore data ");
      if (paramBoolean) {
        localObject1 = "enabled";
      } else {
        localObject1 = "disabled";
      }
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append(" - carrier policy indicates that data is not metered for ViLTE calls.");
      log(((StringBuilder)localObject2).toString());
      return;
    }
    Iterator localIterator = mConnections.iterator();
    for (;;)
    {
      boolean bool1 = localIterator.hasNext();
      boolean bool2 = true;
      if (!bool1) {
        break;
      }
      localObject1 = (ImsPhoneConnection)localIterator.next();
      localObject2 = ((ImsPhoneConnection)localObject1).getImsCall();
      bool1 = bool2;
      if (!paramBoolean) {
        if ((localObject2 != null) && (((ImsCall)localObject2).isWifiCall())) {
          bool1 = bool2;
        } else {
          bool1 = false;
        }
      }
      ((ImsPhoneConnection)localObject1).setVideoEnabled(bool1);
    }
    int i;
    if (paramInt == 3) {
      i = 1405;
    }
    for (;;)
    {
      break;
      if (paramInt == 2) {
        i = 1406;
      } else {
        i = 1406;
      }
    }
    maybeNotifyDataDisabled(paramBoolean, i);
    handleDataEnabledChange(paramBoolean, i);
    if ((!mShouldUpdateImsConfigOnDisconnect) && (paramInt != 0) && (mCarrierConfigLoaded) && (mImsManager != null)) {
      mImsManager.updateImsServiceConfig(true);
    }
  }
  
  private void processCallStateChange(ImsCall paramImsCall, Call.State paramState, int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("processCallStateChange ");
    localStringBuilder.append(paramImsCall);
    localStringBuilder.append(" state=");
    localStringBuilder.append(paramState);
    localStringBuilder.append(" cause=");
    localStringBuilder.append(paramInt);
    log(localStringBuilder.toString());
    processCallStateChange(paramImsCall, paramState, paramInt, false);
  }
  
  private void processCallStateChange(ImsCall paramImsCall, Call.State paramState, int paramInt, boolean paramBoolean)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("processCallStateChange state=");
    ((StringBuilder)localObject).append(paramState);
    ((StringBuilder)localObject).append(" cause=");
    ((StringBuilder)localObject).append(paramInt);
    ((StringBuilder)localObject).append(" ignoreState=");
    ((StringBuilder)localObject).append(paramBoolean);
    log(((StringBuilder)localObject).toString());
    if (paramImsCall == null) {
      return;
    }
    localObject = findConnection(paramImsCall);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("processCallStateChange(): conn = ");
    localStringBuilder.append(localObject);
    localStringBuilder.append(", imsCall = ");
    localStringBuilder.append(paramImsCall);
    log(localStringBuilder.toString());
    if (localObject == null) {
      return;
    }
    ((ImsPhoneConnection)localObject).updateMediaCapabilities(paramImsCall);
    if (paramBoolean)
    {
      ((ImsPhoneConnection)localObject).updateAddressDisplay(paramImsCall);
      ((ImsPhoneConnection)localObject).updateExtras(paramImsCall);
      maybeSetVideoCallProvider((ImsPhoneConnection)localObject, paramImsCall);
      return;
    }
    boolean bool = ((ImsPhoneConnection)localObject).update(paramImsCall, paramState);
    paramBoolean = bool;
    if (paramState == Call.State.DISCONNECTED)
    {
      if ((!((ImsPhoneConnection)localObject).onDisconnect(paramInt)) && (!bool)) {
        paramBoolean = false;
      } else {
        paramBoolean = true;
      }
      ((ImsPhoneConnection)localObject).getCall().detach((ImsPhoneConnection)localObject);
      removeConnection((ImsPhoneConnection)localObject);
    }
    paramImsCall = new StringBuilder();
    paramImsCall.append("processCallStateChange(): changed = ");
    paramImsCall.append(paramBoolean);
    log(paramImsCall.toString());
    if (paramBoolean)
    {
      if (((ImsPhoneConnection)localObject).getCall() == mHandoverCall) {
        return;
      }
      log("processCallStateChange(): update phone state and notify");
      updatePhoneState();
      mPhone.notifyPreciseCallStateChanged();
    }
  }
  
  private void registerForConnectivityChanges()
  {
    if ((!mIsMonitoringConnectivity) && (mNotifyVtHandoverToWifiFail))
    {
      ConnectivityManager localConnectivityManager = (ConnectivityManager)mPhone.getContext().getSystemService("connectivity");
      if (localConnectivityManager != null)
      {
        Rlog.i("ImsPhoneCallTracker", "registerForConnectivityChanges");
        NetworkCapabilities localNetworkCapabilities = new NetworkCapabilities();
        localNetworkCapabilities.addTransportType(1);
        NetworkRequest.Builder localBuilder = new NetworkRequest.Builder();
        localBuilder.setCapabilities(localNetworkCapabilities);
        localConnectivityManager.registerNetworkCallback(localBuilder.build(), mNetworkCallback);
        mIsMonitoringConnectivity = true;
      }
      return;
    }
  }
  
  private void removeConnection(ImsPhoneConnection paramImsPhoneConnection)
  {
    try
    {
      mConnections.remove(paramImsPhoneConnection);
      if (mIsInEmergencyCall)
      {
        int i = 0;
        paramImsPhoneConnection = mConnections.iterator();
        int j;
        for (;;)
        {
          j = i;
          if (!paramImsPhoneConnection.hasNext()) {
            break;
          }
          ImsPhoneConnection localImsPhoneConnection = (ImsPhoneConnection)paramImsPhoneConnection.next();
          if ((localImsPhoneConnection != null) && (localImsPhoneConnection.isEmergency() == true))
          {
            j = 1;
            break;
          }
        }
        if (j == 0)
        {
          mIsInEmergencyCall = false;
          mPhone.sendEmergencyCallStateChange(false);
        }
      }
      return;
    }
    finally {}
  }
  
  private void resetImsCapabilities()
  {
    log("Resetting Capabilities...");
    boolean bool1 = isVideoCallEnabled();
    if (mIgnoreResetUtCapability)
    {
      mMmTelCapabilities.removeCapabilities(1);
      mMmTelCapabilities.removeCapabilities(2);
      mMmTelCapabilities.removeCapabilities(8);
    }
    else
    {
      mMmTelCapabilities = new MmTelFeature.MmTelCapabilities();
    }
    boolean bool2 = isVideoCallEnabled();
    if (bool1 != bool2) {
      mPhone.notifyForVideoCapabilityChanged(bool2);
    }
  }
  
  private void retryGetImsService()
  {
    if (mImsManager.isServiceAvailable()) {
      return;
    }
    mImsManagerConnector.connect();
  }
  
  private void scheduleHandoverCheck()
  {
    ImsCall localImsCall = mForegroundCall.getImsCall();
    ImsPhoneConnection localImsPhoneConnection = mForegroundCall.getFirstConnection();
    if ((mNotifyVtHandoverToWifiFail) && (localImsCall != null) && (localImsCall.isVideoCall()) && (localImsPhoneConnection != null) && (localImsPhoneConnection.getDisconnectCause() == 0))
    {
      if (!hasMessages(25))
      {
        Rlog.i("ImsPhoneCallTracker", "scheduleHandoverCheck: schedule");
        sendMessageDelayed(obtainMessage(25, localImsCall), 60000L);
      }
      return;
    }
  }
  
  private ImsCallProfile setRttModeBasedOnOperator(ImsCallProfile paramImsCallProfile)
  {
    if ((mPhone.isRttSupported()) && (mPhone.isRttOn()))
    {
      int i = QtiImsExtUtils.getRttOperatingMode(mPhone.getContext());
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("RTT: setRttModeBasedOnOperator mode = ");
      localStringBuilder.append(i);
      log(localStringBuilder.toString());
      if ((!QtiImsExtUtils.isRttSupportedOnVtCalls(mPhone.getPhoneId(), mPhone.getContext())) && (paramImsCallProfile.isVideoCall())) {
        return paramImsCallProfile;
      }
      mMediaProfile.setRttMode(i);
      return paramImsCallProfile;
    }
    return paramImsCallProfile;
  }
  
  private void setVideoCallProvider(ImsPhoneConnection paramImsPhoneConnection, ImsCall paramImsCall)
    throws RemoteException
  {
    Object localObject = paramImsCall.getCallSession().getVideoCallProvider();
    if (localObject != null)
    {
      boolean bool = mPhone.getContext().getResources().getBoolean(17957070);
      localObject = new ImsVideoCallProviderWrapper((IImsVideoCallProvider)localObject);
      if (bool) {
        ((ImsVideoCallProviderWrapper)localObject).setUseVideoPauseWorkaround(bool);
      }
      paramImsPhoneConnection.setVideoProvider((Connection.VideoProvider)localObject);
      ((ImsVideoCallProviderWrapper)localObject).registerForDataUsageUpdate(this, 22, paramImsCall);
      ((ImsVideoCallProviderWrapper)localObject).addImsVideoProviderCallback(paramImsPhoneConnection);
    }
  }
  
  private boolean shouldDisconnectActiveCallOnAnswer(ImsCall paramImsCall1, ImsCall paramImsCall2)
  {
    boolean bool1 = false;
    if ((paramImsCall1 != null) && (paramImsCall2 != null))
    {
      if (!mDropVideoCallWhenAnsweringAudioCall) {
        return false;
      }
      boolean bool2;
      if ((!paramImsCall1.isVideoCall()) && ((!mTreatDowngradedVideoCallsAsVideoCalls) || (!paramImsCall1.wasVideoCall()))) {
        bool2 = false;
      } else {
        bool2 = true;
      }
      boolean bool3 = paramImsCall1.isWifiCall();
      boolean bool4;
      if ((mImsManager.isWfcEnabledByPlatform()) && (mImsManager.isWfcEnabledByUser())) {
        bool4 = true;
      } else {
        bool4 = false;
      }
      boolean bool5 = paramImsCall2.isVideoCall() ^ true;
      paramImsCall1 = new StringBuilder();
      paramImsCall1.append("shouldDisconnectActiveCallOnAnswer : isActiveCallVideo=");
      paramImsCall1.append(bool2);
      paramImsCall1.append(" isActiveCallOnWifi=");
      paramImsCall1.append(bool3);
      paramImsCall1.append(" isIncomingCallAudio=");
      paramImsCall1.append(bool5);
      paramImsCall1.append(" isVowifiEnabled=");
      paramImsCall1.append(bool4);
      log(paramImsCall1.toString());
      boolean bool6 = bool1;
      if (bool2)
      {
        bool6 = bool1;
        if (bool3)
        {
          bool6 = bool1;
          if (bool5)
          {
            bool6 = bool1;
            if (!bool4) {
              bool6 = true;
            }
          }
        }
      }
      return bool6;
    }
    return false;
  }
  
  private boolean shouldDisconnectActiveCallOnDial(boolean paramBoolean)
  {
    boolean bool1 = mAllowHoldingVideoCall;
    boolean bool2 = false;
    if (bool1) {
      return false;
    }
    boolean bool3 = false;
    bool1 = bool3;
    if (mForegroundCall.getState() == Call.State.ACTIVE)
    {
      ImsCall localImsCall = mForegroundCall.getImsCall();
      bool1 = bool3;
      if (localImsCall != null) {
        bool1 = localImsCall.isVideoCall();
      }
    }
    bool3 = bool2;
    if (bool1)
    {
      bool3 = bool2;
      if (paramBoolean) {
        bool3 = true;
      }
    }
    return bool3;
  }
  
  private boolean shouldNumberBePlacedOnIms(boolean paramBoolean, String paramString)
  {
    try
    {
      if (mImsManager != null)
      {
        int i = mImsManager.shouldProcessCall(paramBoolean, new String[] { paramString });
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("shouldProcessCall: number: ");
        localStringBuilder.append(Rlog.pii("ImsPhoneCallTracker", paramString));
        localStringBuilder.append(", result: ");
        localStringBuilder.append(i);
        Rlog.i("ImsPhoneCallTracker", localStringBuilder.toString());
        switch (i)
        {
        default: 
          Rlog.w("ImsPhoneCallTracker", "shouldProcessCall returned unknown result.");
          return false;
        case 1: 
          Rlog.i("ImsPhoneCallTracker", "shouldProcessCall: place over CSFB instead.");
          return false;
        }
        return true;
      }
      Rlog.w("ImsPhoneCallTracker", "ImsManager unavailable, shouldProcessCall returning false.");
      return false;
    }
    catch (ImsException paramString)
    {
      Rlog.w("ImsPhoneCallTracker", "ImsService unavailable, shouldProcessCall returning false.");
    }
    return false;
  }
  
  private void startListeningForCalls()
    throws ImsException
  {
    log("startListeningForCalls");
    mImsManager.open(mMmTelFeatureListener);
    mImsManager.addRegistrationCallback(mImsRegistrationCallback);
    mImsManager.addCapabilitiesCallback(mImsCapabilityCallback);
    mImsManager.setConfigListener(mImsConfigListener);
    mImsManager.getConfigInterface().addConfigCallback(mConfigCallback);
    getEcbmInterface().setEcbmStateListener(mPhone.getImsEcbmStateListener());
    if (mPhone.isInEcm()) {
      mPhone.exitEmergencyCallbackMode();
    }
    int i = Settings.Secure.getInt(mPhone.getContext().getContentResolver(), "preferred_tty_mode", 0);
    mImsManager.setUiTTYMode(mPhone.getContext(), i, null);
    ImsMultiEndpoint localImsMultiEndpoint = getMultiEndpointInterface();
    if (localImsMultiEndpoint != null) {
      localImsMultiEndpoint.setExternalCallStateListener(mPhone.getExternalCallTracker().getExternalCallStateListener());
    }
    mUtInterface = getUtInterface();
    if (mUtInterface != null) {
      mUtInterface.registerForSuppServiceIndication(this, 27, null);
    }
    if (mCarrierConfigLoaded) {
      mImsManager.updateImsServiceConfig(true);
    }
  }
  
  private void stopListeningForCalls()
  {
    log("stopListeningForCalls");
    resetImsCapabilities();
    if (mImsManager != null)
    {
      try
      {
        mImsManager.getConfigInterface().removeConfigCallback(mConfigCallback);
      }
      catch (ImsException localImsException)
      {
        Log.w("ImsPhoneCallTracker", "stopListeningForCalls: unable to remove config callback.");
      }
      mImsManager.close();
    }
  }
  
  private void switchAfterConferenceSuccess()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("switchAfterConferenceSuccess fg =");
    localStringBuilder.append(mForegroundCall.getState());
    localStringBuilder.append(", bg = ");
    localStringBuilder.append(mBackgroundCall.getState());
    log(localStringBuilder.toString());
    if (mBackgroundCall.getState() == Call.State.HOLDING)
    {
      log("switchAfterConferenceSuccess");
      mForegroundCall.switchWith(mBackgroundCall);
    }
  }
  
  private void transferHandoverConnections(ImsPhoneCall paramImsPhoneCall)
  {
    Object localObject1;
    Object localObject2;
    if (mConnections != null)
    {
      localObject1 = mConnections.iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (Connection)((Iterator)localObject1).next();
        mPreHandoverState = mState;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Connection state before handover is ");
        localStringBuilder.append(((Connection)localObject2).getStateBeforeHandover());
        log(localStringBuilder.toString());
      }
    }
    if (mHandoverCall.mConnections == null) {
      mHandoverCall.mConnections = mConnections;
    } else {
      mHandoverCall.mConnections.addAll(mConnections);
    }
    if (mHandoverCall.mConnections != null)
    {
      if (paramImsPhoneCall.getImsCall() != null) {
        paramImsPhoneCall.getImsCall().close();
      }
      localObject2 = mHandoverCall.mConnections.iterator();
      while (((Iterator)localObject2).hasNext())
      {
        localObject1 = (Connection)((Iterator)localObject2).next();
        ((ImsPhoneConnection)localObject1).changeParent(mHandoverCall);
        ((ImsPhoneConnection)localObject1).releaseWakeLock();
      }
    }
    if (paramImsPhoneCall.getState().isAlive())
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("Call is alive and state is ");
      ((StringBuilder)localObject2).append(mState);
      log(((StringBuilder)localObject2).toString());
      mHandoverCall.mState = mState;
    }
    mConnections.clear();
    mState = Call.State.IDLE;
  }
  
  private void unregisterForConnectivityChanges()
  {
    if ((mIsMonitoringConnectivity) && (mNotifyVtHandoverToWifiFail))
    {
      ConnectivityManager localConnectivityManager = (ConnectivityManager)mPhone.getContext().getSystemService("connectivity");
      if (localConnectivityManager != null)
      {
        Rlog.i("ImsPhoneCallTracker", "unregisterForConnectivityChanges");
        localConnectivityManager.unregisterNetworkCallback(mNetworkCallback);
        mIsMonitoringConnectivity = false;
      }
      return;
    }
  }
  
  private void updatePhoneState()
  {
    PhoneConstants.State localState = mState;
    int i;
    if ((mPendingMO != null) && (mPendingMO.getState().isAlive())) {
      i = 0;
    } else {
      i = 1;
    }
    if (mRingingCall.isRinging()) {
      mState = PhoneConstants.State.RINGING;
    } else if ((i != 0) && (mForegroundCall.isIdle()) && (mBackgroundCall.isIdle())) {
      mState = PhoneConstants.State.IDLE;
    } else {
      mState = PhoneConstants.State.OFFHOOK;
    }
    if ((mState == PhoneConstants.State.IDLE) && (localState != mState))
    {
      ImsPhoneConnection.clearNumberRecords();
      mVoiceCallEndedRegistrants.notifyRegistrants(new AsyncResult(null, null, null));
    }
    else if ((localState == PhoneConstants.State.IDLE) && (localState != mState))
    {
      mVoiceCallStartedRegistrants.notifyRegistrants(new AsyncResult(null, null, null));
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("updatePhoneState pendingMo = ");
    if (mPendingMO == null) {
      localObject = "null";
    } else {
      localObject = mPendingMO.getState();
    }
    localStringBuilder.append(localObject);
    localStringBuilder.append(", fg= ");
    localStringBuilder.append(mForegroundCall.getState());
    localStringBuilder.append("(");
    localStringBuilder.append(mForegroundCall.getConnections().size());
    localStringBuilder.append("), bg= ");
    localStringBuilder.append(mBackgroundCall.getState());
    localStringBuilder.append("(");
    localStringBuilder.append(mBackgroundCall.getConnections().size());
    localStringBuilder.append(")");
    log(localStringBuilder.toString());
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("updatePhoneState oldState=");
    ((StringBuilder)localObject).append(localState);
    ((StringBuilder)localObject).append(", newState=");
    ((StringBuilder)localObject).append(mState);
    log(((StringBuilder)localObject).toString());
    if (mState != localState)
    {
      mPhone.notifyPhoneStateChanged();
      mMetrics.writePhoneState(mPhone.getPhoneId(), mState);
      notifyPhoneStateChanged(localState, mState);
    }
  }
  
  private void updateVtDataUsage(ImsCall paramImsCall, long paramLong)
  {
    long l = 0L;
    if (mVtDataUsageMap.containsKey(Integer.valueOf(uniqueId))) {
      l = ((Long)mVtDataUsageMap.get(Integer.valueOf(uniqueId))).longValue();
    }
    l = paramLong - l;
    mVtDataUsageMap.put(Integer.valueOf(uniqueId), Long.valueOf(paramLong));
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("updateVtDataUsage: call=");
    ((StringBuilder)localObject).append(paramImsCall);
    ((StringBuilder)localObject).append(", delta=");
    ((StringBuilder)localObject).append(l);
    log(((StringBuilder)localObject).toString());
    paramLong = SystemClock.elapsedRealtime();
    int i = mPhone.getServiceState().getDataRoaming();
    paramImsCall = new NetworkStats(paramLong, 1);
    paramImsCall.combineAllValues(mVtDataUsageSnapshot);
    paramImsCall.combineValues(new NetworkStats.Entry("vt_data0", -1, 1, 0, 1, i, 1, l / 2L, 0L, l / 2L, 0L, 0L));
    mVtDataUsageSnapshot = paramImsCall;
    paramImsCall = new NetworkStats(paramLong, 1);
    paramImsCall.combineAllValues(mVtDataUsageUidSnapshot);
    if (mDefaultDialerUid.get() == -1)
    {
      localObject = (TelecomManager)mPhone.getContext().getSystemService("telecom");
      mDefaultDialerUid.set(getPackageUid(mPhone.getContext(), ((TelecomManager)localObject).getDefaultDialerPackage()));
    }
    paramImsCall.combineValues(new NetworkStats.Entry("vt_data0", mDefaultDialerUid.get(), 1, 0, 1, i, 1, l / 2L, 0L, l / 2L, 0L, 0L));
    mVtDataUsageUidSnapshot = paramImsCall;
  }
  
  public void acceptCall(int paramInt)
    throws CallStateException
  {
    log("acceptCall");
    if ((mForegroundCall.getState().isAlive()) && (mBackgroundCall.getState().isAlive())) {
      throw new CallStateException("cannot accept call");
    }
    ImsStreamMediaProfile localImsStreamMediaProfile = new ImsStreamMediaProfile();
    ImsCall localImsCall2;
    if ((mRingingCall.getState() == Call.State.WAITING) && (mForegroundCall.getState().isAlive()))
    {
      setMute(false);
      boolean bool1 = false;
      ImsCall localImsCall1 = mForegroundCall.getImsCall();
      localImsCall2 = mRingingCall.getImsCall();
      boolean bool2 = bool1;
      if (mForegroundCall.hasConnections())
      {
        bool2 = bool1;
        if (mRingingCall.hasConnections()) {
          bool2 = shouldDisconnectActiveCallOnAnswer(localImsCall1, localImsCall2);
        }
      }
      mPendingCallVideoState = paramInt;
      if (bool2)
      {
        mForegroundCall.hangup();
        try
        {
          localImsStreamMediaProfile = addRttAttributeIfRequired(localImsCall2, localImsStreamMediaProfile);
          localImsCall2.accept(ImsCallProfile.getCallTypeFromVideoState(paramInt), localImsStreamMediaProfile);
        }
        catch (ImsException localImsException1)
        {
          throw new CallStateException("cannot accept call");
        }
      }
      else
      {
        switchWaitingOrHoldingAndActive();
      }
    }
    else
    {
      if (!mRingingCall.getState().isRinging()) {
        break label302;
      }
      log("acceptCall: incoming...");
      setMute(false);
    }
    try
    {
      localImsCall2 = mRingingCall.getImsCall();
      if (localImsCall2 != null)
      {
        localObject = addRttAttributeIfRequired(localImsCall2, localImsException1);
        localImsCall2.accept(ImsCallProfile.getCallTypeFromVideoState(paramInt), (ImsStreamMediaProfile)localObject);
        mMetrics.writeOnImsCommand(mPhone.getPhoneId(), localImsCall2.getSession(), 2);
        return;
      }
      Object localObject = new com/android/internal/telephony/CallStateException;
      ((CallStateException)localObject).<init>("no valid ims call");
      throw ((Throwable)localObject);
    }
    catch (ImsException localImsException2)
    {
      throw new CallStateException("cannot accept call");
    }
    label302:
    throw new CallStateException("phone not ringing");
  }
  
  public void addParticipant(String paramString)
    throws CallStateException
  {
    if (mForegroundCall != null)
    {
      Object localObject = mForegroundCall.getImsCall();
      if (localObject == null)
      {
        loge("addParticipant : No foreground ims call");
      }
      else
      {
        localObject = ((ImsCall)localObject).getCallSession();
        if (localObject != null) {
          ((ImsCallSession)localObject).inviteParticipants(new String[] { paramString });
        } else {
          loge("addParticipant : ImsCallSession does not exist");
        }
      }
    }
    else
    {
      loge("addParticipant : Foreground call does not exist");
    }
  }
  
  @VisibleForTesting
  public void addReasonCodeRemapping(Integer paramInteger1, String paramString, Integer paramInteger2)
  {
    mImsReasonCodeMap.put(new Pair(paramInteger1, paramString), paramInteger2);
  }
  
  void callEndCleanupHandOverCallIfAny()
  {
    if (mHandoverCall.mConnections.size() > 0)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("callEndCleanupHandOverCallIfAny, mHandoverCall.mConnections=");
      localStringBuilder.append(mHandoverCall.mConnections);
      log(localStringBuilder.toString());
      mHandoverCall.mConnections.clear();
      mConnections.clear();
      mState = PhoneConstants.State.IDLE;
    }
  }
  
  public boolean canConference()
  {
    boolean bool;
    if ((mForegroundCall.getState() == Call.State.ACTIVE) && (mBackgroundCall.getState() == Call.State.HOLDING) && (!mBackgroundCall.isFull()) && (!mForegroundCall.isFull())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean canDial()
  {
    String str = SystemProperties.get("ro.telephony.disable-call", "false");
    boolean bool;
    if ((mPendingMO == null) && (!mRingingCall.isRinging()) && (!str.equals("true")) && ((!mForegroundCall.getState().isAlive()) || (!mBackgroundCall.getState().isAlive()))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean canTransfer()
  {
    boolean bool;
    if ((mForegroundCall.getState() == Call.State.ACTIVE) && (mBackgroundCall.getState() == Call.State.HOLDING)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void cancelUSSD()
  {
    if (mUssdSession == null) {
      return;
    }
    try
    {
      mUssdSession.terminate(501);
    }
    catch (ImsException localImsException) {}
  }
  
  public void clearDisconnected()
  {
    log("clearDisconnected");
    internalClearDisconnected();
    updatePhoneState();
    mPhone.notifyPreciseCallStateChanged();
  }
  
  public void conference()
  {
    ImsCall localImsCall1 = mForegroundCall.getImsCall();
    if (localImsCall1 == null)
    {
      log("conference no foreground ims call");
      return;
    }
    ImsCall localImsCall2 = mBackgroundCall.getImsCall();
    if (localImsCall2 == null)
    {
      log("conference no background ims call");
      return;
    }
    if (localImsCall1.isCallSessionMergePending())
    {
      log("conference: skip; foreground call already in process of merging.");
      return;
    }
    if (localImsCall2.isCallSessionMergePending())
    {
      log("conference: skip; background call already in process of merging.");
      return;
    }
    long l1 = mForegroundCall.getEarliestConnectTime();
    long l2 = mBackgroundCall.getEarliestConnectTime();
    if ((l1 > 0L) && (l2 > 0L))
    {
      l2 = Math.min(mForegroundCall.getEarliestConnectTime(), mBackgroundCall.getEarliestConnectTime());
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("conference - using connect time = ");
      ((StringBuilder)localObject1).append(l2);
      log(((StringBuilder)localObject1).toString());
    }
    else if (l1 > 0L)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("conference - bg call connect time is 0; using fg = ");
      ((StringBuilder)localObject1).append(l1);
      log(((StringBuilder)localObject1).toString());
      l2 = l1;
    }
    else
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("conference - fg call connect time is 0; using bg = ");
      ((StringBuilder)localObject1).append(l2);
      log(((StringBuilder)localObject1).toString());
    }
    Object localObject1 = "";
    Object localObject2 = mForegroundCall.getFirstConnection();
    if (localObject2 != null)
    {
      ((ImsPhoneConnection)localObject2).setConferenceConnectTime(l2);
      ((ImsPhoneConnection)localObject2).handleMergeStart();
      localObject1 = ((ImsPhoneConnection)localObject2).getTelecomCallId();
    }
    localObject2 = "";
    Object localObject3 = findConnection(localImsCall2);
    if (localObject3 != null)
    {
      ((ImsPhoneConnection)localObject3).handleMergeStart();
      localObject2 = ((ImsPhoneConnection)localObject3).getTelecomCallId();
    }
    localObject3 = new StringBuilder();
    ((StringBuilder)localObject3).append("conference: fgCallId=");
    ((StringBuilder)localObject3).append((String)localObject1);
    ((StringBuilder)localObject3).append(", bgCallId=");
    ((StringBuilder)localObject3).append((String)localObject2);
    log(((StringBuilder)localObject3).toString());
    try
    {
      localImsCall1.merge(localImsCall2);
    }
    catch (ImsException localImsException)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("conference ");
      ((StringBuilder)localObject1).append(localImsException.getMessage());
      log(((StringBuilder)localObject1).toString());
    }
  }
  
  public Connection dial(String paramString, int paramInt, Bundle paramBundle)
    throws CallStateException
  {
    return dial(paramString, ((ImsPhone.ImsDialArgs.Builder)((ImsPhone.ImsDialArgs.Builder)new ImsPhone.ImsDialArgs.Builder().setIntentExtras(paramBundle)).setVideoState(paramInt)).setClirMode(getClirMode()).build());
  }
  
  /* Error */
  public Connection dial(String paramString, ImsPhone.ImsDialArgs paramImsDialArgs)
    throws CallStateException
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial 936	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:isPhoneInEcbMode	()Z
    //   6: istore_3
    //   7: aload_0
    //   8: getfield 382	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:mPhoneNumberUtilsProxy	Lcom/android/internal/telephony/imsphone/ImsPhoneCallTracker$PhoneNumberUtilsProxy;
    //   11: aload_1
    //   12: invokeinterface 827 2 0
    //   17: istore 4
    //   19: aload_0
    //   20: iload 4
    //   22: aload_1
    //   23: invokespecial 1837	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:shouldNumberBePlacedOnIms	(ZLjava/lang/String;)Z
    //   26: ifeq +662 -> 688
    //   29: aload_2
    //   30: getfield 1842	com/android/internal/telephony/imsphone/ImsPhone$ImsDialArgs:clirMode	I
    //   33: istore 5
    //   35: aload_2
    //   36: getfield 1845	com/android/internal/telephony/imsphone/ImsPhone$ImsDialArgs:videoState	I
    //   39: istore 6
    //   41: new 686	java/lang/StringBuilder
    //   44: astore 7
    //   46: aload 7
    //   48: invokespecial 687	java/lang/StringBuilder:<init>	()V
    //   51: aload 7
    //   53: ldc_w 1847
    //   56: invokevirtual 692	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   59: pop
    //   60: aload 7
    //   62: iload 5
    //   64: invokevirtual 735	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   67: pop
    //   68: aload_0
    //   69: aload 7
    //   71: invokevirtual 698	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   74: invokevirtual 701	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:log	(Ljava/lang/String;)V
    //   77: iload 4
    //   79: ifeq +41 -> 120
    //   82: iconst_2
    //   83: istore 5
    //   85: new 686	java/lang/StringBuilder
    //   88: astore 7
    //   90: aload 7
    //   92: invokespecial 687	java/lang/StringBuilder:<init>	()V
    //   95: aload 7
    //   97: ldc_w 1849
    //   100: invokevirtual 692	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   103: pop
    //   104: aload 7
    //   106: iconst_2
    //   107: invokevirtual 735	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   110: pop
    //   111: aload_0
    //   112: aload 7
    //   114: invokevirtual 698	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   117: invokevirtual 701	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:log	(Ljava/lang/String;)V
    //   120: aload_0
    //   121: invokevirtual 1850	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:clearDisconnected	()V
    //   124: aload_0
    //   125: getfield 498	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:mImsManager	Lcom/android/ims/ImsManager;
    //   128: ifnull +547 -> 675
    //   131: aload_0
    //   132: invokevirtual 1852	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:canDial	()Z
    //   135: ifeq +527 -> 662
    //   138: aload_0
    //   139: iload 6
    //   141: invokespecial 1854	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:canAddVideoCallDuringImsAudioCall	(I)Z
    //   144: ifeq +518 -> 662
    //   147: iload_3
    //   148: ifeq +13 -> 161
    //   151: iload 4
    //   153: ifeq +8 -> 161
    //   156: aload_0
    //   157: iconst_1
    //   158: invokespecial 1856	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:handleEcmTimer	(I)V
    //   161: iload 6
    //   163: istore 8
    //   165: iload 4
    //   167: ifeq +36 -> 203
    //   170: iload 6
    //   172: istore 8
    //   174: iload 6
    //   176: invokestatic 761	android/telecom/VideoProfile:isVideo	(I)Z
    //   179: ifeq +24 -> 203
    //   182: iload 6
    //   184: istore 8
    //   186: aload_0
    //   187: getfield 332	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:mAllowEmergencyVideoCalls	Z
    //   190: ifne +13 -> 203
    //   193: aload_0
    //   194: ldc_w 1858
    //   197: invokevirtual 726	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:loge	(Ljava/lang/String;)V
    //   200: iconst_0
    //   201: istore 8
    //   203: iconst_0
    //   204: istore 6
    //   206: iconst_0
    //   207: istore 9
    //   209: iload 9
    //   211: istore 10
    //   213: aload_0
    //   214: getfield 265	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:mForegroundCall	Lcom/android/internal/telephony/imsphone/ImsPhoneCall;
    //   217: invokevirtual 744	com/android/internal/telephony/imsphone/ImsPhoneCall:getState	()Lcom/android/internal/telephony/Call$State;
    //   220: getstatic 750	com/android/internal/telephony/Call$State:ACTIVE	Lcom/android/internal/telephony/Call$State;
    //   223: if_acmpne +89 -> 312
    //   226: aload_0
    //   227: getfield 269	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:mBackgroundCall	Lcom/android/internal/telephony/imsphone/ImsPhoneCall;
    //   230: invokevirtual 744	com/android/internal/telephony/imsphone/ImsPhoneCall:getState	()Lcom/android/internal/telephony/Call$State;
    //   233: getstatic 1552	com/android/internal/telephony/Call$State:IDLE	Lcom/android/internal/telephony/Call$State;
    //   236: if_acmpne +63 -> 299
    //   239: iconst_1
    //   240: istore 6
    //   242: aload_0
    //   243: iload 8
    //   245: putfield 1686	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:mPendingCallVideoState	I
    //   248: aload_0
    //   249: aload_2
    //   250: getfield 1861	com/android/internal/telephony/imsphone/ImsPhone$ImsDialArgs:intentExtras	Landroid/os/Bundle;
    //   253: putfield 1863	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:mPendingIntentExtras	Landroid/os/Bundle;
    //   256: aload_0
    //   257: iload 4
    //   259: invokespecial 1865	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:shouldDisconnectActiveCallOnDial	(Z)Z
    //   262: ifeq +26 -> 288
    //   265: iconst_0
    //   266: istore 6
    //   268: iconst_1
    //   269: istore 10
    //   271: aload_0
    //   272: ldc_w 1867
    //   275: invokevirtual 701	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:log	(Ljava/lang/String;)V
    //   278: aload_0
    //   279: getfield 265	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:mForegroundCall	Lcom/android/internal/telephony/imsphone/ImsPhoneCall;
    //   282: invokevirtual 1689	com/android/internal/telephony/imsphone/ImsPhoneCall:hangup	()V
    //   285: goto +27 -> 312
    //   288: aload_0
    //   289: invokevirtual 1698	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:switchWaitingOrHoldingAndActive	()V
    //   292: iload 9
    //   294: istore 10
    //   296: goto +16 -> 312
    //   299: new 1672	com/android/internal/telephony/CallStateException
    //   302: astore_1
    //   303: aload_1
    //   304: ldc_w 1869
    //   307: invokespecial 1677	com/android/internal/telephony/CallStateException:<init>	(Ljava/lang/String;)V
    //   310: aload_1
    //   311: athrow
    //   312: getstatic 1552	com/android/internal/telephony/Call$State:IDLE	Lcom/android/internal/telephony/Call$State;
    //   315: astore 7
    //   317: getstatic 1552	com/android/internal/telephony/Call$State:IDLE	Lcom/android/internal/telephony/Call$State;
    //   320: astore 11
    //   322: aload_0
    //   323: iload 5
    //   325: putfield 291	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:mClirMode	I
    //   328: aload_0
    //   329: getfield 296	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:mSyncHold	Ljava/lang/Object;
    //   332: astore 12
    //   334: aload 12
    //   336: monitorenter
    //   337: iload 6
    //   339: istore 9
    //   341: aload 11
    //   343: astore 7
    //   345: iload 6
    //   347: ifeq +80 -> 427
    //   350: aload_0
    //   351: getfield 265	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:mForegroundCall	Lcom/android/internal/telephony/imsphone/ImsPhoneCall;
    //   354: invokevirtual 744	com/android/internal/telephony/imsphone/ImsPhoneCall:getState	()Lcom/android/internal/telephony/Call$State;
    //   357: astore 13
    //   359: aload_0
    //   360: getfield 269	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:mBackgroundCall	Lcom/android/internal/telephony/imsphone/ImsPhoneCall;
    //   363: invokevirtual 744	com/android/internal/telephony/imsphone/ImsPhoneCall:getState	()Lcom/android/internal/telephony/Call$State;
    //   366: astore 11
    //   368: aload 13
    //   370: getstatic 750	com/android/internal/telephony/Call$State:ACTIVE	Lcom/android/internal/telephony/Call$State;
    //   373: if_acmpeq +37 -> 410
    //   376: iload 6
    //   378: istore 9
    //   380: aload 13
    //   382: astore 7
    //   384: aload 11
    //   386: astore 7
    //   388: aload 11
    //   390: getstatic 753	com/android/internal/telephony/Call$State:HOLDING	Lcom/android/internal/telephony/Call$State;
    //   393: if_acmpne +34 -> 427
    //   396: iconst_0
    //   397: istore 9
    //   399: aload 13
    //   401: astore 7
    //   403: aload 11
    //   405: astore 7
    //   407: goto +20 -> 427
    //   410: new 1672	com/android/internal/telephony/CallStateException
    //   413: astore_1
    //   414: aload_1
    //   415: ldc_w 1869
    //   418: invokespecial 1677	com/android/internal/telephony/CallStateException:<init>	(Ljava/lang/String;)V
    //   421: aload_1
    //   422: athrow
    //   423: astore_1
    //   424: goto +229 -> 653
    //   427: new 651	com/android/internal/telephony/imsphone/ImsPhoneConnection
    //   430: astore 11
    //   432: aload_0
    //   433: getfield 402	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:mPhone	Lcom/android/internal/telephony/imsphone/ImsPhone;
    //   436: astore 7
    //   438: aload_0
    //   439: aload_1
    //   440: invokevirtual 1872	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:checkForTestEmergencyNumber	(Ljava/lang/String;)Ljava/lang/String;
    //   443: astore_1
    //   444: aload_0
    //   445: getfield 265	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:mForegroundCall	Lcom/android/internal/telephony/imsphone/ImsPhoneCall;
    //   448: astore 14
    //   450: aload_2
    //   451: getfield 1861	com/android/internal/telephony/imsphone/ImsPhone$ImsDialArgs:intentExtras	Landroid/os/Bundle;
    //   454: astore 13
    //   456: aload 11
    //   458: aload 7
    //   460: aload_1
    //   461: aload_0
    //   462: aload 14
    //   464: iload 4
    //   466: aload 13
    //   468: invokespecial 1875	com/android/internal/telephony/imsphone/ImsPhoneConnection:<init>	(Lcom/android/internal/telephony/Phone;Ljava/lang/String;Lcom/android/internal/telephony/imsphone/ImsPhoneCallTracker;Lcom/android/internal/telephony/imsphone/ImsPhoneCall;ZLandroid/os/Bundle;)V
    //   471: aload_0
    //   472: aload 11
    //   474: putfield 531	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:mPendingMO	Lcom/android/internal/telephony/imsphone/ImsPhoneConnection;
    //   477: aload_0
    //   478: getfield 531	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:mPendingMO	Lcom/android/internal/telephony/imsphone/ImsPhoneConnection;
    //   481: iload 8
    //   483: invokevirtual 833	com/android/internal/telephony/imsphone/ImsPhoneConnection:setVideoState	(I)V
    //   486: aload_2
    //   487: getfield 1879	com/android/internal/telephony/imsphone/ImsPhone$ImsDialArgs:rttTextStream	Landroid/telecom/Connection$RttTextStream;
    //   490: ifnull +21 -> 511
    //   493: aload_0
    //   494: ldc_w 1881
    //   497: invokevirtual 701	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:log	(Ljava/lang/String;)V
    //   500: aload_0
    //   501: getfield 531	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:mPendingMO	Lcom/android/internal/telephony/imsphone/ImsPhoneConnection;
    //   504: aload_2
    //   505: getfield 1879	com/android/internal/telephony/imsphone/ImsPhone$ImsDialArgs:rttTextStream	Landroid/telecom/Connection$RttTextStream;
    //   508: invokevirtual 1885	com/android/internal/telephony/imsphone/ImsPhoneConnection:setCurrentRttTextStream	(Landroid/telecom/Connection$RttTextStream;)V
    //   511: aload 12
    //   513: monitorexit
    //   514: aload_0
    //   515: aload_0
    //   516: getfield 531	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:mPendingMO	Lcom/android/internal/telephony/imsphone/ImsPhoneConnection;
    //   519: invokespecial 637	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:addConnection	(Lcom/android/internal/telephony/imsphone/ImsPhoneConnection;)V
    //   522: iload 9
    //   524: ifne +104 -> 628
    //   527: iload 10
    //   529: ifne +99 -> 628
    //   532: iload_3
    //   533: ifeq +79 -> 612
    //   536: iload_3
    //   537: ifeq +11 -> 548
    //   540: iload 4
    //   542: ifeq +6 -> 548
    //   545: goto +67 -> 612
    //   548: aload_0
    //   549: invokevirtual 1435	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:getEcbmInterface	()Lcom/android/ims/ImsEcbm;
    //   552: invokevirtual 1886	com/android/ims/ImsEcbm:exitEmergencyCallbackMode	()V
    //   555: aload_0
    //   556: getfield 402	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:mPhone	Lcom/android/internal/telephony/imsphone/ImsPhone;
    //   559: aload_0
    //   560: bipush 14
    //   562: aconst_null
    //   563: invokevirtual 1889	com/android/internal/telephony/imsphone/ImsPhone:setOnEcbModeExitResponse	(Landroid/os/Handler;ILjava/lang/Object;)V
    //   566: aload_0
    //   567: iload 5
    //   569: putfield 1891	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:pendingCallClirMode	I
    //   572: aload_0
    //   573: iload 8
    //   575: putfield 1686	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:mPendingCallVideoState	I
    //   578: aload_0
    //   579: aload_2
    //   580: getfield 1861	com/android/internal/telephony/imsphone/ImsPhone$ImsDialArgs:intentExtras	Landroid/os/Bundle;
    //   583: putfield 1863	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:mPendingIntentExtras	Landroid/os/Bundle;
    //   586: aload_0
    //   587: iconst_1
    //   588: putfield 326	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:pendingCallInEcm	Z
    //   591: goto +37 -> 628
    //   594: astore_1
    //   595: aload_1
    //   596: invokevirtual 1894	com/android/ims/ImsException:printStackTrace	()V
    //   599: new 1672	com/android/internal/telephony/CallStateException
    //   602: astore_1
    //   603: aload_1
    //   604: ldc_w 1896
    //   607: invokespecial 1677	com/android/internal/telephony/CallStateException:<init>	(Ljava/lang/String;)V
    //   610: aload_1
    //   611: athrow
    //   612: aload_0
    //   613: aload_0
    //   614: getfield 531	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:mPendingMO	Lcom/android/internal/telephony/imsphone/ImsPhoneConnection;
    //   617: iload 5
    //   619: iload 8
    //   621: aload_2
    //   622: getfield 1861	com/android/internal/telephony/imsphone/ImsPhone$ImsDialArgs:intentExtras	Landroid/os/Bundle;
    //   625: invokespecial 1898	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:dialInternal	(Lcom/android/internal/telephony/imsphone/ImsPhoneConnection;IILandroid/os/Bundle;)V
    //   628: aload_0
    //   629: invokespecial 502	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:updatePhoneState	()V
    //   632: aload_0
    //   633: getfield 402	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:mPhone	Lcom/android/internal/telephony/imsphone/ImsPhone;
    //   636: invokevirtual 1255	com/android/internal/telephony/imsphone/ImsPhone:notifyPreciseCallStateChanged	()V
    //   639: aload_0
    //   640: getfield 531	com/android/internal/telephony/imsphone/ImsPhoneCallTracker:mPendingMO	Lcom/android/internal/telephony/imsphone/ImsPhoneConnection;
    //   643: astore_1
    //   644: aload_0
    //   645: monitorexit
    //   646: aload_1
    //   647: areturn
    //   648: astore_1
    //   649: goto +4 -> 653
    //   652: astore_1
    //   653: aload 12
    //   655: monitorexit
    //   656: aload_1
    //   657: athrow
    //   658: astore_1
    //   659: goto -6 -> 653
    //   662: new 1672	com/android/internal/telephony/CallStateException
    //   665: astore_1
    //   666: aload_1
    //   667: ldc_w 1869
    //   670: invokespecial 1677	com/android/internal/telephony/CallStateException:<init>	(Ljava/lang/String;)V
    //   673: aload_1
    //   674: athrow
    //   675: new 1672	com/android/internal/telephony/CallStateException
    //   678: astore_1
    //   679: aload_1
    //   680: ldc_w 1896
    //   683: invokespecial 1677	com/android/internal/telephony/CallStateException:<init>	(Ljava/lang/String;)V
    //   686: aload_1
    //   687: athrow
    //   688: ldc 66
    //   690: ldc_w 1900
    //   693: invokestatic 1260	android/telephony/Rlog:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   696: pop
    //   697: new 1672	com/android/internal/telephony/CallStateException
    //   700: astore_1
    //   701: aload_1
    //   702: ldc_w 1902
    //   705: invokespecial 1677	com/android/internal/telephony/CallStateException:<init>	(Ljava/lang/String;)V
    //   708: aload_1
    //   709: athrow
    //   710: astore_1
    //   711: aload_0
    //   712: monitorexit
    //   713: aload_1
    //   714: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	715	0	this	ImsPhoneCallTracker
    //   0	715	1	paramString	String
    //   0	715	2	paramImsDialArgs	ImsPhone.ImsDialArgs
    //   6	531	3	bool1	boolean
    //   17	524	4	bool2	boolean
    //   33	585	5	i	int
    //   39	338	6	j	int
    //   44	415	7	localObject1	Object
    //   163	457	8	k	int
    //   207	316	9	m	int
    //   211	317	10	n	int
    //   320	153	11	localObject2	Object
    //   332	322	12	localObject3	Object
    //   357	110	13	localObject4	Object
    //   448	15	14	localImsPhoneCall	ImsPhoneCall
    // Exception table:
    //   from	to	target	type
    //   350	376	423	finally
    //   388	396	423	finally
    //   410	423	423	finally
    //   548	555	594	com/android/ims/ImsException
    //   456	511	648	finally
    //   511	514	648	finally
    //   427	456	652	finally
    //   653	656	658	finally
    //   2	77	710	finally
    //   85	120	710	finally
    //   120	147	710	finally
    //   156	161	710	finally
    //   174	182	710	finally
    //   186	200	710	finally
    //   213	239	710	finally
    //   242	265	710	finally
    //   271	285	710	finally
    //   288	292	710	finally
    //   299	312	710	finally
    //   312	337	710	finally
    //   514	522	710	finally
    //   548	555	710	finally
    //   555	591	710	finally
    //   595	612	710	finally
    //   612	628	710	finally
    //   628	644	710	finally
    //   656	658	710	finally
    //   662	675	710	finally
    //   675	688	710	finally
    //   688	710	710	finally
  }
  
  public void dispose()
  {
    log("dispose");
    mRingingCall.dispose();
    mBackgroundCall.dispose();
    mForegroundCall.dispose();
    mHandoverCall.dispose();
    clearDisconnected();
    if (mUtInterface != null) {
      mUtInterface.unregisterForSuppServiceIndication(this);
    }
    mPhone.getContext().unregisterReceiver(mReceiver);
    mPhone.getDefaultPhone().unregisterForDataEnabledChanged(this);
    mImsManagerConnector.disconnect();
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter.println("ImsPhoneCallTracker extends:");
    super.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mVoiceCallEndedRegistrants=");
    localStringBuilder.append(mVoiceCallEndedRegistrants);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mVoiceCallStartedRegistrants=");
    localStringBuilder.append(mVoiceCallStartedRegistrants);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mRingingCall=");
    localStringBuilder.append(mRingingCall);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mForegroundCall=");
    localStringBuilder.append(mForegroundCall);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mBackgroundCall=");
    localStringBuilder.append(mBackgroundCall);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mHandoverCall=");
    localStringBuilder.append(mHandoverCall);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mPendingMO=");
    localStringBuilder.append(mPendingMO);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mPhone=");
    localStringBuilder.append(mPhone);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mDesiredMute=");
    localStringBuilder.append(mDesiredMute);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mState=");
    localStringBuilder.append(mState);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mMmTelCapabilities=");
    localStringBuilder.append(mMmTelCapabilities);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mDefaultDialerUid=");
    localStringBuilder.append(mDefaultDialerUid.get());
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mVtDataUsageSnapshot=");
    localStringBuilder.append(mVtDataUsageSnapshot);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mVtDataUsageUidSnapshot=");
    localStringBuilder.append(mVtDataUsageUidSnapshot);
    paramPrintWriter.println(localStringBuilder.toString());
    paramPrintWriter.flush();
    paramPrintWriter.println("++++++++++++++++++++++++++++++++");
    try
    {
      if (mImsManager != null) {
        mImsManager.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      }
    }
    catch (Exception paramFileDescriptor)
    {
      paramFileDescriptor.printStackTrace();
    }
    if ((mConnections != null) && (mConnections.size() > 0))
    {
      paramPrintWriter.println("mConnections:");
      for (int i = 0; i < mConnections.size(); i++)
      {
        paramFileDescriptor = new StringBuilder();
        paramFileDescriptor.append("  [");
        paramFileDescriptor.append(i);
        paramFileDescriptor.append("]: ");
        paramFileDescriptor.append(mConnections.get(i));
        paramPrintWriter.println(paramFileDescriptor.toString());
      }
    }
  }
  
  public void explicitCallTransfer() {}
  
  protected void finalize()
  {
    log("ImsPhoneCallTracker finalized");
  }
  
  public int getClirMode()
  {
    if ((mSharedPreferenceProxy != null) && (mPhone.getDefaultPhone() != null))
    {
      SharedPreferences localSharedPreferences = mSharedPreferenceProxy.getDefaultSharedPreferences(mPhone.getContext());
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("clir_key");
      localStringBuilder.append(mPhone.getDefaultPhone().getPhoneId());
      return localSharedPreferences.getInt(localStringBuilder.toString(), 0);
    }
    loge("dial; could not get default CLIR mode.");
    return 0;
  }
  
  @VisibleForTesting
  public int getDisconnectCauseFromReasonInfo(ImsReasonInfo paramImsReasonInfo, Call.State paramState)
  {
    int i = maybeRemapReasonCode(paramImsReasonInfo);
    paramImsReasonInfo = paramImsReasonInfo.getExtraMessage();
    switch (i)
    {
    default: 
      return 36;
    case 1515: 
      return 25;
    case 1514: 
      return 117;
    case 1512: 
      return 60;
    case 1407: 
      return 59;
    case 1406: 
      return 54;
    case 1405: 
      return 55;
    case 1403: 
      return 53;
    case 1016: 
      return 51;
    case 1014: 
      return 52;
    case 510: 
      return 2;
    case 501: 
      return 3;
    case 364: 
      return 64;
    case 363: 
      return 63;
    case 338: 
      return 4;
    case 337: 
    case 341: 
      return 8;
    case 333: 
    case 352: 
    case 354: 
      return 9;
    case 332: 
      if ((paramImsReasonInfo != null) && (paramImsReasonInfo.equalsIgnoreCase("simultaneous call limit has already been reached")) && (Build.FEATURES.VZW)) {
        return 53;
      }
      return 12;
    case 321: 
    case 331: 
    case 340: 
    case 361: 
    case 362: 
      return 12;
    case 251: 
      return 68;
    case 250: 
      return 67;
    case 249: 
      return 70;
    case 248: 
      return 69;
    case 247: 
      return 66;
    case 246: 
      return 48;
    case 245: 
      return 47;
    case 244: 
      return 46;
    case 243: 
      return 58;
    case 241: 
      return 21;
    case 240: 
      return 20;
    case 201: 
    case 202: 
    case 203: 
    case 335: 
      return 13;
    case 143: 
    case 1404: 
      return 16;
    case 112: 
    case 505: 
      if (paramState == Call.State.DIALING) {
        return 62;
      }
      return 61;
    case 111: 
      return 17;
    case 108: 
      return 45;
    }
    return 18;
  }
  
  ImsEcbm getEcbmInterface()
    throws ImsException
  {
    if (mImsManager != null) {
      return mImsManager.getEcbmInterface();
    }
    throw getImsManagerIsNullException();
  }
  
  public int getImsRegistrationTech()
  {
    if (mImsManager != null) {
      return mImsManager.getRegistrationTech();
    }
    return -1;
  }
  
  ImsMultiEndpoint getMultiEndpointInterface()
    throws ImsException
  {
    if (mImsManager != null) {
      try
      {
        ImsMultiEndpoint localImsMultiEndpoint = mImsManager.getMultiEndpointInterface();
        return localImsMultiEndpoint;
      }
      catch (ImsException localImsException)
      {
        if (localImsException.getCode() == 902) {
          return null;
        }
        throw localImsException;
      }
    }
    throw getImsManagerIsNullException();
  }
  
  public boolean getMute()
  {
    return mDesiredMute;
  }
  
  public PhoneConstants.State getState()
  {
    return mState;
  }
  
  @VisibleForTesting
  public boolean getSwitchingFgAndBgCallsValue()
  {
    return mSwitchingFgAndBgCalls;
  }
  
  public ImsUtInterface getUtInterface()
    throws ImsException
  {
    if (mImsManager != null) {
      return mImsManager.getSupplementaryServiceConfiguration();
    }
    throw getImsManagerIsNullException();
  }
  
  public NetworkStats getVtDataUsage(boolean paramBoolean)
  {
    Object localObject;
    if (mState != PhoneConstants.State.IDLE)
    {
      localObject = mConnections.iterator();
      while (((Iterator)localObject).hasNext())
      {
        Connection.VideoProvider localVideoProvider = ((ImsPhoneConnection)((Iterator)localObject).next()).getVideoProvider();
        if (localVideoProvider != null) {
          localVideoProvider.onRequestConnectionDataUsage();
        }
      }
    }
    if (paramBoolean) {
      localObject = mVtDataUsageUidSnapshot;
    } else {
      localObject = mVtDataUsageSnapshot;
    }
    return localObject;
  }
  
  public void handleMessage(Message paramMessage)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("handleMessage what=");
    ((StringBuilder)localObject).append(what);
    log(((StringBuilder)localObject).toString());
    switch (what)
    {
    case 15: 
    case 16: 
    case 17: 
    case 24: 
    default: 
      break;
    case 27: 
      localObject = (AsyncResult)obj;
      paramMessage = new ImsPhoneMmiCode(mPhone);
      try
      {
        paramMessage.setIsSsInfo(true);
        paramMessage.processImsSsData((AsyncResult)localObject);
      }
      catch (ImsException paramMessage)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Exception in parsing SS Data: ");
        ((StringBuilder)localObject).append(paramMessage);
        Rlog.e("ImsPhoneCallTracker", ((StringBuilder)localObject).toString());
      }
    case 26: 
      localObject = (SomeArgs)obj;
    case 25: 
      try
      {
        handleFeatureCapabilityChanged((ImsFeature.Capabilities)arg1);
        ((SomeArgs)localObject).recycle();
      }
      finally
      {
        ((SomeArgs)localObject).recycle();
      }
      paramMessage = (ImsCall)obj;
      if (paramMessage != mForegroundCall.getImsCall())
      {
        Rlog.i("ImsPhoneCallTracker", "handoverCheck: no longer FG; check skipped.");
        unregisterForConnectivityChanges();
        return;
      }
      if (!paramMessage.isWifiCall())
      {
        localObject = findConnection(paramMessage);
        if (localObject != null)
        {
          Rlog.i("ImsPhoneCallTracker", "handoverCheck: handover failed.");
          ((ImsPhoneConnection)localObject).onHandoverToWifiFailed();
        }
        if ((paramMessage.isVideoCall()) && (((ImsPhoneConnection)localObject).getDisconnectCause() == 0)) {
          registerForConnectivityChanges();
        }
      }
      break;
    case 23: 
      paramMessage = (AsyncResult)obj;
      if ((result instanceof Pair))
      {
        paramMessage = (Pair)result;
        onDataEnabledChanged(((Boolean)first).booleanValue(), ((Integer)second).intValue());
      }
      break;
    case 22: 
      localObject = (AsyncResult)obj;
      paramMessage = (ImsCall)userObj;
      Long localLong = Long.valueOf(((Long)result).longValue());
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("VT data usage update. usage = ");
      ((StringBuilder)localObject).append(localLong);
      ((StringBuilder)localObject).append(", imsCall = ");
      ((StringBuilder)localObject).append(paramMessage);
      log(((StringBuilder)localObject).toString());
      if (localLong.longValue() > 0L) {
        updateVtDataUsage(paramMessage, localLong.longValue());
      }
      break;
    case 21: 
      if (mPendingMO != null) {
        try
        {
          getEcbmInterface().exitEmergencyCallbackMode();
          mPhone.setOnEcbModeExitResponse(this, 14, null);
          pendingCallClirMode = mClirMode;
          pendingCallInEcm = true;
        }
        catch (ImsException paramMessage)
        {
          paramMessage.printStackTrace();
          mPendingMO.setDisconnectCause(36);
          sendEmptyMessageDelayed(18, 500L);
        }
      }
      break;
    case 20: 
      dialInternal(mPendingMO, mClirMode, mPendingCallVideoState, mPendingIntentExtras);
      mPendingIntentExtras = null;
      break;
    case 19: 
      try
      {
        resumeWaitingOrHolding();
      }
      catch (CallStateException paramMessage)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("handleMessage EVENT_RESUME_BACKGROUND exception=");
        ((StringBuilder)localObject).append(paramMessage);
        loge(((StringBuilder)localObject).toString());
      }
    case 18: 
      if (mPendingMO != null)
      {
        mPendingMO.onDisconnect();
        removeConnection(mPendingMO);
        mPendingMO = null;
      }
      mPendingIntentExtras = null;
      updatePhoneState();
      mPhone.notifyPreciseCallStateChanged();
      break;
    case 14: 
      if (pendingCallInEcm)
      {
        dialInternal(mPendingMO, pendingCallClirMode, mPendingCallVideoState, mPendingIntentExtras);
        mPendingIntentExtras = null;
        pendingCallInEcm = false;
      }
      mPhone.unsetOnEcbModeExitResponse(this);
    }
  }
  
  protected void handlePollCalls(AsyncResult paramAsyncResult) {}
  
  public void hangup(ImsPhoneCall paramImsPhoneCall)
    throws CallStateException
  {
    log("hangup call");
    if (paramImsPhoneCall.getConnections().size() != 0)
    {
      Object localObject = paramImsPhoneCall.getImsCall();
      int i = 0;
      if (paramImsPhoneCall == mRingingCall)
      {
        log("(ringing) hangup incoming");
        i = 1;
      }
      else if (paramImsPhoneCall == mForegroundCall)
      {
        if (paramImsPhoneCall.isDialingOrAlerting()) {
          log("(foregnd) hangup dialing or alerting...");
        } else {
          log("(foregnd) hangup foreground");
        }
      }
      else
      {
        if (paramImsPhoneCall != mBackgroundCall) {
          break label245;
        }
        log("(backgnd) hangup waiting or background");
      }
      paramImsPhoneCall.onHangupLocal();
      if ((localObject == null) || (i != 0)) {}
      try
      {
        ((ImsCall)localObject).reject(504);
        mMetrics.writeOnImsCommand(mPhone.getPhoneId(), ((ImsCall)localObject).getSession(), 3);
        break label224;
        ((ImsCall)localObject).terminate(501);
        mMetrics.writeOnImsCommand(mPhone.getPhoneId(), ((ImsCall)localObject).getSession(), 4);
        break label224;
        if ((mPendingMO != null) && (paramImsPhoneCall == mForegroundCall))
        {
          mPendingMO.update(null, Call.State.DISCONNECTED);
          mPendingMO.onDisconnect();
          removeConnection(mPendingMO);
          mPendingMO = null;
          updatePhoneState();
          removeMessages(20);
        }
        label224:
        mPhone.notifyPreciseCallStateChanged();
        return;
      }
      catch (ImsException paramImsPhoneCall)
      {
        throw new CallStateException(paramImsPhoneCall.getMessage());
      }
      label245:
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("ImsPhoneCall ");
      ((StringBuilder)localObject).append(paramImsPhoneCall);
      ((StringBuilder)localObject).append("does not belong to ImsPhoneCallTracker ");
      ((StringBuilder)localObject).append(this);
      throw new CallStateException(((StringBuilder)localObject).toString());
    }
    throw new CallStateException("no connections");
  }
  
  public void hangup(ImsPhoneConnection paramImsPhoneConnection)
    throws CallStateException
  {
    log("hangup connection");
    if (paramImsPhoneConnection.getOwner() == this)
    {
      hangup(paramImsPhoneConnection.getCall());
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ImsPhoneConnection ");
    localStringBuilder.append(paramImsPhoneConnection);
    localStringBuilder.append("does not belong to ImsPhoneCallTracker ");
    localStringBuilder.append(this);
    throw new CallStateException(localStringBuilder.toString());
  }
  
  public boolean isCarrierDowngradeOfVtCallSupported()
  {
    return mSupportDowngradeVtToAudio;
  }
  
  boolean isImsServiceReady()
  {
    if (mImsManager == null) {
      return false;
    }
    return mImsManager.isServiceReady();
  }
  
  public boolean isInEmergencyCall()
  {
    return mIsInEmergencyCall;
  }
  
  public boolean isUtEnabled()
  {
    return mMmTelCapabilities.isCapable(4);
  }
  
  public boolean isViLteDataMetered()
  {
    return mIsViLteDataMetered;
  }
  
  public boolean isVideoCallEnabled()
  {
    return mMmTelCapabilities.isCapable(2);
  }
  
  public boolean isVolteEnabled()
  {
    int i = getImsRegistrationTech();
    boolean bool1 = false;
    if (i == 0) {
      i = 1;
    } else {
      i = 0;
    }
    boolean bool2 = bool1;
    if (i != 0)
    {
      bool2 = bool1;
      if (mMmTelCapabilities.isCapable(1)) {
        bool2 = true;
      }
    }
    return bool2;
  }
  
  public boolean isVowifiEnabled()
  {
    int i = getImsRegistrationTech();
    boolean bool1 = false;
    if (i == 1) {
      i = 1;
    } else {
      i = 0;
    }
    boolean bool2 = bool1;
    if (i != 0)
    {
      bool2 = bool1;
      if (mMmTelCapabilities.isCapable(1)) {
        bool2 = true;
      }
    }
    return bool2;
  }
  
  protected void log(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(mPhone.getPhoneId());
    localStringBuilder.append("] ");
    localStringBuilder.append(paramString);
    Rlog.d("ImsPhoneCallTracker", localStringBuilder.toString());
  }
  
  void logState()
  {
    if (!VERBOSE_STATE_LOGGING) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Current IMS PhoneCall State:\n");
    localStringBuilder.append(" Foreground: ");
    localStringBuilder.append(mForegroundCall);
    localStringBuilder.append("\n");
    localStringBuilder.append(" Background: ");
    localStringBuilder.append(mBackgroundCall);
    localStringBuilder.append("\n");
    localStringBuilder.append(" Ringing: ");
    localStringBuilder.append(mRingingCall);
    localStringBuilder.append("\n");
    localStringBuilder.append(" Handover: ");
    localStringBuilder.append(mHandoverCall);
    localStringBuilder.append("\n");
    Rlog.v("ImsPhoneCallTracker", localStringBuilder.toString());
  }
  
  protected void loge(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(mPhone.getPhoneId());
    localStringBuilder.append("] ");
    localStringBuilder.append(paramString);
    Rlog.e("ImsPhoneCallTracker", localStringBuilder.toString());
  }
  
  @VisibleForTesting
  public int maybeRemapReasonCode(ImsReasonInfo paramImsReasonInfo)
  {
    int i = paramImsReasonInfo.getCode();
    Object localObject = new Pair(Integer.valueOf(i), paramImsReasonInfo.getExtraMessage());
    Pair localPair = new Pair(null, paramImsReasonInfo.getExtraMessage());
    if (mImsReasonCodeMap.containsKey(localObject))
    {
      i = ((Integer)mImsReasonCodeMap.get(localObject)).intValue();
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("maybeRemapReasonCode : fromCode = ");
      ((StringBuilder)localObject).append(paramImsReasonInfo.getCode());
      ((StringBuilder)localObject).append(" ; message = ");
      ((StringBuilder)localObject).append(paramImsReasonInfo.getExtraMessage());
      ((StringBuilder)localObject).append(" ; toCode = ");
      ((StringBuilder)localObject).append(i);
      log(((StringBuilder)localObject).toString());
      return i;
    }
    if (mImsReasonCodeMap.containsKey(localPair))
    {
      i = ((Integer)mImsReasonCodeMap.get(localPair)).intValue();
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("maybeRemapReasonCode : fromCode(wildcard) = ");
      ((StringBuilder)localObject).append(paramImsReasonInfo.getCode());
      ((StringBuilder)localObject).append(" ; message = ");
      ((StringBuilder)localObject).append(paramImsReasonInfo.getExtraMessage());
      ((StringBuilder)localObject).append(" ; toCode = ");
      ((StringBuilder)localObject).append(i);
      log(((StringBuilder)localObject).toString());
      return i;
    }
    return i;
  }
  
  void notifySrvccState(Call.SrvccState paramSrvccState)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("notifySrvccState state=");
    localStringBuilder.append(paramSrvccState);
    log(localStringBuilder.toString());
    mSrvccState = paramSrvccState;
    if (mSrvccState == Call.SrvccState.COMPLETED)
    {
      transferHandoverConnections(mForegroundCall);
      transferHandoverConnections(mBackgroundCall);
      transferHandoverConnections(mRingingCall);
      mState = PhoneConstants.State.IDLE;
    }
  }
  
  @VisibleForTesting
  public void onCallHoldReceived(ImsCall paramImsCall)
  {
    log("onCallHoldReceived");
    Object localObject = findConnection(paramImsCall);
    if (localObject != null)
    {
      if ((!mOnHoldToneStarted) && ((ImsPhoneCall.isLocalTone(paramImsCall)) || (mAlwaysPlayRemoteHoldTone)) && (((ImsPhoneConnection)localObject).getState() == Call.State.ACTIVE))
      {
        mPhone.startOnHoldTone((Connection)localObject);
        mOnHoldToneStarted = true;
        mOnHoldToneId = System.identityHashCode(localObject);
      }
      ((ImsPhoneConnection)localObject).onConnectionEvent("android.telecom.event.CALL_REMOTELY_HELD", null);
      if ((mPhone.getContext().getResources().getBoolean(17957070)) && (mSupportPauseVideo) && (VideoProfile.isVideo(((ImsPhoneConnection)localObject).getVideoState()))) {
        ((ImsPhoneConnection)localObject).changeToPausedState();
      }
    }
    localObject = new SuppServiceNotification();
    notificationType = 1;
    code = 2;
    mPhone.notifySuppSvcNotification((SuppServiceNotification)localObject);
    mMetrics.writeOnImsCallHoldReceived(mPhone.getPhoneId(), paramImsCall.getCallSession());
  }
  
  public void pullExternalCall(String paramString, int paramInt1, int paramInt2)
  {
    Object localObject = new Bundle();
    ((Bundle)localObject).putBoolean("CallPull", true);
    ((Bundle)localObject).putInt("android.telephony.ImsExternalCallTracker.extra.EXTERNAL_CALL_ID", paramInt2);
    try
    {
      paramString = dial(paramString, paramInt1, (Bundle)localObject);
      mPhone.notifyUnknownConnection(paramString);
    }
    catch (CallStateException paramString)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("pullExternalCall failed - ");
      ((StringBuilder)localObject).append(paramString);
      loge(((StringBuilder)localObject).toString());
    }
  }
  
  public void registerForVoiceCallEnded(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mVoiceCallEndedRegistrants.add(paramHandler);
  }
  
  public void registerForVoiceCallStarted(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mVoiceCallStartedRegistrants.add(paramHandler);
  }
  
  public void registerPhoneStateListener(PhoneStateListener paramPhoneStateListener)
  {
    mPhoneStateListeners.add(paramPhoneStateListener);
  }
  
  public void rejectCall()
    throws CallStateException
  {
    log("rejectCall");
    if (mRingingCall.getState().isRinging())
    {
      hangup(mRingingCall);
      return;
    }
    throw new CallStateException("phone not ringing");
  }
  
  void resumeWaitingOrHolding()
    throws CallStateException
  {
    log("resumeWaitingOrHolding");
    try
    {
      ImsCall localImsCall;
      if (mForegroundCall.getState().isAlive())
      {
        localImsCall = mForegroundCall.getImsCall();
        if (localImsCall != null)
        {
          localImsCall.resume();
          mMetrics.writeOnImsCommand(mPhone.getPhoneId(), localImsCall.getSession(), 6);
        }
      }
      else if (mRingingCall.getState() == Call.State.WAITING)
      {
        localImsCall = mRingingCall.getImsCall();
        if (localImsCall != null)
        {
          ImsStreamMediaProfile localImsStreamMediaProfile = new android/telephony/ims/ImsStreamMediaProfile;
          localImsStreamMediaProfile.<init>();
          localImsStreamMediaProfile = addRttAttributeIfRequired(localImsCall, localImsStreamMediaProfile);
          localImsCall.accept(ImsCallProfile.getCallTypeFromVideoState(mPendingCallVideoState), localImsStreamMediaProfile);
          mMetrics.writeOnImsCommand(mPhone.getPhoneId(), localImsCall.getSession(), 2);
        }
      }
      else
      {
        localImsCall = mBackgroundCall.getImsCall();
        if (localImsCall != null)
        {
          localImsCall.resume();
          mMetrics.writeOnImsCommand(mPhone.getPhoneId(), localImsCall.getSession(), 6);
        }
      }
      return;
    }
    catch (ImsException localImsException)
    {
      throw new CallStateException(localImsException.getMessage());
    }
  }
  
  public void sendDtmf(char paramChar, Message paramMessage)
  {
    log("sendDtmf");
    ImsCall localImsCall = mForegroundCall.getImsCall();
    if (localImsCall != null) {
      localImsCall.sendDtmf(paramChar, paramMessage);
    }
  }
  
  public void sendUSSD(String paramString, Message paramMessage)
  {
    log("sendUSSD");
    try
    {
      if (mUssdSession != null)
      {
        mUssdSession.sendUssd(paramString);
        AsyncResult.forMessage(paramMessage, null, null);
        paramMessage.sendToTarget();
        return;
      }
      if (mImsManager == null)
      {
        mPhone.sendErrorResponse(paramMessage, getImsManagerIsNullException());
        return;
      }
      ImsCallProfile localImsCallProfile = mImsManager.createCallProfile(1, 2);
      localImsCallProfile.setCallExtraInt("dialstring", 2);
      localObject = mImsManager;
      ImsCall.Listener localListener = mImsUssdListener;
      mUssdSession = ((ImsManager)localObject).makeCall(localImsCallProfile, new String[] { paramString }, localListener);
    }
    catch (ImsException paramString)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("sendUSSD : ");
      ((StringBuilder)localObject).append(paramString);
      loge(((StringBuilder)localObject).toString());
      mPhone.sendErrorResponse(paramMessage, paramString);
      retryGetImsService();
    }
  }
  
  @VisibleForTesting
  public void setAlwaysPlayRemoteHoldTone(boolean paramBoolean)
  {
    mAlwaysPlayRemoteHoldTone = paramBoolean;
  }
  
  @VisibleForTesting
  public void setDataEnabled(boolean paramBoolean)
  {
    mIsDataEnabled = paramBoolean;
  }
  
  public void setMute(boolean paramBoolean)
  {
    mDesiredMute = paramBoolean;
    mForegroundCall.setMute(paramBoolean);
  }
  
  @VisibleForTesting
  public void setPhoneNumberUtilsProxy(PhoneNumberUtilsProxy paramPhoneNumberUtilsProxy)
  {
    mPhoneNumberUtilsProxy = paramPhoneNumberUtilsProxy;
  }
  
  @VisibleForTesting
  public void setRetryTimeout(ImsManager.Connector.RetryTimeout paramRetryTimeout)
  {
    mImsManagerConnector.mRetryTimeout = paramRetryTimeout;
  }
  
  @VisibleForTesting
  public void setSharedPreferenceProxy(SharedPreferenceProxy paramSharedPreferenceProxy)
  {
    mSharedPreferenceProxy = paramSharedPreferenceProxy;
  }
  
  @VisibleForTesting
  public void setSwitchingFgAndBgCallsValue(boolean paramBoolean)
  {
    mSwitchingFgAndBgCalls = paramBoolean;
  }
  
  public void setTtyMode(int paramInt)
  {
    if (mImsManager == null)
    {
      Log.w("ImsPhoneCallTracker", "ImsManager is null when setting TTY mode");
      return;
    }
    try
    {
      mImsManager.setTtyMode(paramInt);
    }
    catch (ImsException localImsException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("setTtyMode : ");
      localStringBuilder.append(localImsException);
      loge(localStringBuilder.toString());
      retryGetImsService();
    }
  }
  
  public void setUiTTYMode(int paramInt, Message paramMessage)
  {
    if (mImsManager == null)
    {
      mPhone.sendErrorResponse(paramMessage, getImsManagerIsNullException());
      return;
    }
    try
    {
      mImsManager.setUiTTYMode(mPhone.getContext(), paramInt, paramMessage);
    }
    catch (ImsException localImsException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("setUITTYMode : ");
      localStringBuilder.append(localImsException);
      loge(localStringBuilder.toString());
      mPhone.sendErrorResponse(paramMessage, localImsException);
      retryGetImsService();
    }
  }
  
  public void startDtmf(char paramChar)
  {
    log("startDtmf");
    ImsCall localImsCall = mForegroundCall.getImsCall();
    if (localImsCall != null) {
      localImsCall.startDtmf(paramChar);
    } else {
      loge("startDtmf : no foreground call");
    }
  }
  
  public void stopDtmf()
  {
    log("stopDtmf");
    ImsCall localImsCall = mForegroundCall.getImsCall();
    if (localImsCall != null) {
      localImsCall.stopDtmf();
    } else {
      loge("stopDtmf : no foreground call");
    }
  }
  
  public void switchWaitingOrHoldingAndActive()
    throws CallStateException
  {
    log("switchWaitingOrHoldingAndActive");
    if (mRingingCall.getState() != Call.State.INCOMING)
    {
      if (mForegroundCall.getState() == Call.State.ACTIVE)
      {
        ImsCall localImsCall = mForegroundCall.getImsCall();
        if (localImsCall != null)
        {
          int i;
          if ((!mBackgroundCall.getState().isAlive()) && (mRingingCall != null) && (mRingingCall.getState() == Call.State.WAITING)) {
            i = 1;
          } else {
            i = 0;
          }
          mSwitchingFgAndBgCalls = true;
          if (i != 0) {
            mCallExpectedToResume = mRingingCall.getImsCall();
          } else {
            mCallExpectedToResume = mBackgroundCall.getImsCall();
          }
          mForegroundCall.switchWith(mBackgroundCall);
          try
          {
            localImsCall.hold();
            mMetrics.writeOnImsCommand(mPhone.getPhoneId(), localImsCall.getSession(), 5);
            if (mCallExpectedToResume == null)
            {
              log("mCallExpectedToResume is null");
              mSwitchingFgAndBgCalls = false;
            }
          }
          catch (ImsException localImsException)
          {
            mForegroundCall.switchWith(mBackgroundCall);
            throw new CallStateException(localImsException.getMessage());
          }
        }
        else
        {
          throw new CallStateException("no ims call");
        }
      }
      else if (mBackgroundCall.getState() == Call.State.HOLDING)
      {
        resumeWaitingOrHolding();
      }
      return;
    }
    throw new CallStateException("cannot be in the incoming state");
  }
  
  public void unregisterForVoiceCallEnded(Handler paramHandler)
  {
    mVoiceCallEndedRegistrants.remove(paramHandler);
  }
  
  public void unregisterForVoiceCallStarted(Handler paramHandler)
  {
    mVoiceCallStartedRegistrants.remove(paramHandler);
  }
  
  public void unregisterPhoneStateListener(PhoneStateListener paramPhoneStateListener)
  {
    mPhoneStateListeners.remove(paramPhoneStateListener);
  }
  
  @VisibleForTesting
  public void updateCarrierConfigCache(PersistableBundle paramPersistableBundle)
  {
    mAllowEmergencyVideoCalls = paramPersistableBundle.getBoolean("allow_emergency_video_calls_bool");
    mTreatDowngradedVideoCallsAsVideoCalls = paramPersistableBundle.getBoolean("treat_downgraded_video_calls_as_video_calls_bool");
    mDropVideoCallWhenAnsweringAudioCall = paramPersistableBundle.getBoolean("drop_video_call_when_answering_audio_call_bool");
    mAllowAddCallDuringVideoCall = paramPersistableBundle.getBoolean("allow_add_call_during_video_call");
    mAllowHoldingVideoCall = paramPersistableBundle.getBoolean("allow_holding_video_call");
    mNotifyVtHandoverToWifiFail = paramPersistableBundle.getBoolean("notify_vt_handover_to_wifi_failure_bool");
    mSupportDowngradeVtToAudio = paramPersistableBundle.getBoolean("support_downgrade_vt_to_audio_bool");
    mNotifyHandoverVideoFromWifiToLTE = paramPersistableBundle.getBoolean("notify_handover_video_from_wifi_to_lte_bool");
    mNotifyHandoverVideoFromLTEToWifi = paramPersistableBundle.getBoolean("notify_handover_video_from_lte_to_wifi_bool");
    mIgnoreDataEnabledChangedForVideoCalls = paramPersistableBundle.getBoolean("ignore_data_enabled_changed_for_video_calls");
    mIsViLteDataMetered = paramPersistableBundle.getBoolean("vilte_data_is_metered_bool");
    mSupportPauseVideo = paramPersistableBundle.getBoolean("support_pause_ims_video_calls_bool");
    mAlwaysPlayRemoteHoldTone = paramPersistableBundle.getBoolean("always_play_remote_hold_tone_bool");
    mIgnoreResetUtCapability = paramPersistableBundle.getBoolean("ignore_reset_ut_capability_bool");
    String[] arrayOfString = paramPersistableBundle.getStringArray("ims_reasoninfo_mapping_string_array");
    int i;
    int j;
    if ((arrayOfString != null) && (arrayOfString.length > 0))
    {
      i = arrayOfString.length;
      j = 0;
    }
    while (j < i)
    {
      String str1 = arrayOfString[j];
      Object localObject = str1.split(Pattern.quote("|"));
      if (localObject.length == 3) {
        try
        {
          if (localObject[0].equals("*")) {
            paramPersistableBundle = null;
          } else {
            paramPersistableBundle = Integer.valueOf(Integer.parseInt(localObject[0]));
          }
          String str2 = localObject[1];
          int k = Integer.parseInt(localObject[2]);
          addReasonCodeRemapping(paramPersistableBundle, str2, Integer.valueOf(k));
          localObject = new java/lang/StringBuilder;
          ((StringBuilder)localObject).<init>();
          ((StringBuilder)localObject).append("Loaded ImsReasonInfo mapping : fromCode = ");
          ((StringBuilder)localObject).append(paramPersistableBundle);
          if (((StringBuilder)localObject).toString() == null)
          {
            paramPersistableBundle = "any";
          }
          else
          {
            localObject = new java/lang/StringBuilder;
            ((StringBuilder)localObject).<init>();
            ((StringBuilder)localObject).append(paramPersistableBundle);
            ((StringBuilder)localObject).append(" ; message = ");
            ((StringBuilder)localObject).append(str2);
            ((StringBuilder)localObject).append(" ; toCode = ");
            ((StringBuilder)localObject).append(k);
            paramPersistableBundle = ((StringBuilder)localObject).toString();
          }
          log(paramPersistableBundle);
        }
        catch (NumberFormatException paramPersistableBundle)
        {
          paramPersistableBundle = new StringBuilder();
          paramPersistableBundle.append("Invalid ImsReasonInfo mapping found: ");
          paramPersistableBundle.append(str1);
          loge(paramPersistableBundle.toString());
        }
      }
      j++;
      continue;
      log("No carrier ImsReasonInfo mappings defined.");
    }
  }
  
  private class MmTelFeatureListener
    extends MmTelFeature.Listener
  {
    private MmTelFeatureListener() {}
    
    public void onIncomingCall(IImsCallSession paramIImsCallSession, Bundle paramBundle)
    {
      log("onReceive : incoming call intent");
      if (mImsManager == null) {
        return;
      }
      try
      {
        if (paramBundle.getBoolean("android:ussd", false))
        {
          log("onReceive : USSD");
          ImsPhoneCallTracker.access$202(ImsPhoneCallTracker.this, mImsManager.takeCall(paramIImsCallSession, paramBundle, mImsUssdListener));
          if (mUssdSession != null) {
            mUssdSession.accept(2);
          }
          return;
        }
        boolean bool = paramBundle.getBoolean("android:isUnknown", false);
        Object localObject1 = ImsPhoneCallTracker.this;
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("onReceive : isUnknown = ");
        ((StringBuilder)localObject2).append(bool);
        ((StringBuilder)localObject2).append(" fg = ");
        ((StringBuilder)localObject2).append(mForegroundCall.getState());
        ((StringBuilder)localObject2).append(" bg = ");
        ((StringBuilder)localObject2).append(mBackgroundCall.getState());
        ((ImsPhoneCallTracker)localObject1).log(((StringBuilder)localObject2).toString());
        localObject2 = mImsManager.takeCall(paramIImsCallSession, paramBundle, mImsCallListener);
        paramBundle = new com/android/internal/telephony/imsphone/ImsPhoneConnection;
        ImsPhone localImsPhone = mPhone;
        localObject1 = ImsPhoneCallTracker.this;
        if (bool) {
          paramIImsCallSession = mForegroundCall;
        } else {
          paramIImsCallSession = mRingingCall;
        }
        paramBundle.<init>(localImsPhone, (ImsCall)localObject2, (ImsPhoneCallTracker)localObject1, paramIImsCallSession, bool);
        paramIImsCallSession = ImsPhoneCallTracker.this;
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("onReceive(): conn = ");
        ((StringBuilder)localObject1).append(paramBundle);
        paramIImsCallSession.log(((StringBuilder)localObject1).toString());
        if (mForegroundCall.hasConnections())
        {
          paramIImsCallSession = mForegroundCall.getFirstConnection().getImsCall();
          if ((paramIImsCallSession != null) && (localObject2 != null)) {
            paramBundle.setActiveCallDisconnectedOnAnswer(ImsPhoneCallTracker.this.shouldDisconnectActiveCallOnAnswer(paramIImsCallSession, (ImsCall)localObject2));
          }
        }
        paramBundle.setAllowAddCallDuringVideoCall(mAllowAddCallDuringVideoCall);
        paramBundle.setAllowHoldingVideoCall(mAllowHoldingVideoCall);
        ImsPhoneCallTracker.this.addConnection(paramBundle);
        ImsPhoneCallTracker.this.setVideoCallProvider(paramBundle, (ImsCall)localObject2);
        TelephonyMetrics.getInstance().writeOnImsCallReceive(mPhone.getPhoneId(), ((ImsCall)localObject2).getSession());
        if (bool)
        {
          mPhone.notifyUnknownConnection(paramBundle);
        }
        else
        {
          if ((mForegroundCall.getState() != Call.State.IDLE) || (mBackgroundCall.getState() != Call.State.IDLE)) {
            paramBundle.update((ImsCall)localObject2, Call.State.WAITING);
          }
          mPhone.notifyNewRingingConnection(paramBundle);
          mPhone.notifyIncomingRing();
        }
        ImsPhoneCallTracker.this.updatePhoneState();
        mPhone.notifyPreciseCallStateChanged();
      }
      catch (RemoteException paramIImsCallSession) {}catch (ImsException paramBundle)
      {
        Object localObject2 = ImsPhoneCallTracker.this;
        paramIImsCallSession = new StringBuilder();
        paramIImsCallSession.append("onReceive : exception ");
        paramIImsCallSession.append(paramBundle);
        ((ImsPhoneCallTracker)localObject2).loge(paramIImsCallSession.toString());
      }
    }
    
    public void onVoiceMessageCountUpdate(int paramInt)
    {
      if ((mPhone != null) && (mPhone.mDefaultPhone != null))
      {
        ImsPhoneCallTracker localImsPhoneCallTracker = ImsPhoneCallTracker.this;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("onVoiceMessageCountChanged :: count=");
        localStringBuilder.append(paramInt);
        localImsPhoneCallTracker.log(localStringBuilder.toString());
        mPhone.mDefaultPhone.setVoiceMessageCount(paramInt);
      }
      else
      {
        loge("onVoiceMessageCountUpdate: null phone");
      }
    }
  }
  
  public static abstract interface PhoneNumberUtilsProxy
  {
    public abstract boolean isEmergencyNumber(String paramString);
  }
  
  public static abstract interface PhoneStateListener
  {
    public abstract void onPhoneStateChanged(PhoneConstants.State paramState1, PhoneConstants.State paramState2);
  }
  
  public static abstract interface SharedPreferenceProxy
  {
    public abstract SharedPreferences getDefaultSharedPreferences(Context paramContext);
  }
}
