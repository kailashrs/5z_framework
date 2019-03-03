package android.media;

import android.hardware.cas.V1_0.IDescramblerBase;
import android.os.IHwBinder;
import android.os.RemoteException;
import android.os.ServiceSpecificException;
import java.nio.ByteBuffer;

public final class MediaDescrambler
  implements AutoCloseable
{
  public static final byte SCRAMBLE_CONTROL_EVEN_KEY = 2;
  public static final byte SCRAMBLE_CONTROL_ODD_KEY = 3;
  public static final byte SCRAMBLE_CONTROL_RESERVED = 1;
  public static final byte SCRAMBLE_CONTROL_UNSCRAMBLED = 0;
  public static final byte SCRAMBLE_FLAG_PES_HEADER = 1;
  private static final String TAG = "MediaDescrambler";
  private IDescramblerBase mIDescrambler;
  private long mNativeContext;
  
  static
  {
    System.loadLibrary("media_jni");
    native_init();
  }
  
  /* Error */
  public MediaDescrambler(int paramInt)
    throws MediaCasException.UnsupportedCasException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 46	java/lang/Object:<init>	()V
    //   4: aload_0
    //   5: invokestatic 52	android/media/MediaCas:getService	()Landroid/hardware/cas/V1_0/IMediaCasService;
    //   8: iload_1
    //   9: invokeinterface 58 2 0
    //   14: putfield 60	android/media/MediaDescrambler:mIDescrambler	Landroid/hardware/cas/V1_0/IDescramblerBase;
    //   17: aload_0
    //   18: getfield 60	android/media/MediaDescrambler:mIDescrambler	Landroid/hardware/cas/V1_0/IDescramblerBase;
    //   21: ifnull +6 -> 27
    //   24: goto +84 -> 108
    //   27: new 62	java/lang/StringBuilder
    //   30: dup
    //   31: invokespecial 63	java/lang/StringBuilder:<init>	()V
    //   34: astore_2
    //   35: aload_2
    //   36: ldc 65
    //   38: invokevirtual 69	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   41: pop
    //   42: aload_2
    //   43: iload_1
    //   44: invokevirtual 72	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   47: pop
    //   48: new 42	android/media/MediaCasException$UnsupportedCasException
    //   51: dup
    //   52: aload_2
    //   53: invokevirtual 76	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   56: invokespecial 78	android/media/MediaCasException$UnsupportedCasException:<init>	(Ljava/lang/String;)V
    //   59: athrow
    //   60: astore_2
    //   61: goto +94 -> 155
    //   64: astore_3
    //   65: new 62	java/lang/StringBuilder
    //   68: astore_2
    //   69: aload_2
    //   70: invokespecial 63	java/lang/StringBuilder:<init>	()V
    //   73: aload_2
    //   74: ldc 80
    //   76: invokevirtual 69	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   79: pop
    //   80: aload_2
    //   81: aload_3
    //   82: invokevirtual 83	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   85: pop
    //   86: ldc 20
    //   88: aload_2
    //   89: invokevirtual 76	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   92: invokestatic 89	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   95: pop
    //   96: aload_0
    //   97: aconst_null
    //   98: putfield 60	android/media/MediaDescrambler:mIDescrambler	Landroid/hardware/cas/V1_0/IDescramblerBase;
    //   101: aload_0
    //   102: getfield 60	android/media/MediaDescrambler:mIDescrambler	Landroid/hardware/cas/V1_0/IDescramblerBase;
    //   105: ifnull +17 -> 122
    //   108: aload_0
    //   109: aload_0
    //   110: getfield 60	android/media/MediaDescrambler:mIDescrambler	Landroid/hardware/cas/V1_0/IDescramblerBase;
    //   113: invokeinterface 95 1 0
    //   118: invokespecial 99	android/media/MediaDescrambler:native_setup	(Landroid/os/IHwBinder;)V
    //   121: return
    //   122: new 62	java/lang/StringBuilder
    //   125: dup
    //   126: invokespecial 63	java/lang/StringBuilder:<init>	()V
    //   129: astore_2
    //   130: aload_2
    //   131: ldc 65
    //   133: invokevirtual 69	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   136: pop
    //   137: aload_2
    //   138: iload_1
    //   139: invokevirtual 72	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   142: pop
    //   143: new 42	android/media/MediaCasException$UnsupportedCasException
    //   146: dup
    //   147: aload_2
    //   148: invokevirtual 76	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   151: invokespecial 78	android/media/MediaCasException$UnsupportedCasException:<init>	(Ljava/lang/String;)V
    //   154: athrow
    //   155: aload_0
    //   156: getfield 60	android/media/MediaDescrambler:mIDescrambler	Landroid/hardware/cas/V1_0/IDescramblerBase;
    //   159: ifnonnull +36 -> 195
    //   162: new 62	java/lang/StringBuilder
    //   165: dup
    //   166: invokespecial 63	java/lang/StringBuilder:<init>	()V
    //   169: astore_2
    //   170: aload_2
    //   171: ldc 65
    //   173: invokevirtual 69	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   176: pop
    //   177: aload_2
    //   178: iload_1
    //   179: invokevirtual 72	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   182: pop
    //   183: new 42	android/media/MediaCasException$UnsupportedCasException
    //   186: dup
    //   187: aload_2
    //   188: invokevirtual 76	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   191: invokespecial 78	android/media/MediaCasException$UnsupportedCasException:<init>	(Ljava/lang/String;)V
    //   194: athrow
    //   195: aload_2
    //   196: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	197	0	this	MediaDescrambler
    //   0	197	1	paramInt	int
    //   34	19	2	localStringBuilder1	StringBuilder
    //   60	1	2	localObject	Object
    //   68	128	2	localStringBuilder2	StringBuilder
    //   64	18	3	localException	Exception
    // Exception table:
    //   from	to	target	type
    //   4	17	60	finally
    //   65	101	60	finally
    //   4	17	64	java/lang/Exception
  }
  
  private final void cleanupAndRethrowIllegalState()
  {
    mIDescrambler = null;
    throw new IllegalStateException();
  }
  
  private final native int native_descramble(byte paramByte1, byte paramByte2, int paramInt1, int[] paramArrayOfInt1, int[] paramArrayOfInt2, ByteBuffer paramByteBuffer1, int paramInt2, int paramInt3, ByteBuffer paramByteBuffer2, int paramInt4, int paramInt5)
    throws RemoteException;
  
  private static final native void native_init();
  
  private final native void native_release();
  
  private final native void native_setup(IHwBinder paramIHwBinder);
  
  private final void validateInternalStates()
  {
    if (mIDescrambler != null) {
      return;
    }
    throw new IllegalStateException();
  }
  
  /* Error */
  public void close()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 60	android/media/MediaDescrambler:mIDescrambler	Landroid/hardware/cas/V1_0/IDescramblerBase;
    //   4: ifnull +30 -> 34
    //   7: aload_0
    //   8: getfield 60	android/media/MediaDescrambler:mIDescrambler	Landroid/hardware/cas/V1_0/IDescramblerBase;
    //   11: invokeinterface 115 1 0
    //   16: pop
    //   17: goto +12 -> 29
    //   20: astore_1
    //   21: aload_0
    //   22: aconst_null
    //   23: putfield 60	android/media/MediaDescrambler:mIDescrambler	Landroid/hardware/cas/V1_0/IDescramblerBase;
    //   26: aload_1
    //   27: athrow
    //   28: astore_1
    //   29: aload_0
    //   30: aconst_null
    //   31: putfield 60	android/media/MediaDescrambler:mIDescrambler	Landroid/hardware/cas/V1_0/IDescramblerBase;
    //   34: aload_0
    //   35: invokespecial 117	android/media/MediaDescrambler:native_release	()V
    //   38: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	39	0	this	MediaDescrambler
    //   20	7	1	localObject	Object
    //   28	1	1	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   7	17	20	finally
    //   7	17	28	android/os/RemoteException
  }
  
  public final int descramble(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2, MediaCodec.CryptoInfo paramCryptoInfo)
  {
    validateInternalStates();
    if (numSubSamples > 0)
    {
      if ((numBytesOfClearData == null) && (numBytesOfEncryptedData == null)) {
        throw new IllegalArgumentException("Invalid CryptoInfo: clearData and encryptedData size arrays are both null!");
      }
      if ((numBytesOfClearData != null) && (numBytesOfClearData.length < numSubSamples)) {
        throw new IllegalArgumentException("Invalid CryptoInfo: numBytesOfClearData is too small!");
      }
      if ((numBytesOfEncryptedData != null) && (numBytesOfEncryptedData.length < numSubSamples)) {
        throw new IllegalArgumentException("Invalid CryptoInfo: numBytesOfEncryptedData is too small!");
      }
      if ((key != null) && (key.length == 16))
      {
        try
        {
          int i = native_descramble(key[0], key[1], numSubSamples, numBytesOfClearData, numBytesOfEncryptedData, paramByteBuffer1, paramByteBuffer1.position(), paramByteBuffer1.limit(), paramByteBuffer2, paramByteBuffer2.position(), paramByteBuffer2.limit());
          return i;
        }
        catch (RemoteException paramByteBuffer1)
        {
          cleanupAndRethrowIllegalState();
        }
        catch (ServiceSpecificException paramByteBuffer1)
        {
          MediaCasStateException.throwExceptionIfNeeded(errorCode, paramByteBuffer1.getMessage());
        }
        return -1;
      }
      throw new IllegalArgumentException("Invalid CryptoInfo: key array is invalid!");
    }
    paramByteBuffer1 = new StringBuilder();
    paramByteBuffer1.append("Invalid CryptoInfo: invalid numSubSamples=");
    paramByteBuffer1.append(numSubSamples);
    throw new IllegalArgumentException(paramByteBuffer1.toString());
  }
  
  protected void finalize()
  {
    close();
  }
  
  IHwBinder getBinder()
  {
    validateInternalStates();
    return mIDescrambler.asBinder();
  }
  
  public final boolean requiresSecureDecoderComponent(String paramString)
  {
    validateInternalStates();
    try
    {
      boolean bool = mIDescrambler.requiresSecureDecoderComponent(paramString);
      return bool;
    }
    catch (RemoteException paramString)
    {
      cleanupAndRethrowIllegalState();
    }
    return true;
  }
  
  public final void setMediaCasSession(MediaCas.Session paramSession)
  {
    validateInternalStates();
    try
    {
      MediaCasStateException.throwExceptionIfNeeded(mIDescrambler.setMediaCasSession(mSessionId));
    }
    catch (RemoteException paramSession)
    {
      cleanupAndRethrowIllegalState();
    }
  }
}
