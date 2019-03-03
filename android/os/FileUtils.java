package android.os;

import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructStat;
import android.text.TextUtils;
import android.util.Log;
import android.util.Slog;
import android.webkit.MimeTypeMap;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.SizedInputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import libcore.io.IoUtils;
import libcore.util.EmptyArray;

public class FileUtils
{
  private static final long COPY_CHECKPOINT_BYTES = 524288L;
  private static final File[] EMPTY = new File[0];
  private static final boolean ENABLE_COPY_OPTIMIZATIONS = true;
  public static final int S_IRGRP = 32;
  public static final int S_IROTH = 4;
  public static final int S_IRUSR = 256;
  public static final int S_IRWXG = 56;
  public static final int S_IRWXO = 7;
  public static final int S_IRWXU = 448;
  public static final int S_IWGRP = 16;
  public static final int S_IWOTH = 2;
  public static final int S_IWUSR = 128;
  public static final int S_IXGRP = 8;
  public static final int S_IXOTH = 1;
  public static final int S_IXUSR = 64;
  private static final String TAG = "FileUtils";
  
  public FileUtils() {}
  
  private static File buildFile(File paramFile, String paramString1, String paramString2)
  {
    if (TextUtils.isEmpty(paramString2)) {
      return new File(paramFile, paramString1);
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString1);
    localStringBuilder.append(".");
    localStringBuilder.append(paramString2);
    return new File(paramFile, localStringBuilder.toString());
  }
  
  public static File buildUniqueFile(File paramFile, String paramString)
    throws FileNotFoundException
  {
    int i = paramString.lastIndexOf('.');
    String str;
    if (i >= 0)
    {
      str = paramString.substring(0, i);
      paramString = paramString.substring(i + 1);
    }
    else
    {
      str = paramString;
      paramString = null;
    }
    return buildUniqueFileWithExtension(paramFile, str, paramString);
  }
  
  public static File buildUniqueFile(File paramFile, String paramString1, String paramString2)
    throws FileNotFoundException
  {
    paramString1 = splitFileName(paramString1, paramString2);
    return buildUniqueFileWithExtension(paramFile, paramString1[0], paramString1[1]);
  }
  
