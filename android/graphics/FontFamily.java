package android.graphics;

import android.content.res.AssetManager;
import android.graphics.fonts.FontVariationAxis;
import android.text.TextUtils;
import dalvik.annotation.optimization.CriticalNative;
import java.nio.ByteBuffer;
import libcore.util.NativeAllocationRegistry;

public class FontFamily
{
  private static String TAG = "FontFamily";
  private static final NativeAllocationRegistry sBuilderRegistry = new NativeAllocationRegistry(FontFamily.class.getClassLoader(), nGetBuilderReleaseFunc(), 64L);
  private static final NativeAllocationRegistry sFamilyRegistry = new NativeAllocationRegistry(FontFamily.class.getClassLoader(), nGetFamilyReleaseFunc(), 64L);
  private long mBuilderPtr;
  private Runnable mNativeBuilderCleaner;
  public long mNativePtr;
  
  public FontFamily()
  {
    mBuilderPtr = nInitBuilder(null, 0);
    mNativeBuilderCleaner = sBuilderRegistry.registerNativeAllocation(this, mBuilderPtr);
  }
  
  public FontFamily(String[] paramArrayOfString, int paramInt)
  {
    if ((paramArrayOfString != null) && (paramArrayOfString.length != 0))
    {
      if (paramArrayOfString.length == 1) {
        paramArrayOfString = paramArrayOfString[0];
      } else {
        paramArrayOfString = TextUtils.join(",", paramArrayOfString);
      }
    }
    else {
      paramArrayOfString = null;
    }
    mBuilderPtr = nInitBuilder(paramArrayOfString, paramInt);
    mNativeBuilderCleaner = sBuilderRegistry.registerNativeAllocation(this, mBuilderPtr);
  }
  
  @CriticalNative
  private static native void nAddAxisValue(long paramLong, int paramInt, float paramFloat);
  
  private static boolean nAddFont(long paramLong, ByteBuffer paramByteBuffer, int paramInt)
  {
    return nAddFont(paramLong, paramByteBuffer, paramInt, -1, -1);
  }
  
  private static native boolean nAddFont(long paramLong, ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, int paramInt3);
  
  private static native boolean nAddFontFromAssetManager(long paramLong, AssetManager paramAssetManager, String paramString, int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, int paramInt4);
  
  private static native boolean nAddFontWeightStyle(long paramLong, ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, int paramInt3);
  
  @CriticalNative
  private static native long nCreateFamily(long paramLong);
  
  @CriticalNative
  private static native long nGetBuilderReleaseFunc();
  
  @CriticalNative
  private static native long nGetFamilyReleaseFunc();
  
  private static native long nInitBuilder(String paramString, int paramInt);
  
  public void abortCreation()
  {
    if (mBuilderPtr != 0L)
    {
      mNativeBuilderCleaner.run();
      mBuilderPtr = 0L;
      return;
    }
    throw new IllegalStateException("This FontFamily is already frozen or abandoned");
  }
  
