package android.security.keystore;

import android.os.SystemProperties;
import android.security.keymaster.ExportResult;
import android.security.keymaster.KeyCharacteristics;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.ProviderException;
import java.security.Security;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.interfaces.ECKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.Mac;

public class AndroidKeyStoreProvider
  extends Provider
{
  private static final String DESEDE_SYSTEM_PROPERTY = "ro.hardware.keystore_desede";
  private static final String PACKAGE_NAME = "android.security.keystore";
  public static final String PROVIDER_NAME = "AndroidKeyStore";
  
  public AndroidKeyStoreProvider()
  {
    super("AndroidKeyStore", 1.0D, "Android KeyStore security provider");
    boolean bool = "true".equals(SystemProperties.get("ro.hardware.keystore_desede"));
    put("KeyStore.AndroidKeyStore", "android.security.keystore.AndroidKeyStoreSpi");
    put("KeyPairGenerator.EC", "android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi$EC");
    put("KeyPairGenerator.RSA", "android.security.keystore.AndroidKeyStoreKeyPairGeneratorSpi$RSA");
    putKeyFactoryImpl("EC");
    putKeyFactoryImpl("RSA");
    put("KeyGenerator.AES", "android.security.keystore.AndroidKeyStoreKeyGeneratorSpi$AES");
    put("KeyGenerator.HmacSHA1", "android.security.keystore.AndroidKeyStoreKeyGeneratorSpi$HmacSHA1");
    put("KeyGenerator.HmacSHA224", "android.security.keystore.AndroidKeyStoreKeyGeneratorSpi$HmacSHA224");
    put("KeyGenerator.HmacSHA256", "android.security.keystore.AndroidKeyStoreKeyGeneratorSpi$HmacSHA256");
    put("KeyGenerator.HmacSHA384", "android.security.keystore.AndroidKeyStoreKeyGeneratorSpi$HmacSHA384");
    put("KeyGenerator.HmacSHA512", "android.security.keystore.AndroidKeyStoreKeyGeneratorSpi$HmacSHA512");
    if (bool) {
      put("KeyGenerator.DESede", "android.security.keystore.AndroidKeyStoreKeyGeneratorSpi$DESede");
    }
    putSecretKeyFactoryImpl("AES");
    if (bool) {
      putSecretKeyFactoryImpl("DESede");
    }
    putSecretKeyFactoryImpl("HmacSHA1");
    putSecretKeyFactoryImpl("HmacSHA224");
    putSecretKeyFactoryImpl("HmacSHA256");
    putSecretKeyFactoryImpl("HmacSHA384");
    putSecretKeyFactoryImpl("HmacSHA512");
  }
  
  private static AndroidKeyStorePrivateKey getAndroidKeyStorePrivateKey(AndroidKeyStorePublicKey paramAndroidKeyStorePublicKey)
  {
    String str = paramAndroidKeyStorePublicKey.getAlgorithm();
    if ("EC".equalsIgnoreCase(str)) {
      return new AndroidKeyStoreECPrivateKey(paramAndroidKeyStorePublicKey.getAlias(), paramAndroidKeyStorePublicKey.getUid(), ((ECKey)paramAndroidKeyStorePublicKey).getParams());
    }
    if ("RSA".equalsIgnoreCase(str)) {
      return new AndroidKeyStoreRSAPrivateKey(paramAndroidKeyStorePublicKey.getAlias(), paramAndroidKeyStorePublicKey.getUid(), ((RSAKey)paramAndroidKeyStorePublicKey).getModulus());
    }
    paramAndroidKeyStorePublicKey = new StringBuilder();
    paramAndroidKeyStorePublicKey.append("Unsupported Android Keystore public key algorithm: ");
    paramAndroidKeyStorePublicKey.append(str);
    throw new ProviderException(paramAndroidKeyStorePublicKey.toString());
  }
  
  public static AndroidKeyStorePublicKey getAndroidKeyStorePublicKey(String paramString1, int paramInt, String paramString2, byte[] paramArrayOfByte)
  {
    try
    {
      KeyFactory localKeyFactory = KeyFactory.getInstance(paramString2);
      X509EncodedKeySpec localX509EncodedKeySpec = new java/security/spec/X509EncodedKeySpec;
      localX509EncodedKeySpec.<init>(paramArrayOfByte);
      paramArrayOfByte = localKeyFactory.generatePublic(localX509EncodedKeySpec);
      if ("EC".equalsIgnoreCase(paramString2)) {
        return new AndroidKeyStoreECPublicKey(paramString1, paramInt, (ECPublicKey)paramArrayOfByte);
      }
      if ("RSA".equalsIgnoreCase(paramString2)) {
        return new AndroidKeyStoreRSAPublicKey(paramString1, paramInt, (RSAPublicKey)paramArrayOfByte);
      }
      paramString1 = new StringBuilder();
      paramString1.append("Unsupported Android Keystore public key algorithm: ");
      paramString1.append(paramString2);
      throw new ProviderException(paramString1.toString());
    }
    catch (InvalidKeySpecException paramString1)
    {
      throw new ProviderException("Invalid X.509 encoding of public key", paramString1);
    }
    catch (NoSuchAlgorithmException paramArrayOfByte)
    {
      paramString1 = new StringBuilder();
      paramString1.append("Failed to obtain ");
      paramString1.append(paramString2);
      paramString1.append(" KeyFactory");
      throw new ProviderException(paramString1.toString(), paramArrayOfByte);
    }
  }
  
  private static KeyCharacteristics getKeyCharacteristics(android.security.KeyStore paramKeyStore, String paramString, int paramInt)
    throws UnrecoverableKeyException
  {
    KeyCharacteristics localKeyCharacteristics = new KeyCharacteristics();
    paramInt = paramKeyStore.getKeyCharacteristics(paramString, null, null, paramInt, localKeyCharacteristics);
    if (paramInt == 1) {
      return localKeyCharacteristics;
    }
    throw ((UnrecoverableKeyException)new UnrecoverableKeyException("Failed to obtain information about key").initCause(android.security.KeyStore.getKeyStoreException(paramInt)));
  }
  
  public static java.security.KeyStore getKeyStoreForUid(int paramInt)
    throws KeyStoreException, NoSuchProviderException
  {
    Object localObject = java.security.KeyStore.getInstance("AndroidKeyStore", "AndroidKeyStore");
    try
    {
      AndroidKeyStoreLoadStoreParameter localAndroidKeyStoreLoadStoreParameter = new android/security/keystore/AndroidKeyStoreLoadStoreParameter;
      localAndroidKeyStoreLoadStoreParameter.<init>(paramInt);
      ((java.security.KeyStore)localObject).load(localAndroidKeyStoreLoadStoreParameter);
      return localObject;
    }
    catch (NoSuchAlgorithmException|CertificateException|IOException localNoSuchAlgorithmException)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Failed to load AndroidKeyStore KeyStore for UID ");
      ((StringBuilder)localObject).append(paramInt);
      throw new KeyStoreException(((StringBuilder)localObject).toString(), localNoSuchAlgorithmException);
    }
  }
  
  public static long getKeyStoreOperationHandle(Object paramObject)
  {
    if (paramObject != null)
    {
      if ((paramObject instanceof Signature)) {
        localObject = ((Signature)paramObject).getCurrentSpi();
      }
      for (;;)
      {
        break;
        if ((paramObject instanceof Mac))
        {
          localObject = ((Mac)paramObject).getCurrentSpi();
        }
        else
        {
          if (!(paramObject instanceof Cipher)) {
            break label138;
          }
          localObject = ((Cipher)paramObject).getCurrentSpi();
        }
      }
      if (localObject != null)
      {
        if ((localObject instanceof KeyStoreCryptoOperation)) {
          return ((KeyStoreCryptoOperation)localObject).getOperationHandle();
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Crypto primitive not backed by AndroidKeyStore provider: ");
        localStringBuilder.append(paramObject);
        localStringBuilder.append(", spi: ");
        localStringBuilder.append(localObject);
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      throw new IllegalStateException("Crypto primitive not initialized");
      label138:
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Unsupported crypto primitive: ");
      ((StringBuilder)localObject).append(paramObject);
      ((StringBuilder)localObject).append(". Supported: Signature, Mac, Cipher");
      throw new IllegalArgumentException(((StringBuilder)localObject).toString());
    }
    throw new NullPointerException();
  }
  
  public static void install()
  {
    Object localObject = Security.getProviders();
    int i = -1;
    int k;
    for (int j = 0;; j++)
    {
      k = i;
      if (j >= localObject.length) {
        break;
      }
      if ("BC".equals(localObject[j].getName()))
      {
        k = j;
        break;
      }
    }
    Security.addProvider(new AndroidKeyStoreProvider());
    localObject = new AndroidKeyStoreBCWorkaroundProvider();
    if (k != -1) {
      Security.insertProviderAt((Provider)localObject, k + 1);
    } else {
      Security.addProvider((Provider)localObject);
    }
  }
  
  public static AndroidKeyStoreKey loadAndroidKeyStoreKeyFromKeystore(android.security.KeyStore paramKeyStore, String paramString, int paramInt)
    throws UnrecoverableKeyException
  {
    KeyCharacteristics localKeyCharacteristics = getKeyCharacteristics(paramKeyStore, paramString, paramInt);
    Integer localInteger = localKeyCharacteristics.getEnum(268435458);
    if (localInteger != null)
    {
      if ((localInteger.intValue() != 128) && (localInteger.intValue() != 32) && (localInteger.intValue() != 33))
      {
        if ((localInteger.intValue() != 1) && (localInteger.intValue() != 3)) {
          throw new UnrecoverableKeyException("Key algorithm unknown");
        }
        return loadAndroidKeyStorePrivateKeyFromKeystore(paramKeyStore, paramString, paramInt, localKeyCharacteristics);
      }
      return loadAndroidKeyStoreSecretKeyFromKeystore(paramString, paramInt, localKeyCharacteristics);
    }
    throw new UnrecoverableKeyException("Key algorithm unknown");
  }
  
  public static KeyPair loadAndroidKeyStoreKeyPairFromKeystore(android.security.KeyStore paramKeyStore, String paramString, int paramInt)
    throws UnrecoverableKeyException
  {
    return loadAndroidKeyStoreKeyPairFromKeystore(paramKeyStore, paramString, paramInt, getKeyCharacteristics(paramKeyStore, paramString, paramInt));
  }
  
  private static KeyPair loadAndroidKeyStoreKeyPairFromKeystore(android.security.KeyStore paramKeyStore, String paramString, int paramInt, KeyCharacteristics paramKeyCharacteristics)
    throws UnrecoverableKeyException
  {
    paramKeyStore = loadAndroidKeyStorePublicKeyFromKeystore(paramKeyStore, paramString, paramInt, paramKeyCharacteristics);
    return new KeyPair(paramKeyStore, getAndroidKeyStorePrivateKey(paramKeyStore));
  }
  
  public static AndroidKeyStorePrivateKey loadAndroidKeyStorePrivateKeyFromKeystore(android.security.KeyStore paramKeyStore, String paramString, int paramInt)
    throws UnrecoverableKeyException
  {
    return loadAndroidKeyStorePrivateKeyFromKeystore(paramKeyStore, paramString, paramInt, getKeyCharacteristics(paramKeyStore, paramString, paramInt));
  }
  
  private static AndroidKeyStorePrivateKey loadAndroidKeyStorePrivateKeyFromKeystore(android.security.KeyStore paramKeyStore, String paramString, int paramInt, KeyCharacteristics paramKeyCharacteristics)
    throws UnrecoverableKeyException
  {
    return (AndroidKeyStorePrivateKey)loadAndroidKeyStoreKeyPairFromKeystore(paramKeyStore, paramString, paramInt, paramKeyCharacteristics).getPrivate();
  }
  
  public static AndroidKeyStorePublicKey loadAndroidKeyStorePublicKeyFromKeystore(android.security.KeyStore paramKeyStore, String paramString, int paramInt)
    throws UnrecoverableKeyException
  {
    return loadAndroidKeyStorePublicKeyFromKeystore(paramKeyStore, paramString, paramInt, getKeyCharacteristics(paramKeyStore, paramString, paramInt));
  }
  
  private static AndroidKeyStorePublicKey loadAndroidKeyStorePublicKeyFromKeystore(android.security.KeyStore paramKeyStore, String paramString, int paramInt, KeyCharacteristics paramKeyCharacteristics)
    throws UnrecoverableKeyException
  {
    paramKeyStore = paramKeyStore.exportKey(paramString, 0, null, null, paramInt);
    if (resultCode == 1)
    {
      paramKeyStore = exportData;
      paramKeyCharacteristics = paramKeyCharacteristics.getEnum(268435458);
      if (paramKeyCharacteristics != null) {
        try
        {
          paramKeyCharacteristics = KeyProperties.KeyAlgorithm.fromKeymasterAsymmetricKeyAlgorithm(paramKeyCharacteristics.intValue());
          return getAndroidKeyStorePublicKey(paramString, paramInt, paramKeyCharacteristics, paramKeyStore);
        }
        catch (IllegalArgumentException paramKeyStore)
        {
          throw ((UnrecoverableKeyException)new UnrecoverableKeyException("Failed to load private key").initCause(paramKeyStore));
        }
      }
      throw new UnrecoverableKeyException("Key algorithm unknown");
    }
    throw ((UnrecoverableKeyException)new UnrecoverableKeyException("Failed to obtain X.509 form of public key").initCause(android.security.KeyStore.getKeyStoreException(resultCode)));
  }
  
  private static AndroidKeyStoreSecretKey loadAndroidKeyStoreSecretKeyFromKeystore(String paramString, int paramInt, KeyCharacteristics paramKeyCharacteristics)
    throws UnrecoverableKeyException
  {
    Integer localInteger = paramKeyCharacteristics.getEnum(268435458);
    if (localInteger != null)
    {
      paramKeyCharacteristics = paramKeyCharacteristics.getEnums(536870917);
      int i;
      if (paramKeyCharacteristics.isEmpty()) {
        i = -1;
      } else {
        i = ((Integer)paramKeyCharacteristics.get(0)).intValue();
      }
      try
      {
        paramKeyCharacteristics = KeyProperties.KeyAlgorithm.fromKeymasterSecretKeyAlgorithm(localInteger.intValue(), i);
        return new AndroidKeyStoreSecretKey(paramString, paramInt, paramKeyCharacteristics);
      }
      catch (IllegalArgumentException paramString)
      {
        throw ((UnrecoverableKeyException)new UnrecoverableKeyException("Unsupported secret key type").initCause(paramString));
      }
    }
    throw new UnrecoverableKeyException("Key algorithm unknown");
  }
  
  private void putKeyFactoryImpl(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("KeyFactory.");
    localStringBuilder.append(paramString);
    put(localStringBuilder.toString(), "android.security.keystore.AndroidKeyStoreKeyFactorySpi");
  }
  
  private void putSecretKeyFactoryImpl(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("SecretKeyFactory.");
    localStringBuilder.append(paramString);
    put(localStringBuilder.toString(), "android.security.keystore.AndroidKeyStoreSecretKeyFactorySpi");
  }
}
