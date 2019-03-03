package com.android.server.backup;

import android.app.IWallpaperManager;
import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.FullBackup;
import android.app.backup.FullBackupDataOutput;
import android.app.backup.WallpaperBackupHelper;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Slog;
import java.io.File;
import java.io.IOException;

public class SystemBackupAgent
  extends BackupAgentHelper
{
  private static final String ACCOUNT_MANAGER_HELPER = "account_manager";
  private static final String NOTIFICATION_HELPER = "notifications";
  private static final String PERMISSION_HELPER = "permissions";
  private static final String PREFERRED_HELPER = "preferred_activities";
  private static final String SHORTCUT_MANAGER_HELPER = "shortcut_manager";
  private static final String SLICES_HELPER = "slices";
  private static final String SYNC_SETTINGS_HELPER = "account_sync_settings";
  private static final String TAG = "SystemBackupAgent";
  private static final String USAGE_STATS_HELPER = "usage_stats";
  private static final String WALLPAPER_HELPER = "wallpaper";
  public static final String WALLPAPER_IMAGE;
  private static final String WALLPAPER_IMAGE_DIR = Environment.getUserSystemDirectory(0).getAbsolutePath();
  private static final String WALLPAPER_IMAGE_FILENAME = "wallpaper";
  private static final String WALLPAPER_IMAGE_KEY = "/data/data/com.android.settings/files/wallpaper";
  public static final String WALLPAPER_INFO = new File(Environment.getUserSystemDirectory(0), "wallpaper_info.xml").getAbsolutePath();
  private static final String WALLPAPER_INFO_DIR;
  private static final String WALLPAPER_INFO_FILENAME = "wallpaper_info.xml";
  private WallpaperBackupHelper mWallpaperHelper = null;
  
  static
  {
    WALLPAPER_IMAGE = new File(Environment.getUserSystemDirectory(0), "wallpaper").getAbsolutePath();
    WALLPAPER_INFO_DIR = Environment.getUserSystemDirectory(0).getAbsolutePath();
  }
  
  public SystemBackupAgent() {}
  
  public void onBackup(ParcelFileDescriptor paramParcelFileDescriptor1, BackupDataOutput paramBackupDataOutput, ParcelFileDescriptor paramParcelFileDescriptor2)
    throws IOException
  {
    addHelper("account_sync_settings", new AccountSyncSettingsBackupHelper(this));
    addHelper("preferred_activities", new PreferredActivityBackupHelper());
    addHelper("notifications", new NotificationBackupHelper(this));
    addHelper("permissions", new PermissionBackupHelper());
    addHelper("usage_stats", new UsageStatsBackupHelper(this));
    addHelper("shortcut_manager", new ShortcutBackupHelper());
    addHelper("account_manager", new AccountManagerBackupHelper());
    addHelper("slices", new SliceBackupHelper(this));
    super.onBackup(paramParcelFileDescriptor1, paramBackupDataOutput, paramParcelFileDescriptor2);
  }
  
  public void onFullBackup(FullBackupDataOutput paramFullBackupDataOutput)
    throws IOException
  {}
  
  public void onRestore(BackupDataInput paramBackupDataInput, int paramInt, ParcelFileDescriptor paramParcelFileDescriptor)
    throws IOException
  {
    mWallpaperHelper = new WallpaperBackupHelper(this, new String[] { "/data/data/com.android.settings/files/wallpaper" });
    addHelper("wallpaper", mWallpaperHelper);
    addHelper("system_files", new WallpaperBackupHelper(this, new String[] { "/data/data/com.android.settings/files/wallpaper" }));
    addHelper("account_sync_settings", new AccountSyncSettingsBackupHelper(this));
    addHelper("preferred_activities", new PreferredActivityBackupHelper());
    addHelper("notifications", new NotificationBackupHelper(this));
    addHelper("permissions", new PermissionBackupHelper());
    addHelper("usage_stats", new UsageStatsBackupHelper(this));
    addHelper("shortcut_manager", new ShortcutBackupHelper());
    addHelper("account_manager", new AccountManagerBackupHelper());
    addHelper("slices", new SliceBackupHelper(this));
    super.onRestore(paramBackupDataInput, paramInt, paramParcelFileDescriptor);
  }
  
  public void onRestoreFile(ParcelFileDescriptor paramParcelFileDescriptor, long paramLong1, int paramInt, String paramString1, String paramString2, long paramLong2, long paramLong3)
    throws IOException
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Restoring file domain=");
    ((StringBuilder)localObject).append(paramString1);
    ((StringBuilder)localObject).append(" path=");
    ((StringBuilder)localObject).append(paramString2);
    Slog.i("SystemBackupAgent", ((StringBuilder)localObject).toString());
    int i = 0;
    StringBuilder localStringBuilder = null;
    int j = i;
    localObject = localStringBuilder;
    if (paramString1.equals("r")) {
      if (paramString2.equals("wallpaper_info.xml"))
      {
        localObject = new File(WALLPAPER_INFO);
        j = 1;
      }
      else
      {
        j = i;
        localObject = localStringBuilder;
        if (paramString2.equals("wallpaper"))
        {
          localObject = new File(WALLPAPER_IMAGE);
          j = 1;
        }
      }
    }
    if (localObject == null) {
      try
      {
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Skipping unrecognized system file: [ ");
        localStringBuilder.append(paramString1);
        localStringBuilder.append(" : ");
        localStringBuilder.append(paramString2);
        localStringBuilder.append(" ]");
        Slog.w("SystemBackupAgent", localStringBuilder.toString());
      }
      catch (IOException paramParcelFileDescriptor)
      {
        break label290;
      }
    }
    FullBackup.restoreFile(paramParcelFileDescriptor, paramLong1, paramInt, paramLong2, paramLong3, (File)localObject);
    if (j != 0)
    {
      paramParcelFileDescriptor = (IWallpaperManager)ServiceManager.getService("wallpaper");
      if (paramParcelFileDescriptor != null) {
        try
        {
          paramParcelFileDescriptor.settingsRestored();
        }
        catch (RemoteException paramParcelFileDescriptor)
        {
          paramString1 = new java/lang/StringBuilder;
          paramString1.<init>();
          paramString1.append("Couldn't restore settings\n");
          paramString1.append(paramParcelFileDescriptor);
          Slog.e("SystemBackupAgent", paramString1.toString());
        }
      }
    }
    return;
    label290:
    if (j != 0)
    {
      new File(WALLPAPER_IMAGE).delete();
      new File(WALLPAPER_INFO).delete();
    }
  }
}
