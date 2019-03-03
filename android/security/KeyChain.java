package android.security;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.os.UserHandle;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.security.KeyPair;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class KeyChain
{
  public static final String ACCOUNT_TYPE = "com.android.keychain";
  private static final String ACTION_CHOOSER = "com.android.keychain.CHOOSER";
  private static final String ACTION_INSTALL = "android.credentials.INSTALL";
  public static final String ACTION_KEYCHAIN_CHANGED = "android.security.action.KEYCHAIN_CHANGED";
  public static final String ACTION_KEY_ACCESS_CHANGED = "android.security.action.KEY_ACCESS_CHANGED";
  public static final String ACTION_STORAGE_CHANGED = "android.security.STORAGE_CHANGED";
  public static final String ACTION_TRUST_STORE_CHANGED = "android.security.action.TRUST_STORE_CHANGED";
  private static final String CERT_INSTALLER_PACKAGE = "com.android.certinstaller";
  public static final String EXTRA_ALIAS = "alias";
  public static final String EXTRA_CERTIFICATE = "CERT";
  public static final String EXTRA_KEY_ACCESSIBLE = "android.security.extra.KEY_ACCESSIBLE";
  public static final String EXTRA_KEY_ALIAS = "android.security.extra.KEY_ALIAS";
  public static final String EXTRA_NAME = "name";
  public static final String EXTRA_PKCS12 = "PKCS12";
  public static final String EXTRA_RESPONSE = "response";
  public static final String EXTRA_SENDER = "sender";
  public static final String EXTRA_URI = "uri";
  private static final String KEYCHAIN_PACKAGE = "com.android.keychain";
  public static final int KEY_ATTESTATION_CANNOT_ATTEST_IDS = 3;
  public static final int KEY_ATTESTATION_CANNOT_COLLECT_DATA = 2;
  public static final int KEY_ATTESTATION_FAILURE = 4;
  public static final int KEY_ATTESTATION_MISSING_CHALLENGE = 1;
  public static final int KEY_ATTESTATION_SUCCESS = 0;
  public static final int KEY_GEN_FAILURE = 6;
  public static final int KEY_GEN_INVALID_ALGORITHM_PARAMETERS = 4;
  public static final int KEY_GEN_MISSING_ALIAS = 1;
  public static final int KEY_GEN_NO_KEYSTORE_PROVIDER = 5;
  public static final int KEY_GEN_NO_SUCH_ALGORITHM = 3;
  public static final int KEY_GEN_SUCCESS = 0;
  public static final int KEY_GEN_SUPERFLUOUS_ATTESTATION_CHALLENGE = 2;
  
  public KeyChain() {}
  
  public static KeyChainConnection bind(Context paramContext)
    throws InterruptedException
  {
    return bindAsUser(paramContext, Process.myUserHandle());
  }
  
  public static KeyChainConnection bindAsUser(Context paramContext, UserHandle paramUserHandle)
    throws InterruptedException
  {
    if (paramContext != null)
    {
      ensureNotOnMainThread(paramContext);
      LinkedBlockingQueue localLinkedBlockingQueue = new LinkedBlockingQueue(1);
      ServiceConnection local1 = new ServiceConnection()
      {
        volatile boolean mConnectedAtLeastOnce = false;
        
        public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
        {
          if (!mConnectedAtLeastOnce)
          {
            mConnectedAtLeastOnce = true;
            try
            {
              put(IKeyChainService.Stub.asInterface(Binder.allowBlocking(paramAnonymousIBinder)));
            }
            catch (InterruptedException paramAnonymousComponentName) {}
          }
        }
        
        public void onServiceDisconnected(ComponentName paramAnonymousComponentName) {}
      };
      Intent localIntent = new Intent(IKeyChainService.class.getName());
      ComponentName localComponentName = localIntent.resolveSystemService(paramContext.getPackageManager(), 0);
      localIntent.setComponent(localComponentName);
      if ((localComponentName != null) && (paramContext.bindServiceAsUser(localIntent, local1, 1, paramUserHandle))) {
        return new KeyChainConnection(paramContext, local1, (IKeyChainService)localLinkedBlockingQueue.take());
      }
      throw new AssertionError("could not bind to KeyChainService");
    }
    throw new NullPointerException("context == null");
  }
  
  public static void choosePrivateKeyAlias(Activity paramActivity, KeyChainAliasCallback paramKeyChainAliasCallback, String[] paramArrayOfString, Principal[] paramArrayOfPrincipal, Uri paramUri, String paramString)
  {
    if (paramActivity != null)
    {
      if (paramKeyChainAliasCallback != null)
      {
        paramArrayOfString = new Intent("com.android.keychain.CHOOSER");
        paramArrayOfString.setPackage("com.android.keychain");
        paramArrayOfString.putExtra("response", new AliasResponse(paramKeyChainAliasCallback, null));
        paramArrayOfString.putExtra("uri", paramUri);
        paramArrayOfString.putExtra("alias", paramString);
        paramArrayOfString.putExtra("sender", PendingIntent.getActivity(paramActivity, 0, new Intent(), 0));
        paramActivity.startActivity(paramArrayOfString);
        return;
      }
      throw new NullPointerException("response == null");
    }
    throw new NullPointerException("activity == null");
  }
  
  public static void choosePrivateKeyAlias(Activity paramActivity, KeyChainAliasCallback paramKeyChainAliasCallback, String[] paramArrayOfString, Principal[] paramArrayOfPrincipal, String paramString1, int paramInt, String paramString2)
  {
    Object localObject = null;
    if (paramString1 != null)
    {
      Uri.Builder localBuilder = new Uri.Builder();
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString1);
      if (paramInt != -1)
      {
        paramString1 = new StringBuilder();
        paramString1.append(":");
        paramString1.append(paramInt);
        paramString1 = paramString1.toString();
      }
      else
      {
        paramString1 = "";
      }
      ((StringBuilder)localObject).append(paramString1);
      localObject = localBuilder.authority(((StringBuilder)localObject).toString()).build();
    }
    choosePrivateKeyAlias(paramActivity, paramKeyChainAliasCallback, paramArrayOfString, paramArrayOfPrincipal, (Uri)localObject, paramString2);
  }
  
  public static Intent createInstallIntent()
  {
    Intent localIntent = new Intent("android.credentials.INSTALL");
    localIntent.setClassName("com.android.certinstaller", "com.android.certinstaller.CertInstallerMain");
    return localIntent;
  }
  
  private static void ensureNotOnMainThread(Context paramContext)
  {
    Looper localLooper = Looper.myLooper();
    if ((localLooper != null) && (localLooper == paramContext.getMainLooper())) {
      throw new IllegalStateException("calling this from your main thread can lead to deadlock");
    }
  }
  
  /* Error */
  public static X509Certificate[] getCertificateChain(Context paramContext, String paramString)
    throws KeyChainException, InterruptedException
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnull +217 -> 218
    //   4: aload_0
    //   5: invokevirtual 284	android/content/Context:getApplicationContext	()Landroid/content/Context;
    //   8: invokestatic 286	android/security/KeyChain:bind	(Landroid/content/Context;)Landroid/security/KeyChain$KeyChainConnection;
    //   11: astore_2
    //   12: aconst_null
    //   13: astore_3
    //   14: aload_3
    //   15: astore_0
    //   16: aload_2
    //   17: invokevirtual 290	android/security/KeyChain$KeyChainConnection:getService	()Landroid/security/IKeyChainService;
    //   20: astore 4
    //   22: aload_3
    //   23: astore_0
    //   24: aload 4
    //   26: aload_1
    //   27: invokeinterface 294 2 0
    //   32: astore 5
    //   34: aload 5
    //   36: ifnonnull +14 -> 50
    //   39: aload_2
    //   40: ifnull +8 -> 48
    //   43: aconst_null
    //   44: aload_2
    //   45: invokestatic 296	android/security/KeyChain:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   48: aconst_null
    //   49: areturn
    //   50: aload_3
    //   51: astore_0
    //   52: aload 4
    //   54: aload_1
    //   55: invokeinterface 299 2 0
    //   60: astore_1
    //   61: aload_2
    //   62: ifnull +8 -> 70
    //   65: aconst_null
    //   66: aload_2
    //   67: invokestatic 296	android/security/KeyChain:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   70: aload 5
    //   72: invokestatic 303	android/security/KeyChain:toCertificate	([B)Ljava/security/cert/X509Certificate;
    //   75: astore_0
    //   76: aload_1
    //   77: ifnull +56 -> 133
    //   80: aload_1
    //   81: arraylength
    //   82: ifeq +51 -> 133
    //   85: aload_1
    //   86: invokestatic 307	android/security/KeyChain:toCertificates	([B)Ljava/util/Collection;
    //   89: astore_3
    //   90: new 309	java/util/ArrayList
    //   93: astore_1
    //   94: aload_1
    //   95: aload_3
    //   96: invokeinterface 315 1 0
    //   101: iconst_1
    //   102: iadd
    //   103: invokespecial 316	java/util/ArrayList:<init>	(I)V
    //   106: aload_1
    //   107: aload_0
    //   108: invokevirtual 320	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   111: pop
    //   112: aload_1
    //   113: aload_3
    //   114: invokevirtual 324	java/util/ArrayList:addAll	(Ljava/util/Collection;)Z
    //   117: pop
    //   118: aload_1
    //   119: aload_1
    //   120: invokevirtual 325	java/util/ArrayList:size	()I
    //   123: anewarray 327	java/security/cert/X509Certificate
    //   126: invokevirtual 331	java/util/ArrayList:toArray	([Ljava/lang/Object;)[Ljava/lang/Object;
    //   129: checkcast 333	[Ljava/security/cert/X509Certificate;
    //   132: areturn
    //   133: new 335	com/android/org/conscrypt/TrustedCertificateStore
    //   136: astore_1
    //   137: aload_1
    //   138: invokespecial 336	com/android/org/conscrypt/TrustedCertificateStore:<init>	()V
    //   141: aload_1
    //   142: aload_0
    //   143: invokevirtual 339	com/android/org/conscrypt/TrustedCertificateStore:getCertificateChain	(Ljava/security/cert/X509Certificate;)Ljava/util/List;
    //   146: astore_0
    //   147: aload_0
    //   148: aload_0
    //   149: invokeinterface 342 1 0
    //   154: anewarray 327	java/security/cert/X509Certificate
    //   157: invokeinterface 343 2 0
    //   162: checkcast 333	[Ljava/security/cert/X509Certificate;
    //   165: astore_0
    //   166: aload_0
    //   167: areturn
    //   168: astore_0
    //   169: new 274	android/security/KeyChainException
    //   172: dup
    //   173: aload_0
    //   174: invokespecial 345	android/security/KeyChainException:<init>	(Ljava/lang/Throwable;)V
    //   177: athrow
    //   178: astore_1
    //   179: goto +8 -> 187
    //   182: astore_1
    //   183: aload_1
    //   184: astore_0
    //   185: aload_1
    //   186: athrow
    //   187: aload_2
    //   188: ifnull +8 -> 196
    //   191: aload_0
    //   192: aload_2
    //   193: invokestatic 296	android/security/KeyChain:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   196: aload_1
    //   197: athrow
    //   198: astore_0
    //   199: new 274	android/security/KeyChainException
    //   202: dup
    //   203: aload_0
    //   204: invokespecial 345	android/security/KeyChainException:<init>	(Ljava/lang/Throwable;)V
    //   207: athrow
    //   208: astore_0
    //   209: new 274	android/security/KeyChainException
    //   212: dup
    //   213: aload_0
    //   214: invokespecial 345	android/security/KeyChainException:<init>	(Ljava/lang/Throwable;)V
    //   217: athrow
    //   218: new 179	java/lang/NullPointerException
    //   221: dup
    //   222: ldc_w 347
    //   225: invokespecial 182	java/lang/NullPointerException:<init>	(Ljava/lang/String;)V
    //   228: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	229	0	paramContext	Context
    //   0	229	1	paramString	String
    //   11	182	2	localKeyChainConnection	KeyChainConnection
    //   13	101	3	localCollection	Collection
    //   20	33	4	localIKeyChainService	IKeyChainService
    //   32	39	5	arrayOfByte	byte[]
    // Exception table:
    //   from	to	target	type
    //   70	76	168	java/security/cert/CertificateException
    //   70	76	168	java/lang/RuntimeException
    //   80	133	168	java/security/cert/CertificateException
    //   80	133	168	java/lang/RuntimeException
    //   133	166	168	java/security/cert/CertificateException
    //   133	166	168	java/lang/RuntimeException
    //   16	22	178	finally
    //   24	34	178	finally
    //   52	61	178	finally
    //   185	187	178	finally
    //   16	22	182	java/lang/Throwable
    //   24	34	182	java/lang/Throwable
    //   52	61	182	java/lang/Throwable
    //   4	12	198	java/lang/RuntimeException
    //   43	48	198	java/lang/RuntimeException
    //   65	70	198	java/lang/RuntimeException
    //   191	196	198	java/lang/RuntimeException
    //   196	198	198	java/lang/RuntimeException
    //   4	12	208	android/os/RemoteException
    //   43	48	208	android/os/RemoteException
    //   65	70	208	android/os/RemoteException
    //   191	196	208	android/os/RemoteException
    //   196	198	208	android/os/RemoteException
  }
  
  /* Error */
  public static KeyPair getKeyPair(Context paramContext, String paramString)
    throws KeyChainException, InterruptedException
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnull +114 -> 115
    //   4: aload_0
    //   5: ifnull +100 -> 105
    //   8: aload_0
    //   9: invokevirtual 284	android/content/Context:getApplicationContext	()Landroid/content/Context;
    //   12: invokestatic 286	android/security/KeyChain:bind	(Landroid/content/Context;)Landroid/security/KeyChain$KeyChainConnection;
    //   15: astore_2
    //   16: aconst_null
    //   17: astore_0
    //   18: aload_2
    //   19: invokevirtual 290	android/security/KeyChain$KeyChainConnection:getService	()Landroid/security/IKeyChainService;
    //   22: aload_1
    //   23: invokeinterface 355 2 0
    //   28: astore_1
    //   29: aload_2
    //   30: ifnull +8 -> 38
    //   33: aconst_null
    //   34: aload_2
    //   35: invokestatic 296	android/security/KeyChain:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   38: aload_1
    //   39: ifnonnull +5 -> 44
    //   42: aconst_null
    //   43: areturn
    //   44: invokestatic 361	android/security/KeyStore:getInstance	()Landroid/security/KeyStore;
    //   47: aload_1
    //   48: iconst_m1
    //   49: invokestatic 367	android/security/keystore/AndroidKeyStoreProvider:loadAndroidKeyStoreKeyPairFromKeystore	(Landroid/security/KeyStore;Ljava/lang/String;I)Ljava/security/KeyPair;
    //   52: astore_0
    //   53: aload_0
    //   54: areturn
    //   55: astore_0
    //   56: new 274	android/security/KeyChainException
    //   59: dup
    //   60: aload_0
    //   61: invokespecial 345	android/security/KeyChainException:<init>	(Ljava/lang/Throwable;)V
    //   64: athrow
    //   65: astore_1
    //   66: goto +8 -> 74
    //   69: astore_1
    //   70: aload_1
    //   71: astore_0
    //   72: aload_1
    //   73: athrow
    //   74: aload_2
    //   75: ifnull +8 -> 83
    //   78: aload_0
    //   79: aload_2
    //   80: invokestatic 296	android/security/KeyChain:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   83: aload_1
    //   84: athrow
    //   85: astore_0
    //   86: new 274	android/security/KeyChainException
    //   89: dup
    //   90: aload_0
    //   91: invokespecial 345	android/security/KeyChainException:<init>	(Ljava/lang/Throwable;)V
    //   94: athrow
    //   95: astore_0
    //   96: new 274	android/security/KeyChainException
    //   99: dup
    //   100: aload_0
    //   101: invokespecial 345	android/security/KeyChainException:<init>	(Ljava/lang/Throwable;)V
    //   104: athrow
    //   105: new 179	java/lang/NullPointerException
    //   108: dup
    //   109: ldc -75
    //   111: invokespecial 182	java/lang/NullPointerException:<init>	(Ljava/lang/String;)V
    //   114: athrow
    //   115: new 179	java/lang/NullPointerException
    //   118: dup
    //   119: ldc_w 347
    //   122: invokespecial 182	java/lang/NullPointerException:<init>	(Ljava/lang/String;)V
    //   125: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	126	0	paramContext	Context
    //   0	126	1	paramString	String
    //   15	65	2	localKeyChainConnection	KeyChainConnection
    // Exception table:
    //   from	to	target	type
    //   44	53	55	java/lang/RuntimeException
    //   44	53	55	java/security/UnrecoverableKeyException
    //   18	29	65	finally
    //   72	74	65	finally
    //   18	29	69	java/lang/Throwable
    //   8	16	85	java/lang/RuntimeException
    //   33	38	85	java/lang/RuntimeException
    //   78	83	85	java/lang/RuntimeException
    //   83	85	85	java/lang/RuntimeException
    //   8	16	95	android/os/RemoteException
    //   33	38	95	android/os/RemoteException
    //   78	83	95	android/os/RemoteException
    //   83	85	95	android/os/RemoteException
  }
  
  public static PrivateKey getPrivateKey(Context paramContext, String paramString)
    throws KeyChainException, InterruptedException
  {
    paramContext = getKeyPair(paramContext, paramString);
    if (paramContext != null) {
      return paramContext.getPrivate();
    }
    return null;
  }
  
  @Deprecated
  public static boolean isBoundKeyAlgorithm(String paramString)
  {
    if (!isKeyAlgorithmSupported(paramString)) {
      return false;
    }
    return KeyStore.getInstance().isHardwareBacked(paramString);
  }
  
  public static boolean isKeyAlgorithmSupported(String paramString)
  {
    paramString = paramString.toUpperCase(Locale.US);
    boolean bool;
    if ((!"EC".equals(paramString)) && (!"RSA".equals(paramString))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static X509Certificate toCertificate(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte != null) {
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
        throw new AssertionError(paramArrayOfByte);
      }
    }
    throw new IllegalArgumentException("bytes == null");
  }
  
  public static Collection<X509Certificate> toCertificates(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte != null) {
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
        throw new AssertionError(paramArrayOfByte);
      }
    }
    throw new IllegalArgumentException("bytes == null");
  }
  
  private static class AliasResponse
    extends IKeyChainAliasCallback.Stub
  {
    private final KeyChainAliasCallback keyChainAliasResponse;
    
    private AliasResponse(KeyChainAliasCallback paramKeyChainAliasCallback)
    {
      keyChainAliasResponse = paramKeyChainAliasCallback;
    }
    
    public void alias(String paramString)
    {
      keyChainAliasResponse.alias(paramString);
    }
  }
  
  public static class KeyChainConnection
    implements Closeable
  {
    private final Context context;
    private final IKeyChainService service;
    private final ServiceConnection serviceConnection;
    
    protected KeyChainConnection(Context paramContext, ServiceConnection paramServiceConnection, IKeyChainService paramIKeyChainService)
    {
      context = paramContext;
      serviceConnection = paramServiceConnection;
      service = paramIKeyChainService;
    }
    
    public void close()
    {
      context.unbindService(serviceConnection);
    }
    
    public IKeyChainService getService()
    {
      return service;
    }
  }
}
