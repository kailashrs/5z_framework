package android.graphics;

import java.io.OutputStream;

public class YuvImage
{
  private static final int WORKING_COMPRESS_STORAGE = 4096;
  private byte[] mData;
  private int mFormat;
  private int mHeight;
  private int[] mStrides;
  private int mWidth;
  
  public YuvImage(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt)
  {
    if ((paramInt1 != 17) && (paramInt1 != 20)) {
      throw new IllegalArgumentException("only support ImageFormat.NV21 and ImageFormat.YUY2 for now");
    }
    if ((paramInt2 > 0) && (paramInt3 > 0))
    {
      if (paramArrayOfByte != null)
      {
        if (paramArrayOfInt == null) {
          mStrides = calculateStrides(paramInt2, paramInt1);
        } else {
          mStrides = paramArrayOfInt;
        }
        mData = paramArrayOfByte;
        mFormat = paramInt1;
        mWidth = paramInt2;
        mHeight = paramInt3;
        return;
      }
      throw new IllegalArgumentException("yuv cannot be null");
    }
    throw new IllegalArgumentException("width and height must large than 0");
  }
  
  private void adjustRectangle(Rect paramRect)
  {
    int i = paramRect.width();
    int j = paramRect.height();
    int k = i;
    if (mFormat == 17)
    {
      k = i & 0xFFFFFFFE;
      left &= 0xFFFFFFFE;
      top &= 0xFFFFFFFE;
      right = (left + k);
      bottom = (top + (j & 0xFFFFFFFE));
    }
    if (mFormat == 20)
    {
      left &= 0xFFFFFFFE;
      right = (left + (k & 0xFFFFFFFE));
    }
  }
  
  private int[] calculateStrides(int paramInt1, int paramInt2)
  {
    if (paramInt2 == 17) {
      return new int[] { paramInt1, paramInt1 };
    }
    if (paramInt2 == 20) {
      return new int[] { paramInt1 * 2 };
    }
    return null;
  }
  
  private static native boolean nativeCompressToJpeg(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt4, OutputStream paramOutputStream, byte[] paramArrayOfByte2);
  
  int[] calculateOffsets(int paramInt1, int paramInt2)
  {
    if (mFormat == 17) {
      return new int[] { mStrides[0] * paramInt2 + paramInt1, mHeight * mStrides[0] + paramInt2 / 2 * mStrides[1] + paramInt1 / 2 * 2 };
    }
    if (mFormat == 20) {
      return new int[] { mStrides[0] * paramInt2 + paramInt1 / 2 * 4 };
    }
    return null;
  }
  
  public boolean compressToJpeg(Rect paramRect, int paramInt, OutputStream paramOutputStream)
  {
    if (new Rect(0, 0, mWidth, mHeight).contains(paramRect))
    {
      if ((paramInt >= 0) && (paramInt <= 100))
      {
        if (paramOutputStream != null)
        {
          adjustRectangle(paramRect);
          int[] arrayOfInt = calculateOffsets(left, top);
          return nativeCompressToJpeg(mData, mFormat, paramRect.width(), paramRect.height(), arrayOfInt, mStrides, paramInt, paramOutputStream, new byte['က']);
        }
        throw new IllegalArgumentException("stream cannot be null");
      }
      throw new IllegalArgumentException("quality must be 0..100");
    }
    throw new IllegalArgumentException("rectangle is not inside the image");
  }
  
  public int getHeight()
  {
    return mHeight;
  }
  
  public int[] getStrides()
  {
    return mStrides;
  }
  
  public int getWidth()
  {
    return mWidth;
  }
  
  public byte[] getYuvData()
  {
    return mData;
  }
  
  public int getYuvFormat()
  {
    return mFormat;
  }
}
