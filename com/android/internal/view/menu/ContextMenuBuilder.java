package com.android.internal.view.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.util.EventLog;
import android.view.ContextMenu;
import android.view.View;
import java.util.ArrayList;

public class ContextMenuBuilder
  extends MenuBuilder
  implements ContextMenu
{
  public ContextMenuBuilder(Context paramContext)
  {
    super(paramContext);
  }
  
  public ContextMenu setHeaderIcon(int paramInt)
  {
    return (ContextMenu)super.setHeaderIconInt(paramInt);
  }
  
  public ContextMenu setHeaderIcon(Drawable paramDrawable)
  {
    return (ContextMenu)super.setHeaderIconInt(paramDrawable);
  }
  
  public ContextMenu setHeaderTitle(int paramInt)
  {
    return (ContextMenu)super.setHeaderTitleInt(paramInt);
  }
  
  public ContextMenu setHeaderTitle(CharSequence paramCharSequence)
  {
    return (ContextMenu)super.setHeaderTitleInt(paramCharSequence);
  }
  
  public ContextMenu setHeaderView(View paramView)
  {
    return (ContextMenu)super.setHeaderViewInt(paramView);
  }
  
  public MenuDialogHelper showDialog(View paramView, IBinder paramIBinder)
  {
    if (paramView != null) {
      paramView.createContextMenu(this);
    }
    if (getVisibleItems().size() > 0)
    {
      EventLog.writeEvent(50001, 1);
      paramView = new MenuDialogHelper(this);
      paramView.show(paramIBinder);
      return paramView;
    }
    return null;
  }
  
  public MenuPopupHelper showPopup(Context paramContext, View paramView, float paramFloat1, float paramFloat2)
  {
    if (paramView != null) {
      paramView.createContextMenu(this);
    }
    if (getVisibleItems().size() > 0)
    {
      EventLog.writeEvent(50001, 1);
      paramView.getLocationOnScreen(new int[2]);
      paramContext = new MenuPopupHelper(paramContext, this, paramView, false, 16844033);
      paramContext.show(Math.round(paramFloat1), Math.round(paramFloat2));
      return paramContext;
    }
    return null;
  }
}
