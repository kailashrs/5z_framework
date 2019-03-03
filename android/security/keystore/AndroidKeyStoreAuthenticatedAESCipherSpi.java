package android.security.keystore;

import android.os.IBinder;
import android.security.KeyStore;
import android.security.KeyStoreException;
import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.OperationResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import javax.crypto.spec.GCMParameterSpec;
import libcore.util.EmptyArray;

abstract class AndroidKeyStoreAuthenticatedAESCipherSpi
  extends AndroidKeyStoreCipherSpiBase
{
  private static final int BLOCK_SIZE_BYTES = 16;
  private byte[] mIv;
  private boolean mIvHasBeenUsed;
  private final int mKeymasterBlockMode;
  private final int mKeymasterPadding;
  
  AndroidKeyStoreAuthenticatedAESCipherSpi(int paramInt1, int paramInt2)
  {
    mKeymasterBlockMode = paramInt1;
    mKeymasterPadding = paramInt2;
  }
  
  protected void addAlgorithmSpecificParametersToBegin(KeymasterArguments paramKeymasterArguments)
  {
    if ((isEncrypting()) && (mIvHasBeenUsed)) {
      throw new IllegalStateException("IV has already been used. Reusing IV in encryption mode violates security best practices.");
    }
    paramKeymasterArguments.addEnum(268435458, 32);
    paramKeymasterArguments.addEnum(536870916, mKeymasterBlockMode);
    paramKeymasterArguments.addEnum(536870918, mKeymasterPadding);
    if (mIv != null) {
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
  
  protected byte[] getIv()
  {
    return mIv;
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
    if (mIv == null) {
      mIv = paramKeymasterArguments;
    } else if ((paramKeymasterArguments != null) && (!Arrays.equals(paramKeymasterArguments, mIv))) {
      throw new ProviderException("IV in use differs from provided IV");
    }
  }
  
  protected void resetAll()
  {
    mIv = null;
    mIvHasBeenUsed = false;
    super.resetAll();
  }
  
  protected void setIv(byte[] paramArrayOfByte)
  {
    mIv = paramArrayOfByte;
  }
  
  private static class AdditionalAuthenticationDataStream
    implements KeyStoreCryptoOperationChunkedStreamer.Stream
  {
    private final KeyStore mKeyStore;
    private final IBinder mOperationToken;
    
    private AdditionalAuthenticationDataStream(KeyStore paramKeyStore, IBinder paramIBinder)
    {
      mKeyStore = paramKeyStore;
      mOperationToken = paramIBinder;
    }
    
    public OperationResult finish(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    {
      if ((paramArrayOfByte2 != null) && (paramArrayOfByte2.length > 0)) {
        throw new ProviderException("AAD stream does not support additional entropy");
      }
      return new OperationResult(1, mOperationToken, 0L, 0, EmptyArray.BYTE, new KeymasterArguments());
    }
    
    public OperationResult update(byte[] paramArrayOfByte)
    {
      Object localObject = new KeymasterArguments();
      ((KeymasterArguments)localObject).addBytes(-1879047192, paramArrayOfByte);
      OperationResult localOperationResult = mKeyStore.update(mOperationToken, (KeymasterArguments)localObject, null);
      localObject = localOperationResult;
      if (resultCode == 1) {
        localObject = new OperationResult(resultCode, token, operationHandle, paramArrayOfByte.length, output, outParams);
      }
      return localObject;
    }
  }
  
  private static class BufferAllOutputUntilDoFinalStreamer
    implements KeyStoreCryptoOperationStreamer
  {
    private ByteArrayOutputStream mBufferedOutput = new ByteArrayOutputStream();
    private final KeyStoreCryptoOperationStreamer mDelegate;
    private long mProducedOutputSizeBytes;
    
    private BufferAllOutputUntilDoFinalStreamer(KeyStoreCryptoOperationStreamer paramKeyStoreCryptoOperationStreamer)
    {
      mDelegate = paramKeyStoreCryptoOperationStreamer;
    }
    
    public byte[] doFinal(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
      throws KeyStoreException
    {
      paramArrayOfByte1 = mDelegate.doFinal(paramArrayOfByte1, paramInt1, paramInt2, paramArrayOfByte2, paramArrayOfByte3);
      if (paramArrayOfByte1 != null) {
        try
        {
          mBufferedOutput.write(paramArrayOfByte1);
        }
        catch (IOException paramArrayOfByte1)
        {
          throw new ProviderException("Failed to buffer output", paramArrayOfByte1);
        }
      }
      paramArrayOfByte1 = mBufferedOutput.toByteArray();
      mBufferedOutput.reset();
      mProducedOutputSizeBytes += paramArrayOfByte1.length;
      return paramArrayOfByte1;
    }
    
    public long getConsumedInputSizeBytes()
    {
      return mDelegate.getConsumedInputSizeBytes();
    }
    
    public long getProducedOutputSizeBytes()
    {
      return mProducedOutputSizeBytes;
    }
    
    public byte[] update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws KeyStoreException
    {
      paramArrayOfByte = mDelegate.update(paramArrayOfByte, paramInt1, paramInt2);
      if (paramArrayOfByte != null) {
        try
        {
          mBufferedOutput.write(paramArrayOfByte);
        }
        catch (IOException paramArrayOfByte)
        {
          throw new ProviderException("Failed to buffer output", paramArrayOfByte);
        }
      }
      return EmptyArray.BYTE;
    }
  }
  
  static abstract class GCM
    extends AndroidKeyStoreAuthenticatedAESCipherSpi
  {
    private static final int DEFAULT_TAG_LENGTH_BITS = 128;
    private static final int IV_LENGTH_BYTES = 12;
    private static final int MAX_SUPPORTED_TAG_LENGTH_BITS = 128;
    static final int MIN_SUPPORTED_TAG_LENGTH_BITS = 96;
    private int mTagLengthBits = 128;
    
    GCM(int paramInt)
    {
      super(paramInt);
    }
    
    protected final void addAlgorithmSpecificParametersToBegin(KeymasterArguments paramKeymasterArguments)
    {
      super.addAlgorithmSpecificParametersToBegin(paramKeymasterArguments);
      paramKeymasterArguments.addUnsignedInt(805307371, mTagLengthBits);
    }
    
    protected final KeyStoreCryptoOperationStreamer createAdditionalAuthenticationDataStreamer(KeyStore paramKeyStore, IBinder paramIBinder)
    {
      return new KeyStoreCryptoOperationChunkedStreamer(new AndroidKeyStoreAuthenticatedAESCipherSpi.AdditionalAuthenticationDataStream(paramKeyStore, paramIBinder, null));
    }
    
    protected KeyStoreCryptoOperationStreamer createMainDataStreamer(KeyStore paramKeyStore, IBinder paramIBinder)
    {
      paramKeyStore = new KeyStoreCryptoOperationChunkedStreamer(new KeyStoreCryptoOperationChunkedStreamer.MainDataStream(paramKeyStore, paramIBinder));
      if (isEncrypting()) {
        return paramKeyStore;
      }
      return new AndroidKeyStoreAuthenticatedAESCipherSpi.BufferAllOutputUntilDoFinalStreamer(paramKeyStore, null);
    }
    
    protected final AlgorithmParameters engineGetParameters()
    {
      byte[] arrayOfByte = getIv();
      if ((arrayOfByte != null) && (arrayOfByte.length > 0)) {
        try
        {
          AlgorithmParameters localAlgorithmParameters = AlgorithmParameters.getInstance("GCM");
          GCMParameterSpec localGCMParameterSpec = new javax/crypto/spec/GCMParameterSpec;
          localGCMParameterSpec.<init>(mTagLengthBits, arrayOfByte);
          localAlgorithmParameters.init(localGCMParameterSpec);
          return localAlgorithmParameters;
        }
        catch (InvalidParameterSpecException localInvalidParameterSpecException)
        {
          throw new ProviderException("Failed to initialize GCM AlgorithmParameters", localInvalidParameterSpecException);
        }
        catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
        {
          throw new ProviderException("Failed to obtain GCM AlgorithmParameters", localNoSuchAlgorithmException);
        }
      }
      return null;
    }
    
    protected final int getAdditionalEntropyAmountForBegin()
    {
      if ((getIv() == null) && (isEncrypting())) {
        return 12;
      }
      return 0;
    }
    
    protected final int getAdditionalEntropyAmountForFinish()
    {
      return 0;
    }
    
    protected final int getTagLengthBits()
    {
      return mTagLengthBits;
    }
    
    protected final void initAlgorithmSpecificParameters()
      throws InvalidKeyException
    {
      if (isEncrypting()) {
        return;
      }
      throw new InvalidKeyException("IV required when decrypting. Use IvParameterSpec or AlgorithmParameters to provide it.");
    }
    
    protected final void initAlgorithmSpecificParameters(AlgorithmParameters paramAlgorithmParameters)
      throws InvalidAlgorithmParameterException
    {
      if (paramAlgorithmParameters == null)
      {
        if (isEncrypting()) {
          return;
        }
        throw new InvalidAlgorithmParameterException("IV required when decrypting. Use GCMParameterSpec or GCM AlgorithmParameters to provide it.");
      }
      if ("GCM".equalsIgnoreCase(paramAlgorithmParameters.getAlgorithm())) {
        try
        {
          GCMParameterSpec localGCMParameterSpec = (GCMParameterSpec)paramAlgorithmParameters.getParameterSpec(GCMParameterSpec.class);
          initAlgorithmSpecificParameters(localGCMParameterSpec);
          return;
        }
        catch (InvalidParameterSpecException localInvalidParameterSpecException)
        {
          if (isEncrypting())
          {
            setIv(null);
            return;
          }
          StringBuilder localStringBuilder2 = new StringBuilder();
          localStringBuilder2.append("IV and tag length required when decrypting, but not found in parameters: ");
          localStringBuilder2.append(paramAlgorithmParameters);
          throw new InvalidAlgorithmParameterException(localStringBuilder2.toString(), localInvalidParameterSpecException);
        }
      }
      StringBuilder localStringBuilder1 = new StringBuilder();
      localStringBuilder1.append("Unsupported AlgorithmParameters algorithm: ");
      localStringBuilder1.append(paramAlgorithmParameters.getAlgorithm());
      localStringBuilder1.append(". Supported: GCM");
      throw new InvalidAlgorithmParameterException(localStringBuilder1.toString());
    }
    
    protected final void initAlgorithmSpecificParameters(AlgorithmParameterSpec paramAlgorithmParameterSpec)
      throws InvalidAlgorithmParameterException
    {
      if (paramAlgorithmParameterSpec == null)
      {
        if (isEncrypting()) {
          return;
        }
        throw new InvalidAlgorithmParameterException("GCMParameterSpec must be provided when decrypting");
      }
      if ((paramAlgorithmParameterSpec instanceof GCMParameterSpec))
      {
        Object localObject = (GCMParameterSpec)paramAlgorithmParameterSpec;
        paramAlgorithmParameterSpec = ((GCMParameterSpec)localObject).getIV();
        if (paramAlgorithmParameterSpec != null)
        {
          if (paramAlgorithmParameterSpec.length == 12)
          {
            int i = ((GCMParameterSpec)localObject).getTLen();
            if ((i >= 96) && (i <= 128) && (i % 8 == 0))
            {
              setIv(paramAlgorithmParameterSpec);
              mTagLengthBits = i;
              return;
            }
            paramAlgorithmParameterSpec = new StringBuilder();
            paramAlgorithmParameterSpec.append("Unsupported tag length: ");
            paramAlgorithmParameterSpec.append(i);
            paramAlgorithmParameterSpec.append(" bits. Supported lengths: 96, 104, 112, 120, 128");
            throw new InvalidAlgorithmParameterException(paramAlgorithmParameterSpec.toString());
          }
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Unsupported IV length: ");
          ((StringBuilder)localObject).append(paramAlgorithmParameterSpec.length);
          ((StringBuilder)localObject).append(" bytes. Only ");
          ((StringBuilder)localObject).append(12);
          ((StringBuilder)localObject).append(" bytes long IV supported");
          throw new InvalidAlgorithmParameterException(((StringBuilder)localObject).toString());
        }
        throw new InvalidAlgorithmParameterException("Null IV in GCMParameterSpec");
      }
      throw new InvalidAlgorithmParameterException("Only GCMParameterSpec supported");
    }
    
    protected final void resetAll()
    {
      mTagLengthBits = 128;
      super.resetAll();
    }
    
    protected final void resetWhilePreservingInitState()
    {
      super.resetWhilePreservingInitState();
    }
    
    public static final class NoPadding
      extends AndroidKeyStoreAuthenticatedAESCipherSpi.GCM
    {
      public NoPadding()
      {
        super();
      }
      
      protected final int engineGetOutputSize(int paramInt)
      {
        int i = (getTagLengthBits() + 7) / 8;
        long l;
        if (isEncrypting()) {
          l = getConsumedInputSizeBytes() - getProducedOutputSizeBytes() + paramInt + i;
        } else {
          l = getConsumedInputSizeBytes() - getProducedOutputSizeBytes() + paramInt - i;
        }
        if (l < 0L) {
          return 0;
        }
        if (l > 2147483647L) {
          return Integer.MAX_VALUE;
        }
        return (int)l;
      }
    }
  }
}
