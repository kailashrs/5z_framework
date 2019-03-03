package android.security.keystore;

import android.security.KeyStoreException;

abstract interface KeyStoreCryptoOperationStreamer
{
  public abstract byte[] doFinal(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
    throws KeyStoreException;
  
  public abstract long getConsumedInputSizeBytes();
  
  public abstract long getProducedOutputSizeBytes();
  
  public abstract byte[] update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws KeyStoreException;
}
