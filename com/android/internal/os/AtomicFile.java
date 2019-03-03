package com.android.internal.os;

import android.os.FileUtils;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public final class AtomicFile
{
  private final File mBackupName;
  private final File mBaseName;
  
  public AtomicFile(File paramFile)
  {
    mBaseName = paramFile;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramFile.getPath());
    localStringBuilder.append(".bak");
    mBackupName = new File(localStringBuilder.toString());
  }
  
  public void delete()
  {
    mBaseName.delete();
    mBackupName.delete();
  }
  
  public boolean exists()
  {
    boolean bool;
    if ((!mBaseName.exists()) && (!mBackupName.exists())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public void failWrite(FileOutputStream paramFileOutputStream)
  {
    if (paramFileOutputStream != null)
    {
      FileUtils.sync(paramFileOutputStream);
      try
      {
        paramFileOutputStream.close();
        mBaseName.delete();
        mBackupName.renameTo(mBaseName);
      }
      catch (IOException paramFileOutputStream)
      {
        Log.w("AtomicFile", "failWrite: Got exception:", paramFileOutputStream);
      }
    }
  }
  
  public void finishWrite(FileOutputStream paramFileOutputStream)
  {
    if (paramFileOutputStream != null)
    {
      FileUtils.sync(paramFileOutputStream);
      try
      {
        paramFileOutputStream.close();
        mBackupName.delete();
      }
      catch (IOException paramFileOutputStream)
      {
        Log.w("AtomicFile", "finishWrite: Got exception:", paramFileOutputStream);
      }
    }
  }
  
  public File getBaseFile()
  {
    return mBaseName;
  }
  
  public FileOutputStream openAppend()
    throws IOException
  {
    try
    {
      FileOutputStream localFileOutputStream = new FileOutputStream(mBaseName, true);
      return localFileOutputStream;
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Couldn't append ");
      localStringBuilder.append(mBaseName);
      throw new IOException(localStringBuilder.toString());
    }
  }
  
  public FileInputStream openRead()
    throws FileNotFoundException
  {
    if (mBackupName.exists())
    {
      mBaseName.delete();
      mBackupName.renameTo(mBaseName);
    }
    return new FileInputStream(mBaseName);
  }
  
  /* Error */
  public byte[] readFully()
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 103	com/android/internal/os/AtomicFile:openRead	()Ljava/io/FileInputStream;
    //   4: astore_1
    //   5: iconst_0
    //   6: istore_2
    //   7: aload_1
    //   8: invokevirtual 107	java/io/FileInputStream:available	()I
    //   11: newarray byte
    //   13: astore_3
    //   14: aload_1
    //   15: aload_3
    //   16: iload_2
    //   17: aload_3
    //   18: arraylength
    //   19: iload_2
    //   20: isub
    //   21: invokevirtual 111	java/io/FileInputStream:read	([BII)I
    //   24: istore 4
    //   26: iload 4
    //   28: ifgt +9 -> 37
    //   31: aload_1
    //   32: invokevirtual 112	java/io/FileInputStream:close	()V
    //   35: aload_3
    //   36: areturn
    //   37: iload_2
    //   38: iload 4
    //   40: iadd
    //   41: istore_2
    //   42: aload_1
    //   43: invokevirtual 107	java/io/FileInputStream:available	()I
    //   46: istore 4
    //   48: aload_3
    //   49: astore 5
    //   51: iload 4
    //   53: aload_3
    //   54: arraylength
    //   55: iload_2
    //   56: isub
    //   57: if_icmple +20 -> 77
    //   60: iload_2
    //   61: iload 4
    //   63: iadd
    //   64: newarray byte
    //   66: astore 5
    //   68: aload_3
    //   69: iconst_0
    //   70: aload 5
    //   72: iconst_0
    //   73: iload_2
    //   74: invokestatic 118	java/lang/System:arraycopy	([BI[BII)V
    //   77: aload 5
    //   79: astore_3
    //   80: goto -66 -> 14
    //   83: astore_3
    //   84: aload_1
    //   85: invokevirtual 112	java/io/FileInputStream:close	()V
    //   88: aload_3
    //   89: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	90	0	this	AtomicFile
    //   4	81	1	localFileInputStream	FileInputStream
    //   6	68	2	i	int
    //   13	67	3	localObject1	Object
    //   83	6	3	localObject2	Object
    //   24	40	4	j	int
    //   49	29	5	localObject3	Object
    // Exception table:
    //   from	to	target	type
    //   7	14	83	finally
    //   14	26	83	finally
    //   42	48	83	finally
    //   51	77	83	finally
  }
  
  public FileOutputStream startWrite()
    throws IOException
  {
    Object localObject1;
    if (mBaseName.exists()) {
      if (!mBackupName.exists())
      {
        if (!mBaseName.renameTo(mBackupName))
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Couldn't rename file ");
          ((StringBuilder)localObject1).append(mBaseName);
          ((StringBuilder)localObject1).append(" to backup file ");
          ((StringBuilder)localObject1).append(mBackupName);
          Log.w("AtomicFile", ((StringBuilder)localObject1).toString());
        }
      }
      else {
        mBaseName.delete();
      }
    }
    Object localObject2;
    try
    {
      localObject1 = new java/io/FileOutputStream;
      ((FileOutputStream)localObject1).<init>(mBaseName);
    }
    catch (FileNotFoundException localFileNotFoundException1)
    {
      localObject2 = mBaseName.getParentFile();
      if (!((File)localObject2).mkdir()) {
        break label190;
      }
    }
    FileUtils.setPermissions(((File)localObject2).getPath(), 505, -1, -1);
    try
    {
      localObject2 = new FileOutputStream(mBaseName);
      return localObject2;
    }
    catch (FileNotFoundException localFileNotFoundException2)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Couldn't create ");
      localStringBuilder.append(mBaseName);
      throw new IOException(localStringBuilder.toString());
    }
    label190:
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Couldn't create directory ");
    localStringBuilder.append(mBaseName);
    throw new IOException(localStringBuilder.toString());
  }
  
  public void truncate()
    throws IOException
  {
    try
    {
      try
      {
        FileOutputStream localFileOutputStream = new java/io/FileOutputStream;
        localFileOutputStream.<init>(mBaseName);
        FileUtils.sync(localFileOutputStream);
        localFileOutputStream.close();
      }
      catch (IOException localIOException) {}
      return;
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Couldn't append ");
      localStringBuilder.append(mBaseName);
      throw new IOException(localStringBuilder.toString());
    }
  }
}
