package android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.transition.Transition;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import com.android.internal.view.menu.ListMenuItemView;
import com.android.internal.view.menu.MenuAdapter;
import com.android.internal.view.menu.MenuBuilder;
import com.android.internal.view.menu.MenuItemImpl;

public class MenuPopupWindow
  extends ListPopupWindow
  implements MenuItemHoverListener
{
  private MenuItemHoverListener mHoverListener;
  
  public MenuPopupWindow(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  DropDownListView createDropDownListView(Context paramContext, boolean paramBoolean)
  {
    paramContext = new MenuDropDownListView(paramContext, paramBoolean);
    paramContext.setHoverListener(this);
    return paramContext;
  }
  
  public void onItemHoverEnter(MenuBuilder paramMenuBuilder, MenuItem paramMenuItem)
  {
    if (mHoverListener != null) {
      mHoverListener.onItemHoverEnter(paramMenuBuilder, paramMenuItem);
    }
  }
  
  public void onItemHoverExit(MenuBuilder paramMenuBuilder, MenuItem paramMenuItem)
  {
    if (mHoverListener != null) {
      mHoverListener.onItemHoverExit(paramMenuBuilder, paramMenuItem);
    }
  }
  
  public void setEnterTransition(Transition paramTransition)
  {
    mPopup.setEnterTransition(paramTransition);
  }
  
  public void setExitTransition(Transition paramTransition)
  {
    mPopup.setExitTransition(paramTransition);
  }
  
  public void setHoverListener(MenuItemHoverListener paramMenuItemHoverListener)
  {
    mHoverListener = paramMenuItemHoverListener;
  }
  
  public void setTouchModal(boolean paramBoolean)
  {
    mPopup.setTouchModal(paramBoolean);
  }
  
  public static class MenuDropDownListView
    extends DropDownListView
  {
    final int mAdvanceKey;
    private MenuItemHoverListener mHoverListener;
    private MenuItem mHoveredMenuItem;
    final int mRetreatKey;
    
    public MenuDropDownListView(Context paramContext, boolean paramBoolean)
    {
      super(paramBoolean);
      if (paramContext.getResources().getConfiguration().getLayoutDirection() == 1)
      {
        mAdvanceKey = 21;
        mRetreatKey = 22;
      }
      else
      {
        mAdvanceKey = 22;
        mRetreatKey = 21;
      }
    }
    
    public void clearSelection()
    {
      setSelectedPositionInt(-1);
      setNextSelectedPositionInt(-1);
    }
    
    public boolean onHoverEvent(MotionEvent paramMotionEvent)
    {
      if (mHoverListener != null)
      {
        Object localObject1 = getAdapter();
        int i;
        Object localObject2;
        if ((localObject1 instanceof HeaderViewListAdapter))
        {
          localObject1 = (HeaderViewListAdapter)localObject1;
          i = ((HeaderViewListAdapter)localObject1).getHeadersCount();
          localObject2 = (MenuAdapter)((HeaderViewListAdapter)localObject1).getWrappedAdapter();
        }
        else
        {
          i = 0;
          localObject2 = (MenuAdapter)localObject1;
        }
        MenuItem localMenuItem = null;
        localObject1 = localMenuItem;
        if (paramMotionEvent.getAction() != 10)
        {
          int j = pointToPosition((int)paramMotionEvent.getX(), (int)paramMotionEvent.getY());
          localObject1 = localMenuItem;
          if (j != -1)
          {
            i = j - i;
            localObject1 = localMenuItem;
            if (i >= 0)
            {
              localObject1 = localMenuItem;
              if (i < ((MenuAdapter)localObject2).getCount()) {
                localObject1 = ((MenuAdapter)localObject2).getItem(i);
              }
            }
          }
        }
        localMenuItem = mHoveredMenuItem;
        if (localMenuItem != localObject1)
        {
          localObject2 = ((MenuAdapter)localObject2).getAdapterMenu();
          if (localMenuItem != null) {
            mHoverListener.onItemHoverExit((MenuBuilder)localObject2, localMenuItem);
          }
          mHoveredMenuItem = ((MenuItem)localObject1);
          if (localObject1 != null) {
            mHoverListener.onItemHoverEnter((MenuBuilder)localObject2, (MenuItem)localObject1);
          }
        }
      }
      return super.onHoverEvent(paramMotionEvent);
    }
    
    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
    {
      ListMenuItemView localListMenuItemView = (ListMenuItemView)getSelectedView();
      if ((localListMenuItemView != null) && (paramInt == mAdvanceKey))
      {
        if ((localListMenuItemView.isEnabled()) && (localListMenuItemView.getItemData().hasSubMenu())) {
          performItemClick(localListMenuItemView, getSelectedItemPosition(), getSelectedItemId());
        }
        return true;
      }
      if ((localListMenuItemView != null) && (paramInt == mRetreatKey))
      {
        setSelectedPositionInt(-1);
        setNextSelectedPositionInt(-1);
        ((MenuAdapter)getAdapter()).getAdapterMenu().close(false);
        return true;
      }
      return super.onKeyDown(paramInt, paramKeyEvent);
    }
    
    public void setHoverListener(MenuItemHoverListener paramMenuItemHoverListener)
    {
      mHoverListener = paramMenuItemHoverListener;
    }
  }
}
