package android.security.keystore;

import android.security.KeyStore;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import libcore.util.EmptyArray;

abstract class KeyStoreCryptoOperationUtils
{
  private static volatile SecureRandom sRng;
  
  private KeyStoreCryptoOperationUtils() {}
  
  public static GeneralSecurityException getExceptionForCipherInit(KeyStore paramKeyStore, AndroidKeyStoreKey paramAndroidKeyStoreKey, int paramInt)
  {
    if (paramInt == 1) {
      return null;
    }
    if (paramInt != -55)
    {
      if (paramInt != -52) {
        return getInvalidKeyExceptionForInit(paramKeyStore, paramAndroidKeyStoreKey, paramInt);
      }
      return new InvalidAlgorithmParameterException("Invalid IV");
    }
    return new InvalidAlgorithmParameterException("Caller-provided IV not permitted");
  }
  
  static InvalidKeyException getInvalidKeyExceptionForInit(KeyStore paramKeyStore, AndroidKeyStoreKey paramAndroidKeyStoreKey, int paramInt)
  {
    if (paramInt == 1) {
      return null;
    }
    paramKeyStore = paramKeyStore.getInvalidKeyException(paramAndroidKeyStoreKey.getAlias(), paramAndroidKeyStoreKey.getUid(), paramInt);
    if ((paramInt == 15) && ((paramKeyStore instanceof UserNotAuthenticatedException))) {
      return null;
    }
    return paramKeyStore;
  }
  
  static byte[] getRandomBytesToMixIntoKeystoreRng(SecureRandom paramSecureRandom, int paramInt)
  {
    if (paramInt <= 0) {
      return EmptyArray.BYTE;
    }
    SecureRandom localSecureRandom = paramSecureRandom;
    if (paramSecureRandom == null) {
      localSecureRandom = getRng();
    }
    paramSecureRandom = new byte[paramInt];
    localSecureRandom.nextBytes(paramSecureRandom);
    return paramSecureRandom;
  }
  
  private static SecureRandom getRng()
  {
    if (sRng == null) {
      sRng = new SecureRandom();
    }
    return sRng;
  }
}
