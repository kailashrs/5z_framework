package android.media;

import android.content.res.AssetFileDescriptor;
import android.os.IBinder;
import android.os.IHwBinder;
import android.os.PersistableBundle;
import com.android.internal.util.Preconditions;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

public final class MediaExtractor
{
  public static final int SAMPLE_FLAG_ENCRYPTED = 2;
  public static final int SAMPLE_FLAG_PARTIAL_FRAME = 4;
  public static final int SAMPLE_FLAG_SYNC = 1;
  public static final int SEEK_TO_CLOSEST_SYNC = 2;
  public static final int SEEK_TO_NEXT_SYNC = 1;
  public static final int SEEK_TO_PREVIOUS_SYNC = 0;
  private MediaCas mMediaCas;
  private long mNativeContext;
  
  static
  {
    System.loadLibrary("media_jni");
    native_init();
  }
  
  public MediaExtractor()
  {
    native_setup();
  }
  
  private native Map<String, Object> getFileFormatNative();
  
  private native Map<String, Object> getTrackFormatNative(int paramInt);
  
  private final native void nativeSetDataSource(IBinder paramIBinder, String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2)
    throws IOException;
  
  private final native void nativeSetMediaCas(IHwBinder paramIHwBinder);
  
  private final native void native_finalize();
  
  private native PersistableBundle native_getMetrics();
  
  private static final native void native_init();
  
  private final native void native_setup();
  
  private ArrayList<Byte> toByteArray(byte[] paramArrayOfByte)
  {
    ArrayList localArrayList = new ArrayList(paramArrayOfByte.length);
    for (int i = 0; i < paramArrayOfByte.length; i++) {
      localArrayList.add(i, Byte.valueOf(paramArrayOfByte[i]));
    }
    return localArrayList;
  }
  
  public native boolean advance();
  
  protected void finalize()
  {
    native_finalize();
  }
  
  public List<AudioPresentation> getAudioPresentations(int paramInt)
  {
    return new ArrayList();
  }
  
  public native long getCachedDuration();
  
  public CasInfo getCasInfo(int paramInt)
  {
    Map localMap = getTrackFormatNative(paramInt);
    if (localMap.containsKey("ca-system-id"))
    {
      paramInt = ((Integer)localMap.get("ca-system-id")).intValue();
      byte[] arrayOfByte = null;
      Object localObject = arrayOfByte;
      if (mMediaCas != null)
      {
        localObject = arrayOfByte;
        if (localMap.containsKey("ca-session-id"))
        {
          localObject = (ByteBuffer)localMap.get("ca-session-id");
          ((ByteBuffer)localObject).rewind();
          arrayOfByte = new byte[((ByteBuffer)localObject).remaining()];
          ((ByteBuffer)localObject).get(arrayOfByte);
          localObject = mMediaCas.createFromSessionId(toByteArray(arrayOfByte));
        }
      }
      return new CasInfo(paramInt, (MediaCas.Session)localObject);
    }
    return null;
  }
  
  public DrmInitData getDrmInitData()
  {
    Object localObject1 = getFileFormatNative();
    if (localObject1 == null) {
      return null;
    }
    Object localObject2;
    if (((Map)localObject1).containsKey("pssh"))
    {
      localObject2 = getPsshInfo();
      localObject1 = new HashMap();
      Iterator localIterator = ((Map)localObject2).entrySet().iterator();
      while (localIterator.hasNext())
      {
        localObject2 = (Map.Entry)localIterator.next();
        ((Map)localObject1).put((UUID)((Map.Entry)localObject2).getKey(), new DrmInitData.SchemeInitData("cenc", (byte[])((Map.Entry)localObject2).getValue()));
      }
      new DrmInitData()
      {
        public DrmInitData.SchemeInitData get(UUID paramAnonymousUUID)
        {
          return (DrmInitData.SchemeInitData)val$initDataMap.get(paramAnonymousUUID);
        }
      };
    }
    int i = getTrackCount();
    int j = 0;
    while (j < i)
    {
      localObject1 = getTrackFormatNative(j);
      if (!((Map)localObject1).containsKey("crypto-key"))
      {
        j++;
      }
      else
      {
        localObject1 = (ByteBuffer)((Map)localObject1).get("crypto-key");
        ((ByteBuffer)localObject1).rewind();
        localObject2 = new byte[((ByteBuffer)localObject1).remaining()];
        ((ByteBuffer)localObject1).get((byte[])localObject2);
        new DrmInitData()
        {
          public DrmInitData.SchemeInitData get(UUID paramAnonymousUUID)
          {
            return new DrmInitData.SchemeInitData("webm", val$data);
          }
        };
      }
    }
    return null;
  }
  
  public PersistableBundle getMetrics()
  {
    return native_getMetrics();
  }
  
