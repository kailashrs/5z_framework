package com.android.internal.util;

import android.os.FileUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import libcore.io.IoUtils;

public class FileRotator
{
  private static final boolean LOGD = false;
  private static final String SUFFIX_BACKUP = ".backup";
  private static final String SUFFIX_NO_BACKUP = ".no_backup";
  private static final String TAG = "FileRotator";
  private final File mBasePath;
  private final long mDeleteAgeMillis;
  private final String mPrefix;
  private final long mRotateAgeMillis;
  
  public FileRotator(File paramFile, String paramString, long paramLong1, long paramLong2)
  {
    mBasePath = ((File)Preconditions.checkNotNull(paramFile));
    mPrefix = ((String)Preconditions.checkNotNull(paramString));
    mRotateAgeMillis = paramLong1;
    mDeleteAgeMillis = paramLong2;
    mBasePath.mkdirs();
    for (Object localObject : mBasePath.list()) {
      if (((String)localObject).startsWith(mPrefix)) {
        if (((String)localObject).endsWith(".backup"))
        {
          new File(mBasePath, (String)localObject).renameTo(new File(mBasePath, ((String)localObject).substring(0, ((String)localObject).length() - ".backup".length())));
        }
        else if (((String)localObject).endsWith(".no_backup"))
        {
          paramString = new File(mBasePath, (String)localObject);
          localObject = new File(mBasePath, ((String)localObject).substring(0, ((String)localObject).length() - ".no_backup".length()));
          paramString.delete();
          ((File)localObject).delete();
        }
      }
    }
  }
  
  private String getActiveName(long paramLong)
  {
    Object localObject1 = null;
    long l1 = Long.MAX_VALUE;
    FileInfo localFileInfo = new FileInfo(mPrefix);
    String[] arrayOfString = mBasePath.list();
    int i = arrayOfString.length;
    int j = 0;
    while (j < i)
    {
      String str = arrayOfString[j];
      Object localObject2;
      long l2;
      if (!localFileInfo.parse(str))
      {
        localObject2 = localObject1;
        l2 = l1;
      }
      else
      {
        localObject2 = localObject1;
        l2 = l1;
        if (localFileInfo.isActive())
        {
          localObject2 = localObject1;
          l2 = l1;
          if (startMillis < paramLong)
          {
            localObject2 = localObject1;
            l2 = l1;
            if (startMillis < l1)
            {
              localObject2 = str;
              l2 = startMillis;
            }
          }
        }
      }
      j++;
      localObject1 = localObject2;
      l1 = l2;
    }
    if (localObject1 != null) {
      return localObject1;
    }
    startMillis = paramLong;
    endMillis = Long.MAX_VALUE;
    return localFileInfo.build();
  }
  
  private static void readFile(File paramFile, Reader paramReader)
    throws IOException
  {
    paramFile = new BufferedInputStream(new FileInputStream(paramFile));
    try
    {
      paramReader.read(paramFile);
      return;
    }
    finally
    {
      IoUtils.closeQuietly(paramFile);
    }
  }
  
  private static IOException rethrowAsIoException(Throwable paramThrowable)
    throws IOException
  {
    if ((paramThrowable instanceof IOException)) {
      throw ((IOException)paramThrowable);
    }
    throw new IOException(paramThrowable.getMessage(), paramThrowable);
  }
  
  private void rewriteSingle(Rewriter paramRewriter, String paramString)
    throws IOException
  {
    File localFile1 = new File(mBasePath, paramString);
    paramRewriter.reset();
    File localFile2;
    StringBuilder localStringBuilder;
    if (localFile1.exists())
    {
      readFile(localFile1, paramRewriter);
      if (!paramRewriter.shouldWrite()) {
        return;
      }
      localFile2 = mBasePath;
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append(".backup");
      paramString = new File(localFile2, localStringBuilder.toString());
      localFile1.renameTo(paramString);
      try
      {
        writeFile(localFile1, paramRewriter);
        paramString.delete();
      }
      catch (Throwable paramRewriter)
      {
        localFile1.delete();
        paramString.renameTo(localFile1);
        throw rethrowAsIoException(paramRewriter);
      }
    }
    else
    {
      localFile2 = mBasePath;
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append(".no_backup");
      paramString = new File(localFile2, localStringBuilder.toString());
      paramString.createNewFile();
    }
    try
    {
      writeFile(localFile1, paramRewriter);
      paramString.delete();
      return;
    }
    catch (Throwable paramRewriter)
    {
      localFile1.delete();
      paramString.delete();
      throw rethrowAsIoException(paramRewriter);
    }
  }
  
  private static void writeFile(File paramFile, Writer paramWriter)
    throws IOException
  {
    paramFile = new FileOutputStream(paramFile);
    BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(paramFile);
    try
    {
      paramWriter.write(localBufferedOutputStream);
      localBufferedOutputStream.flush();
      return;
    }
    finally
    {
      FileUtils.sync(paramFile);
      IoUtils.closeQuietly(localBufferedOutputStream);
    }
  }
  
  @Deprecated
  public void combineActive(final Reader paramReader, final Writer paramWriter, long paramLong)
    throws IOException
  {
    rewriteActive(new Rewriter()
    {
      public void read(InputStream paramAnonymousInputStream)
        throws IOException
      {
        paramReader.read(paramAnonymousInputStream);
      }
      
      public void reset() {}
      
      public boolean shouldWrite()
      {
        return true;
      }
      
      public void write(OutputStream paramAnonymousOutputStream)
        throws IOException
      {
        paramWriter.write(paramAnonymousOutputStream);
      }
    }, paramLong);
  }
  
