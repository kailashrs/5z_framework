package android.security.keystore;

import android.security.Credentials;
import android.security.KeyStore;
import android.security.keymaster.KeyCharacteristics;
import android.security.keymaster.KeymasterArguments;
import java.security.InvalidAlgorithmParameterException;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;
import libcore.util.EmptyArray;

public abstract class AndroidKeyStoreKeyGeneratorSpi
  extends KeyGeneratorSpi
{
  private final int mDefaultKeySizeBits;
  protected int mKeySizeBits;
  private final KeyStore mKeyStore = KeyStore.getInstance();
  private final int mKeymasterAlgorithm;
  private int[] mKeymasterBlockModes;
  private final int mKeymasterDigest;
  private int[] mKeymasterDigests;
  private int[] mKeymasterPaddings;
  private int[] mKeymasterPurposes;
  private SecureRandom mRng;
  private KeyGenParameterSpec mSpec;
  
  protected AndroidKeyStoreKeyGeneratorSpi(int paramInt1, int paramInt2)
  {
    this(paramInt1, -1, paramInt2);
  }
  
  protected AndroidKeyStoreKeyGeneratorSpi(int paramInt1, int paramInt2, int paramInt3)
  {
    mKeymasterAlgorithm = paramInt1;
    mKeymasterDigest = paramInt2;
    mDefaultKeySizeBits = paramInt3;
    if (mDefaultKeySizeBits > 0)
    {
      if ((mKeymasterAlgorithm == 128) && (mKeymasterDigest == -1)) {
        throw new IllegalArgumentException("Digest algorithm must be specified for HMAC key");
      }
      return;
    }
    throw new IllegalArgumentException("Default key size must be positive");
  }
  
  private void resetAll()
  {
    mSpec = null;
    mRng = null;
    mKeySizeBits = -1;
    mKeymasterPurposes = null;
    mKeymasterPaddings = null;
    mKeymasterBlockModes = null;
  }
  
  protected SecretKey engineGenerateKey()
  {
    KeyGenParameterSpec localKeyGenParameterSpec = mSpec;
    if (localKeyGenParameterSpec != null)
    {
      KeymasterArguments localKeymasterArguments = new KeymasterArguments();
      localKeymasterArguments.addUnsignedInt(805306371, mKeySizeBits);
      localKeymasterArguments.addEnum(268435458, mKeymasterAlgorithm);
      localKeymasterArguments.addEnums(536870913, mKeymasterPurposes);
      localKeymasterArguments.addEnums(536870916, mKeymasterBlockModes);
      localKeymasterArguments.addEnums(536870918, mKeymasterPaddings);
      localKeymasterArguments.addEnums(536870917, mKeymasterDigests);
      KeymasterUtils.addUserAuthArgs(localKeymasterArguments, localKeyGenParameterSpec);
      KeymasterUtils.addMinMacLengthAuthorizationIfNecessary(localKeymasterArguments, mKeymasterAlgorithm, mKeymasterBlockModes, mKeymasterDigests);
      localKeymasterArguments.addDateIfNotNull(1610613136, localKeyGenParameterSpec.getKeyValidityStart());
      localKeymasterArguments.addDateIfNotNull(1610613137, localKeyGenParameterSpec.getKeyValidityForOriginationEnd());
      localKeymasterArguments.addDateIfNotNull(1610613138, localKeyGenParameterSpec.getKeyValidityForConsumptionEnd());
      if (((localKeyGenParameterSpec.getPurposes() & 0x1) != 0) && (!localKeyGenParameterSpec.isRandomizedEncryptionRequired())) {
        localKeymasterArguments.addBoolean(1879048199);
      }
      Object localObject1 = KeyStoreCryptoOperationUtils.getRandomBytesToMixIntoKeystoreRng(mRng, (mKeySizeBits + 7) / 8);
      int i = 0;
      if (localKeyGenParameterSpec.isStrongBoxBacked()) {
        i = 0x0 | 0x10;
      }
      Object localObject3 = new StringBuilder();
      ((StringBuilder)localObject3).append("USRPKEY_");
      ((StringBuilder)localObject3).append(localKeyGenParameterSpec.getKeystoreAlias());
      localObject3 = ((StringBuilder)localObject3).toString();
      KeyCharacteristics localKeyCharacteristics = new KeyCharacteristics();
      try
      {
        Credentials.deleteAllTypesForAlias(mKeyStore, localKeyGenParameterSpec.getKeystoreAlias(), localKeyGenParameterSpec.getUid());
        i = mKeyStore.generateKey((String)localObject3, localKeymasterArguments, (byte[])localObject1, localKeyGenParameterSpec.getUid(), i, localKeyCharacteristics);
        if (i != 1)
        {
          if (i == -68)
          {
            localObject1 = new android/security/keystore/StrongBoxUnavailableException;
            ((StrongBoxUnavailableException)localObject1).<init>("Failed to generate key");
            throw ((Throwable)localObject1);
          }
          localObject1 = new java/security/ProviderException;
          ((ProviderException)localObject1).<init>("Keystore operation failed", KeyStore.getKeyStoreException(i));
          throw ((Throwable)localObject1);
        }
        try
        {
          localObject1 = KeyProperties.KeyAlgorithm.fromKeymasterSecretKeyAlgorithm(mKeymasterAlgorithm, mKeymasterDigest);
          localObject1 = new AndroidKeyStoreSecretKey((String)localObject3, localKeyGenParameterSpec.getUid(), (String)localObject1);
          if (1 == 0) {
            Credentials.deleteAllTypesForAlias(mKeyStore, localKeyGenParameterSpec.getKeystoreAlias(), localKeyGenParameterSpec.getUid());
          }
          return localObject1;
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          localObject1 = new java/security/ProviderException;
          ((ProviderException)localObject1).<init>("Failed to obtain JCA secret key algorithm name", localIllegalArgumentException);
          throw ((Throwable)localObject1);
        }
        throw new IllegalStateException("Not initialized");
      }
      finally
      {
        if (0 == 0) {
          Credentials.deleteAllTypesForAlias(mKeyStore, localKeyGenParameterSpec.getKeystoreAlias(), localKeyGenParameterSpec.getUid());
        }
      }
    }
  }
  
  protected void engineInit(int paramInt, SecureRandom paramSecureRandom)
  {
    paramSecureRandom = new StringBuilder();
    paramSecureRandom.append("Cannot initialize without a ");
    paramSecureRandom.append(KeyGenParameterSpec.class.getName());
    paramSecureRandom.append(" parameter");
    throw new UnsupportedOperationException(paramSecureRandom.toString());
  }
  
  protected void engineInit(SecureRandom paramSecureRandom)
  {
    paramSecureRandom = new StringBuilder();
    paramSecureRandom.append("Cannot initialize without a ");
    paramSecureRandom.append(KeyGenParameterSpec.class.getName());
    paramSecureRandom.append(" parameter");
    throw new UnsupportedOperationException(paramSecureRandom.toString());
  }
  
  protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
    throws InvalidAlgorithmParameterException
  {
    resetAll();
    if (paramAlgorithmParameterSpec != null) {
      try
      {
        if ((paramAlgorithmParameterSpec instanceof KeyGenParameterSpec))
        {
          paramAlgorithmParameterSpec = (KeyGenParameterSpec)paramAlgorithmParameterSpec;
          if (paramAlgorithmParameterSpec.getKeystoreAlias() != null)
          {
            mRng = paramSecureRandom;
            mSpec = paramAlgorithmParameterSpec;
            int i;
            if (paramAlgorithmParameterSpec.getKeySize() != -1) {
              i = paramAlgorithmParameterSpec.getKeySize();
            } else {
              i = mDefaultKeySizeBits;
            }
            mKeySizeBits = i;
            if (mKeySizeBits > 0)
            {
              i = mKeySizeBits;
              if (i % 8 == 0) {
                try
                {
                  mKeymasterPurposes = KeyProperties.Purpose.allToKeymaster(paramAlgorithmParameterSpec.getPurposes());
                  mKeymasterPaddings = KeyProperties.EncryptionPadding.allToKeymaster(paramAlgorithmParameterSpec.getEncryptionPaddings());
                  if (paramAlgorithmParameterSpec.getSignaturePaddings().length <= 0)
                  {
                    mKeymasterBlockModes = KeyProperties.BlockMode.allToKeymaster(paramAlgorithmParameterSpec.getBlockModes());
                    if (((paramAlgorithmParameterSpec.getPurposes() & 0x1) != 0) && (paramAlgorithmParameterSpec.isRandomizedEncryptionRequired()))
                    {
                      paramSecureRandom = mKeymasterBlockModes;
                      int j = paramSecureRandom.length;
                      i = 0;
                      while (i < j)
                      {
                        int k = paramSecureRandom[i];
                        if (KeymasterUtils.isKeymasterBlockModeIndCpaCompatibleWithSymmetricCrypto(k))
                        {
                          i++;
                        }
                        else
                        {
                          paramSecureRandom = new java/security/InvalidAlgorithmParameterException;
                          paramAlgorithmParameterSpec = new java/lang/StringBuilder;
                          paramAlgorithmParameterSpec.<init>();
                          paramAlgorithmParameterSpec.append("Randomized encryption (IND-CPA) required but may be violated by block mode: ");
                          paramAlgorithmParameterSpec.append(KeyProperties.BlockMode.fromKeymaster(k));
                          paramAlgorithmParameterSpec.append(". See ");
                          paramAlgorithmParameterSpec.append(KeyGenParameterSpec.class.getName());
                          paramAlgorithmParameterSpec.append(" documentation.");
                          paramSecureRandom.<init>(paramAlgorithmParameterSpec.toString());
                          throw paramSecureRandom;
                        }
                      }
                    }
                    if (mKeymasterAlgorithm == 128)
                    {
                      if (mKeySizeBits >= 64)
                      {
                        mKeymasterDigests = new int[] { mKeymasterDigest };
                        if (paramAlgorithmParameterSpec.isDigestsSpecified())
                        {
                          paramSecureRandom = KeyProperties.Digest.allToKeymaster(paramAlgorithmParameterSpec.getDigests());
                          if ((paramSecureRandom.length != 1) || (paramSecureRandom[0] != mKeymasterDigest))
                          {
                            InvalidAlgorithmParameterException localInvalidAlgorithmParameterException = new java/security/InvalidAlgorithmParameterException;
                            paramSecureRandom = new java/lang/StringBuilder;
                            paramSecureRandom.<init>();
                            paramSecureRandom.append("Unsupported digests specification: ");
                            paramSecureRandom.append(Arrays.asList(paramAlgorithmParameterSpec.getDigests()));
                            paramSecureRandom.append(". Only ");
                            paramSecureRandom.append(KeyProperties.Digest.fromKeymaster(mKeymasterDigest));
                            paramSecureRandom.append(" supported for this HMAC key algorithm");
                            localInvalidAlgorithmParameterException.<init>(paramSecureRandom.toString());
                            throw localInvalidAlgorithmParameterException;
                          }
                        }
                      }
                      else
                      {
                        paramAlgorithmParameterSpec = new java/security/InvalidAlgorithmParameterException;
                        paramAlgorithmParameterSpec.<init>("HMAC key size must be at least 64 bits.");
                        throw paramAlgorithmParameterSpec;
                      }
                    }
                    else if (paramAlgorithmParameterSpec.isDigestsSpecified()) {
                      mKeymasterDigests = KeyProperties.Digest.allToKeymaster(paramAlgorithmParameterSpec.getDigests());
                    } else {
                      mKeymasterDigests = EmptyArray.INT;
                    }
                    paramSecureRandom = new android/security/keymaster/KeymasterArguments;
                    paramSecureRandom.<init>();
                    KeymasterUtils.addUserAuthArgs(paramSecureRandom, paramAlgorithmParameterSpec);
                    if (1 == 0) {
                      resetAll();
                    }
                    return;
                  }
                  paramAlgorithmParameterSpec = new java/security/InvalidAlgorithmParameterException;
                  paramAlgorithmParameterSpec.<init>("Signature paddings not supported for symmetric key algorithms");
                  throw paramAlgorithmParameterSpec;
                }
                catch (IllegalStateException|IllegalArgumentException paramAlgorithmParameterSpec)
                {
                  paramSecureRandom = new java/security/InvalidAlgorithmParameterException;
                  paramSecureRandom.<init>(paramAlgorithmParameterSpec);
                  throw paramSecureRandom;
                }
              }
              paramSecureRandom = new java/security/InvalidAlgorithmParameterException;
              paramAlgorithmParameterSpec = new java/lang/StringBuilder;
              paramAlgorithmParameterSpec.<init>();
              paramAlgorithmParameterSpec.append("Key size must be a multiple of 8: ");
              paramAlgorithmParameterSpec.append(mKeySizeBits);
              paramSecureRandom.<init>(paramAlgorithmParameterSpec.toString());
              throw paramSecureRandom;
            }
            paramSecureRandom = new java/security/InvalidAlgorithmParameterException;
            paramAlgorithmParameterSpec = new java/lang/StringBuilder;
            paramAlgorithmParameterSpec.<init>();
            paramAlgorithmParameterSpec.append("Key size must be positive: ");
            paramAlgorithmParameterSpec.append(mKeySizeBits);
            paramSecureRandom.<init>(paramAlgorithmParameterSpec.toString());
            throw paramSecureRandom;
          }
          paramAlgorithmParameterSpec = new java/security/InvalidAlgorithmParameterException;
          paramAlgorithmParameterSpec.<init>("KeyStore entry alias not provided");
          throw paramAlgorithmParameterSpec;
        }
      }
      finally
      {
        break label610;
      }
    }
    paramAlgorithmParameterSpec = new java/security/InvalidAlgorithmParameterException;
    paramSecureRandom = new java/lang/StringBuilder;
    paramSecureRandom.<init>();
    paramSecureRandom.append("Cannot initialize without a ");
    paramSecureRandom.append(KeyGenParameterSpec.class.getName());
    paramSecureRandom.append(" parameter");
    paramAlgorithmParameterSpec.<init>(paramSecureRandom.toString());
    throw paramAlgorithmParameterSpec;
    label610:
    if (0 == 0) {
      resetAll();
    }
    throw paramAlgorithmParameterSpec;
  }
  
  public static class AES
    extends AndroidKeyStoreKeyGeneratorSpi
  {
    public AES()
    {
      super(128);
    }
    
    protected void engineInit(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
      throws InvalidAlgorithmParameterException
    {
      super.engineInit(paramAlgorithmParameterSpec, paramSecureRandom);
      if ((mKeySizeBits != 128) && (mKeySizeBits != 192) && (mKeySizeBits != 256))
      {
        paramAlgorithmParameterSpec = new StringBuilder();
        paramAlgorithmParameterSpec.append("Unsupported key size: ");
        paramAlgorithmParameterSpec.append(mKeySizeBits);
        paramAlgorithmParameterSpec.append(". Supported: 128, 192, 256.");
        throw new InvalidAlgorithmParameterException(paramAlgorithmParameterSpec.toString());
      }
    }
  }
  
  public static class DESede
    extends AndroidKeyStoreKeyGeneratorSpi
  {
    public DESede()
    {
      super(168);
    }
  }
  
  protected static abstract class HmacBase
    extends AndroidKeyStoreKeyGeneratorSpi
  {
    protected HmacBase(int paramInt)
    {
      super(paramInt, KeymasterUtils.getDigestOutputSizeBits(paramInt));
    }
  }
  
  public static class HmacSHA1
    extends AndroidKeyStoreKeyGeneratorSpi.HmacBase
  {
    public HmacSHA1()
    {
      super();
    }
  }
  
  public static class HmacSHA224
    extends AndroidKeyStoreKeyGeneratorSpi.HmacBase
  {
    public HmacSHA224()
    {
      super();
    }
  }
  
  public static class HmacSHA256
    extends AndroidKeyStoreKeyGeneratorSpi.HmacBase
  {
    public HmacSHA256()
    {
      super();
    }
  }
  
  public static class HmacSHA384
    extends AndroidKeyStoreKeyGeneratorSpi.HmacBase
  {
    public HmacSHA384()
    {
      super();
    }
  }
  
  public static class HmacSHA512
    extends AndroidKeyStoreKeyGeneratorSpi.HmacBase
  {
    public HmacSHA512()
    {
      super();
    }
  }
}
