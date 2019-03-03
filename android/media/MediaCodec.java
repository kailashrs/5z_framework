package android.media;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.IHwBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.Surface;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.NioUtils;
import java.nio.ReadOnlyBufferException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class MediaCodec
{
  public static final int BUFFER_FLAG_CODEC_CONFIG = 2;
  public static final int BUFFER_FLAG_END_OF_STREAM = 4;
  public static final int BUFFER_FLAG_KEY_FRAME = 1;
  public static final int BUFFER_FLAG_MUXER_DATA = 16;
  public static final int BUFFER_FLAG_PARTIAL_FRAME = 8;
  public static final int BUFFER_FLAG_SYNC_FRAME = 1;
  private static final int CB_ERROR = 3;
  private static final int CB_INPUT_AVAILABLE = 1;
  private static final int CB_OUTPUT_AVAILABLE = 2;
  private static final int CB_OUTPUT_FORMAT_CHANGE = 4;
  public static final int CONFIGURE_FLAG_ENCODE = 1;
  public static final int CRYPTO_MODE_AES_CBC = 2;
  public static final int CRYPTO_MODE_AES_CTR = 1;
  public static final int CRYPTO_MODE_UNENCRYPTED = 0;
  private static final int EVENT_CALLBACK = 1;
  private static final int EVENT_FRAME_RENDERED = 3;
  private static final int EVENT_SET_CALLBACK = 2;
  public static final int INFO_OUTPUT_BUFFERS_CHANGED = -3;
  public static final int INFO_OUTPUT_FORMAT_CHANGED = -2;
  public static final int INFO_TRY_AGAIN_LATER = -1;
  public static final String PARAMETER_KEY_REQUEST_SYNC_FRAME = "request-sync";
  public static final String PARAMETER_KEY_SUSPEND = "drop-input-frames";
  public static final String PARAMETER_KEY_VIDEO_BITRATE = "video-bitrate";
  public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT = 1;
  public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING = 2;
  private final Object mBufferLock;
  private ByteBuffer[] mCachedInputBuffers;
  private ByteBuffer[] mCachedOutputBuffers;
  private Callback mCallback;
  private EventHandler mCallbackHandler;
  private MediaCodecInfo mCodecInfo;
  private final Object mCodecInfoLock = new Object();
  private final BufferMap mDequeuedInputBuffers = new BufferMap(null);
  private final BufferMap mDequeuedOutputBuffers = new BufferMap(null);
  private final Map<Integer, BufferInfo> mDequeuedOutputInfos = new HashMap();
  private EventHandler mEventHandler;
  private boolean mHasSurface = false;
  private final Object mListenerLock = new Object();
  private long mNativeContext;
  private EventHandler mOnFrameRenderedHandler;
  private OnFrameRenderedListener mOnFrameRenderedListener;
  
  static
  {
    System.loadLibrary("media_jni");
    native_init();
  }
  
  private MediaCodec(String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    Looper localLooper = Looper.myLooper();
    if (localLooper != null)
    {
      mEventHandler = new EventHandler(this, localLooper);
    }
    else
    {
      localLooper = Looper.getMainLooper();
      if (localLooper != null) {
        mEventHandler = new EventHandler(this, localLooper);
      } else {
        mEventHandler = null;
      }
    }
    mCallbackHandler = mEventHandler;
    mOnFrameRenderedHandler = mEventHandler;
    mBufferLock = new Object();
    native_setup(paramString, paramBoolean1, paramBoolean2);
  }
  
  private final void cacheBuffers(boolean paramBoolean)
  {
    Object localObject = null;
    try
    {
      ByteBuffer[] arrayOfByteBuffer = getBuffers(paramBoolean);
      localObject = arrayOfByteBuffer;
      invalidateByteBuffers(arrayOfByteBuffer);
      localObject = arrayOfByteBuffer;
    }
    catch (IllegalStateException localIllegalStateException) {}
    if (paramBoolean) {
      mCachedInputBuffers = localObject;
    } else {
      mCachedOutputBuffers = localObject;
    }
  }
  
  private void configure(MediaFormat paramMediaFormat, Surface paramSurface, MediaCrypto paramMediaCrypto, IHwBinder paramIHwBinder, int paramInt)
  {
    if ((paramMediaCrypto != null) && (paramIHwBinder != null)) {
      throw new IllegalArgumentException("Can't use crypto and descrambler together!");
    }
    String[] arrayOfString = null;
    boolean bool = false;
    if (paramMediaFormat != null)
    {
      Object localObject = paramMediaFormat.getMap();
      arrayOfString = new String[((Map)localObject).size()];
      paramMediaFormat = new Object[((Map)localObject).size()];
      Iterator localIterator = ((Map)localObject).entrySet().iterator();
      for (int i = 0; localIterator.hasNext(); i++)
      {
        localObject = (Map.Entry)localIterator.next();
        if (((String)((Map.Entry)localObject).getKey()).equals("audio-session-id"))
        {
          try
          {
            int j = ((Integer)((Map.Entry)localObject).getValue()).intValue();
            arrayOfString[i] = "audio-hw-sync";
            paramMediaFormat[i] = Integer.valueOf(AudioSystem.getAudioHwSyncForSession(j));
          }
          catch (Exception paramMediaFormat)
          {
            throw new IllegalArgumentException("Wrong Session ID Parameter!");
          }
        }
        else
        {
          arrayOfString[i] = ((String)((Map.Entry)localObject).getKey());
          paramMediaFormat[i] = ((Map.Entry)localObject).getValue();
        }
      }
    }
    else
    {
      paramMediaFormat = null;
    }
    if (paramSurface != null) {
      bool = true;
    }
    mHasSurface = bool;
    native_configure(arrayOfString, paramMediaFormat, paramSurface, paramMediaCrypto, paramIHwBinder, paramInt);
  }
  
  public static MediaCodec createByCodecName(String paramString)
    throws IOException
  {
    return new MediaCodec(paramString, false, false);
  }
  
  public static MediaCodec createDecoderByType(String paramString)
    throws IOException
  {
    return new MediaCodec(paramString, true, false);
  }
  
  public static MediaCodec createEncoderByType(String paramString)
    throws IOException
  {
    return new MediaCodec(paramString, true, true);
  }
  
  public static Surface createPersistentInputSurface()
  {
    return native_createPersistentInputSurface();
  }
  
  private final void freeAllTrackedBuffers()
  {
    synchronized (mBufferLock)
    {
      freeByteBuffers(mCachedInputBuffers);
      freeByteBuffers(mCachedOutputBuffers);
      mCachedInputBuffers = null;
      mCachedOutputBuffers = null;
      mDequeuedInputBuffers.clear();
      mDequeuedOutputBuffers.clear();
      return;
    }
  }
  
  private final void freeByteBuffer(ByteBuffer paramByteBuffer)
  {
    if (paramByteBuffer != null) {
      NioUtils.freeDirectBuffer(paramByteBuffer);
    }
  }
  
  private final void freeByteBuffers(ByteBuffer[] paramArrayOfByteBuffer)
  {
    if (paramArrayOfByteBuffer != null)
    {
      int i = paramArrayOfByteBuffer.length;
      for (int j = 0; j < i; j++) {
        freeByteBuffer(paramArrayOfByteBuffer[j]);
      }
    }
  }
  
  private final native ByteBuffer getBuffer(boolean paramBoolean, int paramInt);
  
  private final native ByteBuffer[] getBuffers(boolean paramBoolean);
  
  private EventHandler getEventHandlerOn(Handler paramHandler, EventHandler paramEventHandler)
  {
    if (paramHandler == null) {
      return mEventHandler;
    }
    paramHandler = paramHandler.getLooper();
    if (paramEventHandler.getLooper() == paramHandler) {
      return paramEventHandler;
    }
    return new EventHandler(this, paramHandler);
  }
  
  private final native Map<String, Object> getFormatNative(boolean paramBoolean);
  
  private final native Image getImage(boolean paramBoolean, int paramInt);
  
  private final native Map<String, Object> getOutputFormatNative(int paramInt);
  
  private final native MediaCodecInfo getOwnCodecInfo();
  
  private final void invalidateByteBuffer(ByteBuffer[] paramArrayOfByteBuffer, int paramInt)
  {
    if ((paramArrayOfByteBuffer != null) && (paramInt >= 0) && (paramInt < paramArrayOfByteBuffer.length))
    {
      paramArrayOfByteBuffer = paramArrayOfByteBuffer[paramInt];
      if (paramArrayOfByteBuffer != null) {
        paramArrayOfByteBuffer.setAccessible(false);
      }
    }
  }
  
  private final void invalidateByteBuffers(ByteBuffer[] paramArrayOfByteBuffer)
  {
    if (paramArrayOfByteBuffer != null)
    {
      int i = paramArrayOfByteBuffer.length;
      for (int j = 0; j < i; j++)
      {
        ByteBuffer localByteBuffer = paramArrayOfByteBuffer[j];
        if (localByteBuffer != null) {
          localByteBuffer.setAccessible(false);
        }
      }
    }
  }
  
  private final native void native_configure(String[] paramArrayOfString, Object[] paramArrayOfObject, Surface paramSurface, MediaCrypto paramMediaCrypto, IHwBinder paramIHwBinder, int paramInt);
  
  private static final native PersistentSurface native_createPersistentInputSurface();
  
  private final native int native_dequeueInputBuffer(long paramLong);
  
  private final native int native_dequeueOutputBuffer(BufferInfo paramBufferInfo, long paramLong);
  
  private native void native_enableOnFrameRenderedListener(boolean paramBoolean);
  
  private final native void native_finalize();
  
  private final native void native_flush();
  
  private native PersistableBundle native_getMetrics();
  
  private static final native void native_init();
  
  private final native void native_queueInputBuffer(int paramInt1, int paramInt2, int paramInt3, long paramLong, int paramInt4)
    throws MediaCodec.CryptoException;
  
  private final native void native_queueSecureInputBuffer(int paramInt1, int paramInt2, CryptoInfo paramCryptoInfo, long paramLong, int paramInt3)
    throws MediaCodec.CryptoException;
  
  private final native void native_release();
  
  private static final native void native_releasePersistentInputSurface(Surface paramSurface);
  
  private final native void native_reset();
  
  private final native void native_setCallback(Callback paramCallback);
  
  private final native void native_setInputSurface(Surface paramSurface);
  
  private native void native_setSurface(Surface paramSurface);
  
  private final native void native_setup(String paramString, boolean paramBoolean1, boolean paramBoolean2);
  
  private final native void native_start();
  
  private final native void native_stop();
  
  private void postEventFromNative(int paramInt1, int paramInt2, int paramInt3, Object paramObject)
  {
    synchronized (mListenerLock)
    {
      EventHandler localEventHandler = mEventHandler;
      if (paramInt1 == 1) {
        localEventHandler = mCallbackHandler;
      } else if (paramInt1 == 3) {
        localEventHandler = mOnFrameRenderedHandler;
      }
      if (localEventHandler != null) {
        localEventHandler.sendMessage(localEventHandler.obtainMessage(paramInt1, paramInt2, paramInt3, paramObject));
      }
      return;
    }
  }
  
  private final native void releaseOutputBuffer(int paramInt, boolean paramBoolean1, boolean paramBoolean2, long paramLong);
  
  private final void revalidateByteBuffer(ByteBuffer[] paramArrayOfByteBuffer, int paramInt)
  {
    Object localObject = mBufferLock;
    if ((paramArrayOfByteBuffer != null) && (paramInt >= 0)) {}
    try
    {
      if (paramInt < paramArrayOfByteBuffer.length)
      {
        paramArrayOfByteBuffer = paramArrayOfByteBuffer[paramInt];
        if (paramArrayOfByteBuffer != null) {
          paramArrayOfByteBuffer.setAccessible(true);
        }
      }
      return;
    }
    finally {}
  }
  
  private final native void setParameters(String[] paramArrayOfString, Object[] paramArrayOfObject);
  
  private final void validateInputByteBuffer(ByteBuffer[] paramArrayOfByteBuffer, int paramInt)
  {
    if ((paramArrayOfByteBuffer != null) && (paramInt >= 0) && (paramInt < paramArrayOfByteBuffer.length))
    {
      paramArrayOfByteBuffer = paramArrayOfByteBuffer[paramInt];
      if (paramArrayOfByteBuffer != null)
      {
        paramArrayOfByteBuffer.setAccessible(true);
        paramArrayOfByteBuffer.clear();
      }
    }
  }
  
  private final void validateOutputByteBuffer(ByteBuffer[] paramArrayOfByteBuffer, int paramInt, BufferInfo paramBufferInfo)
  {
    if ((paramArrayOfByteBuffer != null) && (paramInt >= 0) && (paramInt < paramArrayOfByteBuffer.length))
    {
      paramArrayOfByteBuffer = paramArrayOfByteBuffer[paramInt];
      if (paramArrayOfByteBuffer != null)
      {
        paramArrayOfByteBuffer.setAccessible(true);
        paramArrayOfByteBuffer.limit(offset + size).position(offset);
      }
    }
  }
  
  public void configure(MediaFormat paramMediaFormat, Surface paramSurface, int paramInt, MediaDescrambler paramMediaDescrambler)
  {
    if (paramMediaDescrambler != null) {}
    for (paramMediaDescrambler = paramMediaDescrambler.getBinder();; paramMediaDescrambler = null) {
      break;
    }
    configure(paramMediaFormat, paramSurface, null, paramMediaDescrambler, paramInt);
  }
  
  public void configure(MediaFormat paramMediaFormat, Surface paramSurface, MediaCrypto paramMediaCrypto, int paramInt)
  {
    configure(paramMediaFormat, paramSurface, paramMediaCrypto, null, paramInt);
  }
  
  public final native Surface createInputSurface();
  
  public final int dequeueInputBuffer(long paramLong)
  {
    int i = native_dequeueInputBuffer(paramLong);
    if (i >= 0) {
      synchronized (mBufferLock)
      {
        validateInputByteBuffer(mCachedInputBuffers, i);
      }
    }
    return i;
  }
  
  public final int dequeueOutputBuffer(BufferInfo paramBufferInfo, long paramLong)
  {
    int i = native_dequeueOutputBuffer(paramBufferInfo, paramLong);
    Object localObject = mBufferLock;
    if (i == -3)
    {
      try
      {
        cacheBuffers(false);
      }
      finally
      {
        break label84;
      }
    }
    else if (i >= 0)
    {
      validateOutputByteBuffer(mCachedOutputBuffers, i, paramBufferInfo);
      if (mHasSurface) {
        mDequeuedOutputInfos.put(Integer.valueOf(i), paramBufferInfo.dup());
      }
    }
    return i;
    label84:
    throw paramBufferInfo;
  }
  
  protected void finalize()
  {
    native_finalize();
  }
  
  public final void flush()
  {
    synchronized (mBufferLock)
    {
      invalidateByteBuffers(mCachedInputBuffers);
      invalidateByteBuffers(mCachedOutputBuffers);
      mDequeuedInputBuffers.clear();
      mDequeuedOutputBuffers.clear();
      native_flush();
      return;
    }
  }
  
  public MediaCodecInfo getCodecInfo()
  {
    Object localObject1 = getName();
    synchronized (mCodecInfoLock)
    {
      if (mCodecInfo == null)
      {
        mCodecInfo = getOwnCodecInfo();
        if (mCodecInfo == null) {
          mCodecInfo = MediaCodecList.getInfoFor((String)localObject1);
        }
      }
      localObject1 = mCodecInfo;
      return localObject1;
    }
  }
  
  public ByteBuffer getInputBuffer(int paramInt)
  {
    ByteBuffer localByteBuffer = getBuffer(true, paramInt);
    synchronized (mBufferLock)
    {
      invalidateByteBuffer(mCachedInputBuffers, paramInt);
      mDequeuedInputBuffers.put(paramInt, localByteBuffer);
      return localByteBuffer;
    }
  }
  
  public ByteBuffer[] getInputBuffers()
  {
    if (mCachedInputBuffers != null) {
      return mCachedInputBuffers;
    }
    throw new IllegalStateException();
  }
  
  public final MediaFormat getInputFormat()
  {
    return new MediaFormat(getFormatNative(true));
  }
  
  public Image getInputImage(int paramInt)
  {
    Image localImage = getImage(true, paramInt);
    synchronized (mBufferLock)
    {
      invalidateByteBuffer(mCachedInputBuffers, paramInt);
      mDequeuedInputBuffers.put(paramInt, localImage);
      return localImage;
    }
  }
  
  public PersistableBundle getMetrics()
  {
    return native_getMetrics();
  }
  
  public final native String getName();
  
  public ByteBuffer getOutputBuffer(int paramInt)
  {
    ByteBuffer localByteBuffer = getBuffer(false, paramInt);
    synchronized (mBufferLock)
    {
      invalidateByteBuffer(mCachedOutputBuffers, paramInt);
      mDequeuedOutputBuffers.put(paramInt, localByteBuffer);
      return localByteBuffer;
    }
  }
  
  public ByteBuffer[] getOutputBuffers()
  {
    if (mCachedOutputBuffers != null) {
      return mCachedOutputBuffers;
    }
    throw new IllegalStateException();
  }
  
  public final MediaFormat getOutputFormat()
  {
    return new MediaFormat(getFormatNative(false));
  }
  
  public final MediaFormat getOutputFormat(int paramInt)
  {
    return new MediaFormat(getOutputFormatNative(paramInt));
  }
  
  public Image getOutputImage(int paramInt)
  {
    Image localImage = getImage(false, paramInt);
    synchronized (mBufferLock)
    {
      invalidateByteBuffer(mCachedOutputBuffers, paramInt);
      mDequeuedOutputBuffers.put(paramInt, localImage);
      return localImage;
    }
  }
  
  public final void queueInputBuffer(int paramInt1, int paramInt2, int paramInt3, long paramLong, int paramInt4)
    throws MediaCodec.CryptoException
  {
    synchronized (mBufferLock)
    {
      invalidateByteBuffer(mCachedInputBuffers, paramInt1);
      mDequeuedInputBuffers.remove(paramInt1);
      try
      {
        native_queueInputBuffer(paramInt1, paramInt2, paramInt3, paramLong, paramInt4);
        return;
      }
      catch (CryptoException|IllegalStateException localCryptoException)
      {
        revalidateByteBuffer(mCachedInputBuffers, paramInt1);
        throw localCryptoException;
      }
    }
  }
  
  public final void queueSecureInputBuffer(int paramInt1, int paramInt2, CryptoInfo paramCryptoInfo, long paramLong, int paramInt3)
    throws MediaCodec.CryptoException
  {
    synchronized (mBufferLock)
    {
      invalidateByteBuffer(mCachedInputBuffers, paramInt1);
      mDequeuedInputBuffers.remove(paramInt1);
      try
      {
        native_queueSecureInputBuffer(paramInt1, paramInt2, paramCryptoInfo, paramLong, paramInt3);
        return;
      }
      catch (CryptoException|IllegalStateException paramCryptoInfo)
      {
        revalidateByteBuffer(mCachedInputBuffers, paramInt1);
        throw paramCryptoInfo;
      }
    }
  }
  
  public final void release()
  {
    freeAllTrackedBuffers();
    native_release();
  }
  
  public final void releaseOutputBuffer(int paramInt, long paramLong)
  {
    synchronized (mBufferLock)
    {
      invalidateByteBuffer(mCachedOutputBuffers, paramInt);
      mDequeuedOutputBuffers.remove(paramInt);
      if (mHasSurface) {
        BufferInfo localBufferInfo = (BufferInfo)mDequeuedOutputInfos.remove(Integer.valueOf(paramInt));
      }
      releaseOutputBuffer(paramInt, true, true, paramLong);
      return;
    }
  }
  
  public final void releaseOutputBuffer(int paramInt, boolean paramBoolean)
  {
    synchronized (mBufferLock)
    {
      invalidateByteBuffer(mCachedOutputBuffers, paramInt);
      mDequeuedOutputBuffers.remove(paramInt);
      if (mHasSurface) {
        BufferInfo localBufferInfo = (BufferInfo)mDequeuedOutputInfos.remove(Integer.valueOf(paramInt));
      }
      releaseOutputBuffer(paramInt, paramBoolean, false, 0L);
      return;
    }
  }
  
  public final void reset()
  {
    freeAllTrackedBuffers();
    native_reset();
  }
  
  public void setCallback(Callback paramCallback)
  {
    setCallback(paramCallback, null);
  }
  
  public void setCallback(Callback paramCallback, Handler paramHandler)
  {
    if (paramCallback != null) {
      synchronized (mListenerLock)
      {
        paramHandler = getEventHandlerOn(paramHandler, mCallbackHandler);
        if (paramHandler != mCallbackHandler)
        {
          mCallbackHandler.removeMessages(2);
          mCallbackHandler.removeMessages(1);
          mCallbackHandler = paramHandler;
        }
      }
    }
    if (mCallbackHandler != null)
    {
      mCallbackHandler.removeMessages(2);
      mCallbackHandler.removeMessages(1);
    }
    if (mCallbackHandler != null)
    {
      paramHandler = mCallbackHandler.obtainMessage(2, 0, 0, paramCallback);
      mCallbackHandler.sendMessage(paramHandler);
      native_setCallback(paramCallback);
    }
  }
  
  public void setInputSurface(Surface paramSurface)
  {
    if ((paramSurface instanceof PersistentSurface))
    {
      native_setInputSurface(paramSurface);
      return;
    }
    throw new IllegalArgumentException("not a PersistentSurface");
  }
  
  public void setOnFrameRenderedListener(OnFrameRenderedListener paramOnFrameRenderedListener, Handler paramHandler)
  {
    synchronized (mListenerLock)
    {
      mOnFrameRenderedListener = paramOnFrameRenderedListener;
      if (paramOnFrameRenderedListener != null)
      {
        paramHandler = getEventHandlerOn(paramHandler, mOnFrameRenderedHandler);
        if (paramHandler != mOnFrameRenderedHandler) {
          mOnFrameRenderedHandler.removeMessages(3);
        }
        mOnFrameRenderedHandler = paramHandler;
      }
      else if (mOnFrameRenderedHandler != null)
      {
        mOnFrameRenderedHandler.removeMessages(3);
      }
      boolean bool;
      if (paramOnFrameRenderedListener != null) {
        bool = true;
      } else {
        bool = false;
      }
      native_enableOnFrameRenderedListener(bool);
      return;
    }
  }
  
  public void setOutputSurface(Surface paramSurface)
  {
    if (mHasSurface)
    {
      native_setSurface(paramSurface);
      return;
    }
    throw new IllegalStateException("codec was not configured for an output surface");
  }
  
  public final void setParameters(Bundle paramBundle)
  {
    if (paramBundle == null) {
      return;
    }
    String[] arrayOfString = new String[paramBundle.size()];
    Object[] arrayOfObject = new Object[paramBundle.size()];
    int i = 0;
    Iterator localIterator = paramBundle.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      arrayOfString[i] = str;
      arrayOfObject[i] = paramBundle.get(str);
      i++;
    }
    setParameters(arrayOfString, arrayOfObject);
  }
  
  public final native void setVideoScalingMode(int paramInt);
  
  public final native void signalEndOfInputStream();
  
  public final void start()
  {
    native_start();
    synchronized (mBufferLock)
    {
      cacheBuffers(true);
      cacheBuffers(false);
      return;
    }
  }
  
  public final void stop()
  {
    native_stop();
    freeAllTrackedBuffers();
    synchronized (mListenerLock)
    {
      if (mCallbackHandler != null)
      {
        mCallbackHandler.removeMessages(2);
        mCallbackHandler.removeMessages(1);
      }
      if (mOnFrameRenderedHandler != null) {
        mOnFrameRenderedHandler.removeMessages(3);
      }
      return;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface BufferFlag {}
  
  public static final class BufferInfo
  {
    public int flags;
    public int offset;
    public long presentationTimeUs;
    public int size;
    
    public BufferInfo() {}
    
    public BufferInfo dup()
    {
      BufferInfo localBufferInfo = new BufferInfo();
      localBufferInfo.set(offset, size, presentationTimeUs, flags);
      return localBufferInfo;
    }
    
    public void set(int paramInt1, int paramInt2, long paramLong, int paramInt3)
    {
      offset = paramInt1;
      size = paramInt2;
      presentationTimeUs = paramLong;
      flags = paramInt3;
    }
  }
  
  private static class BufferMap
  {
    private final Map<Integer, CodecBuffer> mMap = new HashMap();
    
    private BufferMap() {}
    
    public void clear()
    {
      Iterator localIterator = mMap.values().iterator();
      while (localIterator.hasNext()) {
        ((CodecBuffer)localIterator.next()).free();
      }
      mMap.clear();
    }
    
    public void put(int paramInt, Image paramImage)
    {
      CodecBuffer localCodecBuffer1 = (CodecBuffer)mMap.get(Integer.valueOf(paramInt));
      CodecBuffer localCodecBuffer2 = localCodecBuffer1;
      if (localCodecBuffer1 == null)
      {
        localCodecBuffer2 = new CodecBuffer(null);
        mMap.put(Integer.valueOf(paramInt), localCodecBuffer2);
      }
      localCodecBuffer2.setImage(paramImage);
    }
    
    public void put(int paramInt, ByteBuffer paramByteBuffer)
    {
      CodecBuffer localCodecBuffer1 = (CodecBuffer)mMap.get(Integer.valueOf(paramInt));
      CodecBuffer localCodecBuffer2 = localCodecBuffer1;
      if (localCodecBuffer1 == null)
      {
        localCodecBuffer2 = new CodecBuffer(null);
        mMap.put(Integer.valueOf(paramInt), localCodecBuffer2);
      }
      localCodecBuffer2.setByteBuffer(paramByteBuffer);
    }
    
    public void remove(int paramInt)
    {
      CodecBuffer localCodecBuffer = (CodecBuffer)mMap.get(Integer.valueOf(paramInt));
      if (localCodecBuffer != null)
      {
        localCodecBuffer.free();
        mMap.remove(Integer.valueOf(paramInt));
      }
    }
    
    private static class CodecBuffer
    {
      private ByteBuffer mByteBuffer;
      private Image mImage;
      
      private CodecBuffer() {}
      
      public void free()
      {
        if (mByteBuffer != null)
        {
          NioUtils.freeDirectBuffer(mByteBuffer);
          mByteBuffer = null;
        }
        if (mImage != null)
        {
          mImage.close();
          mImage = null;
        }
      }
      
      public void setByteBuffer(ByteBuffer paramByteBuffer)
      {
        free();
        mByteBuffer = paramByteBuffer;
      }
      
      public void setImage(Image paramImage)
      {
        free();
        mImage = paramImage;
      }
    }
  }
  
  public static abstract class Callback
  {
    public Callback() {}
    
    public abstract void onError(MediaCodec paramMediaCodec, MediaCodec.CodecException paramCodecException);
    
    public abstract void onInputBufferAvailable(MediaCodec paramMediaCodec, int paramInt);
    
    public abstract void onOutputBufferAvailable(MediaCodec paramMediaCodec, int paramInt, MediaCodec.BufferInfo paramBufferInfo);
    
    public abstract void onOutputFormatChanged(MediaCodec paramMediaCodec, MediaFormat paramMediaFormat);
  }
  
  public static final class CodecException
    extends IllegalStateException
  {
    private static final int ACTION_RECOVERABLE = 2;
    private static final int ACTION_TRANSIENT = 1;
    public static final int ERROR_INSUFFICIENT_RESOURCE = 1100;
    public static final int ERROR_RECLAIMED = 1101;
    private final int mActionCode;
    private final String mDiagnosticInfo;
    private final int mErrorCode;
    
    CodecException(int paramInt1, int paramInt2, String paramString)
    {
      super();
      mErrorCode = paramInt1;
      mActionCode = paramInt2;
      if (paramInt1 < 0) {
        paramString = "neg_";
      } else {
        paramString = "";
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("android.media.MediaCodec.error_");
      localStringBuilder.append(paramString);
      localStringBuilder.append(Math.abs(paramInt1));
      mDiagnosticInfo = localStringBuilder.toString();
    }
    
    public String getDiagnosticInfo()
    {
      return mDiagnosticInfo;
    }
    
    public int getErrorCode()
    {
      return mErrorCode;
    }
    
    public boolean isRecoverable()
    {
      boolean bool;
      if (mActionCode == 2) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isTransient()
    {
      int i = mActionCode;
      boolean bool = true;
      if (i != 1) {
        bool = false;
      }
      return bool;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface ReasonCode {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ConfigureFlag {}
  
  public static final class CryptoException
    extends RuntimeException
  {
    public static final int ERROR_INSUFFICIENT_OUTPUT_PROTECTION = 4;
    public static final int ERROR_KEY_EXPIRED = 2;
    public static final int ERROR_NO_KEY = 1;
    public static final int ERROR_RESOURCE_BUSY = 3;
    public static final int ERROR_SESSION_NOT_OPENED = 5;
    public static final int ERROR_UNSUPPORTED_OPERATION = 6;
    private int mErrorCode;
    
    public CryptoException(int paramInt, String paramString)
    {
      super();
      mErrorCode = paramInt;
    }
    
    public int getErrorCode()
    {
      return mErrorCode;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface CryptoErrorCode {}
  }
  
  public static final class CryptoInfo
  {
    public byte[] iv;
    public byte[] key;
    public int mode;
    public int[] numBytesOfClearData;
    public int[] numBytesOfEncryptedData;
    public int numSubSamples;
    private Pattern pattern;
    private final Pattern zeroPattern = new Pattern(0, 0);
    
    public CryptoInfo() {}
    
    private void setPattern(int paramInt1, int paramInt2)
    {
      pattern = new Pattern(paramInt1, paramInt2);
    }
    
    public void set(int paramInt1, int[] paramArrayOfInt1, int[] paramArrayOfInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt2)
    {
      numSubSamples = paramInt1;
      numBytesOfClearData = paramArrayOfInt1;
      numBytesOfEncryptedData = paramArrayOfInt2;
      key = paramArrayOfByte1;
      iv = paramArrayOfByte2;
      mode = paramInt2;
      pattern = zeroPattern;
    }
    
    public void setPattern(Pattern paramPattern)
    {
      pattern = paramPattern;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      StringBuilder localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(numSubSamples);
      localStringBuilder2.append(" subsamples, key [");
      localStringBuilder1.append(localStringBuilder2.toString());
      int i = 0;
      for (int j = 0; j < key.length; j++)
      {
        localStringBuilder1.append("0123456789abcdef".charAt((key[j] & 0xF0) >> 4));
        localStringBuilder1.append("0123456789abcdef".charAt(key[j] & 0xF));
      }
      localStringBuilder1.append("], iv [");
      for (j = i; j < key.length; j++)
      {
        localStringBuilder1.append("0123456789abcdef".charAt((iv[j] & 0xF0) >> 4));
        localStringBuilder1.append("0123456789abcdef".charAt(iv[j] & 0xF));
      }
      localStringBuilder1.append("], clear ");
      localStringBuilder1.append(Arrays.toString(numBytesOfClearData));
      localStringBuilder1.append(", encrypted ");
      localStringBuilder1.append(Arrays.toString(numBytesOfEncryptedData));
      return localStringBuilder1.toString();
    }
    
    public static final class Pattern
    {
      private int mEncryptBlocks;
      private int mSkipBlocks;
      
      public Pattern(int paramInt1, int paramInt2)
      {
        set(paramInt1, paramInt2);
      }
      
      public int getEncryptBlocks()
      {
        return mEncryptBlocks;
      }
      
      public int getSkipBlocks()
      {
        return mSkipBlocks;
      }
      
      public void set(int paramInt1, int paramInt2)
      {
        mEncryptBlocks = paramInt1;
        mSkipBlocks = paramInt2;
      }
    }
  }
  
  private class EventHandler
    extends Handler
  {
    private MediaCodec mCodec;
    
    public EventHandler(MediaCodec paramMediaCodec, Looper paramLooper)
    {
      super();
      mCodec = paramMediaCodec;
    }
    
    private void handleCallback(Message arg1)
    {
      if (mCallback == null) {
        return;
      }
      int i;
      switch (arg1)
      {
      default: 
        break;
      case 4: 
        mCallback.onOutputFormatChanged(mCodec, new MediaFormat((Map)obj));
        break;
      case 3: 
        mCallback.onError(mCodec, (MediaCodec.CodecException)obj);
        break;
      case 2: 
        i = arg2;
        MediaCodec.BufferInfo localBufferInfo = (MediaCodec.BufferInfo)obj;
        synchronized (mBufferLock)
        {
          MediaCodec.this.validateOutputByteBuffer(mCachedOutputBuffers, i, localBufferInfo);
          mCallback.onOutputBufferAvailable(mCodec, i, localBufferInfo);
        }
      case 1: 
        i = arg2;
        synchronized (mBufferLock)
        {
          MediaCodec.this.validateInputByteBuffer(mCachedInputBuffers, i);
          mCallback.onInputBufferAvailable(mCodec, i);
        }
      }
    }
    
    public void handleMessage(Message paramMessage)
    {
      switch (what)
      {
      default: 
        break;
      case 3: 
        synchronized (mListenerLock)
        {
          paramMessage = (Map)obj;
          for (int i = 0;; i++)
          {
            Object localObject2 = new java/lang/StringBuilder;
            ((StringBuilder)localObject2).<init>();
            ((StringBuilder)localObject2).append(i);
            ((StringBuilder)localObject2).append("-media-time-us");
            localObject2 = paramMessage.get(((StringBuilder)localObject2).toString());
            Object localObject3 = new java/lang/StringBuilder;
            ((StringBuilder)localObject3).<init>();
            ((StringBuilder)localObject3).append(i);
            ((StringBuilder)localObject3).append("-system-nano");
            localObject3 = paramMessage.get(((StringBuilder)localObject3).toString());
            if ((localObject2 == null) || (localObject3 == null) || (mOnFrameRenderedListener == null)) {
              break;
            }
            mOnFrameRenderedListener.onFrameRendered(mCodec, ((Long)localObject2).longValue(), ((Long)localObject3).longValue());
          }
        }
      case 2: 
        MediaCodec.access$002(MediaCodec.this, (MediaCodec.Callback)obj);
        break;
      case 1: 
        handleCallback(paramMessage);
      }
    }
  }
  
  public static class MediaImage
    extends Image
  {
    private static final int TYPE_YUV = 1;
    private final ByteBuffer mBuffer;
    private final int mFormat = 35;
    private final int mHeight;
    private final ByteBuffer mInfo;
    private final boolean mIsReadOnly;
    private final Image.Plane[] mPlanes;
    private final int mScalingMode = 0;
    private long mTimestamp;
    private final int mTransform = 0;
    private final int mWidth;
    private final int mXOffset;
    private final int mYOffset;
    
    public MediaImage(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2, boolean paramBoolean, long paramLong, int paramInt1, int paramInt2, Rect paramRect)
    {
      mTimestamp = paramLong;
      mIsImageValid = true;
      mIsReadOnly = paramByteBuffer1.isReadOnly();
      mBuffer = paramByteBuffer1.duplicate();
      mXOffset = paramInt1;
      mYOffset = paramInt2;
      mInfo = paramByteBuffer2;
      if (paramByteBuffer2.remaining() == 104)
      {
        int i = paramByteBuffer2.getInt();
        if (i == 1)
        {
          int j = paramByteBuffer2.getInt();
          if (j == 3)
          {
            mWidth = paramByteBuffer2.getInt();
            mHeight = paramByteBuffer2.getInt();
            if ((mWidth >= 1) && (mHeight >= 1))
            {
              int k = paramByteBuffer2.getInt();
              if (k == 8)
              {
                i = paramByteBuffer2.getInt();
                if (i == 8)
                {
                  mPlanes = new MediaPlane[j];
                  i = 0;
                  while (i < j)
                  {
                    int m = paramByteBuffer2.getInt();
                    int n = paramByteBuffer2.getInt();
                    int i1 = paramByteBuffer2.getInt();
                    int i2 = paramByteBuffer2.getInt();
                    int i3 = paramByteBuffer2.getInt();
                    if (i2 == i3)
                    {
                      int i4;
                      if (i == 0) {
                        i4 = 1;
                      } else {
                        i4 = 2;
                      }
                      if (i2 == i4)
                      {
                        if ((n >= 1) && (i1 >= 1))
                        {
                          paramByteBuffer1.clear();
                          paramByteBuffer1.position(mBuffer.position() + m + paramInt1 / i2 * n + paramInt2 / i3 * i1);
                          paramByteBuffer1.limit(paramByteBuffer1.position() + Utils.divUp(k, 8) + (mHeight / i3 - 1) * i1 + (mWidth / i2 - 1) * n);
                          mPlanes[i] = new MediaPlane(paramByteBuffer1.slice(), i1, n);
                          i++;
                          continue;
                        }
                        paramByteBuffer1 = new StringBuilder();
                        paramByteBuffer1.append("unexpected strides: ");
                        paramByteBuffer1.append(n);
                        paramByteBuffer1.append(" pixel, ");
                        paramByteBuffer1.append(i1);
                        paramByteBuffer1.append(" row on plane ");
                        paramByteBuffer1.append(i);
                        throw new UnsupportedOperationException(paramByteBuffer1.toString());
                      }
                    }
                    paramByteBuffer1 = new StringBuilder();
                    paramByteBuffer1.append("unexpected subsampling: ");
                    paramByteBuffer1.append(i2);
                    paramByteBuffer1.append("x");
                    paramByteBuffer1.append(i3);
                    paramByteBuffer1.append(" on plane ");
                    paramByteBuffer1.append(i);
                    throw new UnsupportedOperationException(paramByteBuffer1.toString());
                  }
                  if (paramRect == null) {
                    paramByteBuffer1 = new Rect(0, 0, mWidth, mHeight);
                  } else {
                    paramByteBuffer1 = paramRect;
                  }
                  paramByteBuffer1.offset(-paramInt1, -paramInt2);
                  super.setCropRect(paramByteBuffer1);
                  return;
                }
                paramByteBuffer1 = new StringBuilder();
                paramByteBuffer1.append("unsupported allocated bit depth: ");
                paramByteBuffer1.append(i);
                throw new UnsupportedOperationException(paramByteBuffer1.toString());
              }
              paramByteBuffer1 = new StringBuilder();
              paramByteBuffer1.append("unsupported bit depth: ");
              paramByteBuffer1.append(k);
              throw new UnsupportedOperationException(paramByteBuffer1.toString());
            }
            paramByteBuffer1 = new StringBuilder();
            paramByteBuffer1.append("unsupported size: ");
            paramByteBuffer1.append(mWidth);
            paramByteBuffer1.append("x");
            paramByteBuffer1.append(mHeight);
            throw new UnsupportedOperationException(paramByteBuffer1.toString());
          }
          paramByteBuffer1 = new StringBuilder();
          paramByteBuffer1.append("unexpected number of planes: ");
          paramByteBuffer1.append(j);
          throw new RuntimeException(paramByteBuffer1.toString());
        }
        paramByteBuffer1 = new StringBuilder();
        paramByteBuffer1.append("unsupported type: ");
        paramByteBuffer1.append(i);
        throw new UnsupportedOperationException(paramByteBuffer1.toString());
      }
      paramByteBuffer1 = new StringBuilder();
      paramByteBuffer1.append("unsupported info length: ");
      paramByteBuffer1.append(paramByteBuffer2.remaining());
      throw new UnsupportedOperationException(paramByteBuffer1.toString());
    }
    
    public void close()
    {
      if (mIsImageValid)
      {
        NioUtils.freeDirectBuffer(mBuffer);
        mIsImageValid = false;
      }
    }
    
    public int getFormat()
    {
      throwISEIfImageIsInvalid();
      return mFormat;
    }
    
    public int getHeight()
    {
      throwISEIfImageIsInvalid();
      return mHeight;
    }
    
    public Image.Plane[] getPlanes()
    {
      throwISEIfImageIsInvalid();
      return (Image.Plane[])Arrays.copyOf(mPlanes, mPlanes.length);
    }
    
    public int getScalingMode()
    {
      throwISEIfImageIsInvalid();
      return 0;
    }
    
    public long getTimestamp()
    {
      throwISEIfImageIsInvalid();
      return mTimestamp;
    }
    
    public int getTransform()
    {
      throwISEIfImageIsInvalid();
      return 0;
    }
    
    public int getWidth()
    {
      throwISEIfImageIsInvalid();
      return mWidth;
    }
    
    public void setCropRect(Rect paramRect)
    {
      if (!mIsReadOnly)
      {
        super.setCropRect(paramRect);
        return;
      }
      throw new ReadOnlyBufferException();
    }
    
    private class MediaPlane
      extends Image.Plane
    {
      private final int mColInc;
      private final ByteBuffer mData;
      private final int mRowInc;
      
      public MediaPlane(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2)
      {
        mData = paramByteBuffer;
        mRowInc = paramInt1;
        mColInc = paramInt2;
      }
      
      public ByteBuffer getBuffer()
      {
        throwISEIfImageIsInvalid();
        return mData;
      }
      
      public int getPixelStride()
      {
        throwISEIfImageIsInvalid();
        return mColInc;
      }
      
      public int getRowStride()
      {
        throwISEIfImageIsInvalid();
        return mRowInc;
      }
    }
  }
  
  public static final class MetricsConstants
  {
    public static final String CODEC = "android.media.mediacodec.codec";
    public static final String ENCODER = "android.media.mediacodec.encoder";
    public static final String HEIGHT = "android.media.mediacodec.height";
    public static final String MIME_TYPE = "android.media.mediacodec.mime";
    public static final String MODE = "android.media.mediacodec.mode";
    public static final String MODE_AUDIO = "audio";
    public static final String MODE_VIDEO = "video";
    public static final String ROTATION = "android.media.mediacodec.rotation";
    public static final String SECURE = "android.media.mediacodec.secure";
    public static final String WIDTH = "android.media.mediacodec.width";
    
    private MetricsConstants() {}
  }
  
  public static abstract interface OnFrameRenderedListener
  {
    public abstract void onFrameRendered(MediaCodec paramMediaCodec, long paramLong1, long paramLong2);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface OutputBufferInfo {}
  
  static class PersistentSurface
    extends Surface
  {
    private long mPersistentObject;
    
    PersistentSurface() {}
    
    public void release()
    {
      MediaCodec.native_releasePersistentInputSurface(this);
      super.release();
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface VideoScalingMode {}
}
