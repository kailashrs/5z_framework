package android.view;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.accessibility.AccessibilityEvent;
import com.android.internal.R.styleable;
import java.util.List;

public abstract class Window
{
  public static final int DECOR_CAPTION_SHADE_AUTO = 0;
  public static final int DECOR_CAPTION_SHADE_DARK = 2;
  public static final int DECOR_CAPTION_SHADE_LIGHT = 1;
  @Deprecated
  protected static final int DEFAULT_FEATURES = 65;
  public static final int FEATURE_ACTION_BAR = 8;
  public static final int FEATURE_ACTION_BAR_OVERLAY = 9;
  public static final int FEATURE_ACTION_MODE_OVERLAY = 10;
  public static final int FEATURE_ACTIVITY_TRANSITIONS = 13;
  public static final int FEATURE_CONTENT_TRANSITIONS = 12;
  public static final int FEATURE_CONTEXT_MENU = 6;
  public static final int FEATURE_CUSTOM_TITLE = 7;
  @Deprecated
  public static final int FEATURE_INDETERMINATE_PROGRESS = 5;
  public static final int FEATURE_LEFT_ICON = 3;
  public static final int FEATURE_MAX = 13;
  public static final int FEATURE_NO_TITLE = 1;
  public static final int FEATURE_OPTIONS_PANEL = 0;
  @Deprecated
  public static final int FEATURE_PROGRESS = 2;
  public static final int FEATURE_RIGHT_ICON = 4;
  public static final int FEATURE_SWIPE_TO_DISMISS = 11;
  public static final int ID_ANDROID_CONTENT = 16908290;
  public static final String NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME = "android:navigation:background";
  @Deprecated
  public static final int PROGRESS_END = 10000;
  @Deprecated
  public static final int PROGRESS_INDETERMINATE_OFF = -4;
  @Deprecated
  public static final int PROGRESS_INDETERMINATE_ON = -3;
  @Deprecated
  public static final int PROGRESS_SECONDARY_END = 30000;
  @Deprecated
  public static final int PROGRESS_SECONDARY_START = 20000;
  @Deprecated
  public static final int PROGRESS_START = 0;
  @Deprecated
  public static final int PROGRESS_VISIBILITY_OFF = -2;
  @Deprecated
  public static final int PROGRESS_VISIBILITY_ON = -1;
  private static final String PROPERTY_HARDWARE_UI = "persist.sys.ui.hw";
  public static final String STATUS_BAR_BACKGROUND_TRANSITION_NAME = "android:status:background";
  private Window mActiveChild;
  private String mAppName;
  private IBinder mAppToken;
  private Callback mCallback;
  private boolean mCloseOnSwipeEnabled = false;
  private boolean mCloseOnTouchOutside = false;
  private Window mContainer;
  private final Context mContext;
  private int mDefaultWindowFormat = -1;
  private boolean mDestroyed;
  private int mFeatures;
  private int mForcedWindowFlags = 0;
  private boolean mHardwareAccelerated;
  private boolean mHasChildren = false;
  private boolean mHasSoftInputMode = false;
  private boolean mHaveDimAmount = false;
  private boolean mHaveWindowFormat = false;
  private boolean mIsActive = false;
  private int mLocalFeatures;
  private OnRestrictedCaptionAreaChangedListener mOnRestrictedCaptionAreaChangedListener;
  private OnWindowDismissedCallback mOnWindowDismissedCallback;
  private OnWindowSwipeDismissedCallback mOnWindowSwipeDismissedCallback;
  private boolean mOverlayWithDecorCaptionEnabled = false;
  private Rect mRestrictedCaptionAreaRect;
  private boolean mSetCloseOnTouchOutside = false;
  private final WindowManager.LayoutParams mWindowAttributes = new WindowManager.LayoutParams();
  private WindowControllerCallback mWindowControllerCallback;
  private WindowManager mWindowManager;
  private TypedArray mWindowStyle;
  
