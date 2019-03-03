package android.util.jar;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;
import java.util.jar.Attributes;

class StrictJarVerifier
{
  private static final String[] DIGEST_ALGORITHMS = { "SHA-512", "SHA-384", "SHA-256", "SHA1" };
  private static final String SF_ATTRIBUTE_ANDROID_APK_SIGNED_NAME = "X-Android-APK-Signed";
  private final Hashtable<String, Certificate[]> certificates = new Hashtable(5);
  private final String jarName;
  private final int mainAttributesEnd;
  private final StrictJarManifest manifest;
  private final HashMap<String, byte[]> metaEntries;
  private final boolean signatureSchemeRollbackProtectionsEnforced;
  private final Hashtable<String, HashMap<String, Attributes>> signatures = new Hashtable(5);
  private final Hashtable<String, Certificate[][]> verifiedEntries = new Hashtable();
  
  StrictJarVerifier(String paramString, StrictJarManifest paramStrictJarManifest, HashMap<String, byte[]> paramHashMap, boolean paramBoolean)
  {
    jarName = paramString;
    manifest = paramStrictJarManifest;
    metaEntries = paramHashMap;
    mainAttributesEnd = paramStrictJarManifest.getMainAttributesEnd();
    signatureSchemeRollbackProtectionsEnforced = paramBoolean;
  }
  
