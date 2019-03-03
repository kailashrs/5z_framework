package android.os;

import android.content.pm.UserInfo;
import android.graphics.Bitmap;

public abstract class UserManagerInternal
{
  public static final int CAMERA_DISABLED_GLOBALLY = 2;
  public static final int CAMERA_DISABLED_LOCALLY = 1;
  public static final int CAMERA_NOT_DISABLED = 0;
  
  public UserManagerInternal() {}
  
  public abstract void addUserRestrictionsListener(UserRestrictionsListener paramUserRestrictionsListener);
  
  public abstract UserInfo createUserEvenWhenDisallowed(String paramString, int paramInt, String[] paramArrayOfString);
  
  public abstract boolean exists(int paramInt);
  
  public abstract Bundle getBaseUserRestrictions(int paramInt);
  
  public abstract int getProfileParentId(int paramInt);
  
  public abstract int[] getUserIds();
  
  public abstract boolean getUserRestriction(int paramInt, String paramString);
  
  public abstract boolean isProfileAccessible(int paramInt1, int paramInt2, String paramString, boolean paramBoolean);
  
  public abstract boolean isSettingRestrictedForUser(String paramString1, int paramInt1, String paramString2, int paramInt2);
  
  public abstract boolean isUserInitialized(int paramInt);
  
  public abstract boolean isUserRunning(int paramInt);
  
  public abstract boolean isUserUnlocked(int paramInt);
  
  public abstract boolean isUserUnlockingOrUnlocked(int paramInt);
  
  public abstract void onEphemeralUserStop(int paramInt);
  
  public abstract void removeAllUsers();
  
  public abstract boolean removeUserEvenWhenDisallowed(int paramInt);
  
  public abstract void removeUserRestrictionsListener(UserRestrictionsListener paramUserRestrictionsListener);
  
  public abstract void removeUserState(int paramInt);
  
  public abstract void setBaseUserRestrictionsByDpmsForMigration(int paramInt, Bundle paramBundle);
  
  public abstract void setDeviceManaged(boolean paramBoolean);
  
  public abstract void setDevicePolicyUserRestrictions(int paramInt1, Bundle paramBundle, boolean paramBoolean, int paramInt2);
  
  public abstract void setForceEphemeralUsers(boolean paramBoolean);
  
  public abstract void setUserIcon(int paramInt, Bitmap paramBitmap);
  
  public abstract void setUserManaged(int paramInt, boolean paramBoolean);
  
  public abstract void setUserState(int paramInt1, int paramInt2);
  
  public static abstract interface UserRestrictionsListener
  {
    public abstract void onUserRestrictionsChanged(int paramInt, Bundle paramBundle1, Bundle paramBundle2);
  }
}
