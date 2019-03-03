package android.webkit;

import android.annotation.SystemApi;
import android.app.AppGlobals;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.ArraySet;
import android.util.Log;
import java.io.File;

@SystemApi
public final class WebViewFactory
{
  private static final String CHROMIUM_WEBVIEW_FACTORY = "com.android.webview.chromium.WebViewChromiumFactoryProviderForP";
  private static final String CHROMIUM_WEBVIEW_FACTORY_METHOD = "create";
  public static final String CHROMIUM_WEBVIEW_VMSIZE_SIZE_PROPERTY = "persist.sys.webview.vmsize";
  private static final boolean DEBUG = false;
  public static final int LIBLOAD_ADDRESS_SPACE_NOT_RESERVED = 2;
  public static final int LIBLOAD_FAILED_JNI_CALL = 7;
  public static final int LIBLOAD_FAILED_LISTING_WEBVIEW_PACKAGES = 4;
  public static final int LIBLOAD_FAILED_TO_FIND_NAMESPACE = 10;
  public static final int LIBLOAD_FAILED_TO_LOAD_LIBRARY = 6;
  public static final int LIBLOAD_FAILED_TO_OPEN_RELRO_FILE = 5;
  public static final int LIBLOAD_FAILED_WAITING_FOR_RELRO = 3;
  public static final int LIBLOAD_FAILED_WAITING_FOR_WEBVIEW_REASON_UNKNOWN = 8;
  public static final int LIBLOAD_SUCCESS = 0;
  public static final int LIBLOAD_WRONG_PACKAGE_NAME = 1;
  private static final String LOGTAG = "WebViewFactory";
  private static String WEBVIEW_UPDATE_SERVICE_NAME = "webviewupdate";
  private static String sDataDirectorySuffix;
  private static PackageInfo sPackageInfo;
  private static WebViewFactoryProvider sProviderInstance;
  private static final Object sProviderLock = new Object();
  private static boolean sWebViewDisabled;
  private static Boolean sWebViewSupported;
  
  public WebViewFactory() {}
  
  static void disableWebView()
  {
    synchronized (sProviderLock)
    {
      if (sProviderInstance == null)
      {
        sWebViewDisabled = true;
        return;
      }
      IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
      localIllegalStateException.<init>("Can't disable WebView: WebView already initialized");
      throw localIllegalStateException;
    }
  }
  
  private static void fixupStubApplicationInfo(ApplicationInfo paramApplicationInfo, PackageManager paramPackageManager)
    throws WebViewFactory.MissingWebViewPackageException
  {
    String str = null;
    if (metaData != null) {
      str = metaData.getString("com.android.webview.WebViewDonorPackage");
    }
    if (str != null) {
      try
      {
        paramPackageManager = paramPackageManager.getPackageInfo(str, 270541824);
        paramPackageManager = applicationInfo;
        sourceDir = sourceDir;
        splitSourceDirs = splitSourceDirs;
        nativeLibraryDir = nativeLibraryDir;
        secondaryNativeLibraryDir = secondaryNativeLibraryDir;
        primaryCpuAbi = primaryCpuAbi;
        secondaryCpuAbi = secondaryCpuAbi;
      }
      catch (PackageManager.NameNotFoundException paramApplicationInfo)
      {
        paramApplicationInfo = new StringBuilder();
        paramApplicationInfo.append("Failed to find donor package: ");
        paramApplicationInfo.append(str);
        throw new MissingWebViewPackageException(paramApplicationInfo.toString());
      }
    }
  }
  
  static String getDataDirectorySuffix()
  {
    synchronized (sProviderLock)
    {
      String str = sDataDirectorySuffix;
      return str;
    }
  }
  
  public static PackageInfo getLoadedPackageInfo()
  {
    synchronized (sProviderLock)
    {
      PackageInfo localPackageInfo = sPackageInfo;
      return localPackageInfo;
    }
  }
  
