package com.android.internal.os;

import android.app.ApplicationLoaders;
import android.net.LocalSocket;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebViewFactory;
import android.webkit.WebViewLibraryLoader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

class WebViewZygoteInit
{
  public static final String TAG = "WebViewZygoteInit";
  private static ZygoteServer sServer;
  
  WebViewZygoteInit() {}
  
  /* Error */
  public static void main(String[] paramArrayOfString)
  {
    // Byte code:
    //   0: ldc 16
    //   2: ldc 31
    //   4: invokestatic 37	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   7: pop
    //   8: aconst_null
    //   9: astore_1
    //   10: aload_0
    //   11: arraylength
    //   12: istore_2
    //   13: iconst_0
    //   14: istore_3
    //   15: iload_3
    //   16: iload_2
    //   17: if_icmpge +43 -> 60
    //   20: aload_0
    //   21: iload_3
    //   22: aaload
    //   23: astore 4
    //   25: ldc 16
    //   27: aload 4
    //   29: invokestatic 37	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   32: pop
    //   33: aload 4
    //   35: ldc 39
    //   37: invokevirtual 45	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   40: ifeq +14 -> 54
    //   43: aload 4
    //   45: ldc 39
    //   47: invokevirtual 49	java/lang/String:length	()I
    //   50: invokevirtual 53	java/lang/String:substring	(I)Ljava/lang/String;
    //   53: astore_1
    //   54: iinc 3 1
    //   57: goto -42 -> 15
    //   60: aload_1
    //   61: ifnull +128 -> 189
    //   64: getstatic 59	android/system/OsConstants:PR_SET_NO_NEW_PRIVS	I
    //   67: lconst_1
    //   68: lconst_0
    //   69: lconst_0
    //   70: lconst_0
    //   71: invokestatic 65	android/system/Os:prctl	(IJJJJ)I
    //   74: pop
    //   75: new 11	com/android/internal/os/WebViewZygoteInit$WebViewZygoteServer
    //   78: dup
    //   79: aconst_null
    //   80: invokespecial 68	com/android/internal/os/WebViewZygoteInit$WebViewZygoteServer:<init>	(Lcom/android/internal/os/WebViewZygoteInit$1;)V
    //   83: putstatic 70	com/android/internal/os/WebViewZygoteInit:sServer	Lcom/android/internal/os/ZygoteServer;
    //   86: getstatic 70	com/android/internal/os/WebViewZygoteInit:sServer	Lcom/android/internal/os/ZygoteServer;
    //   89: aload_1
    //   90: invokevirtual 76	com/android/internal/os/ZygoteServer:registerServerSocketAtAbstractName	(Ljava/lang/String;)V
    //   93: new 78	java/lang/StringBuilder
    //   96: astore_0
    //   97: aload_0
    //   98: invokespecial 79	java/lang/StringBuilder:<init>	()V
    //   101: aload_0
    //   102: ldc 81
    //   104: invokevirtual 85	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   107: pop
    //   108: aload_0
    //   109: aload_1
    //   110: invokevirtual 85	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   113: pop
    //   114: aload_0
    //   115: invokevirtual 89	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   118: invokestatic 94	com/android/internal/os/Zygote:nativeAllowFileAcrossFork	(Ljava/lang/String;)V
    //   121: getstatic 70	com/android/internal/os/WebViewZygoteInit:sServer	Lcom/android/internal/os/ZygoteServer;
    //   124: ldc 96
    //   126: getstatic 102	android/os/Build:SUPPORTED_ABIS	[Ljava/lang/String;
    //   129: invokestatic 108	android/text/TextUtils:join	(Ljava/lang/CharSequence;[Ljava/lang/Object;)Ljava/lang/String;
    //   132: invokevirtual 112	com/android/internal/os/ZygoteServer:runSelectLoop	(Ljava/lang/String;)Ljava/lang/Runnable;
    //   135: astore_0
    //   136: getstatic 70	com/android/internal/os/WebViewZygoteInit:sServer	Lcom/android/internal/os/ZygoteServer;
    //   139: invokevirtual 115	com/android/internal/os/ZygoteServer:closeServerSocket	()V
    //   142: aload_0
    //   143: ifnull +9 -> 152
    //   146: aload_0
    //   147: invokeinterface 120 1 0
    //   152: return
    //   153: astore_0
    //   154: goto +15 -> 169
    //   157: astore_0
    //   158: ldc 16
    //   160: ldc 122
    //   162: aload_0
    //   163: invokestatic 126	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   166: pop
    //   167: aload_0
    //   168: athrow
    //   169: getstatic 70	com/android/internal/os/WebViewZygoteInit:sServer	Lcom/android/internal/os/ZygoteServer;
    //   172: invokevirtual 115	com/android/internal/os/ZygoteServer:closeServerSocket	()V
    //   175: aload_0
    //   176: athrow
    //   177: astore_0
    //   178: new 29	java/lang/RuntimeException
    //   181: dup
    //   182: ldc -128
    //   184: aload_0
    //   185: invokespecial 131	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   188: athrow
    //   189: new 29	java/lang/RuntimeException
    //   192: dup
    //   193: ldc -123
    //   195: invokespecial 135	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
    //   198: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	199	0	paramArrayOfString	String[]
    //   9	101	1	str1	String
    //   12	6	2	i	int
    //   14	41	3	j	int
    //   23	21	4	str2	String
    // Exception table:
    //   from	to	target	type
    //   86	136	153	finally
    //   158	169	153	finally
    //   86	136	157	java/lang/RuntimeException
    //   64	75	177	android/system/ErrnoException
  }
  
