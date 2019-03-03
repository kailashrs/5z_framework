package android.bluetooth;

import java.io.IOException;
import java.io.InputStream;

final class BluetoothInputStream
  extends InputStream
{
  private BluetoothSocket mSocket;
  
  BluetoothInputStream(BluetoothSocket paramBluetoothSocket)
  {
    mSocket = paramBluetoothSocket;
  }
  
  public int available()
    throws IOException
  {
    return mSocket.available();
  }
  
  public void close()
    throws IOException
  {
    mSocket.close();
  }
  
  public int read()
    throws IOException
  {
    byte[] arrayOfByte = new byte[1];
    if (mSocket.read(arrayOfByte, 0, 1) == 1) {
      return arrayOfByte[0] & 0xFF;
    }
    return -1;
  }
  
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramArrayOfByte != null)
    {
      if (((paramInt1 | paramInt2) >= 0) && (paramInt2 <= paramArrayOfByte.length - paramInt1)) {
        return mSocket.read(paramArrayOfByte, paramInt1, paramInt2);
      }
      throw new ArrayIndexOutOfBoundsException("invalid offset or length");
    }
    throw new NullPointerException("byte array is null");
  }
}
