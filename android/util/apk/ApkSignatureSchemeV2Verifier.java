package android.util.apk;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.security.cert.X509Certificate;

public class ApkSignatureSchemeV2Verifier
{
  private static final int APK_SIGNATURE_SCHEME_V2_BLOCK_ID = 1896449818;
  public static final int SF_ATTRIBUTE_ANDROID_APK_SIGNED_ID = 2;
  private static final int STRIPPING_PROTECTION_ATTR_ID = -1091571699;
  
  public ApkSignatureSchemeV2Verifier() {}
  
  private static SignatureInfo findSignature(RandomAccessFile paramRandomAccessFile)
    throws IOException, SignatureNotFoundException
  {
    return ApkSigningBlockUtils.findSignature(paramRandomAccessFile, 1896449818);
  }
  
  /* Error */
  static byte[] generateApkVerity(String paramString, ByteBufferFactory paramByteBufferFactory)
    throws IOException, SignatureNotFoundException, SecurityException, java.security.DigestException, java.security.NoSuchAlgorithmException
  {
    // Byte code:
    //   0: new 54	java/io/RandomAccessFile
    //   3: dup
    //   4: aload_0
    //   5: ldc 56
    //   7: invokespecial 59	java/io/RandomAccessFile:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   10: astore_2
    //   11: aconst_null
    //   12: astore_3
    //   13: aload_0
    //   14: aload_1
    //   15: aload_2
    //   16: invokestatic 61	android/util/apk/ApkSignatureSchemeV2Verifier:findSignature	(Ljava/io/RandomAccessFile;)Landroid/util/apk/SignatureInfo;
    //   19: invokestatic 64	android/util/apk/ApkSigningBlockUtils:generateApkVerity	(Ljava/lang/String;Landroid/util/apk/ByteBufferFactory;Landroid/util/apk/SignatureInfo;)[B
    //   22: astore_0
    //   23: aconst_null
    //   24: aload_2
    //   25: invokestatic 66	android/util/apk/ApkSignatureSchemeV2Verifier:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   28: aload_0
    //   29: areturn
    //   30: astore_0
    //   31: goto +8 -> 39
    //   34: astore_0
    //   35: aload_0
    //   36: astore_3
    //   37: aload_0
    //   38: athrow
    //   39: aload_3
    //   40: aload_2
    //   41: invokestatic 66	android/util/apk/ApkSignatureSchemeV2Verifier:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   44: aload_0
    //   45: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	46	0	paramString	String
    //   0	46	1	paramByteBufferFactory	ByteBufferFactory
    //   10	31	2	localRandomAccessFile	RandomAccessFile
    //   12	28	3	str	String
    // Exception table:
    //   from	to	target	type
    //   13	23	30	finally
    //   37	39	30	finally
    //   13	23	34	java/lang/Throwable
  }
  
  /* Error */
  static byte[] generateFsverityRootHash(String paramString)
    throws IOException, SignatureNotFoundException, java.security.DigestException, java.security.NoSuchAlgorithmException
  {
    // Byte code:
    //   0: new 54	java/io/RandomAccessFile
    //   3: dup
    //   4: aload_0
    //   5: ldc 56
    //   7: invokespecial 59	java/io/RandomAccessFile:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   10: astore_1
    //   11: aconst_null
    //   12: astore_2
    //   13: aload_2
    //   14: astore_0
    //   15: aload_1
    //   16: invokestatic 61	android/util/apk/ApkSignatureSchemeV2Verifier:findSignature	(Ljava/io/RandomAccessFile;)Landroid/util/apk/SignatureInfo;
    //   19: astore_3
    //   20: aload_2
    //   21: astore_0
    //   22: aload_1
    //   23: iconst_0
    //   24: invokestatic 72	android/util/apk/ApkSignatureSchemeV2Verifier:verify	(Ljava/io/RandomAccessFile;Z)Landroid/util/apk/ApkSignatureSchemeV2Verifier$VerifiedSigner;
    //   27: astore 4
    //   29: aload_2
    //   30: astore_0
    //   31: aload 4
    //   33: getfield 76	android/util/apk/ApkSignatureSchemeV2Verifier$VerifiedSigner:verityRootHash	[B
    //   36: astore 5
    //   38: aload 5
    //   40: ifnonnull +10 -> 50
    //   43: aconst_null
    //   44: aload_1
    //   45: invokestatic 66	android/util/apk/ApkSignatureSchemeV2Verifier:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   48: aconst_null
    //   49: areturn
    //   50: aload_2
    //   51: astore_0
    //   52: aload_1
    //   53: aload 4
    //   55: getfield 76	android/util/apk/ApkSignatureSchemeV2Verifier$VerifiedSigner:verityRootHash	[B
    //   58: invokestatic 82	java/nio/ByteBuffer:wrap	([B)Ljava/nio/ByteBuffer;
    //   61: aload_3
    //   62: invokestatic 87	android/util/apk/ApkVerityBuilder:generateFsverityRootHash	(Ljava/io/RandomAccessFile;Ljava/nio/ByteBuffer;Landroid/util/apk/SignatureInfo;)[B
    //   65: astore_2
    //   66: aconst_null
    //   67: aload_1
    //   68: invokestatic 66	android/util/apk/ApkSignatureSchemeV2Verifier:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   71: aload_2
    //   72: areturn
    //   73: astore_2
    //   74: goto +8 -> 82
    //   77: astore_2
    //   78: aload_2
    //   79: astore_0
    //   80: aload_2
    //   81: athrow
    //   82: aload_0
    //   83: aload_1
    //   84: invokestatic 66	android/util/apk/ApkSignatureSchemeV2Verifier:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   87: aload_2
    //   88: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	89	0	paramString	String
    //   10	74	1	localRandomAccessFile	RandomAccessFile
    //   12	60	2	arrayOfByte1	byte[]
    //   73	1	2	localObject	Object
    //   77	11	2	localThrowable	Throwable
    //   19	43	3	localSignatureInfo	SignatureInfo
    //   27	27	4	localVerifiedSigner	VerifiedSigner
    //   36	3	5	arrayOfByte2	byte[]
    // Exception table:
    //   from	to	target	type
    //   15	20	73	finally
    //   22	29	73	finally
    //   31	38	73	finally
    //   52	66	73	finally
    //   80	82	73	finally
    //   15	20	77	java/lang/Throwable
    //   22	29	77	java/lang/Throwable
    //   31	38	77	java/lang/Throwable
    //   52	66	77	java/lang/Throwable
  }
  
