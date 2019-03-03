package android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewHierarchyEncoder;
import android.view.accessibility.AccessibilityEvent;
import com.android.internal.view.menu.ActionMenuItemView;
import com.android.internal.view.menu.MenuBuilder;
import com.android.internal.view.menu.MenuBuilder.Callback;
import com.android.internal.view.menu.MenuBuilder.ItemInvoker;
import com.android.internal.view.menu.MenuItemImpl;
import com.android.internal.view.menu.MenuPresenter.Callback;
import com.android.internal.view.menu.MenuView;

public class ActionMenuView
  extends LinearLayout
  implements MenuBuilder.ItemInvoker, MenuView
{
  static final int GENERATED_ITEM_PADDING = 4;
  static final int MIN_CELL_SIZE = 56;
  private static final String TAG = "ActionMenuView";
  private MenuPresenter.Callback mActionMenuPresenterCallback;
  private boolean mFormatItems;
  private int mFormatItemsWidth;
  private int mGeneratedItemPadding;
  private MenuBuilder mMenu;
  private MenuBuilder.Callback mMenuBuilderCallback;
  private int mMinCellSize;
  private OnMenuItemClickListener mOnMenuItemClickListener;
  private Context mPopupContext;
  private int mPopupTheme;
  private ActionMenuPresenter mPresenter;
  private boolean mReserveOverflow;
  
  public ActionMenuView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public ActionMenuView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setBaselineAligned(false);
    float f = getResourcesgetDisplayMetricsdensity;
    mMinCellSize = ((int)(56.0F * f));
    mGeneratedItemPadding = ((int)(4.0F * f));
    mPopupContext = paramContext;
    mPopupTheme = 0;
  }
  
  static int measureChildForCells(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(paramInt3) - paramInt4, View.MeasureSpec.getMode(paramInt3));
    ActionMenuItemView localActionMenuItemView;
    if ((paramView instanceof ActionMenuItemView)) {
      localActionMenuItemView = (ActionMenuItemView)paramView;
    } else {
      localActionMenuItemView = null;
    }
    boolean bool1 = false;
    if ((localActionMenuItemView != null) && (localActionMenuItemView.hasText())) {
      paramInt4 = 1;
    } else {
      paramInt4 = 0;
    }
    int j = 0;
    paramInt3 = j;
    if (paramInt2 > 0) {
      if (paramInt4 != 0)
      {
        paramInt3 = j;
        if (paramInt2 < 2) {}
      }
      else
      {
        paramView.measure(View.MeasureSpec.makeMeasureSpec(paramInt1 * paramInt2, Integer.MIN_VALUE), i);
        j = paramView.getMeasuredWidth();
        paramInt3 = j / paramInt1;
        paramInt2 = paramInt3;
        if (j % paramInt1 != 0) {
          paramInt2 = paramInt3 + 1;
        }
        paramInt3 = paramInt2;
        if (paramInt4 != 0)
        {
          paramInt3 = paramInt2;
          if (paramInt2 < 2) {
            paramInt3 = 2;
          }
        }
      }
    }
    boolean bool2 = bool1;
    if (!isOverflowButton)
    {
      bool2 = bool1;
      if (paramInt4 != 0) {
        bool2 = true;
      }
    }
    expandable = bool2;
    cellsUsed = paramInt3;
    paramView.measure(View.MeasureSpec.makeMeasureSpec(paramInt3 * paramInt1, 1073741824), i);
    return paramInt3;
  }
  
  private void onMeasureExactFormat(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getMode(paramInt2);
    paramInt1 = View.MeasureSpec.getSize(paramInt1);
    int j = View.MeasureSpec.getSize(paramInt2);
    int k = getPaddingLeft() + getPaddingRight();
    int m = getPaddingTop() + getPaddingBottom();
    int n = getChildMeasureSpec(paramInt2, m, -2);
    int i1 = paramInt1 - k;
    int i2 = i1 / mMinCellSize;
    int i3 = i1 % mMinCellSize;
    if (i2 == 0)
    {
      setMeasuredDimension(i1, 0);
      return;
    }
    int i4 = mMinCellSize + i3 / i2;
    int i5 = 0;
    long l1 = 0L;
    int i6 = getChildCount();
    int i7 = 0;
    paramInt2 = 0;
    int i8 = 0;
    int i9 = 0;
    paramInt1 = i2;
    Object localObject1;
    Object localObject2;
    int i11;
    int i12;
    for (int i10 = 0; i10 < i6; i10++)
    {
      localObject1 = getChildAt(i10);
      if (((View)localObject1).getVisibility() != 8)
      {
        boolean bool = localObject1 instanceof ActionMenuItemView;
        paramInt2++;
        if (bool) {
          ((View)localObject1).setPadding(mGeneratedItemPadding, 0, mGeneratedItemPadding, 0);
        }
        localObject2 = (LayoutParams)((View)localObject1).getLayoutParams();
        expanded = false;
        extraPixels = 0;
        cellsUsed = 0;
        expandable = false;
        leftMargin = 0;
        rightMargin = 0;
        if ((bool) && (((ActionMenuItemView)localObject1).hasText())) {
          bool = true;
        } else {
          bool = false;
        }
        preventEdgeOffset = bool;
        if (isOverflowButton) {
          i11 = 1;
        } else {
          i11 = paramInt1;
        }
        i12 = measureChildForCells((View)localObject1, i4, i11, n, m);
        i9 = Math.max(i9, i12);
        i11 = i8;
        if (expandable) {
          i11 = i8 + 1;
        }
        if (isOverflowButton) {
          i5 = 1;
        }
        paramInt1 -= i12;
        i7 = Math.max(i7, ((View)localObject1).getMeasuredHeight());
        if (i12 == 1)
        {
          l1 |= 1 << i10;
          i8 = i11;
        }
        else
        {
          i8 = i11;
        }
      }
    }
    if ((i5 != 0) && (paramInt2 == 2)) {
      i11 = 1;
    } else {
      i11 = 0;
    }
    i3 = 0;
    i2 = paramInt1;
    paramInt1 = i3;
    m = i11;
    while ((i8 > 0) && (i2 > 0))
    {
      long l2 = 0L;
      k = Integer.MAX_VALUE;
      i3 = 0;
      i10 = 0;
      i11 = paramInt1;
      paramInt1 = i3;
      while (i10 < i6)
      {
        localObject1 = (LayoutParams)getChildAt(i10).getLayoutParams();
        if (!expandable)
        {
          i3 = paramInt1;
          i12 = k;
          l3 = l2;
        }
        else if (cellsUsed < k)
        {
          i12 = cellsUsed;
          l3 = 1 << i10;
          i3 = 1;
        }
        else
        {
          i3 = paramInt1;
          i12 = k;
          l3 = l2;
          if (cellsUsed == k)
          {
            l3 = 1 << i10;
            i3 = paramInt1 + 1;
            l3 = l2 | l3;
            i12 = k;
          }
        }
        i10++;
        paramInt1 = i3;
        k = i12;
        l2 = l3;
      }
      l1 |= l2;
      if (paramInt1 > i2)
      {
        paramInt1 = i11;
        i8 = paramInt2;
        break label762;
      }
      i3 = 0;
      i11 = i2;
      while (i3 < i6)
      {
        localObject1 = getChildAt(i3);
        localObject2 = (LayoutParams)((View)localObject1).getLayoutParams();
        if ((l2 & 1 << i3) == 0L)
        {
          l3 = l1;
          if (cellsUsed == k + 1) {
            l3 = l1 | 1 << i3;
          }
          l1 = l3;
        }
        else
        {
          if ((m != 0) && (preventEdgeOffset)) {
            if (i11 == 1) {
              ((View)localObject1).setPadding(mGeneratedItemPadding + i4, 0, mGeneratedItemPadding, 0);
            }
          }
          cellsUsed += 1;
          expanded = true;
          i11--;
        }
        i3++;
      }
      paramInt1 = 1;
      i2 = i11;
    }
    i8 = paramInt2;
    label762:
    if ((i5 == 0) && (i8 == 1)) {
      paramInt2 = 1;
    } else {
      paramInt2 = 0;
    }
    if ((i2 > 0) && (l1 != 0L))
    {
      if ((i2 >= i8 - 1) && (paramInt2 == 0) && (i9 <= 1)) {
        break label1129;
      }
      float f1 = Long.bitCount(l1);
      if (paramInt2 == 0)
      {
        float f2 = f1;
        if ((l1 & 1L) != 0L)
        {
          f2 = f1;
          if (!getChildAt0getLayoutParamspreventEdgeOffset) {
            f2 = f1 - 0.5F;
          }
        }
        f1 = f2;
        if ((1 << i6 - 1 & l1) != 0L)
        {
          f1 = f2;
          if (!getChildAt1getLayoutParamspreventEdgeOffset) {
            f1 = f2 - 0.5F;
          }
        }
      }
      if (f1 > 0.0F) {
        i5 = (int)(i2 * i4 / f1);
      } else {
        i5 = 0;
      }
      i11 = 0;
      i8 = paramInt2;
      while (i11 < i6)
      {
        if ((1 << i11 & l1) != 0L)
        {
          localObject2 = getChildAt(i11);
          localObject1 = (LayoutParams)((View)localObject2).getLayoutParams();
          if ((localObject2 instanceof ActionMenuItemView))
          {
            extraPixels = i5;
            expanded = true;
            if ((i11 == 0) && (!preventEdgeOffset)) {
              leftMargin = (-i5 / 2);
            }
            paramInt1 = 1;
          }
        }
        else
        {
          paramInt2 = paramInt1;
          break label1118;
        }
        if (isOverflowButton)
        {
          extraPixels = i5;
          expanded = true;
          rightMargin = (-i5 / 2);
          paramInt2 = 1;
        }
        else
        {
          if (i11 != 0) {
            leftMargin = (i5 / 2);
          }
          paramInt2 = paramInt1;
          if (i11 != i6 - 1)
          {
            rightMargin = (i5 / 2);
            paramInt2 = paramInt1;
          }
        }
        label1118:
        i11++;
        paramInt1 = paramInt2;
      }
    }
    label1129:
    long l3 = l1;
    if (paramInt1 != 0) {
      for (paramInt1 = 0;; paramInt1++)
      {
        l3 = l1;
        if (paramInt1 >= i6) {
          break;
        }
        localObject1 = getChildAt(paramInt1);
        localObject2 = (LayoutParams)((View)localObject1).getLayoutParams();
        if (expanded) {
          ((View)localObject1).measure(View.MeasureSpec.makeMeasureSpec(cellsUsed * i4 + extraPixels, 1073741824), n);
        }
      }
    }
    if (i != 1073741824) {
      paramInt1 = i7;
    } else {
      paramInt1 = j;
    }
    setMeasuredDimension(i1, paramInt1);
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    boolean bool;
    if ((paramLayoutParams != null) && ((paramLayoutParams instanceof LayoutParams))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void dismissPopupMenus()
  {
    if (mPresenter != null) {
      mPresenter.dismissPopupMenus();
    }
  }
  
  public boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent paramAccessibilityEvent)
  {
    return false;
  }
  
  protected LayoutParams generateDefaultLayoutParams()
  {
    LayoutParams localLayoutParams = new LayoutParams(-2, -2);
    gravity = 16;
    return localLayoutParams;
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    if (paramLayoutParams != null)
    {
      if ((paramLayoutParams instanceof LayoutParams)) {
        paramLayoutParams = new LayoutParams((LayoutParams)paramLayoutParams);
      } else {
        paramLayoutParams = new LayoutParams(paramLayoutParams);
      }
      if (gravity <= 0) {
        gravity = 16;
      }
      return paramLayoutParams;
    }
    return generateDefaultLayoutParams();
  }
  
  public LayoutParams generateOverflowButtonLayoutParams()
  {
    LayoutParams localLayoutParams = generateDefaultLayoutParams();
    isOverflowButton = true;
    return localLayoutParams;
  }
  
  public Menu getMenu()
  {
    if (mMenu == null)
    {
      Object localObject = getContext();
      mMenu = new MenuBuilder((Context)localObject);
      mMenu.setCallback(new MenuBuilderCallback(null));
      mPresenter = new ActionMenuPresenter((Context)localObject);
      mPresenter.setReserveOverflow(true);
      ActionMenuPresenter localActionMenuPresenter = mPresenter;
      if (mActionMenuPresenterCallback != null) {
        localObject = mActionMenuPresenterCallback;
      } else {
        localObject = new ActionMenuPresenterCallback(null);
      }
      localActionMenuPresenter.setCallback((MenuPresenter.Callback)localObject);
      mMenu.addMenuPresenter(mPresenter, mPopupContext);
      mPresenter.setMenuView(this);
    }
    return mMenu;
  }
  
  public Drawable getOverflowIcon()
  {
    getMenu();
    return mPresenter.getOverflowIcon();
  }
  
  public int getPopupTheme()
  {
    return mPopupTheme;
  }
  
  public int getWindowAnimations()
  {
    return 0;
  }
  
  protected boolean hasDividerBeforeChildAt(int paramInt)
  {
    if (paramInt == 0) {
      return false;
    }
    View localView1 = getChildAt(paramInt - 1);
    View localView2 = getChildAt(paramInt);
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (paramInt < getChildCount())
    {
      bool2 = bool1;
      if ((localView1 instanceof ActionMenuChildView)) {
        bool2 = false | ((ActionMenuChildView)localView1).needsDividerAfter();
      }
    }
    bool1 = bool2;
    if (paramInt > 0)
    {
      bool1 = bool2;
      if ((localView2 instanceof ActionMenuChildView)) {
        bool1 = bool2 | ((ActionMenuChildView)localView2).needsDividerBefore();
      }
    }
    return bool1;
  }
  
  public boolean hideOverflowMenu()
  {
    boolean bool;
    if ((mPresenter != null) && (mPresenter.hideOverflowMenu())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void initialize(MenuBuilder paramMenuBuilder)
  {
    mMenu = paramMenuBuilder;
  }
  
  public boolean invokeItem(MenuItemImpl paramMenuItemImpl)
  {
    return mMenu.performItemAction(paramMenuItemImpl, 0);
  }
  
  public boolean isOverflowMenuShowPending()
  {
    boolean bool;
    if ((mPresenter != null) && (mPresenter.isOverflowMenuShowPending())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isOverflowMenuShowing()
  {
    boolean bool;
    if ((mPresenter != null) && (mPresenter.isOverflowMenuShowing())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isOverflowReserved()
  {
    return mReserveOverflow;
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    if (mPresenter != null)
    {
      mPresenter.updateMenuView(false);
      if (mPresenter.isOverflowMenuShowing())
      {
        mPresenter.hideOverflowMenu();
        mPresenter.showOverflowMenu();
      }
    }
  }
  
  public void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    dismissPopupMenus();
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (!mFormatItems)
    {
      super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
      return;
    }
    int i = getChildCount();
    int j = (paramInt4 - paramInt2) / 2;
    int k = getDividerWidth();
    int m = 0;
    paramInt4 = getPaddingRight();
    paramInt2 = getPaddingLeft();
    int n = 0;
    paramBoolean = isLayoutRtl();
    paramInt4 = paramInt3 - paramInt1 - paramInt4 - paramInt2;
    int i1 = 0;
    paramInt2 = 0;
    Object localObject1;
    Object localObject2;
    int i4;
    for (int i2 = 0; i2 < i; i2++)
    {
      localObject1 = getChildAt(i2);
      if (((View)localObject1).getVisibility() != 8)
      {
        localObject2 = (LayoutParams)((View)localObject1).getLayoutParams();
        int i3;
        if (isOverflowButton)
        {
          n = ((View)localObject1).getMeasuredWidth();
          paramInt2 = n;
          if (hasDividerBeforeChildAt(i2)) {
            paramInt2 = n + k;
          }
          i3 = ((View)localObject1).getMeasuredHeight();
          if (paramBoolean)
          {
            n = getPaddingLeft() + leftMargin;
            i4 = n + paramInt2;
          }
          else
          {
            i4 = getWidth() - getPaddingRight() - rightMargin;
            n = i4 - paramInt2;
          }
          int i5 = j - i3 / 2;
          ((View)localObject1).layout(n, i5, i4, i5 + i3);
          paramInt4 -= paramInt2;
          n = 1;
        }
        else
        {
          i3 = ((View)localObject1).getMeasuredWidth() + leftMargin + rightMargin;
          i4 = i1 + i3;
          paramInt4 -= i3;
          i1 = i4;
          if (hasDividerBeforeChildAt(i2)) {
            i1 = i4 + k;
          }
          m++;
        }
      }
    }
    i1 = 1;
    if ((i == 1) && (n == 0))
    {
      localObject2 = getChildAt(0);
      paramInt4 = ((View)localObject2).getMeasuredWidth();
      paramInt2 = ((View)localObject2).getMeasuredHeight();
      paramInt1 = (paramInt3 - paramInt1) / 2 - paramInt4 / 2;
      paramInt3 = j - paramInt2 / 2;
      ((View)localObject2).layout(paramInt1, paramInt3, paramInt1 + paramInt4, paramInt3 + paramInt2);
      return;
    }
    paramInt1 = i1;
    if (n != 0) {
      paramInt1 = 0;
    }
    paramInt3 = m - paramInt1;
    if (paramInt3 > 0) {
      paramInt1 = paramInt4 / paramInt3;
    } else {
      paramInt1 = 0;
    }
    i1 = 0;
    paramInt4 = 0;
    m = Math.max(0, paramInt1);
    if (paramBoolean)
    {
      i1 = getWidth() - getPaddingRight();
      paramInt1 = k;
      while (paramInt4 < i)
      {
        localObject2 = getChildAt(paramInt4);
        localObject1 = (LayoutParams)((View)localObject2).getLayoutParams();
        if ((((View)localObject2).getVisibility() != 8) && (!isOverflowButton))
        {
          i1 -= rightMargin;
          n = ((View)localObject2).getMeasuredWidth();
          i4 = ((View)localObject2).getMeasuredHeight();
          i2 = j - i4 / 2;
          ((View)localObject2).layout(i1 - n, i2, i1, i2 + i4);
          i1 -= leftMargin + n + m;
        }
        paramInt4++;
      }
    }
    else
    {
      paramInt2 = getPaddingLeft();
      paramInt1 = i1;
      while (paramInt1 < i)
      {
        localObject1 = getChildAt(paramInt1);
        localObject2 = (LayoutParams)((View)localObject1).getLayoutParams();
        paramInt3 = paramInt2;
        if (((View)localObject1).getVisibility() != 8) {
          if (isOverflowButton)
          {
            paramInt3 = paramInt2;
          }
          else
          {
            paramInt2 += leftMargin;
            i1 = ((View)localObject1).getMeasuredWidth();
            paramInt3 = ((View)localObject1).getMeasuredHeight();
            paramInt4 = j - paramInt3 / 2;
            ((View)localObject1).layout(paramInt2, paramInt4, paramInt2 + i1, paramInt4 + paramInt3);
            paramInt3 = paramInt2 + (rightMargin + i1 + m);
          }
        }
        paramInt1++;
        paramInt2 = paramInt3;
      }
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    boolean bool1 = mFormatItems;
    boolean bool2;
    if (View.MeasureSpec.getMode(paramInt1) == 1073741824) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mFormatItems = bool2;
    if (bool1 != mFormatItems) {
      mFormatItemsWidth = 0;
    }
    int i = View.MeasureSpec.getSize(paramInt1);
    if ((mFormatItems) && (mMenu != null) && (i != mFormatItemsWidth))
    {
      mFormatItemsWidth = i;
      mMenu.onItemsChanged(true);
    }
    int j = getChildCount();
    if ((mFormatItems) && (j > 0))
    {
      onMeasureExactFormat(paramInt1, paramInt2);
    }
    else
    {
      for (i = 0; i < j; i++)
      {
        LayoutParams localLayoutParams = (LayoutParams)getChildAt(i).getLayoutParams();
        rightMargin = 0;
        leftMargin = 0;
      }
      super.onMeasure(paramInt1, paramInt2);
    }
  }
  
  public MenuBuilder peekMenu()
  {
    return mMenu;
  }
  
  public void setExpandedActionViewsExclusive(boolean paramBoolean)
  {
    mPresenter.setExpandedActionViewsExclusive(paramBoolean);
  }
  
  public void setMenuCallbacks(MenuPresenter.Callback paramCallback, MenuBuilder.Callback paramCallback1)
  {
    mActionMenuPresenterCallback = paramCallback;
    mMenuBuilderCallback = paramCallback1;
  }
  
  public void setOnMenuItemClickListener(OnMenuItemClickListener paramOnMenuItemClickListener)
  {
    mOnMenuItemClickListener = paramOnMenuItemClickListener;
  }
  
  public void setOverflowIcon(Drawable paramDrawable)
  {
    getMenu();
    mPresenter.setOverflowIcon(paramDrawable);
  }
  
  public void setOverflowReserved(boolean paramBoolean)
  {
    mReserveOverflow = paramBoolean;
  }
  
  public void setPopupTheme(int paramInt)
  {
    if (mPopupTheme != paramInt)
    {
      mPopupTheme = paramInt;
      if (paramInt == 0) {
        mPopupContext = mContext;
      } else {
        mPopupContext = new ContextThemeWrapper(mContext, paramInt);
      }
    }
  }
  
  public void setPresenter(ActionMenuPresenter paramActionMenuPresenter)
  {
    mPresenter = paramActionMenuPresenter;
    mPresenter.setMenuView(this);
  }
  
  public boolean showOverflowMenu()
  {
    boolean bool;
    if ((mPresenter != null) && (mPresenter.showOverflowMenu())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static abstract interface ActionMenuChildView
  {
    public abstract boolean needsDividerAfter();
    
    public abstract boolean needsDividerBefore();
  }
  
  private class ActionMenuPresenterCallback
    implements MenuPresenter.Callback
  {
    private ActionMenuPresenterCallback() {}
    
    public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean) {}
    
    public boolean onOpenSubMenu(MenuBuilder paramMenuBuilder)
    {
      return false;
    }
  }
  
  public static class LayoutParams
    extends LinearLayout.LayoutParams
  {
    @ViewDebug.ExportedProperty(category="layout")
    public int cellsUsed;
    @ViewDebug.ExportedProperty(category="layout")
    public boolean expandable;
    public boolean expanded;
    @ViewDebug.ExportedProperty(category="layout")
    public int extraPixels;
    @ViewDebug.ExportedProperty(category="layout")
    public boolean isOverflowButton;
    @ViewDebug.ExportedProperty(category="layout")
    public boolean preventEdgeOffset;
    
    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
      isOverflowButton = false;
    }
    
    public LayoutParams(int paramInt1, int paramInt2, boolean paramBoolean)
    {
      super(paramInt2);
      isOverflowButton = paramBoolean;
    }
    
    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
    }
    
    public LayoutParams(ViewGroup.LayoutParams paramLayoutParams)
    {
      super();
    }
    
    public LayoutParams(LayoutParams paramLayoutParams)
    {
      super();
      isOverflowButton = isOverflowButton;
    }
    
    protected void encodeProperties(ViewHierarchyEncoder paramViewHierarchyEncoder)
    {
      super.encodeProperties(paramViewHierarchyEncoder);
      paramViewHierarchyEncoder.addProperty("layout:overFlowButton", isOverflowButton);
      paramViewHierarchyEncoder.addProperty("layout:cellsUsed", cellsUsed);
      paramViewHierarchyEncoder.addProperty("layout:extraPixels", extraPixels);
      paramViewHierarchyEncoder.addProperty("layout:expandable", expandable);
      paramViewHierarchyEncoder.addProperty("layout:preventEdgeOffset", preventEdgeOffset);
    }
  }
  
  private class MenuBuilderCallback
    implements MenuBuilder.Callback
  {
    private MenuBuilderCallback() {}
    
    public boolean onMenuItemSelected(MenuBuilder paramMenuBuilder, MenuItem paramMenuItem)
    {
      boolean bool;
      if ((mOnMenuItemClickListener != null) && (mOnMenuItemClickListener.onMenuItemClick(paramMenuItem))) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void onMenuModeChange(MenuBuilder paramMenuBuilder)
    {
      if (mMenuBuilderCallback != null) {
        mMenuBuilderCallback.onMenuModeChange(paramMenuBuilder);
      }
    }
  }
  
  public static abstract interface OnMenuItemClickListener
  {
    public abstract boolean onMenuItemClick(MenuItem paramMenuItem);
  }
}
