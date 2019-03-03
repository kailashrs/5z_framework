package android.bluetooth;

import java.io.IOException;
import java.io.OutputStream;

final class BluetoothOutputStream
  extends OutputStream
{
  private BluetoothSocket mSocket;
  
  BluetoothOutputStream(BluetoothSocket paramBluetoothSocket)
  {
    mSocket = paramBluetoothSocket;
  }
  
  public void close()
    throws IOException
  {
    mSocket.close();
  }
  
  public void flush()
    throws IOException
  {
    mSocket.flush();
  }
  
  public void write(int paramInt)
    throws IOException
  {
    int i = (byte)paramInt;
    mSocket.write(new byte[] { i }, 0, 1);
  }
  
  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramArrayOfByte != null)
    {
      if (((paramInt1 | paramInt2) >= 0) && (paramInt2 <= paramArrayOfByte.length - paramInt1))
      {
        mSocket.write(paramArrayOfByte, paramInt1, paramInt2);
        return;
      }
      throw new IndexOutOfBoundsException("invalid offset or length");
    }
    throw new NullPointerException("buffer is null");
  }
}