  /* Error */
  static byte[] getVerityRootHash(String paramString)
    throws IOException, SignatureNotFoundException, SecurityException
  {
    // Byte code:
    //   0: new 54	java/io/RandomAccessFile
    //   3: dup
    //   4: aload_0
    //   5: ldc 56
    //   7: invokespecial 59	java/io/RandomAccessFile:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   10: astore_1
    //   11: aconst_null
    //   12: astore_2
    //   13: aload_2
    //   14: astore_0
    //   15: aload_1
    //   16: invokestatic 61	android/util/apk/ApkSignatureSchemeV2Verifier:findSignature	(Ljava/io/RandomAccessFile;)Landroid/util/apk/SignatureInfo;
    //   19: pop
    //   20: aload_2
    //   21: astore_0
    //   22: aload_1
    //   23: iconst_0
    //   24: invokestatic 72	android/util/apk/ApkSignatureSchemeV2Verifier:verify	(Ljava/io/RandomAccessFile;Z)Landroid/util/apk/ApkSignatureSchemeV2Verifier$VerifiedSigner;
    //   27: getfield 76	android/util/apk/ApkSignatureSchemeV2Verifier$VerifiedSigner:verityRootHash	[B
    //   30: astore_2
    //   31: aconst_null
    //   32: aload_1
    //   33: invokestatic 66	android/util/apk/ApkSignatureSchemeV2Verifier:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   36: aload_2
    //   37: areturn
    //   38: astore_2
    //   39: goto +8 -> 47
    //   42: astore_2
    //   43: aload_2
    //   44: astore_0
    //   45: aload_2
    //   46: athrow
    //   47: aload_0
    //   48: aload_1
    //   49: invokestatic 66	android/util/apk/ApkSignatureSchemeV2Verifier:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   52: aload_2
    //   53: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	54	0	paramString	String
    //   10	39	1	localRandomAccessFile	RandomAccessFile
    //   12	25	2	arrayOfByte	byte[]
    //   38	1	2	localObject	Object
    //   42	11	2	localThrowable	Throwable
    // Exception table:
    //   from	to	target	type
    //   15	20	38	finally
    //   22	31	38	finally
    //   45	47	38	finally
    //   15	20	42	java/lang/Throwable
    //   22	31	42	java/lang/Throwable
  }
  
  /* Error */
  public static boolean hasSignature(String paramString)
    throws IOException
  {
    // Byte code:
    //   0: new 54	java/io/RandomAccessFile
    //   3: astore_1
    //   4: aload_1
    //   5: aload_0
    //   6: ldc 56
    //   8: invokespecial 59	java/io/RandomAccessFile:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   11: aconst_null
    //   12: astore_0
    //   13: aload_1
    //   14: invokestatic 61	android/util/apk/ApkSignatureSchemeV2Verifier:findSignature	(Ljava/io/RandomAccessFile;)Landroid/util/apk/SignatureInfo;
    //   17: pop
    //   18: aconst_null
    //   19: aload_1
    //   20: invokestatic 66	android/util/apk/ApkSignatureSchemeV2Verifier:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   23: iconst_1
    //   24: ireturn
    //   25: astore_2
    //   26: goto +8 -> 34
    //   29: astore_2
    //   30: aload_2
    //   31: astore_0
    //   32: aload_2
    //   33: athrow
    //   34: aload_0
    //   35: aload_1
    //   36: invokestatic 66	android/util/apk/ApkSignatureSchemeV2Verifier:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   39: aload_2
    //   40: athrow
    //   41: astore_0
    //   42: iconst_0
    //   43: ireturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	44	0	paramString	String
    //   3	33	1	localRandomAccessFile	RandomAccessFile
    //   25	1	2	localObject	Object
    //   29	11	2	localThrowable	Throwable
    // Exception table:
    //   from	to	target	type
    //   13	18	25	finally
    //   32	34	25	finally
    //   13	18	29	java/lang/Throwable
    //   0	11	41	android/util/apk/SignatureNotFoundException
    //   18	23	41	android/util/apk/SignatureNotFoundException
    //   34	41	41	android/util/apk/SignatureNotFoundException
  }
  