  public Window(Context paramContext)
  {
    mContext = paramContext;
    int i = getDefaultFeatures(paramContext);
    mLocalFeatures = i;
    mFeatures = i;
  }
  
  public static int getDefaultFeatures(Context paramContext)
  {
    int i = 0;
    paramContext = paramContext.getResources();
    if (paramContext.getBoolean(17956923)) {
      i = 0x0 | 0x1;
    }
    int j = i;
    if (paramContext.getBoolean(17956922)) {
      j = i | 0x40;
    }
    return j;
  }
  
  private boolean isOutOfBounds(Context paramContext, MotionEvent paramMotionEvent)
  {
    int i = (int)paramMotionEvent.getX();
    int j = (int)paramMotionEvent.getY();
    int k = ViewConfiguration.get(paramContext).getScaledWindowTouchSlop();
    paramContext = getDecorView();
    boolean bool;
    if ((i >= -k) && (j >= -k) && (i <= paramContext.getWidth() + k) && (j <= paramContext.getHeight() + k)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private void setPrivateFlags(int paramInt1, int paramInt2)
  {
    WindowManager.LayoutParams localLayoutParams = getAttributes();
    privateFlags = (privateFlags & paramInt2 | paramInt1 & paramInt2);
    dispatchWindowAttributesChanged(localLayoutParams);
  }
  
  public abstract void addContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams);
  
  public void addFlags(int paramInt)
  {
    setFlags(paramInt, paramInt);
  }
  
  public final void addOnFrameMetricsAvailableListener(OnFrameMetricsAvailableListener paramOnFrameMetricsAvailableListener, Handler paramHandler)
  {
    View localView = getDecorView();
    if (localView != null)
    {
      if (paramOnFrameMetricsAvailableListener != null)
      {
        localView.addFrameMetricsListener(this, paramOnFrameMetricsAvailableListener, paramHandler);
        return;
      }
      throw new NullPointerException("listener cannot be null");
    }
    throw new IllegalStateException("can't observe a Window without an attached view");
  }
  
  public void addPrivateFlags(int paramInt)
  {
    setPrivateFlags(paramInt, paramInt);
  }
  
  void adjustLayoutParamsForSubWindow(WindowManager.LayoutParams paramLayoutParams)
  {
    CharSequence localCharSequence = paramLayoutParams.getTitle();
    Object localObject;
    if ((type >= 1000) && (type <= 1999))
    {
      if (token == null)
      {
        localObject = peekDecorView();
        if (localObject != null) {
          token = ((View)localObject).getWindowToken();
        }
      }
      if ((localCharSequence == null) || (localCharSequence.length() == 0))
      {
        localObject = new StringBuilder(32);
        if (type == 1001) {
          ((StringBuilder)localObject).append("Media");
        } else if (type == 1004) {
          ((StringBuilder)localObject).append("MediaOvr");
        } else if (type == 1000) {
          ((StringBuilder)localObject).append("Panel");
        } else if (type == 1002) {
          ((StringBuilder)localObject).append("SubPanel");
        } else if (type == 1005) {
          ((StringBuilder)localObject).append("AboveSubPanel");
        } else if (type == 1003) {
          ((StringBuilder)localObject).append("AtchDlg");
        } else {
          ((StringBuilder)localObject).append(type);
        }
        if (mAppName != null)
        {
          ((StringBuilder)localObject).append(":");
          ((StringBuilder)localObject).append(mAppName);
        }
        paramLayoutParams.setTitle((CharSequence)localObject);
      }
    }
    else if ((type >= 2000) && (type <= 2999))
    {
      if ((localCharSequence == null) || (localCharSequence.length() == 0))
      {
        localObject = new StringBuilder(32);
        ((StringBuilder)localObject).append("Sys");
        ((StringBuilder)localObject).append(type);
        if (mAppName != null)
        {
          ((StringBuilder)localObject).append(":");
          ((StringBuilder)localObject).append(mAppName);
        }
        paramLayoutParams.setTitle((CharSequence)localObject);
      }
    }
    else
    {
      if (token == null)
      {
        if (mContainer == null) {
          localObject = mAppToken;
        } else {
          localObject = mContainer.mAppToken;
        }
        token = ((IBinder)localObject);
      }
      if (((localCharSequence == null) || (localCharSequence.length() == 0)) && (mAppName != null)) {
        paramLayoutParams.setTitle(mAppName);
      }
    }
    if (packageName == null) {
      packageName = mContext.getPackageName();
    }
    if ((mHardwareAccelerated) || ((mWindowAttributes.flags & 0x1000000) != 0)) {
      flags |= 0x1000000;
    }
  }
  
  public abstract void alwaysReadCloseOnTouchAttr();
  
  public abstract void clearContentView();
  
  public void clearFlags(int paramInt)
  {
    setFlags(0, paramInt);
  }
  
  public abstract void closeAllPanels();
  
  public abstract void closePanel(int paramInt);
  
  public final void destroy()
  {
    mDestroyed = true;
  }
  
  public final void dispatchOnWindowDismissed(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (mOnWindowDismissedCallback != null) {
      mOnWindowDismissedCallback.onWindowDismissed(paramBoolean1, paramBoolean2);
    }
  }
  
  public final void dispatchOnWindowSwipeDismissed()
  {
    if (mOnWindowSwipeDismissedCallback != null) {
      mOnWindowSwipeDismissedCallback.onWindowSwipeDismissed();
    }
  }
  
  protected void dispatchWindowAttributesChanged(WindowManager.LayoutParams paramLayoutParams)
  {
    if (mCallback != null) {
      mCallback.onWindowAttributesChanged(paramLayoutParams);
    }
  }
  
  public <T extends View> T findViewById(int paramInt)
  {
    return getDecorView().findViewById(paramInt);
  }
  
  public boolean getAllowEnterTransitionOverlap()
  {
    return true;
  }
  
  public boolean getAllowReturnTransitionOverlap()
  {
    return true;
  }
  
  public final WindowManager.LayoutParams getAttributes()
  {
    return mWindowAttributes;
  }
  
  public final Callback getCallback()
  {
    return mCallback;
  }
  
  public int getColorMode()
  {
    return getAttributes().getColorMode();
  }
  
  public final Window getContainer()
  {
    return mContainer;
  }
  
  public Scene getContentScene()
  {
    return null;
  }
  
  public final Context getContext()
  {
    return mContext;
  }
  
  public abstract View getCurrentFocus();
  
  public abstract View getDecorView();
  
  public float getElevation()
  {
    return 0.0F;
  }
  
  public Transition getEnterTransition()
  {
    return null;
  }
  
  public Transition getExitTransition()
  {
    return null;
  }
  
  protected final int getFeatures()
  {
    return mFeatures;
  }
  
  protected final int getForcedWindowFlags()
  {
    return mForcedWindowFlags;
  }
  
  public abstract LayoutInflater getLayoutInflater();
  
  protected final int getLocalFeatures()
  {
    return mLocalFeatures;
  }
  
  public MediaController getMediaController()
  {
    return null;
  }
  
  public abstract int getNavigationBarColor();
  
  public int getNavigationBarDividerColor()
  {
    return 0;
  }
  
  public Transition getReenterTransition()
  {
    return null;
  }
  
  public Transition getReturnTransition()
  {
    return null;
  }
  
  public Transition getSharedElementEnterTransition()
  {
    return null;
  }
  
  public Transition getSharedElementExitTransition()
  {
    return null;
  }
  
  public Transition getSharedElementReenterTransition()
  {
    return null;
  }
  
  public Transition getSharedElementReturnTransition()
  {
    return null;
  }
  
  public boolean getSharedElementsUseOverlay()
  {
    return true;
  }
  
  public abstract int getStatusBarColor();
  
  public long getTransitionBackgroundFadeDuration()
  {
    return 0L;
  }
  
  public TransitionManager getTransitionManager()
  {
    return null;
  }
  
  public abstract int getVolumeControlStream();
  
  public final WindowControllerCallback getWindowControllerCallback()
  {
    return mWindowControllerCallback;
  }
  
  public WindowManager getWindowManager()
  {
    return mWindowManager;
  }
  
  public final TypedArray getWindowStyle()
  {
    try
    {
      if (mWindowStyle == null) {
        mWindowStyle = mContext.obtainStyledAttributes(R.styleable.Window);
      }
      TypedArray localTypedArray = mWindowStyle;
      return localTypedArray;
    }
    finally {}
  }
  
  public final boolean hasChildren()
  {
    return mHasChildren;
  }
  
  public boolean hasFeature(int paramInt)
  {
    int i = getFeatures();
    boolean bool = true;
    if ((i & 1 << paramInt) == 0) {
      bool = false;
    }
    return bool;
  }
  
  protected final boolean hasSoftInputMode()
  {
    return mHasSoftInputMode;
  }
  
  protected boolean haveDimAmount()
  {
    return mHaveDimAmount;
  }
  
  public void injectInputEvent(InputEvent paramInputEvent) {}
  
  public abstract void invalidatePanelMenu(int paramInt);
  
  public final boolean isActive()
  {
    return mIsActive;
  }
  
  public boolean isCloseOnSwipeEnabled()
  {
    return mCloseOnSwipeEnabled;
  }
  
  public final boolean isDestroyed()
  {
    return mDestroyed;
  }
  
  public abstract boolean isFloating();
  
  public boolean isOverlayWithDecorCaptionEnabled()
  {
    return mOverlayWithDecorCaptionEnabled;
  }
  
  public abstract boolean isShortcutKey(int paramInt, KeyEvent paramKeyEvent);
  
  public boolean isWideColorGamut()
  {
    int i = getColorMode();
    boolean bool = true;
    if ((i != 1) || (!getContext().getResources().getConfiguration().isScreenWideColorGamut())) {
      bool = false;
    }
    return bool;
  }
  
  public final void makeActive()
  {
    if (mContainer != null)
    {
      if (mContainer.mActiveChild != null) {
        mContainer.mActiveChild.mIsActive = false;
      }
      mContainer.mActiveChild = this;
    }
    mIsActive = true;
    onActive();
  }
  
  public void notifyRestrictedCaptionAreaCallback(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (mOnRestrictedCaptionAreaChangedListener != null)
    {
      mRestrictedCaptionAreaRect.set(paramInt1, paramInt2, paramInt3, paramInt4);
      mOnRestrictedCaptionAreaChangedListener.onRestrictedCaptionAreaChanged(mRestrictedCaptionAreaRect);
    }
  }
  
  protected abstract void onActive();
  
  public abstract void onConfigurationChanged(Configuration paramConfiguration);
  
  public abstract void onMultiWindowModeChanged();
  
  public abstract void onPictureInPictureModeChanged(boolean paramBoolean);
  
  public abstract void openPanel(int paramInt, KeyEvent paramKeyEvent);
  
  public abstract View peekDecorView();
  
  public abstract boolean performContextMenuIdentifierAction(int paramInt1, int paramInt2);
  
  public abstract boolean performPanelIdentifierAction(int paramInt1, int paramInt2, int paramInt3);
  
  public abstract boolean performPanelShortcut(int paramInt1, int paramInt2, KeyEvent paramKeyEvent, int paramInt3);
  
  protected void removeFeature(int paramInt)
  {
    paramInt = 1 << paramInt;
    mFeatures &= paramInt;
    int i = mLocalFeatures;
    if (mContainer != null) {
      paramInt = mContainer.mFeatures & paramInt;
    }
    mLocalFeatures = (i & paramInt);
  }
  
  public final void removeOnFrameMetricsAvailableListener(OnFrameMetricsAvailableListener paramOnFrameMetricsAvailableListener)
  {
    if (getDecorView() != null) {
      getDecorView().removeFrameMetricsListener(paramOnFrameMetricsAvailableListener);
    }
  }
  
  public abstract void reportActivityRelaunched();
  
  public boolean requestFeature(int paramInt)
  {
    boolean bool = true;
    int i = 1 << paramInt;
    mFeatures |= i;
    int j = mLocalFeatures;
    if (mContainer != null) {
      paramInt = mContainer.mFeatures & i;
    } else {
      paramInt = i;
    }
    mLocalFeatures = (j | paramInt);
    if ((mFeatures & i) == 0) {
      bool = false;
    }
    return bool;
  }
  
  public final <T extends View> T requireViewById(int paramInt)
  {
    View localView = findViewById(paramInt);
    if (localView != null) {
      return localView;
    }
    throw new IllegalArgumentException("ID does not reference a View inside this Window");
  }
  
  public abstract void restoreHierarchyState(Bundle paramBundle);
  
  public abstract Bundle saveHierarchyState();
  
  public void setAllowEnterTransitionOverlap(boolean paramBoolean) {}
  
  public void setAllowReturnTransitionOverlap(boolean paramBoolean) {}
  
  public void setAttributes(WindowManager.LayoutParams paramLayoutParams)
  {
    mWindowAttributes.copyFrom(paramLayoutParams);
    dispatchWindowAttributesChanged(mWindowAttributes);
  }
  
  public abstract void setBackgroundDrawable(Drawable paramDrawable);
  
  public void setBackgroundDrawableResource(int paramInt)
  {
    setBackgroundDrawable(mContext.getDrawable(paramInt));
  }
  
  public void setCallback(Callback paramCallback)
  {
    mCallback = paramCallback;
  }
  
  public abstract void setChildDrawable(int paramInt, Drawable paramDrawable);
  
  public abstract void setChildInt(int paramInt1, int paramInt2);
  
  public void setClipToOutline(boolean paramBoolean) {}
  
  public void setCloseOnSwipeEnabled(boolean paramBoolean)
  {
    mCloseOnSwipeEnabled = paramBoolean;
  }
  
  public void setCloseOnTouchOutside(boolean paramBoolean)
  {
    mCloseOnTouchOutside = paramBoolean;
    mSetCloseOnTouchOutside = true;
  }
  
  public void setCloseOnTouchOutsideIfNotSet(boolean paramBoolean)
  {
    if (!mSetCloseOnTouchOutside)
    {
      mCloseOnTouchOutside = paramBoolean;
      mSetCloseOnTouchOutside = true;
    }
  }
  
  public void setColorMode(int paramInt)
  {
    WindowManager.LayoutParams localLayoutParams = getAttributes();
    localLayoutParams.setColorMode(paramInt);
    dispatchWindowAttributesChanged(localLayoutParams);
  }
  
  public void setContainer(Window paramWindow)
  {
    mContainer = paramWindow;
    if (paramWindow != null)
    {
      mFeatures |= 0x2;
      mLocalFeatures |= 0x2;
      mHasChildren = true;
    }
  }
  
  public abstract void setContentView(int paramInt);
  
  public abstract void setContentView(View paramView);
  
  public abstract void setContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams);
  
