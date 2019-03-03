package android.app.backup;

import java.io.IOException;
import java.io.InputStream;

public class BackupDataInputStream
  extends InputStream
{
  int dataSize;
  String key;
  BackupDataInput mData;
  byte[] mOneByte;
  
  BackupDataInputStream(BackupDataInput paramBackupDataInput)
  {
    mData = paramBackupDataInput;
  }
  
  public String getKey()
  {
    return key;
  }
  
  public int read()
    throws IOException
  {
    byte[] arrayOfByte = mOneByte;
    if (mOneByte == null)
    {
      arrayOfByte = new byte[1];
      mOneByte = arrayOfByte;
    }
    mData.readEntityData(arrayOfByte, 0, 1);
    return arrayOfByte[0];
  }
  
  public int read(byte[] paramArrayOfByte)
    throws IOException
  {
    return mData.readEntityData(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    return mData.readEntityData(paramArrayOfByte, paramInt1, paramInt2);
  }
  
  public int size()
  {
    return dataSize;
  }
}
