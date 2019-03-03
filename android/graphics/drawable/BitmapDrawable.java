package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Insets;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import com.android.internal.R.styleable;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class BitmapDrawable
  extends Drawable
{
  private static final int DEFAULT_PAINT_FLAGS = 6;
  private static final int TILE_MODE_CLAMP = 0;
  private static final int TILE_MODE_DISABLED = -1;
  private static final int TILE_MODE_MIRROR = 2;
  private static final int TILE_MODE_REPEAT = 1;
  private static final int TILE_MODE_UNDEFINED = -2;
  private int mBitmapHeight;
  private BitmapState mBitmapState;
  private int mBitmapWidth;
  private final Rect mDstRect = new Rect();
  private boolean mDstRectAndInsetsDirty = true;
  private Matrix mMirrorMatrix;
  private boolean mMutated;
  private Insets mOpticalInsets = Insets.NONE;
  private int mTargetDensity = 160;
  private PorterDuffColorFilter mTintFilter;
  
  @Deprecated
  public BitmapDrawable()
  {
    init(new BitmapState((Bitmap)null), null);
  }
  
  @Deprecated
  public BitmapDrawable(Resources paramResources)
  {
    init(new BitmapState((Bitmap)null), paramResources);
  }
  
  public BitmapDrawable(Resources paramResources, Bitmap paramBitmap)
  {
    init(new BitmapState(paramBitmap), paramResources);
  }
  
  /* Error */
  public BitmapDrawable(Resources paramResources, InputStream paramInputStream)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 57	android/graphics/drawable/Drawable:<init>	()V
    //   4: aload_0
    //   5: new 59	android/graphics/Rect
    //   8: dup
    //   9: invokespecial 60	android/graphics/Rect:<init>	()V
    //   12: putfield 62	android/graphics/drawable/BitmapDrawable:mDstRect	Landroid/graphics/Rect;
    //   15: aload_0
    //   16: sipush 160
    //   19: putfield 64	android/graphics/drawable/BitmapDrawable:mTargetDensity	I
    //   22: aload_0
    //   23: iconst_1
    //   24: putfield 66	android/graphics/drawable/BitmapDrawable:mDstRectAndInsetsDirty	Z
    //   27: aload_0
    //   28: getstatic 71	android/graphics/Insets:NONE	Landroid/graphics/Insets;
    //   31: putfield 73	android/graphics/drawable/BitmapDrawable:mOpticalInsets	Landroid/graphics/Insets;
    //   34: aload_1
    //   35: aload_2
    //   36: invokestatic 94	android/graphics/ImageDecoder:createSource	(Landroid/content/res/Resources;Ljava/io/InputStream;)Landroid/graphics/ImageDecoder$Source;
    //   39: getstatic 100	android/graphics/drawable/_$$Lambda$BitmapDrawable$T1BUUqQwU4Z6Ve8DJHFuQvYohkY:INSTANCE	Landroid/graphics/drawable/-$$Lambda$BitmapDrawable$T1BUUqQwU4Z6Ve8DJHFuQvYohkY;
    //   42: invokestatic 104	android/graphics/ImageDecoder:decodeBitmap	(Landroid/graphics/ImageDecoder$Source;Landroid/graphics/ImageDecoder$OnHeaderDecodedListener;)Landroid/graphics/Bitmap;
    //   45: astore_3
    //   46: aload_0
    //   47: new 8	android/graphics/drawable/BitmapDrawable$BitmapState
    //   50: dup
    //   51: aload_3
    //   52: invokespecial 78	android/graphics/drawable/BitmapDrawable$BitmapState:<init>	(Landroid/graphics/Bitmap;)V
    //   55: aload_1
    //   56: invokespecial 82	android/graphics/drawable/BitmapDrawable:init	(Landroid/graphics/drawable/BitmapDrawable$BitmapState;Landroid/content/res/Resources;)V
    //   59: aload_0
    //   60: getfield 106	android/graphics/drawable/BitmapDrawable:mBitmapState	Landroid/graphics/drawable/BitmapDrawable$BitmapState;
    //   63: getfield 110	android/graphics/drawable/BitmapDrawable$BitmapState:mBitmap	Landroid/graphics/Bitmap;
    //   66: ifnonnull +129 -> 195
    //   69: new 112	java/lang/StringBuilder
    //   72: dup
    //   73: invokespecial 113	java/lang/StringBuilder:<init>	()V
    //   76: astore_1
    //   77: aload_1
    //   78: ldc 115
    //   80: invokevirtual 119	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   83: pop
    //   84: aload_1
    //   85: aload_2
    //   86: invokevirtual 122	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   89: pop
    //   90: ldc 124
    //   92: aload_1
    //   93: invokevirtual 128	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   96: invokestatic 134	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   99: pop
    //   100: goto +95 -> 195
    //   103: astore_3
    //   104: aload_0
    //   105: new 8	android/graphics/drawable/BitmapDrawable$BitmapState
    //   108: dup
    //   109: aconst_null
    //   110: invokespecial 78	android/graphics/drawable/BitmapDrawable$BitmapState:<init>	(Landroid/graphics/Bitmap;)V
    //   113: aload_1
    //   114: invokespecial 82	android/graphics/drawable/BitmapDrawable:init	(Landroid/graphics/drawable/BitmapDrawable$BitmapState;Landroid/content/res/Resources;)V
    //   117: aload_0
    //   118: getfield 106	android/graphics/drawable/BitmapDrawable:mBitmapState	Landroid/graphics/drawable/BitmapDrawable$BitmapState;
    //   121: getfield 110	android/graphics/drawable/BitmapDrawable$BitmapState:mBitmap	Landroid/graphics/Bitmap;
    //   124: ifnonnull +34 -> 158
    //   127: new 112	java/lang/StringBuilder
    //   130: dup
    //   131: invokespecial 113	java/lang/StringBuilder:<init>	()V
    //   134: astore_1
    //   135: aload_1
    //   136: ldc 115
    //   138: invokevirtual 119	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   141: pop
    //   142: aload_1
    //   143: aload_2
    //   144: invokevirtual 122	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   147: pop
    //   148: ldc 124
    //   150: aload_1
    //   151: invokevirtual 128	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   154: invokestatic 134	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   157: pop
    //   158: aload_3
    //   159: athrow
    //   160: astore_3
    //   161: aload_0
    //   162: new 8	android/graphics/drawable/BitmapDrawable$BitmapState
    //   165: dup
    //   166: aconst_null
    //   167: invokespecial 78	android/graphics/drawable/BitmapDrawable$BitmapState:<init>	(Landroid/graphics/Bitmap;)V
    //   170: aload_1
    //   171: invokespecial 82	android/graphics/drawable/BitmapDrawable:init	(Landroid/graphics/drawable/BitmapDrawable$BitmapState;Landroid/content/res/Resources;)V
    //   174: aload_0
    //   175: getfield 106	android/graphics/drawable/BitmapDrawable:mBitmapState	Landroid/graphics/drawable/BitmapDrawable$BitmapState;
    //   178: getfield 110	android/graphics/drawable/BitmapDrawable$BitmapState:mBitmap	Landroid/graphics/Bitmap;
    //   181: ifnonnull +14 -> 195
    //   184: new 112	java/lang/StringBuilder
    //   187: dup
    //   188: invokespecial 113	java/lang/StringBuilder:<init>	()V
    //   191: astore_1
    //   192: goto -115 -> 77
    //   195: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	196	0	this	BitmapDrawable
    //   0	196	1	paramResources	Resources
    //   0	196	2	paramInputStream	InputStream
    //   45	7	3	localBitmap	Bitmap
    //   103	56	3	localObject	Object
    //   160	1	3	localException	Exception
    // Exception table:
    //   from	to	target	type
    //   34	46	103	finally
    //   34	46	160	java/lang/Exception
  }
  
  /* Error */
  public BitmapDrawable(Resources paramResources, String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 57	android/graphics/drawable/Drawable:<init>	()V
    //   4: aload_0
    //   5: new 59	android/graphics/Rect
    //   8: dup
    //   9: invokespecial 60	android/graphics/Rect:<init>	()V
    //   12: putfield 62	android/graphics/drawable/BitmapDrawable:mDstRect	Landroid/graphics/Rect;
    //   15: aload_0
    //   16: sipush 160
    //   19: putfield 64	android/graphics/drawable/BitmapDrawable:mTargetDensity	I
    //   22: aload_0
    //   23: iconst_1
    //   24: putfield 66	android/graphics/drawable/BitmapDrawable:mDstRectAndInsetsDirty	Z
    //   27: aload_0
    //   28: getstatic 71	android/graphics/Insets:NONE	Landroid/graphics/Insets;
    //   31: putfield 73	android/graphics/drawable/BitmapDrawable:mOpticalInsets	Landroid/graphics/Insets;
    //   34: aconst_null
    //   35: astore_3
    //   36: aconst_null
    //   37: astore 4
    //   39: aconst_null
    //   40: astore 5
    //   42: aload 5
    //   44: astore 6
    //   46: aload 4
    //   48: astore 7
    //   50: new 137	java/io/FileInputStream
    //   53: astore 8
    //   55: aload 5
    //   57: astore 6
    //   59: aload 4
    //   61: astore 7
    //   63: aload 8
    //   65: aload_2
    //   66: invokespecial 140	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
    //   69: aload_1
    //   70: aload 8
    //   72: invokestatic 94	android/graphics/ImageDecoder:createSource	(Landroid/content/res/Resources;Ljava/io/InputStream;)Landroid/graphics/ImageDecoder$Source;
    //   75: getstatic 145	android/graphics/drawable/_$$Lambda$BitmapDrawable$23eAuhdkgEf5MIRJC_rMNbn4Pyg:INSTANCE	Landroid/graphics/drawable/-$$Lambda$BitmapDrawable$23eAuhdkgEf5MIRJC-rMNbn4Pyg;
    //   78: invokestatic 104	android/graphics/ImageDecoder:decodeBitmap	(Landroid/graphics/ImageDecoder$Source;Landroid/graphics/ImageDecoder$OnHeaderDecodedListener;)Landroid/graphics/Bitmap;
    //   81: astore 6
    //   83: aload 6
    //   85: astore_3
    //   86: aload_3
    //   87: astore 6
    //   89: aload_3
    //   90: astore 7
    //   92: aconst_null
    //   93: aload 8
    //   95: invokestatic 147	android/graphics/drawable/BitmapDrawable:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   98: aload_0
    //   99: new 8	android/graphics/drawable/BitmapDrawable$BitmapState
    //   102: dup
    //   103: aload_3
    //   104: invokespecial 78	android/graphics/drawable/BitmapDrawable$BitmapState:<init>	(Landroid/graphics/Bitmap;)V
    //   107: aload_1
    //   108: invokespecial 82	android/graphics/drawable/BitmapDrawable:init	(Landroid/graphics/drawable/BitmapDrawable$BitmapState;Landroid/content/res/Resources;)V
    //   111: aload_0
    //   112: getfield 106	android/graphics/drawable/BitmapDrawable:mBitmapState	Landroid/graphics/drawable/BitmapDrawable$BitmapState;
    //   115: getfield 110	android/graphics/drawable/BitmapDrawable$BitmapState:mBitmap	Landroid/graphics/Bitmap;
    //   118: ifnonnull +169 -> 287
    //   121: new 112	java/lang/StringBuilder
    //   124: dup
    //   125: invokespecial 113	java/lang/StringBuilder:<init>	()V
    //   128: astore_1
    //   129: aload_1
    //   130: ldc 115
    //   132: invokevirtual 119	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   135: pop
    //   136: aload_1
    //   137: aload_2
    //   138: invokevirtual 119	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   141: pop
    //   142: ldc 124
    //   144: aload_1
    //   145: invokevirtual 128	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   148: invokestatic 134	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   151: pop
    //   152: goto +135 -> 287
    //   155: astore 9
    //   157: goto +11 -> 168
    //   160: astore 6
    //   162: aload 6
    //   164: astore_3
    //   165: aload 6
    //   167: athrow
    //   168: aload 5
    //   170: astore 6
    //   172: aload 4
    //   174: astore 7
    //   176: aload_3
    //   177: aload 8
    //   179: invokestatic 147	android/graphics/drawable/BitmapDrawable:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   182: aload 5
    //   184: astore 6
    //   186: aload 4
    //   188: astore 7
    //   190: aload 9
    //   192: athrow
    //   193: astore_3
    //   194: aload_0
    //   195: new 8	android/graphics/drawable/BitmapDrawable$BitmapState
    //   198: dup
    //   199: aload 6
    //   201: invokespecial 78	android/graphics/drawable/BitmapDrawable$BitmapState:<init>	(Landroid/graphics/Bitmap;)V
    //   204: aload_1
    //   205: invokespecial 82	android/graphics/drawable/BitmapDrawable:init	(Landroid/graphics/drawable/BitmapDrawable$BitmapState;Landroid/content/res/Resources;)V
    //   208: aload_0
    //   209: getfield 106	android/graphics/drawable/BitmapDrawable:mBitmapState	Landroid/graphics/drawable/BitmapDrawable$BitmapState;
    //   212: getfield 110	android/graphics/drawable/BitmapDrawable$BitmapState:mBitmap	Landroid/graphics/Bitmap;
    //   215: ifnonnull +34 -> 249
    //   218: new 112	java/lang/StringBuilder
    //   221: dup
    //   222: invokespecial 113	java/lang/StringBuilder:<init>	()V
    //   225: astore_1
    //   226: aload_1
    //   227: ldc 115
    //   229: invokevirtual 119	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   232: pop
    //   233: aload_1
    //   234: aload_2
    //   235: invokevirtual 119	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   238: pop
    //   239: ldc 124
    //   241: aload_1
    //   242: invokevirtual 128	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   245: invokestatic 134	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   248: pop
    //   249: aload_3
    //   250: athrow
    //   251: astore_3
    //   252: aload_0
    //   253: new 8	android/graphics/drawable/BitmapDrawable$BitmapState
    //   256: dup
    //   257: aload 7
    //   259: invokespecial 78	android/graphics/drawable/BitmapDrawable$BitmapState:<init>	(Landroid/graphics/Bitmap;)V
    //   262: aload_1
    //   263: invokespecial 82	android/graphics/drawable/BitmapDrawable:init	(Landroid/graphics/drawable/BitmapDrawable$BitmapState;Landroid/content/res/Resources;)V
    //   266: aload_0
    //   267: getfield 106	android/graphics/drawable/BitmapDrawable:mBitmapState	Landroid/graphics/drawable/BitmapDrawable$BitmapState;
    //   270: getfield 110	android/graphics/drawable/BitmapDrawable$BitmapState:mBitmap	Landroid/graphics/Bitmap;
    //   273: ifnonnull +14 -> 287
    //   276: new 112	java/lang/StringBuilder
    //   279: dup
    //   280: invokespecial 113	java/lang/StringBuilder:<init>	()V
    //   283: astore_1
    //   284: goto -155 -> 129
    //   287: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	288	0	this	BitmapDrawable
    //   0	288	1	paramResources	Resources
    //   0	288	2	paramString	String
    //   35	142	3	localObject1	Object
    //   193	57	3	localObject2	Object
    //   251	1	3	localException	Exception
    //   37	150	4	localObject3	Object
    //   40	143	5	localObject4	Object
    //   44	44	6	localObject5	Object
    //   160	6	6	localThrowable	Throwable
    //   170	30	6	localObject6	Object
    //   48	210	7	localObject7	Object
    //   53	125	8	localFileInputStream	java.io.FileInputStream
    //   155	36	9	localObject8	Object
    // Exception table:
    //   from	to	target	type
    //   69	83	155	finally
    //   165	168	155	finally
    //   69	83	160	java/lang/Throwable
    //   50	55	193	finally
    //   63	69	193	finally
    //   92	98	193	finally
    //   176	182	193	finally
    //   190	193	193	finally
    //   50	55	251	java/lang/Exception
    //   63	69	251	java/lang/Exception
    //   92	98	251	java/lang/Exception
    //   176	182	251	java/lang/Exception
    //   190	193	251	java/lang/Exception
  }
  
  @Deprecated
  public BitmapDrawable(Bitmap paramBitmap)
  {
    init(new BitmapState(paramBitmap), null);
  }
  
  private BitmapDrawable(BitmapState paramBitmapState, Resources paramResources)
  {
    init(paramBitmapState, paramResources);
  }
  
  @Deprecated
  public BitmapDrawable(InputStream paramInputStream)
  {
    this(null, paramInputStream);
  }
  
  @Deprecated
  public BitmapDrawable(String paramString)
  {
    this(null, paramString);
  }
  
  private void computeBitmapSize()
  {
    Bitmap localBitmap = mBitmapState.mBitmap;
    if (localBitmap != null)
    {
      mBitmapWidth = localBitmap.getScaledWidth(mTargetDensity);
      mBitmapHeight = localBitmap.getScaledHeight(mTargetDensity);
    }
    else
    {
      mBitmapHeight = -1;
      mBitmapWidth = -1;
    }
  }
  
  private Matrix getOrCreateMirrorMatrix()
  {
    if (mMirrorMatrix == null) {
      mMirrorMatrix = new Matrix();
    }
    return mMirrorMatrix;
  }
  
  private void init(BitmapState paramBitmapState, Resources paramResources)
  {
    mBitmapState = paramBitmapState;
    updateLocalState(paramResources);
    if ((mBitmapState != null) && (paramResources != null)) {
      mBitmapState.mTargetDensity = mTargetDensity;
    }
  }
  
  private boolean needMirroring()
  {
    boolean bool1 = isAutoMirrored();
    boolean bool2 = true;
    if ((!bool1) || (getLayoutDirection() != 1)) {
      bool2 = false;
    }
    return bool2;
  }
  
  private static Shader.TileMode parseTileMode(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return null;
    case 2: 
      return Shader.TileMode.MIRROR;
    case 1: 
      return Shader.TileMode.REPEAT;
    }
    return Shader.TileMode.CLAMP;
  }
  
  private void updateDstRectAndInsetsIfDirty()
  {
    if (mDstRectAndInsetsDirty) {
      if ((mBitmapState.mTileModeX == null) && (mBitmapState.mTileModeY == null))
      {
        Rect localRect = getBounds();
        int i = getLayoutDirection();
        Gravity.apply(mBitmapState.mGravity, mBitmapWidth, mBitmapHeight, localRect, mDstRect, i);
        mOpticalInsets = Insets.of(mDstRect.left - left, mDstRect.top - top, right - mDstRect.right, bottom - mDstRect.bottom);
      }
      else
      {
        copyBounds(mDstRect);
        mOpticalInsets = Insets.NONE;
      }
    }
    mDstRectAndInsetsDirty = false;
  }
  
  private void updateLocalState(Resources paramResources)
  {
    mTargetDensity = resolveDensity(paramResources, mBitmapState.mTargetDensity);
    mTintFilter = updateTintFilter(mTintFilter, mBitmapState.mTint, mBitmapState.mTintMode);
    computeBitmapSize();
  }
  
  private void updateShaderMatrix(Bitmap paramBitmap, Paint paramPaint, Shader paramShader, boolean paramBoolean)
  {
    int i = paramBitmap.getDensity();
    int j = mTargetDensity;
    int k;
    if ((i != 0) && (i != j)) {
      k = 1;
    } else {
      k = 0;
    }
    if ((k == 0) && (!paramBoolean))
    {
      mMirrorMatrix = null;
      paramShader.setLocalMatrix(Matrix.IDENTITY_MATRIX);
    }
    else
    {
      paramBitmap = getOrCreateMirrorMatrix();
      paramBitmap.reset();
      if (paramBoolean)
      {
        paramBitmap.setTranslate(mDstRect.right - mDstRect.left, 0.0F);
        paramBitmap.setScale(-1.0F, 1.0F);
      }
      if (k != 0)
      {
        float f = j / i;
        paramBitmap.postScale(f, f);
      }
      paramShader.setLocalMatrix(paramBitmap);
    }
    paramPaint.setShader(paramShader);
  }
  
  /* Error */
  private void updateStateFromTypedArray(TypedArray paramTypedArray, int paramInt)
    throws XmlPullParserException
  {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual 316	android/content/res/TypedArray:getResources	()Landroid/content/res/Resources;
    //   4: astore_3
    //   5: aload_0
    //   6: getfield 106	android/graphics/drawable/BitmapDrawable:mBitmapState	Landroid/graphics/drawable/BitmapDrawable$BitmapState;
    //   9: astore 4
    //   11: aload 4
    //   13: aload 4
    //   15: getfield 319	android/graphics/drawable/BitmapDrawable$BitmapState:mChangingConfigurations	I
    //   18: aload_1
    //   19: invokevirtual 322	android/content/res/TypedArray:getChangingConfigurations	()I
    //   22: ior
    //   23: putfield 319	android/graphics/drawable/BitmapDrawable$BitmapState:mChangingConfigurations	I
    //   26: aload 4
    //   28: aload_1
    //   29: invokevirtual 326	android/content/res/TypedArray:extractThemeAttrs	()[I
    //   32: putfield 330	android/graphics/drawable/BitmapDrawable$BitmapState:mThemeAttrs	[I
    //   35: aload 4
    //   37: iload_2
    //   38: putfield 333	android/graphics/drawable/BitmapDrawable$BitmapState:mSrcDensityOverride	I
    //   41: aload 4
    //   43: aload_3
    //   44: iconst_0
    //   45: invokestatic 334	android/graphics/drawable/Drawable:resolveDensity	(Landroid/content/res/Resources;I)I
    //   48: putfield 178	android/graphics/drawable/BitmapDrawable$BitmapState:mTargetDensity	I
    //   51: aload_1
    //   52: iconst_1
    //   53: iconst_0
    //   54: invokevirtual 338	android/content/res/TypedArray:getResourceId	(II)I
    //   57: istore 5
    //   59: iload 5
    //   61: ifeq +277 -> 338
    //   64: new 340	android/util/TypedValue
    //   67: dup
    //   68: invokespecial 341	android/util/TypedValue:<init>	()V
    //   71: astore 6
    //   73: aload_3
    //   74: iload 5
    //   76: iload_2
    //   77: aload 6
    //   79: iconst_1
    //   80: invokevirtual 347	android/content/res/Resources:getValueForDensity	(IILandroid/util/TypedValue;Z)V
    //   83: iload_2
    //   84: ifle +66 -> 150
    //   87: aload 6
    //   89: getfield 350	android/util/TypedValue:density	I
    //   92: ifle +58 -> 150
    //   95: aload 6
    //   97: getfield 350	android/util/TypedValue:density	I
    //   100: ldc_w 351
    //   103: if_icmpeq +47 -> 150
    //   106: aload 6
    //   108: getfield 350	android/util/TypedValue:density	I
    //   111: iload_2
    //   112: if_icmpne +18 -> 130
    //   115: aload 6
    //   117: aload_3
    //   118: invokevirtual 355	android/content/res/Resources:getDisplayMetrics	()Landroid/util/DisplayMetrics;
    //   121: getfield 360	android/util/DisplayMetrics:densityDpi	I
    //   124: putfield 350	android/util/TypedValue:density	I
    //   127: goto +23 -> 150
    //   130: aload 6
    //   132: aload 6
    //   134: getfield 350	android/util/TypedValue:density	I
    //   137: aload_3
    //   138: invokevirtual 355	android/content/res/Resources:getDisplayMetrics	()Landroid/util/DisplayMetrics;
    //   141: getfield 360	android/util/DisplayMetrics:densityDpi	I
    //   144: imul
    //   145: iload_2
    //   146: idiv
    //   147: putfield 350	android/util/TypedValue:density	I
    //   150: iconst_0
    //   151: istore_2
    //   152: aload 6
    //   154: getfield 350	android/util/TypedValue:density	I
    //   157: ifne +10 -> 167
    //   160: sipush 160
    //   163: istore_2
    //   164: goto +20 -> 184
    //   167: aload 6
    //   169: getfield 350	android/util/TypedValue:density	I
    //   172: ldc_w 351
    //   175: if_icmpeq +9 -> 184
    //   178: aload 6
    //   180: getfield 350	android/util/TypedValue:density	I
    //   183: istore_2
    //   184: aconst_null
    //   185: astore 7
    //   187: aconst_null
    //   188: astore 8
    //   190: aload 8
    //   192: astore 9
    //   194: aload_3
    //   195: iload 5
    //   197: aload 6
    //   199: invokevirtual 364	android/content/res/Resources:openRawResource	(ILandroid/util/TypedValue;)Ljava/io/InputStream;
    //   202: astore 6
    //   204: aload_3
    //   205: aload 6
    //   207: iload_2
    //   208: invokestatic 367	android/graphics/ImageDecoder:createSource	(Landroid/content/res/Resources;Ljava/io/InputStream;I)Landroid/graphics/ImageDecoder$Source;
    //   211: getstatic 372	android/graphics/drawable/_$$Lambda$BitmapDrawable$LMqt8JvxZ4giSOIRAtlCKDg39Jw:INSTANCE	Landroid/graphics/drawable/-$$Lambda$BitmapDrawable$LMqt8JvxZ4giSOIRAtlCKDg39Jw;
    //   214: invokestatic 104	android/graphics/ImageDecoder:decodeBitmap	(Landroid/graphics/ImageDecoder$Source;Landroid/graphics/ImageDecoder$OnHeaderDecodedListener;)Landroid/graphics/Bitmap;
    //   217: astore 9
    //   219: aload 9
    //   221: astore 7
    //   223: aload 6
    //   225: ifnull +13 -> 238
    //   228: aload 7
    //   230: astore 9
    //   232: aconst_null
    //   233: aload 6
    //   235: invokestatic 147	android/graphics/drawable/BitmapDrawable:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   238: aload 7
    //   240: astore 9
    //   242: goto +40 -> 282
    //   245: astore_3
    //   246: goto +12 -> 258
    //   249: astore 9
    //   251: aload 9
    //   253: astore 7
    //   255: aload 9
    //   257: athrow
    //   258: aload 6
    //   260: ifnull +14 -> 274
    //   263: aload 8
    //   265: astore 9
    //   267: aload 7
    //   269: aload 6
    //   271: invokestatic 147	android/graphics/drawable/BitmapDrawable:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   274: aload 8
    //   276: astore 9
    //   278: aload_3
    //   279: athrow
    //   280: astore 7
    //   282: aload 9
    //   284: ifnull +13 -> 297
    //   287: aload 4
    //   289: aload 9
    //   291: putfield 110	android/graphics/drawable/BitmapDrawable$BitmapState:mBitmap	Landroid/graphics/Bitmap;
    //   294: goto +44 -> 338
    //   297: new 112	java/lang/StringBuilder
    //   300: dup
    //   301: invokespecial 113	java/lang/StringBuilder:<init>	()V
    //   304: astore 9
    //   306: aload 9
    //   308: aload_1
    //   309: invokevirtual 375	android/content/res/TypedArray:getPositionDescription	()Ljava/lang/String;
    //   312: invokevirtual 119	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   315: pop
    //   316: aload 9
    //   318: ldc_w 377
    //   321: invokevirtual 119	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   324: pop
    //   325: new 310	org/xmlpull/v1/XmlPullParserException
    //   328: dup
    //   329: aload 9
    //   331: invokevirtual 128	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   334: invokespecial 378	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   337: athrow
    //   338: aload 4
    //   340: getfield 110	android/graphics/drawable/BitmapDrawable$BitmapState:mBitmap	Landroid/graphics/Bitmap;
    //   343: ifnull +16 -> 359
    //   346: aload 4
    //   348: getfield 110	android/graphics/drawable/BitmapDrawable$BitmapState:mBitmap	Landroid/graphics/Bitmap;
    //   351: invokevirtual 381	android/graphics/Bitmap:hasMipMap	()Z
    //   354: istore 10
    //   356: goto +6 -> 362
    //   359: iconst_0
    //   360: istore 10
    //   362: aload_0
    //   363: aload_1
    //   364: bipush 8
    //   366: iload 10
    //   368: invokevirtual 385	android/content/res/TypedArray:getBoolean	(IZ)Z
    //   371: invokevirtual 389	android/graphics/drawable/BitmapDrawable:setMipMap	(Z)V
    //   374: aload 4
    //   376: aload_1
    //   377: bipush 9
    //   379: aload 4
    //   381: getfield 392	android/graphics/drawable/BitmapDrawable$BitmapState:mAutoMirrored	Z
    //   384: invokevirtual 385	android/content/res/TypedArray:getBoolean	(IZ)Z
    //   387: putfield 392	android/graphics/drawable/BitmapDrawable$BitmapState:mAutoMirrored	Z
    //   390: aload 4
    //   392: aload_1
    //   393: bipush 7
    //   395: aload 4
    //   397: getfield 396	android/graphics/drawable/BitmapDrawable$BitmapState:mBaseAlpha	F
    //   400: invokevirtual 400	android/content/res/TypedArray:getFloat	(IF)F
    //   403: putfield 396	android/graphics/drawable/BitmapDrawable$BitmapState:mBaseAlpha	F
    //   406: aload_1
    //   407: bipush 10
    //   409: iconst_m1
    //   410: invokevirtual 403	android/content/res/TypedArray:getInt	(II)I
    //   413: istore_2
    //   414: iload_2
    //   415: iconst_m1
    //   416: if_icmpeq +15 -> 431
    //   419: aload 4
    //   421: iload_2
    //   422: getstatic 408	android/graphics/PorterDuff$Mode:SRC_IN	Landroid/graphics/PorterDuff$Mode;
    //   425: invokestatic 412	android/graphics/drawable/Drawable:parseTintMode	(ILandroid/graphics/PorterDuff$Mode;)Landroid/graphics/PorterDuff$Mode;
    //   428: putfield 263	android/graphics/drawable/BitmapDrawable$BitmapState:mTintMode	Landroid/graphics/PorterDuff$Mode;
    //   431: aload_1
    //   432: iconst_5
    //   433: invokevirtual 416	android/content/res/TypedArray:getColorStateList	(I)Landroid/content/res/ColorStateList;
    //   436: astore 9
    //   438: aload 9
    //   440: ifnull +10 -> 450
    //   443: aload 4
    //   445: aload 9
    //   447: putfield 259	android/graphics/drawable/BitmapDrawable$BitmapState:mTint	Landroid/content/res/ColorStateList;
    //   450: aload_0
    //   451: getfield 106	android/graphics/drawable/BitmapDrawable:mBitmapState	Landroid/graphics/drawable/BitmapDrawable$BitmapState;
    //   454: getfield 420	android/graphics/drawable/BitmapDrawable$BitmapState:mPaint	Landroid/graphics/Paint;
    //   457: astore 9
    //   459: aload 9
    //   461: aload_1
    //   462: iconst_2
    //   463: aload 9
    //   465: invokevirtual 423	android/graphics/Paint:isAntiAlias	()Z
    //   468: invokevirtual 385	android/content/res/TypedArray:getBoolean	(IZ)Z
    //   471: invokevirtual 426	android/graphics/Paint:setAntiAlias	(Z)V
    //   474: aload 9
    //   476: aload_1
    //   477: iconst_3
    //   478: aload 9
    //   480: invokevirtual 429	android/graphics/Paint:isFilterBitmap	()Z
    //   483: invokevirtual 385	android/content/res/TypedArray:getBoolean	(IZ)Z
    //   486: invokevirtual 432	android/graphics/Paint:setFilterBitmap	(Z)V
    //   489: aload 9
    //   491: aload_1
    //   492: iconst_4
    //   493: aload 9
    //   495: invokevirtual 435	android/graphics/Paint:isDither	()Z
    //   498: invokevirtual 385	android/content/res/TypedArray:getBoolean	(IZ)Z
    //   501: invokevirtual 438	android/graphics/Paint:setDither	(Z)V
    //   504: aload_0
    //   505: aload_1
    //   506: iconst_0
    //   507: aload 4
    //   509: getfield 223	android/graphics/drawable/BitmapDrawable$BitmapState:mGravity	I
    //   512: invokevirtual 403	android/content/res/TypedArray:getInt	(II)I
    //   515: invokevirtual 441	android/graphics/drawable/BitmapDrawable:setGravity	(I)V
    //   518: aload_1
    //   519: bipush 6
    //   521: bipush -2
    //   523: invokevirtual 403	android/content/res/TypedArray:getInt	(II)I
    //   526: istore_2
    //   527: iload_2
    //   528: bipush -2
    //   530: if_icmpeq +17 -> 547
    //   533: iload_2
    //   534: invokestatic 443	android/graphics/drawable/BitmapDrawable:parseTileMode	(I)Landroid/graphics/Shader$TileMode;
    //   537: astore 9
    //   539: aload_0
    //   540: aload 9
    //   542: aload 9
    //   544: invokevirtual 447	android/graphics/drawable/BitmapDrawable:setTileModeXY	(Landroid/graphics/Shader$TileMode;Landroid/graphics/Shader$TileMode;)V
    //   547: aload_1
    //   548: bipush 11
    //   550: bipush -2
    //   552: invokevirtual 403	android/content/res/TypedArray:getInt	(II)I
    //   555: istore_2
    //   556: iload_2
    //   557: bipush -2
    //   559: if_icmpeq +11 -> 570
    //   562: aload_0
    //   563: iload_2
    //   564: invokestatic 443	android/graphics/drawable/BitmapDrawable:parseTileMode	(I)Landroid/graphics/Shader$TileMode;
    //   567: invokevirtual 451	android/graphics/drawable/BitmapDrawable:setTileModeX	(Landroid/graphics/Shader$TileMode;)V
    //   570: aload_1
    //   571: bipush 12
    //   573: bipush -2
    //   575: invokevirtual 403	android/content/res/TypedArray:getInt	(II)I
    //   578: istore_2
    //   579: iload_2
    //   580: bipush -2
    //   582: if_icmpeq +11 -> 593
    //   585: aload_0
    //   586: iload_2
    //   587: invokestatic 443	android/graphics/drawable/BitmapDrawable:parseTileMode	(I)Landroid/graphics/Shader$TileMode;
    //   590: invokevirtual 454	android/graphics/drawable/BitmapDrawable:setTileModeY	(Landroid/graphics/Shader$TileMode;)V
    //   593: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	594	0	this	BitmapDrawable
    //   0	594	1	paramTypedArray	TypedArray
    //   0	594	2	paramInt	int
    //   4	201	3	localResources	Resources
    //   245	34	3	localObject1	Object
    //   9	499	4	localBitmapState	BitmapState
    //   57	139	5	i	int
    //   71	199	6	localObject2	Object
    //   185	83	7	localObject3	Object
    //   280	1	7	localException	Exception
    //   188	87	8	localObject4	Object
    //   192	49	9	localObject5	Object
    //   249	7	9	localThrowable	Throwable
    //   265	278	9	localObject6	Object
    //   354	13	10	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   204	219	245	finally
    //   255	258	245	finally
    //   204	219	249	java/lang/Throwable
    //   194	204	280	java/lang/Exception
    //   232	238	280	java/lang/Exception
    //   267	274	280	java/lang/Exception
    //   278	280	280	java/lang/Exception
  }
  
  private void verifyRequiredAttributes(TypedArray paramTypedArray)
    throws XmlPullParserException
  {
    Object localObject = mBitmapState;
    if ((mBitmap == null) && ((mThemeAttrs == null) || (mThemeAttrs[1] == 0)))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramTypedArray.getPositionDescription());
      ((StringBuilder)localObject).append(": <bitmap> requires a valid 'src' attribute");
      throw new XmlPullParserException(((StringBuilder)localObject).toString());
    }
  }
  
  /* Error */
  public void applyTheme(Resources.Theme paramTheme)
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokespecial 461	android/graphics/drawable/Drawable:applyTheme	(Landroid/content/res/Resources$Theme;)V
    //   5: aload_0
    //   6: getfield 106	android/graphics/drawable/BitmapDrawable:mBitmapState	Landroid/graphics/drawable/BitmapDrawable$BitmapState;
    //   9: astore_2
    //   10: aload_2
    //   11: ifnonnull +4 -> 15
    //   14: return
    //   15: aload_2
    //   16: getfield 330	android/graphics/drawable/BitmapDrawable$BitmapState:mThemeAttrs	[I
    //   19: ifnull +51 -> 70
    //   22: aload_1
    //   23: aload_2
    //   24: getfield 330	android/graphics/drawable/BitmapDrawable$BitmapState:mThemeAttrs	[I
    //   27: getstatic 465	com/android/internal/R$styleable:BitmapDrawable	[I
    //   30: invokevirtual 471	android/content/res/Resources$Theme:resolveAttributes	([I[I)Landroid/content/res/TypedArray;
    //   33: astore_3
    //   34: aload_0
    //   35: aload_3
    //   36: aload_2
    //   37: getfield 333	android/graphics/drawable/BitmapDrawable$BitmapState:mSrcDensityOverride	I
    //   40: invokespecial 473	android/graphics/drawable/BitmapDrawable:updateStateFromTypedArray	(Landroid/content/res/TypedArray;I)V
    //   43: aload_3
    //   44: invokevirtual 476	android/content/res/TypedArray:recycle	()V
    //   47: goto +23 -> 70
    //   50: astore_1
    //   51: goto +13 -> 64
    //   54: astore 4
    //   56: aload 4
    //   58: invokestatic 480	android/graphics/drawable/BitmapDrawable:rethrowAsRuntimeException	(Ljava/lang/Exception;)V
    //   61: goto -18 -> 43
    //   64: aload_3
    //   65: invokevirtual 476	android/content/res/TypedArray:recycle	()V
    //   68: aload_1
    //   69: athrow
    //   70: aload_2
    //   71: getfield 259	android/graphics/drawable/BitmapDrawable$BitmapState:mTint	Landroid/content/res/ColorStateList;
    //   74: ifnull +25 -> 99
    //   77: aload_2
    //   78: getfield 259	android/graphics/drawable/BitmapDrawable$BitmapState:mTint	Landroid/content/res/ColorStateList;
    //   81: invokevirtual 485	android/content/res/ColorStateList:canApplyTheme	()Z
    //   84: ifeq +15 -> 99
    //   87: aload_2
    //   88: aload_2
    //   89: getfield 259	android/graphics/drawable/BitmapDrawable$BitmapState:mTint	Landroid/content/res/ColorStateList;
    //   92: aload_1
    //   93: invokevirtual 489	android/content/res/ColorStateList:obtainForTheme	(Landroid/content/res/Resources$Theme;)Landroid/content/res/ColorStateList;
    //   96: putfield 259	android/graphics/drawable/BitmapDrawable$BitmapState:mTint	Landroid/content/res/ColorStateList;
    //   99: aload_0
    //   100: aload_1
    //   101: invokevirtual 490	android/content/res/Resources$Theme:getResources	()Landroid/content/res/Resources;
    //   104: invokespecial 177	android/graphics/drawable/BitmapDrawable:updateLocalState	(Landroid/content/res/Resources;)V
    //   107: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	108	0	this	BitmapDrawable
    //   0	108	1	paramTheme	Resources.Theme
    //   9	80	2	localBitmapState	BitmapState
    //   33	32	3	localTypedArray	TypedArray
    //   54	3	4	localXmlPullParserException	XmlPullParserException
    // Exception table:
    //   from	to	target	type
    //   34	43	50	finally
    //   56	61	50	finally
    //   34	43	54	org/xmlpull/v1/XmlPullParserException
  }
  
  public boolean canApplyTheme()
  {
    boolean bool;
    if ((mBitmapState != null) && (mBitmapState.canApplyTheme())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void clearMutated()
  {
    super.clearMutated();
    mMutated = false;
  }
  
  public void draw(Canvas paramCanvas)
  {
    Bitmap localBitmap = mBitmapState.mBitmap;
    if (localBitmap == null) {
      return;
    }
    BitmapState localBitmapState = mBitmapState;
    Paint localPaint = mPaint;
    boolean bool = mRebuildShader;
    int i = 0;
    if (bool)
    {
      localObject = mTileModeX;
      Shader.TileMode localTileMode = mTileModeY;
      if ((localObject == null) && (localTileMode == null))
      {
        localPaint.setShader(null);
      }
      else
      {
        if (localObject == null) {
          localObject = Shader.TileMode.CLAMP;
        }
        if (localTileMode == null) {
          localTileMode = Shader.TileMode.CLAMP;
        }
        localPaint.setShader(new BitmapShader(localBitmap, (Shader.TileMode)localObject, localTileMode));
      }
      mRebuildShader = false;
    }
    int j;
    if (mBaseAlpha != 1.0F)
    {
      localObject = getPaint();
      j = ((Paint)localObject).getAlpha();
      ((Paint)localObject).setAlpha((int)(j * mBaseAlpha + 0.5F));
    }
    else
    {
      j = -1;
    }
    if ((mTintFilter != null) && (localPaint.getColorFilter() == null))
    {
      localPaint.setColorFilter(mTintFilter);
      i = 1;
    }
    updateDstRectAndInsetsIfDirty();
    Object localObject = localPaint.getShader();
    bool = needMirroring();
    if (localObject == null)
    {
      if (bool)
      {
        paramCanvas.save();
        paramCanvas.translate(mDstRect.right - mDstRect.left, 0.0F);
        paramCanvas.scale(-1.0F, 1.0F);
      }
      paramCanvas.drawBitmap(localBitmap, null, mDstRect, localPaint);
      if (bool) {
        paramCanvas.restore();
      }
    }
    else
    {
      updateShaderMatrix(localBitmap, localPaint, (Shader)localObject, bool);
      paramCanvas.drawRect(mDstRect, localPaint);
    }
    if (i != 0) {
      localPaint.setColorFilter(null);
    }
    if (j >= 0) {
      localPaint.setAlpha(j);
    }
  }
  
  public int getAlpha()
  {
    return mBitmapState.mPaint.getAlpha();
  }
  
  public final Bitmap getBitmap()
  {
    return mBitmapState.mBitmap;
  }
  
  public int getChangingConfigurations()
  {
    return super.getChangingConfigurations() | mBitmapState.getChangingConfigurations();
  }
  
  public ColorFilter getColorFilter()
  {
    return mBitmapState.mPaint.getColorFilter();
  }
  
  public final Drawable.ConstantState getConstantState()
  {
    BitmapState localBitmapState = mBitmapState;
    mChangingConfigurations |= getChangingConfigurations();
    return mBitmapState;
  }
  
  public int getGravity()
  {
    return mBitmapState.mGravity;
  }
  
  public int getIntrinsicHeight()
  {
    return mBitmapHeight;
  }
  
  public int getIntrinsicWidth()
  {
    return mBitmapWidth;
  }
  
  public int getOpacity()
  {
    int i = mBitmapState.mGravity;
    int j = -3;
    if (i != 119) {
      return -3;
    }
    Bitmap localBitmap = mBitmapState.mBitmap;
    i = j;
    if (localBitmap != null)
    {
      i = j;
      if (!localBitmap.hasAlpha()) {
        if (mBitmapState.mPaint.getAlpha() < 255) {
          i = j;
        } else {
          i = -1;
        }
      }
    }
    return i;
  }
  
  public Insets getOpticalInsets()
  {
    updateDstRectAndInsetsIfDirty();
    return mOpticalInsets;
  }
  
  public void getOutline(Outline paramOutline)
  {
    updateDstRectAndInsetsIfDirty();
    paramOutline.setRect(mDstRect);
    int i;
    if ((mBitmapState.mBitmap != null) && (!mBitmapState.mBitmap.hasAlpha())) {
      i = 1;
    } else {
      i = 0;
    }
    float f;
    if (i != 0) {
      f = getAlpha() / 255.0F;
    } else {
      f = 0.0F;
    }
    paramOutline.setAlpha(f);
  }
  
  public final Paint getPaint()
  {
    return mBitmapState.mPaint;
  }
  
  public Shader.TileMode getTileModeX()
  {
    return mBitmapState.mTileModeX;
  }
  
  public Shader.TileMode getTileModeY()
  {
    return mBitmapState.mTileModeY;
  }
  
  public ColorStateList getTint()
  {
    return mBitmapState.mTint;
  }
  
  public PorterDuff.Mode getTintMode()
  {
    return mBitmapState.mTintMode;
  }
  
  public boolean hasAntiAlias()
  {
    return mBitmapState.mPaint.isAntiAlias();
  }
  
  public boolean hasFocusStateSpecified()
  {
    boolean bool;
    if ((mBitmapState.mTint != null) && (mBitmapState.mTint.hasFocusStateSpecified())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasMipMap()
  {
    boolean bool;
    if ((mBitmapState.mBitmap != null) && (mBitmapState.mBitmap.hasMipMap())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    super.inflate(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
    paramXmlPullParser = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.BitmapDrawable);
    updateStateFromTypedArray(paramXmlPullParser, mSrcDensityOverride);
    verifyRequiredAttributes(paramXmlPullParser);
    paramXmlPullParser.recycle();
    updateLocalState(paramResources);
  }
  
  public final boolean isAutoMirrored()
  {
    return mBitmapState.mAutoMirrored;
  }
  
  public boolean isFilterBitmap()
  {
    return mBitmapState.mPaint.isFilterBitmap();
  }
  
  public boolean isStateful()
  {
    boolean bool;
    if (((mBitmapState.mTint != null) && (mBitmapState.mTint.isStateful())) || (super.isStateful())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public Drawable mutate()
  {
    if ((!mMutated) && (super.mutate() == this))
    {
      mBitmapState = new BitmapState(mBitmapState);
      mMutated = true;
    }
    return this;
  }
  
  protected void onBoundsChange(Rect paramRect)
  {
    mDstRectAndInsetsDirty = true;
    Bitmap localBitmap = mBitmapState.mBitmap;
    paramRect = mBitmapState.mPaint.getShader();
    if ((localBitmap != null) && (paramRect != null)) {
      updateShaderMatrix(localBitmap, mBitmapState.mPaint, paramRect, needMirroring());
    }
  }
  
  protected boolean onStateChange(int[] paramArrayOfInt)
  {
    paramArrayOfInt = mBitmapState;
    if ((mTint != null) && (mTintMode != null))
    {
      mTintFilter = updateTintFilter(mTintFilter, mTint, mTintMode);
      return true;
    }
    return false;
  }
  
  public void setAlpha(int paramInt)
  {
    if (paramInt != mBitmapState.mPaint.getAlpha())
    {
      mBitmapState.mPaint.setAlpha(paramInt);
      invalidateSelf();
    }
  }
  
  public void setAntiAlias(boolean paramBoolean)
  {
    mBitmapState.mPaint.setAntiAlias(paramBoolean);
    invalidateSelf();
  }
  
  public void setAutoMirrored(boolean paramBoolean)
  {
    if (mBitmapState.mAutoMirrored != paramBoolean)
    {
      mBitmapState.mAutoMirrored = paramBoolean;
      invalidateSelf();
    }
  }
  
  public void setBitmap(Bitmap paramBitmap)
  {
    if (mBitmapState.mBitmap != paramBitmap)
    {
      mBitmapState.mBitmap = paramBitmap;
      computeBitmapSize();
      invalidateSelf();
    }
  }
  
  public void setColorFilter(ColorFilter paramColorFilter)
  {
    mBitmapState.mPaint.setColorFilter(paramColorFilter);
    invalidateSelf();
  }
  
  public void setDither(boolean paramBoolean)
  {
    mBitmapState.mPaint.setDither(paramBoolean);
    invalidateSelf();
  }
  
  public void setFilterBitmap(boolean paramBoolean)
  {
    mBitmapState.mPaint.setFilterBitmap(paramBoolean);
    invalidateSelf();
  }
  
  public void setGravity(int paramInt)
  {
    if (mBitmapState.mGravity != paramInt)
    {
      mBitmapState.mGravity = paramInt;
      mDstRectAndInsetsDirty = true;
      invalidateSelf();
    }
  }
  
  public void setMipMap(boolean paramBoolean)
  {
    if (mBitmapState.mBitmap != null)
    {
      mBitmapState.mBitmap.setHasMipMap(paramBoolean);
      invalidateSelf();
    }
  }
  
  public void setTargetDensity(int paramInt)
  {
    if (mTargetDensity != paramInt)
    {
      if (paramInt == 0) {
        paramInt = 160;
      }
      mTargetDensity = paramInt;
      if (mBitmapState.mBitmap != null) {
        computeBitmapSize();
      }
      invalidateSelf();
    }
  }
  
  public void setTargetDensity(Canvas paramCanvas)
  {
    setTargetDensity(paramCanvas.getDensity());
  }
  
  public void setTargetDensity(DisplayMetrics paramDisplayMetrics)
  {
    setTargetDensity(densityDpi);
  }
  
  public void setTileModeX(Shader.TileMode paramTileMode)
  {
    setTileModeXY(paramTileMode, mBitmapState.mTileModeY);
  }
  
  public void setTileModeXY(Shader.TileMode paramTileMode1, Shader.TileMode paramTileMode2)
  {
    BitmapState localBitmapState = mBitmapState;
    if ((mTileModeX != paramTileMode1) || (mTileModeY != paramTileMode2))
    {
      mTileModeX = paramTileMode1;
      mTileModeY = paramTileMode2;
      mRebuildShader = true;
      mDstRectAndInsetsDirty = true;
      invalidateSelf();
    }
  }
  
  public final void setTileModeY(Shader.TileMode paramTileMode)
  {
    setTileModeXY(mBitmapState.mTileModeX, paramTileMode);
  }
  
  public void setTintList(ColorStateList paramColorStateList)
  {
    BitmapState localBitmapState = mBitmapState;
    if (mTint != paramColorStateList)
    {
      mTint = paramColorStateList;
      mTintFilter = updateTintFilter(mTintFilter, paramColorStateList, mBitmapState.mTintMode);
      invalidateSelf();
    }
  }
  
  public void setTintMode(PorterDuff.Mode paramMode)
  {
    BitmapState localBitmapState = mBitmapState;
    if (mTintMode != paramMode)
    {
      mTintMode = paramMode;
      mTintFilter = updateTintFilter(mTintFilter, mBitmapState.mTint, paramMode);
      invalidateSelf();
    }
  }
  
  public void setXfermode(Xfermode paramXfermode)
  {
    mBitmapState.mPaint.setXfermode(paramXfermode);
    invalidateSelf();
  }
  
  static final class BitmapState
    extends Drawable.ConstantState
  {
    boolean mAutoMirrored = false;
    float mBaseAlpha = 1.0F;
    Bitmap mBitmap = null;
    int mChangingConfigurations;
    int mGravity = 119;
    final Paint mPaint;
    boolean mRebuildShader;
    int mSrcDensityOverride = 0;
    int mTargetDensity = 160;
    int[] mThemeAttrs = null;
    Shader.TileMode mTileModeX = null;
    Shader.TileMode mTileModeY = null;
    ColorStateList mTint = null;
    PorterDuff.Mode mTintMode = Drawable.DEFAULT_TINT_MODE;
    
    BitmapState(Bitmap paramBitmap)
    {
      mBitmap = paramBitmap;
      mPaint = new Paint(6);
    }
    
    BitmapState(BitmapState paramBitmapState)
    {
      mBitmap = mBitmap;
      mTint = mTint;
      mTintMode = mTintMode;
      mThemeAttrs = mThemeAttrs;
      mChangingConfigurations = mChangingConfigurations;
      mGravity = mGravity;
      mTileModeX = mTileModeX;
      mTileModeY = mTileModeY;
      mSrcDensityOverride = mSrcDensityOverride;
      mTargetDensity = mTargetDensity;
      mBaseAlpha = mBaseAlpha;
      mPaint = new Paint(mPaint);
      mRebuildShader = mRebuildShader;
      mAutoMirrored = mAutoMirrored;
    }
    
    public boolean canApplyTheme()
    {
      boolean bool;
      if ((mThemeAttrs == null) && ((mTint == null) || (!mTint.canApplyTheme()))) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public int getChangingConfigurations()
    {
      int i = mChangingConfigurations;
      int j;
      if (mTint != null) {
        j = mTint.getChangingConfigurations();
      } else {
        j = 0;
      }
      return i | j;
    }
    
    public Drawable newDrawable()
    {
      return new BitmapDrawable(this, null, null);
    }
    
    public Drawable newDrawable(Resources paramResources)
    {
      return new BitmapDrawable(this, paramResources, null);
    }
  }
}
