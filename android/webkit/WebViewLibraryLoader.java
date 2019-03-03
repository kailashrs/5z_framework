package android.webkit;

import android.app.ActivityManagerInternal;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.LocalServices;
import dalvik.system.VMRuntime;
import java.io.File;
import java.util.Arrays;

@VisibleForTesting
public class WebViewLibraryLoader
{
  private static final long CHROMIUM_WEBVIEW_DEFAULT_VMSIZE_BYTES = 104857600L;
  private static final String CHROMIUM_WEBVIEW_NATIVE_RELRO_32 = "/data/misc/shared_relro/libwebviewchromium32.relro";
  private static final String CHROMIUM_WEBVIEW_NATIVE_RELRO_64 = "/data/misc/shared_relro/libwebviewchromium64.relro";
  private static final boolean DEBUG = false;
  private static final String LOGTAG = WebViewLibraryLoader.class.getSimpleName();
  private static boolean sAddressSpaceReserved = false;
  
  public WebViewLibraryLoader() {}
  
  static void createRelroFile(boolean paramBoolean, WebViewNativeLibrary paramWebViewNativeLibrary)
  {
    String str1;
    if (paramBoolean) {
      str1 = Build.SUPPORTED_64_BIT_ABIS[0];
    } else {
      str1 = Build.SUPPORTED_32_BIT_ABIS[0];
    }
    Runnable local1 = new Runnable()
    {
      public void run()
      {
        try
        {
          localObject1 = WebViewLibraryLoader.LOGTAG;
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("relro file creator for ");
          ((StringBuilder)localObject2).append(WebViewLibraryLoader.this);
          ((StringBuilder)localObject2).append(" crashed. Proceeding without");
          Log.e((String)localObject1, ((StringBuilder)localObject2).toString());
          WebViewFactory.getUpdateService().notifyRelroCreationCompleted();
        }
        catch (RemoteException localRemoteException)
        {
          Object localObject2 = WebViewLibraryLoader.LOGTAG;
          Object localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Cannot reach WebViewUpdateService. ");
          ((StringBuilder)localObject1).append(localRemoteException.getMessage());
          Log.e((String)localObject2, ((StringBuilder)localObject1).toString());
        }
      }
    };
    if (paramWebViewNativeLibrary != null) {
      try
      {
        if (path != null)
        {
          localObject1 = (ActivityManagerInternal)LocalServices.getService(ActivityManagerInternal.class);
          String str2 = RelroFileCreator.class.getName();
          paramWebViewNativeLibrary = path;
          Object localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("WebViewLoader-");
          ((StringBuilder)localObject2).append(str1);
          localObject2 = ((StringBuilder)localObject2).toString();
          if (((ActivityManagerInternal)localObject1).startIsolatedProcess(str2, new String[] { paramWebViewNativeLibrary }, (String)localObject2, str1, 1037, local1)) {
            return;
          }
          paramWebViewNativeLibrary = new java/lang/Exception;
          paramWebViewNativeLibrary.<init>("Failed to start the relro file creator process");
          throw paramWebViewNativeLibrary;
        }
      }
      catch (Throwable localThrowable) {}
    }
    paramWebViewNativeLibrary = new java/lang/IllegalArgumentException;
    paramWebViewNativeLibrary.<init>("Native library paths to the WebView RelRo process must not be null!");
    throw paramWebViewNativeLibrary;
    Object localObject1 = LOGTAG;
    paramWebViewNativeLibrary = new StringBuilder();
    paramWebViewNativeLibrary.append("error starting relro file creator for abi ");
    paramWebViewNativeLibrary.append(str1);
    Log.e((String)localObject1, paramWebViewNativeLibrary.toString(), localThrowable);
    local1.run();
  }
  
