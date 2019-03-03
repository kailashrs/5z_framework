package android.app.backup;

import android.app.QueuedWork;
import android.content.Context;
import android.os.ParcelFileDescriptor;
import java.io.File;

public class SharedPreferencesBackupHelper
  extends FileBackupHelperBase
  implements BackupHelper
{
  private static final boolean DEBUG = false;
  private static final String TAG = "SharedPreferencesBackupHelper";
  private Context mContext;
  private String[] mPrefGroups;
  
  public SharedPreferencesBackupHelper(Context paramContext, String... paramVarArgs)
  {
    super(paramContext);
    mContext = paramContext;
    mPrefGroups = paramVarArgs;
  }
  
  public void performBackup(ParcelFileDescriptor paramParcelFileDescriptor1, BackupDataOutput paramBackupDataOutput, ParcelFileDescriptor paramParcelFileDescriptor2)
  {
    Context localContext = mContext;
    QueuedWork.waitToFinish();
    String[] arrayOfString1 = mPrefGroups;
    int i = arrayOfString1.length;
    String[] arrayOfString2 = new String[i];
    for (int j = 0; j < i; j++) {
      arrayOfString2[j] = localContext.getSharedPrefsFile(arrayOfString1[j]).getAbsolutePath();
    }
    performBackup_checked(paramParcelFileDescriptor1, paramBackupDataOutput, paramParcelFileDescriptor2, arrayOfString2, arrayOfString1);
  }
  
  public void restoreEntity(BackupDataInputStream paramBackupDataInputStream)
  {
    Context localContext = mContext;
    String str = paramBackupDataInputStream.getKey();
    if (isKeyInList(str, mPrefGroups)) {
      writeFile(localContext.getSharedPrefsFile(str).getAbsoluteFile(), paramBackupDataInputStream);
    }
  }
}
