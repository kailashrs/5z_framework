package android.app;

import android.content.res.ApkAssets;
import android.content.res.AssetManager;
import android.content.res.AssetManager.Builder;
import android.content.res.CompatResources;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.ResourcesImpl;
import android.content.res.ResourcesKey;
import android.hardware.display.DisplayManagerGlobal;
import android.os.IBinder;
import android.os.Trace;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.util.Pair;
import android.view.Display;
import android.view.DisplayAdjustments;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.IndentingPrintWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.Predicate;

public class ResourcesManager
{
  private static final boolean DEBUG = false;
  static final String TAG = "ResourcesManager";
  private static final Predicate<WeakReference<Resources>> sEmptyReferencePredicate = _..Lambda.ResourcesManager.QJ7UiVk_XS90KuXAsIjIEym1DnM.INSTANCE;
  private static ResourcesManager sResourcesManager;
  private final WeakHashMap<IBinder, ActivityResources> mActivityResourceReferences = new WeakHashMap();
  private final ArrayMap<Pair<Integer, DisplayAdjustments>, WeakReference<Display>> mAdjustedDisplays = new ArrayMap();
  private final ArrayMap<ApkKey, WeakReference<ApkAssets>> mCachedApkAssets = new ArrayMap();
  private final LruCache<ApkKey, ApkAssets> mLoadedApkAssets = new LruCache(3);
  private CompatibilityInfo mResCompatibilityInfo;
  private final Configuration mResConfiguration = new Configuration();
  private final ArrayMap<ResourcesKey, WeakReference<ResourcesImpl>> mResourceImpls = new ArrayMap();
  private final ArrayList<WeakReference<Resources>> mResourceReferences = new ArrayList();
  
  public ResourcesManager() {}
  
  private static void applyNonDefaultDisplayMetricsToConfiguration(DisplayMetrics paramDisplayMetrics, Configuration paramConfiguration)
  {
    touchscreen = 1;
    densityDpi = densityDpi;
    screenWidthDp = ((int)(widthPixels / density));
    screenHeightDp = ((int)(heightPixels / density));
    int i = Configuration.resetScreenLayout(screenLayout);
    if (widthPixels > heightPixels)
    {
      orientation = 2;
      screenLayout = Configuration.reduceScreenLayout(i, screenWidthDp, screenHeightDp);
    }
    else
    {
      orientation = 1;
      screenLayout = Configuration.reduceScreenLayout(i, screenHeightDp, screenWidthDp);
    }
    smallestScreenWidthDp = screenWidthDp;
    compatScreenWidthDp = screenWidthDp;
    compatScreenHeightDp = screenHeightDp;
    compatSmallestScreenWidthDp = smallestScreenWidthDp;
  }
  
  private void cleanupResourceImpl(ResourcesKey paramResourcesKey)
  {
    paramResourcesKey = (ResourcesImpl)((WeakReference)mResourceImpls.remove(paramResourcesKey)).get();
    if (paramResourcesKey != null) {
      paramResourcesKey.flushLayoutCache();
    }
  }
  
