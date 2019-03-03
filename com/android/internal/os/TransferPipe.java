package com.android.internal.os;

import android.os.IBinder;
import android.os.IInterface;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TransferPipe
  implements Runnable, Closeable
{
  static final boolean DEBUG = false;
  static final long DEFAULT_TIMEOUT = 5000L;
  static final String TAG = "TransferPipe";
  String mBufferPrefix;
  boolean mComplete;
  long mEndTime;
  String mFailure;
  final ParcelFileDescriptor[] mFds;
  FileDescriptor mOutFd;
  final Thread mThread;
  
  public TransferPipe()
    throws IOException
  {
    this(null);
  }
  
  public TransferPipe(String paramString)
    throws IOException
  {
    this(paramString, "TransferPipe");
  }
  
  protected TransferPipe(String paramString1, String paramString2)
    throws IOException
  {
    mThread = new Thread(this, paramString2);
    mFds = ParcelFileDescriptor.createPipe();
    mBufferPrefix = paramString1;
  }
  
  public static void dumpAsync(IBinder paramIBinder, FileDescriptor paramFileDescriptor, String[] paramArrayOfString)
    throws IOException, RemoteException
  {
    goDump(paramIBinder, paramFileDescriptor, paramArrayOfString);
  }
  
  /* Error */
  public static byte[] dumpAsync(IBinder paramIBinder, String... paramVarArgs)
    throws IOException, RemoteException
  {
    // Byte code:
    //   0: invokestatic 72	android/os/ParcelFileDescriptor:createPipe	()[Landroid/os/ParcelFileDescriptor;
    //   3: astore_2
    //   4: aload_0
    //   5: aload_2
    //   6: iconst_1
    //   7: aaload
    //   8: invokevirtual 88	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   11: aload_1
    //   12: invokestatic 90	com/android/internal/os/TransferPipe:dumpAsync	(Landroid/os/IBinder;Ljava/io/FileDescriptor;[Ljava/lang/String;)V
    //   15: aload_2
    //   16: iconst_1
    //   17: aaload
    //   18: invokevirtual 91	android/os/ParcelFileDescriptor:close	()V
    //   21: aconst_null
    //   22: astore_3
    //   23: aload_2
    //   24: iconst_1
    //   25: aconst_null
    //   26: aastore
    //   27: sipush 4096
    //   30: newarray byte
    //   32: astore_1
    //   33: new 93	java/io/ByteArrayOutputStream
    //   36: astore 4
    //   38: aload 4
    //   40: invokespecial 94	java/io/ByteArrayOutputStream:<init>	()V
    //   43: aload_3
    //   44: astore_0
    //   45: new 96	java/io/FileInputStream
    //   48: astore 5
    //   50: aload_3
    //   51: astore_0
    //   52: aload 5
    //   54: aload_2
    //   55: iconst_0
    //   56: aaload
    //   57: invokevirtual 88	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   60: invokespecial 99	java/io/FileInputStream:<init>	(Ljava/io/FileDescriptor;)V
    //   63: aload 5
    //   65: aload_1
    //   66: invokevirtual 103	java/io/FileInputStream:read	([B)I
    //   69: istore 6
    //   71: iload 6
    //   73: iconst_m1
    //   74: if_icmpne +39 -> 113
    //   77: aload_3
    //   78: astore_0
    //   79: aconst_null
    //   80: aload 5
    //   82: invokestatic 105	com/android/internal/os/TransferPipe:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   85: aload_3
    //   86: astore_0
    //   87: aload 4
    //   89: invokevirtual 109	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   92: astore_1
    //   93: aconst_null
    //   94: aload 4
    //   96: invokestatic 105	com/android/internal/os/TransferPipe:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   99: aload_2
    //   100: iconst_0
    //   101: aaload
    //   102: invokevirtual 91	android/os/ParcelFileDescriptor:close	()V
    //   105: aload_2
    //   106: iconst_1
    //   107: aaload
    //   108: invokestatic 115	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   111: aload_1
    //   112: areturn
    //   113: aload 4
    //   115: aload_1
    //   116: iconst_0
    //   117: iload 6
    //   119: invokevirtual 119	java/io/ByteArrayOutputStream:write	([BII)V
    //   122: goto -59 -> 63
    //   125: astore 7
    //   127: aconst_null
    //   128: astore_1
    //   129: goto +8 -> 137
    //   132: astore_1
    //   133: aload_1
    //   134: athrow
    //   135: astore 7
    //   137: aload_3
    //   138: astore_0
    //   139: aload_1
    //   140: aload 5
    //   142: invokestatic 105	com/android/internal/os/TransferPipe:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   145: aload_3
    //   146: astore_0
    //   147: aload 7
    //   149: athrow
    //   150: astore_1
    //   151: goto +8 -> 159
    //   154: astore_1
    //   155: aload_1
    //   156: astore_0
    //   157: aload_1
    //   158: athrow
    //   159: aload_0
    //   160: aload 4
    //   162: invokestatic 105	com/android/internal/os/TransferPipe:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   165: aload_1
    //   166: athrow
    //   167: astore_0
    //   168: aload_2
    //   169: iconst_0
    //   170: aaload
    //   171: invokevirtual 91	android/os/ParcelFileDescriptor:close	()V
    //   174: aload_2
    //   175: iconst_1
    //   176: aaload
    //   177: invokestatic 115	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   180: aload_0
    //   181: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	182	0	paramIBinder	IBinder
    //   0	182	1	paramVarArgs	String[]
    //   3	172	2	arrayOfParcelFileDescriptor	ParcelFileDescriptor[]
    //   22	124	3	localObject1	Object
    //   36	125	4	localByteArrayOutputStream	java.io.ByteArrayOutputStream
    //   48	93	5	localFileInputStream	java.io.FileInputStream
    //   69	49	6	i	int
    //   125	1	7	localObject2	Object
    //   135	13	7	localObject3	Object
    // Exception table:
    //   from	to	target	type
    //   63	71	125	finally
    //   113	122	125	finally
    //   63	71	132	java/lang/Throwable
    //   113	122	132	java/lang/Throwable
    //   133	135	135	finally
    //   45	50	150	finally
    //   52	63	150	finally
    //   79	85	150	finally
    //   87	93	150	finally
    //   139	145	150	finally
    //   147	150	150	finally
    //   157	159	150	finally
    //   45	50	154	java/lang/Throwable
    //   52	63	154	java/lang/Throwable
    //   79	85	154	java/lang/Throwable
    //   87	93	154	java/lang/Throwable
    //   139	145	154	java/lang/Throwable
    //   147	150	154	java/lang/Throwable
    //   4	21	167	finally
    //   27	43	167	finally
    //   93	99	167	finally
    //   159	167	167	finally
  }
  
  static void go(Caller paramCaller, IInterface paramIInterface, FileDescriptor paramFileDescriptor, String paramString, String[] paramArrayOfString)
    throws IOException, RemoteException
  {
    go(paramCaller, paramIInterface, paramFileDescriptor, paramString, paramArrayOfString, 5000L);
  }
  
  /* Error */
  static void go(Caller paramCaller, IInterface paramIInterface, FileDescriptor paramFileDescriptor, String paramString, String[] paramArrayOfString, long paramLong)
    throws IOException, RemoteException
  {
    // Byte code:
    //   0: aload_1
    //   1: invokeinterface 130 1 0
    //   6: instanceof 132
    //   9: ifeq +19 -> 28
    //   12: aload_0
    //   13: aload_1
    //   14: aload_2
    //   15: aload_3
    //   16: aload 4
    //   18: invokeinterface 135 5 0
    //   23: goto +4 -> 27
    //   26: astore_0
    //   27: return
    //   28: new 2	com/android/internal/os/TransferPipe
    //   31: dup
    //   32: invokespecial 136	com/android/internal/os/TransferPipe:<init>	()V
    //   35: astore 7
    //   37: aconst_null
    //   38: astore 8
    //   40: aload 8
    //   42: astore 9
    //   44: aload_0
    //   45: aload_1
    //   46: aload 7
    //   48: invokevirtual 140	com/android/internal/os/TransferPipe:getWriteFd	()Landroid/os/ParcelFileDescriptor;
    //   51: invokevirtual 88	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   54: aload_3
    //   55: aload 4
    //   57: invokeinterface 135 5 0
    //   62: aload 8
    //   64: astore 9
    //   66: aload 7
    //   68: aload_2
    //   69: lload 5
    //   71: invokevirtual 143	com/android/internal/os/TransferPipe:go	(Ljava/io/FileDescriptor;J)V
    //   74: aconst_null
    //   75: aload 7
    //   77: invokestatic 105	com/android/internal/os/TransferPipe:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   80: return
    //   81: astore_0
    //   82: goto +9 -> 91
    //   85: astore_0
    //   86: aload_0
    //   87: astore 9
    //   89: aload_0
    //   90: athrow
    //   91: aload 9
    //   93: aload 7
    //   95: invokestatic 105	com/android/internal/os/TransferPipe:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   98: aload_0
    //   99: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	100	0	paramCaller	Caller
    //   0	100	1	paramIInterface	IInterface
    //   0	100	2	paramFileDescriptor	FileDescriptor
    //   0	100	3	paramString	String
    //   0	100	4	paramArrayOfString	String[]
    //   0	100	5	paramLong	long
    //   35	59	7	localTransferPipe	TransferPipe
    //   38	25	8	localObject1	Object
    //   42	50	9	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   12	23	26	android/os/RemoteException
    //   44	62	81	finally
    //   66	74	81	finally
    //   89	91	81	finally
    //   44	62	85	java/lang/Throwable
    //   66	74	85	java/lang/Throwable
  }
  
  static void goDump(IBinder paramIBinder, FileDescriptor paramFileDescriptor, String[] paramArrayOfString)
    throws IOException, RemoteException
  {
    goDump(paramIBinder, paramFileDescriptor, paramArrayOfString, 5000L);
  }
  
  /* Error */
  static void goDump(IBinder paramIBinder, FileDescriptor paramFileDescriptor, String[] paramArrayOfString, long paramLong)
    throws IOException, RemoteException
  {
    // Byte code:
    //   0: aload_0
    //   1: instanceof 132
    //   4: ifeq +16 -> 20
    //   7: aload_0
    //   8: aload_1
    //   9: aload_2
    //   10: invokeinterface 152 3 0
    //   15: goto +4 -> 19
    //   18: astore_0
    //   19: return
    //   20: new 2	com/android/internal/os/TransferPipe
    //   23: dup
    //   24: invokespecial 136	com/android/internal/os/TransferPipe:<init>	()V
    //   27: astore 5
    //   29: aconst_null
    //   30: astore 6
    //   32: aload 6
    //   34: astore 7
    //   36: aload_0
    //   37: aload 5
    //   39: invokevirtual 140	com/android/internal/os/TransferPipe:getWriteFd	()Landroid/os/ParcelFileDescriptor;
    //   42: invokevirtual 88	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   45: aload_2
    //   46: invokeinterface 154 3 0
    //   51: aload 6
    //   53: astore 7
    //   55: aload 5
    //   57: aload_1
    //   58: lload_3
    //   59: invokevirtual 143	com/android/internal/os/TransferPipe:go	(Ljava/io/FileDescriptor;J)V
    //   62: aconst_null
    //   63: aload 5
    //   65: invokestatic 105	com/android/internal/os/TransferPipe:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   68: return
    //   69: astore_0
    //   70: goto +9 -> 79
    //   73: astore_0
    //   74: aload_0
    //   75: astore 7
    //   77: aload_0
    //   78: athrow
    //   79: aload 7
    //   81: aload 5
    //   83: invokestatic 105	com/android/internal/os/TransferPipe:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   86: aload_0
    //   87: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	88	0	paramIBinder	IBinder
    //   0	88	1	paramFileDescriptor	FileDescriptor
    //   0	88	2	paramArrayOfString	String[]
    //   0	88	3	paramLong	long
    //   27	55	5	localTransferPipe	TransferPipe
    //   30	22	6	localObject1	Object
    //   34	46	7	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   7	15	18	android/os/RemoteException
    //   36	51	69	finally
    //   55	62	69	finally
    //   77	79	69	finally
    //   36	51	73	java/lang/Throwable
    //   55	62	73	java/lang/Throwable
  }
  
  public void close()
  {
    kill();
  }
  
  void closeFd(int paramInt)
  {
    if (mFds[paramInt] != null)
    {
      try
      {
        mFds[paramInt].close();
      }
      catch (IOException localIOException) {}
      mFds[paramInt] = null;
    }
  }
  
  protected OutputStream getNewOutputStream()
  {
    return new FileOutputStream(mOutFd);
  }
  
  ParcelFileDescriptor getReadFd()
  {
    return mFds[0];
  }
  
  public ParcelFileDescriptor getWriteFd()
  {
    return mFds[1];
  }
  
  public void go(FileDescriptor paramFileDescriptor)
    throws IOException
  {
    go(paramFileDescriptor, 5000L);
  }
  
  /* Error */
  public void go(FileDescriptor paramFileDescriptor, long paramLong)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield 165	com/android/internal/os/TransferPipe:mOutFd	Ljava/io/FileDescriptor;
    //   7: aload_0
    //   8: invokestatic 175	android/os/SystemClock:uptimeMillis	()J
    //   11: lload_2
    //   12: ladd
    //   13: putfield 177	com/android/internal/os/TransferPipe:mEndTime	J
    //   16: aload_0
    //   17: iconst_1
    //   18: invokevirtual 179	com/android/internal/os/TransferPipe:closeFd	(I)V
    //   21: aload_0
    //   22: getfield 66	com/android/internal/os/TransferPipe:mThread	Ljava/lang/Thread;
    //   25: invokevirtual 182	java/lang/Thread:start	()V
    //   28: aload_0
    //   29: getfield 184	com/android/internal/os/TransferPipe:mFailure	Ljava/lang/String;
    //   32: ifnonnull +62 -> 94
    //   35: aload_0
    //   36: getfield 186	com/android/internal/os/TransferPipe:mComplete	Z
    //   39: ifne +55 -> 94
    //   42: aload_0
    //   43: getfield 177	com/android/internal/os/TransferPipe:mEndTime	J
    //   46: lstore 4
    //   48: invokestatic 175	android/os/SystemClock:uptimeMillis	()J
    //   51: lstore_2
    //   52: lload 4
    //   54: lload_2
    //   55: lsub
    //   56: lstore_2
    //   57: lload_2
    //   58: lconst_0
    //   59: lcmp
    //   60: ifle +15 -> 75
    //   63: aload_0
    //   64: lload_2
    //   65: invokevirtual 190	java/lang/Object:wait	(J)V
    //   68: goto +4 -> 72
    //   71: astore_1
    //   72: goto -44 -> 28
    //   75: aload_0
    //   76: getfield 66	com/android/internal/os/TransferPipe:mThread	Ljava/lang/Thread;
    //   79: invokevirtual 193	java/lang/Thread:interrupt	()V
    //   82: new 50	java/io/IOException
    //   85: astore_1
    //   86: aload_1
    //   87: ldc -61
    //   89: invokespecial 196	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   92: aload_1
    //   93: athrow
    //   94: aload_0
    //   95: getfield 184	com/android/internal/os/TransferPipe:mFailure	Ljava/lang/String;
    //   98: ifnonnull +10 -> 108
    //   101: aload_0
    //   102: monitorexit
    //   103: aload_0
    //   104: invokevirtual 157	com/android/internal/os/TransferPipe:kill	()V
    //   107: return
    //   108: new 50	java/io/IOException
    //   111: astore_1
    //   112: aload_1
    //   113: aload_0
    //   114: getfield 184	com/android/internal/os/TransferPipe:mFailure	Ljava/lang/String;
    //   117: invokespecial 196	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   120: aload_1
    //   121: athrow
    //   122: astore_1
    //   123: aload_0
    //   124: monitorexit
    //   125: aload_1
    //   126: athrow
    //   127: astore_1
    //   128: aload_0
    //   129: invokevirtual 157	com/android/internal/os/TransferPipe:kill	()V
    //   132: aload_1
    //   133: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	134	0	this	TransferPipe
    //   0	134	1	paramFileDescriptor	FileDescriptor
    //   0	134	2	paramLong	long
    //   46	7	4	l	long
    // Exception table:
    //   from	to	target	type
    //   63	68	71	java/lang/InterruptedException
    //   2	28	122	finally
    //   28	52	122	finally
    //   63	68	122	finally
    //   75	94	122	finally
    //   94	103	122	finally
    //   108	122	122	finally
    //   123	125	122	finally
    //   0	2	127	finally
    //   125	127	127	finally
  }
  
  public void kill()
  {
    try
    {
      closeFd(0);
      closeFd(1);
      return;
    }
    finally {}
  }
  
  /* Error */
  public void run()
  {
    // Byte code:
    //   0: sipush 1024
    //   3: newarray byte
    //   5: astore_1
    //   6: aload_0
    //   7: monitorenter
    //   8: aload_0
    //   9: invokevirtual 199	com/android/internal/os/TransferPipe:getReadFd	()Landroid/os/ParcelFileDescriptor;
    //   12: astore_2
    //   13: aload_2
    //   14: ifnonnull +14 -> 28
    //   17: ldc 22
    //   19: ldc -55
    //   21: invokestatic 207	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   24: pop
    //   25: aload_0
    //   26: monitorexit
    //   27: return
    //   28: new 96	java/io/FileInputStream
    //   31: astore_3
    //   32: aload_3
    //   33: aload_2
    //   34: invokevirtual 88	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   37: invokespecial 99	java/io/FileInputStream:<init>	(Ljava/io/FileDescriptor;)V
    //   40: aload_0
    //   41: invokevirtual 209	com/android/internal/os/TransferPipe:getNewOutputStream	()Ljava/io/OutputStream;
    //   44: astore 4
    //   46: aload_0
    //   47: monitorexit
    //   48: aconst_null
    //   49: astore_2
    //   50: iconst_1
    //   51: istore 5
    //   53: iload 5
    //   55: istore 6
    //   57: aload_0
    //   58: getfield 76	com/android/internal/os/TransferPipe:mBufferPrefix	Ljava/lang/String;
    //   61: ifnull +15 -> 76
    //   64: aload_0
    //   65: getfield 76	com/android/internal/os/TransferPipe:mBufferPrefix	Ljava/lang/String;
    //   68: invokevirtual 214	java/lang/String:getBytes	()[B
    //   71: astore_2
    //   72: iload 5
    //   74: istore 6
    //   76: aload_3
    //   77: aload_1
    //   78: invokevirtual 103	java/io/FileInputStream:read	([B)I
    //   81: istore 7
    //   83: iload 7
    //   85: ifle +195 -> 280
    //   88: iconst_0
    //   89: istore 5
    //   91: aload_2
    //   92: ifnonnull +15 -> 107
    //   95: aload 4
    //   97: aload_1
    //   98: iconst_0
    //   99: iload 7
    //   101: invokevirtual 217	java/io/OutputStream:write	([BII)V
    //   104: goto -28 -> 76
    //   107: iconst_0
    //   108: istore 8
    //   110: iload 5
    //   112: iload 7
    //   114: if_icmpge +143 -> 257
    //   117: iload 6
    //   119: istore 9
    //   121: iload 5
    //   123: istore 10
    //   125: iload 8
    //   127: istore 11
    //   129: aload_1
    //   130: iload 5
    //   132: baload
    //   133: bipush 10
    //   135: if_icmpeq +105 -> 240
    //   138: iload 5
    //   140: iload 8
    //   142: if_icmple +16 -> 158
    //   145: aload 4
    //   147: aload_1
    //   148: iload 8
    //   150: iload 5
    //   152: iload 8
    //   154: isub
    //   155: invokevirtual 217	java/io/OutputStream:write	([BII)V
    //   158: iload 5
    //   160: istore 8
    //   162: iload 6
    //   164: istore 9
    //   166: iload 5
    //   168: istore 11
    //   170: iload 6
    //   172: ifeq +16 -> 188
    //   175: aload 4
    //   177: aload_2
    //   178: invokevirtual 220	java/io/OutputStream:write	([B)V
    //   181: iconst_0
    //   182: istore 9
    //   184: iload 5
    //   186: istore 11
    //   188: iload 11
    //   190: iconst_1
    //   191: iadd
    //   192: istore 5
    //   194: iload 5
    //   196: iload 7
    //   198: if_icmpge +16 -> 214
    //   201: iload 5
    //   203: istore 11
    //   205: aload_1
    //   206: iload 5
    //   208: baload
    //   209: bipush 10
    //   211: if_icmpne -23 -> 188
    //   214: iload 5
    //   216: istore 10
    //   218: iload 8
    //   220: istore 11
    //   222: iload 5
    //   224: iload 7
    //   226: if_icmpge +14 -> 240
    //   229: iconst_1
    //   230: istore 9
    //   232: iload 8
    //   234: istore 11
    //   236: iload 5
    //   238: istore 10
    //   240: iload 10
    //   242: iconst_1
    //   243: iadd
    //   244: istore 5
    //   246: iload 9
    //   248: istore 6
    //   250: iload 11
    //   252: istore 8
    //   254: goto -144 -> 110
    //   257: iload 7
    //   259: iload 8
    //   261: if_icmple +16 -> 277
    //   264: aload 4
    //   266: aload_1
    //   267: iload 8
    //   269: iload 7
    //   271: iload 8
    //   273: isub
    //   274: invokevirtual 217	java/io/OutputStream:write	([BII)V
    //   277: goto -201 -> 76
    //   280: aload_0
    //   281: getfield 66	com/android/internal/os/TransferPipe:mThread	Ljava/lang/Thread;
    //   284: invokevirtual 224	java/lang/Thread:isInterrupted	()Z
    //   287: pop
    //   288: aload_0
    //   289: monitorenter
    //   290: aload_0
    //   291: iconst_1
    //   292: putfield 186	com/android/internal/os/TransferPipe:mComplete	Z
    //   295: aload_0
    //   296: invokevirtual 227	java/lang/Object:notifyAll	()V
    //   299: aload_0
    //   300: monitorexit
    //   301: return
    //   302: astore_2
    //   303: aload_0
    //   304: monitorexit
    //   305: aload_2
    //   306: athrow
    //   307: astore_2
    //   308: aload_0
    //   309: monitorenter
    //   310: aload_0
    //   311: aload_2
    //   312: invokevirtual 231	java/io/IOException:toString	()Ljava/lang/String;
    //   315: putfield 184	com/android/internal/os/TransferPipe:mFailure	Ljava/lang/String;
    //   318: aload_0
    //   319: invokevirtual 227	java/lang/Object:notifyAll	()V
    //   322: aload_0
    //   323: monitorexit
    //   324: return
    //   325: astore_2
    //   326: aload_0
    //   327: monitorexit
    //   328: aload_2
    //   329: athrow
    //   330: astore_2
    //   331: aload_0
    //   332: monitorexit
    //   333: aload_2
    //   334: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	335	0	this	TransferPipe
    //   5	262	1	arrayOfByte	byte[]
    //   12	166	2	localObject1	Object
    //   302	4	2	localObject2	Object
    //   307	5	2	localIOException	IOException
    //   325	4	2	localObject3	Object
    //   330	4	2	localObject4	Object
    //   31	46	3	localFileInputStream	java.io.FileInputStream
    //   44	221	4	localOutputStream	OutputStream
    //   51	194	5	i	int
    //   55	194	6	j	int
    //   81	193	7	k	int
    //   108	166	8	m	int
    //   119	128	9	n	int
    //   123	121	10	i1	int
    //   127	124	11	i2	int
    // Exception table:
    //   from	to	target	type
    //   290	301	302	finally
    //   303	305	302	finally
    //   76	83	307	java/io/IOException
    //   95	104	307	java/io/IOException
    //   145	158	307	java/io/IOException
    //   175	181	307	java/io/IOException
    //   264	277	307	java/io/IOException
    //   280	288	307	java/io/IOException
    //   310	324	325	finally
    //   326	328	325	finally
    //   8	13	330	finally
    //   17	27	330	finally
    //   28	48	330	finally
    //   331	333	330	finally
  }
  
  public void setBufferPrefix(String paramString)
  {
    mBufferPrefix = paramString;
  }
  
  static abstract interface Caller
  {
    public abstract void go(IInterface paramIInterface, FileDescriptor paramFileDescriptor, String paramString, String[] paramArrayOfString)
      throws RemoteException;
  }
}
