package android.app.backup;

import android.os.ParcelFileDescriptor;

public abstract interface BackupHelper
{
  public abstract void performBackup(ParcelFileDescriptor paramParcelFileDescriptor1, BackupDataOutput paramBackupDataOutput, ParcelFileDescriptor paramParcelFileDescriptor2);
  
  public abstract void restoreEntity(BackupDataInputStream paramBackupDataInputStream);
  
  public abstract void writeNewStateDescription(ParcelFileDescriptor paramParcelFileDescriptor);
}
