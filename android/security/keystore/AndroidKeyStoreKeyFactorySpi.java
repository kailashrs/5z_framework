package android.security.keystore;

import android.security.KeyStore;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactorySpi;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class AndroidKeyStoreKeyFactorySpi
  extends KeyFactorySpi
{
  private final KeyStore mKeyStore = KeyStore.getInstance();
  
  public AndroidKeyStoreKeyFactorySpi() {}
  
  protected PrivateKey engineGeneratePrivate(KeySpec paramKeySpec)
    throws InvalidKeySpecException
  {
    paramKeySpec = new StringBuilder();
    paramKeySpec.append("To generate a key pair in Android Keystore, use KeyPairGenerator initialized with ");
    paramKeySpec.append(KeyGenParameterSpec.class.getName());
    throw new InvalidKeySpecException(paramKeySpec.toString());
  }
  
  protected PublicKey engineGeneratePublic(KeySpec paramKeySpec)
    throws InvalidKeySpecException
  {
    paramKeySpec = new StringBuilder();
    paramKeySpec.append("To generate a key pair in Android Keystore, use KeyPairGenerator initialized with ");
    paramKeySpec.append(KeyGenParameterSpec.class.getName());
    throw new InvalidKeySpecException(paramKeySpec.toString());
  }
  
  protected <T extends KeySpec> T engineGetKeySpec(Key paramKey, Class<T> paramClass)
    throws InvalidKeySpecException
  {
    if (paramKey != null)
    {
      if ((!(paramKey instanceof AndroidKeyStorePrivateKey)) && (!(paramKey instanceof AndroidKeyStorePublicKey)))
      {
        paramClass = new StringBuilder();
        paramClass.append("Unsupported key type: ");
        paramClass.append(paramKey.getClass().getName());
        paramClass.append(". This KeyFactory supports only Android Keystore asymmetric keys");
        throw new InvalidKeySpecException(paramClass.toString());
      }
      if (paramClass != null)
      {
        if (KeyInfo.class.equals(paramClass))
        {
          if ((paramKey instanceof AndroidKeyStorePrivateKey))
          {
            paramClass = (AndroidKeyStorePrivateKey)paramKey;
            paramKey = paramClass.getAlias();
            if (paramKey.startsWith("USRPKEY_"))
            {
              String str = paramKey.substring("USRPKEY_".length());
              return AndroidKeyStoreSecretKeyFactorySpi.getKeyInfo(mKeyStore, str, paramKey, paramClass.getUid());
            }
            paramClass = new StringBuilder();
            paramClass.append("Invalid key alias: ");
            paramClass.append(paramKey);
            throw new InvalidKeySpecException(paramClass.toString());
          }
          paramClass = new StringBuilder();
          paramClass.append("Unsupported key type: ");
          paramClass.append(paramKey.getClass().getName());
          paramClass.append(". KeyInfo can be obtained only for Android Keystore private keys");
          throw new InvalidKeySpecException(paramClass.toString());
        }
        if (X509EncodedKeySpec.class.equals(paramClass))
        {
          if ((paramKey instanceof AndroidKeyStorePublicKey)) {
            return new X509EncodedKeySpec(((AndroidKeyStorePublicKey)paramKey).getEncoded());
          }
          paramClass = new StringBuilder();
          paramClass.append("Unsupported key type: ");
          paramClass.append(paramKey.getClass().getName());
          paramClass.append(". X509EncodedKeySpec can be obtained only for Android Keystore public keys");
          throw new InvalidKeySpecException(paramClass.toString());
        }
        if (PKCS8EncodedKeySpec.class.equals(paramClass))
        {
          if ((paramKey instanceof AndroidKeyStorePrivateKey)) {
            throw new InvalidKeySpecException("Key material export of Android Keystore private keys is not supported");
          }
          throw new InvalidKeySpecException("Cannot export key material of public key in PKCS#8 format. Only X.509 format (X509EncodedKeySpec) supported for public keys.");
        }
        if (RSAPublicKeySpec.class.equals(paramClass))
        {
          if ((paramKey instanceof AndroidKeyStoreRSAPublicKey))
          {
            paramKey = (AndroidKeyStoreRSAPublicKey)paramKey;
            return new RSAPublicKeySpec(paramKey.getModulus(), paramKey.getPublicExponent());
          }
          paramClass = new StringBuilder();
          paramClass.append("Obtaining RSAPublicKeySpec not supported for ");
          paramClass.append(paramKey.getAlgorithm());
          paramClass.append(" ");
          if ((paramKey instanceof AndroidKeyStorePrivateKey)) {
            paramKey = "private";
          } else {
            paramKey = "public";
          }
          paramClass.append(paramKey);
          paramClass.append(" key");
          throw new InvalidKeySpecException(paramClass.toString());
        }
        if (ECPublicKeySpec.class.equals(paramClass))
        {
          if ((paramKey instanceof AndroidKeyStoreECPublicKey))
          {
            paramKey = (AndroidKeyStoreECPublicKey)paramKey;
            return new ECPublicKeySpec(paramKey.getW(), paramKey.getParams());
          }
          paramClass = new StringBuilder();
          paramClass.append("Obtaining ECPublicKeySpec not supported for ");
          paramClass.append(paramKey.getAlgorithm());
          paramClass.append(" ");
          if ((paramKey instanceof AndroidKeyStorePrivateKey)) {
            paramKey = "private";
          } else {
            paramKey = "public";
          }
          paramClass.append(paramKey);
          paramClass.append(" key");
          throw new InvalidKeySpecException(paramClass.toString());
        }
        paramKey = new StringBuilder();
        paramKey.append("Unsupported key spec: ");
        paramKey.append(paramClass.getName());
        throw new InvalidKeySpecException(paramKey.toString());
      }
      throw new InvalidKeySpecException("keySpecClass == null");
    }
    throw new InvalidKeySpecException("key == null");
  }
  
  protected Key engineTranslateKey(Key paramKey)
    throws InvalidKeyException
  {
    if (paramKey != null)
    {
      if ((!(paramKey instanceof AndroidKeyStorePrivateKey)) && (!(paramKey instanceof AndroidKeyStorePublicKey))) {
        throw new InvalidKeyException("To import a key into Android Keystore, use KeyStore.setEntry");
      }
      return paramKey;
    }
    throw new InvalidKeyException("key == null");
  }
}
