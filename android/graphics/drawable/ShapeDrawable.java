package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.util.Log;
import com.android.internal.R.styleable;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ShapeDrawable
  extends Drawable
{
  private boolean mMutated;
  private ShapeState mShapeState;
  private PorterDuffColorFilter mTintFilter;
  
  public ShapeDrawable()
  {
    this(new ShapeState(), null);
  }
  
  private ShapeDrawable(ShapeState paramShapeState, Resources paramResources)
  {
    mShapeState = paramShapeState;
    updateLocalState();
  }
  
  public ShapeDrawable(Shape paramShape)
  {
    this(new ShapeState(), null);
    mShapeState.mShape = paramShape;
  }
  
  private static int modulateAlpha(int paramInt1, int paramInt2)
  {
    return paramInt1 * ((paramInt2 >>> 7) + paramInt2) >>> 8;
  }
  
  private void updateLocalState()
  {
    mTintFilter = updateTintFilter(mTintFilter, mShapeState.mTint, mShapeState.mTintMode);
  }
  
  private void updateShape()
  {
    if (mShapeState.mShape != null)
    {
      Rect localRect = getBounds();
      int i = localRect.width();
      int j = localRect.height();
      mShapeState.mShape.resize(i, j);
      if (mShapeState.mShaderFactory != null) {
        mShapeState.mPaint.setShader(mShapeState.mShaderFactory.resize(i, j));
      }
    }
    invalidateSelf();
  }
  
  private void updateStateFromTypedArray(TypedArray paramTypedArray)
  {
    ShapeState localShapeState = mShapeState;
    Paint localPaint = mPaint;
    mChangingConfigurations |= paramTypedArray.getChangingConfigurations();
    mThemeAttrs = paramTypedArray.extractThemeAttrs();
    localPaint.setColor(paramTypedArray.getColor(4, localPaint.getColor()));
    localPaint.setDither(paramTypedArray.getBoolean(0, localPaint.isDither()));
    mIntrinsicWidth = ((int)paramTypedArray.getDimension(3, mIntrinsicWidth));
    mIntrinsicHeight = ((int)paramTypedArray.getDimension(2, mIntrinsicHeight));
    int i = paramTypedArray.getInt(5, -1);
    if (i != -1) {
      mTintMode = Drawable.parseTintMode(i, PorterDuff.Mode.SRC_IN);
    }
    paramTypedArray = paramTypedArray.getColorStateList(1);
    if (paramTypedArray != null) {
      mTint = paramTypedArray;
    }
  }
  
  public void applyTheme(Resources.Theme paramTheme)
  {
    super.applyTheme(paramTheme);
    ShapeState localShapeState = mShapeState;
    if (localShapeState == null) {
      return;
    }
    if (mThemeAttrs != null)
    {
      TypedArray localTypedArray = paramTheme.resolveAttributes(mThemeAttrs, R.styleable.ShapeDrawable);
      updateStateFromTypedArray(localTypedArray);
      localTypedArray.recycle();
    }
    if ((mTint != null) && (mTint.canApplyTheme())) {
      mTint = mTint.obtainForTheme(paramTheme);
    }
    updateLocalState();
  }
  
  public void clearMutated()
  {
    super.clearMutated();
    mMutated = false;
  }
  
  public void draw(Canvas paramCanvas)
  {
    Rect localRect = getBounds();
    ShapeState localShapeState = mShapeState;
    Paint localPaint = mPaint;
    int i = localPaint.getAlpha();
    localPaint.setAlpha(modulateAlpha(i, mAlpha));
    if ((localPaint.getAlpha() != 0) || (localPaint.getXfermode() != null) || (localPaint.hasShadowLayer()))
    {
      int j;
      if ((mTintFilter != null) && (localPaint.getColorFilter() == null))
      {
        localPaint.setColorFilter(mTintFilter);
        j = 1;
      }
      else
      {
        j = 0;
      }
      if (mShape != null)
      {
        int k = paramCanvas.save();
        paramCanvas.translate(left, top);
        onDraw(mShape, paramCanvas, localPaint);
        paramCanvas.restoreToCount(k);
      }
      else
      {
        paramCanvas.drawRect(localRect, localPaint);
      }
      if (j != 0) {
        localPaint.setColorFilter(null);
      }
    }
    localPaint.setAlpha(i);
  }
  
  public int getAlpha()
  {
    return mShapeState.mAlpha;
  }
  
  public int getChangingConfigurations()
  {
    return super.getChangingConfigurations() | mShapeState.getChangingConfigurations();
  }
  
  public Drawable.ConstantState getConstantState()
  {
    mShapeState.mChangingConfigurations = getChangingConfigurations();
    return mShapeState;
  }
  
  public int getIntrinsicHeight()
  {
    return mShapeState.mIntrinsicHeight;
  }
  
  public int getIntrinsicWidth()
  {
    return mShapeState.mIntrinsicWidth;
  }
  
  public int getOpacity()
  {
    if (mShapeState.mShape == null)
    {
      Paint localPaint = mShapeState.mPaint;
      if (localPaint.getXfermode() == null)
      {
        int i = localPaint.getAlpha();
        if (i == 0) {
          return -2;
        }
        if (i == 255) {
          return -1;
        }
      }
    }
    return -3;
  }
  
  public void getOutline(Outline paramOutline)
  {
    if (mShapeState.mShape != null)
    {
      mShapeState.mShape.getOutline(paramOutline);
      paramOutline.setAlpha(getAlpha() / 255.0F);
    }
  }
  
  public boolean getPadding(Rect paramRect)
  {
    if (mShapeState.mPadding != null)
    {
      paramRect.set(mShapeState.mPadding);
      return true;
    }
    return super.getPadding(paramRect);
  }
  
  public Paint getPaint()
  {
    return mShapeState.mPaint;
  }
  
  public ShaderFactory getShaderFactory()
  {
    return mShapeState.mShaderFactory;
  }
  
  public Shape getShape()
  {
    return mShapeState.mShape;
  }
  
  public boolean hasFocusStateSpecified()
  {
    boolean bool;
    if ((mShapeState.mTint != null) && (mShapeState.mTint.hasFocusStateSpecified())) {
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
    paramTheme = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.ShapeDrawable);
    updateStateFromTypedArray(paramTheme);
    paramTheme.recycle();
    int i = paramXmlPullParser.getDepth();
    for (;;)
    {
      int j = paramXmlPullParser.next();
      if ((j == 1) || ((j == 3) && (paramXmlPullParser.getDepth() <= i))) {
        break;
      }
      if (j == 2)
      {
        String str = paramXmlPullParser.getName();
        if (!inflateTag(str, paramResources, paramXmlPullParser, paramAttributeSet))
        {
          paramTheme = new StringBuilder();
          paramTheme.append("Unknown element: ");
          paramTheme.append(str);
          paramTheme.append(" for ShapeDrawable ");
          paramTheme.append(this);
          Log.w("drawable", paramTheme.toString());
        }
      }
    }
    updateLocalState();
  }
  
  protected boolean inflateTag(String paramString, Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet)
  {
    if ("padding".equals(paramString))
    {
      paramString = paramResources.obtainAttributes(paramAttributeSet, R.styleable.ShapeDrawablePadding);
      setPadding(paramString.getDimensionPixelOffset(0, 0), paramString.getDimensionPixelOffset(1, 0), paramString.getDimensionPixelOffset(2, 0), paramString.getDimensionPixelOffset(3, 0));
      paramString.recycle();
      return true;
    }
    return false;
  }
  
  public boolean isStateful()
  {
    ShapeState localShapeState = mShapeState;
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
      mShapeState = new ShapeState(mShapeState);
      updateLocalState();
      mMutated = true;
    }
    return this;
  }
  
  protected void onBoundsChange(Rect paramRect)
  {
    super.onBoundsChange(paramRect);
    updateShape();
  }
  
  protected void onDraw(Shape paramShape, Canvas paramCanvas, Paint paramPaint)
  {
    paramShape.draw(paramCanvas, paramPaint);
  }
  
  protected boolean onStateChange(int[] paramArrayOfInt)
  {
    paramArrayOfInt = mShapeState;
    if ((mTint != null) && (mTintMode != null))
    {
      mTintFilter = updateTintFilter(mTintFilter, mTint, mTintMode);
      return true;
    }
    return false;
  }
  
  public void setAlpha(int paramInt)
  {
    mShapeState.mAlpha = paramInt;
    invalidateSelf();
  }
  
  public void setColorFilter(ColorFilter paramColorFilter)
  {
    mShapeState.mPaint.setColorFilter(paramColorFilter);
    invalidateSelf();
  }
  
  public void setDither(boolean paramBoolean)
  {
    mShapeState.mPaint.setDither(paramBoolean);
    invalidateSelf();
  }
  
  public void setIntrinsicHeight(int paramInt)
  {
    mShapeState.mIntrinsicHeight = paramInt;
    invalidateSelf();
  }
  
  public void setIntrinsicWidth(int paramInt)
  {
    mShapeState.mIntrinsicWidth = paramInt;
    invalidateSelf();
  }
  
  public void setPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((paramInt1 | paramInt2 | paramInt3 | paramInt4) == 0)
    {
      mShapeState.mPadding = null;
    }
    else
    {
      if (mShapeState.mPadding == null) {
        mShapeState.mPadding = new Rect();
      }
      mShapeState.mPadding.set(paramInt1, paramInt2, paramInt3, paramInt4);
    }
    invalidateSelf();
  }
  
  public void setPadding(Rect paramRect)
  {
    if (paramRect == null)
    {
      mShapeState.mPadding = null;
    }
    else
    {
      if (mShapeState.mPadding == null) {
        mShapeState.mPadding = new Rect();
      }
      mShapeState.mPadding.set(paramRect);
    }
    invalidateSelf();
  }
  
  public void setShaderFactory(ShaderFactory paramShaderFactory)
  {
    mShapeState.mShaderFactory = paramShaderFactory;
  }
  
  public void setShape(Shape paramShape)
  {
    mShapeState.mShape = paramShape;
    updateShape();
  }
  
  public void setTintList(ColorStateList paramColorStateList)
  {
    mShapeState.mTint = paramColorStateList;
    mTintFilter = updateTintFilter(mTintFilter, paramColorStateList, mShapeState.mTintMode);
    invalidateSelf();
  }
  
  public void setTintMode(PorterDuff.Mode paramMode)
  {
    mShapeState.mTintMode = paramMode;
    mTintFilter = updateTintFilter(mTintFilter, mShapeState.mTint, paramMode);
    invalidateSelf();
  }
  
  public void setXfermode(Xfermode paramXfermode)
  {
    mShapeState.mPaint.setXfermode(paramXfermode);
    invalidateSelf();
  }
  
  public static abstract class ShaderFactory
  {
    public ShaderFactory() {}
    
    public abstract Shader resize(int paramInt1, int paramInt2);
  }
  
  static final class ShapeState
    extends Drawable.ConstantState
  {
    int mAlpha = 255;
    int mChangingConfigurations;
    int mIntrinsicHeight;
    int mIntrinsicWidth;
    Rect mPadding;
    final Paint mPaint;
    ShapeDrawable.ShaderFactory mShaderFactory;
    Shape mShape;
    int[] mThemeAttrs;
    ColorStateList mTint;
    PorterDuff.Mode mTintMode = Drawable.DEFAULT_TINT_MODE;
    
    ShapeState()
    {
      mPaint = new Paint(1);
    }
    
    ShapeState(ShapeState paramShapeState)
    {
      mChangingConfigurations = mChangingConfigurations;
      mPaint = new Paint(mPaint);
      mThemeAttrs = mThemeAttrs;
      if (mShape != null) {
        try
        {
          mShape = mShape.clone();
        }
        catch (CloneNotSupportedException localCloneNotSupportedException)
        {
          mShape = mShape;
        }
      }
      mTint = mTint;
      mTintMode = mTintMode;
      if (mPadding != null) {
        mPadding = new Rect(mPadding);
      }
      mIntrinsicWidth = mIntrinsicWidth;
      mIntrinsicHeight = mIntrinsicHeight;
      mAlpha = mAlpha;
      mShaderFactory = mShaderFactory;
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
      return new ShapeDrawable(this, null, null);
    }
    
    public Drawable newDrawable(Resources paramResources)
    {
      return new ShapeDrawable(this, paramResources, null);
    }
  }
}
