package android.security.keystore;

import android.security.keymaster.KeymasterArguments;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import javax.crypto.spec.IvParameterSpec;

class AndroidKeyStoreUnauthenticatedAESCipherSpi
  extends AndroidKeyStoreCipherSpiBase
{
  private static final int BLOCK_SIZE_BYTES = 16;
  private byte[] mIv;
  private boolean mIvHasBeenUsed;
  private final boolean mIvRequired;
  private final int mKeymasterBlockMode;
  private final int mKeymasterPadding;
  
  AndroidKeyStoreUnauthenticatedAESCipherSpi(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    mKeymasterBlockMode = paramInt1;
    mKeymasterPadding = paramInt2;
    mIvRequired = paramBoolean;
  }
  
  protected final void addAlgorithmSpecificParametersToBegin(KeymasterArguments paramKeymasterArguments)
  {
    if ((isEncrypting()) && (mIvRequired) && (mIvHasBeenUsed)) {
      throw new IllegalStateException("IV has already been used. Reusing IV in encryption mode violates security best practices.");
    }
    paramKeymasterArguments.addEnum(268435458, 32);
    paramKeymasterArguments.addEnum(536870916, mKeymasterBlockMode);
    paramKeymasterArguments.addEnum(536870918, mKeymasterPadding);
    if ((mIvRequired) && (mIv != null)) {
      paramKeymasterArguments.addBytes(-1879047191, mIv);
    }
  }
  
  protected final int engineGetBlockSize()
  {
    return 16;
  }
  
  protected final byte[] engineGetIV()
  {
    return ArrayUtils.cloneIfNotEmpty(mIv);
  }
  
  protected final int engineGetOutputSize(int paramInt)
  {
    return paramInt + 48;
  }
  
  protected final AlgorithmParameters engineGetParameters()
  {
    if (!mIvRequired) {
      return null;
    }
    if ((mIv != null) && (mIv.length > 0)) {
      try
      {
        AlgorithmParameters localAlgorithmParameters = AlgorithmParameters.getInstance("AES");
        IvParameterSpec localIvParameterSpec = new javax/crypto/spec/IvParameterSpec;
        localIvParameterSpec.<init>(mIv);
        localAlgorithmParameters.init(localIvParameterSpec);
        return localAlgorithmParameters;
      }
      catch (InvalidParameterSpecException localInvalidParameterSpecException)
      {
        throw new ProviderException("Failed to initialize AES AlgorithmParameters with an IV", localInvalidParameterSpecException);
      }
      catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
      {
        throw new ProviderException("Failed to obtain AES AlgorithmParameters", localNoSuchAlgorithmException);
      }
    }
    return null;
  }
  
  protected final int getAdditionalEntropyAmountForBegin()
  {
    if ((mIvRequired) && (mIv == null) && (isEncrypting())) {
      return 16;
    }
    return 0;
  }
  
  protected final int getAdditionalEntropyAmountForFinish()
  {
    return 0;
  }
  
  protected final void initAlgorithmSpecificParameters()
    throws InvalidKeyException
  {
    if (!mIvRequired) {
      return;
    }
    if (isEncrypting()) {
      return;
    }
    throw new InvalidKeyException("IV required when decrypting. Use IvParameterSpec or AlgorithmParameters to provide it.");
  }
  
  protected final void initAlgorithmSpecificParameters(AlgorithmParameters paramAlgorithmParameters)
    throws InvalidAlgorithmParameterException
  {
    Object localObject;
    if (!mIvRequired)
    {
      if (paramAlgorithmParameters == null) {
        return;
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Unsupported parameters: ");
      ((StringBuilder)localObject).append(paramAlgorithmParameters);
      throw new InvalidAlgorithmParameterException(((StringBuilder)localObject).toString());
    }
    if (paramAlgorithmParameters == null)
    {
      if (isEncrypting()) {
        return;
      }
      throw new InvalidAlgorithmParameterException("IV required when decrypting. Use IvParameterSpec or AlgorithmParameters to provide it.");
    }
    if ("AES".equalsIgnoreCase(paramAlgorithmParameters.getAlgorithm())) {
      try
      {
        localObject = (IvParameterSpec)paramAlgorithmParameters.getParameterSpec(IvParameterSpec.class);
        mIv = ((IvParameterSpec)localObject).getIV();
        if (mIv != null) {
          return;
        }
        throw new InvalidAlgorithmParameterException("Null IV in AlgorithmParameters");
      }
      catch (InvalidParameterSpecException localInvalidParameterSpecException)
      {
        if (isEncrypting())
        {
          mIv = null;
          return;
        }
        StringBuilder localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append("IV required when decrypting, but not found in parameters: ");
        localStringBuilder2.append(paramAlgorithmParameters);
        throw new InvalidAlgorithmParameterException(localStringBuilder2.toString(), localInvalidParameterSpecException);
      }
    }
    StringBuilder localStringBuilder1 = new StringBuilder();
    localStringBuilder1.append("Unsupported AlgorithmParameters algorithm: ");
    localStringBuilder1.append(paramAlgorithmParameters.getAlgorithm());
    localStringBuilder1.append(". Supported: AES");
    throw new InvalidAlgorithmParameterException(localStringBuilder1.toString());
  }
  
  protected final void initAlgorithmSpecificParameters(AlgorithmParameterSpec paramAlgorithmParameterSpec)
    throws InvalidAlgorithmParameterException
  {
    if (!mIvRequired)
    {
      if (paramAlgorithmParameterSpec == null) {
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unsupported parameters: ");
      localStringBuilder.append(paramAlgorithmParameterSpec);
      throw new InvalidAlgorithmParameterException(localStringBuilder.toString());
    }
    if (paramAlgorithmParameterSpec == null)
    {
      if (isEncrypting()) {
        return;
      }
      throw new InvalidAlgorithmParameterException("IvParameterSpec must be provided when decrypting");
    }
    if ((paramAlgorithmParameterSpec instanceof IvParameterSpec))
    {
      mIv = ((IvParameterSpec)paramAlgorithmParameterSpec).getIV();
      if (mIv != null) {
        return;
      }
      throw new InvalidAlgorithmParameterException("Null IV in IvParameterSpec");
    }
    throw new InvalidAlgorithmParameterException("Only IvParameterSpec supported");
  }
  
  protected final void initKey(int paramInt, Key paramKey)
    throws InvalidKeyException
  {
    if (!(paramKey instanceof AndroidKeyStoreSecretKey))
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unsupported key: ");
      if (paramKey != null) {
        paramKey = paramKey.getClass().getName();
      } else {
        paramKey = "null";
      }
      localStringBuilder.append(paramKey);
      throw new InvalidKeyException(localStringBuilder.toString());
    }
    if ("AES".equalsIgnoreCase(paramKey.getAlgorithm()))
    {
      setKey((AndroidKeyStoreSecretKey)paramKey);
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unsupported key algorithm: ");
    localStringBuilder.append(paramKey.getAlgorithm());
    localStringBuilder.append(". Only ");
    localStringBuilder.append("AES");
    localStringBuilder.append(" supported");
    throw new InvalidKeyException(localStringBuilder.toString());
  }
  
  protected final void loadAlgorithmSpecificParametersFromBeginResult(KeymasterArguments paramKeymasterArguments)
  {
    mIvHasBeenUsed = true;
    byte[] arrayOfByte = paramKeymasterArguments.getBytes(-1879047191, null);
    paramKeymasterArguments = arrayOfByte;
    if (arrayOfByte != null)
    {
      paramKeymasterArguments = arrayOfByte;
      if (arrayOfByte.length == 0) {
        paramKeymasterArguments = null;
      }
    }
    if (mIvRequired)
    {
      if (mIv == null) {
        mIv = paramKeymasterArguments;
      } else if ((paramKeymasterArguments != null) && (!Arrays.equals(paramKeymasterArguments, mIv))) {
        throw new ProviderException("IV in use differs from provided IV");
      }
    }
    else {
      if (paramKeymasterArguments != null) {
        break label83;
      }
    }
    return;
    label83:
    throw new ProviderException("IV in use despite IV not being used by this transformation");
  }
  
  protected final void resetAll()
  {
    mIv = null;
    mIvHasBeenUsed = false;
    super.resetAll();
  }
  
  protected final void resetWhilePreservingInitState()
  {
    super.resetWhilePreservingInitState();
  }
  
  static abstract class CBC
    extends AndroidKeyStoreUnauthenticatedAESCipherSpi
  {
    protected CBC(int paramInt)
    {
      super(paramInt, true);
    }
    
    public static class NoPadding
      extends AndroidKeyStoreUnauthenticatedAESCipherSpi.CBC
    {
      public NoPadding()
      {
        super();
      }
    }
    
    public static class PKCS7Padding
      extends AndroidKeyStoreUnauthenticatedAESCipherSpi.CBC
    {
      public PKCS7Padding()
      {
        super();
      }
    }
  }
  
  static abstract class CTR
    extends AndroidKeyStoreUnauthenticatedAESCipherSpi
  {
    protected CTR(int paramInt)
    {
      super(paramInt, true);
    }
    
    public static class NoPadding
      extends AndroidKeyStoreUnauthenticatedAESCipherSpi.CTR
    {
      public NoPadding()
      {
        super();
      }
    }
  }
  
  static abstract class ECB
    extends AndroidKeyStoreUnauthenticatedAESCipherSpi
  {
    protected ECB(int paramInt)
    {
      super(paramInt, false);
    }
    
    public static class NoPadding
      extends AndroidKeyStoreUnauthenticatedAESCipherSpi.ECB
    {
      public NoPadding()
      {
        super();
      }
    }
    
    public static class PKCS7Padding
      extends AndroidKeyStoreUnauthenticatedAESCipherSpi.ECB
    {
      public PKCS7Padding()
      {
        super();
      }
    }
  }
}
