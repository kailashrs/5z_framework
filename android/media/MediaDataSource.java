package android.media;

import java.io.Closeable;
import java.io.IOException;

public abstract class MediaDataSource
  implements Closeable
{
  public MediaDataSource() {}
  
  public abstract long getSize()
    throws IOException;
  
  public abstract int readAt(long paramLong, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException;
}
