package android.util.apk;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

abstract class ApkVerityBuilder
{
  private static final int CHUNK_SIZE_BYTES = 4096;
  private static final byte[] DEFAULT_SALT = new byte[8];
  private static final int DIGEST_SIZE_BYTES = 32;
  private static final int FSVERITY_HEADER_SIZE_BYTES = 64;
  private static final String JCA_DIGEST_ALGORITHM = "SHA-256";
  private static final int MMAP_REGION_SIZE_BYTES = 1048576;
  private static final int ZIP_EOCD_CENTRAL_DIR_OFFSET_FIELD_OFFSET = 16;
  private static final int ZIP_EOCD_CENTRAL_DIR_OFFSET_FIELD_SIZE = 4;
  
  private ApkVerityBuilder() {}
  
  private static void assertSigningBlockAlignedAndHasFullPages(SignatureInfo paramSignatureInfo)
  {
    if (apkSigningBlockOffset % 4096L == 0L)
    {
      if ((centralDirOffset - apkSigningBlockOffset) % 4096L == 0L) {
        return;
      }
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Size of APK Signing Block is not a multiple of 4096: ");
      localStringBuilder.append(centralDirOffset - apkSigningBlockOffset);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("APK Signing Block does not start at the page  boundary: ");
    localStringBuilder.append(apkSigningBlockOffset);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private static void calculateFsveritySignatureInternal(RandomAccessFile paramRandomAccessFile, SignatureInfo paramSignatureInfo, ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2, ByteBuffer paramByteBuffer3, ByteBuffer paramByteBuffer4)
    throws IOException, NoSuchAlgorithmException, DigestException
  {
    assertSigningBlockAlignedAndHasFullPages(paramSignatureInfo);
    long l = centralDirOffset - apkSigningBlockOffset;
    int[] arrayOfInt = calculateVerityLevelOffset(paramRandomAccessFile.length() - l);
    if (paramByteBuffer1 != null)
    {
      paramByteBuffer1 = generateApkVerityTree(paramRandomAccessFile, paramSignatureInfo, DEFAULT_SALT, arrayOfInt, paramByteBuffer1);
      if (paramByteBuffer2 != null)
      {
        paramByteBuffer2.put(paramByteBuffer1);
        paramByteBuffer2.flip();
      }
    }
    if (paramByteBuffer3 != null)
    {
      paramByteBuffer3.order(ByteOrder.LITTLE_ENDIAN);
      generateFsverityHeader(paramByteBuffer3, paramRandomAccessFile.length(), arrayOfInt.length - 1, DEFAULT_SALT);
    }
    if (paramByteBuffer4 != null)
    {
      paramByteBuffer4.order(ByteOrder.LITTLE_ENDIAN);
      generateFsverityExtensions(paramByteBuffer4, apkSigningBlockOffset, l, eocdOffset);
    }
  }
  
  private static int[] calculateVerityLevelOffset(long paramLong)
  {
    ArrayList localArrayList = new ArrayList();
    for (;;)
    {
      paramLong = divideRoundup(paramLong, 4096L) * 32L;
      localArrayList.add(Long.valueOf(divideRoundup(paramLong, 4096L) * 4096L));
      if (paramLong <= 4096L)
      {
        int[] arrayOfInt = new int[localArrayList.size() + 1];
        int i = 0;
        arrayOfInt[0] = 0;
        while (i < localArrayList.size())
        {
          arrayOfInt[(i + 1)] = (arrayOfInt[i] + Math.toIntExact(((Long)localArrayList.get(localArrayList.size() - i - 1)).longValue()));
          i++;
        }
        return arrayOfInt;
      }
    }
  }
  
  private static void consumeByChunk(DataDigester paramDataDigester, DataSource paramDataSource, int paramInt)
    throws IOException, DigestException
  {
    long l1 = paramDataSource.size();
    long l2 = 0L;
    while (l1 > 0L)
    {
      int i = (int)Math.min(l1, paramInt);
      paramDataSource.feedIntoDataDigester(paramDataDigester, l2, i);
      l2 += i;
      l1 -= i;
    }
  }
  
  private static long divideRoundup(long paramLong1, long paramLong2)
  {
    return (paramLong1 + paramLong2 - 1L) / paramLong2;
  }
  
  static ApkVerityResult generateApkVerity(RandomAccessFile paramRandomAccessFile, SignatureInfo paramSignatureInfo, ByteBufferFactory paramByteBufferFactory)
    throws IOException, SecurityException, NoSuchAlgorithmException, DigestException
  {
    long l1 = centralDirOffset;
    long l2 = apkSigningBlockOffset;
    Object localObject = calculateVerityLevelOffset(paramRandomAccessFile.length() - (l1 - l2));
    int i = localObject[(localObject.length - 1)];
    ByteBuffer localByteBuffer1 = paramByteBufferFactory.create(i + 4096);
    localByteBuffer1.order(ByteOrder.LITTLE_ENDIAN);
    ByteBuffer localByteBuffer2 = slice(localByteBuffer1, 0, i);
    ByteBuffer localByteBuffer3 = slice(localByteBuffer1, i, i + 64);
    localObject = slice(localByteBuffer1, i + 64, i + 4096);
    byte[] arrayOfByte = new byte[32];
    paramByteBufferFactory = ByteBuffer.wrap(arrayOfByte);
    paramByteBufferFactory.order(ByteOrder.LITTLE_ENDIAN);
    calculateFsveritySignatureInternal(paramRandomAccessFile, paramSignatureInfo, localByteBuffer2, paramByteBufferFactory, localByteBuffer3, (ByteBuffer)localObject);
    localByteBuffer1.position(i + 64 + ((ByteBuffer)localObject).limit());
    localByteBuffer1.putInt(64 + ((ByteBuffer)localObject).limit() + 4);
    localByteBuffer1.flip();
    return new ApkVerityResult(localByteBuffer1, arrayOfByte);
  }
  
  private static void generateApkVerityDigestAtLeafLevel(RandomAccessFile paramRandomAccessFile, SignatureInfo paramSignatureInfo, byte[] paramArrayOfByte, ByteBuffer paramByteBuffer)
    throws IOException, NoSuchAlgorithmException, DigestException
  {
    paramArrayOfByte = new BufferedDigester(paramArrayOfByte, paramByteBuffer, null);
    consumeByChunk(paramArrayOfByte, new MemoryMappedFileDataSource(paramRandomAccessFile.getFD(), 0L, apkSigningBlockOffset), 1048576);
    long l = eocdOffset + 16L;
    consumeByChunk(paramArrayOfByte, new MemoryMappedFileDataSource(paramRandomAccessFile.getFD(), centralDirOffset, l - centralDirOffset), 1048576);
    paramByteBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
    paramByteBuffer.putInt(Math.toIntExact(apkSigningBlockOffset));
    paramByteBuffer.flip();
    paramArrayOfByte.consume(paramByteBuffer);
    l = 4L + l;
    consumeByChunk(paramArrayOfByte, new MemoryMappedFileDataSource(paramRandomAccessFile.getFD(), l, paramRandomAccessFile.length() - l), 1048576);
    int i = (int)(paramRandomAccessFile.length() % 4096L);
    if (i != 0) {
      paramArrayOfByte.consume(ByteBuffer.allocate(4096 - i));
    }
    paramArrayOfByte.assertEmptyBuffer();
    paramArrayOfByte.fillUpLastOutputChunk();
  }
  
  private static byte[] generateApkVerityTree(RandomAccessFile paramRandomAccessFile, SignatureInfo paramSignatureInfo, byte[] paramArrayOfByte, int[] paramArrayOfInt, ByteBuffer paramByteBuffer)
    throws IOException, NoSuchAlgorithmException, DigestException
  {
    generateApkVerityDigestAtLeafLevel(paramRandomAccessFile, paramSignatureInfo, paramArrayOfByte, slice(paramByteBuffer, paramArrayOfInt[(paramArrayOfInt.length - 2)], paramArrayOfInt[(paramArrayOfInt.length - 1)]));
    for (int i = paramArrayOfInt.length - 3; i >= 0; i--)
    {
      paramRandomAccessFile = slice(paramByteBuffer, paramArrayOfInt[(i + 1)], paramArrayOfInt[(i + 2)]);
      paramSignatureInfo = slice(paramByteBuffer, paramArrayOfInt[i], paramArrayOfInt[(i + 1)]);
      paramRandomAccessFile = new ByteBufferDataSource(paramRandomAccessFile);
      paramSignatureInfo = new BufferedDigester(paramArrayOfByte, paramSignatureInfo, null);
      consumeByChunk(paramSignatureInfo, paramRandomAccessFile, 4096);
      paramSignatureInfo.assertEmptyBuffer();
      paramSignatureInfo.fillUpLastOutputChunk();
    }
    paramRandomAccessFile = new byte[32];
    paramSignatureInfo = new BufferedDigester(paramArrayOfByte, ByteBuffer.wrap(paramRandomAccessFile), null);
    paramSignatureInfo.consume(slice(paramByteBuffer, 0, 4096));
    paramSignatureInfo.assertEmptyBuffer();
    return paramRandomAccessFile;
  }
  
  private static ByteBuffer generateFsverityExtensions(ByteBuffer paramByteBuffer, long paramLong1, long paramLong2, long paramLong3)
  {
    paramByteBuffer.putInt(24);
    paramByteBuffer.putShort((short)1);
    skip(paramByteBuffer, 2);
    paramByteBuffer.putLong(paramLong1);
    paramByteBuffer.putLong(paramLong2);
    paramByteBuffer.putInt(20);
    paramByteBuffer.putShort((short)2);
    skip(paramByteBuffer, 2);
    paramByteBuffer.putLong(16L + paramLong3);
    paramByteBuffer.putInt(Math.toIntExact(paramLong1));
    int i = 4;
    if (4 == 8) {
      i = 0;
    }
    skip(paramByteBuffer, i);
    paramByteBuffer.flip();
    return paramByteBuffer;
  }
  
  private static ByteBuffer generateFsverityHeader(ByteBuffer paramByteBuffer, long paramLong, int paramInt, byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte.length == 8)
    {
      paramByteBuffer.put("TrueBrew".getBytes());
      paramByteBuffer.put((byte)1);
      paramByteBuffer.put((byte)0);
      paramByteBuffer.put((byte)12);
      paramByteBuffer.put((byte)7);
      paramByteBuffer.putShort((short)1);
      paramByteBuffer.putShort((short)1);
      paramByteBuffer.putInt(0);
      paramByteBuffer.putInt(0);
      paramByteBuffer.putLong(paramLong);
      paramByteBuffer.put((byte)2);
      paramByteBuffer.put((byte)0);
      paramByteBuffer.put(paramArrayOfByte);
      skip(paramByteBuffer, 22);
      paramByteBuffer.flip();
      return paramByteBuffer;
    }
    throw new IllegalArgumentException("salt is not 8 bytes long");
  }
  
  static byte[] generateFsverityRootHash(RandomAccessFile paramRandomAccessFile, ByteBuffer paramByteBuffer, SignatureInfo paramSignatureInfo)
    throws NoSuchAlgorithmException, DigestException, IOException
  {
    ByteBuffer localByteBuffer1 = ByteBuffer.allocate(4096).order(ByteOrder.LITTLE_ENDIAN);
    ByteBuffer localByteBuffer2 = slice(localByteBuffer1, 0, 64);
    localByteBuffer1 = slice(localByteBuffer1, 64, 4032);
    calculateFsveritySignatureInternal(paramRandomAccessFile, paramSignatureInfo, null, null, localByteBuffer2, localByteBuffer1);
    paramRandomAccessFile = MessageDigest.getInstance("SHA-256");
    paramRandomAccessFile.update(localByteBuffer2);
    paramRandomAccessFile.update(localByteBuffer1);
    paramRandomAccessFile.update(paramByteBuffer);
    return paramRandomAccessFile.digest();
  }
  
  private static void skip(ByteBuffer paramByteBuffer, int paramInt)
  {
    paramByteBuffer.position(paramByteBuffer.position() + paramInt);
  }
  
  private static ByteBuffer slice(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2)
  {
    paramByteBuffer = paramByteBuffer.duplicate();
    paramByteBuffer.position(0);
    paramByteBuffer.limit(paramInt2);
    paramByteBuffer.position(paramInt1);
    return paramByteBuffer.slice();
  }
  
  static class ApkVerityResult
  {
    public final ByteBuffer fsverityData;
    public final byte[] rootHash;
    
    ApkVerityResult(ByteBuffer paramByteBuffer, byte[] paramArrayOfByte)
    {
      fsverityData = paramByteBuffer;
      rootHash = paramArrayOfByte;
    }
  }
  
  private static class BufferedDigester
    implements DataDigester
  {
    private static final int BUFFER_SIZE = 4096;
    private int mBytesDigestedSinceReset;
    private final byte[] mDigestBuffer = new byte[32];
    private final MessageDigest mMd;
    private final ByteBuffer mOutput;
    private final byte[] mSalt;
    
    private BufferedDigester(byte[] paramArrayOfByte, ByteBuffer paramByteBuffer)
      throws NoSuchAlgorithmException
    {
      mSalt = paramArrayOfByte;
      mOutput = paramByteBuffer.slice();
      mMd = MessageDigest.getInstance("SHA-256");
      mMd.update(mSalt);
      mBytesDigestedSinceReset = 0;
    }
    
    private void fillUpLastOutputChunk()
    {
      int i = mOutput.position() % 4096;
      if (i == 0) {
        return;
      }
      mOutput.put(ByteBuffer.allocate(4096 - i));
    }
    
    public void assertEmptyBuffer()
      throws DigestException
    {
      if (mBytesDigestedSinceReset == 0) {
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Buffer is not empty: ");
      localStringBuilder.append(mBytesDigestedSinceReset);
      throw new IllegalStateException(localStringBuilder.toString());
    }
    
    public void consume(ByteBuffer paramByteBuffer)
      throws DigestException
    {
      int i = paramByteBuffer.position();
      int j = paramByteBuffer.remaining();
      while (j > 0)
      {
        int k = Math.min(j, 4096 - mBytesDigestedSinceReset);
        paramByteBuffer.limit(paramByteBuffer.position() + k);
        mMd.update(paramByteBuffer);
        i += k;
        j -= k;
        mBytesDigestedSinceReset += k;
        if (mBytesDigestedSinceReset == 4096)
        {
          mMd.digest(mDigestBuffer, 0, mDigestBuffer.length);
          mOutput.put(mDigestBuffer);
          mMd.update(mSalt);
          mBytesDigestedSinceReset = 0;
        }
      }
    }
  }
}
