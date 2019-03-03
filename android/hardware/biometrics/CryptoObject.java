package android.hardware.biometrics;

import android.security.keystore.AndroidKeyStoreProvider;
import java.security.Signature;
import javax.crypto.Cipher;
import javax.crypto.Mac;

public class CryptoObject
{
  private final Object mCrypto;
  
  public CryptoObject(Signature paramSignature)
  {
    mCrypto = paramSignature;
  }
  
  public CryptoObject(Cipher paramCipher)
  {
    mCrypto = paramCipher;
  }
  
  public CryptoObject(Mac paramMac)
  {
    mCrypto = paramMac;
  }
  
  public Cipher getCipher()
  {
    Cipher localCipher;
    if ((mCrypto instanceof Cipher)) {
      localCipher = (Cipher)mCrypto;
    } else {
      localCipher = null;
    }
    return localCipher;
  }
  
  public Mac getMac()
  {
    Mac localMac;
    if ((mCrypto instanceof Mac)) {
      localMac = (Mac)mCrypto;
    } else {
      localMac = null;
    }
    return localMac;
  }
  
  public final long getOpId()
  {
    long l;
    if (mCrypto != null) {
      l = AndroidKeyStoreProvider.getKeyStoreOperationHandle(mCrypto);
    } else {
      l = 0L;
    }
    return l;
  }
  
  public Signature getSignature()
  {
    Signature localSignature;
    if ((mCrypto instanceof Signature)) {
      localSignature = (Signature)mCrypto;
    } else {
      localSignature = null;
    }
    return localSignature;
  }
}
