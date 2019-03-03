package android.app.backup;

import android.content.Context;
import android.os.ParcelFileDescriptor;
import java.io.File;

public class FileBackupHelper
  extends FileBackupHelperBase
  implements BackupHelper
{
  private static final boolean DEBUG = false;
  private static final String TAG = "FileBackupHelper";
  Context mContext;
  String[] mFiles;
  File mFilesDir;
  
  public FileBackupHelper(Context paramContext, String... paramVarArgs)
  {
    super(paramContext);
    mContext = paramContext;
    mFilesDir = paramContext.getFilesDir();
    mFiles = paramVarArgs;
  }
  
  public void performBackup(ParcelFileDescriptor paramParcelFileDescriptor1, BackupDataOutput paramBackupDataOutput, ParcelFileDescriptor paramParcelFileDescriptor2)
  {
    String[] arrayOfString1 = mFiles;
    File localFile = mContext.getFilesDir();
    int i = arrayOfString1.length;
    String[] arrayOfString2 = new String[i];
    for (int j = 0; j < i; j++) {
      arrayOfString2[j] = new File(localFile, arrayOfString1[j]).getAbsolutePath();
    }
    performBackup_checked(paramParcelFileDescriptor1, paramBackupDataOutput, paramParcelFileDescriptor2, arrayOfString2, arrayOfString1);
  }
  
  public void restoreEntity(BackupDataInputStream paramBackupDataInputStream)
  {
    String str = paramBackupDataInputStream.getKey();
    if (isKeyInList(str, mFiles)) {
      writeFile(new File(mFilesDir, str), paramBackupDataInputStream);
    }
  }
}
