package android.media.tv;

import android.annotation.SuppressLint;
import android.annotation.SystemApi;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.hardware.hdmi.HdmiDeviceInfo;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.InputChannel;
import android.view.InputEvent;
import android.view.InputEventReceiver;
import android.view.KeyEvent;
import android.view.KeyEvent.Callback;
import android.view.KeyEvent.DispatcherState;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewRootImpl;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import com.android.internal.os.SomeArgs;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class TvInputService
  extends Service
{
  private static final boolean DEBUG = false;
  private static final int DETACH_OVERLAY_VIEW_TIMEOUT_MS = 5000;
  public static final String SERVICE_INTERFACE = "android.media.tv.TvInputService";
  public static final String SERVICE_META_DATA = "android.media.tv.input";
  private static final String TAG = "TvInputService";
  private final RemoteCallbackList<ITvInputServiceCallback> mCallbacks = new RemoteCallbackList();
  private final Handler mServiceHandler = new ServiceHandler(null);
  private TvInputManager mTvInputManager;
  
  public TvInputService() {}
  
  public static boolean isNavigationKey(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return false;
    }
    return true;
  }
  
  private boolean isPassthroughInput(String paramString)
  {
    if (mTvInputManager == null) {
      mTvInputManager = ((TvInputManager)getSystemService("tv_input"));
    }
    paramString = mTvInputManager.getTvInputInfo(paramString);
    boolean bool;
    if ((paramString != null) && (paramString.isPassthroughInput())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final IBinder onBind(Intent paramIntent)
  {
    new ITvInputService.Stub()
    {
      public void createRecordingSession(ITvInputSessionCallback paramAnonymousITvInputSessionCallback, String paramAnonymousString)
      {
        if (paramAnonymousITvInputSessionCallback == null) {
          return;
        }
        SomeArgs localSomeArgs = SomeArgs.obtain();
        arg1 = paramAnonymousITvInputSessionCallback;
        arg2 = paramAnonymousString;
        mServiceHandler.obtainMessage(3, localSomeArgs).sendToTarget();
      }
      
      public void createSession(InputChannel paramAnonymousInputChannel, ITvInputSessionCallback paramAnonymousITvInputSessionCallback, String paramAnonymousString)
      {
        if (paramAnonymousInputChannel == null) {
          Log.w("TvInputService", "Creating session without input channel");
        }
        if (paramAnonymousITvInputSessionCallback == null) {
          return;
        }
        SomeArgs localSomeArgs = SomeArgs.obtain();
        arg1 = paramAnonymousInputChannel;
        arg2 = paramAnonymousITvInputSessionCallback;
        arg3 = paramAnonymousString;
        mServiceHandler.obtainMessage(1, localSomeArgs).sendToTarget();
      }
      
      public void notifyHardwareAdded(TvInputHardwareInfo paramAnonymousTvInputHardwareInfo)
      {
        mServiceHandler.obtainMessage(4, paramAnonymousTvInputHardwareInfo).sendToTarget();
      }
      
      public void notifyHardwareRemoved(TvInputHardwareInfo paramAnonymousTvInputHardwareInfo)
      {
        mServiceHandler.obtainMessage(5, paramAnonymousTvInputHardwareInfo).sendToTarget();
      }
      
      public void notifyHdmiDeviceAdded(HdmiDeviceInfo paramAnonymousHdmiDeviceInfo)
      {
        mServiceHandler.obtainMessage(6, paramAnonymousHdmiDeviceInfo).sendToTarget();
      }
      
      public void notifyHdmiDeviceRemoved(HdmiDeviceInfo paramAnonymousHdmiDeviceInfo)
      {
        mServiceHandler.obtainMessage(7, paramAnonymousHdmiDeviceInfo).sendToTarget();
      }
      
      public void registerCallback(ITvInputServiceCallback paramAnonymousITvInputServiceCallback)
      {
        if (paramAnonymousITvInputServiceCallback != null) {
          mCallbacks.register(paramAnonymousITvInputServiceCallback);
        }
      }
      
      public void unregisterCallback(ITvInputServiceCallback paramAnonymousITvInputServiceCallback)
      {
        if (paramAnonymousITvInputServiceCallback != null) {
          mCallbacks.unregister(paramAnonymousITvInputServiceCallback);
        }
      }
    };
  }
  
  public RecordingSession onCreateRecordingSession(String paramString)
  {
    return null;
  }
  
  public abstract Session onCreateSession(String paramString);
  
  @SystemApi
  public TvInputInfo onHardwareAdded(TvInputHardwareInfo paramTvInputHardwareInfo)
  {
    return null;
  }
  
  @SystemApi
  public String onHardwareRemoved(TvInputHardwareInfo paramTvInputHardwareInfo)
  {
    return null;
  }
  
  @SystemApi
  public TvInputInfo onHdmiDeviceAdded(HdmiDeviceInfo paramHdmiDeviceInfo)
  {
    return null;
  }
  
  @SystemApi
  public String onHdmiDeviceRemoved(HdmiDeviceInfo paramHdmiDeviceInfo)
  {
    return null;
  }
  
  public static abstract class HardwareSession
    extends TvInputService.Session
  {
    private TvInputManager.Session mHardwareSession;
    private final TvInputManager.SessionCallback mHardwareSessionCallback = new TvInputManager.SessionCallback()
    {
      public void onSessionCreated(TvInputManager.Session paramAnonymousSession)
      {
        TvInputService.HardwareSession.access$1502(TvInputService.HardwareSession.this, paramAnonymousSession);
        SomeArgs localSomeArgs = SomeArgs.obtain();
        if (paramAnonymousSession != null)
        {
          arg1 = TvInputService.HardwareSession.this;
          arg2 = mProxySession;
          arg3 = mProxySessionCallback;
          arg4 = paramAnonymousSession.getToken();
          paramAnonymousSession.tune(TvContract.buildChannelUriForPassthroughInput(getHardwareInputId()));
        }
        else
        {
          arg1 = null;
          arg2 = null;
          arg3 = mProxySessionCallback;
          arg4 = null;
          onRelease();
        }
        mServiceHandler.obtainMessage(2, localSomeArgs).sendToTarget();
      }
      
      public void onVideoAvailable(TvInputManager.Session paramAnonymousSession)
      {
        if (mHardwareSession == paramAnonymousSession) {
          onHardwareVideoAvailable();
        }
      }
      
      public void onVideoUnavailable(TvInputManager.Session paramAnonymousSession, int paramAnonymousInt)
      {
        if (mHardwareSession == paramAnonymousSession) {
          onHardwareVideoUnavailable(paramAnonymousInt);
        }
      }
    };
    private ITvInputSession mProxySession;
    private ITvInputSessionCallback mProxySessionCallback;
    private Handler mServiceHandler;
    
    public HardwareSession(Context paramContext)
    {
      super();
    }
    
    public abstract String getHardwareInputId();
    
    public void onHardwareVideoAvailable() {}
    
    public void onHardwareVideoUnavailable(int paramInt) {}
    
    public final boolean onSetSurface(Surface paramSurface)
    {
      Log.e("TvInputService", "onSetSurface() should not be called in HardwareProxySession.");
      return false;
    }
    
    void release()
    {
      if (mHardwareSession != null)
      {
        mHardwareSession.release();
        mHardwareSession = null;
      }
      super.release();
    }
  }
  
  private static final class OverlayViewCleanUpTask
    extends AsyncTask<View, Void, Void>
  {
    private OverlayViewCleanUpTask() {}
    
    protected Void doInBackground(View... paramVarArgs)
    {
      View localView = paramVarArgs[0];
      try
      {
        Thread.sleep(5000L);
        if (isCancelled()) {
          return null;
        }
        if (localView.isAttachedToWindow())
        {
          paramVarArgs = new StringBuilder();
          paramVarArgs.append("Time out on releasing overlay view. Killing ");
          paramVarArgs.append(localView.getContext().getPackageName());
          Log.e("TvInputService", paramVarArgs.toString());
          Process.killProcess(Process.myPid());
        }
        return null;
      }
      catch (InterruptedException paramVarArgs) {}
      return null;
    }
  }
  
  public static abstract class RecordingSession
  {
    final Handler mHandler;
    private final Object mLock = new Object();
    private final List<Runnable> mPendingActions = new ArrayList();
    private ITvInputSessionCallback mSessionCallback;
    
    public RecordingSession(Context paramContext)
    {
      mHandler = new Handler(paramContext.getMainLooper());
    }
    
    private void executeOrPostRunnableOnMainThread(Runnable paramRunnable)
    {
      synchronized (mLock)
      {
        if (mSessionCallback == null) {
          mPendingActions.add(paramRunnable);
        } else if (mHandler.getLooper().isCurrentThread()) {
          paramRunnable.run();
        } else {
          mHandler.post(paramRunnable);
        }
        return;
      }
    }
    
    private void initialize(ITvInputSessionCallback paramITvInputSessionCallback)
    {
      synchronized (mLock)
      {
        mSessionCallback = paramITvInputSessionCallback;
        paramITvInputSessionCallback = mPendingActions.iterator();
        while (paramITvInputSessionCallback.hasNext()) {
          ((Runnable)paramITvInputSessionCallback.next()).run();
        }
        mPendingActions.clear();
        return;
      }
    }
    
    void appPrivateCommand(String paramString, Bundle paramBundle)
    {
      onAppPrivateCommand(paramString, paramBundle);
    }
    
    public void notifyError(int paramInt)
    {
      final int i;
      if (paramInt >= 0)
      {
        i = paramInt;
        if (paramInt <= 2) {}
      }
      else
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("notifyError - invalid error code (");
        localStringBuilder.append(paramInt);
        localStringBuilder.append(") is changed to RECORDING_ERROR_UNKNOWN.");
        Log.w("TvInputService", localStringBuilder.toString());
        i = 0;
      }
      executeOrPostRunnableOnMainThread(new Runnable()
      {
        public void run()
        {
          try
          {
            if (mSessionCallback != null) {
              mSessionCallback.onError(i);
            }
          }
          catch (RemoteException localRemoteException)
          {
            Log.w("TvInputService", "error in notifyError", localRemoteException);
          }
        }
      });
    }
    
    public void notifyRecordingStopped(final Uri paramUri)
    {
      executeOrPostRunnableOnMainThread(new Runnable()
      {
        public void run()
        {
          try
          {
            if (mSessionCallback != null) {
              mSessionCallback.onRecordingStopped(paramUri);
            }
          }
          catch (RemoteException localRemoteException)
          {
            Log.w("TvInputService", "error in notifyRecordingStopped", localRemoteException);
          }
        }
      });
    }
    
    @SystemApi
    public void notifySessionEvent(final String paramString, final Bundle paramBundle)
    {
      Preconditions.checkNotNull(paramString);
      executeOrPostRunnableOnMainThread(new Runnable()
      {
        public void run()
        {
          try
          {
            if (mSessionCallback != null) {
              mSessionCallback.onSessionEvent(paramString, paramBundle);
            }
          }
          catch (RemoteException localRemoteException)
          {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("error in sending event (event=");
            localStringBuilder.append(paramString);
            localStringBuilder.append(")");
            Log.w("TvInputService", localStringBuilder.toString(), localRemoteException);
          }
        }
      });
    }
    
    public void notifyTuned(final Uri paramUri)
    {
      executeOrPostRunnableOnMainThread(new Runnable()
      {
        public void run()
        {
          try
          {
            if (mSessionCallback != null) {
              mSessionCallback.onTuned(paramUri);
            }
          }
          catch (RemoteException localRemoteException)
          {
            Log.w("TvInputService", "error in notifyTuned", localRemoteException);
          }
        }
      });
    }
    
    public void onAppPrivateCommand(String paramString, Bundle paramBundle) {}
    
    public abstract void onRelease();
    
    public abstract void onStartRecording(Uri paramUri);
    
    public abstract void onStopRecording();
    
    public abstract void onTune(Uri paramUri);
    
    public void onTune(Uri paramUri, Bundle paramBundle)
    {
      onTune(paramUri);
    }
    
    void release()
    {
      onRelease();
    }
    
    void startRecording(Uri paramUri)
    {
      onStartRecording(paramUri);
    }
    
    void stopRecording()
    {
      onStopRecording();
    }
    
    void tune(Uri paramUri, Bundle paramBundle)
    {
      onTune(paramUri, paramBundle);
    }
  }
  
  @SuppressLint({"HandlerLeak"})
  private final class ServiceHandler
    extends Handler
  {
    private static final int DO_ADD_HARDWARE_INPUT = 4;
    private static final int DO_ADD_HDMI_INPUT = 6;
    private static final int DO_CREATE_RECORDING_SESSION = 3;
    private static final int DO_CREATE_SESSION = 1;
    private static final int DO_NOTIFY_SESSION_CREATED = 2;
    private static final int DO_REMOVE_HARDWARE_INPUT = 5;
    private static final int DO_REMOVE_HDMI_INPUT = 7;
    
    private ServiceHandler() {}
    
    private void broadcastAddHardwareInput(int paramInt, TvInputInfo paramTvInputInfo)
    {
      int i = mCallbacks.beginBroadcast();
      for (int j = 0; j < i; j++) {
        try
        {
          ((ITvInputServiceCallback)mCallbacks.getBroadcastItem(j)).addHardwareInput(paramInt, paramTvInputInfo);
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("TvInputService", "error in broadcastAddHardwareInput", localRemoteException);
        }
      }
      mCallbacks.finishBroadcast();
    }
    
    private void broadcastAddHdmiInput(int paramInt, TvInputInfo paramTvInputInfo)
    {
      int i = mCallbacks.beginBroadcast();
      for (int j = 0; j < i; j++) {
        try
        {
          ((ITvInputServiceCallback)mCallbacks.getBroadcastItem(j)).addHdmiInput(paramInt, paramTvInputInfo);
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("TvInputService", "error in broadcastAddHdmiInput", localRemoteException);
        }
      }
      mCallbacks.finishBroadcast();
    }
    
    private void broadcastRemoveHardwareInput(String paramString)
    {
      int i = mCallbacks.beginBroadcast();
      for (int j = 0; j < i; j++) {
        try
        {
          ((ITvInputServiceCallback)mCallbacks.getBroadcastItem(j)).removeHardwareInput(paramString);
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("TvInputService", "error in broadcastRemoveHardwareInput", localRemoteException);
        }
      }
      mCallbacks.finishBroadcast();
    }
    
    public final void handleMessage(Message paramMessage)
    {
      Object localObject4;
      switch (what)
      {
      default: 
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("Unhandled message code: ");
        ((StringBuilder)localObject1).append(what);
        Log.w("TvInputService", ((StringBuilder)localObject1).toString());
        return;
      case 7: 
        paramMessage = (HdmiDeviceInfo)obj;
        paramMessage = onHdmiDeviceRemoved(paramMessage);
        if (paramMessage != null) {
          broadcastRemoveHardwareInput(paramMessage);
        }
        return;
      case 6: 
        localObject1 = (HdmiDeviceInfo)obj;
        paramMessage = onHdmiDeviceAdded((HdmiDeviceInfo)localObject1);
        if (paramMessage != null) {
          broadcastAddHdmiInput(((HdmiDeviceInfo)localObject1).getId(), paramMessage);
        }
        return;
      case 5: 
        paramMessage = (TvInputHardwareInfo)obj;
        paramMessage = onHardwareRemoved(paramMessage);
        if (paramMessage != null) {
          broadcastRemoveHardwareInput(paramMessage);
        }
        return;
      case 4: 
        localObject1 = (TvInputHardwareInfo)obj;
        paramMessage = onHardwareAdded((TvInputHardwareInfo)localObject1);
        if (paramMessage != null) {
          broadcastAddHardwareInput(((TvInputHardwareInfo)localObject1).getDeviceId(), paramMessage);
        }
        return;
      case 3: 
        localObject1 = (SomeArgs)obj;
        paramMessage = (ITvInputSessionCallback)arg1;
        Object localObject2 = (String)arg2;
        ((SomeArgs)localObject1).recycle();
        localObject1 = onCreateRecordingSession((String)localObject2);
        if (localObject1 == null)
        {
          try
          {
            paramMessage.onSessionCreated(null, null);
          }
          catch (RemoteException paramMessage)
          {
            Log.e("TvInputService", "error in onSessionCreated", paramMessage);
          }
          return;
        }
        localObject2 = new ITvInputSessionWrapper(TvInputService.this, (TvInputService.RecordingSession)localObject1);
        try
        {
          paramMessage.onSessionCreated((ITvInputSession)localObject2, null);
        }
        catch (RemoteException localRemoteException1)
        {
          Log.e("TvInputService", "error in onSessionCreated", localRemoteException1);
        }
        ((TvInputService.RecordingSession)localObject1).initialize(paramMessage);
        return;
      case 2: 
        localObject3 = (SomeArgs)obj;
        paramMessage = (TvInputService.Session)arg1;
        localObject4 = (ITvInputSession)arg2;
        localObject1 = (ITvInputSessionCallback)arg3;
        IBinder localIBinder = (IBinder)arg4;
        try
        {
          ((ITvInputSessionCallback)localObject1).onSessionCreated((ITvInputSession)localObject4, localIBinder);
        }
        catch (RemoteException localRemoteException2)
        {
          Log.e("TvInputService", "error in onSessionCreated", localRemoteException2);
        }
        if (paramMessage != null) {
          TvInputService.Session.access$2100(paramMessage, (ITvInputSessionCallback)localObject1);
        }
        ((SomeArgs)localObject3).recycle();
        return;
      }
      Object localObject1 = (SomeArgs)obj;
      Object localObject3 = (InputChannel)arg1;
      paramMessage = (ITvInputSessionCallback)arg2;
      Object localObject5 = (String)arg3;
      ((SomeArgs)localObject1).recycle();
      localObject1 = onCreateSession((String)localObject5);
      if (localObject1 == null)
      {
        try
        {
          paramMessage.onSessionCreated(null, null);
        }
        catch (RemoteException paramMessage)
        {
          Log.e("TvInputService", "error in onSessionCreated", paramMessage);
        }
        return;
      }
      localObject5 = new ITvInputSessionWrapper(TvInputService.this, (TvInputService.Session)localObject1, (InputChannel)localObject3);
      if ((localObject1 instanceof TvInputService.HardwareSession))
      {
        localObject4 = (TvInputService.HardwareSession)localObject1;
        localObject3 = ((TvInputService.HardwareSession)localObject4).getHardwareInputId();
        if ((!TextUtils.isEmpty((CharSequence)localObject3)) && (TvInputService.this.isPassthroughInput((String)localObject3)))
        {
          TvInputService.HardwareSession.access$1602((TvInputService.HardwareSession)localObject4, (ITvInputSession)localObject5);
          TvInputService.HardwareSession.access$1702((TvInputService.HardwareSession)localObject4, paramMessage);
          TvInputService.HardwareSession.access$1802((TvInputService.HardwareSession)localObject4, mServiceHandler);
          paramMessage = (TvInputManager)getSystemService("tv_input");
          paramMessage.createSession((String)localObject3, mHardwareSessionCallback, mServiceHandler);
        }
        else
        {
          if (TextUtils.isEmpty((CharSequence)localObject3))
          {
            Log.w("TvInputService", "Hardware input id is not setup yet.");
          }
          else
          {
            localObject5 = new StringBuilder();
            ((StringBuilder)localObject5).append("Invalid hardware input id : ");
            ((StringBuilder)localObject5).append((String)localObject3);
            Log.w("TvInputService", ((StringBuilder)localObject5).toString());
          }
          ((TvInputService.Session)localObject1).onRelease();
          try
          {
            paramMessage.onSessionCreated(null, null);
          }
          catch (RemoteException paramMessage)
          {
            Log.e("TvInputService", "error in onSessionCreated", paramMessage);
          }
        }
      }
      else
      {
        localObject3 = SomeArgs.obtain();
        arg1 = localObject1;
        arg2 = localObject5;
        arg3 = paramMessage;
        arg4 = null;
        mServiceHandler.obtainMessage(2, localObject3).sendToTarget();
      }
    }
  }
  
  public static abstract class Session
    implements KeyEvent.Callback
  {
    private static final int POSITION_UPDATE_INTERVAL_MS = 1000;
    private final Context mContext;
    private long mCurrentPositionMs = Long.MIN_VALUE;
    private final KeyEvent.DispatcherState mDispatcherState = new KeyEvent.DispatcherState();
    final Handler mHandler;
    private final Object mLock = new Object();
    private Rect mOverlayFrame;
    private View mOverlayView;
    private TvInputService.OverlayViewCleanUpTask mOverlayViewCleanUpTask;
    private FrameLayout mOverlayViewContainer;
    private boolean mOverlayViewEnabled;
    private final List<Runnable> mPendingActions = new ArrayList();
    private ITvInputSessionCallback mSessionCallback;
    private long mStartPositionMs = Long.MIN_VALUE;
    private Surface mSurface;
    private final TimeShiftPositionTrackingRunnable mTimeShiftPositionTrackingRunnable = new TimeShiftPositionTrackingRunnable(null);
    private final WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowParams;
    private IBinder mWindowToken;
    
    public Session(Context paramContext)
    {
      mContext = paramContext;
      mWindowManager = ((WindowManager)paramContext.getSystemService("window"));
      mHandler = new Handler(paramContext.getMainLooper());
    }
    
    private void executeOrPostRunnableOnMainThread(Runnable paramRunnable)
    {
      synchronized (mLock)
      {
        if (mSessionCallback == null) {
          mPendingActions.add(paramRunnable);
        } else if (mHandler.getLooper().isCurrentThread()) {
          paramRunnable.run();
        } else {
          mHandler.post(paramRunnable);
        }
        return;
      }
    }
    
    private void initialize(ITvInputSessionCallback paramITvInputSessionCallback)
    {
      synchronized (mLock)
      {
        mSessionCallback = paramITvInputSessionCallback;
        paramITvInputSessionCallback = mPendingActions.iterator();
        while (paramITvInputSessionCallback.hasNext()) {
          ((Runnable)paramITvInputSessionCallback.next()).run();
        }
        mPendingActions.clear();
        return;
      }
    }
    
    private void notifyTimeShiftCurrentPositionChanged(final long paramLong)
    {
      executeOrPostRunnableOnMainThread(new Runnable()
      {
        public void run()
        {
          try
          {
            if (mSessionCallback != null) {
              mSessionCallback.onTimeShiftCurrentPositionChanged(paramLong);
            }
          }
          catch (RemoteException localRemoteException)
          {
            Log.w("TvInputService", "error in notifyTimeShiftCurrentPositionChanged", localRemoteException);
          }
        }
      });
    }
    
    private void notifyTimeShiftStartPositionChanged(final long paramLong)
    {
      executeOrPostRunnableOnMainThread(new Runnable()
      {
        public void run()
        {
          try
          {
            if (mSessionCallback != null) {
              mSessionCallback.onTimeShiftStartPositionChanged(paramLong);
            }
          }
          catch (RemoteException localRemoteException)
          {
            Log.w("TvInputService", "error in notifyTimeShiftStartPositionChanged", localRemoteException);
          }
        }
      });
    }
    
    void appPrivateCommand(String paramString, Bundle paramBundle)
    {
      onAppPrivateCommand(paramString, paramBundle);
    }
    
    void createOverlayView(IBinder paramIBinder, Rect paramRect)
    {
      if (mOverlayViewContainer != null) {
        removeOverlayView(false);
      }
      mWindowToken = paramIBinder;
      mOverlayFrame = paramRect;
      onOverlayViewSizeChanged(right - left, bottom - top);
      if (!mOverlayViewEnabled) {
        return;
      }
      mOverlayView = onCreateOverlayView();
      if (mOverlayView == null) {
        return;
      }
      if (mOverlayViewCleanUpTask != null)
      {
        mOverlayViewCleanUpTask.cancel(true);
        mOverlayViewCleanUpTask = null;
      }
      mOverlayViewContainer = new FrameLayout(mContext.getApplicationContext());
      mOverlayViewContainer.addView(mOverlayView);
      int i = 536;
      if (ActivityManager.isHighEndGfx()) {
        i = 0x218 | 0x1000000;
      }
      mWindowParams = new WindowManager.LayoutParams(right - left, bottom - top, left, top, 1004, i, -2);
      paramRect = mWindowParams;
      privateFlags |= 0x40;
      mWindowParams.gravity = 8388659;
      mWindowParams.token = paramIBinder;
      mWindowManager.addView(mOverlayViewContainer, mWindowParams);
    }
    
    int dispatchInputEvent(InputEvent paramInputEvent, InputEventReceiver paramInputEventReceiver)
    {
      boolean bool1 = false;
      int i = 0;
      Object localObject;
      boolean bool2;
      int j;
      if ((paramInputEvent instanceof KeyEvent))
      {
        localObject = (KeyEvent)paramInputEvent;
        if (((KeyEvent)localObject).dispatch(this, mDispatcherState, this)) {
          return 1;
        }
        bool2 = TvInputService.isNavigationKey(((KeyEvent)localObject).getKeyCode());
        if ((!KeyEvent.isMediaKey(((KeyEvent)localObject).getKeyCode())) && (((KeyEvent)localObject).getKeyCode() != 222)) {
          j = 0;
        } else {
          j = 1;
        }
      }
      else
      {
        bool2 = bool1;
        j = i;
        if ((paramInputEvent instanceof MotionEvent))
        {
          localObject = (MotionEvent)paramInputEvent;
          j = ((MotionEvent)localObject).getSource();
          if (((MotionEvent)localObject).isTouchEvent())
          {
            bool2 = bool1;
            j = i;
            if (onTouchEvent((MotionEvent)localObject)) {
              return 1;
            }
          }
          else if ((j & 0x4) != 0)
          {
            bool2 = bool1;
            j = i;
            if (onTrackballEvent((MotionEvent)localObject)) {
              return 1;
            }
          }
          else
          {
            bool2 = bool1;
            j = i;
            if (onGenericMotionEvent((MotionEvent)localObject)) {
              return 1;
            }
          }
        }
      }
      if ((mOverlayViewContainer != null) && (mOverlayViewContainer.isAttachedToWindow()) && (j == 0))
      {
        if (!mOverlayViewContainer.hasWindowFocus()) {
          mOverlayViewContainer.getViewRootImpl().windowFocusChanged(true, true);
        }
        if ((bool2) && (mOverlayViewContainer.hasFocusable()))
        {
          mOverlayViewContainer.getViewRootImpl().dispatchInputEvent(paramInputEvent);
          return 1;
        }
        mOverlayViewContainer.getViewRootImpl().dispatchInputEvent(paramInputEvent, paramInputEventReceiver);
        return -1;
      }
      return 0;
    }
    
    void dispatchSurfaceChanged(int paramInt1, int paramInt2, int paramInt3)
    {
      onSurfaceChanged(paramInt1, paramInt2, paramInt3);
    }
    
    public void layoutSurface(final int paramInt1, final int paramInt2, final int paramInt3, final int paramInt4)
    {
      if ((paramInt1 <= paramInt3) && (paramInt2 <= paramInt4))
      {
        executeOrPostRunnableOnMainThread(new Runnable()
        {
          public void run()
          {
            try
            {
              if (mSessionCallback != null) {
                mSessionCallback.onLayoutSurface(paramInt1, paramInt2, paramInt3, paramInt4);
              }
            }
            catch (RemoteException localRemoteException)
            {
              Log.w("TvInputService", "error in layoutSurface", localRemoteException);
            }
          }
        });
        return;
      }
      throw new IllegalArgumentException("Invalid parameter");
    }
    
    public void notifyChannelRetuned(final Uri paramUri)
    {
      executeOrPostRunnableOnMainThread(new Runnable()
      {
        public void run()
        {
          try
          {
            if (mSessionCallback != null) {
              mSessionCallback.onChannelRetuned(paramUri);
            }
          }
          catch (RemoteException localRemoteException)
          {
            Log.w("TvInputService", "error in notifyChannelRetuned", localRemoteException);
          }
        }
      });
    }
    
    public void notifyContentAllowed()
    {
      executeOrPostRunnableOnMainThread(new Runnable()
      {
        public void run()
        {
          try
          {
            if (mSessionCallback != null) {
              mSessionCallback.onContentAllowed();
            }
          }
          catch (RemoteException localRemoteException)
          {
            Log.w("TvInputService", "error in notifyContentAllowed", localRemoteException);
          }
        }
      });
    }
    
    public void notifyContentBlocked(final TvContentRating paramTvContentRating)
    {
      Preconditions.checkNotNull(paramTvContentRating);
      executeOrPostRunnableOnMainThread(new Runnable()
      {
        public void run()
        {
          try
          {
            if (mSessionCallback != null) {
              mSessionCallback.onContentBlocked(paramTvContentRating.flattenToString());
            }
          }
          catch (RemoteException localRemoteException)
          {
            Log.w("TvInputService", "error in notifyContentBlocked", localRemoteException);
          }
        }
      });
    }
    
    @SystemApi
    public void notifySessionEvent(final String paramString, final Bundle paramBundle)
    {
      Preconditions.checkNotNull(paramString);
      executeOrPostRunnableOnMainThread(new Runnable()
      {
        public void run()
        {
          try
          {
            if (mSessionCallback != null) {
              mSessionCallback.onSessionEvent(paramString, paramBundle);
            }
          }
          catch (RemoteException localRemoteException)
          {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("error in sending event (event=");
            localStringBuilder.append(paramString);
            localStringBuilder.append(")");
            Log.w("TvInputService", localStringBuilder.toString(), localRemoteException);
          }
        }
      });
    }
    
    public void notifyTimeShiftStatusChanged(final int paramInt)
    {
      executeOrPostRunnableOnMainThread(new Runnable()
      {
        public void run()
        {
          TvInputService.Session localSession = TvInputService.Session.this;
          boolean bool;
          if (paramInt == 3) {
            bool = true;
          } else {
            bool = false;
          }
          localSession.timeShiftEnablePositionTracking(bool);
          try
          {
            if (mSessionCallback != null) {
              mSessionCallback.onTimeShiftStatusChanged(paramInt);
            }
          }
          catch (RemoteException localRemoteException)
          {
            Log.w("TvInputService", "error in notifyTimeShiftStatusChanged", localRemoteException);
          }
        }
      });
    }
    
    public void notifyTrackSelected(final int paramInt, final String paramString)
    {
      executeOrPostRunnableOnMainThread(new Runnable()
      {
        public void run()
        {
          try
          {
            if (mSessionCallback != null) {
              mSessionCallback.onTrackSelected(paramInt, paramString);
            }
          }
          catch (RemoteException localRemoteException)
          {
            Log.w("TvInputService", "error in notifyTrackSelected", localRemoteException);
          }
        }
      });
    }
    
    public void notifyTracksChanged(List<TvTrackInfo> paramList)
    {
      executeOrPostRunnableOnMainThread(new Runnable()
      {
        public void run()
        {
          try
          {
            if (mSessionCallback != null) {
              mSessionCallback.onTracksChanged(val$tracksCopy);
            }
          }
          catch (RemoteException localRemoteException)
          {
            Log.w("TvInputService", "error in notifyTracksChanged", localRemoteException);
          }
        }
      });
    }
    
    public void notifyVideoAvailable()
    {
      executeOrPostRunnableOnMainThread(new Runnable()
      {
        public void run()
        {
          try
          {
            if (mSessionCallback != null) {
              mSessionCallback.onVideoAvailable();
            }
          }
          catch (RemoteException localRemoteException)
          {
            Log.w("TvInputService", "error in notifyVideoAvailable", localRemoteException);
          }
        }
      });
    }
    
    public void notifyVideoUnavailable(final int paramInt)
    {
      if ((paramInt < 0) || (paramInt > 4))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("notifyVideoUnavailable - unknown reason: ");
        localStringBuilder.append(paramInt);
        Log.e("TvInputService", localStringBuilder.toString());
      }
      executeOrPostRunnableOnMainThread(new Runnable()
      {
        public void run()
        {
          try
          {
            if (mSessionCallback != null) {
              mSessionCallback.onVideoUnavailable(paramInt);
            }
          }
          catch (RemoteException localRemoteException)
          {
            Log.w("TvInputService", "error in notifyVideoUnavailable", localRemoteException);
          }
        }
      });
    }
    
    public void onAppPrivateCommand(String paramString, Bundle paramBundle) {}
    
    public View onCreateOverlayView()
    {
      return null;
    }
    
    public boolean onGenericMotionEvent(MotionEvent paramMotionEvent)
    {
      return false;
    }
    
    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
    {
      return false;
    }
    
    public boolean onKeyLongPress(int paramInt, KeyEvent paramKeyEvent)
    {
      return false;
    }
    
    public boolean onKeyMultiple(int paramInt1, int paramInt2, KeyEvent paramKeyEvent)
    {
      return false;
    }
    
    public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
    {
      return false;
    }
    
    public void onOverlayViewSizeChanged(int paramInt1, int paramInt2) {}
    
    public abstract void onRelease();
    
    public boolean onSelectTrack(int paramInt, String paramString)
    {
      return false;
    }
    
    public abstract void onSetCaptionEnabled(boolean paramBoolean);
    
    @SystemApi
    public void onSetMain(boolean paramBoolean) {}
    
    public abstract void onSetStreamVolume(float paramFloat);
    
    public abstract boolean onSetSurface(Surface paramSurface);
    
    public void onSurfaceChanged(int paramInt1, int paramInt2, int paramInt3) {}
    
    public long onTimeShiftGetCurrentPosition()
    {
      return Long.MIN_VALUE;
    }
    
    public long onTimeShiftGetStartPosition()
    {
      return Long.MIN_VALUE;
    }
    
    public void onTimeShiftPause() {}
    
    public void onTimeShiftPlay(Uri paramUri) {}
    
    public void onTimeShiftResume() {}
    
    public void onTimeShiftSeekTo(long paramLong) {}
    
    public void onTimeShiftSetPlaybackParams(PlaybackParams paramPlaybackParams) {}
    
    public boolean onTouchEvent(MotionEvent paramMotionEvent)
    {
      return false;
    }
    
    public boolean onTrackballEvent(MotionEvent paramMotionEvent)
    {
      return false;
    }
    
    public abstract boolean onTune(Uri paramUri);
    
    public boolean onTune(Uri paramUri, Bundle paramBundle)
    {
      return onTune(paramUri);
    }
    
    public void onUnblockContent(TvContentRating paramTvContentRating) {}
    
    void relayoutOverlayView(Rect paramRect)
    {
      if ((mOverlayFrame == null) || (mOverlayFrame.width() != paramRect.width()) || (mOverlayFrame.height() != paramRect.height())) {
        onOverlayViewSizeChanged(right - left, bottom - top);
      }
      mOverlayFrame = paramRect;
      if ((mOverlayViewEnabled) && (mOverlayViewContainer != null))
      {
        mWindowParams.x = left;
        mWindowParams.y = top;
        mWindowParams.width = (right - left);
        mWindowParams.height = (bottom - top);
        mWindowManager.updateViewLayout(mOverlayViewContainer, mWindowParams);
        return;
      }
    }
    
    void release()
    {
      onRelease();
      if (mSurface != null)
      {
        mSurface.release();
        mSurface = null;
      }
      synchronized (mLock)
      {
        mSessionCallback = null;
        mPendingActions.clear();
        removeOverlayView(true);
        mHandler.removeCallbacks(mTimeShiftPositionTrackingRunnable);
        return;
      }
    }
    
    void removeOverlayView(boolean paramBoolean)
    {
      if (paramBoolean)
      {
        mWindowToken = null;
        mOverlayFrame = null;
      }
      if (mOverlayViewContainer != null)
      {
        mOverlayViewContainer.removeView(mOverlayView);
        mOverlayView = null;
        mWindowManager.removeView(mOverlayViewContainer);
        mOverlayViewContainer = null;
        mWindowParams = null;
      }
    }
    
    void scheduleOverlayViewCleanup()
    {
      FrameLayout localFrameLayout = mOverlayViewContainer;
      if (localFrameLayout != null)
      {
        mOverlayViewCleanUpTask = new TvInputService.OverlayViewCleanUpTask(null);
        mOverlayViewCleanUpTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new View[] { localFrameLayout });
      }
    }
    
    void selectTrack(int paramInt, String paramString)
    {
      onSelectTrack(paramInt, paramString);
    }
    
    void setCaptionEnabled(boolean paramBoolean)
    {
      onSetCaptionEnabled(paramBoolean);
    }
    
    void setMain(boolean paramBoolean)
    {
      onSetMain(paramBoolean);
    }
    
    public void setOverlayViewEnabled(final boolean paramBoolean)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          if (paramBoolean == mOverlayViewEnabled) {
            return;
          }
          TvInputService.Session.access$402(TvInputService.Session.this, paramBoolean);
          if (paramBoolean)
          {
            if (mWindowToken != null) {
              createOverlayView(mWindowToken, mOverlayFrame);
            }
          }
          else {
            removeOverlayView(false);
          }
        }
      });
    }
    
    void setStreamVolume(float paramFloat)
    {
      onSetStreamVolume(paramFloat);
    }
    
    void setSurface(Surface paramSurface)
    {
      onSetSurface(paramSurface);
      if (mSurface != null) {
        mSurface.release();
      }
      mSurface = paramSurface;
    }
    
    void timeShiftEnablePositionTracking(boolean paramBoolean)
    {
      if (paramBoolean)
      {
        mHandler.post(mTimeShiftPositionTrackingRunnable);
      }
      else
      {
        mHandler.removeCallbacks(mTimeShiftPositionTrackingRunnable);
        mStartPositionMs = Long.MIN_VALUE;
        mCurrentPositionMs = Long.MIN_VALUE;
      }
    }
    
    void timeShiftPause()
    {
      onTimeShiftPause();
    }
    
    void timeShiftPlay(Uri paramUri)
    {
      mCurrentPositionMs = 0L;
      onTimeShiftPlay(paramUri);
    }
    
    void timeShiftResume()
    {
      onTimeShiftResume();
    }
    
    void timeShiftSeekTo(long paramLong)
    {
      onTimeShiftSeekTo(paramLong);
    }
    
    void timeShiftSetPlaybackParams(PlaybackParams paramPlaybackParams)
    {
      onTimeShiftSetPlaybackParams(paramPlaybackParams);
    }
    
    void tune(Uri paramUri, Bundle paramBundle)
    {
      mCurrentPositionMs = Long.MIN_VALUE;
      onTune(paramUri, paramBundle);
    }
    
    void unblockContent(String paramString)
    {
      onUnblockContent(TvContentRating.unflattenFromString(paramString));
    }
    
    private final class TimeShiftPositionTrackingRunnable
      implements Runnable
    {
      private TimeShiftPositionTrackingRunnable() {}
      
      public void run()
      {
        long l1 = onTimeShiftGetStartPosition();
        if ((mStartPositionMs == Long.MIN_VALUE) || (mStartPositionMs != l1))
        {
          TvInputService.Session.access$902(TvInputService.Session.this, l1);
          TvInputService.Session.this.notifyTimeShiftStartPositionChanged(l1);
        }
        long l2 = onTimeShiftGetCurrentPosition();
        l1 = l2;
        if (l2 < mStartPositionMs)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Current position (");
          localStringBuilder.append(l2);
          localStringBuilder.append(") cannot be earlier than start position (");
          localStringBuilder.append(mStartPositionMs);
          localStringBuilder.append("). Reset to the start position.");
          Log.w("TvInputService", localStringBuilder.toString());
          l1 = mStartPositionMs;
        }
        if ((mCurrentPositionMs == Long.MIN_VALUE) || (mCurrentPositionMs != l1))
        {
          TvInputService.Session.access$1102(TvInputService.Session.this, l1);
          TvInputService.Session.this.notifyTimeShiftCurrentPositionChanged(l1);
        }
        mHandler.removeCallbacks(mTimeShiftPositionTrackingRunnable);
        mHandler.postDelayed(mTimeShiftPositionTrackingRunnable, 1000L);
      }
    }
  }
}
