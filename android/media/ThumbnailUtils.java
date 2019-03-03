package android.media;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import java.io.IOException;

public class ThumbnailUtils
{
  private static final int MAX_NUM_PIXELS_MICRO_THUMBNAIL = 19200;
  private static final int MAX_NUM_PIXELS_THUMBNAIL = 196608;
  private static final int OPTIONS_NONE = 0;
  public static final int OPTIONS_RECYCLE_INPUT = 2;
  private static final int OPTIONS_SCALE_UP = 1;
  private static final String TAG = "ThumbnailUtils";
  public static final int TARGET_SIZE_MICRO_THUMBNAIL = 96;
  public static final int TARGET_SIZE_MINI_THUMBNAIL = 320;
  private static final int UNCONSTRAINED = -1;
  
  public ThumbnailUtils() {}
  
  private static void closeSilently(ParcelFileDescriptor paramParcelFileDescriptor)
  {
    if (paramParcelFileDescriptor == null) {
      return;
    }
    try
    {
      paramParcelFileDescriptor.close();
    }
    catch (Throwable paramParcelFileDescriptor) {}
  }
  
  private static int computeInitialSampleSize(BitmapFactory.Options paramOptions, int paramInt1, int paramInt2)
  {
    double d1 = outWidth;
    double d2 = outHeight;
    int i;
    if (paramInt2 == -1) {
      i = 1;
    } else {
      i = (int)Math.ceil(Math.sqrt(d1 * d2 / paramInt2));
    }
    int j;
    if (paramInt1 == -1) {
      j = 128;
    } else {
      j = (int)Math.min(Math.floor(d1 / paramInt1), Math.floor(d2 / paramInt1));
    }
    if (j < i) {
      return i;
    }
    if ((paramInt2 == -1) && (paramInt1 == -1)) {
      return 1;
    }
    if (paramInt1 == -1) {
      return i;
    }
    return j;
  }
  
  private static int computeSampleSize(BitmapFactory.Options paramOptions, int paramInt1, int paramInt2)
  {
    int i = computeInitialSampleSize(paramOptions, paramInt1, paramInt2);
    if (i <= 8)
    {
      paramInt1 = 1;
      for (;;)
      {
        paramInt2 = paramInt1;
        if (paramInt1 >= i) {
          break;
        }
        paramInt1 <<= 1;
      }
    }
    paramInt2 = 8 * ((i + 7) / 8);
    return paramInt2;
  }
  
