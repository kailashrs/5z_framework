package com.android.internal.view.menu;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ActionProvider;
import android.view.ActionProvider.VisibilityListener;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewDebug.CapturedViewProperty;
import android.widget.LinearLayout;

public final class MenuItemImpl
  implements MenuItem
{
  private static final int CHECKABLE = 1;
  private static final int CHECKED = 2;
  private static final int ENABLED = 16;
  private static final int EXCLUSIVE = 4;
  private static final int HIDDEN = 8;
  private static final int IS_ACTION = 32;
  static final int NO_ICON = 0;
  private static final int SHOW_AS_ACTION_MASK = 3;
  private static final String TAG = "MenuItemImpl";
  private ActionProvider mActionProvider;
  private View mActionView;
  private final int mCategoryOrder;
  private MenuItem.OnMenuItemClickListener mClickListener;
  private CharSequence mContentDescription;
  private int mFlags = 16;
  private final int mGroup;
  private boolean mHasIconTint = false;
  private boolean mHasIconTintMode = false;
  private Drawable mIconDrawable;
  private int mIconResId = 0;
  private ColorStateList mIconTintList = null;
  private PorterDuff.Mode mIconTintMode = null;
  private final int mId;
  private Intent mIntent;
  private boolean mIsActionViewExpanded = false;
  private Runnable mItemCallback;
  private MenuBuilder mMenu;
  private ContextMenu.ContextMenuInfo mMenuInfo;
  private boolean mNeedToApplyIconTint = false;
  private MenuItem.OnActionExpandListener mOnActionExpandListener;
  private final int mOrdering;
  private char mShortcutAlphabeticChar;
  private int mShortcutAlphabeticModifiers = 4096;
  private char mShortcutNumericChar;
  private int mShortcutNumericModifiers = 4096;
  private int mShowAsAction = 0;
  private SubMenuBuilder mSubMenu;
  private CharSequence mTitle;
  private CharSequence mTitleCondensed;
  private CharSequence mTooltipText;
  
  MenuItemImpl(MenuBuilder paramMenuBuilder, int paramInt1, int paramInt2, int paramInt3, int paramInt4, CharSequence paramCharSequence, int paramInt5)
  {
    mMenu = paramMenuBuilder;
    mId = paramInt2;
    mGroup = paramInt1;
    mCategoryOrder = paramInt3;
    mOrdering = paramInt4;
    mTitle = paramCharSequence;
    mShowAsAction = paramInt5;
  }
  
  private static void appendModifier(StringBuilder paramStringBuilder, int paramInt1, int paramInt2, String paramString)
  {
    if ((paramInt1 & paramInt2) == paramInt2) {
      paramStringBuilder.append(paramString);
    }
  }
  
  private Drawable applyIconTintIfNecessary(Drawable paramDrawable)
  {
    Drawable localDrawable = paramDrawable;
    if (paramDrawable != null)
    {
      localDrawable = paramDrawable;
      if (mNeedToApplyIconTint) {
        if (!mHasIconTint)
        {
          localDrawable = paramDrawable;
          if (!mHasIconTintMode) {}
        }
        else
        {
          localDrawable = paramDrawable.mutate();
          if (mHasIconTint) {
            localDrawable.setTintList(mIconTintList);
          }
          if (mHasIconTintMode) {
            localDrawable.setTintMode(mIconTintMode);
          }
          mNeedToApplyIconTint = false;
        }
      }
    }
    return localDrawable;
  }
  
  public void actionFormatChanged()
  {
    mMenu.onItemActionRequestChanged(this);
  }
  
  public boolean collapseActionView()
  {
    if ((mShowAsAction & 0x8) == 0) {
      return false;
    }
    if (mActionView == null) {
      return true;
    }
    if ((mOnActionExpandListener != null) && (!mOnActionExpandListener.onMenuItemActionCollapse(this))) {
      return false;
    }
    return mMenu.collapseItemActionView(this);
  }
  
  public boolean expandActionView()
  {
    if (!hasCollapsibleActionView()) {
      return false;
    }
    if ((mOnActionExpandListener != null) && (!mOnActionExpandListener.onMenuItemActionExpand(this))) {
      return false;
    }
    return mMenu.expandItemActionView(this);
  }
  
  public ActionProvider getActionProvider()
  {
    return mActionProvider;
  }
  
  public View getActionView()
  {
    if (mActionView != null) {
      return mActionView;
    }
    if (mActionProvider != null)
    {
      mActionView = mActionProvider.onCreateActionView(this);
      return mActionView;
    }
    return null;
  }
  
  public int getAlphabeticModifiers()
  {
    return mShortcutAlphabeticModifiers;
  }
  
  public char getAlphabeticShortcut()
  {
    return mShortcutAlphabeticChar;
  }
  
  Runnable getCallback()
  {
    return mItemCallback;
  }
  
  public CharSequence getContentDescription()
  {
    return mContentDescription;
  }
  
  public int getGroupId()
  {
    return mGroup;
  }
  
  public Drawable getIcon()
  {
    if (mIconDrawable != null) {
      return applyIconTintIfNecessary(mIconDrawable);
    }
    if (mIconResId != 0)
    {
      Drawable localDrawable = mMenu.getContext().getDrawable(mIconResId);
      mIconResId = 0;
      mIconDrawable = localDrawable;
      return applyIconTintIfNecessary(localDrawable);
    }
    return null;
  }
  
  public ColorStateList getIconTintList()
  {
    return mIconTintList;
  }
  
  public PorterDuff.Mode getIconTintMode()
  {
    return mIconTintMode;
  }
  
  public Intent getIntent()
  {
    return mIntent;
  }
  
  @ViewDebug.CapturedViewProperty
  public int getItemId()
  {
    return mId;
  }
  
  public ContextMenu.ContextMenuInfo getMenuInfo()
  {
    return mMenuInfo;
  }
  
  public int getNumericModifiers()
  {
    return mShortcutNumericModifiers;
  }
  
  public char getNumericShortcut()
  {
    return mShortcutNumericChar;
  }
  
  public int getOrder()
  {
    return mCategoryOrder;
  }
  
  public int getOrdering()
  {
    return mOrdering;
  }
  
  char getShortcut()
  {
    char c1;
    char c2;
    if (mMenu.isQwertyMode())
    {
      c1 = mShortcutAlphabeticChar;
      c2 = c1;
    }
    else
    {
      c1 = mShortcutNumericChar;
      c2 = c1;
    }
    return c2;
  }
  
  String getShortcutLabel()
  {
    char c = getShortcut();
    if (c == 0) {
      return "";
    }
    Resources localResources = mMenu.getContext().getResources();
    StringBuilder localStringBuilder = new StringBuilder();
    if (ViewConfiguration.get(mMenu.getContext()).hasPermanentMenuKey()) {
      localStringBuilder.append(localResources.getString(17040857));
    }
    int i;
    if (mMenu.isQwertyMode()) {
      i = mShortcutAlphabeticModifiers;
    } else {
      i = mShortcutNumericModifiers;
    }
    appendModifier(localStringBuilder, i, 65536, localResources.getString(17040431));
    appendModifier(localStringBuilder, i, 4096, localResources.getString(17040427));
    appendModifier(localStringBuilder, i, 2, localResources.getString(17040426));
    appendModifier(localStringBuilder, i, 1, localResources.getString(17040432));
    appendModifier(localStringBuilder, i, 4, localResources.getString(17040434));
    appendModifier(localStringBuilder, i, 8, localResources.getString(17040430));
    if (c != '\b')
    {
      if (c != '\n')
      {
        if (c != ' ') {
          localStringBuilder.append(c);
        } else {
          localStringBuilder.append(localResources.getString(17040433));
        }
      }
      else {
        localStringBuilder.append(localResources.getString(17040429));
      }
    }
    else {
      localStringBuilder.append(localResources.getString(17040428));
    }
    return localStringBuilder.toString();
  }
  
  public SubMenu getSubMenu()
  {
    return mSubMenu;
  }
  
  @ViewDebug.CapturedViewProperty
  public CharSequence getTitle()
  {
    return mTitle;
  }
  
  public CharSequence getTitleCondensed()
  {
    CharSequence localCharSequence;
    if (mTitleCondensed != null) {
      localCharSequence = mTitleCondensed;
    } else {
      localCharSequence = mTitle;
    }
    return localCharSequence;
  }
  
  CharSequence getTitleForItemView(MenuView.ItemView paramItemView)
  {
    if ((paramItemView != null) && (paramItemView.prefersCondensedTitle())) {
      paramItemView = getTitleCondensed();
    } else {
      paramItemView = getTitle();
    }
    return paramItemView;
  }
  
  public CharSequence getTooltipText()
  {
    return mTooltipText;
  }
  
  public boolean hasCollapsibleActionView()
  {
    int i = mShowAsAction;
    boolean bool = false;
    if ((i & 0x8) != 0)
    {
      if ((mActionView == null) && (mActionProvider != null)) {
        mActionView = mActionProvider.onCreateActionView(this);
      }
      if (mActionView != null) {
        bool = true;
      }
      return bool;
    }
    return false;
  }
  
  public boolean hasSubMenu()
  {
    boolean bool;
    if (mSubMenu != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean invoke()
  {
    if ((mClickListener != null) && (mClickListener.onMenuItemClick(this))) {
      return true;
    }
    if (mMenu.dispatchMenuItemSelected(mMenu, this)) {
      return true;
    }
    if (mItemCallback != null)
    {
      mItemCallback.run();
      return true;
    }
    if (mIntent != null) {
      try
      {
        mMenu.getContext().startActivity(mIntent);
        return true;
      }
      catch (ActivityNotFoundException localActivityNotFoundException)
      {
        Log.e("MenuItemImpl", "Can't find activity to handle intent; ignoring", localActivityNotFoundException);
      }
    }
    return (mActionProvider != null) && (mActionProvider.onPerformDefaultAction());
  }
  
  public boolean isActionButton()
  {
    boolean bool;
    if ((mFlags & 0x20) == 32) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isActionViewExpanded()
  {
    return mIsActionViewExpanded;
  }
  
  public boolean isCheckable()
  {
    int i = mFlags;
    boolean bool = true;
    if ((i & 0x1) != 1) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isChecked()
  {
    boolean bool;
    if ((mFlags & 0x2) == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isEnabled()
  {
    boolean bool;
    if ((mFlags & 0x10) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isExclusiveCheckable()
  {
    boolean bool;
    if ((mFlags & 0x4) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isVisible()
  {
    ActionProvider localActionProvider = mActionProvider;
    boolean bool1 = false;
    boolean bool2 = false;
    if ((localActionProvider != null) && (mActionProvider.overridesItemVisibility()))
    {
      bool1 = bool2;
      if ((mFlags & 0x8) == 0)
      {
        bool1 = bool2;
        if (mActionProvider.isVisible()) {
          bool1 = true;
        }
      }
      return bool1;
    }
    if ((mFlags & 0x8) == 0) {
      bool1 = true;
    }
    return bool1;
  }
  
  public boolean requestsActionButton()
  {
    int i = mShowAsAction;
    boolean bool = true;
    if ((i & 0x1) != 1) {
      bool = false;
    }
    return bool;
  }
  
  public boolean requiresActionButton()
  {
    boolean bool;
    if ((mShowAsAction & 0x2) == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean requiresOverflow()
  {
    boolean bool;
    if ((!requiresActionButton()) && (!requestsActionButton())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public MenuItem setActionProvider(ActionProvider paramActionProvider)
  {
    if (mActionProvider != null) {
      mActionProvider.reset();
    }
    mActionView = null;
    mActionProvider = paramActionProvider;
    mMenu.onItemsChanged(true);
    if (mActionProvider != null) {
      mActionProvider.setVisibilityListener(new ActionProvider.VisibilityListener()
      {
        public void onActionProviderVisibilityChanged(boolean paramAnonymousBoolean)
        {
          mMenu.onItemVisibleChanged(MenuItemImpl.this);
        }
      });
    }
    return this;
  }
  
  public MenuItem setActionView(int paramInt)
  {
    Context localContext = mMenu.getContext();
    setActionView(LayoutInflater.from(localContext).inflate(paramInt, new LinearLayout(localContext), false));
    return this;
  }
  
  public MenuItem setActionView(View paramView)
  {
    mActionView = paramView;
    mActionProvider = null;
    if ((paramView != null) && (paramView.getId() == -1) && (mId > 0)) {
      paramView.setId(mId);
    }
    mMenu.onItemActionRequestChanged(this);
    return this;
  }
  
  public void setActionViewExpanded(boolean paramBoolean)
  {
    mIsActionViewExpanded = paramBoolean;
    mMenu.onItemsChanged(false);
  }
  
  public MenuItem setAlphabeticShortcut(char paramChar)
  {
    if (mShortcutAlphabeticChar == paramChar) {
      return this;
    }
    mShortcutAlphabeticChar = Character.toLowerCase(paramChar);
    mMenu.onItemsChanged(false);
    return this;
  }
  
  public MenuItem setAlphabeticShortcut(char paramChar, int paramInt)
  {
    if ((mShortcutAlphabeticChar == paramChar) && (mShortcutAlphabeticModifiers == paramInt)) {
      return this;
    }
    mShortcutAlphabeticChar = Character.toLowerCase(paramChar);
    mShortcutAlphabeticModifiers = KeyEvent.normalizeMetaState(paramInt);
    mMenu.onItemsChanged(false);
    return this;
  }
  
  public MenuItem setCallback(Runnable paramRunnable)
  {
    mItemCallback = paramRunnable;
    return this;
  }
  
  public MenuItem setCheckable(boolean paramBoolean)
  {
    int i = mFlags;
    mFlags = (mFlags & 0xFFFFFFFE | paramBoolean);
    if (i != mFlags) {
      mMenu.onItemsChanged(false);
    }
    return this;
  }
  
  public MenuItem setChecked(boolean paramBoolean)
  {
    if ((mFlags & 0x4) != 0) {
      mMenu.setExclusiveItemChecked(this);
    } else {
      setCheckedInt(paramBoolean);
    }
    return this;
  }
  
  void setCheckedInt(boolean paramBoolean)
  {
    int i = mFlags;
    int j = mFlags;
    int k;
    if (paramBoolean) {
      k = 2;
    } else {
      k = 0;
    }
    mFlags = (j & 0xFFFFFFFD | k);
    if (i != mFlags) {
      mMenu.onItemsChanged(false);
    }
  }
  
  public MenuItem setContentDescription(CharSequence paramCharSequence)
  {
    mContentDescription = paramCharSequence;
    mMenu.onItemsChanged(false);
    return this;
  }
  
  public MenuItem setEnabled(boolean paramBoolean)
  {
    if (paramBoolean) {
      mFlags |= 0x10;
    } else {
      mFlags &= 0xFFFFFFEF;
    }
    mMenu.onItemsChanged(false);
    return this;
  }
  
  public void setExclusiveCheckable(boolean paramBoolean)
  {
    int i = mFlags;
    int j;
    if (paramBoolean) {
      j = 4;
    } else {
      j = 0;
    }
    mFlags = (i & 0xFFFFFFFB | j);
  }
  
  public MenuItem setIcon(int paramInt)
  {
    mIconDrawable = null;
    mIconResId = paramInt;
    mNeedToApplyIconTint = true;
    mMenu.onItemsChanged(false);
    return this;
  }
  
  public MenuItem setIcon(Drawable paramDrawable)
  {
    mIconResId = 0;
    mIconDrawable = paramDrawable;
    mNeedToApplyIconTint = true;
    mMenu.onItemsChanged(false);
    return this;
  }
  
  public MenuItem setIconTintList(ColorStateList paramColorStateList)
  {
    mIconTintList = paramColorStateList;
    mHasIconTint = true;
    mNeedToApplyIconTint = true;
    mMenu.onItemsChanged(false);
    return this;
  }
  
  public MenuItem setIconTintMode(PorterDuff.Mode paramMode)
  {
    mIconTintMode = paramMode;
    mHasIconTintMode = true;
    mNeedToApplyIconTint = true;
    mMenu.onItemsChanged(false);
    return this;
  }
  
  public MenuItem setIntent(Intent paramIntent)
  {
    mIntent = paramIntent;
    return this;
  }
  
  public void setIsActionButton(boolean paramBoolean)
  {
    if (paramBoolean) {
      mFlags |= 0x20;
    } else {
      mFlags &= 0xFFFFFFDF;
    }
  }
  
  void setMenuInfo(ContextMenu.ContextMenuInfo paramContextMenuInfo)
  {
    mMenuInfo = paramContextMenuInfo;
  }
  
  public MenuItem setNumericShortcut(char paramChar)
  {
    if (mShortcutNumericChar == paramChar) {
      return this;
    }
    mShortcutNumericChar = ((char)paramChar);
    mMenu.onItemsChanged(false);
    return this;
  }
  
  public MenuItem setNumericShortcut(char paramChar, int paramInt)
  {
    if ((mShortcutNumericChar == paramChar) && (mShortcutNumericModifiers == paramInt)) {
      return this;
    }
    mShortcutNumericChar = ((char)paramChar);
    mShortcutNumericModifiers = KeyEvent.normalizeMetaState(paramInt);
    mMenu.onItemsChanged(false);
    return this;
  }
  
  public MenuItem setOnActionExpandListener(MenuItem.OnActionExpandListener paramOnActionExpandListener)
  {
    mOnActionExpandListener = paramOnActionExpandListener;
    return this;
  }
  
  public MenuItem setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener paramOnMenuItemClickListener)
  {
    mClickListener = paramOnMenuItemClickListener;
    return this;
  }
  
  public MenuItem setShortcut(char paramChar1, char paramChar2)
  {
    mShortcutNumericChar = ((char)paramChar1);
    mShortcutAlphabeticChar = Character.toLowerCase(paramChar2);
    mMenu.onItemsChanged(false);
    return this;
  }
  
  public MenuItem setShortcut(char paramChar1, char paramChar2, int paramInt1, int paramInt2)
  {
    mShortcutNumericChar = ((char)paramChar1);
    mShortcutNumericModifiers = KeyEvent.normalizeMetaState(paramInt1);
    mShortcutAlphabeticChar = Character.toLowerCase(paramChar2);
    mShortcutAlphabeticModifiers = KeyEvent.normalizeMetaState(paramInt2);
    mMenu.onItemsChanged(false);
    return this;
  }
  
  public void setShowAsAction(int paramInt)
  {
    switch (paramInt & 0x3)
    {
    default: 
      throw new IllegalArgumentException("SHOW_AS_ACTION_ALWAYS, SHOW_AS_ACTION_IF_ROOM, and SHOW_AS_ACTION_NEVER are mutually exclusive.");
    }
    mShowAsAction = paramInt;
    mMenu.onItemActionRequestChanged(this);
  }
  
  public MenuItem setShowAsActionFlags(int paramInt)
  {
    setShowAsAction(paramInt);
    return this;
  }
  
  void setSubMenu(SubMenuBuilder paramSubMenuBuilder)
  {
    mSubMenu = paramSubMenuBuilder;
    paramSubMenuBuilder.setHeaderTitle(getTitle());
  }
  
  public MenuItem setTitle(int paramInt)
  {
    return setTitle(mMenu.getContext().getString(paramInt));
  }
  
  public MenuItem setTitle(CharSequence paramCharSequence)
  {
    mTitle = paramCharSequence;
    mMenu.onItemsChanged(false);
    if (mSubMenu != null) {
      mSubMenu.setHeaderTitle(paramCharSequence);
    }
    return this;
  }
  
  public MenuItem setTitleCondensed(CharSequence paramCharSequence)
  {
    mTitleCondensed = paramCharSequence;
    if (paramCharSequence == null) {
      paramCharSequence = mTitle;
    }
    mMenu.onItemsChanged(false);
    return this;
  }
  
  public MenuItem setTooltipText(CharSequence paramCharSequence)
  {
    mTooltipText = paramCharSequence;
    mMenu.onItemsChanged(false);
    return this;
  }
  
  public MenuItem setVisible(boolean paramBoolean)
  {
    if (setVisibleInt(paramBoolean)) {
      mMenu.onItemVisibleChanged(this);
    }
    return this;
  }
  
  boolean setVisibleInt(boolean paramBoolean)
  {
    int i = mFlags;
    int j = mFlags;
    boolean bool = false;
    int k;
    if (paramBoolean) {
      k = 0;
    } else {
      k = 8;
    }
    mFlags = (j & 0xFFFFFFF7 | k);
    paramBoolean = bool;
    if (i != mFlags) {
      paramBoolean = true;
    }
    return paramBoolean;
  }
  
  public boolean shouldShowIcon()
  {
    return mMenu.getOptionalIconsVisible();
  }
  
  boolean shouldShowShortcut()
  {
    boolean bool;
    if ((mMenu.isShortcutsVisible()) && (getShortcut() != 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean showsTextAsAction()
  {
    boolean bool;
    if ((mShowAsAction & 0x4) == 4) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    String str;
    if (mTitle != null) {
      str = mTitle.toString();
    } else {
      str = null;
    }
    return str;
  }
}
