package com.android.internal.view.menu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MenuItemHoverListener;
import android.widget.MenuPopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

final class CascadingMenuPopup
  extends MenuPopup
  implements MenuPresenter, View.OnKeyListener, PopupWindow.OnDismissListener
{
  private static final int HORIZ_POSITION_LEFT = 0;
  private static final int HORIZ_POSITION_RIGHT = 1;
  private static final int ITEM_LAYOUT = 17367139;
  private static final int SUBMENU_TIMEOUT_MS = 200;
  private View mAnchorView;
  private final View.OnAttachStateChangeListener mAttachStateChangeListener = new View.OnAttachStateChangeListener()
  {
    public void onViewAttachedToWindow(View paramAnonymousView) {}
    
    public void onViewDetachedFromWindow(View paramAnonymousView)
    {
      if (mTreeObserver != null)
      {
        if (!mTreeObserver.isAlive()) {
          CascadingMenuPopup.access$202(CascadingMenuPopup.this, paramAnonymousView.getViewTreeObserver());
        }
        mTreeObserver.removeGlobalOnLayoutListener(mGlobalLayoutListener);
      }
      paramAnonymousView.removeOnAttachStateChangeListener(this);
    }
  };
  private final Context mContext;
  private int mDropDownGravity = 0;
  private boolean mForceShowIcon;
  private final ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener()
  {
    public void onGlobalLayout()
    {
      if ((isShowing()) && (mShowingMenus.size() > 0) && (!mShowingMenus.get(0)).window.isModal()))
      {
        Object localObject = mShownAnchorView;
        if ((localObject != null) && (((View)localObject).isShown())) {
          localObject = mShowingMenus.iterator();
        }
        while (((Iterator)localObject).hasNext())
        {
          nextwindow.show();
          continue;
          dismiss();
        }
      }
    }
  };
  private boolean mHasXOffset;
  private boolean mHasYOffset;
  private int mLastPosition;
  private final MenuItemHoverListener mMenuItemHoverListener = new MenuItemHoverListener()
  {
    public void onItemHoverEnter(final MenuBuilder paramAnonymousMenuBuilder, final MenuItem paramAnonymousMenuItem)
    {
      Handler localHandler = mSubMenuHoverHandler;
      final CascadingMenuPopup.CascadingMenuInfo localCascadingMenuInfo = null;
      localHandler.removeCallbacksAndMessages(null);
      int i = -1;
      int j = 0;
      int k = mShowingMenus.size();
      int m;
      for (;;)
      {
        m = i;
        if (j >= k) {
          break;
        }
        if (paramAnonymousMenuBuilder == mShowingMenus.get(j)).menu)
        {
          m = j;
          break;
        }
        j++;
      }
      if (m == -1) {
        return;
      }
      j = m + 1;
      if (j < mShowingMenus.size()) {
        localCascadingMenuInfo = (CascadingMenuPopup.CascadingMenuInfo)mShowingMenus.get(j);
      }
      paramAnonymousMenuItem = new Runnable()
      {
        public void run()
        {
          if (localCascadingMenuInfo != null)
          {
            CascadingMenuPopup.access$502(CascadingMenuPopup.this, true);
            localCascadingMenuInfomenu.close(false);
            CascadingMenuPopup.access$502(CascadingMenuPopup.this, false);
          }
          if ((paramAnonymousMenuItem.isEnabled()) && (paramAnonymousMenuItem.hasSubMenu())) {
            paramAnonymousMenuBuilder.performItemAction(paramAnonymousMenuItem, 0);
          }
        }
      };
      long l = SystemClock.uptimeMillis();
      mSubMenuHoverHandler.postAtTime(paramAnonymousMenuItem, paramAnonymousMenuBuilder, l + 200L);
    }
    
    public void onItemHoverExit(MenuBuilder paramAnonymousMenuBuilder, MenuItem paramAnonymousMenuItem)
    {
      mSubMenuHoverHandler.removeCallbacksAndMessages(paramAnonymousMenuBuilder);
    }
  };
  private final int mMenuMaxWidth;
  private PopupWindow.OnDismissListener mOnDismissListener;
  private final boolean mOverflowOnly;
  private final List<MenuBuilder> mPendingMenus = new LinkedList();
  private final int mPopupStyleAttr;
  private final int mPopupStyleRes;
  private MenuPresenter.Callback mPresenterCallback;
  private int mRawDropDownGravity = 0;
  private boolean mShouldCloseImmediately;
  private boolean mShowTitle;
  private final List<CascadingMenuInfo> mShowingMenus = new ArrayList();
  private View mShownAnchorView;
  private final Handler mSubMenuHoverHandler;
  private ViewTreeObserver mTreeObserver;
  private int mXOffset;
  private int mYOffset;
  
  public CascadingMenuPopup(Context paramContext, View paramView, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    mContext = ((Context)Preconditions.checkNotNull(paramContext));
    mAnchorView = ((View)Preconditions.checkNotNull(paramView));
    mPopupStyleAttr = paramInt1;
    mPopupStyleRes = paramInt2;
    mOverflowOnly = paramBoolean;
    mForceShowIcon = false;
    mLastPosition = getInitialMenuPosition();
    paramContext = paramContext.getResources();
    mMenuMaxWidth = Math.max(getDisplayMetricswidthPixels / 2, paramContext.getDimensionPixelSize(17105088));
    mSubMenuHoverHandler = new Handler();
  }
  
  private MenuPopupWindow createPopupWindow()
  {
    MenuPopupWindow localMenuPopupWindow = new MenuPopupWindow(mContext, null, mPopupStyleAttr, mPopupStyleRes);
    localMenuPopupWindow.setHoverListener(mMenuItemHoverListener);
    localMenuPopupWindow.setOnItemClickListener(this);
    localMenuPopupWindow.setOnDismissListener(this);
    localMenuPopupWindow.setAnchorView(mAnchorView);
    localMenuPopupWindow.setDropDownGravity(mDropDownGravity);
    localMenuPopupWindow.setModal(true);
    localMenuPopupWindow.setInputMethodMode(2);
    return localMenuPopupWindow;
  }
  
  private int findIndexOfAddedMenu(MenuBuilder paramMenuBuilder)
  {
    int i = 0;
    int j = mShowingMenus.size();
    while (i < j)
    {
      if (paramMenuBuilder == mShowingMenus.get(i)).menu) {
        return i;
      }
      i++;
    }
    return -1;
  }
  
  private MenuItem findMenuItemForSubmenu(MenuBuilder paramMenuBuilder1, MenuBuilder paramMenuBuilder2)
  {
    int i = 0;
    int j = paramMenuBuilder1.size();
    while (i < j)
    {
      MenuItem localMenuItem = paramMenuBuilder1.getItem(i);
      if ((localMenuItem.hasSubMenu()) && (paramMenuBuilder2 == localMenuItem.getSubMenu())) {
        return localMenuItem;
      }
      i++;
    }
    return null;
  }
  
  private View findParentViewForSubmenu(CascadingMenuInfo paramCascadingMenuInfo, MenuBuilder paramMenuBuilder)
  {
    paramMenuBuilder = findMenuItemForSubmenu(menu, paramMenuBuilder);
    if (paramMenuBuilder == null) {
      return null;
    }
    ListView localListView = paramCascadingMenuInfo.getListView();
    paramCascadingMenuInfo = localListView.getAdapter();
    int i;
    if ((paramCascadingMenuInfo instanceof HeaderViewListAdapter))
    {
      paramCascadingMenuInfo = (HeaderViewListAdapter)paramCascadingMenuInfo;
      i = paramCascadingMenuInfo.getHeadersCount();
      paramCascadingMenuInfo = (MenuAdapter)paramCascadingMenuInfo.getWrappedAdapter();
    }
    else
    {
      i = 0;
      paramCascadingMenuInfo = (MenuAdapter)paramCascadingMenuInfo;
    }
    int j = -1;
    int k = 0;
    int m = paramCascadingMenuInfo.getCount();
    int n;
    for (;;)
    {
      n = j;
      if (k >= m) {
        break;
      }
      if (paramMenuBuilder == paramCascadingMenuInfo.getItem(k))
      {
        n = k;
        break;
      }
      k++;
    }
    if (n == -1) {
      return null;
    }
    k = n + i - localListView.getFirstVisiblePosition();
    if ((k >= 0) && (k < localListView.getChildCount())) {
      return localListView.getChildAt(k);
    }
    return null;
  }
  
  private int getInitialMenuPosition()
  {
    int i = mAnchorView.getLayoutDirection();
    int j = 1;
    if (i == 1) {
      j = 0;
    }
    return j;
  }
  
  private int getNextMenuPosition(int paramInt)
  {
    ListView localListView = ((CascadingMenuInfo)mShowingMenus.get(mShowingMenus.size() - 1)).getListView();
    int[] arrayOfInt = new int[2];
    localListView.getLocationOnScreen(arrayOfInt);
    Rect localRect = new Rect();
    mShownAnchorView.getWindowVisibleDisplayFrame(localRect);
    if (mLastPosition == 1)
    {
      if (arrayOfInt[0] + localListView.getWidth() + paramInt > right) {
        return 0;
      }
      return 1;
    }
    if (arrayOfInt[0] - paramInt < 0) {
      return 1;
    }
    return 0;
  }
  
  private void showMenu(MenuBuilder paramMenuBuilder)
  {
    Object localObject1 = LayoutInflater.from(mContext);
    Object localObject2 = new MenuAdapter(paramMenuBuilder, (LayoutInflater)localObject1, mOverflowOnly, 17367139);
    if ((!isShowing()) && (mForceShowIcon)) {
      ((MenuAdapter)localObject2).setForceShowIcon(true);
    } else if (isShowing()) {
      ((MenuAdapter)localObject2).setForceShowIcon(MenuPopup.shouldPreserveIconSpacing(paramMenuBuilder));
    }
    int i = measureIndividualMenuWidth((ListAdapter)localObject2, null, mContext, mMenuMaxWidth);
    MenuPopupWindow localMenuPopupWindow = createPopupWindow();
    localMenuPopupWindow.setAdapter((ListAdapter)localObject2);
    localMenuPopupWindow.setContentWidth(i);
    localMenuPopupWindow.setDropDownGravity(mDropDownGravity);
    if (mShowingMenus.size() > 0)
    {
      localObject2 = (CascadingMenuInfo)mShowingMenus.get(mShowingMenus.size() - 1);
      localObject3 = findParentViewForSubmenu((CascadingMenuInfo)localObject2, paramMenuBuilder);
    }
    else
    {
      localObject2 = null;
      localObject3 = null;
    }
    if (localObject3 != null)
    {
      localMenuPopupWindow.setAnchorView((View)localObject3);
      localMenuPopupWindow.setTouchModal(false);
      localMenuPopupWindow.setEnterTransition(null);
      int j = getNextMenuPosition(i);
      int k;
      if (j == 1) {
        k = 1;
      } else {
        k = 0;
      }
      mLastPosition = j;
      if ((mDropDownGravity & 0x5) == 5) {
        if (k != 0) {
          k = i;
        }
      }
      for (;;)
      {
        break;
        k = -((View)localObject3).getWidth();
        continue;
        if (k != 0) {
          k = ((View)localObject3).getWidth();
        } else {
          k = -i;
        }
      }
      localMenuPopupWindow.setHorizontalOffset(k);
      localMenuPopupWindow.setOverlapAnchor(true);
      localMenuPopupWindow.setVerticalOffset(0);
    }
    else
    {
      if (mHasXOffset) {
        localMenuPopupWindow.setHorizontalOffset(mXOffset);
      }
      if (mHasYOffset) {
        localMenuPopupWindow.setVerticalOffset(mYOffset);
      }
      localMenuPopupWindow.setEpicenterBounds(getEpicenterBounds());
    }
    Object localObject3 = new CascadingMenuInfo(localMenuPopupWindow, paramMenuBuilder, mLastPosition);
    mShowingMenus.add(localObject3);
    localMenuPopupWindow.show();
    localObject3 = localMenuPopupWindow.getListView();
    ((ListView)localObject3).setOnKeyListener(this);
    if ((localObject2 == null) && (mShowTitle) && (paramMenuBuilder.getHeaderTitle() != null))
    {
      localObject1 = (FrameLayout)((LayoutInflater)localObject1).inflate(17367243, (ViewGroup)localObject3, false);
      localObject2 = (TextView)((FrameLayout)localObject1).findViewById(16908310);
      ((FrameLayout)localObject1).setEnabled(false);
      ((TextView)localObject2).setText(paramMenuBuilder.getHeaderTitle());
      ((ListView)localObject3).addHeaderView((View)localObject1, null, false);
      localMenuPopupWindow.show();
    }
  }
  
  public void addMenu(MenuBuilder paramMenuBuilder)
  {
    paramMenuBuilder.addMenuPresenter(this, mContext);
    if (isShowing()) {
      showMenu(paramMenuBuilder);
    } else {
      mPendingMenus.add(paramMenuBuilder);
    }
  }
  
  public void dismiss()
  {
    int i = mShowingMenus.size();
    if (i > 0)
    {
      CascadingMenuInfo[] arrayOfCascadingMenuInfo = (CascadingMenuInfo[])mShowingMenus.toArray(new CascadingMenuInfo[i]);
      i--;
      while (i >= 0)
      {
        CascadingMenuInfo localCascadingMenuInfo = arrayOfCascadingMenuInfo[i];
        if (window.isShowing()) {
          window.dismiss();
        }
        i--;
      }
    }
  }
  
  public boolean flagActionItems()
  {
    return false;
  }
  
  public ListView getListView()
  {
    ListView localListView;
    if (mShowingMenus.isEmpty()) {
      localListView = null;
    } else {
      localListView = ((CascadingMenuInfo)mShowingMenus.get(mShowingMenus.size() - 1)).getListView();
    }
    return localListView;
  }
  
  public boolean isShowing()
  {
    int i = mShowingMenus.size();
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (i > 0)
    {
      bool2 = bool1;
      if (mShowingMenus.get(0)).window.isShowing()) {
        bool2 = true;
      }
    }
    return bool2;
  }
  
  public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean)
  {
    int i = findIndexOfAddedMenu(paramMenuBuilder);
    if (i < 0) {
      return;
    }
    int j = i + 1;
    if (j < mShowingMenus.size()) {
      mShowingMenus.get(j)).menu.close(false);
    }
    CascadingMenuInfo localCascadingMenuInfo = (CascadingMenuInfo)mShowingMenus.remove(i);
    menu.removeMenuPresenter(this);
    if (mShouldCloseImmediately)
    {
      window.setExitTransition(null);
      window.setAnimationStyle(0);
    }
    window.dismiss();
    i = mShowingMenus.size();
    if (i > 0) {
      mLastPosition = mShowingMenus.get(i - 1)).position;
    } else {
      mLastPosition = getInitialMenuPosition();
    }
    if (i == 0)
    {
      dismiss();
      if (mPresenterCallback != null) {
        mPresenterCallback.onCloseMenu(paramMenuBuilder, true);
      }
      if (mTreeObserver != null)
      {
        if (mTreeObserver.isAlive()) {
          mTreeObserver.removeGlobalOnLayoutListener(mGlobalLayoutListener);
        }
        mTreeObserver = null;
      }
      mShownAnchorView.removeOnAttachStateChangeListener(mAttachStateChangeListener);
      mOnDismissListener.onDismiss();
    }
    else if (paramBoolean)
    {
      mShowingMenus.get(0)).menu.close(false);
    }
  }
  
  public void onDismiss()
  {
    Object localObject1 = null;
    int i = 0;
    int j = mShowingMenus.size();
    Object localObject2;
    for (;;)
    {
      localObject2 = localObject1;
      if (i >= j) {
        break;
      }
      localObject2 = (CascadingMenuInfo)mShowingMenus.get(i);
      if (!window.isShowing()) {
        break;
      }
      i++;
    }
    if (localObject2 != null) {
      menu.close(false);
    }
  }
  
  public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent)
  {
    if ((paramKeyEvent.getAction() == 1) && (paramInt == 82))
    {
      dismiss();
      return true;
    }
    return false;
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {}
  
  public Parcelable onSaveInstanceState()
  {
    return null;
  }
  
  public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder)
  {
    Iterator localIterator = mShowingMenus.iterator();
    while (localIterator.hasNext())
    {
      CascadingMenuInfo localCascadingMenuInfo = (CascadingMenuInfo)localIterator.next();
      if (paramSubMenuBuilder == menu)
      {
        localCascadingMenuInfo.getListView().requestFocus();
        return true;
      }
    }
    if (paramSubMenuBuilder.hasVisibleItems())
    {
      addMenu(paramSubMenuBuilder);
      if (mPresenterCallback != null) {
        mPresenterCallback.onOpenSubMenu(paramSubMenuBuilder);
      }
      return true;
    }
    return false;
  }
  
  public void setAnchorView(View paramView)
  {
    if (mAnchorView != paramView)
    {
      mAnchorView = paramView;
      mDropDownGravity = Gravity.getAbsoluteGravity(mRawDropDownGravity, mAnchorView.getLayoutDirection());
    }
  }
  
  public void setCallback(MenuPresenter.Callback paramCallback)
  {
    mPresenterCallback = paramCallback;
  }
  
  public void setForceShowIcon(boolean paramBoolean)
  {
    mForceShowIcon = paramBoolean;
  }
  
  public void setGravity(int paramInt)
  {
    if (mRawDropDownGravity != paramInt)
    {
      mRawDropDownGravity = paramInt;
      mDropDownGravity = Gravity.getAbsoluteGravity(paramInt, mAnchorView.getLayoutDirection());
    }
  }
  
  public void setHorizontalOffset(int paramInt)
  {
    mHasXOffset = true;
    mXOffset = paramInt;
  }
  
  public void setOnDismissListener(PopupWindow.OnDismissListener paramOnDismissListener)
  {
    mOnDismissListener = paramOnDismissListener;
  }
  
  public void setShowTitle(boolean paramBoolean)
  {
    mShowTitle = paramBoolean;
  }
  
  public void setVerticalOffset(int paramInt)
  {
    mHasYOffset = true;
    mYOffset = paramInt;
  }
  
  public void show()
  {
    if (isShowing()) {
      return;
    }
    Iterator localIterator = mPendingMenus.iterator();
    while (localIterator.hasNext()) {
      showMenu((MenuBuilder)localIterator.next());
    }
    mPendingMenus.clear();
    mShownAnchorView = mAnchorView;
    if (mShownAnchorView != null)
    {
      int i;
      if (mTreeObserver == null) {
        i = 1;
      } else {
        i = 0;
      }
      mTreeObserver = mShownAnchorView.getViewTreeObserver();
      if (i != 0) {
        mTreeObserver.addOnGlobalLayoutListener(mGlobalLayoutListener);
      }
      mShownAnchorView.addOnAttachStateChangeListener(mAttachStateChangeListener);
    }
  }
  
  public void updateMenuView(boolean paramBoolean)
  {
    Iterator localIterator = mShowingMenus.iterator();
    while (localIterator.hasNext()) {
      toMenuAdapter(((CascadingMenuInfo)localIterator.next()).getListView().getAdapter()).notifyDataSetChanged();
    }
  }
  
  private static class CascadingMenuInfo
  {
    public final MenuBuilder menu;
    public final int position;
    public final MenuPopupWindow window;
    
    public CascadingMenuInfo(MenuPopupWindow paramMenuPopupWindow, MenuBuilder paramMenuBuilder, int paramInt)
    {
      window = paramMenuPopupWindow;
      menu = paramMenuBuilder;
      position = paramInt;
    }
    
    public ListView getListView()
    {
      return window.getListView();
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface HorizPosition {}
}
