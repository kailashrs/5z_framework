package com.android.internal.view.menu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import java.util.ArrayList;
import java.util.List;

public class ActionMenu
  implements Menu
{
  private Context mContext;
  private boolean mIsQwerty;
  private ArrayList<ActionMenuItem> mItems;
  
  public ActionMenu(Context paramContext)
  {
    mContext = paramContext;
    mItems = new ArrayList();
  }
  
  private int findItemIndex(int paramInt)
  {
    ArrayList localArrayList = mItems;
    int i = localArrayList.size();
    for (int j = 0; j < i; j++) {
      if (((ActionMenuItem)localArrayList.get(j)).getItemId() == paramInt) {
        return j;
      }
    }
    return -1;
  }
  
  private ActionMenuItem findItemWithShortcut(int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool = mIsQwerty;
    ArrayList localArrayList = mItems;
    int i = localArrayList.size();
    int j = paramKeyEvent.getModifiers();
    for (int k = 0; k < i; k++)
    {
      paramKeyEvent = (ActionMenuItem)localArrayList.get(k);
      int m;
      if (bool) {
        m = paramKeyEvent.getAlphabeticShortcut();
      } else {
        m = paramKeyEvent.getNumericShortcut();
      }
      int n;
      if (bool) {
        n = paramKeyEvent.getAlphabeticModifiers();
      } else {
        n = paramKeyEvent.getNumericModifiers();
      }
      if ((j & 0x1100F) == (0x1100F & n)) {
        n = 1;
      } else {
        n = 0;
      }
      if ((paramInt == m) && (n != 0)) {
        return paramKeyEvent;
      }
    }
    return null;
  }
  
  public MenuItem add(int paramInt)
  {
    return add(0, 0, 0, paramInt);
  }
  
  public MenuItem add(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    return add(paramInt1, paramInt2, paramInt3, mContext.getResources().getString(paramInt4));
  }
  
  public MenuItem add(int paramInt1, int paramInt2, int paramInt3, CharSequence paramCharSequence)
  {
    paramCharSequence = new ActionMenuItem(getContext(), paramInt1, paramInt2, 0, paramInt3, paramCharSequence);
    mItems.add(paramInt3, paramCharSequence);
    return paramCharSequence;
  }
  
  public MenuItem add(CharSequence paramCharSequence)
  {
    return add(0, 0, 0, paramCharSequence);
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
  
  public SubMenu addSubMenu(int paramInt)
  {
    return null;
  }
  
  public SubMenu addSubMenu(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    return null;
  }
  
  public SubMenu addSubMenu(int paramInt1, int paramInt2, int paramInt3, CharSequence paramCharSequence)
  {
    return null;
  }
  
  public SubMenu addSubMenu(CharSequence paramCharSequence)
  {
    return null;
  }
  
  public void clear()
  {
    mItems.clear();
  }
  
  public void close() {}
  
  public MenuItem findItem(int paramInt)
  {
    return (MenuItem)mItems.get(findItemIndex(paramInt));
  }
  
  public Context getContext()
  {
    return mContext;
  }
  
  public MenuItem getItem(int paramInt)
  {
    return (MenuItem)mItems.get(paramInt);
  }
  
  public boolean hasVisibleItems()
  {
    ArrayList localArrayList = mItems;
    int i = localArrayList.size();
    for (int j = 0; j < i; j++) {
      if (((ActionMenuItem)localArrayList.get(j)).isVisible()) {
        return true;
      }
    }
    return false;
  }
  
  public boolean isShortcutKey(int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool;
    if (findItemWithShortcut(paramInt, paramKeyEvent) != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean performIdentifierAction(int paramInt1, int paramInt2)
  {
    paramInt1 = findItemIndex(paramInt1);
    if (paramInt1 < 0) {
      return false;
    }
    return ((ActionMenuItem)mItems.get(paramInt1)).invoke();
  }
  
  public boolean performShortcut(int paramInt1, KeyEvent paramKeyEvent, int paramInt2)
  {
    paramKeyEvent = findItemWithShortcut(paramInt1, paramKeyEvent);
    if (paramKeyEvent == null) {
      return false;
    }
    return paramKeyEvent.invoke();
  }
  
  public void removeGroup(int paramInt)
  {
    ArrayList localArrayList = mItems;
    int i = localArrayList.size();
    int j = 0;
    while (j < i) {
      if (((ActionMenuItem)localArrayList.get(j)).getGroupId() == paramInt)
      {
        localArrayList.remove(j);
        i--;
      }
      else
      {
        j++;
      }
    }
  }
  
  public void removeItem(int paramInt)
  {
    mItems.remove(findItemIndex(paramInt));
  }
  
  public void setGroupCheckable(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    ArrayList localArrayList = mItems;
    int i = localArrayList.size();
    for (int j = 0; j < i; j++)
    {
      ActionMenuItem localActionMenuItem = (ActionMenuItem)localArrayList.get(j);
      if (localActionMenuItem.getGroupId() == paramInt)
      {
        localActionMenuItem.setCheckable(paramBoolean1);
        localActionMenuItem.setExclusiveCheckable(paramBoolean2);
      }
    }
  }
  
  public void setGroupEnabled(int paramInt, boolean paramBoolean)
  {
    ArrayList localArrayList = mItems;
    int i = localArrayList.size();
    for (int j = 0; j < i; j++)
    {
      ActionMenuItem localActionMenuItem = (ActionMenuItem)localArrayList.get(j);
      if (localActionMenuItem.getGroupId() == paramInt) {
        localActionMenuItem.setEnabled(paramBoolean);
      }
    }
  }
  
  public void setGroupVisible(int paramInt, boolean paramBoolean)
  {
    ArrayList localArrayList = mItems;
    int i = localArrayList.size();
    for (int j = 0; j < i; j++)
    {
      ActionMenuItem localActionMenuItem = (ActionMenuItem)localArrayList.get(j);
      if (localActionMenuItem.getGroupId() == paramInt) {
        localActionMenuItem.setVisible(paramBoolean);
      }
    }
  }
  
  public void setQwertyMode(boolean paramBoolean)
  {
    mIsQwerty = paramBoolean;
  }
  
  public int size()
  {
    return mItems.size();
  }
}