  private static File buildUniqueFileWithExtension(File paramFile, String paramString1, String paramString2)
    throws FileNotFoundException
  {
    Object localObject = buildFile(paramFile, paramString1, paramString2);
    int i = 0;
    while (((File)localObject).exists())
    {
      int j = i + 1;
      if (i < 32)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(paramString1);
        ((StringBuilder)localObject).append(" (");
        ((StringBuilder)localObject).append(j);
        ((StringBuilder)localObject).append(")");
        localObject = buildFile(paramFile, ((StringBuilder)localObject).toString(), paramString2);
        i = j;
      }
      else
      {
        throw new FileNotFoundException("Failed to create unique file");
      }
    }
    return localObject;
  }
  
  public static String buildValidExtFilename(String paramString)
  {
    if ((!TextUtils.isEmpty(paramString)) && (!".".equals(paramString)) && (!"..".equals(paramString)))
    {
      StringBuilder localStringBuilder = new StringBuilder(paramString.length());
      for (int i = 0; i < paramString.length(); i++)
      {
        char c = paramString.charAt(i);
        if (isValidExtFilenameChar(c)) {
          localStringBuilder.append(c);
        } else {
          localStringBuilder.append('_');
        }
      }
      trimFilename(localStringBuilder, 255);
      return localStringBuilder.toString();
    }
    return "(invalid)";
  }
  
  public static String buildValidFatFilename(String paramString)
  {
    if ((!TextUtils.isEmpty(paramString)) && (!".".equals(paramString)) && (!"..".equals(paramString)))
    {
      StringBuilder localStringBuilder = new StringBuilder(paramString.length());
      for (int i = 0; i < paramString.length(); i++)
      {
        char c = paramString.charAt(i);
        if (isValidFatFilenameChar(c)) {
          localStringBuilder.append(c);
        } else {
          localStringBuilder.append('_');
        }
      }
      trimFilename(localStringBuilder, 255);
      return localStringBuilder.toString();
    }
    return "(invalid)";
  }
  
  /* Error */
  public static void bytesToFile(String paramString, byte[] paramArrayOfByte)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: ldc -72
    //   3: invokevirtual 188	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   6: istore_2
    //   7: aconst_null
    //   8: astore_3
    //   9: aconst_null
    //   10: astore 4
    //   12: iload_2
    //   13: ifeq +62 -> 75
    //   16: invokestatic 193	android/os/StrictMode:allowThreadDiskWritesMask	()I
    //   19: istore 5
    //   21: new 195	java/io/FileOutputStream
    //   24: astore_3
    //   25: aload_3
    //   26: aload_0
    //   27: invokespecial 196	java/io/FileOutputStream:<init>	(Ljava/lang/String;)V
    //   30: aload 4
    //   32: astore_0
    //   33: aload_3
    //   34: aload_1
    //   35: invokevirtual 200	java/io/FileOutputStream:write	([B)V
    //   38: aconst_null
    //   39: aload_3
    //   40: invokestatic 202	android/os/FileUtils:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   43: iload 5
    //   45: invokestatic 205	android/os/StrictMode:setThreadPolicyMask	(I)V
    //   48: goto +51 -> 99
    //   51: astore_1
    //   52: goto +8 -> 60
    //   55: astore_1
    //   56: aload_1
    //   57: astore_0
    //   58: aload_1
    //   59: athrow
    //   60: aload_0
    //   61: aload_3
    //   62: invokestatic 202	android/os/FileUtils:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   65: aload_1
    //   66: athrow
    //   67: astore_0
    //   68: iload 5
    //   70: invokestatic 205	android/os/StrictMode:setThreadPolicyMask	(I)V
    //   73: aload_0
    //   74: athrow
    //   75: new 195	java/io/FileOutputStream
    //   78: dup
    //   79: aload_0
    //   80: invokespecial 196	java/io/FileOutputStream:<init>	(Ljava/lang/String;)V
    //   83: astore 4
    //   85: aload_3
    //   86: astore_0
    //   87: aload 4
    //   89: aload_1
    //   90: invokevirtual 200	java/io/FileOutputStream:write	([B)V
    //   93: aconst_null
    //   94: aload 4
    //   96: invokestatic 202	android/os/FileUtils:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   99: return
    //   100: astore_1
    //   101: goto +8 -> 109
    //   104: astore_1
    //   105: aload_1
    //   106: astore_0
    //   107: aload_1
    //   108: athrow
    //   109: aload_0
    //   110: aload 4
    //   112: invokestatic 202	android/os/FileUtils:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   115: aload_1
    //   116: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	117	0	paramString	String
    //   0	117	1	paramArrayOfByte	byte[]
    //   6	7	2	bool	boolean
    //   8	78	3	localFileOutputStream1	FileOutputStream
    //   10	101	4	localFileOutputStream2	FileOutputStream
    //   19	50	5	i	int
    // Exception table:
    //   from	to	target	type
    //   33	38	51	finally
    //   58	60	51	finally
    //   33	38	55	java/lang/Throwable
    //   21	30	67	finally
    //   38	43	67	finally
    //   60	67	67	finally
    //   87	93	100	finally
    //   107	109	100	finally
    //   87	93	104	java/lang/Throwable
  }
  
  public static long checksumCrc32(File paramFile)
    throws FileNotFoundException, IOException
  {
    CRC32 localCRC32 = new CRC32();
    byte[] arrayOfByte = null;
    Object localObject = arrayOfByte;
    try
    {
      CheckedInputStream localCheckedInputStream = new java/util/zip/CheckedInputStream;
      localObject = arrayOfByte;
      FileInputStream localFileInputStream = new java/io/FileInputStream;
      localObject = arrayOfByte;
      localFileInputStream.<init>(paramFile);
      localObject = arrayOfByte;
      localCheckedInputStream.<init>(localFileInputStream, localCRC32);
      paramFile = localCheckedInputStream;
      localObject = paramFile;
      arrayOfByte = new byte[''];
      do
      {
        localObject = paramFile;
      } while (paramFile.read(arrayOfByte) >= 0);
      localObject = paramFile;
      long l = localCRC32.getValue();
      try
      {
        paramFile.close();
      }
      catch (IOException paramFile) {}
      return l;
    }
    finally
    {
      if (localObject != null) {
        try
        {
          ((CheckedInputStream)localObject).close();
        }
        catch (IOException localIOException) {}
      }
    }
  }
  
  public static boolean contains(File paramFile1, File paramFile2)
  {
    if ((paramFile1 != null) && (paramFile2 != null)) {
      return contains(paramFile1.getAbsolutePath(), paramFile2.getAbsolutePath());
    }
    return false;
  }
  
  public static boolean contains(String paramString1, String paramString2)
  {
    if (paramString1.equals(paramString2)) {
      return true;
    }
    Object localObject = paramString1;
    if (!paramString1.endsWith("/"))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString1);
      ((StringBuilder)localObject).append("/");
      localObject = ((StringBuilder)localObject).toString();
    }
    return paramString2.startsWith((String)localObject);
  }
  
  public static boolean contains(File[] paramArrayOfFile, File paramFile)
  {
    int i = paramArrayOfFile.length;
    for (int j = 0; j < i; j++) {
      if (contains(paramArrayOfFile[j], paramFile)) {
        return true;
      }
    }
    return false;
  }
  
  public static long copy(File paramFile1, File paramFile2)
    throws IOException
  {
    return copy(paramFile1, paramFile2, null, null);
  }
  
  /* Error */
  public static long copy(File paramFile1, File paramFile2, ProgressListener paramProgressListener, CancellationSignal paramCancellationSignal)
    throws IOException
  {
    // Byte code:
    //   0: new 214	java/io/FileInputStream
    //   3: dup
    //   4: aload_0
    //   5: invokespecial 217	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   8: astore 4
    //   10: aconst_null
    //   11: astore 5
    //   13: aload 5
    //   15: astore_0
    //   16: new 195	java/io/FileOutputStream
    //   19: astore 6
    //   21: aload 5
    //   23: astore_0
    //   24: aload 6
    //   26: aload_1
    //   27: invokespecial 251	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   30: aload 4
    //   32: aload 6
    //   34: aload_2
    //   35: aload_3
    //   36: invokestatic 254	android/os/FileUtils:copy	(Ljava/io/InputStream;Ljava/io/OutputStream;Landroid/os/FileUtils$ProgressListener;Landroid/os/CancellationSignal;)J
    //   39: lstore 7
    //   41: aload 5
    //   43: astore_0
    //   44: aconst_null
    //   45: aload 6
    //   47: invokestatic 202	android/os/FileUtils:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   50: aconst_null
    //   51: aload 4
    //   53: invokestatic 202	android/os/FileUtils:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   56: lload 7
    //   58: lreturn
    //   59: astore_1
    //   60: aconst_null
    //   61: astore_2
    //   62: goto +7 -> 69
    //   65: astore_2
    //   66: aload_2
    //   67: athrow
    //   68: astore_1
    //   69: aload 5
    //   71: astore_0
    //   72: aload_2
    //   73: aload 6
    //   75: invokestatic 202	android/os/FileUtils:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   78: aload 5
    //   80: astore_0
    //   81: aload_1
    //   82: athrow
    //   83: astore_1
    //   84: goto +8 -> 92
    //   87: astore_1
    //   88: aload_1
    //   89: astore_0
    //   90: aload_1
    //   91: athrow
    //   92: aload_0
    //   93: aload 4
    //   95: invokestatic 202	android/os/FileUtils:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   98: aload_1
    //   99: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	100	0	paramFile1	File
    //   0	100	1	paramFile2	File
    //   0	100	2	paramProgressListener	ProgressListener
    //   0	100	3	paramCancellationSignal	CancellationSignal
    //   8	86	4	localFileInputStream	FileInputStream
    //   11	68	5	localObject	Object
    //   19	55	6	localFileOutputStream	FileOutputStream
    //   39	18	7	l	long
    // Exception table:
    //   from	to	target	type
    //   30	41	59	finally
    //   30	41	65	java/lang/Throwable
    //   66	68	68	finally
    //   16	21	83	finally
    //   24	30	83	finally
    //   44	50	83	finally
    //   72	78	83	finally
    //   81	83	83	finally
    //   90	92	83	finally
    //   16	21	87	java/lang/Throwable
    //   24	30	87	java/lang/Throwable
    //   44	50	87	java/lang/Throwable
    //   72	78	87	java/lang/Throwable
    //   81	83	87	java/lang/Throwable
  }
  
  public static long copy(FileDescriptor paramFileDescriptor1, FileDescriptor paramFileDescriptor2)
    throws IOException
  {
    return copy(paramFileDescriptor1, paramFileDescriptor2, null, null);
  }
  
  public static long copy(FileDescriptor paramFileDescriptor1, FileDescriptor paramFileDescriptor2, ProgressListener paramProgressListener, CancellationSignal paramCancellationSignal)
    throws IOException
  {
    return copy(paramFileDescriptor1, paramFileDescriptor2, paramProgressListener, paramCancellationSignal, Long.MAX_VALUE);
  }
  
  public static long copy(FileDescriptor paramFileDescriptor1, FileDescriptor paramFileDescriptor2, ProgressListener paramProgressListener, CancellationSignal paramCancellationSignal, long paramLong)
    throws IOException
  {
    try
    {
      StructStat localStructStat1 = Os.fstat(paramFileDescriptor1);
      StructStat localStructStat2 = Os.fstat(paramFileDescriptor2);
      if ((OsConstants.S_ISREG(st_mode)) && (OsConstants.S_ISREG(st_mode))) {
        return copyInternalSendfile(paramFileDescriptor1, paramFileDescriptor2, paramProgressListener, paramCancellationSignal, paramLong);
      }
      if (!OsConstants.S_ISFIFO(st_mode))
      {
        boolean bool = OsConstants.S_ISFIFO(st_mode);
        if (!bool) {
          return copyInternalUserspace(paramFileDescriptor1, paramFileDescriptor2, paramProgressListener, paramCancellationSignal, paramLong);
        }
      }
      paramLong = copyInternalSplice(paramFileDescriptor1, paramFileDescriptor2, paramProgressListener, paramCancellationSignal, paramLong);
      return paramLong;
    }
    catch (ErrnoException paramFileDescriptor1)
    {
      throw paramFileDescriptor1.rethrowAsIOException();
    }
  }
  
  public static long copy(InputStream paramInputStream, OutputStream paramOutputStream)
    throws IOException
  {
    return copy(paramInputStream, paramOutputStream, null, null);
  }
  
  public static long copy(InputStream paramInputStream, OutputStream paramOutputStream, ProgressListener paramProgressListener, CancellationSignal paramCancellationSignal)
    throws IOException
  {
    if (((paramInputStream instanceof FileInputStream)) && ((paramOutputStream instanceof FileOutputStream))) {
      return copy(((FileInputStream)paramInputStream).getFD(), ((FileOutputStream)paramOutputStream).getFD(), paramProgressListener, paramCancellationSignal);
    }
    return copyInternalUserspace(paramInputStream, paramOutputStream, paramProgressListener, paramCancellationSignal);
  }
  
  @Deprecated
  public static boolean copyFile(File paramFile1, File paramFile2)
  {
    try
    {
      copyFileOrThrow(paramFile1, paramFile2);
      return true;
    }
    catch (IOException paramFile1) {}
    return false;
  }
  
  /* Error */
  @Deprecated
  public static void copyFileOrThrow(File paramFile1, File paramFile2)
    throws IOException
  {
    // Byte code:
    //   0: new 214	java/io/FileInputStream
    //   3: dup
    //   4: aload_0
    //   5: invokespecial 217	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   8: astore_2
    //   9: aconst_null
    //   10: astore_0
    //   11: aload_2
    //   12: aload_1
    //   13: invokestatic 317	android/os/FileUtils:copyToFileOrThrow	(Ljava/io/InputStream;Ljava/io/File;)V
    //   16: aconst_null
    //   17: aload_2
    //   18: invokestatic 202	android/os/FileUtils:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   21: return
    //   22: astore_1
    //   23: goto +8 -> 31
    //   26: astore_1
    //   27: aload_1
    //   28: astore_0
    //   29: aload_1
    //   30: athrow
    //   31: aload_0
    //   32: aload_2
    //   33: invokestatic 202	android/os/FileUtils:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   36: aload_1
    //   37: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	38	0	paramFile1	File
    //   0	38	1	paramFile2	File
    //   8	25	2	localFileInputStream	FileInputStream
    // Exception table:
    //   from	to	target	type
    //   11	16	22	finally
    //   29	31	22	finally
    //   11	16	26	java/lang/Throwable
  }
  
  @VisibleForTesting
  public static long copyInternalSendfile(FileDescriptor paramFileDescriptor1, FileDescriptor paramFileDescriptor2, ProgressListener paramProgressListener, CancellationSignal paramCancellationSignal, long paramLong)
    throws ErrnoException
  {
    long l1 = 0L;
    long l2 = 0L;
    long l3 = paramLong;
    paramLong = l2;
    for (;;)
    {
      long l4 = Os.sendfile(paramFileDescriptor2, paramFileDescriptor1, null, Math.min(l3, 524288L));
      if (l4 == 0L) {
        break;
      }
      l2 = l1 + l4;
      long l5 = paramLong + l4;
      l4 = l3 - l4;
      paramLong = l5;
      l3 = l4;
      l1 = l2;
      if (l5 >= 524288L)
      {
        if (paramCancellationSignal != null) {
          paramCancellationSignal.throwIfCanceled();
        }
        if (paramProgressListener != null) {
          paramProgressListener.onProgress(l2);
        }
        paramLong = 0L;
        l3 = l4;
        l1 = l2;
      }
    }
    if (paramProgressListener != null) {
      paramProgressListener.onProgress(l1);
    }
    return l1;
  }
  
  @VisibleForTesting
  public static long copyInternalSplice(FileDescriptor paramFileDescriptor1, FileDescriptor paramFileDescriptor2, ProgressListener paramProgressListener, CancellationSignal paramCancellationSignal, long paramLong)
    throws ErrnoException
  {
    long l1 = 0L;
    long l2 = 0L;
    long l3 = paramLong;
    paramLong = l2;
    for (;;)
    {
      long l4 = Os.splice(paramFileDescriptor1, null, paramFileDescriptor2, null, Math.min(l3, 524288L), OsConstants.SPLICE_F_MOVE | OsConstants.SPLICE_F_MORE);
      if (l4 == 0L) {
        break;
      }
      l2 = l1 + l4;
      long l5 = paramLong + l4;
      l4 = l3 - l4;
      paramLong = l5;
      l3 = l4;
      l1 = l2;
      if (l5 >= 524288L)
      {
        if (paramCancellationSignal != null) {
          paramCancellationSignal.throwIfCanceled();
        }
        if (paramProgressListener != null) {
          paramProgressListener.onProgress(l2);
        }
        paramLong = 0L;
        l3 = l4;
        l1 = l2;
      }
    }
    if (paramProgressListener != null) {
      paramProgressListener.onProgress(l1);
    }
    return l1;
  }
  
  @VisibleForTesting
  public static long copyInternalUserspace(FileDescriptor paramFileDescriptor1, FileDescriptor paramFileDescriptor2, ProgressListener paramProgressListener, CancellationSignal paramCancellationSignal, long paramLong)
    throws IOException
  {
    if (paramLong != Long.MAX_VALUE) {
      return copyInternalUserspace(new SizedInputStream(new FileInputStream(paramFileDescriptor1), paramLong), new FileOutputStream(paramFileDescriptor2), paramProgressListener, paramCancellationSignal);
    }
    return copyInternalUserspace(new FileInputStream(paramFileDescriptor1), new FileOutputStream(paramFileDescriptor2), paramProgressListener, paramCancellationSignal);
  }
  
  @VisibleForTesting
  public static long copyInternalUserspace(InputStream paramInputStream, OutputStream paramOutputStream, ProgressListener paramProgressListener, CancellationSignal paramCancellationSignal)
    throws IOException
  {
    long l1 = 0L;
    long l2 = 0L;
    byte[] arrayOfByte = new byte[' '];
    for (;;)
    {
      int i = paramInputStream.read(arrayOfByte);
      if (i == -1) {
        break;
      }
      paramOutputStream.write(arrayOfByte, 0, i);
      long l3 = l1 + i;
      long l4 = l2 + i;
      l1 = l3;
      l2 = l4;
      if (l4 >= 524288L)
      {
        if (paramCancellationSignal != null) {
          paramCancellationSignal.throwIfCanceled();
        }
        if (paramProgressListener != null) {
          paramProgressListener.onProgress(l3);
        }
        l2 = 0L;
        l1 = l3;
      }
    }
    if (paramProgressListener != null) {
      paramProgressListener.onProgress(l1);
    }
    return l1;
  }
  
  public static void copyPermissions(File paramFile1, File paramFile2)
    throws IOException
  {
    try
    {
      paramFile1 = Os.stat(paramFile1.getAbsolutePath());
      Os.chmod(paramFile2.getAbsolutePath(), st_mode);
      Os.chown(paramFile2.getAbsolutePath(), st_uid, st_gid);
      return;
    }
    catch (ErrnoException paramFile1)
    {
      throw paramFile1.rethrowAsIOException();
    }
  }
  
  @Deprecated
  public static boolean copyToFile(InputStream paramInputStream, File paramFile)
  {
    try
    {
      copyToFileOrThrow(paramInputStream, paramFile);
      return true;
    }
    catch (IOException paramInputStream) {}
    return false;
  }
  
  /* Error */
  @Deprecated
  public static void copyToFileOrThrow(InputStream paramInputStream, File paramFile)
    throws IOException
  {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual 130	java/io/File:exists	()Z
    //   4: ifeq +8 -> 12
    //   7: aload_1
    //   8: invokevirtual 389	java/io/File:delete	()Z
    //   11: pop
    //   12: new 195	java/io/FileOutputStream
    //   15: dup
    //   16: aload_1
    //   17: invokespecial 251	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   20: astore_2
    //   21: aconst_null
    //   22: astore_3
    //   23: aload_3
    //   24: astore_1
    //   25: aload_0
    //   26: aload_2
    //   27: invokestatic 391	android/os/FileUtils:copy	(Ljava/io/InputStream;Ljava/io/OutputStream;)J
    //   30: pop2
    //   31: aload_3
    //   32: astore_1
    //   33: aload_2
    //   34: invokevirtual 304	java/io/FileOutputStream:getFD	()Ljava/io/FileDescriptor;
    //   37: invokestatic 394	android/system/Os:fsync	(Ljava/io/FileDescriptor;)V
    //   40: aconst_null
    //   41: aload_2
    //   42: invokestatic 202	android/os/FileUtils:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   45: return
    //   46: astore_0
    //   47: aload_3
    //   48: astore_1
    //   49: aload_0
    //   50: invokevirtual 298	android/system/ErrnoException:rethrowAsIOException	()Ljava/io/IOException;
    //   53: athrow
    //   54: astore_0
    //   55: goto +8 -> 63
    //   58: astore_0
    //   59: aload_0
    //   60: astore_1
    //   61: aload_0
    //   62: athrow
    //   63: aload_1
    //   64: aload_2
    //   65: invokestatic 202	android/os/FileUtils:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   68: aload_0
    //   69: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	70	0	paramInputStream	InputStream
    //   0	70	1	paramFile	File
    //   20	45	2	localFileOutputStream	FileOutputStream
    //   22	26	3	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   33	40	46	android/system/ErrnoException
    //   25	31	54	finally
    //   33	40	54	finally
    //   49	54	54	finally
    //   61	63	54	finally
    //   25	31	58	java/lang/Throwable
    //   33	40	58	java/lang/Throwable
    //   49	54	58	java/lang/Throwable
  }
  
  public static File createDir(File paramFile, String paramString)
  {
    paramFile = new File(paramFile, paramString);
    boolean bool = paramFile.exists();
    Object localObject = null;
    paramString = null;
    if (bool)
    {
      if (paramFile.isDirectory()) {
        paramString = paramFile;
      }
      return paramString;
    }
    paramString = localObject;
    if (paramFile.mkdir()) {
      paramString = paramFile;
    }
    return paramString;
  }
  
  public static boolean deleteContents(File paramFile)
  {
    File[] arrayOfFile = paramFile.listFiles();
    boolean bool1 = true;
    boolean bool2 = true;
    if (arrayOfFile != null)
    {
      int i = arrayOfFile.length;
      for (int j = 0;; j++)
      {
        bool1 = bool2;
        if (j >= i) {
          break;
        }
        paramFile = arrayOfFile[j];
        bool1 = bool2;
        if (paramFile.isDirectory()) {
          bool1 = bool2 & deleteContents(paramFile);
        }
        bool2 = bool1;
        if (!paramFile.delete())
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Failed to delete ");
          localStringBuilder.append(paramFile);
          Log.w("FileUtils", localStringBuilder.toString());
          bool2 = false;
        }
      }
    }
    return bool1;
  }
  
  public static boolean deleteContentsAndDir(File paramFile)
  {
    if (deleteContents(paramFile)) {
      return paramFile.delete();
    }
    return false;
  }
  
  public static boolean deleteOlderFiles(File paramFile, int paramInt, long paramLong)
  {
    if ((paramInt >= 0) && (paramLong >= 0L))
    {
      paramFile = paramFile.listFiles();
      if (paramFile == null) {
        return false;
      }
      Arrays.sort(paramFile, new Comparator()
      {
        public int compare(File paramAnonymousFile1, File paramAnonymousFile2)
        {
          return Long.compare(paramAnonymousFile2.lastModified(), paramAnonymousFile1.lastModified());
        }
      });
      boolean bool2;
      for (boolean bool1 = false; paramInt < paramFile.length; bool1 = bool2)
      {
        Object localObject = paramFile[paramInt];
        bool2 = bool1;
        if (System.currentTimeMillis() - localObject.lastModified() > paramLong)
        {
          bool2 = bool1;
          if (localObject.delete())
          {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("Deleted old file ");
            localStringBuilder.append(localObject);
            Log.d("FileUtils", localStringBuilder.toString());
            bool2 = true;
          }
        }
        paramInt++;
      }
      return bool1;
    }
    throw new IllegalArgumentException("Constraints must be positive or 0");
  }
  
  public static int getUid(String paramString)
  {
    try
    {
      int i = statst_uid;
      return i;
    }
    catch (ErrnoException paramString) {}
    return -1;
  }
  
  public static boolean isFilenameSafe(File paramFile)
  {
    return NoImagePreloadHolder.SAFE_FILENAME_PATTERN.matcher(paramFile.getPath()).matches();
  }
  
  public static boolean isValidExtFilename(String paramString)
  {
    boolean bool;
    if ((paramString != null) && (paramString.equals(buildValidExtFilename(paramString)))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static boolean isValidExtFilenameChar(char paramChar)
  {
    return (paramChar != 0) && (paramChar != '/');
  }
  
  public static boolean isValidFatFilename(String paramString)
  {
    boolean bool;
    if ((paramString != null) && (paramString.equals(buildValidFatFilename(paramString)))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static boolean isValidFatFilenameChar(char paramChar)
  {
    if ((paramChar >= 0) && (paramChar <= '\037')) {
      return false;
    }
    if ((paramChar != '"') && (paramChar != '*') && (paramChar != '/') && (paramChar != ':') && (paramChar != '<') && (paramChar != '\\') && (paramChar != '|') && (paramChar != '')) {
      switch (paramChar)
      {
      default: 
        return true;
      }
    }
    return false;
  }
  
  public static File[] listFilesOrEmpty(File paramFile)
  {
    if (paramFile == null) {
      return EMPTY;
    }
    paramFile = paramFile.listFiles();
    if (paramFile != null) {
      return paramFile;
    }
    return EMPTY;
  }
  
  public static File[] listFilesOrEmpty(File paramFile, FilenameFilter paramFilenameFilter)
  {
    if (paramFile == null) {
      return EMPTY;
    }
    paramFile = paramFile.listFiles(paramFilenameFilter);
    if (paramFile != null) {
      return paramFile;
    }
    return EMPTY;
  }
  
  public static String[] listOrEmpty(File paramFile)
  {
    if (paramFile == null) {
      return EmptyArray.STRING;
    }
    paramFile = paramFile.list();
    if (paramFile != null) {
      return paramFile;
    }
    return EmptyArray.STRING;
  }
  
  public static File newFileOrNull(String paramString)
  {
    if (paramString != null) {
      paramString = new File(paramString);
    } else {
      paramString = null;
    }
    return paramString;
  }
  
  public static String readTextFile(File paramFile, int paramInt, String paramString)
    throws IOException
  {
    FileInputStream localFileInputStream = new FileInputStream(paramFile);
    BufferedInputStream localBufferedInputStream = new BufferedInputStream(localFileInputStream);
    try
    {
      long l = paramFile.length();
      if ((paramInt <= 0) && ((l <= 0L) || (paramInt != 0)))
      {
        if (paramInt < 0)
        {
          int i = 0;
          localObject1 = null;
          paramFile = null;
          File localFile;
          int k;
          do
          {
            j = i;
            if (localObject1 != null) {
              j = 1;
            }
            localFile = paramFile;
            paramFile = (File)localObject1;
            localObject2 = paramFile;
            if (paramFile == null) {
              localObject2 = new byte[-paramInt];
            }
            k = localBufferedInputStream.read((byte[])localObject2);
            i = j;
            localObject1 = localFile;
            paramFile = (File)localObject2;
          } while (k == localObject2.length);
          if ((localFile == null) && (k <= 0)) {
            return "";
          }
          if (localFile == null)
          {
            paramFile = new String((byte[])localObject2, 0, k);
            return paramFile;
          }
          if (k > 0)
          {
            j = 1;
            System.arraycopy(localFile, k, localFile, 0, localFile.length - k);
            System.arraycopy((byte[])localObject2, 0, localFile, localFile.length - k, k);
          }
          if ((paramString != null) && (j != 0))
          {
            paramFile = new java/lang/StringBuilder;
            paramFile.<init>();
            paramFile.append(paramString);
            paramString = new java/lang/String;
            paramString.<init>(localFile);
            paramFile.append(paramString);
            paramFile = paramFile.toString();
            return paramFile;
          }
          paramFile = new String(localFile);
          return paramFile;
        }
        paramString = new java/io/ByteArrayOutputStream;
        paramString.<init>();
        paramFile = new byte['Ѐ'];
        do
        {
          paramInt = localBufferedInputStream.read(paramFile);
          if (paramInt > 0) {
            paramString.write(paramFile, 0, paramInt);
          }
        } while (paramInt == paramFile.length);
        paramFile = paramString.toString();
        return paramFile;
      }
      int j = paramInt;
      if (l > 0L) {
        if (paramInt != 0)
        {
          j = paramInt;
          if (l >= paramInt) {}
        }
        else
        {
          j = (int)l;
        }
      }
      Object localObject2 = new byte[j + 1];
      paramInt = localBufferedInputStream.read((byte[])localObject2);
      if (paramInt <= 0) {
        return "";
      }
      if (paramInt <= j)
      {
        paramFile = new String((byte[])localObject2, 0, paramInt);
        return paramFile;
      }
      if (paramString == null)
      {
        paramFile = new String((byte[])localObject2, 0, j);
        return paramFile;
      }
      Object localObject1 = new java/lang/StringBuilder;
      ((StringBuilder)localObject1).<init>();
      paramFile = new java/lang/String;
      paramFile.<init>((byte[])localObject2, 0, j);
      ((StringBuilder)localObject1).append(paramFile);
      ((StringBuilder)localObject1).append(paramString);
      paramFile = ((StringBuilder)localObject1).toString();
      return paramFile;
    }
    finally
    {
      localBufferedInputStream.close();
      localFileInputStream.close();
    }
  }
  
  public static File rewriteAfterRename(File paramFile1, File paramFile2, File paramFile3)
  {
    if ((paramFile3 != null) && (paramFile1 != null) && (paramFile2 != null))
    {
      if (contains(paramFile1, paramFile3)) {
        return new File(paramFile2, paramFile3.getAbsolutePath().substring(paramFile1.getAbsolutePath().length()));
      }
      return null;
    }
    return null;
  }
  
  public static String rewriteAfterRename(File paramFile1, File paramFile2, String paramString)
  {
    Object localObject = null;
    if (paramString == null) {
      return null;
    }
    paramFile2 = rewriteAfterRename(paramFile1, paramFile2, new File(paramString));
    paramFile1 = localObject;
    if (paramFile2 != null) {
      paramFile1 = paramFile2.getAbsolutePath();
    }
    return paramFile1;
  }
  
  public static String[] rewriteAfterRename(File paramFile1, File paramFile2, String[] paramArrayOfString)
  {
    if (paramArrayOfString == null) {
      return null;
    }
    String[] arrayOfString = new String[paramArrayOfString.length];
    for (int i = 0; i < paramArrayOfString.length; i++) {
      arrayOfString[i] = rewriteAfterRename(paramFile1, paramFile2, paramArrayOfString[i]);
    }
    return arrayOfString;
  }
  
  public static long roundStorageSize(long paramLong)
  {
    long l1 = 1L;
    long l2 = 1L;
    while (l1 * l2 < paramLong)
    {
      long l3 = l1 << 1;
      l1 = l3;
      if (l3 > 512L)
      {
        l1 = 1L;
        l2 *= 1000L;
      }
    }
    return l1 * l2;
  }
  
  public static int setPermissions(File paramFile, int paramInt1, int paramInt2, int paramInt3)
  {
    return setPermissions(paramFile.getAbsolutePath(), paramInt1, paramInt2, paramInt3);
  }
  
  public static int setPermissions(FileDescriptor paramFileDescriptor, int paramInt1, int paramInt2, int paramInt3)
  {
    try
    {
      Os.fchmod(paramFileDescriptor, paramInt1);
      if ((paramInt2 >= 0) || (paramInt3 >= 0)) {}
      try
      {
        Os.fchown(paramFileDescriptor, paramInt2, paramInt3);
        return 0;
      }
      catch (ErrnoException localErrnoException)
      {
        paramFileDescriptor = new StringBuilder();
        paramFileDescriptor.append("Failed to fchown(): ");
        paramFileDescriptor.append(localErrnoException);
        Slog.w("FileUtils", paramFileDescriptor.toString());
        return errno;
      }
      StringBuilder localStringBuilder;
      return errno;
    }
    catch (ErrnoException paramFileDescriptor)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Failed to fchmod(): ");
      localStringBuilder.append(paramFileDescriptor);
      Slog.w("FileUtils", localStringBuilder.toString());
    }
  }
  
  /* Error */
  public static int setPermissions(String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    // Byte code:
    //   0: aload_0
    //   1: iload_1
    //   2: invokestatic 374	android/system/Os:chmod	(Ljava/lang/String;I)V
    //   5: iload_2
    //   6: ifge +7 -> 13
    //   9: iload_3
    //   10: iflt +9 -> 19
    //   13: aload_0
    //   14: iload_2
    //   15: iload_3
    //   16: invokestatic 384	android/system/Os:chown	(Ljava/lang/String;II)V
    //   19: iconst_0
    //   20: ireturn
    //   21: astore 4
    //   23: new 88	java/lang/StringBuilder
    //   26: dup
    //   27: invokespecial 89	java/lang/StringBuilder:<init>	()V
    //   30: astore 5
    //   32: aload 5
    //   34: ldc_w 563
    //   37: invokevirtual 93	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   40: pop
    //   41: aload 5
    //   43: aload_0
    //   44: invokevirtual 93	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   47: pop
    //   48: aload 5
    //   50: ldc_w 565
    //   53: invokevirtual 93	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   56: pop
    //   57: aload 5
    //   59: aload 4
    //   61: invokevirtual 414	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   64: pop
    //   65: ldc 52
    //   67: aload 5
    //   69: invokevirtual 99	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   72: invokestatic 556	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   75: pop
    //   76: aload 4
    //   78: getfield 559	android/system/ErrnoException:errno	I
    //   81: ireturn
    //   82: astore 5
    //   84: new 88	java/lang/StringBuilder
    //   87: dup
    //   88: invokespecial 89	java/lang/StringBuilder:<init>	()V
    //   91: astore 4
    //   93: aload 4
    //   95: ldc_w 567
    //   98: invokevirtual 93	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   101: pop
    //   102: aload 4
    //   104: aload_0
    //   105: invokevirtual 93	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   108: pop
    //   109: aload 4
    //   111: ldc_w 565
    //   114: invokevirtual 93	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   117: pop
    //   118: aload 4
    //   120: aload 5
    //   122: invokevirtual 414	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   125: pop
    //   126: ldc 52
    //   128: aload 4
    //   130: invokevirtual 99	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   133: invokestatic 556	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   136: pop
    //   137: aload 5
    //   139: getfield 559	android/system/ErrnoException:errno	I
    //   142: ireturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	143	0	paramString	String
    //   0	143	1	paramInt1	int
    //   0	143	2	paramInt2	int
    //   0	143	3	paramInt3	int
    //   21	56	4	localErrnoException1	ErrnoException
    //   91	38	4	localStringBuilder1	StringBuilder
    //   30	38	5	localStringBuilder2	StringBuilder
    //   82	56	5	localErrnoException2	ErrnoException
    // Exception table:
    //   from	to	target	type
    //   13	19	21	android/system/ErrnoException
    //   0	5	82	android/system/ErrnoException
  }
  
  public static String[] splitFileName(String paramString1, String paramString2)
  {
    Object localObject1;
    Object localObject2;
    if ("vnd.android.document/directory".equals(paramString1))
    {
      localObject1 = null;
      localObject2 = paramString2;
    }
    else
    {
      int i = paramString2.lastIndexOf('.');
      String str1;
      if (i >= 0)
      {
        str1 = paramString2.substring(0, i);
        localObject2 = paramString2.substring(i + 1);
      }
      Object localObject3;
      for (localObject1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(((String)localObject2).toLowerCase());; localObject1 = null)
      {
        localObject3 = localObject2;
        break;
        str1 = paramString2;
        localObject2 = null;
      }
      Object localObject4 = localObject1;
      if (localObject1 == null) {
        localObject4 = "application/octet-stream";
      }
      String str2 = MimeTypeMap.getSingleton().getExtensionFromMimeType(paramString1);
      localObject1 = localObject3;
      localObject2 = str1;
      if (!Objects.equals(paramString1, localObject4)) {
        if (Objects.equals(localObject3, str2))
        {
          localObject1 = localObject3;
          localObject2 = str1;
        }
        else
        {
          localObject1 = str2;
          localObject2 = paramString2;
        }
      }
    }
    paramString1 = (String)localObject1;
    if (localObject1 == null) {
      paramString1 = "";
    }
    return new String[] { localObject2, paramString1 };
  }
  
  public static void stringToFile(File paramFile, String paramString)
    throws IOException
  {
    stringToFile(paramFile.getAbsolutePath(), paramString);
  }
  
  public static void stringToFile(String paramString1, String paramString2)
    throws IOException
  {
    bytesToFile(paramString1, paramString2.getBytes(StandardCharsets.UTF_8));
  }
  
  public static boolean sync(FileOutputStream paramFileOutputStream)
  {
    if (paramFileOutputStream != null) {
      try
      {
        paramFileOutputStream.getFD().sync();
      }
      catch (IOException paramFileOutputStream)
      {
        return false;
      }
    }
    return true;
  }
  
  @VisibleForTesting
  public static String trimFilename(String paramString, int paramInt)
  {
    paramString = new StringBuilder(paramString);
    trimFilename(paramString, paramInt);
    return paramString.toString();
  }
  
  private static void trimFilename(StringBuilder paramStringBuilder, int paramInt)
  {
    byte[] arrayOfByte = paramStringBuilder.toString().getBytes(StandardCharsets.UTF_8);
    if (arrayOfByte.length > paramInt)
    {
      while (arrayOfByte.length > paramInt - 3)
      {
        paramStringBuilder.deleteCharAt(paramStringBuilder.length() / 2);
        arrayOfByte = paramStringBuilder.toString().getBytes(StandardCharsets.UTF_8);
      }
      paramStringBuilder.insert(paramStringBuilder.length() / 2, "...");
    }
  }
  
  @VisibleForTesting
  public static class MemoryPipe
    extends Thread
    implements AutoCloseable
  {
    private final byte[] data;
    private final FileDescriptor[] pipe;
    private final boolean sink;
    
    private MemoryPipe(byte[] paramArrayOfByte, boolean paramBoolean)
      throws IOException
    {
      try
      {
        pipe = Os.pipe();
        data = paramArrayOfByte;
        sink = paramBoolean;
        return;
      }
      catch (ErrnoException paramArrayOfByte)
      {
        throw paramArrayOfByte.rethrowAsIOException();
      }
    }
    
    public static MemoryPipe createSink(byte[] paramArrayOfByte)
      throws IOException
    {
      return new MemoryPipe(paramArrayOfByte, true).startInternal();
    }
    
    public static MemoryPipe createSource(byte[] paramArrayOfByte)
      throws IOException
    {
      return new MemoryPipe(paramArrayOfByte, false).startInternal();
    }
    
    private MemoryPipe startInternal()
    {
      super.start();
      return this;
    }
    
    public void close()
      throws Exception
    {
      IoUtils.closeQuietly(getFD());
    }
    
    public FileDescriptor getFD()
    {
      FileDescriptor localFileDescriptor;
      if (sink) {
        localFileDescriptor = pipe[1];
      } else {
        localFileDescriptor = pipe[0];
      }
      return localFileDescriptor;
    }
    
    public FileDescriptor getInternalFD()
    {
      FileDescriptor localFileDescriptor;
      if (sink) {
        localFileDescriptor = pipe[0];
      } else {
        localFileDescriptor = pipe[1];
      }
      return localFileDescriptor;
    }
    
    /* Error */
    public void run()
    {
      // Byte code:
      //   0: aload_0
      //   1: invokevirtual 71	android/os/FileUtils$MemoryPipe:getInternalFD	()Ljava/io/FileDescriptor;
      //   4: astore_1
      //   5: iconst_0
      //   6: istore_2
      //   7: iload_2
      //   8: aload_0
      //   9: getfield 34	android/os/FileUtils$MemoryPipe:data	[B
      //   12: arraylength
      //   13: if_icmpge +56 -> 69
      //   16: aload_0
      //   17: getfield 36	android/os/FileUtils$MemoryPipe:sink	Z
      //   20: ifeq +25 -> 45
      //   23: iload_2
      //   24: aload_1
      //   25: aload_0
      //   26: getfield 34	android/os/FileUtils$MemoryPipe:data	[B
      //   29: iload_2
      //   30: aload_0
      //   31: getfield 34	android/os/FileUtils$MemoryPipe:data	[B
      //   34: arraylength
      //   35: iload_2
      //   36: isub
      //   37: invokestatic 75	android/system/Os:read	(Ljava/io/FileDescriptor;[BII)I
      //   40: iadd
      //   41: istore_2
      //   42: goto -35 -> 7
      //   45: aload_1
      //   46: aload_0
      //   47: getfield 34	android/os/FileUtils$MemoryPipe:data	[B
      //   50: iload_2
      //   51: aload_0
      //   52: getfield 34	android/os/FileUtils$MemoryPipe:data	[B
      //   55: arraylength
      //   56: iload_2
      //   57: isub
      //   58: invokestatic 78	android/system/Os:write	(Ljava/io/FileDescriptor;[BII)I
      //   61: istore_3
      //   62: iload_2
      //   63: iload_3
      //   64: iadd
      //   65: istore_2
      //   66: goto -59 -> 7
      //   69: aload_0
      //   70: getfield 36	android/os/FileUtils$MemoryPipe:sink	Z
      //   73: ifeq +51 -> 124
      //   76: goto +38 -> 114
      //   79: astore 4
      //   81: aload_0
      //   82: getfield 36	android/os/FileUtils$MemoryPipe:sink	Z
      //   85: ifeq +13 -> 98
      //   88: getstatic 84	java/util/concurrent/TimeUnit:SECONDS	Ljava/util/concurrent/TimeUnit;
      //   91: lconst_1
      //   92: invokevirtual 88	java/util/concurrent/TimeUnit:toMillis	(J)J
      //   95: invokestatic 94	android/os/SystemClock:sleep	(J)V
      //   98: aload_1
      //   99: invokestatic 67	libcore/io/IoUtils:closeQuietly	(Ljava/io/FileDescriptor;)V
      //   102: aload 4
      //   104: athrow
      //   105: astore 4
      //   107: aload_0
      //   108: getfield 36	android/os/FileUtils$MemoryPipe:sink	Z
      //   111: ifeq +13 -> 124
      //   114: getstatic 84	java/util/concurrent/TimeUnit:SECONDS	Ljava/util/concurrent/TimeUnit;
      //   117: lconst_1
      //   118: invokevirtual 88	java/util/concurrent/TimeUnit:toMillis	(J)J
      //   121: invokestatic 94	android/os/SystemClock:sleep	(J)V
      //   124: aload_1
      //   125: invokestatic 67	libcore/io/IoUtils:closeQuietly	(Ljava/io/FileDescriptor;)V
      //   128: return
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	129	0	this	MemoryPipe
      //   4	121	1	localFileDescriptor	FileDescriptor
      //   6	60	2	i	int
      //   61	4	3	j	int
      //   79	24	4	localObject	Object
      //   105	1	4	localIOException	IOException
      // Exception table:
      //   from	to	target	type
      //   7	42	79	finally
      //   45	62	79	finally
      //   7	42	105	java/io/IOException
      //   7	42	105	android/system/ErrnoException
      //   45	62	105	java/io/IOException
      //   45	62	105	android/system/ErrnoException
    }
  }
  
  private static class NoImagePreloadHolder
  {
    public static final Pattern SAFE_FILENAME_PATTERN = Pattern.compile("[\\w%+,./=_-]+");
    
    private NoImagePreloadHolder() {}
  }
  
  public static abstract interface ProgressListener
  {
    public abstract void onProgress(long paramLong);
  }
}
