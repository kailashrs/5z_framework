package android.util.apk;

import java.nio.ByteBuffer;
import java.security.DigestException;

abstract interface DataDigester
{
  public abstract void consume(ByteBuffer paramByteBuffer)
    throws DigestException;
}
