package android.security.keystore;

import java.math.BigInteger;
import java.security.interfaces.RSAPublicKey;

public class AndroidKeyStoreRSAPublicKey
  extends AndroidKeyStorePublicKey
  implements RSAPublicKey
{
  private final BigInteger mModulus;
  private final BigInteger mPublicExponent;
  
  public AndroidKeyStoreRSAPublicKey(String paramString, int paramInt, RSAPublicKey paramRSAPublicKey)
  {
    this(paramString, paramInt, paramRSAPublicKey.getEncoded(), paramRSAPublicKey.getModulus(), paramRSAPublicKey.getPublicExponent());
    if ("X.509".equalsIgnoreCase(paramRSAPublicKey.getFormat())) {
      return;
    }
    paramString = new StringBuilder();
    paramString.append("Unsupported key export format: ");
    paramString.append(paramRSAPublicKey.getFormat());
    throw new IllegalArgumentException(paramString.toString());
  }
  
  public AndroidKeyStoreRSAPublicKey(String paramString, int paramInt, byte[] paramArrayOfByte, BigInteger paramBigInteger1, BigInteger paramBigInteger2)
  {
    super(paramString, paramInt, "RSA", paramArrayOfByte);
    mModulus = paramBigInteger1;
    mPublicExponent = paramBigInteger2;
  }
  
  public BigInteger getModulus()
  {
    return mModulus;
  }
  
  public BigInteger getPublicExponent()
  {
    return mPublicExponent;
  }
}
