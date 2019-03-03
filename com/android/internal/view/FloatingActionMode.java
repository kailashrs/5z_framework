package com.android.internal.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.ActionMode;
import android.view.ActionMode.Callback2;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.PopupWindow.OnDismissListener;
import com.android.internal.util.Preconditions;
import com.android.internal.view.menu.MenuBuilder;
import com.android.internal.view.menu.MenuBuilder.Callback;
import com.android.internal.widget.FloatingToolbar;
import java.util.Arrays;

public final class FloatingActionMode
  extends ActionMode
{
  private static final int MAX_HIDE_DURATION = 3000;
  private static final int MOVING_HIDE_DELAY = 50;
  private final int mBottomAllowance;
  private final ActionMode.Callback2 mCallback;
  private final Rect mContentRect;
  private final Rect mContentRectOnScreen;
  private final Context mContext;
  private final Point mDisplaySize;
  private FloatingToolbar mFloatingToolbar;
  private FloatingToolbarVisibilityHelper mFloatingToolbarVisibilityHelper;
  private final Runnable mHideOff = new Runnable()
  {
    public void run()
    {
      if (FloatingActionMode.this.isViewStillActive())
      {
        mFloatingToolbarVisibilityHelper.setHideRequested(false);
        mFloatingToolbarVisibilityHelper.updateToolbarVisibility();
      }
    }
  };
  private final MenuBuilder mMenu;
  private final Runnable mMovingOff = new Runnable()
  {
    public void run()
    {
      if (FloatingActionMode.this.isViewStillActive())
      {
        mFloatingToolbarVisibilityHelper.setMoving(false);
        mFloatingToolbarVisibilityHelper.updateToolbarVisibility();
      }
    }
  };
  private final View mOriginatingView;
  private final Rect mPreviousContentRectOnScreen;
  private final int[] mPreviousViewPositionOnScreen;
  private final Rect mPreviousViewRectOnScreen;
  private final int[] mRootViewPositionOnScreen;
  private final Rect mScreenRect;
  private final int[] mViewPositionOnScreen;
  private final Rect mViewRectOnScreen;
  
  public FloatingActionMode(Context paramContext, ActionMode.Callback2 paramCallback2, View paramView, FloatingToolbar paramFloatingToolbar)
  {
    mContext = ((Context)Preconditions.checkNotNull(paramContext));
    mCallback = ((ActionMode.Callback2)Preconditions.checkNotNull(paramCallback2));
    mMenu = new MenuBuilder(paramContext).setDefaultShowAsAction(1);
    setType(1);
    mMenu.setCallback(new MenuBuilder.Callback()
    {
      public boolean onMenuItemSelected(MenuBuilder paramAnonymousMenuBuilder, MenuItem paramAnonymousMenuItem)
      {
        return mCallback.onActionItemClicked(FloatingActionMode.this, paramAnonymousMenuItem);
      }
      
      public void onMenuModeChange(MenuBuilder paramAnonymousMenuBuilder) {}
    });
    mContentRect = new Rect();
    mContentRectOnScreen = new Rect();
    mPreviousContentRectOnScreen = new Rect();
    mViewPositionOnScreen = new int[2];
    mPreviousViewPositionOnScreen = new int[2];
    mRootViewPositionOnScreen = new int[2];
    mViewRectOnScreen = new Rect();
    mPreviousViewRectOnScreen = new Rect();
    mScreenRect = new Rect();
    mOriginatingView = ((View)Preconditions.checkNotNull(paramView));
    mOriginatingView.getLocationOnScreen(mViewPositionOnScreen);
    mBottomAllowance = paramContext.getResources().getDimensionPixelSize(17105098);
    mDisplaySize = new Point();
    setFloatingToolbar((FloatingToolbar)Preconditions.checkNotNull(paramFloatingToolbar));
  }
  
  private static boolean intersectsClosed(Rect paramRect1, Rect paramRect2)
  {
    boolean bool;
    if ((left <= right) && (left <= right) && (top <= bottom) && (top <= bottom)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isContentRectWithinBounds()
  {
    ((WindowManager)mContext.getSystemService(WindowManager.class)).getDefaultDisplay().getRealSize(mDisplaySize);
    Rect localRect = mScreenRect;
    int i = mDisplaySize.x;
    int j = mDisplaySize.y;
    boolean bool1 = false;
    localRect.set(0, 0, i, j);
    boolean bool2 = bool1;
    if (intersectsClosed(mContentRectOnScreen, mScreenRect))
    {
      bool2 = bool1;
      if (intersectsClosed(mContentRectOnScreen, mViewRectOnScreen)) {
        bool2 = true;
      }
    }
    return bool2;
  }
  
  private boolean isViewStillActive()
  {
    boolean bool;
    if ((mOriginatingView.getWindowVisibility() == 0) && (mOriginatingView.isShown())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void repositionToolbar()
  {
    mContentRectOnScreen.set(mContentRect);
    ViewParent localViewParent = mOriginatingView.getParent();
    if ((localViewParent instanceof ViewGroup))
    {
      ((ViewGroup)localViewParent).getChildVisibleRect(mOriginatingView, mContentRectOnScreen, null, true);
      mContentRectOnScreen.offset(mRootViewPositionOnScreen[0], mRootViewPositionOnScreen[1]);
    }
    else
    {
      mContentRectOnScreen.offset(mViewPositionOnScreen[0], mViewPositionOnScreen[1]);
    }
    if (isContentRectWithinBounds())
    {
      mFloatingToolbarVisibilityHelper.setOutOfBounds(false);
      mContentRectOnScreen.set(Math.max(mContentRectOnScreen.left, mViewRectOnScreen.left), Math.max(mContentRectOnScreen.top, mViewRectOnScreen.top), Math.min(mContentRectOnScreen.right, mViewRectOnScreen.right), Math.min(mContentRectOnScreen.bottom, mViewRectOnScreen.bottom + mBottomAllowance));
      if (!mContentRectOnScreen.equals(mPreviousContentRectOnScreen))
      {
        mOriginatingView.removeCallbacks(mMovingOff);
        mFloatingToolbarVisibilityHelper.setMoving(true);
        mOriginatingView.postDelayed(mMovingOff, 50L);
        mFloatingToolbar.setContentRect(mContentRectOnScreen);
        mFloatingToolbar.updateLayout();
      }
    }
    else
    {
      mFloatingToolbarVisibilityHelper.setOutOfBounds(true);
      mContentRectOnScreen.setEmpty();
    }
    mFloatingToolbarVisibilityHelper.updateToolbarVisibility();
    mPreviousContentRectOnScreen.set(mContentRectOnScreen);
  }
  
  private void reset()
  {
    mFloatingToolbar.dismiss();
    mFloatingToolbarVisibilityHelper.deactivate();
    mOriginatingView.removeCallbacks(mMovingOff);
    mOriginatingView.removeCallbacks(mHideOff);
  }
  
  private void setFloatingToolbar(FloatingToolbar paramFloatingToolbar)
  {
    mFloatingToolbar = paramFloatingToolbar.setMenu(mMenu).setOnMenuItemClickListener(new _..Lambda.FloatingActionMode.LU5MpPuKYDtwlFAuYhXYfzgLNLE(this));
    mFloatingToolbarVisibilityHelper = new FloatingToolbarVisibilityHelper(mFloatingToolbar);
    mFloatingToolbarVisibilityHelper.activate();
  }
  
  public void finish()
  {
    reset();
    mCallback.onDestroyActionMode(this);
  }
  
  public View getCustomView()
  {
    return null;
  }
  
  public Menu getMenu()
  {
    return mMenu;
  }
  
  public MenuInflater getMenuInflater()
  {
    return new MenuInflater(mContext);
  }
  
  public CharSequence getSubtitle()
  {
    return null;
  }
  
  public CharSequence getTitle()
  {
    return null;
  }
  
  public void hide(long paramLong)
  {
    long l = paramLong;
    if (paramLong == -1L) {
      l = ViewConfiguration.getDefaultActionModeHideDuration();
    }
    paramLong = Math.min(3000L, l);
    mOriginatingView.removeCallbacks(mHideOff);
    if (paramLong <= 0L)
    {
      mHideOff.run();
    }
    else
    {
      mFloatingToolbarVisibilityHelper.setHideRequested(true);
      mFloatingToolbarVisibilityHelper.updateToolbarVisibility();
      mOriginatingView.postDelayed(mHideOff, paramLong);
    }
  }
  
  public void invalidate()
  {
    mCallback.onPrepareActionMode(this, mMenu);
    invalidateContentRect();
  }
  
  public void invalidateContentRect()
  {
    mCallback.onGetContentRect(this, mOriginatingView, mContentRect);
    repositionToolbar();
  }
  
  public void onWindowFocusChanged(boolean paramBoolean)
  {
    mFloatingToolbarVisibilityHelper.setWindowFocused(paramBoolean);
    mFloatingToolbarVisibilityHelper.updateToolbarVisibility();
  }
  
  public void setCustomView(View paramView) {}
  
  public void setOutsideTouchable(boolean paramBoolean, PopupWindow.OnDismissListener paramOnDismissListener)
  {
    mFloatingToolbar.setOutsideTouchable(paramBoolean, paramOnDismissListener);
  }
  
  public void setSubtitle(int paramInt) {}
  
  public void setSubtitle(CharSequence paramCharSequence) {}
  
  public void setTitle(int paramInt) {}
  
  public void setTitle(CharSequence paramCharSequence) {}
  
  public void updateViewLocationInWindow()
  {
    mOriginatingView.getLocationOnScreen(mViewPositionOnScreen);
    mOriginatingView.getRootView().getLocationOnScreen(mRootViewPositionOnScreen);
    mOriginatingView.getGlobalVisibleRect(mViewRectOnScreen);
    mViewRectOnScreen.offset(mRootViewPositionOnScreen[0], mRootViewPositionOnScreen[1]);
    if ((!Arrays.equals(mViewPositionOnScreen, mPreviousViewPositionOnScreen)) || (!mViewRectOnScreen.equals(mPreviousViewRectOnScreen)))
    {
      repositionToolbar();
      mPreviousViewPositionOnScreen[0] = mViewPositionOnScreen[0];
      mPreviousViewPositionOnScreen[1] = mViewPositionOnScreen[1];
      mPreviousViewRectOnScreen.set(mViewRectOnScreen);
    }
  }
  
  private static final class FloatingToolbarVisibilityHelper
  {
    private static final long MIN_SHOW_DURATION_FOR_MOVE_HIDE = 500L;
    private boolean mActive;
    private boolean mHideRequested;
    private long mLastShowTime;
    private boolean mMoving;
    private boolean mOutOfBounds;
    private final FloatingToolbar mToolbar;
    private boolean mWindowFocused = true;
    
    public FloatingToolbarVisibilityHelper(FloatingToolbar paramFloatingToolbar)
    {
      mToolbar = ((FloatingToolbar)Preconditions.checkNotNull(paramFloatingToolbar));
    }
    
    public void activate()
    {
      mHideRequested = false;
      mMoving = false;
      mOutOfBounds = false;
      mWindowFocused = true;
      mActive = true;
    }
    
    public void deactivate()
    {
      mActive = false;
      mToolbar.dismiss();
    }
    
    public void setHideRequested(boolean paramBoolean)
    {
      mHideRequested = paramBoolean;
    }
    
    public void setMoving(boolean paramBoolean)
    {
      int i;
      if (System.currentTimeMillis() - mLastShowTime > 500L) {
        i = 1;
      } else {
        i = 0;
      }
      if ((!paramBoolean) || (i != 0)) {
        mMoving = paramBoolean;
      }
    }
    
    public void setOutOfBounds(boolean paramBoolean)
    {
      mOutOfBounds = paramBoolean;
    }
    
    public void setWindowFocused(boolean paramBoolean)
    {
      mWindowFocused = paramBoolean;
    }
    
    public void updateToolbarVisibility()
    {
      if (!mActive) {
        return;
      }
      if ((!mHideRequested) && (!mMoving) && (!mOutOfBounds) && (mWindowFocused))
      {
        mToolbar.show();
        mLastShowTime = System.currentTimeMillis();
      }
      else
      {
        mToolbar.hide();
      }
    }
  }
}
