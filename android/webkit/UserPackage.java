package android.webkit;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.UserInfo;
import android.os.UserManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserPackage
{
  public static final int MINIMUM_SUPPORTED_SDK = 28;
  private final PackageInfo mPackageInfo;
  private final UserInfo mUserInfo;
  
  public UserPackage(UserInfo paramUserInfo, PackageInfo paramPackageInfo)
  {
    mUserInfo = paramUserInfo;
    mPackageInfo = paramPackageInfo;
  }
  
  private static List<UserInfo> getAllUsers(Context paramContext)
  {
    return ((UserManager)paramContext.getSystemService("user")).getUsers(false);
  }
  
  public static List<UserPackage> getPackageInfosAllUsers(Context paramContext, String paramString, int paramInt)
  {
    Object localObject = getAllUsers(paramContext);
    ArrayList localArrayList = new ArrayList(((List)localObject).size());
    Iterator localIterator = ((List)localObject).iterator();
    while (localIterator.hasNext())
    {
      UserInfo localUserInfo = (UserInfo)localIterator.next();
      localObject = null;
      try
      {
        PackageInfo localPackageInfo = paramContext.getPackageManager().getPackageInfoAsUser(paramString, paramInt, id);
        localObject = localPackageInfo;
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException) {}
      localArrayList.add(new UserPackage(localUserInfo, (PackageInfo)localObject));
    }
    return localArrayList;
  }
  
  public static boolean hasCorrectTargetSdkVersion(PackageInfo paramPackageInfo)
  {
    boolean bool;
    if (applicationInfo.targetSdkVersion >= 28) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public PackageInfo getPackageInfo()
  {
    return mPackageInfo;
  }
  
  public UserInfo getUserInfo()
  {
    return mUserInfo;
  }
  
  public boolean isEnabledPackage()
  {
    if (mPackageInfo == null) {
      return false;
    }
    return mPackageInfo.applicationInfo.enabled;
  }
  
  public boolean isInstalledPackage()
  {
    PackageInfo localPackageInfo = mPackageInfo;
    boolean bool1 = false;
    if (localPackageInfo == null) {
      return false;
    }
    boolean bool2 = bool1;
    if ((mPackageInfo.applicationInfo.flags & 0x800000) != 0)
    {
      bool2 = bool1;
      if ((mPackageInfo.applicationInfo.privateFlags & 0x1) == 0) {
        bool2 = true;
      }
    }
    return bool2;
  }
}
