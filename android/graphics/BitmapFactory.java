package android.graphics;

import android.content.res.AssetManager.AssetInputStream;
import android.content.res.Resources;
import android.os.Trace;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import java.io.FileDescriptor;
import java.io.InputStream;

public class BitmapFactory
{
  private static final int DECODE_BUFFER_SIZE = 16384;
  
  public BitmapFactory() {}
  
  public static Bitmap decodeByteArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return decodeByteArray(paramArrayOfByte, paramInt1, paramInt2, null);
  }
  
  public static Bitmap decodeByteArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2, Options paramOptions)
  {
    if (((paramInt1 | paramInt2) >= 0) && (paramArrayOfByte.length >= paramInt1 + paramInt2))
    {
      Options.validate(paramOptions);
      Trace.traceBegin(2L, "decodeBitmap");
      try
      {
        paramArrayOfByte = nativeDecodeByteArray(paramArrayOfByte, paramInt1, paramInt2, paramOptions);
        if ((paramArrayOfByte == null) && (paramOptions != null) && (inBitmap != null))
        {
          paramArrayOfByte = new java/lang/IllegalArgumentException;
          paramArrayOfByte.<init>("Problem decoding into existing bitmap");
          throw paramArrayOfByte;
        }
        setDensityFromOptions(paramArrayOfByte, paramOptions);
        return paramArrayOfByte;
      }
      finally
      {
        Trace.traceEnd(2L);
      }
    }
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public static Bitmap decodeFile(String paramString)
  {
    return decodeFile(paramString, null);
  }
  
  /* Error */
  public static Bitmap decodeFile(String paramString, Options paramOptions)
  {
    // Byte code:
    //   0: aload_1
    //   1: invokestatic 24	android/graphics/BitmapFactory$Options:validate	(Landroid/graphics/BitmapFactory$Options;)V
    //   4: aconst_null
    //   5: astore_2
    //   6: aconst_null
    //   7: astore_3
    //   8: aconst_null
    //   9: astore 4
    //   11: aconst_null
    //   12: astore 5
    //   14: aconst_null
    //   15: astore 6
    //   17: aload 6
    //   19: astore 7
    //   21: aload 5
    //   23: astore 8
    //   25: new 70	java/io/FileInputStream
    //   28: astore 9
    //   30: aload 6
    //   32: astore 7
    //   34: aload 5
    //   36: astore 8
    //   38: aload 9
    //   40: aload_0
    //   41: invokespecial 71	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
    //   44: aload 9
    //   46: astore_0
    //   47: aload_0
    //   48: astore 7
    //   50: aload_0
    //   51: astore 8
    //   53: aload_0
    //   54: aconst_null
    //   55: aload_1
    //   56: invokestatic 75	android/graphics/BitmapFactory:decodeStream	(Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   59: astore_1
    //   60: aload_1
    //   61: astore 8
    //   63: aload 8
    //   65: astore_1
    //   66: aload_0
    //   67: invokevirtual 80	java/io/InputStream:close	()V
    //   70: aload 8
    //   72: astore_0
    //   73: goto +85 -> 158
    //   76: astore_0
    //   77: aload_1
    //   78: astore_0
    //   79: goto -6 -> 73
    //   82: astore_0
    //   83: goto +77 -> 160
    //   86: astore_0
    //   87: aload 8
    //   89: astore 7
    //   91: new 82	java/lang/StringBuilder
    //   94: astore_1
    //   95: aload 8
    //   97: astore 7
    //   99: aload_1
    //   100: invokespecial 83	java/lang/StringBuilder:<init>	()V
    //   103: aload 8
    //   105: astore 7
    //   107: aload_1
    //   108: ldc 85
    //   110: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   113: pop
    //   114: aload 8
    //   116: astore 7
    //   118: aload_1
    //   119: aload_0
    //   120: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   123: pop
    //   124: aload 8
    //   126: astore 7
    //   128: ldc 94
    //   130: aload_1
    //   131: invokevirtual 98	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   134: invokestatic 104	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   137: pop
    //   138: aload_3
    //   139: astore_0
    //   140: aload 8
    //   142: ifnull +16 -> 158
    //   145: aload_2
    //   146: astore_1
    //   147: aload 8
    //   149: invokevirtual 80	java/io/InputStream:close	()V
    //   152: aload 4
    //   154: astore_0
    //   155: goto -82 -> 73
    //   158: aload_0
    //   159: areturn
    //   160: aload 7
    //   162: ifnull +12 -> 174
    //   165: aload 7
    //   167: invokevirtual 80	java/io/InputStream:close	()V
    //   170: goto +4 -> 174
    //   173: astore_1
    //   174: aload_0
    //   175: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	176	0	paramString	String
    //   0	176	1	paramOptions	Options
    //   5	141	2	localObject1	Object
    //   7	132	3	localObject2	Object
    //   9	144	4	localObject3	Object
    //   12	23	5	localObject4	Object
    //   15	16	6	localObject5	Object
    //   19	147	7	localObject6	Object
    //   23	125	8	localObject7	Object
    //   28	17	9	localFileInputStream	java.io.FileInputStream
    // Exception table:
    //   from	to	target	type
    //   66	70	76	java/io/IOException
    //   147	152	76	java/io/IOException
    //   25	30	82	finally
    //   38	44	82	finally
    //   53	60	82	finally
    //   91	95	82	finally
    //   99	103	82	finally
    //   107	114	82	finally
    //   118	124	82	finally
    //   128	138	82	finally
    //   25	30	86	java/lang/Exception
    //   38	44	86	java/lang/Exception
    //   53	60	86	java/lang/Exception
    //   165	170	173	java/io/IOException
  }
  
  public static Bitmap decodeFileDescriptor(FileDescriptor paramFileDescriptor)
  {
    return decodeFileDescriptor(paramFileDescriptor, null, null);
  }
  
  /* Error */
  public static Bitmap decodeFileDescriptor(FileDescriptor paramFileDescriptor, Rect paramRect, Options paramOptions)
  {
    // Byte code:
    //   0: aload_2
    //   1: invokestatic 24	android/graphics/BitmapFactory$Options:validate	(Landroid/graphics/BitmapFactory$Options;)V
    //   4: ldc2_w 25
    //   7: ldc 112
    //   9: invokestatic 34	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   12: aload_0
    //   13: invokestatic 116	android/graphics/BitmapFactory:nativeIsSeekable	(Ljava/io/FileDescriptor;)Z
    //   16: ifeq +13 -> 29
    //   19: aload_0
    //   20: aload_1
    //   21: aload_2
    //   22: invokestatic 119	android/graphics/BitmapFactory:nativeDecodeFileDescriptor	(Ljava/io/FileDescriptor;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   25: astore_0
    //   26: goto +27 -> 53
    //   29: new 70	java/io/FileInputStream
    //   32: astore_3
    //   33: aload_3
    //   34: aload_0
    //   35: invokespecial 122	java/io/FileInputStream:<init>	(Ljava/io/FileDescriptor;)V
    //   38: aload_3
    //   39: aload_1
    //   40: aload_2
    //   41: invokestatic 125	android/graphics/BitmapFactory:decodeStreamInternal	(Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   44: astore_0
    //   45: aload_3
    //   46: invokevirtual 126	java/io/FileInputStream:close	()V
    //   49: goto +4 -> 53
    //   52: astore_1
    //   53: aload_0
    //   54: ifnonnull +29 -> 83
    //   57: aload_2
    //   58: ifnull +25 -> 83
    //   61: aload_2
    //   62: getfield 41	android/graphics/BitmapFactory$Options:inBitmap	Landroid/graphics/Bitmap;
    //   65: ifnonnull +6 -> 71
    //   68: goto +15 -> 83
    //   71: new 43	java/lang/IllegalArgumentException
    //   74: astore_0
    //   75: aload_0
    //   76: ldc 45
    //   78: invokespecial 48	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   81: aload_0
    //   82: athrow
    //   83: aload_0
    //   84: aload_2
    //   85: invokestatic 52	android/graphics/BitmapFactory:setDensityFromOptions	(Landroid/graphics/Bitmap;Landroid/graphics/BitmapFactory$Options;)V
    //   88: ldc2_w 25
    //   91: invokestatic 56	android/os/Trace:traceEnd	(J)V
    //   94: aload_0
    //   95: areturn
    //   96: astore_0
    //   97: aload_3
    //   98: invokevirtual 126	java/io/FileInputStream:close	()V
    //   101: goto +4 -> 105
    //   104: astore_1
    //   105: aload_0
    //   106: athrow
    //   107: astore_0
    //   108: ldc2_w 25
    //   111: invokestatic 56	android/os/Trace:traceEnd	(J)V
    //   114: aload_0
    //   115: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	116	0	paramFileDescriptor	FileDescriptor
    //   0	116	1	paramRect	Rect
    //   0	116	2	paramOptions	Options
    //   32	66	3	localFileInputStream	java.io.FileInputStream
    // Exception table:
    //   from	to	target	type
    //   45	49	52	java/lang/Throwable
    //   38	45	96	finally
    //   97	101	104	java/lang/Throwable
    //   12	26	107	finally
    //   29	38	107	finally
    //   45	49	107	finally
    //   61	68	107	finally
    //   71	83	107	finally
    //   83	88	107	finally
    //   97	101	107	finally
    //   105	107	107	finally
  }
  
  public static Bitmap decodeResource(Resources paramResources, int paramInt)
  {
    return decodeResource(paramResources, paramInt, null);
  }
  
  /* Error */
  public static Bitmap decodeResource(Resources paramResources, int paramInt, Options paramOptions)
  {
    // Byte code:
    //   0: aload_2
    //   1: invokestatic 24	android/graphics/BitmapFactory$Options:validate	(Landroid/graphics/BitmapFactory$Options;)V
    //   4: aconst_null
    //   5: astore_3
    //   6: aconst_null
    //   7: astore 4
    //   9: aconst_null
    //   10: astore 5
    //   12: aconst_null
    //   13: astore 6
    //   15: aload 6
    //   17: astore 7
    //   19: aload 5
    //   21: astore 8
    //   23: new 133	android/util/TypedValue
    //   26: astore 9
    //   28: aload 6
    //   30: astore 7
    //   32: aload 5
    //   34: astore 8
    //   36: aload 9
    //   38: invokespecial 134	android/util/TypedValue:<init>	()V
    //   41: aload 6
    //   43: astore 7
    //   45: aload 5
    //   47: astore 8
    //   49: aload_0
    //   50: iload_1
    //   51: aload 9
    //   53: invokevirtual 140	android/content/res/Resources:openRawResource	(ILandroid/util/TypedValue;)Ljava/io/InputStream;
    //   56: astore 6
    //   58: aload 6
    //   60: astore 7
    //   62: aload 6
    //   64: astore 8
    //   66: aload_0
    //   67: aload 9
    //   69: aload 6
    //   71: aconst_null
    //   72: aload_2
    //   73: invokestatic 144	android/graphics/BitmapFactory:decodeResourceStream	(Landroid/content/res/Resources;Landroid/util/TypedValue;Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   76: astore_0
    //   77: aload_0
    //   78: astore 8
    //   80: aload 8
    //   82: astore_0
    //   83: aload 6
    //   85: ifnull +22 -> 107
    //   88: aload 8
    //   90: astore_0
    //   91: aload 6
    //   93: invokevirtual 80	java/io/InputStream:close	()V
    //   96: aload 8
    //   98: astore_0
    //   99: goto +8 -> 107
    //   102: astore 8
    //   104: goto +44 -> 148
    //   107: goto +41 -> 148
    //   110: astore_0
    //   111: aload 7
    //   113: ifnull +12 -> 125
    //   116: aload 7
    //   118: invokevirtual 80	java/io/InputStream:close	()V
    //   121: goto +4 -> 125
    //   124: astore_2
    //   125: aload_0
    //   126: athrow
    //   127: astore_0
    //   128: aload_3
    //   129: astore_0
    //   130: aload 8
    //   132: ifnull -25 -> 107
    //   135: aload 4
    //   137: astore_0
    //   138: aload 8
    //   140: invokevirtual 80	java/io/InputStream:close	()V
    //   143: aload_3
    //   144: astore_0
    //   145: goto -38 -> 107
    //   148: aload_0
    //   149: ifnonnull +27 -> 176
    //   152: aload_2
    //   153: ifnull +23 -> 176
    //   156: aload_2
    //   157: getfield 41	android/graphics/BitmapFactory$Options:inBitmap	Landroid/graphics/Bitmap;
    //   160: ifnonnull +6 -> 166
    //   163: goto +13 -> 176
    //   166: new 43	java/lang/IllegalArgumentException
    //   169: dup
    //   170: ldc 45
    //   172: invokespecial 48	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   175: athrow
    //   176: aload_0
    //   177: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	178	0	paramResources	Resources
    //   0	178	1	paramInt	int
    //   0	178	2	paramOptions	Options
    //   5	139	3	localObject1	Object
    //   7	129	4	localObject2	Object
    //   10	36	5	localObject3	Object
    //   13	79	6	localInputStream1	InputStream
    //   17	100	7	localInputStream2	InputStream
    //   21	76	8	localObject4	Object
    //   102	37	8	localIOException	java.io.IOException
    //   26	42	9	localTypedValue	TypedValue
    // Exception table:
    //   from	to	target	type
    //   91	96	102	java/io/IOException
    //   138	143	102	java/io/IOException
    //   23	28	110	finally
    //   36	41	110	finally
    //   49	58	110	finally
    //   66	77	110	finally
    //   116	121	124	java/io/IOException
    //   23	28	127	java/lang/Exception
    //   36	41	127	java/lang/Exception
    //   49	58	127	java/lang/Exception
    //   66	77	127	java/lang/Exception
  }
  
  public static Bitmap decodeResourceStream(Resources paramResources, TypedValue paramTypedValue, InputStream paramInputStream, Rect paramRect, Options paramOptions)
  {
    Options.validate(paramOptions);
    Options localOptions = paramOptions;
    if (paramOptions == null) {
      localOptions = new Options();
    }
    if ((inDensity == 0) && (paramTypedValue != null))
    {
      int i = density;
      if (i == 0) {
        inDensity = 160;
      } else if (i != 65535) {
        inDensity = i;
      }
    }
    if ((inTargetDensity == 0) && (paramResources != null)) {
      inTargetDensity = getDisplayMetricsdensityDpi;
    }
    return decodeStream(paramInputStream, paramRect, localOptions);
  }
  
  public static Bitmap decodeStream(InputStream paramInputStream)
  {
    return decodeStream(paramInputStream, null, null);
  }
  
  public static Bitmap decodeStream(InputStream paramInputStream, Rect paramRect, Options paramOptions)
  {
    if (paramInputStream == null) {
      return null;
    }
    Options.validate(paramOptions);
    Trace.traceBegin(2L, "decodeBitmap");
    try
    {
      if ((paramInputStream instanceof AssetManager.AssetInputStream)) {
        paramInputStream = nativeDecodeAsset(((AssetManager.AssetInputStream)paramInputStream).getNativeAsset(), paramRect, paramOptions);
      } else {
        paramInputStream = decodeStreamInternal(paramInputStream, paramRect, paramOptions);
      }
      if ((paramInputStream == null) && (paramOptions != null) && (inBitmap != null))
      {
        paramInputStream = new java/lang/IllegalArgumentException;
        paramInputStream.<init>("Problem decoding into existing bitmap");
        throw paramInputStream;
      }
      setDensityFromOptions(paramInputStream, paramOptions);
      return paramInputStream;
    }
    finally
    {
      Trace.traceEnd(2L);
    }
  }
  
  private static Bitmap decodeStreamInternal(InputStream paramInputStream, Rect paramRect, Options paramOptions)
  {
    byte[] arrayOfByte1 = null;
    if (paramOptions != null) {
      arrayOfByte1 = inTempStorage;
    }
    byte[] arrayOfByte2 = arrayOfByte1;
    if (arrayOfByte1 == null) {
      arrayOfByte2 = new byte['䀀'];
    }
    return nativeDecodeStream(paramInputStream, arrayOfByte2, paramRect, paramOptions);
  }
  
  private static native Bitmap nativeDecodeAsset(long paramLong, Rect paramRect, Options paramOptions);
  
  private static native Bitmap nativeDecodeByteArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2, Options paramOptions);
  
  private static native Bitmap nativeDecodeFileDescriptor(FileDescriptor paramFileDescriptor, Rect paramRect, Options paramOptions);
  
  private static native Bitmap nativeDecodeStream(InputStream paramInputStream, byte[] paramArrayOfByte, Rect paramRect, Options paramOptions);
  
  private static native boolean nativeIsSeekable(FileDescriptor paramFileDescriptor);
  
  private static void setDensityFromOptions(Bitmap paramBitmap, Options paramOptions)
  {
    if ((paramBitmap != null) && (paramOptions != null))
    {
      int i = inDensity;
      if (i != 0)
      {
        paramBitmap.setDensity(i);
        int j = inTargetDensity;
        if ((j != 0) && (i != j) && (i != inScreenDensity))
        {
          byte[] arrayOfByte = paramBitmap.getNinePatchChunk();
          if ((arrayOfByte != null) && (NinePatch.isNinePatchChunk(arrayOfByte))) {
            i = 1;
          } else {
            i = 0;
          }
          if ((inScaled) || (i != 0)) {
            paramBitmap.setDensity(j);
          }
        }
      }
      else if (inBitmap != null)
      {
        paramBitmap.setDensity(Bitmap.getDefaultDensity());
      }
      return;
    }
  }
  
  public static class Options
  {
    public Bitmap inBitmap;
    public int inDensity;
    public boolean inDither;
    @Deprecated
    public boolean inInputShareable;
    public boolean inJustDecodeBounds;
    public boolean inMutable;
    @Deprecated
    public boolean inPreferQualityOverSpeed;
    public ColorSpace inPreferredColorSpace = null;
    public Bitmap.Config inPreferredConfig = Bitmap.Config.ARGB_8888;
    public boolean inPremultiplied = true;
    @Deprecated
    public boolean inPurgeable;
    public int inSampleSize;
    public boolean inScaled = true;
    public int inScreenDensity;
    public int inTargetDensity;
    public byte[] inTempStorage;
    @Deprecated
    public boolean mCancel;
    public ColorSpace outColorSpace;
    public Bitmap.Config outConfig;
    public int outHeight;
    public String outMimeType;
    public int outWidth;
    
    public Options() {}
    
    static void validate(Options paramOptions)
    {
      if (paramOptions == null) {
        return;
      }
      if ((inBitmap != null) && (inBitmap.getConfig() == Bitmap.Config.HARDWARE)) {
        throw new IllegalArgumentException("Bitmaps with Config.HARWARE are always immutable");
      }
      if ((inMutable) && (inPreferredConfig == Bitmap.Config.HARDWARE)) {
        throw new IllegalArgumentException("Bitmaps with Config.HARDWARE cannot be decoded into - they are immutable");
      }
      if (inPreferredColorSpace != null) {
        if ((inPreferredColorSpace instanceof ColorSpace.Rgb))
        {
          if (((ColorSpace.Rgb)inPreferredColorSpace).getTransferParameters() == null) {
            throw new IllegalArgumentException("The destination color space must use an ICC parametric transfer function");
          }
        }
        else {
          throw new IllegalArgumentException("The destination color space must use the RGB color model");
        }
      }
    }
    
    @Deprecated
    public void requestCancelDecode()
    {
      mCancel = true;
    }
  }
}
