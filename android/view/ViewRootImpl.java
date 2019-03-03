package android.view;

import android.animation.LayoutTransition;
import android.app.ActivityManager;
import android.app.IActivityManager;
import android.app.ResourcesManager;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.CompatibilityInfo;
import android.content.res.CompatibilityInfo.Translator;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.AnimatedVectorDrawable.VectorDrawableAnimatorRT;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManager.DisplayListener;
import android.hardware.input.InputManager;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.AndroidRuntimeException;
import android.util.BoostFramework;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LongArray;
import android.util.MergedConfiguration;
import android.util.Slog;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityManager.AccessibilityStateChangeListener;
import android.view.accessibility.AccessibilityManager.HighTextContrastChangeListener;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.accessibility.AccessibilityNodeProvider;
import android.view.accessibility.IAccessibilityInteractionConnection.Stub;
import android.view.accessibility.IAccessibilityInteractionConnectionCallback;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.autofill.AutofillManager;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodManager.FinishedInputEventCallback;
import android.widget.Scroller;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.IResultReceiver;
import com.android.internal.os.SomeArgs;
import com.android.internal.policy.PhoneFallbackEventHandler;
import com.android.internal.util.Preconditions;
import com.android.internal.view.BaseSurfaceHolder;
import com.android.internal.view.RootViewSurfaceTaker;
import com.android.internal.view.SurfaceCallbackHelper;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

public final class ViewRootImpl
  implements ViewParent, View.AttachInfo.Callbacks, ThreadedRenderer.DrawCallbacks
{
  private static final boolean DBG = false;
  private static final boolean DEBUG_CONFIGURATION = false;
  private static final boolean DEBUG_DIALOG = false;
  private static final boolean DEBUG_DRAW = false;
  private static final boolean DEBUG_FPS = false;
  private static final boolean DEBUG_IMF = false;
  private static final boolean DEBUG_INPUT_RESIZE = false;
  private static final boolean DEBUG_INPUT_STAGES = false;
  private static final boolean DEBUG_KEEP_SCREEN_ON = false;
  private static final boolean DEBUG_LAYOUT = false;
  private static final boolean DEBUG_ORIENTATION = false;
  private static final boolean DEBUG_TRACKBALL = false;
  private static final boolean LOCAL_LOGV = false;
  private static final int MAX_QUEUED_INPUT_EVENT_POOL_SIZE = 10;
  static final int MAX_TRACKBALL_DELAY = 250;
  private static final int MSG_CHECK_FOCUS = 13;
  private static final int MSG_CLEAR_ACCESSIBILITY_FOCUS_HOST = 21;
  private static final int MSG_CLOSE_SYSTEM_DIALOGS = 14;
  private static final int MSG_DIE = 3;
  private static final int MSG_DISPATCH_APP_VISIBILITY = 8;
  private static final int MSG_DISPATCH_DRAG_EVENT = 15;
  private static final int MSG_DISPATCH_DRAG_LOCATION_EVENT = 16;
  private static final int MSG_DISPATCH_GET_NEW_SURFACE = 9;
  private static final int MSG_DISPATCH_INPUT_EVENT = 7;
  private static final int MSG_DISPATCH_KEY_FROM_AUTOFILL = 12;
  private static final int MSG_DISPATCH_KEY_FROM_IME = 11;
  private static final int MSG_DISPATCH_NAV_COLOR = 1000;
  private static final int MSG_DISPATCH_SYSTEM_UI_VISIBILITY = 17;
  private static final int MSG_DISPATCH_WINDOW_SHOWN = 25;
  private static final int MSG_DRAW_FINISHED = 29;
  private static final int MSG_INVALIDATE = 1;
  private static final int MSG_INVALIDATE_RECT = 2;
  private static final int MSG_INVALIDATE_WORLD = 22;
  private static final int MSG_POINTER_CAPTURE_CHANGED = 28;
  private static final int MSG_PROCESS_INPUT_EVENTS = 19;
  private static final int MSG_REQUEST_KEYBOARD_SHORTCUTS = 26;
  private static final int MSG_RESIZED = 4;
  private static final int MSG_RESIZED_REPORT = 5;
  private static final int MSG_SYNTHESIZE_INPUT_EVENT = 24;
  private static final int MSG_UPDATE_CONFIGURATION = 18;
  private static final int MSG_UPDATE_POINTER_ICON = 27;
  private static final int MSG_WINDOW_FOCUS_CHANGED = 6;
  private static final int MSG_WINDOW_MOVED = 23;
  private static final boolean MT_RENDERER_AVAILABLE = true;
  public static final String PROPERTY_EMULATOR_WIN_OUTSET_BOTTOM_PX = "ro.emu.win_outset_bottom_px";
  private static final String PROPERTY_PROFILE_RENDERING = "viewroot.profile_rendering";
  private static final String TAG = "ViewRootImpl";
  static final Interpolator mResizeInterpolator = new AccelerateDecelerateInterpolator();
  private static boolean sAlwaysAssignFocus;
  private static boolean sCompatibilityDone;
  private static final ArrayList<ConfigChangedCallback> sConfigCallbacks;
  static boolean sFirstDrawComplete;
  static final ArrayList<Runnable> sFirstDrawHandlers;
  static final ThreadLocal<HandlerActionQueue> sRunQueues = new ThreadLocal();
  private final boolean SCROLL_BOOST_SS_ENABLE;
  View mAccessibilityFocusedHost;
  AccessibilityNodeInfo mAccessibilityFocusedVirtualView;
  final AccessibilityInteractionConnectionManager mAccessibilityInteractionConnectionManager;
  AccessibilityInteractionController mAccessibilityInteractionController;
  final AccessibilityManager mAccessibilityManager;
  private ActivityConfigCallback mActivityConfigCallback;
  private boolean mActivityRelaunched;
  boolean mAdded;
  boolean mAddedTouchMode;
  private boolean mAppVisibilityChanged;
  boolean mAppVisible;
  boolean mApplyInsetsRequested;
  final View.AttachInfo mAttachInfo;
  AudioManager mAudioManager;
  final String mBasePackageName;
  private int mCanvasOffsetX;
  private int mCanvasOffsetY;
  Choreographer mChoreographer;
  int mClientWindowLayoutFlags;
  final ConsumeBatchedInputImmediatelyRunnable mConsumeBatchedInputImmediatelyRunnable;
  boolean mConsumeBatchedInputImmediatelyScheduled;
  boolean mConsumeBatchedInputScheduled;
  final ConsumeBatchedInputRunnable mConsumedBatchedInputRunnable;
  final Context mContext;
  int mCurScrollY;
  View mCurrentDragView;
  private PointerIcon mCustomPointerIcon;
  private final int mDensity;
  Rect mDirty;
  final Rect mDispatchContentInsets;
  DisplayCutout mDispatchDisplayCutout;
  final Rect mDispatchStableInsets;
  Display mDisplay;
  private final DisplayManager.DisplayListener mDisplayListener;
  final DisplayManager mDisplayManager;
  ClipDescription mDragDescription;
  final PointF mDragPoint;
  private boolean mDragResizing;
  boolean mDrawingAllowed;
  int mDrawsNeededToReport;
  FallbackEventHandler mFallbackEventHandler;
  boolean mFirst;
  InputStage mFirstInputStage;
  InputStage mFirstPostImeInputStage;
  private boolean mForceDecorViewVisibility;
  private boolean mForceNextConfigUpdate;
  boolean mForceNextWindowRelayout;
  private int mFpsNumFrames;
  private long mFpsPrevTime;
  private long mFpsStartTime;
  boolean mFullRedrawNeeded;
  boolean mHadWindowFocus;
  final ViewRootHandler mHandler;
  boolean mHandlingLayoutInLayoutRequest;
  int mHardwareXOffset;
  int mHardwareYOffset;
  boolean mHasHadWindowFocus;
  boolean mHaveMoveEvent;
  int mHeight;
  final HighContrastTextManager mHighContrastTextManager;
  private boolean mInLayout;
  InputChannel mInputChannel;
  protected final InputEventConsistencyVerifier mInputEventConsistencyVerifier;
  WindowInputEventReceiver mInputEventReceiver;
  InputQueue mInputQueue;
  InputQueue.Callback mInputQueueCallback;
  final InvalidateOnAnimationRunnable mInvalidateOnAnimationRunnable;
  private boolean mInvalidateRootRequested;
  boolean mIsAmbientMode;
  public boolean mIsAnimating;
  boolean mIsCreating;
  boolean mIsDrawing;
  boolean mIsInTraversal;
  boolean mIsPerfLockAcquired;
  private final Configuration mLastConfigurationFromResources;
  final ViewTreeObserver.InternalInsetsInfo mLastGivenInsets;
  boolean mLastInCompatMode;
  boolean mLastOverscanRequested;
  private final MergedConfiguration mLastReportedMergedConfiguration;
  WeakReference<View> mLastScrolledFocus;
  int mLastSystemUiVisibility;
  final PointF mLastTouchPoint;
  int mLastTouchSource;
  boolean mLastWasImTarget;
  private WindowInsets mLastWindowInsets;
  boolean mLayoutRequested;
  ArrayList<View> mLayoutRequesters;
  volatile Object mLocalDragState;
  final WindowLeaked mLocation;
  boolean mLostWindowFocus;
  private boolean mNeedsRendererSetup;
  boolean mNewSurfaceNeeded;
  private ThreadedRenderer.FrameDrawingCallback mNextRtFrameCallback;
  private final int mNoncompatDensity;
  int mOrigWindowType;
  boolean mPausedForTransition;
  boolean mPendingAlwaysConsumeNavBar;
  final Rect mPendingBackDropFrame;
  final Rect mPendingContentInsets;
  final DisplayCutout.ParcelableWrapper mPendingDisplayCutout;
  int mPendingInputEventCount;
  QueuedInputEvent mPendingInputEventHead;
  String mPendingInputEventQueueLengthCounterName;
  QueuedInputEvent mPendingInputEventTail;
  private final MergedConfiguration mPendingMergedConfiguration;
  final Rect mPendingOutsets;
  final Rect mPendingOverscanInsets;
  final Rect mPendingStableInsets;
  private ArrayList<LayoutTransition> mPendingTransitions;
  final Rect mPendingVisibleInsets;
  BoostFramework mPerf;
  boolean mPointerCapture;
  private int mPointerIconType;
  final Region mPreviousTransparentRegion;
  boolean mProcessInputEventsScheduled;
  private boolean mProfile;
  private boolean mProfileRendering;
  private QueuedInputEvent mQueuedInputEventPool;
  private int mQueuedInputEventPoolSize;
  private boolean mRemoved;
  private Choreographer.FrameCallback mRenderProfiler;
  private boolean mRenderProfilingEnabled;
  boolean mReportNextDraw;
  private int mResizeMode;
  boolean mScrollMayChange;
  int mScrollY;
  Scroller mScroller;
  SendWindowContentChangedAccessibilityEvent mSendWindowContentChangedAccessibilityEvent;
  int mSeq;
  int mSoftInputMode;
  boolean mStopped;
  public final Surface mSurface;
  BaseSurfaceHolder mSurfaceHolder;
  SurfaceHolder.Callback2 mSurfaceHolderCallback;
  InputStage mSyntheticInputStage;
  private String mTag;
  final int mTargetSdkVersion;
  HashSet<View> mTempHashSet;
  final Rect mTempRect;
  final Thread mThread;
  final int[] mTmpLocation;
  final TypedValue mTmpValue;
  CompatibilityInfo.Translator mTranslator;
  final Region mTransparentRegion;
  int mTraversalBarrier;
  final TraversalRunnable mTraversalRunnable;
  public boolean mTraversalScheduled;
  boolean mUnbufferedInputDispatch;
  private final UnhandledKeyManager mUnhandledKeyManager;
  @GuardedBy("this")
  boolean mUpcomingInTouchMode;
  @GuardedBy("this")
  boolean mUpcomingWindowFocus;
  private boolean mUseMTRenderer;
  View mView;
  final ViewConfiguration mViewConfiguration;
  private int mViewLayoutDirectionInitial;
  int mViewVisibility;
  final Rect mVisRect;
  int mWidth;
  boolean mWillDrawSoon;
  final Rect mWinFrame;
  final W mWindow;
  public final WindowManager.LayoutParams mWindowAttributes;
  boolean mWindowAttributesChanged;
  int mWindowAttributesChangesFlag;
  @GuardedBy("mWindowCallbacks")
  final ArrayList<WindowCallbacks> mWindowCallbacks;
  CountDownLatch mWindowDrawCountDown;
  @GuardedBy("this")
  boolean mWindowFocusChanged;
  final IWindowSession mWindowSession;
  private final ArrayList<WindowStoppedCallback> mWindowStoppedCallbacks;
  
  static
  {
    sFirstDrawHandlers = new ArrayList();
    sFirstDrawComplete = false;
    sConfigCallbacks = new ArrayList();
    sCompatibilityDone = false;
  }
  
  public ViewRootImpl(Context paramContext, Display paramDisplay)
  {
    boolean bool = false;
    SCROLL_BOOST_SS_ENABLE = SystemProperties.getBoolean("vendor.perf.gestureflingboost.enable", false);
    mWindowCallbacks = new ArrayList();
    mTmpLocation = new int[2];
    mTmpValue = new TypedValue();
    mWindowAttributes = new WindowManager.LayoutParams();
    mAppVisible = true;
    mForceDecorViewVisibility = false;
    mOrigWindowType = -1;
    mStopped = false;
    mIsAmbientMode = false;
    mPausedForTransition = false;
    mLastInCompatMode = false;
    mPendingInputEventQueueLengthCounterName = "pq";
    mUnhandledKeyManager = new UnhandledKeyManager(null);
    mWindowAttributesChanged = false;
    mWindowAttributesChangesFlag = 0;
    mSurface = new Surface();
    mPendingOverscanInsets = new Rect();
    mPendingVisibleInsets = new Rect();
    mPendingStableInsets = new Rect();
    mPendingContentInsets = new Rect();
    mPendingOutsets = new Rect();
    mPendingBackDropFrame = new Rect();
    mPendingDisplayCutout = new DisplayCutout.ParcelableWrapper(DisplayCutout.NO_CUTOUT);
    mLastGivenInsets = new ViewTreeObserver.InternalInsetsInfo();
    mDispatchContentInsets = new Rect();
    mDispatchStableInsets = new Rect();
    mDispatchDisplayCutout = DisplayCutout.NO_CUTOUT;
    mLastConfigurationFromResources = new Configuration();
    mLastReportedMergedConfiguration = new MergedConfiguration();
    mPendingMergedConfiguration = new MergedConfiguration();
    mDragPoint = new PointF();
    mLastTouchPoint = new PointF();
    mFpsStartTime = -1L;
    mFpsPrevTime = -1L;
    mPointerIconType = 1;
    mCustomPointerIcon = null;
    mAccessibilityInteractionConnectionManager = new AccessibilityInteractionConnectionManager();
    mInLayout = false;
    mLayoutRequesters = new ArrayList();
    mHandlingLayoutInLayoutRequest = false;
    InputEventConsistencyVerifier localInputEventConsistencyVerifier;
    if (InputEventConsistencyVerifier.isInstrumentationEnabled()) {
      localInputEventConsistencyVerifier = new InputEventConsistencyVerifier(this, 0);
    } else {
      localInputEventConsistencyVerifier = null;
    }
    mInputEventConsistencyVerifier = localInputEventConsistencyVerifier;
    mTag = "ViewRootImpl";
    mHaveMoveEvent = false;
    mIsPerfLockAcquired = false;
    mPerf = null;
    mProfile = false;
    mDisplayListener = new DisplayManager.DisplayListener()
    {
      private int toViewScreenState(int paramAnonymousInt)
      {
        int i = 1;
        if (paramAnonymousInt == 1) {
          i = 0;
        }
        return i;
      }
      
      public void onDisplayAdded(int paramAnonymousInt) {}
      
      public void onDisplayChanged(int paramAnonymousInt)
      {
        if ((mView != null) && (mDisplay.getDisplayId() == paramAnonymousInt))
        {
          int i = mAttachInfo.mDisplayState;
          int j = mDisplay.getState();
          if (i != j)
          {
            mAttachInfo.mDisplayState = j;
            pokeDrawLockIfNeeded();
            if (i != 0)
            {
              paramAnonymousInt = toViewScreenState(i);
              j = toViewScreenState(j);
              if (paramAnonymousInt != j) {
                mView.dispatchScreenStateChanged(j);
              }
              if (i == 1)
              {
                mFullRedrawNeeded = true;
                scheduleTraversals();
              }
            }
          }
        }
      }
      
      public void onDisplayRemoved(int paramAnonymousInt) {}
    };
    mWindowStoppedCallbacks = new ArrayList();
    mDrawsNeededToReport = 0;
    mHandler = new ViewRootHandler();
    mTraversalRunnable = new TraversalRunnable();
    mConsumedBatchedInputRunnable = new ConsumeBatchedInputRunnable();
    mConsumeBatchedInputImmediatelyRunnable = new ConsumeBatchedInputImmediatelyRunnable();
    mInvalidateOnAnimationRunnable = new InvalidateOnAnimationRunnable();
    mContext = paramContext;
    mWindowSession = WindowManagerGlobal.getWindowSession();
    mDisplay = paramDisplay;
    mBasePackageName = paramContext.getBasePackageName();
    mThread = Thread.currentThread();
    mLocation = new WindowLeaked(null);
    mLocation.fillInStackTrace();
    mWidth = -1;
    mHeight = -1;
    mDirty = new Rect();
    mTempRect = new Rect();
    mVisRect = new Rect();
    mWinFrame = new Rect();
    mWindow = new W(this);
    mTargetSdkVersion = getApplicationInfotargetSdkVersion;
    mViewVisibility = 8;
    mTransparentRegion = new Region();
    mPreviousTransparentRegion = new Region();
    mFirst = true;
    mAdded = false;
    mAttachInfo = new View.AttachInfo(mWindowSession, mWindow, paramDisplay, this, mHandler, this, paramContext);
    mAccessibilityManager = AccessibilityManager.getInstance(paramContext);
    mAccessibilityManager.addAccessibilityStateChangeListener(mAccessibilityInteractionConnectionManager, mHandler);
    mHighContrastTextManager = new HighContrastTextManager();
    mAccessibilityManager.addHighTextContrastStateChangeListener(mHighContrastTextManager, mHandler);
    mViewConfiguration = ViewConfiguration.get(paramContext);
    mDensity = getResourcesgetDisplayMetricsdensityDpi;
    mNoncompatDensity = getResourcesgetDisplayMetricsnoncompatDensityDpi;
    mFallbackEventHandler = new PhoneFallbackEventHandler(paramContext);
    mChoreographer = Choreographer.getInstance();
    mDisplayManager = ((DisplayManager)paramContext.getSystemService("display"));
    if (!sCompatibilityDone)
    {
      if (mTargetSdkVersion < 28) {
        bool = true;
      }
      sAlwaysAssignFocus = bool;
      sCompatibilityDone = true;
    }
    loadSystemProperties();
    mPerf = new BoostFramework(paramContext);
  }
  
  public static void addConfigCallback(ConfigChangedCallback paramConfigChangedCallback)
  {
    synchronized (sConfigCallbacks)
    {
      sConfigCallbacks.add(paramConfigChangedCallback);
      return;
    }
  }
  
  public static void addFirstDrawHandler(Runnable paramRunnable)
  {
    synchronized (sFirstDrawHandlers)
    {
      if (!sFirstDrawComplete) {
        sFirstDrawHandlers.add(paramRunnable);
      }
      return;
    }
  }
  
  private void adjustInputEventForCompatibility(InputEvent paramInputEvent)
  {
    if ((mTargetSdkVersion < 23) && ((paramInputEvent instanceof MotionEvent)))
    {
      paramInputEvent = (MotionEvent)paramInputEvent;
      int i = paramInputEvent.getButtonState();
      int j = (i & 0x60) >> 4;
      if (j != 0) {
        paramInputEvent.setButtonState(i | j);
      }
    }
  }
  
  private void applyKeepScreenOnFlag(WindowManager.LayoutParams paramLayoutParams)
  {
    if (mAttachInfo.mKeepScreenOn) {
      flags |= 0x80;
    } else {
      flags = (flags & 0xFF7F | mClientWindowLayoutFlags & 0x80);
    }
  }
  
  private boolean checkForLeavingTouchModeAndConsume(KeyEvent paramKeyEvent)
  {
    if (!mAttachInfo.mInTouchMode) {
      return false;
    }
    int i = paramKeyEvent.getAction();
    if ((i != 0) && (i != 2)) {
      return false;
    }
    if ((paramKeyEvent.getFlags() & 0x4) != 0) {
      return false;
    }
    if (isNavigationKey(paramKeyEvent)) {
      return ensureTouchMode(false);
    }
    if (isTypingKey(paramKeyEvent))
    {
      ensureTouchMode(false);
      return false;
    }
    return false;
  }
  
  private boolean collectViewAttributes()
  {
    if (mAttachInfo.mRecomputeGlobalAttributes)
    {
      mAttachInfo.mRecomputeGlobalAttributes = false;
      boolean bool = mAttachInfo.mKeepScreenOn;
      mAttachInfo.mKeepScreenOn = false;
      mAttachInfo.mSystemUiVisibility = 0;
      mAttachInfo.mHasSystemUiListeners = false;
      mView.dispatchCollectViewAttributes(mAttachInfo, 0);
      Object localObject = mAttachInfo;
      mSystemUiVisibility &= mAttachInfo.mDisabledSystemUiVisibility;
      localObject = mWindowAttributes;
      View.AttachInfo localAttachInfo = mAttachInfo;
      mSystemUiVisibility |= getImpliedSystemUiVisibility((WindowManager.LayoutParams)localObject);
      if ((mAttachInfo.mKeepScreenOn != bool) || (mAttachInfo.mSystemUiVisibility != subtreeSystemUiVisibility) || (mAttachInfo.mHasSystemUiListeners != hasSystemUiListeners))
      {
        applyKeepScreenOnFlag((WindowManager.LayoutParams)localObject);
        subtreeSystemUiVisibility = mAttachInfo.mSystemUiVisibility;
        hasSystemUiListeners = mAttachInfo.mHasSystemUiListeners;
        mView.dispatchWindowSystemUiVisiblityChanged(mAttachInfo.mSystemUiVisibility);
        return true;
      }
    }
    return false;
  }
  
  private void deliverInputEvent(QueuedInputEvent paramQueuedInputEvent)
  {
    Trace.asyncTraceBegin(8L, "deliverInputEvent", mEvent.getSequenceNumber());
    if (mInputEventConsistencyVerifier != null) {
      mInputEventConsistencyVerifier.onInputEvent(mEvent, 0);
    }
    InputStage localInputStage;
    if (paramQueuedInputEvent.shouldSendToSynthesizer()) {
      localInputStage = mSyntheticInputStage;
    } else if (paramQueuedInputEvent.shouldSkipIme()) {
      localInputStage = mFirstPostImeInputStage;
    } else {
      localInputStage = mFirstInputStage;
    }
    if ((mEvent instanceof KeyEvent)) {
      mUnhandledKeyManager.preDispatch((KeyEvent)mEvent);
    }
    if (localInputStage != null)
    {
      handleWindowFocusChanged();
      localInputStage.deliver(paramQueuedInputEvent);
    }
    else
    {
      finishInputEvent(paramQueuedInputEvent);
    }
  }
  
  private void destroyHardwareRenderer()
  {
    ThreadedRenderer localThreadedRenderer = mAttachInfo.mThreadedRenderer;
    if (localThreadedRenderer != null)
    {
      if (mView != null) {
        localThreadedRenderer.destroyHardwareResources(mView);
      }
      localThreadedRenderer.destroy();
      localThreadedRenderer.setRequested(false);
      mAttachInfo.mThreadedRenderer = null;
      mAttachInfo.mHardwareAccelerated = false;
    }
  }
  
  private int dipToPx(int paramInt)
  {
    return (int)(mContext.getResources().getDisplayMetrics().density * paramInt + 0.5F);
  }
  
  private void dispatchResized(Rect paramRect1, Rect paramRect2, Rect paramRect3, Rect paramRect4, Rect paramRect5, Rect paramRect6, boolean paramBoolean1, MergedConfiguration paramMergedConfiguration, Rect paramRect7, boolean paramBoolean2, boolean paramBoolean3, int paramInt, DisplayCutout.ParcelableWrapper paramParcelableWrapper)
  {
    boolean bool = mDragResizing;
    int i = 1;
    int j;
    if ((bool) && (mUseMTRenderer))
    {
      bool = paramRect1.equals(paramRect7);
      synchronized (mWindowCallbacks)
      {
        for (j = mWindowCallbacks.size() - 1; j >= 0; j--) {
          ((WindowCallbacks)mWindowCallbacks.get(j)).onWindowSizeIsChanging(paramRect7, bool, paramRect4, paramRect5);
        }
      }
    }
    ??? = mHandler;
    if (paramBoolean1) {
      j = 5;
    } else {
      j = 4;
    }
    Message localMessage = ((ViewRootHandler)???).obtainMessage(j);
    if (mTranslator != null)
    {
      mTranslator.translateRectInScreenToAppWindow(paramRect1);
      mTranslator.translateRectInScreenToAppWindow(paramRect2);
      mTranslator.translateRectInScreenToAppWindow(paramRect3);
      mTranslator.translateRectInScreenToAppWindow(paramRect4);
    }
    ??? = SomeArgs.obtain();
    if (Binder.getCallingPid() == Process.myPid()) {
      j = i;
    } else {
      j = 0;
    }
    if (j != 0) {
      paramRect1 = new Rect(paramRect1);
    }
    arg1 = paramRect1;
    if (j != 0) {
      paramRect1 = new Rect(paramRect3);
    } else {
      paramRect1 = paramRect3;
    }
    arg2 = paramRect1;
    if (j != 0) {
      paramRect1 = new Rect(paramRect4);
    } else {
      paramRect1 = paramRect4;
    }
    arg3 = paramRect1;
    if ((j != 0) && (paramMergedConfiguration != null)) {
      paramMergedConfiguration = new MergedConfiguration(paramMergedConfiguration);
    }
    arg4 = paramMergedConfiguration;
    if (j != 0) {
      paramRect1 = new Rect(paramRect2);
    } else {
      paramRect1 = paramRect2;
    }
    arg5 = paramRect1;
    if (j != 0) {
      paramRect5 = new Rect(paramRect5);
    }
    arg6 = paramRect5;
    if (j != 0) {
      paramRect1 = new Rect(paramRect6);
    } else {
      paramRect1 = paramRect6;
    }
    arg7 = paramRect1;
    if (j != 0) {
      paramRect7 = new Rect(paramRect7);
    }
    arg8 = paramRect7;
    arg9 = paramParcelableWrapper.get();
    argi1 = paramBoolean2;
    argi2 = paramBoolean3;
    argi3 = paramInt;
    obj = ???;
    mHandler.sendMessage(localMessage);
  }
  
  private boolean draw(boolean paramBoolean)
  {
    Surface localSurface = mSurface;
    if (!localSurface.isValid()) {
      return false;
    }
    int i;
    if (!sFirstDrawComplete) {
      synchronized (sFirstDrawHandlers)
      {
        sFirstDrawComplete = true;
        i = sFirstDrawHandlers.size();
        for (j = 0; j < i; j++) {
          mHandler.post((Runnable)sFirstDrawHandlers.get(j));
        }
      }
    }
    scrollToRectOrFocus(null, false);
    if (mAttachInfo.mViewScrollChanged)
    {
      if ((!SCROLL_BOOST_SS_ENABLE) && (mHaveMoveEvent) && (!mIsPerfLockAcquired))
      {
        mIsPerfLockAcquired = true;
        if (mPerf != null)
        {
          ??? = mContext.getPackageName();
          mPerf.perfHint(4224, (String)???, -1, 4);
        }
      }
      mAttachInfo.mViewScrollChanged = false;
      mAttachInfo.mTreeObserver.dispatchOnScrollChanged();
    }
    if ((mScroller != null) && (mScroller.computeScrollOffset())) {
      i = 1;
    } else {
      i = 0;
    }
    if (i != 0) {
      j = mScroller.getCurrY();
    } else {
      j = mScrollY;
    }
    if (mCurScrollY != j)
    {
      mCurScrollY = j;
      if ((mView instanceof RootViewSurfaceTaker)) {
        ((RootViewSurfaceTaker)mView).onRootViewScrollYChanged(mCurScrollY);
      }
      paramBoolean = true;
    }
    float f = mAttachInfo.mApplicationScale;
    boolean bool = mAttachInfo.mScalingRequired;
    Object localObject3 = mDirty;
    if (mSurfaceHolder != null)
    {
      ((Rect)localObject3).setEmpty();
      if ((i != 0) && (mScroller != null)) {
        mScroller.abortAnimation();
      }
      return false;
    }
    if (paramBoolean)
    {
      mAttachInfo.mIgnoreDirtyState = true;
      ((Rect)localObject3).set(0, 0, (int)(mWidth * f + 0.5F), (int)(mHeight * f + 0.5F));
    }
    mAttachInfo.mTreeObserver.dispatchOnDraw();
    int k = -mCanvasOffsetX;
    int j = -mCanvasOffsetY + j;
    ??? = mWindowAttributes;
    if (??? != null) {
      ??? = surfaceInsets;
    } else {
      ??? = null;
    }
    int m = k;
    int n = j;
    if (??? != null)
    {
      m = k - left;
      n = j - top;
      ((Rect)localObject3).offset(left, right);
    }
    k = 0;
    Drawable localDrawable = mAttachInfo.mAccessibilityFocusDrawable;
    j = k;
    if (localDrawable != null)
    {
      Rect localRect = mAttachInfo.mTmpInvalRect;
      if (!getAccessibilityFocusedRect(localRect)) {
        localRect.setEmpty();
      }
      j = k;
      if (!localRect.equals(localDrawable.getBounds())) {
        j = 1;
      }
    }
    mAttachInfo.mDrawingTime = (mChoreographer.getFrameTimeNanos() / 1000000L);
    paramBoolean = false;
    if ((((Rect)localObject3).isEmpty()) && (!mIsAnimating) && (j == 0)) {
      break label882;
    }
    if ((mAttachInfo.mThreadedRenderer != null) && (mAttachInfo.mThreadedRenderer.isEnabled()))
    {
      if ((j == 0) && (!mInvalidateRootRequested)) {
        j = 0;
      } else {
        j = 1;
      }
      mInvalidateRootRequested = false;
      mIsAnimating = false;
      if ((mHardwareYOffset != n) || (mHardwareXOffset != m))
      {
        mHardwareYOffset = n;
        mHardwareXOffset = m;
        j = 1;
      }
      if (j != 0) {
        mAttachInfo.mThreadedRenderer.invalidateRoot();
      }
      ((Rect)localObject3).setEmpty();
      paramBoolean = updateContentDrawBounds();
      if (mReportNextDraw) {
        mAttachInfo.mThreadedRenderer.setStopped(false);
      }
      if (paramBoolean) {
        requestDrawWindow();
      }
      paramBoolean = true;
      ??? = mNextRtFrameCallback;
      mNextRtFrameCallback = null;
      mAttachInfo.mThreadedRenderer.draw(mView, mAttachInfo, this, (ThreadedRenderer.FrameDrawingCallback)???);
    }
    else
    {
      View.AttachInfo localAttachInfo;
      if ((mAttachInfo.mThreadedRenderer != null) && (!mAttachInfo.mThreadedRenderer.isEnabled()) && (mAttachInfo.mThreadedRenderer.isRequested()) && (mSurface.isValid()))
      {
        try
        {
          localObject3 = mAttachInfo.mThreadedRenderer;
          i = mWidth;
          j = mHeight;
          localAttachInfo = mAttachInfo;
          try
          {
            ((ThreadedRenderer)localObject3).initializeIfNeeded(i, j, localAttachInfo, mSurface, (Rect)???);
            mFullRedrawNeeded = true;
            scheduleTraversals();
            return false;
          }
          catch (Surface.OutOfResourcesException localOutOfResourcesException1) {}
          handleOutOfResourcesException(localOutOfResourcesException2);
        }
        catch (Surface.OutOfResourcesException localOutOfResourcesException2) {}
        return false;
      }
      if (!drawSoftware(localAttachInfo, mAttachInfo, m, n, bool, (Rect)localObject3, localOutOfResourcesException2)) {
        return false;
      }
    }
    label882:
    if (i != 0)
    {
      mFullRedrawNeeded = true;
      scheduleTraversals();
    }
    return paramBoolean;
  }
  
  private void drawAccessibilityFocusedDrawableIfNeeded(Canvas paramCanvas)
  {
    Rect localRect = mAttachInfo.mTmpInvalRect;
    if (getAccessibilityFocusedRect(localRect))
    {
      Drawable localDrawable = getAccessibilityFocusedDrawable();
      if (localDrawable != null)
      {
        localDrawable.setBounds(localRect);
        localDrawable.draw(paramCanvas);
      }
    }
    else if (mAttachInfo.mAccessibilityFocusDrawable != null)
    {
      mAttachInfo.mAccessibilityFocusDrawable.setBounds(0, 0, 0, 0);
    }
  }
  
  /* Error */
  private boolean drawSoftware(Surface paramSurface, View.AttachInfo paramAttachInfo, int paramInt1, int paramInt2, boolean paramBoolean, Rect paramRect1, Rect paramRect2)
  {
    // Byte code:
    //   0: iload_3
    //   1: istore 8
    //   3: iload 4
    //   5: istore 9
    //   7: iload 8
    //   9: istore 10
    //   11: iload 9
    //   13: istore 11
    //   15: aload 7
    //   17: ifnull +23 -> 40
    //   20: iload 8
    //   22: aload 7
    //   24: getfield 1298	android/graphics/Rect:left	I
    //   27: iadd
    //   28: istore 10
    //   30: iload 9
    //   32: aload 7
    //   34: getfield 1301	android/graphics/Rect:top	I
    //   37: iadd
    //   38: istore 11
    //   40: iload 10
    //   42: ineg
    //   43: istore 9
    //   45: iload 11
    //   47: ineg
    //   48: istore 8
    //   50: aload 6
    //   52: iload 9
    //   54: iload 8
    //   56: invokevirtual 1308	android/graphics/Rect:offset	(II)V
    //   59: aload 6
    //   61: getfield 1298	android/graphics/Rect:left	I
    //   64: istore 12
    //   66: aload 6
    //   68: getfield 1301	android/graphics/Rect:top	I
    //   71: istore 13
    //   73: aload 6
    //   75: getfield 1304	android/graphics/Rect:right	I
    //   78: istore 9
    //   80: aload 6
    //   82: getfield 1405	android/graphics/Rect:bottom	I
    //   85: istore 8
    //   87: aload_0
    //   88: getfield 546	android/view/ViewRootImpl:mSurface	Landroid/view/Surface;
    //   91: aload 6
    //   93: invokevirtual 1409	android/view/Surface:lockCanvas	(Landroid/graphics/Rect;)Landroid/graphics/Canvas;
    //   96: astore 7
    //   98: iload 12
    //   100: aload 6
    //   102: getfield 1298	android/graphics/Rect:left	I
    //   105: if_icmpne +33 -> 138
    //   108: iload 13
    //   110: aload 6
    //   112: getfield 1301	android/graphics/Rect:top	I
    //   115: if_icmpne +23 -> 138
    //   118: iload 9
    //   120: aload 6
    //   122: getfield 1304	android/graphics/Rect:right	I
    //   125: if_icmpne +13 -> 138
    //   128: iload 8
    //   130: aload 6
    //   132: getfield 1405	android/graphics/Rect:bottom	I
    //   135: if_icmpeq +8 -> 143
    //   138: aload_2
    //   139: iconst_1
    //   140: putfield 1281	android/view/View$AttachInfo:mIgnoreDirtyState	Z
    //   143: aload 7
    //   145: aload_0
    //   146: getfield 796	android/view/ViewRootImpl:mDensity	I
    //   149: invokevirtual 1414	android/graphics/Canvas:setDensity	(I)V
    //   152: aload 6
    //   154: iload 10
    //   156: iload 11
    //   158: invokevirtual 1308	android/graphics/Rect:offset	(II)V
    //   161: aload 7
    //   163: invokevirtual 1417	android/graphics/Canvas:isOpaque	()Z
    //   166: ifeq +18 -> 184
    //   169: iload 4
    //   171: ifne +13 -> 184
    //   174: iload_3
    //   175: ifeq +6 -> 181
    //   178: goto +6 -> 184
    //   181: goto +12 -> 193
    //   184: aload 7
    //   186: iconst_0
    //   187: getstatic 1423	android/graphics/PorterDuff$Mode:CLEAR	Landroid/graphics/PorterDuff$Mode;
    //   190: invokevirtual 1427	android/graphics/Canvas:drawColor	(ILandroid/graphics/PorterDuff$Mode;)V
    //   193: aload 6
    //   195: invokevirtual 1275	android/graphics/Rect:setEmpty	()V
    //   198: aload_0
    //   199: iconst_0
    //   200: putfield 1339	android/view/ViewRootImpl:mIsAnimating	Z
    //   203: aload_0
    //   204: getfield 1019	android/view/ViewRootImpl:mView	Landroid/view/View;
    //   207: astore 6
    //   209: aload 6
    //   211: aload 6
    //   213: getfield 1430	android/view/View:mPrivateFlags	I
    //   216: bipush 32
    //   218: ior
    //   219: putfield 1430	android/view/View:mPrivateFlags	I
    //   222: iload_3
    //   223: ineg
    //   224: i2f
    //   225: fstore 14
    //   227: iload 4
    //   229: ineg
    //   230: i2f
    //   231: fstore 15
    //   233: aload 7
    //   235: fload 14
    //   237: fload 15
    //   239: invokevirtual 1434	android/graphics/Canvas:translate	(FF)V
    //   242: aload_0
    //   243: getfield 1136	android/view/ViewRootImpl:mTranslator	Landroid/content/res/CompatibilityInfo$Translator;
    //   246: ifnull +12 -> 258
    //   249: aload_0
    //   250: getfield 1136	android/view/ViewRootImpl:mTranslator	Landroid/content/res/CompatibilityInfo$Translator;
    //   253: aload 7
    //   255: invokevirtual 1437	android/content/res/CompatibilityInfo$Translator:translateCanvas	(Landroid/graphics/Canvas;)V
    //   258: iload 5
    //   260: ifeq +11 -> 271
    //   263: aload_0
    //   264: getfield 801	android/view/ViewRootImpl:mNoncompatDensity	I
    //   267: istore_3
    //   268: goto +5 -> 273
    //   271: iconst_0
    //   272: istore_3
    //   273: aload 7
    //   275: iload_3
    //   276: invokevirtual 1440	android/graphics/Canvas:setScreenDensity	(I)V
    //   279: aload_2
    //   280: iconst_0
    //   281: putfield 1443	android/view/View$AttachInfo:mSetIgnoreDirtyState	Z
    //   284: aload_0
    //   285: getfield 1019	android/view/ViewRootImpl:mView	Landroid/view/View;
    //   288: aload 7
    //   290: invokevirtual 1444	android/view/View:draw	(Landroid/graphics/Canvas;)V
    //   293: aload_0
    //   294: aload 7
    //   296: invokespecial 1446	android/view/ViewRootImpl:drawAccessibilityFocusedDrawableIfNeeded	(Landroid/graphics/Canvas;)V
    //   299: aload_2
    //   300: getfield 1443	android/view/View$AttachInfo:mSetIgnoreDirtyState	Z
    //   303: ifne +8 -> 311
    //   306: aload_2
    //   307: iconst_0
    //   308: putfield 1281	android/view/View$AttachInfo:mIgnoreDirtyState	Z
    //   311: aload_1
    //   312: aload 7
    //   314: invokevirtual 1449	android/view/Surface:unlockCanvasAndPost	(Landroid/graphics/Canvas;)V
    //   317: iconst_1
    //   318: ireturn
    //   319: astore_1
    //   320: aload_0
    //   321: getfield 637	android/view/ViewRootImpl:mTag	Ljava/lang/String;
    //   324: ldc_w 1451
    //   327: aload_1
    //   328: invokestatic 1457	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   331: pop
    //   332: aload_0
    //   333: iconst_1
    //   334: putfield 1459	android/view/ViewRootImpl:mLayoutRequested	Z
    //   337: iconst_0
    //   338: ireturn
    //   339: astore 6
    //   341: aload_2
    //   342: getfield 1443	android/view/View$AttachInfo:mSetIgnoreDirtyState	Z
    //   345: ifne +8 -> 353
    //   348: aload_2
    //   349: iconst_0
    //   350: putfield 1281	android/view/View$AttachInfo:mIgnoreDirtyState	Z
    //   353: aload 6
    //   355: athrow
    //   356: astore_2
    //   357: aload_1
    //   358: aload 7
    //   360: invokevirtual 1449	android/view/Surface:unlockCanvasAndPost	(Landroid/graphics/Canvas;)V
    //   363: aload_2
    //   364: athrow
    //   365: astore_1
    //   366: goto +49 -> 415
    //   369: astore_1
    //   370: aload_0
    //   371: getfield 637	android/view/ViewRootImpl:mTag	Ljava/lang/String;
    //   374: ldc_w 1461
    //   377: aload_1
    //   378: invokestatic 1457	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   381: pop
    //   382: aload_0
    //   383: iconst_1
    //   384: putfield 1459	android/view/ViewRootImpl:mLayoutRequested	Z
    //   387: aload 6
    //   389: iload 10
    //   391: iload 11
    //   393: invokevirtual 1308	android/graphics/Rect:offset	(II)V
    //   396: iconst_0
    //   397: ireturn
    //   398: astore_1
    //   399: aload_0
    //   400: aload_1
    //   401: invokespecial 1383	android/view/ViewRootImpl:handleOutOfResourcesException	(Landroid/view/Surface$OutOfResourcesException;)V
    //   404: aload 6
    //   406: iload 10
    //   408: iload 11
    //   410: invokevirtual 1308	android/graphics/Rect:offset	(II)V
    //   413: iconst_0
    //   414: ireturn
    //   415: aload 6
    //   417: iload 10
    //   419: iload 11
    //   421: invokevirtual 1308	android/graphics/Rect:offset	(II)V
    //   424: aload_1
    //   425: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	426	0	this	ViewRootImpl
    //   0	426	1	paramSurface	Surface
    //   0	426	2	paramAttachInfo	View.AttachInfo
    //   0	426	3	paramInt1	int
    //   0	426	4	paramInt2	int
    //   0	426	5	paramBoolean	boolean
    //   0	426	6	paramRect1	Rect
    //   0	426	7	paramRect2	Rect
    //   1	135	8	i	int
    //   5	121	9	j	int
    //   9	409	10	k	int
    //   13	407	11	m	int
    //   64	42	12	n	int
    //   71	45	13	i1	int
    //   225	11	14	f1	float
    //   231	7	15	f2	float
    // Exception table:
    //   from	to	target	type
    //   311	317	319	java/lang/IllegalArgumentException
    //   357	363	319	java/lang/IllegalArgumentException
    //   233	258	339	finally
    //   263	268	339	finally
    //   273	299	339	finally
    //   161	169	356	finally
    //   184	193	356	finally
    //   193	222	356	finally
    //   299	311	356	finally
    //   341	353	356	finally
    //   353	356	356	finally
    //   50	138	365	finally
    //   138	143	365	finally
    //   143	152	365	finally
    //   370	387	365	finally
    //   399	404	365	finally
    //   50	138	369	java/lang/IllegalArgumentException
    //   138	143	369	java/lang/IllegalArgumentException
    //   143	152	369	java/lang/IllegalArgumentException
    //   50	138	398	android/view/Surface$OutOfResourcesException
    //   138	143	398	android/view/Surface$OutOfResourcesException
    //   143	152	398	android/view/Surface$OutOfResourcesException
  }
  
  private void dumpViewHierarchy(String paramString, PrintWriter paramPrintWriter, View paramView)
  {
    paramPrintWriter.print(paramString);
    if (paramView == null)
    {
      paramPrintWriter.println("null");
      return;
    }
    paramPrintWriter.println(paramView.toString());
    if (!(paramView instanceof ViewGroup)) {
      return;
    }
    paramView = (ViewGroup)paramView;
    int i = paramView.getChildCount();
    if (i <= 0) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("  ");
    paramString = localStringBuilder.toString();
    for (int j = 0; j < i; j++) {
      dumpViewHierarchy(paramString, paramPrintWriter, paramView.getChildAt(j));
    }
  }
  
  private void enableHardwareAcceleration(WindowManager.LayoutParams paramLayoutParams)
  {
    Object localObject = mAttachInfo;
    boolean bool1 = false;
    mHardwareAccelerated = false;
    mAttachInfo.mHardwareAccelerationRequested = false;
    if (mTranslator != null) {
      return;
    }
    int i;
    if ((flags & 0x1000000) != 0) {
      i = 1;
    } else {
      i = 0;
    }
    if (i != 0)
    {
      if (!ThreadedRenderer.isAvailable()) {
        return;
      }
      if ((privateFlags & 0x1) != 0) {
        i = 1;
      } else {
        i = 0;
      }
      int j;
      if ((privateFlags & 0x2) != 0) {
        j = 1;
      } else {
        j = 0;
      }
      if (i != 0)
      {
        mAttachInfo.mHardwareAccelerationRequested = true;
      }
      else if ((!ThreadedRenderer.sRendererDisabled) || ((ThreadedRenderer.sSystemRendererDisabled) && (j != 0)))
      {
        if (mAttachInfo.mThreadedRenderer != null) {
          mAttachInfo.mThreadedRenderer.destroy();
        }
        localObject = surfaceInsets;
        if ((left == 0) && (right == 0) && (top == 0) && (bottom == 0)) {
          i = 0;
        } else {
          i = 1;
        }
        boolean bool2;
        if ((format == -1) && (i == 0)) {
          bool2 = false;
        } else {
          bool2 = true;
        }
        boolean bool3 = bool1;
        if (mContext.getResources().getConfiguration().isScreenWideColorGamut())
        {
          bool3 = bool1;
          if (paramLayoutParams.getColorMode() == 1) {
            bool3 = true;
          }
        }
        mAttachInfo.mThreadedRenderer = ThreadedRenderer.create(mContext, bool2, paramLayoutParams.getTitle().toString());
        mAttachInfo.mThreadedRenderer.setWideGamut(bool3);
        if (mAttachInfo.mThreadedRenderer != null)
        {
          paramLayoutParams = mAttachInfo;
          mAttachInfo.mHardwareAccelerationRequested = true;
          mHardwareAccelerated = true;
        }
      }
    }
  }
  
  private void endDragResizing()
  {
    if (mDragResizing)
    {
      mDragResizing = false;
      if (mUseMTRenderer) {
        for (int i = mWindowCallbacks.size() - 1; i >= 0; i--) {
          ((WindowCallbacks)mWindowCallbacks.get(i)).onWindowDragResizeEnd();
        }
      }
      mFullRedrawNeeded = true;
    }
  }
  
  private Rect ensureInsetsNonNegative(Rect paramRect, String paramString)
  {
    if ((left >= 0) && (top >= 0) && (right >= 0) && (bottom >= 0)) {
      return paramRect;
    }
    String str = mTag;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Negative ");
    localStringBuilder.append(paramString);
    localStringBuilder.append("Insets: ");
    localStringBuilder.append(paramRect);
    localStringBuilder.append(", mFirst=");
    localStringBuilder.append(mFirst);
    Log.wtf(str, localStringBuilder.toString());
    return new Rect(Math.max(0, left), Math.max(0, top), Math.max(0, right), Math.max(0, bottom));
  }
  
  private boolean ensureTouchModeLocally(boolean paramBoolean)
  {
    if (mAttachInfo.mInTouchMode == paramBoolean) {
      return false;
    }
    mAttachInfo.mInTouchMode = paramBoolean;
    mAttachInfo.mTreeObserver.dispatchOnTouchModeChanged(paramBoolean);
    if (paramBoolean) {
      paramBoolean = enterTouchMode();
    } else {
      paramBoolean = leaveTouchMode();
    }
    return paramBoolean;
  }
  
  private boolean enterTouchMode()
  {
    if ((mView != null) && (mView.hasFocus()))
    {
      View localView = mView.findFocus();
      if ((localView != null) && (!localView.isFocusableInTouchMode()))
      {
        ViewGroup localViewGroup = findAncestorToTakeFocusInTouchMode(localView);
        if (localViewGroup != null) {
          return localViewGroup.requestFocus();
        }
        localView.clearFocusInternal(null, true, false);
        return true;
      }
    }
    return false;
  }
  
  private static ViewGroup findAncestorToTakeFocusInTouchMode(View paramView)
  {
    for (paramView = paramView.getParent(); (paramView instanceof ViewGroup); paramView = paramView.getParent())
    {
      paramView = (ViewGroup)paramView;
      if ((paramView.getDescendantFocusability() == 262144) && (paramView.isFocusableInTouchMode())) {
        return paramView;
      }
      if (paramView.isRootNamespace()) {
        return null;
      }
    }
    return null;
  }
  
  private AccessibilityNodeInfo findFocusedVirtualNode(AccessibilityNodeProvider paramAccessibilityNodeProvider)
  {
    Object localObject = paramAccessibilityNodeProvider.findFocus(1);
    if (localObject != null) {
      return localObject;
    }
    if (!mContext.isAutofillCompatibilityEnabled()) {
      return null;
    }
    AccessibilityNodeInfo localAccessibilityNodeInfo1 = paramAccessibilityNodeProvider.createAccessibilityNodeInfo(-1);
    if (localAccessibilityNodeInfo1.isFocused()) {
      return localAccessibilityNodeInfo1;
    }
    localObject = new LinkedList();
    ((Queue)localObject).offer(localAccessibilityNodeInfo1);
    while (!((Queue)localObject).isEmpty())
    {
      AccessibilityNodeInfo localAccessibilityNodeInfo2 = (AccessibilityNodeInfo)((Queue)localObject).poll();
      LongArray localLongArray = localAccessibilityNodeInfo2.getChildNodeIds();
      if ((localLongArray != null) && (localLongArray.size() > 0))
      {
        int i = localLongArray.size();
        for (int j = 0; j < i; j++)
        {
          localAccessibilityNodeInfo1 = paramAccessibilityNodeProvider.createAccessibilityNodeInfo(AccessibilityNodeInfo.getVirtualDescendantId(localLongArray.get(j)));
          if (localAccessibilityNodeInfo1 != null)
          {
            if (localAccessibilityNodeInfo1.isFocused()) {
              return localAccessibilityNodeInfo1;
            }
            ((Queue)localObject).offer(localAccessibilityNodeInfo1);
          }
        }
        localAccessibilityNodeInfo2.recycle();
      }
    }
    return null;
  }
  
  private void finishInputEvent(QueuedInputEvent paramQueuedInputEvent)
  {
    Trace.asyncTraceEnd(8L, "deliverInputEvent", mEvent.getSequenceNumber());
    if (mReceiver != null)
    {
      boolean bool;
      if ((mFlags & 0x8) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      mReceiver.finishInputEvent(mEvent, bool);
    }
    else
    {
      mEvent.recycleIfNeededAfterDispatch();
    }
    recycleQueuedInputEvent(paramQueuedInputEvent);
  }
  
  private void fireAccessibilityFocusEventIfHasFocusedNode()
  {
    if (!AccessibilityManager.getInstance(mContext).isEnabled()) {
      return;
    }
    View localView = mView.findFocus();
    if (localView == null) {
      return;
    }
    Object localObject = localView.getAccessibilityNodeProvider();
    if (localObject == null)
    {
      localView.sendAccessibilityEvent(8);
    }
    else
    {
      localObject = findFocusedVirtualNode((AccessibilityNodeProvider)localObject);
      if (localObject != null)
      {
        int i = AccessibilityNodeInfo.getVirtualDescendantId(((AccessibilityNodeInfo)localObject).getSourceNodeId());
        AccessibilityEvent localAccessibilityEvent = AccessibilityEvent.obtain(8);
        localAccessibilityEvent.setSource(localView, i);
        localAccessibilityEvent.setPackageName(((AccessibilityNodeInfo)localObject).getPackageName());
        localAccessibilityEvent.setChecked(((AccessibilityNodeInfo)localObject).isChecked());
        localAccessibilityEvent.setContentDescription(((AccessibilityNodeInfo)localObject).getContentDescription());
        localAccessibilityEvent.setPassword(((AccessibilityNodeInfo)localObject).isPassword());
        localAccessibilityEvent.getText().add(((AccessibilityNodeInfo)localObject).getText());
        localAccessibilityEvent.setEnabled(((AccessibilityNodeInfo)localObject).isEnabled());
        localView.getParent().requestSendAccessibilityEvent(localView, localAccessibilityEvent);
        ((AccessibilityNodeInfo)localObject).recycle();
      }
    }
  }
  
  private static void forceLayout(View paramView)
  {
    paramView.forceLayout();
    if ((paramView instanceof ViewGroup))
    {
      paramView = (ViewGroup)paramView;
      int i = paramView.getChildCount();
      for (int j = 0; j < i; j++) {
        forceLayout(paramView.getChildAt(j));
      }
    }
  }
  
  private Drawable getAccessibilityFocusedDrawable()
  {
    if (mAttachInfo.mAccessibilityFocusDrawable == null)
    {
      TypedValue localTypedValue = new TypedValue();
      if (mView.mContext.getTheme().resolveAttribute(17891332, localTypedValue, true)) {
        mAttachInfo.mAccessibilityFocusDrawable = mView.mContext.getDrawable(resourceId);
      }
    }
    return mAttachInfo.mAccessibilityFocusDrawable;
  }
  
  private boolean getAccessibilityFocusedRect(Rect paramRect)
  {
    Object localObject = AccessibilityManager.getInstance(mView.mContext);
    if ((((AccessibilityManager)localObject).isEnabled()) && (((AccessibilityManager)localObject).isTouchExplorationEnabled()))
    {
      localObject = mAccessibilityFocusedHost;
      if ((localObject != null) && (mAttachInfo != null))
      {
        if (((View)localObject).getAccessibilityNodeProvider() == null)
        {
          ((View)localObject).getBoundsOnScreen(paramRect, true);
        }
        else
        {
          if (mAccessibilityFocusedVirtualView == null) {
            break label143;
          }
          mAccessibilityFocusedVirtualView.getBoundsInScreen(paramRect);
        }
        localObject = mAttachInfo;
        paramRect.offset(0, mViewRootImpl.mScrollY);
        paramRect.offset(-mWindowLeft, -mWindowTop);
        if (!paramRect.intersect(0, 0, mViewRootImpl.mWidth, mViewRootImpl.mHeight)) {
          paramRect.setEmpty();
        }
        return paramRect.isEmpty() ^ true;
        label143:
        return false;
      }
      return false;
    }
    return false;
  }
  
  private AudioManager getAudioManager()
  {
    if (mView != null)
    {
      if (mAudioManager == null) {
        mAudioManager = ((AudioManager)mView.getContext().getSystemService("audio"));
      }
      return mAudioManager;
    }
    throw new IllegalStateException("getAudioManager called when there is no mView");
  }
  
  private AutofillManager getAutofillManager()
  {
    if ((mView instanceof ViewGroup))
    {
      ViewGroup localViewGroup = (ViewGroup)mView;
      if (localViewGroup.getChildCount() > 0) {
        return (AutofillManager)localViewGroup.getChildAt(0).getContext().getSystemService(AutofillManager.class);
      }
    }
    return null;
  }
  
  private View getCommonPredecessor(View paramView1, View paramView2)
  {
    if (mTempHashSet == null) {
      mTempHashSet = new HashSet();
    }
    HashSet localHashSet = mTempHashSet;
    localHashSet.clear();
    while (paramView1 != null)
    {
      localHashSet.add(paramView1);
      paramView1 = mParent;
      if ((paramView1 instanceof View)) {
        paramView1 = (View)paramView1;
      } else {
        paramView1 = null;
      }
    }
    paramView1 = paramView2;
    while (paramView1 != null)
    {
      if (localHashSet.contains(paramView1))
      {
        localHashSet.clear();
        return paramView1;
      }
      paramView1 = mParent;
      if ((paramView1 instanceof View)) {
        paramView1 = (View)paramView1;
      } else {
        paramView1 = null;
      }
    }
    localHashSet.clear();
    return null;
  }
  
  private static void getGfxInfo(View paramView, int[] paramArrayOfInt)
  {
    RenderNode localRenderNode = mRenderNode;
    int i = 0;
    paramArrayOfInt[0] += 1;
    if (localRenderNode != null) {
      paramArrayOfInt[1] += localRenderNode.getDebugSize();
    }
    if ((paramView instanceof ViewGroup))
    {
      paramView = (ViewGroup)paramView;
      int j = paramView.getChildCount();
      while (i < j)
      {
        getGfxInfo(paramView.getChildAt(i), paramArrayOfInt);
        i++;
      }
    }
  }
  
  private int getImpliedSystemUiVisibility(WindowManager.LayoutParams paramLayoutParams)
  {
    int i = 0;
    if ((flags & 0x4000000) != 0) {
      i = 0x0 | 0x500;
    }
    int j = i;
    if ((flags & 0x8000000) != 0) {
      j = i | 0x300;
    }
    return j;
  }
  
  private static int getRootMeasureSpec(int paramInt1, int paramInt2)
  {
    switch (paramInt2)
    {
    default: 
      paramInt1 = View.MeasureSpec.makeMeasureSpec(paramInt2, 1073741824);
      break;
    case -1: 
      paramInt1 = View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824);
      break;
    case -2: 
      paramInt1 = View.MeasureSpec.makeMeasureSpec(paramInt1, Integer.MIN_VALUE);
    }
    return paramInt1;
  }
  
  static HandlerActionQueue getRunQueue()
  {
    HandlerActionQueue localHandlerActionQueue = (HandlerActionQueue)sRunQueues.get();
    if (localHandlerActionQueue != null) {
      return localHandlerActionQueue;
    }
    localHandlerActionQueue = new HandlerActionQueue();
    sRunQueues.set(localHandlerActionQueue);
    return localHandlerActionQueue;
  }
  
  private ArrayList<View> getValidLayoutRequesters(ArrayList<View> paramArrayList, boolean paramBoolean)
  {
    int i = paramArrayList.size();
    int j = 0;
    Object localObject1 = null;
    int k = 0;
    Object localObject2;
    while (k < i)
    {
      View localView = (View)paramArrayList.get(k);
      localObject2 = localObject1;
      if (localView != null)
      {
        localObject2 = localObject1;
        if (mAttachInfo != null)
        {
          localObject2 = localObject1;
          if (mParent != null) {
            if (!paramBoolean)
            {
              localObject2 = localObject1;
              if ((mPrivateFlags & 0x1000) != 4096) {}
            }
            else
            {
              int m = 0;
              localObject2 = localView;
              int n;
              for (;;)
              {
                n = m;
                if (localObject2 == null) {
                  break;
                }
                if ((mViewFlags & 0xC) == 8)
                {
                  n = 1;
                  break;
                }
                if ((mParent instanceof View)) {
                  localObject2 = (View)mParent;
                } else {
                  localObject2 = null;
                }
              }
              localObject2 = localObject1;
              if (n == 0)
              {
                localObject2 = localObject1;
                if (localObject1 == null) {
                  localObject2 = new ArrayList();
                }
                ((ArrayList)localObject2).add(localView);
              }
            }
          }
        }
      }
      k++;
      localObject1 = localObject2;
    }
    if (!paramBoolean) {
      for (k = j; k < i; k++)
      {
        localObject2 = (View)paramArrayList.get(k);
        while ((localObject2 != null) && ((mPrivateFlags & 0x1000) != 0))
        {
          mPrivateFlags &= 0xEFFF;
          if ((mParent instanceof View)) {
            localObject2 = (View)mParent;
          } else {
            localObject2 = null;
          }
        }
      }
    }
    paramArrayList.clear();
    return localObject1;
  }
  
  private void handleDispatchNavigationColor()
  {
    mAttachInfo.mRecomputeGlobalAttributes = true;
    scheduleTraversals();
  }
  
  private void handleDragEvent(DragEvent paramDragEvent)
  {
    if ((mView != null) && (mAdded))
    {
      int i = mAction;
      if (i == 1)
      {
        mCurrentDragView = null;
        mDragDescription = mClipDescription;
      }
      else
      {
        if (i == 4) {
          mDragDescription = null;
        }
        mClipDescription = mDragDescription;
      }
      if (i == 6)
      {
        if (View.sCascadedDragDrop) {
          mView.dispatchDragEnterExitInPreN(paramDragEvent);
        }
        setDragFocus(null, paramDragEvent);
      }
      else
      {
        if ((i == 2) || (i == 3))
        {
          mDragPoint.set(mX, mY);
          if (mTranslator != null) {
            mTranslator.translatePointInScreenToAppWindow(mDragPoint);
          }
          if (mCurScrollY != 0) {
            mDragPoint.offset(0.0F, mCurScrollY);
          }
          mX = mDragPoint.x;
          mY = mDragPoint.y;
        }
        View localView = mCurrentDragView;
        if ((i == 3) && (mClipData != null)) {
          mClipData.prepareToEnterProcess();
        }
        boolean bool = mView.dispatchDragEvent(paramDragEvent);
        if ((i == 2) && (!mEventHandlerWasCalled)) {
          setDragFocus(null, paramDragEvent);
        }
        if (localView != mCurrentDragView)
        {
          if (localView != null) {
            try
            {
              mWindowSession.dragRecipientExited(mWindow);
            }
            catch (RemoteException localRemoteException1)
            {
              break label280;
            }
          }
          if (mCurrentDragView != null) {
            mWindowSession.dragRecipientEntered(mWindow);
          }
          break label291;
          label280:
          Slog.e(mTag, "Unable to note drag target change");
        }
        label291:
        if (i == 3) {
          try
          {
            String str = mTag;
            StringBuilder localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append("Reporting drop result: ");
            localStringBuilder.append(bool);
            Log.i(str, localStringBuilder.toString());
            mWindowSession.reportDropResult(mWindow, bool);
          }
          catch (RemoteException localRemoteException2)
          {
            Log.e(mTag, "Unable to report drop result");
          }
        }
        if (i == 4)
        {
          mCurrentDragView = null;
          setLocalDragState(null);
          mAttachInfo.mDragToken = null;
          if (mAttachInfo.mDragSurface != null)
          {
            mAttachInfo.mDragSurface.release();
            mAttachInfo.mDragSurface = null;
          }
        }
      }
    }
    paramDragEvent.recycle();
  }
  
  private void handleOutOfResourcesException(Surface.OutOfResourcesException paramOutOfResourcesException)
  {
    Log.e(mTag, "OutOfResourcesException initializing HW surface", paramOutOfResourcesException);
    try
    {
      if ((!mWindowSession.outOfMemory(mWindow)) && (Process.myUid() != 1000))
      {
        Slog.w(mTag, "No processes killed for memory; killing self");
        Process.killProcess(Process.myPid());
      }
    }
    catch (RemoteException paramOutOfResourcesException) {}
    mLayoutRequested = true;
  }
  
  private void handlePointerCaptureChanged(boolean paramBoolean)
  {
    if (mPointerCapture == paramBoolean) {
      return;
    }
    mPointerCapture = paramBoolean;
    if (mView != null) {
      mView.dispatchPointerCaptureChanged(paramBoolean);
    }
  }
  
  private void handleWindowContentChangedEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    View localView = mAccessibilityFocusedHost;
    if ((localView != null) && (mAccessibilityFocusedVirtualView != null))
    {
      Object localObject = localView.getAccessibilityNodeProvider();
      if (localObject == null)
      {
        mAccessibilityFocusedHost = null;
        mAccessibilityFocusedVirtualView = null;
        localView.clearAccessibilityFocusNoCallbacks(0);
        return;
      }
      int i = paramAccessibilityEvent.getContentChangeTypes();
      if (((i & 0x1) == 0) && (i != 0)) {
        return;
      }
      int j = AccessibilityNodeInfo.getAccessibilityViewId(paramAccessibilityEvent.getSourceNodeId());
      i = 0;
      paramAccessibilityEvent = mAccessibilityFocusedHost;
      while ((paramAccessibilityEvent != null) && (i == 0)) {
        if (j == paramAccessibilityEvent.getAccessibilityViewId())
        {
          i = 1;
        }
        else
        {
          paramAccessibilityEvent = paramAccessibilityEvent.getParent();
          if ((paramAccessibilityEvent instanceof View)) {
            paramAccessibilityEvent = (View)paramAccessibilityEvent;
          } else {
            paramAccessibilityEvent = null;
          }
        }
      }
      if (i == 0) {
        return;
      }
      i = AccessibilityNodeInfo.getVirtualDescendantId(mAccessibilityFocusedVirtualView.getSourceNodeId());
      paramAccessibilityEvent = mTempRect;
      mAccessibilityFocusedVirtualView.getBoundsInScreen(paramAccessibilityEvent);
      mAccessibilityFocusedVirtualView = ((AccessibilityNodeProvider)localObject).createAccessibilityNodeInfo(i);
      if (mAccessibilityFocusedVirtualView == null)
      {
        mAccessibilityFocusedHost = null;
        localView.clearAccessibilityFocusNoCallbacks(0);
        ((AccessibilityNodeProvider)localObject).performAction(i, AccessibilityNodeInfo.AccessibilityAction.ACTION_CLEAR_ACCESSIBILITY_FOCUS.getId(), null);
        invalidateRectOnScreen(paramAccessibilityEvent);
      }
      else
      {
        localObject = mAccessibilityFocusedVirtualView.getBoundsInScreen();
        if (!paramAccessibilityEvent.equals(localObject))
        {
          paramAccessibilityEvent.union((Rect)localObject);
          invalidateRectOnScreen(paramAccessibilityEvent);
        }
      }
      return;
    }
  }
  
  private void handleWindowFocusChanged()
  {
    try
    {
      if (!mWindowFocusChanged) {
        return;
      }
      mWindowFocusChanged = false;
      boolean bool1 = mUpcomingWindowFocus;
      boolean bool2 = mUpcomingInTouchMode;
      if (mAdded)
      {
        profileRendering(bool1);
        if (bool1)
        {
          ensureTouchModeLocally(bool2);
          if ((mAttachInfo.mThreadedRenderer != null) && (mSurface.isValid()))
          {
            mFullRedrawNeeded = true;
            try
            {
              Object localObject1 = mWindowAttributes;
              if (localObject1 != null) {
                localObject1 = surfaceInsets;
              } else {
                localObject1 = null;
              }
              mAttachInfo.mThreadedRenderer.initializeIfNeeded(mWidth, mHeight, mAttachInfo, mSurface, (Rect)localObject1);
            }
            catch (Surface.OutOfResourcesException localOutOfResourcesException)
            {
              Log.e(mTag, "OutOfResourcesException locking surface", localOutOfResourcesException);
              try
              {
                if (!mWindowSession.outOfMemory(mWindow))
                {
                  Slog.w(mTag, "No processes killed for memory; killing self");
                  Process.killProcess(Process.myPid());
                }
              }
              catch (RemoteException localRemoteException) {}
              mHandler.sendMessageDelayed(mHandler.obtainMessage(6), 500L);
              return;
            }
          }
        }
        mAttachInfo.mHasWindowFocus = bool1;
        mLastWasImTarget = WindowManager.LayoutParams.mayUseInputMethod(mWindowAttributes.flags);
        Object localObject2 = InputMethodManager.peekInstance();
        if ((localObject2 != null) && (mLastWasImTarget) && (!isInLocalFocusMode())) {
          ((InputMethodManager)localObject2).onPreWindowFocus(mView, bool1);
        }
        if (mView != null)
        {
          mAttachInfo.mKeyDispatchState.reset();
          mView.dispatchWindowFocusChanged(bool1);
          mAttachInfo.mTreeObserver.dispatchOnWindowFocusChange(bool1);
          if (mAttachInfo.mTooltipHost != null) {
            mAttachInfo.mTooltipHost.hideTooltip();
          }
        }
        if (bool1)
        {
          if ((localObject2 != null) && (mLastWasImTarget) && (!isInLocalFocusMode())) {
            ((InputMethodManager)localObject2).onPostWindowFocus(mView, mView.findFocus(), mWindowAttributes.softInputMode, mHasHadWindowFocus ^ true, mWindowAttributes.flags);
          }
          localObject2 = mWindowAttributes;
          softInputMode &= 0xFEFF;
          localObject2 = (WindowManager.LayoutParams)mView.getLayoutParams();
          softInputMode &= 0xFEFF;
          mHasHadWindowFocus = true;
          fireAccessibilityFocusEventIfHasFocusedNode();
        }
        else if (mPointerCapture)
        {
          handlePointerCaptureChanged(false);
        }
      }
      mFirstInputStage.onWindowFocusChanged(bool1);
      return;
    }
    finally {}
  }
  
  private void invalidateRectOnScreen(Rect paramRect)
  {
    Rect localRect = mDirty;
    if ((!localRect.isEmpty()) && (!localRect.contains(paramRect)))
    {
      mAttachInfo.mSetIgnoreDirtyState = true;
      mAttachInfo.mIgnoreDirtyState = true;
    }
    localRect.union(left, top, right, bottom);
    float f = mAttachInfo.mApplicationScale;
    boolean bool = localRect.intersect(0, 0, (int)(mWidth * f + 0.5F), (int)(mHeight * f + 0.5F));
    if (!bool) {
      localRect.setEmpty();
    }
    if ((!mWillDrawSoon) && ((bool) || (mIsAnimating))) {
      scheduleTraversals();
    }
  }
  
  public static void invokeFunctor(long paramLong, boolean paramBoolean)
  {
    ThreadedRenderer.invokeFunctor(paramLong, paramBoolean);
  }
  
  private boolean isAutofillUiShowing()
  {
    AutofillManager localAutofillManager = getAutofillManager();
    if (localAutofillManager == null) {
      return false;
    }
    return localAutofillManager.isAutofillUiShowing();
  }
  
  private boolean isInLocalFocusMode()
  {
    boolean bool;
    if ((mWindowAttributes.flags & 0x10000000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  static boolean isInTouchMode()
  {
    IWindowSession localIWindowSession = WindowManagerGlobal.peekWindowSession();
    if (localIWindowSession != null) {
      try
      {
        boolean bool = localIWindowSession.getInTouchMode();
        return bool;
      }
      catch (RemoteException localRemoteException) {}
    }
    return false;
  }
  
  private static boolean isNavigationKey(KeyEvent paramKeyEvent)
  {
    switch (paramKeyEvent.getKeyCode())
    {
    default: 
      return false;
    }
    return true;
  }
  
  static boolean isTerminalInputEvent(InputEvent paramInputEvent)
  {
    boolean bool1 = paramInputEvent instanceof KeyEvent;
    boolean bool2 = false;
    boolean bool3 = false;
    if (bool1)
    {
      if (((KeyEvent)paramInputEvent).getAction() == 1) {
        bool3 = true;
      }
      return bool3;
    }
    int i = ((MotionEvent)paramInputEvent).getAction();
    if ((i != 1) && (i != 3) && (i != 10)) {
      bool3 = bool2;
    } else {
      bool3 = true;
    }
    return bool3;
  }
  
  private static boolean isTypingKey(KeyEvent paramKeyEvent)
  {
    boolean bool;
    if (paramKeyEvent.getUnicodeChar() > 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isViewDescendantOf(View paramView1, View paramView2)
  {
    boolean bool = true;
    if (paramView1 == paramView2) {
      return true;
    }
    paramView1 = paramView1.getParent();
    if ((!(paramView1 instanceof ViewGroup)) || (!isViewDescendantOf((View)paramView1, paramView2))) {
      bool = false;
    }
    return bool;
  }
  
  private boolean leaveTouchMode()
  {
    if (mView != null)
    {
      if (mView.hasFocus())
      {
        View localView = mView.findFocus();
        if (!(localView instanceof ViewGroup)) {
          return false;
        }
        if (((ViewGroup)localView).getDescendantFocusability() != 262144) {
          return false;
        }
      }
      return mView.restoreDefaultFocus();
    }
    return false;
  }
  
  private void maybeHandleWindowMove(Rect paramRect)
  {
    int i;
    if ((mAttachInfo.mWindowLeft == left) && (mAttachInfo.mWindowTop == top)) {
      i = 0;
    } else {
      i = 1;
    }
    if (i != 0)
    {
      if (mTranslator != null) {
        mTranslator.translateRectInScreenToAppWinFrame(paramRect);
      }
      mAttachInfo.mWindowLeft = left;
      mAttachInfo.mWindowTop = top;
    }
    if ((i != 0) || (mAttachInfo.mNeedsUpdateLightCenter))
    {
      if (mAttachInfo.mThreadedRenderer != null) {
        mAttachInfo.mThreadedRenderer.setLightCenter(mAttachInfo);
      }
      mAttachInfo.mNeedsUpdateLightCenter = false;
    }
  }
  
  private void maybeUpdateTooltip(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent.getPointerCount() != 1) {
      return;
    }
    int i = paramMotionEvent.getActionMasked();
    if ((i != 9) && (i != 7) && (i != 10)) {
      return;
    }
    AccessibilityManager localAccessibilityManager = AccessibilityManager.getInstance(mContext);
    if ((localAccessibilityManager.isEnabled()) && (localAccessibilityManager.isTouchExplorationEnabled())) {
      return;
    }
    if (mView == null)
    {
      Slog.d(mTag, "maybeUpdateTooltip called after view was removed");
      return;
    }
    mView.dispatchTooltipHoverEvent(paramMotionEvent);
  }
  
  private boolean measureHierarchy(View paramView, WindowManager.LayoutParams paramLayoutParams, Resources paramResources, int paramInt1, int paramInt2)
  {
    boolean bool1 = false;
    int i = 0;
    int j = i;
    if (width == -2)
    {
      DisplayMetrics localDisplayMetrics = paramResources.getDisplayMetrics();
      paramResources.getValue(17105088, mTmpValue, true);
      int k = 0;
      if (mTmpValue.type == 5) {
        k = (int)mTmpValue.getDimension(localDisplayMetrics);
      }
      j = i;
      if (k != 0)
      {
        j = i;
        if (paramInt1 > k)
        {
          j = getRootMeasureSpec(k, width);
          int m = getRootMeasureSpec(paramInt2, height);
          performMeasure(j, m);
          if ((paramView.getMeasuredWidthAndState() & 0x1000000) == 0)
          {
            j = 1;
          }
          else
          {
            performMeasure(getRootMeasureSpec((k + paramInt1) / 2, width), m);
            j = i;
            if ((paramView.getMeasuredWidthAndState() & 0x1000000) == 0) {
              j = 1;
            }
          }
        }
      }
    }
    boolean bool2 = bool1;
    if (j == 0)
    {
      performMeasure(getRootMeasureSpec(paramInt1, width), getRootMeasureSpec(paramInt2, height));
      if (mWidth == paramView.getMeasuredWidth())
      {
        bool2 = bool1;
        if (mHeight == paramView.getMeasuredHeight()) {}
      }
      else
      {
        bool2 = true;
      }
    }
    return bool2;
  }
  
  private QueuedInputEvent obtainQueuedInputEvent(InputEvent paramInputEvent, InputEventReceiver paramInputEventReceiver, int paramInt)
  {
    QueuedInputEvent localQueuedInputEvent = mQueuedInputEventPool;
    if (localQueuedInputEvent != null)
    {
      mQueuedInputEventPoolSize -= 1;
      mQueuedInputEventPool = mNext;
      mNext = null;
    }
    else
    {
      localQueuedInputEvent = new QueuedInputEvent(null);
    }
    mEvent = paramInputEvent;
    mReceiver = paramInputEventReceiver;
    mFlags = paramInt;
    return localQueuedInputEvent;
  }
  
  private void performConfigurationChange(MergedConfiguration paramMergedConfiguration, boolean paramBoolean, int paramInt)
  {
    if (paramMergedConfiguration != null)
    {
      ??? = paramMergedConfiguration.getGlobalConfiguration();
      Configuration localConfiguration = paramMergedConfiguration.getOverrideConfiguration();
      CompatibilityInfo localCompatibilityInfo = mDisplay.getDisplayAdjustments().getCompatibilityInfo();
      paramMergedConfiguration = (MergedConfiguration)???;
      if (!localCompatibilityInfo.equals(CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO))
      {
        paramMergedConfiguration = new Configuration((Configuration)???);
        localCompatibilityInfo.applyToConfiguration(mNoncompatDensity, paramMergedConfiguration);
      }
      synchronized (sConfigCallbacks)
      {
        for (int i = sConfigCallbacks.size() - 1; i >= 0; i--) {
          ((ConfigChangedCallback)sConfigCallbacks.get(i)).onConfigurationChanged(paramMergedConfiguration);
        }
        mLastReportedMergedConfiguration.setConfiguration(paramMergedConfiguration, localConfiguration);
        mForceNextConfigUpdate = paramBoolean;
        if (mActivityConfigCallback != null) {
          mActivityConfigCallback.onConfigurationChanged(localConfiguration, paramInt);
        } else {
          updateConfiguration(paramInt);
        }
        mForceNextConfigUpdate = false;
        return;
      }
    }
    throw new IllegalArgumentException("No merged config provided.");
  }
  
  private void performDraw()
  {
    if ((mAttachInfo.mDisplayState == 1) && (!mReportNextDraw)) {
      return;
    }
    if (mView == null) {
      return;
    }
    boolean bool;
    if ((!mFullRedrawNeeded) && (!mReportNextDraw)) {
      bool = false;
    } else {
      bool = true;
    }
    mFullRedrawNeeded = false;
    mIsDrawing = true;
    Trace.traceBegin(8L, "draw");
    int i = 0;
    int j = i;
    if (mReportNextDraw)
    {
      j = i;
      if (mAttachInfo.mThreadedRenderer != null)
      {
        j = i;
        if (mAttachInfo.mThreadedRenderer.isEnabled())
        {
          j = 1;
          mAttachInfo.mThreadedRenderer.setFrameCompleteCallback(new _..Lambda.ViewRootImpl.zmAX2p20_kqxknxcUyGhSNjsJvM(this));
        }
      }
    }
    try
    {
      bool = draw(bool);
      i = j;
      if (j != 0)
      {
        i = j;
        if (!bool)
        {
          mAttachInfo.mThreadedRenderer.setFrameCompleteCallback(null);
          i = 0;
        }
      }
      mIsDrawing = false;
      Trace.traceEnd(8L);
      if (mAttachInfo.mPendingAnimatingRenderNodes != null)
      {
        int k = mAttachInfo.mPendingAnimatingRenderNodes.size();
        for (j = 0; j < k; j++) {
          ((RenderNode)mAttachInfo.mPendingAnimatingRenderNodes.get(j)).endAllAnimators();
        }
        mAttachInfo.mPendingAnimatingRenderNodes.clear();
      }
      if (mReportNextDraw)
      {
        mReportNextDraw = false;
        if (mWindowDrawCountDown != null)
        {
          try
          {
            mWindowDrawCountDown.await();
          }
          catch (InterruptedException localInterruptedException)
          {
            Log.e(mTag, "Window redraw count down interrupted!");
          }
          mWindowDrawCountDown = null;
        }
        if (mAttachInfo.mThreadedRenderer != null) {
          mAttachInfo.mThreadedRenderer.setStopped(mStopped);
        }
        if ((mSurfaceHolder != null) && (mSurface.isValid()))
        {
          SurfaceCallbackHelper localSurfaceCallbackHelper = new SurfaceCallbackHelper(new _..Lambda.ViewRootImpl.dznxCZGM2R1fsBljsJKomLjBRoM(this));
          SurfaceHolder.Callback[] arrayOfCallback = mSurfaceHolder.getCallbacks();
          localSurfaceCallbackHelper.dispatchSurfaceRedrawNeededAsync(mSurfaceHolder, arrayOfCallback);
        }
        else if (i == 0)
        {
          if (mAttachInfo.mThreadedRenderer != null) {
            mAttachInfo.mThreadedRenderer.fence();
          }
          pendingDrawFinished();
        }
      }
      return;
    }
    finally
    {
      mIsDrawing = false;
      Trace.traceEnd(8L);
    }
  }
  
  private void performLayout(WindowManager.LayoutParams paramLayoutParams, int paramInt1, int paramInt2)
  {
    mLayoutRequested = false;
    mScrollMayChange = true;
    mInLayout = true;
    View localView = mView;
    if (localView == null) {
      return;
    }
    Trace.traceBegin(8L, "layout");
    try
    {
      localView.layout(0, 0, localView.getMeasuredWidth(), localView.getMeasuredHeight());
      mInLayout = false;
      if (mLayoutRequesters.size() > 0)
      {
        ArrayList localArrayList = getValidLayoutRequesters(mLayoutRequesters, false);
        if (localArrayList != null)
        {
          mHandlingLayoutInLayoutRequest = true;
          int i = localArrayList.size();
          Object localObject1;
          Object localObject2;
          for (int j = 0; j < i; j++)
          {
            localObject1 = (View)localArrayList.get(j);
            localObject2 = new java/lang/StringBuilder;
            ((StringBuilder)localObject2).<init>();
            ((StringBuilder)localObject2).append("requestLayout() improperly called by ");
            ((StringBuilder)localObject2).append(localObject1);
            ((StringBuilder)localObject2).append(" during layout: running second layout pass");
            Log.w("View", ((StringBuilder)localObject2).toString());
            ((View)localObject1).requestLayout();
          }
          measureHierarchy(localView, paramLayoutParams, mView.getContext().getResources(), paramInt1, paramInt2);
          mInLayout = true;
          localView.layout(0, 0, localView.getMeasuredWidth(), localView.getMeasuredHeight());
          mHandlingLayoutInLayoutRequest = false;
          paramLayoutParams = getValidLayoutRequesters(mLayoutRequesters, true);
          if (paramLayoutParams != null)
          {
            localObject2 = getRunQueue();
            localObject1 = new android/view/ViewRootImpl$2;
            ((2)localObject1).<init>(this, paramLayoutParams);
            ((HandlerActionQueue)localObject2).post((Runnable)localObject1);
          }
        }
      }
      Trace.traceEnd(8L);
      mInLayout = false;
      return;
    }
    finally
    {
      Trace.traceEnd(8L);
    }
  }
  
  private void performMeasure(int paramInt1, int paramInt2)
  {
    if (mView == null) {
      return;
    }
    Trace.traceBegin(8L, "measure");
    try
    {
      mView.measure(paramInt1, paramInt2);
      return;
    }
    finally
    {
      Trace.traceEnd(8L);
    }
  }
  
  /* Error */
  private void performTraversals()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 1019	android/view/ViewRootImpl:mView	Landroid/view/View;
    //   4: astore_1
    //   5: aload_1
    //   6: ifnull +5587 -> 5593
    //   9: aload_0
    //   10: getfield 745	android/view/ViewRootImpl:mAdded	Z
    //   13: ifne +6 -> 19
    //   16: goto +5577 -> 5593
    //   19: aload_0
    //   20: iconst_1
    //   21: putfield 2374	android/view/ViewRootImpl:mIsInTraversal	Z
    //   24: aload_0
    //   25: iconst_1
    //   26: putfield 2115	android/view/ViewRootImpl:mWillDrawSoon	Z
    //   29: iconst_0
    //   30: istore_2
    //   31: iconst_0
    //   32: istore_3
    //   33: aload_0
    //   34: getfield 514	android/view/ViewRootImpl:mWindowAttributes	Landroid/view/WindowManager$LayoutParams;
    //   37: astore 4
    //   39: aload_0
    //   40: invokevirtual 2377	android/view/ViewRootImpl:getHostVisibility	()I
    //   43: istore 5
    //   45: aload_0
    //   46: getfield 743	android/view/ViewRootImpl:mFirst	Z
    //   49: ifne +32 -> 81
    //   52: aload_0
    //   53: getfield 734	android/view/ViewRootImpl:mViewVisibility	I
    //   56: iload 5
    //   58: if_icmpne +17 -> 75
    //   61: aload_0
    //   62: getfield 2379	android/view/ViewRootImpl:mNewSurfaceNeeded	Z
    //   65: ifne +10 -> 75
    //   68: aload_0
    //   69: getfield 2381	android/view/ViewRootImpl:mAppVisibilityChanged	Z
    //   72: ifeq +9 -> 81
    //   75: iconst_1
    //   76: istore 6
    //   78: goto +6 -> 84
    //   81: iconst_0
    //   82: istore 6
    //   84: aload_0
    //   85: iconst_0
    //   86: putfield 2381	android/view/ViewRootImpl:mAppVisibilityChanged	Z
    //   89: aload_0
    //   90: getfield 743	android/view/ViewRootImpl:mFirst	Z
    //   93: ifne +46 -> 139
    //   96: aload_0
    //   97: getfield 734	android/view/ViewRootImpl:mViewVisibility	I
    //   100: ifne +9 -> 109
    //   103: iconst_1
    //   104: istore 7
    //   106: goto +6 -> 112
    //   109: iconst_0
    //   110: istore 7
    //   112: iload 5
    //   114: ifne +9 -> 123
    //   117: iconst_1
    //   118: istore 8
    //   120: goto +6 -> 126
    //   123: iconst_0
    //   124: istore 8
    //   126: iload 7
    //   128: iload 8
    //   130: if_icmpeq +9 -> 139
    //   133: iconst_1
    //   134: istore 9
    //   136: goto +6 -> 142
    //   139: iconst_0
    //   140: istore 9
    //   142: aconst_null
    //   143: astore 10
    //   145: aload_0
    //   146: getfield 539	android/view/ViewRootImpl:mWindowAttributesChanged	Z
    //   149: ifeq +14 -> 163
    //   152: aload_0
    //   153: iconst_0
    //   154: putfield 539	android/view/ViewRootImpl:mWindowAttributesChanged	Z
    //   157: iconst_1
    //   158: istore_3
    //   159: aload 4
    //   161: astore 10
    //   163: aload_0
    //   164: getfield 679	android/view/ViewRootImpl:mDisplay	Landroid/view/Display;
    //   167: invokevirtual 2233	android/view/Display:getDisplayAdjustments	()Landroid/view/DisplayAdjustments;
    //   170: invokevirtual 2239	android/view/DisplayAdjustments:getCompatibilityInfo	()Landroid/content/res/CompatibilityInfo;
    //   173: invokevirtual 2384	android/content/res/CompatibilityInfo:supportsScreen	()Z
    //   176: aload_0
    //   177: getfield 528	android/view/ViewRootImpl:mLastInCompatMode	Z
    //   180: if_icmpne +65 -> 245
    //   183: aload 4
    //   185: astore 10
    //   187: aload_0
    //   188: iconst_1
    //   189: putfield 1376	android/view/ViewRootImpl:mFullRedrawNeeded	Z
    //   192: aload_0
    //   193: iconst_1
    //   194: putfield 1459	android/view/ViewRootImpl:mLayoutRequested	Z
    //   197: aload_0
    //   198: getfield 528	android/view/ViewRootImpl:mLastInCompatMode	Z
    //   201: ifeq +25 -> 226
    //   204: aload 10
    //   206: aload 10
    //   208: getfield 1508	android/view/WindowManager$LayoutParams:privateFlags	I
    //   211: sipush 65407
    //   214: iand
    //   215: putfield 1508	android/view/WindowManager$LayoutParams:privateFlags	I
    //   218: aload_0
    //   219: iconst_0
    //   220: putfield 528	android/view/ViewRootImpl:mLastInCompatMode	Z
    //   223: goto +22 -> 245
    //   226: aload 10
    //   228: aload 10
    //   230: getfield 1508	android/view/WindowManager$LayoutParams:privateFlags	I
    //   233: sipush 128
    //   236: ior
    //   237: putfield 1508	android/view/WindowManager$LayoutParams:privateFlags	I
    //   240: aload_0
    //   241: iconst_1
    //   242: putfield 528	android/view/ViewRootImpl:mLastInCompatMode	Z
    //   245: aload 10
    //   247: astore 11
    //   249: aload_0
    //   250: iconst_0
    //   251: putfield 541	android/view/ViewRootImpl:mWindowAttributesChangesFlag	I
    //   254: aload_0
    //   255: getfield 718	android/view/ViewRootImpl:mWinFrame	Landroid/graphics/Rect;
    //   258: astore 10
    //   260: aload_0
    //   261: getfield 743	android/view/ViewRootImpl:mFirst	Z
    //   264: ifeq +184 -> 448
    //   267: aload_0
    //   268: iconst_1
    //   269: putfield 1376	android/view/ViewRootImpl:mFullRedrawNeeded	Z
    //   272: aload_0
    //   273: iconst_1
    //   274: putfield 1459	android/view/ViewRootImpl:mLayoutRequested	Z
    //   277: aload_0
    //   278: getfield 669	android/view/ViewRootImpl:mContext	Landroid/content/Context;
    //   281: invokevirtual 783	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   284: invokevirtual 1521	android/content/res/Resources:getConfiguration	()Landroid/content/res/Configuration;
    //   287: astore 12
    //   289: aload 4
    //   291: invokestatic 2388	android/view/ViewRootImpl:shouldUseDisplaySize	(Landroid/view/WindowManager$LayoutParams;)Z
    //   294: ifeq +38 -> 332
    //   297: new 2390	android/graphics/Point
    //   300: dup
    //   301: invokespecial 2391	android/graphics/Point:<init>	()V
    //   304: astore 13
    //   306: aload_0
    //   307: getfield 679	android/view/ViewRootImpl:mDisplay	Landroid/view/Display;
    //   310: aload 13
    //   312: invokevirtual 2395	android/view/Display:getRealSize	(Landroid/graphics/Point;)V
    //   315: aload 13
    //   317: getfield 2397	android/graphics/Point:x	I
    //   320: istore 7
    //   322: aload 13
    //   324: getfield 2399	android/graphics/Point:y	I
    //   327: istore 8
    //   329: goto +21 -> 350
    //   332: aload_0
    //   333: getfield 718	android/view/ViewRootImpl:mWinFrame	Landroid/graphics/Rect;
    //   336: invokevirtual 2401	android/graphics/Rect:width	()I
    //   339: istore 7
    //   341: aload_0
    //   342: getfield 718	android/view/ViewRootImpl:mWinFrame	Landroid/graphics/Rect;
    //   345: invokevirtual 2403	android/graphics/Rect:height	()I
    //   348: istore 8
    //   350: aload_0
    //   351: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   354: iconst_1
    //   355: putfield 2406	android/view/View$AttachInfo:mUse32BitDrawingCache	Z
    //   358: aload_0
    //   359: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   362: iconst_0
    //   363: putfield 2054	android/view/View$AttachInfo:mHasWindowFocus	Z
    //   366: aload_0
    //   367: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   370: iload 5
    //   372: putfield 2409	android/view/View$AttachInfo:mWindowVisibility	I
    //   375: aload_0
    //   376: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   379: iconst_0
    //   380: putfield 1011	android/view/View$AttachInfo:mRecomputeGlobalAttributes	Z
    //   383: aload_0
    //   384: getfield 589	android/view/ViewRootImpl:mLastConfigurationFromResources	Landroid/content/res/Configuration;
    //   387: aload 12
    //   389: invokevirtual 2412	android/content/res/Configuration:setTo	(Landroid/content/res/Configuration;)V
    //   392: aload_0
    //   393: aload_0
    //   394: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   397: getfield 1014	android/view/View$AttachInfo:mSystemUiVisibility	I
    //   400: putfield 2414	android/view/ViewRootImpl:mLastSystemUiVisibility	I
    //   403: aload_0
    //   404: getfield 2416	android/view/ViewRootImpl:mViewLayoutDirectionInitial	I
    //   407: iconst_2
    //   408: if_icmpne +12 -> 420
    //   411: aload_1
    //   412: aload 12
    //   414: invokevirtual 2419	android/content/res/Configuration:getLayoutDirection	()I
    //   417: invokevirtual 2422	android/view/View:setLayoutDirection	(I)V
    //   420: aload_1
    //   421: aload_0
    //   422: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   425: iconst_0
    //   426: invokevirtual 2425	android/view/View:dispatchAttachedToWindow	(Landroid/view/View$AttachInfo;I)V
    //   429: aload_0
    //   430: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   433: getfield 1240	android/view/View$AttachInfo:mTreeObserver	Landroid/view/ViewTreeObserver;
    //   436: iconst_1
    //   437: invokevirtual 2428	android/view/ViewTreeObserver:dispatchOnWindowAttachedChange	(Z)V
    //   440: aload_0
    //   441: aload_1
    //   442: invokevirtual 2431	android/view/ViewRootImpl:dispatchApplyInsets	(Landroid/view/View;)V
    //   445: goto +63 -> 508
    //   448: aload 10
    //   450: invokevirtual 2401	android/graphics/Rect:width	()I
    //   453: istore 14
    //   455: aload 10
    //   457: invokevirtual 2403	android/graphics/Rect:height	()I
    //   460: istore 15
    //   462: iload 14
    //   464: aload_0
    //   465: getfield 708	android/view/ViewRootImpl:mWidth	I
    //   468: if_icmpne +20 -> 488
    //   471: iload 15
    //   473: istore 8
    //   475: iload 14
    //   477: istore 7
    //   479: iload 15
    //   481: aload_0
    //   482: getfield 710	android/view/ViewRootImpl:mHeight	I
    //   485: if_icmpeq +23 -> 508
    //   488: aload_0
    //   489: iconst_1
    //   490: putfield 1376	android/view/ViewRootImpl:mFullRedrawNeeded	Z
    //   493: aload_0
    //   494: iconst_1
    //   495: putfield 1459	android/view/ViewRootImpl:mLayoutRequested	Z
    //   498: iconst_1
    //   499: istore_2
    //   500: iload 14
    //   502: istore 7
    //   504: iload 15
    //   506: istore 8
    //   508: iload 6
    //   510: ifeq +76 -> 586
    //   513: aload_0
    //   514: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   517: iload 5
    //   519: putfield 2409	android/view/View$AttachInfo:mWindowVisibility	I
    //   522: aload_1
    //   523: iload 5
    //   525: invokevirtual 2434	android/view/View:dispatchWindowVisibilityChanged	(I)V
    //   528: iload 9
    //   530: ifeq +24 -> 554
    //   533: iload 5
    //   535: ifne +9 -> 544
    //   538: iconst_1
    //   539: istore 16
    //   541: goto +6 -> 547
    //   544: iconst_0
    //   545: istore 16
    //   547: aload_1
    //   548: iload 16
    //   550: invokevirtual 2437	android/view/View:dispatchVisibilityAggregated	(Z)Z
    //   553: pop
    //   554: iload 5
    //   556: ifne +10 -> 566
    //   559: aload_0
    //   560: getfield 2379	android/view/ViewRootImpl:mNewSurfaceNeeded	Z
    //   563: ifeq +11 -> 574
    //   566: aload_0
    //   567: invokespecial 2439	android/view/ViewRootImpl:endDragResizing	()V
    //   570: aload_0
    //   571: invokevirtual 2441	android/view/ViewRootImpl:destroyHardwareResources	()V
    //   574: iload 5
    //   576: bipush 8
    //   578: if_icmpne +8 -> 586
    //   581: aload_0
    //   582: iconst_0
    //   583: putfield 2096	android/view/ViewRootImpl:mHasHadWindowFocus	Z
    //   586: aload_0
    //   587: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   590: getfield 2409	android/view/View$AttachInfo:mWindowVisibility	I
    //   593: ifeq +7 -> 600
    //   596: aload_1
    //   597: invokevirtual 2444	android/view/View:clearAccessibilityFocus	()V
    //   600: invokestatic 2362	android/view/ViewRootImpl:getRunQueue	()Landroid/view/HandlerActionQueue;
    //   603: aload_0
    //   604: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   607: getfield 2447	android/view/View$AttachInfo:mHandler	Landroid/os/Handler;
    //   610: invokevirtual 2451	android/view/HandlerActionQueue:executeActions	(Landroid/os/Handler;)V
    //   613: iconst_0
    //   614: istore 9
    //   616: iconst_0
    //   617: istore 15
    //   619: aload_0
    //   620: getfield 1459	android/view/ViewRootImpl:mLayoutRequested	Z
    //   623: ifeq +23 -> 646
    //   626: aload_0
    //   627: getfield 522	android/view/ViewRootImpl:mStopped	Z
    //   630: ifeq +10 -> 640
    //   633: aload_0
    //   634: getfield 1356	android/view/ViewRootImpl:mReportNextDraw	Z
    //   637: ifeq +9 -> 646
    //   640: iconst_1
    //   641: istore 14
    //   643: goto +6 -> 649
    //   646: iconst_0
    //   647: istore 14
    //   649: iload 14
    //   651: istore 17
    //   653: iload 17
    //   655: ifeq +350 -> 1005
    //   658: aload_0
    //   659: getfield 1019	android/view/ViewRootImpl:mView	Landroid/view/View;
    //   662: invokevirtual 1806	android/view/View:getContext	()Landroid/content/Context;
    //   665: invokevirtual 783	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   668: astore 13
    //   670: aload_0
    //   671: getfield 743	android/view/ViewRootImpl:mFirst	Z
    //   674: ifeq +34 -> 708
    //   677: aload_0
    //   678: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   681: aload_0
    //   682: getfield 2453	android/view/ViewRootImpl:mAddedTouchMode	Z
    //   685: iconst_1
    //   686: ixor
    //   687: putfield 989	android/view/View$AttachInfo:mInTouchMode	Z
    //   690: aload_0
    //   691: aload_0
    //   692: getfield 2453	android/view/ViewRootImpl:mAddedTouchMode	Z
    //   695: invokespecial 2043	android/view/ViewRootImpl:ensureTouchModeLocally	(Z)Z
    //   698: pop
    //   699: iload_2
    //   700: istore 9
    //   702: iload 15
    //   704: istore_2
    //   705: goto +260 -> 965
    //   708: aload_0
    //   709: getfield 551	android/view/ViewRootImpl:mPendingOverscanInsets	Landroid/graphics/Rect;
    //   712: aload_0
    //   713: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   716: getfield 2456	android/view/View$AttachInfo:mOverscanInsets	Landroid/graphics/Rect;
    //   719: invokevirtual 1118	android/graphics/Rect:equals	(Ljava/lang/Object;)Z
    //   722: ifne +6 -> 728
    //   725: iconst_1
    //   726: istore 9
    //   728: aload_0
    //   729: getfield 557	android/view/ViewRootImpl:mPendingContentInsets	Landroid/graphics/Rect;
    //   732: aload_0
    //   733: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   736: getfield 2459	android/view/View$AttachInfo:mContentInsets	Landroid/graphics/Rect;
    //   739: invokevirtual 1118	android/graphics/Rect:equals	(Ljava/lang/Object;)Z
    //   742: ifne +6 -> 748
    //   745: iconst_1
    //   746: istore 9
    //   748: aload_0
    //   749: getfield 555	android/view/ViewRootImpl:mPendingStableInsets	Landroid/graphics/Rect;
    //   752: aload_0
    //   753: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   756: getfield 2462	android/view/View$AttachInfo:mStableInsets	Landroid/graphics/Rect;
    //   759: invokevirtual 1118	android/graphics/Rect:equals	(Ljava/lang/Object;)Z
    //   762: ifne +6 -> 768
    //   765: iconst_1
    //   766: istore 9
    //   768: aload_0
    //   769: getfield 573	android/view/ViewRootImpl:mPendingDisplayCutout	Landroid/view/DisplayCutout$ParcelableWrapper;
    //   772: aload_0
    //   773: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   776: getfield 2465	android/view/View$AttachInfo:mDisplayCutout	Landroid/view/DisplayCutout$ParcelableWrapper;
    //   779: invokevirtual 2466	android/view/DisplayCutout$ParcelableWrapper:equals	(Ljava/lang/Object;)Z
    //   782: ifne +6 -> 788
    //   785: iconst_1
    //   786: istore 9
    //   788: aload_0
    //   789: getfield 553	android/view/ViewRootImpl:mPendingVisibleInsets	Landroid/graphics/Rect;
    //   792: aload_0
    //   793: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   796: getfield 2469	android/view/View$AttachInfo:mVisibleInsets	Landroid/graphics/Rect;
    //   799: invokevirtual 1118	android/graphics/Rect:equals	(Ljava/lang/Object;)Z
    //   802: ifne +17 -> 819
    //   805: aload_0
    //   806: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   809: getfield 2469	android/view/View$AttachInfo:mVisibleInsets	Landroid/graphics/Rect;
    //   812: aload_0
    //   813: getfield 553	android/view/ViewRootImpl:mPendingVisibleInsets	Landroid/graphics/Rect;
    //   816: invokevirtual 2471	android/graphics/Rect:set	(Landroid/graphics/Rect;)V
    //   819: aload_0
    //   820: getfield 559	android/view/ViewRootImpl:mPendingOutsets	Landroid/graphics/Rect;
    //   823: aload_0
    //   824: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   827: getfield 2474	android/view/View$AttachInfo:mOutsets	Landroid/graphics/Rect;
    //   830: invokevirtual 1118	android/graphics/Rect:equals	(Ljava/lang/Object;)Z
    //   833: ifne +6 -> 839
    //   836: iconst_1
    //   837: istore 9
    //   839: aload_0
    //   840: getfield 2476	android/view/ViewRootImpl:mPendingAlwaysConsumeNavBar	Z
    //   843: aload_0
    //   844: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   847: getfield 2479	android/view/View$AttachInfo:mAlwaysConsumeNavBar	Z
    //   850: if_icmpeq +6 -> 856
    //   853: iconst_1
    //   854: istore 9
    //   856: aload 4
    //   858: getfield 2182	android/view/WindowManager$LayoutParams:width	I
    //   861: bipush -2
    //   863: if_icmpeq +17 -> 880
    //   866: iload 9
    //   868: istore 15
    //   870: aload 4
    //   872: getfield 2199	android/view/WindowManager$LayoutParams:height	I
    //   875: bipush -2
    //   877: if_icmpne -178 -> 699
    //   880: aload 4
    //   882: invokestatic 2388	android/view/ViewRootImpl:shouldUseDisplaySize	(Landroid/view/WindowManager$LayoutParams;)Z
    //   885: ifeq +48 -> 933
    //   888: new 2390	android/graphics/Point
    //   891: dup
    //   892: invokespecial 2391	android/graphics/Point:<init>	()V
    //   895: astore 12
    //   897: aload_0
    //   898: getfield 679	android/view/ViewRootImpl:mDisplay	Landroid/view/Display;
    //   901: aload 12
    //   903: invokevirtual 2395	android/view/Display:getRealSize	(Landroid/graphics/Point;)V
    //   906: aload 12
    //   908: getfield 2397	android/graphics/Point:x	I
    //   911: istore 7
    //   913: aload 12
    //   915: getfield 2399	android/graphics/Point:y	I
    //   918: istore 8
    //   920: iconst_1
    //   921: istore 15
    //   923: iload 9
    //   925: istore_2
    //   926: iload 15
    //   928: istore 9
    //   930: goto +35 -> 965
    //   933: aload 13
    //   935: invokevirtual 1521	android/content/res/Resources:getConfiguration	()Landroid/content/res/Configuration;
    //   938: astore 12
    //   940: aload_0
    //   941: aload 12
    //   943: getfield 2482	android/content/res/Configuration:screenWidthDp	I
    //   946: invokespecial 2484	android/view/ViewRootImpl:dipToPx	(I)I
    //   949: istore 7
    //   951: aload_0
    //   952: aload 12
    //   954: getfield 2487	android/content/res/Configuration:screenHeightDp	I
    //   957: invokespecial 2484	android/view/ViewRootImpl:dipToPx	(I)I
    //   960: istore 8
    //   962: goto -42 -> 920
    //   965: aload_0
    //   966: aload_1
    //   967: aload 4
    //   969: aload 13
    //   971: iload 7
    //   973: iload 8
    //   975: invokespecial 2360	android/view/ViewRootImpl:measureHierarchy	(Landroid/view/View;Landroid/view/WindowManager$LayoutParams;Landroid/content/res/Resources;II)Z
    //   978: istore 16
    //   980: iload 7
    //   982: istore 15
    //   984: iload 9
    //   986: iload 16
    //   988: ior
    //   989: istore 9
    //   991: iload 8
    //   993: istore 7
    //   995: iload 9
    //   997: istore 8
    //   999: iload_2
    //   1000: istore 9
    //   1002: goto +23 -> 1025
    //   1005: iload_2
    //   1006: istore 15
    //   1008: iconst_0
    //   1009: istore 9
    //   1011: iload 8
    //   1013: istore_2
    //   1014: iload 15
    //   1016: istore 8
    //   1018: iload 7
    //   1020: istore 15
    //   1022: iload_2
    //   1023: istore 7
    //   1025: iconst_0
    //   1026: istore 18
    //   1028: iconst_0
    //   1029: istore 19
    //   1031: iconst_0
    //   1032: istore 20
    //   1034: iconst_0
    //   1035: istore 21
    //   1037: iconst_0
    //   1038: istore 22
    //   1040: aload_0
    //   1041: invokespecial 2489	android/view/ViewRootImpl:collectViewAttributes	()Z
    //   1044: ifeq +7 -> 1051
    //   1047: aload 4
    //   1049: astore 11
    //   1051: aload_0
    //   1052: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   1055: getfield 2492	android/view/View$AttachInfo:mForceReportNewAttributes	Z
    //   1058: ifeq +15 -> 1073
    //   1061: aload_0
    //   1062: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   1065: iconst_0
    //   1066: putfield 2492	android/view/View$AttachInfo:mForceReportNewAttributes	Z
    //   1069: aload 4
    //   1071: astore 11
    //   1073: aload_0
    //   1074: getfield 743	android/view/ViewRootImpl:mFirst	Z
    //   1077: ifne +17 -> 1094
    //   1080: aload 11
    //   1082: astore 13
    //   1084: aload_0
    //   1085: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   1088: getfield 2495	android/view/View$AttachInfo:mViewVisibilityChanged	Z
    //   1091: ifeq +130 -> 1221
    //   1094: aload_0
    //   1095: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   1098: iconst_0
    //   1099: putfield 2495	android/view/View$AttachInfo:mViewVisibilityChanged	Z
    //   1102: aload_0
    //   1103: getfield 2497	android/view/ViewRootImpl:mSoftInputMode	I
    //   1106: sipush 240
    //   1109: iand
    //   1110: istore_2
    //   1111: aload 11
    //   1113: astore 13
    //   1115: iload_2
    //   1116: ifne +105 -> 1221
    //   1119: aload_0
    //   1120: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   1123: getfield 2500	android/view/View$AttachInfo:mScrollContainers	Ljava/util/ArrayList;
    //   1126: invokevirtual 1121	java/util/ArrayList:size	()I
    //   1129: istore 23
    //   1131: iconst_0
    //   1132: istore 24
    //   1134: iload 24
    //   1136: iload 23
    //   1138: if_icmpge +33 -> 1171
    //   1141: aload_0
    //   1142: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   1145: getfield 2500	android/view/View$AttachInfo:mScrollContainers	Ljava/util/ArrayList;
    //   1148: iload 24
    //   1150: invokevirtual 1124	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   1153: checkcast 1021	android/view/View
    //   1156: invokevirtual 2503	android/view/View:isShown	()Z
    //   1159: ifeq +6 -> 1165
    //   1162: bipush 16
    //   1164: istore_2
    //   1165: iinc 24 1
    //   1168: goto -34 -> 1134
    //   1171: iload_2
    //   1172: istore 24
    //   1174: iload_2
    //   1175: ifne +7 -> 1182
    //   1178: bipush 32
    //   1180: istore 24
    //   1182: aload 11
    //   1184: astore 13
    //   1186: aload 4
    //   1188: getfield 2094	android/view/WindowManager$LayoutParams:softInputMode	I
    //   1191: sipush 240
    //   1194: iand
    //   1195: iload 24
    //   1197: if_icmpeq +24 -> 1221
    //   1200: aload 4
    //   1202: aload 4
    //   1204: getfield 2094	android/view/WindowManager$LayoutParams:softInputMode	I
    //   1207: sipush 65295
    //   1210: iand
    //   1211: iload 24
    //   1213: ior
    //   1214: putfield 2094	android/view/WindowManager$LayoutParams:softInputMode	I
    //   1217: aload 4
    //   1219: astore 13
    //   1221: aload 13
    //   1223: ifnull +66 -> 1289
    //   1226: aload_1
    //   1227: getfield 1430	android/view/View:mPrivateFlags	I
    //   1230: sipush 512
    //   1233: iand
    //   1234: ifeq +21 -> 1255
    //   1237: aload 13
    //   1239: getfield 1517	android/view/WindowManager$LayoutParams:format	I
    //   1242: invokestatic 2508	android/graphics/PixelFormat:formatHasAlpha	(I)Z
    //   1245: ifne +10 -> 1255
    //   1248: aload 13
    //   1250: bipush -3
    //   1252: putfield 1517	android/view/WindowManager$LayoutParams:format	I
    //   1255: aload_0
    //   1256: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   1259: astore 11
    //   1261: aload 13
    //   1263: getfield 984	android/view/WindowManager$LayoutParams:flags	I
    //   1266: ldc_w 2509
    //   1269: iand
    //   1270: ifeq +9 -> 1279
    //   1273: iconst_1
    //   1274: istore 16
    //   1276: goto +6 -> 1282
    //   1279: iconst_0
    //   1280: istore 16
    //   1282: aload 11
    //   1284: iload 16
    //   1286: putfield 2512	android/view/View$AttachInfo:mOverscanRequested	Z
    //   1289: aload_0
    //   1290: getfield 2514	android/view/ViewRootImpl:mApplyInsetsRequested	Z
    //   1293: ifeq +60 -> 1353
    //   1296: aload_0
    //   1297: iconst_0
    //   1298: putfield 2514	android/view/ViewRootImpl:mApplyInsetsRequested	Z
    //   1301: aload_0
    //   1302: aload_0
    //   1303: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   1306: getfield 2512	android/view/View$AttachInfo:mOverscanRequested	Z
    //   1309: putfield 2516	android/view/ViewRootImpl:mLastOverscanRequested	Z
    //   1312: aload_0
    //   1313: aload_1
    //   1314: invokevirtual 2431	android/view/ViewRootImpl:dispatchApplyInsets	(Landroid/view/View;)V
    //   1317: aload_0
    //   1318: getfield 1459	android/view/ViewRootImpl:mLayoutRequested	Z
    //   1321: ifeq +32 -> 1353
    //   1324: iload 8
    //   1326: aload_0
    //   1327: aload_1
    //   1328: aload 4
    //   1330: aload_0
    //   1331: getfield 1019	android/view/ViewRootImpl:mView	Landroid/view/View;
    //   1334: invokevirtual 1806	android/view/View:getContext	()Landroid/content/Context;
    //   1337: invokevirtual 783	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   1340: iload 15
    //   1342: iload 7
    //   1344: invokespecial 2360	android/view/ViewRootImpl:measureHierarchy	(Landroid/view/View;Landroid/view/WindowManager$LayoutParams;Landroid/content/res/Resources;II)Z
    //   1347: ior
    //   1348: istore 8
    //   1350: goto +3 -> 1353
    //   1353: iload 17
    //   1355: ifeq +8 -> 1363
    //   1358: aload_0
    //   1359: iconst_0
    //   1360: putfield 1459	android/view/ViewRootImpl:mLayoutRequested	Z
    //   1363: iload 17
    //   1365: ifeq +120 -> 1485
    //   1368: iload 8
    //   1370: ifeq +115 -> 1485
    //   1373: aload_0
    //   1374: getfield 708	android/view/ViewRootImpl:mWidth	I
    //   1377: aload_1
    //   1378: invokevirtual 2208	android/view/View:getMeasuredWidth	()I
    //   1381: if_icmpne +98 -> 1479
    //   1384: aload_0
    //   1385: getfield 710	android/view/ViewRootImpl:mHeight	I
    //   1388: aload_1
    //   1389: invokevirtual 2211	android/view/View:getMeasuredHeight	()I
    //   1392: if_icmpne +87 -> 1479
    //   1395: aload 4
    //   1397: getfield 2182	android/view/WindowManager$LayoutParams:width	I
    //   1400: bipush -2
    //   1402: if_icmpne +35 -> 1437
    //   1405: aload 10
    //   1407: astore 11
    //   1409: aload 11
    //   1411: invokevirtual 2401	android/graphics/Rect:width	()I
    //   1414: iload 15
    //   1416: if_icmpge +21 -> 1437
    //   1419: aload 11
    //   1421: invokevirtual 2401	android/graphics/Rect:width	()I
    //   1424: aload_0
    //   1425: getfield 708	android/view/ViewRootImpl:mWidth	I
    //   1428: if_icmpne +6 -> 1434
    //   1431: goto +6 -> 1437
    //   1434: goto +45 -> 1479
    //   1437: aload 10
    //   1439: astore 11
    //   1441: aload 4
    //   1443: getfield 2199	android/view/WindowManager$LayoutParams:height	I
    //   1446: bipush -2
    //   1448: if_icmpne +28 -> 1476
    //   1451: aload 11
    //   1453: invokevirtual 2403	android/graphics/Rect:height	()I
    //   1456: iload 7
    //   1458: if_icmpge +27 -> 1485
    //   1461: aload 11
    //   1463: invokevirtual 2403	android/graphics/Rect:height	()I
    //   1466: aload_0
    //   1467: getfield 710	android/view/ViewRootImpl:mHeight	I
    //   1470: if_icmpeq +15 -> 1485
    //   1473: goto +6 -> 1479
    //   1476: goto +9 -> 1485
    //   1479: iconst_1
    //   1480: istore 7
    //   1482: goto +6 -> 1488
    //   1485: iconst_0
    //   1486: istore 7
    //   1488: aload_0
    //   1489: getfield 1113	android/view/ViewRootImpl:mDragResizing	Z
    //   1492: ifeq +16 -> 1508
    //   1495: aload_0
    //   1496: getfield 2518	android/view/ViewRootImpl:mResizeMode	I
    //   1499: ifne +9 -> 1508
    //   1502: iconst_1
    //   1503: istore 8
    //   1505: goto +6 -> 1511
    //   1508: iconst_0
    //   1509: istore 8
    //   1511: aload_0
    //   1512: getfield 2520	android/view/ViewRootImpl:mActivityRelaunched	Z
    //   1515: istore 16
    //   1517: aload_0
    //   1518: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   1521: getfield 1240	android/view/View$AttachInfo:mTreeObserver	Landroid/view/ViewTreeObserver;
    //   1524: invokevirtual 2523	android/view/ViewTreeObserver:hasComputeInternalInsetsListeners	()Z
    //   1527: ifne +22 -> 1549
    //   1530: aload_0
    //   1531: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   1534: getfield 2526	android/view/View$AttachInfo:mHasNonEmptyGivenInternalInsets	Z
    //   1537: ifeq +6 -> 1543
    //   1540: goto +9 -> 1549
    //   1543: iconst_0
    //   1544: istore 25
    //   1546: goto +6 -> 1552
    //   1549: iconst_1
    //   1550: istore 25
    //   1552: iconst_0
    //   1553: istore 15
    //   1555: iconst_0
    //   1556: istore_2
    //   1557: aload_0
    //   1558: getfield 546	android/view/ViewRootImpl:mSurface	Landroid/view/Surface;
    //   1561: invokevirtual 2529	android/view/Surface:getGenerationId	()I
    //   1564: istore 26
    //   1566: iload 5
    //   1568: ifne +9 -> 1577
    //   1571: iconst_1
    //   1572: istore 23
    //   1574: goto +6 -> 1580
    //   1577: iconst_0
    //   1578: istore 23
    //   1580: aload_0
    //   1581: getfield 2531	android/view/ViewRootImpl:mForceNextWindowRelayout	Z
    //   1584: istore 27
    //   1586: aload_0
    //   1587: getfield 743	android/view/ViewRootImpl:mFirst	Z
    //   1590: ifne +68 -> 1658
    //   1593: iload 7
    //   1595: iload 8
    //   1597: ior
    //   1598: iload 16
    //   1600: ior
    //   1601: ifne +57 -> 1658
    //   1604: iload 9
    //   1606: ifne +52 -> 1658
    //   1609: iload 6
    //   1611: ifne +47 -> 1658
    //   1614: aload 13
    //   1616: ifnonnull +39 -> 1655
    //   1619: aload_0
    //   1620: getfield 2531	android/view/ViewRootImpl:mForceNextWindowRelayout	Z
    //   1623: ifeq +6 -> 1629
    //   1626: goto +32 -> 1658
    //   1629: aload_0
    //   1630: aload 10
    //   1632: invokespecial 952	android/view/ViewRootImpl:maybeHandleWindowMove	(Landroid/graphics/Rect;)V
    //   1635: iconst_0
    //   1636: istore 28
    //   1638: iload 15
    //   1640: istore 7
    //   1642: iload_2
    //   1643: istore 9
    //   1645: iload 17
    //   1647: istore 8
    //   1649: iload 21
    //   1651: istore_2
    //   1652: goto +2934 -> 4586
    //   1655: goto +3 -> 1658
    //   1658: aload_0
    //   1659: iconst_0
    //   1660: putfield 2531	android/view/ViewRootImpl:mForceNextWindowRelayout	Z
    //   1663: iload 23
    //   1665: ifeq +36 -> 1701
    //   1668: iload 25
    //   1670: ifeq +21 -> 1691
    //   1673: aload_0
    //   1674: getfield 743	android/view/ViewRootImpl:mFirst	Z
    //   1677: ifne +8 -> 1685
    //   1680: iload 6
    //   1682: ifeq +9 -> 1691
    //   1685: iconst_1
    //   1686: istore 16
    //   1688: goto +6 -> 1694
    //   1691: iconst_0
    //   1692: istore 16
    //   1694: iload 16
    //   1696: istore 28
    //   1698: goto +6 -> 1704
    //   1701: iconst_0
    //   1702: istore 28
    //   1704: aload_0
    //   1705: getfield 1272	android/view/ViewRootImpl:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
    //   1708: ifnull +21 -> 1729
    //   1711: aload_0
    //   1712: getfield 1272	android/view/ViewRootImpl:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
    //   1715: getfield 2535	com/android/internal/view/BaseSurfaceHolder:mSurfaceLock	Ljava/util/concurrent/locks/ReentrantLock;
    //   1718: invokevirtual 2540	java/util/concurrent/locks/ReentrantLock:lock	()V
    //   1721: aload_0
    //   1722: iconst_1
    //   1723: putfield 2542	android/view/ViewRootImpl:mDrawingAllowed	Z
    //   1726: goto +3 -> 1729
    //   1729: iconst_0
    //   1730: istore 7
    //   1732: iconst_0
    //   1733: istore 24
    //   1735: iconst_0
    //   1736: istore 8
    //   1738: iconst_0
    //   1739: istore 29
    //   1741: iconst_0
    //   1742: istore 21
    //   1744: iconst_0
    //   1745: istore 15
    //   1747: iconst_0
    //   1748: istore 30
    //   1750: aload_0
    //   1751: getfield 546	android/view/ViewRootImpl:mSurface	Landroid/view/Surface;
    //   1754: invokevirtual 1216	android/view/Surface:isValid	()Z
    //   1757: istore 31
    //   1759: aload_0
    //   1760: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   1763: getfield 1090	android/view/View$AttachInfo:mThreadedRenderer	Landroid/view/ThreadedRenderer;
    //   1766: astore 11
    //   1768: aload 11
    //   1770: ifnull +177 -> 1947
    //   1773: aload_0
    //   1774: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   1777: getfield 1090	android/view/View$AttachInfo:mThreadedRenderer	Landroid/view/ThreadedRenderer;
    //   1780: astore 11
    //   1782: iconst_0
    //   1783: istore 16
    //   1785: aload 11
    //   1787: aload_0
    //   1788: getfield 546	android/view/ViewRootImpl:mSurface	Landroid/view/Surface;
    //   1791: invokevirtual 2546	android/view/ThreadedRenderer:pauseSurface	(Landroid/view/Surface;)Z
    //   1794: istore 32
    //   1796: iload 32
    //   1798: ifeq +68 -> 1866
    //   1801: aload_0
    //   1802: getfield 712	android/view/ViewRootImpl:mDirty	Landroid/graphics/Rect;
    //   1805: astore 11
    //   1807: aload_0
    //   1808: getfield 708	android/view/ViewRootImpl:mWidth	I
    //   1811: istore 9
    //   1813: aload_0
    //   1814: getfield 710	android/view/ViewRootImpl:mHeight	I
    //   1817: istore_2
    //   1818: aload 11
    //   1820: iconst_0
    //   1821: iconst_0
    //   1822: iload 9
    //   1824: iload_2
    //   1825: invokevirtual 1285	android/graphics/Rect:set	(IIII)V
    //   1828: goto +38 -> 1866
    //   1831: astore 13
    //   1833: goto +53 -> 1886
    //   1836: astore 13
    //   1838: iconst_0
    //   1839: istore 7
    //   1841: iload 20
    //   1843: istore_2
    //   1844: iload 15
    //   1846: istore 9
    //   1848: goto +2005 -> 3853
    //   1851: astore 13
    //   1853: iconst_0
    //   1854: istore 7
    //   1856: iload 20
    //   1858: istore_2
    //   1859: iload 15
    //   1861: istore 9
    //   1863: goto +1990 -> 3853
    //   1866: aload_0
    //   1867: getfield 815	android/view/ViewRootImpl:mChoreographer	Landroid/view/Choreographer;
    //   1870: getfield 2550	android/view/Choreographer:mFrameInfo	Landroid/view/FrameInfo;
    //   1873: astore 11
    //   1875: aload 11
    //   1877: lconst_1
    //   1878: invokevirtual 2555	android/view/FrameInfo:addFlags	(J)V
    //   1881: goto +66 -> 1947
    //   1884: astore 13
    //   1886: iconst_0
    //   1887: istore 7
    //   1889: iload 20
    //   1891: istore_2
    //   1892: iload 15
    //   1894: istore 9
    //   1896: goto +1957 -> 3853
    //   1899: astore 13
    //   1901: iconst_0
    //   1902: istore 7
    //   1904: iload 20
    //   1906: istore_2
    //   1907: iload 15
    //   1909: istore 9
    //   1911: goto +1942 -> 3853
    //   1914: astore 13
    //   1916: iconst_0
    //   1917: istore 7
    //   1919: iload 20
    //   1921: istore_2
    //   1922: iload 15
    //   1924: istore 9
    //   1926: goto +1927 -> 3853
    //   1929: astore 13
    //   1931: iconst_0
    //   1932: istore 16
    //   1934: iconst_0
    //   1935: istore 7
    //   1937: iload 20
    //   1939: istore_2
    //   1940: iload 15
    //   1942: istore 9
    //   1944: goto +1909 -> 3853
    //   1947: iconst_0
    //   1948: istore 33
    //   1950: iconst_0
    //   1951: istore 34
    //   1953: iconst_0
    //   1954: istore 35
    //   1956: iconst_0
    //   1957: istore 36
    //   1959: iconst_0
    //   1960: istore 16
    //   1962: iconst_0
    //   1963: istore 37
    //   1965: aload_0
    //   1966: aload 13
    //   1968: iload 5
    //   1970: iload 28
    //   1972: invokespecial 2559	android/view/ViewRootImpl:relayoutWindow	(Landroid/view/WindowManager$LayoutParams;IZ)I
    //   1975: istore 9
    //   1977: iload_3
    //   1978: istore 20
    //   1980: iload 19
    //   1982: istore_2
    //   1983: iload 21
    //   1985: istore 17
    //   1987: iload 36
    //   1989: istore 32
    //   1991: aload_0
    //   1992: getfield 596	android/view/ViewRootImpl:mPendingMergedConfiguration	Landroid/util/MergedConfiguration;
    //   1995: aload_0
    //   1996: getfield 594	android/view/ViewRootImpl:mLastReportedMergedConfiguration	Landroid/util/MergedConfiguration;
    //   1999: invokevirtual 2560	android/util/MergedConfiguration:equals	(Ljava/lang/Object;)Z
    //   2002: istore 16
    //   2004: iload 16
    //   2006: ifne +72 -> 2078
    //   2009: iload_3
    //   2010: istore 17
    //   2012: iload 30
    //   2014: istore_2
    //   2015: iload 29
    //   2017: istore 24
    //   2019: aload_0
    //   2020: aload_0
    //   2021: getfield 596	android/view/ViewRootImpl:mPendingMergedConfiguration	Landroid/util/MergedConfiguration;
    //   2024: aload_0
    //   2025: getfield 743	android/view/ViewRootImpl:mFirst	Z
    //   2028: iconst_1
    //   2029: ixor
    //   2030: iconst_m1
    //   2031: invokespecial 929	android/view/ViewRootImpl:performConfigurationChange	(Landroid/util/MergedConfiguration;ZI)V
    //   2034: iconst_1
    //   2035: istore 7
    //   2037: goto +41 -> 2078
    //   2040: astore 13
    //   2042: iload 37
    //   2044: istore 16
    //   2046: iload 24
    //   2048: istore 7
    //   2050: iload_2
    //   2051: istore 8
    //   2053: iload 22
    //   2055: istore_2
    //   2056: iload 17
    //   2058: istore_3
    //   2059: iload 9
    //   2061: istore 15
    //   2063: iload 8
    //   2065: istore 9
    //   2067: iload 7
    //   2069: istore 8
    //   2071: iload 15
    //   2073: istore 7
    //   2075: goto +1778 -> 3853
    //   2078: iload_3
    //   2079: istore 20
    //   2081: iload 19
    //   2083: istore_2
    //   2084: iload 21
    //   2086: istore 17
    //   2088: iload 7
    //   2090: istore 24
    //   2092: iload 36
    //   2094: istore 32
    //   2096: aload_0
    //   2097: getfield 551	android/view/ViewRootImpl:mPendingOverscanInsets	Landroid/graphics/Rect;
    //   2100: aload_0
    //   2101: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   2104: getfield 2456	android/view/View$AttachInfo:mOverscanInsets	Landroid/graphics/Rect;
    //   2107: invokevirtual 1118	android/graphics/Rect:equals	(Ljava/lang/Object;)Z
    //   2110: istore 38
    //   2112: iload_3
    //   2113: istore 20
    //   2115: iload 19
    //   2117: istore_2
    //   2118: iload 21
    //   2120: istore 17
    //   2122: iload 7
    //   2124: istore 24
    //   2126: iload 36
    //   2128: istore 32
    //   2130: aload_0
    //   2131: getfield 557	android/view/ViewRootImpl:mPendingContentInsets	Landroid/graphics/Rect;
    //   2134: aload_0
    //   2135: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   2138: getfield 2459	android/view/View$AttachInfo:mContentInsets	Landroid/graphics/Rect;
    //   2141: invokevirtual 1118	android/graphics/Rect:equals	(Ljava/lang/Object;)Z
    //   2144: iconst_1
    //   2145: ixor
    //   2146: istore 8
    //   2148: iload_3
    //   2149: istore 20
    //   2151: iload 19
    //   2153: istore_2
    //   2154: iload 8
    //   2156: istore 17
    //   2158: iload 7
    //   2160: istore 24
    //   2162: iload 36
    //   2164: istore 32
    //   2166: aload_0
    //   2167: getfield 553	android/view/ViewRootImpl:mPendingVisibleInsets	Landroid/graphics/Rect;
    //   2170: aload_0
    //   2171: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   2174: getfield 2469	android/view/View$AttachInfo:mVisibleInsets	Landroid/graphics/Rect;
    //   2177: invokevirtual 1118	android/graphics/Rect:equals	(Ljava/lang/Object;)Z
    //   2180: istore 16
    //   2182: iload_3
    //   2183: istore 20
    //   2185: iload 19
    //   2187: istore_2
    //   2188: iload 8
    //   2190: istore 17
    //   2192: iload 7
    //   2194: istore 24
    //   2196: iload 36
    //   2198: istore 32
    //   2200: aload_0
    //   2201: getfield 555	android/view/ViewRootImpl:mPendingStableInsets	Landroid/graphics/Rect;
    //   2204: aload_0
    //   2205: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   2208: getfield 2462	android/view/View$AttachInfo:mStableInsets	Landroid/graphics/Rect;
    //   2211: invokevirtual 1118	android/graphics/Rect:equals	(Ljava/lang/Object;)Z
    //   2214: istore 39
    //   2216: iload_3
    //   2217: istore 20
    //   2219: iload 19
    //   2221: istore_2
    //   2222: iload 8
    //   2224: istore 17
    //   2226: iload 7
    //   2228: istore 24
    //   2230: iload 36
    //   2232: istore 32
    //   2234: aload_0
    //   2235: getfield 573	android/view/ViewRootImpl:mPendingDisplayCutout	Landroid/view/DisplayCutout$ParcelableWrapper;
    //   2238: aload_0
    //   2239: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   2242: getfield 2465	android/view/View$AttachInfo:mDisplayCutout	Landroid/view/DisplayCutout$ParcelableWrapper;
    //   2245: invokevirtual 2466	android/view/DisplayCutout$ParcelableWrapper:equals	(Ljava/lang/Object;)Z
    //   2248: istore 40
    //   2250: iload_3
    //   2251: istore 20
    //   2253: iload 19
    //   2255: istore_2
    //   2256: iload 8
    //   2258: istore 17
    //   2260: iload 7
    //   2262: istore 24
    //   2264: iload 36
    //   2266: istore 32
    //   2268: aload_0
    //   2269: getfield 559	android/view/ViewRootImpl:mPendingOutsets	Landroid/graphics/Rect;
    //   2272: aload_0
    //   2273: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   2276: getfield 2474	android/view/View$AttachInfo:mOutsets	Landroid/graphics/Rect;
    //   2279: invokevirtual 1118	android/graphics/Rect:equals	(Ljava/lang/Object;)Z
    //   2282: istore 41
    //   2284: iload 9
    //   2286: bipush 32
    //   2288: iand
    //   2289: ifeq +9 -> 2298
    //   2292: iconst_1
    //   2293: istore 15
    //   2295: goto +6 -> 2301
    //   2298: iconst_0
    //   2299: istore 15
    //   2301: iload_3
    //   2302: iload 15
    //   2304: ior
    //   2305: istore_3
    //   2306: iload_3
    //   2307: istore 20
    //   2309: iload 19
    //   2311: istore_2
    //   2312: iload 8
    //   2314: istore 17
    //   2316: iload 7
    //   2318: istore 24
    //   2320: iload 36
    //   2322: istore 32
    //   2324: aload_0
    //   2325: getfield 2476	android/view/ViewRootImpl:mPendingAlwaysConsumeNavBar	Z
    //   2328: istore 42
    //   2330: iload_3
    //   2331: istore 20
    //   2333: iload 19
    //   2335: istore_2
    //   2336: iload 8
    //   2338: istore 17
    //   2340: iload 7
    //   2342: istore 24
    //   2344: iload 36
    //   2346: istore 32
    //   2348: aload_0
    //   2349: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   2352: getfield 2479	android/view/View$AttachInfo:mAlwaysConsumeNavBar	Z
    //   2355: istore 43
    //   2357: iload 42
    //   2359: iload 43
    //   2361: if_icmpeq +9 -> 2370
    //   2364: iconst_1
    //   2365: istore 20
    //   2367: goto +6 -> 2373
    //   2370: iconst_0
    //   2371: istore 20
    //   2373: iload 8
    //   2375: ifeq +27 -> 2402
    //   2378: iload_3
    //   2379: istore 17
    //   2381: iload 8
    //   2383: istore_2
    //   2384: iload 7
    //   2386: istore 24
    //   2388: aload_0
    //   2389: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   2392: getfield 2459	android/view/View$AttachInfo:mContentInsets	Landroid/graphics/Rect;
    //   2395: aload_0
    //   2396: getfield 557	android/view/ViewRootImpl:mPendingContentInsets	Landroid/graphics/Rect;
    //   2399: invokevirtual 2471	android/graphics/Rect:set	(Landroid/graphics/Rect;)V
    //   2402: iload 8
    //   2404: istore_2
    //   2405: iload 38
    //   2407: iconst_1
    //   2408: ixor
    //   2409: ifeq +29 -> 2438
    //   2412: iload_3
    //   2413: istore 17
    //   2415: iload 8
    //   2417: istore_2
    //   2418: iload 7
    //   2420: istore 24
    //   2422: aload_0
    //   2423: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   2426: getfield 2456	android/view/View$AttachInfo:mOverscanInsets	Landroid/graphics/Rect;
    //   2429: aload_0
    //   2430: getfield 551	android/view/ViewRootImpl:mPendingOverscanInsets	Landroid/graphics/Rect;
    //   2433: invokevirtual 2471	android/graphics/Rect:set	(Landroid/graphics/Rect;)V
    //   2436: iconst_1
    //   2437: istore_2
    //   2438: iload_2
    //   2439: istore 8
    //   2441: iload 39
    //   2443: iconst_1
    //   2444: ixor
    //   2445: ifeq +27 -> 2472
    //   2448: iload_3
    //   2449: istore 17
    //   2451: iload 7
    //   2453: istore 24
    //   2455: aload_0
    //   2456: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   2459: getfield 2462	android/view/View$AttachInfo:mStableInsets	Landroid/graphics/Rect;
    //   2462: aload_0
    //   2463: getfield 555	android/view/ViewRootImpl:mPendingStableInsets	Landroid/graphics/Rect;
    //   2466: invokevirtual 2471	android/graphics/Rect:set	(Landroid/graphics/Rect;)V
    //   2469: iconst_1
    //   2470: istore 8
    //   2472: iload 8
    //   2474: istore_2
    //   2475: iload 40
    //   2477: iconst_1
    //   2478: ixor
    //   2479: ifeq +29 -> 2508
    //   2482: iload_3
    //   2483: istore 17
    //   2485: iload 8
    //   2487: istore_2
    //   2488: iload 7
    //   2490: istore 24
    //   2492: aload_0
    //   2493: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   2496: getfield 2465	android/view/View$AttachInfo:mDisplayCutout	Landroid/view/DisplayCutout$ParcelableWrapper;
    //   2499: aload_0
    //   2500: getfield 573	android/view/ViewRootImpl:mPendingDisplayCutout	Landroid/view/DisplayCutout$ParcelableWrapper;
    //   2503: invokevirtual 2563	android/view/DisplayCutout$ParcelableWrapper:set	(Landroid/view/DisplayCutout$ParcelableWrapper;)V
    //   2506: iconst_1
    //   2507: istore_2
    //   2508: iload_2
    //   2509: istore 8
    //   2511: iload 20
    //   2513: ifeq +24 -> 2537
    //   2516: iload_3
    //   2517: istore 17
    //   2519: iload 7
    //   2521: istore 24
    //   2523: aload_0
    //   2524: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   2527: aload_0
    //   2528: getfield 2476	android/view/ViewRootImpl:mPendingAlwaysConsumeNavBar	Z
    //   2531: putfield 2479	android/view/View$AttachInfo:mAlwaysConsumeNavBar	Z
    //   2534: iconst_1
    //   2535: istore 8
    //   2537: iload 8
    //   2539: ifne +93 -> 2632
    //   2542: iload_3
    //   2543: istore 17
    //   2545: iload 8
    //   2547: istore_2
    //   2548: iload 7
    //   2550: istore 24
    //   2552: aload_0
    //   2553: getfield 2414	android/view/ViewRootImpl:mLastSystemUiVisibility	I
    //   2556: aload_0
    //   2557: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   2560: getfield 1014	android/view/View$AttachInfo:mSystemUiVisibility	I
    //   2563: if_icmpne +69 -> 2632
    //   2566: iload_3
    //   2567: istore 17
    //   2569: iload 8
    //   2571: istore_2
    //   2572: iload 7
    //   2574: istore 24
    //   2576: aload_0
    //   2577: getfield 2514	android/view/ViewRootImpl:mApplyInsetsRequested	Z
    //   2580: ifne +52 -> 2632
    //   2583: iload_3
    //   2584: istore 17
    //   2586: iload 8
    //   2588: istore_2
    //   2589: iload 7
    //   2591: istore 24
    //   2593: aload_0
    //   2594: getfield 2516	android/view/ViewRootImpl:mLastOverscanRequested	Z
    //   2597: istore 32
    //   2599: iload_3
    //   2600: istore 17
    //   2602: iload 8
    //   2604: istore_2
    //   2605: iload 7
    //   2607: istore 24
    //   2609: aload_0
    //   2610: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   2613: getfield 2512	android/view/View$AttachInfo:mOverscanRequested	Z
    //   2616: istore 38
    //   2618: iload 32
    //   2620: iload 38
    //   2622: if_icmpne +10 -> 2632
    //   2625: iload 41
    //   2627: iconst_1
    //   2628: ixor
    //   2629: ifeq +139 -> 2768
    //   2632: iload_3
    //   2633: istore 20
    //   2635: iload 19
    //   2637: istore_2
    //   2638: iload 8
    //   2640: istore 17
    //   2642: iload 7
    //   2644: istore 24
    //   2646: iload 36
    //   2648: istore 32
    //   2650: aload_0
    //   2651: aload_0
    //   2652: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   2655: getfield 1014	android/view/View$AttachInfo:mSystemUiVisibility	I
    //   2658: putfield 2414	android/view/ViewRootImpl:mLastSystemUiVisibility	I
    //   2661: iload_3
    //   2662: istore 20
    //   2664: iload 19
    //   2666: istore_2
    //   2667: iload 8
    //   2669: istore 17
    //   2671: iload 7
    //   2673: istore 24
    //   2675: iload 36
    //   2677: istore 32
    //   2679: aload_0
    //   2680: aload_0
    //   2681: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   2684: getfield 2512	android/view/View$AttachInfo:mOverscanRequested	Z
    //   2687: putfield 2516	android/view/ViewRootImpl:mLastOverscanRequested	Z
    //   2690: iload_3
    //   2691: istore 20
    //   2693: iload 19
    //   2695: istore_2
    //   2696: iload 8
    //   2698: istore 17
    //   2700: iload 7
    //   2702: istore 24
    //   2704: iload 36
    //   2706: istore 32
    //   2708: aload_0
    //   2709: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   2712: getfield 2474	android/view/View$AttachInfo:mOutsets	Landroid/graphics/Rect;
    //   2715: aload_0
    //   2716: getfield 559	android/view/ViewRootImpl:mPendingOutsets	Landroid/graphics/Rect;
    //   2719: invokevirtual 2471	android/graphics/Rect:set	(Landroid/graphics/Rect;)V
    //   2722: iload_3
    //   2723: istore 20
    //   2725: iload 19
    //   2727: istore_2
    //   2728: iload 8
    //   2730: istore 17
    //   2732: iload 7
    //   2734: istore 24
    //   2736: iload 36
    //   2738: istore 32
    //   2740: aload_0
    //   2741: iconst_0
    //   2742: putfield 2514	android/view/ViewRootImpl:mApplyInsetsRequested	Z
    //   2745: iload_3
    //   2746: istore 20
    //   2748: iload 19
    //   2750: istore_2
    //   2751: iload 8
    //   2753: istore 17
    //   2755: iload 7
    //   2757: istore 24
    //   2759: iload 36
    //   2761: istore 32
    //   2763: aload_0
    //   2764: aload_1
    //   2765: invokevirtual 2431	android/view/ViewRootImpl:dispatchApplyInsets	(Landroid/view/View;)V
    //   2768: iload 16
    //   2770: iconst_1
    //   2771: ixor
    //   2772: ifeq +27 -> 2799
    //   2775: iload_3
    //   2776: istore 17
    //   2778: iload 8
    //   2780: istore_2
    //   2781: iload 7
    //   2783: istore 24
    //   2785: aload_0
    //   2786: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   2789: getfield 2469	android/view/View$AttachInfo:mVisibleInsets	Landroid/graphics/Rect;
    //   2792: aload_0
    //   2793: getfield 553	android/view/ViewRootImpl:mPendingVisibleInsets	Landroid/graphics/Rect;
    //   2796: invokevirtual 2471	android/graphics/Rect:set	(Landroid/graphics/Rect;)V
    //   2799: iload 31
    //   2801: ifne +179 -> 2980
    //   2804: iload_3
    //   2805: istore 17
    //   2807: iload 8
    //   2809: istore_2
    //   2810: iload 7
    //   2812: istore 24
    //   2814: aload_0
    //   2815: getfield 546	android/view/ViewRootImpl:mSurface	Landroid/view/Surface;
    //   2818: invokevirtual 1216	android/view/Surface:isValid	()Z
    //   2821: istore 32
    //   2823: iload 18
    //   2825: istore 15
    //   2827: iload 35
    //   2829: istore 16
    //   2831: iload 32
    //   2833: ifeq +595 -> 3428
    //   2836: iload 34
    //   2838: istore 16
    //   2840: aload_0
    //   2841: iconst_1
    //   2842: putfield 1376	android/view/ViewRootImpl:mFullRedrawNeeded	Z
    //   2845: iload 34
    //   2847: istore 16
    //   2849: aload_0
    //   2850: getfield 741	android/view/ViewRootImpl:mPreviousTransparentRegion	Landroid/graphics/Region;
    //   2853: invokevirtual 2564	android/graphics/Region:setEmpty	()V
    //   2856: iload 34
    //   2858: istore 16
    //   2860: aload_0
    //   2861: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   2864: getfield 1090	android/view/View$AttachInfo:mThreadedRenderer	Landroid/view/ThreadedRenderer;
    //   2867: astore 13
    //   2869: aload 13
    //   2871: ifnull +92 -> 2963
    //   2874: iload 34
    //   2876: istore 16
    //   2878: aload_0
    //   2879: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   2882: getfield 1090	android/view/View$AttachInfo:mThreadedRenderer	Landroid/view/ThreadedRenderer;
    //   2885: aload_0
    //   2886: getfield 546	android/view/ViewRootImpl:mSurface	Landroid/view/Surface;
    //   2889: invokevirtual 2567	android/view/ThreadedRenderer:initialize	(Landroid/view/Surface;)Z
    //   2892: istore 32
    //   2894: iload 32
    //   2896: ifeq +44 -> 2940
    //   2899: aload_1
    //   2900: getfield 1430	android/view/View:mPrivateFlags	I
    //   2903: sipush 512
    //   2906: iand
    //   2907: ifne +33 -> 2940
    //   2910: aload_0
    //   2911: getfield 546	android/view/ViewRootImpl:mSurface	Landroid/view/Surface;
    //   2914: invokevirtual 2570	android/view/Surface:allocateBuffers	()V
    //   2917: goto +23 -> 2940
    //   2920: astore 13
    //   2922: iconst_1
    //   2923: istore_2
    //   2924: iload 32
    //   2926: istore 16
    //   2928: goto -869 -> 2059
    //   2931: astore 13
    //   2933: iload 32
    //   2935: istore 16
    //   2937: goto +19 -> 2956
    //   2940: iconst_1
    //   2941: istore 15
    //   2943: iload 32
    //   2945: istore 16
    //   2947: goto +481 -> 3428
    //   2950: astore 13
    //   2952: iload 33
    //   2954: istore 16
    //   2956: aload_0
    //   2957: aload 13
    //   2959: invokespecial 1383	android/view/ViewRootImpl:handleOutOfResourcesException	(Landroid/view/Surface$OutOfResourcesException;)V
    //   2962: return
    //   2963: iconst_1
    //   2964: istore 15
    //   2966: iload 35
    //   2968: istore 16
    //   2970: goto +458 -> 3428
    //   2973: astore 13
    //   2975: iconst_1
    //   2976: istore_2
    //   2977: goto -918 -> 2059
    //   2980: iload_3
    //   2981: istore 20
    //   2983: iload 19
    //   2985: istore_2
    //   2986: iload 8
    //   2988: istore 17
    //   2990: iload 7
    //   2992: istore 24
    //   2994: iload 36
    //   2996: istore 32
    //   2998: aload_0
    //   2999: getfield 546	android/view/ViewRootImpl:mSurface	Landroid/view/Surface;
    //   3002: invokevirtual 1216	android/view/Surface:isValid	()Z
    //   3005: istore 16
    //   3007: iload 16
    //   3009: ifne +237 -> 3246
    //   3012: iload_3
    //   3013: istore 17
    //   3015: iload 8
    //   3017: istore_2
    //   3018: iload 7
    //   3020: istore 24
    //   3022: aload_0
    //   3023: getfield 2572	android/view/ViewRootImpl:mLastScrolledFocus	Ljava/lang/ref/WeakReference;
    //   3026: ifnull +20 -> 3046
    //   3029: iload_3
    //   3030: istore 17
    //   3032: iload 8
    //   3034: istore_2
    //   3035: iload 7
    //   3037: istore 24
    //   3039: aload_0
    //   3040: getfield 2572	android/view/ViewRootImpl:mLastScrolledFocus	Ljava/lang/ref/WeakReference;
    //   3043: invokevirtual 2575	java/lang/ref/WeakReference:clear	()V
    //   3046: iload_3
    //   3047: istore 17
    //   3049: iload 8
    //   3051: istore_2
    //   3052: iload 7
    //   3054: istore 24
    //   3056: aload_0
    //   3057: iconst_0
    //   3058: putfield 1259	android/view/ViewRootImpl:mCurScrollY	I
    //   3061: iload_3
    //   3062: istore 17
    //   3064: iload 8
    //   3066: istore_2
    //   3067: iload 7
    //   3069: istore 24
    //   3071: aload_0
    //   3072: iconst_0
    //   3073: putfield 1257	android/view/ViewRootImpl:mScrollY	I
    //   3076: iload_3
    //   3077: istore 17
    //   3079: iload 8
    //   3081: istore_2
    //   3082: iload 7
    //   3084: istore 24
    //   3086: aload_0
    //   3087: getfield 1019	android/view/ViewRootImpl:mView	Landroid/view/View;
    //   3090: instanceof 1261
    //   3093: ifeq +29 -> 3122
    //   3096: iload_3
    //   3097: istore 17
    //   3099: iload 8
    //   3101: istore_2
    //   3102: iload 7
    //   3104: istore 24
    //   3106: aload_0
    //   3107: getfield 1019	android/view/ViewRootImpl:mView	Landroid/view/View;
    //   3110: checkcast 1261	com/android/internal/view/RootViewSurfaceTaker
    //   3113: aload_0
    //   3114: getfield 1259	android/view/ViewRootImpl:mCurScrollY	I
    //   3117: invokeinterface 1264 2 0
    //   3122: iload_3
    //   3123: istore 17
    //   3125: iload 8
    //   3127: istore_2
    //   3128: iload 7
    //   3130: istore 24
    //   3132: aload_0
    //   3133: getfield 1247	android/view/ViewRootImpl:mScroller	Landroid/widget/Scroller;
    //   3136: ifnull +20 -> 3156
    //   3139: iload_3
    //   3140: istore 17
    //   3142: iload 8
    //   3144: istore_2
    //   3145: iload 7
    //   3147: istore 24
    //   3149: aload_0
    //   3150: getfield 1247	android/view/ViewRootImpl:mScroller	Landroid/widget/Scroller;
    //   3153: invokevirtual 1278	android/widget/Scroller:abortAnimation	()V
    //   3156: iload_3
    //   3157: istore 17
    //   3159: iload 8
    //   3161: istore_2
    //   3162: iload 7
    //   3164: istore 24
    //   3166: iload 18
    //   3168: istore 15
    //   3170: iload 35
    //   3172: istore 16
    //   3174: aload_0
    //   3175: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   3178: getfield 1090	android/view/View$AttachInfo:mThreadedRenderer	Landroid/view/ThreadedRenderer;
    //   3181: ifnull +247 -> 3428
    //   3184: iload_3
    //   3185: istore 17
    //   3187: iload 8
    //   3189: istore_2
    //   3190: iload 7
    //   3192: istore 24
    //   3194: iload 18
    //   3196: istore 15
    //   3198: iload 35
    //   3200: istore 16
    //   3202: aload_0
    //   3203: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   3206: getfield 1090	android/view/View$AttachInfo:mThreadedRenderer	Landroid/view/ThreadedRenderer;
    //   3209: invokevirtual 1342	android/view/ThreadedRenderer:isEnabled	()Z
    //   3212: ifeq +216 -> 3428
    //   3215: iload_3
    //   3216: istore 17
    //   3218: iload 8
    //   3220: istore_2
    //   3221: iload 7
    //   3223: istore 24
    //   3225: aload_0
    //   3226: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   3229: getfield 1090	android/view/View$AttachInfo:mThreadedRenderer	Landroid/view/ThreadedRenderer;
    //   3232: invokevirtual 1098	android/view/ThreadedRenderer:destroy	()V
    //   3235: iload 18
    //   3237: istore 15
    //   3239: iload 35
    //   3241: istore 16
    //   3243: goto +185 -> 3428
    //   3246: iload_3
    //   3247: istore 20
    //   3249: iload 19
    //   3251: istore_2
    //   3252: iload 8
    //   3254: istore 17
    //   3256: iload 7
    //   3258: istore 24
    //   3260: iload 36
    //   3262: istore 32
    //   3264: iload 26
    //   3266: aload_0
    //   3267: getfield 546	android/view/ViewRootImpl:mSurface	Landroid/view/Surface;
    //   3270: invokevirtual 2529	android/view/Surface:getGenerationId	()I
    //   3273: if_icmpne +21 -> 3294
    //   3276: iload 15
    //   3278: ifne +16 -> 3294
    //   3281: iload 18
    //   3283: istore 15
    //   3285: iload 35
    //   3287: istore 16
    //   3289: iload 27
    //   3291: ifeq +137 -> 3428
    //   3294: iload_3
    //   3295: istore 20
    //   3297: iload 19
    //   3299: istore_2
    //   3300: iload 8
    //   3302: istore 17
    //   3304: iload 7
    //   3306: istore 24
    //   3308: iload 36
    //   3310: istore 32
    //   3312: aload_0
    //   3313: getfield 1272	android/view/ViewRootImpl:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
    //   3316: astore 13
    //   3318: iload 18
    //   3320: istore 15
    //   3322: iload 35
    //   3324: istore 16
    //   3326: aload 13
    //   3328: ifnonnull +100 -> 3428
    //   3331: iload_3
    //   3332: istore 17
    //   3334: iload 8
    //   3336: istore_2
    //   3337: iload 7
    //   3339: istore 24
    //   3341: iload 18
    //   3343: istore 15
    //   3345: iload 35
    //   3347: istore 16
    //   3349: aload_0
    //   3350: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   3353: getfield 1090	android/view/View$AttachInfo:mThreadedRenderer	Landroid/view/ThreadedRenderer;
    //   3356: ifnull +72 -> 3428
    //   3359: iload_3
    //   3360: istore 17
    //   3362: iload 8
    //   3364: istore_2
    //   3365: iload 7
    //   3367: istore 24
    //   3369: aload_0
    //   3370: iconst_1
    //   3371: putfield 1376	android/view/ViewRootImpl:mFullRedrawNeeded	Z
    //   3374: iload_3
    //   3375: istore 17
    //   3377: iload 8
    //   3379: istore_2
    //   3380: iload 7
    //   3382: istore 24
    //   3384: aload_0
    //   3385: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   3388: getfield 1090	android/view/View$AttachInfo:mThreadedRenderer	Landroid/view/ThreadedRenderer;
    //   3391: aload_0
    //   3392: getfield 546	android/view/ViewRootImpl:mSurface	Landroid/view/Surface;
    //   3395: invokevirtual 2579	android/view/ThreadedRenderer:updateSurface	(Landroid/view/Surface;)V
    //   3398: iload 18
    //   3400: istore 15
    //   3402: iload 35
    //   3404: istore 16
    //   3406: goto +22 -> 3428
    //   3409: astore 13
    //   3411: iload_3
    //   3412: istore 17
    //   3414: iload 8
    //   3416: istore_2
    //   3417: iload 7
    //   3419: istore 24
    //   3421: aload_0
    //   3422: aload 13
    //   3424: invokespecial 1383	android/view/ViewRootImpl:handleOutOfResourcesException	(Landroid/view/Surface$OutOfResourcesException;)V
    //   3427: return
    //   3428: iload 9
    //   3430: bipush 16
    //   3432: iand
    //   3433: ifeq +9 -> 3442
    //   3436: iconst_1
    //   3437: istore 22
    //   3439: goto +6 -> 3445
    //   3442: iconst_0
    //   3443: istore 22
    //   3445: iload 9
    //   3447: bipush 8
    //   3449: iand
    //   3450: ifeq +8 -> 3458
    //   3453: iconst_1
    //   3454: istore_2
    //   3455: goto +5 -> 3460
    //   3458: iconst_0
    //   3459: istore_2
    //   3460: iload 22
    //   3462: ifne +16 -> 3478
    //   3465: iload_2
    //   3466: ifeq +6 -> 3472
    //   3469: goto +9 -> 3478
    //   3472: iconst_0
    //   3473: istore 33
    //   3475: goto +6 -> 3481
    //   3478: iconst_1
    //   3479: istore 33
    //   3481: iload_3
    //   3482: istore 20
    //   3484: iload 15
    //   3486: istore_2
    //   3487: iload 8
    //   3489: istore 17
    //   3491: iload 7
    //   3493: istore 24
    //   3495: iload 16
    //   3497: istore 32
    //   3499: aload_0
    //   3500: getfield 1113	android/view/ViewRootImpl:mDragResizing	Z
    //   3503: iload 33
    //   3505: if_icmpeq +193 -> 3698
    //   3508: iload 33
    //   3510: ifeq +181 -> 3691
    //   3513: iload 22
    //   3515: ifeq +9 -> 3524
    //   3518: iconst_0
    //   3519: istore 22
    //   3521: goto +6 -> 3527
    //   3524: iconst_1
    //   3525: istore 22
    //   3527: iload_3
    //   3528: istore 20
    //   3530: iload 15
    //   3532: istore_2
    //   3533: iload 8
    //   3535: istore 17
    //   3537: iload 7
    //   3539: istore 24
    //   3541: iload 16
    //   3543: istore 32
    //   3545: aload_0
    //   3546: iload 22
    //   3548: putfield 2518	android/view/ViewRootImpl:mResizeMode	I
    //   3551: iload_3
    //   3552: istore 20
    //   3554: iload 15
    //   3556: istore_2
    //   3557: iload 8
    //   3559: istore 17
    //   3561: iload 7
    //   3563: istore 24
    //   3565: iload 16
    //   3567: istore 32
    //   3569: aload_0
    //   3570: getfield 561	android/view/ViewRootImpl:mPendingBackDropFrame	Landroid/graphics/Rect;
    //   3573: astore 13
    //   3575: iload_3
    //   3576: istore 20
    //   3578: iload 15
    //   3580: istore_2
    //   3581: iload 8
    //   3583: istore 17
    //   3585: iload 7
    //   3587: istore 24
    //   3589: iload 16
    //   3591: istore 32
    //   3593: aload_0
    //   3594: getfield 718	android/view/ViewRootImpl:mWinFrame	Landroid/graphics/Rect;
    //   3597: astore 11
    //   3599: aload 11
    //   3601: aload_0
    //   3602: getfield 561	android/view/ViewRootImpl:mPendingBackDropFrame	Landroid/graphics/Rect;
    //   3605: invokevirtual 1118	android/graphics/Rect:equals	(Ljava/lang/Object;)Z
    //   3608: istore 32
    //   3610: aload_0
    //   3611: getfield 553	android/view/ViewRootImpl:mPendingVisibleInsets	Landroid/graphics/Rect;
    //   3614: astore 11
    //   3616: aload_0
    //   3617: getfield 555	android/view/ViewRootImpl:mPendingStableInsets	Landroid/graphics/Rect;
    //   3620: astore 12
    //   3622: aload_0
    //   3623: getfield 2518	android/view/ViewRootImpl:mResizeMode	I
    //   3626: istore_2
    //   3627: aload_0
    //   3628: aload 13
    //   3630: iload 32
    //   3632: aload 11
    //   3634: aload 12
    //   3636: iload_2
    //   3637: invokespecial 2583	android/view/ViewRootImpl:startDragResizing	(Landroid/graphics/Rect;ZLandroid/graphics/Rect;Landroid/graphics/Rect;I)V
    //   3640: goto +58 -> 3698
    //   3643: astore 13
    //   3645: iload 9
    //   3647: istore 17
    //   3649: iload 15
    //   3651: istore_2
    //   3652: iload 8
    //   3654: istore 9
    //   3656: iload 7
    //   3658: istore 8
    //   3660: iload 17
    //   3662: istore 7
    //   3664: goto +189 -> 3853
    //   3667: astore 13
    //   3669: iload 9
    //   3671: istore 17
    //   3673: iload 15
    //   3675: istore_2
    //   3676: iload 8
    //   3678: istore 9
    //   3680: iload 7
    //   3682: istore 8
    //   3684: iload 17
    //   3686: istore 7
    //   3688: goto +165 -> 3853
    //   3691: aload_0
    //   3692: invokespecial 2439	android/view/ViewRootImpl:endDragResizing	()V
    //   3695: goto +3 -> 3698
    //   3698: iload 9
    //   3700: istore 17
    //   3702: aload 10
    //   3704: astore 13
    //   3706: aload_0
    //   3707: getfield 1115	android/view/ViewRootImpl:mUseMTRenderer	Z
    //   3710: ifne +43 -> 3753
    //   3713: iload 33
    //   3715: ifeq +28 -> 3743
    //   3718: aload_0
    //   3719: aload_0
    //   3720: getfield 718	android/view/ViewRootImpl:mWinFrame	Landroid/graphics/Rect;
    //   3723: getfield 1298	android/graphics/Rect:left	I
    //   3726: putfield 1290	android/view/ViewRootImpl:mCanvasOffsetX	I
    //   3729: aload_0
    //   3730: aload_0
    //   3731: getfield 718	android/view/ViewRootImpl:mWinFrame	Landroid/graphics/Rect;
    //   3734: getfield 1301	android/graphics/Rect:top	I
    //   3737: putfield 1292	android/view/ViewRootImpl:mCanvasOffsetY	I
    //   3740: goto +13 -> 3753
    //   3743: aload_0
    //   3744: iconst_0
    //   3745: putfield 1292	android/view/ViewRootImpl:mCanvasOffsetY	I
    //   3748: aload_0
    //   3749: iconst_0
    //   3750: putfield 1290	android/view/ViewRootImpl:mCanvasOffsetX	I
    //   3753: aload 13
    //   3755: astore 10
    //   3757: iload 15
    //   3759: istore_2
    //   3760: iload 8
    //   3762: istore 9
    //   3764: iload 7
    //   3766: istore 8
    //   3768: iload 17
    //   3770: istore 7
    //   3772: goto +81 -> 3853
    //   3775: iload 9
    //   3777: istore 17
    //   3779: astore 13
    //   3781: iload 15
    //   3783: istore_2
    //   3784: iload 8
    //   3786: istore 9
    //   3788: iload 7
    //   3790: istore 8
    //   3792: iload 17
    //   3794: istore 7
    //   3796: goto +57 -> 3853
    //   3799: astore 13
    //   3801: iload 9
    //   3803: istore 7
    //   3805: iload 20
    //   3807: istore_3
    //   3808: iload 17
    //   3810: istore 9
    //   3812: iload 24
    //   3814: istore 8
    //   3816: iload 32
    //   3818: istore 16
    //   3820: goto +33 -> 3853
    //   3823: astore 13
    //   3825: iconst_0
    //   3826: istore 7
    //   3828: iload 20
    //   3830: istore_2
    //   3831: iload 15
    //   3833: istore 9
    //   3835: goto +18 -> 3853
    //   3838: astore 13
    //   3840: iconst_0
    //   3841: istore 16
    //   3843: iconst_0
    //   3844: istore 7
    //   3846: iload 15
    //   3848: istore 9
    //   3850: iload 20
    //   3852: istore_2
    //   3853: iload 26
    //   3855: istore 15
    //   3857: iload 8
    //   3859: istore 17
    //   3861: aload_0
    //   3862: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   3865: aload 10
    //   3867: getfield 1298	android/graphics/Rect:left	I
    //   3870: putfield 1791	android/view/View$AttachInfo:mWindowLeft	I
    //   3873: aload_0
    //   3874: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   3877: aload 10
    //   3879: getfield 1301	android/graphics/Rect:top	I
    //   3882: putfield 1794	android/view/View$AttachInfo:mWindowTop	I
    //   3885: aload_0
    //   3886: getfield 708	android/view/ViewRootImpl:mWidth	I
    //   3889: aload 10
    //   3891: invokevirtual 2401	android/graphics/Rect:width	()I
    //   3894: if_icmpne +15 -> 3909
    //   3897: aload_0
    //   3898: getfield 710	android/view/ViewRootImpl:mHeight	I
    //   3901: aload 10
    //   3903: invokevirtual 2403	android/graphics/Rect:height	()I
    //   3906: if_icmpeq +21 -> 3927
    //   3909: aload_0
    //   3910: aload 10
    //   3912: invokevirtual 2401	android/graphics/Rect:width	()I
    //   3915: putfield 708	android/view/ViewRootImpl:mWidth	I
    //   3918: aload_0
    //   3919: aload 10
    //   3921: invokevirtual 2403	android/graphics/Rect:height	()I
    //   3924: putfield 710	android/view/ViewRootImpl:mHeight	I
    //   3927: aload_0
    //   3928: getfield 1272	android/view/ViewRootImpl:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
    //   3931: ifnull +342 -> 4273
    //   3934: aload_0
    //   3935: getfield 546	android/view/ViewRootImpl:mSurface	Landroid/view/Surface;
    //   3938: invokevirtual 1216	android/view/Surface:isValid	()Z
    //   3941: ifeq +14 -> 3955
    //   3944: aload_0
    //   3945: getfield 1272	android/view/ViewRootImpl:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
    //   3948: aload_0
    //   3949: getfield 546	android/view/ViewRootImpl:mSurface	Landroid/view/Surface;
    //   3952: putfield 2584	com/android/internal/view/BaseSurfaceHolder:mSurface	Landroid/view/Surface;
    //   3955: aload_0
    //   3956: getfield 1272	android/view/ViewRootImpl:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
    //   3959: aload_0
    //   3960: getfield 708	android/view/ViewRootImpl:mWidth	I
    //   3963: aload_0
    //   3964: getfield 710	android/view/ViewRootImpl:mHeight	I
    //   3967: invokevirtual 2587	com/android/internal/view/BaseSurfaceHolder:setSurfaceFrameSize	(II)V
    //   3970: aload_0
    //   3971: getfield 1272	android/view/ViewRootImpl:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
    //   3974: getfield 2535	com/android/internal/view/BaseSurfaceHolder:mSurfaceLock	Ljava/util/concurrent/locks/ReentrantLock;
    //   3977: invokevirtual 2590	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   3980: aload_0
    //   3981: getfield 546	android/view/ViewRootImpl:mSurface	Landroid/view/Surface;
    //   3984: invokevirtual 1216	android/view/Surface:isValid	()Z
    //   3987: ifeq +166 -> 4153
    //   3990: iload 31
    //   3992: ifne +64 -> 4056
    //   3995: aload_0
    //   3996: getfield 1272	android/view/ViewRootImpl:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
    //   3999: invokevirtual 2593	com/android/internal/view/BaseSurfaceHolder:ungetCallbacks	()V
    //   4002: aload_0
    //   4003: iconst_1
    //   4004: putfield 2595	android/view/ViewRootImpl:mIsCreating	Z
    //   4007: aload_0
    //   4008: getfield 1272	android/view/ViewRootImpl:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
    //   4011: invokevirtual 2331	com/android/internal/view/BaseSurfaceHolder:getCallbacks	()[Landroid/view/SurfaceHolder$Callback;
    //   4014: astore 10
    //   4016: aload 10
    //   4018: ifnull +36 -> 4054
    //   4021: aload 10
    //   4023: arraylength
    //   4024: istore_3
    //   4025: iconst_0
    //   4026: istore 8
    //   4028: iload 8
    //   4030: iload_3
    //   4031: if_icmpge +23 -> 4054
    //   4034: aload 10
    //   4036: iload 8
    //   4038: aaload
    //   4039: aload_0
    //   4040: getfield 1272	android/view/ViewRootImpl:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
    //   4043: invokeinterface 2601 2 0
    //   4048: iinc 8 1
    //   4051: goto -23 -> 4028
    //   4054: iconst_1
    //   4055: istore_3
    //   4056: iload_3
    //   4057: ifne +21 -> 4078
    //   4060: iload 15
    //   4062: aload_0
    //   4063: getfield 546	android/view/ViewRootImpl:mSurface	Landroid/view/Surface;
    //   4066: invokevirtual 2529	android/view/Surface:getGenerationId	()I
    //   4069: if_icmpeq +6 -> 4075
    //   4072: goto +6 -> 4078
    //   4075: goto +70 -> 4145
    //   4078: aload_0
    //   4079: getfield 1272	android/view/ViewRootImpl:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
    //   4082: invokevirtual 2331	com/android/internal/view/BaseSurfaceHolder:getCallbacks	()[Landroid/view/SurfaceHolder$Callback;
    //   4085: astore 10
    //   4087: iload 15
    //   4089: istore 24
    //   4091: aload 10
    //   4093: ifnull +52 -> 4145
    //   4096: aload 10
    //   4098: arraylength
    //   4099: istore 8
    //   4101: iconst_0
    //   4102: istore_3
    //   4103: iload 15
    //   4105: istore 24
    //   4107: iload_3
    //   4108: iload 8
    //   4110: if_icmpge +35 -> 4145
    //   4113: aload 10
    //   4115: iload_3
    //   4116: aaload
    //   4117: aload_0
    //   4118: getfield 1272	android/view/ViewRootImpl:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
    //   4121: aload 4
    //   4123: getfield 1517	android/view/WindowManager$LayoutParams:format	I
    //   4126: aload_0
    //   4127: getfield 708	android/view/ViewRootImpl:mWidth	I
    //   4130: aload_0
    //   4131: getfield 710	android/view/ViewRootImpl:mHeight	I
    //   4134: invokeinterface 2605 5 0
    //   4139: iinc 3 1
    //   4142: goto -39 -> 4103
    //   4145: aload_0
    //   4146: iconst_0
    //   4147: putfield 2595	android/view/ViewRootImpl:mIsCreating	Z
    //   4150: goto +123 -> 4273
    //   4153: iload 31
    //   4155: ifeq +118 -> 4273
    //   4158: aload_0
    //   4159: getfield 1272	android/view/ViewRootImpl:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
    //   4162: invokevirtual 2593	com/android/internal/view/BaseSurfaceHolder:ungetCallbacks	()V
    //   4165: aload_0
    //   4166: getfield 1272	android/view/ViewRootImpl:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
    //   4169: invokevirtual 2331	com/android/internal/view/BaseSurfaceHolder:getCallbacks	()[Landroid/view/SurfaceHolder$Callback;
    //   4172: astore 10
    //   4174: aload 10
    //   4176: ifnull +36 -> 4212
    //   4179: aload 10
    //   4181: arraylength
    //   4182: istore_3
    //   4183: iconst_0
    //   4184: istore 8
    //   4186: iload 8
    //   4188: iload_3
    //   4189: if_icmpge +23 -> 4212
    //   4192: aload 10
    //   4194: iload 8
    //   4196: aaload
    //   4197: aload_0
    //   4198: getfield 1272	android/view/ViewRootImpl:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
    //   4201: invokeinterface 2608 2 0
    //   4206: iinc 8 1
    //   4209: goto -23 -> 4186
    //   4212: aload_0
    //   4213: getfield 1272	android/view/ViewRootImpl:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
    //   4216: getfield 2535	com/android/internal/view/BaseSurfaceHolder:mSurfaceLock	Ljava/util/concurrent/locks/ReentrantLock;
    //   4219: invokevirtual 2540	java/util/concurrent/locks/ReentrantLock:lock	()V
    //   4222: aload_0
    //   4223: getfield 1272	android/view/ViewRootImpl:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
    //   4226: astore 13
    //   4228: new 543	android/view/Surface
    //   4231: astore 10
    //   4233: aload 10
    //   4235: invokespecial 544	android/view/Surface:<init>	()V
    //   4238: aload 13
    //   4240: aload 10
    //   4242: putfield 2584	com/android/internal/view/BaseSurfaceHolder:mSurface	Landroid/view/Surface;
    //   4245: aload_0
    //   4246: getfield 1272	android/view/ViewRootImpl:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
    //   4249: getfield 2535	com/android/internal/view/BaseSurfaceHolder:mSurfaceLock	Ljava/util/concurrent/locks/ReentrantLock;
    //   4252: invokevirtual 2590	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   4255: goto +18 -> 4273
    //   4258: astore 10
    //   4260: aload_0
    //   4261: getfield 1272	android/view/ViewRootImpl:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
    //   4264: getfield 2535	com/android/internal/view/BaseSurfaceHolder:mSurfaceLock	Ljava/util/concurrent/locks/ReentrantLock;
    //   4267: invokevirtual 2590	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   4270: aload 10
    //   4272: athrow
    //   4273: aload_0
    //   4274: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   4277: getfield 1090	android/view/View$AttachInfo:mThreadedRenderer	Landroid/view/ThreadedRenderer;
    //   4280: astore 10
    //   4282: aload 10
    //   4284: ifnull +76 -> 4360
    //   4287: aload 10
    //   4289: invokevirtual 1342	android/view/ThreadedRenderer:isEnabled	()Z
    //   4292: ifeq +68 -> 4360
    //   4295: iload 16
    //   4297: ifne +34 -> 4331
    //   4300: aload_0
    //   4301: getfield 708	android/view/ViewRootImpl:mWidth	I
    //   4304: aload 10
    //   4306: invokevirtual 2611	android/view/ThreadedRenderer:getWidth	()I
    //   4309: if_icmpne +22 -> 4331
    //   4312: aload_0
    //   4313: getfield 710	android/view/ViewRootImpl:mHeight	I
    //   4316: aload 10
    //   4318: invokevirtual 2614	android/view/ThreadedRenderer:getHeight	()I
    //   4321: if_icmpne +10 -> 4331
    //   4324: aload_0
    //   4325: getfield 2616	android/view/ViewRootImpl:mNeedsRendererSetup	Z
    //   4328: ifeq +32 -> 4360
    //   4331: aload 10
    //   4333: aload_0
    //   4334: getfield 708	android/view/ViewRootImpl:mWidth	I
    //   4337: aload_0
    //   4338: getfield 710	android/view/ViewRootImpl:mHeight	I
    //   4341: aload_0
    //   4342: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   4345: aload_0
    //   4346: getfield 514	android/view/ViewRootImpl:mWindowAttributes	Landroid/view/WindowManager$LayoutParams;
    //   4349: getfield 1295	android/view/WindowManager$LayoutParams:surfaceInsets	Landroid/graphics/Rect;
    //   4352: invokevirtual 2620	android/view/ThreadedRenderer:setup	(IILandroid/view/View$AttachInfo;Landroid/graphics/Rect;)V
    //   4355: aload_0
    //   4356: iconst_0
    //   4357: putfield 2616	android/view/ViewRootImpl:mNeedsRendererSetup	Z
    //   4360: aload_0
    //   4361: getfield 522	android/view/ViewRootImpl:mStopped	Z
    //   4364: ifeq +10 -> 4374
    //   4367: aload_0
    //   4368: getfield 1356	android/view/ViewRootImpl:mReportNextDraw	Z
    //   4371: ifeq +63 -> 4434
    //   4374: iload 7
    //   4376: iconst_1
    //   4377: iand
    //   4378: ifeq +9 -> 4387
    //   4381: iconst_1
    //   4382: istore 16
    //   4384: goto +6 -> 4390
    //   4387: iconst_0
    //   4388: istore 16
    //   4390: aload_0
    //   4391: iload 16
    //   4393: invokespecial 2043	android/view/ViewRootImpl:ensureTouchModeLocally	(Z)Z
    //   4396: ifne +45 -> 4441
    //   4399: aload_0
    //   4400: getfield 708	android/view/ViewRootImpl:mWidth	I
    //   4403: aload_1
    //   4404: invokevirtual 2208	android/view/View:getMeasuredWidth	()I
    //   4407: if_icmpne +34 -> 4441
    //   4410: aload_0
    //   4411: getfield 710	android/view/ViewRootImpl:mHeight	I
    //   4414: aload_1
    //   4415: invokevirtual 2211	android/view/View:getMeasuredHeight	()I
    //   4418: if_icmpne +23 -> 4441
    //   4421: iload 9
    //   4423: ifne +18 -> 4441
    //   4426: iload 17
    //   4428: ifeq +6 -> 4434
    //   4431: goto +10 -> 4441
    //   4434: iload 14
    //   4436: istore 8
    //   4438: goto +144 -> 4582
    //   4441: aload_0
    //   4442: getfield 708	android/view/ViewRootImpl:mWidth	I
    //   4445: aload 4
    //   4447: getfield 2182	android/view/WindowManager$LayoutParams:width	I
    //   4450: invokestatic 2196	android/view/ViewRootImpl:getRootMeasureSpec	(II)I
    //   4453: istore 9
    //   4455: aload_0
    //   4456: getfield 710	android/view/ViewRootImpl:mHeight	I
    //   4459: aload 4
    //   4461: getfield 2199	android/view/WindowManager$LayoutParams:height	I
    //   4464: invokestatic 2196	android/view/ViewRootImpl:getRootMeasureSpec	(II)I
    //   4467: istore_3
    //   4468: aload_0
    //   4469: iload 9
    //   4471: iload_3
    //   4472: invokespecial 2202	android/view/ViewRootImpl:performMeasure	(II)V
    //   4475: aload_1
    //   4476: invokevirtual 2208	android/view/View:getMeasuredWidth	()I
    //   4479: istore 14
    //   4481: aload_1
    //   4482: invokevirtual 2211	android/view/View:getMeasuredHeight	()I
    //   4485: istore 15
    //   4487: iconst_0
    //   4488: istore 8
    //   4490: aload 4
    //   4492: getfield 2623	android/view/WindowManager$LayoutParams:horizontalWeight	F
    //   4495: fconst_0
    //   4496: fcmpl
    //   4497: ifle +32 -> 4529
    //   4500: iload 14
    //   4502: aload_0
    //   4503: getfield 708	android/view/ViewRootImpl:mWidth	I
    //   4506: iload 14
    //   4508: isub
    //   4509: i2f
    //   4510: aload 4
    //   4512: getfield 2623	android/view/WindowManager$LayoutParams:horizontalWeight	F
    //   4515: fmul
    //   4516: f2i
    //   4517: iadd
    //   4518: ldc_w 1853
    //   4521: invokestatic 1858	android/view/View$MeasureSpec:makeMeasureSpec	(II)I
    //   4524: istore 9
    //   4526: iconst_1
    //   4527: istore 8
    //   4529: aload 4
    //   4531: getfield 2626	android/view/WindowManager$LayoutParams:verticalWeight	F
    //   4534: fconst_0
    //   4535: fcmpl
    //   4536: ifle +31 -> 4567
    //   4539: iload 15
    //   4541: aload_0
    //   4542: getfield 710	android/view/ViewRootImpl:mHeight	I
    //   4545: iload 15
    //   4547: isub
    //   4548: i2f
    //   4549: aload 4
    //   4551: getfield 2626	android/view/WindowManager$LayoutParams:verticalWeight	F
    //   4554: fmul
    //   4555: f2i
    //   4556: iadd
    //   4557: ldc_w 1853
    //   4560: invokestatic 1858	android/view/View$MeasureSpec:makeMeasureSpec	(II)I
    //   4563: istore_3
    //   4564: iconst_1
    //   4565: istore 8
    //   4567: iload 8
    //   4569: ifeq +10 -> 4579
    //   4572: aload_0
    //   4573: iload 9
    //   4575: iload_3
    //   4576: invokespecial 2202	android/view/ViewRootImpl:performMeasure	(II)V
    //   4579: iconst_1
    //   4580: istore 8
    //   4582: iload 17
    //   4584: istore 9
    //   4586: iload 8
    //   4588: ifeq +23 -> 4611
    //   4591: aload_0
    //   4592: getfield 522	android/view/ViewRootImpl:mStopped	Z
    //   4595: ifeq +10 -> 4605
    //   4598: aload_0
    //   4599: getfield 1356	android/view/ViewRootImpl:mReportNextDraw	Z
    //   4602: ifeq +9 -> 4611
    //   4605: iconst_1
    //   4606: istore 8
    //   4608: goto +6 -> 4614
    //   4611: iconst_0
    //   4612: istore 8
    //   4614: iload 8
    //   4616: ifne +22 -> 4638
    //   4619: aload_0
    //   4620: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   4623: getfield 1011	android/view/View$AttachInfo:mRecomputeGlobalAttributes	Z
    //   4626: ifeq +6 -> 4632
    //   4629: goto +9 -> 4638
    //   4632: iconst_0
    //   4633: istore 9
    //   4635: goto +6 -> 4641
    //   4638: iconst_1
    //   4639: istore 9
    //   4641: iload 8
    //   4643: ifeq +171 -> 4814
    //   4646: aload_0
    //   4647: aload 4
    //   4649: aload_0
    //   4650: getfield 708	android/view/ViewRootImpl:mWidth	I
    //   4653: aload_0
    //   4654: getfield 710	android/view/ViewRootImpl:mHeight	I
    //   4657: invokespecial 2628	android/view/ViewRootImpl:performLayout	(Landroid/view/WindowManager$LayoutParams;II)V
    //   4660: aload_1
    //   4661: getfield 1430	android/view/View:mPrivateFlags	I
    //   4664: sipush 512
    //   4667: iand
    //   4668: ifeq +146 -> 4814
    //   4671: aload_1
    //   4672: aload_0
    //   4673: getfield 504	android/view/ViewRootImpl:mTmpLocation	[I
    //   4676: invokevirtual 2632	android/view/View:getLocationInWindow	([I)V
    //   4679: aload_0
    //   4680: getfield 739	android/view/ViewRootImpl:mTransparentRegion	Landroid/graphics/Region;
    //   4683: aload_0
    //   4684: getfield 504	android/view/ViewRootImpl:mTmpLocation	[I
    //   4687: iconst_0
    //   4688: iaload
    //   4689: aload_0
    //   4690: getfield 504	android/view/ViewRootImpl:mTmpLocation	[I
    //   4693: iconst_1
    //   4694: iaload
    //   4695: aload_0
    //   4696: getfield 504	android/view/ViewRootImpl:mTmpLocation	[I
    //   4699: iconst_0
    //   4700: iaload
    //   4701: aload_1
    //   4702: getfield 2635	android/view/View:mRight	I
    //   4705: iadd
    //   4706: aload_1
    //   4707: getfield 2638	android/view/View:mLeft	I
    //   4710: isub
    //   4711: aload_0
    //   4712: getfield 504	android/view/ViewRootImpl:mTmpLocation	[I
    //   4715: iconst_1
    //   4716: iaload
    //   4717: aload_1
    //   4718: getfield 2641	android/view/View:mBottom	I
    //   4721: iadd
    //   4722: aload_1
    //   4723: getfield 2644	android/view/View:mTop	I
    //   4726: isub
    //   4727: invokevirtual 2646	android/graphics/Region:set	(IIII)Z
    //   4730: pop
    //   4731: aload_1
    //   4732: aload_0
    //   4733: getfield 739	android/view/ViewRootImpl:mTransparentRegion	Landroid/graphics/Region;
    //   4736: invokevirtual 2650	android/view/View:gatherTransparentRegion	(Landroid/graphics/Region;)Z
    //   4739: pop
    //   4740: aload_0
    //   4741: getfield 1136	android/view/ViewRootImpl:mTranslator	Landroid/content/res/CompatibilityInfo$Translator;
    //   4744: ifnull +14 -> 4758
    //   4747: aload_0
    //   4748: getfield 1136	android/view/ViewRootImpl:mTranslator	Landroid/content/res/CompatibilityInfo$Translator;
    //   4751: aload_0
    //   4752: getfield 739	android/view/ViewRootImpl:mTransparentRegion	Landroid/graphics/Region;
    //   4755: invokevirtual 2654	android/content/res/CompatibilityInfo$Translator:translateRegionInWindowToScreen	(Landroid/graphics/Region;)V
    //   4758: aload_0
    //   4759: getfield 739	android/view/ViewRootImpl:mTransparentRegion	Landroid/graphics/Region;
    //   4762: aload_0
    //   4763: getfield 741	android/view/ViewRootImpl:mPreviousTransparentRegion	Landroid/graphics/Region;
    //   4766: invokevirtual 2655	android/graphics/Region:equals	(Ljava/lang/Object;)Z
    //   4769: ifne +45 -> 4814
    //   4772: aload_0
    //   4773: getfield 741	android/view/ViewRootImpl:mPreviousTransparentRegion	Landroid/graphics/Region;
    //   4776: aload_0
    //   4777: getfield 739	android/view/ViewRootImpl:mTransparentRegion	Landroid/graphics/Region;
    //   4780: invokevirtual 2657	android/graphics/Region:set	(Landroid/graphics/Region;)Z
    //   4783: pop
    //   4784: aload_0
    //   4785: iconst_1
    //   4786: putfield 1376	android/view/ViewRootImpl:mFullRedrawNeeded	Z
    //   4789: aload_0
    //   4790: getfield 677	android/view/ViewRootImpl:mWindowSession	Landroid/view/IWindowSession;
    //   4793: aload_0
    //   4794: getfield 721	android/view/ViewRootImpl:mWindow	Landroid/view/ViewRootImpl$W;
    //   4797: aload_0
    //   4798: getfield 739	android/view/ViewRootImpl:mTransparentRegion	Landroid/graphics/Region;
    //   4801: invokeinterface 2661 3 0
    //   4806: goto +8 -> 4814
    //   4809: astore 10
    //   4811: goto +3 -> 4814
    //   4814: iload 9
    //   4816: ifeq +21 -> 4837
    //   4819: aload_0
    //   4820: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   4823: iconst_0
    //   4824: putfield 1011	android/view/View$AttachInfo:mRecomputeGlobalAttributes	Z
    //   4827: aload_0
    //   4828: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   4831: getfield 1240	android/view/View$AttachInfo:mTreeObserver	Landroid/view/ViewTreeObserver;
    //   4834: invokevirtual 2664	android/view/ViewTreeObserver:dispatchOnGlobalLayout	()V
    //   4837: iload 25
    //   4839: ifeq +174 -> 5013
    //   4842: aload_0
    //   4843: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   4846: getfield 2667	android/view/View$AttachInfo:mGivenInternalInsets	Landroid/view/ViewTreeObserver$InternalInsetsInfo;
    //   4849: astore 11
    //   4851: aload 11
    //   4853: invokevirtual 2668	android/view/ViewTreeObserver$InternalInsetsInfo:reset	()V
    //   4856: aload_0
    //   4857: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   4860: getfield 1240	android/view/View$AttachInfo:mTreeObserver	Landroid/view/ViewTreeObserver;
    //   4863: aload 11
    //   4865: invokevirtual 2672	android/view/ViewTreeObserver:dispatchOnComputeInternalInsets	(Landroid/view/ViewTreeObserver$InternalInsetsInfo;)V
    //   4868: aload_0
    //   4869: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   4872: aload 11
    //   4874: invokevirtual 2673	android/view/ViewTreeObserver$InternalInsetsInfo:isEmpty	()Z
    //   4877: iconst_1
    //   4878: ixor
    //   4879: putfield 2526	android/view/View$AttachInfo:mHasNonEmptyGivenInternalInsets	Z
    //   4882: iload 28
    //   4884: ifne +15 -> 4899
    //   4887: aload_0
    //   4888: getfield 578	android/view/ViewRootImpl:mLastGivenInsets	Landroid/view/ViewTreeObserver$InternalInsetsInfo;
    //   4891: aload 11
    //   4893: invokevirtual 2674	android/view/ViewTreeObserver$InternalInsetsInfo:equals	(Ljava/lang/Object;)Z
    //   4896: ifne +117 -> 5013
    //   4899: aload_0
    //   4900: getfield 578	android/view/ViewRootImpl:mLastGivenInsets	Landroid/view/ViewTreeObserver$InternalInsetsInfo;
    //   4903: aload 11
    //   4905: invokevirtual 2676	android/view/ViewTreeObserver$InternalInsetsInfo:set	(Landroid/view/ViewTreeObserver$InternalInsetsInfo;)V
    //   4908: aload_0
    //   4909: getfield 1136	android/view/ViewRootImpl:mTranslator	Landroid/content/res/CompatibilityInfo$Translator;
    //   4912: ifnull +48 -> 4960
    //   4915: aload_0
    //   4916: getfield 1136	android/view/ViewRootImpl:mTranslator	Landroid/content/res/CompatibilityInfo$Translator;
    //   4919: aload 11
    //   4921: getfield 2679	android/view/ViewTreeObserver$InternalInsetsInfo:contentInsets	Landroid/graphics/Rect;
    //   4924: invokevirtual 2683	android/content/res/CompatibilityInfo$Translator:getTranslatedContentInsets	(Landroid/graphics/Rect;)Landroid/graphics/Rect;
    //   4927: astore 10
    //   4929: aload_0
    //   4930: getfield 1136	android/view/ViewRootImpl:mTranslator	Landroid/content/res/CompatibilityInfo$Translator;
    //   4933: aload 11
    //   4935: getfield 2686	android/view/ViewTreeObserver$InternalInsetsInfo:visibleInsets	Landroid/graphics/Rect;
    //   4938: invokevirtual 2689	android/content/res/CompatibilityInfo$Translator:getTranslatedVisibleInsets	(Landroid/graphics/Rect;)Landroid/graphics/Rect;
    //   4941: astore 4
    //   4943: aload_0
    //   4944: getfield 1136	android/view/ViewRootImpl:mTranslator	Landroid/content/res/CompatibilityInfo$Translator;
    //   4947: aload 11
    //   4949: getfield 2692	android/view/ViewTreeObserver$InternalInsetsInfo:touchableRegion	Landroid/graphics/Region;
    //   4952: invokevirtual 2696	android/content/res/CompatibilityInfo$Translator:getTranslatedTouchableArea	(Landroid/graphics/Region;)Landroid/graphics/Region;
    //   4955: astore 13
    //   4957: goto +27 -> 4984
    //   4960: aload 11
    //   4962: getfield 2679	android/view/ViewTreeObserver$InternalInsetsInfo:contentInsets	Landroid/graphics/Rect;
    //   4965: astore 10
    //   4967: aload 11
    //   4969: getfield 2686	android/view/ViewTreeObserver$InternalInsetsInfo:visibleInsets	Landroid/graphics/Rect;
    //   4972: astore 4
    //   4974: aload 11
    //   4976: getfield 2692	android/view/ViewTreeObserver$InternalInsetsInfo:touchableRegion	Landroid/graphics/Region;
    //   4979: astore 13
    //   4981: goto -24 -> 4957
    //   4984: aload_0
    //   4985: getfield 677	android/view/ViewRootImpl:mWindowSession	Landroid/view/IWindowSession;
    //   4988: aload_0
    //   4989: getfield 721	android/view/ViewRootImpl:mWindow	Landroid/view/ViewRootImpl$W;
    //   4992: aload 11
    //   4994: getfield 2699	android/view/ViewTreeObserver$InternalInsetsInfo:mTouchableInsets	I
    //   4997: aload 10
    //   4999: aload 4
    //   5001: aload 13
    //   5003: invokeinterface 2703 6 0
    //   5008: goto +5 -> 5013
    //   5011: astore 10
    //   5013: aload_0
    //   5014: getfield 743	android/view/ViewRootImpl:mFirst	Z
    //   5017: ifeq +83 -> 5100
    //   5020: getstatic 827	android/view/ViewRootImpl:sAlwaysAssignFocus	Z
    //   5023: ifne +52 -> 5075
    //   5026: invokestatic 2705	android/view/ViewRootImpl:isInTouchMode	()Z
    //   5029: ifne +6 -> 5035
    //   5032: goto +43 -> 5075
    //   5035: aload_0
    //   5036: getfield 1019	android/view/ViewRootImpl:mView	Landroid/view/View;
    //   5039: invokevirtual 1588	android/view/View:findFocus	()Landroid/view/View;
    //   5042: astore 10
    //   5044: aload 10
    //   5046: instanceof 1478
    //   5049: ifeq +51 -> 5100
    //   5052: aload 10
    //   5054: checkcast 1478	android/view/ViewGroup
    //   5057: invokevirtual 1609	android/view/ViewGroup:getDescendantFocusability	()I
    //   5060: ldc_w 1610
    //   5063: if_icmpne +37 -> 5100
    //   5066: aload 10
    //   5068: invokevirtual 2153	android/view/View:restoreDefaultFocus	()Z
    //   5071: pop
    //   5072: goto +28 -> 5100
    //   5075: aload_0
    //   5076: getfield 1019	android/view/ViewRootImpl:mView	Landroid/view/View;
    //   5079: ifnull +21 -> 5100
    //   5082: aload_0
    //   5083: getfield 1019	android/view/ViewRootImpl:mView	Landroid/view/View;
    //   5086: invokevirtual 1584	android/view/View:hasFocus	()Z
    //   5089: ifne +11 -> 5100
    //   5092: aload_0
    //   5093: getfield 1019	android/view/ViewRootImpl:mView	Landroid/view/View;
    //   5096: invokevirtual 2153	android/view/View:restoreDefaultFocus	()Z
    //   5099: pop
    //   5100: iload 6
    //   5102: ifne +10 -> 5112
    //   5105: aload_0
    //   5106: getfield 743	android/view/ViewRootImpl:mFirst	Z
    //   5109: ifeq +14 -> 5123
    //   5112: iload 23
    //   5114: ifeq +9 -> 5123
    //   5117: iconst_1
    //   5118: istore 8
    //   5120: goto +6 -> 5126
    //   5123: iconst_0
    //   5124: istore 8
    //   5126: aload_0
    //   5127: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   5130: getfield 2054	android/view/View$AttachInfo:mHasWindowFocus	Z
    //   5133: ifeq +14 -> 5147
    //   5136: iload 23
    //   5138: ifeq +9 -> 5147
    //   5141: iconst_1
    //   5142: istore 16
    //   5144: goto +6 -> 5150
    //   5147: iconst_0
    //   5148: istore 16
    //   5150: iload 16
    //   5152: ifeq +16 -> 5168
    //   5155: aload_0
    //   5156: getfield 2707	android/view/ViewRootImpl:mLostWindowFocus	Z
    //   5159: ifeq +9 -> 5168
    //   5162: iconst_1
    //   5163: istore 9
    //   5165: goto +6 -> 5171
    //   5168: iconst_0
    //   5169: istore 9
    //   5171: iload 9
    //   5173: ifeq +11 -> 5184
    //   5176: aload_0
    //   5177: iconst_0
    //   5178: putfield 2707	android/view/ViewRootImpl:mLostWindowFocus	Z
    //   5181: goto +20 -> 5201
    //   5184: iload 16
    //   5186: ifne +15 -> 5201
    //   5189: aload_0
    //   5190: getfield 2709	android/view/ViewRootImpl:mHadWindowFocus	Z
    //   5193: ifeq +8 -> 5201
    //   5196: aload_0
    //   5197: iconst_1
    //   5198: putfield 2707	android/view/ViewRootImpl:mLostWindowFocus	Z
    //   5201: iload 8
    //   5203: ifne +8 -> 5211
    //   5206: iload 9
    //   5208: ifeq +49 -> 5257
    //   5211: aload_0
    //   5212: getfield 514	android/view/ViewRootImpl:mWindowAttributes	Landroid/view/WindowManager$LayoutParams;
    //   5215: ifnonnull +9 -> 5224
    //   5218: iconst_0
    //   5219: istore 8
    //   5221: goto +25 -> 5246
    //   5224: aload_0
    //   5225: getfield 514	android/view/ViewRootImpl:mWindowAttributes	Landroid/view/WindowManager$LayoutParams;
    //   5228: getfield 2710	android/view/WindowManager$LayoutParams:type	I
    //   5231: sipush 2005
    //   5234: if_icmpne +9 -> 5243
    //   5237: iconst_1
    //   5238: istore 8
    //   5240: goto +6 -> 5246
    //   5243: goto -25 -> 5218
    //   5246: iload 8
    //   5248: ifne +9 -> 5257
    //   5251: aload_1
    //   5252: bipush 32
    //   5254: invokevirtual 1693	android/view/View:sendAccessibilityEvent	(I)V
    //   5257: aload_0
    //   5258: iconst_0
    //   5259: putfield 743	android/view/ViewRootImpl:mFirst	Z
    //   5262: aload_0
    //   5263: iconst_0
    //   5264: putfield 2115	android/view/ViewRootImpl:mWillDrawSoon	Z
    //   5267: aload_0
    //   5268: iconst_0
    //   5269: putfield 2379	android/view/ViewRootImpl:mNewSurfaceNeeded	Z
    //   5272: aload_0
    //   5273: iconst_0
    //   5274: putfield 2520	android/view/ViewRootImpl:mActivityRelaunched	Z
    //   5277: aload_0
    //   5278: iload 5
    //   5280: putfield 734	android/view/ViewRootImpl:mViewVisibility	I
    //   5283: aload_0
    //   5284: iload 16
    //   5286: putfield 2709	android/view/ViewRootImpl:mHadWindowFocus	Z
    //   5289: iload 16
    //   5291: ifeq +102 -> 5393
    //   5294: aload_0
    //   5295: invokespecial 877	android/view/ViewRootImpl:isInLocalFocusMode	()Z
    //   5298: ifne +95 -> 5393
    //   5301: aload_0
    //   5302: getfield 514	android/view/ViewRootImpl:mWindowAttributes	Landroid/view/WindowManager$LayoutParams;
    //   5305: getfield 984	android/view/WindowManager$LayoutParams:flags	I
    //   5308: invokestatic 2058	android/view/WindowManager$LayoutParams:mayUseInputMethod	(I)Z
    //   5311: istore 28
    //   5313: iload 28
    //   5315: aload_0
    //   5316: getfield 2060	android/view/ViewRootImpl:mLastWasImTarget	Z
    //   5319: if_icmpeq +74 -> 5393
    //   5322: aload_0
    //   5323: iload 28
    //   5325: putfield 2060	android/view/ViewRootImpl:mLastWasImTarget	Z
    //   5328: invokestatic 2066	android/view/inputmethod/InputMethodManager:peekInstance	()Landroid/view/inputmethod/InputMethodManager;
    //   5331: astore 10
    //   5333: aload 10
    //   5335: ifnull +58 -> 5393
    //   5338: iload 28
    //   5340: ifeq +53 -> 5393
    //   5343: aload 10
    //   5345: aload_0
    //   5346: getfield 1019	android/view/ViewRootImpl:mView	Landroid/view/View;
    //   5349: iload 16
    //   5351: invokevirtual 2070	android/view/inputmethod/InputMethodManager:onPreWindowFocus	(Landroid/view/View;Z)V
    //   5354: aload 10
    //   5356: aload_0
    //   5357: getfield 1019	android/view/ViewRootImpl:mView	Landroid/view/View;
    //   5360: aload_0
    //   5361: getfield 1019	android/view/ViewRootImpl:mView	Landroid/view/View;
    //   5364: invokevirtual 1588	android/view/View:findFocus	()Landroid/view/View;
    //   5367: aload_0
    //   5368: getfield 514	android/view/ViewRootImpl:mWindowAttributes	Landroid/view/WindowManager$LayoutParams;
    //   5371: getfield 2094	android/view/WindowManager$LayoutParams:softInputMode	I
    //   5374: aload_0
    //   5375: getfield 2096	android/view/ViewRootImpl:mHasHadWindowFocus	Z
    //   5378: iconst_1
    //   5379: ixor
    //   5380: aload_0
    //   5381: getfield 514	android/view/ViewRootImpl:mWindowAttributes	Landroid/view/WindowManager$LayoutParams;
    //   5384: getfield 984	android/view/WindowManager$LayoutParams:flags	I
    //   5387: invokevirtual 2100	android/view/inputmethod/InputMethodManager:onPostWindowFocus	(Landroid/view/View;Landroid/view/View;IZI)V
    //   5390: goto +3 -> 5393
    //   5393: iconst_1
    //   5394: istore 8
    //   5396: iload 7
    //   5398: iconst_2
    //   5399: iand
    //   5400: ifeq +7 -> 5407
    //   5403: aload_0
    //   5404: invokespecial 941	android/view/ViewRootImpl:reportNextDraw	()V
    //   5407: iload 8
    //   5409: istore 7
    //   5411: aload_0
    //   5412: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   5415: getfield 1240	android/view/View$AttachInfo:mTreeObserver	Landroid/view/ViewTreeObserver;
    //   5418: invokevirtual 2713	android/view/ViewTreeObserver:dispatchOnPreDraw	()Z
    //   5421: ifne +18 -> 5439
    //   5424: iload 23
    //   5426: ifne +10 -> 5436
    //   5429: iload 8
    //   5431: istore 7
    //   5433: goto +6 -> 5439
    //   5436: iconst_0
    //   5437: istore 7
    //   5439: iload 7
    //   5441: ifne +74 -> 5515
    //   5444: iload_2
    //   5445: ifne +70 -> 5515
    //   5448: aload_0
    //   5449: getfield 2715	android/view/ViewRootImpl:mPendingTransitions	Ljava/util/ArrayList;
    //   5452: ifnull +56 -> 5508
    //   5455: aload_0
    //   5456: getfield 2715	android/view/ViewRootImpl:mPendingTransitions	Ljava/util/ArrayList;
    //   5459: invokevirtual 1121	java/util/ArrayList:size	()I
    //   5462: ifle +46 -> 5508
    //   5465: iconst_0
    //   5466: istore 7
    //   5468: iload 7
    //   5470: aload_0
    //   5471: getfield 2715	android/view/ViewRootImpl:mPendingTransitions	Ljava/util/ArrayList;
    //   5474: invokevirtual 1121	java/util/ArrayList:size	()I
    //   5477: if_icmpge +24 -> 5501
    //   5480: aload_0
    //   5481: getfield 2715	android/view/ViewRootImpl:mPendingTransitions	Ljava/util/ArrayList;
    //   5484: iload 7
    //   5486: invokevirtual 1124	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   5489: checkcast 2717	android/animation/LayoutTransition
    //   5492: invokevirtual 2720	android/animation/LayoutTransition:startChangingAnimations	()V
    //   5495: iinc 7 1
    //   5498: goto -30 -> 5468
    //   5501: aload_0
    //   5502: getfield 2715	android/view/ViewRootImpl:mPendingTransitions	Ljava/util/ArrayList;
    //   5505: invokevirtual 1875	java/util/ArrayList:clear	()V
    //   5508: aload_0
    //   5509: invokespecial 2722	android/view/ViewRootImpl:performDraw	()V
    //   5512: goto +75 -> 5587
    //   5515: iload 23
    //   5517: ifeq +10 -> 5527
    //   5520: aload_0
    //   5521: invokevirtual 1379	android/view/ViewRootImpl:scheduleTraversals	()V
    //   5524: goto +63 -> 5587
    //   5527: aload_0
    //   5528: getfield 2715	android/view/ViewRootImpl:mPendingTransitions	Ljava/util/ArrayList;
    //   5531: ifnull +56 -> 5587
    //   5534: aload_0
    //   5535: getfield 2715	android/view/ViewRootImpl:mPendingTransitions	Ljava/util/ArrayList;
    //   5538: invokevirtual 1121	java/util/ArrayList:size	()I
    //   5541: ifle +46 -> 5587
    //   5544: iconst_0
    //   5545: istore 7
    //   5547: iload 7
    //   5549: aload_0
    //   5550: getfield 2715	android/view/ViewRootImpl:mPendingTransitions	Ljava/util/ArrayList;
    //   5553: invokevirtual 1121	java/util/ArrayList:size	()I
    //   5556: if_icmpge +24 -> 5580
    //   5559: aload_0
    //   5560: getfield 2715	android/view/ViewRootImpl:mPendingTransitions	Ljava/util/ArrayList;
    //   5563: iload 7
    //   5565: invokevirtual 1124	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   5568: checkcast 2717	android/animation/LayoutTransition
    //   5571: invokevirtual 2725	android/animation/LayoutTransition:endChangingAnimations	()V
    //   5574: iinc 7 1
    //   5577: goto -30 -> 5547
    //   5580: aload_0
    //   5581: getfield 2715	android/view/ViewRootImpl:mPendingTransitions	Ljava/util/ArrayList;
    //   5584: invokevirtual 1875	java/util/ArrayList:clear	()V
    //   5587: aload_0
    //   5588: iconst_0
    //   5589: putfield 2374	android/view/ViewRootImpl:mIsInTraversal	Z
    //   5592: return
    //   5593: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	5594	0	this	ViewRootImpl
    //   4	5248	1	localView	View
    //   30	5415	2	i	int
    //   32	4544	3	j	int
    //   37	4963	4	localObject1	Object
    //   43	5236	5	k	int
    //   76	5025	6	m	int
    //   104	5471	7	n	int
    //   118	1230	8	i1	int
    //   1348	300	8	i2	int
    //   1736	2313	8	i3	int
    //   4099	12	8	i4	int
    //   4184	253	8	i5	int
    //   4488	942	8	i6	int
    //   134	5073	9	i7	int
    //   143	4098	10	localObject2	Object
    //   4258	13	10	localObject3	Object
    //   4280	52	10	localThreadedRenderer1	ThreadedRenderer
    //   4809	1	10	localRemoteException1	RemoteException
    //   4927	71	10	localRect1	Rect
    //   5011	1	10	localRemoteException2	RemoteException
    //   5042	313	10	localObject4	Object
    //   247	4746	11	localObject5	Object
    //   287	3348	12	localObject6	Object
    //   304	1311	13	localObject7	Object
    //   1831	1	13	localRemoteException3	RemoteException
    //   1836	1	13	localRemoteException4	RemoteException
    //   1851	1	13	localRemoteException5	RemoteException
    //   1884	1	13	localRemoteException6	RemoteException
    //   1899	1	13	localRemoteException7	RemoteException
    //   1914	1	13	localRemoteException8	RemoteException
    //   1929	38	13	localRemoteException9	RemoteException
    //   2040	1	13	localRemoteException10	RemoteException
    //   2867	3	13	localThreadedRenderer2	ThreadedRenderer
    //   2920	1	13	localRemoteException11	RemoteException
    //   2931	1	13	localOutOfResourcesException1	Surface.OutOfResourcesException
    //   2950	8	13	localOutOfResourcesException2	Surface.OutOfResourcesException
    //   2973	1	13	localRemoteException12	RemoteException
    //   3316	11	13	localBaseSurfaceHolder	BaseSurfaceHolder
    //   3409	14	13	localOutOfResourcesException3	Surface.OutOfResourcesException
    //   3573	56	13	localRect2	Rect
    //   3643	1	13	localRemoteException13	RemoteException
    //   3667	1	13	localRemoteException14	RemoteException
    //   3704	50	13	localObject8	Object
    //   3779	1	13	localRemoteException15	RemoteException
    //   3799	1	13	localRemoteException16	RemoteException
    //   3823	1	13	localRemoteException17	RemoteException
    //   3838	1	13	localRemoteException18	RemoteException
    //   4226	776	13	localObject9	Object
    //   453	4065	14	i8	int
    //   460	2366	15	i9	int
    //   2941	1616	15	i10	int
    //   539	450	16	i11	int
    //   1274	4076	16	bool1	boolean
    //   651	3932	17	i12	int
    //   1026	2373	18	i13	int
    //   1029	2269	19	i14	int
    //   1032	2819	20	i15	int
    //   1035	1084	21	i16	int
    //   1038	2509	22	i17	int
    //   1129	4387	23	i18	int
    //   1132	2974	24	i19	int
    //   1544	3294	25	i20	int
    //   1564	2290	26	i21	int
    //   1584	1706	27	bool2	boolean
    //   1636	3703	28	bool3	boolean
    //   1739	277	29	i22	int
    //   1748	265	30	i23	int
    //   1757	2397	31	bool4	boolean
    //   1794	2023	32	bool5	boolean
    //   1948	1766	33	bool6	boolean
    //   1951	924	34	bool7	boolean
    //   1954	1449	35	bool8	boolean
    //   1957	1352	36	bool9	boolean
    //   1963	80	37	bool10	boolean
    //   2110	513	38	bool11	boolean
    //   2214	231	39	bool12	boolean
    //   2248	231	40	bool13	boolean
    //   2282	347	41	bool14	boolean
    //   2328	34	42	bool15	boolean
    //   2355	7	43	bool16	boolean
    // Exception table:
    //   from	to	target	type
    //   1818	1828	1831	android/os/RemoteException
    //   1813	1818	1836	android/os/RemoteException
    //   1801	1813	1851	android/os/RemoteException
    //   1875	1881	1884	android/os/RemoteException
    //   1866	1875	1899	android/os/RemoteException
    //   1785	1796	1914	android/os/RemoteException
    //   1773	1782	1929	android/os/RemoteException
    //   2019	2034	2040	android/os/RemoteException
    //   2388	2402	2040	android/os/RemoteException
    //   2422	2436	2040	android/os/RemoteException
    //   2455	2469	2040	android/os/RemoteException
    //   2492	2506	2040	android/os/RemoteException
    //   2523	2534	2040	android/os/RemoteException
    //   2552	2566	2040	android/os/RemoteException
    //   2576	2583	2040	android/os/RemoteException
    //   2593	2599	2040	android/os/RemoteException
    //   2609	2618	2040	android/os/RemoteException
    //   2785	2799	2040	android/os/RemoteException
    //   2814	2823	2040	android/os/RemoteException
    //   3022	3029	2040	android/os/RemoteException
    //   3039	3046	2040	android/os/RemoteException
    //   3056	3061	2040	android/os/RemoteException
    //   3071	3076	2040	android/os/RemoteException
    //   3086	3096	2040	android/os/RemoteException
    //   3106	3122	2040	android/os/RemoteException
    //   3132	3139	2040	android/os/RemoteException
    //   3149	3156	2040	android/os/RemoteException
    //   3174	3184	2040	android/os/RemoteException
    //   3202	3215	2040	android/os/RemoteException
    //   3225	3235	2040	android/os/RemoteException
    //   3349	3359	2040	android/os/RemoteException
    //   3369	3374	2040	android/os/RemoteException
    //   3384	3398	2040	android/os/RemoteException
    //   3421	3427	2040	android/os/RemoteException
    //   2899	2917	2920	android/os/RemoteException
    //   2899	2917	2931	android/view/Surface$OutOfResourcesException
    //   2878	2894	2950	android/view/Surface$OutOfResourcesException
    //   2840	2845	2973	android/os/RemoteException
    //   2849	2856	2973	android/os/RemoteException
    //   2860	2869	2973	android/os/RemoteException
    //   2878	2894	2973	android/os/RemoteException
    //   2956	2962	2973	android/os/RemoteException
    //   3384	3398	3409	android/view/Surface$OutOfResourcesException
    //   3616	3627	3643	android/os/RemoteException
    //   3599	3616	3667	android/os/RemoteException
    //   3627	3640	3775	android/os/RemoteException
    //   3691	3695	3775	android/os/RemoteException
    //   3706	3713	3775	android/os/RemoteException
    //   3718	3740	3775	android/os/RemoteException
    //   3743	3753	3775	android/os/RemoteException
    //   1991	2004	3799	android/os/RemoteException
    //   2096	2112	3799	android/os/RemoteException
    //   2130	2148	3799	android/os/RemoteException
    //   2166	2182	3799	android/os/RemoteException
    //   2200	2216	3799	android/os/RemoteException
    //   2234	2250	3799	android/os/RemoteException
    //   2268	2284	3799	android/os/RemoteException
    //   2324	2330	3799	android/os/RemoteException
    //   2348	2357	3799	android/os/RemoteException
    //   2650	2661	3799	android/os/RemoteException
    //   2679	2690	3799	android/os/RemoteException
    //   2708	2722	3799	android/os/RemoteException
    //   2740	2745	3799	android/os/RemoteException
    //   2763	2768	3799	android/os/RemoteException
    //   2998	3007	3799	android/os/RemoteException
    //   3264	3276	3799	android/os/RemoteException
    //   3312	3318	3799	android/os/RemoteException
    //   3499	3508	3799	android/os/RemoteException
    //   3545	3551	3799	android/os/RemoteException
    //   3569	3575	3799	android/os/RemoteException
    //   3593	3599	3799	android/os/RemoteException
    //   1965	1977	3823	android/os/RemoteException
    //   1759	1768	3838	android/os/RemoteException
    //   4222	4245	4258	finally
    //   4789	4806	4809	android/os/RemoteException
    //   4984	5008	5011	android/os/RemoteException
  }
  
  private void postDrawFinished()
  {
    mHandler.sendEmptyMessage(29);
  }
  
  private void postSendWindowContentChangedCallback(View paramView, int paramInt)
  {
    if (mSendWindowContentChangedAccessibilityEvent == null) {
      mSendWindowContentChangedAccessibilityEvent = new SendWindowContentChangedAccessibilityEvent(null);
    }
    mSendWindowContentChangedAccessibilityEvent.runOrPost(paramView, paramInt);
  }
  
  private void profileRendering(boolean paramBoolean)
  {
    if (mProfileRendering)
    {
      mRenderProfilingEnabled = paramBoolean;
      if (mRenderProfiler != null) {
        mChoreographer.removeFrameCallback(mRenderProfiler);
      }
      if (mRenderProfilingEnabled)
      {
        if (mRenderProfiler == null) {
          mRenderProfiler = new Choreographer.FrameCallback()
          {
            public void doFrame(long paramAnonymousLong)
            {
              mDirty.set(0, 0, mWidth, mHeight);
              scheduleTraversals();
              if (mRenderProfilingEnabled) {
                mChoreographer.postFrameCallback(mRenderProfiler);
              }
            }
          };
        }
        mChoreographer.postFrameCallback(mRenderProfiler);
      }
      else
      {
        mRenderProfiler = null;
      }
    }
  }
  
  private void recycleQueuedInputEvent(QueuedInputEvent paramQueuedInputEvent)
  {
    mEvent = null;
    mReceiver = null;
    if (mQueuedInputEventPoolSize < 10)
    {
      mQueuedInputEventPoolSize += 1;
      mNext = mQueuedInputEventPool;
      mQueuedInputEventPool = paramQueuedInputEvent;
    }
  }
  
  private int relayoutWindow(WindowManager.LayoutParams paramLayoutParams, int paramInt, boolean paramBoolean)
    throws RemoteException
  {
    float f = mAttachInfo.mApplicationScale;
    int i = 0;
    int j = i;
    if (paramLayoutParams != null)
    {
      j = i;
      if (mTranslator != null)
      {
        j = 1;
        paramLayoutParams.backup();
        mTranslator.translateWindowLayout(paramLayoutParams);
      }
    }
    if ((paramLayoutParams != null) && (mOrigWindowType != type) && (mTargetSdkVersion < 14))
    {
      String str = mTag;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Window type can not be changed after the window is added; ignoring change of ");
      localStringBuilder.append(mView);
      Slog.w(str, localStringBuilder.toString());
      type = mOrigWindowType;
    }
    long l = -1L;
    if (mSurface.isValid()) {
      l = mSurface.getNextFrameNumber();
    }
    paramInt = mWindowSession.relayout(mWindow, mSeq, paramLayoutParams, (int)(mView.getMeasuredWidth() * f + 0.5F), (int)(mView.getMeasuredHeight() * f + 0.5F), paramInt, paramBoolean, l, mWinFrame, mPendingOverscanInsets, mPendingContentInsets, mPendingVisibleInsets, mPendingStableInsets, mPendingOutsets, mPendingBackDropFrame, mPendingDisplayCutout, mPendingMergedConfiguration, mSurface);
    boolean bool;
    if ((paramInt & 0x40) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    mPendingAlwaysConsumeNavBar = bool;
    if (j != 0) {
      paramLayoutParams.restore();
    }
    if (mTranslator != null)
    {
      mTranslator.translateRectInScreenToAppWinFrame(mWinFrame);
      mTranslator.translateRectInScreenToAppWindow(mPendingOverscanInsets);
      mTranslator.translateRectInScreenToAppWindow(mPendingContentInsets);
      mTranslator.translateRectInScreenToAppWindow(mPendingVisibleInsets);
      mTranslator.translateRectInScreenToAppWindow(mPendingStableInsets);
    }
    return paramInt;
  }
  
  private void removeSendWindowContentChangedCallback()
  {
    if (mSendWindowContentChangedAccessibilityEvent != null) {
      mHandler.removeCallbacks(mSendWindowContentChangedAccessibilityEvent);
    }
  }
  
  private void reportDrawFinished()
  {
    try
    {
      mDrawsNeededToReport = 0;
      mWindowSession.finishDrawing(mWindow);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  private void reportNextDraw()
  {
    if (!mReportNextDraw) {
      drawPending();
    }
    mReportNextDraw = true;
  }
  
  private void requestDrawWindow()
  {
    if (!mUseMTRenderer) {
      return;
    }
    mWindowDrawCountDown = new CountDownLatch(mWindowCallbacks.size());
    for (int i = mWindowCallbacks.size() - 1; i >= 0; i--) {
      ((WindowCallbacks)mWindowCallbacks.get(i)).onRequestDraw(mReportNextDraw);
    }
  }
  
  private void resetPointerIcon(MotionEvent paramMotionEvent)
  {
    mPointerIconType = 1;
    updatePointerIcon(paramMotionEvent);
  }
  
  private void scheduleProcessInputEvents()
  {
    if (!mProcessInputEventsScheduled)
    {
      mProcessInputEventsScheduled = true;
      Message localMessage = mHandler.obtainMessage(19);
      localMessage.setAsynchronous(true);
      mHandler.sendMessage(localMessage);
    }
  }
  
  private void setTag()
  {
    String[] arrayOfString = mWindowAttributes.getTitle().toString().split("\\.");
    if (arrayOfString.length > 0)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("ViewRootImpl[");
      localStringBuilder.append(arrayOfString[(arrayOfString.length - 1)]);
      localStringBuilder.append("]");
      mTag = localStringBuilder.toString();
    }
  }
  
  private static boolean shouldUseDisplaySize(WindowManager.LayoutParams paramLayoutParams)
  {
    boolean bool;
    if ((type != 2014) && (type != 2011) && (type != 2020)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private void startDragResizing(Rect paramRect1, boolean paramBoolean, Rect paramRect2, Rect paramRect3, int paramInt)
  {
    if (!mDragResizing)
    {
      mDragResizing = true;
      if (mUseMTRenderer) {
        for (int i = mWindowCallbacks.size() - 1; i >= 0; i--) {
          ((WindowCallbacks)mWindowCallbacks.get(i)).onWindowDragResizeStart(paramRect1, paramBoolean, paramRect2, paramRect3, paramInt);
        }
      }
      mFullRedrawNeeded = true;
    }
  }
  
  private void trackFPS()
  {
    long l1 = System.currentTimeMillis();
    if (mFpsStartTime < 0L)
    {
      mFpsPrevTime = l1;
      mFpsStartTime = l1;
      mFpsNumFrames = 0;
    }
    else
    {
      mFpsNumFrames += 1;
      String str = Integer.toHexString(System.identityHashCode(this));
      long l2 = mFpsPrevTime;
      long l3 = l1 - mFpsStartTime;
      Object localObject1 = mTag;
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("0x");
      ((StringBuilder)localObject2).append(str);
      ((StringBuilder)localObject2).append("\tFrame time:\t");
      ((StringBuilder)localObject2).append(l1 - l2);
      Log.v((String)localObject1, ((StringBuilder)localObject2).toString());
      mFpsPrevTime = l1;
      if (l3 > 1000L)
      {
        float f = mFpsNumFrames * 1000.0F / (float)l3;
        localObject2 = mTag;
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("0x");
        ((StringBuilder)localObject1).append(str);
        ((StringBuilder)localObject1).append("\tFPS:\t");
        ((StringBuilder)localObject1).append(f);
        Log.v((String)localObject2, ((StringBuilder)localObject1).toString());
        mFpsStartTime = l1;
        mFpsNumFrames = 0;
      }
    }
  }
  
  private boolean updateContentDrawBounds()
  {
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = mUseMTRenderer;
    boolean bool4 = true;
    if (bool3) {
      for (int i = mWindowCallbacks.size() - 1;; i--)
      {
        bool1 = bool2;
        if (i < 0) {
          break;
        }
        bool2 |= ((WindowCallbacks)mWindowCallbacks.get(i)).onContentDrawn(mWindowAttributes.surfaceInsets.left, mWindowAttributes.surfaceInsets.top, mWidth, mHeight);
      }
    }
    if ((mDragResizing) && (mReportNextDraw)) {
      bool2 = bool4;
    } else {
      bool2 = false;
    }
    return bool1 | bool2;
  }
  
  private boolean updatePointerIcon(MotionEvent paramMotionEvent)
  {
    float f1 = paramMotionEvent.getX(0);
    float f2 = paramMotionEvent.getY(0);
    if (mView == null)
    {
      Slog.d(mTag, "updatePointerIcon called after view was removed");
      return false;
    }
    if ((f1 >= 0.0F) && (f1 < mView.getWidth()) && (f2 >= 0.0F) && (f2 < mView.getHeight()))
    {
      paramMotionEvent = mView.onResolvePointerIcon(paramMotionEvent, 0);
      int i;
      if (paramMotionEvent != null) {
        i = paramMotionEvent.getType();
      } else {
        i = 1000;
      }
      if (mPointerIconType != i)
      {
        mPointerIconType = i;
        mCustomPointerIcon = null;
        if (mPointerIconType != -1)
        {
          InputManager.getInstance().setPointerIconType(i);
          return true;
        }
      }
      if ((mPointerIconType == -1) && (!paramMotionEvent.equals(mCustomPointerIcon)))
      {
        mCustomPointerIcon = paramMotionEvent;
        InputManager.getInstance().setCustomPointerIcon(mCustomPointerIcon);
      }
      return true;
    }
    Slog.d(mTag, "updatePointerIcon called with position out of bounds");
    return false;
  }
  
  public void addWindowCallbacks(WindowCallbacks paramWindowCallbacks)
  {
    synchronized (mWindowCallbacks)
    {
      mWindowCallbacks.add(paramWindowCallbacks);
      return;
    }
  }
  
  void addWindowStoppedCallback(WindowStoppedCallback paramWindowStoppedCallback)
  {
    mWindowStoppedCallbacks.add(paramWindowStoppedCallback);
  }
  
  public void bringChildToFront(View paramView) {}
  
  public boolean canResolveLayoutDirection()
  {
    return true;
  }
  
  public boolean canResolveTextAlignment()
  {
    return true;
  }
  
  public boolean canResolveTextDirection()
  {
    return true;
  }
  
  public void cancelInvalidate(View paramView)
  {
    mHandler.removeMessages(1, paramView);
    mHandler.removeMessages(2, paramView);
    mInvalidateOnAnimationRunnable.removeView(paramView);
  }
  
  void changeCanvasOpacity(boolean paramBoolean)
  {
    String str = mTag;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("changeCanvasOpacity: opaque=");
    localStringBuilder.append(paramBoolean);
    Log.d(str, localStringBuilder.toString());
    if (mAttachInfo.mThreadedRenderer != null) {
      mAttachInfo.mThreadedRenderer.setOpaque(paramBoolean);
    }
  }
  
  void checkThread()
  {
    if (mThread == Thread.currentThread()) {
      return;
    }
    throw new CalledFromWrongThreadException("Only the original thread that created a view hierarchy can touch its views.");
  }
  
  public void childDrawableStateChanged(View paramView) {}
  
  public void childHasTransientStateChanged(View paramView, boolean paramBoolean) {}
  
  public void clearChildFocus(View paramView)
  {
    checkThread();
    scheduleTraversals();
  }
  
  public void createContextMenu(ContextMenu paramContextMenu) {}
  
  public void debug()
  {
    mView.debug();
  }
  
  void destroyHardwareResources()
  {
    if (mAttachInfo.mThreadedRenderer != null)
    {
      mAttachInfo.mThreadedRenderer.destroyHardwareResources(mView);
      mAttachInfo.mThreadedRenderer.destroy();
    }
  }
  
  public void detachFunctor(long paramLong)
  {
    if (mAttachInfo.mThreadedRenderer != null) {
      mAttachInfo.mThreadedRenderer.stopDrawing();
    }
  }
  
  boolean die(boolean paramBoolean)
  {
    if ((paramBoolean) && (!mIsInTraversal))
    {
      doDie();
      return false;
    }
    if (!mIsDrawing)
    {
      destroyHardwareRenderer();
    }
    else
    {
      String str = mTag;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Attempting to destroy the window while drawing!\n  window=");
      localStringBuilder.append(this);
      localStringBuilder.append(", title=");
      localStringBuilder.append(mWindowAttributes.getTitle());
      Log.e(str, localStringBuilder.toString());
    }
    mHandler.sendEmptyMessage(3);
    return true;
  }
  
  public void dispatchAppVisibility(boolean paramBoolean)
  {
    Message localMessage = mHandler.obtainMessage(8);
    arg1 = paramBoolean;
    mHandler.sendMessage(localMessage);
  }
  
  void dispatchApplyInsets(View paramView)
  {
    int i = 1;
    WindowInsets localWindowInsets1 = getWindowInsets(true);
    if (mWindowAttributes.layoutInDisplayCutoutMode != 1) {
      i = 0;
    }
    WindowInsets localWindowInsets2 = localWindowInsets1;
    if (i == 0) {
      localWindowInsets2 = localWindowInsets1.consumeDisplayCutout();
    }
    paramView.dispatchApplyWindowInsets(localWindowInsets2);
  }
  
  public void dispatchCheckFocus()
  {
    if (!mHandler.hasMessages(13)) {
      mHandler.sendEmptyMessage(13);
    }
  }
  
  public void dispatchCloseSystemDialogs(String paramString)
  {
    Message localMessage = Message.obtain();
    what = 14;
    obj = paramString;
    mHandler.sendMessage(localMessage);
  }
  
  void dispatchDetachedFromWindow()
  {
    mFirstInputStage.onDetachedFromWindow();
    if ((mView != null) && (mView.mAttachInfo != null))
    {
      mAttachInfo.mTreeObserver.dispatchOnWindowAttachedChange(false);
      mView.dispatchDetachedFromWindow();
    }
    mAccessibilityInteractionConnectionManager.ensureNoConnection();
    mAccessibilityManager.removeAccessibilityStateChangeListener(mAccessibilityInteractionConnectionManager);
    mAccessibilityManager.removeHighTextContrastStateChangeListener(mHighContrastTextManager);
    removeSendWindowContentChangedCallback();
    destroyHardwareRenderer();
    setAccessibilityFocus(null, null);
    mView.assignParent(null);
    mView = null;
    mAttachInfo.mRootView = null;
    mSurface.release();
    if ((mInputQueueCallback != null) && (mInputQueue != null))
    {
      mInputQueueCallback.onInputQueueDestroyed(mInputQueue);
      mInputQueue.dispose();
      mInputQueueCallback = null;
      mInputQueue = null;
    }
    if (mInputEventReceiver != null)
    {
      mInputEventReceiver.dispose();
      mInputEventReceiver = null;
    }
    try
    {
      mWindowSession.remove(mWindow);
    }
    catch (RemoteException localRemoteException) {}
    if (mInputChannel != null)
    {
      mInputChannel.dispose();
      mInputChannel = null;
    }
    mDisplayManager.unregisterDisplayListener(mDisplayListener);
    unscheduleTraversals();
  }
  
  public void dispatchDragEvent(DragEvent paramDragEvent)
  {
    int i;
    if (paramDragEvent.getAction() == 2)
    {
      i = 16;
      mHandler.removeMessages(16);
    }
    else
    {
      i = 15;
    }
    paramDragEvent = mHandler.obtainMessage(i, paramDragEvent);
    mHandler.sendMessage(paramDragEvent);
  }
  
  public void dispatchGetNewSurface()
  {
    Message localMessage = mHandler.obtainMessage(9);
    mHandler.sendMessage(localMessage);
  }
  
  public void dispatchInputEvent(InputEvent paramInputEvent)
  {
    dispatchInputEvent(paramInputEvent, null);
  }
  
  public void dispatchInputEvent(InputEvent paramInputEvent, InputEventReceiver paramInputEventReceiver)
  {
    SomeArgs localSomeArgs = SomeArgs.obtain();
    arg1 = paramInputEvent;
    arg2 = paramInputEventReceiver;
    paramInputEvent = mHandler.obtainMessage(7, localSomeArgs);
    paramInputEvent.setAsynchronous(true);
    mHandler.sendMessage(paramInputEvent);
  }
  
  public void dispatchInvalidateDelayed(View paramView, long paramLong)
  {
    paramView = mHandler.obtainMessage(1, paramView);
    mHandler.sendMessageDelayed(paramView, paramLong);
  }
  
  public void dispatchInvalidateOnAnimation(View paramView)
  {
    mInvalidateOnAnimationRunnable.addView(paramView);
  }
  
  public void dispatchInvalidateRectDelayed(View.AttachInfo.InvalidateInfo paramInvalidateInfo, long paramLong)
  {
    paramInvalidateInfo = mHandler.obtainMessage(2, paramInvalidateInfo);
    mHandler.sendMessageDelayed(paramInvalidateInfo, paramLong);
  }
  
  public void dispatchInvalidateRectOnAnimation(View.AttachInfo.InvalidateInfo paramInvalidateInfo)
  {
    mInvalidateOnAnimationRunnable.addViewRect(paramInvalidateInfo);
  }
  
  public void dispatchKeyFromAutofill(KeyEvent paramKeyEvent)
  {
    paramKeyEvent = mHandler.obtainMessage(12, paramKeyEvent);
    paramKeyEvent.setAsynchronous(true);
    mHandler.sendMessage(paramKeyEvent);
  }
  
  public void dispatchKeyFromIme(KeyEvent paramKeyEvent)
  {
    paramKeyEvent = mHandler.obtainMessage(11, paramKeyEvent);
    paramKeyEvent.setAsynchronous(true);
    mHandler.sendMessage(paramKeyEvent);
  }
  
  public void dispatchMoved(int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    int j = paramInt2;
    if (mTranslator != null)
    {
      localObject = new PointF(paramInt1, paramInt2);
      mTranslator.translatePointInScreenToAppWindow((PointF)localObject);
      i = (int)(x + 0.5D);
      j = (int)(y + 0.5D);
    }
    Object localObject = mHandler.obtainMessage(23, i, j);
    mHandler.sendMessage((Message)localObject);
  }
  
  public void dispatchNavigationColor()
  {
    Message localMessage = mHandler.obtainMessage(1000);
    mHandler.sendMessageDelayed(localMessage, 213L);
  }
  
  public void dispatchPointerCaptureChanged(boolean paramBoolean)
  {
    mHandler.removeMessages(28);
    Message localMessage = mHandler.obtainMessage(28);
    arg1 = paramBoolean;
    mHandler.sendMessage(localMessage);
  }
  
  public void dispatchRequestKeyboardShortcuts(IResultReceiver paramIResultReceiver, int paramInt)
  {
    mHandler.obtainMessage(26, paramInt, 0, paramIResultReceiver).sendToTarget();
  }
  
  public void dispatchSystemUiVisibilityChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    SystemUiVisibilityInfo localSystemUiVisibilityInfo = new SystemUiVisibilityInfo();
    seq = paramInt1;
    globalVisibility = paramInt2;
    localValue = paramInt3;
    localChanges = paramInt4;
    mHandler.sendMessage(mHandler.obtainMessage(17, localSystemUiVisibilityInfo));
  }
  
  public void dispatchUnhandledInputEvent(InputEvent paramInputEvent)
  {
    Object localObject = paramInputEvent;
    if ((paramInputEvent instanceof MotionEvent)) {
      localObject = MotionEvent.obtain((MotionEvent)paramInputEvent);
    }
    synthesizeInputEvent((InputEvent)localObject);
  }
  
  public boolean dispatchUnhandledKeyEvent(KeyEvent paramKeyEvent)
  {
    return mUnhandledKeyManager.dispatch(mView, paramKeyEvent);
  }
  
  public void dispatchWindowShown()
  {
    mHandler.sendEmptyMessage(25);
  }
  
  void doConsumeBatchedInput(long paramLong)
  {
    if (mConsumeBatchedInputScheduled)
    {
      mConsumeBatchedInputScheduled = false;
      if ((mInputEventReceiver != null) && (mInputEventReceiver.consumeBatchedInputEvents(paramLong)) && (paramLong != -1L)) {
        scheduleConsumeBatchedInput();
      }
      doProcessInputEvents();
    }
  }
  
  void doDie()
  {
    checkThread();
    try
    {
      if (mRemoved) {
        return;
      }
      int i = 1;
      mRemoved = true;
      if (mAdded) {
        dispatchDetachedFromWindow();
      }
      if ((mAdded) && (!mFirst))
      {
        destroyHardwareRenderer();
        if (mView != null)
        {
          int j = mView.getVisibility();
          if (mViewVisibility == j) {
            i = 0;
          }
          boolean bool = mWindowAttributesChanged;
          if ((bool) || (i != 0)) {
            try
            {
              if ((relayoutWindow(mWindowAttributes, j, false) & 0x2) != 0) {
                mWindowSession.finishDrawing(mWindow);
              }
            }
            catch (RemoteException localRemoteException) {}
          }
          mSurface.release();
        }
      }
      mAdded = false;
      WindowManagerGlobal.getInstance().doRemoveView(this);
      return;
    }
    finally {}
  }
  
  void doProcessInputEvents()
  {
    while (mPendingInputEventHead != null)
    {
      QueuedInputEvent localQueuedInputEvent = mPendingInputEventHead;
      mPendingInputEventHead = mNext;
      if (mPendingInputEventHead == null) {
        mPendingInputEventTail = null;
      }
      mNext = null;
      mPendingInputEventCount -= 1;
      Trace.traceCounter(4L, mPendingInputEventQueueLengthCounterName, mPendingInputEventCount);
      long l1 = mEvent.getEventTimeNano();
      long l2 = l1;
      long l3 = l2;
      if ((mEvent instanceof MotionEvent))
      {
        MotionEvent localMotionEvent = (MotionEvent)mEvent;
        l3 = l2;
        if (localMotionEvent.getHistorySize() > 0) {
          l3 = localMotionEvent.getHistoricalEventTimeNano(0);
        }
      }
      mChoreographer.mFrameInfo.updateInputEventTime(l1, l3);
      deliverInputEvent(localQueuedInputEvent);
    }
    if (mProcessInputEventsScheduled)
    {
      mProcessInputEventsScheduled = false;
      mHandler.removeMessages(19);
    }
  }
  
  void doTraversal()
  {
    if (mTraversalScheduled)
    {
      mTraversalScheduled = false;
      mHandler.getLooper().getQueue().removeSyncBarrier(mTraversalBarrier);
      if (mProfile) {
        Debug.startMethodTracing("ViewAncestor");
      }
      performTraversals();
      if (mProfile)
      {
        Debug.stopMethodTracing();
        mProfile = false;
      }
    }
  }
  
  void drawPending()
  {
    mDrawsNeededToReport += 1;
  }
  
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(paramString);
    paramFileDescriptor.append("  ");
    paramFileDescriptor = paramFileDescriptor.toString();
    paramPrintWriter.print(paramString);
    paramPrintWriter.println("ViewRoot:");
    paramPrintWriter.print(paramFileDescriptor);
    paramPrintWriter.print("mAdded=");
    paramPrintWriter.print(mAdded);
    paramPrintWriter.print(" mRemoved=");
    paramPrintWriter.println(mRemoved);
    paramPrintWriter.print(paramFileDescriptor);
    paramPrintWriter.print("mConsumeBatchedInputScheduled=");
    paramPrintWriter.println(mConsumeBatchedInputScheduled);
    paramPrintWriter.print(paramFileDescriptor);
    paramPrintWriter.print("mConsumeBatchedInputImmediatelyScheduled=");
    paramPrintWriter.println(mConsumeBatchedInputImmediatelyScheduled);
    paramPrintWriter.print(paramFileDescriptor);
    paramPrintWriter.print("mPendingInputEventCount=");
    paramPrintWriter.println(mPendingInputEventCount);
    paramPrintWriter.print(paramFileDescriptor);
    paramPrintWriter.print("mProcessInputEventsScheduled=");
    paramPrintWriter.println(mProcessInputEventsScheduled);
    paramPrintWriter.print(paramFileDescriptor);
    paramPrintWriter.print("mTraversalScheduled=");
    paramPrintWriter.print(mTraversalScheduled);
    paramPrintWriter.print(paramFileDescriptor);
    paramPrintWriter.print("mIsAmbientMode=");
    paramPrintWriter.print(mIsAmbientMode);
    if (mTraversalScheduled)
    {
      paramPrintWriter.print(" (barrier=");
      paramPrintWriter.print(mTraversalBarrier);
      paramPrintWriter.println(")");
    }
    else
    {
      paramPrintWriter.println();
    }
    mFirstInputStage.dump(paramFileDescriptor, paramPrintWriter);
    mChoreographer.dump(paramString, paramPrintWriter);
    paramPrintWriter.print(paramString);
    paramPrintWriter.println("View Hierarchy:");
    dumpViewHierarchy(paramFileDescriptor, paramPrintWriter, mView);
  }
  
  public void dumpGfxInfo(int[] paramArrayOfInt)
  {
    paramArrayOfInt[1] = 0;
    paramArrayOfInt[0] = 0;
    if (mView != null) {
      getGfxInfo(mView, paramArrayOfInt);
    }
  }
  
  void enqueueInputEvent(InputEvent paramInputEvent)
  {
    enqueueInputEvent(paramInputEvent, null, 0, false);
  }
  
  void enqueueInputEvent(InputEvent paramInputEvent, InputEventReceiver paramInputEventReceiver, int paramInt, boolean paramBoolean)
  {
    adjustInputEventForCompatibility(paramInputEvent);
    paramInputEvent = obtainQueuedInputEvent(paramInputEvent, paramInputEventReceiver, paramInt);
    paramInputEventReceiver = mPendingInputEventTail;
    if (paramInputEventReceiver == null)
    {
      mPendingInputEventHead = paramInputEvent;
      mPendingInputEventTail = paramInputEvent;
    }
    else
    {
      mNext = paramInputEvent;
      mPendingInputEventTail = paramInputEvent;
    }
    mPendingInputEventCount += 1;
    Trace.traceCounter(4L, mPendingInputEventQueueLengthCounterName, mPendingInputEventCount);
    if (paramBoolean) {
      doProcessInputEvents();
    } else {
      scheduleProcessInputEvents();
    }
  }
  
  boolean ensureTouchMode(boolean paramBoolean)
  {
    if (mAttachInfo.mInTouchMode == paramBoolean) {
      return false;
    }
    try
    {
      mWindowSession.setInTouchMode(paramBoolean);
      return ensureTouchModeLocally(paramBoolean);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeException(localRemoteException);
    }
  }
  
  public View focusSearch(View paramView, int paramInt)
  {
    checkThread();
    if (!(mView instanceof ViewGroup)) {
      return null;
    }
    return FocusFinder.getInstance().findNextFocus((ViewGroup)mView, paramView, paramInt);
  }
  
  public void focusableViewAvailable(View paramView)
  {
    checkThread();
    if (mView != null) {
      if (!mView.hasFocus())
      {
        if ((sAlwaysAssignFocus) || (!mAttachInfo.mInTouchMode)) {
          paramView.requestFocus();
        }
      }
      else
      {
        View localView = mView.findFocus();
        if (((localView instanceof ViewGroup)) && (((ViewGroup)localView).getDescendantFocusability() == 262144) && (isViewDescendantOf(paramView, localView))) {
          paramView.requestFocus();
        }
      }
    }
  }
  
  public View getAccessibilityFocusedHost()
  {
    return mAccessibilityFocusedHost;
  }
  
  public AccessibilityNodeInfo getAccessibilityFocusedVirtualView()
  {
    return mAccessibilityFocusedVirtualView;
  }
  
  public AccessibilityInteractionController getAccessibilityInteractionController()
  {
    if (mView != null)
    {
      if (mAccessibilityInteractionController == null) {
        mAccessibilityInteractionController = new AccessibilityInteractionController(this);
      }
      return mAccessibilityInteractionController;
    }
    throw new IllegalStateException("getAccessibilityInteractionController called when there is no mView");
  }
  
  public boolean getChildVisibleRect(View paramView, Rect paramRect, Point paramPoint)
  {
    if (paramView == mView) {
      return paramRect.intersect(0, 0, mWidth, mHeight);
    }
    throw new RuntimeException("child is not mine, honest!");
  }
  
  public int getDisplayId()
  {
    return mDisplay.getDisplayId();
  }
  
  public int getHeight()
  {
    return mHeight;
  }
  
  int getHostVisibility()
  {
    int i;
    if ((!mAppVisible) && (!mForceDecorViewVisibility)) {
      i = 8;
    } else {
      i = mView.getVisibility();
    }
    return i;
  }
  
  public void getLastTouchPoint(Point paramPoint)
  {
    x = ((int)mLastTouchPoint.x);
    y = ((int)mLastTouchPoint.y);
  }
  
  public int getLastTouchSource()
  {
    return mLastTouchSource;
  }
  
  public int getLayoutDirection()
  {
    return 0;
  }
  
  final WindowLeaked getLocation()
  {
    return mLocation;
  }
  
  public ViewParent getParent()
  {
    return null;
  }
  
  public ViewParent getParentForAccessibility()
  {
    return null;
  }
  
  public int getTextAlignment()
  {
    return 1;
  }
  
  public int getTextDirection()
  {
    return 1;
  }
  
  public CharSequence getTitle()
  {
    return mWindowAttributes.getTitle();
  }
  
  public View getView()
  {
    return mView;
  }
  
  public int getWidth()
  {
    return mWidth;
  }
  
  public int getWindowFlags()
  {
    return mWindowAttributes.flags;
  }
  
  WindowInsets getWindowInsets(boolean paramBoolean)
  {
    if ((mLastWindowInsets == null) || (paramBoolean))
    {
      mDispatchContentInsets.set(mAttachInfo.mContentInsets);
      mDispatchStableInsets.set(mAttachInfo.mStableInsets);
      mDispatchDisplayCutout = mAttachInfo.mDisplayCutout.get();
      Rect localRect1 = mDispatchContentInsets;
      Rect localRect2 = mDispatchStableInsets;
      Object localObject1 = mDispatchDisplayCutout;
      Rect localRect3 = localRect1;
      Rect localRect4 = localRect2;
      Object localObject2 = localObject1;
      if (!paramBoolean) {
        if ((mPendingContentInsets.equals(localRect1)) && (mPendingStableInsets.equals(localRect2)))
        {
          localRect3 = localRect1;
          localRect4 = localRect2;
          localObject2 = localObject1;
          if (mPendingDisplayCutout.get().equals(localObject1)) {}
        }
        else
        {
          localRect3 = mPendingContentInsets;
          localRect4 = mPendingStableInsets;
          localObject2 = mPendingDisplayCutout.get();
        }
      }
      localRect2 = mAttachInfo.mOutsets;
      if ((left <= 0) && (top <= 0) && (right <= 0))
      {
        localObject1 = localRect3;
        if (bottom <= 0) {}
      }
      else
      {
        localObject1 = new Rect(left + left, top + top, right + right, bottom + bottom);
      }
      mLastWindowInsets = new WindowInsets(ensureInsetsNonNegative((Rect)localObject1, "content"), null, ensureInsetsNonNegative(localRect4, "stable"), mContext.getResources().getConfiguration().isScreenRound(), mAttachInfo.mAlwaysConsumeNavBar, (DisplayCutout)localObject2);
    }
    return mLastWindowInsets;
  }
  
  void handleAppVisibility(boolean paramBoolean)
  {
    if (mAppVisible != paramBoolean)
    {
      mAppVisible = paramBoolean;
      mAppVisibilityChanged = true;
      scheduleTraversals();
      if (!mAppVisible) {
        WindowManagerGlobal.trimForeground();
      }
    }
  }
  
  public void handleDispatchSystemUiVisibilityChanged(SystemUiVisibilityInfo paramSystemUiVisibilityInfo)
  {
    if (mSeq != seq)
    {
      mSeq = seq;
      mAttachInfo.mForceReportNewAttributes = true;
      scheduleTraversals();
    }
    if (mView == null) {
      return;
    }
    if (localChanges != 0) {
      mView.updateLocalSystemUiVisibility(localValue, localChanges);
    }
    int i = globalVisibility & 0x7;
    if (i != mAttachInfo.mGlobalSystemUiVisibility)
    {
      mAttachInfo.mGlobalSystemUiVisibility = i;
      mView.dispatchSystemUiVisibilityChanged(i);
    }
  }
  
  public void handleDispatchWindowShown()
  {
    mAttachInfo.mTreeObserver.dispatchOnWindowShown();
  }
  
  void handleGetNewSurface()
  {
    mNewSurfaceNeeded = true;
    mFullRedrawNeeded = true;
    scheduleTraversals();
  }
  
  public void handleRequestKeyboardShortcuts(IResultReceiver paramIResultReceiver, int paramInt)
  {
    Bundle localBundle = new Bundle();
    ArrayList localArrayList = new ArrayList();
    if (mView != null) {
      mView.requestKeyboardShortcuts(localArrayList, paramInt);
    }
    localBundle.putParcelableArrayList("shortcuts_array", localArrayList);
    try
    {
      paramIResultReceiver.send(0, localBundle);
    }
    catch (RemoteException paramIResultReceiver) {}
  }
  
  boolean hasPointerCapture()
  {
    return mPointerCapture;
  }
  
  void invalidate()
  {
    mDirty.set(0, 0, mWidth, mHeight);
    if (!mWillDrawSoon) {
      scheduleTraversals();
    }
  }
  
  public void invalidateChild(View paramView, Rect paramRect)
  {
    invalidateChildInParent(null, paramRect);
  }
  
  public ViewParent invalidateChildInParent(int[] paramArrayOfInt, Rect paramRect)
  {
    checkThread();
    if (paramRect == null)
    {
      invalidate();
      return null;
    }
    if ((paramRect.isEmpty()) && (!mIsAnimating)) {
      return null;
    }
    if (mCurScrollY == 0)
    {
      paramArrayOfInt = paramRect;
      if (mTranslator == null) {}
    }
    else
    {
      mTempRect.set(paramRect);
      paramRect = mTempRect;
      if (mCurScrollY != 0) {
        paramRect.offset(0, -mCurScrollY);
      }
      if (mTranslator != null) {
        mTranslator.translateRectInAppWindowToScreen(paramRect);
      }
      paramArrayOfInt = paramRect;
      if (mAttachInfo.mScalingRequired)
      {
        paramRect.inset(-1, -1);
        paramArrayOfInt = paramRect;
      }
    }
    invalidateRectOnScreen(paramArrayOfInt);
    return null;
  }
  
  void invalidateWorld(View paramView)
  {
    paramView.invalidate();
    if ((paramView instanceof ViewGroup))
    {
      paramView = (ViewGroup)paramView;
      for (int i = 0; i < paramView.getChildCount(); i++) {
        invalidateWorld(paramView.getChildAt(i));
      }
    }
  }
  
  boolean isInLayout()
  {
    return mInLayout;
  }
  
  public boolean isLayoutDirectionResolved()
  {
    return true;
  }
  
  public boolean isLayoutRequested()
  {
    return mLayoutRequested;
  }
  
  public boolean isTextAlignmentResolved()
  {
    return true;
  }
  
  public boolean isTextDirectionResolved()
  {
    return true;
  }
  
  public View keyboardNavigationClusterSearch(View paramView, int paramInt)
  {
    checkThread();
    return FocusFinder.getInstance().findNextKeyboardNavigationCluster(mView, paramView, paramInt);
  }
  
  public void loadSystemProperties()
  {
    mHandler.post(new Runnable()
    {
      public void run()
      {
        ViewRootImpl.access$3502(ViewRootImpl.this, SystemProperties.getBoolean("viewroot.profile_rendering", false));
        ViewRootImpl.this.profileRendering(mAttachInfo.mHasWindowFocus);
        if ((mAttachInfo.mThreadedRenderer != null) && (mAttachInfo.mThreadedRenderer.loadSystemProperties())) {
          invalidate();
        }
        boolean bool = SystemProperties.getBoolean("debug.layout", false);
        if (bool != mAttachInfo.mDebugLayout)
        {
          mAttachInfo.mDebugLayout = bool;
          if (!mHandler.hasMessages(22)) {
            mHandler.sendEmptyMessageDelayed(22, 200L);
          }
        }
      }
    });
  }
  
  public void notifyChildRebuilt()
  {
    if ((mView instanceof RootViewSurfaceTaker))
    {
      if (mSurfaceHolderCallback != null) {
        mSurfaceHolder.removeCallback(mSurfaceHolderCallback);
      }
      mSurfaceHolderCallback = ((RootViewSurfaceTaker)mView).willYouTakeTheSurface();
      if (mSurfaceHolderCallback != null)
      {
        mSurfaceHolder = new TakenSurfaceHolder();
        mSurfaceHolder.setFormat(0);
        mSurfaceHolder.addCallback(mSurfaceHolderCallback);
      }
      else
      {
        mSurfaceHolder = null;
      }
      mInputQueueCallback = ((RootViewSurfaceTaker)mView).willYouTakeTheInputQueue();
      if (mInputQueueCallback != null) {
        mInputQueueCallback.onInputQueueCreated(mInputQueue);
      }
    }
  }
  
  void notifyRendererOfFramePending()
  {
    if (mAttachInfo.mThreadedRenderer != null) {
      mAttachInfo.mThreadedRenderer.notifyFramePending();
    }
  }
  
  public void notifySubtreeAccessibilityStateChanged(View paramView1, View paramView2, int paramInt)
  {
    postSendWindowContentChangedCallback((View)Preconditions.checkNotNull(paramView2), paramInt);
  }
  
  public void onDescendantInvalidated(View paramView1, View paramView2)
  {
    if ((mPrivateFlags & 0x40) != 0) {
      mIsAnimating = true;
    }
    invalidate();
  }
  
  public void onMovedToDisplay(int paramInt, Configuration paramConfiguration)
  {
    if (mDisplay.getDisplayId() == paramInt) {
      return;
    }
    mDisplay = ResourcesManager.getInstance().getAdjustedDisplay(paramInt, mView.getResources());
    mAttachInfo.mDisplayState = mDisplay.getState();
    mView.dispatchMovedToDisplay(mDisplay, paramConfiguration);
  }
  
  public boolean onNestedFling(View paramView, float paramFloat1, float paramFloat2, boolean paramBoolean)
  {
    return false;
  }
  
  public boolean onNestedPreFling(View paramView, float paramFloat1, float paramFloat2)
  {
    return false;
  }
  
  public boolean onNestedPrePerformAccessibilityAction(View paramView, int paramInt, Bundle paramBundle)
  {
    return false;
  }
  
  public void onNestedPreScroll(View paramView, int paramInt1, int paramInt2, int[] paramArrayOfInt) {}
  
  public void onNestedScroll(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {}
  
  public void onNestedScrollAccepted(View paramView1, View paramView2, int paramInt) {}
  
  public void onPostDraw(DisplayListCanvas paramDisplayListCanvas)
  {
    drawAccessibilityFocusedDrawableIfNeeded(paramDisplayListCanvas);
    if (mUseMTRenderer) {
      for (int i = mWindowCallbacks.size() - 1; i >= 0; i--) {
        ((WindowCallbacks)mWindowCallbacks.get(i)).onPostDraw(paramDisplayListCanvas);
      }
    }
  }
  
  public void onPreDraw(DisplayListCanvas paramDisplayListCanvas)
  {
    if ((mCurScrollY != 0) && (mHardwareYOffset != 0) && (mAttachInfo.mThreadedRenderer.isOpaque())) {
      paramDisplayListCanvas.drawColor(-16777216);
    }
    paramDisplayListCanvas.translate(-mHardwareXOffset, -mHardwareYOffset);
  }
  
  public boolean onStartNestedScroll(View paramView1, View paramView2, int paramInt)
  {
    return false;
  }
  
  public void onStopNestedScroll(View paramView) {}
  
  public void onWindowTitleChanged()
  {
    mAttachInfo.mForceReportNewAttributes = true;
  }
  
  void outputDisplayList(View paramView)
  {
    mRenderNode.output();
    if (mAttachInfo.mThreadedRenderer != null) {
      mAttachInfo.mThreadedRenderer.serializeDisplayListTree();
    }
  }
  
  void pendingDrawFinished()
  {
    if (mDrawsNeededToReport != 0)
    {
      mDrawsNeededToReport -= 1;
      if (mDrawsNeededToReport == 0) {
        reportDrawFinished();
      }
      return;
    }
    throw new RuntimeException("Unbalanced drawPending/pendingDrawFinished calls");
  }
  
  public boolean performHapticFeedback(int paramInt, boolean paramBoolean)
  {
    try
    {
      paramBoolean = mWindowSession.performHapticFeedback(mWindow, paramInt, paramBoolean);
      return paramBoolean;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public void playSoundEffect(int paramInt)
  {
    checkThread();
    try
    {
      localObject = getAudioManager();
      switch (paramInt)
      {
      default: 
        localObject = new java/lang/IllegalArgumentException;
        break;
      case 4: 
        ((AudioManager)localObject).playSoundEffect(2);
        return;
      case 3: 
        ((AudioManager)localObject).playSoundEffect(4);
        return;
      case 2: 
        ((AudioManager)localObject).playSoundEffect(1);
        return;
      case 1: 
        ((AudioManager)localObject).playSoundEffect(3);
        return;
      case 0: 
        ((AudioManager)localObject).playSoundEffect(0);
        return;
      }
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("unknown effect id ");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(" not defined in ");
      localStringBuilder.append(SoundEffectConstants.class.getCanonicalName());
      ((IllegalArgumentException)localObject).<init>(localStringBuilder.toString());
      throw ((Throwable)localObject);
    }
    catch (IllegalStateException localIllegalStateException)
    {
      String str = mTag;
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("FATAL EXCEPTION when attempting to play sound effect: ");
      ((StringBuilder)localObject).append(localIllegalStateException);
      Log.e(str, ((StringBuilder)localObject).toString());
      localIllegalStateException.printStackTrace();
    }
  }
  
  void pokeDrawLockIfNeeded()
  {
    int i = mAttachInfo.mDisplayState;
    if ((mView != null) && (mAdded) && (mTraversalScheduled) && ((i == 3) || (i == 4))) {
      try
      {
        mWindowSession.pokeDrawLock(mWindow);
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  public void profile()
  {
    mProfile = true;
  }
  
  public void recomputeViewAttributes(View paramView)
  {
    checkThread();
    if (mView == paramView)
    {
      mAttachInfo.mRecomputeGlobalAttributes = true;
      if (!mWillDrawSoon) {
        scheduleTraversals();
      }
    }
  }
  
  public void registerAnimatingRenderNode(RenderNode paramRenderNode)
  {
    if (mAttachInfo.mThreadedRenderer != null)
    {
      mAttachInfo.mThreadedRenderer.registerAnimatingRenderNode(paramRenderNode);
    }
    else
    {
      if (mAttachInfo.mPendingAnimatingRenderNodes == null) {
        mAttachInfo.mPendingAnimatingRenderNodes = new ArrayList();
      }
      mAttachInfo.mPendingAnimatingRenderNodes.add(paramRenderNode);
    }
  }
  
  public void registerRtFrameCallback(ThreadedRenderer.FrameDrawingCallback paramFrameDrawingCallback)
  {
    mNextRtFrameCallback = paramFrameDrawingCallback;
  }
  
  public void registerVectorDrawableAnimator(AnimatedVectorDrawable.VectorDrawableAnimatorRT paramVectorDrawableAnimatorRT)
  {
    if (mAttachInfo.mThreadedRenderer != null) {
      mAttachInfo.mThreadedRenderer.registerVectorDrawableAnimator(paramVectorDrawableAnimatorRT);
    }
  }
  
  public void removeWindowCallbacks(WindowCallbacks paramWindowCallbacks)
  {
    synchronized (mWindowCallbacks)
    {
      mWindowCallbacks.remove(paramWindowCallbacks);
      return;
    }
  }
  
  void removeWindowStoppedCallback(WindowStoppedCallback paramWindowStoppedCallback)
  {
    mWindowStoppedCallbacks.remove(paramWindowStoppedCallback);
  }
  
  public void reportActivityRelaunched()
  {
    mActivityRelaunched = true;
  }
  
  public void reportDrawFinish()
  {
    if (mWindowDrawCountDown != null) {
      mWindowDrawCountDown.countDown();
    }
  }
  
  public void requestChildFocus(View paramView1, View paramView2)
  {
    checkThread();
    scheduleTraversals();
  }
  
  public boolean requestChildRectangleOnScreen(View paramView, Rect paramRect, boolean paramBoolean)
  {
    if (paramRect == null) {
      return scrollToRectOrFocus(null, paramBoolean);
    }
    paramRect.offset(paramView.getLeft() - paramView.getScrollX(), paramView.getTop() - paramView.getScrollY());
    paramBoolean = scrollToRectOrFocus(paramRect, paramBoolean);
    mTempRect.set(paramRect);
    mTempRect.offset(0, -mCurScrollY);
    mTempRect.offset(mAttachInfo.mWindowLeft, mAttachInfo.mWindowTop);
    try
    {
      mWindowSession.onRectangleOnScreenRequested(mWindow, mTempRect);
    }
    catch (RemoteException paramView) {}
    return paramBoolean;
  }
  
  public void requestDisallowInterceptTouchEvent(boolean paramBoolean) {}
  
  public void requestFitSystemWindows()
  {
    checkThread();
    mApplyInsetsRequested = true;
    scheduleTraversals();
  }
  
  public void requestInvalidateRootRenderNode()
  {
    mInvalidateRootRequested = true;
  }
  
  public void requestLayout()
  {
    if (!mHandlingLayoutInLayoutRequest)
    {
      checkThread();
      mLayoutRequested = true;
      scheduleTraversals();
    }
  }
  
  boolean requestLayoutDuringLayout(View paramView)
  {
    if ((mParent != null) && (mAttachInfo != null))
    {
      if (!mLayoutRequesters.contains(paramView)) {
        mLayoutRequesters.add(paramView);
      }
      return !mHandlingLayoutInLayoutRequest;
    }
    return true;
  }
  
  void requestPointerCapture(boolean paramBoolean)
  {
    if (mPointerCapture == paramBoolean) {
      return;
    }
    InputManager.getInstance().requestPointerCapture(mAttachInfo.mWindowToken, paramBoolean);
  }
  
  public boolean requestSendAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent)
  {
    if ((mView != null) && (!mStopped) && (!mPausedForTransition))
    {
      if ((paramAccessibilityEvent.getEventType() != 2048) && (mSendWindowContentChangedAccessibilityEvent != null) && (mSendWindowContentChangedAccessibilityEvent.mSource != null)) {
        mSendWindowContentChangedAccessibilityEvent.removeCallbacksAndRun();
      }
      int i = paramAccessibilityEvent.getEventType();
      if (i != 2048)
      {
        if (i != 32768)
        {
          if (i == 65536)
          {
            i = AccessibilityNodeInfo.getAccessibilityViewId(paramAccessibilityEvent.getSourceNodeId());
            paramView = mView.findViewByAccessibilityId(i);
            if ((paramView != null) && (paramView.getAccessibilityNodeProvider() != null)) {
              setAccessibilityFocus(null, null);
            }
          }
        }
        else
        {
          long l = paramAccessibilityEvent.getSourceNodeId();
          i = AccessibilityNodeInfo.getAccessibilityViewId(l);
          View localView = mView.findViewByAccessibilityId(i);
          if (localView != null)
          {
            paramView = localView.getAccessibilityNodeProvider();
            if (paramView != null) {
              setAccessibilityFocus(localView, paramView.createAccessibilityNodeInfo(AccessibilityNodeInfo.getVirtualDescendantId(l)));
            }
          }
        }
      }
      else {
        handleWindowContentChangedEvent(paramAccessibilityEvent);
      }
      mAccessibilityManager.sendAccessibilityEvent(paramAccessibilityEvent);
      return true;
    }
    return false;
  }
  
  public void requestTransitionStart(LayoutTransition paramLayoutTransition)
  {
    if ((mPendingTransitions == null) || (!mPendingTransitions.contains(paramLayoutTransition)))
    {
      if (mPendingTransitions == null) {
        mPendingTransitions = new ArrayList();
      }
      mPendingTransitions.add(paramLayoutTransition);
    }
  }
  
  public void requestTransparentRegion(View paramView)
  {
    checkThread();
    if (mView == paramView)
    {
      paramView = mView;
      mPrivateFlags |= 0x200;
      mWindowAttributesChanged = true;
      mWindowAttributesChangesFlag = 0;
      requestLayout();
    }
  }
  
  public void requestUpdateConfiguration(Configuration paramConfiguration)
  {
    paramConfiguration = mHandler.obtainMessage(18, paramConfiguration);
    mHandler.sendMessage(paramConfiguration);
  }
  
  void scheduleConsumeBatchedInput()
  {
    if (!mConsumeBatchedInputScheduled)
    {
      mConsumeBatchedInputScheduled = true;
      mChoreographer.postCallback(0, mConsumedBatchedInputRunnable, null);
    }
  }
  
  void scheduleConsumeBatchedInputImmediately()
  {
    if (!mConsumeBatchedInputImmediatelyScheduled)
    {
      unscheduleConsumeBatchedInput();
      mConsumeBatchedInputImmediatelyScheduled = true;
      mHandler.post(mConsumeBatchedInputImmediatelyRunnable);
    }
  }
  
  void scheduleTraversals()
  {
    if (!mTraversalScheduled)
    {
      mTraversalScheduled = true;
      mTraversalBarrier = mHandler.getLooper().getQueue().postSyncBarrier();
      mChoreographer.postCallback(2, mTraversalRunnable, null);
      if (!mUnbufferedInputDispatch) {
        scheduleConsumeBatchedInput();
      }
      notifyRendererOfFramePending();
      pokeDrawLockIfNeeded();
    }
  }
  
  boolean scrollToRectOrFocus(Rect paramRect, boolean paramBoolean)
  {
    Object localObject = mAttachInfo.mContentInsets;
    Rect localRect = mAttachInfo.mVisibleInsets;
    int i = 0;
    boolean bool1 = false;
    boolean bool2;
    if ((left <= left) && (top <= top) && (right <= right))
    {
      bool2 = bool1;
      if (bottom <= bottom) {}
    }
    else
    {
      int j = mScrollY;
      View localView = mView.findFocus();
      if (localView == null) {
        return false;
      }
      if (mLastScrolledFocus != null) {
        localObject = (View)mLastScrolledFocus.get();
      } else {
        localObject = null;
      }
      if (localView != localObject) {
        paramRect = null;
      }
      if ((localView == localObject) && (!mScrollMayChange) && (paramRect == null))
      {
        i = j;
        bool2 = bool1;
      }
      else
      {
        mLastScrolledFocus = new WeakReference(localView);
        mScrollMayChange = false;
        i = j;
        bool2 = bool1;
        if (localView.getGlobalVisibleRect(mVisRect, null))
        {
          if (paramRect == null)
          {
            localView.getFocusedRect(mTempRect);
            if ((mView instanceof ViewGroup)) {
              ((ViewGroup)mView).offsetDescendantRectToMyCoords(localView, mTempRect);
            }
          }
          else
          {
            mTempRect.set(paramRect);
          }
          i = j;
          bool2 = bool1;
          if (mTempRect.intersect(mVisRect))
          {
            if (mTempRect.height() > mView.getHeight() - top - bottom)
            {
              i = j;
            }
            else
            {
              if (mTempRect.top < top) {
                i = mTempRect.top - top;
              }
              for (;;)
              {
                break;
                if (mTempRect.bottom > mView.getHeight() - bottom) {
                  i = mTempRect.bottom - (mView.getHeight() - bottom);
                } else {
                  i = 0;
                }
              }
            }
            bool2 = true;
          }
        }
      }
    }
    if (i != mScrollY)
    {
      if (!paramBoolean)
      {
        if (mScroller == null) {
          mScroller = new Scroller(mView.getContext());
        }
        mScroller.startScroll(0, mScrollY, 0, i - mScrollY);
      }
      else if (mScroller != null)
      {
        mScroller.abortAnimation();
      }
      mScrollY = i;
    }
    return bool2;
  }
  
  void setAccessibilityFocus(View paramView, AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    if (mAccessibilityFocusedVirtualView != null)
    {
      AccessibilityNodeInfo localAccessibilityNodeInfo = mAccessibilityFocusedVirtualView;
      View localView = mAccessibilityFocusedHost;
      mAccessibilityFocusedHost = null;
      mAccessibilityFocusedVirtualView = null;
      localView.clearAccessibilityFocusNoCallbacks(64);
      AccessibilityNodeProvider localAccessibilityNodeProvider = localView.getAccessibilityNodeProvider();
      if (localAccessibilityNodeProvider != null)
      {
        localAccessibilityNodeInfo.getBoundsInParent(mTempRect);
        localView.invalidate(mTempRect);
        localAccessibilityNodeProvider.performAction(AccessibilityNodeInfo.getVirtualDescendantId(localAccessibilityNodeInfo.getSourceNodeId()), 128, null);
      }
      localAccessibilityNodeInfo.recycle();
    }
    if ((mAccessibilityFocusedHost != null) && (mAccessibilityFocusedHost != paramView)) {
      mAccessibilityFocusedHost.clearAccessibilityFocusNoCallbacks(64);
    }
    mAccessibilityFocusedHost = paramView;
    mAccessibilityFocusedVirtualView = paramAccessibilityNodeInfo;
    if (mAttachInfo.mThreadedRenderer != null) {
      mAttachInfo.mThreadedRenderer.invalidateRoot();
    }
  }
  
  public void setActivityConfigCallback(ActivityConfigCallback paramActivityConfigCallback)
  {
    mActivityConfigCallback = paramActivityConfigCallback;
  }
  
  public void setDragFocus(View paramView, DragEvent paramDragEvent)
  {
    if ((mCurrentDragView != paramView) && (!View.sCascadedDragDrop))
    {
      float f1 = mX;
      float f2 = mY;
      int i = mAction;
      ClipData localClipData = mClipData;
      mX = 0.0F;
      mY = 0.0F;
      mClipData = null;
      if (mCurrentDragView != null)
      {
        mAction = 6;
        mCurrentDragView.callDragEventHandler(paramDragEvent);
      }
      if (paramView != null)
      {
        mAction = 5;
        paramView.callDragEventHandler(paramDragEvent);
      }
      mAction = i;
      mX = f1;
      mY = f2;
      mClipData = localClipData;
    }
    mCurrentDragView = paramView;
  }
  
  public void setIsAmbientMode(boolean paramBoolean)
  {
    mIsAmbientMode = paramBoolean;
  }
  
  void setLayoutParams(WindowManager.LayoutParams paramLayoutParams, boolean paramBoolean)
  {
    try
    {
      int i = mWindowAttributes.surfaceInsets.left;
      int j = mWindowAttributes.surfaceInsets.top;
      int k = mWindowAttributes.surfaceInsets.right;
      int m = mWindowAttributes.surfaceInsets.bottom;
      int n = mWindowAttributes.softInputMode;
      boolean bool = mWindowAttributes.hasManualSurfaceInsets;
      mClientWindowLayoutFlags = flags;
      int i1 = mWindowAttributes.privateFlags;
      systemUiVisibility = mWindowAttributes.systemUiVisibility;
      subtreeSystemUiVisibility = mWindowAttributes.subtreeSystemUiVisibility;
      mWindowAttributesChangesFlag = mWindowAttributes.copyFrom(paramLayoutParams);
      if ((mWindowAttributesChangesFlag & 0x80000) != 0) {
        mAttachInfo.mRecomputeGlobalAttributes = true;
      }
      if ((mWindowAttributesChangesFlag & 0x1) != 0) {
        mAttachInfo.mNeedsUpdateLightCenter = true;
      }
      if (mWindowAttributes.packageName == null) {
        mWindowAttributes.packageName = mBasePackageName;
      }
      WindowManager.LayoutParams localLayoutParams = mWindowAttributes;
      privateFlags |= i1 & 0x80;
      if (mWindowAttributes.preservePreviousSurfaceInsets)
      {
        mWindowAttributes.surfaceInsets.set(i, j, k, m);
        mWindowAttributes.hasManualSurfaceInsets = bool;
      }
      else if ((mWindowAttributes.surfaceInsets.left != i) || (mWindowAttributes.surfaceInsets.top != j) || (mWindowAttributes.surfaceInsets.right != k) || (mWindowAttributes.surfaceInsets.bottom != m))
      {
        mNeedsRendererSetup = true;
      }
      applyKeepScreenOnFlag(mWindowAttributes);
      if (paramBoolean)
      {
        mSoftInputMode = softInputMode;
        requestLayout();
      }
      if ((softInputMode & 0xF0) == 0) {
        mWindowAttributes.softInputMode = (mWindowAttributes.softInputMode & 0xFF0F | n & 0xF0);
      }
      mWindowAttributesChanged = true;
      scheduleTraversals();
      return;
    }
    finally {}
  }
  
  void setLocalDragState(Object paramObject)
  {
    mLocalDragState = paramObject;
  }
  
  public void setPausedForTransition(boolean paramBoolean)
  {
    mPausedForTransition = paramBoolean;
  }
  
  public void setReportNextDraw()
  {
    reportNextDraw();
    invalidate();
  }
  
  /* Error */
  public void setView(View paramView1, WindowManager.LayoutParams paramLayoutParams, View paramView2)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 1019	android/view/ViewRootImpl:mView	Landroid/view/View;
    //   6: ifnonnull +1777 -> 1783
    //   9: aload_0
    //   10: aload_1
    //   11: putfield 1019	android/view/ViewRootImpl:mView	Landroid/view/View;
    //   14: aload_0
    //   15: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   18: aload_0
    //   19: getfield 679	android/view/ViewRootImpl:mDisplay	Landroid/view/Display;
    //   22: invokevirtual 3424	android/view/Display:getState	()I
    //   25: putfield 2279	android/view/View$AttachInfo:mDisplayState	I
    //   28: aload_0
    //   29: getfield 825	android/view/ViewRootImpl:mDisplayManager	Landroid/hardware/display/DisplayManager;
    //   32: aload_0
    //   33: getfield 648	android/view/ViewRootImpl:mDisplayListener	Landroid/hardware/display/DisplayManager$DisplayListener;
    //   36: aload_0
    //   37: getfield 655	android/view/ViewRootImpl:mHandler	Landroid/view/ViewRootImpl$ViewRootHandler;
    //   40: invokevirtual 3651	android/hardware/display/DisplayManager:registerDisplayListener	(Landroid/hardware/display/DisplayManager$DisplayListener;Landroid/os/Handler;)V
    //   43: aload_0
    //   44: aload_0
    //   45: getfield 1019	android/view/ViewRootImpl:mView	Landroid/view/View;
    //   48: invokevirtual 3654	android/view/View:getRawLayoutDirection	()I
    //   51: putfield 2416	android/view/ViewRootImpl:mViewLayoutDirectionInitial	I
    //   54: aload_0
    //   55: getfield 808	android/view/ViewRootImpl:mFallbackEventHandler	Landroid/view/FallbackEventHandler;
    //   58: aload_1
    //   59: invokeinterface 3658 2 0
    //   64: aload_0
    //   65: getfield 514	android/view/ViewRootImpl:mWindowAttributes	Landroid/view/WindowManager$LayoutParams;
    //   68: astore 4
    //   70: aload 4
    //   72: aload_2
    //   73: invokevirtual 3634	android/view/WindowManager$LayoutParams:copyFrom	(Landroid/view/WindowManager$LayoutParams;)I
    //   76: pop
    //   77: aload_0
    //   78: getfield 514	android/view/ViewRootImpl:mWindowAttributes	Landroid/view/WindowManager$LayoutParams;
    //   81: getfield 3638	android/view/WindowManager$LayoutParams:packageName	Ljava/lang/String;
    //   84: ifnonnull +14 -> 98
    //   87: aload_0
    //   88: getfield 514	android/view/ViewRootImpl:mWindowAttributes	Landroid/view/WindowManager$LayoutParams;
    //   91: aload_0
    //   92: getfield 687	android/view/ViewRootImpl:mBasePackageName	Ljava/lang/String;
    //   95: putfield 3638	android/view/WindowManager$LayoutParams:packageName	Ljava/lang/String;
    //   98: aload_0
    //   99: getfield 514	android/view/ViewRootImpl:mWindowAttributes	Landroid/view/WindowManager$LayoutParams;
    //   102: astore_2
    //   103: aload_0
    //   104: invokespecial 3660	android/view/ViewRootImpl:setTag	()V
    //   107: aload_0
    //   108: aload_2
    //   109: getfield 984	android/view/WindowManager$LayoutParams:flags	I
    //   112: putfield 986	android/view/ViewRootImpl:mClientWindowLayoutFlags	I
    //   115: aload_0
    //   116: aconst_null
    //   117: aconst_null
    //   118: invokevirtual 2983	android/view/ViewRootImpl:setAccessibilityFocus	(Landroid/view/View;Landroid/view/accessibility/AccessibilityNodeInfo;)V
    //   121: aload_1
    //   122: instanceof 1261
    //   125: ifeq +59 -> 184
    //   128: aload_0
    //   129: aload_1
    //   130: checkcast 1261	com/android/internal/view/RootViewSurfaceTaker
    //   133: invokeinterface 3380 1 0
    //   138: putfield 3372	android/view/ViewRootImpl:mSurfaceHolderCallback	Landroid/view/SurfaceHolder$Callback2;
    //   141: aload_0
    //   142: getfield 3372	android/view/ViewRootImpl:mSurfaceHolderCallback	Landroid/view/SurfaceHolder$Callback2;
    //   145: ifnull +39 -> 184
    //   148: new 94	android/view/ViewRootImpl$TakenSurfaceHolder
    //   151: astore 4
    //   153: aload 4
    //   155: aload_0
    //   156: invokespecial 3381	android/view/ViewRootImpl$TakenSurfaceHolder:<init>	(Landroid/view/ViewRootImpl;)V
    //   159: aload_0
    //   160: aload 4
    //   162: putfield 1272	android/view/ViewRootImpl:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
    //   165: aload_0
    //   166: getfield 1272	android/view/ViewRootImpl:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
    //   169: iconst_0
    //   170: invokevirtual 3384	com/android/internal/view/BaseSurfaceHolder:setFormat	(I)V
    //   173: aload_0
    //   174: getfield 1272	android/view/ViewRootImpl:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
    //   177: aload_0
    //   178: getfield 3372	android/view/ViewRootImpl:mSurfaceHolderCallback	Landroid/view/SurfaceHolder$Callback2;
    //   181: invokevirtual 3387	com/android/internal/view/BaseSurfaceHolder:addCallback	(Landroid/view/SurfaceHolder$Callback;)V
    //   184: aload_2
    //   185: getfield 3628	android/view/WindowManager$LayoutParams:hasManualSurfaceInsets	Z
    //   188: ifne +10 -> 198
    //   191: aload_2
    //   192: aload_1
    //   193: iconst_0
    //   194: iconst_1
    //   195: invokevirtual 3663	android/view/WindowManager$LayoutParams:setSurfaceInsets	(Landroid/view/View;ZZ)V
    //   198: aload_0
    //   199: getfield 679	android/view/ViewRootImpl:mDisplay	Landroid/view/Display;
    //   202: invokevirtual 2233	android/view/Display:getDisplayAdjustments	()Landroid/view/DisplayAdjustments;
    //   205: invokevirtual 2239	android/view/DisplayAdjustments:getCompatibilityInfo	()Landroid/content/res/CompatibilityInfo;
    //   208: astore 4
    //   210: aload_0
    //   211: aload 4
    //   213: invokevirtual 3667	android/content/res/CompatibilityInfo:getTranslator	()Landroid/content/res/CompatibilityInfo$Translator;
    //   216: putfield 1136	android/view/ViewRootImpl:mTranslator	Landroid/content/res/CompatibilityInfo$Translator;
    //   219: aload_0
    //   220: getfield 1272	android/view/ViewRootImpl:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
    //   223: ifnonnull +46 -> 269
    //   226: aload_0
    //   227: aload_2
    //   228: invokespecial 3669	android/view/ViewRootImpl:enableHardwareAcceleration	(Landroid/view/WindowManager$LayoutParams;)V
    //   231: aload_0
    //   232: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   235: getfield 1090	android/view/View$AttachInfo:mThreadedRenderer	Landroid/view/ThreadedRenderer;
    //   238: ifnull +9 -> 247
    //   241: iconst_1
    //   242: istore 5
    //   244: goto +6 -> 250
    //   247: iconst_0
    //   248: istore 5
    //   250: aload_0
    //   251: getfield 1115	android/view/ViewRootImpl:mUseMTRenderer	Z
    //   254: iload 5
    //   256: if_icmpeq +13 -> 269
    //   259: aload_0
    //   260: invokespecial 2439	android/view/ViewRootImpl:endDragResizing	()V
    //   263: aload_0
    //   264: iload 5
    //   266: putfield 1115	android/view/ViewRootImpl:mUseMTRenderer	Z
    //   269: iconst_0
    //   270: istore 6
    //   272: aload_0
    //   273: getfield 1136	android/view/ViewRootImpl:mTranslator	Landroid/content/res/CompatibilityInfo$Translator;
    //   276: ifnull +29 -> 305
    //   279: aload_0
    //   280: getfield 546	android/view/ViewRootImpl:mSurface	Landroid/view/Surface;
    //   283: aload_0
    //   284: getfield 1136	android/view/ViewRootImpl:mTranslator	Landroid/content/res/CompatibilityInfo$Translator;
    //   287: invokevirtual 3673	android/view/Surface:setCompatibilityTranslator	(Landroid/content/res/CompatibilityInfo$Translator;)V
    //   290: iconst_1
    //   291: istore 6
    //   293: aload_2
    //   294: invokevirtual 2748	android/view/WindowManager$LayoutParams:backup	()V
    //   297: aload_0
    //   298: getfield 1136	android/view/ViewRootImpl:mTranslator	Landroid/content/res/CompatibilityInfo$Translator;
    //   301: aload_2
    //   302: invokevirtual 2751	android/content/res/CompatibilityInfo$Translator:translateWindowLayout	(Landroid/view/WindowManager$LayoutParams;)V
    //   305: aload 4
    //   307: invokevirtual 2384	android/content/res/CompatibilityInfo:supportsScreen	()Z
    //   310: ifne +20 -> 330
    //   313: aload_2
    //   314: aload_2
    //   315: getfield 1508	android/view/WindowManager$LayoutParams:privateFlags	I
    //   318: sipush 128
    //   321: ior
    //   322: putfield 1508	android/view/WindowManager$LayoutParams:privateFlags	I
    //   325: aload_0
    //   326: iconst_1
    //   327: putfield 528	android/view/ViewRootImpl:mLastInCompatMode	Z
    //   330: aload_0
    //   331: aload_2
    //   332: getfield 2094	android/view/WindowManager$LayoutParams:softInputMode	I
    //   335: putfield 2497	android/view/ViewRootImpl:mSoftInputMode	I
    //   338: aload_0
    //   339: iconst_1
    //   340: putfield 539	android/view/ViewRootImpl:mWindowAttributesChanged	Z
    //   343: aload_0
    //   344: iconst_m1
    //   345: putfield 541	android/view/ViewRootImpl:mWindowAttributesChangesFlag	I
    //   348: aload_0
    //   349: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   352: aload_1
    //   353: putfield 2990	android/view/View$AttachInfo:mRootView	Landroid/view/View;
    //   356: aload_0
    //   357: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   360: astore 4
    //   362: aload_0
    //   363: getfield 1136	android/view/ViewRootImpl:mTranslator	Landroid/content/res/CompatibilityInfo$Translator;
    //   366: ifnull +9 -> 375
    //   369: iconst_1
    //   370: istore 5
    //   372: goto +6 -> 378
    //   375: iconst_0
    //   376: istore 5
    //   378: aload 4
    //   380: iload 5
    //   382: putfield 1270	android/view/View$AttachInfo:mScalingRequired	Z
    //   385: aload_0
    //   386: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   389: astore 4
    //   391: aload_0
    //   392: getfield 1136	android/view/ViewRootImpl:mTranslator	Landroid/content/res/CompatibilityInfo$Translator;
    //   395: ifnonnull +9 -> 404
    //   398: fconst_1
    //   399: fstore 7
    //   401: goto +12 -> 413
    //   404: aload_0
    //   405: getfield 1136	android/view/ViewRootImpl:mTranslator	Landroid/content/res/CompatibilityInfo$Translator;
    //   408: getfield 3676	android/content/res/CompatibilityInfo$Translator:applicationScale	F
    //   411: fstore 7
    //   413: aload 4
    //   415: fload 7
    //   417: putfield 1267	android/view/View$AttachInfo:mApplicationScale	F
    //   420: aload_3
    //   421: ifnull +14 -> 435
    //   424: aload_0
    //   425: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   428: aload_3
    //   429: invokevirtual 3680	android/view/View:getApplicationWindowToken	()Landroid/os/IBinder;
    //   432: putfield 3683	android/view/View$AttachInfo:mPanelParentWindowToken	Landroid/os/IBinder;
    //   435: aload_0
    //   436: iconst_1
    //   437: putfield 745	android/view/ViewRootImpl:mAdded	Z
    //   440: aload_0
    //   441: invokevirtual 3575	android/view/ViewRootImpl:requestLayout	()V
    //   444: aload_0
    //   445: getfield 514	android/view/ViewRootImpl:mWindowAttributes	Landroid/view/WindowManager$LayoutParams;
    //   448: getfield 3686	android/view/WindowManager$LayoutParams:inputFeatures	I
    //   451: iconst_2
    //   452: iand
    //   453: ifne +16 -> 469
    //   456: new 3015	android/view/InputChannel
    //   459: astore_3
    //   460: aload_3
    //   461: invokespecial 3687	android/view/InputChannel:<init>	()V
    //   464: aload_0
    //   465: aload_3
    //   466: putfield 3013	android/view/ViewRootImpl:mInputChannel	Landroid/view/InputChannel;
    //   469: aload_0
    //   470: getfield 514	android/view/ViewRootImpl:mWindowAttributes	Landroid/view/WindowManager$LayoutParams;
    //   473: getfield 1508	android/view/WindowManager$LayoutParams:privateFlags	I
    //   476: sipush 16384
    //   479: iand
    //   480: ifeq +9 -> 489
    //   483: iconst_1
    //   484: istore 5
    //   486: goto +6 -> 492
    //   489: iconst_0
    //   490: istore 5
    //   492: aload_0
    //   493: iload 5
    //   495: putfield 518	android/view/ViewRootImpl:mForceDecorViewVisibility	Z
    //   498: aload_0
    //   499: aload_0
    //   500: getfield 514	android/view/ViewRootImpl:mWindowAttributes	Landroid/view/WindowManager$LayoutParams;
    //   503: getfield 2710	android/view/WindowManager$LayoutParams:type	I
    //   506: putfield 520	android/view/ViewRootImpl:mOrigWindowType	I
    //   509: aload_0
    //   510: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   513: iconst_1
    //   514: putfield 1011	android/view/View$AttachInfo:mRecomputeGlobalAttributes	Z
    //   517: aload_0
    //   518: invokespecial 2489	android/view/ViewRootImpl:collectViewAttributes	()Z
    //   521: pop
    //   522: aload_0
    //   523: getfield 677	android/view/ViewRootImpl:mWindowSession	Landroid/view/IWindowSession;
    //   526: astore 8
    //   528: aload_0
    //   529: getfield 721	android/view/ViewRootImpl:mWindow	Landroid/view/ViewRootImpl$W;
    //   532: astore_3
    //   533: aload_0
    //   534: getfield 2758	android/view/ViewRootImpl:mSeq	I
    //   537: istore 9
    //   539: aload_0
    //   540: getfield 514	android/view/ViewRootImpl:mWindowAttributes	Landroid/view/WindowManager$LayoutParams;
    //   543: astore 10
    //   545: aload_0
    //   546: invokevirtual 2377	android/view/ViewRootImpl:getHostVisibility	()I
    //   549: istore 11
    //   551: aload_0
    //   552: getfield 679	android/view/ViewRootImpl:mDisplay	Landroid/view/Display;
    //   555: invokevirtual 3272	android/view/Display:getDisplayId	()I
    //   558: istore 12
    //   560: aload_0
    //   561: getfield 718	android/view/ViewRootImpl:mWinFrame	Landroid/graphics/Rect;
    //   564: astore 4
    //   566: aload_0
    //   567: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   570: getfield 2459	android/view/View$AttachInfo:mContentInsets	Landroid/graphics/Rect;
    //   573: astore 13
    //   575: aload_0
    //   576: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   579: getfield 2462	android/view/View$AttachInfo:mStableInsets	Landroid/graphics/Rect;
    //   582: astore 14
    //   584: aload_0
    //   585: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   588: getfield 2474	android/view/View$AttachInfo:mOutsets	Landroid/graphics/Rect;
    //   591: astore 15
    //   593: aload 8
    //   595: aload_3
    //   596: iload 9
    //   598: aload 10
    //   600: iload 11
    //   602: iload 12
    //   604: aload 4
    //   606: aload 13
    //   608: aload 14
    //   610: aload 15
    //   612: aload_0
    //   613: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   616: getfield 2465	android/view/View$AttachInfo:mDisplayCutout	Landroid/view/DisplayCutout$ParcelableWrapper;
    //   619: aload_0
    //   620: getfield 3013	android/view/ViewRootImpl:mInputChannel	Landroid/view/InputChannel;
    //   623: invokeinterface 3691 12 0
    //   628: istore 12
    //   630: iload 6
    //   632: ifeq +7 -> 639
    //   635: aload_2
    //   636: invokevirtual 2765	android/view/WindowManager$LayoutParams:restore	()V
    //   639: aload_0
    //   640: getfield 1136	android/view/ViewRootImpl:mTranslator	Landroid/content/res/CompatibilityInfo$Translator;
    //   643: ifnull +17 -> 660
    //   646: aload_0
    //   647: getfield 1136	android/view/ViewRootImpl:mTranslator	Landroid/content/res/CompatibilityInfo$Translator;
    //   650: aload_0
    //   651: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   654: getfield 2459	android/view/View$AttachInfo:mContentInsets	Landroid/graphics/Rect;
    //   657: invokevirtual 1141	android/content/res/CompatibilityInfo$Translator:translateRectInScreenToAppWindow	(Landroid/graphics/Rect;)V
    //   660: aload_0
    //   661: getfield 551	android/view/ViewRootImpl:mPendingOverscanInsets	Landroid/graphics/Rect;
    //   664: iconst_0
    //   665: iconst_0
    //   666: iconst_0
    //   667: iconst_0
    //   668: invokevirtual 1285	android/graphics/Rect:set	(IIII)V
    //   671: aload_0
    //   672: getfield 557	android/view/ViewRootImpl:mPendingContentInsets	Landroid/graphics/Rect;
    //   675: aload_0
    //   676: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   679: getfield 2459	android/view/View$AttachInfo:mContentInsets	Landroid/graphics/Rect;
    //   682: invokevirtual 2471	android/graphics/Rect:set	(Landroid/graphics/Rect;)V
    //   685: aload_0
    //   686: getfield 555	android/view/ViewRootImpl:mPendingStableInsets	Landroid/graphics/Rect;
    //   689: aload_0
    //   690: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   693: getfield 2462	android/view/View$AttachInfo:mStableInsets	Landroid/graphics/Rect;
    //   696: invokevirtual 2471	android/graphics/Rect:set	(Landroid/graphics/Rect;)V
    //   699: aload_0
    //   700: getfield 573	android/view/ViewRootImpl:mPendingDisplayCutout	Landroid/view/DisplayCutout$ParcelableWrapper;
    //   703: aload_0
    //   704: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   707: getfield 2465	android/view/View$AttachInfo:mDisplayCutout	Landroid/view/DisplayCutout$ParcelableWrapper;
    //   710: invokevirtual 2563	android/view/DisplayCutout$ParcelableWrapper:set	(Landroid/view/DisplayCutout$ParcelableWrapper;)V
    //   713: aload_0
    //   714: getfield 553	android/view/ViewRootImpl:mPendingVisibleInsets	Landroid/graphics/Rect;
    //   717: iconst_0
    //   718: iconst_0
    //   719: iconst_0
    //   720: iconst_0
    //   721: invokevirtual 1285	android/graphics/Rect:set	(IIII)V
    //   724: aload_0
    //   725: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   728: astore_3
    //   729: iload 12
    //   731: iconst_4
    //   732: iand
    //   733: ifeq +9 -> 742
    //   736: iconst_1
    //   737: istore 5
    //   739: goto +6 -> 745
    //   742: iconst_0
    //   743: istore 5
    //   745: aload_3
    //   746: iload 5
    //   748: putfield 2479	android/view/View$AttachInfo:mAlwaysConsumeNavBar	Z
    //   751: aload_0
    //   752: aload_0
    //   753: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   756: getfield 2479	android/view/View$AttachInfo:mAlwaysConsumeNavBar	Z
    //   759: putfield 2476	android/view/ViewRootImpl:mPendingAlwaysConsumeNavBar	Z
    //   762: iload 12
    //   764: ifge +563 -> 1327
    //   767: aload_0
    //   768: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   771: aconst_null
    //   772: putfield 2990	android/view/View$AttachInfo:mRootView	Landroid/view/View;
    //   775: aload_0
    //   776: iconst_0
    //   777: putfield 745	android/view/ViewRootImpl:mAdded	Z
    //   780: aload_0
    //   781: getfield 808	android/view/ViewRootImpl:mFallbackEventHandler	Landroid/view/FallbackEventHandler;
    //   784: aconst_null
    //   785: invokeinterface 3658 2 0
    //   790: aload_0
    //   791: invokevirtual 3023	android/view/ViewRootImpl:unscheduleTraversals	()V
    //   794: aload_0
    //   795: aconst_null
    //   796: aconst_null
    //   797: invokevirtual 2983	android/view/ViewRootImpl:setAccessibilityFocus	(Landroid/view/View;Landroid/view/accessibility/AccessibilityNodeInfo;)V
    //   800: iload 12
    //   802: tableswitch	default:+54->856, -10:+425->1227, -9:+378->1180, -8:+319->1121, -7:+252->1054, -6:+249->1051, -5:+202->1004, -4:+155->957, -3:+108->910, -2:+61->863, -1:+61->863
    //   856: new 3236	java/lang/RuntimeException
    //   859: astore_2
    //   860: goto +434 -> 1294
    //   863: new 3693	android/view/WindowManager$BadTokenException
    //   866: astore_1
    //   867: new 1483	java/lang/StringBuilder
    //   870: astore_3
    //   871: aload_3
    //   872: invokespecial 1484	java/lang/StringBuilder:<init>	()V
    //   875: aload_3
    //   876: ldc_w 3695
    //   879: invokevirtual 1488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   882: pop
    //   883: aload_3
    //   884: aload_2
    //   885: getfield 3698	android/view/WindowManager$LayoutParams:token	Landroid/os/IBinder;
    //   888: invokevirtual 1554	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   891: pop
    //   892: aload_3
    //   893: ldc_w 3700
    //   896: invokevirtual 1488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   899: pop
    //   900: aload_1
    //   901: aload_3
    //   902: invokevirtual 1491	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   905: invokespecial 3701	android/view/WindowManager$BadTokenException:<init>	(Ljava/lang/String;)V
    //   908: aload_1
    //   909: athrow
    //   910: new 3693	android/view/WindowManager$BadTokenException
    //   913: astore_3
    //   914: new 1483	java/lang/StringBuilder
    //   917: astore_1
    //   918: aload_1
    //   919: invokespecial 1484	java/lang/StringBuilder:<init>	()V
    //   922: aload_1
    //   923: ldc_w 3695
    //   926: invokevirtual 1488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   929: pop
    //   930: aload_1
    //   931: aload_2
    //   932: getfield 3698	android/view/WindowManager$LayoutParams:token	Landroid/os/IBinder;
    //   935: invokevirtual 1554	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   938: pop
    //   939: aload_1
    //   940: ldc_w 3703
    //   943: invokevirtual 1488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   946: pop
    //   947: aload_3
    //   948: aload_1
    //   949: invokevirtual 1491	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   952: invokespecial 3701	android/view/WindowManager$BadTokenException:<init>	(Ljava/lang/String;)V
    //   955: aload_3
    //   956: athrow
    //   957: new 3693	android/view/WindowManager$BadTokenException
    //   960: astore_1
    //   961: new 1483	java/lang/StringBuilder
    //   964: astore_3
    //   965: aload_3
    //   966: invokespecial 1484	java/lang/StringBuilder:<init>	()V
    //   969: aload_3
    //   970: ldc_w 3705
    //   973: invokevirtual 1488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   976: pop
    //   977: aload_3
    //   978: aload_2
    //   979: getfield 3698	android/view/WindowManager$LayoutParams:token	Landroid/os/IBinder;
    //   982: invokevirtual 1554	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   985: pop
    //   986: aload_3
    //   987: ldc_w 3707
    //   990: invokevirtual 1488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   993: pop
    //   994: aload_1
    //   995: aload_3
    //   996: invokevirtual 1491	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   999: invokespecial 3701	android/view/WindowManager$BadTokenException:<init>	(Ljava/lang/String;)V
    //   1002: aload_1
    //   1003: athrow
    //   1004: new 3693	android/view/WindowManager$BadTokenException
    //   1007: astore_1
    //   1008: new 1483	java/lang/StringBuilder
    //   1011: astore_2
    //   1012: aload_2
    //   1013: invokespecial 1484	java/lang/StringBuilder:<init>	()V
    //   1016: aload_2
    //   1017: ldc_w 3709
    //   1020: invokevirtual 1488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1023: pop
    //   1024: aload_2
    //   1025: aload_0
    //   1026: getfield 721	android/view/ViewRootImpl:mWindow	Landroid/view/ViewRootImpl$W;
    //   1029: invokevirtual 1554	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1032: pop
    //   1033: aload_2
    //   1034: ldc_w 3711
    //   1037: invokevirtual 1488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1040: pop
    //   1041: aload_1
    //   1042: aload_2
    //   1043: invokevirtual 1491	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1046: invokespecial 3701	android/view/WindowManager$BadTokenException:<init>	(Ljava/lang/String;)V
    //   1049: aload_1
    //   1050: athrow
    //   1051: aload_0
    //   1052: monitorexit
    //   1053: return
    //   1054: new 3693	android/view/WindowManager$BadTokenException
    //   1057: astore_2
    //   1058: new 1483	java/lang/StringBuilder
    //   1061: astore_1
    //   1062: aload_1
    //   1063: invokespecial 1484	java/lang/StringBuilder:<init>	()V
    //   1066: aload_1
    //   1067: ldc_w 3713
    //   1070: invokevirtual 1488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1073: pop
    //   1074: aload_1
    //   1075: aload_0
    //   1076: getfield 721	android/view/ViewRootImpl:mWindow	Landroid/view/ViewRootImpl$W;
    //   1079: invokevirtual 1554	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1082: pop
    //   1083: aload_1
    //   1084: ldc_w 3715
    //   1087: invokevirtual 1488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1090: pop
    //   1091: aload_1
    //   1092: aload_0
    //   1093: getfield 514	android/view/ViewRootImpl:mWindowAttributes	Landroid/view/WindowManager$LayoutParams;
    //   1096: getfield 2710	android/view/WindowManager$LayoutParams:type	I
    //   1099: invokevirtual 3481	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   1102: pop
    //   1103: aload_1
    //   1104: ldc_w 3717
    //   1107: invokevirtual 1488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1110: pop
    //   1111: aload_2
    //   1112: aload_1
    //   1113: invokevirtual 1491	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1116: invokespecial 3701	android/view/WindowManager$BadTokenException:<init>	(Ljava/lang/String;)V
    //   1119: aload_2
    //   1120: athrow
    //   1121: new 3693	android/view/WindowManager$BadTokenException
    //   1124: astore_1
    //   1125: new 1483	java/lang/StringBuilder
    //   1128: astore_2
    //   1129: aload_2
    //   1130: invokespecial 1484	java/lang/StringBuilder:<init>	()V
    //   1133: aload_2
    //   1134: ldc_w 3713
    //   1137: invokevirtual 1488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1140: pop
    //   1141: aload_2
    //   1142: aload_0
    //   1143: getfield 721	android/view/ViewRootImpl:mWindow	Landroid/view/ViewRootImpl$W;
    //   1146: invokevirtual 1554	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1149: pop
    //   1150: aload_2
    //   1151: ldc_w 3719
    //   1154: invokevirtual 1488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1157: pop
    //   1158: aload_2
    //   1159: aload_0
    //   1160: getfield 514	android/view/ViewRootImpl:mWindowAttributes	Landroid/view/WindowManager$LayoutParams;
    //   1163: getfield 2710	android/view/WindowManager$LayoutParams:type	I
    //   1166: invokevirtual 3481	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   1169: pop
    //   1170: aload_1
    //   1171: aload_2
    //   1172: invokevirtual 1491	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1175: invokespecial 3701	android/view/WindowManager$BadTokenException:<init>	(Ljava/lang/String;)V
    //   1178: aload_1
    //   1179: athrow
    //   1180: new 3721	android/view/WindowManager$InvalidDisplayException
    //   1183: astore_2
    //   1184: new 1483	java/lang/StringBuilder
    //   1187: astore_1
    //   1188: aload_1
    //   1189: invokespecial 1484	java/lang/StringBuilder:<init>	()V
    //   1192: aload_1
    //   1193: ldc_w 3713
    //   1196: invokevirtual 1488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1199: pop
    //   1200: aload_1
    //   1201: aload_0
    //   1202: getfield 721	android/view/ViewRootImpl:mWindow	Landroid/view/ViewRootImpl$W;
    //   1205: invokevirtual 1554	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1208: pop
    //   1209: aload_1
    //   1210: ldc_w 3723
    //   1213: invokevirtual 1488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1216: pop
    //   1217: aload_2
    //   1218: aload_1
    //   1219: invokevirtual 1491	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1222: invokespecial 3724	android/view/WindowManager$InvalidDisplayException:<init>	(Ljava/lang/String;)V
    //   1225: aload_2
    //   1226: athrow
    //   1227: new 3721	android/view/WindowManager$InvalidDisplayException
    //   1230: astore_1
    //   1231: new 1483	java/lang/StringBuilder
    //   1234: astore_2
    //   1235: aload_2
    //   1236: invokespecial 1484	java/lang/StringBuilder:<init>	()V
    //   1239: aload_2
    //   1240: ldc_w 3713
    //   1243: invokevirtual 1488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1246: pop
    //   1247: aload_2
    //   1248: aload_0
    //   1249: getfield 721	android/view/ViewRootImpl:mWindow	Landroid/view/ViewRootImpl$W;
    //   1252: invokevirtual 1554	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1255: pop
    //   1256: aload_2
    //   1257: ldc_w 3726
    //   1260: invokevirtual 1488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1263: pop
    //   1264: aload_2
    //   1265: aload_0
    //   1266: getfield 514	android/view/ViewRootImpl:mWindowAttributes	Landroid/view/WindowManager$LayoutParams;
    //   1269: getfield 2710	android/view/WindowManager$LayoutParams:type	I
    //   1272: invokevirtual 3481	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   1275: pop
    //   1276: aload_2
    //   1277: ldc_w 3728
    //   1280: invokevirtual 1488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1283: pop
    //   1284: aload_1
    //   1285: aload_2
    //   1286: invokevirtual 1491	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1289: invokespecial 3724	android/view/WindowManager$InvalidDisplayException:<init>	(Ljava/lang/String;)V
    //   1292: aload_1
    //   1293: athrow
    //   1294: new 1483	java/lang/StringBuilder
    //   1297: astore_1
    //   1298: aload_1
    //   1299: invokespecial 1484	java/lang/StringBuilder:<init>	()V
    //   1302: aload_1
    //   1303: ldc_w 3730
    //   1306: invokevirtual 1488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1309: pop
    //   1310: aload_1
    //   1311: iload 12
    //   1313: invokevirtual 3481	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   1316: pop
    //   1317: aload_2
    //   1318: aload_1
    //   1319: invokevirtual 1491	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1322: invokespecial 3269	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
    //   1325: aload_2
    //   1326: athrow
    //   1327: aload_1
    //   1328: instanceof 1261
    //   1331: ifeq +16 -> 1347
    //   1334: aload_0
    //   1335: aload_1
    //   1336: checkcast 1261	com/android/internal/view/RootViewSurfaceTaker
    //   1339: invokeinterface 3391 1 0
    //   1344: putfield 2992	android/view/ViewRootImpl:mInputQueueCallback	Landroid/view/InputQueue$Callback;
    //   1347: aload_0
    //   1348: getfield 3013	android/view/ViewRootImpl:mInputChannel	Landroid/view/InputChannel;
    //   1351: ifnull +57 -> 1408
    //   1354: aload_0
    //   1355: getfield 2992	android/view/ViewRootImpl:mInputQueueCallback	Landroid/view/InputQueue$Callback;
    //   1358: ifnull +29 -> 1387
    //   1361: new 3002	android/view/InputQueue
    //   1364: astore_3
    //   1365: aload_3
    //   1366: invokespecial 3731	android/view/InputQueue:<init>	()V
    //   1369: aload_0
    //   1370: aload_3
    //   1371: putfield 2994	android/view/ViewRootImpl:mInputQueue	Landroid/view/InputQueue;
    //   1374: aload_0
    //   1375: getfield 2992	android/view/ViewRootImpl:mInputQueueCallback	Landroid/view/InputQueue$Callback;
    //   1378: aload_0
    //   1379: getfield 2994	android/view/ViewRootImpl:mInputQueue	Landroid/view/InputQueue;
    //   1382: invokeinterface 3394 2 0
    //   1387: new 118	android/view/ViewRootImpl$WindowInputEventReceiver
    //   1390: astore_3
    //   1391: aload_3
    //   1392: aload_0
    //   1393: aload_0
    //   1394: getfield 3013	android/view/ViewRootImpl:mInputChannel	Landroid/view/InputChannel;
    //   1397: invokestatic 3734	android/os/Looper:myLooper	()Landroid/os/Looper;
    //   1400: invokespecial 3737	android/view/ViewRootImpl$WindowInputEventReceiver:<init>	(Landroid/view/ViewRootImpl;Landroid/view/InputChannel;Landroid/os/Looper;)V
    //   1403: aload_0
    //   1404: aload_3
    //   1405: putfield 3007	android/view/ViewRootImpl:mInputEventReceiver	Landroid/view/ViewRootImpl$WindowInputEventReceiver;
    //   1408: aload_1
    //   1409: aload_0
    //   1410: invokevirtual 2987	android/view/View:assignParent	(Landroid/view/ViewParent;)V
    //   1413: iload 12
    //   1415: iconst_1
    //   1416: iand
    //   1417: ifeq +9 -> 1426
    //   1420: iconst_1
    //   1421: istore 5
    //   1423: goto +6 -> 1429
    //   1426: iconst_0
    //   1427: istore 5
    //   1429: aload_0
    //   1430: iload 5
    //   1432: putfield 2453	android/view/ViewRootImpl:mAddedTouchMode	Z
    //   1435: iload 12
    //   1437: iconst_2
    //   1438: iand
    //   1439: ifeq +9 -> 1448
    //   1442: iconst_1
    //   1443: istore 5
    //   1445: goto +6 -> 1451
    //   1448: iconst_0
    //   1449: istore 5
    //   1451: aload_0
    //   1452: iload 5
    //   1454: putfield 516	android/view/ViewRootImpl:mAppVisible	Z
    //   1457: aload_0
    //   1458: getfield 760	android/view/ViewRootImpl:mAccessibilityManager	Landroid/view/accessibility/AccessibilityManager;
    //   1461: invokevirtual 1686	android/view/accessibility/AccessibilityManager:isEnabled	()Z
    //   1464: ifeq +10 -> 1474
    //   1467: aload_0
    //   1468: getfield 618	android/view/ViewRootImpl:mAccessibilityInteractionConnectionManager	Landroid/view/ViewRootImpl$AccessibilityInteractionConnectionManager;
    //   1471: invokevirtual 3740	android/view/ViewRootImpl$AccessibilityInteractionConnectionManager:ensureConnection	()V
    //   1474: aload_1
    //   1475: invokevirtual 3743	android/view/View:getImportantForAccessibility	()I
    //   1478: ifne +8 -> 1486
    //   1481: aload_1
    //   1482: iconst_1
    //   1483: invokevirtual 3746	android/view/View:setImportantForAccessibility	(I)V
    //   1486: aload_2
    //   1487: invokevirtual 1531	android/view/WindowManager$LayoutParams:getTitle	()Ljava/lang/CharSequence;
    //   1490: astore_1
    //   1491: new 71	android/view/ViewRootImpl$SyntheticInputStage
    //   1494: astore_2
    //   1495: aload_2
    //   1496: aload_0
    //   1497: invokespecial 3747	android/view/ViewRootImpl$SyntheticInputStage:<init>	(Landroid/view/ViewRootImpl;)V
    //   1500: aload_0
    //   1501: aload_2
    //   1502: putfield 1071	android/view/ViewRootImpl:mSyntheticInputStage	Landroid/view/ViewRootImpl$InputStage;
    //   1505: new 106	android/view/ViewRootImpl$ViewPostImeInputStage
    //   1508: astore_2
    //   1509: aload_2
    //   1510: aload_0
    //   1511: aload_0
    //   1512: getfield 1071	android/view/ViewRootImpl:mSyntheticInputStage	Landroid/view/ViewRootImpl$InputStage;
    //   1515: invokespecial 3750	android/view/ViewRootImpl$ViewPostImeInputStage:<init>	(Landroid/view/ViewRootImpl;Landroid/view/ViewRootImpl$InputStage;)V
    //   1518: new 59	android/view/ViewRootImpl$NativePostImeInputStage
    //   1521: astore_3
    //   1522: new 1483	java/lang/StringBuilder
    //   1525: astore 4
    //   1527: aload 4
    //   1529: invokespecial 1484	java/lang/StringBuilder:<init>	()V
    //   1532: aload 4
    //   1534: ldc_w 3752
    //   1537: invokevirtual 1488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1540: pop
    //   1541: aload 4
    //   1543: aload_1
    //   1544: invokevirtual 1554	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1547: pop
    //   1548: aload_3
    //   1549: aload_0
    //   1550: aload_2
    //   1551: aload 4
    //   1553: invokevirtual 1491	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1556: invokespecial 3755	android/view/ViewRootImpl$NativePostImeInputStage:<init>	(Landroid/view/ViewRootImpl;Landroid/view/ViewRootImpl$InputStage;Ljava/lang/String;)V
    //   1559: new 44	android/view/ViewRootImpl$EarlyPostImeInputStage
    //   1562: astore_2
    //   1563: aload_2
    //   1564: aload_0
    //   1565: aload_3
    //   1566: invokespecial 3756	android/view/ViewRootImpl$EarlyPostImeInputStage:<init>	(Landroid/view/ViewRootImpl;Landroid/view/ViewRootImpl$InputStage;)V
    //   1569: new 50	android/view/ViewRootImpl$ImeInputStage
    //   1572: astore 4
    //   1574: new 1483	java/lang/StringBuilder
    //   1577: astore_3
    //   1578: aload_3
    //   1579: invokespecial 1484	java/lang/StringBuilder:<init>	()V
    //   1582: aload_3
    //   1583: ldc_w 3758
    //   1586: invokevirtual 1488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1589: pop
    //   1590: aload_3
    //   1591: aload_1
    //   1592: invokevirtual 1554	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1595: pop
    //   1596: aload 4
    //   1598: aload_0
    //   1599: aload_2
    //   1600: aload_3
    //   1601: invokevirtual 1491	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1604: invokespecial 3759	android/view/ViewRootImpl$ImeInputStage:<init>	(Landroid/view/ViewRootImpl;Landroid/view/ViewRootImpl$InputStage;Ljava/lang/String;)V
    //   1607: new 109	android/view/ViewRootImpl$ViewPreImeInputStage
    //   1610: astore_3
    //   1611: aload_3
    //   1612: aload_0
    //   1613: aload 4
    //   1615: invokespecial 3760	android/view/ViewRootImpl$ViewPreImeInputStage:<init>	(Landroid/view/ViewRootImpl;Landroid/view/ViewRootImpl$InputStage;)V
    //   1618: new 62	android/view/ViewRootImpl$NativePreImeInputStage
    //   1621: astore 4
    //   1623: new 1483	java/lang/StringBuilder
    //   1626: astore 14
    //   1628: aload 14
    //   1630: invokespecial 1484	java/lang/StringBuilder:<init>	()V
    //   1633: aload 14
    //   1635: ldc_w 3762
    //   1638: invokevirtual 1488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1641: pop
    //   1642: aload 14
    //   1644: aload_1
    //   1645: invokevirtual 1554	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1648: pop
    //   1649: aload 4
    //   1651: aload_0
    //   1652: aload_3
    //   1653: aload 14
    //   1655: invokevirtual 1491	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1658: invokespecial 3763	android/view/ViewRootImpl$NativePreImeInputStage:<init>	(Landroid/view/ViewRootImpl;Landroid/view/ViewRootImpl$InputStage;Ljava/lang/String;)V
    //   1661: aload_0
    //   1662: aload 4
    //   1664: putfield 1078	android/view/ViewRootImpl:mFirstInputStage	Landroid/view/ViewRootImpl$InputStage;
    //   1667: aload_0
    //   1668: aload_2
    //   1669: putfield 1076	android/view/ViewRootImpl:mFirstPostImeInputStage	Landroid/view/ViewRootImpl$InputStage;
    //   1672: new 1483	java/lang/StringBuilder
    //   1675: astore_2
    //   1676: aload_2
    //   1677: invokespecial 1484	java/lang/StringBuilder:<init>	()V
    //   1680: aload_2
    //   1681: ldc_w 3765
    //   1684: invokevirtual 1488	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1687: pop
    //   1688: aload_2
    //   1689: aload_1
    //   1690: invokevirtual 1554	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1693: pop
    //   1694: aload_0
    //   1695: aload_2
    //   1696: invokevirtual 1491	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1699: putfield 532	android/view/ViewRootImpl:mPendingInputEventQueueLengthCounterName	Ljava/lang/String;
    //   1702: goto +81 -> 1783
    //   1705: astore_1
    //   1706: goto +8 -> 1714
    //   1709: astore_1
    //   1710: goto +62 -> 1772
    //   1713: astore_1
    //   1714: aload_0
    //   1715: iconst_0
    //   1716: putfield 745	android/view/ViewRootImpl:mAdded	Z
    //   1719: aload_0
    //   1720: aconst_null
    //   1721: putfield 1019	android/view/ViewRootImpl:mView	Landroid/view/View;
    //   1724: aload_0
    //   1725: getfield 752	android/view/ViewRootImpl:mAttachInfo	Landroid/view/View$AttachInfo;
    //   1728: aconst_null
    //   1729: putfield 2990	android/view/View$AttachInfo:mRootView	Landroid/view/View;
    //   1732: aload_0
    //   1733: aconst_null
    //   1734: putfield 3013	android/view/ViewRootImpl:mInputChannel	Landroid/view/InputChannel;
    //   1737: aload_0
    //   1738: getfield 808	android/view/ViewRootImpl:mFallbackEventHandler	Landroid/view/FallbackEventHandler;
    //   1741: aconst_null
    //   1742: invokeinterface 3658 2 0
    //   1747: aload_0
    //   1748: invokevirtual 3023	android/view/ViewRootImpl:unscheduleTraversals	()V
    //   1751: aload_0
    //   1752: aconst_null
    //   1753: aconst_null
    //   1754: invokevirtual 2983	android/view/ViewRootImpl:setAccessibilityFocus	(Landroid/view/View;Landroid/view/accessibility/AccessibilityNodeInfo;)V
    //   1757: new 3236	java/lang/RuntimeException
    //   1760: astore_3
    //   1761: aload_3
    //   1762: ldc_w 3767
    //   1765: aload_1
    //   1766: invokespecial 3770	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   1769: aload_3
    //   1770: athrow
    //   1771: astore_1
    //   1772: iload 6
    //   1774: ifeq +7 -> 1781
    //   1777: aload_2
    //   1778: invokevirtual 2765	android/view/WindowManager$LayoutParams:restore	()V
    //   1781: aload_1
    //   1782: athrow
    //   1783: aload_0
    //   1784: monitorexit
    //   1785: return
    //   1786: astore_1
    //   1787: aload_0
    //   1788: monitorexit
    //   1789: aload_1
    //   1790: athrow
    //   1791: astore_1
    //   1792: goto -5 -> 1787
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	1795	0	this	ViewRootImpl
    //   0	1795	1	paramView1	View
    //   0	1795	2	paramLayoutParams	WindowManager.LayoutParams
    //   0	1795	3	paramView2	View
    //   68	1595	4	localObject1	Object
    //   242	1211	5	bool	boolean
    //   270	1503	6	i	int
    //   399	17	7	f	float
    //   526	68	8	localIWindowSession	IWindowSession
    //   537	60	9	j	int
    //   543	56	10	localLayoutParams	WindowManager.LayoutParams
    //   549	52	11	k	int
    //   558	881	12	m	int
    //   573	34	13	localRect1	Rect
    //   582	1072	14	localObject2	Object
    //   591	20	15	localRect2	Rect
    // Exception table:
    //   from	to	target	type
    //   593	630	1705	android/os/RemoteException
    //   498	593	1709	finally
    //   498	593	1713	android/os/RemoteException
    //   593	630	1771	finally
    //   1714	1771	1771	finally
    //   2	70	1786	finally
    //   70	98	1791	finally
    //   98	184	1791	finally
    //   184	198	1791	finally
    //   198	241	1791	finally
    //   250	269	1791	finally
    //   272	290	1791	finally
    //   293	305	1791	finally
    //   305	330	1791	finally
    //   330	369	1791	finally
    //   378	398	1791	finally
    //   404	413	1791	finally
    //   413	420	1791	finally
    //   424	435	1791	finally
    //   435	469	1791	finally
    //   469	483	1791	finally
    //   492	498	1791	finally
    //   635	639	1791	finally
    //   639	660	1791	finally
    //   660	729	1791	finally
    //   745	762	1791	finally
    //   767	800	1791	finally
    //   856	860	1791	finally
    //   863	910	1791	finally
    //   910	957	1791	finally
    //   957	1004	1791	finally
    //   1004	1051	1791	finally
    //   1051	1053	1791	finally
    //   1054	1121	1791	finally
    //   1121	1180	1791	finally
    //   1180	1227	1791	finally
    //   1227	1294	1791	finally
    //   1294	1327	1791	finally
    //   1327	1347	1791	finally
    //   1347	1387	1791	finally
    //   1387	1408	1791	finally
    //   1408	1413	1791	finally
    //   1429	1435	1791	finally
    //   1451	1474	1791	finally
    //   1474	1486	1791	finally
    //   1486	1702	1791	finally
    //   1777	1781	1791	finally
    //   1781	1783	1791	finally
    //   1783	1785	1791	finally
    //   1787	1789	1791	finally
  }
  
  void setWindowStopped(boolean paramBoolean)
  {
    if (mStopped != paramBoolean)
    {
      mStopped = paramBoolean;
      ThreadedRenderer localThreadedRenderer = mAttachInfo.mThreadedRenderer;
      if (localThreadedRenderer != null) {
        localThreadedRenderer.setStopped(mStopped);
      }
      if (!mStopped) {
        scheduleTraversals();
      } else if (localThreadedRenderer != null) {
        localThreadedRenderer.destroyHardwareResources(mView);
      }
      for (int i = 0; i < mWindowStoppedCallbacks.size(); i++) {
        ((WindowStoppedCallback)mWindowStoppedCallbacks.get(i)).windowStopped(paramBoolean);
      }
    }
  }
  
  public boolean showContextMenuForChild(View paramView)
  {
    return false;
  }
  
  public boolean showContextMenuForChild(View paramView, float paramFloat1, float paramFloat2)
  {
    return false;
  }
  
  public ActionMode startActionModeForChild(View paramView, ActionMode.Callback paramCallback)
  {
    return null;
  }
  
  public ActionMode startActionModeForChild(View paramView, ActionMode.Callback paramCallback, int paramInt)
  {
    return null;
  }
  
  public void synthesizeInputEvent(InputEvent paramInputEvent)
  {
    paramInputEvent = mHandler.obtainMessage(24, paramInputEvent);
    paramInputEvent.setAsynchronous(true);
    mHandler.sendMessage(paramInputEvent);
  }
  
  void transformMatrixToGlobal(Matrix paramMatrix)
  {
    paramMatrix.preTranslate(mAttachInfo.mWindowLeft, mAttachInfo.mWindowTop);
  }
  
  void transformMatrixToLocal(Matrix paramMatrix)
  {
    paramMatrix.postTranslate(-mAttachInfo.mWindowLeft, -mAttachInfo.mWindowTop);
  }
  
  void unscheduleConsumeBatchedInput()
  {
    if (mConsumeBatchedInputScheduled)
    {
      mConsumeBatchedInputScheduled = false;
      mChoreographer.removeCallbacks(0, mConsumedBatchedInputRunnable, null);
    }
  }
  
  void unscheduleTraversals()
  {
    if (mTraversalScheduled)
    {
      mTraversalScheduled = false;
      mHandler.getLooper().getQueue().removeSyncBarrier(mTraversalBarrier);
      mChoreographer.removeCallbacks(2, mTraversalRunnable, null);
    }
  }
  
  public void updateConfiguration(int paramInt)
  {
    if (mView == null) {
      return;
    }
    Resources localResources = mView.getResources();
    Configuration localConfiguration = localResources.getConfiguration();
    if (paramInt != -1) {
      onMovedToDisplay(paramInt, localConfiguration);
    }
    if ((mForceNextConfigUpdate) || (mLastConfigurationFromResources.diff(localConfiguration) != 0))
    {
      mDisplay = ResourcesManager.getInstance().getAdjustedDisplay(mDisplay.getDisplayId(), localResources);
      int i = mLastConfigurationFromResources.getLayoutDirection();
      paramInt = localConfiguration.getLayoutDirection();
      mLastConfigurationFromResources.setTo(localConfiguration);
      if ((i != paramInt) && (mViewLayoutDirectionInitial == 2)) {
        mView.setLayoutDirection(paramInt);
      }
      mView.dispatchConfigurationChanged(localConfiguration);
      mForceNextWindowRelayout = true;
      requestLayout();
    }
  }
  
  public void updatePointerIcon(float paramFloat1, float paramFloat2)
  {
    mHandler.removeMessages(27);
    Object localObject = MotionEvent.obtain(0L, SystemClock.uptimeMillis(), 7, paramFloat1, paramFloat2, 0);
    localObject = mHandler.obtainMessage(27, localObject);
    mHandler.sendMessage((Message)localObject);
  }
  
  public void windowFocusChanged(boolean paramBoolean1, boolean paramBoolean2)
  {
    try
    {
      mWindowFocusChanged = true;
      mUpcomingWindowFocus = paramBoolean1;
      mUpcomingInTouchMode = paramBoolean2;
      Message localMessage = Message.obtain();
      what = 6;
      mHandler.sendMessage(localMessage);
      return;
    }
    finally {}
  }
  
  static final class AccessibilityInteractionConnection
    extends IAccessibilityInteractionConnection.Stub
  {
    private final WeakReference<ViewRootImpl> mViewRootImpl;
    
    AccessibilityInteractionConnection(ViewRootImpl paramViewRootImpl)
    {
      mViewRootImpl = new WeakReference(paramViewRootImpl);
    }
    
    public void findAccessibilityNodeInfoByAccessibilityId(long paramLong1, Region paramRegion, int paramInt1, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, int paramInt2, int paramInt3, long paramLong2, MagnificationSpec paramMagnificationSpec, Bundle paramBundle)
    {
      ViewRootImpl localViewRootImpl = (ViewRootImpl)mViewRootImpl.get();
      if ((localViewRootImpl != null) && (mView != null)) {
        localViewRootImpl.getAccessibilityInteractionController().findAccessibilityNodeInfoByAccessibilityIdClientThread(paramLong1, paramRegion, paramInt1, paramIAccessibilityInteractionConnectionCallback, paramInt2, paramInt3, paramLong2, paramMagnificationSpec, paramBundle);
      } else {
        try
        {
          paramIAccessibilityInteractionConnectionCallback.setFindAccessibilityNodeInfosResult(null, paramInt1);
        }
        catch (RemoteException paramRegion) {}
      }
    }
    
    public void findAccessibilityNodeInfosByText(long paramLong1, String paramString, Region paramRegion, int paramInt1, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, int paramInt2, int paramInt3, long paramLong2, MagnificationSpec paramMagnificationSpec)
    {
      ViewRootImpl localViewRootImpl = (ViewRootImpl)mViewRootImpl.get();
      if ((localViewRootImpl != null) && (mView != null)) {
        localViewRootImpl.getAccessibilityInteractionController().findAccessibilityNodeInfosByTextClientThread(paramLong1, paramString, paramRegion, paramInt1, paramIAccessibilityInteractionConnectionCallback, paramInt2, paramInt3, paramLong2, paramMagnificationSpec);
      } else {
        try
        {
          paramIAccessibilityInteractionConnectionCallback.setFindAccessibilityNodeInfosResult(null, paramInt1);
        }
        catch (RemoteException paramString) {}
      }
    }
    
    public void findAccessibilityNodeInfosByViewId(long paramLong1, String paramString, Region paramRegion, int paramInt1, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, int paramInt2, int paramInt3, long paramLong2, MagnificationSpec paramMagnificationSpec)
    {
      ViewRootImpl localViewRootImpl = (ViewRootImpl)mViewRootImpl.get();
      if ((localViewRootImpl != null) && (mView != null)) {
        localViewRootImpl.getAccessibilityInteractionController().findAccessibilityNodeInfosByViewIdClientThread(paramLong1, paramString, paramRegion, paramInt1, paramIAccessibilityInteractionConnectionCallback, paramInt2, paramInt3, paramLong2, paramMagnificationSpec);
      } else {
        try
        {
          paramIAccessibilityInteractionConnectionCallback.setFindAccessibilityNodeInfoResult(null, paramInt1);
        }
        catch (RemoteException paramString) {}
      }
    }
    
    public void findFocus(long paramLong1, int paramInt1, Region paramRegion, int paramInt2, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, int paramInt3, int paramInt4, long paramLong2, MagnificationSpec paramMagnificationSpec)
    {
      ViewRootImpl localViewRootImpl = (ViewRootImpl)mViewRootImpl.get();
      if ((localViewRootImpl != null) && (mView != null)) {
        localViewRootImpl.getAccessibilityInteractionController().findFocusClientThread(paramLong1, paramInt1, paramRegion, paramInt2, paramIAccessibilityInteractionConnectionCallback, paramInt3, paramInt4, paramLong2, paramMagnificationSpec);
      } else {
        try
        {
          paramIAccessibilityInteractionConnectionCallback.setFindAccessibilityNodeInfoResult(null, paramInt2);
        }
        catch (RemoteException paramRegion) {}
      }
    }
    
    public void focusSearch(long paramLong1, int paramInt1, Region paramRegion, int paramInt2, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, int paramInt3, int paramInt4, long paramLong2, MagnificationSpec paramMagnificationSpec)
    {
      ViewRootImpl localViewRootImpl = (ViewRootImpl)mViewRootImpl.get();
      if ((localViewRootImpl != null) && (mView != null)) {
        localViewRootImpl.getAccessibilityInteractionController().focusSearchClientThread(paramLong1, paramInt1, paramRegion, paramInt2, paramIAccessibilityInteractionConnectionCallback, paramInt3, paramInt4, paramLong2, paramMagnificationSpec);
      } else {
        try
        {
          paramIAccessibilityInteractionConnectionCallback.setFindAccessibilityNodeInfoResult(null, paramInt2);
        }
        catch (RemoteException paramRegion) {}
      }
    }
    
    public void performAccessibilityAction(long paramLong1, int paramInt1, Bundle paramBundle, int paramInt2, IAccessibilityInteractionConnectionCallback paramIAccessibilityInteractionConnectionCallback, int paramInt3, int paramInt4, long paramLong2)
    {
      ViewRootImpl localViewRootImpl = (ViewRootImpl)mViewRootImpl.get();
      if ((localViewRootImpl != null) && (mView != null)) {
        localViewRootImpl.getAccessibilityInteractionController().performAccessibilityActionClientThread(paramLong1, paramInt1, paramBundle, paramInt2, paramIAccessibilityInteractionConnectionCallback, paramInt3, paramInt4, paramLong2);
      } else {
        try
        {
          paramIAccessibilityInteractionConnectionCallback.setPerformAccessibilityActionResult(false, paramInt2);
        }
        catch (RemoteException paramBundle) {}
      }
    }
  }
  
  final class AccessibilityInteractionConnectionManager
    implements AccessibilityManager.AccessibilityStateChangeListener
  {
    AccessibilityInteractionConnectionManager() {}
    
    public void ensureConnection()
    {
      int i;
      if (mAttachInfo.mAccessibilityWindowId != -1) {
        i = 1;
      } else {
        i = 0;
      }
      if (i == 0) {
        mAttachInfo.mAccessibilityWindowId = mAccessibilityManager.addAccessibilityInteractionConnection(mWindow, mContext.getPackageName(), new ViewRootImpl.AccessibilityInteractionConnection(ViewRootImpl.this));
      }
    }
    
    public void ensureNoConnection()
    {
      int i;
      if (mAttachInfo.mAccessibilityWindowId != -1) {
        i = 1;
      } else {
        i = 0;
      }
      if (i != 0)
      {
        mAttachInfo.mAccessibilityWindowId = -1;
        mAccessibilityManager.removeAccessibilityInteractionConnection(mWindow);
      }
    }
    
    public void onAccessibilityStateChanged(boolean paramBoolean)
    {
      if (paramBoolean)
      {
        ensureConnection();
        if ((mAttachInfo.mHasWindowFocus) && (mView != null))
        {
          mView.sendAccessibilityEvent(32);
          View localView = mView.findFocus();
          if ((localView != null) && (localView != mView)) {
            localView.sendAccessibilityEvent(8);
          }
        }
      }
      else
      {
        ensureNoConnection();
        mHandler.obtainMessage(21).sendToTarget();
      }
    }
  }
  
  public static abstract interface ActivityConfigCallback
  {
    public abstract void onConfigurationChanged(Configuration paramConfiguration, int paramInt);
  }
  
  abstract class AsyncInputStage
    extends ViewRootImpl.InputStage
  {
    protected static final int DEFER = 3;
    private ViewRootImpl.QueuedInputEvent mQueueHead;
    private int mQueueLength;
    private ViewRootImpl.QueuedInputEvent mQueueTail;
    private final String mTraceCounter;
    
    public AsyncInputStage(ViewRootImpl.InputStage paramInputStage, String paramString)
    {
      super(paramInputStage);
      mTraceCounter = paramString;
    }
    
    private void dequeue(ViewRootImpl.QueuedInputEvent paramQueuedInputEvent1, ViewRootImpl.QueuedInputEvent paramQueuedInputEvent2)
    {
      if (paramQueuedInputEvent2 == null) {
        mQueueHead = mNext;
      } else {
        mNext = mNext;
      }
      if (mQueueTail == paramQueuedInputEvent1) {
        mQueueTail = paramQueuedInputEvent2;
      }
      mNext = null;
      mQueueLength -= 1;
      Trace.traceCounter(4L, mTraceCounter, mQueueLength);
    }
    
    private void enqueue(ViewRootImpl.QueuedInputEvent paramQueuedInputEvent)
    {
      if (mQueueTail == null)
      {
        mQueueHead = paramQueuedInputEvent;
        mQueueTail = paramQueuedInputEvent;
      }
      else
      {
        mQueueTail.mNext = paramQueuedInputEvent;
        mQueueTail = paramQueuedInputEvent;
      }
      mQueueLength += 1;
      Trace.traceCounter(4L, mTraceCounter, mQueueLength);
    }
    
    protected void apply(ViewRootImpl.QueuedInputEvent paramQueuedInputEvent, int paramInt)
    {
      if (paramInt == 3) {
        defer(paramQueuedInputEvent);
      } else {
        super.apply(paramQueuedInputEvent, paramInt);
      }
    }
    
    protected void defer(ViewRootImpl.QueuedInputEvent paramQueuedInputEvent)
    {
      mFlags |= 0x2;
      enqueue(paramQueuedInputEvent);
    }
    
    void dump(String paramString, PrintWriter paramPrintWriter)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print(getClass().getName());
      paramPrintWriter.print(": mQueueLength=");
      paramPrintWriter.println(mQueueLength);
      super.dump(paramString, paramPrintWriter);
    }
    
    protected void forward(ViewRootImpl.QueuedInputEvent paramQueuedInputEvent)
    {
      mFlags &= 0xFFFFFFFD;
      ViewRootImpl.QueuedInputEvent localQueuedInputEvent1 = mQueueHead;
      if (localQueuedInputEvent1 == null)
      {
        super.forward(paramQueuedInputEvent);
        return;
      }
      int i = mEvent.getDeviceId();
      ViewRootImpl.QueuedInputEvent localQueuedInputEvent2 = null;
      int k;
      for (int j = 0; (localQueuedInputEvent1 != null) && (localQueuedInputEvent1 != paramQueuedInputEvent); j = k)
      {
        k = j;
        if (j == 0)
        {
          k = j;
          if (i == mEvent.getDeviceId()) {
            k = 1;
          }
        }
        localQueuedInputEvent2 = localQueuedInputEvent1;
        localQueuedInputEvent1 = mNext;
      }
      if (j != 0)
      {
        if (localQueuedInputEvent1 == null) {
          enqueue(paramQueuedInputEvent);
        }
        return;
      }
      ViewRootImpl.QueuedInputEvent localQueuedInputEvent3 = localQueuedInputEvent1;
      if (localQueuedInputEvent1 != null)
      {
        localQueuedInputEvent3 = mNext;
        dequeue(paramQueuedInputEvent, localQueuedInputEvent2);
      }
      super.forward(paramQueuedInputEvent);
      paramQueuedInputEvent = localQueuedInputEvent3;
      while (paramQueuedInputEvent != null) {
        if (i == mEvent.getDeviceId())
        {
          if ((mFlags & 0x2) != 0) {
            break;
          }
          localQueuedInputEvent1 = mNext;
          dequeue(paramQueuedInputEvent, localQueuedInputEvent2);
          super.forward(paramQueuedInputEvent);
          paramQueuedInputEvent = localQueuedInputEvent1;
        }
        else
        {
          localQueuedInputEvent2 = paramQueuedInputEvent;
          paramQueuedInputEvent = mNext;
        }
      }
    }
  }
  
  public static final class CalledFromWrongThreadException
    extends AndroidRuntimeException
  {
    public CalledFromWrongThreadException(String paramString)
    {
      super();
    }
  }
  
  public static abstract interface ConfigChangedCallback
  {
    public abstract void onConfigurationChanged(Configuration paramConfiguration);
  }
  
  final class ConsumeBatchedInputImmediatelyRunnable
    implements Runnable
  {
    ConsumeBatchedInputImmediatelyRunnable() {}
    
    public void run()
    {
      doConsumeBatchedInput(-1L);
    }
  }
  
  final class ConsumeBatchedInputRunnable
    implements Runnable
  {
    ConsumeBatchedInputRunnable() {}
    
    public void run()
    {
      doConsumeBatchedInput(mChoreographer.getFrameTimeNanos());
    }
  }
  
  final class EarlyPostImeInputStage
    extends ViewRootImpl.InputStage
  {
    public EarlyPostImeInputStage(ViewRootImpl.InputStage paramInputStage)
    {
      super(paramInputStage);
    }
    
    private int processKeyEvent(ViewRootImpl.QueuedInputEvent paramQueuedInputEvent)
    {
      paramQueuedInputEvent = (KeyEvent)mEvent;
      if (mAttachInfo.mTooltipHost != null) {
        mAttachInfo.mTooltipHost.handleTooltipKey(paramQueuedInputEvent);
      }
      if (ViewRootImpl.this.checkForLeavingTouchModeAndConsume(paramQueuedInputEvent)) {
        return 1;
      }
      mFallbackEventHandler.preDispatchKeyEvent(paramQueuedInputEvent);
      return 0;
    }
    
    private int processPointerEvent(ViewRootImpl.QueuedInputEvent paramQueuedInputEvent)
    {
      paramQueuedInputEvent = (MotionEvent)mEvent;
      if (mTranslator != null) {
        mTranslator.translateEventInScreenToAppWindow(paramQueuedInputEvent);
      }
      int i = paramQueuedInputEvent.getAction();
      if ((i == 0) || (i == 8)) {
        ensureTouchMode(paramQueuedInputEvent.isFromSource(4098));
      }
      if (i == 0)
      {
        AutofillManager localAutofillManager = ViewRootImpl.this.getAutofillManager();
        if (localAutofillManager != null) {
          localAutofillManager.requestHideFillUi();
        }
      }
      if ((i == 0) && (mAttachInfo.mTooltipHost != null)) {
        mAttachInfo.mTooltipHost.hideTooltip();
      }
      if (mCurScrollY != 0) {
        paramQueuedInputEvent.offsetLocation(0.0F, mCurScrollY);
      }
      if (paramQueuedInputEvent.isTouchEvent())
      {
        mLastTouchPoint.x = paramQueuedInputEvent.getRawX();
        mLastTouchPoint.y = paramQueuedInputEvent.getRawY();
        mLastTouchSource = paramQueuedInputEvent.getSource();
      }
      return 0;
    }
    
    protected int onProcess(ViewRootImpl.QueuedInputEvent paramQueuedInputEvent)
    {
      if ((mEvent instanceof KeyEvent)) {
        return processKeyEvent(paramQueuedInputEvent);
      }
      if ((mEvent.getSource() & 0x2) != 0) {
        return processPointerEvent(paramQueuedInputEvent);
      }
      return 0;
    }
  }
  
  final class HighContrastTextManager
    implements AccessibilityManager.HighTextContrastChangeListener
  {
    HighContrastTextManager()
    {
      ThreadedRenderer.setHighContrastText(mAccessibilityManager.isHighTextContrastEnabled());
    }
    
    public void onHighTextContrastStateChanged(boolean paramBoolean)
    {
      ThreadedRenderer.setHighContrastText(paramBoolean);
      destroyHardwareResources();
      invalidate();
    }
  }
  
  final class ImeInputStage
    extends ViewRootImpl.AsyncInputStage
    implements InputMethodManager.FinishedInputEventCallback
  {
    public ImeInputStage(ViewRootImpl.InputStage paramInputStage, String paramString)
    {
      super(paramInputStage, paramString);
    }
    
    public void onFinishedInputEvent(Object paramObject, boolean paramBoolean)
    {
      paramObject = (ViewRootImpl.QueuedInputEvent)paramObject;
      if (paramBoolean)
      {
        finish(paramObject, true);
        return;
      }
      forward(paramObject);
    }
    
    protected int onProcess(ViewRootImpl.QueuedInputEvent paramQueuedInputEvent)
    {
      if ((mLastWasImTarget) && (!ViewRootImpl.this.isInLocalFocusMode()))
      {
        InputMethodManager localInputMethodManager = InputMethodManager.peekInstance();
        if (localInputMethodManager != null)
        {
          int i = localInputMethodManager.dispatchInputEvent(mEvent, paramQueuedInputEvent, this, mHandler);
          if (i == 1) {
            return 1;
          }
          if (i == 0) {
            return 0;
          }
          return 3;
        }
      }
      return 0;
    }
  }
  
  abstract class InputStage
  {
    protected static final int FINISH_HANDLED = 1;
    protected static final int FINISH_NOT_HANDLED = 2;
    protected static final int FORWARD = 0;
    private final InputStage mNext;
    
    public InputStage(InputStage paramInputStage)
    {
      mNext = paramInputStage;
    }
    
    private boolean isBack(InputEvent paramInputEvent)
    {
      boolean bool1 = paramInputEvent instanceof KeyEvent;
      boolean bool2 = false;
      if (bool1)
      {
        if (((KeyEvent)paramInputEvent).getKeyCode() == 4) {
          bool2 = true;
        }
        return bool2;
      }
      return false;
    }
    
    protected void apply(ViewRootImpl.QueuedInputEvent paramQueuedInputEvent, int paramInt)
    {
      if (paramInt == 0)
      {
        forward(paramQueuedInputEvent);
      }
      else if (paramInt == 1)
      {
        finish(paramQueuedInputEvent, true);
      }
      else
      {
        if (paramInt != 2) {
          break label38;
        }
        finish(paramQueuedInputEvent, false);
      }
      return;
      label38:
      paramQueuedInputEvent = new StringBuilder();
      paramQueuedInputEvent.append("Invalid result: ");
      paramQueuedInputEvent.append(paramInt);
      throw new IllegalArgumentException(paramQueuedInputEvent.toString());
    }
    
    public final void deliver(ViewRootImpl.QueuedInputEvent paramQueuedInputEvent)
    {
      if ((mFlags & 0x4) != 0) {
        forward(paramQueuedInputEvent);
      } else if (shouldDropInputEvent(paramQueuedInputEvent)) {
        finish(paramQueuedInputEvent, false);
      } else {
        apply(paramQueuedInputEvent, onProcess(paramQueuedInputEvent));
      }
    }
    
    void dump(String paramString, PrintWriter paramPrintWriter)
    {
      if (mNext != null) {
        mNext.dump(paramString, paramPrintWriter);
      }
    }
    
    protected void finish(ViewRootImpl.QueuedInputEvent paramQueuedInputEvent, boolean paramBoolean)
    {
      mFlags |= 0x4;
      if (paramBoolean) {
        mFlags |= 0x8;
      }
      forward(paramQueuedInputEvent);
    }
    
    protected void forward(ViewRootImpl.QueuedInputEvent paramQueuedInputEvent)
    {
      onDeliverToNext(paramQueuedInputEvent);
    }
    
    protected void onDeliverToNext(ViewRootImpl.QueuedInputEvent paramQueuedInputEvent)
    {
      if (mNext != null) {
        mNext.deliver(paramQueuedInputEvent);
      } else {
        ViewRootImpl.this.finishInputEvent(paramQueuedInputEvent);
      }
    }
    
    protected void onDetachedFromWindow()
    {
      if (mNext != null) {
        mNext.onDetachedFromWindow();
      }
    }
    
    protected int onProcess(ViewRootImpl.QueuedInputEvent paramQueuedInputEvent)
    {
      return 0;
    }
    
    protected void onWindowFocusChanged(boolean paramBoolean)
    {
      if (mNext != null) {
        mNext.onWindowFocusChanged(paramBoolean);
      }
    }
    
    protected boolean shouldDropInputEvent(ViewRootImpl.QueuedInputEvent paramQueuedInputEvent)
    {
      if ((mView != null) && (mAdded))
      {
        if (((mAttachInfo.mHasWindowFocus) || (mEvent.isFromSource(2)) || (ViewRootImpl.this.isAutofillUiShowing())) && (!mStopped) && ((!mIsAmbientMode) || (mEvent.isFromSource(1))) && ((!mPausedForTransition) || (isBack(mEvent)))) {
          return false;
        }
        if (ViewRootImpl.isTerminalInputEvent(mEvent))
        {
          mEvent.cancel();
          localObject1 = mTag;
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("Cancelling event due to no window focus: ");
          ((StringBuilder)localObject2).append(mEvent);
          Slog.w((String)localObject1, ((StringBuilder)localObject2).toString());
          return false;
        }
        localObject2 = mTag;
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("Dropping event due to no window focus: ");
        ((StringBuilder)localObject1).append(mEvent);
        Slog.w((String)localObject2, ((StringBuilder)localObject1).toString());
        return true;
      }
      Object localObject1 = mTag;
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("Dropping event due to root view being removed: ");
      ((StringBuilder)localObject2).append(mEvent);
      Slog.w((String)localObject1, ((StringBuilder)localObject2).toString());
      return true;
    }
  }
  
  final class InvalidateOnAnimationRunnable
    implements Runnable
  {
    private boolean mPosted;
    private View.AttachInfo.InvalidateInfo[] mTempViewRects;
    private View[] mTempViews;
    private final ArrayList<View.AttachInfo.InvalidateInfo> mViewRects = new ArrayList();
    private final ArrayList<View> mViews = new ArrayList();
    
    InvalidateOnAnimationRunnable() {}
    
    private void postIfNeededLocked()
    {
      if (!mPosted)
      {
        mChoreographer.postCallback(1, this, null);
        mPosted = true;
      }
    }
    
    public void addView(View paramView)
    {
      try
      {
        mViews.add(paramView);
        postIfNeededLocked();
        return;
      }
      finally {}
    }
    
    public void addViewRect(View.AttachInfo.InvalidateInfo paramInvalidateInfo)
    {
      try
      {
        mViewRects.add(paramInvalidateInfo);
        postIfNeededLocked();
        return;
      }
      finally {}
    }
    
    public void removeView(View paramView)
    {
      try
      {
        mViews.remove(paramView);
        int j;
        for (int i = mViewRects.size();; i = j)
        {
          j = i - 1;
          if (i <= 0) {
            break;
          }
          View.AttachInfo.InvalidateInfo localInvalidateInfo = (View.AttachInfo.InvalidateInfo)mViewRects.get(j);
          if (target == paramView)
          {
            mViewRects.remove(j);
            localInvalidateInfo.recycle();
          }
        }
        if ((mPosted) && (mViews.isEmpty()) && (mViewRects.isEmpty()))
        {
          mChoreographer.removeCallbacks(1, this, null);
          mPosted = false;
        }
        return;
      }
      finally {}
    }
    
    public void run()
    {
      int i = 0;
      try
      {
        mPosted = false;
        int j = mViews.size();
        ArrayList localArrayList;
        Object localObject1;
        if (j != 0)
        {
          localArrayList = mViews;
          if (mTempViews != null) {
            localObject1 = mTempViews;
          } else {
            localObject1 = new View[j];
          }
          mTempViews = ((View[])localArrayList.toArray((Object[])localObject1));
          mViews.clear();
        }
        int k = mViewRects.size();
        if (k != 0)
        {
          localArrayList = mViewRects;
          if (mTempViewRects != null) {
            localObject1 = mTempViewRects;
          } else {
            localObject1 = new View.AttachInfo.InvalidateInfo[k];
          }
          mTempViewRects = ((View.AttachInfo.InvalidateInfo[])localArrayList.toArray((Object[])localObject1));
          mViewRects.clear();
        }
        for (int m = 0; m < j; m++)
        {
          mTempViews[m].invalidate();
          mTempViews[m] = null;
        }
        for (m = i; m < k; m++)
        {
          localObject1 = mTempViewRects[m];
          target.invalidate(left, top, right, bottom);
          ((View.AttachInfo.InvalidateInfo)localObject1).recycle();
        }
        return;
      }
      finally {}
    }
  }
  
  final class NativePostImeInputStage
    extends ViewRootImpl.AsyncInputStage
    implements InputQueue.FinishedInputEventCallback
  {
    public NativePostImeInputStage(ViewRootImpl.InputStage paramInputStage, String paramString)
    {
      super(paramInputStage, paramString);
    }
    
    public void onFinishedInputEvent(Object paramObject, boolean paramBoolean)
    {
      paramObject = (ViewRootImpl.QueuedInputEvent)paramObject;
      if (paramBoolean)
      {
        finish(paramObject, true);
        return;
      }
      forward(paramObject);
    }
    
    protected int onProcess(ViewRootImpl.QueuedInputEvent paramQueuedInputEvent)
    {
      if (mInputQueue != null)
      {
        mInputQueue.sendInputEvent(mEvent, paramQueuedInputEvent, false, this);
        return 3;
      }
      return 0;
    }
  }
  
  final class NativePreImeInputStage
    extends ViewRootImpl.AsyncInputStage
    implements InputQueue.FinishedInputEventCallback
  {
    public NativePreImeInputStage(ViewRootImpl.InputStage paramInputStage, String paramString)
    {
      super(paramInputStage, paramString);
    }
    
    public void onFinishedInputEvent(Object paramObject, boolean paramBoolean)
    {
      paramObject = (ViewRootImpl.QueuedInputEvent)paramObject;
      if (paramBoolean)
      {
        finish(paramObject, true);
        return;
      }
      forward(paramObject);
    }
    
    protected int onProcess(ViewRootImpl.QueuedInputEvent paramQueuedInputEvent)
    {
      if ((mInputQueue != null) && ((mEvent instanceof KeyEvent)))
      {
        mInputQueue.sendInputEvent(mEvent, paramQueuedInputEvent, true, this);
        return 3;
      }
      return 0;
    }
  }
  
  private static final class QueuedInputEvent
  {
    public static final int FLAG_DEFERRED = 2;
    public static final int FLAG_DELIVER_POST_IME = 1;
    public static final int FLAG_FINISHED = 4;
    public static final int FLAG_FINISHED_HANDLED = 8;
    public static final int FLAG_RESYNTHESIZED = 16;
    public static final int FLAG_UNHANDLED = 32;
    public InputEvent mEvent;
    public int mFlags;
    public QueuedInputEvent mNext;
    public InputEventReceiver mReceiver;
    
    private QueuedInputEvent() {}
    
    private boolean flagToString(String paramString, int paramInt, boolean paramBoolean, StringBuilder paramStringBuilder)
    {
      if ((mFlags & paramInt) != 0)
      {
        if (paramBoolean) {
          paramStringBuilder.append("|");
        }
        paramStringBuilder.append(paramString);
        return true;
      }
      return paramBoolean;
    }
    
    public boolean shouldSendToSynthesizer()
    {
      return (mFlags & 0x20) != 0;
    }
    
    public boolean shouldSkipIme()
    {
      int i = mFlags;
      boolean bool = true;
      if ((i & 0x1) != 0) {
        return true;
      }
      if ((!(mEvent instanceof MotionEvent)) || ((mEvent.isFromSource(2)) || (!mEvent.isFromSource(4194304)))) {
        bool = false;
      }
      return bool;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder1 = new StringBuilder("QueuedInputEvent{flags=");
      if (!flagToString("UNHANDLED", 32, flagToString("RESYNTHESIZED", 16, flagToString("FINISHED_HANDLED", 8, flagToString("FINISHED", 4, flagToString("DEFERRED", 2, flagToString("DELIVER_POST_IME", 1, false, localStringBuilder1), localStringBuilder1), localStringBuilder1), localStringBuilder1), localStringBuilder1), localStringBuilder1)) {
        localStringBuilder1.append("0");
      }
      StringBuilder localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", hasNextQueuedEvent=");
      if (mEvent != null) {
        localObject = "true";
      } else {
        localObject = "false";
      }
      localStringBuilder2.append((String)localObject);
      localStringBuilder1.append(localStringBuilder2.toString());
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(", hasInputEventReceiver=");
      if (mReceiver != null) {
        localObject = "true";
      } else {
        localObject = "false";
      }
      localStringBuilder2.append((String)localObject);
      localStringBuilder1.append(localStringBuilder2.toString());
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append(", mEvent=");
      ((StringBuilder)localObject).append(mEvent);
      ((StringBuilder)localObject).append("}");
      localStringBuilder1.append(((StringBuilder)localObject).toString());
      return localStringBuilder1.toString();
    }
  }
  
  private class SendWindowContentChangedAccessibilityEvent
    implements Runnable
  {
    private int mChangeTypes = 0;
    public long mLastEventTimeMillis;
    public StackTraceElement[] mOrigin;
    public View mSource;
    
    private SendWindowContentChangedAccessibilityEvent() {}
    
    public void removeCallbacksAndRun()
    {
      mHandler.removeCallbacks(this);
      run();
    }
    
    public void run()
    {
      View localView = mSource;
      mSource = null;
      if (localView == null)
      {
        Log.e("ViewRootImpl", "Accessibility content change has no source");
        return;
      }
      if (AccessibilityManager.getInstance(mContext).isEnabled())
      {
        mLastEventTimeMillis = SystemClock.uptimeMillis();
        AccessibilityEvent localAccessibilityEvent = AccessibilityEvent.obtain();
        localAccessibilityEvent.setEventType(2048);
        localAccessibilityEvent.setContentChangeTypes(mChangeTypes);
        localView.sendAccessibilityEventUnchecked(localAccessibilityEvent);
      }
      else
      {
        mLastEventTimeMillis = 0L;
      }
      localView.resetSubtreeAccessibilityStateChanged();
      mChangeTypes = 0;
    }
    
    public void runOrPost(View paramView, int paramInt)
    {
      if (mHandler.getLooper() != Looper.myLooper())
      {
        Log.e("ViewRootImpl", "Accessibility content change on non-UI thread. Future Android versions will throw an exception.", new ViewRootImpl.CalledFromWrongThreadException("Only the original thread that created a view hierarchy can touch its views."));
        mHandler.removeCallbacks(this);
        if (mSource != null) {
          run();
        }
      }
      if (mSource != null)
      {
        View localView1 = ViewRootImpl.this.getCommonPredecessor(mSource, paramView);
        View localView2 = localView1;
        if (localView1 != null) {
          localView2 = localView1.getSelfOrParentImportantForA11y();
        }
        if (localView2 != null) {
          paramView = localView2;
        }
        mSource = paramView;
        mChangeTypes |= paramInt;
        return;
      }
      mSource = paramView;
      mChangeTypes = paramInt;
      long l1 = SystemClock.uptimeMillis() - mLastEventTimeMillis;
      long l2 = ViewConfiguration.getSendRecurringAccessibilityEventsInterval();
      if (l1 >= l2) {
        removeCallbacksAndRun();
      } else {
        mHandler.postDelayed(this, l2 - l1);
      }
    }
  }
  
  final class SyntheticInputStage
    extends ViewRootImpl.InputStage
  {
    private final ViewRootImpl.SyntheticJoystickHandler mJoystick = new ViewRootImpl.SyntheticJoystickHandler(ViewRootImpl.this);
    private final ViewRootImpl.SyntheticKeyboardHandler mKeyboard = new ViewRootImpl.SyntheticKeyboardHandler(ViewRootImpl.this);
    private final ViewRootImpl.SyntheticTouchNavigationHandler mTouchNavigation = new ViewRootImpl.SyntheticTouchNavigationHandler(ViewRootImpl.this);
    private final ViewRootImpl.SyntheticTrackballHandler mTrackball = new ViewRootImpl.SyntheticTrackballHandler(ViewRootImpl.this);
    
    public SyntheticInputStage()
    {
      super(null);
    }
    
    protected void onDeliverToNext(ViewRootImpl.QueuedInputEvent paramQueuedInputEvent)
    {
      if (((mFlags & 0x10) == 0) && ((mEvent instanceof MotionEvent)))
      {
        MotionEvent localMotionEvent = (MotionEvent)mEvent;
        int i = localMotionEvent.getSource();
        if ((i & 0x4) != 0) {
          mTrackball.cancel();
        } else if ((i & 0x10) != 0) {
          ViewRootImpl.SyntheticJoystickHandler.access$2600(mJoystick);
        } else if ((i & 0x200000) == 2097152) {
          mTouchNavigation.cancel(localMotionEvent);
        }
      }
      super.onDeliverToNext(paramQueuedInputEvent);
    }
    
    protected void onDetachedFromWindow()
    {
      ViewRootImpl.SyntheticJoystickHandler.access$2600(mJoystick);
    }
    
    protected int onProcess(ViewRootImpl.QueuedInputEvent paramQueuedInputEvent)
    {
      mFlags |= 0x10;
      if ((mEvent instanceof MotionEvent))
      {
        paramQueuedInputEvent = (MotionEvent)mEvent;
        int i = paramQueuedInputEvent.getSource();
        if ((i & 0x4) != 0)
        {
          mTrackball.process(paramQueuedInputEvent);
          return 1;
        }
        if ((i & 0x10) != 0)
        {
          mJoystick.process(paramQueuedInputEvent);
          return 1;
        }
        if ((i & 0x200000) == 2097152)
        {
          mTouchNavigation.process(paramQueuedInputEvent);
          return 1;
        }
      }
      else if ((mFlags & 0x20) != 0)
      {
        mKeyboard.process((KeyEvent)mEvent);
        return 1;
      }
      return 0;
    }
    
    protected void onWindowFocusChanged(boolean paramBoolean)
    {
      if (!paramBoolean) {
        ViewRootImpl.SyntheticJoystickHandler.access$2600(mJoystick);
      }
    }
  }
  
  final class SyntheticJoystickHandler
    extends Handler
  {
    private static final int MSG_ENQUEUE_X_AXIS_KEY_REPEAT = 1;
    private static final int MSG_ENQUEUE_Y_AXIS_KEY_REPEAT = 2;
    private final SparseArray<KeyEvent> mDeviceKeyEvents = new SparseArray();
    private final JoystickAxesState mJoystickAxesState = new JoystickAxesState();
    
    public SyntheticJoystickHandler()
    {
      super();
    }
    
    private void cancel()
    {
      removeMessages(1);
      removeMessages(2);
      for (int i = 0; i < mDeviceKeyEvents.size(); i++)
      {
        KeyEvent localKeyEvent = (KeyEvent)mDeviceKeyEvents.valueAt(i);
        if (localKeyEvent != null) {
          enqueueInputEvent(KeyEvent.changeTimeRepeat(localKeyEvent, SystemClock.uptimeMillis(), 0));
        }
      }
      mDeviceKeyEvents.clear();
      mJoystickAxesState.resetState();
    }
    
    private void update(MotionEvent paramMotionEvent)
    {
      int i = paramMotionEvent.getHistorySize();
      for (int j = 0; j < i; j++)
      {
        l = paramMotionEvent.getHistoricalEventTime(j);
        mJoystickAxesState.updateStateForAxis(paramMotionEvent, l, 0, paramMotionEvent.getHistoricalAxisValue(0, 0, j));
        mJoystickAxesState.updateStateForAxis(paramMotionEvent, l, 1, paramMotionEvent.getHistoricalAxisValue(1, 0, j));
        mJoystickAxesState.updateStateForAxis(paramMotionEvent, l, 15, paramMotionEvent.getHistoricalAxisValue(15, 0, j));
        mJoystickAxesState.updateStateForAxis(paramMotionEvent, l, 16, paramMotionEvent.getHistoricalAxisValue(16, 0, j));
      }
      long l = paramMotionEvent.getEventTime();
      mJoystickAxesState.updateStateForAxis(paramMotionEvent, l, 0, paramMotionEvent.getAxisValue(0));
      mJoystickAxesState.updateStateForAxis(paramMotionEvent, l, 1, paramMotionEvent.getAxisValue(1));
      mJoystickAxesState.updateStateForAxis(paramMotionEvent, l, 15, paramMotionEvent.getAxisValue(15));
      mJoystickAxesState.updateStateForAxis(paramMotionEvent, l, 16, paramMotionEvent.getAxisValue(16));
    }
    
    public void handleMessage(Message paramMessage)
    {
      switch (what)
      {
      default: 
        break;
      case 1: 
      case 2: 
        if (mAttachInfo.mHasWindowFocus)
        {
          KeyEvent localKeyEvent = (KeyEvent)obj;
          localKeyEvent = KeyEvent.changeTimeRepeat(localKeyEvent, SystemClock.uptimeMillis(), localKeyEvent.getRepeatCount() + 1);
          enqueueInputEvent(localKeyEvent);
          paramMessage = obtainMessage(what, localKeyEvent);
          paramMessage.setAsynchronous(true);
          sendMessageDelayed(paramMessage, ViewConfiguration.getKeyRepeatDelay());
        }
        break;
      }
    }
    
    public void process(MotionEvent paramMotionEvent)
    {
      switch (paramMotionEvent.getActionMasked())
      {
      default: 
        String str = mTag;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unexpected action: ");
        localStringBuilder.append(paramMotionEvent.getActionMasked());
        Log.w(str, localStringBuilder.toString());
        break;
      case 3: 
        cancel();
        break;
      case 2: 
        update(paramMotionEvent);
      }
    }
    
    final class JoystickAxesState
    {
      private static final int STATE_DOWN_OR_RIGHT = 1;
      private static final int STATE_NEUTRAL = 0;
      private static final int STATE_UP_OR_LEFT = -1;
      final int[] mAxisStatesHat = { 0, 0 };
      final int[] mAxisStatesStick = { 0, 0 };
      
      JoystickAxesState() {}
      
      private boolean isXAxis(int paramInt)
      {
        boolean bool;
        if ((paramInt != 0) && (paramInt != 15)) {
          bool = false;
        } else {
          bool = true;
        }
        return bool;
      }
      
      private boolean isYAxis(int paramInt)
      {
        boolean bool1 = true;
        boolean bool2 = bool1;
        if (paramInt != 1) {
          if (paramInt == 16) {
            bool2 = bool1;
          } else {
            bool2 = false;
          }
        }
        return bool2;
      }
      
      private int joystickAxisAndStateToKeycode(int paramInt1, int paramInt2)
      {
        if ((isXAxis(paramInt1)) && (paramInt2 == -1)) {
          return 21;
        }
        if ((isXAxis(paramInt1)) && (paramInt2 == 1)) {
          return 22;
        }
        if ((isYAxis(paramInt1)) && (paramInt2 == -1)) {
          return 19;
        }
        if ((isYAxis(paramInt1)) && (paramInt2 == 1)) {
          return 20;
        }
        String str = mTag;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unknown axis ");
        localStringBuilder.append(paramInt1);
        localStringBuilder.append(" or direction ");
        localStringBuilder.append(paramInt2);
        Log.e(str, localStringBuilder.toString());
        return 0;
      }
      
      private int joystickAxisValueToState(float paramFloat)
      {
        if (paramFloat >= 0.5F) {
          return 1;
        }
        if (paramFloat <= -0.5F) {
          return -1;
        }
        return 0;
      }
      
      void resetState()
      {
        mAxisStatesHat[0] = 0;
        mAxisStatesHat[1] = 0;
        mAxisStatesStick[0] = 0;
        mAxisStatesStick[1] = 0;
      }
      
      void updateStateForAxis(MotionEvent paramMotionEvent, long paramLong, int paramInt, float paramFloat)
      {
        int i;
        if (isXAxis(paramInt)) {
          i = 0;
        }
        for (int j = 1;; j = 2)
        {
          break;
          if (!isYAxis(paramInt)) {
            break label362;
          }
          i = 1;
        }
        int k = joystickAxisValueToState(paramFloat);
        if ((paramInt != 0) && (paramInt != 1)) {
          m = mAxisStatesHat[i];
        } else {
          m = mAxisStatesStick[i];
        }
        if (m == k) {
          return;
        }
        int n = paramMotionEvent.getMetaState();
        int i1 = paramMotionEvent.getDeviceId();
        int i2 = i1;
        int i3 = paramMotionEvent.getSource();
        if ((m == 1) || (m == -1))
        {
          m = joystickAxisAndStateToKeycode(paramInt, m);
          if (m != 0)
          {
            enqueueInputEvent(new KeyEvent(paramLong, paramLong, 1, m, 0, n, i2, 0, 1024, i3));
            mDeviceKeyEvents.put(i2, null);
          }
          removeMessages(j);
        }
        if ((k != 1) && (k != -1)) {
          break label326;
        }
        int m = joystickAxisAndStateToKeycode(paramInt, k);
        if (m != 0)
        {
          paramMotionEvent = new KeyEvent(paramLong, paramLong, 0, m, 0, n, i1, 0, 1024, i3);
          enqueueInputEvent(paramMotionEvent);
          paramMotionEvent = obtainMessage(j, paramMotionEvent);
          paramMotionEvent.setAsynchronous(true);
          sendMessageDelayed(paramMotionEvent, ViewConfiguration.getKeyRepeatTimeout());
          mDeviceKeyEvents.put(i1, new KeyEvent(paramLong, paramLong, 1, m, 0, n, i1, 0, 1056, i3));
        }
        label326:
        if ((paramInt != 0) && (paramInt != 1)) {
          mAxisStatesHat[i] = k;
        } else {
          mAxisStatesStick[i] = k;
        }
        return;
        label362:
        paramMotionEvent = mTag;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unexpected axis ");
        localStringBuilder.append(paramInt);
        localStringBuilder.append(" in updateStateForAxis!");
        Log.e(paramMotionEvent, localStringBuilder.toString());
      }
    }
  }
  
  final class SyntheticKeyboardHandler
  {
    SyntheticKeyboardHandler() {}
    
    public void process(KeyEvent paramKeyEvent)
    {
      if ((paramKeyEvent.getFlags() & 0x400) != 0) {
        return;
      }
      Object localObject = paramKeyEvent.getKeyCharacterMap();
      int i = paramKeyEvent.getKeyCode();
      int j = paramKeyEvent.getMetaState();
      localObject = ((KeyCharacterMap)localObject).getFallbackAction(i, j);
      if (localObject != null)
      {
        i = paramKeyEvent.getFlags();
        paramKeyEvent = KeyEvent.obtain(paramKeyEvent.getDownTime(), paramKeyEvent.getEventTime(), paramKeyEvent.getAction(), keyCode, paramKeyEvent.getRepeatCount(), metaState, paramKeyEvent.getDeviceId(), paramKeyEvent.getScanCode(), i | 0x400, paramKeyEvent.getSource(), null);
        ((KeyCharacterMap.FallbackAction)localObject).recycle();
        enqueueInputEvent(paramKeyEvent);
      }
    }
  }
  
  final class SyntheticTouchNavigationHandler
    extends Handler
  {
    private static final float DEFAULT_HEIGHT_MILLIMETERS = 48.0F;
    private static final float DEFAULT_WIDTH_MILLIMETERS = 48.0F;
    private static final float FLING_TICK_DECAY = 0.8F;
    private static final boolean LOCAL_DEBUG = false;
    private static final String LOCAL_TAG = "SyntheticTouchNavigationHandler";
    private static final float MAX_FLING_VELOCITY_TICKS_PER_SECOND = 20.0F;
    private static final float MIN_FLING_VELOCITY_TICKS_PER_SECOND = 6.0F;
    private static final int TICK_DISTANCE_MILLIMETERS = 12;
    private float mAccumulatedX;
    private float mAccumulatedY;
    private int mActivePointerId = -1;
    private float mConfigMaxFlingVelocity;
    private float mConfigMinFlingVelocity;
    private float mConfigTickDistance;
    private boolean mConsumedMovement;
    private int mCurrentDeviceId = -1;
    private boolean mCurrentDeviceSupported;
    private int mCurrentSource;
    private final Runnable mFlingRunnable = new Runnable()
    {
      public void run()
      {
        long l = SystemClock.uptimeMillis();
        ViewRootImpl.SyntheticTouchNavigationHandler.this.sendKeyDownOrRepeat(l, mPendingKeyCode, mPendingKeyMetaState);
        ViewRootImpl.SyntheticTouchNavigationHandler.access$3132(ViewRootImpl.SyntheticTouchNavigationHandler.this, 0.8F);
        if (!ViewRootImpl.SyntheticTouchNavigationHandler.this.postFling(l))
        {
          ViewRootImpl.SyntheticTouchNavigationHandler.access$3302(ViewRootImpl.SyntheticTouchNavigationHandler.this, false);
          ViewRootImpl.SyntheticTouchNavigationHandler.this.finishKeys(l);
        }
      }
    };
    private float mFlingVelocity;
    private boolean mFlinging;
    private float mLastX;
    private float mLastY;
    private int mPendingKeyCode = 0;
    private long mPendingKeyDownTime;
    private int mPendingKeyMetaState;
    private int mPendingKeyRepeatCount;
    private float mStartX;
    private float mStartY;
    private VelocityTracker mVelocityTracker;
    
    public SyntheticTouchNavigationHandler()
    {
      super();
    }
    
    private void cancelFling()
    {
      if (mFlinging)
      {
        removeCallbacks(mFlingRunnable);
        mFlinging = false;
      }
    }
    
    private float consumeAccumulatedMovement(long paramLong, int paramInt1, float paramFloat, int paramInt2, int paramInt3)
    {
      float f;
      for (;;)
      {
        f = paramFloat;
        if (paramFloat > -mConfigTickDistance) {
          break;
        }
        sendKeyDownOrRepeat(paramLong, paramInt2, paramInt1);
        paramFloat += mConfigTickDistance;
      }
      while (f >= mConfigTickDistance)
      {
        sendKeyDownOrRepeat(paramLong, paramInt3, paramInt1);
        f -= mConfigTickDistance;
      }
      return f;
    }
    
    private void consumeAccumulatedMovement(long paramLong, int paramInt)
    {
      float f1 = Math.abs(mAccumulatedX);
      float f2 = Math.abs(mAccumulatedY);
      if (f1 >= f2)
      {
        if (f1 >= mConfigTickDistance)
        {
          mAccumulatedX = consumeAccumulatedMovement(paramLong, paramInt, mAccumulatedX, 21, 22);
          mAccumulatedY = 0.0F;
          mConsumedMovement = true;
        }
      }
      else if (f2 >= mConfigTickDistance)
      {
        mAccumulatedY = consumeAccumulatedMovement(paramLong, paramInt, mAccumulatedY, 19, 20);
        mAccumulatedX = 0.0F;
        mConsumedMovement = true;
      }
    }
    
    private void finishKeys(long paramLong)
    {
      cancelFling();
      sendKeyUp(paramLong);
    }
    
    private void finishTracking(long paramLong)
    {
      if (mActivePointerId >= 0)
      {
        mActivePointerId = -1;
        mVelocityTracker.recycle();
        mVelocityTracker = null;
      }
    }
    
    private boolean postFling(long paramLong)
    {
      if (mFlingVelocity >= mConfigMinFlingVelocity)
      {
        long l = (mConfigTickDistance / mFlingVelocity * 1000.0F);
        postAtTime(mFlingRunnable, paramLong + l);
        return true;
      }
      return false;
    }
    
    private void sendKeyDownOrRepeat(long paramLong, int paramInt1, int paramInt2)
    {
      if (mPendingKeyCode != paramInt1)
      {
        sendKeyUp(paramLong);
        mPendingKeyDownTime = paramLong;
        mPendingKeyCode = paramInt1;
        mPendingKeyRepeatCount = 0;
      }
      else
      {
        mPendingKeyRepeatCount += 1;
      }
      mPendingKeyMetaState = paramInt2;
      enqueueInputEvent(new KeyEvent(mPendingKeyDownTime, paramLong, 0, mPendingKeyCode, mPendingKeyRepeatCount, mPendingKeyMetaState, mCurrentDeviceId, 1024, mCurrentSource));
    }
    
    private void sendKeyUp(long paramLong)
    {
      if (mPendingKeyCode != 0)
      {
        enqueueInputEvent(new KeyEvent(mPendingKeyDownTime, paramLong, 1, mPendingKeyCode, 0, mPendingKeyMetaState, mCurrentDeviceId, 0, 1024, mCurrentSource));
        mPendingKeyCode = 0;
      }
    }
    
    private boolean startFling(long paramLong, float paramFloat1, float paramFloat2)
    {
      switch (mPendingKeyCode)
      {
      default: 
        break;
      case 22: 
        if ((paramFloat1 >= mConfigMinFlingVelocity) && (Math.abs(paramFloat2) < mConfigMinFlingVelocity)) {
          mFlingVelocity = paramFloat1;
        } else {
          return false;
        }
        break;
      case 21: 
        if ((-paramFloat1 >= mConfigMinFlingVelocity) && (Math.abs(paramFloat2) < mConfigMinFlingVelocity)) {
          mFlingVelocity = (-paramFloat1);
        } else {
          return false;
        }
        break;
      case 20: 
        if ((paramFloat2 >= mConfigMinFlingVelocity) && (Math.abs(paramFloat1) < mConfigMinFlingVelocity)) {
          mFlingVelocity = paramFloat2;
        } else {
          return false;
        }
        break;
      case 19: 
        if ((-paramFloat2 >= mConfigMinFlingVelocity) && (Math.abs(paramFloat1) < mConfigMinFlingVelocity)) {
          mFlingVelocity = (-paramFloat2);
        } else {
          return false;
        }
        break;
      }
      mFlinging = postFling(paramLong);
      return mFlinging;
    }
    
    public void cancel(MotionEvent paramMotionEvent)
    {
      if ((mCurrentDeviceId == paramMotionEvent.getDeviceId()) && (mCurrentSource == paramMotionEvent.getSource()))
      {
        long l = paramMotionEvent.getEventTime();
        finishKeys(l);
        finishTracking(l);
      }
    }
    
    public void process(MotionEvent paramMotionEvent)
    {
      long l = paramMotionEvent.getEventTime();
      int i = paramMotionEvent.getDeviceId();
      int j = paramMotionEvent.getSource();
      float f1;
      float f2;
      if ((mCurrentDeviceId != i) || (mCurrentSource != j))
      {
        finishKeys(l);
        finishTracking(l);
        mCurrentDeviceId = i;
        mCurrentSource = j;
        mCurrentDeviceSupported = false;
        Object localObject = paramMotionEvent.getDevice();
        if (localObject != null)
        {
          InputDevice.MotionRange localMotionRange = ((InputDevice)localObject).getMotionRange(0);
          localObject = ((InputDevice)localObject).getMotionRange(1);
          if ((localMotionRange != null) && (localObject != null))
          {
            mCurrentDeviceSupported = true;
            f1 = localMotionRange.getResolution();
            f2 = f1;
            if (f1 <= 0.0F) {
              f2 = localMotionRange.getRange() / 48.0F;
            }
            float f3 = ((InputDevice.MotionRange)localObject).getResolution();
            f1 = f3;
            if (f3 <= 0.0F) {
              f1 = ((InputDevice.MotionRange)localObject).getRange() / 48.0F;
            }
            mConfigTickDistance = (12.0F * ((f2 + f1) * 0.5F));
            mConfigMinFlingVelocity = (6.0F * mConfigTickDistance);
            mConfigMaxFlingVelocity = (20.0F * mConfigTickDistance);
          }
        }
      }
      if (!mCurrentDeviceSupported) {
        return;
      }
      j = paramMotionEvent.getActionMasked();
      switch (j)
      {
      default: 
        break;
      case 3: 
        finishKeys(l);
        finishTracking(l);
        break;
      case 1: 
      case 2: 
        if (mActivePointerId >= 0)
        {
          i = paramMotionEvent.findPointerIndex(mActivePointerId);
          if (i < 0)
          {
            finishKeys(l);
            finishTracking(l);
          }
          else
          {
            mVelocityTracker.addMovement(paramMotionEvent);
            f2 = paramMotionEvent.getX(i);
            f1 = paramMotionEvent.getY(i);
            mAccumulatedX += f2 - mLastX;
            mAccumulatedY += f1 - mLastY;
            mLastX = f2;
            mLastY = f1;
            consumeAccumulatedMovement(l, paramMotionEvent.getMetaState());
            if (j == 1)
            {
              if ((mConsumedMovement) && (mPendingKeyCode != 0))
              {
                mVelocityTracker.computeCurrentVelocity(1000, mConfigMaxFlingVelocity);
                if (!startFling(l, mVelocityTracker.getXVelocity(mActivePointerId), mVelocityTracker.getYVelocity(mActivePointerId))) {
                  finishKeys(l);
                }
              }
              finishTracking(l);
            }
          }
        }
        break;
      case 0: 
        boolean bool = mFlinging;
        finishKeys(l);
        finishTracking(l);
        mActivePointerId = paramMotionEvent.getPointerId(0);
        mVelocityTracker = VelocityTracker.obtain();
        mVelocityTracker.addMovement(paramMotionEvent);
        mStartX = paramMotionEvent.getX();
        mStartY = paramMotionEvent.getY();
        mLastX = mStartX;
        mLastY = mStartY;
        mAccumulatedX = 0.0F;
        mAccumulatedY = 0.0F;
        mConsumedMovement = bool;
      }
    }
  }
  
  final class SyntheticTrackballHandler
  {
    private long mLastTime;
    private final ViewRootImpl.TrackballAxis mX = new ViewRootImpl.TrackballAxis();
    private final ViewRootImpl.TrackballAxis mY = new ViewRootImpl.TrackballAxis();
    
    SyntheticTrackballHandler() {}
    
    public void cancel()
    {
      mLastTime = -2147483648L;
      if ((mView != null) && (mAdded)) {
        ensureTouchMode(false);
      }
    }
    
    public void process(MotionEvent paramMotionEvent)
    {
      long l1 = SystemClock.uptimeMillis();
      if (mLastTime + 250L < l1)
      {
        mX.reset(0);
        mY.reset(0);
        mLastTime = l1;
      }
      int i = paramMotionEvent.getAction();
      int j = paramMotionEvent.getMetaState();
      switch (i)
      {
      default: 
        break;
      case 1: 
        mX.reset(2);
        mY.reset(2);
        enqueueInputEvent(new KeyEvent(l1, l1, 1, 23, 0, j, -1, 0, 1024, 257));
        break;
      case 0: 
        long l2 = l1;
        mX.reset(2);
        mY.reset(2);
        enqueueInputEvent(new KeyEvent(l2, l2, 0, 23, 0, j, -1, 0, 1024, 257));
      }
      float f1 = mX.collect(paramMotionEvent.getX(), paramMotionEvent.getEventTime(), "X");
      float f2 = mY.collect(paramMotionEvent.getY(), paramMotionEvent.getEventTime(), "Y");
      int k = 0;
      i = 0;
      float f3 = 1.0F;
      int m;
      int n;
      float f4;
      if (f1 > f2)
      {
        m = mX.generate();
        n = k;
        i = m;
        f4 = f3;
        if (m != 0)
        {
          if (m > 0) {
            i = 22;
          } else {
            i = 21;
          }
          n = i;
          f4 = mX.acceleration;
          mY.reset(2);
          i = m;
        }
      }
      for (;;)
      {
        break;
        n = k;
        f4 = f3;
        if (f2 > 0.0F)
        {
          m = mY.generate();
          n = k;
          i = m;
          f4 = f3;
          if (m != 0)
          {
            if (m > 0) {
              i = 20;
            } else {
              i = 19;
            }
            f4 = mY.acceleration;
            mX.reset(2);
            n = i;
            i = m;
          }
        }
      }
      if (n != 0)
      {
        m = i;
        if (i < 0) {
          m = -i;
        }
        i = (int)(m * f4);
        if (i > m)
        {
          m--;
          enqueueInputEvent(new KeyEvent(l1, l1, 2, n, i - m, j, -1, 0, 1024, 257));
          f4 = f1;
        }
        else
        {
          f4 = f1;
        }
        while (m > 0)
        {
          l1 = SystemClock.uptimeMillis();
          enqueueInputEvent(new KeyEvent(l1, l1, 0, n, 0, j, -1, 0, 1024, 257));
          enqueueInputEvent(new KeyEvent(l1, l1, 1, n, 0, j, -1, 0, 1024, 257));
          m--;
        }
        mLastTime = l1;
      }
    }
  }
  
  static final class SystemUiVisibilityInfo
  {
    int globalVisibility;
    int localChanges;
    int localValue;
    int seq;
    
    SystemUiVisibilityInfo() {}
  }
  
  class TakenSurfaceHolder
    extends BaseSurfaceHolder
  {
    TakenSurfaceHolder() {}
    
    public boolean isCreating()
    {
      return mIsCreating;
    }
    
    public boolean onAllowLockCanvas()
    {
      return mDrawingAllowed;
    }
    
    public void onRelayoutContainer() {}
    
    public void onUpdateSurface()
    {
      throw new IllegalStateException("Shouldn't be here");
    }
    
    public void setFixedSize(int paramInt1, int paramInt2)
    {
      throw new UnsupportedOperationException("Currently only support sizing from layout");
    }
    
    public void setFormat(int paramInt)
    {
      ((RootViewSurfaceTaker)mView).setSurfaceFormat(paramInt);
    }
    
    public void setKeepScreenOn(boolean paramBoolean)
    {
      ((RootViewSurfaceTaker)mView).setSurfaceKeepScreenOn(paramBoolean);
    }
    
    public void setType(int paramInt)
    {
      ((RootViewSurfaceTaker)mView).setSurfaceType(paramInt);
    }
  }
  
  static final class TrackballAxis
  {
    static final float ACCEL_MOVE_SCALING_FACTOR = 0.025F;
    static final long FAST_MOVE_TIME = 150L;
    static final float FIRST_MOVEMENT_THRESHOLD = 0.5F;
    static final float MAX_ACCELERATION = 20.0F;
    static final float SECOND_CUMULATIVE_MOVEMENT_THRESHOLD = 2.0F;
    static final float SUBSEQUENT_INCREMENTAL_MOVEMENT_THRESHOLD = 1.0F;
    float acceleration = 1.0F;
    int dir;
    long lastMoveTime = 0L;
    int nonAccelMovement;
    float position;
    int step;
    
    TrackballAxis() {}
    
    float collect(float paramFloat, long paramLong, String paramString)
    {
      float f1 = 1.0F;
      long l1;
      if (paramFloat > 0.0F)
      {
        l1 = (150.0F * paramFloat);
        if (dir < 0)
        {
          position = 0.0F;
          step = 0;
          acceleration = 1.0F;
          lastMoveTime = 0L;
        }
        dir = 1;
      }
      else if (paramFloat < 0.0F)
      {
        l1 = (-paramFloat * 150.0F);
        if (dir > 0)
        {
          position = 0.0F;
          step = 0;
          acceleration = 1.0F;
          lastMoveTime = 0L;
        }
        dir = -1;
      }
      else
      {
        l1 = 0L;
      }
      if (l1 > 0L)
      {
        long l2 = paramLong - lastMoveTime;
        lastMoveTime = paramLong;
        float f2 = acceleration;
        float f3;
        if (l2 < l1)
        {
          f1 = (float)(l1 - l2) * 0.025F;
          f3 = f2;
          if (f1 > 1.0F) {
            f3 = f2 * f1;
          }
          f2 = 20.0F;
          if (f3 < 20.0F) {
            f2 = f3;
          }
          acceleration = f2;
        }
        else
        {
          float f4 = (float)(l2 - l1) * 0.025F;
          f3 = f2;
          if (f4 > 1.0F) {
            f3 = f2 / f4;
          }
          f2 = f1;
          if (f3 > 1.0F) {
            f2 = f3;
          }
          acceleration = f2;
        }
      }
      position += paramFloat;
      return Math.abs(position);
    }
    
    int generate()
    {
      int i = 0;
      nonAccelMovement = 0;
      for (;;)
      {
        if (position >= 0.0F) {
          j = 1;
        } else {
          j = -1;
        }
        switch (step)
        {
        default: 
          if (Math.abs(position) < 1.0F) {
            return i;
          }
          break;
        case 1: 
          if (Math.abs(position) < 2.0F) {
            return i;
          }
          i += j;
          nonAccelMovement += j;
          position -= 2.0F * j;
          step = 2;
          j = i;
          break;
        case 0: 
          if (Math.abs(position) < 0.5F) {
            return i;
          }
          i += j;
          nonAccelMovement += j;
          step = 1;
          j = i;
          break;
        }
        i += j;
        position -= j * 1.0F;
        float f = acceleration * 1.1F;
        if (f >= 20.0F) {
          f = acceleration;
        }
        acceleration = f;
        int j = i;
        i = j;
      }
    }
    
    void reset(int paramInt)
    {
      position = 0.0F;
      acceleration = 1.0F;
      lastMoveTime = 0L;
      step = paramInt;
      dir = 0;
    }
  }
  
  final class TraversalRunnable
    implements Runnable
  {
    TraversalRunnable() {}
    
    public void run()
    {
      doTraversal();
    }
  }
  
  private static class UnhandledKeyManager
  {
    private final SparseArray<WeakReference<View>> mCapturedKeys = new SparseArray();
    private WeakReference<View> mCurrentReceiver = null;
    private boolean mDispatched = true;
    
    private UnhandledKeyManager() {}
    
    boolean dispatch(View paramView, KeyEvent paramKeyEvent)
    {
      if (mDispatched) {
        return false;
      }
      try
      {
        Trace.traceBegin(8L, "UnhandledKeyEvent dispatch");
        boolean bool = true;
        mDispatched = true;
        paramView = paramView.dispatchUnhandledKeyEvent(paramKeyEvent);
        if (paramKeyEvent.getAction() == 0)
        {
          int i = paramKeyEvent.getKeyCode();
          if ((paramView != null) && (!KeyEvent.isModifierKey(i)))
          {
            SparseArray localSparseArray = mCapturedKeys;
            paramKeyEvent = new java/lang/ref/WeakReference;
            paramKeyEvent.<init>(paramView);
            localSparseArray.put(i, paramKeyEvent);
          }
        }
        Trace.traceEnd(8L);
        if (paramView == null) {
          bool = false;
        }
        return bool;
      }
      finally
      {
        Trace.traceEnd(8L);
      }
    }
    
    void preDispatch(KeyEvent paramKeyEvent)
    {
      mCurrentReceiver = null;
      if (paramKeyEvent.getAction() == 1)
      {
        int i = mCapturedKeys.indexOfKey(paramKeyEvent.getKeyCode());
        if (i >= 0)
        {
          mCurrentReceiver = ((WeakReference)mCapturedKeys.valueAt(i));
          mCapturedKeys.removeAt(i);
        }
      }
    }
    
    boolean preViewDispatch(KeyEvent paramKeyEvent)
    {
      mDispatched = false;
      if (mCurrentReceiver == null) {
        mCurrentReceiver = ((WeakReference)mCapturedKeys.get(paramKeyEvent.getKeyCode()));
      }
      if (mCurrentReceiver != null)
      {
        View localView = (View)mCurrentReceiver.get();
        if (paramKeyEvent.getAction() == 1) {
          mCurrentReceiver = null;
        }
        if ((localView != null) && (localView.isAttachedToWindow())) {
          localView.onUnhandledKeyEvent(paramKeyEvent);
        }
        return true;
      }
      return false;
    }
  }
  
  final class ViewPostImeInputStage
    extends ViewRootImpl.InputStage
  {
    public ViewPostImeInputStage(ViewRootImpl.InputStage paramInputStage)
    {
      super(paramInputStage);
    }
    
    private void maybeUpdatePointerIcon(MotionEvent paramMotionEvent)
    {
      if ((paramMotionEvent.getPointerCount() == 1) && (paramMotionEvent.isFromSource(8194)))
      {
        if ((paramMotionEvent.getActionMasked() == 9) || (paramMotionEvent.getActionMasked() == 10)) {
          ViewRootImpl.access$2402(ViewRootImpl.this, 1);
        }
        if ((paramMotionEvent.getActionMasked() != 10) && (!ViewRootImpl.this.updatePointerIcon(paramMotionEvent)) && (paramMotionEvent.getActionMasked() == 7)) {
          ViewRootImpl.access$2402(ViewRootImpl.this, 1);
        }
      }
    }
    
    private boolean performFocusNavigation(KeyEvent paramKeyEvent)
    {
      int i = 0;
      int j = paramKeyEvent.getKeyCode();
      if (j != 61) {
        switch (j)
        {
        default: 
          break;
        case 22: 
          if (!paramKeyEvent.hasNoModifiers()) {
            break;
          }
          i = 66;
          break;
        case 21: 
          if (!paramKeyEvent.hasNoModifiers()) {
            break;
          }
          i = 17;
          break;
        case 20: 
          if (!paramKeyEvent.hasNoModifiers()) {
            break;
          }
          i = 130;
          break;
        case 19: 
          if (!paramKeyEvent.hasNoModifiers()) {
            break;
          }
          i = 33;
          break;
        }
      } else if (paramKeyEvent.hasNoModifiers()) {
        i = 2;
      } else if (paramKeyEvent.hasModifiers(1)) {
        i = 1;
      }
      if (i != 0)
      {
        View localView = mView.findFocus();
        if (localView != null)
        {
          paramKeyEvent = localView.focusSearch(i);
          if ((paramKeyEvent != null) && (paramKeyEvent != localView))
          {
            localView.getFocusedRect(mTempRect);
            if ((mView instanceof ViewGroup))
            {
              ((ViewGroup)mView).offsetDescendantRectToMyCoords(localView, mTempRect);
              ((ViewGroup)mView).offsetRectIntoDescendantCoords(paramKeyEvent, mTempRect);
            }
            if (paramKeyEvent.requestFocus(i, mTempRect))
            {
              playSoundEffect(SoundEffectConstants.getContantForFocusDirection(i));
              return true;
            }
          }
          if (mView.dispatchUnhandledMove(localView, i)) {
            return true;
          }
        }
        else if (mView.restoreDefaultFocus())
        {
          return true;
        }
      }
      return false;
    }
    
    private boolean performKeyboardGroupNavigation(int paramInt)
    {
      View localView1 = mView.findFocus();
      if ((localView1 == null) && (mView.restoreDefaultFocus())) {
        return true;
      }
      if (localView1 == null) {
        localView1 = keyboardNavigationClusterSearch(null, paramInt);
      } else {
        localView1 = localView1.keyboardNavigationClusterSearch(null, paramInt);
      }
      int i = paramInt;
      if ((paramInt == 2) || (paramInt == 1)) {
        i = 130;
      }
      View localView2 = localView1;
      if (localView1 != null)
      {
        localView2 = localView1;
        if (localView1.isRootNamespace())
        {
          if (localView1.restoreFocusNotInCluster())
          {
            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(paramInt));
            return true;
          }
          localView2 = keyboardNavigationClusterSearch(null, paramInt);
        }
      }
      if ((localView2 != null) && (localView2.restoreFocusInCluster(i)))
      {
        playSoundEffect(SoundEffectConstants.getContantForFocusDirection(paramInt));
        return true;
      }
      return false;
    }
    
    private int processGenericMotionEvent(ViewRootImpl.QueuedInputEvent paramQueuedInputEvent)
    {
      paramQueuedInputEvent = (MotionEvent)mEvent;
      if (mView.dispatchGenericMotionEvent(paramQueuedInputEvent)) {
        return 1;
      }
      return 0;
    }
    
    private int processKeyEvent(ViewRootImpl.QueuedInputEvent paramQueuedInputEvent)
    {
      KeyEvent localKeyEvent = (KeyEvent)mEvent;
      if (mUnhandledKeyManager.preViewDispatch(localKeyEvent)) {
        return 1;
      }
      if (mView.dispatchKeyEvent(localKeyEvent)) {
        return 1;
      }
      if (shouldDropInputEvent(paramQueuedInputEvent)) {
        return 2;
      }
      if (mUnhandledKeyManager.dispatch(mView, localKeyEvent)) {
        return 1;
      }
      int i = 0;
      int j = i;
      if (localKeyEvent.getAction() == 0)
      {
        j = i;
        if (localKeyEvent.getKeyCode() == 61) {
          if (KeyEvent.metaStateHasModifiers(localKeyEvent.getMetaState(), 65536))
          {
            j = 2;
          }
          else
          {
            j = i;
            if (KeyEvent.metaStateHasModifiers(localKeyEvent.getMetaState(), 65537)) {
              j = 1;
            }
          }
        }
      }
      if ((localKeyEvent.getAction() == 0) && (!KeyEvent.metaStateHasNoModifiers(localKeyEvent.getMetaState())) && (localKeyEvent.getRepeatCount() == 0) && (!KeyEvent.isModifierKey(localKeyEvent.getKeyCode())) && (j == 0))
      {
        if (mView.dispatchKeyShortcutEvent(localKeyEvent)) {
          return 1;
        }
        if (shouldDropInputEvent(paramQueuedInputEvent)) {
          return 2;
        }
      }
      if (mFallbackEventHandler.dispatchKeyEvent(localKeyEvent)) {
        return 1;
      }
      if (shouldDropInputEvent(paramQueuedInputEvent)) {
        return 2;
      }
      if (localKeyEvent.getAction() == 0) {
        if (j != 0)
        {
          if (performKeyboardGroupNavigation(j)) {
            return 1;
          }
        }
        else if (performFocusNavigation(localKeyEvent)) {
          return 1;
        }
      }
      return 0;
    }
    
    private int processPointerEvent(ViewRootImpl.QueuedInputEvent paramQueuedInputEvent)
    {
      paramQueuedInputEvent = (MotionEvent)mEvent;
      mAttachInfo.mUnbufferedDispatchRequested = false;
      mAttachInfo.mHandlingPointerEvent = true;
      int i = mView.dispatchPointerEvent(paramQueuedInputEvent);
      int j = paramQueuedInputEvent.getActionMasked();
      if (!SCROLL_BOOST_SS_ENABLE) {
        if (j == 2)
        {
          mHaveMoveEvent = true;
        }
        else if (j == 1)
        {
          mHaveMoveEvent = false;
          mIsPerfLockAcquired = false;
        }
      }
      maybeUpdatePointerIcon(paramQueuedInputEvent);
      ViewRootImpl.this.maybeUpdateTooltip(paramQueuedInputEvent);
      mAttachInfo.mHandlingPointerEvent = false;
      if ((mAttachInfo.mUnbufferedDispatchRequested) && (!mUnbufferedInputDispatch))
      {
        mUnbufferedInputDispatch = true;
        if (mConsumeBatchedInputScheduled) {
          scheduleConsumeBatchedInputImmediately();
        }
      }
      return i;
    }
    
    private int processTrackballEvent(ViewRootImpl.QueuedInputEvent paramQueuedInputEvent)
    {
      paramQueuedInputEvent = (MotionEvent)mEvent;
      if ((paramQueuedInputEvent.isFromSource(131076)) && ((!hasPointerCapture()) || (mView.dispatchCapturedPointerEvent(paramQueuedInputEvent)))) {
        return 1;
      }
      if (mView.dispatchTrackballEvent(paramQueuedInputEvent)) {
        return 1;
      }
      return 0;
    }
    
    protected void onDeliverToNext(ViewRootImpl.QueuedInputEvent paramQueuedInputEvent)
    {
      if ((mUnbufferedInputDispatch) && ((mEvent instanceof MotionEvent)) && (((MotionEvent)mEvent).isTouchEvent()) && (ViewRootImpl.isTerminalInputEvent(mEvent)))
      {
        mUnbufferedInputDispatch = false;
        scheduleConsumeBatchedInput();
      }
      super.onDeliverToNext(paramQueuedInputEvent);
    }
    
    protected int onProcess(ViewRootImpl.QueuedInputEvent paramQueuedInputEvent)
    {
      if ((mEvent instanceof KeyEvent)) {
        return processKeyEvent(paramQueuedInputEvent);
      }
      int i = mEvent.getSource();
      if ((i & 0x2) != 0) {
        return processPointerEvent(paramQueuedInputEvent);
      }
      if ((i & 0x4) != 0) {
        return processTrackballEvent(paramQueuedInputEvent);
      }
      return processGenericMotionEvent(paramQueuedInputEvent);
    }
  }
  
  final class ViewPreImeInputStage
    extends ViewRootImpl.InputStage
  {
    public ViewPreImeInputStage(ViewRootImpl.InputStage paramInputStage)
    {
      super(paramInputStage);
    }
    
    private int processKeyEvent(ViewRootImpl.QueuedInputEvent paramQueuedInputEvent)
    {
      paramQueuedInputEvent = (KeyEvent)mEvent;
      if (mView.dispatchKeyEventPreIme(paramQueuedInputEvent)) {
        return 1;
      }
      return 0;
    }
    
    protected int onProcess(ViewRootImpl.QueuedInputEvent paramQueuedInputEvent)
    {
      if ((mEvent instanceof KeyEvent)) {
        return processKeyEvent(paramQueuedInputEvent);
      }
      return 0;
    }
  }
  
  final class ViewRootHandler
    extends Handler
  {
    ViewRootHandler() {}
    
    public String getMessageName(Message paramMessage)
    {
      int i = what;
      if (i != 21)
      {
        if (i != 1000)
        {
          switch (i)
          {
          default: 
            switch (i)
            {
            default: 
              switch (i)
              {
              default: 
                switch (i)
                {
                default: 
                  return super.getMessageName(paramMessage);
                case 29: 
                  return "MSG_DRAW_FINISHED";
                case 28: 
                  return "MSG_POINTER_CAPTURE_CHANGED";
                }
                return "MSG_UPDATE_POINTER_ICON";
              case 25: 
                return "MSG_DISPATCH_WINDOW_SHOWN";
              case 24: 
                return "MSG_SYNTHESIZE_INPUT_EVENT";
              }
              return "MSG_WINDOW_MOVED";
            case 19: 
              return "MSG_PROCESS_INPUT_EVENTS";
            case 18: 
              return "MSG_UPDATE_CONFIGURATION";
            case 17: 
              return "MSG_DISPATCH_SYSTEM_UI_VISIBILITY";
            case 16: 
              return "MSG_DISPATCH_DRAG_LOCATION_EVENT";
            case 15: 
              return "MSG_DISPATCH_DRAG_EVENT";
            case 14: 
              return "MSG_CLOSE_SYSTEM_DIALOGS";
            case 13: 
              return "MSG_CHECK_FOCUS";
            case 12: 
              return "MSG_DISPATCH_KEY_FROM_AUTOFILL";
            }
            return "MSG_DISPATCH_KEY_FROM_IME";
          case 9: 
            return "MSG_DISPATCH_GET_NEW_SURFACE";
          case 8: 
            return "MSG_DISPATCH_APP_VISIBILITY";
          case 7: 
            return "MSG_DISPATCH_INPUT_EVENT";
          case 6: 
            return "MSG_WINDOW_FOCUS_CHANGED";
          case 5: 
            return "MSG_RESIZED_REPORT";
          case 4: 
            return "MSG_RESIZED";
          case 3: 
            return "MSG_DIE";
          case 2: 
            return "MSG_INVALIDATE_RECT";
          }
          return "MSG_INVALIDATE";
        }
        return "MSG_DISPATCH_NAV_COLOR";
      }
      return "MSG_CLEAR_ACCESSIBILITY_FOCUS_HOST";
    }
    
    public void handleMessage(Message paramMessage)
    {
      int i = what;
      if (i != 1000)
      {
        int j = -1;
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        Object localObject1;
        int m;
        Object localObject2;
        switch (i)
        {
        default: 
          switch (i)
          {
          default: 
            switch (i)
            {
            default: 
              break;
            case 29: 
              pendingDrawFinished();
              break;
            case 28: 
              if (arg1 != 0) {
                bool3 = true;
              }
              ViewRootImpl.this.handlePointerCaptureChanged(bool3);
              break;
            case 27: 
              paramMessage = (MotionEvent)obj;
              ViewRootImpl.this.resetPointerIcon(paramMessage);
              break;
            case 26: 
              localObject1 = (IResultReceiver)obj;
              i = arg1;
              handleRequestKeyboardShortcuts((IResultReceiver)localObject1, i);
              break;
            case 25: 
              handleDispatchWindowShown();
              break;
            case 24: 
              paramMessage = (InputEvent)obj;
              enqueueInputEvent(paramMessage, null, 32, true);
              break;
            case 23: 
              if (!mAdded) {
                break;
              }
              int k = mWinFrame.width();
              m = mWinFrame.height();
              j = arg1;
              i = arg2;
              mWinFrame.left = j;
              mWinFrame.right = (j + k);
              mWinFrame.top = i;
              mWinFrame.bottom = (i + m);
              mPendingBackDropFrame.set(mWinFrame);
              ViewRootImpl.this.maybeHandleWindowMove(mWinFrame);
              break;
            case 22: 
              if (mView == null) {
                break;
              }
              invalidateWorld(mView);
              break;
            case 21: 
              setAccessibilityFocus(null, null);
            }
            break;
          case 19: 
            mProcessInputEventsScheduled = false;
            doProcessInputEvents();
            break;
          case 18: 
            localObject1 = (Configuration)obj;
            paramMessage = (Message)localObject1;
            if (((Configuration)localObject1).isOtherSeqNewer(mLastReportedMergedConfiguration.getMergedConfiguration())) {
              paramMessage = mLastReportedMergedConfiguration.getGlobalConfiguration();
            }
            mPendingMergedConfiguration.setConfiguration(paramMessage, mLastReportedMergedConfiguration.getOverrideConfiguration());
            ViewRootImpl.this.performConfigurationChange(mPendingMergedConfiguration, false, -1);
            break;
          case 17: 
            handleDispatchSystemUiVisibilityChanged((ViewRootImpl.SystemUiVisibilityInfo)obj);
            break;
          case 15: 
          case 16: 
            paramMessage = (DragEvent)obj;
            mLocalState = mLocalDragState;
            ViewRootImpl.this.handleDragEvent(paramMessage);
            break;
          case 14: 
            if (mView == null) {
              break;
            }
            mView.onCloseSystemDialogs((String)obj);
            break;
          case 13: 
            paramMessage = InputMethodManager.peekInstance();
            if (paramMessage != null) {
              paramMessage.checkFocus();
            }
            break;
          case 12: 
            paramMessage = (KeyEvent)obj;
            enqueueInputEvent(paramMessage, null, 0, true);
            break;
          case 11: 
            localObject1 = (KeyEvent)obj;
            paramMessage = (Message)localObject1;
            if ((((KeyEvent)localObject1).getFlags() & 0x8) != 0) {
              paramMessage = KeyEvent.changeFlags((KeyEvent)localObject1, ((KeyEvent)localObject1).getFlags() & 0xFFFFFFF7);
            }
            enqueueInputEvent(paramMessage, null, 1, true);
          }
          break;
        case 9: 
          handleGetNewSurface();
          break;
        case 8: 
          localObject1 = ViewRootImpl.this;
          bool3 = bool1;
          if (arg1 != 0) {
            bool3 = true;
          }
          ((ViewRootImpl)localObject1).handleAppVisibility(bool3);
          break;
        case 7: 
          paramMessage = (SomeArgs)obj;
          localObject2 = (InputEvent)arg1;
          localObject1 = (InputEventReceiver)arg2;
          enqueueInputEvent((InputEvent)localObject2, (InputEventReceiver)localObject1, 0, true);
          paramMessage.recycle();
          break;
        case 6: 
          ViewRootImpl.this.handleWindowFocusChanged();
          break;
        case 4: 
          localObject1 = (SomeArgs)obj;
        case 5: 
          if (((mWinFrame.equals(arg1)) && (mPendingOverscanInsets.equals(arg5)) && (mPendingContentInsets.equals(arg2)) && (mPendingStableInsets.equals(arg6)) && (mPendingDisplayCutout.get().equals(arg9)) && (mPendingVisibleInsets.equals(arg3)) && (mPendingOutsets.equals(arg7)) && (mPendingBackDropFrame.equals(arg8)) && (arg4 == null) && (argi1 == 0) && (mDisplay.getDisplayId() == argi3)) || (!mAdded)) {
            break;
          }
          localObject1 = (SomeArgs)obj;
          m = argi3;
          localObject2 = (MergedConfiguration)arg4;
          if (mDisplay.getDisplayId() != m) {
            i = 1;
          } else {
            i = 0;
          }
          if (!mLastReportedMergedConfiguration.equals(localObject2))
          {
            ViewRootImpl localViewRootImpl = ViewRootImpl.this;
            if (i != 0) {
              i = m;
            } else {
              i = j;
            }
            localViewRootImpl.performConfigurationChange((MergedConfiguration)localObject2, false, i);
          }
          else if (i != 0)
          {
            onMovedToDisplay(m, mLastConfigurationFromResources);
          }
          if ((mWinFrame.equals(arg1)) && (mPendingOverscanInsets.equals(arg5)) && (mPendingContentInsets.equals(arg2)) && (mPendingStableInsets.equals(arg6)) && (mPendingDisplayCutout.get().equals(arg9)) && (mPendingVisibleInsets.equals(arg3)) && (mPendingOutsets.equals(arg7))) {
            i = 0;
          } else {
            i = 1;
          }
          mWinFrame.set((Rect)arg1);
          mPendingOverscanInsets.set((Rect)arg5);
          mPendingContentInsets.set((Rect)arg2);
          mPendingStableInsets.set((Rect)arg6);
          mPendingDisplayCutout.set((DisplayCutout)arg9);
          mPendingVisibleInsets.set((Rect)arg3);
          mPendingOutsets.set((Rect)arg7);
          mPendingBackDropFrame.set((Rect)arg8);
          localObject2 = ViewRootImpl.this;
          if (argi1 != 0) {
            bool3 = true;
          } else {
            bool3 = false;
          }
          mForceNextWindowRelayout = bool3;
          localObject2 = ViewRootImpl.this;
          bool3 = bool2;
          if (argi2 != 0) {
            bool3 = true;
          }
          mPendingAlwaysConsumeNavBar = bool3;
          ((SomeArgs)localObject1).recycle();
          if (what == 5) {
            ViewRootImpl.this.reportNextDraw();
          }
          if ((mView != null) && (i != 0)) {
            ViewRootImpl.forceLayout(mView);
          }
          requestLayout();
          break;
        case 3: 
          doDie();
          break;
        case 2: 
          paramMessage = (View.AttachInfo.InvalidateInfo)obj;
          target.invalidate(left, top, right, bottom);
          paramMessage.recycle();
          break;
        case 1: 
          ((View)obj).invalidate();
          break;
        }
      }
      else
      {
        ViewRootImpl.this.handleDispatchNavigationColor();
      }
    }
    
    public boolean sendMessageAtTime(Message paramMessage, long paramLong)
    {
      if ((what == 26) && (obj == null)) {
        throw new NullPointerException("Attempted to call MSG_REQUEST_KEYBOARD_SHORTCUTS with null receiver:");
      }
      return super.sendMessageAtTime(paramMessage, paramLong);
    }
  }
  
  static class W
    extends IWindow.Stub
  {
    private final WeakReference<ViewRootImpl> mViewAncestor;
    private final IWindowSession mWindowSession;
    
    W(ViewRootImpl paramViewRootImpl)
    {
      mViewAncestor = new WeakReference(paramViewRootImpl);
      mWindowSession = mWindowSession;
    }
    
    private static int checkCallingPermission(String paramString)
    {
      try
      {
        int i = ActivityManager.getService().checkPermission(paramString, Binder.getCallingPid(), Binder.getCallingUid());
        return i;
      }
      catch (RemoteException paramString) {}
      return -1;
    }
    
    public void closeSystemDialogs(String paramString)
    {
      ViewRootImpl localViewRootImpl = (ViewRootImpl)mViewAncestor.get();
      if (localViewRootImpl != null) {
        localViewRootImpl.dispatchCloseSystemDialogs(paramString);
      }
    }
    
    public void dispatchAppVisibility(boolean paramBoolean)
    {
      ViewRootImpl localViewRootImpl = (ViewRootImpl)mViewAncestor.get();
      if (localViewRootImpl != null) {
        localViewRootImpl.dispatchAppVisibility(paramBoolean);
      }
    }
    
    public void dispatchDragEvent(DragEvent paramDragEvent)
    {
      ViewRootImpl localViewRootImpl = (ViewRootImpl)mViewAncestor.get();
      if (localViewRootImpl != null) {
        localViewRootImpl.dispatchDragEvent(paramDragEvent);
      }
    }
    
    public void dispatchGetNewSurface()
    {
      ViewRootImpl localViewRootImpl = (ViewRootImpl)mViewAncestor.get();
      if (localViewRootImpl != null) {
        localViewRootImpl.dispatchGetNewSurface();
      }
    }
    
    public void dispatchPointerCaptureChanged(boolean paramBoolean)
    {
      ViewRootImpl localViewRootImpl = (ViewRootImpl)mViewAncestor.get();
      if (localViewRootImpl != null) {
        localViewRootImpl.dispatchPointerCaptureChanged(paramBoolean);
      }
    }
    
    public void dispatchSystemUiVisibilityChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      ViewRootImpl localViewRootImpl = (ViewRootImpl)mViewAncestor.get();
      if (localViewRootImpl != null) {
        localViewRootImpl.dispatchSystemUiVisibilityChanged(paramInt1, paramInt2, paramInt3, paramInt4);
      }
    }
    
    public void dispatchWallpaperCommand(String paramString, int paramInt1, int paramInt2, int paramInt3, Bundle paramBundle, boolean paramBoolean)
    {
      if (paramBoolean) {
        try
        {
          mWindowSession.wallpaperCommandComplete(asBinder(), null);
        }
        catch (RemoteException paramString) {}
      }
    }
    
    public void dispatchWallpaperOffsets(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, boolean paramBoolean)
    {
      if (paramBoolean) {
        try
        {
          mWindowSession.wallpaperOffsetsComplete(asBinder());
        }
        catch (RemoteException localRemoteException) {}
      }
    }
    
    public void dispatchWindowShown()
    {
      ViewRootImpl localViewRootImpl = (ViewRootImpl)mViewAncestor.get();
      if (localViewRootImpl != null) {
        localViewRootImpl.dispatchWindowShown();
      }
    }
    
    /* Error */
    public void executeCommand(String paramString1, String paramString2, android.os.ParcelFileDescriptor paramParcelFileDescriptor)
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 24	android/view/ViewRootImpl$W:mViewAncestor	Ljava/lang/ref/WeakReference;
      //   4: invokevirtual 59	java/lang/ref/WeakReference:get	()Ljava/lang/Object;
      //   7: checkcast 6	android/view/ViewRootImpl
      //   10: astore 4
      //   12: aload 4
      //   14: ifnull +184 -> 198
      //   17: aload 4
      //   19: getfield 109	android/view/ViewRootImpl:mView	Landroid/view/View;
      //   22: astore 5
      //   24: aload 5
      //   26: ifnull +172 -> 198
      //   29: ldc 111
      //   31: invokestatic 113	android/view/ViewRootImpl$W:checkCallingPermission	(Ljava/lang/String;)I
      //   34: ifne +114 -> 148
      //   37: aconst_null
      //   38: astore 6
      //   40: aconst_null
      //   41: astore 7
      //   43: aload 7
      //   45: astore 4
      //   47: aload 6
      //   49: astore 8
      //   51: new 115	android/os/ParcelFileDescriptor$AutoCloseOutputStream
      //   54: astore 9
      //   56: aload 7
      //   58: astore 4
      //   60: aload 6
      //   62: astore 8
      //   64: aload 9
      //   66: aload_3
      //   67: invokespecial 118	android/os/ParcelFileDescriptor$AutoCloseOutputStream:<init>	(Landroid/os/ParcelFileDescriptor;)V
      //   70: aload 9
      //   72: astore_3
      //   73: aload_3
      //   74: astore 4
      //   76: aload_3
      //   77: astore 8
      //   79: aload 5
      //   81: aload_1
      //   82: aload_2
      //   83: aload_3
      //   84: invokestatic 124	android/view/ViewDebug:dispatchCommand	(Landroid/view/View;Ljava/lang/String;Ljava/lang/String;Ljava/io/OutputStream;)V
      //   87: aload_3
      //   88: invokevirtual 129	java/io/OutputStream:close	()V
      //   91: goto +26 -> 117
      //   94: astore_1
      //   95: goto +33 -> 128
      //   98: astore_1
      //   99: aload 8
      //   101: astore 4
      //   103: aload_1
      //   104: invokevirtual 132	java/io/IOException:printStackTrace	()V
      //   107: aload 8
      //   109: ifnull +89 -> 198
      //   112: aload 8
      //   114: invokevirtual 129	java/io/OutputStream:close	()V
      //   117: goto +81 -> 198
      //   120: astore_1
      //   121: aload_1
      //   122: invokevirtual 132	java/io/IOException:printStackTrace	()V
      //   125: goto -8 -> 117
      //   128: aload 4
      //   130: ifnull +16 -> 146
      //   133: aload 4
      //   135: invokevirtual 129	java/io/OutputStream:close	()V
      //   138: goto +8 -> 146
      //   141: astore_2
      //   142: aload_2
      //   143: invokevirtual 132	java/io/IOException:printStackTrace	()V
      //   146: aload_1
      //   147: athrow
      //   148: new 134	java/lang/StringBuilder
      //   151: dup
      //   152: invokespecial 135	java/lang/StringBuilder:<init>	()V
      //   155: astore_1
      //   156: aload_1
      //   157: ldc -119
      //   159: invokevirtual 141	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   162: pop
      //   163: aload_1
      //   164: invokestatic 44	android/os/Binder:getCallingPid	()I
      //   167: invokevirtual 144	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   170: pop
      //   171: aload_1
      //   172: ldc -110
      //   174: invokevirtual 141	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   177: pop
      //   178: aload_1
      //   179: invokestatic 47	android/os/Binder:getCallingUid	()I
      //   182: invokevirtual 144	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   185: pop
      //   186: new 148	java/lang/SecurityException
      //   189: dup
      //   190: aload_1
      //   191: invokevirtual 152	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   194: invokespecial 154	java/lang/SecurityException:<init>	(Ljava/lang/String;)V
      //   197: athrow
      //   198: return
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	199	0	this	W
      //   0	199	1	paramString1	String
      //   0	199	2	paramString2	String
      //   0	199	3	paramParcelFileDescriptor	android.os.ParcelFileDescriptor
      //   10	124	4	localObject1	Object
      //   22	58	5	localView	View
      //   38	23	6	localObject2	Object
      //   41	16	7	localObject3	Object
      //   49	64	8	localObject4	Object
      //   54	17	9	localAutoCloseOutputStream	android.os.ParcelFileDescriptor.AutoCloseOutputStream
      // Exception table:
      //   from	to	target	type
      //   51	56	94	finally
      //   64	70	94	finally
      //   79	87	94	finally
      //   103	107	94	finally
      //   51	56	98	java/io/IOException
      //   64	70	98	java/io/IOException
      //   79	87	98	java/io/IOException
      //   87	91	120	java/io/IOException
      //   112	117	120	java/io/IOException
      //   133	138	141	java/io/IOException
    }
    
    public void moved(int paramInt1, int paramInt2)
    {
      ViewRootImpl localViewRootImpl = (ViewRootImpl)mViewAncestor.get();
      if (localViewRootImpl != null) {
        localViewRootImpl.dispatchMoved(paramInt1, paramInt2);
      }
    }
    
    public void requestAppKeyboardShortcuts(IResultReceiver paramIResultReceiver, int paramInt)
    {
      ViewRootImpl localViewRootImpl = (ViewRootImpl)mViewAncestor.get();
      if (localViewRootImpl != null) {
        localViewRootImpl.dispatchRequestKeyboardShortcuts(paramIResultReceiver, paramInt);
      }
    }
    
    public void resized(Rect paramRect1, Rect paramRect2, Rect paramRect3, Rect paramRect4, Rect paramRect5, Rect paramRect6, boolean paramBoolean1, MergedConfiguration paramMergedConfiguration, Rect paramRect7, boolean paramBoolean2, boolean paramBoolean3, int paramInt, DisplayCutout.ParcelableWrapper paramParcelableWrapper)
    {
      ViewRootImpl localViewRootImpl = (ViewRootImpl)mViewAncestor.get();
      if (localViewRootImpl != null) {
        localViewRootImpl.dispatchResized(paramRect1, paramRect2, paramRect3, paramRect4, paramRect5, paramRect6, paramBoolean1, paramMergedConfiguration, paramRect7, paramBoolean2, paramBoolean3, paramInt, paramParcelableWrapper);
      }
    }
    
    public void updatePointerIcon(float paramFloat1, float paramFloat2)
    {
      ViewRootImpl localViewRootImpl = (ViewRootImpl)mViewAncestor.get();
      if (localViewRootImpl != null) {
        localViewRootImpl.updatePointerIcon(paramFloat1, paramFloat2);
      }
    }
    
    public void windowFocusChanged(boolean paramBoolean1, boolean paramBoolean2)
    {
      ViewRootImpl localViewRootImpl = (ViewRootImpl)mViewAncestor.get();
      if (localViewRootImpl != null) {
        localViewRootImpl.windowFocusChanged(paramBoolean1, paramBoolean2);
      }
    }
  }
  
  final class WindowInputEventReceiver
    extends InputEventReceiver
  {
    public WindowInputEventReceiver(InputChannel paramInputChannel, Looper paramLooper)
    {
      super(paramLooper);
    }
    
    public void dispose()
    {
      unscheduleConsumeBatchedInput();
      super.dispose();
    }
    
    public void onBatchedInputEventPending()
    {
      if (mUnbufferedInputDispatch) {
        super.onBatchedInputEventPending();
      } else {
        scheduleConsumeBatchedInput();
      }
    }
    
    public void onInputEvent(InputEvent paramInputEvent, int paramInt)
    {
      enqueueInputEvent(paramInputEvent, this, 0, true);
    }
  }
  
  static abstract interface WindowStoppedCallback
  {
    public abstract void windowStopped(boolean paramBoolean);
  }
}
