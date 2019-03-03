package android.util.jar;

import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import dalvik.system.CloseGuard;
import java.io.FileDescriptor;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import libcore.io.IoBridge;
import libcore.io.IoUtils;
import libcore.io.Streams;

public final class StrictJarFile
{
  private boolean closed;
  private final FileDescriptor fd;
  private final CloseGuard guard = CloseGuard.get();
  private final boolean isSigned;
  private final StrictJarManifest manifest;
  private final long nativeHandle;
  private final StrictJarVerifier verifier;
  
  public StrictJarFile(FileDescriptor paramFileDescriptor)
    throws IOException, SecurityException
  {
    this(paramFileDescriptor, true, true);
  }
  
  public StrictJarFile(FileDescriptor paramFileDescriptor, boolean paramBoolean1, boolean paramBoolean2)
    throws IOException, SecurityException
  {
    this(localStringBuilder.toString(), paramFileDescriptor, paramBoolean1, paramBoolean2);
  }
  
  public StrictJarFile(String paramString)
    throws IOException, SecurityException
  {
    this(paramString, true, true);
  }
  
  private StrictJarFile(String paramString, FileDescriptor paramFileDescriptor, boolean paramBoolean1, boolean paramBoolean2)
    throws IOException, SecurityException
  {
    nativeHandle = nativeOpenJarFile(paramString, paramFileDescriptor.getInt$());
    fd = paramFileDescriptor;
    boolean bool = false;
    if (paramBoolean1) {
      try
      {
        Object localObject1 = getMetaEntries();
        Object localObject2 = new android/util/jar/StrictJarManifest;
        ((StrictJarManifest)localObject2).<init>((byte[])((HashMap)localObject1).get("META-INF/MANIFEST.MF"), true);
        manifest = ((StrictJarManifest)localObject2);
        localObject2 = new android/util/jar/StrictJarVerifier;
        ((StrictJarVerifier)localObject2).<init>(paramString, manifest, (HashMap)localObject1, paramBoolean2);
        verifier = ((StrictJarVerifier)localObject2);
        localObject1 = manifest.getEntries().keySet().iterator();
        while (((Iterator)localObject1).hasNext())
        {
          paramString = (String)((Iterator)localObject1).next();
          if (findEntry(paramString) == null)
          {
            localObject2 = new java/lang/SecurityException;
            localObject1 = new java/lang/StringBuilder;
            ((StringBuilder)localObject1).<init>();
            ((StringBuilder)localObject1).append("File ");
            ((StringBuilder)localObject1).append(paramString);
            ((StringBuilder)localObject1).append(" in manifest does not exist");
            ((SecurityException)localObject2).<init>(((StringBuilder)localObject1).toString());
            throw ((Throwable)localObject2);
          }
        }
        paramBoolean1 = bool;
        if (verifier.readCertificates())
        {
          paramBoolean1 = bool;
          if (verifier.isSignedJar()) {
            paramBoolean1 = true;
          }
        }
        isSigned = paramBoolean1;
      }
      catch (IOException|SecurityException paramString)
      {
        break label260;
      }
    }
    isSigned = false;
    manifest = null;
    verifier = null;
    guard.open("close");
    return;
    label260:
    nativeClose(nativeHandle);
    IoUtils.closeQuietly(paramFileDescriptor);
    closed = true;
    throw paramString;
  }
  
  public StrictJarFile(String paramString, boolean paramBoolean1, boolean paramBoolean2)
    throws IOException, SecurityException
  {
    this(paramString, IoBridge.open(paramString, OsConstants.O_RDONLY), paramBoolean1, paramBoolean2);
  }
  
  private HashMap<String, byte[]> getMetaEntries()
    throws IOException
  {
    HashMap localHashMap = new HashMap();
    EntryIterator localEntryIterator = new EntryIterator(nativeHandle, "META-INF/");
    while (localEntryIterator.hasNext())
    {
      ZipEntry localZipEntry = (ZipEntry)localEntryIterator.next();
      localHashMap.put(localZipEntry.getName(), Streams.readFully(getInputStream(localZipEntry)));
    }
    return localHashMap;
  }
  