  /* Error */
  static WebViewFactoryProvider getProvider()
  {
    // Byte code:
    //   0: getstatic 62	android/webkit/WebViewFactory:sProviderLock	Ljava/lang/Object;
    //   3: astore_0
    //   4: aload_0
    //   5: monitorenter
    //   6: getstatic 70	android/webkit/WebViewFactory:sProviderInstance	Landroid/webkit/WebViewFactoryProvider;
    //   9: ifnull +11 -> 20
    //   12: getstatic 70	android/webkit/WebViewFactory:sProviderInstance	Landroid/webkit/WebViewFactoryProvider;
    //   15: astore_1
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_1
    //   19: areturn
    //   20: invokestatic 161	android/os/Process:myUid	()I
    //   23: istore_2
    //   24: iload_2
    //   25: ifeq +198 -> 223
    //   28: iload_2
    //   29: sipush 1000
    //   32: if_icmpeq +191 -> 223
    //   35: iload_2
    //   36: sipush 1001
    //   39: if_icmpeq +184 -> 223
    //   42: iload_2
    //   43: sipush 1027
    //   46: if_icmpeq +177 -> 223
    //   49: iload_2
    //   50: sipush 1002
    //   53: if_icmpeq +170 -> 223
    //   56: invokestatic 165	android/webkit/WebViewFactory:isWebViewSupported	()Z
    //   59: ifeq +154 -> 213
    //   62: getstatic 72	android/webkit/WebViewFactory:sWebViewDisabled	Z
    //   65: ifne +136 -> 201
    //   68: ldc2_w 166
    //   71: ldc -87
    //   73: invokestatic 175	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   76: invokestatic 179	android/webkit/WebViewFactory:getProviderClass	()Ljava/lang/Class;
    //   79: astore_3
    //   80: aconst_null
    //   81: astore_1
    //   82: aload_3
    //   83: ldc 15
    //   85: iconst_1
    //   86: anewarray 181	java/lang/Class
    //   89: dup
    //   90: iconst_0
    //   91: ldc -73
    //   93: aastore
    //   94: invokevirtual 187	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   97: astore_3
    //   98: aload_3
    //   99: astore_1
    //   100: goto +4 -> 104
    //   103: astore_3
    //   104: ldc2_w 166
    //   107: ldc -67
    //   109: invokestatic 175	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   112: new 183	android/webkit/WebViewDelegate
    //   115: astore_3
    //   116: aload_3
    //   117: invokespecial 190	android/webkit/WebViewDelegate:<init>	()V
    //   120: aload_1
    //   121: aconst_null
    //   122: iconst_1
    //   123: anewarray 4	java/lang/Object
    //   126: dup
    //   127: iconst_0
    //   128: aload_3
    //   129: aastore
    //   130: invokevirtual 196	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   133: checkcast 198	android/webkit/WebViewFactoryProvider
    //   136: putstatic 70	android/webkit/WebViewFactory:sProviderInstance	Landroid/webkit/WebViewFactoryProvider;
    //   139: getstatic 70	android/webkit/WebViewFactory:sProviderInstance	Landroid/webkit/WebViewFactoryProvider;
    //   142: astore_1
    //   143: ldc2_w 166
    //   146: invokestatic 202	android/os/Trace:traceEnd	(J)V
    //   149: ldc2_w 166
    //   152: invokestatic 202	android/os/Trace:traceEnd	(J)V
    //   155: aload_0
    //   156: monitorexit
    //   157: aload_1
    //   158: areturn
    //   159: astore_1
    //   160: goto +24 -> 184
    //   163: astore_1
    //   164: ldc 44
    //   166: ldc -52
    //   168: aload_1
    //   169: invokestatic 210	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   172: pop
    //   173: new 212	android/util/AndroidRuntimeException
    //   176: astore_3
    //   177: aload_3
    //   178: aload_1
    //   179: invokespecial 215	android/util/AndroidRuntimeException:<init>	(Ljava/lang/Exception;)V
    //   182: aload_3
    //   183: athrow
    //   184: ldc2_w 166
    //   187: invokestatic 202	android/os/Trace:traceEnd	(J)V
    //   190: aload_1
    //   191: athrow
    //   192: astore_1
    //   193: ldc2_w 166
    //   196: invokestatic 202	android/os/Trace:traceEnd	(J)V
    //   199: aload_1
    //   200: athrow
    //   201: new 74	java/lang/IllegalStateException
    //   204: astore_1
    //   205: aload_1
    //   206: ldc -39
    //   208: invokespecial 79	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   211: aload_1
    //   212: athrow
    //   213: new 219	java/lang/UnsupportedOperationException
    //   216: astore_1
    //   217: aload_1
    //   218: invokespecial 220	java/lang/UnsupportedOperationException:<init>	()V
    //   221: aload_1
    //   222: athrow
    //   223: new 219	java/lang/UnsupportedOperationException
    //   226: astore_1
    //   227: aload_1
    //   228: ldc -34
    //   230: invokespecial 223	java/lang/UnsupportedOperationException:<init>	(Ljava/lang/String;)V
    //   233: aload_1
    //   234: athrow
    //   235: astore_1
    //   236: aload_0
    //   237: monitorexit
    //   238: aload_1
    //   239: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   3	234	0	localObject1	Object
    //   15	143	1	localObject2	Object
    //   159	1	1	localObject3	Object
    //   163	28	1	localException1	Exception
    //   192	8	1	localObject4	Object
    //   204	30	1	localObject5	Object
    //   235	4	1	localObject6	Object
    //   23	31	2	i	int
    //   79	20	3	localObject7	Object
    //   103	1	3	localException2	Exception
    //   115	68	3	localObject8	Object
    // Exception table:
    //   from	to	target	type
    //   82	98	103	java/lang/Exception
    //   112	143	159	finally
    //   164	184	159	finally
    //   112	143	163	java/lang/Exception
    //   76	80	192	finally
    //   82	98	192	finally
    //   104	112	192	finally
    //   143	149	192	finally
    //   184	192	192	finally
    //   6	18	235	finally
    //   20	24	235	finally
    //   56	76	235	finally
    //   149	157	235	finally
    //   193	201	235	finally
    //   201	213	235	finally
    //   213	223	235	finally
    //   223	235	235	finally
    //   236	238	235	finally
  }
  