  /* Error */
  public static Bitmap createImageThumbnail(String paramString, int paramInt)
  {
    // Byte code:
    //   0: iload_1
    //   1: iconst_1
    //   2: if_icmpne +8 -> 10
    //   5: iconst_1
    //   6: istore_2
    //   7: goto +5 -> 12
    //   10: iconst_0
    //   11: istore_2
    //   12: iload_2
    //   13: ifeq +10 -> 23
    //   16: sipush 320
    //   19: istore_3
    //   20: goto +6 -> 26
    //   23: bipush 96
    //   25: istore_3
    //   26: iload_2
    //   27: ifeq +9 -> 36
    //   30: ldc 14
    //   32: istore_2
    //   33: goto +7 -> 40
    //   36: sipush 19200
    //   39: istore_2
    //   40: new 8	android/media/ThumbnailUtils$SizedThumbnailBitmap
    //   43: dup
    //   44: aconst_null
    //   45: invokespecial 82	android/media/ThumbnailUtils$SizedThumbnailBitmap:<init>	(Landroid/media/ThumbnailUtils$1;)V
    //   48: astore 4
    //   50: aconst_null
    //   51: astore 5
    //   53: aload_0
    //   54: invokestatic 88	android/media/MediaFile:getFileType	(Ljava/lang/String;)Landroid/media/MediaFile$MediaFileType;
    //   57: astore 6
    //   59: aload 5
    //   61: astore 7
    //   63: aload 6
    //   65: ifnull +67 -> 132
    //   68: aload 6
    //   70: getfield 93	android/media/MediaFile$MediaFileType:fileType	I
    //   73: bipush 31
    //   75: if_icmpeq +42 -> 117
    //   78: aload 6
    //   80: getfield 93	android/media/MediaFile$MediaFileType:fileType	I
    //   83: invokestatic 97	android/media/MediaFile:isRawImageFileType	(I)Z
    //   86: ifeq +6 -> 92
    //   89: goto +28 -> 117
    //   92: aload 5
    //   94: astore 7
    //   96: aload 6
    //   98: getfield 93	android/media/MediaFile$MediaFileType:fileType	I
    //   101: bipush 37
    //   103: if_icmpne +29 -> 132
    //   106: aload_0
    //   107: iload_3
    //   108: iload_2
    //   109: invokestatic 101	android/media/ThumbnailUtils:createThumbnailFromMetadataRetriever	(Ljava/lang/String;II)Landroid/graphics/Bitmap;
    //   112: astore 7
    //   114: goto +18 -> 132
    //   117: aload_0
    //   118: iload_3
    //   119: iload_2
    //   120: aload 4
    //   122: invokestatic 105	android/media/ThumbnailUtils:createThumbnailFromEXIF	(Ljava/lang/String;IILandroid/media/ThumbnailUtils$SizedThumbnailBitmap;)V
    //   125: aload 4
    //   127: getfield 109	android/media/ThumbnailUtils$SizedThumbnailBitmap:mBitmap	Landroid/graphics/Bitmap;
    //   130: astore 7
    //   132: aload 7
    //   134: astore 5
    //   136: aload 7
    //   138: ifnonnull +547 -> 685
    //   141: aconst_null
    //   142: astore 8
    //   144: aconst_null
    //   145: astore 9
    //   147: aconst_null
    //   148: astore 10
    //   150: aload 10
    //   152: astore 5
    //   154: aload 8
    //   156: astore 4
    //   158: aload 9
    //   160: astore 11
    //   162: new 111	java/io/FileInputStream
    //   165: astore 6
    //   167: aload 10
    //   169: astore 5
    //   171: aload 8
    //   173: astore 4
    //   175: aload 9
    //   177: astore 11
    //   179: aload 6
    //   181: aload_0
    //   182: invokespecial 114	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
    //   185: aload 6
    //   187: astore 5
    //   189: aload 6
    //   191: astore 4
    //   193: aload 6
    //   195: astore 11
    //   197: aload 6
    //   199: invokevirtual 118	java/io/FileInputStream:getFD	()Ljava/io/FileDescriptor;
    //   202: astore 10
    //   204: aload 6
    //   206: astore 5
    //   208: aload 6
    //   210: astore 4
    //   212: aload 6
    //   214: astore 11
    //   216: new 48	android/graphics/BitmapFactory$Options
    //   219: astore 8
    //   221: aload 6
    //   223: astore 5
    //   225: aload 6
    //   227: astore 4
    //   229: aload 6
    //   231: astore 11
    //   233: aload 8
    //   235: invokespecial 119	android/graphics/BitmapFactory$Options:<init>	()V
    //   238: aload 6
    //   240: astore 5
    //   242: aload 6
    //   244: astore 4
    //   246: aload 6
    //   248: astore 11
    //   250: aload 8
    //   252: iconst_1
    //   253: putfield 122	android/graphics/BitmapFactory$Options:inSampleSize	I
    //   256: aload 6
    //   258: astore 5
    //   260: aload 6
    //   262: astore 4
    //   264: aload 6
    //   266: astore 11
    //   268: aload 8
    //   270: iconst_1
    //   271: putfield 126	android/graphics/BitmapFactory$Options:inJustDecodeBounds	Z
    //   274: aload 6
    //   276: astore 5
    //   278: aload 6
    //   280: astore 4
    //   282: aload 6
    //   284: astore 11
    //   286: aload 10
    //   288: aconst_null
    //   289: aload 8
    //   291: invokestatic 132	android/graphics/BitmapFactory:decodeFileDescriptor	(Ljava/io/FileDescriptor;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   294: pop
    //   295: aload 6
    //   297: astore 5
    //   299: aload 6
    //   301: astore 4
    //   303: aload 6
    //   305: astore 11
    //   307: aload 8
    //   309: getfield 135	android/graphics/BitmapFactory$Options:mCancel	Z
    //   312: ifne +183 -> 495
    //   315: aload 6
    //   317: astore 5
    //   319: aload 6
    //   321: astore 4
    //   323: aload 6
    //   325: astore 11
    //   327: aload 8
    //   329: getfield 51	android/graphics/BitmapFactory$Options:outWidth	I
    //   332: iconst_m1
    //   333: if_icmpeq +162 -> 495
    //   336: aload 6
    //   338: astore 5
    //   340: aload 6
    //   342: astore 4
    //   344: aload 6
    //   346: astore 11
    //   348: aload 8
    //   350: getfield 54	android/graphics/BitmapFactory$Options:outHeight	I
    //   353: iconst_m1
    //   354: if_icmpne +6 -> 360
    //   357: goto +138 -> 495
    //   360: aload 6
    //   362: astore 5
    //   364: aload 6
    //   366: astore 4
    //   368: aload 6
    //   370: astore 11
    //   372: aload 8
    //   374: aload 8
    //   376: iload_3
    //   377: iload_2
    //   378: invokestatic 137	android/media/ThumbnailUtils:computeSampleSize	(Landroid/graphics/BitmapFactory$Options;II)I
    //   381: putfield 122	android/graphics/BitmapFactory$Options:inSampleSize	I
    //   384: aload 6
    //   386: astore 5
    //   388: aload 6
    //   390: astore 4
    //   392: aload 6
    //   394: astore 11
    //   396: aload 8
    //   398: iconst_0
    //   399: putfield 126	android/graphics/BitmapFactory$Options:inJustDecodeBounds	Z
    //   402: aload 6
    //   404: astore 5
    //   406: aload 6
    //   408: astore 4
    //   410: aload 6
    //   412: astore 11
    //   414: aload 8
    //   416: iconst_0
    //   417: putfield 140	android/graphics/BitmapFactory$Options:inDither	Z
    //   420: aload 6
    //   422: astore 5
    //   424: aload 6
    //   426: astore 4
    //   428: aload 6
    //   430: astore 11
    //   432: aload 8
    //   434: getstatic 146	android/graphics/Bitmap$Config:ARGB_8888	Landroid/graphics/Bitmap$Config;
    //   437: putfield 149	android/graphics/BitmapFactory$Options:inPreferredConfig	Landroid/graphics/Bitmap$Config;
    //   440: aload 6
    //   442: astore 5
    //   444: aload 6
    //   446: astore 4
    //   448: aload 6
    //   450: astore 11
    //   452: aload 10
    //   454: aconst_null
    //   455: aload 8
    //   457: invokestatic 132	android/graphics/BitmapFactory:decodeFileDescriptor	(Ljava/io/FileDescriptor;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   460: astore 10
    //   462: aload 10
    //   464: astore_0
    //   465: aload_0
    //   466: astore 5
    //   468: aload 6
    //   470: invokevirtual 150	java/io/FileInputStream:close	()V
    //   473: aload_0
    //   474: astore 5
    //   476: goto +16 -> 492
    //   479: astore_0
    //   480: ldc 24
    //   482: ldc -104
    //   484: aload_0
    //   485: invokestatic 158	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   488: pop
    //   489: goto +196 -> 685
    //   492: goto +193 -> 685
    //   495: aload 6
    //   497: invokevirtual 150	java/io/FileInputStream:close	()V
    //   500: goto +16 -> 516
    //   503: astore_0
    //   504: ldc 24
    //   506: ldc -104
    //   508: aload_0
    //   509: invokestatic 158	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   512: pop
    //   513: goto +3 -> 516
    //   516: aconst_null
    //   517: areturn
    //   518: astore_0
    //   519: goto +139 -> 658
    //   522: astore 6
    //   524: aload 4
    //   526: astore 5
    //   528: new 160	java/lang/StringBuilder
    //   531: astore 11
    //   533: aload 4
    //   535: astore 5
    //   537: aload 11
    //   539: invokespecial 161	java/lang/StringBuilder:<init>	()V
    //   542: aload 4
    //   544: astore 5
    //   546: aload 11
    //   548: ldc -93
    //   550: invokevirtual 167	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   553: pop
    //   554: aload 4
    //   556: astore 5
    //   558: aload 11
    //   560: aload_0
    //   561: invokevirtual 167	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   564: pop
    //   565: aload 4
    //   567: astore 5
    //   569: aload 11
    //   571: ldc -87
    //   573: invokevirtual 167	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   576: pop
    //   577: aload 4
    //   579: astore 5
    //   581: ldc 24
    //   583: aload 11
    //   585: invokevirtual 173	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   588: aload 6
    //   590: invokestatic 158	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   593: pop
    //   594: aload 7
    //   596: astore 5
    //   598: aload 4
    //   600: ifnull -108 -> 492
    //   603: aload 7
    //   605: astore 5
    //   607: aload 4
    //   609: invokevirtual 150	java/io/FileInputStream:close	()V
    //   612: aload 7
    //   614: astore 5
    //   616: goto -124 -> 492
    //   619: astore_0
    //   620: aload 11
    //   622: astore 5
    //   624: ldc 24
    //   626: ldc -104
    //   628: aload_0
    //   629: invokestatic 158	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   632: pop
    //   633: aload 7
    //   635: astore 5
    //   637: aload 11
    //   639: ifnull -147 -> 492
    //   642: aload 7
    //   644: astore 5
    //   646: aload 11
    //   648: invokevirtual 150	java/io/FileInputStream:close	()V
    //   651: aload 7
    //   653: astore 5
    //   655: goto -163 -> 492
    //   658: aload 5
    //   660: ifnull +23 -> 683
    //   663: aload 5
    //   665: invokevirtual 150	java/io/FileInputStream:close	()V
    //   668: goto +15 -> 683
    //   671: astore 7
    //   673: ldc 24
    //   675: ldc -104
    //   677: aload 7
    //   679: invokestatic 158	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   682: pop
    //   683: aload_0
    //   684: athrow
    //   685: aload 5
    //   687: astore_0
    //   688: iload_1
    //   689: iconst_3
    //   690: if_icmpne +14 -> 704
    //   693: aload 5
    //   695: bipush 96
    //   697: bipush 96
    //   699: iconst_2
    //   700: invokestatic 177	android/media/ThumbnailUtils:extractThumbnail	(Landroid/graphics/Bitmap;III)Landroid/graphics/Bitmap;
    //   703: astore_0
    //   704: aload_0
    //   705: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	706	0	paramString	String
    //   0	706	1	paramInt	int
    //   6	372	2	i	int
    //   19	358	3	j	int
    //   48	560	4	localObject1	Object
    //   51	643	5	localObject2	Object
    //   57	439	6	localObject3	Object
    //   522	67	6	localOutOfMemoryError	OutOfMemoryError
    //   61	591	7	localObject4	Object
    //   671	7	7	localIOException	IOException
    //   142	314	8	localOptions	BitmapFactory.Options
    //   145	31	9	localObject5	Object
    //   148	315	10	localObject6	Object
    //   160	487	11	localObject7	Object
    // Exception table:
    //   from	to	target	type
    //   468	473	479	java/io/IOException
    //   607	612	479	java/io/IOException
    //   646	651	479	java/io/IOException
    //   495	500	503	java/io/IOException
    //   162	167	518	finally
    //   179	185	518	finally
    //   197	204	518	finally
    //   216	221	518	finally
    //   233	238	518	finally
    //   250	256	518	finally
    //   268	274	518	finally
    //   286	295	518	finally
    //   307	315	518	finally
    //   327	336	518	finally
    //   348	357	518	finally
    //   372	384	518	finally
    //   396	402	518	finally
    //   414	420	518	finally
    //   432	440	518	finally
    //   452	462	518	finally
    //   528	533	518	finally
    //   537	542	518	finally
    //   546	554	518	finally
    //   558	565	518	finally
    //   569	577	518	finally
    //   581	594	518	finally
    //   624	633	518	finally
    //   162	167	522	java/lang/OutOfMemoryError
    //   179	185	522	java/lang/OutOfMemoryError
    //   197	204	522	java/lang/OutOfMemoryError
    //   216	221	522	java/lang/OutOfMemoryError
    //   233	238	522	java/lang/OutOfMemoryError
    //   250	256	522	java/lang/OutOfMemoryError
    //   268	274	522	java/lang/OutOfMemoryError
    //   286	295	522	java/lang/OutOfMemoryError
    //   307	315	522	java/lang/OutOfMemoryError
    //   327	336	522	java/lang/OutOfMemoryError
    //   348	357	522	java/lang/OutOfMemoryError
    //   372	384	522	java/lang/OutOfMemoryError
    //   396	402	522	java/lang/OutOfMemoryError
    //   414	420	522	java/lang/OutOfMemoryError
    //   432	440	522	java/lang/OutOfMemoryError
    //   452	462	522	java/lang/OutOfMemoryError
    //   162	167	619	java/io/IOException
    //   179	185	619	java/io/IOException
    //   197	204	619	java/io/IOException
    //   216	221	619	java/io/IOException
    //   233	238	619	java/io/IOException
    //   250	256	619	java/io/IOException
    //   268	274	619	java/io/IOException
    //   286	295	619	java/io/IOException
    //   307	315	619	java/io/IOException
    //   327	336	619	java/io/IOException
    //   348	357	619	java/io/IOException
    //   372	384	619	java/io/IOException
    //   396	402	619	java/io/IOException
    //   414	420	619	java/io/IOException
    //   432	440	619	java/io/IOException
    //   452	462	619	java/io/IOException
    //   663	668	671	java/io/IOException
  }
  
