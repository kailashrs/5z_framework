package com.android.internal.os;

import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.system.ErrnoException;
import android.system.Os;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.IOException;

class ZygoteServer
{
  private static final String ANDROID_SOCKET_PREFIX = "ANDROID_SOCKET_";
  public static final String TAG = "ZygoteServer";
  private boolean mCloseSocketFd;
  private boolean mIsForkChild;
  private LocalServerSocket mServerSocket;
  
  ZygoteServer() {}
  
  private ZygoteConnection acceptCommandPeer(String paramString)
  {
    try
    {
      paramString = createNewConnection(mServerSocket.accept(), paramString);
      return paramString;
    }
    catch (IOException paramString)
    {
      throw new RuntimeException("IOException during accept()", paramString);
    }
  }
  
  void closeServerSocket()
  {
    try
    {
      if (mServerSocket != null)
      {
        FileDescriptor localFileDescriptor = mServerSocket.getFileDescriptor();
        mServerSocket.close();
        if ((localFileDescriptor != null) && (mCloseSocketFd)) {
          Os.close(localFileDescriptor);
        }
      }
    }
    catch (ErrnoException localErrnoException)
    {
      Log.e("ZygoteServer", "Zygote:  error closing descriptor", localErrnoException);
    }
    catch (IOException localIOException)
    {
      Log.e("ZygoteServer", "Zygote:  error closing sockets", localIOException);
    }
    mServerSocket = null;
  }
  
  protected ZygoteConnection createNewConnection(LocalSocket paramLocalSocket, String paramString)
    throws IOException
  {
    return new ZygoteConnection(paramLocalSocket, paramString);
  }
  
  FileDescriptor getServerSocketFileDescriptor()
  {
    return mServerSocket.getFileDescriptor();
  }
  
  void registerServerSocketAtAbstractName(String paramString)
  {
    if (mServerSocket == null) {
      try
      {
        localObject = new android/net/LocalServerSocket;
        ((LocalServerSocket)localObject).<init>(paramString);
        mServerSocket = ((LocalServerSocket)localObject);
        mCloseSocketFd = false;
      }
      catch (IOException localIOException)
      {
        Object localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Error binding to abstract socket '");
        ((StringBuilder)localObject).append(paramString);
        ((StringBuilder)localObject).append("'");
        throw new RuntimeException(((StringBuilder)localObject).toString(), localIOException);
      }
    }
  }
  
