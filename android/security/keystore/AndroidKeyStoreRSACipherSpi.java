package android.security.keystore;

import android.security.KeyStore;
import android.security.keymaster.KeyCharacteristics;
import android.security.keymaster.KeymasterArguments;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.MGF1ParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource.PSpecified;

abstract class AndroidKeyStoreRSACipherSpi
  extends AndroidKeyStoreCipherSpiBase
{
  private final int mKeymasterPadding;
  private int mKeymasterPaddingOverride;
  private int mModulusSizeBytes = -1;
  
  AndroidKeyStoreRSACipherSpi(int paramInt)
  {
    mKeymasterPadding = paramInt;
  }
  
  protected void addAlgorithmSpecificParametersToBegin(KeymasterArguments paramKeymasterArguments)
  {
    paramKeymasterArguments.addEnum(268435458, 1);
    int i = getKeymasterPaddingOverride();
    int j = i;
    if (i == -1) {
      j = mKeymasterPadding;
    }
    paramKeymasterArguments.addEnum(536870918, j);
    j = getKeymasterPurposeOverride();
    if ((j != -1) && ((j == 2) || (j == 3))) {
      paramKeymasterArguments.addEnum(536870917, 0);
    }
  }
  
  protected boolean adjustConfigForEncryptingWithPrivateKey()
  {
    return false;
  }
  
  protected final int engineGetBlockSize()
  {
    return 0;
  }
  
  protected final byte[] engineGetIV()
  {
    return null;
  }
  
  protected final int engineGetOutputSize(int paramInt)
  {
    return getModulusSizeBytes();
  }
  
  protected final int getKeymasterPaddingOverride()
  {
    return mKeymasterPaddingOverride;
  }
  
  protected final int getModulusSizeBytes()
  {
    if (mModulusSizeBytes != -1) {
      return mModulusSizeBytes;
    }
    throw new IllegalStateException("Not initialized");
  }
  
  protected final void initKey(int paramInt, Key paramKey)
    throws InvalidKeyException
  {
    if (paramKey != null)
    {
      if ("RSA".equalsIgnoreCase(paramKey.getAlgorithm()))
      {
        if ((paramKey instanceof AndroidKeyStorePrivateKey)) {}
        for (paramKey = (AndroidKeyStoreKey)paramKey;; paramKey = (AndroidKeyStoreKey)paramKey)
        {
          break;
          if (!(paramKey instanceof AndroidKeyStorePublicKey)) {
            break label476;
          }
        }
        if ((paramKey instanceof PrivateKey)) {
          switch (paramInt)
          {
          default: 
            paramKey = new StringBuilder();
            paramKey.append("RSA private keys cannot be used with opmode: ");
            paramKey.append(paramInt);
            throw new InvalidKeyException(paramKey.toString());
          case 2: 
          case 4: 
            break;
          case 1: 
          case 3: 
            if (adjustConfigForEncryptingWithPrivateKey()) {
              break;
            }
            paramKey = new StringBuilder();
            paramKey.append("RSA private keys cannot be used with ");
            paramKey.append(opmodeToString(paramInt));
            paramKey.append(" and padding ");
            paramKey.append(KeyProperties.EncryptionPadding.fromKeymaster(mKeymasterPadding));
            paramKey.append(". Only RSA public keys supported for this mode");
            throw new InvalidKeyException(paramKey.toString());
          }
        } else {
          switch (paramInt)
          {
          default: 
            paramKey = new StringBuilder();
            paramKey.append("RSA public keys cannot be used with ");
            paramKey.append(opmodeToString(paramInt));
            throw new InvalidKeyException(paramKey.toString());
          case 2: 
          case 4: 
            paramKey = new StringBuilder();
            paramKey.append("RSA public keys cannot be used with ");
            paramKey.append(opmodeToString(paramInt));
            paramKey.append(" and padding ");
            paramKey.append(KeyProperties.EncryptionPadding.fromKeymaster(mKeymasterPadding));
            paramKey.append(". Only RSA private keys supported for this opmode.");
            throw new InvalidKeyException(paramKey.toString());
          }
        }
        localObject = new KeyCharacteristics();
        paramInt = getKeyStore().getKeyCharacteristics(paramKey.getAlias(), null, null, paramKey.getUid(), (KeyCharacteristics)localObject);
        if (paramInt == 1)
        {
          long l = ((KeyCharacteristics)localObject).getUnsignedInt(805306371, -1L);
          if (l != -1L)
          {
            if (l <= 2147483647L)
            {
              mModulusSizeBytes = ((int)((7L + l) / 8L));
              setKey(paramKey);
              return;
            }
            paramKey = new StringBuilder();
            paramKey.append("Key too large: ");
            paramKey.append(l);
            paramKey.append(" bits");
            throw new InvalidKeyException(paramKey.toString());
          }
          throw new InvalidKeyException("Size of key not known");
        }
        throw getKeyStore().getInvalidKeyException(paramKey.getAlias(), paramKey.getUid(), paramInt);
        label476:
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Unsupported key type: ");
        ((StringBuilder)localObject).append(paramKey);
        throw new InvalidKeyException(((StringBuilder)localObject).toString());
      }
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Unsupported key algorithm: ");
      ((StringBuilder)localObject).append(paramKey.getAlgorithm());
      ((StringBuilder)localObject).append(". Only ");
      ((StringBuilder)localObject).append("RSA");
      ((StringBuilder)localObject).append(" supported");
      throw new InvalidKeyException(((StringBuilder)localObject).toString());
    }
    throw new InvalidKeyException("Unsupported key: null");
  }
  
  protected void loadAlgorithmSpecificParametersFromBeginResult(KeymasterArguments paramKeymasterArguments) {}
  
  protected final void resetAll()
  {
    mModulusSizeBytes = -1;
    mKeymasterPaddingOverride = -1;
    super.resetAll();
  }
  
  protected final void resetWhilePreservingInitState()
  {
    super.resetWhilePreservingInitState();
  }
  
  protected final void setKeymasterPaddingOverride(int paramInt)
  {
    mKeymasterPaddingOverride = paramInt;
  }
  
  public static final class NoPadding
    extends AndroidKeyStoreRSACipherSpi
  {
    public NoPadding()
    {
      super();
    }
    
    protected boolean adjustConfigForEncryptingWithPrivateKey()
    {
      setKeymasterPurposeOverride(2);
      return true;
    }
    
    protected AlgorithmParameters engineGetParameters()
    {
      return null;
    }
    
    protected final int getAdditionalEntropyAmountForBegin()
    {
      return 0;
    }
    
    protected final int getAdditionalEntropyAmountForFinish()
    {
      return 0;
    }
    
    protected void initAlgorithmSpecificParameters()
      throws InvalidKeyException
    {}
    
    protected void initAlgorithmSpecificParameters(AlgorithmParameters paramAlgorithmParameters)
      throws InvalidAlgorithmParameterException
    {
      if (paramAlgorithmParameters == null) {
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unexpected parameters: ");
      localStringBuilder.append(paramAlgorithmParameters);
      localStringBuilder.append(". No parameters supported");
      throw new InvalidAlgorithmParameterException(localStringBuilder.toString());
    }
    
    protected void initAlgorithmSpecificParameters(AlgorithmParameterSpec paramAlgorithmParameterSpec)
      throws InvalidAlgorithmParameterException
    {
      if (paramAlgorithmParameterSpec == null) {
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unexpected parameters: ");
      localStringBuilder.append(paramAlgorithmParameterSpec);
      localStringBuilder.append(". No parameters supported");
      throw new InvalidAlgorithmParameterException(localStringBuilder.toString());
    }
  }
  
  static abstract class OAEPWithMGF1Padding
    extends AndroidKeyStoreRSACipherSpi
  {
    private static final String MGF_ALGORITGM_MGF1 = "MGF1";
    private int mDigestOutputSizeBytes;
    private int mKeymasterDigest = -1;
    
    OAEPWithMGF1Padding(int paramInt)
    {
      super();
      mKeymasterDigest = paramInt;
      mDigestOutputSizeBytes = ((KeymasterUtils.getDigestOutputSizeBits(paramInt) + 7) / 8);
    }
    
    protected final void addAlgorithmSpecificParametersToBegin(KeymasterArguments paramKeymasterArguments)
    {
      super.addAlgorithmSpecificParametersToBegin(paramKeymasterArguments);
      paramKeymasterArguments.addEnum(536870917, mKeymasterDigest);
    }
    
    protected final AlgorithmParameters engineGetParameters()
    {
      OAEPParameterSpec localOAEPParameterSpec = new OAEPParameterSpec(KeyProperties.Digest.fromKeymaster(mKeymasterDigest), "MGF1", MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT);
      try
      {
        AlgorithmParameters localAlgorithmParameters = AlgorithmParameters.getInstance("OAEP");
        localAlgorithmParameters.init(localOAEPParameterSpec);
        return localAlgorithmParameters;
      }
      catch (InvalidParameterSpecException localInvalidParameterSpecException)
      {
        throw new ProviderException("Failed to initialize OAEP AlgorithmParameters with an IV", localInvalidParameterSpecException);
      }
      catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
      {
        throw new ProviderException("Failed to obtain OAEP AlgorithmParameters", localNoSuchAlgorithmException);
      }
    }
    
    protected final int getAdditionalEntropyAmountForBegin()
    {
      return 0;
    }
    
    protected final int getAdditionalEntropyAmountForFinish()
    {
      int i;
      if (isEncrypting()) {
        i = mDigestOutputSizeBytes;
      } else {
        i = 0;
      }
      return i;
    }
    
    protected final void initAlgorithmSpecificParameters()
      throws InvalidKeyException
    {}
    
    protected final void initAlgorithmSpecificParameters(AlgorithmParameters paramAlgorithmParameters)
      throws InvalidAlgorithmParameterException
    {
      if (paramAlgorithmParameters == null) {
        return;
      }
      try
      {
        Object localObject = (OAEPParameterSpec)paramAlgorithmParameters.getParameterSpec(OAEPParameterSpec.class);
        if (localObject != null)
        {
          initAlgorithmSpecificParameters((AlgorithmParameterSpec)localObject);
          return;
        }
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("OAEP parameters required, but not provided in parameters: ");
        ((StringBuilder)localObject).append(paramAlgorithmParameters);
        throw new InvalidAlgorithmParameterException(((StringBuilder)localObject).toString());
      }
      catch (InvalidParameterSpecException localInvalidParameterSpecException)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("OAEP parameters required, but not found in parameters: ");
        localStringBuilder.append(paramAlgorithmParameters);
        throw new InvalidAlgorithmParameterException(localStringBuilder.toString(), localInvalidParameterSpecException);
      }
    }
    
    protected final void initAlgorithmSpecificParameters(AlgorithmParameterSpec paramAlgorithmParameterSpec)
      throws InvalidAlgorithmParameterException
    {
      if (paramAlgorithmParameterSpec == null) {
        return;
      }
      if ((paramAlgorithmParameterSpec instanceof OAEPParameterSpec))
      {
        paramAlgorithmParameterSpec = (OAEPParameterSpec)paramAlgorithmParameterSpec;
        if ("MGF1".equalsIgnoreCase(paramAlgorithmParameterSpec.getMGFAlgorithm()))
        {
          localObject = paramAlgorithmParameterSpec.getDigestAlgorithm();
          try
          {
            int i = KeyProperties.Digest.toKeymaster((String)localObject);
            switch (i)
            {
            default: 
              paramAlgorithmParameterSpec = new StringBuilder();
              paramAlgorithmParameterSpec.append("Unsupported digest: ");
              paramAlgorithmParameterSpec.append((String)localObject);
              throw new InvalidAlgorithmParameterException(paramAlgorithmParameterSpec.toString());
            }
            localObject = paramAlgorithmParameterSpec.getMGFParameters();
            if (localObject != null)
            {
              if ((localObject instanceof MGF1ParameterSpec))
              {
                localObject = ((MGF1ParameterSpec)localObject).getDigestAlgorithm();
                if ("SHA-1".equalsIgnoreCase((String)localObject))
                {
                  paramAlgorithmParameterSpec = paramAlgorithmParameterSpec.getPSource();
                  if ((paramAlgorithmParameterSpec instanceof PSource.PSpecified))
                  {
                    localObject = ((PSource.PSpecified)paramAlgorithmParameterSpec).getValue();
                    if ((localObject != null) && (localObject.length > 0))
                    {
                      localObject = new StringBuilder();
                      ((StringBuilder)localObject).append("Unsupported source of encoding input P: ");
                      ((StringBuilder)localObject).append(paramAlgorithmParameterSpec);
                      ((StringBuilder)localObject).append(". Only pSpecifiedEmpty (PSource.PSpecified.DEFAULT) supported");
                      throw new InvalidAlgorithmParameterException(((StringBuilder)localObject).toString());
                    }
                    mKeymasterDigest = i;
                    mDigestOutputSizeBytes = ((KeymasterUtils.getDigestOutputSizeBits(i) + 7) / 8);
                    return;
                  }
                  localObject = new StringBuilder();
                  ((StringBuilder)localObject).append("Unsupported source of encoding input P: ");
                  ((StringBuilder)localObject).append(paramAlgorithmParameterSpec);
                  ((StringBuilder)localObject).append(". Only pSpecifiedEmpty (PSource.PSpecified.DEFAULT) supported");
                  throw new InvalidAlgorithmParameterException(((StringBuilder)localObject).toString());
                }
                paramAlgorithmParameterSpec = new StringBuilder();
                paramAlgorithmParameterSpec.append("Unsupported MGF1 digest: ");
                paramAlgorithmParameterSpec.append((String)localObject);
                paramAlgorithmParameterSpec.append(". Only ");
                paramAlgorithmParameterSpec.append("SHA-1");
                paramAlgorithmParameterSpec.append(" supported");
                throw new InvalidAlgorithmParameterException(paramAlgorithmParameterSpec.toString());
              }
              paramAlgorithmParameterSpec = new StringBuilder();
              paramAlgorithmParameterSpec.append("Unsupported MGF parameters: ");
              paramAlgorithmParameterSpec.append(localObject);
              paramAlgorithmParameterSpec.append(". Only MGF1ParameterSpec supported");
              throw new InvalidAlgorithmParameterException(paramAlgorithmParameterSpec.toString());
            }
            throw new InvalidAlgorithmParameterException("MGF parameters must be provided");
          }
          catch (IllegalArgumentException localIllegalArgumentException)
          {
            paramAlgorithmParameterSpec = new StringBuilder();
            paramAlgorithmParameterSpec.append("Unsupported digest: ");
            paramAlgorithmParameterSpec.append((String)localObject);
            throw new InvalidAlgorithmParameterException(paramAlgorithmParameterSpec.toString(), localIllegalArgumentException);
          }
        }
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Unsupported MGF: ");
        ((StringBuilder)localObject).append(paramAlgorithmParameterSpec.getMGFAlgorithm());
        ((StringBuilder)localObject).append(". Only ");
        ((StringBuilder)localObject).append("MGF1");
        ((StringBuilder)localObject).append(" supported");
        throw new InvalidAlgorithmParameterException(((StringBuilder)localObject).toString());
      }
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Unsupported parameter spec: ");
      ((StringBuilder)localObject).append(paramAlgorithmParameterSpec);
      ((StringBuilder)localObject).append(". Only OAEPParameterSpec supported");
      throw new InvalidAlgorithmParameterException(((StringBuilder)localObject).toString());
    }
    
    protected final void loadAlgorithmSpecificParametersFromBeginResult(KeymasterArguments paramKeymasterArguments)
    {
      super.loadAlgorithmSpecificParametersFromBeginResult(paramKeymasterArguments);
    }
  }
  
  public static class OAEPWithSHA1AndMGF1Padding
    extends AndroidKeyStoreRSACipherSpi.OAEPWithMGF1Padding
  {
    public OAEPWithSHA1AndMGF1Padding()
    {
      super();
    }
  }
  
  public static class OAEPWithSHA224AndMGF1Padding
    extends AndroidKeyStoreRSACipherSpi.OAEPWithMGF1Padding
  {
    public OAEPWithSHA224AndMGF1Padding()
    {
      super();
    }
  }
  
  public static class OAEPWithSHA256AndMGF1Padding
    extends AndroidKeyStoreRSACipherSpi.OAEPWithMGF1Padding
  {
    public OAEPWithSHA256AndMGF1Padding()
    {
      super();
    }
  }
  
  public static class OAEPWithSHA384AndMGF1Padding
    extends AndroidKeyStoreRSACipherSpi.OAEPWithMGF1Padding
  {
    public OAEPWithSHA384AndMGF1Padding()
    {
      super();
    }
  }
  
  public static class OAEPWithSHA512AndMGF1Padding
    extends AndroidKeyStoreRSACipherSpi.OAEPWithMGF1Padding
  {
    public OAEPWithSHA512AndMGF1Padding()
    {
      super();
    }
  }
  
  public static final class PKCS1Padding
    extends AndroidKeyStoreRSACipherSpi
  {
    public PKCS1Padding()
    {
      super();
    }
    
    protected boolean adjustConfigForEncryptingWithPrivateKey()
    {
      setKeymasterPurposeOverride(2);
      setKeymasterPaddingOverride(5);
      return true;
    }
    
    protected AlgorithmParameters engineGetParameters()
    {
      return null;
    }
    
    protected final int getAdditionalEntropyAmountForBegin()
    {
      return 0;
    }
    
    protected final int getAdditionalEntropyAmountForFinish()
    {
      int i;
      if (isEncrypting()) {
        i = getModulusSizeBytes();
      } else {
        i = 0;
      }
      return i;
    }
    
    protected void initAlgorithmSpecificParameters()
      throws InvalidKeyException
    {}
    
    protected void initAlgorithmSpecificParameters(AlgorithmParameters paramAlgorithmParameters)
      throws InvalidAlgorithmParameterException
    {
      if (paramAlgorithmParameters == null) {
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unexpected parameters: ");
      localStringBuilder.append(paramAlgorithmParameters);
      localStringBuilder.append(". No parameters supported");
      throw new InvalidAlgorithmParameterException(localStringBuilder.toString());
    }
    
    protected void initAlgorithmSpecificParameters(AlgorithmParameterSpec paramAlgorithmParameterSpec)
      throws InvalidAlgorithmParameterException
    {
      if (paramAlgorithmParameterSpec == null) {
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unexpected parameters: ");
      localStringBuilder.append(paramAlgorithmParameterSpec);
      localStringBuilder.append(". No parameters supported");
      throw new InvalidAlgorithmParameterException(localStringBuilder.toString());
    }
  }
}
