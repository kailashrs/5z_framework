package android.security.net.config;

import android.util.ArraySet;
import com.android.org.conscrypt.Hex;
import com.android.org.conscrypt.NativeCrypto;
import java.io.File;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Set;
import javax.security.auth.x500.X500Principal;

abstract class DirectoryCertificateSource
  implements CertificateSource
{
  private static final String LOG_TAG = "DirectoryCertificateSrc";
  private final CertificateFactory mCertFactory;
  private Set<X509Certificate> mCertificates;
  private final File mDir;
  private final Object mLock = new Object();
  
  protected DirectoryCertificateSource(File paramFile)
  {
    mDir = paramFile;
    try
    {
      mCertFactory = CertificateFactory.getInstance("X.509");
      return;
    }
    catch (CertificateException paramFile)
    {
      throw new RuntimeException("Failed to obtain X.509 CertificateFactory", paramFile);
    }
  }
  
  private X509Certificate findCert(X500Principal paramX500Principal, CertSelector paramCertSelector)
  {
    String str = getHash(paramX500Principal);
    for (int i = 0; i >= 0; i++)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append(str);
      ((StringBuilder)localObject).append(".");
      ((StringBuilder)localObject).append(i);
      localObject = ((StringBuilder)localObject).toString();
      if (!new File(mDir, (String)localObject).exists()) {
        break;
      }
      if (!isCertMarkedAsRemoved((String)localObject))
      {
        localObject = readCertificate((String)localObject);
        if ((localObject != null) && (paramX500Principal.equals(((X509Certificate)localObject).getSubjectX500Principal())) && (paramCertSelector.match((X509Certificate)localObject))) {
          return localObject;
        }
      }
    }
    return null;
  }
  
  private Set<X509Certificate> findCerts(X500Principal paramX500Principal, CertSelector paramCertSelector)
  {
    String str = getHash(paramX500Principal);
    Object localObject1 = null;
    int i = 0;
    while (i >= 0)
    {
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append(str);
      ((StringBuilder)localObject2).append(".");
      ((StringBuilder)localObject2).append(i);
      localObject2 = ((StringBuilder)localObject2).toString();
      if (!new File(mDir, (String)localObject2).exists()) {
        break;
      }
      if (isCertMarkedAsRemoved((String)localObject2))
      {
        localObject2 = localObject1;
      }
      else
      {
        X509Certificate localX509Certificate = readCertificate((String)localObject2);
        if (localX509Certificate == null)
        {
          localObject2 = localObject1;
        }
        else if (!paramX500Principal.equals(localX509Certificate.getSubjectX500Principal()))
        {
          localObject2 = localObject1;
        }
        else
        {
          localObject2 = localObject1;
          if (paramCertSelector.match(localX509Certificate))
          {
            localObject2 = localObject1;
            if (localObject1 == null) {
              localObject2 = new ArraySet();
            }
            ((Set)localObject2).add(localX509Certificate);
          }
        }
      }
      i++;
      localObject1 = localObject2;
    }
    if (localObject1 != null) {
      paramX500Principal = localObject1;
    } else {
      paramX500Principal = Collections.emptySet();
    }
    return paramX500Principal;
  }
  
  private String getHash(X500Principal paramX500Principal)
  {
    return Hex.intToHexString(NativeCrypto.X509_NAME_hash_old(paramX500Principal), 8);
  }
  
  /* Error */
  private X509Certificate readCertificate(String paramString)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: aload_3
    //   5: astore 4
    //   7: aload_2
    //   8: astore 5
    //   10: new 146	java/io/BufferedInputStream
    //   13: astore 6
    //   15: aload_3
    //   16: astore 4
    //   18: aload_2
    //   19: astore 5
    //   21: new 148	java/io/FileInputStream
    //   24: astore 7
    //   26: aload_3
    //   27: astore 4
    //   29: aload_2
    //   30: astore 5
    //   32: new 81	java/io/File
    //   35: astore 8
    //   37: aload_3
    //   38: astore 4
    //   40: aload_2
    //   41: astore 5
    //   43: aload 8
    //   45: aload_0
    //   46: getfield 39	android/security/net/config/DirectoryCertificateSource:mDir	Ljava/io/File;
    //   49: aload_1
    //   50: invokespecial 84	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   53: aload_3
    //   54: astore 4
    //   56: aload_2
    //   57: astore 5
    //   59: aload 7
    //   61: aload 8
    //   63: invokespecial 150	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   66: aload_3
    //   67: astore 4
    //   69: aload_2
    //   70: astore 5
    //   72: aload 6
    //   74: aload 7
    //   76: invokespecial 153	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;)V
    //   79: aload 6
    //   81: astore_3
    //   82: aload_3
    //   83: astore 4
    //   85: aload_3
    //   86: astore 5
    //   88: aload_0
    //   89: getfield 49	android/security/net/config/DirectoryCertificateSource:mCertFactory	Ljava/security/cert/CertificateFactory;
    //   92: aload_3
    //   93: invokevirtual 157	java/security/cert/CertificateFactory:generateCertificate	(Ljava/io/InputStream;)Ljava/security/cert/Certificate;
    //   96: checkcast 98	java/security/cert/X509Certificate
    //   99: astore 6
    //   101: aload_3
    //   102: invokestatic 163	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   105: aload 6
    //   107: areturn
    //   108: astore_1
    //   109: goto +68 -> 177
    //   112: astore_3
    //   113: aload 5
    //   115: astore 4
    //   117: new 65	java/lang/StringBuilder
    //   120: astore 6
    //   122: aload 5
    //   124: astore 4
    //   126: aload 6
    //   128: invokespecial 66	java/lang/StringBuilder:<init>	()V
    //   131: aload 5
    //   133: astore 4
    //   135: aload 6
    //   137: ldc -91
    //   139: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   142: pop
    //   143: aload 5
    //   145: astore 4
    //   147: aload 6
    //   149: aload_1
    //   150: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   153: pop
    //   154: aload 5
    //   156: astore 4
    //   158: ldc 19
    //   160: aload 6
    //   162: invokevirtual 79	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   165: aload_3
    //   166: invokestatic 171	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   169: pop
    //   170: aload 5
    //   172: invokestatic 163	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   175: aconst_null
    //   176: areturn
    //   177: aload 4
    //   179: invokestatic 163	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   182: aload_1
    //   183: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	184	0	this	DirectoryCertificateSource
    //   0	184	1	paramString	String
    //   1	69	2	localObject1	Object
    //   3	99	3	localObject2	Object
    //   112	54	3	localCertificateException	CertificateException
    //   5	173	4	localObject3	Object
    //   8	163	5	localObject4	Object
    //   13	148	6	localObject5	Object
    //   24	51	7	localFileInputStream	java.io.FileInputStream
    //   35	27	8	localFile	File
    // Exception table:
    //   from	to	target	type
    //   10	15	108	finally
    //   21	26	108	finally
    //   32	37	108	finally
    //   43	53	108	finally
    //   59	66	108	finally
    //   72	79	108	finally
    //   88	101	108	finally
    //   117	122	108	finally
    //   126	131	108	finally
    //   135	143	108	finally
    //   147	154	108	finally
    //   158	170	108	finally
    //   10	15	112	java/security/cert/CertificateException
    //   10	15	112	java/io/IOException
    //   21	26	112	java/security/cert/CertificateException
    //   21	26	112	java/io/IOException
    //   32	37	112	java/security/cert/CertificateException
    //   32	37	112	java/io/IOException
    //   43	53	112	java/security/cert/CertificateException
    //   43	53	112	java/io/IOException
    //   59	66	112	java/security/cert/CertificateException
    //   59	66	112	java/io/IOException
    //   72	79	112	java/security/cert/CertificateException
    //   72	79	112	java/io/IOException
    //   88	101	112	java/security/cert/CertificateException
    //   88	101	112	java/io/IOException
  }
  
  public Set<X509Certificate> findAllByIssuerAndSignature(final X509Certificate paramX509Certificate)
  {
    findCerts(paramX509Certificate.getIssuerX500Principal(), new CertSelector()
    {
      public boolean match(X509Certificate paramAnonymousX509Certificate)
      {
        try
        {
          paramX509Certificate.verify(paramAnonymousX509Certificate.getPublicKey());
          return true;
        }
        catch (Exception paramAnonymousX509Certificate) {}
        return false;
      }
    });
  }
  
  public X509Certificate findByIssuerAndSignature(final X509Certificate paramX509Certificate)
  {
    findCert(paramX509Certificate.getIssuerX500Principal(), new CertSelector()
    {
      public boolean match(X509Certificate paramAnonymousX509Certificate)
      {
        try
        {
          paramX509Certificate.verify(paramAnonymousX509Certificate.getPublicKey());
          return true;
        }
        catch (Exception paramAnonymousX509Certificate) {}
        return false;
      }
    });
  }
  
  public X509Certificate findBySubjectAndPublicKey(final X509Certificate paramX509Certificate)
  {
    findCert(paramX509Certificate.getSubjectX500Principal(), new CertSelector()
    {
      public boolean match(X509Certificate paramAnonymousX509Certificate)
      {
        return paramAnonymousX509Certificate.getPublicKey().equals(paramX509Certificate.getPublicKey());
      }
    });
  }
  
  public Set<X509Certificate> getCertificates()
  {
    synchronized (mLock)
    {
      if (mCertificates != null)
      {
        localObject2 = mCertificates;
        return localObject2;
      }
      ArraySet localArraySet = new android/util/ArraySet;
      localArraySet.<init>();
      if (mDir.isDirectory()) {
        for (Object localObject4 : mDir.list()) {
          if (!isCertMarkedAsRemoved((String)localObject4))
          {
            localObject4 = readCertificate((String)localObject4);
            if (localObject4 != null) {
              localArraySet.add(localObject4);
            }
          }
        }
      }
      mCertificates = localArraySet;
      Object localObject2 = mCertificates;
      return localObject2;
    }
  }
  
  public void handleTrustStorageUpdate()
  {
    synchronized (mLock)
    {
      mCertificates = null;
      return;
    }
  }
  
  protected abstract boolean isCertMarkedAsRemoved(String paramString);
  
  private static abstract interface CertSelector
  {
    public abstract boolean match(X509Certificate paramX509Certificate);
  }
}