  /* Error */
  private static Class<WebViewFactoryProvider> getProviderClass()
  {
    // Byte code:
    //   0: invokestatic 231	android/app/AppGlobals:getInitialApplication	()Landroid/app/Application;
    //   3: astore_0
    //   4: ldc2_w 166
    //   7: ldc -23
    //   9: invokestatic 175	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   12: invokestatic 237	android/webkit/WebViewFactory:getWebViewContextAndSetProvider	()Landroid/content/Context;
    //   15: astore_1
    //   16: ldc2_w 166
    //   19: invokestatic 202	android/os/Trace:traceEnd	(J)V
    //   22: new 131	java/lang/StringBuilder
    //   25: astore_2
    //   26: aload_2
    //   27: invokespecial 132	java/lang/StringBuilder:<init>	()V
    //   30: aload_2
    //   31: ldc -17
    //   33: invokevirtual 138	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   36: pop
    //   37: aload_2
    //   38: getstatic 151	android/webkit/WebViewFactory:sPackageInfo	Landroid/content/pm/PackageInfo;
    //   41: getfield 242	android/content/pm/PackageInfo:packageName	Ljava/lang/String;
    //   44: invokevirtual 138	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   47: pop
    //   48: aload_2
    //   49: ldc -12
    //   51: invokevirtual 138	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   54: pop
    //   55: aload_2
    //   56: getstatic 151	android/webkit/WebViewFactory:sPackageInfo	Landroid/content/pm/PackageInfo;
    //   59: getfield 247	android/content/pm/PackageInfo:versionName	Ljava/lang/String;
    //   62: invokevirtual 138	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   65: pop
    //   66: aload_2
    //   67: ldc -7
    //   69: invokevirtual 138	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   72: pop
    //   73: aload_2
    //   74: getstatic 151	android/webkit/WebViewFactory:sPackageInfo	Landroid/content/pm/PackageInfo;
    //   77: invokevirtual 253	android/content/pm/PackageInfo:getLongVersionCode	()J
    //   80: invokevirtual 256	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   83: pop
    //   84: aload_2
    //   85: ldc_w 258
    //   88: invokevirtual 138	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   91: pop
    //   92: ldc 44
    //   94: aload_2
    //   95: invokevirtual 142	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   98: invokestatic 262	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   101: pop
    //   102: ldc2_w 166
    //   105: ldc_w 264
    //   108: invokestatic 175	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   111: aload_0
    //   112: invokevirtual 270	android/app/Application:getAssets	()Landroid/content/res/AssetManager;
    //   115: aload_1
    //   116: invokevirtual 276	android/content/Context:getApplicationInfo	()Landroid/content/pm/ApplicationInfo;
    //   119: getfield 113	android/content/pm/ApplicationInfo:sourceDir	Ljava/lang/String;
    //   122: invokevirtual 282	android/content/res/AssetManager:addAssetPathAsSharedLibrary	(Ljava/lang/String;)I
    //   125: pop
    //   126: aload_1
    //   127: invokevirtual 286	android/content/Context:getClassLoader	()Ljava/lang/ClassLoader;
    //   130: astore_0
    //   131: ldc2_w 166
    //   134: ldc_w 288
    //   137: invokestatic 175	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   140: aload_0
    //   141: getstatic 151	android/webkit/WebViewFactory:sPackageInfo	Landroid/content/pm/PackageInfo;
    //   144: getfield 110	android/content/pm/PackageInfo:applicationInfo	Landroid/content/pm/ApplicationInfo;
    //   147: invokestatic 292	android/webkit/WebViewFactory:getWebViewLibrary	(Landroid/content/pm/ApplicationInfo;)Ljava/lang/String;
    //   150: invokestatic 298	android/webkit/WebViewLibraryLoader:loadNativeLibrary	(Ljava/lang/ClassLoader;Ljava/lang/String;)I
    //   153: pop
    //   154: ldc2_w 166
    //   157: invokestatic 202	android/os/Trace:traceEnd	(J)V
    //   160: ldc2_w 166
    //   163: ldc_w 300
    //   166: invokestatic 175	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   169: aload_0
    //   170: invokestatic 304	android/webkit/WebViewFactory:getWebViewProviderClass	(Ljava/lang/ClassLoader;)Ljava/lang/Class;
    //   173: astore_0
    //   174: ldc2_w 166
    //   177: invokestatic 202	android/os/Trace:traceEnd	(J)V
    //   180: ldc2_w 166
    //   183: invokestatic 202	android/os/Trace:traceEnd	(J)V
    //   186: aload_0
    //   187: areturn
    //   188: astore_0
    //   189: ldc2_w 166
    //   192: invokestatic 202	android/os/Trace:traceEnd	(J)V
    //   195: aload_0
    //   196: athrow
    //   197: astore_0
    //   198: goto +25 -> 223
    //   201: astore_2
    //   202: ldc 44
    //   204: ldc_w 306
    //   207: aload_2
    //   208: invokestatic 210	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   211: pop
    //   212: new 212	android/util/AndroidRuntimeException
    //   215: astore_0
    //   216: aload_0
    //   217: aload_2
    //   218: invokespecial 215	android/util/AndroidRuntimeException:<init>	(Ljava/lang/Exception;)V
    //   221: aload_0
    //   222: athrow
    //   223: ldc2_w 166
    //   226: invokestatic 202	android/os/Trace:traceEnd	(J)V
    //   229: aload_0
    //   230: athrow
    //   231: astore_0
    //   232: ldc2_w 166
    //   235: invokestatic 202	android/os/Trace:traceEnd	(J)V
    //   238: aload_0
    //   239: athrow
    //   240: astore_0
    //   241: ldc 44
    //   243: ldc_w 308
    //   246: aload_0
    //   247: invokestatic 210	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   250: pop
    //   251: new 212	android/util/AndroidRuntimeException
    //   254: dup
    //   255: aload_0
    //   256: invokespecial 215	android/util/AndroidRuntimeException:<init>	(Ljava/lang/Exception;)V
    //   259: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   3	184	0	localObject1	Object
    //   188	8	0	localObject2	Object
    //   197	1	0	localObject3	Object
    //   215	15	0	localAndroidRuntimeException	android.util.AndroidRuntimeException
    //   231	8	0	localObject4	Object
    //   240	16	0	localMissingWebViewPackageException	MissingWebViewPackageException
    //   15	112	1	localContext	android.content.Context
    //   25	70	2	localStringBuilder	StringBuilder
    //   201	17	2	localClassNotFoundException	ClassNotFoundException
    // Exception table:
    //   from	to	target	type
    //   169	174	188	finally
    //   111	169	197	finally
    //   174	180	197	finally
    //   189	197	197	finally
    //   202	223	197	finally
    //   111	169	201	java/lang/ClassNotFoundException
    //   174	180	201	java/lang/ClassNotFoundException
    //   189	197	201	java/lang/ClassNotFoundException
    //   12	16	231	finally
    //   4	12	240	android/webkit/WebViewFactory$MissingWebViewPackageException
    //   16	22	240	android/webkit/WebViewFactory$MissingWebViewPackageException
    //   22	111	240	android/webkit/WebViewFactory$MissingWebViewPackageException
    //   180	186	240	android/webkit/WebViewFactory$MissingWebViewPackageException
    //   223	231	240	android/webkit/WebViewFactory$MissingWebViewPackageException
    //   232	240	240	android/webkit/WebViewFactory$MissingWebViewPackageException
  }
  
