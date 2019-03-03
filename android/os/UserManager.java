package android.os;

import android.accounts.AccountManager;
import android.annotation.SystemApi;
import android.app.ActivityManager;
import android.app.IActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.os.RoSystemProperties;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserManager
{
  private static final String ACTION_CREATE_USER = "android.os.action.CREATE_USER";
  @SystemApi
  public static final String ACTION_USER_RESTRICTIONS_CHANGED = "android.os.action.USER_RESTRICTIONS_CHANGED";
  public static final String ALLOW_PARENT_PROFILE_APP_LINKING = "allow_parent_profile_app_linking";
  private static final boolean DEBUG = false;
  public static final String DISALLOW_ADD_MANAGED_PROFILE = "no_add_managed_profile";
  public static final String DISALLOW_ADD_USER = "no_add_user";
  public static final String DISALLOW_ADJUST_VOLUME = "no_adjust_volume";
  public static final String DISALLOW_AIRPLANE_MODE = "no_airplane_mode";
  public static final String DISALLOW_AMBIENT_DISPLAY = "no_ambient_display";
  public static final String DISALLOW_APPS_CONTROL = "no_control_apps";
  public static final String DISALLOW_AUTOFILL = "no_autofill";
  public static final String DISALLOW_BLUETOOTH = "no_bluetooth";
  public static final String DISALLOW_BLUETOOTH_SHARING = "no_bluetooth_sharing";
  public static final String DISALLOW_CAMERA = "no_camera";
  public static final String DISALLOW_CONFIG_BLUETOOTH = "no_config_bluetooth";
  public static final String DISALLOW_CONFIG_BRIGHTNESS = "no_config_brightness";
  public static final String DISALLOW_CONFIG_CELL_BROADCASTS = "no_config_cell_broadcasts";
  public static final String DISALLOW_CONFIG_CREDENTIALS = "no_config_credentials";
  public static final String DISALLOW_CONFIG_DATE_TIME = "no_config_date_time";
  public static final String DISALLOW_CONFIG_LOCALE = "no_config_locale";
  public static final String DISALLOW_CONFIG_LOCATION = "no_config_location";
  public static final String DISALLOW_CONFIG_MOBILE_NETWORKS = "no_config_mobile_networks";
  public static final String DISALLOW_CONFIG_SCREEN_TIMEOUT = "no_config_screen_timeout";
  public static final String DISALLOW_CONFIG_TETHERING = "no_config_tethering";
  public static final String DISALLOW_CONFIG_VPN = "no_config_vpn";
  public static final String DISALLOW_CONFIG_WIFI = "no_config_wifi";
  public static final String DISALLOW_CREATE_WINDOWS = "no_create_windows";
  public static final String DISALLOW_CROSS_PROFILE_COPY_PASTE = "no_cross_profile_copy_paste";
  public static final String DISALLOW_DATA_ROAMING = "no_data_roaming";
  public static final String DISALLOW_DEBUGGING_FEATURES = "no_debugging_features";
  public static final String DISALLOW_FACTORY_RESET = "no_factory_reset";
  public static final String DISALLOW_FUN = "no_fun";
  public static final String DISALLOW_INSTALL_APPS = "no_install_apps";
  public static final String DISALLOW_INSTALL_UNKNOWN_SOURCES = "no_install_unknown_sources";
  public static final String DISALLOW_MODIFY_ACCOUNTS = "no_modify_accounts";
  public static final String DISALLOW_MOUNT_PHYSICAL_MEDIA = "no_physical_media";
  public static final String DISALLOW_NETWORK_RESET = "no_network_reset";
  @SystemApi
  @Deprecated
  public static final String DISALLOW_OEM_UNLOCK = "no_oem_unlock";
  public static final String DISALLOW_OUTGOING_BEAM = "no_outgoing_beam";
  public static final String DISALLOW_OUTGOING_CALLS = "no_outgoing_calls";
  public static final String DISALLOW_PRINTING = "no_printing";
  public static final String DISALLOW_RECORD_AUDIO = "no_record_audio";
  public static final String DISALLOW_REMOVE_MANAGED_PROFILE = "no_remove_managed_profile";
  public static final String DISALLOW_REMOVE_USER = "no_remove_user";
  @SystemApi
  public static final String DISALLOW_RUN_IN_BACKGROUND = "no_run_in_background";
  public static final String DISALLOW_SAFE_BOOT = "no_safe_boot";
  public static final String DISALLOW_SET_USER_ICON = "no_set_user_icon";
  public static final String DISALLOW_SET_WALLPAPER = "no_set_wallpaper";
  public static final String DISALLOW_SHARE_INTO_MANAGED_PROFILE = "no_sharing_into_profile";
  public static final String DISALLOW_SHARE_LOCATION = "no_share_location";
  public static final String DISALLOW_SMS = "no_sms";
  public static final String DISALLOW_SYSTEM_ERROR_DIALOGS = "no_system_error_dialogs";
  public static final String DISALLOW_UNIFIED_PASSWORD = "no_unified_password";
  public static final String DISALLOW_UNINSTALL_APPS = "no_uninstall_apps";
  public static final String DISALLOW_UNMUTE_DEVICE = "disallow_unmute_device";
  public static final String DISALLOW_UNMUTE_MICROPHONE = "no_unmute_microphone";
  public static final String DISALLOW_USB_FILE_TRANSFER = "no_usb_file_transfer";
  public static final String DISALLOW_USER_SWITCH = "no_user_switch";
  public static final String DISALLOW_WALLPAPER = "no_wallpaper";
  public static final String ENSURE_VERIFY_APPS = "ensure_verify_apps";
  public static final String EXTRA_USER_ACCOUNT_NAME = "android.os.extra.USER_ACCOUNT_NAME";
  public static final String EXTRA_USER_ACCOUNT_OPTIONS = "android.os.extra.USER_ACCOUNT_OPTIONS";
  public static final String EXTRA_USER_ACCOUNT_TYPE = "android.os.extra.USER_ACCOUNT_TYPE";
  public static final String EXTRA_USER_NAME = "android.os.extra.USER_NAME";
  public static final String KEY_RESTRICTIONS_PENDING = "restrictions_pending";
  public static final int PIN_VERIFICATION_FAILED_INCORRECT = -3;
  public static final int PIN_VERIFICATION_FAILED_NOT_SET = -2;
  public static final int PIN_VERIFICATION_SUCCESS = -1;
  private static final long RAM_BASE = 1073741824L;
  @SystemApi
  public static final int RESTRICTION_NOT_SET = 0;
  @SystemApi
  public static final int RESTRICTION_SOURCE_DEVICE_OWNER = 2;
  @SystemApi
  public static final int RESTRICTION_SOURCE_PROFILE_OWNER = 4;
  @SystemApi
  public static final int RESTRICTION_SOURCE_SYSTEM = 1;
  private static final String TAG = "UserManager";
  public static final int USER_CREATION_FAILED_NOT_PERMITTED = 1;
  public static final int USER_CREATION_FAILED_NO_MORE_USERS = 2;
  public static final int USER_OPERATION_ERROR_CURRENT_USER = 4;
  public static final int USER_OPERATION_ERROR_LOW_STORAGE = 5;
  public static final int USER_OPERATION_ERROR_MANAGED_PROFILE = 2;
  public static final int USER_OPERATION_ERROR_MAX_RUNNING_USERS = 3;
  public static final int USER_OPERATION_ERROR_MAX_USERS = 6;
  public static final int USER_OPERATION_ERROR_UNKNOWN = 1;
  public static final int USER_OPERATION_SUCCESS = 0;
  private final Context mContext;
  private Boolean mIsManagedProfileCached;
  private final IUserManager mService;
  
  public UserManager(Context paramContext, IUserManager paramIUserManager)
  {
    mService = paramIUserManager;
    mContext = paramContext.getApplicationContext();
  }
  
  public static Intent createUserCreationIntent(String paramString1, String paramString2, String paramString3, PersistableBundle paramPersistableBundle)
  {
    Intent localIntent = new Intent("android.os.action.CREATE_USER");
    if (paramString1 != null) {
      localIntent.putExtra("android.os.extra.USER_NAME", paramString1);
    }
    if ((paramString2 != null) && (paramString3 == null)) {
      throw new IllegalArgumentException("accountType must be specified if accountName is specified");
    }
    if (paramString2 != null) {
      localIntent.putExtra("android.os.extra.USER_ACCOUNT_NAME", paramString2);
    }
    if (paramString3 != null) {
      localIntent.putExtra("android.os.extra.USER_ACCOUNT_TYPE", paramString3);
    }
    if (paramPersistableBundle != null) {
      localIntent.putExtra("android.os.extra.USER_ACCOUNT_OPTIONS", paramPersistableBundle);
    }
    return localIntent;
  }
  
  public static UserManager get(Context paramContext)
  {
    return (UserManager)paramContext.getSystemService("user");
  }
  
  public static int getMaxSupportedUsers()
  {
    if (Build.ID.startsWith("JVP")) {
      return 1;
    }
    int i = 0;
    if (SystemProperties.getInt("ro.asus.is_verizon_device", 0) == 1) {
      i = 1;
    }
    if (i != 0) {
      return 1;
    }
    if ((ActivityManager.isLowRamDeviceStatic()) && ((getSystemgetConfigurationuiMode & 0xF) != 4)) {
      return 1;
    }
    try
    {
      Resources localResources;
      switch (ramSizeLevel())
      {
      default: 
        localResources = Resources.getSystem();
        break;
      case 4: 
        i = Resources.getSystem().getInteger(17694829);
        break;
      case 3: 
        i = Resources.getSystem().getInteger(17694828);
        break;
      case 2: 
        i = Resources.getSystem().getInteger(17694826);
        break;
      case 1: 
        i = Resources.getSystem().getInteger(17694827);
        break;
      }
      i = localResources.getInteger(17694826);
    }
    catch (Resources.NotFoundException localNotFoundException)
    {
      i = 1;
    }
    return SystemProperties.getInt("fw.max_users", i);
  }
  
  public static boolean isDeviceInDemoMode(Context paramContext)
  {
    paramContext = paramContext.getContentResolver();
    boolean bool = false;
    if (Settings.Global.getInt(paramContext, "device_demo_mode", 0) > 0) {
      bool = true;
    }
    return bool;
  }
  
  public static boolean isGuestUserEphemeral()
  {
    return Resources.getSystem().getBoolean(17956983);
  }
  
  private static boolean isMultiUserExist()
  {
    IUserManager localIUserManager = IUserManager.Stub.asInterface(ServiceManager.getService("user"));
    if (localIUserManager == null) {
      return false;
    }
    try
    {
      boolean bool = localIUserManager.isMultiUserExistNoCheck();
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public static boolean isSplitSystemUser()
  {
    return RoSystemProperties.FW_SYSTEM_USER_SPLIT;
  }
  
  private static int ramSizeLevel()
  {
    long l = Process.getTotalMemory();
    if (l <= 1.610612736E9D) {
      return 1;
    }
    if ((l > 1.610612736E9D) && (l <= 2147483648L)) {
      return 2;
    }
    if ((l > 2147483648L) && (l <= 3221225472L)) {
      return 3;
    }
    if (l > 3221225472L) {
      return 4;
    }
    return 1;
  }
  
  public static boolean supportsMultipleUsers()
  {
    boolean bool1 = false;
    int i;
    if (SystemProperties.getInt("ro.asus.is_verizon_device", 0) == 1) {
      i = 1;
    } else {
      i = 0;
    }
    if (i != 0) {
      return false;
    }
    if (isMultiUserExist())
    {
      Log.v("UserManager", "supportsMultipleUsers()=true");
      return true;
    }
    boolean bool2;
    try
    {
      Resources localResources;
      switch (ramSizeLevel())
      {
      default: 
        localResources = Resources.getSystem();
        break;
      case 4: 
        bool2 = Resources.getSystem().getBoolean(17956965);
        break;
      case 3: 
        bool2 = Resources.getSystem().getBoolean(17956964);
        break;
      case 2: 
        bool2 = Resources.getSystem().getBoolean(17956962);
        break;
      case 1: 
        bool2 = Resources.getSystem().getBoolean(17956963);
        break;
      }
      bool2 = localResources.getBoolean(17956962);
    }
    catch (Resources.NotFoundException localNotFoundException)
    {
      bool2 = false;
    }
    if ((getMaxSupportedUsers() > 1) && (SystemProperties.getBoolean("fw.show_multiuserui", bool2))) {
      bool2 = true;
    } else {
      bool2 = bool1;
    }
    return bool2;
  }
  
  public boolean canAddMoreManagedProfiles(int paramInt, boolean paramBoolean)
  {
    try
    {
      paramBoolean = mService.canAddMoreManagedProfiles(paramInt, paramBoolean);
      return paramBoolean;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean canAddMoreUsers()
  {
    boolean bool = true;
    List localList = getUsers(true);
    int i = localList.size();
    int j = 0;
    int k = 0;
    while (k < i)
    {
      int m = j;
      if (!((UserInfo)localList.get(k)).isGuest()) {
        m = j + 1;
      }
      k++;
      j = m;
    }
    if (j >= getMaxSupportedUsers()) {
      bool = false;
    }
    return bool;
  }
  
  public boolean canHaveRestrictedProfile(int paramInt)
  {
    try
    {
      boolean bool = mService.canHaveRestrictedProfile(paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean canSwitchUsers()
  {
    int i = Settings.Global.getInt(mContext.getContentResolver(), "allow_user_switching_when_system_user_locked", 0);
    boolean bool1 = true;
    if (i != 0) {
      i = 1;
    } else {
      i = 0;
    }
    boolean bool2 = isUserUnlocked(UserHandle.SYSTEM);
    int j;
    if (TelephonyManager.getDefault().getCallState() != 0) {
      j = 1;
    } else {
      j = 0;
    }
    boolean bool3 = hasUserRestriction("no_user_switch");
    if (((i == 0) && (!bool2)) || (j != 0) || (bool3)) {
      bool1 = false;
    }
    return bool1;
  }
  
  @SystemApi
  public void clearSeedAccountData()
  {
    try
    {
      mService.clearSeedAccountData();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public UserInfo createGuest(Context paramContext, String paramString)
  {
    try
    {
      paramString = mService.createUser(paramString, 4);
      if (paramString != null) {
        Settings.Secure.putStringForUser(paramContext.getContentResolver(), "skip_first_use_hints", "1", id);
      }
      return paramString;
    }
    catch (RemoteException paramContext)
    {
      throw paramContext.rethrowFromSystemServer();
    }
  }
  
  public UserInfo createProfileForUser(String paramString, int paramInt1, int paramInt2)
  {
    return createProfileForUser(paramString, paramInt1, paramInt2, null);
  }
  
  public UserInfo createProfileForUser(String paramString, int paramInt1, int paramInt2, String[] paramArrayOfString)
  {
    try
    {
      paramString = mService.createProfileForUser(paramString, paramInt1, paramInt2, paramArrayOfString);
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public UserInfo createProfileForUserEvenWhenDisallowed(String paramString, int paramInt1, int paramInt2, String[] paramArrayOfString)
  {
    try
    {
      paramString = mService.createProfileForUserEvenWhenDisallowed(paramString, paramInt1, paramInt2, paramArrayOfString);
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public UserInfo createRestrictedProfile(String paramString)
  {
    try
    {
      UserHandle localUserHandle = Process.myUserHandle();
      paramString = mService.createRestrictedProfile(paramString, localUserHandle.getIdentifier());
      if (paramString != null) {
        AccountManager.get(mContext).addSharedAccountsFromParentUser(localUserHandle, UserHandle.of(id));
      }
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public UserInfo createUser(String paramString, int paramInt)
  {
    try
    {
      paramString = mService.createUser(paramString, paramInt);
      if ((paramString != null) && (!paramString.isAdmin()) && (!paramString.isDemo()))
      {
        mService.setUserRestriction("no_sms", true, id);
        mService.setUserRestriction("no_outgoing_calls", true, id);
      }
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void evictCredentialEncryptionKey(int paramInt)
  {
    try
    {
      mService.evictCredentialEncryptionKey(paramInt);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public Bundle getApplicationRestrictions(String paramString)
  {
    try
    {
      paramString = mService.getApplicationRestrictions(paramString);
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public Bundle getApplicationRestrictions(String paramString, UserHandle paramUserHandle)
  {
    try
    {
      paramString = mService.getApplicationRestrictionsForUser(paramString, paramUserHandle.getIdentifier());
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public Drawable getBadgedDrawableForUser(Drawable paramDrawable, UserHandle paramUserHandle, Rect paramRect, int paramInt)
  {
    return mContext.getPackageManager().getUserBadgedDrawableForDensity(paramDrawable, paramUserHandle, paramRect, paramInt);
  }
  
  public Drawable getBadgedIconForUser(Drawable paramDrawable, UserHandle paramUserHandle)
  {
    return mContext.getPackageManager().getUserBadgedIcon(paramDrawable, paramUserHandle);
  }
  
  public CharSequence getBadgedLabelForUser(CharSequence paramCharSequence, UserHandle paramUserHandle)
  {
    return mContext.getPackageManager().getUserBadgedLabel(paramCharSequence, paramUserHandle);
  }
  
  public int getCredentialOwnerProfile(int paramInt)
  {
    try
    {
      paramInt = mService.getCredentialOwnerProfile(paramInt);
      return paramInt;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public Bundle getDefaultGuestRestrictions()
  {
    try
    {
      Bundle localBundle = mService.getDefaultGuestRestrictions();
      return localBundle;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int[] getEnabledProfileIds(int paramInt)
  {
    return getProfileIds(paramInt, true);
  }
  
  public List<UserInfo> getEnabledProfiles(int paramInt)
  {
    try
    {
      List localList = mService.getProfiles(paramInt, true);
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getManagedProfileBadge(int paramInt)
  {
    try
    {
      paramInt = mService.getManagedProfileBadge(paramInt);
      return paramInt;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public UserInfo getPrimaryUser()
  {
    try
    {
      UserInfo localUserInfo = mService.getPrimaryUser();
      return localUserInfo;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int[] getProfileIds(int paramInt, boolean paramBoolean)
  {
    try
    {
      int[] arrayOfInt = mService.getProfileIds(paramInt, paramBoolean);
      return arrayOfInt;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int[] getProfileIdsWithDisabled(int paramInt)
  {
    return getProfileIds(paramInt, false);
  }
  
  public UserInfo getProfileParent(int paramInt)
  {
    try
    {
      UserInfo localUserInfo = mService.getProfileParent(paramInt);
      return localUserInfo;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<UserInfo> getProfiles(int paramInt)
  {
    try
    {
      List localList = mService.getProfiles(paramInt, false);
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public String getSeedAccountName()
  {
    try
    {
      String str = mService.getSeedAccountName();
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public PersistableBundle getSeedAccountOptions()
  {
    try
    {
      PersistableBundle localPersistableBundle = mService.getSeedAccountOptions();
      return localPersistableBundle;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public String getSeedAccountType()
  {
    try
    {
      String str = mService.getSeedAccountType();
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public long getSerialNumberForUser(UserHandle paramUserHandle)
  {
    return getUserSerialNumber(paramUserHandle.getIdentifier());
  }
  
  @SystemApi
  public long[] getSerialNumbersOfUsers(boolean paramBoolean)
  {
    try
    {
      List localList = mService.getUsers(paramBoolean);
      long[] arrayOfLong = new long[localList.size()];
      for (int i = 0; i < arrayOfLong.length; i++) {
        arrayOfLong[i] = getserialNumber;
      }
      return arrayOfLong;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getTwinAppsId()
  {
    try
    {
      int i = mService.getTwinAppsId();
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      Log.w("UserManager", "Could not get TwinApps id", localRemoteException);
    }
    return -1;
  }
  
  public String getUserAccount(int paramInt)
  {
    try
    {
      String str = mService.getUserAccount(paramInt);
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getUserCount()
  {
    List localList = getUsers();
    int i;
    if (localList != null) {
      i = localList.size();
    } else {
      i = 1;
    }
    return i;
  }
  
  public long getUserCreationTime(UserHandle paramUserHandle)
  {
    try
    {
      long l = mService.getUserCreationTime(paramUserHandle.getIdentifier());
      return l;
    }
    catch (RemoteException paramUserHandle)
    {
      throw paramUserHandle.rethrowFromSystemServer();
    }
  }
  
  public UserHandle getUserForSerialNumber(long paramLong)
  {
    int i = getUserHandle((int)paramLong);
    UserHandle localUserHandle;
    if (i >= 0) {
      localUserHandle = new UserHandle(i);
    } else {
      localUserHandle = null;
    }
    return localUserHandle;
  }
  
  public int getUserHandle()
  {
    return UserHandle.myUserId();
  }
  
  public int getUserHandle(int paramInt)
  {
    try
    {
      paramInt = mService.getUserHandle(paramInt);
      return paramInt;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public Bitmap getUserIcon(int paramInt)
  {
    try
    {
      ParcelFileDescriptor localParcelFileDescriptor = mService.getUserIcon(paramInt);
      if (localParcelFileDescriptor != null) {
        try
        {
          Bitmap localBitmap = BitmapFactory.decodeFileDescriptor(localParcelFileDescriptor.getFileDescriptor());
          return localBitmap;
        }
        finally
        {
          try
          {
            localIOException1.close();
          }
          catch (IOException localIOException2) {}
        }
      }
      return null;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public UserInfo getUserInfo(int paramInt)
  {
    try
    {
      UserInfo localUserInfo = mService.getUserInfo(paramInt);
      return localUserInfo;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public String getUserName()
  {
    Object localObject = getUserInfo(getUserHandle());
    if (localObject == null) {
      localObject = "";
    } else {
      localObject = name;
    }
    return localObject;
  }
  
  public List<UserHandle> getUserProfiles()
  {
    int[] arrayOfInt = getProfileIds(UserHandle.myUserId(), true);
    ArrayList localArrayList = new ArrayList(arrayOfInt.length);
    int i = arrayOfInt.length;
    for (int j = 0; j < i; j++) {
      localArrayList.add(UserHandle.of(arrayOfInt[j]));
    }
    return localArrayList;
  }
  
  @SystemApi
  @Deprecated
  public int getUserRestrictionSource(String paramString, UserHandle paramUserHandle)
  {
    try
    {
      int i = mService.getUserRestrictionSource(paramString, paramUserHandle.getIdentifier());
      return i;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public List<EnforcingUser> getUserRestrictionSources(String paramString, UserHandle paramUserHandle)
  {
    try
    {
      paramString = mService.getUserRestrictionSources(paramString, paramUserHandle.getIdentifier());
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public Bundle getUserRestrictions()
  {
    return getUserRestrictions(Process.myUserHandle());
  }
  
  public Bundle getUserRestrictions(UserHandle paramUserHandle)
  {
    try
    {
      paramUserHandle = mService.getUserRestrictions(paramUserHandle.getIdentifier());
      return paramUserHandle;
    }
    catch (RemoteException paramUserHandle)
    {
      throw paramUserHandle.rethrowFromSystemServer();
    }
  }
  
  public int getUserSerialNumber(int paramInt)
  {
    try
    {
      paramInt = mService.getUserSerialNumber(paramInt);
      return paramInt;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public long getUserStartRealtime()
  {
    try
    {
      long l = mService.getUserStartRealtime();
      return l;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public long getUserUnlockRealtime()
  {
    try
    {
      long l = mService.getUserUnlockRealtime();
      return l;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<UserInfo> getUsers()
  {
    try
    {
      List localList = mService.getUsers(false);
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<UserInfo> getUsers(boolean paramBoolean)
  {
    try
    {
      List localList = mService.getUsers(paramBoolean);
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean hasBaseUserRestriction(String paramString, UserHandle paramUserHandle)
  {
    try
    {
      boolean bool = mService.hasBaseUserRestriction(paramString, paramUserHandle.getIdentifier());
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public boolean hasRestrictedProfiles()
  {
    try
    {
      boolean bool = mService.hasRestrictedProfiles();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean hasUserRestriction(String paramString)
  {
    return hasUserRestriction(paramString, Process.myUserHandle());
  }
  
  public boolean hasUserRestriction(String paramString, UserHandle paramUserHandle)
  {
    try
    {
      boolean bool = mService.hasUserRestriction(paramString, paramUserHandle.getIdentifier());
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean hasUserRestrictionOnAnyUser(String paramString)
  {
    try
    {
      boolean bool = mService.hasUserRestrictionOnAnyUser(paramString);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean isAdminUser()
  {
    return isUserAdmin(UserHandle.myUserId());
  }
  
  public boolean isDemoUser()
  {
    try
    {
      boolean bool = mService.isDemoUser(UserHandle.myUserId());
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isEphemeralUser()
  {
    return isUserEphemeral(UserHandle.myUserId());
  }
  
  public boolean isGuestUser()
  {
    UserInfo localUserInfo = getUserInfo(UserHandle.myUserId());
    boolean bool;
    if ((localUserInfo != null) && (localUserInfo.isGuest())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isGuestUser(int paramInt)
  {
    UserInfo localUserInfo = getUserInfo(paramInt);
    boolean bool;
    if ((localUserInfo != null) && (localUserInfo.isGuest())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @Deprecated
  public boolean isLinkedUser()
  {
    return isRestrictedProfile();
  }
  
  @SystemApi
  public boolean isManagedProfile()
  {
    if (mIsManagedProfileCached != null) {
      return mIsManagedProfileCached.booleanValue();
    }
    try
    {
      mIsManagedProfileCached = Boolean.valueOf(mService.isManagedProfile(UserHandle.myUserId()));
      boolean bool = mIsManagedProfileCached.booleanValue();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public boolean isManagedProfile(int paramInt)
  {
    if (paramInt == UserHandle.myUserId()) {
      return isManagedProfile();
    }
    try
    {
      boolean bool = mService.isManagedProfile(paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isPrimaryUser()
  {
    UserInfo localUserInfo = getUserInfo(UserHandle.myUserId());
    boolean bool;
    if ((localUserInfo != null) && (localUserInfo.isPrimary())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isQuietModeEnabled(UserHandle paramUserHandle)
  {
    try
    {
      boolean bool = mService.isQuietModeEnabled(paramUserHandle.getIdentifier());
      return bool;
    }
    catch (RemoteException paramUserHandle)
    {
      throw paramUserHandle.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public boolean isRestrictedProfile()
  {
    try
    {
      boolean bool = mService.isRestricted();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isSameProfileGroup(int paramInt1, int paramInt2)
  {
    try
    {
      boolean bool = mService.isSameProfileGroup(paramInt1, paramInt2);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isSystemUser()
  {
    boolean bool;
    if (UserHandle.myUserId() == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isTwinApps(int paramInt)
  {
    try
    {
      boolean bool = mService.isTwinApps(paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isUserAGoat()
  {
    return mContext.getPackageManager().isPackageAvailable("com.coffeestainstudios.goatsimulator");
  }
  
  public boolean isUserAdmin(int paramInt)
  {
    UserInfo localUserInfo = getUserInfo(paramInt);
    boolean bool;
    if ((localUserInfo != null) && (localUserInfo.isAdmin())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isUserEphemeral(int paramInt)
  {
    UserInfo localUserInfo = getUserInfo(paramInt);
    boolean bool;
    if ((localUserInfo != null) && (localUserInfo.isEphemeral())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isUserNameSet()
  {
    try
    {
      boolean bool = mService.isUserNameSet(getUserHandle());
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isUserRunning(int paramInt)
  {
    try
    {
      boolean bool = mService.isUserRunning(paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isUserRunning(UserHandle paramUserHandle)
  {
    return isUserRunning(paramUserHandle.getIdentifier());
  }
  
  public boolean isUserRunningOrStopping(UserHandle paramUserHandle)
  {
    try
    {
      boolean bool = ActivityManager.getService().isUserRunning(paramUserHandle.getIdentifier(), 1);
      return bool;
    }
    catch (RemoteException paramUserHandle)
    {
      throw paramUserHandle.rethrowFromSystemServer();
    }
  }
  
  public boolean isUserSwitcherEnabled()
  {
    if (!supportsMultipleUsers()) {
      return false;
    }
    if (hasUserRestriction("no_user_switch")) {
      return false;
    }
    if (isDeviceInDemoMode(mContext)) {
      return false;
    }
    boolean bool1 = true;
    Object localObject = getUsers(true);
    if (localObject == null) {
      return false;
    }
    int i = 0;
    localObject = ((List)localObject).iterator();
    while (((Iterator)localObject).hasNext())
    {
      int j = i;
      if (((UserInfo)((Iterator)localObject).next()).supportsSwitchToByUser()) {
        j = i + 1;
      }
      i = j;
    }
    boolean bool2 = ((DevicePolicyManager)mContext.getSystemService(DevicePolicyManager.class)).getGuestUserDisabled(null);
    boolean bool3 = bool1;
    if (i <= 1) {
      if ((bool2 ^ true)) {
        bool3 = bool1;
      } else {
        bool3 = false;
      }
    }
    return bool3;
  }
  
  public boolean isUserUnlocked()
  {
    return isUserUnlocked(Process.myUserHandle());
  }
  
  public boolean isUserUnlocked(int paramInt)
  {
    try
    {
      boolean bool = mService.isUserUnlocked(paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isUserUnlocked(UserHandle paramUserHandle)
  {
    return isUserUnlocked(paramUserHandle.getIdentifier());
  }
  
  public boolean isUserUnlockingOrUnlocked(int paramInt)
  {
    try
    {
      boolean bool = mService.isUserUnlockingOrUnlocked(paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isUserUnlockingOrUnlocked(UserHandle paramUserHandle)
  {
    return isUserUnlockingOrUnlocked(paramUserHandle.getIdentifier());
  }
  
  public boolean markGuestForDeletion(int paramInt)
  {
    try
    {
      boolean bool = mService.markGuestForDeletion(paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean removeUser(int paramInt)
  {
    try
    {
      boolean bool = mService.removeUser(paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean removeUserEvenWhenDisallowed(int paramInt)
  {
    try
    {
      boolean bool = mService.removeUserEvenWhenDisallowed(paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean requestQuietModeEnabled(boolean paramBoolean, UserHandle paramUserHandle)
  {
    return requestQuietModeEnabled(paramBoolean, paramUserHandle, null);
  }
  
  public boolean requestQuietModeEnabled(boolean paramBoolean, UserHandle paramUserHandle, IntentSender paramIntentSender)
  {
    try
    {
      paramBoolean = mService.requestQuietModeEnabled(mContext.getPackageName(), paramBoolean, paramUserHandle.getIdentifier(), paramIntentSender);
      return paramBoolean;
    }
    catch (RemoteException paramUserHandle)
    {
      throw paramUserHandle.rethrowFromSystemServer();
    }
  }
  
  public void setApplicationRestrictions(String paramString, Bundle paramBundle, UserHandle paramUserHandle)
  {
    try
    {
      mService.setApplicationRestrictions(paramString, paramBundle, paramUserHandle.getIdentifier());
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void setDefaultGuestRestrictions(Bundle paramBundle)
  {
    try
    {
      mService.setDefaultGuestRestrictions(paramBundle);
      return;
    }
    catch (RemoteException paramBundle)
    {
      throw paramBundle.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public boolean setRestrictionsChallenge(String paramString)
  {
    return false;
  }
  
  public void setSeedAccountData(int paramInt, String paramString1, String paramString2, PersistableBundle paramPersistableBundle)
  {
    try
    {
      mService.setSeedAccountData(paramInt, paramString1, paramString2, paramPersistableBundle, true);
      return;
    }
    catch (RemoteException paramString1)
    {
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  public void setUserAccount(int paramInt, String paramString)
  {
    try
    {
      mService.setUserAccount(paramInt, paramString);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void setUserAdmin(int paramInt)
  {
    try
    {
      mService.setUserAdmin(paramInt);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setUserEnabled(int paramInt)
  {
    try
    {
      mService.setUserEnabled(paramInt);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setUserIcon(int paramInt, Bitmap paramBitmap)
  {
    try
    {
      mService.setUserIcon(paramInt, paramBitmap);
      return;
    }
    catch (RemoteException paramBitmap)
    {
      throw paramBitmap.rethrowFromSystemServer();
    }
  }
  
  public void setUserName(int paramInt, String paramString)
  {
    try
    {
      mService.setUserName(paramInt, paramString);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void setUserRestriction(String paramString, boolean paramBoolean)
  {
    setUserRestriction(paramString, paramBoolean, Process.myUserHandle());
  }
  
  @Deprecated
  public void setUserRestriction(String paramString, boolean paramBoolean, UserHandle paramUserHandle)
  {
    try
    {
      mService.setUserRestriction(paramString, paramBoolean, paramUserHandle.getIdentifier());
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void setUserRestrictions(Bundle paramBundle)
  {
    throw new UnsupportedOperationException("This method is no longer supported");
  }
  
  @Deprecated
  public void setUserRestrictions(Bundle paramBundle, UserHandle paramUserHandle)
  {
    throw new UnsupportedOperationException("This method is no longer supported");
  }
  
  public boolean someUserHasSeedAccount(String paramString1, String paramString2)
  {
    try
    {
      boolean bool = mService.someUserHasSeedAccount(paramString1, paramString2);
      return bool;
    }
    catch (RemoteException paramString1)
    {
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public static final class EnforcingUser
    implements Parcelable
  {
    public static final Parcelable.Creator<EnforcingUser> CREATOR = new Parcelable.Creator()
    {
      public UserManager.EnforcingUser createFromParcel(Parcel paramAnonymousParcel)
      {
        return new UserManager.EnforcingUser(paramAnonymousParcel, null);
      }
      
      public UserManager.EnforcingUser[] newArray(int paramAnonymousInt)
      {
        return new UserManager.EnforcingUser[paramAnonymousInt];
      }
    };
    private final int userId;
    private final int userRestrictionSource;
    
    public EnforcingUser(int paramInt1, int paramInt2)
    {
      userId = paramInt1;
      userRestrictionSource = paramInt2;
    }
    
    private EnforcingUser(Parcel paramParcel)
    {
      userId = paramParcel.readInt();
      userRestrictionSource = paramParcel.readInt();
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public UserHandle getUserHandle()
    {
      return UserHandle.of(userId);
    }
    
    public int getUserRestrictionSource()
    {
      return userRestrictionSource;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(userId);
      paramParcel.writeInt(userRestrictionSource);
    }
  }
  
  public static class UserOperationException
    extends RuntimeException
  {
    private final int mUserOperationResult;
    
    public UserOperationException(String paramString, int paramInt)
    {
      super();
      mUserOperationResult = paramInt;
    }
    
    public int getUserOperationResult()
    {
      return mUserOperationResult;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface UserOperationResult {}
  
  @SystemApi
  @Retention(RetentionPolicy.SOURCE)
  public static @interface UserRestrictionSource {}
}