  private static class WebViewZygoteConnection
    extends ZygoteConnection
  {
    WebViewZygoteConnection(LocalSocket paramLocalSocket, String paramString)
      throws IOException
    {
      super(paramString);
    }
    
    protected void handlePreloadPackage(String paramString1, String paramString2, String paramString3, String paramString4)
    {
      Log.i("WebViewZygoteInit", "Beginning package preload");
      paramString2 = ApplicationLoaders.getDefault().createAndCacheWebViewClassLoader(paramString1, paramString2, paramString4);
      WebViewLibraryLoader.loadNativeLibrary(paramString2, paramString3);
      paramString1 = TextUtils.split(paramString1, File.pathSeparator);
      int i = paramString1.length;
      int j = 0;
      for (int k = 0; k < i; k++) {
        Zygote.nativeAllowFileAcrossFork(paramString1[k]);
      }
      int m = 0;
      boolean bool = false;
      int n = m;
      try
      {
        paramString2 = WebViewFactory.getWebViewProviderClass(paramString2);
        n = m;
        paramString1 = paramString2.getMethod("preloadInZygote", new Class[0]);
        n = m;
        paramString1.setAccessible(true);
        n = m;
        if (paramString1.getReturnType() != Boolean.TYPE)
        {
          n = m;
          Log.e("WebViewZygoteInit", "Unexpected return type: preloadInZygote must return boolean");
          n = bool;
        }
        else
        {
          n = m;
          bool = ((Boolean)paramString2.getMethod("preloadInZygote", new Class[0]).invoke(null, new Object[0])).booleanValue();
          n = bool;
          if (!bool)
          {
            n = bool;
            Log.e("WebViewZygoteInit", "preloadInZygote returned false");
            n = bool;
          }
        }
      }
      catch (ReflectiveOperationException paramString1)
      {
        Log.e("WebViewZygoteInit", "Exception while preloading package", paramString1);
      }
      try
      {
        paramString1 = getSocketOutputStream();
        k = j;
        if (n != 0) {
          k = 1;
        }
        paramString1.writeInt(k);
        Log.i("WebViewZygoteInit", "Package preload done");
        return;
      }
      catch (IOException paramString1)
      {
        throw new IllegalStateException("Error writing to command socket", paramString1);
      }
    }
    
    protected boolean isPreloadComplete()
    {
      return true;
    }
    
    protected void preload() {}
  }
  
  private static class WebViewZygoteServer
    extends ZygoteServer
  {
    private WebViewZygoteServer() {}
    
    protected ZygoteConnection createNewConnection(LocalSocket paramLocalSocket, String paramString)
      throws IOException
    {
      return new WebViewZygoteInit.WebViewZygoteConnection(paramLocalSocket, paramString);
    }
  }
}