  private static boolean isSupportedSignatureAlgorithm(int paramInt)
  {
    if ((paramInt != 769) && (paramInt != 1057) && (paramInt != 1059) && (paramInt != 1061)) {
      switch (paramInt)
      {
      default: 
        switch (paramInt)
        {
        default: 
          return false;
        }
        break;
      }
    }
    return true;
  }
  
  public static X509Certificate[][] plsCertsNoVerifyOnlyCerts(String paramString)
    throws SignatureNotFoundException, SecurityException, IOException
  {
    return verifycerts;
  }
  
  /* Error */
  private static VerifiedSigner verify(RandomAccessFile paramRandomAccessFile, SignatureInfo paramSignatureInfo, boolean paramBoolean)
    throws SecurityException, IOException
  {
    // Byte code:
    //   0: iconst_0
    //   1: istore_3
    //   2: new 108	android/util/ArrayMap
    //   5: dup
    //   6: invokespecial 109	android/util/ArrayMap:<init>	()V
    //   9: astore 4
    //   11: new 111	java/util/ArrayList
    //   14: dup
    //   15: invokespecial 112	java/util/ArrayList:<init>	()V
    //   18: astore 5
    //   20: ldc 114
    //   22: invokestatic 120	java/security/cert/CertificateFactory:getInstance	(Ljava/lang/String;)Ljava/security/cert/CertificateFactory;
    //   25: astore 6
    //   27: aload_1
    //   28: getfield 126	android/util/apk/SignatureInfo:signatureBlock	Ljava/nio/ByteBuffer;
    //   31: invokestatic 130	android/util/apk/ApkSigningBlockUtils:getLengthPrefixedSlice	(Ljava/nio/ByteBuffer;)Ljava/nio/ByteBuffer;
    //   34: astore 7
    //   36: aload 7
    //   38: invokevirtual 134	java/nio/ByteBuffer:hasRemaining	()Z
    //   41: ifeq +71 -> 112
    //   44: iinc 3 1
    //   47: aload 5
    //   49: aload 7
    //   51: invokestatic 130	android/util/apk/ApkSigningBlockUtils:getLengthPrefixedSlice	(Ljava/nio/ByteBuffer;)Ljava/nio/ByteBuffer;
    //   54: aload 4
    //   56: aload 6
    //   58: invokestatic 138	android/util/apk/ApkSignatureSchemeV2Verifier:verifySigner	(Ljava/nio/ByteBuffer;Ljava/util/Map;Ljava/security/cert/CertificateFactory;)[Ljava/security/cert/X509Certificate;
    //   61: invokeinterface 144 2 0
    //   66: pop
    //   67: goto -31 -> 36
    //   70: astore_1
    //   71: new 146	java/lang/StringBuilder
    //   74: dup
    //   75: invokespecial 147	java/lang/StringBuilder:<init>	()V
    //   78: astore_0
    //   79: aload_0
    //   80: ldc -107
    //   82: invokevirtual 153	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   85: pop
    //   86: aload_0
    //   87: iload_3
    //   88: invokevirtual 156	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   91: pop
    //   92: aload_0
    //   93: ldc -98
    //   95: invokevirtual 153	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   98: pop
    //   99: new 48	java/lang/SecurityException
    //   102: dup
    //   103: aload_0
    //   104: invokevirtual 162	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   107: aload_1
    //   108: invokespecial 165	java/lang/SecurityException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   111: athrow
    //   112: iload_3
    //   113: iconst_1
    //   114: if_icmplt +109 -> 223
    //   117: aload 4
    //   119: invokeinterface 170 1 0
    //   124: ifne +89 -> 213
    //   127: iload_2
    //   128: ifeq +10 -> 138
    //   131: aload 4
    //   133: aload_0
    //   134: aload_1
    //   135: invokestatic 174	android/util/apk/ApkSigningBlockUtils:verifyIntegrity	(Ljava/util/Map;Ljava/io/RandomAccessFile;Landroid/util/apk/SignatureInfo;)V
    //   138: aconst_null
    //   139: astore 7
    //   141: aload 4
    //   143: iconst_3
    //   144: invokestatic 180	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   147: invokeinterface 183 2 0
    //   152: ifeq +31 -> 183
    //   155: aload 4
    //   157: iconst_3
    //   158: invokestatic 180	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   161: invokeinterface 187 2 0
    //   166: checkcast 188	[B
    //   169: astore 7
    //   171: aload 7
    //   173: aload_0
    //   174: invokevirtual 192	java/io/RandomAccessFile:length	()J
    //   177: aload_1
    //   178: invokestatic 196	android/util/apk/ApkSigningBlockUtils:parseVerityDigestAndVerifySourceLength	([BJLandroid/util/apk/SignatureInfo;)[B
    //   181: astore 7
    //   183: new 6	android/util/apk/ApkSignatureSchemeV2Verifier$VerifiedSigner
    //   186: dup
    //   187: aload 5
    //   189: aload 5
    //   191: invokeinterface 200 1 0
    //   196: anewarray 202	[Ljava/security/cert/X509Certificate;
    //   199: invokeinterface 206 2 0
    //   204: checkcast 207	[[Ljava/security/cert/X509Certificate;
    //   207: aload 7
    //   209: invokespecial 210	android/util/apk/ApkSignatureSchemeV2Verifier$VerifiedSigner:<init>	([[Ljava/security/cert/X509Certificate;[B)V
    //   212: areturn
    //   213: new 48	java/lang/SecurityException
    //   216: dup
    //   217: ldc -44
    //   219: invokespecial 215	java/lang/SecurityException:<init>	(Ljava/lang/String;)V
    //   222: athrow
    //   223: new 48	java/lang/SecurityException
    //   226: dup
    //   227: ldc -39
    //   229: invokespecial 215	java/lang/SecurityException:<init>	(Ljava/lang/String;)V
    //   232: athrow
    //   233: astore_0
    //   234: new 48	java/lang/SecurityException
    //   237: dup
    //   238: ldc -37
    //   240: aload_0
    //   241: invokespecial 165	java/lang/SecurityException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   244: athrow
    //   245: astore_0
    //   246: new 221	java/lang/RuntimeException
    //   249: dup
    //   250: ldc -33
    //   252: aload_0
    //   253: invokespecial 224	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   256: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	257	0	paramRandomAccessFile	RandomAccessFile
    //   0	257	1	paramSignatureInfo	SignatureInfo
    //   0	257	2	paramBoolean	boolean
    //   1	114	3	i	int
    //   9	147	4	localArrayMap	android.util.ArrayMap
    //   18	172	5	localArrayList	java.util.ArrayList
    //   25	32	6	localCertificateFactory	java.security.cert.CertificateFactory
    //   34	174	7	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   47	67	70	java/io/IOException
    //   47	67	70	java/nio/BufferUnderflowException
    //   47	67	70	java/lang/SecurityException
    //   27	36	233	java/io/IOException
    //   20	27	245	java/security/cert/CertificateException
  }
  