  private InputStream getZipInputStream(ZipEntry paramZipEntry)
  {
    if (paramZipEntry.getMethod() == 0) {
      return new FDStream(fd, paramZipEntry.getDataOffset(), paramZipEntry.getDataOffset() + paramZipEntry.getSize());
    }
    FDStream localFDStream = new FDStream(fd, paramZipEntry.getDataOffset(), paramZipEntry.getDataOffset() + paramZipEntry.getCompressedSize());
    int i = Math.max(1024, (int)Math.min(paramZipEntry.getSize(), 65535L));
    return new ZipInflaterInputStream(localFDStream, new Inflater(true), i, paramZipEntry);
  }
  
  private static native void nativeClose(long paramLong);
  
  private static native ZipEntry nativeFindEntry(long paramLong, String paramString);
  
  private static native ZipEntry nativeNextEntry(long paramLong);
  
  private static native long nativeOpenJarFile(String paramString, int paramInt)
    throws IOException;
  
  private static native long nativeStartIteration(long paramLong, String paramString);
  
  public void close()
    throws IOException
  {
    if (!closed)
    {
      if (guard != null) {
        guard.close();
      }
      nativeClose(nativeHandle);
      IoUtils.closeQuietly(fd);
      closed = true;
    }
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (guard != null) {
        guard.warnIfOpen();
      }
      close();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public ZipEntry findEntry(String paramString)
  {
    return nativeFindEntry(nativeHandle, paramString);
  }
  
  public Certificate[][] getCertificateChains(ZipEntry paramZipEntry)
  {
    if (isSigned) {
      return verifier.getCertificateChains(paramZipEntry.getName());
    }
    return null;
  }
  
  @Deprecated
  public Certificate[] getCertificates(ZipEntry paramZipEntry)
  {
    if (isSigned)
    {
      paramZipEntry = verifier.getCertificateChains(paramZipEntry.getName());
      int i = paramZipEntry.length;
      int j = 0;
      for (int k = 0; k < i; k++) {
        j += paramZipEntry[k].length;
      }
      Certificate[] arrayOfCertificate = new Certificate[j];
      i = paramZipEntry.length;
      k = 0;
      for (j = 0; j < i; j++)
      {
        Object localObject = paramZipEntry[j];
        System.arraycopy(localObject, 0, arrayOfCertificate, k, localObject.length);
        k += localObject.length;
      }
      return arrayOfCertificate;
    }
    return null;
  }
  
  public InputStream getInputStream(ZipEntry paramZipEntry)
  {
    InputStream localInputStream = getZipInputStream(paramZipEntry);
    if (isSigned)
    {
      StrictJarVerifier.VerifierEntry localVerifierEntry = verifier.initEntry(paramZipEntry.getName());
      if (localVerifierEntry == null) {
        return localInputStream;
      }
      return new JarFileInputStream(localInputStream, paramZipEntry.getSize(), localVerifierEntry);
    }
    return localInputStream;
  }
  
  public StrictJarManifest getManifest()
  {
    return manifest;
  }
  
  public Iterator<ZipEntry> iterator()
    throws IOException
  {
    return new EntryIterator(nativeHandle, "");
  }
  
  static final class EntryIterator
    implements Iterator<ZipEntry>
  {
    private final long iterationHandle;
    private ZipEntry nextEntry;
    
    EntryIterator(long paramLong, String paramString)
      throws IOException
    {
      iterationHandle = StrictJarFile.nativeStartIteration(paramLong, paramString);
    }
    
    public boolean hasNext()
    {
      if (nextEntry != null) {
        return true;
      }
      ZipEntry localZipEntry = StrictJarFile.nativeNextEntry(iterationHandle);
      if (localZipEntry == null) {
        return false;
      }
      nextEntry = localZipEntry;
      return true;
    }
    
    public ZipEntry next()
    {
      if (nextEntry != null)
      {
        ZipEntry localZipEntry = nextEntry;
        nextEntry = null;
        return localZipEntry;
      }
      return StrictJarFile.nativeNextEntry(iterationHandle);
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
  
  public static class FDStream
    extends InputStream
  {
    private long endOffset;
    private final FileDescriptor fd;
    private long offset;
    
    public FDStream(FileDescriptor paramFileDescriptor, long paramLong1, long paramLong2)
    {
      fd = paramFileDescriptor;
      offset = paramLong1;
      endOffset = paramLong2;
    }
    
    public int available()
      throws IOException
    {
      int i;
      if (offset < endOffset) {
        i = 1;
      } else {
        i = 0;
      }
      return i;
    }
    
    public int read()
      throws IOException
    {
      return Streams.readSingleByte(this);
    }
    
    public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      synchronized (fd)
      {
        long l1 = endOffset;
        long l2 = offset;
        l1 -= l2;
        int i = paramInt2;
        if (paramInt2 > l1) {
          i = (int)l1;
        }
        try
        {
          Os.lseek(fd, offset, OsConstants.SEEK_SET);
          paramInt1 = IoBridge.read(fd, paramArrayOfByte, paramInt1, i);
          if (paramInt1 > 0)
          {
            offset += paramInt1;
            return paramInt1;
          }
          return -1;
        }
        catch (ErrnoException localErrnoException)
        {
          paramArrayOfByte = new java/io/IOException;
          paramArrayOfByte.<init>(localErrnoException);
          throw paramArrayOfByte;
        }
      }
    }
    
    public long skip(long paramLong)
      throws IOException
    {
      long l = paramLong;
      if (paramLong > endOffset - offset) {
        l = endOffset - offset;
      }
      offset += l;
      return l;
    }
  }
  
  static final class JarFileInputStream
    extends FilterInputStream
  {
    private long count;
    private boolean done = false;
    private final StrictJarVerifier.VerifierEntry entry;
    
    JarFileInputStream(InputStream paramInputStream, long paramLong, StrictJarVerifier.VerifierEntry paramVerifierEntry)
    {
      super();
      entry = paramVerifierEntry;
      count = paramLong;
    }
    
    public int available()
      throws IOException
    {
      if (done) {
        return 0;
      }
      return super.available();
    }
    
    public int read()
      throws IOException
    {
      if (done) {
        return -1;
      }
      if (count > 0L)
      {
        int i = super.read();
        if (i != -1)
        {
          entry.write(i);
          count -= 1L;
        }
        else
        {
          count = 0L;
        }
        if (count == 0L)
        {
          done = true;
          entry.verify();
        }
        return i;
      }
      done = true;
      entry.verify();
      return -1;
    }
    
    public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      if (done) {
        return -1;
      }
      if (count > 0L)
      {
        int i = super.read(paramArrayOfByte, paramInt1, paramInt2);
        if (i != -1)
        {
          paramInt2 = i;
          int j = paramInt2;
          if (count < paramInt2) {
            j = (int)count;
          }
          entry.write(paramArrayOfByte, paramInt1, j);
          count -= j;
        }
        else
        {
          count = 0L;
        }
        if (count == 0L)
        {
          done = true;
          entry.verify();
        }
        return i;
      }
      done = true;
      entry.verify();
      return -1;
    }
    
    public long skip(long paramLong)
      throws IOException
    {
      return Streams.skipByReading(this, paramLong);
    }
  }
  
  public static class ZipInflaterInputStream
    extends InflaterInputStream
  {
    private long bytesRead = 0L;
    private final ZipEntry entry;
    
    public ZipInflaterInputStream(InputStream paramInputStream, Inflater paramInflater, int paramInt, ZipEntry paramZipEntry)
    {
      super(paramInflater, paramInt);
      entry = paramZipEntry;
    }
    
    public int available()
      throws IOException
    {
      boolean bool = closed;
      int i = 0;
      if (bool) {
        return 0;
      }
      if (super.available() != 0) {
        i = (int)(entry.getSize() - bytesRead);
      }
      return i;
    }
    
    public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      try
      {
        paramInt1 = super.read(paramArrayOfByte, paramInt1, paramInt2);
        if (paramInt1 == -1)
        {
          if (entry.getSize() != bytesRead)
          {
            paramArrayOfByte = new StringBuilder();
            paramArrayOfByte.append("Size mismatch on inflated file: ");
            paramArrayOfByte.append(bytesRead);
            paramArrayOfByte.append(" vs ");
            paramArrayOfByte.append(entry.getSize());
            throw new IOException(paramArrayOfByte.toString());
          }
        }
        else {
          bytesRead += paramInt1;
        }
        return paramInt1;
      }
      catch (IOException paramArrayOfByte)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Error reading data for ");
        localStringBuilder.append(entry.getName());
        localStringBuilder.append(" near offset ");
        localStringBuilder.append(bytesRead);
        throw new IOException(localStringBuilder.toString(), paramArrayOfByte);
      }
    }
  }
}
