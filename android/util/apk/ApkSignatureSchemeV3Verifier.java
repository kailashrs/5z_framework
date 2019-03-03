package android.util.apk;

import android.util.Pair;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class ApkSignatureSchemeV3Verifier
{
  private static final int APK_SIGNATURE_SCHEME_V3_BLOCK_ID = -262969152;
  private static final int PROOF_OF_ROTATION_ATTR_ID = 1000370060;
  public static final int SF_ATTRIBUTE_ANDROID_APK_SIGNED_ID = 3;
  
  public ApkSignatureSchemeV3Verifier() {}
  
  private static SignatureInfo findSignature(RandomAccessFile paramRandomAccessFile)
    throws IOException, SignatureNotFoundException
  {
    return ApkSigningBlockUtils.findSignature(paramRandomAccessFile, -262969152);
  }
  
  /* Error */
  static byte[] generateApkVerity(String paramString, ByteBufferFactory paramByteBufferFactory)
    throws IOException, SignatureNotFoundException, SecurityException, java.security.DigestException, NoSuchAlgorithmException
  {
    // Byte code:
    //   0: new 60	java/io/RandomAccessFile
    //   3: dup
    //   4: aload_0
    //   5: ldc 62
    //   7: invokespecial 65	java/io/RandomAccessFile:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   10: astore_2
    //   11: aconst_null
    //   12: astore_3
    //   13: aload_0
    //   14: aload_1
    //   15: aload_2
    //   16: invokestatic 67	android/util/apk/ApkSignatureSchemeV3Verifier:findSignature	(Ljava/io/RandomAccessFile;)Landroid/util/apk/SignatureInfo;
    //   19: invokestatic 70	android/util/apk/ApkSigningBlockUtils:generateApkVerity	(Ljava/lang/String;Landroid/util/apk/ByteBufferFactory;Landroid/util/apk/SignatureInfo;)[B
    //   22: astore_0
    //   23: aconst_null
    //   24: aload_2
    //   25: invokestatic 72	android/util/apk/ApkSignatureSchemeV3Verifier:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
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
    //   41: invokestatic 72	android/util/apk/ApkSignatureSchemeV3Verifier:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
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
    throws NoSuchAlgorithmException, java.security.DigestException, IOException, SignatureNotFoundException
  {
    // Byte code:
    //   0: new 60	java/io/RandomAccessFile
    //   3: dup
    //   4: aload_0
    //   5: ldc 62
    //   7: invokespecial 65	java/io/RandomAccessFile:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   10: astore_1
    //   11: aconst_null
    //   12: astore_2
    //   13: aload_2
    //   14: astore_0
    //   15: aload_1
    //   16: invokestatic 67	android/util/apk/ApkSignatureSchemeV3Verifier:findSignature	(Ljava/io/RandomAccessFile;)Landroid/util/apk/SignatureInfo;
    //   19: astore_3
    //   20: aload_2
    //   21: astore_0
    //   22: aload_1
    //   23: iconst_0
    //   24: invokestatic 78	android/util/apk/ApkSignatureSchemeV3Verifier:verify	(Ljava/io/RandomAccessFile;Z)Landroid/util/apk/ApkSignatureSchemeV3Verifier$VerifiedSigner;
    //   27: astore 4
    //   29: aload_2
    //   30: astore_0
    //   31: aload 4
    //   33: getfield 82	android/util/apk/ApkSignatureSchemeV3Verifier$VerifiedSigner:verityRootHash	[B
    //   36: astore 5
    //   38: aload 5
    //   40: ifnonnull +10 -> 50
    //   43: aconst_null
    //   44: aload_1
    //   45: invokestatic 72	android/util/apk/ApkSignatureSchemeV3Verifier:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   48: aconst_null
    //   49: areturn
    //   50: aload_2
    //   51: astore_0
    //   52: aload_1
    //   53: aload 4
    //   55: getfield 82	android/util/apk/ApkSignatureSchemeV3Verifier$VerifiedSigner:verityRootHash	[B
    //   58: invokestatic 88	java/nio/ByteBuffer:wrap	([B)Ljava/nio/ByteBuffer;
    //   61: aload_3
    //   62: invokestatic 93	android/util/apk/ApkVerityBuilder:generateFsverityRootHash	(Ljava/io/RandomAccessFile;Ljava/nio/ByteBuffer;Landroid/util/apk/SignatureInfo;)[B
    //   65: astore_2
    //   66: aconst_null
    //   67: aload_1
    //   68: invokestatic 72	android/util/apk/ApkSignatureSchemeV3Verifier:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
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
    //   84: invokestatic 72	android/util/apk/ApkSignatureSchemeV3Verifier:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
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
    //   0: new 60	java/io/RandomAccessFile
    //   3: dup
    //   4: aload_0
    //   5: ldc 62
    //   7: invokespecial 65	java/io/RandomAccessFile:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   10: astore_1
    //   11: aconst_null
    //   12: astore_2
    //   13: aload_2
    //   14: astore_0
    //   15: aload_1
    //   16: invokestatic 67	android/util/apk/ApkSignatureSchemeV3Verifier:findSignature	(Ljava/io/RandomAccessFile;)Landroid/util/apk/SignatureInfo;
    //   19: pop
    //   20: aload_2
    //   21: astore_0
    //   22: aload_1
    //   23: iconst_0
    //   24: invokestatic 78	android/util/apk/ApkSignatureSchemeV3Verifier:verify	(Ljava/io/RandomAccessFile;Z)Landroid/util/apk/ApkSignatureSchemeV3Verifier$VerifiedSigner;
    //   27: getfield 82	android/util/apk/ApkSignatureSchemeV3Verifier$VerifiedSigner:verityRootHash	[B
    //   30: astore_2
    //   31: aconst_null
    //   32: aload_1
    //   33: invokestatic 72	android/util/apk/ApkSignatureSchemeV3Verifier:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
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
    //   49: invokestatic 72	android/util/apk/ApkSignatureSchemeV3Verifier:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
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
    //   0: new 60	java/io/RandomAccessFile
    //   3: astore_1
    //   4: aload_1
    //   5: aload_0
    //   6: ldc 62
    //   8: invokespecial 65	java/io/RandomAccessFile:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   11: aconst_null
    //   12: astore_0
    //   13: aload_1
    //   14: invokestatic 67	android/util/apk/ApkSignatureSchemeV3Verifier:findSignature	(Ljava/io/RandomAccessFile;)Landroid/util/apk/SignatureInfo;
    //   17: pop
    //   18: aconst_null
    //   19: aload_1
    //   20: invokestatic 72	android/util/apk/ApkSignatureSchemeV3Verifier:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
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
    //   36: invokestatic 72	android/util/apk/ApkSignatureSchemeV3Verifier:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
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
  
  public static VerifiedSigner plsCertsNoVerifyOnlyCerts(String paramString)
    throws SignatureNotFoundException, SecurityException, IOException
  {
    return verify(paramString, false);
  }
  
  /* Error */
  private static VerifiedSigner verify(RandomAccessFile paramRandomAccessFile, SignatureInfo paramSignatureInfo, boolean paramBoolean)
    throws SecurityException, IOException
  {
    // Byte code:
    //   0: iconst_0
    //   1: istore_3
    //   2: new 110	android/util/ArrayMap
    //   5: dup
    //   6: invokespecial 111	android/util/ArrayMap:<init>	()V
    //   9: astore 4
    //   11: aconst_null
    //   12: astore 5
    //   14: ldc 113
    //   16: invokestatic 119	java/security/cert/CertificateFactory:getInstance	(Ljava/lang/String;)Ljava/security/cert/CertificateFactory;
    //   19: astore 6
    //   21: aload_1
    //   22: getfield 125	android/util/apk/SignatureInfo:signatureBlock	Ljava/nio/ByteBuffer;
    //   25: invokestatic 129	android/util/apk/ApkSigningBlockUtils:getLengthPrefixedSlice	(Ljava/nio/ByteBuffer;)Ljava/nio/ByteBuffer;
    //   28: astore 7
    //   30: aload 7
    //   32: invokevirtual 133	java/nio/ByteBuffer:hasRemaining	()Z
    //   35: ifeq +74 -> 109
    //   38: aload 7
    //   40: invokestatic 129	android/util/apk/ApkSigningBlockUtils:getLengthPrefixedSlice	(Ljava/nio/ByteBuffer;)Ljava/nio/ByteBuffer;
    //   43: aload 4
    //   45: aload 6
    //   47: invokestatic 137	android/util/apk/ApkSignatureSchemeV3Verifier:verifySigner	(Ljava/nio/ByteBuffer;Ljava/util/Map;Ljava/security/cert/CertificateFactory;)Landroid/util/apk/ApkSignatureSchemeV3Verifier$VerifiedSigner;
    //   50: astore 8
    //   52: aload 8
    //   54: astore 5
    //   56: iinc 3 1
    //   59: goto -29 -> 30
    //   62: astore_1
    //   63: new 139	java/lang/StringBuilder
    //   66: dup
    //   67: invokespecial 140	java/lang/StringBuilder:<init>	()V
    //   70: astore_0
    //   71: aload_0
    //   72: ldc -114
    //   74: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   77: pop
    //   78: aload_0
    //   79: iload_3
    //   80: invokevirtual 149	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   83: pop
    //   84: aload_0
    //   85: ldc -105
    //   87: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   90: pop
    //   91: new 54	java/lang/SecurityException
    //   94: dup
    //   95: aload_0
    //   96: invokevirtual 155	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   99: aload_1
    //   100: invokespecial 158	java/lang/SecurityException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   103: athrow
    //   104: astore 8
    //   106: goto -76 -> 30
    //   109: iload_3
    //   110: iconst_1
    //   111: if_icmplt +102 -> 213
    //   114: aload 5
    //   116: ifnull +97 -> 213
    //   119: iload_3
    //   120: iconst_1
    //   121: if_icmpne +82 -> 203
    //   124: aload 4
    //   126: invokeinterface 163 1 0
    //   131: ifne +62 -> 193
    //   134: iload_2
    //   135: ifeq +10 -> 145
    //   138: aload 4
    //   140: aload_0
    //   141: aload_1
    //   142: invokestatic 167	android/util/apk/ApkSigningBlockUtils:verifyIntegrity	(Ljava/util/Map;Ljava/io/RandomAccessFile;Landroid/util/apk/SignatureInfo;)V
    //   145: aload 4
    //   147: iconst_3
    //   148: invokestatic 173	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   151: invokeinterface 177 2 0
    //   156: ifeq +34 -> 190
    //   159: aload 4
    //   161: iconst_3
    //   162: invokestatic 173	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   165: invokeinterface 181 2 0
    //   170: checkcast 182	[B
    //   173: astore 8
    //   175: aload 5
    //   177: aload 8
    //   179: aload_0
    //   180: invokevirtual 186	java/io/RandomAccessFile:length	()J
    //   183: aload_1
    //   184: invokestatic 190	android/util/apk/ApkSigningBlockUtils:parseVerityDigestAndVerifySourceLength	([BJLandroid/util/apk/SignatureInfo;)[B
    //   187: putfield 82	android/util/apk/ApkSignatureSchemeV3Verifier$VerifiedSigner:verityRootHash	[B
    //   190: aload 5
    //   192: areturn
    //   193: new 54	java/lang/SecurityException
    //   196: dup
    //   197: ldc -64
    //   199: invokespecial 195	java/lang/SecurityException:<init>	(Ljava/lang/String;)V
    //   202: athrow
    //   203: new 54	java/lang/SecurityException
    //   206: dup
    //   207: ldc -59
    //   209: invokespecial 195	java/lang/SecurityException:<init>	(Ljava/lang/String;)V
    //   212: athrow
    //   213: new 54	java/lang/SecurityException
    //   216: dup
    //   217: ldc -57
    //   219: invokespecial 195	java/lang/SecurityException:<init>	(Ljava/lang/String;)V
    //   222: athrow
    //   223: astore_0
    //   224: new 54	java/lang/SecurityException
    //   227: dup
    //   228: ldc -55
    //   230: aload_0
    //   231: invokespecial 158	java/lang/SecurityException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   234: athrow
    //   235: astore_0
    //   236: new 203	java/lang/RuntimeException
    //   239: dup
    //   240: ldc -51
    //   242: aload_0
    //   243: invokespecial 206	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   246: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	247	0	paramRandomAccessFile	RandomAccessFile
    //   0	247	1	paramSignatureInfo	SignatureInfo
    //   0	247	2	paramBoolean	boolean
    //   1	121	3	i	int
    //   9	151	4	localArrayMap	android.util.ArrayMap
    //   12	179	5	localObject	Object
    //   19	27	6	localCertificateFactory	CertificateFactory
    //   28	11	7	localByteBuffer	ByteBuffer
    //   50	3	8	localVerifiedSigner	VerifiedSigner
    //   104	1	8	localPlatformNotSupportedException	PlatformNotSupportedException
    //   173	5	8	arrayOfByte	byte[]
    // Exception table:
    //   from	to	target	type
    //   38	52	62	java/io/IOException
    //   38	52	62	java/nio/BufferUnderflowException
    //   38	52	62	java/lang/SecurityException
    //   38	52	104	android/util/apk/ApkSignatureSchemeV3Verifier$PlatformNotSupportedException
    //   21	30	223	java/io/IOException
    //   14	21	235	java/security/cert/CertificateException
  }
  
  private static VerifiedSigner verify(RandomAccessFile paramRandomAccessFile, boolean paramBoolean)
    throws SignatureNotFoundException, SecurityException, IOException
  {
    return verify(paramRandomAccessFile, findSignature(paramRandomAccessFile), paramBoolean);
  }
  
  public static VerifiedSigner verify(String paramString)
    throws SignatureNotFoundException, SecurityException, IOException
  {
    return verify(paramString, true);
  }
  
  /* Error */
  private static VerifiedSigner verify(String paramString, boolean paramBoolean)
    throws SignatureNotFoundException, SecurityException, IOException
  {
    // Byte code:
    //   0: new 60	java/io/RandomAccessFile
    //   3: dup
    //   4: aload_0
    //   5: ldc 62
    //   7: invokespecial 65	java/io/RandomAccessFile:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   10: astore_2
    //   11: aconst_null
    //   12: astore_0
    //   13: aload_2
    //   14: iload_1
    //   15: invokestatic 78	android/util/apk/ApkSignatureSchemeV3Verifier:verify	(Ljava/io/RandomAccessFile;Z)Landroid/util/apk/ApkSignatureSchemeV3Verifier$VerifiedSigner;
    //   18: astore_3
    //   19: aconst_null
    //   20: aload_2
    //   21: invokestatic 72	android/util/apk/ApkSignatureSchemeV3Verifier:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
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
    //   37: invokestatic 72	android/util/apk/ApkSignatureSchemeV3Verifier:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
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
  
  private static VerifiedSigner verifyAdditionalAttributes(ByteBuffer paramByteBuffer, List<X509Certificate> paramList, CertificateFactory paramCertificateFactory)
    throws IOException
  {
    X509Certificate[] arrayOfX509Certificate = (X509Certificate[])paramList.toArray(new X509Certificate[paramList.size()]);
    paramList = null;
    ByteBuffer localByteBuffer;
    for (;;)
    {
      if (!paramByteBuffer.hasRemaining()) {
        break label197;
      }
      localByteBuffer = ApkSigningBlockUtils.getLengthPrefixedSlice(paramByteBuffer);
      if (localByteBuffer.remaining() < 4) {
        break label159;
      }
      if (localByteBuffer.getInt() == 1000370060)
      {
        if (paramList == null) {
          paramList = verifyProofOfRotationStruct(localByteBuffer, paramCertificateFactory);
        }
      }
      else {
        try
        {
          if ((certs.size() > 0) && (!Arrays.equals(((X509Certificate)certs.get(certs.size() - 1)).getEncoded(), arrayOfX509Certificate[0].getEncoded())))
          {
            paramByteBuffer = new java/lang/SecurityException;
            paramByteBuffer.<init>("Terminal certificate in Proof-of-rotation record does not match APK signing certificate");
            throw paramByteBuffer;
          }
        }
        catch (CertificateEncodingException paramByteBuffer)
        {
          throw new SecurityException("Failed to encode certificate when comparing Proof-of-rotation record and signing certificate", paramByteBuffer);
        }
      }
    }
    throw new SecurityException("Encountered multiple Proof-of-rotation records when verifying APK Signature Scheme v3 signature");
    label159:
    paramByteBuffer = new StringBuilder();
    paramByteBuffer.append("Remaining buffer too short to contain additional attribute ID. Remaining: ");
    paramByteBuffer.append(localByteBuffer.remaining());
    throw new IOException(paramByteBuffer.toString());
    label197:
    return new VerifiedSigner(arrayOfX509Certificate, paramList);
  }
  
  private static VerifiedProofOfRotation verifyProofOfRotationStruct(ByteBuffer paramByteBuffer, CertificateFactory paramCertificateFactory)
    throws SecurityException, IOException
  {
    int i = 0;
    int j = 0;
    int k = 0;
    int m = -1;
    Object localObject1 = null;
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    int n = i;
    int i1 = j;
    try
    {
      paramByteBuffer.getInt();
      n = i;
      i1 = j;
      HashSet localHashSet = new java/util/HashSet;
      n = i;
      i1 = j;
      localHashSet.<init>();
      for (;;)
      {
        n = k;
        i1 = k;
        if (paramByteBuffer.hasRemaining())
        {
          k++;
          n = k;
          i1 = k;
          Object localObject2 = ApkSigningBlockUtils.getLengthPrefixedSlice(paramByteBuffer);
          n = k;
          i1 = k;
          Object localObject3 = ApkSigningBlockUtils.getLengthPrefixedSlice((ByteBuffer)localObject2);
          n = k;
          i1 = k;
          j = ((ByteBuffer)localObject2).getInt();
          n = k;
          i1 = k;
          i = ((ByteBuffer)localObject2).getInt();
          n = k;
          i1 = k;
          byte[] arrayOfByte = ApkSigningBlockUtils.readLengthPrefixedByteArray((ByteBuffer)localObject2);
          if (localObject1 != null)
          {
            n = k;
            i1 = k;
            localObject2 = ApkSigningBlockUtils.getSignatureAlgorithmJcaSignatureAlgorithm(m);
            n = k;
            i1 = k;
            PublicKey localPublicKey = ((X509Certificate)localObject1).getPublicKey();
            n = k;
            i1 = k;
            Signature localSignature = Signature.getInstance((String)first);
            n = k;
            i1 = k;
            localSignature.initVerify(localPublicKey);
            n = k;
            i1 = k;
            if (second != null)
            {
              n = k;
              i1 = k;
              localSignature.setParameter((AlgorithmParameterSpec)second);
            }
            n = k;
            i1 = k;
            localSignature.update((ByteBuffer)localObject3);
            n = k;
            i1 = k;
            if (!localSignature.verify(arrayOfByte))
            {
              n = k;
              i1 = k;
              paramByteBuffer = new java/lang/SecurityException;
              n = k;
              i1 = k;
              paramCertificateFactory = new java/lang/StringBuilder;
              n = k;
              i1 = k;
              paramCertificateFactory.<init>();
              n = k;
              i1 = k;
              paramCertificateFactory.append("Unable to verify signature of certificate #");
              n = k;
              i1 = k;
              paramCertificateFactory.append(k);
              n = k;
              i1 = k;
              paramCertificateFactory.append(" using ");
              n = k;
              i1 = k;
              paramCertificateFactory.append((String)first);
              n = k;
              i1 = k;
              paramCertificateFactory.append(" when verifying Proof-of-rotation record");
              n = k;
              i1 = k;
              paramByteBuffer.<init>(paramCertificateFactory.toString());
              n = k;
              i1 = k;
              throw paramByteBuffer;
            }
          }
          n = k;
          i1 = k;
          ((ByteBuffer)localObject3).rewind();
          n = k;
          i1 = k;
          localObject2 = ApkSigningBlockUtils.readLengthPrefixedByteArray((ByteBuffer)localObject3);
          n = k;
          i1 = k;
          int i2 = ((ByteBuffer)localObject3).getInt();
          if ((localObject1 != null) && (m != i2))
          {
            n = k;
            i1 = k;
            paramByteBuffer = new java/lang/SecurityException;
            n = k;
            i1 = k;
            paramCertificateFactory = new java/lang/StringBuilder;
            n = k;
            i1 = k;
            paramCertificateFactory.<init>();
            n = k;
            i1 = k;
            paramCertificateFactory.append("Signing algorithm ID mismatch for certificate #");
            n = k;
            i1 = k;
            paramCertificateFactory.append(k);
            n = k;
            i1 = k;
            paramCertificateFactory.append(" when verifying Proof-of-rotation record");
            n = k;
            i1 = k;
            paramByteBuffer.<init>(paramCertificateFactory.toString());
            n = k;
            i1 = k;
            throw paramByteBuffer;
          }
          n = k;
          i1 = k;
          localObject1 = new java/io/ByteArrayInputStream;
          n = k;
          i1 = k;
          ((ByteArrayInputStream)localObject1).<init>((byte[])localObject2);
          try
          {
            localObject3 = (X509Certificate)paramCertificateFactory.generateCertificate((InputStream)localObject1);
            localObject1 = new android/util/apk/VerbatimX509Certificate;
            ((VerbatimX509Certificate)localObject1).<init>((X509Certificate)localObject3, (byte[])localObject2);
            m = i;
            if (!localHashSet.contains(localObject1))
            {
              localHashSet.add(localObject1);
              localArrayList1.add(localObject1);
              localArrayList2.add(Integer.valueOf(j));
            }
            else
            {
              paramByteBuffer = new java/lang/SecurityException;
              paramCertificateFactory = new java/lang/StringBuilder;
              paramCertificateFactory.<init>();
              paramCertificateFactory.append("Encountered duplicate entries in Proof-of-rotation record at certificate #");
              paramCertificateFactory.append(k);
              paramCertificateFactory.append(".  All signing certificates should be unique");
              paramByteBuffer.<init>(paramCertificateFactory.toString());
              throw paramByteBuffer;
            }
          }
          catch (CertificateException paramByteBuffer)
          {
            break label785;
          }
          catch (NoSuchAlgorithmException|InvalidKeyException|InvalidAlgorithmParameterException|SignatureException paramByteBuffer)
          {
            break label834;
          }
          catch (IOException|BufferUnderflowException paramByteBuffer) {}
        }
      }
      return new VerifiedProofOfRotation(localArrayList1, localArrayList2);
    }
    catch (CertificateException paramByteBuffer)
    {
      k = n;
      paramCertificateFactory = new StringBuilder();
      paramCertificateFactory.append("Failed to decode certificate #");
      paramCertificateFactory.append(k);
      paramCertificateFactory.append(" when verifying Proof-of-rotation record");
      throw new SecurityException(paramCertificateFactory.toString(), paramByteBuffer);
    }
    catch (NoSuchAlgorithmException|InvalidKeyException|InvalidAlgorithmParameterException|SignatureException paramByteBuffer)
    {
      k = i1;
      paramCertificateFactory = new StringBuilder();
      paramCertificateFactory.append("Failed to verify signature over signed data for certificate #");
      paramCertificateFactory.append(k);
      paramCertificateFactory.append(" when verifying Proof-of-rotation record");
      throw new SecurityException(paramCertificateFactory.toString(), paramByteBuffer);
    }
    catch (IOException|BufferUnderflowException paramByteBuffer)
    {
      label785:
      label834:
      throw new IOException("Failed to parse Proof-of-rotation record", paramByteBuffer);
    }
  }
  
  /* Error */
  private static VerifiedSigner verifySigner(ByteBuffer paramByteBuffer, java.util.Map<Integer, byte[]> paramMap, CertificateFactory paramCertificateFactory)
    throws SecurityException, IOException, ApkSignatureSchemeV3Verifier.PlatformNotSupportedException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic 129	android/util/apk/ApkSigningBlockUtils:getLengthPrefixedSlice	(Ljava/nio/ByteBuffer;)Ljava/nio/ByteBuffer;
    //   4: astore_3
    //   5: aload_0
    //   6: invokevirtual 232	java/nio/ByteBuffer:getInt	()I
    //   9: istore 4
    //   11: aload_0
    //   12: invokevirtual 232	java/nio/ByteBuffer:getInt	()I
    //   15: istore 5
    //   17: getstatic 378	android/os/Build$VERSION:SDK_INT	I
    //   20: iload 4
    //   22: if_icmplt +897 -> 919
    //   25: getstatic 378	android/os/Build$VERSION:SDK_INT	I
    //   28: iload 5
    //   30: if_icmpgt +889 -> 919
    //   33: aload_0
    //   34: invokestatic 129	android/util/apk/ApkSigningBlockUtils:getLengthPrefixedSlice	(Ljava/nio/ByteBuffer;)Ljava/nio/ByteBuffer;
    //   37: astore 6
    //   39: aload_0
    //   40: invokestatic 283	android/util/apk/ApkSigningBlockUtils:readLengthPrefixedByteArray	(Ljava/nio/ByteBuffer;)[B
    //   43: astore 7
    //   45: new 275	java/util/ArrayList
    //   48: dup
    //   49: invokespecial 276	java/util/ArrayList:<init>	()V
    //   52: astore 8
    //   54: aconst_null
    //   55: astore_0
    //   56: iconst_m1
    //   57: istore 9
    //   59: iconst_0
    //   60: istore 10
    //   62: aload 6
    //   64: invokevirtual 133	java/nio/ByteBuffer:hasRemaining	()Z
    //   67: ifeq +141 -> 208
    //   70: iinc 10 1
    //   73: aload 6
    //   75: invokestatic 129	android/util/apk/ApkSigningBlockUtils:getLengthPrefixedSlice	(Ljava/nio/ByteBuffer;)Ljava/nio/ByteBuffer;
    //   78: astore 11
    //   80: aload 11
    //   82: invokevirtual 229	java/nio/ByteBuffer:remaining	()I
    //   85: bipush 8
    //   87: if_icmplt +71 -> 158
    //   90: aload 11
    //   92: invokevirtual 232	java/nio/ByteBuffer:getInt	()I
    //   95: istore 12
    //   97: aload 8
    //   99: iload 12
    //   101: invokestatic 173	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   104: invokeinterface 357 2 0
    //   109: pop
    //   110: iload 12
    //   112: invokestatic 380	android/util/apk/ApkSignatureSchemeV3Verifier:isSupportedSignatureAlgorithm	(I)Z
    //   115: ifne +6 -> 121
    //   118: goto -56 -> 62
    //   121: iload 9
    //   123: iconst_m1
    //   124: if_icmpeq +17 -> 141
    //   127: iload 9
    //   129: istore 13
    //   131: iload 12
    //   133: iload 9
    //   135: invokestatic 384	android/util/apk/ApkSigningBlockUtils:compareSignatureAlgorithm	(II)I
    //   138: ifle +13 -> 151
    //   141: iload 12
    //   143: istore 13
    //   145: aload 11
    //   147: invokestatic 283	android/util/apk/ApkSigningBlockUtils:readLengthPrefixedByteArray	(Ljava/nio/ByteBuffer;)[B
    //   150: astore_0
    //   151: iload 13
    //   153: istore 9
    //   155: goto -93 -> 62
    //   158: new 54	java/lang/SecurityException
    //   161: astore_0
    //   162: aload_0
    //   163: ldc_w 386
    //   166: invokespecial 195	java/lang/SecurityException:<init>	(Ljava/lang/String;)V
    //   169: aload_0
    //   170: athrow
    //   171: astore_0
    //   172: new 139	java/lang/StringBuilder
    //   175: dup
    //   176: invokespecial 140	java/lang/StringBuilder:<init>	()V
    //   179: astore_1
    //   180: aload_1
    //   181: ldc_w 388
    //   184: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   187: pop
    //   188: aload_1
    //   189: iload 10
    //   191: invokevirtual 149	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   194: pop
    //   195: new 54	java/lang/SecurityException
    //   198: dup
    //   199: aload_1
    //   200: invokevirtual 155	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   203: aload_0
    //   204: invokespecial 158	java/lang/SecurityException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   207: athrow
    //   208: iload 9
    //   210: iconst_m1
    //   211: if_icmpne +30 -> 241
    //   214: iload 10
    //   216: ifne +14 -> 230
    //   219: new 54	java/lang/SecurityException
    //   222: dup
    //   223: ldc_w 390
    //   226: invokespecial 195	java/lang/SecurityException:<init>	(Ljava/lang/String;)V
    //   229: athrow
    //   230: new 54	java/lang/SecurityException
    //   233: dup
    //   234: ldc_w 392
    //   237: invokespecial 195	java/lang/SecurityException:<init>	(Ljava/lang/String;)V
    //   240: athrow
    //   241: iload 9
    //   243: invokestatic 396	android/util/apk/ApkSigningBlockUtils:getSignatureAlgorithmJcaKeyAlgorithm	(I)Ljava/lang/String;
    //   246: astore 6
    //   248: iload 9
    //   250: invokestatic 287	android/util/apk/ApkSigningBlockUtils:getSignatureAlgorithmJcaSignatureAlgorithm	(I)Landroid/util/Pair;
    //   253: astore 14
    //   255: aload 14
    //   257: getfield 297	android/util/Pair:first	Ljava/lang/Object;
    //   260: checkcast 299	java/lang/String
    //   263: astore 11
    //   265: aload 14
    //   267: getfield 311	android/util/Pair:second	Ljava/lang/Object;
    //   270: checkcast 313	java/security/spec/AlgorithmParameterSpec
    //   273: astore 14
    //   275: aload 6
    //   277: invokestatic 401	java/security/KeyFactory:getInstance	(Ljava/lang/String;)Ljava/security/KeyFactory;
    //   280: astore 15
    //   282: new 403	java/security/spec/X509EncodedKeySpec
    //   285: astore 16
    //   287: aload 16
    //   289: aload 7
    //   291: invokespecial 404	java/security/spec/X509EncodedKeySpec:<init>	([B)V
    //   294: aload 15
    //   296: aload 16
    //   298: invokevirtual 408	java/security/KeyFactory:generatePublic	(Ljava/security/spec/KeySpec;)Ljava/security/PublicKey;
    //   301: astore 16
    //   303: aload 11
    //   305: invokestatic 304	java/security/Signature:getInstance	(Ljava/lang/String;)Ljava/security/Signature;
    //   308: astore 15
    //   310: aload 15
    //   312: aload 16
    //   314: invokevirtual 308	java/security/Signature:initVerify	(Ljava/security/PublicKey;)V
    //   317: aload 14
    //   319: ifnull +17 -> 336
    //   322: aload 15
    //   324: aload 14
    //   326: invokevirtual 317	java/security/Signature:setParameter	(Ljava/security/spec/AlgorithmParameterSpec;)V
    //   329: goto +7 -> 336
    //   332: astore_0
    //   333: goto +542 -> 875
    //   336: aload 15
    //   338: aload_3
    //   339: invokevirtual 321	java/security/Signature:update	(Ljava/nio/ByteBuffer;)V
    //   342: aload 15
    //   344: aload_0
    //   345: invokevirtual 324	java/security/Signature:verify	([B)Z
    //   348: istore 17
    //   350: iload 17
    //   352: ifeq +487 -> 839
    //   355: aload_3
    //   356: invokevirtual 411	java/nio/ByteBuffer:clear	()Ljava/nio/Buffer;
    //   359: pop
    //   360: aload_3
    //   361: invokestatic 129	android/util/apk/ApkSigningBlockUtils:getLengthPrefixedSlice	(Ljava/nio/ByteBuffer;)Ljava/nio/ByteBuffer;
    //   364: astore 14
    //   366: new 275	java/util/ArrayList
    //   369: dup
    //   370: invokespecial 276	java/util/ArrayList:<init>	()V
    //   373: astore 11
    //   375: aconst_null
    //   376: astore 15
    //   378: iconst_0
    //   379: istore 13
    //   381: aload 14
    //   383: invokevirtual 133	java/nio/ByteBuffer:hasRemaining	()Z
    //   386: ifeq +126 -> 512
    //   389: iinc 13 1
    //   392: aload 14
    //   394: invokestatic 129	android/util/apk/ApkSigningBlockUtils:getLengthPrefixedSlice	(Ljava/nio/ByteBuffer;)Ljava/nio/ByteBuffer;
    //   397: astore 16
    //   399: aload 16
    //   401: invokevirtual 229	java/nio/ByteBuffer:remaining	()I
    //   404: istore 10
    //   406: iload 10
    //   408: bipush 8
    //   410: if_icmplt +44 -> 454
    //   413: aload 16
    //   415: invokevirtual 232	java/nio/ByteBuffer:getInt	()I
    //   418: istore 10
    //   420: aload 11
    //   422: iload 10
    //   424: invokestatic 173	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   427: invokeinterface 357 2 0
    //   432: pop
    //   433: iload 10
    //   435: iload 9
    //   437: if_icmpne +10 -> 447
    //   440: aload 16
    //   442: invokestatic 283	android/util/apk/ApkSigningBlockUtils:readLengthPrefixedByteArray	(Ljava/nio/ByteBuffer;)[B
    //   445: astore 15
    //   447: goto -66 -> 381
    //   450: astore_0
    //   451: goto +25 -> 476
    //   454: new 42	java/io/IOException
    //   457: astore_0
    //   458: aload_0
    //   459: ldc_w 413
    //   462: invokespecial 262	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   465: aload_0
    //   466: athrow
    //   467: astore_0
    //   468: goto +8 -> 476
    //   471: astore_0
    //   472: goto +4 -> 476
    //   475: astore_0
    //   476: new 139	java/lang/StringBuilder
    //   479: dup
    //   480: invokespecial 140	java/lang/StringBuilder:<init>	()V
    //   483: astore_1
    //   484: aload_1
    //   485: ldc_w 415
    //   488: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   491: pop
    //   492: aload_1
    //   493: iload 13
    //   495: invokevirtual 149	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   498: pop
    //   499: new 42	java/io/IOException
    //   502: dup
    //   503: aload_1
    //   504: invokevirtual 155	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   507: aload_0
    //   508: invokespecial 371	java/io/IOException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   511: athrow
    //   512: aload 8
    //   514: aload 11
    //   516: invokeinterface 417 2 0
    //   521: ifeq +307 -> 828
    //   524: iload 9
    //   526: invokestatic 421	android/util/apk/ApkSigningBlockUtils:getSignatureAlgorithmContentDigestAlgorithm	(I)I
    //   529: istore 13
    //   531: aload_1
    //   532: iload 13
    //   534: invokestatic 173	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   537: aload 15
    //   539: invokeinterface 425 3 0
    //   544: checkcast 182	[B
    //   547: astore_0
    //   548: aload_0
    //   549: ifnull +53 -> 602
    //   552: aload_0
    //   553: aload 15
    //   555: invokestatic 430	java/security/MessageDigest:isEqual	([B[B)Z
    //   558: ifeq +6 -> 564
    //   561: goto +41 -> 602
    //   564: new 139	java/lang/StringBuilder
    //   567: dup
    //   568: invokespecial 140	java/lang/StringBuilder:<init>	()V
    //   571: astore_0
    //   572: aload_0
    //   573: iload 13
    //   575: invokestatic 433	android/util/apk/ApkSigningBlockUtils:getContentDigestAlgorithmJcaDigestAlgorithm	(I)Ljava/lang/String;
    //   578: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   581: pop
    //   582: aload_0
    //   583: ldc_w 435
    //   586: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   589: pop
    //   590: new 54	java/lang/SecurityException
    //   593: dup
    //   594: aload_0
    //   595: invokevirtual 155	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   598: invokespecial 195	java/lang/SecurityException:<init>	(Ljava/lang/String;)V
    //   601: athrow
    //   602: aload_3
    //   603: invokestatic 129	android/util/apk/ApkSigningBlockUtils:getLengthPrefixedSlice	(Ljava/nio/ByteBuffer;)Ljava/nio/ByteBuffer;
    //   606: astore_0
    //   607: new 275	java/util/ArrayList
    //   610: dup
    //   611: invokespecial 276	java/util/ArrayList:<init>	()V
    //   614: astore 6
    //   616: iconst_0
    //   617: istore 10
    //   619: aload_0
    //   620: invokevirtual 133	java/nio/ByteBuffer:hasRemaining	()Z
    //   623: ifeq +95 -> 718
    //   626: iinc 10 1
    //   629: aload_0
    //   630: invokestatic 283	android/util/apk/ApkSigningBlockUtils:readLengthPrefixedByteArray	(Ljava/nio/ByteBuffer;)[B
    //   633: astore_1
    //   634: new 338	java/io/ByteArrayInputStream
    //   637: astore 11
    //   639: aload 11
    //   641: aload_1
    //   642: invokespecial 341	java/io/ByteArrayInputStream:<init>	([B)V
    //   645: aload_2
    //   646: aload 11
    //   648: invokevirtual 345	java/security/cert/CertificateFactory:generateCertificate	(Ljava/io/InputStream;)Ljava/security/cert/Certificate;
    //   651: checkcast 220	java/security/cert/X509Certificate
    //   654: astore 11
    //   656: aload 6
    //   658: new 347	android/util/apk/VerbatimX509Certificate
    //   661: dup
    //   662: aload 11
    //   664: aload_1
    //   665: invokespecial 350	android/util/apk/VerbatimX509Certificate:<init>	(Ljava/security/cert/X509Certificate;[B)V
    //   668: invokeinterface 357 2 0
    //   673: pop
    //   674: goto -55 -> 619
    //   677: astore_0
    //   678: goto +4 -> 682
    //   681: astore_0
    //   682: new 139	java/lang/StringBuilder
    //   685: dup
    //   686: invokespecial 140	java/lang/StringBuilder:<init>	()V
    //   689: astore_1
    //   690: aload_1
    //   691: ldc_w 366
    //   694: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   697: pop
    //   698: aload_1
    //   699: iload 10
    //   701: invokevirtual 149	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   704: pop
    //   705: new 54	java/lang/SecurityException
    //   708: dup
    //   709: aload_1
    //   710: invokevirtual 155	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   713: aload_0
    //   714: invokespecial 158	java/lang/SecurityException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   717: athrow
    //   718: aload 6
    //   720: invokeinterface 436 1 0
    //   725: ifne +92 -> 817
    //   728: aload 7
    //   730: aload 6
    //   732: iconst_0
    //   733: invokeinterface 243 2 0
    //   738: checkcast 220	java/security/cert/X509Certificate
    //   741: invokevirtual 291	java/security/cert/X509Certificate:getPublicKey	()Ljava/security/PublicKey;
    //   744: invokeinterface 439 1 0
    //   749: invokestatic 253	java/util/Arrays:equals	([B[B)Z
    //   752: ifeq +54 -> 806
    //   755: aload_3
    //   756: invokevirtual 232	java/nio/ByteBuffer:getInt	()I
    //   759: iload 4
    //   761: if_icmpne +34 -> 795
    //   764: aload_3
    //   765: invokevirtual 232	java/nio/ByteBuffer:getInt	()I
    //   768: iload 5
    //   770: if_icmpne +14 -> 784
    //   773: aload_3
    //   774: invokestatic 129	android/util/apk/ApkSigningBlockUtils:getLengthPrefixedSlice	(Ljava/nio/ByteBuffer;)Ljava/nio/ByteBuffer;
    //   777: aload 6
    //   779: aload_2
    //   780: invokestatic 441	android/util/apk/ApkSignatureSchemeV3Verifier:verifyAdditionalAttributes	(Ljava/nio/ByteBuffer;Ljava/util/List;Ljava/security/cert/CertificateFactory;)Landroid/util/apk/ApkSignatureSchemeV3Verifier$VerifiedSigner;
    //   783: areturn
    //   784: new 54	java/lang/SecurityException
    //   787: dup
    //   788: ldc_w 443
    //   791: invokespecial 195	java/lang/SecurityException:<init>	(Ljava/lang/String;)V
    //   794: athrow
    //   795: new 54	java/lang/SecurityException
    //   798: dup
    //   799: ldc_w 445
    //   802: invokespecial 195	java/lang/SecurityException:<init>	(Ljava/lang/String;)V
    //   805: athrow
    //   806: new 54	java/lang/SecurityException
    //   809: dup
    //   810: ldc_w 447
    //   813: invokespecial 195	java/lang/SecurityException:<init>	(Ljava/lang/String;)V
    //   816: athrow
    //   817: new 54	java/lang/SecurityException
    //   820: dup
    //   821: ldc_w 449
    //   824: invokespecial 195	java/lang/SecurityException:<init>	(Ljava/lang/String;)V
    //   827: athrow
    //   828: new 54	java/lang/SecurityException
    //   831: dup
    //   832: ldc_w 451
    //   835: invokespecial 195	java/lang/SecurityException:<init>	(Ljava/lang/String;)V
    //   838: athrow
    //   839: new 139	java/lang/StringBuilder
    //   842: dup
    //   843: invokespecial 140	java/lang/StringBuilder:<init>	()V
    //   846: astore_0
    //   847: aload_0
    //   848: aload 11
    //   850: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   853: pop
    //   854: aload_0
    //   855: ldc_w 453
    //   858: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   861: pop
    //   862: new 54	java/lang/SecurityException
    //   865: dup
    //   866: aload_0
    //   867: invokevirtual 155	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   870: invokespecial 195	java/lang/SecurityException:<init>	(Ljava/lang/String;)V
    //   873: athrow
    //   874: astore_0
    //   875: new 139	java/lang/StringBuilder
    //   878: dup
    //   879: invokespecial 140	java/lang/StringBuilder:<init>	()V
    //   882: astore_1
    //   883: aload_1
    //   884: ldc_w 455
    //   887: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   890: pop
    //   891: aload_1
    //   892: aload 11
    //   894: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   897: pop
    //   898: aload_1
    //   899: ldc_w 457
    //   902: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   905: pop
    //   906: new 54	java/lang/SecurityException
    //   909: dup
    //   910: aload_1
    //   911: invokevirtual 155	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   914: aload_0
    //   915: invokespecial 158	java/lang/SecurityException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   918: athrow
    //   919: new 139	java/lang/StringBuilder
    //   922: dup
    //   923: invokespecial 140	java/lang/StringBuilder:<init>	()V
    //   926: astore_0
    //   927: aload_0
    //   928: ldc_w 459
    //   931: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   934: pop
    //   935: aload_0
    //   936: getstatic 378	android/os/Build$VERSION:SDK_INT	I
    //   939: invokevirtual 149	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   942: pop
    //   943: aload_0
    //   944: ldc_w 461
    //   947: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   950: pop
    //   951: aload_0
    //   952: iload 4
    //   954: invokevirtual 149	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   957: pop
    //   958: aload_0
    //   959: ldc_w 463
    //   962: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   965: pop
    //   966: aload_0
    //   967: iload 5
    //   969: invokevirtual 149	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   972: pop
    //   973: new 6	android/util/apk/ApkSignatureSchemeV3Verifier$PlatformNotSupportedException
    //   976: dup
    //   977: aload_0
    //   978: invokevirtual 155	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   981: invokespecial 464	android/util/apk/ApkSignatureSchemeV3Verifier$PlatformNotSupportedException:<init>	(Ljava/lang/String;)V
    //   984: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	985	0	paramByteBuffer	ByteBuffer
    //   0	985	1	paramMap	java.util.Map<Integer, byte[]>
    //   0	985	2	paramCertificateFactory	CertificateFactory
    //   4	770	3	localByteBuffer	ByteBuffer
    //   9	944	4	i	int
    //   15	953	5	j	int
    //   37	741	6	localObject1	Object
    //   43	686	7	arrayOfByte	byte[]
    //   52	461	8	localArrayList	ArrayList
    //   57	468	9	k	int
    //   60	640	10	m	int
    //   78	815	11	localObject2	Object
    //   95	47	12	n	int
    //   129	445	13	i1	int
    //   253	140	14	localObject3	Object
    //   280	274	15	localObject4	Object
    //   285	156	16	localObject5	Object
    //   348	3	17	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   73	118	171	java/io/IOException
    //   73	118	171	java/nio/BufferUnderflowException
    //   131	141	171	java/io/IOException
    //   131	141	171	java/nio/BufferUnderflowException
    //   145	151	171	java/io/IOException
    //   145	151	171	java/nio/BufferUnderflowException
    //   158	171	171	java/io/IOException
    //   158	171	171	java/nio/BufferUnderflowException
    //   322	329	332	java/security/NoSuchAlgorithmException
    //   322	329	332	java/security/spec/InvalidKeySpecException
    //   322	329	332	java/security/InvalidKeyException
    //   322	329	332	java/security/InvalidAlgorithmParameterException
    //   322	329	332	java/security/SignatureException
    //   413	420	450	java/io/IOException
    //   413	420	450	java/nio/BufferUnderflowException
    //   420	433	467	java/io/IOException
    //   420	433	467	java/nio/BufferUnderflowException
    //   440	447	467	java/io/IOException
    //   440	447	467	java/nio/BufferUnderflowException
    //   454	467	467	java/io/IOException
    //   454	467	467	java/nio/BufferUnderflowException
    //   399	406	471	java/io/IOException
    //   399	406	471	java/nio/BufferUnderflowException
    //   392	399	475	java/io/IOException
    //   392	399	475	java/nio/BufferUnderflowException
    //   639	656	677	java/security/cert/CertificateException
    //   634	639	681	java/security/cert/CertificateException
    //   275	317	874	java/security/NoSuchAlgorithmException
    //   275	317	874	java/security/spec/InvalidKeySpecException
    //   275	317	874	java/security/InvalidKeyException
    //   275	317	874	java/security/InvalidAlgorithmParameterException
    //   275	317	874	java/security/SignatureException
    //   336	350	874	java/security/NoSuchAlgorithmException
    //   336	350	874	java/security/spec/InvalidKeySpecException
    //   336	350	874	java/security/InvalidKeyException
    //   336	350	874	java/security/InvalidAlgorithmParameterException
    //   336	350	874	java/security/SignatureException
  }
  
  private static class PlatformNotSupportedException
    extends Exception
  {
    PlatformNotSupportedException(String paramString)
    {
      super();
    }
  }
  
  public static class VerifiedProofOfRotation
  {
    public final List<X509Certificate> certs;
    public final List<Integer> flagsList;
    
    public VerifiedProofOfRotation(List<X509Certificate> paramList, List<Integer> paramList1)
    {
      certs = paramList;
      flagsList = paramList1;
    }
  }
  
  public static class VerifiedSigner
  {
    public final X509Certificate[] certs;
    public final ApkSignatureSchemeV3Verifier.VerifiedProofOfRotation por;
    public byte[] verityRootHash;
    
    public VerifiedSigner(X509Certificate[] paramArrayOfX509Certificate, ApkSignatureSchemeV3Verifier.VerifiedProofOfRotation paramVerifiedProofOfRotation)
    {
      certs = paramArrayOfX509Certificate;
      por = paramVerifiedProofOfRotation;
    }
  }
}