  public static IWebViewUpdateService getUpdateService()
  {
    if (isWebViewSupported()) {
      return getUpdateServiceUnchecked();
    }
    return null;
  }
  
  static IWebViewUpdateService getUpdateServiceUnchecked()
  {
    return IWebViewUpdateService.Stub.asInterface(ServiceManager.getService(WEBVIEW_UPDATE_SERVICE_NAME));
  }
  
  /* Error */
  private static android.content.Context getWebViewContextAndSetProvider()
    throws WebViewFactory.MissingWebViewPackageException
  {
    // Byte code:
    //   0: invokestatic 231	android/app/AppGlobals:getInitialApplication	()Landroid/app/Application;
    //   3: astore_0
    //   4: ldc2_w 166
    //   7: ldc_w 331
    //   10: invokestatic 175	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   13: invokestatic 333	android/webkit/WebViewFactory:getUpdateService	()Landroid/webkit/IWebViewUpdateService;
    //   16: invokeinterface 339 1 0
    //   21: astore_1
    //   22: ldc2_w 166
    //   25: invokestatic 202	android/os/Trace:traceEnd	(J)V
    //   28: aload_1
    //   29: getfield 344	android/webkit/WebViewProviderResponse:status	I
    //   32: ifeq +56 -> 88
    //   35: aload_1
    //   36: getfield 344	android/webkit/WebViewProviderResponse:status	I
    //   39: iconst_3
    //   40: if_icmpne +6 -> 46
    //   43: goto +45 -> 88
    //   46: new 6	android/webkit/WebViewFactory$MissingWebViewPackageException
    //   49: astore_2
    //   50: new 131	java/lang/StringBuilder
    //   53: astore_0
    //   54: aload_0
    //   55: invokespecial 132	java/lang/StringBuilder:<init>	()V
    //   58: aload_0
    //   59: ldc_w 346
    //   62: invokevirtual 138	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   65: pop
    //   66: aload_0
    //   67: aload_1
    //   68: getfield 344	android/webkit/WebViewProviderResponse:status	I
    //   71: invokestatic 350	android/webkit/WebViewFactory:getWebViewPreparationErrorReason	(I)Ljava/lang/String;
    //   74: invokevirtual 138	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   77: pop
    //   78: aload_2
    //   79: aload_0
    //   80: invokevirtual 142	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   83: invokespecial 143	android/webkit/WebViewFactory$MissingWebViewPackageException:<init>	(Ljava/lang/String;)V
    //   86: aload_2
    //   87: athrow
    //   88: ldc2_w 166
    //   91: ldc_w 352
    //   94: invokestatic 175	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   97: invokestatic 357	android/app/ActivityManager:getService	()Landroid/app/IActivityManager;
    //   100: aload_1
    //   101: getfield 360	android/webkit/WebViewProviderResponse:packageInfo	Landroid/content/pm/PackageInfo;
    //   104: getfield 242	android/content/pm/PackageInfo:packageName	Ljava/lang/String;
    //   107: invokeinterface 365 2 0
    //   112: ldc2_w 166
    //   115: invokestatic 202	android/os/Trace:traceEnd	(J)V
    //   118: aload_0
    //   119: invokevirtual 369	android/app/Application:getPackageManager	()Landroid/content/pm/PackageManager;
    //   122: astore_3
    //   123: ldc2_w 166
    //   126: ldc_w 371
    //   129: invokestatic 175	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   132: aload_3
    //   133: aload_1
    //   134: getfield 360	android/webkit/WebViewProviderResponse:packageInfo	Landroid/content/pm/PackageInfo;
    //   137: getfield 242	android/content/pm/PackageInfo:packageName	Ljava/lang/String;
    //   140: ldc_w 372
    //   143: invokevirtual 104	android/content/pm/PackageManager:getPackageInfo	(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;
    //   146: astore_2
    //   147: ldc2_w 166
    //   150: invokestatic 202	android/os/Trace:traceEnd	(J)V
    //   153: aload_1
    //   154: getfield 360	android/webkit/WebViewProviderResponse:packageInfo	Landroid/content/pm/PackageInfo;
    //   157: aload_2
    //   158: invokestatic 376	android/webkit/WebViewFactory:verifyPackageInfo	(Landroid/content/pm/PackageInfo;Landroid/content/pm/PackageInfo;)V
    //   161: aload_2
    //   162: getfield 110	android/content/pm/PackageInfo:applicationInfo	Landroid/content/pm/ApplicationInfo;
    //   165: astore_1
    //   166: aload_1
    //   167: aload_3
    //   168: invokestatic 378	android/webkit/WebViewFactory:fixupStubApplicationInfo	(Landroid/content/pm/ApplicationInfo;Landroid/content/pm/PackageManager;)V
    //   171: ldc2_w 166
    //   174: ldc_w 380
    //   177: invokestatic 175	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   180: aload_0
    //   181: aload_1
    //   182: iconst_3
    //   183: invokevirtual 384	android/app/Application:createApplicationContext	(Landroid/content/pm/ApplicationInfo;I)Landroid/content/Context;
    //   186: astore_1
    //   187: aload_2
    //   188: putstatic 151	android/webkit/WebViewFactory:sPackageInfo	Landroid/content/pm/PackageInfo;
    //   191: ldc2_w 166
    //   194: invokestatic 202	android/os/Trace:traceEnd	(J)V
    //   197: aload_1
    //   198: areturn
    //   199: astore_1
    //   200: ldc2_w 166
    //   203: invokestatic 202	android/os/Trace:traceEnd	(J)V
    //   206: aload_1
    //   207: athrow
    //   208: astore_1
    //   209: ldc2_w 166
    //   212: invokestatic 202	android/os/Trace:traceEnd	(J)V
    //   215: aload_1
    //   216: athrow
    //   217: astore_1
    //   218: ldc2_w 166
    //   221: invokestatic 202	android/os/Trace:traceEnd	(J)V
    //   224: aload_1
    //   225: athrow
    //   226: astore_1
    //   227: ldc2_w 166
    //   230: invokestatic 202	android/os/Trace:traceEnd	(J)V
    //   233: aload_1
    //   234: athrow
    //   235: astore_2
    //   236: new 131	java/lang/StringBuilder
    //   239: dup
    //   240: invokespecial 132	java/lang/StringBuilder:<init>	()V
    //   243: astore_1
    //   244: aload_1
    //   245: ldc_w 346
    //   248: invokevirtual 138	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   251: pop
    //   252: aload_1
    //   253: aload_2
    //   254: invokevirtual 387	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   257: pop
    //   258: new 6	android/webkit/WebViewFactory$MissingWebViewPackageException
    //   261: dup
    //   262: aload_1
    //   263: invokevirtual 142	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   266: invokespecial 143	android/webkit/WebViewFactory$MissingWebViewPackageException:<init>	(Ljava/lang/String;)V
    //   269: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   3	178	0	localObject1	Object
    //   21	177	1	localObject2	Object
    //   199	8	1	localObject3	Object
    //   208	8	1	localObject4	Object
    //   217	8	1	localObject5	Object
    //   226	8	1	localObject6	Object
    //   243	20	1	localStringBuilder	StringBuilder
    //   49	139	2	localObject7	Object
    //   235	19	2	localRemoteException	RemoteException
    //   122	46	3	localPackageManager	PackageManager
    // Exception table:
    //   from	to	target	type
    //   180	191	199	finally
    //   132	147	208	finally
    //   97	112	217	finally
    //   13	22	226	finally
    //   4	13	235	android/os/RemoteException
    //   4	13	235	android/content/pm/PackageManager$NameNotFoundException
    //   22	28	235	android/os/RemoteException
    //   22	28	235	android/content/pm/PackageManager$NameNotFoundException
    //   28	43	235	android/os/RemoteException
    //   28	43	235	android/content/pm/PackageManager$NameNotFoundException
    //   46	88	235	android/os/RemoteException
    //   46	88	235	android/content/pm/PackageManager$NameNotFoundException
    //   88	97	235	android/os/RemoteException
    //   88	97	235	android/content/pm/PackageManager$NameNotFoundException
    //   112	118	235	android/os/RemoteException
    //   112	118	235	android/content/pm/PackageManager$NameNotFoundException
    //   118	132	235	android/os/RemoteException
    //   118	132	235	android/content/pm/PackageManager$NameNotFoundException
    //   147	153	235	android/os/RemoteException
    //   147	153	235	android/content/pm/PackageManager$NameNotFoundException
    //   153	180	235	android/os/RemoteException
    //   153	180	235	android/content/pm/PackageManager$NameNotFoundException
    //   191	197	235	android/os/RemoteException
    //   191	197	235	android/content/pm/PackageManager$NameNotFoundException
    //   200	208	235	android/os/RemoteException
    //   200	208	235	android/content/pm/PackageManager$NameNotFoundException
    //   209	217	235	android/os/RemoteException
    //   209	217	235	android/content/pm/PackageManager$NameNotFoundException
    //   218	226	235	android/os/RemoteException
    //   218	226	235	android/content/pm/PackageManager$NameNotFoundException
    //   227	235	235	android/os/RemoteException
    //   227	235	235	android/content/pm/PackageManager$NameNotFoundException
  }
  