  private static VerifiedSigner verify(RandomAccessFile paramRandomAccessFile, boolean paramBoolean)
    throws SignatureNotFoundException, SecurityException, IOException
  {
    return verify(paramRandomAccessFile, findSignature(paramRandomAccessFile), paramBoolean);
  }
  
  /* Error */
  private static VerifiedSigner verify(String paramString, boolean paramBoolean)
    throws SignatureNotFoundException, SecurityException, IOException
  {
    // Byte code:
    //   0: new 54	java/io/RandomAccessFile
    //   3: dup
    //   4: aload_0
    //   5: ldc 56
    //   7: invokespecial 59	java/io/RandomAccessFile:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   10: astore_2
    //   11: aconst_null
    //   12: astore_0
    //   13: aload_2
    //   14: iload_1
    //   15: invokestatic 72	android/util/apk/ApkSignatureSchemeV2Verifier:verify	(Ljava/io/RandomAccessFile;Z)Landroid/util/apk/ApkSignatureSchemeV2Verifier$VerifiedSigner;
    //   18: astore_3
    //   19: aconst_null
    //   20: aload_2
    //   21: invokestatic 66	android/util/apk/ApkSignatureSchemeV2Verifier:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   24: aload_3
    //   25: areturn
    //   26: astore_3
    //   27: goto +8 -> 35
    //   30: astore_3
    //   31: aload_3
    //   32: astore_0
    //   33: aload_3
    //   34: athrow
    //   35: aload_0
    //   36: aload_2
    //   37: invokestatic 66	android/util/apk/ApkSignatureSchemeV2Verifier:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   40: aload_3
    //   41: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	42	0	paramString	String
    //   0	42	1	paramBoolean	boolean
    //   10	27	2	localRandomAccessFile	RandomAccessFile
    //   18	7	3	localVerifiedSigner	VerifiedSigner
    //   26	1	3	localObject	Object
    //   30	11	3	localThrowable	Throwable
    // Exception table:
    //   from	to	target	type
    //   13	19	26	finally
    //   33	35	26	finally
    //   13	19	30	java/lang/Throwable
  }
  
  public static X509Certificate[][] verify(String paramString)
    throws SignatureNotFoundException, SecurityException, IOException
  {
    return verifycerts;
  }
  
  private static void verifyAdditionalAttributes(ByteBuffer paramByteBuffer)
    throws SecurityException, IOException
  {
    while (paramByteBuffer.hasRemaining())
    {
      ByteBuffer localByteBuffer = ApkSigningBlockUtils.getLengthPrefixedSlice(paramByteBuffer);
      if (localByteBuffer.remaining() >= 4)
      {
        if (localByteBuffer.getInt() == -1091571699)
        {
          if (localByteBuffer.remaining() >= 4) {
            if (localByteBuffer.getInt() != 3) {
              continue;
            }
          }
        }
        else {
          throw new SecurityException("V2 signature indicates APK is signed using APK Signature Scheme v3, but none was found. Signature stripped?");
        }
        paramByteBuffer = new StringBuilder();
        paramByteBuffer.append("V2 Signature Scheme Stripping Protection Attribute  value too small.  Expected 4 bytes, but found ");
        paramByteBuffer.append(localByteBuffer.remaining());
        throw new IOException(paramByteBuffer.toString());
      }
      else
      {
        paramByteBuffer = new StringBuilder();
        paramByteBuffer.append("Remaining buffer too short to contain additional attribute ID. Remaining: ");
        paramByteBuffer.append(localByteBuffer.remaining());
        throw new IOException(paramByteBuffer.toString());
      }
    }
  }
  
