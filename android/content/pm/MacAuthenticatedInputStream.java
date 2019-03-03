package android.content.pm;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.crypto.Mac;

public class MacAuthenticatedInputStream
  extends FilterInputStream
{
  private final Mac mMac;
  
  public MacAuthenticatedInputStream(InputStream paramInputStream, Mac paramMac)
  {
    super(paramInputStream);
    mMac = paramMac;
  }
  
  public boolean isTagEqual(byte[] paramArrayOfByte)
  {
    byte[] arrayOfByte = mMac.doFinal();
    boolean bool = false;
    if ((paramArrayOfByte != null) && (arrayOfByte != null) && (paramArrayOfByte.length == arrayOfByte.length))
    {
      int i = 0;
      for (int j = 0; j < paramArrayOfByte.length; j++) {
        i |= paramArrayOfByte[j] ^ arrayOfByte[j];
      }
      if (i == 0) {
        bool = true;
      }
      return bool;
    }
    return false;
  }
  
  public int read()
    throws IOException
  {
    int i = super.read();
    if (i >= 0) {
      mMac.update((byte)i);
    }
    return i;
  }
  
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    paramInt2 = super.read(paramArrayOfByte, paramInt1, paramInt2);
    if (paramInt2 > 0) {
      mMac.update(paramArrayOfByte, paramInt1, paramInt2);
    }
    return paramInt2;
  }
}
