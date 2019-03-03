package android.security.net.config;

import android.content.Context;
import android.util.ArraySet;
import com.android.org.conscrypt.TrustedCertificateIndex;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

public class ResourceCertificateSource
  implements CertificateSource
{
  private Set<X509Certificate> mCertificates;
  private Context mContext;
  private TrustedCertificateIndex mIndex;
  private final Object mLock = new Object();
  private final int mResourceId;
  
  public ResourceCertificateSource(int paramInt, Context paramContext)
  {
    mResourceId = paramInt;
    mContext = paramContext;
  }
  
  /* Error */
  private void ensureInitialized()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 24	android/security/net/config/ResourceCertificateSource:mLock	Ljava/lang/Object;
    //   4: astore_1
    //   5: aload_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 34	android/security/net/config/ResourceCertificateSource:mCertificates	Ljava/util/Set;
    //   11: ifnull +6 -> 17
    //   14: aload_1
    //   15: monitorexit
    //   16: return
    //   17: new 36	android/util/ArraySet
    //   20: astore_2
    //   21: aload_2
    //   22: invokespecial 37	android/util/ArraySet:<init>	()V
    //   25: aconst_null
    //   26: astore_3
    //   27: aconst_null
    //   28: astore 4
    //   30: aload 4
    //   32: astore 5
    //   34: aload_3
    //   35: astore 6
    //   37: ldc 39
    //   39: invokestatic 45	java/security/cert/CertificateFactory:getInstance	(Ljava/lang/String;)Ljava/security/cert/CertificateFactory;
    //   42: astore 7
    //   44: aload 4
    //   46: astore 5
    //   48: aload_3
    //   49: astore 6
    //   51: aload_0
    //   52: getfield 28	android/security/net/config/ResourceCertificateSource:mContext	Landroid/content/Context;
    //   55: invokevirtual 51	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   58: aload_0
    //   59: getfield 26	android/security/net/config/ResourceCertificateSource:mResourceId	I
    //   62: invokevirtual 57	android/content/res/Resources:openRawResource	(I)Ljava/io/InputStream;
    //   65: astore 4
    //   67: aload 4
    //   69: astore 5
    //   71: aload 4
    //   73: astore 6
    //   75: aload 7
    //   77: aload 4
    //   79: invokevirtual 61	java/security/cert/CertificateFactory:generateCertificates	(Ljava/io/InputStream;)Ljava/util/Collection;
    //   82: astore_3
    //   83: aload 4
    //   85: invokestatic 67	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   88: new 69	com/android/org/conscrypt/TrustedCertificateIndex
    //   91: astore 5
    //   93: aload 5
    //   95: invokespecial 70	com/android/org/conscrypt/TrustedCertificateIndex:<init>	()V
    //   98: aload_3
    //   99: invokeinterface 76 1 0
    //   104: astore 6
    //   106: aload 6
    //   108: invokeinterface 82 1 0
    //   113: ifeq +41 -> 154
    //   116: aload 6
    //   118: invokeinterface 86 1 0
    //   123: checkcast 88	java/security/cert/Certificate
    //   126: astore 4
    //   128: aload_2
    //   129: aload 4
    //   131: checkcast 90	java/security/cert/X509Certificate
    //   134: invokeinterface 96 2 0
    //   139: pop
    //   140: aload 5
    //   142: aload 4
    //   144: checkcast 90	java/security/cert/X509Certificate
    //   147: invokevirtual 100	com/android/org/conscrypt/TrustedCertificateIndex:index	(Ljava/security/cert/X509Certificate;)Ljava/security/cert/TrustAnchor;
    //   150: pop
    //   151: goto -45 -> 106
    //   154: aload_0
    //   155: aload_2
    //   156: putfield 34	android/security/net/config/ResourceCertificateSource:mCertificates	Ljava/util/Set;
    //   159: aload_0
    //   160: aload 5
    //   162: putfield 102	android/security/net/config/ResourceCertificateSource:mIndex	Lcom/android/org/conscrypt/TrustedCertificateIndex;
    //   165: aload_0
    //   166: aconst_null
    //   167: putfield 28	android/security/net/config/ResourceCertificateSource:mContext	Landroid/content/Context;
    //   170: aload_1
    //   171: monitorexit
    //   172: return
    //   173: astore 6
    //   175: goto +74 -> 249
    //   178: astore_2
    //   179: aload 6
    //   181: astore 5
    //   183: new 104	java/lang/RuntimeException
    //   186: astore 4
    //   188: aload 6
    //   190: astore 5
    //   192: new 106	java/lang/StringBuilder
    //   195: astore_3
    //   196: aload 6
    //   198: astore 5
    //   200: aload_3
    //   201: invokespecial 107	java/lang/StringBuilder:<init>	()V
    //   204: aload 6
    //   206: astore 5
    //   208: aload_3
    //   209: ldc 109
    //   211: invokevirtual 113	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   214: pop
    //   215: aload 6
    //   217: astore 5
    //   219: aload_3
    //   220: aload_0
    //   221: getfield 26	android/security/net/config/ResourceCertificateSource:mResourceId	I
    //   224: invokevirtual 116	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   227: pop
    //   228: aload 6
    //   230: astore 5
    //   232: aload 4
    //   234: aload_3
    //   235: invokevirtual 120	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   238: aload_2
    //   239: invokespecial 123	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   242: aload 6
    //   244: astore 5
    //   246: aload 4
    //   248: athrow
    //   249: aload 5
    //   251: invokestatic 67	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   254: aload 6
    //   256: athrow
    //   257: astore 5
    //   259: aload_1
    //   260: monitorexit
    //   261: aload 5
    //   263: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	264	0	this	ResourceCertificateSource
    //   4	256	1	localObject1	Object
    //   20	136	2	localArraySet	ArraySet
    //   178	61	2	localCertificateException	java.security.cert.CertificateException
    //   26	209	3	localObject2	Object
    //   28	219	4	localObject3	Object
    //   32	218	5	localObject4	Object
    //   257	5	5	localObject5	Object
    //   35	82	6	localObject6	Object
    //   173	82	6	localObject7	Object
    //   42	34	7	localCertificateFactory	java.security.cert.CertificateFactory
    // Exception table:
    //   from	to	target	type
    //   37	44	173	finally
    //   51	67	173	finally
    //   75	83	173	finally
    //   183	188	173	finally
    //   192	196	173	finally
    //   200	204	173	finally
    //   208	215	173	finally
    //   219	228	173	finally
    //   232	242	173	finally
    //   246	249	173	finally
    //   37	44	178	java/security/cert/CertificateException
    //   51	67	178	java/security/cert/CertificateException
    //   75	83	178	java/security/cert/CertificateException
    //   7	16	257	finally
    //   17	25	257	finally
    //   83	88	257	finally
    //   88	106	257	finally
    //   106	151	257	finally
    //   154	172	257	finally
    //   249	257	257	finally
    //   259	261	257	finally
  }
  
  public Set<X509Certificate> findAllByIssuerAndSignature(X509Certificate paramX509Certificate)
  {
    ensureInitialized();
    Object localObject = mIndex.findAllByIssuerAndSignature(paramX509Certificate);
    if (((Set)localObject).isEmpty()) {
      return Collections.emptySet();
    }
    paramX509Certificate = new ArraySet(((Set)localObject).size());
    localObject = ((Set)localObject).iterator();
    while (((Iterator)localObject).hasNext()) {
      paramX509Certificate.add(((TrustAnchor)((Iterator)localObject).next()).getTrustedCert());
    }
    return paramX509Certificate;
  }
  
  public X509Certificate findByIssuerAndSignature(X509Certificate paramX509Certificate)
  {
    ensureInitialized();
    paramX509Certificate = mIndex.findByIssuerAndSignature(paramX509Certificate);
    if (paramX509Certificate == null) {
      return null;
    }
    return paramX509Certificate.getTrustedCert();
  }
  
  public X509Certificate findBySubjectAndPublicKey(X509Certificate paramX509Certificate)
  {
    ensureInitialized();
    paramX509Certificate = mIndex.findBySubjectAndPublicKey(paramX509Certificate);
    if (paramX509Certificate == null) {
      return null;
    }
    return paramX509Certificate.getTrustedCert();
  }
  
  public Set<X509Certificate> getCertificates()
  {
    ensureInitialized();
    return mCertificates;
  }
  
  public void handleTrustStorageUpdate() {}
}
