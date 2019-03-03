package android.graphics;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.AssetManager.AssetInputStream;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.DisplayMetrics;
import android.util.Size;
import android.util.TypedValue;
import dalvik.system.CloseGuard;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;
import libcore.io.IoUtils;

public final class ImageDecoder
  implements AutoCloseable
{
  public static final int ALLOCATOR_DEFAULT = 0;
  public static final int ALLOCATOR_HARDWARE = 3;
  public static final int ALLOCATOR_SHARED_MEMORY = 2;
  public static final int ALLOCATOR_SOFTWARE = 1;
  @Deprecated
  public static final int ERROR_SOURCE_ERROR = 3;
  @Deprecated
  public static final int ERROR_SOURCE_EXCEPTION = 1;
  @Deprecated
  public static final int ERROR_SOURCE_INCOMPLETE = 2;
  public static final int MEMORY_POLICY_DEFAULT = 1;
  public static final int MEMORY_POLICY_LOW_RAM = 0;
  public static int sApiLevel;
  private int mAllocator = 0;
  private final boolean mAnimated;
  private AssetFileDescriptor mAssetFd;
  private final CloseGuard mCloseGuard = CloseGuard.get();
  private final AtomicBoolean mClosed = new AtomicBoolean();
  private boolean mConserveMemory = false;
  private Rect mCropRect;
  private boolean mDecodeAsAlphaMask = false;
  private ColorSpace mDesiredColorSpace = null;
  private int mDesiredHeight;
  private int mDesiredWidth;
  private final int mHeight;
  private InputStream mInputStream;
  private final boolean mIsNinePatch;
  private boolean mMutable = false;
  private long mNativePtr;
  private OnPartialImageListener mOnPartialImageListener;
  private Rect mOutPaddingRect;
  private boolean mOwnsInputStream;
  private PostProcessor mPostProcessor;
  private Source mSource;
  private byte[] mTempStorage;
  private boolean mUnpremultipliedRequired = false;
  private final int mWidth;
  
  private ImageDecoder(long paramLong, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
  {
    mNativePtr = paramLong;
    mWidth = paramInt1;
    mHeight = paramInt2;
    mDesiredWidth = paramInt1;
    mDesiredHeight = paramInt2;
    mAnimated = paramBoolean1;
    mIsNinePatch = paramBoolean2;
    mCloseGuard.open("close");
  }
  
  private void callHeaderDecoded(OnHeaderDecodedListener paramOnHeaderDecodedListener, Source paramSource)
  {
    if (paramOnHeaderDecodedListener != null)
    {
      ImageInfo localImageInfo = new ImageInfo(this, null);
      try
      {
        paramOnHeaderDecodedListener.onHeaderDecoded(this, localImageInfo, paramSource);
        ImageInfo.access$1402(localImageInfo, null);
      }
      finally
      {
        ImageInfo.access$1402(localImageInfo, null);
      }
    }
  }
  
  private void checkState()
  {
    if (mNativePtr != 0L)
    {
      checkSubset(mDesiredWidth, mDesiredHeight, mCropRect);
      if (mAllocator == 3) {
        if (!mMutable)
        {
          if (mDecodeAsAlphaMask) {
            throw new IllegalStateException("Cannot make HARDWARE Alpha mask Bitmap!");
          }
        }
        else {
          throw new IllegalStateException("Cannot make mutable HARDWARE Bitmap!");
        }
      }
      if ((mPostProcessor != null) && (mUnpremultipliedRequired)) {
        throw new IllegalStateException("Cannot draw to unpremultiplied pixels!");
      }
      if (mDesiredColorSpace != null)
      {
        StringBuilder localStringBuilder;
        if ((mDesiredColorSpace instanceof ColorSpace.Rgb))
        {
          if (((ColorSpace.Rgb)mDesiredColorSpace).getTransferParameters() == null)
          {
            localStringBuilder = new StringBuilder();
            localStringBuilder.append("The target color space must use an ICC parametric transfer function - provided: ");
            localStringBuilder.append(mDesiredColorSpace);
            throw new IllegalArgumentException(localStringBuilder.toString());
          }
        }
        else
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("The target color space must use the RGB color model - provided: ");
          localStringBuilder.append(mDesiredColorSpace);
          throw new IllegalArgumentException(localStringBuilder.toString());
        }
      }
      return;
    }
    throw new IllegalStateException("Cannot use closed ImageDecoder!");
  }
  
  private static void checkSubset(int paramInt1, int paramInt2, Rect paramRect)
  {
    if (paramRect == null) {
      return;
    }
    if ((left >= 0) && (top >= 0) && (right <= paramInt1) && (bottom <= paramInt2)) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Subset ");
    localStringBuilder.append(paramRect);
    localStringBuilder.append(" not contained by scaled image bounds: (");
    localStringBuilder.append(paramInt1);
    localStringBuilder.append(" x ");
    localStringBuilder.append(paramInt2);
    localStringBuilder.append(")");
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  private int computeDensity(Source paramSource)
  {
    if (requestedResize()) {
      return 0;
    }
    int i = paramSource.getDensity();
    if (i == 0) {
      return i;
    }
    if ((mIsNinePatch) && (mPostProcessor == null)) {
      return i;
    }
    Resources localResources = paramSource.getResources();
    if ((localResources != null) && (getDisplayMetricsnoncompatDensityDpi == i)) {
      return i;
    }
    int j = paramSource.computeDstDensity();
    if (i == j) {
      return i;
    }
    if ((i < j) && (sApiLevel >= 28)) {
      return i;
    }
    float f = j / i;
    setTargetSize((int)(mWidth * f + 0.5F), (int)(mHeight * f + 0.5F));
    return j;
  }
  
  private static ImageDecoder createFromAsset(AssetManager.AssetInputStream paramAssetInputStream, Source paramSource)
    throws IOException
  {
    try
    {
      paramSource = nCreate(paramAssetInputStream.getNativeAsset(), paramSource);
      if (paramSource == null)
      {
        IoUtils.closeQuietly(paramAssetInputStream);
      }
      else
      {
        mInputStream = paramAssetInputStream;
        mOwnsInputStream = true;
      }
      return paramSource;
    }
    finally
    {
      if (0 == 0) {
        IoUtils.closeQuietly(paramAssetInputStream);
      }
    }
  }
  
  private static ImageDecoder createFromFile(File paramFile, Source paramSource)
    throws IOException
  {
    paramFile = new FileInputStream(paramFile);
    FileDescriptor localFileDescriptor = paramFile.getFD();
    try
    {
      Os.lseek(localFileDescriptor, 0L, OsConstants.SEEK_CUR);
      try
      {
        paramSource = nCreate(localFileDescriptor, paramSource);
        if (paramSource == null)
        {
          IoUtils.closeQuietly(paramFile);
        }
        else
        {
          mInputStream = paramFile;
          mOwnsInputStream = true;
        }
        return paramSource;
      }
      finally
      {
        if (0 == 0) {
          IoUtils.closeQuietly(paramFile);
        }
      }
      return createFromStream(paramFile, true, paramSource);
    }
    catch (ErrnoException localErrnoException) {}
  }
  
  private static ImageDecoder createFromStream(InputStream paramInputStream, boolean paramBoolean, Source paramSource)
    throws IOException
  {
    byte[] arrayOfByte = new byte['䀀'];
    try
    {
      paramSource = nCreate(paramInputStream, arrayOfByte, paramSource);
      if (paramSource == null)
      {
        if (paramBoolean) {
          IoUtils.closeQuietly(paramInputStream);
        }
      }
      else
      {
        mInputStream = paramInputStream;
        mOwnsInputStream = paramBoolean;
        mTempStorage = arrayOfByte;
      }
      return paramSource;
    }
    finally
    {
      if ((0 == 0) && (paramBoolean)) {
        IoUtils.closeQuietly(paramInputStream);
      }
    }
  }
  
  public static Source createSource(ContentResolver paramContentResolver, Uri paramUri)
  {
    return new ContentResolverSource(paramContentResolver, paramUri, null);
  }
  
  public static Source createSource(ContentResolver paramContentResolver, Uri paramUri, Resources paramResources)
  {
    return new ContentResolverSource(paramContentResolver, paramUri, paramResources);
  }
  
  public static Source createSource(AssetManager paramAssetManager, String paramString)
  {
    return new AssetSource(paramAssetManager, paramString);
  }
  
  public static Source createSource(Resources paramResources, int paramInt)
  {
    return new ResourceSource(paramResources, paramInt);
  }
  
  public static Source createSource(Resources paramResources, InputStream paramInputStream)
  {
    return new InputStreamSource(paramResources, paramInputStream, Bitmap.getDefaultDensity());
  }
  
  public static Source createSource(Resources paramResources, InputStream paramInputStream, int paramInt)
  {
    return new InputStreamSource(paramResources, paramInputStream, paramInt);
  }
  
  public static Source createSource(File paramFile)
  {
    return new FileSource(paramFile);
  }
  
  public static Source createSource(ByteBuffer paramByteBuffer)
  {
    return new ByteBufferSource(paramByteBuffer);
  }
  
  public static Source createSource(byte[] paramArrayOfByte)
  {
    return createSource(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public static Source createSource(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws ArrayIndexOutOfBoundsException
  {
    if (paramArrayOfByte != null)
    {
      if ((paramInt1 >= 0) && (paramInt2 >= 0) && (paramInt1 < paramArrayOfByte.length) && (paramInt1 + paramInt2 <= paramArrayOfByte.length)) {
        return new ByteArraySource(paramArrayOfByte, paramInt1, paramInt2);
      }
      throw new ArrayIndexOutOfBoundsException("invalid offset/length!");
    }
    throw new NullPointerException("null byte[] in createSource!");
  }
  
  public static Bitmap decodeBitmap(Source paramSource)
    throws IOException
  {
    return decodeBitmapImpl(paramSource, null);
  }
  
  public static Bitmap decodeBitmap(Source paramSource, OnHeaderDecodedListener paramOnHeaderDecodedListener)
    throws IOException
  {
    if (paramOnHeaderDecodedListener != null) {
      return decodeBitmapImpl(paramSource, paramOnHeaderDecodedListener);
    }
    throw new IllegalArgumentException("listener cannot be null! Use decodeBitmap(Source) to not have a listener");
  }
  
  /* Error */
  private static Bitmap decodeBitmapImpl(Source paramSource, OnHeaderDecodedListener paramOnHeaderDecodedListener)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 449	android/graphics/ImageDecoder$Source:createImageDecoder	()Landroid/graphics/ImageDecoder;
    //   4: astore_2
    //   5: aconst_null
    //   6: astore_3
    //   7: aload_3
    //   8: astore 4
    //   10: aload_2
    //   11: aload_0
    //   12: putfield 451	android/graphics/ImageDecoder:mSource	Landroid/graphics/ImageDecoder$Source;
    //   15: aload_3
    //   16: astore 4
    //   18: aload_2
    //   19: aload_1
    //   20: aload_0
    //   21: invokespecial 453	android/graphics/ImageDecoder:callHeaderDecoded	(Landroid/graphics/ImageDecoder$OnHeaderDecodedListener;Landroid/graphics/ImageDecoder$Source;)V
    //   24: aload_3
    //   25: astore 4
    //   27: aload_2
    //   28: aload_0
    //   29: invokespecial 455	android/graphics/ImageDecoder:computeDensity	(Landroid/graphics/ImageDecoder$Source;)I
    //   32: istore 5
    //   34: aload_3
    //   35: astore 4
    //   37: aload_2
    //   38: invokespecial 459	android/graphics/ImageDecoder:decodeBitmapInternal	()Landroid/graphics/Bitmap;
    //   41: astore_1
    //   42: aload_3
    //   43: astore 4
    //   45: aload_1
    //   46: iload 5
    //   48: invokevirtual 463	android/graphics/Bitmap:setDensity	(I)V
    //   51: aload_3
    //   52: astore 4
    //   54: aload_2
    //   55: getfield 465	android/graphics/ImageDecoder:mOutPaddingRect	Landroid/graphics/Rect;
    //   58: astore_0
    //   59: aload_0
    //   60: ifnull +39 -> 99
    //   63: aload_3
    //   64: astore 4
    //   66: aload_1
    //   67: invokevirtual 469	android/graphics/Bitmap:getNinePatchChunk	()[B
    //   70: astore 6
    //   72: aload 6
    //   74: ifnull +25 -> 99
    //   77: aload_3
    //   78: astore 4
    //   80: aload 6
    //   82: invokestatic 475	android/graphics/NinePatch:isNinePatchChunk	([B)Z
    //   85: ifeq +14 -> 99
    //   88: aload_3
    //   89: astore 4
    //   91: aload_2
    //   92: getfield 155	android/graphics/ImageDecoder:mNativePtr	J
    //   95: aload_0
    //   96: invokestatic 479	android/graphics/ImageDecoder:nGetPadding	(JLandroid/graphics/Rect;)V
    //   99: aload_2
    //   100: ifnull +8 -> 108
    //   103: aconst_null
    //   104: aload_2
    //   105: invokestatic 481	android/graphics/ImageDecoder:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   108: aload_1
    //   109: areturn
    //   110: astore_0
    //   111: goto +9 -> 120
    //   114: astore_0
    //   115: aload_0
    //   116: astore 4
    //   118: aload_0
    //   119: athrow
    //   120: aload_2
    //   121: ifnull +9 -> 130
    //   124: aload 4
    //   126: aload_2
    //   127: invokestatic 481	android/graphics/ImageDecoder:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   130: aload_0
    //   131: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	132	0	paramSource	Source
    //   0	132	1	paramOnHeaderDecodedListener	OnHeaderDecodedListener
    //   4	123	2	localImageDecoder	ImageDecoder
    //   6	83	3	localObject1	Object
    //   8	117	4	localObject2	Object
    //   32	15	5	i	int
    //   70	11	6	arrayOfByte	byte[]
    // Exception table:
    //   from	to	target	type
    //   10	15	110	finally
    //   18	24	110	finally
    //   27	34	110	finally
    //   37	42	110	finally
    //   45	51	110	finally
    //   54	59	110	finally
    //   66	72	110	finally
    //   80	88	110	finally
    //   91	99	110	finally
    //   118	120	110	finally
    //   10	15	114	java/lang/Throwable
    //   18	24	114	java/lang/Throwable
    //   27	34	114	java/lang/Throwable
    //   37	42	114	java/lang/Throwable
    //   45	51	114	java/lang/Throwable
    //   54	59	114	java/lang/Throwable
    //   66	72	114	java/lang/Throwable
    //   80	88	114	java/lang/Throwable
    //   91	99	114	java/lang/Throwable
  }
  
  private Bitmap decodeBitmapInternal()
    throws IOException
  {
    checkState();
    long l = mNativePtr;
    if (mPostProcessor != null) {}
    for (boolean bool = true;; bool = false) {
      break;
    }
    return nDecodeBitmap(l, this, bool, mDesiredWidth, mDesiredHeight, mCropRect, mMutable, mAllocator, mUnpremultipliedRequired, mConserveMemory, mDecodeAsAlphaMask, mDesiredColorSpace);
  }
  
  public static Drawable decodeDrawable(Source paramSource)
    throws IOException
  {
    return decodeDrawableImpl(paramSource, null);
  }
  
  public static Drawable decodeDrawable(Source paramSource, OnHeaderDecodedListener paramOnHeaderDecodedListener)
    throws IOException
  {
    if (paramOnHeaderDecodedListener != null) {
      return decodeDrawableImpl(paramSource, paramOnHeaderDecodedListener);
    }
    throw new IllegalArgumentException("listener cannot be null! Use decodeDrawable(Source) to not have a listener");
  }
  
  /* Error */
  private static Drawable decodeDrawableImpl(Source paramSource, OnHeaderDecodedListener paramOnHeaderDecodedListener)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 449	android/graphics/ImageDecoder$Source:createImageDecoder	()Landroid/graphics/ImageDecoder;
    //   4: astore_2
    //   5: aconst_null
    //   6: astore_3
    //   7: aconst_null
    //   8: astore 4
    //   10: aload_2
    //   11: aload_0
    //   12: putfield 451	android/graphics/ImageDecoder:mSource	Landroid/graphics/ImageDecoder$Source;
    //   15: aload 4
    //   17: astore_3
    //   18: aload_2
    //   19: aload_1
    //   20: aload_0
    //   21: invokespecial 453	android/graphics/ImageDecoder:callHeaderDecoded	(Landroid/graphics/ImageDecoder$OnHeaderDecodedListener;Landroid/graphics/ImageDecoder$Source;)V
    //   24: aload 4
    //   26: astore_3
    //   27: aload_2
    //   28: getfield 132	android/graphics/ImageDecoder:mUnpremultipliedRequired	Z
    //   31: ifne +324 -> 355
    //   34: aload 4
    //   36: astore_3
    //   37: aload_2
    //   38: getfield 134	android/graphics/ImageDecoder:mMutable	Z
    //   41: ifne +292 -> 333
    //   44: aload 4
    //   46: astore_3
    //   47: aload_2
    //   48: aload_0
    //   49: invokespecial 455	android/graphics/ImageDecoder:computeDensity	(Landroid/graphics/ImageDecoder$Source;)I
    //   52: istore 5
    //   54: aload 4
    //   56: astore_3
    //   57: aload_2
    //   58: getfield 165	android/graphics/ImageDecoder:mAnimated	Z
    //   61: ifeq +95 -> 156
    //   64: aload 4
    //   66: astore_3
    //   67: aload_2
    //   68: getfield 254	android/graphics/ImageDecoder:mPostProcessor	Landroid/graphics/PostProcessor;
    //   71: ifnonnull +8 -> 79
    //   74: aconst_null
    //   75: astore_1
    //   76: goto +5 -> 81
    //   79: aload_2
    //   80: astore_1
    //   81: aload 4
    //   83: astore_3
    //   84: new 497	android/graphics/drawable/AnimatedImageDrawable
    //   87: astore 6
    //   89: aload 4
    //   91: astore_3
    //   92: aload 6
    //   94: aload_2
    //   95: getfield 155	android/graphics/ImageDecoder:mNativePtr	J
    //   98: aload_1
    //   99: aload_2
    //   100: getfield 161	android/graphics/ImageDecoder:mDesiredWidth	I
    //   103: aload_2
    //   104: getfield 163	android/graphics/ImageDecoder:mDesiredHeight	I
    //   107: iload 5
    //   109: aload_0
    //   110: invokevirtual 337	android/graphics/ImageDecoder$Source:computeDstDensity	()I
    //   113: aload_2
    //   114: getfield 240	android/graphics/ImageDecoder:mCropRect	Landroid/graphics/Rect;
    //   117: aload_2
    //   118: getfield 361	android/graphics/ImageDecoder:mInputStream	Ljava/io/InputStream;
    //   121: aload_2
    //   122: getfield 211	android/graphics/ImageDecoder:mAssetFd	Landroid/content/res/AssetFileDescriptor;
    //   125: invokespecial 500	android/graphics/drawable/AnimatedImageDrawable:<init>	(JLandroid/graphics/ImageDecoder;IIIILandroid/graphics/Rect;Ljava/io/InputStream;Landroid/content/res/AssetFileDescriptor;)V
    //   128: aload 4
    //   130: astore_3
    //   131: aload_2
    //   132: aconst_null
    //   133: putfield 361	android/graphics/ImageDecoder:mInputStream	Ljava/io/InputStream;
    //   136: aload 4
    //   138: astore_3
    //   139: aload_2
    //   140: aconst_null
    //   141: putfield 211	android/graphics/ImageDecoder:mAssetFd	Landroid/content/res/AssetFileDescriptor;
    //   144: aload_2
    //   145: ifnull +8 -> 153
    //   148: aconst_null
    //   149: aload_2
    //   150: invokestatic 481	android/graphics/ImageDecoder:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   153: aload 6
    //   155: areturn
    //   156: aload 4
    //   158: astore_3
    //   159: aload_2
    //   160: invokespecial 459	android/graphics/ImageDecoder:decodeBitmapInternal	()Landroid/graphics/Bitmap;
    //   163: astore 6
    //   165: aload 4
    //   167: astore_3
    //   168: aload 6
    //   170: iload 5
    //   172: invokevirtual 463	android/graphics/Bitmap:setDensity	(I)V
    //   175: aload 4
    //   177: astore_3
    //   178: aload_0
    //   179: invokevirtual 323	android/graphics/ImageDecoder$Source:getResources	()Landroid/content/res/Resources;
    //   182: astore 7
    //   184: aload 4
    //   186: astore_3
    //   187: aload 6
    //   189: invokevirtual 469	android/graphics/Bitmap:getNinePatchChunk	()[B
    //   192: astore 8
    //   194: aload 8
    //   196: ifnull +111 -> 307
    //   199: aload 4
    //   201: astore_3
    //   202: aload 8
    //   204: invokestatic 475	android/graphics/NinePatch:isNinePatchChunk	([B)Z
    //   207: ifeq +100 -> 307
    //   210: aload 4
    //   212: astore_3
    //   213: new 286	android/graphics/Rect
    //   216: astore 9
    //   218: aload 4
    //   220: astore_3
    //   221: aload 9
    //   223: invokespecial 501	android/graphics/Rect:<init>	()V
    //   226: aload 4
    //   228: astore_3
    //   229: aload 6
    //   231: aload 9
    //   233: invokevirtual 505	android/graphics/Bitmap:getOpticalInsets	(Landroid/graphics/Rect;)V
    //   236: aload 4
    //   238: astore_3
    //   239: aload_2
    //   240: getfield 465	android/graphics/ImageDecoder:mOutPaddingRect	Landroid/graphics/Rect;
    //   243: astore_1
    //   244: aload_1
    //   245: astore_0
    //   246: aload_1
    //   247: ifnonnull +17 -> 264
    //   250: aload 4
    //   252: astore_3
    //   253: new 286	android/graphics/Rect
    //   256: astore_0
    //   257: aload 4
    //   259: astore_3
    //   260: aload_0
    //   261: invokespecial 501	android/graphics/Rect:<init>	()V
    //   264: aload 4
    //   266: astore_3
    //   267: aload_2
    //   268: getfield 155	android/graphics/ImageDecoder:mNativePtr	J
    //   271: aload_0
    //   272: invokestatic 479	android/graphics/ImageDecoder:nGetPadding	(JLandroid/graphics/Rect;)V
    //   275: aload 4
    //   277: astore_3
    //   278: new 507	android/graphics/drawable/NinePatchDrawable
    //   281: dup
    //   282: aload 7
    //   284: aload 6
    //   286: aload 8
    //   288: aload_0
    //   289: aload 9
    //   291: aconst_null
    //   292: invokespecial 510	android/graphics/drawable/NinePatchDrawable:<init>	(Landroid/content/res/Resources;Landroid/graphics/Bitmap;[BLandroid/graphics/Rect;Landroid/graphics/Rect;Ljava/lang/String;)V
    //   295: astore_0
    //   296: aload_2
    //   297: ifnull +8 -> 305
    //   300: aconst_null
    //   301: aload_2
    //   302: invokestatic 481	android/graphics/ImageDecoder:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   305: aload_0
    //   306: areturn
    //   307: aload 4
    //   309: astore_3
    //   310: new 512	android/graphics/drawable/BitmapDrawable
    //   313: dup
    //   314: aload 7
    //   316: aload 6
    //   318: invokespecial 515	android/graphics/drawable/BitmapDrawable:<init>	(Landroid/content/res/Resources;Landroid/graphics/Bitmap;)V
    //   321: astore_0
    //   322: aload_2
    //   323: ifnull +8 -> 331
    //   326: aconst_null
    //   327: aload_2
    //   328: invokestatic 481	android/graphics/ImageDecoder:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   331: aload_0
    //   332: areturn
    //   333: aload 4
    //   335: astore_3
    //   336: new 246	java/lang/IllegalStateException
    //   339: astore_0
    //   340: aload 4
    //   342: astore_3
    //   343: aload_0
    //   344: ldc_w 517
    //   347: invokespecial 250	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   350: aload 4
    //   352: astore_3
    //   353: aload_0
    //   354: athrow
    //   355: aload 4
    //   357: astore_3
    //   358: new 246	java/lang/IllegalStateException
    //   361: astore_0
    //   362: aload 4
    //   364: astore_3
    //   365: aload_0
    //   366: ldc_w 519
    //   369: invokespecial 250	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   372: aload 4
    //   374: astore_3
    //   375: aload_0
    //   376: athrow
    //   377: astore_0
    //   378: goto +16 -> 394
    //   381: astore_0
    //   382: goto +8 -> 390
    //   385: astore_0
    //   386: goto +8 -> 394
    //   389: astore_0
    //   390: aload_0
    //   391: astore_3
    //   392: aload_0
    //   393: athrow
    //   394: aload_2
    //   395: ifnull +8 -> 403
    //   398: aload_3
    //   399: aload_2
    //   400: invokestatic 481	android/graphics/ImageDecoder:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   403: aload_0
    //   404: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	405	0	paramSource	Source
    //   0	405	1	paramOnHeaderDecodedListener	OnHeaderDecodedListener
    //   4	396	2	localImageDecoder	ImageDecoder
    //   6	393	3	localObject1	Object
    //   8	365	4	localObject2	Object
    //   52	119	5	i	int
    //   87	230	6	localObject3	Object
    //   182	133	7	localResources	Resources
    //   192	95	8	arrayOfByte	byte[]
    //   216	74	9	localRect	Rect
    // Exception table:
    //   from	to	target	type
    //   18	24	377	finally
    //   27	34	377	finally
    //   37	44	377	finally
    //   47	54	377	finally
    //   57	64	377	finally
    //   67	74	377	finally
    //   84	89	377	finally
    //   92	128	377	finally
    //   131	136	377	finally
    //   139	144	377	finally
    //   159	165	377	finally
    //   168	175	377	finally
    //   178	184	377	finally
    //   187	194	377	finally
    //   202	210	377	finally
    //   213	218	377	finally
    //   221	226	377	finally
    //   229	236	377	finally
    //   239	244	377	finally
    //   253	257	377	finally
    //   260	264	377	finally
    //   267	275	377	finally
    //   278	296	377	finally
    //   310	322	377	finally
    //   336	340	377	finally
    //   343	350	377	finally
    //   353	355	377	finally
    //   358	362	377	finally
    //   365	372	377	finally
    //   375	377	377	finally
    //   392	394	377	finally
    //   18	24	381	java/lang/Throwable
    //   27	34	381	java/lang/Throwable
    //   37	44	381	java/lang/Throwable
    //   47	54	381	java/lang/Throwable
    //   57	64	381	java/lang/Throwable
    //   67	74	381	java/lang/Throwable
    //   84	89	381	java/lang/Throwable
    //   92	128	381	java/lang/Throwable
    //   131	136	381	java/lang/Throwable
    //   139	144	381	java/lang/Throwable
    //   159	165	381	java/lang/Throwable
    //   168	175	381	java/lang/Throwable
    //   178	184	381	java/lang/Throwable
    //   187	194	381	java/lang/Throwable
    //   202	210	381	java/lang/Throwable
    //   213	218	381	java/lang/Throwable
    //   221	226	381	java/lang/Throwable
    //   229	236	381	java/lang/Throwable
    //   239	244	381	java/lang/Throwable
    //   253	257	381	java/lang/Throwable
    //   260	264	381	java/lang/Throwable
    //   267	275	381	java/lang/Throwable
    //   278	296	381	java/lang/Throwable
    //   310	322	381	java/lang/Throwable
    //   336	340	381	java/lang/Throwable
    //   343	350	381	java/lang/Throwable
    //   353	355	381	java/lang/Throwable
    //   358	362	381	java/lang/Throwable
    //   365	372	381	java/lang/Throwable
    //   375	377	381	java/lang/Throwable
    //   10	15	385	finally
    //   10	15	389	java/lang/Throwable
  }
  
  private ColorSpace getColorSpace()
  {
    return nGetColorSpace(mNativePtr);
  }
  
  private String getMimeType()
  {
    return nGetMimeType(mNativePtr);
  }
  
  private int getTargetDimension(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt2 >= paramInt1) {
      return 1;
    }
    int i = paramInt1 / paramInt2;
    if (paramInt3 == i) {
      return paramInt3;
    }
    if (Math.abs(paramInt3 * paramInt2 - paramInt1) < paramInt2) {
      return paramInt3;
    }
    return i;
  }
  
  private static native void nClose(long paramLong);
  
  private static native ImageDecoder nCreate(long paramLong, Source paramSource)
    throws IOException;
  
  private static native ImageDecoder nCreate(FileDescriptor paramFileDescriptor, Source paramSource)
    throws IOException;
  
  private static native ImageDecoder nCreate(InputStream paramInputStream, byte[] paramArrayOfByte, Source paramSource)
    throws IOException;
  
  private static native ImageDecoder nCreate(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, Source paramSource)
    throws IOException;
  
  private static native ImageDecoder nCreate(byte[] paramArrayOfByte, int paramInt1, int paramInt2, Source paramSource)
    throws IOException;
  
  private static native Bitmap nDecodeBitmap(long paramLong, ImageDecoder paramImageDecoder, boolean paramBoolean1, int paramInt1, int paramInt2, Rect paramRect, boolean paramBoolean2, int paramInt3, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, ColorSpace paramColorSpace)
    throws IOException;
  
  private static native ColorSpace nGetColorSpace(long paramLong);
  
  private static native String nGetMimeType(long paramLong);
  
  private static native void nGetPadding(long paramLong, Rect paramRect);
  
  private static native Size nGetSampledSize(long paramLong, int paramInt);
  
  private void onPartialImage(int paramInt, Throwable paramThrowable)
    throws ImageDecoder.DecodeException
  {
    paramThrowable = new DecodeException(paramInt, paramThrowable, mSource);
    if ((mOnPartialImageListener != null) && (mOnPartialImageListener.onPartialImage(paramThrowable))) {
      return;
    }
    throw paramThrowable;
  }
  
  private int postProcessAndRelease(Canvas paramCanvas)
  {
    try
    {
      int i = mPostProcessor.onPostProcess(paramCanvas);
      return i;
    }
    finally
    {
      paramCanvas.release();
    }
  }
  
  private boolean requestedResize()
  {
    boolean bool;
    if ((mWidth == mDesiredWidth) && (mHeight == mDesiredHeight)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public void close()
  {
    mCloseGuard.close();
    if (!mClosed.compareAndSet(false, true)) {
      return;
    }
    nClose(mNativePtr);
    mNativePtr = 0L;
    if (mOwnsInputStream) {
      IoUtils.closeQuietly(mInputStream);
    }
    IoUtils.closeQuietly(mAssetFd);
    mInputStream = null;
    mAssetFd = null;
    mTempStorage = null;
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (mCloseGuard != null) {
        mCloseGuard.warnIfOpen();
      }
      mInputStream = null;
      mAssetFd = null;
      close();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public int getAllocator()
  {
    return mAllocator;
  }
  
  @Deprecated
  public boolean getAsAlphaMask()
  {
    return getDecodeAsAlphaMask();
  }
  
  @Deprecated
  public boolean getConserveMemory()
  {
    return mConserveMemory;
  }
  
  public Rect getCrop()
  {
    return mCropRect;
  }
  
  @Deprecated
  public boolean getDecodeAsAlphaMask()
  {
    return mDecodeAsAlphaMask;
  }
  
  public int getMemorySizePolicy()
  {
    return mConserveMemory ^ true;
  }
  
  @Deprecated
  public boolean getMutable()
  {
    return isMutableRequired();
  }
  
  public OnPartialImageListener getOnPartialImageListener()
  {
    return mOnPartialImageListener;
  }
  
  public PostProcessor getPostProcessor()
  {
    return mPostProcessor;
  }
  
  @Deprecated
  public boolean getRequireUnpremultiplied()
  {
    return isUnpremultipliedRequired();
  }
  
  public Size getSampledSize(int paramInt)
  {
    if (paramInt > 0)
    {
      if (mNativePtr != 0L) {
        return nGetSampledSize(mNativePtr, paramInt);
      }
      throw new IllegalStateException("ImageDecoder is closed!");
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("sampleSize must be positive! provided ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public boolean isDecodeAsAlphaMaskEnabled()
  {
    return mDecodeAsAlphaMask;
  }
  
  public boolean isMutableRequired()
  {
    return mMutable;
  }
  
  public boolean isUnpremultipliedRequired()
  {
    return mUnpremultipliedRequired;
  }
  
  public void setAllocator(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= 3))
    {
      mAllocator = paramInt;
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("invalid allocator ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  @Deprecated
  public ImageDecoder setAsAlphaMask(boolean paramBoolean)
  {
    setDecodeAsAlphaMask(paramBoolean);
    return this;
  }
  
  @Deprecated
  public void setConserveMemory(boolean paramBoolean)
  {
    mConserveMemory = paramBoolean;
  }
  
  public void setCrop(Rect paramRect)
  {
    mCropRect = paramRect;
  }
  
  @Deprecated
  public ImageDecoder setDecodeAsAlphaMask(boolean paramBoolean)
  {
    setDecodeAsAlphaMaskEnabled(paramBoolean);
    return this;
  }
  
  public void setDecodeAsAlphaMaskEnabled(boolean paramBoolean)
  {
    mDecodeAsAlphaMask = paramBoolean;
  }
  
  public void setMemorySizePolicy(int paramInt)
  {
    boolean bool;
    if (paramInt == 0) {
      bool = true;
    } else {
      bool = false;
    }
    mConserveMemory = bool;
  }
  
  @Deprecated
  public ImageDecoder setMutable(boolean paramBoolean)
  {
    setMutableRequired(paramBoolean);
    return this;
  }
  
  public void setMutableRequired(boolean paramBoolean)
  {
    mMutable = paramBoolean;
  }
  
  public void setOnPartialImageListener(OnPartialImageListener paramOnPartialImageListener)
  {
    mOnPartialImageListener = paramOnPartialImageListener;
  }
  
  public void setOutPaddingRect(Rect paramRect)
  {
    mOutPaddingRect = paramRect;
  }
  
  public void setPostProcessor(PostProcessor paramPostProcessor)
  {
    mPostProcessor = paramPostProcessor;
  }
  
  @Deprecated
  public ImageDecoder setRequireUnpremultiplied(boolean paramBoolean)
  {
    setUnpremultipliedRequired(paramBoolean);
    return this;
  }
  
  @Deprecated
  public ImageDecoder setResize(int paramInt)
  {
    setTargetSampleSize(paramInt);
    return this;
  }
  
  @Deprecated
  public ImageDecoder setResize(int paramInt1, int paramInt2)
  {
    setTargetSize(paramInt1, paramInt2);
    return this;
  }
  
  public void setTargetColorSpace(ColorSpace paramColorSpace)
  {
    mDesiredColorSpace = paramColorSpace;
  }
  
  public void setTargetSampleSize(int paramInt)
  {
    Size localSize = getSampledSize(paramInt);
    setTargetSize(getTargetDimension(mWidth, paramInt, localSize.getWidth()), getTargetDimension(mHeight, paramInt, localSize.getHeight()));
  }
  
  public void setTargetSize(int paramInt1, int paramInt2)
  {
    if ((paramInt1 > 0) && (paramInt2 > 0))
    {
      mDesiredWidth = paramInt1;
      mDesiredHeight = paramInt2;
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Dimensions must be positive! provided (");
    localStringBuilder.append(paramInt1);
    localStringBuilder.append(", ");
    localStringBuilder.append(paramInt2);
    localStringBuilder.append(")");
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public void setUnpremultipliedRequired(boolean paramBoolean)
  {
    mUnpremultipliedRequired = paramBoolean;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Allocator {}
  
  public static class AssetInputStreamSource
    extends ImageDecoder.Source
  {
    private AssetManager.AssetInputStream mAssetInputStream;
    private final int mDensity;
    private final Resources mResources;
    
    public AssetInputStreamSource(AssetManager.AssetInputStream paramAssetInputStream, Resources paramResources, TypedValue paramTypedValue)
    {
      super();
      mAssetInputStream = paramAssetInputStream;
      mResources = paramResources;
      if (density == 0) {
        mDensity = 160;
      } else if (density != 65535) {
        mDensity = density;
      } else {
        mDensity = 0;
      }
    }
    
    public ImageDecoder createImageDecoder()
      throws IOException
    {
      try
      {
        if (mAssetInputStream != null)
        {
          localObject1 = mAssetInputStream;
          mAssetInputStream = null;
          localObject1 = ImageDecoder.createFromAsset((AssetManager.AssetInputStream)localObject1, this);
          return localObject1;
        }
        Object localObject1 = new java/io/IOException;
        ((IOException)localObject1).<init>("Cannot reuse AssetInputStreamSource");
        throw ((Throwable)localObject1);
      }
      finally {}
    }
    
    public int getDensity()
    {
      return mDensity;
    }
    
    public Resources getResources()
    {
      return mResources;
    }
  }
  
  private static class AssetSource
    extends ImageDecoder.Source
  {
    private final AssetManager mAssets;
    private final String mFileName;
    
    AssetSource(AssetManager paramAssetManager, String paramString)
    {
      super();
      mAssets = paramAssetManager;
      mFileName = paramString;
    }
    
    public ImageDecoder createImageDecoder()
      throws IOException
    {
      return ImageDecoder.createFromAsset((AssetManager.AssetInputStream)mAssets.open(mFileName), this);
    }
  }
  
  private static class ByteArraySource
    extends ImageDecoder.Source
  {
    private final byte[] mData;
    private final int mLength;
    private final int mOffset;
    
    ByteArraySource(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    {
      super();
      mData = paramArrayOfByte;
      mOffset = paramInt1;
      mLength = paramInt2;
    }
    
    public ImageDecoder createImageDecoder()
      throws IOException
    {
      return ImageDecoder.nCreate(mData, mOffset, mLength, this);
    }
  }
  
  private static class ByteBufferSource
    extends ImageDecoder.Source
  {
    private final ByteBuffer mBuffer;
    
    ByteBufferSource(ByteBuffer paramByteBuffer)
    {
      super();
      mBuffer = paramByteBuffer;
    }
    
    public ImageDecoder createImageDecoder()
      throws IOException
    {
      if ((!mBuffer.isDirect()) && (mBuffer.hasArray()))
      {
        int i = mBuffer.arrayOffset();
        int j = mBuffer.position();
        int k = mBuffer.limit();
        int m = mBuffer.position();
        return ImageDecoder.nCreate(mBuffer.array(), i + j, k - m, this);
      }
      ByteBuffer localByteBuffer = mBuffer.slice();
      return ImageDecoder.nCreate(localByteBuffer, localByteBuffer.position(), localByteBuffer.limit(), this);
    }
  }
  
  private static class ContentResolverSource
    extends ImageDecoder.Source
  {
    private final ContentResolver mResolver;
    private final Resources mResources;
    private final Uri mUri;
    
    ContentResolverSource(ContentResolver paramContentResolver, Uri paramUri, Resources paramResources)
    {
      super();
      mResolver = paramContentResolver;
      mUri = paramUri;
      mResources = paramResources;
    }
    
    /* Error */
    public ImageDecoder createImageDecoder()
      throws IOException
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 22	android/graphics/ImageDecoder$ContentResolverSource:mUri	Landroid/net/Uri;
      //   4: invokevirtual 39	android/net/Uri:getScheme	()Ljava/lang/String;
      //   7: ldc 41
      //   9: if_acmpne +21 -> 30
      //   12: aload_0
      //   13: getfield 20	android/graphics/ImageDecoder$ContentResolverSource:mResolver	Landroid/content/ContentResolver;
      //   16: aload_0
      //   17: getfield 22	android/graphics/ImageDecoder$ContentResolverSource:mUri	Landroid/net/Uri;
      //   20: ldc 43
      //   22: aconst_null
      //   23: invokevirtual 49	android/content/ContentResolver:openTypedAssetFileDescriptor	(Landroid/net/Uri;Ljava/lang/String;Landroid/os/Bundle;)Landroid/content/res/AssetFileDescriptor;
      //   26: astore_1
      //   27: goto +17 -> 44
      //   30: aload_0
      //   31: getfield 20	android/graphics/ImageDecoder$ContentResolverSource:mResolver	Landroid/content/ContentResolver;
      //   34: aload_0
      //   35: getfield 22	android/graphics/ImageDecoder$ContentResolverSource:mUri	Landroid/net/Uri;
      //   38: ldc 51
      //   40: invokevirtual 55	android/content/ContentResolver:openAssetFileDescriptor	(Landroid/net/Uri;Ljava/lang/String;)Landroid/content/res/AssetFileDescriptor;
      //   43: astore_1
      //   44: aload_1
      //   45: invokevirtual 61	android/content/res/AssetFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
      //   48: astore_2
      //   49: aload_1
      //   50: invokevirtual 65	android/content/res/AssetFileDescriptor:getStartOffset	()J
      //   53: lstore_3
      //   54: aload_2
      //   55: lload_3
      //   56: getstatic 71	android/system/OsConstants:SEEK_SET	I
      //   59: invokestatic 77	android/system/Os:lseek	(Ljava/io/FileDescriptor;JI)J
      //   62: pop2
      //   63: aload_2
      //   64: aload_0
      //   65: invokestatic 81	android/graphics/ImageDecoder:access$400	(Ljava/io/FileDescriptor;Landroid/graphics/ImageDecoder$Source;)Landroid/graphics/ImageDecoder;
      //   68: astore 5
      //   70: goto +30 -> 100
      //   73: astore 5
      //   75: goto +47 -> 122
      //   78: astore 5
      //   80: new 83	java/io/FileInputStream
      //   83: astore 5
      //   85: aload 5
      //   87: aload_2
      //   88: invokespecial 86	java/io/FileInputStream:<init>	(Ljava/io/FileDescriptor;)V
      //   91: aload 5
      //   93: iconst_1
      //   94: aload_0
      //   95: invokestatic 90	android/graphics/ImageDecoder:access$300	(Ljava/io/InputStream;ZLandroid/graphics/ImageDecoder$Source;)Landroid/graphics/ImageDecoder;
      //   98: astore 5
      //   100: aload 5
      //   102: ifnonnull +10 -> 112
      //   105: aload_1
      //   106: invokestatic 96	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   109: goto +10 -> 119
      //   112: aload 5
      //   114: aload_1
      //   115: invokestatic 100	android/graphics/ImageDecoder:access$502	(Landroid/graphics/ImageDecoder;Landroid/content/res/AssetFileDescriptor;)Landroid/content/res/AssetFileDescriptor;
      //   118: pop
      //   119: aload 5
      //   121: areturn
      //   122: iconst_0
      //   123: ifne +10 -> 133
      //   126: aload_1
      //   127: invokestatic 96	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   130: goto +9 -> 139
      //   133: aconst_null
      //   134: aload_1
      //   135: invokestatic 100	android/graphics/ImageDecoder:access$502	(Landroid/graphics/ImageDecoder;Landroid/content/res/AssetFileDescriptor;)Landroid/content/res/AssetFileDescriptor;
      //   138: pop
      //   139: aload 5
      //   141: athrow
      //   142: astore_1
      //   143: aload_0
      //   144: getfield 20	android/graphics/ImageDecoder$ContentResolverSource:mResolver	Landroid/content/ContentResolver;
      //   147: aload_0
      //   148: getfield 22	android/graphics/ImageDecoder$ContentResolverSource:mUri	Landroid/net/Uri;
      //   151: invokevirtual 104	android/content/ContentResolver:openInputStream	(Landroid/net/Uri;)Ljava/io/InputStream;
      //   154: astore_1
      //   155: aload_1
      //   156: ifnull +10 -> 166
      //   159: aload_1
      //   160: iconst_1
      //   161: aload_0
      //   162: invokestatic 90	android/graphics/ImageDecoder:access$300	(Ljava/io/InputStream;ZLandroid/graphics/ImageDecoder$Source;)Landroid/graphics/ImageDecoder;
      //   165: areturn
      //   166: new 31	java/io/FileNotFoundException
      //   169: dup
      //   170: aload_0
      //   171: getfield 22	android/graphics/ImageDecoder$ContentResolverSource:mUri	Landroid/net/Uri;
      //   174: invokevirtual 107	android/net/Uri:toString	()Ljava/lang/String;
      //   177: invokespecial 110	java/io/FileNotFoundException:<init>	(Ljava/lang/String;)V
      //   180: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	181	0	this	ContentResolverSource
      //   26	109	1	localAssetFileDescriptor	AssetFileDescriptor
      //   142	1	1	localFileNotFoundException	java.io.FileNotFoundException
      //   154	6	1	localInputStream	InputStream
      //   48	40	2	localFileDescriptor	FileDescriptor
      //   53	3	3	l	long
      //   68	1	5	localImageDecoder	ImageDecoder
      //   73	1	5	localObject1	Object
      //   78	1	5	localErrnoException	ErrnoException
      //   83	57	5	localObject2	Object
      // Exception table:
      //   from	to	target	type
      //   54	70	73	finally
      //   80	100	73	finally
      //   54	70	78	android/system/ErrnoException
      //   0	27	142	java/io/FileNotFoundException
      //   30	44	142	java/io/FileNotFoundException
    }
    
    Resources getResources()
    {
      return mResources;
    }
  }
  
  public static final class DecodeException
    extends IOException
  {
    public static final int SOURCE_EXCEPTION = 1;
    public static final int SOURCE_INCOMPLETE = 2;
    public static final int SOURCE_MALFORMED_DATA = 3;
    final int mError;
    final ImageDecoder.Source mSource;
    
    DecodeException(int paramInt, String paramString, Throwable paramThrowable, ImageDecoder.Source paramSource)
    {
      super(paramThrowable);
      mError = paramInt;
      mSource = paramSource;
    }
    
    DecodeException(int paramInt, Throwable paramThrowable, ImageDecoder.Source paramSource)
    {
      super(paramThrowable);
      mError = paramInt;
      mSource = paramSource;
    }
    
    private static String errorMessage(int paramInt, Throwable paramThrowable)
    {
      switch (paramInt)
      {
      default: 
        return "";
      case 3: 
        return "Input contained an error.";
      case 2: 
        return "Input was incomplete.";
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Exception in input: ");
      localStringBuilder.append(paramThrowable);
      return localStringBuilder.toString();
    }
    
    public int getError()
    {
      return mError;
    }
    
    public ImageDecoder.Source getSource()
    {
      return mSource;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface Error {}
  }
  
  private static class FileSource
    extends ImageDecoder.Source
  {
    private final File mFile;
    
    FileSource(File paramFile)
    {
      super();
      mFile = paramFile;
    }
    
    public ImageDecoder createImageDecoder()
      throws IOException
    {
      return ImageDecoder.createFromFile(mFile, this);
    }
  }
  
  public static class ImageInfo
  {
    private ImageDecoder mDecoder;
    private final Size mSize;
    
    private ImageInfo(ImageDecoder paramImageDecoder)
    {
      mSize = new Size(mWidth, mHeight);
      mDecoder = paramImageDecoder;
    }
    
    public ColorSpace getColorSpace()
    {
      return mDecoder.getColorSpace();
    }
    
    public String getMimeType()
    {
      return mDecoder.getMimeType();
    }
    
    public Size getSize()
    {
      return mSize;
    }
    
    public boolean isAnimated()
    {
      return mDecoder.mAnimated;
    }
  }
  
  @Deprecated
  public static class IncompleteException
    extends IOException
  {
    public IncompleteException() {}
  }
  
  private static class InputStreamSource
    extends ImageDecoder.Source
  {
    final int mInputDensity;
    InputStream mInputStream;
    final Resources mResources;
    
    InputStreamSource(Resources paramResources, InputStream paramInputStream, int paramInt)
    {
      super();
      if (paramInputStream != null)
      {
        mResources = paramResources;
        mInputStream = paramInputStream;
        if (paramResources == null) {
          paramInt = 0;
        }
        mInputDensity = paramInt;
        return;
      }
      throw new IllegalArgumentException("The InputStream cannot be null");
    }
    
    public ImageDecoder createImageDecoder()
      throws IOException
    {
      try
      {
        if (mInputStream != null)
        {
          localObject1 = mInputStream;
          mInputStream = null;
          localObject1 = ImageDecoder.createFromStream((InputStream)localObject1, false, this);
          return localObject1;
        }
        Object localObject1 = new java/io/IOException;
        ((IOException)localObject1).<init>("Cannot reuse InputStreamSource");
        throw ((Throwable)localObject1);
      }
      finally {}
    }
    
    public int getDensity()
    {
      return mInputDensity;
    }
    
    public Resources getResources()
    {
      return mResources;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface MemoryPolicy {}
  
  public static abstract interface OnHeaderDecodedListener
  {
    public abstract void onHeaderDecoded(ImageDecoder paramImageDecoder, ImageDecoder.ImageInfo paramImageInfo, ImageDecoder.Source paramSource);
  }
  
  public static abstract interface OnPartialImageListener
  {
    public abstract boolean onPartialImage(ImageDecoder.DecodeException paramDecodeException);
  }
  
  private static class ResourceSource
    extends ImageDecoder.Source
  {
    private Object mLock = new Object();
    int mResDensity;
    final int mResId;
    final Resources mResources;
    
    ResourceSource(Resources paramResources, int paramInt)
    {
      super();
      mResources = paramResources;
      mResId = paramInt;
      mResDensity = 0;
    }
    
    public ImageDecoder createImageDecoder()
      throws IOException
    {
      TypedValue localTypedValue = new TypedValue();
      InputStream localInputStream = mResources.openRawResource(mResId, localTypedValue);
      synchronized (mLock)
      {
        if (density == 0) {
          mResDensity = 160;
        } else if (density != 65535) {
          mResDensity = density;
        }
        return ImageDecoder.createFromAsset((AssetManager.AssetInputStream)localInputStream, this);
      }
    }
    
    public int getDensity()
    {
      synchronized (mLock)
      {
        int i = mResDensity;
        return i;
      }
    }
    
    public Resources getResources()
    {
      return mResources;
    }
  }
  
  public static abstract class Source
  {
    private Source() {}
    
    final int computeDstDensity()
    {
      Resources localResources = getResources();
      if (localResources == null) {
        return Bitmap.getDefaultDensity();
      }
      return getDisplayMetricsdensityDpi;
    }
    
    abstract ImageDecoder createImageDecoder()
      throws IOException;
    
    int getDensity()
    {
      return 0;
    }
    
    Resources getResources()
    {
      return null;
    }
  }
}
