package android.util.apk;

import android.content.pm.Signature;
import android.util.ArrayMap;
import android.util.BoostFramework;
import android.util.jar.StrictJarFile;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.ZipEntry;

public class ApkSignatureVerifier
{
  private static final int NUMBER_OF_CORES;
  private static final String TAG = "ApkSignatureVerifier";
  private static final AtomicReference<byte[]> sBuffer = new AtomicReference();
  private static boolean sIsPerfLockAcquired = false;
  private static BoostFramework sPerfBoost;
  
  static
  {
    int i = Runtime.getRuntime().availableProcessors();
    int j = 4;
    if (i < 4) {
      j = Runtime.getRuntime().availableProcessors();
    }
    NUMBER_OF_CORES = j;
    sPerfBoost = null;
  }
  
  public ApkSignatureVerifier() {}
  
  private static void closeQuietly(StrictJarFile paramStrictJarFile)
  {
    if (paramStrictJarFile != null) {
      try
      {
        paramStrictJarFile.close();
      }
      catch (Exception paramStrictJarFile) {}
    }
  }
  
  public static Signature[] convertToSignatures(Certificate[][] paramArrayOfCertificate)
    throws CertificateEncodingException
  {
    Signature[] arrayOfSignature = new Signature[paramArrayOfCertificate.length];
    for (int i = 0; i < paramArrayOfCertificate.length; i++) {
      arrayOfSignature[i] = new Signature(paramArrayOfCertificate[i]);
    }
    return arrayOfSignature;
  }
  
  public static byte[] generateApkVerity(String paramString, ByteBufferFactory paramByteBufferFactory)
    throws IOException, SignatureNotFoundException, SecurityException, DigestException, NoSuchAlgorithmException
  {
    try
    {
      byte[] arrayOfByte = ApkSignatureSchemeV3Verifier.generateApkVerity(paramString, paramByteBufferFactory);
      return arrayOfByte;
    }
    catch (SignatureNotFoundException localSignatureNotFoundException) {}
    return ApkSignatureSchemeV2Verifier.generateApkVerity(paramString, paramByteBufferFactory);
  }
  
  public static byte[] generateFsverityRootHash(String paramString)
    throws NoSuchAlgorithmException, DigestException, IOException
  {
    try
    {
      byte[] arrayOfByte = ApkSignatureSchemeV3Verifier.generateFsverityRootHash(paramString);
      return arrayOfByte;
    }
    catch (SignatureNotFoundException localSignatureNotFoundException)
    {
      try
      {
        paramString = ApkSignatureSchemeV2Verifier.generateFsverityRootHash(paramString);
        return paramString;
      }
      catch (SignatureNotFoundException paramString) {}
    }
    return null;
  }
  
  public static byte[] getVerityRootHash(String paramString)
    throws IOException, SignatureNotFoundException, SecurityException
  {
    try
    {
      byte[] arrayOfByte = ApkSignatureSchemeV3Verifier.getVerityRootHash(paramString);
      return arrayOfByte;
    }
    catch (SignatureNotFoundException localSignatureNotFoundException) {}
    return ApkSignatureSchemeV2Verifier.getVerityRootHash(paramString);
  }
  