  public void deleteAll()
  {
    FileInfo localFileInfo = new FileInfo(mPrefix);
    for (String str : mBasePath.list()) {
      if (localFileInfo.parse(str)) {
        new File(mBasePath, str).delete();
      }
    }
  }
  
  public void dumpAll(OutputStream paramOutputStream)
    throws IOException
  {
    paramOutputStream = new ZipOutputStream(paramOutputStream);
    try
    {
      FileInfo localFileInfo = new com/android/internal/util/FileRotator$FileInfo;
      localFileInfo.<init>(mPrefix);
      String[] arrayOfString = mBasePath.list();
      int i = arrayOfString.length;
      int j = 0;
      while (j < i)
      {
        Object localObject3 = arrayOfString[j];
        if (localFileInfo.parse((String)localObject3))
        {
          Object localObject4 = new java/util/zip/ZipEntry;
          ((ZipEntry)localObject4).<init>((String)localObject3);
          paramOutputStream.putNextEntry((ZipEntry)localObject4);
          localObject4 = new java/io/File;
          ((File)localObject4).<init>(mBasePath, (String)localObject3);
          localObject3 = new java/io/FileInputStream;
          ((FileInputStream)localObject3).<init>((File)localObject4);
        }
        try
        {
          FileUtils.copy((InputStream)localObject3, paramOutputStream);
          IoUtils.closeQuietly((AutoCloseable)localObject3);
          paramOutputStream.closeEntry();
        }
        finally
        {
          IoUtils.closeQuietly((AutoCloseable)localObject3);
        }
      }
      return;
    }
    finally
    {
      IoUtils.closeQuietly(paramOutputStream);
    }
  }
  
  public void maybeRotate(long paramLong)
  {
    long l1 = mRotateAgeMillis;
    long l2 = mDeleteAgeMillis;
    FileInfo localFileInfo = new FileInfo(mPrefix);
    String[] arrayOfString = mBasePath.list();
    if (arrayOfString == null) {
      return;
    }
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++)
    {
      String str = arrayOfString[j];
      if (localFileInfo.parse(str)) {
        if (localFileInfo.isActive())
        {
          if (startMillis <= paramLong - l1)
          {
            endMillis = paramLong;
            new File(mBasePath, str).renameTo(new File(mBasePath, localFileInfo.build()));
          }
        }
        else if (endMillis <= paramLong - l2) {
          new File(mBasePath, str).delete();
        }
      }
    }
  }
  
  public void readMatching(Reader paramReader, long paramLong1, long paramLong2)
    throws IOException
  {
    FileInfo localFileInfo = new FileInfo(mPrefix);
    for (String str : mBasePath.list()) {
      if ((localFileInfo.parse(str)) && (startMillis <= paramLong2) && (paramLong1 <= endMillis)) {
        readFile(new File(mBasePath, str), paramReader);
      }
    }
  }
  
  public void rewriteActive(Rewriter paramRewriter, long paramLong)
    throws IOException
  {
    rewriteSingle(paramRewriter, getActiveName(paramLong));
  }
  
  public void rewriteAll(Rewriter paramRewriter)
    throws IOException
  {
    FileInfo localFileInfo = new FileInfo(mPrefix);
    for (String str : mBasePath.list()) {
      if (localFileInfo.parse(str)) {
        rewriteSingle(paramRewriter, str);
      }
    }
  }
  
  private static class FileInfo
  {
    public long endMillis;
    public final String prefix;
    public long startMillis;
    
    public FileInfo(String paramString)
    {
      prefix = ((String)Preconditions.checkNotNull(paramString));
    }
    
    public String build()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(prefix);
      localStringBuilder.append('.');
      localStringBuilder.append(startMillis);
      localStringBuilder.append('-');
      if (endMillis != Long.MAX_VALUE) {
        localStringBuilder.append(endMillis);
      }
      return localStringBuilder.toString();
    }
    
    public boolean isActive()
    {
      boolean bool;
      if (endMillis == Long.MAX_VALUE) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean parse(String paramString)
    {
      endMillis = -1L;
      startMillis = -1L;
      int i = paramString.lastIndexOf('.');
      int j = paramString.lastIndexOf('-');
      if ((i != -1) && (j != -1))
      {
        if (!prefix.equals(paramString.substring(0, i))) {
          return false;
        }
        try
        {
          startMillis = Long.parseLong(paramString.substring(i + 1, j));
          if (paramString.length() - j == 1) {
            endMillis = Long.MAX_VALUE;
          } else {
            endMillis = Long.parseLong(paramString.substring(j + 1));
          }
          return true;
        }
        catch (NumberFormatException paramString)
        {
          return false;
        }
      }
      return false;
    }
  }
  
  public static abstract interface Reader
  {
    public abstract void read(InputStream paramInputStream)
      throws IOException;
  }
  
  public static abstract interface Rewriter
    extends FileRotator.Reader, FileRotator.Writer
  {
    public abstract void reset();
    
    public abstract boolean shouldWrite();
  }
  
  public static abstract interface Writer
  {
    public abstract void write(OutputStream paramOutputStream)
      throws IOException;
  }
}
