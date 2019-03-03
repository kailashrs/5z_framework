package android.app.backup;

import android.annotation.SystemApi;
import java.io.FileDescriptor;
import java.io.IOException;

public class BackupDataInput
{
  long mBackupReader;
  private EntityHeader mHeader = new EntityHeader(null);
  private boolean mHeaderReady;
  
  @SystemApi
  public BackupDataInput(FileDescriptor paramFileDescriptor)
  {
    if (paramFileDescriptor != null)
    {
      mBackupReader = ctor(paramFileDescriptor);
      if (mBackupReader != 0L) {
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Native initialization failed with fd=");
      localStringBuilder.append(paramFileDescriptor);
      throw new RuntimeException(localStringBuilder.toString());
    }
    throw new NullPointerException();
  }
  
  private static native long ctor(FileDescriptor paramFileDescriptor);
  
  private static native void dtor(long paramLong);
  
  private native int readEntityData_native(long paramLong, byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  private native int readNextHeader_native(long paramLong, EntityHeader paramEntityHeader);
  
  private native int skipEntityData_native(long paramLong);
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      dtor(mBackupReader);
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public int getDataSize()
  {
    if (mHeaderReady) {
      return mHeader.dataSize;
    }
    throw new IllegalStateException("Entity header not read");
  }
  
  public String getKey()
  {
    if (mHeaderReady) {
      return mHeader.key;
    }
    throw new IllegalStateException("Entity header not read");
  }
  
  public int readEntityData(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (mHeaderReady)
    {
      paramInt1 = readEntityData_native(mBackupReader, paramArrayOfByte, paramInt1, paramInt2);
      if (paramInt1 >= 0) {
        return paramInt1;
      }
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("result=0x");
      paramArrayOfByte.append(Integer.toHexString(paramInt1));
      throw new IOException(paramArrayOfByte.toString());
    }
    throw new IllegalStateException("Entity header not read");
  }
  
  public boolean readNextHeader()
    throws IOException
  {
    int i = readNextHeader_native(mBackupReader, mHeader);
    if (i == 0)
    {
      mHeaderReady = true;
      return true;
    }
    if (i > 0)
    {
      mHeaderReady = false;
      return false;
    }
    mHeaderReady = false;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("failed: 0x");
    localStringBuilder.append(Integer.toHexString(i));
    throw new IOException(localStringBuilder.toString());
  }
  
  public void skipEntityData()
    throws IOException
  {
    if (mHeaderReady)
    {
      skipEntityData_native(mBackupReader);
      return;
    }
    throw new IllegalStateException("Entity header not read");
  }
  
  private static class EntityHeader
  {
    int dataSize;
    String key;
    
    private EntityHeader() {}
  }
}
