package com.android.internal.view.menu;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.MenuPopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import com.android.internal.util.Preconditions;

final class StandardMenuPopup
  extends MenuPopup
  implements PopupWindow.OnDismissListener, AdapterView.OnItemClickListener, MenuPresenter, View.OnKeyListener
{
  private static final int ITEM_LAYOUT = 17367244;
  private final MenuAdapter mAdapter;
  private View mAnchorView;
  private final View.OnAttachStateChangeListener mAttachStateChangeListener = new View.OnAttachStateChangeListener()
  {
    public void onViewAttachedToWindow(View paramAnonymousView) {}
    
    public void onViewDetachedFromWindow(View paramAnonymousView)
    {
      if (mTreeObserver != null)
      {
        if (!mTreeObserver.isAlive()) {
          StandardMenuPopup.access$202(StandardMenuPopup.this, paramAnonymousView.getViewTreeObserver());
        }
        mTreeObserver.removeGlobalOnLayoutListener(mGlobalLayoutListener);
      }
      paramAnonymousView.removeOnAttachStateChangeListener(this);
    }
  };
  private int mContentWidth;
  private final Context mContext;
  private int mDropDownGravity = 0;
  private final ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener()
  {
    public void onGlobalLayout()
    {
      if ((isShowing()) && (!mPopup.isModal()))
      {
        View localView = mShownAnchorView;
        if ((localView != null) && (localView.isShown())) {
          mPopup.show();
        } else {
          dismiss();
        }
      }
    }
  };
  private boolean mHasContentWidth;
  private final MenuBuilder mMenu;
  private PopupWindow.OnDismissListener mOnDismissListener;
  private final boolean mOverflowOnly;
  private final MenuPopupWindow mPopup;
  private final int mPopupMaxWidth;
  private final int mPopupStyleAttr;
  private final int mPopupStyleRes;
  private MenuPresenter.Callback mPresenterCallback;
  private boolean mShowTitle;
  private View mShownAnchorView;
  private ViewTreeObserver mTreeObserver;
  private boolean mWasDismissed;
  
  public StandardMenuPopup(Context paramContext, MenuBuilder paramMenuBuilder, View paramView, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    mContext = ((Context)Preconditions.checkNotNull(paramContext));
    mMenu = paramMenuBuilder;
    mOverflowOnly = paramBoolean;
    mAdapter = new MenuAdapter(paramMenuBuilder, LayoutInflater.from(paramContext), mOverflowOnly, 17367244);
    mPopupStyleAttr = paramInt1;
    mPopupStyleRes = paramInt2;
    Resources localResources = paramContext.getResources();
    mPopupMaxWidth = Math.max(getDisplayMetricswidthPixels / 2, localResources.getDimensionPixelSize(17105088));
    mAnchorView = paramView;
    mPopup = new MenuPopupWindow(mContext, null, mPopupStyleAttr, mPopupStyleRes);
    paramMenuBuilder.addMenuPresenter(this, paramContext);
  }
  
  private boolean tryShow()
  {
    if (isShowing()) {
      return true;
    }
    if ((!mWasDismissed) && (mAnchorView != null))
    {
      mShownAnchorView = mAnchorView;
      mPopup.setOnDismissListener(this);
      mPopup.setOnItemClickListener(this);
      mPopup.setAdapter(mAdapter);
      mPopup.setModal(true);
      Object localObject = mShownAnchorView;
      int i;
      if (mTreeObserver == null) {
        i = 1;
      } else {
        i = 0;
      }
      mTreeObserver = ((View)localObject).getViewTreeObserver();
      if (i != 0) {
        mTreeObserver.addOnGlobalLayoutListener(mGlobalLayoutListener);
      }
      ((View)localObject).addOnAttachStateChangeListener(mAttachStateChangeListener);
      mPopup.setAnchorView((View)localObject);
      mPopup.setDropDownGravity(mDropDownGravity);
      if (!mHasContentWidth)
      {
        mContentWidth = measureIndividualMenuWidth(mAdapter, null, mContext, mPopupMaxWidth);
        mHasContentWidth = true;
      }
      mPopup.setContentWidth(mContentWidth);
      mPopup.setInputMethodMode(2);
      mPopup.setEpicenterBounds(getEpicenterBounds());
      mPopup.show();
      localObject = mPopup.getListView();
      ((ListView)localObject).setOnKeyListener(this);
      if ((mShowTitle) && (mMenu.getHeaderTitle() != null))
      {
        FrameLayout localFrameLayout = (FrameLayout)LayoutInflater.from(mContext).inflate(17367243, (ViewGroup)localObject, false);
        TextView localTextView = (TextView)localFrameLayout.findViewById(16908310);
        if (localTextView != null) {
          localTextView.setText(mMenu.getHeaderTitle());
        }
        localFrameLayout.setEnabled(false);
        ((ListView)localObject).addHeaderView(localFrameLayout, null, false);
        mPopup.show();
      }
      return true;
    }
    return false;
  }
  
  public void addMenu(MenuBuilder paramMenuBuilder) {}
  
  public void dismiss()
  {
    if (isShowing()) {
      mPopup.dismiss();
    }
  }
  
  public boolean flagActionItems()
  {
    return false;
  }
  
  public ListView getListView()
  {
    return mPopup.getListView();
  }
  
  public boolean isShowing()
  {
    boolean bool;
    if ((!mWasDismissed) && (mPopup.isShowing())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean)
  {
    if (paramMenuBuilder != mMenu) {
      return;
    }
    dismiss();
    if (mPresenterCallback != null) {
      mPresenterCallback.onCloseMenu(paramMenuBuilder, paramBoolean);
    }
  }
  
  public void onDismiss()
  {
    mWasDismissed = true;
    mMenu.close();
    if (mTreeObserver != null)
    {
      if (!mTreeObserver.isAlive()) {
        mTreeObserver = mShownAnchorView.getViewTreeObserver();
      }
      mTreeObserver.removeGlobalOnLayoutListener(mGlobalLayoutListener);
      mTreeObserver = null;
    }
    mShownAnchorView.removeOnAttachStateChangeListener(mAttachStateChangeListener);
    if (mOnDismissListener != null) {
      mOnDismissListener.onDismiss();
    }
  }
  
  public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent)
  {
    if ((paramKeyEvent.getAction() == 1) && (paramInt == 82))
    {
      dismiss();
      return true;
    }
    return false;
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {}
  
  public Parcelable onSaveInstanceState()
  {
    return null;
  }
  
  public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder)
  {
    if (paramSubMenuBuilder.hasVisibleItems())
    {
      MenuPopupHelper localMenuPopupHelper = new MenuPopupHelper(mContext, paramSubMenuBuilder, mShownAnchorView, mOverflowOnly, mPopupStyleAttr, mPopupStyleRes);
      localMenuPopupHelper.setPresenterCallback(mPresenterCallback);
      localMenuPopupHelper.setForceShowIcon(MenuPopup.shouldPreserveIconSpacing(paramSubMenuBuilder));
      localMenuPopupHelper.setOnDismissListener(mOnDismissListener);
      mOnDismissListener = null;
      mMenu.close(false);
      int i = mPopup.getHorizontalOffset();
      int j = mPopup.getVerticalOffset();
      int k = i;
      if ((Gravity.getAbsoluteGravity(mDropDownGravity, mAnchorView.getLayoutDirection()) & 0x7) == 5) {
        k = i + mAnchorView.getWidth();
      }
      if (localMenuPopupHelper.tryShow(k, j))
      {
        if (mPresenterCallback != null) {
          mPresenterCallback.onOpenSubMenu(paramSubMenuBuilder);
        }
        return true;
      }
    }
    return false;
  }
  
  public void setAnchorView(View paramView)
  {
    mAnchorView = paramView;
  }
  
  public void setCallback(MenuPresenter.Callback paramCallback)
  {
    mPresenterCallback = paramCallback;
  }
  
  public void setForceShowIcon(boolean paramBoolean)
  {
    mAdapter.setForceShowIcon(paramBoolean);
  }
  
  public void setGravity(int paramInt)
  {
    mDropDownGravity = paramInt;
  }
  
  public void setHorizontalOffset(int paramInt)
  {
    mPopup.setHorizontalOffset(paramInt);
  }
  
  public void setOnDismissListener(PopupWindow.OnDismissListener paramOnDismissListener)
  {
    mOnDismissListener = paramOnDismissListener;
  }
  
  public void setShowTitle(boolean paramBoolean)
  {
    mShowTitle = paramBoolean;
  }
  
  public void setVerticalOffset(int paramInt)
  {
    mPopup.setVerticalOffset(paramInt);
  }
  
  public void show()
  {
    if (tryShow()) {
      return;
    }
    throw new IllegalStateException("StandardMenuPopup cannot be used without an anchor");
  }
  
  public void updateMenuView(boolean paramBoolean)
  {
    mHasContentWidth = false;
    if (mAdapter != null) {
      mAdapter.notifyDataSetChanged();
    }
  }
}
