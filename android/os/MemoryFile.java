package android.os;

import android.system.ErrnoException;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class MemoryFile
{
  private static String TAG = "MemoryFile";
  private boolean mAllowPurging = false;
  private ByteBuffer mMapping;
  private SharedMemory mSharedMemory;
  
  public MemoryFile(String paramString, int paramInt)
    throws IOException
  {
    try
    {
      mSharedMemory = SharedMemory.create(paramString, paramInt);
      mMapping = mSharedMemory.mapReadWrite();
    }
    catch (ErrnoException paramString)
    {
      paramString.rethrowAsIOException();
    }
  }
  
  private void beginAccess()
    throws IOException
  {
    checkActive();
    if ((mAllowPurging) && (native_pin(mSharedMemory.getFileDescriptor(), true))) {
      throw new IOException("MemoryFile has been purged");
    }
  }
  
  private void checkActive()
    throws IOException
  {
    if (mMapping != null) {
      return;
    }
    throw new IOException("MemoryFile has been deactivated");
  }
  
  private void endAccess()
    throws IOException
  {
    if (mAllowPurging) {
      native_pin(mSharedMemory.getFileDescriptor(), false);
    }
  }
  
  public static int getSize(FileDescriptor paramFileDescriptor)
    throws IOException
  {
    return native_get_size(paramFileDescriptor);
  }
  
  private static native int native_get_size(FileDescriptor paramFileDescriptor)
    throws IOException;
  
  private static native boolean native_pin(FileDescriptor paramFileDescriptor, boolean paramBoolean)
    throws IOException;
  
  @Deprecated
  public boolean allowPurging(boolean paramBoolean)
    throws IOException
  {
    try
    {
      boolean bool = mAllowPurging;
      if (bool != paramBoolean)
      {
        native_pin(mSharedMemory.getFileDescriptor(), paramBoolean ^ true);
        mAllowPurging = paramBoolean;
      }
      return bool;
    }
    finally {}
  }
  
  public void close()
  {
    deactivate();
    mSharedMemory.close();
  }
  
  void deactivate()
  {
    if (mMapping != null)
    {
      SharedMemory.unmap(mMapping);
      mMapping = null;
    }
  }
  
  public FileDescriptor getFileDescriptor()
    throws IOException
  {
    return mSharedMemory.getFileDescriptor();
  }
  
  public InputStream getInputStream()
  {
    return new MemoryInputStream(null);
  }
  
  public OutputStream getOutputStream()
  {
    return new MemoryOutputStream(null);
  }
  
  @Deprecated
  public boolean isPurgingAllowed()
  {
    return mAllowPurging;
  }
  
  public int length()
  {
    return mSharedMemory.getSize();
  }
  
  public int readBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
    throws IOException
  {
    beginAccess();
    try
    {
      mMapping.position(paramInt1);
      mMapping.get(paramArrayOfByte, paramInt2, paramInt3);
      return paramInt3;
    }
    finally
    {
      endAccess();
    }
  }
  
  public void writeBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
    throws IOException
  {
    beginAccess();
    try
    {
      mMapping.position(paramInt2);
      mMapping.put(paramArrayOfByte, paramInt1, paramInt3);
      return;
    }
    finally
    {
      endAccess();
    }
  }
  
  private class MemoryInputStream
    extends InputStream
  {
    private int mMark = 0;
    private int mOffset = 0;
    private byte[] mSingleByte;
    
    private MemoryInputStream() {}
    
    public int available()
      throws IOException
    {
      if (mOffset >= mSharedMemory.getSize()) {
        return 0;
      }
      return mSharedMemory.getSize() - mOffset;
    }
    
    public void mark(int paramInt)
    {
      mMark = mOffset;
    }
    
    public boolean markSupported()
    {
      return true;
    }
    
    public int read()
      throws IOException
    {
      if (mSingleByte == null) {
        mSingleByte = new byte[1];
      }
      if (read(mSingleByte, 0, 1) != 1) {
        return -1;
      }
      return mSingleByte[0];
    }
    
    public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      if ((paramInt1 >= 0) && (paramInt2 >= 0) && (paramInt1 + paramInt2 <= paramArrayOfByte.length))
      {
        paramInt2 = Math.min(paramInt2, available());
        if (paramInt2 < 1) {
          return -1;
        }
        paramInt1 = readBytes(paramArrayOfByte, mOffset, paramInt1, paramInt2);
        if (paramInt1 > 0) {
          mOffset += paramInt1;
        }
        return paramInt1;
      }
      throw new IndexOutOfBoundsException();
    }
    
    public void reset()
      throws IOException
    {
      mOffset = mMark;
    }
    
    public long skip(long paramLong)
      throws IOException
    {
      long l = paramLong;
      if (mOffset + paramLong > mSharedMemory.getSize()) {
        l = mSharedMemory.getSize() - mOffset;
      }
      mOffset = ((int)(mOffset + l));
      return l;
    }
  }
  
  private class MemoryOutputStream
    extends OutputStream
  {
    private int mOffset = 0;
    private byte[] mSingleByte;
    
    private MemoryOutputStream() {}
    
    public void write(int paramInt)
      throws IOException
    {
      if (mSingleByte == null) {
        mSingleByte = new byte[1];
      }
      mSingleByte[0] = ((byte)(byte)paramInt);
      write(mSingleByte, 0, 1);
    }
    
    public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      writeBytes(paramArrayOfByte, paramInt1, mOffset, paramInt2);
      mOffset += paramInt2;
    }
  }
}
