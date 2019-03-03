package android.security.keystore;

import android.security.KeyPairGeneratorSpec;
import android.security.KeyStore;
import android.security.keymaster.KeyCharacteristics;
import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.KeymasterCertificateChain;
import com.android.internal.util.ArrayUtils;
import com.android.org.bouncycastle.x509.X509V3CertificateGenerator;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGeneratorSpi;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import libcore.util.EmptyArray;

public abstract class AndroidKeyStoreKeyPairGeneratorSpi
  extends KeyPairGeneratorSpi
{
  private static final int EC_DEFAULT_KEY_SIZE = 256;
  private static final int RSA_DEFAULT_KEY_SIZE = 2048;
  private static final int RSA_MAX_KEY_SIZE = 8192;
  private static final int RSA_MIN_KEY_SIZE = 512;
  private static final List<String> SUPPORTED_EC_NIST_CURVE_NAMES;
  private static final Map<String, Integer> SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE = new HashMap();
  private static final List<Integer> SUPPORTED_EC_NIST_CURVE_SIZES;
  private boolean mEncryptionAtRestRequired;
  private String mEntryAlias;
  private int mEntryUid;
  private String mJcaKeyAlgorithm;
  private int mKeySizeBits;
  private KeyStore mKeyStore;
  private int mKeymasterAlgorithm = -1;
  private int[] mKeymasterBlockModes;
  private int[] mKeymasterDigests;
  private int[] mKeymasterEncryptionPaddings;
  private int[] mKeymasterPurposes;
  private int[] mKeymasterSignaturePaddings;
  private final int mOriginalKeymasterAlgorithm;
  private BigInteger mRSAPublicExponent;
  private SecureRandom mRng;
  private KeyGenParameterSpec mSpec;
  
  static
  {
    SUPPORTED_EC_NIST_CURVE_NAMES = new ArrayList();
    SUPPORTED_EC_NIST_CURVE_SIZES = new ArrayList();
    SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("p-224", Integer.valueOf(224));
    SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("secp224r1", Integer.valueOf(224));
    SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("p-256", Integer.valueOf(256));
    SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("secp256r1", Integer.valueOf(256));
    SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("prime256v1", Integer.valueOf(256));
    SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("p-384", Integer.valueOf(384));
    SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("secp384r1", Integer.valueOf(384));
    SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("p-521", Integer.valueOf(521));
    SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.put("secp521r1", Integer.valueOf(521));
    SUPPORTED_EC_NIST_CURVE_NAMES.addAll(SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.keySet());
    Collections.sort(SUPPORTED_EC_NIST_CURVE_NAMES);
    SUPPORTED_EC_NIST_CURVE_SIZES.addAll(new HashSet(SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.values()));
    Collections.sort(SUPPORTED_EC_NIST_CURVE_SIZES);
  }
  
  protected AndroidKeyStoreKeyPairGeneratorSpi(int paramInt)
  {
    mOriginalKeymasterAlgorithm = paramInt;
  }
  
  private void addAlgorithmSpecificParameters(KeymasterArguments paramKeymasterArguments)
  {
    int i = mKeymasterAlgorithm;
    if (i != 1)
    {
      if (i != 3)
      {
        paramKeymasterArguments = new StringBuilder();
        paramKeymasterArguments.append("Unsupported algorithm: ");
        paramKeymasterArguments.append(mKeymasterAlgorithm);
        throw new ProviderException(paramKeymasterArguments.toString());
      }
    }
    else {
      paramKeymasterArguments.addUnsignedLong(1342177480, mRSAPublicExponent);
    }
  }
  
  private static void checkValidKeySize(int paramInt1, int paramInt2)
    throws InvalidAlgorithmParameterException
  {
    if (paramInt1 != 1)
    {
      StringBuilder localStringBuilder;
      if (paramInt1 == 3)
      {
        if (!SUPPORTED_EC_NIST_CURVE_SIZES.contains(Integer.valueOf(paramInt2)))
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("Unsupported EC key size: ");
          localStringBuilder.append(paramInt2);
          localStringBuilder.append(" bits. Supported: ");
          localStringBuilder.append(SUPPORTED_EC_NIST_CURVE_SIZES);
          throw new InvalidAlgorithmParameterException(localStringBuilder.toString());
        }
      }
      else
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unsupported algorithm: ");
        localStringBuilder.append(paramInt1);
        throw new ProviderException(localStringBuilder.toString());
      }
    }
    else
    {
      if ((paramInt2 < 512) || (paramInt2 > 8192)) {
        break label124;
      }
    }
    return;
    label124:
    throw new InvalidAlgorithmParameterException("RSA key size must be >= 512 and <= 8192");
  }
  
  private KeymasterArguments constructKeyGenerationArguments()
  {
    KeymasterArguments localKeymasterArguments = new KeymasterArguments();
    localKeymasterArguments.addUnsignedInt(805306371, mKeySizeBits);
    localKeymasterArguments.addEnum(268435458, mKeymasterAlgorithm);
    localKeymasterArguments.addEnums(536870913, mKeymasterPurposes);
    localKeymasterArguments.addEnums(536870916, mKeymasterBlockModes);
    localKeymasterArguments.addEnums(536870918, mKeymasterEncryptionPaddings);
    localKeymasterArguments.addEnums(536870918, mKeymasterSignaturePaddings);
    localKeymasterArguments.addEnums(536870917, mKeymasterDigests);
    KeymasterUtils.addUserAuthArgs(localKeymasterArguments, mSpec);
    localKeymasterArguments.addDateIfNotNull(1610613136, mSpec.getKeyValidityStart());
    localKeymasterArguments.addDateIfNotNull(1610613137, mSpec.getKeyValidityForOriginationEnd());
    localKeymasterArguments.addDateIfNotNull(1610613138, mSpec.getKeyValidityForConsumptionEnd());
    addAlgorithmSpecificParameters(localKeymasterArguments);
    if (mSpec.isUniqueIdIncluded()) {
      localKeymasterArguments.addBoolean(1879048394);
    }
    return localKeymasterArguments;
  }
  
  private Iterable<byte[]> createCertificateChain(String paramString, KeyPair paramKeyPair)
    throws ProviderException
  {
    byte[] arrayOfByte = mSpec.getAttestationChallenge();
    if (arrayOfByte != null)
    {
      KeymasterArguments localKeymasterArguments = new KeymasterArguments();
      localKeymasterArguments.addBytes(-1879047484, arrayOfByte);
      return getAttestationChain(paramString, paramKeyPair, localKeymasterArguments);
    }
    return Collections.singleton(generateSelfSignedCertificateBytes(paramKeyPair));
  }
  
  private void generateKeystoreKeyPair(String paramString, KeymasterArguments paramKeymasterArguments, byte[] paramArrayOfByte, int paramInt)
    throws ProviderException
  {
    KeyCharacteristics localKeyCharacteristics = new KeyCharacteristics();
    paramInt = mKeyStore.generateKey(paramString, paramKeymasterArguments, paramArrayOfByte, mEntryUid, paramInt, localKeyCharacteristics);
    if (paramInt != 1)
    {
      if (paramInt == -68) {
        throw new StrongBoxUnavailableException("Failed to generate key pair");
      }
      throw new ProviderException("Failed to generate key pair", KeyStore.getKeyStoreException(paramInt));
    }
  }
  
  private X509Certificate generateSelfSignedCertificate(PrivateKey paramPrivateKey, PublicKey paramPublicKey)
    throws CertificateParsingException, IOException
  {
    String str = getCertificateSignatureAlgorithm(mKeymasterAlgorithm, mKeySizeBits, mSpec);
    if (str == null) {
      return generateSelfSignedCertificateWithFakeSignature(paramPublicKey);
    }
    try
    {
      paramPrivateKey = generateSelfSignedCertificateWithValidSignature(paramPrivateKey, paramPublicKey, str);
      return paramPrivateKey;
    }
    catch (Exception paramPrivateKey) {}
    return generateSelfSignedCertificateWithFakeSignature(paramPublicKey);
  }
  
  private byte[] generateSelfSignedCertificateBytes(KeyPair paramKeyPair)
    throws ProviderException
  {
    try
    {
      paramKeyPair = generateSelfSignedCertificate(paramKeyPair.getPrivate(), paramKeyPair.getPublic()).getEncoded();
      return paramKeyPair;
    }
    catch (CertificateEncodingException paramKeyPair)
    {
      throw new ProviderException("Failed to obtain encoded form of self-signed certificate", paramKeyPair);
    }
    catch (IOException|CertificateParsingException paramKeyPair)
    {
      throw new ProviderException("Failed to generate self-signed certificate", paramKeyPair);
    }
  }
  
  /* Error */
  private X509Certificate generateSelfSignedCertificateWithFakeSignature(PublicKey paramPublicKey)
    throws IOException, CertificateParsingException
  {
    // Byte code:
    //   0: new 347	com/android/org/bouncycastle/asn1/x509/V3TBSCertificateGenerator
    //   3: dup
    //   4: invokespecial 348	com/android/org/bouncycastle/asn1/x509/V3TBSCertificateGenerator:<init>	()V
    //   7: astore_2
    //   8: aload_0
    //   9: getfield 126	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:mKeymasterAlgorithm	I
    //   12: istore_3
    //   13: iload_3
    //   14: iconst_1
    //   15: if_icmpeq +107 -> 122
    //   18: iload_3
    //   19: iconst_3
    //   20: if_icmpne +65 -> 85
    //   23: new 350	com/android/org/bouncycastle/asn1/x509/AlgorithmIdentifier
    //   26: dup
    //   27: getstatic 356	com/android/org/bouncycastle/asn1/x9/X9ObjectIdentifiers:ecdsa_with_SHA256	Lcom/android/org/bouncycastle/asn1/ASN1ObjectIdentifier;
    //   30: invokespecial 359	com/android/org/bouncycastle/asn1/x509/AlgorithmIdentifier:<init>	(Lcom/android/org/bouncycastle/asn1/ASN1ObjectIdentifier;)V
    //   33: astore 4
    //   35: new 361	com/android/org/bouncycastle/asn1/ASN1EncodableVector
    //   38: dup
    //   39: invokespecial 362	com/android/org/bouncycastle/asn1/ASN1EncodableVector:<init>	()V
    //   42: astore 5
    //   44: aload 5
    //   46: new 364	com/android/org/bouncycastle/asn1/DERInteger
    //   49: dup
    //   50: lconst_0
    //   51: invokespecial 367	com/android/org/bouncycastle/asn1/DERInteger:<init>	(J)V
    //   54: invokevirtual 371	com/android/org/bouncycastle/asn1/ASN1EncodableVector:add	(Lcom/android/org/bouncycastle/asn1/ASN1Encodable;)V
    //   57: aload 5
    //   59: new 364	com/android/org/bouncycastle/asn1/DERInteger
    //   62: dup
    //   63: lconst_0
    //   64: invokespecial 367	com/android/org/bouncycastle/asn1/DERInteger:<init>	(J)V
    //   67: invokevirtual 371	com/android/org/bouncycastle/asn1/ASN1EncodableVector:add	(Lcom/android/org/bouncycastle/asn1/ASN1Encodable;)V
    //   70: new 373	com/android/org/bouncycastle/asn1/DERSequence
    //   73: dup
    //   74: invokespecial 374	com/android/org/bouncycastle/asn1/DERSequence:<init>	()V
    //   77: invokevirtual 375	com/android/org/bouncycastle/asn1/DERSequence:getEncoded	()[B
    //   80: astore 5
    //   82: goto +60 -> 142
    //   85: new 132	java/lang/StringBuilder
    //   88: dup
    //   89: invokespecial 133	java/lang/StringBuilder:<init>	()V
    //   92: astore_1
    //   93: aload_1
    //   94: ldc_w 377
    //   97: invokevirtual 139	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   100: pop
    //   101: aload_1
    //   102: aload_0
    //   103: getfield 126	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:mKeymasterAlgorithm	I
    //   106: invokevirtual 142	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   109: pop
    //   110: new 144	java/security/ProviderException
    //   113: dup
    //   114: aload_1
    //   115: invokevirtual 148	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   118: invokespecial 151	java/security/ProviderException:<init>	(Ljava/lang/String;)V
    //   121: athrow
    //   122: new 350	com/android/org/bouncycastle/asn1/x509/AlgorithmIdentifier
    //   125: dup
    //   126: getstatic 382	com/android/org/bouncycastle/asn1/pkcs/PKCSObjectIdentifiers:sha256WithRSAEncryption	Lcom/android/org/bouncycastle/asn1/ASN1ObjectIdentifier;
    //   129: getstatic 388	com/android/org/bouncycastle/asn1/DERNull:INSTANCE	Lcom/android/org/bouncycastle/asn1/DERNull;
    //   132: invokespecial 391	com/android/org/bouncycastle/asn1/x509/AlgorithmIdentifier:<init>	(Lcom/android/org/bouncycastle/asn1/ASN1ObjectIdentifier;Lcom/android/org/bouncycastle/asn1/ASN1Encodable;)V
    //   135: astore 4
    //   137: iconst_1
    //   138: newarray byte
    //   140: astore 5
    //   142: new 393	com/android/org/bouncycastle/asn1/ASN1InputStream
    //   145: dup
    //   146: aload_1
    //   147: invokeinterface 396 1 0
    //   152: invokespecial 399	com/android/org/bouncycastle/asn1/ASN1InputStream:<init>	([B)V
    //   155: astore 6
    //   157: aconst_null
    //   158: astore_1
    //   159: aload_2
    //   160: aload 6
    //   162: invokevirtual 403	com/android/org/bouncycastle/asn1/ASN1InputStream:readObject	()Lcom/android/org/bouncycastle/asn1/ASN1Primitive;
    //   165: invokestatic 409	com/android/org/bouncycastle/asn1/x509/SubjectPublicKeyInfo:getInstance	(Ljava/lang/Object;)Lcom/android/org/bouncycastle/asn1/x509/SubjectPublicKeyInfo;
    //   168: invokevirtual 413	com/android/org/bouncycastle/asn1/x509/V3TBSCertificateGenerator:setSubjectPublicKeyInfo	(Lcom/android/org/bouncycastle/asn1/x509/SubjectPublicKeyInfo;)V
    //   171: aload 6
    //   173: invokevirtual 416	com/android/org/bouncycastle/asn1/ASN1InputStream:close	()V
    //   176: aload_2
    //   177: new 418	com/android/org/bouncycastle/asn1/ASN1Integer
    //   180: dup
    //   181: aload_0
    //   182: getfield 213	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:mSpec	Landroid/security/keystore/KeyGenParameterSpec;
    //   185: invokevirtual 422	android/security/keystore/KeyGenParameterSpec:getCertificateSerialNumber	()Ljava/math/BigInteger;
    //   188: invokespecial 425	com/android/org/bouncycastle/asn1/ASN1Integer:<init>	(Ljava/math/BigInteger;)V
    //   191: invokevirtual 429	com/android/org/bouncycastle/asn1/x509/V3TBSCertificateGenerator:setSerialNumber	(Lcom/android/org/bouncycastle/asn1/ASN1Integer;)V
    //   194: new 431	com/android/org/bouncycastle/jce/X509Principal
    //   197: dup
    //   198: aload_0
    //   199: getfield 213	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:mSpec	Landroid/security/keystore/KeyGenParameterSpec;
    //   202: invokevirtual 435	android/security/keystore/KeyGenParameterSpec:getCertificateSubject	()Ljavax/security/auth/x500/X500Principal;
    //   205: invokevirtual 438	javax/security/auth/x500/X500Principal:getEncoded	()[B
    //   208: invokespecial 439	com/android/org/bouncycastle/jce/X509Principal:<init>	([B)V
    //   211: astore_1
    //   212: aload_2
    //   213: aload_1
    //   214: invokevirtual 443	com/android/org/bouncycastle/asn1/x509/V3TBSCertificateGenerator:setSubject	(Lcom/android/org/bouncycastle/asn1/x509/X509Name;)V
    //   217: aload_2
    //   218: aload_1
    //   219: invokevirtual 446	com/android/org/bouncycastle/asn1/x509/V3TBSCertificateGenerator:setIssuer	(Lcom/android/org/bouncycastle/asn1/x509/X509Name;)V
    //   222: aload_2
    //   223: new 448	com/android/org/bouncycastle/asn1/x509/Time
    //   226: dup
    //   227: aload_0
    //   228: getfield 213	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:mSpec	Landroid/security/keystore/KeyGenParameterSpec;
    //   231: invokevirtual 451	android/security/keystore/KeyGenParameterSpec:getCertificateNotBefore	()Ljava/util/Date;
    //   234: invokespecial 454	com/android/org/bouncycastle/asn1/x509/Time:<init>	(Ljava/util/Date;)V
    //   237: invokevirtual 458	com/android/org/bouncycastle/asn1/x509/V3TBSCertificateGenerator:setStartDate	(Lcom/android/org/bouncycastle/asn1/x509/Time;)V
    //   240: aload_2
    //   241: new 448	com/android/org/bouncycastle/asn1/x509/Time
    //   244: dup
    //   245: aload_0
    //   246: getfield 213	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:mSpec	Landroid/security/keystore/KeyGenParameterSpec;
    //   249: invokevirtual 461	android/security/keystore/KeyGenParameterSpec:getCertificateNotAfter	()Ljava/util/Date;
    //   252: invokespecial 454	com/android/org/bouncycastle/asn1/x509/Time:<init>	(Ljava/util/Date;)V
    //   255: invokevirtual 464	com/android/org/bouncycastle/asn1/x509/V3TBSCertificateGenerator:setEndDate	(Lcom/android/org/bouncycastle/asn1/x509/Time;)V
    //   258: aload_2
    //   259: aload 4
    //   261: invokevirtual 468	com/android/org/bouncycastle/asn1/x509/V3TBSCertificateGenerator:setSignature	(Lcom/android/org/bouncycastle/asn1/x509/AlgorithmIdentifier;)V
    //   264: aload_2
    //   265: invokevirtual 472	com/android/org/bouncycastle/asn1/x509/V3TBSCertificateGenerator:generateTBSCertificate	()Lcom/android/org/bouncycastle/asn1/x509/TBSCertificate;
    //   268: astore_2
    //   269: new 361	com/android/org/bouncycastle/asn1/ASN1EncodableVector
    //   272: dup
    //   273: invokespecial 362	com/android/org/bouncycastle/asn1/ASN1EncodableVector:<init>	()V
    //   276: astore_1
    //   277: aload_1
    //   278: aload_2
    //   279: invokevirtual 371	com/android/org/bouncycastle/asn1/ASN1EncodableVector:add	(Lcom/android/org/bouncycastle/asn1/ASN1Encodable;)V
    //   282: aload_1
    //   283: aload 4
    //   285: invokevirtual 371	com/android/org/bouncycastle/asn1/ASN1EncodableVector:add	(Lcom/android/org/bouncycastle/asn1/ASN1Encodable;)V
    //   288: aload_1
    //   289: new 474	com/android/org/bouncycastle/asn1/DERBitString
    //   292: dup
    //   293: aload 5
    //   295: invokespecial 475	com/android/org/bouncycastle/asn1/DERBitString:<init>	([B)V
    //   298: invokevirtual 371	com/android/org/bouncycastle/asn1/ASN1EncodableVector:add	(Lcom/android/org/bouncycastle/asn1/ASN1Encodable;)V
    //   301: new 477	com/android/org/bouncycastle/jce/provider/X509CertificateObject
    //   304: dup
    //   305: new 373	com/android/org/bouncycastle/asn1/DERSequence
    //   308: dup
    //   309: aload_1
    //   310: invokespecial 480	com/android/org/bouncycastle/asn1/DERSequence:<init>	(Lcom/android/org/bouncycastle/asn1/ASN1EncodableVector;)V
    //   313: invokestatic 485	com/android/org/bouncycastle/asn1/x509/Certificate:getInstance	(Ljava/lang/Object;)Lcom/android/org/bouncycastle/asn1/x509/Certificate;
    //   316: invokespecial 488	com/android/org/bouncycastle/jce/provider/X509CertificateObject:<init>	(Lcom/android/org/bouncycastle/asn1/x509/Certificate;)V
    //   319: areturn
    //   320: astore 4
    //   322: goto +11 -> 333
    //   325: astore 4
    //   327: aload 4
    //   329: astore_1
    //   330: aload 4
    //   332: athrow
    //   333: aload_1
    //   334: ifnull +22 -> 356
    //   337: aload 6
    //   339: invokevirtual 416	com/android/org/bouncycastle/asn1/ASN1InputStream:close	()V
    //   342: goto +19 -> 361
    //   345: astore 5
    //   347: aload_1
    //   348: aload 5
    //   350: invokevirtual 492	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   353: goto +8 -> 361
    //   356: aload 6
    //   358: invokevirtual 416	com/android/org/bouncycastle/asn1/ASN1InputStream:close	()V
    //   361: aload 4
    //   363: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	364	0	this	AndroidKeyStoreKeyPairGeneratorSpi
    //   0	364	1	paramPublicKey	PublicKey
    //   7	272	2	localObject1	Object
    //   12	9	3	i	int
    //   33	251	4	localAlgorithmIdentifier	com.android.org.bouncycastle.asn1.x509.AlgorithmIdentifier
    //   320	1	4	localObject2	Object
    //   325	37	4	localThrowable1	Throwable
    //   42	252	5	localObject3	Object
    //   345	4	5	localThrowable2	Throwable
    //   155	202	6	localASN1InputStream	com.android.org.bouncycastle.asn1.ASN1InputStream
    // Exception table:
    //   from	to	target	type
    //   159	171	320	finally
    //   330	333	320	finally
    //   159	171	325	java/lang/Throwable
    //   337	342	345	java/lang/Throwable
  }
  
  private X509Certificate generateSelfSignedCertificateWithValidSignature(PrivateKey paramPrivateKey, PublicKey paramPublicKey, String paramString)
    throws Exception
  {
    X509V3CertificateGenerator localX509V3CertificateGenerator = new X509V3CertificateGenerator();
    localX509V3CertificateGenerator.setPublicKey(paramPublicKey);
    localX509V3CertificateGenerator.setSerialNumber(mSpec.getCertificateSerialNumber());
    localX509V3CertificateGenerator.setSubjectDN(mSpec.getCertificateSubject());
    localX509V3CertificateGenerator.setIssuerDN(mSpec.getCertificateSubject());
    localX509V3CertificateGenerator.setNotBefore(mSpec.getCertificateNotBefore());
    localX509V3CertificateGenerator.setNotAfter(mSpec.getCertificateNotAfter());
    localX509V3CertificateGenerator.setSignatureAlgorithm(paramString);
    return localX509V3CertificateGenerator.generate(paramPrivateKey);
  }
  
  private Iterable<byte[]> getAttestationChain(String paramString, KeyPair paramKeyPair, KeymasterArguments paramKeymasterArguments)
    throws ProviderException
  {
    paramKeyPair = new KeymasterCertificateChain();
    int i = mKeyStore.attestKey(paramString, paramKeymasterArguments, paramKeyPair);
    if (i == 1)
    {
      paramKeyPair = paramKeyPair.getCertificates();
      if (paramKeyPair.size() >= 2) {
        return paramKeyPair;
      }
      paramString = new StringBuilder();
      paramString.append("Attestation certificate chain contained ");
      paramString.append(paramKeyPair.size());
      paramString.append(" entries. At least two are required.");
      throw new ProviderException(paramString.toString());
    }
    throw new ProviderException("Failed to generate attestation certificate chain", KeyStore.getKeyStoreException(i));
  }
  
  private static Set<Integer> getAvailableKeymasterSignatureDigests(String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    HashSet localHashSet = new HashSet();
    paramArrayOfString1 = KeyProperties.Digest.allToKeymaster(paramArrayOfString1);
    int i = paramArrayOfString1.length;
    int j = 0;
    for (int k = 0; k < i; k++) {
      localHashSet.add(Integer.valueOf(paramArrayOfString1[k]));
    }
    paramArrayOfString1 = new HashSet();
    paramArrayOfString2 = KeyProperties.Digest.allToKeymaster(paramArrayOfString2);
    i = paramArrayOfString2.length;
    for (k = j; k < i; k++) {
      paramArrayOfString1.add(Integer.valueOf(paramArrayOfString2[k]));
    }
    paramArrayOfString1 = new HashSet(paramArrayOfString1);
    paramArrayOfString1.retainAll(localHashSet);
    return paramArrayOfString1;
  }
  
  private static String getCertificateSignatureAlgorithm(int paramInt1, int paramInt2, KeyGenParameterSpec paramKeyGenParameterSpec)
  {
    if ((paramKeyGenParameterSpec.getPurposes() & 0x4) == 0) {
      return null;
    }
    if (paramKeyGenParameterSpec.isUserAuthenticationRequired()) {
      return null;
    }
    if (!paramKeyGenParameterSpec.isDigestsSpecified()) {
      return null;
    }
    int i;
    int k;
    if (paramInt1 != 1)
    {
      if (paramInt1 == 3)
      {
        paramKeyGenParameterSpec = getAvailableKeymasterSignatureDigests(paramKeyGenParameterSpec.getDigests(), AndroidKeyStoreBCWorkaroundProvider.getSupportedEcdsaSignatureDigests());
        i = -1;
        int j = -1;
        paramKeyGenParameterSpec = paramKeyGenParameterSpec.iterator();
        for (;;)
        {
          paramInt1 = i;
          if (!paramKeyGenParameterSpec.hasNext()) {
            break;
          }
          k = ((Integer)paramKeyGenParameterSpec.next()).intValue();
          m = KeymasterUtils.getDigestOutputSizeBits(k);
          if (m == paramInt2)
          {
            paramInt1 = k;
            break;
          }
          if (i == -1)
          {
            paramInt1 = k;
            n = m;
          }
          else if (j < paramInt2)
          {
            paramInt1 = i;
            n = j;
            if (m > j)
            {
              paramInt1 = k;
              n = m;
            }
          }
          else
          {
            paramInt1 = i;
            n = j;
            if (m < j)
            {
              paramInt1 = i;
              n = j;
              if (m >= paramInt2)
              {
                paramInt1 = k;
                n = m;
              }
            }
          }
          i = paramInt1;
          j = n;
        }
        if (paramInt1 == -1) {
          return null;
        }
        paramKeyGenParameterSpec = new StringBuilder();
        paramKeyGenParameterSpec.append(KeyProperties.Digest.fromKeymasterToSignatureAlgorithmDigest(paramInt1));
        paramKeyGenParameterSpec.append("WithECDSA");
        return paramKeyGenParameterSpec.toString();
      }
      paramKeyGenParameterSpec = new StringBuilder();
      paramKeyGenParameterSpec.append("Unsupported algorithm: ");
      paramKeyGenParameterSpec.append(paramInt1);
      throw new ProviderException(paramKeyGenParameterSpec.toString());
    }
    if (!ArrayUtils.contains(KeyProperties.SignaturePadding.allToKeymaster(paramKeyGenParameterSpec.getSignaturePaddings()), 5)) {
      return null;
    }
    paramKeyGenParameterSpec = getAvailableKeymasterSignatureDigests(paramKeyGenParameterSpec.getDigests(), AndroidKeyStoreBCWorkaroundProvider.getSupportedEcdsaSignatureDigests());
    int n = -1;
    int m = -1;
    paramKeyGenParameterSpec = paramKeyGenParameterSpec.iterator();
    while (paramKeyGenParameterSpec.hasNext())
    {
      i = ((Integer)paramKeyGenParameterSpec.next()).intValue();
      k = KeymasterUtils.getDigestOutputSizeBits(i);
      if (k <= paramInt2 - 240)
      {
        if (n == -1)
        {
          n = i;
          paramInt1 = k;
        }
        else
        {
          paramInt1 = m;
          if (k > m)
          {
            n = i;
            paramInt1 = k;
          }
        }
        m = paramInt1;
      }
    }
    if (n == -1) {
      return null;
    }
    paramKeyGenParameterSpec = new StringBuilder();
    paramKeyGenParameterSpec.append(KeyProperties.Digest.fromKeymasterToSignatureAlgorithmDigest(n));
    paramKeyGenParameterSpec.append("WithRSA");
    return paramKeyGenParameterSpec.toString();
  }
  
  private static int getDefaultKeySize(int paramInt)
  {
    if (paramInt != 1)
    {
      if (paramInt == 3) {
        return 256;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unsupported algorithm: ");
      localStringBuilder.append(paramInt);
      throw new ProviderException(localStringBuilder.toString());
    }
    return 2048;
  }
  
  private void initAlgorithmSpecificParameters()
    throws InvalidAlgorithmParameterException
  {
    Object localObject1 = mSpec.getAlgorithmParameterSpec();
    int i = mKeymasterAlgorithm;
    Object localObject3;
    if (i != 1)
    {
      if (i == 3)
      {
        if ((localObject1 instanceof ECGenParameterSpec))
        {
          localObject2 = ((ECGenParameterSpec)localObject1).getName();
          localObject3 = (Integer)SUPPORTED_EC_NIST_CURVE_NAME_TO_SIZE.get(((String)localObject2).toLowerCase(Locale.US));
          if (localObject3 != null)
          {
            if (mKeySizeBits == -1) {
              mKeySizeBits = ((Integer)localObject3).intValue();
            } else {
              if (mKeySizeBits == ((Integer)localObject3).intValue()) {
                break label467;
              }
            }
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("EC key size must match  between ");
            ((StringBuilder)localObject2).append(mSpec);
            ((StringBuilder)localObject2).append(" and ");
            ((StringBuilder)localObject2).append(localObject1);
            ((StringBuilder)localObject2).append(": ");
            ((StringBuilder)localObject2).append(mKeySizeBits);
            ((StringBuilder)localObject2).append(" vs ");
            ((StringBuilder)localObject2).append(localObject3);
            throw new InvalidAlgorithmParameterException(((StringBuilder)localObject2).toString());
          }
          else
          {
            localObject1 = new StringBuilder();
            ((StringBuilder)localObject1).append("Unsupported EC curve name: ");
            ((StringBuilder)localObject1).append((String)localObject2);
            ((StringBuilder)localObject1).append(". Supported: ");
            ((StringBuilder)localObject1).append(SUPPORTED_EC_NIST_CURVE_NAMES);
            throw new InvalidAlgorithmParameterException(((StringBuilder)localObject1).toString());
          }
        }
        else if (localObject1 != null)
        {
          throw new InvalidAlgorithmParameterException("EC may only use ECGenParameterSpec");
        }
      }
      else
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Unsupported algorithm: ");
        ((StringBuilder)localObject2).append(mKeymasterAlgorithm);
        throw new ProviderException(((StringBuilder)localObject2).toString());
      }
    }
    else
    {
      localObject2 = null;
      if ((localObject1 instanceof RSAKeyGenParameterSpec))
      {
        localObject3 = (RSAKeyGenParameterSpec)localObject1;
        if (mKeySizeBits == -1) {
          mKeySizeBits = ((RSAKeyGenParameterSpec)localObject3).getKeysize();
        } else {
          if (mKeySizeBits != ((RSAKeyGenParameterSpec)localObject3).getKeysize()) {
            break label341;
          }
        }
        localObject2 = ((RSAKeyGenParameterSpec)localObject3).getPublicExponent();
        break label431;
        label341:
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("RSA key size must match  between ");
        ((StringBuilder)localObject2).append(mSpec);
        ((StringBuilder)localObject2).append(" and ");
        ((StringBuilder)localObject2).append(localObject1);
        ((StringBuilder)localObject2).append(": ");
        ((StringBuilder)localObject2).append(mKeySizeBits);
        ((StringBuilder)localObject2).append(" vs ");
        ((StringBuilder)localObject2).append(((RSAKeyGenParameterSpec)localObject3).getKeysize());
        throw new InvalidAlgorithmParameterException(((StringBuilder)localObject2).toString());
      }
      else
      {
        if (localObject1 != null) {
          break label552;
        }
      }
      label431:
      localObject1 = localObject2;
      if (localObject2 == null) {
        localObject1 = RSAKeyGenParameterSpec.F4;
      }
      if (((BigInteger)localObject1).compareTo(BigInteger.ZERO) < 1) {
        break label518;
      }
      if (((BigInteger)localObject1).compareTo(KeymasterArguments.UINT64_MAX_VALUE) > 0) {
        break label468;
      }
      mRSAPublicExponent = ((BigInteger)localObject1);
    }
    label467:
    return;
    label468:
    Object localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("Unsupported RSA public exponent: ");
    ((StringBuilder)localObject2).append(localObject1);
    ((StringBuilder)localObject2).append(". Maximum supported value: ");
    ((StringBuilder)localObject2).append(KeymasterArguments.UINT64_MAX_VALUE);
    throw new InvalidAlgorithmParameterException(((StringBuilder)localObject2).toString());
    label518:
    localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("RSA public exponent must be positive: ");
    ((StringBuilder)localObject2).append(localObject1);
    throw new InvalidAlgorithmParameterException(((StringBuilder)localObject2).toString());
    label552:
    throw new InvalidAlgorithmParameterException("RSA may only use RSAKeyGenParameterSpec");
  }
  
  private KeyPair loadKeystoreKeyPair(String paramString)
    throws ProviderException
  {
    try
    {
      KeyPair localKeyPair = AndroidKeyStoreProvider.loadAndroidKeyStoreKeyPairFromKeystore(mKeyStore, paramString, mEntryUid);
      if (mJcaKeyAlgorithm.equalsIgnoreCase(localKeyPair.getPrivate().getAlgorithm())) {
        return localKeyPair;
      }
      ProviderException localProviderException = new java/security/ProviderException;
      paramString = new java/lang/StringBuilder;
      paramString.<init>();
      paramString.append("Generated key pair algorithm does not match requested algorithm: ");
      paramString.append(localKeyPair.getPrivate().getAlgorithm());
      paramString.append(" vs ");
      paramString.append(mJcaKeyAlgorithm);
      localProviderException.<init>(paramString.toString());
      throw localProviderException;
    }
    catch (UnrecoverableKeyException paramString)
    {
      throw new ProviderException("Failed to load generated key pair from keystore", paramString);
    }
  }
  
  private void resetAll()
  {
    mEntryAlias = null;
    mEntryUid = -1;
    mJcaKeyAlgorithm = null;
    mKeymasterAlgorithm = -1;
    mKeymasterPurposes = null;
    mKeymasterBlockModes = null;
    mKeymasterEncryptionPaddings = null;
    mKeymasterSignaturePaddings = null;
    mKeymasterDigests = null;
    mKeySizeBits = 0;
    mSpec = null;
    mRSAPublicExponent = null;
    mEncryptionAtRestRequired = false;
    mRng = null;
    mKeyStore = null;
  }
  
  private void storeCertificate(String paramString1, byte[] paramArrayOfByte, int paramInt, String paramString2)
    throws ProviderException
  {
    KeyStore localKeyStore = mKeyStore;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString1);
    localStringBuilder.append(mEntryAlias);
    paramInt = localKeyStore.insert(localStringBuilder.toString(), paramArrayOfByte, mEntryUid, paramInt);
    if (paramInt == 1) {
      return;
    }
    throw new ProviderException(paramString2, KeyStore.getKeyStoreException(paramInt));
  }
  
  private void storeCertificateChain(int paramInt, Iterable<byte[]> paramIterable)
    throws ProviderException
  {
    paramIterable = paramIterable.iterator();
    storeCertificate("USRCERT_", (byte[])paramIterable.next(), paramInt, "Failed to store certificate");
    if (!paramIterable.hasNext()) {
      return;
    }
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    while (paramIterable.hasNext())
    {
      byte[] arrayOfByte = (byte[])paramIterable.next();
      localByteArrayOutputStream.write(arrayOfByte, 0, arrayOfByte.length);
    }
    storeCertificate("CACERT_", localByteArrayOutputStream.toByteArray(), paramInt, "Failed to store attestation CA certificate");
  }
  
  /* Error */
  public KeyPair generateKeyPair()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 280	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:mKeyStore	Landroid/security/KeyStore;
    //   4: ifnull +245 -> 249
    //   7: aload_0
    //   8: getfield 213	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:mSpec	Landroid/security/keystore/KeyGenParameterSpec;
    //   11: ifnull +238 -> 249
    //   14: aload_0
    //   15: getfield 725	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:mEncryptionAtRestRequired	Z
    //   18: istore_1
    //   19: iload_1
    //   20: iconst_1
    //   21: iand
    //   22: ifeq +30 -> 52
    //   25: aload_0
    //   26: getfield 280	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:mKeyStore	Landroid/security/KeyStore;
    //   29: invokevirtual 767	android/security/KeyStore:state	()Landroid/security/KeyStore$State;
    //   32: getstatic 773	android/security/KeyStore$State:UNLOCKED	Landroid/security/KeyStore$State;
    //   35: if_acmpne +6 -> 41
    //   38: goto +14 -> 52
    //   41: new 775	java/lang/IllegalStateException
    //   44: dup
    //   45: ldc_w 777
    //   48: invokespecial 778	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   51: athrow
    //   52: iload_1
    //   53: istore_2
    //   54: aload_0
    //   55: getfield 213	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:mSpec	Landroid/security/keystore/KeyGenParameterSpec;
    //   58: invokevirtual 781	android/security/keystore/KeyGenParameterSpec:isStrongBoxBacked	()Z
    //   61: ifeq +8 -> 69
    //   64: iload_1
    //   65: bipush 16
    //   67: ior
    //   68: istore_2
    //   69: aload_0
    //   70: getfield 727	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:mRng	Ljava/security/SecureRandom;
    //   73: aload_0
    //   74: getfield 185	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:mKeySizeBits	I
    //   77: bipush 7
    //   79: iadd
    //   80: bipush 8
    //   82: idiv
    //   83: invokestatic 787	android/security/keystore/KeyStoreCryptoOperationUtils:getRandomBytesToMixIntoKeystoreRng	(Ljava/security/SecureRandom;I)[B
    //   86: astore_3
    //   87: aload_0
    //   88: getfield 280	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:mKeyStore	Landroid/security/KeyStore;
    //   91: aload_0
    //   92: getfield 723	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:mEntryAlias	Ljava/lang/String;
    //   95: aload_0
    //   96: getfield 282	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:mEntryUid	I
    //   99: invokestatic 793	android/security/Credentials:deleteAllTypesForAlias	(Landroid/security/KeyStore;Ljava/lang/String;I)Z
    //   102: pop
    //   103: new 132	java/lang/StringBuilder
    //   106: dup
    //   107: invokespecial 133	java/lang/StringBuilder:<init>	()V
    //   110: astore 4
    //   112: aload 4
    //   114: ldc_w 795
    //   117: invokevirtual 139	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   120: pop
    //   121: aload 4
    //   123: aload_0
    //   124: getfield 723	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:mEntryAlias	Ljava/lang/String;
    //   127: invokevirtual 139	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   130: pop
    //   131: aload 4
    //   133: invokevirtual 148	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   136: astore 4
    //   138: aload_0
    //   139: aload 4
    //   141: aload_0
    //   142: invokespecial 797	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:constructKeyGenerationArguments	()Landroid/security/keymaster/KeymasterArguments;
    //   145: aload_3
    //   146: iload_2
    //   147: invokespecial 799	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:generateKeystoreKeyPair	(Ljava/lang/String;Landroid/security/keymaster/KeymasterArguments;[BI)V
    //   150: aload_0
    //   151: aload 4
    //   153: invokespecial 801	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:loadKeystoreKeyPair	(Ljava/lang/String;)Ljava/security/KeyPair;
    //   156: astore_3
    //   157: aload_0
    //   158: iload_2
    //   159: aload_0
    //   160: aload 4
    //   162: aload_3
    //   163: invokespecial 803	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:createCertificateChain	(Ljava/lang/String;Ljava/security/KeyPair;)Ljava/lang/Iterable;
    //   166: invokespecial 805	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:storeCertificateChain	(ILjava/lang/Iterable;)V
    //   169: iconst_1
    //   170: ifne +19 -> 189
    //   173: aload_0
    //   174: getfield 280	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:mKeyStore	Landroid/security/KeyStore;
    //   177: aload_0
    //   178: getfield 723	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:mEntryAlias	Ljava/lang/String;
    //   181: aload_0
    //   182: getfield 282	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:mEntryUid	I
    //   185: invokestatic 793	android/security/Credentials:deleteAllTypesForAlias	(Landroid/security/KeyStore;Ljava/lang/String;I)Z
    //   188: pop
    //   189: aload_3
    //   190: areturn
    //   191: astore 4
    //   193: goto +33 -> 226
    //   196: astore 4
    //   198: aload_0
    //   199: getfield 213	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:mSpec	Landroid/security/keystore/KeyGenParameterSpec;
    //   202: invokevirtual 565	android/security/keystore/KeyGenParameterSpec:getPurposes	()I
    //   205: bipush 32
    //   207: iand
    //   208: ifeq +15 -> 223
    //   211: new 807	android/security/keystore/SecureKeyImportUnavailableException
    //   214: astore_3
    //   215: aload_3
    //   216: aload 4
    //   218: invokespecial 809	android/security/keystore/SecureKeyImportUnavailableException:<init>	(Ljava/lang/Throwable;)V
    //   221: aload_3
    //   222: athrow
    //   223: aload 4
    //   225: athrow
    //   226: iconst_0
    //   227: ifne +19 -> 246
    //   230: aload_0
    //   231: getfield 280	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:mKeyStore	Landroid/security/KeyStore;
    //   234: aload_0
    //   235: getfield 723	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:mEntryAlias	Ljava/lang/String;
    //   238: aload_0
    //   239: getfield 282	android/security/keystore/AndroidKeyStoreKeyPairGeneratorSpi:mEntryUid	I
    //   242: invokestatic 793	android/security/Credentials:deleteAllTypesForAlias	(Landroid/security/KeyStore;Ljava/lang/String;I)Z
    //   245: pop
    //   246: aload 4
    //   248: athrow
    //   249: new 775	java/lang/IllegalStateException
    //   252: dup
    //   253: ldc_w 811
    //   256: invokespecial 778	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   259: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	260	0	this	AndroidKeyStoreKeyPairGeneratorSpi
    //   18	50	1	bool1	boolean
    //   53	1	2	bool2	boolean
    //   68	91	2	i	int
    //   86	136	3	localObject1	Object
    //   110	51	4	localObject2	Object
    //   191	1	4	localObject3	Object
    //   196	51	4	localProviderException	ProviderException
    // Exception table:
    //   from	to	target	type
    //   138	169	191	finally
    //   198	223	191	finally
    //   223	226	191	finally
    //   138	169	196	java/security/ProviderException
  }
  
  public void initialize(int paramInt, SecureRandom paramSecureRandom)
  {
    paramSecureRandom = new StringBuilder();
    paramSecureRandom.append(KeyGenParameterSpec.class.getName());
    paramSecureRandom.append(" or ");
    paramSecureRandom.append(KeyPairGeneratorSpec.class.getName());
    paramSecureRandom.append(" required to initialize this KeyPairGenerator");
    throw new IllegalArgumentException(paramSecureRandom.toString());
  }
  
  public void initialize(AlgorithmParameterSpec paramAlgorithmParameterSpec, SecureRandom paramSecureRandom)
    throws InvalidAlgorithmParameterException
  {
    resetAll();
    int i = 0;
    if (paramAlgorithmParameterSpec != null)
    {
      boolean bool = false;
      try
      {
        int j = mOriginalKeymasterAlgorithm;
        if ((paramAlgorithmParameterSpec instanceof KeyGenParameterSpec))
        {
          paramAlgorithmParameterSpec = (KeyGenParameterSpec)paramAlgorithmParameterSpec;
        }
        else
        {
          if (!(paramAlgorithmParameterSpec instanceof KeyPairGeneratorSpec)) {
            break label774;
          }
          localObject = (KeyPairGeneratorSpec)paramAlgorithmParameterSpec;
        }
        try
        {
          paramAlgorithmParameterSpec = ((KeyPairGeneratorSpec)localObject).getKeyType();
          if (paramAlgorithmParameterSpec != null) {
            try
            {
              j = KeyProperties.KeyAlgorithm.toKeymasterAsymmetricKeyAlgorithm(paramAlgorithmParameterSpec);
            }
            catch (IllegalArgumentException paramAlgorithmParameterSpec)
            {
              paramSecureRandom = new java/security/InvalidAlgorithmParameterException;
              paramSecureRandom.<init>("Invalid key type in parameters", paramAlgorithmParameterSpec);
              throw paramSecureRandom;
            }
          }
          if (j != 1)
          {
            if (j == 3)
            {
              paramAlgorithmParameterSpec = new android/security/keystore/KeyGenParameterSpec$Builder;
              paramAlgorithmParameterSpec.<init>(((KeyPairGeneratorSpec)localObject).getKeystoreAlias(), 12);
              paramAlgorithmParameterSpec.setDigests(new String[] { "NONE", "SHA-1", "SHA-224", "SHA-256", "SHA-384", "SHA-512" });
            }
            else
            {
              paramAlgorithmParameterSpec = new java/security/ProviderException;
              paramSecureRandom = new java/lang/StringBuilder;
              paramSecureRandom.<init>();
              paramSecureRandom.append("Unsupported algorithm: ");
              paramSecureRandom.append(mKeymasterAlgorithm);
              paramAlgorithmParameterSpec.<init>(paramSecureRandom.toString());
              throw paramAlgorithmParameterSpec;
            }
          }
          else
          {
            paramAlgorithmParameterSpec = new android/security/keystore/KeyGenParameterSpec$Builder;
            paramAlgorithmParameterSpec.<init>(((KeyPairGeneratorSpec)localObject).getKeystoreAlias(), 15);
            paramAlgorithmParameterSpec.setDigests(new String[] { "NONE", "MD5", "SHA-1", "SHA-224", "SHA-256", "SHA-384", "SHA-512" });
            paramAlgorithmParameterSpec.setEncryptionPaddings(new String[] { "NoPadding", "PKCS1Padding", "OAEPPadding" });
            paramAlgorithmParameterSpec.setSignaturePaddings(new String[] { "PKCS1", "PSS" });
            paramAlgorithmParameterSpec.setRandomizedEncryptionRequired(false);
          }
          if (((KeyPairGeneratorSpec)localObject).getKeySize() != -1) {
            paramAlgorithmParameterSpec.setKeySize(((KeyPairGeneratorSpec)localObject).getKeySize());
          }
          if (((KeyPairGeneratorSpec)localObject).getAlgorithmParameterSpec() != null) {
            paramAlgorithmParameterSpec.setAlgorithmParameterSpec(((KeyPairGeneratorSpec)localObject).getAlgorithmParameterSpec());
          }
          paramAlgorithmParameterSpec.setCertificateSubject(((KeyPairGeneratorSpec)localObject).getSubjectDN());
          paramAlgorithmParameterSpec.setCertificateSerialNumber(((KeyPairGeneratorSpec)localObject).getSerialNumber());
          paramAlgorithmParameterSpec.setCertificateNotBefore(((KeyPairGeneratorSpec)localObject).getStartDate());
          paramAlgorithmParameterSpec.setCertificateNotAfter(((KeyPairGeneratorSpec)localObject).getEndDate());
          bool = ((KeyPairGeneratorSpec)localObject).isEncryptionRequired();
          paramAlgorithmParameterSpec.setUserAuthenticationRequired(false);
          paramAlgorithmParameterSpec = paramAlgorithmParameterSpec.build();
          mEntryAlias = paramAlgorithmParameterSpec.getKeystoreAlias();
          mEntryUid = paramAlgorithmParameterSpec.getUid();
          mSpec = paramAlgorithmParameterSpec;
          mKeymasterAlgorithm = j;
          mEncryptionAtRestRequired = bool;
          mKeySizeBits = paramAlgorithmParameterSpec.getKeySize();
          initAlgorithmSpecificParameters();
          if (mKeySizeBits == -1) {
            mKeySizeBits = getDefaultKeySize(j);
          }
          checkValidKeySize(j, mKeySizeBits);
          localObject = paramAlgorithmParameterSpec.getKeystoreAlias();
          if (localObject != null) {
            try
            {
              localObject = KeyProperties.KeyAlgorithm.fromKeymasterAsymmetricKeyAlgorithm(j);
              mKeymasterPurposes = KeyProperties.Purpose.allToKeymaster(paramAlgorithmParameterSpec.getPurposes());
              mKeymasterBlockModes = KeyProperties.BlockMode.allToKeymaster(paramAlgorithmParameterSpec.getBlockModes());
              mKeymasterEncryptionPaddings = KeyProperties.EncryptionPadding.allToKeymaster(paramAlgorithmParameterSpec.getEncryptionPaddings());
              if (((0x1 & paramAlgorithmParameterSpec.getPurposes()) != 0) && (paramAlgorithmParameterSpec.isRandomizedEncryptionRequired()))
              {
                int[] arrayOfInt = mKeymasterEncryptionPaddings;
                int k = arrayOfInt.length;
                j = i;
                while (j < k)
                {
                  i = arrayOfInt[j];
                  if (KeymasterUtils.isKeymasterPaddingSchemeIndCpaCompatibleWithAsymmetricCrypto(i))
                  {
                    j++;
                  }
                  else
                  {
                    paramSecureRandom = new java/security/InvalidAlgorithmParameterException;
                    paramAlgorithmParameterSpec = new java/lang/StringBuilder;
                    paramAlgorithmParameterSpec.<init>();
                    paramAlgorithmParameterSpec.append("Randomized encryption (IND-CPA) required but may be violated by padding scheme: ");
                    paramAlgorithmParameterSpec.append(KeyProperties.EncryptionPadding.fromKeymaster(i));
                    paramAlgorithmParameterSpec.append(". See ");
                    paramAlgorithmParameterSpec.append(KeyGenParameterSpec.class.getName());
                    paramAlgorithmParameterSpec.append(" documentation.");
                    paramSecureRandom.<init>(paramAlgorithmParameterSpec.toString());
                    throw paramSecureRandom;
                  }
                }
              }
              mKeymasterSignaturePaddings = KeyProperties.SignaturePadding.allToKeymaster(paramAlgorithmParameterSpec.getSignaturePaddings());
              if (paramAlgorithmParameterSpec.isDigestsSpecified()) {
                mKeymasterDigests = KeyProperties.Digest.allToKeymaster(paramAlgorithmParameterSpec.getDigests());
              } else {
                mKeymasterDigests = EmptyArray.INT;
              }
              paramAlgorithmParameterSpec = new android/security/keymaster/KeymasterArguments;
              paramAlgorithmParameterSpec.<init>();
              KeymasterUtils.addUserAuthArgs(paramAlgorithmParameterSpec, mSpec);
              mJcaKeyAlgorithm = ((String)localObject);
              paramAlgorithmParameterSpec = new java/security/InvalidAlgorithmParameterException;
            }
            catch (IllegalArgumentException|IllegalStateException paramSecureRandom)
            {
              try
              {
                mRng = paramSecureRandom;
                mKeyStore = KeyStore.getInstance();
                if (1 == 0) {
                  resetAll();
                }
                return;
              }
              finally
              {
                if (0 != 0) {
                  break label934;
                }
                resetAll();
              }
              paramSecureRandom = paramSecureRandom;
              paramAlgorithmParameterSpec = new java/security/InvalidAlgorithmParameterException;
              paramAlgorithmParameterSpec.<init>(paramSecureRandom);
              throw paramAlgorithmParameterSpec;
            }
          }
          paramAlgorithmParameterSpec.<init>("KeyStore entry alias not provided");
          throw paramAlgorithmParameterSpec;
        }
        catch (NullPointerException|IllegalArgumentException paramAlgorithmParameterSpec)
        {
          paramSecureRandom = new java/security/InvalidAlgorithmParameterException;
          paramSecureRandom.<init>(paramAlgorithmParameterSpec);
          throw paramSecureRandom;
        }
        label774:
        paramSecureRandom = new java/security/InvalidAlgorithmParameterException;
        Object localObject = new java/lang/StringBuilder;
        ((StringBuilder)localObject).<init>();
        ((StringBuilder)localObject).append("Unsupported params class: ");
        ((StringBuilder)localObject).append(paramAlgorithmParameterSpec.getClass().getName());
        ((StringBuilder)localObject).append(". Supported: ");
        ((StringBuilder)localObject).append(KeyGenParameterSpec.class.getName());
        ((StringBuilder)localObject).append(", ");
        ((StringBuilder)localObject).append(KeyPairGeneratorSpec.class.getName());
        paramSecureRandom.<init>(((StringBuilder)localObject).toString());
        throw paramSecureRandom;
      }
      finally
      {
        break label926;
      }
    }
    paramSecureRandom = new java/security/InvalidAlgorithmParameterException;
    paramAlgorithmParameterSpec = new java/lang/StringBuilder;
    paramAlgorithmParameterSpec.<init>();
    paramAlgorithmParameterSpec.append("Must supply params of type ");
    paramAlgorithmParameterSpec.append(KeyGenParameterSpec.class.getName());
    paramAlgorithmParameterSpec.append(" or ");
    paramAlgorithmParameterSpec.append(KeyPairGeneratorSpec.class.getName());
    paramSecureRandom.<init>(paramAlgorithmParameterSpec.toString());
    throw paramSecureRandom;
  }
  
  public static class EC
    extends AndroidKeyStoreKeyPairGeneratorSpi
  {
    public EC()
    {
      super();
    }
  }
  
  public static class RSA
    extends AndroidKeyStoreKeyPairGeneratorSpi
  {
    public RSA()
    {
      super();
    }
  }
}
