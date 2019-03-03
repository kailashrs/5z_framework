package android.widget;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnTouchModeChangeListener;
import android.view.Window;
import com.android.internal.R.styleable;
import java.util.ArrayList;
import java.util.List;

public class TabHost
  extends FrameLayout
  implements ViewTreeObserver.OnTouchModeChangeListener
{
  private static final int TABWIDGET_LOCATION_BOTTOM = 3;
  private static final int TABWIDGET_LOCATION_LEFT = 0;
  private static final int TABWIDGET_LOCATION_RIGHT = 2;
  private static final int TABWIDGET_LOCATION_TOP = 1;
  protected int mCurrentTab = -1;
  private View mCurrentView = null;
  protected LocalActivityManager mLocalActivityManager = null;
  private OnTabChangeListener mOnTabChangeListener;
  private FrameLayout mTabContent;
  private View.OnKeyListener mTabKeyListener;
  private int mTabLayoutId;
  private List<TabSpec> mTabSpecs = new ArrayList(2);
  private TabWidget mTabWidget;
  
  public TabHost(Context paramContext)
  {
    super(paramContext);
    initTabHost();
  }
  
  public TabHost(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842883);
  }
  
  public TabHost(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public TabHost(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.TabWidget, paramInt1, paramInt2);
    mTabLayoutId = paramContext.getResourceId(4, 0);
    paramContext.recycle();
    if (mTabLayoutId == 0) {
      mTabLayoutId = 17367327;
    }
    initTabHost();
  }
  
  private int getTabWidgetLocation()
  {
    int i = mTabWidget.getOrientation();
    int j = 1;
    if (i != 1)
    {
      if (mTabContent.getTop() < mTabWidget.getTop()) {
        j = 3;
      }
    }
    else if (mTabContent.getLeft() < mTabWidget.getLeft()) {
      j = 2;
    } else {
      j = 0;
    }
    return j;
  }
  
  private void initTabHost()
  {
    setFocusableInTouchMode(true);
    setDescendantFocusability(262144);
    mCurrentTab = -1;
    mCurrentView = null;
  }
  
  private void invokeOnTabChangeListener()
  {
    if (mOnTabChangeListener != null) {
      mOnTabChangeListener.onTabChanged(getCurrentTabTag());
    }
  }
  
  public void addTab(TabSpec paramTabSpec)
  {
    if (mIndicatorStrategy != null)
    {
      if (mContentStrategy != null)
      {
        View localView = mIndicatorStrategy.createIndicatorView();
        localView.setOnKeyListener(mTabKeyListener);
        if ((mIndicatorStrategy instanceof ViewIndicatorStrategy)) {
          mTabWidget.setStripEnabled(false);
        }
        mTabWidget.addView(localView);
        mTabSpecs.add(paramTabSpec);
        if (mCurrentTab == -1) {
          setCurrentTab(0);
        }
        return;
      }
      throw new IllegalArgumentException("you must specify a way to create the tab content");
    }
    throw new IllegalArgumentException("you must specify a way to create the tab indicator.");
  }
  
  public void clearAllTabs()
  {
    mTabWidget.removeAllViews();
    initTabHost();
    mTabContent.removeAllViews();
    mTabSpecs.clear();
    requestLayout();
    invalidate();
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    boolean bool = super.dispatchKeyEvent(paramKeyEvent);
    if ((!bool) && (paramKeyEvent.getAction() == 0) && (mCurrentView != null) && (mCurrentView.isRootNamespace()) && (mCurrentView.hasFocus()))
    {
      int i = getTabWidgetLocation();
      int j;
      int k;
      if (i != 0)
      {
        switch (i)
        {
        default: 
          i = 19;
          j = 33;
          k = 2;
          break;
        case 3: 
          i = 20;
          j = 130;
          k = 4;
          break;
        case 2: 
          i = 22;
          j = 66;
          k = 3;
          break;
        }
      }
      else
      {
        i = 21;
        j = 17;
        k = 1;
      }
      if ((paramKeyEvent.getKeyCode() == i) && (mCurrentView.findFocus().focusSearch(j) == null))
      {
        mTabWidget.getChildTabViewAt(mCurrentTab).requestFocus();
        playSoundEffect(k);
        return true;
      }
    }
    return bool;
  }
  
  public void dispatchWindowFocusChanged(boolean paramBoolean)
  {
    if (mCurrentView != null) {
      mCurrentView.dispatchWindowFocusChanged(paramBoolean);
    }
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return TabHost.class.getName();
  }
  
  public int getCurrentTab()
  {
    return mCurrentTab;
  }
  
  public String getCurrentTabTag()
  {
    if ((mCurrentTab >= 0) && (mCurrentTab < mTabSpecs.size())) {
      return ((TabSpec)mTabSpecs.get(mCurrentTab)).getTag();
    }
    return null;
  }
  
  public View getCurrentTabView()
  {
    if ((mCurrentTab >= 0) && (mCurrentTab < mTabSpecs.size())) {
      return mTabWidget.getChildTabViewAt(mCurrentTab);
    }
    return null;
  }
  
  public View getCurrentView()
  {
    return mCurrentView;
  }
  
  public FrameLayout getTabContentView()
  {
    return mTabContent;
  }
  
  public TabWidget getTabWidget()
  {
    return mTabWidget;
  }
  
  public TabSpec newTabSpec(String paramString)
  {
    if (paramString != null) {
      return new TabSpec(paramString, null);
    }
    throw new IllegalArgumentException("tag must be non-null");
  }
  
  public void onTouchModeChanged(boolean paramBoolean) {}
  
  public void sendAccessibilityEventInternal(int paramInt) {}
  
  public void setCurrentTab(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < mTabSpecs.size()))
    {
      if (paramInt == mCurrentTab) {
        return;
      }
      if (mCurrentTab != -1) {
        mTabSpecs.get(mCurrentTab)).mContentStrategy.tabClosed();
      }
      mCurrentTab = paramInt;
      TabSpec localTabSpec = (TabSpec)mTabSpecs.get(paramInt);
      mTabWidget.focusCurrentTab(mCurrentTab);
      mCurrentView = mContentStrategy.getContentView();
      if (mCurrentView.getParent() == null) {
        mTabContent.addView(mCurrentView, new ViewGroup.LayoutParams(-1, -1));
      }
      if (!mTabWidget.hasFocus()) {
        mCurrentView.requestFocus();
      }
      invokeOnTabChangeListener();
      return;
    }
  }
  
  public void setCurrentTabByTag(String paramString)
  {
    int i = 0;
    int j = mTabSpecs.size();
    while (i < j)
    {
      if (((TabSpec)mTabSpecs.get(i)).getTag().equals(paramString))
      {
        setCurrentTab(i);
        break;
      }
      i++;
    }
  }
  
  public void setOnTabChangedListener(OnTabChangeListener paramOnTabChangeListener)
  {
    mOnTabChangeListener = paramOnTabChangeListener;
  }
  
  public void setup()
  {
    mTabWidget = ((TabWidget)findViewById(16908307));
    if (mTabWidget != null)
    {
      mTabKeyListener = new View.OnKeyListener()
      {
        public boolean onKey(View paramAnonymousView, int paramAnonymousInt, KeyEvent paramAnonymousKeyEvent)
        {
          if (KeyEvent.isModifierKey(paramAnonymousInt)) {
            return false;
          }
          if (paramAnonymousInt != 66) {
            switch (paramAnonymousInt)
            {
            default: 
              switch (paramAnonymousInt)
              {
              default: 
                mTabContent.requestFocus(2);
                return mTabContent.dispatchKeyEvent(paramAnonymousKeyEvent);
              }
              break;
            }
          }
          return false;
        }
      };
      mTabWidget.setTabSelectionListener(new TabWidget.OnTabSelectionChanged()
      {
        public void onTabSelectionChanged(int paramAnonymousInt, boolean paramAnonymousBoolean)
        {
          setCurrentTab(paramAnonymousInt);
          if (paramAnonymousBoolean) {
            mTabContent.requestFocus(2);
          }
        }
      });
      mTabContent = ((FrameLayout)findViewById(16908305));
      if (mTabContent != null) {
        return;
      }
      throw new RuntimeException("Your TabHost must have a FrameLayout whose id attribute is 'android.R.id.tabcontent'");
    }
    throw new RuntimeException("Your TabHost must have a TabWidget whose id attribute is 'android.R.id.tabs'");
  }
  
  public void setup(LocalActivityManager paramLocalActivityManager)
  {
    setup();
    mLocalActivityManager = paramLocalActivityManager;
  }
  
  private static abstract interface ContentStrategy
  {
    public abstract View getContentView();
    
    public abstract void tabClosed();
  }
  
  private class FactoryContentStrategy
    implements TabHost.ContentStrategy
  {
    private TabHost.TabContentFactory mFactory;
    private View mTabContent;
    private final CharSequence mTag;
    
    public FactoryContentStrategy(CharSequence paramCharSequence, TabHost.TabContentFactory paramTabContentFactory)
    {
      mTag = paramCharSequence;
      mFactory = paramTabContentFactory;
    }
    
    public View getContentView()
    {
      if (mTabContent == null) {
        mTabContent = mFactory.createTabContent(mTag.toString());
      }
      mTabContent.setVisibility(0);
      return mTabContent;
    }
    
    public void tabClosed()
    {
      mTabContent.setVisibility(8);
    }
  }
  
  private static abstract interface IndicatorStrategy
  {
    public abstract View createIndicatorView();
  }
  
  private class IntentContentStrategy
    implements TabHost.ContentStrategy
  {
    private final Intent mIntent;
    private View mLaunchedView;
    private final String mTag;
    
    private IntentContentStrategy(String paramString, Intent paramIntent)
    {
      mTag = paramString;
      mIntent = paramIntent;
    }
    
    public View getContentView()
    {
      if (mLocalActivityManager != null)
      {
        Object localObject = mLocalActivityManager.startActivity(mTag, mIntent);
        if (localObject != null) {
          localObject = ((Window)localObject).getDecorView();
        } else {
          localObject = null;
        }
        if ((mLaunchedView != localObject) && (mLaunchedView != null) && (mLaunchedView.getParent() != null)) {
          mTabContent.removeView(mLaunchedView);
        }
        mLaunchedView = ((View)localObject);
        if (mLaunchedView != null)
        {
          mLaunchedView.setVisibility(0);
          mLaunchedView.setFocusableInTouchMode(true);
          ((ViewGroup)mLaunchedView).setDescendantFocusability(262144);
        }
        return mLaunchedView;
      }
      throw new IllegalStateException("Did you forget to call 'public void setup(LocalActivityManager activityGroup)'?");
    }
    
    public void tabClosed()
    {
      if (mLaunchedView != null) {
        mLaunchedView.setVisibility(8);
      }
    }
  }
  
  private class LabelAndIconIndicatorStrategy
    implements TabHost.IndicatorStrategy
  {
    private final Drawable mIcon;
    private final CharSequence mLabel;
    
    private LabelAndIconIndicatorStrategy(CharSequence paramCharSequence, Drawable paramDrawable)
    {
      mLabel = paramCharSequence;
      mIcon = paramDrawable;
    }
    
    public View createIndicatorView()
    {
      Context localContext = getContext();
      View localView = ((LayoutInflater)localContext.getSystemService("layout_inflater")).inflate(mTabLayoutId, mTabWidget, false);
      TextView localTextView = (TextView)localView.findViewById(16908310);
      ImageView localImageView = (ImageView)localView.findViewById(16908294);
      int i = localImageView.getVisibility();
      int j = 1;
      if (i == 8) {
        i = 1;
      } else {
        i = 0;
      }
      int k = j;
      if (i != 0) {
        if (TextUtils.isEmpty(mLabel)) {
          k = j;
        } else {
          k = 0;
        }
      }
      localTextView.setText(mLabel);
      if ((k != 0) && (mIcon != null))
      {
        localImageView.setImageDrawable(mIcon);
        localImageView.setVisibility(0);
      }
      if (getApplicationInfotargetSdkVersion <= 4)
      {
        localView.setBackgroundResource(17303875);
        localTextView.setTextColor(localContext.getColorStateList(17170880));
      }
      return localView;
    }
  }
  
  private class LabelIndicatorStrategy
    implements TabHost.IndicatorStrategy
  {
    private final CharSequence mLabel;
    
    private LabelIndicatorStrategy(CharSequence paramCharSequence)
    {
      mLabel = paramCharSequence;
    }
    
    public View createIndicatorView()
    {
      Context localContext = getContext();
      View localView = ((LayoutInflater)localContext.getSystemService("layout_inflater")).inflate(mTabLayoutId, mTabWidget, false);
      TextView localTextView = (TextView)localView.findViewById(16908310);
      localTextView.setText(mLabel);
      if (getApplicationInfotargetSdkVersion <= 4)
      {
        localView.setBackgroundResource(17303875);
        localTextView.setTextColor(localContext.getColorStateList(17170880));
      }
      return localView;
    }
  }
  
  public static abstract interface OnTabChangeListener
  {
    public abstract void onTabChanged(String paramString);
  }
  
  public static abstract interface TabContentFactory
  {
    public abstract View createTabContent(String paramString);
  }
  
  public class TabSpec
  {
    private TabHost.ContentStrategy mContentStrategy;
    private TabHost.IndicatorStrategy mIndicatorStrategy;
    private final String mTag;
    
    private TabSpec(String paramString)
    {
      mTag = paramString;
    }
    
    public String getTag()
    {
      return mTag;
    }
    
    public TabSpec setContent(int paramInt)
    {
      mContentStrategy = new TabHost.ViewIdContentStrategy(TabHost.this, paramInt, null);
      return this;
    }
    
    public TabSpec setContent(Intent paramIntent)
    {
      mContentStrategy = new TabHost.IntentContentStrategy(TabHost.this, mTag, paramIntent, null);
      return this;
    }
    
    public TabSpec setContent(TabHost.TabContentFactory paramTabContentFactory)
    {
      mContentStrategy = new TabHost.FactoryContentStrategy(TabHost.this, mTag, paramTabContentFactory);
      return this;
    }
    
    public TabSpec setIndicator(View paramView)
    {
      mIndicatorStrategy = new TabHost.ViewIndicatorStrategy(TabHost.this, paramView, null);
      return this;
    }
    
    public TabSpec setIndicator(CharSequence paramCharSequence)
    {
      mIndicatorStrategy = new TabHost.LabelIndicatorStrategy(TabHost.this, paramCharSequence, null);
      return this;
    }
    
    public TabSpec setIndicator(CharSequence paramCharSequence, Drawable paramDrawable)
    {
      mIndicatorStrategy = new TabHost.LabelAndIconIndicatorStrategy(TabHost.this, paramCharSequence, paramDrawable, null);
      return this;
    }
  }
  
  private class ViewIdContentStrategy
    implements TabHost.ContentStrategy
  {
    private final View mView;
    
    private ViewIdContentStrategy(int paramInt)
    {
      mView = mTabContent.findViewById(paramInt);
      if (mView != null)
      {
        mView.setVisibility(8);
        return;
      }
      this$1 = new StringBuilder();
      append("Could not create tab content because could not find view with id ");
      append(paramInt);
      throw new RuntimeException(toString());
    }
    
    public View getContentView()
    {
      mView.setVisibility(0);
      return mView;
    }
    
    public void tabClosed()
    {
      mView.setVisibility(8);
    }
  }
  
  private class ViewIndicatorStrategy
    implements TabHost.IndicatorStrategy
  {
    private final View mView;
    
    private ViewIndicatorStrategy(View paramView)
    {
      mView = paramView;
    }
    
    public View createIndicatorView()
    {
      return mView;
    }
  }
}
