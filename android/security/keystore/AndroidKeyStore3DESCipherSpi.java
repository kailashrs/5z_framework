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

public class AndroidKeyStore3DESCipherSpi
  extends AndroidKeyStoreCipherSpiBase
{
  private static final int BLOCK_SIZE_BYTES = 8;
  private byte[] mIv;
  private boolean mIvHasBeenUsed;
  private final boolean mIvRequired;
  private final int mKeymasterBlockMode;
  private final int mKeymasterPadding;
  
  AndroidKeyStore3DESCipherSpi(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    mKeymasterBlockMode = paramInt1;
    mKeymasterPadding = paramInt2;
    mIvRequired = paramBoolean;
  }
  
  protected void addAlgorithmSpecificParametersToBegin(KeymasterArguments paramKeymasterArguments)
  {
    if ((isEncrypting()) && (mIvRequired) && (mIvHasBeenUsed)) {
      throw new IllegalStateException("IV has already been used. Reusing IV in encryption mode violates security best practices.");
    }
    paramKeymasterArguments.addEnum(268435458, 33);
    paramKeymasterArguments.addEnum(536870916, mKeymasterBlockMode);
    paramKeymasterArguments.addEnum(536870918, mKeymasterPadding);
    if ((mIvRequired) && (mIv != null)) {
      paramKeymasterArguments.addBytes(-1879047191, mIv);
    }
  }
  
  protected int engineGetBlockSize()
  {
    return 8;
  }
  
  protected final byte[] engineGetIV()
  {
    return ArrayUtils.cloneIfNotEmpty(mIv);
  }
  
  protected int engineGetOutputSize(int paramInt)
  {
    return paramInt + 24;
  }
  
  protected AlgorithmParameters engineGetParameters()
  {
    if (!mIvRequired) {
      return null;
    }
    if ((mIv != null) && (mIv.length > 0)) {
      try
      {
        AlgorithmParameters localAlgorithmParameters = AlgorithmParameters.getInstance("DESede");
        IvParameterSpec localIvParameterSpec = new javax/crypto/spec/IvParameterSpec;
        localIvParameterSpec.<init>(mIv);
        localAlgorithmParameters.init(localIvParameterSpec);
        return localAlgorithmParameters;
      }
      catch (InvalidParameterSpecException localInvalidParameterSpecException)
      {
        throw new ProviderException("Failed to initialize 3DES AlgorithmParameters with an IV", localInvalidParameterSpecException);
      }
      catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
      {
        throw new ProviderException("Failed to obtain 3DES AlgorithmParameters", localNoSuchAlgorithmException);
      }
    }
    return null;
  }
  
  protected final int getAdditionalEntropyAmountForBegin()
  {
    if ((mIvRequired) && (mIv == null) && (isEncrypting())) {
      return 8;
    }
    return 0;
  }
  
  protected int getAdditionalEntropyAmountForFinish()
  {
    return 0;
  }
  
  protected void initAlgorithmSpecificParameters()
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
  
  protected void initAlgorithmSpecificParameters(AlgorithmParameters paramAlgorithmParameters)
    throws InvalidAlgorithmParameterException
  {
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
    if ("DESede".equalsIgnoreCase(paramAlgorithmParameters.getAlgorithm())) {
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
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("IV required when decrypting, but not found in parameters: ");
        ((StringBuilder)localObject).append(paramAlgorithmParameters);
        throw new InvalidAlgorithmParameterException(((StringBuilder)localObject).toString(), localInvalidParameterSpecException);
      }
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Unsupported AlgorithmParameters algorithm: ");
    ((StringBuilder)localObject).append(paramAlgorithmParameters.getAlgorithm());
    ((StringBuilder)localObject).append(". Supported: DESede");
    throw new InvalidAlgorithmParameterException(((StringBuilder)localObject).toString());
  }
  
  protected void initAlgorithmSpecificParameters(AlgorithmParameterSpec paramAlgorithmParameterSpec)
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
  
  protected void initKey(int paramInt, Key paramKey)
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
    if ("DESede".equalsIgnoreCase(paramKey.getAlgorithm()))
    {
      setKey((AndroidKeyStoreSecretKey)paramKey);
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unsupported key algorithm: ");
    localStringBuilder.append(paramKey.getAlgorithm());
    localStringBuilder.append(". Only ");
    localStringBuilder.append("DESede");
    localStringBuilder.append(" supported");
    throw new InvalidKeyException(localStringBuilder.toString());
  }
  
  protected void loadAlgorithmSpecificParametersFromBeginResult(KeymasterArguments paramKeymasterArguments)
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
  
  static abstract class CBC
    extends AndroidKeyStore3DESCipherSpi
  {
    protected CBC(int paramInt)
    {
      super(paramInt, true);
    }
    
    public static class NoPadding
      extends AndroidKeyStore3DESCipherSpi.CBC
    {
      public NoPadding()
      {
        super();
      }
    }
    
    public static class PKCS7Padding
      extends AndroidKeyStore3DESCipherSpi.CBC
    {
      public PKCS7Padding()
      {
        super();
      }
    }
  }
  
  static abstract class ECB
    extends AndroidKeyStore3DESCipherSpi
  {
    protected ECB(int paramInt)
    {
      super(paramInt, false);
    }
    
    public static class NoPadding
      extends AndroidKeyStore3DESCipherSpi.ECB
    {
      public NoPadding()
      {
        super();
      }
    }
    
    public static class PKCS7Padding
      extends AndroidKeyStore3DESCipherSpi.ECB
    {
      public PKCS7Padding()
      {
        super();
      }
    }
  }
}
