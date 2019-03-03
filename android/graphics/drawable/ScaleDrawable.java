package android.graphics.drawable;

import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import com.android.internal.R.styleable;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ScaleDrawable
  extends DrawableWrapper
{
  private static final int MAX_LEVEL = 10000;
  private ScaleState mState;
  private final Rect mTmpRect = new Rect();
  
  ScaleDrawable()
  {
    this(new ScaleState(null, null), null);
  }
  
  public ScaleDrawable(Drawable paramDrawable, int paramInt, float paramFloat1, float paramFloat2)
  {
    this(new ScaleState(null, null), null);
    mState.mGravity = paramInt;
    mState.mScaleWidth = paramFloat1;
    mState.mScaleHeight = paramFloat2;
    setDrawable(paramDrawable);
  }
  
  private ScaleDrawable(ScaleState paramScaleState, Resources paramResources)
  {
    super(paramScaleState, paramResources);
    mState = paramScaleState;
    updateLocalState();
  }
  
  private static float getPercent(TypedArray paramTypedArray, int paramInt, float paramFloat)
  {
    int i = paramTypedArray.getType(paramInt);
    if ((i != 6) && (i != 0))
    {
      paramTypedArray = paramTypedArray.getString(paramInt);
      if ((paramTypedArray != null) && (paramTypedArray.endsWith("%"))) {
        return Float.parseFloat(paramTypedArray.substring(0, paramTypedArray.length() - 1)) / 100.0F;
      }
      return paramFloat;
    }
    return paramTypedArray.getFraction(paramInt, 1, 1, paramFloat);
  }
  
  private void updateLocalState()
  {
    setLevel(mState.mInitialLevel);
  }
  
  private void updateStateFromTypedArray(TypedArray paramTypedArray)
  {
    ScaleState localScaleState = mState;
    if (localScaleState == null) {
      return;
    }
    mChangingConfigurations |= paramTypedArray.getChangingConfigurations();
    ScaleState.access$002(localScaleState, paramTypedArray.extractThemeAttrs());
    mScaleWidth = getPercent(paramTypedArray, 1, mScaleWidth);
    mScaleHeight = getPercent(paramTypedArray, 2, mScaleHeight);
    mGravity = paramTypedArray.getInt(3, mGravity);
    mUseIntrinsicSizeAsMin = paramTypedArray.getBoolean(4, mUseIntrinsicSizeAsMin);
    mInitialLevel = paramTypedArray.getInt(5, mInitialLevel);
  }
  
  private void verifyRequiredAttributes(TypedArray paramTypedArray)
    throws XmlPullParserException
  {
    if ((getDrawable() == null) && ((mState.mThemeAttrs == null) || (mState.mThemeAttrs[0] == 0)))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramTypedArray.getPositionDescription());
      localStringBuilder.append(": <scale> tag requires a 'drawable' attribute or child tag defining a drawable");
      throw new XmlPullParserException(localStringBuilder.toString());
    }
  }
  
  /* Error */
  public void applyTheme(Resources.Theme paramTheme)
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokespecial 164	android/graphics/drawable/DrawableWrapper:applyTheme	(Landroid/content/res/Resources$Theme;)V
    //   5: aload_0
    //   6: getfield 26	android/graphics/drawable/ScaleDrawable:mState	Landroid/graphics/drawable/ScaleDrawable$ScaleState;
    //   9: astore_2
    //   10: aload_2
    //   11: ifnonnull +4 -> 15
    //   14: return
    //   15: aload_2
    //   16: invokestatic 140	android/graphics/drawable/ScaleDrawable$ScaleState:access$000	(Landroid/graphics/drawable/ScaleDrawable$ScaleState;)[I
    //   19: ifnull +50 -> 69
    //   22: aload_1
    //   23: aload_2
    //   24: invokestatic 140	android/graphics/drawable/ScaleDrawable$ScaleState:access$000	(Landroid/graphics/drawable/ScaleDrawable$ScaleState;)[I
    //   27: getstatic 170	com/android/internal/R$styleable:ScaleDrawable	[I
    //   30: invokevirtual 176	android/content/res/Resources$Theme:resolveAttributes	([I[I)Landroid/content/res/TypedArray;
    //   33: astore_1
    //   34: aload_0
    //   35: aload_1
    //   36: invokespecial 178	android/graphics/drawable/ScaleDrawable:updateStateFromTypedArray	(Landroid/content/res/TypedArray;)V
    //   39: aload_0
    //   40: aload_1
    //   41: invokespecial 180	android/graphics/drawable/ScaleDrawable:verifyRequiredAttributes	(Landroid/content/res/TypedArray;)V
    //   44: aload_1
    //   45: invokevirtual 183	android/content/res/TypedArray:recycle	()V
    //   48: goto +21 -> 69
    //   51: astore_2
    //   52: goto +11 -> 63
    //   55: astore_2
    //   56: aload_2
    //   57: invokestatic 187	android/graphics/drawable/ScaleDrawable:rethrowAsRuntimeException	(Ljava/lang/Exception;)V
    //   60: goto -16 -> 44
    //   63: aload_1
    //   64: invokevirtual 183	android/content/res/TypedArray:recycle	()V
    //   67: aload_2
    //   68: athrow
    //   69: aload_0
    //   70: invokespecial 52	android/graphics/drawable/ScaleDrawable:updateLocalState	()V
    //   73: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	74	0	this	ScaleDrawable
    //   0	74	1	paramTheme	Resources.Theme
    //   9	15	2	localScaleState	ScaleState
    //   51	1	2	localObject	Object
    //   55	13	2	localXmlPullParserException	XmlPullParserException
    // Exception table:
    //   from	to	target	type
    //   34	44	51	finally
    //   56	60	51	finally
    //   34	44	55	org/xmlpull/v1/XmlPullParserException
  }
  
  public void draw(Canvas paramCanvas)
  {
    Drawable localDrawable = getDrawable();
    if ((localDrawable != null) && (localDrawable.getLevel() != 0)) {
      localDrawable.draw(paramCanvas);
    }
  }
  
  public int getOpacity()
  {
    Drawable localDrawable = getDrawable();
    if (localDrawable.getLevel() == 0) {
      return -2;
    }
    int i = localDrawable.getOpacity();
    if ((i == -1) && (localDrawable.getLevel() < 10000)) {
      return -3;
    }
    return i;
  }
  
  public void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    TypedArray localTypedArray = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.ScaleDrawable);
    super.inflate(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
    updateStateFromTypedArray(localTypedArray);
    verifyRequiredAttributes(localTypedArray);
    localTypedArray.recycle();
    updateLocalState();
  }
  
  DrawableWrapper.DrawableWrapperState mutateConstantState()
  {
    mState = new ScaleState(mState, null);
    return mState;
  }
  
  protected void onBoundsChange(Rect paramRect)
  {
    Drawable localDrawable = getDrawable();
    Rect localRect = mTmpRect;
    boolean bool = mState.mUseIntrinsicSizeAsMin;
    int i = getLevel();
    int j = paramRect.width();
    float f = mState.mScaleWidth;
    int k = 0;
    int m = j;
    if (f > 0.0F)
    {
      if (bool) {
        m = localDrawable.getIntrinsicWidth();
      } else {
        m = 0;
      }
      m = j - (int)((j - m) * (10000 - i) * mState.mScaleWidth / 10000.0F);
    }
    int n = paramRect.height();
    j = n;
    if (mState.mScaleHeight > 0.0F)
    {
      j = k;
      if (bool) {
        j = localDrawable.getIntrinsicHeight();
      }
      j = n - (int)((n - j) * (10000 - i) * mState.mScaleHeight / 10000.0F);
    }
    k = getLayoutDirection();
    Gravity.apply(mState.mGravity, m, j, paramRect, localRect, k);
    if ((m > 0) && (j > 0)) {
      localDrawable.setBounds(left, top, right, bottom);
    }
  }
  
  protected boolean onLevelChange(int paramInt)
  {
    super.onLevelChange(paramInt);
    onBoundsChange(getBounds());
    invalidateSelf();
    return true;
  }
  
  static final class ScaleState
    extends DrawableWrapper.DrawableWrapperState
  {
    private static final float DO_NOT_SCALE = -1.0F;
    int mGravity = 3;
    int mInitialLevel = 0;
    float mScaleHeight = -1.0F;
    float mScaleWidth = -1.0F;
    private int[] mThemeAttrs;
    boolean mUseIntrinsicSizeAsMin = false;
    
    ScaleState(ScaleState paramScaleState, Resources paramResources)
    {
      super(paramResources);
      if (paramScaleState != null)
      {
        mScaleWidth = mScaleWidth;
        mScaleHeight = mScaleHeight;
        mGravity = mGravity;
        mUseIntrinsicSizeAsMin = mUseIntrinsicSizeAsMin;
        mInitialLevel = mInitialLevel;
      }
    }
    
    public Drawable newDrawable(Resources paramResources)
    {
      return new ScaleDrawable(this, paramResources, null);
    }
  }
}
