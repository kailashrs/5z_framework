package android.accessibilityservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Region;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.WindowManagerImpl;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityInteractionClient;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;
import com.android.internal.os.HandlerCaller;
import com.android.internal.os.HandlerCaller.Callback;
import com.android.internal.os.SomeArgs;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public abstract class AccessibilityService
  extends Service
{
  public static final int GESTURE_SWIPE_DOWN = 2;
  public static final int GESTURE_SWIPE_DOWN_AND_LEFT = 15;
  public static final int GESTURE_SWIPE_DOWN_AND_RIGHT = 16;
  public static final int GESTURE_SWIPE_DOWN_AND_UP = 8;
  public static final int GESTURE_SWIPE_LEFT = 3;
  public static final int GESTURE_SWIPE_LEFT_AND_DOWN = 10;
  public static final int GESTURE_SWIPE_LEFT_AND_RIGHT = 5;
  public static final int GESTURE_SWIPE_LEFT_AND_UP = 9;
  public static final int GESTURE_SWIPE_RIGHT = 4;
  public static final int GESTURE_SWIPE_RIGHT_AND_DOWN = 12;
  public static final int GESTURE_SWIPE_RIGHT_AND_LEFT = 6;
  public static final int GESTURE_SWIPE_RIGHT_AND_UP = 11;
  public static final int GESTURE_SWIPE_UP = 1;
  public static final int GESTURE_SWIPE_UP_AND_DOWN = 7;
  public static final int GESTURE_SWIPE_UP_AND_LEFT = 13;
  public static final int GESTURE_SWIPE_UP_AND_RIGHT = 14;
  public static final int GLOBAL_ACTION_BACK = 1;
  public static final int GLOBAL_ACTION_HOME = 2;
  public static final int GLOBAL_ACTION_LOCK_SCREEN = 8;
  public static final int GLOBAL_ACTION_NOTIFICATIONS = 4;
  public static final int GLOBAL_ACTION_POWER_DIALOG = 6;
  public static final int GLOBAL_ACTION_QUICK_SETTINGS = 5;
  public static final int GLOBAL_ACTION_RECENTS = 3;
  public static final int GLOBAL_ACTION_TAKE_SCREENSHOT = 9;
  public static final int GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN = 7;
  private static final String LOG_TAG = "AccessibilityService";
  public static final String SERVICE_INTERFACE = "android.accessibilityservice.AccessibilityService";
  public static final String SERVICE_META_DATA = "android.accessibilityservice";
  public static final int SHOW_MODE_AUTO = 0;
  public static final int SHOW_MODE_HIDDEN = 1;
  private AccessibilityButtonController mAccessibilityButtonController;
  private int mConnectionId = -1;
  private FingerprintGestureController mFingerprintGestureController;
  private SparseArray<GestureResultCallbackInfo> mGestureStatusCallbackInfos;
  private int mGestureStatusCallbackSequence;
  private AccessibilityServiceInfo mInfo;
  private final Object mLock = new Object();
  private MagnificationController mMagnificationController;
  private SoftKeyboardController mSoftKeyboardController;
  private WindowManager mWindowManager;
  private IBinder mWindowToken;
  
  public AccessibilityService() {}
  
  private void dispatchServiceConnected()
  {
    if (mMagnificationController != null) {
      mMagnificationController.onServiceConnected();
    }
    if (mSoftKeyboardController != null) {
      mSoftKeyboardController.onServiceConnected();
    }
    onServiceConnected();
  }
  
  private void onAccessibilityButtonAvailabilityChanged(boolean paramBoolean)
  {
    getAccessibilityButtonController().dispatchAccessibilityButtonAvailabilityChanged(paramBoolean);
  }
  
  private void onAccessibilityButtonClicked()
  {
    getAccessibilityButtonController().dispatchAccessibilityButtonClicked();
  }
  
  private void onFingerprintCapturingGesturesChanged(boolean paramBoolean)
  {
    getFingerprintGestureController().onGestureDetectionActiveChanged(paramBoolean);
  }
  
  private void onFingerprintGesture(int paramInt)
  {
    getFingerprintGestureController().onGesture(paramInt);
  }
  
  private void onMagnificationChanged(Region paramRegion, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (mMagnificationController != null) {
      mMagnificationController.dispatchMagnificationChanged(paramRegion, paramFloat1, paramFloat2, paramFloat3);
    }
  }
  
  private void onSoftKeyboardShowModeChanged(int paramInt)
  {
    if (mSoftKeyboardController != null) {
      mSoftKeyboardController.dispatchSoftKeyboardShowModeChanged(paramInt);
    }
  }
  
  private void sendServiceInfo()
  {
    AccessibilityInteractionClient.getInstance();
    IAccessibilityServiceConnection localIAccessibilityServiceConnection = AccessibilityInteractionClient.getConnection(mConnectionId);
    if ((mInfo != null) && (localIAccessibilityServiceConnection != null)) {
      try
      {
        localIAccessibilityServiceConnection.setServiceInfo(mInfo);
        mInfo = null;
        AccessibilityInteractionClient.getInstance().clearCache();
      }
      catch (RemoteException localRemoteException)
      {
        Log.w("AccessibilityService", "Error while setting AccessibilityServiceInfo", localRemoteException);
        localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  public final void disableSelf()
  {
    AccessibilityInteractionClient.getInstance();
    IAccessibilityServiceConnection localIAccessibilityServiceConnection = AccessibilityInteractionClient.getConnection(mConnectionId);
    if (localIAccessibilityServiceConnection != null) {
      try
      {
        localIAccessibilityServiceConnection.disableSelf();
      }
      catch (RemoteException localRemoteException)
      {
        throw new RuntimeException(localRemoteException);
      }
    }
  }
  
  /* Error */
  public final boolean dispatchGesture(GestureDescription paramGestureDescription, GestureResultCallback paramGestureResultCallback, Handler paramHandler)
  {
    // Byte code:
    //   0: invokestatic 218	android/view/accessibility/AccessibilityInteractionClient:getInstance	()Landroid/view/accessibility/AccessibilityInteractionClient;
    //   3: pop
    //   4: aload_0
    //   5: getfield 121	android/accessibilityservice/AccessibilityService:mConnectionId	I
    //   8: invokestatic 222	android/view/accessibility/AccessibilityInteractionClient:getConnection	(I)Landroid/accessibilityservice/IAccessibilityServiceConnection;
    //   11: astore 4
    //   13: aload 4
    //   15: ifnonnull +5 -> 20
    //   18: iconst_0
    //   19: ireturn
    //   20: aload_1
    //   21: bipush 100
    //   23: invokestatic 261	android/accessibilityservice/GestureDescription$MotionEventGenerator:getGestureStepsFromGestureDescription	(Landroid/accessibilityservice/GestureDescription;I)Ljava/util/List;
    //   26: astore 5
    //   28: aload_0
    //   29: getfield 126	android/accessibilityservice/AccessibilityService:mLock	Ljava/lang/Object;
    //   32: astore 6
    //   34: aload 6
    //   36: monitorenter
    //   37: aload_0
    //   38: aload_0
    //   39: getfield 263	android/accessibilityservice/AccessibilityService:mGestureStatusCallbackSequence	I
    //   42: iconst_1
    //   43: iadd
    //   44: putfield 263	android/accessibilityservice/AccessibilityService:mGestureStatusCallbackSequence	I
    //   47: aload_2
    //   48: ifnull +52 -> 100
    //   51: aload_0
    //   52: getfield 265	android/accessibilityservice/AccessibilityService:mGestureStatusCallbackInfos	Landroid/util/SparseArray;
    //   55: ifnonnull +19 -> 74
    //   58: new 267	android/util/SparseArray
    //   61: astore 7
    //   63: aload 7
    //   65: invokespecial 268	android/util/SparseArray:<init>	()V
    //   68: aload_0
    //   69: aload 7
    //   71: putfield 265	android/accessibilityservice/AccessibilityService:mGestureStatusCallbackInfos	Landroid/util/SparseArray;
    //   74: new 16	android/accessibilityservice/AccessibilityService$GestureResultCallbackInfo
    //   77: astore 7
    //   79: aload 7
    //   81: aload_1
    //   82: aload_2
    //   83: aload_3
    //   84: invokespecial 271	android/accessibilityservice/AccessibilityService$GestureResultCallbackInfo:<init>	(Landroid/accessibilityservice/GestureDescription;Landroid/accessibilityservice/AccessibilityService$GestureResultCallback;Landroid/os/Handler;)V
    //   87: aload_0
    //   88: getfield 265	android/accessibilityservice/AccessibilityService:mGestureStatusCallbackInfos	Landroid/util/SparseArray;
    //   91: aload_0
    //   92: getfield 263	android/accessibilityservice/AccessibilityService:mGestureStatusCallbackSequence	I
    //   95: aload 7
    //   97: invokevirtual 275	android/util/SparseArray:put	(ILjava/lang/Object;)V
    //   100: aload_0
    //   101: getfield 263	android/accessibilityservice/AccessibilityService:mGestureStatusCallbackSequence	I
    //   104: istore 8
    //   106: new 277	android/content/pm/ParceledListSlice
    //   109: astore_1
    //   110: aload_1
    //   111: aload 5
    //   113: invokespecial 280	android/content/pm/ParceledListSlice:<init>	(Ljava/util/List;)V
    //   116: aload 4
    //   118: iload 8
    //   120: aload_1
    //   121: invokeinterface 284 3 0
    //   126: aload 6
    //   128: monitorexit
    //   129: iconst_1
    //   130: ireturn
    //   131: astore_1
    //   132: aload 6
    //   134: monitorexit
    //   135: aload_1
    //   136: athrow
    //   137: astore_1
    //   138: new 250	java/lang/RuntimeException
    //   141: dup
    //   142: aload_1
    //   143: invokespecial 253	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
    //   146: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	147	0	this	AccessibilityService
    //   0	147	1	paramGestureDescription	GestureDescription
    //   0	147	2	paramGestureResultCallback	GestureResultCallback
    //   0	147	3	paramHandler	Handler
    //   11	106	4	localIAccessibilityServiceConnection	IAccessibilityServiceConnection
    //   26	86	5	localList	List
    //   61	35	7	localObject2	Object
    //   104	15	8	i	int
    // Exception table:
    //   from	to	target	type
    //   37	47	131	finally
    //   51	74	131	finally
    //   74	100	131	finally
    //   100	129	131	finally
    //   132	135	131	finally
    //   28	37	137	android/os/RemoteException
    //   135	137	137	android/os/RemoteException
  }
  
  public AccessibilityNodeInfo findFocus(int paramInt)
  {
    return AccessibilityInteractionClient.getInstance().findFocus(mConnectionId, -2, AccessibilityNodeInfo.ROOT_NODE_ID, paramInt);
  }
  
  public final AccessibilityButtonController getAccessibilityButtonController()
  {
    synchronized (mLock)
    {
      if (mAccessibilityButtonController == null)
      {
        localAccessibilityButtonController = new android/accessibilityservice/AccessibilityButtonController;
        AccessibilityInteractionClient.getInstance();
        localAccessibilityButtonController.<init>(AccessibilityInteractionClient.getConnection(mConnectionId));
        mAccessibilityButtonController = localAccessibilityButtonController;
      }
      AccessibilityButtonController localAccessibilityButtonController = mAccessibilityButtonController;
      return localAccessibilityButtonController;
    }
  }
  
  public final FingerprintGestureController getFingerprintGestureController()
  {
    if (mFingerprintGestureController == null)
    {
      AccessibilityInteractionClient.getInstance();
      mFingerprintGestureController = new FingerprintGestureController(AccessibilityInteractionClient.getConnection(mConnectionId));
    }
    return mFingerprintGestureController;
  }
  
  public final MagnificationController getMagnificationController()
  {
    synchronized (mLock)
    {
      if (mMagnificationController == null)
      {
        localMagnificationController = new android/accessibilityservice/AccessibilityService$MagnificationController;
        localMagnificationController.<init>(this, mLock);
        mMagnificationController = localMagnificationController;
      }
      MagnificationController localMagnificationController = mMagnificationController;
      return localMagnificationController;
    }
  }
  
  public AccessibilityNodeInfo getRootInActiveWindow()
  {
    return AccessibilityInteractionClient.getInstance().getRootInActiveWindow(mConnectionId);
  }
  
  public final AccessibilityServiceInfo getServiceInfo()
  {
    AccessibilityInteractionClient.getInstance();
    Object localObject = AccessibilityInteractionClient.getConnection(mConnectionId);
    if (localObject != null) {
      try
      {
        localObject = ((IAccessibilityServiceConnection)localObject).getServiceInfo();
        return localObject;
      }
      catch (RemoteException localRemoteException)
      {
        Log.w("AccessibilityService", "Error while getting AccessibilityServiceInfo", localRemoteException);
        localRemoteException.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  public final SoftKeyboardController getSoftKeyboardController()
  {
    synchronized (mLock)
    {
      if (mSoftKeyboardController == null)
      {
        localSoftKeyboardController = new android/accessibilityservice/AccessibilityService$SoftKeyboardController;
        localSoftKeyboardController.<init>(this, mLock);
        mSoftKeyboardController = localSoftKeyboardController;
      }
      SoftKeyboardController localSoftKeyboardController = mSoftKeyboardController;
      return localSoftKeyboardController;
    }
  }
  
  public Object getSystemService(String paramString)
  {
    if (getBaseContext() != null)
    {
      if ("window".equals(paramString))
      {
        if (mWindowManager == null) {
          mWindowManager = ((WindowManager)getBaseContext().getSystemService(paramString));
        }
        return mWindowManager;
      }
      return super.getSystemService(paramString);
    }
    throw new IllegalStateException("System services not available to Activities before onCreate()");
  }
  
  public List<AccessibilityWindowInfo> getWindows()
  {
    return AccessibilityInteractionClient.getInstance().getWindows(mConnectionId);
  }
  
  public abstract void onAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent);
  
  public final IBinder onBind(Intent paramIntent)
  {
    new IAccessibilityServiceClientWrapper(this, getMainLooper(), new Callbacks()
    {
      public void init(int paramAnonymousInt, IBinder paramAnonymousIBinder)
      {
        AccessibilityService.access$002(AccessibilityService.this, paramAnonymousInt);
        AccessibilityService.access$202(AccessibilityService.this, paramAnonymousIBinder);
        ((WindowManagerImpl)getSystemService("window")).setDefaultToken(paramAnonymousIBinder);
      }
      
      public void onAccessibilityButtonAvailabilityChanged(boolean paramAnonymousBoolean)
      {
        AccessibilityService.this.onAccessibilityButtonAvailabilityChanged(paramAnonymousBoolean);
      }
      
      public void onAccessibilityButtonClicked()
      {
        AccessibilityService.this.onAccessibilityButtonClicked();
      }
      
      public void onAccessibilityEvent(AccessibilityEvent paramAnonymousAccessibilityEvent)
      {
        AccessibilityService.this.onAccessibilityEvent(paramAnonymousAccessibilityEvent);
      }
      
      public void onFingerprintCapturingGesturesChanged(boolean paramAnonymousBoolean)
      {
        AccessibilityService.this.onFingerprintCapturingGesturesChanged(paramAnonymousBoolean);
      }
      
      public void onFingerprintGesture(int paramAnonymousInt)
      {
        AccessibilityService.this.onFingerprintGesture(paramAnonymousInt);
      }
      
      public boolean onGesture(int paramAnonymousInt)
      {
        return AccessibilityService.this.onGesture(paramAnonymousInt);
      }
      
      public void onInterrupt()
      {
        AccessibilityService.this.onInterrupt();
      }
      
      public boolean onKeyEvent(KeyEvent paramAnonymousKeyEvent)
      {
        return AccessibilityService.this.onKeyEvent(paramAnonymousKeyEvent);
      }
      
      public void onMagnificationChanged(Region paramAnonymousRegion, float paramAnonymousFloat1, float paramAnonymousFloat2, float paramAnonymousFloat3)
      {
        AccessibilityService.this.onMagnificationChanged(paramAnonymousRegion, paramAnonymousFloat1, paramAnonymousFloat2, paramAnonymousFloat3);
      }
      
      public void onPerformGestureResult(int paramAnonymousInt, boolean paramAnonymousBoolean)
      {
        AccessibilityService.this.onPerformGestureResult(paramAnonymousInt, paramAnonymousBoolean);
      }
      
      public void onServiceConnected()
      {
        AccessibilityService.this.dispatchServiceConnected();
      }
      
      public void onSoftKeyboardShowModeChanged(int paramAnonymousInt)
      {
        AccessibilityService.this.onSoftKeyboardShowModeChanged(paramAnonymousInt);
      }
    });
  }
  
  protected boolean onGesture(int paramInt)
  {
    return false;
  }
  
  public abstract void onInterrupt();
  
  protected boolean onKeyEvent(KeyEvent paramKeyEvent)
  {
    return false;
  }
  
  void onPerformGestureResult(int paramInt, final boolean paramBoolean)
  {
    if (mGestureStatusCallbackInfos == null) {
      return;
    }
    synchronized (mLock)
    {
      final GestureResultCallbackInfo localGestureResultCallbackInfo = (GestureResultCallbackInfo)mGestureStatusCallbackInfos.get(paramInt);
      if ((localGestureResultCallbackInfo != null) && (gestureDescription != null) && (callback != null))
      {
        if (handler != null)
        {
          handler.post(new Runnable()
          {
            public void run()
            {
              if (paramBoolean) {
                localGestureResultCallbackInfocallback.onCompleted(localGestureResultCallbackInfogestureDescription);
              } else {
                localGestureResultCallbackInfocallback.onCancelled(localGestureResultCallbackInfogestureDescription);
              }
            }
          });
          return;
        }
        if (paramBoolean) {
          callback.onCompleted(gestureDescription);
        } else {
          callback.onCancelled(gestureDescription);
        }
      }
      return;
    }
  }
  
  protected void onServiceConnected() {}
  
  public final boolean performGlobalAction(int paramInt)
  {
    AccessibilityInteractionClient.getInstance();
    IAccessibilityServiceConnection localIAccessibilityServiceConnection = AccessibilityInteractionClient.getConnection(mConnectionId);
    if (localIAccessibilityServiceConnection != null) {
      try
      {
        boolean bool = localIAccessibilityServiceConnection.performGlobalAction(paramInt);
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        Log.w("AccessibilityService", "Error while calling performGlobalAction", localRemoteException);
        localRemoteException.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public final void setServiceInfo(AccessibilityServiceInfo paramAccessibilityServiceInfo)
  {
    mInfo = paramAccessibilityServiceInfo;
    sendServiceInfo();
  }
  
  public static abstract interface Callbacks
  {
    public abstract void init(int paramInt, IBinder paramIBinder);
    
    public abstract void onAccessibilityButtonAvailabilityChanged(boolean paramBoolean);
    
    public abstract void onAccessibilityButtonClicked();
    
    public abstract void onAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent);
    
    public abstract void onFingerprintCapturingGesturesChanged(boolean paramBoolean);
    
    public abstract void onFingerprintGesture(int paramInt);
    
    public abstract boolean onGesture(int paramInt);
    
    public abstract void onInterrupt();
    
    public abstract boolean onKeyEvent(KeyEvent paramKeyEvent);
    
    public abstract void onMagnificationChanged(Region paramRegion, float paramFloat1, float paramFloat2, float paramFloat3);
    
    public abstract void onPerformGestureResult(int paramInt, boolean paramBoolean);
    
    public abstract void onServiceConnected();
    
    public abstract void onSoftKeyboardShowModeChanged(int paramInt);
  }
  
  public static abstract class GestureResultCallback
  {
    public GestureResultCallback() {}
    
    public void onCancelled(GestureDescription paramGestureDescription) {}
    
    public void onCompleted(GestureDescription paramGestureDescription) {}
  }
  
  private static class GestureResultCallbackInfo
  {
    AccessibilityService.GestureResultCallback callback;
    GestureDescription gestureDescription;
    Handler handler;
    
    GestureResultCallbackInfo(GestureDescription paramGestureDescription, AccessibilityService.GestureResultCallback paramGestureResultCallback, Handler paramHandler)
    {
      gestureDescription = paramGestureDescription;
      callback = paramGestureResultCallback;
      handler = paramHandler;
    }
  }
  
  public static class IAccessibilityServiceClientWrapper
    extends IAccessibilityServiceClient.Stub
    implements HandlerCaller.Callback
  {
    private static final int DO_ACCESSIBILITY_BUTTON_AVAILABILITY_CHANGED = 13;
    private static final int DO_ACCESSIBILITY_BUTTON_CLICKED = 12;
    private static final int DO_CLEAR_ACCESSIBILITY_CACHE = 5;
    private static final int DO_GESTURE_COMPLETE = 9;
    private static final int DO_INIT = 1;
    private static final int DO_ON_ACCESSIBILITY_EVENT = 3;
    private static final int DO_ON_FINGERPRINT_ACTIVE_CHANGED = 10;
    private static final int DO_ON_FINGERPRINT_GESTURE = 11;
    private static final int DO_ON_GESTURE = 4;
    private static final int DO_ON_INTERRUPT = 2;
    private static final int DO_ON_KEY_EVENT = 6;
    private static final int DO_ON_MAGNIFICATION_CHANGED = 7;
    private static final int DO_ON_SOFT_KEYBOARD_SHOW_MODE_CHANGED = 8;
    private final AccessibilityService.Callbacks mCallback;
    private final HandlerCaller mCaller;
    private int mConnectionId = -1;
    
    public IAccessibilityServiceClientWrapper(Context paramContext, Looper paramLooper, AccessibilityService.Callbacks paramCallbacks)
    {
      mCallback = paramCallbacks;
      mCaller = new HandlerCaller(paramContext, paramLooper, this, true);
    }
    
    public void clearAccessibilityCache()
    {
      Message localMessage = mCaller.obtainMessage(5);
      mCaller.sendMessage(localMessage);
    }
    
    public void executeMessage(Message paramMessage)
    {
      int i = what;
      boolean bool1 = false;
      boolean bool2 = false;
      int j = 0;
      boolean bool3 = false;
      Object localObject1;
      switch (i)
      {
      default: 
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("Unknown message type ");
        ((StringBuilder)localObject1).append(what);
        Log.w("AccessibilityService", ((StringBuilder)localObject1).toString());
        return;
      case 13: 
        if (mConnectionId != -1)
        {
          if (arg1 != 0) {
            bool3 = true;
          }
          mCallback.onAccessibilityButtonAvailabilityChanged(bool3);
        }
        return;
      case 12: 
        if (mConnectionId != -1) {
          mCallback.onAccessibilityButtonClicked();
        }
        return;
      case 11: 
        if (mConnectionId != -1) {
          mCallback.onFingerprintGesture(arg1);
        }
        return;
      case 10: 
        if (mConnectionId != -1)
        {
          localObject1 = mCallback;
          bool3 = bool1;
          if (arg1 == 1) {
            bool3 = true;
          }
          ((AccessibilityService.Callbacks)localObject1).onFingerprintCapturingGesturesChanged(bool3);
        }
        return;
      case 9: 
        if (mConnectionId != -1)
        {
          bool3 = bool2;
          if (arg2 == 1) {
            bool3 = true;
          }
          mCallback.onPerformGestureResult(arg1, bool3);
        }
        return;
      case 8: 
        if (mConnectionId != -1)
        {
          j = arg1;
          mCallback.onSoftKeyboardShowModeChanged(j);
        }
        return;
      case 7: 
        if (mConnectionId != -1)
        {
          localObject1 = (SomeArgs)obj;
          paramMessage = (Region)arg1;
          float f1 = ((Float)arg2).floatValue();
          float f2 = ((Float)arg3).floatValue();
          float f3 = ((Float)arg4).floatValue();
          mCallback.onMagnificationChanged(paramMessage, f1, f2, f3);
        }
        return;
      case 6: 
        localObject1 = (KeyEvent)obj;
        try
        {
          AccessibilityInteractionClient.getInstance();
          localObject3 = AccessibilityInteractionClient.getConnection(mConnectionId);
          if (localObject3 != null)
          {
            bool3 = mCallback.onKeyEvent((KeyEvent)localObject1);
            j = arg1;
            try
            {
              ((IAccessibilityServiceConnection)localObject3).setOnKeyEventResult(bool3, j);
            }
            catch (RemoteException paramMessage) {}
          }
          return;
        }
        finally
        {
          try
          {
            ((KeyEvent)localObject1).recycle();
          }
          catch (IllegalStateException localIllegalStateException) {}
        }
      case 5: 
        AccessibilityInteractionClient.getInstance().clearCache();
        return;
      case 4: 
        if (mConnectionId != -1)
        {
          j = arg1;
          mCallback.onGesture(j);
        }
        return;
      case 3: 
        localObject2 = (AccessibilityEvent)obj;
        if (arg1 != 0) {
          j = 1;
        }
        if (localObject2 != null)
        {
          AccessibilityInteractionClient.getInstance().onAccessibilityEvent((AccessibilityEvent)localObject2);
          if ((j != 0) && (mConnectionId != -1)) {
            mCallback.onAccessibilityEvent((AccessibilityEvent)localObject2);
          }
          try
          {
            ((AccessibilityEvent)localObject2).recycle();
          }
          catch (IllegalStateException paramMessage) {}
        }
        return;
      case 2: 
        if (mConnectionId != -1) {
          mCallback.onInterrupt();
        }
        return;
      }
      mConnectionId = arg1;
      Object localObject3 = (SomeArgs)obj;
      Object localObject2 = (IAccessibilityServiceConnection)arg1;
      paramMessage = (IBinder)arg2;
      ((SomeArgs)localObject3).recycle();
      if (localObject2 != null)
      {
        AccessibilityInteractionClient.getInstance();
        AccessibilityInteractionClient.addConnection(mConnectionId, (IAccessibilityServiceConnection)localObject2);
        mCallback.init(mConnectionId, paramMessage);
        mCallback.onServiceConnected();
      }
      else
      {
        AccessibilityInteractionClient.getInstance();
        AccessibilityInteractionClient.removeConnection(mConnectionId);
        mConnectionId = -1;
        AccessibilityInteractionClient.getInstance().clearCache();
        mCallback.init(-1, null);
      }
    }
    
    public void init(IAccessibilityServiceConnection paramIAccessibilityServiceConnection, int paramInt, IBinder paramIBinder)
    {
      paramIAccessibilityServiceConnection = mCaller.obtainMessageIOO(1, paramInt, paramIAccessibilityServiceConnection, paramIBinder);
      mCaller.sendMessage(paramIAccessibilityServiceConnection);
    }
    
    public void onAccessibilityButtonAvailabilityChanged(boolean paramBoolean)
    {
      Object localObject = mCaller;
      localObject = ((HandlerCaller)localObject).obtainMessageI(13, paramBoolean);
      mCaller.sendMessage((Message)localObject);
    }
    
    public void onAccessibilityButtonClicked()
    {
      Message localMessage = mCaller.obtainMessage(12);
      mCaller.sendMessage(localMessage);
    }
    
    public void onAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent, boolean paramBoolean)
    {
      paramAccessibilityEvent = mCaller.obtainMessageBO(3, paramBoolean, paramAccessibilityEvent);
      mCaller.sendMessage(paramAccessibilityEvent);
    }
    
    public void onFingerprintCapturingGesturesChanged(boolean paramBoolean)
    {
      HandlerCaller localHandlerCaller1 = mCaller;
      HandlerCaller localHandlerCaller2 = mCaller;
      localHandlerCaller1.sendMessage(localHandlerCaller2.obtainMessageI(10, paramBoolean));
    }
    
    public void onFingerprintGesture(int paramInt)
    {
      mCaller.sendMessage(mCaller.obtainMessageI(11, paramInt));
    }
    
    public void onGesture(int paramInt)
    {
      Message localMessage = mCaller.obtainMessageI(4, paramInt);
      mCaller.sendMessage(localMessage);
    }
    
    public void onInterrupt()
    {
      Message localMessage = mCaller.obtainMessage(2);
      mCaller.sendMessage(localMessage);
    }
    
    public void onKeyEvent(KeyEvent paramKeyEvent, int paramInt)
    {
      paramKeyEvent = mCaller.obtainMessageIO(6, paramInt, paramKeyEvent);
      mCaller.sendMessage(paramKeyEvent);
    }
    
    public void onMagnificationChanged(Region paramRegion, float paramFloat1, float paramFloat2, float paramFloat3)
    {
      SomeArgs localSomeArgs = SomeArgs.obtain();
      arg1 = paramRegion;
      arg2 = Float.valueOf(paramFloat1);
      arg3 = Float.valueOf(paramFloat2);
      arg4 = Float.valueOf(paramFloat3);
      paramRegion = mCaller.obtainMessageO(7, localSomeArgs);
      mCaller.sendMessage(paramRegion);
    }
    
    public void onPerformGestureResult(int paramInt, boolean paramBoolean)
    {
      Object localObject = mCaller;
      localObject = ((HandlerCaller)localObject).obtainMessageII(9, paramInt, paramBoolean);
      mCaller.sendMessage((Message)localObject);
    }
    
    public void onSoftKeyboardShowModeChanged(int paramInt)
    {
      Message localMessage = mCaller.obtainMessageI(8, paramInt);
      mCaller.sendMessage(localMessage);
    }
  }
  
  public static final class MagnificationController
  {
    private ArrayMap<OnMagnificationChangedListener, Handler> mListeners;
    private final Object mLock;
    private final AccessibilityService mService;
    
    MagnificationController(AccessibilityService paramAccessibilityService, Object paramObject)
    {
      mService = paramAccessibilityService;
      mLock = paramObject;
    }
    
    private void setMagnificationCallbackEnabled(boolean paramBoolean)
    {
      AccessibilityInteractionClient.getInstance();
      IAccessibilityServiceConnection localIAccessibilityServiceConnection = AccessibilityInteractionClient.getConnection(mService.mConnectionId);
      if (localIAccessibilityServiceConnection != null) {
        try
        {
          localIAccessibilityServiceConnection.setMagnificationCallbackEnabled(paramBoolean);
        }
        catch (RemoteException localRemoteException)
        {
          throw new RuntimeException(localRemoteException);
        }
      }
    }
    
    public void addListener(OnMagnificationChangedListener paramOnMagnificationChangedListener)
    {
      addListener(paramOnMagnificationChangedListener, null);
    }
    
    public void addListener(OnMagnificationChangedListener paramOnMagnificationChangedListener, Handler paramHandler)
    {
      synchronized (mLock)
      {
        if (mListeners == null)
        {
          ArrayMap localArrayMap = new android/util/ArrayMap;
          localArrayMap.<init>();
          mListeners = localArrayMap;
        }
        boolean bool = mListeners.isEmpty();
        mListeners.put(paramOnMagnificationChangedListener, paramHandler);
        if (bool) {
          setMagnificationCallbackEnabled(true);
        }
        return;
      }
    }
    
    void dispatchMagnificationChanged(final Region paramRegion, final float paramFloat1, final float paramFloat2, final float paramFloat3)
    {
      synchronized (mLock)
      {
        if ((mListeners != null) && (!mListeners.isEmpty()))
        {
          ArrayMap localArrayMap = new android/util/ArrayMap;
          localArrayMap.<init>(mListeners);
          int i = localArrayMap.size();
          for (int j = 0; j < i; j++)
          {
            final OnMagnificationChangedListener localOnMagnificationChangedListener = (OnMagnificationChangedListener)localArrayMap.keyAt(j);
            ??? = (Handler)localArrayMap.valueAt(j);
            if (??? != null) {
              ((Handler)???).post(new Runnable()
              {
                public void run()
                {
                  localOnMagnificationChangedListener.onMagnificationChanged(AccessibilityService.MagnificationController.this, paramRegion, paramFloat1, paramFloat2, paramFloat3);
                }
              });
            } else {
              localOnMagnificationChangedListener.onMagnificationChanged(this, paramRegion, paramFloat1, paramFloat2, paramFloat3);
            }
          }
          return;
        }
        Slog.d("AccessibilityService", "Received magnification changed callback with no listeners registered!");
        setMagnificationCallbackEnabled(false);
        return;
      }
    }
    
    public float getCenterX()
    {
      AccessibilityInteractionClient.getInstance();
      IAccessibilityServiceConnection localIAccessibilityServiceConnection = AccessibilityInteractionClient.getConnection(mService.mConnectionId);
      if (localIAccessibilityServiceConnection != null) {
        try
        {
          float f = localIAccessibilityServiceConnection.getMagnificationCenterX();
          return f;
        }
        catch (RemoteException localRemoteException)
        {
          Log.w("AccessibilityService", "Failed to obtain center X", localRemoteException);
          localRemoteException.rethrowFromSystemServer();
        }
      }
      return 0.0F;
    }
    
    public float getCenterY()
    {
      AccessibilityInteractionClient.getInstance();
      IAccessibilityServiceConnection localIAccessibilityServiceConnection = AccessibilityInteractionClient.getConnection(mService.mConnectionId);
      if (localIAccessibilityServiceConnection != null) {
        try
        {
          float f = localIAccessibilityServiceConnection.getMagnificationCenterY();
          return f;
        }
        catch (RemoteException localRemoteException)
        {
          Log.w("AccessibilityService", "Failed to obtain center Y", localRemoteException);
          localRemoteException.rethrowFromSystemServer();
        }
      }
      return 0.0F;
    }
    
    public Region getMagnificationRegion()
    {
      AccessibilityInteractionClient.getInstance();
      Object localObject = AccessibilityInteractionClient.getConnection(mService.mConnectionId);
      if (localObject != null) {
        try
        {
          localObject = ((IAccessibilityServiceConnection)localObject).getMagnificationRegion();
          return localObject;
        }
        catch (RemoteException localRemoteException)
        {
          Log.w("AccessibilityService", "Failed to obtain magnified region", localRemoteException);
          localRemoteException.rethrowFromSystemServer();
        }
      }
      return Region.obtain();
    }
    
    public float getScale()
    {
      AccessibilityInteractionClient.getInstance();
      IAccessibilityServiceConnection localIAccessibilityServiceConnection = AccessibilityInteractionClient.getConnection(mService.mConnectionId);
      if (localIAccessibilityServiceConnection != null) {
        try
        {
          float f = localIAccessibilityServiceConnection.getMagnificationScale();
          return f;
        }
        catch (RemoteException localRemoteException)
        {
          Log.w("AccessibilityService", "Failed to obtain scale", localRemoteException);
          localRemoteException.rethrowFromSystemServer();
        }
      }
      return 1.0F;
    }
    
    void onServiceConnected()
    {
      synchronized (mLock)
      {
        if ((mListeners != null) && (!mListeners.isEmpty())) {
          setMagnificationCallbackEnabled(true);
        }
        return;
      }
    }
    
    public boolean removeListener(OnMagnificationChangedListener paramOnMagnificationChangedListener)
    {
      if (mListeners == null) {
        return false;
      }
      synchronized (mLock)
      {
        int i = mListeners.indexOfKey(paramOnMagnificationChangedListener);
        boolean bool;
        if (i >= 0) {
          bool = true;
        } else {
          bool = false;
        }
        if (bool) {
          mListeners.removeAt(i);
        }
        if ((bool) && (mListeners.isEmpty())) {
          setMagnificationCallbackEnabled(false);
        }
        return bool;
      }
    }
    
    public boolean reset(boolean paramBoolean)
    {
      AccessibilityInteractionClient.getInstance();
      IAccessibilityServiceConnection localIAccessibilityServiceConnection = AccessibilityInteractionClient.getConnection(mService.mConnectionId);
      if (localIAccessibilityServiceConnection != null) {
        try
        {
          paramBoolean = localIAccessibilityServiceConnection.resetMagnification(paramBoolean);
          return paramBoolean;
        }
        catch (RemoteException localRemoteException)
        {
          Log.w("AccessibilityService", "Failed to reset", localRemoteException);
          localRemoteException.rethrowFromSystemServer();
        }
      }
      return false;
    }
    
    public boolean setCenter(float paramFloat1, float paramFloat2, boolean paramBoolean)
    {
      AccessibilityInteractionClient.getInstance();
      IAccessibilityServiceConnection localIAccessibilityServiceConnection = AccessibilityInteractionClient.getConnection(mService.mConnectionId);
      if (localIAccessibilityServiceConnection != null) {
        try
        {
          paramBoolean = localIAccessibilityServiceConnection.setMagnificationScaleAndCenter(NaN.0F, paramFloat1, paramFloat2, paramBoolean);
          return paramBoolean;
        }
        catch (RemoteException localRemoteException)
        {
          Log.w("AccessibilityService", "Failed to set center", localRemoteException);
          localRemoteException.rethrowFromSystemServer();
        }
      }
      return false;
    }
    
    public boolean setScale(float paramFloat, boolean paramBoolean)
    {
      AccessibilityInteractionClient.getInstance();
      IAccessibilityServiceConnection localIAccessibilityServiceConnection = AccessibilityInteractionClient.getConnection(mService.mConnectionId);
      if (localIAccessibilityServiceConnection != null) {
        try
        {
          paramBoolean = localIAccessibilityServiceConnection.setMagnificationScaleAndCenter(paramFloat, NaN.0F, NaN.0F, paramBoolean);
          return paramBoolean;
        }
        catch (RemoteException localRemoteException)
        {
          Log.w("AccessibilityService", "Failed to set scale", localRemoteException);
          localRemoteException.rethrowFromSystemServer();
        }
      }
      return false;
    }
    
    public static abstract interface OnMagnificationChangedListener
    {
      public abstract void onMagnificationChanged(AccessibilityService.MagnificationController paramMagnificationController, Region paramRegion, float paramFloat1, float paramFloat2, float paramFloat3);
    }
  }
  
  public static final class SoftKeyboardController
  {
    private ArrayMap<OnShowModeChangedListener, Handler> mListeners;
    private final Object mLock;
    private final AccessibilityService mService;
    
    SoftKeyboardController(AccessibilityService paramAccessibilityService, Object paramObject)
    {
      mService = paramAccessibilityService;
      mLock = paramObject;
    }
    
    private void setSoftKeyboardCallbackEnabled(boolean paramBoolean)
    {
      AccessibilityInteractionClient.getInstance();
      IAccessibilityServiceConnection localIAccessibilityServiceConnection = AccessibilityInteractionClient.getConnection(mService.mConnectionId);
      if (localIAccessibilityServiceConnection != null) {
        try
        {
          localIAccessibilityServiceConnection.setSoftKeyboardCallbackEnabled(paramBoolean);
        }
        catch (RemoteException localRemoteException)
        {
          throw new RuntimeException(localRemoteException);
        }
      }
    }
    
    public void addOnShowModeChangedListener(OnShowModeChangedListener paramOnShowModeChangedListener)
    {
      addOnShowModeChangedListener(paramOnShowModeChangedListener, null);
    }
    
    public void addOnShowModeChangedListener(OnShowModeChangedListener paramOnShowModeChangedListener, Handler paramHandler)
    {
      synchronized (mLock)
      {
        if (mListeners == null)
        {
          ArrayMap localArrayMap = new android/util/ArrayMap;
          localArrayMap.<init>();
          mListeners = localArrayMap;
        }
        boolean bool = mListeners.isEmpty();
        mListeners.put(paramOnShowModeChangedListener, paramHandler);
        if (bool) {
          setSoftKeyboardCallbackEnabled(true);
        }
        return;
      }
    }
    
    void dispatchSoftKeyboardShowModeChanged(final int paramInt)
    {
      synchronized (mLock)
      {
        if ((mListeners != null) && (!mListeners.isEmpty()))
        {
          ArrayMap localArrayMap = new android/util/ArrayMap;
          localArrayMap.<init>(mListeners);
          int i = 0;
          int j = localArrayMap.size();
          while (i < j)
          {
            final OnShowModeChangedListener localOnShowModeChangedListener = (OnShowModeChangedListener)localArrayMap.keyAt(i);
            ??? = (Handler)localArrayMap.valueAt(i);
            if (??? != null) {
              ((Handler)???).post(new Runnable()
              {
                public void run()
                {
                  localOnShowModeChangedListener.onShowModeChanged(AccessibilityService.SoftKeyboardController.this, paramInt);
                }
              });
            } else {
              localOnShowModeChangedListener.onShowModeChanged(this, paramInt);
            }
            i++;
          }
          return;
        }
        Slog.w("AccessibilityService", "Received soft keyboard show mode changed callback with no listeners registered!");
        setSoftKeyboardCallbackEnabled(false);
        return;
      }
    }
    
    public int getShowMode()
    {
      try
      {
        int i = Settings.Secure.getInt(mService.getContentResolver(), "accessibility_soft_keyboard_mode");
        return i;
      }
      catch (Settings.SettingNotFoundException localSettingNotFoundException)
      {
        Log.v("AccessibilityService", "Failed to obtain the soft keyboard mode", localSettingNotFoundException);
      }
      return 0;
    }
    
    void onServiceConnected()
    {
      synchronized (mLock)
      {
        if ((mListeners != null) && (!mListeners.isEmpty())) {
          setSoftKeyboardCallbackEnabled(true);
        }
        return;
      }
    }
    
    public boolean removeOnShowModeChangedListener(OnShowModeChangedListener paramOnShowModeChangedListener)
    {
      if (mListeners == null) {
        return false;
      }
      synchronized (mLock)
      {
        int i = mListeners.indexOfKey(paramOnShowModeChangedListener);
        boolean bool;
        if (i >= 0) {
          bool = true;
        } else {
          bool = false;
        }
        if (bool) {
          mListeners.removeAt(i);
        }
        if ((bool) && (mListeners.isEmpty())) {
          setSoftKeyboardCallbackEnabled(false);
        }
        return bool;
      }
    }
    
    public boolean setShowMode(int paramInt)
    {
      AccessibilityInteractionClient.getInstance();
      IAccessibilityServiceConnection localIAccessibilityServiceConnection = AccessibilityInteractionClient.getConnection(mService.mConnectionId);
      if (localIAccessibilityServiceConnection != null) {
        try
        {
          boolean bool = localIAccessibilityServiceConnection.setSoftKeyboardShowMode(paramInt);
          return bool;
        }
        catch (RemoteException localRemoteException)
        {
          Log.w("AccessibilityService", "Failed to set soft keyboard behavior", localRemoteException);
          localRemoteException.rethrowFromSystemServer();
        }
      }
      return false;
    }
    
    public static abstract interface OnShowModeChangedListener
    {
      public abstract void onShowModeChanged(AccessibilityService.SoftKeyboardController paramSoftKeyboardController, int paramInt);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SoftKeyboardShowMode {}
}
