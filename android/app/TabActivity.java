package android.app;

import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

@Deprecated
public class TabActivity
  extends ActivityGroup
{
  private String mDefaultTab = null;
  private int mDefaultTabIndex = -1;
  private TabHost mTabHost;
  
  public TabActivity() {}
  
  private void ensureTabHost()
  {
    if (mTabHost == null) {
      setContentView(17367325);
    }
  }
  
  public TabHost getTabHost()
  {
    ensureTabHost();
    return mTabHost;
  }
  
  public TabWidget getTabWidget()
  {
    return mTabHost.getTabWidget();
  }
  
  protected void onChildTitleChanged(Activity paramActivity, CharSequence paramCharSequence)
  {
    if (getLocalActivityManager().getCurrentActivity() == paramActivity)
    {
      paramActivity = mTabHost.getCurrentTabView();
      if ((paramActivity != null) && ((paramActivity instanceof TextView))) {
        ((TextView)paramActivity).setText(paramCharSequence);
      }
    }
  }
  
  public void onContentChanged()
  {
    super.onContentChanged();
    mTabHost = ((TabHost)findViewById(16908306));
    if (mTabHost != null)
    {
      mTabHost.setup(getLocalActivityManager());
      return;
    }
    throw new RuntimeException("Your content must have a TabHost whose id attribute is 'android.R.id.tabhost'");
  }
  
  protected void onPostCreate(Bundle paramBundle)
  {
    super.onPostCreate(paramBundle);
    ensureTabHost();
    if (mTabHost.getCurrentTab() == -1) {
      mTabHost.setCurrentTab(0);
    }
  }
  
  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    super.onRestoreInstanceState(paramBundle);
    ensureTabHost();
    paramBundle = paramBundle.getString("currentTab");
    if (paramBundle != null) {
      mTabHost.setCurrentTabByTag(paramBundle);
    }
    if (mTabHost.getCurrentTab() < 0) {
      if (mDefaultTab != null) {
        mTabHost.setCurrentTabByTag(mDefaultTab);
      } else if (mDefaultTabIndex >= 0) {
        mTabHost.setCurrentTab(mDefaultTabIndex);
      }
    }
  }
  
  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    String str = mTabHost.getCurrentTabTag();
    if (str != null) {
      paramBundle.putString("currentTab", str);
    }
  }
  
  public void setDefaultTab(int paramInt)
  {
    mDefaultTab = null;
    mDefaultTabIndex = paramInt;
  }
  
  public void setDefaultTab(String paramString)
  {
    mDefaultTab = paramString;
    mDefaultTabIndex = -1;
  }
}
