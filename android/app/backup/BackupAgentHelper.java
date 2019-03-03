package android.app.backup;

import android.os.ParcelFileDescriptor;
import java.io.IOException;

public class BackupAgentHelper
  extends BackupAgent
{
  static final String TAG = "BackupAgentHelper";
  BackupHelperDispatcher mDispatcher = new BackupHelperDispatcher();
  
  public BackupAgentHelper() {}
  
  public void addHelper(String paramString, BackupHelper paramBackupHelper)
  {
    mDispatcher.addHelper(paramString, paramBackupHelper);
  }
  
  public BackupHelperDispatcher getDispatcher()
  {
    return mDispatcher;
  }
  
  public void onBackup(ParcelFileDescriptor paramParcelFileDescriptor1, BackupDataOutput paramBackupDataOutput, ParcelFileDescriptor paramParcelFileDescriptor2)
    throws IOException
  {
    mDispatcher.performBackup(paramParcelFileDescriptor1, paramBackupDataOutput, paramParcelFileDescriptor2);
  }
  
  public void onRestore(BackupDataInput paramBackupDataInput, int paramInt, ParcelFileDescriptor paramParcelFileDescriptor)
    throws IOException
  {
    mDispatcher.performRestore(paramBackupDataInput, paramInt, paramParcelFileDescriptor);
  }
}
