package com.android.internal.view.menu;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ActionMenuView.ActionMenuChildView;
import android.widget.ForwardingListener;
import android.widget.TextView;
import com.android.internal.R.styleable;
import java.util.List;

public class ActionMenuItemView
  extends TextView
  implements MenuView.ItemView, View.OnClickListener, ActionMenuView.ActionMenuChildView
{
  private static final int MAX_ICON_SIZE = 32;
  private static final String TAG = "ActionMenuItemView";
  private boolean mAllowTextWithIcon;
  private boolean mExpandedFormat;
  private ForwardingListener mForwardingListener;
  private Drawable mIcon;
  private MenuItemImpl mItemData;
  private MenuBuilder.ItemInvoker mItemInvoker;
  private int mMaxIconSize;
  private int mMinWidth;
  private PopupCallback mPopupCallback;
  private int mSavedPaddingLeft;
  private CharSequence mTitle;
  
  public ActionMenuItemView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public ActionMenuItemView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public ActionMenuItemView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public ActionMenuItemView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    Resources localResources = paramContext.getResources();
    mAllowTextWithIcon = shouldAllowTextWithIcon();
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ActionMenuItemView, paramInt1, paramInt2);
    mMinWidth = paramContext.getDimensionPixelSize(0, 0);
    paramContext.recycle();
    mMaxIconSize = ((int)(32.0F * getDisplayMetricsdensity + 0.5F));
    setOnClickListener(this);
    mSavedPaddingLeft = -1;
    setSaveEnabled(false);
  }
  
  private boolean shouldAllowTextWithIcon()
  {
    Configuration localConfiguration = getContext().getResources().getConfiguration();
    int i = screenWidthDp;
    int j = screenHeightDp;
    boolean bool;
    if ((i < 480) && ((i < 640) || (j < 480)) && (orientation != 2)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private void updateTextButtonVisibility()
  {
    boolean bool = TextUtils.isEmpty(mTitle);
    int i = 1;
    int j = i;
    if (mIcon != null)
    {
      if (mItemData.showsTextAsAction())
      {
        j = i;
        if (mAllowTextWithIcon) {
          break label52;
        }
        if (mExpandedFormat)
        {
          j = i;
          break label52;
        }
      }
      j = 0;
    }
    label52:
    j = (bool ^ true) & j;
    Object localObject1 = null;
    if (j != 0) {
      localObject2 = mTitle;
    } else {
      localObject2 = null;
    }
    setText((CharSequence)localObject2);
    Object localObject2 = mItemData.getContentDescription();
    if (TextUtils.isEmpty((CharSequence)localObject2))
    {
      if (j != 0) {
        localObject2 = null;
      } else {
        localObject2 = mItemData.getTitle();
      }
      setContentDescription((CharSequence)localObject2);
    }
    else
    {
      setContentDescription((CharSequence)localObject2);
    }
    localObject2 = mItemData.getTooltipText();
    if (TextUtils.isEmpty((CharSequence)localObject2))
    {
      if (j != 0) {
        localObject2 = localObject1;
      } else {
        localObject2 = mItemData.getTitle();
      }
      setTooltipText((CharSequence)localObject2);
    }
    else
    {
      setTooltipText((CharSequence)localObject2);
    }
  }
  
  public boolean dispatchHoverEvent(MotionEvent paramMotionEvent)
  {
    return onHoverEvent(paramMotionEvent);
  }
  
  public boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent paramAccessibilityEvent)
  {
    onPopulateAccessibilityEvent(paramAccessibilityEvent);
    return true;
  }
  
  public MenuItemImpl getItemData()
  {
    return mItemData;
  }
  
  public boolean hasText()
  {
    return TextUtils.isEmpty(getText()) ^ true;
  }
  
  public void initialize(MenuItemImpl paramMenuItemImpl, int paramInt)
  {
    mItemData = paramMenuItemImpl;
    setIcon(paramMenuItemImpl.getIcon());
    setTitle(paramMenuItemImpl.getTitleForItemView(this));
    setId(paramMenuItemImpl.getItemId());
    if (paramMenuItemImpl.isVisible()) {
      paramInt = 0;
    } else {
      paramInt = 8;
    }
    setVisibility(paramInt);
    setEnabled(paramMenuItemImpl.isEnabled());
    if ((paramMenuItemImpl.hasSubMenu()) && (mForwardingListener == null)) {
      mForwardingListener = new ActionMenuItemForwardingListener();
    }
  }
  
  public boolean needsDividerAfter()
  {
    return hasText();
  }
  
  public boolean needsDividerBefore()
  {
    boolean bool;
    if ((hasText()) && (mItemData.getIcon() == null)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void onClick(View paramView)
  {
    if (mItemInvoker != null) {
      mItemInvoker.invokeItem(mItemData);
    }
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    mAllowTextWithIcon = shouldAllowTextWithIcon();
    updateTextButtonVisibility();
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    boolean bool = hasText();
    if ((bool) && (mSavedPaddingLeft >= 0)) {
      super.setPadding(mSavedPaddingLeft, getPaddingTop(), getPaddingRight(), getPaddingBottom());
    }
    super.onMeasure(paramInt1, paramInt2);
    int i = View.MeasureSpec.getMode(paramInt1);
    paramInt1 = View.MeasureSpec.getSize(paramInt1);
    int j = getMeasuredWidth();
    if (i == Integer.MIN_VALUE) {
      paramInt1 = Math.min(paramInt1, mMinWidth);
    } else {
      paramInt1 = mMinWidth;
    }
    if ((i != 1073741824) && (mMinWidth > 0) && (j < paramInt1)) {
      super.onMeasure(View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824), paramInt2);
    }
    if ((!bool) && (mIcon != null)) {
      super.setPadding((getMeasuredWidth() - mIcon.getBounds().width()) / 2, getPaddingTop(), getPaddingRight(), getPaddingBottom());
    }
  }
  
  public void onPopulateAccessibilityEventInternal(AccessibilityEvent paramAccessibilityEvent)
  {
    super.onPopulateAccessibilityEventInternal(paramAccessibilityEvent);
    CharSequence localCharSequence = getContentDescription();
    if (!TextUtils.isEmpty(localCharSequence)) {
      paramAccessibilityEvent.getText().add(localCharSequence);
    }
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    super.onRestoreInstanceState(null);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if ((mItemData.hasSubMenu()) && (mForwardingListener != null) && (mForwardingListener.onTouch(this, paramMotionEvent))) {
      return true;
    }
    return super.onTouchEvent(paramMotionEvent);
  }
  
  public boolean prefersCondensedTitle()
  {
    return true;
  }
  
  public void setCheckable(boolean paramBoolean) {}
  
  public void setChecked(boolean paramBoolean) {}
  
  public void setExpandedFormat(boolean paramBoolean)
  {
    if (mExpandedFormat != paramBoolean)
    {
      mExpandedFormat = paramBoolean;
      if (mItemData != null) {
        mItemData.actionFormatChanged();
      }
    }
  }
  
  public void setIcon(Drawable paramDrawable)
  {
    mIcon = paramDrawable;
    if (paramDrawable != null)
    {
      int i = paramDrawable.getIntrinsicWidth();
      int j = paramDrawable.getIntrinsicHeight();
      int k = i;
      int m = j;
      float f;
      if (i > mMaxIconSize)
      {
        f = mMaxIconSize / i;
        k = mMaxIconSize;
        m = (int)(j * f);
      }
      i = k;
      j = m;
      if (m > mMaxIconSize)
      {
        f = mMaxIconSize / m;
        j = mMaxIconSize;
        i = (int)(k * f);
      }
      paramDrawable.setBounds(0, 0, i, j);
    }
    setCompoundDrawables(paramDrawable, null, null, null);
    updateTextButtonVisibility();
  }
  
  public void setItemInvoker(MenuBuilder.ItemInvoker paramItemInvoker)
  {
    mItemInvoker = paramItemInvoker;
  }
  
  public void setPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mSavedPaddingLeft = paramInt1;
    super.setPadding(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public void setPopupCallback(PopupCallback paramPopupCallback)
  {
    mPopupCallback = paramPopupCallback;
  }
  
  public void setShortcut(boolean paramBoolean, char paramChar) {}
  
  public void setTitle(CharSequence paramCharSequence)
  {
    mTitle = paramCharSequence;
    updateTextButtonVisibility();
  }
  
  public boolean showsIcon()
  {
    return true;
  }
  
  private class ActionMenuItemForwardingListener
    extends ForwardingListener
  {
    public ActionMenuItemForwardingListener()
    {
      super();
    }
    
    public ShowableListMenu getPopup()
    {
      if (mPopupCallback != null) {
        return mPopupCallback.getPopup();
      }
      return null;
    }
    
    protected boolean onForwardingStarted()
    {
      Object localObject = mItemInvoker;
      boolean bool1 = false;
      if ((localObject != null) && (mItemInvoker.invokeItem(mItemData)))
      {
        localObject = getPopup();
        boolean bool2 = bool1;
        if (localObject != null)
        {
          bool2 = bool1;
          if (((ShowableListMenu)localObject).isShowing()) {
            bool2 = true;
          }
        }
        return bool2;
      }
      return false;
    }
  }
  
  public static abstract class PopupCallback
  {
    public PopupCallback() {}
    
    public abstract ShowableListMenu getPopup();
  }
}
