package com.android.internal.view.menu;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewDebug.CapturedViewProperty;
import android.widget.TextView;
import com.android.internal.R.styleable;

public final class IconMenuItemView
  extends TextView
  implements MenuView.ItemView
{
  private static final int NO_ALPHA = 255;
  private static String sPrependShortcutLabel;
  private float mDisabledAlpha;
  private Drawable mIcon;
  private IconMenuView mIconMenuView;
  private MenuItemImpl mItemData;
  private MenuBuilder.ItemInvoker mItemInvoker;
  private Rect mPositionIconAvailable = new Rect();
  private Rect mPositionIconOutput = new Rect();
  private String mShortcutCaption;
  private boolean mShortcutCaptionMode;
  private int mTextAppearance;
  private Context mTextAppearanceContext;
  
  public IconMenuItemView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public IconMenuItemView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public IconMenuItemView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    if (sPrependShortcutLabel == null) {
      sPrependShortcutLabel = getResources().getString(17040857);
    }
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.MenuView, paramInt1, paramInt2);
    mDisabledAlpha = paramAttributeSet.getFloat(6, 0.8F);
    mTextAppearance = paramAttributeSet.getResourceId(1, -1);
    mTextAppearanceContext = paramContext;
    paramAttributeSet.recycle();
  }
  
  private void positionIcon()
  {
    if (mIcon == null) {
      return;
    }
    Rect localRect = mPositionIconOutput;
    getLineBounds(0, localRect);
    mPositionIconAvailable.set(0, 0, getWidth(), top);
    int i = getLayoutDirection();
    Gravity.apply(8388627, mIcon.getIntrinsicWidth(), mIcon.getIntrinsicHeight(), mPositionIconAvailable, mPositionIconOutput, i);
    mIcon.setBounds(mPositionIconOutput);
  }
  
  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    if ((mItemData != null) && (mIcon != null))
    {
      int i;
      if ((!mItemData.isEnabled()) && ((isPressed()) || (!isFocused()))) {
        i = 1;
      } else {
        i = 0;
      }
      Drawable localDrawable = mIcon;
      if (i != 0) {
        i = (int)(mDisabledAlpha * 255.0F);
      } else {
        i = 255;
      }
      localDrawable.setAlpha(i);
    }
  }
  
  @ViewDebug.CapturedViewProperty(retrieveReturn=true)
  public MenuItemImpl getItemData()
  {
    return mItemData;
  }
  
  IconMenuView.LayoutParams getTextAppropriateLayoutParams()
  {
    IconMenuView.LayoutParams localLayoutParams1 = (IconMenuView.LayoutParams)getLayoutParams();
    IconMenuView.LayoutParams localLayoutParams2 = localLayoutParams1;
    if (localLayoutParams1 == null) {
      localLayoutParams2 = new IconMenuView.LayoutParams(-1, -1);
    }
    desiredWidth = ((int)Layout.getDesiredWidth(getText(), 0, getText().length(), getPaint(), getTextDirectionHeuristic()));
    return localLayoutParams2;
  }
  
  public void initialize(MenuItemImpl paramMenuItemImpl, int paramInt)
  {
    mItemData = paramMenuItemImpl;
    initialize(paramMenuItemImpl.getTitleForItemView(this), paramMenuItemImpl.getIcon());
    if (paramMenuItemImpl.isVisible()) {
      paramInt = 0;
    } else {
      paramInt = 8;
    }
    setVisibility(paramInt);
    setEnabled(paramMenuItemImpl.isEnabled());
  }
  
  void initialize(CharSequence paramCharSequence, Drawable paramDrawable)
  {
    setClickable(true);
    setFocusable(true);
    if (mTextAppearance != -1) {
      setTextAppearance(mTextAppearanceContext, mTextAppearance);
    }
    setTitle(paramCharSequence);
    setIcon(paramDrawable);
    if (mItemData != null)
    {
      paramDrawable = mItemData.getContentDescription();
      if (TextUtils.isEmpty(paramDrawable)) {
        setContentDescription(paramCharSequence);
      } else {
        setContentDescription(paramDrawable);
      }
      setTooltipText(mItemData.getTooltipText());
    }
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    positionIcon();
  }
  
  protected void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
    super.onTextChanged(paramCharSequence, paramInt1, paramInt2, paramInt3);
    setLayoutParams(getTextAppropriateLayoutParams());
  }
  
  public boolean performClick()
  {
    if (super.performClick()) {
      return true;
    }
    if ((mItemInvoker != null) && (mItemInvoker.invokeItem(mItemData)))
    {
      playSoundEffect(0);
      return true;
    }
    return false;
  }
  
  public boolean prefersCondensedTitle()
  {
    return true;
  }
  
  void setCaptionMode(boolean paramBoolean)
  {
    if (mItemData == null) {
      return;
    }
    if ((paramBoolean) && (mItemData.shouldShowShortcut())) {
      paramBoolean = true;
    } else {
      paramBoolean = false;
    }
    mShortcutCaptionMode = paramBoolean;
    Object localObject = mItemData.getTitleForItemView(this);
    if (mShortcutCaptionMode)
    {
      if (mShortcutCaption == null) {
        mShortcutCaption = mItemData.getShortcutLabel();
      }
      localObject = mShortcutCaption;
    }
    setText((CharSequence)localObject);
  }
  
  public void setCheckable(boolean paramBoolean) {}
  
  public void setChecked(boolean paramBoolean) {}
  
  public void setIcon(Drawable paramDrawable)
  {
    mIcon = paramDrawable;
    if (paramDrawable != null)
    {
      paramDrawable.setBounds(0, 0, paramDrawable.getIntrinsicWidth(), paramDrawable.getIntrinsicHeight());
      setCompoundDrawables(null, paramDrawable, null, null);
      setGravity(81);
      requestLayout();
    }
    else
    {
      setCompoundDrawables(null, null, null, null);
      setGravity(17);
    }
  }
  
  void setIconMenuView(IconMenuView paramIconMenuView)
  {
    mIconMenuView = paramIconMenuView;
  }
  
  public void setItemData(MenuItemImpl paramMenuItemImpl)
  {
    mItemData = paramMenuItemImpl;
  }
  
  public void setItemInvoker(MenuBuilder.ItemInvoker paramItemInvoker)
  {
    mItemInvoker = paramItemInvoker;
  }
  
  public void setShortcut(boolean paramBoolean, char paramChar)
  {
    if (mShortcutCaptionMode)
    {
      mShortcutCaption = null;
      setCaptionMode(true);
    }
  }
  
  public void setTitle(CharSequence paramCharSequence)
  {
    if (mShortcutCaptionMode) {
      setCaptionMode(true);
    } else if (paramCharSequence != null) {
      setText(paramCharSequence);
    }
  }
  
  public void setVisibility(int paramInt)
  {
    super.setVisibility(paramInt);
    if (mIconMenuView != null) {
      mIconMenuView.markStaleChildren();
    }
  }
  
  public boolean showsIcon()
  {
    return true;
  }
}
