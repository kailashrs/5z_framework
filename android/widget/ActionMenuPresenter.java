package android.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.ActionProvider;
import android.view.ActionProvider.SubUiVisibilityListener;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.accessibility.AccessibilityNodeInfo;
import com.android.internal.view.ActionBarPolicy;
import com.android.internal.view.menu.ActionMenuItemView;
import com.android.internal.view.menu.ActionMenuItemView.PopupCallback;
import com.android.internal.view.menu.BaseMenuPresenter;
import com.android.internal.view.menu.MenuBuilder;
import com.android.internal.view.menu.MenuItemImpl;
import com.android.internal.view.menu.MenuPopup;
import com.android.internal.view.menu.MenuPopupHelper;
import com.android.internal.view.menu.MenuPresenter.Callback;
import com.android.internal.view.menu.MenuView;
import com.android.internal.view.menu.MenuView.ItemView;
import com.android.internal.view.menu.ShowableListMenu;
import com.android.internal.view.menu.SubMenuBuilder;
import java.util.ArrayList;
import java.util.List;

public class ActionMenuPresenter
  extends BaseMenuPresenter
  implements ActionProvider.SubUiVisibilityListener
{
  private static final boolean ACTIONBAR_ANIMATIONS_ENABLED = false;
  private static final int ITEM_ANIMATION_DURATION = 150;
  private final SparseBooleanArray mActionButtonGroups = new SparseBooleanArray();
  private ActionButtonSubmenu mActionButtonPopup;
  private int mActionItemWidthLimit;
  private View.OnAttachStateChangeListener mAttachStateChangeListener = new View.OnAttachStateChangeListener()
  {
    public void onViewAttachedToWindow(View paramAnonymousView) {}
    
    public void onViewDetachedFromWindow(View paramAnonymousView)
    {
      ((View)mMenuView).getViewTreeObserver().removeOnPreDrawListener(mItemAnimationPreDrawListener);
      mPreLayoutItems.clear();
      mPostLayoutItems.clear();
    }
  };
  private boolean mExpandedActionViewsExclusive;
  private ViewTreeObserver.OnPreDrawListener mItemAnimationPreDrawListener = new ViewTreeObserver.OnPreDrawListener()
  {
    public boolean onPreDraw()
    {
      ActionMenuPresenter.this.computeMenuItemAnimationInfo(false);
      ((View)mMenuView).getViewTreeObserver().removeOnPreDrawListener(this);
      ActionMenuPresenter.this.runItemAnimations();
      return true;
    }
  };
  private int mMaxItems;
  private boolean mMaxItemsSet;
  private int mMinCellSize;
  int mOpenSubMenuId;
  private OverflowMenuButton mOverflowButton;
  private OverflowPopup mOverflowPopup;
  private Drawable mPendingOverflowIcon;
  private boolean mPendingOverflowIconSet;
  private ActionMenuPopupCallback mPopupCallback;
  final PopupPresenterCallback mPopupPresenterCallback = new PopupPresenterCallback(null);
  private SparseArray<MenuItemLayoutInfo> mPostLayoutItems = new SparseArray();
  private OpenOverflowRunnable mPostedOpenRunnable;
  private SparseArray<MenuItemLayoutInfo> mPreLayoutItems = new SparseArray();
  private boolean mReserveOverflow;
  private boolean mReserveOverflowSet;
  private List<ItemAnimationInfo> mRunningItemAnimations = new ArrayList();
  private boolean mStrictWidthLimit;
  private int mWidthLimit;
  private boolean mWidthLimitSet;
  
  public ActionMenuPresenter(Context paramContext)
  {
    super(paramContext, 17367071, 17367070);
  }
  
  private void computeMenuItemAnimationInfo(boolean paramBoolean)
  {
    ViewGroup localViewGroup = (ViewGroup)mMenuView;
    int i = localViewGroup.getChildCount();
    SparseArray localSparseArray;
    if (paramBoolean) {
      localSparseArray = mPreLayoutItems;
    } else {
      localSparseArray = mPostLayoutItems;
    }
    for (int j = 0; j < i; j++)
    {
      View localView = localViewGroup.getChildAt(j);
      int k = localView.getId();
      if ((k > 0) && (localView.getWidth() != 0) && (localView.getHeight() != 0)) {
        localSparseArray.put(k, new MenuItemLayoutInfo(localView, paramBoolean));
      }
    }
  }
  
  private View findViewForItem(MenuItem paramMenuItem)
  {
    ViewGroup localViewGroup = (ViewGroup)mMenuView;
    if (localViewGroup == null) {
      return null;
    }
    int i = localViewGroup.getChildCount();
    for (int j = 0; j < i; j++)
    {
      View localView = localViewGroup.getChildAt(j);
      if (((localView instanceof MenuView.ItemView)) && (((MenuView.ItemView)localView).getItemData() == paramMenuItem)) {
        return localView;
      }
    }
    return null;
  }
  
  private void runItemAnimations()
  {
    int j;
    int k;
    Object localObject2;
    Object localObject3;
    float f1;
    float f2;
    for (int i = 0; i < mPreLayoutItems.size(); i++)
    {
      j = mPreLayoutItems.keyAt(i);
      Object localObject1 = (MenuItemLayoutInfo)mPreLayoutItems.get(j);
      k = mPostLayoutItems.indexOfKey(j);
      if (k >= 0)
      {
        MenuItemLayoutInfo localMenuItemLayoutInfo = (MenuItemLayoutInfo)mPostLayoutItems.valueAt(k);
        localObject2 = null;
        localObject3 = null;
        if (left != left) {
          localObject2 = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, new float[] { left - left, 0.0F });
        }
        if (top != top) {
          localObject3 = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, new float[] { top - top, 0.0F });
        }
        if ((localObject2 != null) || (localObject3 != null))
        {
          for (k = 0; k < mRunningItemAnimations.size(); k++)
          {
            localObject1 = (ItemAnimationInfo)mRunningItemAnimations.get(k);
            if ((id == j) && (animType == 0)) {
              animator.cancel();
            }
          }
          if (localObject2 != null)
          {
            if (localObject3 != null) {}
            for (localObject2 = ObjectAnimator.ofPropertyValuesHolder(view, new PropertyValuesHolder[] { localObject2, localObject3 });; localObject2 = ObjectAnimator.ofPropertyValuesHolder(view, new PropertyValuesHolder[] { localObject2 })) {
              break;
            }
          }
          localObject2 = ObjectAnimator.ofPropertyValuesHolder(view, new PropertyValuesHolder[] { localObject3 });
          ((ObjectAnimator)localObject2).setDuration(150L);
          ((ObjectAnimator)localObject2).start();
          localObject3 = new ItemAnimationInfo(j, localMenuItemLayoutInfo, (Animator)localObject2, 0);
          mRunningItemAnimations.add(localObject3);
          ((ObjectAnimator)localObject2).addListener(new AnimatorListenerAdapter()
          {
            public void onAnimationEnd(Animator paramAnonymousAnimator)
            {
              for (int i = 0; i < mRunningItemAnimations.size(); i++) {
                if (mRunningItemAnimations.get(i)).animator == paramAnonymousAnimator)
                {
                  mRunningItemAnimations.remove(i);
                  break;
                }
              }
            }
          });
        }
        mPostLayoutItems.remove(j);
      }
      else
      {
        f1 = 1.0F;
        k = 0;
        while (k < mRunningItemAnimations.size())
        {
          localObject2 = (ItemAnimationInfo)mRunningItemAnimations.get(k);
          f2 = f1;
          if (id == j)
          {
            f2 = f1;
            if (animType == 1)
            {
              f2 = menuItemLayoutInfo.view.getAlpha();
              animator.cancel();
            }
          }
          k++;
          f1 = f2;
        }
        localObject2 = ObjectAnimator.ofFloat(view, View.ALPHA, new float[] { f1, 0.0F });
        ((ViewGroup)mMenuView).getOverlay().add(view);
        ((ObjectAnimator)localObject2).setDuration(150L);
        ((ObjectAnimator)localObject2).start();
        localObject3 = new ItemAnimationInfo(j, (MenuItemLayoutInfo)localObject1, (Animator)localObject2, 2);
        mRunningItemAnimations.add(localObject3);
        ((ObjectAnimator)localObject2).addListener(new AnimatorListenerAdapter()
        {
          public void onAnimationEnd(Animator paramAnonymousAnimator)
          {
            for (int i = 0; i < mRunningItemAnimations.size(); i++) {
              if (mRunningItemAnimations.get(i)).animator == paramAnonymousAnimator)
              {
                mRunningItemAnimations.remove(i);
                break;
              }
            }
            ((ViewGroup)mMenuView).getOverlay().remove(val$menuItemLayoutInfoPre.view);
          }
        });
      }
    }
    for (i = 0; i < mPostLayoutItems.size(); i++)
    {
      j = mPostLayoutItems.keyAt(i);
      k = mPostLayoutItems.indexOfKey(j);
      if (k >= 0)
      {
        localObject2 = (MenuItemLayoutInfo)mPostLayoutItems.valueAt(k);
        f2 = 0.0F;
        k = 0;
        while (k < mRunningItemAnimations.size())
        {
          localObject3 = (ItemAnimationInfo)mRunningItemAnimations.get(k);
          f1 = f2;
          if (id == j)
          {
            f1 = f2;
            if (animType == 2)
            {
              f1 = menuItemLayoutInfo.view.getAlpha();
              animator.cancel();
            }
          }
          k++;
          f2 = f1;
        }
        localObject3 = ObjectAnimator.ofFloat(view, View.ALPHA, new float[] { f2, 1.0F });
        ((ObjectAnimator)localObject3).start();
        ((ObjectAnimator)localObject3).setDuration(150L);
        localObject2 = new ItemAnimationInfo(j, (MenuItemLayoutInfo)localObject2, (Animator)localObject3, 1);
        mRunningItemAnimations.add(localObject2);
        ((ObjectAnimator)localObject3).addListener(new AnimatorListenerAdapter()
        {
          public void onAnimationEnd(Animator paramAnonymousAnimator)
          {
            for (int i = 0; i < mRunningItemAnimations.size(); i++) {
              if (mRunningItemAnimations.get(i)).animator == paramAnonymousAnimator)
              {
                mRunningItemAnimations.remove(i);
                break;
              }
            }
          }
        });
      }
    }
    mPreLayoutItems.clear();
    mPostLayoutItems.clear();
  }
  
  private void setupItemAnimations()
  {
    computeMenuItemAnimationInfo(true);
    ((View)mMenuView).getViewTreeObserver().addOnPreDrawListener(mItemAnimationPreDrawListener);
  }
  
  public void bindItemView(MenuItemImpl paramMenuItemImpl, MenuView.ItemView paramItemView)
  {
    paramItemView.initialize(paramMenuItemImpl, 0);
    paramMenuItemImpl = (ActionMenuView)mMenuView;
    paramItemView = (ActionMenuItemView)paramItemView;
    paramItemView.setItemInvoker(paramMenuItemImpl);
    if (mPopupCallback == null) {
      mPopupCallback = new ActionMenuPopupCallback(null);
    }
    paramItemView.setPopupCallback(mPopupCallback);
  }
  
  public boolean dismissPopupMenus()
  {
    return hideOverflowMenu() | hideSubMenus();
  }
  
  public boolean filterLeftoverView(ViewGroup paramViewGroup, int paramInt)
  {
    if (paramViewGroup.getChildAt(paramInt) == mOverflowButton) {
      return false;
    }
    return super.filterLeftoverView(paramViewGroup, paramInt);
  }
  
  public boolean flagActionItems()
  {
    Object localObject1 = this;
    Object localObject2;
    int i;
    if (mMenu != null)
    {
      localObject2 = mMenu.getVisibleItems();
      i = ((ArrayList)localObject2).size();
    }
    else
    {
      localObject2 = null;
      i = 0;
    }
    int j = mMaxItems;
    int k = mActionItemWidthLimit;
    int m = View.MeasureSpec.makeMeasureSpec(0, 0);
    ViewGroup localViewGroup = (ViewGroup)mMenuView;
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    int i4 = 0;
    while (i4 < i)
    {
      localObject3 = (MenuItemImpl)((ArrayList)localObject2).get(i4);
      if (((MenuItemImpl)localObject3).requiresActionButton()) {
        n++;
      } else if (((MenuItemImpl)localObject3).requestsActionButton()) {
        i1++;
      } else {
        i3 = 1;
      }
      i5 = j;
      if (mExpandedActionViewsExclusive)
      {
        i5 = j;
        if (((MenuItemImpl)localObject3).isActionViewExpanded()) {
          i5 = 0;
        }
      }
      i4++;
      j = i5;
    }
    i4 = j;
    if (mReserveOverflow) {
      if (i3 == 0)
      {
        i4 = j;
        if (n + i1 <= j) {}
      }
      else
      {
        i4 = j - 1;
      }
    }
    j = i4 - n;
    Object localObject3 = mActionButtonGroups;
    ((SparseBooleanArray)localObject3).clear();
    i1 = 0;
    i4 = 0;
    if (mStrictWidthLimit)
    {
      i4 = k / mMinCellSize;
      i3 = mMinCellSize;
      i1 = mMinCellSize + k % i3 / i4;
    }
    int i6 = 0;
    i3 = i2;
    int i5 = n;
    n = k;
    k = i;
    for (i2 = i6;; i2++)
    {
      localObject1 = this;
      if (i2 >= k) {
        break;
      }
      MenuItemImpl localMenuItemImpl = (MenuItemImpl)((ArrayList)localObject2).get(i2);
      View localView;
      if (localMenuItemImpl.requiresActionButton())
      {
        localView = ((ActionMenuPresenter)localObject1).getItemView(localMenuItemImpl, null, localViewGroup);
        if (mStrictWidthLimit) {
          i4 -= ActionMenuView.measureChildForCells(localView, i1, i4, m, 0);
        } else {
          localView.measure(m, m);
        }
        i6 = localView.getMeasuredWidth();
        n -= i6;
        i = i3;
        if (i3 == 0) {
          i = i6;
        }
        i3 = localMenuItemImpl.getGroupId();
        if (i3 != 0) {
          ((SparseBooleanArray)localObject3).put(i3, true);
        }
        localMenuItemImpl.setIsActionButton(true);
        i3 = i;
      }
      for (;;)
      {
        break;
        if (localMenuItemImpl.requestsActionButton())
        {
          int i7 = localMenuItemImpl.getGroupId();
          boolean bool = ((SparseBooleanArray)localObject3).get(i7);
          int i8;
          if (((j > 0) || (bool)) && (n > 0) && ((!mStrictWidthLimit) || (i4 > 0))) {
            i8 = 1;
          } else {
            i8 = 0;
          }
          if (i8 != 0)
          {
            localView = ((ActionMenuPresenter)localObject1).getItemView(localMenuItemImpl, null, localViewGroup);
            if (mStrictWidthLimit)
            {
              i = ActionMenuView.measureChildForCells(localView, i1, i4, m, 0);
              i4 -= i;
              if (i == 0) {
                i8 = 0;
              }
            }
            else
            {
              localView.measure(m, m);
            }
            i6 = localView.getMeasuredWidth();
            n -= i6;
            i = i3;
            if (i3 == 0) {
              i = i6;
            }
            if (mStrictWidthLimit)
            {
              if (n >= 0) {
                i3 = 1;
              } else {
                i3 = 0;
              }
              i8 = i3 & i8;
              i3 = i;
            }
            else
            {
              if (n + i > 0) {
                i3 = 1;
              } else {
                i3 = 0;
              }
              i8 = i3 & i8;
              i3 = i;
            }
          }
          if ((i8 != 0) && (i7 != 0))
          {
            ((SparseBooleanArray)localObject3).put(i7, true);
            i = j;
          }
          else
          {
            localObject1 = localObject2;
            i = j;
            if (bool)
            {
              ((SparseBooleanArray)localObject3).put(i7, false);
              i6 = 0;
              for (;;)
              {
                localObject1 = localObject2;
                i = j;
                if (i6 >= i2) {
                  break;
                }
                localObject1 = (MenuItemImpl)((ArrayList)localObject2).get(i6);
                i = j;
                if (((MenuItemImpl)localObject1).getGroupId() == i7)
                {
                  i = j;
                  if (((MenuItemImpl)localObject1).isActionButton()) {
                    i = j + 1;
                  }
                  ((MenuItemImpl)localObject1).setIsActionButton(false);
                }
                i6++;
                j = i;
              }
            }
            localObject2 = localObject1;
          }
          j = i;
          if (i8 != 0) {
            j = i - 1;
          }
          localMenuItemImpl.setIsActionButton(i8);
        }
        else
        {
          localMenuItemImpl.setIsActionButton(false);
        }
      }
    }
    return true;
  }
  
  public View getItemView(MenuItemImpl paramMenuItemImpl, View paramView, ViewGroup paramViewGroup)
  {
    View localView = paramMenuItemImpl.getActionView();
    if ((localView == null) || (paramMenuItemImpl.hasCollapsibleActionView())) {
      localView = super.getItemView(paramMenuItemImpl, paramView, paramViewGroup);
    }
    int i;
    if (paramMenuItemImpl.isActionViewExpanded()) {
      i = 8;
    } else {
      i = 0;
    }
    localView.setVisibility(i);
    paramMenuItemImpl = (ActionMenuView)paramViewGroup;
    paramView = localView.getLayoutParams();
    if (!paramMenuItemImpl.checkLayoutParams(paramView)) {
      localView.setLayoutParams(paramMenuItemImpl.generateLayoutParams(paramView));
    }
    return localView;
  }
  
  public MenuView getMenuView(ViewGroup paramViewGroup)
  {
    MenuView localMenuView = mMenuView;
    paramViewGroup = super.getMenuView(paramViewGroup);
    if (localMenuView != paramViewGroup)
    {
      ((ActionMenuView)paramViewGroup).setPresenter(this);
      if (localMenuView != null) {
        ((View)localMenuView).removeOnAttachStateChangeListener(mAttachStateChangeListener);
      }
      ((View)paramViewGroup).addOnAttachStateChangeListener(mAttachStateChangeListener);
    }
    return paramViewGroup;
  }
  
  public Drawable getOverflowIcon()
  {
    if (mOverflowButton != null) {
      return mOverflowButton.getDrawable();
    }
    if (mPendingOverflowIconSet) {
      return mPendingOverflowIcon;
    }
    return null;
  }
  
  public boolean hideOverflowMenu()
  {
    if ((mPostedOpenRunnable != null) && (mMenuView != null))
    {
      ((View)mMenuView).removeCallbacks(mPostedOpenRunnable);
      mPostedOpenRunnable = null;
      return true;
    }
    OverflowPopup localOverflowPopup = mOverflowPopup;
    if (localOverflowPopup != null)
    {
      localOverflowPopup.dismiss();
      return true;
    }
    return false;
  }
  
  public boolean hideSubMenus()
  {
    if (mActionButtonPopup != null)
    {
      mActionButtonPopup.dismiss();
      return true;
    }
    return false;
  }
  
  public void initForMenu(Context paramContext, MenuBuilder paramMenuBuilder)
  {
    super.initForMenu(paramContext, paramMenuBuilder);
    paramMenuBuilder = paramContext.getResources();
    paramContext = ActionBarPolicy.get(paramContext);
    if (!mReserveOverflowSet) {
      mReserveOverflow = paramContext.showsOverflowMenuButton();
    }
    if (!mWidthLimitSet) {
      mWidthLimit = paramContext.getEmbeddedMenuWidthLimit();
    }
    if (!mMaxItemsSet) {
      mMaxItems = paramContext.getMaxActionButtons();
    }
    int i = mWidthLimit;
    if (mReserveOverflow)
    {
      if (mOverflowButton == null)
      {
        mOverflowButton = new OverflowMenuButton(mSystemContext);
        if (mPendingOverflowIconSet)
        {
          mOverflowButton.setImageDrawable(mPendingOverflowIcon);
          mPendingOverflowIcon = null;
          mPendingOverflowIconSet = false;
        }
        int j = View.MeasureSpec.makeMeasureSpec(0, 0);
        mOverflowButton.measure(j, j);
      }
      i -= mOverflowButton.getMeasuredWidth();
    }
    else
    {
      mOverflowButton = null;
    }
    mActionItemWidthLimit = i;
    mMinCellSize = ((int)(56.0F * getDisplayMetricsdensity));
  }
  
  public boolean isOverflowMenuShowPending()
  {
    boolean bool;
    if ((mPostedOpenRunnable == null) && (!isOverflowMenuShowing())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isOverflowMenuShowing()
  {
    boolean bool;
    if ((mOverflowPopup != null) && (mOverflowPopup.isShowing())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isOverflowReserved()
  {
    return mReserveOverflow;
  }
  
  public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean)
  {
    dismissPopupMenus();
    super.onCloseMenu(paramMenuBuilder, paramBoolean);
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    if (!mMaxItemsSet) {
      mMaxItems = ActionBarPolicy.get(mContext).getMaxActionButtons();
    }
    if (mMenu != null) {
      mMenu.onItemsChanged(true);
    }
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    paramParcelable = (SavedState)paramParcelable;
    if (openSubMenuId > 0)
    {
      paramParcelable = mMenu.findItem(openSubMenuId);
      if (paramParcelable != null) {
        onSubMenuSelected((SubMenuBuilder)paramParcelable.getSubMenu());
      }
    }
  }
  
  public Parcelable onSaveInstanceState()
  {
    SavedState localSavedState = new SavedState();
    openSubMenuId = mOpenSubMenuId;
    return localSavedState;
  }
  
  public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder)
  {
    boolean bool1 = paramSubMenuBuilder.hasVisibleItems();
    int i = 0;
    if (!bool1) {
      return false;
    }
    for (Object localObject = paramSubMenuBuilder; ((SubMenuBuilder)localObject).getParentMenu() != mMenu; localObject = (SubMenuBuilder)((SubMenuBuilder)localObject).getParentMenu()) {}
    localObject = findViewForItem(((SubMenuBuilder)localObject).getItem());
    if (localObject == null) {
      return false;
    }
    mOpenSubMenuId = paramSubMenuBuilder.getItem().getItemId();
    boolean bool2 = false;
    int j = paramSubMenuBuilder.size();
    for (;;)
    {
      bool1 = bool2;
      if (i >= j) {
        break;
      }
      MenuItem localMenuItem = paramSubMenuBuilder.getItem(i);
      if ((localMenuItem.isVisible()) && (localMenuItem.getIcon() != null))
      {
        bool1 = true;
        break;
      }
      i++;
    }
    mActionButtonPopup = new ActionButtonSubmenu(mContext, paramSubMenuBuilder, (View)localObject);
    mActionButtonPopup.setForceShowIcon(bool1);
    mActionButtonPopup.show();
    super.onSubMenuSelected(paramSubMenuBuilder);
    return true;
  }
  
  public void onSubUiVisibilityChanged(boolean paramBoolean)
  {
    if (paramBoolean) {
      super.onSubMenuSelected(null);
    } else if (mMenu != null) {
      mMenu.close(false);
    }
  }
  
  public void setExpandedActionViewsExclusive(boolean paramBoolean)
  {
    mExpandedActionViewsExclusive = paramBoolean;
  }
  
  public void setItemLimit(int paramInt)
  {
    mMaxItems = paramInt;
    mMaxItemsSet = true;
  }
  
  public void setMenuView(ActionMenuView paramActionMenuView)
  {
    if (paramActionMenuView != mMenuView)
    {
      if (mMenuView != null) {
        ((View)mMenuView).removeOnAttachStateChangeListener(mAttachStateChangeListener);
      }
      mMenuView = paramActionMenuView;
      paramActionMenuView.initialize(mMenu);
      paramActionMenuView.addOnAttachStateChangeListener(mAttachStateChangeListener);
    }
  }
  
  public void setOverflowIcon(Drawable paramDrawable)
  {
    if (mOverflowButton != null)
    {
      mOverflowButton.setImageDrawable(paramDrawable);
    }
    else
    {
      mPendingOverflowIconSet = true;
      mPendingOverflowIcon = paramDrawable;
    }
  }
  
  public void setReserveOverflow(boolean paramBoolean)
  {
    mReserveOverflow = paramBoolean;
    mReserveOverflowSet = true;
  }
  
  public void setWidthLimit(int paramInt, boolean paramBoolean)
  {
    mWidthLimit = paramInt;
    mStrictWidthLimit = paramBoolean;
    mWidthLimitSet = true;
  }
  
  public boolean shouldIncludeItem(int paramInt, MenuItemImpl paramMenuItemImpl)
  {
    return paramMenuItemImpl.isActionButton();
  }
  
  public boolean showOverflowMenu()
  {
    if ((mReserveOverflow) && (!isOverflowMenuShowing()) && (mMenu != null) && (mMenuView != null) && (mPostedOpenRunnable == null) && (!mMenu.getNonActionItems().isEmpty()))
    {
      mPostedOpenRunnable = new OpenOverflowRunnable(new OverflowPopup(mContext, mMenu, mOverflowButton, true));
      ((View)mMenuView).post(mPostedOpenRunnable);
      super.onSubMenuSelected(null);
      return true;
    }
    return false;
  }
  
  public void updateMenuView(boolean paramBoolean)
  {
    ((View)mMenuView).getParent();
    super.updateMenuView(paramBoolean);
    ((View)mMenuView).requestLayout();
    Object localObject = mMenu;
    boolean bool1 = false;
    if (localObject != null)
    {
      localObject = mMenu.getActionItems();
      i = ((ArrayList)localObject).size();
      for (j = 0; j < i; j++)
      {
        ActionProvider localActionProvider = ((MenuItemImpl)((ArrayList)localObject).get(j)).getActionProvider();
        if (localActionProvider != null) {
          localActionProvider.setSubUiVisibilityListener(this);
        }
      }
    }
    if (mMenu != null) {
      localObject = mMenu.getNonActionItems();
    } else {
      localObject = null;
    }
    int i = 0;
    int j = i;
    boolean bool2;
    if (mReserveOverflow)
    {
      j = i;
      if (localObject != null)
      {
        i = ((ArrayList)localObject).size();
        if (i == 1)
        {
          bool2 = ((MenuItemImpl)((ArrayList)localObject).get(0)).isActionViewExpanded() ^ true;
        }
        else
        {
          bool2 = bool1;
          if (i > 0) {
            bool2 = true;
          }
        }
      }
    }
    if (bool2)
    {
      if (mOverflowButton == null) {
        mOverflowButton = new OverflowMenuButton(mSystemContext);
      }
      localObject = (ViewGroup)mOverflowButton.getParent();
      if (localObject != mMenuView)
      {
        if (localObject != null) {
          ((ViewGroup)localObject).removeView(mOverflowButton);
        }
        localObject = (ActionMenuView)mMenuView;
        ((ActionMenuView)localObject).addView(mOverflowButton, ((ActionMenuView)localObject).generateOverflowButtonLayoutParams());
      }
    }
    else if ((mOverflowButton != null) && (mOverflowButton.getParent() == mMenuView))
    {
      ((ViewGroup)mMenuView).removeView(mOverflowButton);
    }
    ((ActionMenuView)mMenuView).setOverflowReserved(mReserveOverflow);
  }
  
  private class ActionButtonSubmenu
    extends MenuPopupHelper
  {
    public ActionButtonSubmenu(Context paramContext, SubMenuBuilder paramSubMenuBuilder, View paramView)
    {
      super(paramSubMenuBuilder, paramView, false, 16843844);
      if (!((MenuItemImpl)paramSubMenuBuilder.getItem()).isActionButton())
      {
        if (mOverflowButton == null) {
          paramContext = (View)mMenuView;
        } else {
          paramContext = mOverflowButton;
        }
        setAnchorView(paramContext);
      }
      setPresenterCallback(mPopupPresenterCallback);
    }
    
    protected void onDismiss()
    {
      ActionMenuPresenter.access$1702(ActionMenuPresenter.this, null);
      mOpenSubMenuId = 0;
      super.onDismiss();
    }
  }
  
  private class ActionMenuPopupCallback
    extends ActionMenuItemView.PopupCallback
  {
    private ActionMenuPopupCallback() {}
    
    public ShowableListMenu getPopup()
    {
      MenuPopup localMenuPopup;
      if (mActionButtonPopup != null) {
        localMenuPopup = mActionButtonPopup.getPopup();
      } else {
        localMenuPopup = null;
      }
      return localMenuPopup;
    }
  }
  
  private static class ItemAnimationInfo
  {
    static final int FADE_IN = 1;
    static final int FADE_OUT = 2;
    static final int MOVE = 0;
    int animType;
    Animator animator;
    int id;
    ActionMenuPresenter.MenuItemLayoutInfo menuItemLayoutInfo;
    
    ItemAnimationInfo(int paramInt1, ActionMenuPresenter.MenuItemLayoutInfo paramMenuItemLayoutInfo, Animator paramAnimator, int paramInt2)
    {
      id = paramInt1;
      menuItemLayoutInfo = paramMenuItemLayoutInfo;
      animator = paramAnimator;
      animType = paramInt2;
    }
  }
  
  private static class MenuItemLayoutInfo
  {
    int left;
    int top;
    View view;
    
    MenuItemLayoutInfo(View paramView, boolean paramBoolean)
    {
      left = paramView.getLeft();
      top = paramView.getTop();
      if (paramBoolean)
      {
        left = ((int)(left + paramView.getTranslationX()));
        top = ((int)(top + paramView.getTranslationY()));
      }
      view = paramView;
    }
  }
  
  private class OpenOverflowRunnable
    implements Runnable
  {
    private ActionMenuPresenter.OverflowPopup mPopup;
    
    public OpenOverflowRunnable(ActionMenuPresenter.OverflowPopup paramOverflowPopup)
    {
      mPopup = paramOverflowPopup;
    }
    
    public void run()
    {
      if (mMenu != null) {
        mMenu.changeMenuMode();
      }
      View localView = (View)mMenuView;
      if ((localView != null) && (localView.getWindowToken() != null) && (mPopup.tryShow())) {
        ActionMenuPresenter.access$1102(ActionMenuPresenter.this, mPopup);
      }
      ActionMenuPresenter.access$1202(ActionMenuPresenter.this, null);
    }
  }
  
  private class OverflowMenuButton
    extends ImageButton
    implements ActionMenuView.ActionMenuChildView
  {
    public OverflowMenuButton(Context paramContext)
    {
      super(null, 16843510);
      setClickable(true);
      setFocusable(true);
      setVisibility(0);
      setEnabled(true);
      setOnTouchListener(new ForwardingListener(this)
      {
        public ShowableListMenu getPopup()
        {
          if (mOverflowPopup == null) {
            return null;
          }
          return mOverflowPopup.getPopup();
        }
        
        public boolean onForwardingStarted()
        {
          showOverflowMenu();
          return true;
        }
        
        public boolean onForwardingStopped()
        {
          if (mPostedOpenRunnable != null) {
            return false;
          }
          hideOverflowMenu();
          return true;
        }
      });
    }
    
    public boolean needsDividerAfter()
    {
      return false;
    }
    
    public boolean needsDividerBefore()
    {
      return false;
    }
    
    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo paramAccessibilityNodeInfo)
    {
      super.onInitializeAccessibilityNodeInfoInternal(paramAccessibilityNodeInfo);
      paramAccessibilityNodeInfo.setCanOpenPopup(true);
    }
    
    public boolean performClick()
    {
      if (super.performClick()) {
        return true;
      }
      playSoundEffect(0);
      showOverflowMenu();
      return true;
    }
    
    protected boolean setFrame(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      boolean bool = super.setFrame(paramInt1, paramInt2, paramInt3, paramInt4);
      Drawable localDrawable1 = getDrawable();
      Drawable localDrawable2 = getBackground();
      if ((localDrawable1 != null) && (localDrawable2 != null))
      {
        int i = getWidth();
        paramInt2 = getHeight();
        paramInt1 = Math.max(i, paramInt2) / 2;
        int j = getPaddingLeft();
        int k = getPaddingRight();
        paramInt4 = getPaddingTop();
        paramInt3 = getPaddingBottom();
        k = (i + (j - k)) / 2;
        paramInt2 = (paramInt2 + (paramInt4 - paramInt3)) / 2;
        localDrawable2.setHotspotBounds(k - paramInt1, paramInt2 - paramInt1, k + paramInt1, paramInt2 + paramInt1);
      }
      return bool;
    }
  }
  
  private class OverflowPopup
    extends MenuPopupHelper
  {
    public OverflowPopup(Context paramContext, MenuBuilder paramMenuBuilder, View paramView, boolean paramBoolean)
    {
      super(paramMenuBuilder, paramView, paramBoolean, 16843844);
      setGravity(8388613);
      setPresenterCallback(mPopupPresenterCallback);
    }
    
    protected void onDismiss()
    {
      if (mMenu != null) {
        mMenu.close();
      }
      ActionMenuPresenter.access$1102(ActionMenuPresenter.this, null);
      super.onDismiss();
    }
  }
  
  private class PopupPresenterCallback
    implements MenuPresenter.Callback
  {
    private PopupPresenterCallback() {}
    
    public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean)
    {
      if ((paramMenuBuilder instanceof SubMenuBuilder)) {
        paramMenuBuilder.getRootMenu().close(false);
      }
      MenuPresenter.Callback localCallback = getCallback();
      if (localCallback != null) {
        localCallback.onCloseMenu(paramMenuBuilder, paramBoolean);
      }
    }
    
    public boolean onOpenSubMenu(MenuBuilder paramMenuBuilder)
    {
      boolean bool = false;
      if (paramMenuBuilder == null) {
        return false;
      }
      mOpenSubMenuId = ((SubMenuBuilder)paramMenuBuilder).getItem().getItemId();
      MenuPresenter.Callback localCallback = getCallback();
      if (localCallback != null) {
        bool = localCallback.onOpenSubMenu(paramMenuBuilder);
      }
      return bool;
    }
  }
  
  private static class SavedState
    implements Parcelable
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public ActionMenuPresenter.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ActionMenuPresenter.SavedState(paramAnonymousParcel);
      }
      
      public ActionMenuPresenter.SavedState[] newArray(int paramAnonymousInt)
      {
        return new ActionMenuPresenter.SavedState[paramAnonymousInt];
      }
    };
    public int openSubMenuId;
    
    SavedState() {}
    
    SavedState(Parcel paramParcel)
    {
      openSubMenuId = paramParcel.readInt();
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(openSubMenuId);
    }
  }
}