  public static String getWebViewLibrary(ApplicationInfo paramApplicationInfo)
  {
    if (metaData != null) {
      return metaData.getString("com.android.webview.WebViewLibrary");
    }
    return null;
  }
  
  private static String getWebViewPreparationErrorReason(int paramInt)
  {
    if (paramInt != 8)
    {
      switch (paramInt)
      {
      default: 
        return "Unknown";
      case 4: 
        return "No WebView installed";
      }
      return "Time out waiting for Relro files being created";
    }
    return "Crashed for unknown reason";
  }
  
  public static Class<WebViewFactoryProvider> getWebViewProviderClass(ClassLoader paramClassLoader)
    throws ClassNotFoundException
  {
    return Class.forName("com.android.webview.chromium.WebViewChromiumFactoryProviderForP", true, paramClassLoader);
  }
  
  private static boolean isWebViewSupported()
  {
    if (sWebViewSupported == null) {
      sWebViewSupported = Boolean.valueOf(AppGlobals.getInitialApplication().getPackageManager().hasSystemFeature("android.software.webview"));
    }
    return sWebViewSupported.booleanValue();
  }
  
  public static int loadWebViewNativeLibraryFromPackage(String paramString, ClassLoader paramClassLoader)
  {
    if (!isWebViewSupported()) {
      return 1;
    }
    try
    {
      WebViewProviderResponse localWebViewProviderResponse = getUpdateService().waitForAndGetProvider();
      if ((status != 0) && (status != 3)) {
        return status;
      }
      if (!packageInfo.packageName.equals(paramString)) {
        return 1;
      }
      Object localObject = AppGlobals.getInitialApplication().getPackageManager();
      try
      {
        localObject = getWebViewLibrary(getPackageInfo268435584applicationInfo);
        int i = WebViewLibraryLoader.loadNativeLibrary(paramClassLoader, (String)localObject);
        if (i == 0) {
          return status;
        }
        return i;
      }
      catch (PackageManager.NameNotFoundException paramClassLoader)
      {
        paramClassLoader = new StringBuilder();
        paramClassLoader.append("Couldn't find package ");
        paramClassLoader.append(paramString);
        Log.e("WebViewFactory", paramClassLoader.toString());
        return 1;
      }
      return 8;
    }
    catch (RemoteException paramString)
    {
      Log.e("WebViewFactory", "error waiting for relro creation", paramString);
    }
  }
  