  private static <T> int countLiveReferences(Collection<WeakReference<T>> paramCollection)
  {
    int i = 0;
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      paramCollection = (WeakReference)localIterator.next();
      if (paramCollection != null) {
        paramCollection = paramCollection.get();
      } else {
        paramCollection = null;
      }
      int j = i;
      if (paramCollection != null) {
        j = i + 1;
      }
      i = j;
    }
    return i;
  }
  
  private ResourcesImpl createResourcesImpl(ResourcesKey paramResourcesKey)
  {
    DisplayAdjustments localDisplayAdjustments = new DisplayAdjustments(mOverrideConfiguration);
    localDisplayAdjustments.setCompatibilityInfo(mCompatInfo);
    AssetManager localAssetManager = createAssetManager(paramResourcesKey);
    if (localAssetManager == null) {
      return null;
    }
    DisplayMetrics localDisplayMetrics = getDisplayMetrics(mDisplayId, localDisplayAdjustments);
    return new ResourcesImpl(localAssetManager, localDisplayMetrics, generateConfig(paramResourcesKey, localDisplayMetrics), localDisplayAdjustments);
  }
  
  private ResourcesKey findKeyForResourceImplLocked(ResourcesImpl paramResourcesImpl)
  {
    int i = mResourceImpls.size();
    for (int j = 0;; j++)
    {
      ResourcesImpl localResourcesImpl = null;
      if (j >= i) {
        break;
      }
      WeakReference localWeakReference = (WeakReference)mResourceImpls.valueAt(j);
      if (localWeakReference != null) {
        localResourcesImpl = (ResourcesImpl)localWeakReference.get();
      }
      if ((localResourcesImpl != null) && (paramResourcesImpl == localResourcesImpl)) {
        return (ResourcesKey)mResourceImpls.keyAt(j);
      }
    }
    return null;
  }
  
  private ResourcesImpl findOrCreateResourcesImplForKeyLocked(ResourcesKey paramResourcesKey)
  {
    ResourcesImpl localResourcesImpl1 = findResourcesImplForKeyLocked(paramResourcesKey);
    ResourcesImpl localResourcesImpl2 = localResourcesImpl1;
    if (localResourcesImpl1 == null)
    {
      localResourcesImpl1 = createResourcesImpl(paramResourcesKey);
      localResourcesImpl2 = localResourcesImpl1;
      if (localResourcesImpl1 != null)
      {
        mResourceImpls.put(paramResourcesKey, new WeakReference(localResourcesImpl1));
        localResourcesImpl2 = localResourcesImpl1;
      }
    }
    return localResourcesImpl2;
  }
  
  private ResourcesImpl findResourcesImplForKeyLocked(ResourcesKey paramResourcesKey)
  {
    paramResourcesKey = (WeakReference)mResourceImpls.get(paramResourcesKey);
    if (paramResourcesKey != null) {
      paramResourcesKey = (ResourcesImpl)paramResourcesKey.get();
    } else {
      paramResourcesKey = null;
    }
    if ((paramResourcesKey != null) && (paramResourcesKey.getAssets().isUpToDate())) {
      return paramResourcesKey;
    }
    return null;
  }
  
  private Configuration generateConfig(ResourcesKey paramResourcesKey, DisplayMetrics paramDisplayMetrics)
  {
    int i;
    if (mDisplayId == 0) {
      i = 1;
    } else {
      i = 0;
    }
    boolean bool = paramResourcesKey.hasOverrideConfiguration();
    if ((i != 0) && (!bool))
    {
      paramDisplayMetrics = getConfiguration();
    }
    else
    {
      Configuration localConfiguration = new Configuration(getConfiguration());
      if (i == 0) {
        applyNonDefaultDisplayMetricsToConfiguration(paramDisplayMetrics, localConfiguration);
      }
      paramDisplayMetrics = localConfiguration;
      if (bool)
      {
        localConfiguration.updateFrom(mOverrideConfiguration);
        paramDisplayMetrics = localConfiguration;
      }
    }
    return paramDisplayMetrics;
  }
  
  private Display getAdjustedDisplay(int paramInt, DisplayAdjustments paramDisplayAdjustments)
  {
    if (paramDisplayAdjustments != null) {
      paramDisplayAdjustments = new DisplayAdjustments(paramDisplayAdjustments);
    } else {
      paramDisplayAdjustments = new DisplayAdjustments();
    }
    paramDisplayAdjustments = Pair.create(Integer.valueOf(paramInt), paramDisplayAdjustments);
    try
    {
      Object localObject = (WeakReference)mAdjustedDisplays.get(paramDisplayAdjustments);
      if (localObject != null)
      {
        localObject = (Display)((WeakReference)localObject).get();
        if (localObject != null) {
          return localObject;
        }
      }
      localObject = DisplayManagerGlobal.getInstance();
      if (localObject == null) {
        return null;
      }
      localObject = ((DisplayManagerGlobal)localObject).getCompatibleDisplay(paramInt, (DisplayAdjustments)second);
      if (localObject != null)
      {
        ArrayMap localArrayMap = mAdjustedDisplays;
        WeakReference localWeakReference = new java/lang/ref/WeakReference;
        localWeakReference.<init>(localObject);
        localArrayMap.put(paramDisplayAdjustments, localWeakReference);
      }
      return localObject;
    }
    finally {}
  }
  
  public static ResourcesManager getInstance()
  {
    try
    {
      if (sResourcesManager == null)
      {
        localResourcesManager = new android/app/ResourcesManager;
        localResourcesManager.<init>();
        sResourcesManager = localResourcesManager;
      }
      ResourcesManager localResourcesManager = sResourcesManager;
      return localResourcesManager;
    }
    finally {}
  }
  
  private ActivityResources getOrCreateActivityResourcesStructLocked(IBinder paramIBinder)
  {
    ActivityResources localActivityResources1 = (ActivityResources)mActivityResourceReferences.get(paramIBinder);
    ActivityResources localActivityResources2 = localActivityResources1;
    if (localActivityResources1 == null)
    {
      localActivityResources2 = new ActivityResources(null);
      mActivityResourceReferences.put(paramIBinder, localActivityResources2);
    }
    return localActivityResources2;
  }
  
  private Resources getOrCreateResources(IBinder paramIBinder, ResourcesKey paramResourcesKey, ClassLoader paramClassLoader)
  {
    if (paramIBinder != null) {
      try
      {
        localObject1 = getOrCreateActivityResourcesStructLocked(paramIBinder);
        ArrayUtils.unstableRemoveIf(activityResources, sEmptyReferencePredicate);
        if ((paramResourcesKey.hasOverrideConfiguration()) && (!overrideConfig.equals(Configuration.EMPTY)))
        {
          localObject2 = new android/content/res/Configuration;
          ((Configuration)localObject2).<init>(overrideConfig);
          ((Configuration)localObject2).updateFrom(mOverrideConfiguration);
          mOverrideConfiguration.setTo((Configuration)localObject2);
        }
        localObject2 = findResourcesImplForKeyLocked(paramResourcesKey);
        if (localObject2 != null)
        {
          paramIBinder = getOrCreateResourcesForActivityLocked(paramIBinder, paramClassLoader, (ResourcesImpl)localObject2, mCompatInfo);
          return paramIBinder;
        }
      }
      finally
      {
        break label234;
      }
    }
    ArrayUtils.unstableRemoveIf(mResourceReferences, sEmptyReferencePredicate);
    Object localObject2 = findResourcesImplForKeyLocked(paramResourcesKey);
    if (localObject2 != null)
    {
      paramIBinder = getOrCreateResourcesLocked(paramClassLoader, (ResourcesImpl)localObject2, mCompatInfo);
      return paramIBinder;
    }
    ResourcesImpl localResourcesImpl = createResourcesImpl(paramResourcesKey);
    if (localResourcesImpl == null) {
      return null;
    }
    Object localObject1 = mResourceImpls;
    localObject2 = new java/lang/ref/WeakReference;
    ((WeakReference)localObject2).<init>(localResourcesImpl);
    ((ArrayMap)localObject1).put(paramResourcesKey, localObject2);
    if (paramIBinder != null) {
      paramIBinder = getOrCreateResourcesForActivityLocked(paramIBinder, paramClassLoader, localResourcesImpl, mCompatInfo);
    } else {
      paramIBinder = getOrCreateResourcesLocked(paramClassLoader, localResourcesImpl, mCompatInfo);
    }
    return paramIBinder;
    label234:
    throw paramIBinder;
  }
  
  private Resources getOrCreateResourcesForActivityLocked(IBinder paramIBinder, ClassLoader paramClassLoader, ResourcesImpl paramResourcesImpl, CompatibilityInfo paramCompatibilityInfo)
  {
    ActivityResources localActivityResources = getOrCreateActivityResourcesStructLocked(paramIBinder);
    int i = activityResources.size();
    for (int j = 0; j < i; j++)
    {
      paramIBinder = (Resources)((WeakReference)activityResources.get(j)).get();
      if ((paramIBinder != null) && (Objects.equals(paramIBinder.getClassLoader(), paramClassLoader)) && (paramIBinder.getImpl() == paramResourcesImpl)) {
        return paramIBinder;
      }
    }
    if (paramCompatibilityInfo.needsCompatResources()) {
      paramIBinder = new CompatResources(paramClassLoader);
    } else {
      paramIBinder = new Resources(paramClassLoader);
    }
    paramIBinder.setImpl(paramResourcesImpl);
    activityResources.add(new WeakReference(paramIBinder));
    return paramIBinder;
  }
  
  private Resources getOrCreateResourcesLocked(ClassLoader paramClassLoader, ResourcesImpl paramResourcesImpl, CompatibilityInfo paramCompatibilityInfo)
  {
    int i = mResourceReferences.size();
    for (int j = 0; j < i; j++)
    {
      Resources localResources = (Resources)((WeakReference)mResourceReferences.get(j)).get();
      if ((localResources != null) && (Objects.equals(localResources.getClassLoader(), paramClassLoader)) && (localResources.getImpl() == paramResourcesImpl)) {
        return localResources;
      }
    }
    if (paramCompatibilityInfo.needsCompatResources()) {
      paramClassLoader = new CompatResources(paramClassLoader);
    } else {
      paramClassLoader = new Resources(paramClassLoader);
    }
    paramClassLoader.setImpl(paramResourcesImpl);
    mResourceReferences.add(new WeakReference(paramClassLoader));
    return paramClassLoader;
  }
  
  private ApkAssets loadApkAssets(String paramString, boolean paramBoolean1, boolean paramBoolean2)
    throws IOException
  {
    ApkKey localApkKey = new ApkKey(paramString, paramBoolean1, paramBoolean2);
    Object localObject = (ApkAssets)mLoadedApkAssets.get(localApkKey);
    if (localObject != null) {
      return localObject;
    }
    localObject = (WeakReference)mCachedApkAssets.get(localApkKey);
    if (localObject != null)
    {
      localObject = (ApkAssets)((WeakReference)localObject).get();
      if (localObject != null)
      {
        mLoadedApkAssets.put(localApkKey, localObject);
        return localObject;
      }
      mCachedApkAssets.remove(localApkKey);
    }
    if (paramBoolean2) {
      paramString = ApkAssets.loadOverlayFromPath(overlayPathToIdmapPath(paramString), false);
    } else {
      paramString = ApkAssets.loadFromPath(paramString, false, paramBoolean1);
    }
    mLoadedApkAssets.put(localApkKey, paramString);
    mCachedApkAssets.put(localApkKey, new WeakReference(paramString));
    return paramString;
  }
  
  private static String overlayPathToIdmapPath(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("/data/resource-cache/");
    localStringBuilder.append(paramString.substring(1).replace('/', '@'));
    localStringBuilder.append("@idmap");
    return localStringBuilder.toString();
  }
  
  private void redirectResourcesToNewImplLocked(ArrayMap<ResourcesImpl, ResourcesKey> paramArrayMap)
  {
    if (paramArrayMap.isEmpty()) {
      return;
    }
    int i = mResourceReferences.size();
    Object localObject1;
    for (int j = 0;; j++)
    {
      localObject1 = null;
      if (j >= i) {
        break;
      }
      localObject2 = (WeakReference)mResourceReferences.get(j);
      if (localObject2 != null) {
        localObject1 = (Resources)((WeakReference)localObject2).get();
      }
      if (localObject1 != null)
      {
        localObject2 = (ResourcesKey)paramArrayMap.get(((Resources)localObject1).getImpl());
        if (localObject2 != null)
        {
          localObject2 = findOrCreateResourcesImplForKeyLocked((ResourcesKey)localObject2);
          if (localObject2 != null) {
            ((Resources)localObject1).setImpl((ResourcesImpl)localObject2);
          } else {
            throw new Resources.NotFoundException("failed to redirect ResourcesImpl");
          }
        }
      }
    }
    Object localObject2 = mActivityResourceReferences.values().iterator();
    while (((Iterator)localObject2).hasNext())
    {
      ActivityResources localActivityResources = (ActivityResources)((Iterator)localObject2).next();
      i = activityResources.size();
      for (j = 0; j < i; j++)
      {
        localObject1 = (WeakReference)activityResources.get(j);
        if (localObject1 != null) {
          localObject1 = (Resources)((WeakReference)localObject1).get();
        } else {
          localObject1 = null;
        }
        if (localObject1 != null)
        {
          Object localObject3 = (ResourcesKey)paramArrayMap.get(((Resources)localObject1).getImpl());
          if (localObject3 != null)
          {
            localObject3 = findOrCreateResourcesImplForKeyLocked((ResourcesKey)localObject3);
            if (localObject3 != null) {
              ((Resources)localObject1).setImpl((ResourcesImpl)localObject3);
            } else {
              throw new Resources.NotFoundException("failed to redirect ResourcesImpl");
            }
          }
        }
      }
    }
  }
  
  public void appendLibAssetForMainAssetPath(String paramString1, String paramString2)
  {
    for (;;)
    {
      ArrayMap localArrayMap;
      int j;
      Object localObject1;
      ResourcesKey localResourcesKey;
      Object localObject2;
      Object localObject3;
      try
      {
        localArrayMap = new android/util/ArrayMap;
        localArrayMap.<init>();
        int i = mResourceImpls.size();
        j = 0;
        localObject1 = paramString2;
        if (j < i)
        {
          localResourcesKey = (ResourcesKey)mResourceImpls.keyAt(j);
          localObject2 = (WeakReference)mResourceImpls.valueAt(j);
          if (localObject2 != null) {
            localObject2 = (ResourcesImpl)((WeakReference)localObject2).get();
          } else {
            localObject2 = null;
          }
          if (localObject2 != null) {
            localObject3 = mResDir;
          }
        }
      }
      finally {}
      try
      {
        if ((Objects.equals(localObject3, paramString1)) && (!ArrayUtils.contains(mLibDirs, localObject1)))
        {
          int k;
          if (mLibDirs != null) {
            k = mLibDirs.length;
          } else {
            k = 0;
          }
          k++;
          localObject3 = new String[k];
          if (mLibDirs != null) {
            System.arraycopy(mLibDirs, 0, localObject3, 0, mLibDirs.length);
          }
          localObject3[(k - 1)] = localObject1;
          localObject1 = new android/content/res/ResourcesKey;
          ((ResourcesKey)localObject1).<init>(mResDir, mSplitResDirs, mOverlayDirs, (String[])localObject3, mDisplayId, mOverrideConfiguration, mCompatInfo);
          localArrayMap.put(localObject2, localObject1);
        }
        j++;
      }
      finally
      {
        for (;;) {}
      }
    }
    redirectResourcesToNewImplLocked(localArrayMap);
    return;
    throw paramString1;
  }
  
  public boolean applyCompatConfigurationLocked(int paramInt, Configuration paramConfiguration)
  {
    if ((mResCompatibilityInfo != null) && (!mResCompatibilityInfo.supportsScreen()))
    {
      mResCompatibilityInfo.applyToConfiguration(paramInt, paramConfiguration);
      return true;
    }
    return false;
  }
  
  public final boolean applyConfigurationToResourcesLocked(Configuration paramConfiguration, CompatibilityInfo paramCompatibilityInfo)
  {
    try
    {
      Trace.traceBegin(8192L, "ResourcesManager#applyConfigurationToResourcesLocked");
      boolean bool = mResConfiguration.isOtherSeqNewer(paramConfiguration);
      if ((!bool) && (paramCompatibilityInfo == null)) {
        return false;
      }
      int i = mResConfiguration.updateFrom(paramConfiguration);
      mAdjustedDisplays.clear();
      DisplayMetrics localDisplayMetrics = getDisplayMetrics();
      int j = i;
      if (paramCompatibilityInfo != null) {
        if (mResCompatibilityInfo != null)
        {
          j = i;
          if (mResCompatibilityInfo.equals(paramCompatibilityInfo)) {}
        }
        else
        {
          mResCompatibilityInfo = paramCompatibilityInfo;
          j = i | 0xD00;
        }
      }
      Resources.updateSystemConfiguration(paramConfiguration, localDisplayMetrics, paramCompatibilityInfo);
      ApplicationPackageManager.configurationChanged();
      Object localObject1 = null;
      for (i = mResourceImpls.size() - 1; i >= 0; i--)
      {
        ResourcesKey localResourcesKey = (ResourcesKey)mResourceImpls.keyAt(i);
        Object localObject2 = (WeakReference)mResourceImpls.valueAt(i);
        ResourcesImpl localResourcesImpl;
        if (localObject2 != null) {
          localResourcesImpl = (ResourcesImpl)((WeakReference)localObject2).get();
        } else {
          localResourcesImpl = null;
        }
        if (localResourcesImpl != null)
        {
          int k = mDisplayId;
          int m;
          if (k == 0) {
            m = 1;
          } else {
            m = 0;
          }
          bool = localResourcesKey.hasOverrideConfiguration();
          if ((m != 0) && (!bool))
          {
            localResourcesImpl.updateConfiguration(paramConfiguration, localDisplayMetrics, paramCompatibilityInfo);
          }
          else
          {
            localObject2 = localObject1;
            if (localObject1 == null)
            {
              localObject2 = new android/content/res/Configuration;
              ((Configuration)localObject2).<init>();
            }
            ((Configuration)localObject2).setTo(paramConfiguration);
            DisplayAdjustments localDisplayAdjustments = localResourcesImpl.getDisplayAdjustments();
            localObject1 = localDisplayAdjustments;
            if (paramCompatibilityInfo != null)
            {
              localObject1 = new android/view/DisplayAdjustments;
              ((DisplayAdjustments)localObject1).<init>(localDisplayAdjustments);
              ((DisplayAdjustments)localObject1).setCompatibilityInfo(paramCompatibilityInfo);
            }
            localObject1 = getDisplayMetrics(k, (DisplayAdjustments)localObject1);
            if (m == 0) {
              applyNonDefaultDisplayMetricsToConfiguration((DisplayMetrics)localObject1, (Configuration)localObject2);
            }
            if (bool) {
              ((Configuration)localObject2).updateFrom(mOverrideConfiguration);
            }
            localResourcesImpl.updateConfiguration((Configuration)localObject2, (DisplayMetrics)localObject1, paramCompatibilityInfo);
            localObject1 = localObject2;
          }
        }
        else
        {
          mResourceImpls.removeAt(i);
        }
      }
      if (j != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    finally
    {
      Trace.traceEnd(8192L);
    }
  }
  
  final void applyNewResourceDirsLocked(String paramString, String[] paramArrayOfString)
  {
    try
    {
      Trace.traceBegin(8192L, "ResourcesManager#applyNewResourceDirsLocked");
      ArrayMap localArrayMap = new android/util/ArrayMap;
      localArrayMap.<init>();
      int i = mResourceImpls.size();
      int j = 0;
      for (;;)
      {
        ResourcesKey localResourcesKey;
        Object localObject1;
        Object localObject2;
        if (j < i)
        {
          localResourcesKey = (ResourcesKey)mResourceImpls.keyAt(j);
          localObject1 = (WeakReference)mResourceImpls.valueAt(j);
          if (localObject1 != null) {
            localObject1 = (ResourcesImpl)((WeakReference)localObject1).get();
          } else {
            localObject1 = null;
          }
          if (localObject1 != null) {
            if (mResDir != null) {
              localObject2 = mResDir;
            }
          }
        }
        try
        {
          if (((String)localObject2).equals(paramString))
          {
            localObject2 = new android/content/res/ResourcesKey;
            ((ResourcesKey)localObject2).<init>(mResDir, mSplitResDirs, paramArrayOfString, mLibDirs, mDisplayId, mOverrideConfiguration, mCompatInfo);
            localArrayMap.put(localObject1, localObject2);
          }
          j++;
        }
        finally
        {
          break label193;
        }
      }
      redirectResourcesToNewImplLocked(localArrayMap);
      Trace.traceEnd(8192L);
      return;
    }
    finally {}
    label193:
    Trace.traceEnd(8192L);
    throw paramString;
  }
  
  @VisibleForTesting
  protected AssetManager createAssetManager(ResourcesKey paramResourcesKey)
  {
    AssetManager.Builder localBuilder = new AssetManager.Builder();
    StringBuilder localStringBuilder1;
    if (mResDir != null) {
      try
      {
        localBuilder.addApkAssets(loadApkAssets(mResDir, false, false));
      }
      catch (IOException localIOException1)
      {
        localStringBuilder1 = new StringBuilder();
        localStringBuilder1.append("failed to add asset path ");
        localStringBuilder1.append(mResDir);
        Log.e("ResourcesManager", localStringBuilder1.toString());
        return null;
      }
    }
    String[] arrayOfString;
    int i;
    int j;
    String str;
    if (mSplitResDirs != null)
    {
      arrayOfString = mSplitResDirs;
      i = arrayOfString.length;
      j = 0;
      while (j < i)
      {
        str = arrayOfString[j];
        try
        {
          localStringBuilder1.addApkAssets(loadApkAssets(str, false, false));
          j++;
        }
        catch (IOException paramResourcesKey)
        {
          paramResourcesKey = new StringBuilder();
          paramResourcesKey.append("failed to add split asset path ");
          paramResourcesKey.append(str);
          Log.e("ResourcesManager", paramResourcesKey.toString());
          return null;
        }
      }
    }
    if (mOverlayDirs != null) {
      for (str : mOverlayDirs) {
        try
        {
          localStringBuilder1.addApkAssets(loadApkAssets(str, false, true));
        }
        catch (IOException localIOException3)
        {
          StringBuilder localStringBuilder3 = new StringBuilder();
          localStringBuilder3.append("failed to add overlay path ");
          localStringBuilder3.append(str);
          Log.w("ResourcesManager", localStringBuilder3.toString());
        }
      }
    }
    if (mLibDirs != null) {
      for (str : mLibDirs) {
        if (str.endsWith(".apk")) {
          try
          {
            localStringBuilder1.addApkAssets(loadApkAssets(str, true, false));
          }
          catch (IOException localIOException2)
          {
            StringBuilder localStringBuilder2 = new StringBuilder();
            localStringBuilder2.append("Asset path '");
            localStringBuilder2.append(str);
            localStringBuilder2.append("' does not exist or contains no resources.");
            Log.w("ResourcesManager", localStringBuilder2.toString());
          }
        }
      }
    }
    return localStringBuilder1.build();
  }
  
  /* Error */
  public Resources createBaseActivityResources(IBinder paramIBinder, String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, int paramInt, Configuration paramConfiguration, CompatibilityInfo paramCompatibilityInfo, ClassLoader paramClassLoader)
  {
    // Byte code:
    //   0: ldc2_w 495
    //   3: ldc_w 586
    //   6: invokestatic 504	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   9: new 185	android/content/res/ResourcesKey
    //   12: astore 10
    //   14: aload 7
    //   16: ifnull +18 -> 34
    //   19: new 60	android/content/res/Configuration
    //   22: astore 11
    //   24: aload 11
    //   26: aload 7
    //   28: invokespecial 261	android/content/res/Configuration:<init>	(Landroid/content/res/Configuration;)V
    //   31: goto +6 -> 37
    //   34: aconst_null
    //   35: astore 11
    //   37: aload 10
    //   39: aload_2
    //   40: aload_3
    //   41: aload 4
    //   43: aload 5
    //   45: iload 6
    //   47: aload 11
    //   49: aload 8
    //   51: invokespecial 479	android/content/res/ResourcesKey:<init>	(Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;ILandroid/content/res/Configuration;Landroid/content/res/CompatibilityInfo;)V
    //   54: aload 9
    //   56: ifnull +6 -> 62
    //   59: goto +8 -> 67
    //   62: invokestatic 591	java/lang/ClassLoader:getSystemClassLoader	()Ljava/lang/ClassLoader;
    //   65: astore 9
    //   67: aload_0
    //   68: monitorenter
    //   69: aload_0
    //   70: aload_1
    //   71: invokespecial 315	android/app/ResourcesManager:getOrCreateActivityResourcesStructLocked	(Landroid/os/IBinder;)Landroid/app/ResourcesManager$ActivityResources;
    //   74: pop
    //   75: aload_0
    //   76: monitorexit
    //   77: aload_0
    //   78: aload_1
    //   79: aload 7
    //   81: iload 6
    //   83: iconst_0
    //   84: invokevirtual 595	android/app/ResourcesManager:updateResourcesForActivity	(Landroid/os/IBinder;Landroid/content/res/Configuration;IZ)V
    //   87: aload_0
    //   88: aload_1
    //   89: aload 10
    //   91: aload 9
    //   93: invokespecial 597	android/app/ResourcesManager:getOrCreateResources	(Landroid/os/IBinder;Landroid/content/res/ResourcesKey;Ljava/lang/ClassLoader;)Landroid/content/res/Resources;
    //   96: astore_1
    //   97: ldc2_w 495
    //   100: invokestatic 511	android/os/Trace:traceEnd	(J)V
    //   103: aload_1
    //   104: areturn
    //   105: astore_1
    //   106: aload_0
    //   107: monitorexit
    //   108: aload_1
    //   109: athrow
    //   110: astore_1
    //   111: goto +12 -> 123
    //   114: astore_1
    //   115: goto -9 -> 106
    //   118: astore_1
    //   119: goto +4 -> 123
    //   122: astore_1
    //   123: ldc2_w 495
    //   126: invokestatic 511	android/os/Trace:traceEnd	(J)V
    //   129: aload_1
    //   130: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	131	0	this	ResourcesManager
    //   0	131	1	paramIBinder	IBinder
    //   0	131	2	paramString	String
    //   0	131	3	paramArrayOfString1	String[]
    //   0	131	4	paramArrayOfString2	String[]
    //   0	131	5	paramArrayOfString3	String[]
    //   0	131	6	paramInt	int
    //   0	131	7	paramConfiguration	Configuration
    //   0	131	8	paramCompatibilityInfo	CompatibilityInfo
    //   0	131	9	paramClassLoader	ClassLoader
    //   12	78	10	localResourcesKey	ResourcesKey
    //   22	26	11	localConfiguration	Configuration
    // Exception table:
    //   from	to	target	type
    //   69	77	105	finally
    //   77	97	110	finally
    //   108	110	110	finally
    //   106	108	114	finally
    //   67	69	118	finally
    //   0	14	122	finally
    //   19	31	122	finally
    //   37	54	122	finally
    //   62	67	122	finally
  }
  
  public void dump(String paramString, PrintWriter paramPrintWriter)
  {
    try
    {
      IndentingPrintWriter localIndentingPrintWriter = new com/android/internal/util/IndentingPrintWriter;
      localIndentingPrintWriter.<init>(paramPrintWriter, "  ");
      for (int i = 0; i < paramString.length() / 2; i++) {
        localIndentingPrintWriter.increaseIndent();
      }
      localIndentingPrintWriter.println("ResourcesManager:");
      localIndentingPrintWriter.increaseIndent();
      localIndentingPrintWriter.print("cached apks: total=");
      localIndentingPrintWriter.print(mLoadedApkAssets.size());
      localIndentingPrintWriter.print(" created=");
      localIndentingPrintWriter.print(mLoadedApkAssets.createCount());
      localIndentingPrintWriter.print(" evicted=");
      localIndentingPrintWriter.print(mLoadedApkAssets.evictionCount());
      localIndentingPrintWriter.print(" hit=");
      localIndentingPrintWriter.print(mLoadedApkAssets.hitCount());
      localIndentingPrintWriter.print(" miss=");
      localIndentingPrintWriter.print(mLoadedApkAssets.missCount());
      localIndentingPrintWriter.print(" max=");
      localIndentingPrintWriter.print(mLoadedApkAssets.maxSize());
      localIndentingPrintWriter.println();
      localIndentingPrintWriter.print("total apks: ");
      localIndentingPrintWriter.println(countLiveReferences(mCachedApkAssets.values()));
      localIndentingPrintWriter.print("resources: ");
      i = countLiveReferences(mResourceReferences);
      paramString = mActivityResourceReferences.values().iterator();
      while (paramString.hasNext()) {
        i += countLiveReferences(nextactivityResources);
      }
      localIndentingPrintWriter.println(i);
      localIndentingPrintWriter.print("resource impls: ");
      localIndentingPrintWriter.println(countLiveReferences(mResourceImpls.values()));
      return;
    }
    finally {}
  }
  
  public Display getAdjustedDisplay(int paramInt, Resources paramResources)
  {
    try
    {
      DisplayManagerGlobal localDisplayManagerGlobal = DisplayManagerGlobal.getInstance();
      if (localDisplayManagerGlobal == null) {
        return null;
      }
      paramResources = localDisplayManagerGlobal.getCompatibleDisplay(paramInt, paramResources);
      return paramResources;
    }
    finally {}
  }
  
  public Configuration getConfiguration()
  {
    try
    {
      Configuration localConfiguration = mResConfiguration;
      return localConfiguration;
    }
    finally {}
  }
  
  DisplayMetrics getDisplayMetrics()
  {
    return getDisplayMetrics(0, DisplayAdjustments.DEFAULT_DISPLAY_ADJUSTMENTS);
  }
  
  @VisibleForTesting
  protected DisplayMetrics getDisplayMetrics(int paramInt, DisplayAdjustments paramDisplayAdjustments)
  {
    DisplayMetrics localDisplayMetrics = new DisplayMetrics();
    paramDisplayAdjustments = getAdjustedDisplay(paramInt, paramDisplayAdjustments);
    if (paramDisplayAdjustments != null) {
      paramDisplayAdjustments.getMetrics(localDisplayMetrics);
    } else {
      localDisplayMetrics.setToDefaults();
    }
    return localDisplayMetrics;
  }
  
  public Resources getResources(IBinder paramIBinder, String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, int paramInt, Configuration paramConfiguration, CompatibilityInfo paramCompatibilityInfo, ClassLoader paramClassLoader)
  {
    try
    {
      Trace.traceBegin(8192L, "ResourcesManager#getResources");
      ResourcesKey localResourcesKey = new android/content/res/ResourcesKey;
      if (paramConfiguration != null)
      {
        Configuration localConfiguration = new android/content/res/Configuration;
        localConfiguration.<init>(paramConfiguration);
        paramConfiguration = localConfiguration;
      }
      else
      {
        paramConfiguration = null;
      }
      localResourcesKey.<init>(paramString, paramArrayOfString1, paramArrayOfString2, paramArrayOfString3, paramInt, paramConfiguration, paramCompatibilityInfo);
      if (paramClassLoader != null) {
        paramString = paramClassLoader;
      } else {
        paramString = ClassLoader.getSystemClassLoader();
      }
      try
      {
        paramIBinder = getOrCreateResources(paramIBinder, localResourcesKey, paramString);
        Trace.traceEnd(8192L);
        return paramIBinder;
      }
      finally {}
      Trace.traceEnd(8192L);
    }
    finally {}
    throw paramIBinder;
  }
  
  public void invalidatePath(String paramString)
  {
    int i = 0;
    int j = 0;
    try
    {
      while (j < mResourceImpls.size())
      {
        localObject = (ResourcesKey)mResourceImpls.keyAt(j);
        if (((ResourcesKey)localObject).isPathReferenced(paramString))
        {
          cleanupResourceImpl((ResourcesKey)localObject);
          i++;
        }
        else
        {
          j++;
        }
      }
      Object localObject = new java/lang/StringBuilder;
      ((StringBuilder)localObject).<init>();
      ((StringBuilder)localObject).append("Invalidated ");
      ((StringBuilder)localObject).append(i);
      ((StringBuilder)localObject).append(" asset managers that referenced ");
      ((StringBuilder)localObject).append(paramString);
      Log.i("ResourcesManager", ((StringBuilder)localObject).toString());
      return;
    }
    finally {}
  }
  
  boolean isSameResourcesOverrideConfig(IBinder paramIBinder, Configuration paramConfiguration)
  {
    if (paramIBinder != null) {
      try
      {
        paramIBinder = (ActivityResources)mActivityResourceReferences.get(paramIBinder);
      }
      finally
      {
        break label98;
      }
    } else {
      paramIBinder = null;
    }
    boolean bool1 = false;
    boolean bool2 = false;
    if (paramIBinder == null)
    {
      if (paramConfiguration == null) {
        bool2 = true;
      }
      return bool2;
    }
    if ((!Objects.equals(overrideConfig, paramConfiguration)) && ((paramConfiguration == null) || (overrideConfig == null) || (paramConfiguration.diffPublicOnly(overrideConfig) != 0))) {
      bool2 = bool1;
    } else {
      bool2 = true;
    }
    return bool2;
    label98:
    throw paramIBinder;
  }
  
  /* Error */
  public void updateResourcesForActivity(IBinder paramIBinder, Configuration paramConfiguration, int paramInt, boolean paramBoolean)
  {
    // Byte code:
    //   0: ldc2_w 495
    //   3: ldc_w 707
    //   6: invokestatic 504	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   9: aload_0
    //   10: monitorenter
    //   11: aload_0
    //   12: aload_1
    //   13: invokespecial 315	android/app/ResourcesManager:getOrCreateActivityResourcesStructLocked	(Landroid/os/IBinder;)Landroid/app/ResourcesManager$ActivityResources;
    //   16: astore_1
    //   17: aload_1
    //   18: getfield 327	android/app/ResourcesManager$ActivityResources:overrideConfig	Landroid/content/res/Configuration;
    //   21: aload_2
    //   22: invokestatic 359	java/util/Objects:equals	(Ljava/lang/Object;Ljava/lang/Object;)Z
    //   25: ifeq +17 -> 42
    //   28: iload 4
    //   30: ifne +12 -> 42
    //   33: aload_0
    //   34: monitorexit
    //   35: ldc2_w 495
    //   38: invokestatic 511	android/os/Trace:traceEnd	(J)V
    //   41: return
    //   42: new 60	android/content/res/Configuration
    //   45: astore 5
    //   47: aload 5
    //   49: aload_1
    //   50: getfield 327	android/app/ResourcesManager$ActivityResources:overrideConfig	Landroid/content/res/Configuration;
    //   53: invokespecial 261	android/content/res/Configuration:<init>	(Landroid/content/res/Configuration;)V
    //   56: aload_2
    //   57: ifnull +14 -> 71
    //   60: aload_1
    //   61: getfield 327	android/app/ResourcesManager$ActivityResources:overrideConfig	Landroid/content/res/Configuration;
    //   64: aload_2
    //   65: invokevirtual 337	android/content/res/Configuration:setTo	(Landroid/content/res/Configuration;)V
    //   68: goto +10 -> 78
    //   71: aload_1
    //   72: getfield 327	android/app/ResourcesManager$ActivityResources:overrideConfig	Landroid/content/res/Configuration;
    //   75: invokevirtual 710	android/content/res/Configuration:unset	()V
    //   78: aload_1
    //   79: getfield 327	android/app/ResourcesManager$ActivityResources:overrideConfig	Landroid/content/res/Configuration;
    //   82: getstatic 330	android/content/res/Configuration:EMPTY	Landroid/content/res/Configuration;
    //   85: invokevirtual 334	android/content/res/Configuration:equals	(Landroid/content/res/Configuration;)Z
    //   88: istore 4
    //   90: aload_1
    //   91: getfield 318	android/app/ResourcesManager$ActivityResources:activityResources	Ljava/util/ArrayList;
    //   94: invokevirtual 346	java/util/ArrayList:size	()I
    //   97: istore 6
    //   99: iconst_0
    //   100: istore 7
    //   102: iload 7
    //   104: iload 6
    //   106: if_icmpge +274 -> 380
    //   109: aload_1
    //   110: getfield 318	android/app/ResourcesManager$ActivityResources:activityResources	Ljava/util/ArrayList;
    //   113: iload 7
    //   115: invokevirtual 348	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   118: checkcast 151	java/lang/ref/WeakReference
    //   121: invokevirtual 155	java/lang/ref/WeakReference:get	()Ljava/lang/Object;
    //   124: checkcast 350	android/content/res/Resources
    //   127: astore 8
    //   129: aload 8
    //   131: ifnonnull +6 -> 137
    //   134: goto +240 -> 374
    //   137: aload_0
    //   138: aload 8
    //   140: invokevirtual 363	android/content/res/Resources:getImpl	()Landroid/content/res/ResourcesImpl;
    //   143: invokespecial 712	android/app/ResourcesManager:findKeyForResourceImplLocked	(Landroid/content/res/ResourcesImpl;)Landroid/content/res/ResourcesKey;
    //   146: astore 9
    //   148: aload 9
    //   150: ifnonnull +47 -> 197
    //   153: new 409	java/lang/StringBuilder
    //   156: astore 10
    //   158: aload 10
    //   160: invokespecial 410	java/lang/StringBuilder:<init>	()V
    //   163: aload 10
    //   165: ldc_w 714
    //   168: invokevirtual 416	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   171: pop
    //   172: aload 10
    //   174: aload 8
    //   176: invokevirtual 363	android/content/res/Resources:getImpl	()Landroid/content/res/ResourcesImpl;
    //   179: invokevirtual 717	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   182: pop
    //   183: ldc 19
    //   185: aload 10
    //   187: invokevirtual 432	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   190: invokestatic 720	android/util/Slog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   193: pop
    //   194: goto -60 -> 134
    //   197: new 60	android/content/res/Configuration
    //   200: astore 10
    //   202: aload 10
    //   204: invokespecial 61	android/content/res/Configuration:<init>	()V
    //   207: aload_2
    //   208: ifnull +9 -> 217
    //   211: aload 10
    //   213: aload_2
    //   214: invokevirtual 337	android/content/res/Configuration:setTo	(Landroid/content/res/Configuration;)V
    //   217: iload 4
    //   219: iconst_1
    //   220: ixor
    //   221: ifeq +27 -> 248
    //   224: aload 9
    //   226: invokevirtual 256	android/content/res/ResourcesKey:hasOverrideConfiguration	()Z
    //   229: ifeq +19 -> 248
    //   232: aload 10
    //   234: aload 5
    //   236: aload 9
    //   238: getfield 188	android/content/res/ResourcesKey:mOverrideConfiguration	Landroid/content/res/Configuration;
    //   241: invokestatic 724	android/content/res/Configuration:generateDelta	(Landroid/content/res/Configuration;Landroid/content/res/Configuration;)Landroid/content/res/Configuration;
    //   244: invokevirtual 267	android/content/res/Configuration:updateFrom	(Landroid/content/res/Configuration;)I
    //   247: pop
    //   248: new 185	android/content/res/ResourcesKey
    //   251: astore 11
    //   253: aload 11
    //   255: aload 9
    //   257: getfield 456	android/content/res/ResourcesKey:mResDir	Ljava/lang/String;
    //   260: aload 9
    //   262: getfield 473	android/content/res/ResourcesKey:mSplitResDirs	[Ljava/lang/String;
    //   265: aload 9
    //   267: getfield 476	android/content/res/ResourcesKey:mOverlayDirs	[Ljava/lang/String;
    //   270: aload 9
    //   272: getfield 460	android/content/res/ResourcesKey:mLibDirs	[Ljava/lang/String;
    //   275: iload_3
    //   276: aload 10
    //   278: aload 9
    //   280: getfield 194	android/content/res/ResourcesKey:mCompatInfo	Landroid/content/res/CompatibilityInfo;
    //   283: invokespecial 479	android/content/res/ResourcesKey:<init>	(Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;[Ljava/lang/String;ILandroid/content/res/Configuration;Landroid/content/res/CompatibilityInfo;)V
    //   286: aload_0
    //   287: aload 11
    //   289: invokespecial 233	android/app/ResourcesManager:findResourcesImplForKeyLocked	(Landroid/content/res/ResourcesKey;)Landroid/content/res/ResourcesImpl;
    //   292: astore 9
    //   294: aload 9
    //   296: astore 10
    //   298: aload 9
    //   300: ifnonnull +52 -> 352
    //   303: aload_0
    //   304: aload 11
    //   306: invokespecial 235	android/app/ResourcesManager:createResourcesImpl	(Landroid/content/res/ResourcesKey;)Landroid/content/res/ResourcesImpl;
    //   309: astore 9
    //   311: aload 9
    //   313: astore 10
    //   315: aload 9
    //   317: ifnull +35 -> 352
    //   320: aload_0
    //   321: getfield 68	android/app/ResourcesManager:mResourceImpls	Landroid/util/ArrayMap;
    //   324: astore 12
    //   326: new 151	java/lang/ref/WeakReference
    //   329: astore 10
    //   331: aload 10
    //   333: aload 9
    //   335: invokespecial 238	java/lang/ref/WeakReference:<init>	(Ljava/lang/Object;)V
    //   338: aload 12
    //   340: aload 11
    //   342: aload 10
    //   344: invokevirtual 242	android/util/ArrayMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   347: pop
    //   348: aload 9
    //   350: astore 10
    //   352: aload 10
    //   354: ifnull +20 -> 374
    //   357: aload 10
    //   359: aload 8
    //   361: invokevirtual 363	android/content/res/Resources:getImpl	()Landroid/content/res/ResourcesImpl;
    //   364: if_acmpeq +10 -> 374
    //   367: aload 8
    //   369: aload 10
    //   371: invokevirtual 378	android/content/res/Resources:setImpl	(Landroid/content/res/ResourcesImpl;)V
    //   374: iinc 7 1
    //   377: goto -275 -> 102
    //   380: aload_0
    //   381: monitorexit
    //   382: ldc2_w 495
    //   385: invokestatic 511	android/os/Trace:traceEnd	(J)V
    //   388: return
    //   389: astore_1
    //   390: aload_0
    //   391: monitorexit
    //   392: aload_1
    //   393: athrow
    //   394: astore_1
    //   395: ldc2_w 495
    //   398: invokestatic 511	android/os/Trace:traceEnd	(J)V
    //   401: aload_1
    //   402: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	403	0	this	ResourcesManager
    //   0	403	1	paramIBinder	IBinder
    //   0	403	2	paramConfiguration	Configuration
    //   0	403	3	paramInt	int
    //   0	403	4	paramBoolean	boolean
    //   45	190	5	localConfiguration	Configuration
    //   97	10	6	i	int
    //   100	275	7	j	int
    //   127	241	8	localResources	Resources
    //   146	203	9	localObject1	Object
    //   156	214	10	localObject2	Object
    //   251	90	11	localResourcesKey	ResourcesKey
    //   324	15	12	localArrayMap	ArrayMap
    // Exception table:
    //   from	to	target	type
    //   11	28	389	finally
    //   33	35	389	finally
    //   42	56	389	finally
    //   60	68	389	finally
    //   71	78	389	finally
    //   78	99	389	finally
    //   109	129	389	finally
    //   137	148	389	finally
    //   153	194	389	finally
    //   197	207	389	finally
    //   211	217	389	finally
    //   224	248	389	finally
    //   248	294	389	finally
    //   303	311	389	finally
    //   320	348	389	finally
    //   357	374	389	finally
    //   380	382	389	finally
    //   390	392	389	finally
    //   0	11	394	finally
    //   392	394	394	finally
  }
  
  private static class ActivityResources
  {
    public final ArrayList<WeakReference<Resources>> activityResources = new ArrayList();
    public final Configuration overrideConfig = new Configuration();
    
    private ActivityResources() {}
  }
  
  private static class ApkKey
  {
    public final boolean overlay;
    public final String path;
    public final boolean sharedLib;
    
    ApkKey(String paramString, boolean paramBoolean1, boolean paramBoolean2)
    {
      path = paramString;
      sharedLib = paramBoolean1;
      overlay = paramBoolean2;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool1 = paramObject instanceof ApkKey;
      boolean bool2 = false;
      if (!bool1) {
        return false;
      }
      paramObject = (ApkKey)paramObject;
      bool1 = bool2;
      if (path.equals(path))
      {
        bool1 = bool2;
        if (sharedLib == sharedLib)
        {
          bool1 = bool2;
          if (overlay == overlay) {
            bool1 = true;
          }
        }
      }
      return bool1;
    }
    
    public int hashCode()
    {
      return 31 * (31 * (31 * 1 + path.hashCode()) + Boolean.hashCode(sharedLib)) + Boolean.hashCode(overlay);
    }
  }
}