  private static void createThumbnailFromEXIF(String paramString, int paramInt1, int paramInt2, SizedThumbnailBitmap paramSizedThumbnailBitmap)
  {
    if (paramString == null) {
      return;
    }
    Object localObject1 = null;
    try
    {
      Object localObject2 = new android/media/ExifInterface;
      ((ExifInterface)localObject2).<init>(paramString);
      localObject2 = ((ExifInterface)localObject2).getThumbnail();
      localObject1 = localObject2;
    }
    catch (IOException localIOException)
    {
      Log.w("ThumbnailUtils", localIOException);
    }
    BitmapFactory.Options localOptions1 = new BitmapFactory.Options();
    BitmapFactory.Options localOptions2 = new BitmapFactory.Options();
    int i = 0;
    if (localObject1 != null)
    {
      inJustDecodeBounds = true;
      BitmapFactory.decodeByteArray(localObject1, 0, localObject1.length, localOptions2);
      inSampleSize = computeSampleSize(localOptions2, paramInt1, paramInt2);
      i = outWidth / inSampleSize;
    }
    inJustDecodeBounds = true;
    BitmapFactory.decodeFile(paramString, localOptions1);
    inSampleSize = computeSampleSize(localOptions1, paramInt1, paramInt2);
    paramInt1 = outWidth / inSampleSize;
    if ((localObject1 != null) && (i >= paramInt1))
    {
      paramInt1 = outWidth;
      paramInt2 = outHeight;
      inJustDecodeBounds = false;
      mBitmap = BitmapFactory.decodeByteArray(localObject1, 0, localObject1.length, localOptions2);
      if (mBitmap != null)
      {
        mThumbnailData = localObject1;
        mThumbnailWidth = paramInt1;
        mThumbnailHeight = paramInt2;
      }
    }
    else
    {
      inJustDecodeBounds = false;
      mBitmap = BitmapFactory.decodeFile(paramString, localOptions1);
    }
  }
  
