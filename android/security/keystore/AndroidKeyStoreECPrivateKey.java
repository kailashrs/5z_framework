package android.security.keystore;

import java.security.interfaces.ECKey;
import java.security.spec.ECParameterSpec;

public class AndroidKeyStoreECPrivateKey
  extends AndroidKeyStorePrivateKey
  implements ECKey
{
  private final ECParameterSpec mParams;
  
  public AndroidKeyStoreECPrivateKey(String paramString, int paramInt, ECParameterSpec paramECParameterSpec)
  {
    super(paramString, paramInt, "EC");
    mParams = paramECParameterSpec;
  }
  
  public ECParameterSpec getParams()
  {
    return mParams;
  }
}
