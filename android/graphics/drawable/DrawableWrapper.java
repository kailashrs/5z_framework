package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Insets;
import android.graphics.Outline;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import com.android.internal.R.styleable;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public abstract class DrawableWrapper
  extends Drawable
  implements Drawable.Callback
{
  private Drawable mDrawable;
  private boolean mMutated;
  private DrawableWrapperState mState;
  
  public DrawableWrapper(Drawable paramDrawable)
  {
    mState = null;
    mDrawable = paramDrawable;
  }
  
  DrawableWrapper(DrawableWrapperState paramDrawableWrapperState, Resources paramResources)
  {
    mState = paramDrawableWrapperState;
    updateLocalState(paramResources);
  }
  
  private void inflateChildDrawable(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    Drawable localDrawable = null;
    int i = paramXmlPullParser.getDepth();
    for (;;)
    {
      int j = paramXmlPullParser.next();
      if ((j == 1) || ((j == 3) && (paramXmlPullParser.getDepth() <= i))) {
        break;
      }
      if (j == 2) {
        localDrawable = Drawable.createFromXmlInnerForDensity(paramResources, paramXmlPullParser, paramAttributeSet, mState.mSrcDensityOverride, paramTheme);
      }
    }
    if (localDrawable != null) {
      setDrawable(localDrawable);
    }
  }
  
  private void updateLocalState(Resources paramResources)
  {
    if ((mState != null) && (mState.mDrawableState != null)) {
      setDrawable(mState.mDrawableState.newDrawable(paramResources));
    }
  }
  
  private void updateStateFromTypedArray(TypedArray paramTypedArray)
  {
    DrawableWrapperState localDrawableWrapperState = mState;
    if (localDrawableWrapperState == null) {
      return;
    }
    mChangingConfigurations |= paramTypedArray.getChangingConfigurations();
    DrawableWrapperState.access$002(localDrawableWrapperState, paramTypedArray.extractThemeAttrs());
    if (paramTypedArray.hasValueOrEmpty(0)) {
      setDrawable(paramTypedArray.getDrawable(0));
    }
  }
  
  public void applyTheme(Resources.Theme paramTheme)
  {
    super.applyTheme(paramTheme);
    if ((mDrawable != null) && (mDrawable.canApplyTheme())) {
      mDrawable.applyTheme(paramTheme);
    }
    DrawableWrapperState localDrawableWrapperState = mState;
    if (localDrawableWrapperState == null) {
      return;
    }
    int i = getResourcesgetDisplayMetricsdensityDpi;
    if (i == 0) {
      i = 160;
    }
    localDrawableWrapperState.setDensity(i);
    if (mThemeAttrs != null)
    {
      paramTheme = paramTheme.resolveAttributes(mThemeAttrs, R.styleable.DrawableWrapper);
      updateStateFromTypedArray(paramTheme);
      paramTheme.recycle();
    }
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
  
  public void clearMutated()
  {
    super.clearMutated();
    if (mDrawable != null) {
      mDrawable.clearMutated();
    }
    mMutated = false;
  }
  
  public void draw(Canvas paramCanvas)
  {
    if (mDrawable != null) {
      mDrawable.draw(paramCanvas);
    }
  }
  
  public int getAlpha()
  {
    int i;
    if (mDrawable != null) {
      i = mDrawable.getAlpha();
    } else {
      i = 255;
    }
    return i;
  }
  
  public int getChangingConfigurations()
  {
    int i = super.getChangingConfigurations();
    int j;
    if (mState != null) {
      j = mState.getChangingConfigurations();
    } else {
      j = 0;
    }
    return i | j | mDrawable.getChangingConfigurations();
  }
  
  public ColorFilter getColorFilter()
  {
    Drawable localDrawable = getDrawable();
    if (localDrawable != null) {
      return localDrawable.getColorFilter();
    }
    return super.getColorFilter();
  }
  
  public Drawable.ConstantState getConstantState()
  {
    if ((mState != null) && (mState.canConstantState()))
    {
      mState.mChangingConfigurations = getChangingConfigurations();
      return mState;
    }
    return null;
  }
  
  public Drawable getDrawable()
  {
    return mDrawable;
  }
  
  public void getHotspotBounds(Rect paramRect)
  {
    if (mDrawable != null) {
      mDrawable.getHotspotBounds(paramRect);
    } else {
      paramRect.set(getBounds());
    }
  }
  
  public int getIntrinsicHeight()
  {
    int i;
    if (mDrawable != null) {
      i = mDrawable.getIntrinsicHeight();
    } else {
      i = -1;
    }
    return i;
  }
  
  public int getIntrinsicWidth()
  {
    int i;
    if (mDrawable != null) {
      i = mDrawable.getIntrinsicWidth();
    } else {
      i = -1;
    }
    return i;
  }
  
  public int getOpacity()
  {
    int i;
    if (mDrawable != null) {
      i = mDrawable.getOpacity();
    } else {
      i = -2;
    }
    return i;
  }
  
  public Insets getOpticalInsets()
  {
    Insets localInsets;
    if (mDrawable != null) {
      localInsets = mDrawable.getOpticalInsets();
    } else {
      localInsets = Insets.NONE;
    }
    return localInsets;
  }
  
  public void getOutline(Outline paramOutline)
  {
    if (mDrawable != null) {
      mDrawable.getOutline(paramOutline);
    } else {
      super.getOutline(paramOutline);
    }
  }
  
  public boolean getPadding(Rect paramRect)
  {
    boolean bool;
    if ((mDrawable != null) && (mDrawable.getPadding(paramRect))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasFocusStateSpecified()
  {
    boolean bool;
    if ((mDrawable != null) && (mDrawable.hasFocusStateSpecified())) {
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
    Object localObject = mState;
    if (localObject == null) {
      return;
    }
    int i = getDisplayMetricsdensityDpi;
    if (i == 0) {
      i = 160;
    }
    ((DrawableWrapperState)localObject).setDensity(i);
    mSrcDensityOverride = mSrcDensityOverride;
    localObject = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.DrawableWrapper);
    updateStateFromTypedArray((TypedArray)localObject);
    ((TypedArray)localObject).recycle();
    inflateChildDrawable(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
  }
  
  public void invalidateDrawable(Drawable paramDrawable)
  {
    paramDrawable = getCallback();
    if (paramDrawable != null) {
      paramDrawable.invalidateDrawable(this);
    }
  }
  
  public boolean isStateful()
  {
    boolean bool;
    if ((mDrawable != null) && (mDrawable.isStateful())) {
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
      mState = mutateConstantState();
      if (mDrawable != null) {
        mDrawable.mutate();
      }
      if (mState != null)
      {
        DrawableWrapperState localDrawableWrapperState = mState;
        Drawable.ConstantState localConstantState;
        if (mDrawable != null) {
          localConstantState = mDrawable.getConstantState();
        } else {
          localConstantState = null;
        }
        mDrawableState = localConstantState;
      }
      mMutated = true;
    }
    return this;
  }
  
  DrawableWrapperState mutateConstantState()
  {
    return mState;
  }
  
  protected void onBoundsChange(Rect paramRect)
  {
    if (mDrawable != null) {
      mDrawable.setBounds(paramRect);
    }
  }
  
  public boolean onLayoutDirectionChanged(int paramInt)
  {
    boolean bool;
    if ((mDrawable != null) && (mDrawable.setLayoutDirection(paramInt))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected boolean onLevelChange(int paramInt)
  {
    boolean bool;
    if ((mDrawable != null) && (mDrawable.setLevel(paramInt))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected boolean onStateChange(int[] paramArrayOfInt)
  {
    if ((mDrawable != null) && (mDrawable.isStateful()))
    {
      boolean bool = mDrawable.setState(paramArrayOfInt);
      if (bool) {
        onBoundsChange(getBounds());
      }
      return bool;
    }
    return false;
  }
  
  public void scheduleDrawable(Drawable paramDrawable, Runnable paramRunnable, long paramLong)
  {
    paramDrawable = getCallback();
    if (paramDrawable != null) {
      paramDrawable.scheduleDrawable(this, paramRunnable, paramLong);
    }
  }
  
  public void setAlpha(int paramInt)
  {
    if (mDrawable != null) {
      mDrawable.setAlpha(paramInt);
    }
  }
  
  public void setColorFilter(ColorFilter paramColorFilter)
  {
    if (mDrawable != null) {
      mDrawable.setColorFilter(paramColorFilter);
    }
  }
  
  public void setDrawable(Drawable paramDrawable)
  {
    if (mDrawable != null) {
      mDrawable.setCallback(null);
    }
    mDrawable = paramDrawable;
    if (paramDrawable != null)
    {
      paramDrawable.setCallback(this);
      paramDrawable.setVisible(isVisible(), true);
      paramDrawable.setState(getState());
      paramDrawable.setLevel(getLevel());
      paramDrawable.setBounds(getBounds());
      paramDrawable.setLayoutDirection(getLayoutDirection());
      if (mState != null) {
        mState.mDrawableState = paramDrawable.getConstantState();
      }
    }
    invalidateSelf();
  }
  
  public void setHotspot(float paramFloat1, float paramFloat2)
  {
    if (mDrawable != null) {
      mDrawable.setHotspot(paramFloat1, paramFloat2);
    }
  }
  
  public void setHotspotBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (mDrawable != null) {
      mDrawable.setHotspotBounds(paramInt1, paramInt2, paramInt3, paramInt4);
    }
  }
  
  public void setTintList(ColorStateList paramColorStateList)
  {
    if (mDrawable != null) {
      mDrawable.setTintList(paramColorStateList);
    }
  }
  
  public void setTintMode(PorterDuff.Mode paramMode)
  {
    if (mDrawable != null) {
      mDrawable.setTintMode(paramMode);
    }
  }
  
  public boolean setVisible(boolean paramBoolean1, boolean paramBoolean2)
  {
    boolean bool1 = super.setVisible(paramBoolean1, paramBoolean2);
    boolean bool2;
    if ((mDrawable != null) && (mDrawable.setVisible(paramBoolean1, paramBoolean2))) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    return bool1 | bool2;
  }
  
  public void unscheduleDrawable(Drawable paramDrawable, Runnable paramRunnable)
  {
    paramDrawable = getCallback();
    if (paramDrawable != null) {
      paramDrawable.unscheduleDrawable(this, paramRunnable);
    }
  }
  
  static abstract class DrawableWrapperState
    extends Drawable.ConstantState
  {
    int mChangingConfigurations;
    int mDensity;
    Drawable.ConstantState mDrawableState;
    int mSrcDensityOverride;
    private int[] mThemeAttrs;
    
    DrawableWrapperState(DrawableWrapperState paramDrawableWrapperState, Resources paramResources)
    {
      int i = 160;
      mDensity = 160;
      int j = 0;
      mSrcDensityOverride = 0;
      if (paramDrawableWrapperState != null)
      {
        mThemeAttrs = mThemeAttrs;
        mChangingConfigurations = mChangingConfigurations;
        mDrawableState = mDrawableState;
        mSrcDensityOverride = mSrcDensityOverride;
      }
      if (paramResources != null) {}
      for (j = getDisplayMetricsdensityDpi;; j = mDensity)
      {
        break;
        if (paramDrawableWrapperState == null) {
          break;
        }
      }
      if (j == 0) {
        j = i;
      }
      mDensity = j;
    }
    
    public boolean canApplyTheme()
    {
      boolean bool;
      if ((mThemeAttrs == null) && ((mDrawableState == null) || (!mDrawableState.canApplyTheme())) && (!super.canApplyTheme())) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public boolean canConstantState()
    {
      boolean bool;
      if (mDrawableState != null) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public int getChangingConfigurations()
    {
      int i = mChangingConfigurations;
      int j;
      if (mDrawableState != null) {
        j = mDrawableState.getChangingConfigurations();
      } else {
        j = 0;
      }
      return i | j;
    }
    
    public Drawable newDrawable()
    {
      return newDrawable(null);
    }
    
    public abstract Drawable newDrawable(Resources paramResources);
    
    void onDensityChanged(int paramInt1, int paramInt2) {}
    
    public final void setDensity(int paramInt)
    {
      if (mDensity != paramInt)
      {
        int i = mDensity;
        mDensity = paramInt;
        onDensityChanged(i, paramInt);
      }
    }
  }
}
