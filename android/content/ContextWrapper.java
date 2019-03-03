package android.content;

import android.annotation.SystemApi;
import android.app.IApplicationThread;
import android.app.IServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.UserHandle;
import android.view.Display;
import android.view.DisplayAdjustments;
import android.view.autofill.AutofillManager.AutofillClient;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;

public class ContextWrapper
  extends Context
{
  Context mBase;
  
  public ContextWrapper(Context paramContext)
  {
    mBase = paramContext;
  }
  
  protected void attachBaseContext(Context paramContext)
  {
    if (mBase == null)
    {
      mBase = paramContext;
      return;
    }
    throw new IllegalStateException("Base context already set");
  }
  
  public boolean bindService(Intent paramIntent, ServiceConnection paramServiceConnection, int paramInt)
  {
    return mBase.bindService(paramIntent, paramServiceConnection, paramInt);
  }
  
  public boolean bindServiceAsUser(Intent paramIntent, ServiceConnection paramServiceConnection, int paramInt, Handler paramHandler, UserHandle paramUserHandle)
  {
    return mBase.bindServiceAsUser(paramIntent, paramServiceConnection, paramInt, paramHandler, paramUserHandle);
  }
  
  public boolean bindServiceAsUser(Intent paramIntent, ServiceConnection paramServiceConnection, int paramInt, UserHandle paramUserHandle)
  {
    return mBase.bindServiceAsUser(paramIntent, paramServiceConnection, paramInt, paramUserHandle);
  }
  
  public boolean canLoadUnsafeResources()
  {
    return mBase.canLoadUnsafeResources();
  }
  
  public boolean canStartActivityForResult()
  {
    return mBase.canStartActivityForResult();
  }
  
  public int checkCallingOrSelfPermission(String paramString)
  {
    return mBase.checkCallingOrSelfPermission(paramString);
  }
  
  public int checkCallingOrSelfUriPermission(Uri paramUri, int paramInt)
  {
    return mBase.checkCallingOrSelfUriPermission(paramUri, paramInt);
  }
  
  public int checkCallingPermission(String paramString)
  {
    return mBase.checkCallingPermission(paramString);
  }
  
  public int checkCallingUriPermission(Uri paramUri, int paramInt)
  {
    return mBase.checkCallingUriPermission(paramUri, paramInt);
  }
  
  public int checkPermission(String paramString, int paramInt1, int paramInt2)
  {
    return mBase.checkPermission(paramString, paramInt1, paramInt2);
  }
  
  public int checkPermission(String paramString, int paramInt1, int paramInt2, IBinder paramIBinder)
  {
    return mBase.checkPermission(paramString, paramInt1, paramInt2, paramIBinder);
  }
  
  public int checkSelfPermission(String paramString)
  {
    return mBase.checkSelfPermission(paramString);
  }
  
  public int checkUriPermission(Uri paramUri, int paramInt1, int paramInt2, int paramInt3)
  {
    return mBase.checkUriPermission(paramUri, paramInt1, paramInt2, paramInt3);
  }
  
  public int checkUriPermission(Uri paramUri, int paramInt1, int paramInt2, int paramInt3, IBinder paramIBinder)
  {
    return mBase.checkUriPermission(paramUri, paramInt1, paramInt2, paramInt3, paramIBinder);
  }
  
  public int checkUriPermission(Uri paramUri, String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3)
  {
    return mBase.checkUriPermission(paramUri, paramString1, paramString2, paramInt1, paramInt2, paramInt3);
  }
  
  @Deprecated
  public void clearWallpaper()
    throws IOException
  {
    mBase.clearWallpaper();
  }
  
  public Context createApplicationContext(ApplicationInfo paramApplicationInfo, int paramInt)
    throws PackageManager.NameNotFoundException
  {
    return mBase.createApplicationContext(paramApplicationInfo, paramInt);
  }
  
  public Context createConfigurationContext(Configuration paramConfiguration)
  {
    return mBase.createConfigurationContext(paramConfiguration);
  }
  
  public Context createContextForSplit(String paramString)
    throws PackageManager.NameNotFoundException
  {
    return mBase.createContextForSplit(paramString);
  }
  
  @SystemApi
  public Context createCredentialProtectedStorageContext()
  {
    return mBase.createCredentialProtectedStorageContext();
  }
  
  public Context createDeviceProtectedStorageContext()
  {
    return mBase.createDeviceProtectedStorageContext();
  }
  
  public Context createDisplayContext(Display paramDisplay)
  {
    return mBase.createDisplayContext(paramDisplay);
  }
  
  public Context createPackageContext(String paramString, int paramInt)
    throws PackageManager.NameNotFoundException
  {
    return mBase.createPackageContext(paramString, paramInt);
  }
  
  public Context createPackageContextAsUser(String paramString, int paramInt, UserHandle paramUserHandle)
    throws PackageManager.NameNotFoundException
  {
    return mBase.createPackageContextAsUser(paramString, paramInt, paramUserHandle);
  }
  
  public String[] databaseList()
  {
    return mBase.databaseList();
  }
  
  public boolean deleteDatabase(String paramString)
  {
    return mBase.deleteDatabase(paramString);
  }
  
  public boolean deleteFile(String paramString)
  {
    return mBase.deleteFile(paramString);
  }
  
  public boolean deleteSharedPreferences(String paramString)
  {
    return mBase.deleteSharedPreferences(paramString);
  }
  
  public void enforceCallingOrSelfPermission(String paramString1, String paramString2)
  {
    mBase.enforceCallingOrSelfPermission(paramString1, paramString2);
  }
  
  public void enforceCallingOrSelfUriPermission(Uri paramUri, int paramInt, String paramString)
  {
    mBase.enforceCallingOrSelfUriPermission(paramUri, paramInt, paramString);
  }
  
  public void enforceCallingPermission(String paramString1, String paramString2)
  {
    mBase.enforceCallingPermission(paramString1, paramString2);
  }
  
  public void enforceCallingUriPermission(Uri paramUri, int paramInt, String paramString)
  {
    mBase.enforceCallingUriPermission(paramUri, paramInt, paramString);
  }
  
  public void enforcePermission(String paramString1, int paramInt1, int paramInt2, String paramString2)
  {
    mBase.enforcePermission(paramString1, paramInt1, paramInt2, paramString2);
  }
  
  public void enforceUriPermission(Uri paramUri, int paramInt1, int paramInt2, int paramInt3, String paramString)
  {
    mBase.enforceUriPermission(paramUri, paramInt1, paramInt2, paramInt3, paramString);
  }
  
  public void enforceUriPermission(Uri paramUri, String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3, String paramString3)
  {
    mBase.enforceUriPermission(paramUri, paramString1, paramString2, paramInt1, paramInt2, paramInt3, paramString3);
  }
  
  public String[] fileList()
  {
    return mBase.fileList();
  }
  
  public IBinder getActivityToken()
  {
    return mBase.getActivityToken();
  }
  
  public Context getApplicationContext()
  {
    return mBase.getApplicationContext();
  }
  
  public ApplicationInfo getApplicationInfo()
  {
    return mBase.getApplicationInfo();
  }
  
  public AssetManager getAssets()
  {
    return mBase.getAssets();
  }
  
  public AutofillManager.AutofillClient getAutofillClient()
  {
    return mBase.getAutofillClient();
  }
  
  public Context getBaseContext()
  {
    return mBase;
  }
  
  public String getBasePackageName()
  {
    return mBase.getBasePackageName();
  }
  
  public File getCacheDir()
  {
    return mBase.getCacheDir();
  }
  
  public ClassLoader getClassLoader()
  {
    return mBase.getClassLoader();
  }
  
  public File getCodeCacheDir()
  {
    return mBase.getCodeCacheDir();
  }
  
  public ContentResolver getContentResolver()
  {
    return mBase.getContentResolver();
  }
  
  public File getDataDir()
  {
    return mBase.getDataDir();
  }
  
  public File getDatabasePath(String paramString)
  {
    return mBase.getDatabasePath(paramString);
  }
  
  public File getDir(String paramString, int paramInt)
  {
    return mBase.getDir(paramString, paramInt);
  }
  
  public Display getDisplay()
  {
    return mBase.getDisplay();
  }
  
  public DisplayAdjustments getDisplayAdjustments(int paramInt)
  {
    return mBase.getDisplayAdjustments(paramInt);
  }
  
  public File getExternalCacheDir()
  {
    return mBase.getExternalCacheDir();
  }
  
  public File[] getExternalCacheDirs()
  {
    return mBase.getExternalCacheDirs();
  }
  
  public File getExternalFilesDir(String paramString)
  {
    return mBase.getExternalFilesDir(paramString);
  }
  
  public File[] getExternalFilesDirs(String paramString)
  {
    return mBase.getExternalFilesDirs(paramString);
  }
  
  public File[] getExternalMediaDirs()
  {
    return mBase.getExternalMediaDirs();
  }
  
  public File getFileStreamPath(String paramString)
  {
    return mBase.getFileStreamPath(paramString);
  }
  
  public File getFilesDir()
  {
    return mBase.getFilesDir();
  }
  
  public IApplicationThread getIApplicationThread()
  {
    return mBase.getIApplicationThread();
  }
  
  public Executor getMainExecutor()
  {
    return mBase.getMainExecutor();
  }
  
  public Looper getMainLooper()
  {
    return mBase.getMainLooper();
  }
  
  public Handler getMainThreadHandler()
  {
    return mBase.getMainThreadHandler();
  }
  
  public int getNextAutofillId()
  {
    return mBase.getNextAutofillId();
  }
  
  public File getNoBackupFilesDir()
  {
    return mBase.getNoBackupFilesDir();
  }
  
  public File getObbDir()
  {
    return mBase.getObbDir();
  }
  
  public File[] getObbDirs()
  {
    return mBase.getObbDirs();
  }
  
  public String getOpPackageName()
  {
    return mBase.getOpPackageName();
  }
  
  public String getPackageCodePath()
  {
    return mBase.getPackageCodePath();
  }
  
  public PackageManager getPackageManager()
  {
    return mBase.getPackageManager();
  }
  
  public String getPackageName()
  {
    if (mBase != null) {
      return mBase.getPackageName();
    }
    return "";
  }
  
  public String getPackageResourcePath()
  {
    return mBase.getPackageResourcePath();
  }
  
  public File getPreloadsFileCache()
  {
    return mBase.getPreloadsFileCache();
  }
  
  public Resources getResources()
  {
    return mBase.getResources();
  }
  
  public IServiceConnection getServiceDispatcher(ServiceConnection paramServiceConnection, Handler paramHandler, int paramInt)
  {
    return mBase.getServiceDispatcher(paramServiceConnection, paramHandler, paramInt);
  }
  
  public SharedPreferences getSharedPreferences(File paramFile, int paramInt)
  {
    return mBase.getSharedPreferences(paramFile, paramInt);
  }
  
  public SharedPreferences getSharedPreferences(String paramString, int paramInt)
  {
    return mBase.getSharedPreferences(paramString, paramInt);
  }
  
  public File getSharedPreferencesPath(String paramString)
  {
    return mBase.getSharedPreferencesPath(paramString);
  }
  
  public Object getSystemService(String paramString)
  {
    return mBase.getSystemService(paramString);
  }
  
  public String getSystemServiceName(Class<?> paramClass)
  {
    return mBase.getSystemServiceName(paramClass);
  }
  
  public Resources.Theme getTheme()
  {
    return mBase.getTheme();
  }
  
  public int getThemeResId()
  {
    return mBase.getThemeResId();
  }
  
  public int getUserId()
  {
    return mBase.getUserId();
  }
  
  @Deprecated
  public Drawable getWallpaper()
  {
    return mBase.getWallpaper();
  }
  
  @Deprecated
  public int getWallpaperDesiredMinimumHeight()
  {
    return mBase.getWallpaperDesiredMinimumHeight();
  }
  
  @Deprecated
  public int getWallpaperDesiredMinimumWidth()
  {
    return mBase.getWallpaperDesiredMinimumWidth();
  }
  
  public void grantUriPermission(String paramString, Uri paramUri, int paramInt)
  {
    mBase.grantUriPermission(paramString, paramUri, paramInt);
  }
  
  public boolean isAutofillCompatibilityEnabled()
  {
    boolean bool;
    if ((mBase != null) && (mBase.isAutofillCompatibilityEnabled())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @SystemApi
  public boolean isCredentialProtectedStorage()
  {
    return mBase.isCredentialProtectedStorage();
  }
  
  public boolean isDeviceProtectedStorage()
  {
    return mBase.isDeviceProtectedStorage();
  }
  
  public boolean isRestricted()
  {
    return mBase.isRestricted();
  }
  
  public boolean moveDatabaseFrom(Context paramContext, String paramString)
  {
    return mBase.moveDatabaseFrom(paramContext, paramString);
  }
  
  public boolean moveSharedPreferencesFrom(Context paramContext, String paramString)
  {
    return mBase.moveSharedPreferencesFrom(paramContext, paramString);
  }
  
  public FileInputStream openFileInput(String paramString)
    throws FileNotFoundException
  {
    return mBase.openFileInput(paramString);
  }
  
  public FileOutputStream openFileOutput(String paramString, int paramInt)
    throws FileNotFoundException
  {
    return mBase.openFileOutput(paramString, paramInt);
  }
  
  public SQLiteDatabase openOrCreateDatabase(String paramString, int paramInt, SQLiteDatabase.CursorFactory paramCursorFactory)
  {
    return mBase.openOrCreateDatabase(paramString, paramInt, paramCursorFactory);
  }
  
  public SQLiteDatabase openOrCreateDatabase(String paramString, int paramInt, SQLiteDatabase.CursorFactory paramCursorFactory, DatabaseErrorHandler paramDatabaseErrorHandler)
  {
    return mBase.openOrCreateDatabase(paramString, paramInt, paramCursorFactory, paramDatabaseErrorHandler);
  }
  
  @Deprecated
  public Drawable peekWallpaper()
  {
    return mBase.peekWallpaper();
  }
  
  public Intent registerReceiver(BroadcastReceiver paramBroadcastReceiver, IntentFilter paramIntentFilter)
  {
    return mBase.registerReceiver(paramBroadcastReceiver, paramIntentFilter);
  }
  
  public Intent registerReceiver(BroadcastReceiver paramBroadcastReceiver, IntentFilter paramIntentFilter, int paramInt)
  {
    return mBase.registerReceiver(paramBroadcastReceiver, paramIntentFilter, paramInt);
  }
  
  public Intent registerReceiver(BroadcastReceiver paramBroadcastReceiver, IntentFilter paramIntentFilter, String paramString, Handler paramHandler)
  {
    return mBase.registerReceiver(paramBroadcastReceiver, paramIntentFilter, paramString, paramHandler);
  }
  
  public Intent registerReceiver(BroadcastReceiver paramBroadcastReceiver, IntentFilter paramIntentFilter, String paramString, Handler paramHandler, int paramInt)
  {
    return mBase.registerReceiver(paramBroadcastReceiver, paramIntentFilter, paramString, paramHandler, paramInt);
  }
  
  public Intent registerReceiverAsUser(BroadcastReceiver paramBroadcastReceiver, UserHandle paramUserHandle, IntentFilter paramIntentFilter, String paramString, Handler paramHandler)
  {
    return mBase.registerReceiverAsUser(paramBroadcastReceiver, paramUserHandle, paramIntentFilter, paramString, paramHandler);
  }
  
  public void reloadSharedPreferences()
  {
    mBase.reloadSharedPreferences();
  }
  
  @Deprecated
  public void removeStickyBroadcast(Intent paramIntent)
  {
    mBase.removeStickyBroadcast(paramIntent);
  }
  
  @Deprecated
  public void removeStickyBroadcastAsUser(Intent paramIntent, UserHandle paramUserHandle)
  {
    mBase.removeStickyBroadcastAsUser(paramIntent, paramUserHandle);
  }
  
  public void revokeUriPermission(Uri paramUri, int paramInt)
  {
    mBase.revokeUriPermission(paramUri, paramInt);
  }
  
  public void revokeUriPermission(String paramString, Uri paramUri, int paramInt)
  {
    mBase.revokeUriPermission(paramString, paramUri, paramInt);
  }
  
  public void sendBroadcast(Intent paramIntent)
  {
    mBase.sendBroadcast(paramIntent);
  }
  
  public void sendBroadcast(Intent paramIntent, String paramString)
  {
    mBase.sendBroadcast(paramIntent, paramString);
  }
  
  public void sendBroadcast(Intent paramIntent, String paramString, int paramInt)
  {
    mBase.sendBroadcast(paramIntent, paramString, paramInt);
  }
  
  @SystemApi
  public void sendBroadcast(Intent paramIntent, String paramString, Bundle paramBundle)
  {
    mBase.sendBroadcast(paramIntent, paramString, paramBundle);
  }
  
  public void sendBroadcastAsUser(Intent paramIntent, UserHandle paramUserHandle)
  {
    mBase.sendBroadcastAsUser(paramIntent, paramUserHandle);
  }
  
  public void sendBroadcastAsUser(Intent paramIntent, UserHandle paramUserHandle, String paramString)
  {
    mBase.sendBroadcastAsUser(paramIntent, paramUserHandle, paramString);
  }
  
  public void sendBroadcastAsUser(Intent paramIntent, UserHandle paramUserHandle, String paramString, int paramInt)
  {
    mBase.sendBroadcastAsUser(paramIntent, paramUserHandle, paramString, paramInt);
  }
  
  public void sendBroadcastAsUser(Intent paramIntent, UserHandle paramUserHandle, String paramString, Bundle paramBundle)
  {
    mBase.sendBroadcastAsUser(paramIntent, paramUserHandle, paramString, paramBundle);
  }
  
  public void sendBroadcastAsUserMultiplePermissions(Intent paramIntent, UserHandle paramUserHandle, String[] paramArrayOfString)
  {
    mBase.sendBroadcastAsUserMultiplePermissions(paramIntent, paramUserHandle, paramArrayOfString);
  }
  
  public void sendBroadcastMultiplePermissions(Intent paramIntent, String[] paramArrayOfString)
  {
    mBase.sendBroadcastMultiplePermissions(paramIntent, paramArrayOfString);
  }
  
  public void sendOrderedBroadcast(Intent paramIntent, String paramString)
  {
    mBase.sendOrderedBroadcast(paramIntent, paramString);
  }
  
  public void sendOrderedBroadcast(Intent paramIntent, String paramString1, int paramInt1, BroadcastReceiver paramBroadcastReceiver, Handler paramHandler, int paramInt2, String paramString2, Bundle paramBundle)
  {
    mBase.sendOrderedBroadcast(paramIntent, paramString1, paramInt1, paramBroadcastReceiver, paramHandler, paramInt2, paramString2, paramBundle);
  }
  
  public void sendOrderedBroadcast(Intent paramIntent, String paramString1, BroadcastReceiver paramBroadcastReceiver, Handler paramHandler, int paramInt, String paramString2, Bundle paramBundle)
  {
    mBase.sendOrderedBroadcast(paramIntent, paramString1, paramBroadcastReceiver, paramHandler, paramInt, paramString2, paramBundle);
  }
  
  @SystemApi
  public void sendOrderedBroadcast(Intent paramIntent, String paramString1, Bundle paramBundle1, BroadcastReceiver paramBroadcastReceiver, Handler paramHandler, int paramInt, String paramString2, Bundle paramBundle2)
  {
    mBase.sendOrderedBroadcast(paramIntent, paramString1, paramBundle1, paramBroadcastReceiver, paramHandler, paramInt, paramString2, paramBundle2);
  }
  
  public void sendOrderedBroadcastAsUser(Intent paramIntent, UserHandle paramUserHandle, String paramString1, int paramInt1, BroadcastReceiver paramBroadcastReceiver, Handler paramHandler, int paramInt2, String paramString2, Bundle paramBundle)
  {
    mBase.sendOrderedBroadcastAsUser(paramIntent, paramUserHandle, paramString1, paramInt1, paramBroadcastReceiver, paramHandler, paramInt2, paramString2, paramBundle);
  }
  
  public void sendOrderedBroadcastAsUser(Intent paramIntent, UserHandle paramUserHandle, String paramString1, int paramInt1, Bundle paramBundle1, BroadcastReceiver paramBroadcastReceiver, Handler paramHandler, int paramInt2, String paramString2, Bundle paramBundle2)
  {
    mBase.sendOrderedBroadcastAsUser(paramIntent, paramUserHandle, paramString1, paramInt1, paramBundle1, paramBroadcastReceiver, paramHandler, paramInt2, paramString2, paramBundle2);
  }
  
  public void sendOrderedBroadcastAsUser(Intent paramIntent, UserHandle paramUserHandle, String paramString1, BroadcastReceiver paramBroadcastReceiver, Handler paramHandler, int paramInt, String paramString2, Bundle paramBundle)
  {
    mBase.sendOrderedBroadcastAsUser(paramIntent, paramUserHandle, paramString1, paramBroadcastReceiver, paramHandler, paramInt, paramString2, paramBundle);
  }
  
  @Deprecated
  public void sendStickyBroadcast(Intent paramIntent)
  {
    mBase.sendStickyBroadcast(paramIntent);
  }
  
  @Deprecated
  public void sendStickyBroadcastAsUser(Intent paramIntent, UserHandle paramUserHandle)
  {
    mBase.sendStickyBroadcastAsUser(paramIntent, paramUserHandle);
  }
  
  @Deprecated
  public void sendStickyBroadcastAsUser(Intent paramIntent, UserHandle paramUserHandle, Bundle paramBundle)
  {
    mBase.sendStickyBroadcastAsUser(paramIntent, paramUserHandle, paramBundle);
  }
  
  @Deprecated
  public void sendStickyOrderedBroadcast(Intent paramIntent, BroadcastReceiver paramBroadcastReceiver, Handler paramHandler, int paramInt, String paramString, Bundle paramBundle)
  {
    mBase.sendStickyOrderedBroadcast(paramIntent, paramBroadcastReceiver, paramHandler, paramInt, paramString, paramBundle);
  }
  
  @Deprecated
  public void sendStickyOrderedBroadcastAsUser(Intent paramIntent, UserHandle paramUserHandle, BroadcastReceiver paramBroadcastReceiver, Handler paramHandler, int paramInt, String paramString, Bundle paramBundle)
  {
    mBase.sendStickyOrderedBroadcastAsUser(paramIntent, paramUserHandle, paramBroadcastReceiver, paramHandler, paramInt, paramString, paramBundle);
  }
  
  public void setAutofillClient(AutofillManager.AutofillClient paramAutofillClient)
  {
    mBase.setAutofillClient(paramAutofillClient);
  }
  
  public void setAutofillCompatibilityEnabled(boolean paramBoolean)
  {
    if (mBase != null) {
      mBase.setAutofillCompatibilityEnabled(paramBoolean);
    }
  }
  
  public void setTheme(int paramInt)
  {
    mBase.setTheme(paramInt);
  }
  
  @Deprecated
  public void setWallpaper(Bitmap paramBitmap)
    throws IOException
  {
    mBase.setWallpaper(paramBitmap);
  }
  
  @Deprecated
  public void setWallpaper(InputStream paramInputStream)
    throws IOException
  {
    mBase.setWallpaper(paramInputStream);
  }
  
  public void startActivities(Intent[] paramArrayOfIntent)
  {
    mBase.startActivities(paramArrayOfIntent);
  }
  
  public void startActivities(Intent[] paramArrayOfIntent, Bundle paramBundle)
  {
    mBase.startActivities(paramArrayOfIntent, paramBundle);
  }
  
  public int startActivitiesAsUser(Intent[] paramArrayOfIntent, Bundle paramBundle, UserHandle paramUserHandle)
  {
    return mBase.startActivitiesAsUser(paramArrayOfIntent, paramBundle, paramUserHandle);
  }
  
  public void startActivity(Intent paramIntent)
  {
    mBase.startActivity(paramIntent);
  }
  
  public void startActivity(Intent paramIntent, Bundle paramBundle)
  {
    mBase.startActivity(paramIntent, paramBundle);
  }
  
  public void startActivityAsUser(Intent paramIntent, Bundle paramBundle, UserHandle paramUserHandle)
  {
    mBase.startActivityAsUser(paramIntent, paramBundle, paramUserHandle);
  }
  
  public void startActivityAsUser(Intent paramIntent, UserHandle paramUserHandle)
  {
    mBase.startActivityAsUser(paramIntent, paramUserHandle);
  }
  
  public void startActivityForResult(String paramString, Intent paramIntent, int paramInt, Bundle paramBundle)
  {
    mBase.startActivityForResult(paramString, paramIntent, paramInt, paramBundle);
  }
  
  public ComponentName startForegroundService(Intent paramIntent)
  {
    return mBase.startForegroundService(paramIntent);
  }
  
  public ComponentName startForegroundServiceAsUser(Intent paramIntent, UserHandle paramUserHandle)
  {
    return mBase.startForegroundServiceAsUser(paramIntent, paramUserHandle);
  }
  
  public boolean startInstrumentation(ComponentName paramComponentName, String paramString, Bundle paramBundle)
  {
    return mBase.startInstrumentation(paramComponentName, paramString, paramBundle);
  }
  
  public void startIntentSender(IntentSender paramIntentSender, Intent paramIntent, int paramInt1, int paramInt2, int paramInt3)
    throws IntentSender.SendIntentException
  {
    mBase.startIntentSender(paramIntentSender, paramIntent, paramInt1, paramInt2, paramInt3);
  }
  
  public void startIntentSender(IntentSender paramIntentSender, Intent paramIntent, int paramInt1, int paramInt2, int paramInt3, Bundle paramBundle)
    throws IntentSender.SendIntentException
  {
    mBase.startIntentSender(paramIntentSender, paramIntent, paramInt1, paramInt2, paramInt3, paramBundle);
  }
  
  public ComponentName startService(Intent paramIntent)
  {
    return mBase.startService(paramIntent);
  }
  
  public ComponentName startServiceAsUser(Intent paramIntent, UserHandle paramUserHandle)
  {
    return mBase.startServiceAsUser(paramIntent, paramUserHandle);
  }
  
  public boolean stopService(Intent paramIntent)
  {
    return mBase.stopService(paramIntent);
  }
  
  public boolean stopServiceAsUser(Intent paramIntent, UserHandle paramUserHandle)
  {
    return mBase.stopServiceAsUser(paramIntent, paramUserHandle);
  }
  
  public void unbindService(ServiceConnection paramServiceConnection)
  {
    mBase.unbindService(paramServiceConnection);
  }
  
  public void unregisterReceiver(BroadcastReceiver paramBroadcastReceiver)
  {
    mBase.unregisterReceiver(paramBroadcastReceiver);
  }
  
  public void updateDisplay(int paramInt)
  {
    mBase.updateDisplay(paramInt);
  }
}
