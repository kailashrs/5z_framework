package android.security.keystore;

import java.math.BigInteger;
import java.security.interfaces.RSAKey;

public class AndroidKeyStoreRSAPrivateKey
  extends AndroidKeyStorePrivateKey
  implements RSAKey
{
  private final BigInteger mModulus;
  
  public AndroidKeyStoreRSAPrivateKey(String paramString, int paramInt, BigInteger paramBigInteger)
  {
    super(paramString, paramInt, "RSA");
    mModulus = paramBigInteger;
  }
  
  public BigInteger getModulus()
  {
    return mModulus;
  }
}
