package com.android.internal.os;

import android.net.Credentials;
import android.net.LocalSocket;
import android.os.FactoryTest;
import android.os.Process;
import android.os.SystemProperties;
import android.os.Trace;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructPollfd;
import android.util.Log;
import dalvik.system.VMRuntime;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import libcore.io.IoUtils;

class ZygoteConnection
{
  private static final String TAG = "Zygote";
  private static final int[][] intArray2d = new int[0][0];
  private final String abiList;
  private boolean isEof;
  private final LocalSocket mSocket;
  private final DataOutputStream mSocketOutStream;
  private final BufferedReader mSocketReader;
  private final Credentials peer;
  
  ZygoteConnection(LocalSocket paramLocalSocket, String paramString)
    throws IOException
  {
    mSocket = paramLocalSocket;
    abiList = paramString;
    mSocketOutStream = new DataOutputStream(paramLocalSocket.getOutputStream());
    mSocketReader = new BufferedReader(new InputStreamReader(paramLocalSocket.getInputStream()), 256);
    mSocket.setSoTimeout(1000);
    try
    {
      peer = mSocket.getPeerCredentials();
      isEof = false;
      return;
    }
    catch (IOException paramLocalSocket)
    {
      Log.e("Zygote", "Cannot read peer credentials", paramLocalSocket);
      throw paramLocalSocket;
    }
  }
  
  public static void applyDebuggerSystemProperty(Arguments paramArguments)
  {
    if (RoSystemProperties.DEBUGGABLE) {
      runtimeFlags |= 0x1;
    }
  }
  
  private static void applyInvokeWithSecurityPolicy(Arguments paramArguments, Credentials paramCredentials)
    throws ZygoteSecurityException
  {
    int i = paramCredentials.getUid();
    if ((invokeWith != null) && (i != 0) && ((runtimeFlags & 0x1) == 0)) {
      throw new ZygoteSecurityException("Peer is permitted to specify anexplicit invoke-with wrapper command only for debuggableapplications.");
    }
  }
  