  /* Error */
  private static Certificate[][] loadCertificates(StrictJarFile paramStrictJarFile, ZipEntry paramZipEntry)
    throws android.content.pm.PackageParser.PackageParserException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: aload_0
    //   5: aload_1
    //   6: invokevirtual 113	android/util/jar/StrictJarFile:getInputStream	(Ljava/util/zip/ZipEntry;)Ljava/io/InputStream;
    //   9: astore 4
    //   11: aload 4
    //   13: astore_3
    //   14: aload 4
    //   16: astore_2
    //   17: aload 4
    //   19: invokestatic 117	android/util/apk/ApkSignatureVerifier:readFullyIgnoringContents	(Ljava/io/InputStream;)V
    //   22: aload 4
    //   24: astore_3
    //   25: aload 4
    //   27: astore_2
    //   28: aload_0
    //   29: aload_1
    //   30: invokevirtual 121	android/util/jar/StrictJarFile:getCertificateChains	(Ljava/util/zip/ZipEntry;)[[Ljava/security/cert/Certificate;
    //   33: astore 5
    //   35: aload 4
    //   37: invokestatic 126	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   40: aload 5
    //   42: areturn
    //   43: astore_0
    //   44: goto +88 -> 132
    //   47: astore 6
    //   49: aload_2
    //   50: astore_3
    //   51: new 57	android/content/pm/PackageParser$PackageParserException
    //   54: astore 5
    //   56: aload_2
    //   57: astore_3
    //   58: new 128	java/lang/StringBuilder
    //   61: astore 4
    //   63: aload_2
    //   64: astore_3
    //   65: aload 4
    //   67: invokespecial 129	java/lang/StringBuilder:<init>	()V
    //   70: aload_2
    //   71: astore_3
    //   72: aload 4
    //   74: ldc -125
    //   76: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   79: pop
    //   80: aload_2
    //   81: astore_3
    //   82: aload 4
    //   84: aload_1
    //   85: invokevirtual 141	java/util/zip/ZipEntry:getName	()Ljava/lang/String;
    //   88: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   91: pop
    //   92: aload_2
    //   93: astore_3
    //   94: aload 4
    //   96: ldc -113
    //   98: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   101: pop
    //   102: aload_2
    //   103: astore_3
    //   104: aload 4
    //   106: aload_0
    //   107: invokevirtual 146	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   110: pop
    //   111: aload_2
    //   112: astore_3
    //   113: aload 5
    //   115: bipush -102
    //   117: aload 4
    //   119: invokevirtual 149	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   122: aload 6
    //   124: invokespecial 152	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;Ljava/lang/Throwable;)V
    //   127: aload_2
    //   128: astore_3
    //   129: aload 5
    //   131: athrow
    //   132: aload_3
    //   133: invokestatic 126	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   136: aload_0
    //   137: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	138	0	paramStrictJarFile	StrictJarFile
    //   0	138	1	paramZipEntry	ZipEntry
    //   1	127	2	localObject1	Object
    //   3	130	3	localObject2	Object
    //   9	109	4	localObject3	Object
    //   33	97	5	localObject4	Object
    //   47	76	6	localIOException	IOException
    // Exception table:
    //   from	to	target	type
    //   4	11	43	finally
    //   17	22	43	finally
    //   28	35	43	finally
    //   51	56	43	finally
    //   58	63	43	finally
    //   65	70	43	finally
    //   72	80	43	finally
    //   82	92	43	finally
    //   94	102	43	finally
    //   104	111	43	finally
    //   113	127	43	finally
    //   129	132	43	finally
    //   4	11	47	java/io/IOException
    //   4	11	47	java/lang/RuntimeException
    //   17	22	47	java/io/IOException
    //   17	22	47	java/lang/RuntimeException
    //   28	35	47	java/io/IOException
    //   28	35	47	java/lang/RuntimeException
  }
  
  /* Error */
  public static android.content.pm.PackageParser.SigningDetails plsCertsNoVerifyOnlyCerts(String paramString, int paramInt)
    throws android.content.pm.PackageParser.PackageParserException
  {
    // Byte code:
    //   0: iload_1
    //   1: iconst_3
    //   2: if_icmpgt +604 -> 606
    //   5: ldc2_w 155
    //   8: ldc -98
    //   10: invokestatic 164	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   13: aload_0
    //   14: invokestatic 167	android/util/apk/ApkSignatureSchemeV3Verifier:plsCertsNoVerifyOnlyCerts	(Ljava/lang/String;)Landroid/util/apk/ApkSignatureSchemeV3Verifier$VerifiedSigner;
    //   17: astore_2
    //   18: iconst_1
    //   19: anewarray 169	[Ljava/security/cert/Certificate;
    //   22: dup
    //   23: iconst_0
    //   24: aload_2
    //   25: getfield 175	android/util/apk/ApkSignatureSchemeV3Verifier$VerifiedSigner:certs	[Ljava/security/cert/X509Certificate;
    //   28: aastore
    //   29: invokestatic 177	android/util/apk/ApkSignatureVerifier:convertToSignatures	([[Ljava/security/cert/Certificate;)[Landroid/content/pm/Signature;
    //   32: astore_3
    //   33: aconst_null
    //   34: astore 4
    //   36: aconst_null
    //   37: astore 5
    //   39: aload_2
    //   40: getfield 181	android/util/apk/ApkSignatureSchemeV3Verifier$VerifiedSigner:por	Landroid/util/apk/ApkSignatureSchemeV3Verifier$VerifiedProofOfRotation;
    //   43: ifnull +123 -> 166
    //   46: aload_2
    //   47: getfield 181	android/util/apk/ApkSignatureSchemeV3Verifier$VerifiedSigner:por	Landroid/util/apk/ApkSignatureSchemeV3Verifier$VerifiedProofOfRotation;
    //   50: getfield 186	android/util/apk/ApkSignatureSchemeV3Verifier$VerifiedProofOfRotation:certs	Ljava/util/List;
    //   53: invokeinterface 191 1 0
    //   58: anewarray 76	android/content/pm/Signature
    //   61: astore 6
    //   63: aload_2
    //   64: getfield 181	android/util/apk/ApkSignatureSchemeV3Verifier$VerifiedSigner:por	Landroid/util/apk/ApkSignatureSchemeV3Verifier$VerifiedProofOfRotation;
    //   67: getfield 194	android/util/apk/ApkSignatureSchemeV3Verifier$VerifiedProofOfRotation:flagsList	Ljava/util/List;
    //   70: invokeinterface 191 1 0
    //   75: newarray int
    //   77: astore 7
    //   79: iconst_0
    //   80: istore 8
    //   82: aload 6
    //   84: astore 4
    //   86: aload 7
    //   88: astore 5
    //   90: iload 8
    //   92: aload 6
    //   94: arraylength
    //   95: if_icmpge +71 -> 166
    //   98: new 76	android/content/pm/Signature
    //   101: astore 4
    //   103: aload 4
    //   105: aload_2
    //   106: getfield 181	android/util/apk/ApkSignatureSchemeV3Verifier$VerifiedSigner:por	Landroid/util/apk/ApkSignatureSchemeV3Verifier$VerifiedProofOfRotation;
    //   109: getfield 186	android/util/apk/ApkSignatureSchemeV3Verifier$VerifiedProofOfRotation:certs	Ljava/util/List;
    //   112: iload 8
    //   114: invokeinterface 198 2 0
    //   119: checkcast 200	java/security/cert/X509Certificate
    //   122: invokevirtual 204	java/security/cert/X509Certificate:getEncoded	()[B
    //   125: invokespecial 207	android/content/pm/Signature:<init>	([B)V
    //   128: aload 6
    //   130: iload 8
    //   132: aload 4
    //   134: aastore
    //   135: aload 7
    //   137: iload 8
    //   139: aload_2
    //   140: getfield 181	android/util/apk/ApkSignatureSchemeV3Verifier$VerifiedSigner:por	Landroid/util/apk/ApkSignatureSchemeV3Verifier$VerifiedProofOfRotation;
    //   143: getfield 194	android/util/apk/ApkSignatureSchemeV3Verifier$VerifiedProofOfRotation:flagsList	Ljava/util/List;
    //   146: iload 8
    //   148: invokeinterface 198 2 0
    //   153: checkcast 209	java/lang/Integer
    //   156: invokevirtual 212	java/lang/Integer:intValue	()I
    //   159: iastore
    //   160: iinc 8 1
    //   163: goto -81 -> 82
    //   166: new 214	android/content/pm/PackageParser$SigningDetails
    //   169: dup
    //   170: aload_3
    //   171: iconst_3
    //   172: aload 4
    //   174: aload 5
    //   176: invokespecial 217	android/content/pm/PackageParser$SigningDetails:<init>	([Landroid/content/pm/Signature;I[Landroid/content/pm/Signature;[I)V
    //   179: astore 4
    //   181: ldc2_w 155
    //   184: invokestatic 221	android/os/Trace:traceEnd	(J)V
    //   187: aload 4
    //   189: areturn
    //   190: astore_0
    //   191: goto +407 -> 598
    //   194: astore 5
    //   196: new 57	android/content/pm/PackageParser$PackageParserException
    //   199: astore 7
    //   201: new 128	java/lang/StringBuilder
    //   204: astore 4
    //   206: aload 4
    //   208: invokespecial 129	java/lang/StringBuilder:<init>	()V
    //   211: aload 4
    //   213: ldc -33
    //   215: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   218: pop
    //   219: aload 4
    //   221: aload_0
    //   222: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   225: pop
    //   226: aload 4
    //   228: ldc -31
    //   230: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   233: pop
    //   234: aload 7
    //   236: bipush -103
    //   238: aload 4
    //   240: invokevirtual 149	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   243: aload 5
    //   245: invokespecial 152	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;Ljava/lang/Throwable;)V
    //   248: aload 7
    //   250: athrow
    //   251: astore 4
    //   253: iload_1
    //   254: iconst_3
    //   255: if_icmpge +296 -> 551
    //   258: ldc2_w 155
    //   261: invokestatic 221	android/os/Trace:traceEnd	(J)V
    //   264: iload_1
    //   265: iconst_2
    //   266: if_icmpgt +231 -> 497
    //   269: ldc2_w 155
    //   272: ldc -29
    //   274: invokestatic 164	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   277: new 214	android/content/pm/PackageParser$SigningDetails
    //   280: dup
    //   281: aload_0
    //   282: invokestatic 230	android/util/apk/ApkSignatureSchemeV2Verifier:plsCertsNoVerifyOnlyCerts	(Ljava/lang/String;)[[Ljava/security/cert/X509Certificate;
    //   285: invokestatic 177	android/util/apk/ApkSignatureVerifier:convertToSignatures	([[Ljava/security/cert/Certificate;)[Landroid/content/pm/Signature;
    //   288: iconst_2
    //   289: invokespecial 233	android/content/pm/PackageParser$SigningDetails:<init>	([Landroid/content/pm/Signature;I)V
    //   292: astore 4
    //   294: ldc2_w 155
    //   297: invokestatic 221	android/os/Trace:traceEnd	(J)V
    //   300: aload 4
    //   302: areturn
    //   303: astore_0
    //   304: goto +185 -> 489
    //   307: astore 7
    //   309: new 57	android/content/pm/PackageParser$PackageParserException
    //   312: astore 4
    //   314: new 128	java/lang/StringBuilder
    //   317: astore 5
    //   319: aload 5
    //   321: invokespecial 129	java/lang/StringBuilder:<init>	()V
    //   324: aload 5
    //   326: ldc -33
    //   328: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   331: pop
    //   332: aload 5
    //   334: aload_0
    //   335: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   338: pop
    //   339: aload 5
    //   341: ldc -21
    //   343: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   346: pop
    //   347: aload 4
    //   349: bipush -103
    //   351: aload 5
    //   353: invokevirtual 149	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   356: aload 7
    //   358: invokespecial 152	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;Ljava/lang/Throwable;)V
    //   361: aload 4
    //   363: athrow
    //   364: astore 7
    //   366: iload_1
    //   367: iconst_2
    //   368: if_icmpge +74 -> 442
    //   371: ldc2_w 155
    //   374: invokestatic 221	android/os/Trace:traceEnd	(J)V
    //   377: iload_1
    //   378: iconst_1
    //   379: if_icmpgt +9 -> 388
    //   382: aload_0
    //   383: iconst_0
    //   384: invokestatic 239	android/util/apk/ApkSignatureVerifier:verifyV1Signature	(Ljava/lang/String;Z)Landroid/content/pm/PackageParser$SigningDetails;
    //   387: areturn
    //   388: new 128	java/lang/StringBuilder
    //   391: dup
    //   392: invokespecial 129	java/lang/StringBuilder:<init>	()V
    //   395: astore 4
    //   397: aload 4
    //   399: ldc -15
    //   401: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   404: pop
    //   405: aload 4
    //   407: iload_1
    //   408: invokevirtual 244	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   411: pop
    //   412: aload 4
    //   414: ldc -10
    //   416: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   419: pop
    //   420: aload 4
    //   422: aload_0
    //   423: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   426: pop
    //   427: new 57	android/content/pm/PackageParser$PackageParserException
    //   430: dup
    //   431: bipush -103
    //   433: aload 4
    //   435: invokevirtual 149	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   438: invokespecial 249	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;)V
    //   441: athrow
    //   442: new 57	android/content/pm/PackageParser$PackageParserException
    //   445: astore 4
    //   447: new 128	java/lang/StringBuilder
    //   450: astore 5
    //   452: aload 5
    //   454: invokespecial 129	java/lang/StringBuilder:<init>	()V
    //   457: aload 5
    //   459: ldc -5
    //   461: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   464: pop
    //   465: aload 5
    //   467: aload_0
    //   468: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   471: pop
    //   472: aload 4
    //   474: bipush -103
    //   476: aload 5
    //   478: invokevirtual 149	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   481: aload 7
    //   483: invokespecial 152	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;Ljava/lang/Throwable;)V
    //   486: aload 4
    //   488: athrow
    //   489: ldc2_w 155
    //   492: invokestatic 221	android/os/Trace:traceEnd	(J)V
    //   495: aload_0
    //   496: athrow
    //   497: new 128	java/lang/StringBuilder
    //   500: dup
    //   501: invokespecial 129	java/lang/StringBuilder:<init>	()V
    //   504: astore 4
    //   506: aload 4
    //   508: ldc -15
    //   510: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   513: pop
    //   514: aload 4
    //   516: iload_1
    //   517: invokevirtual 244	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   520: pop
    //   521: aload 4
    //   523: ldc -10
    //   525: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   528: pop
    //   529: aload 4
    //   531: aload_0
    //   532: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   535: pop
    //   536: new 57	android/content/pm/PackageParser$PackageParserException
    //   539: dup
    //   540: bipush -103
    //   542: aload 4
    //   544: invokevirtual 149	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   547: invokespecial 249	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;)V
    //   550: athrow
    //   551: new 57	android/content/pm/PackageParser$PackageParserException
    //   554: astore 7
    //   556: new 128	java/lang/StringBuilder
    //   559: astore 5
    //   561: aload 5
    //   563: invokespecial 129	java/lang/StringBuilder:<init>	()V
    //   566: aload 5
    //   568: ldc -3
    //   570: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   573: pop
    //   574: aload 5
    //   576: aload_0
    //   577: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   580: pop
    //   581: aload 7
    //   583: bipush -103
    //   585: aload 5
    //   587: invokevirtual 149	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   590: aload 4
    //   592: invokespecial 152	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;Ljava/lang/Throwable;)V
    //   595: aload 7
    //   597: athrow
    //   598: ldc2_w 155
    //   601: invokestatic 221	android/os/Trace:traceEnd	(J)V
    //   604: aload_0
    //   605: athrow
    //   606: new 128	java/lang/StringBuilder
    //   609: dup
    //   610: invokespecial 129	java/lang/StringBuilder:<init>	()V
    //   613: astore 4
    //   615: aload 4
    //   617: ldc -15
    //   619: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   622: pop
    //   623: aload 4
    //   625: iload_1
    //   626: invokevirtual 244	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   629: pop
    //   630: aload 4
    //   632: ldc -10
    //   634: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   637: pop
    //   638: aload 4
    //   640: aload_0
    //   641: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   644: pop
    //   645: new 57	android/content/pm/PackageParser$PackageParserException
    //   648: dup
    //   649: bipush -103
    //   651: aload 4
    //   653: invokevirtual 149	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   656: invokespecial 249	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;)V
    //   659: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	660	0	paramString	String
    //   0	660	1	paramInt	int
    //   17	123	2	localVerifiedSigner	ApkSignatureSchemeV3Verifier.VerifiedSigner
    //   32	139	3	arrayOfSignature1	Signature[]
    //   34	205	4	localObject1	Object
    //   251	1	4	localSignatureNotFoundException1	SignatureNotFoundException
    //   292	360	4	localObject2	Object
    //   37	138	5	localObject3	Object
    //   194	50	5	localException1	Exception
    //   317	269	5	localStringBuilder	StringBuilder
    //   61	68	6	arrayOfSignature2	Signature[]
    //   77	172	7	localObject4	Object
    //   307	50	7	localException2	Exception
    //   364	118	7	localSignatureNotFoundException2	SignatureNotFoundException
    //   554	42	7	localPackageParserException	android.content.pm.PackageParser.PackageParserException
    //   80	81	8	i	int
    // Exception table:
    //   from	to	target	type
    //   13	33	190	finally
    //   39	79	190	finally
    //   90	128	190	finally
    //   135	160	190	finally
    //   166	181	190	finally
    //   196	251	190	finally
    //   551	598	190	finally
    //   13	33	194	java/lang/Exception
    //   39	79	194	java/lang/Exception
    //   90	128	194	java/lang/Exception
    //   135	160	194	java/lang/Exception
    //   166	181	194	java/lang/Exception
    //   13	33	251	android/util/apk/SignatureNotFoundException
    //   39	79	251	android/util/apk/SignatureNotFoundException
    //   90	128	251	android/util/apk/SignatureNotFoundException
    //   135	160	251	android/util/apk/SignatureNotFoundException
    //   166	181	251	android/util/apk/SignatureNotFoundException
    //   277	294	303	finally
    //   309	364	303	finally
    //   442	489	303	finally
    //   277	294	307	java/lang/Exception
    //   277	294	364	android/util/apk/SignatureNotFoundException
  }
  
  private static void readFullyIgnoringContents(InputStream paramInputStream)
    throws IOException
  {
    byte[] arrayOfByte1 = (byte[])sBuffer.getAndSet(null);
    byte[] arrayOfByte2 = arrayOfByte1;
    if (arrayOfByte1 == null) {
      arrayOfByte2 = new byte['က'];
    }
    int i = 0;
    for (;;)
    {
      int j = paramInputStream.read(arrayOfByte2, 0, arrayOfByte2.length);
      if (j == -1) {
        break;
      }
      i += j;
    }
    sBuffer.set(arrayOfByte2);
  }
  
  /* Error */
  public static android.content.pm.PackageParser.SigningDetails verify(String paramString, @android.content.pm.PackageParser.SigningDetails.SignatureSchemeVersion int paramInt)
    throws android.content.pm.PackageParser.PackageParserException
  {
    // Byte code:
    //   0: iload_1
    //   1: iconst_3
    //   2: if_icmpgt +572 -> 574
    //   5: ldc2_w 155
    //   8: ldc_w 273
    //   11: invokestatic 164	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   14: aload_0
    //   15: invokestatic 275	android/util/apk/ApkSignatureSchemeV3Verifier:verify	(Ljava/lang/String;)Landroid/util/apk/ApkSignatureSchemeV3Verifier$VerifiedSigner;
    //   18: astore_2
    //   19: aload_2
    //   20: getfield 175	android/util/apk/ApkSignatureSchemeV3Verifier$VerifiedSigner:certs	[Ljava/security/cert/X509Certificate;
    //   23: astore_3
    //   24: iconst_0
    //   25: istore 4
    //   27: iconst_1
    //   28: anewarray 169	[Ljava/security/cert/Certificate;
    //   31: dup
    //   32: iconst_0
    //   33: aload_3
    //   34: aastore
    //   35: invokestatic 177	android/util/apk/ApkSignatureVerifier:convertToSignatures	([[Ljava/security/cert/Certificate;)[Landroid/content/pm/Signature;
    //   38: astore 5
    //   40: aconst_null
    //   41: astore_3
    //   42: aconst_null
    //   43: astore 6
    //   45: aload_2
    //   46: getfield 181	android/util/apk/ApkSignatureSchemeV3Verifier$VerifiedSigner:por	Landroid/util/apk/ApkSignatureSchemeV3Verifier$VerifiedProofOfRotation;
    //   49: ifnull +116 -> 165
    //   52: aload_2
    //   53: getfield 181	android/util/apk/ApkSignatureSchemeV3Verifier$VerifiedSigner:por	Landroid/util/apk/ApkSignatureSchemeV3Verifier$VerifiedProofOfRotation;
    //   56: getfield 186	android/util/apk/ApkSignatureSchemeV3Verifier$VerifiedProofOfRotation:certs	Ljava/util/List;
    //   59: invokeinterface 191 1 0
    //   64: anewarray 76	android/content/pm/Signature
    //   67: astore 7
    //   69: aload_2
    //   70: getfield 181	android/util/apk/ApkSignatureSchemeV3Verifier$VerifiedSigner:por	Landroid/util/apk/ApkSignatureSchemeV3Verifier$VerifiedProofOfRotation;
    //   73: getfield 194	android/util/apk/ApkSignatureSchemeV3Verifier$VerifiedProofOfRotation:flagsList	Ljava/util/List;
    //   76: invokeinterface 191 1 0
    //   81: newarray int
    //   83: astore 8
    //   85: aload 7
    //   87: astore_3
    //   88: aload 8
    //   90: astore 6
    //   92: iload 4
    //   94: aload 7
    //   96: arraylength
    //   97: if_icmpge +68 -> 165
    //   100: new 76	android/content/pm/Signature
    //   103: astore_3
    //   104: aload_3
    //   105: aload_2
    //   106: getfield 181	android/util/apk/ApkSignatureSchemeV3Verifier$VerifiedSigner:por	Landroid/util/apk/ApkSignatureSchemeV3Verifier$VerifiedProofOfRotation;
    //   109: getfield 186	android/util/apk/ApkSignatureSchemeV3Verifier$VerifiedProofOfRotation:certs	Ljava/util/List;
    //   112: iload 4
    //   114: invokeinterface 198 2 0
    //   119: checkcast 200	java/security/cert/X509Certificate
    //   122: invokevirtual 204	java/security/cert/X509Certificate:getEncoded	()[B
    //   125: invokespecial 207	android/content/pm/Signature:<init>	([B)V
    //   128: aload 7
    //   130: iload 4
    //   132: aload_3
    //   133: aastore
    //   134: aload 8
    //   136: iload 4
    //   138: aload_2
    //   139: getfield 181	android/util/apk/ApkSignatureSchemeV3Verifier$VerifiedSigner:por	Landroid/util/apk/ApkSignatureSchemeV3Verifier$VerifiedProofOfRotation;
    //   142: getfield 194	android/util/apk/ApkSignatureSchemeV3Verifier$VerifiedProofOfRotation:flagsList	Ljava/util/List;
    //   145: iload 4
    //   147: invokeinterface 198 2 0
    //   152: checkcast 209	java/lang/Integer
    //   155: invokevirtual 212	java/lang/Integer:intValue	()I
    //   158: iastore
    //   159: iinc 4 1
    //   162: goto -77 -> 85
    //   165: new 214	android/content/pm/PackageParser$SigningDetails
    //   168: dup
    //   169: aload 5
    //   171: iconst_3
    //   172: aload_3
    //   173: aload 6
    //   175: invokespecial 217	android/content/pm/PackageParser$SigningDetails:<init>	([Landroid/content/pm/Signature;I[Landroid/content/pm/Signature;[I)V
    //   178: astore_3
    //   179: ldc2_w 155
    //   182: invokestatic 221	android/os/Trace:traceEnd	(J)V
    //   185: aload_3
    //   186: areturn
    //   187: astore_0
    //   188: goto +378 -> 566
    //   191: astore 7
    //   193: new 57	android/content/pm/PackageParser$PackageParserException
    //   196: astore 6
    //   198: new 128	java/lang/StringBuilder
    //   201: astore_3
    //   202: aload_3
    //   203: invokespecial 129	java/lang/StringBuilder:<init>	()V
    //   206: aload_3
    //   207: ldc -33
    //   209: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   212: pop
    //   213: aload_3
    //   214: aload_0
    //   215: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   218: pop
    //   219: aload_3
    //   220: ldc -31
    //   222: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   225: pop
    //   226: aload 6
    //   228: bipush -103
    //   230: aload_3
    //   231: invokevirtual 149	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   234: aload 7
    //   236: invokespecial 152	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;Ljava/lang/Throwable;)V
    //   239: aload 6
    //   241: athrow
    //   242: astore_3
    //   243: iload_1
    //   244: iconst_3
    //   245: if_icmpge +275 -> 520
    //   248: ldc2_w 155
    //   251: invokestatic 221	android/os/Trace:traceEnd	(J)V
    //   254: iload_1
    //   255: iconst_2
    //   256: if_icmpgt +216 -> 472
    //   259: ldc2_w 155
    //   262: ldc_w 277
    //   265: invokestatic 164	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   268: new 214	android/content/pm/PackageParser$SigningDetails
    //   271: dup
    //   272: aload_0
    //   273: invokestatic 279	android/util/apk/ApkSignatureSchemeV2Verifier:verify	(Ljava/lang/String;)[[Ljava/security/cert/X509Certificate;
    //   276: invokestatic 177	android/util/apk/ApkSignatureVerifier:convertToSignatures	([[Ljava/security/cert/Certificate;)[Landroid/content/pm/Signature;
    //   279: iconst_2
    //   280: invokespecial 233	android/content/pm/PackageParser$SigningDetails:<init>	([Landroid/content/pm/Signature;I)V
    //   283: astore_3
    //   284: ldc2_w 155
    //   287: invokestatic 221	android/os/Trace:traceEnd	(J)V
    //   290: aload_3
    //   291: areturn
    //   292: astore_0
    //   293: goto +171 -> 464
    //   296: astore 7
    //   298: new 57	android/content/pm/PackageParser$PackageParserException
    //   301: astore 6
    //   303: new 128	java/lang/StringBuilder
    //   306: astore_3
    //   307: aload_3
    //   308: invokespecial 129	java/lang/StringBuilder:<init>	()V
    //   311: aload_3
    //   312: ldc -33
    //   314: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   317: pop
    //   318: aload_3
    //   319: aload_0
    //   320: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   323: pop
    //   324: aload_3
    //   325: ldc -21
    //   327: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   330: pop
    //   331: aload 6
    //   333: bipush -103
    //   335: aload_3
    //   336: invokevirtual 149	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   339: aload 7
    //   341: invokespecial 152	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;Ljava/lang/Throwable;)V
    //   344: aload 6
    //   346: athrow
    //   347: astore_3
    //   348: iload_1
    //   349: iconst_2
    //   350: if_icmpge +68 -> 418
    //   353: ldc2_w 155
    //   356: invokestatic 221	android/os/Trace:traceEnd	(J)V
    //   359: iload_1
    //   360: iconst_1
    //   361: if_icmpgt +9 -> 370
    //   364: aload_0
    //   365: iconst_1
    //   366: invokestatic 239	android/util/apk/ApkSignatureVerifier:verifyV1Signature	(Ljava/lang/String;Z)Landroid/content/pm/PackageParser$SigningDetails;
    //   369: areturn
    //   370: new 128	java/lang/StringBuilder
    //   373: dup
    //   374: invokespecial 129	java/lang/StringBuilder:<init>	()V
    //   377: astore_3
    //   378: aload_3
    //   379: ldc -15
    //   381: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   384: pop
    //   385: aload_3
    //   386: iload_1
    //   387: invokevirtual 244	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   390: pop
    //   391: aload_3
    //   392: ldc -10
    //   394: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   397: pop
    //   398: aload_3
    //   399: aload_0
    //   400: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   403: pop
    //   404: new 57	android/content/pm/PackageParser$PackageParserException
    //   407: dup
    //   408: bipush -103
    //   410: aload_3
    //   411: invokevirtual 149	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   414: invokespecial 249	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;)V
    //   417: athrow
    //   418: new 57	android/content/pm/PackageParser$PackageParserException
    //   421: astore 6
    //   423: new 128	java/lang/StringBuilder
    //   426: astore 7
    //   428: aload 7
    //   430: invokespecial 129	java/lang/StringBuilder:<init>	()V
    //   433: aload 7
    //   435: ldc -5
    //   437: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   440: pop
    //   441: aload 7
    //   443: aload_0
    //   444: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   447: pop
    //   448: aload 6
    //   450: bipush -103
    //   452: aload 7
    //   454: invokevirtual 149	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   457: aload_3
    //   458: invokespecial 152	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;Ljava/lang/Throwable;)V
    //   461: aload 6
    //   463: athrow
    //   464: ldc2_w 155
    //   467: invokestatic 221	android/os/Trace:traceEnd	(J)V
    //   470: aload_0
    //   471: athrow
    //   472: new 128	java/lang/StringBuilder
    //   475: dup
    //   476: invokespecial 129	java/lang/StringBuilder:<init>	()V
    //   479: astore_3
    //   480: aload_3
    //   481: ldc -15
    //   483: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   486: pop
    //   487: aload_3
    //   488: iload_1
    //   489: invokevirtual 244	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   492: pop
    //   493: aload_3
    //   494: ldc -10
    //   496: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   499: pop
    //   500: aload_3
    //   501: aload_0
    //   502: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   505: pop
    //   506: new 57	android/content/pm/PackageParser$PackageParserException
    //   509: dup
    //   510: bipush -103
    //   512: aload_3
    //   513: invokevirtual 149	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   516: invokespecial 249	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;)V
    //   519: athrow
    //   520: new 57	android/content/pm/PackageParser$PackageParserException
    //   523: astore 6
    //   525: new 128	java/lang/StringBuilder
    //   528: astore 7
    //   530: aload 7
    //   532: invokespecial 129	java/lang/StringBuilder:<init>	()V
    //   535: aload 7
    //   537: ldc -3
    //   539: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   542: pop
    //   543: aload 7
    //   545: aload_0
    //   546: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   549: pop
    //   550: aload 6
    //   552: bipush -103
    //   554: aload 7
    //   556: invokevirtual 149	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   559: aload_3
    //   560: invokespecial 152	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;Ljava/lang/Throwable;)V
    //   563: aload 6
    //   565: athrow
    //   566: ldc2_w 155
    //   569: invokestatic 221	android/os/Trace:traceEnd	(J)V
    //   572: aload_0
    //   573: athrow
    //   574: new 128	java/lang/StringBuilder
    //   577: dup
    //   578: invokespecial 129	java/lang/StringBuilder:<init>	()V
    //   581: astore_3
    //   582: aload_3
    //   583: ldc -15
    //   585: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   588: pop
    //   589: aload_3
    //   590: iload_1
    //   591: invokevirtual 244	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   594: pop
    //   595: aload_3
    //   596: ldc -10
    //   598: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   601: pop
    //   602: aload_3
    //   603: aload_0
    //   604: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   607: pop
    //   608: new 57	android/content/pm/PackageParser$PackageParserException
    //   611: dup
    //   612: bipush -103
    //   614: aload_3
    //   615: invokevirtual 149	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   618: invokespecial 249	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;)V
    //   621: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	622	0	paramString	String
    //   0	622	1	paramInt	int
    //   18	121	2	localVerifiedSigner	ApkSignatureSchemeV3Verifier.VerifiedSigner
    //   23	208	3	localObject1	Object
    //   242	1	3	localSignatureNotFoundException1	SignatureNotFoundException
    //   283	53	3	localObject2	Object
    //   347	1	3	localSignatureNotFoundException2	SignatureNotFoundException
    //   377	238	3	localStringBuilder1	StringBuilder
    //   25	135	4	i	int
    //   38	132	5	arrayOfSignature1	Signature[]
    //   43	521	6	localObject3	Object
    //   67	62	7	arrayOfSignature2	Signature[]
    //   191	44	7	localException1	Exception
    //   296	44	7	localException2	Exception
    //   426	129	7	localStringBuilder2	StringBuilder
    //   83	52	8	arrayOfInt	int[]
    // Exception table:
    //   from	to	target	type
    //   14	24	187	finally
    //   27	40	187	finally
    //   45	85	187	finally
    //   92	128	187	finally
    //   134	159	187	finally
    //   165	179	187	finally
    //   193	242	187	finally
    //   520	566	187	finally
    //   14	24	191	java/lang/Exception
    //   27	40	191	java/lang/Exception
    //   45	85	191	java/lang/Exception
    //   92	128	191	java/lang/Exception
    //   134	159	191	java/lang/Exception
    //   165	179	191	java/lang/Exception
    //   14	24	242	android/util/apk/SignatureNotFoundException
    //   27	40	242	android/util/apk/SignatureNotFoundException
    //   45	85	242	android/util/apk/SignatureNotFoundException
    //   92	128	242	android/util/apk/SignatureNotFoundException
    //   134	159	242	android/util/apk/SignatureNotFoundException
    //   165	179	242	android/util/apk/SignatureNotFoundException
    //   268	284	292	finally
    //   298	347	292	finally
    //   418	464	292	finally
    //   268	284	296	java/lang/Exception
    //   268	284	347	android/util/apk/SignatureNotFoundException
  }
  
  /* Error */
  private static android.content.pm.PackageParser.SigningDetails verifyV1Signature(String paramString, boolean paramBoolean)
    throws android.content.pm.PackageParser.PackageParserException
  {
    // Byte code:
    //   0: iload_1
    //   1: ifeq +10 -> 11
    //   4: getstatic 46	android/util/apk/ApkSignatureVerifier:NUMBER_OF_CORES	I
    //   7: istore_2
    //   8: goto +5 -> 13
    //   11: iconst_1
    //   12: istore_2
    //   13: iload_2
    //   14: anewarray 67	android/util/jar/StrictJarFile
    //   17: astore_3
    //   18: new 286	android/util/ArrayMap
    //   21: dup
    //   22: invokespecial 287	android/util/ArrayMap:<init>	()V
    //   25: astore 4
    //   27: ldc2_w 155
    //   30: ldc_w 289
    //   33: invokestatic 164	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   36: getstatic 48	android/util/apk/ApkSignatureVerifier:sPerfBoost	Landroid/util/BoostFramework;
    //   39: ifnonnull +18 -> 57
    //   42: new 291	android/util/BoostFramework
    //   45: astore 5
    //   47: aload 5
    //   49: invokespecial 292	android/util/BoostFramework:<init>	()V
    //   52: aload 5
    //   54: putstatic 48	android/util/apk/ApkSignatureVerifier:sPerfBoost	Landroid/util/BoostFramework;
    //   57: getstatic 48	android/util/apk/ApkSignatureVerifier:sPerfBoost	Landroid/util/BoostFramework;
    //   60: ifnull +41 -> 101
    //   63: getstatic 50	android/util/apk/ApkSignatureVerifier:sIsPerfLockAcquired	Z
    //   66: ifne +35 -> 101
    //   69: iload_1
    //   70: ifeq +31 -> 101
    //   73: getstatic 48	android/util/apk/ApkSignatureVerifier:sPerfBoost	Landroid/util/BoostFramework;
    //   76: sipush 4232
    //   79: aconst_null
    //   80: ldc_w 293
    //   83: iconst_m1
    //   84: invokevirtual 297	android/util/BoostFramework:perfHint	(ILjava/lang/String;II)I
    //   87: pop
    //   88: ldc 18
    //   90: ldc_w 299
    //   93: invokestatic 305	android/util/Slog:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   96: pop
    //   97: iconst_1
    //   98: putstatic 50	android/util/apk/ApkSignatureVerifier:sIsPerfLockAcquired	Z
    //   101: iconst_0
    //   102: istore 6
    //   104: iload 6
    //   106: iload_2
    //   107: if_icmpge +23 -> 130
    //   110: aload_3
    //   111: iload 6
    //   113: new 67	android/util/jar/StrictJarFile
    //   116: dup
    //   117: aload_0
    //   118: iconst_1
    //   119: iload_1
    //   120: invokespecial 308	android/util/jar/StrictJarFile:<init>	(Ljava/lang/String;ZZ)V
    //   123: aastore
    //   124: iinc 6 1
    //   127: goto -23 -> 104
    //   130: new 310	java/util/ArrayList
    //   133: astore 7
    //   135: aload 7
    //   137: invokespecial 311	java/util/ArrayList:<init>	()V
    //   140: aload_3
    //   141: iconst_0
    //   142: aaload
    //   143: ldc_w 313
    //   146: invokevirtual 317	android/util/jar/StrictJarFile:findEntry	(Ljava/lang/String;)Ljava/util/zip/ZipEntry;
    //   149: astore 8
    //   151: aload 8
    //   153: ifnull +701 -> 854
    //   156: aload_3
    //   157: iconst_0
    //   158: aaload
    //   159: aload 8
    //   161: invokestatic 60	android/util/apk/ApkSignatureVerifier:loadCertificates	(Landroid/util/jar/StrictJarFile;Ljava/util/zip/ZipEntry;)[[Ljava/security/cert/Certificate;
    //   164: astore 9
    //   166: aload 9
    //   168: invokestatic 323	com/android/internal/util/ArrayUtils:isEmpty	([Ljava/lang/Object;)Z
    //   171: ifne +578 -> 749
    //   174: aload 9
    //   176: invokestatic 177	android/util/apk/ApkSignatureVerifier:convertToSignatures	([[Ljava/security/cert/Certificate;)[Landroid/content/pm/Signature;
    //   179: astore 10
    //   181: iload_1
    //   182: ifeq +454 -> 636
    //   185: aload_3
    //   186: iconst_0
    //   187: aaload
    //   188: invokevirtual 327	android/util/jar/StrictJarFile:iterator	()Ljava/util/Iterator;
    //   191: astore 5
    //   193: aload 5
    //   195: invokeinterface 333 1 0
    //   200: ifeq +74 -> 274
    //   203: aload 5
    //   205: invokeinterface 337 1 0
    //   210: checkcast 137	java/util/zip/ZipEntry
    //   213: astore 11
    //   215: aload 11
    //   217: invokevirtual 340	java/util/zip/ZipEntry:isDirectory	()Z
    //   220: ifeq +6 -> 226
    //   223: goto +48 -> 271
    //   226: aload 11
    //   228: invokevirtual 141	java/util/zip/ZipEntry:getName	()Ljava/lang/String;
    //   231: astore 12
    //   233: aload 12
    //   235: ldc_w 342
    //   238: invokevirtual 348	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   241: ifeq +6 -> 247
    //   244: goto +27 -> 271
    //   247: aload 12
    //   249: ldc_w 313
    //   252: invokevirtual 352	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   255: ifeq +6 -> 261
    //   258: goto +13 -> 271
    //   261: aload 7
    //   263: aload 11
    //   265: invokeinterface 355 2 0
    //   270: pop
    //   271: goto -78 -> 193
    //   274: new 8	android/util/apk/ApkSignatureVerifier$1VerificationData
    //   277: astore 11
    //   279: aload 11
    //   281: invokespecial 356	android/util/apk/ApkSignatureVerifier$1VerificationData:<init>	()V
    //   284: new 4	java/lang/Object
    //   287: astore 12
    //   289: aload 12
    //   291: invokespecial 52	java/lang/Object:<init>	()V
    //   294: aload 11
    //   296: aload 12
    //   298: putfield 360	android/util/apk/ApkSignatureVerifier$1VerificationData:objWaitAll	Ljava/lang/Object;
    //   301: new 362	java/util/concurrent/ThreadPoolExecutor
    //   304: astore 12
    //   306: getstatic 46	android/util/apk/ApkSignatureVerifier:NUMBER_OF_CORES	I
    //   309: istore 6
    //   311: getstatic 46	android/util/apk/ApkSignatureVerifier:NUMBER_OF_CORES	I
    //   314: istore 13
    //   316: getstatic 368	java/util/concurrent/TimeUnit:SECONDS	Ljava/util/concurrent/TimeUnit;
    //   319: astore 14
    //   321: new 370	java/util/concurrent/LinkedBlockingQueue
    //   324: astore 15
    //   326: aload 15
    //   328: invokespecial 371	java/util/concurrent/LinkedBlockingQueue:<init>	()V
    //   331: aload 12
    //   333: iload 6
    //   335: iload 13
    //   337: lconst_1
    //   338: aload 14
    //   340: aload 15
    //   342: invokespecial 374	java/util/concurrent/ThreadPoolExecutor:<init>	(IIJLjava/util/concurrent/TimeUnit;Ljava/util/concurrent/BlockingQueue;)V
    //   345: aload 7
    //   347: invokeinterface 375 1 0
    //   352: astore 15
    //   354: aload 5
    //   356: astore 14
    //   358: aload 10
    //   360: astore 5
    //   362: aload 15
    //   364: invokeinterface 333 1 0
    //   369: ifeq +74 -> 443
    //   372: aload 15
    //   374: invokeinterface 337 1 0
    //   379: checkcast 137	java/util/zip/ZipEntry
    //   382: astore 16
    //   384: new 6	android/util/apk/ApkSignatureVerifier$1
    //   387: astore 10
    //   389: aload 10
    //   391: aload 11
    //   393: aload 4
    //   395: aload_3
    //   396: aload 16
    //   398: aload_0
    //   399: aload 5
    //   401: invokespecial 378	android/util/apk/ApkSignatureVerifier$1:<init>	(Landroid/util/apk/ApkSignatureVerifier$1VerificationData;Landroid/util/ArrayMap;[Landroid/util/jar/StrictJarFile;Ljava/util/zip/ZipEntry;Ljava/lang/String;[Landroid/content/pm/Signature;)V
    //   404: aload 11
    //   406: getfield 360	android/util/apk/ApkSignatureVerifier$1VerificationData:objWaitAll	Ljava/lang/Object;
    //   409: astore 16
    //   411: aload 16
    //   413: monitorenter
    //   414: aload 11
    //   416: getfield 381	android/util/apk/ApkSignatureVerifier$1VerificationData:exceptionFlag	I
    //   419: ifne +10 -> 429
    //   422: aload 12
    //   424: aload 10
    //   426: invokevirtual 385	java/util/concurrent/ThreadPoolExecutor:execute	(Ljava/lang/Runnable;)V
    //   429: aload 16
    //   431: monitorexit
    //   432: goto -70 -> 362
    //   435: astore 5
    //   437: aload 16
    //   439: monitorexit
    //   440: aload 5
    //   442: athrow
    //   443: aload 11
    //   445: iconst_1
    //   446: putfield 388	android/util/apk/ApkSignatureVerifier$1VerificationData:wait	Z
    //   449: aload 12
    //   451: invokevirtual 391	java/util/concurrent/ThreadPoolExecutor:shutdown	()V
    //   454: aload 11
    //   456: getfield 388	android/util/apk/ApkSignatureVerifier$1VerificationData:wait	Z
    //   459: istore_1
    //   460: iload_1
    //   461: ifeq +107 -> 568
    //   464: aload 11
    //   466: getfield 381	android/util/apk/ApkSignatureVerifier$1VerificationData:exceptionFlag	I
    //   469: ifeq +64 -> 533
    //   472: aload 11
    //   474: getfield 394	android/util/apk/ApkSignatureVerifier$1VerificationData:shutDown	Z
    //   477: ifne +56 -> 533
    //   480: new 128	java/lang/StringBuilder
    //   483: astore 10
    //   485: aload 10
    //   487: invokespecial 129	java/lang/StringBuilder:<init>	()V
    //   490: aload 10
    //   492: ldc_w 396
    //   495: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   498: pop
    //   499: aload 10
    //   501: aload 11
    //   503: getfield 381	android/util/apk/ApkSignatureVerifier$1VerificationData:exceptionFlag	I
    //   506: invokevirtual 244	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   509: pop
    //   510: ldc 18
    //   512: aload 10
    //   514: invokevirtual 149	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   517: invokestatic 399	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   520: pop
    //   521: aload 12
    //   523: invokevirtual 403	java/util/concurrent/ThreadPoolExecutor:shutdownNow	()Ljava/util/List;
    //   526: pop
    //   527: aload 11
    //   529: iconst_1
    //   530: putfield 394	android/util/apk/ApkSignatureVerifier$1VerificationData:shutDown	Z
    //   533: aload 11
    //   535: aload 12
    //   537: ldc2_w 404
    //   540: getstatic 408	java/util/concurrent/TimeUnit:MILLISECONDS	Ljava/util/concurrent/TimeUnit;
    //   543: invokevirtual 412	java/util/concurrent/ThreadPoolExecutor:awaitTermination	(JLjava/util/concurrent/TimeUnit;)Z
    //   546: iconst_1
    //   547: ixor
    //   548: putfield 388	android/util/apk/ApkSignatureVerifier$1VerificationData:wait	Z
    //   551: goto +14 -> 565
    //   554: astore 10
    //   556: ldc 18
    //   558: ldc_w 414
    //   561: invokestatic 399	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   564: pop
    //   565: goto -111 -> 454
    //   568: aload 11
    //   570: getfield 381	android/util/apk/ApkSignatureVerifier$1VerificationData:exceptionFlag	I
    //   573: ifne +6 -> 579
    //   576: goto +64 -> 640
    //   579: new 57	android/content/pm/PackageParser$PackageParserException
    //   582: astore 10
    //   584: aload 11
    //   586: getfield 381	android/util/apk/ApkSignatureVerifier$1VerificationData:exceptionFlag	I
    //   589: istore 6
    //   591: new 128	java/lang/StringBuilder
    //   594: astore 5
    //   596: aload 5
    //   598: invokespecial 129	java/lang/StringBuilder:<init>	()V
    //   601: aload 5
    //   603: ldc -33
    //   605: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   608: pop
    //   609: aload 5
    //   611: aload_0
    //   612: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   615: pop
    //   616: aload 10
    //   618: iload 6
    //   620: aload 5
    //   622: invokevirtual 149	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   625: aload 11
    //   627: getfield 418	android/util/apk/ApkSignatureVerifier$1VerificationData:exception	Ljava/lang/Exception;
    //   630: invokespecial 152	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;Ljava/lang/Throwable;)V
    //   633: aload 10
    //   635: athrow
    //   636: aload 10
    //   638: astore 5
    //   640: new 214	android/content/pm/PackageParser$SigningDetails
    //   643: dup
    //   644: aload 5
    //   646: iconst_1
    //   647: invokespecial 233	android/content/pm/PackageParser$SigningDetails:<init>	([Landroid/content/pm/Signature;I)V
    //   650: astore 5
    //   652: getstatic 50	android/util/apk/ApkSignatureVerifier:sIsPerfLockAcquired	Z
    //   655: ifeq +29 -> 684
    //   658: getstatic 48	android/util/apk/ApkSignatureVerifier:sPerfBoost	Landroid/util/BoostFramework;
    //   661: ifnull +23 -> 684
    //   664: getstatic 48	android/util/apk/ApkSignatureVerifier:sPerfBoost	Landroid/util/BoostFramework;
    //   667: invokevirtual 421	android/util/BoostFramework:perfLockRelease	()I
    //   670: pop
    //   671: iconst_0
    //   672: putstatic 50	android/util/apk/ApkSignatureVerifier:sIsPerfLockAcquired	Z
    //   675: ldc 18
    //   677: ldc_w 423
    //   680: invokestatic 305	android/util/Slog:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   683: pop
    //   684: aload 4
    //   686: invokevirtual 426	android/util/ArrayMap:clear	()V
    //   689: ldc2_w 155
    //   692: invokestatic 221	android/os/Trace:traceEnd	(J)V
    //   695: iconst_0
    //   696: istore 6
    //   698: iload 6
    //   700: iload_2
    //   701: if_icmpge +16 -> 717
    //   704: aload_3
    //   705: iload 6
    //   707: aaload
    //   708: invokestatic 428	android/util/apk/ApkSignatureVerifier:closeQuietly	(Landroid/util/jar/StrictJarFile;)V
    //   711: iinc 6 1
    //   714: goto -16 -> 698
    //   717: aload 5
    //   719: areturn
    //   720: astore_0
    //   721: ldc2_w 155
    //   724: lstore 17
    //   726: goto +414 -> 1140
    //   729: astore 5
    //   731: ldc2_w 155
    //   734: lstore 19
    //   736: goto +246 -> 982
    //   739: astore 5
    //   741: ldc2_w 155
    //   744: lstore 19
    //   746: goto +318 -> 1064
    //   749: ldc2_w 155
    //   752: lstore 19
    //   754: lload 19
    //   756: lstore 17
    //   758: new 57	android/content/pm/PackageParser$PackageParserException
    //   761: astore 5
    //   763: lload 19
    //   765: lstore 17
    //   767: new 128	java/lang/StringBuilder
    //   770: astore 10
    //   772: lload 19
    //   774: lstore 17
    //   776: aload 10
    //   778: invokespecial 129	java/lang/StringBuilder:<init>	()V
    //   781: lload 19
    //   783: lstore 17
    //   785: aload 10
    //   787: ldc_w 430
    //   790: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   793: pop
    //   794: lload 19
    //   796: lstore 17
    //   798: aload 10
    //   800: aload_0
    //   801: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   804: pop
    //   805: lload 19
    //   807: lstore 17
    //   809: aload 10
    //   811: ldc_w 432
    //   814: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   817: pop
    //   818: lload 19
    //   820: lstore 17
    //   822: aload 10
    //   824: ldc_w 313
    //   827: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   830: pop
    //   831: lload 19
    //   833: lstore 17
    //   835: aload 5
    //   837: bipush -103
    //   839: aload 10
    //   841: invokevirtual 149	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   844: invokespecial 249	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;)V
    //   847: lload 19
    //   849: lstore 17
    //   851: aload 5
    //   853: athrow
    //   854: ldc2_w 155
    //   857: lstore 19
    //   859: lload 19
    //   861: lstore 17
    //   863: new 57	android/content/pm/PackageParser$PackageParserException
    //   866: astore 10
    //   868: lload 19
    //   870: lstore 17
    //   872: new 128	java/lang/StringBuilder
    //   875: astore 5
    //   877: lload 19
    //   879: lstore 17
    //   881: aload 5
    //   883: invokespecial 129	java/lang/StringBuilder:<init>	()V
    //   886: lload 19
    //   888: lstore 17
    //   890: aload 5
    //   892: ldc_w 430
    //   895: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   898: pop
    //   899: lload 19
    //   901: lstore 17
    //   903: aload 5
    //   905: aload_0
    //   906: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   909: pop
    //   910: lload 19
    //   912: lstore 17
    //   914: aload 5
    //   916: ldc_w 434
    //   919: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   922: pop
    //   923: lload 19
    //   925: lstore 17
    //   927: aload 10
    //   929: bipush -101
    //   931: aload 5
    //   933: invokevirtual 149	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   936: invokespecial 249	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;)V
    //   939: lload 19
    //   941: lstore 17
    //   943: aload 10
    //   945: athrow
    //   946: ldc2_w 155
    //   949: lstore 19
    //   951: astore 5
    //   953: goto +29 -> 982
    //   956: ldc2_w 155
    //   959: lstore 19
    //   961: astore 5
    //   963: goto +101 -> 1064
    //   966: astore_0
    //   967: ldc2_w 155
    //   970: lstore 17
    //   972: goto +168 -> 1140
    //   975: astore 5
    //   977: ldc2_w 155
    //   980: lstore 19
    //   982: lload 19
    //   984: lstore 17
    //   986: new 57	android/content/pm/PackageParser$PackageParserException
    //   989: astore 11
    //   991: lload 19
    //   993: lstore 17
    //   995: new 128	java/lang/StringBuilder
    //   998: astore 10
    //   1000: lload 19
    //   1002: lstore 17
    //   1004: aload 10
    //   1006: invokespecial 129	java/lang/StringBuilder:<init>	()V
    //   1009: lload 19
    //   1011: lstore 17
    //   1013: aload 10
    //   1015: ldc -33
    //   1017: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1020: pop
    //   1021: lload 19
    //   1023: lstore 17
    //   1025: aload 10
    //   1027: aload_0
    //   1028: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1031: pop
    //   1032: lload 19
    //   1034: lstore 17
    //   1036: aload 11
    //   1038: bipush -103
    //   1040: aload 10
    //   1042: invokevirtual 149	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1045: aload 5
    //   1047: invokespecial 152	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;Ljava/lang/Throwable;)V
    //   1050: lload 19
    //   1052: lstore 17
    //   1054: aload 11
    //   1056: athrow
    //   1057: astore 5
    //   1059: ldc2_w 155
    //   1062: lstore 19
    //   1064: lload 19
    //   1066: lstore 17
    //   1068: new 57	android/content/pm/PackageParser$PackageParserException
    //   1071: astore 11
    //   1073: lload 19
    //   1075: lstore 17
    //   1077: new 128	java/lang/StringBuilder
    //   1080: astore 10
    //   1082: lload 19
    //   1084: lstore 17
    //   1086: aload 10
    //   1088: invokespecial 129	java/lang/StringBuilder:<init>	()V
    //   1091: lload 19
    //   1093: lstore 17
    //   1095: aload 10
    //   1097: ldc -33
    //   1099: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1102: pop
    //   1103: lload 19
    //   1105: lstore 17
    //   1107: aload 10
    //   1109: aload_0
    //   1110: invokevirtual 135	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1113: pop
    //   1114: lload 19
    //   1116: lstore 17
    //   1118: aload 11
    //   1120: bipush -105
    //   1122: aload 10
    //   1124: invokevirtual 149	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1127: aload 5
    //   1129: invokespecial 152	android/content/pm/PackageParser$PackageParserException:<init>	(ILjava/lang/String;Ljava/lang/Throwable;)V
    //   1132: lload 19
    //   1134: lstore 17
    //   1136: aload 11
    //   1138: athrow
    //   1139: astore_0
    //   1140: getstatic 50	android/util/apk/ApkSignatureVerifier:sIsPerfLockAcquired	Z
    //   1143: ifeq +32 -> 1175
    //   1146: getstatic 48	android/util/apk/ApkSignatureVerifier:sPerfBoost	Landroid/util/BoostFramework;
    //   1149: ifnull +26 -> 1175
    //   1152: getstatic 48	android/util/apk/ApkSignatureVerifier:sPerfBoost	Landroid/util/BoostFramework;
    //   1155: invokevirtual 421	android/util/BoostFramework:perfLockRelease	()I
    //   1158: pop
    //   1159: iconst_0
    //   1160: putstatic 50	android/util/apk/ApkSignatureVerifier:sIsPerfLockAcquired	Z
    //   1163: ldc 18
    //   1165: ldc_w 423
    //   1168: invokestatic 305	android/util/Slog:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   1171: pop
    //   1172: goto +3 -> 1175
    //   1175: iconst_0
    //   1176: istore 6
    //   1178: aload 4
    //   1180: invokevirtual 426	android/util/ArrayMap:clear	()V
    //   1183: lload 17
    //   1185: invokestatic 221	android/os/Trace:traceEnd	(J)V
    //   1188: iload 6
    //   1190: iload_2
    //   1191: if_icmpge +16 -> 1207
    //   1194: aload_3
    //   1195: iload 6
    //   1197: aaload
    //   1198: invokestatic 428	android/util/apk/ApkSignatureVerifier:closeQuietly	(Landroid/util/jar/StrictJarFile;)V
    //   1201: iinc 6 1
    //   1204: goto -16 -> 1188
    //   1207: aload_0
    //   1208: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	1209	0	paramString	String
    //   0	1209	1	paramBoolean	boolean
    //   7	1185	2	i	int
    //   17	1178	3	arrayOfStrictJarFile	StrictJarFile[]
    //   25	1154	4	localArrayMap	ArrayMap
    //   45	355	5	localObject1	Object
    //   435	6	5	localObject2	Object
    //   594	124	5	localObject3	Object
    //   729	1	5	localIOException1	IOException
    //   739	1	5	localGeneralSecurityException1	java.security.GeneralSecurityException
    //   761	171	5	localObject4	Object
    //   951	1	5	localIOException2	IOException
    //   961	1	5	localGeneralSecurityException2	java.security.GeneralSecurityException
    //   975	71	5	localIOException3	IOException
    //   1057	71	5	localGeneralSecurityException3	java.security.GeneralSecurityException
    //   102	1100	6	j	int
    //   133	213	7	localArrayList	java.util.ArrayList
    //   149	11	8	localZipEntry	ZipEntry
    //   164	11	9	arrayOfCertificate	Certificate[][]
    //   179	334	10	localObject5	Object
    //   554	1	10	localInterruptedException	InterruptedException
    //   582	541	10	localObject6	Object
    //   213	924	11	localObject7	Object
    //   231	305	12	localObject8	Object
    //   314	22	13	k	int
    //   319	38	14	localObject9	Object
    //   324	49	15	localObject10	Object
    //   382	56	16	localObject11	Object
    //   724	460	17	l1	long
    //   734	399	19	l2	long
    // Exception table:
    //   from	to	target	type
    //   414	429	435	finally
    //   429	432	435	finally
    //   437	440	435	finally
    //   464	533	554	java/lang/InterruptedException
    //   533	551	554	java/lang/InterruptedException
    //   233	244	720	finally
    //   247	258	720	finally
    //   261	271	720	finally
    //   274	284	720	finally
    //   284	345	720	finally
    //   345	354	720	finally
    //   362	414	720	finally
    //   440	443	720	finally
    //   443	454	720	finally
    //   454	460	720	finally
    //   464	533	720	finally
    //   533	551	720	finally
    //   556	565	720	finally
    //   568	576	720	finally
    //   579	636	720	finally
    //   640	652	720	finally
    //   233	244	729	java/io/IOException
    //   233	244	729	java/lang/RuntimeException
    //   247	258	729	java/io/IOException
    //   247	258	729	java/lang/RuntimeException
    //   261	271	729	java/io/IOException
    //   261	271	729	java/lang/RuntimeException
    //   274	284	729	java/io/IOException
    //   274	284	729	java/lang/RuntimeException
    //   284	345	729	java/io/IOException
    //   284	345	729	java/lang/RuntimeException
    //   345	354	729	java/io/IOException
    //   345	354	729	java/lang/RuntimeException
    //   362	414	729	java/io/IOException
    //   362	414	729	java/lang/RuntimeException
    //   440	443	729	java/io/IOException
    //   440	443	729	java/lang/RuntimeException
    //   443	454	729	java/io/IOException
    //   443	454	729	java/lang/RuntimeException
    //   454	460	729	java/io/IOException
    //   454	460	729	java/lang/RuntimeException
    //   464	533	729	java/io/IOException
    //   464	533	729	java/lang/RuntimeException
    //   533	551	729	java/io/IOException
    //   533	551	729	java/lang/RuntimeException
    //   556	565	729	java/io/IOException
    //   556	565	729	java/lang/RuntimeException
    //   568	576	729	java/io/IOException
    //   568	576	729	java/lang/RuntimeException
    //   579	636	729	java/io/IOException
    //   579	636	729	java/lang/RuntimeException
    //   640	652	729	java/io/IOException
    //   640	652	729	java/lang/RuntimeException
    //   233	244	739	java/security/GeneralSecurityException
    //   247	258	739	java/security/GeneralSecurityException
    //   261	271	739	java/security/GeneralSecurityException
    //   274	284	739	java/security/GeneralSecurityException
    //   284	345	739	java/security/GeneralSecurityException
    //   345	354	739	java/security/GeneralSecurityException
    //   362	414	739	java/security/GeneralSecurityException
    //   440	443	739	java/security/GeneralSecurityException
    //   443	454	739	java/security/GeneralSecurityException
    //   454	460	739	java/security/GeneralSecurityException
    //   464	533	739	java/security/GeneralSecurityException
    //   533	551	739	java/security/GeneralSecurityException
    //   556	565	739	java/security/GeneralSecurityException
    //   568	576	739	java/security/GeneralSecurityException
    //   579	636	739	java/security/GeneralSecurityException
    //   640	652	739	java/security/GeneralSecurityException
    //   758	763	946	java/io/IOException
    //   758	763	946	java/lang/RuntimeException
    //   767	772	946	java/io/IOException
    //   767	772	946	java/lang/RuntimeException
    //   776	781	946	java/io/IOException
    //   776	781	946	java/lang/RuntimeException
    //   785	794	946	java/io/IOException
    //   785	794	946	java/lang/RuntimeException
    //   798	805	946	java/io/IOException
    //   798	805	946	java/lang/RuntimeException
    //   809	818	946	java/io/IOException
    //   809	818	946	java/lang/RuntimeException
    //   822	831	946	java/io/IOException
    //   822	831	946	java/lang/RuntimeException
    //   835	847	946	java/io/IOException
    //   835	847	946	java/lang/RuntimeException
    //   851	854	946	java/io/IOException
    //   851	854	946	java/lang/RuntimeException
    //   863	868	946	java/io/IOException
    //   863	868	946	java/lang/RuntimeException
    //   872	877	946	java/io/IOException
    //   872	877	946	java/lang/RuntimeException
    //   881	886	946	java/io/IOException
    //   881	886	946	java/lang/RuntimeException
    //   890	899	946	java/io/IOException
    //   890	899	946	java/lang/RuntimeException
    //   903	910	946	java/io/IOException
    //   903	910	946	java/lang/RuntimeException
    //   914	923	946	java/io/IOException
    //   914	923	946	java/lang/RuntimeException
    //   927	939	946	java/io/IOException
    //   927	939	946	java/lang/RuntimeException
    //   943	946	946	java/io/IOException
    //   943	946	946	java/lang/RuntimeException
    //   758	763	956	java/security/GeneralSecurityException
    //   767	772	956	java/security/GeneralSecurityException
    //   776	781	956	java/security/GeneralSecurityException
    //   785	794	956	java/security/GeneralSecurityException
    //   798	805	956	java/security/GeneralSecurityException
    //   809	818	956	java/security/GeneralSecurityException
    //   822	831	956	java/security/GeneralSecurityException
    //   835	847	956	java/security/GeneralSecurityException
    //   851	854	956	java/security/GeneralSecurityException
    //   863	868	956	java/security/GeneralSecurityException
    //   872	877	956	java/security/GeneralSecurityException
    //   881	886	956	java/security/GeneralSecurityException
    //   890	899	956	java/security/GeneralSecurityException
    //   903	910	956	java/security/GeneralSecurityException
    //   914	923	956	java/security/GeneralSecurityException
    //   927	939	956	java/security/GeneralSecurityException
    //   943	946	956	java/security/GeneralSecurityException
    //   27	57	966	finally
    //   57	69	966	finally
    //   73	101	966	finally
    //   110	124	966	finally
    //   130	140	966	finally
    //   140	151	966	finally
    //   156	181	966	finally
    //   185	193	966	finally
    //   193	223	966	finally
    //   226	233	966	finally
    //   27	57	975	java/io/IOException
    //   27	57	975	java/lang/RuntimeException
    //   57	69	975	java/io/IOException
    //   57	69	975	java/lang/RuntimeException
    //   73	101	975	java/io/IOException
    //   73	101	975	java/lang/RuntimeException
    //   110	124	975	java/io/IOException
    //   110	124	975	java/lang/RuntimeException
    //   130	140	975	java/io/IOException
    //   130	140	975	java/lang/RuntimeException
    //   140	151	975	java/io/IOException
    //   140	151	975	java/lang/RuntimeException
    //   156	181	975	java/io/IOException
    //   156	181	975	java/lang/RuntimeException
    //   185	193	975	java/io/IOException
    //   185	193	975	java/lang/RuntimeException
    //   193	223	975	java/io/IOException
    //   193	223	975	java/lang/RuntimeException
    //   226	233	975	java/io/IOException
    //   226	233	975	java/lang/RuntimeException
    //   27	57	1057	java/security/GeneralSecurityException
    //   57	69	1057	java/security/GeneralSecurityException
    //   73	101	1057	java/security/GeneralSecurityException
    //   110	124	1057	java/security/GeneralSecurityException
    //   130	140	1057	java/security/GeneralSecurityException
    //   140	151	1057	java/security/GeneralSecurityException
    //   156	181	1057	java/security/GeneralSecurityException
    //   185	193	1057	java/security/GeneralSecurityException
    //   193	223	1057	java/security/GeneralSecurityException
    //   226	233	1057	java/security/GeneralSecurityException
    //   758	763	1139	finally
    //   767	772	1139	finally
    //   776	781	1139	finally
    //   785	794	1139	finally
    //   798	805	1139	finally
    //   809	818	1139	finally
    //   822	831	1139	finally
    //   835	847	1139	finally
    //   851	854	1139	finally
    //   863	868	1139	finally
    //   872	877	1139	finally
    //   881	886	1139	finally
    //   890	899	1139	finally
    //   903	910	1139	finally
    //   914	923	1139	finally
    //   927	939	1139	finally
    //   943	946	1139	finally
    //   986	991	1139	finally
    //   995	1000	1139	finally
    //   1004	1009	1139	finally
    //   1013	1021	1139	finally
    //   1025	1032	1139	finally
    //   1036	1050	1139	finally
    //   1054	1057	1139	finally
    //   1068	1073	1139	finally
    //   1077	1082	1139	finally
    //   1086	1091	1139	finally
    //   1095	1103	1139	finally
    //   1107	1114	1139	finally
    //   1118	1132	1139	finally
    //   1136	1139	1139	finally
  }
  
  public static class Result
  {
    public final Certificate[][] certs;
    public final int signatureSchemeVersion;
    public final Signature[] sigs;
    
    public Result(Certificate[][] paramArrayOfCertificate, Signature[] paramArrayOfSignature, int paramInt)
    {
      certs = paramArrayOfCertificate;
      sigs = paramArrayOfSignature;
      signatureSchemeVersion = paramInt;
    }
  }
}
