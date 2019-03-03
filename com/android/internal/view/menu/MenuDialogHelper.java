package com.android.internal.view.menu;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.KeyEvent.DispatcherState;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ListAdapter;

public class MenuDialogHelper
  implements MenuHelper, DialogInterface.OnKeyListener, DialogInterface.OnClickListener, DialogInterface.OnDismissListener, MenuPresenter.Callback
{
  private AlertDialog mDialog;
  private MenuBuilder mMenu;
  ListMenuPresenter mPresenter;
  private MenuPresenter.Callback mPresenterCallback;
  
  public MenuDialogHelper(MenuBuilder paramMenuBuilder)
  {
    mMenu = paramMenuBuilder;
  }
  
  public void dismiss()
  {
    if (mDialog != null) {
      mDialog.dismiss();
    }
  }
  
  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    mMenu.performItemAction((MenuItemImpl)mPresenter.getAdapter().getItem(paramInt), 0);
  }
  
  public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean)
  {
    if ((paramBoolean) || (paramMenuBuilder == mMenu)) {
      dismiss();
    }
    if (mPresenterCallback != null) {
      mPresenterCallback.onCloseMenu(paramMenuBuilder, paramBoolean);
    }
  }
  
  public void onDismiss(DialogInterface paramDialogInterface)
  {
    mPresenter.onCloseMenu(mMenu, true);
  }
  
  public boolean onKey(DialogInterface paramDialogInterface, int paramInt, KeyEvent paramKeyEvent)
  {
    if ((paramInt == 82) || (paramInt == 4)) {
      if ((paramKeyEvent.getAction() == 0) && (paramKeyEvent.getRepeatCount() == 0))
      {
        paramDialogInterface = mDialog.getWindow();
        if (paramDialogInterface != null)
        {
          paramDialogInterface = paramDialogInterface.getDecorView();
          if (paramDialogInterface != null)
          {
            paramDialogInterface = paramDialogInterface.getKeyDispatcherState();
            if (paramDialogInterface != null)
            {
              paramDialogInterface.startTracking(paramKeyEvent, this);
              return true;
            }
          }
        }
      }
      else if ((paramKeyEvent.getAction() == 1) && (!paramKeyEvent.isCanceled()))
      {
        Object localObject = mDialog.getWindow();
        if (localObject != null)
        {
          localObject = ((Window)localObject).getDecorView();
          if (localObject != null)
          {
            localObject = ((View)localObject).getKeyDispatcherState();
            if ((localObject != null) && (((KeyEvent.DispatcherState)localObject).isTracking(paramKeyEvent)))
            {
              mMenu.close(true);
              paramDialogInterface.dismiss();
              return true;
            }
          }
        }
      }
    }
    return mMenu.performShortcut(paramInt, paramKeyEvent, 0);
  }
  
  public boolean onOpenSubMenu(MenuBuilder paramMenuBuilder)
  {
    if (mPresenterCallback != null) {
      return mPresenterCallback.onOpenSubMenu(paramMenuBuilder);
    }
    return false;
  }
  
  public void setPresenterCallback(MenuPresenter.Callback paramCallback)
  {
    mPresenterCallback = paramCallback;
  }
  
  public void show(IBinder paramIBinder)
  {
    MenuBuilder localMenuBuilder = mMenu;
    Object localObject = new AlertDialog.Builder(localMenuBuilder.getContext());
    mPresenter = new ListMenuPresenter(((AlertDialog.Builder)localObject).getContext(), 17367201);
    mPresenter.setCallback(this);
    mMenu.addMenuPresenter(mPresenter);
    ((AlertDialog.Builder)localObject).setAdapter(mPresenter.getAdapter(), this);
    View localView = localMenuBuilder.getHeaderView();
    if (localView != null) {
      ((AlertDialog.Builder)localObject).setCustomTitle(localView);
    } else {
      ((AlertDialog.Builder)localObject).setIcon(localMenuBuilder.getHeaderIcon()).setTitle(localMenuBuilder.getHeaderTitle());
    }
    ((AlertDialog.Builder)localObject).setOnKeyListener(this);
    mDialog = ((AlertDialog.Builder)localObject).create();
    mDialog.setOnDismissListener(this);
    localObject = mDialog.getWindow().getAttributes();
    type = 1003;
    if (paramIBinder != null) {
      token = paramIBinder;
    }
    flags |= 0x20000;
    mDialog.show();
  }
}
