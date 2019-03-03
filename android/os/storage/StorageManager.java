package android.os.storage;

import android.annotation.SuppressLint;
import android.annotation.SystemApi;
import android.app.ActivityThread;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.IPackageMoveObserver;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IVoldTaskListener;
import android.os.IVoldTaskListener.Stub;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.ParcelableException;
import android.os.PersistableBundle;
import android.os.Process;
import android.os.ProxyFileDescriptorCallback;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceManager.ServiceNotFoundException;
import android.os.SystemProperties;
import android.provider.Settings.Global;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructStat;
import android.text.TextUtils;
import android.util.DataUnit;
import android.util.Log;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.FuseAppLoop;
import com.android.internal.os.RoSystemProperties;
import com.android.internal.os.SomeArgs;
import com.android.internal.util.Preconditions;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class StorageManager
{
  public static final String ACTION_MANAGE_STORAGE = "android.os.storage.action.MANAGE_STORAGE";
  public static final int CRYPT_TYPE_DEFAULT = 1;
  public static final int CRYPT_TYPE_PASSWORD = 0;
  public static final int CRYPT_TYPE_PATTERN = 2;
  public static final int CRYPT_TYPE_PIN = 3;
  public static final int DEBUG_ADOPTABLE_FORCE_OFF = 2;
  public static final int DEBUG_ADOPTABLE_FORCE_ON = 1;
  public static final int DEBUG_EMULATE_FBE = 4;
  public static final int DEBUG_SDCARDFS_FORCE_OFF = 16;
  public static final int DEBUG_SDCARDFS_FORCE_ON = 8;
  public static final int DEBUG_VIRTUAL_DISK = 32;
  private static final long DEFAULT_CACHE_MAX_BYTES = DataUnit.GIBIBYTES.toBytes(5L);
  private static final int DEFAULT_CACHE_PERCENTAGE = 10;
  private static final long DEFAULT_FULL_THRESHOLD_BYTES = DataUnit.MEBIBYTES.toBytes(1L);
  private static final long DEFAULT_THRESHOLD_MAX_BYTES;
  private static final int DEFAULT_THRESHOLD_PERCENTAGE = 5;
  public static final int ENCRYPTION_STATE_ERROR_CORRUPT = -4;
  public static final int ENCRYPTION_STATE_ERROR_INCOMPLETE = -2;
  public static final int ENCRYPTION_STATE_ERROR_INCONSISTENT = -3;
  public static final int ENCRYPTION_STATE_ERROR_UNKNOWN = -1;
  public static final int ENCRYPTION_STATE_NONE = 1;
  public static final int ENCRYPTION_STATE_OK = 0;
  public static final String EXTRA_REQUESTED_BYTES = "android.os.storage.extra.REQUESTED_BYTES";
  public static final String EXTRA_UUID = "android.os.storage.extra.UUID";
  @SystemApi
  public static final int FLAG_ALLOCATE_AGGRESSIVE = 1;
  public static final int FLAG_ALLOCATE_DEFY_ALL_RESERVED = 2;
  public static final int FLAG_ALLOCATE_DEFY_HALF_RESERVED = 4;
  public static final int FLAG_FOR_WRITE = 256;
  public static final int FLAG_INCLUDE_INVISIBLE = 1024;
  public static final int FLAG_REAL_STATE = 512;
  public static final int FLAG_STORAGE_CE = 2;
  public static final int FLAG_STORAGE_DE = 1;
  public static final int FSTRIM_FLAG_DEEP = 1;
  public static final String OWNER_INFO_KEY = "OwnerInfo";
  public static final String PASSWORD_VISIBLE_KEY = "PasswordVisible";
  public static final String PATTERN_VISIBLE_KEY = "PatternVisible";
  public static final String PROP_ADOPTABLE = "persist.sys.adoptable";
  public static final String PROP_EMULATE_FBE = "persist.sys.emulate_fbe";
  public static final String PROP_HAS_ADOPTABLE = "vold.has_adoptable";
  public static final String PROP_HAS_RESERVED = "vold.has_reserved";
  public static final String PROP_PRIMARY_PHYSICAL = "ro.vold.primary_physical";
  public static final String PROP_SDCARDFS = "persist.sys.sdcardfs";
  public static final String PROP_VIRTUAL_DISK = "persist.sys.virtual_disk";
  public static final String SYSTEM_LOCALE_KEY = "SystemLocale";
  private static final String TAG = "StorageManager";
  public static final UUID UUID_DEFAULT;
  public static final String UUID_PRIMARY_PHYSICAL = "primary_physical";
  public static final UUID UUID_PRIMARY_PHYSICAL_;
  public static final String UUID_PRIVATE_INTERNAL = null;
  public static final String UUID_SYSTEM = "system";
  public static final UUID UUID_SYSTEM_;
  private static final String XATTR_CACHE_GROUP = "user.cache_group";
  private static final String XATTR_CACHE_TOMBSTONE = "user.cache_tombstone";
  private static volatile IStorageManager sStorageManager;
  private final Context mContext;
  private final ArrayList<StorageEventListenerDelegate> mDelegates = new ArrayList();
  @GuardedBy("mFuseAppLoopLock")
  private FuseAppLoop mFuseAppLoop = null;
  private final Object mFuseAppLoopLock = new Object();
  private final Looper mLooper;
  private final AtomicInteger mNextNonce = new AtomicInteger(0);
  private final ObbActionListener mObbActionListener = new ObbActionListener(null);
  private final ContentResolver mResolver;
  private final IStorageManager mStorageManager;
  
  static
  {
    UUID_DEFAULT = UUID.fromString("41217664-9172-527a-b3d5-edabb50a7d69");
    UUID_PRIMARY_PHYSICAL_ = UUID.fromString("0f95a519-dae7-5abf-9519-fbd6209e05fd");
    UUID_SYSTEM_ = UUID.fromString("5d258386-e60d-59e3-826d-0089cdd42cc0");
    sStorageManager = null;
    DEFAULT_THRESHOLD_MAX_BYTES = DataUnit.MEBIBYTES.toBytes(500L);
  }
  
  public StorageManager(Context paramContext, Looper paramLooper)
    throws ServiceManager.ServiceNotFoundException
  {
    mContext = paramContext;
    mResolver = paramContext.getContentResolver();
    mLooper = paramLooper;
    mStorageManager = IStorageManager.Stub.asInterface(ServiceManager.getServiceOrThrow("mount"));
  }
  
  public static String convert(UUID paramUUID)
  {
    if (UUID_DEFAULT.equals(paramUUID)) {
      return UUID_PRIVATE_INTERNAL;
    }
    if (UUID_PRIMARY_PHYSICAL_.equals(paramUUID)) {
      return "primary_physical";
    }
    if (UUID_SYSTEM_.equals(paramUUID)) {
      return "system";
    }
    return paramUUID.toString();
  }
  
  public static UUID convert(String paramString)
  {
    if (Objects.equals(paramString, UUID_PRIVATE_INTERNAL)) {
      return UUID_DEFAULT;
    }
    if (Objects.equals(paramString, "primary_physical")) {
      return UUID_PRIMARY_PHYSICAL_;
    }
    if (Objects.equals(paramString, "system")) {
      return UUID_SYSTEM_;
    }
    return UUID.fromString(paramString);
  }
  
  @Deprecated
  public static StorageManager from(Context paramContext)
  {
    return (StorageManager)paramContext.getSystemService(StorageManager.class);
  }
  
  private int getNextNonce()
  {
    return mNextNonce.getAndIncrement();
  }
  
  public static Pair<String, Long> getPrimaryStoragePathAndSize()
  {
    return Pair.create(null, Long.valueOf(FileUtils.roundStorageSize(Environment.getDataDirectory().getTotalSpace() + Environment.getRootDirectory().getTotalSpace())));
  }
  
  public static StorageVolume getPrimaryVolume(StorageVolume[] paramArrayOfStorageVolume)
  {
    int i = paramArrayOfStorageVolume.length;
    for (int j = 0; j < i; j++)
    {
      StorageVolume localStorageVolume = paramArrayOfStorageVolume[j];
      if (localStorageVolume.isPrimary()) {
        return localStorageVolume;
      }
    }
    throw new IllegalStateException("Missing primary storage");
  }
  
  public static StorageVolume getStorageVolume(File paramFile, int paramInt)
  {
    return getStorageVolume(getVolumeList(paramInt, 0), paramFile);
  }
  
  private static StorageVolume getStorageVolume(StorageVolume[] paramArrayOfStorageVolume, File paramFile)
  {
    if (paramFile == null) {
      return null;
    }
    try
    {
      File localFile1 = paramFile.getCanonicalFile();
      int i = paramArrayOfStorageVolume.length;
      int j = 0;
      while (j < i)
      {
        paramFile = paramArrayOfStorageVolume[j];
        File localFile2 = paramFile.getPathFile();
        try
        {
          localFile2 = localFile2.getCanonicalFile();
          if (FileUtils.contains(localFile2, localFile1)) {
            return paramFile;
          }
        }
        catch (IOException paramFile)
        {
          j++;
        }
      }
      return null;
    }
    catch (IOException paramArrayOfStorageVolume)
    {
      paramArrayOfStorageVolume = new StringBuilder();
      paramArrayOfStorageVolume.append("Could not get canonical path for ");
      paramArrayOfStorageVolume.append(paramFile);
      Slog.d("StorageManager", paramArrayOfStorageVolume.toString());
    }
    return null;
  }
  
  public static StorageVolume[] getVolumeList(int paramInt1, int paramInt2)
  {
    IStorageManager localIStorageManager = IStorageManager.Stub.asInterface(ServiceManager.getService("mount"));
    try
    {
      String str = ActivityThread.currentOpPackageName();
      Object localObject = str;
      if (str == null)
      {
        localObject = ActivityThread.getPackageManager().getPackagesForUid(Process.myUid());
        if ((localObject != null) && (localObject.length > 0)) {
          localObject = localObject[0];
        } else {
          return new StorageVolume[0];
        }
      }
      paramInt1 = ActivityThread.getPackageManager().getPackageUid((String)localObject, 268435456, paramInt1);
      if (paramInt1 <= 0) {
        return new StorageVolume[0];
      }
      localObject = localIStorageManager.getVolumeList(paramInt1, (String)localObject, paramInt2);
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public static boolean hasAdoptable()
  {
    return SystemProperties.getBoolean("vold.has_adoptable", false);
  }
  
  public static boolean inCryptKeeperBounce()
  {
    return "trigger_restart_min_framework".equals(SystemProperties.get("vold.decrypt"));
  }
  
  public static boolean isBlockEncrypted()
  {
    if (!isEncrypted()) {
      return false;
    }
    return RoSystemProperties.CRYPTO_BLOCK_ENCRYPTED;
  }
  
  public static boolean isBlockEncrypting()
  {
    return "".equalsIgnoreCase(SystemProperties.get("vold.encrypt_progress", "")) ^ true;
  }
  
  private static boolean isCacheBehavior(File paramFile, String paramString)
    throws IOException
  {
    try
    {
      Os.getxattr(paramFile.getAbsolutePath(), paramString);
      return true;
    }
    catch (ErrnoException paramFile)
    {
      if (errno == OsConstants.ENODATA) {
        return false;
      }
      throw paramFile.rethrowAsIOException();
    }
  }
  
  public static boolean isEncryptable()
  {
    return RoSystemProperties.CRYPTO_ENCRYPTABLE;
  }
  
  public static boolean isEncrypted()
  {
    return RoSystemProperties.CRYPTO_ENCRYPTED;
  }
  
  public static boolean isFileEncryptedEmulatedOnly()
  {
    return SystemProperties.getBoolean("persist.sys.emulate_fbe", false);
  }
  
  public static boolean isFileEncryptedNativeOnly()
  {
    if (!isEncrypted()) {
      return false;
    }
    return RoSystemProperties.CRYPTO_FILE_ENCRYPTED;
  }
  
  public static boolean isFileEncryptedNativeOrEmulated()
  {
    boolean bool;
    if ((!isFileEncryptedNativeOnly()) && (!isFileEncryptedEmulatedOnly())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static boolean isNonDefaultBlockEncrypted()
  {
    boolean bool1 = isBlockEncrypted();
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    try
    {
      int i = IStorageManager.Stub.asInterface(ServiceManager.getService("mount")).getPasswordType();
      if (i != 1) {
        bool2 = true;
      }
      return bool2;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("StorageManager", "Error getting encryption type");
    }
    return false;
  }
  
  /* Error */
  public static boolean isUserKeyUnlocked(int paramInt)
  {
    // Byte code:
    //   0: getstatic 179	android/os/storage/StorageManager:sStorageManager	Landroid/os/storage/IStorageManager;
    //   3: ifnonnull +14 -> 17
    //   6: ldc -12
    //   8: invokestatic 389	android/os/ServiceManager:getService	(Ljava/lang/String;)Landroid/os/IBinder;
    //   11: invokestatic 256	android/os/storage/IStorageManager$Stub:asInterface	(Landroid/os/IBinder;)Landroid/os/storage/IStorageManager;
    //   14: putstatic 179	android/os/storage/StorageManager:sStorageManager	Landroid/os/storage/IStorageManager;
    //   17: getstatic 179	android/os/storage/StorageManager:sStorageManager	Landroid/os/storage/IStorageManager;
    //   20: ifnonnull +14 -> 34
    //   23: ldc 116
    //   25: ldc_w 523
    //   28: invokestatic 526	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   31: pop
    //   32: iconst_0
    //   33: ireturn
    //   34: invokestatic 531	android/os/Binder:clearCallingIdentity	()J
    //   37: lstore_1
    //   38: getstatic 179	android/os/storage/StorageManager:sStorageManager	Landroid/os/storage/IStorageManager;
    //   41: iload_0
    //   42: invokeinterface 533 2 0
    //   47: istore_3
    //   48: lload_1
    //   49: invokestatic 537	android/os/Binder:restoreCallingIdentity	(J)V
    //   52: iload_3
    //   53: ireturn
    //   54: astore 4
    //   56: goto +11 -> 67
    //   59: astore 4
    //   61: aload 4
    //   63: invokevirtual 540	android/os/RemoteException:rethrowAsRuntimeException	()Ljava/lang/RuntimeException;
    //   66: athrow
    //   67: lload_1
    //   68: invokestatic 537	android/os/Binder:restoreCallingIdentity	(J)V
    //   71: aload 4
    //   73: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	74	0	paramInt	int
    //   37	31	1	l	long
    //   47	6	3	bool	boolean
    //   54	1	4	localObject	Object
    //   59	13	4	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   38	48	54	finally
    //   61	67	54	finally
    //   38	48	59	android/os/RemoteException
  }
  
  public static File maybeTranslateEmulatedPathToInternal(File paramFile)
  {
    return paramFile;
  }
  
  private static void setCacheBehavior(File paramFile, String paramString, boolean paramBoolean)
    throws IOException
  {
    if (paramFile.isDirectory())
    {
      if (paramBoolean) {
        try
        {
          Os.setxattr(paramFile.getAbsolutePath(), paramString, "1".getBytes(StandardCharsets.UTF_8), 0);
        }
        catch (ErrnoException paramFile)
        {
          throw paramFile.rethrowAsIOException();
        }
      } else {
        try
        {
          Os.removexattr(paramFile.getAbsolutePath(), paramString);
        }
        catch (ErrnoException paramFile)
        {
          if (errno != OsConstants.ENODATA) {
            break label61;
          }
        }
      }
      return;
      label61:
      throw paramFile.rethrowAsIOException();
    }
    throw new IOException("Cache behavior can only be set on directories");
  }
  
  public void allocateBytes(FileDescriptor paramFileDescriptor, long paramLong)
    throws IOException
  {
    allocateBytes(paramFileDescriptor, paramLong, 0);
  }
  
  @SystemApi
  @SuppressLint({"Doclava125"})
  public void allocateBytes(FileDescriptor paramFileDescriptor, long paramLong, int paramInt)
    throws IOException
  {
    File localFile = ParcelFileDescriptor.getFile(paramFileDescriptor);
    UUID localUUID = getUuidForPath(localFile);
    int i = 0;
    while (i < 3) {
      try
      {
        long l = paramLong - fstatst_blocks * 512L;
        if (l > 0L) {
          allocateBytes(localUUID, l, paramInt);
        }
        try
        {
          Os.posix_fallocate(paramFileDescriptor, 0L, paramLong);
          return;
        }
        catch (ErrnoException localErrnoException1)
        {
          if ((errno != OsConstants.ENOSYS) && (errno != OsConstants.ENOTSUP)) {
            throw localErrnoException1;
          }
          Log.w("StorageManager", "fallocate() not supported; falling back to ftruncate()");
          Os.ftruncate(paramFileDescriptor, paramLong);
          return;
        }
      }
      catch (ErrnoException localErrnoException2)
      {
        if (errno == OsConstants.ENOSPC)
        {
          Log.w("StorageManager", "Odd, not enough space; let's try again?");
          i++;
        }
        else
        {
          throw localErrnoException2.rethrowAsIOException();
        }
      }
    }
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("Well this is embarassing; we can't allocate ");
    paramFileDescriptor.append(paramLong);
    paramFileDescriptor.append(" for ");
    paramFileDescriptor.append(localFile);
    throw new IOException(paramFileDescriptor.toString());
  }
  
  public void allocateBytes(UUID paramUUID, long paramLong)
    throws IOException
  {
    allocateBytes(paramUUID, paramLong, 0);
  }
  
  @SystemApi
  @SuppressLint({"Doclava125"})
  public void allocateBytes(UUID paramUUID, long paramLong, int paramInt)
    throws IOException
  {
    try
    {
      mStorageManager.allocateBytes(convert(paramUUID), paramLong, paramInt, mContext.getOpPackageName());
    }
    catch (RemoteException paramUUID)
    {
      throw paramUUID.rethrowFromSystemServer();
    }
    catch (ParcelableException paramUUID)
    {
      paramUUID.maybeRethrow(IOException.class);
    }
  }
  
  @Deprecated
  public long benchmark(String paramString)
  {
    final CompletableFuture localCompletableFuture = new CompletableFuture();
    benchmark(paramString, new IVoldTaskListener.Stub()
    {
      public void onFinished(int paramAnonymousInt, PersistableBundle paramAnonymousPersistableBundle)
      {
        localCompletableFuture.complete(paramAnonymousPersistableBundle);
      }
      
      public void onStatus(int paramAnonymousInt, PersistableBundle paramAnonymousPersistableBundle) {}
    });
    try
    {
      long l = ((PersistableBundle)localCompletableFuture.get(3L, TimeUnit.MINUTES)).getLong("run", Long.MAX_VALUE);
      return l * 1000000L;
    }
    catch (Exception paramString) {}
    return Long.MAX_VALUE;
  }
  
  public void benchmark(String paramString, IVoldTaskListener paramIVoldTaskListener)
  {
    try
    {
      mStorageManager.benchmark(paramString, paramIVoldTaskListener);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void createUserKey(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    try
    {
      mStorageManager.createUserKey(paramInt1, paramInt2, paramBoolean);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void destroyUserKey(int paramInt)
  {
    try
    {
      mStorageManager.destroyUserKey(paramInt);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void destroyUserStorage(String paramString, int paramInt1, int paramInt2)
  {
    try
    {
      mStorageManager.destroyUserStorage(paramString, paramInt1, paramInt2);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void disableUsbMassStorage() {}
  
  @Deprecated
  public void enableUsbMassStorage() {}
  
  public DiskInfo findDiskById(String paramString)
  {
    Preconditions.checkNotNull(paramString);
    Iterator localIterator = getDisks().iterator();
    while (localIterator.hasNext())
    {
      DiskInfo localDiskInfo = (DiskInfo)localIterator.next();
      if (Objects.equals(id, paramString)) {
        return localDiskInfo;
      }
    }
    return null;
  }
  
  public VolumeInfo findEmulatedForPrivate(VolumeInfo paramVolumeInfo)
  {
    if (paramVolumeInfo != null) {
      return findVolumeById(paramVolumeInfo.getId().replace("private", "emulated"));
    }
    return null;
  }
  
  public File findPathForUuid(String paramString)
    throws FileNotFoundException
  {
    Object localObject = findVolumeByQualifiedUuid(paramString);
    if (localObject != null) {
      return ((VolumeInfo)localObject).getPath();
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Failed to find a storage device for ");
    ((StringBuilder)localObject).append(paramString);
    throw new FileNotFoundException(((StringBuilder)localObject).toString());
  }
  
  public VolumeInfo findPrivateForEmulated(VolumeInfo paramVolumeInfo)
  {
    if (paramVolumeInfo != null) {
      return findVolumeById(paramVolumeInfo.getId().replace("emulated", "private"));
    }
    return null;
  }
  
  public VolumeRecord findRecordByUuid(String paramString)
  {
    Preconditions.checkNotNull(paramString);
    Iterator localIterator = getVolumeRecords().iterator();
    while (localIterator.hasNext())
    {
      VolumeRecord localVolumeRecord = (VolumeRecord)localIterator.next();
      if (Objects.equals(fsUuid, paramString)) {
        return localVolumeRecord;
      }
    }
    return null;
  }
  
  public VolumeInfo findVolumeById(String paramString)
  {
    Preconditions.checkNotNull(paramString);
    Iterator localIterator = getVolumes().iterator();
    while (localIterator.hasNext())
    {
      VolumeInfo localVolumeInfo = (VolumeInfo)localIterator.next();
      if (Objects.equals(id, paramString)) {
        return localVolumeInfo;
      }
    }
    return null;
  }
  
  public VolumeInfo findVolumeByQualifiedUuid(String paramString)
  {
    if (Objects.equals(UUID_PRIVATE_INTERNAL, paramString)) {
      return findVolumeById("private");
    }
    if (Objects.equals("primary_physical", paramString)) {
      return getPrimaryPhysicalVolume();
    }
    return findVolumeByUuid(paramString);
  }
  
  public VolumeInfo findVolumeByUuid(String paramString)
  {
    Preconditions.checkNotNull(paramString);
    Iterator localIterator = getVolumes().iterator();
    while (localIterator.hasNext())
    {
      VolumeInfo localVolumeInfo = (VolumeInfo)localIterator.next();
      if (Objects.equals(fsUuid, paramString)) {
        return localVolumeInfo;
      }
    }
    return null;
  }
  
  public void forgetVolume(String paramString)
  {
    try
    {
      mStorageManager.forgetVolume(paramString);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void format(String paramString)
  {
    try
    {
      mStorageManager.format(paramString);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public long getAllocatableBytes(UUID paramUUID)
    throws IOException
  {
    return getAllocatableBytes(paramUUID, 0);
  }
  
  @SystemApi
  @SuppressLint({"Doclava125"})
  public long getAllocatableBytes(UUID paramUUID, int paramInt)
    throws IOException
  {
    try
    {
      long l = mStorageManager.getAllocatableBytes(convert(paramUUID), paramInt, mContext.getOpPackageName());
      return l;
    }
    catch (RemoteException paramUUID)
    {
      throw paramUUID.rethrowFromSystemServer();
    }
    catch (ParcelableException paramUUID)
    {
      paramUUID.maybeRethrow(IOException.class);
      throw new RuntimeException(paramUUID);
    }
  }
  
  public String getBestVolumeDescription(VolumeInfo paramVolumeInfo)
  {
    if (paramVolumeInfo == null) {
      return null;
    }
    if (!TextUtils.isEmpty(fsUuid))
    {
      VolumeRecord localVolumeRecord = findRecordByUuid(fsUuid);
      if ((localVolumeRecord != null) && (!TextUtils.isEmpty(nickname))) {
        return nickname;
      }
    }
    if (!TextUtils.isEmpty(paramVolumeInfo.getDescription())) {
      return paramVolumeInfo.getDescription();
    }
    if (disk != null) {
      return disk.getDescription();
    }
    return null;
  }
  
  public long getCacheQuotaBytes(UUID paramUUID)
    throws IOException
  {
    try
    {
      ApplicationInfo localApplicationInfo = mContext.getApplicationInfo();
      long l = mStorageManager.getCacheQuotaBytes(convert(paramUUID), uid);
      return l;
    }
    catch (RemoteException paramUUID)
    {
      throw paramUUID.rethrowFromSystemServer();
    }
    catch (ParcelableException paramUUID)
    {
      paramUUID.maybeRethrow(IOException.class);
      throw new RuntimeException(paramUUID);
    }
  }
  
  public long getCacheSizeBytes(UUID paramUUID)
    throws IOException
  {
    try
    {
      ApplicationInfo localApplicationInfo = mContext.getApplicationInfo();
      long l = mStorageManager.getCacheSizeBytes(convert(paramUUID), uid);
      return l;
    }
    catch (RemoteException paramUUID)
    {
      throw paramUUID.rethrowFromSystemServer();
    }
    catch (ParcelableException paramUUID)
    {
      paramUUID.maybeRethrow(IOException.class);
      throw new RuntimeException(paramUUID);
    }
  }
  
  public List<DiskInfo> getDisks()
  {
    try
    {
      List localList = Arrays.asList(mStorageManager.getDisks());
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public String getMountedObbPath(String paramString)
  {
    Preconditions.checkNotNull(paramString, "rawPath cannot be null");
    try
    {
      paramString = mStorageManager.getMountedObbPath(paramString);
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public VolumeInfo getPrimaryPhysicalVolume()
  {
    Iterator localIterator = getVolumes().iterator();
    while (localIterator.hasNext())
    {
      VolumeInfo localVolumeInfo = (VolumeInfo)localIterator.next();
      if (localVolumeInfo.isPrimaryPhysical()) {
        return localVolumeInfo;
      }
    }
    return null;
  }
  
  public long getPrimaryStorageSize()
  {
    return FileUtils.roundStorageSize(Environment.getDataDirectory().getTotalSpace() + Environment.getRootDirectory().getTotalSpace());
  }
  
  public String getPrimaryStorageUuid()
  {
    try
    {
      String str = mStorageManager.getPrimaryStorageUuid();
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public StorageVolume getPrimaryStorageVolume()
  {
    return getVolumeList(mContext.getUserId(), 1536)[0];
  }
  
  public StorageVolume getPrimaryVolume()
  {
    return getPrimaryVolume(getVolumeList());
  }
  
  @VisibleForTesting
  public int getProxyFileDescriptorMountPointId()
  {
    synchronized (mFuseAppLoopLock)
    {
      int i;
      if (mFuseAppLoop != null) {
        i = mFuseAppLoop.getMountPointId();
      } else {
        i = -1;
      }
      return i;
    }
  }
  
  public long getStorageBytesUntilLow(File paramFile)
  {
    return paramFile.getUsableSpace() - getStorageFullBytes(paramFile);
  }
  
  public long getStorageCacheBytes(File paramFile, int paramInt)
  {
    long l = Settings.Global.getInt(mResolver, "sys_storage_cache_percentage", 10);
    l = Math.min(paramFile.getTotalSpace() * l / 100L, Settings.Global.getLong(mResolver, "sys_storage_cache_max_bytes", DEFAULT_CACHE_MAX_BYTES));
    if ((paramInt & 0x1) != 0) {
      return 0L;
    }
    if ((paramInt & 0x2) != 0) {
      return 0L;
    }
    if ((paramInt & 0x4) != 0) {
      return l / 2L;
    }
    return l;
  }
  
  public long getStorageFullBytes(File paramFile)
  {
    return Settings.Global.getLong(mResolver, "sys_storage_full_threshold_bytes", DEFAULT_FULL_THRESHOLD_BYTES);
  }
  
  public long getStorageLowBytes(File paramFile)
  {
    long l = Settings.Global.getInt(mResolver, "sys_storage_threshold_percentage", 5);
    return Math.min(paramFile.getTotalSpace() * l / 100L, Settings.Global.getLong(mResolver, "sys_storage_threshold_max_bytes", DEFAULT_THRESHOLD_MAX_BYTES));
  }
  
  public StorageVolume getStorageVolume(File paramFile)
  {
    return getStorageVolume(getVolumeList(), paramFile);
  }
  
  public List<StorageVolume> getStorageVolumes()
  {
    ArrayList localArrayList = new ArrayList();
    Collections.addAll(localArrayList, getVolumeList(mContext.getUserId(), 1536));
    return localArrayList;
  }
  
  public UUID getUuidForPath(File paramFile)
    throws IOException
  {
    Preconditions.checkNotNull(paramFile);
    Object localObject1 = paramFile.getCanonicalPath();
    if (FileUtils.contains(Environment.getDataDirectory().getAbsolutePath(), (String)localObject1)) {
      return UUID_DEFAULT;
    }
    try
    {
      Object localObject2 = mStorageManager;
      int i = 0;
      localObject2 = ((IStorageManager)localObject2).getVolumes(0);
      int j = localObject2.length;
      while (i < j)
      {
        UUID localUUID = localObject2[i];
        if ((path != null) && (FileUtils.contains(path, (String)localObject1)))
        {
          int k = type;
          if (k != 0) {
            try
            {
              localUUID = convert(fsUuid);
              return localUUID;
            }
            catch (IllegalArgumentException localIllegalArgumentException) {}
          }
        }
        i++;
      }
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Failed to find a storage device for ");
      ((StringBuilder)localObject1).append(paramFile);
      throw new FileNotFoundException(((StringBuilder)localObject1).toString());
    }
    catch (RemoteException paramFile)
    {
      throw paramFile.rethrowFromSystemServer();
    }
  }
  
  public StorageVolume[] getVolumeList()
  {
    return getVolumeList(mContext.getUserId(), 0);
  }
  
  @Deprecated
  public String[] getVolumePaths()
  {
    StorageVolume[] arrayOfStorageVolume = getVolumeList();
    int i = arrayOfStorageVolume.length;
    String[] arrayOfString = new String[i];
    for (int j = 0; j < i; j++) {
      arrayOfString[j] = arrayOfStorageVolume[j].getPath();
    }
    return arrayOfString;
  }
  
  public List<VolumeRecord> getVolumeRecords()
  {
    try
    {
      List localList = Arrays.asList(mStorageManager.getVolumeRecords(0));
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public String getVolumeState(String paramString)
  {
    paramString = getStorageVolume(new File(paramString));
    if (paramString != null) {
      return paramString.getState();
    }
    return "unknown";
  }
  
  public List<VolumeInfo> getVolumes()
  {
    try
    {
      List localList = Arrays.asList(mStorageManager.getVolumes(0));
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<VolumeInfo> getWritablePrivateVolumes()
  {
    try
    {
      ArrayList localArrayList = new java/util/ArrayList;
      localArrayList.<init>();
      Object localObject1 = mStorageManager;
      int i = 0;
      localObject1 = ((IStorageManager)localObject1).getVolumes(0);
      int j = localObject1.length;
      while (i < j)
      {
        Object localObject2 = localObject1[i];
        if ((localObject2.getType() == 1) && (localObject2.isMountedWritable())) {
          localArrayList.add(localObject2);
        }
        i++;
      }
      return localArrayList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isAllocationSupported(FileDescriptor paramFileDescriptor)
  {
    try
    {
      getUuidForPath(ParcelFileDescriptor.getFile(paramFileDescriptor));
      return true;
    }
    catch (IOException paramFileDescriptor) {}
    return false;
  }
  
  public boolean isCacheBehaviorGroup(File paramFile)
    throws IOException
  {
    return isCacheBehavior(paramFile, "user.cache_group");
  }
  
  public boolean isCacheBehaviorTombstone(File paramFile)
    throws IOException
  {
    return isCacheBehavior(paramFile, "user.cache_tombstone");
  }
  
  public boolean isEncrypted(File paramFile)
  {
    if (FileUtils.contains(Environment.getDataDirectory(), paramFile)) {
      return isEncrypted();
    }
    return FileUtils.contains(Environment.getExpandDirectory(), paramFile);
  }
  
  public boolean isObbMounted(String paramString)
  {
    Preconditions.checkNotNull(paramString, "rawPath cannot be null");
    try
    {
      boolean bool = mStorageManager.isObbMounted(paramString);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public boolean isUsbMassStorageConnected()
  {
    return false;
  }
  
  @Deprecated
  public boolean isUsbMassStorageEnabled()
  {
    return false;
  }
  
  public void lockUserKey(int paramInt)
  {
    try
    {
      mStorageManager.lockUserKey(paramInt);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void mkdirs(File paramFile)
  {
    try
    {
      mStorageManager.mkdirs(mContext.getOpPackageName(), paramFile.getAbsolutePath());
      return;
    }
    catch (RemoteException paramFile)
    {
      throw paramFile.rethrowFromSystemServer();
    }
  }
  
  public void mount(String paramString)
  {
    try
    {
      mStorageManager.mount(paramString);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean mountObb(String paramString1, String paramString2, OnObbStateChangeListener paramOnObbStateChangeListener)
  {
    Preconditions.checkNotNull(paramString1, "rawPath cannot be null");
    Preconditions.checkNotNull(paramOnObbStateChangeListener, "listener cannot be null");
    try
    {
      Object localObject = new java/io/File;
      ((File)localObject).<init>(paramString1);
      localObject = ((File)localObject).getCanonicalPath();
      int i = mObbActionListener.addListener(paramOnObbStateChangeListener);
      mStorageManager.mountObb(paramString1, (String)localObject, paramString2, mObbActionListener, i);
      return true;
    }
    catch (RemoteException paramString1)
    {
      throw paramString1.rethrowFromSystemServer();
    }
    catch (IOException paramOnObbStateChangeListener)
    {
      paramString2 = new StringBuilder();
      paramString2.append("Failed to resolve path: ");
      paramString2.append(paramString1);
      throw new IllegalArgumentException(paramString2.toString(), paramOnObbStateChangeListener);
    }
  }
  
  public ParcelFileDescriptor openProxyFileDescriptor(int paramInt, ProxyFileDescriptorCallback paramProxyFileDescriptorCallback)
    throws IOException
  {
    return openProxyFileDescriptor(paramInt, paramProxyFileDescriptorCallback, null, null);
  }
  
  public ParcelFileDescriptor openProxyFileDescriptor(int paramInt, ProxyFileDescriptorCallback paramProxyFileDescriptorCallback, Handler paramHandler)
    throws IOException
  {
    Preconditions.checkNotNull(paramHandler);
    return openProxyFileDescriptor(paramInt, paramProxyFileDescriptorCallback, paramHandler, null);
  }
  
  /* Error */
  @VisibleForTesting
  public ParcelFileDescriptor openProxyFileDescriptor(int paramInt, ProxyFileDescriptorCallback paramProxyFileDescriptorCallback, Handler paramHandler, java.util.concurrent.ThreadFactory paramThreadFactory)
    throws IOException
  {
    // Byte code:
    //   0: aload_2
    //   1: invokestatic 703	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;)Ljava/lang/Object;
    //   4: pop
    //   5: aload_0
    //   6: getfield 232	android/os/storage/StorageManager:mContext	Landroid/content/Context;
    //   9: ldc_w 1026
    //   12: iconst_1
    //   13: invokestatic 1032	com/android/internal/logging/MetricsLogger:count	(Landroid/content/Context;Ljava/lang/String;I)V
    //   16: aload_3
    //   17: astore 5
    //   19: aload_0
    //   20: getfield 228	android/os/storage/StorageManager:mFuseAppLoopLock	Ljava/lang/Object;
    //   23: astore 6
    //   25: aload 6
    //   27: monitorenter
    //   28: iconst_0
    //   29: istore 7
    //   31: aload_0
    //   32: getfield 230	android/os/storage/StorageManager:mFuseAppLoop	Lcom/android/internal/os/FuseAppLoop;
    //   35: ifnonnull +63 -> 98
    //   38: aload_0
    //   39: getfield 258	android/os/storage/StorageManager:mStorageManager	Landroid/os/storage/IStorageManager;
    //   42: invokeinterface 1036 1 0
    //   47: astore 8
    //   49: aload 8
    //   51: ifnull +34 -> 85
    //   54: new 877	com/android/internal/os/FuseAppLoop
    //   57: astore_3
    //   58: aload_3
    //   59: aload 8
    //   61: getfield 1041	com/android/internal/os/AppFuseMount:mountPointId	I
    //   64: aload 8
    //   66: getfield 1045	com/android/internal/os/AppFuseMount:fd	Landroid/os/ParcelFileDescriptor;
    //   69: aload 4
    //   71: invokespecial 1048	com/android/internal/os/FuseAppLoop:<init>	(ILandroid/os/ParcelFileDescriptor;Ljava/util/concurrent/ThreadFactory;)V
    //   74: aload_0
    //   75: aload_3
    //   76: putfield 230	android/os/storage/StorageManager:mFuseAppLoop	Lcom/android/internal/os/FuseAppLoop;
    //   79: iconst_1
    //   80: istore 7
    //   82: goto +16 -> 98
    //   85: new 355	java/io/IOException
    //   88: astore_2
    //   89: aload_2
    //   90: ldc_w 1050
    //   93: invokespecial 570	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   96: aload_2
    //   97: athrow
    //   98: aload 5
    //   100: astore_3
    //   101: aload 5
    //   103: ifnonnull +14 -> 117
    //   106: new 1052	android/os/Handler
    //   109: astore_3
    //   110: aload_3
    //   111: invokestatic 1058	android/os/Looper:getMainLooper	()Landroid/os/Looper;
    //   114: invokespecial 1061	android/os/Handler:<init>	(Landroid/os/Looper;)V
    //   117: aload_0
    //   118: getfield 230	android/os/storage/StorageManager:mFuseAppLoop	Lcom/android/internal/os/FuseAppLoop;
    //   121: aload_2
    //   122: aload_3
    //   123: invokevirtual 1065	com/android/internal/os/FuseAppLoop:registerCallback	(Landroid/os/ProxyFileDescriptorCallback;Landroid/os/Handler;)I
    //   126: istore 9
    //   128: aload_0
    //   129: getfield 258	android/os/storage/StorageManager:mStorageManager	Landroid/os/storage/IStorageManager;
    //   132: aload_0
    //   133: getfield 230	android/os/storage/StorageManager:mFuseAppLoop	Lcom/android/internal/os/FuseAppLoop;
    //   136: invokevirtual 880	com/android/internal/os/FuseAppLoop:getMountPointId	()I
    //   139: iload 9
    //   141: iload_1
    //   142: invokeinterface 1068 4 0
    //   147: astore 5
    //   149: aload 5
    //   151: ifnull +9 -> 160
    //   154: aload 6
    //   156: monitorexit
    //   157: aload 5
    //   159: areturn
    //   160: aload_0
    //   161: getfield 230	android/os/storage/StorageManager:mFuseAppLoop	Lcom/android/internal/os/FuseAppLoop;
    //   164: iload 9
    //   166: invokevirtual 1071	com/android/internal/os/FuseAppLoop:unregisterCallback	(I)V
    //   169: new 1024	com/android/internal/os/FuseUnavailableMountException
    //   172: astore 5
    //   174: aload 5
    //   176: aload_0
    //   177: getfield 230	android/os/storage/StorageManager:mFuseAppLoop	Lcom/android/internal/os/FuseAppLoop;
    //   180: invokevirtual 880	com/android/internal/os/FuseAppLoop:getMountPointId	()I
    //   183: invokespecial 1072	com/android/internal/os/FuseUnavailableMountException:<init>	(I)V
    //   186: aload 5
    //   188: athrow
    //   189: astore 5
    //   191: iload 7
    //   193: ifne +17 -> 210
    //   196: aload_0
    //   197: aconst_null
    //   198: putfield 230	android/os/storage/StorageManager:mFuseAppLoop	Lcom/android/internal/os/FuseAppLoop;
    //   201: aload 6
    //   203: monitorexit
    //   204: aload_3
    //   205: astore 5
    //   207: goto -188 -> 19
    //   210: new 355	java/io/IOException
    //   213: astore_2
    //   214: aload_2
    //   215: aload 5
    //   217: invokespecial 1073	java/io/IOException:<init>	(Ljava/lang/Throwable;)V
    //   220: aload_2
    //   221: athrow
    //   222: astore_2
    //   223: aload 6
    //   225: monitorexit
    //   226: aload_2
    //   227: athrow
    //   228: astore_2
    //   229: new 355	java/io/IOException
    //   232: dup
    //   233: aload_2
    //   234: invokespecial 1073	java/io/IOException:<init>	(Ljava/lang/Throwable;)V
    //   237: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	238	0	this	StorageManager
    //   0	238	1	paramInt	int
    //   0	238	2	paramProxyFileDescriptorCallback	ProxyFileDescriptorCallback
    //   0	238	3	paramHandler	Handler
    //   0	238	4	paramThreadFactory	java.util.concurrent.ThreadFactory
    //   17	170	5	localObject1	Object
    //   189	1	5	localFuseUnavailableMountException	com.android.internal.os.FuseUnavailableMountException
    //   205	11	5	localHandler	Handler
    //   23	201	6	localObject2	Object
    //   29	163	7	i	int
    //   47	18	8	localAppFuseMount	com.android.internal.os.AppFuseMount
    //   126	39	9	j	int
    // Exception table:
    //   from	to	target	type
    //   117	149	189	com/android/internal/os/FuseUnavailableMountException
    //   160	189	189	com/android/internal/os/FuseUnavailableMountException
    //   31	49	222	finally
    //   54	79	222	finally
    //   85	98	222	finally
    //   106	117	222	finally
    //   117	149	222	finally
    //   154	157	222	finally
    //   160	189	222	finally
    //   196	204	222	finally
    //   210	222	222	finally
    //   223	226	222	finally
    //   19	28	228	android/os/RemoteException
    //   226	228	228	android/os/RemoteException
  }
  
  public void partitionMixed(String paramString, int paramInt)
  {
    try
    {
      mStorageManager.partitionMixed(paramString, paramInt);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void partitionPrivate(String paramString)
  {
    try
    {
      mStorageManager.partitionPrivate(paramString);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void partitionPublic(String paramString)
  {
    try
    {
      mStorageManager.partitionPublic(paramString);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void prepareUserStorage(String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    try
    {
      mStorageManager.prepareUserStorage(paramString, paramInt1, paramInt2, paramInt3);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void registerListener(StorageEventListener paramStorageEventListener)
  {
    synchronized (mDelegates)
    {
      StorageEventListenerDelegate localStorageEventListenerDelegate = new android/os/storage/StorageManager$StorageEventListenerDelegate;
      localStorageEventListenerDelegate.<init>(paramStorageEventListener, mLooper);
      try
      {
        mStorageManager.registerListener(localStorageEventListenerDelegate);
        mDelegates.add(localStorageEventListenerDelegate);
        return;
      }
      catch (RemoteException paramStorageEventListener)
      {
        throw paramStorageEventListener.rethrowFromSystemServer();
      }
    }
  }
  
  public void setCacheBehaviorGroup(File paramFile, boolean paramBoolean)
    throws IOException
  {
    setCacheBehavior(paramFile, "user.cache_group", paramBoolean);
  }
  
  public void setCacheBehaviorTombstone(File paramFile, boolean paramBoolean)
    throws IOException
  {
    setCacheBehavior(paramFile, "user.cache_tombstone", paramBoolean);
  }
  
  public void setPrimaryStorageUuid(String paramString, IPackageMoveObserver paramIPackageMoveObserver)
  {
    try
    {
      mStorageManager.setPrimaryStorageUuid(paramString, paramIPackageMoveObserver);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void setVolumeInited(String paramString, boolean paramBoolean)
  {
    try
    {
      mStorageManager.setVolumeUserFlags(paramString, paramBoolean, 1);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void setVolumeNickname(String paramString1, String paramString2)
  {
    try
    {
      mStorageManager.setVolumeNickname(paramString1, paramString2);
      return;
    }
    catch (RemoteException paramString1)
    {
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  public void setVolumeSnoozed(String paramString, boolean paramBoolean)
  {
    try
    {
      IStorageManager localIStorageManager = mStorageManager;
      int i;
      if (paramBoolean) {
        i = 2;
      } else {
        i = 0;
      }
      localIStorageManager.setVolumeUserFlags(paramString, i, 2);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void unlockUserKey(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    try
    {
      mStorageManager.unlockUserKey(paramInt1, paramInt2, paramArrayOfByte1, paramArrayOfByte2);
      return;
    }
    catch (RemoteException paramArrayOfByte1)
    {
      throw paramArrayOfByte1.rethrowFromSystemServer();
    }
  }
  
  public void unmount(String paramString)
  {
    try
    {
      mStorageManager.unmount(paramString);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean unmountObb(String paramString, boolean paramBoolean, OnObbStateChangeListener paramOnObbStateChangeListener)
  {
    Preconditions.checkNotNull(paramString, "rawPath cannot be null");
    Preconditions.checkNotNull(paramOnObbStateChangeListener, "listener cannot be null");
    try
    {
      int i = mObbActionListener.addListener(paramOnObbStateChangeListener);
      mStorageManager.unmountObb(paramString, paramBoolean, mObbActionListener, i);
      return true;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void unregisterListener(StorageEventListener paramStorageEventListener)
  {
    synchronized (mDelegates)
    {
      Iterator localIterator = mDelegates.iterator();
      while (localIterator.hasNext())
      {
        StorageEventListenerDelegate localStorageEventListenerDelegate = (StorageEventListenerDelegate)localIterator.next();
        StorageEventListener localStorageEventListener = mCallback;
        if (localStorageEventListener == paramStorageEventListener) {
          try
          {
            mStorageManager.unregisterListener(localStorageEventListenerDelegate);
            localIterator.remove();
          }
          catch (RemoteException paramStorageEventListener)
          {
            throw paramStorageEventListener.rethrowFromSystemServer();
          }
        }
      }
      return;
    }
  }
  
  public void wipeAdoptableDisks()
  {
    Iterator localIterator = getDisks().iterator();
    while (localIterator.hasNext())
    {
      Object localObject1 = (DiskInfo)localIterator.next();
      Object localObject2 = ((DiskInfo)localObject1).getId();
      if (((DiskInfo)localObject1).isAdoptable())
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("Found adoptable ");
        ((StringBuilder)localObject1).append((String)localObject2);
        ((StringBuilder)localObject1).append("; wiping");
        Slog.d("StorageManager", ((StringBuilder)localObject1).toString());
        try
        {
          mStorageManager.partitionPublic((String)localObject2);
        }
        catch (Exception localException)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Failed to wipe ");
          localStringBuilder.append((String)localObject2);
          localStringBuilder.append(", but soldiering onward");
          Slog.w("StorageManager", localStringBuilder.toString(), localException);
        }
      }
      else
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Ignorning non-adoptable disk ");
        ((StringBuilder)localObject2).append(localException.getId());
        Slog.d("StorageManager", ((StringBuilder)localObject2).toString());
      }
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface AllocateFlags {}
  
  private class ObbActionListener
    extends IObbActionListener.Stub
  {
    private SparseArray<StorageManager.ObbListenerDelegate> mListeners = new SparseArray();
    
    private ObbActionListener() {}
    
    public int addListener(OnObbStateChangeListener arg1)
    {
      StorageManager.ObbListenerDelegate localObbListenerDelegate = new StorageManager.ObbListenerDelegate(StorageManager.this, ???);
      synchronized (mListeners)
      {
        mListeners.put(StorageManager.ObbListenerDelegate.access$100(localObbListenerDelegate), localObbListenerDelegate);
        return StorageManager.ObbListenerDelegate.access$100(localObbListenerDelegate);
      }
    }
    
    public void onObbResult(String paramString, int paramInt1, int paramInt2)
    {
      synchronized (mListeners)
      {
        StorageManager.ObbListenerDelegate localObbListenerDelegate = (StorageManager.ObbListenerDelegate)mListeners.get(paramInt1);
        if (localObbListenerDelegate != null) {
          mListeners.remove(paramInt1);
        }
        if (localObbListenerDelegate != null) {
          localObbListenerDelegate.sendObbStateChanged(paramString, paramInt2);
        }
        return;
      }
    }
  }
  
  private class ObbListenerDelegate
  {
    private final Handler mHandler;
    private final WeakReference<OnObbStateChangeListener> mObbEventListenerRef;
    private final int nonce = StorageManager.this.getNextNonce();
    
    ObbListenerDelegate(OnObbStateChangeListener paramOnObbStateChangeListener)
    {
      mObbEventListenerRef = new WeakReference(paramOnObbStateChangeListener);
      mHandler = new Handler(mLooper)
      {
        public void handleMessage(Message paramAnonymousMessage)
        {
          OnObbStateChangeListener localOnObbStateChangeListener = getListener();
          if (localOnObbStateChangeListener == null) {
            return;
          }
          localOnObbStateChangeListener.onObbStateChange((String)obj, arg1);
        }
      };
    }
    
    OnObbStateChangeListener getListener()
    {
      if (mObbEventListenerRef == null) {
        return null;
      }
      return (OnObbStateChangeListener)mObbEventListenerRef.get();
    }
    
    void sendObbStateChanged(String paramString, int paramInt)
    {
      mHandler.obtainMessage(0, paramInt, 0, paramString).sendToTarget();
    }
  }
  
  private static class StorageEventListenerDelegate
    extends IStorageEventListener.Stub
    implements Handler.Callback
  {
    private static final int MSG_DISK_DESTROYED = 6;
    private static final int MSG_DISK_SCANNED = 5;
    private static final int MSG_STORAGE_STATE_CHANGED = 1;
    private static final int MSG_VOLUME_FORGOTTEN = 4;
    private static final int MSG_VOLUME_RECORD_CHANGED = 3;
    private static final int MSG_VOLUME_STATE_CHANGED = 2;
    final StorageEventListener mCallback;
    final Handler mHandler;
    
    public StorageEventListenerDelegate(StorageEventListener paramStorageEventListener, Looper paramLooper)
    {
      mCallback = paramStorageEventListener;
      mHandler = new Handler(paramLooper, this);
    }
    
    public boolean handleMessage(Message paramMessage)
    {
      SomeArgs localSomeArgs = (SomeArgs)obj;
      switch (what)
      {
      default: 
        localSomeArgs.recycle();
        return false;
      case 6: 
        mCallback.onDiskDestroyed((DiskInfo)arg1);
        localSomeArgs.recycle();
        return true;
      case 5: 
        mCallback.onDiskScanned((DiskInfo)arg1, argi2);
        localSomeArgs.recycle();
        return true;
      case 4: 
        mCallback.onVolumeForgotten((String)arg1);
        localSomeArgs.recycle();
        return true;
      case 3: 
        mCallback.onVolumeRecordChanged((VolumeRecord)arg1);
        localSomeArgs.recycle();
        return true;
      case 2: 
        mCallback.onVolumeStateChanged((VolumeInfo)arg1, argi2, argi3);
        localSomeArgs.recycle();
        return true;
      }
      mCallback.onStorageStateChanged((String)arg1, (String)arg2, (String)arg3);
      localSomeArgs.recycle();
      return true;
    }
    
    public void onDiskDestroyed(DiskInfo paramDiskInfo)
      throws RemoteException
    {
      SomeArgs localSomeArgs = SomeArgs.obtain();
      arg1 = paramDiskInfo;
      mHandler.obtainMessage(6, localSomeArgs).sendToTarget();
    }
    
    public void onDiskScanned(DiskInfo paramDiskInfo, int paramInt)
    {
      SomeArgs localSomeArgs = SomeArgs.obtain();
      arg1 = paramDiskInfo;
      argi2 = paramInt;
      mHandler.obtainMessage(5, localSomeArgs).sendToTarget();
    }
    
    public void onStorageStateChanged(String paramString1, String paramString2, String paramString3)
    {
      SomeArgs localSomeArgs = SomeArgs.obtain();
      arg1 = paramString1;
      arg2 = paramString2;
      arg3 = paramString3;
      mHandler.obtainMessage(1, localSomeArgs).sendToTarget();
    }
    
    public void onUsbMassStorageConnectionChanged(boolean paramBoolean)
      throws RemoteException
    {}
    
    public void onVolumeForgotten(String paramString)
    {
      SomeArgs localSomeArgs = SomeArgs.obtain();
      arg1 = paramString;
      mHandler.obtainMessage(4, localSomeArgs).sendToTarget();
    }
    
    public void onVolumeRecordChanged(VolumeRecord paramVolumeRecord)
    {
      SomeArgs localSomeArgs = SomeArgs.obtain();
      arg1 = paramVolumeRecord;
      mHandler.obtainMessage(3, localSomeArgs).sendToTarget();
    }
    
    public void onVolumeStateChanged(VolumeInfo paramVolumeInfo, int paramInt1, int paramInt2)
    {
      SomeArgs localSomeArgs = SomeArgs.obtain();
      arg1 = paramVolumeInfo;
      argi2 = paramInt1;
      argi3 = paramInt2;
      mHandler.obtainMessage(2, localSomeArgs).sendToTarget();
    }
  }
}