  void registerServerSocketFromEnv(String paramString)
  {
    if (mServerSocket == null)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("ANDROID_SOCKET_");
      ((StringBuilder)localObject).append(paramString);
      localObject = ((StringBuilder)localObject).toString();
      try
      {
        int i = Integer.parseInt(System.getenv((String)localObject));
        try
        {
          paramString = new java/io/FileDescriptor;
          paramString.<init>();
          paramString.setInt$(i);
          localObject = new android/net/LocalServerSocket;
          ((LocalServerSocket)localObject).<init>(paramString);
          mServerSocket = ((LocalServerSocket)localObject);
          mCloseSocketFd = true;
        }
        catch (IOException paramString)
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Error binding to local socket '");
          ((StringBuilder)localObject).append(i);
          ((StringBuilder)localObject).append("'");
          throw new RuntimeException(((StringBuilder)localObject).toString(), paramString);
        }
        StringBuilder localStringBuilder;
        return;
      }
      catch (RuntimeException paramString)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append((String)localObject);
        localStringBuilder.append(" unset or invalid");
        throw new RuntimeException(localStringBuilder.toString(), paramString);
      }
    }
  }
  
  /* Error */
  Runnable runSelectLoop(String paramString)
  {
    // Byte code:
    //   0: new 132	java/util/ArrayList
    //   3: dup
    //   4: invokespecial 133	java/util/ArrayList:<init>	()V
    //   7: astore_2
    //   8: new 132	java/util/ArrayList
    //   11: dup
    //   12: invokespecial 133	java/util/ArrayList:<init>	()V
    //   15: astore_3
    //   16: aload_2
    //   17: aload_0
    //   18: getfield 27	com/android/internal/os/ZygoteServer:mServerSocket	Landroid/net/LocalServerSocket;
    //   21: invokevirtual 51	android/net/LocalServerSocket:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   24: invokevirtual 137	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   27: pop
    //   28: aload_3
    //   29: aconst_null
    //   30: invokevirtual 137	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   33: pop
    //   34: aload_2
    //   35: invokevirtual 141	java/util/ArrayList:size	()I
    //   38: anewarray 143	android/system/StructPollfd
    //   41: astore 4
    //   43: iconst_0
    //   44: istore 5
    //   46: iload 5
    //   48: aload 4
    //   50: arraylength
    //   51: if_icmpge +51 -> 102
    //   54: aload 4
    //   56: iload 5
    //   58: new 143	android/system/StructPollfd
    //   61: dup
    //   62: invokespecial 144	android/system/StructPollfd:<init>	()V
    //   65: aastore
    //   66: aload 4
    //   68: iload 5
    //   70: aaload
    //   71: aload_2
    //   72: iload 5
    //   74: invokevirtual 148	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   77: checkcast 112	java/io/FileDescriptor
    //   80: putfield 152	android/system/StructPollfd:fd	Ljava/io/FileDescriptor;
    //   83: aload 4
    //   85: iload 5
    //   87: aaload
    //   88: getstatic 158	android/system/OsConstants:POLLIN	I
    //   91: i2s
    //   92: i2s
    //   93: putfield 162	android/system/StructPollfd:events	S
    //   96: iinc 5 1
    //   99: goto -53 -> 46
    //   102: aload 4
    //   104: iconst_m1
    //   105: invokestatic 166	android/system/Os:poll	([Landroid/system/StructPollfd;I)I
    //   108: pop
    //   109: aload 4
    //   111: arraylength
    //   112: iconst_1
    //   113: isub
    //   114: istore 5
    //   116: iload 5
    //   118: iflt +237 -> 355
    //   121: aload 4
    //   123: iload 5
    //   125: aaload
    //   126: getfield 169	android/system/StructPollfd:revents	S
    //   129: getstatic 158	android/system/OsConstants:POLLIN	I
    //   132: iand
    //   133: ifne +6 -> 139
    //   136: goto +193 -> 329
    //   139: iload 5
    //   141: ifne +30 -> 171
    //   144: aload_0
    //   145: aload_1
    //   146: invokespecial 171	com/android/internal/os/ZygoteServer:acceptCommandPeer	(Ljava/lang/String;)Lcom/android/internal/os/ZygoteConnection;
    //   149: astore 6
    //   151: aload_3
    //   152: aload 6
    //   154: invokevirtual 137	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   157: pop
    //   158: aload_2
    //   159: aload 6
    //   161: invokevirtual 174	com/android/internal/os/ZygoteConnection:getFileDesciptor	()Ljava/io/FileDescriptor;
    //   164: invokevirtual 137	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   167: pop
    //   168: goto +161 -> 329
    //   171: aload_3
    //   172: iload 5
    //   174: invokevirtual 148	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   177: checkcast 73	com/android/internal/os/ZygoteConnection
    //   180: astore 7
    //   182: aload 7
    //   184: aload_0
    //   185: invokevirtual 178	com/android/internal/os/ZygoteConnection:processOneCommand	(Lcom/android/internal/os/ZygoteServer;)Ljava/lang/Runnable;
    //   188: astore 6
    //   190: aload_0
    //   191: getfield 180	com/android/internal/os/ZygoteServer:mIsForkChild	Z
    //   194: istore 8
    //   196: iload 8
    //   198: ifeq +31 -> 229
    //   201: aload 6
    //   203: ifnull +11 -> 214
    //   206: aload_0
    //   207: iconst_0
    //   208: putfield 180	com/android/internal/os/ZygoteServer:mIsForkChild	Z
    //   211: aload 6
    //   213: areturn
    //   214: new 182	java/lang/IllegalStateException
    //   217: astore 6
    //   219: aload 6
    //   221: ldc -72
    //   223: invokespecial 185	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   226: aload 6
    //   228: athrow
    //   229: aload 6
    //   231: ifnonnull +38 -> 269
    //   234: aload 7
    //   236: invokevirtual 189	com/android/internal/os/ZygoteConnection:isClosedByPeer	()Z
    //   239: ifeq +22 -> 261
    //   242: aload 7
    //   244: invokevirtual 192	com/android/internal/os/ZygoteConnection:closeSocket	()V
    //   247: aload_3
    //   248: iload 5
    //   250: invokevirtual 195	java/util/ArrayList:remove	(I)Ljava/lang/Object;
    //   253: pop
    //   254: aload_2
    //   255: iload 5
    //   257: invokevirtual 195	java/util/ArrayList:remove	(I)Ljava/lang/Object;
    //   260: pop
    //   261: aload_0
    //   262: iconst_0
    //   263: putfield 180	com/android/internal/os/ZygoteServer:mIsForkChild	Z
    //   266: goto +63 -> 329
    //   269: new 182	java/lang/IllegalStateException
    //   272: astore 6
    //   274: aload 6
    //   276: ldc -59
    //   278: invokespecial 185	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   281: aload 6
    //   283: athrow
    //   284: astore_1
    //   285: goto +63 -> 348
    //   288: astore 6
    //   290: aload_0
    //   291: getfield 180	com/android/internal/os/ZygoteServer:mIsForkChild	Z
    //   294: ifne +41 -> 335
    //   297: ldc 11
    //   299: ldc -57
    //   301: aload 6
    //   303: invokestatic 202	android/util/Slog:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   306: pop
    //   307: aload_3
    //   308: iload 5
    //   310: invokevirtual 195	java/util/ArrayList:remove	(I)Ljava/lang/Object;
    //   313: checkcast 73	com/android/internal/os/ZygoteConnection
    //   316: invokevirtual 192	com/android/internal/os/ZygoteConnection:closeSocket	()V
    //   319: aload_2
    //   320: iload 5
    //   322: invokevirtual 195	java/util/ArrayList:remove	(I)Ljava/lang/Object;
    //   325: pop
    //   326: goto -65 -> 261
    //   329: iinc 5 -1
    //   332: goto -216 -> 116
    //   335: ldc 11
    //   337: ldc -52
    //   339: aload 6
    //   341: invokestatic 69	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   344: pop
    //   345: aload 6
    //   347: athrow
    //   348: aload_0
    //   349: iconst_0
    //   350: putfield 180	com/android/internal/os/ZygoteServer:mIsForkChild	Z
    //   353: aload_1
    //   354: athrow
    //   355: goto -321 -> 34
    //   358: astore_1
    //   359: new 39	java/lang/RuntimeException
    //   362: dup
    //   363: ldc -50
    //   365: aload_1
    //   366: invokespecial 44	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   369: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	370	0	this	ZygoteServer
    //   0	370	1	paramString	String
    //   7	313	2	localArrayList1	java.util.ArrayList
    //   15	293	3	localArrayList2	java.util.ArrayList
    //   41	81	4	arrayOfStructPollfd	android.system.StructPollfd[]
    //   44	286	5	i	int
    //   149	133	6	localObject	Object
    //   288	58	6	localException	Exception
    //   180	63	7	localZygoteConnection	ZygoteConnection
    //   194	3	8	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   171	196	284	finally
    //   214	229	284	finally
    //   234	261	284	finally
    //   269	284	284	finally
    //   290	326	284	finally
    //   335	348	284	finally
    //   171	196	288	java/lang/Exception
    //   214	229	288	java/lang/Exception
    //   234	261	288	java/lang/Exception
    //   269	284	288	java/lang/Exception
    //   102	109	358	android/system/ErrnoException
  }
  
  void setForkChild()
  {
    mIsForkChild = true;
  }
}