  /* Error */
  public boolean addFont(String paramString, int paramInt1, FontVariationAxis[] paramArrayOfFontVariationAxis, int paramInt2, int paramInt3)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 54	android/graphics/FontFamily:mBuilderPtr	J
    //   4: lconst_0
    //   5: lcmp
    //   6: ifeq +194 -> 200
    //   9: new 103	java/io/FileInputStream
    //   12: astore 6
    //   14: aload 6
    //   16: aload_1
    //   17: invokespecial 104	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
    //   20: aload 6
    //   22: invokevirtual 108	java/io/FileInputStream:getChannel	()Ljava/nio/channels/FileChannel;
    //   25: astore 7
    //   27: aload 7
    //   29: invokevirtual 113	java/nio/channels/FileChannel:size	()J
    //   32: lstore 8
    //   34: aload 7
    //   36: getstatic 119	java/nio/channels/FileChannel$MapMode:READ_ONLY	Ljava/nio/channels/FileChannel$MapMode;
    //   39: lconst_0
    //   40: lload 8
    //   42: invokevirtual 123	java/nio/channels/FileChannel:map	(Ljava/nio/channels/FileChannel$MapMode;JJ)Ljava/nio/MappedByteBuffer;
    //   45: astore 7
    //   47: aload_3
    //   48: ifnull +46 -> 94
    //   51: aload_3
    //   52: arraylength
    //   53: istore 10
    //   55: iconst_0
    //   56: istore 11
    //   58: iload 11
    //   60: iload 10
    //   62: if_icmpge +32 -> 94
    //   65: aload_3
    //   66: iload 11
    //   68: aaload
    //   69: astore 12
    //   71: aload_0
    //   72: getfield 54	android/graphics/FontFamily:mBuilderPtr	J
    //   75: aload 12
    //   77: invokevirtual 129	android/graphics/fonts/FontVariationAxis:getOpenTypeTagValue	()I
    //   80: aload 12
    //   82: invokevirtual 133	android/graphics/fonts/FontVariationAxis:getStyleValue	()F
    //   85: invokestatic 135	android/graphics/FontFamily:nAddAxisValue	(JIF)V
    //   88: iinc 11 1
    //   91: goto -33 -> 58
    //   94: aload_0
    //   95: getfield 54	android/graphics/FontFamily:mBuilderPtr	J
    //   98: aload 7
    //   100: iload_2
    //   101: iload 4
    //   103: iload 5
    //   105: invokestatic 77	android/graphics/FontFamily:nAddFont	(JLjava/nio/ByteBuffer;III)Z
    //   108: istore 13
    //   110: aload 6
    //   112: invokevirtual 138	java/io/FileInputStream:close	()V
    //   115: iload 13
    //   117: ireturn
    //   118: astore 7
    //   120: aconst_null
    //   121: astore_3
    //   122: goto +8 -> 130
    //   125: astore_3
    //   126: aload_3
    //   127: athrow
    //   128: astore 7
    //   130: aload_3
    //   131: ifnull +22 -> 153
    //   134: aload 6
    //   136: invokevirtual 138	java/io/FileInputStream:close	()V
    //   139: goto +19 -> 158
    //   142: astore 6
    //   144: aload_3
    //   145: aload 6
    //   147: invokevirtual 142	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   150: goto +8 -> 158
    //   153: aload 6
    //   155: invokevirtual 138	java/io/FileInputStream:close	()V
    //   158: aload 7
    //   160: athrow
    //   161: astore_3
    //   162: getstatic 20	android/graphics/FontFamily:TAG	Ljava/lang/String;
    //   165: astore 7
    //   167: new 144	java/lang/StringBuilder
    //   170: dup
    //   171: invokespecial 145	java/lang/StringBuilder:<init>	()V
    //   174: astore_3
    //   175: aload_3
    //   176: ldc -109
    //   178: invokevirtual 151	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   181: pop
    //   182: aload_3
    //   183: aload_1
    //   184: invokevirtual 151	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   187: pop
    //   188: aload 7
    //   190: aload_3
    //   191: invokevirtual 155	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   194: invokestatic 161	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   197: pop
    //   198: iconst_0
    //   199: ireturn
    //   200: new 90	java/lang/IllegalStateException
    //   203: dup
    //   204: ldc -93
    //   206: invokespecial 95	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   209: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	210	0	this	FontFamily
    //   0	210	1	paramString	String
    //   0	210	2	paramInt1	int
    //   0	210	3	paramArrayOfFontVariationAxis	FontVariationAxis[]
    //   0	210	4	paramInt2	int
    //   0	210	5	paramInt3	int
    //   12	123	6	localFileInputStream	java.io.FileInputStream
    //   142	12	6	localThrowable	Throwable
    //   25	74	7	localObject1	Object
    //   118	1	7	localObject2	Object
    //   128	31	7	localObject3	Object
    //   165	24	7	str	String
    //   32	9	8	l	long
    //   53	10	10	i	int
    //   56	33	11	j	int
    //   69	12	12	localFontVariationAxis	FontVariationAxis
    //   108	8	13	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   20	47	118	finally
    //   51	55	118	finally
    //   71	88	118	finally
    //   94	110	118	finally
    //   20	47	125	java/lang/Throwable
    //   51	55	125	java/lang/Throwable
    //   71	88	125	java/lang/Throwable
    //   94	110	125	java/lang/Throwable
    //   126	128	128	finally
    //   134	139	142	java/lang/Throwable
    //   9	20	161	java/io/IOException
    //   110	115	161	java/io/IOException
    //   134	139	161	java/io/IOException
    //   144	150	161	java/io/IOException
    //   153	158	161	java/io/IOException
    //   158	161	161	java/io/IOException
  }
  
  public boolean addFontFromAssetManager(AssetManager paramAssetManager, String paramString, int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, int paramInt4, FontVariationAxis[] paramArrayOfFontVariationAxis)
  {
    if (mBuilderPtr != 0L)
    {
      if (paramArrayOfFontVariationAxis != null)
      {
        int i = paramArrayOfFontVariationAxis.length;
        for (int j = 0; j < i; j++)
        {
          FontVariationAxis localFontVariationAxis = paramArrayOfFontVariationAxis[j];
          nAddAxisValue(mBuilderPtr, localFontVariationAxis.getOpenTypeTagValue(), localFontVariationAxis.getStyleValue());
        }
      }
      return nAddFontFromAssetManager(mBuilderPtr, paramAssetManager, paramString, paramInt1, paramBoolean, paramInt2, paramInt3, paramInt4);
    }
    throw new IllegalStateException("Unable to call addFontFromAsset after freezing.");
  }
  
  public boolean addFontFromBuffer(ByteBuffer paramByteBuffer, int paramInt1, FontVariationAxis[] paramArrayOfFontVariationAxis, int paramInt2, int paramInt3)
  {
    if (mBuilderPtr != 0L)
    {
      if (paramArrayOfFontVariationAxis != null)
      {
        int i = paramArrayOfFontVariationAxis.length;
        for (int j = 0; j < i; j++)
        {
          FontVariationAxis localFontVariationAxis = paramArrayOfFontVariationAxis[j];
          nAddAxisValue(mBuilderPtr, localFontVariationAxis.getOpenTypeTagValue(), localFontVariationAxis.getStyleValue());
        }
      }
      return nAddFontWeightStyle(mBuilderPtr, paramByteBuffer, paramInt1, paramInt2, paramInt3);
    }
    throw new IllegalStateException("Unable to call addFontWeightStyle after freezing.");
  }
  
  public boolean freeze()
  {
    if (mBuilderPtr != 0L)
    {
      mNativePtr = nCreateFamily(mBuilderPtr);
      mNativeBuilderCleaner.run();
      mBuilderPtr = 0L;
      if (mNativePtr != 0L) {
        sFamilyRegistry.registerNativeAllocation(this, mNativePtr);
      }
      boolean bool;
      if (mNativePtr != 0L) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    throw new IllegalStateException("This FontFamily is already frozen");
  }
}
