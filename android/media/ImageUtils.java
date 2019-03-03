package android.media;

import android.util.Size;
import java.nio.ByteBuffer;
import libcore.io.Memory;

class ImageUtils
{
  ImageUtils() {}
  
  private static void directByteBufferCopy(ByteBuffer paramByteBuffer1, int paramInt1, ByteBuffer paramByteBuffer2, int paramInt2, int paramInt3)
  {
    Memory.memmove(paramByteBuffer2, paramInt2, paramByteBuffer1, paramInt1, paramInt3);
  }
  
  private static Size getEffectivePlaneSizeForImage(Image paramImage, int paramInt)
  {
    switch (paramImage.getFormat())
    {
    default: 
      throw new UnsupportedOperationException(String.format("Invalid image format %d", new Object[] { Integer.valueOf(paramImage.getFormat()) }));
    case 34: 
      return new Size(0, 0);
    case 17: 
    case 35: 
    case 842094169: 
      if (paramInt == 0) {
        return new Size(paramImage.getWidth(), paramImage.getHeight());
      }
      return new Size(paramImage.getWidth() / 2, paramImage.getHeight() / 2);
    case 16: 
      if (paramInt == 0) {
        return new Size(paramImage.getWidth(), paramImage.getHeight());
      }
      return new Size(paramImage.getWidth(), paramImage.getHeight() / 2);
    }
    return new Size(paramImage.getWidth(), paramImage.getHeight());
  }
  
  public static int getEstimatedNativeAllocBytes(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    double d;
    switch (paramInt3)
    {
    default: 
      throw new UnsupportedOperationException(String.format("Invalid format specified %d", new Object[] { Integer.valueOf(paramInt3) }));
    case 538982489: 
      d = 1.0D;
      break;
    case 256: 
    case 257: 
      d = 0.3D;
      break;
    case 37: 
      d = 1.25D;
      break;
    case 17: 
    case 34: 
    case 35: 
    case 38: 
    case 842094169: 
      d = 1.5D;
      break;
    case 4: 
    case 16: 
    case 20: 
    case 32: 
    case 36: 
    case 4098: 
    case 540422489: 
    case 1144402265: 
      d = 2.0D;
      break;
    case 3: 
      d = 3.0D;
      break;
    case 1: 
    case 2: 
      d = 4.0D;
    }
    return (int)(paramInt1 * paramInt2 * d * paramInt4);
  }
  
  public static int getNumPlanesForFormat(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      throw new UnsupportedOperationException(String.format("Invalid format specified %d", new Object[] { Integer.valueOf(paramInt) }));
    case 34: 
      return 0;
    case 17: 
    case 35: 
    case 842094169: 
      return 3;
    case 16: 
      return 2;
    }
    return 1;
  }
  
  public static void imageCopy(Image paramImage1, Image paramImage2)
  {
    if ((paramImage1 != null) && (paramImage2 != null))
    {
      if (paramImage1.getFormat() == paramImage2.getFormat())
      {
        if ((paramImage1.getFormat() != 34) && (paramImage2.getFormat() != 34))
        {
          if (paramImage1.getFormat() != 36)
          {
            if (paramImage1.getFormat() != 4098)
            {
              if ((paramImage2.getOwner() instanceof ImageWriter))
              {
                Object localObject1 = new Size(paramImage1.getWidth(), paramImage1.getHeight());
                Object localObject2 = new Size(paramImage2.getWidth(), paramImage2.getHeight());
                if (((Size)localObject1).equals(localObject2))
                {
                  localObject1 = paramImage1.getPlanes();
                  paramImage2 = paramImage2.getPlanes();
                  int i = 0;
                  while (i < localObject1.length)
                  {
                    int j = localObject1[i].getRowStride();
                    int k = paramImage2[i].getRowStride();
                    ByteBuffer localByteBuffer = localObject1[i].getBuffer();
                    localObject2 = paramImage2[i].getBuffer();
                    if ((localByteBuffer.isDirect()) && (((ByteBuffer)localObject2).isDirect()))
                    {
                      if (localObject1[i].getPixelStride() == paramImage2[i].getPixelStride())
                      {
                        int m = localByteBuffer.position();
                        localByteBuffer.rewind();
                        ((ByteBuffer)localObject2).rewind();
                        if (j == k)
                        {
                          ((ByteBuffer)localObject2).put(localByteBuffer);
                        }
                        else
                        {
                          int n = localByteBuffer.position();
                          int i1 = ((ByteBuffer)localObject2).position();
                          Size localSize = getEffectivePlaneSizeForImage(paramImage1, i);
                          int i2 = localSize.getWidth() * localObject1[i].getPixelStride();
                          int i3 = 0;
                          while (i3 < localSize.getHeight())
                          {
                            int i4 = i2;
                            if (i3 == localSize.getHeight() - 1)
                            {
                              int i5 = localByteBuffer.remaining() - n;
                              i4 = i2;
                              if (i2 > i5) {
                                i4 = i5;
                              }
                            }
                            directByteBufferCopy(localByteBuffer, n, (ByteBuffer)localObject2, i1, i4);
                            n += j;
                            i1 += k;
                            i3++;
                            i2 = i4;
                          }
                        }
                        localByteBuffer.position(m);
                        ((ByteBuffer)localObject2).rewind();
                        i++;
                      }
                      else
                      {
                        paramImage1 = new StringBuilder();
                        paramImage1.append("Source plane image pixel stride ");
                        paramImage1.append(localObject1[i].getPixelStride());
                        paramImage1.append(" must be same as destination image pixel stride ");
                        paramImage1.append(paramImage2[i].getPixelStride());
                        throw new IllegalArgumentException(paramImage1.toString());
                      }
                    }
                    else {
                      throw new IllegalArgumentException("Source and destination ByteBuffers must be direct byteBuffer!");
                    }
                  }
                  return;
                }
                paramImage1 = new StringBuilder();
                paramImage1.append("source image size ");
                paramImage1.append(localObject1);
                paramImage1.append(" is different with destination image size ");
                paramImage1.append(localObject2);
                throw new IllegalArgumentException(paramImage1.toString());
              }
              throw new IllegalArgumentException("Destination image is not from ImageWriter. Only the images from ImageWriter are writable");
            }
            throw new IllegalArgumentException("Copy of RAW_DEPTH format has not been implemented");
          }
          throw new IllegalArgumentException("Copy of RAW_OPAQUE format has not been implemented");
        }
        throw new IllegalArgumentException("PRIVATE format images are not copyable");
      }
      throw new IllegalArgumentException("Src and dst images should have the same format");
    }
    throw new IllegalArgumentException("Images should be non-null");
  }
}