  public abstract void setDecorCaptionShade(int paramInt);
  
  public void setDefaultIcon(int paramInt) {}
  
  public void setDefaultLogo(int paramInt) {}
  
  protected void setDefaultWindowFormat(int paramInt)
  {
    mDefaultWindowFormat = paramInt;
    if (!mHaveWindowFormat)
    {
      WindowManager.LayoutParams localLayoutParams = getAttributes();
      format = paramInt;
      dispatchWindowAttributesChanged(localLayoutParams);
    }
  }
  
  public void setDimAmount(float paramFloat)
  {
    WindowManager.LayoutParams localLayoutParams = getAttributes();
    dimAmount = paramFloat;
    mHaveDimAmount = true;
    dispatchWindowAttributesChanged(localLayoutParams);
  }
  
  public void setElevation(float paramFloat) {}
  
  public void setEnterTransition(Transition paramTransition) {}
  
  public void setExitTransition(Transition paramTransition) {}
  
  public abstract void setFeatureDrawable(int paramInt, Drawable paramDrawable);
  
  public abstract void setFeatureDrawableAlpha(int paramInt1, int paramInt2);
  
  public abstract void setFeatureDrawableResource(int paramInt1, int paramInt2);
  
  public abstract void setFeatureDrawableUri(int paramInt, Uri paramUri);
  
