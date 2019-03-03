package android.app;

import android.content.BroadcastReceiver;
import android.content.BroadcastReceiver.PendingResult;
import android.content.ComponentName;
import android.content.Context;
import android.content.IIntentReceiver;
import android.content.IIntentReceiver.Stub;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.dex.ArtManager;
import android.content.pm.split.SplitDependencyLoader;
import android.content.res.AssetManager;
import android.content.res.CompatibilityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Process;
import android.os.RemoteException;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import android.view.DisplayAdjustments;
import com.android.internal.util.ArrayUtils;
import dalvik.system.VMRuntime;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public final class LoadedApk
{
  static final boolean DEBUG = false;
  private static final String PROPERTY_NAME_APPEND_NATIVE = "pi.append_native_lib_paths";
  static final String TAG = "LoadedApk";
  private final ActivityThread mActivityThread;
  private AppComponentFactory mAppComponentFactory;
  private String mAppDir;
  private Application mApplication;
  private ApplicationInfo mApplicationInfo;
  private final ClassLoader mBaseClassLoader;
  private ClassLoader mClassLoader;
  private File mCredentialProtectedDataDirFile;
  private String mDataDir;
  private File mDataDirFile;
  private File mDeviceProtectedDataDirFile;
  private final DisplayAdjustments mDisplayAdjustments = new DisplayAdjustments();
  private final boolean mIncludeCode;
  private String mLibDir;
  private String[] mOverlayDirs;
  final String mPackageName;
  private final ArrayMap<Context, ArrayMap<BroadcastReceiver, ReceiverDispatcher>> mReceivers = new ArrayMap();
  private final boolean mRegisterPackage;
  private String mResDir;
  Resources mResources;
  private final boolean mSecurityViolation;
  private final ArrayMap<Context, ArrayMap<ServiceConnection, ServiceDispatcher>> mServices = new ArrayMap();
  private String[] mSplitAppDirs;
  private String[] mSplitClassLoaderNames;
  private SplitDependencyLoaderImpl mSplitLoader;
  private String[] mSplitNames;
  private String[] mSplitResDirs;
  private final ArrayMap<Context, ArrayMap<ServiceConnection, ServiceDispatcher>> mUnboundServices = new ArrayMap();
  private final ArrayMap<Context, ArrayMap<BroadcastReceiver, ReceiverDispatcher>> mUnregisteredReceivers = new ArrayMap();
  
  LoadedApk(ActivityThread paramActivityThread)
  {
    mActivityThread = paramActivityThread;
    mApplicationInfo = new ApplicationInfo();
    mApplicationInfo.packageName = "android";
    mPackageName = "android";
    mAppDir = null;
    mResDir = null;
    mSplitAppDirs = null;
    mSplitResDirs = null;
    mSplitClassLoaderNames = null;
    mOverlayDirs = null;
    mDataDir = null;
    mDataDirFile = null;
    mDeviceProtectedDataDirFile = null;
    mCredentialProtectedDataDirFile = null;
    mLibDir = null;
    mBaseClassLoader = null;
    mSecurityViolation = false;
    mIncludeCode = true;
    mRegisterPackage = false;
    mClassLoader = ClassLoader.getSystemClassLoader();
    mResources = Resources.getSystem();
    mAppComponentFactory = createAppFactory(mApplicationInfo, mClassLoader);
  }
  
  public LoadedApk(ActivityThread paramActivityThread, ApplicationInfo paramApplicationInfo, CompatibilityInfo paramCompatibilityInfo, ClassLoader paramClassLoader, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    mActivityThread = paramActivityThread;
    setApplicationInfo(paramApplicationInfo);
    mPackageName = packageName;
    mBaseClassLoader = paramClassLoader;
    mSecurityViolation = paramBoolean1;
    mIncludeCode = paramBoolean2;
    mRegisterPackage = paramBoolean3;
    mDisplayAdjustments.setCompatibilityInfo(paramCompatibilityInfo);
    mAppComponentFactory = createAppFactory(mApplicationInfo, mBaseClassLoader);
  }
  
  private static ApplicationInfo adjustNativeLibraryPaths(ApplicationInfo paramApplicationInfo)
  {
    if ((primaryCpuAbi != null) && (secondaryCpuAbi != null))
    {
      String str = VMRuntime.getRuntime().vmInstructionSet();
      Object localObject1 = VMRuntime.getInstructionSet(secondaryCpuAbi);
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("ro.dalvik.vm.isa.");
      ((StringBuilder)localObject2).append((String)localObject1);
      localObject2 = SystemProperties.get(((StringBuilder)localObject2).toString());
      if (!((String)localObject2).isEmpty()) {
        localObject1 = localObject2;
      }
      if (str.equals(localObject1))
      {
        paramApplicationInfo = new ApplicationInfo(paramApplicationInfo);
        nativeLibraryDir = secondaryNativeLibraryDir;
        primaryCpuAbi = secondaryCpuAbi;
        return paramApplicationInfo;
      }
    }
    return paramApplicationInfo;
  }
  
  private static void appendApkLibPathIfNeeded(String paramString, ApplicationInfo paramApplicationInfo, List<String> paramList)
  {
    if ((paramList != null) && (primaryCpuAbi != null) && (paramString.endsWith(".apk")) && (targetSdkVersion >= 26))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("!/lib/");
      localStringBuilder.append(primaryCpuAbi);
      paramList.add(localStringBuilder.toString());
    }
  }
  
  private AppComponentFactory createAppFactory(ApplicationInfo paramApplicationInfo, ClassLoader paramClassLoader)
  {
    if ((appComponentFactory != null) && (paramClassLoader != null)) {
      try
      {
        paramApplicationInfo = (AppComponentFactory)paramClassLoader.loadClass(appComponentFactory).newInstance();
        return paramApplicationInfo;
      }
      catch (InstantiationException|IllegalAccessException|ClassNotFoundException paramApplicationInfo)
      {
        Slog.e("LoadedApk", "Unable to instantiate appComponentFactory", paramApplicationInfo);
      }
    }
    return AppComponentFactory.DEFAULT;
  }
  
  private void createOrUpdateClassLoaderLocked(List<String> paramList)
  {
    if (mPackageName.equals("android"))
    {
      if (mClassLoader != null) {
        return;
      }
      if (mBaseClassLoader != null) {
        mClassLoader = mBaseClassLoader;
      } else {
        mClassLoader = ClassLoader.getSystemClassLoader();
      }
      mAppComponentFactory = createAppFactory(mApplicationInfo, mClassLoader);
      return;
    }
    if ((!Objects.equals(mPackageName, ActivityThread.currentPackageName())) && (mIncludeCode)) {
      try
      {
        ActivityThread.getPackageManager().notifyPackageUse(mPackageName, 6);
      }
      catch (RemoteException paramList)
      {
        throw paramList.rethrowFromSystemServer();
      }
    }
    if (mRegisterPackage) {
      try
      {
        ActivityManager.getService().addPackageDependency(mPackageName);
      }
      catch (RemoteException paramList)
      {
        throw paramList.rethrowFromSystemServer();
      }
    }
    Object localObject1 = new ArrayList(10);
    Object localObject2 = new ArrayList(10);
    boolean bool1;
    if ((mApplicationInfo.isSystemApp()) && (!mApplicationInfo.isUpdatedSystemApp())) {
      bool1 = true;
    } else {
      bool1 = false;
    }
    Object localObject3 = System.getProperty("java.library.path");
    boolean bool2 = ((String)localObject3).contains("/vendor/lib");
    boolean bool3 = bool1;
    if (mApplicationInfo.getCodePath() != null)
    {
      bool3 = bool1;
      if (mApplicationInfo.isVendor())
      {
        bool3 = bool1;
        if ((bool2 ^ true)) {
          bool3 = false;
        }
      }
    }
    makePaths(mActivityThread, bool3, mApplicationInfo, (List)localObject1, (List)localObject2);
    Object localObject4 = mDataDir;
    Object localObject5 = localObject4;
    if (bool3)
    {
      localObject5 = new StringBuilder();
      ((StringBuilder)localObject5).append((String)localObject4);
      ((StringBuilder)localObject5).append(File.pathSeparator);
      ((StringBuilder)localObject5).append(Paths.get(getAppDir(), new String[0]).getParent().toString());
      localObject4 = ((StringBuilder)localObject5).toString();
      localObject5 = new StringBuilder();
      ((StringBuilder)localObject5).append((String)localObject4);
      ((StringBuilder)localObject5).append(File.pathSeparator);
      ((StringBuilder)localObject5).append((String)localObject3);
      localObject5 = ((StringBuilder)localObject5).toString();
    }
    String str = TextUtils.join(File.pathSeparator, (Iterable)localObject2);
    if (!mIncludeCode)
    {
      if (mClassLoader == null)
      {
        paramList = StrictMode.allowThreadDiskReads();
        mClassLoader = ApplicationLoaders.getDefault().getClassLoader("", mApplicationInfo.targetSdkVersion, bool3, str, (String)localObject5, mBaseClassLoader, null);
        StrictMode.setThreadPolicy(paramList);
        mAppComponentFactory = AppComponentFactory.DEFAULT;
      }
      return;
    }
    if (((List)localObject1).size() == 1) {}
    for (localObject4 = (String)((List)localObject1).get(0);; localObject4 = TextUtils.join(File.pathSeparator, (Iterable)localObject1)) {
      break;
    }
    int i = 0;
    if (mClassLoader == null)
    {
      localObject1 = StrictMode.allowThreadDiskReads();
      mClassLoader = ApplicationLoaders.getDefault().getClassLoader((String)localObject4, mApplicationInfo.targetSdkVersion, bool3, str, (String)localObject5, mBaseClassLoader, mApplicationInfo.classLoaderName);
      mAppComponentFactory = createAppFactory(mApplicationInfo, mClassLoader);
      StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)localObject1);
      i = 1;
    }
    int j = i;
    if ((!((List)localObject2).isEmpty()) && (SystemProperties.getBoolean("pi.append_native_lib_paths", true))) {
      localObject5 = StrictMode.allowThreadDiskReads();
    }
    try
    {
      ApplicationLoaders.getDefault().addNative(mClassLoader, (Collection)localObject2);
      StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)localObject5);
    }
    finally
    {
      StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)localObject5);
    }
    if (VMRuntime.getRuntime().is64Bit()) {
      localObject5 = "64";
    } else {
      localObject5 = "";
    }
    if (!((String)localObject3).contains("/vendor/lib"))
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("/vendor/lib");
      ((StringBuilder)localObject2).append((String)localObject5);
      ((List)localObject4).add(((StringBuilder)localObject2).toString());
    }
    if (!((String)localObject3).contains("/odm/lib"))
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("/odm/lib");
      ((StringBuilder)localObject2).append((String)localObject5);
      ((List)localObject4).add(((StringBuilder)localObject2).toString());
    }
    if (!((String)localObject3).contains("/product/lib"))
    {
      localObject3 = new StringBuilder();
      ((StringBuilder)localObject3).append("/product/lib");
      ((StringBuilder)localObject3).append((String)localObject5);
      ((List)localObject4).add(((StringBuilder)localObject3).toString());
    }
    if (!((List)localObject4).isEmpty()) {
      localObject5 = StrictMode.allowThreadDiskReads();
    }
    try
    {
      ApplicationLoaders.getDefault().addNative(mClassLoader, (Collection)localObject4);
      StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)localObject5);
    }
    finally
    {
      StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)localObject5);
    }
    if (paramList != null)
    {
      i = j;
      if (paramList.size() > 0)
      {
        paramList = TextUtils.join(File.pathSeparator, paramList);
        ApplicationLoaders.getDefault().addPath(mClassLoader, paramList);
        i = 1;
      }
    }
    if ((i != 0) && (!ActivityThread.isSystem())) {
      setupJitProfileSupport();
    }
  }
  
  private static String[] getLibrariesFor(String paramString)
  {
    try
    {
      paramString = ActivityThread.getPackageManager().getApplicationInfo(paramString, 1024, UserHandle.myUserId());
      if (paramString == null) {
        return null;
      }
      return sharedLibraryFiles;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  private void initializeJavaContextClassLoader()
  {
    Object localObject = ActivityThread.getPackageManager();
    try
    {
      PackageInfo localPackageInfo = ((IPackageManager)localObject).getPackageInfo(mPackageName, 268435456, UserHandle.myUserId());
      if (localPackageInfo != null)
      {
        localObject = sharedUserId;
        int i = 0;
        int j;
        if (localObject != null) {
          j = 1;
        } else {
          j = 0;
        }
        int k;
        if ((applicationInfo != null) && (!mPackageName.equals(applicationInfo.processName))) {
          k = 1;
        } else {
          k = 0;
        }
        if ((j == 0) && (k == 0)) {
          j = i;
        } else {
          j = 1;
        }
        if (j != 0) {
          localObject = new WarningContextClassLoader(null);
        } else {
          localObject = mClassLoader;
        }
        Thread.currentThread().setContextClassLoader((ClassLoader)localObject);
        return;
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Unable to get package info for ");
      ((StringBuilder)localObject).append(mPackageName);
      ((StringBuilder)localObject).append("; is package not installed?");
      throw new IllegalStateException(((StringBuilder)localObject).toString());
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public static void makePaths(ActivityThread paramActivityThread, ApplicationInfo paramApplicationInfo, List<String> paramList)
  {
    makePaths(paramActivityThread, false, paramApplicationInfo, paramList, null);
  }
  
  public static void makePaths(ActivityThread paramActivityThread, boolean paramBoolean, ApplicationInfo paramApplicationInfo, List<String> paramList1, List<String> paramList2)
  {
    String str1 = sourceDir;
    String str2 = nativeLibraryDir;
    String[] arrayOfString1 = sharedLibraryFiles;
    paramList1.clear();
    paramList1.add(str1);
    if ((splitSourceDirs != null) && (!paramApplicationInfo.requestsIsolatedSplitLoading())) {
      Collections.addAll(paramList1, splitSourceDirs);
    }
    if (paramList2 != null) {
      paramList2.clear();
    }
    Object localObject1 = null;
    Object localObject2 = localObject1;
    if (paramActivityThread != null)
    {
      String str3 = mInstrumentationPackageName;
      String str4 = mInstrumentationAppDir;
      String[] arrayOfString2 = mInstrumentationSplitAppDirs;
      String str5 = mInstrumentationLibDir;
      String str6 = mInstrumentedAppDir;
      String[] arrayOfString3 = mInstrumentedSplitAppDirs;
      paramActivityThread = mInstrumentedLibDir;
      if (!str1.equals(str4))
      {
        localObject2 = localObject1;
        if (!str1.equals(str6)) {}
      }
      else
      {
        paramList1.clear();
        paramList1.add(str4);
        if (!paramApplicationInfo.requestsIsolatedSplitLoading())
        {
          if (arrayOfString2 != null) {
            Collections.addAll(paramList1, arrayOfString2);
          }
          if (!str4.equals(str6))
          {
            paramList1.add(str6);
            if (arrayOfString3 != null) {
              Collections.addAll(paramList1, arrayOfString3);
            }
          }
        }
        if (paramList2 != null)
        {
          paramList2.add(str5);
          if (!str5.equals(paramActivityThread)) {
            paramList2.add(paramActivityThread);
          }
        }
        localObject2 = localObject1;
        if (!str6.equals(str4)) {
          localObject2 = getLibrariesFor(str3);
        }
      }
    }
    if (paramList2 != null)
    {
      if (paramList2.isEmpty()) {
        paramList2.add(str2);
      }
      if (primaryCpuAbi != null)
      {
        if (targetSdkVersion < 24)
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("/system/fake-libs");
          if (VMRuntime.is64BitAbi(primaryCpuAbi)) {
            paramActivityThread = "64";
          } else {
            paramActivityThread = "";
          }
          ((StringBuilder)localObject1).append(paramActivityThread);
          paramList2.add(((StringBuilder)localObject1).toString());
        }
        localObject1 = paramList1.iterator();
        while (((Iterator)localObject1).hasNext())
        {
          str2 = (String)((Iterator)localObject1).next();
          paramActivityThread = new StringBuilder();
          paramActivityThread.append(str2);
          paramActivityThread.append("!/lib/");
          paramActivityThread.append(primaryCpuAbi);
          paramList2.add(paramActivityThread.toString());
        }
      }
      if (paramBoolean) {
        paramList2.add(System.getProperty("java.library.path"));
      }
    }
    int k;
    int m;
    if (arrayOfString1 != null)
    {
      int i = arrayOfString1.length;
      int j = 0;
      k = 0;
      while (k < i)
      {
        paramActivityThread = arrayOfString1[k];
        m = j;
        if (!paramList1.contains(paramActivityThread))
        {
          paramList1.add(j, paramActivityThread);
          m = j + 1;
          appendApkLibPathIfNeeded(paramActivityThread, paramApplicationInfo, paramList2);
        }
        k++;
        j = m;
      }
    }
    if (localObject2 != null)
    {
      m = localObject2.length;
      for (k = 0; k < m; k++)
      {
        paramActivityThread = localObject2[k];
        if (!paramList1.contains(paramActivityThread))
        {
          paramList1.add(0, paramActivityThread);
          appendApkLibPathIfNeeded(paramActivityThread, paramApplicationInfo, paramList2);
        }
      }
    }
  }
  
  private void rewriteRValues(ClassLoader paramClassLoader, String paramString, int paramInt)
  {
    try
    {
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append(paramString);
      localStringBuilder.append(".R");
      paramClassLoader = paramClassLoader.loadClass(localStringBuilder.toString());
      try
      {
        paramClassLoader = paramClassLoader.getMethod("onResourcesLoaded", new Class[] { Integer.TYPE });
        try
        {
          paramClassLoader.invoke(null, new Object[] { Integer.valueOf(paramInt) });
          return;
        }
        catch (InvocationTargetException paramClassLoader)
        {
          paramClassLoader = paramClassLoader.getCause();
        }
        catch (IllegalAccessException paramClassLoader) {}
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Failed to rewrite resource references for ");
        localStringBuilder.append(paramString);
        throw new RuntimeException(localStringBuilder.toString(), paramClassLoader);
      }
      catch (NoSuchMethodException paramClassLoader) {}
      return;
    }
    catch (ClassNotFoundException paramClassLoader)
    {
      paramClassLoader = new StringBuilder();
      paramClassLoader.append("No resource references to update in package ");
      paramClassLoader.append(paramString);
      Log.i("LoadedApk", paramClassLoader.toString());
    }
  }
  
  private void setApplicationInfo(ApplicationInfo paramApplicationInfo)
  {
    int i = Process.myUid();
    ApplicationInfo localApplicationInfo = adjustNativeLibraryPaths(paramApplicationInfo);
    mApplicationInfo = localApplicationInfo;
    mAppDir = sourceDir;
    if (uid == i) {
      paramApplicationInfo = sourceDir;
    } else {
      paramApplicationInfo = publicSourceDir;
    }
    mResDir = paramApplicationInfo;
    mOverlayDirs = resourceDirs;
    mDataDir = dataDir;
    mLibDir = nativeLibraryDir;
    mDataDirFile = FileUtils.newFileOrNull(dataDir);
    mDeviceProtectedDataDirFile = FileUtils.newFileOrNull(deviceProtectedDataDir);
    mCredentialProtectedDataDirFile = FileUtils.newFileOrNull(credentialProtectedDataDir);
    mSplitNames = splitNames;
    mSplitAppDirs = splitSourceDirs;
    if (uid == i) {
      paramApplicationInfo = splitSourceDirs;
    } else {
      paramApplicationInfo = splitPublicSourceDirs;
    }
    mSplitResDirs = paramApplicationInfo;
    mSplitClassLoaderNames = splitClassLoaderNames;
    if ((localApplicationInfo.requestsIsolatedSplitLoading()) && (!ArrayUtils.isEmpty(mSplitNames))) {
      mSplitLoader = new SplitDependencyLoaderImpl(splitDependencies);
    }
  }
  
  private void setupJitProfileSupport()
  {
    if (!SystemProperties.getBoolean("dalvik.vm.usejitprofiles", false)) {
      return;
    }
    if (mApplicationInfo.uid != Process.myUid()) {
      return;
    }
    ArrayList localArrayList = new ArrayList();
    if ((mApplicationInfo.flags & 0x4) != 0) {
      localArrayList.add(mApplicationInfo.sourceDir);
    }
    if (mApplicationInfo.splitSourceDirs != null) {
      Collections.addAll(localArrayList, mApplicationInfo.splitSourceDirs);
    }
    if (localArrayList.isEmpty()) {
      return;
    }
    for (int i = localArrayList.size() - 1; i >= 0; i--)
    {
      String str;
      if (i == 0) {
        str = null;
      } else {
        str = mApplicationInfo.splitNames[(i - 1)];
      }
      VMRuntime.registerAppInfo(ArtManager.getCurrentProfilePath(mPackageName, UserHandle.myUserId(), str), new String[] { (String)localArrayList.get(i) });
    }
    DexLoadReporter.getInstance().registerAppDataDir(mPackageName, mDataDir);
  }
  
  public IIntentReceiver forgetReceiverDispatcher(Context paramContext, BroadcastReceiver paramBroadcastReceiver)
  {
    synchronized (mReceivers)
    {
      Object localObject1 = (ArrayMap)mReceivers.get(paramContext);
      Object localObject2;
      if (localObject1 != null)
      {
        ReceiverDispatcher localReceiverDispatcher = (ReceiverDispatcher)((ArrayMap)localObject1).get(paramBroadcastReceiver);
        if (localReceiverDispatcher != null)
        {
          ((ArrayMap)localObject1).remove(paramBroadcastReceiver);
          if (((ArrayMap)localObject1).size() == 0) {
            mReceivers.remove(paramContext);
          }
          if (paramBroadcastReceiver.getDebugUnregister())
          {
            localObject2 = (ArrayMap)mUnregisteredReceivers.get(paramContext);
            localObject1 = localObject2;
            if (localObject2 == null)
            {
              localObject1 = new android/util/ArrayMap;
              ((ArrayMap)localObject1).<init>();
              mUnregisteredReceivers.put(paramContext, localObject1);
            }
            paramContext = new java/lang/IllegalArgumentException;
            paramContext.<init>("Originally unregistered here:");
            paramContext.fillInStackTrace();
            localReceiverDispatcher.setUnregisterLocation(paramContext);
            ((ArrayMap)localObject1).put(paramBroadcastReceiver, localReceiverDispatcher);
          }
          mForgotten = true;
          paramContext = localReceiverDispatcher.getIIntentReceiver();
          return paramContext;
        }
      }
      localObject1 = (ArrayMap)mUnregisteredReceivers.get(paramContext);
      if (localObject1 != null)
      {
        localObject1 = (ReceiverDispatcher)((ArrayMap)localObject1).get(paramBroadcastReceiver);
        if (localObject1 != null)
        {
          paramContext = ((ReceiverDispatcher)localObject1).getUnregisterLocation();
          localObject1 = new java/lang/IllegalArgumentException;
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("Unregistering Receiver ");
          ((StringBuilder)localObject2).append(paramBroadcastReceiver);
          ((StringBuilder)localObject2).append(" that was already unregistered");
          ((IllegalArgumentException)localObject1).<init>(((StringBuilder)localObject2).toString(), paramContext);
          throw ((Throwable)localObject1);
        }
      }
      if (paramContext == null)
      {
        localObject2 = new java/lang/IllegalStateException;
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("Unbinding Receiver ");
        ((StringBuilder)localObject1).append(paramBroadcastReceiver);
        ((StringBuilder)localObject1).append(" from Context that is no longer in use: ");
        ((StringBuilder)localObject1).append(paramContext);
        ((IllegalStateException)localObject2).<init>(((StringBuilder)localObject1).toString());
        throw ((Throwable)localObject2);
      }
      paramContext = new java/lang/IllegalArgumentException;
      localObject1 = new java/lang/StringBuilder;
      ((StringBuilder)localObject1).<init>();
      ((StringBuilder)localObject1).append("Receiver not registered: ");
      ((StringBuilder)localObject1).append(paramBroadcastReceiver);
      paramContext.<init>(((StringBuilder)localObject1).toString());
      throw paramContext;
    }
  }
  
  public final IServiceConnection forgetServiceDispatcher(Context paramContext, ServiceConnection paramServiceConnection)
  {
    synchronized (mServices)
    {
      Object localObject1 = (ArrayMap)mServices.get(paramContext);
      Object localObject2;
      if (localObject1 != null)
      {
        ServiceDispatcher localServiceDispatcher = (ServiceDispatcher)((ArrayMap)localObject1).get(paramServiceConnection);
        if (localServiceDispatcher != null)
        {
          ((ArrayMap)localObject1).remove(paramServiceConnection);
          localServiceDispatcher.doForget();
          if (((ArrayMap)localObject1).size() == 0) {
            mServices.remove(paramContext);
          }
          if ((localServiceDispatcher.getFlags() & 0x2) != 0)
          {
            localObject2 = (ArrayMap)mUnboundServices.get(paramContext);
            localObject1 = localObject2;
            if (localObject2 == null)
            {
              localObject1 = new android/util/ArrayMap;
              ((ArrayMap)localObject1).<init>();
              mUnboundServices.put(paramContext, localObject1);
            }
            paramContext = new java/lang/IllegalArgumentException;
            paramContext.<init>("Originally unbound here:");
            paramContext.fillInStackTrace();
            localServiceDispatcher.setUnbindLocation(paramContext);
            ((ArrayMap)localObject1).put(paramServiceConnection, localServiceDispatcher);
          }
          paramContext = localServiceDispatcher.getIServiceConnection();
          return paramContext;
        }
      }
      localObject1 = (ArrayMap)mUnboundServices.get(paramContext);
      if (localObject1 != null)
      {
        localObject1 = (ServiceDispatcher)((ArrayMap)localObject1).get(paramServiceConnection);
        if (localObject1 != null)
        {
          localObject1 = ((ServiceDispatcher)localObject1).getUnbindLocation();
          paramContext = new java/lang/IllegalArgumentException;
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("Unbinding Service ");
          ((StringBuilder)localObject2).append(paramServiceConnection);
          ((StringBuilder)localObject2).append(" that was already unbound");
          paramContext.<init>(((StringBuilder)localObject2).toString(), (Throwable)localObject1);
          throw paramContext;
        }
      }
      if (paramContext == null)
      {
        localObject1 = new java/lang/IllegalStateException;
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("Unbinding Service ");
        ((StringBuilder)localObject2).append(paramServiceConnection);
        ((StringBuilder)localObject2).append(" from Context that is no longer in use: ");
        ((StringBuilder)localObject2).append(paramContext);
        ((IllegalStateException)localObject1).<init>(((StringBuilder)localObject2).toString());
        throw ((Throwable)localObject1);
      }
      paramContext = new java/lang/IllegalArgumentException;
      localObject1 = new java/lang/StringBuilder;
      ((StringBuilder)localObject1).<init>();
      ((StringBuilder)localObject1).append("Service not registered: ");
      ((StringBuilder)localObject1).append(paramServiceConnection);
      paramContext.<init>(((StringBuilder)localObject1).toString());
      throw paramContext;
    }
  }
  
  public String getAppDir()
  {
    return mAppDir;
  }
  
  public AppComponentFactory getAppFactory()
  {
    return mAppComponentFactory;
  }
  
  Application getApplication()
  {
    return mApplication;
  }
  
  public ApplicationInfo getApplicationInfo()
  {
    return mApplicationInfo;
  }
  
  public AssetManager getAssets()
  {
    return getResources().getAssets();
  }
  
  public ClassLoader getClassLoader()
  {
    try
    {
      if (mClassLoader == null) {
        createOrUpdateClassLoaderLocked(null);
      }
      ClassLoader localClassLoader = mClassLoader;
      return localClassLoader;
    }
    finally {}
  }
  
  public CompatibilityInfo getCompatibilityInfo()
  {
    return mDisplayAdjustments.getCompatibilityInfo();
  }
  
  public File getCredentialProtectedDataDirFile()
  {
    return mCredentialProtectedDataDirFile;
  }
  
  public String getDataDir()
  {
    return mDataDir;
  }
  
  public File getDataDirFile()
  {
    return mDataDirFile;
  }
  
  public File getDeviceProtectedDataDirFile()
  {
    return mDeviceProtectedDataDirFile;
  }
  
  public String getLibDir()
  {
    return mLibDir;
  }
  
  public String[] getOverlayDirs()
  {
    return mOverlayDirs;
  }
  
  public String getPackageName()
  {
    return mPackageName;
  }
  
  public IIntentReceiver getReceiverDispatcher(BroadcastReceiver paramBroadcastReceiver, Context paramContext, Handler paramHandler, Instrumentation paramInstrumentation, boolean paramBoolean)
  {
    ArrayMap localArrayMap1 = mReceivers;
    Object localObject1 = null;
    Object localObject2 = null;
    Object localObject3 = localObject1;
    if (paramBoolean) {
      try
      {
        ArrayMap localArrayMap2 = (ArrayMap)mReceivers.get(paramContext);
        localObject3 = localObject1;
        localObject2 = localArrayMap2;
        if (localArrayMap2 != null)
        {
          localObject3 = (ReceiverDispatcher)localArrayMap2.get(paramBroadcastReceiver);
          localObject2 = localArrayMap2;
        }
      }
      finally
      {
        break label173;
      }
    }
    if (localObject3 == null)
    {
      localObject3 = new android/app/LoadedApk$ReceiverDispatcher;
      ((ReceiverDispatcher)localObject3).<init>(paramBroadcastReceiver, paramContext, paramHandler, paramInstrumentation, paramBoolean);
      paramHandler = (Handler)localObject3;
      localObject3 = paramHandler;
      if (paramBoolean)
      {
        paramInstrumentation = localObject2;
        if (localObject2 == null)
        {
          paramInstrumentation = new android/util/ArrayMap;
          paramInstrumentation.<init>();
          mReceivers.put(paramContext, paramInstrumentation);
        }
        paramInstrumentation.put(paramBroadcastReceiver, paramHandler);
        localObject3 = paramHandler;
      }
    }
    else
    {
      ((ReceiverDispatcher)localObject3).validate(paramContext, paramHandler);
    }
    mForgotten = false;
    paramBroadcastReceiver = ((ReceiverDispatcher)localObject3).getIIntentReceiver();
    return paramBroadcastReceiver;
    label173:
    throw paramBroadcastReceiver;
  }
  
  public String getResDir()
  {
    return mResDir;
  }
  
  public Resources getResources()
  {
    if (mResources == null) {
      try
      {
        String[] arrayOfString = getSplitPaths(null);
        mResources = ResourcesManager.getInstance().getResources(null, mResDir, arrayOfString, mOverlayDirs, mApplicationInfo.sharedLibraryFiles, 0, null, getCompatibilityInfo(), getClassLoader());
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
        throw new AssertionError("null split not found");
      }
    }
    return mResources;
  }
  
  public final IServiceConnection getServiceDispatcher(ServiceConnection paramServiceConnection, Context paramContext, Handler paramHandler, int paramInt)
  {
    ArrayMap localArrayMap1 = mServices;
    Object localObject = null;
    try
    {
      ArrayMap localArrayMap2 = (ArrayMap)mServices.get(paramContext);
      if (localArrayMap2 != null) {
        localObject = (ServiceDispatcher)localArrayMap2.get(paramServiceConnection);
      }
      if (localObject == null)
      {
        localObject = new android/app/LoadedApk$ServiceDispatcher;
        ((ServiceDispatcher)localObject).<init>(paramServiceConnection, paramContext, paramHandler, paramInt);
        paramHandler = (Handler)localObject;
        localObject = localArrayMap2;
        if (localArrayMap2 == null)
        {
          localObject = new android/util/ArrayMap;
          ((ArrayMap)localObject).<init>();
          mServices.put(paramContext, localObject);
        }
        ((ArrayMap)localObject).put(paramServiceConnection, paramHandler);
      }
      else
      {
        ((ServiceDispatcher)localObject).validate(paramContext, paramHandler);
        paramHandler = (Handler)localObject;
      }
      paramServiceConnection = paramHandler.getIServiceConnection();
      return paramServiceConnection;
    }
    finally {}
  }
  
  public String[] getSplitAppDirs()
  {
    return mSplitAppDirs;
  }
  
  ClassLoader getSplitClassLoader(String paramString)
    throws PackageManager.NameNotFoundException
  {
    if (mSplitLoader == null) {
      return mClassLoader;
    }
    return mSplitLoader.getClassLoaderForSplit(paramString);
  }
  
  String[] getSplitPaths(String paramString)
    throws PackageManager.NameNotFoundException
  {
    if (mSplitLoader == null) {
      return mSplitResDirs;
    }
    return mSplitLoader.getSplitPathsForSplit(paramString);
  }
  
  public String[] getSplitResDirs()
  {
    return mSplitResDirs;
  }
  
  public int getTargetSdkVersion()
  {
    return mApplicationInfo.targetSdkVersion;
  }
  
  void installSystemApplicationInfo(ApplicationInfo paramApplicationInfo, ClassLoader paramClassLoader)
  {
    mApplicationInfo = paramApplicationInfo;
    mClassLoader = paramClassLoader;
    mAppComponentFactory = createAppFactory(paramApplicationInfo, paramClassLoader);
  }
  
  public boolean isSecurityViolation()
  {
    return mSecurityViolation;
  }
  
  public Application makeApplication(boolean paramBoolean, Instrumentation paramInstrumentation)
  {
    if (mApplication != null) {
      return mApplication;
    }
    Trace.traceBegin(64L, "makeApplication");
    Application localApplication = null;
    Object localObject1 = mApplicationInfo.className;
    Object localObject2;
    if (!paramBoolean)
    {
      localObject2 = localObject1;
      if (localObject1 != null) {}
    }
    else
    {
      localObject2 = "android.app.Application";
    }
    localObject1 = localApplication;
    try
    {
      ClassLoader localClassLoader = getClassLoader();
      localObject1 = localApplication;
      if (!mPackageName.equals("android"))
      {
        localObject1 = localApplication;
        Trace.traceBegin(64L, "initializeJavaContextClassLoader");
        localObject1 = localApplication;
        initializeJavaContextClassLoader();
        localObject1 = localApplication;
        Trace.traceEnd(64L);
      }
      localObject1 = localApplication;
      ContextImpl localContextImpl = ContextImpl.createAppContext(mActivityThread, this);
      localObject1 = localApplication;
      localApplication = mActivityThread.mInstrumentation.newApplication(localClassLoader, (String)localObject2, localContextImpl);
      localObject1 = localApplication;
      localContextImpl.setOuterContext(localApplication);
      localObject1 = localApplication;
    }
    catch (Exception localException1)
    {
      if (!mActivityThread.mInstrumentation.onException(localObject1, localException1)) {
        break label364;
      }
    }
    mActivityThread.mAllApplications.add(localObject1);
    mApplication = ((Application)localObject1);
    if (paramInstrumentation != null) {
      try
      {
        paramInstrumentation.callApplicationOnCreate((Application)localObject1);
      }
      catch (Exception localException2)
      {
        if (!paramInstrumentation.onException(localObject1, localException2))
        {
          Trace.traceEnd(64L);
          paramInstrumentation = new StringBuilder();
          paramInstrumentation.append("Unable to create application ");
          paramInstrumentation.append(localObject1.getClass().getName());
          paramInstrumentation.append(": ");
          paramInstrumentation.append(localException2.toString());
          throw new RuntimeException(paramInstrumentation.toString(), localException2);
        }
      }
    }
    paramInstrumentation = getAssets().getAssignedPackageIdentifiers();
    int i = paramInstrumentation.size();
    for (int j = 0; j < i; j++)
    {
      int k = paramInstrumentation.keyAt(j);
      if ((k != 1) && (k != 127)) {
        rewriteRValues(getClassLoader(), (String)paramInstrumentation.valueAt(j), k);
      }
    }
    Trace.traceEnd(64L);
    return localObject1;
    label364:
    Trace.traceEnd(64L);
    paramInstrumentation = new StringBuilder();
    paramInstrumentation.append("Unable to instantiate application ");
    paramInstrumentation.append(localException2);
    paramInstrumentation.append(": ");
    paramInstrumentation.append(localException1.toString());
    throw new RuntimeException(paramInstrumentation.toString(), localException1);
  }
  
  public void removeContextRegistrations(Context paramContext, String paramString1, String paramString2)
  {
    boolean bool = StrictMode.vmRegistrationLeaksEnabled();
    synchronized (mReceivers)
    {
      ArrayMap localArrayMap2 = (ArrayMap)mReceivers.remove(paramContext);
      int i = 0;
      int j;
      Object localObject1;
      Object localObject2;
      Object localObject3;
      if (localArrayMap2 != null)
      {
        j = 0;
        while (j < localArrayMap2.size())
        {
          localObject1 = (ReceiverDispatcher)localArrayMap2.valueAt(j);
          localObject2 = new android/app/IntentReceiverLeaked;
          localObject3 = new java/lang/StringBuilder;
          ((StringBuilder)localObject3).<init>();
          ((StringBuilder)localObject3).append(paramString2);
          ((StringBuilder)localObject3).append(" ");
          ((StringBuilder)localObject3).append(paramString1);
          ((StringBuilder)localObject3).append(" has leaked IntentReceiver ");
          ((StringBuilder)localObject3).append(((ReceiverDispatcher)localObject1).getIntentReceiver());
          ((StringBuilder)localObject3).append(" that was originally registered here. Are you missing a call to unregisterReceiver()?");
          ((IntentReceiverLeaked)localObject2).<init>(((StringBuilder)localObject3).toString());
          ((IntentReceiverLeaked)localObject2).setStackTrace(((ReceiverDispatcher)localObject1).getLocation().getStackTrace());
          Slog.e("ActivityThread", ((IntentReceiverLeaked)localObject2).getMessage(), (Throwable)localObject2);
          if (bool) {
            StrictMode.onIntentReceiverLeaked((Throwable)localObject2);
          }
          try
          {
            ActivityManager.getService().unregisterReceiver(((ReceiverDispatcher)localObject1).getIIntentReceiver());
            j++;
          }
          catch (RemoteException paramContext)
          {
            throw paramContext.rethrowFromSystemServer();
          }
        }
      }
      mUnregisteredReceivers.remove(paramContext);
      synchronized (mServices)
      {
        localArrayMap2 = (ArrayMap)mServices.remove(paramContext);
        if (localArrayMap2 != null)
        {
          j = i;
          while (j < localArrayMap2.size())
          {
            localObject3 = (ServiceDispatcher)localArrayMap2.valueAt(j);
            localObject2 = new android/app/ServiceConnectionLeaked;
            localObject1 = new java/lang/StringBuilder;
            ((StringBuilder)localObject1).<init>();
            ((StringBuilder)localObject1).append(paramString2);
            ((StringBuilder)localObject1).append(" ");
            ((StringBuilder)localObject1).append(paramString1);
            ((StringBuilder)localObject1).append(" has leaked ServiceConnection ");
            ((StringBuilder)localObject1).append(((ServiceDispatcher)localObject3).getServiceConnection());
            ((StringBuilder)localObject1).append(" that was originally bound here");
            ((ServiceConnectionLeaked)localObject2).<init>(((StringBuilder)localObject1).toString());
            ((ServiceConnectionLeaked)localObject2).setStackTrace(((ServiceDispatcher)localObject3).getLocation().getStackTrace());
            Slog.e("ActivityThread", ((ServiceConnectionLeaked)localObject2).getMessage(), (Throwable)localObject2);
            if (bool) {
              StrictMode.onServiceConnectionLeaked((Throwable)localObject2);
            }
            try
            {
              ActivityManager.getService().unbindService(((ServiceDispatcher)localObject3).getIServiceConnection());
              ((ServiceDispatcher)localObject3).doForget();
              j++;
            }
            catch (RemoteException paramContext)
            {
              throw paramContext.rethrowFromSystemServer();
            }
          }
        }
        mUnboundServices.remove(paramContext);
        return;
      }
    }
  }
  
  public void setCompatibilityInfo(CompatibilityInfo paramCompatibilityInfo)
  {
    mDisplayAdjustments.setCompatibilityInfo(paramCompatibilityInfo);
  }
  
  public void updateApplicationInfo(ApplicationInfo paramApplicationInfo, List<String> paramList)
  {
    setApplicationInfo(paramApplicationInfo);
    Object localObject = new ArrayList();
    makePaths(mActivityThread, paramApplicationInfo, (List)localObject);
    ArrayList localArrayList = new ArrayList(((List)localObject).size());
    if (paramList != null)
    {
      Iterator localIterator = ((List)localObject).iterator();
      while (localIterator.hasNext())
      {
        String str1 = (String)localIterator.next();
        String str2 = str1.substring(str1.lastIndexOf(File.separator));
        int i = 0;
        localObject = paramList.iterator();
        int j;
        for (;;)
        {
          j = i;
          if (!((Iterator)localObject).hasNext()) {
            break;
          }
          String str3 = (String)((Iterator)localObject).next();
          if (str2.equals(str3.substring(str3.lastIndexOf(File.separator))))
          {
            j = 1;
            break;
          }
        }
        if (j == 0) {
          localArrayList.add(str1);
        }
      }
    }
    localArrayList.addAll((Collection)localObject);
    try
    {
      createOrUpdateClassLoaderLocked(localArrayList);
      paramList = mResources;
      if (paramList != null) {
        try
        {
          paramList = getSplitPaths(null);
          mResources = ResourcesManager.getInstance().getResources(null, mResDir, paramList, mOverlayDirs, mApplicationInfo.sharedLibraryFiles, 0, null, getCompatibilityInfo(), getClassLoader());
        }
        catch (PackageManager.NameNotFoundException paramApplicationInfo)
        {
          paramApplicationInfo = new java/lang/AssertionError;
          paramApplicationInfo.<init>("null split not found");
          throw paramApplicationInfo;
        }
      }
      mAppComponentFactory = createAppFactory(paramApplicationInfo, mClassLoader);
      return;
    }
    finally {}
  }
  
  static final class ReceiverDispatcher
  {
    final Handler mActivityThread;
    final Context mContext;
    boolean mForgotten;
    final IIntentReceiver.Stub mIIntentReceiver;
    final Instrumentation mInstrumentation;
    final IntentReceiverLeaked mLocation;
    final BroadcastReceiver mReceiver;
    final boolean mRegistered;
    RuntimeException mUnregisterLocation;
    
    ReceiverDispatcher(BroadcastReceiver paramBroadcastReceiver, Context paramContext, Handler paramHandler, Instrumentation paramInstrumentation, boolean paramBoolean)
    {
      if (paramHandler != null)
      {
        mIIntentReceiver = new InnerReceiver(this, paramBoolean ^ true);
        mReceiver = paramBroadcastReceiver;
        mContext = paramContext;
        mActivityThread = paramHandler;
        mInstrumentation = paramInstrumentation;
        mRegistered = paramBoolean;
        mLocation = new IntentReceiverLeaked(null);
        mLocation.fillInStackTrace();
        return;
      }
      throw new NullPointerException("Handler must not be null");
    }
    
    IIntentReceiver getIIntentReceiver()
    {
      return mIIntentReceiver;
    }
    
    BroadcastReceiver getIntentReceiver()
    {
      return mReceiver;
    }
    
    IntentReceiverLeaked getLocation()
    {
      return mLocation;
    }
    
    RuntimeException getUnregisterLocation()
    {
      return mUnregisterLocation;
    }
    
    public void performReceive(Intent paramIntent, int paramInt1, String paramString, Bundle paramBundle, boolean paramBoolean1, boolean paramBoolean2, int paramInt2)
    {
      paramString = new Args(paramIntent, paramInt1, paramString, paramBundle, paramBoolean1, paramBoolean2, paramInt2);
      if (paramIntent == null) {
        Log.wtf("LoadedApk", "Null intent received");
      }
      if (((paramIntent == null) || (!mActivityThread.post(paramString.getRunnable()))) && (mRegistered) && (paramBoolean1)) {
        paramString.sendFinished(ActivityManager.getService());
      }
    }
    
    void setUnregisterLocation(RuntimeException paramRuntimeException)
    {
      mUnregisterLocation = paramRuntimeException;
    }
    
    void validate(Context paramContext, Handler paramHandler)
    {
      if (mContext == paramContext)
      {
        if (mActivityThread == paramHandler) {
          return;
        }
        paramContext = new StringBuilder();
        paramContext.append("Receiver ");
        paramContext.append(mReceiver);
        paramContext.append(" registered with differing handler (was ");
        paramContext.append(mActivityThread);
        paramContext.append(" now ");
        paramContext.append(paramHandler);
        paramContext.append(")");
        throw new IllegalStateException(paramContext.toString());
      }
      paramHandler = new StringBuilder();
      paramHandler.append("Receiver ");
      paramHandler.append(mReceiver);
      paramHandler.append(" registered with differing Context (was ");
      paramHandler.append(mContext);
      paramHandler.append(" now ");
      paramHandler.append(paramContext);
      paramHandler.append(")");
      throw new IllegalStateException(paramHandler.toString());
    }
    
    final class Args
      extends BroadcastReceiver.PendingResult
    {
      private Intent mCurIntent;
      private boolean mDispatched;
      private final boolean mOrdered;
      private Throwable mPreviousRunStacktrace;
      
      public Args(Intent paramIntent, int paramInt1, String paramString, Bundle paramBundle, boolean paramBoolean1, boolean paramBoolean2, int paramInt2)
      {
        super(paramString, paramBundle, i, paramBoolean1, paramBoolean2, mIIntentReceiver.asBinder(), paramInt2, paramIntent.getFlags());
        mCurIntent = paramIntent;
        mOrdered = paramBoolean1;
      }
      
      public final Runnable getRunnable()
      {
        return new _..Lambda.LoadedApk.ReceiverDispatcher.Args._BumDX2UKsnxLVrE6UJsJZkotuA(this);
      }
    }
    
    static final class InnerReceiver
      extends IIntentReceiver.Stub
    {
      final WeakReference<LoadedApk.ReceiverDispatcher> mDispatcher;
      final LoadedApk.ReceiverDispatcher mStrongRef;
      
      InnerReceiver(LoadedApk.ReceiverDispatcher paramReceiverDispatcher, boolean paramBoolean)
      {
        mDispatcher = new WeakReference(paramReceiverDispatcher);
        if (!paramBoolean) {
          paramReceiverDispatcher = null;
        }
        mStrongRef = paramReceiverDispatcher;
      }
      
      public void performReceive(Intent paramIntent, int paramInt1, String paramString, Bundle paramBundle, boolean paramBoolean1, boolean paramBoolean2, int paramInt2)
      {
        Object localObject;
        if (paramIntent == null)
        {
          Log.wtf("LoadedApk", "Null intent received");
          localObject = null;
        }
        else
        {
          localObject = (LoadedApk.ReceiverDispatcher)mDispatcher.get();
        }
        if (localObject != null)
        {
          ((LoadedApk.ReceiverDispatcher)localObject).performReceive(paramIntent, paramInt1, paramString, paramBundle, paramBoolean1, paramBoolean2, paramInt2);
        }
        else
        {
          localObject = ActivityManager.getService();
          if (paramBundle != null) {
            try
            {
              paramBundle.setAllowFds(false);
            }
            catch (RemoteException paramIntent)
            {
              break label96;
            }
          }
          ((IActivityManager)localObject).finishReceiver(this, paramInt1, paramString, paramBundle, false, paramIntent.getFlags());
        }
        return;
        label96:
        throw paramIntent.rethrowFromSystemServer();
      }
    }
  }
  
  static final class ServiceDispatcher
  {
    private final ArrayMap<ComponentName, ConnectionInfo> mActiveConnections = new ArrayMap();
    private final Handler mActivityThread;
    private final ServiceConnection mConnection;
    private final Context mContext;
    private final int mFlags;
    private boolean mForgotten;
    private final InnerConnection mIServiceConnection = new InnerConnection(this);
    private final ServiceConnectionLeaked mLocation;
    private RuntimeException mUnbindLocation;
    
    ServiceDispatcher(ServiceConnection paramServiceConnection, Context paramContext, Handler paramHandler, int paramInt)
    {
      mConnection = paramServiceConnection;
      mContext = paramContext;
      mActivityThread = paramHandler;
      mLocation = new ServiceConnectionLeaked(null);
      mLocation.fillInStackTrace();
      mFlags = paramInt;
    }
    
    public void connected(ComponentName paramComponentName, IBinder paramIBinder, boolean paramBoolean)
    {
      if (mActivityThread != null) {
        mActivityThread.post(new RunConnection(paramComponentName, paramIBinder, 0, paramBoolean));
      } else {
        doConnected(paramComponentName, paramIBinder, paramBoolean);
      }
    }
    
    public void death(ComponentName paramComponentName, IBinder paramIBinder)
    {
      if (mActivityThread != null) {
        mActivityThread.post(new RunConnection(paramComponentName, paramIBinder, 1, false));
      } else {
        doDeath(paramComponentName, paramIBinder);
      }
    }
    
    public void doConnected(ComponentName paramComponentName, IBinder paramIBinder, boolean paramBoolean)
    {
      try
      {
        if (mForgotten) {
          return;
        }
        ConnectionInfo localConnectionInfo1 = (ConnectionInfo)mActiveConnections.get(paramComponentName);
        if ((localConnectionInfo1 != null) && (binder == paramIBinder)) {
          return;
        }
        if (paramIBinder != null)
        {
          ConnectionInfo localConnectionInfo2 = new android/app/LoadedApk$ServiceDispatcher$ConnectionInfo;
          localConnectionInfo2.<init>(null);
          binder = paramIBinder;
          DeathMonitor localDeathMonitor = new android/app/LoadedApk$ServiceDispatcher$DeathMonitor;
          localDeathMonitor.<init>(this, paramComponentName, paramIBinder);
          deathMonitor = localDeathMonitor;
          try
          {
            paramIBinder.linkToDeath(deathMonitor, 0);
            mActiveConnections.put(paramComponentName, localConnectionInfo2);
          }
          catch (RemoteException paramIBinder)
          {
            mActiveConnections.remove(paramComponentName);
            return;
          }
        }
        else
        {
          mActiveConnections.remove(paramComponentName);
        }
        if (localConnectionInfo1 != null) {
          binder.unlinkToDeath(deathMonitor, 0);
        }
        if (localConnectionInfo1 != null) {
          mConnection.onServiceDisconnected(paramComponentName);
        }
        if (paramBoolean) {
          mConnection.onBindingDied(paramComponentName);
        }
        if (paramIBinder != null) {
          mConnection.onServiceConnected(paramComponentName, paramIBinder);
        } else {
          mConnection.onNullBinding(paramComponentName);
        }
        return;
      }
      finally {}
    }
    
    public void doDeath(ComponentName paramComponentName, IBinder paramIBinder)
    {
      try
      {
        ConnectionInfo localConnectionInfo = (ConnectionInfo)mActiveConnections.get(paramComponentName);
        if ((localConnectionInfo != null) && (binder == paramIBinder))
        {
          mActiveConnections.remove(paramComponentName);
          binder.unlinkToDeath(deathMonitor, 0);
          mConnection.onServiceDisconnected(paramComponentName);
          return;
        }
        return;
      }
      finally {}
    }
    
    void doForget()
    {
      int i = 0;
      try
      {
        while (i < mActiveConnections.size())
        {
          ConnectionInfo localConnectionInfo = (ConnectionInfo)mActiveConnections.valueAt(i);
          binder.unlinkToDeath(deathMonitor, 0);
          i++;
        }
        mActiveConnections.clear();
        mForgotten = true;
        return;
      }
      finally {}
    }
    
    int getFlags()
    {
      return mFlags;
    }
    
    IServiceConnection getIServiceConnection()
    {
      return mIServiceConnection;
    }
    
    ServiceConnectionLeaked getLocation()
    {
      return mLocation;
    }
    
    ServiceConnection getServiceConnection()
    {
      return mConnection;
    }
    
    RuntimeException getUnbindLocation()
    {
      return mUnbindLocation;
    }
    
    void setUnbindLocation(RuntimeException paramRuntimeException)
    {
      mUnbindLocation = paramRuntimeException;
    }
    
    void validate(Context paramContext, Handler paramHandler)
    {
      if (mContext == paramContext)
      {
        if (mActivityThread == paramHandler) {
          return;
        }
        paramContext = new StringBuilder();
        paramContext.append("ServiceConnection ");
        paramContext.append(mConnection);
        paramContext.append(" registered with differing handler (was ");
        paramContext.append(mActivityThread);
        paramContext.append(" now ");
        paramContext.append(paramHandler);
        paramContext.append(")");
        throw new RuntimeException(paramContext.toString());
      }
      paramHandler = new StringBuilder();
      paramHandler.append("ServiceConnection ");
      paramHandler.append(mConnection);
      paramHandler.append(" registered with differing Context (was ");
      paramHandler.append(mContext);
      paramHandler.append(" now ");
      paramHandler.append(paramContext);
      paramHandler.append(")");
      throw new RuntimeException(paramHandler.toString());
    }
    
    private static class ConnectionInfo
    {
      IBinder binder;
      IBinder.DeathRecipient deathMonitor;
      
      private ConnectionInfo() {}
    }
    
    private final class DeathMonitor
      implements IBinder.DeathRecipient
    {
      final ComponentName mName;
      final IBinder mService;
      
      DeathMonitor(ComponentName paramComponentName, IBinder paramIBinder)
      {
        mName = paramComponentName;
        mService = paramIBinder;
      }
      
      public void binderDied()
      {
        death(mName, mService);
      }
    }
    
    private static class InnerConnection
      extends IServiceConnection.Stub
    {
      final WeakReference<LoadedApk.ServiceDispatcher> mDispatcher;
      
      InnerConnection(LoadedApk.ServiceDispatcher paramServiceDispatcher)
      {
        mDispatcher = new WeakReference(paramServiceDispatcher);
      }
      
      public void connected(ComponentName paramComponentName, IBinder paramIBinder, boolean paramBoolean)
        throws RemoteException
      {
        LoadedApk.ServiceDispatcher localServiceDispatcher = (LoadedApk.ServiceDispatcher)mDispatcher.get();
        if (localServiceDispatcher != null) {
          localServiceDispatcher.connected(paramComponentName, paramIBinder, paramBoolean);
        }
      }
    }
    
    private final class RunConnection
      implements Runnable
    {
      final int mCommand;
      final boolean mDead;
      final ComponentName mName;
      final IBinder mService;
      
      RunConnection(ComponentName paramComponentName, IBinder paramIBinder, int paramInt, boolean paramBoolean)
      {
        mName = paramComponentName;
        mService = paramIBinder;
        mCommand = paramInt;
        mDead = paramBoolean;
      }
      
      public void run()
      {
        if (mCommand == 0) {
          doConnected(mName, mService, mDead);
        } else if (mCommand == 1) {
          doDeath(mName, mService);
        }
      }
    }
  }
  
  private class SplitDependencyLoaderImpl
    extends SplitDependencyLoader<PackageManager.NameNotFoundException>
  {
    private final ClassLoader[] mCachedClassLoaders = new ClassLoader[mSplitNames.length + 1];
    private final String[][] mCachedResourcePaths = new String[mSplitNames.length + 1][];
    
    SplitDependencyLoaderImpl()
    {
      super();
    }
    
    private int ensureSplitLoaded(String paramString)
      throws PackageManager.NameNotFoundException
    {
      int i = 0;
      if (paramString != null)
      {
        i = Arrays.binarySearch(mSplitNames, paramString);
        if (i >= 0)
        {
          i++;
        }
        else
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Split name '");
          localStringBuilder.append(paramString);
          localStringBuilder.append("' is not installed");
          throw new PackageManager.NameNotFoundException(localStringBuilder.toString());
        }
      }
      loadDependenciesForSplit(i);
      return i;
    }
    
    protected void constructSplit(int paramInt1, int[] paramArrayOfInt, int paramInt2)
      throws PackageManager.NameNotFoundException
    {
      ArrayList localArrayList = new ArrayList();
      int i = 0;
      if (paramInt1 == 0)
      {
        LoadedApk.this.createOrUpdateClassLoaderLocked(null);
        mCachedClassLoaders[0] = mClassLoader;
        paramInt2 = paramArrayOfInt.length;
        for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++)
        {
          i = paramArrayOfInt[paramInt1];
          localArrayList.add(mSplitResDirs[(i - 1)]);
        }
        mCachedResourcePaths[0] = ((String[])localArrayList.toArray(new String[localArrayList.size()]));
        return;
      }
      ClassLoader localClassLoader = mCachedClassLoaders[paramInt2];
      mCachedClassLoaders[paramInt1] = ApplicationLoaders.getDefault().getClassLoader(mSplitAppDirs[(paramInt1 - 1)], getTargetSdkVersion(), false, null, null, localClassLoader, mSplitClassLoaderNames[(paramInt1 - 1)]);
      Collections.addAll(localArrayList, mCachedResourcePaths[paramInt2]);
      localArrayList.add(mSplitResDirs[(paramInt1 - 1)]);
      int j = paramArrayOfInt.length;
      for (paramInt2 = i; paramInt2 < j; paramInt2++)
      {
        i = paramArrayOfInt[paramInt2];
        localArrayList.add(mSplitResDirs[(i - 1)]);
      }
      mCachedResourcePaths[paramInt1] = ((String[])localArrayList.toArray(new String[localArrayList.size()]));
    }
    
    ClassLoader getClassLoaderForSplit(String paramString)
      throws PackageManager.NameNotFoundException
    {
      return mCachedClassLoaders[ensureSplitLoaded(paramString)];
    }
    
    String[] getSplitPathsForSplit(String paramString)
      throws PackageManager.NameNotFoundException
    {
      return mCachedResourcePaths[ensureSplitLoaded(paramString)];
    }
    
    protected boolean isSplitCached(int paramInt)
    {
      boolean bool;
      if (mCachedClassLoaders[paramInt] != null) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
  }
  
  private static class WarningContextClassLoader
    extends ClassLoader
  {
    private static boolean warned = false;
    
    private WarningContextClassLoader() {}
    
    private void warn(String paramString)
    {
      if (warned) {
        return;
      }
      warned = true;
      Thread.currentThread().setContextClassLoader(getParent());
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("ClassLoader.");
      localStringBuilder.append(paramString);
      localStringBuilder.append(": The class loader returned by Thread.getContextClassLoader() may fail for processes that host multiple applications. You should explicitly specify a context class loader. For example: Thread.setContextClassLoader(getClass().getClassLoader());");
      Slog.w("ActivityThread", localStringBuilder.toString());
    }
    
    public void clearAssertionStatus()
    {
      warn("clearAssertionStatus");
      getParent().clearAssertionStatus();
    }
    
    public URL getResource(String paramString)
    {
      warn("getResource");
      return getParent().getResource(paramString);
    }
    
    public InputStream getResourceAsStream(String paramString)
    {
      warn("getResourceAsStream");
      return getParent().getResourceAsStream(paramString);
    }
    
    public Enumeration<URL> getResources(String paramString)
      throws IOException
    {
      warn("getResources");
      return getParent().getResources(paramString);
    }
    
    public Class<?> loadClass(String paramString)
      throws ClassNotFoundException
    {
      warn("loadClass");
      return getParent().loadClass(paramString);
    }
    
    public void setClassAssertionStatus(String paramString, boolean paramBoolean)
    {
      warn("setClassAssertionStatus");
      getParent().setClassAssertionStatus(paramString, paramBoolean);
    }
    
    public void setDefaultAssertionStatus(boolean paramBoolean)
    {
      warn("setDefaultAssertionStatus");
      getParent().setDefaultAssertionStatus(paramBoolean);
    }
    
    public void setPackageAssertionStatus(String paramString, boolean paramBoolean)
    {
      warn("setPackageAssertionStatus");
      getParent().setPackageAssertionStatus(paramString, paramBoolean);
    }
  }
}
