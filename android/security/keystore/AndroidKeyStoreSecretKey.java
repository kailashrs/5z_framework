package android.security.keystore;

import javax.crypto.SecretKey;

public class AndroidKeyStoreSecretKey
  extends AndroidKeyStoreKey
  implements SecretKey
{
  public AndroidKeyStoreSecretKey(String paramString1, int paramInt, String paramString2)
  {
    super(paramString1, paramInt, paramString2);
  }
}