  /* Error */
  private static Bitmap createThumbnailFromMetadataRetriever(String paramString, int paramInt1, int paramInt2)
  {
    // Byte code:
    //   0: aload_0
    //   1: ifnonnull +5 -> 6
    //   4: aconst_null
    //   5: areturn
    //   6: aconst_null
    //   7: astore_3
    //   8: new 210	android/media/MediaMetadataRetriever
    //   11: dup
    //   12: invokespecial 211	android/media/MediaMetadataRetriever:<init>	()V
    //   15: astore 4
    //   17: aload 4
    //   19: aload_0
    //   20: invokevirtual 214	android/media/MediaMetadataRetriever:setDataSource	(Ljava/lang/String;)V
    //   23: new 216	android/media/MediaMetadataRetriever$BitmapParams
    //   26: astore_0
    //   27: aload_0
    //   28: invokespecial 217	android/media/MediaMetadataRetriever$BitmapParams:<init>	()V
    //   31: aload_0
    //   32: getstatic 146	android/graphics/Bitmap$Config:ARGB_8888	Landroid/graphics/Bitmap$Config;
    //   35: invokevirtual 221	android/media/MediaMetadataRetriever$BitmapParams:setPreferredConfig	(Landroid/graphics/Bitmap$Config;)V
    //   38: aload 4
    //   40: iconst_m1
    //   41: aload_0
    //   42: iload_1
    //   43: iload_2
    //   44: invokevirtual 225	android/media/MediaMetadataRetriever:getThumbnailImageAtIndex	(ILandroid/media/MediaMetadataRetriever$BitmapParams;II)Landroid/graphics/Bitmap;
    //   47: astore_0
    //   48: aload 4
    //   50: invokevirtual 228	android/media/MediaMetadataRetriever:release	()V
    //   53: goto +17 -> 70
    //   56: astore_0
    //   57: aload 4
    //   59: invokevirtual 228	android/media/MediaMetadataRetriever:release	()V
    //   62: aload_0
    //   63: athrow
    //   64: astore_0
    //   65: aload_3
    //   66: astore_0
    //   67: goto -19 -> 48
    //   70: aload_0
    //   71: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	72	0	paramString	String
    //   0	72	1	paramInt1	int
    //   0	72	2	paramInt2	int
    //   7	59	3	localObject	Object
    //   15	43	4	localMediaMetadataRetriever	MediaMetadataRetriever
    // Exception table:
    //   from	to	target	type
    //   17	48	56	finally
    //   17	48	64	java/lang/RuntimeException
  }
  
