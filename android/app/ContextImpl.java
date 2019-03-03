package android.app;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.IContentProvider;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.CompatResources;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.storage.StorageManager;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructStat;
import android.util.AndroidRuntimeException;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Slog;
import android.view.Display;
import android.view.DisplayAdjustments;
import android.view.autofill.AutofillManager.AutofillClient;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
import libcore.io.Memory;

class ContextImpl
  extends Context
{
  private static final boolean DEBUG = false;
  static final int STATE_INITIALIZING = 1;
  static final int STATE_NOT_FOUND = 3;
  static final int STATE_READY = 2;
  static final int STATE_UNINITIALIZED = 0;
  private static final String TAG = "ContextImpl";
  private static final String XATTR_INODE_CACHE = "user.inode_cache";
  private static final String XATTR_INODE_CODE_CACHE = "user.inode_code_cache";
  @GuardedBy("ContextImpl.class")
  private static ArrayMap<String, ArrayMap<File, SharedPreferencesImpl>> sSharedPrefsCache;
  private final IBinder mActivityToken;
  private AutofillManager.AutofillClient mAutofillClient = null;
  private final String mBasePackageName;
  @GuardedBy("mSync")
  private File mCacheDir;
  private ClassLoader mClassLoader;
  @GuardedBy("mSync")
  private File mCodeCacheDir;
  private final ApplicationContentResolver mContentResolver;
  @GuardedBy("mSync")
  private File mDatabasesDir;
  private Display mDisplay;
  @GuardedBy("mSync")
  private File mFilesDir;
  private final int mFlags;
  private boolean mIsAutofillCompatEnabled;
  final ActivityThread mMainThread;
  @GuardedBy("mSync")
  private File mNoBackupFilesDir;
  private final String mOpPackageName;
  private Context mOuterContext = this;
  final LoadedApk mPackageInfo;
  private PackageManager mPackageManager;
  @GuardedBy("mSync")
  private File mPreferencesDir;
  private Context mReceiverRestrictedContext = null;
  private Resources mResources;
  private final ResourcesManager mResourcesManager;
  final Object[] mServiceCache = SystemServiceRegistry.createServiceCache();
  final int[] mServiceInitializationStateArray = new int[mServiceCache.length];
  @GuardedBy("ContextImpl.class")
  private ArrayMap<String, File> mSharedPrefsPaths;
  private String mSplitName = null;
  private final Object mSync = new Object();
  private Resources.Theme mTheme = null;
  private int mThemeResource = 0;
  private final UserHandle mUser;
  
  private ContextImpl(ContextImpl paramContextImpl, ActivityThread paramActivityThread, LoadedApk paramLoadedApk, String paramString, IBinder paramIBinder, UserHandle paramUserHandle, int paramInt, ClassLoader paramClassLoader)
  {
    int i = paramInt;
    if ((paramInt & 0x18) == 0)
    {
      File localFile = paramLoadedApk.getDataDirFile();
      if (Objects.equals(localFile, paramLoadedApk.getCredentialProtectedDataDirFile()))
      {
        i = paramInt | 0x10;
      }
      else
      {
        i = paramInt;
        if (Objects.equals(localFile, paramLoadedApk.getDeviceProtectedDataDirFile())) {
          i = paramInt | 0x8;
        }
      }
    }
    mMainThread = paramActivityThread;
    mActivityToken = paramIBinder;
    mFlags = i;
    paramIBinder = paramUserHandle;
    if (paramUserHandle == null) {
      paramIBinder = Process.myUserHandle();
    }
    mUser = paramIBinder;
    mPackageInfo = paramLoadedApk;
    mSplitName = paramString;
    mClassLoader = paramClassLoader;
    mResourcesManager = ResourcesManager.getInstance();
    if (paramContextImpl != null)
    {
      mBasePackageName = mBasePackageName;
      mOpPackageName = mOpPackageName;
      setResources(mResources);
      mDisplay = mDisplay;
    }
    else
    {
      mBasePackageName = mPackageName;
      paramContextImpl = paramLoadedApk.getApplicationInfo();
      if ((uid == 1000) && (uid != Process.myUid())) {
        mOpPackageName = ActivityThread.currentPackageName();
      } else {
        mOpPackageName = mBasePackageName;
      }
    }
    mContentResolver = new ApplicationContentResolver(this, paramActivityThread);
  }
  
  private boolean bindServiceCommon(Intent paramIntent, ServiceConnection paramServiceConnection, int paramInt, Handler paramHandler, UserHandle paramUserHandle)
  {
    int i = paramInt;
    if (paramServiceConnection != null)
    {
      if (mPackageInfo != null)
      {
        paramServiceConnection = mPackageInfo.getServiceDispatcher(paramServiceConnection, getOuterContext(), paramHandler, i);
        validateServiceIntent(paramIntent);
        paramInt = i;
        try
        {
          if (getActivityToken() == null)
          {
            paramInt = i;
            if ((i & 0x1) == 0)
            {
              paramInt = i;
              if (mPackageInfo != null)
              {
                int j = mPackageInfo.getApplicationInfo().targetSdkVersion;
                paramInt = i;
                if (j < 14) {
                  paramInt = i | 0x20;
                }
              }
            }
          }
          try
          {
            paramIntent.prepareToLeaveProcess(this);
            paramInt = ActivityManager.getService().bindService(mMainThread.getApplicationThread(), getActivityToken(), paramIntent, paramIntent.resolveTypeIfNeeded(getContentResolver()), paramServiceConnection, paramInt, getOpPackageName(), paramUserHandle.getIdentifier());
            if (paramInt >= 0)
            {
              boolean bool;
              if (paramInt != 0) {
                bool = true;
              } else {
                bool = false;
              }
              return bool;
            }
            paramHandler = new java/lang/SecurityException;
            paramServiceConnection = new java/lang/StringBuilder;
            paramServiceConnection.<init>();
            paramServiceConnection.append("Not allowed to bind to service ");
            paramServiceConnection.append(paramIntent);
            paramHandler.<init>(paramServiceConnection.toString());
            throw paramHandler;
          }
          catch (RemoteException paramIntent) {}
          throw paramIntent.rethrowFromSystemServer();
        }
        catch (RemoteException paramIntent) {}
      }
      throw new RuntimeException("Not supported in system context");
    }
    throw new IllegalArgumentException("connection is null");
  }
  
  private void checkMode(int paramInt)
  {
    if (getApplicationInfotargetSdkVersion >= 24) {
      if ((paramInt & 0x1) == 0)
      {
        if ((paramInt & 0x2) != 0) {
          throw new SecurityException("MODE_WORLD_WRITEABLE no longer supported");
        }
      }
      else {
        throw new SecurityException("MODE_WORLD_READABLE no longer supported");
      }
    }
  }
  
  /* Error */
  static ContextImpl createActivityContext(ActivityThread paramActivityThread, LoadedApk paramLoadedApk, android.content.pm.ActivityInfo paramActivityInfo, IBinder paramIBinder, int paramInt, Configuration paramConfiguration)
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnull +195 -> 196
    //   4: aload_1
    //   5: invokevirtual 313	android/app/LoadedApk:getSplitResDirs	()[Ljava/lang/String;
    //   8: astore 6
    //   10: aload_1
    //   11: invokevirtual 317	android/app/LoadedApk:getClassLoader	()Ljava/lang/ClassLoader;
    //   14: astore 7
    //   16: aload_1
    //   17: invokevirtual 182	android/app/LoadedApk:getApplicationInfo	()Landroid/content/pm/ApplicationInfo;
    //   20: invokevirtual 321	android/content/pm/ApplicationInfo:requestsIsolatedSplitLoading	()Z
    //   23: ifeq +65 -> 88
    //   26: ldc2_w 322
    //   29: ldc_w 325
    //   32: invokestatic 331	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   35: aload_1
    //   36: aload_2
    //   37: getfield 336	android/content/pm/ActivityInfo:splitName	Ljava/lang/String;
    //   40: invokevirtual 340	android/app/LoadedApk:getSplitClassLoader	(Ljava/lang/String;)Ljava/lang/ClassLoader;
    //   43: astore 7
    //   45: aload_1
    //   46: aload_2
    //   47: getfield 336	android/content/pm/ActivityInfo:splitName	Ljava/lang/String;
    //   50: invokevirtual 344	android/app/LoadedApk:getSplitPaths	(Ljava/lang/String;)[Ljava/lang/String;
    //   53: astore 6
    //   55: ldc2_w 322
    //   58: invokestatic 348	android/os/Trace:traceEnd	(J)V
    //   61: goto +27 -> 88
    //   64: astore_0
    //   65: goto +15 -> 80
    //   68: astore_0
    //   69: new 290	java/lang/RuntimeException
    //   72: astore_1
    //   73: aload_1
    //   74: aload_0
    //   75: invokespecial 351	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
    //   78: aload_1
    //   79: athrow
    //   80: ldc2_w 322
    //   83: invokestatic 348	android/os/Trace:traceEnd	(J)V
    //   86: aload_0
    //   87: athrow
    //   88: new 2	android/app/ContextImpl
    //   91: dup
    //   92: aconst_null
    //   93: aload_0
    //   94: aload_1
    //   95: aload_2
    //   96: getfield 336	android/content/pm/ActivityInfo:splitName	Ljava/lang/String;
    //   99: aload_3
    //   100: aconst_null
    //   101: iconst_0
    //   102: aload 7
    //   104: invokespecial 353	android/app/ContextImpl:<init>	(Landroid/app/ContextImpl;Landroid/app/ActivityThread;Landroid/app/LoadedApk;Ljava/lang/String;Landroid/os/IBinder;Landroid/os/UserHandle;ILjava/lang/ClassLoader;)V
    //   107: astore_2
    //   108: iload 4
    //   110: iconst_m1
    //   111: if_icmpeq +6 -> 117
    //   114: goto +6 -> 120
    //   117: iconst_0
    //   118: istore 4
    //   120: iload 4
    //   122: ifne +11 -> 133
    //   125: aload_1
    //   126: invokevirtual 357	android/app/LoadedApk:getCompatibilityInfo	()Landroid/content/res/CompatibilityInfo;
    //   129: astore_0
    //   130: goto +10 -> 140
    //   133: getstatic 363	android/content/res/CompatibilityInfo:DEFAULT_COMPATIBILITY_INFO	Landroid/content/res/CompatibilityInfo;
    //   136: astore_0
    //   137: goto -7 -> 130
    //   140: invokestatic 161	android/app/ResourcesManager:getInstance	()Landroid/app/ResourcesManager;
    //   143: astore 8
    //   145: aload_2
    //   146: aload 8
    //   148: aload_3
    //   149: aload_1
    //   150: invokevirtual 366	android/app/LoadedApk:getResDir	()Ljava/lang/String;
    //   153: aload 6
    //   155: aload_1
    //   156: invokevirtual 369	android/app/LoadedApk:getOverlayDirs	()[Ljava/lang/String;
    //   159: aload_1
    //   160: invokevirtual 182	android/app/LoadedApk:getApplicationInfo	()Landroid/content/pm/ApplicationInfo;
    //   163: getfield 373	android/content/pm/ApplicationInfo:sharedLibraryFiles	[Ljava/lang/String;
    //   166: iload 4
    //   168: aload 5
    //   170: aload_0
    //   171: aload 7
    //   173: invokevirtual 377	android/app/ResourcesManager:createBaseActivityResources	(Landroid/os/IBinder;Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;ILandroid/content/res/Configuration;Landroid/content/res/CompatibilityInfo;Ljava/lang/ClassLoader;)Landroid/content/res/Resources;
    //   176: invokevirtual 173	android/app/ContextImpl:setResources	(Landroid/content/res/Resources;)V
    //   179: aload_2
    //   180: aload 8
    //   182: iload 4
    //   184: aload_2
    //   185: invokevirtual 381	android/app/ContextImpl:getResources	()Landroid/content/res/Resources;
    //   188: invokevirtual 385	android/app/ResourcesManager:getAdjustedDisplay	(ILandroid/content/res/Resources;)Landroid/view/Display;
    //   191: putfield 175	android/app/ContextImpl:mDisplay	Landroid/view/Display;
    //   194: aload_2
    //   195: areturn
    //   196: new 295	java/lang/IllegalArgumentException
    //   199: dup
    //   200: ldc_w 387
    //   203: invokespecial 298	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   206: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	207	0	paramActivityThread	ActivityThread
    //   0	207	1	paramLoadedApk	LoadedApk
    //   0	207	2	paramActivityInfo	android.content.pm.ActivityInfo
    //   0	207	3	paramIBinder	IBinder
    //   0	207	4	paramInt	int
    //   0	207	5	paramConfiguration	Configuration
    //   8	146	6	arrayOfString	String[]
    //   14	158	7	localClassLoader	ClassLoader
    //   143	38	8	localResourcesManager	ResourcesManager
    // Exception table:
    //   from	to	target	type
    //   35	55	64	finally
    //   69	80	64	finally
    //   35	55	68	android/content/pm/PackageManager$NameNotFoundException
  }
  
  static ContextImpl createAppContext(ActivityThread paramActivityThread, LoadedApk paramLoadedApk)
  {
    if (paramLoadedApk != null)
    {
      paramActivityThread = new ContextImpl(null, paramActivityThread, paramLoadedApk, null, null, null, 0, null);
      paramActivityThread.setResources(paramLoadedApk.getResources());
      return paramActivityThread;
    }
    throw new IllegalArgumentException("packageInfo");
  }
  
  private static Resources createResources(IBinder paramIBinder, LoadedApk paramLoadedApk, String paramString, int paramInt, Configuration paramConfiguration, CompatibilityInfo paramCompatibilityInfo)
  {
    try
    {
      String[] arrayOfString = paramLoadedApk.getSplitPaths(paramString);
      paramString = paramLoadedApk.getSplitClassLoader(paramString);
      return ResourcesManager.getInstance().getResources(paramIBinder, paramLoadedApk.getResDir(), arrayOfString, paramLoadedApk.getOverlayDirs(), getApplicationInfosharedLibraryFiles, paramInt, paramConfiguration, paramCompatibilityInfo, paramString);
    }
    catch (PackageManager.NameNotFoundException paramIBinder)
    {
      throw new RuntimeException(paramIBinder);
    }
  }
  
  static ContextImpl createSystemContext(ActivityThread paramActivityThread)
  {
    LoadedApk localLoadedApk = new LoadedApk(paramActivityThread);
    paramActivityThread = new ContextImpl(null, paramActivityThread, localLoadedApk, null, null, null, 0, null);
    paramActivityThread.setResources(localLoadedApk.getResources());
    mResources.updateConfiguration(mResourcesManager.getConfiguration(), mResourcesManager.getDisplayMetrics());
    return paramActivityThread;
  }
  
  static ContextImpl createSystemUiContext(ContextImpl paramContextImpl)
  {
    LoadedApk localLoadedApk = mPackageInfo;
    paramContextImpl = new ContextImpl(null, mMainThread, localLoadedApk, null, null, null, 0, null);
    paramContextImpl.setResources(createResources(null, localLoadedApk, null, 0, null, localLoadedApk.getCompatibilityInfo()));
    return paramContextImpl;
  }
  
  private void enforce(String paramString1, int paramInt1, boolean paramBoolean, int paramInt2, String paramString2)
  {
    if (paramInt1 != 0)
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      if (paramString2 != null)
      {
        StringBuilder localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(paramString2);
        localStringBuilder2.append(": ");
        paramString2 = localStringBuilder2.toString();
      }
      else
      {
        paramString2 = "";
      }
      localStringBuilder1.append(paramString2);
      if (paramBoolean)
      {
        paramString2 = new StringBuilder();
        paramString2.append("Neither user ");
        paramString2.append(paramInt2);
        paramString2.append(" nor current process has ");
        paramString2 = paramString2.toString();
      }
      else
      {
        paramString2 = new StringBuilder();
        paramString2.append("uid ");
        paramString2.append(paramInt2);
        paramString2.append(" does not have ");
        paramString2 = paramString2.toString();
      }
      localStringBuilder1.append(paramString2);
      localStringBuilder1.append(paramString1);
      localStringBuilder1.append(".");
      throw new SecurityException(localStringBuilder1.toString());
    }
  }
  
  private void enforceForUri(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3, Uri paramUri, String paramString)
  {
    if (paramInt2 != 0)
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      if (paramString != null)
      {
        StringBuilder localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append(paramString);
        localStringBuilder2.append(": ");
        paramString = localStringBuilder2.toString();
      }
      else
      {
        paramString = "";
      }
      localStringBuilder1.append(paramString);
      if (paramBoolean)
      {
        paramString = new StringBuilder();
        paramString.append("Neither user ");
        paramString.append(paramInt3);
        paramString.append(" nor current process has ");
        paramString = paramString.toString();
      }
      else
      {
        paramString = new StringBuilder();
        paramString.append("User ");
        paramString.append(paramInt3);
        paramString.append(" does not have ");
        paramString = paramString.toString();
      }
      localStringBuilder1.append(paramString);
      localStringBuilder1.append(uriModeFlagToString(paramInt1));
      localStringBuilder1.append(" permission on ");
      localStringBuilder1.append(paramUri);
      localStringBuilder1.append(".");
      throw new SecurityException(localStringBuilder1.toString());
    }
  }
  
  private File[] ensureExternalDirsExistOrFilter(File[] paramArrayOfFile)
  {
    StorageManager localStorageManager = (StorageManager)getSystemService(StorageManager.class);
    File[] arrayOfFile = new File[paramArrayOfFile.length];
    for (int i = 0; i < paramArrayOfFile.length; i++)
    {
      File localFile = paramArrayOfFile[i];
      Object localObject = localFile;
      if (!localFile.exists())
      {
        localObject = localFile;
        if (!localFile.mkdirs())
        {
          localObject = localFile;
          if (!localFile.exists()) {
            try
            {
              localStorageManager.mkdirs(localFile);
              localObject = localFile;
            }
            catch (Exception localException)
            {
              localObject = new StringBuilder();
              ((StringBuilder)localObject).append("Failed to ensure ");
              ((StringBuilder)localObject).append(localFile);
              ((StringBuilder)localObject).append(": ");
              ((StringBuilder)localObject).append(localException);
              Log.w("ContextImpl", ((StringBuilder)localObject).toString());
              localObject = null;
            }
          }
        }
      }
      arrayOfFile[i] = localObject;
    }
    return arrayOfFile;
  }
  
  private static File ensurePrivateCacheDirExists(File paramFile, String paramString)
  {
    return ensurePrivateDirExists(paramFile, 1529, UserHandle.getCacheAppGid(Process.myUid()), paramString);
  }
  
  private static File ensurePrivateDirExists(File paramFile)
  {
    return ensurePrivateDirExists(paramFile, 505, -1, null);
  }
  
  private static File ensurePrivateDirExists(File paramFile, int paramInt1, int paramInt2, String paramString)
  {
    if (!paramFile.exists())
    {
      String str = paramFile.getAbsolutePath();
      Object localObject;
      try
      {
        Os.mkdir(str, paramInt1);
        Os.chmod(str, paramInt1);
        if (paramInt2 != -1) {
          Os.chown(str, -1, paramInt2);
        }
      }
      catch (ErrnoException localErrnoException1)
      {
        if (errno != OsConstants.EEXIST)
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Failed to ensure ");
          ((StringBuilder)localObject).append(paramFile);
          ((StringBuilder)localObject).append(": ");
          ((StringBuilder)localObject).append(localErrnoException1.getMessage());
          Log.w("ContextImpl", ((StringBuilder)localObject).toString());
        }
      }
      if (paramString != null) {
        try
        {
          localObject = Os.stat(paramFile.getAbsolutePath());
          byte[] arrayOfByte = new byte[8];
          Memory.pokeLong(arrayOfByte, 0, st_ino, ByteOrder.nativeOrder());
          Os.setxattr(paramFile.getParentFile().getAbsolutePath(), paramString, arrayOfByte, 0);
        }
        catch (ErrnoException localErrnoException2)
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Failed to update ");
          ((StringBuilder)localObject).append(paramString);
          ((StringBuilder)localObject).append(": ");
          ((StringBuilder)localObject).append(localErrnoException2.getMessage());
          Log.w("ContextImpl", ((StringBuilder)localObject).toString());
        }
      }
    }
    return paramFile;
  }
  
  private File getDatabasesDir()
  {
    synchronized (mSync)
    {
      if (mDatabasesDir == null) {
        if ("android".equals(getPackageName()))
        {
          localFile = new java/io/File;
          localFile.<init>("/data/system");
          mDatabasesDir = localFile;
        }
        else
        {
          localFile = new java/io/File;
          localFile.<init>(getDataDir(), "databases");
          mDatabasesDir = localFile;
        }
      }
      File localFile = ensurePrivateDirExists(mDatabasesDir);
      return localFile;
    }
  }
  
  static ContextImpl getImpl(Context paramContext)
  {
    while ((paramContext instanceof ContextWrapper))
    {
      Context localContext = ((ContextWrapper)paramContext).getBaseContext();
      if (localContext == null) {
        break;
      }
      paramContext = localContext;
    }
    return (ContextImpl)paramContext;
  }
  
  private File getPreferencesDir()
  {
    synchronized (mSync)
    {
      if (mPreferencesDir == null)
      {
        localFile = new java/io/File;
        localFile.<init>(getDataDir(), "shared_prefs");
        mPreferencesDir = localFile;
      }
      File localFile = ensurePrivateDirExists(mPreferencesDir);
      return localFile;
    }
  }
  
  @GuardedBy("ContextImpl.class")
  private ArrayMap<File, SharedPreferencesImpl> getSharedPreferencesCacheLocked()
  {
    if (sSharedPrefsCache == null) {
      sSharedPrefsCache = new ArrayMap();
    }
    String str = getPackageName();
    ArrayMap localArrayMap1 = (ArrayMap)sSharedPrefsCache.get(str);
    ArrayMap localArrayMap2 = localArrayMap1;
    if (localArrayMap1 == null)
    {
      localArrayMap2 = new ArrayMap();
      sSharedPrefsCache.put(str, localArrayMap2);
    }
    return localArrayMap2;
  }
  
  private WallpaperManager getWallpaperManager()
  {
    return (WallpaperManager)getSystemService(WallpaperManager.class);
  }
  
  private void initializeTheme()
  {
    if (mTheme == null) {
      mTheme = mResources.newTheme();
    }
    mTheme.applyStyle(mThemeResource, true);
  }
  
  private File makeFilename(File paramFile, String paramString)
  {
    if (paramString.indexOf(File.separatorChar) < 0) {
      return new File(paramFile, paramString);
    }
    paramFile = new StringBuilder();
    paramFile.append("File ");
    paramFile.append(paramString);
    paramFile.append(" contains a path separator");
    throw new IllegalArgumentException(paramFile.toString());
  }
  
  private static int moveFiles(File paramFile1, File paramFile2, String paramString)
  {
    paramString = FileUtils.listFilesOrEmpty(paramFile1, new FilenameFilter()
    {
      public boolean accept(File paramAnonymousFile, String paramAnonymousString)
      {
        return paramAnonymousString.startsWith(ContextImpl.this);
      }
    });
    int i = 0;
    int j = paramString.length;
    int k = 0;
    while (k < j)
    {
      paramFile1 = paramString[k];
      Object localObject = new File(paramFile2, paramFile1.getName());
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Migrating ");
      localStringBuilder.append(paramFile1);
      localStringBuilder.append(" to ");
      localStringBuilder.append(localObject);
      Log.d("ContextImpl", localStringBuilder.toString());
      try
      {
        FileUtils.copyFileOrThrow(paramFile1, (File)localObject);
        FileUtils.copyPermissions(paramFile1, (File)localObject);
        if (paramFile1.delete())
        {
          int m = i;
          if (i != -1) {
            m = i + 1;
          }
          i = m;
        }
        else
        {
          localObject = new java/io/IOException;
          localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Failed to clean up ");
          localStringBuilder.append(paramFile1);
          ((IOException)localObject).<init>(localStringBuilder.toString());
          throw ((Throwable)localObject);
        }
      }
      catch (IOException localIOException)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Failed to migrate ");
        localStringBuilder.append(paramFile1);
        localStringBuilder.append(": ");
        localStringBuilder.append(localIOException);
        Log.w("ContextImpl", localStringBuilder.toString());
        i = -1;
        k++;
      }
    }
    return i;
  }
  
  private Intent registerReceiverInternal(BroadcastReceiver paramBroadcastReceiver, int paramInt1, IntentFilter paramIntentFilter, String paramString, Handler paramHandler, Context paramContext, int paramInt2)
  {
    Object localObject = null;
    if (paramBroadcastReceiver != null)
    {
      if ((mPackageInfo != null) && (paramContext != null))
      {
        if (paramHandler == null) {
          paramHandler = mMainThread.getHandler();
        }
        paramBroadcastReceiver = mPackageInfo.getReceiverDispatcher(paramBroadcastReceiver, paramContext, paramHandler, mMainThread.getInstrumentation(), true);
      }
      else
      {
        if (paramHandler == null) {
          paramHandler = mMainThread.getHandler();
        }
        paramBroadcastReceiver = new LoadedApk.ReceiverDispatcher(paramBroadcastReceiver, paramContext, paramHandler, null, true).getIIntentReceiver();
      }
    }
    else {
      paramBroadcastReceiver = localObject;
    }
    try
    {
      paramBroadcastReceiver = ActivityManager.getService().registerReceiver(mMainThread.getApplicationThread(), mBasePackageName, paramBroadcastReceiver, paramIntentFilter, paramString, paramInt1, paramInt2);
      if (paramBroadcastReceiver != null)
      {
        paramBroadcastReceiver.setExtrasClassLoader(getClassLoader());
        paramBroadcastReceiver.prepareToEnterProcess();
      }
      return paramBroadcastReceiver;
    }
    catch (RemoteException paramBroadcastReceiver)
    {
      throw paramBroadcastReceiver.rethrowFromSystemServer();
    }
  }
  
  private int resolveUserId(Uri paramUri)
  {
    return ContentProvider.getUserIdFromUri(paramUri, getUserId());
  }
  
  static void setFilePermissionsFromMode(String paramString, int paramInt1, int paramInt2)
  {
    int i = 0x1B0 | paramInt2;
    paramInt2 = i;
    if ((paramInt1 & 0x1) != 0) {
      paramInt2 = i | 0x4;
    }
    i = paramInt2;
    if ((paramInt1 & 0x2) != 0) {
      i = paramInt2 | 0x2;
    }
    FileUtils.setPermissions(paramString, i, -1, -1);
  }
  
  private ComponentName startServiceCommon(Intent paramIntent, boolean paramBoolean, UserHandle paramUserHandle)
  {
    try
    {
      validateServiceIntent(paramIntent);
      paramIntent.prepareToLeaveProcess(this);
      paramUserHandle = ActivityManager.getService().startService(mMainThread.getApplicationThread(), paramIntent, paramIntent.resolveTypeIfNeeded(getContentResolver()), paramBoolean, getOpPackageName(), paramUserHandle.getIdentifier());
      if (paramUserHandle != null)
      {
        Object localObject;
        StringBuilder localStringBuilder;
        if (!paramUserHandle.getPackageName().equals("!"))
        {
          if (!paramUserHandle.getPackageName().equals("!!"))
          {
            if (paramUserHandle.getPackageName().equals("?"))
            {
              localObject = new java/lang/IllegalStateException;
              localStringBuilder = new java/lang/StringBuilder;
              localStringBuilder.<init>();
              localStringBuilder.append("Not allowed to start service ");
              localStringBuilder.append(paramIntent);
              localStringBuilder.append(": ");
              localStringBuilder.append(paramUserHandle.getClassName());
              ((IllegalStateException)localObject).<init>(localStringBuilder.toString());
              throw ((Throwable)localObject);
            }
          }
          else
          {
            localObject = new java/lang/SecurityException;
            localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append("Unable to start service ");
            localStringBuilder.append(paramIntent);
            localStringBuilder.append(": ");
            localStringBuilder.append(paramUserHandle.getClassName());
            ((SecurityException)localObject).<init>(localStringBuilder.toString());
            throw ((Throwable)localObject);
          }
        }
        else
        {
          localObject = new java/lang/SecurityException;
          localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Not allowed to start service ");
          localStringBuilder.append(paramIntent);
          localStringBuilder.append(" without permission ");
          localStringBuilder.append(paramUserHandle.getClassName());
          ((SecurityException)localObject).<init>(localStringBuilder.toString());
          throw ((Throwable)localObject);
        }
      }
      return paramUserHandle;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  private boolean stopServiceCommon(Intent paramIntent, UserHandle paramUserHandle)
  {
    try
    {
      validateServiceIntent(paramIntent);
      paramIntent.prepareToLeaveProcess(this);
      int i = ActivityManager.getService().stopService(mMainThread.getApplicationThread(), paramIntent, paramIntent.resolveTypeIfNeeded(getContentResolver()), paramUserHandle.getIdentifier());
      if (i >= 0)
      {
        boolean bool;
        if (i != 0) {
          bool = true;
        } else {
          bool = false;
        }
        return bool;
      }
      paramUserHandle = new java/lang/SecurityException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Not allowed to stop service ");
      localStringBuilder.append(paramIntent);
      paramUserHandle.<init>(localStringBuilder.toString());
      throw paramUserHandle;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  private String uriModeFlagToString(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if ((paramInt & 0x1) != 0) {
      localStringBuilder.append("read and ");
    }
    if ((paramInt & 0x2) != 0) {
      localStringBuilder.append("write and ");
    }
    if ((paramInt & 0x40) != 0) {
      localStringBuilder.append("persistable and ");
    }
    if ((paramInt & 0x80) != 0) {
      localStringBuilder.append("prefix and ");
    }
    if (localStringBuilder.length() > 5)
    {
      localStringBuilder.setLength(localStringBuilder.length() - 5);
      return localStringBuilder.toString();
    }
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unknown permission mode flags: ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private void validateServiceIntent(Intent paramIntent)
  {
    if ((paramIntent.getComponent() == null) && (paramIntent.getPackage() == null))
    {
      StringBuilder localStringBuilder;
      if (getApplicationInfotargetSdkVersion < 21)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Implicit intents with startService are not safe: ");
        localStringBuilder.append(paramIntent);
        localStringBuilder.append(" ");
        localStringBuilder.append(Debug.getCallers(2, 3));
        Log.w("ContextImpl", localStringBuilder.toString());
      }
      else
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Service Intent must be explicit: ");
        localStringBuilder.append(paramIntent);
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
    }
  }
  
  private void warnIfCallingFromSystemProcess()
  {
    if (Process.myUid() == 1000)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Calling a method in the system process without a qualified user: ");
      localStringBuilder.append(Debug.getCallers(5));
      Slog.w("ContextImpl", localStringBuilder.toString());
    }
  }
  
  public boolean bindService(Intent paramIntent, ServiceConnection paramServiceConnection, int paramInt)
  {
    warnIfCallingFromSystemProcess();
    return bindServiceCommon(paramIntent, paramServiceConnection, paramInt, mMainThread.getHandler(), getUser());
  }
  
  public boolean bindServiceAsUser(Intent paramIntent, ServiceConnection paramServiceConnection, int paramInt, Handler paramHandler, UserHandle paramUserHandle)
  {
    if (paramHandler != null) {
      return bindServiceCommon(paramIntent, paramServiceConnection, paramInt, paramHandler, paramUserHandle);
    }
    throw new IllegalArgumentException("handler must not be null.");
  }
  
  public boolean bindServiceAsUser(Intent paramIntent, ServiceConnection paramServiceConnection, int paramInt, UserHandle paramUserHandle)
  {
    return bindServiceCommon(paramIntent, paramServiceConnection, paramInt, mMainThread.getHandler(), paramUserHandle);
  }
  
  public boolean canLoadUnsafeResources()
  {
    boolean bool1 = getPackageName().equals(getOpPackageName());
    boolean bool2 = true;
    if (bool1) {
      return true;
    }
    if ((mFlags & 0x2) == 0) {
      bool2 = false;
    }
    return bool2;
  }
  
  public int checkCallingOrSelfPermission(String paramString)
  {
    if (paramString != null) {
      return checkPermission(paramString, Binder.getCallingPid(), Binder.getCallingUid());
    }
    throw new IllegalArgumentException("permission is null");
  }
  
  public int checkCallingOrSelfUriPermission(Uri paramUri, int paramInt)
  {
    return checkUriPermission(paramUri, Binder.getCallingPid(), Binder.getCallingUid(), paramInt);
  }
  
  public int checkCallingPermission(String paramString)
  {
    if (paramString != null)
    {
      int i = Binder.getCallingPid();
      if (i != Process.myPid()) {
        return checkPermission(paramString, i, Binder.getCallingUid());
      }
      return -1;
    }
    throw new IllegalArgumentException("permission is null");
  }
  
  public int checkCallingUriPermission(Uri paramUri, int paramInt)
  {
    int i = Binder.getCallingPid();
    if (i != Process.myPid()) {
      return checkUriPermission(paramUri, i, Binder.getCallingUid(), paramInt);
    }
    return -1;
  }
  
  public int checkPermission(String paramString, int paramInt1, int paramInt2)
  {
    if (paramString != null)
    {
      Object localObject = ActivityManager.getService();
      if (localObject == null)
      {
        paramInt1 = UserHandle.getAppId(paramInt2);
        if ((paramInt1 != 0) && (paramInt1 != 1000))
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Missing ActivityManager; assuming ");
          ((StringBuilder)localObject).append(paramInt2);
          ((StringBuilder)localObject).append(" does not hold ");
          ((StringBuilder)localObject).append(paramString);
          Slog.w("ContextImpl", ((StringBuilder)localObject).toString());
          return -1;
        }
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Missing ActivityManager; assuming ");
        ((StringBuilder)localObject).append(paramInt2);
        ((StringBuilder)localObject).append(" holds ");
        ((StringBuilder)localObject).append(paramString);
        Slog.w("ContextImpl", ((StringBuilder)localObject).toString());
        return 0;
      }
      try
      {
        paramInt1 = ((IActivityManager)localObject).checkPermission(paramString, paramInt1, paramInt2);
        return paramInt1;
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("permission is null");
  }
  
  public int checkPermission(String paramString, int paramInt1, int paramInt2, IBinder paramIBinder)
  {
    if (paramString != null) {
      try
      {
        paramInt1 = ActivityManager.getService().checkPermissionWithToken(paramString, paramInt1, paramInt2, paramIBinder);
        return paramInt1;
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("permission is null");
  }
  
  public int checkSelfPermission(String paramString)
  {
    if (paramString != null) {
      return checkPermission(paramString, Process.myPid(), Process.myUid());
    }
    throw new IllegalArgumentException("permission is null");
  }
  
  public int checkUriPermission(Uri paramUri, int paramInt1, int paramInt2, int paramInt3)
  {
    try
    {
      paramInt1 = ActivityManager.getService().checkUriPermission(ContentProvider.getUriWithoutUserId(paramUri), paramInt1, paramInt2, paramInt3, resolveUserId(paramUri), null);
      return paramInt1;
    }
    catch (RemoteException paramUri)
    {
      throw paramUri.rethrowFromSystemServer();
    }
  }
  
  public int checkUriPermission(Uri paramUri, int paramInt1, int paramInt2, int paramInt3, IBinder paramIBinder)
  {
    try
    {
      paramInt1 = ActivityManager.getService().checkUriPermission(ContentProvider.getUriWithoutUserId(paramUri), paramInt1, paramInt2, paramInt3, resolveUserId(paramUri), paramIBinder);
      return paramInt1;
    }
    catch (RemoteException paramUri)
    {
      throw paramUri.rethrowFromSystemServer();
    }
  }
  
  public int checkUriPermission(Uri paramUri, String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3)
  {
    if (((paramInt3 & 0x1) != 0) && ((paramString1 == null) || (checkPermission(paramString1, paramInt1, paramInt2) == 0))) {
      return 0;
    }
    if (((paramInt3 & 0x2) != 0) && ((paramString2 == null) || (checkPermission(paramString2, paramInt1, paramInt2) == 0))) {
      return 0;
    }
    if (paramUri != null) {
      paramInt1 = checkUriPermission(paramUri, paramInt1, paramInt2, paramInt3);
    } else {
      paramInt1 = -1;
    }
    return paramInt1;
  }
  
  @Deprecated
  public void clearWallpaper()
    throws IOException
  {
    getWallpaperManager().clear();
  }
  
  public Context createApplicationContext(ApplicationInfo paramApplicationInfo, int paramInt)
    throws PackageManager.NameNotFoundException
  {
    Object localObject = mMainThread.getPackageInfo(paramApplicationInfo, mResources.getCompatibilityInfo(), 0x40000000 | paramInt);
    if (localObject != null)
    {
      ContextImpl localContextImpl = new ContextImpl(this, mMainThread, (LoadedApk)localObject, null, mActivityToken, new UserHandle(UserHandle.getUserId(uid)), paramInt, null);
      if (mDisplay != null) {
        paramInt = mDisplay.getDisplayId();
      } else {
        paramInt = 0;
      }
      localContextImpl.setResources(createResources(mActivityToken, (LoadedApk)localObject, null, paramInt, null, getDisplayAdjustments(paramInt).getCompatibilityInfo()));
      if (mResources != null) {
        return localContextImpl;
      }
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Application package ");
    ((StringBuilder)localObject).append(packageName);
    ((StringBuilder)localObject).append(" not found");
    throw new PackageManager.NameNotFoundException(((StringBuilder)localObject).toString());
  }
  
  public Context createConfigurationContext(Configuration paramConfiguration)
  {
    if (paramConfiguration != null)
    {
      ContextImpl localContextImpl = new ContextImpl(this, mMainThread, mPackageInfo, mSplitName, mActivityToken, mUser, mFlags, mClassLoader);
      int i;
      if (mDisplay != null) {
        i = mDisplay.getDisplayId();
      } else {
        i = 0;
      }
      localContextImpl.setResources(createResources(mActivityToken, mPackageInfo, mSplitName, i, paramConfiguration, getDisplayAdjustments(i).getCompatibilityInfo()));
      return localContextImpl;
    }
    throw new IllegalArgumentException("overrideConfiguration must not be null");
  }
  
  public Context createContextForSplit(String paramString)
    throws PackageManager.NameNotFoundException
  {
    if (!mPackageInfo.getApplicationInfo().requestsIsolatedSplitLoading()) {
      return this;
    }
    ClassLoader localClassLoader = mPackageInfo.getSplitClassLoader(paramString);
    String[] arrayOfString = mPackageInfo.getSplitPaths(paramString);
    paramString = new ContextImpl(this, mMainThread, mPackageInfo, paramString, mActivityToken, mUser, mFlags, localClassLoader);
    if (mDisplay != null) {}
    for (int i = mDisplay.getDisplayId();; i = 0) {
      break;
    }
    paramString.setResources(ResourcesManager.getInstance().getResources(mActivityToken, mPackageInfo.getResDir(), arrayOfString, mPackageInfo.getOverlayDirs(), mPackageInfo.getApplicationInfo().sharedLibraryFiles, i, null, mPackageInfo.getCompatibilityInfo(), localClassLoader));
    return paramString;
  }
  
  public Context createCredentialProtectedStorageContext()
  {
    int i = mFlags;
    return new ContextImpl(this, mMainThread, mPackageInfo, mSplitName, mActivityToken, mUser, i & 0xFFFFFFF7 | 0x10, mClassLoader);
  }
  
  public Context createDeviceProtectedStorageContext()
  {
    int i = mFlags;
    return new ContextImpl(this, mMainThread, mPackageInfo, mSplitName, mActivityToken, mUser, i & 0xFFFFFFEF | 0x8, mClassLoader);
  }
  
  public Context createDisplayContext(Display paramDisplay)
  {
    if (paramDisplay != null)
    {
      ContextImpl localContextImpl = new ContextImpl(this, mMainThread, mPackageInfo, mSplitName, mActivityToken, mUser, mFlags, mClassLoader);
      int i = paramDisplay.getDisplayId();
      localContextImpl.setResources(createResources(mActivityToken, mPackageInfo, mSplitName, i, null, getDisplayAdjustments(i).getCompatibilityInfo()));
      mDisplay = paramDisplay;
      return localContextImpl;
    }
    throw new IllegalArgumentException("display must not be null");
  }
  
  public Context createPackageContext(String paramString, int paramInt)
    throws PackageManager.NameNotFoundException
  {
    return createPackageContextAsUser(paramString, paramInt, mUser);
  }
  
  public Context createPackageContextAsUser(String paramString, int paramInt, UserHandle paramUserHandle)
    throws PackageManager.NameNotFoundException
  {
    if ((!paramString.equals("system")) && (!paramString.equals("android")))
    {
      LoadedApk localLoadedApk = mMainThread.getPackageInfo(paramString, mResources.getCompatibilityInfo(), paramInt | 0x40000000, paramUserHandle.getIdentifier());
      if (localLoadedApk != null)
      {
        paramUserHandle = new ContextImpl(this, mMainThread, localLoadedApk, null, mActivityToken, paramUserHandle, paramInt, null);
        if (mDisplay != null) {
          paramInt = mDisplay.getDisplayId();
        } else {
          paramInt = 0;
        }
        paramUserHandle.setResources(createResources(mActivityToken, localLoadedApk, null, paramInt, null, getDisplayAdjustments(paramInt).getCompatibilityInfo()));
        if (mResources != null) {
          return paramUserHandle;
        }
      }
      paramUserHandle = new StringBuilder();
      paramUserHandle.append("Application package ");
      paramUserHandle.append(paramString);
      paramUserHandle.append(" not found");
      throw new PackageManager.NameNotFoundException(paramUserHandle.toString());
    }
    return new ContextImpl(this, mMainThread, mPackageInfo, null, mActivityToken, paramUserHandle, paramInt, null);
  }
  
  public String[] databaseList()
  {
    return FileUtils.listOrEmpty(getDatabasesDir());
  }
  
  public boolean deleteDatabase(String paramString)
  {
    try
    {
      boolean bool = SQLiteDatabase.deleteDatabase(getDatabasePath(paramString));
      return bool;
    }
    catch (Exception paramString) {}
    return false;
  }
  
  public boolean deleteFile(String paramString)
  {
    return makeFilename(getFilesDir(), paramString).delete();
  }
  
  public boolean deleteSharedPreferences(String paramString)
  {
    try
    {
      File localFile = getSharedPreferencesPath(paramString);
      paramString = SharedPreferencesImpl.makeBackupFile(localFile);
      getSharedPreferencesCacheLocked().remove(localFile);
      localFile.delete();
      paramString.delete();
      boolean bool;
      if ((!localFile.exists()) && (!paramString.exists())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    finally {}
  }
  
  public void enforceCallingOrSelfPermission(String paramString1, String paramString2)
  {
    enforce(paramString1, checkCallingOrSelfPermission(paramString1), true, Binder.getCallingUid(), paramString2);
  }
  
  public void enforceCallingOrSelfUriPermission(Uri paramUri, int paramInt, String paramString)
  {
    enforceForUri(paramInt, checkCallingOrSelfUriPermission(paramUri, paramInt), true, Binder.getCallingUid(), paramUri, paramString);
  }
  
  public void enforceCallingPermission(String paramString1, String paramString2)
  {
    enforce(paramString1, checkCallingPermission(paramString1), false, Binder.getCallingUid(), paramString2);
  }
  
  public void enforceCallingUriPermission(Uri paramUri, int paramInt, String paramString)
  {
    enforceForUri(paramInt, checkCallingUriPermission(paramUri, paramInt), false, Binder.getCallingUid(), paramUri, paramString);
  }
  
  public void enforcePermission(String paramString1, int paramInt1, int paramInt2, String paramString2)
  {
    enforce(paramString1, checkPermission(paramString1, paramInt1, paramInt2), false, paramInt2, paramString2);
  }
  
  public void enforceUriPermission(Uri paramUri, int paramInt1, int paramInt2, int paramInt3, String paramString)
  {
    enforceForUri(paramInt3, checkUriPermission(paramUri, paramInt1, paramInt2, paramInt3), false, paramInt2, paramUri, paramString);
  }
  
  public void enforceUriPermission(Uri paramUri, String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3, String paramString3)
  {
    enforceForUri(paramInt3, checkUriPermission(paramUri, paramString1, paramString2, paramInt1, paramInt2, paramInt3), false, paramInt2, paramUri, paramString3);
  }
  
  public String[] fileList()
  {
    return FileUtils.listOrEmpty(getFilesDir());
  }
  
  public IBinder getActivityToken()
  {
    return mActivityToken;
  }
  
  public Context getApplicationContext()
  {
    Application localApplication;
    if (mPackageInfo != null) {
      localApplication = mPackageInfo.getApplication();
    } else {
      localApplication = mMainThread.getApplication();
    }
    return localApplication;
  }
  
  public ApplicationInfo getApplicationInfo()
  {
    if (mPackageInfo != null) {
      return mPackageInfo.getApplicationInfo();
    }
    throw new RuntimeException("Not supported in system context");
  }
  
  public AssetManager getAssets()
  {
    return getResources().getAssets();
  }
  
  public AutofillManager.AutofillClient getAutofillClient()
  {
    return mAutofillClient;
  }
  
  public String getBasePackageName()
  {
    String str;
    if (mBasePackageName != null) {
      str = mBasePackageName;
    } else {
      str = getPackageName();
    }
    return str;
  }
  
  public File getCacheDir()
  {
    synchronized (mSync)
    {
      if (mCacheDir == null)
      {
        localFile = new java/io/File;
        localFile.<init>(getDataDir(), "cache");
        mCacheDir = localFile;
      }
      File localFile = ensurePrivateCacheDirExists(mCacheDir, "user.inode_cache");
      return localFile;
    }
  }
  
  public ClassLoader getClassLoader()
  {
    ClassLoader localClassLoader;
    if (mClassLoader != null) {
      localClassLoader = mClassLoader;
    } else if (mPackageInfo != null) {
      localClassLoader = mPackageInfo.getClassLoader();
    } else {
      localClassLoader = ClassLoader.getSystemClassLoader();
    }
    return localClassLoader;
  }
  
  public File getCodeCacheDir()
  {
    synchronized (mSync)
    {
      if (mCodeCacheDir == null)
      {
        localFile = new java/io/File;
        localFile.<init>(getDataDir(), "code_cache");
        mCodeCacheDir = localFile;
      }
      File localFile = ensurePrivateCacheDirExists(mCodeCacheDir, "user.inode_code_cache");
      return localFile;
    }
  }
  
  public ContentResolver getContentResolver()
  {
    return mContentResolver;
  }
  
  public File getDataDir()
  {
    if (mPackageInfo != null)
    {
      if (isCredentialProtectedStorage()) {
        localObject = mPackageInfo.getCredentialProtectedDataDirFile();
      } else if (isDeviceProtectedStorage()) {
        localObject = mPackageInfo.getDeviceProtectedDataDirFile();
      } else {
        localObject = mPackageInfo.getDataDirFile();
      }
      if (localObject != null)
      {
        if ((!((File)localObject).exists()) && (Process.myUid() == 1000))
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Data directory doesn't exist for package ");
          localStringBuilder.append(getPackageName());
          Log.wtf("ContextImpl", localStringBuilder.toString(), new Throwable());
        }
        return localObject;
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("No data directory found for package ");
      ((StringBuilder)localObject).append(getPackageName());
      throw new RuntimeException(((StringBuilder)localObject).toString());
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("No package details found for package ");
    ((StringBuilder)localObject).append(getPackageName());
    throw new RuntimeException(((StringBuilder)localObject).toString());
  }
  
  public File getDatabasePath(String paramString)
  {
    if (paramString.charAt(0) == File.separatorChar)
    {
      File localFile = new File(paramString.substring(0, paramString.lastIndexOf(File.separatorChar)));
      paramString = new File(localFile, paramString.substring(paramString.lastIndexOf(File.separatorChar)));
      if ((!localFile.isDirectory()) && (localFile.mkdir())) {
        FileUtils.setPermissions(localFile.getPath(), 505, -1, -1);
      }
    }
    else
    {
      paramString = makeFilename(getDatabasesDir(), paramString);
    }
    return paramString;
  }
  
  public File getDir(String paramString, int paramInt)
  {
    checkMode(paramInt);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("app_");
    localStringBuilder.append(paramString);
    paramString = localStringBuilder.toString();
    paramString = makeFilename(getDataDir(), paramString);
    if (!paramString.exists())
    {
      paramString.mkdir();
      setFilePermissionsFromMode(paramString.getPath(), paramInt, 505);
    }
    return paramString;
  }
  
  public Display getDisplay()
  {
    if (mDisplay == null) {
      return mResourcesManager.getAdjustedDisplay(0, mResources);
    }
    return mDisplay;
  }
  
  public DisplayAdjustments getDisplayAdjustments(int paramInt)
  {
    return mResources.getDisplayAdjustments();
  }
  
  public File getExternalCacheDir()
  {
    Object localObject = getExternalCacheDirs();
    if ((localObject != null) && (localObject.length > 0)) {
      localObject = localObject[0];
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  public File[] getExternalCacheDirs()
  {
    synchronized (mSync)
    {
      File[] arrayOfFile = ensureExternalDirsExistOrFilter(Environment.buildExternalStorageAppCacheDirs(getPackageName()));
      return arrayOfFile;
    }
  }
  
  public File getExternalFilesDir(String paramString)
  {
    paramString = getExternalFilesDirs(paramString);
    if ((paramString != null) && (paramString.length > 0)) {
      paramString = paramString[0];
    } else {
      paramString = null;
    }
    return paramString;
  }
  
  public File[] getExternalFilesDirs(String paramString)
  {
    synchronized (mSync)
    {
      File[] arrayOfFile1 = Environment.buildExternalStorageAppFilesDirs(getPackageName());
      File[] arrayOfFile2 = arrayOfFile1;
      if (paramString != null) {
        arrayOfFile2 = Environment.buildPaths(arrayOfFile1, new String[] { paramString });
      }
      paramString = ensureExternalDirsExistOrFilter(arrayOfFile2);
      return paramString;
    }
  }
  
  public File[] getExternalMediaDirs()
  {
    synchronized (mSync)
    {
      File[] arrayOfFile = ensureExternalDirsExistOrFilter(Environment.buildExternalStorageAppMediaDirs(getPackageName()));
      return arrayOfFile;
    }
  }
  
  public File getFileStreamPath(String paramString)
  {
    return makeFilename(getFilesDir(), paramString);
  }
  
  public File getFilesDir()
  {
    synchronized (mSync)
    {
      if (mFilesDir == null)
      {
        localFile = new java/io/File;
        localFile.<init>(getDataDir(), "files");
        mFilesDir = localFile;
      }
      File localFile = ensurePrivateDirExists(mFilesDir);
      return localFile;
    }
  }
  
  public IApplicationThread getIApplicationThread()
  {
    return mMainThread.getApplicationThread();
  }
  
  public Executor getMainExecutor()
  {
    return mMainThread.getExecutor();
  }
  
  public Looper getMainLooper()
  {
    return mMainThread.getLooper();
  }
  
  public Handler getMainThreadHandler()
  {
    return mMainThread.getHandler();
  }
  
  public File getNoBackupFilesDir()
  {
    synchronized (mSync)
    {
      if (mNoBackupFilesDir == null)
      {
        localFile = new java/io/File;
        localFile.<init>(getDataDir(), "no_backup");
        mNoBackupFilesDir = localFile;
      }
      File localFile = ensurePrivateDirExists(mNoBackupFilesDir);
      return localFile;
    }
  }
  
  public File getObbDir()
  {
    Object localObject = getObbDirs();
    if ((localObject != null) && (localObject.length > 0)) {
      localObject = localObject[0];
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  public File[] getObbDirs()
  {
    synchronized (mSync)
    {
      File[] arrayOfFile = ensureExternalDirsExistOrFilter(Environment.buildExternalStorageAppObbDirs(getPackageName()));
      return arrayOfFile;
    }
  }
  
  public String getOpPackageName()
  {
    String str;
    if (mOpPackageName != null) {
      str = mOpPackageName;
    } else {
      str = getBasePackageName();
    }
    return str;
  }
  
  final Context getOuterContext()
  {
    return mOuterContext;
  }
  
  public String getPackageCodePath()
  {
    if (mPackageInfo != null) {
      return mPackageInfo.getAppDir();
    }
    throw new RuntimeException("Not supported in system context");
  }
  
  public PackageManager getPackageManager()
  {
    if (mPackageManager != null) {
      return mPackageManager;
    }
    Object localObject = ActivityThread.getPackageManager();
    if (localObject != null)
    {
      localObject = new ApplicationPackageManager(this, (IPackageManager)localObject);
      mPackageManager = ((PackageManager)localObject);
      return localObject;
    }
    return null;
  }
  
  public String getPackageName()
  {
    if (mPackageInfo != null) {
      return mPackageInfo.getPackageName();
    }
    return "android";
  }
  
  public String getPackageResourcePath()
  {
    if (mPackageInfo != null) {
      return mPackageInfo.getResDir();
    }
    throw new RuntimeException("Not supported in system context");
  }
  
  public File getPreloadsFileCache()
  {
    return Environment.getDataPreloadsFileCacheDirectory(getPackageName());
  }
  
  final Context getReceiverRestrictedContext()
  {
    if (mReceiverRestrictedContext != null) {
      return mReceiverRestrictedContext;
    }
    ReceiverRestrictedContext localReceiverRestrictedContext = new ReceiverRestrictedContext(getOuterContext());
    mReceiverRestrictedContext = localReceiverRestrictedContext;
    return localReceiverRestrictedContext;
  }
  
  public Resources getResources()
  {
    return mResources;
  }
  
  public IServiceConnection getServiceDispatcher(ServiceConnection paramServiceConnection, Handler paramHandler, int paramInt)
  {
    return mPackageInfo.getServiceDispatcher(paramServiceConnection, getOuterContext(), paramHandler, paramInt);
  }
  
  public SharedPreferences getSharedPreferences(File paramFile, int paramInt)
  {
    try
    {
      ArrayMap localArrayMap = getSharedPreferencesCacheLocked();
      SharedPreferencesImpl localSharedPreferencesImpl = (SharedPreferencesImpl)localArrayMap.get(paramFile);
      if (localSharedPreferencesImpl == null)
      {
        checkMode(paramInt);
        if ((getApplicationInfotargetSdkVersion >= 26) && (isCredentialProtectedStorage()) && (!((UserManager)getSystemService(UserManager.class)).isUserUnlockingOrUnlocked(UserHandle.myUserId())))
        {
          paramFile = new java/lang/IllegalStateException;
          paramFile.<init>("SharedPreferences in credential encrypted storage are not available until after user is unlocked");
          throw paramFile;
        }
        localSharedPreferencesImpl = new android/app/SharedPreferencesImpl;
        localSharedPreferencesImpl.<init>(paramFile, paramInt);
        localArrayMap.put(paramFile, localSharedPreferencesImpl);
        return localSharedPreferencesImpl;
      }
      if (((paramInt & 0x4) != 0) || (getApplicationInfotargetSdkVersion < 11)) {
        localSharedPreferencesImpl.startReloadIfChangedUnexpectedly();
      }
      return localSharedPreferencesImpl;
    }
    finally {}
  }
  
  public SharedPreferences getSharedPreferences(String paramString, int paramInt)
  {
    String str = paramString;
    if (mPackageInfo.getApplicationInfo().targetSdkVersion < 19)
    {
      str = paramString;
      if (paramString == null) {
        str = "null";
      }
    }
    try
    {
      if (mSharedPrefsPaths == null)
      {
        paramString = new android/util/ArrayMap;
        paramString.<init>();
        mSharedPrefsPaths = paramString;
      }
      File localFile = (File)mSharedPrefsPaths.get(str);
      paramString = localFile;
      if (localFile == null)
      {
        paramString = getSharedPreferencesPath(str);
        mSharedPrefsPaths.put(str, paramString);
      }
      return getSharedPreferences(paramString, paramInt);
    }
    finally {}
  }
  
  public File getSharedPreferencesPath(String paramString)
  {
    File localFile = getPreferencesDir();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append(".xml");
    return makeFilename(localFile, localStringBuilder.toString());
  }
  
  public Object getSystemService(String paramString)
  {
    return SystemServiceRegistry.getSystemService(this, paramString);
  }
  
  public String getSystemServiceName(Class<?> paramClass)
  {
    return SystemServiceRegistry.getSystemServiceName(paramClass);
  }
  
  public Resources.Theme getTheme()
  {
    synchronized (mSync)
    {
      if (mTheme != null)
      {
        localTheme = mTheme;
        return localTheme;
      }
      mThemeResource = Resources.selectDefaultTheme(mThemeResource, getOuterContextgetApplicationInfotargetSdkVersion);
      initializeTheme();
      Resources.Theme localTheme = mTheme;
      return localTheme;
    }
  }
  
  public int getThemeResId()
  {
    synchronized (mSync)
    {
      int i = mThemeResource;
      return i;
    }
  }
  
  public UserHandle getUser()
  {
    return mUser;
  }
  
  public int getUserId()
  {
    return mUser.getIdentifier();
  }
  
  @Deprecated
  public Drawable getWallpaper()
  {
    return getWallpaperManager().getDrawable();
  }
  
  @Deprecated
  public int getWallpaperDesiredMinimumHeight()
  {
    return getWallpaperManager().getDesiredMinimumHeight();
  }
  
  @Deprecated
  public int getWallpaperDesiredMinimumWidth()
  {
    return getWallpaperManager().getDesiredMinimumWidth();
  }
  
  public void grantUriPermission(String paramString, Uri paramUri, int paramInt)
  {
    try
    {
      ActivityManager.getService().grantUriPermission(mMainThread.getApplicationThread(), paramString, ContentProvider.getUriWithoutUserId(paramUri), paramInt, resolveUserId(paramUri));
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  void installSystemApplicationInfo(ApplicationInfo paramApplicationInfo, ClassLoader paramClassLoader)
  {
    mPackageInfo.installSystemApplicationInfo(paramApplicationInfo, paramClassLoader);
  }
  
  public boolean isAutofillCompatibilityEnabled()
  {
    return mIsAutofillCompatEnabled;
  }
  
  public boolean isCredentialProtectedStorage()
  {
    boolean bool;
    if ((mFlags & 0x10) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isDeviceProtectedStorage()
  {
    boolean bool;
    if ((mFlags & 0x8) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isRestricted()
  {
    boolean bool;
    if ((mFlags & 0x4) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean moveDatabaseFrom(Context paramContext, String paramString)
  {
    try
    {
      paramContext = paramContext.getDatabasePath(paramString);
      paramString = getDatabasePath(paramString);
      boolean bool;
      if (moveFiles(paramContext.getParentFile(), paramString.getParentFile(), paramContext.getName()) != -1) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    finally {}
  }
  
  public boolean moveSharedPreferencesFrom(Context paramContext, String paramString)
  {
    try
    {
      paramContext = paramContext.getSharedPreferencesPath(paramString);
      paramString = getSharedPreferencesPath(paramString);
      int i = moveFiles(paramContext.getParentFile(), paramString.getParentFile(), paramContext.getName());
      if (i > 0)
      {
        ArrayMap localArrayMap = getSharedPreferencesCacheLocked();
        localArrayMap.remove(paramContext);
        localArrayMap.remove(paramString);
      }
      boolean bool;
      if (i != -1) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    finally {}
  }
  
  public FileInputStream openFileInput(String paramString)
    throws FileNotFoundException
  {
    return new FileInputStream(makeFilename(getFilesDir(), paramString));
  }
  
  public FileOutputStream openFileOutput(String paramString, int paramInt)
    throws FileNotFoundException
  {
    checkMode(paramInt);
    boolean bool;
    if ((0x8000 & paramInt) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    paramString = makeFilename(getFilesDir(), paramString);
    try
    {
      FileOutputStream localFileOutputStream = new java/io/FileOutputStream;
      localFileOutputStream.<init>(paramString, bool);
      setFilePermissionsFromMode(paramString.getPath(), paramInt, 0);
      return localFileOutputStream;
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      Object localObject = paramString.getParentFile();
      ((File)localObject).mkdir();
      FileUtils.setPermissions(((File)localObject).getPath(), 505, -1, -1);
      localObject = new FileOutputStream(paramString, bool);
      setFilePermissionsFromMode(paramString.getPath(), paramInt, 0);
      return localObject;
    }
  }
  
  public SQLiteDatabase openOrCreateDatabase(String paramString, int paramInt, SQLiteDatabase.CursorFactory paramCursorFactory)
  {
    return openOrCreateDatabase(paramString, paramInt, paramCursorFactory, null);
  }
  
  public SQLiteDatabase openOrCreateDatabase(String paramString, int paramInt, SQLiteDatabase.CursorFactory paramCursorFactory, DatabaseErrorHandler paramDatabaseErrorHandler)
  {
    checkMode(paramInt);
    paramString = getDatabasePath(paramString);
    int i = 268435456;
    if ((paramInt & 0x8) != 0) {
      i = 0x10000000 | 0x20000000;
    }
    int j = i;
    if ((paramInt & 0x10) != 0) {
      j = i | 0x10;
    }
    paramCursorFactory = SQLiteDatabase.openDatabase(paramString.getPath(), paramCursorFactory, j, paramDatabaseErrorHandler);
    setFilePermissionsFromMode(paramString.getPath(), paramInt, 0);
    return paramCursorFactory;
  }
  
  @Deprecated
  public Drawable peekWallpaper()
  {
    return getWallpaperManager().peekDrawable();
  }
  
  final void performFinalCleanup(String paramString1, String paramString2)
  {
    mPackageInfo.removeContextRegistrations(getOuterContext(), paramString1, paramString2);
  }
  
  public Intent registerReceiver(BroadcastReceiver paramBroadcastReceiver, IntentFilter paramIntentFilter)
  {
    return registerReceiver(paramBroadcastReceiver, paramIntentFilter, null, null);
  }
  
  public Intent registerReceiver(BroadcastReceiver paramBroadcastReceiver, IntentFilter paramIntentFilter, int paramInt)
  {
    return registerReceiver(paramBroadcastReceiver, paramIntentFilter, null, null, paramInt);
  }
  
  public Intent registerReceiver(BroadcastReceiver paramBroadcastReceiver, IntentFilter paramIntentFilter, String paramString, Handler paramHandler)
  {
    return registerReceiverInternal(paramBroadcastReceiver, getUserId(), paramIntentFilter, paramString, paramHandler, getOuterContext(), 0);
  }
  
  public Intent registerReceiver(BroadcastReceiver paramBroadcastReceiver, IntentFilter paramIntentFilter, String paramString, Handler paramHandler, int paramInt)
  {
    return registerReceiverInternal(paramBroadcastReceiver, getUserId(), paramIntentFilter, paramString, paramHandler, getOuterContext(), paramInt);
  }
  
  public Intent registerReceiverAsUser(BroadcastReceiver paramBroadcastReceiver, UserHandle paramUserHandle, IntentFilter paramIntentFilter, String paramString, Handler paramHandler)
  {
    return registerReceiverInternal(paramBroadcastReceiver, paramUserHandle.getIdentifier(), paramIntentFilter, paramString, paramHandler, getOuterContext(), 0);
  }
  
  public void reloadSharedPreferences()
  {
    ArrayList localArrayList = new ArrayList();
    try
    {
      ArrayMap localArrayMap = getSharedPreferencesCacheLocked();
      int i = 0;
      for (int j = 0; j < localArrayMap.size(); j++)
      {
        SharedPreferencesImpl localSharedPreferencesImpl = (SharedPreferencesImpl)localArrayMap.valueAt(j);
        if (localSharedPreferencesImpl != null) {
          localArrayList.add(localSharedPreferencesImpl);
        }
      }
      for (j = i; j < localArrayList.size(); j++) {
        ((SharedPreferencesImpl)localArrayList.get(j)).startReloadIfChangedUnexpectedly();
      }
      return;
    }
    finally {}
  }
  
  @Deprecated
  public void removeStickyBroadcast(Intent paramIntent)
  {
    String str = paramIntent.resolveTypeIfNeeded(getContentResolver());
    Intent localIntent = paramIntent;
    if (str != null)
    {
      localIntent = new Intent(paramIntent);
      localIntent.setDataAndType(localIntent.getData(), str);
    }
    try
    {
      localIntent.prepareToLeaveProcess(this);
      ActivityManager.getService().unbroadcastIntent(mMainThread.getApplicationThread(), localIntent, getUserId());
      return;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void removeStickyBroadcastAsUser(Intent paramIntent, UserHandle paramUserHandle)
  {
    String str = paramIntent.resolveTypeIfNeeded(getContentResolver());
    Intent localIntent = paramIntent;
    if (str != null)
    {
      localIntent = new Intent(paramIntent);
      localIntent.setDataAndType(localIntent.getData(), str);
    }
    try
    {
      localIntent.prepareToLeaveProcess(this);
      ActivityManager.getService().unbroadcastIntent(mMainThread.getApplicationThread(), localIntent, paramUserHandle.getIdentifier());
      return;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  public void revokeUriPermission(Uri paramUri, int paramInt)
  {
    try
    {
      ActivityManager.getService().revokeUriPermission(mMainThread.getApplicationThread(), null, ContentProvider.getUriWithoutUserId(paramUri), paramInt, resolveUserId(paramUri));
      return;
    }
    catch (RemoteException paramUri)
    {
      throw paramUri.rethrowFromSystemServer();
    }
  }
  
  public void revokeUriPermission(String paramString, Uri paramUri, int paramInt)
  {
    try
    {
      ActivityManager.getService().revokeUriPermission(mMainThread.getApplicationThread(), paramString, ContentProvider.getUriWithoutUserId(paramUri), paramInt, resolveUserId(paramUri));
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  final void scheduleFinalCleanup(String paramString1, String paramString2)
  {
    mMainThread.scheduleContextCleanup(this, paramString1, paramString2);
  }
  
  public void sendBroadcast(Intent paramIntent)
  {
    warnIfCallingFromSystemProcess();
    String str = paramIntent.resolveTypeIfNeeded(getContentResolver());
    try
    {
      paramIntent.prepareToLeaveProcess(this);
      ActivityManager.getService().broadcastIntent(mMainThread.getApplicationThread(), paramIntent, str, null, -1, null, null, null, -1, null, false, false, getUserId());
      return;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  public void sendBroadcast(Intent paramIntent, String paramString)
  {
    warnIfCallingFromSystemProcess();
    String str = paramIntent.resolveTypeIfNeeded(getContentResolver());
    if (paramString == null) {}
    String[] arrayOfString;
    for (paramString = null;; paramString = arrayOfString)
    {
      break;
      arrayOfString = new String[1];
      arrayOfString[0] = paramString;
    }
    try
    {
      paramIntent.prepareToLeaveProcess(this);
      ActivityManager.getService().broadcastIntent(mMainThread.getApplicationThread(), paramIntent, str, null, -1, null, null, paramString, -1, null, false, false, getUserId());
      return;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  public void sendBroadcast(Intent paramIntent, String paramString, int paramInt)
  {
    warnIfCallingFromSystemProcess();
    String str = paramIntent.resolveTypeIfNeeded(getContentResolver());
    if (paramString == null) {}
    String[] arrayOfString;
    for (paramString = null;; paramString = arrayOfString)
    {
      break;
      arrayOfString = new String[1];
      arrayOfString[0] = paramString;
    }
    try
    {
      paramIntent.prepareToLeaveProcess(this);
      ActivityManager.getService().broadcastIntent(mMainThread.getApplicationThread(), paramIntent, str, null, -1, null, null, paramString, paramInt, null, false, false, getUserId());
      return;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  public void sendBroadcast(Intent paramIntent, String paramString, Bundle paramBundle)
  {
    warnIfCallingFromSystemProcess();
    String str = paramIntent.resolveTypeIfNeeded(getContentResolver());
    if (paramString == null) {}
    String[] arrayOfString;
    for (paramString = null;; paramString = arrayOfString)
    {
      break;
      arrayOfString = new String[1];
      arrayOfString[0] = paramString;
    }
    try
    {
      paramIntent.prepareToLeaveProcess(this);
      ActivityManager.getService().broadcastIntent(mMainThread.getApplicationThread(), paramIntent, str, null, -1, null, null, paramString, -1, paramBundle, false, false, getUserId());
      return;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  public void sendBroadcastAsUser(Intent paramIntent, UserHandle paramUserHandle)
  {
    String str = paramIntent.resolveTypeIfNeeded(getContentResolver());
    try
    {
      paramIntent.prepareToLeaveProcess(this);
      ActivityManager.getService().broadcastIntent(mMainThread.getApplicationThread(), paramIntent, str, null, -1, null, null, null, -1, null, false, false, paramUserHandle.getIdentifier());
      return;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  public void sendBroadcastAsUser(Intent paramIntent, UserHandle paramUserHandle, String paramString)
  {
    sendBroadcastAsUser(paramIntent, paramUserHandle, paramString, -1);
  }
  
  public void sendBroadcastAsUser(Intent paramIntent, UserHandle paramUserHandle, String paramString, int paramInt)
  {
    String str = paramIntent.resolveTypeIfNeeded(getContentResolver());
    if (paramString == null) {}
    String[] arrayOfString;
    for (paramString = null;; paramString = arrayOfString)
    {
      break;
      arrayOfString = new String[1];
      arrayOfString[0] = paramString;
    }
    try
    {
      paramIntent.prepareToLeaveProcess(this);
      ActivityManager.getService().broadcastIntent(mMainThread.getApplicationThread(), paramIntent, str, null, -1, null, null, paramString, paramInt, null, false, false, paramUserHandle.getIdentifier());
      return;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  public void sendBroadcastAsUser(Intent paramIntent, UserHandle paramUserHandle, String paramString, Bundle paramBundle)
  {
    String str = paramIntent.resolveTypeIfNeeded(getContentResolver());
    if (paramString == null) {}
    String[] arrayOfString;
    for (paramString = null;; paramString = arrayOfString)
    {
      break;
      arrayOfString = new String[1];
      arrayOfString[0] = paramString;
    }
    try
    {
      paramIntent.prepareToLeaveProcess(this);
      ActivityManager.getService().broadcastIntent(mMainThread.getApplicationThread(), paramIntent, str, null, -1, null, null, paramString, -1, paramBundle, false, false, paramUserHandle.getIdentifier());
      return;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  public void sendBroadcastAsUserMultiplePermissions(Intent paramIntent, UserHandle paramUserHandle, String[] paramArrayOfString)
  {
    warnIfCallingFromSystemProcess();
    String str = paramIntent.resolveTypeIfNeeded(getContentResolver());
    try
    {
      paramIntent.prepareToLeaveProcess(this);
      ActivityManager.getService().broadcastIntent(mMainThread.getApplicationThread(), paramIntent, str, null, -1, null, null, paramArrayOfString, -1, null, false, false, paramUserHandle.getIdentifier());
      return;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  public void sendBroadcastMultiplePermissions(Intent paramIntent, String[] paramArrayOfString)
  {
    warnIfCallingFromSystemProcess();
    String str = paramIntent.resolveTypeIfNeeded(getContentResolver());
    try
    {
      paramIntent.prepareToLeaveProcess(this);
      ActivityManager.getService().broadcastIntent(mMainThread.getApplicationThread(), paramIntent, str, null, -1, null, null, paramArrayOfString, -1, null, false, false, getUserId());
      return;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  public void sendOrderedBroadcast(Intent paramIntent, String paramString)
  {
    warnIfCallingFromSystemProcess();
    String str = paramIntent.resolveTypeIfNeeded(getContentResolver());
    if (paramString == null) {}
    String[] arrayOfString;
    for (paramString = null;; paramString = arrayOfString)
    {
      break;
      arrayOfString = new String[1];
      arrayOfString[0] = paramString;
    }
    try
    {
      paramIntent.prepareToLeaveProcess(this);
      ActivityManager.getService().broadcastIntent(mMainThread.getApplicationThread(), paramIntent, str, null, -1, null, null, paramString, -1, null, true, false, getUserId());
      return;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  public void sendOrderedBroadcast(Intent paramIntent, String paramString1, int paramInt1, BroadcastReceiver paramBroadcastReceiver, Handler paramHandler, int paramInt2, String paramString2, Bundle paramBundle)
  {
    sendOrderedBroadcast(paramIntent, paramString1, paramInt1, paramBroadcastReceiver, paramHandler, paramInt2, paramString2, paramBundle, null);
  }
  
  void sendOrderedBroadcast(Intent paramIntent, String paramString1, int paramInt1, BroadcastReceiver paramBroadcastReceiver, Handler paramHandler, int paramInt2, String paramString2, Bundle paramBundle1, Bundle paramBundle2)
  {
    warnIfCallingFromSystemProcess();
    String str = null;
    if (paramBroadcastReceiver != null)
    {
      if (mPackageInfo != null) {
        if (paramHandler == null) {
          paramHandler = mMainThread.getHandler();
        }
      }
      for (paramBroadcastReceiver = mPackageInfo.getReceiverDispatcher(paramBroadcastReceiver, getOuterContext(), paramHandler, mMainThread.getInstrumentation(), false);; paramBroadcastReceiver = new LoadedApk.ReceiverDispatcher(paramBroadcastReceiver, getOuterContext(), paramHandler, null, false).getIIntentReceiver())
      {
        break;
        if (paramHandler == null) {
          paramHandler = mMainThread.getHandler();
        }
      }
    }
    paramBroadcastReceiver = str;
    str = paramIntent.resolveTypeIfNeeded(getContentResolver());
    if (paramString1 == null) {}
    for (paramString1 = null;; paramString1 = paramHandler)
    {
      break;
      paramHandler = new String[1];
      paramHandler[0] = paramString1;
    }
    try
    {
      paramIntent.prepareToLeaveProcess(this);
      ActivityManager.getService().broadcastIntent(mMainThread.getApplicationThread(), paramIntent, str, paramBroadcastReceiver, paramInt2, paramString2, paramBundle1, paramString1, paramInt1, paramBundle2, true, false, getUserId());
      return;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  public void sendOrderedBroadcast(Intent paramIntent, String paramString1, BroadcastReceiver paramBroadcastReceiver, Handler paramHandler, int paramInt, String paramString2, Bundle paramBundle)
  {
    sendOrderedBroadcast(paramIntent, paramString1, -1, paramBroadcastReceiver, paramHandler, paramInt, paramString2, paramBundle, null);
  }
  
  public void sendOrderedBroadcast(Intent paramIntent, String paramString1, Bundle paramBundle1, BroadcastReceiver paramBroadcastReceiver, Handler paramHandler, int paramInt, String paramString2, Bundle paramBundle2)
  {
    sendOrderedBroadcast(paramIntent, paramString1, -1, paramBroadcastReceiver, paramHandler, paramInt, paramString2, paramBundle2, paramBundle1);
  }
  
  public void sendOrderedBroadcastAsUser(Intent paramIntent, UserHandle paramUserHandle, String paramString1, int paramInt1, BroadcastReceiver paramBroadcastReceiver, Handler paramHandler, int paramInt2, String paramString2, Bundle paramBundle)
  {
    sendOrderedBroadcastAsUser(paramIntent, paramUserHandle, paramString1, paramInt1, null, paramBroadcastReceiver, paramHandler, paramInt2, paramString2, paramBundle);
  }
  
  public void sendOrderedBroadcastAsUser(Intent paramIntent, UserHandle paramUserHandle, String paramString1, int paramInt1, Bundle paramBundle1, BroadcastReceiver paramBroadcastReceiver, Handler paramHandler, int paramInt2, String paramString2, Bundle paramBundle2)
  {
    String str = null;
    if (paramBroadcastReceiver != null)
    {
      if (mPackageInfo != null) {
        if (paramHandler == null) {
          paramHandler = mMainThread.getHandler();
        }
      }
      for (paramBroadcastReceiver = mPackageInfo.getReceiverDispatcher(paramBroadcastReceiver, getOuterContext(), paramHandler, mMainThread.getInstrumentation(), false);; paramBroadcastReceiver = new LoadedApk.ReceiverDispatcher(paramBroadcastReceiver, getOuterContext(), paramHandler, null, false).getIIntentReceiver())
      {
        break;
        if (paramHandler == null) {
          paramHandler = mMainThread.getHandler();
        }
      }
    }
    paramBroadcastReceiver = str;
    str = paramIntent.resolveTypeIfNeeded(getContentResolver());
    if (paramString1 == null) {}
    for (paramString1 = null;; paramString1 = paramHandler)
    {
      break;
      paramHandler = new String[1];
      paramHandler[0] = paramString1;
    }
    try
    {
      paramIntent.prepareToLeaveProcess(this);
      ActivityManager.getService().broadcastIntent(mMainThread.getApplicationThread(), paramIntent, str, paramBroadcastReceiver, paramInt2, paramString2, paramBundle2, paramString1, paramInt1, paramBundle1, true, false, paramUserHandle.getIdentifier());
      return;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  public void sendOrderedBroadcastAsUser(Intent paramIntent, UserHandle paramUserHandle, String paramString1, BroadcastReceiver paramBroadcastReceiver, Handler paramHandler, int paramInt, String paramString2, Bundle paramBundle)
  {
    sendOrderedBroadcastAsUser(paramIntent, paramUserHandle, paramString1, -1, null, paramBroadcastReceiver, paramHandler, paramInt, paramString2, paramBundle);
  }
  
  @Deprecated
  public void sendStickyBroadcast(Intent paramIntent)
  {
    warnIfCallingFromSystemProcess();
    String str = paramIntent.resolveTypeIfNeeded(getContentResolver());
    try
    {
      paramIntent.prepareToLeaveProcess(this);
      ActivityManager.getService().broadcastIntent(mMainThread.getApplicationThread(), paramIntent, str, null, -1, null, null, null, -1, null, false, true, getUserId());
      return;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void sendStickyBroadcastAsUser(Intent paramIntent, UserHandle paramUserHandle)
  {
    String str = paramIntent.resolveTypeIfNeeded(getContentResolver());
    try
    {
      paramIntent.prepareToLeaveProcess(this);
      ActivityManager.getService().broadcastIntent(mMainThread.getApplicationThread(), paramIntent, str, null, -1, null, null, null, -1, null, false, true, paramUserHandle.getIdentifier());
      return;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void sendStickyBroadcastAsUser(Intent paramIntent, UserHandle paramUserHandle, Bundle paramBundle)
  {
    String str = paramIntent.resolveTypeIfNeeded(getContentResolver());
    try
    {
      paramIntent.prepareToLeaveProcess(this);
      ActivityManager.getService().broadcastIntent(mMainThread.getApplicationThread(), paramIntent, str, null, -1, null, null, null, -1, paramBundle, false, true, paramUserHandle.getIdentifier());
      return;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void sendStickyOrderedBroadcast(Intent paramIntent, BroadcastReceiver paramBroadcastReceiver, Handler paramHandler, int paramInt, String paramString, Bundle paramBundle)
  {
    warnIfCallingFromSystemProcess();
    Object localObject = null;
    if (paramBroadcastReceiver != null)
    {
      if (mPackageInfo != null) {
        if (paramHandler == null) {
          paramHandler = mMainThread.getHandler();
        }
      }
      for (paramBroadcastReceiver = mPackageInfo.getReceiverDispatcher(paramBroadcastReceiver, getOuterContext(), paramHandler, mMainThread.getInstrumentation(), false);; paramBroadcastReceiver = new LoadedApk.ReceiverDispatcher(paramBroadcastReceiver, getOuterContext(), paramHandler, null, false).getIIntentReceiver())
      {
        break;
        if (paramHandler == null) {
          paramHandler = mMainThread.getHandler();
        }
      }
    }
    paramBroadcastReceiver = localObject;
    paramHandler = paramIntent.resolveTypeIfNeeded(getContentResolver());
    try
    {
      paramIntent.prepareToLeaveProcess(this);
      ActivityManager.getService().broadcastIntent(mMainThread.getApplicationThread(), paramIntent, paramHandler, paramBroadcastReceiver, paramInt, paramString, paramBundle, null, -1, null, true, true, getUserId());
      return;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void sendStickyOrderedBroadcastAsUser(Intent paramIntent, UserHandle paramUserHandle, BroadcastReceiver paramBroadcastReceiver, Handler paramHandler, int paramInt, String paramString, Bundle paramBundle)
  {
    Object localObject = null;
    if (paramBroadcastReceiver != null)
    {
      if (mPackageInfo != null) {
        if (paramHandler == null) {
          paramHandler = mMainThread.getHandler();
        }
      }
      for (paramBroadcastReceiver = mPackageInfo.getReceiverDispatcher(paramBroadcastReceiver, getOuterContext(), paramHandler, mMainThread.getInstrumentation(), false);; paramBroadcastReceiver = new LoadedApk.ReceiverDispatcher(paramBroadcastReceiver, getOuterContext(), paramHandler, null, false).getIIntentReceiver())
      {
        break;
        if (paramHandler == null) {
          paramHandler = mMainThread.getHandler();
        }
      }
    }
    paramBroadcastReceiver = localObject;
    paramHandler = paramIntent.resolveTypeIfNeeded(getContentResolver());
    try
    {
      paramIntent.prepareToLeaveProcess(this);
      ActivityManager.getService().broadcastIntent(mMainThread.getApplicationThread(), paramIntent, paramHandler, paramBroadcastReceiver, paramInt, paramString, paramBundle, null, -1, null, true, true, paramUserHandle.getIdentifier());
      return;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  public void setAutofillClient(AutofillManager.AutofillClient paramAutofillClient)
  {
    mAutofillClient = paramAutofillClient;
  }
  
  public void setAutofillCompatibilityEnabled(boolean paramBoolean)
  {
    mIsAutofillCompatEnabled = paramBoolean;
  }
  
  final void setOuterContext(Context paramContext)
  {
    mOuterContext = paramContext;
  }
  
  void setResources(Resources paramResources)
  {
    if ((paramResources instanceof CompatResources)) {
      ((CompatResources)paramResources).setContext(this);
    }
    mResources = paramResources;
  }
  
  public void setTheme(int paramInt)
  {
    synchronized (mSync)
    {
      if (mThemeResource != paramInt)
      {
        mThemeResource = paramInt;
        initializeTheme();
      }
      return;
    }
  }
  
  @Deprecated
  public void setWallpaper(Bitmap paramBitmap)
    throws IOException
  {
    getWallpaperManager().setBitmap(paramBitmap);
  }
  
  @Deprecated
  public void setWallpaper(InputStream paramInputStream)
    throws IOException
  {
    getWallpaperManager().setStream(paramInputStream);
  }
  
  public void startActivities(Intent[] paramArrayOfIntent)
  {
    warnIfCallingFromSystemProcess();
    startActivities(paramArrayOfIntent, null);
  }
  
  public void startActivities(Intent[] paramArrayOfIntent, Bundle paramBundle)
  {
    warnIfCallingFromSystemProcess();
    if ((paramArrayOfIntent[0].getFlags() & 0x10000000) != 0)
    {
      mMainThread.getInstrumentation().execStartActivities(getOuterContext(), mMainThread.getApplicationThread(), null, (Activity)null, paramArrayOfIntent, paramBundle);
      return;
    }
    throw new AndroidRuntimeException("Calling startActivities() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag on first Intent. Is this really what you want?");
  }
  
  public int startActivitiesAsUser(Intent[] paramArrayOfIntent, Bundle paramBundle, UserHandle paramUserHandle)
  {
    if ((paramArrayOfIntent[0].getFlags() & 0x10000000) != 0) {
      return mMainThread.getInstrumentation().execStartActivitiesAsUser(getOuterContext(), mMainThread.getApplicationThread(), null, (Activity)null, paramArrayOfIntent, paramBundle, paramUserHandle.getIdentifier());
    }
    throw new AndroidRuntimeException("Calling startActivities() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag on first Intent. Is this really what you want?");
  }
  
  public void startActivity(Intent paramIntent)
  {
    warnIfCallingFromSystemProcess();
    startActivity(paramIntent, null);
  }
  
  public void startActivity(Intent paramIntent, Bundle paramBundle)
  {
    warnIfCallingFromSystemProcess();
    int i = getApplicationInfotargetSdkVersion;
    if (((paramIntent.getFlags() & 0x10000000) == 0) && ((i < 24) || (i >= 28)) && ((paramBundle == null) || (ActivityOptions.fromBundle(paramBundle).getLaunchTaskId() == -1))) {
      throw new AndroidRuntimeException("Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag. Is this really what you want?");
    }
    mMainThread.getInstrumentation().execStartActivity(getOuterContext(), mMainThread.getApplicationThread(), null, (Activity)null, paramIntent, -1, paramBundle);
  }
  
  public void startActivityAsUser(Intent paramIntent, Bundle paramBundle, UserHandle paramUserHandle)
  {
    try
    {
      ActivityManager.getService().startActivityAsUser(mMainThread.getApplicationThread(), getBasePackageName(), paramIntent, paramIntent.resolveTypeIfNeeded(getContentResolver()), null, null, 0, 268435456, null, paramBundle, paramUserHandle.getIdentifier());
      return;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  public void startActivityAsUser(Intent paramIntent, UserHandle paramUserHandle)
  {
    startActivityAsUser(paramIntent, null, paramUserHandle);
  }
  
  public ComponentName startForegroundService(Intent paramIntent)
  {
    warnIfCallingFromSystemProcess();
    return startServiceCommon(paramIntent, true, mUser);
  }
  
  public ComponentName startForegroundServiceAsUser(Intent paramIntent, UserHandle paramUserHandle)
  {
    return startServiceCommon(paramIntent, true, paramUserHandle);
  }
  
  public boolean startInstrumentation(ComponentName paramComponentName, String paramString, Bundle paramBundle)
  {
    if (paramBundle != null) {
      try
      {
        paramBundle.setAllowFds(false);
      }
      catch (RemoteException paramComponentName)
      {
        break label41;
      }
    }
    boolean bool = ActivityManager.getService().startInstrumentation(paramComponentName, paramString, 0, paramBundle, null, null, getUserId(), null);
    return bool;
    label41:
    throw paramComponentName.rethrowFromSystemServer();
  }
  
  public void startIntentSender(IntentSender paramIntentSender, Intent paramIntent, int paramInt1, int paramInt2, int paramInt3)
    throws IntentSender.SendIntentException
  {
    startIntentSender(paramIntentSender, paramIntent, paramInt1, paramInt2, paramInt3, null);
  }
  
  public void startIntentSender(IntentSender paramIntentSender, Intent paramIntent, int paramInt1, int paramInt2, int paramInt3, Bundle paramBundle)
    throws IntentSender.SendIntentException
  {
    String str = null;
    if (paramIntent != null) {
      try
      {
        paramIntent.migrateExtraStreamToClipData();
        paramIntent.prepareToLeaveProcess(this);
        str = paramIntent.resolveTypeIfNeeded(getContentResolver());
      }
      catch (RemoteException paramIntentSender)
      {
        break label124;
      }
    }
    IActivityManager localIActivityManager = ActivityManager.getService();
    ActivityThread.ApplicationThread localApplicationThread = mMainThread.getApplicationThread();
    IIntentSender localIIntentSender;
    if (paramIntentSender != null) {
      localIIntentSender = paramIntentSender.getTarget();
    } else {
      localIIntentSender = null;
    }
    if (paramIntentSender != null) {
      paramIntentSender = paramIntentSender.getWhitelistToken();
    } else {
      paramIntentSender = null;
    }
    paramInt1 = localIActivityManager.startActivityIntentSender(localApplicationThread, localIIntentSender, paramIntentSender, paramIntent, str, null, null, 0, paramInt1, paramInt2, paramBundle);
    if (paramInt1 != -96)
    {
      Instrumentation.checkStartActivityResult(paramInt1, null);
      return;
    }
    paramIntentSender = new android/content/IntentSender$SendIntentException;
    paramIntentSender.<init>();
    throw paramIntentSender;
    label124:
    throw paramIntentSender.rethrowFromSystemServer();
  }
  
  public ComponentName startService(Intent paramIntent)
  {
    warnIfCallingFromSystemProcess();
    return startServiceCommon(paramIntent, false, mUser);
  }
  
  public ComponentName startServiceAsUser(Intent paramIntent, UserHandle paramUserHandle)
  {
    return startServiceCommon(paramIntent, false, paramUserHandle);
  }
  
  public boolean stopService(Intent paramIntent)
  {
    warnIfCallingFromSystemProcess();
    return stopServiceCommon(paramIntent, mUser);
  }
  
  public boolean stopServiceAsUser(Intent paramIntent, UserHandle paramUserHandle)
  {
    return stopServiceCommon(paramIntent, paramUserHandle);
  }
  
  public void unbindService(ServiceConnection paramServiceConnection)
  {
    if (paramServiceConnection != null)
    {
      if (mPackageInfo != null)
      {
        paramServiceConnection = mPackageInfo.forgetServiceDispatcher(getOuterContext(), paramServiceConnection);
        try
        {
          ActivityManager.getService().unbindService(paramServiceConnection);
          return;
        }
        catch (RemoteException paramServiceConnection)
        {
          throw paramServiceConnection.rethrowFromSystemServer();
        }
      }
      throw new RuntimeException("Not supported in system context");
    }
    throw new IllegalArgumentException("connection is null");
  }
  
  public void unregisterReceiver(BroadcastReceiver paramBroadcastReceiver)
  {
    if (mPackageInfo != null)
    {
      paramBroadcastReceiver = mPackageInfo.forgetReceiverDispatcher(getOuterContext(), paramBroadcastReceiver);
      try
      {
        ActivityManager.getService().unregisterReceiver(paramBroadcastReceiver);
        return;
      }
      catch (RemoteException paramBroadcastReceiver)
      {
        throw paramBroadcastReceiver.rethrowFromSystemServer();
      }
    }
    throw new RuntimeException("Not supported in system context");
  }
  
  public void updateDisplay(int paramInt)
  {
    mDisplay = mResourcesManager.getAdjustedDisplay(paramInt, mResources);
  }
  
  private static final class ApplicationContentResolver
    extends ContentResolver
  {
    private final ActivityThread mMainThread;
    
    public ApplicationContentResolver(Context paramContext, ActivityThread paramActivityThread)
    {
      super();
      mMainThread = ((ActivityThread)Preconditions.checkNotNull(paramActivityThread));
    }
    
    protected IContentProvider acquireExistingProvider(Context paramContext, String paramString)
    {
      return mMainThread.acquireExistingProvider(paramContext, ContentProvider.getAuthorityWithoutUserId(paramString), resolveUserIdFromAuthority(paramString), true);
    }
    
    protected IContentProvider acquireProvider(Context paramContext, String paramString)
    {
      return mMainThread.acquireProvider(paramContext, ContentProvider.getAuthorityWithoutUserId(paramString), resolveUserIdFromAuthority(paramString), true);
    }
    
    protected IContentProvider acquireUnstableProvider(Context paramContext, String paramString)
    {
      return mMainThread.acquireProvider(paramContext, ContentProvider.getAuthorityWithoutUserId(paramString), resolveUserIdFromAuthority(paramString), false);
    }
    
    public void appNotRespondingViaProvider(IContentProvider paramIContentProvider)
    {
      mMainThread.appNotRespondingViaProvider(paramIContentProvider.asBinder());
    }
    
    public boolean releaseProvider(IContentProvider paramIContentProvider)
    {
      return mMainThread.releaseProvider(paramIContentProvider, true);
    }
    
    public boolean releaseUnstableProvider(IContentProvider paramIContentProvider)
    {
      return mMainThread.releaseProvider(paramIContentProvider, false);
    }
    
    protected int resolveUserIdFromAuthority(String paramString)
    {
      return ContentProvider.getUserIdFromAuthority(paramString, getUserId());
    }
    
    public void unstableProviderDied(IContentProvider paramIContentProvider)
    {
      mMainThread.handleUnstableProviderDied(paramIContentProvider.asBinder(), true);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  static @interface ServiceInitializationState {}
}
