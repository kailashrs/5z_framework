package com.android.internal.policy;

import android.app.ActivityManager;
import android.app.IActivityManager;
import android.app.KeyguardManager;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.net.Uri;
import android.os.Build.FEATURES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.AndroidRuntimeException;
import android.util.EventLog;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.IRotationWatcher.Stub;
import android.view.IWindowManager;
import android.view.IWindowManager.Stub;
import android.view.InputDevice;
import android.view.InputEvent;
import android.view.InputQueue.Callback;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.KeyEvent.DispatcherState;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.SurfaceHolder.Callback2;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewManager;
import android.view.ViewRootImpl;
import android.view.ViewRootImpl.ActivityConfigCallback;
import android.view.Window;
import android.view.Window.Callback;
import android.view.Window.WindowControllerCallback;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.internal.R.styleable;
import com.android.internal.view.menu.ContextMenuBuilder;
import com.android.internal.view.menu.IconMenuPresenter;
import com.android.internal.view.menu.ListMenuPresenter;
import com.android.internal.view.menu.MenuBuilder;
import com.android.internal.view.menu.MenuBuilder.Callback;
import com.android.internal.view.menu.MenuDialogHelper;
import com.android.internal.view.menu.MenuHelper;
import com.android.internal.view.menu.MenuPresenter.Callback;
import com.android.internal.view.menu.MenuView;
import com.android.internal.widget.DecorContentParent;
import com.android.internal.widget.SwipeDismissLayout;
import com.android.internal.widget.SwipeDismissLayout.OnDismissedListener;
import com.android.internal.widget.SwipeDismissLayout.OnSwipeProgressChangedListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class PhoneWindow
  extends Window
  implements MenuBuilder.Callback
{
  private static final String ACTION_BAR_TAG = "android:ActionBar";
  private static final int CUSTOM_TITLE_COMPATIBLE_FEATURES = 13505;
  private static final boolean DEBUG = false;
  private static final int DEFAULT_BACKGROUND_FADE_DURATION_MS = 300;
  static final int FLAG_RESOURCE_SET_ICON = 1;
  static final int FLAG_RESOURCE_SET_ICON_FALLBACK = 4;
  static final int FLAG_RESOURCE_SET_LOGO = 2;
  private static final String FOCUSED_ID_TAG = "android:focusedViewId";
  private static final String PANELS_TAG = "android:Panels";
  private static final String TAG = "PhoneWindow";
  private static final Transition USE_DEFAULT_TRANSITION = new TransitionSet();
  private static final String VIEWS_TAG = "android:views";
  static final RotationWatcher sRotationWatcher = new RotationWatcher();
  private ActionMenuPresenterCallback mActionMenuPresenterCallback;
  private ViewRootImpl.ActivityConfigCallback mActivityConfigCallback;
  private Boolean mAllowEnterTransitionOverlap;
  private Boolean mAllowReturnTransitionOverlap;
  private boolean mAlwaysReadCloseOnTouchAttr = false;
  private AudioManager mAudioManager;
  private Drawable mBackgroundDrawable;
  private long mBackgroundFadeDurationMillis = -1L;
  int mBackgroundFallbackResource = 0;
  int mBackgroundResource = 0;
  private ProgressBar mCircularProgressBar;
  private boolean mClipToOutline;
  private boolean mClosingActionMenu;
  ViewGroup mContentParent;
  private boolean mContentParentExplicitlySet = false;
  private Scene mContentScene;
  ContextMenuBuilder mContextMenu;
  final PhoneWindowMenuCallback mContextMenuCallback = new PhoneWindowMenuCallback(this);
  MenuHelper mContextMenuHelper;
  private Integer mCurrentNavigationBarColor = null;
  private DecorView mDecor;
  private int mDecorCaptionShade = 0;
  DecorContentParent mDecorContentParent;
  private DrawableFeatureState[] mDrawables;
  private float mElevation;
  private Transition mEnterTransition = null;
  private Transition mExitTransition = null;
  TypedValue mFixedHeightMajor;
  TypedValue mFixedHeightMinor;
  TypedValue mFixedWidthMajor;
  TypedValue mFixedWidthMinor;
  private boolean mForceDecorInstall = false;
  private boolean mForcedNavigationBarColor = false;
  private boolean mForcedStatusBarColor = false;
  private int mFrameResource = 0;
  private ProgressBar mHorizontalProgressBar;
  int mIconRes;
  private int mInvalidatePanelMenuFeatures;
  private boolean mInvalidatePanelMenuPosted;
  private final Runnable mInvalidatePanelMenuRunnable = new Runnable()
  {
    public void run()
    {
      for (int i = 0; i <= 13; i++) {
        if ((mInvalidatePanelMenuFeatures & 1 << i) != 0) {
          doInvalidatePanelMenu(i);
        }
      }
      PhoneWindow.access$102(PhoneWindow.this, false);
      PhoneWindow.access$002(PhoneWindow.this, 0);
    }
  };
  boolean mIsFloating;
  private boolean mIsStartingWindow;
  private boolean mIsTranslucent;
  private KeyguardManager mKeyguardManager;
  private LayoutInflater mLayoutInflater;
  private ImageView mLeftIconView;
  private boolean mLoadElevation = true;
  int mLogoRes;
  private MediaController mMediaController;
  private MediaSessionManager mMediaSessionManager;
  final TypedValue mMinWidthMajor = new TypedValue();
  final TypedValue mMinWidthMinor = new TypedValue();
  int mNavigationBarColor = 0;
  private int mNavigationBarColorLight;
  int mNavigationBarDividerColor = 0;
  int mPanelChordingKey;
  private PanelMenuPresenterCallback mPanelMenuPresenterCallback;
  private PanelFeatureState[] mPanels;
  PanelFeatureState mPreparedPanel;
  private Transition mReenterTransition = USE_DEFAULT_TRANSITION;
  int mResourcesSetFlags;
  private Transition mReturnTransition = USE_DEFAULT_TRANSITION;
  private ImageView mRightIconView;
  private Transition mSharedElementEnterTransition = null;
  private Transition mSharedElementExitTransition = null;
  private Transition mSharedElementReenterTransition = USE_DEFAULT_TRANSITION;
  private Transition mSharedElementReturnTransition = USE_DEFAULT_TRANSITION;
  private Boolean mSharedElementsUseOverlay;
  int mStatusBarColor = 0;
  private boolean mSupportsPictureInPicture;
  InputQueue.Callback mTakeInputQueueCallback;
  SurfaceHolder.Callback2 mTakeSurfaceCallback;
  private int mTextColor = 0;
  private int mTheme = -1;
  private CharSequence mTitle = null;
  private int mTitleColor = 0;
  private TextView mTitleView;
  private TransitionManager mTransitionManager;
  private int mUiOptions = 0;
  private boolean mUseDecorContext = false;
  private int mVolumeControlStreamType = Integer.MIN_VALUE;
  
  public PhoneWindow(Context paramContext)
  {
    super(paramContext);
    mLayoutInflater = LayoutInflater.from(paramContext);
  }
  
  public PhoneWindow(Context paramContext, Window paramWindow, ViewRootImpl.ActivityConfigCallback paramActivityConfigCallback)
  {
    this(paramContext);
    boolean bool1 = true;
    mUseDecorContext = true;
    if (paramWindow != null)
    {
      mDecor = ((DecorView)paramWindow.getDecorView());
      mElevation = paramWindow.getElevation();
      mLoadElevation = false;
      mForceDecorInstall = true;
      getAttributestoken = getAttributestoken;
    }
    int i;
    if (Settings.Global.getInt(paramContext.getContentResolver(), "force_resizable_activities", 0) != 0) {
      i = 1;
    } else {
      i = 0;
    }
    boolean bool2 = bool1;
    if (i == 0) {
      if (paramContext.getPackageManager().hasSystemFeature("android.software.picture_in_picture")) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
    }
    mSupportsPictureInPicture = bool2;
    mActivityConfigCallback = paramActivityConfigCallback;
  }
  
  private void callOnPanelClosed(int paramInt, PanelFeatureState paramPanelFeatureState, Menu paramMenu)
  {
    Window.Callback localCallback = getCallback();
    if (localCallback == null) {
      return;
    }
    Object localObject1 = paramPanelFeatureState;
    Object localObject2 = paramMenu;
    if (paramMenu == null)
    {
      PanelFeatureState localPanelFeatureState = paramPanelFeatureState;
      if (paramPanelFeatureState == null)
      {
        localPanelFeatureState = paramPanelFeatureState;
        if (paramInt >= 0)
        {
          localPanelFeatureState = paramPanelFeatureState;
          if (paramInt < mPanels.length) {
            localPanelFeatureState = mPanels[paramInt];
          }
        }
      }
      localObject1 = localPanelFeatureState;
      localObject2 = paramMenu;
      if (localPanelFeatureState != null)
      {
        localObject2 = menu;
        localObject1 = localPanelFeatureState;
      }
    }
    if ((localObject1 != null) && (!isOpen)) {
      return;
    }
    if (!isDestroyed()) {
      localCallback.onPanelClosed(paramInt, (Menu)localObject2);
    }
  }
  
  private static void clearMenuViews(PanelFeatureState paramPanelFeatureState)
  {
    createdPanelView = null;
    refreshDecorView = true;
    paramPanelFeatureState.clearMenuPresenters();
  }
  
  private void closeContextMenu()
  {
    try
    {
      if (mContextMenu != null)
      {
        mContextMenu.close();
        dismissContextMenu();
      }
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  private void dismissContextMenu()
  {
    try
    {
      mContextMenu = null;
      if (mContextMenuHelper != null)
      {
        mContextMenuHelper.dismiss();
        mContextMenuHelper = null;
      }
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  private ProgressBar getCircularProgressBar(boolean paramBoolean)
  {
    if (mCircularProgressBar != null) {
      return mCircularProgressBar;
    }
    if ((mContentParent == null) && (paramBoolean)) {
      installDecor();
    }
    mCircularProgressBar = ((ProgressBar)findViewById(16909265));
    if (mCircularProgressBar != null) {
      mCircularProgressBar.setVisibility(4);
    }
    return mCircularProgressBar;
  }
  
  private DrawableFeatureState getDrawableState(int paramInt, boolean paramBoolean)
  {
    if ((getFeatures() & 1 << paramInt) == 0)
    {
      if (!paramBoolean) {
        return null;
      }
      throw new RuntimeException("The feature has not been requested");
    }
    Object localObject1 = mDrawables;
    Object localObject2 = localObject1;
    if (localObject1 != null)
    {
      localObject1 = localObject2;
      if (localObject2.length > paramInt) {}
    }
    else
    {
      localObject3 = new DrawableFeatureState[paramInt + 1];
      if (localObject2 != null) {
        System.arraycopy(localObject2, 0, localObject3, 0, localObject2.length);
      }
      localObject1 = localObject3;
      mDrawables = ((DrawableFeatureState[])localObject3);
    }
    Object localObject3 = localObject1[paramInt];
    localObject2 = localObject3;
    if (localObject3 == null)
    {
      localObject3 = new DrawableFeatureState(paramInt);
      localObject2 = localObject3;
      localObject1[paramInt] = localObject3;
    }
    return localObject2;
  }
  
  private ProgressBar getHorizontalProgressBar(boolean paramBoolean)
  {
    if (mHorizontalProgressBar != null) {
      return mHorizontalProgressBar;
    }
    if ((mContentParent == null) && (paramBoolean)) {
      installDecor();
    }
    mHorizontalProgressBar = ((ProgressBar)findViewById(16909266));
    if (mHorizontalProgressBar != null) {
      mHorizontalProgressBar.setVisibility(4);
    }
    return mHorizontalProgressBar;
  }
  
  private KeyguardManager getKeyguardManager()
  {
    if (mKeyguardManager == null) {
      mKeyguardManager = ((KeyguardManager)getContext().getSystemService("keyguard"));
    }
    return mKeyguardManager;
  }
  
  private ImageView getLeftIconView()
  {
    if (mLeftIconView != null) {
      return mLeftIconView;
    }
    if (mContentParent == null) {
      installDecor();
    }
    ImageView localImageView = (ImageView)findViewById(16909084);
    mLeftIconView = localImageView;
    return localImageView;
  }
  
  private MediaSessionManager getMediaSessionManager()
  {
    if (mMediaSessionManager == null) {
      mMediaSessionManager = ((MediaSessionManager)getContext().getSystemService("media_session"));
    }
    return mMediaSessionManager;
  }
  
  private int getOptionsPanelGravity()
  {
    try
    {
      int i = WindowManagerHolder.sWindowManager.getPreferredOptionsPanelGravity();
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("PhoneWindow", "Couldn't getOptionsPanelGravity; using default", localRemoteException);
    }
    return 81;
  }
  
  private PanelFeatureState getPanelState(int paramInt, boolean paramBoolean, PanelFeatureState paramPanelFeatureState)
  {
    if ((getFeatures() & 1 << paramInt) == 0)
    {
      if (!paramBoolean) {
        return null;
      }
      throw new RuntimeException("The feature has not been requested");
    }
    Object localObject1 = mPanels;
    Object localObject2 = localObject1;
    if (localObject1 != null)
    {
      localObject1 = localObject2;
      if (localObject2.length > paramInt) {}
    }
    else
    {
      arrayOfPanelFeatureState = new PanelFeatureState[paramInt + 1];
      if (localObject2 != null) {
        System.arraycopy(localObject2, 0, arrayOfPanelFeatureState, 0, localObject2.length);
      }
      localObject1 = arrayOfPanelFeatureState;
      mPanels = arrayOfPanelFeatureState;
    }
    PanelFeatureState[] arrayOfPanelFeatureState = localObject1[paramInt];
    localObject2 = arrayOfPanelFeatureState;
    if (arrayOfPanelFeatureState == null)
    {
      if (paramPanelFeatureState == null) {
        paramPanelFeatureState = new PanelFeatureState(paramInt);
      }
      localObject2 = paramPanelFeatureState;
      localObject1[paramInt] = paramPanelFeatureState;
    }
    return localObject2;
  }
  
  private ImageView getRightIconView()
  {
    if (mRightIconView != null) {
      return mRightIconView;
    }
    if (mContentParent == null) {
      installDecor();
    }
    ImageView localImageView = (ImageView)findViewById(16909299);
    mRightIconView = localImageView;
    return localImageView;
  }
  
  private Transition getTransition(Transition paramTransition1, Transition paramTransition2, int paramInt)
  {
    if (paramTransition1 != paramTransition2) {
      return paramTransition1;
    }
    paramInt = getWindowStyle().getResourceId(paramInt, -1);
    paramTransition1 = paramTransition2;
    if (paramInt != -1)
    {
      paramTransition1 = paramTransition2;
      if (paramInt != 17760256)
      {
        paramTransition2 = TransitionInflater.from(getContext()).inflateTransition(paramInt);
        paramTransition1 = paramTransition2;
        if ((paramTransition2 instanceof TransitionSet))
        {
          paramTransition1 = paramTransition2;
          if (((TransitionSet)paramTransition2).getTransitionCount() == 0) {
            paramTransition1 = null;
          }
        }
      }
    }
    return paramTransition1;
  }
  
  private ViewRootImpl getViewRootImpl()
  {
    if (mDecor != null)
    {
      ViewRootImpl localViewRootImpl = mDecor.getViewRootImpl();
      if (localViewRootImpl != null) {
        return localViewRootImpl;
      }
    }
    throw new IllegalStateException("view not added");
  }
  
  private void hideProgressBars(ProgressBar paramProgressBar1, ProgressBar paramProgressBar2)
  {
    int i = getLocalFeatures();
    Animation localAnimation = AnimationUtils.loadAnimation(getContext(), 17432577);
    localAnimation.setDuration(1000L);
    if (((i & 0x20) != 0) && (paramProgressBar2 != null) && (paramProgressBar2.getVisibility() == 0))
    {
      paramProgressBar2.startAnimation(localAnimation);
      paramProgressBar2.setVisibility(4);
    }
    if (((i & 0x4) != 0) && (paramProgressBar1 != null) && (paramProgressBar1.getVisibility() == 0))
    {
      paramProgressBar1.startAnimation(localAnimation);
      paramProgressBar1.setVisibility(4);
    }
  }
  
  private void installDecor()
  {
    mForceDecorInstall = false;
    if (mDecor == null)
    {
      mDecor = generateDecor(-1);
      mDecor.setDescendantFocusability(262144);
      mDecor.setIsRootNamespace(true);
      if ((!mInvalidatePanelMenuPosted) && (mInvalidatePanelMenuFeatures != 0)) {
        mDecor.postOnAnimation(mInvalidatePanelMenuRunnable);
      }
    }
    else
    {
      mDecor.setWindow(this);
    }
    if (mContentParent == null)
    {
      mContentParent = generateLayout(mDecor);
      mDecor.makeOptionalFitsSystemWindows();
      Object localObject = (DecorContentParent)mDecor.findViewById(16908895);
      int j;
      if (localObject != null)
      {
        mDecorContentParent = ((DecorContentParent)localObject);
        mDecorContentParent.setWindowCallback(getCallback());
        if (mDecorContentParent.getTitle() == null) {
          mDecorContentParent.setWindowTitle(mTitle);
        }
        int i = getLocalFeatures();
        for (j = 0; j < 13; j++) {
          if ((1 << j & i) != 0) {
            mDecorContentParent.initFeature(j);
          }
        }
        mDecorContentParent.setUiOptions(mUiOptions);
        if (((mResourcesSetFlags & 0x1) == 0) && ((mIconRes == 0) || (mDecorContentParent.hasIcon())))
        {
          if (((mResourcesSetFlags & 0x1) == 0) && (mIconRes == 0) && (!mDecorContentParent.hasIcon()))
          {
            mDecorContentParent.setIcon(getContext().getPackageManager().getDefaultActivityIcon());
            mResourcesSetFlags |= 0x4;
          }
        }
        else {
          mDecorContentParent.setIcon(mIconRes);
        }
        if (((mResourcesSetFlags & 0x2) != 0) || ((mLogoRes != 0) && (!mDecorContentParent.hasLogo()))) {
          mDecorContentParent.setLogo(mLogoRes);
        }
        localObject = getPanelState(0, false);
        if ((!isDestroyed()) && ((localObject == null) || (menu == null)) && (!mIsStartingWindow)) {
          invalidatePanelMenu(8);
        }
      }
      else
      {
        mTitleView = ((TextView)findViewById(16908310));
        if (mTitleView != null) {
          if ((getLocalFeatures() & 0x2) != 0)
          {
            localObject = findViewById(16909478);
            if (localObject != null) {
              ((View)localObject).setVisibility(8);
            } else {
              mTitleView.setVisibility(8);
            }
            mContentParent.setForeground(null);
          }
          else
          {
            mTitleView.setText(mTitle);
          }
        }
      }
      if ((mDecor.getBackground() == null) && (mBackgroundFallbackResource != 0)) {
        mDecor.setBackgroundFallback(mBackgroundFallbackResource);
      }
      if (hasFeature(13))
      {
        if (mTransitionManager == null)
        {
          j = getWindowStyle().getResourceId(27, 0);
          if (j != 0) {
            mTransitionManager = TransitionInflater.from(getContext()).inflateTransitionManager(j, mContentParent);
          } else {
            mTransitionManager = new TransitionManager();
          }
        }
        mEnterTransition = getTransition(mEnterTransition, null, 28);
        mReturnTransition = getTransition(mReturnTransition, USE_DEFAULT_TRANSITION, 40);
        mExitTransition = getTransition(mExitTransition, null, 29);
        mReenterTransition = getTransition(mReenterTransition, USE_DEFAULT_TRANSITION, 41);
        mSharedElementEnterTransition = getTransition(mSharedElementEnterTransition, null, 30);
        mSharedElementReturnTransition = getTransition(mSharedElementReturnTransition, USE_DEFAULT_TRANSITION, 42);
        mSharedElementExitTransition = getTransition(mSharedElementExitTransition, null, 31);
        mSharedElementReenterTransition = getTransition(mSharedElementReenterTransition, USE_DEFAULT_TRANSITION, 43);
        if (mAllowEnterTransitionOverlap == null) {
          mAllowEnterTransitionOverlap = Boolean.valueOf(getWindowStyle().getBoolean(33, true));
        }
        if (mAllowReturnTransitionOverlap == null) {
          mAllowReturnTransitionOverlap = Boolean.valueOf(getWindowStyle().getBoolean(32, true));
        }
        if (mBackgroundFadeDurationMillis < 0L) {
          mBackgroundFadeDurationMillis = getWindowStyle().getInteger(37, 300);
        }
        if (mSharedElementsUseOverlay == null) {
          mSharedElementsUseOverlay = Boolean.valueOf(getWindowStyle().getBoolean(44, true));
        }
      }
    }
    if (Build.FEATURES.ENABLE_COLORFUL_NAV) {
      mNavigationBarColorLight = getContext().getColor(17170772);
    }
  }
  
  private boolean isNotInstantAppAndKeyguardRestricted()
  {
    boolean bool;
    if ((!getContext().getPackageManager().isInstantApp()) && (getKeyguardManager().inKeyguardRestrictedInputMode())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isTvUserSetupComplete()
  {
    int i = Settings.Secure.getInt(getContext().getContentResolver(), "user_setup_complete", 0);
    int j = 1;
    if (i != 0) {
      i = 1;
    } else {
      i = 0;
    }
    if (Settings.Secure.getInt(getContext().getContentResolver(), "tv_user_setup_complete", 0) == 0) {
      j = 0;
    }
    return i & j;
  }
  
  private boolean launchDefaultSearch(KeyEvent paramKeyEvent)
  {
    if ((getContext().getPackageManager().hasSystemFeature("android.software.leanback")) && (!isTvUserSetupComplete())) {
      return false;
    }
    Window.Callback localCallback = getCallback();
    boolean bool;
    if ((localCallback != null) && (!isDestroyed()))
    {
      sendCloseSystemWindows("search");
      int i = paramKeyEvent.getDeviceId();
      SearchEvent localSearchEvent = null;
      if (i != 0) {
        localSearchEvent = new SearchEvent(InputDevice.getDevice(i));
      }
      try
      {
        bool = localCallback.onSearchRequested(localSearchEvent);
      }
      catch (AbstractMethodError localAbstractMethodError)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("WindowCallback ");
        localStringBuilder.append(localCallback.getClass().getName());
        localStringBuilder.append(" does not implement method onSearchRequested(SearchEvent); fa");
        Log.e("PhoneWindow", localStringBuilder.toString(), localAbstractMethodError);
        bool = localCallback.onSearchRequested();
      }
    }
    else
    {
      bool = false;
    }
    if ((!bool) && ((getContextgetResourcesgetConfigurationuiMode & 0xF) == 4))
    {
      Bundle localBundle = new Bundle();
      localBundle.putInt("android.intent.extra.ASSIST_INPUT_DEVICE_ID", paramKeyEvent.getDeviceId());
      return ((SearchManager)getContext().getSystemService("search")).launchLegacyAssist(null, getContext().getUserId(), localBundle);
    }
    return bool;
  }
  
  private Drawable loadImageURI(Uri paramUri)
  {
    try
    {
      Drawable localDrawable = Drawable.createFromStream(getContext().getContentResolver().openInputStream(paramUri), null);
      return localDrawable;
    }
    catch (Exception localException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unable to open content: ");
      localStringBuilder.append(paramUri);
      Log.w("PhoneWindow", localStringBuilder.toString());
    }
    return null;
  }
  
  private void openPanel(PanelFeatureState paramPanelFeatureState, KeyEvent paramKeyEvent)
  {
    if ((!isOpen) && (!isDestroyed()))
    {
      int i;
      if (featureId == 0)
      {
        localObject = getContext();
        if ((getResourcesgetConfigurationscreenLayout & 0xF) == 4) {
          i = 1;
        } else {
          i = 0;
        }
        if (getApplicationInfotargetSdkVersion >= 11) {
          j = 1;
        } else {
          j = 0;
        }
        if ((i != 0) && (j != 0)) {
          return;
        }
      }
      Object localObject = getCallback();
      if ((localObject != null) && (!((Window.Callback)localObject).onMenuOpened(featureId, menu)))
      {
        closePanel(paramPanelFeatureState, true);
        return;
      }
      WindowManager localWindowManager = getWindowManager();
      if (localWindowManager == null) {
        return;
      }
      if (!preparePanel(paramPanelFeatureState, paramKeyEvent)) {
        return;
      }
      int j = -2;
      if ((decorView != null) && (!refreshDecorView))
      {
        if (!paramPanelFeatureState.isInListMode())
        {
          i = -1;
        }
        else
        {
          i = j;
          if (createdPanelView != null)
          {
            paramKeyEvent = createdPanelView.getLayoutParams();
            i = j;
            if (paramKeyEvent != null)
            {
              i = j;
              if (width == -1) {
                i = -1;
              }
            }
          }
        }
      }
      else
      {
        if (decorView == null)
        {
          if ((initializePanelDecor(paramPanelFeatureState)) && (decorView != null)) {}
        }
        else if ((refreshDecorView) && (decorView.getChildCount() > 0)) {
          decorView.removeAllViews();
        }
        if ((!initializePanelContent(paramPanelFeatureState)) || (!paramPanelFeatureState.hasPanelItems())) {
          break label515;
        }
        localObject = shownPanelView.getLayoutParams();
        paramKeyEvent = (KeyEvent)localObject;
        if (localObject == null) {
          paramKeyEvent = new ViewGroup.LayoutParams(-2, -2);
        }
        int k;
        if (width == -1)
        {
          k = fullBackground;
          i = -1;
        }
        else
        {
          k = background;
          i = j;
        }
        decorView.setWindowBackground(getContext().getDrawable(k));
        localObject = shownPanelView.getParent();
        if ((localObject != null) && ((localObject instanceof ViewGroup))) {
          ((ViewGroup)localObject).removeView(shownPanelView);
        }
        decorView.addView(shownPanelView, paramKeyEvent);
        if (!shownPanelView.hasFocus()) {
          shownPanelView.requestFocus();
        }
      }
      isHandled = false;
      paramKeyEvent = new WindowManager.LayoutParams(i, -2, x, y, 1003, 8519680, decorView.mDefaultOpacity);
      if (isCompact)
      {
        gravity = getOptionsPanelGravity();
        sRotationWatcher.addWindow(this);
      }
      else
      {
        gravity = gravity;
      }
      windowAnimations = windowAnimations;
      localWindowManager.addView(decorView, paramKeyEvent);
      isOpen = true;
      return;
      label515:
      return;
    }
  }
  
  private void registerSwipeCallbacks(ViewGroup paramViewGroup)
  {
    if (!(paramViewGroup instanceof SwipeDismissLayout))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("contentParent is not a SwipeDismissLayout: ");
      localStringBuilder.append(paramViewGroup);
      Log.w("PhoneWindow", localStringBuilder.toString());
      return;
    }
    paramViewGroup = (SwipeDismissLayout)paramViewGroup;
    paramViewGroup.setOnDismissedListener(new SwipeDismissLayout.OnDismissedListener()
    {
      public void onDismissed(SwipeDismissLayout paramAnonymousSwipeDismissLayout)
      {
        dispatchOnWindowSwipeDismissed();
        dispatchOnWindowDismissed(false, true);
      }
    });
    paramViewGroup.setOnSwipeProgressChangedListener(new SwipeDismissLayout.OnSwipeProgressChangedListener()
    {
      public void onSwipeCancelled(SwipeDismissLayout paramAnonymousSwipeDismissLayout)
      {
        paramAnonymousSwipeDismissLayout = getAttributes();
        if ((x != 0) || (alpha != 1.0F))
        {
          x = 0;
          alpha = 1.0F;
          setAttributes(paramAnonymousSwipeDismissLayout);
          setFlags(1024, 1536);
        }
      }
      
      public void onSwipeProgressChanged(SwipeDismissLayout paramAnonymousSwipeDismissLayout, float paramAnonymousFloat1, float paramAnonymousFloat2)
      {
        paramAnonymousSwipeDismissLayout = getAttributes();
        x = ((int)paramAnonymousFloat2);
        alpha = paramAnonymousFloat1;
        setAttributes(paramAnonymousSwipeDismissLayout);
        int i;
        if (x == 0) {
          i = 1024;
        } else {
          i = 512;
        }
        setFlags(i, 1536);
      }
    });
  }
  
  private void reopenMenu(boolean paramBoolean)
  {
    if ((mDecorContentParent != null) && (mDecorContentParent.canShowOverflowMenu()) && ((!ViewConfiguration.get(getContext()).hasPermanentMenuKey()) || (mDecorContentParent.isOverflowMenuShowPending())))
    {
      localObject = getCallback();
      PanelFeatureState localPanelFeatureState;
      if ((mDecorContentParent.isOverflowMenuShowing()) && (paramBoolean))
      {
        mDecorContentParent.hideOverflowMenu();
        localPanelFeatureState = getPanelState(0, false);
        if ((localPanelFeatureState != null) && (localObject != null) && (!isDestroyed())) {
          ((Window.Callback)localObject).onPanelClosed(8, menu);
        }
      }
      else if ((localObject != null) && (!isDestroyed()))
      {
        if ((mInvalidatePanelMenuPosted) && ((0x1 & mInvalidatePanelMenuFeatures) != 0))
        {
          mDecor.removeCallbacks(mInvalidatePanelMenuRunnable);
          mInvalidatePanelMenuRunnable.run();
        }
        localPanelFeatureState = getPanelState(0, false);
        if ((localPanelFeatureState != null) && (menu != null) && (!refreshMenuContent) && (((Window.Callback)localObject).onPreparePanel(0, createdPanelView, menu)))
        {
          ((Window.Callback)localObject).onMenuOpened(8, menu);
          mDecorContentParent.showOverflowMenu();
        }
      }
      return;
    }
    Object localObject = getPanelState(0, false);
    if (localObject == null) {
      return;
    }
    if (paramBoolean)
    {
      if (!isInExpandedMode) {
        paramBoolean = true;
      } else {
        paramBoolean = false;
      }
    }
    else {
      paramBoolean = isInExpandedMode;
    }
    refreshDecorView = true;
    closePanel((PanelFeatureState)localObject, false);
    isInExpandedMode = paramBoolean;
    openPanel((PanelFeatureState)localObject, null);
  }
  
  private void restorePanelState(SparseArray<Parcelable> paramSparseArray)
  {
    for (int i = paramSparseArray.size() - 1; i >= 0; i--)
    {
      int j = paramSparseArray.keyAt(i);
      PanelFeatureState localPanelFeatureState = getPanelState(j, false);
      if (localPanelFeatureState != null)
      {
        localPanelFeatureState.onRestoreInstanceState((Parcelable)paramSparseArray.get(j));
        invalidatePanelMenu(j);
      }
    }
  }
  
  private void savePanelState(SparseArray<Parcelable> paramSparseArray)
  {
    PanelFeatureState[] arrayOfPanelFeatureState = mPanels;
    if (arrayOfPanelFeatureState == null) {
      return;
    }
    for (int i = arrayOfPanelFeatureState.length - 1; i >= 0; i--) {
      if (arrayOfPanelFeatureState[i] != null) {
        paramSparseArray.put(i, arrayOfPanelFeatureState[i].onSaveInstanceState());
      }
    }
  }
  
  public static void sendCloseSystemWindows(Context paramContext, String paramString)
  {
    if (ActivityManager.isSystemReady()) {
      try
      {
        ActivityManager.getService().closeSystemDialogs(paramString);
      }
      catch (RemoteException paramContext) {}
    }
  }
  
  private void showProgressBars(ProgressBar paramProgressBar1, ProgressBar paramProgressBar2)
  {
    int i = getLocalFeatures();
    if (((i & 0x20) != 0) && (paramProgressBar2 != null) && (paramProgressBar2.getVisibility() == 4)) {
      paramProgressBar2.setVisibility(0);
    }
    if (((i & 0x4) != 0) && (paramProgressBar1 != null) && (paramProgressBar1.getProgress() < 10000)) {
      paramProgressBar1.setVisibility(0);
    }
  }
  
  private void transitionTo(Scene paramScene)
  {
    if (mContentScene == null) {
      paramScene.enter();
    } else {
      mTransitionManager.transitionTo(paramScene);
    }
    mContentScene = paramScene;
  }
  
  private void updateDrawable(int paramInt, DrawableFeatureState paramDrawableFeatureState, boolean paramBoolean)
  {
    if (mContentParent == null) {
      return;
    }
    int i = 1 << paramInt;
    if (((getFeatures() & i) == 0) && (!paramBoolean)) {
      return;
    }
    Object localObject1 = null;
    if (paramDrawableFeatureState != null)
    {
      localObject1 = child;
      Object localObject2 = localObject1;
      if (localObject1 == null) {
        localObject2 = local;
      }
      localObject1 = localObject2;
      if (localObject2 == null) {
        localObject1 = def;
      }
    }
    if ((getLocalFeatures() & i) == 0)
    {
      if ((getContainer() != null) && ((isActive()) || (paramBoolean))) {
        getContainer().setChildDrawable(paramInt, (Drawable)localObject1);
      }
    }
    else if ((paramDrawableFeatureState != null) && ((cur != localObject1) || (curAlpha != alpha)))
    {
      cur = ((Drawable)localObject1);
      curAlpha = alpha;
      onDrawableChanged(paramInt, (Drawable)localObject1, alpha);
    }
  }
  
  private void updateInt(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if (mContentParent == null) {
      return;
    }
    int i = 1 << paramInt1;
    if (((getFeatures() & i) == 0) && (!paramBoolean)) {
      return;
    }
    if ((getLocalFeatures() & i) == 0)
    {
      if (getContainer() != null) {
        getContainer().setChildInt(paramInt1, paramInt2);
      }
    }
    else {
      onIntChanged(paramInt1, paramInt2);
    }
  }
  
  private void updateProgressBars(int paramInt)
  {
    ProgressBar localProgressBar1 = getCircularProgressBar(true);
    ProgressBar localProgressBar2 = getHorizontalProgressBar(true);
    int i = getLocalFeatures();
    if (paramInt == -1)
    {
      if ((i & 0x4) != 0) {
        if (localProgressBar2 != null)
        {
          paramInt = localProgressBar2.getProgress();
          if ((!localProgressBar2.isIndeterminate()) && (paramInt >= 10000)) {
            paramInt = 4;
          } else {
            paramInt = 0;
          }
          localProgressBar2.setVisibility(paramInt);
        }
        else
        {
          Log.e("PhoneWindow", "Horizontal progress bar not located in current window decor");
        }
      }
      if ((i & 0x20) != 0) {
        if (localProgressBar1 != null) {
          localProgressBar1.setVisibility(0);
        } else {
          Log.e("PhoneWindow", "Circular progress bar not located in current window decor");
        }
      }
    }
    else if (paramInt == -2)
    {
      if ((i & 0x4) != 0) {
        if (localProgressBar2 != null) {
          localProgressBar2.setVisibility(8);
        } else {
          Log.e("PhoneWindow", "Horizontal progress bar not located in current window decor");
        }
      }
      if ((i & 0x20) != 0) {
        if (localProgressBar1 != null) {
          localProgressBar1.setVisibility(8);
        } else {
          Log.e("PhoneWindow", "Circular progress bar not located in current window decor");
        }
      }
    }
    else if (paramInt == -3)
    {
      if (localProgressBar2 != null) {
        localProgressBar2.setIndeterminate(true);
      } else {
        Log.e("PhoneWindow", "Horizontal progress bar not located in current window decor");
      }
    }
    else if (paramInt == -4)
    {
      if (localProgressBar2 != null) {
        localProgressBar2.setIndeterminate(false);
      } else {
        Log.e("PhoneWindow", "Horizontal progress bar not located in current window decor");
      }
    }
    else if ((paramInt >= 0) && (paramInt <= 10000))
    {
      if (localProgressBar2 != null) {
        localProgressBar2.setProgress(paramInt + 0);
      } else {
        Log.e("PhoneWindow", "Horizontal progress bar not located in current window decor");
      }
      if (paramInt < 10000) {
        showProgressBars(localProgressBar2, localProgressBar1);
      } else {
        hideProgressBars(localProgressBar2, localProgressBar1);
      }
    }
    else if ((20000 <= paramInt) && (paramInt <= 30000))
    {
      if (localProgressBar2 != null) {
        localProgressBar2.setSecondaryProgress(paramInt - 20000);
      } else {
        Log.e("PhoneWindow", "Horizontal progress bar not located in current window decor");
      }
      showProgressBars(localProgressBar2, localProgressBar1);
    }
  }
  
  public void addContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    if (mContentParent == null) {
      installDecor();
    }
    if (hasFeature(12)) {
      Log.v("PhoneWindow", "addContentView does not support content transitions");
    }
    mContentParent.addView(paramView, paramLayoutParams);
    mContentParent.requestApplyInsets();
    paramView = getCallback();
    if ((paramView != null) && (!isDestroyed())) {
      paramView.onContentChanged();
    }
  }
  
  public void alwaysReadCloseOnTouchAttr()
  {
    mAlwaysReadCloseOnTouchAttr = true;
  }
  
  void checkCloseActionMenu(Menu paramMenu)
  {
    if (mClosingActionMenu) {
      return;
    }
    mClosingActionMenu = true;
    mDecorContentParent.dismissPopups();
    Window.Callback localCallback = getCallback();
    if ((localCallback != null) && (!isDestroyed())) {
      localCallback.onPanelClosed(8, paramMenu);
    }
    mClosingActionMenu = false;
  }
  
  public void clearContentView()
  {
    if (mDecor != null) {
      mDecor.clearContentView();
    }
  }
  
  public final void closeAllPanels()
  {
    if (getWindowManager() == null) {
      return;
    }
    PanelFeatureState[] arrayOfPanelFeatureState = mPanels;
    int i = 0;
    int j;
    if (arrayOfPanelFeatureState != null) {
      j = arrayOfPanelFeatureState.length;
    } else {
      j = 0;
    }
    while (i < j)
    {
      PanelFeatureState localPanelFeatureState = arrayOfPanelFeatureState[i];
      if (localPanelFeatureState != null) {
        closePanel(localPanelFeatureState, true);
      }
      i++;
    }
    closeContextMenu();
  }
  
  public final void closePanel(int paramInt)
  {
    if ((paramInt == 0) && (mDecorContentParent != null) && (mDecorContentParent.canShowOverflowMenu()) && (!ViewConfiguration.get(getContext()).hasPermanentMenuKey())) {
      mDecorContentParent.hideOverflowMenu();
    } else if (paramInt == 6) {
      closeContextMenu();
    } else {
      closePanel(getPanelState(paramInt, true), true);
    }
  }
  
  public final void closePanel(PanelFeatureState paramPanelFeatureState, boolean paramBoolean)
  {
    if ((paramBoolean) && (featureId == 0) && (mDecorContentParent != null) && (mDecorContentParent.isOverflowMenuShowing()))
    {
      checkCloseActionMenu(menu);
      return;
    }
    WindowManager localWindowManager = getWindowManager();
    if ((localWindowManager != null) && (isOpen))
    {
      if (decorView != null)
      {
        localWindowManager.removeView(decorView);
        if (isCompact) {
          sRotationWatcher.removeWindow(this);
        }
      }
      if (paramBoolean) {
        callOnPanelClosed(featureId, paramPanelFeatureState, null);
      }
    }
    isPrepared = false;
    isHandled = false;
    isOpen = false;
    shownPanelView = null;
    if (isInExpandedMode)
    {
      refreshDecorView = true;
      isInExpandedMode = false;
    }
    if (mPreparedPanel == paramPanelFeatureState)
    {
      mPreparedPanel = null;
      mPanelChordingKey = 0;
    }
  }
  
  void dispatchNavigationColor()
  {
    if (mDecor != null)
    {
      int i = mDecor.getNavigationColor();
      if (i == mNavigationBarColorLight) {
        mDecor.setOverrideSystemUiVisibility(16);
      } else {
        mDecor.setOverrideSystemUiVisibility(0);
      }
      if ((mCurrentNavigationBarColor == null) || (mCurrentNavigationBarColor.intValue() != i))
      {
        ViewRootImpl localViewRootImpl = mDecor.getViewRootImpl();
        if (localViewRootImpl != null) {
          localViewRootImpl.dispatchNavigationColor();
        }
        mCurrentNavigationBarColor = Integer.valueOf(i);
      }
    }
  }
  
  protected void dispatchWindowAttributesChanged(WindowManager.LayoutParams paramLayoutParams)
  {
    super.dispatchWindowAttributesChanged(paramLayoutParams);
    if (mDecor != null) {
      mDecor.updateColorViews(null, true);
    }
  }
  
  void doInvalidatePanelMenu(int paramInt)
  {
    PanelFeatureState localPanelFeatureState = getPanelState(paramInt, false);
    if (localPanelFeatureState == null) {
      return;
    }
    Object localObject;
    if (menu != null)
    {
      localObject = new Bundle();
      menu.saveActionViewStates((Bundle)localObject);
      if (((Bundle)localObject).size() > 0) {
        frozenActionViewState = ((Bundle)localObject);
      }
      menu.stopDispatchingItemsChanged();
      menu.clear();
    }
    refreshMenuContent = true;
    refreshDecorView = true;
    if (((paramInt == 8) || (paramInt == 0)) && (mDecorContentParent != null))
    {
      localObject = getPanelState(0, false);
      if (localObject != null)
      {
        isPrepared = false;
        preparePanel((PanelFeatureState)localObject, null);
      }
    }
  }
  
  void doPendingInvalidatePanelMenu()
  {
    if (mInvalidatePanelMenuPosted)
    {
      mDecor.removeCallbacks(mInvalidatePanelMenuRunnable);
      mInvalidatePanelMenuRunnable.run();
    }
  }
  
  public PanelFeatureState findMenuPanel(Menu paramMenu)
  {
    PanelFeatureState[] arrayOfPanelFeatureState = mPanels;
    int i = 0;
    int j;
    if (arrayOfPanelFeatureState != null) {
      j = arrayOfPanelFeatureState.length;
    } else {
      j = 0;
    }
    while (i < j)
    {
      PanelFeatureState localPanelFeatureState = arrayOfPanelFeatureState[i];
      if ((localPanelFeatureState != null) && (menu == paramMenu)) {
        return localPanelFeatureState;
      }
      i++;
    }
    return null;
  }
  
  protected DecorView generateDecor(int paramInt)
  {
    Object localObject;
    if (mUseDecorContext)
    {
      localObject = getContext().getApplicationContext();
      if (localObject == null)
      {
        localObject = getContext();
      }
      else
      {
        DecorContext localDecorContext = new DecorContext((Context)localObject, getContext());
        localObject = localDecorContext;
        if (mTheme != -1)
        {
          localDecorContext.setTheme(mTheme);
          localObject = localDecorContext;
        }
      }
    }
    else
    {
      localObject = getContext();
    }
    return new DecorView((Context)localObject, paramInt, this, getAttributes());
  }
  
  protected ViewGroup generateLayout(DecorView paramDecorView)
  {
    Object localObject1 = getWindowStyle();
    mIsFloating = ((TypedArray)localObject1).getBoolean(4, false);
    int i = getForcedWindowFlags() & 0x10100;
    if (mIsFloating)
    {
      setLayout(-2, -2);
      setFlags(0, i);
    }
    else
    {
      setFlags(65792, i);
    }
    if (((TypedArray)localObject1).getBoolean(3, false)) {
      requestFeature(1);
    } else if (((TypedArray)localObject1).getBoolean(15, false)) {
      requestFeature(8);
    }
    if (((TypedArray)localObject1).getBoolean(17, false)) {
      requestFeature(9);
    }
    if (((TypedArray)localObject1).getBoolean(16, false)) {
      requestFeature(10);
    }
    if (((TypedArray)localObject1).getBoolean(25, false)) {
      requestFeature(11);
    }
    if (((TypedArray)localObject1).getBoolean(9, false)) {
      setFlags(1024, getForcedWindowFlags() & 0x400);
    }
    if (((TypedArray)localObject1).getBoolean(23, false)) {
      setFlags(67108864, getForcedWindowFlags() & 0x4000000);
    }
    if (((TypedArray)localObject1).getBoolean(24, false)) {
      setFlags(134217728, getForcedWindowFlags() & 0x8000000);
    }
    if (((TypedArray)localObject1).getBoolean(22, false)) {
      setFlags(33554432, getForcedWindowFlags() & 0x2000000);
    }
    if (((TypedArray)localObject1).getBoolean(14, false)) {
      setFlags(1048576, 0x100000 & getForcedWindowFlags());
    }
    if (getContextgetApplicationInfotargetSdkVersion >= 11) {
      bool = true;
    } else {
      bool = false;
    }
    if (((TypedArray)localObject1).getBoolean(18, bool)) {
      setFlags(8388608, 0x800000 & getForcedWindowFlags());
    }
    ((TypedArray)localObject1).getValue(19, mMinWidthMajor);
    ((TypedArray)localObject1).getValue(20, mMinWidthMinor);
    if (((TypedArray)localObject1).hasValue(55))
    {
      if (mFixedWidthMajor == null) {
        mFixedWidthMajor = new TypedValue();
      }
      ((TypedArray)localObject1).getValue(55, mFixedWidthMajor);
    }
    if (((TypedArray)localObject1).hasValue(56))
    {
      if (mFixedWidthMinor == null) {
        mFixedWidthMinor = new TypedValue();
      }
      ((TypedArray)localObject1).getValue(56, mFixedWidthMinor);
    }
    if (((TypedArray)localObject1).hasValue(53))
    {
      if (mFixedHeightMajor == null) {
        mFixedHeightMajor = new TypedValue();
      }
      ((TypedArray)localObject1).getValue(53, mFixedHeightMajor);
    }
    if (((TypedArray)localObject1).hasValue(54))
    {
      if (mFixedHeightMinor == null) {
        mFixedHeightMinor = new TypedValue();
      }
      ((TypedArray)localObject1).getValue(54, mFixedHeightMinor);
    }
    if (((TypedArray)localObject1).getBoolean(26, false)) {
      requestFeature(12);
    }
    if (((TypedArray)localObject1).getBoolean(45, false)) {
      requestFeature(13);
    }
    mIsTranslucent = ((TypedArray)localObject1).getBoolean(5, false);
    Object localObject2 = getContext();
    int j = getApplicationInfotargetSdkVersion;
    if (j < 11) {
      i = 1;
    } else {
      i = 0;
    }
    if (j < 14) {
      k = 1;
    } else {
      k = 0;
    }
    if (j < 21) {
      j = 1;
    } else {
      j = 0;
    }
    boolean bool = ((Context)localObject2).getResources().getBoolean(17957119);
    int m;
    if ((hasFeature(8)) && (!hasFeature(1))) {
      m = 0;
    } else {
      m = 1;
    }
    if ((i == 0) && ((k == 0) || (!bool) || (m == 0))) {
      setNeedsMenuKey(2);
    } else {
      setNeedsMenuKey(1);
    }
    if (!mForcedStatusBarColor) {
      mStatusBarColor = ((TypedArray)localObject1).getColor(35, -16777216);
    }
    if (!mForcedNavigationBarColor)
    {
      mNavigationBarColor = ((TypedArray)localObject1).getColor(36, -16777216);
      mNavigationBarDividerColor = ((TypedArray)localObject1).getColor(50, 0);
    }
    localObject2 = getAttributes();
    if (!mIsFloating)
    {
      if ((j == 0) && (((TypedArray)localObject1).getBoolean(34, false))) {
        setFlags(Integer.MIN_VALUE, 0x80000000 & getForcedWindowFlags());
      }
      if (mDecor.mForceWindowDrawsStatusBarBackground) {
        privateFlags |= 0x20000;
      }
    }
    if (((TypedArray)localObject1).getBoolean(46, false)) {
      paramDecorView.setSystemUiVisibility(paramDecorView.getSystemUiVisibility() | 0x2000);
    }
    if (((TypedArray)localObject1).getBoolean(49, false)) {
      paramDecorView.setSystemUiVisibility(0x10 | paramDecorView.getSystemUiVisibility());
    }
    if (((TypedArray)localObject1).hasValue(51))
    {
      i = ((TypedArray)localObject1).getInt(51, -1);
      if ((i >= 0) && (i <= 2))
      {
        layoutInDisplayCutoutMode = i;
      }
      else
      {
        paramDecorView = new StringBuilder();
        paramDecorView.append("Unknown windowLayoutInDisplayCutoutMode: ");
        paramDecorView.append(((TypedArray)localObject1).getString(51));
        throw new UnsupportedOperationException(paramDecorView.toString());
      }
    }
    if (((mAlwaysReadCloseOnTouchAttr) || (getContextgetApplicationInfotargetSdkVersion >= 11)) && (((TypedArray)localObject1).getBoolean(21, false))) {
      setCloseOnTouchOutsideIfNotSet(true);
    }
    if (!hasSoftInputMode()) {
      softInputMode = ((TypedArray)localObject1).getInt(13, softInputMode);
    }
    if (((TypedArray)localObject1).getBoolean(11, mIsFloating))
    {
      if ((getForcedWindowFlags() & 0x2) == 0) {
        flags |= 0x2;
      }
      if (!haveDimAmount()) {
        dimAmount = ((TypedArray)localObject1).getFloat(0, 0.5F);
      }
    }
    if (windowAnimations == 0) {
      windowAnimations = ((TypedArray)localObject1).getResourceId(8, 0);
    }
    if (getContainer() == null)
    {
      if (mBackgroundDrawable == null)
      {
        if (mBackgroundResource == 0) {
          mBackgroundResource = ((TypedArray)localObject1).getResourceId(1, 0);
        }
        if (mFrameResource == 0) {
          mFrameResource = ((TypedArray)localObject1).getResourceId(2, 0);
        }
        mBackgroundFallbackResource = ((TypedArray)localObject1).getResourceId(47, 0);
      }
      if (mLoadElevation) {
        mElevation = ((TypedArray)localObject1).getDimension(38, 0.0F);
      }
      mClipToOutline = ((TypedArray)localObject1).getBoolean(39, false);
      mTextColor = ((TypedArray)localObject1).getColor(7, 0);
    }
    int k = getLocalFeatures();
    if ((k & 0x800) != 0)
    {
      i = 17367293;
      setCloseOnSwipeEnabled(true);
    }
    else if ((k & 0x18) != 0)
    {
      if (mIsFloating)
      {
        paramDecorView = new TypedValue();
        getContext().getTheme().resolveAttribute(17891371, paramDecorView, true);
        i = resourceId;
      }
      else
      {
        i = 17367295;
      }
      removeFeature(8);
    }
    else
    {
      if (((k & 0x24) != 0) && ((k & 0x100) == 0)) {
        i = 17367290;
      }
      for (;;)
      {
        break;
        if ((k & 0x80) != 0)
        {
          if (mIsFloating)
          {
            paramDecorView = new TypedValue();
            getContext().getTheme().resolveAttribute(17891368, paramDecorView, true);
            i = resourceId;
          }
          else
          {
            i = 17367289;
          }
          removeFeature(8);
        }
        else if ((k & 0x2) == 0)
        {
          if (mIsFloating)
          {
            paramDecorView = new TypedValue();
            getContext().getTheme().resolveAttribute(17891370, paramDecorView, true);
            i = resourceId;
          }
          else if ((k & 0x100) != 0)
          {
            i = ((TypedArray)localObject1).getResourceId(52, 17367288);
          }
          else
          {
            i = 17367294;
          }
        }
        else if ((k & 0x400) != 0)
        {
          i = 17367292;
        }
        else
        {
          i = 17367291;
        }
      }
    }
    mDecor.startChanging();
    mDecor.onResourcesLoaded(mLayoutInflater, i);
    localObject1 = (ViewGroup)findViewById(16908290);
    if (localObject1 != null)
    {
      if ((k & 0x20) != 0)
      {
        paramDecorView = getCircularProgressBar(false);
        if (paramDecorView != null) {
          paramDecorView.setIndeterminate(true);
        }
      }
      if ((k & 0x800) != 0) {
        registerSwipeCallbacks((ViewGroup)localObject1);
      }
      if (getContainer() == null)
      {
        if (mBackgroundResource != 0) {
          paramDecorView = getContext().getDrawable(mBackgroundResource);
        } else {
          paramDecorView = mBackgroundDrawable;
        }
        mDecor.setWindowBackground(paramDecorView);
        if (mFrameResource != 0) {
          paramDecorView = getContext().getDrawable(mFrameResource);
        } else {
          paramDecorView = null;
        }
        mDecor.setWindowFrame(paramDecorView);
        mDecor.setElevation(mElevation);
        mDecor.setClipToOutline(mClipToOutline);
        if (mTitle != null) {
          setTitle(mTitle);
        }
        if (mTitleColor == 0) {
          mTitleColor = mTextColor;
        }
        setTitleColor(mTitleColor);
      }
      mDecor.finishChanging();
      return localObject1;
    }
    throw new RuntimeException("Window couldn't find content container view");
  }
  
  public boolean getAllowEnterTransitionOverlap()
  {
    boolean bool;
    if (mAllowEnterTransitionOverlap == null) {
      bool = true;
    } else {
      bool = mAllowEnterTransitionOverlap.booleanValue();
    }
    return bool;
  }
  
  public boolean getAllowReturnTransitionOverlap()
  {
    boolean bool;
    if (mAllowReturnTransitionOverlap == null) {
      bool = true;
    } else {
      bool = mAllowReturnTransitionOverlap.booleanValue();
    }
    return bool;
  }
  
  AudioManager getAudioManager()
  {
    if (mAudioManager == null) {
      mAudioManager = ((AudioManager)getContext().getSystemService("audio"));
    }
    return mAudioManager;
  }
  
  public Scene getContentScene()
  {
    return mContentScene;
  }
  
  public View getCurrentFocus()
  {
    View localView;
    if (mDecor != null) {
      localView = mDecor.findFocus();
    } else {
      localView = null;
    }
    return localView;
  }
  
  int getDecorCaptionShade()
  {
    return mDecorCaptionShade;
  }
  
  public final View getDecorView()
  {
    if ((mDecor == null) || (mForceDecorInstall)) {
      installDecor();
    }
    return mDecor;
  }
  
  public float getElevation()
  {
    return mElevation;
  }
  
  public Transition getEnterTransition()
  {
    return mEnterTransition;
  }
  
  public Transition getExitTransition()
  {
    return mExitTransition;
  }
  
  public LayoutInflater getLayoutInflater()
  {
    return mLayoutInflater;
  }
  
  int getLocalFeaturesPrivate()
  {
    return super.getLocalFeatures();
  }
  
  public MediaController getMediaController()
  {
    return mMediaController;
  }
  
  public int getNavigationBarColor()
  {
    return mNavigationBarColor;
  }
  
  public int getNavigationBarDividerColor()
  {
    return mNavigationBarDividerColor;
  }
  
  PanelFeatureState getPanelState(int paramInt, boolean paramBoolean)
  {
    return getPanelState(paramInt, paramBoolean, null);
  }
  
  public Transition getReenterTransition()
  {
    Transition localTransition;
    if (mReenterTransition == USE_DEFAULT_TRANSITION) {
      localTransition = getExitTransition();
    } else {
      localTransition = mReenterTransition;
    }
    return localTransition;
  }
  
  public Transition getReturnTransition()
  {
    Transition localTransition;
    if (mReturnTransition == USE_DEFAULT_TRANSITION) {
      localTransition = getEnterTransition();
    } else {
      localTransition = mReturnTransition;
    }
    return localTransition;
  }
  
  public Transition getSharedElementEnterTransition()
  {
    return mSharedElementEnterTransition;
  }
  
  public Transition getSharedElementExitTransition()
  {
    return mSharedElementExitTransition;
  }
  
  public Transition getSharedElementReenterTransition()
  {
    Transition localTransition;
    if (mSharedElementReenterTransition == USE_DEFAULT_TRANSITION) {
      localTransition = getSharedElementExitTransition();
    } else {
      localTransition = mSharedElementReenterTransition;
    }
    return localTransition;
  }
  
  public Transition getSharedElementReturnTransition()
  {
    Transition localTransition;
    if (mSharedElementReturnTransition == USE_DEFAULT_TRANSITION) {
      localTransition = getSharedElementEnterTransition();
    } else {
      localTransition = mSharedElementReturnTransition;
    }
    return localTransition;
  }
  
  public boolean getSharedElementsUseOverlay()
  {
    boolean bool;
    if (mSharedElementsUseOverlay == null) {
      bool = true;
    } else {
      bool = mSharedElementsUseOverlay.booleanValue();
    }
    return bool;
  }
  
  public int getStatusBarColor()
  {
    return mStatusBarColor;
  }
  
  public long getTransitionBackgroundFadeDuration()
  {
    long l;
    if (mBackgroundFadeDurationMillis < 0L) {
      l = 300L;
    } else {
      l = mBackgroundFadeDurationMillis;
    }
    return l;
  }
  
  public TransitionManager getTransitionManager()
  {
    return mTransitionManager;
  }
  
  public int getVolumeControlStream()
  {
    return mVolumeControlStreamType;
  }
  
  protected boolean initializePanelContent(PanelFeatureState paramPanelFeatureState)
  {
    if (createdPanelView != null)
    {
      shownPanelView = createdPanelView;
      return true;
    }
    if (menu == null) {
      return false;
    }
    if (mPanelMenuPresenterCallback == null) {
      mPanelMenuPresenterCallback = new PanelMenuPresenterCallback(null);
    }
    MenuView localMenuView;
    if (paramPanelFeatureState.isInListMode()) {
      localMenuView = paramPanelFeatureState.getListMenuView(getContext(), mPanelMenuPresenterCallback);
    } else {
      localMenuView = paramPanelFeatureState.getIconMenuView(getContext(), mPanelMenuPresenterCallback);
    }
    shownPanelView = ((View)localMenuView);
    if (shownPanelView != null)
    {
      int i = localMenuView.getWindowAnimations();
      if (i != 0) {
        windowAnimations = i;
      }
      return true;
    }
    return false;
  }
  
  protected boolean initializePanelDecor(PanelFeatureState paramPanelFeatureState)
  {
    decorView = generateDecor(featureId);
    gravity = 81;
    paramPanelFeatureState.setStyle(getContext());
    TypedArray localTypedArray = getContext().obtainStyledAttributes(null, R.styleable.Window, 0, listPresenterTheme);
    float f = localTypedArray.getDimension(38, 0.0F);
    if (f != 0.0F) {
      decorView.setElevation(f);
    }
    localTypedArray.recycle();
    return true;
  }
  
  protected boolean initializePanelMenu(PanelFeatureState paramPanelFeatureState)
  {
    Context localContext = getContext();
    if (featureId != 0)
    {
      localObject1 = localContext;
      if (featureId != 8) {}
    }
    else
    {
      localObject1 = localContext;
      if (mDecorContentParent != null)
      {
        TypedValue localTypedValue = new TypedValue();
        Resources.Theme localTheme = localContext.getTheme();
        localTheme.resolveAttribute(16843825, localTypedValue, true);
        localObject1 = null;
        if (resourceId != 0)
        {
          localObject1 = localContext.getResources().newTheme();
          ((Resources.Theme)localObject1).setTo(localTheme);
          ((Resources.Theme)localObject1).applyStyle(resourceId, true);
          ((Resources.Theme)localObject1).resolveAttribute(16843671, localTypedValue, true);
        }
        else
        {
          localTheme.resolveAttribute(16843671, localTypedValue, true);
        }
        Object localObject2 = localObject1;
        if (resourceId != 0)
        {
          localObject2 = localObject1;
          if (localObject1 == null)
          {
            localObject2 = localContext.getResources().newTheme();
            ((Resources.Theme)localObject2).setTo(localTheme);
          }
          ((Resources.Theme)localObject2).applyStyle(resourceId, true);
        }
        localObject1 = localContext;
        if (localObject2 != null)
        {
          localObject1 = new ContextThemeWrapper(localContext, 0);
          ((Context)localObject1).getTheme().setTo((Resources.Theme)localObject2);
        }
      }
    }
    Object localObject1 = new MenuBuilder((Context)localObject1);
    ((MenuBuilder)localObject1).setCallback(this);
    paramPanelFeatureState.setMenu((MenuBuilder)localObject1);
    return true;
  }
  
  public void injectInputEvent(InputEvent paramInputEvent)
  {
    getViewRootImpl().dispatchInputEvent(paramInputEvent);
  }
  
  public void invalidatePanelMenu(int paramInt)
  {
    mInvalidatePanelMenuFeatures |= 1 << paramInt;
    if ((!mInvalidatePanelMenuPosted) && (mDecor != null))
    {
      mDecor.postOnAnimation(mInvalidatePanelMenuRunnable);
      mInvalidatePanelMenuPosted = true;
    }
  }
  
  public boolean isFloating()
  {
    return mIsFloating;
  }
  
  public boolean isShortcutKey(int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool1 = false;
    PanelFeatureState localPanelFeatureState = getPanelState(0, false);
    boolean bool2 = bool1;
    if (localPanelFeatureState != null)
    {
      bool2 = bool1;
      if (menu != null)
      {
        bool2 = bool1;
        if (menu.isShortcutKey(paramInt, paramKeyEvent)) {
          bool2 = true;
        }
      }
    }
    return bool2;
  }
  
  boolean isShowingWallpaper()
  {
    boolean bool;
    if ((getAttributesflags & 0x100000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isTranslucent()
  {
    return mIsTranslucent;
  }
  
  protected void onActive() {}
  
  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    if (mDecorContentParent == null)
    {
      paramConfiguration = getPanelState(0, false);
      if ((paramConfiguration != null) && (menu != null)) {
        if (isOpen)
        {
          Bundle localBundle = new Bundle();
          if (iconMenuPresenter != null) {
            iconMenuPresenter.saveHierarchyState(localBundle);
          }
          if (listMenuPresenter != null) {
            listMenuPresenter.saveHierarchyState(localBundle);
          }
          clearMenuViews(paramConfiguration);
          reopenMenu(false);
          if (iconMenuPresenter != null) {
            iconMenuPresenter.restoreHierarchyState(localBundle);
          }
          if (listMenuPresenter != null) {
            listMenuPresenter.restoreHierarchyState(localBundle);
          }
        }
        else
        {
          clearMenuViews(paramConfiguration);
        }
      }
    }
  }
  
  protected void onDrawableChanged(int paramInt1, Drawable paramDrawable, int paramInt2)
  {
    if (paramInt1 == 3) {}
    for (ImageView localImageView = getLeftIconView();; localImageView = getRightIconView())
    {
      break;
      if (paramInt1 != 4) {
        return;
      }
    }
    if (paramDrawable != null)
    {
      paramDrawable.setAlpha(paramInt2);
      localImageView.setImageDrawable(paramDrawable);
      localImageView.setVisibility(0);
    }
    else
    {
      localImageView.setVisibility(8);
    }
    return;
  }
  
  protected void onIntChanged(int paramInt1, int paramInt2)
  {
    if ((paramInt1 != 2) && (paramInt1 != 5))
    {
      if (paramInt1 == 7)
      {
        FrameLayout localFrameLayout = (FrameLayout)findViewById(16909478);
        if (localFrameLayout != null) {
          mLayoutInflater.inflate(paramInt2, localFrameLayout);
        }
      }
    }
    else {
      updateProgressBars(paramInt2);
    }
  }
  
  protected boolean onKeyDown(int paramInt1, int paramInt2, KeyEvent paramKeyEvent)
  {
    KeyEvent.DispatcherState localDispatcherState;
    if (mDecor != null) {
      localDispatcherState = mDecor.getKeyDispatcherState();
    } else {
      localDispatcherState = null;
    }
    int i = 0;
    if (paramInt2 != 4)
    {
      if (paramInt2 != 79) {
        if (paramInt2 != 82)
        {
          if (paramInt2 != 130)
          {
            if (paramInt2 != 164) {
              switch (paramInt2)
              {
              default: 
                switch (paramInt2)
                {
                default: 
                  switch (paramInt2)
                  {
                  }
                  break;
                }
                break;
              }
            }
            if (mMediaController != null) {
              mMediaController.dispatchVolumeButtonEventAsSystemService(paramKeyEvent);
            } else {
              getMediaSessionManager().dispatchVolumeKeyEventAsSystemService(paramKeyEvent, mVolumeControlStreamType);
            }
            return true;
          }
        }
        else
        {
          if (paramInt1 < 0) {
            paramInt1 = i;
          }
          onKeyDownPanel(paramInt1, paramKeyEvent);
          return true;
        }
      }
      return (mMediaController != null) && (mMediaController.dispatchMediaButtonEventAsSystemService(paramKeyEvent));
    }
    else
    {
      if ((paramKeyEvent.getRepeatCount() <= 0) && (paramInt1 >= 0)) {
        break label240;
      }
    }
    return false;
    label240:
    if (localDispatcherState != null) {
      localDispatcherState.startTracking(paramKeyEvent, this);
    }
    return true;
  }
  
  public final boolean onKeyDownPanel(int paramInt, KeyEvent paramKeyEvent)
  {
    int i = paramKeyEvent.getKeyCode();
    if (paramKeyEvent.getRepeatCount() == 0)
    {
      mPanelChordingKey = i;
      PanelFeatureState localPanelFeatureState = getPanelState(paramInt, false);
      if ((localPanelFeatureState != null) && (!isOpen)) {
        return preparePanel(localPanelFeatureState, paramKeyEvent);
      }
    }
    return false;
  }
  
  protected boolean onKeyUp(int paramInt1, int paramInt2, KeyEvent paramKeyEvent)
  {
    KeyEvent.DispatcherState localDispatcherState;
    if (mDecor != null) {
      localDispatcherState = mDecor.getKeyDispatcherState();
    } else {
      localDispatcherState = null;
    }
    if (localDispatcherState != null) {
      localDispatcherState.handleUpEvent(paramKeyEvent);
    }
    int i = 0;
    if (paramInt2 != 4)
    {
      if (paramInt2 != 79) {
        if (paramInt2 != 82)
        {
          if (paramInt2 != 130)
          {
            if (paramInt2 != 164)
            {
              if (paramInt2 != 171) {
                switch (paramInt2)
                {
                default: 
                  switch (paramInt2)
                  {
                  default: 
                    switch (paramInt2)
                    {
                    }
                    break;
                  case 84: 
                    if ((isNotInstantAppAndKeyguardRestricted()) || ((getContextgetResourcesgetConfigurationuiMode & 0xF) == 6)) {
                      break;
                    }
                    if ((paramKeyEvent.isTracking()) && (!paramKeyEvent.isCanceled())) {
                      launchDefaultSearch(paramKeyEvent);
                    }
                    return true;
                  }
                  break;
                case 24: 
                case 25: 
                  if (mMediaController != null) {
                    mMediaController.dispatchVolumeButtonEventAsSystemService(paramKeyEvent);
                  } else {
                    getMediaSessionManager().dispatchVolumeKeyEventAsSystemService(paramKeyEvent, mVolumeControlStreamType);
                  }
                  return true;
                }
              }
              if ((mSupportsPictureInPicture) && (!paramKeyEvent.isCanceled())) {
                getWindowControllerCallback().enterPictureInPictureModeIfPossible();
              }
              return true;
            }
            getMediaSessionManager().dispatchVolumeKeyEventAsSystemService(paramKeyEvent, Integer.MIN_VALUE);
            return true;
          }
        }
        else
        {
          if (paramInt1 < 0) {
            paramInt1 = i;
          }
          onKeyUpPanel(paramInt1, paramKeyEvent);
          return true;
        }
      }
      return (mMediaController != null) && (mMediaController.dispatchMediaButtonEventAsSystemService(paramKeyEvent));
    }
    else if ((paramInt1 >= 0) && (paramKeyEvent.isTracking()) && (!paramKeyEvent.isCanceled()))
    {
      if (paramInt1 == 0)
      {
        paramKeyEvent = getPanelState(paramInt1, false);
        if ((paramKeyEvent != null) && (isInExpandedMode))
        {
          reopenMenu(true);
          return true;
        }
      }
      closePanel(paramInt1);
      return true;
    }
    return false;
  }
  
  public final void onKeyUpPanel(int paramInt, KeyEvent paramKeyEvent)
  {
    if (mPanelChordingKey != 0)
    {
      mPanelChordingKey = 0;
      PanelFeatureState localPanelFeatureState = getPanelState(paramInt, false);
      if ((!paramKeyEvent.isCanceled()) && ((mDecor == null) || (mDecor.mPrimaryActionMode == null)) && (localPanelFeatureState != null))
      {
        boolean bool1 = false;
        boolean bool2;
        if ((paramInt == 0) && (mDecorContentParent != null) && (mDecorContentParent.canShowOverflowMenu()) && (!ViewConfiguration.get(getContext()).hasPermanentMenuKey()))
        {
          if (!mDecorContentParent.isOverflowMenuShowing())
          {
            bool2 = bool1;
            if (!isDestroyed())
            {
              bool2 = bool1;
              if (preparePanel(localPanelFeatureState, paramKeyEvent)) {
                bool2 = mDecorContentParent.showOverflowMenu();
              }
            }
          }
          else
          {
            bool2 = mDecorContentParent.hideOverflowMenu();
          }
        }
        else if ((!isOpen) && (!isHandled))
        {
          bool2 = bool1;
          if (isPrepared)
          {
            boolean bool3 = true;
            if (refreshMenuContent)
            {
              isPrepared = false;
              bool3 = preparePanel(localPanelFeatureState, paramKeyEvent);
            }
            bool2 = bool1;
            if (bool3)
            {
              EventLog.writeEvent(50001, 0);
              openPanel(localPanelFeatureState, paramKeyEvent);
              bool2 = true;
            }
          }
        }
        else
        {
          bool2 = isOpen;
          closePanel(localPanelFeatureState, true);
        }
        if (bool2)
        {
          paramKeyEvent = (AudioManager)getContext().getSystemService("audio");
          if (paramKeyEvent != null) {
            paramKeyEvent.playSoundEffect(0);
          } else {
            Log.w("PhoneWindow", "Couldn't get audio manager");
          }
        }
      }
      else {}
    }
  }
  
  public boolean onMenuItemSelected(MenuBuilder paramMenuBuilder, MenuItem paramMenuItem)
  {
    Window.Callback localCallback = getCallback();
    if ((localCallback != null) && (!isDestroyed()))
    {
      paramMenuBuilder = findMenuPanel(paramMenuBuilder.getRootMenu());
      if (paramMenuBuilder != null) {
        return localCallback.onMenuItemSelected(featureId, paramMenuItem);
      }
    }
    return false;
  }
  
  public void onMenuModeChange(MenuBuilder paramMenuBuilder)
  {
    reopenMenu(true);
  }
  
  public void onMultiWindowModeChanged()
  {
    if (mDecor != null) {
      mDecor.onConfigurationChanged(getContext().getResources().getConfiguration());
    }
  }
  
  void onOptionsPanelRotationChanged()
  {
    PanelFeatureState localPanelFeatureState = getPanelState(0, false);
    if (localPanelFeatureState == null) {
      return;
    }
    WindowManager.LayoutParams localLayoutParams;
    if (decorView != null) {
      localLayoutParams = (WindowManager.LayoutParams)decorView.getLayoutParams();
    } else {
      localLayoutParams = null;
    }
    if (localLayoutParams != null)
    {
      gravity = getOptionsPanelGravity();
      WindowManager localWindowManager = getWindowManager();
      if (localWindowManager != null) {
        localWindowManager.updateViewLayout(decorView, localLayoutParams);
      }
    }
  }
  
  public void onPictureInPictureModeChanged(boolean paramBoolean)
  {
    if (mDecor != null) {
      mDecor.updatePictureInPictureOutlineProvider(paramBoolean);
    }
  }
  
  void onViewRootImplSet(ViewRootImpl paramViewRootImpl)
  {
    paramViewRootImpl.setActivityConfigCallback(mActivityConfigCallback);
  }
  
  public final void openPanel(int paramInt, KeyEvent paramKeyEvent)
  {
    if ((paramInt == 0) && (mDecorContentParent != null) && (mDecorContentParent.canShowOverflowMenu()) && (!ViewConfiguration.get(getContext()).hasPermanentMenuKey())) {
      mDecorContentParent.showOverflowMenu();
    } else {
      openPanel(getPanelState(paramInt, true), paramKeyEvent);
    }
  }
  
  void openPanelsAfterRestore()
  {
    PanelFeatureState[] arrayOfPanelFeatureState = mPanels;
    if (arrayOfPanelFeatureState == null) {
      return;
    }
    for (int i = arrayOfPanelFeatureState.length - 1; i >= 0; i--)
    {
      PanelFeatureState localPanelFeatureState = arrayOfPanelFeatureState[i];
      if (localPanelFeatureState != null)
      {
        localPanelFeatureState.applyFrozenState();
        if ((!isOpen) && (wasLastOpen))
        {
          isInExpandedMode = wasLastExpanded;
          openPanel(localPanelFeatureState, null);
        }
      }
    }
  }
  
  public final View peekDecorView()
  {
    return mDecor;
  }
  
  public boolean performContextMenuIdentifierAction(int paramInt1, int paramInt2)
  {
    boolean bool;
    if (mContextMenu != null) {
      bool = mContextMenu.performIdentifierAction(paramInt1, paramInt2);
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean performPanelIdentifierAction(int paramInt1, int paramInt2, int paramInt3)
  {
    PanelFeatureState localPanelFeatureState = getPanelState(paramInt1, true);
    if (!preparePanel(localPanelFeatureState, new KeyEvent(0, 82))) {
      return false;
    }
    if (menu == null) {
      return false;
    }
    boolean bool = menu.performIdentifierAction(paramInt2, paramInt3);
    if (mDecorContentParent == null) {
      closePanel(localPanelFeatureState, true);
    }
    return bool;
  }
  
  public boolean performPanelShortcut(int paramInt1, int paramInt2, KeyEvent paramKeyEvent, int paramInt3)
  {
    return performPanelShortcut(getPanelState(paramInt1, false), paramInt2, paramKeyEvent, paramInt3);
  }
  
  boolean performPanelShortcut(PanelFeatureState paramPanelFeatureState, int paramInt1, KeyEvent paramKeyEvent, int paramInt2)
  {
    if ((!paramKeyEvent.isSystem()) && (paramPanelFeatureState != null))
    {
      boolean bool1 = false;
      boolean bool2;
      if (!isPrepared)
      {
        bool2 = bool1;
        if (!preparePanel(paramPanelFeatureState, paramKeyEvent)) {}
      }
      else
      {
        bool2 = bool1;
        if (menu != null) {
          bool2 = menu.performShortcut(paramInt1, paramKeyEvent, paramInt2);
        }
      }
      if (bool2)
      {
        isHandled = true;
        if (((paramInt2 & 0x1) == 0) && (mDecorContentParent == null)) {
          closePanel(paramPanelFeatureState, true);
        }
      }
      return bool2;
    }
    return false;
  }
  
  public final boolean preparePanel(PanelFeatureState paramPanelFeatureState, KeyEvent paramKeyEvent)
  {
    if (isDestroyed()) {
      return false;
    }
    if (isPrepared) {
      return true;
    }
    if ((mPreparedPanel != null) && (mPreparedPanel != paramPanelFeatureState)) {
      closePanel(mPreparedPanel, false);
    }
    Window.Callback localCallback = getCallback();
    if (localCallback != null) {
      createdPanelView = localCallback.onCreatePanelView(featureId);
    }
    int i;
    if ((featureId != 0) && (featureId != 8)) {
      i = 0;
    } else {
      i = 1;
    }
    if ((i != 0) && (mDecorContentParent != null)) {
      mDecorContentParent.setMenuPrepared();
    }
    if (createdPanelView == null)
    {
      if ((menu == null) || (refreshMenuContent))
      {
        if ((menu == null) && ((!initializePanelMenu(paramPanelFeatureState)) || (menu == null))) {
          return false;
        }
        if ((i != 0) && (mDecorContentParent != null))
        {
          if (mActionMenuPresenterCallback == null) {
            mActionMenuPresenterCallback = new ActionMenuPresenterCallback(null);
          }
          mDecorContentParent.setMenu(menu, mActionMenuPresenterCallback);
        }
        menu.stopDispatchingItemsChanged();
        if ((localCallback != null) && (localCallback.onCreatePanelMenu(featureId, menu))) {
          refreshMenuContent = false;
        }
      }
      else
      {
        menu.stopDispatchingItemsChanged();
        if (frozenActionViewState != null)
        {
          menu.restoreActionViewStates(frozenActionViewState);
          frozenActionViewState = null;
        }
        if (!localCallback.onPreparePanel(featureId, createdPanelView, menu))
        {
          if ((i != 0) && (mDecorContentParent != null)) {
            mDecorContentParent.setMenu(null, mActionMenuPresenterCallback);
          }
          menu.startDispatchingItemsChanged();
          return false;
        }
        if (paramKeyEvent != null) {
          i = paramKeyEvent.getDeviceId();
        } else {
          i = -1;
        }
        boolean bool;
        if (KeyCharacterMap.load(i).getKeyboardType() != 1) {
          bool = true;
        } else {
          bool = false;
        }
        qwertyMode = bool;
        menu.setQwertyMode(qwertyMode);
        menu.startDispatchingItemsChanged();
        break label427;
      }
      paramPanelFeatureState.setMenu(null);
      if ((i != 0) && (mDecorContentParent != null)) {
        mDecorContentParent.setMenu(null, mActionMenuPresenterCallback);
      }
      return false;
    }
    label427:
    isPrepared = true;
    isHandled = false;
    mPreparedPanel = paramPanelFeatureState;
    return true;
  }
  
  public void reportActivityRelaunched()
  {
    if ((mDecor != null) && (mDecor.getViewRootImpl() != null)) {
      mDecor.getViewRootImpl().reportActivityRelaunched();
    }
  }
  
  public boolean requestFeature(int paramInt)
  {
    if (!mContentParentExplicitlySet)
    {
      int i = getFeatures();
      int j = 1 << paramInt | i;
      if (((j & 0x80) != 0) && ((j & 0xCB3E) != 0)) {
        throw new AndroidRuntimeException("You cannot combine custom titles with other title features");
      }
      if (((i & 0x2) != 0) && (paramInt == 8)) {
        return false;
      }
      if (((i & 0x100) != 0) && (paramInt == 1)) {
        removeFeature(8);
      }
      if (((i & 0x100) != 0) && (paramInt == 11)) {
        throw new AndroidRuntimeException("You cannot combine swipe dismissal and the action bar.");
      }
      if (((i & 0x800) != 0) && (paramInt == 8)) {
        throw new AndroidRuntimeException("You cannot combine swipe dismissal and the action bar.");
      }
      if ((paramInt == 5) && (getContext().getPackageManager().hasSystemFeature("android.hardware.type.watch"))) {
        throw new AndroidRuntimeException("You cannot use indeterminate progress on a watch.");
      }
      return super.requestFeature(paramInt);
    }
    throw new AndroidRuntimeException("requestFeature() must be called before adding content");
  }
  
  public void restoreHierarchyState(Bundle paramBundle)
  {
    if (mContentParent == null) {
      return;
    }
    Object localObject = paramBundle.getSparseParcelableArray("android:views");
    if (localObject != null) {
      mContentParent.restoreHierarchyState((SparseArray)localObject);
    }
    int i = paramBundle.getInt("android:focusedViewId", -1);
    if (i != -1)
    {
      localObject = mContentParent.findViewById(i);
      if (localObject != null)
      {
        ((View)localObject).requestFocus();
      }
      else
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Previously focused view reported id ");
        ((StringBuilder)localObject).append(i);
        ((StringBuilder)localObject).append(" during save, but can't be found during restore.");
        Log.w("PhoneWindow", ((StringBuilder)localObject).toString());
      }
    }
    localObject = paramBundle.getSparseParcelableArray("android:Panels");
    if (localObject != null) {
      restorePanelState((SparseArray)localObject);
    }
    if (mDecorContentParent != null)
    {
      paramBundle = paramBundle.getSparseParcelableArray("android:ActionBar");
      if (paramBundle != null)
      {
        doPendingInvalidatePanelMenu();
        mDecorContentParent.restoreToolbarHierarchyState(paramBundle);
      }
      else
      {
        Log.w("PhoneWindow", "Missing saved instance states for action bar views! State will not be restored.");
      }
    }
  }
  
  public Bundle saveHierarchyState()
  {
    Bundle localBundle = new Bundle();
    if (mContentParent == null) {
      return localBundle;
    }
    Object localObject = new SparseArray();
    mContentParent.saveHierarchyState((SparseArray)localObject);
    localBundle.putSparseParcelableArray("android:views", (SparseArray)localObject);
    localObject = mContentParent.findFocus();
    if ((localObject != null) && (((View)localObject).getId() != -1)) {
      localBundle.putInt("android:focusedViewId", ((View)localObject).getId());
    }
    localObject = new SparseArray();
    savePanelState((SparseArray)localObject);
    if (((SparseArray)localObject).size() > 0) {
      localBundle.putSparseParcelableArray("android:Panels", (SparseArray)localObject);
    }
    if (mDecorContentParent != null)
    {
      localObject = new SparseArray();
      mDecorContentParent.saveToolbarHierarchyState((SparseArray)localObject);
      localBundle.putSparseParcelableArray("android:ActionBar", (SparseArray)localObject);
    }
    return localBundle;
  }
  
  void sendCloseSystemWindows()
  {
    sendCloseSystemWindows(getContext(), null);
  }
  
  void sendCloseSystemWindows(String paramString)
  {
    sendCloseSystemWindows(getContext(), paramString);
  }
  
  public void setAllowEnterTransitionOverlap(boolean paramBoolean)
  {
    mAllowEnterTransitionOverlap = Boolean.valueOf(paramBoolean);
  }
  
  public void setAllowReturnTransitionOverlap(boolean paramBoolean)
  {
    mAllowReturnTransitionOverlap = Boolean.valueOf(paramBoolean);
  }
  
  public void setAttributes(WindowManager.LayoutParams paramLayoutParams)
  {
    super.setAttributes(paramLayoutParams);
    if (mDecor != null) {
      mDecor.updateLogTag(paramLayoutParams);
    }
  }
  
  public final void setBackgroundDrawable(Drawable paramDrawable)
  {
    if ((paramDrawable != mBackgroundDrawable) || (mBackgroundResource != 0))
    {
      int i = 0;
      mBackgroundResource = 0;
      mBackgroundDrawable = paramDrawable;
      if (mDecor != null) {
        mDecor.setWindowBackground(paramDrawable);
      }
      if (mBackgroundFallbackResource != 0)
      {
        DecorView localDecorView = mDecor;
        if (paramDrawable == null) {
          i = mBackgroundFallbackResource;
        }
        localDecorView.setBackgroundFallback(i);
      }
    }
  }
  
  public final void setChildDrawable(int paramInt, Drawable paramDrawable)
  {
    DrawableFeatureState localDrawableFeatureState = getDrawableState(paramInt, true);
    child = paramDrawable;
    updateDrawable(paramInt, localDrawableFeatureState, false);
  }
  
  public final void setChildInt(int paramInt1, int paramInt2)
  {
    updateInt(paramInt1, paramInt2, false);
  }
  
  public final void setClipToOutline(boolean paramBoolean)
  {
    mClipToOutline = paramBoolean;
    if (mDecor != null) {
      mDecor.setClipToOutline(paramBoolean);
    }
  }
  
  public void setCloseOnSwipeEnabled(boolean paramBoolean)
  {
    if ((hasFeature(11)) && ((mContentParent instanceof SwipeDismissLayout))) {
      ((SwipeDismissLayout)mContentParent).setDismissable(paramBoolean);
    }
    super.setCloseOnSwipeEnabled(paramBoolean);
  }
  
  public final void setContainer(Window paramWindow)
  {
    super.setContainer(paramWindow);
  }
  
  public void setContentView(int paramInt)
  {
    if (mContentParent == null) {
      installDecor();
    } else if (!hasFeature(12)) {
      mContentParent.removeAllViews();
    }
    if (hasFeature(12)) {
      transitionTo(Scene.getSceneForLayout(mContentParent, paramInt, getContext()));
    } else {
      mLayoutInflater.inflate(paramInt, mContentParent);
    }
    mContentParent.requestApplyInsets();
    Window.Callback localCallback = getCallback();
    if ((localCallback != null) && (!isDestroyed())) {
      localCallback.onContentChanged();
    }
    mContentParentExplicitlySet = true;
  }
  
  public void setContentView(View paramView)
  {
    setContentView(paramView, new ViewGroup.LayoutParams(-1, -1));
  }
  
  public void setContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    if (mContentParent == null) {
      installDecor();
    } else if (!hasFeature(12)) {
      mContentParent.removeAllViews();
    }
    if (hasFeature(12))
    {
      paramView.setLayoutParams(paramLayoutParams);
      transitionTo(new Scene(mContentParent, paramView));
    }
    else
    {
      mContentParent.addView(paramView, paramLayoutParams);
    }
    mContentParent.requestApplyInsets();
    paramView = getCallback();
    if ((paramView != null) && (!isDestroyed())) {
      paramView.onContentChanged();
    }
    mContentParentExplicitlySet = true;
  }
  
  public void setDecorCaptionShade(int paramInt)
  {
    mDecorCaptionShade = paramInt;
    if (mDecor != null) {
      mDecor.updateDecorCaptionShade();
    }
  }
  
  public void setDefaultIcon(int paramInt)
  {
    if ((mResourcesSetFlags & 0x1) != 0) {
      return;
    }
    mIconRes = paramInt;
    if ((mDecorContentParent != null) && ((!mDecorContentParent.hasIcon()) || ((mResourcesSetFlags & 0x4) != 0))) {
      if (paramInt != 0)
      {
        mDecorContentParent.setIcon(paramInt);
        mResourcesSetFlags &= 0xFFFFFFFB;
      }
      else
      {
        mDecorContentParent.setIcon(getContext().getPackageManager().getDefaultActivityIcon());
        mResourcesSetFlags |= 0x4;
      }
    }
  }
  
  public void setDefaultLogo(int paramInt)
  {
    if ((mResourcesSetFlags & 0x2) != 0) {
      return;
    }
    mLogoRes = paramInt;
    if ((mDecorContentParent != null) && (!mDecorContentParent.hasLogo())) {
      mDecorContentParent.setLogo(paramInt);
    }
  }
  
  protected void setDefaultWindowFormat(int paramInt)
  {
    super.setDefaultWindowFormat(paramInt);
  }
  
  public final void setElevation(float paramFloat)
  {
    mElevation = paramFloat;
    WindowManager.LayoutParams localLayoutParams = getAttributes();
    if (mDecor != null)
    {
      mDecor.setElevation(paramFloat);
      localLayoutParams.setSurfaceInsets(mDecor, true, false);
    }
    dispatchWindowAttributesChanged(localLayoutParams);
  }
  
  public void setEnterTransition(Transition paramTransition)
  {
    mEnterTransition = paramTransition;
  }
  
  public void setExitTransition(Transition paramTransition)
  {
    mExitTransition = paramTransition;
  }
  
  protected final void setFeatureDefaultDrawable(int paramInt, Drawable paramDrawable)
  {
    DrawableFeatureState localDrawableFeatureState = getDrawableState(paramInt, true);
    if (def != paramDrawable)
    {
      def = paramDrawable;
      updateDrawable(paramInt, localDrawableFeatureState, false);
    }
  }
  
  public final void setFeatureDrawable(int paramInt, Drawable paramDrawable)
  {
    DrawableFeatureState localDrawableFeatureState = getDrawableState(paramInt, true);
    resid = 0;
    uri = null;
    if (local != paramDrawable)
    {
      local = paramDrawable;
      updateDrawable(paramInt, localDrawableFeatureState, false);
    }
  }
  
  public void setFeatureDrawableAlpha(int paramInt1, int paramInt2)
  {
    DrawableFeatureState localDrawableFeatureState = getDrawableState(paramInt1, true);
    if (alpha != paramInt2)
    {
      alpha = paramInt2;
      updateDrawable(paramInt1, localDrawableFeatureState, false);
    }
  }
  
  public final void setFeatureDrawableResource(int paramInt1, int paramInt2)
  {
    if (paramInt2 != 0)
    {
      DrawableFeatureState localDrawableFeatureState = getDrawableState(paramInt1, true);
      if (resid != paramInt2)
      {
        resid = paramInt2;
        uri = null;
        local = getContext().getDrawable(paramInt2);
        updateDrawable(paramInt1, localDrawableFeatureState, false);
      }
    }
    else
    {
      setFeatureDrawable(paramInt1, null);
    }
  }
  
  public final void setFeatureDrawableUri(int paramInt, Uri paramUri)
  {
    if (paramUri != null)
    {
      DrawableFeatureState localDrawableFeatureState = getDrawableState(paramInt, true);
      if ((uri == null) || (!uri.equals(paramUri)))
      {
        resid = 0;
        uri = paramUri;
        local = loadImageURI(paramUri);
        updateDrawable(paramInt, localDrawableFeatureState, false);
      }
    }
    else
    {
      setFeatureDrawable(paramInt, null);
    }
  }
  
  public final void setFeatureInt(int paramInt1, int paramInt2)
  {
    updateInt(paramInt1, paramInt2, false);
  }
  
  public void setIcon(int paramInt)
  {
    mIconRes = paramInt;
    mResourcesSetFlags |= 0x1;
    mResourcesSetFlags &= 0xFFFFFFFB;
    if (mDecorContentParent != null) {
      mDecorContentParent.setIcon(paramInt);
    }
  }
  
  public void setIsStartingWindow(boolean paramBoolean)
  {
    mIsStartingWindow = paramBoolean;
  }
  
  public void setLocalFocus(boolean paramBoolean1, boolean paramBoolean2)
  {
    getViewRootImpl().windowFocusChanged(paramBoolean1, paramBoolean2);
  }
  
  public void setLogo(int paramInt)
  {
    mLogoRes = paramInt;
    mResourcesSetFlags |= 0x2;
    if (mDecorContentParent != null) {
      mDecorContentParent.setLogo(paramInt);
    }
  }
  
  public void setMediaController(MediaController paramMediaController)
  {
    mMediaController = paramMediaController;
  }
  
  public void setNavigationBarColor(int paramInt)
  {
    mNavigationBarColor = paramInt;
    mForcedNavigationBarColor = true;
    if (mDecor != null) {
      mDecor.updateColorViews(null, false);
    }
  }
  
  public void setNavigationBarDividerColor(int paramInt)
  {
    mNavigationBarDividerColor = paramInt;
    if (mDecor != null) {
      mDecor.updateColorViews(null, false);
    }
  }
  
  public void setReenterTransition(Transition paramTransition)
  {
    mReenterTransition = paramTransition;
  }
  
  public void setResizingCaptionDrawable(Drawable paramDrawable)
  {
    mDecor.setUserCaptionBackgroundDrawable(paramDrawable);
  }
  
  public void setReturnTransition(Transition paramTransition)
  {
    mReturnTransition = paramTransition;
  }
  
  public void setSharedElementEnterTransition(Transition paramTransition)
  {
    mSharedElementEnterTransition = paramTransition;
  }
  
  public void setSharedElementExitTransition(Transition paramTransition)
  {
    mSharedElementExitTransition = paramTransition;
  }
  
  public void setSharedElementReenterTransition(Transition paramTransition)
  {
    mSharedElementReenterTransition = paramTransition;
  }
  
  public void setSharedElementReturnTransition(Transition paramTransition)
  {
    mSharedElementReturnTransition = paramTransition;
  }
  
  public void setSharedElementsUseOverlay(boolean paramBoolean)
  {
    mSharedElementsUseOverlay = Boolean.valueOf(paramBoolean);
  }
  
  public void setStatusBarColor(int paramInt)
  {
    mStatusBarColor = paramInt;
    mForcedStatusBarColor = true;
    if (mDecor != null) {
      mDecor.updateColorViews(null, false);
    }
  }
  
  public void setTheme(int paramInt)
  {
    mTheme = paramInt;
    if (mDecor != null)
    {
      Context localContext = mDecor.getContext();
      if ((localContext instanceof DecorContext)) {
        localContext.setTheme(paramInt);
      }
    }
  }
  
  public void setTitle(CharSequence paramCharSequence)
  {
    setTitle(paramCharSequence, true);
  }
  
  public void setTitle(CharSequence paramCharSequence, boolean paramBoolean)
  {
    if (mTitleView != null) {
      mTitleView.setText(paramCharSequence);
    } else if (mDecorContentParent != null) {
      mDecorContentParent.setWindowTitle(paramCharSequence);
    }
    mTitle = paramCharSequence;
    if (paramBoolean)
    {
      WindowManager.LayoutParams localLayoutParams = getAttributes();
      if (!TextUtils.equals(paramCharSequence, accessibilityTitle))
      {
        accessibilityTitle = TextUtils.stringOrSpannedString(paramCharSequence);
        if (mDecor != null)
        {
          paramCharSequence = mDecor.getViewRootImpl();
          if (paramCharSequence != null) {
            paramCharSequence.onWindowTitleChanged();
          }
        }
        dispatchWindowAttributesChanged(getAttributes());
      }
    }
  }
  
  @Deprecated
  public void setTitleColor(int paramInt)
  {
    if (mTitleView != null) {
      mTitleView.setTextColor(paramInt);
    }
    mTitleColor = paramInt;
  }
  
  public void setTransitionBackgroundFadeDuration(long paramLong)
  {
    if (paramLong >= 0L)
    {
      mBackgroundFadeDurationMillis = paramLong;
      return;
    }
    throw new IllegalArgumentException("negative durations are not allowed");
  }
  
  public void setTransitionManager(TransitionManager paramTransitionManager)
  {
    mTransitionManager = paramTransitionManager;
  }
  
  public void setUiOptions(int paramInt)
  {
    mUiOptions = paramInt;
  }
  
  public void setUiOptions(int paramInt1, int paramInt2)
  {
    mUiOptions = (mUiOptions & paramInt2 | paramInt1 & paramInt2);
  }
  
  public void setVolumeControlStream(int paramInt)
  {
    mVolumeControlStreamType = paramInt;
  }
  
  public boolean superDispatchGenericMotionEvent(MotionEvent paramMotionEvent)
  {
    return mDecor.superDispatchGenericMotionEvent(paramMotionEvent);
  }
  
  public boolean superDispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    return mDecor.superDispatchKeyEvent(paramKeyEvent);
  }
  
  public boolean superDispatchKeyShortcutEvent(KeyEvent paramKeyEvent)
  {
    return mDecor.superDispatchKeyShortcutEvent(paramKeyEvent);
  }
  
  public boolean superDispatchTouchEvent(MotionEvent paramMotionEvent)
  {
    return mDecor.superDispatchTouchEvent(paramMotionEvent);
  }
  
  public boolean superDispatchTrackballEvent(MotionEvent paramMotionEvent)
  {
    return mDecor.superDispatchTrackballEvent(paramMotionEvent);
  }
  
  public void takeInputQueue(InputQueue.Callback paramCallback)
  {
    mTakeInputQueueCallback = paramCallback;
  }
  
  public void takeKeyEvents(boolean paramBoolean)
  {
    mDecor.setFocusable(paramBoolean);
  }
  
  public void takeSurface(SurfaceHolder.Callback2 paramCallback2)
  {
    mTakeSurfaceCallback = paramCallback2;
  }
  
  public final void togglePanel(int paramInt, KeyEvent paramKeyEvent)
  {
    PanelFeatureState localPanelFeatureState = getPanelState(paramInt, true);
    if (isOpen) {
      closePanel(localPanelFeatureState, true);
    } else {
      openPanel(localPanelFeatureState, paramKeyEvent);
    }
  }
  
  protected final void updateDrawable(int paramInt, boolean paramBoolean)
  {
    DrawableFeatureState localDrawableFeatureState = getDrawableState(paramInt, false);
    if (localDrawableFeatureState != null) {
      updateDrawable(paramInt, localDrawableFeatureState, paramBoolean);
    }
  }
  
  private final class ActionMenuPresenterCallback
    implements MenuPresenter.Callback
  {
    private ActionMenuPresenterCallback() {}
    
    public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean)
    {
      checkCloseActionMenu(paramMenuBuilder);
    }
    
    public boolean onOpenSubMenu(MenuBuilder paramMenuBuilder)
    {
      Window.Callback localCallback = getCallback();
      if (localCallback != null)
      {
        localCallback.onMenuOpened(8, paramMenuBuilder);
        return true;
      }
      return false;
    }
  }
  
  private static final class DrawableFeatureState
  {
    int alpha = 255;
    Drawable child;
    Drawable cur;
    int curAlpha = 255;
    Drawable def;
    final int featureId;
    Drawable local;
    int resid;
    Uri uri;
    
    DrawableFeatureState(int paramInt)
    {
      featureId = paramInt;
    }
  }
  
  static final class PanelFeatureState
  {
    int background;
    View createdPanelView;
    DecorView decorView;
    int featureId;
    Bundle frozenActionViewState;
    Bundle frozenMenuState;
    int fullBackground;
    int gravity;
    IconMenuPresenter iconMenuPresenter;
    boolean isCompact;
    boolean isHandled;
    boolean isInExpandedMode;
    boolean isOpen;
    boolean isPrepared;
    ListMenuPresenter listMenuPresenter;
    int listPresenterTheme;
    MenuBuilder menu;
    public boolean qwertyMode;
    boolean refreshDecorView;
    boolean refreshMenuContent;
    View shownPanelView;
    boolean wasLastExpanded;
    boolean wasLastOpen;
    int windowAnimations;
    int x;
    int y;
    
    PanelFeatureState(int paramInt)
    {
      featureId = paramInt;
      refreshDecorView = false;
    }
    
    void applyFrozenState()
    {
      if ((menu != null) && (frozenMenuState != null))
      {
        menu.restorePresenterStates(frozenMenuState);
        frozenMenuState = null;
      }
    }
    
    public void clearMenuPresenters()
    {
      if (menu != null)
      {
        menu.removeMenuPresenter(iconMenuPresenter);
        menu.removeMenuPresenter(listMenuPresenter);
      }
      iconMenuPresenter = null;
      listMenuPresenter = null;
    }
    
    MenuView getIconMenuView(Context paramContext, MenuPresenter.Callback paramCallback)
    {
      if (menu == null) {
        return null;
      }
      if (iconMenuPresenter == null)
      {
        iconMenuPresenter = new IconMenuPresenter(paramContext);
        iconMenuPresenter.setCallback(paramCallback);
        iconMenuPresenter.setId(16909017);
        menu.addMenuPresenter(iconMenuPresenter);
      }
      return iconMenuPresenter.getMenuView(decorView);
    }
    
    MenuView getListMenuView(Context paramContext, MenuPresenter.Callback paramCallback)
    {
      if (menu == null) {
        return null;
      }
      if (!isCompact) {
        getIconMenuView(paramContext, paramCallback);
      }
      if (listMenuPresenter == null)
      {
        listMenuPresenter = new ListMenuPresenter(17367201, listPresenterTheme);
        listMenuPresenter.setCallback(paramCallback);
        listMenuPresenter.setId(16909093);
        menu.addMenuPresenter(listMenuPresenter);
      }
      if (iconMenuPresenter != null) {
        listMenuPresenter.setItemIndexOffset(iconMenuPresenter.getNumActualItemsShown());
      }
      return listMenuPresenter.getMenuView(decorView);
    }
    
    public boolean hasPanelItems()
    {
      View localView = shownPanelView;
      boolean bool1 = false;
      boolean bool2 = false;
      if (localView == null) {
        return false;
      }
      if (createdPanelView != null) {
        return true;
      }
      if ((!isCompact) && (!isInExpandedMode))
      {
        if (((ViewGroup)shownPanelView).getChildCount() > 0) {
          bool2 = true;
        }
        return bool2;
      }
      bool2 = bool1;
      if (listMenuPresenter.getAdapter().getCount() > 0) {
        bool2 = true;
      }
      return bool2;
    }
    
    public boolean isInListMode()
    {
      boolean bool;
      if ((!isInExpandedMode) && (!isCompact)) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    void onRestoreInstanceState(Parcelable paramParcelable)
    {
      paramParcelable = (SavedState)paramParcelable;
      featureId = featureId;
      wasLastOpen = isOpen;
      wasLastExpanded = isInExpandedMode;
      frozenMenuState = menuState;
      createdPanelView = null;
      shownPanelView = null;
      decorView = null;
    }
    
    Parcelable onSaveInstanceState()
    {
      SavedState localSavedState = new SavedState(null);
      featureId = featureId;
      isOpen = isOpen;
      isInExpandedMode = isInExpandedMode;
      if (menu != null)
      {
        menuState = new Bundle();
        menu.savePresenterStates(menuState);
      }
      return localSavedState;
    }
    
    void setMenu(MenuBuilder paramMenuBuilder)
    {
      if (paramMenuBuilder == menu) {
        return;
      }
      if (menu != null)
      {
        menu.removeMenuPresenter(iconMenuPresenter);
        menu.removeMenuPresenter(listMenuPresenter);
      }
      menu = paramMenuBuilder;
      if (paramMenuBuilder != null)
      {
        if (iconMenuPresenter != null) {
          paramMenuBuilder.addMenuPresenter(iconMenuPresenter);
        }
        if (listMenuPresenter != null) {
          paramMenuBuilder.addMenuPresenter(listMenuPresenter);
        }
      }
    }
    
    void setStyle(Context paramContext)
    {
      paramContext = paramContext.obtainStyledAttributes(R.styleable.Theme);
      background = paramContext.getResourceId(46, 0);
      fullBackground = paramContext.getResourceId(47, 0);
      windowAnimations = paramContext.getResourceId(93, 0);
      isCompact = paramContext.getBoolean(314, false);
      listPresenterTheme = paramContext.getResourceId(315, 16975047);
      paramContext.recycle();
    }
    
    private static class SavedState
      implements Parcelable
    {
      public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
      {
        public PhoneWindow.PanelFeatureState.SavedState createFromParcel(Parcel paramAnonymousParcel)
        {
          return PhoneWindow.PanelFeatureState.SavedState.readFromParcel(paramAnonymousParcel);
        }
        
        public PhoneWindow.PanelFeatureState.SavedState[] newArray(int paramAnonymousInt)
        {
          return new PhoneWindow.PanelFeatureState.SavedState[paramAnonymousInt];
        }
      };
      int featureId;
      boolean isInExpandedMode;
      boolean isOpen;
      Bundle menuState;
      
      private SavedState() {}
      
      private static SavedState readFromParcel(Parcel paramParcel)
      {
        SavedState localSavedState = new SavedState();
        featureId = paramParcel.readInt();
        int i = paramParcel.readInt();
        boolean bool1 = false;
        if (i == 1) {
          bool2 = true;
        } else {
          bool2 = false;
        }
        isOpen = bool2;
        boolean bool2 = bool1;
        if (paramParcel.readInt() == 1) {
          bool2 = true;
        }
        isInExpandedMode = bool2;
        if (isOpen) {
          menuState = paramParcel.readBundle();
        }
        return localSavedState;
      }
      
      public int describeContents()
      {
        return 0;
      }
      
      public void writeToParcel(Parcel paramParcel, int paramInt)
      {
        paramParcel.writeInt(featureId);
        paramParcel.writeInt(isOpen);
        paramParcel.writeInt(isInExpandedMode);
        if (isOpen) {
          paramParcel.writeBundle(menuState);
        }
      }
    }
  }
  
  private class PanelMenuPresenterCallback
    implements MenuPresenter.Callback
  {
    private PanelMenuPresenterCallback() {}
    
    public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean)
    {
      MenuBuilder localMenuBuilder = paramMenuBuilder.getRootMenu();
      int i;
      if (localMenuBuilder != paramMenuBuilder) {
        i = 1;
      } else {
        i = 0;
      }
      PhoneWindow localPhoneWindow = PhoneWindow.this;
      if (i != 0) {
        paramMenuBuilder = localMenuBuilder;
      }
      paramMenuBuilder = localPhoneWindow.findMenuPanel(paramMenuBuilder);
      if (paramMenuBuilder != null) {
        if (i != 0)
        {
          PhoneWindow.this.callOnPanelClosed(featureId, paramMenuBuilder, localMenuBuilder);
          closePanel(paramMenuBuilder, true);
        }
        else
        {
          closePanel(paramMenuBuilder, paramBoolean);
        }
      }
    }
    
    public boolean onOpenSubMenu(MenuBuilder paramMenuBuilder)
    {
      if ((paramMenuBuilder == null) && (hasFeature(8)))
      {
        Window.Callback localCallback = getCallback();
        if ((localCallback != null) && (!isDestroyed())) {
          localCallback.onMenuOpened(8, paramMenuBuilder);
        }
      }
      return true;
    }
  }
  
  public static final class PhoneWindowMenuCallback
    implements MenuBuilder.Callback, MenuPresenter.Callback
  {
    private static final int FEATURE_ID = 6;
    private boolean mShowDialogForSubmenu;
    private MenuDialogHelper mSubMenuHelper;
    private final PhoneWindow mWindow;
    
    public PhoneWindowMenuCallback(PhoneWindow paramPhoneWindow)
    {
      mWindow = paramPhoneWindow;
    }
    
    private void onCloseSubMenu(MenuBuilder paramMenuBuilder)
    {
      Window.Callback localCallback = mWindow.getCallback();
      if ((localCallback != null) && (!mWindow.isDestroyed())) {
        localCallback.onPanelClosed(6, paramMenuBuilder.getRootMenu());
      }
    }
    
    public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean)
    {
      if (paramMenuBuilder.getRootMenu() != paramMenuBuilder) {
        onCloseSubMenu(paramMenuBuilder);
      }
      if (paramBoolean)
      {
        Window.Callback localCallback = mWindow.getCallback();
        if ((localCallback != null) && (!mWindow.isDestroyed())) {
          localCallback.onPanelClosed(6, paramMenuBuilder);
        }
        if (paramMenuBuilder == mWindow.mContextMenu) {
          mWindow.dismissContextMenu();
        }
        if (mSubMenuHelper != null)
        {
          mSubMenuHelper.dismiss();
          mSubMenuHelper = null;
        }
      }
    }
    
    public boolean onMenuItemSelected(MenuBuilder paramMenuBuilder, MenuItem paramMenuItem)
    {
      paramMenuBuilder = mWindow.getCallback();
      boolean bool;
      if ((paramMenuBuilder != null) && (!mWindow.isDestroyed()) && (paramMenuBuilder.onMenuItemSelected(6, paramMenuItem))) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void onMenuModeChange(MenuBuilder paramMenuBuilder) {}
    
    public boolean onOpenSubMenu(MenuBuilder paramMenuBuilder)
    {
      if (paramMenuBuilder == null) {
        return false;
      }
      paramMenuBuilder.setCallback(this);
      if (mShowDialogForSubmenu)
      {
        mSubMenuHelper = new MenuDialogHelper(paramMenuBuilder);
        mSubMenuHelper.show(null);
        return true;
      }
      return false;
    }
    
    public void setShowDialogForSubmenu(boolean paramBoolean)
    {
      mShowDialogForSubmenu = paramBoolean;
    }
  }
  
  static class RotationWatcher
    extends IRotationWatcher.Stub
  {
    private Handler mHandler;
    private boolean mIsWatching;
    private final Runnable mRotationChanged = new Runnable()
    {
      public void run()
      {
        dispatchRotationChanged();
      }
    };
    private final ArrayList<WeakReference<PhoneWindow>> mWindows = new ArrayList();
    
    RotationWatcher() {}
    
    public void addWindow(PhoneWindow paramPhoneWindow)
    {
      synchronized (mWindows)
      {
        boolean bool = mIsWatching;
        if (!bool) {
          try
          {
            PhoneWindow.WindowManagerHolder.sWindowManager.watchRotation(this, paramPhoneWindow.getContext().getDisplay().getDisplayId());
            Handler localHandler = new android/os/Handler;
            localHandler.<init>();
            mHandler = localHandler;
            mIsWatching = true;
          }
          catch (RemoteException localRemoteException)
          {
            Log.e("PhoneWindow", "Couldn't start watching for device rotation", localRemoteException);
          }
        }
        ArrayList localArrayList2 = mWindows;
        WeakReference localWeakReference = new java/lang/ref/WeakReference;
        localWeakReference.<init>(paramPhoneWindow);
        localArrayList2.add(localWeakReference);
        return;
      }
    }
    
    void dispatchRotationChanged()
    {
      ArrayList localArrayList = mWindows;
      int i = 0;
      try
      {
        while (i < mWindows.size())
        {
          PhoneWindow localPhoneWindow = (PhoneWindow)((WeakReference)mWindows.get(i)).get();
          if (localPhoneWindow != null)
          {
            localPhoneWindow.onOptionsPanelRotationChanged();
            i++;
          }
          else
          {
            mWindows.remove(i);
          }
        }
        return;
      }
      finally {}
    }
    
    public void onRotationChanged(int paramInt)
      throws RemoteException
    {
      mHandler.post(mRotationChanged);
    }
    
    public void removeWindow(PhoneWindow paramPhoneWindow)
    {
      ArrayList localArrayList = mWindows;
      int i = 0;
      try
      {
        while (i < mWindows.size())
        {
          PhoneWindow localPhoneWindow = (PhoneWindow)((WeakReference)mWindows.get(i)).get();
          if ((localPhoneWindow != null) && (localPhoneWindow != paramPhoneWindow)) {
            i++;
          } else {
            mWindows.remove(i);
          }
        }
        return;
      }
      finally {}
    }
  }
  
  static class WindowManagerHolder
  {
    static final IWindowManager sWindowManager = IWindowManager.Stub.asInterface(ServiceManager.getService("window"));
    
    WindowManagerHolder() {}
  }
}
