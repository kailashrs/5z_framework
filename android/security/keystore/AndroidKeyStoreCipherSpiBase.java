package android.security.keystore;

import android.os.IBinder;
import android.security.KeyStore;
import android.security.KeyStoreException;
import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.OperationResult;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.AEADBadTagException;
import javax.crypto.BadPaddingException;
import javax.crypto.CipherSpi;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;
import libcore.util.EmptyArray;

abstract class AndroidKeyStoreCipherSpiBase
  extends CipherSpi
  implements KeyStoreCryptoOperation
{
  private KeyStoreCryptoOperationStreamer mAdditionalAuthenticationDataStreamer;
  private boolean mAdditionalAuthenticationDataStreamerClosed;
  private Exception mCachedException;
  private boolean mEncrypting;
  private AndroidKeyStoreKey mKey;
  private final KeyStore mKeyStore = KeyStore.getInstance();
  private int mKeymasterPurposeOverride = -1;
  private KeyStoreCryptoOperationStreamer mMainDataStreamer;
  private long mOperationHandle;
  private IBinder mOperationToken;
  private SecureRandom mRng;
  
  AndroidKeyStoreCipherSpiBase() {}
  
  private void ensureKeystoreOperationInitialized()
    throws InvalidKeyException, InvalidAlgorithmParameterException
  {
    if (mMainDataStreamer != null) {
      return;
    }
    if (mCachedException != null) {
      return;
    }
    if (mKey != null)
    {
      Object localObject1 = new KeymasterArguments();
      addAlgorithmSpecificParametersToBegin((KeymasterArguments)localObject1);
      Object localObject2 = KeyStoreCryptoOperationUtils.getRandomBytesToMixIntoKeystoreRng(mRng, getAdditionalEntropyAmountForBegin());
      int i;
      if (mKeymasterPurposeOverride != -1) {
        i = mKeymasterPurposeOverride;
      }
      for (;;)
      {
        break;
        if (mEncrypting) {
          i = 0;
        } else {
          i = 1;
        }
      }
      localObject1 = mKeyStore.begin(mKey.getAlias(), i, true, (KeymasterArguments)localObject1, (byte[])localObject2, mKey.getUid());
      if (localObject1 != null)
      {
        mOperationToken = token;
        mOperationHandle = operationHandle;
        localObject2 = KeyStoreCryptoOperationUtils.getExceptionForCipherInit(mKeyStore, mKey, resultCode);
        if (localObject2 != null)
        {
          if (!(localObject2 instanceof InvalidKeyException))
          {
            if ((localObject2 instanceof InvalidAlgorithmParameterException)) {
              throw ((InvalidAlgorithmParameterException)localObject2);
            }
            throw new ProviderException("Unexpected exception type", (Throwable)localObject2);
          }
          throw ((InvalidKeyException)localObject2);
        }
        if (mOperationToken != null)
        {
          if (mOperationHandle != 0L)
          {
            loadAlgorithmSpecificParametersFromBeginResult(outParams);
            mMainDataStreamer = createMainDataStreamer(mKeyStore, token);
            mAdditionalAuthenticationDataStreamer = createAdditionalAuthenticationDataStreamer(mKeyStore, token);
            mAdditionalAuthenticationDataStreamerClosed = false;
            return;
          }
          throw new ProviderException("Keystore returned invalid operation handle");
        }
        throw new ProviderException("Keystore returned null operation token");
      }
      throw new KeyStoreConnectException();
    }
    throw new IllegalStateException("Not initialized");
  }
  
  private void flushAAD()
    throws KeyStoreException
  {
    if ((mAdditionalAuthenticationDataStreamer != null) && (!mAdditionalAuthenticationDataStreamerClosed)) {
      try
      {
        byte[] arrayOfByte = mAdditionalAuthenticationDataStreamer.doFinal(EmptyArray.BYTE, 0, 0, null, null);
        mAdditionalAuthenticationDataStreamerClosed = true;
        if ((arrayOfByte != null) && (arrayOfByte.length > 0))
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("AAD update unexpectedly returned data: ");
          localStringBuilder.append(arrayOfByte.length);
          localStringBuilder.append(" bytes");
          throw new ProviderException(localStringBuilder.toString());
        }
      }
      finally
      {
        mAdditionalAuthenticationDataStreamerClosed = true;
      }
    }
  }
  
  private void init(int paramInt, Key paramKey, SecureRandom paramSecureRandom)
    throws InvalidKeyException
  {
    switch (paramInt)
    {
    default: 
      paramKey = new StringBuilder();
      paramKey.append("Unsupported opmode: ");
      paramKey.append(paramInt);
      throw new InvalidParameterException(paramKey.toString());
    case 2: 
    case 4: 
      mEncrypting = false;
      break;
    case 1: 
    case 3: 
      mEncrypting = true;
    }
    initKey(paramInt, paramKey);
    if (mKey != null)
    {
      mRng = paramSecureRandom;
      return;
    }
    throw new ProviderException("initKey did not initialize the key");
  }
  
  static String opmodeToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return String.valueOf(paramInt);
    case 4: 
      return "UNWRAP_MODE";
    case 3: 
      return "WRAP_MODE";
    case 2: 
      return "DECRYPT_MODE";
    }
    return "ENCRYPT_MODE";
  }
  
  protected abstract void addAlgorithmSpecificParametersToBegin(KeymasterArguments paramKeymasterArguments);
  
  protected KeyStoreCryptoOperationStreamer createAdditionalAuthenticationDataStreamer(KeyStore paramKeyStore, IBinder paramIBinder)
  {
    return null;
  }
  
  protected KeyStoreCryptoOperationStreamer createMainDataStreamer(KeyStore paramKeyStore, IBinder paramIBinder)
  {
    return new KeyStoreCryptoOperationChunkedStreamer(new KeyStoreCryptoOperationChunkedStreamer.MainDataStream(paramKeyStore, paramIBinder));
  }
  
  protected final int engineDoFinal(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2)
    throws ShortBufferException, IllegalBlockSizeException, BadPaddingException
  {
    if (paramByteBuffer1 != null)
    {
      if (paramByteBuffer2 != null)
      {
        int i = paramByteBuffer1.remaining();
        boolean bool = paramByteBuffer1.hasArray();
        int j = 0;
        byte[] arrayOfByte;
        if (bool)
        {
          arrayOfByte = engineDoFinal(paramByteBuffer1.array(), paramByteBuffer1.arrayOffset() + paramByteBuffer1.position(), i);
          paramByteBuffer1.position(paramByteBuffer1.position() + i);
          paramByteBuffer1 = arrayOfByte;
        }
        else
        {
          arrayOfByte = new byte[i];
          paramByteBuffer1.get(arrayOfByte);
          paramByteBuffer1 = engineDoFinal(arrayOfByte, 0, i);
        }
        if (paramByteBuffer1 != null) {
          j = paramByteBuffer1.length;
        }
        if (j > 0)
        {
          i = paramByteBuffer2.remaining();
          try
          {
            paramByteBuffer2.put(paramByteBuffer1);
          }
          catch (BufferOverflowException paramByteBuffer1)
          {
            paramByteBuffer1 = new StringBuilder();
            paramByteBuffer1.append("Output buffer too small. Produced: ");
            paramByteBuffer1.append(j);
            paramByteBuffer1.append(", available: ");
            paramByteBuffer1.append(i);
            throw new ShortBufferException(paramByteBuffer1.toString());
          }
        }
        return j;
      }
      throw new NullPointerException("output == null");
    }
    throw new NullPointerException("input == null");
  }
  
  protected final int engineDoFinal(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
    throws ShortBufferException, IllegalBlockSizeException, BadPaddingException
  {
    paramArrayOfByte1 = engineDoFinal(paramArrayOfByte1, paramInt1, paramInt2);
    if (paramArrayOfByte1 == null) {
      return 0;
    }
    paramInt1 = paramArrayOfByte2.length - paramInt3;
    if (paramArrayOfByte1.length <= paramInt1)
    {
      System.arraycopy(paramArrayOfByte1, 0, paramArrayOfByte2, paramInt3, paramArrayOfByte1.length);
      return paramArrayOfByte1.length;
    }
    paramArrayOfByte2 = new StringBuilder();
    paramArrayOfByte2.append("Output buffer too short. Produced: ");
    paramArrayOfByte2.append(paramArrayOfByte1.length);
    paramArrayOfByte2.append(", available: ");
    paramArrayOfByte2.append(paramInt1);
    throw new ShortBufferException(paramArrayOfByte2.toString());
  }
  
  protected final byte[] engineDoFinal(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IllegalBlockSizeException, BadPaddingException
  {
    if (mCachedException == null) {
      try
      {
        ensureKeystoreOperationInitialized();
        try
        {
          flushAAD();
          byte[] arrayOfByte = KeyStoreCryptoOperationUtils.getRandomBytesToMixIntoKeystoreRng(mRng, getAdditionalEntropyAmountForFinish());
          paramArrayOfByte = mMainDataStreamer.doFinal(paramArrayOfByte, paramInt1, paramInt2, null, arrayOfByte);
          resetWhilePreservingInitState();
          return paramArrayOfByte;
        }
        catch (KeyStoreException paramArrayOfByte)
        {
          paramInt1 = paramArrayOfByte.getErrorCode();
          if (paramInt1 != -38)
          {
            if (paramInt1 != -30)
            {
              if (paramInt1 != -21) {
                throw ((IllegalBlockSizeException)new IllegalBlockSizeException().initCause(paramArrayOfByte));
              }
              throw ((IllegalBlockSizeException)new IllegalBlockSizeException().initCause(paramArrayOfByte));
            }
            throw ((AEADBadTagException)new AEADBadTagException().initCause(paramArrayOfByte));
          }
          throw ((BadPaddingException)new BadPaddingException().initCause(paramArrayOfByte));
        }
        throw ((IllegalBlockSizeException)new IllegalBlockSizeException().initCause(mCachedException));
      }
      catch (InvalidKeyException|InvalidAlgorithmParameterException paramArrayOfByte)
      {
        throw ((IllegalBlockSizeException)new IllegalBlockSizeException().initCause(paramArrayOfByte));
      }
    }
  }
  
  protected final int engineGetKeySize(Key paramKey)
    throws InvalidKeyException
  {
    throw new UnsupportedOperationException();
  }
  
  protected abstract AlgorithmParameters engineGetParameters();
  
  protected final void engineInit(int paramInt, Key paramKey, AlgorithmParameters paramAlgorithmParameters, SecureRandom paramSecureRandom)
    throws InvalidKeyException, InvalidAlgorithmParameterException
  {
    resetAll();
    try
    {
      init(paramInt, paramKey, paramSecureRandom);
      initAlgorithmSpecificParameters(paramAlgorithmParameters);
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
  
  /* Error */
  protected final void engineInit(int paramInt, Key paramKey, SecureRandom paramSecureRandom)
    throws InvalidKeyException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 315	android/security/keystore/AndroidKeyStoreCipherSpiBase:resetAll	()V
    //   4: aload_0
    //   5: iload_1
    //   6: aload_2
    //   7: aload_3
    //   8: invokespecial 317	android/security/keystore/AndroidKeyStoreCipherSpiBase:init	(ILjava/security/Key;Ljava/security/SecureRandom;)V
    //   11: aload_0
    //   12: invokevirtual 323	android/security/keystore/AndroidKeyStoreCipherSpiBase:initAlgorithmSpecificParameters	()V
    //   15: aload_0
    //   16: invokespecial 281	android/security/keystore/AndroidKeyStoreCipherSpiBase:ensureKeystoreOperationInitialized	()V
    //   19: iconst_1
    //   20: ifne +7 -> 27
    //   23: aload_0
    //   24: invokevirtual 315	android/security/keystore/AndroidKeyStoreCipherSpiBase:resetAll	()V
    //   27: return
    //   28: astore_3
    //   29: new 44	java/security/InvalidKeyException
    //   32: astore_2
    //   33: aload_2
    //   34: aload_3
    //   35: invokespecial 326	java/security/InvalidKeyException:<init>	(Ljava/lang/Throwable;)V
    //   38: aload_2
    //   39: athrow
    //   40: astore_2
    //   41: iconst_0
    //   42: ifne +7 -> 49
    //   45: aload_0
    //   46: invokevirtual 315	android/security/keystore/AndroidKeyStoreCipherSpiBase:resetAll	()V
    //   49: aload_2
    //   50: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	51	0	this	AndroidKeyStoreCipherSpiBase
    //   0	51	1	paramInt	int
    //   0	51	2	paramKey	Key
    //   0	51	3	paramSecureRandom	SecureRandom
    // Exception table:
    //   from	to	target	type
    //   15	19	28	java/security/InvalidAlgorithmParameterException
    //   4	15	40	finally
    //   15	19	40	finally
    //   29	40	40	finally
  }
  
  protected final void engineInit(int paramInt, Key paramKey, AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
    throws InvalidKeyException, InvalidAlgorithmParameterException
  {
    resetAll();
    try
    {
      init(paramInt, paramKey, paramSecureRandom);
      initAlgorithmSpecificParameters(paramAlgorithmParameterSpec);
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
  
  protected final void engineSetMode(String paramString)
    throws NoSuchAlgorithmException
  {
    throw new UnsupportedOperationException();
  }
  
  protected final void engineSetPadding(String paramString)
    throws NoSuchPaddingException
  {
    throw new UnsupportedOperationException();
  }
  
  protected final Key engineUnwrap(byte[] paramArrayOfByte, String paramString, int paramInt)
    throws InvalidKeyException, NoSuchAlgorithmException
  {
    if (mKey != null)
    {
      if (!isEncrypting()) {
        if (paramArrayOfByte != null) {
          try
          {
            paramArrayOfByte = engineDoFinal(paramArrayOfByte, 0, paramArrayOfByte.length);
            Object localObject;
            switch (paramInt)
            {
            default: 
              paramArrayOfByte = new StringBuilder();
              paramArrayOfByte.append("Unsupported wrappedKeyType: ");
              paramArrayOfByte.append(paramInt);
              throw new InvalidParameterException(paramArrayOfByte.toString());
            case 3: 
              return new SecretKeySpec(paramArrayOfByte, paramString);
            case 2: 
              localObject = KeyFactory.getInstance(paramString);
              try
              {
                paramString = new java/security/spec/PKCS8EncodedKeySpec;
                paramString.<init>(paramArrayOfByte);
                paramArrayOfByte = ((KeyFactory)localObject).generatePrivate(paramString);
                return paramArrayOfByte;
              }
              catch (InvalidKeySpecException paramArrayOfByte)
              {
                throw new InvalidKeyException("Failed to create private key from its PKCS#8 encoded form", paramArrayOfByte);
              }
            }
            paramString = KeyFactory.getInstance(paramString);
            try
            {
              localObject = new java/security/spec/X509EncodedKeySpec;
              ((X509EncodedKeySpec)localObject).<init>(paramArrayOfByte);
              paramArrayOfByte = paramString.generatePublic((KeySpec)localObject);
              return paramArrayOfByte;
            }
            catch (InvalidKeySpecException paramArrayOfByte)
            {
              throw new InvalidKeyException("Failed to create public key from its X.509 encoded form", paramArrayOfByte);
            }
            throw new NullPointerException("wrappedKey == null");
          }
          catch (IllegalBlockSizeException|BadPaddingException paramArrayOfByte)
          {
            throw new InvalidKeyException("Failed to unwrap key", paramArrayOfByte);
          }
        }
      }
      throw new IllegalStateException("Cipher must be initialized in Cipher.WRAP_MODE to wrap keys");
    }
    throw new IllegalStateException("Not initilized");
  }
  
  protected final int engineUpdate(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2)
    throws ShortBufferException
  {
    if (paramByteBuffer1 != null)
    {
      if (paramByteBuffer2 != null)
      {
        int i = paramByteBuffer1.remaining();
        boolean bool = paramByteBuffer1.hasArray();
        int j = 0;
        byte[] arrayOfByte;
        if (bool)
        {
          arrayOfByte = engineUpdate(paramByteBuffer1.array(), paramByteBuffer1.arrayOffset() + paramByteBuffer1.position(), i);
          paramByteBuffer1.position(paramByteBuffer1.position() + i);
          paramByteBuffer1 = arrayOfByte;
        }
        else
        {
          arrayOfByte = new byte[i];
          paramByteBuffer1.get(arrayOfByte);
          paramByteBuffer1 = engineUpdate(arrayOfByte, 0, i);
        }
        if (paramByteBuffer1 != null) {
          j = paramByteBuffer1.length;
        }
        if (j > 0)
        {
          i = paramByteBuffer2.remaining();
          try
          {
            paramByteBuffer2.put(paramByteBuffer1);
          }
          catch (BufferOverflowException paramByteBuffer1)
          {
            paramByteBuffer1 = new StringBuilder();
            paramByteBuffer1.append("Output buffer too small. Produced: ");
            paramByteBuffer1.append(j);
            paramByteBuffer1.append(", available: ");
            paramByteBuffer1.append(i);
            throw new ShortBufferException(paramByteBuffer1.toString());
          }
        }
        return j;
      }
      throw new NullPointerException("output == null");
    }
    throw new NullPointerException("input == null");
  }
  
  protected final int engineUpdate(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
    throws ShortBufferException
  {
    paramArrayOfByte1 = engineUpdate(paramArrayOfByte1, paramInt1, paramInt2);
    if (paramArrayOfByte1 == null) {
      return 0;
    }
    paramInt1 = paramArrayOfByte2.length - paramInt3;
    if (paramArrayOfByte1.length <= paramInt1)
    {
      System.arraycopy(paramArrayOfByte1, 0, paramArrayOfByte2, paramInt3, paramArrayOfByte1.length);
      return paramArrayOfByte1.length;
    }
    paramArrayOfByte2 = new StringBuilder();
    paramArrayOfByte2.append("Output buffer too short. Produced: ");
    paramArrayOfByte2.append(paramArrayOfByte1.length);
    paramArrayOfByte2.append(", available: ");
    paramArrayOfByte2.append(paramInt1);
    throw new ShortBufferException(paramArrayOfByte2.toString());
  }
  
  protected final byte[] engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (mCachedException != null) {
      return null;
    }
    try
    {
      ensureKeystoreOperationInitialized();
      if (paramInt2 == 0) {
        return null;
      }
      try
      {
        flushAAD();
        paramArrayOfByte = mMainDataStreamer.update(paramArrayOfByte, paramInt1, paramInt2);
        if (paramArrayOfByte.length == 0) {
          return null;
        }
        return paramArrayOfByte;
      }
      catch (KeyStoreException paramArrayOfByte)
      {
        mCachedException = paramArrayOfByte;
        return null;
      }
      return null;
    }
    catch (InvalidKeyException|InvalidAlgorithmParameterException paramArrayOfByte)
    {
      mCachedException = paramArrayOfByte;
    }
  }
  
  protected final void engineUpdateAAD(ByteBuffer paramByteBuffer)
  {
    if (paramByteBuffer != null)
    {
      if (!paramByteBuffer.hasRemaining()) {
        return;
      }
      byte[] arrayOfByte;
      int i;
      int j;
      if (paramByteBuffer.hasArray())
      {
        arrayOfByte = paramByteBuffer.array();
        i = paramByteBuffer.arrayOffset() + paramByteBuffer.position();
        j = paramByteBuffer.remaining();
        paramByteBuffer.position(paramByteBuffer.limit());
        paramByteBuffer = arrayOfByte;
      }
      else
      {
        arrayOfByte = new byte[paramByteBuffer.remaining()];
        i = 0;
        j = arrayOfByte.length;
        paramByteBuffer.get(arrayOfByte);
        paramByteBuffer = arrayOfByte;
      }
      engineUpdateAAD(paramByteBuffer, i, j);
      return;
    }
    throw new IllegalArgumentException("src == null");
  }
  
  protected final void engineUpdateAAD(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (mCachedException != null) {
      return;
    }
    try
    {
      ensureKeystoreOperationInitialized();
      if (!mAdditionalAuthenticationDataStreamerClosed)
      {
        if (mAdditionalAuthenticationDataStreamer != null) {
          try
          {
            byte[] arrayOfByte = mAdditionalAuthenticationDataStreamer.update(paramArrayOfByte, paramInt1, paramInt2);
            if ((arrayOfByte != null) && (arrayOfByte.length > 0))
            {
              paramArrayOfByte = new StringBuilder();
              paramArrayOfByte.append("AAD update unexpectedly produced output: ");
              paramArrayOfByte.append(arrayOfByte.length);
              paramArrayOfByte.append(" bytes");
              throw new ProviderException(paramArrayOfByte.toString());
            }
            return;
          }
          catch (KeyStoreException paramArrayOfByte)
          {
            mCachedException = paramArrayOfByte;
            return;
          }
        }
        throw new IllegalStateException("This cipher does not support AAD");
      }
      throw new IllegalStateException("AAD can only be provided before Cipher.update is invoked");
    }
    catch (InvalidKeyException|InvalidAlgorithmParameterException paramArrayOfByte)
    {
      mCachedException = paramArrayOfByte;
    }
  }
  
  protected final byte[] engineWrap(Key paramKey)
    throws IllegalBlockSizeException, InvalidKeyException
  {
    if (mKey != null)
    {
      if (isEncrypting())
      {
        if (paramKey != null)
        {
          Object localObject1 = null;
          Object localObject2 = null;
          Object localObject3 = null;
          if ((paramKey instanceof SecretKey))
          {
            if ("RAW".equalsIgnoreCase(paramKey.getFormat())) {
              localObject3 = paramKey.getEncoded();
            }
            localObject1 = localObject3;
            if (localObject3 == null) {
              try
              {
                localObject1 = ((SecretKeySpec)SecretKeyFactory.getInstance(paramKey.getAlgorithm()).getKeySpec((SecretKey)paramKey, SecretKeySpec.class)).getEncoded();
              }
              catch (NoSuchAlgorithmException|InvalidKeySpecException paramKey)
              {
                throw new InvalidKeyException("Failed to wrap key because it does not export its key material", paramKey);
              }
            }
          }
          else if ((paramKey instanceof PrivateKey))
          {
            localObject3 = localObject1;
            if ("PKCS8".equalsIgnoreCase(paramKey.getFormat())) {
              localObject3 = paramKey.getEncoded();
            }
            localObject1 = localObject3;
            if (localObject3 == null) {
              try
              {
                localObject1 = ((PKCS8EncodedKeySpec)KeyFactory.getInstance(paramKey.getAlgorithm()).getKeySpec(paramKey, PKCS8EncodedKeySpec.class)).getEncoded();
              }
              catch (NoSuchAlgorithmException|InvalidKeySpecException paramKey)
              {
                throw new InvalidKeyException("Failed to wrap key because it does not export its key material", paramKey);
              }
            }
          }
          else
          {
            if (!(paramKey instanceof PublicKey)) {
              break label307;
            }
            localObject3 = localObject2;
            if ("X.509".equalsIgnoreCase(paramKey.getFormat())) {
              localObject3 = paramKey.getEncoded();
            }
            localObject1 = localObject3;
            if (localObject3 == null) {
              try
              {
                localObject1 = ((X509EncodedKeySpec)KeyFactory.getInstance(paramKey.getAlgorithm()).getKeySpec(paramKey, X509EncodedKeySpec.class)).getEncoded();
              }
              catch (NoSuchAlgorithmException|InvalidKeySpecException paramKey)
              {
                throw new InvalidKeyException("Failed to wrap key because it does not export its key material", paramKey);
              }
            }
          }
          if (localObject1 != null) {
            try
            {
              paramKey = engineDoFinal((byte[])localObject1, 0, localObject1.length);
              return paramKey;
            }
            catch (BadPaddingException paramKey)
            {
              throw ((IllegalBlockSizeException)new IllegalBlockSizeException().initCause(paramKey));
            }
          }
          throw new InvalidKeyException("Failed to wrap key because it does not export its key material");
          label307:
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Unsupported key type: ");
          ((StringBuilder)localObject1).append(paramKey.getClass().getName());
          throw new InvalidKeyException(((StringBuilder)localObject1).toString());
        }
        throw new NullPointerException("key == null");
      }
      throw new IllegalStateException("Cipher must be initialized in Cipher.WRAP_MODE to wrap keys");
    }
    throw new IllegalStateException("Not initilized");
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
  
  protected abstract int getAdditionalEntropyAmountForBegin();
  
  protected abstract int getAdditionalEntropyAmountForFinish();
  
  protected final long getConsumedInputSizeBytes()
  {
    if (mMainDataStreamer != null) {
      return mMainDataStreamer.getConsumedInputSizeBytes();
    }
    throw new IllegalStateException("Not initialized");
  }
  
  protected final KeyStore getKeyStore()
  {
    return mKeyStore;
  }
  
  protected final int getKeymasterPurposeOverride()
  {
    return mKeymasterPurposeOverride;
  }
  
  public final long getOperationHandle()
  {
    return mOperationHandle;
  }
  
  protected final long getProducedOutputSizeBytes()
  {
    if (mMainDataStreamer != null) {
      return mMainDataStreamer.getProducedOutputSizeBytes();
    }
    throw new IllegalStateException("Not initialized");
  }
  
  protected abstract void initAlgorithmSpecificParameters()
    throws InvalidKeyException;
  
  protected abstract void initAlgorithmSpecificParameters(AlgorithmParameters paramAlgorithmParameters)
    throws InvalidAlgorithmParameterException;
  
  protected abstract void initAlgorithmSpecificParameters(AlgorithmParameterSpec paramAlgorithmParameterSpec)
    throws InvalidAlgorithmParameterException;
  
  protected abstract void initKey(int paramInt, Key paramKey)
    throws InvalidKeyException;
  
  protected final boolean isEncrypting()
  {
    return mEncrypting;
  }
  
  protected abstract void loadAlgorithmSpecificParametersFromBeginResult(KeymasterArguments paramKeymasterArguments);
  
  protected void resetAll()
  {
    IBinder localIBinder = mOperationToken;
    if (localIBinder != null) {
      mKeyStore.abort(localIBinder);
    }
    mEncrypting = false;
    mKeymasterPurposeOverride = -1;
    mKey = null;
    mRng = null;
    mOperationToken = null;
    mOperationHandle = 0L;
    mMainDataStreamer = null;
    mAdditionalAuthenticationDataStreamer = null;
    mAdditionalAuthenticationDataStreamerClosed = false;
    mCachedException = null;
  }
  
  protected void resetWhilePreservingInitState()
  {
    IBinder localIBinder = mOperationToken;
    if (localIBinder != null) {
      mKeyStore.abort(localIBinder);
    }
    mOperationToken = null;
    mOperationHandle = 0L;
    mMainDataStreamer = null;
    mAdditionalAuthenticationDataStreamer = null;
    mAdditionalAuthenticationDataStreamerClosed = false;
    mCachedException = null;
  }
  
  protected final void setKey(AndroidKeyStoreKey paramAndroidKeyStoreKey)
  {
    mKey = paramAndroidKeyStoreKey;
  }
  
  protected final void setKeymasterPurposeOverride(int paramInt)
  {
    mKeymasterPurposeOverride = paramInt;
  }
}
