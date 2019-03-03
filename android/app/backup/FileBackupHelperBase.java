package android.app.backup;

import android.content.Context;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import java.io.File;
import java.io.FileDescriptor;

class FileBackupHelperBase
{
  private static final String TAG = "FileBackupHelperBase";
  Context mContext;
  boolean mExceptionLogged;
  long mPtr = ctor();
  
  FileBackupHelperBase(Context paramContext)
  {
    mContext = paramContext;
  }
  
  private static native long ctor();
  
  private static native void dtor(long paramLong);
  
  static void performBackup_checked(ParcelFileDescriptor paramParcelFileDescriptor1, BackupDataOutput paramBackupDataOutput, ParcelFileDescriptor paramParcelFileDescriptor2, String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    if (paramArrayOfString1.length == 0) {
      return;
    }
    int i = paramArrayOfString1.length;
    int j = 0;
    while (j < i)
    {
      String str = paramArrayOfString1[j];
      if (str.charAt(0) == '/')
      {
        j++;
      }
      else
      {
        paramParcelFileDescriptor1 = new StringBuilder();
        paramParcelFileDescriptor1.append("files must have all absolute paths: ");
        paramParcelFileDescriptor1.append(str);
        throw new RuntimeException(paramParcelFileDescriptor1.toString());
      }
    }
    if (paramArrayOfString1.length == paramArrayOfString2.length)
    {
      if (paramParcelFileDescriptor1 != null) {}
      for (paramParcelFileDescriptor1 = paramParcelFileDescriptor1.getFileDescriptor();; paramParcelFileDescriptor1 = null) {
        break;
      }
      paramParcelFileDescriptor2 = paramParcelFileDescriptor2.getFileDescriptor();
      if (paramParcelFileDescriptor2 != null)
      {
        j = performBackup_native(paramParcelFileDescriptor1, mBackupWriter, paramParcelFileDescriptor2, paramArrayOfString1, paramArrayOfString2);
        if (j == 0) {
          return;
        }
        paramParcelFileDescriptor1 = new StringBuilder();
        paramParcelFileDescriptor1.append("Backup failed 0x");
        paramParcelFileDescriptor1.append(Integer.toHexString(j));
        throw new RuntimeException(paramParcelFileDescriptor1.toString());
      }
      throw new NullPointerException();
    }
    paramParcelFileDescriptor1 = new StringBuilder();
    paramParcelFileDescriptor1.append("files.length=");
    paramParcelFileDescriptor1.append(paramArrayOfString1.length);
    paramParcelFileDescriptor1.append(" keys.length=");
    paramParcelFileDescriptor1.append(paramArrayOfString2.length);
    throw new RuntimeException(paramParcelFileDescriptor1.toString());
  }
  
  private static native int performBackup_native(FileDescriptor paramFileDescriptor1, long paramLong, FileDescriptor paramFileDescriptor2, String[] paramArrayOfString1, String[] paramArrayOfString2);
  
  private static native int writeFile_native(long paramLong1, String paramString, long paramLong2);
  
  private static native int writeSnapshot_native(long paramLong, FileDescriptor paramFileDescriptor);
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      dtor(mPtr);
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  boolean isKeyInList(String paramString, String[] paramArrayOfString)
  {
    int i = paramArrayOfString.length;
    for (int j = 0; j < i; j++) {
      if (paramArrayOfString[j].equals(paramString)) {
        return true;
      }
    }
    return false;
  }
  
  boolean writeFile(File paramFile, BackupDataInputStream paramBackupDataInputStream)
  {
    paramFile.getParentFile().mkdirs();
    int i = writeFile_native(mPtr, paramFile.getAbsolutePath(), mData.mBackupReader);
    boolean bool = true;
    if ((i != 0) && (!mExceptionLogged))
    {
      paramBackupDataInputStream = new StringBuilder();
      paramBackupDataInputStream.append("Failed restoring file '");
      paramBackupDataInputStream.append(paramFile);
      paramBackupDataInputStream.append("' for app '");
      paramBackupDataInputStream.append(mContext.getPackageName());
      paramBackupDataInputStream.append("' result=0x");
      paramBackupDataInputStream.append(Integer.toHexString(i));
      Log.e("FileBackupHelperBase", paramBackupDataInputStream.toString());
      mExceptionLogged = true;
    }
    if (i != 0) {
      bool = false;
    }
    return bool;
  }
  
  public void writeNewStateDescription(ParcelFileDescriptor paramParcelFileDescriptor)
  {
    writeSnapshot_native(mPtr, paramParcelFileDescriptor.getFileDescriptor());
  }
}
