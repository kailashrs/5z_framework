package android.util;

import android.os.FileUtils;
import android.os.SystemClock;
import com.android.internal.logging.EventLogTags;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AtomicFile
{
  private final File mBackupName;
  private final File mBaseName;
  private final String mCommitTag;
  private long mStartTime;
  
  public AtomicFile(File paramFile)
  {
    this(paramFile, null);
  }
  
  public AtomicFile(File paramFile, String paramString)
  {
    mBaseName = paramFile;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramFile.getPath());
    localStringBuilder.append(".bak");
    mBackupName = new File(localStringBuilder.toString());
    mCommitTag = paramString;
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
      if (mCommitTag != null) {
        EventLogTags.writeCommitSysConfigFile(mCommitTag, SystemClock.uptimeMillis() - mStartTime);
      }
    }
  }
  
  public File getBaseFile()
  {
    return mBaseName;
  }
  
  public long getLastModifiedTime()
  {
    if (mBackupName.exists()) {
      return mBackupName.lastModified();
    }
    return mBaseName.lastModified();
  }
  
  @Deprecated
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
    //   1: invokevirtual 132	android/util/AtomicFile:openRead	()Ljava/io/FileInputStream;
    //   4: astore_1
    //   5: iconst_0
    //   6: istore_2
    //   7: aload_1
    //   8: invokevirtual 136	java/io/FileInputStream:available	()I
    //   11: newarray byte
    //   13: astore_3
    //   14: aload_1
    //   15: aload_3
    //   16: iload_2
    //   17: aload_3
    //   18: arraylength
    //   19: iload_2
    //   20: isub
    //   21: invokevirtual 140	java/io/FileInputStream:read	([BII)I
    //   24: istore 4
    //   26: iload 4
    //   28: ifgt +9 -> 37
    //   31: aload_1
    //   32: invokevirtual 141	java/io/FileInputStream:close	()V
    //   35: aload_3
    //   36: areturn
    //   37: iload_2
    //   38: iload 4
    //   40: iadd
    //   41: istore_2
    //   42: aload_1
    //   43: invokevirtual 136	java/io/FileInputStream:available	()I
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
    //   74: invokestatic 147	java/lang/System:arraycopy	([BI[BII)V
    //   77: aload 5
    //   79: astore_3
    //   80: goto -66 -> 14
    //   83: astore_3
    //   84: aload_1
    //   85: invokevirtual 141	java/io/FileInputStream:close	()V
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
    long l;
    if (mCommitTag != null) {
      l = SystemClock.uptimeMillis();
    } else {
      l = 0L;
    }
    return startWrite(l);
  }
  
  public FileOutputStream startWrite(long paramLong)
    throws IOException
  {
    mStartTime = paramLong;
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
      if (!((File)localObject2).mkdirs()) {
        break label195;
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
    label195:
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Couldn't create directory ");
    localStringBuilder.append(mBaseName);
    throw new IOException(localStringBuilder.toString());
  }
  
  @Deprecated
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
  
  /* Error */
  public void write(java.util.function.Consumer<FileOutputStream> paramConsumer)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: aload_0
    //   5: invokevirtual 180	android/util/AtomicFile:startWrite	()Ljava/io/FileOutputStream;
    //   8: astore 4
    //   10: aload 4
    //   12: astore_3
    //   13: aload 4
    //   15: astore_2
    //   16: aload_1
    //   17: aload 4
    //   19: invokeinterface 186 2 0
    //   24: aload 4
    //   26: astore_3
    //   27: aload 4
    //   29: astore_2
    //   30: aload_0
    //   31: aload 4
    //   33: invokevirtual 188	android/util/AtomicFile:finishWrite	(Ljava/io/FileOutputStream;)V
    //   36: aload 4
    //   38: invokestatic 194	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   41: return
    //   42: astore_1
    //   43: goto +18 -> 61
    //   46: astore_1
    //   47: aload_2
    //   48: astore_3
    //   49: aload_0
    //   50: aload_2
    //   51: invokevirtual 196	android/util/AtomicFile:failWrite	(Ljava/io/FileOutputStream;)V
    //   54: aload_2
    //   55: astore_3
    //   56: aload_1
    //   57: invokestatic 202	android/util/ExceptionUtils:propagate	(Ljava/lang/Throwable;)Ljava/lang/RuntimeException;
    //   60: athrow
    //   61: aload_3
    //   62: invokestatic 194	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   65: aload_1
    //   66: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	67	0	this	AtomicFile
    //   0	67	1	paramConsumer	java.util.function.Consumer<FileOutputStream>
    //   1	54	2	localObject1	Object
    //   3	59	3	localObject2	Object
    //   8	29	4	localFileOutputStream	FileOutputStream
    // Exception table:
    //   from	to	target	type
    //   4	10	42	finally
    //   16	24	42	finally
    //   30	36	42	finally
    //   49	54	42	finally
    //   56	61	42	finally
    //   4	10	46	java/lang/Throwable
    //   16	24	46	java/lang/Throwable
    //   30	36	46	java/lang/Throwable
  }
}
