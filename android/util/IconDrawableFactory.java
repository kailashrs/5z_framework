package android.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;
import android.os.UserManager;
import com.android.internal.annotations.VisibleForTesting;

public class IconDrawableFactory
{
  @VisibleForTesting
  public static final int[] CORP_BADGE_COLORS = { 17170825, 17170826, 17170827 };
  protected final Context mContext;
  protected final boolean mEmbedShadow;
  protected final LauncherIcons mLauncherIcons;
  protected final PackageManager mPm;
  protected final UserManager mUm;
  
  private IconDrawableFactory(Context paramContext, boolean paramBoolean)
  {
    mContext = paramContext;
    mPm = paramContext.getPackageManager();
    mUm = ((UserManager)paramContext.getSystemService(UserManager.class));
    mLauncherIcons = new LauncherIcons(paramContext);
    mEmbedShadow = paramBoolean;
  }
  
  public static int getUserBadgeColor(UserManager paramUserManager, int paramInt)
  {
    int i = paramUserManager.getManagedProfileBadge(paramInt);
    paramInt = i;
    if (i < 0) {
      paramInt = 0;
    }
    paramInt = CORP_BADGE_COLORS[(paramInt % CORP_BADGE_COLORS.length)];
    return Resources.getSystem().getColor(paramInt, null);
  }
  
  public static IconDrawableFactory newInstance(Context paramContext)
  {
    return new IconDrawableFactory(paramContext, true);
  }
  
  public static IconDrawableFactory newInstance(Context paramContext, boolean paramBoolean)
  {
    return new IconDrawableFactory(paramContext, paramBoolean);
  }
  
  public Drawable getBadgedIcon(ApplicationInfo paramApplicationInfo)
  {
    return getBadgedIcon(paramApplicationInfo, UserHandle.getUserId(uid));
  }
  
  public Drawable getBadgedIcon(ApplicationInfo paramApplicationInfo, int paramInt)
  {
    return getBadgedIcon(paramApplicationInfo, paramApplicationInfo, paramInt);
  }
  
  public Drawable getBadgedIcon(PackageItemInfo paramPackageItemInfo, ApplicationInfo paramApplicationInfo, int paramInt)
  {
    paramPackageItemInfo = mPm.loadUnbadgedItemIcon(paramPackageItemInfo, paramApplicationInfo);
    if ((!mEmbedShadow) && (!needsBadging(paramApplicationInfo, paramInt))) {
      return paramPackageItemInfo;
    }
    Drawable localDrawable = getShadowedIcon(paramPackageItemInfo);
    paramPackageItemInfo = localDrawable;
    if (paramApplicationInfo.isInstantApp())
    {
      int i = Resources.getSystem().getColor(17170705, null);
      paramPackageItemInfo = mLauncherIcons.getBadgedDrawable(localDrawable, 17302667, i);
    }
    paramApplicationInfo = paramPackageItemInfo;
    if (mUm.isManagedProfile(paramInt)) {
      paramApplicationInfo = mLauncherIcons.getBadgedDrawable(paramPackageItemInfo, 17302601, getUserBadgeColor(mUm, paramInt));
    }
    paramPackageItemInfo = paramApplicationInfo;
    if (mUm.isTwinApps(paramInt)) {
      paramPackageItemInfo = mLauncherIcons.getBadgedDrawable(paramApplicationInfo, 17302604, 0, 0);
    }
    return paramPackageItemInfo;
  }
  
  public Drawable getShadowedIcon(Drawable paramDrawable)
  {
    return mLauncherIcons.wrapIconDrawableWithShadow(paramDrawable);
  }
  
  protected boolean needsBadging(ApplicationInfo paramApplicationInfo, int paramInt)
  {
    boolean bool;
    if ((!paramApplicationInfo.isInstantApp()) && (!mUm.isManagedProfile(paramInt)) && (!mUm.isTwinApps(paramInt))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
}
