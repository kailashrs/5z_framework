package android.service.wallpaper;

import android.app.Service;
import android.app.WallpaperColors;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManager.DisplayListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.util.MergedConfiguration;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.DisplayCutout.ParcelableWrapper;
import android.view.IWindowSession;
import android.view.InputChannel;
import android.view.InputEvent;
import android.view.InputEventReceiver;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.WindowInsets;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManagerGlobal;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.HandlerCaller;
import com.android.internal.os.HandlerCaller.Callback;
import com.android.internal.view.BaseIWindow;
import com.android.internal.view.BaseSurfaceHolder;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.function.Supplier;

public abstract class WallpaperService
  extends Service
{
  static final boolean DEBUG = false;
  private static final int DO_ATTACH = 10;
  private static final int DO_DETACH = 20;
  private static final int DO_IN_AMBIENT_MODE = 50;
  private static final int DO_SET_DESIRED_SIZE = 30;
  private static final int DO_SET_DISPLAY_PADDING = 40;
  private static final int MSG_REQUEST_WALLPAPER_COLORS = 10050;
  private static final int MSG_TOUCH_EVENT = 10040;
  private static final int MSG_UPDATE_SURFACE = 10000;
  private static final int MSG_VISIBILITY_CHANGED = 10010;
  private static final int MSG_WALLPAPER_COMMAND = 10025;
  private static final int MSG_WALLPAPER_OFFSETS = 10020;
  private static final int MSG_WINDOW_MOVED = 10035;
  private static final int MSG_WINDOW_RESIZED = 10030;
  private static final int NOTIFY_COLORS_RATE_LIMIT_MS = 1000;
  public static final String SERVICE_INTERFACE = "android.service.wallpaper.WallpaperService";
  public static final String SERVICE_META_DATA = "android.service.wallpaper";
  static final String TAG = "WallpaperService";
  private final ArrayList<Engine> mActiveEngines = new ArrayList();
  
  public WallpaperService() {}
  
  protected void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter.print("State of wallpaper ");
    paramPrintWriter.print(this);
    paramPrintWriter.println(":");
    for (int i = 0; i < mActiveEngines.size(); i++)
    {
      Engine localEngine = (Engine)mActiveEngines.get(i);
      paramPrintWriter.print("  Engine ");
      paramPrintWriter.print(localEngine);
      paramPrintWriter.println(":");
      localEngine.dump("    ", paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    }
  }
  
  public final IBinder onBind(Intent paramIntent)
  {
    return new IWallpaperServiceWrapper(this);
  }
  
  public void onCreate()
  {
    super.onCreate();
  }
  
  public abstract Engine onCreateEngine();
  
  public void onDestroy()
  {
    super.onDestroy();
    for (int i = 0; i < mActiveEngines.size(); i++) {
      ((Engine)mActiveEngines.get(i)).detach();
    }
    mActiveEngines.clear();
  }
  
  public class Engine
  {
    final Rect mBackdropFrame = new Rect();
    HandlerCaller mCaller;
    private final Supplier<Long> mClockFunction;
    IWallpaperConnection mConnection;
    final Rect mContentInsets = new Rect();
    boolean mCreated;
    int mCurHeight;
    int mCurWidth;
    int mCurWindowFlags = mWindowFlags;
    int mCurWindowPrivateFlags = mWindowPrivateFlags;
    boolean mDestroyed;
    final Rect mDispatchedContentInsets = new Rect();
    DisplayCutout mDispatchedDisplayCutout = DisplayCutout.NO_CUTOUT;
    final Rect mDispatchedOutsets = new Rect();
    final Rect mDispatchedOverscanInsets = new Rect();
    final Rect mDispatchedStableInsets = new Rect();
    Display mDisplay;
    final DisplayCutout.ParcelableWrapper mDisplayCutout = new DisplayCutout.ParcelableWrapper();
    private final DisplayManager.DisplayListener mDisplayListener = new DisplayManager.DisplayListener()
    {
      public void onDisplayAdded(int paramAnonymousInt) {}
      
      public void onDisplayChanged(int paramAnonymousInt)
      {
        if (mDisplay.getDisplayId() == paramAnonymousInt) {
          reportVisibility();
        }
      }
      
      public void onDisplayRemoved(int paramAnonymousInt) {}
    };
    DisplayManager mDisplayManager;
    private int mDisplayState;
    boolean mDrawingAllowed;
    final Rect mFinalStableInsets = new Rect();
    final Rect mFinalSystemInsets = new Rect();
    boolean mFixedSizeAllowed;
    int mFormat;
    private final Handler mHandler;
    int mHeight;
    WallpaperService.IWallpaperEngineWrapper mIWallpaperEngine;
    boolean mInitializing = true;
    InputChannel mInputChannel;
    WallpaperInputEventReceiver mInputEventReceiver;
    boolean mIsCreating;
    boolean mIsInAmbientMode;
    private long mLastColorInvalidation;
    final WindowManager.LayoutParams mLayout = new WindowManager.LayoutParams();
    final Object mLock = new Object();
    final MergedConfiguration mMergedConfiguration = new MergedConfiguration();
    private final Runnable mNotifyColorsChanged = new _..Lambda.vsWBQpiXExY07tlrSzTqh4pNQAQ(this);
    boolean mOffsetMessageEnqueued;
    boolean mOffsetsChanged;
    final Rect mOutsets = new Rect();
    final Rect mOverscanInsets = new Rect();
    MotionEvent mPendingMove;
    boolean mPendingSync;
    float mPendingXOffset;
    float mPendingXOffsetStep;
    float mPendingYOffset;
    float mPendingYOffsetStep;
    boolean mReportedVisible;
    IWindowSession mSession;
    final Rect mStableInsets = new Rect();
    boolean mSurfaceCreated;
    final BaseSurfaceHolder mSurfaceHolder = new BaseSurfaceHolder()
    {
      private void prepareToDraw()
      {
        if ((mDisplayState == 3) || (mDisplayState == 4)) {
          try
          {
            mSession.pokeDrawLock(mWindow);
          }
          catch (RemoteException localRemoteException) {}
        }
      }
      
      public boolean isCreating()
      {
        return mIsCreating;
      }
      
      public Canvas lockCanvas()
      {
        prepareToDraw();
        return super.lockCanvas();
      }
      
      public Canvas lockCanvas(Rect paramAnonymousRect)
      {
        prepareToDraw();
        return super.lockCanvas(paramAnonymousRect);
      }
      
      public Canvas lockHardwareCanvas()
      {
        prepareToDraw();
        return super.lockHardwareCanvas();
      }
      
      public boolean onAllowLockCanvas()
      {
        return mDrawingAllowed;
      }
      
      public void onRelayoutContainer()
      {
        Message localMessage = mCaller.obtainMessage(10000);
        mCaller.sendMessage(localMessage);
      }
      
      public void onUpdateSurface()
      {
        Message localMessage = mCaller.obtainMessage(10000);
        mCaller.sendMessage(localMessage);
      }
      
      public void setFixedSize(int paramAnonymousInt1, int paramAnonymousInt2)
      {
        if (mFixedSizeAllowed)
        {
          super.setFixedSize(paramAnonymousInt1, paramAnonymousInt2);
          return;
        }
        throw new UnsupportedOperationException("Wallpapers currently only support sizing from layout");
      }
      
      public void setKeepScreenOn(boolean paramAnonymousBoolean)
      {
        throw new UnsupportedOperationException("Wallpapers do not support keep screen on");
      }
    };
    int mType;
    boolean mVisible;
    final Rect mVisibleInsets = new Rect();
    private WallpaperConnectInfo mWallpaperConnectInfo = null;
    int mWidth;
    final Rect mWinFrame = new Rect();
    final BaseIWindow mWindow = new BaseIWindow()
    {
      public void dispatchAppVisibility(boolean paramAnonymousBoolean)
      {
        if (!mIWallpaperEngine.mIsPreview)
        {
          Object localObject = mCaller;
          localObject = ((HandlerCaller)localObject).obtainMessageI(10010, paramAnonymousBoolean);
          mCaller.sendMessage((Message)localObject);
        }
      }
      
      public void dispatchWallpaperCommand(String paramAnonymousString, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, Bundle paramAnonymousBundle, boolean paramAnonymousBoolean)
      {
        synchronized (mLock)
        {
          WallpaperService.WallpaperCommand localWallpaperCommand = new android/service/wallpaper/WallpaperService$WallpaperCommand;
          localWallpaperCommand.<init>();
          action = paramAnonymousString;
          x = paramAnonymousInt1;
          y = paramAnonymousInt2;
          z = paramAnonymousInt3;
          extras = paramAnonymousBundle;
          sync = paramAnonymousBoolean;
          paramAnonymousString = mCaller.obtainMessage(10025);
          obj = localWallpaperCommand;
          mCaller.sendMessage(paramAnonymousString);
          return;
        }
      }
      
      public void dispatchWallpaperOffsets(float paramAnonymousFloat1, float paramAnonymousFloat2, float paramAnonymousFloat3, float paramAnonymousFloat4, boolean paramAnonymousBoolean)
      {
        synchronized (mLock)
        {
          mPendingXOffset = paramAnonymousFloat1;
          mPendingYOffset = paramAnonymousFloat2;
          mPendingXOffsetStep = paramAnonymousFloat3;
          mPendingYOffsetStep = paramAnonymousFloat4;
          if (paramAnonymousBoolean) {
            mPendingSync = true;
          }
          if (!mOffsetMessageEnqueued)
          {
            mOffsetMessageEnqueued = true;
            Message localMessage = mCaller.obtainMessage(10020);
            mCaller.sendMessage(localMessage);
          }
          return;
        }
      }
      
      public void moved(int paramAnonymousInt1, int paramAnonymousInt2)
      {
        Message localMessage = mCaller.obtainMessageII(10035, paramAnonymousInt1, paramAnonymousInt2);
        mCaller.sendMessage(localMessage);
      }
      
      public void resized(Rect paramAnonymousRect1, Rect paramAnonymousRect2, Rect paramAnonymousRect3, Rect paramAnonymousRect4, Rect paramAnonymousRect5, Rect paramAnonymousRect6, boolean paramAnonymousBoolean1, MergedConfiguration paramAnonymousMergedConfiguration, Rect paramAnonymousRect7, boolean paramAnonymousBoolean2, boolean paramAnonymousBoolean3, int paramAnonymousInt, DisplayCutout.ParcelableWrapper paramAnonymousParcelableWrapper)
      {
        paramAnonymousRect1 = mCaller;
        paramAnonymousRect1 = paramAnonymousRect1.obtainMessageIO(10030, paramAnonymousBoolean1, paramAnonymousRect6);
        mCaller.sendMessage(paramAnonymousRect1);
      }
    };
    int mWindowFlags = 16;
    int mWindowPrivateFlags = 4;
    IBinder mWindowToken;
    
    public Engine()
    {
      this(_..Lambda.87Do_TfJA3qVM7QF6F_6BpQlQTA.INSTANCE, Handler.getMain());
    }
    
    @VisibleForTesting
    public Engine(Handler paramHandler)
    {
      mClockFunction = paramHandler;
      Object localObject;
      mHandler = localObject;
    }
    
    private void dispatchPointer(MotionEvent paramMotionEvent)
    {
      if (paramMotionEvent.isTouchEvent()) {
        synchronized (mLock)
        {
          if (paramMotionEvent.getAction() == 2) {
            mPendingMove = paramMotionEvent;
          } else {
            mPendingMove = null;
          }
          paramMotionEvent = mCaller.obtainMessageO(10040, paramMotionEvent);
          mCaller.sendMessage(paramMotionEvent);
        }
      }
      paramMotionEvent.recycle();
    }
    
    void attach(WallpaperService.IWallpaperEngineWrapper paramIWallpaperEngineWrapper)
    {
      if (mDestroyed) {
        return;
      }
      mIWallpaperEngine = paramIWallpaperEngineWrapper;
      mCaller = WallpaperService.IWallpaperEngineWrapper.access$200(paramIWallpaperEngineWrapper);
      mConnection = mConnection;
      mWindowToken = mWindowToken;
      mSurfaceHolder.setSizeFromLayout();
      mInitializing = true;
      mSession = WindowManagerGlobal.getWindowSession();
      mWindow.setSession(mSession);
      mLayout.packageName = getPackageName();
      mDisplayManager = ((DisplayManager)getSystemService("display"));
      mDisplayManager.registerDisplayListener(mDisplayListener, mCaller.getHandler());
      mDisplay = mDisplayManager.getDisplay(0);
      mDisplayState = mDisplay.getState();
      onCreate(mSurfaceHolder);
      mInitializing = false;
      mReportedVisible = false;
      updateSurface(false, false, false);
      if (isPreview()) {
        mWallpaperConnectInfo = new WallpaperConnectInfo(mWindowToken);
      }
    }
    
    void detach()
    {
      if (mDestroyed) {
        return;
      }
      mDestroyed = true;
      if (mDisplayManager != null) {
        mDisplayManager.unregisterDisplayListener(mDisplayListener);
      }
      if (mVisible)
      {
        mVisible = false;
        onVisibilityChanged(false);
      }
      reportSurfaceDestroyed();
      onDestroy();
      if (mCreated)
      {
        try
        {
          if (mInputEventReceiver != null)
          {
            mInputEventReceiver.dispose();
            mInputEventReceiver = null;
          }
          mSession.remove(mWindow);
        }
        catch (RemoteException localRemoteException) {}
        mSurfaceHolder.mSurface.release();
        mCreated = false;
        if (mInputChannel != null)
        {
          mInputChannel.dispose();
          mInputChannel = null;
        }
      }
      if (mWallpaperConnectInfo != null)
      {
        mWallpaperConnectInfo.unlinkDeathRecipient();
        mWallpaperConnectInfo = null;
      }
    }
    
    @VisibleForTesting
    public void doAmbientModeChanged(boolean paramBoolean1, boolean paramBoolean2)
    {
      if (!mDestroyed)
      {
        mIsInAmbientMode = paramBoolean1;
        if (mCreated) {
          onAmbientModeChanged(paramBoolean1, paramBoolean2);
        }
      }
    }
    
    void doCommand(WallpaperService.WallpaperCommand paramWallpaperCommand)
    {
      Bundle localBundle;
      if (!mDestroyed) {
        localBundle = onCommand(action, x, y, z, extras, sync);
      } else {
        localBundle = null;
      }
      if (sync) {
        try
        {
          mSession.wallpaperCommandComplete(mWindow.asBinder(), localBundle);
        }
        catch (RemoteException paramWallpaperCommand) {}
      }
    }
    
    void doDesiredSizeChanged(int paramInt1, int paramInt2)
    {
      if (!mDestroyed)
      {
        mIWallpaperEngine.mReqWidth = paramInt1;
        mIWallpaperEngine.mReqHeight = paramInt2;
        onDesiredSizeChanged(paramInt1, paramInt2);
        doOffsetsChanged(true);
      }
    }
    
    void doDisplayPaddingChanged(Rect paramRect)
    {
      if ((!mDestroyed) && (!mIWallpaperEngine.mDisplayPadding.equals(paramRect)))
      {
        mIWallpaperEngine.mDisplayPadding.set(paramRect);
        updateSurface(true, false, false);
      }
    }
    
    void doOffsetsChanged(boolean paramBoolean)
    {
      if (mDestroyed) {
        return;
      }
      if ((!paramBoolean) && (!mOffsetsChanged)) {
        return;
      }
      synchronized (mLock)
      {
        float f1 = mPendingXOffset;
        float f2 = mPendingYOffset;
        float f3 = mPendingXOffsetStep;
        float f4 = mPendingYOffsetStep;
        paramBoolean = mPendingSync;
        int i = 0;
        mPendingSync = false;
        mOffsetMessageEnqueued = false;
        if (mSurfaceCreated) {
          if (mReportedVisible)
          {
            int j = mIWallpaperEngine.mReqWidth - mCurWidth;
            if (j > 0) {
              j = -(int)(j * f1 + 0.5F);
            } else {
              j = 0;
            }
            int k = mIWallpaperEngine.mReqHeight - mCurHeight;
            if (k > 0) {
              i = -(int)(k * f2 + 0.5F);
            }
            for (;;)
            {
              break;
            }
            onOffsetsChanged(f1, f2, f3, f4, j, i);
          }
          else
          {
            mOffsetsChanged = true;
          }
        }
        if (paramBoolean) {
          try
          {
            mSession.wallpaperOffsetsComplete(mWindow.asBinder());
          }
          catch (RemoteException localRemoteException) {}
        }
        return;
      }
    }
    
    void doVisibilityChanged(boolean paramBoolean)
    {
      if (!mDestroyed)
      {
        mVisible = paramBoolean;
        reportVisibility();
      }
    }
    
    protected void dump(String paramString, FileDescriptor arg2, PrintWriter paramPrintWriter, String[] paramArrayOfString)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mInitializing=");
      paramPrintWriter.print(mInitializing);
      paramPrintWriter.print(" mDestroyed=");
      paramPrintWriter.println(mDestroyed);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mVisible=");
      paramPrintWriter.print(mVisible);
      paramPrintWriter.print(" mReportedVisible=");
      paramPrintWriter.println(mReportedVisible);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mDisplay=");
      paramPrintWriter.println(mDisplay);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mCreated=");
      paramPrintWriter.print(mCreated);
      paramPrintWriter.print(" mSurfaceCreated=");
      paramPrintWriter.print(mSurfaceCreated);
      paramPrintWriter.print(" mIsCreating=");
      paramPrintWriter.print(mIsCreating);
      paramPrintWriter.print(" mDrawingAllowed=");
      paramPrintWriter.println(mDrawingAllowed);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mWidth=");
      paramPrintWriter.print(mWidth);
      paramPrintWriter.print(" mCurWidth=");
      paramPrintWriter.print(mCurWidth);
      paramPrintWriter.print(" mHeight=");
      paramPrintWriter.print(mHeight);
      paramPrintWriter.print(" mCurHeight=");
      paramPrintWriter.println(mCurHeight);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mType=");
      paramPrintWriter.print(mType);
      paramPrintWriter.print(" mWindowFlags=");
      paramPrintWriter.print(mWindowFlags);
      paramPrintWriter.print(" mCurWindowFlags=");
      paramPrintWriter.println(mCurWindowFlags);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mWindowPrivateFlags=");
      paramPrintWriter.print(mWindowPrivateFlags);
      paramPrintWriter.print(" mCurWindowPrivateFlags=");
      paramPrintWriter.println(mCurWindowPrivateFlags);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mVisibleInsets=");
      paramPrintWriter.print(mVisibleInsets.toShortString());
      paramPrintWriter.print(" mWinFrame=");
      paramPrintWriter.print(mWinFrame.toShortString());
      paramPrintWriter.print(" mContentInsets=");
      paramPrintWriter.println(mContentInsets.toShortString());
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mConfiguration=");
      paramPrintWriter.println(mMergedConfiguration.getMergedConfiguration());
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mLayout=");
      paramPrintWriter.println(mLayout);
      synchronized (mLock)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mPendingXOffset=");
        paramPrintWriter.print(mPendingXOffset);
        paramPrintWriter.print(" mPendingXOffset=");
        paramPrintWriter.println(mPendingXOffset);
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mPendingXOffsetStep=");
        paramPrintWriter.print(mPendingXOffsetStep);
        paramPrintWriter.print(" mPendingXOffsetStep=");
        paramPrintWriter.println(mPendingXOffsetStep);
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mOffsetMessageEnqueued=");
        paramPrintWriter.print(mOffsetMessageEnqueued);
        paramPrintWriter.print(" mPendingSync=");
        paramPrintWriter.println(mPendingSync);
        if (mPendingMove != null)
        {
          paramPrintWriter.print(paramString);
          paramPrintWriter.print("mPendingMove=");
          paramPrintWriter.println(mPendingMove);
        }
        return;
      }
    }
    
    public int getDesiredMinimumHeight()
    {
      return mIWallpaperEngine.mReqHeight;
    }
    
    public int getDesiredMinimumWidth()
    {
      return mIWallpaperEngine.mReqWidth;
    }
    
    public SurfaceHolder getSurfaceHolder()
    {
      return mSurfaceHolder;
    }
    
    public boolean isInAmbientMode()
    {
      return mIsInAmbientMode;
    }
    
    public boolean isPreview()
    {
      return mIWallpaperEngine.mIsPreview;
    }
    
    public boolean isVisible()
    {
      return mReportedVisible;
    }
    
    public void notifyColorsChanged()
    {
      long l = ((Long)mClockFunction.get()).longValue();
      if (l - mLastColorInvalidation < 1000L)
      {
        Log.w("WallpaperService", "This call has been deferred. You should only call notifyColorsChanged() once every 1.0 seconds.");
        if (!mHandler.hasCallbacks(mNotifyColorsChanged)) {
          mHandler.postDelayed(mNotifyColorsChanged, 1000L);
        }
        return;
      }
      mLastColorInvalidation = l;
      mHandler.removeCallbacks(mNotifyColorsChanged);
      try
      {
        WallpaperColors localWallpaperColors = onComputeColors();
        if (mConnection != null) {
          mConnection.onWallpaperColorsChanged(localWallpaperColors);
        } else {
          Log.w("WallpaperService", "Can't notify system because wallpaper connection was not established.");
        }
      }
      catch (RemoteException localRemoteException)
      {
        Log.w("WallpaperService", "Can't notify system because wallpaper connection was lost.", localRemoteException);
      }
    }
    
    public void onAmbientModeChanged(boolean paramBoolean1, boolean paramBoolean2) {}
    
    public void onApplyWindowInsets(WindowInsets paramWindowInsets) {}
    
    public Bundle onCommand(String paramString, int paramInt1, int paramInt2, int paramInt3, Bundle paramBundle, boolean paramBoolean)
    {
      return null;
    }
    
    public WallpaperColors onComputeColors()
    {
      return null;
    }
    
    public void onCreate(SurfaceHolder paramSurfaceHolder) {}
    
    public void onDesiredSizeChanged(int paramInt1, int paramInt2) {}
    
    public void onDestroy() {}
    
    public void onOffsetsChanged(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt1, int paramInt2) {}
    
    public void onSurfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3) {}
    
    public void onSurfaceCreated(SurfaceHolder paramSurfaceHolder) {}
    
    public void onSurfaceDestroyed(SurfaceHolder paramSurfaceHolder) {}
    
    public void onSurfaceRedrawNeeded(SurfaceHolder paramSurfaceHolder) {}
    
    public void onTouchEvent(MotionEvent paramMotionEvent) {}
    
    public void onVisibilityChanged(boolean paramBoolean) {}
    
    void reportSurfaceDestroyed()
    {
      if (mSurfaceCreated)
      {
        int i = 0;
        mSurfaceCreated = false;
        mSurfaceHolder.ungetCallbacks();
        SurfaceHolder.Callback[] arrayOfCallback = mSurfaceHolder.getCallbacks();
        if (arrayOfCallback != null)
        {
          int j = arrayOfCallback.length;
          while (i < j)
          {
            arrayOfCallback[i].surfaceDestroyed(mSurfaceHolder);
            i++;
          }
        }
        onSurfaceDestroyed(mSurfaceHolder);
      }
    }
    
    void reportVisibility()
    {
      if (!mDestroyed)
      {
        int i;
        if (mDisplay == null) {
          i = 0;
        } else {
          i = mDisplay.getState();
        }
        mDisplayState = i;
        boolean bool1 = mVisible;
        boolean bool2 = true;
        if ((!bool1) || (mDisplayState == 1)) {
          bool2 = false;
        }
        if (mReportedVisible != bool2)
        {
          mReportedVisible = bool2;
          if (bool2)
          {
            doOffsetsChanged(false);
            updateSurface(false, false, false);
          }
          onVisibilityChanged(bool2);
        }
      }
    }
    
    @VisibleForTesting
    public void setCreated(boolean paramBoolean)
    {
      mCreated = paramBoolean;
    }
    
    public void setFixedSizeAllowed(boolean paramBoolean)
    {
      mFixedSizeAllowed = paramBoolean;
    }
    
    public void setOffsetNotificationsEnabled(boolean paramBoolean)
    {
      int i;
      if (paramBoolean) {
        i = mWindowPrivateFlags | 0x4;
      } else {
        i = mWindowPrivateFlags & 0xFFFFFFFB;
      }
      mWindowPrivateFlags = i;
      if (mCreated) {
        updateSurface(false, false, false);
      }
    }
    
    public void setTouchEventsEnabled(boolean paramBoolean)
    {
      int i;
      if (paramBoolean) {
        i = mWindowFlags & 0xFFFFFFEF;
      } else {
        i = mWindowFlags | 0x10;
      }
      mWindowFlags = i;
      if (mCreated) {
        updateSurface(false, false, false);
      }
    }
    
    /* Error */
    void updateSurface(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 266	android/service/wallpaper/WallpaperService$Engine:mDestroyed	Z
      //   4: ifeq +13 -> 17
      //   7: ldc_w 634
      //   10: ldc_w 701
      //   13: invokestatic 642	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
      //   16: pop
      //   17: iconst_0
      //   18: istore 4
      //   20: aload_0
      //   21: getfield 210	android/service/wallpaper/WallpaperService$Engine:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
      //   24: invokevirtual 704	com/android/internal/view/BaseSurfaceHolder:getRequestedWidth	()I
      //   27: istore 5
      //   29: iload 5
      //   31: ifgt +9 -> 40
      //   34: iconst_m1
      //   35: istore 5
      //   37: goto +6 -> 43
      //   40: iconst_1
      //   41: istore 4
      //   43: aload_0
      //   44: getfield 210	android/service/wallpaper/WallpaperService$Engine:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
      //   47: invokevirtual 707	com/android/internal/view/BaseSurfaceHolder:getRequestedHeight	()I
      //   50: istore 6
      //   52: iload 6
      //   54: ifgt +13 -> 67
      //   57: iconst_m1
      //   58: istore 6
      //   60: iload 4
      //   62: istore 7
      //   64: goto +9 -> 73
      //   67: iconst_1
      //   68: istore 7
      //   70: goto -6 -> 64
      //   73: aload_0
      //   74: getfield 372	android/service/wallpaper/WallpaperService$Engine:mCreated	Z
      //   77: iconst_1
      //   78: ixor
      //   79: istore 8
      //   81: aload_0
      //   82: getfield 484	android/service/wallpaper/WallpaperService$Engine:mSurfaceCreated	Z
      //   85: iconst_1
      //   86: ixor
      //   87: istore 9
      //   89: aload_0
      //   90: getfield 709	android/service/wallpaper/WallpaperService$Engine:mFormat	I
      //   93: aload_0
      //   94: getfield 210	android/service/wallpaper/WallpaperService$Engine:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
      //   97: invokevirtual 712	com/android/internal/view/BaseSurfaceHolder:getRequestedFormat	()I
      //   100: if_icmpeq +9 -> 109
      //   103: iconst_1
      //   104: istore 10
      //   106: goto +6 -> 112
      //   109: iconst_0
      //   110: istore 10
      //   112: aload_0
      //   113: getfield 543	android/service/wallpaper/WallpaperService$Engine:mWidth	I
      //   116: iload 5
      //   118: if_icmpne +21 -> 139
      //   121: aload_0
      //   122: getfield 552	android/service/wallpaper/WallpaperService$Engine:mHeight	I
      //   125: iload 6
      //   127: if_icmpeq +6 -> 133
      //   130: goto +9 -> 139
      //   133: iconst_0
      //   134: istore 4
      //   136: goto +6 -> 142
      //   139: iconst_1
      //   140: istore 4
      //   142: aload_0
      //   143: getfield 372	android/service/wallpaper/WallpaperService$Engine:mCreated	Z
      //   146: istore 11
      //   148: aload_0
      //   149: getfield 560	android/service/wallpaper/WallpaperService$Engine:mType	I
      //   152: aload_0
      //   153: getfield 210	android/service/wallpaper/WallpaperService$Engine:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
      //   156: invokevirtual 715	com/android/internal/view/BaseSurfaceHolder:getRequestedType	()I
      //   159: if_icmpeq +9 -> 168
      //   162: iconst_1
      //   163: istore 12
      //   165: goto +6 -> 171
      //   168: iconst_0
      //   169: istore 12
      //   171: aload_0
      //   172: getfield 145	android/service/wallpaper/WallpaperService$Engine:mCurWindowFlags	I
      //   175: aload_0
      //   176: getfield 141	android/service/wallpaper/WallpaperService$Engine:mWindowFlags	I
      //   179: if_icmpne +23 -> 202
      //   182: aload_0
      //   183: getfield 147	android/service/wallpaper/WallpaperService$Engine:mCurWindowPrivateFlags	I
      //   186: aload_0
      //   187: getfield 143	android/service/wallpaper/WallpaperService$Engine:mWindowPrivateFlags	I
      //   190: if_icmpeq +6 -> 196
      //   193: goto +9 -> 202
      //   196: iconst_0
      //   197: istore 13
      //   199: goto +6 -> 205
      //   202: iconst_1
      //   203: istore 13
      //   205: iload_1
      //   206: ifne +53 -> 259
      //   209: iload 8
      //   211: ifne +48 -> 259
      //   214: iload 9
      //   216: ifne +43 -> 259
      //   219: iload 10
      //   221: ifne +38 -> 259
      //   224: iload 4
      //   226: ifne +33 -> 259
      //   229: iload 12
      //   231: ifne +28 -> 259
      //   234: iload 13
      //   236: ifne +23 -> 259
      //   239: iload_3
      //   240: ifne +19 -> 259
      //   243: aload_0
      //   244: getfield 268	android/service/wallpaper/WallpaperService$Engine:mIWallpaperEngine	Landroid/service/wallpaper/WallpaperService$IWallpaperEngineWrapper;
      //   247: getfield 718	android/service/wallpaper/WallpaperService$IWallpaperEngineWrapper:mShownReported	Z
      //   250: ifne +6 -> 256
      //   253: goto +6 -> 259
      //   256: goto +2068 -> 2324
      //   259: aload_0
      //   260: iload 5
      //   262: putfield 543	android/service/wallpaper/WallpaperService$Engine:mWidth	I
      //   265: aload_0
      //   266: iload 6
      //   268: putfield 552	android/service/wallpaper/WallpaperService$Engine:mHeight	I
      //   271: aload_0
      //   272: aload_0
      //   273: getfield 210	android/service/wallpaper/WallpaperService$Engine:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
      //   276: invokevirtual 712	com/android/internal/view/BaseSurfaceHolder:getRequestedFormat	()I
      //   279: putfield 709	android/service/wallpaper/WallpaperService$Engine:mFormat	I
      //   282: aload_0
      //   283: aload_0
      //   284: getfield 210	android/service/wallpaper/WallpaperService$Engine:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
      //   287: invokevirtual 715	com/android/internal/view/BaseSurfaceHolder:getRequestedType	()I
      //   290: putfield 560	android/service/wallpaper/WallpaperService$Engine:mType	I
      //   293: aload_0
      //   294: getfield 198	android/service/wallpaper/WallpaperService$Engine:mLayout	Landroid/view/WindowManager$LayoutParams;
      //   297: iconst_0
      //   298: putfield 719	android/view/WindowManager$LayoutParams:x	I
      //   301: aload_0
      //   302: getfield 198	android/service/wallpaper/WallpaperService$Engine:mLayout	Landroid/view/WindowManager$LayoutParams;
      //   305: iconst_0
      //   306: putfield 720	android/view/WindowManager$LayoutParams:y	I
      //   309: aload_0
      //   310: getfield 198	android/service/wallpaper/WallpaperService$Engine:mLayout	Landroid/view/WindowManager$LayoutParams;
      //   313: iload 5
      //   315: putfield 723	android/view/WindowManager$LayoutParams:width	I
      //   318: aload_0
      //   319: getfield 198	android/service/wallpaper/WallpaperService$Engine:mLayout	Landroid/view/WindowManager$LayoutParams;
      //   322: iload 6
      //   324: putfield 726	android/view/WindowManager$LayoutParams:height	I
      //   327: aload_0
      //   328: getfield 198	android/service/wallpaper/WallpaperService$Engine:mLayout	Landroid/view/WindowManager$LayoutParams;
      //   331: aload_0
      //   332: getfield 709	android/service/wallpaper/WallpaperService$Engine:mFormat	I
      //   335: putfield 729	android/view/WindowManager$LayoutParams:format	I
      //   338: aload_0
      //   339: aload_0
      //   340: getfield 141	android/service/wallpaper/WallpaperService$Engine:mWindowFlags	I
      //   343: putfield 145	android/service/wallpaper/WallpaperService$Engine:mCurWindowFlags	I
      //   346: aload_0
      //   347: getfield 198	android/service/wallpaper/WallpaperService$Engine:mLayout	Landroid/view/WindowManager$LayoutParams;
      //   350: aload_0
      //   351: getfield 141	android/service/wallpaper/WallpaperService$Engine:mWindowFlags	I
      //   354: sipush 512
      //   357: ior
      //   358: ldc_w 730
      //   361: ior
      //   362: sipush 256
      //   365: ior
      //   366: bipush 8
      //   368: ior
      //   369: putfield 733	android/view/WindowManager$LayoutParams:flags	I
      //   372: aload_0
      //   373: aload_0
      //   374: getfield 143	android/service/wallpaper/WallpaperService$Engine:mWindowPrivateFlags	I
      //   377: putfield 147	android/service/wallpaper/WallpaperService$Engine:mCurWindowPrivateFlags	I
      //   380: aload_0
      //   381: getfield 198	android/service/wallpaper/WallpaperService$Engine:mLayout	Landroid/view/WindowManager$LayoutParams;
      //   384: aload_0
      //   385: getfield 143	android/service/wallpaper/WallpaperService$Engine:mWindowPrivateFlags	I
      //   388: putfield 736	android/view/WindowManager$LayoutParams:privateFlags	I
      //   391: aload_0
      //   392: getfield 198	android/service/wallpaper/WallpaperService$Engine:mLayout	Landroid/view/WindowManager$LayoutParams;
      //   395: aload_0
      //   396: getfield 560	android/service/wallpaper/WallpaperService$Engine:mType	I
      //   399: putfield 739	android/view/WindowManager$LayoutParams:memoryType	I
      //   402: aload_0
      //   403: getfield 198	android/service/wallpaper/WallpaperService$Engine:mLayout	Landroid/view/WindowManager$LayoutParams;
      //   406: aload_0
      //   407: getfield 280	android/service/wallpaper/WallpaperService$Engine:mWindowToken	Landroid/os/IBinder;
      //   410: putfield 742	android/view/WindowManager$LayoutParams:token	Landroid/os/IBinder;
      //   413: aload_0
      //   414: getfield 372	android/service/wallpaper/WallpaperService$Engine:mCreated	Z
      //   417: istore_1
      //   418: iload_1
      //   419: ifne +257 -> 676
      //   422: aload_0
      //   423: getfield 134	android/service/wallpaper/WallpaperService$Engine:this$0	Landroid/service/wallpaper/WallpaperService;
      //   426: getstatic 748	com/android/internal/R$styleable:Window	[I
      //   429: invokevirtual 752	android/service/wallpaper/WallpaperService:obtainStyledAttributes	([I)Landroid/content/res/TypedArray;
      //   432: invokevirtual 755	android/content/res/TypedArray:recycle	()V
      //   435: aload_0
      //   436: getfield 198	android/service/wallpaper/WallpaperService$Engine:mLayout	Landroid/view/WindowManager$LayoutParams;
      //   439: aload_0
      //   440: getfield 268	android/service/wallpaper/WallpaperService$Engine:mIWallpaperEngine	Landroid/service/wallpaper/WallpaperService$IWallpaperEngineWrapper;
      //   443: getfield 758	android/service/wallpaper/WallpaperService$IWallpaperEngineWrapper:mWindowType	I
      //   446: putfield 761	android/view/WindowManager$LayoutParams:type	I
      //   449: aload_0
      //   450: getfield 198	android/service/wallpaper/WallpaperService$Engine:mLayout	Landroid/view/WindowManager$LayoutParams;
      //   453: ldc_w 762
      //   456: putfield 765	android/view/WindowManager$LayoutParams:gravity	I
      //   459: aload_0
      //   460: getfield 198	android/service/wallpaper/WallpaperService$Engine:mLayout	Landroid/view/WindowManager$LayoutParams;
      //   463: aload_0
      //   464: getfield 134	android/service/wallpaper/WallpaperService$Engine:this$0	Landroid/service/wallpaper/WallpaperService;
      //   467: invokevirtual 769	java/lang/Object:getClass	()Ljava/lang/Class;
      //   470: invokevirtual 774	java/lang/Class:getName	()Ljava/lang/String;
      //   473: invokevirtual 778	android/view/WindowManager$LayoutParams:setTitle	(Ljava/lang/CharSequence;)V
      //   476: aload_0
      //   477: getfield 198	android/service/wallpaper/WallpaperService$Engine:mLayout	Landroid/view/WindowManager$LayoutParams;
      //   480: ldc_w 779
      //   483: putfield 782	android/view/WindowManager$LayoutParams:windowAnimations	I
      //   486: new 396	android/view/InputChannel
      //   489: astore 14
      //   491: aload 14
      //   493: invokespecial 783	android/view/InputChannel:<init>	()V
      //   496: aload_0
      //   497: aload 14
      //   499: putfield 394	android/service/wallpaper/WallpaperService$Engine:mInputChannel	Landroid/view/InputChannel;
      //   502: aload_0
      //   503: getfield 293	android/service/wallpaper/WallpaperService$Engine:mSession	Landroid/view/IWindowSession;
      //   506: astore 15
      //   508: aload_0
      //   509: getfield 213	android/service/wallpaper/WallpaperService$Engine:mWindow	Lcom/android/internal/view/BaseIWindow;
      //   512: astore 14
      //   514: aload_0
      //   515: getfield 213	android/service/wallpaper/WallpaperService$Engine:mWindow	Lcom/android/internal/view/BaseIWindow;
      //   518: getfield 786	com/android/internal/view/BaseIWindow:mSeq	I
      //   521: istore 5
      //   523: aload_0
      //   524: getfield 198	android/service/wallpaper/WallpaperService$Engine:mLayout	Landroid/view/WindowManager$LayoutParams;
      //   527: astore 16
      //   529: aload_0
      //   530: getfield 154	android/service/wallpaper/WallpaperService$Engine:mWinFrame	Landroid/graphics/Rect;
      //   533: astore 17
      //   535: aload_0
      //   536: getfield 158	android/service/wallpaper/WallpaperService$Engine:mContentInsets	Landroid/graphics/Rect;
      //   539: astore 18
      //   541: aload_0
      //   542: getfield 160	android/service/wallpaper/WallpaperService$Engine:mStableInsets	Landroid/graphics/Rect;
      //   545: astore 19
      //   547: aload_0
      //   548: getfield 162	android/service/wallpaper/WallpaperService$Engine:mOutsets	Landroid/graphics/Rect;
      //   551: astore 20
      //   553: aload_0
      //   554: getfield 181	android/service/wallpaper/WallpaperService$Engine:mDisplayCutout	Landroid/view/DisplayCutout$ParcelableWrapper;
      //   557: astore 21
      //   559: aload 15
      //   561: aload 14
      //   563: iload 5
      //   565: aload 16
      //   567: iconst_0
      //   568: iconst_0
      //   569: aload 17
      //   571: aload 18
      //   573: aload 19
      //   575: aload 20
      //   577: aload 21
      //   579: aload_0
      //   580: getfield 394	android/service/wallpaper/WallpaperService$Engine:mInputChannel	Landroid/view/InputChannel;
      //   583: invokeinterface 790 12 0
      //   588: ifge +14 -> 602
      //   591: ldc_w 634
      //   594: ldc_w 792
      //   597: invokestatic 642	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
      //   600: pop
      //   601: return
      //   602: aload_0
      //   603: iconst_1
      //   604: putfield 372	android/service/wallpaper/WallpaperService$Engine:mCreated	Z
      //   607: new 18	android/service/wallpaper/WallpaperService$Engine$WallpaperInputEventReceiver
      //   610: astore 14
      //   612: aload 14
      //   614: aload_0
      //   615: aload_0
      //   616: getfield 394	android/service/wallpaper/WallpaperService$Engine:mInputChannel	Landroid/view/InputChannel;
      //   619: invokestatic 798	android/os/Looper:myLooper	()Landroid/os/Looper;
      //   622: invokespecial 801	android/service/wallpaper/WallpaperService$Engine$WallpaperInputEventReceiver:<init>	(Landroid/service/wallpaper/WallpaperService$Engine;Landroid/view/InputChannel;Landroid/os/Looper;)V
      //   625: aload_0
      //   626: aload 14
      //   628: putfield 374	android/service/wallpaper/WallpaperService$Engine:mInputEventReceiver	Landroid/service/wallpaper/WallpaperService$Engine$WallpaperInputEventReceiver;
      //   631: goto +45 -> 676
      //   634: astore 14
      //   636: iload_3
      //   637: istore_1
      //   638: goto +1686 -> 2324
      //   641: astore 14
      //   643: iload_3
      //   644: istore_1
      //   645: goto +1679 -> 2324
      //   648: astore 14
      //   650: iload_3
      //   651: istore_1
      //   652: goto +1672 -> 2324
      //   655: astore 14
      //   657: iload_3
      //   658: istore_1
      //   659: goto +1665 -> 2324
      //   662: astore 14
      //   664: iload_3
      //   665: istore_1
      //   666: goto +1658 -> 2324
      //   669: astore 14
      //   671: iload_3
      //   672: istore_1
      //   673: goto +1651 -> 2324
      //   676: aload_0
      //   677: getfield 210	android/service/wallpaper/WallpaperService$Engine:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
      //   680: getfield 805	com/android/internal/view/BaseSurfaceHolder:mSurfaceLock	Ljava/util/concurrent/locks/ReentrantLock;
      //   683: invokevirtual 810	java/util/concurrent/locks/ReentrantLock:lock	()V
      //   686: aload_0
      //   687: iconst_1
      //   688: putfield 539	android/service/wallpaper/WallpaperService$Engine:mDrawingAllowed	Z
      //   691: iload 7
      //   693: ifne +138 -> 831
      //   696: aload_0
      //   697: getfield 198	android/service/wallpaper/WallpaperService$Engine:mLayout	Landroid/view/WindowManager$LayoutParams;
      //   700: getfield 813	android/view/WindowManager$LayoutParams:surfaceInsets	Landroid/graphics/Rect;
      //   703: aload_0
      //   704: getfield 268	android/service/wallpaper/WallpaperService$Engine:mIWallpaperEngine	Landroid/service/wallpaper/WallpaperService$IWallpaperEngineWrapper;
      //   707: getfield 461	android/service/wallpaper/WallpaperService$IWallpaperEngineWrapper:mDisplayPadding	Landroid/graphics/Rect;
      //   710: invokevirtual 468	android/graphics/Rect:set	(Landroid/graphics/Rect;)V
      //   713: aload_0
      //   714: getfield 198	android/service/wallpaper/WallpaperService$Engine:mLayout	Landroid/view/WindowManager$LayoutParams;
      //   717: getfield 813	android/view/WindowManager$LayoutParams:surfaceInsets	Landroid/graphics/Rect;
      //   720: astore 14
      //   722: aload 14
      //   724: aload 14
      //   726: getfield 816	android/graphics/Rect:left	I
      //   729: aload_0
      //   730: getfield 162	android/service/wallpaper/WallpaperService$Engine:mOutsets	Landroid/graphics/Rect;
      //   733: getfield 816	android/graphics/Rect:left	I
      //   736: iadd
      //   737: putfield 816	android/graphics/Rect:left	I
      //   740: aload_0
      //   741: getfield 198	android/service/wallpaper/WallpaperService$Engine:mLayout	Landroid/view/WindowManager$LayoutParams;
      //   744: getfield 813	android/view/WindowManager$LayoutParams:surfaceInsets	Landroid/graphics/Rect;
      //   747: astore 14
      //   749: aload 14
      //   751: aload 14
      //   753: getfield 819	android/graphics/Rect:top	I
      //   756: aload_0
      //   757: getfield 162	android/service/wallpaper/WallpaperService$Engine:mOutsets	Landroid/graphics/Rect;
      //   760: getfield 819	android/graphics/Rect:top	I
      //   763: iadd
      //   764: putfield 819	android/graphics/Rect:top	I
      //   767: aload_0
      //   768: getfield 198	android/service/wallpaper/WallpaperService$Engine:mLayout	Landroid/view/WindowManager$LayoutParams;
      //   771: getfield 813	android/view/WindowManager$LayoutParams:surfaceInsets	Landroid/graphics/Rect;
      //   774: astore 14
      //   776: aload 14
      //   778: aload 14
      //   780: getfield 822	android/graphics/Rect:right	I
      //   783: aload_0
      //   784: getfield 162	android/service/wallpaper/WallpaperService$Engine:mOutsets	Landroid/graphics/Rect;
      //   787: getfield 822	android/graphics/Rect:right	I
      //   790: iadd
      //   791: putfield 822	android/graphics/Rect:right	I
      //   794: aload_0
      //   795: getfield 198	android/service/wallpaper/WallpaperService$Engine:mLayout	Landroid/view/WindowManager$LayoutParams;
      //   798: getfield 813	android/view/WindowManager$LayoutParams:surfaceInsets	Landroid/graphics/Rect;
      //   801: astore 14
      //   803: aload 14
      //   805: aload 14
      //   807: getfield 825	android/graphics/Rect:bottom	I
      //   810: aload_0
      //   811: getfield 162	android/service/wallpaper/WallpaperService$Engine:mOutsets	Landroid/graphics/Rect;
      //   814: getfield 825	android/graphics/Rect:bottom	I
      //   817: iadd
      //   818: putfield 825	android/graphics/Rect:bottom	I
      //   821: goto +24 -> 845
      //   824: astore 14
      //   826: iload_3
      //   827: istore_1
      //   828: goto +1496 -> 2324
      //   831: aload_0
      //   832: getfield 198	android/service/wallpaper/WallpaperService$Engine:mLayout	Landroid/view/WindowManager$LayoutParams;
      //   835: getfield 813	android/view/WindowManager$LayoutParams:surfaceInsets	Landroid/graphics/Rect;
      //   838: iconst_0
      //   839: iconst_0
      //   840: iconst_0
      //   841: iconst_0
      //   842: invokevirtual 828	android/graphics/Rect:set	(IIII)V
      //   845: aload_0
      //   846: getfield 293	android/service/wallpaper/WallpaperService$Engine:mSession	Landroid/view/IWindowSession;
      //   849: astore 22
      //   851: aload_0
      //   852: getfield 213	android/service/wallpaper/WallpaperService$Engine:mWindow	Lcom/android/internal/view/BaseIWindow;
      //   855: astore 20
      //   857: aload_0
      //   858: getfield 213	android/service/wallpaper/WallpaperService$Engine:mWindow	Lcom/android/internal/view/BaseIWindow;
      //   861: getfield 786	com/android/internal/view/BaseIWindow:mSeq	I
      //   864: istore 6
      //   866: aload_0
      //   867: getfield 198	android/service/wallpaper/WallpaperService$Engine:mLayout	Landroid/view/WindowManager$LayoutParams;
      //   870: astore 23
      //   872: aload_0
      //   873: getfield 543	android/service/wallpaper/WallpaperService$Engine:mWidth	I
      //   876: istore 12
      //   878: aload_0
      //   879: getfield 552	android/service/wallpaper/WallpaperService$Engine:mHeight	I
      //   882: istore 5
      //   884: aload_0
      //   885: getfield 154	android/service/wallpaper/WallpaperService$Engine:mWinFrame	Landroid/graphics/Rect;
      //   888: astore 24
      //   890: aload_0
      //   891: getfield 156	android/service/wallpaper/WallpaperService$Engine:mOverscanInsets	Landroid/graphics/Rect;
      //   894: astore 19
      //   896: aload_0
      //   897: getfield 158	android/service/wallpaper/WallpaperService$Engine:mContentInsets	Landroid/graphics/Rect;
      //   900: astore 18
      //   902: aload_0
      //   903: getfield 152	android/service/wallpaper/WallpaperService$Engine:mVisibleInsets	Landroid/graphics/Rect;
      //   906: astore 15
      //   908: aload_0
      //   909: getfield 160	android/service/wallpaper/WallpaperService$Engine:mStableInsets	Landroid/graphics/Rect;
      //   912: astore 16
      //   914: aload_0
      //   915: getfield 162	android/service/wallpaper/WallpaperService$Engine:mOutsets	Landroid/graphics/Rect;
      //   918: astore 21
      //   920: aload_0
      //   921: getfield 176	android/service/wallpaper/WallpaperService$Engine:mBackdropFrame	Landroid/graphics/Rect;
      //   924: astore 17
      //   926: aload_0
      //   927: getfield 181	android/service/wallpaper/WallpaperService$Engine:mDisplayCutout	Landroid/view/DisplayCutout$ParcelableWrapper;
      //   930: astore 14
      //   932: aload 22
      //   934: aload 20
      //   936: iload 6
      //   938: aload 23
      //   940: iload 12
      //   942: iload 5
      //   944: iconst_0
      //   945: iconst_0
      //   946: ldc2_w 829
      //   949: aload 24
      //   951: aload 19
      //   953: aload 18
      //   955: aload 15
      //   957: aload 16
      //   959: aload 21
      //   961: aload 17
      //   963: aload 14
      //   965: aload_0
      //   966: getfield 193	android/service/wallpaper/WallpaperService$Engine:mMergedConfiguration	Landroid/util/MergedConfiguration;
      //   969: aload_0
      //   970: getfield 210	android/service/wallpaper/WallpaperService$Engine:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
      //   973: getfield 387	com/android/internal/view/BaseSurfaceHolder:mSurface	Landroid/view/Surface;
      //   976: invokeinterface 834 20 0
      //   981: istore 25
      //   983: aload_0
      //   984: getfield 154	android/service/wallpaper/WallpaperService$Engine:mWinFrame	Landroid/graphics/Rect;
      //   987: invokevirtual 836	android/graphics/Rect:width	()I
      //   990: istore 13
      //   992: aload_0
      //   993: getfield 154	android/service/wallpaper/WallpaperService$Engine:mWinFrame	Landroid/graphics/Rect;
      //   996: invokevirtual 838	android/graphics/Rect:height	()I
      //   999: istore 12
      //   1001: iload 13
      //   1003: istore 6
      //   1005: iload 12
      //   1007: istore 5
      //   1009: iload 7
      //   1011: ifne +381 -> 1392
      //   1014: aload_0
      //   1015: getfield 268	android/service/wallpaper/WallpaperService$Engine:mIWallpaperEngine	Landroid/service/wallpaper/WallpaperService$IWallpaperEngineWrapper;
      //   1018: getfield 461	android/service/wallpaper/WallpaperService$IWallpaperEngineWrapper:mDisplayPadding	Landroid/graphics/Rect;
      //   1021: astore 14
      //   1023: iload 13
      //   1025: aload 14
      //   1027: getfield 816	android/graphics/Rect:left	I
      //   1030: aload 14
      //   1032: getfield 822	android/graphics/Rect:right	I
      //   1035: iadd
      //   1036: aload_0
      //   1037: getfield 162	android/service/wallpaper/WallpaperService$Engine:mOutsets	Landroid/graphics/Rect;
      //   1040: getfield 816	android/graphics/Rect:left	I
      //   1043: iadd
      //   1044: aload_0
      //   1045: getfield 162	android/service/wallpaper/WallpaperService$Engine:mOutsets	Landroid/graphics/Rect;
      //   1048: getfield 822	android/graphics/Rect:right	I
      //   1051: iadd
      //   1052: iadd
      //   1053: istore 6
      //   1055: iload 12
      //   1057: aload 14
      //   1059: getfield 819	android/graphics/Rect:top	I
      //   1062: aload 14
      //   1064: getfield 825	android/graphics/Rect:bottom	I
      //   1067: iadd
      //   1068: aload_0
      //   1069: getfield 162	android/service/wallpaper/WallpaperService$Engine:mOutsets	Landroid/graphics/Rect;
      //   1072: getfield 819	android/graphics/Rect:top	I
      //   1075: iadd
      //   1076: aload_0
      //   1077: getfield 162	android/service/wallpaper/WallpaperService$Engine:mOutsets	Landroid/graphics/Rect;
      //   1080: getfield 825	android/graphics/Rect:bottom	I
      //   1083: iadd
      //   1084: iadd
      //   1085: istore 5
      //   1087: aload_0
      //   1088: getfield 156	android/service/wallpaper/WallpaperService$Engine:mOverscanInsets	Landroid/graphics/Rect;
      //   1091: astore 21
      //   1093: aload 21
      //   1095: aload 21
      //   1097: getfield 816	android/graphics/Rect:left	I
      //   1100: aload 14
      //   1102: getfield 816	android/graphics/Rect:left	I
      //   1105: iadd
      //   1106: putfield 816	android/graphics/Rect:left	I
      //   1109: aload_0
      //   1110: getfield 156	android/service/wallpaper/WallpaperService$Engine:mOverscanInsets	Landroid/graphics/Rect;
      //   1113: astore 21
      //   1115: aload 21
      //   1117: aload 21
      //   1119: getfield 819	android/graphics/Rect:top	I
      //   1122: aload 14
      //   1124: getfield 819	android/graphics/Rect:top	I
      //   1127: iadd
      //   1128: putfield 819	android/graphics/Rect:top	I
      //   1131: aload_0
      //   1132: getfield 156	android/service/wallpaper/WallpaperService$Engine:mOverscanInsets	Landroid/graphics/Rect;
      //   1135: astore 21
      //   1137: aload 21
      //   1139: aload 21
      //   1141: getfield 822	android/graphics/Rect:right	I
      //   1144: aload 14
      //   1146: getfield 822	android/graphics/Rect:right	I
      //   1149: iadd
      //   1150: putfield 822	android/graphics/Rect:right	I
      //   1153: aload_0
      //   1154: getfield 156	android/service/wallpaper/WallpaperService$Engine:mOverscanInsets	Landroid/graphics/Rect;
      //   1157: astore 21
      //   1159: aload 21
      //   1161: aload 21
      //   1163: getfield 825	android/graphics/Rect:bottom	I
      //   1166: aload 14
      //   1168: getfield 825	android/graphics/Rect:bottom	I
      //   1171: iadd
      //   1172: putfield 825	android/graphics/Rect:bottom	I
      //   1175: aload_0
      //   1176: getfield 158	android/service/wallpaper/WallpaperService$Engine:mContentInsets	Landroid/graphics/Rect;
      //   1179: astore 21
      //   1181: aload 21
      //   1183: aload 21
      //   1185: getfield 816	android/graphics/Rect:left	I
      //   1188: aload 14
      //   1190: getfield 816	android/graphics/Rect:left	I
      //   1193: iadd
      //   1194: putfield 816	android/graphics/Rect:left	I
      //   1197: aload_0
      //   1198: getfield 158	android/service/wallpaper/WallpaperService$Engine:mContentInsets	Landroid/graphics/Rect;
      //   1201: astore 21
      //   1203: aload 21
      //   1205: aload 21
      //   1207: getfield 819	android/graphics/Rect:top	I
      //   1210: aload 14
      //   1212: getfield 819	android/graphics/Rect:top	I
      //   1215: iadd
      //   1216: putfield 819	android/graphics/Rect:top	I
      //   1219: aload_0
      //   1220: getfield 158	android/service/wallpaper/WallpaperService$Engine:mContentInsets	Landroid/graphics/Rect;
      //   1223: astore 21
      //   1225: aload 21
      //   1227: aload 21
      //   1229: getfield 822	android/graphics/Rect:right	I
      //   1232: aload 14
      //   1234: getfield 822	android/graphics/Rect:right	I
      //   1237: iadd
      //   1238: putfield 822	android/graphics/Rect:right	I
      //   1241: aload_0
      //   1242: getfield 158	android/service/wallpaper/WallpaperService$Engine:mContentInsets	Landroid/graphics/Rect;
      //   1245: astore 21
      //   1247: aload 21
      //   1249: aload 21
      //   1251: getfield 825	android/graphics/Rect:bottom	I
      //   1254: aload 14
      //   1256: getfield 825	android/graphics/Rect:bottom	I
      //   1259: iadd
      //   1260: putfield 825	android/graphics/Rect:bottom	I
      //   1263: aload_0
      //   1264: getfield 160	android/service/wallpaper/WallpaperService$Engine:mStableInsets	Landroid/graphics/Rect;
      //   1267: astore 21
      //   1269: aload 21
      //   1271: aload 21
      //   1273: getfield 816	android/graphics/Rect:left	I
      //   1276: aload 14
      //   1278: getfield 816	android/graphics/Rect:left	I
      //   1281: iadd
      //   1282: putfield 816	android/graphics/Rect:left	I
      //   1285: aload_0
      //   1286: getfield 160	android/service/wallpaper/WallpaperService$Engine:mStableInsets	Landroid/graphics/Rect;
      //   1289: astore 21
      //   1291: aload 21
      //   1293: aload 21
      //   1295: getfield 819	android/graphics/Rect:top	I
      //   1298: aload 14
      //   1300: getfield 819	android/graphics/Rect:top	I
      //   1303: iadd
      //   1304: putfield 819	android/graphics/Rect:top	I
      //   1307: aload_0
      //   1308: getfield 160	android/service/wallpaper/WallpaperService$Engine:mStableInsets	Landroid/graphics/Rect;
      //   1311: astore 21
      //   1313: aload 21
      //   1315: aload 21
      //   1317: getfield 822	android/graphics/Rect:right	I
      //   1320: aload 14
      //   1322: getfield 822	android/graphics/Rect:right	I
      //   1325: iadd
      //   1326: putfield 822	android/graphics/Rect:right	I
      //   1329: aload_0
      //   1330: getfield 160	android/service/wallpaper/WallpaperService$Engine:mStableInsets	Landroid/graphics/Rect;
      //   1333: astore 21
      //   1335: aload 21
      //   1337: aload 21
      //   1339: getfield 825	android/graphics/Rect:bottom	I
      //   1342: aload 14
      //   1344: getfield 825	android/graphics/Rect:bottom	I
      //   1347: iadd
      //   1348: putfield 825	android/graphics/Rect:bottom	I
      //   1351: aload_0
      //   1352: getfield 181	android/service/wallpaper/WallpaperService$Engine:mDisplayCutout	Landroid/view/DisplayCutout$ParcelableWrapper;
      //   1355: aload_0
      //   1356: getfield 181	android/service/wallpaper/WallpaperService$Engine:mDisplayCutout	Landroid/view/DisplayCutout$ParcelableWrapper;
      //   1359: invokevirtual 841	android/view/DisplayCutout$ParcelableWrapper:get	()Landroid/view/DisplayCutout;
      //   1362: aload 14
      //   1364: getfield 816	android/graphics/Rect:left	I
      //   1367: ineg
      //   1368: aload 14
      //   1370: getfield 819	android/graphics/Rect:top	I
      //   1373: ineg
      //   1374: aload 14
      //   1376: getfield 822	android/graphics/Rect:right	I
      //   1379: ineg
      //   1380: aload 14
      //   1382: getfield 825	android/graphics/Rect:bottom	I
      //   1385: ineg
      //   1386: invokevirtual 845	android/view/DisplayCutout:inset	(IIII)Landroid/view/DisplayCutout;
      //   1389: invokevirtual 848	android/view/DisplayCutout$ParcelableWrapper:set	(Landroid/view/DisplayCutout;)V
      //   1392: aload_0
      //   1393: getfield 486	android/service/wallpaper/WallpaperService$Engine:mCurWidth	I
      //   1396: istore 7
      //   1398: iload 7
      //   1400: iload 6
      //   1402: if_icmpeq +20 -> 1422
      //   1405: iconst_1
      //   1406: istore 4
      //   1408: aload_0
      //   1409: iload 6
      //   1411: putfield 486	android/service/wallpaper/WallpaperService$Engine:mCurWidth	I
      //   1414: goto +8 -> 1422
      //   1417: astore 14
      //   1419: goto +868 -> 2287
      //   1422: aload_0
      //   1423: getfield 489	android/service/wallpaper/WallpaperService$Engine:mCurHeight	I
      //   1426: istore 12
      //   1428: iload 4
      //   1430: istore 7
      //   1432: iload 12
      //   1434: iload 5
      //   1436: if_icmpeq +20 -> 1456
      //   1439: aload_0
      //   1440: iload 5
      //   1442: putfield 489	android/service/wallpaper/WallpaperService$Engine:mCurHeight	I
      //   1445: iconst_1
      //   1446: istore 7
      //   1448: goto +8 -> 1456
      //   1451: astore 14
      //   1453: goto +834 -> 2287
      //   1456: aload_0
      //   1457: getfield 164	android/service/wallpaper/WallpaperService$Engine:mDispatchedOverscanInsets	Landroid/graphics/Rect;
      //   1460: aload_0
      //   1461: getfield 156	android/service/wallpaper/WallpaperService$Engine:mOverscanInsets	Landroid/graphics/Rect;
      //   1464: invokevirtual 465	android/graphics/Rect:equals	(Ljava/lang/Object;)Z
      //   1467: istore_1
      //   1468: iload 11
      //   1470: iconst_1
      //   1471: ixor
      //   1472: iload_1
      //   1473: iconst_1
      //   1474: ixor
      //   1475: ior
      //   1476: istore 12
      //   1478: iload 12
      //   1480: istore 4
      //   1482: iload 12
      //   1484: aload_0
      //   1485: getfield 166	android/service/wallpaper/WallpaperService$Engine:mDispatchedContentInsets	Landroid/graphics/Rect;
      //   1488: aload_0
      //   1489: getfield 158	android/service/wallpaper/WallpaperService$Engine:mContentInsets	Landroid/graphics/Rect;
      //   1492: invokevirtual 465	android/graphics/Rect:equals	(Ljava/lang/Object;)Z
      //   1495: iconst_1
      //   1496: ixor
      //   1497: ior
      //   1498: istore 12
      //   1500: iload 12
      //   1502: istore 4
      //   1504: iload 12
      //   1506: aload_0
      //   1507: getfield 168	android/service/wallpaper/WallpaperService$Engine:mDispatchedStableInsets	Landroid/graphics/Rect;
      //   1510: aload_0
      //   1511: getfield 160	android/service/wallpaper/WallpaperService$Engine:mStableInsets	Landroid/graphics/Rect;
      //   1514: invokevirtual 465	android/graphics/Rect:equals	(Ljava/lang/Object;)Z
      //   1517: iconst_1
      //   1518: ixor
      //   1519: ior
      //   1520: istore 12
      //   1522: iload 12
      //   1524: istore 4
      //   1526: iload 12
      //   1528: aload_0
      //   1529: getfield 170	android/service/wallpaper/WallpaperService$Engine:mDispatchedOutsets	Landroid/graphics/Rect;
      //   1532: aload_0
      //   1533: getfield 162	android/service/wallpaper/WallpaperService$Engine:mOutsets	Landroid/graphics/Rect;
      //   1536: invokevirtual 465	android/graphics/Rect:equals	(Ljava/lang/Object;)Z
      //   1539: iconst_1
      //   1540: ixor
      //   1541: ior
      //   1542: istore 12
      //   1544: iload 12
      //   1546: istore 4
      //   1548: aload_0
      //   1549: getfield 188	android/service/wallpaper/WallpaperService$Engine:mDispatchedDisplayCutout	Landroid/view/DisplayCutout;
      //   1552: aload_0
      //   1553: getfield 181	android/service/wallpaper/WallpaperService$Engine:mDisplayCutout	Landroid/view/DisplayCutout$ParcelableWrapper;
      //   1556: invokevirtual 841	android/view/DisplayCutout$ParcelableWrapper:get	()Landroid/view/DisplayCutout;
      //   1559: invokevirtual 849	android/view/DisplayCutout:equals	(Ljava/lang/Object;)Z
      //   1562: istore_1
      //   1563: aload_0
      //   1564: getfield 210	android/service/wallpaper/WallpaperService$Engine:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
      //   1567: iload 6
      //   1569: iload 5
      //   1571: invokevirtual 852	com/android/internal/view/BaseSurfaceHolder:setSurfaceFrameSize	(II)V
      //   1574: aload_0
      //   1575: getfield 210	android/service/wallpaper/WallpaperService$Engine:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
      //   1578: getfield 805	com/android/internal/view/BaseSurfaceHolder:mSurfaceLock	Ljava/util/concurrent/locks/ReentrantLock;
      //   1581: invokevirtual 855	java/util/concurrent/locks/ReentrantLock:unlock	()V
      //   1584: aload_0
      //   1585: getfield 210	android/service/wallpaper/WallpaperService$Engine:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
      //   1588: getfield 387	com/android/internal/view/BaseSurfaceHolder:mSurface	Landroid/view/Surface;
      //   1591: invokevirtual 858	android/view/Surface:isValid	()Z
      //   1594: ifne +8 -> 1602
      //   1597: aload_0
      //   1598: invokevirtual 367	android/service/wallpaper/WallpaperService$Engine:reportSurfaceDestroyed	()V
      //   1601: return
      //   1602: iconst_0
      //   1603: istore 4
      //   1605: aload_0
      //   1606: getfield 210	android/service/wallpaper/WallpaperService$Engine:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
      //   1609: invokevirtual 682	com/android/internal/view/BaseSurfaceHolder:ungetCallbacks	()V
      //   1612: iload 9
      //   1614: ifeq +76 -> 1690
      //   1617: aload_0
      //   1618: iconst_1
      //   1619: putfield 535	android/service/wallpaper/WallpaperService$Engine:mIsCreating	Z
      //   1622: iconst_1
      //   1623: istore 6
      //   1625: aload_0
      //   1626: aload_0
      //   1627: getfield 210	android/service/wallpaper/WallpaperService$Engine:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
      //   1630: invokevirtual 860	android/service/wallpaper/WallpaperService$Engine:onSurfaceCreated	(Landroid/view/SurfaceHolder;)V
      //   1633: aload_0
      //   1634: getfield 210	android/service/wallpaper/WallpaperService$Engine:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
      //   1637: invokevirtual 686	com/android/internal/view/BaseSurfaceHolder:getCallbacks	()[Landroid/view/SurfaceHolder$Callback;
      //   1640: astore 14
      //   1642: iload 6
      //   1644: istore 4
      //   1646: aload 14
      //   1648: ifnull +42 -> 1690
      //   1651: aload 14
      //   1653: arraylength
      //   1654: istore 13
      //   1656: iconst_0
      //   1657: istore 5
      //   1659: iload 6
      //   1661: istore 4
      //   1663: iload 5
      //   1665: iload 13
      //   1667: if_icmpge +23 -> 1690
      //   1670: aload 14
      //   1672: iload 5
      //   1674: aaload
      //   1675: aload_0
      //   1676: getfield 210	android/service/wallpaper/WallpaperService$Engine:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
      //   1679: invokeinterface 863 2 0
      //   1684: iinc 5 1
      //   1687: goto -28 -> 1659
      //   1690: iload 8
      //   1692: ifne +19 -> 1711
      //   1695: iload 25
      //   1697: iconst_2
      //   1698: iand
      //   1699: ifeq +6 -> 1705
      //   1702: goto +9 -> 1711
      //   1705: iconst_0
      //   1706: istore 5
      //   1708: goto +6 -> 1714
      //   1711: iconst_1
      //   1712: istore 5
      //   1714: iload_3
      //   1715: iload 5
      //   1717: ior
      //   1718: istore_3
      //   1719: iload_2
      //   1720: ifne +23 -> 1743
      //   1723: iload 8
      //   1725: ifne +18 -> 1743
      //   1728: iload 9
      //   1730: ifne +13 -> 1743
      //   1733: iload 10
      //   1735: ifne +8 -> 1743
      //   1738: iload 7
      //   1740: ifeq +95 -> 1835
      //   1743: iconst_1
      //   1744: istore 7
      //   1746: aload_0
      //   1747: aload_0
      //   1748: getfield 210	android/service/wallpaper/WallpaperService$Engine:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
      //   1751: aload_0
      //   1752: getfield 709	android/service/wallpaper/WallpaperService$Engine:mFormat	I
      //   1755: aload_0
      //   1756: getfield 486	android/service/wallpaper/WallpaperService$Engine:mCurWidth	I
      //   1759: aload_0
      //   1760: getfield 489	android/service/wallpaper/WallpaperService$Engine:mCurHeight	I
      //   1763: invokevirtual 865	android/service/wallpaper/WallpaperService$Engine:onSurfaceChanged	(Landroid/view/SurfaceHolder;III)V
      //   1766: aload_0
      //   1767: getfield 210	android/service/wallpaper/WallpaperService$Engine:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
      //   1770: invokevirtual 686	com/android/internal/view/BaseSurfaceHolder:getCallbacks	()[Landroid/view/SurfaceHolder$Callback;
      //   1773: astore 14
      //   1775: iload 7
      //   1777: istore 4
      //   1779: aload 14
      //   1781: ifnull +54 -> 1835
      //   1784: aload 14
      //   1786: arraylength
      //   1787: istore 5
      //   1789: iconst_0
      //   1790: istore 10
      //   1792: iload 7
      //   1794: istore 4
      //   1796: iload 10
      //   1798: iload 5
      //   1800: if_icmpge +35 -> 1835
      //   1803: aload 14
      //   1805: iload 10
      //   1807: aaload
      //   1808: aload_0
      //   1809: getfield 210	android/service/wallpaper/WallpaperService$Engine:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
      //   1812: aload_0
      //   1813: getfield 709	android/service/wallpaper/WallpaperService$Engine:mFormat	I
      //   1816: aload_0
      //   1817: getfield 486	android/service/wallpaper/WallpaperService$Engine:mCurWidth	I
      //   1820: aload_0
      //   1821: getfield 489	android/service/wallpaper/WallpaperService$Engine:mCurHeight	I
      //   1824: invokeinterface 868 5 0
      //   1829: iinc 10 1
      //   1832: goto -40 -> 1792
      //   1835: iload 12
      //   1837: iload_1
      //   1838: iconst_1
      //   1839: ixor
      //   1840: ior
      //   1841: ifeq +219 -> 2060
      //   1844: aload_0
      //   1845: getfield 164	android/service/wallpaper/WallpaperService$Engine:mDispatchedOverscanInsets	Landroid/graphics/Rect;
      //   1848: aload_0
      //   1849: getfield 156	android/service/wallpaper/WallpaperService$Engine:mOverscanInsets	Landroid/graphics/Rect;
      //   1852: invokevirtual 468	android/graphics/Rect:set	(Landroid/graphics/Rect;)V
      //   1855: aload_0
      //   1856: getfield 164	android/service/wallpaper/WallpaperService$Engine:mDispatchedOverscanInsets	Landroid/graphics/Rect;
      //   1859: astore 14
      //   1861: aload 14
      //   1863: aload 14
      //   1865: getfield 816	android/graphics/Rect:left	I
      //   1868: aload_0
      //   1869: getfield 162	android/service/wallpaper/WallpaperService$Engine:mOutsets	Landroid/graphics/Rect;
      //   1872: getfield 816	android/graphics/Rect:left	I
      //   1875: iadd
      //   1876: putfield 816	android/graphics/Rect:left	I
      //   1879: aload_0
      //   1880: getfield 164	android/service/wallpaper/WallpaperService$Engine:mDispatchedOverscanInsets	Landroid/graphics/Rect;
      //   1883: astore 14
      //   1885: aload 14
      //   1887: aload 14
      //   1889: getfield 819	android/graphics/Rect:top	I
      //   1892: aload_0
      //   1893: getfield 162	android/service/wallpaper/WallpaperService$Engine:mOutsets	Landroid/graphics/Rect;
      //   1896: getfield 819	android/graphics/Rect:top	I
      //   1899: iadd
      //   1900: putfield 819	android/graphics/Rect:top	I
      //   1903: aload_0
      //   1904: getfield 164	android/service/wallpaper/WallpaperService$Engine:mDispatchedOverscanInsets	Landroid/graphics/Rect;
      //   1907: astore 14
      //   1909: aload 14
      //   1911: aload 14
      //   1913: getfield 822	android/graphics/Rect:right	I
      //   1916: aload_0
      //   1917: getfield 162	android/service/wallpaper/WallpaperService$Engine:mOutsets	Landroid/graphics/Rect;
      //   1920: getfield 822	android/graphics/Rect:right	I
      //   1923: iadd
      //   1924: putfield 822	android/graphics/Rect:right	I
      //   1927: aload_0
      //   1928: getfield 164	android/service/wallpaper/WallpaperService$Engine:mDispatchedOverscanInsets	Landroid/graphics/Rect;
      //   1931: astore 14
      //   1933: aload 14
      //   1935: aload 14
      //   1937: getfield 825	android/graphics/Rect:bottom	I
      //   1940: aload_0
      //   1941: getfield 162	android/service/wallpaper/WallpaperService$Engine:mOutsets	Landroid/graphics/Rect;
      //   1944: getfield 825	android/graphics/Rect:bottom	I
      //   1947: iadd
      //   1948: putfield 825	android/graphics/Rect:bottom	I
      //   1951: aload_0
      //   1952: getfield 166	android/service/wallpaper/WallpaperService$Engine:mDispatchedContentInsets	Landroid/graphics/Rect;
      //   1955: aload_0
      //   1956: getfield 158	android/service/wallpaper/WallpaperService$Engine:mContentInsets	Landroid/graphics/Rect;
      //   1959: invokevirtual 468	android/graphics/Rect:set	(Landroid/graphics/Rect;)V
      //   1962: aload_0
      //   1963: getfield 168	android/service/wallpaper/WallpaperService$Engine:mDispatchedStableInsets	Landroid/graphics/Rect;
      //   1966: aload_0
      //   1967: getfield 160	android/service/wallpaper/WallpaperService$Engine:mStableInsets	Landroid/graphics/Rect;
      //   1970: invokevirtual 468	android/graphics/Rect:set	(Landroid/graphics/Rect;)V
      //   1973: aload_0
      //   1974: getfield 170	android/service/wallpaper/WallpaperService$Engine:mDispatchedOutsets	Landroid/graphics/Rect;
      //   1977: aload_0
      //   1978: getfield 162	android/service/wallpaper/WallpaperService$Engine:mOutsets	Landroid/graphics/Rect;
      //   1981: invokevirtual 468	android/graphics/Rect:set	(Landroid/graphics/Rect;)V
      //   1984: aload_0
      //   1985: aload_0
      //   1986: getfield 181	android/service/wallpaper/WallpaperService$Engine:mDisplayCutout	Landroid/view/DisplayCutout$ParcelableWrapper;
      //   1989: invokevirtual 841	android/view/DisplayCutout$ParcelableWrapper:get	()Landroid/view/DisplayCutout;
      //   1992: putfield 188	android/service/wallpaper/WallpaperService$Engine:mDispatchedDisplayCutout	Landroid/view/DisplayCutout;
      //   1995: aload_0
      //   1996: getfield 172	android/service/wallpaper/WallpaperService$Engine:mFinalSystemInsets	Landroid/graphics/Rect;
      //   1999: aload_0
      //   2000: getfield 164	android/service/wallpaper/WallpaperService$Engine:mDispatchedOverscanInsets	Landroid/graphics/Rect;
      //   2003: invokevirtual 468	android/graphics/Rect:set	(Landroid/graphics/Rect;)V
      //   2006: aload_0
      //   2007: getfield 174	android/service/wallpaper/WallpaperService$Engine:mFinalStableInsets	Landroid/graphics/Rect;
      //   2010: aload_0
      //   2011: getfield 168	android/service/wallpaper/WallpaperService$Engine:mDispatchedStableInsets	Landroid/graphics/Rect;
      //   2014: invokevirtual 468	android/graphics/Rect:set	(Landroid/graphics/Rect;)V
      //   2017: new 870	android/view/WindowInsets
      //   2020: astore 14
      //   2022: aload 14
      //   2024: aload_0
      //   2025: getfield 172	android/service/wallpaper/WallpaperService$Engine:mFinalSystemInsets	Landroid/graphics/Rect;
      //   2028: aconst_null
      //   2029: aload_0
      //   2030: getfield 174	android/service/wallpaper/WallpaperService$Engine:mFinalStableInsets	Landroid/graphics/Rect;
      //   2033: aload_0
      //   2034: getfield 134	android/service/wallpaper/WallpaperService$Engine:this$0	Landroid/service/wallpaper/WallpaperService;
      //   2037: invokevirtual 874	android/service/wallpaper/WallpaperService:getResources	()Landroid/content/res/Resources;
      //   2040: invokevirtual 879	android/content/res/Resources:getConfiguration	()Landroid/content/res/Configuration;
      //   2043: invokevirtual 884	android/content/res/Configuration:isScreenRound	()Z
      //   2046: iconst_0
      //   2047: aload_0
      //   2048: getfield 188	android/service/wallpaper/WallpaperService$Engine:mDispatchedDisplayCutout	Landroid/view/DisplayCutout;
      //   2051: invokespecial 887	android/view/WindowInsets:<init>	(Landroid/graphics/Rect;Landroid/graphics/Rect;Landroid/graphics/Rect;ZZLandroid/view/DisplayCutout;)V
      //   2054: aload_0
      //   2055: aload 14
      //   2057: invokevirtual 889	android/service/wallpaper/WallpaperService$Engine:onApplyWindowInsets	(Landroid/view/WindowInsets;)V
      //   2060: iload_3
      //   2061: ifeq +75 -> 2136
      //   2064: aload_0
      //   2065: aload_0
      //   2066: getfield 210	android/service/wallpaper/WallpaperService$Engine:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
      //   2069: invokevirtual 891	android/service/wallpaper/WallpaperService$Engine:onSurfaceRedrawNeeded	(Landroid/view/SurfaceHolder;)V
      //   2072: aload_0
      //   2073: getfield 210	android/service/wallpaper/WallpaperService$Engine:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
      //   2076: invokevirtual 686	com/android/internal/view/BaseSurfaceHolder:getCallbacks	()[Landroid/view/SurfaceHolder$Callback;
      //   2079: astore 21
      //   2081: aload 21
      //   2083: ifnull +53 -> 2136
      //   2086: aload 21
      //   2088: arraylength
      //   2089: istore 7
      //   2091: iconst_0
      //   2092: istore 10
      //   2094: iload 10
      //   2096: iload 7
      //   2098: if_icmpge +38 -> 2136
      //   2101: aload 21
      //   2103: iload 10
      //   2105: aaload
      //   2106: astore 14
      //   2108: aload 14
      //   2110: instanceof 893
      //   2113: ifeq +17 -> 2130
      //   2116: aload 14
      //   2118: checkcast 893	android/view/SurfaceHolder$Callback2
      //   2121: aload_0
      //   2122: getfield 210	android/service/wallpaper/WallpaperService$Engine:mSurfaceHolder	Lcom/android/internal/view/BaseSurfaceHolder;
      //   2125: invokeinterface 896 2 0
      //   2130: iinc 10 1
      //   2133: goto -39 -> 2094
      //   2136: iload 4
      //   2138: ifeq +27 -> 2165
      //   2141: aload_0
      //   2142: getfield 341	android/service/wallpaper/WallpaperService$Engine:mReportedVisible	Z
      //   2145: ifne +20 -> 2165
      //   2148: aload_0
      //   2149: getfield 535	android/service/wallpaper/WallpaperService$Engine:mIsCreating	Z
      //   2152: ifeq +8 -> 2160
      //   2155: aload_0
      //   2156: iconst_1
      //   2157: invokevirtual 364	android/service/wallpaper/WallpaperService$Engine:onVisibilityChanged	(Z)V
      //   2160: aload_0
      //   2161: iconst_0
      //   2162: invokevirtual 364	android/service/wallpaper/WallpaperService$Engine:onVisibilityChanged	(Z)V
      //   2165: iload_3
      //   2166: istore_1
      //   2167: aload_0
      //   2168: iconst_0
      //   2169: putfield 535	android/service/wallpaper/WallpaperService$Engine:mIsCreating	Z
      //   2172: iload_3
      //   2173: istore_1
      //   2174: aload_0
      //   2175: iconst_1
      //   2176: putfield 484	android/service/wallpaper/WallpaperService$Engine:mSurfaceCreated	Z
      //   2179: iload_3
      //   2180: ifeq +18 -> 2198
      //   2183: iload_3
      //   2184: istore_1
      //   2185: aload_0
      //   2186: getfield 293	android/service/wallpaper/WallpaperService$Engine:mSession	Landroid/view/IWindowSession;
      //   2189: aload_0
      //   2190: getfield 213	android/service/wallpaper/WallpaperService$Engine:mWindow	Lcom/android/internal/view/BaseIWindow;
      //   2193: invokeinterface 899 2 0
      //   2198: iload_3
      //   2199: istore_1
      //   2200: aload_0
      //   2201: getfield 268	android/service/wallpaper/WallpaperService$Engine:mIWallpaperEngine	Landroid/service/wallpaper/WallpaperService$IWallpaperEngineWrapper;
      //   2204: invokevirtual 902	android/service/wallpaper/WallpaperService$IWallpaperEngineWrapper:reportShown	()V
      //   2207: goto +117 -> 2324
      //   2210: astore 14
      //   2212: iload_3
      //   2213: istore_2
      //   2214: goto +7 -> 2221
      //   2217: astore 14
      //   2219: iload_3
      //   2220: istore_2
      //   2221: iload_2
      //   2222: istore_1
      //   2223: aload_0
      //   2224: iconst_0
      //   2225: putfield 535	android/service/wallpaper/WallpaperService$Engine:mIsCreating	Z
      //   2228: iload_2
      //   2229: istore_1
      //   2230: aload_0
      //   2231: iconst_1
      //   2232: putfield 484	android/service/wallpaper/WallpaperService$Engine:mSurfaceCreated	Z
      //   2235: iload_2
      //   2236: ifeq +18 -> 2254
      //   2239: iload_2
      //   2240: istore_1
      //   2241: aload_0
      //   2242: getfield 293	android/service/wallpaper/WallpaperService$Engine:mSession	Landroid/view/IWindowSession;
      //   2245: aload_0
      //   2246: getfield 213	android/service/wallpaper/WallpaperService$Engine:mWindow	Lcom/android/internal/view/BaseIWindow;
      //   2249: invokeinterface 899 2 0
      //   2254: iload_2
      //   2255: istore_1
      //   2256: aload_0
      //   2257: getfield 268	android/service/wallpaper/WallpaperService$Engine:mIWallpaperEngine	Landroid/service/wallpaper/WallpaperService$IWallpaperEngineWrapper;
      //   2260: invokevirtual 902	android/service/wallpaper/WallpaperService$IWallpaperEngineWrapper:reportShown	()V
      //   2263: iload_2
      //   2264: istore_1
      //   2265: aload 14
      //   2267: athrow
      //   2268: astore 14
      //   2270: goto +54 -> 2324
      //   2273: astore 14
      //   2275: goto -1449 -> 826
      //   2278: astore 14
      //   2280: iload_3
      //   2281: istore_1
      //   2282: goto +42 -> 2324
      //   2285: astore 14
      //   2287: iload_3
      //   2288: istore_1
      //   2289: goto +35 -> 2324
      //   2292: astore 14
      //   2294: iload_3
      //   2295: istore_1
      //   2296: goto +28 -> 2324
      //   2299: astore 14
      //   2301: iload_3
      //   2302: istore_1
      //   2303: goto +21 -> 2324
      //   2306: astore 14
      //   2308: iload_3
      //   2309: istore_1
      //   2310: goto +14 -> 2324
      //   2313: astore 14
      //   2315: iload_3
      //   2316: istore_1
      //   2317: goto +7 -> 2324
      //   2320: astore 14
      //   2322: iload_3
      //   2323: istore_1
      //   2324: return
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	2325	0	this	Engine
      //   0	2325	1	paramBoolean1	boolean
      //   0	2325	2	paramBoolean2	boolean
      //   0	2325	3	paramBoolean3	boolean
      //   18	2119	4	i	int
      //   27	1691	5	bool1	boolean
      //   1787	14	5	j	int
      //   50	1610	6	k	int
      //   62	2037	7	m	int
      //   79	1645	8	bool2	boolean
      //   87	1642	9	bool3	boolean
      //   104	2027	10	n	int
      //   146	1326	11	bool4	boolean
      //   163	1678	12	bool5	boolean
      //   197	1471	13	bool6	boolean
      //   489	138	14	localObject1	Object
      //   634	1	14	localRemoteException1	RemoteException
      //   641	1	14	localRemoteException2	RemoteException
      //   648	1	14	localRemoteException3	RemoteException
      //   655	1	14	localRemoteException4	RemoteException
      //   662	1	14	localRemoteException5	RemoteException
      //   669	1	14	localRemoteException6	RemoteException
      //   720	86	14	localRect1	Rect
      //   824	1	14	localRemoteException7	RemoteException
      //   930	451	14	localObject2	Object
      //   1417	1	14	localRemoteException8	RemoteException
      //   1451	1	14	localRemoteException9	RemoteException
      //   1640	477	14	localObject3	Object
      //   2210	1	14	localObject4	Object
      //   2217	49	14	localObject5	Object
      //   2268	1	14	localRemoteException10	RemoteException
      //   2273	1	14	localRemoteException11	RemoteException
      //   2278	1	14	localRemoteException12	RemoteException
      //   2285	1	14	localRemoteException13	RemoteException
      //   2292	1	14	localRemoteException14	RemoteException
      //   2299	1	14	localRemoteException15	RemoteException
      //   2306	1	14	localRemoteException16	RemoteException
      //   2313	1	14	localRemoteException17	RemoteException
      //   2320	1	14	localRemoteException18	RemoteException
      //   506	450	15	localObject6	Object
      //   527	431	16	localObject7	Object
      //   533	429	17	localRect2	Rect
      //   539	415	18	localRect3	Rect
      //   545	407	19	localRect4	Rect
      //   551	384	20	localObject8	Object
      //   557	1545	21	localObject9	Object
      //   849	84	22	localIWindowSession	IWindowSession
      //   870	69	23	localLayoutParams	WindowManager.LayoutParams
      //   888	62	24	localRect5	Rect
      //   981	718	25	i1	int
      // Exception table:
      //   from	to	target	type
      //   547	559	634	android/os/RemoteException
      //   541	547	641	android/os/RemoteException
      //   535	541	648	android/os/RemoteException
      //   529	535	655	android/os/RemoteException
      //   523	529	662	android/os/RemoteException
      //   422	523	669	android/os/RemoteException
      //   559	601	824	android/os/RemoteException
      //   602	631	824	android/os/RemoteException
      //   696	821	824	android/os/RemoteException
      //   1408	1414	1417	android/os/RemoteException
      //   1422	1428	1417	android/os/RemoteException
      //   1456	1468	1417	android/os/RemoteException
      //   1439	1445	1451	android/os/RemoteException
      //   1746	1775	2210	finally
      //   1784	1789	2210	finally
      //   1803	1829	2210	finally
      //   1844	2060	2210	finally
      //   2064	2081	2210	finally
      //   2086	2091	2210	finally
      //   2108	2130	2210	finally
      //   2141	2160	2210	finally
      //   2160	2165	2210	finally
      //   1605	1612	2217	finally
      //   1617	1622	2217	finally
      //   1625	1642	2217	finally
      //   1651	1656	2217	finally
      //   1670	1684	2217	finally
      //   2167	2172	2268	android/os/RemoteException
      //   2174	2179	2268	android/os/RemoteException
      //   2185	2198	2268	android/os/RemoteException
      //   2200	2207	2268	android/os/RemoteException
      //   2223	2228	2268	android/os/RemoteException
      //   2230	2235	2268	android/os/RemoteException
      //   2241	2254	2268	android/os/RemoteException
      //   2256	2263	2268	android/os/RemoteException
      //   2265	2268	2268	android/os/RemoteException
      //   1563	1601	2273	android/os/RemoteException
      //   1482	1500	2278	android/os/RemoteException
      //   1504	1522	2278	android/os/RemoteException
      //   1526	1544	2278	android/os/RemoteException
      //   1548	1563	2278	android/os/RemoteException
      //   932	1001	2285	android/os/RemoteException
      //   1014	1392	2285	android/os/RemoteException
      //   1392	1398	2285	android/os/RemoteException
      //   926	932	2292	android/os/RemoteException
      //   920	926	2299	android/os/RemoteException
      //   914	920	2306	android/os/RemoteException
      //   676	691	2313	android/os/RemoteException
      //   831	845	2313	android/os/RemoteException
      //   845	914	2313	android/os/RemoteException
      //   259	418	2320	android/os/RemoteException
    }
    
    private class WallpaperConnectInfo
      implements IBinder.DeathRecipient
    {
      IBinder mBinder;
      
      WallpaperConnectInfo(IBinder paramIBinder)
      {
        mBinder = paramIBinder;
        try
        {
          mBinder.linkToDeath(this, 0);
        }
        catch (RemoteException this$1)
        {
          binderDied();
        }
      }
      
      public void binderDied()
      {
        Log.w("WallpaperService", "Binder died");
        mIWallpaperEngine.destroy();
      }
      
      void unlinkDeathRecipient()
      {
        mBinder.unlinkToDeath(this, 0);
      }
    }
    
    final class WallpaperInputEventReceiver
      extends InputEventReceiver
    {
      public WallpaperInputEventReceiver(InputChannel paramInputChannel, Looper paramLooper)
      {
        super(paramLooper);
      }
      
      public void onInputEvent(InputEvent paramInputEvent, int paramInt)
      {
        boolean bool1 = false;
        boolean bool2 = bool1;
        try
        {
          if ((paramInputEvent instanceof MotionEvent))
          {
            bool2 = bool1;
            if ((paramInputEvent.getSource() & 0x2) != 0)
            {
              MotionEvent localMotionEvent = MotionEvent.obtainNoHistory((MotionEvent)paramInputEvent);
              WallpaperService.Engine.this.dispatchPointer(localMotionEvent);
              bool2 = true;
            }
          }
          finishInputEvent(paramInputEvent, bool2);
          return;
        }
        finally
        {
          finishInputEvent(paramInputEvent, false);
        }
      }
    }
  }
  
  class IWallpaperEngineWrapper
    extends IWallpaperEngine.Stub
    implements HandlerCaller.Callback
  {
    private final HandlerCaller mCaller = new HandlerCaller(paramWallpaperService, paramWallpaperService.getMainLooper(), this, true);
    final IWallpaperConnection mConnection;
    final Rect mDisplayPadding = new Rect();
    WallpaperService.Engine mEngine;
    final boolean mIsPreview;
    int mReqHeight;
    int mReqWidth;
    boolean mShownReported;
    final IBinder mWindowToken;
    final int mWindowType;
    
    IWallpaperEngineWrapper(WallpaperService paramWallpaperService, IWallpaperConnection paramIWallpaperConnection, IBinder paramIBinder, int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, Rect paramRect)
    {
      mConnection = paramIWallpaperConnection;
      mWindowToken = paramIBinder;
      mWindowType = paramInt1;
      mIsPreview = paramBoolean;
      mReqWidth = paramInt2;
      mReqHeight = paramInt3;
      mDisplayPadding.set(paramRect);
      this$1 = mCaller.obtainMessage(10);
      mCaller.sendMessage(WallpaperService.this);
    }
    
    public void destroy()
    {
      Message localMessage = mCaller.obtainMessage(20);
      mCaller.sendMessage(localMessage);
    }
    
    public void dispatchPointer(MotionEvent paramMotionEvent)
    {
      if (mEngine != null) {
        mEngine.dispatchPointer(paramMotionEvent);
      } else {
        paramMotionEvent.recycle();
      }
    }
    
    public void dispatchWallpaperCommand(String paramString, int paramInt1, int paramInt2, int paramInt3, Bundle paramBundle)
    {
      if (mEngine != null) {
        mEngine.mWindow.dispatchWallpaperCommand(paramString, paramInt1, paramInt2, paramInt3, paramBundle, false);
      }
    }
    
    public void executeMessage(Message arg1)
    {
      int i = what;
      boolean bool1 = false;
      boolean bool2 = false;
      Object localObject;
      WallpaperService.Engine localEngine;
      switch (i)
      {
      default: 
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Unknown message type ");
        ((StringBuilder)localObject).append(what);
        Log.w("WallpaperService", ((StringBuilder)localObject).toString());
        break;
      case 10050: 
        if (mConnection != null) {
          try
          {
            mConnection.onWallpaperColorsChanged(mEngine.onComputeColors());
          }
          catch (RemoteException ???) {}
        }
        break;
      case 10040: 
        i = 0;
        int j = 0;
        localObject = (MotionEvent)obj;
        if (((MotionEvent)localObject).getAction() == 2) {
          synchronized (mEngine.mLock)
          {
            if (mEngine.mPendingMove == localObject)
            {
              mEngine.mPendingMove = null;
              i = j;
            }
            else
            {
              i = 1;
            }
          }
        }
        if (i == 0) {
          mEngine.onTouchEvent(localMotionEvent);
        }
        localMotionEvent.recycle();
        break;
      case 10035: 
        break;
      case 10030: 
        if (arg1 != 0) {
          bool2 = true;
        } else {
          bool2 = false;
        }
        mEngine.mOutsets.set((Rect)obj);
        mEngine.updateSurface(true, false, bool2);
        mEngine.doOffsetsChanged(true);
        break;
      case 10025: 
        ??? = (WallpaperService.WallpaperCommand)obj;
        mEngine.doCommand(???);
        break;
      case 10020: 
        mEngine.doOffsetsChanged(true);
        break;
      case 10010: 
        localEngine = mEngine;
        if (arg1 != 0) {
          bool2 = true;
        }
        localEngine.doVisibilityChanged(bool2);
        break;
      case 10000: 
        mEngine.updateSurface(true, false, false);
        break;
      case 50: 
        localEngine = mEngine;
        if (arg1 != 0) {
          bool2 = true;
        } else {
          bool2 = false;
        }
        if (arg2 != 0) {
          bool1 = true;
        }
        localEngine.doAmbientModeChanged(bool2, bool1);
        return;
      case 40: 
        mEngine.doDisplayPaddingChanged((Rect)obj);
        return;
      case 30: 
        mEngine.doDesiredSizeChanged(arg1, arg2);
        return;
      case 20: 
        mActiveEngines.remove(mEngine);
        mEngine.detach();
        return;
      case 10: 
        try
        {
          mConnection.attachEngine(this);
          ??? = onCreateEngine();
          mEngine = ???;
          mActiveEngines.add(???);
          ???.attach(this);
          return;
        }
        catch (RemoteException ???)
        {
          Log.w("WallpaperService", "Wallpaper host disappeared", ???);
          return;
        }
      }
    }
    
    public void reportShown()
    {
      if (!mShownReported)
      {
        mShownReported = true;
        try
        {
          mConnection.engineShown(this);
        }
        catch (RemoteException localRemoteException)
        {
          Log.w("WallpaperService", "Wallpaper host disappeared", localRemoteException);
          return;
        }
      }
    }
    
    public void requestWallpaperColors()
    {
      Message localMessage = mCaller.obtainMessage(10050);
      mCaller.sendMessage(localMessage);
    }
    
    public void setDesiredSize(int paramInt1, int paramInt2)
    {
      Message localMessage = mCaller.obtainMessageII(30, paramInt1, paramInt2);
      mCaller.sendMessage(localMessage);
    }
    
    public void setDisplayPadding(Rect paramRect)
    {
      paramRect = mCaller.obtainMessageO(40, paramRect);
      mCaller.sendMessage(paramRect);
    }
    
    public void setInAmbientMode(boolean paramBoolean1, boolean paramBoolean2)
      throws RemoteException
    {
      Object localObject = mCaller;
      localObject = ((HandlerCaller)localObject).obtainMessageII(50, paramBoolean1, paramBoolean2);
      mCaller.sendMessage((Message)localObject);
    }
    
    public void setVisibility(boolean paramBoolean)
    {
      Object localObject = mCaller;
      localObject = ((HandlerCaller)localObject).obtainMessageI(10010, paramBoolean);
      mCaller.sendMessage((Message)localObject);
    }
  }
  
  class IWallpaperServiceWrapper
    extends IWallpaperService.Stub
  {
    private final WallpaperService mTarget;
    
    public IWallpaperServiceWrapper(WallpaperService paramWallpaperService)
    {
      mTarget = paramWallpaperService;
    }
    
    public void attach(IWallpaperConnection paramIWallpaperConnection, IBinder paramIBinder, int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, Rect paramRect)
    {
      new WallpaperService.IWallpaperEngineWrapper(WallpaperService.this, mTarget, paramIWallpaperConnection, paramIBinder, paramInt1, paramBoolean, paramInt2, paramInt3, paramRect);
    }
  }
  
  static final class WallpaperCommand
  {
    String action;
    Bundle extras;
    boolean sync;
    int x;
    int y;
    int z;
    
    WallpaperCommand() {}
  }
}
