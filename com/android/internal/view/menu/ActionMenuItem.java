package com.android.internal.view.menu;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;

public class ActionMenuItem
  implements MenuItem
{
  private static final int CHECKABLE = 1;
  private static final int CHECKED = 2;
  private static final int ENABLED = 16;
  private static final int EXCLUSIVE = 4;
  private static final int HIDDEN = 8;
  private static final int NO_ICON = 0;
  private final int mCategoryOrder;
  private MenuItem.OnMenuItemClickListener mClickListener;
  private CharSequence mContentDescription;
  private Context mContext;
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
  private final int mOrdering;
  private char mShortcutAlphabeticChar;
  private int mShortcutAlphabeticModifiers = 4096;
  private char mShortcutNumericChar;
  private int mShortcutNumericModifiers = 4096;
  private CharSequence mTitle;
  private CharSequence mTitleCondensed;
  private CharSequence mTooltipText;
  
  public ActionMenuItem(Context paramContext, int paramInt1, int paramInt2, int paramInt3, int paramInt4, CharSequence paramCharSequence)
  {
    mContext = paramContext;
    mId = paramInt2;
    mGroup = paramInt1;
    mCategoryOrder = paramInt3;
    mOrdering = paramInt4;
    mTitle = paramCharSequence;
  }
  
  private void applyIconTint()
  {
    if ((mIconDrawable != null) && ((mHasIconTint) || (mHasIconTintMode)))
    {
      mIconDrawable = mIconDrawable.mutate();
      if (mHasIconTint) {
        mIconDrawable.setTintList(mIconTintList);
      }
      if (mHasIconTintMode) {
        mIconDrawable.setTintMode(mIconTintMode);
      }
    }
  }
  
  public boolean collapseActionView()
  {
    return false;
  }
  
  public boolean expandActionView()
  {
    return false;
  }
  
  public ActionProvider getActionProvider()
  {
    return null;
  }
  
  public View getActionView()
  {
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
    return mIconDrawable;
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
  
  public int getItemId()
  {
    return mId;
  }
  
  public ContextMenu.ContextMenuInfo getMenuInfo()
  {
    return null;
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
    return mOrdering;
  }
  
  public SubMenu getSubMenu()
  {
    return null;
  }
  
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
  
  public CharSequence getTooltipText()
  {
    return mTooltipText;
  }
  
  public boolean hasSubMenu()
  {
    return false;
  }
  
  public boolean invoke()
  {
    if ((mClickListener != null) && (mClickListener.onMenuItemClick(this))) {
      return true;
    }
    if (mIntent != null)
    {
      mContext.startActivity(mIntent);
      return true;
    }
    return false;
  }
  
  public boolean isActionViewExpanded()
  {
    return false;
  }
  
  public boolean isCheckable()
  {
    int i = mFlags;
    boolean bool = true;
    if ((i & 0x1) == 0) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isChecked()
  {
    boolean bool;
    if ((mFlags & 0x2) != 0) {
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
  
  public boolean isVisible()
  {
    boolean bool;
    if ((mFlags & 0x8) == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public MenuItem setActionProvider(ActionProvider paramActionProvider)
  {
    throw new UnsupportedOperationException();
  }
  
  public MenuItem setActionView(int paramInt)
  {
    throw new UnsupportedOperationException();
  }
  
  public MenuItem setActionView(View paramView)
  {
    throw new UnsupportedOperationException();
  }
  
  public MenuItem setAlphabeticShortcut(char paramChar)
  {
    mShortcutAlphabeticChar = Character.toLowerCase(paramChar);
    return this;
  }
  
  public MenuItem setAlphabeticShortcut(char paramChar, int paramInt)
  {
    mShortcutAlphabeticChar = Character.toLowerCase(paramChar);
    mShortcutAlphabeticModifiers = KeyEvent.normalizeMetaState(paramInt);
    return this;
  }
  
  public MenuItem setCheckable(boolean paramBoolean)
  {
    mFlags = (mFlags & 0xFFFFFFFE | paramBoolean);
    return this;
  }
  
  public MenuItem setChecked(boolean paramBoolean)
  {
    int i = mFlags;
    int j;
    if (paramBoolean) {
      j = 2;
    } else {
      j = 0;
    }
    mFlags = (i & 0xFFFFFFFD | j);
    return this;
  }
  
  public MenuItem setContentDescription(CharSequence paramCharSequence)
  {
    mContentDescription = paramCharSequence;
    return this;
  }
  
  public MenuItem setEnabled(boolean paramBoolean)
  {
    int i = mFlags;
    int j;
    if (paramBoolean) {
      j = 16;
    } else {
      j = 0;
    }
    mFlags = (i & 0xFFFFFFEF | j);
    return this;
  }
  
  public ActionMenuItem setExclusiveCheckable(boolean paramBoolean)
  {
    int i = mFlags;
    int j;
    if (paramBoolean) {
      j = 4;
    } else {
      j = 0;
    }
    mFlags = (i & 0xFFFFFFFB | j);
    return this;
  }
  
  public MenuItem setIcon(int paramInt)
  {
    mIconResId = paramInt;
    mIconDrawable = mContext.getDrawable(paramInt);
    applyIconTint();
    return this;
  }
  
  public MenuItem setIcon(Drawable paramDrawable)
  {
    mIconDrawable = paramDrawable;
    mIconResId = 0;
    applyIconTint();
    return this;
  }
  
  public MenuItem setIconTintList(ColorStateList paramColorStateList)
  {
    mIconTintList = paramColorStateList;
    mHasIconTint = true;
    applyIconTint();
    return this;
  }
  
  public MenuItem setIconTintMode(PorterDuff.Mode paramMode)
  {
    mIconTintMode = paramMode;
    mHasIconTintMode = true;
    applyIconTint();
    return this;
  }
  
  public MenuItem setIntent(Intent paramIntent)
  {
    mIntent = paramIntent;
    return this;
  }
  
  public MenuItem setNumericShortcut(char paramChar)
  {
    mShortcutNumericChar = ((char)paramChar);
    return this;
  }
  
  public MenuItem setNumericShortcut(char paramChar, int paramInt)
  {
    mShortcutNumericChar = ((char)paramChar);
    mShortcutNumericModifiers = KeyEvent.normalizeMetaState(paramInt);
    return this;
  }
  
  public MenuItem setOnActionExpandListener(MenuItem.OnActionExpandListener paramOnActionExpandListener)
  {
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
    return this;
  }
  
  public MenuItem setShortcut(char paramChar1, char paramChar2, int paramInt1, int paramInt2)
  {
    mShortcutNumericChar = ((char)paramChar1);
    mShortcutNumericModifiers = KeyEvent.normalizeMetaState(paramInt1);
    mShortcutAlphabeticChar = Character.toLowerCase(paramChar2);
    mShortcutAlphabeticModifiers = KeyEvent.normalizeMetaState(paramInt2);
    return this;
  }
  
  public void setShowAsAction(int paramInt) {}
  
  public MenuItem setShowAsActionFlags(int paramInt)
  {
    setShowAsAction(paramInt);
    return this;
  }
  
  public MenuItem setTitle(int paramInt)
  {
    mTitle = mContext.getResources().getString(paramInt);
    return this;
  }
  
  public MenuItem setTitle(CharSequence paramCharSequence)
  {
    mTitle = paramCharSequence;
    return this;
  }
  
  public MenuItem setTitleCondensed(CharSequence paramCharSequence)
  {
    mTitleCondensed = paramCharSequence;
    return this;
  }
  
  public MenuItem setTooltipText(CharSequence paramCharSequence)
  {
    mTooltipText = paramCharSequence;
    return this;
  }
  
  public MenuItem setVisible(boolean paramBoolean)
  {
    int i = mFlags;
    int j = 8;
    if (paramBoolean) {
      j = 0;
    }
    mFlags = (i & 0x8 | j);
    return this;
  }
}