  /* Error */
  public static Bitmap createVideoThumbnail(String paramString, int paramInt)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: new 210	android/media/MediaMetadataRetriever
    //   7: dup
    //   8: invokespecial 211	android/media/MediaMetadataRetriever:<init>	()V
    //   11: astore 4
    //   13: aload 4
    //   15: aload_0
    //   16: invokevirtual 214	android/media/MediaMetadataRetriever:setDataSource	(Ljava/lang/String;)V
    //   19: aload 4
    //   21: ldc2_w 232
    //   24: invokevirtual 237	android/media/MediaMetadataRetriever:getFrameAtTime	(J)Landroid/graphics/Bitmap;
    //   27: astore_0
    //   28: aload_0
    //   29: astore_3
    //   30: aload_3
    //   31: astore_0
    //   32: aload 4
    //   34: invokevirtual 228	android/media/MediaMetadataRetriever:release	()V
    //   37: aload_3
    //   38: astore_0
    //   39: goto +45 -> 84
    //   42: astore_3
    //   43: goto +41 -> 84
    //   46: astore_0
    //   47: aload 4
    //   49: invokevirtual 228	android/media/MediaMetadataRetriever:release	()V
    //   52: goto +4 -> 56
    //   55: astore_3
    //   56: aload_0
    //   57: athrow
    //   58: astore_0
    //   59: aload_2
    //   60: astore_0
    //   61: aload 4
    //   63: invokevirtual 228	android/media/MediaMetadataRetriever:release	()V
    //   66: aload_3
    //   67: astore_0
    //   68: goto -29 -> 39
    //   71: astore_0
    //   72: aload_2
    //   73: astore_0
    //   74: aload 4
    //   76: invokevirtual 228	android/media/MediaMetadataRetriever:release	()V
    //   79: aload_3
    //   80: astore_0
    //   81: goto -42 -> 39
    //   84: aload_0
    //   85: ifnonnull +5 -> 90
    //   88: aconst_null
    //   89: areturn
    //   90: iload_1
    //   91: iconst_1
    //   92: if_icmpne +66 -> 158
    //   95: aload_0
    //   96: invokevirtual 243	android/graphics/Bitmap:getWidth	()I
    //   99: istore 5
    //   101: aload_0
    //   102: invokevirtual 246	android/graphics/Bitmap:getHeight	()I
    //   105: istore 6
    //   107: iload 5
    //   109: iload 6
    //   111: invokestatic 250	java/lang/Math:max	(II)I
    //   114: istore_1
    //   115: aload_0
    //   116: astore_3
    //   117: iload_1
    //   118: sipush 512
    //   121: if_icmple +34 -> 155
    //   124: ldc -5
    //   126: iload_1
    //   127: i2f
    //   128: fdiv
    //   129: fstore 7
    //   131: aload_0
    //   132: iload 5
    //   134: i2f
    //   135: fload 7
    //   137: fmul
    //   138: invokestatic 255	java/lang/Math:round	(F)I
    //   141: iload 6
    //   143: i2f
    //   144: fload 7
    //   146: fmul
    //   147: invokestatic 255	java/lang/Math:round	(F)I
    //   150: iconst_1
    //   151: invokestatic 259	android/graphics/Bitmap:createScaledBitmap	(Landroid/graphics/Bitmap;IIZ)Landroid/graphics/Bitmap;
    //   154: astore_3
    //   155: goto +20 -> 175
    //   158: aload_0
    //   159: astore_3
    //   160: iload_1
    //   161: iconst_3
    //   162: if_icmpne +13 -> 175
    //   165: aload_0
    //   166: bipush 96
    //   168: bipush 96
    //   170: iconst_2
    //   171: invokestatic 177	android/media/ThumbnailUtils:extractThumbnail	(Landroid/graphics/Bitmap;III)Landroid/graphics/Bitmap;
    //   174: astore_3
    //   175: aload_3
    //   176: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	177	0	paramString	String
    //   0	177	1	paramInt	int
    //   1	72	2	localObject1	Object
    //   3	35	3	str	String
    //   42	1	3	localRuntimeException1	RuntimeException
    //   55	25	3	localRuntimeException2	RuntimeException
    //   116	60	3	localObject2	Object
    //   11	64	4	localMediaMetadataRetriever	MediaMetadataRetriever
    //   99	34	5	i	int
    //   105	37	6	j	int
    //   129	16	7	f	float
    // Exception table:
    //   from	to	target	type
    //   32	37	42	java/lang/RuntimeException
    //   61	66	42	java/lang/RuntimeException
    //   74	79	42	java/lang/RuntimeException
    //   13	28	46	finally
    //   47	52	55	java/lang/RuntimeException
    //   13	28	58	java/lang/RuntimeException
    //   13	28	71	java/lang/IllegalArgumentException
  }
  
  public static Bitmap extractThumbnail(Bitmap paramBitmap, int paramInt1, int paramInt2)
  {
    return extractThumbnail(paramBitmap, paramInt1, paramInt2, 0);
  }
  
  public static Bitmap extractThumbnail(Bitmap paramBitmap, int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramBitmap == null) {
      return null;
    }
    float f;
    if (paramBitmap.getWidth() < paramBitmap.getHeight()) {
      f = paramInt1 / paramBitmap.getWidth();
    } else {
      f = paramInt2 / paramBitmap.getHeight();
    }
    Matrix localMatrix = new Matrix();
    localMatrix.setScale(f, f);
    return transform(localMatrix, paramBitmap, paramInt1, paramInt2, 0x1 | paramInt3);
  }
  
  /* Error */
  private static Bitmap makeBitmap(int paramInt1, int paramInt2, Uri paramUri, ContentResolver paramContentResolver, ParcelFileDescriptor paramParcelFileDescriptor, BitmapFactory.Options paramOptions)
  {
    // Byte code:
    //   0: aload 4
    //   2: astore 6
    //   4: aload 4
    //   6: ifnonnull +25 -> 31
    //   9: aload 4
    //   11: astore 7
    //   13: aload_2
    //   14: aload_3
    //   15: invokestatic 277	android/media/ThumbnailUtils:makeInputStream	(Landroid/net/Uri;Landroid/content/ContentResolver;)Landroid/os/ParcelFileDescriptor;
    //   18: astore 6
    //   20: goto +11 -> 31
    //   23: astore_2
    //   24: goto +265 -> 289
    //   27: astore_2
    //   28: goto +240 -> 268
    //   31: aload 6
    //   33: ifnonnull +10 -> 43
    //   36: aload 6
    //   38: invokestatic 279	android/media/ThumbnailUtils:closeSilently	(Landroid/os/ParcelFileDescriptor;)V
    //   41: aconst_null
    //   42: areturn
    //   43: aload 5
    //   45: astore_2
    //   46: aload 5
    //   48: ifnonnull +27 -> 75
    //   51: aload 6
    //   53: astore 7
    //   55: aload 6
    //   57: astore 4
    //   59: new 48	android/graphics/BitmapFactory$Options
    //   62: astore_2
    //   63: aload 6
    //   65: astore 7
    //   67: aload 6
    //   69: astore 4
    //   71: aload_2
    //   72: invokespecial 119	android/graphics/BitmapFactory$Options:<init>	()V
    //   75: aload 6
    //   77: astore 7
    //   79: aload 6
    //   81: astore 4
    //   83: aload 6
    //   85: invokevirtual 282	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   88: astore_3
    //   89: aload 6
    //   91: astore 7
    //   93: aload 6
    //   95: astore 4
    //   97: aload_2
    //   98: iconst_1
    //   99: putfield 122	android/graphics/BitmapFactory$Options:inSampleSize	I
    //   102: aload 6
    //   104: astore 7
    //   106: aload 6
    //   108: astore 4
    //   110: aload_2
    //   111: iconst_1
    //   112: putfield 126	android/graphics/BitmapFactory$Options:inJustDecodeBounds	Z
    //   115: aload 6
    //   117: astore 7
    //   119: aload 6
    //   121: astore 4
    //   123: aload_3
    //   124: aconst_null
    //   125: aload_2
    //   126: invokestatic 132	android/graphics/BitmapFactory:decodeFileDescriptor	(Ljava/io/FileDescriptor;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   129: pop
    //   130: aload 6
    //   132: astore 7
    //   134: aload 6
    //   136: astore 4
    //   138: aload_2
    //   139: getfield 135	android/graphics/BitmapFactory$Options:mCancel	Z
    //   142: ifne +119 -> 261
    //   145: aload 6
    //   147: astore 7
    //   149: aload 6
    //   151: astore 4
    //   153: aload_2
    //   154: getfield 51	android/graphics/BitmapFactory$Options:outWidth	I
    //   157: iconst_m1
    //   158: if_icmpeq +103 -> 261
    //   161: aload 6
    //   163: astore 7
    //   165: aload 6
    //   167: astore 4
    //   169: aload_2
    //   170: getfield 54	android/graphics/BitmapFactory$Options:outHeight	I
    //   173: iconst_m1
    //   174: if_icmpne +6 -> 180
    //   177: goto +84 -> 261
    //   180: aload 6
    //   182: astore 7
    //   184: aload 6
    //   186: astore 4
    //   188: aload_2
    //   189: aload_2
    //   190: iload_0
    //   191: iload_1
    //   192: invokestatic 137	android/media/ThumbnailUtils:computeSampleSize	(Landroid/graphics/BitmapFactory$Options;II)I
    //   195: putfield 122	android/graphics/BitmapFactory$Options:inSampleSize	I
    //   198: aload 6
    //   200: astore 7
    //   202: aload 6
    //   204: astore 4
    //   206: aload_2
    //   207: iconst_0
    //   208: putfield 126	android/graphics/BitmapFactory$Options:inJustDecodeBounds	Z
    //   211: aload 6
    //   213: astore 7
    //   215: aload 6
    //   217: astore 4
    //   219: aload_2
    //   220: iconst_0
    //   221: putfield 140	android/graphics/BitmapFactory$Options:inDither	Z
    //   224: aload 6
    //   226: astore 7
    //   228: aload 6
    //   230: astore 4
    //   232: aload_2
    //   233: getstatic 146	android/graphics/Bitmap$Config:ARGB_8888	Landroid/graphics/Bitmap$Config;
    //   236: putfield 149	android/graphics/BitmapFactory$Options:inPreferredConfig	Landroid/graphics/Bitmap$Config;
    //   239: aload 6
    //   241: astore 7
    //   243: aload 6
    //   245: astore 4
    //   247: aload_3
    //   248: aconst_null
    //   249: aload_2
    //   250: invokestatic 132	android/graphics/BitmapFactory:decodeFileDescriptor	(Ljava/io/FileDescriptor;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   253: astore_2
    //   254: aload 6
    //   256: invokestatic 279	android/media/ThumbnailUtils:closeSilently	(Landroid/os/ParcelFileDescriptor;)V
    //   259: aload_2
    //   260: areturn
    //   261: aload 6
    //   263: invokestatic 279	android/media/ThumbnailUtils:closeSilently	(Landroid/os/ParcelFileDescriptor;)V
    //   266: aconst_null
    //   267: areturn
    //   268: aload 4
    //   270: astore 7
    //   272: ldc 24
    //   274: ldc_w 284
    //   277: aload_2
    //   278: invokestatic 158	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   281: pop
    //   282: aload 4
    //   284: invokestatic 279	android/media/ThumbnailUtils:closeSilently	(Landroid/os/ParcelFileDescriptor;)V
    //   287: aconst_null
    //   288: areturn
    //   289: aload 7
    //   291: invokestatic 279	android/media/ThumbnailUtils:closeSilently	(Landroid/os/ParcelFileDescriptor;)V
    //   294: aload_2
    //   295: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	296	0	paramInt1	int
    //   0	296	1	paramInt2	int
    //   0	296	2	paramUri	Uri
    //   0	296	3	paramContentResolver	ContentResolver
    //   0	296	4	paramParcelFileDescriptor	ParcelFileDescriptor
    //   0	296	5	paramOptions	BitmapFactory.Options
    //   2	260	6	localParcelFileDescriptor1	ParcelFileDescriptor
    //   11	279	7	localParcelFileDescriptor2	ParcelFileDescriptor
    // Exception table:
    //   from	to	target	type
    //   13	20	23	finally
    //   59	63	23	finally
    //   71	75	23	finally
    //   83	89	23	finally
    //   97	102	23	finally
    //   110	115	23	finally
    //   123	130	23	finally
    //   138	145	23	finally
    //   153	161	23	finally
    //   169	177	23	finally
    //   188	198	23	finally
    //   206	211	23	finally
    //   219	224	23	finally
    //   232	239	23	finally
    //   247	254	23	finally
    //   272	282	23	finally
    //   13	20	27	java/lang/OutOfMemoryError
    //   59	63	27	java/lang/OutOfMemoryError
    //   71	75	27	java/lang/OutOfMemoryError
    //   83	89	27	java/lang/OutOfMemoryError
    //   97	102	27	java/lang/OutOfMemoryError
    //   110	115	27	java/lang/OutOfMemoryError
    //   123	130	27	java/lang/OutOfMemoryError
    //   138	145	27	java/lang/OutOfMemoryError
    //   153	161	27	java/lang/OutOfMemoryError
    //   169	177	27	java/lang/OutOfMemoryError
    //   188	198	27	java/lang/OutOfMemoryError
    //   206	211	27	java/lang/OutOfMemoryError
    //   219	224	27	java/lang/OutOfMemoryError
    //   232	239	27	java/lang/OutOfMemoryError
    //   247	254	27	java/lang/OutOfMemoryError
  }
  
  private static ParcelFileDescriptor makeInputStream(Uri paramUri, ContentResolver paramContentResolver)
  {
    try
    {
      paramUri = paramContentResolver.openFileDescriptor(paramUri, "r");
      return paramUri;
    }
    catch (IOException paramUri) {}
    return null;
  }
  
  private static Bitmap transform(Matrix paramMatrix, Bitmap paramBitmap, int paramInt1, int paramInt2, int paramInt3)
  {
    int i = 1;
    if ((paramInt3 & 0x1) != 0) {
      j = 1;
    } else {
      j = 0;
    }
    if ((paramInt3 & 0x2) != 0) {
      paramInt3 = i;
    } else {
      paramInt3 = 0;
    }
    int k = paramBitmap.getWidth() - paramInt1;
    i = paramBitmap.getHeight() - paramInt2;
    if ((j == 0) && ((k < 0) || (i < 0)))
    {
      Bitmap localBitmap = Bitmap.createBitmap(paramInt1, paramInt2, Bitmap.Config.ARGB_8888);
      paramMatrix = new Canvas(localBitmap);
      j = Math.max(0, k / 2);
      i = Math.max(0, i / 2);
      localObject = new Rect(j, i, Math.min(paramInt1, paramBitmap.getWidth()) + j, Math.min(paramInt2, paramBitmap.getHeight()) + i);
      i = (paramInt1 - ((Rect)localObject).width()) / 2;
      j = (paramInt2 - ((Rect)localObject).height()) / 2;
      paramMatrix.drawBitmap(paramBitmap, (Rect)localObject, new Rect(i, j, paramInt1 - i, paramInt2 - j), null);
      if (paramInt3 != 0) {
        paramBitmap.recycle();
      }
      paramMatrix.setBitmap(null);
      return localBitmap;
    }
    float f1 = paramBitmap.getWidth();
    float f2 = paramBitmap.getHeight();
    if (f1 / f2 > paramInt1 / paramInt2)
    {
      f1 = paramInt2 / f2;
      if ((f1 >= 0.9F) && (f1 <= 1.0F)) {
        paramMatrix = null;
      } else {
        paramMatrix.setScale(f1, f1);
      }
    }
    else
    {
      f1 = paramInt1 / f1;
      if ((f1 >= 0.9F) && (f1 <= 1.0F)) {
        paramMatrix = null;
      } else {
        paramMatrix.setScale(f1, f1);
      }
    }
    if (paramMatrix != null) {
      paramMatrix = Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), paramMatrix, true);
    } else {
      paramMatrix = paramBitmap;
    }
    if ((paramInt3 != 0) && (paramMatrix != paramBitmap)) {
      paramBitmap.recycle();
    }
    i = Math.max(0, paramMatrix.getWidth() - paramInt1);
    int j = Math.max(0, paramMatrix.getHeight() - paramInt2);
    Object localObject = Bitmap.createBitmap(paramMatrix, i / 2, j / 2, paramInt1, paramInt2);
    if ((localObject != paramMatrix) && ((paramInt3 != 0) || (paramMatrix != paramBitmap))) {
      paramMatrix.recycle();
    }
    return localObject;
  }
  
  private static class SizedThumbnailBitmap
  {
    public Bitmap mBitmap;
    public byte[] mThumbnailData;
    public int mThumbnailHeight;
    public int mThumbnailWidth;
    
    private SizedThumbnailBitmap() {}
  }
}
