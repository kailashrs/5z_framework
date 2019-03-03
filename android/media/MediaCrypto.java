package android.media;

import java.util.UUID;

public final class MediaCrypto
{
  private long mNativeContext;
  
  static
  {
    System.loadLibrary("media_jni");
    native_init();
  }
  
  public MediaCrypto(UUID paramUUID, byte[] paramArrayOfByte)
    throws MediaCryptoException
  {
    native_setup(getByteArrayFromUUID(paramUUID), paramArrayOfByte);
  }
  
  private static final byte[] getByteArrayFromUUID(UUID paramUUID)
  {
    long l1 = paramUUID.getMostSignificantBits();
    long l2 = paramUUID.getLeastSignificantBits();
    paramUUID = new byte[16];
    for (int i = 0; i < 8; i++)
    {
      paramUUID[i] = ((byte)(byte)(int)(l1 >>> (7 - i) * 8));
      paramUUID[(8 + i)] = ((byte)(byte)(int)(l2 >>> 8 * (7 - i)));
    }
    return paramUUID;
  }
  
  public static final boolean isCryptoSchemeSupported(UUID paramUUID)
  {
    return isCryptoSchemeSupportedNative(getByteArrayFromUUID(paramUUID));
  }
  
  private static final native boolean isCryptoSchemeSupportedNative(byte[] paramArrayOfByte);
  
  private final native void native_finalize();
  
  private static final native void native_init();
  
  private final native void native_setup(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws MediaCryptoException;
  
  protected void finalize()
  {
    native_finalize();
  }
  
  public final native void release();
  
  public final native boolean requiresSecureDecoderComponent(String paramString);
  
  public final native void setMediaDrmSession(byte[] paramArrayOfByte)
    throws MediaCryptoException;
}
