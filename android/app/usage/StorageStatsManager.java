package android.app.usage;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.ParcelableException;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import com.android.internal.util.Preconditions;
import java.io.IOException;
import java.util.UUID;

public class StorageStatsManager
{
  private final Context mContext;
  private final IStorageStatsManager mService;
  
  public StorageStatsManager(Context paramContext, IStorageStatsManager paramIStorageStatsManager)
  {
    mContext = ((Context)Preconditions.checkNotNull(paramContext));
    mService = ((IStorageStatsManager)Preconditions.checkNotNull(paramIStorageStatsManager));
  }
  
  @Deprecated
  public long getCacheBytes(String paramString)
    throws IOException
  {
    return getCacheBytes(StorageManager.convert(paramString));
  }
  
  public long getCacheBytes(UUID paramUUID)
    throws IOException
  {
    try
    {
      long l = mService.getCacheBytes(StorageManager.convert(paramUUID), mContext.getOpPackageName());
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
  
  public long getCacheQuotaBytes(String paramString, int paramInt)
  {
    try
    {
      long l = mService.getCacheQuotaBytes(paramString, paramInt, mContext.getOpPackageName());
      return l;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public long getFreeBytes(String paramString)
    throws IOException
  {
    return getFreeBytes(StorageManager.convert(paramString));
  }
  
  public long getFreeBytes(UUID paramUUID)
    throws IOException
  {
    try
    {
      long l = mService.getFreeBytes(StorageManager.convert(paramUUID), mContext.getOpPackageName());
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
  
  @Deprecated
  public long getTotalBytes(String paramString)
    throws IOException
  {
    return getTotalBytes(StorageManager.convert(paramString));
  }
  
  public long getTotalBytes(UUID paramUUID)
    throws IOException
  {
    try
    {
      long l = mService.getTotalBytes(StorageManager.convert(paramUUID), mContext.getOpPackageName());
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
  
  @Deprecated
  public boolean isQuotaSupported(String paramString)
  {
    return isQuotaSupported(StorageManager.convert(paramString));
  }
  
  public boolean isQuotaSupported(UUID paramUUID)
  {
    try
    {
      boolean bool = mService.isQuotaSupported(StorageManager.convert(paramUUID), mContext.getOpPackageName());
      return bool;
    }
    catch (RemoteException paramUUID)
    {
      throw paramUUID.rethrowFromSystemServer();
    }
  }
  
  public boolean isReservedSupported(UUID paramUUID)
  {
    try
    {
      boolean bool = mService.isReservedSupported(StorageManager.convert(paramUUID), mContext.getOpPackageName());
      return bool;
    }
    catch (RemoteException paramUUID)
    {
      throw paramUUID.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public ExternalStorageStats queryExternalStatsForUser(String paramString, UserHandle paramUserHandle)
    throws IOException
  {
    return queryExternalStatsForUser(StorageManager.convert(paramString), paramUserHandle);
  }
  
  public ExternalStorageStats queryExternalStatsForUser(UUID paramUUID, UserHandle paramUserHandle)
    throws IOException
  {
    try
    {
      paramUUID = mService.queryExternalStatsForUser(StorageManager.convert(paramUUID), paramUserHandle.getIdentifier(), mContext.getOpPackageName());
      return paramUUID;
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
  
  @Deprecated
  public StorageStats queryStatsForPackage(String paramString1, String paramString2, UserHandle paramUserHandle)
    throws PackageManager.NameNotFoundException, IOException
  {
    return queryStatsForPackage(StorageManager.convert(paramString1), paramString2, paramUserHandle);
  }
  
  public StorageStats queryStatsForPackage(UUID paramUUID, String paramString, UserHandle paramUserHandle)
    throws PackageManager.NameNotFoundException, IOException
  {
    try
    {
      paramUUID = mService.queryStatsForPackage(StorageManager.convert(paramUUID), paramString, paramUserHandle.getIdentifier(), mContext.getOpPackageName());
      return paramUUID;
    }
    catch (RemoteException paramUUID)
    {
      throw paramUUID.rethrowFromSystemServer();
    }
    catch (ParcelableException paramUUID)
    {
      paramUUID.maybeRethrow(PackageManager.NameNotFoundException.class);
      paramUUID.maybeRethrow(IOException.class);
      throw new RuntimeException(paramUUID);
    }
  }
  
  @Deprecated
  public StorageStats queryStatsForUid(String paramString, int paramInt)
    throws IOException
  {
    return queryStatsForUid(StorageManager.convert(paramString), paramInt);
  }
  
  public StorageStats queryStatsForUid(UUID paramUUID, int paramInt)
    throws IOException
  {
    try
    {
      paramUUID = mService.queryStatsForUid(StorageManager.convert(paramUUID), paramInt, mContext.getOpPackageName());
      return paramUUID;
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
  
  @Deprecated
  public StorageStats queryStatsForUser(String paramString, UserHandle paramUserHandle)
    throws IOException
  {
    return queryStatsForUser(StorageManager.convert(paramString), paramUserHandle);
  }
  
  public StorageStats queryStatsForUser(UUID paramUUID, UserHandle paramUserHandle)
    throws IOException
  {
    try
    {
      paramUUID = mService.queryStatsForUser(StorageManager.convert(paramUUID), paramUserHandle.getIdentifier(), mContext.getOpPackageName());
      return paramUUID;
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
}
