package android.app.backup;

import android.annotation.SystemApi;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import com.android.internal.backup.IBackupTransport;
import com.android.internal.backup.IBackupTransport.Stub;

@SystemApi
public class BackupTransport
{
  public static final int AGENT_ERROR = -1003;
  public static final int AGENT_UNKNOWN = -1004;
  public static final String EXTRA_TRANSPORT_REGISTRATION = "android.app.backup.extra.TRANSPORT_REGISTRATION";
  public static final int FLAG_INCREMENTAL = 2;
  public static final int FLAG_NON_INCREMENTAL = 4;
  public static final int FLAG_USER_INITIATED = 1;
  public static final int NO_MORE_DATA = -1;
  public static final int TRANSPORT_ERROR = -1000;
  public static final int TRANSPORT_NON_INCREMENTAL_BACKUP_REQUIRED = -1006;
  public static final int TRANSPORT_NOT_INITIALIZED = -1001;
  public static final int TRANSPORT_OK = 0;
  public static final int TRANSPORT_PACKAGE_REJECTED = -1002;
  public static final int TRANSPORT_QUOTA_EXCEEDED = -1005;
  IBackupTransport mBinderImpl = new TransportImpl();
  
  public BackupTransport() {}
  
  public int abortFullRestore()
  {
    return 0;
  }
  
  public void cancelFullBackup()
  {
    throw new UnsupportedOperationException("Transport cancelFullBackup() not implemented");
  }
  
  public int checkFullBackupSize(long paramLong)
  {
    return 0;
  }
  
  public int clearBackupData(PackageInfo paramPackageInfo)
  {
    return 64536;
  }
  
  public Intent configurationIntent()
  {
    return null;
  }
  
  public String currentDestinationString()
  {
    throw new UnsupportedOperationException("Transport currentDestinationString() not implemented");
  }
  
  public Intent dataManagementIntent()
  {
    return null;
  }
  
  public String dataManagementLabel()
  {
    throw new UnsupportedOperationException("Transport dataManagementLabel() not implemented");
  }
  
  public int finishBackup()
  {
    return 64536;
  }
  
  public void finishRestore()
  {
    throw new UnsupportedOperationException("Transport finishRestore() not implemented");
  }
  
  public RestoreSet[] getAvailableRestoreSets()
  {
    return null;
  }
  
  public long getBackupQuota(String paramString, boolean paramBoolean)
  {
    return Long.MAX_VALUE;
  }
  
  public IBinder getBinder()
  {
    return mBinderImpl.asBinder();
  }
  
  public long getCurrentRestoreSet()
  {
    return 0L;
  }
  
  public int getNextFullRestoreDataChunk(ParcelFileDescriptor paramParcelFileDescriptor)
  {
    return 0;
  }
  
  public int getRestoreData(ParcelFileDescriptor paramParcelFileDescriptor)
  {
    return 64536;
  }
  
  public int getTransportFlags()
  {
    return 0;
  }
  
  public int initializeDevice()
  {
    return 64536;
  }
  
  public boolean isAppEligibleForBackup(PackageInfo paramPackageInfo, boolean paramBoolean)
  {
    return true;
  }
  
  public String name()
  {
    throw new UnsupportedOperationException("Transport name() not implemented");
  }
  
  public RestoreDescription nextRestorePackage()
  {
    return null;
  }
  
  public int performBackup(PackageInfo paramPackageInfo, ParcelFileDescriptor paramParcelFileDescriptor)
  {
    return 64536;
  }
  
  public int performBackup(PackageInfo paramPackageInfo, ParcelFileDescriptor paramParcelFileDescriptor, int paramInt)
  {
    return performBackup(paramPackageInfo, paramParcelFileDescriptor);
  }
  
  public int performFullBackup(PackageInfo paramPackageInfo, ParcelFileDescriptor paramParcelFileDescriptor)
  {
    return 64534;
  }
  
  public int performFullBackup(PackageInfo paramPackageInfo, ParcelFileDescriptor paramParcelFileDescriptor, int paramInt)
  {
    return performFullBackup(paramPackageInfo, paramParcelFileDescriptor);
  }
  
  public long requestBackupTime()
  {
    return 0L;
  }
  
  public long requestFullBackupTime()
  {
    return 0L;
  }
  
  public int sendBackupData(int paramInt)
  {
    return 64536;
  }
  
  public int setAsusFileDestination(String paramString)
  {
    return 0;
  }
  
  public int startRestore(long paramLong, PackageInfo[] paramArrayOfPackageInfo)
  {
    return 64536;
  }
  
