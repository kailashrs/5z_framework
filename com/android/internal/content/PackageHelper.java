package com.android.internal.content;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInstaller.SessionParams;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageParser.PackageLite;
import android.content.pm.dex.DexMetadataHelper;
import android.content.res.Resources;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.storage.IStorageManager;
import android.os.storage.IStorageManager.Stub;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.os.storage.VolumeInfo;
import android.provider.Settings.Global;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import libcore.io.IoUtils;

public class PackageHelper
{
  public static final int APP_INSTALL_AUTO = 0;
  public static final int APP_INSTALL_EXTERNAL = 2;
  public static final int APP_INSTALL_INTERNAL = 1;
  public static final int RECOMMEND_FAILED_ALREADY_EXISTS = -4;
  public static final int RECOMMEND_FAILED_INSUFFICIENT_STORAGE = -1;
  public static final int RECOMMEND_FAILED_INVALID_APK = -2;
  public static final int RECOMMEND_FAILED_INVALID_LOCATION = -3;
  public static final int RECOMMEND_FAILED_INVALID_URI = -6;
  public static final int RECOMMEND_FAILED_VERSION_DOWNGRADE = -7;
  public static final int RECOMMEND_INSTALL_EPHEMERAL = 3;
  public static final int RECOMMEND_INSTALL_EXTERNAL = 2;
  public static final int RECOMMEND_INSTALL_INTERNAL = 1;
  public static final int RECOMMEND_MEDIA_UNAVAILABLE = -5;
  private static final String TAG = "PackageHelper";
  private static TestableInterface sDefaultTestableInterface = null;
  
  public PackageHelper() {}
  
  public static long calculateInstalledSize(PackageParser.PackageLite paramPackageLite, NativeLibraryHelper.Handle paramHandle, String paramString)
    throws IOException
  {
    long l = 0L;
    Iterator localIterator = paramPackageLite.getAllCodePaths().iterator();
    while (localIterator.hasNext()) {
      l += new File((String)localIterator.next()).length();
    }
    return l + DexMetadataHelper.getPackageDexMetadataSize(paramPackageLite) + NativeLibraryHelper.sumNativeBinariesWithOverride(paramHandle, paramString);
  }
  
  public static long calculateInstalledSize(PackageParser.PackageLite paramPackageLite, String paramString)
    throws IOException
  {
    return calculateInstalledSize(paramPackageLite, paramString, null);
  }
  
  public static long calculateInstalledSize(PackageParser.PackageLite paramPackageLite, String paramString, FileDescriptor paramFileDescriptor)
    throws IOException
  {
    FileDescriptor localFileDescriptor = null;
    if (paramFileDescriptor != null) {
      try
      {
        paramFileDescriptor = NativeLibraryHelper.Handle.createFd(paramPackageLite, paramFileDescriptor);
      }
      finally
      {
        break label41;
      }
    } else {
      paramFileDescriptor = NativeLibraryHelper.Handle.create(paramPackageLite);
    }
    localFileDescriptor = paramFileDescriptor;
    long l = calculateInstalledSize(paramPackageLite, paramFileDescriptor, paramString);
    IoUtils.closeQuietly(paramFileDescriptor);
    return l;
    label41:
    IoUtils.closeQuietly(localFileDescriptor);
    throw paramPackageLite;
  }
  
  @Deprecated
  public static long calculateInstalledSize(PackageParser.PackageLite paramPackageLite, boolean paramBoolean, NativeLibraryHelper.Handle paramHandle, String paramString)
    throws IOException
  {
    return calculateInstalledSize(paramPackageLite, paramHandle, paramString);
  }
  
  @Deprecated
  public static long calculateInstalledSize(PackageParser.PackageLite paramPackageLite, boolean paramBoolean, String paramString)
    throws IOException
  {
    return calculateInstalledSize(paramPackageLite, paramString);
  }
  
