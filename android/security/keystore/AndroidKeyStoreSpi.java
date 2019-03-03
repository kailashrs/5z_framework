package android.security.keystore;

import android.security.Credentials;
import android.security.GateKeeper;
import android.security.KeyStore;
import android.security.keymaster.KeyCharacteristics;
import android.security.keymaster.KeymasterArguments;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyStore.Entry;
import java.security.KeyStore.LoadStoreParameter;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStore.ProtectionParameter;
import java.security.KeyStore.SecretKeyEntry;
import java.security.KeyStore.TrustedCertificateEntry;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.crypto.SecretKey;
import libcore.util.EmptyArray;

public class AndroidKeyStoreSpi
  extends KeyStoreSpi
{
  public static final String NAME = "AndroidKeyStore";
  private KeyStore mKeyStore;
  private int mUid = -1;
  
  public AndroidKeyStoreSpi() {}
  
  private Certificate getCertificateForPrivateKeyEntry(String paramString, byte[] paramArrayOfByte)
  {
    paramArrayOfByte = toCertificate(paramArrayOfByte);
    if (paramArrayOfByte == null) {
      return null;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("USRPKEY_");
    localStringBuilder.append(paramString);
    paramString = localStringBuilder.toString();
    if (mKeyStore.contains(paramString, mUid)) {
      return wrapIntoKeyStoreCertificate(paramString, mUid, paramArrayOfByte);
    }
    return paramArrayOfByte;
  }
  
  private Certificate getCertificateForTrustedCertificateEntry(byte[] paramArrayOfByte)
  {
    return toCertificate(paramArrayOfByte);
  }
  
  private static KeyProtection getLegacyKeyProtectionParameter(PrivateKey paramPrivateKey)
    throws KeyStoreException
  {
    String str = paramPrivateKey.getAlgorithm();
    if ("EC".equalsIgnoreCase(str))
    {
      paramPrivateKey = new KeyProtection.Builder(12);
      paramPrivateKey.setDigests(new String[] { "NONE", "SHA-1", "SHA-224", "SHA-256", "SHA-384", "SHA-512" });
    }
    else
    {
      if (!"RSA".equalsIgnoreCase(str)) {
        break label194;
      }
      paramPrivateKey = new KeyProtection.Builder(15);
      paramPrivateKey.setDigests(new String[] { "NONE", "MD5", "SHA-1", "SHA-224", "SHA-256", "SHA-384", "SHA-512" });
      paramPrivateKey.setEncryptionPaddings(new String[] { "NoPadding", "PKCS1Padding", "OAEPPadding" });
      paramPrivateKey.setSignaturePaddings(new String[] { "PKCS1", "PSS" });
      paramPrivateKey.setRandomizedEncryptionRequired(false);
    }
    paramPrivateKey.setUserAuthenticationRequired(false);
    return paramPrivateKey.build();
    label194:
    paramPrivateKey = new StringBuilder();
    paramPrivateKey.append("Unsupported key algorithm: ");
    paramPrivateKey.append(str);
    throw new KeyStoreException(paramPrivateKey.toString());
  }
  
  private Date getModificationDate(String paramString)
  {
    long l = mKeyStore.getmtime(paramString, mUid);
    if (l == -1L) {
      return null;
    }
    return new Date(l);
  }
  
  private Set<String> getUniqueAliases()
  {
    String[] arrayOfString = mKeyStore.list("", mUid);
    if (arrayOfString == null) {
      return new HashSet();
    }
    HashSet localHashSet = new HashSet(arrayOfString.length);
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++)
    {
      String str = arrayOfString[j];
      int k = str.indexOf('_');
      if ((k != -1) && (str.length() > k))
      {
        localHashSet.add(new String(str.substring(k + 1)));
      }
      else
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("invalid alias: ");
        localStringBuilder.append(str);
        Log.e("AndroidKeyStore", localStringBuilder.toString());
      }
    }
    return localHashSet;
  }
  
  private boolean isCertificateEntry(String paramString)
  {
    if (paramString != null)
    {
      KeyStore localKeyStore = mKeyStore;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("CACERT_");
      localStringBuilder.append(paramString);
      return localKeyStore.contains(localStringBuilder.toString(), mUid);
    }
    throw new NullPointerException("alias == null");
  }
  
  private boolean isKeyEntry(String paramString)
  {
    KeyStore localKeyStore = mKeyStore;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("USRPKEY_");
    localStringBuilder.append(paramString);
    if (!localKeyStore.contains(localStringBuilder.toString(), mUid))
    {
      localKeyStore = mKeyStore;
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("USRSKEY_");
      localStringBuilder.append(paramString);
      if (!localKeyStore.contains(localStringBuilder.toString(), mUid))
      {
        bool = false;
        break label94;
      }
    }
    boolean bool = true;
    label94:
    return bool;
  }
  
  /* Error */
  private void setPrivateKeyEntry(String paramString, PrivateKey paramPrivateKey, Certificate[] paramArrayOfCertificate, KeyStore.ProtectionParameter paramProtectionParameter)
    throws KeyStoreException
  {
    // Byte code:
    //   0: iconst_0
    //   1: istore 5
    //   3: iconst_0
    //   4: istore 6
    //   6: iconst_0
    //   7: istore 7
    //   9: aload 4
    //   11: ifnonnull +12 -> 23
    //   14: aload_2
    //   15: invokestatic 205	android/security/keystore/AndroidKeyStoreSpi:getLegacyKeyProtectionParameter	(Ljava/security/PrivateKey;)Landroid/security/keystore/KeyProtection;
    //   18: astore 4
    //   20: goto +105 -> 125
    //   23: aload 4
    //   25: instanceof 207
    //   28: ifeq +34 -> 62
    //   31: aload_2
    //   32: invokestatic 205	android/security/keystore/AndroidKeyStoreSpi:getLegacyKeyProtectionParameter	(Ljava/security/PrivateKey;)Landroid/security/keystore/KeyProtection;
    //   35: astore 8
    //   37: iload 5
    //   39: istore 7
    //   41: aload 4
    //   43: checkcast 207	android/security/KeyStoreParameter
    //   46: invokevirtual 211	android/security/KeyStoreParameter:isEncryptionRequired	()Z
    //   49: ifeq +6 -> 55
    //   52: iconst_1
    //   53: istore 7
    //   55: aload 8
    //   57: astore 4
    //   59: goto -39 -> 20
    //   62: aload 4
    //   64: instanceof 213
    //   67: ifeq +1321 -> 1388
    //   70: aload 4
    //   72: checkcast 213	android/security/keystore/KeyProtection
    //   75: astore 8
    //   77: iload 6
    //   79: istore 5
    //   81: aload 8
    //   83: invokevirtual 216	android/security/keystore/KeyProtection:isCriticalToDeviceEncryption	()Z
    //   86: ifeq +9 -> 95
    //   89: iconst_0
    //   90: bipush 8
    //   92: ior
    //   93: istore 5
    //   95: iload 5
    //   97: istore 7
    //   99: aload 8
    //   101: astore 4
    //   103: aload 8
    //   105: invokevirtual 219	android/security/keystore/KeyProtection:isStrongBoxBacked	()Z
    //   108: ifeq -88 -> 20
    //   111: iload 5
    //   113: bipush 16
    //   115: ior
    //   116: istore 7
    //   118: aload 8
    //   120: astore 4
    //   122: goto -102 -> 20
    //   125: aload_3
    //   126: ifnull +1251 -> 1377
    //   129: aload_3
    //   130: arraylength
    //   131: ifeq +1246 -> 1377
    //   134: aload_3
    //   135: arraylength
    //   136: anewarray 221	java/security/cert/X509Certificate
    //   139: astore 8
    //   141: iconst_0
    //   142: istore 5
    //   144: iload 5
    //   146: aload_3
    //   147: arraylength
    //   148: if_icmpge +114 -> 262
    //   151: ldc -33
    //   153: aload_3
    //   154: iload 5
    //   156: aaload
    //   157: invokevirtual 228	java/security/cert/Certificate:getType	()Ljava/lang/String;
    //   160: invokevirtual 231	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   163: ifeq +65 -> 228
    //   166: aload_3
    //   167: iload 5
    //   169: aaload
    //   170: instanceof 221
    //   173: ifeq +21 -> 194
    //   176: aload 8
    //   178: iload 5
    //   180: aload_3
    //   181: iload 5
    //   183: aaload
    //   184: checkcast 221	java/security/cert/X509Certificate
    //   187: aastore
    //   188: iinc 5 1
    //   191: goto -47 -> 144
    //   194: new 30	java/lang/StringBuilder
    //   197: dup
    //   198: invokespecial 31	java/lang/StringBuilder:<init>	()V
    //   201: astore_1
    //   202: aload_1
    //   203: ldc -23
    //   205: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   208: pop
    //   209: aload_1
    //   210: iload 5
    //   212: invokevirtual 236	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   215: pop
    //   216: new 59	java/security/KeyStoreException
    //   219: dup
    //   220: aload_1
    //   221: invokevirtual 41	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   224: invokespecial 129	java/security/KeyStoreException:<init>	(Ljava/lang/String;)V
    //   227: athrow
    //   228: new 30	java/lang/StringBuilder
    //   231: dup
    //   232: invokespecial 31	java/lang/StringBuilder:<init>	()V
    //   235: astore_1
    //   236: aload_1
    //   237: ldc -23
    //   239: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   242: pop
    //   243: aload_1
    //   244: iload 5
    //   246: invokevirtual 236	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   249: pop
    //   250: new 59	java/security/KeyStoreException
    //   253: dup
    //   254: aload_1
    //   255: invokevirtual 41	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   258: invokespecial 129	java/security/KeyStoreException:<init>	(Ljava/lang/String;)V
    //   261: athrow
    //   262: aload 8
    //   264: iconst_0
    //   265: aaload
    //   266: invokevirtual 240	java/security/cert/X509Certificate:getEncoded	()[B
    //   269: astore 9
    //   271: aload_3
    //   272: arraylength
    //   273: iconst_1
    //   274: if_icmple +156 -> 430
    //   277: aload 8
    //   279: arraylength
    //   280: iconst_1
    //   281: isub
    //   282: anewarray 242	[B
    //   285: astore_3
    //   286: iconst_0
    //   287: istore 6
    //   289: iconst_0
    //   290: istore 5
    //   292: iload 5
    //   294: aload_3
    //   295: arraylength
    //   296: if_icmpge +73 -> 369
    //   299: aload_3
    //   300: iload 5
    //   302: aload 8
    //   304: iload 5
    //   306: iconst_1
    //   307: iadd
    //   308: aaload
    //   309: invokevirtual 240	java/security/cert/X509Certificate:getEncoded	()[B
    //   312: aastore
    //   313: aload_3
    //   314: iload 5
    //   316: aaload
    //   317: arraylength
    //   318: istore 10
    //   320: iload 6
    //   322: iload 10
    //   324: iadd
    //   325: istore 6
    //   327: iinc 5 1
    //   330: goto -38 -> 292
    //   333: astore_2
    //   334: new 30	java/lang/StringBuilder
    //   337: dup
    //   338: invokespecial 31	java/lang/StringBuilder:<init>	()V
    //   341: astore_1
    //   342: aload_1
    //   343: ldc -12
    //   345: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   348: pop
    //   349: aload_1
    //   350: iload 5
    //   352: invokevirtual 236	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   355: pop
    //   356: new 59	java/security/KeyStoreException
    //   359: dup
    //   360: aload_1
    //   361: invokevirtual 41	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   364: aload_2
    //   365: invokespecial 247	java/security/KeyStoreException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   368: athrow
    //   369: iload 6
    //   371: newarray byte
    //   373: astore 8
    //   375: iconst_0
    //   376: istore 6
    //   378: iconst_0
    //   379: istore 5
    //   381: iload 5
    //   383: aload_3
    //   384: arraylength
    //   385: if_icmpge +42 -> 427
    //   388: aload_3
    //   389: iload 5
    //   391: aaload
    //   392: arraylength
    //   393: istore 10
    //   395: aload_3
    //   396: iload 5
    //   398: aaload
    //   399: iconst_0
    //   400: aload 8
    //   402: iload 6
    //   404: iload 10
    //   406: invokestatic 253	java/lang/System:arraycopy	([BI[BII)V
    //   409: iload 6
    //   411: iload 10
    //   413: iadd
    //   414: istore 6
    //   416: aload_3
    //   417: iload 5
    //   419: aconst_null
    //   420: aastore
    //   421: iinc 5 1
    //   424: goto -43 -> 381
    //   427: goto +6 -> 433
    //   430: aconst_null
    //   431: astore 8
    //   433: aconst_null
    //   434: astore_3
    //   435: aload_2
    //   436: instanceof 255
    //   439: ifeq +11 -> 450
    //   442: aload_2
    //   443: checkcast 257	android/security/keystore/AndroidKeyStoreKey
    //   446: invokevirtual 260	android/security/keystore/AndroidKeyStoreKey:getAlias	()Ljava/lang/String;
    //   449: astore_3
    //   450: aload_3
    //   451: ifnull +88 -> 539
    //   454: aload_3
    //   455: ldc 33
    //   457: invokevirtual 263	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   460: ifeq +79 -> 539
    //   463: aload_3
    //   464: ldc 33
    //   466: invokevirtual 163	java/lang/String:length	()I
    //   469: invokevirtual 167	java/lang/String:substring	(I)Ljava/lang/String;
    //   472: astore_2
    //   473: aload_1
    //   474: aload_2
    //   475: invokevirtual 231	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   478: ifeq +13 -> 491
    //   481: aconst_null
    //   482: astore_3
    //   483: iconst_0
    //   484: istore 5
    //   486: aconst_null
    //   487: astore_2
    //   488: goto +394 -> 882
    //   491: new 30	java/lang/StringBuilder
    //   494: dup
    //   495: invokespecial 31	java/lang/StringBuilder:<init>	()V
    //   498: astore_3
    //   499: aload_3
    //   500: ldc_w 265
    //   503: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   506: pop
    //   507: aload_3
    //   508: aload_1
    //   509: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   512: pop
    //   513: aload_3
    //   514: ldc_w 267
    //   517: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   520: pop
    //   521: aload_3
    //   522: aload_2
    //   523: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   526: pop
    //   527: new 59	java/security/KeyStoreException
    //   530: dup
    //   531: aload_3
    //   532: invokevirtual 41	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   535: invokespecial 129	java/security/KeyStoreException:<init>	(Ljava/lang/String;)V
    //   538: athrow
    //   539: aload_2
    //   540: invokeinterface 270 1 0
    //   545: astore 11
    //   547: aload 11
    //   549: ifnull +772 -> 1321
    //   552: ldc_w 272
    //   555: aload 11
    //   557: invokevirtual 231	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   560: ifeq +761 -> 1321
    //   563: aload_2
    //   564: invokeinterface 273 1 0
    //   569: astore 12
    //   571: aload 12
    //   573: ifnull +737 -> 1310
    //   576: new 275	android/security/keymaster/KeymasterArguments
    //   579: dup
    //   580: invokespecial 276	android/security/keymaster/KeymasterArguments:<init>	()V
    //   583: astore 11
    //   585: aload 11
    //   587: ldc_w 277
    //   590: aload_2
    //   591: invokeinterface 64 1 0
    //   596: invokestatic 283	android/security/keystore/KeyProperties$KeyAlgorithm:toKeymasterAsymmetricKeyAlgorithm	(Ljava/lang/String;)I
    //   599: invokevirtual 287	android/security/keymaster/KeymasterArguments:addEnum	(II)V
    //   602: aload 4
    //   604: invokevirtual 290	android/security/keystore/KeyProtection:getPurposes	()I
    //   607: istore 5
    //   609: aload 11
    //   611: ldc_w 291
    //   614: iload 5
    //   616: invokestatic 297	android/security/keystore/KeyProperties$Purpose:allToKeymaster	(I)[I
    //   619: invokevirtual 301	android/security/keymaster/KeymasterArguments:addEnums	(I[I)V
    //   622: aload 4
    //   624: invokevirtual 304	android/security/keystore/KeyProtection:isDigestsSpecified	()Z
    //   627: istore 13
    //   629: iload 13
    //   631: ifeq +26 -> 657
    //   634: aload 11
    //   636: ldc_w 305
    //   639: aload 4
    //   641: invokevirtual 309	android/security/keystore/KeyProtection:getDigests	()[Ljava/lang/String;
    //   644: invokestatic 314	android/security/keystore/KeyProperties$Digest:allToKeymaster	([Ljava/lang/String;)[I
    //   647: invokevirtual 301	android/security/keymaster/KeymasterArguments:addEnums	(I[I)V
    //   650: goto +7 -> 657
    //   653: astore_1
    //   654: goto +647 -> 1301
    //   657: aload 11
    //   659: ldc_w 315
    //   662: aload 4
    //   664: invokevirtual 318	android/security/keystore/KeyProtection:getBlockModes	()[Ljava/lang/String;
    //   667: invokestatic 321	android/security/keystore/KeyProperties$BlockMode:allToKeymaster	([Ljava/lang/String;)[I
    //   670: invokevirtual 301	android/security/keymaster/KeymasterArguments:addEnums	(I[I)V
    //   673: aload 4
    //   675: invokevirtual 324	android/security/keystore/KeyProtection:getEncryptionPaddings	()[Ljava/lang/String;
    //   678: invokestatic 327	android/security/keystore/KeyProperties$EncryptionPadding:allToKeymaster	([Ljava/lang/String;)[I
    //   681: astore 14
    //   683: iload 5
    //   685: iconst_1
    //   686: iand
    //   687: ifeq +114 -> 801
    //   690: aload_3
    //   691: astore_2
    //   692: aload 4
    //   694: invokevirtual 330	android/security/keystore/KeyProtection:isRandomizedEncryptionRequired	()Z
    //   697: ifeq +104 -> 801
    //   700: aload_3
    //   701: astore_2
    //   702: aload 14
    //   704: arraylength
    //   705: istore 6
    //   707: iconst_0
    //   708: istore 5
    //   710: iload 5
    //   712: iload 6
    //   714: if_icmpge +80 -> 794
    //   717: aload 14
    //   719: iload 5
    //   721: iaload
    //   722: istore 10
    //   724: iload 10
    //   726: invokestatic 336	android/security/keystore/KeymasterUtils:isKeymasterPaddingSchemeIndCpaCompatibleWithAsymmetricCrypto	(I)Z
    //   729: ifeq +9 -> 738
    //   732: iinc 5 1
    //   735: goto -25 -> 710
    //   738: new 59	java/security/KeyStoreException
    //   741: astore_2
    //   742: new 30	java/lang/StringBuilder
    //   745: astore_1
    //   746: aload_1
    //   747: invokespecial 31	java/lang/StringBuilder:<init>	()V
    //   750: aload_1
    //   751: ldc_w 338
    //   754: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   757: pop
    //   758: aload_1
    //   759: iload 10
    //   761: invokestatic 341	android/security/keystore/KeyProperties$EncryptionPadding:fromKeymaster	(I)Ljava/lang/String;
    //   764: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   767: pop
    //   768: aload_1
    //   769: ldc_w 343
    //   772: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   775: pop
    //   776: aload_2
    //   777: aload_1
    //   778: invokevirtual 41	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   781: invokespecial 129	java/security/KeyStoreException:<init>	(Ljava/lang/String;)V
    //   784: aload_2
    //   785: athrow
    //   786: astore_1
    //   787: goto -133 -> 654
    //   790: astore_1
    //   791: goto +510 -> 1301
    //   794: goto +7 -> 801
    //   797: astore_1
    //   798: goto +503 -> 1301
    //   801: iconst_1
    //   802: istore 5
    //   804: aload 11
    //   806: ldc_w 344
    //   809: aload 14
    //   811: invokevirtual 301	android/security/keymaster/KeymasterArguments:addEnums	(I[I)V
    //   814: aload 11
    //   816: ldc_w 344
    //   819: aload 4
    //   821: invokevirtual 347	android/security/keystore/KeyProtection:getSignaturePaddings	()[Ljava/lang/String;
    //   824: invokestatic 350	android/security/keystore/KeyProperties$SignaturePadding:allToKeymaster	([Ljava/lang/String;)[I
    //   827: invokevirtual 301	android/security/keymaster/KeymasterArguments:addEnums	(I[I)V
    //   830: aload 11
    //   832: aload 4
    //   834: invokestatic 354	android/security/keystore/KeymasterUtils:addUserAuthArgs	(Landroid/security/keymaster/KeymasterArguments;Landroid/security/keystore/UserAuthArgs;)V
    //   837: aload 11
    //   839: ldc_w 355
    //   842: aload 4
    //   844: invokevirtual 359	android/security/keystore/KeyProtection:getKeyValidityStart	()Ljava/util/Date;
    //   847: invokevirtual 363	android/security/keymaster/KeymasterArguments:addDateIfNotNull	(ILjava/util/Date;)V
    //   850: aload 11
    //   852: ldc_w 364
    //   855: aload 4
    //   857: invokevirtual 367	android/security/keystore/KeyProtection:getKeyValidityForOriginationEnd	()Ljava/util/Date;
    //   860: invokevirtual 363	android/security/keymaster/KeymasterArguments:addDateIfNotNull	(ILjava/util/Date;)V
    //   863: aload 11
    //   865: ldc_w 368
    //   868: aload 4
    //   870: invokevirtual 371	android/security/keystore/KeyProtection:getKeyValidityForConsumptionEnd	()Ljava/util/Date;
    //   873: invokevirtual 363	android/security/keymaster/KeymasterArguments:addDateIfNotNull	(ILjava/util/Date;)V
    //   876: aload 12
    //   878: astore_3
    //   879: aload 11
    //   881: astore_2
    //   882: iload 5
    //   884: ifeq +123 -> 1007
    //   887: aload_0
    //   888: getfield 43	android/security/keystore/AndroidKeyStoreSpi:mKeyStore	Landroid/security/KeyStore;
    //   891: aload_1
    //   892: aload_0
    //   893: getfield 21	android/security/keystore/AndroidKeyStoreSpi:mUid	I
    //   896: invokestatic 377	android/security/Credentials:deleteAllTypesForAlias	(Landroid/security/KeyStore;Ljava/lang/String;I)Z
    //   899: pop
    //   900: new 379	android/security/keymaster/KeyCharacteristics
    //   903: astore 4
    //   905: aload 4
    //   907: invokespecial 380	android/security/keymaster/KeyCharacteristics:<init>	()V
    //   910: aload_0
    //   911: getfield 43	android/security/keystore/AndroidKeyStoreSpi:mKeyStore	Landroid/security/KeyStore;
    //   914: astore 11
    //   916: new 30	java/lang/StringBuilder
    //   919: astore 12
    //   921: aload 12
    //   923: invokespecial 31	java/lang/StringBuilder:<init>	()V
    //   926: aload 12
    //   928: ldc 33
    //   930: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   933: pop
    //   934: aload 12
    //   936: aload_1
    //   937: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   940: pop
    //   941: aload 12
    //   943: invokevirtual 41	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   946: astore 12
    //   948: aload_0
    //   949: getfield 21	android/security/keystore/AndroidKeyStoreSpi:mUid	I
    //   952: istore 6
    //   954: aload 11
    //   956: aload 12
    //   958: aload_2
    //   959: iconst_1
    //   960: aload_3
    //   961: iload 6
    //   963: iload 7
    //   965: aload 4
    //   967: invokevirtual 384	android/security/KeyStore:importKey	(Ljava/lang/String;Landroid/security/keymaster/KeymasterArguments;I[BIILandroid/security/keymaster/KeyCharacteristics;)I
    //   970: istore 6
    //   972: iload 6
    //   974: iconst_1
    //   975: if_icmpne +6 -> 981
    //   978: goto +55 -> 1033
    //   981: new 59	java/security/KeyStoreException
    //   984: astore_2
    //   985: aload_2
    //   986: ldc_w 386
    //   989: iload 6
    //   991: invokestatic 390	android/security/KeyStore:getKeyStoreException	(I)Landroid/security/KeyStoreException;
    //   994: invokespecial 247	java/security/KeyStoreException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   997: aload_2
    //   998: athrow
    //   999: astore_2
    //   1000: goto +243 -> 1243
    //   1003: astore_2
    //   1004: goto +239 -> 1243
    //   1007: aload_0
    //   1008: getfield 43	android/security/keystore/AndroidKeyStoreSpi:mKeyStore	Landroid/security/KeyStore;
    //   1011: aload_1
    //   1012: aload_0
    //   1013: getfield 21	android/security/keystore/AndroidKeyStoreSpi:mUid	I
    //   1016: invokestatic 393	android/security/Credentials:deleteCertificateTypesForAlias	(Landroid/security/KeyStore;Ljava/lang/String;I)Z
    //   1019: pop
    //   1020: aload_0
    //   1021: getfield 43	android/security/keystore/AndroidKeyStoreSpi:mKeyStore	Landroid/security/KeyStore;
    //   1024: aload_1
    //   1025: aload_0
    //   1026: getfield 21	android/security/keystore/AndroidKeyStoreSpi:mUid	I
    //   1029: invokestatic 396	android/security/Credentials:deleteLegacyKeyForAlias	(Landroid/security/KeyStore;Ljava/lang/String;I)Z
    //   1032: pop
    //   1033: aload_0
    //   1034: getfield 43	android/security/keystore/AndroidKeyStoreSpi:mKeyStore	Landroid/security/KeyStore;
    //   1037: astore_2
    //   1038: new 30	java/lang/StringBuilder
    //   1041: astore_3
    //   1042: aload_3
    //   1043: invokespecial 31	java/lang/StringBuilder:<init>	()V
    //   1046: aload_3
    //   1047: ldc_w 398
    //   1050: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1053: pop
    //   1054: aload_3
    //   1055: aload_1
    //   1056: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1059: pop
    //   1060: aload_3
    //   1061: invokevirtual 41	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1064: astore_3
    //   1065: aload_0
    //   1066: getfield 21	android/security/keystore/AndroidKeyStoreSpi:mUid	I
    //   1069: istore 6
    //   1071: aload_2
    //   1072: aload_3
    //   1073: aload 9
    //   1075: iload 6
    //   1077: iload 7
    //   1079: invokevirtual 402	android/security/KeyStore:insert	(Ljava/lang/String;[BII)I
    //   1082: istore 6
    //   1084: iload 6
    //   1086: iconst_1
    //   1087: if_icmpne +129 -> 1216
    //   1090: aload_0
    //   1091: getfield 43	android/security/keystore/AndroidKeyStoreSpi:mKeyStore	Landroid/security/KeyStore;
    //   1094: astore_2
    //   1095: new 30	java/lang/StringBuilder
    //   1098: astore_3
    //   1099: aload_3
    //   1100: invokespecial 31	java/lang/StringBuilder:<init>	()V
    //   1103: aload_3
    //   1104: ldc -69
    //   1106: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1109: pop
    //   1110: aload_3
    //   1111: aload_1
    //   1112: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1115: pop
    //   1116: aload_3
    //   1117: invokevirtual 41	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1120: astore_3
    //   1121: aload_0
    //   1122: getfield 21	android/security/keystore/AndroidKeyStoreSpi:mUid	I
    //   1125: istore 6
    //   1127: aload_2
    //   1128: aload_3
    //   1129: aload 8
    //   1131: iload 6
    //   1133: iload 7
    //   1135: invokevirtual 402	android/security/KeyStore:insert	(Ljava/lang/String;[BII)I
    //   1138: istore 7
    //   1140: iload 7
    //   1142: iconst_1
    //   1143: if_icmpne +55 -> 1198
    //   1146: iconst_1
    //   1147: ifne +50 -> 1197
    //   1150: iload 5
    //   1152: ifeq +19 -> 1171
    //   1155: aload_0
    //   1156: getfield 43	android/security/keystore/AndroidKeyStoreSpi:mKeyStore	Landroid/security/KeyStore;
    //   1159: aload_1
    //   1160: aload_0
    //   1161: getfield 21	android/security/keystore/AndroidKeyStoreSpi:mUid	I
    //   1164: invokestatic 377	android/security/Credentials:deleteAllTypesForAlias	(Landroid/security/KeyStore;Ljava/lang/String;I)Z
    //   1167: pop
    //   1168: goto +29 -> 1197
    //   1171: aload_0
    //   1172: getfield 43	android/security/keystore/AndroidKeyStoreSpi:mKeyStore	Landroid/security/KeyStore;
    //   1175: aload_1
    //   1176: aload_0
    //   1177: getfield 21	android/security/keystore/AndroidKeyStoreSpi:mUid	I
    //   1180: invokestatic 393	android/security/Credentials:deleteCertificateTypesForAlias	(Landroid/security/KeyStore;Ljava/lang/String;I)Z
    //   1183: pop
    //   1184: aload_0
    //   1185: getfield 43	android/security/keystore/AndroidKeyStoreSpi:mKeyStore	Landroid/security/KeyStore;
    //   1188: aload_1
    //   1189: aload_0
    //   1190: getfield 21	android/security/keystore/AndroidKeyStoreSpi:mUid	I
    //   1193: invokestatic 396	android/security/Credentials:deleteLegacyKeyForAlias	(Landroid/security/KeyStore;Ljava/lang/String;I)Z
    //   1196: pop
    //   1197: return
    //   1198: new 59	java/security/KeyStoreException
    //   1201: astore_2
    //   1202: aload_2
    //   1203: ldc_w 404
    //   1206: iload 7
    //   1208: invokestatic 390	android/security/KeyStore:getKeyStoreException	(I)Landroid/security/KeyStoreException;
    //   1211: invokespecial 247	java/security/KeyStoreException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   1214: aload_2
    //   1215: athrow
    //   1216: new 59	java/security/KeyStoreException
    //   1219: astore_2
    //   1220: aload_2
    //   1221: ldc_w 406
    //   1224: iload 6
    //   1226: invokestatic 390	android/security/KeyStore:getKeyStoreException	(I)Landroid/security/KeyStoreException;
    //   1229: invokespecial 247	java/security/KeyStoreException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   1232: aload_2
    //   1233: athrow
    //   1234: astore_2
    //   1235: goto +8 -> 1243
    //   1238: astore_2
    //   1239: goto +4 -> 1243
    //   1242: astore_2
    //   1243: iconst_0
    //   1244: ifne +50 -> 1294
    //   1247: iload 5
    //   1249: ifeq +19 -> 1268
    //   1252: aload_0
    //   1253: getfield 43	android/security/keystore/AndroidKeyStoreSpi:mKeyStore	Landroid/security/KeyStore;
    //   1256: aload_1
    //   1257: aload_0
    //   1258: getfield 21	android/security/keystore/AndroidKeyStoreSpi:mUid	I
    //   1261: invokestatic 377	android/security/Credentials:deleteAllTypesForAlias	(Landroid/security/KeyStore;Ljava/lang/String;I)Z
    //   1264: pop
    //   1265: goto +29 -> 1294
    //   1268: aload_0
    //   1269: getfield 43	android/security/keystore/AndroidKeyStoreSpi:mKeyStore	Landroid/security/KeyStore;
    //   1272: aload_1
    //   1273: aload_0
    //   1274: getfield 21	android/security/keystore/AndroidKeyStoreSpi:mUid	I
    //   1277: invokestatic 393	android/security/Credentials:deleteCertificateTypesForAlias	(Landroid/security/KeyStore;Ljava/lang/String;I)Z
    //   1280: pop
    //   1281: aload_0
    //   1282: getfield 43	android/security/keystore/AndroidKeyStoreSpi:mKeyStore	Landroid/security/KeyStore;
    //   1285: aload_1
    //   1286: aload_0
    //   1287: getfield 21	android/security/keystore/AndroidKeyStoreSpi:mUid	I
    //   1290: invokestatic 396	android/security/Credentials:deleteLegacyKeyForAlias	(Landroid/security/KeyStore;Ljava/lang/String;I)Z
    //   1293: pop
    //   1294: aload_2
    //   1295: athrow
    //   1296: astore_1
    //   1297: goto +4 -> 1301
    //   1300: astore_1
    //   1301: new 59	java/security/KeyStoreException
    //   1304: dup
    //   1305: aload_1
    //   1306: invokespecial 409	java/security/KeyStoreException:<init>	(Ljava/lang/Throwable;)V
    //   1309: athrow
    //   1310: new 59	java/security/KeyStoreException
    //   1313: dup
    //   1314: ldc_w 411
    //   1317: invokespecial 129	java/security/KeyStoreException:<init>	(Ljava/lang/String;)V
    //   1320: athrow
    //   1321: new 30	java/lang/StringBuilder
    //   1324: dup
    //   1325: invokespecial 31	java/lang/StringBuilder:<init>	()V
    //   1328: astore_1
    //   1329: aload_1
    //   1330: ldc_w 413
    //   1333: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1336: pop
    //   1337: aload_1
    //   1338: aload 11
    //   1340: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1343: pop
    //   1344: aload_1
    //   1345: ldc_w 415
    //   1348: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1351: pop
    //   1352: new 59	java/security/KeyStoreException
    //   1355: dup
    //   1356: aload_1
    //   1357: invokevirtual 41	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1360: invokespecial 129	java/security/KeyStoreException:<init>	(Ljava/lang/String;)V
    //   1363: athrow
    //   1364: astore_1
    //   1365: new 59	java/security/KeyStoreException
    //   1368: dup
    //   1369: ldc_w 417
    //   1372: aload_1
    //   1373: invokespecial 247	java/security/KeyStoreException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   1376: athrow
    //   1377: new 59	java/security/KeyStoreException
    //   1380: dup
    //   1381: ldc_w 419
    //   1384: invokespecial 129	java/security/KeyStoreException:<init>	(Ljava/lang/String;)V
    //   1387: athrow
    //   1388: new 30	java/lang/StringBuilder
    //   1391: dup
    //   1392: invokespecial 31	java/lang/StringBuilder:<init>	()V
    //   1395: astore_1
    //   1396: aload_1
    //   1397: ldc_w 421
    //   1400: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1403: pop
    //   1404: aload_1
    //   1405: aload 4
    //   1407: invokevirtual 427	java/lang/Object:getClass	()Ljava/lang/Class;
    //   1410: invokevirtual 432	java/lang/Class:getName	()Ljava/lang/String;
    //   1413: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1416: pop
    //   1417: aload_1
    //   1418: ldc_w 434
    //   1421: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1424: pop
    //   1425: aload_1
    //   1426: ldc -43
    //   1428: invokevirtual 432	java/lang/Class:getName	()Ljava/lang/String;
    //   1431: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1434: pop
    //   1435: aload_1
    //   1436: ldc_w 436
    //   1439: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1442: pop
    //   1443: aload_1
    //   1444: ldc -49
    //   1446: invokevirtual 432	java/lang/Class:getName	()Ljava/lang/String;
    //   1449: invokevirtual 37	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1452: pop
    //   1453: new 59	java/security/KeyStoreException
    //   1456: dup
    //   1457: aload_1
    //   1458: invokevirtual 41	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1461: invokespecial 129	java/security/KeyStoreException:<init>	(Ljava/lang/String;)V
    //   1464: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	1465	0	this	AndroidKeyStoreSpi
    //   0	1465	1	paramString	String
    //   0	1465	2	paramPrivateKey	PrivateKey
    //   0	1465	3	paramArrayOfCertificate	Certificate[]
    //   0	1465	4	paramProtectionParameter	KeyStore.ProtectionParameter
    //   1	1247	5	i	int
    //   4	1221	6	j	int
    //   7	1200	7	k	int
    //   35	1095	8	localObject1	Object
    //   269	805	9	arrayOfByte	byte[]
    //   318	442	10	m	int
    //   545	794	11	localObject2	Object
    //   569	388	12	localObject3	Object
    //   627	3	13	bool	boolean
    //   681	129	14	arrayOfInt	int[]
    // Exception table:
    //   from	to	target	type
    //   299	320	333	java/security/cert/CertificateEncodingException
    //   634	650	653	java/lang/IllegalArgumentException
    //   634	650	653	java/lang/IllegalStateException
    //   750	786	786	java/lang/IllegalArgumentException
    //   750	786	786	java/lang/IllegalStateException
    //   724	732	790	java/lang/IllegalArgumentException
    //   724	732	790	java/lang/IllegalStateException
    //   738	750	790	java/lang/IllegalArgumentException
    //   738	750	790	java/lang/IllegalStateException
    //   692	700	797	java/lang/IllegalArgumentException
    //   692	700	797	java/lang/IllegalStateException
    //   702	707	797	java/lang/IllegalArgumentException
    //   702	707	797	java/lang/IllegalStateException
    //   954	972	999	finally
    //   981	999	999	finally
    //   887	954	1003	finally
    //   1127	1140	1234	finally
    //   1198	1216	1234	finally
    //   1216	1234	1234	finally
    //   1071	1084	1238	finally
    //   1090	1127	1238	finally
    //   1007	1033	1242	finally
    //   1033	1071	1242	finally
    //   804	814	1296	java/lang/IllegalArgumentException
    //   804	814	1296	java/lang/IllegalStateException
    //   814	876	1296	java/lang/IllegalArgumentException
    //   814	876	1296	java/lang/IllegalStateException
    //   585	609	1300	java/lang/IllegalArgumentException
    //   585	609	1300	java/lang/IllegalStateException
    //   609	629	1300	java/lang/IllegalArgumentException
    //   609	629	1300	java/lang/IllegalStateException
    //   657	673	1300	java/lang/IllegalArgumentException
    //   657	673	1300	java/lang/IllegalStateException
    //   673	683	1300	java/lang/IllegalArgumentException
    //   673	683	1300	java/lang/IllegalStateException
    //   262	271	1364	java/security/cert/CertificateEncodingException
  }
  
  private void setSecretKeyEntry(String paramString, SecretKey paramSecretKey, KeyStore.ProtectionParameter paramProtectionParameter)
    throws KeyStoreException
  {
    if ((paramProtectionParameter != null) && (!(paramProtectionParameter instanceof KeyProtection)))
    {
      paramString = new StringBuilder();
      paramString.append("Unsupported protection parameter class: ");
      paramString.append(paramProtectionParameter.getClass().getName());
      paramString.append(". Supported: ");
      paramString.append(KeyProtection.class.getName());
      throw new KeyStoreException(paramString.toString());
    }
    KeyProtection localKeyProtection = (KeyProtection)paramProtectionParameter;
    if ((paramSecretKey instanceof AndroidKeyStoreSecretKey))
    {
      paramProtectionParameter = ((AndroidKeyStoreSecretKey)paramSecretKey).getAlias();
      if (paramProtectionParameter != null)
      {
        paramSecretKey = "USRPKEY_";
        if (!paramProtectionParameter.startsWith("USRPKEY_"))
        {
          paramSecretKey = "USRSKEY_";
          if (!paramProtectionParameter.startsWith("USRSKEY_"))
          {
            paramString = new StringBuilder();
            paramString.append("KeyStore-backed secret key has invalid alias: ");
            paramString.append(paramProtectionParameter);
            throw new KeyStoreException(paramString.toString());
          }
        }
        paramSecretKey = paramProtectionParameter.substring(paramSecretKey.length());
        if (paramString.equals(paramSecretKey))
        {
          if (localKeyProtection == null) {
            return;
          }
          throw new KeyStoreException("Modifying KeyStore-backed key using protection parameters not supported");
        }
        paramProtectionParameter = new StringBuilder();
        paramProtectionParameter.append("Can only replace KeyStore-backed keys with same alias: ");
        paramProtectionParameter.append(paramString);
        paramProtectionParameter.append(" != ");
        paramProtectionParameter.append(paramSecretKey);
        throw new KeyStoreException(paramProtectionParameter.toString());
      }
      throw new KeyStoreException("KeyStore-backed secret key does not have an alias");
    }
    if (localKeyProtection != null)
    {
      paramProtectionParameter = paramSecretKey.getFormat();
      if (paramProtectionParameter != null)
      {
        if ("RAW".equals(paramProtectionParameter))
        {
          byte[] arrayOfByte = paramSecretKey.getEncoded();
          if (arrayOfByte != null)
          {
            KeymasterArguments localKeymasterArguments = new KeymasterArguments();
            try
            {
              int i = KeyProperties.KeyAlgorithm.toKeymasterSecretKeyAlgorithm(paramSecretKey.getAlgorithm());
              localKeymasterArguments.addEnum(268435458, i);
              int j = 0;
              if (i == 128) {
                try
                {
                  k = KeyProperties.KeyAlgorithm.toKeymasterDigest(paramSecretKey.getAlgorithm());
                  if (k != -1)
                  {
                    paramProtectionParameter = new int[1];
                    paramProtectionParameter[0] = k;
                    if (localKeyProtection.isDigestsSpecified())
                    {
                      int[] arrayOfInt = KeyProperties.Digest.allToKeymaster(localKeyProtection.getDigests());
                      if ((arrayOfInt.length != 1) || (arrayOfInt[0] != k))
                      {
                        paramProtectionParameter = new java/security/KeyStoreException;
                        paramString = new java/lang/StringBuilder;
                        paramString.<init>();
                        paramString.append("Unsupported digests specification: ");
                        paramString.append(Arrays.asList(localKeyProtection.getDigests()));
                        paramString.append(". Only ");
                        paramString.append(KeyProperties.Digest.fromKeymaster(k));
                        paramString.append(" supported for HMAC key algorithm ");
                        paramString.append(paramSecretKey.getAlgorithm());
                        paramProtectionParameter.<init>(paramString.toString());
                        throw paramProtectionParameter;
                      }
                    }
                    paramSecretKey = paramProtectionParameter;
                    break label552;
                  }
                  paramString = new java/security/ProviderException;
                  paramProtectionParameter = new java/lang/StringBuilder;
                  paramProtectionParameter.<init>();
                  paramProtectionParameter.append("HMAC key algorithm digest unknown for key algorithm ");
                  paramProtectionParameter.append(paramSecretKey.getAlgorithm());
                  paramString.<init>(paramProtectionParameter.toString());
                  throw paramString;
                }
                catch (IllegalArgumentException|IllegalStateException paramString) {}
              }
              boolean bool = localKeyProtection.isDigestsSpecified();
              if (bool) {
                paramSecretKey = KeyProperties.Digest.allToKeymaster(localKeyProtection.getDigests());
              } else {
                paramSecretKey = EmptyArray.INT;
              }
              label552:
              localKeymasterArguments.addEnums(536870917, paramSecretKey);
              int k = localKeyProtection.getPurposes();
              paramProtectionParameter = KeyProperties.BlockMode.allToKeymaster(localKeyProtection.getBlockModes());
              if (((k & 0x1) != 0) && (localKeyProtection.isRandomizedEncryptionRequired()))
              {
                int m = paramProtectionParameter.length;
                while (j < m)
                {
                  int n = paramProtectionParameter[j];
                  if (KeymasterUtils.isKeymasterBlockModeIndCpaCompatibleWithSymmetricCrypto(n))
                  {
                    j++;
                  }
                  else
                  {
                    paramString = new java/security/KeyStoreException;
                    paramSecretKey = new java/lang/StringBuilder;
                    paramSecretKey.<init>();
                    paramSecretKey.append("Randomized encryption (IND-CPA) required but may be violated by block mode: ");
                    paramSecretKey.append(KeyProperties.BlockMode.fromKeymaster(n));
                    paramSecretKey.append(". See KeyProtection documentation.");
                    paramString.<init>(paramSecretKey.toString());
                    throw paramString;
                  }
                }
              }
              localKeymasterArguments.addEnums(536870913, KeyProperties.Purpose.allToKeymaster(k));
              localKeymasterArguments.addEnums(536870916, paramProtectionParameter);
              if (localKeyProtection.getSignaturePaddings().length <= 0)
              {
                localKeymasterArguments.addEnums(536870918, KeyProperties.EncryptionPadding.allToKeymaster(localKeyProtection.getEncryptionPaddings()));
                KeymasterUtils.addUserAuthArgs(localKeymasterArguments, localKeyProtection);
                KeymasterUtils.addMinMacLengthAuthorizationIfNecessary(localKeymasterArguments, i, paramProtectionParameter, paramSecretKey);
                localKeymasterArguments.addDateIfNotNull(1610613136, localKeyProtection.getKeyValidityStart());
                localKeymasterArguments.addDateIfNotNull(1610613137, localKeyProtection.getKeyValidityForOriginationEnd());
                localKeymasterArguments.addDateIfNotNull(1610613138, localKeyProtection.getKeyValidityForConsumptionEnd());
                if (((k & 0x1) != 0) && (!localKeyProtection.isRandomizedEncryptionRequired())) {
                  localKeymasterArguments.addBoolean(1879048199);
                }
                j = 0;
                if (localKeyProtection.isCriticalToDeviceEncryption()) {
                  j = 0x0 | 0x8;
                }
                i = j;
                if (localKeyProtection.isStrongBoxBacked()) {
                  i = j | 0x10;
                }
                Credentials.deleteAllTypesForAlias(mKeyStore, paramString, mUid);
                paramSecretKey = new StringBuilder();
                paramSecretKey.append("USRPKEY_");
                paramSecretKey.append(paramString);
                paramString = paramSecretKey.toString();
                j = mKeyStore.importKey(paramString, localKeymasterArguments, 3, arrayOfByte, mUid, i, new KeyCharacteristics());
                if (j == 1) {
                  return;
                }
                paramString = new StringBuilder();
                paramString.append("Failed to import secret key. Keystore error code: ");
                paramString.append(j);
                throw new KeyStoreException(paramString.toString());
              }
              try
              {
                paramString = new java/security/KeyStoreException;
                paramString.<init>("Signature paddings not supported for symmetric keys");
                throw paramString;
              }
              catch (IllegalArgumentException|IllegalStateException paramString) {}
              throw new KeyStoreException(paramString);
            }
            catch (IllegalArgumentException|IllegalStateException paramString) {}
          }
          throw new KeyStoreException("Key did not export its key material despite supporting RAW format export");
        }
        paramString = new StringBuilder();
        paramString.append("Unsupported secret key material export format: ");
        paramString.append(paramProtectionParameter);
        throw new KeyStoreException(paramString.toString());
      }
      throw new KeyStoreException("Only secret keys that export their key material are supported");
    }
    throw new KeyStoreException("Protection parameters must be specified when importing a symmetric key");
  }
  
  private void setWrappedKeyEntry(String paramString, WrappedKeyEntry paramWrappedKeyEntry, KeyStore.ProtectionParameter paramProtectionParameter)
    throws KeyStoreException
  {
    if (paramProtectionParameter == null)
    {
      byte[] arrayOfByte = new byte[32];
      paramProtectionParameter = new KeymasterArguments();
      Object localObject1 = paramWrappedKeyEntry.getTransformation().split("/");
      Object localObject2 = localObject1[0];
      if ("RSA".equalsIgnoreCase((String)localObject2)) {
        paramProtectionParameter.addEnum(268435458, 1);
      } else if ("EC".equalsIgnoreCase((String)localObject2)) {
        paramProtectionParameter.addEnum(268435458, 1);
      }
      if (localObject1.length > 1)
      {
        localObject2 = localObject1[1];
        if ("ECB".equalsIgnoreCase((String)localObject2)) {
          paramProtectionParameter.addEnums(536870916, new int[] { 1 });
        } else if ("CBC".equalsIgnoreCase((String)localObject2)) {
          paramProtectionParameter.addEnums(536870916, new int[] { 2 });
        } else if ("CTR".equalsIgnoreCase((String)localObject2)) {
          paramProtectionParameter.addEnums(536870916, new int[] { 3 });
        } else if ("GCM".equalsIgnoreCase((String)localObject2)) {
          paramProtectionParameter.addEnums(536870916, new int[] { 32 });
        }
      }
      if (localObject1.length > 2)
      {
        localObject1 = localObject1[2];
        if (!"NoPadding".equalsIgnoreCase((String)localObject1)) {
          if ("PKCS7Padding".equalsIgnoreCase((String)localObject1)) {
            paramProtectionParameter.addEnums(536870918, new int[] { 64 });
          } else if ("PKCS1Padding".equalsIgnoreCase((String)localObject1)) {
            paramProtectionParameter.addEnums(536870918, new int[] { 4 });
          } else if ("OAEPPadding".equalsIgnoreCase((String)localObject1)) {
            paramProtectionParameter.addEnums(536870918, new int[] { 2 });
          }
        }
      }
      localObject1 = (KeyGenParameterSpec)paramWrappedKeyEntry.getAlgorithmParameterSpec();
      if (((KeyGenParameterSpec)localObject1).isDigestsSpecified())
      {
        localObject1 = localObject1.getDigests()[0];
        if (!"NONE".equalsIgnoreCase((String)localObject1)) {
          if ("MD5".equalsIgnoreCase((String)localObject1)) {
            paramProtectionParameter.addEnums(536870917, new int[] { 1 });
          } else if ("SHA-1".equalsIgnoreCase((String)localObject1)) {
            paramProtectionParameter.addEnums(536870917, new int[] { 2 });
          } else if ("SHA-224".equalsIgnoreCase((String)localObject1)) {
            paramProtectionParameter.addEnums(536870917, new int[] { 3 });
          } else if ("SHA-256".equalsIgnoreCase((String)localObject1)) {
            paramProtectionParameter.addEnums(536870917, new int[] { 4 });
          } else if ("SHA-384".equalsIgnoreCase((String)localObject1)) {
            paramProtectionParameter.addEnums(536870917, new int[] { 5 });
          } else if ("SHA-512".equalsIgnoreCase((String)localObject1)) {
            paramProtectionParameter.addEnums(536870917, new int[] { 6 });
          }
        }
      }
      localObject1 = mKeyStore;
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("USRPKEY_");
      ((StringBuilder)localObject2).append(paramString);
      String str = ((StringBuilder)localObject2).toString();
      paramString = paramWrappedKeyEntry.getWrappedKeyBytes();
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("USRPKEY_");
      ((StringBuilder)localObject2).append(paramWrappedKeyEntry.getWrappingKeyAlias());
      int i = ((KeyStore)localObject1).importWrappedKey(str, paramString, ((StringBuilder)localObject2).toString(), arrayOfByte, paramProtectionParameter, GateKeeper.getSecureUserId(), 0L, mUid, new KeyCharacteristics());
      if (i != -100)
      {
        if (i == 1) {
          return;
        }
        paramString = new StringBuilder();
        paramString.append("Failed to import wrapped key. Keystore error code: ");
        paramString.append(i);
        throw new KeyStoreException(paramString.toString());
      }
      throw new SecureKeyImportUnavailableException("Could not import wrapped key");
    }
    throw new KeyStoreException("Protection parameters are specified inside wrapped keys");
  }
  
  private static X509Certificate toCertificate(byte[] paramArrayOfByte)
  {
    try
    {
      CertificateFactory localCertificateFactory = CertificateFactory.getInstance("X.509");
      ByteArrayInputStream localByteArrayInputStream = new java/io/ByteArrayInputStream;
      localByteArrayInputStream.<init>(paramArrayOfByte);
      paramArrayOfByte = (X509Certificate)localCertificateFactory.generateCertificate(localByteArrayInputStream);
      return paramArrayOfByte;
    }
    catch (CertificateException paramArrayOfByte)
    {
      Log.w("AndroidKeyStore", "Couldn't parse certificate in keystore", paramArrayOfByte);
    }
    return null;
  }
  
  private static Collection<X509Certificate> toCertificates(byte[] paramArrayOfByte)
  {
    try
    {
      CertificateFactory localCertificateFactory = CertificateFactory.getInstance("X.509");
      ByteArrayInputStream localByteArrayInputStream = new java/io/ByteArrayInputStream;
      localByteArrayInputStream.<init>(paramArrayOfByte);
      paramArrayOfByte = localCertificateFactory.generateCertificates(localByteArrayInputStream);
      return paramArrayOfByte;
    }
    catch (CertificateException paramArrayOfByte)
    {
      Log.w("AndroidKeyStore", "Couldn't parse certificates in keystore", paramArrayOfByte);
    }
    return new ArrayList();
  }
  
  private static KeyStoreX509Certificate wrapIntoKeyStoreCertificate(String paramString, int paramInt, X509Certificate paramX509Certificate)
  {
    if (paramX509Certificate != null) {
      paramString = new KeyStoreX509Certificate(paramString, paramInt, paramX509Certificate);
    } else {
      paramString = null;
    }
    return paramString;
  }
  
  public Enumeration<String> engineAliases()
  {
    return Collections.enumeration(getUniqueAliases());
  }
  
  public boolean engineContainsAlias(String paramString)
  {
    if (paramString != null)
    {
      Object localObject1 = mKeyStore;
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("USRPKEY_");
      ((StringBuilder)localObject2).append(paramString);
      if (!((KeyStore)localObject1).contains(((StringBuilder)localObject2).toString(), mUid))
      {
        localObject1 = mKeyStore;
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("USRSKEY_");
        ((StringBuilder)localObject2).append(paramString);
        if (!((KeyStore)localObject1).contains(((StringBuilder)localObject2).toString(), mUid))
        {
          localObject2 = mKeyStore;
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("USRCERT_");
          ((StringBuilder)localObject1).append(paramString);
          if (!((KeyStore)localObject2).contains(((StringBuilder)localObject1).toString(), mUid))
          {
            localObject2 = mKeyStore;
            localObject1 = new StringBuilder();
            ((StringBuilder)localObject1).append("CACERT_");
            ((StringBuilder)localObject1).append(paramString);
            if (!((KeyStore)localObject2).contains(((StringBuilder)localObject1).toString(), mUid))
            {
              bool = false;
              break label181;
            }
          }
        }
      }
      boolean bool = true;
      label181:
      return bool;
    }
    throw new NullPointerException("alias == null");
  }
  
  public void engineDeleteEntry(String paramString)
    throws KeyStoreException
  {
    if (Credentials.deleteAllTypesForAlias(mKeyStore, paramString, mUid)) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Failed to delete entry: ");
    localStringBuilder.append(paramString);
    throw new KeyStoreException(localStringBuilder.toString());
  }
  
  public Certificate engineGetCertificate(String paramString)
  {
    if (paramString != null)
    {
      Object localObject1 = mKeyStore;
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("USRCERT_");
      ((StringBuilder)localObject2).append(paramString);
      localObject1 = ((KeyStore)localObject1).get(((StringBuilder)localObject2).toString(), mUid);
      if (localObject1 != null) {
        return getCertificateForPrivateKeyEntry(paramString, (byte[])localObject1);
      }
      localObject2 = mKeyStore;
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("CACERT_");
      ((StringBuilder)localObject1).append(paramString);
      paramString = ((KeyStore)localObject2).get(((StringBuilder)localObject1).toString(), mUid);
      if (paramString != null) {
        return getCertificateForTrustedCertificateEntry(paramString);
      }
      return null;
    }
    throw new NullPointerException("alias == null");
  }
  
  public String engineGetCertificateAlias(Certificate paramCertificate)
  {
    if (paramCertificate == null) {
      return null;
    }
    if (!"X.509".equalsIgnoreCase(paramCertificate.getType())) {
      return null;
    }
    try
    {
      paramCertificate = paramCertificate.getEncoded();
      if (paramCertificate == null) {
        return null;
      }
      HashSet localHashSet = new HashSet();
      String[] arrayOfString = mKeyStore.list("USRCERT_", mUid);
      int i = 0;
      int j;
      int k;
      Object localObject2;
      Object localObject3;
      if (arrayOfString != null)
      {
        j = arrayOfString.length;
        for (k = 0; k < j; k++)
        {
          localObject1 = arrayOfString[k];
          localObject2 = mKeyStore;
          localObject3 = new StringBuilder();
          ((StringBuilder)localObject3).append("USRCERT_");
          ((StringBuilder)localObject3).append((String)localObject1);
          localObject2 = ((KeyStore)localObject2).get(((StringBuilder)localObject3).toString(), mUid);
          if (localObject2 != null)
          {
            localHashSet.add(localObject1);
            if (Arrays.equals((byte[])localObject2, paramCertificate)) {
              return localObject1;
            }
          }
        }
      }
      Object localObject1 = mKeyStore.list("CACERT_", mUid);
      if (arrayOfString != null)
      {
        j = localObject1.length;
        for (k = i; k < j; k++)
        {
          arrayOfString = localObject1[k];
          if (!localHashSet.contains(arrayOfString))
          {
            localObject3 = mKeyStore;
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("CACERT_");
            ((StringBuilder)localObject2).append(arrayOfString);
            localObject2 = ((KeyStore)localObject3).get(((StringBuilder)localObject2).toString(), mUid);
            if ((localObject2 != null) && (Arrays.equals((byte[])localObject2, paramCertificate))) {
              return arrayOfString;
            }
          }
        }
      }
      return null;
    }
    catch (CertificateEncodingException paramCertificate) {}
    return null;
  }
  
  public Certificate[] engineGetCertificateChain(String paramString)
  {
    if (paramString != null)
    {
      X509Certificate localX509Certificate = (X509Certificate)engineGetCertificate(paramString);
      if (localX509Certificate == null) {
        return null;
      }
      Object localObject = mKeyStore;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("CACERT_");
      localStringBuilder.append(paramString);
      paramString = ((KeyStore)localObject).get(localStringBuilder.toString(), mUid);
      int i = 1;
      if (paramString != null)
      {
        localObject = toCertificates(paramString);
        paramString = new Certificate[((Collection)localObject).size() + 1];
        localObject = ((Collection)localObject).iterator();
        while (((Iterator)localObject).hasNext())
        {
          paramString[i] = ((Certificate)((Iterator)localObject).next());
          i++;
        }
      }
      else
      {
        paramString = new Certificate[1];
      }
      paramString[0] = localX509Certificate;
      return paramString;
    }
    throw new NullPointerException("alias == null");
  }
  
  public Date engineGetCreationDate(String paramString)
  {
    if (paramString != null)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("USRPKEY_");
      ((StringBuilder)localObject).append(paramString);
      localObject = getModificationDate(((StringBuilder)localObject).toString());
      if (localObject != null) {
        return localObject;
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("USRSKEY_");
      ((StringBuilder)localObject).append(paramString);
      localObject = getModificationDate(((StringBuilder)localObject).toString());
      if (localObject != null) {
        return localObject;
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("USRCERT_");
      ((StringBuilder)localObject).append(paramString);
      localObject = getModificationDate(((StringBuilder)localObject).toString());
      if (localObject != null) {
        return localObject;
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("CACERT_");
      ((StringBuilder)localObject).append(paramString);
      return getModificationDate(((StringBuilder)localObject).toString());
    }
    throw new NullPointerException("alias == null");
  }
  
  public Key engineGetKey(String paramString, char[] paramArrayOfChar)
    throws NoSuchAlgorithmException, UnrecoverableKeyException
  {
    paramArrayOfChar = new StringBuilder();
    paramArrayOfChar.append("USRPKEY_");
    paramArrayOfChar.append(paramString);
    String str = paramArrayOfChar.toString();
    paramArrayOfChar = str;
    if (!mKeyStore.contains(str, mUid))
    {
      paramArrayOfChar = new StringBuilder();
      paramArrayOfChar.append("USRSKEY_");
      paramArrayOfChar.append(paramString);
      paramString = paramArrayOfChar.toString();
      paramArrayOfChar = paramString;
      if (!mKeyStore.contains(paramString, mUid)) {
        return null;
      }
    }
    return AndroidKeyStoreProvider.loadAndroidKeyStoreKeyFromKeystore(mKeyStore, paramArrayOfChar, mUid);
  }
  
  public boolean engineIsCertificateEntry(String paramString)
  {
    boolean bool;
    if ((!isKeyEntry(paramString)) && (isCertificateEntry(paramString))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean engineIsKeyEntry(String paramString)
  {
    return isKeyEntry(paramString);
  }
  
  public void engineLoad(InputStream paramInputStream, char[] paramArrayOfChar)
    throws IOException, NoSuchAlgorithmException, CertificateException
  {
    if (paramInputStream == null)
    {
      if (paramArrayOfChar == null)
      {
        mKeyStore = KeyStore.getInstance();
        mUid = -1;
        return;
      }
      throw new IllegalArgumentException("password not supported");
    }
    throw new IllegalArgumentException("InputStream not supported");
  }
  
  public void engineLoad(KeyStore.LoadStoreParameter paramLoadStoreParameter)
    throws IOException, NoSuchAlgorithmException, CertificateException
  {
    int i = -1;
    if (paramLoadStoreParameter != null) {
      if ((paramLoadStoreParameter instanceof AndroidKeyStoreLoadStoreParameter))
      {
        i = ((AndroidKeyStoreLoadStoreParameter)paramLoadStoreParameter).getUid();
      }
      else
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unsupported param type: ");
        localStringBuilder.append(paramLoadStoreParameter.getClass());
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
    }
    mKeyStore = KeyStore.getInstance();
    mUid = i;
  }
  
  public void engineSetCertificateEntry(String paramString, Certificate paramCertificate)
    throws KeyStoreException
  {
    if (!isKeyEntry(paramString))
    {
      if (paramCertificate != null) {
        try
        {
          paramCertificate = paramCertificate.getEncoded();
          KeyStore localKeyStore = mKeyStore;
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("CACERT_");
          localStringBuilder.append(paramString);
          if (localKeyStore.put(localStringBuilder.toString(), paramCertificate, mUid, 0)) {
            return;
          }
          throw new KeyStoreException("Couldn't insert certificate; is KeyStore initialized?");
        }
        catch (CertificateEncodingException paramString)
        {
          throw new KeyStoreException(paramString);
        }
      }
      throw new NullPointerException("cert == null");
    }
    throw new KeyStoreException("Entry exists and is not a trusted certificate");
  }
  
  public void engineSetEntry(String paramString, KeyStore.Entry paramEntry, KeyStore.ProtectionParameter paramProtectionParameter)
    throws KeyStoreException
  {
    if (paramEntry != null)
    {
      Credentials.deleteAllTypesForAlias(mKeyStore, paramString, mUid);
      if ((paramEntry instanceof KeyStore.TrustedCertificateEntry))
      {
        engineSetCertificateEntry(paramString, ((KeyStore.TrustedCertificateEntry)paramEntry).getTrustedCertificate());
        return;
      }
      if ((paramEntry instanceof KeyStore.PrivateKeyEntry))
      {
        paramEntry = (KeyStore.PrivateKeyEntry)paramEntry;
        setPrivateKeyEntry(paramString, paramEntry.getPrivateKey(), paramEntry.getCertificateChain(), paramProtectionParameter);
      }
      else if ((paramEntry instanceof KeyStore.SecretKeyEntry))
      {
        setSecretKeyEntry(paramString, ((KeyStore.SecretKeyEntry)paramEntry).getSecretKey(), paramProtectionParameter);
      }
      else
      {
        if (!(paramEntry instanceof WrappedKeyEntry)) {
          break label107;
        }
        setWrappedKeyEntry(paramString, (WrappedKeyEntry)paramEntry, paramProtectionParameter);
      }
      return;
      label107:
      paramString = new StringBuilder();
      paramString.append("Entry must be a PrivateKeyEntry, SecretKeyEntry or TrustedCertificateEntry; was ");
      paramString.append(paramEntry);
      throw new KeyStoreException(paramString.toString());
    }
    throw new KeyStoreException("entry == null");
  }
  
  public void engineSetKeyEntry(String paramString, Key paramKey, char[] paramArrayOfChar, Certificate[] paramArrayOfCertificate)
    throws KeyStoreException
  {
    if ((paramArrayOfChar != null) && (paramArrayOfChar.length > 0)) {
      throw new KeyStoreException("entries cannot be protected with passwords");
    }
    if ((paramKey instanceof PrivateKey))
    {
      setPrivateKeyEntry(paramString, (PrivateKey)paramKey, paramArrayOfCertificate, null);
    }
    else
    {
      if (!(paramKey instanceof SecretKey)) {
        break label63;
      }
      setSecretKeyEntry(paramString, (SecretKey)paramKey, null);
    }
    return;
    label63:
    throw new KeyStoreException("Only PrivateKey and SecretKey are supported");
  }
  
  public void engineSetKeyEntry(String paramString, byte[] paramArrayOfByte, Certificate[] paramArrayOfCertificate)
    throws KeyStoreException
  {
    throw new KeyStoreException("Operation not supported because key encoding is unknown");
  }
  
  public int engineSize()
  {
    return getUniqueAliases().size();
  }
  
  public void engineStore(OutputStream paramOutputStream, char[] paramArrayOfChar)
    throws IOException, NoSuchAlgorithmException, CertificateException
  {
    throw new UnsupportedOperationException("Can not serialize AndroidKeyStore to OutputStream");
  }
  
  static class KeyStoreX509Certificate
    extends DelegatingX509Certificate
  {
    private final String mPrivateKeyAlias;
    private final int mPrivateKeyUid;
    
    KeyStoreX509Certificate(String paramString, int paramInt, X509Certificate paramX509Certificate)
    {
      super();
      mPrivateKeyAlias = paramString;
      mPrivateKeyUid = paramInt;
    }
    
    public PublicKey getPublicKey()
    {
      PublicKey localPublicKey = super.getPublicKey();
      return AndroidKeyStoreProvider.getAndroidKeyStorePublicKey(mPrivateKeyAlias, mPrivateKeyUid, localPublicKey.getAlgorithm(), localPublicKey.getEncoded());
    }
  }
}