  public String transportDirName()
  {
    throw new UnsupportedOperationException("Transport transportDirName() not implemented");
  }
  
  class TransportImpl
    extends IBackupTransport.Stub
  {
    TransportImpl() {}
    
    public int abortFullRestore()
    {
      return BackupTransport.this.abortFullRestore();
    }
    
    public void cancelFullBackup()
      throws RemoteException
    {
      BackupTransport.this.cancelFullBackup();
    }
    
    public int checkFullBackupSize(long paramLong)
    {
      return BackupTransport.this.checkFullBackupSize(paramLong);
    }
    
    public int clearBackupData(PackageInfo paramPackageInfo)
      throws RemoteException
    {
      return BackupTransport.this.clearBackupData(paramPackageInfo);
    }
    
    public Intent configurationIntent()
      throws RemoteException
    {
      return BackupTransport.this.configurationIntent();
    }
    
    public String currentDestinationString()
      throws RemoteException
    {
      return BackupTransport.this.currentDestinationString();
    }
    
    public Intent dataManagementIntent()
    {
      return BackupTransport.this.dataManagementIntent();
    }
    
    public String dataManagementLabel()
    {
      return BackupTransport.this.dataManagementLabel();
    }
    
    public int finishBackup()
      throws RemoteException
    {
      return BackupTransport.this.finishBackup();
    }
    
    public void finishRestore()
      throws RemoteException
    {
      BackupTransport.this.finishRestore();
    }
    
    public RestoreSet[] getAvailableRestoreSets()
      throws RemoteException
    {
      return BackupTransport.this.getAvailableRestoreSets();
    }
    
    public long getBackupQuota(String paramString, boolean paramBoolean)
    {
      return BackupTransport.this.getBackupQuota(paramString, paramBoolean);
    }
    
    public long getCurrentRestoreSet()
      throws RemoteException
    {
      return BackupTransport.this.getCurrentRestoreSet();
    }
    
    public int getNextFullRestoreDataChunk(ParcelFileDescriptor paramParcelFileDescriptor)
    {
      return BackupTransport.this.getNextFullRestoreDataChunk(paramParcelFileDescriptor);
    }
    
    public int getRestoreData(ParcelFileDescriptor paramParcelFileDescriptor)
      throws RemoteException
    {
      return BackupTransport.this.getRestoreData(paramParcelFileDescriptor);
    }
    
    public int getTransportFlags()
    {
      return BackupTransport.this.getTransportFlags();
    }
    
    public int initializeDevice()
      throws RemoteException
    {
      return BackupTransport.this.initializeDevice();
    }
    
    public boolean isAppEligibleForBackup(PackageInfo paramPackageInfo, boolean paramBoolean)
      throws RemoteException
    {
      return BackupTransport.this.isAppEligibleForBackup(paramPackageInfo, paramBoolean);
    }
    
    public String name()
      throws RemoteException
    {
      return BackupTransport.this.name();
    }
    
    public RestoreDescription nextRestorePackage()
      throws RemoteException
    {
      return BackupTransport.this.nextRestorePackage();
    }
    
    public int performBackup(PackageInfo paramPackageInfo, ParcelFileDescriptor paramParcelFileDescriptor, int paramInt)
      throws RemoteException
    {
      return BackupTransport.this.performBackup(paramPackageInfo, paramParcelFileDescriptor, paramInt);
    }
    
    public int performFullBackup(PackageInfo paramPackageInfo, ParcelFileDescriptor paramParcelFileDescriptor, int paramInt)
      throws RemoteException
    {
      return BackupTransport.this.performFullBackup(paramPackageInfo, paramParcelFileDescriptor, paramInt);
    }
    
    public long requestBackupTime()
      throws RemoteException
    {
      return BackupTransport.this.requestBackupTime();
    }
    
    public long requestFullBackupTime()
      throws RemoteException
    {
      return BackupTransport.this.requestFullBackupTime();
    }
    
    public int sendBackupData(int paramInt)
      throws RemoteException
    {
      return BackupTransport.this.sendBackupData(paramInt);
    }
    
    public int setAsusFileDestination(String paramString)
    {
      return BackupTransport.this.setAsusFileDestination(paramString);
    }
    
    public int startRestore(long paramLong, PackageInfo[] paramArrayOfPackageInfo)
      throws RemoteException
    {
      return BackupTransport.this.startRestore(paramLong, paramArrayOfPackageInfo);
    }
    
    public String transportDirName()
      throws RemoteException
    {
      return BackupTransport.this.transportDirName();
    }
  }
}