  public static int onWebViewProviderChanged(PackageInfo paramPackageInfo)
  {
    int i = 0;
    ApplicationInfo localApplicationInfo = new ApplicationInfo(applicationInfo);
    try
    {
      fixupStubApplicationInfo(applicationInfo, AppGlobals.getInitialApplication().getPackageManager());
      int j = WebViewLibraryLoader.prepareNativeLibraries(paramPackageInfo);
      i = j;
    }
    catch (Throwable localThrowable)
    {
      Log.e("WebViewFactory", "error preparing webview native library", localThrowable);
    }
    WebViewZygote.onWebViewProviderChanged(paramPackageInfo, localApplicationInfo);
    return i;
  }
  
  public static void prepareWebViewInZygote()
  {
    try
    {
      
    }
    catch (Throwable localThrowable)
    {
      Log.e("WebViewFactory", "error preparing native loader", localThrowable);
    }
  }
  
  static void setDataDirectorySuffix(String paramString)
  {
    synchronized (sProviderLock)
    {
      if (sProviderInstance == null)
      {
        if (paramString.indexOf(File.separatorChar) < 0)
        {
          sDataDirectorySuffix = paramString;
          return;
        }
        IllegalArgumentException localIllegalArgumentException = new java/lang/IllegalArgumentException;
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Suffix ");
        localStringBuilder.append(paramString);
        localStringBuilder.append(" contains a path separator");
        localIllegalArgumentException.<init>(localStringBuilder.toString());
        throw localIllegalArgumentException;
      }
      paramString = new java/lang/IllegalStateException;
      paramString.<init>("Can't set data directory suffix: WebView already initialized");
      throw paramString;
    }
  }
  