  public Map<UUID, byte[]> getPsshInfo()
  {
    HashMap localHashMap = null;
    Object localObject1 = getFileFormatNative();
    Object localObject2 = localHashMap;
    if (localObject1 != null)
    {
      localObject2 = localHashMap;
      if (((Map)localObject1).containsKey("pssh"))
      {
        ByteBuffer localByteBuffer = (ByteBuffer)((Map)localObject1).get("pssh");
        localByteBuffer.order(ByteOrder.nativeOrder());
        localByteBuffer.rewind();
        ((Map)localObject1).remove("pssh");
        localHashMap = new HashMap();
        for (;;)
        {
          localObject2 = localHashMap;
          if (localByteBuffer.remaining() <= 0) {
            break;
          }
          localByteBuffer.order(ByteOrder.BIG_ENDIAN);
          localObject2 = new UUID(localByteBuffer.getLong(), localByteBuffer.getLong());
          localByteBuffer.order(ByteOrder.nativeOrder());
          localObject1 = new byte[localByteBuffer.getInt()];
          localByteBuffer.get((byte[])localObject1);
          localHashMap.put(localObject2, localObject1);
        }
      }
    }
    return localObject2;
  }
  
  public native boolean getSampleCryptoInfo(MediaCodec.CryptoInfo paramCryptoInfo);
  
  public native int getSampleFlags();
  
  public native long getSampleSize();
  
  public native long getSampleTime();
  
  public native int getSampleTrackIndex();
  
  public final native int getTrackCount();
  
  public MediaFormat getTrackFormat(int paramInt)
  {
    return new MediaFormat(getTrackFormatNative(paramInt));
  }
  
  public native boolean hasCacheReachedEndOfStream();
  
  public native int readSampleData(ByteBuffer paramByteBuffer, int paramInt);
  
  public final native void release();
  
  public native void seekTo(long paramLong, int paramInt);
  
  public native void selectTrack(int paramInt);
  
  /* Error */
  public final void setDataSource(android.content.Context paramContext, android.net.Uri paramUri, Map<String, String> paramMap)
    throws IOException
  {
    // Byte code:
    //   0: aload_2
    //   1: invokevirtual 272	android/net/Uri:getScheme	()Ljava/lang/String;
    //   4: astore 4
    //   6: aload 4
    //   8: ifnull +173 -> 181
    //   11: aload 4
    //   13: ldc_w 274
    //   16: invokevirtual 279	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   19: ifeq +6 -> 25
    //   22: goto +159 -> 181
    //   25: aconst_null
    //   26: astore 4
    //   28: aconst_null
    //   29: astore 5
    //   31: aconst_null
    //   32: astore 6
    //   34: aload_1
    //   35: invokevirtual 285	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   38: aload_2
    //   39: ldc_w 287
    //   42: invokevirtual 293	android/content/ContentResolver:openAssetFileDescriptor	(Landroid/net/Uri;Ljava/lang/String;)Landroid/content/res/AssetFileDescriptor;
    //   45: astore_1
    //   46: aload_1
    //   47: ifnonnull +12 -> 59
    //   50: aload_1
    //   51: ifnull +7 -> 58
    //   54: aload_1
    //   55: invokevirtual 298	android/content/res/AssetFileDescriptor:close	()V
    //   58: return
    //   59: aload_1
    //   60: astore 6
    //   62: aload_1
    //   63: astore 4
    //   65: aload_1
    //   66: astore 5
    //   68: aload_1
    //   69: invokevirtual 301	android/content/res/AssetFileDescriptor:getDeclaredLength	()J
    //   72: lconst_0
    //   73: lcmp
    //   74: ifge +23 -> 97
    //   77: aload_1
    //   78: astore 6
    //   80: aload_1
    //   81: astore 4
    //   83: aload_1
    //   84: astore 5
    //   86: aload_0
    //   87: aload_1
    //   88: invokevirtual 305	android/content/res/AssetFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   91: invokevirtual 308	android/media/MediaExtractor:setDataSource	(Ljava/io/FileDescriptor;)V
    //   94: goto +28 -> 122
    //   97: aload_1
    //   98: astore 6
    //   100: aload_1
    //   101: astore 4
    //   103: aload_1
    //   104: astore 5
    //   106: aload_0
    //   107: aload_1
    //   108: invokevirtual 305	android/content/res/AssetFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   111: aload_1
    //   112: invokevirtual 311	android/content/res/AssetFileDescriptor:getStartOffset	()J
    //   115: aload_1
    //   116: invokevirtual 301	android/content/res/AssetFileDescriptor:getDeclaredLength	()J
    //   119: invokevirtual 314	android/media/MediaExtractor:setDataSource	(Ljava/io/FileDescriptor;JJ)V
    //   122: aload_1
    //   123: ifnull +7 -> 130
    //   126: aload_1
    //   127: invokevirtual 298	android/content/res/AssetFileDescriptor:close	()V
    //   130: return
    //   131: astore_1
    //   132: aload 6
    //   134: ifnull +8 -> 142
    //   137: aload 6
    //   139: invokevirtual 298	android/content/res/AssetFileDescriptor:close	()V
    //   142: aload_1
    //   143: athrow
    //   144: astore_1
    //   145: aload 4
    //   147: ifnull +24 -> 171
    //   150: aload 4
    //   152: invokevirtual 298	android/content/res/AssetFileDescriptor:close	()V
    //   155: goto +16 -> 171
    //   158: astore_1
    //   159: aload 5
    //   161: ifnull +10 -> 171
    //   164: aload 5
    //   166: astore 4
    //   168: goto -18 -> 150
    //   171: aload_0
    //   172: aload_2
    //   173: invokevirtual 317	android/net/Uri:toString	()Ljava/lang/String;
    //   176: aload_3
    //   177: invokevirtual 320	android/media/MediaExtractor:setDataSource	(Ljava/lang/String;Ljava/util/Map;)V
    //   180: return
    //   181: aload_0
    //   182: aload_2
    //   183: invokevirtual 323	android/net/Uri:getPath	()Ljava/lang/String;
    //   186: invokevirtual 325	android/media/MediaExtractor:setDataSource	(Ljava/lang/String;)V
    //   189: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	190	0	this	MediaExtractor
    //   0	190	1	paramContext	android.content.Context
    //   0	190	2	paramUri	android.net.Uri
    //   0	190	3	paramMap	Map<String, String>
    //   4	163	4	localObject	Object
    //   29	136	5	localContext1	android.content.Context
    //   32	106	6	localContext2	android.content.Context
    // Exception table:
    //   from	to	target	type
    //   34	46	131	finally
    //   68	77	131	finally
    //   86	94	131	finally
    //   106	122	131	finally
    //   34	46	144	java/io/IOException
    //   68	77	144	java/io/IOException
    //   86	94	144	java/io/IOException
    //   106	122	144	java/io/IOException
    //   34	46	158	java/lang/SecurityException
    //   68	77	158	java/lang/SecurityException
    //   86	94	158	java/lang/SecurityException
    //   106	122	158	java/lang/SecurityException
  }
  
