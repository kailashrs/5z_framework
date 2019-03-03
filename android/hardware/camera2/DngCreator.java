package android.hardware.camera2;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.location.Location;
import android.media.Image;
import android.media.Image.Plane;
import android.os.SystemClock;
import android.util.Log;
import android.util.Size;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public final class DngCreator
  implements AutoCloseable
{
  private static final int BYTES_PER_RGB_PIX = 3;
  private static final int DEFAULT_PIXEL_STRIDE = 2;
  private static final String GPS_DATE_FORMAT_STR = "yyyy:MM:dd";
  private static final String GPS_LAT_REF_NORTH = "N";
  private static final String GPS_LAT_REF_SOUTH = "S";
  private static final String GPS_LONG_REF_EAST = "E";
  private static final String GPS_LONG_REF_WEST = "W";
  public static final int MAX_THUMBNAIL_DIMENSION = 256;
  private static final String TAG = "DngCreator";
  private static final int TAG_ORIENTATION_UNKNOWN = 9;
  private static final String TIFF_DATETIME_FORMAT = "yyyy:MM:dd HH:mm:ss";
  private static final DateFormat sExifGPSDateStamp = new SimpleDateFormat("yyyy:MM:dd", Locale.US);
  private final Calendar mGPSTimeStampCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
  private long mNativeContext;
  
  static
  {
    sExifGPSDateStamp.setTimeZone(TimeZone.getTimeZone("UTC"));
    nativeClassInit();
  }
  
  public DngCreator(CameraCharacteristics paramCameraCharacteristics, CaptureResult paramCaptureResult)
  {
    if ((paramCameraCharacteristics != null) && (paramCaptureResult != null))
    {
      long l1 = System.currentTimeMillis();
      int i = ((Integer)paramCameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_TIMESTAMP_SOURCE)).intValue();
      long l2;
      if (i == 1)
      {
        l2 = l1 - SystemClock.elapsedRealtime();
      }
      else if (i == 0)
      {
        l2 = l1 - SystemClock.uptimeMillis();
      }
      else
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Sensor timestamp source is unexpected: ");
        ((StringBuilder)localObject).append(i);
        Log.w("DngCreator", ((StringBuilder)localObject).toString());
        l2 = l1 - SystemClock.uptimeMillis();
      }
      Object localObject = (Long)paramCaptureResult.get(CaptureResult.SENSOR_TIMESTAMP);
      if (localObject != null) {
        l1 = ((Long)localObject).longValue() / 1000000L + l2;
      }
      localObject = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.US);
      ((DateFormat)localObject).setTimeZone(TimeZone.getDefault());
      localObject = ((DateFormat)localObject).format(Long.valueOf(l1));
      nativeInit(paramCameraCharacteristics.getNativeCopy(), paramCaptureResult.getNativeCopy(), (String)localObject);
      return;
    }
    throw new IllegalArgumentException("Null argument to DngCreator constructor");
  }
  
  private static void colorToRgb(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    paramArrayOfByte[paramInt2] = ((byte)(byte)Color.red(paramInt1));
    paramArrayOfByte[(paramInt2 + 1)] = ((byte)(byte)Color.green(paramInt1));
    paramArrayOfByte[(paramInt2 + 2)] = ((byte)(byte)Color.blue(paramInt1));
  }
  
  private static ByteBuffer convertToRGB(Bitmap paramBitmap)
  {
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    ByteBuffer localByteBuffer = ByteBuffer.allocateDirect(3 * i * j);
    int[] arrayOfInt = new int[i];
    byte[] arrayOfByte = new byte[3 * i];
    for (int k = 0; k < j; k++)
    {
      paramBitmap.getPixels(arrayOfInt, 0, i, 0, k, i, 1);
      for (int m = 0; m < i; m++) {
        colorToRgb(arrayOfInt[m], m * 3, arrayOfByte);
      }
      localByteBuffer.put(arrayOfByte);
    }
    localByteBuffer.rewind();
    return localByteBuffer;
  }
  
  private static ByteBuffer convertToRGB(Image paramImage)
  {
    int i = paramImage.getWidth();
    int j = paramImage.getHeight();
    ByteBuffer localByteBuffer1 = ByteBuffer.allocateDirect(3 * i * j);
    Object localObject = paramImage.getPlanes()[0];
    Image.Plane localPlane = paramImage.getPlanes()[1];
    paramImage = paramImage.getPlanes()[2];
    ByteBuffer localByteBuffer2 = ((Image.Plane)localObject).getBuffer();
    ByteBuffer localByteBuffer3 = localPlane.getBuffer();
    ByteBuffer localByteBuffer4 = paramImage.getBuffer();
    localByteBuffer2.rewind();
    localByteBuffer3.rewind();
    localByteBuffer4.rewind();
    int k = ((Image.Plane)localObject).getRowStride();
    int m = paramImage.getRowStride();
    int n = localPlane.getRowStride();
    int i1 = ((Image.Plane)localObject).getPixelStride();
    int i2 = paramImage.getPixelStride();
    int i3 = localPlane.getPixelStride();
    byte[] arrayOfByte1 = new byte[3];
    byte[] tmp127_125 = arrayOfByte1;
    tmp127_125[0] = 0;
    byte[] tmp132_127 = tmp127_125;
    tmp132_127[1] = 0;
    byte[] tmp137_132 = tmp132_127;
    tmp137_132[2] = 0;
    tmp137_132;
    localObject = new byte[(i - 1) * i1 + 1];
    byte[] arrayOfByte2 = new byte[(i / 2 - 1) * i3 + 1];
    byte[] arrayOfByte3 = new byte[(i / 2 - 1) * i2 + 1];
    byte[] arrayOfByte4 = new byte[3 * i];
    int i4 = 0;
    paramImage = localPlane;
    while (i4 < j)
    {
      int i5 = i4 / 2;
      localByteBuffer2.position(k * i4);
      localByteBuffer2.get((byte[])localObject);
      localByteBuffer3.position(n * i5);
      localByteBuffer3.get(arrayOfByte2);
      localByteBuffer4.position(m * i5);
      localByteBuffer4.get(arrayOfByte3);
      for (i5 = 0; i5 < i; i5++)
      {
        int i6 = i5 / 2;
        arrayOfByte1[0] = ((byte)localObject[(i1 * i5)]);
        arrayOfByte1[1] = ((byte)arrayOfByte2[(i3 * i6)]);
        arrayOfByte1[2] = ((byte)arrayOfByte3[(i2 * i6)]);
        yuvToRgb(arrayOfByte1, i5 * 3, arrayOfByte4);
      }
      localByteBuffer1.put(arrayOfByte4);
      i4++;
    }
    localByteBuffer2.rewind();
    localByteBuffer3.rewind();
    localByteBuffer4.rewind();
    localByteBuffer1.rewind();
    return localByteBuffer1;
  }
  
  private static native void nativeClassInit();
  
  private synchronized native void nativeDestroy();
  
  private synchronized native void nativeInit(CameraMetadataNative paramCameraMetadataNative1, CameraMetadataNative paramCameraMetadataNative2, String paramString);
  
  private synchronized native void nativeSetDescription(String paramString);
  
  private synchronized native void nativeSetGpsTags(int[] paramArrayOfInt1, String paramString1, int[] paramArrayOfInt2, String paramString2, String paramString3, int[] paramArrayOfInt3);
  
  private synchronized native void nativeSetOrientation(int paramInt);
  
  private synchronized native void nativeSetThumbnail(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2);
  
  private synchronized native void nativeWriteImage(OutputStream paramOutputStream, int paramInt1, int paramInt2, ByteBuffer paramByteBuffer, int paramInt3, int paramInt4, long paramLong, boolean paramBoolean)
    throws IOException;
  
  private synchronized native void nativeWriteInputStream(OutputStream paramOutputStream, InputStream paramInputStream, int paramInt1, int paramInt2, long paramLong)
    throws IOException;
  
  private static int[] toExifLatLong(double paramDouble)
  {
    paramDouble = Math.abs(paramDouble);
    int i = (int)paramDouble;
    paramDouble = (paramDouble - i) * 60.0D;
    int j = (int)paramDouble;
    return new int[] { i, 1, j, 1, (int)((paramDouble - j) * 6000.0D), 100 };
  }
  
  private void writeByteBuffer(int paramInt1, int paramInt2, ByteBuffer paramByteBuffer, OutputStream paramOutputStream, int paramInt3, int paramInt4, long paramLong)
    throws IOException
  {
    if ((paramInt1 > 0) && (paramInt2 > 0))
    {
      long l1 = paramByteBuffer.capacity();
      long l2 = paramInt4 * paramInt2 + paramLong;
      if (l1 >= l2)
      {
        int i = paramInt3 * paramInt1;
        if (i <= paramInt4)
        {
          paramByteBuffer.clear();
          nativeWriteImage(paramOutputStream, paramInt1, paramInt2, paramByteBuffer, paramInt4, paramInt3, paramLong, paramByteBuffer.isDirect());
          paramByteBuffer.clear();
          return;
        }
        paramByteBuffer = new StringBuilder();
        paramByteBuffer.append("Invalid image pixel stride, row byte width ");
        paramByteBuffer.append(i);
        paramByteBuffer.append(" is too large, expecting ");
        paramByteBuffer.append(paramInt4);
        throw new IllegalArgumentException(paramByteBuffer.toString());
      }
      paramByteBuffer = new StringBuilder();
      paramByteBuffer.append("Image size ");
      paramByteBuffer.append(l1);
      paramByteBuffer.append(" is too small (must be larger than ");
      paramByteBuffer.append(l2);
      paramByteBuffer.append(")");
      throw new IllegalArgumentException(paramByteBuffer.toString());
    }
    paramByteBuffer = new StringBuilder();
    paramByteBuffer.append("Image with invalid width, height: (");
    paramByteBuffer.append(paramInt1);
    paramByteBuffer.append(",");
    paramByteBuffer.append(paramInt2);
    paramByteBuffer.append(") passed to write");
    throw new IllegalArgumentException(paramByteBuffer.toString());
  }
  
  private static void yuvToRgb(byte[] paramArrayOfByte1, int paramInt, byte[] paramArrayOfByte2)
  {
    float f1 = paramArrayOfByte1[0] & 0xFF;
    float f2 = paramArrayOfByte1[1] & 0xFF;
    float f3 = paramArrayOfByte1[2] & 0xFF;
    paramArrayOfByte2[paramInt] = ((byte)(byte)(int)Math.max(0.0F, Math.min(255.0F, 1.402F * (f3 - 128.0F) + f1)));
    paramArrayOfByte2[(paramInt + 1)] = ((byte)(byte)(int)Math.max(0.0F, Math.min(255.0F, f1 - 0.34414F * (f2 - 128.0F) - 0.71414F * (f3 - 128.0F))));
    paramArrayOfByte2[(paramInt + 2)] = ((byte)(byte)(int)Math.max(0.0F, Math.min(255.0F, 1.772F * (f2 - 128.0F) + f1)));
  }
  
  public void close()
  {
    nativeDestroy();
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      close();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public DngCreator setDescription(String paramString)
  {
    if (paramString != null)
    {
      nativeSetDescription(paramString);
      return this;
    }
    throw new IllegalArgumentException("Null description passed to setDescription.");
  }
  
  public DngCreator setLocation(Location paramLocation)
  {
    if (paramLocation != null)
    {
      double d1 = paramLocation.getLatitude();
      double d2 = paramLocation.getLongitude();
      long l = paramLocation.getTime();
      int[] arrayOfInt1 = toExifLatLong(d1);
      int[] arrayOfInt2 = toExifLatLong(d2);
      if (d1 >= 0.0D) {
        paramLocation = "N";
      } else {
        paramLocation = "S";
      }
      if (d2 >= 0.0D) {}
      for (String str1 = "E";; str1 = "W") {
        break;
      }
      String str2 = sExifGPSDateStamp.format(Long.valueOf(l));
      mGPSTimeStampCalendar.setTimeInMillis(l);
      nativeSetGpsTags(arrayOfInt1, paramLocation, arrayOfInt2, str1, str2, new int[] { mGPSTimeStampCalendar.get(11), 1, mGPSTimeStampCalendar.get(12), 1, mGPSTimeStampCalendar.get(13), 1 });
      return this;
    }
    throw new IllegalArgumentException("Null location passed to setLocation");
  }
  
  public DngCreator setOrientation(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= 8))
    {
      int i = paramInt;
      if (paramInt == 0) {
        i = 9;
      }
      nativeSetOrientation(i);
      return this;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Orientation ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append(" is not a valid EXIF orientation value");
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public DngCreator setThumbnail(Bitmap paramBitmap)
  {
    if (paramBitmap != null)
    {
      int i = paramBitmap.getWidth();
      int j = paramBitmap.getHeight();
      if ((i <= 256) && (j <= 256))
      {
        nativeSetThumbnail(convertToRGB(paramBitmap), i, j);
        return this;
      }
      paramBitmap = new StringBuilder();
      paramBitmap.append("Thumbnail dimensions width,height (");
      paramBitmap.append(i);
      paramBitmap.append(",");
      paramBitmap.append(j);
      paramBitmap.append(") too large, dimensions must be smaller than ");
      paramBitmap.append(256);
      throw new IllegalArgumentException(paramBitmap.toString());
    }
    throw new IllegalArgumentException("Null argument to setThumbnail");
  }
  
  public DngCreator setThumbnail(Image paramImage)
  {
    if (paramImage != null)
    {
      int i = paramImage.getFormat();
      if (i == 35)
      {
        int j = paramImage.getWidth();
        i = paramImage.getHeight();
        if ((j <= 256) && (i <= 256))
        {
          nativeSetThumbnail(convertToRGB(paramImage), j, i);
          return this;
        }
        paramImage = new StringBuilder();
        paramImage.append("Thumbnail dimensions width,height (");
        paramImage.append(j);
        paramImage.append(",");
        paramImage.append(i);
        paramImage.append(") too large, dimensions must be smaller than ");
        paramImage.append(256);
        throw new IllegalArgumentException(paramImage.toString());
      }
      paramImage = new StringBuilder();
      paramImage.append("Unsupported Image format ");
      paramImage.append(i);
      throw new IllegalArgumentException(paramImage.toString());
    }
    throw new IllegalArgumentException("Null argument to setThumbnail");
  }
  
  public void writeByteBuffer(OutputStream paramOutputStream, Size paramSize, ByteBuffer paramByteBuffer, long paramLong)
    throws IOException
  {
    if (paramOutputStream != null)
    {
      if (paramSize != null)
      {
        if (paramByteBuffer != null)
        {
          if (paramLong >= 0L)
          {
            int i = paramSize.getWidth();
            writeByteBuffer(i, paramSize.getHeight(), paramByteBuffer, paramOutputStream, 2, i * 2, paramLong);
            return;
          }
          throw new IllegalArgumentException("Negative offset passed to writeByteBuffer");
        }
        throw new IllegalArgumentException("Null pixels passed to writeByteBuffer");
      }
      throw new IllegalArgumentException("Null size passed to writeByteBuffer");
    }
    throw new IllegalArgumentException("Null dngOutput passed to writeByteBuffer");
  }
  
  public void writeImage(OutputStream paramOutputStream, Image paramImage)
    throws IOException
  {
    if (paramOutputStream != null)
    {
      if (paramImage != null)
      {
        int i = paramImage.getFormat();
        if (i == 32)
        {
          Image.Plane[] arrayOfPlane = paramImage.getPlanes();
          if ((arrayOfPlane != null) && (arrayOfPlane.length > 0))
          {
            ByteBuffer localByteBuffer = arrayOfPlane[0].getBuffer();
            writeByteBuffer(paramImage.getWidth(), paramImage.getHeight(), localByteBuffer, paramOutputStream, arrayOfPlane[0].getPixelStride(), arrayOfPlane[0].getRowStride(), 0L);
            return;
          }
          throw new IllegalArgumentException("Image with no planes passed to writeImage");
        }
        paramOutputStream = new StringBuilder();
        paramOutputStream.append("Unsupported image format ");
        paramOutputStream.append(i);
        throw new IllegalArgumentException(paramOutputStream.toString());
      }
      throw new IllegalArgumentException("Null pixels to writeImage");
    }
    throw new IllegalArgumentException("Null dngOutput to writeImage");
  }
  
  public void writeInputStream(OutputStream paramOutputStream, Size paramSize, InputStream paramInputStream, long paramLong)
    throws IOException
  {
    if (paramOutputStream != null)
    {
      if (paramSize != null)
      {
        if (paramInputStream != null)
        {
          if (paramLong >= 0L)
          {
            int i = paramSize.getWidth();
            int j = paramSize.getHeight();
            if ((i > 0) && (j > 0))
            {
              nativeWriteInputStream(paramOutputStream, paramInputStream, i, j, paramLong);
              return;
            }
            paramOutputStream = new StringBuilder();
            paramOutputStream.append("Size with invalid width, height: (");
            paramOutputStream.append(i);
            paramOutputStream.append(",");
            paramOutputStream.append(j);
            paramOutputStream.append(") passed to writeInputStream");
            throw new IllegalArgumentException(paramOutputStream.toString());
          }
          throw new IllegalArgumentException("Negative offset passed to writeInputStream");
        }
        throw new IllegalArgumentException("Null pixels passed to writeInputStream");
      }
      throw new IllegalArgumentException("Null size passed to writeInputStream");
    }
    throw new IllegalArgumentException("Null dngOutput passed to writeInputStream");
  }
}
