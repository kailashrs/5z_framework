package com.android.internal.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.IntProperty;
import android.util.Log;
import android.util.Property;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewPropertyAnimator;
import android.view.Window.Callback;
import android.view.WindowInsets;
import android.widget.OverScroller;
import android.widget.Toolbar;
import com.android.internal.view.menu.MenuPresenter.Callback;

public class ActionBarOverlayLayout
  extends ViewGroup
  implements DecorContentParent
{
  public static final Property<ActionBarOverlayLayout, Integer> ACTION_BAR_HIDE_OFFSET = new IntProperty("actionBarHideOffset")
  {
    public Integer get(ActionBarOverlayLayout paramAnonymousActionBarOverlayLayout)
    {
      return Integer.valueOf(paramAnonymousActionBarOverlayLayout.getActionBarHideOffset());
    }
    
    public void setValue(ActionBarOverlayLayout paramAnonymousActionBarOverlayLayout, int paramAnonymousInt)
    {
      paramAnonymousActionBarOverlayLayout.setActionBarHideOffset(paramAnonymousInt);
    }
  };
  static final int[] ATTRS = { 16843499, 16842841 };
  private static final String TAG = "ActionBarOverlayLayout";
  private final int ACTION_BAR_ANIMATE_DELAY = 600;
  private ActionBarContainer mActionBarBottom;
  private int mActionBarHeight;
  private ActionBarContainer mActionBarTop;
  private ActionBarVisibilityCallback mActionBarVisibilityCallback;
  private final Runnable mAddActionBarHideOffset = new Runnable()
  {
    public void run()
    {
      ActionBarOverlayLayout.this.haltActionBarHideOffsetAnimations();
      ActionBarOverlayLayout.access$002(ActionBarOverlayLayout.this, mActionBarTop.animate().translationY(-mActionBarTop.getHeight()).setListener(mTopAnimatorListener));
      if ((mActionBarBottom != null) && (mActionBarBottom.getVisibility() != 8)) {
        ActionBarOverlayLayout.access$202(ActionBarOverlayLayout.this, mActionBarBottom.animate().translationY(mActionBarBottom.getHeight()).setListener(mBottomAnimatorListener));
      }
    }
  };
  private boolean mAnimatingForFling;
  private final Rect mBaseContentInsets = new Rect();
  private WindowInsets mBaseInnerInsets = WindowInsets.CONSUMED;
  private final Animator.AnimatorListener mBottomAnimatorListener = new AnimatorListenerAdapter()
  {
    public void onAnimationCancel(Animator paramAnonymousAnimator)
    {
      ActionBarOverlayLayout.access$202(ActionBarOverlayLayout.this, null);
      ActionBarOverlayLayout.access$102(ActionBarOverlayLayout.this, false);
    }
    
    public void onAnimationEnd(Animator paramAnonymousAnimator)
    {
      ActionBarOverlayLayout.access$202(ActionBarOverlayLayout.this, null);
      ActionBarOverlayLayout.access$102(ActionBarOverlayLayout.this, false);
    }
  };
  private View mContent;
  private final Rect mContentInsets = new Rect();
  private ViewPropertyAnimator mCurrentActionBarBottomAnimator;
  private ViewPropertyAnimator mCurrentActionBarTopAnimator;
  private DecorToolbar mDecorToolbar;
  private OverScroller mFlingEstimator;
  private boolean mHasNonEmbeddedTabs;
  private boolean mHideOnContentScroll;
  private int mHideOnContentScrollReference;
  private boolean mIgnoreWindowContentOverlay;
  private WindowInsets mInnerInsets = WindowInsets.CONSUMED;
  private final Rect mLastBaseContentInsets = new Rect();
  private WindowInsets mLastBaseInnerInsets = WindowInsets.CONSUMED;
  private WindowInsets mLastInnerInsets = WindowInsets.CONSUMED;
  private int mLastSystemUiVisibility;
  private boolean mOverlayMode;
  private final Runnable mRemoveActionBarHideOffset = new Runnable()
  {
    public void run()
    {
      ActionBarOverlayLayout.this.haltActionBarHideOffsetAnimations();
      ActionBarOverlayLayout.access$002(ActionBarOverlayLayout.this, mActionBarTop.animate().translationY(0.0F).setListener(mTopAnimatorListener));
      if ((mActionBarBottom != null) && (mActionBarBottom.getVisibility() != 8)) {
        ActionBarOverlayLayout.access$202(ActionBarOverlayLayout.this, mActionBarBottom.animate().translationY(0.0F).setListener(mBottomAnimatorListener));
      }
    }
  };
  private final Animator.AnimatorListener mTopAnimatorListener = new AnimatorListenerAdapter()
  {
    public void onAnimationCancel(Animator paramAnonymousAnimator)
    {
      ActionBarOverlayLayout.access$002(ActionBarOverlayLayout.this, null);
      ActionBarOverlayLayout.access$102(ActionBarOverlayLayout.this, false);
    }
    
    public void onAnimationEnd(Animator paramAnonymousAnimator)
    {
      ActionBarOverlayLayout.access$002(ActionBarOverlayLayout.this, null);
      ActionBarOverlayLayout.access$102(ActionBarOverlayLayout.this, false);
    }
  };
  private Drawable mWindowContentOverlay;
  private int mWindowVisibility = 0;
  
  public ActionBarOverlayLayout(Context paramContext)
  {
    super(paramContext);
    init(paramContext);
  }
  
  public ActionBarOverlayLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init(paramContext);
  }
  
  private void addActionBarHideOffset()
  {
    haltActionBarHideOffsetAnimations();
    mAddActionBarHideOffset.run();
  }
  
  private boolean applyInsets(View paramView, Rect paramRect, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
  {
    boolean bool1 = false;
    paramView = (LayoutParams)paramView.getLayoutParams();
    boolean bool2 = bool1;
    if (paramBoolean1)
    {
      bool2 = bool1;
      if (leftMargin != left)
      {
        bool2 = true;
        leftMargin = left;
      }
    }
    paramBoolean1 = bool2;
    if (paramBoolean2)
    {
      paramBoolean1 = bool2;
      if (topMargin != top)
      {
        paramBoolean1 = true;
        topMargin = top;
      }
    }
    paramBoolean2 = paramBoolean1;
    if (paramBoolean4)
    {
      paramBoolean2 = paramBoolean1;
      if (rightMargin != right)
      {
        paramBoolean2 = true;
        rightMargin = right;
      }
    }
    paramBoolean1 = paramBoolean2;
    if (paramBoolean3)
    {
      paramBoolean1 = paramBoolean2;
      if (bottomMargin != bottom)
      {
        paramBoolean1 = true;
        bottomMargin = bottom;
      }
    }
    return paramBoolean1;
  }
  
  private DecorToolbar getDecorToolbar(View paramView)
  {
    if ((paramView instanceof DecorToolbar)) {
      return (DecorToolbar)paramView;
    }
    if ((paramView instanceof Toolbar)) {
      return ((Toolbar)paramView).getWrapper();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Can't make a decor toolbar out of ");
    localStringBuilder.append(paramView.getClass().getSimpleName());
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  private void haltActionBarHideOffsetAnimations()
  {
    removeCallbacks(mRemoveActionBarHideOffset);
    removeCallbacks(mAddActionBarHideOffset);
    if (mCurrentActionBarTopAnimator != null) {
      mCurrentActionBarTopAnimator.cancel();
    }
    if (mCurrentActionBarBottomAnimator != null) {
      mCurrentActionBarBottomAnimator.cancel();
    }
  }
  
  private void init(Context paramContext)
  {
    TypedArray localTypedArray = getContext().getTheme().obtainStyledAttributes(ATTRS);
    boolean bool1 = false;
    mActionBarHeight = localTypedArray.getDimensionPixelSize(0, 0);
    mWindowContentOverlay = localTypedArray.getDrawable(1);
    if (mWindowContentOverlay == null) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    setWillNotDraw(bool2);
    localTypedArray.recycle();
    boolean bool2 = bool1;
    if (getApplicationInfotargetSdkVersion < 19) {
      bool2 = true;
    }
    mIgnoreWindowContentOverlay = bool2;
    mFlingEstimator = new OverScroller(paramContext);
  }
  
  private void postAddActionBarHideOffset()
  {
    haltActionBarHideOffsetAnimations();
    postDelayed(mAddActionBarHideOffset, 600L);
  }
  
  private void postRemoveActionBarHideOffset()
  {
    haltActionBarHideOffsetAnimations();
    postDelayed(mRemoveActionBarHideOffset, 600L);
  }
  
  private void removeActionBarHideOffset()
  {
    haltActionBarHideOffsetAnimations();
    mRemoveActionBarHideOffset.run();
  }
  
  private boolean shouldHideActionBarOnFling(float paramFloat1, float paramFloat2)
  {
    mFlingEstimator.fling(0, 0, 0, (int)paramFloat2, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
    boolean bool;
    if (mFlingEstimator.getFinalY() > mActionBarTop.getHeight()) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean canShowOverflowMenu()
  {
    pullChildren();
    return mDecorToolbar.canShowOverflowMenu();
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return paramLayoutParams instanceof LayoutParams;
  }
  
  public void dismissPopups()
  {
    pullChildren();
    mDecorToolbar.dismissPopupMenus();
  }
  
  public void draw(Canvas paramCanvas)
  {
    super.draw(paramCanvas);
    if ((mWindowContentOverlay != null) && (!mIgnoreWindowContentOverlay))
    {
      int i;
      if (mActionBarTop.getVisibility() == 0) {
        i = (int)(mActionBarTop.getBottom() + mActionBarTop.getTranslationY() + 0.5F);
      } else {
        i = 0;
      }
      mWindowContentOverlay.setBounds(0, i, getWidth(), mWindowContentOverlay.getIntrinsicHeight() + i);
      mWindowContentOverlay.draw(paramCanvas);
    }
  }
  
  protected LayoutParams generateDefaultLayoutParams()
  {
    return new LayoutParams(-1, -1);
  }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return new LayoutParams(paramLayoutParams);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  public int getActionBarHideOffset()
  {
    int i;
    if (mActionBarTop != null) {
      i = -(int)mActionBarTop.getTranslationY();
    } else {
      i = 0;
    }
    return i;
  }
  
  public CharSequence getTitle()
  {
    pullChildren();
    return mDecorToolbar.getTitle();
  }
  
  public boolean hasIcon()
  {
    pullChildren();
    return mDecorToolbar.hasIcon();
  }
  
  public boolean hasLogo()
  {
    pullChildren();
    return mDecorToolbar.hasLogo();
  }
  
  public boolean hideOverflowMenu()
  {
    pullChildren();
    return mDecorToolbar.hideOverflowMenu();
  }
  
  public void initFeature(int paramInt)
  {
    pullChildren();
    if (paramInt != 2)
    {
      if (paramInt != 5)
      {
        if (paramInt == 9) {
          setOverlayMode(true);
        }
      }
      else {
        mDecorToolbar.initIndeterminateProgress();
      }
    }
    else {
      mDecorToolbar.initProgress();
    }
  }
  
  public boolean isHideOnContentScrollEnabled()
  {
    return mHideOnContentScroll;
  }
  
  public boolean isInOverlayMode()
  {
    return mOverlayMode;
  }
  
  public boolean isOverflowMenuShowPending()
  {
    pullChildren();
    return mDecorToolbar.isOverflowMenuShowPending();
  }
  
  public boolean isOverflowMenuShowing()
  {
    pullChildren();
    return mDecorToolbar.isOverflowMenuShowing();
  }
  
  public WindowInsets onApplyWindowInsets(WindowInsets paramWindowInsets)
  {
    pullChildren();
    if ((getWindowSystemUiVisibility() & 0x100) == 0) {}
    Rect localRect = paramWindowInsets.getSystemWindowInsets();
    boolean bool1 = applyInsets(mActionBarTop, localRect, true, true, false, true);
    boolean bool2 = bool1;
    if (mActionBarBottom != null) {
      bool2 = bool1 | applyInsets(mActionBarBottom, localRect, true, false, true, true);
    }
    computeSystemWindowInsets(paramWindowInsets, mBaseContentInsets);
    mBaseInnerInsets = paramWindowInsets.inset(mBaseContentInsets);
    if (!mLastBaseInnerInsets.equals(mBaseInnerInsets))
    {
      bool2 = true;
      mLastBaseInnerInsets = mBaseInnerInsets;
    }
    if (!mLastBaseContentInsets.equals(mBaseContentInsets))
    {
      bool2 = true;
      mLastBaseContentInsets.set(mBaseContentInsets);
    }
    if (bool2) {
      requestLayout();
    }
    return WindowInsets.CONSUMED;
  }
  
  protected void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    init(getContext());
    requestApplyInsets();
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    haltActionBarHideOffsetAnimations();
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    ActionBarOverlayLayout localActionBarOverlayLayout = this;
    paramInt3 = localActionBarOverlayLayout.getChildCount();
    paramInt1 = localActionBarOverlayLayout.getPaddingLeft();
    localActionBarOverlayLayout.getPaddingRight();
    int i = localActionBarOverlayLayout.getPaddingTop();
    int j = localActionBarOverlayLayout.getPaddingBottom();
    for (int k = 0;; k++)
    {
      localActionBarOverlayLayout = this;
      if (k >= paramInt3) {
        break;
      }
      View localView = localActionBarOverlayLayout.getChildAt(k);
      if (localView.getVisibility() != 8)
      {
        LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
        int m = localView.getMeasuredWidth();
        int n = localView.getMeasuredHeight();
        int i1 = leftMargin + paramInt1;
        int i2;
        if (localView == mActionBarBottom) {
          i2 = paramInt4 - paramInt2 - j - n - bottomMargin;
        } else {
          i2 = i + topMargin;
        }
        localView.layout(i1, i2, i1 + m, i2 + n);
      }
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    pullChildren();
    int i = 0;
    int j = 0;
    measureChildWithMargins(mActionBarTop, paramInt1, 0, paramInt2, 0);
    Object localObject = (LayoutParams)mActionBarTop.getLayoutParams();
    int k = Math.max(0, mActionBarTop.getMeasuredWidth() + leftMargin + rightMargin);
    int m = Math.max(0, mActionBarTop.getMeasuredHeight() + topMargin + bottomMargin);
    int n = combineMeasuredStates(0, mActionBarTop.getMeasuredState());
    int i1 = m;
    int i2 = k;
    int i3 = n;
    if (mActionBarBottom != null)
    {
      measureChildWithMargins(mActionBarBottom, paramInt1, 0, paramInt2, 0);
      localObject = (LayoutParams)mActionBarBottom.getLayoutParams();
      i2 = Math.max(k, mActionBarBottom.getMeasuredWidth() + leftMargin + rightMargin);
      i1 = Math.max(m, mActionBarBottom.getMeasuredHeight() + topMargin + bottomMargin);
      i3 = combineMeasuredStates(n, mActionBarBottom.getMeasuredState());
    }
    if ((getWindowSystemUiVisibility() & 0x100) != 0) {
      m = 1;
    } else {
      m = 0;
    }
    if (m != 0)
    {
      n = mActionBarHeight;
      i = n;
      if (mHasNonEmbeddedTabs)
      {
        i = n;
        if (mActionBarTop.getTabContainer() != null) {
          i = n + mActionBarHeight;
        }
      }
    }
    else if (mActionBarTop.getVisibility() != 8)
    {
      i = mActionBarTop.getMeasuredHeight();
    }
    n = j;
    if (mDecorToolbar.isSplit())
    {
      n = j;
      if (mActionBarBottom != null) {
        if (m != 0) {
          n = mActionBarHeight;
        } else {
          n = mActionBarBottom.getMeasuredHeight();
        }
      }
    }
    mContentInsets.set(mBaseContentInsets);
    mInnerInsets = mBaseInnerInsets;
    if ((!mOverlayMode) && (m == 0))
    {
      localObject = mContentInsets;
      top += i;
      localObject = mContentInsets;
      bottom += n;
      mInnerInsets = mInnerInsets.inset(0, i, 0, n);
    }
    else
    {
      mInnerInsets = mInnerInsets.replaceSystemWindowInsets(mInnerInsets.getSystemWindowInsetLeft(), mInnerInsets.getSystemWindowInsetTop() + i, mInnerInsets.getSystemWindowInsetRight(), mInnerInsets.getSystemWindowInsetBottom() + n);
    }
    applyInsets(mContent, mContentInsets, true, true, true, true);
    if (!mLastInnerInsets.equals(mInnerInsets))
    {
      mLastInnerInsets = mInnerInsets;
      mContent.dispatchApplyWindowInsets(mInnerInsets);
    }
    measureChildWithMargins(mContent, paramInt1, 0, paramInt2, 0);
    localObject = (LayoutParams)mContent.getLayoutParams();
    i = Math.max(i2, mContent.getMeasuredWidth() + leftMargin + rightMargin);
    i2 = Math.max(i1, mContent.getMeasuredHeight() + topMargin + bottomMargin);
    i1 = combineMeasuredStates(i3, mContent.getMeasuredState());
    n = getPaddingLeft();
    i3 = getPaddingRight();
    i2 = Math.max(i2 + (getPaddingTop() + getPaddingBottom()), getSuggestedMinimumHeight());
    setMeasuredDimension(resolveSizeAndState(Math.max(i + (n + i3), getSuggestedMinimumWidth()), paramInt1, i1), resolveSizeAndState(i2, paramInt2, i1 << 16));
  }
  
  public boolean onNestedFling(View paramView, float paramFloat1, float paramFloat2, boolean paramBoolean)
  {
    if ((mHideOnContentScroll) && (paramBoolean))
    {
      if (shouldHideActionBarOnFling(paramFloat1, paramFloat2)) {
        addActionBarHideOffset();
      } else {
        removeActionBarHideOffset();
      }
      mAnimatingForFling = true;
      return true;
    }
    return false;
  }
  
  public void onNestedScroll(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mHideOnContentScrollReference += paramInt2;
    setActionBarHideOffset(mHideOnContentScrollReference);
  }
  
  public void onNestedScrollAccepted(View paramView1, View paramView2, int paramInt)
  {
    super.onNestedScrollAccepted(paramView1, paramView2, paramInt);
    mHideOnContentScrollReference = getActionBarHideOffset();
    haltActionBarHideOffsetAnimations();
    if (mActionBarVisibilityCallback != null) {
      mActionBarVisibilityCallback.onContentScrollStarted();
    }
  }
  
  public boolean onStartNestedScroll(View paramView1, View paramView2, int paramInt)
  {
    if (((paramInt & 0x2) != 0) && (mActionBarTop.getVisibility() == 0)) {
      return mHideOnContentScroll;
    }
    return false;
  }
  
  public void onStopNestedScroll(View paramView)
  {
    super.onStopNestedScroll(paramView);
    if ((mHideOnContentScroll) && (!mAnimatingForFling)) {
      if (mHideOnContentScrollReference <= mActionBarTop.getHeight()) {
        postRemoveActionBarHideOffset();
      } else {
        postAddActionBarHideOffset();
      }
    }
    if (mActionBarVisibilityCallback != null) {
      mActionBarVisibilityCallback.onContentScrollStopped();
    }
  }
  
  public void onWindowSystemUiVisibilityChanged(int paramInt)
  {
    super.onWindowSystemUiVisibilityChanged(paramInt);
    pullChildren();
    int i = mLastSystemUiVisibility;
    mLastSystemUiVisibility = paramInt;
    boolean bool = false;
    int j;
    if ((paramInt & 0x4) == 0) {
      j = 1;
    } else {
      j = 0;
    }
    int k;
    if ((paramInt & 0x100) != 0) {
      k = 1;
    } else {
      k = 0;
    }
    if (mActionBarVisibilityCallback != null)
    {
      ActionBarVisibilityCallback localActionBarVisibilityCallback = mActionBarVisibilityCallback;
      if (k == 0) {
        bool = true;
      }
      localActionBarVisibilityCallback.enableContentAnimations(bool);
      if ((j == 0) && (k != 0)) {
        mActionBarVisibilityCallback.hideForSystem();
      } else {
        mActionBarVisibilityCallback.showForSystem();
      }
    }
    if ((((i ^ paramInt) & 0x100) != 0) && (mActionBarVisibilityCallback != null)) {
      requestApplyInsets();
    }
  }
  
  protected void onWindowVisibilityChanged(int paramInt)
  {
    super.onWindowVisibilityChanged(paramInt);
    mWindowVisibility = paramInt;
    if (mActionBarVisibilityCallback != null) {
      mActionBarVisibilityCallback.onWindowVisibilityChanged(paramInt);
    }
  }
  
  void pullChildren()
  {
    if (mContent == null)
    {
      mContent = findViewById(16908290);
      mActionBarTop = ((ActionBarContainer)findViewById(16908698));
      mDecorToolbar = getDecorToolbar(findViewById(16908697));
      mActionBarBottom = ((ActionBarContainer)findViewById(16909396));
    }
  }
  
  public void restoreToolbarHierarchyState(SparseArray<Parcelable> paramSparseArray)
  {
    pullChildren();
    mDecorToolbar.restoreHierarchyState(paramSparseArray);
  }
  
  public void saveToolbarHierarchyState(SparseArray<Parcelable> paramSparseArray)
  {
    pullChildren();
    mDecorToolbar.saveHierarchyState(paramSparseArray);
  }
  
  public void setActionBarHideOffset(int paramInt)
  {
    haltActionBarHideOffsetAnimations();
    int i = mActionBarTop.getHeight();
    paramInt = Math.max(0, Math.min(paramInt, i));
    mActionBarTop.setTranslationY(-paramInt);
    if ((mActionBarBottom != null) && (mActionBarBottom.getVisibility() != 8))
    {
      float f = paramInt / i;
      paramInt = (int)(mActionBarBottom.getHeight() * f);
      mActionBarBottom.setTranslationY(paramInt);
    }
  }
  
  public void setActionBarVisibilityCallback(ActionBarVisibilityCallback paramActionBarVisibilityCallback)
  {
    mActionBarVisibilityCallback = paramActionBarVisibilityCallback;
    if (getWindowToken() != null)
    {
      mActionBarVisibilityCallback.onWindowVisibilityChanged(mWindowVisibility);
      if (mLastSystemUiVisibility != 0)
      {
        onWindowSystemUiVisibilityChanged(mLastSystemUiVisibility);
        requestApplyInsets();
      }
    }
  }
  
  public void setHasNonEmbeddedTabs(boolean paramBoolean)
  {
    mHasNonEmbeddedTabs = paramBoolean;
  }
  
  public void setHideOnContentScrollEnabled(boolean paramBoolean)
  {
    if (paramBoolean != mHideOnContentScroll)
    {
      mHideOnContentScroll = paramBoolean;
      if (!paramBoolean)
      {
        stopNestedScroll();
        haltActionBarHideOffsetAnimations();
        setActionBarHideOffset(0);
      }
    }
  }
  
  public void setIcon(int paramInt)
  {
    pullChildren();
    mDecorToolbar.setIcon(paramInt);
  }
  
  public void setIcon(Drawable paramDrawable)
  {
    pullChildren();
    mDecorToolbar.setIcon(paramDrawable);
  }
  
  public void setLogo(int paramInt)
  {
    pullChildren();
    mDecorToolbar.setLogo(paramInt);
  }
  
  public void setMenu(Menu paramMenu, MenuPresenter.Callback paramCallback)
  {
    pullChildren();
    mDecorToolbar.setMenu(paramMenu, paramCallback);
  }
  
  public void setMenuPrepared()
  {
    pullChildren();
    mDecorToolbar.setMenuPrepared();
  }
  
  public void setOverlayMode(boolean paramBoolean)
  {
    mOverlayMode = paramBoolean;
    if ((paramBoolean) && (getContextgetApplicationInfotargetSdkVersion < 19)) {
      paramBoolean = true;
    } else {
      paramBoolean = false;
    }
    mIgnoreWindowContentOverlay = paramBoolean;
  }
  
  public void setShowingForActionMode(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      if ((getWindowSystemUiVisibility() & 0x500) == 1280) {
        setDisabledSystemUiVisibility(4);
      }
    }
    else {
      setDisabledSystemUiVisibility(0);
    }
  }
  
  public void setUiOptions(int paramInt)
  {
    boolean bool1 = false;
    boolean bool2;
    if ((paramInt & 0x1) != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    if (bool2) {
      bool1 = getContext().getResources().getBoolean(17957118);
    }
    if (bool1)
    {
      pullChildren();
      if ((mActionBarBottom != null) && (mDecorToolbar.canSplit()))
      {
        mDecorToolbar.setSplitView(mActionBarBottom);
        mDecorToolbar.setSplitToolbar(bool1);
        mDecorToolbar.setSplitWhenNarrow(bool2);
        ActionBarContextView localActionBarContextView = (ActionBarContextView)findViewById(16908702);
        localActionBarContextView.setSplitView(mActionBarBottom);
        localActionBarContextView.setSplitToolbar(bool1);
        localActionBarContextView.setSplitWhenNarrow(bool2);
      }
      else if (bool1)
      {
        Log.e("ActionBarOverlayLayout", "Requested split action bar with incompatible window decor! Ignoring request.");
      }
    }
  }
  
  public void setWindowCallback(Window.Callback paramCallback)
  {
    pullChildren();
    mDecorToolbar.setWindowCallback(paramCallback);
  }
  
  public void setWindowTitle(CharSequence paramCharSequence)
  {
    pullChildren();
    mDecorToolbar.setWindowTitle(paramCharSequence);
  }
  
  public boolean shouldDelayChildPressedState()
  {
    return false;
  }
  
  public boolean showOverflowMenu()
  {
    pullChildren();
    return mDecorToolbar.showOverflowMenu();
  }
  
  public static abstract interface ActionBarVisibilityCallback
  {
    public abstract void enableContentAnimations(boolean paramBoolean);
    
    public abstract void hideForSystem();
    
    public abstract void onContentScrollStarted();
    
    public abstract void onContentScrollStopped();
    
    public abstract void onWindowVisibilityChanged(int paramInt);
    
    public abstract void showForSystem();
  }
  
  public static class LayoutParams
    extends ViewGroup.MarginLayoutParams
  {
    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
    }
    
    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
    }
    
    public LayoutParams(ViewGroup.LayoutParams paramLayoutParams)
    {
      super();
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams paramMarginLayoutParams)
    {
      super();
    }
  }
}
