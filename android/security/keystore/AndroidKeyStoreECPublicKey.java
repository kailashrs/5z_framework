package android.security.keystore;

import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;

public class AndroidKeyStoreECPublicKey
  extends AndroidKeyStorePublicKey
  implements ECPublicKey
{
  private final ECParameterSpec mParams;
  private final ECPoint mW;
  
  public AndroidKeyStoreECPublicKey(String paramString, int paramInt, ECPublicKey paramECPublicKey)
  {
    this(paramString, paramInt, paramECPublicKey.getEncoded(), paramECPublicKey.getParams(), paramECPublicKey.getW());
    if ("X.509".equalsIgnoreCase(paramECPublicKey.getFormat())) {
      return;
    }
    paramString = new StringBuilder();
    paramString.append("Unsupported key export format: ");
    paramString.append(paramECPublicKey.getFormat());
    throw new IllegalArgumentException(paramString.toString());
  }
  
  public AndroidKeyStoreECPublicKey(String paramString, int paramInt, byte[] paramArrayOfByte, ECParameterSpec paramECParameterSpec, ECPoint paramECPoint)
  {
    super(paramString, paramInt, "EC", paramArrayOfByte);
    mParams = paramECParameterSpec;
    mW = paramECPoint;
  }
  
  public ECParameterSpec getParams()
  {
    return mParams;
  }
  
  public ECPoint getW()
  {
    return mW;
  }
}
