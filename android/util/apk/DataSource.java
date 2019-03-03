package android.util.apk;

import java.io.IOException;
import java.security.DigestException;

abstract interface DataSource
{
  public abstract void feedIntoDataDigester(DataDigester paramDataDigester, long paramLong, int paramInt)
    throws IOException, DigestException;
  
  public abstract long size();
}
