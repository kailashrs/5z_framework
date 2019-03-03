package android.media;

import java.io.Closeable;
import java.io.IOException;

public abstract class Media2DataSource
  implements Closeable
{
  public Media2DataSource() {}
  
  public abstract long getSize()
    throws IOException;
  
  public abstract int readAt(long paramLong, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException;
}
