package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.util.PathParser;
import com.android.internal.R.styleable;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AdaptiveIconDrawable
  extends Drawable
  implements Drawable.Callback
{
  private static final int BACKGROUND_ID = 0;
  private static final float DEFAULT_VIEW_PORT_SCALE = 0.6666667F;
  private static final float EXTRA_INSET_PERCENTAGE = 0.25F;
  private static final int FOREGROUND_ID = 1;
  public static final float MASK_SIZE = 100.0F;
  private static final float SAFEZONE_SCALE = 0.9166667F;
  private static Path sMask;
  private final Canvas mCanvas;
  private boolean mChildRequestedInvalidation;
  private Rect mHotspotBounds;
  LayerState mLayerState = createConstantState(paramLayerState, paramResources);
  private Bitmap mLayersBitmap;
  private Shader mLayersShader;
  private final Path mMask;
  private Bitmap mMaskBitmap;
  private final Matrix mMaskMatrix;
  private boolean mMutated;
  private Paint mPaint = new Paint(7);
  private boolean mSuspendChildInvalidation;
  private final Rect mTmpOutRect = new Rect();
  private final Region mTransparentRegion;
  
  AdaptiveIconDrawable()
  {
    this((LayerState)null, null);
  }
  
  AdaptiveIconDrawable(LayerState paramLayerState, Resources paramResources)
  {
    if (sMask == null) {
      sMask = PathParser.createPathFromPathData(Resources.getSystem().getString(17039724));
    }
    mMask = PathParser.createPathFromPathData(Resources.getSystem().getString(17039724));
    mMaskMatrix = new Matrix();
    mCanvas = new Canvas();
    mTransparentRegion = new Region();
  }
  
  public AdaptiveIconDrawable(Drawable paramDrawable1, Drawable paramDrawable2)
  {
    this((LayerState)null, null);
    if (paramDrawable1 != null) {
      addLayer(0, createChildDrawable(paramDrawable1));
    }
    if (paramDrawable2 != null) {
      addLayer(1, createChildDrawable(paramDrawable2));
    }
  }
  
  private void addLayer(int paramInt, ChildDrawable paramChildDrawable)
  {
    mLayerState.mChildren[paramInt] = paramChildDrawable;
    mLayerState.invalidateCache();
  }
  
  private ChildDrawable createChildDrawable(Drawable paramDrawable)
  {
    ChildDrawable localChildDrawable = new ChildDrawable(mLayerState.mDensity);
    mDrawable = paramDrawable;
    mDrawable.setCallback(this);
    paramDrawable = mLayerState;
    mChildrenChangingConfigurations |= mDrawable.getChangingConfigurations();
    return localChildDrawable;
  }
  
  public static float getExtraInsetFraction()
  {
    return 0.25F;
  }
  
  public static float getExtraInsetPercentage()
  {
    return 0.25F;
  }
  
  private int getMaxIntrinsicHeight()
  {
    int i = -1;
    int j = 0;
    for (;;)
    {
      Object localObject = mLayerState;
      if (j >= 2) {
        break;
      }
      localObject = mLayerState.mChildren[j];
      int k;
      if (mDrawable == null)
      {
        k = i;
      }
      else
      {
        int m = mDrawable.getIntrinsicHeight();
        k = i;
        if (m > i) {
          k = m;
        }
      }
      j++;
      i = k;
    }
    return i;
  }
  
  private int getMaxIntrinsicWidth()
  {
    int i = -1;
    int j = 0;
    for (;;)
    {
      Object localObject = mLayerState;
      if (j >= 2) {
        break;
      }
      localObject = mLayerState.mChildren[j];
      int k;
      if (mDrawable == null)
      {
        k = i;
      }
      else
      {
        int m = mDrawable.getIntrinsicWidth();
        k = i;
        if (m > i) {
          k = m;
        }
      }
      j++;
      i = k;
    }
    return i;
  }
  
  private void inflateLayers(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    LayerState localLayerState = mLayerState;
    int i = paramXmlPullParser.getDepth() + 1;
    for (;;)
    {
      int j = paramXmlPullParser.next();
      if (j == 1) {
        break;
      }
      int k = paramXmlPullParser.getDepth();
      if ((k < i) && (j == 3)) {
        break;
      }
      if (j == 2)
      {
        do
        {
          while (k > i) {}
          localObject = paramXmlPullParser.getName();
          if (((String)localObject).equals("background"))
          {
            k = 0;
            break;
          }
        } while (!((String)localObject).equals("foreground"));
        k = 1;
        Object localObject = new ChildDrawable(mDensity);
        TypedArray localTypedArray = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.AdaptiveIconDrawableLayer);
        updateLayerFromTypedArray((ChildDrawable)localObject, localTypedArray);
        localTypedArray.recycle();
        if ((mDrawable == null) && (mThemeAttrs == null))
        {
          do
          {
            j = paramXmlPullParser.next();
          } while (j == 4);
          if (j == 2)
          {
            mDrawable = Drawable.createFromXmlInnerForDensity(paramResources, paramXmlPullParser, paramAttributeSet, mLayerState.mSrcDensityOverride, paramTheme);
            mDrawable.setCallback(this);
            mChildrenChangingConfigurations |= mDrawable.getChangingConfigurations();
          }
          else
          {
            paramResources = new StringBuilder();
            paramResources.append(paramXmlPullParser.getPositionDescription());
            paramResources.append(": <foreground> or <background> tag requires a 'drawable'attribute or child tag defining a drawable");
            throw new XmlPullParserException(paramResources.toString());
          }
        }
        addLayer(k, (ChildDrawable)localObject);
      }
    }
  }
  
  private void resumeChildInvalidation()
  {
    mSuspendChildInvalidation = false;
    if (mChildRequestedInvalidation)
    {
      mChildRequestedInvalidation = false;
      invalidateSelf();
    }
  }
  
  private void suspendChildInvalidation()
  {
    mSuspendChildInvalidation = true;
  }
  
  private void updateLayerBounds(Rect paramRect)
  {
    if (paramRect.isEmpty()) {
      return;
    }
    try
    {
      suspendChildInvalidation();
      updateLayerBoundsInternal(paramRect);
      updateMaskBoundsInternal(paramRect);
      return;
    }
    finally
    {
      resumeChildInvalidation();
    }
  }
  
  private void updateLayerBoundsInternal(Rect paramRect)
  {
    int i = paramRect.width() / 2;
    int j = paramRect.height() / 2;
    int k = 0;
    Object localObject = mLayerState;
    while (k < 2)
    {
      localObject = mLayerState.mChildren[k];
      if (localObject != null)
      {
        localObject = mDrawable;
        if (localObject != null)
        {
          int m = (int)(paramRect.width() / 1.3333334F);
          int n = (int)(paramRect.height() / 1.3333334F);
          Rect localRect = mTmpOutRect;
          localRect.set(i - m, j - n, i + m, j + n);
          ((Drawable)localObject).setBounds(localRect);
        }
      }
      k++;
    }
  }
  
  private void updateLayerFromTypedArray(ChildDrawable paramChildDrawable, TypedArray paramTypedArray)
  {
    LayerState localLayerState = mLayerState;
    mChildrenChangingConfigurations |= paramTypedArray.getChangingConfigurations();
    mThemeAttrs = paramTypedArray.extractThemeAttrs();
    paramTypedArray = paramTypedArray.getDrawableForDensity(0, mSrcDensityOverride);
    if (paramTypedArray != null)
    {
      if (mDrawable != null) {
        mDrawable.setCallback(null);
      }
      mDrawable = paramTypedArray;
      mDrawable.setCallback(this);
      mChildrenChangingConfigurations |= mDrawable.getChangingConfigurations();
    }
  }
  
  private void updateMaskBoundsInternal(Rect paramRect)
  {
    mMaskMatrix.setScale(paramRect.width() / 100.0F, paramRect.height() / 100.0F);
    sMask.transform(mMaskMatrix, mMask);
    if ((mMaskBitmap == null) || (mMaskBitmap.getWidth() != paramRect.width()) || (mMaskBitmap.getHeight() != paramRect.height()))
    {
      mMaskBitmap = Bitmap.createBitmap(paramRect.width(), paramRect.height(), Bitmap.Config.ALPHA_8);
      mLayersBitmap = Bitmap.createBitmap(paramRect.width(), paramRect.height(), Bitmap.Config.ARGB_8888);
    }
    mCanvas.setBitmap(mMaskBitmap);
    mPaint.setShader(null);
    mCanvas.drawPath(mMask, mPaint);
    mMaskMatrix.postTranslate(left, top);
    mMask.reset();
    sMask.transform(mMaskMatrix, mMask);
    mTransparentRegion.setEmpty();
    mLayersShader = null;
  }
  
  public void applyTheme(Resources.Theme paramTheme)
  {
    super.applyTheme(paramTheme);
    LayerState localLayerState = mLayerState;
    if (localLayerState == null) {
      return;
    }
    Object localObject1 = paramTheme.getResources();
    int i = 0;
    int j = Drawable.resolveDensity((Resources)localObject1, 0);
    localLayerState.setDensity(j);
    localObject1 = mChildren;
    while (i < 2)
    {
      Object localObject2 = localObject1[i];
      ((ChildDrawable)localObject2).setDensity(j);
      if (mThemeAttrs != null)
      {
        TypedArray localTypedArray = paramTheme.resolveAttributes(mThemeAttrs, R.styleable.AdaptiveIconDrawableLayer);
        updateLayerFromTypedArray((ChildDrawable)localObject2, localTypedArray);
        localTypedArray.recycle();
      }
      localObject2 = mDrawable;
      if ((localObject2 != null) && (((Drawable)localObject2).canApplyTheme()))
      {
        ((Drawable)localObject2).applyTheme(paramTheme);
        mChildrenChangingConfigurations |= ((Drawable)localObject2).getChangingConfigurations();
      }
      i++;
    }
  }
  
  public boolean canApplyTheme()
  {
    boolean bool;
    if (((mLayerState != null) && (mLayerState.canApplyTheme())) || (super.canApplyTheme())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void clearMutated()
  {
    super.clearMutated();
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    for (int i = 0;; i++)
    {
      Object localObject = mLayerState;
      if (i >= 2) {
        break;
      }
      localObject = mDrawable;
      if (localObject != null) {
        ((Drawable)localObject).clearMutated();
      }
    }
    mMutated = false;
  }
  
  LayerState createConstantState(LayerState paramLayerState, Resources paramResources)
  {
    return new LayerState(paramLayerState, this, paramResources);
  }
  
  public void draw(Canvas paramCanvas)
  {
    if (mLayersBitmap == null) {
      return;
    }
    Object localObject;
    if (mLayersShader == null)
    {
      mCanvas.setBitmap(mLayersBitmap);
      mCanvas.drawColor(-16777216);
      for (int i = 0;; i++)
      {
        localObject = mLayerState;
        if (i >= 2) {
          break;
        }
        if (mLayerState.mChildren[i] != null)
        {
          localObject = mLayerState.mChildren[i].mDrawable;
          if (localObject != null) {
            ((Drawable)localObject).draw(mCanvas);
          }
        }
      }
      mLayersShader = new BitmapShader(mLayersBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
      mPaint.setShader(mLayersShader);
    }
    if (mMaskBitmap != null)
    {
      localObject = getBounds();
      paramCanvas.drawBitmap(mMaskBitmap, left, top, mPaint);
    }
  }
  
  public int getAlpha()
  {
    return -3;
  }
  
  public Drawable getBackground()
  {
    return mLayerState.mChildren[0].mDrawable;
  }
  
  public int getChangingConfigurations()
  {
    return super.getChangingConfigurations() | mLayerState.getChangingConfigurations();
  }
  
  public Drawable.ConstantState getConstantState()
  {
    if (mLayerState.canConstantState())
    {
      mLayerState.mChangingConfigurations = getChangingConfigurations();
      return mLayerState;
    }
    return null;
  }
  
  public Drawable getForeground()
  {
    return mLayerState.mChildren[1].mDrawable;
  }
  
  public void getHotspotBounds(Rect paramRect)
  {
    if (mHotspotBounds != null) {
      paramRect.set(mHotspotBounds);
    } else {
      super.getHotspotBounds(paramRect);
    }
  }
  
  public Path getIconMask()
  {
    return mMask;
  }
  
  public int getIntrinsicHeight()
  {
    return (int)(getMaxIntrinsicHeight() * 0.6666667F);
  }
  
  public int getIntrinsicWidth()
  {
    return (int)(getMaxIntrinsicWidth() * 0.6666667F);
  }
  
  public int getOpacity()
  {
    if (mLayerState.mOpacityOverride != 0) {
      return mLayerState.mOpacityOverride;
    }
    return mLayerState.getOpacity();
  }
  
  public void getOutline(Outline paramOutline)
  {
    paramOutline.setConvexPath(mMask);
  }
  
  public Region getSafeZone()
  {
    mMaskMatrix.reset();
    mMaskMatrix.setScale(0.9166667F, 0.9166667F, getBounds().centerX(), getBounds().centerY());
    Path localPath = new Path();
    mMask.transform(mMaskMatrix, localPath);
    Region localRegion = new Region(getBounds());
    localRegion.setPath(localPath, localRegion);
    return localRegion;
  }
  
  public Region getTransparentRegion()
  {
    if (mTransparentRegion.isEmpty())
    {
      mMask.toggleInverseFillType();
      mTransparentRegion.set(getBounds());
      mTransparentRegion.setPath(mMask, mTransparentRegion);
      mMask.toggleInverseFillType();
    }
    return mTransparentRegion;
  }
  
  public boolean hasFocusStateSpecified()
  {
    return mLayerState.hasFocusStateSpecified();
  }
  
  public void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    super.inflate(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
    LayerState localLayerState = mLayerState;
    if (localLayerState == null) {
      return;
    }
    int i = 0;
    int j = Drawable.resolveDensity(paramResources, 0);
    localLayerState.setDensity(j);
    mSrcDensityOverride = mSrcDensityOverride;
    ChildDrawable[] arrayOfChildDrawable = mChildren;
    while (i < mChildren.length)
    {
      arrayOfChildDrawable[i].setDensity(j);
      i++;
    }
    inflateLayers(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
  }
  
  public void invalidateDrawable(Drawable paramDrawable)
  {
    if (mSuspendChildInvalidation) {
      mChildRequestedInvalidation = true;
    } else {
      invalidateSelf();
    }
  }
  
  public void invalidateSelf()
  {
    mLayersShader = null;
    super.invalidateSelf();
  }
  
  public boolean isAutoMirrored()
  {
    return mLayerState.mAutoMirrored;
  }
  
  public boolean isProjected()
  {
    if (super.isProjected()) {
      return true;
    }
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    for (int i = 0;; i++)
    {
      LayerState localLayerState = mLayerState;
      if (i >= 2) {
        break;
      }
      if (mDrawable.isProjected()) {
        return true;
      }
    }
    return false;
  }
  
  public boolean isStateful()
  {
    return mLayerState.isStateful();
  }
  
  public void jumpToCurrentState()
  {
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    for (int i = 0;; i++)
    {
      Object localObject = mLayerState;
      if (i >= 2) {
        break;
      }
      localObject = mDrawable;
      if (localObject != null) {
        ((Drawable)localObject).jumpToCurrentState();
      }
    }
  }
  
  public Drawable mutate()
  {
    if ((!mMutated) && (super.mutate() == this))
    {
      mLayerState = createConstantState(mLayerState, null);
      for (int i = 0;; i++)
      {
        Object localObject = mLayerState;
        if (i >= 2) {
          break;
        }
        localObject = mLayerState.mChildren[i].mDrawable;
        if (localObject != null) {
          ((Drawable)localObject).mutate();
        }
      }
      mMutated = true;
    }
    return this;
  }
  
  protected void onBoundsChange(Rect paramRect)
  {
    if (paramRect.isEmpty()) {
      return;
    }
    updateLayerBounds(paramRect);
  }
  
  protected boolean onLevelChange(int paramInt)
  {
    boolean bool1 = false;
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    int i = 0;
    for (;;)
    {
      Object localObject = mLayerState;
      if (i >= 2) {
        break;
      }
      localObject = mDrawable;
      boolean bool2 = bool1;
      if (localObject != null)
      {
        bool2 = bool1;
        if (((Drawable)localObject).setLevel(paramInt)) {
          bool2 = true;
        }
      }
      i++;
      bool1 = bool2;
    }
    if (bool1) {
      updateLayerBounds(getBounds());
    }
    return bool1;
  }
  
  protected boolean onStateChange(int[] paramArrayOfInt)
  {
    boolean bool1 = false;
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    int i = 0;
    for (;;)
    {
      Object localObject = mLayerState;
      if (i >= 2) {
        break;
      }
      localObject = mDrawable;
      boolean bool2 = bool1;
      if (localObject != null)
      {
        bool2 = bool1;
        if (((Drawable)localObject).isStateful())
        {
          bool2 = bool1;
          if (((Drawable)localObject).setState(paramArrayOfInt)) {
            bool2 = true;
          }
        }
      }
      i++;
      bool1 = bool2;
    }
    if (bool1) {
      updateLayerBounds(getBounds());
    }
    return bool1;
  }
  
  public void scheduleDrawable(Drawable paramDrawable, Runnable paramRunnable, long paramLong)
  {
    scheduleSelf(paramRunnable, paramLong);
  }
  
  public void setAlpha(int paramInt)
  {
    mPaint.setAlpha(paramInt);
  }
  
  public void setAutoMirrored(boolean paramBoolean)
  {
    LayerState.access$002(mLayerState, paramBoolean);
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    for (int i = 0;; i++)
    {
      Object localObject = mLayerState;
      if (i >= 2) {
        break;
      }
      localObject = mDrawable;
      if (localObject != null) {
        ((Drawable)localObject).setAutoMirrored(paramBoolean);
      }
    }
  }
  
  public void setColorFilter(ColorFilter paramColorFilter)
  {
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    for (int i = 0;; i++)
    {
      Object localObject = mLayerState;
      if (i >= 2) {
        break;
      }
      localObject = mDrawable;
      if (localObject != null) {
        ((Drawable)localObject).setColorFilter(paramColorFilter);
      }
    }
  }
  
  public void setDither(boolean paramBoolean)
  {
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    for (int i = 0;; i++)
    {
      Object localObject = mLayerState;
      if (i >= 2) {
        break;
      }
      localObject = mDrawable;
      if (localObject != null) {
        ((Drawable)localObject).setDither(paramBoolean);
      }
    }
  }
  
  public void setHotspot(float paramFloat1, float paramFloat2)
  {
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    for (int i = 0;; i++)
    {
      Object localObject = mLayerState;
      if (i >= 2) {
        break;
      }
      localObject = mDrawable;
      if (localObject != null) {
        ((Drawable)localObject).setHotspot(paramFloat1, paramFloat2);
      }
    }
  }
  
  public void setHotspotBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    for (int i = 0;; i++)
    {
      Object localObject = mLayerState;
      if (i >= 2) {
        break;
      }
      localObject = mDrawable;
      if (localObject != null) {
        ((Drawable)localObject).setHotspotBounds(paramInt1, paramInt2, paramInt3, paramInt4);
      }
    }
    if (mHotspotBounds == null) {
      mHotspotBounds = new Rect(paramInt1, paramInt2, paramInt3, paramInt4);
    } else {
      mHotspotBounds.set(paramInt1, paramInt2, paramInt3, paramInt4);
    }
  }
  
  public void setOpacity(int paramInt)
  {
    mLayerState.mOpacityOverride = paramInt;
  }
  
  public void setTintList(ColorStateList paramColorStateList)
  {
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    Object localObject = mLayerState;
    for (int i = 0; i < 2; i++)
    {
      localObject = mDrawable;
      if (localObject != null) {
        ((Drawable)localObject).setTintList(paramColorStateList);
      }
    }
  }
  
  public void setTintMode(PorterDuff.Mode paramMode)
  {
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    Object localObject = mLayerState;
    for (int i = 0; i < 2; i++)
    {
      localObject = mDrawable;
      if (localObject != null) {
        ((Drawable)localObject).setTintMode(paramMode);
      }
    }
  }
  
  public boolean setVisible(boolean paramBoolean1, boolean paramBoolean2)
  {
    boolean bool = super.setVisible(paramBoolean1, paramBoolean2);
    ChildDrawable[] arrayOfChildDrawable = mLayerState.mChildren;
    for (int i = 0;; i++)
    {
      Object localObject = mLayerState;
      if (i >= 2) {
        break;
      }
      localObject = mDrawable;
      if (localObject != null) {
        ((Drawable)localObject).setVisible(paramBoolean1, paramBoolean2);
      }
    }
    return bool;
  }
  
  public void unscheduleDrawable(Drawable paramDrawable, Runnable paramRunnable)
  {
    unscheduleSelf(paramRunnable);
  }
  
  static class ChildDrawable
  {
    public int mDensity = 160;
    public Drawable mDrawable;
    public int[] mThemeAttrs;
    
    ChildDrawable(int paramInt)
    {
      mDensity = paramInt;
    }
    
    ChildDrawable(ChildDrawable paramChildDrawable, AdaptiveIconDrawable paramAdaptiveIconDrawable, Resources paramResources)
    {
      Drawable localDrawable = mDrawable;
      Object localObject;
      if (localDrawable != null)
      {
        localObject = localDrawable.getConstantState();
        if (localObject == null) {
          localObject = localDrawable;
        }
        for (;;)
        {
          break;
          if (paramResources != null) {
            localObject = ((Drawable.ConstantState)localObject).newDrawable(paramResources);
          } else {
            localObject = ((Drawable.ConstantState)localObject).newDrawable();
          }
        }
        ((Drawable)localObject).setCallback(paramAdaptiveIconDrawable);
        ((Drawable)localObject).setBounds(localDrawable.getBounds());
        ((Drawable)localObject).setLevel(localDrawable.getLevel());
      }
      else
      {
        localObject = null;
      }
      mDrawable = ((Drawable)localObject);
      mThemeAttrs = mThemeAttrs;
      mDensity = Drawable.resolveDensity(paramResources, mDensity);
    }
    
    public boolean canApplyTheme()
    {
      boolean bool;
      if ((mThemeAttrs == null) && ((mDrawable == null) || (!mDrawable.canApplyTheme()))) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public final void setDensity(int paramInt)
    {
      if (mDensity != paramInt) {
        mDensity = paramInt;
      }
    }
  }
  
  static class LayerState
    extends Drawable.ConstantState
  {
    static final int N_CHILDREN = 2;
    private boolean mAutoMirrored;
    int mChangingConfigurations;
    private boolean mCheckedOpacity;
    private boolean mCheckedStateful;
    AdaptiveIconDrawable.ChildDrawable[] mChildren;
    int mChildrenChangingConfigurations;
    int mDensity;
    private boolean mIsStateful;
    private int mOpacity;
    int mOpacityOverride;
    int mSrcDensityOverride;
    private int[] mThemeAttrs;
    
    LayerState(LayerState paramLayerState, AdaptiveIconDrawable paramAdaptiveIconDrawable, Resources paramResources)
    {
      int i = 0;
      int j = 0;
      mSrcDensityOverride = 0;
      mOpacityOverride = 0;
      mAutoMirrored = false;
      int k;
      if (paramLayerState != null) {
        k = mDensity;
      } else {
        k = 0;
      }
      mDensity = Drawable.resolveDensity(paramResources, k);
      mChildren = new AdaptiveIconDrawable.ChildDrawable[2];
      if (paramLayerState != null)
      {
        AdaptiveIconDrawable.ChildDrawable[] arrayOfChildDrawable = mChildren;
        mChangingConfigurations = mChangingConfigurations;
        mChildrenChangingConfigurations = mChildrenChangingConfigurations;
        for (k = j; k < 2; k++)
        {
          AdaptiveIconDrawable.ChildDrawable localChildDrawable = arrayOfChildDrawable[k];
          mChildren[k] = new AdaptiveIconDrawable.ChildDrawable(localChildDrawable, paramAdaptiveIconDrawable, paramResources);
        }
        mCheckedOpacity = mCheckedOpacity;
        mOpacity = mOpacity;
        mCheckedStateful = mCheckedStateful;
        mIsStateful = mIsStateful;
        mAutoMirrored = mAutoMirrored;
        mThemeAttrs = mThemeAttrs;
        mOpacityOverride = mOpacityOverride;
        mSrcDensityOverride = mSrcDensityOverride;
      }
      else
      {
        for (k = i; k < 2; k++) {
          mChildren[k] = new AdaptiveIconDrawable.ChildDrawable(mDensity);
        }
      }
    }
    
    public boolean canApplyTheme()
    {
      if ((mThemeAttrs == null) && (!super.canApplyTheme()))
      {
        AdaptiveIconDrawable.ChildDrawable[] arrayOfChildDrawable = mChildren;
        for (int i = 0; i < 2; i++) {
          if (arrayOfChildDrawable[i].canApplyTheme()) {
            return true;
          }
        }
        return false;
      }
      return true;
    }
    
    public final boolean canConstantState()
    {
      AdaptiveIconDrawable.ChildDrawable[] arrayOfChildDrawable = mChildren;
      for (int i = 0; i < 2; i++)
      {
        Drawable localDrawable = mDrawable;
        if ((localDrawable != null) && (localDrawable.getConstantState() == null)) {
          return false;
        }
      }
      return true;
    }
    
    public int getChangingConfigurations()
    {
      return mChangingConfigurations | mChildrenChangingConfigurations;
    }
    
    public final int getOpacity()
    {
      if (mCheckedOpacity) {
        return mOpacity;
      }
      AdaptiveIconDrawable.ChildDrawable[] arrayOfChildDrawable = mChildren;
      int i = -1;
      int k;
      for (int j = 0;; j++)
      {
        k = i;
        if (j >= 2) {
          break;
        }
        if (mDrawable != null)
        {
          k = j;
          break;
        }
      }
      if (k >= 0) {
        j = mDrawable.getOpacity();
      } else {
        j = -2;
      }
      k++;
      while (k < 2)
      {
        Drawable localDrawable = mDrawable;
        i = j;
        if (localDrawable != null) {
          i = Drawable.resolveOpacity(j, localDrawable.getOpacity());
        }
        k++;
        j = i;
      }
      mOpacity = j;
      mCheckedOpacity = true;
      return j;
    }
    
    public final boolean hasFocusStateSpecified()
    {
      AdaptiveIconDrawable.ChildDrawable[] arrayOfChildDrawable = mChildren;
      for (int i = 0; i < 2; i++)
      {
        Drawable localDrawable = mDrawable;
        if ((localDrawable != null) && (localDrawable.hasFocusStateSpecified())) {
          return true;
        }
      }
      return false;
    }
    
    public void invalidateCache()
    {
      mCheckedOpacity = false;
      mCheckedStateful = false;
    }
    
    public final boolean isStateful()
    {
      if (mCheckedStateful) {
        return mIsStateful;
      }
      AdaptiveIconDrawable.ChildDrawable[] arrayOfChildDrawable = mChildren;
      boolean bool1 = false;
      boolean bool2;
      for (int i = 0;; i++)
      {
        bool2 = bool1;
        if (i >= 2) {
          break;
        }
        Drawable localDrawable = mDrawable;
        if ((localDrawable != null) && (localDrawable.isStateful()))
        {
          bool2 = true;
          break;
        }
      }
      mIsStateful = bool2;
      mCheckedStateful = true;
      return bool2;
    }
    
    public Drawable newDrawable()
    {
      return new AdaptiveIconDrawable(this, null);
    }
    
    public Drawable newDrawable(Resources paramResources)
    {
      return new AdaptiveIconDrawable(this, paramResources);
    }
    
    public final void setDensity(int paramInt)
    {
      if (mDensity != paramInt) {
        mDensity = paramInt;
      }
    }
  }
}
