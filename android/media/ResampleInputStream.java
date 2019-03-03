package android.media;

import java.io.IOException;
import java.io.InputStream;

public final class ResampleInputStream
  extends InputStream
{
  private static final String TAG = "ResampleInputStream";
  private static final int mFirLength = 29;
  private byte[] mBuf;
  private int mBufCount;
  private InputStream mInputStream;
  private final byte[] mOneByte = new byte[1];
  private final int mRateIn;
  private final int mRateOut;
  
  static
  {
    System.loadLibrary("media_jni");
  }
  
  public ResampleInputStream(InputStream paramInputStream, int paramInt1, int paramInt2)
  {
    if (paramInt1 == 2 * paramInt2)
    {
      mInputStream = paramInputStream;
      mRateIn = 2;
      mRateOut = 1;
      return;
    }
    throw new IllegalArgumentException("only support 2:1 at the moment");
  }
  
  private static native void fir21(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3);
  
  public void close()
    throws IOException
  {
    try
    {
      if (mInputStream != null) {
        mInputStream.close();
      }
      return;
    }
    finally
    {
      mInputStream = null;
    }
  }
  
  protected void finalize()
    throws Throwable
  {
    if (mInputStream == null) {
      return;
    }
    close();
    throw new IllegalStateException("someone forgot to close ResampleInputStream");
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
    if (mInputStream != null)
    {
      int i = (paramInt2 / 2 * mRateIn / mRateOut + 29) * 2;
      if (mBuf == null)
      {
        mBuf = new byte[i];
      }
      else if (i > mBuf.length)
      {
        byte[] arrayOfByte = new byte[i];
        System.arraycopy(mBuf, 0, arrayOfByte, 0, mBufCount);
        mBuf = arrayOfByte;
      }
      for (;;)
      {
        i = (mBufCount / 2 - 29) * mRateOut / mRateIn * 2;
        if (i > 0)
        {
          if (i < paramInt2) {
            paramInt2 = i;
          } else {
            paramInt2 = paramInt2 / 2 * 2;
          }
          fir21(mBuf, 0, paramArrayOfByte, paramInt1, paramInt2 / 2);
          paramInt1 = mRateIn * paramInt2 / mRateOut;
          mBufCount -= paramInt1;
          if (mBufCount > 0) {
            System.arraycopy(mBuf, paramInt1, mBuf, 0, mBufCount);
          }
          return paramInt2;
        }
        i = mInputStream.read(mBuf, mBufCount, mBuf.length - mBufCount);
        if (i == -1) {
          return -1;
        }
        mBufCount += i;
      }
    }
    throw new IllegalStateException("not open");
  }
}