  /* Error */
  private static X509Certificate[] verifySigner(ByteBuffer paramByteBuffer, java.util.Map<Integer, byte[]> paramMap, java.security.cert.CertificateFactory paramCertificateFactory)
    throws SecurityException, IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic 130	android/util/apk/ApkSigningBlockUtils:getLengthPrefixedSlice	(Ljava/nio/ByteBuffer;)Ljava/nio/ByteBuffer;
    //   4: astore_3
    //   5: aload_0
    //   6: invokestatic 130	android/util/apk/ApkSigningBlockUtils:getLengthPrefixedSlice	(Ljava/nio/ByteBuffer;)Ljava/nio/ByteBuffer;
    //   9: astore 4
    //   11: aload_0
    //   12: invokestatic 253	android/util/apk/ApkSigningBlockUtils:readLengthPrefixedByteArray	(Ljava/nio/ByteBuffer;)[B
    //   15: astore 5
    //   17: new 111	java/util/ArrayList
    //   20: dup
    //   21: invokespecial 112	java/util/ArrayList:<init>	()V
    //   24: astore 6
    //   26: aconst_null
    //   27: astore_0
    //   28: iconst_m1
    //   29: istore 7
    //   31: iconst_0
    //   32: istore 8
    //   34: aload 4
    //   36: invokevirtual 134	java/nio/ByteBuffer:hasRemaining	()Z
    //   39: ifeq +141 -> 180
    //   42: iinc 8 1
    //   45: aload 4
    //   47: invokestatic 130	android/util/apk/ApkSigningBlockUtils:getLengthPrefixedSlice	(Ljava/nio/ByteBuffer;)Ljava/nio/ByteBuffer;
    //   50: astore 9
    //   52: aload 9
    //   54: invokevirtual 231	java/nio/ByteBuffer:remaining	()I
    //   57: bipush 8
    //   59: if_icmplt +71 -> 130
    //   62: aload 9
    //   64: invokevirtual 234	java/nio/ByteBuffer:getInt	()I
    //   67: istore 10
    //   69: aload 6
    //   71: iload 10
    //   73: invokestatic 180	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   76: invokeinterface 144 2 0
    //   81: pop
    //   82: iload 10
    //   84: invokestatic 255	android/util/apk/ApkSignatureSchemeV2Verifier:isSupportedSignatureAlgorithm	(I)Z
    //   87: ifne +6 -> 93
    //   90: goto -56 -> 34
    //   93: iload 7
    //   95: iconst_m1
    //   96: if_icmpeq +17 -> 113
    //   99: iload 7
    //   101: istore 11
    //   103: iload 10
    //   105: iload 7
    //   107: invokestatic 259	android/util/apk/ApkSigningBlockUtils:compareSignatureAlgorithm	(II)I
    //   110: ifle +13 -> 123
    //   113: iload 10
    //   115: istore 11
    //   117: aload 9
    //   119: invokestatic 253	android/util/apk/ApkSigningBlockUtils:readLengthPrefixedByteArray	(Ljava/nio/ByteBuffer;)[B
    //   122: astore_0
    //   123: iload 11
    //   125: istore 7
    //   127: goto -93 -> 34
    //   130: new 48	java/lang/SecurityException
    //   133: astore_0
    //   134: aload_0
    //   135: ldc_w 261
    //   138: invokespecial 215	java/lang/SecurityException:<init>	(Ljava/lang/String;)V
    //   141: aload_0
    //   142: athrow
    //   143: astore_0
    //   144: new 146	java/lang/StringBuilder
    //   147: dup
    //   148: invokespecial 147	java/lang/StringBuilder:<init>	()V
    //   151: astore_1
    //   152: aload_1
    //   153: ldc_w 263
    //   156: invokevirtual 153	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   159: pop
    //   160: aload_1
    //   161: iload 8
    //   163: invokevirtual 156	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   166: pop
    //   167: new 48	java/lang/SecurityException
    //   170: dup
    //   171: aload_1
    //   172: invokevirtual 162	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   175: aload_0
    //   176: invokespecial 165	java/lang/SecurityException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   179: athrow
    //   180: iload 7
    //   182: iconst_m1
    //   183: if_icmpne +30 -> 213
    //   186: iload 8
    //   188: ifne +14 -> 202
    //   191: new 48	java/lang/SecurityException
    //   194: dup
    //   195: ldc_w 265
    //   198: invokespecial 215	java/lang/SecurityException:<init>	(Ljava/lang/String;)V
    //   201: athrow
    //   202: new 48	java/lang/SecurityException
    //   205: dup
    //   206: ldc_w 267
    //   209: invokespecial 215	java/lang/SecurityException:<init>	(Ljava/lang/String;)V
    //   212: athrow
    //   213: iload 7
    //   215: invokestatic 271	android/util/apk/ApkSigningBlockUtils:getSignatureAlgorithmJcaKeyAlgorithm	(I)Ljava/lang/String;
    //   218: astore 12
    //   220: iload 7
    //   222: invokestatic 275	android/util/apk/ApkSigningBlockUtils:getSignatureAlgorithmJcaSignatureAlgorithm	(I)Landroid/util/Pair;
    //   225: astore 13
    //   227: aload 13
    //   229: getfield 281	android/util/Pair:first	Ljava/lang/Object;
    //   232: checkcast 283	java/lang/String
    //   235: astore 9
    //   237: aload 13
    //   239: getfield 286	android/util/Pair:second	Ljava/lang/Object;
    //   242: checkcast 288	java/security/spec/AlgorithmParameterSpec
    //   245: astore 13
    //   247: aload 12
    //   249: invokestatic 293	java/security/KeyFactory:getInstance	(Ljava/lang/String;)Ljava/security/KeyFactory;
    //   252: astore 12
    //   254: new 295	java/security/spec/X509EncodedKeySpec
    //   257: astore 14
    //   259: aload 14
    //   261: aload 5
    //   263: invokespecial 298	java/security/spec/X509EncodedKeySpec:<init>	([B)V
    //   266: aload 12
    //   268: aload 14
    //   270: invokevirtual 302	java/security/KeyFactory:generatePublic	(Ljava/security/spec/KeySpec;)Ljava/security/PublicKey;
    //   273: astore 14
    //   275: aload 9
    //   277: invokestatic 307	java/security/Signature:getInstance	(Ljava/lang/String;)Ljava/security/Signature;
    //   280: astore 12
    //   282: aload 12
    //   284: aload 14
    //   286: invokevirtual 311	java/security/Signature:initVerify	(Ljava/security/PublicKey;)V
    //   289: aload 13
    //   291: ifnull +17 -> 308
    //   294: aload 12
    //   296: aload 13
    //   298: invokevirtual 315	java/security/Signature:setParameter	(Ljava/security/spec/AlgorithmParameterSpec;)V
    //   301: goto +7 -> 308
    //   304: astore_0
    //   305: goto +519 -> 824
    //   308: aload 12
    //   310: aload_3
    //   311: invokevirtual 318	java/security/Signature:update	(Ljava/nio/ByteBuffer;)V
    //   314: aload 12
    //   316: aload_0
    //   317: invokevirtual 321	java/security/Signature:verify	([B)Z
    //   320: istore 15
    //   322: iload 15
    //   324: ifeq +464 -> 788
    //   327: aload_3
    //   328: invokevirtual 325	java/nio/ByteBuffer:clear	()Ljava/nio/Buffer;
    //   331: pop
    //   332: aload_3
    //   333: invokestatic 130	android/util/apk/ApkSigningBlockUtils:getLengthPrefixedSlice	(Ljava/nio/ByteBuffer;)Ljava/nio/ByteBuffer;
    //   336: astore 12
    //   338: new 111	java/util/ArrayList
    //   341: dup
    //   342: invokespecial 112	java/util/ArrayList:<init>	()V
    //   345: astore 13
    //   347: aconst_null
    //   348: astore 9
    //   350: iconst_0
    //   351: istore 11
    //   353: aload 12
    //   355: invokevirtual 134	java/nio/ByteBuffer:hasRemaining	()Z
    //   358: ifeq +122 -> 480
    //   361: iinc 11 1
    //   364: aload 12
    //   366: invokestatic 130	android/util/apk/ApkSigningBlockUtils:getLengthPrefixedSlice	(Ljava/nio/ByteBuffer;)Ljava/nio/ByteBuffer;
    //   369: astore 14
    //   371: aload 14
    //   373: invokevirtual 231	java/nio/ByteBuffer:remaining	()I
    //   376: istore 10
    //   378: iload 10
    //   380: bipush 8
    //   382: if_icmplt +40 -> 422
    //   385: aload 14
    //   387: invokevirtual 234	java/nio/ByteBuffer:getInt	()I
    //   390: istore 10
    //   392: aload 13
    //   394: iload 10
    //   396: invokestatic 180	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   399: invokeinterface 144 2 0
    //   404: pop
    //   405: iload 10
    //   407: iload 7
    //   409: if_icmpne +10 -> 419
    //   412: aload 14
    //   414: invokestatic 253	android/util/apk/ApkSigningBlockUtils:readLengthPrefixedByteArray	(Ljava/nio/ByteBuffer;)[B
    //   417: astore 9
    //   419: goto -66 -> 353
    //   422: new 36	java/io/IOException
    //   425: astore_0
    //   426: aload_0
    //   427: ldc_w 327
    //   430: invokespecial 239	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   433: aload_0
    //   434: athrow
    //   435: astore_0
    //   436: goto +8 -> 444
    //   439: astore_0
    //   440: goto +4 -> 444
    //   443: astore_0
    //   444: new 146	java/lang/StringBuilder
    //   447: dup
    //   448: invokespecial 147	java/lang/StringBuilder:<init>	()V
    //   451: astore_1
    //   452: aload_1
    //   453: ldc_w 329
    //   456: invokevirtual 153	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   459: pop
    //   460: aload_1
    //   461: iload 11
    //   463: invokevirtual 156	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   466: pop
    //   467: new 36	java/io/IOException
    //   470: dup
    //   471: aload_1
    //   472: invokevirtual 162	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   475: aload_0
    //   476: invokespecial 330	java/io/IOException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   479: athrow
    //   480: aload 6
    //   482: aload 13
    //   484: invokeinterface 333 2 0
    //   489: ifeq +288 -> 777
    //   492: iload 7
    //   494: invokestatic 337	android/util/apk/ApkSigningBlockUtils:getSignatureAlgorithmContentDigestAlgorithm	(I)I
    //   497: istore 8
    //   499: aload_1
    //   500: iload 8
    //   502: invokestatic 180	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   505: aload 9
    //   507: invokeinterface 341 3 0
    //   512: checkcast 188	[B
    //   515: astore_0
    //   516: aload_0
    //   517: ifnull +53 -> 570
    //   520: aload_0
    //   521: aload 9
    //   523: invokestatic 347	java/security/MessageDigest:isEqual	([B[B)Z
    //   526: ifeq +6 -> 532
    //   529: goto +41 -> 570
    //   532: new 146	java/lang/StringBuilder
    //   535: dup
    //   536: invokespecial 147	java/lang/StringBuilder:<init>	()V
    //   539: astore_0
    //   540: aload_0
    //   541: iload 8
    //   543: invokestatic 350	android/util/apk/ApkSigningBlockUtils:getContentDigestAlgorithmJcaDigestAlgorithm	(I)Ljava/lang/String;
    //   546: invokevirtual 153	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   549: pop
    //   550: aload_0
    //   551: ldc_w 352
    //   554: invokevirtual 153	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   557: pop
    //   558: new 48	java/lang/SecurityException
    //   561: dup
    //   562: aload_0
    //   563: invokevirtual 162	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   566: invokespecial 215	java/lang/SecurityException:<init>	(Ljava/lang/String;)V
    //   569: athrow
    //   570: aload_3
    //   571: invokestatic 130	android/util/apk/ApkSigningBlockUtils:getLengthPrefixedSlice	(Ljava/nio/ByteBuffer;)Ljava/nio/ByteBuffer;
    //   574: astore_0
    //   575: new 111	java/util/ArrayList
    //   578: dup
    //   579: invokespecial 112	java/util/ArrayList:<init>	()V
    //   582: astore 9
    //   584: iconst_0
    //   585: istore 11
    //   587: aload_0
    //   588: invokevirtual 134	java/nio/ByteBuffer:hasRemaining	()Z
    //   591: ifeq +99 -> 690
    //   594: iinc 11 1
    //   597: aload_0
    //   598: invokestatic 253	android/util/apk/ApkSigningBlockUtils:readLengthPrefixedByteArray	(Ljava/nio/ByteBuffer;)[B
    //   601: astore_1
    //   602: new 354	java/io/ByteArrayInputStream
    //   605: astore 4
    //   607: aload 4
    //   609: aload_1
    //   610: invokespecial 355	java/io/ByteArrayInputStream:<init>	([B)V
    //   613: aload_2
    //   614: aload 4
    //   616: invokevirtual 359	java/security/cert/CertificateFactory:generateCertificate	(Ljava/io/InputStream;)Ljava/security/cert/Certificate;
    //   619: checkcast 361	java/security/cert/X509Certificate
    //   622: astore 4
    //   624: aload 9
    //   626: new 363	android/util/apk/VerbatimX509Certificate
    //   629: dup
    //   630: aload 4
    //   632: aload_1
    //   633: invokespecial 366	android/util/apk/VerbatimX509Certificate:<init>	(Ljava/security/cert/X509Certificate;[B)V
    //   636: invokeinterface 144 2 0
    //   641: pop
    //   642: goto -55 -> 587
    //   645: astore_0
    //   646: goto +8 -> 654
    //   649: astore_0
    //   650: goto +4 -> 654
    //   653: astore_0
    //   654: new 146	java/lang/StringBuilder
    //   657: dup
    //   658: invokespecial 147	java/lang/StringBuilder:<init>	()V
    //   661: astore_1
    //   662: aload_1
    //   663: ldc_w 368
    //   666: invokevirtual 153	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   669: pop
    //   670: aload_1
    //   671: iload 11
    //   673: invokevirtual 156	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   676: pop
    //   677: new 48	java/lang/SecurityException
    //   680: dup
    //   681: aload_1
    //   682: invokevirtual 162	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   685: aload_0
    //   686: invokespecial 165	java/lang/SecurityException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   689: athrow
    //   690: aload 9
    //   692: invokeinterface 369 1 0
    //   697: ifne +69 -> 766
    //   700: aload 5
    //   702: aload 9
    //   704: iconst_0
    //   705: invokeinterface 372 2 0
    //   710: checkcast 361	java/security/cert/X509Certificate
    //   713: invokevirtual 376	java/security/cert/X509Certificate:getPublicKey	()Ljava/security/PublicKey;
    //   716: invokeinterface 382 1 0
    //   721: invokestatic 386	java/util/Arrays:equals	([B[B)Z
    //   724: ifeq +31 -> 755
    //   727: aload_3
    //   728: invokestatic 130	android/util/apk/ApkSigningBlockUtils:getLengthPrefixedSlice	(Ljava/nio/ByteBuffer;)Ljava/nio/ByteBuffer;
    //   731: invokestatic 388	android/util/apk/ApkSignatureSchemeV2Verifier:verifyAdditionalAttributes	(Ljava/nio/ByteBuffer;)V
    //   734: aload 9
    //   736: aload 9
    //   738: invokeinterface 200 1 0
    //   743: anewarray 361	java/security/cert/X509Certificate
    //   746: invokeinterface 206 2 0
    //   751: checkcast 202	[Ljava/security/cert/X509Certificate;
    //   754: areturn
    //   755: new 48	java/lang/SecurityException
    //   758: dup
    //   759: ldc_w 390
    //   762: invokespecial 215	java/lang/SecurityException:<init>	(Ljava/lang/String;)V
    //   765: athrow
    //   766: new 48	java/lang/SecurityException
    //   769: dup
    //   770: ldc_w 392
    //   773: invokespecial 215	java/lang/SecurityException:<init>	(Ljava/lang/String;)V
    //   776: athrow
    //   777: new 48	java/lang/SecurityException
    //   780: dup
    //   781: ldc_w 394
    //   784: invokespecial 215	java/lang/SecurityException:<init>	(Ljava/lang/String;)V
    //   787: athrow
    //   788: new 146	java/lang/StringBuilder
    //   791: dup
    //   792: invokespecial 147	java/lang/StringBuilder:<init>	()V
    //   795: astore_0
    //   796: aload_0
    //   797: aload 9
    //   799: invokevirtual 153	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   802: pop
    //   803: aload_0
    //   804: ldc_w 396
    //   807: invokevirtual 153	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   810: pop
    //   811: new 48	java/lang/SecurityException
    //   814: dup
    //   815: aload_0
    //   816: invokevirtual 162	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   819: invokespecial 215	java/lang/SecurityException:<init>	(Ljava/lang/String;)V
    //   822: athrow
    //   823: astore_0
    //   824: new 146	java/lang/StringBuilder
    //   827: dup
    //   828: invokespecial 147	java/lang/StringBuilder:<init>	()V
    //   831: astore_1
    //   832: aload_1
    //   833: ldc_w 398
    //   836: invokevirtual 153	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   839: pop
    //   840: aload_1
    //   841: aload 9
    //   843: invokevirtual 153	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   846: pop
    //   847: aload_1
    //   848: ldc_w 400
    //   851: invokevirtual 153	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   854: pop
    //   855: new 48	java/lang/SecurityException
    //   858: dup
    //   859: aload_1
    //   860: invokevirtual 162	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   863: aload_0
    //   864: invokespecial 165	java/lang/SecurityException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   867: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	868	0	paramByteBuffer	ByteBuffer
    //   0	868	1	paramMap	java.util.Map<Integer, byte[]>
    //   0	868	2	paramCertificateFactory	java.security.cert.CertificateFactory
    //   4	724	3	localByteBuffer	ByteBuffer
    //   9	622	4	localObject1	Object
    //   15	686	5	arrayOfByte	byte[]
    //   24	457	6	localArrayList	java.util.ArrayList
    //   29	464	7	i	int
    //   32	510	8	j	int
    //   50	792	9	localObject2	Object
    //   67	343	10	k	int
    //   101	571	11	m	int
    //   218	147	12	localObject3	Object
    //   225	258	13	localObject4	Object
    //   257	156	14	localObject5	Object
    //   320	3	15	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   45	90	143	java/io/IOException
    //   45	90	143	java/nio/BufferUnderflowException
    //   103	113	143	java/io/IOException
    //   103	113	143	java/nio/BufferUnderflowException
    //   117	123	143	java/io/IOException
    //   117	123	143	java/nio/BufferUnderflowException
    //   130	143	143	java/io/IOException
    //   130	143	143	java/nio/BufferUnderflowException
    //   294	301	304	java/security/NoSuchAlgorithmException
    //   294	301	304	java/security/spec/InvalidKeySpecException
    //   294	301	304	java/security/InvalidKeyException
    //   294	301	304	java/security/InvalidAlgorithmParameterException
    //   294	301	304	java/security/SignatureException
    //   385	405	435	java/io/IOException
    //   385	405	435	java/nio/BufferUnderflowException
    //   412	419	435	java/io/IOException
    //   412	419	435	java/nio/BufferUnderflowException
    //   422	435	435	java/io/IOException
    //   422	435	435	java/nio/BufferUnderflowException
    //   371	378	439	java/io/IOException
    //   371	378	439	java/nio/BufferUnderflowException
    //   364	371	443	java/io/IOException
    //   364	371	443	java/nio/BufferUnderflowException
    //   613	624	645	java/security/cert/CertificateException
    //   607	613	649	java/security/cert/CertificateException
    //   602	607	653	java/security/cert/CertificateException
    //   247	289	823	java/security/NoSuchAlgorithmException
    //   247	289	823	java/security/spec/InvalidKeySpecException
    //   247	289	823	java/security/InvalidKeyException
    //   247	289	823	java/security/InvalidAlgorithmParameterException
    //   247	289	823	java/security/SignatureException
    //   308	322	823	java/security/NoSuchAlgorithmException
    //   308	322	823	java/security/spec/InvalidKeySpecException
    //   308	322	823	java/security/InvalidKeyException
    //   308	322	823	java/security/InvalidAlgorithmParameterException
    //   308	322	823	java/security/SignatureException
  }
  
  public static class VerifiedSigner
  {
    public final X509Certificate[][] certs;
    public final byte[] verityRootHash;
    
    public VerifiedSigner(X509Certificate[][] paramArrayOfX509Certificate, byte[] paramArrayOfByte)
    {
      certs = paramArrayOfX509Certificate;
      verityRootHash = paramArrayOfByte;
    }
  }
}
