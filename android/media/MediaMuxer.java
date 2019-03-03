package android.media;

import dalvik.system.CloseGuard;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class MediaMuxer
{
  private static final int MUXER_STATE_INITIALIZED = 0;
  private static final int MUXER_STATE_STARTED = 1;
  private static final int MUXER_STATE_STOPPED = 2;
  private static final int MUXER_STATE_UNINITIALIZED = -1;
  private final CloseGuard mCloseGuard = CloseGuard.get();
  private int mLastTrackIndex = -1;
  private long mNativeObject;
  private int mState = -1;
  
  static
  {
    System.loadLibrary("media_jni");
  }
  
  public MediaMuxer(FileDescriptor paramFileDescriptor, int paramInt)
    throws IOException
  {
    setUpMediaMuxer(paramFileDescriptor, paramInt);
  }
  
  public MediaMuxer(String paramString, int paramInt)
    throws IOException
  {
    if (paramString != null)
    {
      Object localObject1 = null;
      Object localObject2 = localObject1;
      try
      {
        RandomAccessFile localRandomAccessFile = new java/io/RandomAccessFile;
        localObject2 = localObject1;
        localRandomAccessFile.<init>(paramString, "rws");
        paramString = localRandomAccessFile;
        localObject2 = paramString;
        paramString.setLength(0L);
        localObject2 = paramString;
        setUpMediaMuxer(paramString.getFD(), paramInt);
        paramString.close();
        return;
      }
      finally
      {
        if (localObject2 != null) {
          ((RandomAccessFile)localObject2).close();
        }
      }
    }
    throw new IllegalArgumentException("path must not be null");
  }
  
  private static native int nativeAddTrack(long paramLong, String[] paramArrayOfString, Object[] paramArrayOfObject);
  
  private static native void nativeRelease(long paramLong);
  
  private static native void nativeSetLocation(long paramLong, int paramInt1, int paramInt2);
  
  private static native void nativeSetOrientationHint(long paramLong, int paramInt);
  
  private static native long nativeSetup(FileDescriptor paramFileDescriptor, int paramInt)
    throws IllegalArgumentException, IOException;
  
  private static native void nativeStart(long paramLong);
  
  private static native void nativeStop(long paramLong);
  
  private static native void nativeWriteSampleData(long paramLong1, int paramInt1, ByteBuffer paramByteBuffer, int paramInt2, int paramInt3, long paramLong2, int paramInt4);
  
  private void setUpMediaMuxer(FileDescriptor paramFileDescriptor, int paramInt)
    throws IOException
  {
    if ((paramInt >= 0) && (paramInt <= 3))
    {
      mNativeObject = nativeSetup(paramFileDescriptor, paramInt);
      mState = 0;
      mCloseGuard.open("release");
      return;
    }
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("format: ");
    paramFileDescriptor.append(paramInt);
    paramFileDescriptor.append(" is invalid");
    throw new IllegalArgumentException(paramFileDescriptor.toString());
  }
  
  public int addTrack(MediaFormat paramMediaFormat)
  {
    if (paramMediaFormat != null)
    {
      if (mState == 0)
      {
        if (mNativeObject != 0L)
        {
          Object localObject = paramMediaFormat.getMap();
          int i = ((Map)localObject).size();
          if (i > 0)
          {
            paramMediaFormat = new String[i];
            Object[] arrayOfObject = new Object[i];
            i = 0;
            localObject = ((Map)localObject).entrySet().iterator();
            while (((Iterator)localObject).hasNext())
            {
              Map.Entry localEntry = (Map.Entry)((Iterator)localObject).next();
              paramMediaFormat[i] = ((String)localEntry.getKey());
              arrayOfObject[i] = localEntry.getValue();
              i++;
            }
            i = nativeAddTrack(mNativeObject, paramMediaFormat, arrayOfObject);
            if (mLastTrackIndex < i)
            {
              mLastTrackIndex = i;
              return i;
            }
            throw new IllegalArgumentException("Invalid format.");
          }
          throw new IllegalArgumentException("format must not be empty.");
        }
        throw new IllegalStateException("Muxer has been released!");
      }
      throw new IllegalStateException("Muxer is not initialized.");
    }
    throw new IllegalArgumentException("format must not be null.");
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (mCloseGuard != null) {
        mCloseGuard.warnIfOpen();
      }
      if (mNativeObject != 0L)
      {
        nativeRelease(mNativeObject);
        mNativeObject = 0L;
      }
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public void release()
  {
    if (mState == 1) {
      stop();
    }
    if (mNativeObject != 0L)
    {
      nativeRelease(mNativeObject);
      mNativeObject = 0L;
      mCloseGuard.close();
    }
    mState = -1;
  }
  
  public void setLocation(float paramFloat1, float paramFloat2)
  {
    int i = (int)(paramFloat1 * 10000.0F + 0.5D);
    int j = (int)(10000.0F * paramFloat2 + 0.5D);
    if ((i <= 900000) && (i >= -900000))
    {
      if ((j <= 1800000) && (j >= -1800000))
      {
        if ((mState == 0) && (mNativeObject != 0L))
        {
          nativeSetLocation(mNativeObject, i, j);
          return;
        }
        throw new IllegalStateException("Can't set location due to wrong state.");
      }
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Longitude: ");
      localStringBuilder.append(paramFloat2);
      localStringBuilder.append(" out of range");
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Latitude: ");
    localStringBuilder.append(paramFloat1);
    localStringBuilder.append(" out of range.");
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public void setOrientationHint(int paramInt)
  {
    if ((paramInt != 0) && (paramInt != 90) && (paramInt != 180) && (paramInt != 270))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unsupported angle: ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    if (mState == 0)
    {
      nativeSetOrientationHint(mNativeObject, paramInt);
      return;
    }
    throw new IllegalStateException("Can't set rotation degrees due to wrong state.");
  }
  
  public void start()
  {
    if (mNativeObject != 0L)
    {
      if (mState == 0)
      {
        nativeStart(mNativeObject);
        mState = 1;
        return;
      }
      throw new IllegalStateException("Can't start due to wrong state.");
    }
    throw new IllegalStateException("Muxer has been released!");
  }
  
  public void stop()
  {
    if (mState == 1)
    {
      nativeStop(mNativeObject);
      mState = 2;
      return;
    }
    throw new IllegalStateException("Can't stop due to wrong state.");
  }
  
  public void writeSampleData(int paramInt, ByteBuffer paramByteBuffer, MediaCodec.BufferInfo paramBufferInfo)
  {
    if ((paramInt >= 0) && (paramInt <= mLastTrackIndex))
    {
      if (paramByteBuffer != null)
      {
        if (paramBufferInfo != null)
        {
          if ((size >= 0) && (offset >= 0) && (offset + size <= paramByteBuffer.capacity()) && (presentationTimeUs >= 0L))
          {
            if (mNativeObject != 0L)
            {
              if (mState == 1)
              {
                nativeWriteSampleData(mNativeObject, paramInt, paramByteBuffer, offset, size, presentationTimeUs, flags);
                return;
              }
              throw new IllegalStateException("Can't write, muxer is not started");
            }
            throw new IllegalStateException("Muxer has been released!");
          }
          throw new IllegalArgumentException("bufferInfo must specify a valid buffer offset, size and presentation time");
        }
        throw new IllegalArgumentException("bufferInfo must not be null");
      }
      throw new IllegalArgumentException("byteBuffer must not be null");
    }
    throw new IllegalArgumentException("trackIndex is invalid");
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Format {}
  
  public static final class OutputFormat
  {
    public static final int MUXER_OUTPUT_3GPP = 2;
    public static final int MUXER_OUTPUT_FIRST = 0;
    public static final int MUXER_OUTPUT_HEIF = 3;
    public static final int MUXER_OUTPUT_LAST = 3;
    public static final int MUXER_OUTPUT_MPEG_4 = 0;
    public static final int MUXER_OUTPUT_WEBM = 1;
    
    private OutputFormat() {}
  }
}
