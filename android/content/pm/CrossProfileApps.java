package android.content.pm;

import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import com.android.internal.util.UserIcons;
import java.util.List;

public class CrossProfileApps
{
  private final Context mContext;
  private final Resources mResources;
  private final ICrossProfileApps mService;
  private final UserManager mUserManager;
  
  public CrossProfileApps(Context paramContext, ICrossProfileApps paramICrossProfileApps)
  {
    mContext = paramContext;
    mService = paramICrossProfileApps;
    mUserManager = ((UserManager)paramContext.getSystemService(UserManager.class));
    mResources = paramContext.getResources();
  }
  
  private void verifyCanAccessUser(UserHandle paramUserHandle)
  {
    if (getTargetUserProfiles().contains(paramUserHandle)) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Not allowed to access ");
    localStringBuilder.append(paramUserHandle);
    throw new SecurityException(localStringBuilder.toString());
  }
  
  public Drawable getProfileSwitchingIconDrawable(UserHandle paramUserHandle)
  {
    verifyCanAccessUser(paramUserHandle);
    if (mUserManager.isManagedProfile(paramUserHandle.getIdentifier())) {
      return mResources.getDrawable(17302595, null);
    }
    return UserIcons.getDefaultUserIcon(mResources, 0, true);
  }
  
  public CharSequence getProfileSwitchingLabel(UserHandle paramUserHandle)
  {
    verifyCanAccessUser(paramUserHandle);
    int i;
    if (mUserManager.isManagedProfile(paramUserHandle.getIdentifier())) {
      i = 17040303;
    } else {
      i = 17041163;
    }
    return mResources.getString(i);
  }
  
  public List<UserHandle> getTargetUserProfiles()
  {
    try
    {
      List localList = mService.getTargetUserProfiles(mContext.getPackageName());
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void startMainActivity(ComponentName paramComponentName, UserHandle paramUserHandle)
  {
    try
    {
      mService.startActivityAsUser(mContext.getIApplicationThread(), mContext.getPackageName(), paramComponentName, paramUserHandle);
      return;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
}