  public abstract void setFeatureInt(int paramInt1, int paramInt2);
  
  public void setFlags(int paramInt1, int paramInt2)
  {
    WindowManager.LayoutParams localLayoutParams = getAttributes();
    flags = (flags & paramInt2 | paramInt1 & paramInt2);
    mForcedWindowFlags |= paramInt2;
    dispatchWindowAttributesChanged(localLayoutParams);
  }
  
  public void setFormat(int paramInt)
  {
    WindowManager.LayoutParams localLayoutParams = getAttributes();
    if (paramInt != 0)
    {
      format = paramInt;
      mHaveWindowFormat = true;
    }
    else
    {
      format = mDefaultWindowFormat;
      mHaveWindowFormat = false;
    }
    dispatchWindowAttributesChanged(localLayoutParams);
  }
  
  public void setGravity(int paramInt)
  {
    WindowManager.LayoutParams localLayoutParams = getAttributes();
    gravity = paramInt;
    dispatchWindowAttributesChanged(localLayoutParams);
  }
  
  public void setIcon(int paramInt) {}
  
  public void setLayout(int paramInt1, int paramInt2)
  {
    WindowManager.LayoutParams localLayoutParams = getAttributes();
    width = paramInt1;
    height = paramInt2;
    dispatchWindowAttributesChanged(localLayoutParams);
  }
  
