package android.graphics.drawable;

import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Insets;
import android.graphics.Outline;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import com.android.internal.R.styleable;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class InsetDrawable
  extends DrawableWrapper
{
  private InsetState mState;
  private final Rect mTmpInsetRect = new Rect();
  private final Rect mTmpRect = new Rect();
  
  InsetDrawable()
  {
    this(new InsetState(null, null), null);
  }
  
  public InsetDrawable(Drawable paramDrawable, float paramFloat)
  {
    this(paramDrawable, paramFloat, paramFloat, paramFloat, paramFloat);
  }
  
  public InsetDrawable(Drawable paramDrawable, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    this(new InsetState(null, null), null);
    mState.mInsetLeft = new InsetValue(paramFloat1, 0);
    mState.mInsetTop = new InsetValue(paramFloat2, 0);
    mState.mInsetRight = new InsetValue(paramFloat3, 0);
    mState.mInsetBottom = new InsetValue(paramFloat4, 0);
    setDrawable(paramDrawable);
  }
  
  public InsetDrawable(Drawable paramDrawable, int paramInt)
  {
    this(paramDrawable, paramInt, paramInt, paramInt, paramInt);
  }
  
  public InsetDrawable(Drawable paramDrawable, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this(new InsetState(null, null), null);
    mState.mInsetLeft = new InsetValue(0.0F, paramInt1);
    mState.mInsetTop = new InsetValue(0.0F, paramInt2);
    mState.mInsetRight = new InsetValue(0.0F, paramInt3);
    mState.mInsetBottom = new InsetValue(0.0F, paramInt4);
    setDrawable(paramDrawable);
  }
  
  private InsetDrawable(InsetState paramInsetState, Resources paramResources)
  {
    super(paramInsetState, paramResources);
    mState = paramInsetState;
  }
  
  private InsetValue getInset(TypedArray paramTypedArray, int paramInt, InsetValue paramInsetValue)
  {
    if (paramTypedArray.hasValue(paramInt))
    {
      TypedValue localTypedValue = paramTypedArray.peekValue(paramInt);
      if (type == 6)
      {
        float f = localTypedValue.getFraction(1.0F, 1.0F);
        if (f < 1.0F) {
          return new InsetValue(f, 0);
        }
        throw new IllegalStateException("Fraction cannot be larger than 1");
      }
      paramInt = paramTypedArray.getDimensionPixelOffset(paramInt, 0);
      if (paramInt != 0) {
        return new InsetValue(0.0F, paramInt);
      }
    }
    return paramInsetValue;
  }
  
  private void getInsets(Rect paramRect)
  {
    Rect localRect = getBounds();
    left = mState.mInsetLeft.getDimension(localRect.width());
    right = mState.mInsetRight.getDimension(localRect.width());
    top = mState.mInsetTop.getDimension(localRect.height());
    bottom = mState.mInsetBottom.getDimension(localRect.height());
  }
  
  private void updateStateFromTypedArray(TypedArray paramTypedArray)
  {
    InsetState localInsetState = mState;
    if (localInsetState == null) {
      return;
    }
    mChangingConfigurations |= paramTypedArray.getChangingConfigurations();
    InsetState.access$002(localInsetState, paramTypedArray.extractThemeAttrs());
    if (paramTypedArray.hasValue(6))
    {
      InsetValue localInsetValue = getInset(paramTypedArray, 6, new InsetValue());
      mInsetLeft = localInsetValue;
      mInsetTop = localInsetValue;
      mInsetRight = localInsetValue;
      mInsetBottom = localInsetValue;
    }
    mInsetLeft = getInset(paramTypedArray, 2, mInsetLeft);
    mInsetTop = getInset(paramTypedArray, 4, mInsetTop);
    mInsetRight = getInset(paramTypedArray, 3, mInsetRight);
    mInsetBottom = getInset(paramTypedArray, 5, mInsetBottom);
  }
  
  private void verifyRequiredAttributes(TypedArray paramTypedArray)
    throws XmlPullParserException
  {
    if ((getDrawable() == null) && ((mState.mThemeAttrs == null) || (mState.mThemeAttrs[1] == 0)))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramTypedArray.getPositionDescription());
      localStringBuilder.append(": <inset> tag requires a 'drawable' attribute or child tag defining a drawable");
      throw new XmlPullParserException(localStringBuilder.toString());
    }
  }
  
  /* Error */
  public void applyTheme(Resources.Theme paramTheme)
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokespecial 180	android/graphics/drawable/DrawableWrapper:applyTheme	(Landroid/content/res/Resources$Theme;)V
    //   5: aload_0
    //   6: getfield 30	android/graphics/drawable/InsetDrawable:mState	Landroid/graphics/drawable/InsetDrawable$InsetState;
    //   9: astore_2
    //   10: aload_2
    //   11: ifnonnull +4 -> 15
    //   14: return
    //   15: aload_2
    //   16: invokestatic 158	android/graphics/drawable/InsetDrawable$InsetState:access$000	(Landroid/graphics/drawable/InsetDrawable$InsetState;)[I
    //   19: ifnull +50 -> 69
    //   22: aload_1
    //   23: aload_2
    //   24: invokestatic 158	android/graphics/drawable/InsetDrawable$InsetState:access$000	(Landroid/graphics/drawable/InsetDrawable$InsetState;)[I
    //   27: getstatic 186	com/android/internal/R$styleable:InsetDrawable	[I
    //   30: invokevirtual 192	android/content/res/Resources$Theme:resolveAttributes	([I[I)Landroid/content/res/TypedArray;
    //   33: astore_1
    //   34: aload_0
    //   35: aload_1
    //   36: invokespecial 194	android/graphics/drawable/InsetDrawable:updateStateFromTypedArray	(Landroid/content/res/TypedArray;)V
    //   39: aload_0
    //   40: aload_1
    //   41: invokespecial 196	android/graphics/drawable/InsetDrawable:verifyRequiredAttributes	(Landroid/content/res/TypedArray;)V
    //   44: aload_1
    //   45: invokevirtual 199	android/content/res/TypedArray:recycle	()V
    //   48: goto +21 -> 69
    //   51: astore_2
    //   52: goto +11 -> 63
    //   55: astore_2
    //   56: aload_2
    //   57: invokestatic 203	android/graphics/drawable/InsetDrawable:rethrowAsRuntimeException	(Ljava/lang/Exception;)V
    //   60: goto -16 -> 44
    //   63: aload_1
    //   64: invokevirtual 199	android/content/res/TypedArray:recycle	()V
    //   67: aload_2
    //   68: athrow
    //   69: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	70	0	this	InsetDrawable
    //   0	70	1	paramTheme	Resources.Theme
    //   9	15	2	localInsetState	InsetState
    //   51	1	2	localObject	Object
    //   55	13	2	localXmlPullParserException	XmlPullParserException
    // Exception table:
    //   from	to	target	type
    //   34	44	51	finally
    //   56	60	51	finally
    //   34	44	55	org/xmlpull/v1/XmlPullParserException
  }
  
  public int getIntrinsicHeight()
  {
    int i = getDrawable().getIntrinsicHeight();
    float f = mState.mInsetTop.mFraction + mState.mInsetBottom.mFraction;
    if ((i >= 0) && (f < 1.0F)) {
      return (int)(i / (1.0F - f)) + mState.mInsetTop.mDimension + mState.mInsetBottom.mDimension;
    }
    return -1;
  }
  
  public int getIntrinsicWidth()
  {
    int i = getDrawable().getIntrinsicWidth();
    float f = mState.mInsetLeft.mFraction + mState.mInsetRight.mFraction;
    if ((i >= 0) && (f < 1.0F)) {
      return (int)(i / (1.0F - f)) + mState.mInsetLeft.mDimension + mState.mInsetRight.mDimension;
    }
    return -1;
  }
  
  public int getOpacity()
  {
    InsetState localInsetState = mState;
    int i = getDrawable().getOpacity();
    getInsets(mTmpInsetRect);
    if ((i == -1) && ((mTmpInsetRect.left > 0) || (mTmpInsetRect.top > 0) || (mTmpInsetRect.right > 0) || (mTmpInsetRect.bottom > 0))) {
      return -3;
    }
    return i;
  }
  
  public Insets getOpticalInsets()
  {
    Insets localInsets = super.getOpticalInsets();
    getInsets(mTmpInsetRect);
    return Insets.of(left + mTmpInsetRect.left, top + mTmpInsetRect.top, right + mTmpInsetRect.right, bottom + mTmpInsetRect.bottom);
  }
  
  public void getOutline(Outline paramOutline)
  {
    getDrawable().getOutline(paramOutline);
  }
  
  public boolean getPadding(Rect paramRect)
  {
    boolean bool = super.getPadding(paramRect);
    getInsets(mTmpInsetRect);
    left += mTmpInsetRect.left;
    right += mTmpInsetRect.right;
    top += mTmpInsetRect.top;
    bottom += mTmpInsetRect.bottom;
    if ((!bool) && ((mTmpInsetRect.left | mTmpInsetRect.right | mTmpInsetRect.top | mTmpInsetRect.bottom) == 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    TypedArray localTypedArray = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.InsetDrawable);
    super.inflate(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
    updateStateFromTypedArray(localTypedArray);
    verifyRequiredAttributes(localTypedArray);
    localTypedArray.recycle();
  }
  
  DrawableWrapper.DrawableWrapperState mutateConstantState()
  {
    mState = new InsetState(mState, null);
    return mState;
  }
  
  protected void onBoundsChange(Rect paramRect)
  {
    Rect localRect = mTmpRect;
    localRect.set(paramRect);
    left += mState.mInsetLeft.getDimension(paramRect.width());
    top += mState.mInsetTop.getDimension(paramRect.height());
    right -= mState.mInsetRight.getDimension(paramRect.width());
    bottom -= mState.mInsetBottom.getDimension(paramRect.height());
    super.onBoundsChange(localRect);
  }
  
  static final class InsetState
    extends DrawableWrapper.DrawableWrapperState
  {
    InsetDrawable.InsetValue mInsetBottom;
    InsetDrawable.InsetValue mInsetLeft;
    InsetDrawable.InsetValue mInsetRight;
    InsetDrawable.InsetValue mInsetTop;
    private int[] mThemeAttrs;
    
    InsetState(InsetState paramInsetState, Resources paramResources)
    {
      super(paramResources);
      if (paramInsetState != null)
      {
        mInsetLeft = mInsetLeft.clone();
        mInsetTop = mInsetTop.clone();
        mInsetRight = mInsetRight.clone();
        mInsetBottom = mInsetBottom.clone();
        if (mDensity != mDensity) {
          applyDensityScaling(mDensity, mDensity);
        }
      }
      else
      {
        mInsetLeft = new InsetDrawable.InsetValue();
        mInsetTop = new InsetDrawable.InsetValue();
        mInsetRight = new InsetDrawable.InsetValue();
        mInsetBottom = new InsetDrawable.InsetValue();
      }
    }
    
    private void applyDensityScaling(int paramInt1, int paramInt2)
    {
      mInsetLeft.scaleFromDensity(paramInt1, paramInt2);
      mInsetTop.scaleFromDensity(paramInt1, paramInt2);
      mInsetRight.scaleFromDensity(paramInt1, paramInt2);
      mInsetBottom.scaleFromDensity(paramInt1, paramInt2);
    }
    
    public Drawable newDrawable(Resources paramResources)
    {
      InsetState localInsetState;
      if (paramResources != null)
      {
        int i = getDisplayMetricsdensityDpi;
        if (i == 0) {
          i = 160;
        }
        if (i != mDensity) {
          localInsetState = new InsetState(this, paramResources);
        } else {
          localInsetState = this;
        }
      }
      else
      {
        localInsetState = this;
      }
      return new InsetDrawable(localInsetState, paramResources, null);
    }
    
    void onDensityChanged(int paramInt1, int paramInt2)
    {
      super.onDensityChanged(paramInt1, paramInt2);
      applyDensityScaling(paramInt1, paramInt2);
    }
  }
  
  static final class InsetValue
    implements Cloneable
  {
    int mDimension;
    final float mFraction;
    
    public InsetValue()
    {
      this(0.0F, 0);
    }
    
    public InsetValue(float paramFloat, int paramInt)
    {
      mFraction = paramFloat;
      mDimension = paramInt;
    }
    
    public InsetValue clone()
    {
      return new InsetValue(mFraction, mDimension);
    }
    
    int getDimension(int paramInt)
    {
      return (int)(paramInt * mFraction) + mDimension;
    }
    
    void scaleFromDensity(int paramInt1, int paramInt2)
    {
      if (mDimension != 0) {
        mDimension = Bitmap.scaleFromDensity(mDimension, paramInt1, paramInt2);
      }
    }
  }
}
