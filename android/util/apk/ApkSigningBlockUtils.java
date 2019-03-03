package android.util.apk;

import [B;
import android.util.ArrayMap;
import android.util.Pair;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

final class ApkSigningBlockUtils
{
  private static final long APK_SIG_BLOCK_MAGIC_HI = 3617552046287187010L;
  private static final long APK_SIG_BLOCK_MAGIC_LO = 2334950737559900225L;
  private static final int APK_SIG_BLOCK_MIN_SIZE = 32;
  private static final int CHUNK_SIZE_BYTES = 1048576;
  static final int CONTENT_DIGEST_CHUNKED_SHA256 = 1;
  static final int CONTENT_DIGEST_CHUNKED_SHA512 = 2;
  static final int CONTENT_DIGEST_VERITY_CHUNKED_SHA256 = 3;
  static final int SIGNATURE_DSA_WITH_SHA256 = 769;
  static final int SIGNATURE_ECDSA_WITH_SHA256 = 513;
  static final int SIGNATURE_ECDSA_WITH_SHA512 = 514;
  static final int SIGNATURE_RSA_PKCS1_V1_5_WITH_SHA256 = 259;
  static final int SIGNATURE_RSA_PKCS1_V1_5_WITH_SHA512 = 260;
  static final int SIGNATURE_RSA_PSS_WITH_SHA256 = 257;
  static final int SIGNATURE_RSA_PSS_WITH_SHA512 = 258;
  static final int SIGNATURE_VERITY_DSA_WITH_SHA256 = 1061;
  static final int SIGNATURE_VERITY_ECDSA_WITH_SHA256 = 1059;
  static final int SIGNATURE_VERITY_RSA_PKCS1_V1_5_WITH_SHA256 = 1057;
  
  private ApkSigningBlockUtils() {}
  
  private static void checkByteOrderLittleEndian(ByteBuffer paramByteBuffer)
  {
    if (paramByteBuffer.order() == ByteOrder.LITTLE_ENDIAN) {
      return;
    }
    throw new IllegalArgumentException("ByteBuffer byte order must be little endian");
  }
  
  private static int compareContentDigestAlgorithm(int paramInt1, int paramInt2)
  {
    StringBuilder localStringBuilder;
    switch (paramInt1)
    {
    default: 
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown digestAlgorithm1: ");
      localStringBuilder.append(paramInt1);
      throw new IllegalArgumentException(localStringBuilder.toString());
    case 3: 
      switch (paramInt2)
      {
      default: 
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unknown digestAlgorithm2: ");
        localStringBuilder.append(paramInt2);
        throw new IllegalArgumentException(localStringBuilder.toString());
      case 3: 
        return 0;
      case 2: 
        return -1;
      }
      return 1;
    case 2: 
      switch (paramInt2)
      {
      default: 
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unknown digestAlgorithm2: ");
        localStringBuilder.append(paramInt2);
        throw new IllegalArgumentException(localStringBuilder.toString());
      case 2: 
        return 0;
      }
      return 1;
    }
    switch (paramInt2)
    {
    default: 
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown digestAlgorithm2: ");
      localStringBuilder.append(paramInt2);
      throw new IllegalArgumentException(localStringBuilder.toString());
    case 2: 
    case 3: 
      return -1;
    }
    return 0;
  }
  
  static int compareSignatureAlgorithm(int paramInt1, int paramInt2)
  {
    return compareContentDigestAlgorithm(getSignatureAlgorithmContentDigestAlgorithm(paramInt1), getSignatureAlgorithmContentDigestAlgorithm(paramInt2));
  }
  
  private static byte[][] computeContentDigestsPer1MbChunk(int[] paramArrayOfInt, DataSource[] paramArrayOfDataSource)
    throws DigestException
  {
    Object localObject1 = paramArrayOfDataSource;
    int i = localObject1.length;
    long l1 = 0L;
    for (int j = 0; j < i; j++) {
      l1 += getChunkCount(localObject1[j].size());
    }
    if (l1 < 2097151L)
    {
      int k = (int)l1;
      byte[][] arrayOfByte = new byte[paramArrayOfInt.length][];
      for (j = 0; j < paramArrayOfInt.length; j++)
      {
        localObject2 = new byte[5 + k * getContentDigestAlgorithmOutputSizeBytes(paramArrayOfInt[j])];
        localObject2[0] = ((byte)90);
        setUnsignedInt32LittleEndian(k, (byte[])localObject2, 1);
        arrayOfByte[j] = localObject2;
      }
      byte[] arrayOfByte1 = new byte[5];
      arrayOfByte1[0] = ((byte)-91);
      int m = 0;
      Object localObject2 = new MessageDigest[paramArrayOfInt.length];
      j = 0;
      while (j < paramArrayOfInt.length)
      {
        localObject3 = getContentDigestAlgorithmJcaDigestAlgorithm(paramArrayOfInt[j]);
        try
        {
          localObject2[j] = MessageDigest.getInstance((String)localObject3);
          j++;
        }
        catch (NoSuchAlgorithmException paramArrayOfInt)
        {
          paramArrayOfDataSource = new StringBuilder();
          paramArrayOfDataSource.append((String)localObject3);
          paramArrayOfDataSource.append(" digest not supported");
          throw new RuntimeException(paramArrayOfDataSource.toString(), paramArrayOfInt);
        }
      }
      Object localObject3 = new MultipleDigestDataDigester((MessageDigest[])localObject2);
      j = localObject1.length;
      i = 0;
      int n = 0;
      localObject1 = localObject3;
      while (n < j)
      {
        localObject3 = paramArrayOfDataSource[n];
        long l2 = ((DataSource)localObject3).size();
        long l3 = 0L;
        while (l2 > 0L)
        {
          int i1 = (int)Math.min(l2, 1048576L);
          setUnsignedInt32LittleEndian(i1, arrayOfByte1, 1);
          for (int i2 = 0; i2 < localObject2.length; i2++) {
            localObject2[i2].update(arrayOfByte1);
          }
          try
          {
            ((DataSource)localObject3).feedIntoDataDigester((DataDigester)localObject1, l3, i1);
            i2 = 0;
            while (i2 < paramArrayOfInt.length)
            {
              int i3 = paramArrayOfInt[i2];
              [B local[B = arrayOfByte[i2];
              int i4 = getContentDigestAlgorithmOutputSizeBytes(i3);
              Object localObject4 = localObject2[i2];
              i3 = localObject4.digest(local[B, 5 + m * i4, i4);
              if (i3 == i4)
              {
                i2++;
              }
              else
              {
                paramArrayOfInt = new StringBuilder();
                paramArrayOfInt.append("Unexpected output size of ");
                paramArrayOfInt.append(localObject4.getAlgorithm());
                paramArrayOfInt.append(" digest: ");
                paramArrayOfInt.append(i3);
                throw new RuntimeException(paramArrayOfInt.toString());
              }
            }
            l3 += i1;
            l2 -= i1;
            m++;
          }
          catch (IOException paramArrayOfInt)
          {
            paramArrayOfDataSource = new StringBuilder();
            paramArrayOfDataSource.append("Failed to digest chunk #");
            paramArrayOfDataSource.append(m);
            paramArrayOfDataSource.append(" of section #");
            paramArrayOfDataSource.append(i);
            throw new DigestException(paramArrayOfDataSource.toString(), paramArrayOfInt);
          }
        }
        i++;
        n++;
      }
      localObject3 = new byte[paramArrayOfInt.length][];
      j = 0;
      while (j < paramArrayOfInt.length)
      {
        i = paramArrayOfInt[j];
        localObject2 = arrayOfByte[j];
        paramArrayOfDataSource = getContentDigestAlgorithmJcaDigestAlgorithm(i);
        try
        {
          localObject1 = MessageDigest.getInstance(paramArrayOfDataSource);
          localObject3[j] = ((MessageDigest)localObject1).digest((byte[])localObject2);
          j++;
        }
        catch (NoSuchAlgorithmException paramArrayOfInt)
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append(paramArrayOfDataSource);
          ((StringBuilder)localObject2).append(" digest not supported");
          throw new RuntimeException(((StringBuilder)localObject2).toString(), paramArrayOfInt);
        }
      }
      return localObject3;
    }
    paramArrayOfInt = new StringBuilder();
    paramArrayOfInt.append("Too many chunks: ");
    paramArrayOfInt.append(l1);
    throw new DigestException(paramArrayOfInt.toString());
  }
  
  static ByteBuffer findApkSignatureSchemeBlock(ByteBuffer paramByteBuffer, int paramInt)
    throws SignatureNotFoundException
  {
    checkByteOrderLittleEndian(paramByteBuffer);
    paramByteBuffer = sliceFromTo(paramByteBuffer, 8, paramByteBuffer.capacity() - 24);
    int i = 0;
    while (paramByteBuffer.hasRemaining())
    {
      i++;
      if (paramByteBuffer.remaining() >= 8)
      {
        long l = paramByteBuffer.getLong();
        if ((l >= 4L) && (l <= 2147483647L))
        {
          int j = (int)l;
          int k = paramByteBuffer.position();
          if (j <= paramByteBuffer.remaining())
          {
            if (paramByteBuffer.getInt() == paramInt) {
              return getByteBuffer(paramByteBuffer, j - 4);
            }
            paramByteBuffer.position(k + j);
          }
          else
          {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("APK Signing Block entry #");
            localStringBuilder.append(i);
            localStringBuilder.append(" size out of range: ");
            localStringBuilder.append(j);
            localStringBuilder.append(", available: ");
            localStringBuilder.append(paramByteBuffer.remaining());
            throw new SignatureNotFoundException(localStringBuilder.toString());
          }
        }
        else
        {
          paramByteBuffer = new StringBuilder();
          paramByteBuffer.append("APK Signing Block entry #");
          paramByteBuffer.append(i);
          paramByteBuffer.append(" size out of range: ");
          paramByteBuffer.append(l);
          throw new SignatureNotFoundException(paramByteBuffer.toString());
        }
      }
      else
      {
        paramByteBuffer = new StringBuilder();
        paramByteBuffer.append("Insufficient data to read size of APK Signing Block entry #");
        paramByteBuffer.append(i);
        throw new SignatureNotFoundException(paramByteBuffer.toString());
      }
    }
    paramByteBuffer = new StringBuilder();
    paramByteBuffer.append("No block with ID ");
    paramByteBuffer.append(paramInt);
    paramByteBuffer.append(" in APK Signing Block.");
    throw new SignatureNotFoundException(paramByteBuffer.toString());
  }
  
  static Pair<ByteBuffer, Long> findApkSigningBlock(RandomAccessFile paramRandomAccessFile, long paramLong)
    throws IOException, SignatureNotFoundException
  {
    if (paramLong >= 32L)
    {
      ByteBuffer localByteBuffer = ByteBuffer.allocate(24);
      localByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
      paramRandomAccessFile.seek(paramLong - localByteBuffer.capacity());
      paramRandomAccessFile.readFully(localByteBuffer.array(), localByteBuffer.arrayOffset(), localByteBuffer.capacity());
      if ((localByteBuffer.getLong(8) == 2334950737559900225L) && (localByteBuffer.getLong(16) == 3617552046287187010L))
      {
        long l1 = localByteBuffer.getLong(0);
        if ((l1 >= localByteBuffer.capacity()) && (l1 <= 2147483639L))
        {
          int i = (int)(8L + l1);
          paramLong -= i;
          if (paramLong >= 0L)
          {
            localByteBuffer = ByteBuffer.allocate(i);
            localByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            paramRandomAccessFile.seek(paramLong);
            paramRandomAccessFile.readFully(localByteBuffer.array(), localByteBuffer.arrayOffset(), localByteBuffer.capacity());
            long l2 = localByteBuffer.getLong(0);
            if (l2 == l1) {
              return Pair.create(localByteBuffer, Long.valueOf(paramLong));
            }
            paramRandomAccessFile = new StringBuilder();
            paramRandomAccessFile.append("APK Signing Block sizes in header and footer do not match: ");
            paramRandomAccessFile.append(l2);
            paramRandomAccessFile.append(" vs ");
            paramRandomAccessFile.append(l1);
            throw new SignatureNotFoundException(paramRandomAccessFile.toString());
          }
          paramRandomAccessFile = new StringBuilder();
          paramRandomAccessFile.append("APK Signing Block offset out of range: ");
          paramRandomAccessFile.append(paramLong);
          throw new SignatureNotFoundException(paramRandomAccessFile.toString());
        }
        paramRandomAccessFile = new StringBuilder();
        paramRandomAccessFile.append("APK Signing Block size out of range: ");
        paramRandomAccessFile.append(l1);
        throw new SignatureNotFoundException(paramRandomAccessFile.toString());
      }
      throw new SignatureNotFoundException("No APK Signing Block before ZIP Central Directory");
    }
    paramRandomAccessFile = new StringBuilder();
    paramRandomAccessFile.append("APK too small for APK Signing Block. ZIP Central Directory offset: ");
    paramRandomAccessFile.append(paramLong);
    throw new SignatureNotFoundException(paramRandomAccessFile.toString());
  }
  
  static SignatureInfo findSignature(RandomAccessFile paramRandomAccessFile, int paramInt)
    throws IOException, SignatureNotFoundException
  {
    Object localObject = getEocd(paramRandomAccessFile);
    ByteBuffer localByteBuffer = (ByteBuffer)first;
    long l1 = ((Long)second).longValue();
    if (!ZipUtils.isZip64EndOfCentralDirectoryLocatorPresent(paramRandomAccessFile, l1))
    {
      long l2 = getCentralDirOffset(localByteBuffer, l1);
      paramRandomAccessFile = findApkSigningBlock(paramRandomAccessFile, l2);
      localObject = (ByteBuffer)first;
      long l3 = ((Long)second).longValue();
      return new SignatureInfo(findApkSignatureSchemeBlock((ByteBuffer)localObject, paramInt), l3, l2, l1, localByteBuffer);
    }
    throw new SignatureNotFoundException("ZIP64 APK not supported");
  }
  
  /* Error */
  public static byte[] generateApkVerity(String paramString, ByteBufferFactory paramByteBufferFactory, SignatureInfo paramSignatureInfo)
    throws IOException, SignatureNotFoundException, SecurityException, DigestException, NoSuchAlgorithmException
  {
    // Byte code:
    //   0: new 258	java/io/RandomAccessFile
    //   3: dup
    //   4: aload_0
    //   5: ldc_w 351
    //   8: invokespecial 354	java/io/RandomAccessFile:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   11: astore_3
    //   12: aconst_null
    //   13: astore_0
    //   14: aload_3
    //   15: aload_2
    //   16: aload_1
    //   17: invokestatic 359	android/util/apk/ApkVerityBuilder:generateApkVerity	(Ljava/io/RandomAccessFile;Landroid/util/apk/SignatureInfo;Landroid/util/apk/ByteBufferFactory;)Landroid/util/apk/ApkVerityBuilder$ApkVerityResult;
    //   20: getfield 364	android/util/apk/ApkVerityBuilder$ApkVerityResult:rootHash	[B
    //   23: astore_1
    //   24: aload_3
    //   25: invokevirtual 367	java/io/RandomAccessFile:close	()V
    //   28: aload_1
    //   29: areturn
    //   30: astore_1
    //   31: goto +8 -> 39
    //   34: astore_1
    //   35: aload_1
    //   36: astore_0
    //   37: aload_1
    //   38: athrow
    //   39: aload_0
    //   40: ifnull +19 -> 59
    //   43: aload_3
    //   44: invokevirtual 367	java/io/RandomAccessFile:close	()V
    //   47: goto +16 -> 63
    //   50: astore_2
    //   51: aload_0
    //   52: aload_2
    //   53: invokevirtual 371	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   56: goto +7 -> 63
    //   59: aload_3
    //   60: invokevirtual 367	java/io/RandomAccessFile:close	()V
    //   63: aload_1
    //   64: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	65	0	paramString	String
    //   0	65	1	paramByteBufferFactory	ByteBufferFactory
    //   0	65	2	paramSignatureInfo	SignatureInfo
    //   11	49	3	localRandomAccessFile	RandomAccessFile
    // Exception table:
    //   from	to	target	type
    //   14	24	30	finally
    //   37	39	30	finally
    //   14	24	34	java/lang/Throwable
    //   43	47	50	java/lang/Throwable
  }
  
  static ByteBuffer getByteBuffer(ByteBuffer paramByteBuffer, int paramInt)
    throws BufferUnderflowException
  {
    if (paramInt >= 0)
    {
      int i = paramByteBuffer.limit();
      int j = paramByteBuffer.position();
      paramInt = j + paramInt;
      if ((paramInt >= j) && (paramInt <= i))
      {
        paramByteBuffer.limit(paramInt);
        try
        {
          ByteBuffer localByteBuffer = paramByteBuffer.slice();
          localByteBuffer.order(paramByteBuffer.order());
          paramByteBuffer.position(paramInt);
          return localByteBuffer;
        }
        finally
        {
          paramByteBuffer.limit(i);
        }
      }
      throw new BufferUnderflowException();
    }
    paramByteBuffer = new StringBuilder();
    paramByteBuffer.append("size: ");
    paramByteBuffer.append(paramInt);
    throw new IllegalArgumentException(paramByteBuffer.toString());
  }
  
  static long getCentralDirOffset(ByteBuffer paramByteBuffer, long paramLong)
    throws SignatureNotFoundException
  {
    long l = ZipUtils.getZipEocdCentralDirectoryOffset(paramByteBuffer);
    if (l <= paramLong)
    {
      if (l + ZipUtils.getZipEocdCentralDirectorySizeBytes(paramByteBuffer) == paramLong) {
        return l;
      }
      throw new SignatureNotFoundException("ZIP Central Directory is not immediately followed by End of Central Directory");
    }
    paramByteBuffer = new StringBuilder();
    paramByteBuffer.append("ZIP Central Directory offset out of range: ");
    paramByteBuffer.append(l);
    paramByteBuffer.append(". ZIP End of Central Directory offset: ");
    paramByteBuffer.append(paramLong);
    throw new SignatureNotFoundException(paramByteBuffer.toString());
  }
  
  private static long getChunkCount(long paramLong)
  {
    return (paramLong + 1048576L - 1L) / 1048576L;
  }
  
  static String getContentDigestAlgorithmJcaDigestAlgorithm(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown content digest algorthm: ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    case 2: 
      return "SHA-512";
    }
    return "SHA-256";
  }
  
  private static int getContentDigestAlgorithmOutputSizeBytes(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown content digest algorthm: ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    case 2: 
      return 64;
    }
    return 32;
  }
  
  static Pair<ByteBuffer, Long> getEocd(RandomAccessFile paramRandomAccessFile)
    throws IOException, SignatureNotFoundException
  {
    paramRandomAccessFile = ZipUtils.findZipEndOfCentralDirectoryRecord(paramRandomAccessFile);
    if (paramRandomAccessFile != null) {
      return paramRandomAccessFile;
    }
    throw new SignatureNotFoundException("Not an APK file: ZIP End of Central Directory record not found");
  }
  
  static ByteBuffer getLengthPrefixedSlice(ByteBuffer paramByteBuffer)
    throws IOException
  {
    if (paramByteBuffer.remaining() >= 4)
    {
      int i = paramByteBuffer.getInt();
      if (i >= 0)
      {
        if (i <= paramByteBuffer.remaining()) {
          return getByteBuffer(paramByteBuffer, i);
        }
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Length-prefixed field longer than remaining buffer. Field length: ");
        localStringBuilder.append(i);
        localStringBuilder.append(", remaining: ");
        localStringBuilder.append(paramByteBuffer.remaining());
        throw new IOException(localStringBuilder.toString());
      }
      throw new IllegalArgumentException("Negative length");
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Remaining buffer too short to contain length of length-prefixed field. Remaining: ");
    localStringBuilder.append(paramByteBuffer.remaining());
    throw new IOException(localStringBuilder.toString());
  }
  
  static int getSignatureAlgorithmContentDigestAlgorithm(int paramInt)
  {
    if (paramInt != 769)
    {
      if ((paramInt != 1057) && (paramInt != 1059) && (paramInt != 1061)) {}
      switch (paramInt)
      {
      default: 
        switch (paramInt)
        {
        default: 
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Unknown signature algorithm: 0x");
          localStringBuilder.append(Long.toHexString(paramInt & 0xFFFFFFFF));
          throw new IllegalArgumentException(localStringBuilder.toString());
        }
      case 258: 
      case 260: 
        return 2;
        return 3;
      }
    }
    return 1;
  }
  
  static String getSignatureAlgorithmJcaKeyAlgorithm(int paramInt)
  {
    if (paramInt != 769)
    {
      if (paramInt != 1057)
      {
        if (paramInt != 1059) {
          if (paramInt == 1061) {
            break label132;
          }
        }
        switch (paramInt)
        {
        default: 
          switch (paramInt)
          {
          default: 
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("Unknown signature algorithm: 0x");
            localStringBuilder.append(Long.toHexString(paramInt & 0xFFFFFFFF));
            throw new IllegalArgumentException(localStringBuilder.toString());
          }
          return "EC";
        }
      }
      return "RSA";
    }
    label132:
    return "DSA";
  }
  
  static Pair<String, ? extends AlgorithmParameterSpec> getSignatureAlgorithmJcaSignatureAlgorithm(int paramInt)
  {
    if (paramInt != 769)
    {
      if (paramInt != 1057)
      {
        if (paramInt != 1059) {
          if (paramInt == 1061) {
            break label208;
          }
        }
        switch (paramInt)
        {
        default: 
          switch (paramInt)
          {
          default: 
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("Unknown signature algorithm: 0x");
            localStringBuilder.append(Long.toHexString(paramInt & 0xFFFFFFFF));
            throw new IllegalArgumentException(localStringBuilder.toString());
          case 514: 
            return Pair.create("SHA512withECDSA", null);
          }
        case 260: 
          return Pair.create("SHA512withRSA", null);
        case 258: 
          return Pair.create("SHA512withRSA/PSS", new PSSParameterSpec("SHA-512", "MGF1", MGF1ParameterSpec.SHA512, 64, 1));
        case 257: 
          return Pair.create("SHA256withRSA/PSS", new PSSParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, 32, 1));
          return Pair.create("SHA256withECDSA", null);
        }
      }
      return Pair.create("SHA256withRSA", null);
    }
    label208:
    return Pair.create("SHA256withDSA", null);
  }
  
  static byte[] parseVerityDigestAndVerifySourceLength(byte[] paramArrayOfByte, long paramLong, SignatureInfo paramSignatureInfo)
    throws SecurityException
  {
    if (paramArrayOfByte.length == 32 + 8)
    {
      ByteBuffer localByteBuffer = ByteBuffer.wrap(paramArrayOfByte).order(ByteOrder.LITTLE_ENDIAN);
      localByteBuffer.position(32);
      if (localByteBuffer.getLong() == paramLong - (centralDirOffset - apkSigningBlockOffset)) {
        return Arrays.copyOfRange(paramArrayOfByte, 0, 32);
      }
      throw new SecurityException("APK content size did not verify");
    }
    paramSignatureInfo = new StringBuilder();
    paramSignatureInfo.append("Verity digest size is wrong: ");
    paramSignatureInfo.append(paramArrayOfByte.length);
    throw new SecurityException(paramSignatureInfo.toString());
  }
  
  static byte[] readLengthPrefixedByteArray(ByteBuffer paramByteBuffer)
    throws IOException
  {
    int i = paramByteBuffer.getInt();
    if (i >= 0)
    {
      if (i <= paramByteBuffer.remaining())
      {
        localObject = new byte[i];
        paramByteBuffer.get((byte[])localObject);
        return localObject;
      }
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Underflow while reading length-prefixed value. Length: ");
      ((StringBuilder)localObject).append(i);
      ((StringBuilder)localObject).append(", available: ");
      ((StringBuilder)localObject).append(paramByteBuffer.remaining());
      throw new IOException(((StringBuilder)localObject).toString());
    }
    throw new IOException("Negative length");
  }
  
  static void setUnsignedInt32LittleEndian(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    paramArrayOfByte[paramInt2] = ((byte)(byte)(paramInt1 & 0xFF));
    paramArrayOfByte[(paramInt2 + 1)] = ((byte)(byte)(paramInt1 >>> 8 & 0xFF));
    paramArrayOfByte[(paramInt2 + 2)] = ((byte)(byte)(paramInt1 >>> 16 & 0xFF));
    paramArrayOfByte[(paramInt2 + 3)] = ((byte)(byte)(paramInt1 >>> 24 & 0xFF));
  }
  
  static ByteBuffer sliceFromTo(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2)
  {
    if (paramInt1 >= 0)
    {
      if (paramInt2 >= paramInt1)
      {
        int i = paramByteBuffer.capacity();
        if (paramInt2 <= paramByteBuffer.capacity())
        {
          int j = paramByteBuffer.limit();
          i = paramByteBuffer.position();
          try
          {
            paramByteBuffer.position(0);
            paramByteBuffer.limit(paramInt2);
            paramByteBuffer.position(paramInt1);
            ByteBuffer localByteBuffer = paramByteBuffer.slice();
            localByteBuffer.order(paramByteBuffer.order());
            return localByteBuffer;
          }
          finally
          {
            paramByteBuffer.position(0);
            paramByteBuffer.limit(j);
            paramByteBuffer.position(i);
          }
        }
        paramByteBuffer = new StringBuilder();
        paramByteBuffer.append("end > capacity: ");
        paramByteBuffer.append(paramInt2);
        paramByteBuffer.append(" > ");
        paramByteBuffer.append(i);
        throw new IllegalArgumentException(paramByteBuffer.toString());
      }
      paramByteBuffer = new StringBuilder();
      paramByteBuffer.append("end < start: ");
      paramByteBuffer.append(paramInt2);
      paramByteBuffer.append(" < ");
      paramByteBuffer.append(paramInt1);
      throw new IllegalArgumentException(paramByteBuffer.toString());
    }
    paramByteBuffer = new StringBuilder();
    paramByteBuffer.append("start: ");
    paramByteBuffer.append(paramInt1);
    throw new IllegalArgumentException(paramByteBuffer.toString());
  }
  
  static void verifyIntegrity(Map<Integer, byte[]> paramMap, RandomAccessFile paramRandomAccessFile, SignatureInfo paramSignatureInfo)
    throws SecurityException
  {
    if (!paramMap.isEmpty())
    {
      int i = 1;
      ArrayMap localArrayMap = new ArrayMap();
      if (paramMap.containsKey(Integer.valueOf(1))) {
        localArrayMap.put(Integer.valueOf(1), (byte[])paramMap.get(Integer.valueOf(1)));
      }
      if (paramMap.containsKey(Integer.valueOf(2))) {
        localArrayMap.put(Integer.valueOf(2), (byte[])paramMap.get(Integer.valueOf(2)));
      }
      if (!localArrayMap.isEmpty()) {
        try
        {
          verifyIntegrityFor1MbChunkBasedAlgorithm(localArrayMap, paramRandomAccessFile.getFD(), paramSignatureInfo);
          i = 0;
        }
        catch (IOException paramMap)
        {
          throw new SecurityException("Cannot get FD", paramMap);
        }
      }
      if (paramMap.containsKey(Integer.valueOf(3)))
      {
        verifyIntegrityForVerityBasedAlgorithm((byte[])paramMap.get(Integer.valueOf(3)), paramRandomAccessFile, paramSignatureInfo);
        i = 0;
      }
      if (i == 0) {
        return;
      }
      throw new SecurityException("No known digest exists for integrity check");
    }
    throw new SecurityException("No digests provided");
  }
  
  private static void verifyIntegrityFor1MbChunkBasedAlgorithm(Map<Integer, byte[]> paramMap, FileDescriptor paramFileDescriptor, SignatureInfo paramSignatureInfo)
    throws SecurityException
  {
    MemoryMappedFileDataSource localMemoryMappedFileDataSource = new MemoryMappedFileDataSource(paramFileDescriptor, 0L, apkSigningBlockOffset);
    paramFileDescriptor = new MemoryMappedFileDataSource(paramFileDescriptor, centralDirOffset, eocdOffset - centralDirOffset);
    Object localObject = eocd.duplicate();
    ((ByteBuffer)localObject).order(ByteOrder.LITTLE_ENDIAN);
    ZipUtils.setZipEocdCentralDirectoryOffset((ByteBuffer)localObject, apkSigningBlockOffset);
    ByteBufferDataSource localByteBufferDataSource = new ByteBufferDataSource((ByteBuffer)localObject);
    paramSignatureInfo = new int[paramMap.size()];
    localObject = paramMap.keySet().iterator();
    for (int i = 0; ((Iterator)localObject).hasNext(); i++) {
      paramSignatureInfo[i] = ((Integer)((Iterator)localObject).next()).intValue();
    }
    i = 0;
    try
    {
      paramFileDescriptor = computeContentDigestsPer1MbChunk(paramSignatureInfo, new DataSource[] { localMemoryMappedFileDataSource, paramFileDescriptor, localByteBufferDataSource });
      while (i < paramSignatureInfo.length)
      {
        int j = paramSignatureInfo[i];
        if (MessageDigest.isEqual((byte[])paramMap.get(Integer.valueOf(j)), paramFileDescriptor[i]))
        {
          i++;
        }
        else
        {
          paramMap = new StringBuilder();
          paramMap.append(getContentDigestAlgorithmJcaDigestAlgorithm(j));
          paramMap.append(" digest of contents did not verify");
          throw new SecurityException(paramMap.toString());
        }
      }
      return;
    }
    catch (DigestException paramMap)
    {
      throw new SecurityException("Failed to compute digest(s) of contents", paramMap);
    }
  }
  
  private static void verifyIntegrityForVerityBasedAlgorithm(byte[] paramArrayOfByte, RandomAccessFile paramRandomAccessFile, SignatureInfo paramSignatureInfo)
    throws SecurityException
  {
    try
    {
      paramArrayOfByte = parseVerityDigestAndVerifySourceLength(paramArrayOfByte, paramRandomAccessFile.length(), paramSignatureInfo);
      ByteBufferFactory local1 = new android/util/apk/ApkSigningBlockUtils$1;
      local1.<init>();
      if (Arrays.equals(paramArrayOfByte, generateApkVerityrootHash)) {
        return;
      }
      paramArrayOfByte = new java/lang/SecurityException;
      paramArrayOfByte.<init>("APK verity digest of contents did not verify");
      throw paramArrayOfByte;
    }
    catch (DigestException|IOException|NoSuchAlgorithmException paramArrayOfByte)
    {
      throw new SecurityException("Error during verification", paramArrayOfByte);
    }
  }
  
  private static class MultipleDigestDataDigester
    implements DataDigester
  {
    private final MessageDigest[] mMds;
    
    MultipleDigestDataDigester(MessageDigest[] paramArrayOfMessageDigest)
    {
      mMds = paramArrayOfMessageDigest;
    }
    
    public void consume(ByteBuffer paramByteBuffer)
    {
      ByteBuffer localByteBuffer = paramByteBuffer.slice();
      for (paramByteBuffer : mMds)
      {
        localByteBuffer.position(0);
        paramByteBuffer.update(localByteBuffer);
      }
    }
  }
}
