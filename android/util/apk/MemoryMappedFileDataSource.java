package android.util.apk;

import android.system.Os;
import android.system.OsConstants;
import java.io.FileDescriptor;

class MemoryMappedFileDataSource
  implements DataSource
{
  private static final long MEMORY_PAGE_SIZE_BYTES = Os.sysconf(OsConstants._SC_PAGESIZE);
  private final FileDescriptor mFd;
  private final long mFilePosition;
  private final long mSize;
  
  MemoryMappedFileDataSource(FileDescriptor paramFileDescriptor, long paramLong1, long paramLong2)
  {
    mFd = paramFileDescriptor;
    mFilePosition = paramLong1;
    mSize = paramLong2;
  }
  
  /* Error */
  public void feedIntoDataDigester(DataDigester paramDataDigester, long paramLong, int paramInt)
    throws java.io.IOException, java.security.DigestException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 37	android/util/apk/MemoryMappedFileDataSource:mFilePosition	J
    //   4: lload_2
    //   5: ladd
    //   6: lstore 5
    //   8: lload 5
    //   10: getstatic 28	android/util/apk/MemoryMappedFileDataSource:MEMORY_PAGE_SIZE_BYTES	J
    //   13: ldiv
    //   14: getstatic 28	android/util/apk/MemoryMappedFileDataSource:MEMORY_PAGE_SIZE_BYTES	J
    //   17: lmul
    //   18: lstore_2
    //   19: lload 5
    //   21: lload_2
    //   22: lsub
    //   23: l2i
    //   24: istore 7
    //   26: iload 4
    //   28: iload 7
    //   30: iadd
    //   31: i2l
    //   32: lstore 8
    //   34: lconst_0
    //   35: lstore 5
    //   37: getstatic 50	android/system/OsConstants:PROT_READ	I
    //   40: istore 10
    //   42: getstatic 53	android/system/OsConstants:MAP_SHARED	I
    //   45: istore 11
    //   47: getstatic 56	android/system/OsConstants:MAP_POPULATE	I
    //   50: istore 12
    //   52: aload_0
    //   53: getfield 35	android/util/apk/MemoryMappedFileDataSource:mFd	Ljava/io/FileDescriptor;
    //   56: astore 13
    //   58: lconst_0
    //   59: lload 8
    //   61: iload 10
    //   63: iload 11
    //   65: iload 12
    //   67: ior
    //   68: aload 13
    //   70: lload_2
    //   71: invokestatic 60	android/system/Os:mmap	(JJIILjava/io/FileDescriptor;J)J
    //   74: lstore_2
    //   75: new 62	java/nio/DirectByteBuffer
    //   78: astore 14
    //   80: iload 7
    //   82: i2l
    //   83: lstore 5
    //   85: aload_0
    //   86: getfield 35	android/util/apk/MemoryMappedFileDataSource:mFd	Ljava/io/FileDescriptor;
    //   89: astore 13
    //   91: aload 14
    //   93: iload 4
    //   95: lload_2
    //   96: lload 5
    //   98: ladd
    //   99: aload 13
    //   101: aconst_null
    //   102: iconst_1
    //   103: invokespecial 65	java/nio/DirectByteBuffer:<init>	(IJLjava/io/FileDescriptor;Ljava/lang/Runnable;Z)V
    //   106: aload_1
    //   107: aload 14
    //   109: invokeinterface 71 2 0
    //   114: lload_2
    //   115: lconst_0
    //   116: lcmp
    //   117: ifeq +16 -> 133
    //   120: lload_2
    //   121: lload 8
    //   123: invokestatic 75	android/system/Os:munmap	(JJ)V
    //   126: goto +7 -> 133
    //   129: astore_1
    //   130: goto +3 -> 133
    //   133: return
    //   134: astore_1
    //   135: goto +8 -> 143
    //   138: astore_1
    //   139: goto +8 -> 147
    //   142: astore_1
    //   143: goto +94 -> 237
    //   146: astore_1
    //   147: goto +36 -> 183
    //   150: astore_1
    //   151: goto +86 -> 237
    //   154: astore_1
    //   155: goto +28 -> 183
    //   158: astore_1
    //   159: lload 5
    //   161: lstore_2
    //   162: goto +75 -> 237
    //   165: astore_1
    //   166: lload 5
    //   168: lstore_2
    //   169: goto +14 -> 183
    //   172: astore_1
    //   173: lload 5
    //   175: lstore_2
    //   176: goto +61 -> 237
    //   179: astore_1
    //   180: lload 5
    //   182: lstore_2
    //   183: new 43	java/io/IOException
    //   186: astore 13
    //   188: new 77	java/lang/StringBuilder
    //   191: astore 14
    //   193: aload 14
    //   195: invokespecial 78	java/lang/StringBuilder:<init>	()V
    //   198: aload 14
    //   200: ldc 80
    //   202: invokevirtual 84	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   205: pop
    //   206: aload 14
    //   208: lload 8
    //   210: invokevirtual 87	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   213: pop
    //   214: aload 14
    //   216: ldc 89
    //   218: invokevirtual 84	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   221: pop
    //   222: aload 13
    //   224: aload 14
    //   226: invokevirtual 93	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   229: aload_1
    //   230: invokespecial 96	java/io/IOException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   233: aload 13
    //   235: athrow
    //   236: astore_1
    //   237: lload_2
    //   238: lconst_0
    //   239: lcmp
    //   240: ifeq +14 -> 254
    //   243: lload_2
    //   244: lload 8
    //   246: invokestatic 75	android/system/Os:munmap	(JJ)V
    //   249: goto +5 -> 254
    //   252: astore 13
    //   254: aload_1
    //   255: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	256	0	this	MemoryMappedFileDataSource
    //   0	256	1	paramDataDigester	DataDigester
    //   0	256	2	paramLong	long
    //   0	256	4	paramInt	int
    //   6	175	5	l1	long
    //   24	57	7	i	int
    //   32	213	8	l2	long
    //   40	22	10	j	int
    //   45	23	11	k	int
    //   50	18	12	m	int
    //   56	178	13	localObject1	Object
    //   252	1	13	localErrnoException	android.system.ErrnoException
    //   78	147	14	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   120	126	129	android/system/ErrnoException
    //   106	114	134	finally
    //   106	114	138	android/system/ErrnoException
    //   91	106	142	finally
    //   91	106	146	android/system/ErrnoException
    //   75	80	150	finally
    //   85	91	150	finally
    //   75	80	154	android/system/ErrnoException
    //   85	91	154	android/system/ErrnoException
    //   58	75	158	finally
    //   58	75	165	android/system/ErrnoException
    //   37	58	172	finally
    //   37	58	179	android/system/ErrnoException
    //   183	236	236	finally
    //   243	249	252	android/system/ErrnoException
  }
  
  public long size()
  {
    return mSize;
  }
}
