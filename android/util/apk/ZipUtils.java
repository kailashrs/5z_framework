package android.util.apk;

import android.util.Pair;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

abstract class ZipUtils
{
  private static final int UINT16_MAX_VALUE = 65535;
  private static final int ZIP64_EOCD_LOCATOR_SIG_REVERSE_BYTE_ORDER = 1347094023;
  private static final int ZIP64_EOCD_LOCATOR_SIZE = 20;
  private static final int ZIP_EOCD_CENTRAL_DIR_OFFSET_FIELD_OFFSET = 16;
  private static final int ZIP_EOCD_CENTRAL_DIR_SIZE_FIELD_OFFSET = 12;
  private static final int ZIP_EOCD_COMMENT_LENGTH_FIELD_OFFSET = 20;
  private static final int ZIP_EOCD_REC_MIN_SIZE = 22;
  private static final int ZIP_EOCD_REC_SIG = 101010256;
  
  private ZipUtils() {}
  
  private static void assertByteOrderLittleEndian(ByteBuffer paramByteBuffer)
  {
    if (paramByteBuffer.order() == ByteOrder.LITTLE_ENDIAN) {
      return;
    }
    throw new IllegalArgumentException("ByteBuffer byte order must be little endian");
  }
  
  private static int findZipEndOfCentralDirectoryRecord(ByteBuffer paramByteBuffer)
  {
    assertByteOrderLittleEndian(paramByteBuffer);
    int i = paramByteBuffer.capacity();
    if (i < 22) {
      return -1;
    }
    int j = Math.min(i - 22, 65535);
    for (int k = 0; k <= j; k++)
    {
      int m = i - 22 - k;
      if ((paramByteBuffer.getInt(m) == 101010256) && (getUnsignedInt16(paramByteBuffer, m + 20) == k)) {
        return m;
      }
    }
    return -1;
  }
  
  static Pair<ByteBuffer, Long> findZipEndOfCentralDirectoryRecord(RandomAccessFile paramRandomAccessFile)
    throws IOException
  {
    if (paramRandomAccessFile.length() < 22L) {
      return null;
    }
    Pair localPair = findZipEndOfCentralDirectoryRecord(paramRandomAccessFile, 0);
    if (localPair != null) {
      return localPair;
    }
    return findZipEndOfCentralDirectoryRecord(paramRandomAccessFile, 65535);
  }
  
  private static Pair<ByteBuffer, Long> findZipEndOfCentralDirectoryRecord(RandomAccessFile paramRandomAccessFile, int paramInt)
    throws IOException
  {
    if ((paramInt >= 0) && (paramInt <= 65535))
    {
      long l = paramRandomAccessFile.length();
      if (l < 22L) {
        return null;
      }
      ByteBuffer localByteBuffer = ByteBuffer.allocate(22 + (int)Math.min(paramInt, l - 22L));
      localByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
      l -= localByteBuffer.capacity();
      paramRandomAccessFile.seek(l);
      paramRandomAccessFile.readFully(localByteBuffer.array(), localByteBuffer.arrayOffset(), localByteBuffer.capacity());
      paramInt = findZipEndOfCentralDirectoryRecord(localByteBuffer);
      if (paramInt == -1) {
        return null;
      }
      localByteBuffer.position(paramInt);
      paramRandomAccessFile = localByteBuffer.slice();
      paramRandomAccessFile.order(ByteOrder.LITTLE_ENDIAN);
      return Pair.create(paramRandomAccessFile, Long.valueOf(paramInt + l));
    }
    paramRandomAccessFile = new StringBuilder();
    paramRandomAccessFile.append("maxCommentSize: ");
    paramRandomAccessFile.append(paramInt);
    throw new IllegalArgumentException(paramRandomAccessFile.toString());
  }
  
  private static int getUnsignedInt16(ByteBuffer paramByteBuffer, int paramInt)
  {
    return paramByteBuffer.getShort(paramInt) & 0xFFFF;
  }
  
  private static long getUnsignedInt32(ByteBuffer paramByteBuffer, int paramInt)
  {
    return paramByteBuffer.getInt(paramInt) & 0xFFFFFFFF;
  }
  
  public static long getZipEocdCentralDirectoryOffset(ByteBuffer paramByteBuffer)
  {
    assertByteOrderLittleEndian(paramByteBuffer);
    return getUnsignedInt32(paramByteBuffer, paramByteBuffer.position() + 16);
  }
  
  public static long getZipEocdCentralDirectorySizeBytes(ByteBuffer paramByteBuffer)
  {
    assertByteOrderLittleEndian(paramByteBuffer);
    return getUnsignedInt32(paramByteBuffer, paramByteBuffer.position() + 12);
  }
  
  public static final boolean isZip64EndOfCentralDirectoryLocatorPresent(RandomAccessFile paramRandomAccessFile, long paramLong)
    throws IOException
  {
    paramLong -= 20L;
    boolean bool = false;
    if (paramLong < 0L) {
      return false;
    }
    paramRandomAccessFile.seek(paramLong);
    if (paramRandomAccessFile.readInt() == 1347094023) {
      bool = true;
    }
    return bool;
  }
  
  private static void setUnsignedInt32(ByteBuffer paramByteBuffer, int paramInt, long paramLong)
  {
    if ((paramLong >= 0L) && (paramLong <= 4294967295L))
    {
      paramByteBuffer.putInt(paramByteBuffer.position() + paramInt, (int)paramLong);
      return;
    }
    paramByteBuffer = new StringBuilder();
    paramByteBuffer.append("uint32 value of out range: ");
    paramByteBuffer.append(paramLong);
    throw new IllegalArgumentException(paramByteBuffer.toString());
  }
  
  public static void setZipEocdCentralDirectoryOffset(ByteBuffer paramByteBuffer, long paramLong)
  {
    assertByteOrderLittleEndian(paramByteBuffer);
    setUnsignedInt32(paramByteBuffer, paramByteBuffer.position() + 16, paramLong);
  }
}
