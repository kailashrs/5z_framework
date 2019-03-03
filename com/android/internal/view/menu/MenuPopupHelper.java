package com.android.internal.view.menu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow.OnDismissListener;

public class MenuPopupHelper
  implements MenuHelper
{
  private static final int TOUCH_EPICENTER_SIZE_DP = 48;
  private View mAnchorView;
  private final Context mContext;
  private int mDropDownGravity = 8388611;
  private boolean mForceShowIcon;
  private final PopupWindow.OnDismissListener mInternalOnDismissListener = new PopupWindow.OnDismissListener()
  {
    public void onDismiss()
    {
      MenuPopupHelper.this.onDismiss();
    }
  };
  private final MenuBuilder mMenu;
  private PopupWindow.OnDismissListener mOnDismissListener;
  private final boolean mOverflowOnly;
  private MenuPopup mPopup;
  private final int mPopupStyleAttr;
  private final int mPopupStyleRes;
  private MenuPresenter.Callback mPresenterCallback;
  
  public MenuPopupHelper(Context paramContext, MenuBuilder paramMenuBuilder)
  {
    this(paramContext, paramMenuBuilder, null, false, 16843520, 0);
  }
  
  public MenuPopupHelper(Context paramContext, MenuBuilder paramMenuBuilder, View paramView)
  {
    this(paramContext, paramMenuBuilder, paramView, false, 16843520, 0);
  }
  
  public MenuPopupHelper(Context paramContext, MenuBuilder paramMenuBuilder, View paramView, boolean paramBoolean, int paramInt)
  {
    this(paramContext, paramMenuBuilder, paramView, paramBoolean, paramInt, 0);
  }
  
  public MenuPopupHelper(Context paramContext, MenuBuilder paramMenuBuilder, View paramView, boolean paramBoolean, int paramInt1, int paramInt2)
  {
    mContext = paramContext;
    mMenu = paramMenuBuilder;
    mAnchorView = paramView;
    mOverflowOnly = paramBoolean;
    mPopupStyleAttr = paramInt1;
    mPopupStyleRes = paramInt2;
  }
  
  private MenuPopup createPopup()
  {
    Display localDisplay = ((WindowManager)mContext.getSystemService("window")).getDefaultDisplay();
    Object localObject = new Point();
    localDisplay.getRealSize((Point)localObject);
    int i;
    if (Math.min(x, y) >= mContext.getResources().getDimensionPixelSize(17105069)) {
      i = 1;
    } else {
      i = 0;
    }
    if (i != 0) {
      localObject = new CascadingMenuPopup(mContext, mAnchorView, mPopupStyleAttr, mPopupStyleRes, mOverflowOnly);
    } else {
      localObject = new StandardMenuPopup(mContext, mMenu, mAnchorView, mPopupStyleAttr, mPopupStyleRes, mOverflowOnly);
    }
    ((MenuPopup)localObject).addMenu(mMenu);
    ((MenuPopup)localObject).setOnDismissListener(mInternalOnDismissListener);
    ((MenuPopup)localObject).setAnchorView(mAnchorView);
    ((MenuPopup)localObject).setCallback(mPresenterCallback);
    ((MenuPopup)localObject).setForceShowIcon(mForceShowIcon);
    ((MenuPopup)localObject).setGravity(mDropDownGravity);
    return localObject;
  }
  
  private void showPopup(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
  {
    MenuPopup localMenuPopup = getPopup();
    localMenuPopup.setShowTitle(paramBoolean2);
    if (paramBoolean1)
    {
      int i = paramInt1;
      if ((Gravity.getAbsoluteGravity(mDropDownGravity, mAnchorView.getLayoutDirection()) & 0x7) == 5) {
        i = paramInt1 - mAnchorView.getWidth();
      }
      localMenuPopup.setHorizontalOffset(i);
      localMenuPopup.setVerticalOffset(paramInt2);
      paramInt1 = (int)(48.0F * mContext.getResources().getDisplayMetrics().density / 2.0F);
      localMenuPopup.setEpicenterBounds(new Rect(i - paramInt1, paramInt2 - paramInt1, i + paramInt1, paramInt2 + paramInt1));
    }
    localMenuPopup.show();
  }
  
  public void dismiss()
  {
    if (isShowing()) {
      mPopup.dismiss();
    }
  }
  
  public int getGravity()
  {
    return mDropDownGravity;
  }
  
  public MenuPopup getPopup()
  {
    if (mPopup == null) {
      mPopup = createPopup();
    }
    return mPopup;
  }
  
  public boolean isShowing()
  {
    boolean bool;
    if ((mPopup != null) && (mPopup.isShowing())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected void onDismiss()
  {
    mPopup = null;
    if (mOnDismissListener != null) {
      mOnDismissListener.onDismiss();
    }
  }
  
  public void setAnchorView(View paramView)
  {
    mAnchorView = paramView;
  }
  
  public void setForceShowIcon(boolean paramBoolean)
  {
    mForceShowIcon = paramBoolean;
    if (mPopup != null) {
      mPopup.setForceShowIcon(paramBoolean);
    }
  }
  
  public void setGravity(int paramInt)
  {
    mDropDownGravity = paramInt;
  }
  
  public void setOnDismissListener(PopupWindow.OnDismissListener paramOnDismissListener)
  {
    mOnDismissListener = paramOnDismissListener;
  }
  
  public void setPresenterCallback(MenuPresenter.Callback paramCallback)
  {
    mPresenterCallback = paramCallback;
    if (mPopup != null) {
      mPopup.setCallback(paramCallback);
    }
  }
  
  public void show()
  {
    if (tryShow()) {
      return;
    }
    throw new IllegalStateException("MenuPopupHelper cannot be used without an anchor");
  }
  
  public void show(int paramInt1, int paramInt2)
  {
    if (tryShow(paramInt1, paramInt2)) {
      return;
    }
    throw new IllegalStateException("MenuPopupHelper cannot be used without an anchor");
  }
  
  public boolean tryShow()
  {
    if (isShowing()) {
      return true;
    }
    if (mAnchorView == null) {
      return false;
    }
    showPopup(0, 0, false, false);
    return true;
  }
  
  public boolean tryShow(int paramInt1, int paramInt2)
  {
    if (isShowing()) {
      return true;
    }
    if (mAnchorView == null) {
      return false;
    }
    showPopup(paramInt1, paramInt2, true, true);
    return true;
  }
}
