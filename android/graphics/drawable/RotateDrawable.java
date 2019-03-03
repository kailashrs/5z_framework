package android.graphics.drawable;

import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.MathUtils;
import android.util.TypedValue;
import com.android.internal.R.styleable;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class RotateDrawable
  extends DrawableWrapper
{
  private static final int MAX_LEVEL = 10000;
  private RotateState mState;
  
  public RotateDrawable()
  {
    this(new RotateState(null, null), null);
  }
  
  private RotateDrawable(RotateState paramRotateState, Resources paramResources)
  {
    super(paramRotateState, paramResources);
    mState = paramRotateState;
  }
  
  private void updateStateFromTypedArray(TypedArray paramTypedArray)
  {
    RotateState localRotateState = mState;
    if (localRotateState == null) {
      return;
    }
    mChangingConfigurations |= paramTypedArray.getChangingConfigurations();
    RotateState.access$002(localRotateState, paramTypedArray.extractThemeAttrs());
    boolean bool1 = paramTypedArray.hasValue(4);
    boolean bool2 = false;
    TypedValue localTypedValue;
    float f;
    if (bool1)
    {
      localTypedValue = paramTypedArray.peekValue(4);
      if (type == 6) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      mPivotXRel = bool1;
      if (mPivotXRel) {
        f = localTypedValue.getFraction(1.0F, 1.0F);
      } else {
        f = localTypedValue.getFloat();
      }
      mPivotX = f;
    }
    if (paramTypedArray.hasValue(5))
    {
      localTypedValue = paramTypedArray.peekValue(5);
      bool1 = bool2;
      if (type == 6) {
        bool1 = true;
      }
      mPivotYRel = bool1;
      if (mPivotYRel) {
        f = localTypedValue.getFraction(1.0F, 1.0F);
      } else {
        f = localTypedValue.getFloat();
      }
      mPivotY = f;
    }
    mFromDegrees = paramTypedArray.getFloat(2, mFromDegrees);
    mToDegrees = paramTypedArray.getFloat(3, mToDegrees);
    mCurrentDegrees = mFromDegrees;
  }
  
  private void verifyRequiredAttributes(TypedArray paramTypedArray)
    throws XmlPullParserException
  {
    if ((getDrawable() == null) && ((mState.mThemeAttrs == null) || (mState.mThemeAttrs[1] == 0)))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramTypedArray.getPositionDescription());
      localStringBuilder.append(": <rotate> tag requires a 'drawable' attribute or child tag defining a drawable");
      throw new XmlPullParserException(localStringBuilder.toString());
    }
  }
  
  /* Error */
  public void applyTheme(Resources.Theme paramTheme)
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokespecial 129	android/graphics/drawable/DrawableWrapper:applyTheme	(Landroid/content/res/Resources$Theme;)V
    //   5: aload_0
    //   6: getfield 26	android/graphics/drawable/RotateDrawable:mState	Landroid/graphics/drawable/RotateDrawable$RotateState;
    //   9: astore_2
    //   10: aload_2
    //   11: ifnonnull +4 -> 15
    //   14: return
    //   15: aload_2
    //   16: invokestatic 104	android/graphics/drawable/RotateDrawable$RotateState:access$000	(Landroid/graphics/drawable/RotateDrawable$RotateState;)[I
    //   19: ifnull +50 -> 69
    //   22: aload_1
    //   23: aload_2
    //   24: invokestatic 104	android/graphics/drawable/RotateDrawable$RotateState:access$000	(Landroid/graphics/drawable/RotateDrawable$RotateState;)[I
    //   27: getstatic 135	com/android/internal/R$styleable:RotateDrawable	[I
    //   30: invokevirtual 141	android/content/res/Resources$Theme:resolveAttributes	([I[I)Landroid/content/res/TypedArray;
    //   33: astore_1
    //   34: aload_0
    //   35: aload_1
    //   36: invokespecial 143	android/graphics/drawable/RotateDrawable:updateStateFromTypedArray	(Landroid/content/res/TypedArray;)V
    //   39: aload_0
    //   40: aload_1
    //   41: invokespecial 145	android/graphics/drawable/RotateDrawable:verifyRequiredAttributes	(Landroid/content/res/TypedArray;)V
    //   44: aload_1
    //   45: invokevirtual 148	android/content/res/TypedArray:recycle	()V
    //   48: goto +21 -> 69
    //   51: astore_2
    //   52: goto +11 -> 63
    //   55: astore_2
    //   56: aload_2
    //   57: invokestatic 152	android/graphics/drawable/RotateDrawable:rethrowAsRuntimeException	(Ljava/lang/Exception;)V
    //   60: goto -16 -> 44
    //   63: aload_1
    //   64: invokevirtual 148	android/content/res/TypedArray:recycle	()V
    //   67: aload_2
    //   68: athrow
    //   69: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	70	0	this	RotateDrawable
    //   0	70	1	paramTheme	Resources.Theme
    //   9	15	2	localRotateState	RotateState
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
    Rect localRect = localDrawable.getBounds();
    int i = right;
    int j = left;
    int k = bottom;
    int m = top;
    RotateState localRotateState = mState;
    float f1;
    if (mPivotXRel) {
      f1 = (i - j) * mPivotX;
    } else {
      f1 = mPivotX;
    }
    float f2;
    if (mPivotYRel) {
      f2 = (k - m) * mPivotY;
    } else {
      f2 = mPivotY;
    }
    k = paramCanvas.save();
    paramCanvas.rotate(mCurrentDegrees, left + f1, top + f2);
    localDrawable.draw(paramCanvas);
    paramCanvas.restoreToCount(k);
  }
  
  public float getFromDegrees()
  {
    return mState.mFromDegrees;
  }
  
  public float getPivotX()
  {
    return mState.mPivotX;
  }
  
  public float getPivotY()
  {
    return mState.mPivotY;
  }
  
  public float getToDegrees()
  {
    return mState.mToDegrees;
  }
  
  public void inflate(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet, Resources.Theme paramTheme)
    throws XmlPullParserException, IOException
  {
    TypedArray localTypedArray = obtainAttributes(paramResources, paramTheme, paramAttributeSet, R.styleable.RotateDrawable);
    super.inflate(paramResources, paramXmlPullParser, paramAttributeSet, paramTheme);
    updateStateFromTypedArray(localTypedArray);
    verifyRequiredAttributes(localTypedArray);
    localTypedArray.recycle();
  }
  
  public boolean isPivotXRelative()
  {
    return mState.mPivotXRel;
  }
  
  public boolean isPivotYRelative()
  {
    return mState.mPivotYRel;
  }
  
  DrawableWrapper.DrawableWrapperState mutateConstantState()
  {
    mState = new RotateState(mState, null);
    return mState;
  }
  
  protected boolean onLevelChange(int paramInt)
  {
    super.onLevelChange(paramInt);
    float f = paramInt / 10000.0F;
    f = MathUtils.lerp(mState.mFromDegrees, mState.mToDegrees, f);
    mState.mCurrentDegrees = f;
    invalidateSelf();
    return true;
  }
  
  public void setFromDegrees(float paramFloat)
  {
    if (mState.mFromDegrees != paramFloat)
    {
      mState.mFromDegrees = paramFloat;
      invalidateSelf();
    }
  }
  
  public void setPivotX(float paramFloat)
  {
    if (mState.mPivotX != paramFloat)
    {
      mState.mPivotX = paramFloat;
      invalidateSelf();
    }
  }
  
  public void setPivotXRelative(boolean paramBoolean)
  {
    if (mState.mPivotXRel != paramBoolean)
    {
      mState.mPivotXRel = paramBoolean;
      invalidateSelf();
    }
  }
  
  public void setPivotY(float paramFloat)
  {
    if (mState.mPivotY != paramFloat)
    {
      mState.mPivotY = paramFloat;
      invalidateSelf();
    }
  }
  
  public void setPivotYRelative(boolean paramBoolean)
  {
    if (mState.mPivotYRel != paramBoolean)
    {
      mState.mPivotYRel = paramBoolean;
      invalidateSelf();
    }
  }
  
  public void setToDegrees(float paramFloat)
  {
    if (mState.mToDegrees != paramFloat)
    {
      mState.mToDegrees = paramFloat;
      invalidateSelf();
    }
  }
  
  static final class RotateState
    extends DrawableWrapper.DrawableWrapperState
  {
    float mCurrentDegrees = 0.0F;
    float mFromDegrees = 0.0F;
    float mPivotX = 0.5F;
    boolean mPivotXRel = true;
    float mPivotY = 0.5F;
    boolean mPivotYRel = true;
    private int[] mThemeAttrs;
    float mToDegrees = 360.0F;
    
    RotateState(RotateState paramRotateState, Resources paramResources)
    {
      super(paramResources);
      if (paramRotateState != null)
      {
        mPivotXRel = mPivotXRel;
        mPivotX = mPivotX;
        mPivotYRel = mPivotYRel;
        mPivotY = mPivotY;
        mFromDegrees = mFromDegrees;
        mToDegrees = mToDegrees;
        mCurrentDegrees = mCurrentDegrees;
      }
    }
    
    public Drawable newDrawable(Resources paramResources)
    {
      return new RotateDrawable(this, paramResources, null);
    }
  }
}
