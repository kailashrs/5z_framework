package android.app.backup;

import android.content.Context;
import android.os.ParcelFileDescriptor;
import java.io.File;

public class AbsoluteFileBackupHelper
  extends FileBackupHelperBase
  implements BackupHelper
{
  private static final boolean DEBUG = false;
  private static final String TAG = "AbsoluteFileBackupHelper";
  Context mContext;
  String[] mFiles;
  
  public AbsoluteFileBackupHelper(Context paramContext, String... paramVarArgs)
  {
    super(paramContext);
    mContext = paramContext;
    mFiles = paramVarArgs;
  }
  
  public void performBackup(ParcelFileDescriptor paramParcelFileDescriptor1, BackupDataOutput paramBackupDataOutput, ParcelFileDescriptor paramParcelFileDescriptor2)
  {
    performBackup_checked(paramParcelFileDescriptor1, paramBackupDataOutput, paramParcelFileDescriptor2, mFiles, mFiles);
  }
  
  public void restoreEntity(BackupDataInputStream paramBackupDataInputStream)
  {
    String str = paramBackupDataInputStream.getKey();
    if (isKeyInList(str, mFiles)) {
      writeFile(new File(str), paramBackupDataInputStream);
    }
  }
}
