package android.media;

import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class AmrInputStream
  extends InputStream
{
  private static final int SAMPLES_PER_FRAME = 160;
  private static final String TAG = "AmrInputStream";
  private final byte[] mBuf = new byte['ŀ'];
  private int mBufIn = 0;
  private int mBufOut = 0;
  MediaCodec mCodec;
  MediaCodec.BufferInfo mInfo;
  private InputStream mInputStream;
  private byte[] mOneByte = new byte[1];
  boolean mSawInputEOS;
  boolean mSawOutputEOS;
  
  public AmrInputStream(InputStream paramInputStream)
  {
    Log.w("AmrInputStream", "@@@@ AmrInputStream is not a public API @@@@");
    mInputStream = paramInputStream;
    paramInputStream = new MediaFormat();
    paramInputStream.setString("mime", "audio/3gpp");
    paramInputStream.setInteger("sample-rate", 8000);
    paramInputStream.setInteger("channel-count", 1);
    paramInputStream.setInteger("bitrate", 12200);
    String str = new MediaCodecList(0).findEncoderForFormat(paramInputStream);
    if (str != null) {
      try
      {
        mCodec = MediaCodec.createByCodecName(str);
        mCodec.configure(paramInputStream, null, null, 1);
        mCodec.start();
      }
      catch (IOException paramInputStream)
      {
        if (mCodec != null) {
          mCodec.release();
        }
        mCodec = null;
      }
    }
    mInfo = new MediaCodec.BufferInfo();
  }
  
  /* Error */
  public void close()
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 50	android/media/AmrInputStream:mInputStream	Ljava/io/InputStream;
    //   4: ifnull +10 -> 14
    //   7: aload_0
    //   8: getfield 50	android/media/AmrInputStream:mInputStream	Ljava/io/InputStream;
    //   11: invokevirtual 107	java/io/InputStream:close	()V
    //   14: aload_0
    //   15: aconst_null
    //   16: putfield 50	android/media/AmrInputStream:mInputStream	Ljava/io/InputStream;
    //   19: aload_0
    //   20: getfield 88	android/media/AmrInputStream:mCodec	Landroid/media/MediaCodec;
    //   23: ifnull +10 -> 33
    //   26: aload_0
    //   27: getfield 88	android/media/AmrInputStream:mCodec	Landroid/media/MediaCodec;
    //   30: invokevirtual 98	android/media/MediaCodec:release	()V
    //   33: aload_0
    //   34: aconst_null
    //   35: putfield 88	android/media/AmrInputStream:mCodec	Landroid/media/MediaCodec;
    //   38: return
    //   39: astore_1
    //   40: aload_0
    //   41: aconst_null
    //   42: putfield 88	android/media/AmrInputStream:mCodec	Landroid/media/MediaCodec;
    //   45: aload_1
    //   46: athrow
    //   47: astore_1
    //   48: aload_0
    //   49: aconst_null
    //   50: putfield 50	android/media/AmrInputStream:mInputStream	Ljava/io/InputStream;
    //   53: aload_0
    //   54: getfield 88	android/media/AmrInputStream:mCodec	Landroid/media/MediaCodec;
    //   57: ifnull +10 -> 67
    //   60: aload_0
    //   61: getfield 88	android/media/AmrInputStream:mCodec	Landroid/media/MediaCodec;
    //   64: invokevirtual 98	android/media/MediaCodec:release	()V
    //   67: aload_0
    //   68: aconst_null
    //   69: putfield 88	android/media/AmrInputStream:mCodec	Landroid/media/MediaCodec;
    //   72: aload_1
    //   73: athrow
    //   74: astore_1
    //   75: aload_0
    //   76: aconst_null
    //   77: putfield 88	android/media/AmrInputStream:mCodec	Landroid/media/MediaCodec;
    //   80: aload_1
    //   81: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	82	0	this	AmrInputStream
    //   39	7	1	localObject1	Object
    //   47	26	1	localObject2	Object
    //   74	7	1	localObject3	Object
    // Exception table:
    //   from	to	target	type
    //   19	33	39	finally
    //   0	14	47	finally
    //   53	67	74	finally
  }
  
  protected void finalize()
    throws Throwable
  {
    if (mCodec != null)
    {
      Log.w("AmrInputStream", "AmrInputStream wasn't closed");
      mCodec.release();
    }
  }
  
  public int read()
    throws IOException
  {
    int i;
    if (read(mOneByte, 0, 1) == 1) {
      i = 0xFF & mOneByte[0];
    } else {
      i = -1;
    }
    return i;
  }
  
  public int read(byte[] paramArrayOfByte)
    throws IOException
  {
    return read(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (mCodec != null)
    {
      if ((mBufOut >= mBufIn) && (!mSawOutputEOS))
      {
        mBufOut = 0;
        mBufIn = 0;
        while (!mSawInputEOS)
        {
          int i = mCodec.dequeueInputBuffer(0L);
          if (i < 0) {
            break;
          }
          j = 0;
          int k;
          while (j < 320)
          {
            k = mInputStream.read(mBuf, j, 320 - j);
            if (k == -1)
            {
              mSawInputEOS = true;
              break;
            }
            j += k;
          }
          mCodec.getInputBuffer(i).put(mBuf, 0, j);
          MediaCodec localMediaCodec = mCodec;
          if (mSawInputEOS) {
            k = 4;
          } else {
            k = 0;
          }
          localMediaCodec.queueInputBuffer(i, 0, j, 0L, k);
        }
        int j = mCodec.dequeueOutputBuffer(mInfo, 0L);
        if (j >= 0)
        {
          mBufIn = mInfo.size;
          mCodec.getOutputBuffer(j).get(mBuf, 0, mBufIn);
          mCodec.releaseOutputBuffer(j, false);
          if ((0x4 & mInfo.flags) != 0) {
            mSawOutputEOS = true;
          }
        }
      }
      if (mBufOut < mBufIn)
      {
        if (paramInt2 > mBufIn - mBufOut) {
          paramInt2 = mBufIn - mBufOut;
        }
        System.arraycopy(mBuf, mBufOut, paramArrayOfByte, paramInt1, paramInt2);
        mBufOut += paramInt2;
        return paramInt2;
      }
      if ((mSawInputEOS) && (mSawOutputEOS)) {
        return -1;
      }
      return 0;
    }
    throw new IllegalStateException("not open");
  }
}
