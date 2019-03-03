package android.content.pm;

import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;

public class LauncherActivityInfo
{
  private static final String TAG = "LauncherActivityInfo";
  private ActivityInfo mActivityInfo;
  private ComponentName mComponentName;
  private final PackageManager mPm;
  private UserHandle mUser;
  
  LauncherActivityInfo(Context paramContext)
  {
    mPm = paramContext.getPackageManager();
  }
  
  LauncherActivityInfo(Context paramContext, ActivityInfo paramActivityInfo, UserHandle paramUserHandle)
  {
    this(paramContext);
    mActivityInfo = paramActivityInfo;
    mComponentName = new ComponentName(packageName, name);
    mUser = paramUserHandle;
  }
  
  public int getApplicationFlags()
  {
    return mActivityInfo.applicationInfo.flags;
  }
  
  public ApplicationInfo getApplicationInfo()
  {
    return mActivityInfo.applicationInfo;
  }
  
  public Drawable getBadgedIcon(int paramInt)
  {
    Drawable localDrawable = getIcon(paramInt);
    return mPm.getUserBadgedIcon(localDrawable, mUser);
  }
  
  public ComponentName getComponentName()
  {
    return mComponentName;
  }
  
  public long getFirstInstallTime()
  {
    try
    {
      long l = mPm.getPackageInfo(mActivityInfo.packageName, 8192).firstInstallTime;
      return l;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException) {}
    return 0L;
  }
  
  public Drawable getIcon(int paramInt)
  {
    int i = mActivityInfo.getIconResource();
    Object localObject1 = null;
    Object localObject2 = localObject1;
    Object localObject3;
    if (paramInt != 0)
    {
      localObject2 = localObject1;
      if (i != 0) {
        try
        {
          localObject2 = mPm.getResourcesForApplication(mActivityInfo.applicationInfo).getDrawableForDensity(i, paramInt);
        }
        catch (PackageManager.NameNotFoundException|Resources.NotFoundException localNameNotFoundException)
        {
          localObject3 = localObject1;
        }
      }
    }
    localObject1 = localObject3;
    if (localObject3 == null) {
      localObject1 = mActivityInfo.loadIcon(mPm);
    }
    return localObject1;
  }
  
  public CharSequence getLabel()
  {
    return mActivityInfo.loadLabel(mPm);
  }
  
  public String getName()
  {
    return mActivityInfo.name;
  }
  
  public UserHandle getUser()
  {
    return mUser;
  }
}
