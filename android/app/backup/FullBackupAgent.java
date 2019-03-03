package android.app.backup;

import android.os.ParcelFileDescriptor;
import java.io.IOException;

public class FullBackupAgent
  extends BackupAgent
{
  public FullBackupAgent() {}
  
  public void onBackup(ParcelFileDescriptor paramParcelFileDescriptor1, BackupDataOutput paramBackupDataOutput, ParcelFileDescriptor paramParcelFileDescriptor2)
    throws IOException
  {}
  
  public void onRestore(BackupDataInput paramBackupDataInput, int paramInt, ParcelFileDescriptor paramParcelFileDescriptor)
    throws IOException
  {}
}
