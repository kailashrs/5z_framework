package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import com.android.internal.R.styleable;
import java.io.IOException;
import java.util.Arrays;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class RippleDrawable
  extends LayerDrawable
{
  private static final int MASK_CONTENT = 1;
  private static final int MASK_EXPLICIT = 2;
  private static final int MASK_NONE = 0;
  private static final int MASK_UNKNOWN = -1;
  private static final int MAX_RIPPLES = 10;
  public static final int RADIUS_AUTO = -1;
  private RippleBackground mBackground;
  private int mDensity;
  private final Rect mDirtyBounds = new Rect();
  private final Rect mDrawingBounds = new Rect();
  private RippleForeground[] mExitingRipples;
  private int mExitingRipplesCount = 0;
  private boolean mForceSoftware;
  private boolean mHasPending;
  private boolean mHasValidMask;
  private final Rect mHotspotBounds = new Rect();
  private Drawable mMask;
  private Bitmap mMaskBuffer;
  private Canvas mMaskCanvas;
  private PorterDuffColorFilter mMaskColorFilter;
  private Matrix mMaskMatrix;
  private BitmapShader mMaskShader;
  private boolean mOverrideBounds;
  private float mPendingX;
  private float mPendingY;
  private RippleForeground mRipple;
  private boolean mRippleActive;
  private Paint mRipplePaint;
  private RippleState mState;
  private final Rect mTempRect = new Rect();
  
  RippleDrawable()
  {
    this(new RippleState(null, null, null), null);
  }
  
  public RippleDrawable(ColorStateList paramColorStateList, Drawable paramDrawable1, Drawable paramDrawable2)
  {
    this(new RippleState(null, null, null), null);
    if (paramColorStateList != null)
    {
      if (paramDrawable1 != null) {
        addLayer(paramDrawable1, null, 0, 0, 0, 0, 0);
      }
      if (paramDrawable2 != null) {
        addLayer(paramDrawable2, null, 16908334, 0, 0, 0, 0);
      }
      setColor(paramColorStateList);
      ensurePadding();
      refreshPadding();
      updateLocalState();
      return;
    }
    throw new IllegalArgumentException("RippleDrawable requires a non-null color");
  }
  
  private RippleDrawable(RippleState paramRippleState, Resources paramResources)
  {
    mState = new RippleState(paramRippleState, this, paramResources);
    mLayerState = mState;
    mDensity = Drawable.resolveDensity(paramResources, mState.mDensity);
    if (mState.mNumChildren > 0)
    {
      ensurePadding();
      refreshPadding();
    }
    updateLocalState();
  }
  
  private void cancelExitingRipples()
  {
    int i = mExitingRipplesCount;
    RippleForeground[] arrayOfRippleForeground = mExitingRipples;
    for (int j = 0; j < i; j++) {
      arrayOfRippleForeground[j].end();
    }
    if (arrayOfRippleForeground != null) {
      Arrays.fill(arrayOfRippleForeground, 0, i, null);
    }
    mExitingRipplesCount = 0;
    invalidateSelf(false);
  }
  
  private void clearHotspots()
  {
    if (mRipple != null)
    {
      mRipple.end();
      mRipple = null;
      mRippleActive = false;
    }
    if (mBackground != null) {
      mBackground.setState(false, false, false);
    }
    cancelExitingRipples();
  }
  
  private void drawBackgroundAndRipples(Canvas paramCanvas)
  {
    RippleForeground localRippleForeground = mRipple;
    Object localObject = mBackground;
    int i = mExitingRipplesCount;
    if ((localRippleForeground == null) && (i <= 0) && ((localObject == null) || (!((RippleBackground)localObject).isVisible()))) {
      return;
    }
    float f1 = mHotspotBounds.exactCenterX();
    float f2 = mHotspotBounds.exactCenterY();
    paramCanvas.translate(f1, f2);
    Paint localPaint = getRipplePaint();
    if ((localObject != null) && (((RippleBackground)localObject).isVisible())) {
      ((RippleBackground)localObject).draw(paramCanvas, localPaint);
    }
    if (i > 0)
    {
      localObject = mExitingRipples;
      for (int j = 0; j < i; j++) {
        localObject[j].draw(paramCanvas, localPaint);
      }
    }
    if (localRippleForeground != null) {
      localRippleForeground.draw(paramCanvas, localPaint);
    }
    paramCanvas.translate(-f1, -f2);
  }
  
  private void drawContent(Canvas paramCanvas)
  {
    LayerDrawable.ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    int i = mLayerState.mNumChildren;
    for (int j = 0; j < i; j++) {
      if (mId != 16908334) {
        mDrawable.draw(paramCanvas);
      }
    }
  }
  
  private void drawMask(Canvas paramCanvas)
  {
    mMask.draw(paramCanvas);
  }
  
  private int getMaskType()
  {
    if ((mRipple == null) && (mExitingRipplesCount <= 0) && ((mBackground == null) || (!mBackground.isVisible()))) {
      return -1;
    }
    if (mMask != null)
    {
      if (mMask.getOpacity() == -1) {
        return 0;
      }
      return 2;
    }
    LayerDrawable.ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    int i = mLayerState.mNumChildren;
    for (int j = 0; j < i; j++) {
      if (mDrawable.getOpacity() != -1) {
        return 1;
      }
    }
    return 0;
  }
  
  private boolean isBounded()
  {
    boolean bool;
    if (getNumberOfLayers() > 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void onHotspotBoundsChanged()
  {
    int i = mExitingRipplesCount;
    RippleForeground[] arrayOfRippleForeground = mExitingRipples;
    for (int j = 0; j < i; j++) {
      arrayOfRippleForeground[j].onHotspotBoundsChanged();
    }
    if (mRipple != null) {
      mRipple.onHotspotBoundsChanged();
    }
    if (mBackground != null) {
      mBackground.onHotspotBoundsChanged();
    }
  }
  
  private void pruneRipples()
  {
    int i = 0;
    RippleForeground[] arrayOfRippleForeground = mExitingRipples;
    int j = mExitingRipplesCount;
    int k = 0;
    while (k < j)
    {
      int m = i;
      if (!arrayOfRippleForeground[k].hasFinishedExit())
      {
        arrayOfRippleForeground[i] = arrayOfRippleForeground[k];
        m = i + 1;
      }
      k++;
      i = m;
    }
    for (k = i; k < j; k++) {
      arrayOfRippleForeground[k] = null;
    }
    mExitingRipplesCount = i;
  }
  
  private void setBackgroundActive(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    if ((mBackground == null) && ((paramBoolean1) || (paramBoolean2)))
    {
      mBackground = new RippleBackground(this, mHotspotBounds, isBounded());
      mBackground.setup(mState.mMaxRadius, mDensity);
    }
    if (mBackground != null) {
      mBackground.setState(paramBoolean2, paramBoolean1, paramBoolean3);
    }
  }
  
  private void setRippleActive(boolean paramBoolean)
  {
    if (mRippleActive != paramBoolean)
    {
      mRippleActive = paramBoolean;
      if (paramBoolean) {
        tryRippleEnter();
      } else {
        tryRippleExit();
      }
    }
  }
  
  private void tryRippleEnter()
  {
    if (mExitingRipplesCount >= 10) {
      return;
    }
    if (mRipple == null)
    {
      float f1;
      if (mHasPending)
      {
        mHasPending = false;
        f1 = mPendingX;
      }
      for (float f2 = mPendingY;; f2 = mHotspotBounds.exactCenterY())
      {
        break;
        f1 = mHotspotBounds.exactCenterX();
      }
      mRipple = new RippleForeground(this, mHotspotBounds, f1, f2, mForceSoftware);
    }
    mRipple.setup(mState.mMaxRadius, mDensity);
    mRipple.enter();
  }
  
  private void tryRippleExit()
  {
    if (mRipple != null)
    {
      if (mExitingRipples == null) {
        mExitingRipples = new RippleForeground[10];
      }
      RippleForeground[] arrayOfRippleForeground = mExitingRipples;
      int i = mExitingRipplesCount;
      mExitingRipplesCount = (i + 1);
      arrayOfRippleForeground[i] = mRipple;
      mRipple.exit();
      mRipple = null;
    }
  }
  
  private void updateLocalState()
  {
    mMask = findDrawableByLayerId(16908334);
  }
  
  private void updateMaskShaderIfNeeded()
  {
    if (mHasValidMask) {
      return;
    }
    int i = getMaskType();
    if (i == -1) {
      return;
    }
    mHasValidMask = true;
    Rect localRect = getBounds();
    if ((i != 0) && (!localRect.isEmpty()))
    {
      if ((mMaskBuffer != null) && (mMaskBuffer.getWidth() == localRect.width()) && (mMaskBuffer.getHeight() == localRect.height()))
      {
        mMaskBuffer.eraseColor(0);
      }
      else
      {
        if (mMaskBuffer != null) {
          mMaskBuffer.recycle();
        }
        mMaskBuffer = Bitmap.createBitmap(localRect.width(), localRect.height(), Bitmap.Config.ALPHA_8);
        mMaskShader = new BitmapShader(mMaskBuffer, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mMaskCanvas = new Canvas(mMaskBuffer);
      }
      if (mMaskMatrix == null) {
        mMaskMatrix = new Matrix();
      } else {
        mMaskMatrix.reset();
      }
      if (mMaskColorFilter == null) {
        mMaskColorFilter = new PorterDuffColorFilter(0, PorterDuff.Mode.SRC_IN);
      }
      int j = left;
      int k = top;
      mMaskCanvas.translate(-j, -k);
      if (i == 2) {
        drawMask(mMaskCanvas);
      } else if (i == 1) {
        drawContent(mMaskCanvas);
      }
      mMaskCanvas.translate(j, k);
      return;
    }
    if (mMaskBuffer != null)
    {
      mMaskBuffer.recycle();
      mMaskBuffer = null;
      mMaskShader = null;
      mMaskCanvas = null;
    }
    mMaskMatrix = null;
    mMaskColorFilter = null;
  }
  
  private void updateStateFromTypedArray(TypedArray paramTypedArray)
    throws XmlPullParserException
  {
    Object localObject = mState;
    mChangingConfigurations |= paramTypedArray.getChangingConfigurations();
    mTouchThemeAttrs = paramTypedArray.extractThemeAttrs();
    localObject = paramTypedArray.getColorStateList(0);
    if (localObject != null) {
      mState.mColor = ((ColorStateList)localObject);
    }
    mState.mMaxRadius = paramTypedArray.getDimensionPixelSize(1, mState.mMaxRadius);
  }
  
  private void verifyRequiredAttributes(TypedArray paramTypedArray)
    throws XmlPullParserException
  {
    if ((mState.mColor == null) && ((mState.mTouchThemeAttrs == null) || (mState.mTouchThemeAttrs[0] == 0)))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramTypedArray.getPositionDescription());
      localStringBuilder.append(": <ripple> requires a valid color attribute");
      throw new XmlPullParserException(localStringBuilder.toString());
    }
  }
  
  /* Error */
  public void applyTheme(Resources.Theme paramTheme)
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokespecial 418	android/graphics/drawable/LayerDrawable:applyTheme	(Landroid/content/res/Resources$Theme;)V
    //   5: aload_0
    //   6: getfield 111	android/graphics/drawable/RippleDrawable:mState	Landroid/graphics/drawable/RippleDrawable$RippleState;
    //   9: astore_2
    //   10: aload_2
    //   11: ifnonnull +4 -> 15
    //   14: return
    //   15: aload_2
    //   16: getfield 383	android/graphics/drawable/RippleDrawable$RippleState:mTouchThemeAttrs	[I
    //   19: ifnull +52 -> 71
    //   22: aload_1
    //   23: aload_2
    //   24: getfield 383	android/graphics/drawable/RippleDrawable$RippleState:mTouchThemeAttrs	[I
    //   27: getstatic 423	com/android/internal/R$styleable:RippleDrawable	[I
    //   30: invokevirtual 429	android/content/res/Resources$Theme:resolveAttributes	([I[I)Landroid/content/res/TypedArray;
    //   33: astore_3
    //   34: aload_0
    //   35: aload_3
    //   36: invokespecial 431	android/graphics/drawable/RippleDrawable:updateStateFromTypedArray	(Landroid/content/res/TypedArray;)V
    //   39: aload_0
    //   40: aload_3
    //   41: invokespecial 433	android/graphics/drawable/RippleDrawable:verifyRequiredAttributes	(Landroid/content/res/TypedArray;)V
    //   44: aload_3
    //   45: invokevirtual 434	android/content/res/TypedArray:recycle	()V
    //   48: goto +23 -> 71
    //   51: astore_1
    //   52: goto +13 -> 65
    //   55: astore 4
    //   57: aload 4
    //   59: invokestatic 438	android/graphics/drawable/RippleDrawable:rethrowAsRuntimeException	(Ljava/lang/Exception;)V
    //   62: goto -18 -> 44
    //   65: aload_3
    //   66: invokevirtual 434	android/content/res/TypedArray:recycle	()V
    //   69: aload_1
    //   70: athrow
    //   71: aload_2
    //   72: getfield 391	android/graphics/drawable/RippleDrawable$RippleState:mColor	Landroid/content/res/ColorStateList;
    //   75: ifnull +25 -> 100
    //   78: aload_2
    //   79: getfield 391	android/graphics/drawable/RippleDrawable$RippleState:mColor	Landroid/content/res/ColorStateList;
    //   82: invokevirtual 443	android/content/res/ColorStateList:canApplyTheme	()Z
    //   85: ifeq +15 -> 100
    //   88: aload_2
    //   89: aload_2
    //   90: getfield 391	android/graphics/drawable/RippleDrawable$RippleState:mColor	Landroid/content/res/ColorStateList;
    //   93: aload_1
    //   94: invokevirtual 447	android/content/res/ColorStateList:obtainForTheme	(Landroid/content/res/Resources$Theme;)Landroid/content/res/ColorStateList;
    //   97: putfield 391	android/graphics/drawable/RippleDrawable$RippleState:mColor	Landroid/content/res/ColorStateList;
    //   100: aload_0
    //   101: invokespecial 87	android/graphics/drawable/RippleDrawable:updateLocalState	()V
    //   104: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	105	0	this	RippleDrawable
    //   0	105	1	paramTheme	Resources.Theme
    //   9	81	2	localRippleState	RippleState
    //   33	33	3	localTypedArray	TypedArray
    //   55	3	4	localXmlPullParserException	XmlPullParserException
    // Exception table:
    //   from	to	target	type
    //   34	44	51	finally
    //   57	62	51	finally
    //   34	44	55	org/xmlpull/v1/XmlPullParserException
  }
  
  public boolean canApplyTheme()
  {
    boolean bool;
    if (((mState != null) && (mState.canApplyTheme())) || (super.canApplyTheme())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  RippleState createConstantState(LayerDrawable.LayerState paramLayerState, Resources paramResources)
  {
    return new RippleState(paramLayerState, this, paramResources);
  }
  
  public void draw(Canvas paramCanvas)
  {
    pruneRipples();
    Rect localRect = getDirtyBounds();
    int i = paramCanvas.save(2);
    if (isBounded()) {
      paramCanvas.clipRect(localRect);
    }
    drawContent(paramCanvas);
    drawBackgroundAndRipples(paramCanvas);
    paramCanvas.restoreToCount(i);
  }
  
  public Drawable.ConstantState getConstantState()
  {
    return mState;
  }
  
  public Rect getDirtyBounds()
  {
    if (!isBounded())
    {
      Rect localRect1 = mDrawingBounds;
      Rect localRect2 = mDirtyBounds;
      localRect2.set(localRect1);
      localRect1.setEmpty();
      int i = (int)mHotspotBounds.exactCenterX();
      int j = (int)mHotspotBounds.exactCenterY();
      Rect localRect3 = mTempRect;
      Object localObject = mExitingRipples;
      int k = mExitingRipplesCount;
      for (int m = 0; m < k; m++)
      {
        localObject[m].getBounds(localRect3);
        localRect3.offset(i, j);
        localRect1.union(localRect3);
      }
      localObject = mBackground;
      if (localObject != null)
      {
        ((RippleBackground)localObject).getBounds(localRect3);
        localRect3.offset(i, j);
        localRect1.union(localRect3);
      }
      localRect2.union(localRect1);
      localRect2.union(super.getDirtyBounds());
      return localRect2;
    }
    return getBounds();
  }
  
  public void getHotspotBounds(Rect paramRect)
  {
    paramRect.set(mHotspotBounds);
  }
  
  public int getOpacity()
  {
    return -3;
  }
  
  public void getOutline(Outline paramOutline)
  {
    LayerDrawable.LayerState localLayerState = mLayerState;
    LayerDrawable.ChildDrawable[] arrayOfChildDrawable = mChildren;
    int i = mNumChildren;
    for (int j = 0; j < i; j++) {
      if (mId != 16908334)
      {
        mDrawable.getOutline(paramOutline);
        if (!paramOutline.isEmpty()) {
          return;
        }
      }
    }
  }
  
  public int getRadius()
  {
    return mState.mMaxRadius;
  }
  
  Paint getRipplePaint()
  {
    if (mRipplePaint == null)
    {
      mRipplePaint = new Paint();
      mRipplePaint.setAntiAlias(true);
      mRipplePaint.setStyle(Paint.Style.FILL);
    }
    float f1 = mHotspotBounds.exactCenterX();
    float f2 = mHotspotBounds.exactCenterY();
    updateMaskShaderIfNeeded();
    if (mMaskShader != null)
    {
      localObject = getBounds();
      mMaskMatrix.setTranslate(left - f1, top - f2);
      mMaskShader.setLocalMatrix(mMaskMatrix);
    }
    int i = mState.mColor.getColorForState(getState(), -16777216);
    int j = i;
    if (Color.alpha(i) > 128) {
      j = 0xFFFFFF & i | 0x80000000;
    }
    Object localObject = mRipplePaint;
    if (mMaskColorFilter != null)
    {
      mMaskColorFilter.setColor(j | 0xFF000000);
      ((Paint)localObject).setColor(0xFF000000 & j);
      ((Paint)localObject).setColorFilter(mMaskColorFilter);
      ((Paint)localObject).setShader(mMaskShader);
    }
    else
    {
      ((Paint)localObject).setColor(j);
      ((Paint)localObject).setColorFilter(null);
      ((Paint)localObject).setShader(null);
    }
    return localObject;
  }
  
  public boolean hasFocusStateSpecified()
  {
    return true;
  }
  
  public void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    TypedArray localTypedArray = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.RippleDrawable);
    setPaddingMode(1);
    super.inflate(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
    updateStateFromTypedArray(localTypedArray);
    verifyRequiredAttributes(localTypedArray);
    localTypedArray.recycle();
    updateLocalState();
  }
  
  public void invalidateSelf()
  {
    invalidateSelf(true);
  }
  
  void invalidateSelf(boolean paramBoolean)
  {
    super.invalidateSelf();
    if (paramBoolean) {
      mHasValidMask = false;
    }
  }
  
  public boolean isProjected()
  {
    if (isBounded()) {
      return false;
    }
    int i = mState.mMaxRadius;
    Rect localRect1 = getBounds();
    Rect localRect2 = mHotspotBounds;
    return (i == -1) || (i > localRect2.width() / 2) || (i > localRect2.height() / 2) || ((!localRect1.equals(localRect2)) && (!localRect1.contains(localRect2)));
  }
  
  public boolean isStateful()
  {
    return true;
  }
  
  public void jumpToCurrentState()
  {
    super.jumpToCurrentState();
    if (mRipple != null) {
      mRipple.end();
    }
    if (mBackground != null) {
      mBackground.jumpToFinal();
    }
    cancelExitingRipples();
  }
  
  public Drawable mutate()
  {
    super.mutate();
    mState = ((RippleState)mLayerState);
    mMask = findDrawableByLayerId(16908334);
    return this;
  }
  
  protected void onBoundsChange(Rect paramRect)
  {
    super.onBoundsChange(paramRect);
    if (!mOverrideBounds)
    {
      mHotspotBounds.set(paramRect);
      onHotspotBoundsChanged();
    }
    int i = mExitingRipplesCount;
    paramRect = mExitingRipples;
    for (int j = 0; j < i; j++) {
      paramRect[j].onBoundsChange();
    }
    if (mBackground != null) {
      mBackground.onBoundsChange();
    }
    if (mRipple != null) {
      mRipple.onBoundsChange();
    }
    invalidateSelf();
  }
  
  protected boolean onStateChange(int[] paramArrayOfInt)
  {
    boolean bool1 = super.onStateChange(paramArrayOfInt);
    int i = paramArrayOfInt.length;
    boolean bool2 = false;
    boolean bool3 = false;
    boolean bool4 = false;
    boolean bool5 = false;
    int j = 0;
    int k = 0;
    while (k < i)
    {
      int m = paramArrayOfInt[k];
      int n;
      boolean bool7;
      if (m == 16842910)
      {
        n = 1;
        bool6 = bool5;
        bool7 = bool4;
      }
      else if (m == 16842908)
      {
        bool7 = true;
        n = j;
        bool6 = bool5;
      }
      else if (m == 16842919)
      {
        bool6 = true;
        n = j;
        bool7 = bool4;
      }
      else
      {
        n = j;
        bool6 = bool5;
        bool7 = bool4;
        if (m == 16843623)
        {
          bool3 = true;
          bool7 = bool4;
          bool6 = bool5;
          n = j;
        }
      }
      k++;
      j = n;
      bool5 = bool6;
      bool4 = bool7;
    }
    boolean bool6 = bool2;
    if (j != 0)
    {
      bool6 = bool2;
      if (bool5) {
        bool6 = true;
      }
    }
    setRippleActive(bool6);
    setBackgroundActive(bool3, bool4, bool5);
    return bool1;
  }
  
  public void setColor(ColorStateList paramColorStateList)
  {
    mState.mColor = paramColorStateList;
    invalidateSelf(false);
  }
  
  public boolean setDrawableByLayerId(int paramInt, Drawable paramDrawable)
  {
    if (super.setDrawableByLayerId(paramInt, paramDrawable))
    {
      if (paramInt == 16908334)
      {
        mMask = paramDrawable;
        mHasValidMask = false;
      }
      return true;
    }
    return false;
  }
  
  public void setForceSoftware(boolean paramBoolean)
  {
    mForceSoftware = paramBoolean;
  }
  
  public void setHotspot(float paramFloat1, float paramFloat2)
  {
    if ((mRipple == null) || (mBackground == null))
    {
      mPendingX = paramFloat1;
      mPendingY = paramFloat2;
      mHasPending = true;
    }
    if (mRipple != null) {
      mRipple.move(paramFloat1, paramFloat2);
    }
  }
  
  public void setHotspotBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mOverrideBounds = true;
    mHotspotBounds.set(paramInt1, paramInt2, paramInt3, paramInt4);
    onHotspotBoundsChanged();
  }
  
  public void setPaddingMode(int paramInt)
  {
    super.setPaddingMode(paramInt);
  }
  
  public void setRadius(int paramInt)
  {
    mState.mMaxRadius = paramInt;
    invalidateSelf(false);
  }
  
  public boolean setVisible(boolean paramBoolean1, boolean paramBoolean2)
  {
    paramBoolean2 = super.setVisible(paramBoolean1, paramBoolean2);
    if (!paramBoolean1)
    {
      clearHotspots();
    }
    else if (paramBoolean2)
    {
      if (mRippleActive) {
        tryRippleEnter();
      }
      jumpToCurrentState();
    }
    return paramBoolean2;
  }
  
  static class RippleState
    extends LayerDrawable.LayerState
  {
    ColorStateList mColor = ColorStateList.valueOf(-65281);
    int mMaxRadius = -1;
    int[] mTouchThemeAttrs;
    
    public RippleState(LayerDrawable.LayerState paramLayerState, RippleDrawable paramRippleDrawable, Resources paramResources)
    {
      super(paramRippleDrawable, paramResources);
      if ((paramLayerState != null) && ((paramLayerState instanceof RippleState)))
      {
        paramRippleDrawable = (RippleState)paramLayerState;
        mTouchThemeAttrs = mTouchThemeAttrs;
        mColor = mColor;
        mMaxRadius = mMaxRadius;
        if (mDensity != mDensity) {
          applyDensityScaling(mDensity, mDensity);
        }
      }
    }
    
    private void applyDensityScaling(int paramInt1, int paramInt2)
    {
      if (mMaxRadius != -1) {
        mMaxRadius = Drawable.scaleFromDensity(mMaxRadius, paramInt1, paramInt2, true);
      }
    }
    
    public boolean canApplyTheme()
    {
      boolean bool;
      if ((mTouchThemeAttrs == null) && ((mColor == null) || (!mColor.canApplyTheme())) && (!super.canApplyTheme())) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public int getChangingConfigurations()
    {
      int i = super.getChangingConfigurations();
      int j;
      if (mColor != null) {
        j = mColor.getChangingConfigurations();
      } else {
        j = 0;
      }
      return i | j;
    }
    
    public Drawable newDrawable()
    {
      return new RippleDrawable(this, null, null);
    }
    
    public Drawable newDrawable(Resources paramResources)
    {
      return new RippleDrawable(this, paramResources, null);
    }
    
    protected void onDensityChanged(int paramInt1, int paramInt2)
    {
      super.onDensityChanged(paramInt1, paramInt2);
      applyDensityScaling(paramInt1, paramInt2);
    }
  }
}
