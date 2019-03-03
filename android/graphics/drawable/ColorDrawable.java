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
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import com.android.internal.R.styleable;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ColorDrawable
  extends Drawable
{
  @ViewDebug.ExportedProperty(deepExport=true, prefix="state_")
  private ColorState mColorState;
  private boolean mMutated;
  private final Paint mPaint = new Paint(1);
  private PorterDuffColorFilter mTintFilter;
  
  public ColorDrawable()
  {
    mColorState = new ColorState();
  }
  
  public ColorDrawable(int paramInt)
  {
    mColorState = new ColorState();
    setColor(paramInt);
  }
  
  private ColorDrawable(ColorState paramColorState, Resources paramResources)
  {
    mColorState = paramColorState;
    updateLocalState(paramResources);
  }
  
  private void updateLocalState(Resources paramResources)
  {
    mTintFilter = updateTintFilter(mTintFilter, mColorState.mTint, mColorState.mTintMode);
  }
  
  private void updateStateFromTypedArray(TypedArray paramTypedArray)
  {
    ColorState localColorState = mColorState;
    mChangingConfigurations |= paramTypedArray.getChangingConfigurations();
    mThemeAttrs = paramTypedArray.extractThemeAttrs();
    mBaseColor = paramTypedArray.getColor(0, mBaseColor);
    mUseColor = mBaseColor;
  }
  
  public void applyTheme(Resources.Theme paramTheme)
  {
    super.applyTheme(paramTheme);
    ColorState localColorState = mColorState;
    if (localColorState == null) {
      return;
    }
    if (mThemeAttrs != null)
    {
      TypedArray localTypedArray = paramTheme.resolveAttributes(mThemeAttrs, R.styleable.ColorDrawable);
      updateStateFromTypedArray(localTypedArray);
      localTypedArray.recycle();
    }
    if ((mTint != null) && (mTint.canApplyTheme())) {
      mTint = mTint.obtainForTheme(paramTheme);
    }
    updateLocalState(paramTheme.getResources());
  }
  
  public boolean canApplyTheme()
  {
    boolean bool;
    if ((!mColorState.canApplyTheme()) && (!super.canApplyTheme())) {
      bool = false;
    } else {
      bool = true;
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
    ColorFilter localColorFilter = mPaint.getColorFilter();
    if ((mColorState.mUseColor >>> 24 != 0) || (localColorFilter != null) || (mTintFilter != null))
    {
      if (localColorFilter == null) {
        mPaint.setColorFilter(mTintFilter);
      }
      mPaint.setColor(mColorState.mUseColor);
      paramCanvas.drawRect(getBounds(), mPaint);
      mPaint.setColorFilter(localColorFilter);
    }
  }
  
  public int getAlpha()
  {
    return mColorState.mUseColor >>> 24;
  }
  
  public int getChangingConfigurations()
  {
    return super.getChangingConfigurations() | mColorState.getChangingConfigurations();
  }
  
  public int getColor()
  {
    return mColorState.mUseColor;
  }
  
  public Drawable.ConstantState getConstantState()
  {
    return mColorState;
  }
  
  public int getOpacity()
  {
    if ((mTintFilter == null) && (mPaint.getColorFilter() == null))
    {
      int i = mColorState.mUseColor >>> 24;
      if (i != 0)
      {
        if (i != 255) {
          return -3;
        }
        return -1;
      }
      return -2;
    }
    return -3;
  }
  
  public void getOutline(Outline paramOutline)
  {
    paramOutline.setRect(getBounds());
    paramOutline.setAlpha(getAlpha() / 255.0F);
  }
  
  public Xfermode getXfermode()
  {
    return mPaint.getXfermode();
  }
  
  public boolean hasFocusStateSpecified()
  {
    boolean bool;
    if ((mColorState.mTint != null) && (mColorState.mTint.hasFocusStateSpecified())) {
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
    paramXmlPullParser = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.ColorDrawable);
    updateStateFromTypedArray(paramXmlPullParser);
    paramXmlPullParser.recycle();
    updateLocalState(paramResources);
  }
  
  public boolean isStateful()
  {
    boolean bool;
    if ((mColorState.mTint != null) && (mColorState.mTint.isStateful())) {
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
      mColorState = new ColorState(mColorState);
      mMutated = true;
    }
    return this;
  }
  
  protected boolean onStateChange(int[] paramArrayOfInt)
  {
    paramArrayOfInt = mColorState;
    if ((mTint != null) && (mTintMode != null))
    {
      mTintFilter = updateTintFilter(mTintFilter, mTint, mTintMode);
      return true;
    }
    return false;
  }
  
  public void setAlpha(int paramInt)
  {
    int i = mColorState.mBaseColor;
    paramInt = mColorState.mBaseColor << 8 >>> 8 | (i >>> 24) * (paramInt + (paramInt >> 7)) >> 8 << 24;
    if (mColorState.mUseColor != paramInt)
    {
      mColorState.mUseColor = paramInt;
      invalidateSelf();
    }
  }
  
  public void setColor(int paramInt)
  {
    if ((mColorState.mBaseColor != paramInt) || (mColorState.mUseColor != paramInt))
    {
      ColorState localColorState = mColorState;
      mColorState.mUseColor = paramInt;
      mBaseColor = paramInt;
      invalidateSelf();
    }
  }
  
  public void setColorFilter(ColorFilter paramColorFilter)
  {
    mPaint.setColorFilter(paramColorFilter);
  }
  
  public void setTintList(ColorStateList paramColorStateList)
  {
    mColorState.mTint = paramColorStateList;
    mTintFilter = updateTintFilter(mTintFilter, paramColorStateList, mColorState.mTintMode);
    invalidateSelf();
  }
  
  public void setTintMode(PorterDuff.Mode paramMode)
  {
    mColorState.mTintMode = paramMode;
    mTintFilter = updateTintFilter(mTintFilter, mColorState.mTint, paramMode);
    invalidateSelf();
  }
  
  public void setXfermode(Xfermode paramXfermode)
  {
    mPaint.setXfermode(paramXfermode);
    invalidateSelf();
  }
  
  static final class ColorState
    extends Drawable.ConstantState
  {
    int mBaseColor;
    int mChangingConfigurations;
    int[] mThemeAttrs;
    ColorStateList mTint = null;
    PorterDuff.Mode mTintMode = Drawable.DEFAULT_TINT_MODE;
    @ViewDebug.ExportedProperty
    int mUseColor;
    
    ColorState() {}
    
    ColorState(ColorState paramColorState)
    {
      mThemeAttrs = mThemeAttrs;
      mBaseColor = mBaseColor;
      mUseColor = mUseColor;
      mChangingConfigurations = mChangingConfigurations;
      mTint = mTint;
      mTintMode = mTintMode;
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
      return new ColorDrawable(this, null, null);
    }
    
    public Drawable newDrawable(Resources paramResources)
    {
      return new ColorDrawable(this, paramResources, null);
    }
  }
}
