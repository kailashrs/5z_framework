package android.util.apk;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.DigestException;

class ByteBufferDataSource
  implements DataSource
{
  private final ByteBuffer mBuf;
  
  ByteBufferDataSource(ByteBuffer paramByteBuffer)
  {
    mBuf = paramByteBuffer.slice();
  }
  
  public void feedIntoDataDigester(DataDigester paramDataDigester, long paramLong, int paramInt)
    throws IOException, DigestException
  {
    synchronized (mBuf)
    {
      mBuf.position(0);
      mBuf.limit((int)paramLong + paramInt);
      mBuf.position((int)paramLong);
      ByteBuffer localByteBuffer2 = mBuf.slice();
      paramDataDigester.consume(localByteBuffer2);
      return;
    }
  }
  
  public long size()
  {
    return mBuf.capacity();
  }
}
