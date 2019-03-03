package android.app;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ChangedPackages;
import android.content.pm.ComponentInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.IDexModuleRegisterCallback;
import android.content.pm.IDexModuleRegisterCallback.Stub;
import android.content.pm.IOnPermissionsChangeListener;
import android.content.pm.IOnPermissionsChangeListener.Stub;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageManager;
import android.content.pm.IPackageMoveObserver.Stub;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.InstantAppInfo;
import android.content.pm.InstrumentationInfo;
import android.content.pm.IntentFilterVerificationInfo;
import android.content.pm.KeySet;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.DexModuleRegisterCallback;
import android.content.pm.PackageManager.MoveCallback;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageManager.OnPermissionsChangedListener;
import android.content.pm.ParceledListSlice;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.SharedLibraryInfo;
import android.content.pm.VerifierDeviceIdentity;
import android.content.pm.VersionedPackage;
import android.content.pm.dex.ArtManager;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.LayerDrawable;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.storage.StorageManager;
import android.os.storage.VolumeInfo;
import android.provider.Settings.Global;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructStat;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.IconDrawableFactory;
import android.util.LauncherIcons;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.SomeArgs;
import com.android.internal.util.Preconditions;
import com.android.internal.util.UserIcons;
import dalvik.system.VMRuntime;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import libcore.util.EmptyArray;

