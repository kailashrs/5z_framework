package android.security.keystore;

import android.os.IBinder;
import android.security.KeyStore;
import android.security.KeyStoreException;
import android.security.keymaster.KeyCharacteristics;
import android.security.keymaster.KeymasterArguments;
import java.io.ByteArrayOutputStream;
import java.security.InvalidKeyException;
import libcore.util.EmptyArray;

abstract class AndroidKeyStoreECDSASignatureSpi
  extends AndroidKeyStoreSignatureSpiBase
{
  private int mGroupSizeBits = -1;
  private final int mKeymasterDigest;
  
  AndroidKeyStoreECDSASignatureSpi(int paramInt)
  {
    mKeymasterDigest = paramInt;
  }
  
  protected final void addAlgorithmSpecificParametersToBegin(KeymasterArguments paramKeymasterArguments)
  {
    paramKeymasterArguments.addEnum(268435458, 3);
    paramKeymasterArguments.addEnum(536870917, mKeymasterDigest);
  }
  
  protected final int getAdditionalEntropyAmountForSign()
  {
    return (mGroupSizeBits + 7) / 8;
  }
  
  protected final int getGroupSizeBits()
  {
    if (mGroupSizeBits != -1) {
      return mGroupSizeBits;
    }
    throw new IllegalStateException("Not initialized");
  }
  
  protected final void initKey(AndroidKeyStoreKey paramAndroidKeyStoreKey)
    throws InvalidKeyException
  {
    if ("EC".equalsIgnoreCase(paramAndroidKeyStoreKey.getAlgorithm()))
    {
      localObject = new KeyCharacteristics();
      int i = getKeyStore().getKeyCharacteristics(paramAndroidKeyStoreKey.getAlias(), null, null, paramAndroidKeyStoreKey.getUid(), (KeyCharacteristics)localObject);
      if (i == 1)
      {
        long l = ((KeyCharacteristics)localObject).getUnsignedInt(805306371, -1L);
        if (l != -1L)
        {
          if (l <= 2147483647L)
          {
            mGroupSizeBits = ((int)l);
            super.initKey(paramAndroidKeyStoreKey);
            return;
          }
          paramAndroidKeyStoreKey = new StringBuilder();
          paramAndroidKeyStoreKey.append("Key too large: ");
          paramAndroidKeyStoreKey.append(l);
          paramAndroidKeyStoreKey.append(" bits");
          throw new InvalidKeyException(paramAndroidKeyStoreKey.toString());
        }
        throw new InvalidKeyException("Size of key not known");
      }
      throw getKeyStore().getInvalidKeyException(paramAndroidKeyStoreKey.getAlias(), paramAndroidKeyStoreKey.getUid(), i);
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Unsupported key algorithm: ");
    ((StringBuilder)localObject).append(paramAndroidKeyStoreKey.getAlgorithm());
    ((StringBuilder)localObject).append(". Only");
    ((StringBuilder)localObject).append("EC");
    ((StringBuilder)localObject).append(" supported");
    throw new InvalidKeyException(((StringBuilder)localObject).toString());
  }
  
  protected final void resetAll()
  {
    mGroupSizeBits = -1;
    super.resetAll();
  }
  
  protected final void resetWhilePreservingInitState()
  {
    super.resetWhilePreservingInitState();
  }
  
  public static final class NONE
    extends AndroidKeyStoreECDSASignatureSpi
  {
    public NONE()
    {
      super();
    }
    
    protected KeyStoreCryptoOperationStreamer createMainDataStreamer(KeyStore paramKeyStore, IBinder paramIBinder)
    {
      return new TruncateToFieldSizeMessageStreamer(super.createMainDataStreamer(paramKeyStore, paramIBinder), getGroupSizeBits(), null);
    }
    
    private static class TruncateToFieldSizeMessageStreamer
      implements KeyStoreCryptoOperationStreamer
    {
      private long mConsumedInputSizeBytes;
      private final KeyStoreCryptoOperationStreamer mDelegate;
      private final int mGroupSizeBits;
      private final ByteArrayOutputStream mInputBuffer = new ByteArrayOutputStream();
      
      private TruncateToFieldSizeMessageStreamer(KeyStoreCryptoOperationStreamer paramKeyStoreCryptoOperationStreamer, int paramInt)
      {
        mDelegate = paramKeyStoreCryptoOperationStreamer;
        mGroupSizeBits = paramInt;
      }
      
      public byte[] doFinal(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
        throws KeyStoreException
      {
        if (paramInt2 > 0)
        {
          mConsumedInputSizeBytes += paramInt2;
          mInputBuffer.write(paramArrayOfByte1, paramInt1, paramInt2);
        }
        paramArrayOfByte1 = mInputBuffer.toByteArray();
        mInputBuffer.reset();
        return mDelegate.doFinal(paramArrayOfByte1, 0, Math.min(paramArrayOfByte1.length, (mGroupSizeBits + 7) / 8), paramArrayOfByte2, paramArrayOfByte3);
      }
      
      public long getConsumedInputSizeBytes()
      {
        return mConsumedInputSizeBytes;
      }
      
      public long getProducedOutputSizeBytes()
      {
        return mDelegate.getProducedOutputSizeBytes();
      }
      
      public byte[] update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
        throws KeyStoreException
      {
        if (paramInt2 > 0)
        {
          mInputBuffer.write(paramArrayOfByte, paramInt1, paramInt2);
          mConsumedInputSizeBytes += paramInt2;
        }
        return EmptyArray.BYTE;
      }
    }
  }
  
  public static final class SHA1
    extends AndroidKeyStoreECDSASignatureSpi
  {
    public SHA1()
    {
      super();
    }
  }
  
  public static final class SHA224
    extends AndroidKeyStoreECDSASignatureSpi
  {
    public SHA224()
    {
      super();
    }
  }
  
  public static final class SHA256
    extends AndroidKeyStoreECDSASignatureSpi
  {
    public SHA256()
    {
      super();
    }
  }
  
  public static final class SHA384
    extends AndroidKeyStoreECDSASignatureSpi
  {
    public SHA384()
    {
      super();
    }
  }
  
  public static final class SHA512
    extends AndroidKeyStoreECDSASignatureSpi
  {
    public SHA512()
    {
      super();
    }
  }
}
