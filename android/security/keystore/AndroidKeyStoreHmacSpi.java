package android.security.keystore;

import android.os.IBinder;
import android.security.KeyStore;
import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.OperationResult;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.ProviderException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.MacSpi;

public abstract class AndroidKeyStoreHmacSpi
  extends MacSpi
  implements KeyStoreCryptoOperation
{
  private KeyStoreCryptoOperationChunkedStreamer mChunkedStreamer;
  private AndroidKeyStoreSecretKey mKey;
  private final KeyStore mKeyStore = KeyStore.getInstance();
  private final int mKeymasterDigest;
  private final int mMacSizeBits;
  private long mOperationHandle;
  private IBinder mOperationToken;
  
  protected AndroidKeyStoreHmacSpi(int paramInt)
  {
    mKeymasterDigest = paramInt;
    mMacSizeBits = KeymasterUtils.getDigestOutputSizeBits(paramInt);
  }
  
  private void ensureKeystoreOperationInitialized()
    throws InvalidKeyException
  {
    if (mChunkedStreamer != null) {
      return;
    }
    if (mKey != null)
    {
      Object localObject = new KeymasterArguments();
      ((KeymasterArguments)localObject).addEnum(268435458, 128);
      ((KeymasterArguments)localObject).addEnum(536870917, mKeymasterDigest);
      ((KeymasterArguments)localObject).addUnsignedInt(805307371, mMacSizeBits);
      localObject = mKeyStore.begin(mKey.getAlias(), 2, true, (KeymasterArguments)localObject, null, mKey.getUid());
      if (localObject != null)
      {
        mOperationToken = token;
        mOperationHandle = operationHandle;
        localObject = KeyStoreCryptoOperationUtils.getInvalidKeyExceptionForInit(mKeyStore, mKey, resultCode);
        if (localObject == null)
        {
          if (mOperationToken != null)
          {
            if (mOperationHandle != 0L)
            {
              mChunkedStreamer = new KeyStoreCryptoOperationChunkedStreamer(new KeyStoreCryptoOperationChunkedStreamer.MainDataStream(mKeyStore, mOperationToken));
              return;
            }
            throw new ProviderException("Keystore returned invalid operation handle");
          }
          throw new ProviderException("Keystore returned null operation token");
        }
        throw ((Throwable)localObject);
      }
      throw new KeyStoreConnectException();
    }
    throw new IllegalStateException("Not initialized");
  }
  
  private void init(Key paramKey, AlgorithmParameterSpec paramAlgorithmParameterSpec)
    throws InvalidKeyException, InvalidAlgorithmParameterException
  {
    if (paramKey != null)
    {
      if ((paramKey instanceof AndroidKeyStoreSecretKey))
      {
        mKey = ((AndroidKeyStoreSecretKey)paramKey);
        if (paramAlgorithmParameterSpec == null) {
          return;
        }
        paramKey = new StringBuilder();
        paramKey.append("Unsupported algorithm parameters: ");
        paramKey.append(paramAlgorithmParameterSpec);
        throw new InvalidAlgorithmParameterException(paramKey.toString());
      }
      paramAlgorithmParameterSpec = new StringBuilder();
      paramAlgorithmParameterSpec.append("Only Android KeyStore secret keys supported. Key: ");
      paramAlgorithmParameterSpec.append(paramKey);
      throw new InvalidKeyException(paramAlgorithmParameterSpec.toString());
    }
    throw new InvalidKeyException("key == null");
  }
  
  private void resetAll()
  {
    mKey = null;
    IBinder localIBinder = mOperationToken;
    if (localIBinder != null) {
      mKeyStore.abort(localIBinder);
    }
    mOperationToken = null;
    mOperationHandle = 0L;
    mChunkedStreamer = null;
  }
  
  private void resetWhilePreservingInitState()
  {
    IBinder localIBinder = mOperationToken;
    if (localIBinder != null) {
      mKeyStore.abort(localIBinder);
    }
    mOperationToken = null;
    mOperationHandle = 0L;
    mChunkedStreamer = null;
  }
  
  /* Error */
  protected byte[] engineDoFinal()
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 179	android/security/keystore/AndroidKeyStoreHmacSpi:ensureKeystoreOperationInitialized	()V
    //   4: aload_0
    //   5: getfield 63	android/security/keystore/AndroidKeyStoreHmacSpi:mChunkedStreamer	Landroid/security/keystore/KeyStoreCryptoOperationChunkedStreamer;
    //   8: aconst_null
    //   9: iconst_0
    //   10: iconst_0
    //   11: aconst_null
    //   12: aconst_null
    //   13: invokevirtual 183	android/security/keystore/KeyStoreCryptoOperationChunkedStreamer:doFinal	([BII[B[B)[B
    //   16: astore_1
    //   17: aload_0
    //   18: invokespecial 185	android/security/keystore/AndroidKeyStoreHmacSpi:resetWhilePreservingInitState	()V
    //   21: aload_1
    //   22: areturn
    //   23: astore_1
    //   24: new 126	java/security/ProviderException
    //   27: dup
    //   28: ldc -69
    //   30: aload_1
    //   31: invokespecial 190	java/security/ProviderException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   34: athrow
    //   35: astore_1
    //   36: new 126	java/security/ProviderException
    //   39: dup
    //   40: ldc -64
    //   42: aload_1
    //   43: invokespecial 190	java/security/ProviderException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   46: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	47	0	this	AndroidKeyStoreHmacSpi
    //   16	6	1	arrayOfByte	byte[]
    //   23	8	1	localKeyStoreException	android.security.KeyStoreException
    //   35	8	1	localInvalidKeyException	InvalidKeyException
    // Exception table:
    //   from	to	target	type
    //   4	17	23	android/security/KeyStoreException
    //   0	4	35	java/security/InvalidKeyException
  }
  
  protected int engineGetMacLength()
  {
    return (mMacSizeBits + 7) / 8;
  }
  
  protected void engineInit(Key paramKey, AlgorithmParameterSpec paramAlgorithmParameterSpec)
    throws InvalidKeyException, InvalidAlgorithmParameterException
  {
    resetAll();
    try
    {
      init(paramKey, paramAlgorithmParameterSpec);
      ensureKeystoreOperationInitialized();
      if (1 == 0) {
        resetAll();
      }
      return;
    }
    finally
    {
      if (0 == 0) {
        resetAll();
      }
    }
  }
  
  protected void engineReset()
  {
    resetWhilePreservingInitState();
  }
  
  protected void engineUpdate(byte paramByte)
  {
    engineUpdate(new byte[] { paramByte }, 0, 1);
  }
  
  /* Error */
  protected void engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 179	android/security/keystore/AndroidKeyStoreHmacSpi:ensureKeystoreOperationInitialized	()V
    //   4: aload_0
    //   5: getfield 63	android/security/keystore/AndroidKeyStoreHmacSpi:mChunkedStreamer	Landroid/security/keystore/KeyStoreCryptoOperationChunkedStreamer;
    //   8: aload_1
    //   9: iload_2
    //   10: iload_3
    //   11: invokevirtual 208	android/security/keystore/KeyStoreCryptoOperationChunkedStreamer:update	([BII)[B
    //   14: astore_1
    //   15: aload_1
    //   16: ifnull +21 -> 37
    //   19: aload_1
    //   20: arraylength
    //   21: ifne +6 -> 27
    //   24: goto +13 -> 37
    //   27: new 126	java/security/ProviderException
    //   30: dup
    //   31: ldc -46
    //   33: invokespecial 131	java/security/ProviderException:<init>	(Ljava/lang/String;)V
    //   36: athrow
    //   37: return
    //   38: astore_1
    //   39: new 126	java/security/ProviderException
    //   42: dup
    //   43: ldc -69
    //   45: aload_1
    //   46: invokespecial 190	java/security/ProviderException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   49: athrow
    //   50: astore_1
    //   51: new 126	java/security/ProviderException
    //   54: dup
    //   55: ldc -64
    //   57: aload_1
    //   58: invokespecial 190	java/security/ProviderException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   61: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	62	0	this	AndroidKeyStoreHmacSpi
    //   0	62	1	paramArrayOfByte	byte[]
    //   0	62	2	paramInt1	int
    //   0	62	3	paramInt2	int
    // Exception table:
    //   from	to	target	type
    //   4	15	38	android/security/KeyStoreException
    //   0	4	50	java/security/InvalidKeyException
  }
  
  public void finalize()
    throws Throwable
  {
    try
    {
      IBinder localIBinder = mOperationToken;
      if (localIBinder != null) {
        mKeyStore.abort(localIBinder);
      }
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public long getOperationHandle()
  {
    return mOperationHandle;
  }
  
  public static class HmacSHA1
    extends AndroidKeyStoreHmacSpi
  {
    public HmacSHA1()
    {
      super();
    }
  }
  
  public static class HmacSHA224
    extends AndroidKeyStoreHmacSpi
  {
    public HmacSHA224()
    {
      super();
    }
  }
  
  public static class HmacSHA256
    extends AndroidKeyStoreHmacSpi
  {
    public HmacSHA256()
    {
      super();
    }
  }
  
  public static class HmacSHA384
    extends AndroidKeyStoreHmacSpi
  {
    public HmacSHA384()
    {
      super();
    }
  }
  
  public static class HmacSHA512
    extends AndroidKeyStoreHmacSpi
  {
    public HmacSHA512()
    {
      super();
    }
  }
}