  public void setLocalFocus(boolean paramBoolean1, boolean paramBoolean2) {}
  
  public void setLogo(int paramInt) {}
  
  public void setMediaController(MediaController paramMediaController) {}
  
  public abstract void setNavigationBarColor(int paramInt);
  
  public void setNavigationBarDividerColor(int paramInt) {}
  
  protected void setNeedsMenuKey(int paramInt)
  {
    WindowManager.LayoutParams localLayoutParams = getAttributes();
    needsMenuKey = paramInt;
    dispatchWindowAttributesChanged(localLayoutParams);
  }
  
  public final void setOnWindowDismissedCallback(OnWindowDismissedCallback paramOnWindowDismissedCallback)
  {
    mOnWindowDismissedCallback = paramOnWindowDismissedCallback;
  }
  
  public final void setOnWindowSwipeDismissedCallback(OnWindowSwipeDismissedCallback paramOnWindowSwipeDismissedCallback)
  {
    mOnWindowSwipeDismissedCallback = paramOnWindowSwipeDismissedCallback;
  }
  
  public void setOverlayWithDecorCaptionEnabled(boolean paramBoolean)
  {
    mOverlayWithDecorCaptionEnabled = paramBoolean;
  }
  
  public void setReenterTransition(Transition paramTransition) {}
  
