package android.app.backup;

import android.os.ParcelFileDescriptor;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class BackupHelperDispatcher
{
  private static final String TAG = "BackupHelperDispatcher";
  TreeMap<String, BackupHelper> mHelpers = new TreeMap();
  
  public BackupHelperDispatcher() {}
  
  private static native int allocateHeader_native(Header paramHeader, FileDescriptor paramFileDescriptor);
  
  private void doOneBackup(ParcelFileDescriptor paramParcelFileDescriptor1, BackupDataOutput paramBackupDataOutput, ParcelFileDescriptor paramParcelFileDescriptor2, Header paramHeader, BackupHelper paramBackupHelper)
    throws IOException
  {
    FileDescriptor localFileDescriptor = paramParcelFileDescriptor2.getFileDescriptor();
    int i = allocateHeader_native(paramHeader, localFileDescriptor);
    if (i >= 0)
    {
      paramBackupDataOutput.setKeyPrefix(keyPrefix);
      paramBackupHelper.performBackup(paramParcelFileDescriptor1, paramBackupDataOutput, paramParcelFileDescriptor2);
      i = writeHeader_native(paramHeader, localFileDescriptor, i);
      if (i == 0) {
        return;
      }
      paramParcelFileDescriptor1 = new StringBuilder();
      paramParcelFileDescriptor1.append("writeHeader_native failed (error ");
      paramParcelFileDescriptor1.append(i);
      paramParcelFileDescriptor1.append(")");
      throw new IOException(paramParcelFileDescriptor1.toString());
    }
    paramParcelFileDescriptor1 = new StringBuilder();
    paramParcelFileDescriptor1.append("allocateHeader_native failed (error ");
    paramParcelFileDescriptor1.append(i);
    paramParcelFileDescriptor1.append(")");
    throw new IOException(paramParcelFileDescriptor1.toString());
  }
  
  private static native int readHeader_native(Header paramHeader, FileDescriptor paramFileDescriptor);
  
  private static native int skipChunk_native(FileDescriptor paramFileDescriptor, int paramInt);
  
  private static native int writeHeader_native(Header paramHeader, FileDescriptor paramFileDescriptor, int paramInt);
  
  public void addHelper(String paramString, BackupHelper paramBackupHelper)
  {
    mHelpers.put(paramString, paramBackupHelper);
  }
  
  public void performBackup(ParcelFileDescriptor paramParcelFileDescriptor1, BackupDataOutput paramBackupDataOutput, ParcelFileDescriptor paramParcelFileDescriptor2)
    throws IOException
  {
    Header localHeader = new Header(null);
    TreeMap localTreeMap = (TreeMap)mHelpers.clone();
    Object localObject1;
    Object localObject2;
    if (paramParcelFileDescriptor1 != null)
    {
      localObject1 = paramParcelFileDescriptor1.getFileDescriptor();
      for (;;)
      {
        int i = readHeader_native(localHeader, (FileDescriptor)localObject1);
        if (i < 0) {
          break;
        }
        if (i == 0)
        {
          localObject2 = (BackupHelper)localTreeMap.get(keyPrefix);
          localObject3 = new StringBuilder();
          ((StringBuilder)localObject3).append("handling existing helper '");
          ((StringBuilder)localObject3).append(keyPrefix);
          ((StringBuilder)localObject3).append("' ");
          ((StringBuilder)localObject3).append(localObject2);
          Log.d("BackupHelperDispatcher", ((StringBuilder)localObject3).toString());
          if (localObject2 != null)
          {
            doOneBackup(paramParcelFileDescriptor1, paramBackupDataOutput, paramParcelFileDescriptor2, localHeader, (BackupHelper)localObject2);
            localTreeMap.remove(keyPrefix);
          }
          else
          {
            skipChunk_native((FileDescriptor)localObject1, chunkSize);
          }
        }
      }
    }
    Object localObject3 = localTreeMap.entrySet().iterator();
    while (((Iterator)localObject3).hasNext())
    {
      localObject2 = (Map.Entry)((Iterator)localObject3).next();
      keyPrefix = ((String)((Map.Entry)localObject2).getKey());
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("handling new helper '");
      ((StringBuilder)localObject1).append(keyPrefix);
      ((StringBuilder)localObject1).append("'");
      Log.d("BackupHelperDispatcher", ((StringBuilder)localObject1).toString());
      doOneBackup(paramParcelFileDescriptor1, paramBackupDataOutput, paramParcelFileDescriptor2, localHeader, (BackupHelper)((Map.Entry)localObject2).getValue());
    }
  }
  
  public void performRestore(BackupDataInput paramBackupDataInput, int paramInt, ParcelFileDescriptor paramParcelFileDescriptor)
    throws IOException
  {
    paramInt = 0;
    BackupDataInputStream localBackupDataInputStream = new BackupDataInputStream(paramBackupDataInput);
    while (paramBackupDataInput.readNextHeader())
    {
      String str = paramBackupDataInput.getKey();
      int i = str.indexOf(':');
      Object localObject;
      if (i > 0)
      {
        localObject = str.substring(0, i);
        localObject = (BackupHelper)mHelpers.get(localObject);
        if (localObject != null)
        {
          dataSize = paramBackupDataInput.getDataSize();
          key = str.substring(i + 1);
          ((BackupHelper)localObject).restoreEntity(localBackupDataInputStream);
          i = paramInt;
        }
        else
        {
          i = paramInt;
          if (paramInt == 0)
          {
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append("Couldn't find helper for: '");
            ((StringBuilder)localObject).append(str);
            ((StringBuilder)localObject).append("'");
            Log.w("BackupHelperDispatcher", ((StringBuilder)localObject).toString());
            i = 1;
          }
        }
      }
      else
      {
        i = paramInt;
        if (paramInt == 0)
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Entity with no prefix: '");
          ((StringBuilder)localObject).append(str);
          ((StringBuilder)localObject).append("'");
          Log.w("BackupHelperDispatcher", ((StringBuilder)localObject).toString());
          i = 1;
        }
      }
      paramBackupDataInput.skipEntityData();
      paramInt = i;
    }
    paramBackupDataInput = mHelpers.values().iterator();
    while (paramBackupDataInput.hasNext()) {
      ((BackupHelper)paramBackupDataInput.next()).writeNewStateDescription(paramParcelFileDescriptor);
    }
  }
  
  private static class Header
  {
    int chunkSize;
    String keyPrefix;
    
    private Header() {}
  }
}