  private static int createRelros(WebViewNativeLibrary paramWebViewNativeLibrary1, WebViewNativeLibrary paramWebViewNativeLibrary2)
  {
    int i = 0;
    int j = i;
    if (Build.SUPPORTED_32_BIT_ABIS.length > 0) {
      if (paramWebViewNativeLibrary1 == null)
      {
        Log.e(LOGTAG, "No 32-bit WebView library path, skipping relro creation.");
        j = i;
      }
      else
      {
        createRelroFile(false, paramWebViewNativeLibrary1);
        j = 0 + 1;
      }
    }
    i = j;
    if (Build.SUPPORTED_64_BIT_ABIS.length > 0) {
      if (paramWebViewNativeLibrary2 == null)
      {
        Log.e(LOGTAG, "No 64-bit WebView library path, skipping relro creation.");
        i = j;
      }
      else
      {
        createRelroFile(true, paramWebViewNativeLibrary2);
        i = j + 1;
      }
    }
    return i;
  }
  
  private static WebViewNativeLibrary findNativeLibrary(ApplicationInfo paramApplicationInfo, String paramString1, String[] paramArrayOfString, String paramString2)
    throws WebViewFactory.MissingWebViewPackageException
  {
    if (TextUtils.isEmpty(paramString2)) {
      return null;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(paramString2);
    ((StringBuilder)localObject).append("/");
    ((StringBuilder)localObject).append(paramString1);
    paramString2 = ((StringBuilder)localObject).toString();
    localObject = new File(paramString2);
    if (((File)localObject).exists()) {
      return new WebViewNativeLibrary(paramString2, ((File)localObject).length());
    }
    return getLoadFromApkPath(sourceDir, paramArrayOfString, paramString1);
  }
  
  /* Error */
  private static WebViewNativeLibrary getLoadFromApkPath(String paramString1, String[] paramArrayOfString, String paramString2)
    throws WebViewFactory.MissingWebViewPackageException
  {
    // Byte code:
    //   0: new 169	java/util/zip/ZipFile
    //   3: astore_3
    //   4: aload_3
    //   5: aload_0
    //   6: invokespecial 170	java/util/zip/ZipFile:<init>	(Ljava/lang/String;)V
    //   9: aconst_null
    //   10: astore 4
    //   12: aload 4
    //   14: astore 5
    //   16: aload_1
    //   17: arraylength
    //   18: istore 6
    //   20: iconst_0
    //   21: istore 7
    //   23: iload 7
    //   25: iload 6
    //   27: if_icmpge +199 -> 226
    //   30: aload_1
    //   31: iload 7
    //   33: aaload
    //   34: astore 8
    //   36: aload 4
    //   38: astore 5
    //   40: new 80	java/lang/StringBuilder
    //   43: astore 9
    //   45: aload 4
    //   47: astore 5
    //   49: aload 9
    //   51: invokespecial 81	java/lang/StringBuilder:<init>	()V
    //   54: aload 4
    //   56: astore 5
    //   58: aload 9
    //   60: ldc -84
    //   62: invokevirtual 87	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   65: pop
    //   66: aload 4
    //   68: astore 5
    //   70: aload 9
    //   72: aload 8
    //   74: invokevirtual 87	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   77: pop
    //   78: aload 4
    //   80: astore 5
    //   82: aload 9
    //   84: ldc -114
    //   86: invokevirtual 87	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   89: pop
    //   90: aload 4
    //   92: astore 5
    //   94: aload 9
    //   96: aload_2
    //   97: invokevirtual 87	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   100: pop
    //   101: aload 4
    //   103: astore 5
    //   105: aload 9
    //   107: invokevirtual 90	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   110: astore 9
    //   112: aload 4
    //   114: astore 5
    //   116: aload_3
    //   117: aload 9
    //   119: invokevirtual 176	java/util/zip/ZipFile:getEntry	(Ljava/lang/String;)Ljava/util/zip/ZipEntry;
    //   122: astore 8
    //   124: aload 8
    //   126: ifnull +94 -> 220
    //   129: aload 4
    //   131: astore 5
    //   133: aload 8
    //   135: invokevirtual 182	java/util/zip/ZipEntry:getMethod	()I
    //   138: ifne +82 -> 220
    //   141: aload 4
    //   143: astore 5
    //   145: new 11	android/webkit/WebViewLibraryLoader$WebViewNativeLibrary
    //   148: astore_2
    //   149: aload 4
    //   151: astore 5
    //   153: new 80	java/lang/StringBuilder
    //   156: astore_1
    //   157: aload 4
    //   159: astore 5
    //   161: aload_1
    //   162: invokespecial 81	java/lang/StringBuilder:<init>	()V
    //   165: aload 4
    //   167: astore 5
    //   169: aload_1
    //   170: aload_0
    //   171: invokevirtual 87	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   174: pop
    //   175: aload 4
    //   177: astore 5
    //   179: aload_1
    //   180: ldc -72
    //   182: invokevirtual 87	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   185: pop
    //   186: aload 4
    //   188: astore 5
    //   190: aload_1
    //   191: aload 9
    //   193: invokevirtual 87	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   196: pop
    //   197: aload 4
    //   199: astore 5
    //   201: aload_2
    //   202: aload_1
    //   203: invokevirtual 90	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   206: aload 8
    //   208: invokevirtual 187	java/util/zip/ZipEntry:getSize	()J
    //   211: invokespecial 155	android/webkit/WebViewLibraryLoader$WebViewNativeLibrary:<init>	(Ljava/lang/String;J)V
    //   214: aload_3
    //   215: invokevirtual 190	java/util/zip/ZipFile:close	()V
    //   218: aload_2
    //   219: areturn
    //   220: iinc 7 1
    //   223: goto -200 -> 23
    //   226: aload_3
    //   227: invokevirtual 190	java/util/zip/ZipFile:close	()V
    //   230: aconst_null
    //   231: areturn
    //   232: astore_0
    //   233: goto +9 -> 242
    //   236: astore_0
    //   237: aload_0
    //   238: astore 5
    //   240: aload_0
    //   241: athrow
    //   242: aload 5
    //   244: ifnull +20 -> 264
    //   247: aload_3
    //   248: invokevirtual 190	java/util/zip/ZipFile:close	()V
    //   251: goto +17 -> 268
    //   254: astore_1
    //   255: aload 5
    //   257: aload_1
    //   258: invokevirtual 194	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   261: goto +7 -> 268
    //   264: aload_3
    //   265: invokevirtual 190	java/util/zip/ZipFile:close	()V
    //   268: aload_0
    //   269: athrow
    //   270: astore_0
    //   271: new 134	android/webkit/WebViewFactory$MissingWebViewPackageException
    //   274: dup
    //   275: aload_0
    //   276: invokespecial 197	android/webkit/WebViewFactory$MissingWebViewPackageException:<init>	(Ljava/lang/Exception;)V
    //   279: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	280	0	paramString1	String
    //   0	280	1	paramArrayOfString	String[]
    //   0	280	2	paramString2	String
    //   3	262	3	localZipFile	java.util.zip.ZipFile
    //   10	188	4	localObject1	Object
    //   14	242	5	localObject2	Object
    //   18	10	6	i	int
    //   21	200	7	j	int
    //   34	173	8	localObject3	Object
    //   43	149	9	localObject4	Object
    // Exception table:
    //   from	to	target	type
    //   16	20	232	finally
    //   40	45	232	finally
    //   49	54	232	finally
    //   58	66	232	finally
    //   70	78	232	finally
    //   82	90	232	finally
    //   94	101	232	finally
    //   105	112	232	finally
    //   116	124	232	finally
    //   133	141	232	finally
    //   145	149	232	finally
    //   153	157	232	finally
    //   161	165	232	finally
    //   169	175	232	finally
    //   179	186	232	finally
    //   190	197	232	finally
    //   201	214	232	finally
    //   240	242	232	finally
    //   16	20	236	java/lang/Throwable
    //   40	45	236	java/lang/Throwable
    //   49	54	236	java/lang/Throwable
    //   58	66	236	java/lang/Throwable
    //   70	78	236	java/lang/Throwable
    //   82	90	236	java/lang/Throwable
    //   94	101	236	java/lang/Throwable
    //   105	112	236	java/lang/Throwable
    //   116	124	236	java/lang/Throwable
    //   133	141	236	java/lang/Throwable
    //   145	149	236	java/lang/Throwable
    //   153	157	236	java/lang/Throwable
    //   161	165	236	java/lang/Throwable
    //   169	175	236	java/lang/Throwable
    //   179	186	236	java/lang/Throwable
    //   190	197	236	java/lang/Throwable
    //   201	214	236	java/lang/Throwable
    //   247	251	254	java/lang/Throwable
    //   0	9	270	java/io/IOException
    //   214	218	270	java/io/IOException
    //   226	230	270	java/io/IOException
    //   247	251	270	java/io/IOException
    //   255	261	270	java/io/IOException
    //   264	268	270	java/io/IOException
    //   268	270	270	java/io/IOException
  }
  
  @VisibleForTesting
  public static WebViewNativeLibrary getWebViewNativeLibrary(PackageInfo paramPackageInfo, boolean paramBoolean)
    throws WebViewFactory.MissingWebViewPackageException
  {
    ApplicationInfo localApplicationInfo = applicationInfo;
    String str1 = WebViewFactory.getWebViewLibrary(localApplicationInfo);
    String str2 = getWebViewNativeLibraryDirectory(localApplicationInfo, paramBoolean);
    if (paramBoolean) {
      paramPackageInfo = Build.SUPPORTED_64_BIT_ABIS;
    } else {
      paramPackageInfo = Build.SUPPORTED_32_BIT_ABIS;
    }
    return findNativeLibrary(localApplicationInfo, str1, paramPackageInfo, str2);
  }
  
  @VisibleForTesting
  public static String getWebViewNativeLibraryDirectory(ApplicationInfo paramApplicationInfo, boolean paramBoolean)
  {
    if (paramBoolean == VMRuntime.is64BitAbi(primaryCpuAbi)) {
      return nativeLibraryDir;
    }
    if (!TextUtils.isEmpty(secondaryCpuAbi)) {
      return secondaryNativeLibraryDir;
    }
    return "";
  }
  
  public static int loadNativeLibrary(ClassLoader paramClassLoader, String paramString)
  {
    if (!sAddressSpaceReserved)
    {
      Log.e(LOGTAG, "can't load with relro file; address space not reserved");
      return 2;
    }
    String str;
    if (VMRuntime.getRuntime().is64Bit()) {
      str = "/data/misc/shared_relro/libwebviewchromium64.relro";
    } else {
      str = "/data/misc/shared_relro/libwebviewchromium32.relro";
    }
    int i = nativeLoadWithRelroFile(paramString, str, paramClassLoader);
    if (i != 0) {
      Log.w(LOGTAG, "failed to load with relro file, proceeding without");
    }
    return i;
  }
  
  static native boolean nativeCreateRelroFile(String paramString1, String paramString2);
  
  static native int nativeLoadWithRelroFile(String paramString1, String paramString2, ClassLoader paramClassLoader);
  
  static native boolean nativeReserveAddressSpace(long paramLong);
  
  static int prepareNativeLibraries(PackageInfo paramPackageInfo)
    throws WebViewFactory.MissingWebViewPackageException
  {
    WebViewNativeLibrary localWebViewNativeLibrary = getWebViewNativeLibrary(paramPackageInfo, false);
    paramPackageInfo = getWebViewNativeLibrary(paramPackageInfo, true);
    updateWebViewZygoteVmSize(localWebViewNativeLibrary, paramPackageInfo);
    return createRelros(localWebViewNativeLibrary, paramPackageInfo);
  }
  
  static void reserveAddressSpaceInZygote()
  {
    System.loadLibrary("webviewchromium_loader");
    long l = SystemProperties.getLong("persist.sys.webview.vmsize", 104857600L);
    sAddressSpaceReserved = nativeReserveAddressSpace(l);
    if (!sAddressSpaceReserved)
    {
      String str = LOGTAG;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("reserving ");
      localStringBuilder.append(l);
      localStringBuilder.append(" bytes of address space failed");
      Log.e(str, localStringBuilder.toString());
    }
  }
  
  private static void setWebViewZygoteVmSize(long paramLong)
  {
    SystemProperties.set("persist.sys.webview.vmsize", Long.toString(paramLong));
  }
  
  private static void updateWebViewZygoteVmSize(WebViewNativeLibrary paramWebViewNativeLibrary1, WebViewNativeLibrary paramWebViewNativeLibrary2)
    throws WebViewFactory.MissingWebViewPackageException
  {
    long l1 = 0L;
    if (paramWebViewNativeLibrary1 != null) {
      l1 = Math.max(0L, size);
    }
    long l2 = l1;
    if (paramWebViewNativeLibrary2 != null) {
      l2 = Math.max(l1, size);
    }
    l1 = Math.max(2L * l2, 104857600L);
    paramWebViewNativeLibrary1 = LOGTAG;
    paramWebViewNativeLibrary2 = new StringBuilder();
    paramWebViewNativeLibrary2.append("Setting new address space to ");
    paramWebViewNativeLibrary2.append(l1);
    Log.d(paramWebViewNativeLibrary1, paramWebViewNativeLibrary2.toString());
    setWebViewZygoteVmSize(l1);
  }
  
  private static class RelroFileCreator
  {
    private RelroFileCreator() {}
    
    public static void main(String[] paramArrayOfString)
    {
      boolean bool = VMRuntime.getRuntime().is64Bit();
      try
      {
        if ((paramArrayOfString.length == 1) && (paramArrayOfString[0] != null))
        {
          localObject1 = WebViewLibraryLoader.LOGTAG;
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("RelroFileCreator (64bit = ");
          ((StringBuilder)localObject2).append(bool);
          ((StringBuilder)localObject2).append("), lib: ");
          ((StringBuilder)localObject2).append(paramArrayOfString[0]);
          Log.v((String)localObject1, ((StringBuilder)localObject2).toString());
          if (!WebViewLibraryLoader.sAddressSpaceReserved)
          {
            Log.e(WebViewLibraryLoader.LOGTAG, "can't create relro file; address space not reserved");
            return;
          }
          localObject2 = paramArrayOfString[0];
          if (bool) {
            paramArrayOfString = "/data/misc/shared_relro/libwebviewchromium64.relro";
          } else {
            paramArrayOfString = "/data/misc/shared_relro/libwebviewchromium32.relro";
          }
          bool = WebViewLibraryLoader.nativeCreateRelroFile((String)localObject2, paramArrayOfString);
          try
          {
            WebViewFactory.getUpdateServiceUnchecked().notifyRelroCreationCompleted();
          }
          catch (RemoteException paramArrayOfString)
          {
            Log.e(WebViewLibraryLoader.LOGTAG, "error notifying update service", paramArrayOfString);
          }
          if (!bool) {
            Log.e(WebViewLibraryLoader.LOGTAG, "failed to create relro file");
          }
          System.exit(0);
          return;
        }
        Object localObject2 = WebViewLibraryLoader.LOGTAG;
        Object localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("Invalid RelroFileCreator args: ");
        ((StringBuilder)localObject1).append(Arrays.toString(paramArrayOfString));
        Log.e((String)localObject2, ((StringBuilder)localObject1).toString());
        return;
      }
      finally
      {
        try
        {
          WebViewFactory.getUpdateServiceUnchecked().notifyRelroCreationCompleted();
        }
        catch (RemoteException localRemoteException)
        {
          Log.e(WebViewLibraryLoader.LOGTAG, "error notifying update service", localRemoteException);
        }
        if (0 == 0) {
          Log.e(WebViewLibraryLoader.LOGTAG, "failed to create relro file");
        }
        System.exit(0);
      }
    }
  }
  
  @VisibleForTesting
  public static class WebViewNativeLibrary
  {
    public final String path;
    public final long size;
    
    WebViewNativeLibrary(String paramString, long paramLong)
    {
      path = paramString;
      size = paramLong;
    }
  }
}