  public abstract void setResizingCaptionDrawable(Drawable paramDrawable);
  
  public final void setRestrictedCaptionAreaListener(OnRestrictedCaptionAreaChangedListener paramOnRestrictedCaptionAreaChangedListener)
  {
    mOnRestrictedCaptionAreaChangedListener = paramOnRestrictedCaptionAreaChangedListener;
    if (paramOnRestrictedCaptionAreaChangedListener != null) {
      paramOnRestrictedCaptionAreaChangedListener = new Rect();
    } else {
      paramOnRestrictedCaptionAreaChangedListener = null;
    }
    mRestrictedCaptionAreaRect = paramOnRestrictedCaptionAreaChangedListener;
  }
  
  public void setReturnTransition(Transition paramTransition) {}
  
  public void setSharedElementEnterTransition(Transition paramTransition) {}
  
  public void setSharedElementExitTransition(Transition paramTransition) {}
  
  public void setSharedElementReenterTransition(Transition paramTransition) {}
  
  public void setSharedElementReturnTransition(Transition paramTransition) {}
  
  public void setSharedElementsUseOverlay(boolean paramBoolean) {}
  
  public void setSoftInputMode(int paramInt)
  {
    WindowManager.LayoutParams localLayoutParams = getAttributes();
    if (paramInt != 0)
    {
      softInputMode = paramInt;
      mHasSoftInputMode = true;
    }
    else
    {
      mHasSoftInputMode = false;
    }
    dispatchWindowAttributesChanged(localLayoutParams);
  }
  
