package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.ComplexColor;
import android.content.res.GradientColor;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Insets;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Trace;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.FloatProperty;
import android.util.IntProperty;
import android.util.Log;
import android.util.PathParser.PathData;
import android.util.Property;
import android.util.Xml;
import com.android.internal.R.styleable;
import com.android.internal.util.VirtualRefBasePtr;
import dalvik.annotation.optimization.FastNative;
import dalvik.system.VMRuntime;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class VectorDrawable
  extends Drawable
{
  private static final String LOGTAG = VectorDrawable.class.getSimpleName();
  private static final String SHAPE_CLIP_PATH = "clip-path";
  private static final String SHAPE_GROUP = "group";
  private static final String SHAPE_PATH = "path";
  private static final String SHAPE_VECTOR = "vector";
  private ColorFilter mColorFilter;
  private boolean mDpiScaledDirty = true;
  private int mDpiScaledHeight = 0;
  private Insets mDpiScaledInsets = Insets.NONE;
  private int mDpiScaledWidth = 0;
  private boolean mMutated;
  private int mTargetDensity;
  private PorterDuffColorFilter mTintFilter;
  private final Rect mTmpBounds = new Rect();
  private VectorDrawableState mVectorState;
  
  public VectorDrawable()
  {
    this(new VectorDrawableState(null), null);
  }
  
  private VectorDrawable(VectorDrawableState paramVectorDrawableState, Resources paramResources)
  {
    mVectorState = paramVectorDrawableState;
    updateLocalState(paramResources);
  }
  
  public static VectorDrawable create(Resources paramResources, int paramInt)
  {
    try
    {
      XmlResourceParser localXmlResourceParser = paramResources.getXml(paramInt);
      AttributeSet localAttributeSet = Xml.asAttributeSet(localXmlResourceParser);
      do
      {
        paramInt = localXmlResourceParser.next();
      } while ((paramInt != 2) && (paramInt != 1));
      if (paramInt == 2)
      {
        VectorDrawable localVectorDrawable = new android/graphics/drawable/VectorDrawable;
        localVectorDrawable.<init>();
        localVectorDrawable.inflate(paramResources, localXmlResourceParser, localAttributeSet);
        return localVectorDrawable;
      }
      paramResources = new org/xmlpull/v1/XmlPullParserException;
      paramResources.<init>("No start tag found");
      throw paramResources;
    }
    catch (IOException paramResources)
    {
      Log.e(LOGTAG, "parser error", paramResources);
    }
    catch (XmlPullParserException paramResources)
    {
      Log.e(LOGTAG, "parser error", paramResources);
    }
    return null;
  }
  
  private void inflateChildElements(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    VectorDrawableState localVectorDrawableState = mVectorState;
    int i = 1;
    Stack localStack = new Stack();
    localStack.push(mRootGroup);
    int j = paramXmlPullParser.getEventType();
    int k = paramXmlPullParser.getDepth();
    while ((j != 1) && ((paramXmlPullParser.getDepth() >= k + 1) || (j != 3)))
    {
      int m;
      if (j == 2)
      {
        Object localObject = paramXmlPullParser.getName();
        VGroup localVGroup = (VGroup)localStack.peek();
        if ("path".equals(localObject))
        {
          localObject = new VFullPath();
          ((VFullPath)localObject).inflate(paramResources, paramAttributeSet, paramTheme);
          localVGroup.addChild((VObject)localObject);
          if (((VFullPath)localObject).getPathName() != null) {
            mVGTargetsMap.put(((VFullPath)localObject).getPathName(), localObject);
          }
          m = 0;
          mChangingConfigurations |= mChangingConfigurations;
        }
        else if ("clip-path".equals(localObject))
        {
          localObject = new VClipPath();
          ((VClipPath)localObject).inflate(paramResources, paramAttributeSet, paramTheme);
          localVGroup.addChild((VObject)localObject);
          if (((VClipPath)localObject).getPathName() != null) {
            mVGTargetsMap.put(((VClipPath)localObject).getPathName(), localObject);
          }
          mChangingConfigurations |= mChangingConfigurations;
          m = i;
        }
        else
        {
          m = i;
          if ("group".equals(localObject))
          {
            localObject = new VGroup();
            ((VGroup)localObject).inflate(paramResources, paramAttributeSet, paramTheme);
            localVGroup.addChild((VObject)localObject);
            localStack.push(localObject);
            if (((VGroup)localObject).getGroupName() != null) {
              mVGTargetsMap.put(((VGroup)localObject).getGroupName(), localObject);
            }
            mChangingConfigurations |= mChangingConfigurations;
            m = i;
          }
        }
      }
      else
      {
        m = i;
        if (j == 3)
        {
          m = i;
          if ("group".equals(paramXmlPullParser.getName()))
          {
            localStack.pop();
            m = i;
          }
        }
      }
      j = paramXmlPullParser.next();
      i = m;
    }
    if (i != 0)
    {
      paramXmlPullParser = new StringBuffer();
      if (paramXmlPullParser.length() > 0) {
        paramXmlPullParser.append(" or ");
      }
      paramXmlPullParser.append("path");
      paramResources = new StringBuilder();
      paramResources.append("no ");
      paramResources.append(paramXmlPullParser);
      paramResources.append(" defined");
      throw new XmlPullParserException(paramResources.toString());
    }
  }
  
  @FastNative
  private static native void nAddChild(long paramLong1, long paramLong2);
  
  @FastNative
  private static native long nCreateClipPath();
  
  @FastNative
  private static native long nCreateClipPath(long paramLong);
  
  @FastNative
  private static native long nCreateFullPath();
  
  @FastNative
  private static native long nCreateFullPath(long paramLong);
  
  @FastNative
  private static native long nCreateGroup();
  
  @FastNative
  private static native long nCreateGroup(long paramLong);
  
  @FastNative
  private static native long nCreateTree(long paramLong);
  
  @FastNative
  private static native long nCreateTreeFromCopy(long paramLong1, long paramLong2);
  
  private static native int nDraw(long paramLong1, long paramLong2, long paramLong3, Rect paramRect, boolean paramBoolean1, boolean paramBoolean2);
  
  @FastNative
  private static native float nGetFillAlpha(long paramLong);
  
  @FastNative
  private static native int nGetFillColor(long paramLong);
  
  private static native boolean nGetFullPathProperties(long paramLong, byte[] paramArrayOfByte, int paramInt);
  
  private static native boolean nGetGroupProperties(long paramLong, float[] paramArrayOfFloat, int paramInt);
  
  @FastNative
  private static native float nGetPivotX(long paramLong);
  
  @FastNative
  private static native float nGetPivotY(long paramLong);
  
  @FastNative
  private static native float nGetRootAlpha(long paramLong);
  
  @FastNative
  private static native float nGetRotation(long paramLong);
  
  @FastNative
  private static native float nGetScaleX(long paramLong);
  
  @FastNative
  private static native float nGetScaleY(long paramLong);
  
  @FastNative
  private static native float nGetStrokeAlpha(long paramLong);
  
  @FastNative
  private static native int nGetStrokeColor(long paramLong);
  
  @FastNative
  private static native float nGetStrokeWidth(long paramLong);
  
  @FastNative
  private static native float nGetTranslateX(long paramLong);
  
  @FastNative
  private static native float nGetTranslateY(long paramLong);
  
  @FastNative
  private static native float nGetTrimPathEnd(long paramLong);
  
  @FastNative
  private static native float nGetTrimPathOffset(long paramLong);
  
  @FastNative
  private static native float nGetTrimPathStart(long paramLong);
  
  @FastNative
  private static native void nSetAllowCaching(long paramLong, boolean paramBoolean);
  
  @FastNative
  private static native void nSetAntiAlias(long paramLong, boolean paramBoolean);
  
  @FastNative
  private static native void nSetFillAlpha(long paramLong, float paramFloat);
  
  @FastNative
  private static native void nSetFillColor(long paramLong, int paramInt);
  
  private static native void nSetName(long paramLong, String paramString);
  
  @FastNative
  private static native void nSetPathData(long paramLong1, long paramLong2);
  
  private static native void nSetPathString(long paramLong, String paramString, int paramInt);
  
  @FastNative
  private static native void nSetPivotX(long paramLong, float paramFloat);
  
  @FastNative
  private static native void nSetPivotY(long paramLong, float paramFloat);
  
  @FastNative
  private static native void nSetRendererViewportSize(long paramLong, float paramFloat1, float paramFloat2);
  
  @FastNative
  private static native boolean nSetRootAlpha(long paramLong, float paramFloat);
  
  @FastNative
  private static native void nSetRotation(long paramLong, float paramFloat);
  
  @FastNative
  private static native void nSetScaleX(long paramLong, float paramFloat);
  
  @FastNative
  private static native void nSetScaleY(long paramLong, float paramFloat);
  
  @FastNative
  private static native void nSetStrokeAlpha(long paramLong, float paramFloat);
  
  @FastNative
  private static native void nSetStrokeColor(long paramLong, int paramInt);
  
  @FastNative
  private static native void nSetStrokeWidth(long paramLong, float paramFloat);
  
  @FastNative
  private static native void nSetTranslateX(long paramLong, float paramFloat);
  
  @FastNative
  private static native void nSetTranslateY(long paramLong, float paramFloat);
  
  @FastNative
  private static native void nSetTrimPathEnd(long paramLong, float paramFloat);
  
  @FastNative
  private static native void nSetTrimPathOffset(long paramLong, float paramFloat);
  
  @FastNative
  private static native void nSetTrimPathStart(long paramLong, float paramFloat);
  
  @FastNative
  private static native void nUpdateFullPathFillGradient(long paramLong1, long paramLong2);
  
  @FastNative
  private static native void nUpdateFullPathProperties(long paramLong, float paramFloat1, int paramInt1, float paramFloat2, int paramInt2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, int paramInt3, int paramInt4, int paramInt5);
  
  @FastNative
  private static native void nUpdateFullPathStrokeGradient(long paramLong1, long paramLong2);
  
  @FastNative
  private static native void nUpdateGroupProperties(long paramLong, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7);
  
  private boolean needMirroring()
  {
    boolean bool1 = isAutoMirrored();
    boolean bool2 = true;
    if ((!bool1) || (getLayoutDirection() != 1)) {
      bool2 = false;
    }
    return bool2;
  }
  
  private void updateLocalState(Resources paramResources)
  {
    int i = Drawable.resolveDensity(paramResources, mVectorState.mDensity);
    if (mTargetDensity != i)
    {
      mTargetDensity = i;
      mDpiScaledDirty = true;
    }
    mTintFilter = updateTintFilter(mTintFilter, mVectorState.mTint, mVectorState.mTintMode);
  }
  
  private void updateStateFromTypedArray(TypedArray paramTypedArray)
    throws XmlPullParserException
  {
    Object localObject = mVectorState;
    mChangingConfigurations |= paramTypedArray.getChangingConfigurations();
    mThemeAttrs = paramTypedArray.extractThemeAttrs();
    int i = paramTypedArray.getInt(6, -1);
    if (i != -1) {
      mTintMode = Drawable.parseTintMode(i, PorterDuff.Mode.SRC_IN);
    }
    ColorStateList localColorStateList = paramTypedArray.getColorStateList(1);
    if (localColorStateList != null) {
      mTint = localColorStateList;
    }
    mAutoMirrored = paramTypedArray.getBoolean(5, mAutoMirrored);
    ((VectorDrawableState)localObject).setViewportSize(paramTypedArray.getFloat(7, mViewportWidth), paramTypedArray.getFloat(8, mViewportHeight));
    if (mViewportWidth > 0.0F)
    {
      if (mViewportHeight > 0.0F)
      {
        mBaseWidth = paramTypedArray.getDimensionPixelSize(3, mBaseWidth);
        mBaseHeight = paramTypedArray.getDimensionPixelSize(2, mBaseHeight);
        if (mBaseWidth > 0)
        {
          if (mBaseHeight > 0)
          {
            mOpticalInsets = Insets.of(paramTypedArray.getDimensionPixelOffset(10, mOpticalInsets.left), paramTypedArray.getDimensionPixelOffset(12, mOpticalInsets.top), paramTypedArray.getDimensionPixelOffset(11, mOpticalInsets.right), paramTypedArray.getDimensionPixelOffset(9, mOpticalInsets.bottom));
            ((VectorDrawableState)localObject).setAlpha(paramTypedArray.getFloat(4, ((VectorDrawableState)localObject).getAlpha()));
            paramTypedArray = paramTypedArray.getString(0);
            if (paramTypedArray != null)
            {
              mRootName = paramTypedArray;
              mVGTargetsMap.put(paramTypedArray, localObject);
            }
            return;
          }
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append(paramTypedArray.getPositionDescription());
          ((StringBuilder)localObject).append("<vector> tag requires height > 0");
          throw new XmlPullParserException(((StringBuilder)localObject).toString());
        }
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(paramTypedArray.getPositionDescription());
        ((StringBuilder)localObject).append("<vector> tag requires width > 0");
        throw new XmlPullParserException(((StringBuilder)localObject).toString());
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramTypedArray.getPositionDescription());
      ((StringBuilder)localObject).append("<vector> tag requires viewportHeight > 0");
      throw new XmlPullParserException(((StringBuilder)localObject).toString());
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(paramTypedArray.getPositionDescription());
    ((StringBuilder)localObject).append("<vector> tag requires viewportWidth > 0");
    throw new XmlPullParserException(((StringBuilder)localObject).toString());
  }
  
  /* Error */
  public void applyTheme(Resources.Theme paramTheme)
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokespecial 655	android/graphics/drawable/Drawable:applyTheme	(Landroid/content/res/Resources$Theme;)V
    //   5: aload_0
    //   6: getfield 137	android/graphics/drawable/VectorDrawable:mVectorState	Landroid/graphics/drawable/VectorDrawable$VectorDrawableState;
    //   9: astore_2
    //   10: aload_2
    //   11: ifnonnull +4 -> 15
    //   14: return
    //   15: aload_0
    //   16: getfield 137	android/graphics/drawable/VectorDrawable:mVectorState	Landroid/graphics/drawable/VectorDrawable$VectorDrawableState;
    //   19: aload_1
    //   20: invokevirtual 661	android/content/res/Resources$Theme:getResources	()Landroid/content/res/Resources;
    //   23: iconst_0
    //   24: invokestatic 524	android/graphics/drawable/Drawable:resolveDensity	(Landroid/content/res/Resources;I)I
    //   27: invokevirtual 665	android/graphics/drawable/VectorDrawable$VectorDrawableState:setDensity	(I)Z
    //   30: istore_3
    //   31: aload_0
    //   32: aload_0
    //   33: getfield 130	android/graphics/drawable/VectorDrawable:mDpiScaledDirty	Z
    //   36: iload_3
    //   37: ior
    //   38: putfield 130	android/graphics/drawable/VectorDrawable:mDpiScaledDirty	Z
    //   41: aload_2
    //   42: getfield 555	android/graphics/drawable/VectorDrawable$VectorDrawableState:mThemeAttrs	[I
    //   45: ifnull +63 -> 108
    //   48: aload_1
    //   49: aload_2
    //   50: getfield 555	android/graphics/drawable/VectorDrawable$VectorDrawableState:mThemeAttrs	[I
    //   53: getstatic 670	com/android/internal/R$styleable:VectorDrawable	[I
    //   56: invokevirtual 674	android/content/res/Resources$Theme:resolveAttributes	([I[I)Landroid/content/res/TypedArray;
    //   59: astore 4
    //   61: aload_2
    //   62: iconst_1
    //   63: putfield 677	android/graphics/drawable/VectorDrawable$VectorDrawableState:mCacheDirty	Z
    //   66: aload_0
    //   67: aload 4
    //   69: invokespecial 679	android/graphics/drawable/VectorDrawable:updateStateFromTypedArray	(Landroid/content/res/TypedArray;)V
    //   72: aload 4
    //   74: invokevirtual 682	android/content/res/TypedArray:recycle	()V
    //   77: aload_0
    //   78: iconst_1
    //   79: putfield 130	android/graphics/drawable/VectorDrawable:mDpiScaledDirty	Z
    //   82: goto +26 -> 108
    //   85: astore_1
    //   86: goto +15 -> 101
    //   89: astore_2
    //   90: new 684	java/lang/RuntimeException
    //   93: astore_1
    //   94: aload_1
    //   95: aload_2
    //   96: invokespecial 687	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
    //   99: aload_1
    //   100: athrow
    //   101: aload 4
    //   103: invokevirtual 682	android/content/res/TypedArray:recycle	()V
    //   106: aload_1
    //   107: athrow
    //   108: aload_2
    //   109: getfield 532	android/graphics/drawable/VectorDrawable$VectorDrawableState:mTint	Landroid/content/res/ColorStateList;
    //   112: ifnull +25 -> 137
    //   115: aload_2
    //   116: getfield 532	android/graphics/drawable/VectorDrawable$VectorDrawableState:mTint	Landroid/content/res/ColorStateList;
    //   119: invokevirtual 692	android/content/res/ColorStateList:canApplyTheme	()Z
    //   122: ifeq +15 -> 137
    //   125: aload_2
    //   126: aload_2
    //   127: getfield 532	android/graphics/drawable/VectorDrawable$VectorDrawableState:mTint	Landroid/content/res/ColorStateList;
    //   130: aload_1
    //   131: invokevirtual 696	android/content/res/ColorStateList:obtainForTheme	(Landroid/content/res/Resources$Theme;)Landroid/content/res/ColorStateList;
    //   134: putfield 532	android/graphics/drawable/VectorDrawable$VectorDrawableState:mTint	Landroid/content/res/ColorStateList;
    //   137: aload_0
    //   138: getfield 137	android/graphics/drawable/VectorDrawable:mVectorState	Landroid/graphics/drawable/VectorDrawable$VectorDrawableState;
    //   141: ifnull +21 -> 162
    //   144: aload_0
    //   145: getfield 137	android/graphics/drawable/VectorDrawable:mVectorState	Landroid/graphics/drawable/VectorDrawable$VectorDrawableState;
    //   148: invokevirtual 697	android/graphics/drawable/VectorDrawable$VectorDrawableState:canApplyTheme	()Z
    //   151: ifeq +11 -> 162
    //   154: aload_0
    //   155: getfield 137	android/graphics/drawable/VectorDrawable:mVectorState	Landroid/graphics/drawable/VectorDrawable$VectorDrawableState;
    //   158: aload_1
    //   159: invokevirtual 698	android/graphics/drawable/VectorDrawable$VectorDrawableState:applyTheme	(Landroid/content/res/Resources$Theme;)V
    //   162: aload_0
    //   163: aload_1
    //   164: invokevirtual 661	android/content/res/Resources$Theme:getResources	()Landroid/content/res/Resources;
    //   167: invokespecial 141	android/graphics/drawable/VectorDrawable:updateLocalState	(Landroid/content/res/Resources;)V
    //   170: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	171	0	this	VectorDrawable
    //   0	171	1	paramTheme	Resources.Theme
    //   9	53	2	localVectorDrawableState	VectorDrawableState
    //   89	38	2	localXmlPullParserException	XmlPullParserException
    //   30	8	3	bool	boolean
    //   59	43	4	localTypedArray	TypedArray
    // Exception table:
    //   from	to	target	type
    //   61	72	85	finally
    //   90	101	85	finally
    //   61	72	89	org/xmlpull/v1/XmlPullParserException
  }
  
  public boolean canApplyTheme()
  {
    boolean bool;
    if (((mVectorState != null) && (mVectorState.canApplyTheme())) || (super.canApplyTheme())) {
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
  
  void computeVectorSize()
  {
    Insets localInsets = mVectorState.mOpticalInsets;
    int i = mVectorState.mDensity;
    int j = mTargetDensity;
    if (j != i)
    {
      mDpiScaledWidth = Drawable.scaleFromDensity(mVectorState.mBaseWidth, i, j, true);
      mDpiScaledHeight = Drawable.scaleFromDensity(mVectorState.mBaseHeight, i, j, true);
      int k = Drawable.scaleFromDensity(left, i, j, false);
      int m = Drawable.scaleFromDensity(right, i, j, false);
      mDpiScaledInsets = Insets.of(k, Drawable.scaleFromDensity(top, i, j, false), m, Drawable.scaleFromDensity(bottom, i, j, false));
    }
    else
    {
      mDpiScaledWidth = mVectorState.mBaseWidth;
      mDpiScaledHeight = mVectorState.mBaseHeight;
      mDpiScaledInsets = localInsets;
    }
    mDpiScaledDirty = false;
  }
  
  public void draw(Canvas paramCanvas)
  {
    copyBounds(mTmpBounds);
    if ((mTmpBounds.width() > 0) && (mTmpBounds.height() > 0))
    {
      Object localObject;
      if (mColorFilter == null) {
        localObject = mTintFilter;
      } else {
        localObject = mColorFilter;
      }
      if (localObject == null) {}
      for (long l = 0L;; l = ((ColorFilter)localObject).getNativeInstance()) {
        break;
      }
      boolean bool = mVectorState.canReuseCache();
      int i = nDraw(mVectorState.getNativeRenderer(), paramCanvas.getNativeCanvasWrapper(), l, mTmpBounds, needMirroring(), bool);
      if (i == 0) {
        return;
      }
      int j;
      if (paramCanvas.isHardwareAccelerated())
      {
        j = (i - mVectorState.mLastHWCachePixelCount) * 4;
        mVectorState.mLastHWCachePixelCount = i;
      }
      else
      {
        j = (i - mVectorState.mLastSWCachePixelCount) * 4;
        mVectorState.mLastSWCachePixelCount = i;
      }
      if (j > 0) {
        VMRuntime.getRuntime().registerNativeAllocation(j);
      } else if (j < 0) {
        VMRuntime.getRuntime().registerNativeFree(-j);
      }
      return;
    }
  }
  
  public int getAlpha()
  {
    return (int)(mVectorState.getAlpha() * 255.0F);
  }
  
  public int getChangingConfigurations()
  {
    return super.getChangingConfigurations() | mVectorState.getChangingConfigurations();
  }
  
  public ColorFilter getColorFilter()
  {
    return mColorFilter;
  }
  
  public Drawable.ConstantState getConstantState()
  {
    mVectorState.mChangingConfigurations = getChangingConfigurations();
    return mVectorState;
  }
  
  public int getIntrinsicHeight()
  {
    if (mDpiScaledDirty) {
      computeVectorSize();
    }
    return mDpiScaledHeight;
  }
  
  public int getIntrinsicWidth()
  {
    if (mDpiScaledDirty) {
      computeVectorSize();
    }
    return mDpiScaledWidth;
  }
  
  public long getNativeTree()
  {
    return mVectorState.getNativeRenderer();
  }
  
  public int getOpacity()
  {
    int i;
    if (getAlpha() == 0) {
      i = -2;
    } else {
      i = -3;
    }
    return i;
  }
  
  public Insets getOpticalInsets()
  {
    if (mDpiScaledDirty) {
      computeVectorSize();
    }
    return mDpiScaledInsets;
  }
  
  public float getPixelSize()
  {
    if ((mVectorState != null) && (mVectorState.mBaseWidth != 0) && (mVectorState.mBaseHeight != 0) && (mVectorState.mViewportHeight != 0.0F) && (mVectorState.mViewportWidth != 0.0F))
    {
      float f1 = mVectorState.mBaseWidth;
      float f2 = mVectorState.mBaseHeight;
      float f3 = mVectorState.mViewportWidth;
      float f4 = mVectorState.mViewportHeight;
      return Math.min(f3 / f1, f4 / f2);
    }
    return 1.0F;
  }
  
  Object getTargetByName(String paramString)
  {
    return mVectorState.mVGTargetsMap.get(paramString);
  }
  
  public boolean hasFocusStateSpecified()
  {
    boolean bool;
    if ((mVectorState != null) && (mVectorState.hasFocusStateSpecified())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    try
    {
      Trace.traceBegin(8192L, "VectorDrawable#inflate");
      if ((mVectorState.mRootGroup != null) || (mVectorState.mNativeTree != null))
      {
        if (mVectorState.mRootGroup != null)
        {
          VMRuntime.getRuntime().registerNativeFree(mVectorState.mRootGroup.getNativeSize());
          mVectorState.mRootGroup.setTree(null);
        }
        localObject1 = mVectorState;
        localObject2 = new android/graphics/drawable/VectorDrawable$VGroup;
        ((VGroup)localObject2).<init>();
        mRootGroup = ((VGroup)localObject2);
        if (mVectorState.mNativeTree != null)
        {
          VMRuntime.getRuntime().registerNativeFree(316);
          mVectorState.mNativeTree.release();
        }
        mVectorState.createNativeTree(mVectorState.mRootGroup);
      }
      Object localObject2 = mVectorState;
      ((VectorDrawableState)localObject2).setDensity(Drawable.resolveDensity(paramResources, 0));
      Object localObject1 = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.VectorDrawable);
      updateStateFromTypedArray((TypedArray)localObject1);
      ((TypedArray)localObject1).recycle();
      mDpiScaledDirty = true;
      mCacheDirty = true;
      inflateChildElements(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
      ((VectorDrawableState)localObject2).onTreeConstructionFinished();
      updateLocalState(paramResources);
      return;
    }
    finally
    {
      Trace.traceEnd(8192L);
    }
  }
  
  public boolean isAutoMirrored()
  {
    return mVectorState.mAutoMirrored;
  }
  
  public boolean isStateful()
  {
    boolean bool;
    if ((!super.isStateful()) && ((mVectorState == null) || (!mVectorState.isStateful()))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public Drawable mutate()
  {
    if ((!mMutated) && (super.mutate() == this))
    {
      mVectorState = new VectorDrawableState(mVectorState);
      mMutated = true;
    }
    return this;
  }
  
  protected boolean onStateChange(int[] paramArrayOfInt)
  {
    boolean bool1 = false;
    if (isStateful()) {
      mutate();
    }
    VectorDrawableState localVectorDrawableState = mVectorState;
    if (localVectorDrawableState.onStateChange(paramArrayOfInt))
    {
      bool1 = true;
      mCacheDirty = true;
    }
    boolean bool2 = bool1;
    if (mTint != null)
    {
      bool2 = bool1;
      if (mTintMode != null)
      {
        mTintFilter = updateTintFilter(mTintFilter, mTint, mTintMode);
        bool2 = true;
      }
    }
    return bool2;
  }
  
  void setAllowCaching(boolean paramBoolean)
  {
    nSetAllowCaching(mVectorState.getNativeRenderer(), paramBoolean);
  }
  
  public void setAlpha(int paramInt)
  {
    if (mVectorState.setAlpha(paramInt / 255.0F)) {
      invalidateSelf();
    }
  }
  
  public void setAntiAlias(boolean paramBoolean)
  {
    nSetAntiAlias(mVectorState.mNativeTree.get(), paramBoolean);
  }
  
  public void setAutoMirrored(boolean paramBoolean)
  {
    if (mVectorState.mAutoMirrored != paramBoolean)
    {
      mVectorState.mAutoMirrored = paramBoolean;
      invalidateSelf();
    }
  }
  
  public void setColorFilter(ColorFilter paramColorFilter)
  {
    mColorFilter = paramColorFilter;
    invalidateSelf();
  }
  
  public void setTintList(ColorStateList paramColorStateList)
  {
    VectorDrawableState localVectorDrawableState = mVectorState;
    if (mTint != paramColorStateList)
    {
      mTint = paramColorStateList;
      mTintFilter = updateTintFilter(mTintFilter, paramColorStateList, mTintMode);
      invalidateSelf();
    }
  }
  
  public void setTintMode(PorterDuff.Mode paramMode)
  {
    VectorDrawableState localVectorDrawableState = mVectorState;
    if (mTintMode != paramMode)
    {
      mTintMode = paramMode;
      mTintFilter = updateTintFilter(mTintFilter, mTint, paramMode);
      invalidateSelf();
    }
  }
  
  private static class VClipPath
    extends VectorDrawable.VPath
  {
    private static final int NATIVE_ALLOCATION_SIZE = 120;
    private final long mNativePtr;
    
    public VClipPath()
    {
      mNativePtr = VectorDrawable.access$3700();
    }
    
    public VClipPath(VClipPath paramVClipPath)
    {
      super();
      mNativePtr = VectorDrawable.nCreateClipPath(mNativePtr);
    }
    
    private void updateStateFromTypedArray(TypedArray paramTypedArray)
    {
      mChangingConfigurations |= paramTypedArray.getChangingConfigurations();
      String str = paramTypedArray.getString(0);
      if (str != null)
      {
        mPathName = str;
        VectorDrawable.nSetName(mNativePtr, mPathName);
      }
      paramTypedArray = paramTypedArray.getString(1);
      if (paramTypedArray != null)
      {
        mPathData = new PathParser.PathData(paramTypedArray);
        VectorDrawable.nSetPathString(mNativePtr, paramTypedArray, paramTypedArray.length());
      }
    }
    
    public void applyTheme(Resources.Theme paramTheme) {}
    
    public boolean canApplyTheme()
    {
      return false;
    }
    
    public long getNativePtr()
    {
      return mNativePtr;
    }
    
    int getNativeSize()
    {
      return 120;
    }
    
    public boolean hasFocusStateSpecified()
    {
      return false;
    }
    
    public void inflate(Resources paramResources, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    {
      paramResources = Drawable.obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.VectorDrawableClipPath);
      updateStateFromTypedArray(paramResources);
      paramResources.recycle();
    }
    
    public boolean isStateful()
    {
      return false;
    }
    
    public boolean onStateChange(int[] paramArrayOfInt)
    {
      return false;
    }
  }
  
  static class VFullPath
    extends VectorDrawable.VPath
  {
    private static final Property<VFullPath, Float> FILL_ALPHA;
    private static final int FILL_ALPHA_INDEX = 4;
    private static final Property<VFullPath, Integer> FILL_COLOR;
    private static final int FILL_COLOR_INDEX = 3;
    private static final int FILL_TYPE_INDEX = 11;
    private static final int NATIVE_ALLOCATION_SIZE = 264;
    private static final Property<VFullPath, Float> STROKE_ALPHA;
    private static final int STROKE_ALPHA_INDEX = 2;
    private static final Property<VFullPath, Integer> STROKE_COLOR;
    private static final int STROKE_COLOR_INDEX = 1;
    private static final int STROKE_LINE_CAP_INDEX = 8;
    private static final int STROKE_LINE_JOIN_INDEX = 9;
    private static final int STROKE_MITER_LIMIT_INDEX = 10;
    private static final Property<VFullPath, Float> STROKE_WIDTH;
    private static final int STROKE_WIDTH_INDEX = 0;
    private static final int TOTAL_PROPERTY_COUNT = 12;
    private static final Property<VFullPath, Float> TRIM_PATH_END = new FloatProperty("trimPathEnd")
    {
      public Float get(VectorDrawable.VFullPath paramAnonymousVFullPath)
      {
        return Float.valueOf(paramAnonymousVFullPath.getTrimPathEnd());
      }
      
      public void setValue(VectorDrawable.VFullPath paramAnonymousVFullPath, float paramAnonymousFloat)
      {
        paramAnonymousVFullPath.setTrimPathEnd(paramAnonymousFloat);
      }
    };
    private static final int TRIM_PATH_END_INDEX = 6;
    private static final Property<VFullPath, Float> TRIM_PATH_OFFSET = new FloatProperty("trimPathOffset")
    {
      public Float get(VectorDrawable.VFullPath paramAnonymousVFullPath)
      {
        return Float.valueOf(paramAnonymousVFullPath.getTrimPathOffset());
      }
      
      public void setValue(VectorDrawable.VFullPath paramAnonymousVFullPath, float paramAnonymousFloat)
      {
        paramAnonymousVFullPath.setTrimPathOffset(paramAnonymousFloat);
      }
    };
    private static final int TRIM_PATH_OFFSET_INDEX = 7;
    private static final Property<VFullPath, Float> TRIM_PATH_START;
    private static final int TRIM_PATH_START_INDEX = 5;
    private static final HashMap<String, Integer> sPropertyIndexMap = new HashMap() {};
    private static final HashMap<String, Property> sPropertyMap = new HashMap() {};
    ComplexColor mFillColors = null;
    private final long mNativePtr;
    private byte[] mPropertyData;
    ComplexColor mStrokeColors = null;
    private int[] mThemeAttrs;
    
    static
    {
      STROKE_WIDTH = new FloatProperty("strokeWidth")
      {
        public Float get(VectorDrawable.VFullPath paramAnonymousVFullPath)
        {
          return Float.valueOf(paramAnonymousVFullPath.getStrokeWidth());
        }
        
        public void setValue(VectorDrawable.VFullPath paramAnonymousVFullPath, float paramAnonymousFloat)
        {
          paramAnonymousVFullPath.setStrokeWidth(paramAnonymousFloat);
        }
      };
      STROKE_COLOR = new IntProperty("strokeColor")
      {
        public Integer get(VectorDrawable.VFullPath paramAnonymousVFullPath)
        {
          return Integer.valueOf(paramAnonymousVFullPath.getStrokeColor());
        }
        
        public void setValue(VectorDrawable.VFullPath paramAnonymousVFullPath, int paramAnonymousInt)
        {
          paramAnonymousVFullPath.setStrokeColor(paramAnonymousInt);
        }
      };
      STROKE_ALPHA = new FloatProperty("strokeAlpha")
      {
        public Float get(VectorDrawable.VFullPath paramAnonymousVFullPath)
        {
          return Float.valueOf(paramAnonymousVFullPath.getStrokeAlpha());
        }
        
        public void setValue(VectorDrawable.VFullPath paramAnonymousVFullPath, float paramAnonymousFloat)
        {
          paramAnonymousVFullPath.setStrokeAlpha(paramAnonymousFloat);
        }
      };
      FILL_COLOR = new IntProperty("fillColor")
      {
        public Integer get(VectorDrawable.VFullPath paramAnonymousVFullPath)
        {
          return Integer.valueOf(paramAnonymousVFullPath.getFillColor());
        }
        
        public void setValue(VectorDrawable.VFullPath paramAnonymousVFullPath, int paramAnonymousInt)
        {
          paramAnonymousVFullPath.setFillColor(paramAnonymousInt);
        }
      };
      FILL_ALPHA = new FloatProperty("fillAlpha")
      {
        public Float get(VectorDrawable.VFullPath paramAnonymousVFullPath)
        {
          return Float.valueOf(paramAnonymousVFullPath.getFillAlpha());
        }
        
        public void setValue(VectorDrawable.VFullPath paramAnonymousVFullPath, float paramAnonymousFloat)
        {
          paramAnonymousVFullPath.setFillAlpha(paramAnonymousFloat);
        }
      };
      TRIM_PATH_START = new FloatProperty("trimPathStart")
      {
        public Float get(VectorDrawable.VFullPath paramAnonymousVFullPath)
        {
          return Float.valueOf(paramAnonymousVFullPath.getTrimPathStart());
        }
        
        public void setValue(VectorDrawable.VFullPath paramAnonymousVFullPath, float paramAnonymousFloat)
        {
          paramAnonymousVFullPath.setTrimPathStart(paramAnonymousFloat);
        }
      };
    }
    
    public VFullPath()
    {
      mNativePtr = VectorDrawable.access$4800();
    }
    
    public VFullPath(VFullPath paramVFullPath)
    {
      super();
      mNativePtr = VectorDrawable.nCreateFullPath(mNativePtr);
      mThemeAttrs = mThemeAttrs;
      mStrokeColors = mStrokeColors;
      mFillColors = mFillColors;
    }
    
    private boolean canComplexColorApplyTheme(ComplexColor paramComplexColor)
    {
      boolean bool;
      if ((paramComplexColor != null) && (paramComplexColor.canApplyTheme())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    private void updateStateFromTypedArray(TypedArray paramTypedArray)
    {
      if (mPropertyData == null) {
        mPropertyData = new byte[48];
      }
      if (VectorDrawable.nGetFullPathProperties(mNativePtr, mPropertyData, 48))
      {
        Object localObject1 = ByteBuffer.wrap(mPropertyData);
        ((ByteBuffer)localObject1).order(ByteOrder.nativeOrder());
        float f1 = ((ByteBuffer)localObject1).getFloat(0);
        int i = ((ByteBuffer)localObject1).getInt(4);
        float f2 = ((ByteBuffer)localObject1).getFloat(8);
        int j = ((ByteBuffer)localObject1).getInt(12);
        float f3 = ((ByteBuffer)localObject1).getFloat(16);
        float f4 = ((ByteBuffer)localObject1).getFloat(20);
        float f5 = ((ByteBuffer)localObject1).getFloat(24);
        float f6 = ((ByteBuffer)localObject1).getFloat(28);
        int k = ((ByteBuffer)localObject1).getInt(32);
        int m = ((ByteBuffer)localObject1).getInt(36);
        float f7 = ((ByteBuffer)localObject1).getFloat(40);
        int n = ((ByteBuffer)localObject1).getInt(44);
        Object localObject2 = null;
        localObject1 = null;
        Object localObject3 = null;
        mChangingConfigurations |= paramTypedArray.getChangingConfigurations();
        mThemeAttrs = paramTypedArray.extractThemeAttrs();
        Object localObject4 = paramTypedArray.getString(0);
        if (localObject4 != null)
        {
          mPathName = ((String)localObject4);
          VectorDrawable.nSetName(mNativePtr, mPathName);
        }
        localObject4 = paramTypedArray.getString(2);
        if (localObject4 != null)
        {
          mPathData = new PathParser.PathData((String)localObject4);
          VectorDrawable.nSetPathString(mNativePtr, (String)localObject4, ((String)localObject4).length());
        }
        localObject4 = paramTypedArray.getComplexColor(1);
        if (localObject4 != null)
        {
          if ((localObject4 instanceof GradientColor))
          {
            mFillColors = ((ComplexColor)localObject4);
            localObject1 = ((GradientColor)localObject4).getShader();
          }
          else if (((ComplexColor)localObject4).isStateful())
          {
            mFillColors = ((ComplexColor)localObject4);
          }
          else
          {
            mFillColors = null;
          }
          j = ((ComplexColor)localObject4).getDefaultColor();
          localObject2 = localObject1;
        }
        localObject4 = paramTypedArray.getComplexColor(3);
        if (localObject4 != null)
        {
          if ((localObject4 instanceof GradientColor))
          {
            mStrokeColors = ((ComplexColor)localObject4);
            localObject1 = ((GradientColor)localObject4).getShader();
          }
          else if (((ComplexColor)localObject4).isStateful())
          {
            mStrokeColors = ((ComplexColor)localObject4);
            localObject1 = localObject3;
          }
          else
          {
            mStrokeColors = null;
            localObject1 = localObject3;
          }
          i = ((ComplexColor)localObject4).getDefaultColor();
        }
        else
        {
          localObject1 = null;
        }
        long l1 = mNativePtr;
        long l2 = 0L;
        if (localObject2 != null) {
          l3 = localObject2.getNativeInstance();
        } else {
          l3 = 0L;
        }
        VectorDrawable.nUpdateFullPathFillGradient(l1, l3);
        l1 = mNativePtr;
        if (localObject1 != null) {}
        for (long l3 = ((Shader)localObject1).getNativeInstance();; l3 = l2) {
          break;
        }
        VectorDrawable.nUpdateFullPathStrokeGradient(l1, l3);
        f3 = paramTypedArray.getFloat(12, f3);
        k = paramTypedArray.getInt(8, k);
        m = paramTypedArray.getInt(9, m);
        f7 = paramTypedArray.getFloat(10, f7);
        f2 = paramTypedArray.getFloat(11, f2);
        f1 = paramTypedArray.getFloat(4, f1);
        f5 = paramTypedArray.getFloat(6, f5);
        f6 = paramTypedArray.getFloat(7, f6);
        f4 = paramTypedArray.getFloat(5, f4);
        n = paramTypedArray.getInt(13, n);
        VectorDrawable.nUpdateFullPathProperties(mNativePtr, f1, i, f2, j, f3, f4, f5, f6, f7, k, m, n);
        return;
      }
      throw new RuntimeException("Error: inconsistent property count");
    }
    
    public void applyTheme(Resources.Theme paramTheme)
    {
      if (mThemeAttrs != null)
      {
        TypedArray localTypedArray = paramTheme.resolveAttributes(mThemeAttrs, R.styleable.VectorDrawablePath);
        updateStateFromTypedArray(localTypedArray);
        localTypedArray.recycle();
      }
      boolean bool1 = canComplexColorApplyTheme(mFillColors);
      boolean bool2 = canComplexColorApplyTheme(mStrokeColors);
      if (bool1)
      {
        mFillColors = mFillColors.obtainForTheme(paramTheme);
        if ((mFillColors instanceof GradientColor)) {
          VectorDrawable.nUpdateFullPathFillGradient(mNativePtr, ((GradientColor)mFillColors).getShader().getNativeInstance());
        } else if ((mFillColors instanceof ColorStateList)) {
          VectorDrawable.nSetFillColor(mNativePtr, mFillColors.getDefaultColor());
        }
      }
      if (bool2)
      {
        mStrokeColors = mStrokeColors.obtainForTheme(paramTheme);
        if ((mStrokeColors instanceof GradientColor)) {
          VectorDrawable.nUpdateFullPathStrokeGradient(mNativePtr, ((GradientColor)mStrokeColors).getShader().getNativeInstance());
        } else if ((mStrokeColors instanceof ColorStateList)) {
          VectorDrawable.nSetStrokeColor(mNativePtr, mStrokeColors.getDefaultColor());
        }
      }
    }
    
    public boolean canApplyTheme()
    {
      if (mThemeAttrs != null) {
        return true;
      }
      boolean bool1 = canComplexColorApplyTheme(mFillColors);
      boolean bool2 = canComplexColorApplyTheme(mStrokeColors);
      return (bool1) || (bool2);
    }
    
    float getFillAlpha()
    {
      float f;
      if (isTreeValid()) {
        f = VectorDrawable.nGetFillAlpha(mNativePtr);
      } else {
        f = 0.0F;
      }
      return f;
    }
    
    int getFillColor()
    {
      int i;
      if (isTreeValid()) {
        i = VectorDrawable.nGetFillColor(mNativePtr);
      } else {
        i = 0;
      }
      return i;
    }
    
    public long getNativePtr()
    {
      return mNativePtr;
    }
    
    int getNativeSize()
    {
      return 264;
    }
    
    Property getProperty(String paramString)
    {
      Property localProperty = super.getProperty(paramString);
      if (localProperty != null) {
        return localProperty;
      }
      if (sPropertyMap.containsKey(paramString)) {
        return (Property)sPropertyMap.get(paramString);
      }
      return null;
    }
    
    int getPropertyIndex(String paramString)
    {
      if (!sPropertyIndexMap.containsKey(paramString)) {
        return -1;
      }
      return ((Integer)sPropertyIndexMap.get(paramString)).intValue();
    }
    
    float getStrokeAlpha()
    {
      float f;
      if (isTreeValid()) {
        f = VectorDrawable.nGetStrokeAlpha(mNativePtr);
      } else {
        f = 0.0F;
      }
      return f;
    }
    
    int getStrokeColor()
    {
      int i;
      if (isTreeValid()) {
        i = VectorDrawable.nGetStrokeColor(mNativePtr);
      } else {
        i = 0;
      }
      return i;
    }
    
    float getStrokeWidth()
    {
      float f;
      if (isTreeValid()) {
        f = VectorDrawable.nGetStrokeWidth(mNativePtr);
      } else {
        f = 0.0F;
      }
      return f;
    }
    
    float getTrimPathEnd()
    {
      float f;
      if (isTreeValid()) {
        f = VectorDrawable.nGetTrimPathEnd(mNativePtr);
      } else {
        f = 0.0F;
      }
      return f;
    }
    
    float getTrimPathOffset()
    {
      float f;
      if (isTreeValid()) {
        f = VectorDrawable.nGetTrimPathOffset(mNativePtr);
      } else {
        f = 0.0F;
      }
      return f;
    }
    
    float getTrimPathStart()
    {
      float f;
      if (isTreeValid()) {
        f = VectorDrawable.nGetTrimPathStart(mNativePtr);
      } else {
        f = 0.0F;
      }
      return f;
    }
    
    public boolean hasFocusStateSpecified()
    {
      boolean bool;
      if ((mStrokeColors != null) && ((mStrokeColors instanceof ColorStateList)) && (((ColorStateList)mStrokeColors).hasFocusStateSpecified()) && (mFillColors != null) && ((mFillColors instanceof ColorStateList)) && (((ColorStateList)mFillColors).hasFocusStateSpecified())) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void inflate(Resources paramResources, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    {
      paramResources = Drawable.obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.VectorDrawablePath);
      updateStateFromTypedArray(paramResources);
      paramResources.recycle();
    }
    
    public boolean isStateful()
    {
      boolean bool;
      if ((mStrokeColors == null) && (mFillColors == null)) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public boolean onStateChange(int[] paramArrayOfInt)
    {
      boolean bool1 = false;
      ComplexColor localComplexColor = mStrokeColors;
      boolean bool2 = false;
      boolean bool3 = bool1;
      int i;
      int j;
      boolean bool4;
      if (localComplexColor != null)
      {
        bool3 = bool1;
        if ((mStrokeColors instanceof ColorStateList))
        {
          i = getStrokeColor();
          j = ((ColorStateList)mStrokeColors).getColorForState(paramArrayOfInt, i);
          if (i != j) {
            bool4 = true;
          } else {
            bool4 = false;
          }
          bool1 = false | bool4;
          bool3 = bool1;
          if (i != j)
          {
            VectorDrawable.nSetStrokeColor(mNativePtr, j);
            bool3 = bool1;
          }
        }
      }
      bool1 = bool3;
      if (mFillColors != null)
      {
        bool1 = bool3;
        if ((mFillColors instanceof ColorStateList))
        {
          j = getFillColor();
          i = ((ColorStateList)mFillColors).getColorForState(paramArrayOfInt, j);
          bool4 = bool2;
          if (j != i) {
            bool4 = true;
          }
          bool3 |= bool4;
          bool1 = bool3;
          if (j != i)
          {
            VectorDrawable.nSetFillColor(mNativePtr, i);
            bool1 = bool3;
          }
        }
      }
      return bool1;
    }
    
    void setFillAlpha(float paramFloat)
    {
      if (isTreeValid()) {
        VectorDrawable.nSetFillAlpha(mNativePtr, paramFloat);
      }
    }
    
    void setFillColor(int paramInt)
    {
      mFillColors = null;
      if (isTreeValid()) {
        VectorDrawable.nSetFillColor(mNativePtr, paramInt);
      }
    }
    
    void setStrokeAlpha(float paramFloat)
    {
      if (isTreeValid()) {
        VectorDrawable.nSetStrokeAlpha(mNativePtr, paramFloat);
      }
    }
    
    void setStrokeColor(int paramInt)
    {
      mStrokeColors = null;
      if (isTreeValid()) {
        VectorDrawable.nSetStrokeColor(mNativePtr, paramInt);
      }
    }
    
    void setStrokeWidth(float paramFloat)
    {
      if (isTreeValid()) {
        VectorDrawable.nSetStrokeWidth(mNativePtr, paramFloat);
      }
    }
    
    void setTrimPathEnd(float paramFloat)
    {
      if (isTreeValid()) {
        VectorDrawable.nSetTrimPathEnd(mNativePtr, paramFloat);
      }
    }
    
    void setTrimPathOffset(float paramFloat)
    {
      if (isTreeValid()) {
        VectorDrawable.nSetTrimPathOffset(mNativePtr, paramFloat);
      }
    }
    
    void setTrimPathStart(float paramFloat)
    {
      if (isTreeValid()) {
        VectorDrawable.nSetTrimPathStart(mNativePtr, paramFloat);
      }
    }
  }
  
  static class VGroup
    extends VectorDrawable.VObject
  {
    private static final int NATIVE_ALLOCATION_SIZE = 100;
    private static final Property<VGroup, Float> PIVOT_X = new FloatProperty("pivotX")
    {
      public Float get(VectorDrawable.VGroup paramAnonymousVGroup)
      {
        return Float.valueOf(paramAnonymousVGroup.getPivotX());
      }
      
      public void setValue(VectorDrawable.VGroup paramAnonymousVGroup, float paramAnonymousFloat)
      {
        paramAnonymousVGroup.setPivotX(paramAnonymousFloat);
      }
    };
    private static final int PIVOT_X_INDEX = 1;
    private static final Property<VGroup, Float> PIVOT_Y = new FloatProperty("pivotY")
    {
      public Float get(VectorDrawable.VGroup paramAnonymousVGroup)
      {
        return Float.valueOf(paramAnonymousVGroup.getPivotY());
      }
      
      public void setValue(VectorDrawable.VGroup paramAnonymousVGroup, float paramAnonymousFloat)
      {
        paramAnonymousVGroup.setPivotY(paramAnonymousFloat);
      }
    };
    private static final int PIVOT_Y_INDEX = 2;
    private static final Property<VGroup, Float> ROTATION = new FloatProperty("rotation")
    {
      public Float get(VectorDrawable.VGroup paramAnonymousVGroup)
      {
        return Float.valueOf(paramAnonymousVGroup.getRotation());
      }
      
      public void setValue(VectorDrawable.VGroup paramAnonymousVGroup, float paramAnonymousFloat)
      {
        paramAnonymousVGroup.setRotation(paramAnonymousFloat);
      }
    };
    private static final int ROTATION_INDEX = 0;
    private static final Property<VGroup, Float> SCALE_X;
    private static final int SCALE_X_INDEX = 3;
    private static final Property<VGroup, Float> SCALE_Y;
    private static final int SCALE_Y_INDEX = 4;
    private static final int TRANSFORM_PROPERTY_COUNT = 7;
    private static final Property<VGroup, Float> TRANSLATE_X;
    private static final int TRANSLATE_X_INDEX = 5;
    private static final Property<VGroup, Float> TRANSLATE_Y;
    private static final int TRANSLATE_Y_INDEX = 6;
    private static final HashMap<String, Integer> sPropertyIndexMap = new HashMap() {};
    private static final HashMap<String, Property> sPropertyMap = new HashMap() {};
    private int mChangingConfigurations;
    private final ArrayList<VectorDrawable.VObject> mChildren = new ArrayList();
    private String mGroupName = null;
    private boolean mIsStateful;
    private final long mNativePtr;
    private int[] mThemeAttrs;
    private float[] mTransform;
    
    static
    {
      TRANSLATE_X = new FloatProperty("translateX")
      {
        public Float get(VectorDrawable.VGroup paramAnonymousVGroup)
        {
          return Float.valueOf(paramAnonymousVGroup.getTranslateX());
        }
        
        public void setValue(VectorDrawable.VGroup paramAnonymousVGroup, float paramAnonymousFloat)
        {
          paramAnonymousVGroup.setTranslateX(paramAnonymousFloat);
        }
      };
      TRANSLATE_Y = new FloatProperty("translateY")
      {
        public Float get(VectorDrawable.VGroup paramAnonymousVGroup)
        {
          return Float.valueOf(paramAnonymousVGroup.getTranslateY());
        }
        
        public void setValue(VectorDrawable.VGroup paramAnonymousVGroup, float paramAnonymousFloat)
        {
          paramAnonymousVGroup.setTranslateY(paramAnonymousFloat);
        }
      };
      SCALE_X = new FloatProperty("scaleX")
      {
        public Float get(VectorDrawable.VGroup paramAnonymousVGroup)
        {
          return Float.valueOf(paramAnonymousVGroup.getScaleX());
        }
        
        public void setValue(VectorDrawable.VGroup paramAnonymousVGroup, float paramAnonymousFloat)
        {
          paramAnonymousVGroup.setScaleX(paramAnonymousFloat);
        }
      };
      SCALE_Y = new FloatProperty("scaleY")
      {
        public Float get(VectorDrawable.VGroup paramAnonymousVGroup)
        {
          return Float.valueOf(paramAnonymousVGroup.getScaleY());
        }
        
        public void setValue(VectorDrawable.VGroup paramAnonymousVGroup, float paramAnonymousFloat)
        {
          paramAnonymousVGroup.setScaleY(paramAnonymousFloat);
        }
      };
    }
    
    public VGroup()
    {
      mNativePtr = VectorDrawable.access$1700();
    }
    
    public VGroup(VGroup paramVGroup, ArrayMap<String, Object> paramArrayMap)
    {
      mIsStateful = mIsStateful;
      mThemeAttrs = mThemeAttrs;
      mGroupName = mGroupName;
      mChangingConfigurations = mChangingConfigurations;
      if (mGroupName != null) {
        paramArrayMap.put(mGroupName, this);
      }
      mNativePtr = VectorDrawable.nCreateGroup(mNativePtr);
      ArrayList localArrayList = mChildren;
      int i = 0;
      while (i < localArrayList.size())
      {
        paramVGroup = (VectorDrawable.VObject)localArrayList.get(i);
        if ((paramVGroup instanceof VGroup))
        {
          addChild(new VGroup((VGroup)paramVGroup, paramArrayMap));
        }
        else
        {
          if ((paramVGroup instanceof VectorDrawable.VFullPath)) {}
          for (paramVGroup = new VectorDrawable.VFullPath((VectorDrawable.VFullPath)paramVGroup);; paramVGroup = new VectorDrawable.VClipPath((VectorDrawable.VClipPath)paramVGroup))
          {
            break;
            if (!(paramVGroup instanceof VectorDrawable.VClipPath)) {
              break label205;
            }
          }
          addChild(paramVGroup);
          if (mPathName != null) {
            paramArrayMap.put(mPathName, paramVGroup);
          }
        }
        i++;
        continue;
        label205:
        throw new IllegalStateException("Unknown object in the tree!");
      }
    }
    
    static int getPropertyIndex(String paramString)
    {
      if (sPropertyIndexMap.containsKey(paramString)) {
        return ((Integer)sPropertyIndexMap.get(paramString)).intValue();
      }
      return -1;
    }
    
    public void addChild(VectorDrawable.VObject paramVObject)
    {
      VectorDrawable.nAddChild(mNativePtr, paramVObject.getNativePtr());
      mChildren.add(paramVObject);
      mIsStateful |= paramVObject.isStateful();
    }
    
    public void applyTheme(Resources.Theme paramTheme)
    {
      Object localObject;
      if (mThemeAttrs != null)
      {
        localObject = paramTheme.resolveAttributes(mThemeAttrs, R.styleable.VectorDrawableGroup);
        updateStateFromTypedArray((TypedArray)localObject);
        ((TypedArray)localObject).recycle();
      }
      ArrayList localArrayList = mChildren;
      int i = 0;
      int j = localArrayList.size();
      while (i < j)
      {
        localObject = (VectorDrawable.VObject)localArrayList.get(i);
        if (((VectorDrawable.VObject)localObject).canApplyTheme())
        {
          ((VectorDrawable.VObject)localObject).applyTheme(paramTheme);
          mIsStateful |= ((VectorDrawable.VObject)localObject).isStateful();
        }
        i++;
      }
    }
    
    public boolean canApplyTheme()
    {
      if (mThemeAttrs != null) {
        return true;
      }
      ArrayList localArrayList = mChildren;
      int i = 0;
      int j = localArrayList.size();
      while (i < j)
      {
        if (((VectorDrawable.VObject)localArrayList.get(i)).canApplyTheme()) {
          return true;
        }
        i++;
      }
      return false;
    }
    
    public String getGroupName()
    {
      return mGroupName;
    }
    
    public long getNativePtr()
    {
      return mNativePtr;
    }
    
    int getNativeSize()
    {
      int i = 100;
      for (int j = 0; j < mChildren.size(); j++) {
        i += ((VectorDrawable.VObject)mChildren.get(j)).getNativeSize();
      }
      return i;
    }
    
    public float getPivotX()
    {
      float f;
      if (isTreeValid()) {
        f = VectorDrawable.nGetPivotX(mNativePtr);
      } else {
        f = 0.0F;
      }
      return f;
    }
    
    public float getPivotY()
    {
      float f;
      if (isTreeValid()) {
        f = VectorDrawable.nGetPivotY(mNativePtr);
      } else {
        f = 0.0F;
      }
      return f;
    }
    
    Property getProperty(String paramString)
    {
      if (sPropertyMap.containsKey(paramString)) {
        return (Property)sPropertyMap.get(paramString);
      }
      return null;
    }
    
    public float getRotation()
    {
      float f;
      if (isTreeValid()) {
        f = VectorDrawable.nGetRotation(mNativePtr);
      } else {
        f = 0.0F;
      }
      return f;
    }
    
    public float getScaleX()
    {
      float f;
      if (isTreeValid()) {
        f = VectorDrawable.nGetScaleX(mNativePtr);
      } else {
        f = 0.0F;
      }
      return f;
    }
    
    public float getScaleY()
    {
      float f;
      if (isTreeValid()) {
        f = VectorDrawable.nGetScaleY(mNativePtr);
      } else {
        f = 0.0F;
      }
      return f;
    }
    
    public float getTranslateX()
    {
      float f;
      if (isTreeValid()) {
        f = VectorDrawable.nGetTranslateX(mNativePtr);
      } else {
        f = 0.0F;
      }
      return f;
    }
    
    public float getTranslateY()
    {
      float f;
      if (isTreeValid()) {
        f = VectorDrawable.nGetTranslateY(mNativePtr);
      } else {
        f = 0.0F;
      }
      return f;
    }
    
    public boolean hasFocusStateSpecified()
    {
      boolean bool1 = false;
      ArrayList localArrayList = mChildren;
      int i = 0;
      int j = localArrayList.size();
      while (i < j)
      {
        VectorDrawable.VObject localVObject = (VectorDrawable.VObject)localArrayList.get(i);
        boolean bool2 = bool1;
        if (localVObject.isStateful()) {
          bool2 = bool1 | localVObject.hasFocusStateSpecified();
        }
        i++;
        bool1 = bool2;
      }
      return bool1;
    }
    
    public void inflate(Resources paramResources, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    {
      paramResources = Drawable.obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.VectorDrawableGroup);
      updateStateFromTypedArray(paramResources);
      paramResources.recycle();
    }
    
    public boolean isStateful()
    {
      return mIsStateful;
    }
    
    public boolean onStateChange(int[] paramArrayOfInt)
    {
      boolean bool1 = false;
      ArrayList localArrayList = mChildren;
      int i = 0;
      int j = localArrayList.size();
      while (i < j)
      {
        VectorDrawable.VObject localVObject = (VectorDrawable.VObject)localArrayList.get(i);
        boolean bool2 = bool1;
        if (localVObject.isStateful()) {
          bool2 = bool1 | localVObject.onStateChange(paramArrayOfInt);
        }
        i++;
        bool1 = bool2;
      }
      return bool1;
    }
    
    public void setPivotX(float paramFloat)
    {
      if (isTreeValid()) {
        VectorDrawable.nSetPivotX(mNativePtr, paramFloat);
      }
    }
    
    public void setPivotY(float paramFloat)
    {
      if (isTreeValid()) {
        VectorDrawable.nSetPivotY(mNativePtr, paramFloat);
      }
    }
    
    public void setRotation(float paramFloat)
    {
      if (isTreeValid()) {
        VectorDrawable.nSetRotation(mNativePtr, paramFloat);
      }
    }
    
    public void setScaleX(float paramFloat)
    {
      if (isTreeValid()) {
        VectorDrawable.nSetScaleX(mNativePtr, paramFloat);
      }
    }
    
    public void setScaleY(float paramFloat)
    {
      if (isTreeValid()) {
        VectorDrawable.nSetScaleY(mNativePtr, paramFloat);
      }
    }
    
    public void setTranslateX(float paramFloat)
    {
      if (isTreeValid()) {
        VectorDrawable.nSetTranslateX(mNativePtr, paramFloat);
      }
    }
    
    public void setTranslateY(float paramFloat)
    {
      if (isTreeValid()) {
        VectorDrawable.nSetTranslateY(mNativePtr, paramFloat);
      }
    }
    
    public void setTree(VirtualRefBasePtr paramVirtualRefBasePtr)
    {
      super.setTree(paramVirtualRefBasePtr);
      for (int i = 0; i < mChildren.size(); i++) {
        ((VectorDrawable.VObject)mChildren.get(i)).setTree(paramVirtualRefBasePtr);
      }
    }
    
    void updateStateFromTypedArray(TypedArray paramTypedArray)
    {
      mChangingConfigurations |= paramTypedArray.getChangingConfigurations();
      mThemeAttrs = paramTypedArray.extractThemeAttrs();
      if (mTransform == null) {
        mTransform = new float[7];
      }
      if (VectorDrawable.nGetGroupProperties(mNativePtr, mTransform, 7))
      {
        float f1 = paramTypedArray.getFloat(5, mTransform[0]);
        float f2 = paramTypedArray.getFloat(1, mTransform[1]);
        float f3 = paramTypedArray.getFloat(2, mTransform[2]);
        float f4 = paramTypedArray.getFloat(3, mTransform[3]);
        float f5 = paramTypedArray.getFloat(4, mTransform[4]);
        float f6 = paramTypedArray.getFloat(6, mTransform[5]);
        float f7 = paramTypedArray.getFloat(7, mTransform[6]);
        paramTypedArray = paramTypedArray.getString(0);
        if (paramTypedArray != null)
        {
          mGroupName = paramTypedArray;
          VectorDrawable.nSetName(mNativePtr, mGroupName);
        }
        VectorDrawable.nUpdateGroupProperties(mNativePtr, f1, f2, f3, f4, f5, f6, f7);
        return;
      }
      throw new RuntimeException("Error: inconsistent property count");
    }
  }
  
  static abstract class VObject
  {
    VirtualRefBasePtr mTreePtr = null;
    
    VObject() {}
    
    abstract void applyTheme(Resources.Theme paramTheme);
    
    abstract boolean canApplyTheme();
    
    abstract long getNativePtr();
    
    abstract int getNativeSize();
    
    abstract Property getProperty(String paramString);
    
    abstract boolean hasFocusStateSpecified();
    
    abstract void inflate(Resources paramResources, AttributeSet paramAttributeSet, Resources.Theme paramTheme);
    
    abstract boolean isStateful();
    
    boolean isTreeValid()
    {
      boolean bool;
      if ((mTreePtr != null) && (mTreePtr.get() != 0L)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    abstract boolean onStateChange(int[] paramArrayOfInt);
    
    void setTree(VirtualRefBasePtr paramVirtualRefBasePtr)
    {
      mTreePtr = paramVirtualRefBasePtr;
    }
  }
  
  static abstract class VPath
    extends VectorDrawable.VObject
  {
    private static final Property<VPath, PathParser.PathData> PATH_DATA = new Property(PathParser.PathData.class, "pathData")
    {
      public PathParser.PathData get(VectorDrawable.VPath paramAnonymousVPath)
      {
        return paramAnonymousVPath.getPathData();
      }
      
      public void set(VectorDrawable.VPath paramAnonymousVPath, PathParser.PathData paramAnonymousPathData)
      {
        paramAnonymousVPath.setPathData(paramAnonymousPathData);
      }
    };
    int mChangingConfigurations;
    protected PathParser.PathData mPathData;
    String mPathName;
    
    public VPath()
    {
      mPathData = null;
    }
    
    public VPath(VPath paramVPath)
    {
      Object localObject = null;
      mPathData = null;
      mPathName = mPathName;
      mChangingConfigurations = mChangingConfigurations;
      if (mPathData == null) {
        paramVPath = localObject;
      } else {
        paramVPath = new PathParser.PathData(mPathData);
      }
      mPathData = paramVPath;
    }
    
    public PathParser.PathData getPathData()
    {
      return mPathData;
    }
    
    public String getPathName()
    {
      return mPathName;
    }
    
    Property getProperty(String paramString)
    {
      if (PATH_DATA.getName().equals(paramString)) {
        return PATH_DATA;
      }
      return null;
    }
    
    public void setPathData(PathParser.PathData paramPathData)
    {
      mPathData.setPathData(paramPathData);
      if (isTreeValid()) {
        VectorDrawable.nSetPathData(getNativePtr(), mPathData.getNativePtr());
      }
    }
  }
  
  static class VectorDrawableState
    extends Drawable.ConstantState
  {
    static final Property<VectorDrawableState, Float> ALPHA = new FloatProperty("alpha")
    {
      public Float get(VectorDrawable.VectorDrawableState paramAnonymousVectorDrawableState)
      {
        return Float.valueOf(paramAnonymousVectorDrawableState.getAlpha());
      }
      
      public void setValue(VectorDrawable.VectorDrawableState paramAnonymousVectorDrawableState, float paramAnonymousFloat)
      {
        paramAnonymousVectorDrawableState.setAlpha(paramAnonymousFloat);
      }
    };
    private static final int NATIVE_ALLOCATION_SIZE = 316;
    private int mAllocationOfAllNodes = 0;
    boolean mAutoMirrored;
    int mBaseHeight = 0;
    int mBaseWidth = 0;
    boolean mCacheDirty;
    boolean mCachedAutoMirrored;
    int[] mCachedThemeAttrs;
    ColorStateList mCachedTint;
    PorterDuff.Mode mCachedTintMode;
    int mChangingConfigurations;
    int mDensity = 160;
    int mLastHWCachePixelCount = 0;
    int mLastSWCachePixelCount = 0;
    VirtualRefBasePtr mNativeTree = null;
    Insets mOpticalInsets = Insets.NONE;
    VectorDrawable.VGroup mRootGroup;
    String mRootName = null;
    int[] mThemeAttrs;
    ColorStateList mTint = null;
    PorterDuff.Mode mTintMode = Drawable.DEFAULT_TINT_MODE;
    final ArrayMap<String, Object> mVGTargetsMap = new ArrayMap();
    float mViewportHeight = 0.0F;
    float mViewportWidth = 0.0F;
    
    public VectorDrawableState(VectorDrawableState paramVectorDrawableState)
    {
      if (paramVectorDrawableState != null)
      {
        mThemeAttrs = mThemeAttrs;
        mChangingConfigurations = mChangingConfigurations;
        mTint = mTint;
        mTintMode = mTintMode;
        mAutoMirrored = mAutoMirrored;
        mRootGroup = new VectorDrawable.VGroup(mRootGroup, mVGTargetsMap);
        createNativeTreeFromCopy(paramVectorDrawableState, mRootGroup);
        mBaseWidth = mBaseWidth;
        mBaseHeight = mBaseHeight;
        setViewportSize(mViewportWidth, mViewportHeight);
        mOpticalInsets = mOpticalInsets;
        mRootName = mRootName;
        mDensity = mDensity;
        if (mRootName != null) {
          mVGTargetsMap.put(mRootName, this);
        }
      }
      else
      {
        mRootGroup = new VectorDrawable.VGroup();
        createNativeTree(mRootGroup);
      }
      onTreeConstructionFinished();
    }
    
    private void applyDensityScaling(int paramInt1, int paramInt2)
    {
      mBaseWidth = Drawable.scaleFromDensity(mBaseWidth, paramInt1, paramInt2, true);
      mBaseHeight = Drawable.scaleFromDensity(mBaseHeight, paramInt1, paramInt2, true);
      mOpticalInsets = Insets.of(Drawable.scaleFromDensity(mOpticalInsets.left, paramInt1, paramInt2, false), Drawable.scaleFromDensity(mOpticalInsets.top, paramInt1, paramInt2, false), Drawable.scaleFromDensity(mOpticalInsets.right, paramInt1, paramInt2, false), Drawable.scaleFromDensity(mOpticalInsets.bottom, paramInt1, paramInt2, false));
    }
    
    private void createNativeTree(VectorDrawable.VGroup paramVGroup)
    {
      mNativeTree = new VirtualRefBasePtr(VectorDrawable.nCreateTree(mNativePtr));
      VMRuntime.getRuntime().registerNativeAllocation(316);
    }
    
    private void createNativeTreeFromCopy(VectorDrawableState paramVectorDrawableState, VectorDrawable.VGroup paramVGroup)
    {
      mNativeTree = new VirtualRefBasePtr(VectorDrawable.nCreateTreeFromCopy(mNativeTree.get(), mNativePtr));
      VMRuntime.getRuntime().registerNativeAllocation(316);
    }
    
    public void applyTheme(Resources.Theme paramTheme)
    {
      mRootGroup.applyTheme(paramTheme);
    }
    
    public boolean canApplyTheme()
    {
      boolean bool;
      if ((mThemeAttrs == null) && ((mRootGroup == null) || (!mRootGroup.canApplyTheme())) && ((mTint == null) || (!mTint.canApplyTheme())) && (!super.canApplyTheme())) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public boolean canReuseCache()
    {
      if ((!mCacheDirty) && (mCachedThemeAttrs == mThemeAttrs) && (mCachedTint == mTint) && (mCachedTintMode == mTintMode) && (mCachedAutoMirrored == mAutoMirrored)) {
        return true;
      }
      updateCacheStates();
      return false;
    }
    
    public void finalize()
      throws Throwable
    {
      super.finalize();
      int i = mLastHWCachePixelCount;
      int j = mLastSWCachePixelCount;
      VMRuntime.getRuntime().registerNativeFree(316 + mAllocationOfAllNodes + (i * 4 + j * 4));
    }
    
    public float getAlpha()
    {
      return VectorDrawable.nGetRootAlpha(mNativeTree.get());
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
    
    long getNativeRenderer()
    {
      if (mNativeTree == null) {
        return 0L;
      }
      return mNativeTree.get();
    }
    
    Property getProperty(String paramString)
    {
      if (ALPHA.getName().equals(paramString)) {
        return ALPHA;
      }
      return null;
    }
    
    public boolean hasFocusStateSpecified()
    {
      boolean bool;
      if (((mTint != null) && (mTint.hasFocusStateSpecified())) || ((mRootGroup != null) && (mRootGroup.hasFocusStateSpecified()))) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isStateful()
    {
      boolean bool;
      if (((mTint != null) && (mTint.isStateful())) || ((mRootGroup != null) && (mRootGroup.isStateful()))) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public Drawable newDrawable()
    {
      return new VectorDrawable(this, null, null);
    }
    
    public Drawable newDrawable(Resources paramResources)
    {
      return new VectorDrawable(this, paramResources, null);
    }
    
    public boolean onStateChange(int[] paramArrayOfInt)
    {
      return mRootGroup.onStateChange(paramArrayOfInt);
    }
    
    void onTreeConstructionFinished()
    {
      mRootGroup.setTree(mNativeTree);
      mAllocationOfAllNodes = mRootGroup.getNativeSize();
      VMRuntime.getRuntime().registerNativeAllocation(mAllocationOfAllNodes);
    }
    
    public boolean setAlpha(float paramFloat)
    {
      return VectorDrawable.nSetRootAlpha(mNativeTree.get(), paramFloat);
    }
    
    public final boolean setDensity(int paramInt)
    {
      if (mDensity != paramInt)
      {
        int i = mDensity;
        mDensity = paramInt;
        applyDensityScaling(i, paramInt);
        return true;
      }
      return false;
    }
    
    void setViewportSize(float paramFloat1, float paramFloat2)
    {
      mViewportWidth = paramFloat1;
      mViewportHeight = paramFloat2;
      VectorDrawable.nSetRendererViewportSize(getNativeRenderer(), paramFloat1, paramFloat2);
    }
    
    public void updateCacheStates()
    {
      mCachedThemeAttrs = mThemeAttrs;
      mCachedTint = mTint;
      mCachedTintMode = mTintMode;
      mCachedAutoMirrored = mAutoMirrored;
      mCacheDirty = false;
    }
  }
}
