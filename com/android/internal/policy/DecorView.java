package com.android.internal.policy;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.WindowConfiguration;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build.FEATURES;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.ActionMode.Callback2;
import android.view.ContextThemeWrapper;
import android.view.DisplayListCanvas;
import android.view.InputQueue.Callback;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder.Callback2;
import android.view.ThreadedRenderer;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewOutlineProvider;
import android.view.ViewPropertyAnimator;
import android.view.ViewRootImpl;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window.Callback;
import android.view.Window.WindowControllerCallback;
import android.view.WindowCallbacks;
import android.view.WindowInsets;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.PopupWindow;
import com.android.internal.view.FloatingActionMode;
import com.android.internal.view.RootViewSurfaceTaker;
import com.android.internal.view.StandaloneActionMode;
import com.android.internal.view.menu.ContextMenuBuilder;
import com.android.internal.view.menu.MenuBuilder;
import com.android.internal.view.menu.MenuHelper;
import com.android.internal.widget.ActionBarContextView;
import com.android.internal.widget.BackgroundFallback;
import com.android.internal.widget.DecorCaptionView;
import com.android.internal.widget.DecorContentParent;
import com.android.internal.widget.FloatingToolbar;
import java.util.List;

public class DecorView
  extends FrameLayout
  implements RootViewSurfaceTaker, WindowCallbacks
{
  private static final boolean DEBUG_MEASURE = false;
  private static final int DECOR_SHADOW_FOCUSED_HEIGHT_IN_DIP = 20;
  private static final int DECOR_SHADOW_UNFOCUSED_HEIGHT_IN_DIP = 5;
  public static final ColorViewAttributes NAVIGATION_BAR_COLOR_VIEW_ATTRIBUTES = new ColorViewAttributes(2, 134217728, 80, 5, 3, "android:navigation:background", 16908336, 0, null);
  private static final ViewOutlineProvider PIP_OUTLINE_PROVIDER = new ViewOutlineProvider()
  {
    public void getOutline(View paramAnonymousView, Outline paramAnonymousOutline)
    {
      paramAnonymousOutline.setRect(0, 0, paramAnonymousView.getWidth(), paramAnonymousView.getHeight());
      paramAnonymousOutline.setAlpha(1.0F);
    }
  };
  public static final ColorViewAttributes STATUS_BAR_COLOR_VIEW_ATTRIBUTES = new ColorViewAttributes(4, 67108864, 48, 3, 5, "android:status:background", 16908335, 1024, null);
  private static final boolean SWEEP_OPEN_MENU = false;
  private static final String TAG = "DecorView";
  private boolean mAllowUpdateElevation;
  private boolean mApplyFloatingHorizontalInsets;
  private boolean mApplyFloatingVerticalInsets;
  private float mAvailableWidth;
  private BackdropFrameRenderer mBackdropFrameRenderer;
  private final BackgroundFallback mBackgroundFallback;
  private final Rect mBackgroundPadding;
  private final int mBarEnterExitDuration;
  private Drawable mCaptionBackgroundDrawable;
  private boolean mChanging;
  ViewGroup mContentRoot;
  DecorCaptionView mDecorCaptionView;
  int mDefaultOpacity;
  private int mDownY;
  private final Rect mDrawingBounds;
  private boolean mElevationAdjustedForStack;
  private ObjectAnimator mFadeAnim;
  private final int mFeatureId;
  private ActionMode mFloatingActionMode;
  private View mFloatingActionModeOriginatingView;
  private final Rect mFloatingInsets;
  private FloatingToolbar mFloatingToolbar;
  private ViewTreeObserver.OnPreDrawListener mFloatingToolbarPreDrawListener;
  final boolean mForceWindowDrawsStatusBarBackground;
  private final Rect mFrameOffsets;
  private final Rect mFramePadding;
  private boolean mHasCaption;
  private final Interpolator mHideInterpolator;
  private final Paint mHorizontalResizeShadowPaint;
  private boolean mIsInPictureInPictureMode;
  private Drawable.Callback mLastBackgroundDrawableCb;
  private int mLastBottomInset;
  private boolean mLastHasBottomStableInset;
  private boolean mLastHasLeftStableInset;
  private boolean mLastHasRightStableInset;
  private boolean mLastHasTopStableInset;
  private int mLastLeftInset;
  private ViewOutlineProvider mLastOutlineProvider;
  private int mLastRightInset;
  private boolean mLastShouldAlwaysConsumeNavBar;
  private int mLastTopInset;
  private int mLastWindowFlags;
  String mLogTag;
  private Drawable mMenuBackground;
  private final ColorViewState mNavigationColorViewState;
  private int mOriSystemUiVisibility;
  private Rect mOutsets;
  private int mOverrideSystemUiVisibility;
  ActionMode mPrimaryActionMode;
  private PopupWindow mPrimaryActionModePopup;
  private ActionBarContextView mPrimaryActionModeView;
  private int mResizeMode;
  private final int mResizeShadowSize;
  private Drawable mResizingBackgroundDrawable;
  private int mRootScrollY;
  private final int mSemiTransparentStatusBarColor;
  private final Interpolator mShowInterpolator;
  private Runnable mShowPrimaryActionModePopup;
  private final ColorViewState mStatusColorViewState;
  private View mStatusGuard;
  private Rect mTempRect;
  private Drawable mUserCaptionBackgroundDrawable;
  private final Paint mVerticalResizeShadowPaint;
  private boolean mWatchingForMenu;
  private PhoneWindow mWindow;
  private boolean mWindowResizeCallbacksAdded;
  
  DecorView(Context paramContext, int paramInt, PhoneWindow paramPhoneWindow, WindowManager.LayoutParams paramLayoutParams)
  {
    super(paramContext);
    boolean bool1 = false;
    mAllowUpdateElevation = false;
    mElevationAdjustedForStack = false;
    mDefaultOpacity = -1;
    mDrawingBounds = new Rect();
    mBackgroundPadding = new Rect();
    mFramePadding = new Rect();
    mFrameOffsets = new Rect();
    mHasCaption = false;
    mStatusColorViewState = new ColorViewState(STATUS_BAR_COLOR_VIEW_ATTRIBUTES);
    mNavigationColorViewState = new ColorViewState(NAVIGATION_BAR_COLOR_VIEW_ATTRIBUTES);
    mBackgroundFallback = new BackgroundFallback();
    mLastTopInset = 0;
    mLastBottomInset = 0;
    mLastRightInset = 0;
    mLastLeftInset = 0;
    mLastHasTopStableInset = false;
    mLastHasBottomStableInset = false;
    mLastHasRightStableInset = false;
    mLastHasLeftStableInset = false;
    mLastWindowFlags = 0;
    mLastShouldAlwaysConsumeNavBar = false;
    mRootScrollY = 0;
    mOutsets = new Rect();
    mWindowResizeCallbacksAdded = false;
    mLastBackgroundDrawableCb = null;
    mBackdropFrameRenderer = null;
    mLogTag = "DecorView";
    mFloatingInsets = new Rect();
    mApplyFloatingVerticalInsets = false;
    mApplyFloatingHorizontalInsets = false;
    mResizeMode = -1;
    mVerticalResizeShadowPaint = new Paint();
    mHorizontalResizeShadowPaint = new Paint();
    mFeatureId = paramInt;
    mShowInterpolator = AnimationUtils.loadInterpolator(paramContext, 17563662);
    mHideInterpolator = AnimationUtils.loadInterpolator(paramContext, 17563663);
    mBarEnterExitDuration = paramContext.getResources().getInteger(17694950);
    boolean bool2 = bool1;
    if (paramContext.getResources().getBoolean(17956980))
    {
      bool2 = bool1;
      if (getApplicationInfotargetSdkVersion >= 24) {
        bool2 = true;
      }
    }
    mForceWindowDrawsStatusBarBackground = bool2;
    mSemiTransparentStatusBarColor = paramContext.getResources().getColor(17170875, null);
    updateAvailableWidth();
    setWindow(paramPhoneWindow);
    updateLogTag(paramLayoutParams);
    mResizeShadowSize = paramContext.getResources().getDimensionPixelSize(17105406);
    initResizingPaints();
  }
  
  private int calculateStatusBarColor()
  {
    return calculateStatusBarColor(mWindow.getAttributes().flags, mSemiTransparentStatusBarColor, mWindow.mStatusBarColor);
  }
  
  public static int calculateStatusBarColor(int paramInt1, int paramInt2, int paramInt3)
  {
    if ((0x4000000 & paramInt1) != 0) {
      paramInt1 = paramInt2;
    } else if ((0x80000000 & paramInt1) != 0) {
      paramInt1 = paramInt3;
    } else {
      paramInt1 = -16777216;
    }
    return paramInt1;
  }
  
  private void cleanupFloatingActionModeViews()
  {
    if (mFloatingToolbar != null)
    {
      mFloatingToolbar.dismiss();
      mFloatingToolbar = null;
    }
    if (mFloatingActionModeOriginatingView != null)
    {
      if (mFloatingToolbarPreDrawListener != null)
      {
        mFloatingActionModeOriginatingView.getViewTreeObserver().removeOnPreDrawListener(mFloatingToolbarPreDrawListener);
        mFloatingToolbarPreDrawListener = null;
      }
      mFloatingActionModeOriginatingView = null;
    }
  }
  
  private void cleanupPrimaryActionMode()
  {
    if (mPrimaryActionMode != null)
    {
      mPrimaryActionMode.finish();
      mPrimaryActionMode = null;
    }
    if (mPrimaryActionModeView != null) {
      mPrimaryActionModeView.killMode();
    }
  }
  
  private ActionMode createActionMode(int paramInt, ActionMode.Callback2 paramCallback2, View paramView)
  {
    if (paramInt != 1) {
      return createStandaloneActionMode(paramCallback2);
    }
    return createFloatingActionMode(paramView, paramCallback2);
  }
  
  private DecorCaptionView createDecorCaptionView(LayoutInflater paramLayoutInflater)
  {
    DecorCaptionView localDecorCaptionView = null;
    int i = getChildCount();
    boolean bool = true;
    i--;
    while ((i >= 0) && (localDecorCaptionView == null))
    {
      localObject = getChildAt(i);
      if ((localObject instanceof DecorCaptionView))
      {
        localDecorCaptionView = (DecorCaptionView)localObject;
        removeViewAt(i);
      }
      i--;
    }
    Object localObject = mWindow.getAttributes();
    if ((type != 1) && (type != 2) && (type != 4)) {
      i = 0;
    } else {
      i = 1;
    }
    localObject = getResourcesgetConfigurationwindowConfiguration;
    if ((!mWindow.isFloating()) && (i != 0) && (((WindowConfiguration)localObject).hasWindowDecorCaption()))
    {
      localObject = localDecorCaptionView;
      if (localDecorCaptionView == null) {
        localObject = inflateDecorCaptionView(paramLayoutInflater);
      }
      ((DecorCaptionView)localObject).setPhoneWindow(mWindow, true);
    }
    else
    {
      localObject = null;
    }
    if (localObject == null) {
      bool = false;
    }
    enableCaption(bool);
    return localObject;
  }
  
  private ActionMode createFloatingActionMode(View paramView, final ActionMode.Callback2 paramCallback2)
  {
    if (mFloatingActionMode != null) {
      mFloatingActionMode.finish();
    }
    cleanupFloatingActionModeViews();
    mFloatingToolbar = new FloatingToolbar(mWindow);
    paramCallback2 = new FloatingActionMode(mContext, paramCallback2, paramView, mFloatingToolbar);
    mFloatingActionModeOriginatingView = paramView;
    mFloatingToolbarPreDrawListener = new ViewTreeObserver.OnPreDrawListener()
    {
      public boolean onPreDraw()
      {
        paramCallback2.updateViewLocationInWindow();
        return true;
      }
    };
    return paramCallback2;
  }
  
  private ActionMode createStandaloneActionMode(ActionMode.Callback paramCallback)
  {
    endOnGoingFadeAnimation();
    cleanupPrimaryActionMode();
    Object localObject1 = mPrimaryActionModeView;
    boolean bool = false;
    Object localObject2;
    if ((localObject1 == null) || (!mPrimaryActionModeView.isAttachedToWindow())) {
      if (mWindow.isFloating())
      {
        localObject2 = new TypedValue();
        localObject1 = mContext.getTheme();
        ((Resources.Theme)localObject1).resolveAttribute(16843825, (TypedValue)localObject2, true);
        if (resourceId != 0)
        {
          Resources.Theme localTheme = mContext.getResources().newTheme();
          localTheme.setTo((Resources.Theme)localObject1);
          localTheme.applyStyle(resourceId, true);
          localObject1 = new ContextThemeWrapper(mContext, 0);
          ((Context)localObject1).getTheme().setTo(localTheme);
        }
        else
        {
          localObject1 = mContext;
        }
        mPrimaryActionModeView = new ActionBarContextView((Context)localObject1);
        mPrimaryActionModePopup = new PopupWindow((Context)localObject1, null, 17891334);
        mPrimaryActionModePopup.setWindowLayoutType(2);
        mPrimaryActionModePopup.setContentView(mPrimaryActionModeView);
        mPrimaryActionModePopup.setWidth(-1);
        ((Context)localObject1).getTheme().resolveAttribute(16843499, (TypedValue)localObject2, true);
        int i = TypedValue.complexToDimensionPixelSize(data, ((Context)localObject1).getResources().getDisplayMetrics());
        mPrimaryActionModeView.setContentHeight(i);
        mPrimaryActionModePopup.setHeight(-2);
        mShowPrimaryActionModePopup = new Runnable()
        {
          public void run()
          {
            mPrimaryActionModePopup.showAtLocation(mPrimaryActionModeView.getApplicationWindowToken(), 55, 0, 0);
            DecorView.this.endOnGoingFadeAnimation();
            if (shouldAnimatePrimaryActionModeView())
            {
              DecorView.access$402(DecorView.this, ObjectAnimator.ofFloat(mPrimaryActionModeView, View.ALPHA, new float[] { 0.0F, 1.0F }));
              mFadeAnim.addListener(new AnimatorListenerAdapter()
              {
                public void onAnimationEnd(Animator paramAnonymous2Animator)
                {
                  mPrimaryActionModeView.setAlpha(1.0F);
                  DecorView.access$402(DecorView.this, null);
                }
                
                public void onAnimationStart(Animator paramAnonymous2Animator)
                {
                  mPrimaryActionModeView.setVisibility(0);
                }
              });
              mFadeAnim.start();
            }
            else
            {
              mPrimaryActionModeView.setAlpha(1.0F);
              mPrimaryActionModeView.setVisibility(0);
            }
          }
        };
      }
      else
      {
        localObject1 = (ViewStub)findViewById(16908707);
        if (localObject1 != null)
        {
          mPrimaryActionModeView = ((ActionBarContextView)((ViewStub)localObject1).inflate());
          mPrimaryActionModePopup = null;
        }
      }
    }
    if (mPrimaryActionModeView != null)
    {
      mPrimaryActionModeView.killMode();
      localObject1 = mPrimaryActionModeView.getContext();
      localObject2 = mPrimaryActionModeView;
      if (mPrimaryActionModePopup == null) {
        bool = true;
      }
      return new StandaloneActionMode((Context)localObject1, (ActionBarContextView)localObject2, paramCallback, bool);
    }
    return null;
  }
  
  private float dipToPx(float paramFloat)
  {
    return TypedValue.applyDimension(1, paramFloat, getResources().getDisplayMetrics());
  }
  
  private void drawResizingShadowIfNeeded(DisplayListCanvas paramDisplayListCanvas)
  {
    if ((mResizeMode == 1) && (!mWindow.mIsFloating) && (!mWindow.isTranslucent()) && (!mWindow.isShowingWallpaper()))
    {
      paramDisplayListCanvas.save();
      paramDisplayListCanvas.translate(0.0F, getHeight() - mFrameOffsets.bottom);
      paramDisplayListCanvas.drawRect(0.0F, 0.0F, getWidth(), mResizeShadowSize, mHorizontalResizeShadowPaint);
      paramDisplayListCanvas.restore();
      paramDisplayListCanvas.save();
      paramDisplayListCanvas.translate(getWidth() - mFrameOffsets.right, 0.0F);
      paramDisplayListCanvas.drawRect(0.0F, 0.0F, mResizeShadowSize, getHeight(), mVerticalResizeShadowPaint);
      paramDisplayListCanvas.restore();
      return;
    }
  }
  
  private void drawableChanged()
  {
    if (mChanging) {
      return;
    }
    setPadding(mFramePadding.left + mBackgroundPadding.left, mFramePadding.top + mBackgroundPadding.top, mFramePadding.right + mBackgroundPadding.right, mFramePadding.bottom + mBackgroundPadding.bottom);
    requestLayout();
    invalidate();
    int i = -1;
    if (getResourcesgetConfigurationwindowConfiguration.hasWindowShadow())
    {
      i = -3;
    }
    else
    {
      Drawable localDrawable1 = getBackground();
      Drawable localDrawable2 = getForeground();
      if (localDrawable1 != null) {
        if (localDrawable2 == null)
        {
          i = localDrawable1.getOpacity();
        }
        else if ((mFramePadding.left <= 0) && (mFramePadding.top <= 0) && (mFramePadding.right <= 0) && (mFramePadding.bottom <= 0))
        {
          int j = localDrawable2.getOpacity();
          i = localDrawable1.getOpacity();
          if ((j != -1) && (i != -1))
          {
            if (j != 0) {
              if (i == 0) {
                i = j;
              } else {
                i = Drawable.resolveOpacity(j, i);
              }
            }
          }
          else {
            i = -1;
          }
        }
        else
        {
          i = -3;
        }
      }
    }
    mDefaultOpacity = i;
    if (mFeatureId < 0) {
      mWindow.setDefaultWindowFormat(i);
    }
  }
  
  private void endOnGoingFadeAnimation()
  {
    if (mFadeAnim != null) {
      mFadeAnim.end();
    }
  }
  
  private static Drawable enforceNonTranslucentBackground(Drawable paramDrawable, boolean paramBoolean)
  {
    if ((!paramBoolean) && ((paramDrawable instanceof ColorDrawable)))
    {
      ColorDrawable localColorDrawable = (ColorDrawable)paramDrawable;
      int i = localColorDrawable.getColor();
      if (Color.alpha(i) != 255)
      {
        paramDrawable = (ColorDrawable)localColorDrawable.getConstantState().newDrawable().mutate();
        paramDrawable.setColor(Color.argb(255, Color.red(i), Color.green(i), Color.blue(i)));
        return paramDrawable;
      }
    }
    return paramDrawable;
  }
  
  public static int getColorViewBottomInset(int paramInt1, int paramInt2)
  {
    return Math.min(paramInt1, paramInt2);
  }
  
  public static int getColorViewLeftInset(int paramInt1, int paramInt2)
  {
    return Math.min(paramInt1, paramInt2);
  }
  
  public static int getColorViewRightInset(int paramInt1, int paramInt2)
  {
    return Math.min(paramInt1, paramInt2);
  }
  
  public static int getColorViewTopInset(int paramInt1, int paramInt2)
  {
    return Math.min(paramInt1, paramInt2);
  }
  
  private int getCurrentColor(ColorViewState paramColorViewState)
  {
    if (visible) {
      return color;
    }
    return 0;
  }
  
  public static int getNavBarSize(int paramInt1, int paramInt2, int paramInt3)
  {
    if (isNavBarToRightEdge(paramInt1, paramInt2)) {
      paramInt1 = paramInt2;
    } else if (isNavBarToLeftEdge(paramInt1, paramInt3)) {
      paramInt1 = paramInt3;
    }
    return paramInt1;
  }
  
  public static void getNavigationBarRect(int paramInt1, int paramInt2, Rect paramRect1, Rect paramRect2, Rect paramRect3)
  {
    int i = getColorViewBottomInset(bottom, bottom);
    int j = getColorViewLeftInset(left, left);
    int k = getColorViewLeftInset(right, right);
    int m = getNavBarSize(i, k, j);
    if (isNavBarToRightEdge(i, k)) {
      paramRect3.set(paramInt1 - m, 0, paramInt1, paramInt2);
    } else if (isNavBarToLeftEdge(i, j)) {
      paramRect3.set(0, 0, m, paramInt2);
    } else {
      paramRect3.set(0, paramInt2 - m, paramInt1, paramInt2);
    }
  }
  
  public static Drawable getResizingBackgroundDrawable(Context paramContext, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if (paramInt1 != 0)
    {
      Drawable localDrawable = paramContext.getDrawable(paramInt1);
      if (localDrawable != null) {
        return enforceNonTranslucentBackground(localDrawable, paramBoolean);
      }
    }
    if (paramInt2 != 0)
    {
      paramContext = paramContext.getDrawable(paramInt2);
      if (paramContext != null) {
        return enforceNonTranslucentBackground(paramContext, paramBoolean);
      }
    }
    return new ColorDrawable(-16777216);
  }
  
  private static String getTitleSuffix(WindowManager.LayoutParams paramLayoutParams)
  {
    if (paramLayoutParams == null) {
      return "";
    }
    paramLayoutParams = paramLayoutParams.getTitle().toString().split("\\.");
    if (paramLayoutParams.length > 0) {
      return paramLayoutParams[(paramLayoutParams.length - 1)];
    }
    return "";
  }
  
  private DecorCaptionView inflateDecorCaptionView(LayoutInflater paramLayoutInflater)
  {
    Context localContext = getContext();
    paramLayoutInflater = (DecorCaptionView)LayoutInflater.from(localContext).inflate(17367157, null);
    setDecorCaptionShade(localContext, paramLayoutInflater);
    return paramLayoutInflater;
  }
  
  private void initResizingPaints()
  {
    int i = mContext.getResources().getColor(17170833, null);
    int j = mContext.getResources().getColor(17170832, null);
    int k = (i + j) / 2;
    Object localObject1 = mHorizontalResizeShadowPaint;
    float f = mResizeShadowSize;
    Object localObject2 = Shader.TileMode.CLAMP;
    ((Paint)localObject1).setShader(new LinearGradient(0.0F, 0.0F, 0.0F, f, new int[] { i, k, j }, new float[] { 0.0F, 0.3F, 1.0F }, (Shader.TileMode)localObject2));
    localObject2 = mVerticalResizeShadowPaint;
    f = mResizeShadowSize;
    localObject1 = Shader.TileMode.CLAMP;
    ((Paint)localObject2).setShader(new LinearGradient(0.0F, 0.0F, f, 0.0F, new int[] { i, k, j }, new float[] { 0.0F, 0.3F, 1.0F }, (Shader.TileMode)localObject1));
  }
  
  private void initializeElevation()
  {
    mAllowUpdateElevation = false;
    updateElevation();
  }
  
  public static boolean isNavBarToLeftEdge(int paramInt1, int paramInt2)
  {
    boolean bool;
    if ((paramInt1 == 0) && (paramInt2 > 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isNavBarToRightEdge(int paramInt1, int paramInt2)
  {
    boolean bool;
    if ((paramInt1 == 0) && (paramInt2 > 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isOutOfBounds(int paramInt1, int paramInt2)
  {
    boolean bool;
    if ((paramInt1 >= -5) && (paramInt2 >= -5) && (paramInt1 <= getWidth() + 5) && (paramInt2 <= getHeight() + 5)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private boolean isOutOfInnerBounds(int paramInt1, int paramInt2)
  {
    boolean bool;
    if ((paramInt1 >= 0) && (paramInt2 >= 0) && (paramInt1 <= getWidth()) && (paramInt2 <= getHeight())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private boolean isResizing()
  {
    boolean bool;
    if (mBackdropFrameRenderer != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void loadBackgroundDrawablesIfNeeded()
  {
    if (mResizingBackgroundDrawable == null)
    {
      Object localObject = getContext();
      int i = mWindow.mBackgroundResource;
      int j = mWindow.mBackgroundFallbackResource;
      boolean bool;
      if ((!mWindow.isTranslucent()) && (!mWindow.isShowingWallpaper())) {
        bool = false;
      } else {
        bool = true;
      }
      mResizingBackgroundDrawable = getResizingBackgroundDrawable((Context)localObject, i, j, bool);
      if (mResizingBackgroundDrawable == null)
      {
        localObject = mLogTag;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Failed to find background drawable for PhoneWindow=");
        localStringBuilder.append(mWindow);
        Log.w((String)localObject, localStringBuilder.toString());
      }
    }
    if (mCaptionBackgroundDrawable == null) {
      mCaptionBackgroundDrawable = getContext().getDrawable(17302379);
    }
    if (mResizingBackgroundDrawable != null)
    {
      mLastBackgroundDrawableCb = mResizingBackgroundDrawable.getCallback();
      mResizingBackgroundDrawable.setCallback(null);
    }
  }
  
  private void releaseThreadedRenderer()
  {
    if ((mResizingBackgroundDrawable != null) && (mLastBackgroundDrawableCb != null))
    {
      mResizingBackgroundDrawable.setCallback(mLastBackgroundDrawableCb);
      mLastBackgroundDrawableCb = null;
    }
    if (mBackdropFrameRenderer != null)
    {
      mBackdropFrameRenderer.releaseRenderer();
      mBackdropFrameRenderer = null;
      updateElevation();
    }
  }
  
  private static void setColor(View paramView, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (paramInt2 != 0)
    {
      Object localObject = (Pair)paramView.getTag();
      if ((localObject != null) && (((Boolean)first).booleanValue() == paramBoolean1) && (((Boolean)second).booleanValue() == paramBoolean2))
      {
        paramView = (LayerDrawable)paramView.getBackground();
        ((ColorDrawable)((InsetDrawable)paramView.getDrawable(1)).getDrawable()).setColor(paramInt1);
        ((ColorDrawable)paramView.getDrawable(0)).setColor(paramInt2);
      }
      else
      {
        int i = Math.round(TypedValue.applyDimension(1, 1.0F, paramView.getContext().getResources().getDisplayMetrics()));
        localObject = new ColorDrawable(paramInt1);
        if ((paramBoolean1) && (!paramBoolean2)) {
          paramInt1 = i;
        } else {
          paramInt1 = 0;
        }
        int j;
        if (!paramBoolean1) {
          j = i;
        } else {
          j = 0;
        }
        if ((!paramBoolean1) || (!paramBoolean2)) {
          i = 0;
        }
        localObject = new InsetDrawable((Drawable)localObject, paramInt1, j, i, 0);
        paramView.setBackground(new LayerDrawable(new Drawable[] { new ColorDrawable(paramInt2), localObject }));
        paramView.setTag(new Pair(Boolean.valueOf(paramBoolean1), Boolean.valueOf(paramBoolean2)));
      }
    }
    else
    {
      paramView.setTag(null);
      paramView.setBackgroundColor(paramInt1);
    }
  }
  
  private void setDarkDecorCaptionShade(DecorCaptionView paramDecorCaptionView)
  {
    paramDecorCaptionView.findViewById(16909109).setBackgroundResource(17302383);
    paramDecorCaptionView.findViewById(16908864).setBackgroundResource(17302381);
  }
  
  private void setDecorCaptionShade(Context paramContext, DecorCaptionView paramDecorCaptionView)
  {
    switch (mWindow.getDecorCaptionShade())
    {
    default: 
      TypedValue localTypedValue = new TypedValue();
      paramContext.getTheme().resolveAttribute(16843827, localTypedValue, true);
      if (Color.luminance(data) < 0.5D) {
        setLightDecorCaptionShade(paramDecorCaptionView);
      }
      break;
    case 2: 
      setDarkDecorCaptionShade(paramDecorCaptionView);
      break;
    case 1: 
      setLightDecorCaptionShade(paramDecorCaptionView);
      break;
    }
    setDarkDecorCaptionShade(paramDecorCaptionView);
  }
  
  private void setHandledActionMode(ActionMode paramActionMode)
  {
    if (paramActionMode.getType() == 0) {
      setHandledPrimaryActionMode(paramActionMode);
    } else if (paramActionMode.getType() == 1) {
      setHandledFloatingActionMode(paramActionMode);
    }
  }
  
  private void setHandledFloatingActionMode(ActionMode paramActionMode)
  {
    mFloatingActionMode = paramActionMode;
    mFloatingActionMode.invalidate();
    mFloatingActionModeOriginatingView.getViewTreeObserver().addOnPreDrawListener(mFloatingToolbarPreDrawListener);
  }
  
  private void setHandledPrimaryActionMode(ActionMode paramActionMode)
  {
    endOnGoingFadeAnimation();
    mPrimaryActionMode = paramActionMode;
    mPrimaryActionMode.invalidate();
    mPrimaryActionModeView.initForMode(mPrimaryActionMode);
    if (mPrimaryActionModePopup != null)
    {
      post(mShowPrimaryActionModePopup);
    }
    else if (shouldAnimatePrimaryActionModeView())
    {
      mFadeAnim = ObjectAnimator.ofFloat(mPrimaryActionModeView, View.ALPHA, new float[] { 0.0F, 1.0F });
      mFadeAnim.addListener(new AnimatorListenerAdapter()
      {
        public void onAnimationEnd(Animator paramAnonymousAnimator)
        {
          mPrimaryActionModeView.setAlpha(1.0F);
          DecorView.access$402(DecorView.this, null);
        }
        
        public void onAnimationStart(Animator paramAnonymousAnimator)
        {
          mPrimaryActionModeView.setVisibility(0);
        }
      });
      mFadeAnim.start();
    }
    else
    {
      mPrimaryActionModeView.setAlpha(1.0F);
      mPrimaryActionModeView.setVisibility(0);
    }
    mPrimaryActionModeView.sendAccessibilityEvent(32);
  }
  
  private void setLightDecorCaptionShade(DecorCaptionView paramDecorCaptionView)
  {
    paramDecorCaptionView.findViewById(16909109).setBackgroundResource(17302384);
    paramDecorCaptionView.findViewById(16908864).setBackgroundResource(17302382);
  }
  
  private boolean showContextMenuForChildInternal(View paramView, float paramFloat1, float paramFloat2)
  {
    if (mWindow.mContextMenuHelper != null)
    {
      mWindow.mContextMenuHelper.dismiss();
      mWindow.mContextMenuHelper = null;
    }
    PhoneWindow.PhoneWindowMenuCallback localPhoneWindowMenuCallback = mWindow.mContextMenuCallback;
    if (mWindow.mContextMenu == null)
    {
      mWindow.mContextMenu = new ContextMenuBuilder(getContext());
      mWindow.mContextMenu.setCallback(localPhoneWindowMenuCallback);
    }
    else
    {
      mWindow.mContextMenu.clearAll();
    }
    boolean bool1 = Float.isNaN(paramFloat1);
    boolean bool2 = false;
    int i;
    if ((!bool1) && (!Float.isNaN(paramFloat2))) {
      i = 1;
    } else {
      i = 0;
    }
    if (i != 0) {
      paramView = mWindow.mContextMenu.showPopup(getContext(), paramView, paramFloat1, paramFloat2);
    } else {
      paramView = mWindow.mContextMenu.showDialog(paramView, paramView.getWindowToken());
    }
    if (paramView != null)
    {
      if (i == 0) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      localPhoneWindowMenuCallback.setShowDialogForSubmenu(bool1);
      paramView.setPresenterCallback(localPhoneWindowMenuCallback);
    }
    mWindow.mContextMenuHelper = paramView;
    bool1 = bool2;
    if (paramView != null) {
      bool1 = true;
    }
    return bool1;
  }
  
  private ActionMode startActionMode(View paramView, ActionMode.Callback paramCallback, int paramInt)
  {
    ActionModeCallback2Wrapper localActionModeCallback2Wrapper = new ActionModeCallback2Wrapper(paramCallback);
    Object localObject = null;
    paramCallback = localObject;
    if (mWindow.getCallback() != null)
    {
      paramCallback = localObject;
      if (!mWindow.isDestroyed()) {
        try
        {
          paramCallback = mWindow.getCallback().onWindowStartingActionMode(localActionModeCallback2Wrapper, paramInt);
        }
        catch (AbstractMethodError paramCallback)
        {
          paramCallback = localObject;
          if (paramInt == 0) {
            try
            {
              paramCallback = mWindow.getCallback().onWindowStartingActionMode(localActionModeCallback2Wrapper);
            }
            catch (AbstractMethodError paramCallback)
            {
              paramCallback = localObject;
            }
          }
        }
      }
    }
    if (paramCallback != null)
    {
      if (paramCallback.getType() == 0)
      {
        cleanupPrimaryActionMode();
        mPrimaryActionMode = paramCallback;
        paramView = paramCallback;
      }
      else
      {
        paramView = paramCallback;
        if (paramCallback.getType() == 1)
        {
          if (mFloatingActionMode != null) {
            mFloatingActionMode.finish();
          }
          mFloatingActionMode = paramCallback;
          paramView = paramCallback;
        }
      }
    }
    else
    {
      paramView = createActionMode(paramInt, localActionModeCallback2Wrapper, paramView);
      if ((paramView != null) && (localActionModeCallback2Wrapper.onCreateActionMode(paramView, paramView.getMenu()))) {
        setHandledActionMode(paramView);
      } else {
        paramView = null;
      }
    }
    if ((paramView != null) && (mWindow.getCallback() != null) && (!mWindow.isDestroyed())) {
      try
      {
        mWindow.getCallback().onActionModeStarted(paramView);
      }
      catch (AbstractMethodError paramCallback) {}
    }
    return paramView;
  }
  
  private void updateAvailableWidth()
  {
    Resources localResources = getResources();
    mAvailableWidth = TypedValue.applyDimension(1, getConfigurationscreenWidthDp, localResources.getDisplayMetrics());
  }
  
  private void updateColorViewInt(final ColorViewState paramColorViewState, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2, int paramInt5, boolean paramBoolean3, boolean paramBoolean4)
  {
    present = attributes.isPresent(paramInt1, mWindow.getAttributes().flags, paramBoolean4);
    paramBoolean4 = attributes.isVisible(present, paramInt2, mWindow.getAttributes().flags, paramBoolean4);
    int i;
    if ((paramBoolean4) && (!isResizing()) && (paramInt4 > 0)) {
      i = 1;
    } else {
      i = 0;
    }
    int j = 0;
    Object localObject1 = view;
    int k = -1;
    int m;
    if (paramBoolean1) {
      m = -1;
    } else {
      m = paramInt4;
    }
    if (paramBoolean1) {
      k = paramInt4;
    }
    if (paramBoolean1)
    {
      if (paramBoolean2) {
        paramInt1 = attributes.seascapeGravity;
      } else {
        paramInt1 = attributes.horizontalGravity;
      }
    }
    else {
      paramInt1 = attributes.verticalGravity;
    }
    Object localObject2;
    if (localObject1 == null)
    {
      if (i != 0)
      {
        localObject2 = new View(mContext);
        localObject1 = localObject2;
        view = ((View)localObject2);
        setColor((View)localObject1, paramInt2, paramInt3, paramBoolean1, paramBoolean2);
        ((View)localObject1).setTransitionName(attributes.transitionName);
        ((View)localObject1).setId(attributes.id);
        paramInt3 = 1;
        ((View)localObject1).setVisibility(4);
        targetVisibility = 0;
        localObject2 = new FrameLayout.LayoutParams(k, m, paramInt1);
        if (paramBoolean2) {
          leftMargin = paramInt5;
        } else {
          rightMargin = paramInt5;
        }
        addView((View)localObject1, (ViewGroup.LayoutParams)localObject2);
        updateColorViewTranslations();
        paramInt1 = paramInt3;
      }
      else
      {
        paramInt1 = j;
      }
    }
    else
    {
      if (i != 0) {
        j = 0;
      } else {
        j = 4;
      }
      if (targetVisibility != j) {
        paramInt4 = 1;
      } else {
        paramInt4 = 0;
      }
      targetVisibility = j;
      localObject2 = (FrameLayout.LayoutParams)((View)localObject1).getLayoutParams();
      if (paramBoolean2) {
        j = 0;
      } else {
        j = paramInt5;
      }
      if (!paramBoolean2) {
        paramInt5 = 0;
      }
      if ((height == m) && (width == k) && (gravity == paramInt1)) {
        if (rightMargin == j) {
          if (leftMargin == paramInt5) {
            break label465;
          }
        } else {}
      }
      height = m;
      width = k;
      gravity = paramInt1;
      rightMargin = j;
      leftMargin = paramInt5;
      ((View)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
      label465:
      if (i != 0) {
        setColor((View)localObject1, paramInt2, paramInt3, paramBoolean1, paramBoolean2);
      }
      paramInt1 = paramInt4;
    }
    if (paramInt1 != 0)
    {
      ((View)localObject1).animate().cancel();
      if ((paramBoolean3) && (!isResizing()))
      {
        if (i != 0)
        {
          if (((View)localObject1).getVisibility() != 0)
          {
            ((View)localObject1).setVisibility(0);
            ((View)localObject1).setAlpha(0.0F);
          }
          ((View)localObject1).animate().alpha(1.0F).setInterpolator(mShowInterpolator).setDuration(mBarEnterExitDuration);
        }
        else
        {
          ((View)localObject1).animate().alpha(0.0F).setInterpolator(mHideInterpolator).setDuration(mBarEnterExitDuration).withEndAction(new Runnable()
          {
            public void run()
            {
              paramColorViewStateview.setAlpha(1.0F);
              paramColorViewStateview.setVisibility(4);
            }
          });
        }
      }
      else
      {
        paramInt1 = 0;
        ((View)localObject1).setAlpha(1.0F);
        if (i == 0) {
          paramInt1 = 4;
        }
        ((View)localObject1).setVisibility(paramInt1);
      }
    }
    visible = paramBoolean4;
    color = paramInt2;
    if ((Build.FEATURES.ENABLE_COLORFUL_NAV) && (paramColorViewState == mNavigationColorViewState)) {
      mWindow.dispatchNavigationColor();
    }
  }
  
  private void updateColorViewTranslations()
  {
    int i = mRootScrollY;
    View localView = mStatusColorViewState.view;
    float f1 = 0.0F;
    float f2;
    if (localView != null)
    {
      localView = mStatusColorViewState.view;
      if (i > 0) {
        f2 = i;
      } else {
        f2 = 0.0F;
      }
      localView.setTranslationY(f2);
    }
    if (mNavigationColorViewState.view != null)
    {
      localView = mNavigationColorViewState.view;
      f2 = f1;
      if (i < 0) {
        f2 = i;
      }
      localView.setTranslationY(f2);
    }
  }
  
  private void updateElevation()
  {
    float f1 = 0.0F;
    boolean bool = mElevationAdjustedForStack;
    int i = getResourcesgetConfigurationwindowConfiguration.getWindowingMode();
    float f2 = 5.0F;
    if ((i == 5) && (!isResizing()))
    {
      f1 = f2;
      if (hasWindowFocus()) {
        f1 = 20.0F;
      }
      if (!mAllowUpdateElevation) {
        f1 = 20.0F;
      }
      f1 = dipToPx(f1);
      mElevationAdjustedForStack = true;
    }
    else if (i == 2)
    {
      f1 = dipToPx(5.0F);
      mElevationAdjustedForStack = true;
    }
    else
    {
      mElevationAdjustedForStack = false;
    }
    if (((bool) || (mElevationAdjustedForStack)) && (getElevation() != f1)) {
      mWindow.setElevation(f1);
    }
  }
  
  private WindowInsets updateStatusGuard(WindowInsets paramWindowInsets)
  {
    int i = 0;
    int j = 0;
    Object localObject1 = mPrimaryActionModeView;
    int k = 0;
    int m = i;
    Object localObject2 = paramWindowInsets;
    if (localObject1 != null)
    {
      m = i;
      localObject2 = paramWindowInsets;
      if ((mPrimaryActionModeView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams))
      {
        ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)mPrimaryActionModeView.getLayoutParams();
        m = 0;
        i = 0;
        int n;
        if (mPrimaryActionModeView.isShown())
        {
          if (mTempRect == null) {
            mTempRect = new Rect();
          }
          localObject1 = mTempRect;
          mWindow.mContentParent.computeSystemWindowInsets(paramWindowInsets, (Rect)localObject1);
          if (top == 0) {
            n = paramWindowInsets.getSystemWindowInsetTop();
          } else {
            n = 0;
          }
          if (topMargin != n)
          {
            n = 1;
            topMargin = paramWindowInsets.getSystemWindowInsetTop();
            if (mStatusGuard == null)
            {
              mStatusGuard = new View(mContext);
              mStatusGuard.setBackgroundColor(mContext.getColor(17170648));
              addView(mStatusGuard, indexOfChild(mStatusColorViewState.view), new FrameLayout.LayoutParams(-1, topMargin, 8388659));
              i = n;
            }
            else
            {
              localObject1 = (FrameLayout.LayoutParams)mStatusGuard.getLayoutParams();
              i = n;
              if (height != topMargin)
              {
                height = topMargin;
                mStatusGuard.setLayoutParams((ViewGroup.LayoutParams)localObject1);
                i = n;
              }
            }
          }
          localObject1 = mStatusGuard;
          m = 1;
          if (localObject1 != null) {
            n = 1;
          } else {
            n = 0;
          }
          if ((mWindow.getLocalFeaturesPrivate() & 0x400) != 0) {
            m = 0;
          }
          localObject1 = paramWindowInsets;
          if (m != 0)
          {
            localObject1 = paramWindowInsets;
            if (n != 0) {
              localObject1 = paramWindowInsets.inset(0, paramWindowInsets.getSystemWindowInsetTop(), 0, 0);
            }
          }
        }
        else
        {
          n = j;
          i = m;
          localObject1 = paramWindowInsets;
          if (topMargin != 0)
          {
            i = 1;
            topMargin = 0;
            localObject1 = paramWindowInsets;
            n = j;
          }
        }
        m = n;
        localObject2 = localObject1;
        if (i != 0)
        {
          mPrimaryActionModeView.setLayoutParams(localMarginLayoutParams);
          localObject2 = localObject1;
          m = n;
        }
      }
    }
    if (mStatusGuard != null)
    {
      paramWindowInsets = mStatusGuard;
      if (m != 0) {
        i = k;
      } else {
        i = 8;
      }
      paramWindowInsets.setVisibility(i);
    }
    return localObject2;
  }
  
  void clearContentView()
  {
    if (mDecorCaptionView != null) {
      mDecorCaptionView.removeContentView();
    } else {
      for (int i = getChildCount() - 1; i >= 0; i--)
      {
        View localView = getChildAt(i);
        if ((localView != mStatusColorViewState.view) && (localView != mNavigationColorViewState.view) && (localView != mStatusGuard)) {
          removeViewAt(i);
        }
      }
    }
  }
  
  public boolean dispatchGenericMotionEvent(MotionEvent paramMotionEvent)
  {
    Window.Callback localCallback = mWindow.getCallback();
    boolean bool;
    if ((localCallback != null) && (!mWindow.isDestroyed()) && (mFeatureId < 0)) {
      bool = localCallback.dispatchGenericMotionEvent(paramMotionEvent);
    } else {
      bool = super.dispatchGenericMotionEvent(paramMotionEvent);
    }
    return bool;
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    int i = paramKeyEvent.getKeyCode();
    int j;
    if (paramKeyEvent.getAction() == 0) {
      j = 1;
    } else {
      j = 0;
    }
    if ((j != 0) && (paramKeyEvent.getRepeatCount() == 0))
    {
      if ((mWindow.mPanelChordingKey > 0) && (mWindow.mPanelChordingKey != i) && (dispatchKeyShortcutEvent(paramKeyEvent))) {
        return true;
      }
      if ((mWindow.mPreparedPanel != null) && (mWindow.mPreparedPanel.isOpen) && (mWindow.performPanelShortcut(mWindow.mPreparedPanel, i, paramKeyEvent, 0))) {
        return true;
      }
    }
    boolean bool;
    if (!mWindow.isDestroyed())
    {
      Window.Callback localCallback = mWindow.getCallback();
      if ((localCallback != null) && (mFeatureId < 0)) {
        bool = localCallback.dispatchKeyEvent(paramKeyEvent);
      } else {
        bool = super.dispatchKeyEvent(paramKeyEvent);
      }
      if (bool) {
        return true;
      }
    }
    if (j != 0) {
      bool = mWindow.onKeyDown(mFeatureId, paramKeyEvent.getKeyCode(), paramKeyEvent);
    } else {
      bool = mWindow.onKeyUp(mFeatureId, paramKeyEvent.getKeyCode(), paramKeyEvent);
    }
    return bool;
  }
  
  public boolean dispatchKeyShortcutEvent(KeyEvent paramKeyEvent)
  {
    if ((mWindow.mPreparedPanel != null) && (mWindow.performPanelShortcut(mWindow.mPreparedPanel, paramKeyEvent.getKeyCode(), paramKeyEvent, 1)))
    {
      if (mWindow.mPreparedPanel != null) {
        mWindow.mPreparedPanel.isHandled = true;
      }
      return true;
    }
    Object localObject = mWindow.getCallback();
    boolean bool;
    if ((localObject != null) && (!mWindow.isDestroyed()) && (mFeatureId < 0)) {
      bool = ((Window.Callback)localObject).dispatchKeyShortcutEvent(paramKeyEvent);
    } else {
      bool = super.dispatchKeyShortcutEvent(paramKeyEvent);
    }
    if (bool) {
      return true;
    }
    localObject = mWindow.getPanelState(0, false);
    if ((localObject != null) && (mWindow.mPreparedPanel == null))
    {
      mWindow.preparePanel((PhoneWindow.PanelFeatureState)localObject, paramKeyEvent);
      bool = mWindow.performPanelShortcut((PhoneWindow.PanelFeatureState)localObject, paramKeyEvent.getKeyCode(), paramKeyEvent, 1);
      isPrepared = false;
      if (bool) {
        return true;
      }
    }
    return false;
  }
  
  public void dispatchPointerCaptureChanged(boolean paramBoolean)
  {
    super.dispatchPointerCaptureChanged(paramBoolean);
    if ((!mWindow.isDestroyed()) && (mWindow.getCallback() != null)) {
      mWindow.getCallback().onPointerCaptureChanged(paramBoolean);
    }
  }
  
  public boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent paramAccessibilityEvent)
  {
    Window.Callback localCallback = mWindow.getCallback();
    if ((localCallback != null) && (!mWindow.isDestroyed()) && (localCallback.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent))) {
      return true;
    }
    return super.dispatchPopulateAccessibilityEventInternal(paramAccessibilityEvent);
  }
  
  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
  {
    Window.Callback localCallback = mWindow.getCallback();
    boolean bool;
    if ((localCallback != null) && (!mWindow.isDestroyed()) && (mFeatureId < 0)) {
      bool = localCallback.dispatchTouchEvent(paramMotionEvent);
    } else {
      bool = super.dispatchTouchEvent(paramMotionEvent);
    }
    return bool;
  }
  
  public boolean dispatchTrackballEvent(MotionEvent paramMotionEvent)
  {
    Window.Callback localCallback = mWindow.getCallback();
    boolean bool;
    if ((localCallback != null) && (!mWindow.isDestroyed()) && (mFeatureId < 0)) {
      bool = localCallback.dispatchTrackballEvent(paramMotionEvent);
    } else {
      bool = super.dispatchTrackballEvent(paramMotionEvent);
    }
    return bool;
  }
  
  public void draw(Canvas paramCanvas)
  {
    super.draw(paramCanvas);
    if (mMenuBackground != null) {
      mMenuBackground.draw(paramCanvas);
    }
  }
  
  void enableCaption(boolean paramBoolean)
  {
    if (mHasCaption != paramBoolean)
    {
      mHasCaption = paramBoolean;
      if (getForeground() != null) {
        drawableChanged();
      }
    }
  }
  
  void finishChanging()
  {
    mChanging = false;
    drawableChanged();
  }
  
  public boolean gatherTransparentRegion(Region paramRegion)
  {
    boolean bool1 = gatherTransparentRegion(mStatusColorViewState, paramRegion);
    boolean bool2 = gatherTransparentRegion(mNavigationColorViewState, paramRegion);
    boolean bool3 = super.gatherTransparentRegion(paramRegion);
    if ((!bool1) && (!bool2) && (!bool3)) {
      bool3 = false;
    } else {
      bool3 = true;
    }
    return bool3;
  }
  
  boolean gatherTransparentRegion(ColorViewState paramColorViewState, Region paramRegion)
  {
    if ((view != null) && (visible) && (isResizing())) {
      return view.gatherTransparentRegion(paramRegion);
    }
    return false;
  }
  
  public int getAccessibilityViewId()
  {
    return 2147483646;
  }
  
  int getCaptionHeight()
  {
    int i;
    if (isShowingCaption()) {
      i = mDecorCaptionView.getCaptionHeight();
    } else {
      i = 0;
    }
    return i;
  }
  
  int getNavigationColor()
  {
    if ((mNavigationColorViewState.view != null) && (mNavigationColorViewState.view.getVisibility() == 0)) {
      return mNavigationColorViewState.color;
    }
    return -16777216;
  }
  
  public Resources getResources()
  {
    return getContext().getResources();
  }
  
  boolean isShowingCaption()
  {
    boolean bool;
    if ((mDecorCaptionView != null) && (mDecorCaptionView.isCaptionShowing())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isTransitionGroup()
  {
    return false;
  }
  
  public WindowInsets onApplyWindowInsets(WindowInsets paramWindowInsets)
  {
    WindowManager.LayoutParams localLayoutParams = mWindow.getAttributes();
    mFloatingInsets.setEmpty();
    Object localObject = paramWindowInsets;
    if ((flags & 0x100) == 0)
    {
      WindowInsets localWindowInsets = paramWindowInsets;
      if (height == -2)
      {
        mFloatingInsets.top = paramWindowInsets.getSystemWindowInsetTop();
        mFloatingInsets.bottom = paramWindowInsets.getSystemWindowInsetBottom();
        localWindowInsets = paramWindowInsets.inset(0, paramWindowInsets.getSystemWindowInsetTop(), 0, paramWindowInsets.getSystemWindowInsetBottom());
      }
      localObject = localWindowInsets;
      if (mWindow.getAttributes().width == -2)
      {
        mFloatingInsets.left = localWindowInsets.getSystemWindowInsetTop();
        mFloatingInsets.right = localWindowInsets.getSystemWindowInsetBottom();
        localObject = localWindowInsets.inset(localWindowInsets.getSystemWindowInsetLeft(), 0, localWindowInsets.getSystemWindowInsetRight(), 0);
      }
    }
    mFrameOffsets.set(((WindowInsets)localObject).getSystemWindowInsets());
    paramWindowInsets = updateStatusGuard(updateColorViews((WindowInsets)localObject, true));
    if (getForeground() != null) {
      drawableChanged();
    }
    return paramWindowInsets;
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    Window.Callback localCallback = mWindow.getCallback();
    if ((localCallback != null) && (!mWindow.isDestroyed()) && (mFeatureId < 0)) {
      localCallback.onAttachedToWindow();
    }
    if (mFeatureId == -1) {
      mWindow.openPanelsAfterRestore();
    }
    if (!mWindowResizeCallbacksAdded)
    {
      getViewRootImpl().addWindowCallbacks(this);
      mWindowResizeCallbacksAdded = true;
    }
    else if (mBackdropFrameRenderer != null)
    {
      mBackdropFrameRenderer.onConfigurationChange();
    }
    mWindow.onViewRootImplSet(getViewRootImpl());
  }
  
  public void onCloseSystemDialogs(String paramString)
  {
    if (mFeatureId >= 0) {
      mWindow.closeAllPanels();
    }
  }
  
  protected void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    boolean bool = windowConfiguration.hasWindowDecorCaption();
    if ((mDecorCaptionView == null) && (bool))
    {
      mDecorCaptionView = createDecorCaptionView(mWindow.getLayoutInflater());
      if (mDecorCaptionView != null)
      {
        if (mDecorCaptionView.getParent() == null) {
          addView(mDecorCaptionView, 0, new ViewGroup.LayoutParams(-1, -1));
        }
        removeView(mContentRoot);
        mDecorCaptionView.addView(mContentRoot, new ViewGroup.MarginLayoutParams(-1, -1));
      }
    }
    else if (mDecorCaptionView != null)
    {
      mDecorCaptionView.onConfigurationChanged(bool);
      enableCaption(bool);
    }
    updateAvailableWidth();
    initializeElevation();
  }
  
  public boolean onContentDrawn(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (mBackdropFrameRenderer == null) {
      return false;
    }
    return mBackdropFrameRenderer.onContentDrawn(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    Object localObject = mWindow.getCallback();
    if ((localObject != null) && (mFeatureId < 0)) {
      ((Window.Callback)localObject).onDetachedFromWindow();
    }
    if (mWindow.mDecorContentParent != null) {
      mWindow.mDecorContentParent.dismissPopups();
    }
    if (mPrimaryActionModePopup != null)
    {
      removeCallbacks(mShowPrimaryActionModePopup);
      if (mPrimaryActionModePopup.isShowing()) {
        mPrimaryActionModePopup.dismiss();
      }
      mPrimaryActionModePopup = null;
    }
    if (mFloatingToolbar != null)
    {
      mFloatingToolbar.dismiss();
      mFloatingToolbar = null;
    }
    localObject = mWindow.getPanelState(0, false);
    if ((localObject != null) && (menu != null) && (mFeatureId < 0)) {
      menu.close();
    }
    releaseThreadedRenderer();
    if (mWindowResizeCallbacksAdded)
    {
      getViewRootImpl().removeWindowCallbacks(this);
      mWindowResizeCallbacksAdded = false;
    }
  }
  
  public void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    mBackgroundFallback.draw(this, mContentRoot, paramCanvas, mWindow.mContentParent, mStatusColorViewState.view, mNavigationColorViewState.view);
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction();
    if ((mHasCaption) && (isShowingCaption()) && (i == 0) && (isOutOfInnerBounds((int)paramMotionEvent.getX(), (int)paramMotionEvent.getY()))) {
      return true;
    }
    if ((mFeatureId >= 0) && (i == 0) && (isOutOfBounds((int)paramMotionEvent.getX(), (int)paramMotionEvent.getY())))
    {
      mWindow.closePanel(mFeatureId);
      return true;
    }
    return false;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    getOutsets(mOutsets);
    if (mOutsets.left > 0) {
      offsetLeftAndRight(-mOutsets.left);
    }
    if (mOutsets.top > 0) {
      offsetTopAndBottom(-mOutsets.top);
    }
    if (mApplyFloatingVerticalInsets) {
      offsetTopAndBottom(mFloatingInsets.top);
    }
    if (mApplyFloatingHorizontalInsets) {
      offsetLeftAndRight(mFloatingInsets.left);
    }
    updateElevation();
    mAllowUpdateElevation = true;
    if ((paramBoolean) && (mResizeMode == 1)) {
      getViewRootImpl().requestInvalidateRootRenderNode();
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    DisplayMetrics localDisplayMetrics = getContext().getResources().getDisplayMetrics();
    int i;
    if (getResourcesgetConfigurationorientation == 1) {
      i = 1;
    } else {
      i = 0;
    }
    int j = View.MeasureSpec.getMode(paramInt1);
    int k = View.MeasureSpec.getMode(paramInt2);
    int m = 0;
    mApplyFloatingHorizontalInsets = false;
    TypedValue localTypedValue;
    int n;
    if (j == Integer.MIN_VALUE)
    {
      if (i != 0) {
        localTypedValue = mWindow.mFixedWidthMinor;
      } else {
        localTypedValue = mWindow.mFixedWidthMajor;
      }
      if ((localTypedValue != null) && (type != 0))
      {
        if (type == 5) {
          n = (int)localTypedValue.getDimension(localDisplayMetrics);
        }
        for (;;)
        {
          break;
          if (type == 6) {
            n = (int)localTypedValue.getFraction(widthPixels, widthPixels);
          } else {
            n = 0;
          }
        }
        paramInt1 = View.MeasureSpec.getSize(paramInt1);
        if (n > 0)
        {
          paramInt1 = View.MeasureSpec.makeMeasureSpec(Math.min(n, paramInt1), 1073741824);
          m = 1;
        }
        else
        {
          paramInt1 = View.MeasureSpec.makeMeasureSpec(paramInt1 - mFloatingInsets.left - mFloatingInsets.right, Integer.MIN_VALUE);
          mApplyFloatingHorizontalInsets = true;
        }
      }
    }
    mApplyFloatingVerticalInsets = false;
    if (k == Integer.MIN_VALUE)
    {
      if (i != 0) {
        localTypedValue = mWindow.mFixedHeightMajor;
      } else {
        localTypedValue = mWindow.mFixedHeightMinor;
      }
      if ((localTypedValue != null) && (type != 0))
      {
        if (type == 5) {
          n = (int)localTypedValue.getDimension(localDisplayMetrics);
        }
        for (;;)
        {
          break;
          if (type == 6) {
            n = (int)localTypedValue.getFraction(heightPixels, heightPixels);
          } else {
            n = 0;
          }
        }
        k = View.MeasureSpec.getSize(paramInt2);
        if (n > 0)
        {
          paramInt2 = View.MeasureSpec.makeMeasureSpec(Math.min(n, k), 1073741824);
        }
        else if ((mWindow.getAttributes().flags & 0x100) == 0)
        {
          paramInt2 = View.MeasureSpec.makeMeasureSpec(k - mFloatingInsets.top - mFloatingInsets.bottom, Integer.MIN_VALUE);
          mApplyFloatingVerticalInsets = true;
        }
      }
    }
    getOutsets(mOutsets);
    if (mOutsets.top <= 0)
    {
      n = paramInt2;
      if (mOutsets.bottom <= 0) {}
    }
    else
    {
      k = View.MeasureSpec.getMode(paramInt2);
      n = paramInt2;
      if (k != 0)
      {
        paramInt2 = View.MeasureSpec.getSize(paramInt2);
        n = View.MeasureSpec.makeMeasureSpec(mOutsets.top + paramInt2 + mOutsets.bottom, k);
      }
    }
    if (mOutsets.left <= 0)
    {
      paramInt2 = paramInt1;
      if (mOutsets.right <= 0) {}
    }
    else
    {
      k = View.MeasureSpec.getMode(paramInt1);
      paramInt2 = paramInt1;
      if (k != 0)
      {
        paramInt1 = View.MeasureSpec.getSize(paramInt1);
        paramInt2 = View.MeasureSpec.makeMeasureSpec(mOutsets.left + paramInt1 + mOutsets.right, k);
      }
    }
    super.onMeasure(paramInt2, n);
    int i1 = getMeasuredWidth();
    k = 0;
    int i2 = View.MeasureSpec.makeMeasureSpec(i1, 1073741824);
    paramInt2 = k;
    paramInt1 = i2;
    if (m == 0)
    {
      paramInt2 = k;
      paramInt1 = i2;
      if (j == Integer.MIN_VALUE)
      {
        if (i != 0) {
          localTypedValue = mWindow.mMinWidthMinor;
        } else {
          localTypedValue = mWindow.mMinWidthMajor;
        }
        paramInt2 = k;
        paramInt1 = i2;
        if (type != 0)
        {
          if (type == 5) {}
          for (paramInt1 = (int)localTypedValue.getDimension(localDisplayMetrics);; paramInt1 = (int)localTypedValue.getFraction(mAvailableWidth, mAvailableWidth))
          {
            i = paramInt1;
            break label684;
            if (type != 6) {
              break;
            }
          }
          i = 0;
          label684:
          paramInt2 = k;
          paramInt1 = i2;
          if (i1 < i)
          {
            paramInt1 = View.MeasureSpec.makeMeasureSpec(i, 1073741824);
            paramInt2 = 1;
          }
        }
      }
    }
    if (paramInt2 != 0) {
      super.onMeasure(paramInt1, n);
    }
  }
  
  public void onPostDraw(DisplayListCanvas paramDisplayListCanvas)
  {
    drawResizingShadowIfNeeded(paramDisplayListCanvas);
  }
  
  public void onRequestDraw(boolean paramBoolean)
  {
    if (mBackdropFrameRenderer != null) {
      mBackdropFrameRenderer.onRequestDraw(paramBoolean);
    } else if ((paramBoolean) && (isAttachedToWindow())) {
      getViewRootImpl().reportDrawFinish();
    }
  }
  
  void onResourcesLoaded(LayoutInflater paramLayoutInflater, int paramInt)
  {
    if (mBackdropFrameRenderer != null)
    {
      loadBackgroundDrawablesIfNeeded();
      mBackdropFrameRenderer.onResourcesLoaded(this, mResizingBackgroundDrawable, mCaptionBackgroundDrawable, mUserCaptionBackgroundDrawable, getCurrentColor(mStatusColorViewState), getCurrentColor(mNavigationColorViewState));
    }
    mDecorCaptionView = createDecorCaptionView(paramLayoutInflater);
    paramLayoutInflater = paramLayoutInflater.inflate(paramInt, null);
    if (mDecorCaptionView != null)
    {
      if (mDecorCaptionView.getParent() == null) {
        addView(mDecorCaptionView, new ViewGroup.LayoutParams(-1, -1));
      }
      mDecorCaptionView.addView(paramLayoutInflater, new ViewGroup.MarginLayoutParams(-1, -1));
    }
    else
    {
      addView(paramLayoutInflater, 0, new ViewGroup.LayoutParams(-1, -1));
    }
    mContentRoot = ((ViewGroup)paramLayoutInflater);
    initializeElevation();
  }
  
  public void onRootViewScrollYChanged(int paramInt)
  {
    mRootScrollY = paramInt;
    updateColorViewTranslations();
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    return onInterceptTouchEvent(paramMotionEvent);
  }
  
  public void onWindowDragResizeEnd()
  {
    releaseThreadedRenderer();
    updateColorViews(null, false);
    mResizeMode = -1;
    getViewRootImpl().requestInvalidateRootRenderNode();
  }
  
  public void onWindowDragResizeStart(Rect paramRect1, boolean paramBoolean, Rect paramRect2, Rect paramRect3, int paramInt)
  {
    if (mWindow.isDestroyed())
    {
      releaseThreadedRenderer();
      return;
    }
    if (mBackdropFrameRenderer != null) {
      return;
    }
    ThreadedRenderer localThreadedRenderer = getThreadedRenderer();
    if (localThreadedRenderer != null)
    {
      loadBackgroundDrawablesIfNeeded();
      mBackdropFrameRenderer = new BackdropFrameRenderer(this, localThreadedRenderer, paramRect1, mResizingBackgroundDrawable, mCaptionBackgroundDrawable, mUserCaptionBackgroundDrawable, getCurrentColor(mStatusColorViewState), getCurrentColor(mNavigationColorViewState), paramBoolean, paramRect2, paramRect3, paramInt);
      updateElevation();
      updateColorViews(null, false);
    }
    mResizeMode = paramInt;
    getViewRootImpl().requestInvalidateRootRenderNode();
  }
  
  public void onWindowFocusChanged(boolean paramBoolean)
  {
    super.onWindowFocusChanged(paramBoolean);
    if ((mWindow.hasFeature(0)) && (!paramBoolean) && (mWindow.mPanelChordingKey != 0)) {
      mWindow.closePanel(0);
    }
    Window.Callback localCallback = mWindow.getCallback();
    if ((localCallback != null) && (!mWindow.isDestroyed()) && (mFeatureId < 0)) {
      localCallback.onWindowFocusChanged(paramBoolean);
    }
    if (mPrimaryActionMode != null) {
      mPrimaryActionMode.onWindowFocusChanged(paramBoolean);
    }
    if (mFloatingActionMode != null) {
      mFloatingActionMode.onWindowFocusChanged(paramBoolean);
    }
    updateElevation();
  }
  
  public void onWindowSizeIsChanging(Rect paramRect1, boolean paramBoolean, Rect paramRect2, Rect paramRect3)
  {
    if (mBackdropFrameRenderer != null) {
      mBackdropFrameRenderer.setTargetRect(paramRect1, paramBoolean, paramRect2, paramRect3);
    }
  }
  
  public void onWindowSystemUiVisibilityChanged(int paramInt)
  {
    updateColorViews(null, true);
  }
  
  public void requestKeyboardShortcuts(List<KeyboardShortcutGroup> paramList, int paramInt)
  {
    Object localObject = mWindow.getPanelState(0, false);
    if (localObject != null) {
      localObject = menu;
    } else {
      localObject = null;
    }
    if ((!mWindow.isDestroyed()) && (mWindow.getCallback() != null)) {
      mWindow.getCallback().onProvideKeyboardShortcuts(paramList, (Menu)localObject, paramInt);
    }
  }
  
  public void sendAccessibilityEvent(int paramInt)
  {
    if (!AccessibilityManager.getInstance(mContext).isEnabled()) {
      return;
    }
    if (((mFeatureId == 0) || (mFeatureId == 6) || (mFeatureId == 2) || (mFeatureId == 5)) && (getChildCount() == 1)) {
      getChildAt(0).sendAccessibilityEvent(paramInt);
    } else {
      super.sendAccessibilityEvent(paramInt);
    }
  }
  
  void setBackgroundFallback(int paramInt)
  {
    BackgroundFallback localBackgroundFallback = mBackgroundFallback;
    Drawable localDrawable;
    if (paramInt != 0) {
      localDrawable = getContext().getDrawable(paramInt);
    } else {
      localDrawable = null;
    }
    localBackgroundFallback.setDrawable(localDrawable);
    boolean bool;
    if ((getBackground() == null) && (!mBackgroundFallback.hasFallback())) {
      bool = true;
    } else {
      bool = false;
    }
    setWillNotDraw(bool);
  }
  
  protected boolean setFrame(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    boolean bool = super.setFrame(paramInt1, paramInt2, paramInt3, paramInt4);
    if (bool)
    {
      Rect localRect = mDrawingBounds;
      getDrawingRect(localRect);
      Object localObject1 = getForeground();
      if (localObject1 != null)
      {
        localObject2 = mFrameOffsets;
        left += left;
        top += top;
        right -= right;
        bottom -= bottom;
        ((Drawable)localObject1).setBounds(localRect);
        localObject1 = mFramePadding;
        left += left - left;
        top += top - top;
        right -= right - right;
        bottom -= bottom - bottom;
      }
      Object localObject2 = getBackground();
      if (localObject2 != null) {
        ((Drawable)localObject2).setBounds(localRect);
      }
    }
    return bool;
  }
  
  public void setOutlineProvider(ViewOutlineProvider paramViewOutlineProvider)
  {
    super.setOutlineProvider(paramViewOutlineProvider);
    mLastOutlineProvider = paramViewOutlineProvider;
  }
  
  void setOverrideSystemUiVisibility(int paramInt)
  {
    mOverrideSystemUiVisibility = paramInt;
    setSystemUiVisibility(mOriSystemUiVisibility);
  }
  
  public void setSurfaceFormat(int paramInt)
  {
    mWindow.setFormat(paramInt);
  }
  
  public void setSurfaceKeepScreenOn(boolean paramBoolean)
  {
    if (paramBoolean) {
      mWindow.addFlags(128);
    } else {
      mWindow.clearFlags(128);
    }
  }
  
  public void setSurfaceType(int paramInt)
  {
    mWindow.setType(paramInt);
  }
  
  public void setSystemUiVisibility(int paramInt)
  {
    mOriSystemUiVisibility = paramInt;
    super.setSystemUiVisibility(mOriSystemUiVisibility | mOverrideSystemUiVisibility);
  }
  
  void setUserCaptionBackgroundDrawable(Drawable paramDrawable)
  {
    mUserCaptionBackgroundDrawable = paramDrawable;
    if (mBackdropFrameRenderer != null) {
      mBackdropFrameRenderer.setUserCaptionBackgroundDrawable(paramDrawable);
    }
  }
  
  void setWindow(PhoneWindow paramPhoneWindow)
  {
    mWindow = paramPhoneWindow;
    paramPhoneWindow = getContext();
    if ((paramPhoneWindow instanceof DecorContext)) {
      ((DecorContext)paramPhoneWindow).setPhoneWindow(mWindow);
    }
  }
  
  public void setWindowBackground(Drawable paramDrawable)
  {
    if (getBackground() != paramDrawable)
    {
      setBackgroundDrawable(paramDrawable);
      boolean bool1 = true;
      boolean bool2 = true;
      if (paramDrawable != null)
      {
        if ((!mWindow.isTranslucent()) && (!mWindow.isShowingWallpaper())) {
          bool2 = false;
        }
        mResizingBackgroundDrawable = enforceNonTranslucentBackground(paramDrawable, bool2);
      }
      else
      {
        paramDrawable = getContext();
        int i = mWindow.mBackgroundFallbackResource;
        if ((!mWindow.isTranslucent()) && (!mWindow.isShowingWallpaper())) {
          bool2 = false;
        } else {
          bool2 = bool1;
        }
        mResizingBackgroundDrawable = getResizingBackgroundDrawable(paramDrawable, 0, i, bool2);
      }
      if (mResizingBackgroundDrawable != null) {
        mResizingBackgroundDrawable.getPadding(mBackgroundPadding);
      } else {
        mBackgroundPadding.setEmpty();
      }
      drawableChanged();
    }
  }
  
  public void setWindowFrame(Drawable paramDrawable)
  {
    if (getForeground() != paramDrawable)
    {
      setForeground(paramDrawable);
      if (paramDrawable != null) {
        paramDrawable.getPadding(mFramePadding);
      } else {
        mFramePadding.setEmpty();
      }
      drawableChanged();
    }
  }
  
  boolean shouldAnimatePrimaryActionModeView()
  {
    return isLaidOut();
  }
  
  public boolean showContextMenuForChild(View paramView)
  {
    return showContextMenuForChildInternal(paramView, NaN.0F, NaN.0F);
  }
  
  public boolean showContextMenuForChild(View paramView, float paramFloat1, float paramFloat2)
  {
    return showContextMenuForChildInternal(paramView, paramFloat1, paramFloat2);
  }
  
  public ActionMode startActionMode(ActionMode.Callback paramCallback)
  {
    return startActionMode(paramCallback, 0);
  }
  
  public ActionMode startActionMode(ActionMode.Callback paramCallback, int paramInt)
  {
    return startActionMode(this, paramCallback, paramInt);
  }
  
  public ActionMode startActionModeForChild(View paramView, ActionMode.Callback paramCallback)
  {
    return startActionModeForChild(paramView, paramCallback, 0);
  }
  
  public ActionMode startActionModeForChild(View paramView, ActionMode.Callback paramCallback, int paramInt)
  {
    return startActionMode(paramView, paramCallback, paramInt);
  }
  
  void startChanging()
  {
    mChanging = true;
  }
  
  public boolean superDispatchGenericMotionEvent(MotionEvent paramMotionEvent)
  {
    return super.dispatchGenericMotionEvent(paramMotionEvent);
  }
  
  public boolean superDispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    int i = paramKeyEvent.getKeyCode();
    boolean bool = true;
    if (i == 4)
    {
      i = paramKeyEvent.getAction();
      if (mPrimaryActionMode != null)
      {
        if (i == 1) {
          mPrimaryActionMode.finish();
        }
        return true;
      }
    }
    if (super.dispatchKeyEvent(paramKeyEvent)) {
      return true;
    }
    if ((getViewRootImpl() == null) || (!getViewRootImpl().dispatchUnhandledKeyEvent(paramKeyEvent))) {
      bool = false;
    }
    return bool;
  }
  
  public boolean superDispatchKeyShortcutEvent(KeyEvent paramKeyEvent)
  {
    return super.dispatchKeyShortcutEvent(paramKeyEvent);
  }
  
  public boolean superDispatchTouchEvent(MotionEvent paramMotionEvent)
  {
    return super.dispatchTouchEvent(paramMotionEvent);
  }
  
  public boolean superDispatchTrackballEvent(MotionEvent paramMotionEvent)
  {
    return super.dispatchTrackballEvent(paramMotionEvent);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("DecorView@");
    localStringBuilder.append(Integer.toHexString(hashCode()));
    localStringBuilder.append("[");
    localStringBuilder.append(getTitleSuffix(mWindow.getAttributes()));
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  WindowInsets updateColorViews(WindowInsets paramWindowInsets, boolean paramBoolean)
  {
    WindowInsets localWindowInsets = paramWindowInsets;
    WindowManager.LayoutParams localLayoutParams = mWindow.getAttributes();
    int i = systemUiVisibility | getWindowSystemUiVisibility();
    int j = mWindow.getAttributes().type;
    int k = 1;
    int m = 0;
    if (j == 2011) {
      j = 1;
    } else {
      j = 0;
    }
    int i1;
    int i2;
    int i3;
    if ((!mWindow.mIsFloating) || (j != 0))
    {
      boolean bool1 = isLaidOut();
      if (((mLastWindowFlags ^ flags) & 0x80000000) != 0) {
        j = 1;
      } else {
        j = 0;
      }
      int n = bool1 ^ true | j;
      mLastWindowFlags = flags;
      i1 = n;
      if (localWindowInsets != null)
      {
        mLastTopInset = getColorViewTopInset(paramWindowInsets.getStableInsetTop(), paramWindowInsets.getSystemWindowInsetTop());
        mLastBottomInset = getColorViewBottomInset(paramWindowInsets.getStableInsetBottom(), paramWindowInsets.getSystemWindowInsetBottom());
        mLastRightInset = getColorViewRightInset(paramWindowInsets.getStableInsetRight(), paramWindowInsets.getSystemWindowInsetRight());
        mLastLeftInset = getColorViewRightInset(paramWindowInsets.getStableInsetLeft(), paramWindowInsets.getSystemWindowInsetLeft());
        if (paramWindowInsets.getStableInsetTop() != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        }
        if (bool1 != mLastHasTopStableInset) {
          j = 1;
        } else {
          j = 0;
        }
        mLastHasTopStableInset = bool1;
        if (paramWindowInsets.getStableInsetBottom() != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        }
        if (bool1 != mLastHasBottomStableInset) {
          i1 = 1;
        } else {
          i1 = 0;
        }
        mLastHasBottomStableInset = bool1;
        if (paramWindowInsets.getStableInsetRight() != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        }
        if (bool1 != mLastHasRightStableInset) {
          i2 = 1;
        } else {
          i2 = 0;
        }
        mLastHasRightStableInset = bool1;
        if (paramWindowInsets.getStableInsetLeft() != 0) {
          bool1 = true;
        } else {
          bool1 = false;
        }
        if (bool1 != mLastHasLeftStableInset) {
          i3 = 1;
        } else {
          i3 = 0;
        }
        i1 = n | j | i1 | i2 | i3;
        mLastHasLeftStableInset = bool1;
        mLastShouldAlwaysConsumeNavBar = paramWindowInsets.shouldAlwaysConsumeNavBar();
      }
      boolean bool2 = isNavBarToRightEdge(mLastBottomInset, mLastRightInset);
      boolean bool3 = isNavBarToLeftEdge(mLastBottomInset, mLastLeftInset);
      i2 = getNavBarSize(mLastBottomInset, mLastRightInset, mLastLeftInset);
      paramWindowInsets = mNavigationColorViewState;
      j = mWindow.mNavigationBarColor;
      i3 = mWindow.mNavigationBarDividerColor;
      if ((!bool2) && (!bool3)) {
        bool1 = false;
      } else {
        bool1 = true;
      }
      boolean bool4;
      if ((paramBoolean) && (i1 == 0)) {
        bool4 = true;
      } else {
        bool4 = false;
      }
      updateColorViewInt(paramWindowInsets, i, j, i3, i2, bool1, bool3, 0, bool4, false);
      if ((bool2) && (mNavigationColorViewState.present)) {
        j = 1;
      } else {
        j = 0;
      }
      if ((bool3) && (mNavigationColorViewState.present)) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      if (j != 0) {
        j = mLastRightInset;
      }
      for (;;)
      {
        break;
        if (bool1) {
          j = mLastLeftInset;
        } else {
          j = 0;
        }
      }
      paramWindowInsets = mStatusColorViewState;
      i2 = calculateStatusBarColor();
      i3 = mLastTopInset;
      if ((paramBoolean) && (i1 == 0)) {
        paramBoolean = true;
      } else {
        paramBoolean = false;
      }
      updateColorViewInt(paramWindowInsets, i, i2, 0, i3, false, bool1, j, paramBoolean, mForceWindowDrawsStatusBarBackground);
    }
    if ((((flags & 0x80000000) != 0) && ((i & 0x200) == 0) && ((i & 0x2) == 0)) || (mLastShouldAlwaysConsumeNavBar)) {
      j = 1;
    } else {
      j = 0;
    }
    if (((i & 0x400) == 0) && ((i & 0x80000000) == 0) && ((flags & 0x100) == 0) && ((flags & 0x10000) == 0) && (mForceWindowDrawsStatusBarBackground) && (mLastTopInset != 0)) {
      i1 = k;
    } else {
      i1 = 0;
    }
    if (i1 != 0) {
      i1 = mLastTopInset;
    } else {
      i1 = 0;
    }
    if (j != 0) {
      i2 = mLastRightInset;
    } else {
      i2 = 0;
    }
    if (j != 0) {
      i3 = mLastBottomInset;
    } else {
      i3 = 0;
    }
    if (j != 0) {
      m = mLastLeftInset;
    }
    paramWindowInsets = localWindowInsets;
    if (mContentRoot != null)
    {
      paramWindowInsets = localWindowInsets;
      if ((mContentRoot.getLayoutParams() instanceof ViewGroup.MarginLayoutParams))
      {
        paramWindowInsets = (ViewGroup.MarginLayoutParams)mContentRoot.getLayoutParams();
        if ((topMargin != i1) || (rightMargin != i2) || (bottomMargin != i3) || (leftMargin != m))
        {
          topMargin = i1;
          rightMargin = i2;
          bottomMargin = i3;
          leftMargin = m;
          mContentRoot.setLayoutParams(paramWindowInsets);
          if (localWindowInsets == null) {
            requestApplyInsets();
          }
        }
        paramWindowInsets = localWindowInsets;
        if (localWindowInsets != null) {
          paramWindowInsets = localWindowInsets.inset(m, i1, i2, i3);
        }
      }
    }
    localWindowInsets = paramWindowInsets;
    if (paramWindowInsets != null) {
      localWindowInsets = paramWindowInsets.consumeStableInsets();
    }
    return localWindowInsets;
  }
  
  void updateDecorCaptionShade()
  {
    if (mDecorCaptionView != null) {
      setDecorCaptionShade(getContext(), mDecorCaptionView);
    }
  }
  
  void updateLogTag(WindowManager.LayoutParams paramLayoutParams)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("DecorView[");
    localStringBuilder.append(getTitleSuffix(paramLayoutParams));
    localStringBuilder.append("]");
    mLogTag = localStringBuilder.toString();
  }
  
  public void updatePictureInPictureOutlineProvider(boolean paramBoolean)
  {
    if (mIsInPictureInPictureMode == paramBoolean) {
      return;
    }
    if (paramBoolean)
    {
      Window.WindowControllerCallback localWindowControllerCallback = mWindow.getWindowControllerCallback();
      if ((localWindowControllerCallback != null) && (localWindowControllerCallback.isTaskRoot())) {
        super.setOutlineProvider(PIP_OUTLINE_PROVIDER);
      }
    }
    else if (getOutlineProvider() != mLastOutlineProvider)
    {
      setOutlineProvider(mLastOutlineProvider);
    }
    mIsInPictureInPictureMode = paramBoolean;
  }
  
  public InputQueue.Callback willYouTakeTheInputQueue()
  {
    InputQueue.Callback localCallback;
    if (mFeatureId < 0) {
      localCallback = mWindow.mTakeInputQueueCallback;
    } else {
      localCallback = null;
    }
    return localCallback;
  }
  
  public SurfaceHolder.Callback2 willYouTakeTheSurface()
  {
    SurfaceHolder.Callback2 localCallback2;
    if (mFeatureId < 0) {
      localCallback2 = mWindow.mTakeSurfaceCallback;
    } else {
      localCallback2 = null;
    }
    return localCallback2;
  }
  
  private class ActionModeCallback2Wrapper
    extends ActionMode.Callback2
  {
    private final ActionMode.Callback mWrapped;
    
    public ActionModeCallback2Wrapper(ActionMode.Callback paramCallback)
    {
      mWrapped = paramCallback;
    }
    
    public boolean onActionItemClicked(ActionMode paramActionMode, MenuItem paramMenuItem)
    {
      return mWrapped.onActionItemClicked(paramActionMode, paramMenuItem);
    }
    
    public boolean onCreateActionMode(ActionMode paramActionMode, Menu paramMenu)
    {
      return mWrapped.onCreateActionMode(paramActionMode, paramMenu);
    }
    
    public void onDestroyActionMode(ActionMode paramActionMode)
    {
      mWrapped.onDestroyActionMode(paramActionMode);
      int i = mContext.getApplicationInfo().targetSdkVersion;
      int j = 0;
      int k = 0;
      if (i >= 23) {
        i = 1;
      } else {
        i = 0;
      }
      Object localObject;
      int m;
      if (i != 0)
      {
        if (paramActionMode == mPrimaryActionMode) {
          i = 1;
        } else {
          i = 0;
        }
        if (paramActionMode == mFloatingActionMode) {
          k = 1;
        }
        String str;
        if ((i == 0) && (paramActionMode.getType() == 0))
        {
          str = mLogTag;
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Destroying unexpected ActionMode instance of TYPE_PRIMARY; ");
          ((StringBuilder)localObject).append(paramActionMode);
          ((StringBuilder)localObject).append(" was not the current primary action mode! Expected ");
          ((StringBuilder)localObject).append(mPrimaryActionMode);
          Log.e(str, ((StringBuilder)localObject).toString());
        }
        j = k;
        m = i;
        if (k == 0)
        {
          j = k;
          m = i;
          if (paramActionMode.getType() == 1)
          {
            str = mLogTag;
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append("Destroying unexpected ActionMode instance of TYPE_FLOATING; ");
            ((StringBuilder)localObject).append(paramActionMode);
            ((StringBuilder)localObject).append(" was not the current floating action mode! Expected ");
            ((StringBuilder)localObject).append(mFloatingActionMode);
            Log.e(str, ((StringBuilder)localObject).toString());
            j = k;
            m = i;
          }
        }
      }
      else
      {
        if (paramActionMode.getType() == 0) {
          i = 1;
        } else {
          i = 0;
        }
        m = i;
        if (paramActionMode.getType() == 1)
        {
          j = 1;
          m = i;
        }
      }
      if (m != 0)
      {
        if (mPrimaryActionModePopup != null) {
          removeCallbacks(mShowPrimaryActionModePopup);
        }
        if (mPrimaryActionModeView != null)
        {
          DecorView.this.endOnGoingFadeAnimation();
          localObject = mPrimaryActionModeView;
          DecorView.access$402(DecorView.this, ObjectAnimator.ofFloat(mPrimaryActionModeView, View.ALPHA, new float[] { 1.0F, 0.0F }));
          mFadeAnim.addListener(new Animator.AnimatorListener()
          {
            public void onAnimationCancel(Animator paramAnonymousAnimator) {}
            
            public void onAnimationEnd(Animator paramAnonymousAnimator)
            {
              if (val$lastActionModeView == mPrimaryActionModeView)
              {
                val$lastActionModeView.setVisibility(8);
                if (mPrimaryActionModePopup != null) {
                  mPrimaryActionModePopup.dismiss();
                }
                val$lastActionModeView.killMode();
                DecorView.access$402(DecorView.this, null);
              }
            }
            
            public void onAnimationRepeat(Animator paramAnonymousAnimator) {}
            
            public void onAnimationStart(Animator paramAnonymousAnimator) {}
          });
          mFadeAnim.start();
        }
        mPrimaryActionMode = null;
      }
      else if (j != 0)
      {
        DecorView.this.cleanupFloatingActionModeViews();
        DecorView.access$602(DecorView.this, null);
      }
      if ((mWindow.getCallback() != null) && (!mWindow.isDestroyed())) {
        try
        {
          mWindow.getCallback().onActionModeFinished(paramActionMode);
        }
        catch (AbstractMethodError paramActionMode) {}
      }
      requestFitSystemWindows();
    }
    
    public void onGetContentRect(ActionMode paramActionMode, View paramView, Rect paramRect)
    {
      if ((mWrapped instanceof ActionMode.Callback2)) {
        ((ActionMode.Callback2)mWrapped).onGetContentRect(paramActionMode, paramView, paramRect);
      } else {
        super.onGetContentRect(paramActionMode, paramView, paramRect);
      }
    }
    
    public boolean onPrepareActionMode(ActionMode paramActionMode, Menu paramMenu)
    {
      requestFitSystemWindows();
      return mWrapped.onPrepareActionMode(paramActionMode, paramMenu);
    }
  }
  
  public static class ColorViewAttributes
  {
    final int hideWindowFlag;
    final int horizontalGravity;
    final int id;
    final int seascapeGravity;
    final int systemUiHideFlag;
    final String transitionName;
    final int translucentFlag;
    final int verticalGravity;
    
    private ColorViewAttributes(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, String paramString, int paramInt6, int paramInt7)
    {
      id = paramInt6;
      systemUiHideFlag = paramInt1;
      translucentFlag = paramInt2;
      verticalGravity = paramInt3;
      horizontalGravity = paramInt4;
      seascapeGravity = paramInt5;
      transitionName = paramString;
      hideWindowFlag = paramInt7;
    }
    
    public boolean isPresent(int paramInt1, int paramInt2, boolean paramBoolean)
    {
      if (((systemUiHideFlag & paramInt1) == 0) && ((hideWindowFlag & paramInt2) == 0) && (((0x80000000 & paramInt2) != 0) || (paramBoolean))) {
        paramBoolean = true;
      } else {
        paramBoolean = false;
      }
      return paramBoolean;
    }
    
    public boolean isVisible(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
    {
      return isVisible(isPresent(paramInt1, paramInt3, paramBoolean), paramInt2, paramInt3, paramBoolean);
    }
    
    public boolean isVisible(boolean paramBoolean1, int paramInt1, int paramInt2, boolean paramBoolean2)
    {
      if ((paramBoolean1) && ((0xFF000000 & paramInt1) != 0) && (((translucentFlag & paramInt2) == 0) || (paramBoolean2))) {
        paramBoolean1 = true;
      } else {
        paramBoolean1 = false;
      }
      return paramBoolean1;
    }
  }
  
  private static class ColorViewState
  {
    final DecorView.ColorViewAttributes attributes;
    int color;
    boolean present = false;
    int targetVisibility = 4;
    View view = null;
    boolean visible;
    
    ColorViewState(DecorView.ColorViewAttributes paramColorViewAttributes)
    {
      attributes = paramColorViewAttributes;
    }
  }
}
