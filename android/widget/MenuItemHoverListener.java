package android.widget;

import android.view.MenuItem;
import com.android.internal.view.menu.MenuBuilder;

public abstract interface MenuItemHoverListener
{
  public abstract void onItemHoverEnter(MenuBuilder paramMenuBuilder, MenuItem paramMenuItem);
  
  public abstract void onItemHoverExit(MenuBuilder paramMenuBuilder, MenuItem paramMenuItem);
}
