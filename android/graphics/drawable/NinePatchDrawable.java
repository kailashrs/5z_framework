package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ImageDecoder;
import android.graphics.ImageDecoder.OnHeaderDecodedListener;
import android.graphics.ImageDecoder.Source;
import android.graphics.Insets;
import android.graphics.NinePatch;
import android.graphics.NinePatch.InsetStruct;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import com.android.internal.R.styleable;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class NinePatchDrawable
  extends Drawable
{
  private static final boolean DEFAULT_DITHER = false;
  private int mBitmapHeight = -1;
  private int mBitmapWidth = -1;
  private boolean mMutated;
  private NinePatchState mNinePatchState;
  private Insets mOpticalInsets = Insets.NONE;
  private Rect mOutlineInsets;
  private float mOutlineRadius;
  private Rect mPadding;
  private Paint mPaint;
  private int mTargetDensity = 160;
  private Rect mTempRect;
  private PorterDuffColorFilter mTintFilter;
  
  NinePatchDrawable()
  {
    mNinePatchState = new NinePatchState();
  }
  
  public NinePatchDrawable(Resources paramResources, Bitmap paramBitmap, byte[] paramArrayOfByte, Rect paramRect1, Rect paramRect2, String paramString)
  {
    this(new NinePatchState(new NinePatch(paramBitmap, paramArrayOfByte, paramString), paramRect1, paramRect2), paramResources);
  }
  
  public NinePatchDrawable(Resources paramResources, Bitmap paramBitmap, byte[] paramArrayOfByte, Rect paramRect, String paramString)
  {
    this(new NinePatchState(new NinePatch(paramBitmap, paramArrayOfByte, paramString), paramRect), paramResources);
  }
  
  public NinePatchDrawable(Resources paramResources, NinePatch paramNinePatch)
  {
    this(new NinePatchState(paramNinePatch, new Rect()), paramResources);
  }
  
  @Deprecated
  public NinePatchDrawable(Bitmap paramBitmap, byte[] paramArrayOfByte, Rect paramRect, String paramString)
  {
    this(new NinePatchState(new NinePatch(paramBitmap, paramArrayOfByte, paramString), paramRect), null);
  }
  
  @Deprecated
  public NinePatchDrawable(NinePatch paramNinePatch)
  {
    this(new NinePatchState(paramNinePatch, new Rect()), null);
  }
  
  private NinePatchDrawable(NinePatchState paramNinePatchState, Resources paramResources)
  {
    mNinePatchState = paramNinePatchState;
    updateLocalState(paramResources);
  }
  
  private void computeBitmapSize()
  {
    Object localObject1 = mNinePatchState.mNinePatch;
    if (localObject1 == null) {
      return;
    }
    int i = mTargetDensity;
    int j;
    if (((NinePatch)localObject1).getDensity() == 0) {
      j = i;
    } else {
      j = ((NinePatch)localObject1).getDensity();
    }
    Object localObject2 = mNinePatchState.mOpticalInsets;
    if (localObject2 != Insets.NONE) {
      mOpticalInsets = Insets.of(Drawable.scaleFromDensity(left, j, i, true), Drawable.scaleFromDensity(top, j, i, true), Drawable.scaleFromDensity(right, j, i, true), Drawable.scaleFromDensity(bottom, j, i, true));
    } else {
      mOpticalInsets = Insets.NONE;
    }
    localObject2 = mNinePatchState.mPadding;
    if (localObject2 != null)
    {
      if (mPadding == null) {
        mPadding = new Rect();
      }
      mPadding.left = Drawable.scaleFromDensity(left, j, i, true);
      mPadding.top = Drawable.scaleFromDensity(top, j, i, true);
      mPadding.right = Drawable.scaleFromDensity(right, j, i, true);
      mPadding.bottom = Drawable.scaleFromDensity(bottom, j, i, true);
    }
    else
    {
      mPadding = null;
    }
    mBitmapHeight = Drawable.scaleFromDensity(((NinePatch)localObject1).getHeight(), j, i, true);
    mBitmapWidth = Drawable.scaleFromDensity(((NinePatch)localObject1).getWidth(), j, i, true);
    localObject2 = ((NinePatch)localObject1).getBitmap().getNinePatchInsets();
    if (localObject2 != null)
    {
      localObject1 = outlineRect;
      mOutlineInsets = NinePatch.InsetStruct.scaleInsets(left, top, right, bottom, i / j);
      mOutlineRadius = Drawable.scaleFromDensity(outlineRadius, j, i);
    }
    else
    {
      mOutlineInsets = null;
    }
  }
  
  private boolean needsMirroring()
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
    NinePatchState localNinePatchState = mNinePatchState;
    if (mDither) {
      setDither(mDither);
    }
    if ((paramResources == null) && (mNinePatch != null)) {
      mTargetDensity = mNinePatch.getDensity();
    } else {
      mTargetDensity = Drawable.resolveDensity(paramResources, mTargetDensity);
    }
    mTintFilter = updateTintFilter(mTintFilter, mTint, mTintMode);
    computeBitmapSize();
  }
  
  private void updateStateFromTypedArray(TypedArray paramTypedArray)
    throws XmlPullParserException
  {
    Object localObject1 = paramTypedArray.getResources();
    NinePatchState localNinePatchState = mNinePatchState;
    mChangingConfigurations |= paramTypedArray.getChangingConfigurations();
    mThemeAttrs = paramTypedArray.extractThemeAttrs();
    mDither = paramTypedArray.getBoolean(1, mDither);
    int i = paramTypedArray.getResourceId(0, 0);
    if (i != 0)
    {
      Rect localRect1 = new Rect();
      Rect localRect2 = new Rect();
      Bitmap localBitmap = null;
      Object localObject2 = localBitmap;
      try
      {
        Object localObject3 = new android/util/TypedValue;
        localObject2 = localBitmap;
        ((TypedValue)localObject3).<init>();
        localObject2 = localBitmap;
        InputStream localInputStream = ((Resources)localObject1).openRawResource(i, (TypedValue)localObject3);
        i = 0;
        localObject2 = localBitmap;
        if (density == 0)
        {
          i = 160;
        }
        else
        {
          localObject2 = localBitmap;
          if (density != 65535)
          {
            localObject2 = localBitmap;
            i = density;
          }
        }
        localObject2 = localBitmap;
        localObject3 = ImageDecoder.createSource((Resources)localObject1, localInputStream, i);
        localObject2 = localBitmap;
        localObject1 = new android/graphics/drawable/_$$Lambda$NinePatchDrawable$yQvfm7FAkslD5wdGFysjgwt8cLE;
        localObject2 = localBitmap;
        ((_..Lambda.NinePatchDrawable.yQvfm7FAkslD5wdGFysjgwt8cLE)localObject1).<init>(localRect1);
        localObject2 = localBitmap;
        localBitmap = ImageDecoder.decodeBitmap((ImageDecoder.Source)localObject3, (ImageDecoder.OnHeaderDecodedListener)localObject1);
        localObject2 = localBitmap;
        localInputStream.close();
        localObject2 = localBitmap;
      }
      catch (IOException localIOException) {}
      if (localObject2 != null)
      {
        if (((Bitmap)localObject2).getNinePatchChunk() != null)
        {
          ((Bitmap)localObject2).getOpticalInsets(localRect2);
          mNinePatch = new NinePatch((Bitmap)localObject2, ((Bitmap)localObject2).getNinePatchChunk());
          mPadding = localRect1;
          mOpticalInsets = Insets.of(localRect2);
        }
        else
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append(paramTypedArray.getPositionDescription());
          ((StringBuilder)localObject2).append(": <nine-patch> requires a valid 9-patch source image");
          throw new XmlPullParserException(((StringBuilder)localObject2).toString());
        }
      }
      else
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append(paramTypedArray.getPositionDescription());
        ((StringBuilder)localObject2).append(": <nine-patch> requires a valid src attribute");
        throw new XmlPullParserException(((StringBuilder)localObject2).toString());
      }
    }
    mAutoMirrored = paramTypedArray.getBoolean(4, mAutoMirrored);
    mBaseAlpha = paramTypedArray.getFloat(3, mBaseAlpha);
    i = paramTypedArray.getInt(5, -1);
    if (i != -1) {
      mTintMode = Drawable.parseTintMode(i, PorterDuff.Mode.SRC_IN);
    }
    paramTypedArray = paramTypedArray.getColorStateList(2);
    if (paramTypedArray != null) {
      mTint = paramTypedArray;
    }
  }
  
  /* Error */
  public void applyTheme(Resources.Theme paramTheme)
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokespecial 329	android/graphics/drawable/Drawable:applyTheme	(Landroid/content/res/Resources$Theme;)V
    //   5: aload_0
    //   6: getfield 51	android/graphics/drawable/NinePatchDrawable:mNinePatchState	Landroid/graphics/drawable/NinePatchDrawable$NinePatchState;
    //   9: astore_2
    //   10: aload_2
    //   11: ifnonnull +4 -> 15
    //   14: return
    //   15: aload_2
    //   16: getfield 226	android/graphics/drawable/NinePatchDrawable$NinePatchState:mThemeAttrs	[I
    //   19: ifnull +47 -> 66
    //   22: aload_1
    //   23: aload_2
    //   24: getfield 226	android/graphics/drawable/NinePatchDrawable$NinePatchState:mThemeAttrs	[I
    //   27: getstatic 334	com/android/internal/R$styleable:NinePatchDrawable	[I
    //   30: invokevirtual 340	android/content/res/Resources$Theme:resolveAttributes	([I[I)Landroid/content/res/TypedArray;
    //   33: astore_3
    //   34: aload_0
    //   35: aload_3
    //   36: invokespecial 342	android/graphics/drawable/NinePatchDrawable:updateStateFromTypedArray	(Landroid/content/res/TypedArray;)V
    //   39: aload_3
    //   40: invokevirtual 345	android/content/res/TypedArray:recycle	()V
    //   43: goto +23 -> 66
    //   46: astore_1
    //   47: goto +13 -> 60
    //   50: astore 4
    //   52: aload 4
    //   54: invokestatic 349	android/graphics/drawable/NinePatchDrawable:rethrowAsRuntimeException	(Ljava/lang/Exception;)V
    //   57: goto -18 -> 39
    //   60: aload_3
    //   61: invokevirtual 345	android/content/res/TypedArray:recycle	()V
    //   64: aload_1
    //   65: athrow
    //   66: aload_2
    //   67: getfield 190	android/graphics/drawable/NinePatchDrawable$NinePatchState:mTint	Landroid/content/res/ColorStateList;
    //   70: ifnull +25 -> 95
    //   73: aload_2
    //   74: getfield 190	android/graphics/drawable/NinePatchDrawable$NinePatchState:mTint	Landroid/content/res/ColorStateList;
    //   77: invokevirtual 354	android/content/res/ColorStateList:canApplyTheme	()Z
    //   80: ifeq +15 -> 95
    //   83: aload_2
    //   84: aload_2
    //   85: getfield 190	android/graphics/drawable/NinePatchDrawable$NinePatchState:mTint	Landroid/content/res/ColorStateList;
    //   88: aload_1
    //   89: invokevirtual 358	android/content/res/ColorStateList:obtainForTheme	(Landroid/content/res/Resources$Theme;)Landroid/content/res/ColorStateList;
    //   92: putfield 190	android/graphics/drawable/NinePatchDrawable$NinePatchState:mTint	Landroid/content/res/ColorStateList;
    //   95: aload_0
    //   96: aload_1
    //   97: invokevirtual 359	android/content/res/Resources$Theme:getResources	()Landroid/content/res/Resources;
    //   100: invokespecial 80	android/graphics/drawable/NinePatchDrawable:updateLocalState	(Landroid/content/res/Resources;)V
    //   103: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	104	0	this	NinePatchDrawable
    //   0	104	1	paramTheme	Resources.Theme
    //   9	76	2	localNinePatchState	NinePatchState
    //   33	28	3	localTypedArray	TypedArray
    //   50	3	4	localXmlPullParserException	XmlPullParserException
    // Exception table:
    //   from	to	target	type
    //   34	39	46	finally
    //   52	57	46	finally
    //   34	39	50	org/xmlpull/v1/XmlPullParserException
  }
  
  public boolean canApplyTheme()
  {
    boolean bool;
    if ((mNinePatchState != null) && (mNinePatchState.canApplyTheme())) {
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
    NinePatchState localNinePatchState = mNinePatchState;
    Rect localRect = getBounds();
    int i = -1;
    Object localObject = mTintFilter;
    int j = 0;
    int k;
    if ((localObject != null) && (getPaint().getColorFilter() == null))
    {
      mPaint.setColorFilter(mTintFilter);
      k = 1;
    }
    else
    {
      k = 0;
    }
    int m;
    if (mBaseAlpha != 1.0F)
    {
      m = getPaint().getAlpha();
      mPaint.setAlpha((int)(m * mBaseAlpha + 0.5F));
    }
    else
    {
      m = -1;
    }
    int n = j;
    if (paramCanvas.getDensity() == 0)
    {
      n = j;
      if (mNinePatch.getDensity() != 0) {
        n = 1;
      }
    }
    localObject = localRect;
    if (n != 0)
    {
      if (-1 >= 0) {
        i = -1;
      } else {
        i = paramCanvas.save();
      }
      float f = mTargetDensity / mNinePatch.getDensity();
      paramCanvas.scale(f, f, left, top);
      if (mTempRect == null) {
        mTempRect = new Rect();
      }
      localObject = mTempRect;
      left = left;
      top = top;
      right = (left + Math.round(localRect.width() / f));
      bottom = (top + Math.round(localRect.height() / f));
    }
    n = i;
    if (needsMirroring())
    {
      if (i < 0) {
        i = paramCanvas.save();
      }
      paramCanvas.scale(-1.0F, 1.0F, (left + right) / 2.0F, (top + bottom) / 2.0F);
      n = i;
    }
    mNinePatch.draw(paramCanvas, (Rect)localObject, mPaint);
    if (n >= 0) {
      paramCanvas.restoreToCount(n);
    }
    if (k != 0) {
      mPaint.setColorFilter(null);
    }
    if (m >= 0) {
      mPaint.setAlpha(m);
    }
  }
  
  public int getAlpha()
  {
    if (mPaint == null) {
      return 255;
    }
    return getPaint().getAlpha();
  }
  
  public int getChangingConfigurations()
  {
    return super.getChangingConfigurations() | mNinePatchState.getChangingConfigurations();
  }
  
  public Drawable.ConstantState getConstantState()
  {
    mNinePatchState.mChangingConfigurations = getChangingConfigurations();
    return mNinePatchState;
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
    int i;
    if ((!mNinePatchState.mNinePatch.hasAlpha()) && ((mPaint == null) || (mPaint.getAlpha() >= 255))) {
      i = -1;
    } else {
      i = -3;
    }
    return i;
  }
  
  public Insets getOpticalInsets()
  {
    Insets localInsets = mOpticalInsets;
    if (needsMirroring()) {
      return Insets.of(right, top, left, bottom);
    }
    return localInsets;
  }
  
  public void getOutline(Outline paramOutline)
  {
    Rect localRect = getBounds();
    if (localRect.isEmpty()) {
      return;
    }
    if ((mNinePatchState != null) && (mOutlineInsets != null))
    {
      NinePatch.InsetStruct localInsetStruct = mNinePatchState.mNinePatch.getBitmap().getNinePatchInsets();
      if (localInsetStruct != null)
      {
        paramOutline.setRoundRect(left + mOutlineInsets.left, top + mOutlineInsets.top, right - mOutlineInsets.right, bottom - mOutlineInsets.bottom, mOutlineRadius);
        paramOutline.setAlpha(outlineAlpha * (getAlpha() / 255.0F));
        return;
      }
    }
    super.getOutline(paramOutline);
  }
  
  public boolean getPadding(Rect paramRect)
  {
    if (mPadding != null)
    {
      paramRect.set(mPadding);
      boolean bool;
      if ((left | top | right | bottom) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    return super.getPadding(paramRect);
  }
  
  public Paint getPaint()
  {
    if (mPaint == null)
    {
      mPaint = new Paint();
      mPaint.setDither(false);
    }
    return mPaint;
  }
  
  public Region getTransparentRegion()
  {
    return mNinePatchState.mNinePatch.getTransparentRegion(getBounds());
  }
  
  public boolean hasFocusStateSpecified()
  {
    boolean bool;
    if ((mNinePatchState.mTint != null) && (mNinePatchState.mTint.hasFocusStateSpecified())) {
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
    paramXmlPullParser = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.NinePatchDrawable);
    updateStateFromTypedArray(paramXmlPullParser);
    paramXmlPullParser.recycle();
    updateLocalState(paramResources);
  }
  
  public boolean isAutoMirrored()
  {
    return mNinePatchState.mAutoMirrored;
  }
  
  public boolean isFilterBitmap()
  {
    boolean bool;
    if ((mPaint != null) && (getPaint().isFilterBitmap())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isStateful()
  {
    NinePatchState localNinePatchState = mNinePatchState;
    boolean bool;
    if ((!super.isStateful()) && ((mTint == null) || (!mTint.isStateful()))) {
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
      mNinePatchState = new NinePatchState(mNinePatchState);
      mMutated = true;
    }
    return this;
  }
  
  protected boolean onStateChange(int[] paramArrayOfInt)
  {
    paramArrayOfInt = mNinePatchState;
    if ((mTint != null) && (mTintMode != null))
    {
      mTintFilter = updateTintFilter(mTintFilter, mTint, mTintMode);
      return true;
    }
    return false;
  }
  
  public void setAlpha(int paramInt)
  {
    if ((mPaint == null) && (paramInt == 255)) {
      return;
    }
    getPaint().setAlpha(paramInt);
    invalidateSelf();
  }
  
  public void setAutoMirrored(boolean paramBoolean)
  {
    mNinePatchState.mAutoMirrored = paramBoolean;
  }
  
  public void setColorFilter(ColorFilter paramColorFilter)
  {
    if ((mPaint == null) && (paramColorFilter == null)) {
      return;
    }
    getPaint().setColorFilter(paramColorFilter);
    invalidateSelf();
  }
  
  public void setDither(boolean paramBoolean)
  {
    if ((mPaint == null) && (!paramBoolean)) {
      return;
    }
    getPaint().setDither(paramBoolean);
    invalidateSelf();
  }
  
  public void setFilterBitmap(boolean paramBoolean)
  {
    getPaint().setFilterBitmap(paramBoolean);
    invalidateSelf();
  }
  
  public void setTargetDensity(int paramInt)
  {
    int i = paramInt;
    if (paramInt == 0) {
      i = 160;
    }
    if (mTargetDensity != i)
    {
      mTargetDensity = i;
      computeBitmapSize();
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
  
  public void setTintList(ColorStateList paramColorStateList)
  {
    mNinePatchState.mTint = paramColorStateList;
    mTintFilter = updateTintFilter(mTintFilter, paramColorStateList, mNinePatchState.mTintMode);
    invalidateSelf();
  }
  
  public void setTintMode(PorterDuff.Mode paramMode)
  {
    mNinePatchState.mTintMode = paramMode;
    mTintFilter = updateTintFilter(mTintFilter, mNinePatchState.mTint, paramMode);
    invalidateSelf();
  }
  
  static final class NinePatchState
    extends Drawable.ConstantState
  {
    boolean mAutoMirrored = false;
    float mBaseAlpha = 1.0F;
    int mChangingConfigurations;
    boolean mDither = false;
    NinePatch mNinePatch = null;
    Insets mOpticalInsets = Insets.NONE;
    Rect mPadding = null;
    int[] mThemeAttrs;
    ColorStateList mTint = null;
    PorterDuff.Mode mTintMode = Drawable.DEFAULT_TINT_MODE;
    
    NinePatchState() {}
    
    NinePatchState(NinePatch paramNinePatch, Rect paramRect)
    {
      this(paramNinePatch, paramRect, null, false, false);
    }
    
    NinePatchState(NinePatch paramNinePatch, Rect paramRect1, Rect paramRect2)
    {
      this(paramNinePatch, paramRect1, paramRect2, false, false);
    }
    
    NinePatchState(NinePatch paramNinePatch, Rect paramRect1, Rect paramRect2, boolean paramBoolean1, boolean paramBoolean2)
    {
      mNinePatch = paramNinePatch;
      mPadding = paramRect1;
      mOpticalInsets = Insets.of(paramRect2);
      mDither = paramBoolean1;
      mAutoMirrored = paramBoolean2;
    }
    
    NinePatchState(NinePatchState paramNinePatchState)
    {
      mChangingConfigurations = mChangingConfigurations;
      mNinePatch = mNinePatch;
      mTint = mTint;
      mTintMode = mTintMode;
      mPadding = mPadding;
      mOpticalInsets = mOpticalInsets;
      mBaseAlpha = mBaseAlpha;
      mDither = mDither;
      mAutoMirrored = mAutoMirrored;
      mThemeAttrs = mThemeAttrs;
    }
    
    public boolean canApplyTheme()
    {
      boolean bool;
      if ((mThemeAttrs == null) && ((mTint == null) || (!mTint.canApplyTheme())) && (!super.canApplyTheme())) {
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
      return new NinePatchDrawable(this, null, null);
    }
    
    public Drawable newDrawable(Resources paramResources)
    {
      return new NinePatchDrawable(this, paramResources, null);
    }
  }
}