  public abstract void setStatusBarColor(int paramInt);
  
  public void setSustainedPerformanceMode(boolean paramBoolean)
  {
    int i;
    if (paramBoolean) {
      i = 262144;
    } else {
      i = 0;
    }
    setPrivateFlags(i, 262144);
  }
  
  public void setTheme(int paramInt) {}
  
  public abstract void setTitle(CharSequence paramCharSequence);
  
  @Deprecated
  public abstract void setTitleColor(int paramInt);
  
  public void setTransitionBackgroundFadeDuration(long paramLong) {}
  
  public void setTransitionManager(TransitionManager paramTransitionManager)
  {
    throw new UnsupportedOperationException();
  }
  
  public void setType(int paramInt)
  {
    WindowManager.LayoutParams localLayoutParams = getAttributes();
    type = paramInt;
    dispatchWindowAttributesChanged(localLayoutParams);
  }
  
  public void setUiOptions(int paramInt) {}
  
  public void setUiOptions(int paramInt1, int paramInt2) {}
  
  public abstract void setVolumeControlStream(int paramInt);
  
  public void setWindowAnimations(int paramInt)
  {
    WindowManager.LayoutParams localLayoutParams = getAttributes();
    windowAnimations = paramInt;
    dispatchWindowAttributesChanged(localLayoutParams);
  }
  
  public final void setWindowControllerCallback(WindowControllerCallback paramWindowControllerCallback)
  {
    mWindowControllerCallback = paramWindowControllerCallback;
  }
  
  public void setWindowManager(WindowManager paramWindowManager, IBinder paramIBinder, String paramString)
  {
    setWindowManager(paramWindowManager, paramIBinder, paramString, false);
  }
  
  public void setWindowManager(WindowManager paramWindowManager, IBinder paramIBinder, String paramString, boolean paramBoolean)
  {
    mAppToken = paramIBinder;
    mAppName = paramString;
    boolean bool = false;
    if ((!paramBoolean) && (!SystemProperties.getBoolean("persist.sys.ui.hw", false))) {
      paramBoolean = bool;
    } else {
      paramBoolean = true;
    }
    mHardwareAccelerated = paramBoolean;
    paramIBinder = paramWindowManager;
    if (paramWindowManager == null) {
      paramIBinder = (WindowManager)mContext.getSystemService("window");
    }
    mWindowManager = ((WindowManagerImpl)paramIBinder).createLocalWindowManager(this);
  }
  
  public boolean shouldCloseOnTouch(Context paramContext, MotionEvent paramMotionEvent)
  {
    int i;
    if (((paramMotionEvent.getAction() == 0) && (isOutOfBounds(paramContext, paramMotionEvent))) || (paramMotionEvent.getAction() == 4)) {
      i = 1;
    } else {
      i = 0;
    }
    return (mCloseOnTouchOutside) && (peekDecorView() != null) && (i != 0);
  }
  