public class ApplicationPackageManager
  extends PackageManager
{
  @VisibleForTesting
  public static final int[] CORP_BADGE_LABEL_RES_ID = { 17040304, 17040305, 17040306 };
  private static final boolean DEBUG_ICONS = false;
  private static final int DEFAULT_EPHEMERAL_COOKIE_MAX_SIZE_BYTES = 16384;
  private static final String TAG = "ApplicationPackageManager";
  private static final int sDefaultFlags = 1024;
  private static ArrayMap<ResourceName, WeakReference<Drawable.ConstantState>> sIconCache = new ArrayMap();
  private static ArrayMap<ResourceName, WeakReference<CharSequence>> sStringCache = new ArrayMap();
  private static final Object sSync = new Object();
  @GuardedBy("mLock")
  private ArtManager mArtManager;
  volatile int mCachedSafeMode = -1;
  private final ContextImpl mContext;
  @GuardedBy("mDelegates")
  private final ArrayList<MoveCallbackDelegate> mDelegates = new ArrayList();
  private boolean mEnableTwinApps = false;
  @GuardedBy("mLock")
  private PackageInstaller mInstaller;
  private final Object mLock = new Object();
  private final IPackageManager mPM;
  private final Map<PackageManager.OnPermissionsChangedListener, IOnPermissionsChangeListener> mPermissionListeners = new ArrayMap();
  @GuardedBy("mLock")
  private String mPermissionsControllerPackageName;
  @GuardedBy("mLock")
  private UserManager mUserManager;
  
  protected ApplicationPackageManager(ContextImpl paramContextImpl, IPackageManager paramIPackageManager)
  {
    mContext = paramContextImpl;
    mPM = paramIPackageManager;
    mEnableTwinApps = hasSystemFeature("asus.software.twinapps");
  }
  
  static void configurationChanged()
  {
    synchronized (sSync)
    {
      sIconCache.clear();
      sStringCache.clear();
      return;
    }
  }
  
  private Drawable getBadgedDrawable(Drawable paramDrawable1, Drawable paramDrawable2, Rect paramRect, boolean paramBoolean)
  {
    int i = paramDrawable1.getIntrinsicWidth();
    int j = paramDrawable1.getIntrinsicHeight();
    int k;
    if ((paramBoolean) && ((paramDrawable1 instanceof BitmapDrawable)) && (((BitmapDrawable)paramDrawable1).getBitmap().isMutable())) {
      k = 1;
    } else {
      k = 0;
    }
    Bitmap localBitmap;
    if (k != 0) {
      localBitmap = ((BitmapDrawable)paramDrawable1).getBitmap();
    } else {
      localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.ARGB_8888);
    }
    Canvas localCanvas = new Canvas(localBitmap);
    if (k == 0)
    {
      paramDrawable1.setBounds(0, 0, i, j);
      paramDrawable1.draw(localCanvas);
    }
    if (paramRect != null)
    {
      if ((left >= 0) && (top >= 0) && (paramRect.width() <= i) && (paramRect.height() <= j))
      {
        paramDrawable2.setBounds(0, 0, paramRect.width(), paramRect.height());
        localCanvas.save();
        localCanvas.translate(left, top);
        paramDrawable2.draw(localCanvas);
        localCanvas.restore();
      }
      else
      {
        paramDrawable1 = new StringBuilder();
        paramDrawable1.append("Badge location ");
        paramDrawable1.append(paramRect);
        paramDrawable1.append(" not in badged drawable bounds ");
        paramDrawable1.append(new Rect(0, 0, i, j));
        throw new IllegalArgumentException(paramDrawable1.toString());
      }
    }
    else
    {
      paramDrawable2.setBounds(0, 0, i, j);
      paramDrawable2.draw(localCanvas);
    }
    if (k == 0)
    {
      paramDrawable2 = new BitmapDrawable(mContext.getResources(), localBitmap);
      if ((paramDrawable1 instanceof BitmapDrawable)) {
        paramDrawable2.setTargetDensity(((BitmapDrawable)paramDrawable1).getBitmap().getDensity());
      }
      return paramDrawable2;
    }
    return paramDrawable1;
  }
  
  private Drawable getCachedIcon(ResourceName paramResourceName)
  {
    synchronized (sSync)
    {
      Object localObject2 = (WeakReference)sIconCache.get(paramResourceName);
      if (localObject2 != null)
      {
        localObject2 = (Drawable.ConstantState)((WeakReference)localObject2).get();
        if (localObject2 != null)
        {
          paramResourceName = ((Drawable.ConstantState)localObject2).newDrawable();
          return paramResourceName;
        }
        sIconCache.remove(paramResourceName);
      }
      return null;
    }
  }
  
  private CharSequence getCachedString(ResourceName paramResourceName)
  {
    synchronized (sSync)
    {
      Object localObject2 = (WeakReference)sStringCache.get(paramResourceName);
      if (localObject2 != null)
      {
        localObject2 = (CharSequence)((WeakReference)localObject2).get();
        if (localObject2 != null) {
          return localObject2;
        }
        sStringCache.remove(paramResourceName);
      }
      return null;
    }
  }
  
  private Drawable getDrawableForDensity(int paramInt1, int paramInt2)
  {
    int i = paramInt2;
    if (paramInt2 <= 0) {
      i = mContext.getResources().getDisplayMetrics().densityDpi;
    }
    return Resources.getSystem().getDrawableForDensity(paramInt1, i);
  }
  
  private Intent getLaunchIntentForPackageAndCategory(String paramString1, String paramString2)
  {
    Intent localIntent = new Intent("android.intent.action.MAIN");
    localIntent.addCategory(paramString2);
    localIntent.setPackage(paramString1);
    paramString1 = queryIntentActivities(localIntent, 0);
    if ((paramString1 != null) && (paramString1.size() > 0))
    {
      paramString2 = new Intent(localIntent);
      paramString2.setFlags(268435456);
      paramString2.setClassName(get0activityInfo.packageName, get0activityInfo.name);
      return paramString2;
    }
    return null;
  }
  
  private Drawable getManagedProfileIconForDensity(UserHandle paramUserHandle, int paramInt1, int paramInt2)
  {
    if (isManagedProfile(paramUserHandle.getIdentifier())) {
      return getDrawableForDensity(paramInt1, paramInt2);
    }
    if ((mEnableTwinApps) && (isTwinApps(paramUserHandle.getIdentifier())))
    {
      paramInt1 = paramInt2;
      if (paramInt2 <= 0) {
        paramInt1 = mContext.getResources().getDisplayMetrics().densityDpi;
      }
      return Resources.getSystem().getDrawableForDensity(17301710, paramInt1);
    }
    return null;
  }
  
  private int getUserBadgeColor(UserHandle paramUserHandle)
  {
    return IconDrawableFactory.getUserBadgeColor(getUserManager(), paramUserHandle.getIdentifier());
  }
  
  static void handlePackageBroadcast(int paramInt, String[] paramArrayOfString, boolean paramBoolean)
  {
    int i = 0;
    if (paramInt == 1) {
      i = 1;
    }
    if ((paramArrayOfString != null) && (paramArrayOfString.length > 0))
    {
      paramInt = 0;
      int j = paramArrayOfString.length;
      int k = 0;
      while (k < j)
      {
        String str = paramArrayOfString[k];
        synchronized (sSync)
        {
          for (int m = sIconCache.size() - 1; m >= 0; m--) {
            if (sIconCachekeyAtpackageName.equals(str))
            {
              sIconCache.removeAt(m);
              paramInt = 1;
            }
          }
          for (m = sStringCache.size() - 1; m >= 0; m--) {
            if (sStringCachekeyAtpackageName.equals(str))
            {
              sStringCache.removeAt(m);
              paramInt = 1;
            }
          }
          k++;
        }
      }
      if ((paramInt != 0) || (paramBoolean)) {
        if (i != 0) {
          Runtime.getRuntime().gc();
        } else {
          ActivityThread.currentActivityThread().scheduleGcIdler();
        }
      }
    }
  }
  
  private int installExistingPackageAsUser(String paramString, int paramInt1, int paramInt2)
    throws PackageManager.NameNotFoundException
  {
    try
    {
      paramInt1 = mPM.installExistingPackageAsUser(paramString, paramInt2, 0, paramInt1);
      if (paramInt1 != -3) {
        return paramInt1;
      }
      PackageManager.NameNotFoundException localNameNotFoundException = new android/content/pm/PackageManager$NameNotFoundException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Package ");
      localStringBuilder.append(paramString);
      localStringBuilder.append(" doesn't exist");
      localNameNotFoundException.<init>(localStringBuilder.toString());
      throw localNameNotFoundException;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  private boolean isManagedProfile(int paramInt)
  {
    return getUserManager().isManagedProfile(paramInt);
  }
  
  private boolean isPackageCandidateVolume(ContextImpl paramContextImpl, ApplicationInfo paramApplicationInfo, VolumeInfo paramVolumeInfo, IPackageManager paramIPackageManager)
  {
    boolean bool1 = isForceAllowOnExternal(paramContextImpl);
    boolean bool2 = "private".equals(paramVolumeInfo.getId());
    boolean bool3 = true;
    boolean bool4 = true;
    if (bool2)
    {
      bool3 = bool4;
      if (!paramApplicationInfo.isSystemApp()) {
        if (isAllow3rdPartyOnInternal(paramContextImpl)) {
          bool3 = bool4;
        } else {
          bool3 = false;
        }
      }
      return bool3;
    }
    if (paramApplicationInfo.isSystemApp()) {
      return false;
    }
    if ((!bool1) && ((installLocation == 1) || (installLocation == -1))) {
      return false;
    }
    if (!paramVolumeInfo.isMountedWritable()) {
      return false;
    }
    if (paramVolumeInfo.isPrimaryPhysical()) {
      return paramApplicationInfo.isInternal();
    }
    try
    {
      bool4 = paramIPackageManager.isPackageDeviceAdminOnAnyUser(packageName);
      if (bool4) {
        return false;
      }
      if (paramVolumeInfo.getType() != 1) {
        bool3 = false;
      }
      return bool3;
    }
    catch (RemoteException paramContextImpl)
    {
      throw paramContextImpl.rethrowFromSystemServer();
    }
  }
  
  private static boolean isPrimaryStorageCandidateVolume(VolumeInfo paramVolumeInfo)
  {
    boolean bool1 = "private".equals(paramVolumeInfo.getId());
    boolean bool2 = true;
    if (bool1) {
      return true;
    }
    if (!paramVolumeInfo.isMountedWritable()) {
      return false;
    }
    if (paramVolumeInfo.getType() != 1) {
      bool2 = false;
    }
    return bool2;
  }
  
  private boolean isTwinApps(int paramInt)
  {
    return getUserManager().isTwinApps(paramInt);
  }
  
  private static ApplicationInfo maybeAdjustApplicationInfo(ApplicationInfo paramApplicationInfo)
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
        localObject1 = new ApplicationInfo(paramApplicationInfo);
        nativeLibraryDir = secondaryNativeLibraryDir;
        return localObject1;
      }
    }
    return paramApplicationInfo;
  }
  
  private void putCachedIcon(ResourceName paramResourceName, Drawable paramDrawable)
  {
    synchronized (sSync)
    {
      ArrayMap localArrayMap = sIconCache;
      WeakReference localWeakReference = new java/lang/ref/WeakReference;
      localWeakReference.<init>(paramDrawable.getConstantState());
      localArrayMap.put(paramResourceName, localWeakReference);
      return;
    }
  }
  
  private void putCachedString(ResourceName paramResourceName, CharSequence paramCharSequence)
  {
    synchronized (sSync)
    {
      ArrayMap localArrayMap = sStringCache;
      WeakReference localWeakReference = new java/lang/ref/WeakReference;
      localWeakReference.<init>(paramCharSequence);
      localArrayMap.put(paramResourceName, localWeakReference);
      return;
    }
  }
  
  public void addCrossProfileIntentFilter(IntentFilter paramIntentFilter, int paramInt1, int paramInt2, int paramInt3)
  {
    try
    {
      mPM.addCrossProfileIntentFilter(paramIntentFilter, mContext.getOpPackageName(), paramInt1, paramInt2, paramInt3);
      return;
    }
    catch (RemoteException paramIntentFilter)
    {
      throw paramIntentFilter.rethrowFromSystemServer();
    }
  }
  
  public void addOnPermissionsChangeListener(PackageManager.OnPermissionsChangedListener paramOnPermissionsChangedListener)
  {
    synchronized (mPermissionListeners)
    {
      if (mPermissionListeners.get(paramOnPermissionsChangedListener) != null) {
        return;
      }
      OnPermissionsChangeListenerDelegate localOnPermissionsChangeListenerDelegate = new android/app/ApplicationPackageManager$OnPermissionsChangeListenerDelegate;
      localOnPermissionsChangeListenerDelegate.<init>(this, paramOnPermissionsChangedListener, Looper.getMainLooper());
      try
      {
        mPM.addOnPermissionsChangeListener(localOnPermissionsChangeListenerDelegate);
        mPermissionListeners.put(paramOnPermissionsChangedListener, localOnPermissionsChangeListenerDelegate);
        return;
      }
      catch (RemoteException paramOnPermissionsChangedListener)
      {
        throw paramOnPermissionsChangedListener.rethrowFromSystemServer();
      }
    }
  }
  
  public void addPackageToPreferred(String paramString)
  {
    Log.w("ApplicationPackageManager", "addPackageToPreferred() is a no-op");
  }
  
  public boolean addPermission(PermissionInfo paramPermissionInfo)
  {
    try
    {
      boolean bool = mPM.addPermission(paramPermissionInfo);
      return bool;
    }
    catch (RemoteException paramPermissionInfo)
    {
      throw paramPermissionInfo.rethrowFromSystemServer();
    }
  }
  
  public boolean addPermissionAsync(PermissionInfo paramPermissionInfo)
  {
    try
    {
      boolean bool = mPM.addPermissionAsync(paramPermissionInfo);
      return bool;
    }
    catch (RemoteException paramPermissionInfo)
    {
      throw paramPermissionInfo.rethrowFromSystemServer();
    }
  }
  
  public void addPreferredActivity(IntentFilter paramIntentFilter, int paramInt, ComponentName[] paramArrayOfComponentName, ComponentName paramComponentName)
  {
    try
    {
      mPM.addPreferredActivity(paramIntentFilter, paramInt, paramArrayOfComponentName, paramComponentName, mContext.getUserId());
      return;
    }
    catch (RemoteException paramIntentFilter)
    {
      throw paramIntentFilter.rethrowFromSystemServer();
    }
  }
  
  public void addPreferredActivityAsUser(IntentFilter paramIntentFilter, int paramInt1, ComponentName[] paramArrayOfComponentName, ComponentName paramComponentName, int paramInt2)
  {
    try
    {
      mPM.addPreferredActivity(paramIntentFilter, paramInt1, paramArrayOfComponentName, paramComponentName, paramInt2);
      return;
    }
    catch (RemoteException paramIntentFilter)
    {
      throw paramIntentFilter.rethrowFromSystemServer();
    }
  }
  
  public boolean canRequestPackageInstalls()
  {
    try
    {
      boolean bool = mPM.canRequestPackageInstalls(mContext.getPackageName(), mContext.getUserId());
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowAsRuntimeException();
    }
  }
  
  public String[] canonicalToCurrentPackageNames(String[] paramArrayOfString)
  {
    try
    {
      paramArrayOfString = mPM.canonicalToCurrentPackageNames(paramArrayOfString);
      return paramArrayOfString;
    }
    catch (RemoteException paramArrayOfString)
    {
      throw paramArrayOfString.rethrowFromSystemServer();
    }
  }
  
  public int checkPermission(String paramString1, String paramString2)
  {
    try
    {
      int i = mPM.checkPermission(paramString1, paramString2, mContext.getUserId());
      return i;
    }
    catch (RemoteException paramString1)
    {
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  public int checkSignatures(int paramInt1, int paramInt2)
  {
    try
    {
      paramInt1 = mPM.checkUidSignatures(paramInt1, paramInt2);
      return paramInt1;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int checkSignatures(String paramString1, String paramString2)
  {
    try
    {
      int i = mPM.checkSignatures(paramString1, paramString2);
      return i;
    }
    catch (RemoteException paramString1)
    {
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  public void clearApplicationUserData(String paramString, IPackageDataObserver paramIPackageDataObserver)
  {
    try
    {
      mPM.clearApplicationUserData(paramString, paramIPackageDataObserver, mContext.getUserId());
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void clearCrossProfileIntentFilters(int paramInt)
  {
    try
    {
      mPM.clearCrossProfileIntentFilters(paramInt, mContext.getOpPackageName());
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void clearInstantAppCookie()
  {
    updateInstantAppCookie(null);
  }
  
  public void clearPackagePreferredActivities(String paramString)
  {
    try
    {
      mPM.clearPackagePreferredActivities(paramString);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public String[] currentToCanonicalPackageNames(String[] paramArrayOfString)
  {
    try
    {
      paramArrayOfString = mPM.currentToCanonicalPackageNames(paramArrayOfString);
      return paramArrayOfString;
    }
    catch (RemoteException paramArrayOfString)
    {
      throw paramArrayOfString.rethrowFromSystemServer();
    }
  }
  
  public void deleteApplicationCacheFiles(String paramString, IPackageDataObserver paramIPackageDataObserver)
  {
    try
    {
      mPM.deleteApplicationCacheFiles(paramString, paramIPackageDataObserver);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void deleteApplicationCacheFilesAsUser(String paramString, int paramInt, IPackageDataObserver paramIPackageDataObserver)
  {
    try
    {
      mPM.deleteApplicationCacheFilesAsUser(paramString, paramInt, paramIPackageDataObserver);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void deletePackage(String paramString, IPackageDeleteObserver paramIPackageDeleteObserver, int paramInt)
  {
    deletePackageAsUser(paramString, paramIPackageDeleteObserver, paramInt, mContext.getUserId());
  }
  
  public void deletePackageAsUser(String paramString, IPackageDeleteObserver paramIPackageDeleteObserver, int paramInt1, int paramInt2)
  {
    try
    {
      mPM.deletePackageAsUser(paramString, -1, paramIPackageDeleteObserver, paramInt2, paramInt1);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void executeForceUninstall()
  {
    try
    {
      mPM.executeForceUninstall();
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void extendVerificationTimeout(int paramInt1, int paramInt2, long paramLong)
  {
    try
    {
      mPM.extendVerificationTimeout(paramInt1, paramInt2, paramLong);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void flushPackageRestrictionsAsUser(int paramInt)
  {
    try
    {
      mPM.flushPackageRestrictionsAsUser(paramInt);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void freeStorage(String paramString, long paramLong, IntentSender paramIntentSender)
  {
    try
    {
      mPM.freeStorage(paramString, paramLong, 0, paramIntentSender);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void freeStorageAndNotify(String paramString, long paramLong, IPackageDataObserver paramIPackageDataObserver)
  {
    try
    {
      mPM.freeStorageAndNotify(paramString, paramLong, 0, paramIPackageDataObserver);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public Drawable getActivityBanner(ComponentName paramComponentName)
    throws PackageManager.NameNotFoundException
  {
    return getActivityInfo(paramComponentName, 1024).loadBanner(this);
  }
  
  public Drawable getActivityBanner(Intent paramIntent)
    throws PackageManager.NameNotFoundException
  {
    if (paramIntent.getComponent() != null) {
      return getActivityBanner(paramIntent.getComponent());
    }
    ResolveInfo localResolveInfo = resolveActivity(paramIntent, 65536);
    if (localResolveInfo != null) {
      return activityInfo.loadBanner(this);
    }
    throw new PackageManager.NameNotFoundException(paramIntent.toUri(0));
  }
  
  public Drawable getActivityIcon(ComponentName paramComponentName)
    throws PackageManager.NameNotFoundException
  {
    return getActivityInfo(paramComponentName, 1024).loadIcon(this);
  }
  
  public Drawable getActivityIcon(Intent paramIntent)
    throws PackageManager.NameNotFoundException
  {
    if (paramIntent.getComponent() != null) {
      return getActivityIcon(paramIntent.getComponent());
    }
    ResolveInfo localResolveInfo = resolveActivity(paramIntent, 65536);
    if (localResolveInfo != null) {
      return activityInfo.loadIcon(this);
    }
    throw new PackageManager.NameNotFoundException(paramIntent.toUri(0));
  }
  
  public ActivityInfo getActivityInfo(ComponentName paramComponentName, int paramInt)
    throws PackageManager.NameNotFoundException
  {
    try
    {
      ActivityInfo localActivityInfo = mPM.getActivityInfo(paramComponentName, paramInt, mContext.getUserId());
      if (localActivityInfo != null) {
        return localActivityInfo;
      }
      throw new PackageManager.NameNotFoundException(paramComponentName.toString());
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public Drawable getActivityLogo(ComponentName paramComponentName)
    throws PackageManager.NameNotFoundException
  {
    return getActivityInfo(paramComponentName, 1024).loadLogo(this);
  }
  
  public Drawable getActivityLogo(Intent paramIntent)
    throws PackageManager.NameNotFoundException
  {
    if (paramIntent.getComponent() != null) {
      return getActivityLogo(paramIntent.getComponent());
    }
    ResolveInfo localResolveInfo = resolveActivity(paramIntent, 65536);
    if (localResolveInfo != null) {
      return activityInfo.loadLogo(this);
    }
    throw new PackageManager.NameNotFoundException(paramIntent.toUri(0));
  }
  
  public List<IntentFilter> getAllIntentFilters(String paramString)
  {
    try
    {
      paramString = mPM.getAllIntentFilters(paramString);
      if (paramString == null) {
        return Collections.emptyList();
      }
      paramString = paramString.getList();
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public List<PermissionGroupInfo> getAllPermissionGroups(int paramInt)
  {
    try
    {
      Object localObject = mPM.getAllPermissionGroups(paramInt);
      if (localObject == null) {
        return Collections.emptyList();
      }
      localObject = ((ParceledListSlice)localObject).getList();
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public Drawable getApplicationBanner(ApplicationInfo paramApplicationInfo)
  {
    return paramApplicationInfo.loadBanner(this);
  }
  
  public Drawable getApplicationBanner(String paramString)
    throws PackageManager.NameNotFoundException
  {
    return getApplicationBanner(getApplicationInfo(paramString, 1024));
  }
  
  public int getApplicationEnabledSetting(String paramString)
  {
    try
    {
      int i = mPM.getApplicationEnabledSetting(paramString, mContext.getUserId());
      return i;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean getApplicationHiddenSettingAsUser(String paramString, UserHandle paramUserHandle)
  {
    try
    {
      boolean bool = mPM.getApplicationHiddenSettingAsUser(paramString, paramUserHandle.getIdentifier());
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public Drawable getApplicationIcon(ApplicationInfo paramApplicationInfo)
  {
    return paramApplicationInfo.loadIcon(this);
  }
  
  public Drawable getApplicationIcon(String paramString)
    throws PackageManager.NameNotFoundException
  {
    return getApplicationIcon(getApplicationInfo(paramString, 1024));
  }
  
  public ApplicationInfo getApplicationInfo(String paramString, int paramInt)
    throws PackageManager.NameNotFoundException
  {
    return getApplicationInfoAsUser(paramString, paramInt, mContext.getUserId());
  }
  
  public ApplicationInfo getApplicationInfoAsUser(String paramString, int paramInt1, int paramInt2)
    throws PackageManager.NameNotFoundException
  {
    try
    {
      ApplicationInfo localApplicationInfo = mPM.getApplicationInfo(paramString, paramInt1, paramInt2);
      if (localApplicationInfo != null)
      {
        paramString = maybeAdjustApplicationInfo(localApplicationInfo);
        return paramString;
      }
      throw new PackageManager.NameNotFoundException(paramString);
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public CharSequence getApplicationLabel(ApplicationInfo paramApplicationInfo)
  {
    return paramApplicationInfo.loadLabel(this);
  }
  
  public Drawable getApplicationLogo(ApplicationInfo paramApplicationInfo)
  {
    return paramApplicationInfo.loadLogo(this);
  }
  
  public Drawable getApplicationLogo(String paramString)
    throws PackageManager.NameNotFoundException
  {
    return getApplicationLogo(getApplicationInfo(paramString, 1024));
  }
  
  public ArtManager getArtManager()
  {
    synchronized (mLock)
    {
      ArtManager localArtManager1 = mArtManager;
      if (localArtManager1 == null) {
        try
        {
          localArtManager1 = new android/content/pm/dex/ArtManager;
          localArtManager1.<init>(mContext, mPM.getArtManager());
          mArtManager = localArtManager1;
        }
        catch (RemoteException localRemoteException)
        {
          throw localRemoteException.rethrowFromSystemServer();
        }
      }
      ArtManager localArtManager2 = mArtManager;
      return localArtManager2;
    }
  }
  
  public Intent getCarLaunchIntentForPackage(String paramString)
  {
    return getLaunchIntentForPackageAndCategory(paramString, "android.intent.category.CAR_LAUNCHER");
  }
  
  public ChangedPackages getChangedPackages(int paramInt)
  {
    try
    {
      ChangedPackages localChangedPackages = mPM.getChangedPackages(paramInt, mContext.getUserId());
      return localChangedPackages;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getComponentEnabledSetting(ComponentName paramComponentName)
  {
    try
    {
      int i = mPM.getComponentEnabledSetting(paramComponentName, mContext.getUserId());
      return i;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public Drawable getDefaultActivityIcon()
  {
    return Resources.getSystem().getDrawable(17301651);
  }
  
  public String getDefaultBrowserPackageNameAsUser(int paramInt)
  {
    try
    {
      String str = mPM.getDefaultBrowserPackageName(paramInt);
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public Drawable getDrawable(String paramString, int paramInt, ApplicationInfo paramApplicationInfo)
  {
    ResourceName localResourceName = new ResourceName(paramString, paramInt);
    Object localObject = getCachedIcon(localResourceName);
    if (localObject != null) {
      return localObject;
    }
    localObject = paramApplicationInfo;
    if (paramApplicationInfo == null) {
      try
      {
        localObject = getApplicationInfo(paramString, 1024);
      }
      catch (PackageManager.NameNotFoundException paramString)
      {
        return null;
      }
    }
    if (paramInt != 0) {
      try
      {
        paramApplicationInfo = getResourcesForApplication((ApplicationInfo)localObject).getDrawable(paramInt, null);
        if (paramApplicationInfo != null) {
          putCachedIcon(localResourceName, paramApplicationInfo);
        }
        return paramApplicationInfo;
      }
      catch (Exception paramApplicationInfo)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Failure retrieving icon 0x");
        ((StringBuilder)localObject).append(Integer.toHexString(paramInt));
        ((StringBuilder)localObject).append(" in package ");
        ((StringBuilder)localObject).append(paramString);
        Log.w("PackageManager", ((StringBuilder)localObject).toString(), paramApplicationInfo);
      }
      catch (Resources.NotFoundException paramApplicationInfo)
      {
        paramString = new StringBuilder();
        paramString.append("Failure retrieving resources for ");
        paramString.append(packageName);
        paramString.append(": ");
        paramString.append(paramApplicationInfo.getMessage());
        Log.w("PackageManager", paramString.toString());
      }
      catch (PackageManager.NameNotFoundException paramString)
      {
        paramString = new StringBuilder();
        paramString.append("Failure retrieving resources for ");
        paramString.append(packageName);
        Log.w("PackageManager", paramString.toString());
      }
    }
    return null;
  }
  
  public CharSequence getHarmfulAppWarning(String paramString)
  {
    try
    {
      paramString = mPM.getHarmfulAppWarning(paramString, mContext.getUserId());
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowAsRuntimeException();
    }
  }
  
  public ComponentName getHomeActivities(List<ResolveInfo> paramList)
  {
    try
    {
      paramList = mPM.getHomeActivities(paramList);
      return paramList;
    }
    catch (RemoteException paramList)
    {
      throw paramList.rethrowFromSystemServer();
    }
  }
  
  public int getInstallReason(String paramString, UserHandle paramUserHandle)
  {
    try
    {
      int i = mPM.getInstallReason(paramString, paramUserHandle.getIdentifier());
      return i;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public List<ApplicationInfo> getInstalledApplications(int paramInt)
  {
    return getInstalledApplicationsAsUser(paramInt, mContext.getUserId());
  }
  
  public List<ApplicationInfo> getInstalledApplicationsAsUser(int paramInt1, int paramInt2)
  {
    try
    {
      Object localObject = mPM.getInstalledApplications(paramInt1, paramInt2);
      if (localObject == null) {
        return Collections.emptyList();
      }
      localObject = ((ParceledListSlice)localObject).getList();
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<PackageInfo> getInstalledPackages(int paramInt)
  {
    return getInstalledPackagesAsUser(paramInt, mContext.getUserId());
  }
  
  public List<PackageInfo> getInstalledPackagesAsUser(int paramInt1, int paramInt2)
  {
    try
    {
      Object localObject = mPM.getInstalledPackages(paramInt1, paramInt2);
      if (localObject == null) {
        return Collections.emptyList();
      }
      localObject = ((ParceledListSlice)localObject).getList();
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public String getInstallerPackageName(String paramString)
  {
    try
    {
      paramString = mPM.getInstallerPackageName(paramString);
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public String getInstantAppAndroidId(String paramString, UserHandle paramUserHandle)
  {
    try
    {
      paramString = mPM.getInstantAppAndroidId(paramString, paramUserHandle.getIdentifier());
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowAsRuntimeException();
    }
  }
  
  public byte[] getInstantAppCookie()
  {
    try
    {
      byte[] arrayOfByte = mPM.getInstantAppCookie(mContext.getPackageName(), mContext.getUserId());
      if (arrayOfByte != null) {
        return arrayOfByte;
      }
      arrayOfByte = EmptyArray.BYTE;
      return arrayOfByte;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getInstantAppCookieMaxBytes()
  {
    return Settings.Global.getInt(mContext.getContentResolver(), "ephemeral_cookie_max_size_bytes", 16384);
  }
  
  public int getInstantAppCookieMaxSize()
  {
    return getInstantAppCookieMaxBytes();
  }
  
  public Drawable getInstantAppIcon(String paramString)
  {
    try
    {
      paramString = mPM.getInstantAppIcon(paramString, mContext.getUserId());
      if (paramString != null)
      {
        paramString = new BitmapDrawable(null, paramString);
        return paramString;
      }
      return null;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public ComponentName getInstantAppInstallerComponent()
  {
    try
    {
      ComponentName localComponentName = mPM.getInstantAppInstallerComponent();
      return localComponentName;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowAsRuntimeException();
    }
  }
  
  public ComponentName getInstantAppResolverSettingsComponent()
  {
    try
    {
      ComponentName localComponentName = mPM.getInstantAppResolverSettingsComponent();
      return localComponentName;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowAsRuntimeException();
    }
  }
  
  public List<InstantAppInfo> getInstantApps()
  {
    try
    {
      Object localObject = mPM.getInstantApps(mContext.getUserId());
      if (localObject != null) {
        return ((ParceledListSlice)localObject).getList();
      }
      localObject = Collections.emptyList();
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public InstrumentationInfo getInstrumentationInfo(ComponentName paramComponentName, int paramInt)
    throws PackageManager.NameNotFoundException
  {
    try
    {
      InstrumentationInfo localInstrumentationInfo = mPM.getInstrumentationInfo(paramComponentName, paramInt);
      if (localInstrumentationInfo != null) {
        return localInstrumentationInfo;
      }
      throw new PackageManager.NameNotFoundException(paramComponentName.toString());
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public List<IntentFilterVerificationInfo> getIntentFilterVerifications(String paramString)
  {
    try
    {
      paramString = mPM.getIntentFilterVerifications(paramString);
      if (paramString == null) {
        return Collections.emptyList();
      }
      paramString = paramString.getList();
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public int getIntentVerificationStatusAsUser(String paramString, int paramInt)
  {
    try
    {
      paramInt = mPM.getIntentVerificationStatus(paramString, paramInt);
      return paramInt;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public KeySet getKeySetByAlias(String paramString1, String paramString2)
  {
    Preconditions.checkNotNull(paramString1);
    Preconditions.checkNotNull(paramString2);
    try
    {
      paramString1 = mPM.getKeySetByAlias(paramString1, paramString2);
      return paramString1;
    }
    catch (RemoteException paramString1)
    {
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  public Intent getLaunchIntentForPackage(String paramString)
  {
    Intent localIntent = new Intent("android.intent.action.MAIN");
    localIntent.addCategory("android.intent.category.INFO");
    localIntent.setPackage(paramString);
    List localList1 = queryIntentActivities(localIntent, 0);
    List localList2;
    if (localList1 != null)
    {
      localList2 = localList1;
      if (localList1.size() > 0) {}
    }
    else
    {
      localIntent.removeCategory("android.intent.category.INFO");
      localIntent.addCategory("android.intent.category.LAUNCHER");
      localIntent.setPackage(paramString);
      localList2 = queryIntentActivities(localIntent, 0);
    }
    if ((localList2 != null) && (localList2.size() > 0))
    {
      paramString = new Intent(localIntent);
      paramString.setFlags(268435456);
      paramString.setClassName(get0activityInfo.packageName, get0activityInfo.name);
      return paramString;
    }
    return null;
  }
  
  public Intent getLeanbackLaunchIntentForPackage(String paramString)
  {
    return getLaunchIntentForPackageAndCategory(paramString, "android.intent.category.LEANBACK_LAUNCHER");
  }
  
  public int getMoveStatus(int paramInt)
  {
    try
    {
      paramInt = mPM.getMoveStatus(paramInt);
      return paramInt;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public String getNameForUid(int paramInt)
  {
    try
    {
      String str = mPM.getNameForUid(paramInt);
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public String[] getNamesForUids(int[] paramArrayOfInt)
  {
    try
    {
      paramArrayOfInt = mPM.getNamesForUids(paramArrayOfInt);
      return paramArrayOfInt;
    }
    catch (RemoteException paramArrayOfInt)
    {
      throw paramArrayOfInt.rethrowFromSystemServer();
    }
  }
  
  public List<VolumeInfo> getPackageCandidateVolumes(ApplicationInfo paramApplicationInfo)
  {
    return getPackageCandidateVolumes(paramApplicationInfo, (StorageManager)mContext.getSystemService(StorageManager.class), mPM);
  }
  
  @VisibleForTesting
  protected List<VolumeInfo> getPackageCandidateVolumes(ApplicationInfo paramApplicationInfo, StorageManager paramStorageManager, IPackageManager paramIPackageManager)
  {
    VolumeInfo localVolumeInfo1 = getPackageCurrentVolume(paramApplicationInfo, paramStorageManager);
    Object localObject = paramStorageManager.getVolumes();
    paramStorageManager = new ArrayList();
    localObject = ((List)localObject).iterator();
    while (((Iterator)localObject).hasNext())
    {
      VolumeInfo localVolumeInfo2 = (VolumeInfo)((Iterator)localObject).next();
      if ((Objects.equals(localVolumeInfo2, localVolumeInfo1)) || (isPackageCandidateVolume(mContext, paramApplicationInfo, localVolumeInfo2, paramIPackageManager))) {
        if ((!"private".equals(localVolumeInfo2.getId())) || (!isInApp2sdBlacklist(packageName)) || ((flags & 0x40000) != 0)) {
          paramStorageManager.add(localVolumeInfo2);
        }
      }
    }
    return paramStorageManager;
  }
  
  public VolumeInfo getPackageCurrentVolume(ApplicationInfo paramApplicationInfo)
  {
    return getPackageCurrentVolume(paramApplicationInfo, (StorageManager)mContext.getSystemService(StorageManager.class));
  }
  
  @VisibleForTesting
  protected VolumeInfo getPackageCurrentVolume(ApplicationInfo paramApplicationInfo, StorageManager paramStorageManager)
  {
    if (paramApplicationInfo.isInternal()) {
      return paramStorageManager.findVolumeById("private");
    }
    if (paramApplicationInfo.isExternalAsec()) {
      return paramStorageManager.getPrimaryPhysicalVolume();
    }
    return paramStorageManager.findVolumeByUuid(volumeUuid);
  }
  
  public int[] getPackageGids(String paramString)
    throws PackageManager.NameNotFoundException
  {
    return getPackageGids(paramString, 0);
  }
  
  public int[] getPackageGids(String paramString, int paramInt)
    throws PackageManager.NameNotFoundException
  {
    try
    {
      int[] arrayOfInt = mPM.getPackageGids(paramString, paramInt, mContext.getUserId());
      if (arrayOfInt != null) {
        return arrayOfInt;
      }
      throw new PackageManager.NameNotFoundException(paramString);
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public PackageInfo getPackageInfo(VersionedPackage paramVersionedPackage, int paramInt)
    throws PackageManager.NameNotFoundException
  {
    try
    {
      PackageInfo localPackageInfo = mPM.getPackageInfoVersioned(paramVersionedPackage, paramInt, mContext.getUserId());
      if (localPackageInfo != null) {
        return localPackageInfo;
      }
      throw new PackageManager.NameNotFoundException(paramVersionedPackage.toString());
    }
    catch (RemoteException paramVersionedPackage)
    {
      throw paramVersionedPackage.rethrowFromSystemServer();
    }
  }
  
  public PackageInfo getPackageInfo(String paramString, int paramInt)
    throws PackageManager.NameNotFoundException
  {
    return getPackageInfoAsUser(paramString, paramInt, mContext.getUserId());
  }
  
  public PackageInfo getPackageInfoAsUser(String paramString, int paramInt1, int paramInt2)
    throws PackageManager.NameNotFoundException
  {
    try
    {
      PackageInfo localPackageInfo = mPM.getPackageInfo(paramString, paramInt1, paramInt2);
      if (localPackageInfo != null) {
        return localPackageInfo;
      }
      throw new PackageManager.NameNotFoundException(paramString);
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public PackageInstaller getPackageInstaller()
  {
    synchronized (mLock)
    {
      PackageInstaller localPackageInstaller1 = mInstaller;
      if (localPackageInstaller1 == null) {
        try
        {
          localPackageInstaller1 = new android/content/pm/PackageInstaller;
          localPackageInstaller1.<init>(mPM.getPackageInstaller(), mContext.getPackageName(), mContext.getUserId());
          mInstaller = localPackageInstaller1;
        }
        catch (RemoteException localRemoteException)
        {
          throw localRemoteException.rethrowFromSystemServer();
        }
      }
      PackageInstaller localPackageInstaller2 = mInstaller;
      return localPackageInstaller2;
    }
  }
  
  public void getPackageSizeInfoAsUser(String paramString, int paramInt, IPackageStatsObserver paramIPackageStatsObserver)
  {
    if (mContext.getApplicationInfo().targetSdkVersion < 26)
    {
      if (paramIPackageStatsObserver != null)
      {
        Log.d("ApplicationPackageManager", "Shame on you for calling the hidden API getPackageSizeInfoAsUser(). Shame!");
        try
        {
          paramIPackageStatsObserver.onGetStatsCompleted(null, false);
        }
        catch (RemoteException paramString) {}
      }
      return;
    }
    throw new UnsupportedOperationException("Shame on you for calling the hidden API getPackageSizeInfoAsUser(). Shame!");
  }
  
  public int getPackageUid(String paramString, int paramInt)
    throws PackageManager.NameNotFoundException
  {
    return getPackageUidAsUser(paramString, paramInt, mContext.getUserId());
  }
  
  public int getPackageUidAsUser(String paramString, int paramInt)
    throws PackageManager.NameNotFoundException
  {
    return getPackageUidAsUser(paramString, 0, paramInt);
  }
  
  public int getPackageUidAsUser(String paramString, int paramInt1, int paramInt2)
    throws PackageManager.NameNotFoundException
  {
    try
    {
      paramInt1 = mPM.getPackageUid(paramString, paramInt1, paramInt2);
      if (paramInt1 >= 0) {
        return paramInt1;
      }
      throw new PackageManager.NameNotFoundException(paramString);
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public String[] getPackagesForUid(int paramInt)
  {
    try
    {
      String[] arrayOfString = mPM.getPackagesForUid(paramInt);
      return arrayOfString;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<PackageInfo> getPackagesHoldingPermissions(String[] paramArrayOfString, int paramInt)
  {
    int i = mContext.getUserId();
    try
    {
      paramArrayOfString = mPM.getPackagesHoldingPermissions(paramArrayOfString, paramInt, i);
      if (paramArrayOfString == null) {
        return Collections.emptyList();
      }
      paramArrayOfString = paramArrayOfString.getList();
      return paramArrayOfString;
    }
    catch (RemoteException paramArrayOfString)
    {
      throw paramArrayOfString.rethrowFromSystemServer();
    }
  }
  
  public String getPermissionControllerPackageName()
  {
    synchronized (mLock)
    {
      String str1 = mPermissionsControllerPackageName;
      if (str1 == null) {
        try
        {
          mPermissionsControllerPackageName = mPM.getPermissionControllerPackageName();
        }
        catch (RemoteException localRemoteException)
        {
          throw localRemoteException.rethrowFromSystemServer();
        }
      }
      String str2 = mPermissionsControllerPackageName;
      return str2;
    }
  }
  
  public int getPermissionFlags(String paramString1, String paramString2, UserHandle paramUserHandle)
  {
    try
    {
      int i = mPM.getPermissionFlags(paramString1, paramString2, paramUserHandle.getIdentifier());
      return i;
    }
    catch (RemoteException paramString1)
    {
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  public PermissionGroupInfo getPermissionGroupInfo(String paramString, int paramInt)
    throws PackageManager.NameNotFoundException
  {
    try
    {
      PermissionGroupInfo localPermissionGroupInfo = mPM.getPermissionGroupInfo(paramString, paramInt);
      if (localPermissionGroupInfo != null) {
        return localPermissionGroupInfo;
      }
      throw new PackageManager.NameNotFoundException(paramString);
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public PermissionInfo getPermissionInfo(String paramString, int paramInt)
    throws PackageManager.NameNotFoundException
  {
    try
    {
      PermissionInfo localPermissionInfo = mPM.getPermissionInfo(paramString, mContext.getOpPackageName(), paramInt);
      if (localPermissionInfo != null) {
        return localPermissionInfo;
      }
      throw new PackageManager.NameNotFoundException(paramString);
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public int getPreferredActivities(List<IntentFilter> paramList, List<ComponentName> paramList1, String paramString)
  {
    try
    {
      int i = mPM.getPreferredActivities(paramList, paramList1, paramString);
      return i;
    }
    catch (RemoteException paramList)
    {
      throw paramList.rethrowFromSystemServer();
    }
  }
  
  public List<PackageInfo> getPreferredPackages(int paramInt)
  {
    Log.w("ApplicationPackageManager", "getPreferredPackages() is a no-op");
    return Collections.emptyList();
  }
  
  public List<VolumeInfo> getPrimaryStorageCandidateVolumes()
  {
    Object localObject1 = (StorageManager)mContext.getSystemService(StorageManager.class);
    VolumeInfo localVolumeInfo = getPrimaryStorageCurrentVolume();
    Object localObject2 = ((StorageManager)localObject1).getVolumes();
    ArrayList localArrayList = new ArrayList();
    if ((Objects.equals("primary_physical", ((StorageManager)localObject1).getPrimaryStorageUuid())) && (localVolumeInfo != null))
    {
      localArrayList.add(localVolumeInfo);
    }
    else
    {
      localObject2 = ((List)localObject2).iterator();
      while (((Iterator)localObject2).hasNext())
      {
        localObject1 = (VolumeInfo)((Iterator)localObject2).next();
        if ((Objects.equals(localObject1, localVolumeInfo)) || (isPrimaryStorageCandidateVolume((VolumeInfo)localObject1))) {
          localArrayList.add(localObject1);
        }
      }
    }
    return localArrayList;
  }
  
  public VolumeInfo getPrimaryStorageCurrentVolume()
  {
    StorageManager localStorageManager = (StorageManager)mContext.getSystemService(StorageManager.class);
    return localStorageManager.findVolumeByQualifiedUuid(localStorageManager.getPrimaryStorageUuid());
  }
  
  public ProviderInfo getProviderInfo(ComponentName paramComponentName, int paramInt)
    throws PackageManager.NameNotFoundException
  {
    try
    {
      ProviderInfo localProviderInfo = mPM.getProviderInfo(paramComponentName, paramInt, mContext.getUserId());
      if (localProviderInfo != null) {
        return localProviderInfo;
      }
      throw new PackageManager.NameNotFoundException(paramComponentName.toString());
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public ActivityInfo getReceiverInfo(ComponentName paramComponentName, int paramInt)
    throws PackageManager.NameNotFoundException
  {
    try
    {
      ActivityInfo localActivityInfo = mPM.getReceiverInfo(paramComponentName, paramInt, mContext.getUserId());
      if (localActivityInfo != null) {
        return localActivityInfo;
      }
      throw new PackageManager.NameNotFoundException(paramComponentName.toString());
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public Resources getResourcesForActivity(ComponentName paramComponentName)
    throws PackageManager.NameNotFoundException
  {
    return getResourcesForApplication(getActivityInfo1024applicationInfo);
  }
  
  public Resources getResourcesForApplication(ApplicationInfo paramApplicationInfo)
    throws PackageManager.NameNotFoundException
  {
    if (packageName.equals("system")) {
      return mContext.mMainThread.getSystemUiContext().getResources();
    }
    int i;
    if (uid == Process.myUid()) {
      i = 1;
    } else {
      i = 0;
    }
    ActivityThread localActivityThread = mContext.mMainThread;
    if (i != 0) {}
    for (Object localObject = sourceDir;; localObject = publicSourceDir) {
      break;
    }
    if (i != 0) {}
    for (String[] arrayOfString = splitSourceDirs;; arrayOfString = splitPublicSourceDirs) {
      break;
    }
    localObject = localActivityThread.getTopLevelResources((String)localObject, arrayOfString, resourceDirs, sharedLibraryFiles, 0, mContext.mPackageInfo);
    if (localObject != null) {
      return localObject;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Unable to open ");
    ((StringBuilder)localObject).append(publicSourceDir);
    throw new PackageManager.NameNotFoundException(((StringBuilder)localObject).toString());
  }
  
  public Resources getResourcesForApplication(String paramString)
    throws PackageManager.NameNotFoundException
  {
    return getResourcesForApplication(getApplicationInfo(paramString, 1024));
  }
  
  public Resources getResourcesForApplicationAsUser(String paramString, int paramInt)
    throws PackageManager.NameNotFoundException
  {
    if (paramInt >= 0)
    {
      if ("system".equals(paramString)) {
        return mContext.mMainThread.getSystemUiContext().getResources();
      }
      try
      {
        Object localObject = mPM.getApplicationInfo(paramString, 1024, paramInt);
        if (localObject != null)
        {
          paramString = getResourcesForApplication((ApplicationInfo)localObject);
          return paramString;
        }
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Package ");
        ((StringBuilder)localObject).append(paramString);
        ((StringBuilder)localObject).append(" doesn't exist");
        throw new PackageManager.NameNotFoundException(((StringBuilder)localObject).toString());
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
    }
    paramString = new StringBuilder();
    paramString.append("Call does not support special user #");
    paramString.append(paramInt);
    throw new IllegalArgumentException(paramString.toString());
  }
  
  public ServiceInfo getServiceInfo(ComponentName paramComponentName, int paramInt)
    throws PackageManager.NameNotFoundException
  {
    try
    {
      ServiceInfo localServiceInfo = mPM.getServiceInfo(paramComponentName, paramInt, mContext.getUserId());
      if (localServiceInfo != null) {
        return localServiceInfo;
      }
      throw new PackageManager.NameNotFoundException(paramComponentName.toString());
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public String getServicesSystemSharedLibraryPackageName()
  {
    try
    {
      String str = mPM.getServicesSystemSharedLibraryPackageName();
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<SharedLibraryInfo> getSharedLibraries(int paramInt)
  {
    return getSharedLibrariesAsUser(paramInt, mContext.getUserId());
  }
  
  public List<SharedLibraryInfo> getSharedLibrariesAsUser(int paramInt1, int paramInt2)
  {
    try
    {
      Object localObject = mPM.getSharedLibraries(mContext.getOpPackageName(), paramInt1, paramInt2);
      if (localObject == null) {
        return Collections.emptyList();
      }
      localObject = ((ParceledListSlice)localObject).getList();
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public String getSharedSystemSharedLibraryPackageName()
  {
    try
    {
      String str = mPM.getSharedSystemSharedLibraryPackageName();
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public KeySet getSigningKeySet(String paramString)
  {
    Preconditions.checkNotNull(paramString);
    try
    {
      paramString = mPM.getSigningKeySet(paramString);
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public Bundle getSuspendedPackageAppExtras()
  {
    try
    {
      Object localObject = mPM.getSuspendedPackageAppExtras(mContext.getOpPackageName(), mContext.getUserId());
      if (localObject != null) {
        localObject = new Bundle(((PersistableBundle)localObject).deepCopy());
      } else {
        localObject = null;
      }
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public FeatureInfo[] getSystemAvailableFeatures()
  {
    try
    {
      Object localObject = mPM.getSystemAvailableFeatures();
      int i = 0;
      if (localObject == null) {
        return new FeatureInfo[0];
      }
      List localList = ((ParceledListSlice)localObject).getList();
      localObject = new FeatureInfo[localList.size()];
      while (i < localObject.length)
      {
        localObject[i] = ((FeatureInfo)localList.get(i));
        i++;
      }
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public String[] getSystemSharedLibraryNames()
  {
    try
    {
      String[] arrayOfString = mPM.getSystemSharedLibraryNames();
      return arrayOfString;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public String getSystemTextClassifierPackageName()
  {
    try
    {
      String str = mPM.getSystemTextClassifierPackageName();
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowAsRuntimeException();
    }
  }
  
  public CharSequence getText(String paramString, int paramInt, ApplicationInfo paramApplicationInfo)
  {
    ResourceName localResourceName = new ResourceName(paramString, paramInt);
    Object localObject = getCachedString(localResourceName);
    if (localObject != null) {
      return localObject;
    }
    localObject = paramApplicationInfo;
    if (paramApplicationInfo == null) {
      try
      {
        localObject = getApplicationInfo(paramString, 1024);
      }
      catch (PackageManager.NameNotFoundException paramString)
      {
        return null;
      }
    }
    try
    {
      paramApplicationInfo = getResourcesForApplication((ApplicationInfo)localObject).getText(paramInt);
      putCachedString(localResourceName, paramApplicationInfo);
      return paramApplicationInfo;
    }
    catch (RuntimeException localRuntimeException)
    {
      paramApplicationInfo = new StringBuilder();
      paramApplicationInfo.append("Failure retrieving text 0x");
      paramApplicationInfo.append(Integer.toHexString(paramInt));
      paramApplicationInfo.append(" in package ");
      paramApplicationInfo.append(paramString);
      Log.w("PackageManager", paramApplicationInfo.toString(), localRuntimeException);
    }
    catch (PackageManager.NameNotFoundException paramString)
    {
      paramString = new StringBuilder();
      paramString.append("Failure retrieving resources for ");
      paramString.append(packageName);
      Log.w("PackageManager", paramString.toString());
    }
    return null;
  }
  
  public int getUidForSharedUser(String paramString)
    throws PackageManager.NameNotFoundException
  {
    try
    {
      int i = mPM.getUidForSharedUser(paramString);
      if (i != -1) {
        return i;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("No shared userid for user:");
      localStringBuilder.append(paramString);
      throw new PackageManager.NameNotFoundException(localStringBuilder.toString());
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public Drawable getUserBadgeForDensity(UserHandle paramUserHandle, int paramInt)
  {
    Drawable localDrawable1 = getManagedProfileIconForDensity(paramUserHandle, 17302597, paramInt);
    if (localDrawable1 == null) {
      return null;
    }
    Drawable localDrawable2 = getDrawableForDensity(17302596, paramInt);
    localDrawable2.setTint(getUserBadgeColor(paramUserHandle));
    return new LayerDrawable(new Drawable[] { localDrawable1, localDrawable2 });
  }
  
  public Drawable getUserBadgeForDensityNoBackground(UserHandle paramUserHandle, int paramInt)
  {
    Drawable localDrawable1 = getManagedProfileIconForDensity(paramUserHandle, 17302598, paramInt);
    if (localDrawable1 != null) {
      localDrawable1.setTint(getUserBadgeColor(paramUserHandle));
    }
    Drawable localDrawable2 = localDrawable1;
    if (mEnableTwinApps)
    {
      localDrawable2 = localDrawable1;
      if (isTwinApps(paramUserHandle.getIdentifier())) {
        localDrawable2 = getDrawableForDensity(17301710, paramInt);
      }
    }
    return localDrawable2;
  }
  
  public Drawable getUserBadgedDrawableForDensity(Drawable paramDrawable, UserHandle paramUserHandle, Rect paramRect, int paramInt)
  {
    paramUserHandle = getUserBadgeForDensity(paramUserHandle, paramInt);
    if (paramUserHandle == null) {
      return paramDrawable;
    }
    return getBadgedDrawable(paramDrawable, paramUserHandle, paramRect, true);
  }
  
  public Drawable getUserBadgedIcon(Drawable paramDrawable, UserHandle paramUserHandle)
  {
    if (!isManagedProfile(paramUserHandle.getIdentifier()))
    {
      if ((mEnableTwinApps) && (isTwinApps(paramUserHandle.getIdentifier())) && (!getNameForUid(Binder.getCallingUid()).equals("com.asus.launcher3"))) {
        return getBadgedDrawable(paramDrawable, new LauncherIcons(mContext).getBadgedDrawable(paramDrawable, 17302604, 0, 0), null, true);
      }
      return paramDrawable;
    }
    return getBadgedDrawable(paramDrawable, new LauncherIcons(mContext).getBadgeDrawable(17302601, getUserBadgeColor(paramUserHandle)), null, true);
  }
  
  public CharSequence getUserBadgedLabel(CharSequence paramCharSequence, UserHandle paramUserHandle)
  {
    if (isManagedProfile(paramUserHandle.getIdentifier()))
    {
      int i = getUserManager().getManagedProfileBadge(paramUserHandle.getIdentifier());
      i = CORP_BADGE_LABEL_RES_ID[(i % CORP_BADGE_LABEL_RES_ID.length)];
      return Resources.getSystem().getString(i, new Object[] { paramCharSequence });
    }
    return paramCharSequence;
  }
  
  public int getUserId()
  {
    return mContext.getUserId();
  }
  
  UserManager getUserManager()
  {
    synchronized (mLock)
    {
      if (mUserManager == null) {
        mUserManager = UserManager.get(mContext);
      }
      UserManager localUserManager = mUserManager;
      return localUserManager;
    }
  }
  
  public VerifierDeviceIdentity getVerifierDeviceIdentity()
  {
    try
    {
      VerifierDeviceIdentity localVerifierDeviceIdentity = mPM.getVerifierDeviceIdentity();
      return localVerifierDeviceIdentity;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public XmlResourceParser getXml(String paramString, int paramInt, ApplicationInfo paramApplicationInfo)
  {
    ApplicationInfo localApplicationInfo = paramApplicationInfo;
    if (paramApplicationInfo == null) {
      try
      {
        localApplicationInfo = getApplicationInfo(paramString, 1024);
      }
      catch (PackageManager.NameNotFoundException paramString)
      {
        return null;
      }
    }
    try
    {
      paramApplicationInfo = getResourcesForApplication(localApplicationInfo).getXml(paramInt);
      return paramApplicationInfo;
    }
    catch (PackageManager.NameNotFoundException paramString)
    {
      paramString = new StringBuilder();
      paramString.append("Failure retrieving resources for ");
      paramString.append(packageName);
      Log.w("PackageManager", paramString.toString());
    }
    catch (RuntimeException localRuntimeException)
    {
      paramApplicationInfo = new StringBuilder();
      paramApplicationInfo.append("Failure retrieving xml 0x");
      paramApplicationInfo.append(Integer.toHexString(paramInt));
      paramApplicationInfo.append(" in package ");
      paramApplicationInfo.append(paramString);
      Log.w("PackageManager", paramApplicationInfo.toString(), localRuntimeException);
    }
    return null;
  }
  
  public void grantRuntimePermission(String paramString1, String paramString2, UserHandle paramUserHandle)
  {
    try
    {
      mPM.grantRuntimePermission(paramString1, paramString2, paramUserHandle.getIdentifier());
      return;
    }
    catch (RemoteException paramString1)
    {
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  public boolean hasSigningCertificate(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    try
    {
      boolean bool = mPM.hasUidSigningCertificate(paramInt1, paramArrayOfByte, paramInt2);
      return bool;
    }
    catch (RemoteException paramArrayOfByte)
    {
      throw paramArrayOfByte.rethrowFromSystemServer();
    }
  }
  
  public boolean hasSigningCertificate(String paramString, byte[] paramArrayOfByte, int paramInt)
  {
    try
    {
      boolean bool = mPM.hasSigningCertificate(paramString, paramArrayOfByte, paramInt);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean hasSystemFeature(String paramString)
  {
    return hasSystemFeature(paramString, 0);
  }
  
  public boolean hasSystemFeature(String paramString, int paramInt)
  {
    try
    {
      boolean bool = mPM.hasSystemFeature(paramString, paramInt);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public int installExistingPackage(String paramString)
    throws PackageManager.NameNotFoundException
  {
    return installExistingPackage(paramString, 0);
  }
  
  public int installExistingPackage(String paramString, int paramInt)
    throws PackageManager.NameNotFoundException
  {
    return installExistingPackageAsUser(paramString, paramInt, mContext.getUserId());
  }
  
  public int installExistingPackageAsUser(String paramString, int paramInt)
    throws PackageManager.NameNotFoundException
  {
    return installExistingPackageAsUser(paramString, 0, paramInt);
  }
  
  @VisibleForTesting
  protected boolean isAllow3rdPartyOnInternal(Context paramContext)
  {
    return paramContext.getResources().getBoolean(17956870);
  }
  
  @VisibleForTesting
  protected boolean isForceAllowOnExternal(Context paramContext)
  {
    paramContext = paramContext.getContentResolver();
    boolean bool = false;
    if (Settings.Global.getInt(paramContext, "force_allow_on_external", 0) != 0) {
      bool = true;
    }
    return bool;
  }
  
  public boolean isInApp2sdBlacklist(String paramString)
  {
    try
    {
      boolean bool = mPM.isInApp2sdBlacklist(paramString);
      return bool;
    }
    catch (RemoteException paramString) {}
    return false;
  }
  
  public boolean isInstantApp()
  {
    return isInstantApp(mContext.getPackageName());
  }
  
  public boolean isInstantApp(String paramString)
  {
    try
    {
      boolean bool = mPM.isInstantApp(paramString, mContext.getUserId());
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean isNeedProtectSecPreloadedApps()
  {
    try
    {
      boolean bool = mPM.isNeedProtectSecPreloadedApps();
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean isPackageAvailable(String paramString)
  {
    try
    {
      boolean bool = mPM.isPackageAvailable(paramString, mContext.getUserId());
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean isPackageStateProtected(String paramString, int paramInt)
  {
    try
    {
      boolean bool = mPM.isPackageStateProtected(paramString, paramInt);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowAsRuntimeException();
    }
  }
  
  public boolean isPackageSuspended()
  {
    return isPackageSuspendedForUser(mContext.getOpPackageName(), mContext.getUserId());
  }
  
  public boolean isPackageSuspended(String paramString)
    throws PackageManager.NameNotFoundException
  {
    try
    {
      boolean bool = isPackageSuspendedForUser(paramString, mContext.getUserId());
      return bool;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      throw new PackageManager.NameNotFoundException(paramString);
    }
  }
  
  public boolean isPackageSuspendedForUser(String paramString, int paramInt)
  {
    try
    {
      boolean bool = mPM.isPackageSuspendedForUser(paramString, paramInt);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean isPermissionReviewModeEnabled()
  {
    return mContext.getResources().getBoolean(17957004);
  }
  
  public boolean isPermissionRevokedByPolicy(String paramString1, String paramString2)
  {
    try
    {
      boolean bool = mPM.isPermissionRevokedByPolicy(paramString1, paramString2, mContext.getUserId());
      return bool;
    }
    catch (RemoteException paramString1)
    {
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  public boolean isSafeMode()
  {
    try
    {
      if (mCachedSafeMode < 0) {
        mCachedSafeMode = mPM.isSafeMode();
      }
      int i = mCachedSafeMode;
      boolean bool;
      if (i != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isSignedBy(String paramString, KeySet paramKeySet)
  {
    Preconditions.checkNotNull(paramString);
    Preconditions.checkNotNull(paramKeySet);
    try
    {
      boolean bool = mPM.isPackageSignedByKeySet(paramString, paramKeySet);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean isSignedByExactly(String paramString, KeySet paramKeySet)
  {
    Preconditions.checkNotNull(paramString);
    Preconditions.checkNotNull(paramKeySet);
    try
    {
      boolean bool = mPM.isPackageSignedByKeySetExactly(paramString, paramKeySet);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean isSupportUninstallablePreloadedApps()
  {
    try
    {
      boolean bool = mPM.isSupportUninstallablePreloadedApps();
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean isUpgrade()
  {
    try
    {
      boolean bool = mPM.isUpgrade();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public Drawable loadItemIcon(PackageItemInfo paramPackageItemInfo, ApplicationInfo paramApplicationInfo)
  {
    if ((paramApplicationInfo != null) && (mEnableTwinApps))
    {
      int i = getUserManager().getTwinAppsId();
      if ((i != -1) && (i == UserHandle.getUserId(uid))) {
        return getUserBadgedIcon(loadUnbadgedItemIcon(paramPackageItemInfo, paramApplicationInfo), new UserHandle(i));
      }
      if (mContext.getUserId() == i) {
        return getUserBadgedIcon(loadUnbadgedItemIcon(paramPackageItemInfo, paramApplicationInfo), new UserHandle(0));
      }
    }
    paramApplicationInfo = loadUnbadgedItemIcon(paramPackageItemInfo, paramApplicationInfo);
    if (showUserIcon != 55536) {
      return paramApplicationInfo;
    }
    return getUserBadgedIcon(paramApplicationInfo, new UserHandle(mContext.getUserId()));
  }
  
  public Drawable loadUnbadgedItemIcon(PackageItemInfo paramPackageItemInfo, ApplicationInfo paramApplicationInfo)
  {
    if (showUserIcon != 55536)
    {
      paramApplicationInfo = getUserManager().getUserIcon(showUserIcon);
      if (paramApplicationInfo == null) {
        return UserIcons.getDefaultUserIcon(mContext.getResources(), showUserIcon, false);
      }
      return new BitmapDrawable(paramApplicationInfo);
    }
    Drawable localDrawable = null;
    if (packageName != null) {
      localDrawable = getDrawable(packageName, icon, paramApplicationInfo);
    }
    paramApplicationInfo = localDrawable;
    if (localDrawable == null) {
      paramApplicationInfo = paramPackageItemInfo.loadDefaultIcon(this);
    }
    return paramApplicationInfo;
  }
  
  public int movePackage(String paramString, VolumeInfo paramVolumeInfo)
  {
    try
    {
      if ("private".equals(id)) {
        paramVolumeInfo = StorageManager.UUID_PRIVATE_INTERNAL;
      }
      for (;;)
      {
        break;
        if (paramVolumeInfo.isPrimaryPhysical()) {
          paramVolumeInfo = "primary_physical";
        } else {
          paramVolumeInfo = (String)Preconditions.checkNotNull(fsUuid);
        }
      }
      int i = mPM.movePackage(paramString, paramVolumeInfo);
      return i;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public int movePrimaryStorage(VolumeInfo paramVolumeInfo)
  {
    try
    {
      if ("private".equals(id)) {
        paramVolumeInfo = StorageManager.UUID_PRIVATE_INTERNAL;
      }
      for (;;)
      {
        break;
        if (paramVolumeInfo.isPrimaryPhysical()) {
          paramVolumeInfo = "primary_physical";
        } else {
          paramVolumeInfo = (String)Preconditions.checkNotNull(fsUuid);
        }
      }
      int i = mPM.movePrimaryStorage(paramVolumeInfo);
      return i;
    }
    catch (RemoteException paramVolumeInfo)
    {
      throw paramVolumeInfo.rethrowFromSystemServer();
    }
  }
  
  public List<ResolveInfo> queryBroadcastReceivers(Intent paramIntent, int paramInt)
  {
    return queryBroadcastReceiversAsUser(paramIntent, paramInt, mContext.getUserId());
  }
  
  public List<ResolveInfo> queryBroadcastReceiversAsUser(Intent paramIntent, int paramInt1, int paramInt2)
  {
    try
    {
      paramIntent = mPM.queryIntentReceivers(paramIntent, paramIntent.resolveTypeIfNeeded(mContext.getContentResolver()), paramInt1, paramInt2);
      if (paramIntent == null) {
        return Collections.emptyList();
      }
      paramIntent = paramIntent.getList();
      return paramIntent;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  public List<ProviderInfo> queryContentProviders(String paramString, int paramInt1, int paramInt2)
  {
    return queryContentProviders(paramString, paramInt1, paramInt2, null);
  }
  
  public List<ProviderInfo> queryContentProviders(String paramString1, int paramInt1, int paramInt2, String paramString2)
  {
    try
    {
      paramString1 = mPM.queryContentProviders(paramString1, paramInt1, paramInt2, paramString2);
      if (paramString1 != null) {
        paramString1 = paramString1.getList();
      } else {
        paramString1 = Collections.emptyList();
      }
      return paramString1;
    }
    catch (RemoteException paramString1)
    {
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  public List<InstrumentationInfo> queryInstrumentation(String paramString, int paramInt)
  {
    try
    {
      paramString = mPM.queryInstrumentation(paramString, paramInt);
      if (paramString == null) {
        return Collections.emptyList();
      }
      paramString = paramString.getList();
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public List<ResolveInfo> queryIntentActivities(Intent paramIntent, int paramInt)
  {
    return queryIntentActivitiesAsUser(paramIntent, paramInt, mContext.getUserId());
  }
  
  public List<ResolveInfo> queryIntentActivitiesAsUser(Intent paramIntent, int paramInt1, int paramInt2)
  {
    try
    {
      paramIntent = mPM.queryIntentActivities(paramIntent, paramIntent.resolveTypeIfNeeded(mContext.getContentResolver()), paramInt1, paramInt2);
      if (paramIntent == null) {
        return Collections.emptyList();
      }
      paramIntent = paramIntent.getList();
      return paramIntent;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  public List<ResolveInfo> queryIntentActivityOptions(ComponentName paramComponentName, Intent[] paramArrayOfIntent, Intent paramIntent, int paramInt)
  {
    ContentResolver localContentResolver = mContext.getContentResolver();
    Object localObject1 = null;
    Object localObject2 = null;
    if (paramArrayOfIntent != null)
    {
      int i = paramArrayOfIntent.length;
      int j = 0;
      for (;;)
      {
        localObject1 = localObject2;
        if (j >= i) {
          break;
        }
        Object localObject3 = paramArrayOfIntent[j];
        localObject1 = localObject2;
        if (localObject3 != null)
        {
          localObject3 = ((Intent)localObject3).resolveTypeIfNeeded(localContentResolver);
          localObject1 = localObject2;
          if (localObject3 != null)
          {
            localObject1 = localObject2;
            if (localObject2 == null) {
              localObject1 = new String[i];
            }
            localObject1[j] = localObject3;
          }
        }
        j++;
        localObject2 = localObject1;
      }
    }
    try
    {
      localObject2 = mPM;
      try
      {
        paramComponentName = ((IPackageManager)localObject2).queryIntentActivityOptions(paramComponentName, paramArrayOfIntent, (String[])localObject1, paramIntent, paramIntent.resolveTypeIfNeeded(localContentResolver), paramInt, mContext.getUserId());
        if (paramComponentName == null) {
          return Collections.emptyList();
        }
        paramComponentName = paramComponentName.getList();
        return paramComponentName;
      }
      catch (RemoteException paramComponentName) {}
      throw paramComponentName.rethrowFromSystemServer();
    }
    catch (RemoteException paramComponentName) {}
  }
  
  public List<ResolveInfo> queryIntentContentProviders(Intent paramIntent, int paramInt)
  {
    return queryIntentContentProvidersAsUser(paramIntent, paramInt, mContext.getUserId());
  }
  
  public List<ResolveInfo> queryIntentContentProvidersAsUser(Intent paramIntent, int paramInt1, int paramInt2)
  {
    try
    {
      paramIntent = mPM.queryIntentContentProviders(paramIntent, paramIntent.resolveTypeIfNeeded(mContext.getContentResolver()), paramInt1, paramInt2);
      if (paramIntent == null) {
        return Collections.emptyList();
      }
      paramIntent = paramIntent.getList();
      return paramIntent;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  public List<ResolveInfo> queryIntentServices(Intent paramIntent, int paramInt)
  {
    return queryIntentServicesAsUser(paramIntent, paramInt, mContext.getUserId());
  }
  
  public List<ResolveInfo> queryIntentServicesAsUser(Intent paramIntent, int paramInt1, int paramInt2)
  {
    try
    {
      paramIntent = mPM.queryIntentServices(paramIntent, paramIntent.resolveTypeIfNeeded(mContext.getContentResolver()), paramInt1, paramInt2);
      if (paramIntent == null) {
        return Collections.emptyList();
      }
      paramIntent = paramIntent.getList();
      return paramIntent;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  public List<PermissionInfo> queryPermissionsByGroup(String paramString, int paramInt)
    throws PackageManager.NameNotFoundException
  {
    try
    {
      Object localObject = mPM.queryPermissionsByGroup(paramString, paramInt);
      if (localObject != null)
      {
        localObject = ((ParceledListSlice)localObject).getList();
        if (localObject != null) {
          return localObject;
        }
      }
      throw new PackageManager.NameNotFoundException(paramString);
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void refreshApp2sdBlacklist()
  {
    try
    {
      mPM.refreshApp2sdBlacklist();
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void refreshAppAspectRatio()
  {
    try
    {
      mPM.refreshAppAspectRatio();
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void refreshAppSupportNotchList()
  {
    try
    {
      mPM.refreshAppSupportNotchList();
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void registerDexModule(String paramString, PackageManager.DexModuleRegisterCallback paramDexModuleRegisterCallback)
  {
    boolean bool = false;
    try
    {
      Object localObject = Os.stat(paramString);
      int i = OsConstants.S_IROTH;
      int j = st_mode;
      if ((i & j) != 0) {
        bool = true;
      }
      localObject = null;
      if (paramDexModuleRegisterCallback != null) {
        localObject = new DexModuleRegisterCallbackDelegate(paramDexModuleRegisterCallback);
      }
      try
      {
        mPM.registerDexModule(mContext.getPackageName(), paramString, bool, (IDexModuleRegisterCallback)localObject);
        return;
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowAsRuntimeException();
      }
      StringBuilder localStringBuilder;
      return;
    }
    catch (ErrnoException localErrnoException)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Could not get stat the module file: ");
      localStringBuilder.append(localErrnoException.getMessage());
      paramDexModuleRegisterCallback.onDexModuleRegistered(paramString, false, localStringBuilder.toString());
    }
  }
  
  public void registerMoveCallback(PackageManager.MoveCallback paramMoveCallback, Handler paramHandler)
  {
    synchronized (mDelegates)
    {
      MoveCallbackDelegate localMoveCallbackDelegate = new android/app/ApplicationPackageManager$MoveCallbackDelegate;
      localMoveCallbackDelegate.<init>(paramMoveCallback, paramHandler.getLooper());
      try
      {
        mPM.registerMoveCallback(localMoveCallbackDelegate);
        mDelegates.add(localMoveCallbackDelegate);
        return;
      }
      catch (RemoteException paramMoveCallback)
      {
        throw paramMoveCallback.rethrowFromSystemServer();
      }
    }
  }
  
  public void removeOnPermissionsChangeListener(PackageManager.OnPermissionsChangedListener paramOnPermissionsChangedListener)
  {
    synchronized (mPermissionListeners)
    {
      IOnPermissionsChangeListener localIOnPermissionsChangeListener = (IOnPermissionsChangeListener)mPermissionListeners.get(paramOnPermissionsChangedListener);
      if (localIOnPermissionsChangeListener != null) {
        try
        {
          mPM.removeOnPermissionsChangeListener(localIOnPermissionsChangeListener);
          mPermissionListeners.remove(paramOnPermissionsChangedListener);
        }
        catch (RemoteException paramOnPermissionsChangedListener)
        {
          throw paramOnPermissionsChangedListener.rethrowFromSystemServer();
        }
      }
      return;
    }
  }
  
  public void removePackageFromPreferred(String paramString)
  {
    Log.w("ApplicationPackageManager", "removePackageFromPreferred() is a no-op");
  }
  
  public void removePermission(String paramString)
  {
    try
    {
      mPM.removePermission(paramString);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void replacePreferredActivity(IntentFilter paramIntentFilter, int paramInt, ComponentName[] paramArrayOfComponentName, ComponentName paramComponentName)
  {
    try
    {
      mPM.replacePreferredActivity(paramIntentFilter, paramInt, paramArrayOfComponentName, paramComponentName, mContext.getUserId());
      return;
    }
    catch (RemoteException paramIntentFilter)
    {
      throw paramIntentFilter.rethrowFromSystemServer();
    }
  }
  
  public void replacePreferredActivityAsUser(IntentFilter paramIntentFilter, int paramInt1, ComponentName[] paramArrayOfComponentName, ComponentName paramComponentName, int paramInt2)
  {
    try
    {
      mPM.replacePreferredActivity(paramIntentFilter, paramInt1, paramArrayOfComponentName, paramComponentName, paramInt2);
      return;
    }
    catch (RemoteException paramIntentFilter)
    {
      throw paramIntentFilter.rethrowFromSystemServer();
    }
  }
  
  public ResolveInfo resolveActivity(Intent paramIntent, int paramInt)
  {
    return resolveActivityAsUser(paramIntent, paramInt, mContext.getUserId());
  }
  
  public ResolveInfo resolveActivityAsUser(Intent paramIntent, int paramInt1, int paramInt2)
  {
    try
    {
      paramIntent = mPM.resolveIntent(paramIntent, paramIntent.resolveTypeIfNeeded(mContext.getContentResolver()), paramInt1, paramInt2);
      return paramIntent;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  public ProviderInfo resolveContentProvider(String paramString, int paramInt)
  {
    return resolveContentProviderAsUser(paramString, paramInt, mContext.getUserId());
  }
  
  public ProviderInfo resolveContentProviderAsUser(String paramString, int paramInt1, int paramInt2)
  {
    try
    {
      paramString = mPM.resolveContentProvider(paramString, paramInt1, paramInt2);
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public ResolveInfo resolveService(Intent paramIntent, int paramInt)
  {
    return resolveServiceAsUser(paramIntent, paramInt, mContext.getUserId());
  }
  
  public ResolveInfo resolveServiceAsUser(Intent paramIntent, int paramInt1, int paramInt2)
  {
    try
    {
      paramIntent = mPM.resolveService(paramIntent, paramIntent.resolveTypeIfNeeded(mContext.getContentResolver()), paramInt1, paramInt2);
      return paramIntent;
    }
    catch (RemoteException paramIntent)
    {
      throw paramIntent.rethrowFromSystemServer();
    }
  }
  
  public void revokeRuntimePermission(String paramString1, String paramString2, UserHandle paramUserHandle)
  {
    try
    {
      mPM.revokeRuntimePermission(paramString1, paramString2, paramUserHandle.getIdentifier());
      return;
    }
    catch (RemoteException paramString1)
    {
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  public void setApplicationCategoryHint(String paramString, int paramInt)
  {
    try
    {
      mPM.setApplicationCategoryHint(paramString, paramInt, mContext.getOpPackageName());
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void setApplicationEnabledSetting(String paramString, int paramInt1, int paramInt2)
  {
    try
    {
      mPM.setApplicationEnabledSetting(paramString, paramInt1, paramInt2, mContext.getUserId(), mContext.getOpPackageName());
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean setApplicationHiddenSettingAsUser(String paramString, boolean paramBoolean, UserHandle paramUserHandle)
  {
    try
    {
      paramBoolean = mPM.setApplicationHiddenSettingAsUser(paramString, paramBoolean, paramUserHandle.getIdentifier());
      return paramBoolean;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void setComponentEnabledSetting(ComponentName paramComponentName, int paramInt1, int paramInt2)
  {
    try
    {
      mPM.setComponentEnabledSetting(paramComponentName, paramInt1, paramInt2, mContext.getUserId());
      return;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public boolean setDefaultBrowserPackageNameAsUser(String paramString, int paramInt)
  {
    try
    {
      boolean bool = mPM.setDefaultBrowserPackageName(paramString, paramInt);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void setFillNotchRegion(String paramString, boolean paramBoolean, int paramInt)
  {
    try
    {
      mPM.setFillNotchRegion(paramString, paramBoolean, paramInt);
    }
    catch (RemoteException paramString) {}
  }
  
  public void setHarmfulAppWarning(String paramString, CharSequence paramCharSequence)
  {
    try
    {
      mPM.setHarmfulAppWarning(paramString, paramCharSequence, mContext.getUserId());
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowAsRuntimeException();
    }
  }
  
  public void setInstallerPackageName(String paramString1, String paramString2)
  {
    try
    {
      mPM.setInstallerPackageName(paramString1, paramString2);
      return;
    }
    catch (RemoteException paramString1)
    {
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  public boolean setInstantAppCookie(byte[] paramArrayOfByte)
  {
    try
    {
      boolean bool = mPM.setInstantAppCookie(mContext.getPackageName(), paramArrayOfByte, mContext.getUserId());
      return bool;
    }
    catch (RemoteException paramArrayOfByte)
    {
      throw paramArrayOfByte.rethrowFromSystemServer();
    }
  }
  
  public void setOverrideMaxAspect(String paramString, float paramFloat, int paramInt)
  {
    try
    {
      mPM.setOverrideMaxAspect(paramString, paramFloat, paramInt);
    }
    catch (RemoteException paramString) {}
  }
  
  public String[] setPackagesSuspended(String[] paramArrayOfString, boolean paramBoolean, PersistableBundle paramPersistableBundle1, PersistableBundle paramPersistableBundle2, String paramString)
  {
    try
    {
      paramArrayOfString = mPM.setPackagesSuspendedAsUser(paramArrayOfString, paramBoolean, paramPersistableBundle1, paramPersistableBundle2, paramString, mContext.getOpPackageName(), mContext.getUserId());
      return paramArrayOfString;
    }
    catch (RemoteException paramArrayOfString)
    {
      throw paramArrayOfString.rethrowFromSystemServer();
    }
  }
  
  public void setUpdateAvailable(String paramString, boolean paramBoolean)
  {
    try
    {
      mPM.setUpdateAvailable(paramString, paramBoolean);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean shouldShowRequestPermissionRationale(String paramString)
  {
    try
    {
      boolean bool = mPM.shouldShowRequestPermissionRationale(paramString, mContext.getPackageName(), mContext.getUserId());
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void unregisterMoveCallback(PackageManager.MoveCallback paramMoveCallback)
  {
    synchronized (mDelegates)
    {
      Iterator localIterator = mDelegates.iterator();
      while (localIterator.hasNext())
      {
        MoveCallbackDelegate localMoveCallbackDelegate = (MoveCallbackDelegate)localIterator.next();
        PackageManager.MoveCallback localMoveCallback = mCallback;
        if (localMoveCallback == paramMoveCallback) {
          try
          {
            mPM.unregisterMoveCallback(localMoveCallbackDelegate);
            localIterator.remove();
          }
          catch (RemoteException paramMoveCallback)
          {
            throw paramMoveCallback.rethrowFromSystemServer();
          }
        }
      }
      return;
    }
  }
  
  public void updateInstantAppCookie(byte[] paramArrayOfByte)
  {
    if ((paramArrayOfByte != null) && (paramArrayOfByte.length > getInstantAppCookieMaxBytes()))
    {
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("instant cookie longer than ");
      paramArrayOfByte.append(getInstantAppCookieMaxBytes());
      throw new IllegalArgumentException(paramArrayOfByte.toString());
    }
    try
    {
      mPM.setInstantAppCookie(mContext.getPackageName(), paramArrayOfByte, mContext.getUserId());
      return;
    }
    catch (RemoteException paramArrayOfByte)
    {
      throw paramArrayOfByte.rethrowFromSystemServer();
    }
  }
  
  public boolean updateIntentVerificationStatusAsUser(String paramString, int paramInt1, int paramInt2)
  {
    try
    {
      boolean bool = mPM.updateIntentVerificationStatus(paramString, paramInt1, paramInt2);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void updatePermissionFlags(String paramString1, String paramString2, int paramInt1, int paramInt2, UserHandle paramUserHandle)
  {
    try
    {
      mPM.updatePermissionFlags(paramString1, paramString2, paramInt1, paramInt2, paramUserHandle.getIdentifier());
      return;
    }
    catch (RemoteException paramString1)
    {
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  public void verifyIntentFilter(int paramInt1, int paramInt2, List<String> paramList)
  {
    try
    {
      mPM.verifyIntentFilter(paramInt1, paramInt2, paramList);
      return;
    }
    catch (RemoteException paramList)
    {
      throw paramList.rethrowFromSystemServer();
    }
  }
  
  public void verifyPendingInstall(int paramInt1, int paramInt2)
  {
    try
    {
      mPM.verifyPendingInstall(paramInt1, paramInt2);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  private static class DexModuleRegisterCallbackDelegate
    extends IDexModuleRegisterCallback.Stub
    implements Handler.Callback
  {
    private static final int MSG_DEX_MODULE_REGISTERED = 1;
    private final PackageManager.DexModuleRegisterCallback callback;
    private final Handler mHandler;
    
    DexModuleRegisterCallbackDelegate(PackageManager.DexModuleRegisterCallback paramDexModuleRegisterCallback)
    {
      callback = paramDexModuleRegisterCallback;
      mHandler = new Handler(Looper.getMainLooper(), this);
    }
    
    public boolean handleMessage(Message paramMessage)
    {
      if (what != 1) {
        return false;
      }
      paramMessage = (ApplicationPackageManager.DexModuleRegisterResult)obj;
      callback.onDexModuleRegistered(dexModulePath, success, message);
      return true;
    }
    
    public void onDexModuleRegistered(String paramString1, boolean paramBoolean, String paramString2)
      throws RemoteException
    {
      mHandler.obtainMessage(1, new ApplicationPackageManager.DexModuleRegisterResult(paramString1, paramBoolean, paramString2, null)).sendToTarget();
    }
  }
  
  private static class DexModuleRegisterResult
  {
    final String dexModulePath;
    final String message;
    final boolean success;
    
    private DexModuleRegisterResult(String paramString1, boolean paramBoolean, String paramString2)
    {
      dexModulePath = paramString1;
      success = paramBoolean;
      message = paramString2;
    }
  }
  
  private static class MoveCallbackDelegate
    extends IPackageMoveObserver.Stub
    implements Handler.Callback
  {
    private static final int MSG_CREATED = 1;
    private static final int MSG_STATUS_CHANGED = 2;
    final PackageManager.MoveCallback mCallback;
    final Handler mHandler;
    
    public MoveCallbackDelegate(PackageManager.MoveCallback paramMoveCallback, Looper paramLooper)
    {
      mCallback = paramMoveCallback;
      mHandler = new Handler(paramLooper, this);
    }
    
    public boolean handleMessage(Message paramMessage)
    {
      switch (what)
      {
      default: 
        return false;
      case 2: 
        paramMessage = (SomeArgs)obj;
        mCallback.onStatusChanged(argi1, argi2, ((Long)arg3).longValue());
        paramMessage.recycle();
        return true;
      }
      paramMessage = (SomeArgs)obj;
      mCallback.onCreated(argi1, (Bundle)arg2);
      paramMessage.recycle();
      return true;
    }
    
    public void onCreated(int paramInt, Bundle paramBundle)
    {
      SomeArgs localSomeArgs = SomeArgs.obtain();
      argi1 = paramInt;
      arg2 = paramBundle;
      mHandler.obtainMessage(1, localSomeArgs).sendToTarget();
    }
    
    public void onStatusChanged(int paramInt1, int paramInt2, long paramLong)
    {
      SomeArgs localSomeArgs = SomeArgs.obtain();
      argi1 = paramInt1;
      argi2 = paramInt2;
      arg3 = Long.valueOf(paramLong);
      mHandler.obtainMessage(2, localSomeArgs).sendToTarget();
    }
  }
  
  public class OnPermissionsChangeListenerDelegate
    extends IOnPermissionsChangeListener.Stub
    implements Handler.Callback
  {
    private static final int MSG_PERMISSIONS_CHANGED = 1;
    private final Handler mHandler;
    private final PackageManager.OnPermissionsChangedListener mListener;
    
    public OnPermissionsChangeListenerDelegate(PackageManager.OnPermissionsChangedListener paramOnPermissionsChangedListener, Looper paramLooper)
    {
      mListener = paramOnPermissionsChangedListener;
      mHandler = new Handler(paramLooper, this);
    }
    
    public boolean handleMessage(Message paramMessage)
    {
      if (what != 1) {
        return false;
      }
      int i = arg1;
      mListener.onPermissionsChanged(i);
      return true;
    }
    
    public void onPermissionsChanged(int paramInt)
    {
      mHandler.obtainMessage(1, paramInt, 0).sendToTarget();
    }
  }
  
  private static final class ResourceName
  {
    final int iconId;
    final String packageName;
    
    ResourceName(ApplicationInfo paramApplicationInfo, int paramInt)
    {
      this(packageName, paramInt);
    }
    
    ResourceName(ComponentInfo paramComponentInfo, int paramInt)
    {
      this(applicationInfo.packageName, paramInt);
    }
    
    ResourceName(ResolveInfo paramResolveInfo, int paramInt)
    {
      this(activityInfo.applicationInfo.packageName, paramInt);
    }
    
    ResourceName(String paramString, int paramInt)
    {
      packageName = paramString;
      iconId = paramInt;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool = true;
      if (this == paramObject) {
        return true;
      }
      if ((paramObject != null) && (getClass() == paramObject.getClass()))
      {
        paramObject = (ResourceName)paramObject;
        if (iconId != iconId) {
          return false;
        }
        if (packageName != null)
        {
          if (!packageName.equals(packageName)) {
            break label79;
          }
        }
        else {
          if (packageName == null) {
            break label81;
          }
        }
        label79:
        bool = false;
        label81:
        return bool;
      }
      return false;
    }
    
    public int hashCode()
    {
      return 31 * packageName.hashCode() + iconId;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("{ResourceName ");
      localStringBuilder.append(packageName);
      localStringBuilder.append(" / ");
      localStringBuilder.append(iconId);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
  }
}
