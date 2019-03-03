package com.android.internal.view.menu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.ActionProvider;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyCharacterMap.KeyData;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewConfiguration;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MenuBuilder
  implements Menu
{
  private static final String ACTION_VIEW_STATES_KEY = "android:menu:actionviewstates";
  private static final String EXPANDED_ACTION_VIEW_ID = "android:menu:expandedactionview";
  private static final String PRESENTER_KEY = "android:menu:presenters";
  private static final String TAG = "MenuBuilder";
  private static final int[] sCategoryToOrder = { 1, 4, 5, 3, 2, 0 };
  private ArrayList<MenuItemImpl> mActionItems;
  private Callback mCallback;
  private final Context mContext;
  private ContextMenu.ContextMenuInfo mCurrentMenuInfo;
  private int mDefaultShowAsAction = 0;
  private MenuItemImpl mExpandedItem;
  private SparseArray<Parcelable> mFrozenViewStates;
  private boolean mGroupDividerEnabled = false;
  Drawable mHeaderIcon;
  CharSequence mHeaderTitle;
  View mHeaderView;
  private boolean mIsActionItemsStale;
  private boolean mIsClosing = false;
  private boolean mIsVisibleItemsStale;
  private ArrayList<MenuItemImpl> mItems;
  private boolean mItemsChangedWhileDispatchPrevented = false;
  private ArrayList<MenuItemImpl> mNonActionItems;
  private boolean mOptionalIconsVisible = false;
  private CopyOnWriteArrayList<WeakReference<MenuPresenter>> mPresenters = new CopyOnWriteArrayList();
  private boolean mPreventDispatchingItemsChanged = false;
  private boolean mQwertyMode;
  private final Resources mResources;
  private boolean mShortcutsVisible;
  private ArrayList<MenuItemImpl> mTempShortcutItemList = new ArrayList();
  private ArrayList<MenuItemImpl> mVisibleItems;
  
  public MenuBuilder(Context paramContext)
  {
    mContext = paramContext;
    mResources = paramContext.getResources();
    mItems = new ArrayList();
    mVisibleItems = new ArrayList();
    mIsVisibleItemsStale = true;
    mActionItems = new ArrayList();
    mNonActionItems = new ArrayList();
    mIsActionItemsStale = true;
    setShortcutsVisibleInner(true);
  }
  
  private MenuItem addInternal(int paramInt1, int paramInt2, int paramInt3, CharSequence paramCharSequence)
  {
    int i = getOrdering(paramInt3);
    paramCharSequence = createNewMenuItem(paramInt1, paramInt2, paramInt3, i, paramCharSequence, mDefaultShowAsAction);
    if (mCurrentMenuInfo != null) {
      paramCharSequence.setMenuInfo(mCurrentMenuInfo);
    }
    mItems.add(findInsertIndex(mItems, i), paramCharSequence);
    onItemsChanged(true);
    return paramCharSequence;
  }
  
  private MenuItemImpl createNewMenuItem(int paramInt1, int paramInt2, int paramInt3, int paramInt4, CharSequence paramCharSequence, int paramInt5)
  {
    return new MenuItemImpl(this, paramInt1, paramInt2, paramInt3, paramInt4, paramCharSequence, paramInt5);
  }
  
  private void dispatchPresenterUpdate(boolean paramBoolean)
  {
    if (mPresenters.isEmpty()) {
      return;
    }
    stopDispatchingItemsChanged();
    Iterator localIterator = mPresenters.iterator();
    while (localIterator.hasNext())
    {
      WeakReference localWeakReference = (WeakReference)localIterator.next();
      MenuPresenter localMenuPresenter = (MenuPresenter)localWeakReference.get();
      if (localMenuPresenter == null) {
        mPresenters.remove(localWeakReference);
      } else {
        localMenuPresenter.updateMenuView(paramBoolean);
      }
    }
    startDispatchingItemsChanged();
  }
  
  private void dispatchRestoreInstanceState(Bundle paramBundle)
  {
    SparseArray localSparseArray = paramBundle.getSparseParcelableArray("android:menu:presenters");
    if ((localSparseArray != null) && (!mPresenters.isEmpty()))
    {
      Iterator localIterator = mPresenters.iterator();
      while (localIterator.hasNext())
      {
        Object localObject = (WeakReference)localIterator.next();
        paramBundle = (MenuPresenter)((WeakReference)localObject).get();
        if (paramBundle == null)
        {
          mPresenters.remove(localObject);
        }
        else
        {
          int i = paramBundle.getId();
          if (i > 0)
          {
            localObject = (Parcelable)localSparseArray.get(i);
            if (localObject != null) {
              paramBundle.onRestoreInstanceState((Parcelable)localObject);
            }
          }
        }
      }
      return;
    }
  }
  
  private void dispatchSaveInstanceState(Bundle paramBundle)
  {
    if (mPresenters.isEmpty()) {
      return;
    }
    SparseArray localSparseArray = new SparseArray();
    Iterator localIterator = mPresenters.iterator();
    while (localIterator.hasNext())
    {
      Object localObject = (WeakReference)localIterator.next();
      MenuPresenter localMenuPresenter = (MenuPresenter)((WeakReference)localObject).get();
      if (localMenuPresenter == null)
      {
        mPresenters.remove(localObject);
      }
      else
      {
        int i = localMenuPresenter.getId();
        if (i > 0)
        {
          localObject = localMenuPresenter.onSaveInstanceState();
          if (localObject != null) {
            localSparseArray.put(i, localObject);
          }
        }
      }
    }
    paramBundle.putSparseParcelableArray("android:menu:presenters", localSparseArray);
  }
  
  private boolean dispatchSubMenuSelected(SubMenuBuilder paramSubMenuBuilder, MenuPresenter paramMenuPresenter)
  {
    if (mPresenters.isEmpty()) {
      return false;
    }
    boolean bool1 = false;
    if (paramMenuPresenter != null) {
      bool1 = paramMenuPresenter.onSubMenuSelected(paramSubMenuBuilder);
    }
    Iterator localIterator = mPresenters.iterator();
    while (localIterator.hasNext())
    {
      paramMenuPresenter = (WeakReference)localIterator.next();
      MenuPresenter localMenuPresenter = (MenuPresenter)paramMenuPresenter.get();
      boolean bool2;
      if (localMenuPresenter == null)
      {
        mPresenters.remove(paramMenuPresenter);
        bool2 = bool1;
      }
      else
      {
        bool2 = bool1;
        if (!bool1) {
          bool2 = localMenuPresenter.onSubMenuSelected(paramSubMenuBuilder);
        }
      }
      bool1 = bool2;
    }
    return bool1;
  }
  
  private static int findInsertIndex(ArrayList<MenuItemImpl> paramArrayList, int paramInt)
  {
    for (int i = paramArrayList.size() - 1; i >= 0; i--) {
      if (((MenuItemImpl)paramArrayList.get(i)).getOrdering() <= paramInt) {
        return i + 1;
      }
    }
    return 0;
  }
  
  private static int getOrdering(int paramInt)
  {
    int i = (0xFFFF0000 & paramInt) >> 16;
    if ((i >= 0) && (i < sCategoryToOrder.length)) {
      return sCategoryToOrder[i] << 16 | 0xFFFF & paramInt;
    }
    throw new IllegalArgumentException("order does not contain a valid category.");
  }
  
  private void removeItemAtInt(int paramInt, boolean paramBoolean)
  {
    if ((paramInt >= 0) && (paramInt < mItems.size()))
    {
      mItems.remove(paramInt);
      if (paramBoolean) {
        onItemsChanged(true);
      }
      return;
    }
  }
  
  private void setHeaderInternal(int paramInt1, CharSequence paramCharSequence, int paramInt2, Drawable paramDrawable, View paramView)
  {
    Resources localResources = getResources();
    if (paramView != null)
    {
      mHeaderView = paramView;
      mHeaderTitle = null;
      mHeaderIcon = null;
    }
    else
    {
      if (paramInt1 > 0) {
        mHeaderTitle = localResources.getText(paramInt1);
      } else if (paramCharSequence != null) {
        mHeaderTitle = paramCharSequence;
      }
      if (paramInt2 > 0) {
        mHeaderIcon = getContext().getDrawable(paramInt2);
      } else if (paramDrawable != null) {
        mHeaderIcon = paramDrawable;
      }
      mHeaderView = null;
    }
    onItemsChanged(false);
  }
  
  private void setShortcutsVisibleInner(boolean paramBoolean)
  {
    boolean bool = true;
    if ((paramBoolean) && (mResources.getConfiguration().keyboard != 1) && (ViewConfiguration.get(mContext).shouldShowMenuShortcutsWhenKeyboardPresent())) {
      paramBoolean = bool;
    } else {
      paramBoolean = false;
    }
    mShortcutsVisible = paramBoolean;
  }
  
  public MenuItem add(int paramInt)
  {
    return addInternal(0, 0, 0, mResources.getString(paramInt));
  }
  
  public MenuItem add(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    return addInternal(paramInt1, paramInt2, paramInt3, mResources.getString(paramInt4));
  }
  
  public MenuItem add(int paramInt1, int paramInt2, int paramInt3, CharSequence paramCharSequence)
  {
    return addInternal(paramInt1, paramInt2, paramInt3, paramCharSequence);
  }
  
  public MenuItem add(CharSequence paramCharSequence)
  {
    return addInternal(0, 0, 0, paramCharSequence);
  }
  
  public int addIntentOptions(int paramInt1, int paramInt2, int paramInt3, ComponentName paramComponentName, Intent[] paramArrayOfIntent, Intent paramIntent, int paramInt4, MenuItem[] paramArrayOfMenuItem)
  {
    PackageManager localPackageManager = mContext.getPackageManager();
    int i = 0;
    List localList = localPackageManager.queryIntentActivityOptions(paramComponentName, paramArrayOfIntent, paramIntent, 0);
    int j;
    if (localList != null) {
      j = localList.size();
    } else {
      j = 0;
    }
    if ((paramInt4 & 0x1) == 0) {
      removeGroup(paramInt1);
    }
    for (paramInt4 = i; paramInt4 < j; paramInt4++)
    {
      ResolveInfo localResolveInfo = (ResolveInfo)localList.get(paramInt4);
      if (specificIndex < 0) {
        paramComponentName = paramIntent;
      } else {
        paramComponentName = paramArrayOfIntent[specificIndex];
      }
      paramComponentName = new Intent(paramComponentName);
      paramComponentName.setComponent(new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name));
      paramComponentName = add(paramInt1, paramInt2, paramInt3, localResolveInfo.loadLabel(localPackageManager)).setIcon(localResolveInfo.loadIcon(localPackageManager)).setIntent(paramComponentName);
      if ((paramArrayOfMenuItem != null) && (specificIndex >= 0)) {
        paramArrayOfMenuItem[specificIndex] = paramComponentName;
      }
    }
    return j;
  }
  
  public void addMenuPresenter(MenuPresenter paramMenuPresenter)
  {
    addMenuPresenter(paramMenuPresenter, mContext);
  }
  
  public void addMenuPresenter(MenuPresenter paramMenuPresenter, Context paramContext)
  {
    mPresenters.add(new WeakReference(paramMenuPresenter));
    paramMenuPresenter.initForMenu(paramContext, this);
    mIsActionItemsStale = true;
  }
  
  public SubMenu addSubMenu(int paramInt)
  {
    return addSubMenu(0, 0, 0, mResources.getString(paramInt));
  }
  
  public SubMenu addSubMenu(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    return addSubMenu(paramInt1, paramInt2, paramInt3, mResources.getString(paramInt4));
  }
  
  public SubMenu addSubMenu(int paramInt1, int paramInt2, int paramInt3, CharSequence paramCharSequence)
  {
    paramCharSequence = (MenuItemImpl)addInternal(paramInt1, paramInt2, paramInt3, paramCharSequence);
    SubMenuBuilder localSubMenuBuilder = new SubMenuBuilder(mContext, this, paramCharSequence);
    paramCharSequence.setSubMenu(localSubMenuBuilder);
    return localSubMenuBuilder;
  }
  
  public SubMenu addSubMenu(CharSequence paramCharSequence)
  {
    return addSubMenu(0, 0, 0, paramCharSequence);
  }
  
  public void changeMenuMode()
  {
    if (mCallback != null) {
      mCallback.onMenuModeChange(this);
    }
  }
  
  public void clear()
  {
    if (mExpandedItem != null) {
      collapseItemActionView(mExpandedItem);
    }
    mItems.clear();
    onItemsChanged(true);
  }
  
  public void clearAll()
  {
    mPreventDispatchingItemsChanged = true;
    clear();
    clearHeader();
    mPresenters.clear();
    mPreventDispatchingItemsChanged = false;
    mItemsChangedWhileDispatchPrevented = false;
    onItemsChanged(true);
  }
  
  public void clearHeader()
  {
    mHeaderIcon = null;
    mHeaderTitle = null;
    mHeaderView = null;
    onItemsChanged(false);
  }
  
  public void close()
  {
    close(true);
  }
  
  public final void close(boolean paramBoolean)
  {
    if (mIsClosing) {
      return;
    }
    mIsClosing = true;
    Iterator localIterator = mPresenters.iterator();
    while (localIterator.hasNext())
    {
      WeakReference localWeakReference = (WeakReference)localIterator.next();
      MenuPresenter localMenuPresenter = (MenuPresenter)localWeakReference.get();
      if (localMenuPresenter == null) {
        mPresenters.remove(localWeakReference);
      } else {
        localMenuPresenter.onCloseMenu(this, paramBoolean);
      }
    }
    mIsClosing = false;
  }
  
  public boolean collapseItemActionView(MenuItemImpl paramMenuItemImpl)
  {
    if ((!mPresenters.isEmpty()) && (mExpandedItem == paramMenuItemImpl))
    {
      boolean bool1 = false;
      stopDispatchingItemsChanged();
      Iterator localIterator = mPresenters.iterator();
      boolean bool2;
      for (;;)
      {
        bool2 = bool1;
        if (!localIterator.hasNext()) {
          break;
        }
        WeakReference localWeakReference = (WeakReference)localIterator.next();
        MenuPresenter localMenuPresenter = (MenuPresenter)localWeakReference.get();
        if (localMenuPresenter == null)
        {
          mPresenters.remove(localWeakReference);
          bool2 = bool1;
        }
        else
        {
          boolean bool3 = localMenuPresenter.collapseItemActionView(this, paramMenuItemImpl);
          bool1 = bool3;
          bool2 = bool1;
          if (bool3)
          {
            bool2 = bool1;
            break;
          }
        }
        bool1 = bool2;
      }
      startDispatchingItemsChanged();
      if (bool2) {
        mExpandedItem = null;
      }
      return bool2;
    }
    return false;
  }
  
  boolean dispatchMenuItemSelected(MenuBuilder paramMenuBuilder, MenuItem paramMenuItem)
  {
    boolean bool;
    if ((mCallback != null) && (mCallback.onMenuItemSelected(paramMenuBuilder, paramMenuItem))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean expandItemActionView(MenuItemImpl paramMenuItemImpl)
  {
    if (mPresenters.isEmpty()) {
      return false;
    }
    boolean bool1 = false;
    stopDispatchingItemsChanged();
    Iterator localIterator = mPresenters.iterator();
    boolean bool2;
    for (;;)
    {
      bool2 = bool1;
      if (!localIterator.hasNext()) {
        break;
      }
      WeakReference localWeakReference = (WeakReference)localIterator.next();
      MenuPresenter localMenuPresenter = (MenuPresenter)localWeakReference.get();
      if (localMenuPresenter == null)
      {
        mPresenters.remove(localWeakReference);
        bool2 = bool1;
      }
      else
      {
        boolean bool3 = localMenuPresenter.expandItemActionView(this, paramMenuItemImpl);
        bool1 = bool3;
        bool2 = bool1;
        if (bool3)
        {
          bool2 = bool1;
          break;
        }
      }
      bool1 = bool2;
    }
    startDispatchingItemsChanged();
    if (bool2) {
      mExpandedItem = paramMenuItemImpl;
    }
    return bool2;
  }
  
  public int findGroupIndex(int paramInt)
  {
    return findGroupIndex(paramInt, 0);
  }
  
  public int findGroupIndex(int paramInt1, int paramInt2)
  {
    int i = size();
    int j = paramInt2;
    if (paramInt2 < 0) {}
    for (j = 0; j < i; j++) {
      if (((MenuItemImpl)mItems.get(j)).getGroupId() == paramInt1) {
        return j;
      }
    }
    return -1;
  }
  
  public MenuItem findItem(int paramInt)
  {
    int i = size();
    for (int j = 0; j < i; j++)
    {
      Object localObject = (MenuItemImpl)mItems.get(j);
      if (((MenuItemImpl)localObject).getItemId() == paramInt) {
        return localObject;
      }
      if (((MenuItemImpl)localObject).hasSubMenu())
      {
        localObject = ((MenuItemImpl)localObject).getSubMenu().findItem(paramInt);
        if (localObject != null) {
          return localObject;
        }
      }
    }
    return null;
  }
  
  public int findItemIndex(int paramInt)
  {
    int i = size();
    for (int j = 0; j < i; j++) {
      if (((MenuItemImpl)mItems.get(j)).getItemId() == paramInt) {
        return j;
      }
    }
    return -1;
  }
  
  MenuItemImpl findItemWithShortcutForKey(int paramInt, KeyEvent paramKeyEvent)
  {
    ArrayList localArrayList = mTempShortcutItemList;
    localArrayList.clear();
    findItemsWithShortcutForKey(localArrayList, paramInt, paramKeyEvent);
    if (localArrayList.isEmpty()) {
      return null;
    }
    int i = paramKeyEvent.getMetaState();
    KeyCharacterMap.KeyData localKeyData = new KeyCharacterMap.KeyData();
    paramKeyEvent.getKeyData(localKeyData);
    int j = localArrayList.size();
    if (j == 1) {
      return (MenuItemImpl)localArrayList.get(0);
    }
    boolean bool = isQwertyMode();
    for (int k = 0; k < j; k++)
    {
      paramKeyEvent = (MenuItemImpl)localArrayList.get(k);
      int m;
      if (bool) {
        m = paramKeyEvent.getAlphabeticShortcut();
      } else {
        m = paramKeyEvent.getNumericShortcut();
      }
      if (((m == meta[0]) && ((i & 0x2) == 0)) || ((m == meta[2]) && ((i & 0x2) != 0)) || ((bool) && (m == 8) && (paramInt == 67))) {
        return paramKeyEvent;
      }
    }
    return null;
  }
  
  void findItemsWithShortcutForKey(List<MenuItemImpl> paramList, int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool = isQwertyMode();
    int i = paramKeyEvent.getModifiers();
    KeyCharacterMap.KeyData localKeyData = new KeyCharacterMap.KeyData();
    if ((!paramKeyEvent.getKeyData(localKeyData)) && (paramInt != 67)) {
      return;
    }
    int j = mItems.size();
    for (int k = 0; k < j; k++)
    {
      MenuItemImpl localMenuItemImpl = (MenuItemImpl)mItems.get(k);
      if (localMenuItemImpl.hasSubMenu()) {
        ((MenuBuilder)localMenuItemImpl.getSubMenu()).findItemsWithShortcutForKey(paramList, paramInt, paramKeyEvent);
      }
      int m;
      if (bool) {
        m = localMenuItemImpl.getAlphabeticShortcut();
      } else {
        m = localMenuItemImpl.getNumericShortcut();
      }
      int n;
      if (bool) {
        n = localMenuItemImpl.getAlphabeticModifiers();
      } else {
        n = localMenuItemImpl.getNumericModifiers();
      }
      if ((i & 0x1100F) == (0x1100F & n)) {
        n = 1;
      } else {
        n = 0;
      }
      if ((n != 0) && (m != 0))
      {
        if ((m != meta[0]) && (m != meta[2])) {
          if ((!bool) || (m != 8) || (paramInt != 67)) {
            continue;
          }
        }
        if (localMenuItemImpl.isEnabled()) {
          paramList.add(localMenuItemImpl);
        }
      }
    }
  }
  
  public void flagActionItems()
  {
    ArrayList localArrayList = getVisibleItems();
    if (!mIsActionItemsStale) {
      return;
    }
    int i = 0;
    Iterator localIterator = mPresenters.iterator();
    Object localObject;
    while (localIterator.hasNext())
    {
      localObject = (WeakReference)localIterator.next();
      MenuPresenter localMenuPresenter = (MenuPresenter)((WeakReference)localObject).get();
      if (localMenuPresenter == null) {
        mPresenters.remove(localObject);
      } else {
        i |= localMenuPresenter.flagActionItems();
      }
    }
    if (i != 0)
    {
      mActionItems.clear();
      mNonActionItems.clear();
      int j = localArrayList.size();
      for (i = 0; i < j; i++)
      {
        localObject = (MenuItemImpl)localArrayList.get(i);
        if (((MenuItemImpl)localObject).isActionButton()) {
          mActionItems.add(localObject);
        } else {
          mNonActionItems.add(localObject);
        }
      }
    }
    else
    {
      mActionItems.clear();
      mNonActionItems.clear();
      mNonActionItems.addAll(getVisibleItems());
    }
    mIsActionItemsStale = false;
  }
  
  public ArrayList<MenuItemImpl> getActionItems()
  {
    flagActionItems();
    return mActionItems;
  }
  
  protected String getActionViewStatesKey()
  {
    return "android:menu:actionviewstates";
  }
  
  public Context getContext()
  {
    return mContext;
  }
  
  public MenuItemImpl getExpandedItem()
  {
    return mExpandedItem;
  }
  
  public Drawable getHeaderIcon()
  {
    return mHeaderIcon;
  }
  
  public CharSequence getHeaderTitle()
  {
    return mHeaderTitle;
  }
  
  public View getHeaderView()
  {
    return mHeaderView;
  }
  
  public MenuItem getItem(int paramInt)
  {
    return (MenuItem)mItems.get(paramInt);
  }
  
  public ArrayList<MenuItemImpl> getNonActionItems()
  {
    flagActionItems();
    return mNonActionItems;
  }
  
  boolean getOptionalIconsVisible()
  {
    return mOptionalIconsVisible;
  }
  
  Resources getResources()
  {
    return mResources;
  }
  
  public MenuBuilder getRootMenu()
  {
    return this;
  }
  
  public ArrayList<MenuItemImpl> getVisibleItems()
  {
    if (!mIsVisibleItemsStale) {
      return mVisibleItems;
    }
    mVisibleItems.clear();
    int i = mItems.size();
    for (int j = 0; j < i; j++)
    {
      MenuItemImpl localMenuItemImpl = (MenuItemImpl)mItems.get(j);
      if (localMenuItemImpl.isVisible()) {
        mVisibleItems.add(localMenuItemImpl);
      }
    }
    mIsVisibleItemsStale = false;
    mIsActionItemsStale = true;
    return mVisibleItems;
  }
  
  public boolean hasVisibleItems()
  {
    int i = size();
    for (int j = 0; j < i; j++) {
      if (((MenuItemImpl)mItems.get(j)).isVisible()) {
        return true;
      }
    }
    return false;
  }
  
  public boolean isGroupDividerEnabled()
  {
    return mGroupDividerEnabled;
  }
  
  boolean isQwertyMode()
  {
    return mQwertyMode;
  }
  
  public boolean isShortcutKey(int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool;
    if (findItemWithShortcutForKey(paramInt, paramKeyEvent) != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isShortcutsVisible()
  {
    return mShortcutsVisible;
  }
  
  void onItemActionRequestChanged(MenuItemImpl paramMenuItemImpl)
  {
    mIsActionItemsStale = true;
    onItemsChanged(true);
  }
  
  void onItemVisibleChanged(MenuItemImpl paramMenuItemImpl)
  {
    mIsVisibleItemsStale = true;
    onItemsChanged(true);
  }
  
  public void onItemsChanged(boolean paramBoolean)
  {
    if (!mPreventDispatchingItemsChanged)
    {
      if (paramBoolean)
      {
        mIsVisibleItemsStale = true;
        mIsActionItemsStale = true;
      }
      dispatchPresenterUpdate(paramBoolean);
    }
    else
    {
      mItemsChangedWhileDispatchPrevented = true;
    }
  }
  
  public boolean performIdentifierAction(int paramInt1, int paramInt2)
  {
    return performItemAction(findItem(paramInt1), paramInt2);
  }
  
  public boolean performItemAction(MenuItem paramMenuItem, int paramInt)
  {
    return performItemAction(paramMenuItem, null, paramInt);
  }
  
  public boolean performItemAction(MenuItem paramMenuItem, MenuPresenter paramMenuPresenter, int paramInt)
  {
    Object localObject = (MenuItemImpl)paramMenuItem;
    int i = 0;
    if ((localObject != null) && (((MenuItemImpl)localObject).isEnabled()))
    {
      boolean bool1 = ((MenuItemImpl)localObject).invoke();
      paramMenuItem = paramMenuItem.getActionProvider();
      int j = i;
      if (paramMenuItem != null)
      {
        j = i;
        if (paramMenuItem.hasSubMenu()) {
          j = 1;
        }
      }
      boolean bool2;
      if (((MenuItemImpl)localObject).hasCollapsibleActionView())
      {
        bool1 |= ((MenuItemImpl)localObject).expandActionView();
        bool2 = bool1;
        if (bool1)
        {
          close(true);
          bool2 = bool1;
        }
      }
      else if ((!((MenuItemImpl)localObject).hasSubMenu()) && (j == 0))
      {
        bool2 = bool1;
        if ((paramInt & 0x1) == 0)
        {
          close(true);
          bool2 = bool1;
        }
      }
      else
      {
        if (!((MenuItemImpl)localObject).hasSubMenu()) {
          ((MenuItemImpl)localObject).setSubMenu(new SubMenuBuilder(getContext(), this, (MenuItemImpl)localObject));
        }
        localObject = (SubMenuBuilder)((MenuItemImpl)localObject).getSubMenu();
        if (j != 0) {
          paramMenuItem.onPrepareSubMenu((SubMenu)localObject);
        }
        bool2 = bool1 | dispatchSubMenuSelected((SubMenuBuilder)localObject, paramMenuPresenter);
        if (!bool2) {
          close(true);
        }
      }
      return bool2;
    }
    return false;
  }
  
  public boolean performShortcut(int paramInt1, KeyEvent paramKeyEvent, int paramInt2)
  {
    paramKeyEvent = findItemWithShortcutForKey(paramInt1, paramKeyEvent);
    boolean bool = false;
    if (paramKeyEvent != null) {
      bool = performItemAction(paramKeyEvent, paramInt2);
    }
    if ((paramInt2 & 0x2) != 0) {
      close(true);
    }
    return bool;
  }
  
  public void removeGroup(int paramInt)
  {
    int i = findGroupIndex(paramInt);
    if (i >= 0)
    {
      int j = mItems.size();
      for (int k = 0; (k < j - i) && (((MenuItemImpl)mItems.get(i)).getGroupId() == paramInt); k++) {
        removeItemAtInt(i, false);
      }
      onItemsChanged(true);
    }
  }
  
  public void removeItem(int paramInt)
  {
    removeItemAtInt(findItemIndex(paramInt), true);
  }
  
  public void removeItemAt(int paramInt)
  {
    removeItemAtInt(paramInt, true);
  }
  
  public void removeMenuPresenter(MenuPresenter paramMenuPresenter)
  {
    Iterator localIterator = mPresenters.iterator();
    while (localIterator.hasNext())
    {
      WeakReference localWeakReference = (WeakReference)localIterator.next();
      MenuPresenter localMenuPresenter = (MenuPresenter)localWeakReference.get();
      if ((localMenuPresenter == null) || (localMenuPresenter == paramMenuPresenter)) {
        mPresenters.remove(localWeakReference);
      }
    }
  }
  
  public void restoreActionViewStates(Bundle paramBundle)
  {
    if (paramBundle == null) {
      return;
    }
    SparseArray localSparseArray = paramBundle.getSparseParcelableArray(getActionViewStatesKey());
    int i = size();
    for (int j = 0; j < i; j++)
    {
      MenuItem localMenuItem = getItem(j);
      View localView = localMenuItem.getActionView();
      if ((localView != null) && (localView.getId() != -1)) {
        localView.restoreHierarchyState(localSparseArray);
      }
      if (localMenuItem.hasSubMenu()) {
        ((SubMenuBuilder)localMenuItem.getSubMenu()).restoreActionViewStates(paramBundle);
      }
    }
    j = paramBundle.getInt("android:menu:expandedactionview");
    if (j > 0)
    {
      paramBundle = findItem(j);
      if (paramBundle != null) {
        paramBundle.expandActionView();
      }
    }
  }
  
  public void restorePresenterStates(Bundle paramBundle)
  {
    dispatchRestoreInstanceState(paramBundle);
  }
  
  public void saveActionViewStates(Bundle paramBundle)
  {
    Object localObject1 = null;
    int i = size();
    int j = 0;
    while (j < i)
    {
      MenuItem localMenuItem = getItem(j);
      View localView = localMenuItem.getActionView();
      Object localObject2 = localObject1;
      if (localView != null)
      {
        localObject2 = localObject1;
        if (localView.getId() != -1)
        {
          Object localObject3 = localObject1;
          if (localObject1 == null) {
            localObject3 = new SparseArray();
          }
          localView.saveHierarchyState((SparseArray)localObject3);
          localObject2 = localObject3;
          if (localMenuItem.isActionViewExpanded())
          {
            paramBundle.putInt("android:menu:expandedactionview", localMenuItem.getItemId());
            localObject2 = localObject3;
          }
        }
      }
      if (localMenuItem.hasSubMenu()) {
        ((SubMenuBuilder)localMenuItem.getSubMenu()).saveActionViewStates(paramBundle);
      }
      j++;
      localObject1 = localObject2;
    }
    if (localObject1 != null) {
      paramBundle.putSparseParcelableArray(getActionViewStatesKey(), localObject1);
    }
  }
  
  public void savePresenterStates(Bundle paramBundle)
  {
    dispatchSaveInstanceState(paramBundle);
  }
  
  public void setCallback(Callback paramCallback)
  {
    mCallback = paramCallback;
  }
  
  public void setCurrentMenuInfo(ContextMenu.ContextMenuInfo paramContextMenuInfo)
  {
    mCurrentMenuInfo = paramContextMenuInfo;
  }
  
  public MenuBuilder setDefaultShowAsAction(int paramInt)
  {
    mDefaultShowAsAction = paramInt;
    return this;
  }
  
  void setExclusiveItemChecked(MenuItem paramMenuItem)
  {
    int i = paramMenuItem.getGroupId();
    int j = mItems.size();
    for (int k = 0; k < j; k++)
    {
      MenuItemImpl localMenuItemImpl = (MenuItemImpl)mItems.get(k);
      if ((localMenuItemImpl.getGroupId() == i) && (localMenuItemImpl.isExclusiveCheckable()) && (localMenuItemImpl.isCheckable()))
      {
        boolean bool;
        if (localMenuItemImpl == paramMenuItem) {
          bool = true;
        } else {
          bool = false;
        }
        localMenuItemImpl.setCheckedInt(bool);
      }
    }
  }
  
  public void setGroupCheckable(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = mItems.size();
    for (int j = 0; j < i; j++)
    {
      MenuItemImpl localMenuItemImpl = (MenuItemImpl)mItems.get(j);
      if (localMenuItemImpl.getGroupId() == paramInt)
      {
        localMenuItemImpl.setExclusiveCheckable(paramBoolean2);
        localMenuItemImpl.setCheckable(paramBoolean1);
      }
    }
  }
  
  public void setGroupDividerEnabled(boolean paramBoolean)
  {
    mGroupDividerEnabled = paramBoolean;
  }
  
  public void setGroupEnabled(int paramInt, boolean paramBoolean)
  {
    int i = mItems.size();
    for (int j = 0; j < i; j++)
    {
      MenuItemImpl localMenuItemImpl = (MenuItemImpl)mItems.get(j);
      if (localMenuItemImpl.getGroupId() == paramInt) {
        localMenuItemImpl.setEnabled(paramBoolean);
      }
    }
  }
  
  public void setGroupVisible(int paramInt, boolean paramBoolean)
  {
    int i = mItems.size();
    int j = 0;
    int k = 0;
    while (k < i)
    {
      MenuItemImpl localMenuItemImpl = (MenuItemImpl)mItems.get(k);
      int m = j;
      if (localMenuItemImpl.getGroupId() == paramInt)
      {
        m = j;
        if (localMenuItemImpl.setVisibleInt(paramBoolean)) {
          m = 1;
        }
      }
      k++;
      j = m;
    }
    if (j != 0) {
      onItemsChanged(true);
    }
  }
  
  protected MenuBuilder setHeaderIconInt(int paramInt)
  {
    setHeaderInternal(0, null, paramInt, null, null);
    return this;
  }
  
  protected MenuBuilder setHeaderIconInt(Drawable paramDrawable)
  {
    setHeaderInternal(0, null, 0, paramDrawable, null);
    return this;
  }
  
  protected MenuBuilder setHeaderTitleInt(int paramInt)
  {
    setHeaderInternal(paramInt, null, 0, null, null);
    return this;
  }
  
  protected MenuBuilder setHeaderTitleInt(CharSequence paramCharSequence)
  {
    setHeaderInternal(0, paramCharSequence, 0, null, null);
    return this;
  }
  
  protected MenuBuilder setHeaderViewInt(View paramView)
  {
    setHeaderInternal(0, null, 0, null, paramView);
    return this;
  }
  
  void setOptionalIconsVisible(boolean paramBoolean)
  {
    mOptionalIconsVisible = paramBoolean;
  }
  
  public void setQwertyMode(boolean paramBoolean)
  {
    mQwertyMode = paramBoolean;
    onItemsChanged(false);
  }
  
  public void setShortcutsVisible(boolean paramBoolean)
  {
    if (mShortcutsVisible == paramBoolean) {
      return;
    }
    setShortcutsVisibleInner(paramBoolean);
    onItemsChanged(false);
  }
  
  public int size()
  {
    return mItems.size();
  }
  
  public void startDispatchingItemsChanged()
  {
    mPreventDispatchingItemsChanged = false;
    if (mItemsChangedWhileDispatchPrevented)
    {
      mItemsChangedWhileDispatchPrevented = false;
      onItemsChanged(true);
    }
  }
  
  public void stopDispatchingItemsChanged()
  {
    if (!mPreventDispatchingItemsChanged)
    {
      mPreventDispatchingItemsChanged = true;
      mItemsChangedWhileDispatchPrevented = false;
    }
  }
  
  public static abstract interface Callback
  {
    public abstract boolean onMenuItemSelected(MenuBuilder paramMenuBuilder, MenuItem paramMenuItem);
    
    public abstract void onMenuModeChange(MenuBuilder paramMenuBuilder);
  }
  
  public static abstract interface ItemInvoker
  {
    public abstract boolean invokeItem(MenuItemImpl paramMenuItemImpl);
  }
}
