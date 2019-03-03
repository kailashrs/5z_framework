package android.security.keystore;

import android.os.IBinder;
import android.security.KeyStore;
import android.security.KeyStoreException;
import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.OperationResult;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.SignatureSpi;
import libcore.util.EmptyArray;

abstract class AndroidKeyStoreSignatureSpiBase
  extends SignatureSpi
  implements KeyStoreCryptoOperation
{
  private Exception mCachedException;
  private AndroidKeyStoreKey mKey;
  private final KeyStore mKeyStore = KeyStore.getInstance();
  private KeyStoreCryptoOperationStreamer mMessageStreamer;
  private long mOperationHandle;
  private IBinder mOperationToken;
  private boolean mSigning;
  
  AndroidKeyStoreSignatureSpiBase() {}
  
  private void ensureKeystoreOperationInitialized()
    throws InvalidKeyException
  {
    if (mMessageStreamer != null) {
      return;
    }
    if (mCachedException != null) {
      return;
    }
    if (mKey != null)
    {
      Object localObject1 = new KeymasterArguments();
      addAlgorithmSpecificParametersToBegin((KeymasterArguments)localObject1);
      KeyStore localKeyStore = mKeyStore;
      Object localObject2 = mKey.getAlias();
      int i;
      if (mSigning) {
        i = 2;
      } else {
        i = 3;
      }
      localObject1 = localKeyStore.begin((String)localObject2, i, true, (KeymasterArguments)localObject1, null, mKey.getUid());
      if (localObject1 != null)
      {
        mOperationToken = token;
        mOperationHandle = operationHandle;
        localObject2 = KeyStoreCryptoOperationUtils.getInvalidKeyExceptionForInit(mKeyStore, mKey, resultCode);
        if (localObject2 == null)
        {
          if (mOperationToken != null)
          {
            if (mOperationHandle != 0L)
            {
              mMessageStreamer = createMainDataStreamer(mKeyStore, token);
              return;
            }
            throw new ProviderException("Keystore returned invalid operation handle");
          }
          throw new ProviderException("Keystore returned null operation token");
        }
        throw ((Throwable)localObject2);
      }
      throw new KeyStoreConnectException();
    }
    throw new IllegalStateException("Not initialized");
  }
  
  protected abstract void addAlgorithmSpecificParametersToBegin(KeymasterArguments paramKeymasterArguments);
  
  protected KeyStoreCryptoOperationStreamer createMainDataStreamer(KeyStore paramKeyStore, IBinder paramIBinder)
  {
    return new KeyStoreCryptoOperationChunkedStreamer(new KeyStoreCryptoOperationChunkedStreamer.MainDataStream(paramKeyStore, paramIBinder));
  }
  
  @Deprecated
  protected final Object engineGetParameter(String paramString)
    throws InvalidParameterException
  {
    throw new InvalidParameterException();
  }
  
  protected final void engineInitSign(PrivateKey paramPrivateKey)
    throws InvalidKeyException
  {
    engineInitSign(paramPrivateKey, null);
  }
  
  protected final void engineInitSign(PrivateKey paramPrivateKey, SecureRandom paramSecureRandom)
    throws InvalidKeyException
  {
    resetAll();
    if (paramPrivateKey != null) {}
    try
    {
      if ((paramPrivateKey instanceof AndroidKeyStorePrivateKey))
      {
        paramPrivateKey = (AndroidKeyStoreKey)paramPrivateKey;
        mSigning = true;
        initKey(paramPrivateKey);
        appRandom = paramSecureRandom;
        ensureKeystoreOperationInitialized();
        if (1 == 0) {
          resetAll();
        }
        return;
      }
      paramSecureRandom = new java/security/InvalidKeyException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Unsupported private key type: ");
      localStringBuilder.append(paramPrivateKey);
      paramSecureRandom.<init>(localStringBuilder.toString());
      throw paramSecureRandom;
    }
    finally
    {
      if (0 != 0) {
        break label104;
      }
      resetAll();
    }
    paramPrivateKey = new java/security/InvalidKeyException;
    paramPrivateKey.<init>("Unsupported key: null");
    throw paramPrivateKey;
  }
  
  protected final void engineInitVerify(PublicKey paramPublicKey)
    throws InvalidKeyException
  {
    resetAll();
    if (paramPublicKey != null) {}
    try
    {
      if ((paramPublicKey instanceof AndroidKeyStorePublicKey))
      {
        paramPublicKey = (AndroidKeyStorePublicKey)paramPublicKey;
        mSigning = false;
        initKey(paramPublicKey);
        appRandom = null;
        ensureKeystoreOperationInitialized();
        if (1 == 0) {
          resetAll();
        }
        return;
      }
      InvalidKeyException localInvalidKeyException = new java/security/InvalidKeyException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Unsupported public key type: ");
      localStringBuilder.append(paramPublicKey);
      localInvalidKeyException.<init>(localStringBuilder.toString());
      throw localInvalidKeyException;
    }
    finally
    {
      if (0 != 0) {
        break label104;
      }
      resetAll();
    }
    paramPublicKey = new java/security/InvalidKeyException;
    paramPublicKey.<init>("Unsupported key: null");
    throw paramPublicKey;
  }
  
  @Deprecated
  protected final void engineSetParameter(String paramString, Object paramObject)
    throws InvalidParameterException
  {
    throw new InvalidParameterException();
  }
  
  protected final int engineSign(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws SignatureException
  {
    return super.engineSign(paramArrayOfByte, paramInt1, paramInt2);
  }
  
  protected final byte[] engineSign()
    throws SignatureException
  {
    if (mCachedException == null) {
      try
      {
        ensureKeystoreOperationInitialized();
        byte[] arrayOfByte = KeyStoreCryptoOperationUtils.getRandomBytesToMixIntoKeystoreRng(appRandom, getAdditionalEntropyAmountForSign());
        arrayOfByte = mMessageStreamer.doFinal(EmptyArray.BYTE, 0, 0, null, arrayOfByte);
        resetWhilePreservingInitState();
        return arrayOfByte;
      }
      catch (InvalidKeyException|KeyStoreException localInvalidKeyException)
      {
        throw new SignatureException(localInvalidKeyException);
      }
    }
    throw new SignatureException(mCachedException);
  }
  
  protected final void engineUpdate(byte paramByte)
    throws SignatureException
  {
    engineUpdate(new byte[] { paramByte }, 0, 1);
  }
  
  protected final void engineUpdate(ByteBuffer paramByteBuffer)
  {
    int i = paramByteBuffer.remaining();
    byte[] arrayOfByte;
    int j;
    if (paramByteBuffer.hasArray())
    {
      arrayOfByte = paramByteBuffer.array();
      j = paramByteBuffer.arrayOffset() + paramByteBuffer.position();
      paramByteBuffer.position(paramByteBuffer.limit());
      paramByteBuffer = arrayOfByte;
    }
    else
    {
      arrayOfByte = new byte[i];
      j = 0;
      paramByteBuffer.get(arrayOfByte);
      paramByteBuffer = arrayOfByte;
    }
    try
    {
      engineUpdate(paramByteBuffer, j, i);
    }
    catch (SignatureException paramByteBuffer)
    {
      mCachedException = paramByteBuffer;
    }
  }
  
  protected final void engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws SignatureException
  {
    if (mCachedException == null) {
      try
      {
        ensureKeystoreOperationInitialized();
        if (paramInt2 == 0) {
          return;
        }
        try
        {
          paramArrayOfByte = mMessageStreamer.update(paramArrayOfByte, paramInt1, paramInt2);
          if (paramArrayOfByte.length == 0) {
            return;
          }
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Update operation unexpectedly produced output: ");
          localStringBuilder.append(paramArrayOfByte.length);
          localStringBuilder.append(" bytes");
          throw new ProviderException(localStringBuilder.toString());
        }
        catch (KeyStoreException paramArrayOfByte)
        {
          throw new SignatureException(paramArrayOfByte);
        }
        throw new SignatureException(mCachedException);
      }
      catch (InvalidKeyException paramArrayOfByte)
      {
        throw new SignatureException(paramArrayOfByte);
      }
    }
  }
  
  protected final boolean engineVerify(byte[] paramArrayOfByte)
    throws SignatureException
  {
    if (mCachedException == null) {
      try
      {
        ensureKeystoreOperationInitialized();
        try
        {
          byte[] arrayOfByte = mMessageStreamer.doFinal(EmptyArray.BYTE, 0, 0, paramArrayOfByte, null);
          if (arrayOfByte.length == 0)
          {
            bool = true;
          }
          else
          {
            ProviderException localProviderException = new java/security/ProviderException;
            paramArrayOfByte = new java/lang/StringBuilder;
            paramArrayOfByte.<init>();
            paramArrayOfByte.append("Signature verification unexpected produced output: ");
            paramArrayOfByte.append(arrayOfByte.length);
            paramArrayOfByte.append(" bytes");
            localProviderException.<init>(paramArrayOfByte.toString());
            throw localProviderException;
          }
        }
        catch (KeyStoreException paramArrayOfByte)
        {
          boolean bool;
          if (paramArrayOfByte.getErrorCode() == -30)
          {
            bool = false;
            resetWhilePreservingInitState();
            return bool;
          }
          throw new SignatureException(paramArrayOfByte);
        }
        throw new SignatureException(mCachedException);
      }
      catch (InvalidKeyException paramArrayOfByte)
      {
        throw new SignatureException(paramArrayOfByte);
      }
    }
  }
  
  protected final boolean engineVerify(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws SignatureException
  {
    return engineVerify(ArrayUtils.subarray(paramArrayOfByte, paramInt1, paramInt2));
  }
  
  protected abstract int getAdditionalEntropyAmountForSign();
  
  protected final KeyStore getKeyStore()
  {
    return mKeyStore;
  }
  
  public final long getOperationHandle()
  {
    return mOperationHandle;
  }
  
  protected void initKey(AndroidKeyStoreKey paramAndroidKeyStoreKey)
    throws InvalidKeyException
  {
    mKey = paramAndroidKeyStoreKey;
  }
  
  protected final boolean isSigning()
  {
    return mSigning;
  }
  
  protected void resetAll()
  {
    IBinder localIBinder = mOperationToken;
    if (localIBinder != null)
    {
      mOperationToken = null;
      mKeyStore.abort(localIBinder);
    }
    mSigning = false;
    mKey = null;
    appRandom = null;
    mOperationToken = null;
    mOperationHandle = 0L;
    mMessageStreamer = null;
    mCachedException = null;
  }
  
  protected void resetWhilePreservingInitState()
  {
    IBinder localIBinder = mOperationToken;
    if (localIBinder != null)
    {
      mOperationToken = null;
      mKeyStore.abort(localIBinder);
    }
    mOperationHandle = 0L;
    mMessageStreamer = null;
    mCachedException = null;
  }
}