  public abstract boolean superDispatchGenericMotionEvent(MotionEvent paramMotionEvent);
  
  public abstract boolean superDispatchKeyEvent(KeyEvent paramKeyEvent);
  
  public abstract boolean superDispatchKeyShortcutEvent(KeyEvent paramKeyEvent);
  
  public abstract boolean superDispatchTouchEvent(MotionEvent paramMotionEvent);
  
  public abstract boolean superDispatchTrackballEvent(MotionEvent paramMotionEvent);
  
  public abstract void takeInputQueue(InputQueue.Callback paramCallback);
  
  public abstract void takeKeyEvents(boolean paramBoolean);
  
  public abstract void takeSurface(SurfaceHolder.Callback2 paramCallback2);
  
  public abstract void togglePanel(int paramInt, KeyEvent paramKeyEvent);
  
  public static abstract interface Callback
  {
    public abstract boolean dispatchGenericMotionEvent(MotionEvent paramMotionEvent);
    
    public abstract boolean dispatchKeyEvent(KeyEvent paramKeyEvent);
    
    public abstract boolean dispatchKeyShortcutEvent(KeyEvent paramKeyEvent);
    
    public abstract boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent);
    
    public abstract boolean dispatchTouchEvent(MotionEvent paramMotionEvent);
    
    public abstract boolean dispatchTrackballEvent(MotionEvent paramMotionEvent);
    
    public abstract void onActionModeFinished(ActionMode paramActionMode);
    
    public abstract void onActionModeStarted(ActionMode paramActionMode);
    
    public abstract void onAttachedToWindow();
    
    public abstract void onContentChanged();
    
    public abstract boolean onCreatePanelMenu(int paramInt, Menu paramMenu);
    
    public abstract View onCreatePanelView(int paramInt);
    
    public abstract void onDetachedFromWindow();
    
    public abstract boolean onMenuItemSelected(int paramInt, MenuItem paramMenuItem);
    
    public abstract boolean onMenuOpened(int paramInt, Menu paramMenu);
    
    public abstract void onPanelClosed(int paramInt, Menu paramMenu);
    
    public void onPointerCaptureChanged(boolean paramBoolean) {}
    
    public abstract boolean onPreparePanel(int paramInt, View paramView, Menu paramMenu);
    
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> paramList, Menu paramMenu, int paramInt) {}
    
    public abstract boolean onSearchRequested();
    
    public abstract boolean onSearchRequested(SearchEvent paramSearchEvent);
    
    public abstract void onWindowAttributesChanged(WindowManager.LayoutParams paramLayoutParams);
    
    public abstract void onWindowFocusChanged(boolean paramBoolean);
    
    public abstract ActionMode onWindowStartingActionMode(ActionMode.Callback paramCallback);
    
    public abstract ActionMode onWindowStartingActionMode(ActionMode.Callback paramCallback, int paramInt);
  }
  
  public static abstract interface OnFrameMetricsAvailableListener
  {
    public abstract void onFrameMetricsAvailable(Window paramWindow, FrameMetrics paramFrameMetrics, int paramInt);
  }
  
  public static abstract interface OnRestrictedCaptionAreaChangedListener
  {
    public abstract void onRestrictedCaptionAreaChanged(Rect paramRect);
  }
  
  public static abstract interface OnWindowDismissedCallback
  {
    public abstract void onWindowDismissed(boolean paramBoolean1, boolean paramBoolean2);
  }
  
  public static abstract interface OnWindowSwipeDismissedCallback
  {
    public abstract void onWindowSwipeDismissed();
  }
  
  public static abstract interface WindowControllerCallback
  {
    public abstract void enterPictureInPictureModeIfPossible();
    
    public abstract void exitFreeformMode()
      throws RemoteException;
    
    public abstract boolean isTaskRoot();
  }
}
