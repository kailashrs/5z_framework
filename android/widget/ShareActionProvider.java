package android.widget;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources.Theme;
import android.util.TypedValue;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;

public class ShareActionProvider
  extends ActionProvider
{
  private static final int DEFAULT_INITIAL_ACTIVITY_COUNT = 4;
  public static final String DEFAULT_SHARE_HISTORY_FILE_NAME = "share_history.xml";
  private final Context mContext;
  private int mMaxShownActivityCount = 4;
  private ActivityChooserModel.OnChooseActivityListener mOnChooseActivityListener;
  private final ShareMenuItemOnMenuItemClickListener mOnMenuItemClickListener = new ShareMenuItemOnMenuItemClickListener(null);
  private OnShareTargetSelectedListener mOnShareTargetSelectedListener;
  private String mShareHistoryFileName = "share_history.xml";
  
  public ShareActionProvider(Context paramContext)
  {
    super(paramContext);
    mContext = paramContext;
  }
  
  private void setActivityChooserPolicyIfNeeded()
  {
    if (mOnShareTargetSelectedListener == null) {
      return;
    }
    if (mOnChooseActivityListener == null) {
      mOnChooseActivityListener = new ShareActivityChooserModelPolicy(null);
    }
    ActivityChooserModel.get(mContext, mShareHistoryFileName).setOnChooseActivityListener(mOnChooseActivityListener);
  }
  
  public boolean hasSubMenu()
  {
    return true;
  }
  
  public View onCreateActionView()
  {
    ActivityChooserView localActivityChooserView = new ActivityChooserView(mContext);
    if (!localActivityChooserView.isInEditMode()) {
      localActivityChooserView.setActivityChooserModel(ActivityChooserModel.get(mContext, mShareHistoryFileName));
    }
    TypedValue localTypedValue = new TypedValue();
    mContext.getTheme().resolveAttribute(16843897, localTypedValue, true);
    localActivityChooserView.setExpandActivityOverflowButtonDrawable(mContext.getDrawable(resourceId));
    localActivityChooserView.setProvider(this);
    localActivityChooserView.setDefaultActionButtonContentDescription(17040993);
    localActivityChooserView.setExpandActivityOverflowButtonContentDescription(17040992);
    return localActivityChooserView;
  }
  
  public void onPrepareSubMenu(SubMenu paramSubMenu)
  {
    paramSubMenu.clear();
    ActivityChooserModel localActivityChooserModel = ActivityChooserModel.get(mContext, mShareHistoryFileName);
    PackageManager localPackageManager = mContext.getPackageManager();
    int i = localActivityChooserModel.getActivityCount();
    int j = Math.min(i, mMaxShownActivityCount);
    Object localObject;
    for (int k = 0; k < j; k++)
    {
      localObject = localActivityChooserModel.getActivity(k);
      paramSubMenu.add(0, k, k, ((ResolveInfo)localObject).loadLabel(localPackageManager)).setIcon(((ResolveInfo)localObject).loadIcon(localPackageManager)).setOnMenuItemClickListener(mOnMenuItemClickListener);
    }
    if (j < i)
    {
      localObject = paramSubMenu.addSubMenu(0, j, j, mContext.getString(17039436));
      for (k = 0; k < i; k++)
      {
        paramSubMenu = localActivityChooserModel.getActivity(k);
        ((SubMenu)localObject).add(0, k, k, paramSubMenu.loadLabel(localPackageManager)).setIcon(paramSubMenu.loadIcon(localPackageManager)).setOnMenuItemClickListener(mOnMenuItemClickListener);
      }
    }
  }
  
  public void setOnShareTargetSelectedListener(OnShareTargetSelectedListener paramOnShareTargetSelectedListener)
  {
    mOnShareTargetSelectedListener = paramOnShareTargetSelectedListener;
    setActivityChooserPolicyIfNeeded();
  }
  
  public void setShareHistoryFileName(String paramString)
  {
    mShareHistoryFileName = paramString;
    setActivityChooserPolicyIfNeeded();
  }
  
  public void setShareIntent(Intent paramIntent)
  {
    if (paramIntent != null)
    {
      String str = paramIntent.getAction();
      if (("android.intent.action.SEND".equals(str)) || ("android.intent.action.SEND_MULTIPLE".equals(str))) {
        paramIntent.addFlags(134742016);
      }
    }
    ActivityChooserModel.get(mContext, mShareHistoryFileName).setIntent(paramIntent);
  }
  
  public static abstract interface OnShareTargetSelectedListener
  {
    public abstract boolean onShareTargetSelected(ShareActionProvider paramShareActionProvider, Intent paramIntent);
  }
  
  private class ShareActivityChooserModelPolicy
    implements ActivityChooserModel.OnChooseActivityListener
  {
    private ShareActivityChooserModelPolicy() {}
    
    public boolean onChooseActivity(ActivityChooserModel paramActivityChooserModel, Intent paramIntent)
    {
      if (mOnShareTargetSelectedListener != null) {
        mOnShareTargetSelectedListener.onShareTargetSelected(ShareActionProvider.this, paramIntent);
      }
      return false;
    }
  }
  
  private class ShareMenuItemOnMenuItemClickListener
    implements MenuItem.OnMenuItemClickListener
  {
    private ShareMenuItemOnMenuItemClickListener() {}
    
    public boolean onMenuItemClick(MenuItem paramMenuItem)
    {
      Intent localIntent = ActivityChooserModel.get(mContext, mShareHistoryFileName).chooseActivity(paramMenuItem.getItemId());
      if (localIntent != null)
      {
        paramMenuItem = localIntent.getAction();
        if (("android.intent.action.SEND".equals(paramMenuItem)) || ("android.intent.action.SEND_MULTIPLE".equals(paramMenuItem))) {
          localIntent.addFlags(134742016);
        }
        mContext.startActivity(localIntent);
      }
      return true;
    }
  }
}
