package android.media;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.IBinder;
import java.io.FileDescriptor;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MediaMetadataRetriever
{
  private static final int EMBEDDED_PICTURE_TYPE_ANY = 65535;
  public static final int METADATA_KEY_ALBUM = 1;
  public static final int METADATA_KEY_ALBUMARTIST = 13;
  public static final int METADATA_KEY_ARTIST = 2;
  public static final int METADATA_KEY_AUTHOR = 3;
  public static final int METADATA_KEY_BITRATE = 20;
  public static final int METADATA_KEY_CAPTURE_FRAMERATE = 25;
  public static final int METADATA_KEY_CD_TRACK_NUMBER = 0;
  public static final int METADATA_KEY_COMPILATION = 15;
  public static final int METADATA_KEY_COMPOSER = 4;
  public static final int METADATA_KEY_DATE = 5;
  public static final int METADATA_KEY_DISC_NUMBER = 14;
  public static final int METADATA_KEY_DURATION = 9;
  public static final int METADATA_KEY_EXIF_LENGTH = 34;
  public static final int METADATA_KEY_EXIF_OFFSET = 33;
  public static final int METADATA_KEY_GENRE = 6;
  public static final int METADATA_KEY_HAS_AUDIO = 16;
  public static final int METADATA_KEY_HAS_IMAGE = 26;
  public static final int METADATA_KEY_HAS_VIDEO = 17;
  public static final int METADATA_KEY_IMAGE_COUNT = 27;
  public static final int METADATA_KEY_IMAGE_HEIGHT = 30;
  public static final int METADATA_KEY_IMAGE_PRIMARY = 28;
  public static final int METADATA_KEY_IMAGE_ROTATION = 31;
  public static final int METADATA_KEY_IMAGE_WIDTH = 29;
  public static final int METADATA_KEY_IS_DRM = 22;
  public static final int METADATA_KEY_LOCATION = 23;
  public static final int METADATA_KEY_MIMETYPE = 12;
  public static final int METADATA_KEY_NUM_TRACKS = 10;
  public static final int METADATA_KEY_TIMED_TEXT_LANGUAGES = 21;
  public static final int METADATA_KEY_TITLE = 7;
  public static final int METADATA_KEY_VIDEO_FRAME_COUNT = 32;
  public static final int METADATA_KEY_VIDEO_HEIGHT = 19;
  public static final int METADATA_KEY_VIDEO_ROTATION = 24;
  public static final int METADATA_KEY_VIDEO_WIDTH = 18;
  public static final int METADATA_KEY_WRITER = 11;
  public static final int METADATA_KEY_YEAR = 8;
  public static final int OPTION_CLOSEST = 3;
  public static final int OPTION_CLOSEST_SYNC = 2;
  public static final int OPTION_NEXT_SYNC = 1;
  public static final int OPTION_PREVIOUS_SYNC = 0;
  private long mNativeContext;
  
  static
  {
    System.loadLibrary("media_jni");
    native_init();
  }
  
  public MediaMetadataRetriever()
  {
    native_setup();
  }
  
  private native List<Bitmap> _getFrameAtIndex(int paramInt1, int paramInt2, BitmapParams paramBitmapParams);
  
  private native Bitmap _getFrameAtTime(long paramLong, int paramInt1, int paramInt2, int paramInt3);
  
  private native Bitmap _getImageAtIndex(int paramInt, BitmapParams paramBitmapParams);
  
  private native void _setDataSource(MediaDataSource paramMediaDataSource)
    throws IllegalArgumentException;
  
  private native void _setDataSource(IBinder paramIBinder, String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2)
    throws IllegalArgumentException;
  
  private native byte[] getEmbeddedPicture(int paramInt);
  
  private List<Bitmap> getFramesAtIndexInternal(int paramInt1, int paramInt2, BitmapParams paramBitmapParams)
  {
    if ("yes".equals(extractMetadata(17)))
    {
      int i = Integer.parseInt(extractMetadata(32));
      if ((paramInt1 >= 0) && (paramInt2 >= 1) && (paramInt1 < i) && (paramInt1 <= i - paramInt2)) {
        return _getFrameAtIndex(paramInt1, paramInt2, paramBitmapParams);
      }
      paramBitmapParams = new StringBuilder();
      paramBitmapParams.append("Invalid frameIndex or numFrames: ");
      paramBitmapParams.append(paramInt1);
      paramBitmapParams.append(", ");
      paramBitmapParams.append(paramInt2);
      throw new IllegalArgumentException(paramBitmapParams.toString());
    }
    throw new IllegalStateException("Does not contail video or image sequences");
  }
  
  private Bitmap getImageAtIndexInternal(int paramInt, BitmapParams paramBitmapParams)
  {
    if ("yes".equals(extractMetadata(26)))
    {
      String str = extractMetadata(27);
      if (paramInt < Integer.parseInt(str)) {
        return _getImageAtIndex(paramInt, paramBitmapParams);
      }
      paramBitmapParams = new StringBuilder();
      paramBitmapParams.append("Invalid image index: ");
      paramBitmapParams.append(str);
      throw new IllegalArgumentException(paramBitmapParams.toString());
    }
    throw new IllegalStateException("Does not contail still images");
  }
  
  private final native void native_finalize();
  
  private static native void native_init();
  
  private native void native_setup();
  
  public native String extractMetadata(int paramInt);
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      native_finalize();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public byte[] getEmbeddedPicture()
  {
    return getEmbeddedPicture(65535);
  }
  
  public Bitmap getFrameAtIndex(int paramInt)
  {
    return (Bitmap)getFramesAtIndex(paramInt, 1).get(0);
  }
  
  public Bitmap getFrameAtIndex(int paramInt, BitmapParams paramBitmapParams)
  {
    return (Bitmap)getFramesAtIndex(paramInt, 1, paramBitmapParams).get(0);
  }
  
  public Bitmap getFrameAtTime()
  {
    return _getFrameAtTime(-1L, 2, -1, -1);
  }
  
  public Bitmap getFrameAtTime(long paramLong)
  {
    return getFrameAtTime(paramLong, 2);
  }
  
  public Bitmap getFrameAtTime(long paramLong, int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= 3)) {
      return _getFrameAtTime(paramLong, paramInt, -1, -1);
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unsupported option: ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public List<Bitmap> getFramesAtIndex(int paramInt1, int paramInt2)
  {
    return getFramesAtIndexInternal(paramInt1, paramInt2, null);
  }
  
  public List<Bitmap> getFramesAtIndex(int paramInt1, int paramInt2, BitmapParams paramBitmapParams)
  {
    return getFramesAtIndexInternal(paramInt1, paramInt2, paramBitmapParams);
  }
  
  public Bitmap getImageAtIndex(int paramInt)
  {
    return getImageAtIndexInternal(paramInt, null);
  }
  
  public Bitmap getImageAtIndex(int paramInt, BitmapParams paramBitmapParams)
  {
    return getImageAtIndexInternal(paramInt, paramBitmapParams);
  }
  
  public Bitmap getPrimaryImage()
  {
    return getImageAtIndexInternal(-1, null);
  }
  
  public Bitmap getPrimaryImage(BitmapParams paramBitmapParams)
  {
    return getImageAtIndexInternal(-1, paramBitmapParams);
  }
  
  public Bitmap getScaledFrameAtTime(long paramLong, int paramInt1, int paramInt2, int paramInt3)
  {
    if ((paramInt1 >= 0) && (paramInt1 <= 3))
    {
      if (paramInt2 > 0)
      {
        if (paramInt3 > 0) {
          return _getFrameAtTime(paramLong, paramInt1, paramInt2, paramInt3);
        }
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Invalid height: ");
        localStringBuilder.append(paramInt3);
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Invalid width: ");
      localStringBuilder.append(paramInt2);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unsupported option: ");
    localStringBuilder.append(paramInt1);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public native Bitmap getThumbnailImageAtIndex(int paramInt1, BitmapParams paramBitmapParams, int paramInt2, int paramInt3);
  
  public native void release();
  
  /* Error */
  public void setDataSource(android.content.Context paramContext, android.net.Uri paramUri)
    throws IllegalArgumentException, java.lang.SecurityException
  {
    // Byte code:
    //   0: aload_2
    //   1: ifnull +288 -> 289
    //   4: aload_2
    //   5: invokevirtual 246	android/net/Uri:getScheme	()Ljava/lang/String;
    //   8: astore_3
    //   9: aload_3
    //   10: ifnull +270 -> 280
    //   13: aload_3
    //   14: ldc -8
    //   16: invokevirtual 135	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   19: ifeq +6 -> 25
    //   22: goto +258 -> 280
    //   25: aconst_null
    //   26: astore 4
    //   28: aconst_null
    //   29: astore 5
    //   31: aload 5
    //   33: astore 6
    //   35: aload 4
    //   37: astore_3
    //   38: aload_1
    //   39: invokevirtual 254	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   42: astore_1
    //   43: aload 5
    //   45: astore 6
    //   47: aload 4
    //   49: astore_3
    //   50: aload_1
    //   51: aload_2
    //   52: ldc_w 256
    //   55: invokevirtual 262	android/content/ContentResolver:openAssetFileDescriptor	(Landroid/net/Uri;Ljava/lang/String;)Landroid/content/res/AssetFileDescriptor;
    //   58: astore_1
    //   59: aload_1
    //   60: ifnull +118 -> 178
    //   63: aload_1
    //   64: astore 6
    //   66: aload_1
    //   67: astore_3
    //   68: aload_1
    //   69: invokevirtual 268	android/content/res/AssetFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   72: astore 5
    //   74: aload_1
    //   75: astore 6
    //   77: aload_1
    //   78: astore_3
    //   79: aload 5
    //   81: invokevirtual 274	java/io/FileDescriptor:valid	()Z
    //   84: ifeq +66 -> 150
    //   87: aload_1
    //   88: astore 6
    //   90: aload_1
    //   91: astore_3
    //   92: aload_1
    //   93: invokevirtual 278	android/content/res/AssetFileDescriptor:getDeclaredLength	()J
    //   96: lconst_0
    //   97: lcmp
    //   98: ifge +17 -> 115
    //   101: aload_1
    //   102: astore 6
    //   104: aload_1
    //   105: astore_3
    //   106: aload_0
    //   107: aload 5
    //   109: invokevirtual 281	android/media/MediaMetadataRetriever:setDataSource	(Ljava/io/FileDescriptor;)V
    //   112: goto +22 -> 134
    //   115: aload_1
    //   116: astore 6
    //   118: aload_1
    //   119: astore_3
    //   120: aload_0
    //   121: aload 5
    //   123: aload_1
    //   124: invokevirtual 284	android/content/res/AssetFileDescriptor:getStartOffset	()J
    //   127: aload_1
    //   128: invokevirtual 278	android/content/res/AssetFileDescriptor:getDeclaredLength	()J
    //   131: invokevirtual 287	android/media/MediaMetadataRetriever:setDataSource	(Ljava/io/FileDescriptor;JJ)V
    //   134: aload_1
    //   135: ifnull +14 -> 149
    //   138: aload_1
    //   139: invokevirtual 290	android/content/res/AssetFileDescriptor:close	()V
    //   142: goto +7 -> 149
    //   145: astore_1
    //   146: goto +3 -> 149
    //   149: return
    //   150: aload_1
    //   151: astore 6
    //   153: aload_1
    //   154: astore_3
    //   155: new 119	java/lang/IllegalArgumentException
    //   158: astore 5
    //   160: aload_1
    //   161: astore 6
    //   163: aload_1
    //   164: astore_3
    //   165: aload 5
    //   167: invokespecial 291	java/lang/IllegalArgumentException:<init>	()V
    //   170: aload_1
    //   171: astore 6
    //   173: aload_1
    //   174: astore_3
    //   175: aload 5
    //   177: athrow
    //   178: aload_1
    //   179: astore 6
    //   181: aload_1
    //   182: astore_3
    //   183: new 119	java/lang/IllegalArgumentException
    //   186: astore 5
    //   188: aload_1
    //   189: astore 6
    //   191: aload_1
    //   192: astore_3
    //   193: aload 5
    //   195: invokespecial 291	java/lang/IllegalArgumentException:<init>	()V
    //   198: aload_1
    //   199: astore 6
    //   201: aload_1
    //   202: astore_3
    //   203: aload 5
    //   205: athrow
    //   206: astore_1
    //   207: aload 5
    //   209: astore 6
    //   211: aload 4
    //   213: astore_3
    //   214: new 119	java/lang/IllegalArgumentException
    //   217: astore_1
    //   218: aload 5
    //   220: astore 6
    //   222: aload 4
    //   224: astore_3
    //   225: aload_1
    //   226: invokespecial 291	java/lang/IllegalArgumentException:<init>	()V
    //   229: aload 5
    //   231: astore 6
    //   233: aload 4
    //   235: astore_3
    //   236: aload_1
    //   237: athrow
    //   238: astore_1
    //   239: aload 6
    //   241: ifnull +12 -> 253
    //   244: aload 6
    //   246: invokevirtual 290	android/content/res/AssetFileDescriptor:close	()V
    //   249: goto +4 -> 253
    //   252: astore_2
    //   253: aload_1
    //   254: athrow
    //   255: astore_1
    //   256: aload_3
    //   257: ifnull +14 -> 271
    //   260: aload_3
    //   261: invokevirtual 290	android/content/res/AssetFileDescriptor:close	()V
    //   264: goto +7 -> 271
    //   267: astore_1
    //   268: goto +3 -> 271
    //   271: aload_0
    //   272: aload_2
    //   273: invokevirtual 292	android/net/Uri:toString	()Ljava/lang/String;
    //   276: invokevirtual 294	android/media/MediaMetadataRetriever:setDataSource	(Ljava/lang/String;)V
    //   279: return
    //   280: aload_0
    //   281: aload_2
    //   282: invokevirtual 297	android/net/Uri:getPath	()Ljava/lang/String;
    //   285: invokevirtual 294	android/media/MediaMetadataRetriever:setDataSource	(Ljava/lang/String;)V
    //   288: return
    //   289: new 119	java/lang/IllegalArgumentException
    //   292: dup
    //   293: invokespecial 291	java/lang/IllegalArgumentException:<init>	()V
    //   296: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	297	0	this	MediaMetadataRetriever
    //   0	297	1	paramContext	android.content.Context
    //   0	297	2	paramUri	android.net.Uri
    //   8	253	3	localObject1	Object
    //   26	208	4	localObject2	Object
    //   29	201	5	localObject3	Object
    //   33	212	6	localObject4	Object
    // Exception table:
    //   from	to	target	type
    //   138	142	145	java/io/IOException
    //   50	59	206	java/io/FileNotFoundException
    //   38	43	238	finally
    //   50	59	238	finally
    //   68	74	238	finally
    //   79	87	238	finally
    //   92	101	238	finally
    //   106	112	238	finally
    //   120	134	238	finally
    //   155	160	238	finally
    //   165	170	238	finally
    //   175	178	238	finally
    //   183	188	238	finally
    //   193	198	238	finally
    //   203	206	238	finally
    //   214	218	238	finally
    //   225	229	238	finally
    //   236	238	238	finally
    //   244	249	252	java/io/IOException
    //   38	43	255	java/lang/SecurityException
    //   50	59	255	java/lang/SecurityException
    //   68	74	255	java/lang/SecurityException
    //   79	87	255	java/lang/SecurityException
    //   92	101	255	java/lang/SecurityException
    //   106	112	255	java/lang/SecurityException
    //   120	134	255	java/lang/SecurityException
    //   155	160	255	java/lang/SecurityException
    //   165	170	255	java/lang/SecurityException
    //   175	178	255	java/lang/SecurityException
    //   183	188	255	java/lang/SecurityException
    //   193	198	255	java/lang/SecurityException
    //   203	206	255	java/lang/SecurityException
    //   214	218	255	java/lang/SecurityException
    //   225	229	255	java/lang/SecurityException
    //   236	238	255	java/lang/SecurityException
    //   260	264	267	java/io/IOException
  }
  
  public void setDataSource(MediaDataSource paramMediaDataSource)
    throws IllegalArgumentException
  {
    _setDataSource(paramMediaDataSource);
  }
  
  public void setDataSource(FileDescriptor paramFileDescriptor)
    throws IllegalArgumentException
  {
    setDataSource(paramFileDescriptor, 0L, 576460752303423487L);
  }
  
  public native void setDataSource(FileDescriptor paramFileDescriptor, long paramLong1, long paramLong2)
    throws IllegalArgumentException;
  
  /* Error */
  public void setDataSource(String paramString)
    throws IllegalArgumentException
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnull +84 -> 85
    //   4: new 303	java/io/FileInputStream
    //   7: astore_2
    //   8: aload_2
    //   9: aload_1
    //   10: invokespecial 304	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
    //   13: aconst_null
    //   14: astore_1
    //   15: aload_0
    //   16: aload_2
    //   17: invokevirtual 307	java/io/FileInputStream:getFD	()Ljava/io/FileDescriptor;
    //   20: lconst_0
    //   21: ldc2_w 300
    //   24: invokevirtual 287	android/media/MediaMetadataRetriever:setDataSource	(Ljava/io/FileDescriptor;JJ)V
    //   27: aload_2
    //   28: invokevirtual 308	java/io/FileInputStream:close	()V
    //   31: return
    //   32: astore_3
    //   33: goto +8 -> 41
    //   36: astore_3
    //   37: aload_3
    //   38: astore_1
    //   39: aload_3
    //   40: athrow
    //   41: aload_1
    //   42: ifnull +19 -> 61
    //   45: aload_2
    //   46: invokevirtual 308	java/io/FileInputStream:close	()V
    //   49: goto +16 -> 65
    //   52: astore_2
    //   53: aload_1
    //   54: aload_2
    //   55: invokevirtual 312	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   58: goto +7 -> 65
    //   61: aload_2
    //   62: invokevirtual 308	java/io/FileInputStream:close	()V
    //   65: aload_3
    //   66: athrow
    //   67: astore_1
    //   68: new 119	java/lang/IllegalArgumentException
    //   71: dup
    //   72: invokespecial 291	java/lang/IllegalArgumentException:<init>	()V
    //   75: athrow
    //   76: astore_1
    //   77: new 119	java/lang/IllegalArgumentException
    //   80: dup
    //   81: invokespecial 291	java/lang/IllegalArgumentException:<init>	()V
    //   84: athrow
    //   85: new 119	java/lang/IllegalArgumentException
    //   88: dup
    //   89: invokespecial 291	java/lang/IllegalArgumentException:<init>	()V
    //   92: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	93	0	this	MediaMetadataRetriever
    //   0	93	1	paramString	String
    //   7	39	2	localFileInputStream	java.io.FileInputStream
    //   52	10	2	localThrowable1	Throwable
    //   32	1	3	localObject	Object
    //   36	30	3	localThrowable2	Throwable
    // Exception table:
    //   from	to	target	type
    //   15	27	32	finally
    //   39	41	32	finally
    //   15	27	36	java/lang/Throwable
    //   45	49	52	java/lang/Throwable
    //   4	13	67	java/io/IOException
    //   27	31	67	java/io/IOException
    //   45	49	67	java/io/IOException
    //   53	58	67	java/io/IOException
    //   61	65	67	java/io/IOException
    //   65	67	67	java/io/IOException
    //   4	13	76	java/io/FileNotFoundException
    //   27	31	76	java/io/FileNotFoundException
    //   45	49	76	java/io/FileNotFoundException
    //   53	58	76	java/io/FileNotFoundException
    //   61	65	76	java/io/FileNotFoundException
    //   65	67	76	java/io/FileNotFoundException
  }
  
  public void setDataSource(String paramString, Map<String, String> paramMap)
    throws IllegalArgumentException
  {
    int i = 0;
    String[] arrayOfString1 = new String[paramMap.size()];
    String[] arrayOfString2 = new String[paramMap.size()];
    paramMap = paramMap.entrySet().iterator();
    while (paramMap.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)paramMap.next();
      arrayOfString1[i] = ((String)localEntry.getKey());
      arrayOfString2[i] = ((String)localEntry.getValue());
      i++;
    }
    _setDataSource(MediaHTTPService.createHttpServiceBinderIfNecessary(paramString), paramString, arrayOfString1, arrayOfString2);
  }
  
  public static final class BitmapParams
  {
    private Bitmap.Config inPreferredConfig = Bitmap.Config.ARGB_8888;
    private Bitmap.Config outActualConfig = Bitmap.Config.ARGB_8888;
    
    public BitmapParams() {}
    
    public Bitmap.Config getActualConfig()
    {
      return outActualConfig;
    }
    
    public Bitmap.Config getPreferredConfig()
    {
      return inPreferredConfig;
    }
    
    public void setPreferredConfig(Bitmap.Config paramConfig)
    {
      if (paramConfig != null)
      {
        inPreferredConfig = paramConfig;
        return;
      }
      throw new IllegalArgumentException("preferred config can't be null");
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Option {}
}
