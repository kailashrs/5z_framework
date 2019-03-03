package com.android.internal.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnComputeInternalInsetsListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public final class FloatingToolbar
{
  public static final String FLOATING_TOOLBAR_TAG = "floating_toolbar";
  private static final MenuItem.OnMenuItemClickListener NO_OP_MENUITEM_CLICK_LISTENER = _..Lambda.FloatingToolbar.7_enOzxeypZYfdFYr1HzBLfj47k.INSTANCE;
  private final Rect mContentRect = new Rect();
  private final Context mContext;
  private Menu mMenu;
  private MenuItem.OnMenuItemClickListener mMenuItemClickListener = NO_OP_MENUITEM_CLICK_LISTENER;
  private final Comparator<MenuItem> mMenuItemComparator = _..Lambda.FloatingToolbar.LutnsyBKrZiroTBekgIjhIyrl40.INSTANCE;
  private final View.OnLayoutChangeListener mOrientationChangeHandler = new View.OnLayoutChangeListener()
  {
    private final Rect mNewRect = new Rect();
    private final Rect mOldRect = new Rect();
    
    public void onLayoutChange(View paramAnonymousView, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, int paramAnonymousInt4, int paramAnonymousInt5, int paramAnonymousInt6, int paramAnonymousInt7, int paramAnonymousInt8)
    {
      mNewRect.set(paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3, paramAnonymousInt4);
      mOldRect.set(paramAnonymousInt5, paramAnonymousInt6, paramAnonymousInt7, paramAnonymousInt8);
      if ((mPopup.isShowing()) && (!mNewRect.equals(mOldRect)))
      {
        FloatingToolbar.access$102(FloatingToolbar.this, true);
        updateLayout();
      }
    }
  };
  private final FloatingToolbarPopup mPopup;
  private final Rect mPreviousContentRect = new Rect();
  private List<MenuItem> mShowingMenuItems = new ArrayList();
  private int mSuggestedWidth;
  private boolean mWidthChanged = true;
  private final Window mWindow;
  
  public FloatingToolbar(Window paramWindow)
  {
    mContext = applyDefaultTheme(paramWindow.getContext());
    mWindow = ((Window)Preconditions.checkNotNull(paramWindow));
    mPopup = new FloatingToolbarPopup(mContext, paramWindow.getDecorView());
  }
  
  private static Context applyDefaultTheme(Context paramContext)
  {
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(new int[] { 17891418 });
    int i;
    if (localTypedArray.getBoolean(0, true)) {
      i = 16974123;
    } else {
      i = 16974120;
    }
    localTypedArray.recycle();
    return new ContextThemeWrapper(paramContext, i);
  }
  
  private static ViewGroup createContentContainer(Context paramContext)
  {
    paramContext = (ViewGroup)LayoutInflater.from(paramContext).inflate(17367170, null);
    paramContext.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
    paramContext.setTag("floating_toolbar");
    paramContext.setClipToOutline(true);
    return paramContext;
  }
  
  private static AnimatorSet createEnterAnimation(View paramView)
  {
    AnimatorSet localAnimatorSet = new AnimatorSet();
    localAnimatorSet.playTogether(new Animator[] { ObjectAnimator.ofFloat(paramView, View.ALPHA, new float[] { 0.0F, 1.0F }).setDuration(150L) });
    return localAnimatorSet;
  }
  
  private static AnimatorSet createExitAnimation(View paramView, int paramInt, Animator.AnimatorListener paramAnimatorListener)
  {
    AnimatorSet localAnimatorSet = new AnimatorSet();
    localAnimatorSet.playTogether(new Animator[] { ObjectAnimator.ofFloat(paramView, View.ALPHA, new float[] { 1.0F, 0.0F }).setDuration(100L) });
    localAnimatorSet.setStartDelay(paramInt);
    localAnimatorSet.addListener(paramAnimatorListener);
    return localAnimatorSet;
  }
  
  private static View createMenuItemButton(Context paramContext, MenuItem paramMenuItem, int paramInt, boolean paramBoolean)
  {
    paramContext = LayoutInflater.from(paramContext).inflate(17367171, null);
    if (paramMenuItem != null) {
      updateMenuItemButton(paramContext, paramMenuItem, paramInt, paramBoolean);
    }
    return paramContext;
  }
  
  private static PopupWindow createPopupWindow(ViewGroup paramViewGroup)
  {
    LinearLayout localLinearLayout = new LinearLayout(paramViewGroup.getContext());
    PopupWindow localPopupWindow = new PopupWindow(localLinearLayout);
    localPopupWindow.setClippingEnabled(false);
    localPopupWindow.setWindowLayoutType(1005);
    localPopupWindow.setAnimationStyle(0);
    localPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
    paramViewGroup.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
    localLinearLayout.addView(paramViewGroup);
    return localPopupWindow;
  }
  
  private void doShow()
  {
    List localList = getVisibleAndEnabledMenuItems(mMenu);
    localList.sort(mMenuItemComparator);
    if ((!isCurrentlyShowing(localList)) || (mWidthChanged))
    {
      mPopup.dismiss();
      mPopup.layoutMenuItems(localList, mMenuItemClickListener, mSuggestedWidth);
      mShowingMenuItems = localList;
    }
    if (!mPopup.isShowing()) {
      mPopup.show(mContentRect);
    } else if (!mPreviousContentRect.equals(mContentRect)) {
      mPopup.updateCoordinates(mContentRect);
    }
    mWidthChanged = false;
    mPreviousContentRect.set(mContentRect);
  }
  
  private List<MenuItem> getVisibleAndEnabledMenuItems(Menu paramMenu)
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; (paramMenu != null) && (i < paramMenu.size()); i++)
    {
      MenuItem localMenuItem = paramMenu.getItem(i);
      if ((localMenuItem.isVisible()) && (localMenuItem.isEnabled()))
      {
        SubMenu localSubMenu = localMenuItem.getSubMenu();
        if (localSubMenu != null) {
          localArrayList.addAll(getVisibleAndEnabledMenuItems(localSubMenu));
        } else {
          localArrayList.add(localMenuItem);
        }
      }
    }
    return localArrayList;
  }
  
  private boolean isCurrentlyShowing(List<MenuItem> paramList)
  {
    if ((mShowingMenuItems != null) && (paramList.size() == mShowingMenuItems.size()))
    {
      int i = paramList.size();
      int j = 0;
      while (j < i)
      {
        MenuItem localMenuItem1 = (MenuItem)paramList.get(j);
        MenuItem localMenuItem2 = (MenuItem)mShowingMenuItems.get(j);
        if ((localMenuItem1.getItemId() == localMenuItem2.getItemId()) && (TextUtils.equals(localMenuItem1.getTitle(), localMenuItem2.getTitle())) && (Objects.equals(localMenuItem1.getIcon(), localMenuItem2.getIcon())) && (localMenuItem1.getGroupId() == localMenuItem2.getGroupId())) {
          j++;
        } else {
          return false;
        }
      }
      return true;
    }
    return false;
  }
  
  private void registerOrientationHandler()
  {
    unregisterOrientationHandler();
    mWindow.getDecorView().addOnLayoutChangeListener(mOrientationChangeHandler);
  }
  
  private void unregisterOrientationHandler()
  {
    mWindow.getDecorView().removeOnLayoutChangeListener(mOrientationChangeHandler);
  }
  
  private static void updateMenuItemButton(View paramView, MenuItem paramMenuItem, int paramInt, boolean paramBoolean)
  {
    TextView localTextView = (TextView)paramView.findViewById(16908976);
    localTextView.setEllipsize(null);
    if (TextUtils.isEmpty(paramMenuItem.getTitle()))
    {
      localTextView.setVisibility(8);
    }
    else
    {
      localTextView.setVisibility(0);
      localTextView.setText(paramMenuItem.getTitle());
    }
    Object localObject = (ImageView)paramView.findViewById(16908974);
    if ((paramMenuItem.getIcon() != null) && (paramBoolean))
    {
      ((ImageView)localObject).setVisibility(0);
      ((ImageView)localObject).setImageDrawable(paramMenuItem.getIcon());
      if (localTextView != null) {
        localTextView.setPaddingRelative(paramInt, 0, 0, 0);
      }
    }
    else
    {
      ((ImageView)localObject).setVisibility(8);
      if (localTextView != null) {
        localTextView.setPaddingRelative(0, 0, 0, 0);
      }
    }
    localObject = paramMenuItem.getContentDescription();
    if (TextUtils.isEmpty((CharSequence)localObject)) {
      paramView.setContentDescription(paramMenuItem.getTitle());
    } else {
      paramView.setContentDescription((CharSequence)localObject);
    }
  }
  
  public void dismiss()
  {
    unregisterOrientationHandler();
    mPopup.dismiss();
  }
  
  public void hide()
  {
    mPopup.hide();
  }
  
  public boolean isHidden()
  {
    return mPopup.isHidden();
  }
  
  public boolean isShowing()
  {
    return mPopup.isShowing();
  }
  
  public FloatingToolbar setContentRect(Rect paramRect)
  {
    mContentRect.set((Rect)Preconditions.checkNotNull(paramRect));
    return this;
  }
  
  public FloatingToolbar setMenu(Menu paramMenu)
  {
    mMenu = ((Menu)Preconditions.checkNotNull(paramMenu));
    return this;
  }
  
  public FloatingToolbar setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener paramOnMenuItemClickListener)
  {
    if (paramOnMenuItemClickListener != null) {
      mMenuItemClickListener = paramOnMenuItemClickListener;
    } else {
      mMenuItemClickListener = NO_OP_MENUITEM_CLICK_LISTENER;
    }
    return this;
  }
  
  public void setOutsideTouchable(boolean paramBoolean, PopupWindow.OnDismissListener paramOnDismissListener)
  {
    if ((mPopup.setOutsideTouchable(paramBoolean, paramOnDismissListener)) && (isShowing()))
    {
      dismiss();
      doShow();
    }
  }
  
  public FloatingToolbar setSuggestedWidth(int paramInt)
  {
    boolean bool;
    if (Math.abs(paramInt - mSuggestedWidth) > mSuggestedWidth * 0.2D) {
      bool = true;
    } else {
      bool = false;
    }
    mWidthChanged = bool;
    mSuggestedWidth = paramInt;
    return this;
  }
  
  public FloatingToolbar show()
  {
    registerOrientationHandler();
    doShow();
    return this;
  }
  
  public FloatingToolbar updateLayout()
  {
    if (mPopup.isShowing()) {
      doShow();
    }
    return this;
  }
  
  private static final class FloatingToolbarPopup
  {
    private static final int MAX_OVERFLOW_SIZE = 4;
    private static final int MIN_OVERFLOW_SIZE = 2;
    private final Drawable mArrow;
    private final AnimationSet mCloseOverflowAnimation;
    private final ViewGroup mContentContainer;
    private final Context mContext;
    private final Point mCoordsOnWindow = new Point();
    private final AnimatorSet mDismissAnimation;
    private boolean mDismissed = true;
    private final Interpolator mFastOutLinearInInterpolator;
    private final Interpolator mFastOutSlowInInterpolator;
    private boolean mHidden;
    private final AnimatorSet mHideAnimation;
    private final int mIconTextSpacing;
    private final ViewTreeObserver.OnComputeInternalInsetsListener mInsetsComputer = new _..Lambda.FloatingToolbar.FloatingToolbarPopup.77YZy6kisO5OnjlgtKp0Zi1V8EY(this);
    private boolean mIsOverflowOpen;
    private final int mLineHeight;
    private final Interpolator mLinearOutSlowInInterpolator;
    private final Interpolator mLogAccelerateInterpolator;
    private final ViewGroup mMainPanel;
    private Size mMainPanelSize;
    private final int mMarginHorizontal;
    private final int mMarginVertical;
    private final View.OnClickListener mMenuItemButtonOnClickListener = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        if (((paramAnonymousView.getTag() instanceof MenuItem)) && (mOnMenuItemClickListener != null)) {
          mOnMenuItemClickListener.onMenuItemClick((MenuItem)paramAnonymousView.getTag());
        }
      }
    };
    private MenuItem.OnMenuItemClickListener mOnMenuItemClickListener;
    private final AnimationSet mOpenOverflowAnimation;
    private boolean mOpenOverflowUpwards;
    private final Drawable mOverflow;
    private final Animation.AnimationListener mOverflowAnimationListener;
    private final ImageButton mOverflowButton;
    private final Size mOverflowButtonSize;
    private final OverflowPanel mOverflowPanel;
    private Size mOverflowPanelSize;
    private final OverflowPanelViewHelper mOverflowPanelViewHelper;
    private final View mParent;
    private final PopupWindow mPopupWindow;
    private final Runnable mPreparePopupContentRTLHelper = new Runnable()
    {
      public void run()
      {
        FloatingToolbar.FloatingToolbarPopup.this.setPanelsStatesAtRestingPosition();
        FloatingToolbar.FloatingToolbarPopup.this.setContentAreaAsTouchableSurface();
        mContentContainer.setAlpha(1.0F);
      }
    };
    private final AnimatorSet mShowAnimation;
    private final int[] mTmpCoords = new int[2];
    private final AnimatedVectorDrawable mToArrow;
    private final AnimatedVectorDrawable mToOverflow;
    private final Region mTouchableRegion = new Region();
    private int mTransitionDurationScale;
    private final Rect mViewPortOnScreen = new Rect();
    
    public FloatingToolbarPopup(Context paramContext, View paramView)
    {
      mParent = ((View)Preconditions.checkNotNull(paramView));
      mContext = ((Context)Preconditions.checkNotNull(paramContext));
      mContentContainer = FloatingToolbar.createContentContainer(paramContext);
      mPopupWindow = FloatingToolbar.createPopupWindow(mContentContainer);
      mMarginHorizontal = paramView.getResources().getDimensionPixelSize(17105162);
      mMarginVertical = paramView.getResources().getDimensionPixelSize(17105175);
      mLineHeight = paramContext.getResources().getDimensionPixelSize(17105161);
      mIconTextSpacing = paramContext.getResources().getDimensionPixelSize(17105163);
      mLogAccelerateInterpolator = new LogAccelerateInterpolator(null);
      mFastOutSlowInInterpolator = AnimationUtils.loadInterpolator(mContext, 17563661);
      mLinearOutSlowInInterpolator = AnimationUtils.loadInterpolator(mContext, 17563662);
      mFastOutLinearInInterpolator = AnimationUtils.loadInterpolator(mContext, 17563663);
      mArrow = mContext.getResources().getDrawable(17302504, mContext.getTheme());
      mArrow.setAutoMirrored(true);
      mOverflow = mContext.getResources().getDrawable(17302502, mContext.getTheme());
      mOverflow.setAutoMirrored(true);
      mToArrow = ((AnimatedVectorDrawable)mContext.getResources().getDrawable(17302503, mContext.getTheme()));
      mToArrow.setAutoMirrored(true);
      mToOverflow = ((AnimatedVectorDrawable)mContext.getResources().getDrawable(17302505, mContext.getTheme()));
      mToOverflow.setAutoMirrored(true);
      mOverflowButton = createOverflowButton();
      mOverflowButtonSize = measure(mOverflowButton);
      mMainPanel = createMainPanel();
      mOverflowPanelViewHelper = new OverflowPanelViewHelper(mContext, mIconTextSpacing);
      mOverflowPanel = createOverflowPanel();
      mOverflowAnimationListener = createOverflowAnimationListener();
      mOpenOverflowAnimation = new AnimationSet(true);
      mOpenOverflowAnimation.setAnimationListener(mOverflowAnimationListener);
      mCloseOverflowAnimation = new AnimationSet(true);
      mCloseOverflowAnimation.setAnimationListener(mOverflowAnimationListener);
      mShowAnimation = FloatingToolbar.createEnterAnimation(mContentContainer);
      mDismissAnimation = FloatingToolbar.createExitAnimation(mContentContainer, 150, new AnimatorListenerAdapter()
      {
        public void onAnimationEnd(Animator paramAnonymousAnimator)
        {
          mPopupWindow.dismiss();
          mContentContainer.removeAllViews();
        }
      });
      mHideAnimation = FloatingToolbar.createExitAnimation(mContentContainer, 0, new AnimatorListenerAdapter()
      {
        public void onAnimationEnd(Animator paramAnonymousAnimator)
        {
          mPopupWindow.dismiss();
        }
      });
    }
    
    private int calculateOverflowHeight(int paramInt)
    {
      int i = Math.min(4, Math.min(Math.max(2, paramInt), mOverflowPanel.getCount()));
      paramInt = 0;
      if (i < mOverflowPanel.getCount()) {
        paramInt = (int)(mLineHeight * 0.5F);
      }
      return mLineHeight * i + mOverflowButtonSize.getHeight() + paramInt;
    }
    
    private void cancelDismissAndHideAnimations()
    {
      mDismissAnimation.cancel();
      mHideAnimation.cancel();
    }
    
    private void cancelOverflowAnimations()
    {
      mContentContainer.clearAnimation();
      mMainPanel.animate().cancel();
      mOverflowPanel.animate().cancel();
      mToArrow.stop();
      mToOverflow.stop();
    }
    
    private void clearPanels()
    {
      mOverflowPanelSize = null;
      mMainPanelSize = null;
      mIsOverflowOpen = false;
      mMainPanel.removeAllViews();
      ArrayAdapter localArrayAdapter = (ArrayAdapter)mOverflowPanel.getAdapter();
      localArrayAdapter.clear();
      mOverflowPanel.setAdapter(localArrayAdapter);
      mContentContainer.removeAllViews();
    }
    
    private void closeOverflow()
    {
      final int i = mMainPanelSize.getWidth();
      final int j = mContentContainer.getWidth();
      final float f1 = mContentContainer.getX();
      Animation local8 = new Animation()
      {
        protected void applyTransformation(float paramAnonymousFloat, Transformation paramAnonymousTransformation)
        {
          int i = (int)((i - j) * paramAnonymousFloat);
          FloatingToolbar.FloatingToolbarPopup.setWidth(mContentContainer, j + i);
          if (FloatingToolbar.FloatingToolbarPopup.this.isInRTLMode())
          {
            mContentContainer.setX(f1);
            mMainPanel.setX(0.0F);
            mOverflowPanel.setX(0.0F);
          }
          else
          {
            mContentContainer.setX(val$right - mContentContainer.getWidth());
            mMainPanel.setX(mContentContainer.getWidth() - i);
            mOverflowPanel.setX(mContentContainer.getWidth() - j);
          }
        }
      };
      Animation local9 = new Animation()
      {
        protected void applyTransformation(float paramAnonymousFloat, Transformation paramAnonymousTransformation)
        {
          int i = (int)((val$targetHeight - val$startHeight) * paramAnonymousFloat);
          FloatingToolbar.FloatingToolbarPopup.setHeight(mContentContainer, val$startHeight + i);
          if (mOpenOverflowUpwards)
          {
            mContentContainer.setY(val$bottom - mContentContainer.getHeight());
            FloatingToolbar.FloatingToolbarPopup.this.positionContentYCoordinatesIfOpeningOverflowUpwards();
          }
        }
      };
      final float f2 = mOverflowButton.getX();
      if (isInRTLMode()) {
        f1 = f2 - j + mOverflowButton.getWidth();
      } else {
        f1 = j + f2 - mOverflowButton.getWidth();
      }
      Animation local10 = new Animation()
      {
        protected void applyTransformation(float paramAnonymousFloat, Transformation paramAnonymousTransformation)
        {
          float f1 = f2;
          float f2 = f1;
          float f3 = f2;
          float f4;
          if (FloatingToolbar.FloatingToolbarPopup.this.isInRTLMode()) {
            f4 = 0.0F;
          } else {
            f4 = mContentContainer.getWidth() - j;
          }
          mOverflowButton.setX(f1 + (f2 - f3) * paramAnonymousFloat + f4);
        }
      };
      local8.setInterpolator(mFastOutSlowInInterpolator);
      local8.setDuration(getAdjustedDuration(250));
      local9.setInterpolator(mLogAccelerateInterpolator);
      local9.setDuration(getAdjustedDuration(250));
      local10.setInterpolator(mFastOutSlowInInterpolator);
      local10.setDuration(getAdjustedDuration(250));
      mCloseOverflowAnimation.getAnimations().clear();
      mCloseOverflowAnimation.addAnimation(local8);
      mCloseOverflowAnimation.addAnimation(local9);
      mCloseOverflowAnimation.addAnimation(local10);
      mContentContainer.startAnimation(mCloseOverflowAnimation);
      mIsOverflowOpen = false;
      mMainPanel.animate().alpha(1.0F).withLayer().setInterpolator(mFastOutLinearInInterpolator).setDuration(100L).start();
      mOverflowPanel.animate().alpha(0.0F).withLayer().setInterpolator(mLinearOutSlowInInterpolator).setDuration(150L).start();
    }
    
    private ViewGroup createMainPanel()
    {
      new LinearLayout(mContext)
      {
        public boolean onInterceptTouchEvent(MotionEvent paramAnonymousMotionEvent)
        {
          return FloatingToolbar.FloatingToolbarPopup.this.isOverflowAnimating();
        }
        
        protected void onMeasure(int paramAnonymousInt1, int paramAnonymousInt2)
        {
          if (FloatingToolbar.FloatingToolbarPopup.this.isOverflowAnimating()) {
            paramAnonymousInt1 = View.MeasureSpec.makeMeasureSpec(mMainPanelSize.getWidth(), 1073741824);
          }
          super.onMeasure(paramAnonymousInt1, paramAnonymousInt2);
        }
      };
    }
    
    private Animation.AnimationListener createOverflowAnimationListener()
    {
      new Animation.AnimationListener()
      {
        public void onAnimationEnd(Animation paramAnonymousAnimation)
        {
          mContentContainer.post(new _..Lambda.FloatingToolbar.FloatingToolbarPopup.13.7WTSUuAWkzil48e0QxuKTn0YOXI(this));
        }
        
        public void onAnimationRepeat(Animation paramAnonymousAnimation) {}
        
        public void onAnimationStart(Animation paramAnonymousAnimation)
        {
          mOverflowButton.setEnabled(false);
          mMainPanel.setVisibility(0);
          mOverflowPanel.setVisibility(0);
        }
      };
    }
    
    private ImageButton createOverflowButton()
    {
      ImageButton localImageButton = (ImageButton)LayoutInflater.from(mContext).inflate(17367173, null);
      localImageButton.setImageDrawable(mOverflow);
      localImageButton.setOnClickListener(new _..Lambda.FloatingToolbar.FloatingToolbarPopup._uEfRwR__1oHxMvRVdmbNRdukDM(this, localImageButton));
      return localImageButton;
    }
    
    private OverflowPanel createOverflowPanel()
    {
      OverflowPanel localOverflowPanel = new OverflowPanel(this);
      localOverflowPanel.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
      localOverflowPanel.setDivider(null);
      localOverflowPanel.setDividerHeight(0);
      localOverflowPanel.setAdapter(new ArrayAdapter(mContext, 0)
      {
        public View getView(int paramAnonymousInt, View paramAnonymousView, ViewGroup paramAnonymousViewGroup)
        {
          return mOverflowPanelViewHelper.getView((MenuItem)getItem(paramAnonymousInt), mOverflowPanelSize.getWidth(), paramAnonymousView);
        }
      });
      localOverflowPanel.setOnItemClickListener(new _..Lambda.FloatingToolbar.FloatingToolbarPopup.E8FwnPCl7gZpcTlX_UaRPIBRnT0(this, localOverflowPanel));
      return localOverflowPanel;
    }
    
    private int getAdjustedDuration(int paramInt)
    {
      if (mTransitionDurationScale < 150) {
        return Math.max(paramInt - 50, 0);
      }
      if (mTransitionDurationScale > 300) {
        return paramInt + 50;
      }
      return (int)(paramInt * ValueAnimator.getDurationScale());
    }
    
    private int getAdjustedToolbarWidth(int paramInt)
    {
      refreshViewPort();
      int i = mViewPortOnScreen.width();
      int j = mParent.getResources().getDimensionPixelSize(17105162);
      int k = paramInt;
      if (paramInt <= 0) {
        k = mParent.getResources().getDimensionPixelSize(17105173);
      }
      return Math.min(k, i - 2 * j);
    }
    
    private int getOverflowWidth()
    {
      int i = 0;
      int j = mOverflowPanel.getAdapter().getCount();
      for (int k = 0; k < j; k++)
      {
        MenuItem localMenuItem = (MenuItem)mOverflowPanel.getAdapter().getItem(k);
        i = Math.max(mOverflowPanelViewHelper.calculateWidth(localMenuItem), i);
      }
      return i;
    }
    
    private boolean hasOverflow()
    {
      boolean bool;
      if (mOverflowPanelSize != null) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    private boolean isInRTLMode()
    {
      boolean bool1 = mContext.getApplicationInfo().hasRtlSupport();
      boolean bool2 = true;
      if ((!bool1) || (mContext.getResources().getConfiguration().getLayoutDirection() != 1)) {
        bool2 = false;
      }
      return bool2;
    }
    
    private boolean isOverflowAnimating()
    {
      boolean bool1 = mOpenOverflowAnimation.hasStarted();
      boolean bool2 = false;
      int i;
      if ((bool1) && (!mOpenOverflowAnimation.hasEnded())) {
        i = 1;
      } else {
        i = 0;
      }
      int j;
      if ((mCloseOverflowAnimation.hasStarted()) && (!mCloseOverflowAnimation.hasEnded())) {
        j = 1;
      } else {
        j = 0;
      }
      if ((i == 0) && (j == 0)) {
        return bool2;
      }
      bool2 = true;
      return bool2;
    }
    
    private void layoutOverflowPanelItems(List<MenuItem> paramList)
    {
      ArrayAdapter localArrayAdapter = (ArrayAdapter)mOverflowPanel.getAdapter();
      localArrayAdapter.clear();
      int i = paramList.size();
      for (int j = 0; j < i; j++) {
        localArrayAdapter.add((MenuItem)paramList.get(j));
      }
      mOverflowPanel.setAdapter(localArrayAdapter);
      if (mOpenOverflowUpwards) {
        mOverflowPanel.setY(0.0F);
      } else {
        mOverflowPanel.setY(mOverflowButtonSize.getHeight());
      }
      mOverflowPanelSize = new Size(Math.max(getOverflowWidth(), mOverflowButtonSize.getWidth()), calculateOverflowHeight(4));
      setSize(mOverflowPanel, mOverflowPanelSize);
    }
    
    private void maybeComputeTransitionDurationScale()
    {
      if ((mMainPanelSize != null) && (mOverflowPanelSize != null))
      {
        int i = mMainPanelSize.getWidth() - mOverflowPanelSize.getWidth();
        int j = mOverflowPanelSize.getHeight() - mMainPanelSize.getHeight();
        mTransitionDurationScale = ((int)(Math.sqrt(i * i + j * j) / mContentContainer.getContext().getResources().getDisplayMetrics().density));
      }
    }
    
    private static Size measure(View paramView)
    {
      boolean bool;
      if (paramView.getParent() == null) {
        bool = true;
      } else {
        bool = false;
      }
      Preconditions.checkState(bool);
      paramView.measure(0, 0);
      return new Size(paramView.getMeasuredWidth(), paramView.getMeasuredHeight());
    }
    
    private void openOverflow()
    {
      final int i = mOverflowPanelSize.getWidth();
      final int j = mOverflowPanelSize.getHeight();
      final int k = mContentContainer.getWidth();
      final int m = mContentContainer.getHeight();
      final float f1 = mContentContainer.getY();
      final float f2 = mContentContainer.getX();
      Animation local5 = new Animation()
      {
        protected void applyTransformation(float paramAnonymousFloat, Transformation paramAnonymousTransformation)
        {
          int i = (int)((i - k) * paramAnonymousFloat);
          FloatingToolbar.FloatingToolbarPopup.setWidth(mContentContainer, k + i);
          if (FloatingToolbar.FloatingToolbarPopup.this.isInRTLMode())
          {
            mContentContainer.setX(f2);
            mMainPanel.setX(0.0F);
            mOverflowPanel.setX(0.0F);
          }
          else
          {
            mContentContainer.setX(val$right - mContentContainer.getWidth());
            mMainPanel.setX(mContentContainer.getWidth() - k);
            mOverflowPanel.setX(mContentContainer.getWidth() - i);
          }
        }
      };
      Animation local6 = new Animation()
      {
        protected void applyTransformation(float paramAnonymousFloat, Transformation paramAnonymousTransformation)
        {
          int i = (int)((j - m) * paramAnonymousFloat);
          FloatingToolbar.FloatingToolbarPopup.setHeight(mContentContainer, m + i);
          if (mOpenOverflowUpwards)
          {
            mContentContainer.setY(f1 - (mContentContainer.getHeight() - m));
            FloatingToolbar.FloatingToolbarPopup.this.positionContentYCoordinatesIfOpeningOverflowUpwards();
          }
        }
      };
      f2 = mOverflowButton.getX();
      if (isInRTLMode()) {
        f1 = i + f2 - mOverflowButton.getWidth();
      } else {
        f1 = f2 - i + mOverflowButton.getWidth();
      }
      Animation local7 = new Animation()
      {
        protected void applyTransformation(float paramAnonymousFloat, Transformation paramAnonymousTransformation)
        {
          float f1 = f2;
          float f2 = f1;
          float f3 = f2;
          float f4;
          if (FloatingToolbar.FloatingToolbarPopup.this.isInRTLMode()) {
            f4 = 0.0F;
          } else {
            f4 = mContentContainer.getWidth() - k;
          }
          mOverflowButton.setX(f1 + (f2 - f3) * paramAnonymousFloat + f4);
        }
      };
      local5.setInterpolator(mLogAccelerateInterpolator);
      local5.setDuration(getAdjustedDuration(250));
      local6.setInterpolator(mFastOutSlowInInterpolator);
      local6.setDuration(getAdjustedDuration(250));
      local7.setInterpolator(mFastOutSlowInInterpolator);
      local7.setDuration(getAdjustedDuration(250));
      mOpenOverflowAnimation.getAnimations().clear();
      mOpenOverflowAnimation.getAnimations().clear();
      mOpenOverflowAnimation.addAnimation(local5);
      mOpenOverflowAnimation.addAnimation(local6);
      mOpenOverflowAnimation.addAnimation(local7);
      mContentContainer.startAnimation(mOpenOverflowAnimation);
      mIsOverflowOpen = true;
      mMainPanel.animate().alpha(0.0F).withLayer().setInterpolator(mLinearOutSlowInInterpolator).setDuration(250L).start();
      mOverflowPanel.setAlpha(1.0F);
    }
    
    private void positionContentYCoordinatesIfOpeningOverflowUpwards()
    {
      if (mOpenOverflowUpwards)
      {
        mMainPanel.setY(mContentContainer.getHeight() - mMainPanelSize.getHeight());
        mOverflowButton.setY(mContentContainer.getHeight() - mOverflowButton.getHeight());
        mOverflowPanel.setY(mContentContainer.getHeight() - mOverflowPanelSize.getHeight());
      }
    }
    
    private void preparePopupContent()
    {
      mContentContainer.removeAllViews();
      if (hasOverflow()) {
        mContentContainer.addView(mOverflowPanel);
      }
      mContentContainer.addView(mMainPanel);
      if (hasOverflow()) {
        mContentContainer.addView(mOverflowButton);
      }
      setPanelsStatesAtRestingPosition();
      setContentAreaAsTouchableSurface();
      if (isInRTLMode())
      {
        mContentContainer.setAlpha(0.0F);
        mContentContainer.post(mPreparePopupContentRTLHelper);
      }
    }
    
    private void refreshCoordinatesAndOverflowDirection(Rect paramRect)
    {
      refreshViewPort();
      int i = Math.min(paramRect.centerX() - mPopupWindow.getWidth() / 2, mViewPortOnScreen.right - mPopupWindow.getWidth());
      int j = top - mViewPortOnScreen.top;
      int k = mViewPortOnScreen.bottom - bottom;
      int m = mMarginVertical * 2;
      int n = mLineHeight + m;
      if (!hasOverflow())
      {
        if (j >= n) {
          j = top - n;
        }
        for (;;)
        {
          break;
          if (k >= n) {
            j = bottom;
          } else if (k >= mLineHeight) {
            j = bottom - mMarginVertical;
          } else {
            j = Math.max(mViewPortOnScreen.top, top - n);
          }
        }
      }
      int i1 = calculateOverflowHeight(2) + m;
      int i2 = mViewPortOnScreen.bottom - top + n;
      int i3 = bottom;
      int i4 = mViewPortOnScreen.top;
      if (j >= i1)
      {
        updateOverflowHeight(j - m);
        j = top - mPopupWindow.getHeight();
        mOpenOverflowUpwards = true;
      }
      else if ((j >= n) && (i2 >= i1))
      {
        updateOverflowHeight(i2 - m);
        j = top - n;
        mOpenOverflowUpwards = false;
      }
      else if (k >= i1)
      {
        updateOverflowHeight(k - m);
        j = bottom;
        mOpenOverflowUpwards = false;
      }
      else if ((k >= n) && (mViewPortOnScreen.height() >= i1))
      {
        updateOverflowHeight(i3 - i4 + n - m);
        j = bottom + n - mPopupWindow.getHeight();
        mOpenOverflowUpwards = true;
      }
      else
      {
        updateOverflowHeight(mViewPortOnScreen.height() - m);
        j = mViewPortOnScreen.top;
        mOpenOverflowUpwards = false;
      }
      mParent.getRootView().getLocationOnScreen(mTmpCoords);
      m = mTmpCoords[0];
      i4 = mTmpCoords[1];
      mParent.getRootView().getLocationInWindow(mTmpCoords);
      i3 = mTmpCoords[0];
      n = mTmpCoords[1];
      mCoordsOnWindow.set(Math.max(0, i - (m - i3)), Math.max(0, j - (i4 - n)));
    }
    
    private void refreshViewPort()
    {
      mParent.getWindowVisibleDisplayFrame(mViewPortOnScreen);
    }
    
    private void runDismissAnimation()
    {
      mDismissAnimation.start();
    }
    
    private void runHideAnimation()
    {
      mHideAnimation.start();
    }
    
    private void runShowAnimation()
    {
      mShowAnimation.start();
    }
    
    private void setButtonTagAndClickListener(View paramView, MenuItem paramMenuItem)
    {
      paramView.setTag(paramMenuItem);
      paramView.setOnClickListener(mMenuItemButtonOnClickListener);
    }
    
    private void setContentAreaAsTouchableSurface()
    {
      Preconditions.checkNotNull(mMainPanelSize);
      int i;
      int j;
      if (mIsOverflowOpen)
      {
        Preconditions.checkNotNull(mOverflowPanelSize);
        i = mOverflowPanelSize.getWidth();
        j = mOverflowPanelSize.getHeight();
      }
      else
      {
        i = mMainPanelSize.getWidth();
        j = mMainPanelSize.getHeight();
      }
      mTouchableRegion.set((int)mContentContainer.getX(), (int)mContentContainer.getY(), (int)mContentContainer.getX() + i, (int)mContentContainer.getY() + j);
    }
    
    private static void setHeight(View paramView, int paramInt)
    {
      setSize(paramView, getLayoutParamswidth, paramInt);
    }
    
    private void setPanelsStatesAtRestingPosition()
    {
      mOverflowButton.setEnabled(true);
      mOverflowPanel.awakenScrollBars();
      Size localSize;
      if (mIsOverflowOpen)
      {
        localSize = mOverflowPanelSize;
        setSize(mContentContainer, localSize);
        mMainPanel.setAlpha(0.0F);
        mMainPanel.setVisibility(4);
        mOverflowPanel.setAlpha(1.0F);
        mOverflowPanel.setVisibility(0);
        mOverflowButton.setImageDrawable(mArrow);
        mOverflowButton.setContentDescription(mContext.getString(17040016));
        if (isInRTLMode())
        {
          mContentContainer.setX(mMarginHorizontal);
          mMainPanel.setX(0.0F);
          mOverflowButton.setX(localSize.getWidth() - mOverflowButtonSize.getWidth());
          mOverflowPanel.setX(0.0F);
        }
        else
        {
          mContentContainer.setX(mPopupWindow.getWidth() - localSize.getWidth() - mMarginHorizontal);
          mMainPanel.setX(-mContentContainer.getX());
          mOverflowButton.setX(0.0F);
          mOverflowPanel.setX(0.0F);
        }
        if (mOpenOverflowUpwards)
        {
          mContentContainer.setY(mMarginVertical);
          mMainPanel.setY(localSize.getHeight() - mContentContainer.getHeight());
          mOverflowButton.setY(localSize.getHeight() - mOverflowButtonSize.getHeight());
          mOverflowPanel.setY(0.0F);
        }
        else
        {
          mContentContainer.setY(mMarginVertical);
          mMainPanel.setY(0.0F);
          mOverflowButton.setY(0.0F);
          mOverflowPanel.setY(mOverflowButtonSize.getHeight());
        }
      }
      else
      {
        localSize = mMainPanelSize;
        setSize(mContentContainer, localSize);
        mMainPanel.setAlpha(1.0F);
        mMainPanel.setVisibility(0);
        mOverflowPanel.setAlpha(0.0F);
        mOverflowPanel.setVisibility(4);
        mOverflowButton.setImageDrawable(mOverflow);
        mOverflowButton.setContentDescription(mContext.getString(17040017));
        if (hasOverflow())
        {
          if (isInRTLMode())
          {
            mContentContainer.setX(mMarginHorizontal);
            mMainPanel.setX(0.0F);
            mOverflowButton.setX(0.0F);
            mOverflowPanel.setX(0.0F);
          }
          else
          {
            mContentContainer.setX(mPopupWindow.getWidth() - localSize.getWidth() - mMarginHorizontal);
            mMainPanel.setX(0.0F);
            mOverflowButton.setX(localSize.getWidth() - mOverflowButtonSize.getWidth());
            mOverflowPanel.setX(localSize.getWidth() - mOverflowPanelSize.getWidth());
          }
          if (mOpenOverflowUpwards)
          {
            mContentContainer.setY(mMarginVertical + mOverflowPanelSize.getHeight() - localSize.getHeight());
            mMainPanel.setY(0.0F);
            mOverflowButton.setY(0.0F);
            mOverflowPanel.setY(localSize.getHeight() - mOverflowPanelSize.getHeight());
          }
          else
          {
            mContentContainer.setY(mMarginVertical);
            mMainPanel.setY(0.0F);
            mOverflowButton.setY(0.0F);
            mOverflowPanel.setY(mOverflowButtonSize.getHeight());
          }
        }
        else
        {
          mContentContainer.setX(mMarginHorizontal);
          mContentContainer.setY(mMarginVertical);
          mMainPanel.setX(0.0F);
          mMainPanel.setY(0.0F);
        }
      }
    }
    
    private static void setSize(View paramView, int paramInt1, int paramInt2)
    {
      paramView.setMinimumWidth(paramInt1);
      paramView.setMinimumHeight(paramInt2);
      ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
      if (localLayoutParams == null) {
        localLayoutParams = new ViewGroup.LayoutParams(0, 0);
      }
      width = paramInt1;
      height = paramInt2;
      paramView.setLayoutParams(localLayoutParams);
    }
    
    private static void setSize(View paramView, Size paramSize)
    {
      setSize(paramView, paramSize.getWidth(), paramSize.getHeight());
    }
    
    private void setTouchableSurfaceInsetsComputer()
    {
      ViewTreeObserver localViewTreeObserver = mPopupWindow.getContentView().getRootView().getViewTreeObserver();
      localViewTreeObserver.removeOnComputeInternalInsetsListener(mInsetsComputer);
      localViewTreeObserver.addOnComputeInternalInsetsListener(mInsetsComputer);
    }
    
    private static void setWidth(View paramView, int paramInt)
    {
      setSize(paramView, paramInt, getLayoutParamsheight);
    }
    
    private void setZeroTouchableSurface()
    {
      mTouchableRegion.setEmpty();
    }
    
    private void updateOverflowHeight(int paramInt)
    {
      if (hasOverflow())
      {
        paramInt = calculateOverflowHeight((paramInt - mOverflowButtonSize.getHeight()) / mLineHeight);
        if (mOverflowPanelSize.getHeight() != paramInt) {
          mOverflowPanelSize = new Size(mOverflowPanelSize.getWidth(), paramInt);
        }
        setSize(mOverflowPanel, mOverflowPanelSize);
        if (mIsOverflowOpen)
        {
          setSize(mContentContainer, mOverflowPanelSize);
          if (mOpenOverflowUpwards)
          {
            paramInt = mOverflowPanelSize.getHeight() - paramInt;
            mContentContainer.setY(mContentContainer.getY() + paramInt);
            mOverflowButton.setY(mOverflowButton.getY() - paramInt);
          }
        }
        else
        {
          setSize(mContentContainer, mMainPanelSize);
        }
        updatePopupSize();
      }
    }
    
    private void updatePopupSize()
    {
      int i = 0;
      int j = 0;
      if (mMainPanelSize != null)
      {
        i = Math.max(0, mMainPanelSize.getWidth());
        j = Math.max(0, mMainPanelSize.getHeight());
      }
      int k = i;
      int m = j;
      if (mOverflowPanelSize != null)
      {
        k = Math.max(i, mOverflowPanelSize.getWidth());
        m = Math.max(j, mOverflowPanelSize.getHeight());
      }
      mPopupWindow.setWidth(mMarginHorizontal * 2 + k);
      mPopupWindow.setHeight(mMarginVertical * 2 + m);
      maybeComputeTransitionDurationScale();
    }
    
    public void dismiss()
    {
      if (mDismissed) {
        return;
      }
      mHidden = false;
      mDismissed = true;
      mHideAnimation.cancel();
      runDismissAnimation();
      setZeroTouchableSurface();
    }
    
    public void hide()
    {
      if (!isShowing()) {
        return;
      }
      mHidden = true;
      runHideAnimation();
      setZeroTouchableSurface();
    }
    
    public boolean isHidden()
    {
      return mHidden;
    }
    
    public boolean isShowing()
    {
      boolean bool;
      if ((!mDismissed) && (!mHidden)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public List<MenuItem> layoutMainPanelItems(List<MenuItem> paramList, int paramInt)
    {
      Preconditions.checkNotNull(paramList);
      LinkedList localLinkedList = new LinkedList();
      Object localObject1 = new LinkedList();
      paramList = paramList.iterator();
      Object localObject2;
      while (paramList.hasNext())
      {
        localObject2 = (MenuItem)paramList.next();
        if ((((MenuItem)localObject2).getItemId() != 16908353) && (((MenuItem)localObject2).requiresOverflow())) {
          ((LinkedList)localObject1).add(localObject2);
        } else {
          localLinkedList.add(localObject2);
        }
      }
      localLinkedList.addAll((Collection)localObject1);
      mMainPanel.removeAllViews();
      mMainPanel.setPaddingRelative(0, 0, 0, 0);
      int i = -1;
      int j = paramInt;
      for (int k = 1; !localLinkedList.isEmpty(); k = 0)
      {
        paramList = (MenuItem)localLinkedList.peek();
        if ((k == 0) && (paramList.requiresOverflow())) {
          break;
        }
        boolean bool;
        if ((k != 0) && (paramList.getItemId() == 16908353)) {
          bool = true;
        } else {
          bool = false;
        }
        localObject2 = FloatingToolbar.createMenuItemButton(mContext, paramList, mIconTextSpacing, bool);
        if ((!bool) && ((localObject2 instanceof LinearLayout))) {
          ((LinearLayout)localObject2).setGravity(17);
        }
        if (k != 0) {
          ((View)localObject2).setPaddingRelative((int)(((View)localObject2).getPaddingStart() * 1.5D), ((View)localObject2).getPaddingTop(), ((View)localObject2).getPaddingEnd(), ((View)localObject2).getPaddingBottom());
        }
        if (localLinkedList.size() == 1) {
          i = 1;
        } else {
          i = 0;
        }
        if (i != 0) {
          ((View)localObject2).setPaddingRelative(((View)localObject2).getPaddingStart(), ((View)localObject2).getPaddingTop(), (int)(1.5D * ((View)localObject2).getPaddingEnd()), ((View)localObject2).getPaddingBottom());
        }
        ((View)localObject2).measure(0, 0);
        int m = Math.min(((View)localObject2).getMeasuredWidth(), paramInt);
        if (m <= j - mOverflowButtonSize.getWidth()) {
          k = 1;
        } else {
          k = 0;
        }
        if ((i != 0) && (m <= j)) {
          i = 1;
        } else {
          i = 0;
        }
        if ((k == 0) && (i == 0)) {
          break;
        }
        setButtonTagAndClickListener((View)localObject2, paramList);
        ((View)localObject2).setTooltipText(paramList.getTooltipText());
        mMainPanel.addView((View)localObject2);
        localObject1 = ((View)localObject2).getLayoutParams();
        width = m;
        ((View)localObject2).setLayoutParams((ViewGroup.LayoutParams)localObject1);
        j -= m;
        localLinkedList.pop();
        i = paramList.getGroupId();
      }
      if (!localLinkedList.isEmpty()) {
        mMainPanel.setPaddingRelative(0, 0, mOverflowButtonSize.getWidth(), 0);
      }
      mMainPanelSize = measure(mMainPanel);
      return localLinkedList;
    }
    
    public void layoutMenuItems(List<MenuItem> paramList, MenuItem.OnMenuItemClickListener paramOnMenuItemClickListener, int paramInt)
    {
      mOnMenuItemClickListener = paramOnMenuItemClickListener;
      cancelOverflowAnimations();
      clearPanels();
      paramList = layoutMainPanelItems(paramList, getAdjustedToolbarWidth(paramInt));
      if (!paramList.isEmpty()) {
        layoutOverflowPanelItems(paramList);
      }
      updatePopupSize();
    }
    
    public boolean setOutsideTouchable(boolean paramBoolean, PopupWindow.OnDismissListener paramOnDismissListener)
    {
      boolean bool = false;
      if ((mPopupWindow.isOutsideTouchable() ^ paramBoolean))
      {
        mPopupWindow.setOutsideTouchable(paramBoolean);
        mPopupWindow.setFocusable(paramBoolean ^ true);
        bool = true;
      }
      mPopupWindow.setOnDismissListener(paramOnDismissListener);
      return bool;
    }
    
    public void show(Rect paramRect)
    {
      Preconditions.checkNotNull(paramRect);
      if (isShowing()) {
        return;
      }
      mHidden = false;
      mDismissed = false;
      cancelDismissAndHideAnimations();
      cancelOverflowAnimations();
      refreshCoordinatesAndOverflowDirection(paramRect);
      preparePopupContent();
      mPopupWindow.showAtLocation(mParent, 0, mCoordsOnWindow.x, mCoordsOnWindow.y);
      setTouchableSurfaceInsetsComputer();
      runShowAnimation();
    }
    
    public void updateCoordinates(Rect paramRect)
    {
      Preconditions.checkNotNull(paramRect);
      if ((isShowing()) && (mPopupWindow.isShowing()))
      {
        cancelOverflowAnimations();
        refreshCoordinatesAndOverflowDirection(paramRect);
        preparePopupContent();
        mPopupWindow.update(mCoordsOnWindow.x, mCoordsOnWindow.y, mPopupWindow.getWidth(), mPopupWindow.getHeight());
        return;
      }
    }
    
    private static final class LogAccelerateInterpolator
      implements Interpolator
    {
      private static final int BASE = 100;
      private static final float LOGS_SCALE = 1.0F / computeLog(1.0F, 100);
      
      private LogAccelerateInterpolator() {}
      
      private static float computeLog(float paramFloat, int paramInt)
      {
        return (float)(1.0D - Math.pow(paramInt, -paramFloat));
      }
      
      public float getInterpolation(float paramFloat)
      {
        return 1.0F - computeLog(1.0F - paramFloat, 100) * LOGS_SCALE;
      }
    }
    
    private static final class OverflowPanel
      extends ListView
    {
      private final FloatingToolbar.FloatingToolbarPopup mPopup;
      
      OverflowPanel(FloatingToolbar.FloatingToolbarPopup paramFloatingToolbarPopup)
      {
        super();
        mPopup = paramFloatingToolbarPopup;
        setScrollBarDefaultDelayBeforeFade(ViewConfiguration.getScrollDefaultDelay() * 3);
        setScrollIndicators(3);
      }
      
      protected boolean awakenScrollBars()
      {
        return super.awakenScrollBars();
      }
      
      public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
      {
        if (mPopup.isOverflowAnimating()) {
          return true;
        }
        return super.dispatchTouchEvent(paramMotionEvent);
      }
      
      protected void onMeasure(int paramInt1, int paramInt2)
      {
        super.onMeasure(paramInt1, View.MeasureSpec.makeMeasureSpec(mPopup.mOverflowPanelSize.getHeight() - mPopup.mOverflowButtonSize.getHeight(), 1073741824));
      }
    }
    
    private static final class OverflowPanelViewHelper
    {
      private final View mCalculator;
      private final Context mContext;
      private final int mIconTextSpacing;
      private final int mSidePadding;
      
      public OverflowPanelViewHelper(Context paramContext, int paramInt)
      {
        mContext = ((Context)Preconditions.checkNotNull(paramContext));
        mIconTextSpacing = paramInt;
        mSidePadding = paramContext.getResources().getDimensionPixelSize(17105172);
        mCalculator = createMenuButton(null);
      }
      
      private View createMenuButton(MenuItem paramMenuItem)
      {
        paramMenuItem = FloatingToolbar.createMenuItemButton(mContext, paramMenuItem, mIconTextSpacing, shouldShowIcon(paramMenuItem));
        paramMenuItem.setPadding(mSidePadding, 0, mSidePadding, 0);
        return paramMenuItem;
      }
      
      private boolean shouldShowIcon(MenuItem paramMenuItem)
      {
        boolean bool = false;
        if (paramMenuItem != null)
        {
          if (paramMenuItem.getGroupId() == 16908353) {
            bool = true;
          }
          return bool;
        }
        return false;
      }
      
      public int calculateWidth(MenuItem paramMenuItem)
      {
        FloatingToolbar.updateMenuItemButton(mCalculator, paramMenuItem, mIconTextSpacing, shouldShowIcon(paramMenuItem));
        mCalculator.measure(0, 0);
        return mCalculator.getMeasuredWidth();
      }
      
      public View getView(MenuItem paramMenuItem, int paramInt, View paramView)
      {
        Preconditions.checkNotNull(paramMenuItem);
        if (paramView != null) {
          FloatingToolbar.updateMenuItemButton(paramView, paramMenuItem, mIconTextSpacing, shouldShowIcon(paramMenuItem));
        } else {
          paramView = createMenuButton(paramMenuItem);
        }
        paramView.setMinimumWidth(paramInt);
        return paramView;
      }
    }
  }
}