  private static boolean signaturesEquals(Signature[] paramArrayOfSignature1, Signature[] paramArrayOfSignature2)
  {
    int i = 0;
    boolean bool = false;
    if (paramArrayOfSignature1 == null)
    {
      if (paramArrayOfSignature2 == null) {
        bool = true;
      }
      return bool;
    }
    if (paramArrayOfSignature2 == null) {
      return false;
    }
    ArraySet localArraySet = new ArraySet();
    int j = paramArrayOfSignature1.length;
    for (int k = 0; k < j; k++) {
      localArraySet.add(paramArrayOfSignature1[k]);
    }
    paramArrayOfSignature1 = new ArraySet();
    j = paramArrayOfSignature2.length;
    for (k = i; k < j; k++) {
      paramArrayOfSignature1.add(paramArrayOfSignature2[k]);
    }
    return localArraySet.equals(paramArrayOfSignature1);
  }
  
  private static void verifyPackageInfo(PackageInfo paramPackageInfo1, PackageInfo paramPackageInfo2)
    throws WebViewFactory.MissingWebViewPackageException
  {
    if (packageName.equals(packageName))
    {
      if (paramPackageInfo1.getLongVersionCode() <= paramPackageInfo2.getLongVersionCode())
      {
        if (getWebViewLibrary(applicationInfo) != null)
        {
          if (signaturesEquals(signatures, signatures)) {
            return;
          }
          throw new MissingWebViewPackageException("Failed to verify WebView provider, signature mismatch");
        }
        paramPackageInfo1 = new StringBuilder();
        paramPackageInfo1.append("Tried to load an invalid WebView provider: ");
        paramPackageInfo1.append(packageName);
        throw new MissingWebViewPackageException(paramPackageInfo1.toString());
      }
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Failed to verify WebView provider, version code is lower than expected: ");
      localStringBuilder.append(paramPackageInfo1.getLongVersionCode());
      localStringBuilder.append(" actual: ");
      localStringBuilder.append(paramPackageInfo2.getLongVersionCode());
      throw new MissingWebViewPackageException(localStringBuilder.toString());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Failed to verify WebView provider, packageName mismatch, expected: ");
    localStringBuilder.append(packageName);
    localStringBuilder.append(" actual: ");
    localStringBuilder.append(packageName);
    throw new MissingWebViewPackageException(localStringBuilder.toString());
  }
  
  static class MissingWebViewPackageException
    extends Exception
  {
    public MissingWebViewPackageException(Exception paramException)
    {
      super();
    }
    
    public MissingWebViewPackageException(String paramString)
    {
      super();
    }
  }
}
