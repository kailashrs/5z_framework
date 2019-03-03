package android.security.keystore;

import java.security.KeyStore.LoadStoreParameter;
import java.security.KeyStore.ProtectionParameter;

class AndroidKeyStoreLoadStoreParameter
  implements KeyStore.LoadStoreParameter
{
  private final int mUid;
  
  AndroidKeyStoreLoadStoreParameter(int paramInt)
  {
    mUid = paramInt;
  }
  
  public KeyStore.ProtectionParameter getProtectionParameter()
  {
    return null;
  }
  
  int getUid()
  {
    return mUid;
  }
}
