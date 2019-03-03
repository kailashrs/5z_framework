package android.widget;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.Layout;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.CollapsibleActionView;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import com.android.internal.R.styleable;
import com.android.internal.view.menu.MenuBuilder;
import com.android.internal.view.menu.MenuBuilder.Callback;
import com.android.internal.view.menu.MenuItemImpl;
import com.android.internal.view.menu.MenuPresenter;
import com.android.internal.view.menu.MenuPresenter.Callback;
import com.android.internal.view.menu.MenuView;
import com.android.internal.view.menu.SubMenuBuilder;
import com.android.internal.widget.DecorToolbar;
import com.android.internal.widget.ToolbarWidgetWrapper;
import java.util.ArrayList;
import java.util.List;

public class Toolbar
  extends ViewGroup
{
  private static final String TAG = "Toolbar";
  private MenuPresenter.Callback mActionMenuPresenterCallback;
  private int mButtonGravity;
  private ImageButton mCollapseButtonView;
  private CharSequence mCollapseDescription;
  private Drawable mCollapseIcon;
  private boolean mCollapsible;
  private int mContentInsetEndWithActions;
  private int mContentInsetStartWithNavigation;
  private RtlSpacingHelper mContentInsets;
  private boolean mEatingTouch;
  View mExpandedActionView;
  private ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
  private int mGravity = 8388627;
  private final ArrayList<View> mHiddenViews = new ArrayList();
  private ImageView mLogoView;
  private int mMaxButtonHeight;
  private MenuBuilder.Callback mMenuBuilderCallback;
  private ActionMenuView mMenuView;
  private final ActionMenuView.OnMenuItemClickListener mMenuViewItemClickListener = new ActionMenuView.OnMenuItemClickListener()
  {
    public boolean onMenuItemClick(MenuItem paramAnonymousMenuItem)
    {
      if (mOnMenuItemClickListener != null) {
        return mOnMenuItemClickListener.onMenuItemClick(paramAnonymousMenuItem);
      }
      return false;
    }
  };
  private int mNavButtonStyle;
  private ImageButton mNavButtonView;
  private OnMenuItemClickListener mOnMenuItemClickListener;
  private ActionMenuPresenter mOuterActionMenuPresenter;
  private Context mPopupContext;
  private int mPopupTheme;
  private final Runnable mShowOverflowMenuRunnable = new Runnable()
  {
    public void run()
    {
      showOverflowMenu();
    }
  };
  private CharSequence mSubtitleText;
  private int mSubtitleTextAppearance;
  private int mSubtitleTextColor;
  private TextView mSubtitleTextView;
  private final int[] mTempMargins = new int[2];
  private final ArrayList<View> mTempViews = new ArrayList();
  private int mTitleMarginBottom;
  private int mTitleMarginEnd;
  private int mTitleMarginStart;
  private int mTitleMarginTop;
  private CharSequence mTitleText;
  private int mTitleTextAppearance;
  private int mTitleTextColor;
  private TextView mTitleTextView;
  private ToolbarWidgetWrapper mWrapper;
  
  public Toolbar(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public Toolbar(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16843946);
  }
  
  public Toolbar(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public Toolbar(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.Toolbar, paramInt1, paramInt2);
    mTitleTextAppearance = paramContext.getResourceId(4, 0);
    mSubtitleTextAppearance = paramContext.getResourceId(5, 0);
    mNavButtonStyle = paramContext.getResourceId(27, 0);
    mGravity = paramContext.getInteger(0, mGravity);
    mButtonGravity = paramContext.getInteger(23, 48);
    paramInt1 = paramContext.getDimensionPixelOffset(17, 0);
    mTitleMarginBottom = paramInt1;
    mTitleMarginTop = paramInt1;
    mTitleMarginEnd = paramInt1;
    mTitleMarginStart = paramInt1;
    paramInt1 = paramContext.getDimensionPixelOffset(18, -1);
    if (paramInt1 >= 0) {
      mTitleMarginStart = paramInt1;
    }
    paramInt1 = paramContext.getDimensionPixelOffset(19, -1);
    if (paramInt1 >= 0) {
      mTitleMarginEnd = paramInt1;
    }
    paramInt1 = paramContext.getDimensionPixelOffset(20, -1);
    if (paramInt1 >= 0) {
      mTitleMarginTop = paramInt1;
    }
    paramInt1 = paramContext.getDimensionPixelOffset(21, -1);
    if (paramInt1 >= 0) {
      mTitleMarginBottom = paramInt1;
    }
    mMaxButtonHeight = paramContext.getDimensionPixelSize(22, -1);
    int i = paramContext.getDimensionPixelOffset(6, Integer.MIN_VALUE);
    int j = paramContext.getDimensionPixelOffset(7, Integer.MIN_VALUE);
    paramInt1 = paramContext.getDimensionPixelSize(8, 0);
    paramInt2 = paramContext.getDimensionPixelSize(9, 0);
    ensureContentInsets();
    mContentInsets.setAbsolute(paramInt1, paramInt2);
    if ((i != Integer.MIN_VALUE) || (j != Integer.MIN_VALUE)) {
      mContentInsets.setRelative(i, j);
    }
    mContentInsetStartWithNavigation = paramContext.getDimensionPixelOffset(25, Integer.MIN_VALUE);
    mContentInsetEndWithActions = paramContext.getDimensionPixelOffset(26, Integer.MIN_VALUE);
    mCollapseIcon = paramContext.getDrawable(24);
    mCollapseDescription = paramContext.getText(13);
    paramAttributeSet = paramContext.getText(1);
    if (!TextUtils.isEmpty(paramAttributeSet)) {
      setTitle(paramAttributeSet);
    }
    paramAttributeSet = paramContext.getText(3);
    if (!TextUtils.isEmpty(paramAttributeSet)) {
      setSubtitle(paramAttributeSet);
    }
    mPopupContext = mContext;
    setPopupTheme(paramContext.getResourceId(10, 0));
    paramAttributeSet = paramContext.getDrawable(11);
    if (paramAttributeSet != null) {
      setNavigationIcon(paramAttributeSet);
    }
    paramAttributeSet = paramContext.getText(12);
    if (!TextUtils.isEmpty(paramAttributeSet)) {
      setNavigationContentDescription(paramAttributeSet);
    }
    paramAttributeSet = paramContext.getDrawable(2);
    if (paramAttributeSet != null) {
      setLogo(paramAttributeSet);
    }
    paramAttributeSet = paramContext.getText(16);
    if (!TextUtils.isEmpty(paramAttributeSet)) {
      setLogoDescription(paramAttributeSet);
    }
    if (paramContext.hasValue(14)) {
      setTitleTextColor(paramContext.getColor(14, -1));
    }
    if (paramContext.hasValue(15)) {
      setSubtitleTextColor(paramContext.getColor(15, -1));
    }
    paramContext.recycle();
  }
  
  private void addCustomViewsWithGravity(List<View> paramList, int paramInt)
  {
    int i = getLayoutDirection();
    int j = 0;
    int k = 1;
    if (i != 1) {
      k = 0;
    }
    int m = getChildCount();
    i = Gravity.getAbsoluteGravity(paramInt, getLayoutDirection());
    paramList.clear();
    Object localObject1;
    Object localObject2;
    if (k != 0) {
      for (paramInt = m - 1; paramInt >= 0; paramInt--)
      {
        localObject1 = getChildAt(paramInt);
        localObject2 = (LayoutParams)((View)localObject1).getLayoutParams();
        if ((mViewType == 0) && (shouldLayout((View)localObject1)) && (getChildHorizontalGravity(gravity) == i)) {
          paramList.add(localObject1);
        }
      }
    }
    for (paramInt = j; paramInt < m; paramInt++)
    {
      localObject2 = getChildAt(paramInt);
      localObject1 = (LayoutParams)((View)localObject2).getLayoutParams();
      if ((mViewType == 0) && (shouldLayout((View)localObject2)) && (getChildHorizontalGravity(gravity) == i)) {
        paramList.add(localObject2);
      }
    }
  }
  
  private void addSystemView(View paramView, boolean paramBoolean)
  {
    Object localObject = paramView.getLayoutParams();
    if (localObject == null) {
      localObject = generateDefaultLayoutParams();
    }
    for (;;)
    {
      break;
      if (!checkLayoutParams((ViewGroup.LayoutParams)localObject)) {
        localObject = generateLayoutParams((ViewGroup.LayoutParams)localObject);
      } else {
        localObject = (LayoutParams)localObject;
      }
    }
    mViewType = 1;
    if ((paramBoolean) && (mExpandedActionView != null))
    {
      paramView.setLayoutParams((ViewGroup.LayoutParams)localObject);
      mHiddenViews.add(paramView);
    }
    else
    {
      addView(paramView, (ViewGroup.LayoutParams)localObject);
    }
  }
  
  private void ensureCollapseButtonView()
  {
    if (mCollapseButtonView == null)
    {
      mCollapseButtonView = new ImageButton(getContext(), null, 0, mNavButtonStyle);
      mCollapseButtonView.setImageDrawable(mCollapseIcon);
      mCollapseButtonView.setContentDescription(mCollapseDescription);
      LayoutParams localLayoutParams = generateDefaultLayoutParams();
      gravity = (0x800003 | mButtonGravity & 0x70);
      mViewType = 2;
      mCollapseButtonView.setLayoutParams(localLayoutParams);
      mCollapseButtonView.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          collapseActionView();
        }
      });
    }
  }
  
  private void ensureContentInsets()
  {
    if (mContentInsets == null) {
      mContentInsets = new RtlSpacingHelper();
    }
  }
  
  private void ensureLogoView()
  {
    if (mLogoView == null) {
      mLogoView = new ImageView(getContext());
    }
  }
  
  private void ensureMenu()
  {
    ensureMenuView();
    if (mMenuView.peekMenu() == null)
    {
      MenuBuilder localMenuBuilder = (MenuBuilder)mMenuView.getMenu();
      if (mExpandedMenuPresenter == null) {
        mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter(null);
      }
      mMenuView.setExpandedActionViewsExclusive(true);
      localMenuBuilder.addMenuPresenter(mExpandedMenuPresenter, mPopupContext);
    }
  }
  
  private void ensureMenuView()
  {
    if (mMenuView == null)
    {
      mMenuView = new ActionMenuView(getContext());
      mMenuView.setPopupTheme(mPopupTheme);
      mMenuView.setOnMenuItemClickListener(mMenuViewItemClickListener);
      mMenuView.setMenuCallbacks(mActionMenuPresenterCallback, mMenuBuilderCallback);
      LayoutParams localLayoutParams = generateDefaultLayoutParams();
      gravity = (0x800005 | mButtonGravity & 0x70);
      mMenuView.setLayoutParams(localLayoutParams);
      addSystemView(mMenuView, false);
    }
  }
  
  private void ensureNavButtonView()
  {
    if (mNavButtonView == null)
    {
      mNavButtonView = new ImageButton(getContext(), null, 0, mNavButtonStyle);
      LayoutParams localLayoutParams = generateDefaultLayoutParams();
      gravity = (0x800003 | mButtonGravity & 0x70);
      mNavButtonView.setLayoutParams(localLayoutParams);
    }
  }
  
  private int getChildHorizontalGravity(int paramInt)
  {
    int i = getLayoutDirection();
    int j = Gravity.getAbsoluteGravity(paramInt, i) & 0x7;
    if (j != 1)
    {
      paramInt = 3;
      if ((j != 3) && (j != 5))
      {
        if (i == 1) {
          paramInt = 5;
        }
        return paramInt;
      }
    }
    return j;
  }
  
  private int getChildTop(View paramView, int paramInt)
  {
    LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = paramView.getMeasuredHeight();
    if (paramInt > 0) {
      paramInt = (i - paramInt) / 2;
    } else {
      paramInt = 0;
    }
    int j = getChildVerticalGravity(gravity);
    if (j != 48)
    {
      if (j != 80)
      {
        int k = getPaddingTop();
        int m = getPaddingBottom();
        paramInt = getHeight();
        j = (paramInt - k - m - i) / 2;
        if (j < topMargin)
        {
          paramInt = topMargin;
        }
        else
        {
          i = paramInt - m - i - j - k;
          paramInt = j;
          if (i < bottomMargin) {
            paramInt = Math.max(0, j - (bottomMargin - i));
          }
        }
        return k + paramInt;
      }
      return getHeight() - getPaddingBottom() - i - bottomMargin - paramInt;
    }
    return getPaddingTop() - paramInt;
  }
  
  private int getChildVerticalGravity(int paramInt)
  {
    paramInt &= 0x70;
    if ((paramInt != 16) && (paramInt != 48) && (paramInt != 80)) {
      return mGravity & 0x70;
    }
    return paramInt;
  }
  
  private int getHorizontalMargins(View paramView)
  {
    paramView = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    return paramView.getMarginStart() + paramView.getMarginEnd();
  }
  
  private MenuInflater getMenuInflater()
  {
    return new MenuInflater(getContext());
  }
  
  private int getVerticalMargins(View paramView)
  {
    paramView = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    return topMargin + bottomMargin;
  }
  
  private int getViewListMeasuredWidth(List<View> paramList, int[] paramArrayOfInt)
  {
    int i = paramArrayOfInt[0];
    int j = paramArrayOfInt[1];
    int k = paramList.size();
    int m = 0;
    for (int n = 0; n < k; n++)
    {
      paramArrayOfInt = (View)paramList.get(n);
      LayoutParams localLayoutParams = (LayoutParams)paramArrayOfInt.getLayoutParams();
      i = leftMargin - i;
      j = rightMargin - j;
      int i1 = Math.max(0, i);
      int i2 = Math.max(0, j);
      i = Math.max(0, -i);
      j = Math.max(0, -j);
      m += paramArrayOfInt.getMeasuredWidth() + i1 + i2;
    }
    return m;
  }
  
  private boolean isChildOrHidden(View paramView)
  {
    boolean bool;
    if ((paramView.getParent() != this) && (!mHiddenViews.contains(paramView))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private static boolean isCustomView(View paramView)
  {
    boolean bool;
    if (getLayoutParamsmViewType == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private int layoutChildLeft(View paramView, int paramInt1, int[] paramArrayOfInt, int paramInt2)
  {
    LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = leftMargin - paramArrayOfInt[0];
    paramInt1 += Math.max(0, i);
    paramArrayOfInt[0] = Math.max(0, -i);
    i = getChildTop(paramView, paramInt2);
    paramInt2 = paramView.getMeasuredWidth();
    paramView.layout(paramInt1, i, paramInt1 + paramInt2, paramView.getMeasuredHeight() + i);
    return paramInt1 + (rightMargin + paramInt2);
  }
  
  private int layoutChildRight(View paramView, int paramInt1, int[] paramArrayOfInt, int paramInt2)
  {
    LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = rightMargin - paramArrayOfInt[1];
    paramInt1 -= Math.max(0, i);
    paramArrayOfInt[1] = Math.max(0, -i);
    paramInt2 = getChildTop(paramView, paramInt2);
    i = paramView.getMeasuredWidth();
    paramView.layout(paramInt1 - i, paramInt2, paramInt1, paramView.getMeasuredHeight() + paramInt2);
    return paramInt1 - (leftMargin + i);
  }
  
  private int measureChildCollapseMargins(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt)
  {
    ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    int i = leftMargin - paramArrayOfInt[0];
    int j = rightMargin - paramArrayOfInt[1];
    int k = Math.max(0, i) + Math.max(0, j);
    paramArrayOfInt[0] = Math.max(0, -i);
    paramArrayOfInt[1] = Math.max(0, -j);
    paramView.measure(getChildMeasureSpec(paramInt1, mPaddingLeft + mPaddingRight + k + paramInt2, width), getChildMeasureSpec(paramInt3, mPaddingTop + mPaddingBottom + topMargin + bottomMargin + paramInt4, height));
    return paramView.getMeasuredWidth() + k;
  }
  
  private void measureChildConstrained(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    int i = getChildMeasureSpec(paramInt1, mPaddingLeft + mPaddingRight + leftMargin + rightMargin + paramInt2, width);
    paramInt2 = getChildMeasureSpec(paramInt3, mPaddingTop + mPaddingBottom + topMargin + bottomMargin + paramInt4, height);
    paramInt3 = View.MeasureSpec.getMode(paramInt2);
    paramInt1 = paramInt2;
    if (paramInt3 != 1073741824)
    {
      paramInt1 = paramInt2;
      if (paramInt5 >= 0)
      {
        if (paramInt3 != 0) {
          paramInt1 = Math.min(View.MeasureSpec.getSize(paramInt2), paramInt5);
        } else {
          paramInt1 = paramInt5;
        }
        paramInt1 = View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824);
      }
    }
    paramView.measure(i, paramInt1);
  }
  
  private void postShowOverflowMenu()
  {
    removeCallbacks(mShowOverflowMenuRunnable);
    post(mShowOverflowMenuRunnable);
  }
  
  private boolean shouldCollapse()
  {
    if (!mCollapsible) {
      return false;
    }
    int i = getChildCount();
    for (int j = 0; j < i; j++)
    {
      View localView = getChildAt(j);
      if ((shouldLayout(localView)) && (localView.getMeasuredWidth() > 0) && (localView.getMeasuredHeight() > 0)) {
        return false;
      }
    }
    return true;
  }
  
  private boolean shouldLayout(View paramView)
  {
    boolean bool;
    if ((paramView != null) && (paramView.getParent() == this) && (paramView.getVisibility() != 8)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  void addChildrenForExpandedActionView()
  {
    for (int i = mHiddenViews.size() - 1; i >= 0; i--) {
      addView((View)mHiddenViews.get(i));
    }
    mHiddenViews.clear();
  }
  
  public boolean canShowOverflowMenu()
  {
    boolean bool;
    if ((getVisibility() == 0) && (mMenuView != null) && (mMenuView.isOverflowReserved())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    boolean bool;
    if ((super.checkLayoutParams(paramLayoutParams)) && ((paramLayoutParams instanceof LayoutParams))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void collapseActionView()
  {
    MenuItemImpl localMenuItemImpl;
    if (mExpandedMenuPresenter == null) {
      localMenuItemImpl = null;
    } else {
      localMenuItemImpl = mExpandedMenuPresenter.mCurrentExpandedItem;
    }
    if (localMenuItemImpl != null) {
      localMenuItemImpl.collapseActionView();
    }
  }
  
  public void dismissPopupMenus()
  {
    if (mMenuView != null) {
      mMenuView.dismissPopupMenus();
    }
  }
  
  protected LayoutParams generateDefaultLayoutParams()
  {
    return new LayoutParams(-2, -2);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    if ((paramLayoutParams instanceof LayoutParams)) {
      return new LayoutParams((LayoutParams)paramLayoutParams);
    }
    if ((paramLayoutParams instanceof ActionBar.LayoutParams)) {
      return new LayoutParams((ActionBar.LayoutParams)paramLayoutParams);
    }
    if ((paramLayoutParams instanceof ViewGroup.MarginLayoutParams)) {
      return new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams);
    }
    return new LayoutParams(paramLayoutParams);
  }
  
  public int getContentInsetEnd()
  {
    int i;
    if (mContentInsets != null) {
      i = mContentInsets.getEnd();
    } else {
      i = 0;
    }
    return i;
  }
  
  public int getContentInsetEndWithActions()
  {
    int i;
    if (mContentInsetEndWithActions != Integer.MIN_VALUE) {
      i = mContentInsetEndWithActions;
    } else {
      i = getContentInsetEnd();
    }
    return i;
  }
  
  public int getContentInsetLeft()
  {
    int i;
    if (mContentInsets != null) {
      i = mContentInsets.getLeft();
    } else {
      i = 0;
    }
    return i;
  }
  
  public int getContentInsetRight()
  {
    int i;
    if (mContentInsets != null) {
      i = mContentInsets.getRight();
    } else {
      i = 0;
    }
    return i;
  }
  
  public int getContentInsetStart()
  {
    int i;
    if (mContentInsets != null) {
      i = mContentInsets.getStart();
    } else {
      i = 0;
    }
    return i;
  }
  
  public int getContentInsetStartWithNavigation()
  {
    int i;
    if (mContentInsetStartWithNavigation != Integer.MIN_VALUE) {
      i = mContentInsetStartWithNavigation;
    } else {
      i = getContentInsetStart();
    }
    return i;
  }
  
  public int getCurrentContentInsetEnd()
  {
    int i = 0;
    if (mMenuView != null)
    {
      MenuBuilder localMenuBuilder = mMenuView.peekMenu();
      if ((localMenuBuilder != null) && (localMenuBuilder.hasVisibleItems())) {
        i = 1;
      } else {
        i = 0;
      }
    }
    if (i != 0) {
      i = Math.max(getContentInsetEnd(), Math.max(mContentInsetEndWithActions, 0));
    } else {
      i = getContentInsetEnd();
    }
    return i;
  }
  
  public int getCurrentContentInsetLeft()
  {
    int i;
    if (isLayoutRtl()) {
      i = getCurrentContentInsetEnd();
    } else {
      i = getCurrentContentInsetStart();
    }
    return i;
  }
  
  public int getCurrentContentInsetRight()
  {
    int i;
    if (isLayoutRtl()) {
      i = getCurrentContentInsetStart();
    } else {
      i = getCurrentContentInsetEnd();
    }
    return i;
  }
  
  public int getCurrentContentInsetStart()
  {
    int i;
    if (getNavigationIcon() != null) {
      i = Math.max(getContentInsetStart(), Math.max(mContentInsetStartWithNavigation, 0));
    } else {
      i = getContentInsetStart();
    }
    return i;
  }
  
  public Drawable getLogo()
  {
    Drawable localDrawable;
    if (mLogoView != null) {
      localDrawable = mLogoView.getDrawable();
    } else {
      localDrawable = null;
    }
    return localDrawable;
  }
  
  public CharSequence getLogoDescription()
  {
    CharSequence localCharSequence;
    if (mLogoView != null) {
      localCharSequence = mLogoView.getContentDescription();
    } else {
      localCharSequence = null;
    }
    return localCharSequence;
  }
  
  public Menu getMenu()
  {
    ensureMenu();
    return mMenuView.getMenu();
  }
  
  public CharSequence getNavigationContentDescription()
  {
    CharSequence localCharSequence;
    if (mNavButtonView != null) {
      localCharSequence = mNavButtonView.getContentDescription();
    } else {
      localCharSequence = null;
    }
    return localCharSequence;
  }
  
  public Drawable getNavigationIcon()
  {
    Drawable localDrawable;
    if (mNavButtonView != null) {
      localDrawable = mNavButtonView.getDrawable();
    } else {
      localDrawable = null;
    }
    return localDrawable;
  }
  
  public View getNavigationView()
  {
    return mNavButtonView;
  }
  
  ActionMenuPresenter getOuterActionMenuPresenter()
  {
    return mOuterActionMenuPresenter;
  }
  
  public Drawable getOverflowIcon()
  {
    ensureMenu();
    return mMenuView.getOverflowIcon();
  }
  
  Context getPopupContext()
  {
    return mPopupContext;
  }
  
  public int getPopupTheme()
  {
    return mPopupTheme;
  }
  
  public CharSequence getSubtitle()
  {
    return mSubtitleText;
  }
  
  public CharSequence getTitle()
  {
    return mTitleText;
  }
  
  public int getTitleMarginBottom()
  {
    return mTitleMarginBottom;
  }
  
  public int getTitleMarginEnd()
  {
    return mTitleMarginEnd;
  }
  
  public int getTitleMarginStart()
  {
    return mTitleMarginStart;
  }
  
  public int getTitleMarginTop()
  {
    return mTitleMarginTop;
  }
  
  public DecorToolbar getWrapper()
  {
    if (mWrapper == null) {
      mWrapper = new ToolbarWidgetWrapper(this, true);
    }
    return mWrapper;
  }
  
  public boolean hasExpandedActionView()
  {
    boolean bool;
    if ((mExpandedMenuPresenter != null) && (mExpandedMenuPresenter.mCurrentExpandedItem != null)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hideOverflowMenu()
  {
    boolean bool;
    if ((mMenuView != null) && (mMenuView.hideOverflowMenu())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void inflateMenu(int paramInt)
  {
    getMenuInflater().inflate(paramInt, getMenu());
  }
  
  public boolean isOverflowMenuShowPending()
  {
    boolean bool;
    if ((mMenuView != null) && (mMenuView.isOverflowMenuShowPending())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isOverflowMenuShowing()
  {
    boolean bool;
    if ((mMenuView != null) && (mMenuView.isOverflowMenuShowing())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isTitleTruncated()
  {
    if (mTitleTextView == null) {
      return false;
    }
    Layout localLayout = mTitleTextView.getLayout();
    if (localLayout == null) {
      return false;
    }
    int i = localLayout.getLineCount();
    for (int j = 0; j < i; j++) {
      if (localLayout.getEllipsisCount(j) > 0) {
        return true;
      }
    }
    return false;
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    for (Object localObject = getParent(); (localObject != null) && ((localObject instanceof ViewGroup)); localObject = ((ViewGroup)localObject).getParent())
    {
      localObject = (ViewGroup)localObject;
      if (((ViewGroup)localObject).isKeyboardNavigationCluster())
      {
        setKeyboardNavigationCluster(false);
        if (!((ViewGroup)localObject).getTouchscreenBlocksFocus()) {
          break;
        }
        setTouchscreenBlocksFocus(false);
        break;
      }
    }
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    removeCallbacks(mShowOverflowMenuRunnable);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (getLayoutDirection() == 1) {
      i = 1;
    } else {
      i = 0;
    }
    int j = getWidth();
    int k = getHeight();
    int m = getPaddingLeft();
    int n = getPaddingRight();
    int i1 = getPaddingTop();
    int i2 = getPaddingBottom();
    paramInt1 = m;
    int i3 = j - n;
    int[] arrayOfInt = mTempMargins;
    arrayOfInt[1] = 0;
    arrayOfInt[0] = 0;
    paramInt3 = getMinimumHeight();
    if (paramInt3 >= 0) {
      paramInt3 = Math.min(paramInt3, paramInt4 - paramInt2);
    } else {
      paramInt3 = 0;
    }
    int i4 = paramInt1;
    paramInt2 = i3;
    if (shouldLayout(mNavButtonView)) {
      if (i != 0)
      {
        paramInt2 = layoutChildRight(mNavButtonView, i3, arrayOfInt, paramInt3);
        i4 = paramInt1;
      }
      else
      {
        i4 = layoutChildLeft(mNavButtonView, paramInt1, arrayOfInt, paramInt3);
        paramInt2 = i3;
      }
    }
    paramInt4 = i4;
    paramInt1 = paramInt2;
    if (shouldLayout(mCollapseButtonView)) {
      if (i != 0)
      {
        paramInt1 = layoutChildRight(mCollapseButtonView, paramInt2, arrayOfInt, paramInt3);
        paramInt4 = i4;
      }
      else
      {
        paramInt4 = layoutChildLeft(mCollapseButtonView, i4, arrayOfInt, paramInt3);
        paramInt1 = paramInt2;
      }
    }
    i4 = paramInt4;
    paramInt2 = paramInt1;
    if (shouldLayout(mMenuView)) {
      if (i != 0)
      {
        i4 = layoutChildLeft(mMenuView, paramInt4, arrayOfInt, paramInt3);
        paramInt2 = paramInt1;
      }
      else
      {
        paramInt2 = layoutChildRight(mMenuView, paramInt1, arrayOfInt, paramInt3);
        i4 = paramInt4;
      }
    }
    paramInt4 = getCurrentContentInsetLeft();
    paramInt1 = getCurrentContentInsetRight();
    arrayOfInt[0] = Math.max(0, paramInt4 - i4);
    arrayOfInt[1] = Math.max(0, paramInt1 - (j - n - paramInt2));
    paramInt4 = Math.max(i4, paramInt4);
    paramInt2 = Math.min(paramInt2, j - n - paramInt1);
    paramInt1 = paramInt4;
    i4 = paramInt2;
    if (shouldLayout(mExpandedActionView)) {
      if (i != 0)
      {
        i4 = layoutChildRight(mExpandedActionView, paramInt2, arrayOfInt, paramInt3);
        paramInt1 = paramInt4;
      }
      else
      {
        paramInt1 = layoutChildLeft(mExpandedActionView, paramInt4, arrayOfInt, paramInt3);
        i4 = paramInt2;
      }
    }
    paramInt2 = paramInt1;
    paramInt4 = i4;
    if (shouldLayout(mLogoView)) {
      if (i != 0)
      {
        paramInt4 = layoutChildRight(mLogoView, i4, arrayOfInt, paramInt3);
        paramInt2 = paramInt1;
      }
      else
      {
        paramInt2 = layoutChildLeft(mLogoView, paramInt1, arrayOfInt, paramInt3);
        paramInt4 = i4;
      }
    }
    paramBoolean = shouldLayout(mTitleTextView);
    boolean bool = shouldLayout(mSubtitleTextView);
    paramInt1 = 0;
    Object localObject1;
    if (paramBoolean)
    {
      localObject1 = (LayoutParams)mTitleTextView.getLayoutParams();
      paramInt1 = 0 + (topMargin + mTitleTextView.getMeasuredHeight() + bottomMargin);
    }
    i3 = paramInt1;
    if (bool)
    {
      localObject1 = (LayoutParams)mSubtitleTextView.getLayoutParams();
      i3 = paramInt1 + (topMargin + mSubtitleTextView.getMeasuredHeight() + bottomMargin);
    }
    if ((!paramBoolean) && (!bool))
    {
      paramInt1 = paramInt2;
      paramInt2 = paramInt4;
    }
    else
    {
      if (paramBoolean) {
        localObject1 = mTitleTextView;
      } else {
        localObject1 = mSubtitleTextView;
      }
      if (bool) {
        localObject2 = mSubtitleTextView;
      } else {
        localObject2 = mTitleTextView;
      }
      localObject1 = (LayoutParams)((View)localObject1).getLayoutParams();
      Object localObject2 = (LayoutParams)((View)localObject2).getLayoutParams();
      if (paramBoolean) {
        if (mTitleTextView.getMeasuredWidth() > 0) {
          break label690;
        }
      }
      if ((bool) && (mSubtitleTextView.getMeasuredWidth() > 0)) {
        label690:
        i4 = 1;
      } else {
        i4 = 0;
      }
      paramInt1 = mGravity & 0x70;
      int i5;
      if (paramInt1 != 48)
      {
        if (paramInt1 != 80)
        {
          i5 = (k - i1 - i2 - i3) / 2;
          if (i5 < topMargin + mTitleMarginTop)
          {
            paramInt1 = topMargin + mTitleMarginTop;
          }
          else
          {
            i3 = k - i2 - i3 - i5 - i1;
            paramInt1 = i5;
            if (i3 < bottomMargin + mTitleMarginBottom) {
              paramInt1 = Math.max(0, i5 - (bottomMargin + mTitleMarginBottom - i3));
            }
          }
          paramInt1 = i1 + paramInt1;
        }
        else
        {
          paramInt1 = k - i2 - bottomMargin - mTitleMarginBottom - i3;
        }
      }
      else {
        paramInt1 = getPaddingTop() + topMargin + mTitleMarginTop;
      }
      i3 = paramInt2;
      if (i != 0)
      {
        if (i4 != 0) {
          paramInt2 = mTitleMarginStart;
        } else {
          paramInt2 = 0;
        }
        i = paramInt2 - arrayOfInt[1];
        paramInt2 = paramInt4 - Math.max(0, i);
        arrayOfInt[1] = Math.max(0, -i);
        i = paramInt2;
        paramInt4 = paramInt2;
        if (paramBoolean)
        {
          localObject1 = (LayoutParams)mTitleTextView.getLayoutParams();
          i1 = i - mTitleTextView.getMeasuredWidth();
          i5 = mTitleTextView.getMeasuredHeight() + paramInt1;
          mTitleTextView.layout(i1, paramInt1, i, i5);
          paramInt1 = i1 - mTitleMarginEnd;
          i5 += bottomMargin;
        }
        else
        {
          i5 = paramInt1;
          paramInt1 = i;
        }
        i = paramInt4;
        if (bool)
        {
          localObject1 = (LayoutParams)mSubtitleTextView.getLayoutParams();
          i1 = i5 + topMargin;
          i = mSubtitleTextView.getMeasuredWidth();
          i5 = mSubtitleTextView.getMeasuredHeight() + i1;
          mSubtitleTextView.layout(paramInt4 - i, i1, paramInt4, i5);
          i = paramInt4 - mTitleMarginEnd;
          paramInt4 = bottomMargin;
        }
        if (i4 != 0) {
          paramInt2 = Math.min(paramInt1, i);
        }
        paramInt1 = i3;
      }
      else
      {
        if (i4 != 0) {
          paramInt2 = mTitleMarginStart;
        } else {
          paramInt2 = 0;
        }
        i = paramInt2 - arrayOfInt[0];
        paramInt2 = i3 + Math.max(0, i);
        arrayOfInt[0] = Math.max(0, -i);
        i3 = paramInt2;
        i = paramInt2;
        if (paramBoolean)
        {
          localObject1 = (LayoutParams)mTitleTextView.getLayoutParams();
          i1 = mTitleTextView.getMeasuredWidth() + i3;
          i5 = mTitleTextView.getMeasuredHeight() + paramInt1;
          mTitleTextView.layout(i3, paramInt1, i1, i5);
          i3 = i1 + mTitleMarginEnd;
          paramInt1 = i5 + bottomMargin;
        }
        i5 = i;
        if (bool)
        {
          localObject1 = (LayoutParams)mSubtitleTextView.getLayoutParams();
          i5 = paramInt1 + topMargin;
          i1 = mSubtitleTextView.getMeasuredWidth() + i;
          paramInt1 = mSubtitleTextView.getMeasuredHeight() + i5;
          mSubtitleTextView.layout(i, i5, i1, paramInt1);
          i5 = i1 + mTitleMarginEnd;
          paramInt1 = bottomMargin;
        }
        paramInt1 = paramInt2;
        paramInt2 = paramInt4;
        if (i4 != 0)
        {
          paramInt1 = Math.max(i3, i5);
          paramInt2 = paramInt4;
        }
      }
    }
    i4 = paramInt3;
    paramInt4 = m;
    addCustomViewsWithGravity(mTempViews, 3);
    m = mTempViews.size();
    for (paramInt3 = 0; paramInt3 < m; paramInt3++) {
      paramInt1 = layoutChildLeft((View)mTempViews.get(paramInt3), paramInt1, arrayOfInt, i4);
    }
    addCustomViewsWithGravity(mTempViews, 5);
    int i = mTempViews.size();
    for (paramInt3 = 0; paramInt3 < i; paramInt3++) {
      paramInt2 = layoutChildRight((View)mTempViews.get(paramInt3), paramInt2, arrayOfInt, i4);
    }
    addCustomViewsWithGravity(mTempViews, 1);
    i3 = getViewListMeasuredWidth(mTempViews, arrayOfInt);
    paramInt3 = paramInt4 + (j - paramInt4 - n) / 2 - i3 / 2;
    paramInt4 = paramInt3 + i3;
    if (paramInt3 >= paramInt1)
    {
      paramInt1 = paramInt3;
      if (paramInt4 > paramInt2) {
        paramInt1 = paramInt3 - (paramInt4 - paramInt2);
      }
    }
    i3 = mTempViews.size();
    paramInt2 = 0;
    paramInt4 = paramInt1;
    paramInt1 = i;
    paramInt3 = m;
    while (paramInt2 < i3)
    {
      paramInt4 = layoutChildLeft((View)mTempViews.get(paramInt2), paramInt4, arrayOfInt, i4);
      paramInt2++;
    }
    mTempViews.clear();
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = 0;
    int j = 0;
    int[] arrayOfInt = mTempMargins;
    if (isLayoutRtl()) {
      k = 1;
    }
    for (int m = 0;; m = 1)
    {
      break;
      k = 0;
    }
    int n = 0;
    if (shouldLayout(mNavButtonView))
    {
      measureChildConstrained(mNavButtonView, paramInt1, 0, paramInt2, 0, mMaxButtonHeight);
      n = mNavButtonView.getMeasuredWidth() + getHorizontalMargins(mNavButtonView);
      i = Math.max(0, mNavButtonView.getMeasuredHeight() + getVerticalMargins(mNavButtonView));
      j = combineMeasuredStates(0, mNavButtonView.getMeasuredState());
    }
    int i1 = i;
    int i2 = j;
    if (shouldLayout(mCollapseButtonView))
    {
      measureChildConstrained(mCollapseButtonView, paramInt1, 0, paramInt2, 0, mMaxButtonHeight);
      n = mCollapseButtonView.getMeasuredWidth() + getHorizontalMargins(mCollapseButtonView);
      i1 = Math.max(i, mCollapseButtonView.getMeasuredHeight() + getVerticalMargins(mCollapseButtonView));
      i2 = combineMeasuredStates(j, mCollapseButtonView.getMeasuredState());
    }
    j = getCurrentContentInsetStart();
    i = 0 + Math.max(j, n);
    arrayOfInt[k] = Math.max(0, j - n);
    if (shouldLayout(mMenuView))
    {
      measureChildConstrained(mMenuView, paramInt1, i, paramInt2, 0, mMaxButtonHeight);
      n = mMenuView.getMeasuredWidth();
      k = getHorizontalMargins(mMenuView);
      i1 = Math.max(i1, mMenuView.getMeasuredHeight() + getVerticalMargins(mMenuView));
      j = combineMeasuredStates(i2, mMenuView.getMeasuredState());
      i2 = n + k;
    }
    else
    {
      j = i2;
      i2 = 0;
    }
    n = getCurrentContentInsetEnd();
    int k = i + Math.max(n, i2);
    arrayOfInt[m] = Math.max(0, n - i2);
    if (shouldLayout(mExpandedActionView))
    {
      k += measureChildCollapseMargins(mExpandedActionView, paramInt1, k, paramInt2, 0, arrayOfInt);
      m = Math.max(i1, mExpandedActionView.getMeasuredHeight() + getVerticalMargins(mExpandedActionView));
      i = combineMeasuredStates(j, mExpandedActionView.getMeasuredState());
    }
    else
    {
      i = j;
      m = i1;
    }
    j = k;
    n = m;
    i1 = i;
    if (shouldLayout(mLogoView))
    {
      j = k + measureChildCollapseMargins(mLogoView, paramInt1, k, paramInt2, 0, arrayOfInt);
      n = Math.max(m, mLogoView.getMeasuredHeight() + getVerticalMargins(mLogoView));
      i1 = combineMeasuredStates(i, mLogoView.getMeasuredState());
    }
    k = getChildCount();
    i = 0;
    m = n;
    n = j;
    j = k;
    while (i < j)
    {
      View localView = getChildAt(i);
      if ((getLayoutParamsmViewType == 0) && (shouldLayout(localView)))
      {
        n += measureChildCollapseMargins(localView, paramInt1, n, paramInt2, 0, arrayOfInt);
        m = Math.max(m, localView.getMeasuredHeight() + getVerticalMargins(localView));
        i1 = combineMeasuredStates(i1, localView.getMeasuredState());
      }
      i++;
    }
    i = 0;
    j = 0;
    int i3 = mTitleMarginTop + mTitleMarginBottom;
    int i4 = mTitleMarginStart + mTitleMarginEnd;
    i2 = i1;
    if (shouldLayout(mTitleTextView))
    {
      measureChildCollapseMargins(mTitleTextView, paramInt1, n + i4, paramInt2, i3, arrayOfInt);
      i = mTitleTextView.getMeasuredWidth() + getHorizontalMargins(mTitleTextView);
      j = mTitleTextView.getMeasuredHeight() + getVerticalMargins(mTitleTextView);
      i2 = combineMeasuredStates(i1, mTitleTextView.getMeasuredState());
    }
    k = i;
    i1 = i2;
    int i5 = j;
    if (shouldLayout(mSubtitleTextView))
    {
      k = Math.max(i, measureChildCollapseMargins(mSubtitleTextView, paramInt1, n + i4, paramInt2, j + i3, arrayOfInt));
      i5 = j + (mSubtitleTextView.getMeasuredHeight() + getVerticalMargins(mSubtitleTextView));
      i1 = combineMeasuredStates(i2, mSubtitleTextView.getMeasuredState());
    }
    i2 = Math.max(m, i5);
    i5 = getPaddingLeft();
    m = getPaddingRight();
    j = getPaddingTop();
    i = getPaddingBottom();
    n = resolveSizeAndState(Math.max(n + k + (i5 + m), getSuggestedMinimumWidth()), paramInt1, 0xFF000000 & i1);
    paramInt1 = resolveSizeAndState(Math.max(i2 + (j + i), getSuggestedMinimumHeight()), paramInt2, i1 << 16);
    if (shouldCollapse()) {
      paramInt1 = 0;
    }
    setMeasuredDimension(n, paramInt1);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    SavedState localSavedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(localSavedState.getSuperState());
    if (mMenuView != null) {
      paramParcelable = mMenuView.peekMenu();
    } else {
      paramParcelable = null;
    }
    if ((expandedMenuItemId != 0) && (mExpandedMenuPresenter != null) && (paramParcelable != null))
    {
      paramParcelable = paramParcelable.findItem(expandedMenuItemId);
      if (paramParcelable != null) {
        paramParcelable.expandActionView();
      }
    }
    if (isOverflowOpen) {
      postShowOverflowMenu();
    }
  }
  
  public void onRtlPropertiesChanged(int paramInt)
  {
    super.onRtlPropertiesChanged(paramInt);
    ensureContentInsets();
    RtlSpacingHelper localRtlSpacingHelper = mContentInsets;
    boolean bool = true;
    if (paramInt != 1) {
      bool = false;
    }
    localRtlSpacingHelper.setDirection(bool);
  }
  
  protected Parcelable onSaveInstanceState()
  {
    SavedState localSavedState = new SavedState(super.onSaveInstanceState());
    if ((mExpandedMenuPresenter != null) && (mExpandedMenuPresenter.mCurrentExpandedItem != null)) {
      expandedMenuItemId = mExpandedMenuPresenter.mCurrentExpandedItem.getItemId();
    }
    isOverflowOpen = isOverflowMenuShowing();
    return localSavedState;
  }
  
  protected void onSetLayoutParams(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    if (!checkLayoutParams(paramLayoutParams)) {
      paramView.setLayoutParams(generateLayoutParams(paramLayoutParams));
    }
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getActionMasked();
    if (i == 0) {
      mEatingTouch = false;
    }
    if (!mEatingTouch)
    {
      boolean bool = super.onTouchEvent(paramMotionEvent);
      if ((i == 0) && (!bool)) {
        mEatingTouch = true;
      }
    }
    if ((i == 1) || (i == 3)) {
      mEatingTouch = false;
    }
    return true;
  }
  
  void removeChildrenForExpandedActionView()
  {
    for (int i = getChildCount() - 1; i >= 0; i--)
    {
      View localView = getChildAt(i);
      if ((getLayoutParamsmViewType != 2) && (localView != mMenuView))
      {
        removeViewAt(i);
        mHiddenViews.add(localView);
      }
    }
  }
  
  public void setCollapsible(boolean paramBoolean)
  {
    mCollapsible = paramBoolean;
    requestLayout();
  }
  
  public void setContentInsetEndWithActions(int paramInt)
  {
    int i = paramInt;
    if (paramInt < 0) {
      i = Integer.MIN_VALUE;
    }
    if (i != mContentInsetEndWithActions)
    {
      mContentInsetEndWithActions = i;
      if (getNavigationIcon() != null) {
        requestLayout();
      }
    }
  }
  
  public void setContentInsetStartWithNavigation(int paramInt)
  {
    int i = paramInt;
    if (paramInt < 0) {
      i = Integer.MIN_VALUE;
    }
    if (i != mContentInsetStartWithNavigation)
    {
      mContentInsetStartWithNavigation = i;
      if (getNavigationIcon() != null) {
        requestLayout();
      }
    }
  }
  
  public void setContentInsetsAbsolute(int paramInt1, int paramInt2)
  {
    ensureContentInsets();
    mContentInsets.setAbsolute(paramInt1, paramInt2);
  }
  
  public void setContentInsetsRelative(int paramInt1, int paramInt2)
  {
    ensureContentInsets();
    mContentInsets.setRelative(paramInt1, paramInt2);
  }
  
  public void setLogo(int paramInt)
  {
    setLogo(getContext().getDrawable(paramInt));
  }
  
  public void setLogo(Drawable paramDrawable)
  {
    if (paramDrawable != null)
    {
      ensureLogoView();
      if (!isChildOrHidden(mLogoView)) {
        addSystemView(mLogoView, true);
      }
    }
    else if ((mLogoView != null) && (isChildOrHidden(mLogoView)))
    {
      removeView(mLogoView);
      mHiddenViews.remove(mLogoView);
    }
    if (mLogoView != null) {
      mLogoView.setImageDrawable(paramDrawable);
    }
  }
  
  public void setLogoDescription(int paramInt)
  {
    setLogoDescription(getContext().getText(paramInt));
  }
  
  public void setLogoDescription(CharSequence paramCharSequence)
  {
    if (!TextUtils.isEmpty(paramCharSequence)) {
      ensureLogoView();
    }
    if (mLogoView != null) {
      mLogoView.setContentDescription(paramCharSequence);
    }
  }
  
  public void setMenu(MenuBuilder paramMenuBuilder, ActionMenuPresenter paramActionMenuPresenter)
  {
    if ((paramMenuBuilder == null) && (mMenuView == null)) {
      return;
    }
    ensureMenuView();
    MenuBuilder localMenuBuilder = mMenuView.peekMenu();
    if (localMenuBuilder == paramMenuBuilder) {
      return;
    }
    if (localMenuBuilder != null)
    {
      localMenuBuilder.removeMenuPresenter(mOuterActionMenuPresenter);
      localMenuBuilder.removeMenuPresenter(mExpandedMenuPresenter);
    }
    if (mExpandedMenuPresenter == null) {
      mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter(null);
    }
    paramActionMenuPresenter.setExpandedActionViewsExclusive(true);
    if (paramMenuBuilder != null)
    {
      paramMenuBuilder.addMenuPresenter(paramActionMenuPresenter, mPopupContext);
      paramMenuBuilder.addMenuPresenter(mExpandedMenuPresenter, mPopupContext);
    }
    else
    {
      paramActionMenuPresenter.initForMenu(mPopupContext, null);
      mExpandedMenuPresenter.initForMenu(mPopupContext, null);
      paramActionMenuPresenter.updateMenuView(true);
      mExpandedMenuPresenter.updateMenuView(true);
    }
    mMenuView.setPopupTheme(mPopupTheme);
    mMenuView.setPresenter(paramActionMenuPresenter);
    mOuterActionMenuPresenter = paramActionMenuPresenter;
  }
  
  public void setMenuCallbacks(MenuPresenter.Callback paramCallback, MenuBuilder.Callback paramCallback1)
  {
    mActionMenuPresenterCallback = paramCallback;
    mMenuBuilderCallback = paramCallback1;
    if (mMenuView != null) {
      mMenuView.setMenuCallbacks(paramCallback, paramCallback1);
    }
  }
  
  public void setNavigationContentDescription(int paramInt)
  {
    CharSequence localCharSequence;
    if (paramInt != 0) {
      localCharSequence = getContext().getText(paramInt);
    } else {
      localCharSequence = null;
    }
    setNavigationContentDescription(localCharSequence);
  }
  
  public void setNavigationContentDescription(CharSequence paramCharSequence)
  {
    if (!TextUtils.isEmpty(paramCharSequence)) {
      ensureNavButtonView();
    }
    if (mNavButtonView != null) {
      mNavButtonView.setContentDescription(paramCharSequence);
    }
  }
  
  public void setNavigationIcon(int paramInt)
  {
    setNavigationIcon(getContext().getDrawable(paramInt));
  }
  
  public void setNavigationIcon(Drawable paramDrawable)
  {
    if (paramDrawable != null)
    {
      ensureNavButtonView();
      if (!isChildOrHidden(mNavButtonView)) {
        addSystemView(mNavButtonView, true);
      }
    }
    else if ((mNavButtonView != null) && (isChildOrHidden(mNavButtonView)))
    {
      removeView(mNavButtonView);
      mHiddenViews.remove(mNavButtonView);
    }
    if (mNavButtonView != null) {
      mNavButtonView.setImageDrawable(paramDrawable);
    }
  }
  
  public void setNavigationOnClickListener(View.OnClickListener paramOnClickListener)
  {
    ensureNavButtonView();
    mNavButtonView.setOnClickListener(paramOnClickListener);
  }
  
  public void setOnMenuItemClickListener(OnMenuItemClickListener paramOnMenuItemClickListener)
  {
    mOnMenuItemClickListener = paramOnMenuItemClickListener;
  }
  
  public void setOverflowIcon(Drawable paramDrawable)
  {
    ensureMenu();
    mMenuView.setOverflowIcon(paramDrawable);
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
  
  public void setSubtitle(int paramInt)
  {
    setSubtitle(getContext().getText(paramInt));
  }
  
  public void setSubtitle(CharSequence paramCharSequence)
  {
    if (!TextUtils.isEmpty(paramCharSequence))
    {
      if (mSubtitleTextView == null)
      {
        mSubtitleTextView = new TextView(getContext());
        mSubtitleTextView.setSingleLine();
        mSubtitleTextView.setEllipsize(TextUtils.TruncateAt.END);
        if (mSubtitleTextAppearance != 0) {
          mSubtitleTextView.setTextAppearance(mSubtitleTextAppearance);
        }
        if (mSubtitleTextColor != 0) {
          mSubtitleTextView.setTextColor(mSubtitleTextColor);
        }
      }
      if (!isChildOrHidden(mSubtitleTextView)) {
        addSystemView(mSubtitleTextView, true);
      }
    }
    else if ((mSubtitleTextView != null) && (isChildOrHidden(mSubtitleTextView)))
    {
      removeView(mSubtitleTextView);
      mHiddenViews.remove(mSubtitleTextView);
    }
    if (mSubtitleTextView != null) {
      mSubtitleTextView.setText(paramCharSequence);
    }
    mSubtitleText = paramCharSequence;
  }
  
  public void setSubtitleTextAppearance(Context paramContext, int paramInt)
  {
    mSubtitleTextAppearance = paramInt;
    if (mSubtitleTextView != null) {
      mSubtitleTextView.setTextAppearance(paramInt);
    }
  }
  
  public void setSubtitleTextColor(int paramInt)
  {
    mSubtitleTextColor = paramInt;
    if (mSubtitleTextView != null) {
      mSubtitleTextView.setTextColor(paramInt);
    }
  }
  
  public void setTitle(int paramInt)
  {
    setTitle(getContext().getText(paramInt));
  }
  
  public void setTitle(CharSequence paramCharSequence)
  {
    if (!TextUtils.isEmpty(paramCharSequence))
    {
      if (mTitleTextView == null)
      {
        mTitleTextView = new TextView(getContext());
        mTitleTextView.setSingleLine();
        mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
        if (mTitleTextAppearance != 0) {
          mTitleTextView.setTextAppearance(mTitleTextAppearance);
        }
        if (mTitleTextColor != 0) {
          mTitleTextView.setTextColor(mTitleTextColor);
        }
      }
      if (!isChildOrHidden(mTitleTextView)) {
        addSystemView(mTitleTextView, true);
      }
    }
    else if ((mTitleTextView != null) && (isChildOrHidden(mTitleTextView)))
    {
      removeView(mTitleTextView);
      mHiddenViews.remove(mTitleTextView);
    }
    if (mTitleTextView != null) {
      mTitleTextView.setText(paramCharSequence);
    }
    mTitleText = paramCharSequence;
  }
  
  public void setTitleMargin(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mTitleMarginStart = paramInt1;
    mTitleMarginTop = paramInt2;
    mTitleMarginEnd = paramInt3;
    mTitleMarginBottom = paramInt4;
    requestLayout();
  }
  
  public void setTitleMarginBottom(int paramInt)
  {
    mTitleMarginBottom = paramInt;
    requestLayout();
  }
  
  public void setTitleMarginEnd(int paramInt)
  {
    mTitleMarginEnd = paramInt;
    requestLayout();
  }
  
  public void setTitleMarginStart(int paramInt)
  {
    mTitleMarginStart = paramInt;
    requestLayout();
  }
  
  public void setTitleMarginTop(int paramInt)
  {
    mTitleMarginTop = paramInt;
    requestLayout();
  }
  
  public void setTitleTextAppearance(Context paramContext, int paramInt)
  {
    mTitleTextAppearance = paramInt;
    if (mTitleTextView != null) {
      mTitleTextView.setTextAppearance(paramInt);
    }
  }
  
  public void setTitleTextColor(int paramInt)
  {
    mTitleTextColor = paramInt;
    if (mTitleTextView != null) {
      mTitleTextView.setTextColor(paramInt);
    }
  }
  
  public boolean showOverflowMenu()
  {
    boolean bool;
    if ((mMenuView != null) && (mMenuView.showOverflowMenu())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private class ExpandedActionViewMenuPresenter
    implements MenuPresenter
  {
    MenuItemImpl mCurrentExpandedItem;
    MenuBuilder mMenu;
    
    private ExpandedActionViewMenuPresenter() {}
    
    public boolean collapseItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl)
    {
      if ((mExpandedActionView instanceof CollapsibleActionView)) {
        ((CollapsibleActionView)mExpandedActionView).onActionViewCollapsed();
      }
      removeView(mExpandedActionView);
      removeView(mCollapseButtonView);
      mExpandedActionView = null;
      addChildrenForExpandedActionView();
      mCurrentExpandedItem = null;
      requestLayout();
      paramMenuItemImpl.setActionViewExpanded(false);
      return true;
    }
    
    public boolean expandItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl)
    {
      Toolbar.this.ensureCollapseButtonView();
      if (mCollapseButtonView.getParent() != Toolbar.this) {
        addView(mCollapseButtonView);
      }
      mExpandedActionView = paramMenuItemImpl.getActionView();
      mCurrentExpandedItem = paramMenuItemImpl;
      if (mExpandedActionView.getParent() != Toolbar.this)
      {
        paramMenuBuilder = generateDefaultLayoutParams();
        gravity = (0x800003 | mButtonGravity & 0x70);
        mViewType = 2;
        mExpandedActionView.setLayoutParams(paramMenuBuilder);
        addView(mExpandedActionView);
      }
      removeChildrenForExpandedActionView();
      requestLayout();
      paramMenuItemImpl.setActionViewExpanded(true);
      if ((mExpandedActionView instanceof CollapsibleActionView)) {
        ((CollapsibleActionView)mExpandedActionView).onActionViewExpanded();
      }
      return true;
    }
    
    public boolean flagActionItems()
    {
      return false;
    }
    
    public int getId()
    {
      return 0;
    }
    
    public MenuView getMenuView(ViewGroup paramViewGroup)
    {
      return null;
    }
    
    public void initForMenu(Context paramContext, MenuBuilder paramMenuBuilder)
    {
      if ((mMenu != null) && (mCurrentExpandedItem != null)) {
        mMenu.collapseItemActionView(mCurrentExpandedItem);
      }
      mMenu = paramMenuBuilder;
    }
    
    public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean) {}
    
    public void onRestoreInstanceState(Parcelable paramParcelable) {}
    
    public Parcelable onSaveInstanceState()
    {
      return null;
    }
    
    public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder)
    {
      return false;
    }
    
    public void setCallback(MenuPresenter.Callback paramCallback) {}
    
    public void updateMenuView(boolean paramBoolean)
    {
      if (mCurrentExpandedItem != null)
      {
        int i = 0;
        int j = i;
        if (mMenu != null)
        {
          int k = mMenu.size();
          for (int m = 0;; m++)
          {
            j = i;
            if (m >= k) {
              break;
            }
            if (mMenu.getItem(m) == mCurrentExpandedItem)
            {
              j = 1;
              break;
            }
          }
        }
        if (j == 0) {
          collapseItemActionView(mMenu, mCurrentExpandedItem);
        }
      }
    }
  }
  
  public static class LayoutParams
    extends ActionBar.LayoutParams
  {
    static final int CUSTOM = 0;
    static final int EXPANDED = 2;
    static final int SYSTEM = 1;
    int mViewType = 0;
    
    public LayoutParams(int paramInt)
    {
      this(-2, -1, paramInt);
    }
    
    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
      gravity = 8388627;
    }
    
    public LayoutParams(int paramInt1, int paramInt2, int paramInt3)
    {
      super(paramInt2);
      gravity = paramInt3;
    }
    
    public LayoutParams(ActionBar.LayoutParams paramLayoutParams)
    {
      super();
    }
    
    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
    }
    
    public LayoutParams(ViewGroup.LayoutParams paramLayoutParams)
    {
      super();
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams paramMarginLayoutParams)
    {
      super();
      copyMarginsFrom(paramMarginLayoutParams);
    }
    
    public LayoutParams(LayoutParams paramLayoutParams)
    {
      super();
      mViewType = mViewType;
    }
  }
  
  public static abstract interface OnMenuItemClickListener
  {
    public abstract boolean onMenuItemClick(MenuItem paramMenuItem);
  }
  
  static class SavedState
    extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public Toolbar.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new Toolbar.SavedState(paramAnonymousParcel);
      }
      
      public Toolbar.SavedState[] newArray(int paramAnonymousInt)
      {
        return new Toolbar.SavedState[paramAnonymousInt];
      }
    };
    public int expandedMenuItemId;
    public boolean isOverflowOpen;
    
    public SavedState(Parcel paramParcel)
    {
      super();
      expandedMenuItemId = paramParcel.readInt();
      boolean bool;
      if (paramParcel.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      isOverflowOpen = bool;
    }
    
    public SavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(expandedMenuItemId);
      paramParcel.writeInt(isOverflowOpen);
    }
  }
}
