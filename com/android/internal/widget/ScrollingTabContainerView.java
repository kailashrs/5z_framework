package com.android.internal.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import com.android.internal.view.ActionBarPolicy;

public class ScrollingTabContainerView
  extends HorizontalScrollView
  implements AdapterView.OnItemClickListener
{
  private static final int FADE_DURATION = 200;
  private static final String TAG = "ScrollingTabContainerView";
  private static final TimeInterpolator sAlphaInterpolator = new DecelerateInterpolator();
  private boolean mAllowCollapse;
  private int mContentHeight;
  int mMaxTabWidth;
  private int mSelectedTabIndex;
  int mStackedTabMaxWidth;
  private TabClickListener mTabClickListener;
  private LinearLayout mTabLayout;
  Runnable mTabSelector;
  private Spinner mTabSpinner;
  protected final VisibilityAnimListener mVisAnimListener = new VisibilityAnimListener();
  protected Animator mVisibilityAnim;
  
  public ScrollingTabContainerView(Context paramContext)
  {
    super(paramContext);
    setHorizontalScrollBarEnabled(false);
    paramContext = ActionBarPolicy.get(paramContext);
    setContentHeight(paramContext.getTabContainerHeight());
    mStackedTabMaxWidth = paramContext.getStackedTabMaxWidth();
    mTabLayout = createTabLayout();
    addView(mTabLayout, new ViewGroup.LayoutParams(-2, -1));
  }
  
  private Spinner createSpinner()
  {
    Spinner localSpinner = new Spinner(getContext(), null, 16843479);
    localSpinner.setLayoutParams(new LinearLayout.LayoutParams(-2, -1));
    localSpinner.setOnItemClickListenerInt(this);
    return localSpinner;
  }
  
  private LinearLayout createTabLayout()
  {
    LinearLayout localLinearLayout = new LinearLayout(getContext(), null, 16843508);
    localLinearLayout.setMeasureWithLargestChildEnabled(true);
    localLinearLayout.setGravity(17);
    localLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-2, -1));
    return localLinearLayout;
  }
  
  private TabView createTabView(Context paramContext, ActionBar.Tab paramTab, boolean paramBoolean)
  {
    paramContext = new TabView(paramContext, paramTab, paramBoolean);
    if (paramBoolean)
    {
      paramContext.setBackgroundDrawable(null);
      paramContext.setLayoutParams(new AbsListView.LayoutParams(-1, mContentHeight));
    }
    else
    {
      paramContext.setFocusable(true);
      if (mTabClickListener == null) {
        mTabClickListener = new TabClickListener(null);
      }
      paramContext.setOnClickListener(mTabClickListener);
    }
    return paramContext;
  }
  
  private boolean isCollapsed()
  {
    boolean bool;
    if ((mTabSpinner != null) && (mTabSpinner.getParent() == this)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void performCollapse()
  {
    if (isCollapsed()) {
      return;
    }
    if (mTabSpinner == null) {
      mTabSpinner = createSpinner();
    }
    removeView(mTabLayout);
    addView(mTabSpinner, new ViewGroup.LayoutParams(-2, -1));
    if (mTabSpinner.getAdapter() == null)
    {
      TabAdapter localTabAdapter = new TabAdapter(mContext);
      localTabAdapter.setDropDownViewContext(mTabSpinner.getPopupContext());
      mTabSpinner.setAdapter(localTabAdapter);
    }
    if (mTabSelector != null)
    {
      removeCallbacks(mTabSelector);
      mTabSelector = null;
    }
    mTabSpinner.setSelection(mSelectedTabIndex);
  }
  
  private boolean performExpand()
  {
    if (!isCollapsed()) {
      return false;
    }
    removeView(mTabSpinner);
    addView(mTabLayout, new ViewGroup.LayoutParams(-2, -1));
    setTabSelected(mTabSpinner.getSelectedItemPosition());
    return false;
  }
  
  public void addTab(ActionBar.Tab paramTab, int paramInt, boolean paramBoolean)
  {
    paramTab = createTabView(mContext, paramTab, false);
    mTabLayout.addView(paramTab, paramInt, new LinearLayout.LayoutParams(0, -1, 1.0F));
    if (mTabSpinner != null) {
      ((TabAdapter)mTabSpinner.getAdapter()).notifyDataSetChanged();
    }
    if (paramBoolean) {
      paramTab.setSelected(true);
    }
    if (mAllowCollapse) {
      requestLayout();
    }
  }
  
  public void addTab(ActionBar.Tab paramTab, boolean paramBoolean)
  {
    paramTab = createTabView(mContext, paramTab, false);
    mTabLayout.addView(paramTab, new LinearLayout.LayoutParams(0, -1, 1.0F));
    if (mTabSpinner != null) {
      ((TabAdapter)mTabSpinner.getAdapter()).notifyDataSetChanged();
    }
    if (paramBoolean) {
      paramTab.setSelected(true);
    }
    if (mAllowCollapse) {
      requestLayout();
    }
  }
  
  public void animateToTab(int paramInt)
  {
    final View localView = mTabLayout.getChildAt(paramInt);
    if (mTabSelector != null) {
      removeCallbacks(mTabSelector);
    }
    mTabSelector = new Runnable()
    {
      public void run()
      {
        int i = localView.getLeft();
        int j = (getWidth() - localView.getWidth()) / 2;
        smoothScrollTo(i - j, 0);
        mTabSelector = null;
      }
    };
    post(mTabSelector);
  }
  
  public void animateToVisibility(int paramInt)
  {
    if (mVisibilityAnim != null) {
      mVisibilityAnim.cancel();
    }
    ObjectAnimator localObjectAnimator;
    if (paramInt == 0)
    {
      if (getVisibility() != 0) {
        setAlpha(0.0F);
      }
      localObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", new float[] { 1.0F });
      localObjectAnimator.setDuration(200L);
      localObjectAnimator.setInterpolator(sAlphaInterpolator);
      localObjectAnimator.addListener(mVisAnimListener.withFinalVisibility(paramInt));
      localObjectAnimator.start();
    }
    else
    {
      localObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", new float[] { 0.0F });
      localObjectAnimator.setDuration(200L);
      localObjectAnimator.setInterpolator(sAlphaInterpolator);
      localObjectAnimator.addListener(mVisAnimListener.withFinalVisibility(paramInt));
      localObjectAnimator.start();
    }
  }
  
  public void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    if (mTabSelector != null) {
      post(mTabSelector);
    }
  }
  
  protected void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    paramConfiguration = ActionBarPolicy.get(getContext());
    setContentHeight(paramConfiguration.getTabContainerHeight());
    mStackedTabMaxWidth = paramConfiguration.getStackedTabMaxWidth();
  }
  
  public void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    if (mTabSelector != null) {
      removeCallbacks(mTabSelector);
    }
  }
  
  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    ((TabView)paramView).getTab().select();
  }
  
  public void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getMode(paramInt1);
    paramInt2 = 1;
    boolean bool;
    if (i == 1073741824) {
      bool = true;
    } else {
      bool = false;
    }
    setFillViewport(bool);
    int j = mTabLayout.getChildCount();
    if ((j > 1) && ((i == 1073741824) || (i == Integer.MIN_VALUE)))
    {
      if (j > 2) {
        mMaxTabWidth = ((int)(View.MeasureSpec.getSize(paramInt1) * 0.4F));
      } else {
        mMaxTabWidth = (View.MeasureSpec.getSize(paramInt1) / 2);
      }
      mMaxTabWidth = Math.min(mMaxTabWidth, mStackedTabMaxWidth);
    }
    else
    {
      mMaxTabWidth = -1;
    }
    j = View.MeasureSpec.makeMeasureSpec(mContentHeight, 1073741824);
    if ((bool) || (!mAllowCollapse)) {
      paramInt2 = 0;
    }
    if (paramInt2 != 0)
    {
      mTabLayout.measure(0, j);
      if (mTabLayout.getMeasuredWidth() > View.MeasureSpec.getSize(paramInt1)) {
        performCollapse();
      } else {
        performExpand();
      }
    }
    else
    {
      performExpand();
    }
    paramInt2 = getMeasuredWidth();
    super.onMeasure(paramInt1, j);
    paramInt1 = getMeasuredWidth();
    if ((bool) && (paramInt2 != paramInt1)) {
      setTabSelected(mSelectedTabIndex);
    }
  }
  
  public void removeAllTabs()
  {
    mTabLayout.removeAllViews();
    if (mTabSpinner != null) {
      ((TabAdapter)mTabSpinner.getAdapter()).notifyDataSetChanged();
    }
    if (mAllowCollapse) {
      requestLayout();
    }
  }
  
  public void removeTabAt(int paramInt)
  {
    mTabLayout.removeViewAt(paramInt);
    if (mTabSpinner != null) {
      ((TabAdapter)mTabSpinner.getAdapter()).notifyDataSetChanged();
    }
    if (mAllowCollapse) {
      requestLayout();
    }
  }
  
  public void setAllowCollapse(boolean paramBoolean)
  {
    mAllowCollapse = paramBoolean;
  }
  
  public void setContentHeight(int paramInt)
  {
    mContentHeight = paramInt;
    requestLayout();
  }
  
  public void setTabSelected(int paramInt)
  {
    mSelectedTabIndex = paramInt;
    int i = mTabLayout.getChildCount();
    for (int j = 0; j < i; j++)
    {
      View localView = mTabLayout.getChildAt(j);
      boolean bool;
      if (j == paramInt) {
        bool = true;
      } else {
        bool = false;
      }
      localView.setSelected(bool);
      if (bool) {
        animateToTab(paramInt);
      }
    }
    if ((mTabSpinner != null) && (paramInt >= 0)) {
      mTabSpinner.setSelection(paramInt);
    }
  }
  
  public void updateTab(int paramInt)
  {
    ((TabView)mTabLayout.getChildAt(paramInt)).update();
    if (mTabSpinner != null) {
      ((TabAdapter)mTabSpinner.getAdapter()).notifyDataSetChanged();
    }
    if (mAllowCollapse) {
      requestLayout();
    }
  }
  
  private class TabAdapter
    extends BaseAdapter
  {
    private Context mDropDownContext;
    
    public TabAdapter(Context paramContext)
    {
      setDropDownViewContext(paramContext);
    }
    
    public int getCount()
    {
      return mTabLayout.getChildCount();
    }
    
    public View getDropDownView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView == null) {
        paramView = ScrollingTabContainerView.this.createTabView(mDropDownContext, (ActionBar.Tab)getItem(paramInt), true);
      } else {
        ((ScrollingTabContainerView.TabView)paramView).bindTab((ActionBar.Tab)getItem(paramInt));
      }
      return paramView;
    }
    
    public Object getItem(int paramInt)
    {
      return ((ScrollingTabContainerView.TabView)mTabLayout.getChildAt(paramInt)).getTab();
    }
    
    public long getItemId(int paramInt)
    {
      return paramInt;
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView == null) {
        paramView = ScrollingTabContainerView.this.createTabView(mContext, (ActionBar.Tab)getItem(paramInt), true);
      } else {
        ((ScrollingTabContainerView.TabView)paramView).bindTab((ActionBar.Tab)getItem(paramInt));
      }
      return paramView;
    }
    
    public void setDropDownViewContext(Context paramContext)
    {
      mDropDownContext = paramContext;
    }
  }
  
  private class TabClickListener
    implements View.OnClickListener
  {
    private TabClickListener() {}
    
    public void onClick(View paramView)
    {
      ((ScrollingTabContainerView.TabView)paramView).getTab().select();
      int i = mTabLayout.getChildCount();
      for (int j = 0; j < i; j++)
      {
        View localView = mTabLayout.getChildAt(j);
        boolean bool;
        if (localView == paramView) {
          bool = true;
        } else {
          bool = false;
        }
        localView.setSelected(bool);
      }
    }
  }
  
  private class TabView
    extends LinearLayout
  {
    private View mCustomView;
    private ImageView mIconView;
    private ActionBar.Tab mTab;
    private TextView mTextView;
    
    public TabView(Context paramContext, ActionBar.Tab paramTab, boolean paramBoolean)
    {
      super(null, 16843507);
      mTab = paramTab;
      if (paramBoolean) {
        setGravity(8388627);
      }
      update();
    }
    
    public void bindTab(ActionBar.Tab paramTab)
    {
      mTab = paramTab;
      update();
    }
    
    public CharSequence getAccessibilityClassName()
    {
      return ActionBar.Tab.class.getName();
    }
    
    public ActionBar.Tab getTab()
    {
      return mTab;
    }
    
    public void onMeasure(int paramInt1, int paramInt2)
    {
      super.onMeasure(paramInt1, paramInt2);
      if ((mMaxTabWidth > 0) && (getMeasuredWidth() > mMaxTabWidth)) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(mMaxTabWidth, 1073741824), paramInt2);
      }
    }
    
    public void setSelected(boolean paramBoolean)
    {
      int i;
      if (isSelected() != paramBoolean) {
        i = 1;
      } else {
        i = 0;
      }
      super.setSelected(paramBoolean);
      if ((i != 0) && (paramBoolean)) {
        sendAccessibilityEvent(4);
      }
    }
    
    public void update()
    {
      ActionBar.Tab localTab = mTab;
      Object localObject1 = localTab.getCustomView();
      Object localObject2 = null;
      if (localObject1 != null)
      {
        localObject2 = ((View)localObject1).getParent();
        if (localObject2 != this)
        {
          if (localObject2 != null) {
            ((ViewGroup)localObject2).removeView((View)localObject1);
          }
          addView((View)localObject1);
        }
        mCustomView = ((View)localObject1);
        if (mTextView != null) {
          mTextView.setVisibility(8);
        }
        if (mIconView != null)
        {
          mIconView.setVisibility(8);
          mIconView.setImageDrawable(null);
        }
      }
      else
      {
        if (mCustomView != null)
        {
          removeView(mCustomView);
          mCustomView = null;
        }
        Drawable localDrawable = localTab.getIcon();
        localObject1 = localTab.getText();
        Object localObject3;
        Object localObject4;
        if (localDrawable != null)
        {
          if (mIconView == null)
          {
            localObject3 = new ImageView(getContext());
            localObject4 = new LinearLayout.LayoutParams(-2, -2);
            gravity = 16;
            ((ImageView)localObject3).setLayoutParams((ViewGroup.LayoutParams)localObject4);
            addView((View)localObject3, 0);
            mIconView = ((ImageView)localObject3);
          }
          mIconView.setImageDrawable(localDrawable);
          mIconView.setVisibility(0);
        }
        else if (mIconView != null)
        {
          mIconView.setVisibility(8);
          mIconView.setImageDrawable(null);
        }
        boolean bool = TextUtils.isEmpty((CharSequence)localObject1) ^ true;
        if (bool)
        {
          if (mTextView == null)
          {
            localObject4 = new TextView(getContext(), null, 16843509);
            ((TextView)localObject4).setEllipsize(TextUtils.TruncateAt.END);
            localObject3 = new LinearLayout.LayoutParams(-2, -2);
            gravity = 16;
            ((TextView)localObject4).setLayoutParams((ViewGroup.LayoutParams)localObject3);
            addView((View)localObject4);
            mTextView = ((TextView)localObject4);
          }
          mTextView.setText((CharSequence)localObject1);
          mTextView.setVisibility(0);
        }
        else if (mTextView != null)
        {
          mTextView.setVisibility(8);
          mTextView.setText(null);
        }
        if (mIconView != null) {
          mIconView.setContentDescription(localTab.getContentDescription());
        }
        if (!bool) {
          localObject2 = localTab.getContentDescription();
        }
        setTooltipText((CharSequence)localObject2);
      }
    }
  }
  
  protected class VisibilityAnimListener
    implements Animator.AnimatorListener
  {
    private boolean mCanceled = false;
    private int mFinalVisibility;
    
    protected VisibilityAnimListener() {}
    
    public void onAnimationCancel(Animator paramAnimator)
    {
      mCanceled = true;
    }
    
    public void onAnimationEnd(Animator paramAnimator)
    {
      if (mCanceled) {
        return;
      }
      mVisibilityAnim = null;
      setVisibility(mFinalVisibility);
    }
    
    public void onAnimationRepeat(Animator paramAnimator) {}
    
    public void onAnimationStart(Animator paramAnimator)
    {
      setVisibility(0);
      mVisibilityAnim = paramAnimator;
      mCanceled = false;
    }
    
    public VisibilityAnimListener withFinalVisibility(int paramInt)
    {
      mFinalVisibility = paramInt;
      return this;
    }
  }
}