  public static void applyInvokeWithSystemProperty(Arguments paramArguments)
  {
    if ((invokeWith == null) && (niceName != null))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("wrap.");
      localStringBuilder.append(niceName);
      invokeWith = SystemProperties.get(localStringBuilder.toString());
      if ((invokeWith != null) && (invokeWith.length() == 0)) {
        invokeWith = null;
      }
    }
  }
  
  private static void applyUidSecurityPolicy(Arguments paramArguments, Credentials paramCredentials)
    throws ZygoteSecurityException
  {
    if (paramCredentials.getUid() == 1000)
    {
      int i;
      if (FactoryTest.getMode() == 0) {
        i = 1;
      } else {
        i = 0;
      }
      if ((i != 0) && (uidSpecified) && (uid < 1000)) {
        throw new ZygoteSecurityException("System UID may not launch process with UID < 1000");
      }
    }
    if (!uidSpecified)
    {
      uid = paramCredentials.getUid();
      uidSpecified = true;
    }
    if (!gidSpecified)
    {
      gid = paramCredentials.getGid();
      gidSpecified = true;
    }
  }
  
  private void handleAbiListQuery()
  {
    try
    {
      byte[] arrayOfByte = abiList.getBytes(StandardCharsets.US_ASCII);
      mSocketOutStream.writeInt(arrayOfByte.length);
      mSocketOutStream.write(arrayOfByte);
      return;
    }
    catch (IOException localIOException)
    {
      throw new IllegalStateException("Error writing to command socket", localIOException);
    }
  }
  
  private void handleApiBlacklistExemptions(String[] paramArrayOfString)
  {
    try
    {
      ZygoteInit.setApiBlacklistExemptions(paramArrayOfString);
      mSocketOutStream.writeInt(0);
      return;
    }
    catch (IOException paramArrayOfString)
    {
      throw new IllegalStateException("Error writing to command socket", paramArrayOfString);
    }
  }
  
  private Runnable handleChildProc(Arguments paramArguments, FileDescriptor[] paramArrayOfFileDescriptor, FileDescriptor paramFileDescriptor, boolean paramBoolean)
  {
    closeSocket();
    if (paramArrayOfFileDescriptor != null)
    {
      int i = 0;
      try
      {
        Os.dup2(paramArrayOfFileDescriptor[0], OsConstants.STDIN_FILENO);
        Os.dup2(paramArrayOfFileDescriptor[1], OsConstants.STDOUT_FILENO);
        Os.dup2(paramArrayOfFileDescriptor[2], OsConstants.STDERR_FILENO);
        int j = paramArrayOfFileDescriptor.length;
        while (i < j)
        {
          IoUtils.closeQuietly(paramArrayOfFileDescriptor[i]);
          i++;
        }
      }
      catch (ErrnoException paramArrayOfFileDescriptor)
      {
        Log.e("Zygote", "Error reopening stdio", paramArrayOfFileDescriptor);
      }
    }
    if (niceName != null) {
      Process.setArgV0(niceName);
    }
    Trace.traceEnd(64L);
    if (invokeWith == null)
    {
      if (!paramBoolean) {
        return ZygoteInit.zygoteInit(targetSdkVersion, remainingArgs, null);
      }
      return ZygoteInit.childZygoteInit(targetSdkVersion, remainingArgs, null);
    }
    WrapperInit.execApplication(invokeWith, niceName, targetSdkVersion, VMRuntime.getCurrentInstructionSet(), paramFileDescriptor, remainingArgs);
    throw new IllegalStateException("WrapperInit.execApplication unexpectedly returned");
  }
  
  private void handleHiddenApiAccessLogSampleRate(int paramInt)
  {
    try
    {
      ZygoteInit.setHiddenApiAccessLogSampleRate(paramInt);
      mSocketOutStream.writeInt(0);
      return;
    }
    catch (IOException localIOException)
    {
      throw new IllegalStateException("Error writing to command socket", localIOException);
    }
  }
  
  private void handleParentProc(int paramInt, FileDescriptor[] paramArrayOfFileDescriptor, FileDescriptor paramFileDescriptor)
  {
    if (paramInt > 0) {
      setChildPgid(paramInt);
    }
    int i;
    int j;
    if (paramArrayOfFileDescriptor != null)
    {
      i = paramArrayOfFileDescriptor.length;
      for (j = 0; j < i; j++) {
        IoUtils.closeQuietly(paramArrayOfFileDescriptor[j]);
      }
    }
    boolean bool = false;
    if ((paramFileDescriptor != null) && (paramInt > 0))
    {
      int k = -1;
      i = k;
      try
      {
        Object localObject = new StructPollfd[1];
        i = k;
        paramArrayOfFileDescriptor = new android/system/StructPollfd;
        i = k;
        paramArrayOfFileDescriptor.<init>();
        localObject[0] = paramArrayOfFileDescriptor;
        i = k;
        paramArrayOfFileDescriptor = new byte[4];
        int m = 30000;
        int n = 0;
        i = k;
        long l = System.nanoTime();
        for (;;)
        {
          i = k;
          if ((n >= paramArrayOfFileDescriptor.length) || (m <= 0)) {
            break;
          }
          i = k;
          0fd = paramFileDescriptor;
          i = k;
          0events = ((short)(short)OsConstants.POLLIN);
          i = k;
          0revents = ((short)0);
          i = k;
          0userData = null;
          i = k;
          int i1 = Os.poll((StructPollfd[])localObject, m);
          i = k;
          m = 30000 - (int)((System.nanoTime() - l) / 1000000L);
          if (i1 > 0)
          {
            i = k;
            if ((0revents & OsConstants.POLLIN) == 0) {
              break;
            }
            i = k;
            j = Os.read(paramFileDescriptor, paramArrayOfFileDescriptor, n, 1);
            if (j >= 0)
            {
              j = n + j;
            }
            else
            {
              i = k;
              paramArrayOfFileDescriptor = new java/lang/RuntimeException;
              i = k;
              paramArrayOfFileDescriptor.<init>("Some error");
              i = k;
              throw paramArrayOfFileDescriptor;
            }
          }
          else
          {
            j = n;
            if (i1 == 0)
            {
              i = k;
              Log.w("Zygote", "Timed out waiting for child.");
              j = n;
            }
          }
          n = j;
        }
        j = k;
        i = k;
        if (n == paramArrayOfFileDescriptor.length)
        {
          i = k;
          paramFileDescriptor = new java/io/DataInputStream;
          i = k;
          localObject = new java/io/ByteArrayInputStream;
          i = k;
          ((ByteArrayInputStream)localObject).<init>(paramArrayOfFileDescriptor);
          i = k;
          paramFileDescriptor.<init>((InputStream)localObject);
          i = k;
          j = paramFileDescriptor.readInt();
        }
        if (j == -1)
        {
          i = j;
          Log.w("Zygote", "Error reading pid from wrapped process, child may have died");
        }
      }
      catch (Exception paramArrayOfFileDescriptor)
      {
        Log.w("Zygote", "Error reading pid from wrapped process, child may have died", paramArrayOfFileDescriptor);
        j = i;
      }
      if (j > 0)
      {
        for (i = j; (i > 0) && (i != paramInt); i = Process.getParentPid(i)) {}
        if (i > 0)
        {
          paramArrayOfFileDescriptor = new StringBuilder();
          paramArrayOfFileDescriptor.append("Wrapped process has pid ");
          paramArrayOfFileDescriptor.append(j);
          Log.i("Zygote", paramArrayOfFileDescriptor.toString());
          paramInt = j;
          bool = true;
        }
        else
        {
          paramArrayOfFileDescriptor = new StringBuilder();
          paramArrayOfFileDescriptor.append("Wrapped process reported a pid that is not a child of the process that we forked: childPid=");
          paramArrayOfFileDescriptor.append(paramInt);
          paramArrayOfFileDescriptor.append(" innerPid=");
          paramArrayOfFileDescriptor.append(j);
          Log.w("Zygote", paramArrayOfFileDescriptor.toString());
        }
      }
    }
    try
    {
      mSocketOutStream.writeInt(paramInt);
      mSocketOutStream.writeBoolean(bool);
      return;
    }
    catch (IOException paramArrayOfFileDescriptor)
    {
      throw new IllegalStateException("Error writing to command socket", paramArrayOfFileDescriptor);
    }
  }
  
  private void handlePreload()
  {
    try
    {
      if (isPreloadComplete())
      {
        mSocketOutStream.writeInt(1);
      }
      else
      {
        preload();
        mSocketOutStream.writeInt(0);
      }
      return;
    }
    catch (IOException localIOException)
    {
      throw new IllegalStateException("Error writing to command socket", localIOException);
    }
  }
  
  private String[] readArgumentList()
    throws IOException
  {
    try
    {
      Object localObject = mSocketReader.readLine();
      if (localObject == null) {
        return null;
      }
      int i = Integer.parseInt((String)localObject);
      if (i <= 1024)
      {
        localObject = new String[i];
        int j = 0;
        while (j < i)
        {
          localObject[j] = mSocketReader.readLine();
          if (localObject[j] != null) {
            j++;
          } else {
            throw new IOException("truncated request");
          }
        }
        return localObject;
      }
      throw new IOException("max arg count exceeded");
    }
    catch (NumberFormatException localNumberFormatException)
    {
      Log.e("Zygote", "invalid Zygote wire format: non-int at argc");
      throw new IOException("invalid wire format");
    }
  }
  
  private void setChildPgid(int paramInt)
  {
    try
    {
      Os.setpgid(paramInt, Os.getpgid(peer.getPid()));
    }
    catch (ErrnoException localErrnoException)
    {
      Log.i("Zygote", "Zygote: setpgid failed. This is normal if peer is not in our session");
    }
  }
  
  void closeSocket()
  {
    try
    {
      mSocket.close();
    }
    catch (IOException localIOException)
    {
      Log.e("Zygote", "Exception while closing command socket in parent", localIOException);
    }
  }
  
  FileDescriptor getFileDesciptor()
  {
    return mSocket.getFileDescriptor();
  }
  
  protected DataOutputStream getSocketOutputStream()
  {
    return mSocketOutStream;
  }
  
  protected void handlePreloadPackage(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    throw new RuntimeException("Zyogte does not support package preloading");
  }
  
  boolean isClosedByPeer()
  {
    return isEof;
  }
  
  protected boolean isPreloadComplete()
  {
    return ZygoteInit.isPreloadComplete();
  }
  
  protected void preload() {}
  
  /* Error */
  Runnable processOneCommand(ZygoteServer paramZygoteServer)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 437	com/android/internal/os/ZygoteConnection:readArgumentList	()[Ljava/lang/String;
    //   4: astore_2
    //   5: aload_0
    //   6: getfield 38	com/android/internal/os/ZygoteConnection:mSocket	Landroid/net/LocalSocket;
    //   9: invokevirtual 441	android/net/LocalSocket:getAncillaryFileDescriptors	()[Ljava/io/FileDescriptor;
    //   12: astore_3
    //   13: aload_2
    //   14: ifnonnull +10 -> 24
    //   17: aload_0
    //   18: iconst_1
    //   19: putfield 81	com/android/internal/os/ZygoteConnection:isEof	Z
    //   22: aconst_null
    //   23: areturn
    //   24: aconst_null
    //   25: astore 4
    //   27: aconst_null
    //   28: astore 5
    //   30: new 6	com/android/internal/os/ZygoteConnection$Arguments
    //   33: dup
    //   34: aload_2
    //   35: invokespecial 443	com/android/internal/os/ZygoteConnection$Arguments:<init>	([Ljava/lang/String;)V
    //   38: astore 6
    //   40: aload 6
    //   42: getfield 446	com/android/internal/os/ZygoteConnection$Arguments:abiListQuery	Z
    //   45: ifeq +9 -> 54
    //   48: aload_0
    //   49: invokespecial 448	com/android/internal/os/ZygoteConnection:handleAbiListQuery	()V
    //   52: aconst_null
    //   53: areturn
    //   54: aload 6
    //   56: getfield 451	com/android/internal/os/ZygoteConnection$Arguments:preloadDefault	Z
    //   59: ifeq +9 -> 68
    //   62: aload_0
    //   63: invokespecial 453	com/android/internal/os/ZygoteConnection:handlePreload	()V
    //   66: aconst_null
    //   67: areturn
    //   68: aload 6
    //   70: getfield 456	com/android/internal/os/ZygoteConnection$Arguments:preloadPackage	Ljava/lang/String;
    //   73: ifnull +29 -> 102
    //   76: aload_0
    //   77: aload 6
    //   79: getfield 456	com/android/internal/os/ZygoteConnection$Arguments:preloadPackage	Ljava/lang/String;
    //   82: aload 6
    //   84: getfield 459	com/android/internal/os/ZygoteConnection$Arguments:preloadPackageLibs	Ljava/lang/String;
    //   87: aload 6
    //   89: getfield 462	com/android/internal/os/ZygoteConnection$Arguments:preloadPackageLibFileName	Ljava/lang/String;
    //   92: aload 6
    //   94: getfield 465	com/android/internal/os/ZygoteConnection$Arguments:preloadPackageCacheKey	Ljava/lang/String;
    //   97: invokevirtual 467	com/android/internal/os/ZygoteConnection:handlePreloadPackage	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
    //   100: aconst_null
    //   101: areturn
    //   102: aload 6
    //   104: getfield 470	com/android/internal/os/ZygoteConnection$Arguments:apiBlacklistExemptions	[Ljava/lang/String;
    //   107: ifnull +14 -> 121
    //   110: aload_0
    //   111: aload 6
    //   113: getfield 470	com/android/internal/os/ZygoteConnection$Arguments:apiBlacklistExemptions	[Ljava/lang/String;
    //   116: invokespecial 472	com/android/internal/os/ZygoteConnection:handleApiBlacklistExemptions	([Ljava/lang/String;)V
    //   119: aconst_null
    //   120: areturn
    //   121: aload 6
    //   123: getfield 475	com/android/internal/os/ZygoteConnection$Arguments:hiddenApiAccessLogSampleRate	I
    //   126: iconst_m1
    //   127: if_icmpeq +14 -> 141
    //   130: aload_0
    //   131: aload 6
    //   133: getfield 475	com/android/internal/os/ZygoteConnection$Arguments:hiddenApiAccessLogSampleRate	I
    //   136: invokespecial 477	com/android/internal/os/ZygoteConnection:handleHiddenApiAccessLogSampleRate	(I)V
    //   139: aconst_null
    //   140: areturn
    //   141: aload 6
    //   143: getfield 481	com/android/internal/os/ZygoteConnection$Arguments:permittedCapabilities	J
    //   146: lconst_0
    //   147: lcmp
    //   148: ifne +422 -> 570
    //   151: aload 6
    //   153: getfield 484	com/android/internal/os/ZygoteConnection$Arguments:effectiveCapabilities	J
    //   156: lconst_0
    //   157: lcmp
    //   158: ifne +412 -> 570
    //   161: aload 6
    //   163: aload_0
    //   164: getfield 79	com/android/internal/os/ZygoteConnection:peer	Landroid/net/Credentials;
    //   167: invokestatic 486	com/android/internal/os/ZygoteConnection:applyUidSecurityPolicy	(Lcom/android/internal/os/ZygoteConnection$Arguments;Landroid/net/Credentials;)V
    //   170: aload 6
    //   172: aload_0
    //   173: getfield 79	com/android/internal/os/ZygoteConnection:peer	Landroid/net/Credentials;
    //   176: invokestatic 488	com/android/internal/os/ZygoteConnection:applyInvokeWithSecurityPolicy	(Lcom/android/internal/os/ZygoteConnection$Arguments;Landroid/net/Credentials;)V
    //   179: aload 6
    //   181: invokestatic 490	com/android/internal/os/ZygoteConnection:applyDebuggerSystemProperty	(Lcom/android/internal/os/ZygoteConnection$Arguments;)V
    //   184: aload 6
    //   186: invokestatic 492	com/android/internal/os/ZygoteConnection:applyInvokeWithSystemProperty	(Lcom/android/internal/os/ZygoteConnection$Arguments;)V
    //   189: aconst_null
    //   190: astore_2
    //   191: aload 6
    //   193: getfield 496	com/android/internal/os/ZygoteConnection$Arguments:rlimits	Ljava/util/ArrayList;
    //   196: ifnull +18 -> 214
    //   199: aload 6
    //   201: getfield 496	com/android/internal/os/ZygoteConnection$Arguments:rlimits	Ljava/util/ArrayList;
    //   204: getstatic 29	com/android/internal/os/ZygoteConnection:intArray2d	[[I
    //   207: invokevirtual 502	java/util/ArrayList:toArray	([Ljava/lang/Object;)[Ljava/lang/Object;
    //   210: checkcast 27	[[I
    //   213: astore_2
    //   214: aconst_null
    //   215: astore 7
    //   217: aload 6
    //   219: getfield 114	com/android/internal/os/ZygoteConnection$Arguments:invokeWith	Ljava/lang/String;
    //   222: ifnull +78 -> 300
    //   225: getstatic 505	android/system/OsConstants:O_CLOEXEC	I
    //   228: invokestatic 509	android/system/Os:pipe2	(I)[Ljava/io/FileDescriptor;
    //   231: astore 5
    //   233: aload 5
    //   235: iconst_1
    //   236: aaload
    //   237: astore 4
    //   239: aload 5
    //   241: iconst_0
    //   242: aaload
    //   243: astore 5
    //   245: aload 4
    //   247: getstatic 512	android/system/OsConstants:F_SETFD	I
    //   250: iconst_0
    //   251: invokestatic 516	android/system/Os:fcntlInt	(Ljava/io/FileDescriptor;II)I
    //   254: pop
    //   255: aload 4
    //   257: invokevirtual 521	java/io/FileDescriptor:getInt$	()I
    //   260: istore 8
    //   262: aload 5
    //   264: invokevirtual 521	java/io/FileDescriptor:getInt$	()I
    //   267: istore 9
    //   269: iconst_2
    //   270: newarray int
    //   272: dup
    //   273: iconst_0
    //   274: iload 8
    //   276: iastore
    //   277: dup
    //   278: iconst_1
    //   279: iload 9
    //   281: iastore
    //   282: astore 7
    //   284: goto +16 -> 300
    //   287: astore_1
    //   288: new 190	java/lang/IllegalStateException
    //   291: dup
    //   292: ldc_w 523
    //   295: aload_1
    //   296: invokespecial 195	java/lang/IllegalStateException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   299: athrow
    //   300: iconst_2
    //   301: newarray int
    //   303: astore 10
    //   305: aload 10
    //   307: dup
    //   308: iconst_0
    //   309: iconst_m1
    //   310: iastore
    //   311: dup
    //   312: iconst_1
    //   313: iconst_m1
    //   314: iastore
    //   315: pop
    //   316: aload_0
    //   317: getfield 38	com/android/internal/os/ZygoteConnection:mSocket	Landroid/net/LocalSocket;
    //   320: invokevirtual 422	android/net/LocalSocket:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   323: astore 11
    //   325: aload 11
    //   327: ifnull +12 -> 339
    //   330: aload 10
    //   332: iconst_0
    //   333: aload 11
    //   335: invokevirtual 521	java/io/FileDescriptor:getInt$	()I
    //   338: iastore
    //   339: aload_1
    //   340: invokevirtual 528	com/android/internal/os/ZygoteServer:getServerSocketFileDescriptor	()Ljava/io/FileDescriptor;
    //   343: astore 11
    //   345: aload 11
    //   347: ifnull +12 -> 359
    //   350: aload 10
    //   352: iconst_1
    //   353: aload 11
    //   355: invokevirtual 521	java/io/FileDescriptor:getInt$	()I
    //   358: iastore
    //   359: aload 6
    //   361: getfield 159	com/android/internal/os/ZygoteConnection$Arguments:uid	I
    //   364: istore 12
    //   366: aload 6
    //   368: getfield 170	com/android/internal/os/ZygoteConnection$Arguments:gid	I
    //   371: istore 8
    //   373: aload 6
    //   375: getfield 532	com/android/internal/os/ZygoteConnection$Arguments:gids	[I
    //   378: astore 13
    //   380: aload 6
    //   382: getfield 101	com/android/internal/os/ZygoteConnection$Arguments:runtimeFlags	I
    //   385: istore 14
    //   387: aload 6
    //   389: getfield 535	com/android/internal/os/ZygoteConnection$Arguments:mountExternal	I
    //   392: istore 9
    //   394: aload 6
    //   396: getfield 538	com/android/internal/os/ZygoteConnection$Arguments:seInfo	Ljava/lang/String;
    //   399: astore 15
    //   401: aload 6
    //   403: getfield 123	com/android/internal/os/ZygoteConnection$Arguments:niceName	Ljava/lang/String;
    //   406: astore 11
    //   408: aload 6
    //   410: getfield 541	com/android/internal/os/ZygoteConnection$Arguments:startChildZygote	Z
    //   413: istore 16
    //   415: aload 6
    //   417: getfield 544	com/android/internal/os/ZygoteConnection$Arguments:instructionSet	Ljava/lang/String;
    //   420: astore 17
    //   422: aload 6
    //   424: getfield 547	com/android/internal/os/ZygoteConnection$Arguments:appDataDir	Ljava/lang/String;
    //   427: astore 18
    //   429: iload 12
    //   431: iload 8
    //   433: aload 13
    //   435: iload 14
    //   437: aload_2
    //   438: iload 9
    //   440: aload 15
    //   442: aload 11
    //   444: aload 10
    //   446: aload 7
    //   448: iload 16
    //   450: aload 17
    //   452: aload 18
    //   454: invokestatic 553	com/android/internal/os/Zygote:forkAndSpecialize	(II[II[[IILjava/lang/String;Ljava/lang/String;[I[IZLjava/lang/String;Ljava/lang/String;)I
    //   457: istore 9
    //   459: iload 9
    //   461: ifne +61 -> 522
    //   464: aload_1
    //   465: invokevirtual 556	com/android/internal/os/ZygoteServer:setForkChild	()V
    //   468: aload_1
    //   469: invokevirtual 559	com/android/internal/os/ZygoteServer:closeServerSocket	()V
    //   472: aload 5
    //   474: invokestatic 232	libcore/io/IoUtils:closeQuietly	(Ljava/io/FileDescriptor;)V
    //   477: aload 6
    //   479: getfield 541	com/android/internal/os/ZygoteConnection$Arguments:startChildZygote	Z
    //   482: istore 16
    //   484: aload_0
    //   485: aload 6
    //   487: aload_3
    //   488: aload 4
    //   490: iload 16
    //   492: invokespecial 561	com/android/internal/os/ZygoteConnection:handleChildProc	(Lcom/android/internal/os/ZygoteConnection$Arguments;[Ljava/io/FileDescriptor;Ljava/io/FileDescriptor;Z)Ljava/lang/Runnable;
    //   495: astore_1
    //   496: aload 4
    //   498: invokestatic 232	libcore/io/IoUtils:closeQuietly	(Ljava/io/FileDescriptor;)V
    //   501: aconst_null
    //   502: invokestatic 232	libcore/io/IoUtils:closeQuietly	(Ljava/io/FileDescriptor;)V
    //   505: aload_1
    //   506: areturn
    //   507: astore_1
    //   508: goto +4 -> 512
    //   511: astore_1
    //   512: aconst_null
    //   513: astore 5
    //   515: goto +43 -> 558
    //   518: astore_1
    //   519: goto +39 -> 558
    //   522: aload 4
    //   524: astore_1
    //   525: aload 4
    //   527: invokestatic 232	libcore/io/IoUtils:closeQuietly	(Ljava/io/FileDescriptor;)V
    //   530: aconst_null
    //   531: astore_1
    //   532: aload_0
    //   533: iload 9
    //   535: aload_3
    //   536: aload 5
    //   538: invokespecial 563	com/android/internal/os/ZygoteConnection:handleParentProc	(I[Ljava/io/FileDescriptor;Ljava/io/FileDescriptor;)V
    //   541: aconst_null
    //   542: invokestatic 232	libcore/io/IoUtils:closeQuietly	(Ljava/io/FileDescriptor;)V
    //   545: aload 5
    //   547: invokestatic 232	libcore/io/IoUtils:closeQuietly	(Ljava/io/FileDescriptor;)V
    //   550: aconst_null
    //   551: areturn
    //   552: astore_2
    //   553: aload_1
    //   554: astore 4
    //   556: aload_2
    //   557: astore_1
    //   558: aload 4
    //   560: invokestatic 232	libcore/io/IoUtils:closeQuietly	(Ljava/io/FileDescriptor;)V
    //   563: aload 5
    //   565: invokestatic 232	libcore/io/IoUtils:closeQuietly	(Ljava/io/FileDescriptor;)V
    //   568: aload_1
    //   569: athrow
    //   570: new 125	java/lang/StringBuilder
    //   573: dup
    //   574: invokespecial 126	java/lang/StringBuilder:<init>	()V
    //   577: astore_1
    //   578: aload_1
    //   579: ldc_w 565
    //   582: invokevirtual 132	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   585: pop
    //   586: aload_1
    //   587: aload 6
    //   589: getfield 481	com/android/internal/os/ZygoteConnection$Arguments:permittedCapabilities	J
    //   592: invokestatic 571	java/lang/Long:toHexString	(J)Ljava/lang/String;
    //   595: invokevirtual 132	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   598: pop
    //   599: aload_1
    //   600: ldc_w 573
    //   603: invokevirtual 132	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   606: pop
    //   607: aload_1
    //   608: aload 6
    //   610: getfield 484	com/android/internal/os/ZygoteConnection$Arguments:effectiveCapabilities	J
    //   613: invokestatic 571	java/lang/Long:toHexString	(J)Ljava/lang/String;
    //   616: invokevirtual 132	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   619: pop
    //   620: new 105	com/android/internal/os/ZygoteSecurityException
    //   623: dup
    //   624: aload_1
    //   625: invokevirtual 136	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   628: invokespecial 119	com/android/internal/os/ZygoteSecurityException:<init>	(Ljava/lang/String;)V
    //   631: athrow
    //   632: astore_1
    //   633: new 190	java/lang/IllegalStateException
    //   636: dup
    //   637: ldc_w 575
    //   640: aload_1
    //   641: invokespecial 195	java/lang/IllegalStateException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   644: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	645	0	this	ZygoteConnection
    //   0	645	1	paramZygoteServer	ZygoteServer
    //   4	434	2	localObject1	Object
    //   552	5	2	localObject2	Object
    //   12	524	3	arrayOfFileDescriptor	FileDescriptor[]
    //   25	534	4	localObject3	Object
    //   28	536	5	localObject4	Object
    //   38	571	6	localArguments	Arguments
    //   215	232	7	arrayOfInt1	int[]
    //   260	172	8	i	int
    //   267	267	9	j	int
    //   303	142	10	arrayOfInt2	int[]
    //   323	120	11	localObject5	Object
    //   364	66	12	k	int
    //   378	56	13	arrayOfInt3	int[]
    //   385	51	14	m	int
    //   399	42	15	str1	String
    //   413	78	16	bool	boolean
    //   420	31	17	str2	String
    //   427	26	18	str3	String
    // Exception table:
    //   from	to	target	type
    //   225	233	287	android/system/ErrnoException
    //   245	269	287	android/system/ErrnoException
    //   484	496	507	finally
    //   477	484	511	finally
    //   464	477	518	finally
    //   525	530	552	finally
    //   532	541	552	finally
    //   0	13	632	java/io/IOException
  }
  
  static class Arguments
  {
    boolean abiListQuery;
    String[] apiBlacklistExemptions;
    String appDataDir;
    boolean capabilitiesSpecified;
    long effectiveCapabilities;
    int gid = 0;
    boolean gidSpecified;
    int[] gids;
    int hiddenApiAccessLogSampleRate = -1;
    String instructionSet;
    String invokeWith;
    int mountExternal = 0;
    String niceName;
    long permittedCapabilities;
    boolean preloadDefault;
    String preloadPackage;
    String preloadPackageCacheKey;
    String preloadPackageLibFileName;
    String preloadPackageLibs;
    String[] remainingArgs;
    ArrayList<int[]> rlimits;
    int runtimeFlags;
    String seInfo;
    boolean seInfoSpecified;
    boolean startChildZygote;
    int targetSdkVersion;
    boolean targetSdkVersionSpecified;
    int uid = 0;
    boolean uidSpecified;
    
    Arguments(String[] paramArrayOfString)
      throws IllegalArgumentException
    {
      parseArgs(paramArrayOfString);
    }
    
    private void parseArgs(String[] paramArrayOfString)
      throws IllegalArgumentException
    {
      int i = 0;
      int j = 0;
      int k = 1;
      int n;
      int i2;
      Object localObject;
      for (;;)
      {
        int m = paramArrayOfString.length;
        n = 0;
        int i1 = 0;
        i2 = j;
        if (j < m)
        {
          localObject = paramArrayOfString[j];
          if (((String)localObject).equals("--"))
          {
            i2 = j + 1;
          }
          else
          {
            if (((String)localObject).startsWith("--setuid="))
            {
              if (!uidSpecified)
              {
                uidSpecified = true;
                uid = Integer.parseInt(((String)localObject).substring(((String)localObject).indexOf('=') + 1));
              }
              else
              {
                throw new IllegalArgumentException("Duplicate arg specified");
              }
            }
            else if (((String)localObject).startsWith("--setgid="))
            {
              if (!gidSpecified)
              {
                gidSpecified = true;
                gid = Integer.parseInt(((String)localObject).substring(((String)localObject).indexOf('=') + 1));
              }
              else
              {
                throw new IllegalArgumentException("Duplicate arg specified");
              }
            }
            else if (((String)localObject).startsWith("--target-sdk-version="))
            {
              if (!targetSdkVersionSpecified)
              {
                targetSdkVersionSpecified = true;
                targetSdkVersion = Integer.parseInt(((String)localObject).substring(((String)localObject).indexOf('=') + 1));
              }
              else
              {
                throw new IllegalArgumentException("Duplicate target-sdk-version specified");
              }
            }
            else if (((String)localObject).equals("--runtime-args"))
            {
              i = 1;
            }
            else if (((String)localObject).startsWith("--runtime-flags="))
            {
              runtimeFlags = Integer.parseInt(((String)localObject).substring(((String)localObject).indexOf('=') + 1));
            }
            else if (((String)localObject).startsWith("--seinfo="))
            {
              if (!seInfoSpecified)
              {
                seInfoSpecified = true;
                seInfo = ((String)localObject).substring(((String)localObject).indexOf('=') + 1);
              }
              else
              {
                throw new IllegalArgumentException("Duplicate arg specified");
              }
            }
            else if (((String)localObject).startsWith("--capabilities="))
            {
              if (!capabilitiesSpecified)
              {
                capabilitiesSpecified = true;
                localObject = ((String)localObject).substring(((String)localObject).indexOf('=') + 1).split(",", 2);
                if (localObject.length == 1)
                {
                  effectiveCapabilities = Long.decode(localObject[0]).longValue();
                  permittedCapabilities = effectiveCapabilities;
                }
                else
                {
                  permittedCapabilities = Long.decode(localObject[0]).longValue();
                  effectiveCapabilities = Long.decode(localObject[1]).longValue();
                }
              }
              else
              {
                throw new IllegalArgumentException("Duplicate arg specified");
              }
            }
            else if (((String)localObject).startsWith("--rlimit="))
            {
              localObject = ((String)localObject).substring(((String)localObject).indexOf('=') + 1).split(",");
              if (localObject.length == 3)
              {
                int[] arrayOfInt = new int[localObject.length];
                for (i2 = i1; i2 < localObject.length; i2++) {
                  arrayOfInt[i2] = Integer.parseInt(localObject[i2]);
                }
                if (rlimits == null) {
                  rlimits = new ArrayList();
                }
                rlimits.add(arrayOfInt);
              }
              else
              {
                throw new IllegalArgumentException("--rlimit= should have 3 comma-delimited ints");
              }
            }
            else if (((String)localObject).startsWith("--setgroups="))
            {
              if (gids == null)
              {
                localObject = ((String)localObject).substring(((String)localObject).indexOf('=') + 1).split(",");
                gids = new int[localObject.length];
                for (i2 = localObject.length - 1; i2 >= 0; i2--) {
                  gids[i2] = Integer.parseInt(localObject[i2]);
                }
              }
              else
              {
                throw new IllegalArgumentException("Duplicate arg specified");
              }
            }
            else if (((String)localObject).equals("--invoke-with"))
            {
              if (invokeWith == null)
              {
                j++;
                try
                {
                  invokeWith = paramArrayOfString[j];
                }
                catch (IndexOutOfBoundsException paramArrayOfString)
                {
                  throw new IllegalArgumentException("--invoke-with requires argument");
                }
              }
              else
              {
                throw new IllegalArgumentException("Duplicate arg specified");
              }
            }
            else if (((String)localObject).startsWith("--nice-name="))
            {
              if (niceName == null) {
                niceName = ((String)localObject).substring(((String)localObject).indexOf('=') + 1);
              } else {
                throw new IllegalArgumentException("Duplicate arg specified");
              }
            }
            else if (((String)localObject).equals("--mount-external-default"))
            {
              mountExternal = 1;
            }
            else if (((String)localObject).equals("--mount-external-read"))
            {
              mountExternal = 2;
            }
            else if (((String)localObject).equals("--mount-external-write"))
            {
              mountExternal = 3;
            }
            else if (((String)localObject).equals("--query-abi-list"))
            {
              abiListQuery = true;
            }
            else if (((String)localObject).startsWith("--instruction-set="))
            {
              instructionSet = ((String)localObject).substring(((String)localObject).indexOf('=') + 1);
            }
            else if (((String)localObject).startsWith("--app-data-dir="))
            {
              appDataDir = ((String)localObject).substring(((String)localObject).indexOf('=') + 1);
            }
            else if (((String)localObject).equals("--preload-package"))
            {
              j++;
              preloadPackage = paramArrayOfString[j];
              j++;
              preloadPackageLibs = paramArrayOfString[j];
              j++;
              preloadPackageLibFileName = paramArrayOfString[j];
              j++;
              preloadPackageCacheKey = paramArrayOfString[j];
            }
            else if (((String)localObject).equals("--preload-default"))
            {
              preloadDefault = true;
              k = 0;
            }
            else if (((String)localObject).equals("--start-child-zygote"))
            {
              startChildZygote = true;
            }
            else if (((String)localObject).equals("--set-api-blacklist-exemptions"))
            {
              apiBlacklistExemptions = ((String[])Arrays.copyOfRange(paramArrayOfString, j + 1, paramArrayOfString.length));
              j = paramArrayOfString.length;
              k = 0;
            }
            else
            {
              i2 = j;
              if (!((String)localObject).startsWith("--hidden-api-log-sampling-rate=")) {
                break;
              }
              localObject = ((String)localObject).substring(((String)localObject).indexOf('=') + 1);
            }
            try
            {
              hiddenApiAccessLogSampleRate = Integer.parseInt((String)localObject);
              k = 0;
              j++;
            }
            catch (NumberFormatException localNumberFormatException)
            {
              paramArrayOfString = new StringBuilder();
              paramArrayOfString.append("Invalid log sampling rate: ");
              paramArrayOfString.append((String)localObject);
              throw new IllegalArgumentException(paramArrayOfString.toString(), localNumberFormatException);
            }
          }
        }
      }
      if (abiListQuery)
      {
        if (paramArrayOfString.length - i2 > 0) {
          throw new IllegalArgumentException("Unexpected arguments after --query-abi-list.");
        }
      }
      else if (preloadPackage != null)
      {
        if (paramArrayOfString.length - i2 > 0) {
          throw new IllegalArgumentException("Unexpected arguments after --preload-package.");
        }
      }
      else if (k != 0) {
        if (i != 0)
        {
          remainingArgs = new String[paramArrayOfString.length - i2];
          System.arraycopy(paramArrayOfString, i2, remainingArgs, 0, remainingArgs.length);
        }
        else
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Unexpected argument : ");
          ((StringBuilder)localObject).append(paramArrayOfString[i2]);
          throw new IllegalArgumentException(((StringBuilder)localObject).toString());
        }
      }
      if (startChildZygote)
      {
        i = 0;
        paramArrayOfString = remainingArgs;
        i2 = paramArrayOfString.length;
        for (k = n;; k++)
        {
          j = i;
          if (k >= i2) {
            break;
          }
          if (paramArrayOfString[k].startsWith("--zygote-socket="))
          {
            j = 1;
            break;
          }
        }
        if (j == 0) {
          throw new IllegalArgumentException("--start-child-zygote specified without --zygote-socket=");
        }
      }
    }
  }
}
