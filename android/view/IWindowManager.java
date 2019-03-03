package android.view;

import android.app.IAssistDataReceiver;
import android.app.IAssistDataReceiver.Stub;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.GraphicBuffer;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.IRemoteCallback;
import android.os.IRemoteCallback.Stub;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.text.TextUtils;
import com.android.internal.os.IResultReceiver;
import com.android.internal.os.IResultReceiver.Stub;
import com.android.internal.policy.IKeyguardDismissCallback;
import com.android.internal.policy.IKeyguardDismissCallback.Stub;
import com.android.internal.policy.IShortcutService;
import com.android.internal.policy.IShortcutService.Stub;
import com.android.internal.view.IInputContext;
import com.android.internal.view.IInputContext.Stub;
import com.android.internal.view.IInputMethodClient;
import com.android.internal.view.IInputMethodClient.Stub;

public abstract interface IWindowManager
  extends IInterface
{
  public abstract void addWindowToken(IBinder paramIBinder, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract float[] asusGetWallpaperOffset()
    throws RemoteException;
  
  public abstract void clearForcedDisplayDensityForUser(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void clearForcedDisplaySize(int paramInt)
    throws RemoteException;
  
  public abstract boolean clearWindowContentFrameStats(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void closeSystemDialogs(String paramString)
    throws RemoteException;
  
  public abstract void createInputConsumer(IBinder paramIBinder, String paramString, InputChannel paramInputChannel)
    throws RemoteException;
  
  public abstract boolean destroyInputConsumer(String paramString)
    throws RemoteException;
  
  public abstract void disableKeyguard(IBinder paramIBinder, String paramString)
    throws RemoteException;
  
  public abstract void dismissKeyguard(IKeyguardDismissCallback paramIKeyguardDismissCallback, CharSequence paramCharSequence)
    throws RemoteException;
  
  public abstract void dontOverrideDisplayInfo(int paramInt)
    throws RemoteException;
  
  public abstract void enableScreenIfNeeded()
    throws RemoteException;
  
  public abstract void endProlongedAnimations()
    throws RemoteException;
  
  public abstract void executeAppTransition()
    throws RemoteException;
  
  public abstract void exitKeyguardSecurely(IOnKeyguardExitResult paramIOnKeyguardExitResult)
    throws RemoteException;
  
  public abstract void freezeRotation(int paramInt)
    throws RemoteException;
  
  public abstract float getAnimationScale(int paramInt)
    throws RemoteException;
  
  public abstract float[] getAnimationScales()
    throws RemoteException;
  
  public abstract int getBaseDisplayDensity(int paramInt)
    throws RemoteException;
  
  public abstract void getBaseDisplaySize(int paramInt, Point paramPoint)
    throws RemoteException;
  
  public abstract float getCurrentAnimatorScale()
    throws RemoteException;
  
  public abstract Region getCurrentImeTouchRegion()
    throws RemoteException;
  
  public abstract int getDefaultDisplayRotation()
    throws RemoteException;
  
  public abstract Rect getDisplayMagnifiedRect(int paramInt)
    throws RemoteException;
  
  public abstract int getDockedStackSide()
    throws RemoteException;
  
  public abstract int getInitialDisplayDensity(int paramInt)
    throws RemoteException;
  
  public abstract void getInitialDisplaySize(int paramInt, Point paramPoint)
    throws RemoteException;
  
  public abstract int getNavBarPosition()
    throws RemoteException;
  
  public abstract int getPendingAppTransition()
    throws RemoteException;
  
  public abstract int getPreferredOptionsPanelGravity()
    throws RemoteException;
  
  public abstract void getStableInsets(int paramInt, Rect paramRect)
    throws RemoteException;
  
  public abstract WindowContentFrameStats getWindowContentFrameStats(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract boolean hasNavigationBar()
    throws RemoteException;
  
  public abstract boolean inputMethodClientHasFocus(IInputMethodClient paramIInputMethodClient)
    throws RemoteException;
  
  public abstract boolean isKeyguardLocked()
    throws RemoteException;
  
  public abstract boolean isKeyguardSecure()
    throws RemoteException;
  
  public abstract boolean isRotationFrozen()
    throws RemoteException;
  
  public abstract boolean isSafeModeEnabled()
    throws RemoteException;
  
  public abstract boolean isViewServerRunning()
    throws RemoteException;
  
  public abstract boolean isWindowTraceEnabled()
    throws RemoteException;
  
  public abstract void lockNow(Bundle paramBundle)
    throws RemoteException;
  
  public abstract IWindowSession openSession(IWindowSessionCallback paramIWindowSessionCallback, IInputMethodClient paramIInputMethodClient, IInputContext paramIInputContext)
    throws RemoteException;
  
  public abstract void overridePendingAppTransition(String paramString, int paramInt1, int paramInt2, IRemoteCallback paramIRemoteCallback)
    throws RemoteException;
  
  public abstract void overridePendingAppTransitionAspectScaledThumb(GraphicBuffer paramGraphicBuffer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, IRemoteCallback paramIRemoteCallback, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void overridePendingAppTransitionClipReveal(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws RemoteException;
  
  public abstract void overridePendingAppTransitionInPlace(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void overridePendingAppTransitionMultiThumb(AppTransitionAnimationSpec[] paramArrayOfAppTransitionAnimationSpec, IRemoteCallback paramIRemoteCallback1, IRemoteCallback paramIRemoteCallback2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void overridePendingAppTransitionMultiThumbFuture(IAppTransitionAnimationSpecsFuture paramIAppTransitionAnimationSpecsFuture, IRemoteCallback paramIRemoteCallback, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void overridePendingAppTransitionRemote(RemoteAnimationAdapter paramRemoteAnimationAdapter)
    throws RemoteException;
  
  public abstract void overridePendingAppTransitionScaleUp(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    throws RemoteException;
  
  public abstract void overridePendingAppTransitionThumb(GraphicBuffer paramGraphicBuffer, int paramInt1, int paramInt2, IRemoteCallback paramIRemoteCallback, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void prepareAppTransition(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void reenableKeyguard(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void refreshScreenCaptureDisabled(int paramInt)
    throws RemoteException;
  
  public abstract void registerDockedStackListener(IDockedStackListener paramIDockedStackListener)
    throws RemoteException;
  
  public abstract void registerPinnedStackListener(int paramInt, IPinnedStackListener paramIPinnedStackListener)
    throws RemoteException;
  
  public abstract void registerShortcutKey(long paramLong, IShortcutService paramIShortcutService)
    throws RemoteException;
  
  public abstract boolean registerWallpaperVisibilityListener(IWallpaperVisibilityListener paramIWallpaperVisibilityListener, int paramInt)
    throws RemoteException;
  
  public abstract void removeRotationWatcher(IRotationWatcher paramIRotationWatcher)
    throws RemoteException;
  
  public abstract void removeWindowToken(IBinder paramIBinder, int paramInt)
    throws RemoteException;
  
  public abstract void requestAppKeyboardShortcuts(IResultReceiver paramIResultReceiver, int paramInt)
    throws RemoteException;
  
  public abstract boolean requestAssistScreenshot(IAssistDataReceiver paramIAssistDataReceiver)
    throws RemoteException;
  
  public abstract void requestUserActivityNotification()
    throws RemoteException;
  
  public abstract Bitmap screenshotWallpaper()
    throws RemoteException;
  
  public abstract void setAnimationScale(int paramInt, float paramFloat)
    throws RemoteException;
  
  public abstract void setAnimationScales(float[] paramArrayOfFloat)
    throws RemoteException;
  
  public abstract void setDockedStackDividerTouchRegion(Rect paramRect)
    throws RemoteException;
  
  public abstract void setEventDispatching(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setFocusedApp(IBinder paramIBinder, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setForcedDisplayDensityForUser(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void setForcedDisplayScalingMode(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setForcedDisplaySize(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void setInTouchMode(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setNavBarVirtualKeyHapticFeedbackEnabled(boolean paramBoolean)
    throws RemoteException;
  
  public abstract int[] setNewDisplayOverrideConfiguration(Configuration paramConfiguration, int paramInt)
    throws RemoteException;
  
  public abstract void setOverscan(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    throws RemoteException;
  
  public abstract void setPipVisibility(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setRecentsVisibility(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setResizeDimLayer(boolean paramBoolean, int paramInt, float paramFloat)
    throws RemoteException;
  
  public abstract void setShelfHeight(boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void setStrictModeVisualIndicatorPreference(String paramString)
    throws RemoteException;
  
  public abstract void setSwitchingUser(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void showStrictModeViolation(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void startFreezingScreen(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract boolean startViewServer(int paramInt)
    throws RemoteException;
  
  public abstract void startWindowTrace()
    throws RemoteException;
  
  public abstract void statusBarVisibilityChanged(int paramInt)
    throws RemoteException;
  
  public abstract void stopFreezingScreen()
    throws RemoteException;
  
  public abstract boolean stopViewServer()
    throws RemoteException;
  
  public abstract void stopWindowTrace()
    throws RemoteException;
  
  public abstract void thawRotation()
    throws RemoteException;
  
  public abstract void unregisterWallpaperVisibilityListener(IWallpaperVisibilityListener paramIWallpaperVisibilityListener, int paramInt)
    throws RemoteException;
  
  public abstract Configuration updateOrientationFromAppTokens(Configuration paramConfiguration, IBinder paramIBinder, int paramInt)
    throws RemoteException;
  
  public abstract void updateRotation(boolean paramBoolean1, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract int watchRotation(IRotationWatcher paramIRotationWatcher, int paramInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IWindowManager
  {
    private static final String DESCRIPTOR = "android.view.IWindowManager";
    static final int TRANSACTION_addWindowToken = 17;
    static final int TRANSACTION_asusGetWallpaperOffset = 94;
    static final int TRANSACTION_clearForcedDisplayDensityForUser = 13;
    static final int TRANSACTION_clearForcedDisplaySize = 9;
    static final int TRANSACTION_clearWindowContentFrameStats = 76;
    static final int TRANSACTION_closeSystemDialogs = 44;
    static final int TRANSACTION_createInputConsumer = 86;
    static final int TRANSACTION_destroyInputConsumer = 87;
    static final int TRANSACTION_disableKeyguard = 37;
    static final int TRANSACTION_dismissKeyguard = 42;
    static final int TRANSACTION_dontOverrideDisplayInfo = 93;
    static final int TRANSACTION_enableScreenIfNeeded = 75;
    static final int TRANSACTION_endProlongedAnimations = 32;
    static final int TRANSACTION_executeAppTransition = 31;
    static final int TRANSACTION_exitKeyguardSecurely = 39;
    static final int TRANSACTION_freezeRotation = 59;
    static final int TRANSACTION_getAnimationScale = 45;
    static final int TRANSACTION_getAnimationScales = 46;
    static final int TRANSACTION_getBaseDisplayDensity = 11;
    static final int TRANSACTION_getBaseDisplaySize = 7;
    static final int TRANSACTION_getCurrentAnimatorScale = 49;
    static final int TRANSACTION_getCurrentImeTouchRegion = 88;
    static final int TRANSACTION_getDefaultDisplayRotation = 55;
    static final int TRANSACTION_getDisplayMagnifiedRect = 95;
    static final int TRANSACTION_getDockedStackSide = 78;
    static final int TRANSACTION_getInitialDisplayDensity = 10;
    static final int TRANSACTION_getInitialDisplaySize = 6;
    static final int TRANSACTION_getNavBarPosition = 72;
    static final int TRANSACTION_getPendingAppTransition = 21;
    static final int TRANSACTION_getPreferredOptionsPanelGravity = 58;
    static final int TRANSACTION_getStableInsets = 84;
    static final int TRANSACTION_getWindowContentFrameStats = 77;
    static final int TRANSACTION_hasNavigationBar = 71;
    static final int TRANSACTION_inputMethodClientHasFocus = 5;
    static final int TRANSACTION_isKeyguardLocked = 40;
    static final int TRANSACTION_isKeyguardSecure = 41;
    static final int TRANSACTION_isRotationFrozen = 61;
    static final int TRANSACTION_isSafeModeEnabled = 74;
    static final int TRANSACTION_isViewServerRunning = 3;
    static final int TRANSACTION_isWindowTraceEnabled = 91;
    static final int TRANSACTION_lockNow = 73;
    static final int TRANSACTION_openSession = 4;
    static final int TRANSACTION_overridePendingAppTransition = 22;
    static final int TRANSACTION_overridePendingAppTransitionAspectScaledThumb = 26;
    static final int TRANSACTION_overridePendingAppTransitionClipReveal = 24;
    static final int TRANSACTION_overridePendingAppTransitionInPlace = 28;
    static final int TRANSACTION_overridePendingAppTransitionMultiThumb = 27;
    static final int TRANSACTION_overridePendingAppTransitionMultiThumbFuture = 29;
    static final int TRANSACTION_overridePendingAppTransitionRemote = 30;
    static final int TRANSACTION_overridePendingAppTransitionScaleUp = 23;
    static final int TRANSACTION_overridePendingAppTransitionThumb = 25;
    static final int TRANSACTION_prepareAppTransition = 20;
    static final int TRANSACTION_reenableKeyguard = 38;
    static final int TRANSACTION_refreshScreenCaptureDisabled = 53;
    static final int TRANSACTION_registerDockedStackListener = 80;
    static final int TRANSACTION_registerPinnedStackListener = 81;
    static final int TRANSACTION_registerShortcutKey = 85;
    static final int TRANSACTION_registerWallpaperVisibilityListener = 63;
    static final int TRANSACTION_removeRotationWatcher = 57;
    static final int TRANSACTION_removeWindowToken = 18;
    static final int TRANSACTION_requestAppKeyboardShortcuts = 83;
    static final int TRANSACTION_requestAssistScreenshot = 65;
    static final int TRANSACTION_requestUserActivityNotification = 92;
    static final int TRANSACTION_screenshotWallpaper = 62;
    static final int TRANSACTION_setAnimationScale = 47;
    static final int TRANSACTION_setAnimationScales = 48;
    static final int TRANSACTION_setDockedStackDividerTouchRegion = 79;
    static final int TRANSACTION_setEventDispatching = 16;
    static final int TRANSACTION_setFocusedApp = 19;
    static final int TRANSACTION_setForcedDisplayDensityForUser = 12;
    static final int TRANSACTION_setForcedDisplayScalingMode = 14;
    static final int TRANSACTION_setForcedDisplaySize = 8;
    static final int TRANSACTION_setInTouchMode = 50;
    static final int TRANSACTION_setNavBarVirtualKeyHapticFeedbackEnabled = 70;
    static final int TRANSACTION_setNewDisplayOverrideConfiguration = 34;
    static final int TRANSACTION_setOverscan = 15;
    static final int TRANSACTION_setPipVisibility = 68;
    static final int TRANSACTION_setRecentsVisibility = 67;
    static final int TRANSACTION_setResizeDimLayer = 82;
    static final int TRANSACTION_setShelfHeight = 69;
    static final int TRANSACTION_setStrictModeVisualIndicatorPreference = 52;
    static final int TRANSACTION_setSwitchingUser = 43;
    static final int TRANSACTION_showStrictModeViolation = 51;
    static final int TRANSACTION_startFreezingScreen = 35;
    static final int TRANSACTION_startViewServer = 1;
    static final int TRANSACTION_startWindowTrace = 89;
    static final int TRANSACTION_statusBarVisibilityChanged = 66;
    static final int TRANSACTION_stopFreezingScreen = 36;
    static final int TRANSACTION_stopViewServer = 2;
    static final int TRANSACTION_stopWindowTrace = 90;
    static final int TRANSACTION_thawRotation = 60;
    static final int TRANSACTION_unregisterWallpaperVisibilityListener = 64;
    static final int TRANSACTION_updateOrientationFromAppTokens = 33;
    static final int TRANSACTION_updateRotation = 54;
    static final int TRANSACTION_watchRotation = 56;
    
    public Stub()
    {
      attachInterface(this, "android.view.IWindowManager");
    }
    
    public static IWindowManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.view.IWindowManager");
      if ((localIInterface != null) && ((localIInterface instanceof IWindowManager))) {
        return (IWindowManager)localIInterface;
      }
      return new Proxy(paramIBinder);
    }
    
    public IBinder asBinder()
    {
      return this;
    }
    
    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      if (paramInt1 != 1598968902)
      {
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        Object localObject7 = null;
        Object localObject8 = null;
        AppTransitionAnimationSpec[] arrayOfAppTransitionAnimationSpec = null;
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        boolean bool4 = false;
        boolean bool5 = false;
        boolean bool6 = false;
        boolean bool7 = false;
        boolean bool8 = false;
        boolean bool9 = false;
        boolean bool10 = false;
        boolean bool11 = false;
        boolean bool12 = false;
        boolean bool13 = false;
        boolean bool14 = false;
        float f;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 95: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramParcel1 = getDisplayMagnifiedRect(paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 94: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramParcel1 = asusGetWallpaperOffset();
          paramParcel2.writeNoException();
          paramParcel2.writeFloatArray(paramParcel1);
          return true;
        case 93: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          dontOverrideDisplayInfo(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 92: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          requestUserActivityNotification();
          paramParcel2.writeNoException();
          return true;
        case 91: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramInt1 = isWindowTraceEnabled();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 90: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          stopWindowTrace();
          paramParcel2.writeNoException();
          return true;
        case 89: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          startWindowTrace();
          paramParcel2.writeNoException();
          return true;
        case 88: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramParcel1 = getCurrentImeTouchRegion();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 87: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramInt1 = destroyInputConsumer(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 86: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          localObject2 = paramParcel1.readStrongBinder();
          localObject8 = paramParcel1.readString();
          paramParcel1 = new InputChannel();
          createInputConsumer((IBinder)localObject2, (String)localObject8, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(1);
          paramParcel1.writeToParcel(paramParcel2, 1);
          return true;
        case 85: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          registerShortcutKey(paramParcel1.readLong(), IShortcutService.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 84: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramInt1 = paramParcel1.readInt();
          paramParcel1 = new Rect();
          getStableInsets(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(1);
          paramParcel1.writeToParcel(paramParcel2, 1);
          return true;
        case 83: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          requestAppKeyboardShortcuts(IResultReceiver.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 82: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          }
          setResizeDimLayer(bool14, paramParcel1.readInt(), paramParcel1.readFloat());
          paramParcel2.writeNoException();
          return true;
        case 81: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          registerPinnedStackListener(paramParcel1.readInt(), IPinnedStackListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 80: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          registerDockedStackListener(IDockedStackListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 79: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Rect)Rect.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = arrayOfAppTransitionAnimationSpec;
          }
          setDockedStackDividerTouchRegion(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 78: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramInt1 = getDockedStackSide();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 77: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramParcel1 = getWindowContentFrameStats(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 76: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramInt1 = clearWindowContentFrameStats(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 75: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          enableScreenIfNeeded();
          paramParcel2.writeNoException();
          return true;
        case 74: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramInt1 = isSafeModeEnabled();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 73: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          lockNow(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 72: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramInt1 = getNavBarPosition();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 71: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramInt1 = hasNavigationBar();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 70: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          bool14 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          }
          setNavBarVirtualKeyHapticFeedbackEnabled(bool14);
          paramParcel2.writeNoException();
          return true;
        case 69: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          bool14 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          }
          setShelfHeight(bool14, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 68: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          bool14 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          }
          setPipVisibility(bool14);
          return true;
        case 67: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          bool14 = bool4;
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          }
          setRecentsVisibility(bool14);
          return true;
        case 66: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          statusBarVisibilityChanged(paramParcel1.readInt());
          return true;
        case 65: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramInt1 = requestAssistScreenshot(IAssistDataReceiver.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 64: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          unregisterWallpaperVisibilityListener(IWallpaperVisibilityListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 63: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramInt1 = registerWallpaperVisibilityListener(IWallpaperVisibilityListener.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 62: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramParcel1 = screenshotWallpaper();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 61: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramInt1 = isRotationFrozen();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 60: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          thawRotation();
          paramParcel2.writeNoException();
          return true;
        case 59: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          freezeRotation(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 58: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramInt1 = getPreferredOptionsPanelGravity();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 57: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          removeRotationWatcher(IRotationWatcher.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 56: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramInt1 = watchRotation(IRotationWatcher.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 55: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramInt1 = getDefaultDisplayRotation();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 54: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          } else {
            bool14 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          }
          updateRotation(bool14, bool5);
          paramParcel2.writeNoException();
          return true;
        case 53: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          refreshScreenCaptureDisabled(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 52: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          setStrictModeVisualIndicatorPreference(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 51: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          bool14 = bool6;
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          }
          showStrictModeViolation(bool14);
          paramParcel2.writeNoException();
          return true;
        case 50: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          bool14 = bool7;
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          }
          setInTouchMode(bool14);
          paramParcel2.writeNoException();
          return true;
        case 49: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          f = getCurrentAnimatorScale();
          paramParcel2.writeNoException();
          paramParcel2.writeFloat(f);
          return true;
        case 48: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          setAnimationScales(paramParcel1.createFloatArray());
          paramParcel2.writeNoException();
          return true;
        case 47: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          setAnimationScale(paramParcel1.readInt(), paramParcel1.readFloat());
          paramParcel2.writeNoException();
          return true;
        case 46: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramParcel1 = getAnimationScales();
          paramParcel2.writeNoException();
          paramParcel2.writeFloatArray(paramParcel1);
          return true;
        case 45: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          f = getAnimationScale(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeFloat(f);
          return true;
        case 44: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          closeSystemDialogs(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 43: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          bool14 = bool8;
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          }
          setSwitchingUser(bool14);
          paramParcel2.writeNoException();
          return true;
        case 42: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          localObject8 = IKeyguardDismissCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject2;
          }
          dismissKeyguard((IKeyguardDismissCallback)localObject8, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 41: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramInt1 = isKeyguardSecure();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 40: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramInt1 = isKeyguardLocked();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 39: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          exitKeyguardSecurely(IOnKeyguardExitResult.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 38: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          reenableKeyguard(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 37: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          disableKeyguard(paramParcel1.readStrongBinder(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 36: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          stopFreezingScreen();
          paramParcel2.writeNoException();
          return true;
        case 35: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          startFreezingScreen(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 34: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          if (paramParcel1.readInt() != 0) {
            localObject2 = (Configuration)Configuration.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = localObject3;
          }
          paramParcel1 = setNewDisplayOverrideConfiguration((Configuration)localObject2, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 33: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          if (paramParcel1.readInt() != 0) {
            localObject2 = (Configuration)Configuration.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject2 = localObject4;
          }
          paramParcel1 = updateOrientationFromAppTokens((Configuration)localObject2, paramParcel1.readStrongBinder(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 32: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          endProlongedAnimations();
          paramParcel2.writeNoException();
          return true;
        case 31: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          executeAppTransition();
          paramParcel2.writeNoException();
          return true;
        case 30: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (RemoteAnimationAdapter)RemoteAnimationAdapter.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject5;
          }
          overridePendingAppTransitionRemote(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 29: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          localObject2 = IAppTransitionAnimationSpecsFuture.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject8 = IRemoteCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          bool14 = bool9;
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          }
          overridePendingAppTransitionMultiThumbFuture((IAppTransitionAnimationSpecsFuture)localObject2, (IRemoteCallback)localObject8, bool14);
          paramParcel2.writeNoException();
          return true;
        case 28: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          overridePendingAppTransitionInPlace(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 27: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          arrayOfAppTransitionAnimationSpec = (AppTransitionAnimationSpec[])paramParcel1.createTypedArray(AppTransitionAnimationSpec.CREATOR);
          localObject2 = IRemoteCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject8 = IRemoteCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          bool14 = bool10;
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          }
          overridePendingAppTransitionMultiThumb(arrayOfAppTransitionAnimationSpec, (IRemoteCallback)localObject2, (IRemoteCallback)localObject8, bool14);
          paramParcel2.writeNoException();
          return true;
        case 26: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          if (paramParcel1.readInt() != 0) {}
          for (localObject2 = (GraphicBuffer)GraphicBuffer.CREATOR.createFromParcel(paramParcel1);; localObject2 = localObject6) {
            break;
          }
          int i = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          int j = paramParcel1.readInt();
          localObject8 = IRemoteCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          } else {
            bool14 = false;
          }
          overridePendingAppTransitionAspectScaledThumb((GraphicBuffer)localObject2, i, paramInt2, paramInt1, j, (IRemoteCallback)localObject8, bool14);
          paramParcel2.writeNoException();
          return true;
        case 25: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          if (paramParcel1.readInt() != 0) {}
          for (localObject2 = (GraphicBuffer)GraphicBuffer.CREATOR.createFromParcel(paramParcel1);; localObject2 = localObject7) {
            break;
          }
          paramInt1 = paramParcel1.readInt();
          paramInt2 = paramParcel1.readInt();
          localObject8 = IRemoteCallback.Stub.asInterface(paramParcel1.readStrongBinder());
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          } else {
            bool14 = false;
          }
          overridePendingAppTransitionThumb((GraphicBuffer)localObject2, paramInt1, paramInt2, (IRemoteCallback)localObject8, bool14);
          paramParcel2.writeNoException();
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          overridePendingAppTransitionClipReveal(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          overridePendingAppTransitionScaleUp(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          overridePendingAppTransition(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), IRemoteCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramInt1 = getPendingAppTransition();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramInt1 = paramParcel1.readInt();
          bool14 = bool11;
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          }
          prepareAppTransition(paramInt1, bool14);
          paramParcel2.writeNoException();
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          localObject2 = paramParcel1.readStrongBinder();
          bool14 = bool12;
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          }
          setFocusedApp((IBinder)localObject2, bool14);
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          removeWindowToken(paramParcel1.readStrongBinder(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          addWindowToken(paramParcel1.readStrongBinder(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          bool14 = bool13;
          if (paramParcel1.readInt() != 0) {
            bool14 = true;
          }
          setEventDispatching(bool14);
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          setOverscan(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          setForcedDisplayScalingMode(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          clearForcedDisplayDensityForUser(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          setForcedDisplayDensityForUser(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramInt1 = getBaseDisplayDensity(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramInt1 = getInitialDisplayDensity(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          clearForcedDisplaySize(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          setForcedDisplaySize(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramInt1 = paramParcel1.readInt();
          paramParcel1 = new Point();
          getBaseDisplaySize(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(1);
          paramParcel1.writeToParcel(paramParcel2, 1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramInt1 = paramParcel1.readInt();
          paramParcel1 = new Point();
          getInitialDisplaySize(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(1);
          paramParcel1.writeToParcel(paramParcel2, 1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramInt1 = inputMethodClientHasFocus(IInputMethodClient.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          localObject2 = openSession(IWindowSessionCallback.Stub.asInterface(paramParcel1.readStrongBinder()), IInputMethodClient.Stub.asInterface(paramParcel1.readStrongBinder()), IInputContext.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          paramParcel1 = (Parcel)localObject8;
          if (localObject2 != null) {
            paramParcel1 = ((IWindowSession)localObject2).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramInt1 = isViewServerRunning();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.view.IWindowManager");
          paramInt1 = stopViewServer();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.view.IWindowManager");
        paramInt1 = startViewServer(paramParcel1.readInt());
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.view.IWindowManager");
      return true;
    }
    
    private static class Proxy
      implements IWindowManager
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addWindowToken(IBinder paramIBinder, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public float[] asusGetWallpaperOffset()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          mRemote.transact(94, localParcel1, localParcel2, 0);
          localParcel2.readException();
          float[] arrayOfFloat = localParcel2.createFloatArray();
          return arrayOfFloat;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void clearForcedDisplayDensityForUser(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void clearForcedDisplaySize(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean clearWindowContentFrameStats(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeStrongBinder(paramIBinder);
          paramIBinder = mRemote;
          boolean bool = false;
          paramIBinder.transact(76, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void closeSystemDialogs(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeString(paramString);
          mRemote.transact(44, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void createInputConsumer(IBinder paramIBinder, String paramString, InputChannel paramInputChannel)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString);
          mRemote.transact(86, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramInputChannel.readFromParcel(localParcel2);
          }
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean destroyInputConsumer(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(87, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void disableKeyguard(IBinder paramIBinder, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString);
          mRemote.transact(37, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void dismissKeyguard(IKeyguardDismissCallback paramIKeyguardDismissCallback, CharSequence paramCharSequence)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          if (paramIKeyguardDismissCallback != null) {
            paramIKeyguardDismissCallback = paramIKeyguardDismissCallback.asBinder();
          } else {
            paramIKeyguardDismissCallback = null;
          }
          localParcel1.writeStrongBinder(paramIKeyguardDismissCallback);
          if (paramCharSequence != null)
          {
            localParcel1.writeInt(1);
            TextUtils.writeToParcel(paramCharSequence, localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(42, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void dontOverrideDisplayInfo(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(93, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void enableScreenIfNeeded()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          mRemote.transact(75, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void endProlongedAnimations()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          mRemote.transact(32, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void executeAppTransition()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          mRemote.transact(31, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void exitKeyguardSecurely(IOnKeyguardExitResult paramIOnKeyguardExitResult)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          if (paramIOnKeyguardExitResult != null) {
            paramIOnKeyguardExitResult = paramIOnKeyguardExitResult.asBinder();
          } else {
            paramIOnKeyguardExitResult = null;
          }
          localParcel1.writeStrongBinder(paramIOnKeyguardExitResult);
          mRemote.transact(39, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void freezeRotation(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(59, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public float getAnimationScale(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(45, localParcel1, localParcel2, 0);
          localParcel2.readException();
          float f = localParcel2.readFloat();
          return f;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public float[] getAnimationScales()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          mRemote.transact(46, localParcel1, localParcel2, 0);
          localParcel2.readException();
          float[] arrayOfFloat = localParcel2.createFloatArray();
          return arrayOfFloat;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getBaseDisplayDensity(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void getBaseDisplaySize(int paramInt, Point paramPoint)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramPoint.readFromParcel(localParcel2);
          }
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public float getCurrentAnimatorScale()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          mRemote.transact(49, localParcel1, localParcel2, 0);
          localParcel2.readException();
          float f = localParcel2.readFloat();
          return f;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Region getCurrentImeTouchRegion()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          mRemote.transact(88, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Region localRegion;
          if (localParcel2.readInt() != 0) {
            localRegion = (Region)Region.CREATOR.createFromParcel(localParcel2);
          } else {
            localRegion = null;
          }
          return localRegion;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getDefaultDisplayRotation()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          mRemote.transact(55, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Rect getDisplayMagnifiedRect(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(95, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Rect localRect;
          if (localParcel2.readInt() != 0) {
            localRect = (Rect)Rect.CREATOR.createFromParcel(localParcel2);
          } else {
            localRect = null;
          }
          return localRect;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getDockedStackSide()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          mRemote.transact(78, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getInitialDisplayDensity(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void getInitialDisplaySize(int paramInt, Point paramPoint)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramPoint.readFromParcel(localParcel2);
          }
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.view.IWindowManager";
      }
      
      public int getNavBarPosition()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          mRemote.transact(72, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getPendingAppTransition()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getPreferredOptionsPanelGravity()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          mRemote.transact(58, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void getStableInsets(int paramInt, Rect paramRect)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(84, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramRect.readFromParcel(localParcel2);
          }
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public WindowContentFrameStats getWindowContentFrameStats(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(77, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIBinder = (WindowContentFrameStats)WindowContentFrameStats.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIBinder = null;
          }
          return paramIBinder;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean hasNavigationBar()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(71, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean inputMethodClientHasFocus(IInputMethodClient paramIInputMethodClient)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          if (paramIInputMethodClient != null) {
            paramIInputMethodClient = paramIInputMethodClient.asBinder();
          } else {
            paramIInputMethodClient = null;
          }
          localParcel1.writeStrongBinder(paramIInputMethodClient);
          paramIInputMethodClient = mRemote;
          boolean bool = false;
          paramIInputMethodClient.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isKeyguardLocked()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(40, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isKeyguardSecure()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(41, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isRotationFrozen()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(61, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isSafeModeEnabled()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(74, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isViewServerRunning()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isWindowTraceEnabled()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(91, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void lockNow(Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(73, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IWindowSession openSession(IWindowSessionCallback paramIWindowSessionCallback, IInputMethodClient paramIInputMethodClient, IInputContext paramIInputContext)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          Object localObject = null;
          if (paramIWindowSessionCallback != null) {
            paramIWindowSessionCallback = paramIWindowSessionCallback.asBinder();
          } else {
            paramIWindowSessionCallback = null;
          }
          localParcel1.writeStrongBinder(paramIWindowSessionCallback);
          if (paramIInputMethodClient != null) {
            paramIWindowSessionCallback = paramIInputMethodClient.asBinder();
          } else {
            paramIWindowSessionCallback = null;
          }
          localParcel1.writeStrongBinder(paramIWindowSessionCallback);
          paramIWindowSessionCallback = localObject;
          if (paramIInputContext != null) {
            paramIWindowSessionCallback = paramIInputContext.asBinder();
          }
          localParcel1.writeStrongBinder(paramIWindowSessionCallback);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramIWindowSessionCallback = IWindowSession.Stub.asInterface(localParcel2.readStrongBinder());
          return paramIWindowSessionCallback;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void overridePendingAppTransition(String paramString, int paramInt1, int paramInt2, IRemoteCallback paramIRemoteCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          if (paramIRemoteCallback != null) {
            paramString = paramIRemoteCallback.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          mRemote.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void overridePendingAppTransitionAspectScaledThumb(GraphicBuffer paramGraphicBuffer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, IRemoteCallback paramIRemoteCallback, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          if (paramGraphicBuffer != null)
          {
            localParcel1.writeInt(1);
            paramGraphicBuffer.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          if (paramIRemoteCallback != null) {
            paramGraphicBuffer = paramIRemoteCallback.asBinder();
          } else {
            paramGraphicBuffer = null;
          }
          localParcel1.writeStrongBinder(paramGraphicBuffer);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(26, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void overridePendingAppTransitionClipReveal(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          mRemote.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void overridePendingAppTransitionInPlace(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(28, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void overridePendingAppTransitionMultiThumb(AppTransitionAnimationSpec[] paramArrayOfAppTransitionAnimationSpec, IRemoteCallback paramIRemoteCallback1, IRemoteCallback paramIRemoteCallback2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeTypedArray(paramArrayOfAppTransitionAnimationSpec, 0);
          Object localObject = null;
          if (paramIRemoteCallback1 != null) {
            paramArrayOfAppTransitionAnimationSpec = paramIRemoteCallback1.asBinder();
          } else {
            paramArrayOfAppTransitionAnimationSpec = null;
          }
          localParcel1.writeStrongBinder(paramArrayOfAppTransitionAnimationSpec);
          paramArrayOfAppTransitionAnimationSpec = localObject;
          if (paramIRemoteCallback2 != null) {
            paramArrayOfAppTransitionAnimationSpec = paramIRemoteCallback2.asBinder();
          }
          localParcel1.writeStrongBinder(paramArrayOfAppTransitionAnimationSpec);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(27, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void overridePendingAppTransitionMultiThumbFuture(IAppTransitionAnimationSpecsFuture paramIAppTransitionAnimationSpecsFuture, IRemoteCallback paramIRemoteCallback, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          Object localObject = null;
          if (paramIAppTransitionAnimationSpecsFuture != null) {
            paramIAppTransitionAnimationSpecsFuture = paramIAppTransitionAnimationSpecsFuture.asBinder();
          } else {
            paramIAppTransitionAnimationSpecsFuture = null;
          }
          localParcel1.writeStrongBinder(paramIAppTransitionAnimationSpecsFuture);
          paramIAppTransitionAnimationSpecsFuture = localObject;
          if (paramIRemoteCallback != null) {
            paramIAppTransitionAnimationSpecsFuture = paramIRemoteCallback.asBinder();
          }
          localParcel1.writeStrongBinder(paramIAppTransitionAnimationSpecsFuture);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(29, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void overridePendingAppTransitionRemote(RemoteAnimationAdapter paramRemoteAnimationAdapter)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          if (paramRemoteAnimationAdapter != null)
          {
            localParcel1.writeInt(1);
            paramRemoteAnimationAdapter.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(30, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void overridePendingAppTransitionScaleUp(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          mRemote.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void overridePendingAppTransitionThumb(GraphicBuffer paramGraphicBuffer, int paramInt1, int paramInt2, IRemoteCallback paramIRemoteCallback, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          if (paramGraphicBuffer != null)
          {
            localParcel1.writeInt(1);
            paramGraphicBuffer.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          if (paramIRemoteCallback != null) {
            paramGraphicBuffer = paramIRemoteCallback.asBinder();
          } else {
            paramGraphicBuffer = null;
          }
          localParcel1.writeStrongBinder(paramGraphicBuffer);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void prepareAppTransition(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void reenableKeyguard(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(38, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void refreshScreenCaptureDisabled(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(53, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerDockedStackListener(IDockedStackListener paramIDockedStackListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          if (paramIDockedStackListener != null) {
            paramIDockedStackListener = paramIDockedStackListener.asBinder();
          } else {
            paramIDockedStackListener = null;
          }
          localParcel1.writeStrongBinder(paramIDockedStackListener);
          mRemote.transact(80, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerPinnedStackListener(int paramInt, IPinnedStackListener paramIPinnedStackListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramInt);
          if (paramIPinnedStackListener != null) {
            paramIPinnedStackListener = paramIPinnedStackListener.asBinder();
          } else {
            paramIPinnedStackListener = null;
          }
          localParcel1.writeStrongBinder(paramIPinnedStackListener);
          mRemote.transact(81, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerShortcutKey(long paramLong, IShortcutService paramIShortcutService)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeLong(paramLong);
          if (paramIShortcutService != null) {
            paramIShortcutService = paramIShortcutService.asBinder();
          } else {
            paramIShortcutService = null;
          }
          localParcel1.writeStrongBinder(paramIShortcutService);
          mRemote.transact(85, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean registerWallpaperVisibilityListener(IWallpaperVisibilityListener paramIWallpaperVisibilityListener, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          if (paramIWallpaperVisibilityListener != null) {
            paramIWallpaperVisibilityListener = paramIWallpaperVisibilityListener.asBinder();
          } else {
            paramIWallpaperVisibilityListener = null;
          }
          localParcel1.writeStrongBinder(paramIWallpaperVisibilityListener);
          localParcel1.writeInt(paramInt);
          paramIWallpaperVisibilityListener = mRemote;
          boolean bool = false;
          paramIWallpaperVisibilityListener.transact(63, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeRotationWatcher(IRotationWatcher paramIRotationWatcher)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          if (paramIRotationWatcher != null) {
            paramIRotationWatcher = paramIRotationWatcher.asBinder();
          } else {
            paramIRotationWatcher = null;
          }
          localParcel1.writeStrongBinder(paramIRotationWatcher);
          mRemote.transact(57, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeWindowToken(IBinder paramIBinder, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt);
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void requestAppKeyboardShortcuts(IResultReceiver paramIResultReceiver, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          if (paramIResultReceiver != null) {
            paramIResultReceiver = paramIResultReceiver.asBinder();
          } else {
            paramIResultReceiver = null;
          }
          localParcel1.writeStrongBinder(paramIResultReceiver);
          localParcel1.writeInt(paramInt);
          mRemote.transact(83, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean requestAssistScreenshot(IAssistDataReceiver paramIAssistDataReceiver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          if (paramIAssistDataReceiver != null) {
            paramIAssistDataReceiver = paramIAssistDataReceiver.asBinder();
          } else {
            paramIAssistDataReceiver = null;
          }
          localParcel1.writeStrongBinder(paramIAssistDataReceiver);
          paramIAssistDataReceiver = mRemote;
          boolean bool = false;
          paramIAssistDataReceiver.transact(65, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void requestUserActivityNotification()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          mRemote.transact(92, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Bitmap screenshotWallpaper()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          mRemote.transact(62, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Bitmap localBitmap;
          if (localParcel2.readInt() != 0) {
            localBitmap = (Bitmap)Bitmap.CREATOR.createFromParcel(localParcel2);
          } else {
            localBitmap = null;
          }
          return localBitmap;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setAnimationScale(int paramInt, float paramFloat)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeFloat(paramFloat);
          mRemote.transact(47, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setAnimationScales(float[] paramArrayOfFloat)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeFloatArray(paramArrayOfFloat);
          mRemote.transact(48, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setDockedStackDividerTouchRegion(Rect paramRect)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          if (paramRect != null)
          {
            localParcel1.writeInt(1);
            paramRect.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(79, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setEventDispatching(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setFocusedApp(IBinder paramIBinder, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setForcedDisplayDensityForUser(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setForcedDisplayScalingMode(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setForcedDisplaySize(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setInTouchMode(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(50, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setNavBarVirtualKeyHapticFeedbackEnabled(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(70, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int[] setNewDisplayOverrideConfiguration(Configuration paramConfiguration, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          if (paramConfiguration != null)
          {
            localParcel1.writeInt(1);
            paramConfiguration.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(34, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramConfiguration = localParcel2.createIntArray();
          return paramConfiguration;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setOverscan(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          localParcel1.writeInt(paramInt5);
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setPipVisibility(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IWindowManager");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(68, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setRecentsVisibility(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IWindowManager");
          localParcel.writeInt(paramBoolean);
          mRemote.transact(67, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void setResizeDimLayer(boolean paramBoolean, int paramInt, float paramFloat)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
          localParcel1.writeFloat(paramFloat);
          mRemote.transact(82, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setShelfHeight(boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
          mRemote.transact(69, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setStrictModeVisualIndicatorPreference(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeString(paramString);
          mRemote.transact(52, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setSwitchingUser(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(43, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void showStrictModeViolation(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(51, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startFreezingScreen(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(35, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean startViewServer(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startWindowTrace()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          mRemote.transact(89, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void statusBarVisibilityChanged(int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.view.IWindowManager");
          localParcel.writeInt(paramInt);
          mRemote.transact(66, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void stopFreezingScreen()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          mRemote.transact(36, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean stopViewServer()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void stopWindowTrace()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          mRemote.transact(90, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void thawRotation()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          mRemote.transact(60, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unregisterWallpaperVisibilityListener(IWallpaperVisibilityListener paramIWallpaperVisibilityListener, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          if (paramIWallpaperVisibilityListener != null) {
            paramIWallpaperVisibilityListener = paramIWallpaperVisibilityListener.asBinder();
          } else {
            paramIWallpaperVisibilityListener = null;
          }
          localParcel1.writeStrongBinder(paramIWallpaperVisibilityListener);
          localParcel1.writeInt(paramInt);
          mRemote.transact(64, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Configuration updateOrientationFromAppTokens(Configuration paramConfiguration, IBinder paramIBinder, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          if (paramConfiguration != null)
          {
            localParcel1.writeInt(1);
            paramConfiguration.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt);
          mRemote.transact(33, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramConfiguration = (Configuration)Configuration.CREATOR.createFromParcel(localParcel2);
          } else {
            paramConfiguration = null;
          }
          return paramConfiguration;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void updateRotation(boolean paramBoolean1, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeInt(paramBoolean2);
          mRemote.transact(54, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int watchRotation(IRotationWatcher paramIRotationWatcher, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.view.IWindowManager");
          if (paramIRotationWatcher != null) {
            paramIRotationWatcher = paramIRotationWatcher.asBinder();
          } else {
            paramIRotationWatcher = null;
          }
          localParcel1.writeStrongBinder(paramIRotationWatcher);
          localParcel1.writeInt(paramInt);
          mRemote.transact(56, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
    }
  }
}