  public static boolean fitsOnExternal(Context paramContext, PackageInstaller.SessionParams paramSessionParams)
  {
    StorageManager localStorageManager = (StorageManager)paramContext.getSystemService(StorageManager.class);
    paramContext = localStorageManager.getPrimaryVolume();
    boolean bool;
    if ((sizeBytes > 0L) && (!paramContext.isEmulated()) && ("mounted".equals(paramContext.getState())) && (sizeBytes <= localStorageManager.getStorageBytesUntilLow(paramContext.getPathFile()))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean fitsOnInternal(Context paramContext, PackageInstaller.SessionParams paramSessionParams)
    throws IOException
  {
    paramContext = (StorageManager)paramContext.getSystemService(StorageManager.class);
    UUID localUUID = paramContext.getUuidForPath(Environment.getDataDirectory());
    boolean bool;
    if (sizeBytes <= paramContext.getAllocatableBytes(localUUID, translateAllocateFlags(installFlags))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static TestableInterface getDefaultTestableInterface()
  {
    try
    {
      if (sDefaultTestableInterface == null)
      {
        localObject1 = new com/android/internal/content/PackageHelper$1;
        ((1)localObject1).<init>();
        sDefaultTestableInterface = (TestableInterface)localObject1;
      }
      Object localObject1 = sDefaultTestableInterface;
      return localObject1;
    }
    finally {}
  }
  
  public static IStorageManager getStorageManager()
    throws RemoteException
  {
    IBinder localIBinder = ServiceManager.getService("mount");
    if (localIBinder != null) {
      return IStorageManager.Stub.asInterface(localIBinder);
    }
    Log.e("PackageHelper", "Can't get storagemanager service");
    throw new RemoteException("Could not contact storagemanager service");
  }
  
  public static String replaceEnd(String paramString1, String paramString2, String paramString3)
  {
    if (paramString1.endsWith(paramString2))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString1.substring(0, paramString1.length() - paramString2.length()));
      localStringBuilder.append(paramString3);
      return localStringBuilder.toString();
    }
    paramString3 = new StringBuilder();
    paramString3.append("Expected ");
    paramString3.append(paramString1);
    paramString3.append(" to end with ");
    paramString3.append(paramString2);
    throw new IllegalArgumentException(paramString3.toString());
  }
  
  public static int resolveInstallLocation(Context paramContext, PackageInstaller.SessionParams paramSessionParams)
    throws IOException
  {
    Object localObject = null;
    PackageManager localPackageManager = paramContext.getPackageManager();
    try
    {
      ApplicationInfo localApplicationInfo = localPackageManager.getApplicationInfo(appPackageName, 4194304);
      localObject = localApplicationInfo;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException) {}
    int i = 0;
    int j = 0;
    int k = installFlags;
    int m = 1;
    boolean bool1;
    if ((k & 0x800) != 0)
    {
      k = 1;
      j = 1;
      bool1 = false;
    }
    for (;;)
    {
      break;
      if ((installFlags & 0x10) != 0)
      {
        k = 1;
        bool1 = false;
      }
      else if ((installFlags & 0x8) != 0)
      {
        k = 2;
        bool1 = false;
      }
      else if (installLocation == 1)
      {
        k = 1;
        bool1 = false;
      }
      else if ((installLocation == 2) && (!localPackageManager.isInApp2sdBlacklist(appPackageName)))
      {
        k = 2;
        bool1 = true;
      }
      else if (installLocation == 0)
      {
        if (localObject != null)
        {
          if ((flags & 0x40000) != 0) {}
          for (k = 2;; k = 1) {
            break;
          }
        }
        k = 1;
        bool1 = true;
      }
      else
      {
        k = 1;
        bool1 = false;
        j = i;
      }
    }
    boolean bool2 = false;
    if ((bool1) || (k == 1)) {
      bool2 = fitsOnInternal(paramContext, paramSessionParams);
    }
    boolean bool3 = false;
    if ((bool1) || (k == 2)) {
      bool3 = fitsOnExternal(paramContext, paramSessionParams);
    }
    paramContext = new StringBuilder();
    paramContext.append("resolveInstallLocation\n");
    paramContext.append(appPackageName);
    paramContext.append("(");
    paramContext.append(installLocation);
    paramContext.append("/");
    paramContext.append(installFlags);
    paramContext.append("):\n prefer=");
    paramContext.append(k);
    paramContext.append(" checkBoth =");
    paramContext.append(bool1);
    paramContext.append(" fitsOnInternal=");
    paramContext.append(bool2);
    paramContext.append(" fitsOnExternal=");
    paramContext.append(bool3);
    Log.i("PackageHelper", paramContext.toString());
    if (k == 1)
    {
      if (bool2)
      {
        if (j != 0) {
          k = 3;
        } else {
          k = m;
        }
        return k;
      }
    }
    else if ((k == 2) && (bool3)) {
      return 2;
    }
    if (bool1)
    {
      if (bool2) {
        return 1;
      }
      if (bool3) {
        return 2;
      }
    }
    return -1;
  }
  
  @Deprecated
  public static int resolveInstallLocation(Context paramContext, String paramString, int paramInt1, long paramLong, int paramInt2)
  {
    PackageInstaller.SessionParams localSessionParams = new PackageInstaller.SessionParams(-1);
    appPackageName = paramString;
    installLocation = paramInt1;
    sizeBytes = paramLong;
    installFlags = paramInt2;
    try
    {
      paramInt1 = resolveInstallLocation(paramContext, localSessionParams);
      return paramInt1;
    }
    catch (IOException paramContext)
    {
      throw new IllegalStateException(paramContext);
    }
  }
  
  public static String resolveInstallVolume(Context paramContext, PackageInstaller.SessionParams paramSessionParams)
    throws IOException
  {
    TestableInterface localTestableInterface = getDefaultTestableInterface();
    return resolveInstallVolume(paramContext, appPackageName, installLocation, sizeBytes, localTestableInterface);
  }
  
  @VisibleForTesting
  public static String resolveInstallVolume(Context paramContext, PackageInstaller.SessionParams paramSessionParams, TestableInterface paramTestableInterface)
    throws IOException
  {
    StorageManager localStorageManager = paramTestableInterface.getStorageManager(paramContext);
    boolean bool1 = paramTestableInterface.getForceAllowOnExternalSetting(paramContext);
    boolean bool2 = paramTestableInterface.getAllow3rdPartyOnInternalConfig(paramContext);
    ApplicationInfo localApplicationInfo = paramTestableInterface.getExistingAppInfo(paramContext, appPackageName);
    ArraySet localArraySet = new ArraySet();
    int i = 0;
    TestableInterface localTestableInterface = null;
    long l1 = Long.MIN_VALUE;
    Iterator localIterator = localStorageManager.getVolumes().iterator();
    while (localIterator.hasNext())
    {
      VolumeInfo localVolumeInfo = (VolumeInfo)localIterator.next();
      int j;
      long l3;
      if ((type == 1) && (localVolumeInfo.isMountedWritable()))
      {
        boolean bool3 = "private".equals(id);
        long l2 = localStorageManager.getAllocatableBytes(localStorageManager.getUuidForPath(new File(path)), translateAllocateFlags(installFlags));
        if (bool3) {
          if (sizeBytes <= l2) {
            i = 1;
          } else {
            i = 0;
          }
        }
        if (bool3)
        {
          j = i;
          paramTestableInterface = localTestableInterface;
          l3 = l1;
          if (!bool2) {}
        }
        else
        {
          if (l2 >= sizeBytes) {
            localArraySet.add(fsUuid);
          }
          j = i;
          paramTestableInterface = localTestableInterface;
          l3 = l1;
          if (l2 >= l1)
          {
            l3 = l2;
            paramTestableInterface = localVolumeInfo;
            j = i;
          }
        }
      }
      else
      {
        l3 = l1;
        paramTestableInterface = localTestableInterface;
        j = i;
      }
      i = j;
      localTestableInterface = paramTestableInterface;
      l1 = l3;
    }
    if ((localApplicationInfo != null) && (localApplicationInfo.isSystemApp()))
    {
      if (i != 0) {
        return StorageManager.UUID_PRIVATE_INTERNAL;
      }
      paramContext = new StringBuilder();
      paramContext.append("Not enough space on existing volume ");
      paramContext.append(volumeUuid);
      paramContext.append(" for system app ");
      paramContext.append(appPackageName);
      paramContext.append(" upgrade");
      throw new IOException(paramContext.toString());
    }
    if ((!bool1) && (installLocation == 1))
    {
      if ((localApplicationInfo != null) && (!Objects.equals(volumeUuid, StorageManager.UUID_PRIVATE_INTERNAL)))
      {
        paramContext = new StringBuilder();
        paramContext.append("Cannot automatically move ");
        paramContext.append(appPackageName);
        paramContext.append(" from ");
        paramContext.append(volumeUuid);
        paramContext.append(" to internal storage");
        throw new IOException(paramContext.toString());
      }
      if (bool2)
      {
        if (i != 0) {
          return StorageManager.UUID_PRIVATE_INTERNAL;
        }
        throw new IOException("Requested internal only, but not enough space");
      }
      throw new IOException("Not allowed to install non-system apps on internal storage");
    }
    if (localApplicationInfo != null)
    {
      if ((Objects.equals(volumeUuid, StorageManager.UUID_PRIVATE_INTERNAL)) && (i != 0)) {
        return StorageManager.UUID_PRIVATE_INTERNAL;
      }
      if (localArraySet.contains(volumeUuid)) {
        return volumeUuid;
      }
      paramContext = new StringBuilder();
      paramContext.append("Not enough space on existing volume ");
      paramContext.append(volumeUuid);
      paramContext.append(" for ");
      paramContext.append(appPackageName);
      paramContext.append(" upgrade");
      throw new IOException(paramContext.toString());
    }
    if ((localTestableInterface != null) && (!paramContext.getPackageManager().isInApp2sdBlacklist(appPackageName))) {
      return fsUuid;
    }
    paramContext = new StringBuilder();
    paramContext.append("No special requests, but no room on allowed volumes.  allow3rdPartyOnInternal? ");
    paramContext.append(bool2);
    throw new IOException(paramContext.toString());
  }
  
  @Deprecated
  @VisibleForTesting
  public static String resolveInstallVolume(Context paramContext, String paramString, int paramInt, long paramLong, TestableInterface paramTestableInterface)
    throws IOException
  {
    PackageInstaller.SessionParams localSessionParams = new PackageInstaller.SessionParams(-1);
    appPackageName = paramString;
    installLocation = paramInt;
    sizeBytes = paramLong;
    return resolveInstallVolume(paramContext, localSessionParams, paramTestableInterface);
  }
  
  public static int translateAllocateFlags(int paramInt)
  {
    if ((0x8000 & paramInt) != 0) {
      return 1;
    }
    return 0;
  }
  
  public static abstract class TestableInterface
  {
    public TestableInterface() {}
    
    public abstract boolean getAllow3rdPartyOnInternalConfig(Context paramContext);
    
    public abstract File getDataDirectory();
    
    public abstract ApplicationInfo getExistingAppInfo(Context paramContext, String paramString);
    
    public abstract boolean getForceAllowOnExternalSetting(Context paramContext);
    
    public abstract StorageManager getStorageManager(Context paramContext);
  }
}
