package android.service.dreams;

import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.IRemoteCallback;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.MathUtils;
import android.util.Slog;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.Window.Callback;
import android.view.WindowManager;
import android.view.WindowManager.BadTokenException;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManagerGlobal;
import android.view.accessibility.AccessibilityEvent;
import com.android.internal.policy.PhoneWindow;
import com.android.internal.util.DumpUtils;
import com.android.internal.util.DumpUtils.Dump;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class DreamService
  extends Service
  implements Window.Callback
{
  public static final String DREAM_META_DATA = "android.service.dream";
  public static final String DREAM_SERVICE = "dreams";
  public static final String SERVICE_INTERFACE = "android.service.dreams.DreamService";
  private final String TAG;
  private boolean mCanDoze;
  private boolean mDebug;
  private int mDozeScreenBrightness;
  private int mDozeScreenState;
  private boolean mDozing;
  private boolean mFinished;
  private boolean mFullscreen;
  private final Handler mHandler;
  private boolean mInteractive;
  private boolean mLowProfile;
  private final IDreamManager mSandman;
  private boolean mScreenBright;
  private boolean mStarted;
  private boolean mWaking;
  private Window mWindow;
  private IBinder mWindowToken;
  private boolean mWindowless;
  
  public DreamService()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(DreamService.class.getSimpleName());
    localStringBuilder.append("[");
    localStringBuilder.append(getClass().getSimpleName());
    localStringBuilder.append("]");
    TAG = localStringBuilder.toString();
    mHandler = new Handler();
    mLowProfile = true;
    mScreenBright = true;
    mDozeScreenState = 0;
    mDozeScreenBrightness = -1;
    mDebug = false;
    mSandman = IDreamManager.Stub.asInterface(ServiceManager.getService("dreams"));
  }
  
  private int applyFlags(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt3 & paramInt1 | paramInt2 & paramInt3;
  }
  
  private void applySystemUiVisibilityFlags(int paramInt1, int paramInt2)
  {
    View localView;
    if (mWindow == null) {
      localView = null;
    } else {
      localView = mWindow.getDecorView();
    }
    if (localView != null) {
      localView.setSystemUiVisibility(applyFlags(localView.getSystemUiVisibility(), paramInt1, paramInt2));
    }
  }
  
  private void applyWindowFlags(int paramInt1, int paramInt2)
  {
    if (mWindow != null)
    {
      WindowManager.LayoutParams localLayoutParams = mWindow.getAttributes();
      flags = applyFlags(flags, paramInt1, paramInt2);
      mWindow.setAttributes(localLayoutParams);
      mWindow.getWindowManager().updateViewLayout(mWindow.getDecorView(), localLayoutParams);
    }
  }
  
  private final void attach(IBinder paramIBinder, boolean paramBoolean, final IRemoteCallback paramIRemoteCallback)
  {
    if (mWindowToken != null)
    {
      paramIBinder = TAG;
      paramIRemoteCallback = new StringBuilder();
      paramIRemoteCallback.append("attach() called when already attached with token=");
      paramIRemoteCallback.append(mWindowToken);
      Slog.e(paramIBinder, paramIRemoteCallback.toString());
      return;
    }
    if ((!mFinished) && (!mWaking))
    {
      mWindowToken = paramIBinder;
      mCanDoze = paramBoolean;
      if ((mWindowless) && (!mCanDoze)) {
        throw new IllegalStateException("Only doze dreams can be windowless");
      }
      if (!mWindowless)
      {
        mWindow = new PhoneWindow(this);
        mWindow.setCallback(this);
        mWindow.requestFeature(1);
        mWindow.setBackgroundDrawable(new ColorDrawable(-16777216));
        mWindow.setFormat(-1);
        paramBoolean = mDebug;
        int i = 0;
        if (paramBoolean) {
          Slog.v(TAG, String.format("Attaching window token: %s to window of type %s", new Object[] { paramIBinder, Integer.valueOf(2023) }));
        }
        WindowManager.LayoutParams localLayoutParams = mWindow.getAttributes();
        type = 2023;
        token = paramIBinder;
        windowAnimations = 16974576;
        int j = flags;
        int k;
        if (mFullscreen) {
          k = 1024;
        } else {
          k = 0;
        }
        if (mScreenBright) {
          i = 128;
        }
        flags = (j | i | 0x490101 | k);
        mWindow.setAttributes(localLayoutParams);
        mWindow.clearFlags(Integer.MIN_VALUE);
        mWindow.setWindowManager(null, paramIBinder, "dream", true);
        applySystemUiVisibilityFlags(mLowProfile, 1);
        try
        {
          getWindowManager().addView(mWindow.getDecorView(), mWindow.getAttributes());
        }
        catch (WindowManager.BadTokenException paramIBinder)
        {
          Slog.i(TAG, "attach() called after window token already removed, dream will finish soon");
          mWindow = null;
          return;
        }
      }
      mHandler.post(new Runnable()
      {
        /* Error */
        public void run()
        {
          // Byte code:
          //   0: aload_0
          //   1: getfield 19	android/service/dreams/DreamService$1:this$0	Landroid/service/dreams/DreamService;
          //   4: invokestatic 32	android/service/dreams/DreamService:access$100	(Landroid/service/dreams/DreamService;)Landroid/view/Window;
          //   7: ifnonnull +13 -> 20
          //   10: aload_0
          //   11: getfield 19	android/service/dreams/DreamService$1:this$0	Landroid/service/dreams/DreamService;
          //   14: invokestatic 36	android/service/dreams/DreamService:access$200	(Landroid/service/dreams/DreamService;)Z
          //   17: ifeq +52 -> 69
          //   20: aload_0
          //   21: getfield 19	android/service/dreams/DreamService$1:this$0	Landroid/service/dreams/DreamService;
          //   24: invokestatic 39	android/service/dreams/DreamService:access$300	(Landroid/service/dreams/DreamService;)Z
          //   27: ifeq +16 -> 43
          //   30: aload_0
          //   31: getfield 19	android/service/dreams/DreamService$1:this$0	Landroid/service/dreams/DreamService;
          //   34: invokestatic 43	android/service/dreams/DreamService:access$400	(Landroid/service/dreams/DreamService;)Ljava/lang/String;
          //   37: ldc 45
          //   39: invokestatic 51	android/util/Slog:v	(Ljava/lang/String;Ljava/lang/String;)I
          //   42: pop
          //   43: aload_0
          //   44: getfield 19	android/service/dreams/DreamService$1:this$0	Landroid/service/dreams/DreamService;
          //   47: iconst_1
          //   48: invokestatic 55	android/service/dreams/DreamService:access$502	(Landroid/service/dreams/DreamService;Z)Z
          //   51: pop
          //   52: aload_0
          //   53: getfield 19	android/service/dreams/DreamService$1:this$0	Landroid/service/dreams/DreamService;
          //   56: invokevirtual 58	android/service/dreams/DreamService:onDreamingStarted	()V
          //   59: aload_0
          //   60: getfield 21	android/service/dreams/DreamService$1:val$started	Landroid/os/IRemoteCallback;
          //   63: aconst_null
          //   64: invokeinterface 64 2 0
          //   69: return
          //   70: astore_1
          //   71: aload_1
          //   72: invokevirtual 68	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
          //   75: athrow
          //   76: astore_1
          //   77: aload_0
          //   78: getfield 21	android/service/dreams/DreamService$1:val$started	Landroid/os/IRemoteCallback;
          //   81: aconst_null
          //   82: invokeinterface 64 2 0
          //   87: aload_1
          //   88: athrow
          //   89: astore_1
          //   90: aload_1
          //   91: invokevirtual 68	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
          //   94: athrow
          // Local variable table:
          //   start	length	slot	name	signature
          //   0	95	0	this	1
          //   70	2	1	localRemoteException1	RemoteException
          //   76	12	1	localObject	Object
          //   89	2	1	localRemoteException2	RemoteException
          // Exception table:
          //   from	to	target	type
          //   59	69	70	android/os/RemoteException
          //   52	59	76	finally
          //   77	87	89	android/os/RemoteException
        }
      });
      return;
    }
    Slog.w(TAG, "attach() called after dream already finished");
    try
    {
      mSandman.finishSelf(paramIBinder, true);
    }
    catch (RemoteException paramIBinder) {}
  }
  
  private static int clampAbsoluteBrightness(int paramInt)
  {
    return MathUtils.constrain(paramInt, 0, 255);
  }
  
  private final void detach()
  {
    if (mStarted)
    {
      if (mDebug) {
        Slog.v(TAG, "detach(): Calling onDreamingStopped()");
      }
      mStarted = false;
      onDreamingStopped();
    }
    if (mWindow != null)
    {
      if (mDebug) {
        Slog.v(TAG, "detach(): Removing window from window manager");
      }
      mWindow.getWindowManager().removeViewImmediate(mWindow.getDecorView());
      mWindow = null;
    }
    if (mWindowToken != null)
    {
      WindowManagerGlobal.getInstance().closeAll(mWindowToken, getClass().getName(), "Dream");
      mWindowToken = null;
      mCanDoze = false;
    }
  }
  
  private boolean getSystemUiVisibilityFlagValue(int paramInt, boolean paramBoolean)
  {
    View localView;
    if (mWindow == null) {
      localView = null;
    } else {
      localView = mWindow.getDecorView();
    }
    if (localView != null) {
      if ((localView.getSystemUiVisibility() & paramInt) != 0) {
        paramBoolean = true;
      } else {
        paramBoolean = false;
      }
    }
    return paramBoolean;
  }
  
  private boolean getWindowFlagValue(int paramInt, boolean paramBoolean)
  {
    if (mWindow != null) {
      if ((mWindow.getAttributes().flags & paramInt) != 0) {
        paramBoolean = true;
      } else {
        paramBoolean = false;
      }
    }
    return paramBoolean;
  }
  
  private void updateDoze()
  {
    if (mWindowToken == null)
    {
      Slog.w(TAG, "Updating doze without a window token.");
      return;
    }
    if (mDozing) {
      try
      {
        mSandman.startDozing(mWindowToken, mDozeScreenState, mDozeScreenBrightness);
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  private void wakeUp(boolean paramBoolean)
  {
    if (mDebug)
    {
      String str = TAG;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("wakeUp(): fromSystem=");
      localStringBuilder.append(paramBoolean);
      localStringBuilder.append(", mWaking=");
      localStringBuilder.append(mWaking);
      localStringBuilder.append(", mFinished=");
      localStringBuilder.append(mFinished);
      Slog.v(str, localStringBuilder.toString());
    }
    if ((!mWaking) && (!mFinished))
    {
      mWaking = true;
      onWakeUp();
      if ((!paramBoolean) && (!mFinished)) {
        if (mWindowToken == null) {
          Slog.w(TAG, "WakeUp was called before the dream was attached.");
        } else {
          try
          {
            mSandman.finishSelf(mWindowToken, false);
          }
          catch (RemoteException localRemoteException) {}
        }
      }
    }
  }
  
  public void addContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    getWindow().addContentView(paramView, paramLayoutParams);
  }
  
  public boolean canDoze()
  {
    return mCanDoze;
  }
  
  public boolean dispatchGenericMotionEvent(MotionEvent paramMotionEvent)
  {
    if (!mInteractive)
    {
      if (mDebug) {
        Slog.v(TAG, "Waking up on genericMotionEvent");
      }
      wakeUp();
      return true;
    }
    return mWindow.superDispatchGenericMotionEvent(paramMotionEvent);
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    if (!mInteractive)
    {
      if (mDebug) {
        Slog.v(TAG, "Waking up on keyEvent");
      }
      wakeUp();
      return true;
    }
    if (paramKeyEvent.getKeyCode() == 4)
    {
      if (mDebug) {
        Slog.v(TAG, "Waking up on back key");
      }
      wakeUp();
      return true;
    }
    return mWindow.superDispatchKeyEvent(paramKeyEvent);
  }
  
  public boolean dispatchKeyShortcutEvent(KeyEvent paramKeyEvent)
  {
    if (!mInteractive)
    {
      if (mDebug) {
        Slog.v(TAG, "Waking up on keyShortcutEvent");
      }
      wakeUp();
      return true;
    }
    return mWindow.superDispatchKeyShortcutEvent(paramKeyEvent);
  }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    return false;
  }
  
  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
  {
    if (!mInteractive)
    {
      if (mDebug) {
        Slog.v(TAG, "Waking up on touchEvent");
      }
      wakeUp();
      return true;
    }
    return mWindow.superDispatchTouchEvent(paramMotionEvent);
  }
  
  public boolean dispatchTrackballEvent(MotionEvent paramMotionEvent)
  {
    if (!mInteractive)
    {
      if (mDebug) {
        Slog.v(TAG, "Waking up on trackballEvent");
      }
      wakeUp();
      return true;
    }
    return mWindow.superDispatchTrackballEvent(paramMotionEvent);
  }
  
  protected void dump(final FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, final String[] paramArrayOfString)
  {
    DumpUtils.dumpAsync(mHandler, new DumpUtils.Dump()
    {
      public void dump(PrintWriter paramAnonymousPrintWriter, String paramAnonymousString)
      {
        dumpOnHandler(paramFileDescriptor, paramAnonymousPrintWriter, paramArrayOfString);
      }
    }, paramPrintWriter, "", 1000L);
  }
  
  protected void dumpOnHandler(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(TAG);
    paramFileDescriptor.append(": ");
    paramPrintWriter.print(paramFileDescriptor.toString());
    if (mWindowToken == null)
    {
      paramPrintWriter.println("stopped");
    }
    else
    {
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append("running (token=");
      paramFileDescriptor.append(mWindowToken);
      paramFileDescriptor.append(")");
      paramPrintWriter.println(paramFileDescriptor.toString());
    }
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("  window: ");
    paramFileDescriptor.append(mWindow);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramPrintWriter.print("  flags:");
    if (isInteractive()) {
      paramPrintWriter.print(" interactive");
    }
    if (isLowProfile()) {
      paramPrintWriter.print(" lowprofile");
    }
    if (isFullscreen()) {
      paramPrintWriter.print(" fullscreen");
    }
    if (isScreenBright()) {
      paramPrintWriter.print(" bright");
    }
    if (isWindowless()) {
      paramPrintWriter.print(" windowless");
    }
    if (isDozing()) {
      paramPrintWriter.print(" dozing");
    } else if (canDoze()) {
      paramPrintWriter.print(" candoze");
    }
    paramPrintWriter.println();
    if (canDoze())
    {
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append("  doze screen state: ");
      paramFileDescriptor.append(Display.stateToString(mDozeScreenState));
      paramPrintWriter.println(paramFileDescriptor.toString());
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append("  doze screen brightness: ");
      paramFileDescriptor.append(mDozeScreenBrightness);
      paramPrintWriter.println(paramFileDescriptor.toString());
    }
  }
  
  public <T extends View> T findViewById(int paramInt)
  {
    return getWindow().findViewById(paramInt);
  }
  
  public final void finish()
  {
    if (mDebug)
    {
      String str = TAG;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("finish(): mFinished=");
      localStringBuilder.append(mFinished);
      Slog.v(str, localStringBuilder.toString());
    }
    if (!mFinished)
    {
      mFinished = true;
      if (mWindowToken == null) {
        Slog.w(TAG, "Finish was called before the dream was attached.");
      } else {
        try
        {
          mSandman.finishSelf(mWindowToken, true);
        }
        catch (RemoteException localRemoteException) {}
      }
      stopSelf();
    }
  }
  
  public int getDozeScreenBrightness()
  {
    return mDozeScreenBrightness;
  }
  
  public int getDozeScreenState()
  {
    return mDozeScreenState;
  }
  
  public Window getWindow()
  {
    return mWindow;
  }
  
  public WindowManager getWindowManager()
  {
    WindowManager localWindowManager;
    if (mWindow != null) {
      localWindowManager = mWindow.getWindowManager();
    } else {
      localWindowManager = null;
    }
    return localWindowManager;
  }
  
  public boolean isDozing()
  {
    return mDozing;
  }
  
  public boolean isFullscreen()
  {
    return mFullscreen;
  }
  
  public boolean isInteractive()
  {
    return mInteractive;
  }
  
  public boolean isLowProfile()
  {
    return getSystemUiVisibilityFlagValue(1, mLowProfile);
  }
  
  public boolean isScreenBright()
  {
    return getWindowFlagValue(128, mScreenBright);
  }
  
  public boolean isWindowless()
  {
    return mWindowless;
  }
  
  public void onActionModeFinished(ActionMode paramActionMode) {}
  
  public void onActionModeStarted(ActionMode paramActionMode) {}
  
  public void onAttachedToWindow() {}
  
  public final IBinder onBind(Intent paramIntent)
  {
    if (mDebug)
    {
      String str = TAG;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onBind() intent = ");
      localStringBuilder.append(paramIntent);
      Slog.v(str, localStringBuilder.toString());
    }
    return new DreamServiceWrapper(null);
  }
  
  public void onContentChanged() {}
  
  public void onCreate()
  {
    if (mDebug) {
      Slog.v(TAG, "onCreate()");
    }
    super.onCreate();
  }
  
  public boolean onCreatePanelMenu(int paramInt, Menu paramMenu)
  {
    return false;
  }
  
  public View onCreatePanelView(int paramInt)
  {
    return null;
  }
  
  public void onDestroy()
  {
    if (mDebug) {
      Slog.v(TAG, "onDestroy()");
    }
    detach();
    super.onDestroy();
  }
  
  public void onDetachedFromWindow() {}
  
  public void onDreamingStarted()
  {
    if (mDebug) {
      Slog.v(TAG, "onDreamingStarted()");
    }
  }
  
  public void onDreamingStopped()
  {
    if (mDebug) {
      Slog.v(TAG, "onDreamingStopped()");
    }
  }
  
  public boolean onMenuItemSelected(int paramInt, MenuItem paramMenuItem)
  {
    return false;
  }
  
  public boolean onMenuOpened(int paramInt, Menu paramMenu)
  {
    return false;
  }
  
  public void onPanelClosed(int paramInt, Menu paramMenu) {}
  
  public boolean onPreparePanel(int paramInt, View paramView, Menu paramMenu)
  {
    return false;
  }
  
  public boolean onSearchRequested()
  {
    return false;
  }
  
  public boolean onSearchRequested(SearchEvent paramSearchEvent)
  {
    return onSearchRequested();
  }
  
  public void onWakeUp()
  {
    finish();
  }
  
  public void onWindowAttributesChanged(WindowManager.LayoutParams paramLayoutParams) {}
  
  public void onWindowFocusChanged(boolean paramBoolean) {}
  
  public ActionMode onWindowStartingActionMode(ActionMode.Callback paramCallback)
  {
    return null;
  }
  
  public ActionMode onWindowStartingActionMode(ActionMode.Callback paramCallback, int paramInt)
  {
    return null;
  }
  
  public final <T extends View> T requireViewById(int paramInt)
  {
    View localView = findViewById(paramInt);
    if (localView != null) {
      return localView;
    }
    throw new IllegalArgumentException("ID does not reference a View inside this DreamService");
  }
  
  public void setContentView(int paramInt)
  {
    getWindow().setContentView(paramInt);
  }
  
  public void setContentView(View paramView)
  {
    getWindow().setContentView(paramView);
  }
  
  public void setContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    getWindow().setContentView(paramView, paramLayoutParams);
  }
  
  public void setDebug(boolean paramBoolean)
  {
    mDebug = paramBoolean;
  }
  
  public void setDozeScreenBrightness(int paramInt)
  {
    int i = paramInt;
    if (paramInt != -1) {
      i = clampAbsoluteBrightness(paramInt);
    }
    if (mDozeScreenBrightness != i)
    {
      mDozeScreenBrightness = i;
      updateDoze();
    }
  }
  
  public void setDozeScreenState(int paramInt)
  {
    if (mDozeScreenState != paramInt)
    {
      mDozeScreenState = paramInt;
      updateDoze();
    }
  }
  
  public void setFullscreen(boolean paramBoolean)
  {
    if (mFullscreen != paramBoolean)
    {
      mFullscreen = paramBoolean;
      int i;
      if (mFullscreen) {
        i = 1024;
      } else {
        i = 0;
      }
      applyWindowFlags(i, 1024);
    }
  }
  
  public void setInteractive(boolean paramBoolean)
  {
    mInteractive = paramBoolean;
  }
  
  public void setLowProfile(boolean paramBoolean)
  {
    if (mLowProfile != paramBoolean)
    {
      mLowProfile = paramBoolean;
      int i;
      if (mLowProfile) {
        i = 1;
      } else {
        i = 0;
      }
      applySystemUiVisibilityFlags(i, 1);
    }
  }
  
  public void setScreenBright(boolean paramBoolean)
  {
    if (mScreenBright != paramBoolean)
    {
      mScreenBright = paramBoolean;
      int i;
      if (mScreenBright) {
        i = 128;
      } else {
        i = 0;
      }
      applyWindowFlags(i, 128);
    }
  }
  
  public void setWindowless(boolean paramBoolean)
  {
    mWindowless = paramBoolean;
  }
  
  public void startDozing()
  {
    if ((mCanDoze) && (!mDozing))
    {
      mDozing = true;
      updateDoze();
    }
  }
  
  public void stopDozing()
  {
    if (mDozing)
    {
      mDozing = false;
      try
      {
        mSandman.stopDozing(mWindowToken);
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  public final void wakeUp()
  {
    wakeUp(false);
  }
  
  private final class DreamServiceWrapper
    extends IDreamService.Stub
  {
    private DreamServiceWrapper() {}
    
    public void attach(final IBinder paramIBinder, final boolean paramBoolean, final IRemoteCallback paramIRemoteCallback)
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          DreamService.this.attach(paramIBinder, paramBoolean, paramIRemoteCallback);
        }
      });
    }
    
    public void detach()
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          DreamService.this.detach();
        }
      });
    }
    
    public void wakeUp()
    {
      mHandler.post(new Runnable()
      {
        public void run()
        {
          DreamService.this.wakeUp(true);
        }
      });
    }
  }
}