  private static SecurityException failedVerification(String paramString1, String paramString2)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString1);
    localStringBuilder.append(" failed verification of ");
    localStringBuilder.append(paramString2);
    throw new SecurityException(localStringBuilder.toString());
  }
  
  private static SecurityException failedVerification(String paramString1, String paramString2, Throwable paramThrowable)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString1);
    localStringBuilder.append(" failed verification of ");
    localStringBuilder.append(paramString2);
    throw new SecurityException(localStringBuilder.toString(), paramThrowable);
  }
  
  private static SecurityException invalidDigest(String paramString1, String paramString2, String paramString3)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString1);
    localStringBuilder.append(" has invalid digest for ");
    localStringBuilder.append(paramString2);
    localStringBuilder.append(" in ");
    localStringBuilder.append(paramString3);
    throw new SecurityException(localStringBuilder.toString());
  }
  
  private boolean verify(Attributes paramAttributes, String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
  {
    for (int i = 0; i < DIGEST_ALGORITHMS.length; i++)
    {
      Object localObject1 = DIGEST_ALGORITHMS[i];
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append(paramString);
      localObject2 = paramAttributes.getValue(((StringBuilder)localObject2).toString());
      if (localObject2 != null) {
        try
        {
          localObject1 = MessageDigest.getInstance((String)localObject1);
          if ((paramBoolean1) && (paramArrayOfByte[(paramInt2 - 1)] == 10) && (paramArrayOfByte[(paramInt2 - 2)] == 10)) {
            ((MessageDigest)localObject1).update(paramArrayOfByte, paramInt1, paramInt2 - 1 - paramInt1);
          } else {
            ((MessageDigest)localObject1).update(paramArrayOfByte, paramInt1, paramInt2 - paramInt1);
          }
          return verifyMessageDigest(((MessageDigest)localObject1).digest(), ((String)localObject2).getBytes(StandardCharsets.ISO_8859_1));
        }
        catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {}
      }
    }
    return paramBoolean2;
  }
  
  /* Error */
  static Certificate[] verifyBytes(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws java.security.GeneralSecurityException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: invokestatic 163	sun/security/jca/Providers:startJarVerification	()Ljava/lang/Object;
    //   7: astore 4
    //   9: aload 4
    //   11: astore_3
    //   12: aload 4
    //   14: astore_2
    //   15: new 165	sun/security/pkcs/PKCS7
    //   18: astore 5
    //   20: aload 4
    //   22: astore_3
    //   23: aload 4
    //   25: astore_2
    //   26: aload 5
    //   28: aload_0
    //   29: invokespecial 168	sun/security/pkcs/PKCS7:<init>	([B)V
    //   32: aload 4
    //   34: astore_3
    //   35: aload 4
    //   37: astore_2
    //   38: aload 5
    //   40: aload_1
    //   41: invokevirtual 171	sun/security/pkcs/PKCS7:verify	([B)[Lsun/security/pkcs/SignerInfo;
    //   44: astore_0
    //   45: aload_0
    //   46: ifnull +142 -> 188
    //   49: aload 4
    //   51: astore_3
    //   52: aload 4
    //   54: astore_2
    //   55: aload_0
    //   56: arraylength
    //   57: ifeq +131 -> 188
    //   60: aload_0
    //   61: iconst_0
    //   62: aaload
    //   63: astore_0
    //   64: aload 4
    //   66: astore_3
    //   67: aload 4
    //   69: astore_2
    //   70: aload_0
    //   71: aload 5
    //   73: invokevirtual 177	sun/security/pkcs/SignerInfo:getCertificateChain	(Lsun/security/pkcs/PKCS7;)Ljava/util/ArrayList;
    //   76: astore_0
    //   77: aload_0
    //   78: ifnull +80 -> 158
    //   81: aload 4
    //   83: astore_3
    //   84: aload 4
    //   86: astore_2
    //   87: aload_0
    //   88: invokeinterface 183 1 0
    //   93: ifne +35 -> 128
    //   96: aload 4
    //   98: astore_3
    //   99: aload 4
    //   101: astore_2
    //   102: aload_0
    //   103: aload_0
    //   104: invokeinterface 186 1 0
    //   109: anewarray 188	java/security/cert/X509Certificate
    //   112: invokeinterface 192 2 0
    //   117: checkcast 194	[Ljava/security/cert/Certificate;
    //   120: astore_0
    //   121: aload 4
    //   123: invokestatic 198	sun/security/jca/Providers:stopJarVerification	(Ljava/lang/Object;)V
    //   126: aload_0
    //   127: areturn
    //   128: aload 4
    //   130: astore_3
    //   131: aload 4
    //   133: astore_2
    //   134: new 155	java/security/GeneralSecurityException
    //   137: astore_0
    //   138: aload 4
    //   140: astore_3
    //   141: aload 4
    //   143: astore_2
    //   144: aload_0
    //   145: ldc -56
    //   147: invokespecial 201	java/security/GeneralSecurityException:<init>	(Ljava/lang/String;)V
    //   150: aload 4
    //   152: astore_3
    //   153: aload 4
    //   155: astore_2
    //   156: aload_0
    //   157: athrow
    //   158: aload 4
    //   160: astore_3
    //   161: aload 4
    //   163: astore_2
    //   164: new 155	java/security/GeneralSecurityException
    //   167: astore_0
    //   168: aload 4
    //   170: astore_3
    //   171: aload 4
    //   173: astore_2
    //   174: aload_0
    //   175: ldc -53
    //   177: invokespecial 201	java/security/GeneralSecurityException:<init>	(Ljava/lang/String;)V
    //   180: aload 4
    //   182: astore_3
    //   183: aload 4
    //   185: astore_2
    //   186: aload_0
    //   187: athrow
    //   188: aload 4
    //   190: astore_3
    //   191: aload 4
    //   193: astore_2
    //   194: new 155	java/security/GeneralSecurityException
    //   197: astore_0
    //   198: aload 4
    //   200: astore_3
    //   201: aload 4
    //   203: astore_2
    //   204: aload_0
    //   205: ldc -51
    //   207: invokespecial 201	java/security/GeneralSecurityException:<init>	(Ljava/lang/String;)V
    //   210: aload 4
    //   212: astore_3
    //   213: aload 4
    //   215: astore_2
    //   216: aload_0
    //   217: athrow
    //   218: astore_0
    //   219: goto +23 -> 242
    //   222: astore_1
    //   223: aload_2
    //   224: astore_3
    //   225: new 155	java/security/GeneralSecurityException
    //   228: astore_0
    //   229: aload_2
    //   230: astore_3
    //   231: aload_0
    //   232: ldc -49
    //   234: aload_1
    //   235: invokespecial 208	java/security/GeneralSecurityException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   238: aload_2
    //   239: astore_3
    //   240: aload_0
    //   241: athrow
    //   242: aload_3
    //   243: invokestatic 198	sun/security/jca/Providers:stopJarVerification	(Ljava/lang/Object;)V
    //   246: aload_0
    //   247: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	248	0	paramArrayOfByte1	byte[]
    //   0	248	1	paramArrayOfByte2	byte[]
    //   1	238	2	localObject1	Object
    //   3	240	3	localObject2	Object
    //   7	207	4	localObject3	Object
    //   18	54	5	localPKCS7	sun.security.pkcs.PKCS7
    // Exception table:
    //   from	to	target	type
    //   4	9	218	finally
    //   15	20	218	finally
    //   26	32	218	finally
    //   38	45	218	finally
    //   55	60	218	finally
    //   70	77	218	finally
    //   87	96	218	finally
    //   102	121	218	finally
    //   134	138	218	finally
    //   144	150	218	finally
    //   156	158	218	finally
    //   164	168	218	finally
    //   174	180	218	finally
    //   186	188	218	finally
    //   194	198	218	finally
    //   204	210	218	finally
    //   216	218	218	finally
    //   225	229	218	finally
    //   231	238	218	finally
    //   240	242	218	finally
    //   4	9	222	java/io/IOException
    //   15	20	222	java/io/IOException
    //   26	32	222	java/io/IOException
    //   38	45	222	java/io/IOException
    //   55	60	222	java/io/IOException
    //   70	77	222	java/io/IOException
    //   87	96	222	java/io/IOException
    //   102	121	222	java/io/IOException
    //   134	138	222	java/io/IOException
    //   144	150	222	java/io/IOException
    //   156	158	222	java/io/IOException
    //   164	168	222	java/io/IOException
    //   174	180	222	java/io/IOException
    //   186	188	222	java/io/IOException
    //   194	198	222	java/io/IOException
    //   204	210	222	java/io/IOException
    //   216	218	222	java/io/IOException
  }
  
  /* Error */
  private void verifyCertificate(String paramString)
  {
    // Byte code:
    //   0: new 93	java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial 94	java/lang/StringBuilder:<init>	()V
    //   7: astore_2
    //   8: aload_2
    //   9: aload_1
    //   10: iconst_0
    //   11: aload_1
    //   12: bipush 46
    //   14: invokevirtual 216	java/lang/String:lastIndexOf	(I)I
    //   17: invokevirtual 220	java/lang/String:substring	(II)Ljava/lang/String;
    //   20: invokevirtual 98	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   23: pop
    //   24: aload_2
    //   25: ldc -34
    //   27: invokevirtual 98	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   30: pop
    //   31: aload_2
    //   32: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   35: astore_3
    //   36: aload_0
    //   37: getfield 67	android/util/jar/StrictJarVerifier:metaEntries	Ljava/util/HashMap;
    //   40: aload_3
    //   41: invokevirtual 228	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   44: checkcast 230	[B
    //   47: astore 4
    //   49: aload 4
    //   51: ifnonnull +4 -> 55
    //   54: return
    //   55: aload_0
    //   56: getfield 67	android/util/jar/StrictJarVerifier:metaEntries	Ljava/util/HashMap;
    //   59: ldc -24
    //   61: invokevirtual 228	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   64: checkcast 230	[B
    //   67: astore 5
    //   69: aload 5
    //   71: ifnonnull +4 -> 75
    //   74: return
    //   75: aload_0
    //   76: getfield 67	android/util/jar/StrictJarVerifier:metaEntries	Ljava/util/HashMap;
    //   79: aload_1
    //   80: invokevirtual 228	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   83: checkcast 230	[B
    //   86: astore 6
    //   88: aload 6
    //   90: aload 4
    //   92: invokestatic 234	android/util/jar/StrictJarVerifier:verifyBytes	([B[B)[Ljava/security/cert/Certificate;
    //   95: astore_1
    //   96: aload_1
    //   97: ifnull +20 -> 117
    //   100: aload_0
    //   101: getfield 58	android/util/jar/StrictJarVerifier:certificates	Ljava/util/Hashtable;
    //   104: aload_3
    //   105: aload_1
    //   106: invokevirtual 238	java/util/Hashtable:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   109: pop
    //   110: goto +7 -> 117
    //   113: astore_1
    //   114: goto +560 -> 674
    //   117: new 123	java/util/jar/Attributes
    //   120: dup
    //   121: invokespecial 239	java/util/jar/Attributes:<init>	()V
    //   124: astore_2
    //   125: new 224	java/util/HashMap
    //   128: dup
    //   129: invokespecial 240	java/util/HashMap:<init>	()V
    //   132: astore 7
    //   134: new 242	android/util/jar/StrictJarManifestReader
    //   137: astore_1
    //   138: aload_1
    //   139: aload 4
    //   141: aload_2
    //   142: invokespecial 245	android/util/jar/StrictJarManifestReader:<init>	([BLjava/util/jar/Attributes;)V
    //   145: aload_1
    //   146: aload 7
    //   148: aconst_null
    //   149: invokevirtual 249	android/util/jar/StrictJarManifestReader:readEntries	(Ljava/util/Map;Ljava/util/Map;)V
    //   152: aload_0
    //   153: getfield 77	android/util/jar/StrictJarVerifier:signatureSchemeRollbackProtectionsEnforced	Z
    //   156: ifeq +228 -> 384
    //   159: aload_2
    //   160: ldc 13
    //   162: invokevirtual 127	java/util/jar/Attributes:getValue	(Ljava/lang/String;)Ljava/lang/String;
    //   165: astore_1
    //   166: aload_1
    //   167: ifnull +217 -> 384
    //   170: iconst_0
    //   171: istore 8
    //   173: iconst_0
    //   174: istore 9
    //   176: new 251	java/util/StringTokenizer
    //   179: dup
    //   180: aload_1
    //   181: ldc -3
    //   183: invokespecial 256	java/util/StringTokenizer:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   186: astore_1
    //   187: iload 8
    //   189: istore 10
    //   191: iload 9
    //   193: istore 11
    //   195: aload_1
    //   196: invokevirtual 259	java/util/StringTokenizer:hasMoreTokens	()Z
    //   199: ifeq +70 -> 269
    //   202: aload_1
    //   203: invokevirtual 262	java/util/StringTokenizer:nextToken	()Ljava/lang/String;
    //   206: invokevirtual 265	java/lang/String:trim	()Ljava/lang/String;
    //   209: astore 4
    //   211: aload 4
    //   213: invokevirtual 266	java/lang/String:isEmpty	()Z
    //   216: ifeq +6 -> 222
    //   219: goto +47 -> 266
    //   222: aload 4
    //   224: invokestatic 272	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   227: istore 10
    //   229: iload 10
    //   231: iconst_2
    //   232: if_icmpne +13 -> 245
    //   235: iconst_1
    //   236: istore 10
    //   238: iload 9
    //   240: istore 11
    //   242: goto +27 -> 269
    //   245: iload 10
    //   247: iconst_3
    //   248: if_icmpne +13 -> 261
    //   251: iconst_1
    //   252: istore 11
    //   254: iload 8
    //   256: istore 10
    //   258: goto +11 -> 269
    //   261: goto +5 -> 266
    //   264: astore 4
    //   266: goto -79 -> 187
    //   269: iload 10
    //   271: ifne +62 -> 333
    //   274: iload 11
    //   276: ifne +6 -> 282
    //   279: goto +105 -> 384
    //   282: new 93	java/lang/StringBuilder
    //   285: dup
    //   286: invokespecial 94	java/lang/StringBuilder:<init>	()V
    //   289: astore_1
    //   290: aload_1
    //   291: aload_3
    //   292: invokevirtual 98	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   295: pop
    //   296: aload_1
    //   297: ldc_w 274
    //   300: invokevirtual 98	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   303: pop
    //   304: aload_1
    //   305: aload_0
    //   306: getfield 63	android/util/jar/StrictJarVerifier:jarName	Ljava/lang/String;
    //   309: invokevirtual 98	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   312: pop
    //   313: aload_1
    //   314: ldc_w 276
    //   317: invokevirtual 98	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   320: pop
    //   321: new 102	java/lang/SecurityException
    //   324: dup
    //   325: aload_1
    //   326: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   329: invokespecial 109	java/lang/SecurityException:<init>	(Ljava/lang/String;)V
    //   332: athrow
    //   333: new 93	java/lang/StringBuilder
    //   336: dup
    //   337: invokespecial 94	java/lang/StringBuilder:<init>	()V
    //   340: astore_1
    //   341: aload_1
    //   342: aload_3
    //   343: invokevirtual 98	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   346: pop
    //   347: aload_1
    //   348: ldc_w 274
    //   351: invokevirtual 98	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   354: pop
    //   355: aload_1
    //   356: aload_0
    //   357: getfield 63	android/util/jar/StrictJarVerifier:jarName	Ljava/lang/String;
    //   360: invokevirtual 98	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   363: pop
    //   364: aload_1
    //   365: ldc_w 278
    //   368: invokevirtual 98	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   371: pop
    //   372: new 102	java/lang/SecurityException
    //   375: dup
    //   376: aload_1
    //   377: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   380: invokespecial 109	java/lang/SecurityException:<init>	(Ljava/lang/String;)V
    //   383: athrow
    //   384: aload_2
    //   385: getstatic 284	java/util/jar/Attributes$Name:SIGNATURE_VERSION	Ljava/util/jar/Attributes$Name;
    //   388: invokevirtual 285	java/util/jar/Attributes:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   391: ifnonnull +4 -> 395
    //   394: return
    //   395: iconst_0
    //   396: istore 12
    //   398: aload_2
    //   399: ldc_w 287
    //   402: invokevirtual 127	java/util/jar/Attributes:getValue	(Ljava/lang/String;)Ljava/lang/String;
    //   405: astore_1
    //   406: aload_1
    //   407: ifnull +23 -> 430
    //   410: aload_1
    //   411: ldc_w 289
    //   414: invokevirtual 292	java/lang/String:indexOf	(Ljava/lang/String;)I
    //   417: iconst_m1
    //   418: if_icmpeq +9 -> 427
    //   421: iconst_1
    //   422: istore 12
    //   424: goto +6 -> 430
    //   427: iconst_0
    //   428: istore 12
    //   430: aload_0
    //   431: getfield 75	android/util/jar/StrictJarVerifier:mainAttributesEnd	I
    //   434: ifle +40 -> 474
    //   437: iload 12
    //   439: ifne +35 -> 474
    //   442: aload_0
    //   443: aload_2
    //   444: ldc_w 294
    //   447: aload 5
    //   449: iconst_0
    //   450: aload_0
    //   451: getfield 75	android/util/jar/StrictJarVerifier:mainAttributesEnd	I
    //   454: iconst_0
    //   455: iconst_1
    //   456: invokespecial 296	android/util/jar/StrictJarVerifier:verify	(Ljava/util/jar/Attributes;Ljava/lang/String;[BIIZZ)Z
    //   459: ifeq +6 -> 465
    //   462: goto +12 -> 474
    //   465: aload_0
    //   466: getfield 63	android/util/jar/StrictJarVerifier:jarName	Ljava/lang/String;
    //   469: aload_3
    //   470: invokestatic 298	android/util/jar/StrictJarVerifier:failedVerification	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/SecurityException;
    //   473: athrow
    //   474: iload 12
    //   476: ifeq +10 -> 486
    //   479: ldc_w 300
    //   482: astore_1
    //   483: goto +10 -> 493
    //   486: ldc_w 302
    //   489: astore_1
    //   490: goto -7 -> 483
    //   493: aload_2
    //   494: astore 4
    //   496: aload 6
    //   498: astore 4
    //   500: aload 5
    //   502: astore 4
    //   504: aload_0
    //   505: aload_2
    //   506: aload_1
    //   507: aload 5
    //   509: iconst_0
    //   510: aload 5
    //   512: arraylength
    //   513: iconst_0
    //   514: iconst_0
    //   515: invokespecial 296	android/util/jar/StrictJarVerifier:verify	(Ljava/util/jar/Attributes;Ljava/lang/String;[BIIZZ)Z
    //   518: ifne +131 -> 649
    //   521: aload 7
    //   523: invokevirtual 306	java/util/HashMap:entrySet	()Ljava/util/Set;
    //   526: invokeinterface 312 1 0
    //   531: astore 13
    //   533: aload 5
    //   535: astore_1
    //   536: aload_2
    //   537: astore 4
    //   539: aload 6
    //   541: astore 4
    //   543: aload_1
    //   544: astore 4
    //   546: aload 13
    //   548: invokeinterface 317 1 0
    //   553: ifeq +96 -> 649
    //   556: aload 13
    //   558: invokeinterface 320 1 0
    //   563: checkcast 322	java/util/Map$Entry
    //   566: astore 5
    //   568: aload_0
    //   569: getfield 65	android/util/jar/StrictJarVerifier:manifest	Landroid/util/jar/StrictJarManifest;
    //   572: aload 5
    //   574: invokeinterface 325 1 0
    //   579: checkcast 34	java/lang/String
    //   582: invokevirtual 329	android/util/jar/StrictJarManifest:getChunk	(Ljava/lang/String;)Landroid/util/jar/StrictJarManifest$Chunk;
    //   585: astore 4
    //   587: aload 4
    //   589: ifnonnull +4 -> 593
    //   592: return
    //   593: aload_0
    //   594: aload 5
    //   596: invokeinterface 331 1 0
    //   601: checkcast 123	java/util/jar/Attributes
    //   604: ldc_w 300
    //   607: aload_1
    //   608: aload 4
    //   610: getfield 336	android/util/jar/StrictJarManifest$Chunk:start	I
    //   613: aload 4
    //   615: getfield 339	android/util/jar/StrictJarManifest$Chunk:end	I
    //   618: iload 12
    //   620: iconst_0
    //   621: invokespecial 296	android/util/jar/StrictJarVerifier:verify	(Ljava/util/jar/Attributes;Ljava/lang/String;[BIIZZ)Z
    //   624: ifeq +6 -> 630
    //   627: goto -91 -> 536
    //   630: aload_3
    //   631: aload 5
    //   633: invokeinterface 325 1 0
    //   638: checkcast 34	java/lang/String
    //   641: aload_0
    //   642: getfield 63	android/util/jar/StrictJarVerifier:jarName	Ljava/lang/String;
    //   645: invokestatic 89	android/util/jar/StrictJarVerifier:invalidDigest	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/SecurityException;
    //   648: athrow
    //   649: aload_0
    //   650: getfield 67	android/util/jar/StrictJarVerifier:metaEntries	Ljava/util/HashMap;
    //   653: aload_3
    //   654: aconst_null
    //   655: invokevirtual 340	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   658: pop
    //   659: aload_0
    //   660: getfield 56	android/util/jar/StrictJarVerifier:signatures	Ljava/util/Hashtable;
    //   663: aload_3
    //   664: aload 7
    //   666: invokevirtual 238	java/util/Hashtable:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   669: pop
    //   670: return
    //   671: astore_1
    //   672: return
    //   673: astore_1
    //   674: aload_0
    //   675: getfield 63	android/util/jar/StrictJarVerifier:jarName	Ljava/lang/String;
    //   678: aload_3
    //   679: aload_1
    //   680: invokestatic 342	android/util/jar/StrictJarVerifier:failedVerification	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)Ljava/lang/SecurityException;
    //   683: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	684	0	this	StrictJarVerifier
    //   0	684	1	paramString	String
    //   7	530	2	localObject1	Object
    //   35	644	3	str	String
    //   47	176	4	localObject2	Object
    //   264	1	4	localException	Exception
    //   494	120	4	localObject3	Object
    //   67	565	5	localObject4	Object
    //   86	454	6	arrayOfByte	byte[]
    //   132	533	7	localHashMap	HashMap
    //   171	84	8	i	int
    //   174	65	9	j	int
    //   189	81	10	k	int
    //   193	82	11	m	int
    //   396	223	12	bool	boolean
    //   531	26	13	localIterator	Iterator
    // Exception table:
    //   from	to	target	type
    //   100	110	113	java/security/GeneralSecurityException
    //   222	229	264	java/lang/Exception
    //   134	152	671	java/io/IOException
    //   88	96	673	java/security/GeneralSecurityException
  }
  
  private static boolean verifyMessageDigest(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    try
    {
      paramArrayOfByte2 = Base64.getDecoder().decode(paramArrayOfByte2);
      return MessageDigest.isEqual(paramArrayOfByte1, paramArrayOfByte2);
    }
    catch (IllegalArgumentException paramArrayOfByte1) {}
    return false;
  }
  
  void addMetaEntry(String paramString, byte[] paramArrayOfByte)
  {
    metaEntries.put(paramString.toUpperCase(Locale.US), paramArrayOfByte);
  }
  
  Certificate[][] getCertificateChains(String paramString)
  {
    return (Certificate[][])verifiedEntries.get(paramString);
  }
  
  VerifierEntry initEntry(String paramString)
  {
    if ((manifest != null) && (!signatures.isEmpty()))
    {
      Attributes localAttributes = manifest.getAttributes(paramString);
      if (localAttributes == null) {
        return null;
      }
      Object localObject1 = new ArrayList();
      Object localObject2 = signatures.entrySet().iterator();
      Object localObject3;
      while (((Iterator)localObject2).hasNext())
      {
        localObject3 = (Map.Entry)((Iterator)localObject2).next();
        if (((HashMap)((Map.Entry)localObject3).getValue()).get(paramString) != null)
        {
          localObject3 = (String)((Map.Entry)localObject3).getKey();
          localObject3 = (Certificate[])certificates.get(localObject3);
          if (localObject3 != null) {
            ((ArrayList)localObject1).add(localObject3);
          }
        }
      }
      if (((ArrayList)localObject1).isEmpty()) {
        return null;
      }
      localObject2 = (Certificate[][])((ArrayList)localObject1).toArray(new Certificate[((ArrayList)localObject1).size()][]);
      int i = 0;
      while (i < DIGEST_ALGORITHMS.length)
      {
        localObject3 = DIGEST_ALGORITHMS[i];
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append((String)localObject3);
        ((StringBuilder)localObject1).append("-Digest");
        localObject1 = localAttributes.getValue(((StringBuilder)localObject1).toString());
        if (localObject1 != null)
        {
          localObject1 = ((String)localObject1).getBytes(StandardCharsets.ISO_8859_1);
          try
          {
            localObject3 = MessageDigest.getInstance((String)localObject3);
            Hashtable localHashtable = verifiedEntries;
            try
            {
              localObject1 = new VerifierEntry(paramString, (MessageDigest)localObject3, (byte[])localObject1, (Certificate[][])localObject2, localHashtable);
              return localObject1;
            }
            catch (NoSuchAlgorithmException localNoSuchAlgorithmException1) {}
            i++;
          }
          catch (NoSuchAlgorithmException localNoSuchAlgorithmException2) {}
        }
      }
      return null;
    }
    return null;
  }
  
  boolean isSignedJar()
  {
    boolean bool;
    if (certificates.size() > 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  boolean readCertificates()
  {
    try
    {
      boolean bool = metaEntries.isEmpty();
      if (bool) {
        return false;
      }
      Iterator localIterator = metaEntries.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        if ((str.endsWith(".DSA")) || (str.endsWith(".RSA")) || (str.endsWith(".EC")))
        {
          verifyCertificate(str);
          localIterator.remove();
        }
      }
      return true;
    }
    finally {}
  }
  
  void removeMetaEntries()
  {
    metaEntries.clear();
  }
  
  static class VerifierEntry
    extends OutputStream
  {
    private final Certificate[][] certChains;
    private final MessageDigest digest;
    private final byte[] hash;
    private final String name;
    private final Hashtable<String, Certificate[][]> verifiedEntries;
    
    VerifierEntry(String paramString, MessageDigest paramMessageDigest, byte[] paramArrayOfByte, Certificate[][] paramArrayOfCertificate, Hashtable<String, Certificate[][]> paramHashtable)
    {
      name = paramString;
      digest = paramMessageDigest;
      hash = paramArrayOfByte;
      certChains = paramArrayOfCertificate;
      verifiedEntries = paramHashtable;
    }
    
    void verify()
    {
      if (StrictJarVerifier.verifyMessageDigest(digest.digest(), hash))
      {
        verifiedEntries.put(name, certChains);
        return;
      }
      throw StrictJarVerifier.invalidDigest("META-INF/MANIFEST.MF", name, name);
    }
    
    public void write(int paramInt)
    {
      digest.update((byte)paramInt);
    }
    
    public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    {
      digest.update(paramArrayOfByte, paramInt1, paramInt2);
    }
  }
}
