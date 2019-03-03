package android.security.keystore;

import android.os.IBinder;
import android.security.KeyStore;
import android.security.KeyStoreException;
import android.security.keymaster.OperationResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.ProviderException;
import libcore.util.EmptyArray;

class KeyStoreCryptoOperationChunkedStreamer
  implements KeyStoreCryptoOperationStreamer
{
  private static final int DEFAULT_MAX_CHUNK_SIZE = 65536;
  private byte[] mBuffered = EmptyArray.BYTE;
  private int mBufferedLength;
  private int mBufferedOffset;
  private long mConsumedInputSizeBytes;
  private final Stream mKeyStoreStream;
  private final int mMaxChunkSize;
  private long mProducedOutputSizeBytes;
  
  public KeyStoreCryptoOperationChunkedStreamer(Stream paramStream)
  {
    this(paramStream, 65536);
  }
  
  public KeyStoreCryptoOperationChunkedStreamer(Stream paramStream, int paramInt)
  {
    mKeyStoreStream = paramStream;
    mMaxChunkSize = paramInt;
  }
  
  public byte[] doFinal(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
    throws KeyStoreException
  {
    if (paramInt2 == 0)
    {
      paramArrayOfByte1 = EmptyArray.BYTE;
      paramInt1 = 0;
    }
    paramArrayOfByte1 = ArrayUtils.concat(update(paramArrayOfByte1, paramInt1, paramInt2), flush());
    paramArrayOfByte2 = mKeyStoreStream.finish(paramArrayOfByte2, paramArrayOfByte3);
    if (paramArrayOfByte2 != null)
    {
      if (resultCode == 1)
      {
        mProducedOutputSizeBytes += output.length;
        return ArrayUtils.concat(paramArrayOfByte1, output);
      }
      throw KeyStore.getKeyStoreException(resultCode);
    }
    throw new KeyStoreConnectException();
  }
  
  public byte[] flush()
    throws KeyStoreException
  {
    if (mBufferedLength <= 0) {
      return EmptyArray.BYTE;
    }
    Object localObject1 = null;
    Object localObject3;
    Object localObject2;
    while (mBufferedLength > 0)
    {
      localObject3 = ArrayUtils.subarray(mBuffered, mBufferedOffset, mBufferedLength);
      OperationResult localOperationResult = mKeyStoreStream.update((byte[])localObject3);
      if (localOperationResult != null)
      {
        if (resultCode == 1)
        {
          if (inputConsumed > 0)
          {
            if (inputConsumed >= localObject3.length)
            {
              mBuffered = EmptyArray.BYTE;
              mBufferedOffset = 0;
              mBufferedLength = 0;
            }
            else
            {
              mBuffered = ((byte[])localObject3);
              mBufferedOffset = inputConsumed;
              mBufferedLength = (localObject3.length - inputConsumed);
            }
            if (inputConsumed <= localObject3.length)
            {
              localObject3 = localObject1;
              if (output != null)
              {
                localObject3 = localObject1;
                if (output.length > 0)
                {
                  localObject3 = localObject1;
                  if (localObject1 == null)
                  {
                    if (mBufferedLength == 0)
                    {
                      mProducedOutputSizeBytes += output.length;
                      return output;
                    }
                    localObject3 = new ByteArrayOutputStream();
                  }
                  try
                  {
                    ((ByteArrayOutputStream)localObject3).write(output);
                  }
                  catch (IOException localIOException)
                  {
                    throw new ProviderException("Failed to buffer output", localIOException);
                  }
                }
              }
              localObject2 = localObject3;
            }
            else
            {
              localObject2 = new StringBuilder();
              ((StringBuilder)localObject2).append("Keystore consumed more input than provided. Provided: ");
              ((StringBuilder)localObject2).append(localObject3.length);
              ((StringBuilder)localObject2).append(", consumed: ");
              ((StringBuilder)localObject2).append(inputConsumed);
              throw new KeyStoreException(64536, ((StringBuilder)localObject2).toString());
            }
          }
        }
        else {
          throw KeyStore.getKeyStoreException(resultCode);
        }
      }
      else {
        throw new KeyStoreConnectException();
      }
    }
    if (mBufferedLength > 0)
    {
      localObject3 = new StringBuilder();
      ((StringBuilder)localObject3).append("Keystore failed to consume last ");
      if (mBufferedLength != 1)
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append(mBufferedLength);
        ((StringBuilder)localObject2).append(" bytes");
        localObject2 = ((StringBuilder)localObject2).toString();
      }
      else
      {
        localObject2 = "byte";
      }
      ((StringBuilder)localObject3).append((String)localObject2);
      ((StringBuilder)localObject3).append(" of input");
      throw new KeyStoreException(-21, ((StringBuilder)localObject3).toString());
    }
    if (localObject2 != null) {
      localObject2 = ((ByteArrayOutputStream)localObject2).toByteArray();
    } else {
      localObject2 = EmptyArray.BYTE;
    }
    mProducedOutputSizeBytes += localObject2.length;
    return localObject2;
  }
  
  public long getConsumedInputSizeBytes()
  {
    return mConsumedInputSizeBytes;
  }
  
  public long getProducedOutputSizeBytes()
  {
    return mProducedOutputSizeBytes;
  }
  
  public byte[] update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws KeyStoreException
  {
    if (paramInt2 == 0) {
      return EmptyArray.BYTE;
    }
    Object localObject1 = null;
    int i = paramInt1;
    while (paramInt2 > 0)
    {
      Object localObject2;
      if (mBufferedLength + paramInt2 > mMaxChunkSize)
      {
        paramInt1 = mMaxChunkSize - mBufferedLength;
        localObject2 = ArrayUtils.concat(mBuffered, mBufferedOffset, mBufferedLength, paramArrayOfByte, i, paramInt1);
      }
      else if ((mBufferedLength == 0) && (i == 0) && (paramInt2 == paramArrayOfByte.length))
      {
        localObject2 = paramArrayOfByte;
        paramInt1 = paramArrayOfByte.length;
      }
      else
      {
        localObject2 = ArrayUtils.concat(mBuffered, mBufferedOffset, mBufferedLength, paramArrayOfByte, i, paramInt2);
        paramInt1 = paramInt2;
      }
      i += paramInt1;
      paramInt2 -= paramInt1;
      mConsumedInputSizeBytes += paramInt1;
      OperationResult localOperationResult = mKeyStoreStream.update((byte[])localObject2);
      if (localOperationResult != null)
      {
        if (resultCode == 1)
        {
          if (inputConsumed == localObject2.length)
          {
            mBuffered = EmptyArray.BYTE;
            mBufferedOffset = 0;
            mBufferedLength = 0;
          }
          else if (inputConsumed <= 0)
          {
            if (paramInt2 <= 0)
            {
              mBuffered = ((byte[])localObject2);
              mBufferedOffset = 0;
              mBufferedLength = localObject2.length;
            }
            else
            {
              paramArrayOfByte = new StringBuilder();
              paramArrayOfByte.append("Keystore consumed nothing from max-sized chunk: ");
              paramArrayOfByte.append(localObject2.length);
              paramArrayOfByte.append(" bytes");
              throw new KeyStoreException(64536, paramArrayOfByte.toString());
            }
          }
          else
          {
            if (inputConsumed >= localObject2.length) {
              break label446;
            }
            mBuffered = ((byte[])localObject2);
            mBufferedOffset = inputConsumed;
            mBufferedLength = (localObject2.length - inputConsumed);
          }
          localObject2 = localObject1;
          if (output != null)
          {
            localObject2 = localObject1;
            if (output.length > 0) {
              if (paramInt2 > 0)
              {
                localObject2 = localObject1;
                if (localObject1 == null)
                {
                  localObject2 = new ByteArrayOutputStream();
                  try
                  {
                    ((ByteArrayOutputStream)localObject2).write(output);
                  }
                  catch (IOException paramArrayOfByte)
                  {
                    throw new ProviderException("Failed to buffer output", paramArrayOfByte);
                  }
                }
              }
              else
              {
                if (localObject1 == null) {
                  paramArrayOfByte = output;
                }
                try
                {
                  localObject1.write(output);
                  paramArrayOfByte = localObject1.toByteArray();
                  mProducedOutputSizeBytes += paramArrayOfByte.length;
                  return paramArrayOfByte;
                }
                catch (IOException paramArrayOfByte)
                {
                  throw new ProviderException("Failed to buffer output", paramArrayOfByte);
                }
              }
            }
          }
          localObject1 = localObject2;
          continue;
          label446:
          paramArrayOfByte = new StringBuilder();
          paramArrayOfByte.append("Keystore consumed more input than provided. Provided: ");
          paramArrayOfByte.append(localObject2.length);
          paramArrayOfByte.append(", consumed: ");
          paramArrayOfByte.append(inputConsumed);
          throw new KeyStoreException(64536, paramArrayOfByte.toString());
        }
        else
        {
          throw KeyStore.getKeyStoreException(resultCode);
        }
      }
      else {
        throw new KeyStoreConnectException();
      }
    }
    if (localObject1 == null) {
      paramArrayOfByte = EmptyArray.BYTE;
    } else {
      paramArrayOfByte = localObject1.toByteArray();
    }
    mProducedOutputSizeBytes += paramArrayOfByte.length;
    return paramArrayOfByte;
  }
  
  public static class MainDataStream
    implements KeyStoreCryptoOperationChunkedStreamer.Stream
  {
    private final KeyStore mKeyStore;
    private final IBinder mOperationToken;
    
    public MainDataStream(KeyStore paramKeyStore, IBinder paramIBinder)
    {
      mKeyStore = paramKeyStore;
      mOperationToken = paramIBinder;
    }
    
    public OperationResult finish(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    {
      return mKeyStore.finish(mOperationToken, null, paramArrayOfByte1, paramArrayOfByte2);
    }
    
    public OperationResult update(byte[] paramArrayOfByte)
    {
      return mKeyStore.update(mOperationToken, null, paramArrayOfByte);
    }
  }
  
  static abstract interface Stream
  {
    public abstract OperationResult finish(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
    
    public abstract OperationResult update(byte[] paramArrayOfByte);
  }
}