  public final void setDataSource(AssetFileDescriptor paramAssetFileDescriptor)
    throws IOException, IllegalArgumentException, IllegalStateException
  {
    Preconditions.checkNotNull(paramAssetFileDescriptor);
    if (paramAssetFileDescriptor.getDeclaredLength() < 0L) {
      setDataSource(paramAssetFileDescriptor.getFileDescriptor());
    } else {
      setDataSource(paramAssetFileDescriptor.getFileDescriptor(), paramAssetFileDescriptor.getStartOffset(), paramAssetFileDescriptor.getDeclaredLength());
    }
  }
  
  public final native void setDataSource(MediaDataSource paramMediaDataSource)
    throws IOException;
  
  public final void setDataSource(FileDescriptor paramFileDescriptor)
    throws IOException
  {
    setDataSource(paramFileDescriptor, 0L, 576460752303423487L);
  }
  
  public final native void setDataSource(FileDescriptor paramFileDescriptor, long paramLong1, long paramLong2)
    throws IOException;
  
  public final void setDataSource(String paramString)
    throws IOException
  {
    nativeSetDataSource(MediaHTTPService.createHttpServiceBinderIfNecessary(paramString), paramString, null, null);
  }
  
  public final void setDataSource(String paramString, Map<String, String> paramMap)
    throws IOException
  {
    Object localObject1 = null;
    Object localObject2 = null;
    if (paramMap != null)
    {
      String[] arrayOfString1 = new String[paramMap.size()];
      String[] arrayOfString2 = new String[paramMap.size()];
      int i = 0;
      paramMap = paramMap.entrySet().iterator();
      for (;;)
      {
        localObject1 = arrayOfString1;
        localObject2 = arrayOfString2;
        if (!paramMap.hasNext()) {
          break;
        }
        localObject1 = (Map.Entry)paramMap.next();
        arrayOfString1[i] = ((String)((Map.Entry)localObject1).getKey());
        arrayOfString2[i] = ((String)((Map.Entry)localObject1).getValue());
        i++;
      }
    }
    nativeSetDataSource(MediaHTTPService.createHttpServiceBinderIfNecessary(paramString), paramString, (String[])localObject1, localObject2);
  }
  
  public final void setMediaCas(MediaCas paramMediaCas)
  {
    mMediaCas = paramMediaCas;
    nativeSetMediaCas(paramMediaCas.getBinder());
  }
  
  public native void unselectTrack(int paramInt);
  
  public static final class CasInfo
  {
    private final MediaCas.Session mSession;
    private final int mSystemId;
    
    CasInfo(int paramInt, MediaCas.Session paramSession)
    {
      mSystemId = paramInt;
      mSession = paramSession;
    }
    
    public MediaCas.Session getSession()
    {
      return mSession;
    }
    
    public int getSystemId()
    {
      return mSystemId;
    }
  }
  
  public static final class MetricsConstants
  {
    public static final String FORMAT = "android.media.mediaextractor.fmt";
    public static final String MIME_TYPE = "android.media.mediaextractor.mime";
    public static final String TRACKS = "android.media.mediaextractor.ntrk";
    
    private MetricsConstants() {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SampleFlag {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SeekMode {}
}
