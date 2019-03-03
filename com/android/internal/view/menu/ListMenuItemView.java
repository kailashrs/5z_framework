package com.android.internal.view.menu;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AbsListView.SelectionBoundsAdjuster;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.TextView;
import com.android.internal.R.styleable;

public class ListMenuItemView
  extends LinearLayout
  implements MenuView.ItemView, AbsListView.SelectionBoundsAdjuster
{
  private static final String TAG = "ListMenuItemView";
  private Drawable mBackground;
  private CheckBox mCheckBox;
  private LinearLayout mContent;
  private boolean mForceShowIcon;
  private ImageView mGroupDivider;
  private boolean mHasListDivider;
  private ImageView mIconView;
  private LayoutInflater mInflater;
  private MenuItemImpl mItemData;
  private int mMenuType;
  private boolean mPreserveIconSpacing;
  private RadioButton mRadioButton;
  private TextView mShortcutView;
  private Drawable mSubMenuArrow;
  private ImageView mSubMenuArrowView;
  private int mTextAppearance;
  private Context mTextAppearanceContext;
  private TextView mTitleView;
  
  public ListMenuItemView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16844018);
  }
  
  public ListMenuItemView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public ListMenuItemView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.MenuView, paramInt1, paramInt2);
    mBackground = paramAttributeSet.getDrawable(5);
    mTextAppearance = paramAttributeSet.getResourceId(1, -1);
    mPreserveIconSpacing = paramAttributeSet.getBoolean(8, false);
    mTextAppearanceContext = paramContext;
    mSubMenuArrow = paramAttributeSet.getDrawable(7);
    paramContext = paramContext.getTheme().obtainStyledAttributes(null, new int[] { 16843049 }, 16842861, 0);
    mHasListDivider = paramContext.hasValue(0);
    paramAttributeSet.recycle();
    paramContext.recycle();
  }
  
  private void addContentView(View paramView)
  {
    addContentView(paramView, -1);
  }
  
  private void addContentView(View paramView, int paramInt)
  {
    if (mContent != null) {
      mContent.addView(paramView, paramInt);
    } else {
      addView(paramView, paramInt);
    }
  }
  
  private LayoutInflater getInflater()
  {
    if (mInflater == null) {
      mInflater = LayoutInflater.from(mContext);
    }
    return mInflater;
  }
  
  private void insertCheckBox()
  {
    LayoutInflater localLayoutInflater = getInflater();
    mCheckBox = ((CheckBox)localLayoutInflater.inflate(17367199, this, false));
    addContentView(mCheckBox);
  }
  
  private void insertIconView()
  {
    mIconView = ((ImageView)getInflater().inflate(17367200, this, false));
    addContentView(mIconView, 0);
  }
  
  private void insertRadioButton()
  {
    LayoutInflater localLayoutInflater = getInflater();
    mRadioButton = ((RadioButton)localLayoutInflater.inflate(17367202, this, false));
    addContentView(mRadioButton);
  }
  
  private void setSubMenuArrowVisible(boolean paramBoolean)
  {
    if (mSubMenuArrowView != null)
    {
      ImageView localImageView = mSubMenuArrowView;
      int i;
      if (paramBoolean) {
        i = 0;
      } else {
        i = 8;
      }
      localImageView.setVisibility(i);
    }
  }
  
  public void adjustListItemSelectionBounds(Rect paramRect)
  {
    if ((mGroupDivider != null) && (mGroupDivider.getVisibility() == 0))
    {
      LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)mGroupDivider.getLayoutParams();
      top += mGroupDivider.getHeight() + topMargin + bottomMargin;
    }
  }
  
  public MenuItemImpl getItemData()
  {
    return mItemData;
  }
  
  public void initialize(MenuItemImpl paramMenuItemImpl, int paramInt)
  {
    mItemData = paramMenuItemImpl;
    mMenuType = paramInt;
    if (paramMenuItemImpl.isVisible()) {
      paramInt = 0;
    } else {
      paramInt = 8;
    }
    setVisibility(paramInt);
    setTitle(paramMenuItemImpl.getTitleForItemView(this));
    setCheckable(paramMenuItemImpl.isCheckable());
    setShortcut(paramMenuItemImpl.shouldShowShortcut(), paramMenuItemImpl.getShortcut());
    setIcon(paramMenuItemImpl.getIcon());
    setEnabled(paramMenuItemImpl.isEnabled());
    setSubMenuArrowVisible(paramMenuItemImpl.hasSubMenu());
    setContentDescription(paramMenuItemImpl.getContentDescription());
  }
  
  protected void onFinishInflate()
  {
    super.onFinishInflate();
    setBackgroundDrawable(mBackground);
    mTitleView = ((TextView)findViewById(16908310));
    if (mTextAppearance != -1) {
      mTitleView.setTextAppearance(mTextAppearanceContext, mTextAppearance);
    }
    mShortcutView = ((TextView)findViewById(16909356));
    mSubMenuArrowView = ((ImageView)findViewById(16909419));
    if (mSubMenuArrowView != null) {
      mSubMenuArrowView.setImageDrawable(mSubMenuArrow);
    }
    mGroupDivider = ((ImageView)findViewById(16908990));
    mContent = ((LinearLayout)findViewById(16908290));
  }
  
  public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    super.onInitializeAccessibilityNodeInfoInternal(paramAccessibilityNodeInfo);
    if ((mItemData != null) && (mItemData.hasSubMenu())) {
      paramAccessibilityNodeInfo.setCanOpenPopup(true);
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if ((mIconView != null) && (mPreserveIconSpacing))
    {
      ViewGroup.LayoutParams localLayoutParams = getLayoutParams();
      LinearLayout.LayoutParams localLayoutParams1 = (LinearLayout.LayoutParams)mIconView.getLayoutParams();
      if ((height > 0) && (width <= 0)) {
        width = height;
      }
    }
    super.onMeasure(paramInt1, paramInt2);
  }
  
  public boolean prefersCondensedTitle()
  {
    return false;
  }
  
  public void setCheckable(boolean paramBoolean)
  {
    if ((!paramBoolean) && (mRadioButton == null) && (mCheckBox == null)) {
      return;
    }
    Object localObject1;
    Object localObject2;
    if (mItemData.isExclusiveCheckable())
    {
      if (mRadioButton == null) {
        insertRadioButton();
      }
      localObject1 = mRadioButton;
      localObject2 = mCheckBox;
    }
    else
    {
      if (mCheckBox == null) {
        insertCheckBox();
      }
      localObject1 = mCheckBox;
      localObject2 = mRadioButton;
    }
    if (paramBoolean)
    {
      ((CompoundButton)localObject1).setChecked(mItemData.isChecked());
      int i;
      if (paramBoolean) {
        i = 0;
      } else {
        i = 8;
      }
      if (((CompoundButton)localObject1).getVisibility() != i) {
        ((CompoundButton)localObject1).setVisibility(i);
      }
      if ((localObject2 != null) && (((CompoundButton)localObject2).getVisibility() != 8)) {
        ((CompoundButton)localObject2).setVisibility(8);
      }
    }
    else
    {
      if (mCheckBox != null) {
        mCheckBox.setVisibility(8);
      }
      if (mRadioButton != null) {
        mRadioButton.setVisibility(8);
      }
    }
  }
  
  public void setChecked(boolean paramBoolean)
  {
    Object localObject;
    if (mItemData.isExclusiveCheckable())
    {
      if (mRadioButton == null) {
        insertRadioButton();
      }
      localObject = mRadioButton;
    }
    else
    {
      if (mCheckBox == null) {
        insertCheckBox();
      }
      localObject = mCheckBox;
    }
    ((CompoundButton)localObject).setChecked(paramBoolean);
  }
  
  public void setForceShowIcon(boolean paramBoolean)
  {
    mForceShowIcon = paramBoolean;
    mPreserveIconSpacing = paramBoolean;
  }
  
  public void setGroupDividerEnabled(boolean paramBoolean)
  {
    if (mGroupDivider != null)
    {
      ImageView localImageView = mGroupDivider;
      int i;
      if ((!mHasListDivider) && (paramBoolean)) {
        i = 0;
      } else {
        i = 8;
      }
      localImageView.setVisibility(i);
    }
  }
  
  public void setIcon(Drawable paramDrawable)
  {
    int i;
    if ((!mItemData.shouldShowIcon()) && (!mForceShowIcon)) {
      i = 0;
    } else {
      i = 1;
    }
    if ((i == 0) && (!mPreserveIconSpacing)) {
      return;
    }
    if ((mIconView == null) && (paramDrawable == null) && (!mPreserveIconSpacing)) {
      return;
    }
    if (mIconView == null) {
      insertIconView();
    }
    if ((paramDrawable == null) && (!mPreserveIconSpacing))
    {
      mIconView.setVisibility(8);
    }
    else
    {
      ImageView localImageView = mIconView;
      if (i == 0) {
        paramDrawable = null;
      }
      localImageView.setImageDrawable(paramDrawable);
      if (mIconView.getVisibility() != 0) {
        mIconView.setVisibility(0);
      }
    }
  }
  
  public void setShortcut(boolean paramBoolean, char paramChar)
  {
    if ((paramBoolean) && (mItemData.shouldShowShortcut())) {
      paramChar = '\000';
    } else {
      paramChar = '\b';
    }
    if (paramChar == 0) {
      mShortcutView.setText(mItemData.getShortcutLabel());
    }
    if (mShortcutView.getVisibility() != paramChar) {
      mShortcutView.setVisibility(paramChar);
    }
  }
  
  public void setTitle(CharSequence paramCharSequence)
  {
    if (paramCharSequence != null)
    {
      mTitleView.setText(paramCharSequence);
      if (mTitleView.getVisibility() != 0) {
        mTitleView.setVisibility(0);
      }
    }
    else if (mTitleView.getVisibility() != 8)
    {
      mTitleView.setVisibility(8);
    }
  }
  
  public boolean showsIcon()
  {
    return mForceShowIcon;
  }
}
