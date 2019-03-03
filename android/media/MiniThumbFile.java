package android.media;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class MiniThumbFile
{
  public static final int BYTES_PER_MINTHUMB = 10000;
  private static final int HEADER_SIZE = 13;
  private static final int MINI_THUMB_DATA_FILE_VERSION = 4;
  private static final String TAG = "MiniThumbFile";
  private static final Hashtable<String, MiniThumbFile> sThumbFiles = new Hashtable();
  private ByteBuffer mBuffer;
  private FileChannel mChannel;
  private ByteBuffer mEmptyBuffer;
  private RandomAccessFile mMiniThumbFile;
  private Uri mUri;
  
  private MiniThumbFile(Uri paramUri)
  {
    mUri = paramUri;
    mBuffer = ByteBuffer.allocateDirect(10000);
    mEmptyBuffer = ByteBuffer.allocateDirect(10000);
  }
  
  public static MiniThumbFile instance(Uri paramUri)
  {
    try
    {
      String str = (String)paramUri.getPathSegments().get(1);
      Object localObject = (MiniThumbFile)sThumbFiles.get(str);
      paramUri = (Uri)localObject;
      if (localObject == null)
      {
        paramUri = new android/media/MiniThumbFile;
        localObject = new java/lang/StringBuilder;
        ((StringBuilder)localObject).<init>();
        ((StringBuilder)localObject).append("content://media/external/");
        ((StringBuilder)localObject).append(str);
        ((StringBuilder)localObject).append("/media");
        paramUri.<init>(Uri.parse(((StringBuilder)localObject).toString()));
        sThumbFiles.put(str, paramUri);
      }
      return paramUri;
    }
    finally {}
  }
  
  private RandomAccessFile miniThumbDataFile()
  {
    if (mMiniThumbFile == null)
    {
      removeOldFile();
      Object localObject1 = randomAccessFilePath(4);
      File localFile = new File((String)localObject1).getParentFile();
      Object localObject2;
      if ((!localFile.isDirectory()) && (!localFile.mkdirs()))
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Unable to create .thumbnails directory ");
        ((StringBuilder)localObject2).append(localFile.toString());
        Log.e("MiniThumbFile", ((StringBuilder)localObject2).toString());
      }
      localObject1 = new File((String)localObject1);
      try
      {
        localObject2 = new java/io/RandomAccessFile;
        ((RandomAccessFile)localObject2).<init>((File)localObject1, "rw");
        mMiniThumbFile = ((RandomAccessFile)localObject2);
      }
      catch (IOException localIOException2)
      {
        try
        {
          RandomAccessFile localRandomAccessFile = new java/io/RandomAccessFile;
          localRandomAccessFile.<init>((File)localObject1, "r");
          mMiniThumbFile = localRandomAccessFile;
        }
        catch (IOException localIOException1) {}
      }
      if (mMiniThumbFile != null) {
        mChannel = mMiniThumbFile.getChannel();
      }
    }
    return mMiniThumbFile;
  }
  
  private String randomAccessFilePath(int paramInt)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(Environment.getExternalStorageDirectory().toString());
    ((StringBuilder)localObject).append("/DCIM/.thumbnails");
    localObject = ((StringBuilder)localObject).toString();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append((String)localObject);
    localStringBuilder.append("/.thumbdata");
    localStringBuilder.append(paramInt);
    localStringBuilder.append("-");
    localStringBuilder.append(mUri.hashCode());
    return localStringBuilder.toString();
  }
  
  private void removeOldFile()
  {
    File localFile = new File(randomAccessFilePath(3));
    if (localFile.exists()) {
      try
      {
        localFile.delete();
      }
      catch (SecurityException localSecurityException) {}
    }
  }
  
  public static void reset()
  {
    try
    {
      Iterator localIterator = sThumbFiles.values().iterator();
      while (localIterator.hasNext()) {
        ((MiniThumbFile)localIterator.next()).deactivate();
      }
      sThumbFiles.clear();
      return;
    }
    finally {}
  }
  
  public void deactivate()
  {
    try
    {
      RandomAccessFile localRandomAccessFile = mMiniThumbFile;
      if (localRandomAccessFile != null) {
        try
        {
          mMiniThumbFile.close();
          mMiniThumbFile = null;
        }
        catch (IOException localIOException) {}
      }
      return;
    }
    finally {}
  }
  
  /* Error */
  public void eraseMiniThumb(long paramLong)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial 209	android/media/MiniThumbFile:miniThumbDataFile	()Ljava/io/RandomAccessFile;
    //   6: astore_3
    //   7: aload_3
    //   8: ifnull +477 -> 485
    //   11: ldc2_w 210
    //   14: lload_1
    //   15: lmul
    //   16: lstore 4
    //   18: aconst_null
    //   19: astore 6
    //   21: aconst_null
    //   22: astore 7
    //   24: aconst_null
    //   25: astore 8
    //   27: aload 8
    //   29: astore_3
    //   30: aload 6
    //   32: astore 9
    //   34: aload 7
    //   36: astore 10
    //   38: aload_0
    //   39: getfield 49	android/media/MiniThumbFile:mBuffer	Ljava/nio/ByteBuffer;
    //   42: invokevirtual 214	java/nio/ByteBuffer:clear	()Ljava/nio/Buffer;
    //   45: pop
    //   46: aload 8
    //   48: astore_3
    //   49: aload 6
    //   51: astore 9
    //   53: aload 7
    //   55: astore 10
    //   57: aload_0
    //   58: getfield 49	android/media/MiniThumbFile:mBuffer	Ljava/nio/ByteBuffer;
    //   61: bipush 9
    //   63: invokevirtual 218	java/nio/ByteBuffer:limit	(I)Ljava/nio/Buffer;
    //   66: pop
    //   67: aload 8
    //   69: astore_3
    //   70: aload 6
    //   72: astore 9
    //   74: aload 7
    //   76: astore 10
    //   78: aload_0
    //   79: getfield 148	android/media/MiniThumbFile:mChannel	Ljava/nio/channels/FileChannel;
    //   82: lload 4
    //   84: ldc2_w 210
    //   87: iconst_0
    //   88: invokevirtual 224	java/nio/channels/FileChannel:lock	(JJZ)Ljava/nio/channels/FileLock;
    //   91: astore 8
    //   93: aload 8
    //   95: astore_3
    //   96: aload 8
    //   98: astore 9
    //   100: aload 8
    //   102: astore 10
    //   104: aload_0
    //   105: getfield 148	android/media/MiniThumbFile:mChannel	Ljava/nio/channels/FileChannel;
    //   108: aload_0
    //   109: getfield 49	android/media/MiniThumbFile:mBuffer	Ljava/nio/ByteBuffer;
    //   112: lload 4
    //   114: invokevirtual 228	java/nio/channels/FileChannel:read	(Ljava/nio/ByteBuffer;J)I
    //   117: bipush 9
    //   119: if_icmpne +204 -> 323
    //   122: aload 8
    //   124: astore_3
    //   125: aload 8
    //   127: astore 9
    //   129: aload 8
    //   131: astore 10
    //   133: aload_0
    //   134: getfield 49	android/media/MiniThumbFile:mBuffer	Ljava/nio/ByteBuffer;
    //   137: iconst_0
    //   138: invokevirtual 231	java/nio/ByteBuffer:position	(I)Ljava/nio/Buffer;
    //   141: pop
    //   142: aload 8
    //   144: astore_3
    //   145: aload 8
    //   147: astore 9
    //   149: aload 8
    //   151: astore 10
    //   153: aload_0
    //   154: getfield 49	android/media/MiniThumbFile:mBuffer	Ljava/nio/ByteBuffer;
    //   157: invokevirtual 234	java/nio/ByteBuffer:get	()B
    //   160: iconst_1
    //   161: if_icmpne +162 -> 323
    //   164: aload 8
    //   166: astore_3
    //   167: aload 8
    //   169: astore 9
    //   171: aload 8
    //   173: astore 10
    //   175: aload_0
    //   176: getfield 49	android/media/MiniThumbFile:mBuffer	Ljava/nio/ByteBuffer;
    //   179: invokevirtual 238	java/nio/ByteBuffer:getLong	()J
    //   182: lconst_0
    //   183: lcmp
    //   184: ifne +114 -> 298
    //   187: aload 8
    //   189: astore_3
    //   190: aload 8
    //   192: astore 9
    //   194: aload 8
    //   196: astore 10
    //   198: new 72	java/lang/StringBuilder
    //   201: astore 6
    //   203: aload 8
    //   205: astore_3
    //   206: aload 8
    //   208: astore 9
    //   210: aload 8
    //   212: astore 10
    //   214: aload 6
    //   216: invokespecial 73	java/lang/StringBuilder:<init>	()V
    //   219: aload 8
    //   221: astore_3
    //   222: aload 8
    //   224: astore 9
    //   226: aload 8
    //   228: astore 10
    //   230: aload 6
    //   232: ldc -16
    //   234: invokevirtual 79	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   237: pop
    //   238: aload 8
    //   240: astore_3
    //   241: aload 8
    //   243: astore 9
    //   245: aload 8
    //   247: astore 10
    //   249: aload 6
    //   251: lload_1
    //   252: invokevirtual 243	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   255: pop
    //   256: aload 8
    //   258: astore_3
    //   259: aload 8
    //   261: astore 9
    //   263: aload 8
    //   265: astore 10
    //   267: ldc 15
    //   269: aload 6
    //   271: invokevirtual 85	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   274: invokestatic 246	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   277: pop
    //   278: aload 8
    //   280: ifnull +15 -> 295
    //   283: aload 8
    //   285: invokevirtual 251	java/nio/channels/FileLock:release	()V
    //   288: goto +7 -> 295
    //   291: astore_3
    //   292: goto +3 -> 295
    //   295: aload_0
    //   296: monitorexit
    //   297: return
    //   298: aload 8
    //   300: astore_3
    //   301: aload 8
    //   303: astore 9
    //   305: aload 8
    //   307: astore 10
    //   309: aload_0
    //   310: getfield 148	android/media/MiniThumbFile:mChannel	Ljava/nio/channels/FileChannel;
    //   313: aload_0
    //   314: getfield 51	android/media/MiniThumbFile:mEmptyBuffer	Ljava/nio/ByteBuffer;
    //   317: lload 4
    //   319: invokevirtual 254	java/nio/channels/FileChannel:write	(Ljava/nio/ByteBuffer;J)I
    //   322: pop
    //   323: aload 8
    //   325: ifnull +142 -> 467
    //   328: aload 8
    //   330: invokevirtual 251	java/nio/channels/FileLock:release	()V
    //   333: goto +134 -> 467
    //   336: astore 9
    //   338: goto +132 -> 470
    //   341: astore 10
    //   343: aload 9
    //   345: astore_3
    //   346: new 72	java/lang/StringBuilder
    //   349: astore 8
    //   351: aload 9
    //   353: astore_3
    //   354: aload 8
    //   356: invokespecial 73	java/lang/StringBuilder:<init>	()V
    //   359: aload 9
    //   361: astore_3
    //   362: aload 8
    //   364: ldc_w 256
    //   367: invokevirtual 79	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   370: pop
    //   371: aload 9
    //   373: astore_3
    //   374: aload 8
    //   376: lload_1
    //   377: invokevirtual 243	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   380: pop
    //   381: aload 9
    //   383: astore_3
    //   384: aload 8
    //   386: ldc_w 258
    //   389: invokevirtual 79	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   392: pop
    //   393: aload 9
    //   395: astore_3
    //   396: aload 8
    //   398: aload 10
    //   400: invokevirtual 262	java/lang/Object:getClass	()Ljava/lang/Class;
    //   403: invokevirtual 265	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   406: pop
    //   407: aload 9
    //   409: astore_3
    //   410: ldc 15
    //   412: aload 8
    //   414: invokevirtual 85	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   417: invokestatic 133	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   420: pop
    //   421: aload 9
    //   423: ifnull +44 -> 467
    //   426: aload 9
    //   428: invokevirtual 251	java/nio/channels/FileLock:release	()V
    //   431: goto +36 -> 467
    //   434: astore 9
    //   436: aload 10
    //   438: astore_3
    //   439: ldc 15
    //   441: ldc_w 267
    //   444: aload 9
    //   446: invokestatic 271	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   449: pop
    //   450: aload 10
    //   452: ifnull +15 -> 467
    //   455: aload 10
    //   457: invokevirtual 251	java/nio/channels/FileLock:release	()V
    //   460: goto +7 -> 467
    //   463: astore_3
    //   464: goto +21 -> 485
    //   467: goto +18 -> 485
    //   470: aload_3
    //   471: ifnull +11 -> 482
    //   474: aload_3
    //   475: invokevirtual 251	java/nio/channels/FileLock:release	()V
    //   478: goto +4 -> 482
    //   481: astore_3
    //   482: aload 9
    //   484: athrow
    //   485: aload_0
    //   486: monitorexit
    //   487: return
    //   488: astore_3
    //   489: aload_0
    //   490: monitorexit
    //   491: aload_3
    //   492: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	493	0	this	MiniThumbFile
    //   0	493	1	paramLong	long
    //   6	253	3	localObject1	Object
    //   291	1	3	localIOException1	IOException
    //   300	139	3	localObject2	Object
    //   463	12	3	localIOException2	IOException
    //   481	1	3	localIOException3	IOException
    //   488	4	3	localObject3	Object
    //   16	302	4	l	long
    //   19	251	6	localStringBuilder	StringBuilder
    //   22	53	7	localObject4	Object
    //   25	388	8	localObject5	Object
    //   32	272	9	localObject6	Object
    //   336	91	9	localObject7	Object
    //   434	49	9	localIOException4	IOException
    //   36	272	10	localObject8	Object
    //   341	115	10	localRuntimeException	RuntimeException
    // Exception table:
    //   from	to	target	type
    //   283	288	291	java/io/IOException
    //   38	46	336	finally
    //   57	67	336	finally
    //   78	93	336	finally
    //   104	122	336	finally
    //   133	142	336	finally
    //   153	164	336	finally
    //   175	187	336	finally
    //   198	203	336	finally
    //   214	219	336	finally
    //   230	238	336	finally
    //   249	256	336	finally
    //   267	278	336	finally
    //   309	323	336	finally
    //   346	351	336	finally
    //   354	359	336	finally
    //   362	371	336	finally
    //   374	381	336	finally
    //   384	393	336	finally
    //   396	407	336	finally
    //   410	421	336	finally
    //   439	450	336	finally
    //   38	46	341	java/lang/RuntimeException
    //   57	67	341	java/lang/RuntimeException
    //   78	93	341	java/lang/RuntimeException
    //   104	122	341	java/lang/RuntimeException
    //   133	142	341	java/lang/RuntimeException
    //   153	164	341	java/lang/RuntimeException
    //   175	187	341	java/lang/RuntimeException
    //   198	203	341	java/lang/RuntimeException
    //   214	219	341	java/lang/RuntimeException
    //   230	238	341	java/lang/RuntimeException
    //   249	256	341	java/lang/RuntimeException
    //   267	278	341	java/lang/RuntimeException
    //   309	323	341	java/lang/RuntimeException
    //   38	46	434	java/io/IOException
    //   57	67	434	java/io/IOException
    //   78	93	434	java/io/IOException
    //   104	122	434	java/io/IOException
    //   133	142	434	java/io/IOException
    //   153	164	434	java/io/IOException
    //   175	187	434	java/io/IOException
    //   198	203	434	java/io/IOException
    //   214	219	434	java/io/IOException
    //   230	238	434	java/io/IOException
    //   249	256	434	java/io/IOException
    //   267	278	434	java/io/IOException
    //   309	323	434	java/io/IOException
    //   328	333	463	java/io/IOException
    //   426	431	463	java/io/IOException
    //   455	460	463	java/io/IOException
    //   474	478	481	java/io/IOException
    //   2	7	488	finally
    //   283	288	488	finally
    //   328	333	488	finally
    //   426	431	488	finally
    //   455	460	488	finally
    //   474	478	488	finally
    //   482	485	488	finally
  }
  
  /* Error */
  public long getMagic(long paramLong)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial 209	android/media/MiniThumbFile:miniThumbDataFile	()Ljava/io/RandomAccessFile;
    //   6: astore_3
    //   7: aload_3
    //   8: ifnull +360 -> 368
    //   11: ldc2_w 210
    //   14: lload_1
    //   15: lmul
    //   16: lstore 4
    //   18: aconst_null
    //   19: astore 6
    //   21: aconst_null
    //   22: astore 7
    //   24: aconst_null
    //   25: astore 8
    //   27: aload 8
    //   29: astore_3
    //   30: aload 6
    //   32: astore 9
    //   34: aload 7
    //   36: astore 10
    //   38: aload_0
    //   39: getfield 49	android/media/MiniThumbFile:mBuffer	Ljava/nio/ByteBuffer;
    //   42: invokevirtual 214	java/nio/ByteBuffer:clear	()Ljava/nio/Buffer;
    //   45: pop
    //   46: aload 8
    //   48: astore_3
    //   49: aload 6
    //   51: astore 9
    //   53: aload 7
    //   55: astore 10
    //   57: aload_0
    //   58: getfield 49	android/media/MiniThumbFile:mBuffer	Ljava/nio/ByteBuffer;
    //   61: bipush 9
    //   63: invokevirtual 218	java/nio/ByteBuffer:limit	(I)Ljava/nio/Buffer;
    //   66: pop
    //   67: aload 8
    //   69: astore_3
    //   70: aload 6
    //   72: astore 9
    //   74: aload 7
    //   76: astore 10
    //   78: aload_0
    //   79: getfield 148	android/media/MiniThumbFile:mChannel	Ljava/nio/channels/FileChannel;
    //   82: lload 4
    //   84: ldc2_w 274
    //   87: iconst_1
    //   88: invokevirtual 224	java/nio/channels/FileChannel:lock	(JJZ)Ljava/nio/channels/FileLock;
    //   91: astore 8
    //   93: aload 8
    //   95: astore_3
    //   96: aload 8
    //   98: astore 9
    //   100: aload 8
    //   102: astore 10
    //   104: aload_0
    //   105: getfield 148	android/media/MiniThumbFile:mChannel	Ljava/nio/channels/FileChannel;
    //   108: aload_0
    //   109: getfield 49	android/media/MiniThumbFile:mBuffer	Ljava/nio/ByteBuffer;
    //   112: lload 4
    //   114: invokevirtual 228	java/nio/channels/FileChannel:read	(Ljava/nio/ByteBuffer;J)I
    //   117: bipush 9
    //   119: if_icmpne +87 -> 206
    //   122: aload 8
    //   124: astore_3
    //   125: aload 8
    //   127: astore 9
    //   129: aload 8
    //   131: astore 10
    //   133: aload_0
    //   134: getfield 49	android/media/MiniThumbFile:mBuffer	Ljava/nio/ByteBuffer;
    //   137: iconst_0
    //   138: invokevirtual 231	java/nio/ByteBuffer:position	(I)Ljava/nio/Buffer;
    //   141: pop
    //   142: aload 8
    //   144: astore_3
    //   145: aload 8
    //   147: astore 9
    //   149: aload 8
    //   151: astore 10
    //   153: aload_0
    //   154: getfield 49	android/media/MiniThumbFile:mBuffer	Ljava/nio/ByteBuffer;
    //   157: invokevirtual 234	java/nio/ByteBuffer:get	()B
    //   160: iconst_1
    //   161: if_icmpne +45 -> 206
    //   164: aload 8
    //   166: astore_3
    //   167: aload 8
    //   169: astore 9
    //   171: aload 8
    //   173: astore 10
    //   175: aload_0
    //   176: getfield 49	android/media/MiniThumbFile:mBuffer	Ljava/nio/ByteBuffer;
    //   179: invokevirtual 238	java/nio/ByteBuffer:getLong	()J
    //   182: lstore 4
    //   184: aload 8
    //   186: ifnull +15 -> 201
    //   189: aload 8
    //   191: invokevirtual 251	java/nio/channels/FileLock:release	()V
    //   194: goto +7 -> 201
    //   197: astore_3
    //   198: goto +3 -> 201
    //   201: aload_0
    //   202: monitorexit
    //   203: lload 4
    //   205: lreturn
    //   206: aload 8
    //   208: ifnull +142 -> 350
    //   211: aload 8
    //   213: invokevirtual 251	java/nio/channels/FileLock:release	()V
    //   216: goto +134 -> 350
    //   219: astore 9
    //   221: goto +132 -> 353
    //   224: astore 10
    //   226: aload 9
    //   228: astore_3
    //   229: new 72	java/lang/StringBuilder
    //   232: astore 8
    //   234: aload 9
    //   236: astore_3
    //   237: aload 8
    //   239: invokespecial 73	java/lang/StringBuilder:<init>	()V
    //   242: aload 9
    //   244: astore_3
    //   245: aload 8
    //   247: ldc_w 256
    //   250: invokevirtual 79	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   253: pop
    //   254: aload 9
    //   256: astore_3
    //   257: aload 8
    //   259: lload_1
    //   260: invokevirtual 243	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   263: pop
    //   264: aload 9
    //   266: astore_3
    //   267: aload 8
    //   269: ldc_w 258
    //   272: invokevirtual 79	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   275: pop
    //   276: aload 9
    //   278: astore_3
    //   279: aload 8
    //   281: aload 10
    //   283: invokevirtual 262	java/lang/Object:getClass	()Ljava/lang/Class;
    //   286: invokevirtual 265	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   289: pop
    //   290: aload 9
    //   292: astore_3
    //   293: ldc 15
    //   295: aload 8
    //   297: invokevirtual 85	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   300: invokestatic 133	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   303: pop
    //   304: aload 9
    //   306: ifnull +44 -> 350
    //   309: aload 9
    //   311: invokevirtual 251	java/nio/channels/FileLock:release	()V
    //   314: goto +36 -> 350
    //   317: astore 9
    //   319: aload 10
    //   321: astore_3
    //   322: ldc 15
    //   324: ldc_w 267
    //   327: aload 9
    //   329: invokestatic 271	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   332: pop
    //   333: aload 10
    //   335: ifnull +15 -> 350
    //   338: aload 10
    //   340: invokevirtual 251	java/nio/channels/FileLock:release	()V
    //   343: goto +7 -> 350
    //   346: astore_3
    //   347: goto +21 -> 368
    //   350: goto +18 -> 368
    //   353: aload_3
    //   354: ifnull +11 -> 365
    //   357: aload_3
    //   358: invokevirtual 251	java/nio/channels/FileLock:release	()V
    //   361: goto +4 -> 365
    //   364: astore_3
    //   365: aload 9
    //   367: athrow
    //   368: aload_0
    //   369: monitorexit
    //   370: lconst_0
    //   371: lreturn
    //   372: astore_3
    //   373: aload_0
    //   374: monitorexit
    //   375: aload_3
    //   376: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	377	0	this	MiniThumbFile
    //   0	377	1	paramLong	long
    //   6	161	3	localObject1	Object
    //   197	1	3	localIOException1	IOException
    //   228	94	3	localObject2	Object
    //   346	12	3	localIOException2	IOException
    //   364	1	3	localIOException3	IOException
    //   372	4	3	localObject3	Object
    //   16	188	4	l	long
    //   19	52	6	localObject4	Object
    //   22	53	7	localObject5	Object
    //   25	271	8	localObject6	Object
    //   32	138	9	localObject7	Object
    //   219	91	9	localObject8	Object
    //   317	49	9	localIOException4	IOException
    //   36	138	10	localObject9	Object
    //   224	115	10	localRuntimeException	RuntimeException
    // Exception table:
    //   from	to	target	type
    //   189	194	197	java/io/IOException
    //   38	46	219	finally
    //   57	67	219	finally
    //   78	93	219	finally
    //   104	122	219	finally
    //   133	142	219	finally
    //   153	164	219	finally
    //   175	184	219	finally
    //   229	234	219	finally
    //   237	242	219	finally
    //   245	254	219	finally
    //   257	264	219	finally
    //   267	276	219	finally
    //   279	290	219	finally
    //   293	304	219	finally
    //   322	333	219	finally
    //   38	46	224	java/lang/RuntimeException
    //   57	67	224	java/lang/RuntimeException
    //   78	93	224	java/lang/RuntimeException
    //   104	122	224	java/lang/RuntimeException
    //   133	142	224	java/lang/RuntimeException
    //   153	164	224	java/lang/RuntimeException
    //   175	184	224	java/lang/RuntimeException
    //   38	46	317	java/io/IOException
    //   57	67	317	java/io/IOException
    //   78	93	317	java/io/IOException
    //   104	122	317	java/io/IOException
    //   133	142	317	java/io/IOException
    //   153	164	317	java/io/IOException
    //   175	184	317	java/io/IOException
    //   211	216	346	java/io/IOException
    //   309	314	346	java/io/IOException
    //   338	343	346	java/io/IOException
    //   357	361	364	java/io/IOException
    //   2	7	372	finally
    //   189	194	372	finally
    //   211	216	372	finally
    //   309	314	372	finally
    //   338	343	372	finally
    //   357	361	372	finally
    //   365	368	372	finally
  }
  
  /* Error */
  public byte[] getMiniThumbFromFile(long paramLong, byte[] paramArrayOfByte)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial 209	android/media/MiniThumbFile:miniThumbDataFile	()Ljava/io/RandomAccessFile;
    //   6: astore 4
    //   8: aload 4
    //   10: ifnonnull +7 -> 17
    //   13: aload_0
    //   14: monitorexit
    //   15: aconst_null
    //   16: areturn
    //   17: ldc2_w 210
    //   20: lload_1
    //   21: lmul
    //   22: lstore 5
    //   24: aconst_null
    //   25: astore 7
    //   27: aconst_null
    //   28: astore 8
    //   30: aconst_null
    //   31: astore 9
    //   33: aload 9
    //   35: astore 4
    //   37: aload 7
    //   39: astore 10
    //   41: aload 8
    //   43: astore 11
    //   45: aload_0
    //   46: getfield 49	android/media/MiniThumbFile:mBuffer	Ljava/nio/ByteBuffer;
    //   49: invokevirtual 214	java/nio/ByteBuffer:clear	()Ljava/nio/Buffer;
    //   52: pop
    //   53: aload 9
    //   55: astore 4
    //   57: aload 7
    //   59: astore 10
    //   61: aload 8
    //   63: astore 11
    //   65: aload_0
    //   66: getfield 148	android/media/MiniThumbFile:mChannel	Ljava/nio/channels/FileChannel;
    //   69: lload 5
    //   71: ldc2_w 210
    //   74: iconst_1
    //   75: invokevirtual 224	java/nio/channels/FileChannel:lock	(JJZ)Ljava/nio/channels/FileLock;
    //   78: astore 9
    //   80: aload 9
    //   82: astore 4
    //   84: aload 9
    //   86: astore 10
    //   88: aload 9
    //   90: astore 11
    //   92: aload_0
    //   93: getfield 148	android/media/MiniThumbFile:mChannel	Ljava/nio/channels/FileChannel;
    //   96: aload_0
    //   97: getfield 49	android/media/MiniThumbFile:mBuffer	Ljava/nio/ByteBuffer;
    //   100: lload 5
    //   102: invokevirtual 228	java/nio/channels/FileChannel:read	(Ljava/nio/ByteBuffer;J)I
    //   105: istore 12
    //   107: iload 12
    //   109: bipush 13
    //   111: if_icmple +180 -> 291
    //   114: aload 9
    //   116: astore 4
    //   118: aload 9
    //   120: astore 10
    //   122: aload 9
    //   124: astore 11
    //   126: aload_0
    //   127: getfield 49	android/media/MiniThumbFile:mBuffer	Ljava/nio/ByteBuffer;
    //   130: iconst_0
    //   131: invokevirtual 231	java/nio/ByteBuffer:position	(I)Ljava/nio/Buffer;
    //   134: pop
    //   135: aload 9
    //   137: astore 4
    //   139: aload 9
    //   141: astore 10
    //   143: aload 9
    //   145: astore 11
    //   147: aload_0
    //   148: getfield 49	android/media/MiniThumbFile:mBuffer	Ljava/nio/ByteBuffer;
    //   151: invokevirtual 234	java/nio/ByteBuffer:get	()B
    //   154: istore 13
    //   156: aload 9
    //   158: astore 4
    //   160: aload 9
    //   162: astore 10
    //   164: aload 9
    //   166: astore 11
    //   168: aload_0
    //   169: getfield 49	android/media/MiniThumbFile:mBuffer	Ljava/nio/ByteBuffer;
    //   172: invokevirtual 238	java/nio/ByteBuffer:getLong	()J
    //   175: lstore 5
    //   177: aload 9
    //   179: astore 4
    //   181: aload 9
    //   183: astore 10
    //   185: aload 9
    //   187: astore 11
    //   189: aload_0
    //   190: getfield 49	android/media/MiniThumbFile:mBuffer	Ljava/nio/ByteBuffer;
    //   193: invokevirtual 280	java/nio/ByteBuffer:getInt	()I
    //   196: istore 14
    //   198: iload 12
    //   200: bipush 13
    //   202: iload 14
    //   204: iadd
    //   205: if_icmplt +86 -> 291
    //   208: iload 14
    //   210: ifeq +81 -> 291
    //   213: lload 5
    //   215: lconst_0
    //   216: lcmp
    //   217: ifeq +74 -> 291
    //   220: iload 13
    //   222: iconst_1
    //   223: if_icmpne +68 -> 291
    //   226: aload 9
    //   228: astore 4
    //   230: aload 9
    //   232: astore 10
    //   234: aload 9
    //   236: astore 11
    //   238: aload_3
    //   239: arraylength
    //   240: iload 14
    //   242: if_icmplt +49 -> 291
    //   245: aload 9
    //   247: astore 4
    //   249: aload 9
    //   251: astore 10
    //   253: aload 9
    //   255: astore 11
    //   257: aload_0
    //   258: getfield 49	android/media/MiniThumbFile:mBuffer	Ljava/nio/ByteBuffer;
    //   261: aload_3
    //   262: iconst_0
    //   263: iload 14
    //   265: invokevirtual 283	java/nio/ByteBuffer:get	([BII)Ljava/nio/ByteBuffer;
    //   268: pop
    //   269: aload 9
    //   271: ifnull +16 -> 287
    //   274: aload 9
    //   276: invokevirtual 251	java/nio/channels/FileLock:release	()V
    //   279: goto +8 -> 287
    //   282: astore 4
    //   284: goto +3 -> 287
    //   287: aload_0
    //   288: monitorexit
    //   289: aload_3
    //   290: areturn
    //   291: aload 9
    //   293: ifnull +15 -> 308
    //   296: aload 9
    //   298: invokevirtual 251	java/nio/channels/FileLock:release	()V
    //   301: goto +7 -> 308
    //   304: astore_3
    //   305: goto +203 -> 508
    //   308: goto +200 -> 508
    //   311: astore_3
    //   312: goto +200 -> 512
    //   315: astore_3
    //   316: aload 10
    //   318: astore 4
    //   320: new 72	java/lang/StringBuilder
    //   323: astore 11
    //   325: aload 10
    //   327: astore 4
    //   329: aload 11
    //   331: invokespecial 73	java/lang/StringBuilder:<init>	()V
    //   334: aload 10
    //   336: astore 4
    //   338: aload 11
    //   340: ldc_w 285
    //   343: invokevirtual 79	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   346: pop
    //   347: aload 10
    //   349: astore 4
    //   351: aload 11
    //   353: lload_1
    //   354: invokevirtual 243	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   357: pop
    //   358: aload 10
    //   360: astore 4
    //   362: aload 11
    //   364: ldc_w 258
    //   367: invokevirtual 79	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   370: pop
    //   371: aload 10
    //   373: astore 4
    //   375: aload 11
    //   377: aload_3
    //   378: invokevirtual 262	java/lang/Object:getClass	()Ljava/lang/Class;
    //   381: invokevirtual 265	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   384: pop
    //   385: aload 10
    //   387: astore 4
    //   389: ldc 15
    //   391: aload 11
    //   393: invokevirtual 85	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   396: invokestatic 133	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   399: pop
    //   400: aload 10
    //   402: ifnull -94 -> 308
    //   405: aload 10
    //   407: invokevirtual 251	java/nio/channels/FileLock:release	()V
    //   410: goto -102 -> 308
    //   413: astore_3
    //   414: aload 11
    //   416: astore 4
    //   418: new 72	java/lang/StringBuilder
    //   421: astore 10
    //   423: aload 11
    //   425: astore 4
    //   427: aload 10
    //   429: invokespecial 73	java/lang/StringBuilder:<init>	()V
    //   432: aload 11
    //   434: astore 4
    //   436: aload 10
    //   438: ldc_w 287
    //   441: invokevirtual 79	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   444: pop
    //   445: aload 11
    //   447: astore 4
    //   449: aload 10
    //   451: lload_1
    //   452: invokevirtual 243	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   455: pop
    //   456: aload 11
    //   458: astore 4
    //   460: aload 10
    //   462: ldc_w 289
    //   465: invokevirtual 79	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   468: pop
    //   469: aload 11
    //   471: astore 4
    //   473: aload 10
    //   475: aload_3
    //   476: invokevirtual 265	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   479: pop
    //   480: aload 11
    //   482: astore 4
    //   484: ldc 15
    //   486: aload 10
    //   488: invokevirtual 85	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   491: invokestatic 292	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   494: pop
    //   495: aload 11
    //   497: ifnull -189 -> 308
    //   500: aload 11
    //   502: invokevirtual 251	java/nio/channels/FileLock:release	()V
    //   505: goto -197 -> 308
    //   508: aload_0
    //   509: monitorexit
    //   510: aconst_null
    //   511: areturn
    //   512: aload 4
    //   514: ifnull +13 -> 527
    //   517: aload 4
    //   519: invokevirtual 251	java/nio/channels/FileLock:release	()V
    //   522: goto +5 -> 527
    //   525: astore 4
    //   527: aload_3
    //   528: athrow
    //   529: astore_3
    //   530: aload_0
    //   531: monitorexit
    //   532: aload_3
    //   533: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	534	0	this	MiniThumbFile
    //   0	534	1	paramLong	long
    //   0	534	3	paramArrayOfByte	byte[]
    //   6	242	4	localObject1	Object
    //   282	1	4	localIOException1	IOException
    //   318	200	4	localObject2	Object
    //   525	1	4	localIOException2	IOException
    //   22	192	5	l	long
    //   25	33	7	localObject3	Object
    //   28	34	8	localObject4	Object
    //   31	266	9	localFileLock	java.nio.channels.FileLock
    //   39	448	10	localObject5	Object
    //   43	458	11	localObject6	Object
    //   105	101	12	i	int
    //   154	70	13	j	int
    //   196	68	14	k	int
    // Exception table:
    //   from	to	target	type
    //   274	279	282	java/io/IOException
    //   296	301	304	java/io/IOException
    //   405	410	304	java/io/IOException
    //   500	505	304	java/io/IOException
    //   45	53	311	finally
    //   65	80	311	finally
    //   92	107	311	finally
    //   126	135	311	finally
    //   147	156	311	finally
    //   168	177	311	finally
    //   189	198	311	finally
    //   238	245	311	finally
    //   257	269	311	finally
    //   320	325	311	finally
    //   329	334	311	finally
    //   338	347	311	finally
    //   351	358	311	finally
    //   362	371	311	finally
    //   375	385	311	finally
    //   389	400	311	finally
    //   418	423	311	finally
    //   427	432	311	finally
    //   436	445	311	finally
    //   449	456	311	finally
    //   460	469	311	finally
    //   473	480	311	finally
    //   484	495	311	finally
    //   45	53	315	java/lang/RuntimeException
    //   65	80	315	java/lang/RuntimeException
    //   92	107	315	java/lang/RuntimeException
    //   126	135	315	java/lang/RuntimeException
    //   147	156	315	java/lang/RuntimeException
    //   168	177	315	java/lang/RuntimeException
    //   189	198	315	java/lang/RuntimeException
    //   238	245	315	java/lang/RuntimeException
    //   257	269	315	java/lang/RuntimeException
    //   45	53	413	java/io/IOException
    //   65	80	413	java/io/IOException
    //   92	107	413	java/io/IOException
    //   126	135	413	java/io/IOException
    //   147	156	413	java/io/IOException
    //   168	177	413	java/io/IOException
    //   189	198	413	java/io/IOException
    //   238	245	413	java/io/IOException
    //   257	269	413	java/io/IOException
    //   517	522	525	java/io/IOException
    //   2	8	529	finally
    //   274	279	529	finally
    //   296	301	529	finally
    //   405	410	529	finally
    //   500	505	529	finally
    //   517	522	529	finally
    //   527	529	529	finally
  }
  
  /* Error */
  public void saveMiniThumbToFile(byte[] paramArrayOfByte, long paramLong1, long paramLong2)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial 209	android/media/MiniThumbFile:miniThumbDataFile	()Ljava/io/RandomAccessFile;
    //   6: astore 6
    //   8: aload 6
    //   10: ifnonnull +6 -> 16
    //   13: aload_0
    //   14: monitorexit
    //   15: return
    //   16: ldc2_w 210
    //   19: lload_2
    //   20: lmul
    //   21: lstore 7
    //   23: aconst_null
    //   24: astore 9
    //   26: aconst_null
    //   27: astore 10
    //   29: aconst_null
    //   30: astore 6
    //   32: aconst_null
    //   33: astore 11
    //   35: aload_1
    //   36: ifnull +429 -> 465
    //   39: aload 11
    //   41: astore 6
    //   43: aload 9
    //   45: astore 12
    //   47: aload 10
    //   49: astore 13
    //   51: aload_1
    //   52: arraylength
    //   53: istore 14
    //   55: iload 14
    //   57: sipush 9987
    //   60: if_icmple +23 -> 83
    //   63: aload 11
    //   65: ifnull +15 -> 80
    //   68: aload 11
    //   70: invokevirtual 251	java/nio/channels/FileLock:release	()V
    //   73: goto +7 -> 80
    //   76: astore_1
    //   77: goto +3 -> 80
    //   80: aload_0
    //   81: monitorexit
    //   82: return
    //   83: aload 11
    //   85: astore 6
    //   87: aload 9
    //   89: astore 12
    //   91: aload 10
    //   93: astore 13
    //   95: aload_0
    //   96: getfield 49	android/media/MiniThumbFile:mBuffer	Ljava/nio/ByteBuffer;
    //   99: invokevirtual 214	java/nio/ByteBuffer:clear	()Ljava/nio/Buffer;
    //   102: pop
    //   103: aload 11
    //   105: astore 6
    //   107: aload 9
    //   109: astore 12
    //   111: aload 10
    //   113: astore 13
    //   115: aload_0
    //   116: getfield 49	android/media/MiniThumbFile:mBuffer	Ljava/nio/ByteBuffer;
    //   119: iconst_1
    //   120: invokevirtual 297	java/nio/ByteBuffer:put	(B)Ljava/nio/ByteBuffer;
    //   123: pop
    //   124: aload 11
    //   126: astore 6
    //   128: aload 9
    //   130: astore 12
    //   132: aload 10
    //   134: astore 13
    //   136: aload_0
    //   137: getfield 49	android/media/MiniThumbFile:mBuffer	Ljava/nio/ByteBuffer;
    //   140: lload 4
    //   142: invokevirtual 301	java/nio/ByteBuffer:putLong	(J)Ljava/nio/ByteBuffer;
    //   145: pop
    //   146: aload 11
    //   148: astore 6
    //   150: aload 9
    //   152: astore 12
    //   154: aload 10
    //   156: astore 13
    //   158: aload_0
    //   159: getfield 49	android/media/MiniThumbFile:mBuffer	Ljava/nio/ByteBuffer;
    //   162: aload_1
    //   163: arraylength
    //   164: invokevirtual 304	java/nio/ByteBuffer:putInt	(I)Ljava/nio/ByteBuffer;
    //   167: pop
    //   168: aload 11
    //   170: astore 6
    //   172: aload 9
    //   174: astore 12
    //   176: aload 10
    //   178: astore 13
    //   180: aload_0
    //   181: getfield 49	android/media/MiniThumbFile:mBuffer	Ljava/nio/ByteBuffer;
    //   184: aload_1
    //   185: invokevirtual 307	java/nio/ByteBuffer:put	([B)Ljava/nio/ByteBuffer;
    //   188: pop
    //   189: aload 11
    //   191: astore 6
    //   193: aload 9
    //   195: astore 12
    //   197: aload 10
    //   199: astore 13
    //   201: aload_0
    //   202: getfield 49	android/media/MiniThumbFile:mBuffer	Ljava/nio/ByteBuffer;
    //   205: invokevirtual 310	java/nio/ByteBuffer:flip	()Ljava/nio/Buffer;
    //   208: pop
    //   209: aload 11
    //   211: astore 6
    //   213: aload 9
    //   215: astore 12
    //   217: aload 10
    //   219: astore 13
    //   221: aload_0
    //   222: getfield 148	android/media/MiniThumbFile:mChannel	Ljava/nio/channels/FileChannel;
    //   225: lload 7
    //   227: ldc2_w 210
    //   230: iconst_0
    //   231: invokevirtual 224	java/nio/channels/FileChannel:lock	(JJZ)Ljava/nio/channels/FileLock;
    //   234: astore_1
    //   235: aload_1
    //   236: astore 6
    //   238: aload_1
    //   239: astore 12
    //   241: aload_1
    //   242: astore 13
    //   244: aload_0
    //   245: getfield 148	android/media/MiniThumbFile:mChannel	Ljava/nio/channels/FileChannel;
    //   248: aload_0
    //   249: getfield 49	android/media/MiniThumbFile:mBuffer	Ljava/nio/ByteBuffer;
    //   252: lload 7
    //   254: invokevirtual 254	java/nio/channels/FileChannel:write	(Ljava/nio/ByteBuffer;J)I
    //   257: pop
    //   258: aload_1
    //   259: astore 6
    //   261: goto +204 -> 465
    //   264: astore_1
    //   265: goto +183 -> 448
    //   268: astore_1
    //   269: aload 12
    //   271: astore 6
    //   273: new 72	java/lang/StringBuilder
    //   276: astore 13
    //   278: aload 12
    //   280: astore 6
    //   282: aload 13
    //   284: invokespecial 73	java/lang/StringBuilder:<init>	()V
    //   287: aload 12
    //   289: astore 6
    //   291: aload 13
    //   293: ldc_w 312
    //   296: invokevirtual 79	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   299: pop
    //   300: aload 12
    //   302: astore 6
    //   304: aload 13
    //   306: lload_2
    //   307: invokevirtual 243	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   310: pop
    //   311: aload 12
    //   313: astore 6
    //   315: aload 13
    //   317: ldc_w 314
    //   320: invokevirtual 79	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   323: pop
    //   324: aload 12
    //   326: astore 6
    //   328: aload 13
    //   330: aload_1
    //   331: invokevirtual 262	java/lang/Object:getClass	()Ljava/lang/Class;
    //   334: invokevirtual 265	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   337: pop
    //   338: aload 12
    //   340: astore 6
    //   342: ldc 15
    //   344: aload 13
    //   346: invokevirtual 85	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   349: invokestatic 133	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   352: pop
    //   353: aload 12
    //   355: ifnull +15 -> 370
    //   358: aload 12
    //   360: invokevirtual 251	java/nio/channels/FileLock:release	()V
    //   363: goto +7 -> 370
    //   366: astore_1
    //   367: goto +111 -> 478
    //   370: goto +108 -> 478
    //   373: astore 12
    //   375: aload 13
    //   377: astore 6
    //   379: new 72	java/lang/StringBuilder
    //   382: astore_1
    //   383: aload 13
    //   385: astore 6
    //   387: aload_1
    //   388: invokespecial 73	java/lang/StringBuilder:<init>	()V
    //   391: aload 13
    //   393: astore 6
    //   395: aload_1
    //   396: ldc_w 312
    //   399: invokevirtual 79	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   402: pop
    //   403: aload 13
    //   405: astore 6
    //   407: aload_1
    //   408: lload_2
    //   409: invokevirtual 243	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   412: pop
    //   413: aload 13
    //   415: astore 6
    //   417: aload_1
    //   418: ldc_w 316
    //   421: invokevirtual 79	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   424: pop
    //   425: aload 13
    //   427: astore 6
    //   429: ldc 15
    //   431: aload_1
    //   432: invokevirtual 85	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   435: aload 12
    //   437: invokestatic 318	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   440: pop
    //   441: aload 13
    //   443: astore 6
    //   445: aload 12
    //   447: athrow
    //   448: aload 6
    //   450: ifnull +13 -> 463
    //   453: aload 6
    //   455: invokevirtual 251	java/nio/channels/FileLock:release	()V
    //   458: goto +5 -> 463
    //   461: astore 6
    //   463: aload_1
    //   464: athrow
    //   465: aload 6
    //   467: ifnull -97 -> 370
    //   470: aload 6
    //   472: invokevirtual 251	java/nio/channels/FileLock:release	()V
    //   475: goto -105 -> 370
    //   478: aload_0
    //   479: monitorexit
    //   480: return
    //   481: astore_1
    //   482: aload_0
    //   483: monitorexit
    //   484: aload_1
    //   485: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	486	0	this	MiniThumbFile
    //   0	486	1	paramArrayOfByte	byte[]
    //   0	486	2	paramLong1	long
    //   0	486	4	paramLong2	long
    //   6	448	6	localObject1	Object
    //   461	10	6	localIOException1	IOException
    //   21	232	7	l	long
    //   24	190	9	localObject2	Object
    //   27	191	10	localObject3	Object
    //   33	177	11	localObject4	Object
    //   45	314	12	localObject5	Object
    //   373	73	12	localIOException2	IOException
    //   49	393	13	localObject6	Object
    //   53	8	14	i	int
    // Exception table:
    //   from	to	target	type
    //   68	73	76	java/io/IOException
    //   51	55	264	finally
    //   95	103	264	finally
    //   115	124	264	finally
    //   136	146	264	finally
    //   158	168	264	finally
    //   180	189	264	finally
    //   201	209	264	finally
    //   221	235	264	finally
    //   244	258	264	finally
    //   273	278	264	finally
    //   282	287	264	finally
    //   291	300	264	finally
    //   304	311	264	finally
    //   315	324	264	finally
    //   328	338	264	finally
    //   342	353	264	finally
    //   379	383	264	finally
    //   387	391	264	finally
    //   395	403	264	finally
    //   407	413	264	finally
    //   417	425	264	finally
    //   429	441	264	finally
    //   445	448	264	finally
    //   51	55	268	java/lang/RuntimeException
    //   95	103	268	java/lang/RuntimeException
    //   115	124	268	java/lang/RuntimeException
    //   136	146	268	java/lang/RuntimeException
    //   158	168	268	java/lang/RuntimeException
    //   180	189	268	java/lang/RuntimeException
    //   201	209	268	java/lang/RuntimeException
    //   221	235	268	java/lang/RuntimeException
    //   244	258	268	java/lang/RuntimeException
    //   358	363	366	java/io/IOException
    //   470	475	366	java/io/IOException
    //   51	55	373	java/io/IOException
    //   95	103	373	java/io/IOException
    //   115	124	373	java/io/IOException
    //   136	146	373	java/io/IOException
    //   158	168	373	java/io/IOException
    //   180	189	373	java/io/IOException
    //   201	209	373	java/io/IOException
    //   221	235	373	java/io/IOException
    //   244	258	373	java/io/IOException
    //   453	458	461	java/io/IOException
    //   2	8	481	finally
    //   68	73	481	finally
    //   358	363	481	finally
    //   453	458	481	finally
    //   463	465	481	finally
    //   470	475	481	finally
  }
}
