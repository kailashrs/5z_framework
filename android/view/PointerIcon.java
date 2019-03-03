package android.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.R.styleable;

public final class PointerIcon
  implements Parcelable
{
  public static final Parcelable.Creator<PointerIcon> CREATOR = new Parcelable.Creator()
  {
    public PointerIcon createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      if (i == 0) {
        return PointerIcon.getNullIcon();
      }
      int j = paramAnonymousParcel.readInt();
      if (j != 0)
      {
        paramAnonymousParcel = new PointerIcon(i, null);
        PointerIcon.access$102(paramAnonymousParcel, j);
        return paramAnonymousParcel;
      }
      return PointerIcon.create((Bitmap)Bitmap.CREATOR.createFromParcel(paramAnonymousParcel), paramAnonymousParcel.readFloat(), paramAnonymousParcel.readFloat());
    }
    
    public PointerIcon[] newArray(int paramAnonymousInt)
    {
      return new PointerIcon[paramAnonymousInt];
    }
  };
  private static final String TAG = "PointerIcon";
  public static final int TYPE_ALIAS = 1010;
  public static final int TYPE_ALL_SCROLL = 1013;
  public static final int TYPE_ARROW = 1000;
  public static final int TYPE_CELL = 1006;
  public static final int TYPE_CONTEXT_MENU = 1001;
  public static final int TYPE_COPY = 1011;
  public static final int TYPE_CROSSHAIR = 1007;
  public static final int TYPE_CUSTOM = -1;
  public static final int TYPE_DEFAULT = 1000;
  public static final int TYPE_GRAB = 1020;
  public static final int TYPE_GRABBING = 1021;
  public static final int TYPE_HAND = 1002;
  public static final int TYPE_HELP = 1003;
  public static final int TYPE_HORIZONTAL_DOUBLE_ARROW = 1014;
  public static final int TYPE_NOT_SPECIFIED = 1;
  public static final int TYPE_NO_DROP = 1012;
  public static final int TYPE_NULL = 0;
  private static final int TYPE_OEM_FIRST = 10000;
  public static final int TYPE_SPOT_ANCHOR = 2002;
  public static final int TYPE_SPOT_HOVER = 2000;
  public static final int TYPE_SPOT_TOUCH = 2001;
  public static final int TYPE_TEXT = 1008;
  public static final int TYPE_TOP_LEFT_DIAGONAL_DOUBLE_ARROW = 1017;
  public static final int TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW = 1016;
  public static final int TYPE_VERTICAL_DOUBLE_ARROW = 1015;
  public static final int TYPE_VERTICAL_TEXT = 1009;
  public static final int TYPE_WAIT = 1004;
  public static final int TYPE_ZOOM_IN = 1018;
  public static final int TYPE_ZOOM_OUT = 1019;
  private static final PointerIcon gNullIcon = new PointerIcon(0);
  private static final SparseArray<PointerIcon> gSystemIcons = new SparseArray();
  private static boolean sUseLargeIcons = false;
  private Bitmap mBitmap;
  private Bitmap[] mBitmapFrames;
  private int mDurationPerFrame;
  private float mHotSpotX;
  private float mHotSpotY;
  private int mSystemIconResourceId;
  private final int mType;
  
  private PointerIcon(int paramInt)
  {
    mType = paramInt;
  }
  
  public static PointerIcon create(Bitmap paramBitmap, float paramFloat1, float paramFloat2)
  {
    if (paramBitmap != null)
    {
      validateHotSpot(paramBitmap, paramFloat1, paramFloat2);
      PointerIcon localPointerIcon = new PointerIcon(-1);
      mBitmap = paramBitmap;
      mHotSpotX = paramFloat1;
      mHotSpotY = paramFloat2;
      return localPointerIcon;
    }
    throw new IllegalArgumentException("bitmap must not be null");
  }
  
  private Bitmap getBitmapFromDrawable(BitmapDrawable paramBitmapDrawable)
  {
    Bitmap localBitmap1 = paramBitmapDrawable.getBitmap();
    int i = paramBitmapDrawable.getIntrinsicWidth();
    int j = paramBitmapDrawable.getIntrinsicHeight();
    if ((i == localBitmap1.getWidth()) && (j == localBitmap1.getHeight())) {
      return localBitmap1;
    }
    Rect localRect = new Rect(0, 0, localBitmap1.getWidth(), localBitmap1.getHeight());
    paramBitmapDrawable = new RectF(0.0F, 0.0F, i, j);
    Bitmap localBitmap2 = Bitmap.createBitmap(i, j, localBitmap1.getConfig());
    Canvas localCanvas = new Canvas(localBitmap2);
    Paint localPaint = new Paint();
    localPaint.setFilterBitmap(true);
    localCanvas.drawBitmap(localBitmap1, localRect, paramBitmapDrawable, localPaint);
    return localBitmap2;
  }
  
  public static PointerIcon getDefaultIcon(Context paramContext)
  {
    return getSystemIcon(paramContext, 1000);
  }
  
  public static PointerIcon getNullIcon()
  {
    return gNullIcon;
  }
  
  public static PointerIcon getSystemIcon(Context paramContext, int paramInt)
  {
    if (paramContext != null)
    {
      if (paramInt == 0) {
        return gNullIcon;
      }
      Object localObject = (PointerIcon)gSystemIcons.get(paramInt);
      if (localObject != null) {
        return localObject;
      }
      int i = getSystemIconTypeIndex(paramInt);
      int j = i;
      if (i == 0) {
        j = getSystemIconTypeIndex(1000);
      }
      if (sUseLargeIcons) {
        i = 16974800;
      } else {
        i = 16974813;
      }
      localObject = paramContext.obtainStyledAttributes(null, R.styleable.Pointer, 0, i);
      j = ((TypedArray)localObject).getResourceId(j, -1);
      ((TypedArray)localObject).recycle();
      if (j == -1)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Missing theme resources for pointer icon type ");
        ((StringBuilder)localObject).append(paramInt);
        Log.w("PointerIcon", ((StringBuilder)localObject).toString());
        if (paramInt == 1000) {
          paramContext = gNullIcon;
        } else {
          paramContext = getSystemIcon(paramContext, 1000);
        }
        return paramContext;
      }
      localObject = new PointerIcon(paramInt);
      if ((0xFF000000 & j) == 16777216) {
        mSystemIconResourceId = j;
      } else {
        ((PointerIcon)localObject).loadResource(paramContext, paramContext.getResources(), j);
      }
      gSystemIcons.append(paramInt, localObject);
      return localObject;
    }
    throw new IllegalArgumentException("context must not be null");
  }
  
  private static int getSystemIconTypeIndex(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      switch (paramInt)
      {
      default: 
        switch (paramInt)
        {
        default: 
          return 0;
        case 2002: 
          return 13;
        case 2001: 
          return 15;
        }
        return 14;
      case 1021: 
        return 8;
      case 1020: 
        return 7;
      case 1019: 
        return 23;
      case 1018: 
        return 22;
      case 1017: 
        return 17;
      case 1016: 
        return 18;
      case 1015: 
        return 19;
      case 1014: 
        return 11;
      case 1013: 
        return 1;
      case 1012: 
        return 12;
      case 1011: 
        return 5;
      case 1010: 
        return 0;
      case 1009: 
        return 20;
      case 1008: 
        return 16;
      case 1007: 
        return 6;
      }
      return 3;
    case 1004: 
      return 21;
    case 1003: 
      return 10;
    case 1002: 
      return 9;
    case 1001: 
      return 4;
    }
    return 2;
  }
  
  public static PointerIcon load(Resources paramResources, int paramInt)
  {
    if (paramResources != null)
    {
      PointerIcon localPointerIcon = new PointerIcon(-1);
      localPointerIcon.loadResource(null, paramResources, paramInt);
      return localPointerIcon;
    }
    throw new IllegalArgumentException("resources must not be null");
  }
  
  /* Error */
  private void loadResource(Context paramContext, Resources paramResources, int paramInt)
  {
    // Byte code:
    //   0: aload_2
    //   1: iload_3
    //   2: invokevirtual 282	android/content/res/Resources:getXml	(I)Landroid/content/res/XmlResourceParser;
    //   5: astore 4
    //   7: aload 4
    //   9: ldc_w 284
    //   12: invokestatic 290	com/android/internal/util/XmlUtils:beginDocument	(Lorg/xmlpull/v1/XmlPullParser;Ljava/lang/String;)V
    //   15: aload_2
    //   16: aload 4
    //   18: getstatic 292	com/android/internal/R$styleable:PointerIcon	[I
    //   21: invokevirtual 296	android/content/res/Resources:obtainAttributes	(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
    //   24: astore 5
    //   26: aload 5
    //   28: iconst_0
    //   29: iconst_0
    //   30: invokevirtual 230	android/content/res/TypedArray:getResourceId	(II)I
    //   33: istore 6
    //   35: iconst_1
    //   36: istore_3
    //   37: aload 5
    //   39: iconst_1
    //   40: fconst_0
    //   41: invokevirtual 300	android/content/res/TypedArray:getDimension	(IF)F
    //   44: fstore 7
    //   46: aload 5
    //   48: iconst_2
    //   49: fconst_0
    //   50: invokevirtual 300	android/content/res/TypedArray:getDimension	(IF)F
    //   53: fstore 8
    //   55: aload 5
    //   57: invokevirtual 233	android/content/res/TypedArray:recycle	()V
    //   60: aload 4
    //   62: invokeinterface 305 1 0
    //   67: iload 6
    //   69: ifeq +274 -> 343
    //   72: aload_1
    //   73: ifnonnull +13 -> 86
    //   76: aload_2
    //   77: iload 6
    //   79: invokevirtual 309	android/content/res/Resources:getDrawable	(I)Landroid/graphics/drawable/Drawable;
    //   82: astore_1
    //   83: goto +10 -> 93
    //   86: aload_1
    //   87: iload 6
    //   89: invokevirtual 310	android/content/Context:getDrawable	(I)Landroid/graphics/drawable/Drawable;
    //   92: astore_1
    //   93: aload_1
    //   94: astore_2
    //   95: aload_1
    //   96: instanceof 312
    //   99: ifeq +191 -> 290
    //   102: aload_1
    //   103: checkcast 312	android/graphics/drawable/AnimationDrawable
    //   106: astore_1
    //   107: aload_1
    //   108: invokevirtual 315	android/graphics/drawable/AnimationDrawable:getNumberOfFrames	()I
    //   111: istore 9
    //   113: aload_1
    //   114: iconst_0
    //   115: invokevirtual 318	android/graphics/drawable/AnimationDrawable:getFrame	(I)Landroid/graphics/drawable/Drawable;
    //   118: astore_2
    //   119: iload 9
    //   121: iconst_1
    //   122: if_icmpne +15 -> 137
    //   125: ldc 15
    //   127: ldc_w 320
    //   130: invokestatic 255	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   133: pop
    //   134: goto +156 -> 290
    //   137: aload_0
    //   138: aload_1
    //   139: iconst_0
    //   140: invokevirtual 323	android/graphics/drawable/AnimationDrawable:getDuration	(I)I
    //   143: putfield 325	android/view/PointerIcon:mDurationPerFrame	I
    //   146: aload_0
    //   147: iload 9
    //   149: iconst_1
    //   150: isub
    //   151: anewarray 154	android/graphics/Bitmap
    //   154: putfield 327	android/view/PointerIcon:mBitmapFrames	[Landroid/graphics/Bitmap;
    //   157: aload_2
    //   158: invokevirtual 330	android/graphics/drawable/Drawable:getIntrinsicWidth	()I
    //   161: istore 10
    //   163: aload_2
    //   164: invokevirtual 331	android/graphics/drawable/Drawable:getIntrinsicHeight	()I
    //   167: istore 6
    //   169: iload_3
    //   170: iload 9
    //   172: if_icmpge +118 -> 290
    //   175: aload_1
    //   176: iload_3
    //   177: invokevirtual 318	android/graphics/drawable/AnimationDrawable:getFrame	(I)Landroid/graphics/drawable/Drawable;
    //   180: astore 4
    //   182: aload 4
    //   184: instanceof 141
    //   187: ifeq +92 -> 279
    //   190: aload 4
    //   192: invokevirtual 330	android/graphics/drawable/Drawable:getIntrinsicWidth	()I
    //   195: iload 10
    //   197: if_icmpne +40 -> 237
    //   200: aload 4
    //   202: invokevirtual 331	android/graphics/drawable/Drawable:getIntrinsicHeight	()I
    //   205: iload 6
    //   207: if_icmpne +30 -> 237
    //   210: aload 4
    //   212: checkcast 141	android/graphics/drawable/BitmapDrawable
    //   215: astore 4
    //   217: aload_0
    //   218: getfield 327	android/view/PointerIcon:mBitmapFrames	[Landroid/graphics/Bitmap;
    //   221: iload_3
    //   222: iconst_1
    //   223: isub
    //   224: aload_0
    //   225: aload 4
    //   227: invokespecial 333	android/view/PointerIcon:getBitmapFromDrawable	(Landroid/graphics/drawable/BitmapDrawable;)Landroid/graphics/Bitmap;
    //   230: aastore
    //   231: iinc 3 1
    //   234: goto -65 -> 169
    //   237: new 235	java/lang/StringBuilder
    //   240: dup
    //   241: invokespecial 236	java/lang/StringBuilder:<init>	()V
    //   244: astore_1
    //   245: aload_1
    //   246: ldc_w 335
    //   249: invokevirtual 242	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   252: pop
    //   253: aload_1
    //   254: iload_3
    //   255: invokevirtual 245	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   258: pop
    //   259: aload_1
    //   260: ldc_w 337
    //   263: invokevirtual 242	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   266: pop
    //   267: new 132	java/lang/IllegalArgumentException
    //   270: dup
    //   271: aload_1
    //   272: invokevirtual 249	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   275: invokespecial 137	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   278: athrow
    //   279: new 132	java/lang/IllegalArgumentException
    //   282: dup
    //   283: ldc_w 339
    //   286: invokespecial 137	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   289: athrow
    //   290: aload_2
    //   291: instanceof 141
    //   294: ifeq +38 -> 332
    //   297: aload_0
    //   298: aload_2
    //   299: checkcast 141	android/graphics/drawable/BitmapDrawable
    //   302: invokespecial 333	android/view/PointerIcon:getBitmapFromDrawable	(Landroid/graphics/drawable/BitmapDrawable;)Landroid/graphics/Bitmap;
    //   305: astore_1
    //   306: aload_1
    //   307: fload 7
    //   309: fload 8
    //   311: invokestatic 124	android/view/PointerIcon:validateHotSpot	(Landroid/graphics/Bitmap;FF)V
    //   314: aload_0
    //   315: aload_1
    //   316: putfield 126	android/view/PointerIcon:mBitmap	Landroid/graphics/Bitmap;
    //   319: aload_0
    //   320: fload 7
    //   322: putfield 128	android/view/PointerIcon:mHotSpotX	F
    //   325: aload_0
    //   326: fload 8
    //   328: putfield 130	android/view/PointerIcon:mHotSpotY	F
    //   331: return
    //   332: new 132	java/lang/IllegalArgumentException
    //   335: dup
    //   336: ldc_w 341
    //   339: invokespecial 137	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   342: athrow
    //   343: new 132	java/lang/IllegalArgumentException
    //   346: dup
    //   347: ldc_w 343
    //   350: invokespecial 137	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   353: athrow
    //   354: astore_1
    //   355: goto +18 -> 373
    //   358: astore_1
    //   359: new 132	java/lang/IllegalArgumentException
    //   362: astore_2
    //   363: aload_2
    //   364: ldc_w 345
    //   367: aload_1
    //   368: invokespecial 348	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   371: aload_2
    //   372: athrow
    //   373: aload 4
    //   375: invokeinterface 305 1 0
    //   380: aload_1
    //   381: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	382	0	this	PointerIcon
    //   0	382	1	paramContext	Context
    //   0	382	2	paramResources	Resources
    //   0	382	3	paramInt	int
    //   5	369	4	localObject	Object
    //   24	32	5	localTypedArray	TypedArray
    //   33	175	6	i	int
    //   44	277	7	f1	float
    //   53	274	8	f2	float
    //   111	62	9	j	int
    //   161	37	10	k	int
    // Exception table:
    //   from	to	target	type
    //   7	35	354	finally
    //   37	60	354	finally
    //   359	373	354	finally
    //   7	35	358	java/lang/Exception
    //   37	60	358	java/lang/Exception
  }
  
  public static void setUseLargeIcons(boolean paramBoolean)
  {
    sUseLargeIcons = paramBoolean;
    gSystemIcons.clear();
  }
  
  private static void validateHotSpot(Bitmap paramBitmap, float paramFloat1, float paramFloat2)
  {
    if ((paramFloat1 >= 0.0F) && (paramFloat1 < paramBitmap.getWidth()))
    {
      if ((paramFloat2 >= 0.0F) && (paramFloat2 < paramBitmap.getHeight())) {
        return;
      }
      throw new IllegalArgumentException("y hotspot lies outside of the bitmap area");
    }
    throw new IllegalArgumentException("x hotspot lies outside of the bitmap area");
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && ((paramObject instanceof PointerIcon)))
    {
      paramObject = (PointerIcon)paramObject;
      if ((mType == mType) && (mSystemIconResourceId == mSystemIconResourceId)) {
        return (mSystemIconResourceId != 0) || ((mBitmap == mBitmap) && (mHotSpotX == mHotSpotX) && (mHotSpotY == mHotSpotY));
      }
      return false;
    }
    return false;
  }
  
  public int getType()
  {
    return mType;
  }
  
  public PointerIcon load(Context paramContext)
  {
    if (paramContext != null)
    {
      if ((mSystemIconResourceId != 0) && (mBitmap == null))
      {
        PointerIcon localPointerIcon = new PointerIcon(mType);
        mSystemIconResourceId = mSystemIconResourceId;
        localPointerIcon.loadResource(paramContext, paramContext.getResources(), mSystemIconResourceId);
        return localPointerIcon;
      }
      return this;
    }
    throw new IllegalArgumentException("context must not be null");
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mType);
    if (mType != 0)
    {
      paramParcel.writeInt(mSystemIconResourceId);
      if (mSystemIconResourceId == 0)
      {
        mBitmap.writeToParcel(paramParcel, paramInt);
        paramParcel.writeFloat(mHotSpotX);
        paramParcel.writeFloat(mHotSpotY);
      }
    }
  }
}
