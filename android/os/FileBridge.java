package android.os;

import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteOrder;
import java.util.Arrays;
import libcore.io.IoBridge;
import libcore.io.IoUtils;
import libcore.io.Memory;
import libcore.io.Streams;

@Deprecated
public class FileBridge
  extends Thread
{
  private static final int CMD_CLOSE = 3;
  private static final int CMD_FSYNC = 2;
  private static final int CMD_WRITE = 1;
  private static final int MSG_LENGTH = 8;
  private static final String TAG = "FileBridge";
  private final FileDescriptor mClient = new FileDescriptor();
  private volatile boolean mClosed;
  private final FileDescriptor mServer = new FileDescriptor();
  private FileDescriptor mTarget;
  
  public FileBridge()
  {
    try
    {
      Os.socketpair(OsConstants.AF_UNIX, OsConstants.SOCK_STREAM, 0, mServer, mClient);
      return;
    }
    catch (ErrnoException localErrnoException)
    {
      throw new RuntimeException("Failed to create bridge");
    }
  }
  
  public void forceClose()
  {
    IoUtils.closeQuietly(mTarget);
    IoUtils.closeQuietly(mServer);
    IoUtils.closeQuietly(mClient);
    mClosed = true;
  }
  
  public FileDescriptor getClientSocket()
  {
    return mClient;
  }
  
  public boolean isClosed()
  {
    return mClosed;
  }
  
  /* Error */
  public void run()
  {
    // Byte code:
    //   0: sipush 8192
    //   3: newarray byte
    //   5: astore_1
    //   6: aload_0
    //   7: getfield 38	android/os/FileBridge:mServer	Ljava/io/FileDescriptor;
    //   10: aload_1
    //   11: iconst_0
    //   12: bipush 8
    //   14: invokestatic 86	libcore/io/IoBridge:read	(Ljava/io/FileDescriptor;[BII)I
    //   17: bipush 8
    //   19: if_icmpne +186 -> 205
    //   22: aload_1
    //   23: iconst_0
    //   24: getstatic 92	java/nio/ByteOrder:BIG_ENDIAN	Ljava/nio/ByteOrder;
    //   27: invokestatic 98	libcore/io/Memory:peekInt	([BILjava/nio/ByteOrder;)I
    //   30: istore_2
    //   31: iload_2
    //   32: iconst_1
    //   33: if_icmpne +105 -> 138
    //   36: aload_1
    //   37: iconst_4
    //   38: getstatic 92	java/nio/ByteOrder:BIG_ENDIAN	Ljava/nio/ByteOrder;
    //   41: invokestatic 98	libcore/io/Memory:peekInt	([BILjava/nio/ByteOrder;)I
    //   44: istore_2
    //   45: iload_2
    //   46: ifle +89 -> 135
    //   49: aload_0
    //   50: getfield 38	android/os/FileBridge:mServer	Ljava/io/FileDescriptor;
    //   53: aload_1
    //   54: iconst_0
    //   55: aload_1
    //   56: arraylength
    //   57: iload_2
    //   58: invokestatic 104	java/lang/Math:min	(II)I
    //   61: invokestatic 86	libcore/io/IoBridge:read	(Ljava/io/FileDescriptor;[BII)I
    //   64: istore_3
    //   65: iload_3
    //   66: iconst_m1
    //   67: if_icmpeq +20 -> 87
    //   70: aload_0
    //   71: getfield 65	android/os/FileBridge:mTarget	Ljava/io/FileDescriptor;
    //   74: aload_1
    //   75: iconst_0
    //   76: iload_3
    //   77: invokestatic 108	libcore/io/IoBridge:write	(Ljava/io/FileDescriptor;[BII)V
    //   80: iload_2
    //   81: iload_3
    //   82: isub
    //   83: istore_2
    //   84: goto -39 -> 45
    //   87: new 80	java/io/IOException
    //   90: astore_1
    //   91: new 110	java/lang/StringBuilder
    //   94: astore 4
    //   96: aload 4
    //   98: invokespecial 111	java/lang/StringBuilder:<init>	()V
    //   101: aload 4
    //   103: ldc 113
    //   105: invokevirtual 117	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   108: pop
    //   109: aload 4
    //   111: iload_2
    //   112: invokevirtual 120	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   115: pop
    //   116: aload 4
    //   118: ldc 122
    //   120: invokevirtual 117	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   123: pop
    //   124: aload_1
    //   125: aload 4
    //   127: invokevirtual 126	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   130: invokespecial 127	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   133: aload_1
    //   134: athrow
    //   135: goto +67 -> 202
    //   138: iload_2
    //   139: iconst_2
    //   140: if_icmpne +24 -> 164
    //   143: aload_0
    //   144: getfield 65	android/os/FileBridge:mTarget	Ljava/io/FileDescriptor;
    //   147: invokestatic 130	android/system/Os:fsync	(Ljava/io/FileDescriptor;)V
    //   150: aload_0
    //   151: getfield 38	android/os/FileBridge:mServer	Ljava/io/FileDescriptor;
    //   154: aload_1
    //   155: iconst_0
    //   156: bipush 8
    //   158: invokestatic 108	libcore/io/IoBridge:write	(Ljava/io/FileDescriptor;[BII)V
    //   161: goto +41 -> 202
    //   164: iload_2
    //   165: iconst_3
    //   166: if_icmpne +36 -> 202
    //   169: aload_0
    //   170: getfield 65	android/os/FileBridge:mTarget	Ljava/io/FileDescriptor;
    //   173: invokestatic 130	android/system/Os:fsync	(Ljava/io/FileDescriptor;)V
    //   176: aload_0
    //   177: getfield 65	android/os/FileBridge:mTarget	Ljava/io/FileDescriptor;
    //   180: invokestatic 133	android/system/Os:close	(Ljava/io/FileDescriptor;)V
    //   183: aload_0
    //   184: iconst_1
    //   185: putfield 73	android/os/FileBridge:mClosed	Z
    //   188: aload_0
    //   189: getfield 38	android/os/FileBridge:mServer	Ljava/io/FileDescriptor;
    //   192: aload_1
    //   193: iconst_0
    //   194: bipush 8
    //   196: invokestatic 108	libcore/io/IoBridge:write	(Ljava/io/FileDescriptor;[BII)V
    //   199: goto +6 -> 205
    //   202: goto -196 -> 6
    //   205: aload_0
    //   206: invokevirtual 135	android/os/FileBridge:forceClose	()V
    //   209: goto +20 -> 229
    //   212: astore_1
    //   213: goto +17 -> 230
    //   216: astore_1
    //   217: ldc 21
    //   219: ldc -119
    //   221: aload_1
    //   222: invokestatic 143	android/util/Log:wtf	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   225: pop
    //   226: goto -21 -> 205
    //   229: return
    //   230: aload_0
    //   231: invokevirtual 135	android/os/FileBridge:forceClose	()V
    //   234: aload_1
    //   235: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	236	0	this	FileBridge
    //   5	188	1	localObject1	Object
    //   212	1	1	localObject2	Object
    //   216	19	1	localErrnoException	ErrnoException
    //   30	137	2	i	int
    //   64	19	3	j	int
    //   94	32	4	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   6	31	212	finally
    //   36	45	212	finally
    //   49	65	212	finally
    //   70	80	212	finally
    //   87	135	212	finally
    //   143	161	212	finally
    //   169	199	212	finally
    //   217	226	212	finally
    //   6	31	216	android/system/ErrnoException
    //   6	31	216	java/io/IOException
    //   36	45	216	android/system/ErrnoException
    //   36	45	216	java/io/IOException
    //   49	65	216	android/system/ErrnoException
    //   49	65	216	java/io/IOException
    //   70	80	216	android/system/ErrnoException
    //   70	80	216	java/io/IOException
    //   87	135	216	android/system/ErrnoException
    //   87	135	216	java/io/IOException
    //   143	161	216	android/system/ErrnoException
    //   143	161	216	java/io/IOException
    //   169	199	216	android/system/ErrnoException
    //   169	199	216	java/io/IOException
  }
  
  public void setTargetFile(FileDescriptor paramFileDescriptor)
  {
    mTarget = paramFileDescriptor;
  }
  
  public static class FileBridgeOutputStream
    extends OutputStream
  {
    private final FileDescriptor mClient;
    private final ParcelFileDescriptor mClientPfd;
    private final byte[] mTemp = new byte[8];
    
    public FileBridgeOutputStream(ParcelFileDescriptor paramParcelFileDescriptor)
    {
      mClientPfd = paramParcelFileDescriptor;
      mClient = paramParcelFileDescriptor.getFileDescriptor();
    }
    
    public FileBridgeOutputStream(FileDescriptor paramFileDescriptor)
    {
      mClientPfd = null;
      mClient = paramFileDescriptor;
    }
    
    private void writeCommandAndBlock(int paramInt, String paramString)
      throws IOException
    {
      Memory.pokeInt(mTemp, 0, paramInt, ByteOrder.BIG_ENDIAN);
      IoBridge.write(mClient, mTemp, 0, 8);
      if ((IoBridge.read(mClient, mTemp, 0, 8) == 8) && (Memory.peekInt(mTemp, 0, ByteOrder.BIG_ENDIAN) == paramInt)) {
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Failed to execute ");
      localStringBuilder.append(paramString);
      localStringBuilder.append(" across bridge");
      throw new IOException(localStringBuilder.toString());
    }
    
    public void close()
      throws IOException
    {
      try
      {
        writeCommandAndBlock(3, "close()");
        return;
      }
      finally
      {
        IoBridge.closeAndSignalBlockedThreads(mClient);
        IoUtils.closeQuietly(mClientPfd);
      }
    }
    
    public void fsync()
      throws IOException
    {
      writeCommandAndBlock(2, "fsync()");
    }
    
    public void write(int paramInt)
      throws IOException
    {
      Streams.writeSingleByte(this, paramInt);
    }
    
    public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      Arrays.checkOffsetAndCount(paramArrayOfByte.length, paramInt1, paramInt2);
      Memory.pokeInt(mTemp, 0, 1, ByteOrder.BIG_ENDIAN);
      Memory.pokeInt(mTemp, 4, paramInt2, ByteOrder.BIG_ENDIAN);
      IoBridge.write(mClient, mTemp, 0, 8);
      IoBridge.write(mClient, paramArrayOfByte, paramInt1, paramInt2);
    }
  }
}
